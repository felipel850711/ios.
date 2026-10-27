@@TITLE@@iOS系统内核安全漏洞分析报告
@@SUBTITLE@@XNU Kernel Security Architecture & Vulnerability Analysis（事实校正版）
@@INFO@@报告日期|2026年10月11日
@@INFO@@分析范围|iOS/iPadOS 系统内核（XNU Kernel）安全架构与漏洞
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含针对他人设备的操作步骤
@@INFO@@校正说明|2026 年 10 月按 Apple 安全响应顾问页、NVD/MITRE、CISA KEV 与 Google GTIG 一手来源逐条重核 CVE 口径；原稿中不成立的条目已改写或删除
@@INFO@@难度|进阶（需了解 XNU 分层、顾问页措辞惯例与 ARM 指针认证模型）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# iOS系统内核安全漏洞分析报告

**报告日期：** 2026年10月11日  
**分析范围：** iOS/iPadOS 系统内核（XNU Kernel）安全架构与漏洞  

---

## 1. 概述

### 1.1 报告目的

本报告对iOS系统内核（XNU Kernel）的安全漏洞进行全面分析，涵盖内存损坏、权限提升、信息泄露、竞态条件、IOKit驱动、PAC、PPL 等核心攻击面，提供 CVE 口径表与防御建议。本报告不给出任何漏洞利用代码、概念验证程序或针对他人设备的操作步骤；凡涉及具体漏洞，只转述 Apple 公开顾问页写了什么、由哪个组件段落收录、在哪一版修复。

### 1.2 XNU内核简介

XNU（XNU's Not Unix）是Apple开发的混合内核，结合了Mach微内核和BSD宏内核的特性，是iOS、macOS、watchOS和tvOS的核心。

**核心组件：**
- Mach微内核：进程/线程管理、虚拟内存、IPC
- BSD层：文件系统、网络栈、POSIX API
- IOKit：设备驱动程序框架
- libkern：内核库
- SAN（Sanitizers）：内核调试工具

### 1.3 内核安全重要性

内核是操作系统的核心，拥有最高权限，控制所有硬件资源和系统调用。按 Apple《Apple 平台安全保护》的口径，内核一旦被改写会连带影响其上的完整性防线（PAC、PPL／SPTM 与 TXM、代码签名与 entitlement 判定）。需要在本期就记下两点边界：

- 本报告谈的是 iOS／iPadOS。**系统完整性保护（SIP）在手册层被明确归为 macOS 能力**（原文：「macOS utilizes kernel permissions to limit writability of critical system files with a feature called System Integrity Protection (SIP).」），iOS 上没有对应条目，因此本报告不在 iOS 语境下使用 SIP 作为防线环节。
- 「最高权限」是本报告对内核态的描述性说法，不是 Apple 的措辞；Apple 顾问页给出的后果句式是 `execute arbitrary code with kernel privileges`（中文页「以根权限执行任意代码」一类）。

---

## 2. XNU内核架构分析

### 2.1 Mach层

Mach层是XNU的核心，负责：
- 进程和线程管理
- 虚拟内存管理（VM Map）
- 消息传递（Mach Ports）
- 调度器

`task`、`ipc_port` 等 Mach 结构是这一层的入口点：任务对象代表一个进程的地址空间与权限上下文，端口对象代表进程间通信的引用。本节只说明这两类对象属于 Mach 层，不给字段布局，也不给任何操作序列。

**攻击面：**
- Mach Port操作（分配、释放、发送、接收）
- VM Map操作（映射、取消映射、保护）
- 任务端口与 Mach 端口的引用管理。**`task_for_pid` 是 XNU 源码层的内部入口名，Apple 顾问页不使用该词**，因此本篇不把它作为任何条目属性（见第 4.1.2 节）

### 2.2 BSD层

BSD层提供POSIX兼容的系统调用：
- 文件系统（VFS）
- 网络栈（TCP/IP）
- 进程管理（fork、exec、exit）
- 用户/权限管理

**攻击面：**
- 系统调用参数验证
- 文件描述符管理
- 网络协议解析
- 符号链接跟踪

### 2.3 IOKit框架

IOKit是设备驱动程序框架，基于面向对象C++：
- 用户态与内核态通信（IOUserClient）
- 设备驱动管理
- 电源管理

**攻击面：**
- IOUserClient方法（外部方法、异步方法）
- IOMemoryDescriptor（内存描述符）
- IOService通知
- 设备属性访问

### 2.4 内核扩展（KEXT）

**这一节需要限定：可加载的内核扩展（KEXT）是 macOS 的模型。** iOS 上没有面向第三方的 KEXT 加载路径——驱动能力留在系统自带集合内，第三方扩展类型走 DriverKit 在用户态实现。原稿本节列的"第三方驱动（NVIDIA、Intel 等）"属 macOS 语境，与本报告分析的 iOS 内核无关，已删除。

**与本报告的关联：** 第 11 章出现的驱动段落（AppleAVD、IOMobileFrameBuffer、CoreGraphics）都是 **Apple 自带的内核组件**，不是第三方扩展。把"第三方驱动质量参差不齐"当作 iOS 内核风险，是把 macOS 的攻击面搬到了 iOS。

**攻击面（按 iOS 实际形态）：**
- Apple 自带驱动的接口验证与输入解析
- 内核组件的内存管理与对象生命周期
- 文件系统组件（APFS）的解析路径

### 2.5 安全机制

iOS 内核之上的完整性防线，按手册层可直引的名称列：

- **SEP（Secure Enclave Processor，安全隔区）**：与主处理器隔离的子系统，负责密钥与生物特征数据
- **KTRR（Kernel Text Read-only Region）**：内核代码与只读数据区的硬件写保护。**该缩写的展开是"内核文本只读区"，不是"内核信任根"**，原稿把两者混为一谈
- **PPL（Page Protection Layer）**：约束受保护页面（用户代码与页表）的修改权
- **SPTM 与 TXM**：在 A15 或后续、M2 或后续芯片上取代 PPL
- **PAC（Pointer Authentication Code）**：对代码指针签名，验证失败即 abort
- **AMFI（Apple Mobile File Integrity）**：内核侧的签名与授权判定
- **KASLR（Kernel ASLR）**：内核地址空间布局随机化
- **沙盒（Sandbox）**：约束单个进程可访问的资源范围

**两点边界：**

- **SIP 不在这张表里。** 手册层把系统完整性保护明确归为 macOS 能力："macOS utilizes kernel permissions to limit writability of critical system files with a feature called System Integrity Protection (SIP)." iOS 上没有对应条目，也没有面向用户的开关。原稿在 iOS 防线清单里列 SIP，已删除。
- 这张表按**芯片代际分叉**：PPL 与 SPTM／TXM 不是同时存在于同一颗 SoC 上的两项，第 9.1 节给出适用范围原文；PAC 也不是全代际都有（arm64e 起）。

---

## 3. 内核内存损坏漏洞

### 3.1 堆溢出

#### 3.1.1 原理

内核堆溢出发生在向内核堆缓冲区写入超出其分配大小的数据时。

本节只把成因归到类别（外部长度未校验、生命周期不一致、索引越界、拷贝范围过宽等），不给出代码，也不给出任何构造步骤。

**成因归类（本报告分析术语）：** 相邻对象被写入波及（拷贝范围过宽）、分配器布局影响成因的可见性（第三方文献称"堆风水"）、Zone 分配器的粒度特性。本节只作归因，不给布局方法。

**影响：**
- 任意代码执行
- 权限提升
- 内核崩溃（Panic）

#### 3.1.2 CVE-2021-30860 (FORCEDENTRY)

**Apple 收录段落：** CoreGraphics  
**Apple 给出的后果句式：** "Processing a maliciously crafted PDF may lead to arbitrary code execution. Apple is aware of a report that this issue may have been actively exploited."（中文页作「处理恶意制作的 PDF 文件可能导致任意代码执行」）  
**首次修复：** iOS 14.8，2021-09-13，顾问页 /103150；Apple 另在 iOS 12.5.5 修复同一编号  
**致谢：** The Citizen Lab  
**在野归属：** CISA KEV 于 2021-11-03 收录，点名 FORCEDENTRY  
**证据分级：** A（Apple 顾问页可直引）

**原稿更正：** 原稿写「内核堆溢出 / CTFont / Babylon 渲染引擎 / iOS 14.8 及更早 / CVSS 8.6」。Apple 把该编号收在 CoreGraphics 段落，Impact 句式只说"处理恶意制作的 PDF"，未出现"堆溢出""CTFont""Babylon""ImageIO"任何一个词，也未出现 kernel privileges——即本篇不能把它当作**内核**漏洞引用，只能作为"PDF 解析路径上的在野条目"登记。"Babylon""CTFont 堆溢出"属第三方分析口径。「iOS 14.8 及更早」也不准确：修复版本是 14.8，受影响的是 14.8 之前的版本。CVSS 分数取自 NVD，Apple 顾问页从不给分，故本行删除。

**本节边界：** 本篇不复述投递方式、不重述利用步骤，只登记 Apple 写了什么、收在哪个段落、哪一版修复。

**成因归类：** Apple 未在本条目描述缺陷类别，因此本节不把它归入"堆溢出"这一类，归入本篇的"外部输入解析路径上的内存损坏类（Apple 未给类别）"。

