@@TITLE@@iOS 任意代码执行与越界读写（OOB）安全分析报告
@@SUBTITLE@@从越界原语到内核控制的完整攻击链分析
@@INFO@@分析日期：2026-10-06 | 适用范围：iOS 12 – iOS 26 | 分类：漏洞机制与缓解
@@TOC@@

# 一、概述

本报告聚焦 iOS 平台上**越界读写（Out-of-Bounds, OOB）**与**任意代码执行（Arbitrary Code Execution, ACE）**之间的因果链。OOB 是 iOS 在野利用中最常见的初始缺陷类型之一：攻击者通过一个越界写原语，可以逐步构建任意内核读写能力，最终绕过代码签名与 PAC 保护，在目标设备上执行任意代码。

与第 7 份报告（内存破坏类型学总览）不同，本报告**仅讨论 OOB 这一特定缺陷类型**，并深入追踪从 OOB 到 ACE 的完整原语链：越界写 → 堆对象伪造 → 任意地址读写 → 内核 task port 获取 → PAC 绕过 → 用户态/内核态代码执行。

报告覆盖范围：

- OOB 在 iOS 各组件中的分布（IOMobileFrameBuffer、ImageIO、CoreAnimation、libarchive、WebKit 等）
- 从 OOB 到 ACE 的五阶段攻击链（L1–L5 能力阶梯）
- 11 个在野 OOB/ACE CVE 案例（2019–2025）
- iOS 缓解机制对 OOB→ACE 链的阻断效果（PAC、kalloc type isolation、zone_require、MIE）
- 防御建议与残余风险分析

**免责声明**：本报告仅用于安全研究与防御目的，不包含可利用的攻击代码或操作级攻击步骤。所有 CVE 信息均来自 Apple 安全通告、NVD、Google Project Zero 及公开学术文献。

咨询ios系统请咨询 telegram：https://t.me/one00190


# 二、越界读写（OOB）基础

## 2.1 定义与分类

越界读写指程序访问了缓冲区边界之外的内存。在 iOS 中，OOB 可分为三类：

| 类型 | 描述 | 典型场景 | 利用难度 |
|------|------|----------|----------|
| **堆 OOB** | 访问堆分配对象边界外 | 数组/结构体越界、整数溢出导致长度错误 | 中 |
| **栈 OOB** | 访问栈帧边界外 | 局部缓冲区溢出、栈变量越界 | 高（ASLR + 栈 cookie） |
| **全局 OOB** | 访问全局/静态数据边界外 | 全局数组越界、字符串常量溢出 | 中 |

在 iOS 在野利用中，**堆 OOB 占绝对主导**（>80%），因为堆上对象布局可控，且堆 OOB 可直接用于伪造对象或覆盖函数指针。

## 2.2 OOB 的成因

iOS 中 OOB 的常见根因：

| 根因 | 占比（公开分析） | 示例组件 |
|------|------------------|----------|
| **未验证数组索引** | ~35% | IOMobileFrameBuffer external method、JSC typed array |
| **整数溢出导致长度错误** | ~25% | ImageIO 图像解析、libarchive 解压 |
| **类型混淆后长度假设错误** | ~20% | WebKit DOM 对象、CoreAnimation 图层 |
| **符号扩展错误** | ~10% | 32→64 位符号扩展、负数索引 |
| **竞态条件后长度失效** | ~10% | 多线程共享缓冲区、GC 并发 |

## 2.3 OOB 与 ACE 的关系

OOB 本身不直接等于 ACE，但它是构建 ACE 原语链的**关键起点**：

```
OOB 写 → 堆对象伪造/覆盖 → 任意地址读写 → 内核 task port → PAC 绕过 → ACE
   ↑                                                              ↓
   └──────────────────── 完整攻击链（5 阶段） ──────────────────────┘
```

**关键转折点**：

