@@TITLE@@iOS Sandbox Escape 技术详解
@@SUBTITLE@@iOS Sandbox Escape Technical Deep Dive
@@INFO@@报告日期|2026年10月13日
@@INFO@@分析范围|iOS 沙箱逃逸技术全景：SeatBelt/SBPL 机制、XPC 攻击面、共享内存攻击、内核漏洞利用、BlastDoor/WebContent 逃逸路径
@@INFO@@技术层级|系统级安全 / 进程隔离 / 沙箱逃逸
@@INFO@@联系方式|Telegram: https://t.me/one00190

@@TOC@@目录

# 概述

## 1.1 沙箱在 iOS 安全架构中的地位

iOS 安全架构采用多层纵深防御模型，沙箱（Sandbox）是其中承上启下的关键一层。在代码签名和 AMFI（Apple Mobile File Integrity）确保代码来源可信之后，沙箱负责限制已签名代码的行为边界——即使代码是合法的，也不能超越其被授权的操作范围。

沙箱在 iOS 安全栈中的位置：

```
┌─────────────────────────────────────────────┐
│  硬件安全层                                   │
│  Secure Boot / Secure Enclave / PAC / MIE    │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│  内核安全层                                   │
│  KTRR / PPL / KASLR / kalloc type           │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│  沙箱隔离层  ◄── 本报告焦点                    │
│  SeatBelt / SBPL / BlastDoor / XPC          │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│  应用安全层                                   │
│  Entitlements / Privacy Controls / ATT       │
└─────────────────────────────────────────────┘
```

沙箱的核心价值在于**损害遏制（Damage Containment）**：当攻击者通过漏洞获得某个进程的代码执行权时，沙箱决定了攻击者能走多远。没有沙箱，一个 WebKit 漏洞就意味着完全控制；有了沙箱，攻击者还需要额外的沙箱逃逸漏洞才能突破。

## 1.2 沙箱逃逸的定义与分类

沙箱逃逸（Sandbox Escape）是指攻击者突破进程被施加的沙箱限制，获得超出授权范围的操作能力。根据逃逸的目标层级，可以分为：

| 类型 | 描述 | 典型场景 |
|------|------|---------|
| **沙箱内提权** | 在同一沙箱内获得更高权限 | 在 WebContent 内从 JS 到 native 代码执行 |
| **沙箱突破** | 从受限进程逃逸到非受限进程 | 从 WebContent 逃逸到系统级 |
| **内核逃逸** | 从用户空间逃逸到内核空间 | 利用内核漏洞获取 root |
| **完全逃逸** | 从最严格沙箱到完全控制 | WebContent → 系统 → 内核 → root |

本报告聚焦于**沙箱突破**和**完全逃逸**，即从受限进程（如 WebContent、BlastDoor）突破到更宽松的执行环境。

## 1.3 沙箱逃逸在攻击链中的角色

在现代 iOS 攻击链中，沙箱逃逸是连接"初始代码执行"和"完全控制"的关键桥梁：

```
初始访问 → 解析器 RCE（沙箱内） → 沙箱逃逸 → 内核提权 → 完全控制
                ↑                      ↑
           本报告不涉及            本报告核心
```

以 Pegasus FORCEDENTRY 为例：

1. iMessage 投递恶意 TIFF → CoreGraphics 解析触发整数溢出 → BlastDoor 沙箱内代码执行
2. **BlastDoor 沙箱逃逸** → 系统级代码执行
3. 内核漏洞利用 → root 权限
4. Pegasus 植入

步骤 2 就是沙箱逃逸，它决定了攻击者能否从"解析器内的有限代码执行"升级为"系统级的完全控制"。

---

# iOS 沙箱架构基础

## 2.1 SeatBelt 框架

iOS 的沙箱实现基于 **SeatBelt** 框架（也称为 Sandbox Framework），其核心组件包括：

### 2.1.1 SBPL（Sandbox Profile Language）

SBPL 是 SeatBelt 使用的策略描述语言，采用 Scheme/Lisp 风格的 S-expression 语法。每个沙箱策略定义了一组允许和禁止的操作。

SBPL 策略示例（概念性）：

```scheme
(version 1)
(deny default)

; 允许读取特定目录
(allow file-read*
  (subpath "/usr/lib")
  (subpath "/System/Library"))

; 禁止网络访问
(deny network*)

; 禁止进程创建
(deny process-exec)
(deny process-fork)

; 允许特定的 Mach 服务查询
(allow mach-lookup
  (global-name "com.apple.lookupd"))

; 禁止写入大部分目录
(deny file-write*)
(allow file-write*
  (subpath "/private/var/mobile/Containers/Data/Application"))
```

### 2.1.2 策略编译与执行

SBPL 策略在编译时转换为内部的字节码格式，由内核中的沙箱扩展（Sandbox.kext / sandbox.kext）在运行时执行：

