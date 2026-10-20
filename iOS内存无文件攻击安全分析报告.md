@@TITLE@@iOS 内存无文件攻击安全分析报告
@@SUBTITLE@@Fileless & In-Memory Attack Techniques on iOS
@@INFO@@创建日期|2026-10-10
@@INFO@@适用范围|iOS / iPadOS 全版本（含最新 iOS 18）
@@INFO@@分类|网络安全 · 漏洞分析 · 内存安全
@@INFO@@联系|Telegram: https://t.me/one00190
@@TOC@@目录

# iOS 漏洞背景与影响范围

## 1.1 什么是内存无文件攻击

**内存无文件攻击**（Fileless / In-Memory Attack）是指攻击载荷**从未以独立文件形式写入磁盘**，而是完全在目标系统的易失性内存（RAM）中完成注入、执行与驻留的攻击方式。与传统恶意软件相比，此类攻击具有以下核心特征：

- **无磁盘痕迹**：不存在可被文件扫描引擎检测的恶意二进制文件
- **内存驻留**：恶意代码仅存在于进程地址空间中，重启后即消失
- **利用合法组件**：借助系统自带的合法进程（如 WebKit、kernel_task）执行恶意逻辑
- **规避传统检测**：基于文件签名的杀毒软件与完整性检查难以发现

在 iOS 生态中，无文件攻击代表了对 Apple 纵深防御体系最高级别的挑战——攻击者必须在不留下持久化证据的前提下，突破代码签名、沙箱、PAC 与 MIE 等多层防线。

## 1.2 影响范围

| 维度 | 说明 |
|------|------|
| 受影响设备 | 所有运行 iOS / iPadOS 的 iPhone 与 iPad |
| 攻击入口 | Safari/WebKit 网页渲染、iMessage 媒体解析、基带消息处理 |
| 攻击目标 | 用户数据窃取、通信监控、持久化后门 |
| 攻击难度 | 极高——需要组合多个零日漏洞形成完整利用链 |
| 已知攻击者 | 国家级 APT 组织（NSO Group、Intellexa/Canvas）、Google TAG 披露的商业利用套件 |

## 1.3 与传统攻击的对比

| 特征 | 传统文件攻击 | 内存无文件攻击 |
|------|------------|--------------|
| 磁盘存在 | 有恶意文件 | 无恶意文件 |
| 持久化 | 写入文件系统 | 仅内存驻留，重启消失 |
| 检测方式 | 文件扫描、哈希匹配 | 内存取证、行为分析 |
| 代码执行 | 加载并执行文件 | ROP/JOP/JIT 喷射 |
| 取证难度 | 较低 | 极高（需捕获易失性内存） |
| 典型入口 | 恶意 App、钓鱼附件 | 网页浏览、消息处理 |

# 无文件攻击核心技术原理

## 2.1 攻击能力阶梯（L1–L5）

内存无文件攻击的实现需要逐步获取一系列递进能力：

| 层级 | 能力 | 说明 |
|------|------|------|
| L1 | 初始访问 | 通过网页/消息触发解析器漏洞 |
| L2 | 任意内存读写 | 利用类型混淆/UAF 获取 addrof/fakeobj 原语 |
| L3 | 代码执行 | 构造 ROP 链或 JIT 喷射执行任意代码 |
| L4 | 沙箱逃逸 | 利用 XNU 漏洞突破 WebContent 沙箱 |
| L5 | 持久化/提权 | 内核利用实现内存驻留后门 |

## 2.2 面向返回编程（ROP）

**ROP（Return-Oriented Programming）** 是 iOS 无文件攻击的核心技术之一。在代码签名与 W^X（Write XOR Execute）保护下，攻击者无法直接在内存中执行新写入的代码，转而**复用已加载到内存中的合法代码片段**（称为 gadget）。

### ROP 工作原理

1. **Gadget 发现**：扫描已加载的二进制映像（dyld_shared_cache），寻找以 `ret` 指令结尾的短指令序列
2. **链式构造**：将多个 gadget 的入口地址按执行顺序排列在栈上
3. **控制流劫持**：通过漏洞覆盖返回地址，使程序跳转到 gadget 链
4. **功能组合**：每个 gadget 完成一小步操作（如写寄存器、调用函数），串联后实现复杂逻辑