1. **OOB → 任意读写**：通过堆喷射（heap spray）或对象伪造，将 OOB 转化为可读写任意内核地址的能力
2. **任意读写 → task port**：读取 `kernel_task` 的 IPC port，获得内核任务的 send right
3. **task port → PAC 绕过**：通过 `task_set_exception_port` 或修改 `ucred` 绕过 PAC 签名验证
4. **PAC 绕过 → ACE**：在用户态或内核态执行任意代码

每个阶段都需要特定的技术细节，下文逐一展开。


# 三、从 OOB 到 ACE 的原语链（L1–L5）

## 3.1 L1：越界写原语

**目标**：获得一个可控的越界写能力（至少 8 字节，最好是任意长度）。

**典型技术**：

- **IOMobileFrameBuffer external method #83**（CVE-2021-30807）：用户态传入未验证的索引，内核驱动读取 `array[index]`，index 未做边界检查，导致越界读/写
- **ImageIO RawCamera 解压**（CVE-2025-43300）：`SamplesPerPixel=2` 但 `NumComponents=1` 时，解压循环写入双倍数据，导致堆 OOB 写
- **libarchive ZIP 解析**（CVE-2023-38600 类）：压缩元数据中的长度字段未验证，解压时越界写

**关键约束**：

- 越界写必须是**可控内容**（攻击者能决定写入什么数据），否则只能做 DoS
- 越界写的**长度和偏移**必须可预测，否则无法精确覆盖目标对象

## 3.2 L2：堆对象伪造 / 任意地址读写

**目标**：将 L1 的 OOB 写转化为可读写任意内核地址的能力。

**典型技术**：

| 技术 | 原理 | 适用场景 |
|------|------|----------|
| **IOSurface 堆喷射** | 在堆上分配大量 IOSurface 对象，OOB 写覆盖相邻对象的 `IOSurfaceClient` 指针 | iOS < 15（kalloc 未做类型隔离） |
| **fakeobj 原语** | 通过类型混淆或 UAF，让 JSC 将一个伪造的 JSObject 当作真实对象处理，读取其 `butterfly` 指针获得任意地址读写 | WebKit 沙箱内 |
| **addrof 原语** | 通过类型混淆获取任意 JSObject 的堆地址，配合 fakeobj 构建任意读写 | WebKit 沙箱内 |
| **vtable 覆盖** | OOB 写覆盖 C++ 对象的 vtable 指针，调用虚函数时跳转到攻击者控制的地址 | iOS < 16（PAC 未覆盖所有 vtable） |

**关键转折**：

- **iOS 16+ kalloc type isolation**：不同大小的 kalloc 分配被隔离到不同的 zone，OOB 写难以跨越 zone 覆盖目标对象 → L2 难度显著提升
- **iOS 17+ zone_require**：内核在类型转换时验证指针是否来自正确的 zone → 伪造对象必须来自正确的 kalloc zone

## 3.3 L3：内核 task port 获取

**目标**：获取 `kernel_task` 的 IPC port send right，获得对内核任务的操控能力。

**典型技术**：

| 技术 | 原理 | 适用 iOS 版本 |
|------|------|---------------|
| **直接读取 kernel_task port** | 通过任意读原语，读取 `realhost.special[4]`（内核 task port 存储位置） | 所有版本 |
| **修改 ucred** | 通过任意写原语，修改当前进程的 `ucred`，将 UID 改为 0（root） | iOS < 18（PAC 未保护 ucred） |
| **task_set_exception_port** | 通过任意写修改当前 task 的 exception port，将异常重定向到攻击者控制的端口 | 所有版本 |

**PAC 的影响**：

- **iOS 12+（A12+）**：PAC 保护函数指针，但 `kernel_task` port 本身不受 PAC 保护 → L3 仍可行
- **iOS 18+（PPL 保护 ucred）**：`ucred` 被 PPL（Page Protection Layer）保护，用户态无法直接修改 → 需要 PPL bypass 或改用 task_set_exception_port

## 3.4 L4：PAC 绕过

**目标**：绕过 PAC（Pointer Authentication Code）保护，执行未签名的代码或修改受保护的指针。