```
SBPL 源文件 (.sb)
    │
    ▼
sandbox_compile_profile()    ← 用户空间 API
    │
    ▼
内部字节码
    │
    ▼
内核沙箱扩展执行检查          ← 每次系统调用时
    │
    ▼
允许 / 拒绝
```

### 2.1.3 沙箱检查点

沙箱在以下关键操作点进行检查：

| 检查点 | 说明 | 示例 |
|--------|------|------|
| 文件操作 | 读/写/创建/删除文件 | `open()`, `read()`, `write()` |
| 网络操作 | 发送/接收网络数据 | `send()`, `recv()`, `connect()` |
| 进程操作 | 创建/执行进程 | `fork()`, `exec()`, `posix_spawn()` |
| Mach 操作 | Mach 消息发送/接收 | `mach_msg()`, `mach_port_allocate()` |
| IOKit 操作 | 设备驱动交互 | `IOServiceOpen()`, `IOConnectCallMethod()` |
| 系统调用 | 特权系统调用 | `mount()`, `sysctl()`, `ptrace()` |
| 信号操作 | 发送/接收信号 | `kill()`, `signal()` |
| IPC 操作 | 进程间通信 | XPC、UNIX socket |

## 2.2 沙箱层级模型

iOS 中的不同进程被施加不同严格程度的沙箱：

| 层级 | 进程 | 严格度 | 关键限制 |
|------|------|--------|---------|
| L0 | WebContent | 最严格 | 无直接网络、无文件系统、无 IPC |
| L1 | BlastDoor | 严格 | 受限网络、受限文件、受限 IPC |
| L2 | 第三方 App | 中等 | App 沙箱、entitlement 限制 |
| L3 | 系统服务 | 宽松 | 部分系统权限 |
| L4 | rootd/launchd | 极宽松 | 接近 root |
| L5 | kernel_task | 无限制 | 完全控制 |

沙箱逃逸的本质就是从低层级向高层级的"跳跃"。

## 2.3 Entitlement 与沙箱的关系

Entitlement（授权）和沙箱是两个独立但互补的机制：

- **Entitlement**：声明式权限，嵌入在代码签名中，由 AMFI 验证
- **沙箱**：强制性限制，由内核沙箱扩展执行

两者必须同时满足才能执行操作：

```
操作请求
    │
    ├─ Entitlement 检查 → 通过？
    │
    ├─ 沙箱检查 → 通过？
    │
    ▼
两者都通过 → 允许操作
任一失败 → 拒绝操作
```

这意味着即使绕过了沙箱，如果缺少相应的 entitlement，操作仍然会被拒绝。成功的沙箱逃逸通常需要同时解决 entitlement 问题。

---

# XPC 服务攻击面

## 3.1 XPC 架构概述

XPC（Cross Process Communication）是 iOS/macOS 中的主要 IPC 机制，基于 Mach 消息和 libxpc 库实现。XPC 服务是沙箱逃逸的主要攻击面之一。

### 3.1.1 XPC 服务模型

```
客户端进程                    XPC 服务进程
┌──────────┐               ┌──────────────┐
│          │  mach_msg()   │              │
│  App     │ ────────────► │  XPC Service │
│          │               │              │
│          │  mach_msg()   │  沙箱策略     │
│          │ ◄──────────── │  (通常较宽松)  │
└──────────┘               └──────────────┘
```

XPC 服务通常运行在比客户端更宽松的沙箱中，因为它们需要执行系统级操作。攻击者可以从受限进程中向 XPC 服务发送精心构造的消息，利用服务中的漏洞实现沙箱逃逸。

### 3.1.2 XPC 消息格式

XPC 消息使用类型化的键值对格式：

```c
// 概念性代码（非实际利用代码）
xpc_object_t msg = xpc_dictionary_create(NULL, NULL, 0);
xpc_dictionary_set_string(msg, "command", "process_file");
xpc_dictionary_set_data(msg, "file_data", buffer, length);
xpc_dictionary_set_int64(msg, "flags", 0x1);

xpc_connection_send_message(connection, msg);
```

攻击者可以通过构造异常的 XPC 消息来触发服务中的漏洞。

## 3.2 XPC 攻击技术

### 3.2.1 反序列化漏洞

XPC 消息在接收端需要被反序列化为内部数据结构。如果反序列化逻辑存在漏洞，攻击者可以通过构造恶意消息触发代码执行。

**常见反序列化漏洞类型**：

| 漏洞类型 | 描述 | 影响 |
|---------|------|------|
| 类型混淆 | 期望类型 A，实际收到类型 B | 内存损坏 |
| 缓冲区溢出 | 数据长度超出预期 | 堆/栈溢出 |
| 整数溢出 | 数值计算溢出 | 分配错误 |
| 空引用 | 缺少必需字段 | 崩溃/信息泄露 |
| 递归解析 | 嵌套过深 | 栈溢出 |

### 3.2.2 授权绕过