### iOS 上的 ROP 特点

| 特点 | 说明 |
|------|------|
| PAC 签名 | ARM64e 设备上返回地址受 PAC 保护，需绕过或禁用 PAC |
| ASLR | 每次启动随机化基址，需先泄露内存布局 |
| dyld_shared_cache | 系统库预链接为单一缓存，提供大量可用 gadget |
| W^X 强制 | 无法分配可执行内存，必须使用 ROP 或 JIT 喷射 |

## 2.3 JIT 喷射（JIT Spraying）

**JIT 喷射**是利用 JavaScriptCore（JSC）即时编译器将攻击载荷"编译"到可执行内存区域的技术。

### 技术流程

1. **构造特殊 JS 代码**：编写 JavaScript 使 JSC 编译器生成包含攻击者控制数据的机器码
2. **触发 JIT 编译**：通过热点循环或类型专业化迫使 JSC 将代码编译为本地码
3. **定位喷射区域**：利用信息泄露获取 JIT 编译后代码的内存地址
4. **跳转执行**：将控制流重定向到 JIT 区域，执行"编译"出的载荷

### JIT 喷射 vs ROP

| 维度 | ROP | JIT 喷射 |
|------|-----|---------|
| 代码来源 | 复用已有代码片段 | 生成新的可执行代码 |
| 复杂度 | 需精确构造 gadget 链 | 需理解 JIT 编译器行为 |
| PAC 影响 | 需绕过 PAC | 不受 PAC 影响（新代码有合法签名） |
| 稳定性 | 较低（依赖 gadget 可用性） | 较高（载荷完全可控） |
| 检测特征 | 异常控制流 | 异常 JIT 代码模式 |

## 2.4 内存驻留载荷技术

攻击者成功执行代码后，需将载荷保持在内存中而不被检测：

### 进程镂空（Process Hollowing）

- 利用 fork/exec 机制创建合法进程
- 在内存中替换其代码段为恶意载荷
- 进程对外表现为合法系统服务

### 函数钩取（Function Hooking）

- 在目标进程的内存中修改函数指针
- 将系统调用重定向到恶意代码
- 无需修改磁盘上的二进制文件

### 内存映射注入

- 利用 `mmap` 在目标进程地址空间分配内存
- 将载荷写入新映射区域
- 通过 `mach_vm_write` 跨进程注入

# iOS 内存攻击面分析

## 3.1 WebKit / JSC 攻击面

WebKit 是 iOS 上最大的内存攻击面，所有网页浏览、邮件 HTML 渲染、iMessage 富媒体处理均经过此路径。

| 攻击面 | 说明 | 利用潜力 |
|--------|------|---------|
| JSC 类型系统 | JSValue 类型混淆导致 addrof/fakeobj | L2 任意读写原语 |
| JSC JIT 编译器 | 推测优化中的边界检查遗漏 | L3 代码执行 |
| WebCore 解析器 | HTML/DOM 解析中的 UAF | 进程内代码执行 |
| GPU 进程 | GPU 驱动共享内存区域 | 跨进程数据泄露 |
| WebContent 沙箱 | 沙箱配置错误可能导致逃逸 | L4 沙箱逃逸 |

### JSC 堆布局利用

JSC 使用独立的堆管理器，其对象布局为攻击者提供了丰富的利用原语：

- **JSObject 结构**：包含 Butterfly 指针（指向属性/元素存储），可被伪造实现任意读写
- **JSCell 头**：每个 JS 值的元数据，类型混淆可改变解释器对内存的解释方式
- **MarkedBlock**：GC 管理单元，UAF 可导致已释放对象被重新分配为攻击者控制的内容

## 3.2 XNU 内核攻击面

内核级内存攻击可实现最高权限的代码执行：

| 攻击面 | 说明 | 典型漏洞 |
|--------|------|---------|
| Mach 消息处理 | 端口权限与消息解析 | CVE-2021-1782（voucher UAF） |
| IOKit 驱动 | 用户态 ↔ 内核态共享结构体 | CVE-2023-38682（内核越界写） |
| 网络协议栈 | 数据包解析与内存管理 | CVE-2023-38685（基带内存破坏） |
| 文件系统 | APFS / HFS+ 解析 | 特制镜像触发的内存破坏 |
| 内存管理 | kalloc/vm_map 逻辑 | 整数溢出导致的分配错误 |