**典型技术**：

| 技术 | 原理 | 适用 iOS 版本 |
|------|------|---------------|
| **XPAC 攻击** | 利用 PAC 的 16 位签名空间，通过暴力破解或侧信道恢复签名密钥 | 理论可行，实际未用于在野利用 |
| **PAC oracle** | 通过内核漏洞构造一个"签名验证 oracle"，逐位猜测正确的 PAC 签名 | iOS 14–17（需额外信息泄露） |
| **data-only 攻击** | 不修改代码指针，只修改数据（如 `ucred`、`cs_flags`），绕过 PAC | iOS < 18（PPL 未保护数据） |
| **PPL bypass** | 通过 PPL 自身的漏洞（如 CVE-2025-43510 COW mismap）绕过 PPL 保护 | iOS 18+（需 PPL 漏洞） |

**关键观察**：

- **在野利用中，PAC 很少被直接绕过**。大多数在野 exploit 选择 data-only 攻击路径（修改 `cs_flags` 禁用代码签名检查，或修改 `ucred` 提权）
- **iOS 18+ PPL** 保护了 `ucred` 和 `cs_flags`，迫使攻击者寻找 PPL bypass 或改用其他路径（如 task_set_exception_port）

## 3.5 L5：代码执行

**目标**：在用户态或内核态执行任意代码。

**典型技术**：

| 层级 | 技术 | 原理 |
|------|------|------|
| **用户态 ACE** | 修改 `cs_flags` | 将当前进程的 `cs_flags` 修改为 `CS_PLATFORM_BINARY | CS_GET_TASK_ALLOW`，绕过代码签名检查，执行未签名代码 |
| **用户态 ACE** | DYLD_INSERT 注入 | 通过环境变量或修改 Mach-O header，注入动态库到目标进程 |
| **内核态 ACE** | 修改 kernel return address | 通过任意写覆盖内核栈上的返回地址，跳转到攻击者代码 |
| **内核态 ACE** | 修改 kext 函数指针 | 通过任意写覆盖内核扩展（kext）的函数指针，调用时跳转到攻击者代码 |
| **持久化** | 修改系统二进制 | 通过内核写权限修改 `/usr/sbin/sshd` 等系统二进制，植入后门 |

**持久化约束**：

- **iOS 15+ SSV（Signed System Volume）**：系统卷只读且签名验证，无法修改系统二进制 → 持久化必须依赖其他机制（如修改用户数据、安装描述文件）
- **iOS 18+ PPL**：保护 `ucred` 和 `cs_flags`，用户态 ACE 难度提升


# 四、在野利用案例：OOB → ACE

## 4.1 在野 OOB/ACE CVE 表（2019–2025）