某些 XPC 服务在处理请求前会检查调用者的身份或权限。如果检查逻辑存在缺陷，攻击者可以绕过授权检查。

**常见授权绕过技术**：

- **PID/UID 伪造**：在某些旧版本中，XPC 服务的调用者身份可以被伪造
- **entitlement 检查绕过**：服务检查调用者的 entitlement，但检查逻辑有缺陷
- **竞态条件**：在授权检查和实际操作之间的时间窗口中修改状态
- **路径遍历**：通过路径遍历访问未授权的文件

### 3.2.3 注入攻击

如果 XPC 服务将客户端提供的数据传递给其他组件（如 shell 命令、SQL 查询、文件路径），可能存在注入漏洞。

**示例场景**：

```
客户端 → XPC 消息 { "filename": "../../etc/passwd" } → XPC 服务
XPC 服务 → fopen(filename) → 路径遍历 → 读取敏感文件
```

## 3.3 历史 XPC 攻击案例

### 3.3.1 CVE-2020-9852 — sandboxd 漏洞

该漏洞存在于 `sandboxd` 守护进程中，攻击者可以通过构造恶意 XPC 消息触发内存损坏，在 `sandboxd` 的上下文中获得代码执行。由于 `sandboxd` 运行在相对宽松的沙箱中，这为沙箱逃逸提供了跳板。

### 3.3.2 CVE-2021-1782 — IOMobileFrameBuffer

虽然 Apple 将此漏洞归类为 IOMobileFrameBuffer 漏洞，但其利用路径涉及 XPC 服务：

1. WebContent 进程通过 XPC 向 `backboardd` 发送消息
2. `backboardd` 处理消息时通过 IOKit 调用 IOMobileFrameBuffer
3. IOMobileFrameBuffer 中的内存损坏导致内核代码执行

这条路径绕过了 WebContent 的沙箱限制。

### 3.3.3 CVE-2022-26712 — AppleIPCAssistance

该漏洞存在于 `AppleIPCAssistance` 内核扩展中，允许用户空间进程通过 IOKit 接口触发内核内存损坏。攻击者可以从第三方 App 的沙箱中逃逸到内核空间。

---

# 共享内存与 IPC 攻击

## 4.1 共享内存攻击面

iOS 中的多个进程通过共享内存区域进行通信。如果共享内存的管理存在漏洞，攻击者可以通过篡改共享数据来影响其他进程。

### 4.1.1 Mach 共享内存

Mach 内核提供 `mach_vm_map()` 等 API 用于创建共享内存区域。攻击面包括：

- **内存映射竞态**：在映射和访问之间的时间窗口中修改映射
- **引用计数错误**：共享内存对象的引用计数管理错误
- **权限检查缺失**：共享内存的访问权限检查不完整

### 4.1.2 IOSurface 共享

IOSurface 是 iOS 中用于图形渲染的共享内存机制。多个进程（如 SpringBoard、WebContent、第三方 App）通过 IOSurface 共享图形缓冲区。

**攻击场景**：

1. 攻击者在 WebContent 中创建 IOSurface
2. 将 IOSurface 句柄传递给其他进程
3. 在 IOSurface 数据中嵌入恶意载荷
4. 目标进程解析 IOSurface 数据时触发漏洞

### 4.1.3 匿名共享内存（ASMR）

Anonymous Shared Memory Regions 用于进程间的数据交换。攻击面类似于 Mach 共享内存，但实现细节不同。

## 4.2 Mach 端口攻击

Mach 端口是 iOS IPC 的基础。攻击 Mach 端口是沙箱逃逸的重要技术路径。

### 4.2.1 端口权限泄露

如果受限进程能够获取到特权进程的 Mach 端口发送权限，就可以向特权进程发送消息。

**端口泄露途径**：

- **继承泄露**：子进程继承了父进程的特权端口
- **任务端口泄露**：`task_for_pid()` 被滥用导致任务端口泄露
- **命名端口泄露**：全局命名的 Mach 端口被未授权进程访问

### 4.2.2 端口权限提升

即使获得了特权端口，发送的消息仍然受到沙箱限制。但如果目标进程在处理消息时存在漏洞，攻击者可以实现权限提升。

### 4.2.3 taskgated 攻击

`taskgated` 是负责管理任务端口的系统守护进程。如果 `taskgated` 存在漏洞，攻击者可能获取到其他进程的任务端口，从而完全控制目标进程。

**CVE-2019-6225**：`taskgated` 中的漏洞允许本地攻击者获取任意进程的任务端口，实现完全的权限提升。

## 4.3 IOKit 攻击面

IOKit 是 iOS 的设备驱动框架，提供了用户空间与内核空间通信的接口。IOKit 是沙箱逃逸到内核空间的主要攻击面。

### 4.3.1 IOUserClient

`IOUserClient` 是 IOKit 中用户空间与内核驱动通信的桥梁。每个 `IOUserClient` 暴露一组方法（`externalMethod`），用户空间通过 `IOConnectCallMethod()` 调用这些方法。