## 3.3 进程间通信攻击面

iOS 的 IPC 机制为跨进程内存攻击提供了通道：

- **Mach 端口**：消息传递可携带 OOL（Out-Of-Line）内存描述符，解析错误可导致内核内存破坏
- **XPC**：序列化/反序列化过程中的类型混淆
- **共享内存**：`mach_memory_object` 接口允许进程间共享内存区域
- **通知中心**：`darwin_notify_center` 可触发跨进程状态变更

# 典型攻击案例与 CVE 分析

## 4.1 在野内存无文件攻击 CVE 汇总

| CVE | 组件 | 缺陷类型 | 修复版本 | 攻击阶段 | 在野利用 |
|-----|------|---------|---------|---------|---------|
| CVE-2019-8554 | JSC | 类型混淆 | iOS 12.1.4 | L2 任意读写 | 是（水坑攻击） |
| CVE-2019-8605 | IOMobileFrameBuffer | 堆溢出 | iOS 12.4.1 | L4 沙箱逃逸 | 是 |
| CVE-2020-9818 | JSC | JIT 编译器错误 | iOS 13.5 | L3 代码执行 | 是 |
| CVE-2020-9819 | XNU (IOMobileFrameBuffer) | 越界写 | iOS 13.5 | L4 沙箱逃逸 | 是 |
| CVE-2021-1782 | Mach voucher | UAF | iOS 14.3 | L4 沙箱逃逸 | 是（存在争议） |
| CVE-2021-30860 | IOSurface | 类型混淆 | iOS 14.8 | L4 沙箱逃逸 | 是（FORCEDENTRY） |
| CVE-2023-32409 | WebKit (WebCore) | 越界读 | iOS 16.5 | L2 信息泄露 | 是 |
| CVE-2023-32435 | JSC | 类型混淆 | iOS 16.5.1 | L2 任意读写 | 是 |
| CVE-2023-38681 | IOKit (AGXCompilerService) | 越界写 | iOS 16.6.1 | L4 沙箱逃逸 | 是（锁定模式触发） |
| CVE-2023-38682 | XNU | 内核越界写 | iOS 16.6.1 | L5 内核代码执行 | 是（锁定模式触发） |
| CVE-2023-38685 | Baseband | 内存破坏 | iOS 16.6.1 | L1 远程触发 | 是（锁定模式触发） |
| CVE-2024-44308 | WebKit | 类型混淆 | iOS 18.0 | L2 任意读写 | 是 |
| CVE-2025-14174 | WebKit | 类型混淆/UAF | iOS 18.4 | L2 初始访问 | 是（DarkSword） |
| CVE-2025-31200 | CoreAudio / XNU | PAC 绕过 / 任意读写 | iOS 18.3.2 | L4/L5 | 是（DarkSword） |
| CVE-2025-43529 | WebKit | 内存破坏 | iOS 18.5 | L2-L3 | 是（极复杂攻击） |

## 4.2 案例一：FORCEDENTRY（NSO Group，2021）

**FORCEDENTRY** 是针对 iOS 的零点击内存无文件攻击，通过 iMessage 处理特制 PDF 实现：

### 攻击链

1. **L1 初始访问**：iMessage 接收特制 PDF，触发 ImageIO 解析器
2. **L2 内存破坏**：CVE-2021-30857 — Babylon 渲染引擎中的堆溢出
3. **L3 原语获取**：CVE-2021-30860 — IOSurface 类型混淆，获取任意读写
4. **L4 沙箱逃逸**：CVE-2021-30807 — XNU 内核漏洞实现代码执行
5. **L5 载荷部署**：Pegasus 间谍软件完全在内存中运行

### 无文件特征

- 载荷通过 iMessage 协议传输，不经过文件系统
- 所有恶意代码在 SpringBoard 进程内存中解密和执行
- 重启后无持久化痕迹（需配合其他机制实现持久化）
- 传统文件扫描完全无法检测

## 4.3 案例二：DarkSword（Intellexa/Canvas，2025–2026）

**DarkSword** 是 Google TAG 于 2026 年 3 月披露的商业间谍软件利用套件：

### 攻击链