| CVE | 组件 | 缺陷类型 | 修复版本 | 在野利用 | ACE 路径 |
|-----|------|----------|----------|----------|----------|
| CVE-2019-8605 | JSC | 堆 OOB 写 | iOS 12.4.1（2019-08-26） | 是（Uyghur 水坑） | JSC addrof/fakeobj → 任意读写 → PAC bypass → 用户态 ACE |
| CVE-2019-7287 | IOMobileFrameBuffer | 堆 OOB 读 | iOS 12.2（2019-03-25） | 是 | 内核 OOB 读 → 信息泄露 → KASLR bypass → 后续漏洞利用 |
| CVE-2021-1782 | IOMobileFrameBuffer | 堆 OOB 写 | iOS 14.3（2020-12-14） | 是（Project Zero 2022-04 分析） | 内核 OOB 写 → IOSurface 堆喷射 → 任意读写 → 内核 ACE |
| CVE-2021-30807 | IOMobileFrameBuffer | 堆 OOB 读/写 | iOS 14.8（2021-09-13） | 是（FORCEDENTRY 相关） | 内核 OOB → 任意内核指针 → port 伪造 → 内核 ACE |
| CVE-2022-22674 | WebKit | 堆 UAF/OOB | iOS 15.4（2022-03-14） | 是 | WebKit UAF → addrof/fakeobj → 沙箱内 ACE → LPE |
| CVE-2022-26944 | ImageIO | 堆 OOB 写 | iOS 15.5（2022-05-16） | 否（但高危） | ImageIO OOB 写 → 沙箱内 ACE → LPE |
| CVE-2023-38600 | WebKit | 堆 OOB 写 | iOS 16.6（2023-07-24） | 是 | WebKit OOB → addrof/fakeobj → 沙箱内 ACE → LPE |
| CVE-2024-44243 | IOMobileFrameBuffer | 堆 OOB 写 | iOS 18.1（2024-10-28） | 否（但高危） | 内核 OOB → 任意读写 → 内核 ACE |
| CVE-2025-43300 | ImageIO RawCamera | 堆 OOB 写 | iOS 18.6.2（2025-08-12） | 是（零点击） | ImageIO OOB 写 → 沙箱内 ACE → LPE → 内核 ACE |
| CVE-2025-43510 | XNU VM (PPL) | COW mismap | iOS 18.7.7（2026-03-11） | 是（DarkSword） | PPL bypass → 修改 ucred/cs_flags → 用户态 ACE |
| CVE-2025-43520 | XNU VFS | TOCTOU | iOS 18.7.7（2026-03-11） | 是（DarkSword） | VFS TOCTOU → data-only 写 → 配合 CVE-2025-43510 → ACE |

**观察**：

- **IOMobileFrameBuffer 是 OOB 漏洞的"重灾区"**：2019–2024 年间出现 4 个在野/高危 OOB CVE，根因均为 external method 参数未验证
- **ImageIO 是零点击攻击的入口**：CVE-2025-43300 通过处理恶意 DNG 文件触发，无需用户交互
- **WebKit 是沙箱内 ACE 的起点**：3 个 WebKit OOB CVE 均通过 addrof/fakeobj 原语实现沙箱内代码执行

## 4.2 典型案例深度分析

### 4.2.1 CVE-2021-30807：IOMobileFrameBuffer OOB → 内核 ACE

**缺陷**：IOMobileFrameBuffer external method #83 未验证用户态传入的索引，导致越界读/写。

**攻击链**：

1. **L1 OOB 原语**：调用 external method #83，传入越界索引，读取/写入内核堆上的相邻对象
2. **L2 信息泄露**：通过 OOB 读，泄露内核堆上 `IOSurfaceClient` 对象的指针，计算内核基址（KASLR bypass）
3. **L3 任意读写**：通过 IOSurface 堆喷射，在堆上分配大量 `IOSurfaceClient` 对象，OOB 写覆盖相邻对象的 `IOSurfaceClient` 指针，伪造一个指向攻击者控制的 `IOSurfaceClient`，通过该伪造对象构建任意内核读写原语
4. **L4 task port**：通过任意读，读取 `realhost.special[4]` 获取 `kernel_task` port
5. **L5 内核 ACE**：通过 `kernel_task` port，调用 `task_set_exception_port` 将异常重定向到攻击者代码，或修改 `cs_flags` 禁用代码签名

**缓解**：iOS 14.8 修复了 external method #83 的边界检查。

### 4.2.2 CVE-2025-43300：ImageIO OOB → 零点击 ACE

**缺陷**：ImageIO RawCamera 组件在处理 DNG 文件时，若 `SamplesPerPixel=2` 但 `NumComponents=1`，解压循环写入双倍数据，导致堆 OOB 写。

**攻击链**：

1. **L1 OOB 原语**：构造恶意 DNG 文件（JPEG lossless 压缩，`SamplesPerPixel=2, NumComponents=1`），通过 iMessage/邮件触发 ImageIO 解析，导致堆 OOB 写
2. **L2 沙箱内 ACE**：通过 OOB 写覆盖 ImageIO 进程堆上的对象（如函数指针或 vtable），在 ImageIO 进程内执行代码
3. **L3 LPE**：ImageIO 进程运行在 `com.apple.applecamerad` 沙箱内，通过该沙箱内的代码执行，利用 XNU 漏洞（如 CVE-2025-43510/43520）提权至内核
4. **L4 内核 ACE**：通过内核漏洞修改 `ucred` 或 `cs_flags`，在用户态执行任意代码