**攻击面**：

- **输入验证不足**：`externalMethod` 对用户输入验证不足
- **内存损坏**：内核驱动中的内存管理错误
- **竞态条件**：并发访问导致的数据竞争
- **类型混淆**：参数类型检查不严格

### 4.3.2 历史 IOKit 漏洞

| CVE | 驱动 | 漏洞类型 | 影响 |
|-----|------|---------|------|
| CVE-2019-8605 | IOMobileFrameBuffer | 越界写入 | 在野利用，内核代码执行 |
| CVE-2020-27930 | XNU (mach_vm_remap) | 信息泄露 | KASLR 绕过 |
| CVE-2020-27950 | XNU (内核) | 内存损坏 | 内核代码执行 |
| CVE-2021-30860 | CoreGraphics | 整数溢出 | FORCEDENTRY 攻击链 |
| CVE-2022-22620 | WebKit Compositor | 内存损坏 | Predator 攻击链沙箱逃逸 |
| CVE-2022-26712 | AppleIPCAssistance | 内存损坏 | 用户空间到内核 |
| CVE-2023-32435 | WebKit | RCE | BLASTPASS 攻击链 |
| CVE-2023-38606 | CoreMedia | RCE | BLASTPASS 攻击链 |
| CVE-2024-23222 | XNU | 内核 RCE | CLOUDPRONG 攻击链 |

---

# BlastDoor 沙箱逃逸

## 5.1 BlastDoor 架构

BlastDoor 是 Apple 在 iOS 14 中引入的安全机制，专门用于隔离 iMessage 的富媒体解析。所有来自 iMessage 的不可信数据都在 BlastDoor 沙箱中处理。

### 5.1.1 BlastDoor 设计目标

1. **隔离解析风险**：将不可信数据的解析限制在独立沙箱中
2. **最小权限**：BlastDoor 沙箱只授予解析所需的最小权限
3. **故障安全**：即使解析器被攻破，攻击者仍受沙箱限制
4. **可审计**：BlastDoor 的行为可以被监控和审计

### 5.1.2 BlastDoor 沙箱策略

BlastDoor 的沙箱策略（概念性）：

```scheme
; BlastDoor 沙箱策略（简化版）
(deny default)

; 允许读取系统库
(allow file-read*
  (subpath "/System/Library")
  (subpath "/usr/lib"))

; 禁止网络访问
(deny network*)

; 禁止进程创建
(deny process-exec)
(deny process-fork)

; 限制 Mach 端口访问
(deny mach-lookup)
(allow mach-lookup
  (global-name "com.apple.imagent.blastdoor"))

; 限制 IOKit 访问
(deny iokit-open)
(allow iokit-open
  (user-client-class "IOSurface"))
```

## 5.2 BlastDoor 逃逸路径

### 5.2.1 路径一：XPC 服务滥用

BlastDoor 通过 XPC 与 `imagent` 通信。攻击路径：

```
BlastDoor 进程                    imagent 进程
┌──────────────┐               ┌──────────────┐
│              │  XPC 消息     │              │
│  沙箱内      │ ────────────► │  沙箱外      │
│  代码执行    │               │  (或更宽松)   │
│              │  XPC 响应     │              │
│              │ ◄──────────── │  漏洞触发    │
└──────────────┘               └──────────────┘
```

**攻击步骤**：

1. 在 BlastDoor 中获得代码执行（通过解析器漏洞）
2. 构造恶意 XPC 消息发送给 `imagent`
3. `imagent` 处理消息时触发漏洞
4. 在 `imagent` 的上下文中获得代码执行
5. `imagent` 的沙箱比 BlastDoor 宽松，实现逃逸

**关键技术**：

- 需要找到 `imagent` 中的 XPC 消息处理漏洞
- 需要绕过 XPC 消息的类型检查和验证
- 可能需要伪造 BlastDoor 的身份

### 5.2.2 路径二：共享内存攻击

BlastDoor 与宿主进程共享某些内存区域（如 IOSurface）。攻击路径：

1. 在 BlastDoor 中修改共享内存数据
2. 宿主进程读取被篡改的数据
3. 宿主进程处理数据时触发漏洞
4. 在宿主进程中获得代码执行

**关键技术**：

- 需要找到 BlastDoor 和宿主进程之间的共享内存区域
- 需要理解宿主进程如何处理共享数据
- 需要构造能触发漏洞的恶意数据

### 5.2.3 路径三：内核漏洞直接逃逸

某些内核漏洞允许从任何沙箱中直接逃逸到内核空间：

1. 在 BlastDoor 中调用 IOKit API
2. 通过 IOKit 接口触发内核漏洞
3. 在内核空间中获得代码执行
4. 修改当前进程的沙箱策略或直接获取 root 权限

**BLASTPASS 攻击链**就是使用这种方式：