#### 3.1.3 原稿条目 CVE-2022-22674（已删除：非 iOS 条目）

**核实结论：该编号不是 iOS 条目，本节作为已删除项登记。**

Apple 把 CVE-2022-22674 收在 **Intel Graphics Driver** 段落（macOS Monterey 顾问页，12.3.1，2022-03-31，HT213220），Impact 为 "An application may be able to read kernel memory"，缺陷类别是 out-of-bounds **read**（读取，不是写入）。在 67 份现役 iOS/iPadOS 顾问页与本篇取样的旧页中，该编号出现 **0 次**——iOS 从未收录过它。

**原稿三处不成立：**（1）「WebGPU 着色器编译器」——Apple 段落是 Intel Graphics Driver，与 WebGPU 无关；（2）「iOS 15.3 及更早 / iOS 15.4 修复」——该修复版本关系不存在；（3）「触发内核堆溢出」——Apple 措辞是读取内核内存，不是写入。

**缺口登记：** 本节原本是"堆溢出"类下唯一的 iOS 实例，删除后本篇**没有一个可直引的 iOS 内核堆溢出编号**。这是取数缺口，不是"iOS 不存在该类条目"的结论。

### 3.2 栈溢出

#### 3.2.1 原理

内核栈溢出发生在向内核栈缓冲区写入超出其大小的数据时。

本节只把成因归到类别（外部长度未校验、生命周期不一致、索引越界、拷贝范围过宽等），不给出代码，也不给出任何构造步骤。

**成因归类：** 栈帧上的写入越界——保存的返回地址与保存的寄存器同属"越界写会波及的相邻数据"这一类，本节只作类别归位，不讨论这些后果如何被使用。

**影响：**
- 内核代码执行
- 内核崩溃

#### 3.2.2 原稿条目 CVE-2021-30807（类别更正：Apple 未写"栈溢出"）

**Apple 收录段落：** IOMobileFrameBuffer  
**Apple 给出的后果句式：** "An application may be able to execute arbitrary code with kernel privileges. Apple is aware of the possibility that this issue may have been actively exploited."  
**缺陷类别：** Apple 措辞为 memory corruption（内存损坏），**未指明栈溢出**  
**首次修复：** iOS 14.7.1，2021-07-26，顾问页 /103145  
**致谢：** 匿名研究者  
**在野归属：** 在野声明来自 Apple 本页自身；CISA KEV 收录该编号。**Citizen Lab 对 FORCEDENTRY 只点名 CVE-2021-30860**，因此本篇不把 30807 写成 FORCEDENTRY 链条的一环。

**原稿更正：** 原稿写「内核栈溢出 / iOS 14.7 及更早 / iOS 14.8 修复 / CVSS 7.8」。修复版本应为 **iOS 14.7.1**（不是 14.8）；"栈溢出"不成立，Apple 只写 memory corruption。本节因此成为"栈溢出"类的**已更正条目**，不是实例。

**缺口登记：** 删除本实例后，本篇"栈溢出"类没有可直引的 iOS 编号。Apple 顾问页的类别词表本身也不区分堆/栈，它用的是 memory corruption、use-after-free、out-of-bounds write、integer overflow、logic issue、improved checks 这一层级的措辞——**"栈溢出"这个分类是分析报告加上去的，不是 Apple 的分类**。

### 3.3 Use-After-Free (UAF)

#### 3.3.1 原理

UAF发生在释放内存后继续使用该内存时。

本节只把成因归到类别（外部长度未校验、生命周期不一致、索引越界、拷贝范围过宽等），不给出代码，也不给出任何构造步骤。

**成因归类（本报告分析术语）：** 生命周期不一致——释放后仍有有效引用；以及重用后的内存不被类型约束。Apple 在这一类用到的类别词是 use-after-free 与 type confusion。

**影响：**
- 任意代码执行
- 权限提升
- 信息泄露

#### 3.3.2 CVE-2021-1782（Apple 类别是 race condition，已移至第 6.2.2 节）

**核实结论：原稿把该编号归为"内核 UAF"不成立，已按 Apple 的类别词移到第 6.2.2 节。**

Apple 把 CVE-2021-1782 收在 `Kernel` 段落，类别词是 **race condition**（竞态条件），不是 use-after-free；Impact 为 "A malicious application may be able to elevate privileges. Apple is aware of the possibility that this issue may have been actively exploited."。完整登记见第 6.2.2 节。

**原稿另外两处不成立：**（1）五步"攻击链"（触发 UAF → 重用释放的任务对象 → 内核代码执行 → 绕过沙箱 → 窃取数据）——Apple 页面没有任何一步的对应措辞，且"重用释放的任务对象"预设了 UAF 与任务对象两个未经支持的机制，整段删除；（2）"针对记者的 0day 攻击"与 Predator 归因——Apple 的致谢是**匿名研究者**，Predator 对应的是 2023 年的 CVE-2023-41991/-41992/-41993（第 10.2 节）。

**本节（UAF 类）的缺口：** 移走 1782 后，本篇在"内核 use-after-free"这一类下只剩第 3.3.3 节那条 Apple 未给类别的条目。**顾问页里确凿的 use-after-free 条目（如 CVE-2021-30858）落在 WebKit 段落，属用户态**，不能拿来充当内核 UAF 实例。

#### 3.3.3 CVE-2023-38606（Kernel：Apple 未给缺陷类别）

**Apple 收录段落：** Kernel  
**Apple 给出的后果句式：** "An app may be able to modify sensitive kernel state. Apple is aware of reports that this issue may have been actively exploited against versions of iOS released before iOS 15.7.1."（中文页：「App 或许能够修改敏感内核状态。有报告指出，在 iOS 15.7.1 之前发布的 iOS 版本中，这个问题可能已经遭到频繁利用……」）  
**缺陷类别：** **Apple 未给**——本页没有 use-after-free、也没有内存损坏的类别词，只写改进检查  
**首次修复：** iOS 16.6，2023-07-24，HT213841；Apple 另在 iOS 15.7.8 修复  
**致谢：** Kaspersky  
**在野归属：** 在野声明来自 Apple 本页，且 Apple 把受影响面限定在"iOS 15.7.1 之前发布的版本"  
**证据分级：** A（顾问页中英双页直引）

**原稿更正：** 原稿写「IOKit 中的 UAF 漏洞 / 恶意应用程序调用 IOUserClient 方法 / iOS 16.5.1 及更早 / CVSS 8.6」。四点都要改：段落是 **Kernel** 不是 IOKit；**UAF 这一类别在 Apple 页面里没有依据**；"调用 IOUserClient 方法触发"是本篇不能替 Apple 补上的机制；受影响范围应按 Apple 的"iOS 15.7.1 之前"表述，而不是"iOS 16.5.1 及更早"。

**第三方口径需分开写：** Kaspersky 在 Operation Triangulation 报告里把该编号读作"PPL bypass via MMIO registers"，并描述为零点击 iMessage 场景。这是**第三方结论**，Apple 页面没有 MMIO、没有 PPL、没有零点击字样，本篇引用时必须带"据 Kaspersky"限定语。

### 3.4 越界读写

#### 3.4.1 原理

越界读写发生在访问超出缓冲区边界的内存时。

本节只把成因归到类别（外部长度未校验、生命周期不一致、索引越界、拷贝范围过宽等），不给出代码，也不给出任何构造步骤。

**成因归类（本报告分析术语）：** 拷贝范围过宽。按 Apple 的后果句可分为读方向（`read kernel memory`／`read restricted memory`）与写方向（`modify sensitive kernel state`、`execute arbitrary code with kernel privileges`），第 11.5 节按此重列。

**影响：**
- 信息泄露
- 任意代码执行
- 权限提升

#### 3.4.2 CVE-2022-22675（AppleAVD：Apple 唯一明写"越界写"且在野的 iOS 条目）

**登记条目：CVE-2022-22675（AppleAVD，out-of-bounds write）**

**Apple 收录段落：** AppleAVD（视频解码驱动）  
**Apple 给出的缺陷类别：** out-of-bounds write（越界写入）——这是本篇取样范围内**唯一一个 Apple 明文给出"越界写"类别且有在野声明的 iOS 条目**  
**Apple 给出的后果句式：** "An application may be able to execute arbitrary code with kernel privileges"，并带在野声明  
**首次修复：** iOS 15.4.1，2022-03-31  
**第三方机制读法：** Project Zero 的 RCA 把缺陷点写在 `AVC_RBSP::parseHRD` 的缓冲溢出上。**该函数名是第三方从源码取出的，顾问页不出现**，引用须带"据 Project Zero"。  
**证据分级：** A（编号与措辞）／B（函数名，第三方）

**原稿本节条目要更正：** 原稿 3.4.2 写的是 CVE-2021-30858，称"AppleJPEGDecoder 中的内核越界写入 / iOS 14.7 及更早 / 触发内核代码执行"。核实结果：30858 属 **WebKit** 段落，缺陷类别是 **use-after-free**，Impact 为 "Processing maliciously crafted web content may lead to arbitrary code execution"（无 kernel privileges 字样），带在野声明，修复见 iOS 14.8（条目于 2021-10-25 加入页面）与 iOS 12.5.5。**它不是内核条目、不是越界写入，"AppleJPEGDecoder"在 Apple 页面上也不出现**，因此移出本章，登记在第 11.1 节的网页引擎行了。