1. **GHOSTBLADE 阶段**：网页零日漏洞（CVE-2025-14174）实现初始代码执行
2. **KNIFE 阶段**：WebKit 沙箱逃逸 + 内核提权（CVE-2025-31200/31201）
3. **SABER 阶段**：内存驻留载荷部署，完全无文件运行

### 技术特点

- 全链路无文件：从初始访问到最终载荷均不接触磁盘
- 利用 JIT 编译器生成可执行代码绕过 W^X
- PAC 绕过技术使返回地址伪造在 ARM64e 设备上可行
- 内存驻留载荷通过 Mach 端口与 C2 通信

## 4.4 案例三：Predator（Thrip/NetRevenge，2022–2023）

**Predator** 是另一个在野 iOS 利用套件，展示了内存无文件攻击的完整流程：

### 攻击特征

- 通过水坑攻击分发，受害者访问被入侵的合法网站
- WebKit 漏洞触发后，所有后续操作在浏览器进程内存中完成
- 使用 ROP 链绕过 W^X 保护
- 沙箱逃逸后通过 Mach IPC 与内核交互
- 最终载荷以内存驻留方式运行，不写入文件系统

# 内存攻击检测与取证

## 5.1 检测挑战

| 挑战 | 说明 |
|------|------|
| 易失性 | 内存内容在设备重启后完全丢失 |
| 加密 | iOS 内存内容受硬件级加密保护 |
| 访问限制 | 无 root 权限无法直接读取物理内存 |
| 正常噪声 | 合法 JIT 代码与恶意喷射代码难以区分 |
| 反取证 | 高级攻击者主动清除内存痕迹 |

## 5.2 检测方法

### 运行时内存分析

- **进程映射扫描**：检查异常的 `VM_PROT_EXECUTE` 内存区域
- **JIT 代码审计**：分析 JSC JIT 编译输出的代码模式异常
- **堆完整性检查**：检测 JSC 堆中的类型混淆痕迹
- **控制流完整性**：验证返回地址的 PAC 签名

### 行为检测

- **异常系统调用**：监控 `mach_vm_write`、`task_for_pid` 等敏感调用
- **网络行为**：检测异常的 Mach 端口通信模式
- **资源使用**：监控异常的内存分配与 CPU 使用模式

### 事后取证

- **内存转储**：通过硬件调试接口或内核漏洞获取内存映像
- **交换文件分析**：检查 `/var/vm/swapfile` 中的残留数据
- **崩溃日志**：分析异常崩溃中可能包含的内存状态信息
- **诊断数据**：利用 iOS 诊断报告中的内存使用统计

## 5.3 IOC（入侵指标）

| 指标类型 | 具体表现 |
|---------|---------|
| 网络 | 异常的 Mach 端口活动、非标准 TLS 指纹、定期心跳包 |
| 内存 | 未解释的可执行内存区域、异常 JIT 代码密度 |
| 行为 | 摄像头/麦克风在用户未操作时激活、异常电池消耗 |
| 配置 | 描述文件异常安装、MDM 配置变更 |

# 缓解机制与防御

## 6.1 Apple 内置缓解机制

| 机制 | 版本引入 | 对无文件攻击的影响 |
|------|---------|-----------------|
| W^X（Write XOR Execute） | iOS 2.0 | 阻止直接内存代码执行，迫使使用 ROP/JIT 喷射 |
| ASLR | iOS 4.3 | 增加 gadget 定位难度，需信息泄露配合 |
| PAC（Pointer Authentication） | iOS 12（A12+） | 保护返回地址，增加 ROP 难度 |
| MIE（Memory Integrity Enforcement） | iOS 15 | 硬件级内存标签，检测越界访问 |
| BlastDoor | iOS 13 | 隔离 iMessage 解析，限制攻击面 |
| JITless 模式 | iOS 17（锁定模式） | 禁用 JIT，消除 JIT 喷射攻击面 |
| kalloc type isolation | iOS 16 | 内核堆类型隔离，减少 UAF 利用成功率 |
| zone_require | iOS 17 | 内核区域类型检查，防止跨类型对象混淆 |

## 6.2 缓解有效性对照