1. CVE-2023-38606：CoreMedia 漏洞 → BlastDoor 内代码执行
2. CVE-2023-32435：WebKit 漏洞 → 沙箱逃逸
3. 未公开内核漏洞 → 内核代码执行
4. 获取 root 权限

### 5.2.4 路径四：Mach 端口攻击

如果 BlastDoor 能够获取到特权进程的 Mach 端口：

1. 枚举可用的 Mach 端口
2. 找到特权进程的端口
3. 向特权端口发送恶意消息
4. 特权进程处理消息时触发漏洞
5. 在特权进程中执行代码

## 5.3 BlastDoor 逃逸案例

### 5.3.1 BLASTPASS（2023）

BLASTPASS 是首个被确认的 BlastDoor 逃逸攻击链，由 Citizen Lab 在 2023 年 9 月披露。

**攻击链详解**：

1. **初始 RCE**：通过 iMessage 发送恶意媒体文件，触发 CVE-2023-38606（CoreMedia 漏洞），在 BlastDoor 沙箱内获得代码执行
2. **沙箱逃逸**：利用 CVE-2023-32435（WebKit RCE，已在野利用）实现从 BlastDoor 到系统空间的逃逸
3. **内核提权**：使用未公开的内核漏洞获取 root 权限
4. **Pegasus 植入**：安装完整的 Pegasus 监控载荷

**影响**：

- 影响 iOS 16.5.1 及更早版本
- Apple 通过 RSR（Rapid Security Response）紧急修复
- 这是 RSR 首次被用于修复在野零日

### 5.3.2 其他已知尝试

虽然 BLASTPASS 是唯一被公开确认的 BlastDoor 逃逸，但安全研究人员已经发现了多个潜在的逃逸路径：

- **CoreMedia 解析器漏洞**：多个 CVE 涉及 CoreMedia 的媒体解析
- **ImageIO 漏洞**：图像解析中的内存损坏
- **PDFKit 漏洞**：PDF 解析中的逻辑错误

---

# WebContent 沙箱逃逸

## 6.1 WebContent 沙箱架构

WebContent 是 WebKit 的多进程架构中负责渲染网页内容的进程。WebContent 沙箱是 iOS 中最严格的沙箱之一。

### 6.1.1 WebContent 沙箱策略

```scheme
; WebContent 沙箱策略（简化版）
(deny default)

; 极度受限的文件系统访问
(allow file-read*
  (regex #"^/usr/lib/.*\.dylib$"))

; 完全禁止网络
(deny network*)

; 限制 IPC
(deny mach-lookup)
(deny mach-register)

; 禁止进程操作
(deny process*)

; 限制 IOKit
(deny iokit-open)
```

### 6.1.2 WebContent 的攻击面

尽管 WebContent 沙箱极其严格，但 WebKit 的复杂性仍然提供了攻击面：

- **JavaScript 引擎（JSC）**：类型混淆、内存损坏
- **DOM 解析器**：HTML/XML 解析中的内存损坏
- **CSS 引擎**：样式计算中的逻辑错误
- **WebGL**：GPU 驱动交互
- **Web Audio**：音频处理中的内存损坏
- **WebAssembly**：WASM 执行引擎

## 6.2 WebContent 逃逸路径

### 6.2.1 路径一：Compositor 进程攻击

WebKit 的 Compositor 进程负责合成网页的视觉层，其沙箱比 WebContent 宽松。

**攻击流程**：

```
WebContent 进程                  Compositor 进程
┌──────────────┐               ┌──────────────┐
│              │  共享 GPU     │              │
│  JS 代码执行 │  内存/IPC    │  较宽松沙箱   │
│              │ ────────────► │              │
│  漏洞触发    │               │  漏洞触发    │
│              │               │              │
└──────────────┘               └──────────────┘
```

1. 在 WebContent 中通过 JavaScript 触发 WebKit 漏洞
2. 通过共享 GPU 内存或 IPC 向 Compositor 发送恶意数据
3. Compositor 处理数据时触发内存损坏
4. 在 Compositor 中获得代码执行
5. Compositor 沙箱较宽松，可以访问更多资源

**CVE-2022-22620** 就是使用这种方式，Predator 攻击链通过此漏洞实现 WebContent 沙箱逃逸。

### 6.2.2 路径二：GPU 进程攻击

WebKit 的 GPU 进程负责硬件加速渲染。如果 GPU 进程存在漏洞，可以从 WebContent 逃逸。

**攻击流程**：

1. 在 WebContent 中通过 WebGL 或 CSS 触发 GPU 驱动漏洞
2. GPU 驱动处理恶意请求时触发内存损坏
3. 在 GPU 进程中获得代码执行
4. GPU 进程沙箱较宽松，可以访问系统资源

### 6.2.3 路径三：网络进程攻击

WebKit 的网络进程负责处理 HTTP 请求。如果网络进程存在漏洞，可以从 WebContent 逃逸。

**攻击流程**：