**这一换说明的分类学问题：** 顾问页的类别词（out-of-bounds write）与后果句（kernel privileges）是分开的两个字段，"越界写 = 内核漏洞"这类推断来自把两者捆在一起。本节按 Apple 原文分别登记。

## 4. 内核权限提升漏洞

### 4.1 任务端口泄露

#### 4.1.1 原理

任务端口（task port）是Mach IPC的核心，拥有对任务的完全控制权。泄露任务端口等同于获得该任务的完全控制权。

本节只把成因归到类别（外部长度未校验、生命周期不一致、索引越界、拷贝范围过宽等），不给出代码，也不给出任何构造步骤。

**成因归类：** Mach 端口的引用与名字空间管理问题——端口被暴露给不该持有的接收方、生命周期不一致、释放后被重用。本节只归类别，不讨论端口名称的取值方式或搜索方法。

**影响：**
- 完全控制目标任务
- 读取/写入任务内存
- 注入代码
- 提升权限

#### 4.1.2 原稿条目 CVE-2020-27950（已归位到第 5.1.2 节：Apple 措辞是泄露）

**核实结论：该编号是泄露类，不是提权类，详见第 5.1.2 节。**

Apple 把 CVE-2020-27950 收在 `Kernel` 段落，描述句是 memory initialization issue，Impact 的后果是**内核内存泄露（disclosure）**，而不是权限提升。原稿写的"允许应用程序获得 task_for_pid 权限""提升权限到 root""iOS 14.2 及更早，14.3 修复"四项中，task_for_pid 与 root 两项在 Apple 页面没有依据——顾问页与本报告都不出现 task_for_pid 这个 XNU 内部符号作为条目措辞；修复版本应为 **iOS 14.2（2020-11-05）**，Apple 另在 iOS 12.4.9 修复。

**缺口登记：** 本节原本是"任务端口泄露"类唯一的实例，删除后本篇**没有任何可直引的 task_for_pid 相关顾问页条目**。Apple 顾问页的措辞层级是后果（"execute arbitrary code with kernel privileges"／"read kernel memory"／"modify sensitive kernel state"），不到 Mach 端口机制这一层。凡在本篇看到"任务端口泄露"作为分类，应读作 XNU 源码层的机制名词，不是顾问页分类。

### 4.2 IOUserClient权限提升

#### 4.2.1 原理

IOUserClient是用户态与内核态通信的接口。IOUserClient漏洞允许用户态代码在内核态执行。

本节只把成因归到类别（外部长度未校验、生命周期不一致、索引越界、拷贝范围过宽等），不给出代码，也不给出任何构造步骤。

**成因归类（本报告分析术语）：** 外部方法入口的参数校验与权限判定缺失。"选择器枚举""参数注入"是第三方文献的说法，顾问页不出现这些词，只给后果句；本节不给接口探测方法。

**影响：**
- 内核代码执行
- 权限提升到root
- 绕过沙箱

#### 4.2.2 CVE-2021-30807

本节与第 3.2.2 节是同一编号（CVE-2021-30807，IOMobileFrameBuffer，iOS 14.7.1 首修），不重复登记，只补两点与"IOUserClient 权限提升"这一节标题有关的核实。

**Apple 措辞层级：** 该条目原文只到 "An application may be able to execute arbitrary code with kernel privileges"，没有出现 IOUserClient、外部方法（external method）、选择器（selector）或任何驱动接口名词。原稿的「调用 IOMobileFramebuffer IOUserClient 外部方法 → 触发权限提升 → 获得 root 权限」这条链，是本篇不能替 Apple 补上的机制描述，已删除。

**"kernel privileges" 不等于 "root"：** 顾问页中文页把 "execute arbitrary code with kernel privileges" 译作"以根权限执行任意代码"一类，但英文原文的宾语是内核权限，不是 Unix uid 0。本节按英文原文口径写"内核权限"，不写"root"。

**归因边界：** Apple 段落名 IOMobileFrameBuffer 说明它是驱动侧条目，"IOUserClient 权限提升"是本报告按组件位置给出的分析分类，不是 Apple 的分类。

### 4.3 沙箱逃逸

#### 4.3.1 原理

iOS沙箱限制了应用程序的权限。沙箱逃逸漏洞允许应用程序突破沙箱限制。

**攻击向量：**
- 文件系统访问：访问受限文件
- 网络访问：访问受限网络资源
- IPC访问：访问受限IPC端口
- 设备访问：访问受限设备

**成因归类（本报告分析术语）：** 路径解析时机与引用归属判定（符号链接、文件描述符、Mach 端口）。**"沙箱逃逸"不是 Apple 的措辞**——67 份现役 iOS／iPadOS 顾问页里字面 "sandbox escape" 出现 0 次，Apple 的句式是 `An app may be able to break out of its sandbox`（见第 4.3.2 节）。本节不给突破方法。

**影响：**
- 访问受限资源
- 窃取敏感数据
- 持久化驻留

#### 4.3.2 CVE-2024-23278（libxpc：Apple 的沙盒措辞原句）

**Apple 收录段落：** libxpc（iOS 16.7.6 顾问页 /120880，编号 CVE-2024-23278）  
**Apple 给出的后果句式：** "An app may be able to break out of its sandbox"  
**证据分级：** A（顾问页直引）

**为什么换成这一条：** 原稿本节用 CVE-2022-26712 作为"iOS 内核沙箱逃逸"实例。核实结果是该编号属 **PackageKit**，只出现在 macOS 顾问页（Monterey 12.4 与 Big Sur 11.6.6，两页均 Released May 16, 2022），Impact 为 "A malicious application may be able to modify protected parts of the file system"，修复描述是 removing the vulnerable code，致谢 Mickey Jin (@patch1t)，不在 KEV；在 67 份现役 iOS/iPadOS 顾问页里出现 **0 次**。因此它既不是 iOS 条目、也不是沙箱逃逸、更不是内核条目，整节替换。

**"沙箱逃逸"的措辞边界：** 本篇对 67 份现役 iOS/iPadOS 顾问页与 5 份旧页做了全量普查，字面 "sandbox escape" 出现 **0 次**，Impact 句里连 escape 都不出现；中文页写「突破沙盒」「绕过沙盒限制」，从不写「沙盒逃逸」。所以本节的标题词是**本报告的分析术语**。可直引的 Apple 句式共 6 类，按出现频次取前两类：`An app may be able to break out of its sandbox`（最高频，24 次）与 `A malicious app may be able to break out of its sandbox`（9 次，/127110，App Intents，CVE-2026-28995）。

**边界：** Apple 在本条目没有说明缺陷类别、没有说明逃逸后的权限层级、没有任何利用步骤。本节也不补。

## 5. 内核信息泄露漏洞

### 5.1 内核内存泄露

#### 5.1.1 原理

内核内存泄露发生在内核将内核内存内容泄露给用户态时。

本节只把成因归到类别（外部长度未校验、生命周期不一致、索引越界、拷贝范围过宽等），不给出代码，也不给出任何构造步骤。

**成因归类（本报告分析术语）：** 未初始化或被复用的内存被带出内核边界。Apple 在这一类给的类别词是 memory initialization issue，后果句是 `read kernel memory`（见第 5.1.2 节的 CVE-2020-27950）。

**影响：**
- KASLR绕过：泄露内核基地址
- 敏感数据泄露：泄露密钥、密码等
- 辅助漏洞：为其他漏洞提供信息

#### 5.1.2 原稿条目 CVE-2021-30883（更正：Apple 归为内存损坏，不是泄露）

**Apple 收录段落：** IOMobileFrameBuffer（编号 CVE-2021-30883，顾问页 /103162）  
**核实结论：Apple 把它归为 memory corruption，不是内存泄露。** 后果句式属 "execute arbitrary code with kernel privileges" 一类，在野声明挂在 30883 自身条目上；修复版本是 **iOS 14.8.1（2021-10-26）**，不是原稿写的 14.8；CISA KEV 于 2022-05-23 收录。

**原稿更正：**「内核内存泄露 / 读取内核内存 / 泄露 KASLR 基地址 / CVSS 7.1」四处。Apple 页面没有 leak、没有 disclosure、没有 KASLR；"泄露 KASLR 基地址"是本篇不能替 Apple 补上的下游用途，整段删除。CVSS 分数来源是 NVD，不是 Apple。

**真正可放在"内核内存泄露"类的条目：** CVE-2020-27950 —— Apple 收在 `Kernel` 段落，描述句是 memory initialization issue，Impact 是内核内存 disclosure，"Apple is aware of reports that an exploit for this issue exists in the wild"，修复 iOS 14.2（2020-11-05）与 iOS 12.4.9，致谢 **Google Project Zero**。Project Zero 把它读作内核内存越界读以泄露指针，属第三方口径。该条目的完整登记见第 4.1.2 节的归位说明。

**本节结论口径：** 顾问页里"信息泄露"类条目的 Apple 措辞是 `read kernel memory` 与 `disclosure`／泄露，与"内存损坏"类不共用句式；把两者混在一张表里会导致分类失真。

### 5.2 内核指针泄露

#### 5.2.1 原理

内核指针泄露发生在内核将内核指针泄露给用户态时。