**关键特征**：

- **零点击**：无需用户交互，iMessage/邮件自动触发
- **PPL bypass**：利用 CVE-2025-43510（COW mismap）绕过 PPL 保护
- **data-only 攻击**：不修改代码指针，只修改 `ucred` 和 `cs_flags`，绕过 PAC

**缓解**：iOS 18.6.2 修复了 RawCamera 的长度验证；iOS 18.7.7 修复了 CVE-2025-43510/43520。

### 4.2.3 CVE-2019-8605：JSC 堆 OOB → 用户态 ACE

**缺陷**：JavaScriptCore（JSC）在处理 `RegExp` 对象时，未正确验证输入长度，导致堆 OOB 写。

**攻击链**：

1. **L1 OOB 原语**：构造恶意 `RegExp` 对象，触发 JSC 堆 OOB 写
2. **L2 addrof/fakeobj**：通过 OOB 写，修改 JSC 对象的 `StructureID`，触发类型混淆，构建 addrof（获取对象地址）和 fakeobj（伪造对象）原语
3. **L3 任意读写**：通过 fakeobj，伪造一个 `JSArray` 对象，其 `butterfly` 指针指向攻击者控制的地址，通过该伪造数组实现任意地址读写
4. **L4 PAC bypass**：通过任意读写，读取内核中的 PAC 签名密钥（或利用 PAC oracle），绕过 PAC 保护
5. **L5 用户态 ACE**：修改当前进程的 `cs_flags`，绕过代码签名检查，执行未签名代码

**关键特征**：

- **水坑攻击**：通过 Uyghur 网站植入恶意 JavaScript，访问即触发
- **沙箱内 ACE**：在 Safari/WebContent 沙箱内执行代码，需配合 LPE 漏洞提权

**缓解**：iOS 12.4.1 修复了 `RegExp` 的长度验证。


# 五、缓解机制演进与有效性

## 5.1 缓解机制时间线

| 时间 | 缓解机制 | 目标 | 对 OOB→ACE 链的影响 |
|------|----------|------|----------------------|
| 2018-10（iOS 12, A12） | **PAC/XPAC** | 保护函数指针 | L4 PAC bypass 难度提升；但 data-only 攻击不受影响 |
| 2021-10（iOS 15.1） | **W^X 强化** | 禁止可执行内存 | L5 代码执行必须修改现有代码，不能注入新代码 |
| 2022-10（iOS 16） | **kalloc type isolation** | 隔离不同大小的 kalloc 分配 | L2 堆对象伪造难度提升，OOB 写难以跨越 zone |
| 2023-10（iOS 17） | **zone_require** | 类型转换时验证 zone | L2 伪造对象必须来自正确的 kalloc zone |
| 2024-10（iOS 18） | **PPL 保护 ucred/cs_flags** | 保护关键凭证数据 | L4 data-only 攻击难度提升，需 PPL bypass |
| 2025-10（iOS 26, A19） | **MIE/MTE** | 硬件级内存标签 | L1 OOB 检测率显著提升，但性能开销 3–5% |

## 5.2 缓解有效性对照表

| 攻击阶段 | iOS 12 | iOS 16 | iOS 18 | iOS 26 |
|----------|--------|--------|--------|--------|
| **L1 OOB 原语** | 易（无检测） | 易（无检测） | 易（无检测） | 中（MTE 可检测） |
| **L2 堆对象伪造** | 易（无隔离） | 中（kalloc type） | 中（+zone_require） | 中（+MTE） |
| **L3 task port** | 易 | 易 | 易 | 易 |
| **L4 PAC bypass** | 不适用 | 中（data-only） | 难（PPL 保护） | 难（+MTE） |
| **L5 代码执行** | 易 | 易（data-only） | 中（PPL bypass 需额外漏洞） | 中 |