1. 在 WebContent 中通过 JavaScript 发起特殊的网络请求
2. 网络进程处理请求时触发漏洞
3. 在网络进程中获得代码执行
4. 网络进程可以访问网络，为进一步攻击提供跳板

### 6.2.4 路径四：内核漏洞直接逃逸

与 BlastDoor 类似，某些内核漏洞允许从 WebContent 直接逃逸到内核空间。

## 6.3 WebContent 逃逸案例

### 6.3.1 Predator 攻击链（2022）

Predator 是 Google TAG 在 2022 年 2 月披露的攻击链，用于 Pegasus 投递。

**攻击链详解**：

1. **Watering Hole 投递**：攻击者入侵目标可能访问的网站，注入恶意 JavaScript
2. **WebKit RCE**：CVE-2021-30860（CoreGraphics 整数溢出，通过 WebKit 路径触发）→ WebContent 内代码执行
3. **沙箱逃逸**：CVE-2022-22620（Compositor 漏洞）→ 从 WebContent 逃逸到系统空间
4. **内核提权**：使用未公开内核漏洞 → root 权限
5. **Pegasus 植入**：安装完整载荷

### 6.3.2 Operation Shax（2020）

虽然 Operation Shax 主要通过 iMessage 投递，但其沙箱逃逸路径涉及 WebKit：

1. iMessage 投递 → 内核信息泄露（CVE-2020-27930）
2. 内核代码执行（CVE-2020-27950）
3. 直接获取 root 权限（无需沙箱逃逸）

这个案例说明某些内核漏洞可以直接跳过沙箱逃逸阶段。

### 6.3.3 其他 WebKit 沙箱逃逸漏洞

| CVE | 年份 | 漏洞位置 | 逃逸方式 |
|-----|------|---------|---------|
| CVE-2020-9907 | 2020 | WebKit JIT | 通过 JIT 喷射实现代码执行 |
| CVE-2021-1789 | 2021 | WebKit DOM | 通过 DOM 解析器内存损坏 |
| CVE-2021-30860 | 2021 | CoreGraphics | 整数溢出导致堆溢出 |
| CVE-2022-22620 | 2022 | WebKit Compositor | Compositor 进程内存损坏 |
| CVE-2022-26708 | 2022 | WebKit WebAudio | 音频处理内存损坏 |
| CVE-2023-32435 | 2023 | WebKit | RCE，BLASTPASS 攻击链 |

---

# 第三方 App 沙箱逃逸

## 7.1 第三方 App 沙箱

第三方 App 的沙箱比 WebContent 和 BlastDoor 宽松，但仍然有严格限制：

### 7.1.1 App 沙箱策略

```scheme
; 第三方 App 沙箱策略（简化版）
(deny default)

; 允许访问 App 自己的容器
(allow file-read* file-write*
  (subpath "/private/var/mobile/Containers/Data/Application/<APP-UUID>"))

; 允许有限的网络访问
(allow network*)

; 限制 IPC
(deny mach-lookup)
(allow mach-lookup
  (regex #"^com\.apple\.(lsd|backboardd|SpringBoard)$"))

; 限制 IOKit
(deny iokit-open)
(allow iokit-open
  (user-client-class "IOSurface"))

; 禁止进程创建
(deny process-exec)
```

### 7.1.2 App 沙箱的限制

- 只能访问自己的容器目录
- 不能访问其他 App 的数据
- 不能访问系统敏感数据（通讯录、照片等需要用户授权）
- 不能执行其他进程
- 不能使用某些系统 API

## 7.2 App 沙箱逃逸路径

### 7.2.1 路径一：内核漏洞

最直接的方式是利用内核漏洞获取 root 权限：

1. 在 App 进程中触发内核漏洞（通过 IOKit 或其他接口）
2. 在内核空间获得代码执行
3. 修改当前进程的凭证，获取 root 权限
4. 绕过沙箱限制

### 7.2.2 路径二：XPC 服务攻击

向特权 XPC 服务发送恶意消息：

1. 枚举可用的 XPC 服务
2. 找到存在漏洞的服务
3. 构造恶意 XPC 消息
4. 在服务中触发漏洞
5. 在服务上下文中执行代码

### 7.2.3 路径三：文件系统攻击

通过文件系统漏洞访问未授权数据：

1. 利用符号链接攻击
2. 利用路径遍历漏洞
3. 利用文件系统竞态条件
4. 访问其他 App 或系统数据

### 7.2.4 路径四：权限提升

通过权限提升获取更高权限：

1. 利用 entitlement 检查漏洞
2. 利用 entitlement 伪造
3. 利用权限继承漏洞
4. 获取额外的 entitlement

## 7.3 App 沙箱逃逸案例

### 7.3.1 CVE-2020-9931 — 通讯录访问

该漏洞允许第三方 App 在未经用户授权的情况下访问通讯录。攻击者通过构造特殊的文件路径，绕过了沙箱的文件系统限制，读取了通讯录数据库。