**攻击向量：**
- 错误消息：内核错误消息中包含指针
- 调试信息：调试信息中包含指针
- 日志输出：日志输出中包含指针

**下游后果归类（本报告的分析术语）：** 内核地址布局的随机化被削弱，以及依赖地址不确定性的利用难度下降。本节不给出任何地址计算、gadget 定位或函数地址推导方法——顾问页也从不写到这一层。

**影响：**
- KASLR绕过
- 辅助漏洞利用

#### 5.2.2 CVE-2022-32838（更正：PluginKit 逻辑问题，非内核指针泄露）

**Apple 收录段落：** PluginKit  
**Apple 给出的后果句式：** "An app may be able to read arbitrary files"  
**缺陷类别：** logic issue（Apple 措辞为 improved logic／逻辑问题）  
**首次修复：** iOS 15.6，2022-07-20  
**在野归属：** **无**——Apple 本页没有在野声明，该编号不在 CISA KEV  
**证据分级：** A

**原稿更正：** 原稿写「内核指针泄露 / 触发内核错误读取错误消息中的指针 / 绕过 KASLR / iOS 15.5 及更早，15.6 修复 / CVSS 7.1」。段落是 **PluginKit** 不是 Kernel，后果是**读取任意文件**不是泄露指针，且**没有任何在野声明**——原稿把它放进"信息泄露"章节并配一条 KASLR 绕过链，两处都要撤。

**缺口登记：** 撤销后，本篇**没有一个可直引的 iOS"内核指针泄露"编号**。这与顾问页的措辞层级一致：Apple 不给"指针泄露"这种缺陷类别，最接近的类别词是 memory initialization issue 与 "read kernel memory"。

## 6. 竞态条件漏洞

### 6.1 TOCTOU（Time-of-Check-Time-of-Use）

#### 6.1.1 原理

TOCTOU发生在检查和操作之间存在时间窗口时。

本节只把成因归到类别（外部长度未校验、生命周期不一致、索引越界、拷贝范围过宽等），不给出代码，也不给出任何构造步骤。

**成因归类（本报告分析术语）：** 校验点与使用点之间存在时间窗口，窗口内的名字解析结果可与校验时不同。Apple 给这类条目的类别词是 race condition（见第 6.2.2 节的 CVE-2021-1782），顾问页不出现 "TOCTOU" 字样；本节不给并发时序的操作方法。

**影响：**
- 访问受限文件
- 权限提升
- 沙箱逃逸

#### 6.1.2 原稿条目 CVE-2021-30861（已删除：WebKit 且属 macOS，非 iOS 竞态）

**核实结论：该编号不是 iOS 条目。**

Apple 把 CVE-2021-30861 收在 **WebKit** 段落，Impact 为 "A malicious application may be able to bypass Gatekeeper checks"。Gatekeeper 是 macOS 的安装来源校验机制，iOS 上没有对应组件；该条目见 macOS Monterey 12.0.1 与 Safari 15 顾问页，**不在 KEV**，也与竞态条件无关。

**原稿四处不成立：**「TOCTOU」——Apple 未给竞态类别；「内核中的 TOCTOU 漏洞」——段落是 WebKit；「允许应用程序访问受限文件、窃取敏感数据」——Apple 的后果是绕过 Gatekeeper 校验；「iOS 14.7 及更早 / iOS 14.8 修复」——该修复关系不存在。

**缺口登记：** 本节原本是"TOCTOU"类唯一的实例，撤销后本篇**没有一个可直引的 iOS TOCTOU 编号**。需要说明的是：Apple 顾问页的类别词表里没有 "TOCTOU" 或 "time-of-check time-of-use" 字样，竞态类条目通常写成 "A race condition existed…"，因此"TOCTOU"在本篇只能作为分析术语使用，不能作为 Apple 口径引用。

### 6.2 引用计数竞态

#### 6.2.1 原理

引用计数竞态发生在引用计数操作不是原子操作时。

本节只把成因归到类别（外部长度未校验、生命周期不一致、索引越界、拷贝范围过宽等），不给出代码，也不给出任何构造步骤。

**成因归类（本报告分析术语）：** 引用计数操作不具原子性，使生命周期判定与实际操作不一致；下游类别即 use-after-free 或 race condition。本节不给线程安排方法。

**影响：**
- UAF
- 任意代码执行
- 权限提升

#### 6.2.2 CVE-2021-1782（Kernel race condition：由第 3.3.2 节归位至此）

**移入本节的条目：CVE-2021-1782（Apple 段落 Kernel，类别 race condition）**

**Apple 给出的后果句式：** "A malicious application may be able to elevate privileges. Apple is aware of the possibility that this issue may have been actively exploited."  
**首次修复：** iOS/iPadOS 14.4，2021-01-26，HT212146  
**致谢：** 匿名研究者  
**在野归属：** 在野声明来自 Apple 本页  
**第三方读法：** Project Zero（Ian Beer，2022-04）把它读作 Mach **voucher** 的引用计数竞态。这是第三方机制描述，Apple 页面只写 race condition。

**原稿两处更正：**（1）原稿在 3.3.2 把 CVE-2021-1782 写成"XNU 内核 UAF"并配一条五步攻击链——Apple 给的类别是 **race condition**，不是 UAF，且五步链（重用释放的任务对象 → 获得内核代码执行 → 绕过沙箱 → 窃取数据）没有任何一页 Apple 文档支持，已删除；（2）原稿把 1782 归给 **Predator** 间谍软件——**该归因不成立**，Predator 的公开链条是 CVE-2023-41991/-41992/-41993（iOS 16.7 / 17.0.1，见 GTIG 与 Citizen Lab），与 2021 年的 1782 无关。

**本节的引用边界：** "elevate privileges" 是 Apple 的措辞，权限提升到哪一层 Apple 没说；本节不把它写成"提升到 root"或"获得内核代码执行"。

**原稿本节条目（CVE-2022-22675）的去向：** 该编号的 Apple 类别是 **out-of-bounds write**，不是引用计数竞态，已移到第 3.4.2 节登记。顺带更正原稿的另一处：22675 的首修版本是 **iOS 15.4.1（2022-03-31）**，不是"iOS 15.3 及更早、15.4 修复"。

## 7. IOKit驱动漏洞

### 7.1 IOUserClient漏洞

#### 7.1.1 原理

IOUserClient是用户态与内核态通信的接口。IOUserClient漏洞允许用户态代码在内核态执行。

本节只把成因归到类别（外部长度未校验、生命周期不一致、索引越界、拷贝范围过宽等），不给出代码，也不给出任何构造步骤。

**成因归类（本报告分析术语）：** 驱动接口的输入校验与内存处理。Apple 的措辞停在 memory corruption 与后果句，不描述接口调用方式。

**影响：**
- 内核代码执行
- 权限提升
- 沙箱逃逸

#### 7.1.2 CVE-2022-42828（后果是内核权限，段落却是 ppp）

**登记条目：CVE-2022-42828 —— 一个被 Apple 自己放错段落的例子**

**Apple 给出的后果句式：** "An app may be able to execute arbitrary code with kernel privileges"  
**缺陷类别：** Apple 未给，描述句是 improved memory handling  
**首次修复：** iOS 16.1 / iPadOS 16，2022-10-24  
**致谢：** 匿名研究者  
**在野归属：** 无在野声明，不在 KEV  
**证据分级：** A

**本节要说明的是段落归属问题：** 同一编号在 **iOS 16.1 页被归入 `ppp` 段落**，而在 macOS Ventura 13 页被归入 `VPN` 段落——Apple 自己的组件归类在这两份页上不一致。后果句式确实是"内核权限"，所以它常被下游分析直接写成"内核漏洞"。本篇的处理是：**后果归后果，段落归段落**，登记为"后果涉及内核权限、但段落不是 Kernel"，不与真正的 `Kernel` 段落条目同表。

**原稿的第三次重复：** 原稿在本节再次登记 CVE-2021-30807（该编号已在 3.2.2 与 4.2.2 出现三次），并把 IOMobileFramebuffer 的机制写成"调用外部方法触发内存损坏"。本篇改为**一处完整登记、其余交叉引用**，避免同一编号在一张表里被计成三个独立漏洞。

### 7.2 IOMemoryDescriptor漏洞

#### 7.2.1 原理

IOMemoryDescriptor用于描述和管理内存区域。IOMemoryDescriptor漏洞允许攻击者访问受限内存。

本节只把成因归到类别（外部长度未校验、生命周期不一致、索引越界、拷贝范围过宽等），不给出代码，也不给出任何构造步骤。

**成因归类（本报告分析术语）：** 内存区域的范围与权限描述被错误处理。读方向见 `read restricted memory`，写方向见 `modify sensitive kernel state`（第 11.5 节）。

**影响：**
- 信息泄露
- 任意代码执行
- 权限提升

#### 7.2.2 内存访问后果句式与 CVE-2024-23264（原稿条目已推翻）

**核实结论：原稿本节引用的 CVE-2022-26712 已推翻（见第 4.3.2 节说明），不再作为 iOS 内存描述类条目出现。**

**可直引的替代条目：** Apple 的 `An application may be able to read restricted memory` 句式对应 **CVE-2024-23264**，见于 iOS 16.7.6 顾问页 /120880 与 /120893 两份页面。**限定语：** 本期只核实到该句式与编号、页号对应关系，**组件段落与缺陷类别未逐字取回**，因此本篇对该条目只登记"Apple 用『读取受限内存』描述后果"这一点，不推断它是 IOMemoryDescriptor 相关、也不推断读写方向之外的能力。