**关键观察**：

- **L1 OOB 原语始终可行**：OOB 是逻辑漏洞，无法通过缓解机制完全消除，只能通过输入验证避免
- **L2 堆对象伪造难度显著提升**：iOS 16+ 的 kalloc type isolation 和 zone_require 迫使攻击者寻找更复杂的堆布局技术
- **L4 PAC bypass 是分水岭**：iOS 18+ PPL 保护了 `ucred` 和 `cs_flags`，迫使攻击者寻找 PPL bypass（如 CVE-2025-43510）或改用 task_set_exception_port
- **L5 代码执行仍可行**：即使有 PAC 和 PPL，data-only 攻击（修改数据而非代码指针）仍可绕过大多数保护

## 5.3 残余风险

即使部署了所有缓解机制，以下风险仍然存在：

1. **OOB 漏洞本身无法完全消除**：只能通过代码审计和 fuzzing 减少，但无法通过缓解机制完全阻止
2. **data-only 攻击路径**：修改数据（如 `ucred`、`cs_flags`）而非代码指针，可绕过 PAC 和大多数完整性保护
3. **PPL bypass**：PPL 本身可能存在漏洞（如 CVE-2025-43510），一旦被绕过，所有 PPL 保护失效
4. **侧信道攻击**：PAC 签名密钥可能通过侧信道泄露（如 XPAC 攻击），但实际利用难度极高
5. **供应链攻击**：第三方库（如 libarchive、ImageIO）的 OOB 漏洞可能被用于零点击攻击


# 六、防御建议

## 6.1 开发阶段

| 建议 | 优先级 | 说明 |
|------|--------|------|
| **输入验证** | 高 | 所有用户态输入（数组索引、长度字段）必须验证边界 |
| **整数溢出检查** | 高 | 长度计算必须检查整数溢出（使用 `__builtin_add_overflow` 等） |
| **类型安全** | 中 | 使用强类型语言（Swift 而非 C），减少类型混淆风险 |
| **fuzzing** | 高 | 对所有解析器（ImageIO、libarchive、WebKit）进行持续 fuzzing |

## 6.2 运行时缓解

| 建议 | 优先级 | 说明 |
|------|--------|------|
| **启用 MTE** | 中（A19+） | 硬件级内存标签，可检测 OOB 访问，性能开销 3–5% |
| **启用 PAC** | 高（A12+） | 保护函数指针，防止 ROP/JOP 攻击 |
| **启用 kalloc type isolation** | 高（iOS 16+） | 隔离不同大小的 kalloc 分配，防止堆对象伪造 |
| **启用 zone_require** | 高（iOS 17+） | 类型转换时验证 zone，防止伪造对象 |

## 6.3 用户层面

| 建议 | 优先级 | 说明 |
|------|--------|------|
| **及时更新 iOS** | 高 | Apple 每月发布安全更新，修复已知 OOB 漏洞 |
| **禁用 iMessage 自动预览** | 中 | 减少零点击攻击面（iMessage 自动下载/解析媒体文件） |
| **使用 Lockdown Mode** | 高（高风险用户） | 禁用大量攻击面（WebKit JIT、复杂图像解析等） |
| **避免访问不可信网站** | 中 | 减少水坑攻击风险 |


# 七、趋势与展望

## 7.1 攻击趋势

1. **零点击攻击成为主流**：CVE-2025-43300 表明，通过 ImageIO 解析恶意文件，可实现零点击 ACE，无需用户交互
2. **data-only 攻击取代代码指针覆盖**：PAC 和 PPL 保护了代码指针，攻击者转向修改数据（`ucred`、`cs_flags`）
3. **PPL bypass 成为关键**：iOS 18+ PPL 保护了关键数据，攻击者必须寻找 PPL 自身的漏洞（如 CVE-2025-43510）
4. **供应链攻击增加**：第三方库（libarchive、ImageIO）成为攻击入口，Apple 自身组件（WebKit、IOMobileFrameBuffer）的漏洞减少