### 7.3.2 CVE-2021-1765 — 键盘输入泄露

该漏洞允许恶意 App 监听其他 App 的键盘输入。攻击者通过利用辅助功能（Accessibility）API 的漏洞，绕过了沙箱的 IPC 限制。

### 7.3.3 CVE-2022-26729 — 照片访问

该漏洞允许第三方 App 在未经用户授权的情况下访问照片库。攻击者通过路径遍历漏洞，绕过了沙箱的文件系统限制。

---

# 高级沙箱逃逸技术

## 8.1 堆风水（Heap Feng Shui）

堆风水是一种通过精确控制堆内存布局来提高漏洞利用成功率的技术。

### 8.1.1 原理

堆风水通过以下手段控制堆布局：

- **分配模式**：精心控制内存分配和释放的顺序
- **大小选择**：选择特定的分配大小以落入目标堆区域
- **填充技术**：使用填充对象隔离目标对象
- **释放时序**：精确控制释放时机以创建特定的空闲列表状态

### 8.1.2 在沙箱逃逸中的应用

在沙箱逃逸中，堆风水用于：

1. **定位目标对象**：将目标对象（如函数指针、vtable）放置在可预测的位置
2. **覆盖控制**：确保溢出能精确覆盖目标对象
3. **避免崩溃**：避免覆盖关键数据结构导致进程崩溃

### 8.1.3 示例场景

```
// 概念性伪代码（非实际利用代码）
// 目标：覆盖 BlastDoor 中的 XPC 连接对象的函数指针

// 步骤 1：分配大量相同大小的对象，填充堆
for (int i = 0; i < 1000; i++) {
    allocate_object(SIZE_XPC_CONN);
}

// 步骤 2：释放特定位置的对象，创建空洞
free_object_at_index(500);
free_object_at_index(502);

// 步骤 3：分配漏洞触发对象，落入空洞
trigger_vulnerability(SIZE_XPC_CONN);

// 步骤 4：溢出覆盖相邻的 XPC 连接对象
// 溢出数据中包含恶意函数指针
```

## 8.2 类型混淆攻击

类型混淆（Type Confusion）是沙箱逃逸中最常用的技术之一。

### 8.2.1 原理

类型混淆发生在程序期望某种类型的对象，但实际收到另一种类型的对象时。攻击者通过构造恶意数据，使程序将一种类型的对象误认为另一种类型。

### 8.2.2 在沙箱逃逸中的应用

**场景一：XPC 消息类型混淆**

```
// 期望接收字符串
xpc_type_t type = xpc_get_type(msg["field"]);
if (type == XPC_TYPE_STRING) {
    // 处理字符串
}

// 攻击者发送数组类型
// 如果检查逻辑有缺陷，数组被当作字符串处理
// 导致内存损坏
```

**场景二：IOKit 用户客户端类型混淆**

```
// 期望打开 A 类型的用户客户端
IOServiceOpen(service, task, A_TYPE, &connect);

// 攻击者通过竞态条件替换为 B 类型
// B 类型的方法被当作 A 类型调用
// 导致类型混淆和内存损坏
```

## 8.3 竞态条件（Race Condition）

竞态条件攻击利用并发操作中的时序漏洞。

### 8.3.1 TOCTOU（Time-of-Check to Time-of-Use）

TOCTOU 是最经典的竞态条件攻击：

1. **检查阶段**：系统检查文件权限/类型
2. **替换阶段**：攻击者在检查和操作之间替换文件
3. **操作阶段**：系统对替换后的文件执行操作

**示例**：

```
// 沙箱检查文件路径是否允许访问
if (sandbox_check_permission("/tmp/safe_file")) {
    // 攻击者在此处将 /tmp/safe_file 替换为符号链接
    // 指向 /private/key_data
    fopen("/tmp/safe_file");  // 实际读取了 /private/key_data
}
```

### 8.3.2 在沙箱逃逸中的应用

竞态条件在沙箱逃逸中的应用：

- **文件替换**：在权限检查和文件操作之间替换文件
- **端口替换**：在端口查找和使用之间替换端口
- **对象替换**：在类型检查和对象使用之间替换对象
- **状态修改**：在状态检查和状态使用之间修改状态

## 8.4 侧信道攻击

侧信道攻击通过观察物理实现泄露信息，辅助沙箱逃逸。

### 8.4.1 信息泄露

沙箱逃逸通常需要绕过 KASLR（内核地址空间布局随机化）。侧信道可以用于泄露内核布局信息：

- **时序侧信道**：通过测量操作时间推断内存布局
- **缓存侧信道**：通过观察缓存命中模式推断内存访问
- **错误侧信道**：通过错误消息的差异推断内部状态

### 8.4.2 在沙箱逃逸中的应用

1. 使用侧信道泄露内核基址
2. 计算关键数据结构的地址
3. 精确覆盖目标数据结构
4. 实现沙箱逃逸

---