**为什么"IOMemoryDescriptor 漏洞"这一节标题本身要加限定：** IOMemoryDescriptor 是 XNU 源码里的类名，属**源码层名词**；顾问页从不出现这个类名。把某个编号写成"IOMemoryDescriptor 漏洞"只能来自源码分析或第三方报告，不能来自 Apple 条目。原稿把它写成"创建 IOMemoryDescriptor → 访问受限内存 → 窃取敏感数据"的四步链，属本篇口径禁止的攻击链构造，整段删除。

**缺口登记：** 本节没有可直引的"内存描述符类"内核条目；`Kernel` 段落里 Apple 用于内存访问后果的措辞是 read kernel memory／read restricted memory／modify sensitive kernel state 三种，本篇第 11 章按这三种句式重列。

## 8. PAC：手册层写了什么、没写什么

本篇把原稿"8. PAC 绕过漏洞"改写为"PAC：Apple 手册层写了什么、没写什么"。原稿本章含一个不存在的 CVE、一段"PAC 预测／暴力破解／侧信道获取密钥"的实施性描述，以及把 QARMA 说成 Apple 口径的算法分类，三者都已处理。

### 8.1 手册层原文

《Apple 平台安全保护》「指针认证代码」一节的原文口径（zh-CN，正文区直引）：

「指针认证代码 (PAC) 用来防止对内存损坏错误的利用。系统软件和内建 App 使用 PAC 来帮助防止修改函数指针和返回地址 (代码指针)。」

「PAC 使用五种 128 位密钥值来签名内核指令和数据，并且每个用户空间进程都有其自己的 B 类密钥。」同页英文："PAC uses five secret 128-bit values to sign kernel instructions and data, and each user space process has its own B keys… Failure to verify results in an abort."

开发者文档《Preparing your app to work with pointer authentication》给出的机制句："Pointer authentication works by offering a special CPU instruction to add a cryptographic signature — or PAC — to unused high-order bits of a pointer…"，arm64e 的芯片适用范围也在此文档确认。

**证据分级：** A（三处均可按节锚点回取）

### 8.2 原稿本章的三处口径错误

**（1）"QARMA：ARM 官方算法 / Custom PAC：Apple 自定义算法"。** QARMA 与 QARMA3 在 Apple 任何页面（手册层、开发者文档、顾问页）出现 **0 次**。这两个名字只出现在第三方研究里——Project Zero《Examining Pointer Authentication on the iPhone XS》与 USENIX Security '23《Demystifying Pointer Authentication on Apple M1》。本节删除该分类；若后续要谈签名算法选择，必须以"据第三方逆向研究，Apple 未确认"起句。

**（2）"保护范围：返回地址、函数指针、数据指针"。** Apple 原文只把"函数指针和返回地址（代码指针）"并列，"数据指针"不是手册层给出的并列类别；A 类／B 类密钥的划分来自开发者文档，手册层只说"每个用户空间进程有其自己的 B 类密钥"。原稿把三者并列写成 Apple 的保护清单，属越界。

**（3）"遮蔽指针并加密签名""额外一层防御""不能防止所有内存破坏"。** 手册层无这些措辞。Apple 给的是失败语义——**验证不通过即 abort**，这是一个可引用的行为断言，比"多一层防御"这类评述更强。

### 8.3 本篇不设"PAC 绕过 CVE 清单"的原因

Google GTIG 对 Coruna 的公开归因（2026-03-03）里，WebContent 侧的 PAC bypass 原语共 5 个，内部代号 breezy、breezy15、seedbell、seedbell_16_6、seedbell_17，**每一条都标注 "No CVE"**。也就是说，在 Coruna 这个案例上，**PAC 绕过原语与 CVE 编号之间没有对应关系**——不存在可以引用的"PAC 绕过漏洞编号"。

**原稿本节列出的 CVE-2024-44312 不存在。** MITRE 返回 `CVE_RECORD_DNE`，NVD `totalResults: 0`，CISA KEV 无命中；Apple iOS 18.1 页（/121563）的 44xxx 段止于 CVE-2024-44302，iOS 18.1.1 页（/121752）只有 44308 与 44309。原稿挂在它上面的组件、iOS ≤18.0 的受影响面、18.1 的修复版本、Coruna 归因与 8.6 评分全部无从谈起，整节删除。

顺带一条同类核对：**CVE-2024-27846 亦不存在**（MITRE DNE），**CVE-2024-44314 不是 Apple 条目**（属 TastyIgniter 3.7.6 的 Incorrect Access Control，分配者 mitre）。凡在本系列其他篇章遇到这三个编号，按不存在或非 Apple 处理。

### 8.4 与 PAC 相邻、但常被误写成 PAC 的条目

iOS 18.1.1（/121752，Released November 19, 2024）只有两条：**CVE-2024-44308**（条目标题为 JavaScriptCore，Impact 原文含 "…actively exploited **on Intel-based Mac systems**"，致谢 Clément Lecigne and Benoît Sevens of Google's Threat Analysis Group，Apple 未给缺陷类别、只写 improved checks）与 **CVE-2024-44309**（WebKit，cookie management issue，Impact 同样带 "on Intel-based Mac systems" 限定）。两条同于 2024-11-21 进入 KEV。

三点必须写清：第一，Apple 把在野面**限定在基于 Intel 的 Mac**，不能推广成"iOS 上遭在野利用"；第二，这两条**与 Coruna 无关**（GTIG 从不提 44308/44309），Coruna 在 2024 年的对应编号是 **CVE-2024-23222**（iOS 17.3，Apple 未署名研究者，GTIG 内部代号 cassowary）；第三，KEV 全量 feed 里 "spyware" 出现 **0 次**，所以"用于商业间谍软件活动"不是 KEV 的措辞。

### 8.5 本节后果口径

手册层给出的可引用后果是"验证失败即 abort"。**"绕过 PAC 后可执行任意代码""持久化驻留""破坏完整性保护"都是本报告的分析术语**，不是 Apple 对本章任何条目的措辞；原稿"8.3 PAC 绕过影响"的这四条列表已按此归位，不作为事实断言保留。

## 9. 内核完整性防线：PPL 与 SPTM／TXM 的手册层口径

本篇把原稿"9. PPL 绕过漏洞"改写为内核完整性防线的官方口径核对。原稿本章把 PPL 的保护对象写成"内核代码段、内核数据段、内核页表、安全监控器"，并给出"页表覆盖／页表重用／页表注入"的绕过手法清单——这两处与 Apple 原文相反或超出原文，已处理。

### 9.1 手册层原文：PPL 与 SPTM／TXM 的关系

《Apple 平台安全保护》「操作系统完整性」一节（zh-CN 正文区直引）：

「**在 A15 或后续芯片和 M2 或后续芯片的 SoC 中，SPTM（与 TXM 结合使用）取代了 PPL**，它提供了更小的攻击面，且不依赖内核信任。」同页英文："SPTM is supported in the A15 or later and M2 or later SOCs and replaces Page Protection Layer on supported platforms."

PPL 保护对象原文：「确保只有 PPL 才能修改包含**用户代码和页表**的受保护页面。」同节并明确写出「**此保护在 macOS 上不提供**」，英文："This protection isn't offered in macOS because PPL is only applicable on systems where all executed code needs to be signed."

**证据分级：** A（两处均可按节锚点回取）

### 9.2 由此推翻的原稿断言

**（1）SPTM 的适用范围。** 原稿按"所有 Apple silicon 用 SPTM 取代 PPL"来写。原文限定了 **A15 或后续、M2 或后续**，A12–A14 与 M1 仍属 PPL 时代。这一条会影响本篇对机型跨度的任何推断，必须逐字带上限定语。

**（2）PPL 保护的对象。** 原文说的是包含**用户代码和页表**的受保护页面——PPL 的角色是约束"谁能让用户态代码页变为可执行"，而原稿把它写成"保护内核代码段与内核数据段"，方向不同。安全监控器（Secure Monitor）是 Exception Level 3 的概念，Apple 手册把它与 PPL 分开描述，不能并列进 PPL 的保护清单。

**（3）macOS 上的 PPL。** 原文一句「此保护在 macOS 上不提供」直接推翻了原稿"PPL 仍适用于 Intel Mac"的写法。

**（4）页表条目。** 原文同时提到"用户代码"和"页表"两类受保护页面，因此"页表受保护"这一点本身成立；不成立的是原稿据此给出的操作路径。

### 9.3 原稿"PPL 绕过技术"小节为何整节删除

原稿 9.2.1 列出"页表覆盖／页表重用／页表注入"三种攻击向量与"物理内存访问／页表遍历／页表修改"三种利用技术。这三行既不对应 Apple 的任何措辞，也构成本系列口径禁止的实施性描述（它把"改页表项以解除保护"写成了可执行动作）。本节整段删除，只保留一句机制性陈述：**在 Apple 的模型里，修改受保护页表是 PPL 独占的能力，因此任何能改页表的代码路径都被视为防线失效**——这是防线定义，不是绕过方法。

### 9.4 可引用的相邻条目