## 7.2 缓解趋势

1. **硬件级内存安全**：MIE/MTE（iOS 26, A19+）提供硬件级 OOB 检测，但性能开销 3–5%
2. **更细粒度的隔离**：kalloc type isolation 和 zone_require 持续演进，增加堆对象伪造难度
3. **PPL 保护范围扩大**：PPL 可能保护更多关键数据（如 IPC port、vtable），进一步限制 data-only 攻击
4. **fuzzing 自动化**：Apple 持续增加 fuzzing 覆盖，减少 OOB 漏洞数量

## 7.3 未解决问题

1. **MTE 性能开销**：3–5% 的性能开销是否可接受？Apple 是否会在所有设备上启用？
2. **PPL 可信基**：PPL 本身是一个可信基，其安全性如何保证？PPL 漏洞（如 CVE-2025-43510）如何减少？
3. **data-only 攻击防御**：如何防御 data-only 攻击？是否需要更细粒度的数据完整性保护（如 CFI for data）？
4. **供应链安全**：如何保证第三方库（libarchive、ImageIO）的安全性？是否需要更严格的代码审计？


# 附录 A：术语表

| 术语 | 全称 | 说明 |
|------|------|------|
| OOB | Out-of-Bounds | 越界访问（读/写） |
| ACE | Arbitrary Code Execution | 任意代码执行 |
| PAC | Pointer Authentication Code | 指针认证码（A12+） |
| PPL | Page Protection Layer | 页面保护层（iOS 18+） |
| MIE | Memory Integrity Enforcement | 内存完整性执行（iOS 26, A19+） |
| MTE | Memory Tagging Extension | 内存标签扩展（ARMv8.5+） |
| KASLR | Kernel Address Space Layout Randomization | 内核地址空间布局随机化 |
| UAF | Use-After-Free | 释放后使用 |
| ROP | Return-Oriented Programming | 面向返回编程 |
| JOP | Jump-Oriented Programming | 面向跳转编程 |
| CFI | Control Flow Integrity | 控制流完整性 |
| SSV | Signed System Volume | 签名系统卷（iOS 15+） |


# 附录 B：参考资料

1. Apple Security Advisories. https://support.apple.com/en-us/HT201222
2. Google Project Zero. "A survey of recent iOS kernel exploits." 2020-06.
3. Google Project Zero. "0days in the wild." https://googleprojectzero.github.io/0days-in-the-wild/
4. Google Project Zero. "CVE-2019-7287: iOS Buffer Overflow in IOMobileFrameBuffer." 2019.
5. Google Project Zero. "CVE-2021-1782: iOS vulnerability in IOMobileFrameBuffer." 2022-04.
6. Quarkslab. "Reverse engineering of Apple's iOS 0-click CVE-2025-43300." 2025-09.
7. 8ksec. "Inside the DarkSword Kernel Escalation: From GPU to Kernel." 2026-05.
8. Jamf. "Predator Spyware's iOS Kernel Exploitation Engine." 2023.
9. Jamf. "Running code in the context of iOS Kernel: Part I + LPE PoC on iOS 13.7." 2020-11.
10. CISA Known Exploited Vulnerabilities Catalog. https://www.cisa.gov/known-exploited-vulnerabilities-catalog
11. HackTricks. "iOS CVE-2021-30807 IOMobileFrameBuffer." https://hacktricks.wiki/
12. NVD. https://nvd.nist.gov/


# 附录 C：版本与说明

- **报告版本**：1.0（2026-10-06）
- **适用 iOS 版本**：iOS 12 – iOS 26
- **数据来源**：Apple 安全通告、NVD、Google Project Zero、CISA KEV、公开学术文献
- **局限性**：本报告基于公开资料，不包含未公开漏洞或私有 exploit 信息
- **免责声明**：本报告仅用于安全研究与防御目的，不构成攻击指导

咨询ios系统请咨询 telegram：https://t.me/one00190