| 攻击技术 | PAC | MIE | JITless | BlastDoor | 有效性 |
|---------|-----|-----|---------|-----------|-------|
| ROP 链 | ✓ 阻断 | — | — | — | 高 |
| JIT 喷射 | — | — | ✓ 阻断 | — | 高 |
| 类型混淆 | — | ✓ 检测 | — | ✓ 隔离 | 中 |
| UAF | — | ✓ 检测 | — | ✓ 隔离 | 中 |
| 堆溢出 | — | ✓ 检测 | — | ✓ 隔离 | 中 |
| 内核利用 | — | — | — | — | 低（需内核级缓解） |

## 6.3 残余风险

尽管 Apple 的缓解机制不断进步，以下残余风险仍然存在：

1. **JIT 喷射在普通模式下仍可用**：仅锁定模式启用 JITless
2. **ROP 在 PAC 绕过后仍可行**：CVE-2025-31200 展示了 RPAC 绕过技术
3. **内核攻击面持续存在**：IOKit 驱动的共享结构体难以完全消除
4. **零日漏洞持续被发现**：每年仍有多个在野零日被披露

# 防御最佳实践

## 7.1 用户层面

- **启用锁定模式**：对高风险用户，锁定模式禁用 JIT 并缩小攻击面
- **及时更新**：Apple 的安全补丁修复已知内存漏洞
- **谨慎处理链接**：避免访问不可信网站，减少水坑攻击风险
- **审查描述文件**：定期检查并删除不明配置描述文件

## 7.2 企业层面

- **MDM 管控**：通过 MDM 强制启用锁定模式并限制侧载
- **网络监控**：部署网络检测系统识别异常 C2 通信
- **设备管理**：实施设备合规性检查与远程擦除能力
- **威胁情报**：订阅 Apple 安全更新与 Google TAG 报告

## 7.3 开发者层面

- **安全编码**：避免在应用中使用不安全的内存操作
- **沙箱合规**：遵循 App Sandbox 要求，不尝试突破沙箱限制
- **代码签名**：确保所有代码正确签名，不使用调试 entitlement
- **内存安全**：使用 Swift 的内存安全特性，避免 Unsafe 操作

# 术语表

| 术语 | 全称 | 说明 |
|------|------|------|
| ROP | Return-Oriented Programming | 面向返回编程，复用已有代码片段 |
| JOP | Jump-Oriented Programming | 面向跳转编程，使用 jmp 代替 ret |
| JIT | Just-In-Time | 即时编译，运行时将字节码编译为本地码 |
| PAC | Pointer Authentication Code | 指针认证码，ARM64e 的返回地址保护 |
| MIE | Memory Integrity Enforcement | 内存完整性执行，硬件级内存安全 |
| ASLR | Address Space Layout Randomization | 地址空间布局随机化 |
| W^X | Write XOR Execute | 写与执行互斥，内存页不能同时可写和可执行 |
| UAF | Use-After-Free | 释放后使用，访问已释放的内存 |
| OOB | Out-Of-Bounds | 越界访问 |
| OOL | Out-Of-Line | Mach 消息中的线外内存描述符 |
| Gadget | — | ROP 中的短指令序列片段 |
| Butterfly | — | JSC 中指向属性/元素存储的指针 |
| dyld_shared_cache | — | iOS 系统库的预链接缓存 |

# 参考资料

1. Apple Security Releases. https://support.apple.com/en-us/100100
2. Google TAG. "DarkSword Exploit Kit Analysis." 2026-03-18.
3. Project Zero. "JITSploitation III: Subverting Control Flow." 2020-09.
4. Project Zero. "Remote iPhone Exploitation Part 1: Poking Memory via iMessage." 2020-01.
5. Apple. "Memory Integrity Enforcement: A Complete Vision." 2025-09.
6. Cititzen Lab. "FORCEDENTRY: NSO Group's Zero-Click iPhone Exploit." 2021-09.
7. Lookout. "WebKit and Kernel Vulnerabilities and DarkSword Exploit." 2026-03.
8. Gawlik et al. "SoK: Make JIT-Spray Great Again." WOOT 2018.
9. 8ksec. "Memory Integrity Enforcement (MIE) on iOS Deep Dive." 2026-01.
10. MITRE ATT&CK. "Mobile Techniques." https://attack.mitre.org/matrices/enterprise/mobile/

---

Telegram: https://t.me/one00190