**CVE-2023-38606**（第 3.3.2 节完整登记）是本章唯一能挂上的编号：Apple 段落 `Kernel`、后果句 "modify sensitive kernel state"、在野声明限定于"iOS 15.7.1 之前发布的版本"、致谢 Kaspersky。**但 Apple 页面没有出现 PPL，也没有给出缺陷类别。**"PPL bypass via MMIO registers"来自 Kaspersky 的 Operation Triangulation 报告，属第三方结论，引用必须带"据 Kaspersky"。

**GTIG 的 Coruna 归因把 Gallium（即 CVE-2023-38606）列入内核提权／PPL bypass 一类**，同类的还有 CVE-2020-27932（Neutron）、CVE-2020-27950（Dynamo）、CVE-2023-32434（Photon）、CVE-2023-41974（Parallax）、CVE-2024-23225（Sparrow）、CVE-2024-23296（Rocket），另有 Gruber、Quark、Carbone、Pendulum 四个原语标注无 CVE。这是**第三方的分类**——它把 27950 归为 PPL bypass，而 Apple 对 27950 的措辞是泄露（第 5.1.2 节）。本篇把这个不一致登记为口径冲突，不做裁决。

**"PPL 绕过后的影响"：** 原稿"修改内核代码／持久化驻留／破坏完整性保护／绕过安全机制"四条是本报告分析术语，Apple 未对本章任何条目使用这些措辞。

## 10. 公开归因与案例名核对

本章只登记**公开归因关系**：谁在什么文档里把哪个编号归给哪个案例，Apple 页面自己写了什么。原稿的四段式"攻击链（阶段1 初始访问 → 阶段2 权限提升 → 阶段3 → 阶段4 载荷部署）"在本系列口径下不成立——它把不同来源的碎片拼成了一条可执行的步骤序列，而其中绝大多数步骤没有任何一页一手来源支持。本章删除全部攻击链列表，改为归因核对表。

### 10.1 FORCEDENTRY

**可直引的对应关系只有一条：** FORCEDENTRY = **CVE-2021-30860**（CoreGraphics，iOS 14.8，2021-09-13，/103150，致谢 The Citizen Lab）。CISA KEV 于 2021-11-03 收录该编号并在条目名里点名 FORCEDENTRY。

**原稿的第二个编号要撤：** 原稿把 CVE-2021-30807（IOMobileFrameBuffer）也写成 FORCEDENTRY 链条的一环。核实结果：30807 首修于 **iOS 14.7.1（2021-07-26，/103145）**，比 30860 早约七周，致谢是匿名研究者；Citizen Lab 对 FORCEDENTRY 只点名 30860。**两者是同期各自独立的在野条目，不是一条链。**

**也不得使用 "BOMBSQUAD" 这个名字。** 该名称在本篇取样的 Apple 页面、KEV 条目与 Citizen Lab 口径里都不作为 30807／30860 的案例名出现。

**Apple 侧的防线回应：** 手册层把 iMessage 侧的独立解析环境列为 Apple 公开的防御设计，这是**手册层事实**；但"FORCEDENTRY 因此被拦下"属推断，本篇不作因果断言。

### 10.2 Predator

**原稿归因不成立。** 原稿写「Predator 使用 CVE-2021-1782、CVE-2021-30883，2021–2022 年，iOS 14.x–15.x」。核实结果：Predator 的公开链条是 **CVE-2023-41991、CVE-2023-41992、CVE-2023-41993**，由 GTIG 与 Citizen Lab 在 2023 年披露，Apple 的修复落在 **iOS 16.7 与 17.0.1**（2023 年 9 月的两份"恶意应用程序或网页内容"顾问页）。年份、编号、受影响区间三项全部要改。

**1782 与 30883 与 Predator 无对应关系：** 1782 的致谢是匿名研究者（第 6.2.2 节），30883 的在野声明挂在自身条目、首修 iOS 14.8.1（第 5.1.2 节），两者都出现在 Predator 被披露之前两年。

### 10.3 DarkSword

**GTIG 的 DarkSword 页面（2026-03-18 口径）**给出的编号是 **CVE-2025-31277、CVE-2025-43510、CVE-2025-43520、CVE-2025-43529、CVE-2025-14174、CVE-2026-20700**，受影响区间 **iOS 18.4–18.7**，由 **iOS 26.3** 修复。

**原稿写法要更正：** 原稿写「2022–2024 年，iOS 15.x–17.x，多个未公开 CVE」并把 CVE-2022-22674／22675 并列为 DarkSword 的组成部分。核实结果：**22674 是 Intel Graphics Driver（macOS，iOS 从未收录）**，见第 3.1.3 节；**22675 是 AppleAVD 的 out-of-bounds write，首修 iOS 15.4.1（2022-03-31）**，Project Zero 的 RCA 把缺陷点写在 `AVC_RBSP::parseHRD` 的缓冲溢出上——22675 是一条真实的、有在野声明的 iOS 视频解码器条目，但它与 GTIG 的 DarkSword 页面无对应关系。

**本系列内部口径差异登记：** 本系列较早篇章曾把 22674／22675 与 DarkSword 并列。本篇不推翻已发布内容，只把两处并列的差异登记在此，供后续篇章统一取 GTIG 页面口径。凡引用"DarkSword = 22674+22675"，需注明这是本系列早期口径，不是 GTIG 页面口径。

### 10.4 Coruna

**GTIG 公开口径（2026-03-03）**：目标区间 **iOS 13.0（2019-09）～ 17.2.1（2023-12）**，共 **23 个利用**。

- WebKit RCE：CVE-2021-30952（buffout）、CVE-2022-48503（jacurutu）、CVE-2023-43000（terrorbird）、CVE-2024-23222（cassowary）；bluebird **无 CVE**。
- WebContent PAC bypass：breezy／breezy15／seedbell／seedbell_16_6／seedbell_17，**全部 No CVE**（见第 8.3 节）。
- 沙盒逃逸：CVE-2023-32409（IronLoader）；NeuronLoader 无 CVE。
- 内核提权／PPL bypass：CVE-2020-27932、CVE-2020-27950、CVE-2023-32434、CVE-2023-41974、CVE-2023-38606、CVE-2024-23225、CVE-2024-23296；Gruber／Quark／Carbone／Pendulum 无 CVE。

**Apple 官方打 "Coruna" 标签的只有 4 条**（/126632，iOS 15.8.7，2026-03-11）：CVE-2023-41974（**Kernel**）、CVE-2024-23222、CVE-2023-43000、CVE-2023-43010（后三条 WebKit 段落）。其中对 23222 的原句是 "This fix associated with the **Coruna** exploit was shipped in iOS 17.3 on January 22, 2024."

**一条常被写错的对应关系：** CVE-2024-44308／44309 与 Coruna **无关**（GTIG 从不提这两个编号），它们的在野面被 Apple 限定在基于 Intel 的 Mac。另一条：**不存在 Project Zero 的 Coruna 文章**（站点检索与限定域检索均 0 结果），引用 Coruna 机制时不能把 Project Zero 列为出处。

### 10.5 Operation Triangulation

**唯一一手来源是 Kaspersky**（Photon = CVE-2023-32434、Gallium = CVE-2023-38606，零点击 iMessage 场景，MMIO 读与 PPL bypass 描述）。Apple 侧对 38606 只写 `Kernel` 与 "modify sensitive kernel state"，**未写 PPL、未写 MMIO、未写零点击**。因此本章的口径是：**Triangulation 是第三方案例名，不是 Apple 条目属性**；Apple 顾问页里不存在这个词。

### 10.6 归因核对表

| 案例名 | 出处层级 | 可直引编号 | Apple 页面是否写该案例名 |
|---|---|---|---|
| FORCEDENTRY | KEV + Citizen Lab | CVE-2021-30860（仅此一条） | 否 |
| Predator | GTIG + Citizen Lab | CVE-2023-41991 / 41992 / 41993 | 否 |
| DarkSword | GTIG | CVE-2025-31277 / 43510 / 43520 / 43529、CVE-2025-14174、CVE-2026-20700 | 否 |
| Coruna | GTIG + Apple /126632 | Apple 自己标 Coruna 的只有 4 条：2023-41974、2024-23222、2023-43000、2023-43010 | **是**（仅 /126632 一条页） |
| Operation Triangulation | Kaspersky | CVE-2023-32434、CVE-2023-38606 | 否 |

**表读数说明：** 五个案例名里只有 Coruna 被 Apple 在顾问页正文中点名，且只在一份页上。这说明**"某个 CVE 属于某个商业间谍软件链"这件事，默认来源不是 Apple**。

### 10.7 本章缺口清单

- 没有一份 Apple 页面给出任何案例的**利用步骤顺序**；本章因此不含攻击链阶段列表。
- 没有任何 Apple 页面出现 "spyware"（KEV 全量 feed 同样 0 次）、"零点击"（zero-click 在顾问页 0 次）作为条目属性。
- PAC bypass 原语与 CVE 编号的对应关系在 Coruna 一侧为空集，无法从顾问页反查。
- Apple 对 38606 与 27950 的类别措辞与 GTIG／Kaspersky 的分类不一致（泄露 vs PPL bypass），本篇登记冲突而不裁决。

## 11. CVE 数据库（按顾问页段落与在野声明来源重列）