# 防御与缓解

## 9.1 Apple 的防御措施

### 9.1.1 BlastDoor

BlastDoor 本身就是针对沙箱逃逸的防御措施：

- **隔离解析**：将不可信数据的解析限制在独立沙箱中
- **最小权限**：BlastDoor 沙箱只授予解析所需的最小权限
- **纵深防御**：即使 BlastDoor 被突破，还需要额外的漏洞才能完全逃逸

### 9.1.2 Lockdown Mode

iOS 16 引入的 Lockdown Mode 进一步减少了沙箱逃逸的攻击面：

- **限制 iMessage 附件**：禁用大多数附件类型
- **禁用链接预览**：减少 WebKit 解析
- **限制复杂网页技术**：减少 WebKit 攻击面
- **禁用有线连接**：防止物理攻击

### 9.1.3 沙箱策略强化

Apple 持续强化沙箱策略：

- **更严格的默认策略**：默认拒绝更多操作
- **细粒度权限**：更精细的权限控制
- **动态策略**：根据运行时上下文调整策略

### 9.1.4 硬件辅助安全

新一代硬件安全机制增强沙箱：

- **SPTM**：硬件级线程管理，防止线程劫持
- **TXM**：硬件级执行控制，防止控制流劫持
- **PAC**：指针认证，防止函数指针覆盖
- **MIE**：内存标识扩展，防止内存损坏

## 9.2 开发者防御建议

### 9.2.1 减少攻击面

- **最小权限原则**：只请求必需的 entitlement
- **输入验证**：严格验证所有外部输入
- **安全编码**：避免内存损坏漏洞
- **沙箱测试**：在沙箱环境中测试应用

### 9.2.2 检测异常

- **监控 IPC**：监控异常的 IPC 请求
- **日志记录**：记录安全相关事件
- **完整性检查**：定期检查关键数据完整性

## 9.3 用户防御建议

### 9.3.1 基本防护

- **保持更新**：及时安装 iOS 安全更新
- **启用 Lockdown Mode**：高风险用户必须启用
- **谨慎点击**：不点击可疑链接和附件
- **使用官方 App**：只从 App Store 安装应用

### 9.3.2 高级防护

- **定期重启**：某些沙箱逃逸在重启后需要重新利用
- **监控设备行为**：注意电池、流量、温度异常
- **使用 MVT 检测**：定期使用 MVT 工具检测设备安全

---

# 总结与展望

## 10.1 关键发现

1. **沙箱逃逸是攻击链的关键环节**：连接"初始代码执行"和"完全控制"的桥梁

2. **XPC 和 IOKit 是主要攻击面**：大部分沙箱逃逸通过这些接口实现

3. **BlastDoor 显著提升安全性**：但并非不可突破，BLASTPASS 证明了这一点

4. **堆风水和类型混淆是核心技术**：几乎所有沙箱逃逸都使用这些技术

5. **攻防持续升级**：Apple 的防御措施不断被新的攻击技术突破

## 10.2 未来趋势

### 10.2.1 攻击趋势

- **AI 辅助漏洞发现**：使用 AI 自动化发现沙箱逃逸漏洞
- **跨沙箱链式攻击**：组合多个沙箱的漏洞实现完全逃逸
- **硬件侧信道**：利用硬件侧信道辅助沙箱逃逸

### 10.2.2 防御趋势

- **形式化验证**：对沙箱策略进行形式化验证
- **AI 防御**：使用 AI 检测异常行为
- **硬件强制沙箱**：使用硬件机制强制执行沙箱策略
- **零信任架构**：假设沙箱可能被突破，实施持续验证

## 10.3 建议

### 10.3.1 对 Apple

1. **持续强化 BlastDoor**：增加 BlastDoor 的隔离强度
2. **减少 XPC 攻击面**：审计所有 XPC 服务的安全性
3. **形式化验证**：对关键沙箱组件进行形式化验证
4. **增加透明度**：公开更多沙箱实现细节

### 10.3.2 对安全研究者

1. **关注 XPC 服务**：XPC 服务是沙箱逃逸的主要攻击面
2. **研究 BlastDoor**：BlastDoor 的安全性至关重要
3. **开发检测工具**：开发检测沙箱逃逸的工具
4. **分享研究成果**：在负责任的披露框架下分享发现

### 10.3.3 对用户

1. **保持更新**：及时安装安全更新
2. **启用 Lockdown Mode**：高风险用户必须启用
3. **提高警惕**：对可疑链接和消息保持警惕
4. **定期检测**：使用 MVT 等工具定期检测设备安全

---

**联系方式**

Telegram: https://t.me/one00190

---

*本报告仅供安全研究和教育目的使用。报告中提及的漏洞信息和攻击技术分析旨在帮助安全研究人员理解 iOS 沙箱逃逸技术，提升防御能力。请勿将报告内容用于任何非法目的。所有概念性代码仅用于说明原理，非实际可利用代码。*