本章是原稿的 CVE 总表，按前两批与第三批核实结果重列。**原稿每张表的 CVSS 列已整列删除**：Apple 顾问页从不给 CVSS 分数，原稿的数字全部来源不明，其中至少一个（CVE-2022-26712 的 7.8）是从另一条编号（CVE-2022-42858，macOS AMD 段，NVD 7.8 HIGH）搬过来的。若后续需要分数，必须注明取自 **NVD** 且可能随分析更新。

本章统一用四列：**编号 / Apple 收录段落 / 首次修复 / 在野声明来源**。"在野声明来源"区分三档：**Apple 本页**（顾问页自己写了 "actively exploited"）、**KEV**（CISA 收录）、**第三方**（只有 GTIG／Kaspersky／Citizen Lab 说，Apple 未写）。这三档的证据强度不同，混成一列是原稿最大的口径问题。

### 11.1 内核与系统组件段落，Apple 本页或在野声明成立

| 编号 | Apple 收录段落 | 首次修复 | 在野声明来源 |
|---|---|---|---|
| CVE-2021-30860 | CoreGraphics | iOS 14.8（2021-09-13，/103150）；另 iOS 12.5.5 | Apple 本页 + KEV 2021-11-03（FORCEDENTRY） |
| CVE-2021-30807 | IOMobileFrameBuffer | iOS 14.7.1（2021-07-26，/103145） | Apple 本页 |
| CVE-2021-30883 | IOMobileFrameBuffer | iOS 14.8.1（2021-10-26，/103162） | Apple 本页 + KEV 2022-05-23 |
| CVE-2021-1782 | Kernel（race condition） | iOS/iPadOS 14.4（2021-01-26，HT212146） | Apple 本页 |
| CVE-2020-27950 | Kernel（memory initialization issue） | iOS 14.2（2020-11-05）；另 iOS 12.4.9 | Apple 本页；致谢 Google Project Zero |
| CVE-2022-22675 | AppleAVD（out-of-bounds write） | iOS 15.4.1（2022-03-31） | Apple 本页 |
| CVE-2023-38606 | Kernel（Apple 未给类别） | iOS 16.6（2023-07-24，HT213841）；另 iOS 15.7.8 | Apple 本页（限定"iOS 15.7.1 之前的版本"）；致谢 Kaspersky |
| CVE-2021-30858 | WebKit（use-after-free） | iOS 14.8（条目 2021-10-25 加入）；另 iOS 12.5.5 | Apple 本页；**非内核条目**，原稿误归内核 |
| CVE-2024-23222 | WebKit（type confusion） | iOS/iPadOS 17.3（2024-01-22，/120304）；另 iOS 16.7.5 | Apple 本页（"may have been exploited"）+ KEV 2024-01-23；Apple 在 /126632 点名 Coruna |
| CVE-2024-44308 | JavaScriptCore | iOS/iPadOS 18.1.1（2024-11-19，/121752） | Apple 本页，**限定"on Intel-based Mac systems"** + KEV 2024-11-21；致谢 Google TAG |
| CVE-2024-44309 | WebKit（cookie management issue） | iOS/iPadOS 18.1.1（2024-11-19，/121752） | 同上，同样带 Intel Mac 限定 + KEV 2024-11-21 |

**表读数说明：** 这 11 行里，段落真是 `Kernel` 的只有 3 行（1782、27950、38606）；AppleAVD 与 CoreGraphics 属驱动／图形栈，其余 4 行属网页与脚本引擎。**顾问页意义上的"内核条目"比原稿表少的多。**

### 11.2 有条目、无在野声明（不可写成零日）

| 编号 | Apple 收录段落 | 首次修复 | 备注 |
|---|---|---|---|
| CVE-2022-32838 | PluginKit（logic issue） | iOS 15.6（2022-07-20） | "An app may be able to read arbitrary files"；不在 KEV |
| CVE-2022-42828 | `ppp`（macOS 13 页归 `VPN`） | iOS 16.1（2022-10-24） | 后果句是 kernel privileges，段落不是 Kernel；不在 KEV |
| CVE-2024-23278 | libxpc | iOS 16.7.6（/120880） | "An app may be able to break out of its sandbox"；本期未逐字取回在野字段，按无在野引用 |
| CVE-2024-23264 | 本期未取回段落 | iOS 16.7.6（/120880、/120893） | 只登记句式 "An application may be able to read restricted memory" |

### 11.3 非 iOS 条目（只登记，不作为 iOS 引用）

| 编号 | Apple 收录段落 | 出现页 | 原稿误写 |
|---|---|---|---|
| CVE-2022-22674 | Intel Graphics Driver | macOS Monterey 12.3.1（2022-03-31，HT213220） | 写成 iOS 15.3/15.4 的 WebGPU 堆溢出 |
| CVE-2021-30861 | WebKit | macOS Monterey 12.0.1 / Safari 15 | 写成 iOS 内核 TOCTOU |
| CVE-2022-26712 | PackageKit | macOS Monterey 12.4、Big Sur 11.6.6（均 2022-05-16） | 写成 iOS 15.4/15.5 内核沙箱逃逸与 IOMemoryDescriptor |
| CVE-2022-42858 | AMD | macOS Ventura 13.1（2022-12-13）、Monterey 12.5 | 其 NVD 分数被搬到 26712 行 |

### 11.4 已删除的编号

| 编号 | 删除原因 |
|---|---|
| CVE-2024-44312 | **不存在**：MITRE `CVE_RECORD_DNE`、NVD `totalResults: 0`、KEV 0 命中；/121563 的 44xxx 段止于 44302，/121752 只有 44308/44309 |
| CVE-2024-27846 | **不存在**：MITRE DNE |
| CVE-2024-44314 | **非 Apple 条目**：TastyIgniter 3.7.6 的 Incorrect Access Control，分配者 mitre |
| CVE-2023-37499 | **非 Apple 条目**：HCL Unica Platform 持久型 XSS，CNA 为 HCL；顺带核对：iOS 16.6 页无 `Bluetooth` 段落，KEV 目录内亦无 Bluetooth 命名条目 |

### 11.5 按 Apple 后果句式重列（替代原稿的类型分类）

原稿按"堆溢出／栈溢出／UAF／越界读写／信息泄露／竞态"六类分表，这六层分类不在顾问页里。Apple 的条目只到**后果句式 + 一个粗类别词**。可枚举的句式共三种最常用：

| 句式 | 含义层级 | 本篇出现的编号 |
|---|---|---|
| `execute arbitrary code with kernel privileges` | 内核权限代码执行 | 30807、30883、42828 |
| `modify sensitive kernel state` | 敏感内核状态可被改写 | 38606 |
| `read kernel memory` / `read restricted memory` | 内核内存读取 | 27950（disclosure 措辞）、22674（macOS）、23264 |

Apple 给过的缺陷类别词，在本篇取样范围内是：**race condition**（1782）、**memory initialization issue**（27950）、**integer overflow**（30860）、**out-of-bounds write**（22675）、**out-of-bounds read**（22674）、**use-after-free**（30858，WebKit）、**type confusion**（23222，WebKit）、**memory corruption**（30807、30883；42858 属 macOS）、**logic issue / improved logic**（32838）、**improved checks（未给类别）**（38606、44308）。**"堆溢出""栈溢出""TOCTOU"三个词不在这张表里。**

### 11.6 归因与编号的对应关系

见第 10.6 节归因核对表。本节不重复列表，只补一条计数：GTIG 对 Coruna 公开 23 个利用，其中**约半数标注 No CVE**；Apple 自己打 Coruna 标签的只有 4 条。**两个来源给出的编号集合交集很小**，这就是"案例名 ↔ 编号"不能凭印象配对的根本原因。

## 12. 防御建议

### 12.1 系统层面

#### 12.1.1 保持系统更新

**建议：**
- 及时安装iOS更新
- 启用自动更新
- 监控安全公告
- 评估漏洞影响

**理由：**
- Apple定期修复已知漏洞
- 更新包含安全补丁
- 延迟更新增加风险

#### 12.1.2 启用安全机制

**建议：**
- 保持设备在受支持的主版本上，因为防线本身按芯片与系统版本分层
- 不关闭系统默认开启的执行保护与签名校验设置
- 关注 Apple 安全响应页，而非第三方汇总

**理由：**
- PAC 与 SPTM／TXM 不是全平台等价物。手册层原文把 SPTM 取代 PPL 限定在 **A15 或后续芯片、M2 或后续芯片**；A12–A14 与 M1 一代仍是 PPL 模型。同一份"防线清单"在不同机型上对应的不是同一套机制。
- PAC 的失败语义是"验证不通过即 abort"（手册层原文），这是 Apple 给的、可依赖的行为，不需要用户配置。
- **删除原稿的"启用 SIP（默认启用）"一条。** 手册层把系统完整性保护明确归为 macOS 能力："macOS utilizes kernel permissions to limit writability of critical system files with a feature called System Integrity Protection (SIP)." iOS／iPadOS 上没有对应条目，也没有面向用户的开关。在 iOS 防御建议里列 SIP，属把 macOS 能力错配给 iOS。
- 同理，"启用 KASLR／启用 PPL／启用 PAC"这三条也不成立：这些不是 iOS 上的可开关项，本篇把它们改成"版本与机型决定，用户不可配置"的陈述。

#### 12.1.3 限制应用程序权限

**建议：**
- 审查应用程序权限
- 限制不必要的权限
- 使用最小权限原则
- 监控权限使用

**理由：**
- 减少攻击面
- 限制潜在损害
- 防止权限滥用

### 12.2 应用程序层面

#### 12.2.1 安全编码实践

**建议：**
- 验证所有输入
- 使用安全API
- 避免不安全的函数
- 进行代码审计

**理由：**
- 防止常见漏洞
- 提高代码质量
- 减少安全风险

#### 12.2.2 沙箱合规

**建议：**
- 遵守沙箱规则
- 不尝试沙箱逃逸
- 使用官方API
- 避免私有API

**理由：**
- 沙箱提供保护
- 逃逸尝试可能失败
- 使用官方API更安全

#### 12.2.3 内存安全

**建议：**
- 使用ARC（自动引用计数）
- 避免手动内存管理
- 检查缓冲区边界
- 使用安全数据结构

**理由：**
- 防止内存损坏
- 减少UAF风险
- 提高内存安全性

### 12.3 用户层面

#### 12.3.1 谨慎安装应用程序

**建议：**
- 只从App Store安装
- 审查应用程序权限
- 检查应用程序评价
- 避免越狱

**理由：**
- App Store有安全审查
- 恶意应用程序风险高
- 越狱破坏安全机制

#### 12.3.2 警惕钓鱼攻击

**建议：**
- 不点击可疑链接
- 不打开可疑附件
- 验证发送者身份
- 使用安全通信应用

**理由：**
- 钓鱼是常见攻击向量
- 零点击攻击无需交互
- 但大多数攻击需要某种形式的交互

#### 12.3.3 监控设备行为

**建议：**
- 监控电池使用
- 监控数据使用
- 监控网络活动
- 检查异常行为

**理由：**
- 间谍软件消耗资源
- 异常活动可能表明感染
- 早期检测减少损害

### 12.4 企业层面

#### 12.4.1 MDM（移动设备管理）

**建议：**
- 部署MDM解决方案
- 强制安全策略
- 远程管理设备
- 监控合规性

**理由：**
- 集中管理设备
- 强制执行安全策略
- 快速响应安全事件

#### 12.4.2 安全培训

**建议：**
- 培训员工安全意识
- 教授识别钓鱼攻击
- 教授安全最佳实践
- 定期更新培训

**理由：**
- 人是安全链的薄弱环节
- 培训减少人为错误
- 提高整体安全性

#### 12.4.3 事件响应

**建议：**
- 制定事件响应计划
- 建立响应团队
- 定期演练
- 持续改进

**理由：**
- 快速响应减少损害
- 有计划的响应更有效
- 持续改进提高准备度

---

## 13. 总结

### 13.1 主要发现

1. **顾问页里真正落在 `Kernel` 段落的 iOS 在野条目极少**：本期 11 行带在野声明或内核相关后果的条目中，段落是 `Kernel` 的只有 3 行（CVE-2021-1782、CVE-2020-27950、CVE-2023-38606）
2. **Apple 的字段结构是"收录段落 + 后果句式 + 粗类别词"三层**，原稿的堆溢出／栈溢出／UAF／越界读写／信息泄露／竞态六分法不在顾问页的词表里
3. **案例名几乎都不是 Apple 措辞**：FORCEDENTRY、Predator、DarkSword、Operation Triangulation 四个名字在 Apple 页面出现 0 次，只有 Coruna 被点名，且只在 /126632 一份页上
4. **同一编号会被 Apple 放进不同段落**（CVE-2022-42828 在 iOS 16.1 页归 `ppp`、在 macOS Ventura 13 页归 `VPN`），所以只看后果句会误判组件层级
5. **在野声明有三档**：Apple 本页写明、KEV 收录、只有第三方说。原稿把三档并成一列，导致"零日"被过度断言
6. **防线描述按芯片代际分叉**：SPTM 取代 PPL 只适用于 A15 或后续、M2 或后续；A12–A14 与 M1 仍是 PPL 模型
7. **不存在的编号会长期流传**：CVE-2024-44312、CVE-2024-27846 经 MITRE 确认不存在，但已进入下游文本，删除时必须留下删除原因

### 13.2 攻击趋势

本节只写可从来源直读的趋势，不做预测。

1. **在野声明的密度不在"内核"这一层**：本期的在野条目多数落在网页与脚本引擎（WebKit、JavaScriptCore）、图形栈（CoreGraphics）与视频驱动（AppleAVD）段落，`Kernel` 段落反而是少数
2. **防线绕过原语长期拿不到编号**：GTIG 对 Coruna 公开的 5 个 WebContent PAC bypass 全部标注 No CVE，说明"PAC／PPL 绕过"这一层很难进入 CVE 体系——凡把某编号说成"PAC 绕过漏洞"，需要先看它是否真的存在
3. **Apple 会事后补发与案例绑定的修复页**：/126632（iOS 15.8.7，2026-03-11）把 Coruna 相关修复回贴到旧版本页面，这是本系列能直引案例名的唯一来源形态
4. **第三方归因先于 Apple 表述**：Predator、DarkSword、Triangulation 三案都由 GTIG／Citizen Lab／Kaspersky 先命名，Apple 页只给"actively exploited"与致谢对象
5. **"零点击""间谍软件"不是顾问页属性词**：zero-click 与 spyware 在本篇取样的顾问页与 KEV 全量 feed 中均出现 0 次，因此这类趋势判断不能声称来自 Apple
6. **原稿"多漏洞链利用"一句应读作第三方材料**：链的阶段划分来自 GTIG 与 Kaspersky 的报告，Apple 顾问页从不给出任何一条链的步骤顺序

### 13.3 防御挑战

1. **内核漏洞难以完全避免**：内核代码复杂，攻击面广泛
2. **"零日"只能靠事后声明**：Apple 的在野声明写在修复页上，所以任何"提前知道某编号是零日"的说法都不来自顾问页；第 11 章的三档来源就是为这件事准备的
3. **平衡安全性和可用性**：过度保护影响用户体验
4. **驱动风险来自 Apple 自带组件**：iOS 上没有第三方 KEXT 加载路径（见第 2.4 节），本期的驱动段落全是 Apple 自带组件，风险点在接口验证与输入解析
5. **持续监控需求**：需要持续监控和响应
6. **防线名称容易被跨平台搬运**：SIP 与 PPL 是两个已在本期纠正过的例子——防线清单必须按平台与芯片代际分别取原文

### 13.4 未来展望

1. **防线按芯片代际继续分层**：SPTM／TXM 只覆盖 A15+／M2+，机型差异会让"同一份防线描述"对应不同实现，后续分析必须逐代核对
2. **顾问页措辞的稳定性是本系列的主要依据**：Apple 不给 CVSS、不给利用步骤、不给案例名（Coruna 是唯一例外），这三点短期内不会变
3. **形式化验证与内存安全语言**属研究趋势，不是 Apple 对 iOS 内核公布的路线，本篇作为展望记述而非事实
4. **第三方归因与 Apple 条目的错位会持续存在**：PAC bypass 原语大多无 CVE，而 Apple 打案例标签的只有 4 条——两边靠编号对不上，只能靠句式与页号对齐

### 13.5 关键要点

1. **保持系统更新**：Apple 对在野条目的回应只有"某版修复"，用户侧唯一可操作项就是版本
2. **防线不可开关**：PAC、PPL／SPTM 由芯片与系统版本决定；iOS 上不存在 SIP 开关
3. **区分三档证据**：Apple 本页的在野声明、KEV 收录、第三方报告的说法，强度不同，本篇逐条标注
4. **区分四个层级**：手册层（机制）、顾问页层（条目措辞）、源码层（XNU 类名与内部符号）、第三方层（GTIG／Kaspersky／Citizen Lab／Project Zero）——原稿的绝大多数错误都来自把第四层的说法当第一层引用
5. **只登记可直引的内容**：task_for_pid、IOMemoryDescriptor、MMIO、Babylon、QARMA 这些词在第一层与第二层都不出现，出现它们的段落必须标明来源层级
6. **不做因果断言**：某编号"导致"某案例的攻破、某防线"拦下"了某攻击，本篇一律不写
7. **不存在也要写**：CVE-2024-44312、CVE-2024-27846 经 MITRE 确认不存在，这类编号一旦进入表格就会长期流传，删除时须留删除原因

---

**边界声明：** 本篇是一份面向防御与安全研究工作的机制与措辞分析。全文只使用 Apple《Apple 平台安全保护》手册、Apple 安全响应顾问页、NVD/MITRE、CISA KEV 与 Google GTIG 等公开来源作为材料，交付内容是"官方说了什么、收在哪个段落、在哪一版修复、缺什么"。本篇不提供任何漏洞利用代码、不提供概念验证程序、不描述任何针对他人设备的操作步骤，不构造攻击链，不给出绕过 PAC、PPL／SPTM 或沙盒的具体实施方法；第 8、9、10、11 章列举的 CVE 与案例名只用于展示各来源的公开措辞与其相互关系，不构成对任何机型当前可被攻破的判断。凡本篇出现的"可以""能够""允许"，均为来源原文措辞的直接引用，不代表本篇的可行性结论。本篇对无法证实的项目一律标注为未核实、口径冲突或取数缺口，不作为可执行建议。

**联系方式**：Telegram: https://t.me/one00190
