@@TITLE@@iOS 内核安全机制深度解析：XNU 各子系统的职责边界、强制点与可观测面
@@SUBTITLE@@How XNU Divides Responsibility Across Mach, BSD and I/O Kit — Enforcement Points, Preconditions and Observability of the iOS Kernel Security Mechanisms
@@INFO@@报告日期|2026年10月19日
@@INFO@@分析对象|XNU 内核子系统分层、Mach IPC、内核内存分配、MACF、AMFI、代码签名强制、IOKit 边界、控制流与指针保护、异常与调试权限
@@INFO@@视角定位|机制纵向拆解（每个子系统各自的职责、强制点与边界），与系列内按漏洞类别横向铺开的相邻篇目互补而非重复
@@INFO@@分类|网络安全 · 内核机制解析 · 防御研究
@@INFO@@难度|高级（需具备操作系统、ARM64 与 Mach/IOKit 基础概念）
@@INFO@@联系方式|Telegram: https://t.me/ios_one001

@@TOC@@目录

# 概述

## 1.1 为什么按机制纵向拆解

iOS 内核安全的公开叙述大多按漏洞类别横向铺开：一次 use-after-free、一处类型混淆、一条完整的越狱链。这种叙述方式对复盘很有价值，但它有一个副作用——它把 XNU 内部数十个职责各异的子系统压扁成"内核有漏洞"这一件事，读者很难从中还原出：某个安全决定究竟在内核的哪一个函数、由哪一层发起、依赖什么输入、在什么条件下根本不会发生。

本篇采取相反的组织方式：**以机制为单元纵向拆解**。每个子系统单独成章或成节，并统一回答四个问题：

| 问题 | 含义 | 为什么必须回答 |
|---|---|---|
| 它负责什么 | 该子系统被设计来阻断的具体失效类别 | 避免把"存在某个开关"误读为"该风险已被消除" |
| 强制点在内核何处 | 具体的文件、函数、调用链位置（以源码路径:行号标注） | 决定绕过讨论的层次，也决定审计时该看哪段代码 |
| 生效前提 | boot-arg、entitlement、编译开关、平台形态、进程类型 | 同一机制在不同设备上可能完全不同强度 |
| 它不覆盖什么 | 明确的负面边界 | 这是防御工作最有价值也最常被省略的一栏 |

这种切法还与系列内其他篇目互补而非重复：横向篇目按漏洞类别覆盖跨子系统的现象，本篇按子系统覆盖机制本身的结构。两者共用同一套证据分级（见第 2 章），因此读者可以把本篇的机制结论直接当作横向分析的"生效前提清单"来查。

这套四问也不是凭空设定的提问方式，它在 Apple 的一手材料里有对应物。其一，Apple 对 XNU 的自述本身就是按可辨识来源切开描述的——"a hybrid kernel combining the Mach kernel developed at Carnegie Mellon University with components from FreeBSD and a C++ API for writing drivers called IOKit"【一手-源码 src/README.md:4】；其二，Apple 随源码发布的仓库内文档也确实按"哪种用途被允许做什么、违反后发生什么"的形态写成，例如 `doc/port_types.md` 逐个端口类型绑定安全语义【一手-官方doc doc/port_types.md】。既然 Apple 自己就把内核分成职责可辨的层次并写下类型级策略，"强制点落在何处"就是一个可逐层作答的问题；四问只是把这种描述方式变成每节的必答题。

需要在一开始就说明的方法论代价：纵向拆解会放大"口径缺口"。因为机制的内部实现细节并不等于它的公开描述，把一个子系统写成完整小节时，凡是 Apple 未公开、公开源码又不可观察的部分，都必须原地写成缺口，而不是用相邻事实填补成流畅的叙述。本篇因此在每一章末尾都保留一段"本章口径边界"。

## 1.2 本篇覆盖的九类机制

按"机制纵向拆解"的口径，整份报告覆盖九类内核安全机制。下表列出每类机制的主要承载位置与本报告的展开层次，其中前三类在本篇（第 3–5 章）完整展开，其余六类在同一报告的后段章节展开；最后一列标注该类机制在公开材料中的**一手口径可得性**，它是读者判断后续论证强度的第一道提示。

| 机制类别 | 主要承载（目录/文件级） | 本篇展开位置 | 一手口径可得性 |
|---|---|---|---|
| XNU 子系统分层与职责边界 | `README.md`、`osfmk/`、`bsd/`、`libkern/`+`security/`、`pexpert/`、`config/MASTER.arm64.iPhoneOS` | 第 3 章 | 高（Apple 自述目录职责，一手-源码） |
| Mach IPC 与 port right | `ipc_object.h`、`ipc_port.h`、`doc/ipc_security_concepts.md`、`doc/port_types.md`、`doc/guard_exceptions.md` | 第 4 章 | 中高（官方 doc 存在，但名额/限额与 `mach_msg(2)` 手册页缺失） |
| 内核内存分配与分配器加固 | `zalloc.c`、`vm_kern.c`（均属 Mach 核心子系统，见 3.2 的目录职责口径） | 第 5 章 | 高（宏名、zone 命名逐个可比对源码） |
| MACF 强制访问控制框架 | `security/mac_*.c`、`mac_policy.h`、`security/conf/files` | 后段章节 | 中（框架一手；iOS 已启用策略模块清单官方从未公布） |
| AMFI 与 entitlement 判定 | `libkern/libkern/amfi/amfi.h`、`libkern/amfi/amfi.c`；Apple 官方称 AppleMobileFileIntegrity kernel extension | 后段章节 | 中（内核侧接口一手；kext 本体与 `amfid` 职责无公开口径） |
| 代码签名强制与 CSM | `code_signing.h`、`kern_codesigning.c`、`pmap_cs.c/h`、`osfmk/kern/cs_blobs.h` | 后段章节 | 中高（源码与《Apple Platform Security》可互证） |
| IOKit 与用户客户端边界 | `iokit/Kernel/IOUserClient.cpp/.h` | 后段章节 | 中（框架注释一手；"某 iOS 版本收紧某类驱动"无官方口径） |
| 控制流完整性与指针保护（PAC/BTI/GCS/KASLR） | `arm_vm_init.c`、`osfmk/arm64/proc_reg.h`、`ccarm_pac_bti_macros.h`、`ptrauth.h` | 后段章节 | 中（源码具备能力，构建开关取值部分未公开） |
| Mach 异常分层与调试权限 | `osfmk/kern/exception.c`、`ux_handler.c`、`bsd/uxkern/ux_exception.c`、`bsd/kern/kern_sig.c` | 后段章节 | 中高（源码 + Apple 随源码发布的 man page 均为一级；Apple 面向用户的机制文档为缺失项） |

表中位置的锚点可以当场复核，三例足以说明这张表不是目录清单式的想象：目录职责与 hybrid kernel 定性出自 README【一手-源码 src/README.md:4】，Mach IPC 的类型级策略出自随源码发布的仓库文档【一手-官方doc doc/port_types.md】，zone 命名规则与分配上限出自 `zalloc.c`【一手-源码 zalloc.c:158, 2847-2859】。最后一列的"可得性"评估即按这三类锚点的密度给出，读者可据此判断后段章节的论证强度上限。

## 1.3 明确不覆盖什么

一份机制解析的价值很大程度上取决于它承认自己的边界。以下内容**不在本篇范围内**：

**其一，SPTM、TXM 与 Exclaves 的机制主体。** 这三者已由系列内另一篇目专门覆盖，本篇只在必要处做一句话级别的交叉引用（例如第 3.5 节说明代码签名监视器可运行于 PPL 或 TXM 之上、第 5.6 节说明 `SECURE_KERNEL` 与"可信内核新功能"的措辞关系），不重述其设计、启动度量与隔离细节。需要说明的是"不覆盖"不等于"无公开口径"——PPL 与 SPTM/TXM 的更替关系本身有一手文档可引："In the A15 or later and M2 or later SOCs, SPTM (in combination with TXM) replaces the PPL"【一手-文档 指南p.59-60】，本篇引用的正是这一句级别的边界，而非其机制展开。

**其二，按漏洞类别横向铺开的利用面梳理。** iOS 内核漏洞的历史形态、在野链组合与影响面，属于相邻两篇的口径；本篇提到历史漏洞时，只写到「Apple 公告层面的组件名与影响摘要」这一层，不写利用路径。

**其三，任何可直接照做的操作序列。** 本篇不包含利用代码、概念验证、绕过步骤、提权或提取流程。涉及"失效模式"的段落只回答一个问题：在什么前提下该机制不提供保护——这是审计与加固所需的信息，而非操作指南。

**其四，闭源实现的内部细节。** AMFI 内核扩展本体、Sandbox 策略模块、`board_config` 的设备清单、CoreEntitlements 用户态库均不在公开源码范围内；Apple 未公开的内部实现，本篇一律写成缺口，不用推测替代。这条边界的两侧其实不对称：对外定性是一手可证的——Apple 明确写作 "the RESEARCH variant of the AppleMobileFileIntegrity kernel extension"【一手-文档 指南p.85】；不可证的只是该 kext 内部如何判定。因此本篇会引用"它是内核扩展"这一层，而不写任何判定流程。

**其五，零售构建的取值确认。** 公开源码能看到"机制存在"与"开关名称"，但某个开关在消费者设备上是否取值打开，往往无法从公开材料判定——本篇会在第 2.4 节以 `BTI_ENFORCED`（公开源码树中只有 `#if` 使用点、无任何定义处）作为完整样本。本篇不会把"源码里有"写成"设备上开着"。

## 1.4 读者画像与使用方式

本篇预设读者具备三类背景：操作系统原理（虚拟内存、进程/线程模型、系统调用与 IPC 基本概念）、ARM64 体系结构（异常等级、`pac`/`bti` 类指令的存在性，不要求会写）、以及 Mach 或 IOKit 的任一线上经验。缺少 Mach 背景的读者可以先读第 4.1 节的权利词汇表，它是后续所有能力式表述的基础。该词汇表的每一行都能回指一手出处，可作"每句都要有锚点"这一写作纪律的最小样例：send-once right 见 `ipc_port.h:511`【一手-源码 ipc_port.h:511】，dead-name entry 由内核分配这一点见 `ipc_object.h:519-520` 的 `ipc_object_alloc_dead`【一手-源码 ipc_object.h:519-520】。

推荐使用方式有三种：

| 使用场景 | 建议读法 |
|---|---|
| 排查"某个保护为什么在我的设备上没生效" | 直接查该机制小节的**生效前提**行，对照 boot-arg、entitlement、设备形态、进程类型（平台二进制/hardened/普通 App） |
| 为内核审计圈定检查点 | 读**强制点在内核何处**，把给出的文件路径当作 grep 起点；注意行号锚定说明（第 2.3 节） |
| 评估加固配置与可观测性 | 读第 4.6、5.6 两节的"可观测面"与"缺口"，以及每章末尾的口径边界，确定哪些结论能对外引用、哪些只能内部留档 |

反向提示：本篇不适合被当作"攻击面清单"使用。机制的强制点位置与失效前提在报告里是为了让防御者知道自己依赖了什么；把它们孤立地读成"此处可打"会丢掉全部生效前提信息，结论通常是错的。

同理，本篇**不提供**利用代码、概念验证、可直接照做的绕过/提权/提取步骤或攻击操作序列。涉及"失效模式"的段落只回答一个问题：**在什么前提下该机制不提供保护**。读者若需要把该信息用于渗透测试或攻防演练，应另行取得书面授权与安全边界约束，报告本身即为此类用途的适用性设了限。

一条实用的自检问题：读完某一小节后，能否写出"若我要依赖这个保护，我需要确认的三件事是什么"？如果答案是"能不能构造一个绕过"，说明阅读方式偏了——本篇的每个四问表都是为前者设计的。

## 1.5 来源等级如何在正文里呈现

所有事实性陈述后都跟着一个行内标注，形如 `【一手-源码 路径:行号】`、`【一手-文档 指南p.114】`、`【公开研究 机构/作者/年份】`。这套标注不是装饰，它决定该论断能被引用到什么场合。三级最常用的标注含义如下（完整六级体系见第 2.1 节）：

- **一手-源码**：Apple 以 APSL 2.0 开源的 XNU 树中存在该字符串/函数/注释原文。它证明的是"源码如此"，不自动证明"Apple 承诺该行为"或"零售构建启用了它"。
- **一手-文档**：Apple 面向用户或开发者的官方文档、或 Apple 随源码发布的官方 doc 与 man page。这是可以对外直接引用的层级。
- **分析推断**：由上述两类事实组合得出、但没有任何单一来源直接陈述的结论。写作时必须显式带此标注，且不得出现在"Apple 表示"这类句式里。

当一个论断同时存在多个层级来源时，正文按第 2.2 节规则处理：**同一论断取最高层级；混合来源必须拆开表述**。典型的拆表示例是"port right 即 capability"——源码注释层一手存在，Apple 开发者/支持文档层查无此表述，因此不能合并写成"Apple 文档称 port right 为 capability"。

## 1.6 本篇的诚实边界

本篇最需要注意的是：**大量内核内部实现在 Apple 公开材料里没有口径**。这不是资料没找全，而是一种结构性缺失——公开源码只包含 Apple 选择开源的部分（框架、调用点、注释、随源码发布的 doc），而策略模块、设备配置、零售构建开关、面向用户的机制文档分别落在不同的不可见区域。

因此本篇采用如下统一处理：

| 不可得类型 | 本篇写法 | 禁止写法 |
|---|---|---|
| 公开源码里存在但取值不明 | "源码具备该能力，构建开关取值 Apple 未公开" | 断言设备上已启用 |
| 源码里没有，但闭源组件可能存在 | "开源树内未见；相关实现不在公开源码范围" | 断言该机制不存在 |
| 官方文档缺失某主题 | "本次核查未获得可核实条目，属审计缺口" | 用三方措辞冒充官方口径 |
| 具体数字（名额、上限、计数阈值） | "未公开，来自源码观察" | 写出无一手来源的数字 |
| 简报中标为 `未核实` / `部分核实` 的条目 | 照实写成缺口或加限定语 | 写成确定事实，或反向断言其不存在 |

这五条纪律贯穿全篇。读到某节结束时若只得到"这里没有官方口径"，那不是写作失败，而是本篇的产出之一：它把不确定的位置标定了下来。

**本章口径边界**：第 1 章除"九类机制的承载路径"（一手-源码目录级事实，见 `src/README.md` 的目录描述【一手-源码 src/README.md:「The XNU Source Tree」节】）之外，全部为方法论陈述，不含需外部佐证的事实主张；覆盖范围表中的"一手口径可得性"列是对简报各节核查结论的归纳，不是 Apple 的分类。

---

# 方法与证据分级

## 2.1 六级来源体系

本报告的证据分级由核查简报的来源等级体系重述而来，共六级。分级名称在正文行内标注中原样使用，读者可据此判断每条陈述的引用场合上限。

| 等级 | 定义 | 典型载体 | 可用于何种表述 |
|---|---|---|---|
| **一手-Apple 文档** | Apple 官方对外发布的文档、支持页与安全指南 | `security.apple.com`（含工程博客）、`support.apple.com`、`developer.apple.com`、Apple 平台 PDF；其中《Apple Platform Security》本地核查取自 `help.apple.com` 官方 PDF（网页版为 JS 渲染，不便逐字取证），页码为该 PDF 的印刷页码 | 可写"Apple 官方说明/要求……"；引用时须保留版本落款（见 2.2） |
| **一手-XNU 源码** | Apple 以 APSL 2.0 开源的 XNU 内核树本体 | 上游 `https://github.com/apple-oss-distributions/xnu`（本地镜像 tree sha `f6217f8`，见 `src/tree.json`；各文件头为 APSL 声明，例 `src/vm_kern.c:14` 引用 `http://www.opensource.apple.com/apsl/`） | 可写"源码中……为实名存在的符号/注释"；不可写"Apple 承诺" |
| **一手-Apple 随源码发布的官方 doc 与 man page** | Apple 撰写并随源码开源的仓库内文档 | `xnu` 仓库 `doc/*.md`（如 `doc/ipc_security_concepts.md`、`doc/port_types.md`、`doc/guard_exceptions.md`）、`osfmk/man/*.html` | 与一手文档同级：属 Apple 撰写，可作官方口径引用，但要说明其载体是源码仓库而非开发者门户 |
| **手册页** | Darwin man pages | `opensource.apple.com/man/`（收录范围有限，且部分 mach 相关页实测 404，见 2.5） | 仅作语义限定用途；缺失时须转为缺口表述 |
| **公开研究（二级）** | 会议论文、厂商博客、公开缺陷档案 | Project Zero 缺陷档案、Black Hat 白皮书、POC 会议论文、安全媒体分析文 | 只能写"公开研究指出……"，禁止升格为"Apple 承认/官方口径" |
| **分析推断** | 由前述事实组合得出、无单一来源直接陈述的结论 | 报告自身的结构性判断 | 必须显式标注；用于说明机制边界，不冒充事实 |

**"man page"这一层要分成两种载体来用。** Apple 随源码发布的官方 man page 实际存在于 XNU 仓库内（`osfmk/man/*.html`），其层级与仓库 doc 相同；例如 `osfmk/man/task_get_exception_ports.html` 的首句原文就是 "**Function - Return send rights to the target task's exception ports.**"，参数 `task` 被标注为 "**[in task send right]**"【一手-官方doc osfmk/man/task_get_exception_ports.html；交叉引用：简报 §B11】——这是一条把"异常端口以 send right 形式返回/设置"写成官方口径的最佳证据。另一类是 `opensource.apple.com/man/` 的 Darwin 手册页，其收录范围有限，且本章第 4.6 节会记录：与 Mach IPC 直接相关的 `mach_msg(2)` 一类页面**实测 404**。两者不能混称"Apple 手册页"：前者可作一手，后者是缺失项。

三级易混淆之处需要额外说明。**"一手-Apple 文档"与"一手-源码"的差别不在新旧而在承诺性**：文档表达 Apple 对外的行为承诺，源码只表达当前实现。而"随源码发布的官方 doc"处在中间偏上位置——它由 Apple 撰写、以 Markdown 形式随仓库开源（例如 `doc/ipc_security_concepts.md` 明确讨论 `is_policy` 与 dPAC 加固【一手-官方doc doc/ipc_security_concepts.md:10-37】），因此可以当作 Apple 的自述引用，但引用场合应注明"该表述见于 XNU 仓库内文档"，避免读者误以为它出现在安全指南里。

## 2.2 引用规则

六级体系本身不保证一致性，因此本篇固定六条引用规则。

**规则一：同一论断取最高层级。** 若一个说法同时有一手与二级来源，正文以最高层级为准引用，二级来源最多作为"该结论在研究界亦被复述"的旁证。例如"PPL 不提供于 macOS"有《Apple Platform Security》原文【一手-文档 指南p.59-60】，就不得写成"研究者观察到 macOS 缺少 PPL"。

**规则二：混合来源必须拆开表述。** 当一个概念的"存在性"来自源码、"含义解释"来自三方资料时，两段必须分开陈述，各自标注。违反此规则最常见的后果是把三方措辞挂名 Apple——第 3.4 节的误解校正正是这类案例。

**规则三：源码结论必须注明版本锚定。** 本篇所有 `路径:行号` 结论均以本地镜像（tree sha `f6217f8`，对应上游 `main` 分支某 commit）为准；**历史版本的行号不同**，且本次核查未把源码结论逐条锚定到 `xnu-12xxx` 发布标签【一手-源码 src/tree.json；锚定缺口见简报 D 节】。因此正文引用行号时统一采用"以该镜像/上游 main 为准"的口径，不把行号当作跨版本稳定的定位。

**规则四：《Apple Platform Security》须保留落款。** 引用该书（本地文本记作 `apsg`）时保留 **August 2026** 版落款与印刷页码，形如【一手-文档 指南p.114（Apple Platform Security, August 2026）】。该指南逐年改版，去掉落款的页码引用在他人复核时即失效。

**规则五：二级来源不得升格。** 高暴露驱动一类结论（例如 IOSurface 相关的 use-after-free 缺陷档案与会议论文）全部来自公开研究【公开研究 Project Zero 缺陷档案 42452365；公开研究 Lane/Black Hat EU-17；公开研究 Liang/POC2019】，写作时必须保留"公开研究指出"的措辞，不得表述为 Apple 官方承认的攻击面。

**规则六：术语口径冲突时并列两侧再收口。** 同一事实在不同层级来源间措辞不一致时，本篇不擅自选边，而是先并列两侧、再给出统一的收口写法。系列核查记录中的两例可作为范式：

| 冲突项 | 一侧口径 | 另一侧口径 | 收口写法 |
|---|---|---|---|
| 内核地址随机化的名称 | 源码/研究界称 `KASLR`（`kern_sysctl.c:4313` 原文 "Boolean indicating if KASLR is active."，随机量正式名 `vm_kernel_slide`）【一手-源码】 | Apple 面向用户文档只用 "Address Space Layout Randomization (ASLR)" 且语境为进程级，全书未单独为内核地址随机化命名【一手-文档 指南】 | "源码与研究界称 KASLR；Apple 官方文档只称 ASLR，二者不可并列当作 Apple 官方术语" |
| entitlement 真名前缀 | 源码常量为 `get-task-allow`（无域名前缀，对应旗标 `CS_GET_TASK_ALLOW 0x00000004`，`cs_blobs.h:38`）【一手-源码】 | 社区惯写 `com.apple.security.get-task-allow` | "以源码常量为准写 `get-task-allow`；带 `com.apple.security.` 前缀的全名未在任何一手来源确证" |

（上表两行取自本系列术语冲突核查记录，供后段章节沿用同一收口口径；本篇第 3–5 章涉及的层级冲突主要出现在 capability 措辞与 `mach_msg(2)` 手册页两处，分别在第 4.2 与 4.6 节就地处理。）

## 2.3 源码可观察性的锚定与三个已知失真源

源码是本报告最容易核实也最容易误用的一级。误用主要来自三个失真源，写作与阅读时都要带在心上。

**失真源一：本地镜像是扁平副本，目录层级不可反推。** 简报两处直接证实这一点：`config/` 下的平台清单在本地为"同名扁平副本"【简报 B1】，而 `amfi.h` 的仓库真实路径写作 `libkern/libkern/amfi/amfi.h`、本地即 `src/amfi.h` 且"与之逐行一致"【简报 B5】。也就是说，镜像里的 `zalloc.c`、`vm_kern.c`、`ipc_object.h` 等文件名不带目录信息，其子系统归属只能按 README 的目录职责描述（`osfmk` = "Mach kernel based subsystems"【一手-源码 src/README.md】）来表述，而不是拼出一条"完整仓库路径"去引用。简报中给出了完整仓库路径的文件（`osfmk/kern/cs_blobs.h`、`bsd/kern/ubc_subr.c`、`iokit/Kernel/IOUserClient.cpp`、`security/mac_policy.h`、`osfmk/kern/exception.c`、`bsd/uxkern/ux_exception.c`）本篇才按完整路径引用。

**失真源二：部分文件是节选，函数体可能不在其中。** 简报明确记录：`vm_kern.c`、`IOUserClient.cpp`、`ubc_subr.c` 等在本地镜像中为**节选文件**【简报 A 节核查方法记录⑤】。这带来一类特殊结论形态——"宏名/类型名已核实存在，但函数体需引上游完整文件"。本篇第 5.4 节的 `kmem_guard_t` 与 `kmem_entry_validate_guard` 正是此种情形：类型与字段名可在 `vm_kern.c:224-238`、`:507-518` 直接比对【一手-源码 src/vm_kern.c:224-238】，而 `kmem_allocate()` 的函数体不在节选范围内，需指向上游完整文件。这类缺口的性质是"本地节选所致，非事实缺失"，与"Apple 未公开"完全不同，必须在文字里区分开。

**失真源三：grep 无命中不等于概念不存在。** 简报把"在 `ipc_port.h`/`ipc_object.h` 中检索 `msgsend`、`quota`、`max_urefs` 无命中"记录为**负面结果**，并给出倾向性判断（该命名更可能属 FreeBSD `rctl`/`mac_rlimit` 概念而非 XNU iOS 机制）【简报 B2、C.3】。本篇的处理方式是：把它写成"检索范围内不存在该命名 + 更可能的归属"，而不写成"XNU 没有资源限制机制"。同理，源码里没有某策略模块，也不等于设备上不存在该策略——第 2.4 节展开。

## 2.4 为什么"源码可观察"不等于"Apple 承诺"

这一节是整份报告的方法论支点。开源 XNU 让人可以直接读到机制的实现与注释，这带来一种很强的诱惑：把"我在源码里看到了这段逻辑"等同于"Apple 承诺该保护始终生效"。这两个命题之间存在三道独立的断裂，本节用简报中三个具体例子把它们逐一展示出来。

**断裂一：开关存在，取值未公开。** `BTI_ENFORCED` 是最干净的例证。arm64 内核里确实有 BTI 强制的实现路径——`arm_vm_init.c:1075-1082` 的 `#if BTI_ENFORCED … ptmp |= ARM_PTE_GP;`，以及 `ARM_PTE_GP` 被定义为 `0x0004000000000000ULL /* value marking a guarded page */`【一手-源码 osfmk/arm64/proc_reg.h:2307】。但关键在于：**`BTI_ENFORCED` 在这棵公开源码树中没有任何定义处，只有 `#if` 使用点**，因此"零售 iOS 构建是否打开 BTI 强制"无法从公开源码判定。安全口径只能写成"源码具备 BTI 强制能力，构建开关取值 Apple 未公开"【简报 B10、E.11】。把这段逻辑读成"iOS 强制 BTI"就是典型的层级越权。

**断裂二：框架开源，策略不在开源树里。** MACF 是另一个方向的例子。`security/` 目录中有 27 个 `mac_*.c` 与 `mac_policy.h`，注册入口 `mac_policy_register(struct mac_policy_conf*)`、`MAC_POLICY_SET()`、`MAC_POLICY_OPS_VERSION 91` 与 `mpo_<object>_check_<operation>` 命名约定全部可一手核实【一手-源码 mac_policy.h:6406/6565/5966/158-162】；但**开源树内一个策略模块实现都没有**——`bsd/security/` 现仅剩 `audit/`，无任何策略 `.c` 文件；Sandbox 与 AppleMobileFileIntegrity 这两个策略以闭源 kext/组件形式在运行时注册进来。于是出现一组必须并存的陈述：机制层面（IOKit 打开、VFS/exec 路径都过 MACF hook）可一手核实，而"iOS 上到底注册了哪几个策略模块"官方从未公布，只能标为【公开研究/二级】（Jonathan Levin《*OS Internals: Security & Insecurity》与 kernelcache 符号观察一类）。**框架可见性不能推导策略可见性。**

**断裂三：组件的官方定性与源码接口分居两处。** AMFI 的第三例更微妙。内核侧接口完全开源：`libkern/libkern/amfi/amfi.h` 定义函数表 `amfi_t`（`:245-265`）与 `amfi_interface_register()`（`:294`），含 `KERN_AMFI_INTERFACE_VERSION 7`（`:38`）；`libkern/amfi/amfi.c` 把实现注册进 `SECURITY_READ_ONLY_LATE(const amfi_t*) amfi` 全局，重复注册直接 `panic("AppleMobileFileIntegrity interface already set")`。而 Apple 对外把它定性为内核扩展："The research. namespace is respected only by the RESEARCH variant of the **AppleMobileFileIntegrity kernel extension**"【一手-文档 指南p.85】。**接口与定性都可核实，唯独 kext 本体的实现不可核实**——它不在公开源码里，简报因此要求"AMFI kext 本体、Sandbox.kext 源码、board_config 设备清单、CoreEntitlements 用户态库的相关内部实现细节一律不得写实"。

三道断裂合并成一条纪律，以及它的镜像推论：

| 情形 | 可以写 | 不可以写 |
|---|---|---|
| 源码有实现、开关取值未公开 | "源码具备 X 能力，取值 Apple 未公开" | "iOS 已强制 X" |
| 框架开源、策略闭源 | "hook 位置与注册机制一手可证；启用清单为研究观察" | "Apple 官方称 iOS 启用了 Y 策略" |
| 接口开源、实现体闭源 | "接口契约与注册方是内核路径；实现细节无公开口径" | 描述闭源内部的判定流程 |
| 源码里没有 | "开源树内未见；不在公开源码范围" | "该机制不存在" |
| 官方文档没写 | "本次核查未获得可核实条目（审计缺口）" | 用三方措辞冒充官方结论 |

镜像推论同样重要：**源码里看不到某机制，绝不能推出设备上不存在**。AMFI kext 与 Sandbox 策略模块都是"运行时存在但源码不可观察"的对象；`board_config.h` 属 Apple 内部目标文件，因此"哪台设备用哪种代码签名监视器"这类设备级映射也只能写"未公开"【简报 B6】。

## 2.5 核查方法与负面结果的用法

本篇的事实层不是"检索到资料然后复述"，而是**逐条比对一手文件**。简报记录的五项方法值得写入报告，因为读者需要知道每条结论是怎么来的、以及哪些结论的缺席同样是核查产出。

| 方法 | 具体做法 | 对写作的影响 |
|---|---|---|
| 全量 trees 交叉验证 | 本地 `src/` 镜像与 GitHub API 的 recursive trees（未截断）交叉验证，文件内容以 `raw.githubusercontent.com` 为准 | 目录归属、文件是否存在这类判断可作一手陈述 |
| 宏名/函数名/注释原文逐条 grep | 所有形如 `ZSECURITY_CONFIG(...)`、`IE_BITS_PINNED_SEND` 的名称都直接 grep 一手文件并记录行号 | 第 5.3 节能"逐个列出真实开关名"，正因未列出的名字可能就是编造的 |
| 官方 PDF 取一手引文 | Apple support 网页版为 JS 渲染，一手引文改自 `help.apple.com` 的《Apple Platform Security》PDF（`pdftotext` 转换，页码为该 PDF 印刷页码） | 页码引用必须带 August 2026 落款，且不与网页版段落编号混用 |
| 404 与无命中作为记录项 | `opensource.apple.com/man` 的相关 mach 手册页 URL 实测 404（负面结果，已入缺口清单） | 手册页缺失被写成审计缺口，而不是改用三方解释填充 |
| 节选文件标注 | 镜像内 `vm_kern.c`/`IOUserClient.cpp`/`ubc_subr.c` 等为节选，涉及处一律标"本地节选所致，需引上游完整文件" | 出现"类型名已核实、函数体待补"的中间结论形态（见 2.3） |

**负面结果的正确用法。** 简报的缺口清单列了十余条"本次未获得可核实条目"，其中与本篇直接相关的三类：`mach_msg(2)` 及 port 分配类 Darwin 手册页（对应 URL 均 404）、Apple 对 `mach_msg` 传输语义的现行公开文档、Mach IPC port 名额与 msgsend 资源限额的官方数值。这些缺席**只降低我们对该主题的可陈述强度，不改变该主题在内核中存在的事实**。因此第 4.6 节的写法是"该主题缺一手口径 + 可替代的一手表述来自何处"，而不是"该主题不存在"。

**倾向性判断与结论的分离。** 对 `msgsend` 一词，简报给出的是"检索范围内不存在该命名 → 更可能是 FreeBSD `rctl`/`mac_rlimit` 概念"，并标注【未核实，倾向不存在】、要求"正文不得引用，除非另得一手"。本篇遵循同一处理：在需要澄清读者可能持有的错误概念时，可以说明"该命名更可能源自另一内核谱系"，但不会把它写成"XNU 没有消息资源限制"——后者是需要一手证明的否定性论断，而我们的证据只到"未获得口径"为止。

同一纪律也适用于计数类事实。简报记录：`ipc_entry` 的 `IE_BITS_UREFS()` 确实出现在 `doc/ipc_security_concepts.md:92`（pinned send 要求 `urefs >= 1`），足以说明内核内部存在用户级引用计数这一**机制**【一手-官方doc doc/ipc_security_concepts.md:92】；但"上限是多少"在 Apple 面向开发者/安全文档中不存在公开描述【简报 B2、C.10】。机制存在性与数值可陈述性是两件事，报告必须分开处理。

## 2.6 分级体系的适用示例与本章口径边界

把规则落到具体句子上，是检验分级是否真在起作用的最快方式。下表左列是该主题下**最常见**的表述形态（多来自三方教材与二手转述），中列是按本篇规则改写后的可发表口径，右列标注改写依据来自哪一级。

| 常见表述 | 本篇采用的口径 | 依据层级 |
|---|---|---|
| "Apple 官方文档称 port right 就是一种 capability" | "Apple 开源源码注释与仓库内官方 doc 中直接把 port name 称为 capability；Apple 开发者/支持文档层面查无此表述" | 【一手-源码 ipc_object.h:541】【一手-官方doc doc/ipc_security_concepts.md:49】；缺口项来自简报 C.2 |
| "XNU 是对 Mach 的大幅改动" | "README 原文只称 XNU 为 combining CMU Mach、FreeBSD 组件与 C++ IOKit 的 hybrid kernel；'heavily modified' 是三方措辞；改动幅度只能作为源码观察的分析结论" | 一手-源码 `src/README.md:4`；反面索引 Wikipedia「XNU」条 |
| "内核分配器有 per-page guard 保护" | "真实存在的表述是 zone chunk 尾 guard page 与 `ZSECURITY_CONFIG(PGZ_OOB_ADJUST)`；不存在名为 'per-page guard' 的公开宏" | 一手-源码 `zalloc.c:194`、`:233`；简报 B3、C.4 |
| "iOS 上启用的 MAC 策略模块是 Sandbox 与 AMFI" | "开源树内零个策略模块实现；该清单来自公开研究与 kernelcache 符号观察，Apple 从未公布官方清单" | 一手-源码（框架侧）+【公开研究/二级】（清单侧） |
| "每个进程有 N 个 Mach port 名额" | "port 名额与限额无任何 Apple 官方公开文档记载；任何具体数字都必须标注'未公开，来自源码观察'" | 简报 B2（负面结果）、D、E.9 |

**本章口径边界**：第 2 章所述六级体系、六条引用规则与五项核查方法，均重述自本报告的核查简报（A 节来源等级、方法记录①–⑤、C 节推翻项、D 节缺口、E 节限定语、F 节术语冲突）；其中用于论证"源码可观察 ≠ Apple 承诺"的三个例子（`BTI_ENFORCED` 无定义处、MACF 策略模块不在开源树、AMFI kext 本体闭源）分别取自简报 B10、B4、B5 三节，其完整展开由本报告后段对应章节承担，本章只取其"证据形态"作为方法论样本。本章不引入任何简报之外的事实。

---

# XNU 分层与职责边界

## 3.1 Apple 自述的 hybrid kernel：三要素的原文口径

"XNU 是混合内核"这句话被引用得太随意，以至于很少有人去核对 Apple 究竟说了什么。现行 README 的定性原文只有一句，值得整段照录：

> "XNU is a hybrid kernel combining the Mach kernel developed at Carnegie Mellon University with components from FreeBSD and a C++ API for writing drivers called IOKit."
> 【一手-源码 src/README.md:4】

这句话给出三个要素，且**三个要素都是 Apple 自述**，不需要借助任何三方材料：

| 要素 | Apple 自述措辞 | 在 XNU 树中的落点 | 该要素承担的安全职责 |
|---|---|---|---|
| CMU Mach | "the Mach kernel developed at Carnegie Mellon University" | `osfmk/`（Mach 核心子系统） | 任务/线程对象、VM 与 pmap、**Mach IPC 与 port right**、异常分派、内核内存分配 |
| FreeBSD 组件 | "components from FreeBSD" | `bsd/`（POSIX/BSD 子系统） | 进程与 exec 路径、VFS、网络与 socket、`security/` 所依托的 MAC 框架调用点 |
| IOKit | "a C++ API for writing drivers called IOKit" | `iokit/`、`libkern/` | C++ 驱动与 kext 生命周期、IORegistry、**IOUserClient 用户态入口** |

三要素中，Mach 与 FreeBSD 有明确的外部谱系，而 **IOKit 在 README 的措辞里是 Apple 自述的"用于写驱动的 C++ API"**，未被描述为对某第三方内核的继承。这一点对机制分析有实际意义：IOKit 的用户客户端边界（本报告后段专门展开）不能沿用 Mach 或 BSD 的信任假设来推断，它有自己的对象模型与授权通道。

许可与位置同样是一手可核事实：仓库为 `apple-oss-distributions/xnu`（`src/tree.json` 的 url 字段），LICENSE 为 APSL 2.0（各文件的 `OSREFERENCE_LICENSE` 头，例 `src/vm_kern.c:14` 指向 `http://www.opensource.apple.com/apsl/`），仓库根另有 `APPLE_LICENSE` 文件（`src/tree.json` tree 条目）【一手-源码 src/tree.json；src/vm_kern.c:14】。这三项决定了"源码可引用、可 grep"的证据资格，也是第 2 章把源码列为一手的依据。

**四问归位**：Mach/BSD/IOKit 三要素**负责**的是把不同来源的安全语义放进可辨识的代码边界；**强制点**分散在三者各自的子系统（详见 3.2）；**生效前提**是相应构建配置确实把该子系统纳入编译（详见 3.3）；**不覆盖**的是三要素之外的 Apple 自有组件——AMFI、CSM、SPTM/TXM 一类都不在这句话的三个要素里，把它们归入"Mach 的职责"或"BSD 的职责"都属于误读。

## 3.2 目录即职责：osfmk、bsd、libkern/security、pexpert

README 的「The XNU Source Tree」节不是导览手册式的介绍，它逐目录给出了 Apple 自己对该目录职责的一句话界定。本节把这份界定原样整理成表，并在最后一列补上"该目录在本篇里对应的强制点"——补充列属【分析推断】，前三列是一手。

| 目录 | README 的职责描述（一手-源码 src/README.md） | 承担的内核对象/语义 | 安全机制的强制点落在此处 |
|---|---|---|---|
| `osfmk/` | "Mach kernel based subsystems" | task、thread、ipc、vm、pmap、调度 | port right 的持有与处置（`ipc_object.h`/`ipc_port.h`）、异常三级分派（`exception.c`）、内核内存分配（`zalloc.c`）、页级签名校验接口（`pmap_cs.c/h`） |
| `bsd/` | "BSD subsystems code" | 进程模型、文件、网络、socket；POSIX 语义 | exec 路径上的签名与 entitlement 提取（`bsd/kern/ubc_subr.c` 的 `csb_*` 字段）、VFS/文件/进程的系统调用入口、Mach 异常→UNIX 信号的翻译（`bsd/uxkern/ux_exception.c`） |
| `security/` | "Mandatory Access Check policy interfaces" | MACF 框架与各调用点封装 | 每个受控操作前的策略回调点（`mpo_<object>_check_<operation>`），含 `security/mac_iokit.c` 对 `IOServiceOpen`/IORegistry 属性读写的 hook |
| `libkern/` | "C++ IOKit library code for handling of drivers and kexts" | 驱动/kext 的 C++ 运行时与内核侧库接口 | AMFI 的内核侧函数表接口（`libkern/libkern/amfi/amfi.h` 的 `amfi_t` 与 `amfi_interface_register()`）即在此处 |
| `pexpert/` | 平台代码（简报据 README 归纳为中断/原子操作一类平台原语；此处未照录原文） | 启动/中断/原子操作的平台适配 | 平台差异的收口位置；本身不承担策略判定（见下方"不覆盖"） |

这张表最需要强调的一条是 **`security/` 的位置**：README 把 MACF 定性为"强制访问检查的**策略接口**"，而不是"访问控制系统"。这与简报核查结果一致——`security/` 目录里的 27 个 `mac_*.c` 是**调用点封装**，`mac_policy.h` 保留 FreeBSD CVS 标签 `FreeBSD: src/sys/sys/mac_policy.h,v 1.39 2003` 与 "TrustedBSD MAC Framwork on Darwin" 自述【一手-源码 mac_policy.h:65,73】，而真正的策略实现（Sandbox、AppleMobileFileIntegrity）不在开源树内。因此"`security/` 目录决定访问控制结果"是错的；正确表述是：**决策发生在被注册进来的策略模块内，`security/` 只决定何时向其提问**。

另一条容易混淆的是 `bsd/` 与 `security/` 的次序。`mac_vfs.c`/`mac_file.c`/`mac_process.c` 等封装在 DAC（属主/权限位一类传统检查）之后调用策略，调用点位于 VFS、exec、IOKit 路径上——这是源码结构可以直接证明的【一手-源码 security/ 目录结构，见简报 B4】。但"沙箱容器即 MACF 策略"这一表述在 Apple 官方文档中并不存在，只有源码旁证（例如上游 `bsd/kern/ubc_subr.c:4833` 使用 `MAC_VNODE_CHECK_DYLD_SIM` 标志，说明 dyld shared cache 的模拟检查经 MACF 进行）【简报 B4，须标"源码观察"】。

各目录的**生效前提**并不相同：`osfmk/` 与 `bsd/` 是内核主干，任何平台构建都会纳入；`security/` 的调用点封装按 `optional config_macf` 编译开关纳入（`security/conf/files`）【一手-源码 security/conf/files】，即"MACF 可按构建裁剪"是源码事实；`libkern/` 的 AMFI 接口只提供函数表契约，其实现在注册方（kext 本体闭源）。**不覆盖**的一面同样要写清：`pexpert/` 只做平台适配、不判定策略；`osfmk/` 不认识 entitlement 字符串；`bsd/` 不维护代码签名监视器的页状态。

关于"谁拥有代码签名强制"这类跨目录问题，简报给出的写作建议被本篇直接采纳：**它跨三层（bsd exec → security/MACF → AMFI 接口 → pmap/CSM），不得写成单层独占**【简报 B1 职责边界写法建议】。完整链路见 3.5 节。

## 3.3 同一棵树，多个内核：构建配置与功能开关

分层讨论最容易滑向的一个误区是"iOS 内核 = 一套和 macOS 不同的代码"。源码事实相反：**同一棵 XNU 树经不同构建配置分别产出 macOS 与 iOS 内核**，而安全特性的取舍主要靠功能开关，不靠平台宏。

构建配置这一层可以一手核实。仓库 `config/` 下存在 `MASTER`、`MASTER.arm64`、`MASTER.arm64.iPhoneOS` 等文件，GitHub 全量 trees API 另确认同族的 `MASTER.arm64.BridgeOS`、`MASTER.arm64.WatchOS` 等；本地镜像中 iPhoneOS 清单的版权年份为 2001–2024【一手-源码 config/MASTER.arm64.iPhoneOS；简报 B1】。这些清单决定了哪个子系统、哪个驱动进入某个平台内核，因此"该机制在 iOS 上是否被编译进来"的第一手答案就在这里，而不是在文件名或目录名里。

第二层更关键：README 自述 XNU **按功能开关而非平台宏编译**。原文口径包括"`SECURE_KERNEL` 一类开关"与一句直接说明——"xnu does not define the platform macros from TargetConditionals.h (`TARGET_OS_IOS`…)"【一手-源码 src/README.md:470-480】。README 还对 `SECURE_KERNEL` 给出了使用指引：「use SECURE_KERNEL if your code implements new functionality exclusively relevant to the trusted kernel」。

| 判定问题 | 错误问法 | 正确问法 | 可核实位置 |
|---|---|---|---|
| 某子系统是否在此内核里 | "iOS 有没有 X 子系统？" | "该平台的 `config/MASTER.arm64.iPhoneOS` 清单是否纳入它？" | 一手（config 文件存在，逐条内容需按镜像核对） |
| 某加固是否生效 | "iOS 默认开着 X 吧？" | "该功能开关在此构建里取值为何？" | 部分不可核实（开关取值常未公开，见 2.4） |
| 某机制是否 iOS 专属 | "`#if TARGET_OS_IOS` 里有它吗？" | "该开关在哪些平台清单中出现？" | 一手（但 XNU 不定义 TargetConditionals 平台宏，源码里本就查不到那种写法） |

由此得出本篇后续所有机制小节都要复用的三条推论。**其一**，"iOS 与 macOS 的差异"应当被表述为**同一份代码在不同清单/开关下的形态差异**，而不是两套实现的分叉——这也意味着任何"iOS 有 X 所以 macOS 没有 X"的论断都必须另有官方文档或设备侧证据（PPL 与 SPTM/TXM 的平台覆盖差异即属此类，见 3.6 与后段代码签名章节，须逐条定平台）。**其二**，`SECURE_KERNEL` 这类开关的存在提醒我们：内核中"专供可信内核的新功能"是一条被 README 显式承认的编译维度，因此"某代码路径在开源树里可见"与"它进入消费者设备内核"是两个命题。**其三**，设备级映射（哪台机器用哪种代码签名监视器）取决于 `board_config.h`，而该文件属 Apple 内部目标文件、不在开源 config 中全量可得，故简报把设备清单列为未核实【简报 B6】。

**强制点位置**：构建层面的强制点其实落在"编译期"，是内核镜像形成之时而非运行之时；这与第 4、5 章里运行期强制点（IPC 策略查询、zone 分配检查）构成两个不同时间尺度的前提。**本篇不覆盖**的是各 config 的完整符号清单——逐项枚举超出简报核查范围，凡未点名的开关均不得推定其存在。

## 3.4 误解校正：分层归属上的三类常见误述

误解校正一：「Apple 官方说 XNU 大幅改动了 Mach」。

| 项 | 内容 |
|---|---|
| **常见说法** | "XNU 是 Apple 对 Mach 3.0 microkernel 的**大幅改动**版本"，常被挂名为 Apple 官方表述，并进一步推论"因此 Mach 层的信任模型不可照搬" |
| **正确口径** | 现行 README 原文只有 "hybrid kernel **combining** the Mach kernel developed at Carnegie Mellon University with components from FreeBSD and a C++ API for writing drivers called IOKit"【一手-源码 src/README.md:4】，**全文没有出现 "extensively/heavily modified" 一类措辞**（本地全文 grep 无该命中）。Apple 对外文档层面确有"XNU 基于 Mach 3.0 microkernel"的历史表述（早期 opensource.apple.com/xnu 页面），但**"改动幅度"没有任何可点名的官方原文** |
| **依据层级** | "combining CMU Mach + FreeBSD 组件 + IOKit"＝一手-源码；"大幅改动"＝**Wikipedia 等三方条目的措辞**，简报已把它列入反面索引，明令不得当作 Apple 官方引用【简报 C.1、F 节反面索引】 |
| **可保留的部分** | 改动幅度并非完全不可谈：README 自述 XNU 与 CMU Mach 已分叉、且按功能开关而非平台宏编译【一手-源码 src/README.md:360 附近；:470-480】。这些是源码可观察事实，据此写出"XNU 在 Mach 基础上做了大量 Apple 自有改动"只能作为**【分析推断】**，并说明依据是目录结构、开关体系与注释，而不是 Apple 的原话 |

这个校正的实际影响不只是引文规范。"大幅改动 Mach"这类表述会让读者以为 Mach 层的既有语义（port right、IPC 策略、异常分派）都已不可预测，从而放弃去读 `osfmk` 的 IPC 部分与仓库 `doc/` 里那些一手材料；而第 4 章会显示，恰恰相反——**Mach IPC 的现代语义在 Apple 自己随源码发布的官方文档里写得相当具体**（`doc/ipc_security_concepts.md`、`doc/port_types.md`），完全可以按一手引用。换言之，把"改动幅度"当成官方结论，会让读者错过真正可核实的部分。

误解校正二（与分层相关、须在此处点明的一条）：「BSD 层负责安全策略」。README 给 `bsd/` 的描述是 "BSD subsystems code"，给 `security/` 的描述才是 "Mandatory Access Check policy interfaces"。二者都在树上，但**策略接口不属于 BSD 层**；把 MACF 说成"BSD 的安全扩展"虽在血统上有一点道理（`mac_policy.h:65` 保留 `FreeBSD: src/sys/sys/mac_policy.h,v 1.39 2003` 标签、`:73` 自述 "TrustedBSD MAC Framwork on Darwin"），在目录职责上却不成立——简报特别指出 MACF **不是独立开源项目，仅存在于 XNU 树内**，`bsd/security/` 现仅剩 `audit/`【一手-源码 mac_policy.h:65,73；简报 B4、C.9】。

误解校正三：「AMFI 是一个用户态守护进程」。这与 3.2 的目录归属直接相关，故在此登记，详细展开见后段 AMFI 章。单一化表述被推翻：Apple 官方口径为 **AppleMobileFileIntegrity kernel extension**（内核态）【一手-文档 指南p.85】；用户态 `amfid`（`/usr/libexec/amfid`，LaunchDaemon `com.apple.amfid`）确实存在，但**其职责无 Apple 官方文档**，只能标【公开研究/二级】。可安全使用的写法是"AMFI 主体是内核内组件（kext），另有同名用户态守护进程参与部分策略评估"，内核侧函数表接口则是一手（`libkern/libkern/amfi/amfi.h` 的 `amfi_t` 与 `amfi_interface_register()`）【简报 B5、G.5】。

## 3.5 跨层协作实例：一次 exec 中的代码签名强制

分层表读到最后必须回答一个问题：一个真实的安全决定是如何跨层完成的。本节以"启动一个可执行文件"为例把链条摊开，因为它同时涉及 3.2 表中的四个目录，是"单层独占"式描述最常见的受害场景。

**链条第一段：BSD 层解析 Mach-O 与签名内嵌 blob。** exec/mmap 时由内核从签名内嵌 blob 提取字段，落点在 `bsd/kern/ubc_subr.c` 的 `csb_*` 字段（如 `csb_platform_binary`、`csb_teamid`，`ubc_subr.c:3160` 等）【一手-源码 bsd/kern/ubc_subr.c】。这一段的职责是**把二进制里写了什么读出来**，不含"允不允许"的判断。注意本地镜像中该文件为节选，逐行了解读需引上游完整文件【简报 A⑤】。

**第二段：Mach 层把旗标写进代码签名状态。** `osfmk/kern/cs_blobs.h` 定义 `CS_HARD 0x100 "don't load invalid pages"`、`CS_KILL 0x200`、`CS_GET_TASK_ALLOW 0x4`、`CS_REQUIRE_LV 0x2000 "require library validation"`、`CS_RUNTIME 0x10000 "Apply hardened runtime policies"`、`CS_KILLED 0x1000000 "was killed by kernel for invalidity"`【一手-源码 osfmk/kern/cs_blobs.h】。这些是 **Mach-O 通用编码，不区分平台**；平台差异体现在"谁强制、默认策略是什么"（简报 E.7）。

**第三段：security/ 框架向已注册策略提问。** 调用点命名遵循 `mpo_<object>_check_<operation>`（如 `mpo_vnode_check_exec`），未注册该 op 的策略在该入口被直接跳过【一手-源码 mac_policy.h:158-162、:73-77】。同一目录里的 `mac_iokit.c` 把 IOKit 打开与 IORegistry 属性读写也纳入 hook（`mpo_iokit_check_open_t` 等，`mac_policy.h:1099-1149`）。框架只决定"何时提问、如何聚合答复"，**决定内容由运行时注册的闭源策略给出**。

**第四段：AMFI 内核接口给出判定。** 内核通过只读函数表调用 AMFI 实现：`libkern/libkern/amfi/amfi.h` 定义 `amfi_t`（`:245-265`）与 `amfi_interface_register()`（`:294`），`libkern/amfi/amfi.c` 把实现注册进 `SECURITY_READ_ONLY_LATE(const amfi_t*) amfi`，重复注册即 `panic("AppleMobileFileIntegrity interface already set")`；entitlement 经 CoreEntitlements V2 内核 API 消费（`src/amfi.h:37-38` 的 include、`:245-253` 的 `OSEntitlements_query`）【一手-源码 amfi.h/amfi.c】。Apple 官方的定性同向："The research. namespace is respected only by the RESEARCH variant of the AppleMobileFileIntegrity kernel extension; any process with this entitlement is terminated on a customer device during signature validation."【一手-文档 指南p.85（Apple Platform Security, August 2026）】——**"在签名校验时被终止"这一效果是官方口径，函数表内部如何判定则无公开实现**。

**第五段：pmap/CSM 在运行期逐页把关。** `code_signing.h` 定义 `code_signing_monitor_type_t` 为 `CS_MONITOR_TYPE_NONE/PPL/TXM`（`:35-37`），`kern_codesigning.c:111` 的 `cs_monitor` 变量与 `:115` 的只读 sysctl `security.codesigning.monitor` 提供观测口；`pmap_cs.h` 的注释 "Check if the PPL based code signing is enabled"（`:42`）与 `PMAP_CS_PPL_MONITOR`（`:71-75`）说明监视器由 PPL 或 TXM 承载【一手-源码 code_signing.h/kern_codesigning.c/pmap_cs.h】。官方文档与之互证：iOS 等平台的表述是 "After the kernel has started, it controls which user processes and apps can be run… require that all executable code be signed using an Apple-issued certificate"（p.114）与 "At runtime, code signature checks of all executable memory pages are checked as they're loaded"（p.115）【一手-文档 指南p.114-115】。**exec 时整体验证 + 运行期逐页验证**的双层描述因此成立。

把五段合起来看，"谁负责代码签名强制"的正确答案是**分工**而非单点：

| 段 | 层 | 负责什么 | 它不负责什么 |
|---|---|---|---|
| 1 | `bsd/kern/ubc_subr.c` | 读出签名 blob 与 entitlement 原始字段 | 不判定是否放行 |
| 2 | Mach 层旗标（`cs_blobs.h`） | 承载策略位与"已被内核杀死"状态 | 位本身不含平台政策 |
| 3 | `security/mac_*.c` | 在受控操作点询问策略、聚合结果 | 不含任何策略实现体 |
| 4 | AMFI 内核接口 | 提供判定能力的内核侧契约与 entitlement 查询通道 | 实现体闭源，细节不可写实 |
| 5 | pmap/CSM | 页映射时的签名一致性把关 | macOS 无 PPL 语境下逐页验证由普通内核路径完成（非 CSM，简报 B6 限定） |

**链条级口径**：第 4、5 段各自展开属于后段章节；本节要固定的是**结构结论**——任何"某层已强制代码签名"的单层表述都会漏掉至少一段，进而把"生效前提"写错。

链条的**生效前提**必须在分层口径里说清，否则读者会把"链条存在"读成"链条处处等强"。两点一手限定值得在此登记，其完整展开属后段 AMFI 与代码签名章节。其一，内核维护一份运行时代码签名配置位图 `CS_CONFIG_*`【一手-源码 code_signing.h:42-53】，而 AMFI 的 boot-args 旁路（`amfi_get_out_of_my_way`、`cs_enforcement_disable`、`amfi_allow_any_signature` 等）附有源码注释原文 "We only ever need to parse for boot-args based exemption state on DEVELOPMENT"【一手-源码 kern_codesigning.c:219】——**这些旁路仅 DEVELOPMENT 内核生效，生产 iOS 内核不可用**，生产设备上相关位默认全 0【简报 B5、E.1】。这是"链条第 4 段的强度取决于内核形态"的直接证据。其二，链条第 5 段的监视器形态由编译期决定：`pmap_cs.h:70-75` 的三分支（`CONFIG_SPTM` → `PMAP_CS_PPL_MONITOR 0`；`XNU_MONITOR` → `1`；否则 `0`）配合注释 "XNU_MONITOR and PMAP_CS are both defined for the same targets in board_config.h."【一手-源码 pmap_cs.h:70-88】——**哪台设备用哪种监视器取决于不公开的 `board_config.h`**，故设备级映射只能写"未公开"。

分层本身还会影响"这句话该挂在哪个平台"。链条里可引的 Apple 面向用户的整体定性包括 "Code signing is still present and enforced by the kernel"（见 `§SSV and code signing`）与 "At runtime, code signature checks of all executable memory pages are checked as they're loaded"（p.115），而 PPL 的平台边界由指南另文给出（p.59-60）；macOS 在无 PPL 的语境下逐页验证由普通内核路径完成，**不属 CSM**【简报 B6，macOS 侧细节标部分核实】。因此本篇把"内核强制代码签名"始终写成**跨层协作的结果**，拒绝把它写成任何单层的独立能力。

最后一条属于本章的结构结论而非事实：链条的第 1、3、4 段各自都有"实现体不在开源树内"的部分（`ubc_subr.c` 为节选、MACF 策略模块闭源、AMFI kext 闭源），因此**这条链条的可审计程度随段落急剧下降**——第 2、5 段的符号与命名几乎全部可一手核对，第 4 段只能核到接口契约。防御工作据此排优先级时，应把"接口契约可核、实现不可核"的段落当作需要另行取证的对象，而不是当作已验证的结论。

防御视角下，这条链条还有一个常被忽略的推论：**链条越长，"单层已加固"式结论越不可靠**。举两个具体形态。其一，第 3 段（MACF）的调用点在框架里可以一手定位，但策略实现闭源——所以"该操作已过 MACF"并不等于"该操作已被限制"，因为未订阅该 hook 的策略会在该入口被直接跳过（`mac_policy.h:73-77` 的跳过逻辑是官方设计声明）。其二，第 4 段（AMFI 接口）经只读函数表被调用，函数表本身受 `SECURITY_READ_ONLY_LATE` 保护、且重复注册直接 `panic`【一手-源码 amfi.h/amfi.c】——这两条一手事实说明内核确实在保护该接口的完整性，但它们保护的是**接口契约**，不是判定逻辑的正确性。

分层级结论因此可以收束成一句可操作的话：**任何"iOS 内核在某处强制某事"的表述，都必须同时给出它落在链条的哪一段、该段实现何处可核、以及该段的生效前提**；三项缺一，该表述在下一次改版或换平台时就会失效。

## 3.6 iOS 与 macOS：同树不同形态带来的推断负担

同一棵树产出多个平台内核这件事，给分析带来一种特殊负担：**任何平台差异都必须重新走一遍证据链**，否则很容易把源码里看到的共同代码当成"两边行为一致"，或反向把官方文档里的一句平台限定当成"两边必然不同"。本篇的处理办法是把差异按证据强度分三档。

**第一档：官方文档明文的平台差异。** 这类可以直接陈述。PPL 是最清楚的例子："Page Protection Layer (PPL) in iOS, iPadOS, visionOS, and watchOS is designed to prevent user space code from being modified after code signature verification is complete… This protection **isn't offered in macOS** because PPL is only applicable on systems where all executed code needs to be signed."【一手-文档 指南p.59-60】；同一指南的 SPTM/TXM 章节则把 iOS/iPadOS/macOS/visionOS 都列入，并写明 "In the A15 or later and M2 or later SOCs, SPTM (in combination with TXM) replaces the PPL"【一手-文档 指南p.59-60】。简报据此特别提示：**「PPL 只存在于 iOS 系」有官方原文，但「CSM 概念仅 iOS」不成立**——macOS 在 A15/M2 之后同样有 SPTM/TXM。

library validation 与 hardened runtime 的对照也属第一档，且方向与常识相反：前者在 iOS 章有明文（"the system performs a code signature validation of all the dynamic libraries that a process links against at launch time… A program may link against any platform library that ships with the system or any library with the same team identifier in its code signature as the main executable."，p.114-115），后者被 Apple 开发者文档自限为 "Protect the runtime integrity of your **macOS app**"【一手-文档 developer.apple.com「Configuring the hardened runtime」】。iOS 上没有"开启 hardened runtime"这一选项，其等效性由"强制签名 + PPL/SPTM + LV + 沙箱"整体达成【一手-文档 指南p.115】。**`CS_RUNTIME` 位出现在 iOS 二进制里不等于 iOS 提供该功能开关**（简报 B6、C.6）。

**第二档：源码事实 + 需平台限定语的迁移。** 典型是 trust cache 与 entitlement 的关系。"When a binary runs… its code directory is extracted and hashed… platform privileges"这一段【一手-文档 指南p.69】的**语境是 Apple silicon macOS**；"Mandatory Access Controls (MACs) require code signing to enable entitlements protected by the system."【一手-文档 指南p.119】属 **macOS 章**。两者移用到 iOS 必须加限定或标"类比"【简报 E.3、E.4】。旧版 iOS Security Guide 的现行官方 URL 本次未获得（apple.com/business 链接失效，现版为 Apple Platform Security），因此这一档的 iOS 侧替代引文属**缺口**【简报 D】。

**第三档：只能标注为分析推断的组合结论。** 例如"iOS 与 macOS 用户态可触达的 IOUserClient 集合规模不同"这一表述，其依据是三块一手事实的组合：iOS 不允许第三方驱动（指南 App Store 模式章 p.116 语境；developer 文档从未向 iOS 开放 kext）、IOKit 框架在 `iokit/`+`libkern/`（README 目录口径）、AMFI/entitlement 判定在用户客户端路径上与 IOKit 核心直接耦合（`IOUserClient.cpp:1584-1595` 校验 `task`/`entitlement` 后转 `amfi->OSEntitlements.copyEntitlementAsOSObjectWithProc`）。结论合理，但**没有任何单一来源直接陈述它**，故必须带【分析推断】标注【简报 B7】。同理，"哪台设备用哪种 CSM"依赖 `board_config.h`，该文件属内部目标、开源 config 中不可全量获得，设备级映射写"未公开"。

另一条值得在此登记的跨层链条（供后段异常章沿用口径）是 Mach 异常到 BSD 信号的分层：`exception_triage_thread()` 按 thread→task→host 三级上抛【一手-源码 `osfmk/kern/exception.c:702-787`】，兜底的 host 级 handler 正是内核内的 Mach kobject `ux_handler`（其注释原文 "This is the default Mach exception handler for initproc, which then filters to all subprocesses as the host level exception handler for most Mach exceptions."），再由 `bsd/uxkern/ux_exception.c` 翻译成信号——即**信号不是内核直接投递的**。这条链条同时提供一个 iOS 特有的能力收口：`exception.c:351-364` 注释原文 "Only deliver control port if Developer Mode enabled, or task is a corpse. Otherwise we only deliver the (immovable) read port in exception handler"，即开发者模式未开启时异常接收方只拿到不可移动的 read port 而非 control port。该节完整展开属后段异常章，此处仅作为"Mach 层权利语义会随平台形态收紧"的第二个样本登记在案【交叉引用：简报 §B11】。

**本章口径边界**：3.1、3.2 的定性原文与目录职责、3.3 的 config 文件与 README 功能开关表述、3.5 链条中第 2/3/4/5 段的符号名与 sysctl，均为一手（源码或随源码发布文档）；3.4 的"大幅改动 Mach"是**已被推翻的三方措辞**，正文只保留其源码可观察部分并标【分析推断】。本章缺口有三处：README 之外的 Apple 宣传性定性（不提供，也不评价）、`config` 清单的逐项开关取值（未逐条锚定）、以及 iOS 侧 trust cache/entitlement 关系的现行官方文档（旧版指南 URL 失效）。凡本章出现的"iOS 与 macOS 同树不同形态"类结论，均已按上述三档标注证据强度，读者不应把它们合并为一句平台比较。

---

# Mach IPC：以内核对象为单元的能力模型

## 4.1 权利词汇表：port、send/receive right、dead name、deallocate

Mach IPC 的分析单元不是"消息"而是**权利（right）**：内核认可以及只认可某个进程对某个内核对象的特定处置能力。下表逐项给出该词汇在 XNU 里的实名出处，全部是一手；右列标注简报对每一词的核实结论。

| 术语 | 一手出处 | 内核侧含义 | 核实结论 |
|---|---|---|---|
| port / port name | `ipc_object.h`、`ipc_port.h`（大量 API 注释，属 `osfmk` 的 Mach IPC 子系统） | 用户态句柄到内核 `ipc_port` 对象的映射 | 已核实 |
| send right | `ipc_port.h:775,800,853` 等的 "naked send right" API 注释 | 可向该端口发消息；可多方持有 | 已核实 |
| receive right | `ipc_port` 相关 API（`ipc_port.h`）；`doc/port_types.md` 讨论 immovable receive 等增强语义 | 接收权决定端口对象的生命周期；`IOT_SERVICE_PORT` 一类端口被要求为**不可移动的 receive right** | 已核实（源码实名 + 官方 doc 语义） |
| send-once right | `ipc_port.h:511`；`doc/port_types.md` 用于 reply port | 只能发一次的权利，官方 doc 用它保证"恰好一个回复" | 已核实 |
| dead name | `ipc_object.h:100` `IPC_OBJECT_COPYIN_FLAGS_DEADOK`、`:123` "The object is dead"、`:519-520` `ipc_object_alloc_dead`（分配 dead-name entry） | 对象已"死"后仍可存在的名字形态；内核为其分配 entry，并在 copyin 时按 DEADOK 标志决定是否接受 | 已核实 |
| deallocate / destroy（处置语义） | `MACH_SEND_DEALLOCATE` 与 `MACH_PORT_RIGHT` 处置；`<mach/mach.h>` 头注释；源码措辞见 4.2 | 丢弃本地权利；deallocate 不通知原持有者 | **部分核实**：区分主要见于历史 Mach 手册与头文件注释，属【手册页/头文件】级，引用时须注明出处为头文件而非开发者文档 |
| port right 误用 | `doc/guard_exceptions.md:67+` 整节 "Port right mismanagement" | 权利误用（如把 send right 当 receive 处置）触发 guard 异常 | 已核实 |

三个阅读要点。**第一，权利是对象引用而非整数。** `ipc_object.h:519-520` 的 `ipc_object_alloc_dead()` 说明 dead-name entry 是被"分配"出来的内核对象条目，而不是用户侧数字的副作用；这决定了"销毁权利"在内核里必然对应一次对象处置，也因此才可能在其上绑定策略（见 4.3、4.5）。

**第二，send-once 是被当作安全语义使用的，不只是优化。** `doc/port_types.md` 明确把 reply port 的 send-once 性质表述为保证"恰好一个回复"【一手-官方doc doc/port_types.md】。这一句很重要：它说明 Apple 在官方文档层面把权利**类型**当作可依赖的安全属性来使用，而不是仅仅当作引用计数的特例。

**第三，deallocate 的"不通知 owner"要按头文件级引用。** 关于 deallocate 与 destroy 的差别，简报的处理是：源码与标头确实使用 `MACH_SEND_DEALLOCATE` / `MACH_PORT_RIGHT` 处置语义，但 **Apple 公开文档层面对这两者的区分主要见于历史 Mach 手册与 `<mach/mach.h>` 头注释**，属【手册页/头文件】级；因此正文可以描述处置语义的差异，但必须注明出处是头文件，且不得写成"Apple 开发者文档规定"。

**四问**：权利模型**负责**的是"把访问控制下沉到内核对象引用层"；**强制点**在每次权利处置与消息发送的内核路径上（`osfmk/ipc/`）；**生效前提**是发送方确实持有该权利、且其所处 ipc space 的策略允许该操作（4.3）；它**不覆盖**的是接收方的身份认证——拿到 send right 就等于有权发送，这正是 4.3/4.4 要处理 confused deputy 问题的原因。

**本章缺口预告**：`mach_msg(2)` 的现行官方手册页缺失（对应 Darwin man pages URL 实测 404），消息传输语义只能限定在 `<mach/mach.h>` 头与历史 Mach man page 范围内；完整处理见 4.6。

## 4.2 port right 即 capability：一手出处与层级差

「Mach port right 本质上是一种 capability」这个说法在学界与工程写作里很常见，但它在 Apple 材料里的证据分布很不均匀——这正是第 1.5 节所说"混合来源必须拆开表述"的典型场景。

**一手存在的位置：源码注释与仓库内官方 doc。** XNU 的 `osfmk/ipc/ipc_object.h` 在权利处置函数上直接使用 capability 一词，构成一对完整措辞【一手-源码 ipc_object.h:541-581】：

| 行号 | 注释原文 | 对应的用户侧动作 |
|---|---|---|
| `:541` | "Copyin a capability from a space" | 从某 ipc space 取入权利（发送/接收消息时的入参处置） |
| `:551` | "Copyin a naked capability from the kernel" | 从内核取入裸权利 |
| `:556` | "Destroy a naked capability" | 销毁 |
| `:561` | "Destroy a naked destination capability" | 销毁目的端权利 |
| `:572` | "Copyout a capability, placing it into a space" | 把权利放入某 space（随消息外发） |

同文件 `:541,551,556,572,581` 处均出现 "capability" 措辞（`:581` 附近另有 "Allocate a dead-name entry" 一类表述）【简报 B2】。仓库内官方文档则直接使用能力模型的经典安全术语：`doc/ipc_security_concepts.md:49` 使用 **"confused deputies"**【一手-官方doc】；错误返回值也按能力语义命名——`doc/guard_exceptions.md:345` 提及 `KERN_INVALID_CAPABILITY`【一手-官方doc】。

**结论层级要写准。** 因此可以安全陈述的是：*在 Apple 自己的源码注释与随源码发布的官方文档中，port name / port right 被直接称为 capability，非法权利处置的错误码也叫 `KERN_INVALID_CAPABILITY`*。**不可以**陈述的是：*Apple 开发者文档/支持文档称 port right 为 capability*——简报明确记录，在 Apple 面向开发者与用户的文档层面**未发现该表述**【简报 C.2】。这两个句式的差别不是文风，而是引用场合：前者可挂"Apple 一手（源码/仓库文档）"，后者挂名即错。

**为什么这个层级差值得单独一节。** 因为"capability"这个词自带一整套设计预期——不可伪造、按对象分发、权限随权利转移而转移。把它挂到 Apple 官方文档上，会让读者以为 Apple 对外承诺了 Mach IPC 的能力式安全语义；而真实情况是：能力式语义在**实现层与仓库文档层**是明确的，在**对外承诺层**是缺席的。第 4.3–4.5 节展示的正是实现层的实际强度：Apple 确实在 `is_policy`、pinned send rights、port 类型策略上把能力模型的访问控制做实了，但它的表述载体始终是"源码/仓库 doc"，不是"开发者文档"。

**四问**（capability 视角的权利模型）：**负责**——把"能不能做这件事"归结为"是否持有这种权利"；**强制点**——权利处置（copyin/copyout/destroy）与发送路径，位于 `osfmk/ipc/`；**生效前提**——权利的取得本身合法（内核分配或经允许的消息传递获得），且所在 space 的策略不禁止该处置；**不覆盖**——权利一旦被交出便无法在传输层撤回或限定用途，因此需要端口类型级与 space 级的附加约束（4.3、4.5）。

## 4.3 IPC space policy：把进程身份投影成一个不可变枚举

如果把 4.2 的能力式表述当作教科书语言，就会漏掉现代 XNU 真正做的事：**把"这个进程是谁"投影成一个可以在 IPC 路径上无锁读取的枚举值**。Apple 在仓库内文档 `doc/ipc_security_concepts.md` 里把这层机制写得相当具体【一手-官方doc doc/ipc_security_concepts.md:10-37】。

| 要素 | 文档口径 | 机制含义 |
|---|---|---|
| 每 space 一个策略值 | 每个 ipc space 携带**不可变**的 `is_policy` 枚举 | 策略在 space 建立时定死，之后不随进程状态漂移；判定退化为一次枚举比较 |
| 多源策略投影 | 把 platform binary 身份、浏览器 entitlement 等系统策略**投影为单一值** | 分散在各子系统（AMFI/entitlement/平台判定）的结论在 IPC 层被归一，避免每条消息重算 |
| 无锁查询 | 该值可无锁读取 | 发送路径上的高频检查不因锁而成为瓶颈，也不引入锁序问题 |
| dPAC 加固 | "dPACed in order to be resilient to early memory corruption" | 策略字段本身受指针认证类保护，目的是**抵抗早期内存破坏**——即攻击者在早期阶段污染该字段时不能任意取值 |

四个问题一次答清。**负责什么**：它负责把"进程身份/entitlement"这一系统级结论，转成 IPC 权利处置时可用的访问控制输入；它回答的是"这个 space 里的端口被允许以何种方式收发/存在"，而**不是**"这条消息内容是否合法"。**强制点**：`is_policy` 的读取发生在 Mach IPC 的权利处置与消息路径上（Mach IPC 子系统，`osfmk/ipc/`；简报另在 `ipc_policy.c:1600` 处记录了针对 `is_ux_handler_port(port)` 的特判【一手-源码 ipc_policy.c:1600】，可作策略层确有集中判定点的旁证）。**生效前提**：策略值在 space 建立时即由系统侧输入决定——因此**普通进程无法自行改变它**，能影响它的只有身份与 entitlement 本身（其判定属后段 AMFI 与代码签名章节的范围）。**不覆盖什么**：它不提供消息级授权，也不覆盖已被合法转让的权利——权利一旦进入某 space，其后续处置受该 space 的策略约束，但约束粒度是 space 级而非 call site 级。

这一层的意义要按来源层级说清：**"port 空间的访问控制取决于进程身份/entitlement"在 Apple 一手（仓库内官方 doc）里有文档**【简报 B2 已核实】，因此可以按一手引用；但同一结论在 Apple 面向开发者/用户的文档中不存在，不得写成"Apple 开发者文档规定"。dPAC 那句尤其值得保留原文，因为它是少见的、由 Apple 自己说明"某字段做指针签名是为了抵抗早期内存破坏"的一手表述；该口径与 PAC 机制的耦合关系在本节内单独展开。

**与指针签名（PAC）的耦合点值得单独点出。** 上面第四条口径——`is_policy` 被 dPAC 保护以抵抗早期内存破坏——说明 Apple 在这层做的是一个复合设计：**能力模型的判定输入本身由硬件辅助的完整性保护兜底**。把两件事分开看很容易低估其意义：`is_policy` 只是枚举值，若它可以被任意改写，攻击者就能把自己所在 space 的策略伪造成平台二进制级别；而 dPAC 让这种改写不能取任意值。因此"Mach IPC 的能力模型"与"控制流/数据签名"在本篇的口径里不是两个互不相干的专题，而是**同一决策的语义层与完整性层**（PAC 侧的展开属后段控制流与指针保护章节）。

需要保留的不确定性有两条。**其一**，简报核实的是"存在这种投影"，未列出 `is_policy` 的具体枚举取值与投影规则表，因此本篇不写任何枚举名。**其二**，`is_policy` 的输入来自进程身份与 entitlement，而这两者的判定链（`ubc_subr.c` 解析 → AMFI 判定）在链条上游还有"实现体闭源"的段落（见 3.5 第 4 段），所以**这一层的"取决于身份"在端到端意义上不能整体标为已核实**——可核实的只是 IPC 层的读取与使用方式。这是分层拆解的必要诚实：一层机制可以一手可见，而它的输入却来自不可见的上游。

最后一点容易被低估：**不可变性既是保护也是限制**。因为 `is_policy` 在 space 生命周期内不变，任何"运行中动态调整某进程 IPC 权限"的设计都不可能落在这一层，只能落在端口类型策略（4.5）或更上层。这一判断属【分析推断】，依据仅是"`is_policy` 不可变"这一条一手口径。

## 4.4 pinned send rights：control port 的强制常驻

pinned send rights 是 Mach 层针对 **confused deputy（被误导的代理）** 这一经典能力模型失效方式所做的加固，Apple 在 `doc/ipc_security_concepts.md:39-113` 给出完整口径【一手-官方doc】。

**机制内容**：task 与 thread 的 control port 所对应的 send right 被强制常驻（源码侧标志为 `IE_BITS_PINNED_SEND`，并要求 `urefs >= 1`，见 `doc/ipc_security_concepts.md:92`）。换句话说，内核不允许"控制端口的发送权被意外全部丢弃后仍继续运行"这种状态出现。

**它防的是什么**：文档把动机直接写成防 port 生命周期错误导致的 confused deputy——当一个进程因为权利管理失误（过早 dealloc、把 control port 当普通端口处置）而失去对自身的控制权，或让第三方持有了本该独占的权利时，后续那些"以合法身份执行的操作"就变成被误导的操作。权利常驻使这类生命周期错误**在发生当场就变成可检出的违规**，而不是延后表现为一次看似合法的越权操作。

**违规后的分级反应**（这条是本机制最值得防御方关注的部分）：

| 进程形态 | 违规后果 | 依据 |
|---|---|---|
| hardened（强化）进程 | **硬执行**：进程被终止 | 【一手-官方doc :39-113】 |
| 其他进程 | 返回 `KERN_INVALID_CAPABILITY`，并产生一次**非致命**的 guard exception | 同上；`KERN_INVALID_CAPABILITY` 亦见 `doc/guard_exceptions.md:345` |
| 反应强度本身 | 由 boot-arg **`ipc_control_port_options`** 决定 | 同上 |

**四问**：**负责**——保证 task/thread control port 的发送权不因生命周期管理错误而消失，把"能力丢失"变成可检出的即时违规；**强制点**——权利处置路径上的 pinned 标志检查（`IE_BITS_PINNED_SEND`/`urefs` 条件），违规经 guard exception 通道上报；**生效前提**——进程被认定为 hardened 形态，且 boot-arg 未把反应强度降到仅返回错误码；**不覆盖**——它不验证权利是如何取得的，也不管权利被**合法转让**后的用途，因此必须与 4.3（space 级策略）和 4.5（端口类型级策略）配合才构成完整约束。

**把两条证据合起来看，可以还原出一条"权利强度序"。** 同一段 Mach IPC 语义里，Apple 实际交付给外部观察者的权利形态是分级的：control port（可读写状态、可续行）> send right（可发送）> **immovable read port**（只可读取标识、不可移动）> dead name（对象已死，仅剩名字）。4.4 的 pinned send rights 保证"该常驻的强权利不会消失"，而异常投递路径的 read/control 取舍决定"新交付的权利尽量弱"。两者方向相反却互补：**前者防的是权利被误丢，后者防的是权利被多给**。这条序是本报告对"Mach IPC 作为能力模型"最实用的概括，但它属【分析推断】——依据是上述两条一手事实的组合，Apple 未在任何来源里写过这样一个排序。

必须保留的限定：`ipc_control_port_options` 是 boot-arg，因此 pinned send rights 的**实际反应强度取决于设备启动参数**；本篇未获得"零售 iOS 上该 boot-arg 可取何值、是否可被外部影响"的一手口径，故只陈述"行为由该 boot-arg 决定"这一源码/文档事实，不推断零售形态【简报 B2】。

同一文档家族还给出另一条相关的 iOS 侧收口，可作为"权利形态随平台形态收紧"的第二个一手样本：异常投递路径上，仅当开发者模式开启或目标为 corpse 时才交付 control port，否则只交付**不可移动的 read port**（`exception.c:351-364`，注释原文含工单号 94669540）【一手-源码，交叉引用：简报 §B11】。把两条并排放置可以看出 XNU 的通行做法：**默认交付尽可能弱的权利形态，把强权利留给显式启用的场景**。

## 4.5 按端口类型绑定策略：port\_types 与 guard 异常

如果只把 Mach IPC 理解为"端口 + 消息"，就会错过现代 iOS 上真正在起作用的那一层：**Apple 给不同用途的端口绑定了不同的安全语义，违规即触发异常**。这套内容在仓库内文档 `doc/port_types.md` 中是逐类型写明的【一手-官方doc doc/port_types.md】，简报据此给出一条重要写作指示：**报告应以它为主体，避免依赖 CMU 时代的教科书描述**【简报 B2】。

| 端口类型 | 绑定的安全语义（官方 doc 口径） | 防的是哪类失效 |
|---|---|---|
| `IOT_SERVICE_PORT` | launchd 按需服务端口："immovable receive right + enforced reply port semantics"（接收权不可移动 + 强制 reply port 语义） | 服务端接收权被转手/挪动导致服务被劫持 |
| `IOT_CONNECTION_PORT` | 防中间人（连接端口的对偶关系不可被第三方插入） | 连接建立过程中的 MITM 与权利替换 |
| `IOT_REPLY_PORT` | 以 send-once 保证"恰好一个回复" | 回复被多次消费、伪造多个应答 |
| `IOT_EXCEPTION_PORT` | 配合 entitlement `com.apple.security.only-one-exception-port` | 多异常端口造成的观察者竞争与重复投递 |
| `IOT_CONNECTION_PORT_WITH_PORT_ARRAY` | 仅 **platform binary** 可接收 OOL port array，且**限 1 个**、处置**仅 `COPY_SEND`**；需 entitlement `com.apple.developer.allow-connection-port-with-port-array` | 借连接端口批量转运权利（把连接变成权利走私通道） |

最后一行是本节信息密度最高的一条，值得按四问拆开：**负责**——限制"随消息携带端口数组"这一强能力只对平台二进制开放，并把数量与处置方式钉死；**强制点**——消息接收路径上对 OOL port array 的类型/数量/处置校验，违规经 `mach_port_exc_guard` 异常上报（`doc/port_types.md:157-164`）；**生效前提**——接收方被认定为 platform binary，或持有该专名 entitlement；**不覆盖**——它不管数组里每个端口自身的使用方式，权利一旦交付即回到 4.2/4.3 的能力语义下由 space 策略继续约束。

**违规的统一出口：guard exception。** 类型语义之外的通用权利误用也走同一条通道。`doc/guard_exceptions.md:67+` 有整节题为 **"Port right mismanagement"**，列出权利误用的各类形态；消息计数/覆写一类违规（msgcount/overwrite）则为 **fatal guard**【一手-官方doc guard_exceptions.md】。这一层的防御价值在于：它把"能力管理错误"变成**在出错当场可见的事件**（异常 + `KERN_INVALID_CAPABILITY`），而不是留给后续行为去暴露。对审计者而言，是否把某类误用配置为致命，是比"是否存在检查"更有操作意义的问法。

**为什么本篇以 `port_types.md` 为主体叙述。** 上面那条"以它为主体"的指示，分量比它看起来大。以教科书口径写 Mach IPC，读者得到的是一套权利代数（send/receive/dead name 的组合规则）；以 `port_types.md` 口径写，读者得到的是"哪种用途的端口被允许做什么、违反后发生什么"。前者是通用模型的性质，后者才是这台设备上的实际强制。第 4 章的三节（4.3、4.4、4.5）就是按这一取向组织的：**权利代数用作词汇表（4.1、4.2），策略主体交给 Apple 自己写下的类型语义**。

**必须承认的一手局限**：`doc/port_types.md` 给出的是**语义与策略**，未给出各类型在零售 iOS 上被实际创建的比例、以及每类型在闭源策略下的例外规则；此外"浏览器 entitlement 一类系统策略如何映射到 `is_policy` 的具体枚举取值"，简报只记录了"存在这种投影"【一手-官方doc :10-37】，未列取值表。凡涉及具体枚举名与阈值的写法，本篇一律不写。

## 4.6 名额、限额与 mach\_msg(2)：三个必须保留的审计缺口

本章前三节展示了 Mach IPC 能力模型在 Apple 一手材料里相当扎实的部分；但有三处**结构性缺口**必须原样保留。它们不是"资料没找全"，而是公开口径本身不存在——这直接决定了外部报告与内部结论在引用时的边界。

| 缺口 | 核查记录 | 允许的写法 | 禁止的写法 |
|---|---|---|---|
| 每进程 port 名额 | **不存在任何 Apple 官方公开文档记载**；XNU 内部确有 ipc 表增长逻辑与 MIG 层限制，具体阈值属源码事实而非公开规格 | "未公开，来自源码观察"；或只陈述"存在条目表增长逻辑"而不给数字 | 写出具体名额数字并挂名官方规格 |
| send right 引用计数上限 | 计数本身可一手核实：`ipc_entry` 的 `IE_BITS_UREFS()` 见于 `doc/ipc_security_concepts.md:92`（pinned send 要求 `urefs >= 1`）【一手-官方doc doc/ipc_security_concepts.md:92】→ 内核确有用户级引用计数；**上限无数** | 写"存在 urefs 计数（源码/doc 级）" | 写"Apple 文档规定了 urefs 上限为 N" |
| msgsend 资源限额 | 负面结果：本地镜像内 grep `msgsend`、`quota`、`max_urefs` 于 `ipc_port.h`/`ipc_object.h` **均无命中**；简报判断该命名更可能是 FreeBSD `rctl`/`mac_rlimit` 概念，标注【未核实，倾向不存在】，并要求"正文不得引用，除非另得一手" | 说明该命名更可能源自 FreeBSD 谱系，且本次未获一手 | 把 msgsend 限额当 XNU/iOS 机制描述，或断言"XNU 没有消息资源限制" |

**第四处缺口是 `mach_msg(2)` 的现行官方文档。** Darwin man pages（`opensource.apple.com/man/`）中对应的 `mach_msg(2)`/port 分配类手册页 **URL 实测 404**（简报把这一负面结果同时写入 D 节缺口清单与 F 节索引）【简报 A④、D】。这带来一个具体的写作约束：**完整 OOL（out-of-line）与 deadline 语义不能以"现行 Apple 手册页"为出处**，其可依赖范围只能限定在 `<mach/mach.h>` 头与历史 Mach man page【简报 B2】。本地镜像里也没有 mach_msg2 的核心源文件（`ipc_mqueue.c`/`mach_msg.c` 不在镜像内），所以"从源码补齐传输语义"这条路同样不通。

当前可核实的**一手替代口径**是这三份仓库文档：`doc/ipc_security_concepts.md`（space 策略与 pinned send）【一手-官方doc】、`doc/port_types.md`（reply port 用 send-once 保证"恰好一个回复"、immovable receive 等增强语义）【一手-官方doc】、`doc/guard_exceptions.md`（msgcount/overwrite 等违规 → fatal guard）【一手-官方doc】。也就是说：**权利语义与策略层有一手可依，消息传输层的细粒度规范（OOL 描述符、超时/ deadline、错误码全表）处于公开口径缺失状态**。

这一缺失对防御工作的实际影响值得点名：任何依赖"消息级参数校验契约"做安全评估的读者，都不能把 `mach_msg` 的行为当作可对外引用的稳定规范来描述，只能引用 Apple 明确写下的**端口类型级**语义（4.5）。反过来，把"手册页 404"解释成"Apple 不再支持该 API"或"iOS 上没有这层语义"都是无依据的——简报只支持一句话：**Apple 对 `mach_msg` 传输语义的现行公开文档，本次未获得可核实条目**。

**本章口径边界**：4.1 的权利术语、4.2 的 capability 措辞表、4.3 的 `is_policy`/dPAC、4.4 的 pinned send rights 与分级反应、4.5 的五类端口语义与 `mach_port_exc_guard`，均为**一手**（源码或 Apple 随源码发布的官方 doc；简报 B2 逐项标为已核实）。需要限定语的有三处：deallocate/destroy 的区分属【手册页/头文件】级；"port right = capability"在开发者/支持文档层查无表述（简报 C.2）；urefs 计数只有源码/doc 级来源（C.10）。缺口四处：port 名额、计数上限、msgsend 限额、`mach_msg(2)` 现行手册页。本章不包含任何端口分配、消息伪造或权利转移的操作步骤。

---

# 内核内存分配：zone、kalloc 与分配器加固

## 5.1 kalloc 与 zone 的关系：size-class 分发而非独立分配器

内核内存分配是"机制纵向拆解"最容易写出价值的一章：它既是绝大多数内核内存破坏问题的落点，又是 Apple 在源码里把加固写得很具体、却在对外文档里几乎不谈的区域。本节先把当前形态说清楚。

**当前事实：kalloc 构建在 zone 分配器之上。** 在现代 XNU 中，`zalloc.c` 直接 include `<kern/kalloc.h>`（`src/zalloc.c:96`），并由 `track_kalloc_zones()` 与 `:2847-2859` 的注释给出 zone 命名规则【一手-源码 zalloc.c:96, 2847-2859】：

| zone 名模板 | 用途 | 观察含义 |
|---|---|---|
| `kalloc.<size>` | 按 size class 的通用内核分配 | 同一 size 的分配彼此同 zone，即"大小相同"是唯一分组依据 |
| `kalloc.type.<size>` | **类型化**（typed kalloc）分配 | 分组依据升级为 C++ 类型/请求的 kalloc type，见 5.5 |
| `kalloc.type.var.<size>` | 类型化但大小可变 | 对应 `KHEAP_ID_KT_VAR` 一类的可变大小堆 |
| `early.kalloc` | 启动早期分配 | 早期内核状态，此时安全加固尚未完整建立 |

由此可以给出简报所称的当前源码事实：**"kalloc 走 zone（per-size-class zone）"**；与之配套的一条版本限定语见下一段。

**版本限定语（必写的一条）。** 简报要求："kalloc 曾是独立实现"这类句子必须带版本限定——历史版本（约 xnu-7000 之前的 `vm_kern` 时代）的 kalloc 是独立的 slab 式实现，当前源码事实是 kalloc 构建在 zone 之上【简报 B3】。这条限定的实际作用不止于准确：它提醒读者，**任何"kalloc 有 X 属性"的论断都隐含版本假设**，同一句话在旧设备上可能不成立；对做兼容性评估或跨版本比对的读者，这一句比机制细节更容易被忽略。

同时，"归属关系"还要与第 3.3 节的构建维度叠看：zone 命名与安全配置都是**编译期/初始化期**确定的（`track_kalloc_zones()` 建立的是 kalloc 与 zone 的对应关系），因此同一份源码在不同 config 下给出的加固覆盖面可以不同——这正是 5.3 末尾"取值未逐条锚定"这条限定的来源。

**为什么这个归属关系重要（防御视角）。** 一旦 kalloc 落到 zone 之上，第 5.2、5.3 节列出的所有 zone 级加固（专属 `zone_map` 地址范围、chunk 尾 guard page、tagging、read-only、OOB 调整）就自动作用于几乎全部小尺寸内核分配，而不只是某些显式使用 zone API 的对象。换言之，**"分配器加固的覆盖面"是由 kalloc 与 zone 的这层关系决定的**，而不是由加固代码自身的复杂度决定的。这也是为什么把 iOS 内核内存安全简单叙述成"有 zone 分配器"会漏掉重点：重点在于所有内核小对象都被纳入同一套可加固、可统计、可在 panic 时回报的路径。

**四问**：**负责**——为内核提供按大小/类型分组的通用分配，并把分配纳入可加固的容器；**强制点**——`zalloc.c` 内的分配与释放路径（size class 选择、zone 归属、chunk 管理），以及 `vm_kern.c` 侧的 kmem 子分配与校验；**生效前提**——相关 zone 安全配置在编译期被纳入（见 5.3），且分配确实经由 kalloc/zone 而非其他专用分配器；**不覆盖**——超过 zone 分配上限的尺寸（`ZONE_MAX_ALLOC_SIZE` 为 `32*1024`【一手-源码 zalloc.c:158】）走不到 size-class zone 这条路径；启动早期（`early.kalloc`）与类型化分组之外的对象，其加固强度需另行核对。

## 5.2 zone\_map、chunk 与 guard page

zone 分配器的第一项加固不是"检查分配"，而是**把可分配对象收进一个专属地址范围**。这样做的价值在于：越界访问落在受保护页上时，故障是**同步且可识别**的，而不是安静地踩到邻近对象。

| 源码事实 | 出处 | 含义 |
|---|---|---|
| `static struct mach_vm_range zone_map_range;` | 【一手-源码 zalloc.c:399】 | zone 分配器拥有自己的地址范围对象，而非与通用 kmem 混用 |
| `from_zone_map(addr, size)` 宏 | 【一手-源码 zalloc.c:804】 | 内核在路径上可以判断"这个地址是否来自 zone_map"，即可做归属校验 |
| 注释 "This chunk ends with a guard page." | 【一手-源码 zalloc.c:194】 | **zone chunk 的尾部是 guard page**：这是"内核内存 guard page"这一常见说法真正对应的东西 |
| `zm_oob_offs /* in guard pages */` | 【一手-源码 zalloc.c:233】 | zone map 上维护以"guard page 数"为单位的越界偏移元数据 |

这张表最容易被略过的一栏是 `zm_oob_offs`。它记录的是"以 guard page 计"的越界偏移量，也就是说：**分配器不仅布置了边界，还留了把越界量换算回请求尺寸的元数据**。这类元数据的存在对 panic 分析有直接价值（与 5.6 的类型统计属同一可观测面），但它同时提醒我们注意覆盖面边界：换算逻辑只在越界确实撞上 guard page 时才被触发。

`zone_map` 的第二个作用是**归属判定**。`from_zone_map(addr, size)` 让内核在任何怀疑某地址来自 zone 分配器时做一次范围判断【一手-源码 zalloc.c:804】，而不必遍历 zone 元数据；配合 `zone_map_range` 这个专属 `mach_vm_range` 对象（`zalloc.c:399`），zone 分配获得了"地址段—容器"的对应关系。审计上这意味着：**判断"这块内核内存是不是 zone 分配的"是一个范围检查问题，而不是类型识别问题**，因此它不会被对象内容破坏所干扰，但反过来也无法区分同一 zone 内的不同对象。

对"加固覆盖面"的完整表述因此是三条：**专属地址范围（zone_map）+ chunk 尾部保护页（guard page）+ 越界偏移元数据（`zm_oob_offs`）**。这三条都在 `zalloc.c` 里可一手核对，但请注意它们都属于**地址布局层面**的加固；内容层面（对象是否被写成别的类型）由 5.3 的 tagging 与 5.5 的类型化分堆负责。把这两层混作一层，是"zone 加固"相关误述的共同根源。`PGZ_OOB_ADJUST`（page-zone out-of-bounds adjust，见 5.3）这一命名本身就说明这条路径是为"越界"准备的调整逻辑，而不是通用的对象校验。

**四问**：**负责**——把 zone 分配限制在专属地址范围内，并在每个 chunk 尾部放置不可访问页，使越界写入/读取在越过 chunk 末尾时立即触发保护故障。**强制点**——`zalloc.c` 内 chunk 分配与地址判定路径（`zone_map_range`、`from_zone_map()`），保护页本身的强制由页表权限完成。**生效前提**——该 zone 走 chunk 式分配（受 `ZONE_MAX_CHUNK_ALLOC_NUM` 一类的 chunk/分配上限常量约束【一手-源码 zalloc.c:161】），且分配规模在 zone 分配上限之内（`ZONE_MAX_ALLOC_SIZE` 为 `32*1024`【一手-源码 zalloc.c:158】）。**不覆盖**——这一点必须写明：**guard page 位于 chunk 尾部，因此它拦不住"在同一个 chunk 内跨对象"的越界**。同一 chunk 内相邻对象之间的溢出不会碰到保护页，只会落到邻近对象——这正是 `ZONE_TAGGING` 与 typed kalloc（5.5）要处理的问题域，也是"分配器加固"不能只靠 guard page 的原因。

顺带修正一条常见误述的成因：许多二手资料把 XNU 的这项能力概括为"内核内存有 per-page guard"。它把两件不同的事压成了一件——真实形态是 **zone chunk 级**的保护页加上 OOB 元数据，而不是"每个分配对象一页、每页都有 guard"。第 5.4 节按误解校正格式展开。

## 5.3 安全开关的真实名字逐个列出

内核内存加固的讨论最容易滑向"编造宏名"——二手材料里常见的 `SECURITY_*` 一类写法并不存在。简报对名称逐个 grep 核实，本篇只列**核实到的真实名称**，并给出一句总原则：写作时禁止编造 `SECURITY_*` 类不存在的宏名【简报 B3、C.4】。

| 真实名称（逐项核实） | 出处 | 它负责什么 | 生效前提 / 局限 |
|---|---|---|---|
| `ZSECURITY_CONFIG(ZONE_TAGGING)` | 【一手-源码 zalloc.c】 | zone  tagging：给 zone 打标签，使归属可判定、越界/误用更易检出 | 编译期安全配置；具体构建取值未逐条锚定 |
| `ZSECURITY_CONFIG(READ_ONLY)` | 同上 | 只读 zone：把某类对象所在 zone 设为只读 | 需分配点确实声明为只读用途；不覆盖普通可写 zone |
| `ZSECURITY_CONFIG(SAD_FENG_SHUI)` | 同上 | 缓解"风水"式碎片化布局（把分配摊开以降低相邻挪用收益） | 属缓解而非修复；命名取自源码注释传统 |
| `ZSECURITY_CONFIG(PGZ_OOB_ADJUST)` | 同上 | page-zone out-of-bounds adjust：面向越界的尺寸/布局调整（与 5.2 的 guard page、`zm_oob_offs` 同域） | 仅作用于相关 zone 形态 |
| `SECURE_KERNEL` | 【一手-源码 README.md，自述"use SECURE_KERNEL if your code implements new functionality exclusively relevant to the trusted kernel"】 | 可信内核专属功能的总开关（3.3 已述其"按功能开关编译"含义） | 功能维度开关，非平台维度 |
| `ZONE_MAX_ALLOC_SIZE`（`32*1024`） | 【一手-源码 zalloc.c:158】 | 单次 zone 分配的规模上限 | 超过该规模的分配不走此路径 |
| `ZONE_MAX_CHUNK_ALLOC_NUM` | 【一手-源码 zalloc.c:161】 | chunk 内分配数量一类的上限常量 | 影响 chunk 形态与 5.2 的覆盖面 |
| `kalloc_is_restricted_data_mode_enforced()` | 【一手-源码 vm_kern.c:216】 | 运行期判定"受限数据模式是否被强制执行" | 判定函数存在；其取值条件依赖构建/形态，未逐条核实 |

三点解读。**第一，`ZSECURITY_CONFIG(...)` 是一个族而非一个开关。** 它把多项 zone 级安全配置收敛到同一个宏形式下，因此"iOS 是否启用了 tagging"这类问题的正确问法是"该构建里 `ZSECURITY_CONFIG(ZONE_TAGGING)` 的取值是什么"——而这个取值本次核查未逐条锚定，简报对 typed kalloc 的 iOS 生产启用程度同样标注"需以 config 为准（未逐条核实），写作加「源码机制」限定"【简报 B3】。本篇所有相关表述都带这一限定。

**第二，`SAD_FENG_SHUI` 值得单独解释一句，因为它最容易被误读成"修复"。** 该名称对应的是把分配布局打散、降低"相邻对象被顺手挪用"这类收益的缓解手段；它不阻止溢出本身，也不改变对象生命周期。把这类缓解叙述成"防溢出机制"会误导加固决策——真正的类型隔离来自 tagging 与 typed kalloc（5.5）。

**关于"上限常量族"的一条名称说明。** 二手材料里常出现 `ZONE_MAX_CHUNK_ALLOC_SIZE` 这一写法。本次核查**逐字核实到的名称是 `ZONE_MAX_ALLOC_SIZE`（值 `32*1024`，`zalloc.c:158`）与 `ZONE_MAX_CHUNK_ALLOC_NUM`（`zalloc.c:161`）**，前者管单次分配规模、后者管 chunk 内分配数量。本篇因此只使用后两个名称；`ZONE_MAX_CHUNK_ALLOC_SIZE` 未出现在核实清单内，其是否存在与取值如何均属未核实项——按第 1.6 节的纪律，这里既不采用该名称，也不断言它不存在。zone/chunk 尺寸与数量确实受上限常量约束这一**结构事实**是一手的，具体常量名的差异则属于必须精确对待的部分。

**第三，`kalloc_is_restricted_data_mode_enforced()` 是运行期函数，不是编译开关。** 这一区分对可观测性有直接意义：运行期判定意味着同一构建在不同条件下可能给出不同答案，因此"该模式是否生效"无法仅靠读源码断言。简报只核实了该函数名的存在【一手-源码 vm_kern.c:216】，未核实其判定条件与各平台取值，本篇据此不写任何结论。

**四问**（这一族开关整体）：**负责**——在不改调用点代码的前提下改变 zone 的布局、属性与归属标记；**强制点**——`zalloc.c` 的 zone 创建与分配/释放路径，配置在编译期定型；**生效前提**——相应 `ZSECURITY_CONFIG` 项在该平台清单里被取为开启（未逐条核实）；**不覆盖**——它们不校验内容、不理解类型语义（类型语义靠 5.5），也不约束绕过 zone 的分配路径（`vm_kern.c` 侧 kmem 子分配，见 5.4）。

## 5.4 误解校正：不存在名为 "per-page guard" 的公开宏

误解校正：「iOS 内核有 per-page guard 保护每个分配页」。

| 项 | 内容 |
|---|---|
| **常见说法** | "XNU 内核分配器为每个分配提供 per-page guard"，或写成"内核内存有 guard 页 + `SECURITY_*` 系列编译选项" |
| **正确口径** | 真实存在的表述是 **zone chunk 尾 guard page**（`zalloc.c:194` 注释 "This chunk ends with a guard page."）加上 `ZSECURITY_CONFIG(PGZ_OOB_ADJUST)`（page-zone out-of-bounds adjust）与 `zm_oob_offs`（以 guard page 数计）【一手-源码 zalloc.c:194,233,158,161】。**不存在名为 "per-page guard" 的公开宏**；`SECURITY_*` 一类名称同样不成立 |
| **依据层级** | 一手-源码（宏名逐个 grep 核实）+ 简报 B3、C.4 的明确纠正条款 |
| **误读的实际代价** | "per-page guard" 暗示"每个对象都被独立保护页包围"。若按这个预期评估风险，会严重高估覆盖面：真实布局是**多个对象共享一个 chunk、只在 chunk 末尾有保护页**，因此同 chunk 内的跨对象溢出不会触发保护故障（见 5.2 的"不覆盖"）。这类高估比低估更危险，因为它会让审计在此处停止 |

**与 guard page 相邻的另一项机制：`kmem_guard_t`。** 名称里同样带 "guard"，但它是**完全不同的东西**——不是保护页，而是 kmem 子分配层的元数据校验。可核实的部分：`vm_kern.c:224-238` 存在 `kmem_guard_t` 类型及 `__kmem_guard_left/right/size` 字段，`:507-518` 存在 `kmem_entry_validate_guard()`【一手-源码 vm_kern.c:224-238,507-518】。这些字段名（left/right/size）说明它的用途是**记录并校验分配项两侧的守卫信息**，即对分配项元数据做一致性验证。

必须同时写出两条限定。**其一，本地镜像的 `vm_kern.c` 是节选文件**：`kmem_guard_t` 与 `kmem_entry_validate_guard` 的名称/字段已核实，但节选**未包含 `kmem_allocate()` 函数体本身**，因此"该 guard 校验在分配/释放路径的哪一步被调用、失败后做什么"这类结论需要引上游完整文件才能给出【简报 B3、A⑤】。**其二，`kmem_allocate` 属于 vm_kern 层的 kmem 子分配 API**，其宏名与 `kmg_*` 字段已核实，但函数本体的引用位置未在本篇锚定。这两条限定的性质是"本地节选所致、非事实缺失"，与 5.3 里"取值 Apple 未公开"的性质不同，读者不应混用（见 2.3）。

**四问**（kmem 层校验）：**负责**——校验 kmem 分配项自身的元数据一致性，使元数据破坏在下次访问时被检出；**强制点**——`kmem_entry_validate_guard()`；**生效前提**——分配确实经由 vm_kern 的 kmem 子分配路径（而非 zone size-class 路径），且 guard 字段在该路径上被填充与检查；**不覆盖**——它不检查对象内容、不设边界页，也不覆盖 zone 侧的 chunk 布局问题。

一句话收口这两个 "guard"：**chunk 尾的保护页管"越出边界"，`kmem_guard_t` 管"分配项元数据被改"**。二者层次不同、失效模式不同，把它们合称为"内核有 guard"是本章最想纠正的表述。

## 5.5 kheap 分类与 typed kalloc

如果 size class 是 zone 分组的第一代依据，那么 **kheap 与 typed kalloc 就是第二代**：分组依据从"多大"变成"拿来干什么"。这一变化的安全意义在于——**跨类型挪用（把 A 类型的缓冲区当成 B 类型的对象来写）在物理上变得不可能**，因为两者根本不在同一个 zone、不共享同一批 chunk。

**kheap 分类。** 可核实的枚举成员包括 `KHEAP_ID_NONE`、`KHEAP_ID_EARLY`、`KHEAP_ID_DATA_BUFFERS`、`KHEAP_ID_DATA_SHARED`、`KHEAP_ID_KT_VAR`、`KHEAP_ID_COUNT`【一手-源码 zalloc.c:2449-2450、2861-2887、3738-3740】。该枚举决定 zone 归属哪个堆命名（zone 结构上的 `z_kheap_id`），对应关系可从命名直接读出：typed-kalloc 的**可变大小**分配走 `KHEAP_ID_KT_VAR`（即 5.1 表中的 `kalloc.type.var.<size>`），**数据缓冲区**走 `KHEAP_ID_DATA_BUFFERS`；`KHEAP_ID_EARLY` 对应启动早期，`KHEAP_ID_DATA_SHARED` 对应共享用途数据，`KHEAP_ID_NONE`/`KHEAP_ID_COUNT` 分别是"未归类"与枚举计数。堆名本身由 `kalloc_heap_names[]` 承载【一手-源码 zalloc.c:2450】。

**typed kalloc。** 机制侧一手事实：`z_kalloc_type` 标志、`kalloc_type_src_zone`/`kalloc_type_dst_zone` 字段，以及 panic 时输出类型统计的能力（见 5.6）【一手-源码 zalloc.c:454-456、494、695-711】。这套组合支持"**按 C++/类型自动分 zone**"（命名即 `kalloc.type.<size>`）：请求方（或编译器辅助路径）给出类型，分配器把同一 size 但不同类型的对象放进不同 zone。

简报对这一节的定性必须完整保留：**"kalloc 分配按用途分堆、名字可在观察工具中还原"是源码事实**；但 **iOS 生产构建的启用程度需以 config 为准，本次未逐条核实**，因此写作时必须加"源码机制"限定【简报 B3】。本篇遵循该限定：可以说"XNU 提供了按类型分 zone 的机制并有对应命名与观测字段"，不可以说"iOS 上所有内核分配都已按类型隔离"。

**四问**：**负责**——把"同大小但不同用途/类型"的对象物理隔离，从而消除跨类型挪用这一整类失效；**强制点**——zone 创建时依据 kheap id 与 `z_kalloc_type` 决定归属，分配时按类型选择 zone；**生效前提**——请求方确实以类型化接口申请（否则退化为 `kalloc.<size>`），且该平台的构建把 typed kalloc 纳入（未逐条核实）；**不覆盖**——同一类型内部的溢出仍不受影响（那仍是 5.2/5.3 的 guard page 与 OOB 调整域），且它不验证"这个指针本该指向哪种类型"，隔离只在分配容器层级成立。

**一个容易被忽略的连带效果：分组信息天然构成审计面。** 由于堆名与 zone 名可还原（`zone_name()`、`zone_heap_name()`、`MAX_ZONE_NAME`【一手-源码 zalloc.c:2440、2450】），内核自己就维护了一份"什么类型占了哪块内存"的账。5.6 会说明这份账在可观测性与失效分析上的两种用法，以及它在外部工具上近年收紧的情况。

## 5.6 可观测性、失效模式与 Apple 的公开自述

分配器加固的最终产出之一是**可观测性**：内核自己知道每个 zone 叫什么、属于哪个堆、包含哪些类型。这条能力对防御方（尤其是崩溃分析）价值很高，但它同时也是历史上被反复收紧的一类面。

**内核内部：zone/heap 命名可还原。** `zone_name()`、`zone_heap_name()`、`MAX_ZONE_NAME` 与 `kalloc_heap_names[]` 说明每个 zone/堆都有可读名称【一手-源码 zalloc.c:2440、2450】，5.1 的 `kalloc.<size>` 系列命名正是靠这套机制落地。

**panic 输出可携带类型统计。** `panic_include_kalloc_types` 默认值为 `false`（`zalloc.c:454`），在 `:696`、`:710` 处被置 `true`，并在 `:7790` 处决定是否输出形如 `"kalloc %s in zone, %s (%s):\n"` 的类型名列表（相关代码 `:7555-7568`）【一手-源码 zalloc.c:454,696,710,7555-7568,7790】。这是"默认关、特定条件下开"的典型形态，其后果是：**崩溃报告里是否包含 kalloc 类型分布，取决于该开关当时是否为真**，因此分析人员不能把"报告里没有类型统计"读成"分配器没有类型信息"。该条目与本报告崩溃可观测性章节的核实结论一致，此处按交叉引用处理，不重复展开【交叉引用：简报 §B14 `panic_include_kalloc_types`】。

**外部可观测性的历史要按限定语写。** zone 名称曾可经 `zone_info` MIG 接口或 `zprint` 一类工具查询，**近年已收紧**；简报明确定性：这属于"源码 + 历史工具事实，**非官方安全文档**"，须标注为源码与研究观察【简报 B3、E.5】。因此本篇的写法是：内核内部维护完整命名与统计能力（一手），对外暴露面已收窄（源码+工具史实，二手性质），二者不可混为"iOS 提供了 zone 枚举接口"。

**Apple 关于硬化分配器的公开自述。** 可直接点名的入口是 security.apple.com 的 XNU memory safety 系列一文 *"What if we had the SockPuppet vulnerability in iOS 16?"*（2023-05-23），其中 Apple 自述 hardened kernel allocator 用于对抗真实利用【一手-文档 https://security.apple.com/blog/what-if-we-had-sockpuppet-in-ios16/ 】。**本次核查仅确认该文的可达性与标题，正文细节未逐段引用**，因此本篇只把它作为"Apple 公开承认硬化分配器面向真实利用场景"这一层面的依据，不复述其中的对比数据或结论段落；任何要引用具体段落的场合需先补核【简报 B3、D】。同一入口也说明：提到历史漏洞时，本篇的边界就是"Apple 公告/博客层面的组件名与影响摘要"，不进入利用细节。

**失效模式一览**（防御视角的负面边界，逐条对应上文一手事实）：

| 失效模式 | 成因（一手依据） | 审计时应问什么 |
|---|---|---|
| 同 chunk 内跨对象越界不触发保护故障 | guard page 位于 chunk 尾部（`zalloc.c:194`） | 该 zone 是否为 typed/tagged 形态，越界是否落在同 zone 内 |
| 大尺寸分配不受益 size-class 加固 | `ZONE_MAX_ALLOC_SIZE` = `32*1024`（`zalloc.c:158`） | 目标对象的实际请求尺寸是否超出 zone 上限 |
| 缓解被当成修复 | `SAD_FENG_SHUI` 类配置改变布局而不阻止溢出 | 该配置降低的是"收益"还是"发生概率" |
| 类型隔离未覆盖全部路径 | typed kalloc 为"源码机制"，iOS 生产启用程度未逐条核实 | 该请求方是否以类型化接口申请 |
| kmem 侧校验是否命中不可判 | `kmem_allocate()` 函数体不在本地节选内（2.3、5.4） | 需引上游完整 `vm_kern.c` 后再定调用位置 |
| 崩溃报告缺类型统计 ≠ 无类型信息 | `panic_include_kalloc_types` 默认 `false`（`zalloc.c:454`） | 该次崩溃时开关取值与触发路径 |

**本章口径边界**：5.1 的 kalloc/zone 归属与四种命名、5.2 的 `zone_map_range`/`from_zone_map()`/chunk 尾 guard page/`zm_oob_offs`、5.3 的全部开关名（`ZSECURITY_CONFIG(ZONE_TAGGING/READ_ONLY/SAD_FENG_SHUI/PGZ_OOB_ADJUST)`、`SECURE_KERNEL`、`ZONE_MAX_ALLOC_SIZE`、`ZONE_MAX_CHUNK_ALLOC_NUM`、`kalloc_is_restricted_data_mode_enforced()`）、5.4 的 `kmem_guard_t`/`kmem_entry_validate_guard`、5.5 的 kheap 枚举与 `z_kalloc_type`、5.6 的 `panic_include_kalloc_types` 行为，均为**一手-源码**（宏名与枚举名逐个 grep 比对过；行号以本地镜像 tree sha `f6217f8` / 上游 `main` 为准，历史版本行号不同）。需限定语的有四处：各安全配置在零售 iOS 构建中的**取值**（未逐条锚定）、typed kalloc 的**生产启用程度**（"源码机制"）、zone 名外部可枚举性的**历史**（源码+研究观察，非官方文档）、Apple 博客的**细节段落**（仅确认可达性与标题）。缺口一处：`kmem_allocate()` 函数体（本地节选所致，非事实缺失）。本章不描述任何分配器缺陷的利用方式。

---
# 强制访问控制框架：MACF 的位置与边界

## 6.1 框架血统与开源边界

强制访问控制（Mandatory Access Control，MAC）在 XNU 中以一个名为 MACF（Mandatory Access Control Framework）的分发框架形式存在。讨论它之前必须先把两件事分开：**框架**与**策略**。框架决定「在哪些内核路径上存在强制点、如何把强制点分发给已登记的策略模块」；策略决定「这一笔访问到底放行还是拒绝」。本章的主体是前者，而后者恰恰是 Apple 公开材料中缺失的那一半——这个不对称，决定了本章所有结论的证据上限。

框架血统是可直接从源码头部注释读出的事实。XNU 开源树 `security/` 目录下的 `mac_policy.h` 保留了 FreeBSD 的 CVS 标签行：`FreeBSD: src/sys/sys/mac_policy.h,v 1.39 2003`，并在紧邻的注释中自述为 "TrustedBSD MAC Framwork on Darwin"（引号内为注释原文，含原文中的 "Framwork" 拼写）【一手-源码 src/mac_policy.h:65、:73】。这句话同时给出了两个口径：其一，MACF 承自 TrustedBSD 项目，是 FreeBSD 安全子系统血统的软件在 Darwin 上的移植；其二，它被定位为 "on Darwin" 的框架实现，而非 Apple 另起炉灶的独立设计。

由此产生一个常见误解，需要正面校正：**MACF 不是一个独立的开源项目，也不存在独立于 XNU 的发布物**。核实方式是目录级枚举而非措辞推断——框架代码全部位于 XNU 仓库的 `security/` 目录内（`mac_base.c`、`mac_vfs.c`、`mac_proc` 系列等共 27 个 `mac_*.c` 文件加上 `mac_policy.h`，经 GitHub 全量 trees API 递归枚举确认），随 XNU 整体以 APSL 2.0 许可开源，仓库另有根级 `APPLE_LICENSE` 文件【一手-源码 上游 https://github.com/apple-oss-distributions/xnu，本地镜像 `src/`，tree sha f6217f8】。BSD 子系统中原本可能容纳策略的位置 `bsd/security/` 如今只剩 `audit/` 目录，**没有任何策略模块的 `.c` 文件**。换言之，「苹果有没有单独开源 MACF」这个问题本身就是伪命题：它在源码树里的唯一存在形式，就是 XNU 的一部分。

MACF 在 XNU 分层中的归属也需要用一手的目录职责口径来锚定，而不能用宣传性定语。README 自述 `security` 目录为 "Mandatory Access Check policy interfaces"，`bsd` 目录为 "BSD subsystems code"【一手-源码 src/README.md「The XNU Source Tree」节】——即框架接口层独立成目录、位于 BSD 语义之上，而不在 Mach 核心（`osfmk`）之内。这与第 3 章的分层描述一致，也提示代码签名的强制链天然跨层（BSD 的 exec 路径 → `security/` 的 MACF 调用点 → AMFI 接口 → pmap/CSM），不可写成单层独占。

一个常被忽略的事实是 MACF 属于**可按构建裁剪**的组件：内核调用点封装在 `security/mac_*.c`，通过 `optional config_macf` 这类编译开关决定是否纳入构建（`security/conf/files`）【一手-源码 xnu security/conf/files】。这构成 MACF 的「生效前提」维度——若某构建未启用该开关，框架连分发骨架都不存在。

| 维度 | 本章可核实的口径 | 依据 |
| --- | --- | --- |
| 负责什么 | 策略登记、hook 分发、强制点骨架；不做任何具体放行判定 | 【一手-源码 src/mac_policy.h】 |
| 强制点位置 | XNU 树 `security/` 目录（27 个 `mac_*.c` + `mac_policy.h`），BSD 层之上 | 【一手-源码 src/README.md 目录职责节】 |
| 生效前提 | 构建经 `config_macf` 类开关纳入框架；有策略模块完成注册 | 【一手-源码 security/conf/files】 |
| 不覆盖什么 | 不含任何策略模块实现；`bsd/security/` 现仅剩 `audit/` | 【一手-源码 目录枚举（负面结果）】 |

行号口径提醒：本章源码引用以本地镜像所对应的上游 main 分支提交（tree sha f6217f8）为准，历史 xnu 发布标签的行号会不同；引用具体行号时应理解为「该镜像中的位置」而非跨版本的稳定编号。

## 6.2 注册机制与 ops 表结构

强制访问控制的核心数据结构是一张**按对象域切分的函数指针表**（policy operations table），策略模块通过注册接口把它交给框架。注册入口是 `mac_policy_register(struct mac_policy_conf*)`【一手-源码 src/mac_policy.h:6406】；策略也可以在镜像里以声明式宏 `MAC_POLICY_SET()` 静态落位【同上 :6565】。两者构成两条注册路径：静态声明用于随镜像装载即完成登记，运行时注册则把登记动作推迟到模块启动阶段。框架与策略之间的接口兼容性由一个显式版本号锚定：`MAC_POLICY_OPS_VERSION 91`【同上 :5966】——ops 表条目集合变动时该版本号随之变更，注册时不匹配即不兼容。

命名约定是这套机制里最具信息量的部分。条目按 `mpo_<object>_check_<operation>` 三段式命名【一手-源码 src/mac_policy.h:158-162】，例如 `mpo_vnode_check_exec`、`mpo_iokit_check_open`。`mpo_` 前缀标识「策略操作」条目，`<object>` 段对应被保护的内核对象域（vnode、file、process、task、iokit 等），`<operation>` 段对应具体动作。头文件以 "hook" 措辞描述这些条目位置，全文约 46 处【同上，`mac_policy.h` 计数】——框架对自身的描述就是「在既定内核位置挂接外部代码」，而不是「实现某种安全语义」。

这套结构在审计视角下的价值与代价同样明确。价值在于**强制点候选位置可枚举**：拿到 `mac_policy.h` 就能列出内核愿意在哪些对象与操作上接受外部判定，再回查 `security/mac_*.c` 中的对应封装，确认某条内核路径（如 vnode 打开、IOService 打开）是否真的存在 MAC 强制点。代价在于**可枚举不等于已覆盖**：ops 条目集合表示「内核愿意问」，实际「谁在答」取决于是否有策略订阅该条目（见 6.3），而 iOS 上注册了哪些策略属于未公开范围（见 6.6）。

| 接口要素 | 实名 | 位置 | 审计含义 |
| --- | --- | --- | --- |
| 运行时注册 | `mac_policy_register(struct mac_policy_conf*)` | `mac_policy.h:6406` | 调用方与调用时刻决定策略生效时机 |
| 声明式登记 | `MAC_POLICY_SET()` | `mac_policy.h:6565` | 随镜像落位的静态登记形式 |
| 接口版本 | `MAC_POLICY_OPS_VERSION 91` | `mac_policy.h:5966` | 策略与框架必须版本匹配 |
| 条目命名 | `mpo_<object>_check_<operation>` | `mac_policy.h:158-162` | 强制点可按对象域系统枚 |
| hook 措辞密度 | 约 46 处 "hook" | `mac_policy.h` 全文 | 框架自述为挂接层而非判定层 |

按「负责什么／强制点在内核何处／生效前提／不覆盖什么」四问落位：注册机制负责**登记与版本校验**；分发实现位于 `security/mac_base.c` 等框架文件，具体强制点则由各对象域的 `security/mac_<object>.c` 承载；生效前提是策略提供与 `MAC_POLICY_OPS_VERSION` 匹配的 ops 表并完成注册；它**不覆盖判定规则本身**，也不覆盖策略之间的相对优先次序——后者在本次核实的一手条目中没有可引用的 Apple 明文表述，故不作推断。

## 6.3 兴趣点订阅与 hook 跳过语义

MACF 不是「所有策略在所有检查点都被叫到」的广播结构，而是**按条目订阅**的结构。`mac_policy.h` 在描述 ops 表与设计约定时给出了一条关键语义：**未声明相应兴趣点（interest）的策略，在该检查入口会被直接跳过**【一手-源码 src/mac_policy.h:73-77（其中 :75-77 为跳过语义的原文表述）】。这条语义与 6.2 的命名约定配套：框架侧以 `mpo_<object>_check_<operation>` 划出条目，策略侧只填自己关心的条目，其余留空；框架在遍历已注册策略执行某一检查时，对未填该条目的策略不再调用【同上 :158-162，命名约定与 `mpo_vnode_check_exec` 示例】。「策略按需订阅 hook」因此不是外部研究者的推测，而是框架头文件中的官方设计声明。

这条语义有三层可审计的后果，也有一个必须留白的边界。

其一，**强制点密度由订阅集合决定，而非由框架条目总数决定**。`mac_policy.h` 声明的条目集合只是「内核愿意问」的候选；某条内核路径是否真的被某个策略约束，还要看该策略是否在 ops 表里填了相应条目。对 iOS 而言，这一层恰恰不可核实——策略模块本体闭源（见 6.6），既无法从开源树读出订阅集合，也没有官方清单可查。

其二，**新增内核路径不会自动获得 MAC 保护**。要让某条新路径可被策略约束，必须同时满足两个条件：框架侧存在对应的 ops 条目，且内核代码在正确位置调用了对应 `security/mac_<object>.c` 封装。缺任一条件，订阅就无从谈起。这正是第 9 章 IOUserClient 边界上「框架提供机制、调用方承担责任」同一逻辑在 MAC 层的表现。

其三，**跳过语义带来性能收益，也带来「静默不覆盖」的形态**。跳过未订阅策略意味着常规路径上只有少数策略被调用，代价低；反面是：某个对象域的某个操作没有任何策略订阅时，运行时不会报错、不会告警，检查入口照常走完然后返回——即「没被约束」与「被约束后放行」在可观测性上难以区分。这一点决定了仅靠动态观察无法证明覆盖，必须回到静态的调用点枚举与已注册策略的 ops 表内容。

必须留白的边界：框架在「所有已订阅策略都返回成功」与「没有任何策略订阅某条目」两种情形下如何汇总最终判定值，本次核实的一手条目中没有可直接引用的 Apple 明文。因此正文不断言 MACF 的具体返回值聚合规则，只陈述已核实的订阅与跳过语义。

| 问题 | 已核实的回答 | 依据 |
| --- | --- | --- |
| 负责什么 | 只调用订阅了该条目的策略，其余跳过 | 【一手-源码 `mac_policy.h:73-77`】 |
| 强制点在内核何处 | 各对象域 `security/mac_<object>.c` 的封装调用点 | 【一手-源码 `security/` 目录枚举】 |
| 生效前提 | 框架有对应 ops 条目 + 内核代码调用封装 + 策略订阅 | 【一手-源码 `mac_policy.h:158-162`】 |
| 不覆盖什么 | 未订阅的条目不产生任何判定；判定值聚合规则本次未核实 | 【缺口：未获得可引用口径】 |

## 6.4 「非公开 KPI」与 mac_ 前缀的三层内部接口

关于 MACF 最重要的一句官方自述，不在任何开发者文档站上，而在头文件里以编译警告的形式存在：

```
#warning "MAC policy is not KPI, see Technical Q&A QA1574"
```

【一手-源码 src/mac_policy.h:79-81】。这句话的证据价值高于普通的文档表态：它是 Apple 写进代码、要求编译期可见的自我定位——**MAC 策略接口不是 KPI（Kernel Programming Interface）**，即不属于 Apple 承诺对外稳定、可供第三方内核代码依赖的公开内核接口。据此可以直接否定一类常见误写：把 MACF 描述成「开发者可以对接、查询或扩展的公开内核框架」。它不是；任何第三方代码对它的依赖都不受兼容性承诺保护，这也意味着 Apple 可以在不破坏任何公开契约的前提下重构 ops 表条目——`MAC_POLICY_OPS_VERSION 91` 这个显式版本号【同上 :5966】正是这种内部演进的护栏与痕迹。

由此引出「mac_ 前缀」这个词在报告写作中的歧义，必须拆成三层内核内部接口来表述，不能笼统写成「mac_ 前缀 API」：

| 层 | 前缀/形式 | 所在位置 | 面向对象 |
| --- | --- | --- | --- |
| 策略实现层 | `mpo_<object>_check_<operation>` | ops 表条目（`mac_policy.h:158-162`） | 策略模块自身需实现的回调 |
| 框架封装层 | `mac_*` 函数与 `security/mac_<object>.c` 文件 | 内核调用点封装 | 内核子系统调用框架的入口 |
| 分发宏层 | `MPC_*` 封装 | 框架内部把检查分发给已注册策略 | 内核内部 |

三层全在内核内部，**用户态没有公开的 MAC API**——QA1574 警告本身就是这条结论的一手证据【一手-源码 src/mac_policy.h:79-81】。本报告因此不引用任何用户态 `mac_*` 调用形式作为机制依据；本次核实也未获得 Apple 关于用户态 MAC 接口的可引用文档条目，相关历史接口问题一律留白而不作断言。

从加固视角看，「非 KPI」这一属性有两面。对平台方是自由：强制点集合可按构建裁剪（`config_macf`，见 6.1），条目可按版本增删，无需通知外部。对审计方是约束：**不能把「MACF 存在」当作「某策略可被外部对接」的前提**，也不能假设某个 `mpo_` 条目在下一版本仍保持相同签名或语义。凡是依赖 MACF 内部结构得出的结论，都必须同时给出源码行号级依据，并注明它属于「随 XNU 开源的内部实现」而非「对外承诺的接口」。

## 6.5 对象域覆盖：VFS、exec 与 IOKit

框架侧的对象域划分可以直接从 `security/` 目录的文件名读出：`mac_vfs.c`、`mac_file.c`、`mac_process.c` 等封装文件分别对应文件系统级、文件句柄级与进程级的检查入口【一手-源码 xnu `security/` 目录，27 个 `mac_*.c` 经全量 trees 枚举确认】。就与 BSD 传统权限检查（DAC，即属主/模式位那一套）的关系而言，可核实的是**调用点存在且落在 VFS、exec 与 IOKit 路径上**，封装函数在这些路径上把检查交给已注册策略；至于「DAC 先于 MAC」这一先后次序，本报告按**源码结构层面的观察**表述，不把它写成 Apple 官方文档的结论——该条在第 2 章的证据分级体系里属「部分核实」，其依据是源码调用结构而非任何明文声明。

值得注意的是 IOKit 也被 MACF 覆盖，而且覆盖粒度相当具体。`mac_policy.h` 在 iokit 对象域声明了一组条目【一手-源码 src/mac_policy.h:1099-1149】：

| ops 条目 | 被约束的用户态动作 |
| --- | --- |
| `mpo_iokit_check_open_t` | 打开（连接）某个 IOService 的用户客户端 |
| `mpo_iokit_check_open_service_t` | 针对特定 service 对象的打开检查 |
| `mpo_iokit_check_set_properties_t` | 写入 IORegistry 属性 |
| `mpo_iokit_check_get_property_t` | 读取 IORegistry 属性 |

框架侧对应的实现文件是 `security/mac_iokit.c`【同上】。把这张表和第 9 章的 IOUserClient 派发路径合起来读，结论是明确的：**IOServiceOpen 与 IORegistry 属性读写都要经过 MAC 强制点**，沙箱对 IOKit 访问的限制在机制层面正是经由这条通道实现的。但必须把「机制」与「规则」分开标注：机制层面可一手核实（框架条目与调用点开源可见），**具体规则属闭源策略**，本次无任何一手条目可支撑对规则内容的描述。

| 维度 | 口径 |
| --- | --- |
| 负责什么 | 在 VFS/文件/进程/IOKit 等对象域上提供统一的检查分发入口 |
| 强制点在内核何处 | 各对象封装文件（`mac_vfs.c`、`mac_file.c`、`mac_process.c`、`mac_iokit.c`）内的 `MPC_*` 分发点 |
| 生效前提 | 相应 ops 条目存在且被某策略订阅；构建纳入 MACF |
| 不覆盖什么 | 不覆盖未被框架调用点触及的内核路径；不公布任何已订阅规则的内容 |

这也解释了为什么「IOKit 用户客户端边界」（第 9 章）与「MACF 覆盖 IOKit」（本章）必须一起读：前者描述用户态如何进入驱动方法，后者描述进入前后存在哪些强制点。两者的交叠面，正是 iOS 上「非特权进程能打开哪些服务、能读写哪些 IORegistry 属性」这个问题的机制骨架——而骨架之上的判定策略，本章无法给出一手内容。

exec 路径同样在这张地图上。按 6.2 的命名约定，exec 时刻的 vnode 级检查对应 `mpo_vnode_check_exec` 这一条目【一手-源码 src/mac_policy.h:158-162】；它落在 `mac_vfs.c`/`mac_file.c` 所在的文件对象域，而不是 `mac_process.c` 所在的进程域。这一区分有实际的审计意义：**exec 的强制点是「是否允许把这个 vnode 当作可执行映像加载」，而进程域的强制点是「是否允许对已有进程做某类操作」**，两者属于不同条目、可被不同策略订阅，不能因为都在「进程生命周期」上就合并描述。把三个对象域与用户态动作对应起来：

| 对象域 | 框架封装文件 | 典型可被约束的用户态动作 |
| --- | --- | --- |
| vnode / file | `mac_vfs.c`、`mac_file.c` | 打开、读写、创建、把 vnode 作为映像 exec |
| process | `mac_process.c` | 对既有进程的操作类访问 |
| iokit | `mac_iokit.c` | 打开 IOService 用户客户端、读写 IORegistry 属性 |

需要提醒的是，这张表只到「对象域—封装文件—动作类别」这一层，具体每个域声明了多少条目、条目签名如何，本次未逐条核对（`mac_policy.h` 全文 6000 余行，本章只在 6.2、6.5 给出带行号的抽样位置）；表中的框架文件名以 `security/` 目录枚举与本章点名为限，不外推。

## 6.6 误解校正与本章口径边界：策略清单为何是缺口

本章最大的一处缺口，恰好是读者最想要的那份材料：**iOS 上启用了哪些 MAC 策略模块，官方从未公布**。缺口的性质需要精确表述，不能滑向两个方向的误读。

先说可核实的部分。开源树内**策略模块实现的数量是零**——`security/` 目录只有框架与各对象域的调用点封装，`bsd/security/` 下只剩 `audit/`，没有任何策略 `.c` 文件（6.1 已给目录级证据）。而注册接口 `mac_policy_register()` 明确存在且可被运行时调用（6.2）。两件事合起来的可核实结论是：**iOS 上的 MAC 策略必然由开源树之外的组件在运行时登记**，其代码不随 XNU 公开。哪些组件在做这件事，公开研究（Jonathan Levin《*OS Internals: Security & Insecurity》、针对 kernelcache 的符号级观察）常列出 sandbox 与 amfi 两个注册策略【公开研究 二级来源】，但这只能作为二级证据引用，**禁止写成 Apple 官方口径**；Sandbox.kext 与 AppleMobileFileIntegrity 的本体源码均不公开，其内部规则不得写实。

据此做两处误解校正。

**其一：「可以列出一份官方发布的 iOS 已启用 MAC 策略清单」——不成立。** Apple 没有任何官方文档公布 iOS 的启用模块列表；这条预期在本报告的证据分级中属于「需推翻」一类，其核实结果是：iOS 上 MACF 已注册策略模块的官方清单（含 Sandbox/AMFI 的具体注册名）本次未获得任何可核实条目。正确写法是把模块名标注为**符号级/研究级证据**，并说明官方清单不存在；同时也不得反过来断言「不存在这样的清单说明 iOS 上没有 MAC 策略」——`mac_policy_register()` 的存在，加上 `mpo_iokit_*`、`mpo_vnode_check_exec` 这类框架条目与各对象域封装文件的实际存在，恰恰证明框架在被使用。

**其二：「沙箱容器就是一个 MACF 策略模块」——该表述在 Apple 官方文档中不存在。** 目前只有一条源码旁证可以支撑其机制关联：上游 `bsd/kern/ubc_subr.c:4833` 使用了 `MAC_VNODE_CHECK_DYLD_SIM` 标志，即 dyld shared cache 相关的模拟检查是经 MACF 通道问出去的【一手-源码】。这足以说明「沙箱语义与 MACF 调用点存在实际交集」，但不足以支撑「容器 = 策略模块」这种结构性论断。因此正文把它写成**源码观察**，并给出可核实的范围：能从源码证明的是框架条目与调用点存在，不能证明的是订阅它们的策略内部如何组织「容器」这一概念。

| 常见写法 | 校正后写法 | 最高可用证据层级 |
| --- | --- | --- |
| 「Apple 官方公布的 iOS MAC 策略清单为 sandbox、amfi」 | iOS 启用模块清单官方从未公布；sandbox/amfi 两个注册策略来自符号级与文献级观察 | 【公开研究 二级】 |
| 「沙箱容器即 MACF 策略模块」 | 沙箱语义与 MACF 调用点存在交集，可由 `MAC_VNODE_CHECK_DYLD_SIM` 使用点旁证 | 【一手-源码 ubc_subr.c:4833】 |
| 「MACF 是可用于第三方对接的公开 KPI」 | Apple 在 `mac_policy.h` 中以 `#warning` 自述 MAC policy 非 KPI（QA1574） | 【一手-源码 mac_policy.h:79-81】 |
| 「MACF 是独立开源项目」 | 框架仅存在于 XNU 树 `security/` 内，随 XNU 以 APSL 开源，无独立发布物 | 【一手-源码 目录枚举 + README】 |

在清单不可得的前提下，仍然可做的是**框架层审计**，而且方法完全是静态的：先用 `mac_policy.h` 枚举候选条目集合，再在 `security/mac_<object>.c` 中确认某条内核路径是否真的调用了对应封装，最后按 6.3 的订阅语义把「调用点存在」与「有策略订阅」当作两个独立结论对待——三者中任何一步都可以用开源材料完成，也都不能替代后一步。反过来，凡是试图用动态观察（例如观察某次访问被拒）来反推「iOS 上注册了哪些策略」的做法，只能得到样本级的证据，不能得到清单级结论。

**本章口径边界。** 可以把本章内容当作事实使用的部分，全部落在框架层：血统（TrustedBSD，保留 FreeBSD CVS 标签）、位置（XNU 树 `security/`，非独立项目）、注册与分发机制（`mac_policy_register`、`MAC_POLICY_SET()`、`MAC_POLICY_OPS_VERSION 91`、`mpo_<object>_check_<operation>` 命名）、订阅与跳过语义、非 KPI 的自我定位、以及包括 IOKit 在内的对象域覆盖。必须降级处理的部分有三类：策略模块清单（研究/符号级）、策略规则内容（完全不可写实）、判定值聚合与策略间优先次序（本次未核实，正文不作断言）。一句话概括本章可安全引用的口径：**框架开源、策略闭源**——凡是越过这条线去描述「iOS 上 MAC 具体拦了什么」的表述，都不属于本报告可核实的范围。

---

# AMFI：代码签名与 entitlement 的执行主体

## 7.1 定性结论：内核扩展为主体，同名守护进程并存

AppleMobileFileIntegrity（AMFI）的定性必须是**内核态组件**，而这一结论的证据不是社区惯例，是 Apple 自己的措辞。平台安全指南在解释 `research.` 前缀 entitlement 时的原文是：

> "The research. namespace is respected only by the RESEARCH variant of the **AppleMobileFileIntegrity kernel extension**; any process with this entitlement is terminated on a customer device during signature validation."

【一手-文档 指南p.85】。这里 Apple 把 AMFI 明确称为 **kernel extension**，即运行于内核态的扩展，并且把它置于「signature validation」这一执行时刻的主语位置——签名校验阶段的终止动作由这个内核扩展做出。据此，本报告采用如下定性硬结论：**AMFI 的执行主体在内核内部**。

同时，用户态确实存在一个同名守护进程：`/usr/libexec/amfid`，由 LaunchDaemon `com.apple.amfid` 拉起。关于它的现代职责，**Apple 没有任何公开文档描述**——这是缺口而非争议：「amfid 在当前 iOS 上的具体职责、其 plist 载体的官方描述」在本次核查中未获得任何可核实条目。公开研究文献（iOS Hacker's Handbook、exploitreversing 系列第 04 篇等）普遍把 AMFI 描述为「kernel component + daemon」两层结构，并在早期 iOS（iOS 10 之前）语境里把部分签名判定归给 amfid【公开研究 二级来源】。

因此这一节要做的误解校正是：**「AMFI 是一个用户态守护进程」这种单一化表述不成立**。正确的分层写法是按证据等级拆开的两句——主体是内核内的组件（一手文档定性 + 一手源码接口，见 7.2），另有同名用户态守护进程 `amfid` 存在并据公开研究参与部分策略评估，其后者的现代职责缺乏官方口径。把「kernel component + daemon」整体当作 Apple 官方说法，是把公开研究共识误挂名为一手结论；把它整体当作事实断言，同样是越级。

| 组成部分 | 定性 | 最高可用证据层级 |
| --- | --- | --- |
| AMFI 内核实现 | Apple 称 "AppleMobileFileIntegrity kernel extension" | 【一手-文档 指南p.85】 |
| 内核调用 AMFI 的通道 | 只读函数表 + 一次性注册接口 | 【一手-源码 libkern/libkern/amfi/amfi.h、libkern/amfi/amfi.c】 |
| `/usr/libexec/amfid` | 守护进程存在（LaunchDaemon `com.apple.amfid`）；职责无官方文档 | 【公开研究/文件系统观察】 |
| 「AMFI = 内核 + amfid 双层」 | 公开研究共识，非 Apple 文档表述 | 【公开研究 二级】 |

按四问落位：AMFI 负责**代码签名与 entitlement 相关的判定与授权语义**（具体规则内容不公开，见 7.6）；强制点位于内核——签名校验阶段与内核经函数表发起的调用；生效前提是对应内核扩展已加载并完成接口注册；不覆盖的是**未被内核调用点触及的路径**，也不意味着用户态 amfid 可以替代内核内判定。

## 7.2 内核侧接口：amfi_t 只读函数表与一次性注册

「AMFI 主体在内核」这句话在源码里有非常具体的结构支撑。`libkern/libkern/amfi/amfi.h` 定义了接口版本号 `KERN_AMFI_INTERFACE_VERSION 7`【一手-源码 src/amfi.h:38，本地镜像与上游该文件逐行一致】，并声明了一张供内核其他部分消费的函数表 `amfi_t`【同上 :245-265】与注册函数 `amfi_interface_register()`【同上 :294】。注册侧的实现位于 `libkern/amfi/amfi.c`：全局符号以只读晚期常量形式存在——`SECURITY_READ_ONLY_LATE(const amfi_t*) amfi`；而注册具有**一次性**语义，重复注册直接 panic，原文为 `panic("AppleMobileFileIntegrity interface already set")`【一手-源码 libkern/amfi/amfi.c，经上游 raw 核实】。

这几行代码透露的信息量比大多数文档更大，可以拆成四点读：

其一，**内核不直接调用 AMFI 的函数符号，而是经一张函数表间接调用**。`amfi` 是指针，实体由注册方提供，因此「谁实现这些判定」与「谁使用这些判定」在链接期就是解耦的——这正好解释了 7.1 的定性：判定逻辑不在开源树里，开源树里只有它对外暴露的形状。

其二，**`SECURITY_READ_ONLY_LATE` 表明该指针在启动早期写定之后即被转为只读**。这与内核把易被篡改的间接调用目标做「晚期只读化」的一般加固取向一致；对审计者而言，这意味着该表指针不是运行期可随意改写的配置项，任何指向它的改动都要先突破只读保护。

其三，**只允许一个实现者**。重复注册即 panic 的语义意味着内核内不存在「多个 AMFI 实现叠加」的形态，也不允许后来者替换既有实现；从可用性视角看这是一种 fail-closed 的设计取向（注册冲突直接终止系统而非静默降级）。

其四，**函数表就是接口清单**。`amfi_t` 的条目集合（包括 7.3 要谈的 entitlement 查询入口）界定了内核其他子系统能够向 AMFI 提出哪些请求，这是可一手枚举的；反过来，条目背后的实现细节一律不可写实。

版本号 `KERN_AMFI_INTERFACE_VERSION 7` 的审计价值容易被忽略，这里补一句：它是接口层的**兼容性护栏**。消费者与实现方必须对同一版本达成一致，否则注册即不成立；对分析者来说，版本号递增意味着函数表条目集合发生过多次扩充，因此**跨 xnu 版本比较 `amfi_t` 条目时必须先看版本号**，不能假设条目集合稳定。这与第 6 章 `MAC_POLICY_OPS_VERSION 91` 的作用同构，也是「AMFI 属于内核内组件而非外部服务」的又一处结构性痕迹。

| 维度 | 口径 |
| --- | --- |
| 负责什么 | 提供内核内消费 AMFI 能力的唯一入口点与版本护栏 |
| 强制点在内核何处 | `libkern/libkern/amfi/amfi.h` 声明、`libkern/amfi/amfi.c` 注册；消费者经 `amfi->…` 调用 |
| 生效前提 | 实现方在早期完成一次性注册；接口版本与消费者匹配 |
| 不覆盖什么 | 不覆盖判定规则的实现位置与内容；不覆盖用户态 amfid 的行为 |

| 符号/常量 | 语义 | 位置 |
| --- | --- | --- |
| `KERN_AMFI_INTERFACE_VERSION 7` | 接口版本，约束实现与消费者匹配 | `src/amfi.h:38` |
| `amfi_t` | 内核侧可见的 AMFI 函数表 | `src/amfi.h:245-265` |
| `amfi_interface_register()` | 由实现方登记函数表 | `src/amfi.h:294` |
| `SECURITY_READ_ONLY_LATE(const amfi_t*) amfi` | 全局只读晚期指针 | `libkern/amfi/amfi.c` |
| `panic("…interface already set")` | 重复注册即终止 | `libkern/amfi/amfi.c` |

## 7.3 与 CoreEntitlements V2 的衔接

`amfi_t` 函数表里最值得单列出来的一组条目，是 entitlement 的查询通道。`src/amfi.h` 在头部即引入 CoreEntitlements 相关内核接口（:37-38 的 include）【一手-源码 src/amfi.h:37-38】，函数表内则包含 `OSEntitlements_query` 一类的查询入口【同上 :245-253】。含义是：**entitlement 的内核内读取并非由 AMFI 自己解析签名里的字符串，而是经 CoreEntitlements V2 这套结构化 API 完成**，AMFI 在其中充当提供该 API 的一方，内核其他子系统通过 `amfi->OSEntitlements.*` 消费它。

这条通道有一个可以直接落到具体调用点的例子，即第 9 章要展开的 IOUserClient 路径。`iokit/Kernel/IOUserClient.cpp` 在把 entitlement 交给驱动之前，先做了框架自身的入参校验——原文形如 `/* Validate input arguments */ if (task == kernel_task || entitlement == NULL) return NULL;`——随后经 `amfi->OSEntitlements.copyEntitlementAsOSObjectWithProc` 取出 entitlement 对象【一手-源码 src/IOUserClient.cpp:1584-1595】。这一段同时是 7.2 与本章定性论断的交叉证据：**IOKit 核心与 AMFI 接口在用户客户端路径上直接耦合**，驱动拿到的授权语义是以对象形式从 AMFI 提供的通道里取出来的，而不是从调用方自报的属性里读出来的。

在整体强制链里，这条衔接的位置是这样刻画的。exec 阶段内核从签名的内嵌 blob 中提取 entitlement（`bsd/kern/ubc_subr.c` 的 `csb_*` 字段，如 `csb_platform_binary`、`csb_teamid`，见 8.5）；「该进程是否真的有资格持有某条 entitlement、某条 entitlement 现在能不能兑现」的判定落在 AMFI 内核接口与 CoreEntitlements V2 之上；最终授权效果体现在 trust cache 命中与 platform privileges（该段原文为 Apple silicon Mac 语境，移用到 iOS 须加限定，见 8.5）；消费点则分散在各 MACF 调用点与 IOUC 的按方法授权检查里。entitlement 因此不是「二进制里写了就生效」的声明，而是一段需要被判定与授权的字符串——判定方正是 AMFI。

至于官方文档层面与 entitlement 相关的表述，可以引的一句是平台安全指南关于 `research.` 前缀的处理："any process with this entitlement is terminated on a customer device during signature validation"【一手-文档 指南p.85】——它同时说明 entitlement 判定发生在签名校验时刻、且由 AMFI 内核扩展执行（详见 7.5）。指南 p.119 另有 "Mandatory Access Controls (MACs) require code signing to enable entitlements protected by the system" 一句，但该句位于 **macOS 章节**，引用到 iOS 语境必须标注平台限定。

把这条衔接放在第 7 章而不是第 9 章来讲，理由在于它是**结构性的**：entitlement 查询能力被编进 `amfi_t` 函数表（作为 `OSEntitlements` 子结构存在），说明「读 entitlement」这件事在 XNU 的设计里就是 AMFI 提供的一项内核服务，而不是 IOKit 或 MACF 顺带实现的私有逻辑。由此可以给出两个可核实的观察与一个必须留白的边界。观察一：**这条通道是 entitlement 语义的内核内收口点**——所有消费方都经由同一个只读函数表取值，因此它成为整条授权链的关键依赖，而 `SECURITY_READ_ONLY_LATE` 与一次性注册正是围绕这一关键位置的设防措施（7.2）。观察二：CoreEntitlements V2 的价值在于把 entitlement 从「签名里的字符串」变成「内核内可结构化查询的对象」，这让消费点可以按名字取值、按进程取用，而不必各自解析签名。边界则是：**该 API 集合的完整条目与内部语义不公开**，本报告只引用 `amfi.h` 中点名的 `OSEntitlements_query` 与 IOUC 中点名的 `copyEntitlementAsOSObjectWithProc` 两处，不外推其余。

| 维度 | 口径 |
| --- | --- |
| 负责什么 | 让内核子系统以对象形式读取某进程被承认的 entitlement |
| 强制点在内核何处 | `amfi_t` 内的 `OSEntitlements` 子结构；消费示例在 `IOUserClient.cpp:1584-1595` |
| 生效前提 | AMFI 已注册；调用方提供可核验的 task/进程上下文（框架先做入参校验） |
| 不覆盖什么 | 不覆盖 entitlement 的签发条件与规则内容；API 完整条目不公开 |

## 7.4 配置载体：boot-args 实名与 CS_CONFIG_* 位图

AMFI 的运行形态不是纯粹静态的，它有一组来自 boot-args 的**豁免/放宽开关**。这些开关的名称、位值与去向在源码里逐条可核【一手-源码 src/kern_codesigning.c:219-315】：

| boot-args 实名 | 对应掩码位（如有） | 落入的内核配置位 |
| --- | --- | --- |
| `amfi` | 以掩码形式承载多项位 | 见下行各项 |
| `amfi_allow_any_signature` | `CS_AMFI_MASK_ALLOW_ANY_SIGNATURE 0x02` | `CS_CONFIG_ALLOW_ANY_SIGNATURE` |
| `amfi_get_out_of_my_way` | `CS_AMFI_MASK_GET_OUT_OF_MY_WAY 0x80` | `CS_CONFIG_GET_OUT_OF_MY_WAY` |
| `amfi_unrestrict_task_for_pid` | — | `CS_CONFIG_UNRESTRICTED_DEBUGGING` |
| `cs_enforcement_disable` | — | `CS_CONFIG_ENFORCEMENT_DISABLED` |
| `cs_integrity_skip` | — | `CS_CONFIG_INTEGRITY_SKIP` |
| `amfi_relax_profile_trust` | — | `CS_CONFIG_RELAX_PROFILE_TRUST` |
| `amfi_dev_mode_policy` | — | `CS_CONFIG_DEV_MODE_POLICY` |

`CS_CONFIG_*` 的完整枚举在 `osfmk/kern/code_signing.h`【一手-源码 src/code_signing.h:42-53，另见 :27 附近的 `CS_CONFIG_DEVELOPER_MODE_SUPPORTED` 位】，其中除上表来源外还包含 `CS_CONFIG_MAP_JIT`（:52）。把这张表合起来读，可核实的结构性结论是：**内核维护一个运行时的代码签名配置位图，由 AMFI 侧 boot-args 与硬件/构建形态共同填充**；MAP_JIT、开发者模式与锁定模式（见 8.6）都汇入这同一套配置，而不是各自另建通道。

必须强调的限定语，是这段解析代码的源码注释原文：**"We only ever need to parse for boot-args based exemption state on DEVELOPMENT"**【一手-源码 src/kern_codesigning.c:219】。也就是说，这些豁免**只在 DEVELOPMENT（以及 DEBUG）内核的解析路径中被读取，零售构建的 iOS 内核不认这些旁路**；生产设备上这些配置位默认为 0。在采用 SPTM/TXM 的形态下，这类豁免还会被监视器侧策略进一步削减，不随内核位图全量生效【一手-源码 kern_codesigning.c 中以 `#if DEVELOPMENT || DEBUG` 条件编译包裹的解析段】。本报告对这些开关只做**机制与生效范围**层面的描述：它们是「内核允许自己被放宽的地方有哪些」的清单，用途是审计构建形态与评估风险面，不构成任何配置操作指引，也不描述在生产设备上使其生效的路径（该路径不存在于公开口径中）。

配置载体的另一半是缺口。boot-args 之外的 AMFI 配置形态——例如 `com.apple.amfi.*` 一类 LaunchDaemon plist——**没有任何官方文档描述**；AMFI 内部规则如何读取这些载体更不公开（见 7.6）。因此正文只把 plist 记为「存在但未获官方描述」的载体类别，不写其键名与语义。

这张位图的真正意义在于它把「构建形态决定安全属性」写进了代码结构。可以核实的是三类：其一，位图条目是**内核内配置状态**而非进程属性，即放宽动作影响的是整个签名强制体系的行为；其二，位图的填充来源不止 boot-args——`CS_CONFIG_DEVELOPER_MODE_SUPPORTED`、`CS_CONFIG_MAP_JIT` 这类位与开发者模式、JIT 映射许可相关，锁定模式的耦合也汇入同一处（见 8.6 的 `csm_check_lockdown_mode()`），说明 Apple 用**同一套配置位图**收纳了多种来源的策略输入；其三，位图有位置不代表强制方一定理会——在 SPTM/TXM 形态下，监视器侧策略还会再削减一次，这正是 7.6 把「监视器削减」列为独立失效/风险模式的原因。

| 评估问题 | 可核实答案 | 依据 |
| --- | --- | --- |
| 生产 iOS 是否会读取这些豁免 | 不会——解析限定在 DEVELOPMENT（注释原文），生产设备默认各位为 0 | 【一手-源码 `kern_codesigning.c:219`】 |
| 位图是否有公开查询接口 | 本次未获得可引用口径（记为缺口） | 缺口，见 7.6 |
| 除 boot-args 外还有谁填这些位 | 开发者模式、MAP_JIT、锁定模式等来源共用同一配置位图 | 【一手-源码 `code_signing.h:42-53`】 |
| 置位即等于全面放宽吗 | 不一定——SPTM/TXM 形态下还会被监视器侧削减 | 【一手-源码，条件编译段】 |

**加固视角的一条结论**：面对这套位图，审计者应问的不是「怎么改」，而是「这台设备的构建类型与监视器形态，是否让它本来就不该出现非零位」。对零售设备而言，可核实的事实是这些豁免位不在解析路径上；因此任何把生产设备上的异常放宽归因于「boot-args 被使用」的分析，都必须先拿出构建类型的独立证据，否则属于无据推断。

## 7.5 research. namespace 与生产设备上的签名校验终止

`research.` 前缀 entitlement 的处理方式，是本章定性论断最集中的一条证据。平台安全指南的原文是："The research. namespace is respected only by the RESEARCH variant of the AppleMobileFileIntegrity kernel extension; any process with this entitlement is terminated on a customer device during signature validation."【一手-文档 指南p.85】。把这句话拆成机制要素，可以得到四个可引用的结论：

1. **执行者在内核**：被尊重的对象是 "AppleMobileFileIntegrity **kernel extension**" 的某个 variant，而非某个用户态代理。
2. **强制时刻在签名校验阶段**：终止动作的状语是 "during signature validation"，即 8.1 所描述的 exec 整体验证环节，而不是运行期某个惰性检查。
3. **判定粒度是 entitlement 命名空间**：AMFI 读取的不是进程自称的能力，而是签名里的 `research.` 前缀命名空间，并据设备形态决定是否尊重。
4. **同一字符串在不同设备形态上语义不同**：RESEARCH variant 才「尊重」，生产设备上一律终止——这排除了「带着正确 entitlement 字符串就能在生产设备上获得研究能力」的读法。

需要说明的是，第 4 点里的「设备形态」并非由 AMFI 单独决定，而是由更早的启动链环节锁定：研究用启动组件在生产融合状态的硬件上无法启动，安全研究设备与零售设备的差异在 iBoot 与 Boot Kernel Collection 层就已分岔。这一部分属于第 15 章（内核扩展/系统扩展的平台差异与安全研究设备）的范围，本章不重复展开，只在机制边界上标注一句：**AMFI 的 variant 选择是被动结果，而不是可被用户态影响的配置**。

从审计与加固视角，这条语义的价值在于它给出了一个可预期的判定行为：**在生产设备上，出现带 `research.` 前缀 entitlement 的进程，其预期结局是在签名校验阶段被终止**。这既说明 AMFI 的 namespace 门控是设备形态感知的，也说明「进程被内核在签名校验环节终止」这类事件本身属于可观测信号——其事件面归入崩溃与可观测性机制的讨论范围，本报告不在此处描述触发条件或规避方式。

| 情形 | 行为 | 证据层级 |
| --- | --- | --- |
| 生产设备 + `research.*` entitlement | 在签名校验阶段终止该进程 | 【一手-文档 指南p.85】 |
| RESEARCH variant（研究设备形态） | 尊重 `research.` 命名空间 | 【一手-文档 指南p.85】；设备形态细节详见第 15 章 |
| 用户态能否切换 AMFI variant | 无任何公开口径支持；启动链层已锁定 | 【一手-文档基础上的限定推论】 |

## 7.6 失效模式、审计缺口与本章口径边界

AMFI 一章可以给出的「失效模式」，全部是**配置形态与生命周期层面**的，而不是具体规则的漏洞面；后者不可写实，理由见本节末。本节不引入新证据源，各行模式的依据均来自前文已点名的一手条目【一手-源码 src/amfi.h:38、:245-265、:294，libkern/amfi/amfi.c，src/kern_codesigning.c:219-315，src/code_signing.h:42-53；一手-文档 指南p.85】。

| 失效/风险模式 | 机制描述 | 生效前提 |
| --- | --- | --- |
| 实现缺位 | 内核经 `amfi` 只读指针调用，若实现方未完成 `amfi_interface_register()`，表不可用；具体行为本次未核实，正文不断言 | 构建含该调用面 |
| 注册冲突 | 重复注册直接 `panic("AppleMobileFileIntegrity interface already set")`，属 fail-closed 的可用性形态 | 一次性注册语义 |
| 豁免位被置 1 | `CS_CONFIG_*` 各项对应放宽；仅 DEVELOPMENT/DEBUG 内核读取 boot-args 豁免 | 源码注释原文限定（`kern_codesigning.c:219`） |
| 监视器削减 | SPTM/TXM 形态下这类豁免还被进一步削减，不随位图全量生效 | 采用 CSM 的平台形态（第 8 章） |
| 命名空间门控 | 生产设备上 `research.` 一律终止；无公开口径支持用户态改变该判定 | 指南 p.85 原文 |

审计缺口需要按「不公开」清单逐条列明，否则很容易被写成看似合理的推测。按本次核实范围，**AMFI（AppleMobileFileIntegrity）kext 本体源码、Sandbox.kext 源码、board_config 设备清单、CoreEntitlements 用户态库均不公开**，因此以下四类内容一律不得写实：AMFI 内部规则与判定流程、entitlement 的完整生效条件表、按设备型号区分的启用差异、amfid 与内核之间的具体协作协议。可观测性同样有缺口：本节引用的源码与文档里，可直接观测的内核侧状态是代码签名监视器的 sysctl（见 8.2 的 `security.codesigning.monitor`），而 **AMFI 自身的配置位图在本次核实范围内没有公开查询接口**——「如何在不依赖私有工具的前提下读出当前豁免位」应记为缺口，不能假装已有答案。

另一类需要设防的误写是**把 amfid 的职责具体化**。守护进程的存在可以核实（`/usr/libexec/amfid`、LaunchDaemon `com.apple.amfid`），但其现代职责没有官方文档，因此任何「amfid 负责 X」的句子都必须带「公开研究/未获官方口径」的标注，且不得与内核侧一手接口证据写在同一句里，制造「全部一手」的错觉。

**本章口径边界。** 可承担事实表述的一手内容有四类：Apple 对 AMFI 的官方定性（kernel extension，指南 p.85）；内核侧接口形状（`KERN_AMFI_INTERFACE_VERSION 7`、`amfi_t`、`amfi_interface_register()`、`SECURITY_READ_ONLY_LATE(const amfi_t*) amfi`、重复注册即 panic 的原文）；配置载体实名（8 个 boot-args、两个 `CS_AMFI_MASK_*` 位、`CS_CONFIG_*` 全表）与那句 DEVELOPMENT 限定注释；CoreEntitlements 通道及其在 IOUC 中的消费点。必须降级或记为缺口的有三类：amfid 职责（研究级）、AMFI 内部规则（完全不可写实）、设备级启用差异与 AMFI 状态可观测接口（未公开）。一句话口径：**AMFI 是内核里的代码签名与 entitlement 执行主体——接口形状公开、判定规则不公开，放宽通道只在开发类内核存在。**

---

# 代码签名强制链：从 exec 到逐页校验

## 8.1 双层强制结构：整体验证与逐页校验

iOS 上的代码签名强制不是一次性动作，而是**两个时刻不同的层**，且这两层都能在 Apple 官方文档里找到直接对应句。

第一层在 **exec 时刻**。平台安全指南对 iOS/iPadOS/tvOS/visionOS/watchOS 的表述是："After the kernel has started, it controls which user processes and apps can be run… require that all executable code be signed using an Apple-issued certificate."【一手-文档 指南p.114】。主语是内核，动作是「控制哪些用户进程与应用可以运行」，条件是所有可执行代码必须由 Apple 签发的证书签名。这一层的对象是**进程**，判定发生在加载/启动路径上，与 7.5 中 "terminated on a customer device during signature validation" 的时刻一致。

第二层在 **运行期，粒度是页**。同指南下一段写得更为具体："At runtime, code signature checks of all executable memory pages are checked as they're loaded…"【一手-文档 指南p.115】。这一层的对象是**可执行内存页的映射动作**——每一次把代码页装进可执行映射，都要重做签名校验。指南另有 "Code signing is still present and enforced by the kernel" 一句（SSV 与代码签名一节），用来强调即使其他机制变化，页级校验仍由内核执行【一手-文档 指南相关章节】。

把这两层写成「双层结构」的理由不止是文档措辞，还有源码侧的互证：pmap 层确实存在按页校验映射的实现接口（`pmap_cs.c`/`pmap_cs.h`，见 8.2），与 p.115 的 "as they're loaded" 恰好对齐【一手-源码 + 一手-文档互证】。因此「iOS = exec 整体验证 + 运行期逐页验证」这一双层描述是可以按事实书写的。

必须补上的平台限定是：第二层在**不同平台上由谁执行**并不相同。在没有 PPL 的 macOS 语境下，逐页校验由普通内核路径完成，而非由 CSM 承担（该项细节本次属「部分核实」，正文只作范围性陈述）。换言之，「所有平台都做页级校验」这一句的公因数是「校验存在」，不是「校验由同一执行体完成」。

| 层 | 对象 | 时刻 | 官方依据 |
| --- | --- | --- | --- |
| 整体验证 | 进程/可执行文件 | exec、启动 | 【一手-文档 指南p.114】 |
| 逐页校验 | 可执行内存页映射 | 运行期，页装载时 | 【一手-文档 指南p.115】+【一手-源码 pmap_cs.c/h】 |

四问口径：负责什么——保证「运行的代码即被签名的代码」；强制点——exec 路径（BSD 层 `ubc_*` 与 AMFI 判定）与 pmap 层的页映射校验（Mach/vm 层）；生效前提——内核已启动并完成签名/信任配置（8.4 的监视器形态决定第二层的执行体）；不覆盖——不覆盖非可执行映射的数据页，也不覆盖校验之后代码语义层面的正确性。

这两层与第 6/7 章的关系值得点明，因为它们是「代码签名强制链跨三层」这一结论的具体化。exec 层从 BSD 层的加载路径发起，把判定交给 AMFI 内核接口（第 7 章），并把 entitlement 类问题带到 MACF 调用点（第 6 章）；页级层则完全在 pmap/vm 侧，由 CSM 形态决定执行体（8.2/8.3）。任何把「代码签名」写成单点模块的叙述——例如「签名校验就是 AMFI 做的」或「签名校验就是 PPL 做的」——都会丢掉这个跨层事实，也解释不了为什么 8.6 的 `CS_KILL`、`CS_KILLED` 这类旗标要由内核而非某个模块维护。

从可观测性角度，这一层结构给出的公开口径其实有限：Apple 文档只承诺「校验存在且由内核执行」，页级校验的每一次动作没有公开的逐次事件接口；相对可用的观测点集中在 8.2 的 sysctl（形态读数）与异常终止事件本身（8.6 的 `CS_KILLED` 语义）。因此对「运行期是否真的每页都校验」这类问题，正确回答方式是引用指南 p.115 的官方承诺加上源码侧的 pmap/CSM 调用面，而不是声称存在某种可穷举的外部观测手段。

| 想核实的问题 | 可用的公开口径 | 不可用的做法 |
| --- | --- | --- |
| iOS 是否在 exec 时控制可运行进程 | 指南 p.114 原文 | 引第三方二手转述当作 Apple 说法 |
| 运行期是否逐页校验 | 指南 p.115 + `pmap_cs.c/h` 调用面 | 声称可用外部工具枚举每次校验 |
| 校验由哪个执行体完成 | 8.2/8.3 的类型与 sysctl | 用机型推测（设备映射未公开） |

## 8.2 CSM 作为源码中的抽象层：类型、变量与 sysctl

「Code Signing Monitor（CSM）」常被当作一个口头概念使用，但在 XNU 源码里它是一个**有类型、有全局变量、有可观测 sysctl 的真实抽象层**，这一点必须写实。

类型层面，`osfmk/kern/code_signing.h` 定义了 `code_signing_monitor_type_t`，取值为 `CS_MONITOR_TYPE_NONE`、`CS_MONITOR_TYPE_PPL`、`CS_MONITOR_TYPE_TXM`【一手-源码 src/code_signing.h:35-37】。这一行枚举就是 CSM 的定义本身：**页级签名校验监视器由 PPL 或 TXM 承载，或者根本没有监视器**。变量层面，`kern_codesigning.c` 维护 `cs_monitor`【一手-源码 src/kern_codesigning.c:111】，并把它暴露为一个**只读 sysctl**：`security.codesigning.monitor`【同上 :115】。这是本章最有审计价值的一条事实：设备当前运行在哪种 CSM 形态下，内核自己提供了查询口径，无需依赖私有工具。

pmap 侧的对接面在 `pmap_cs.h`/`pmap_cs.c`。头文件中存在 `PMAP_CS_PPL_MONITOR` 宏【一手-源码 src/pmap_cs.h:71-87】以及形如 "Check if the PPL based code signing is enabled" 的注释【同上 :42】；pmap 代码按页处理映射并把校验请求交给当前形态的监视器。头文件注释还提及对 Apple internal kernel extensions 的覆盖范围【同上 :29 附近】，说明这套接口并非只服务于用户态二进制。

值得强调的是 CSM 抽象层的**设计意图**：内核的 pmap 代码不希望知道自己是跑在 PPL 上还是 TXM 上，于是用一个类型枚举加一个编译期宏把差异挡住，运行时则用一个 sysctl 暴露结果。对审计者，这种「挡住差异」的设计带来一个副作用——**不能把「存在 CSM」与「存在 PPL」混为一谈**（8.4 处理这一点），也不能把 `security.codesigning.monitor` 的读数当作「校验强度」的量表，它只说明执行体是哪一种。

| 要素 | 实名 | 位置 | 用途 |
| --- | --- | --- | --- |
| 监视器类型 | `code_signing_monitor_type_t`：NONE/PPL/TXM | `code_signing.h:35-37` | 定义 CSM 的三种形态 |
| 全局变量 | `cs_monitor` | `kern_codesigning.c:111` | 记录当前形态 |
| 可观测接口 | sysctl `security.codesigning.monitor`（只读） | `kern_codesigning.c:115` | 外部读取当前形态 |
| pmap 对接宏 | `PMAP_CS_PPL_MONITOR` | `pmap_cs.h:71-87` | 编译期选择校验路径 |

抽象层的存在本身还有一层加固含义，值得单独说。因为校验请求经由「类型枚举 + 前缀宏」下发，实际执行体处于比常规内核代码更受限制的一侧（PPL 分区，或 SPTM 环境下的 TXM 监视器），8.1 第二层校验的**判定因此不依赖普通内核路径里的可写状态**——这正是「签名校验完成后防止用户空间代码被修改」（8.4 引的 PPL 定义句）在实现上的落点；至于 PPL 与 TXM 各自的地址空间与特权模型细节，属硅上安全特性的专门讨论，本章不展开。但要把这句话的强度说准：本报告依据的是源码里的抽象层与 sysctl 事实，以及指南对 PPL/SPTM/TXM 职责的表述，**不依据任何「CSM 使内核不可能被攻破」式的推断**；CSM 保护的是校验通道，不是全部内核状态，第 9 章所列的用户客户端问题恰恰发生在校验通道之外的对象生命周期上。

四问落位：负责什么——把「页级签名校验该由谁执行」收敛成一个可枚举的类型与一个可观测读数；强制点——pmap 层的页映射路径（`pmap_cs.c`）与内核侧配置（`kern_codesigning.c`）；生效前提——构建经 8.3 的编译期分支选定形态，且设备使用该形态启动；不覆盖——不覆盖 exec 层整体判定、不覆盖 entitlement 授权，也不提供设备级形态清单（见 8.3）。

## 8.3 监视器选择的编译期逻辑与设备映射缺口

哪台设备用哪种 CSM，在源码里不是运行时配置，而是**编译期三分支**决定的。`pmap_cs.h` 的相关段落结构如下【一手-源码 src/pmap_cs.h:70-75、:81-82、:86-88】：

```
#if CONFIG_SPTM
#define PMAP_CS_PPL_MONITOR 0
#elif XNU_MONITOR
#define PMAP_CS_PPL_MONITOR 1
#else
#define PMAP_CS_PPL_MONITOR 0
#endif
```

紧邻的注释解释了这套开关为何能对上具体芯片：原文写明 **"XNU_MONITOR and PMAP_CS are both defined for the same targets in board_config.h."**【同上 :81-82】，另有注释再次说明 `PMAP_CS_PPL_MONITOR` 只在 `XNU_MONITOR` 成立且没有 `CONFIG_SPTM` 时才为 1【同上 :86-88】。把三分支与 8.2 的类型枚举对着读，可以得出一个精确的读法：**定义了 `CONFIG_SPTM` 的目标上，PPL 式校验路径不编译**（此时页级校验走 SPTM/TXM 形态，对应 `CS_MONITOR_TYPE_TXM`）；`XNU_MONITOR` 且无 SPTM 的目标上，编译进 PPL 式路径；两者都没有的目标上，`PMAP_CS_PPL_MONITOR` 为 0。要注意别把最后一种误读成「该构建完全没有代码签名校验」——它只表示**没有把 PPL 式监视器路径编译进去**，整体验证那一层与内核常规校验路径不属于这个宏。

缺口在这里非常明确，而且必须写成缺口。决定上述开关落在哪些设备上的文件是 `board_config.h`，它属于内部目标配置文件，**不在开源 config 的完整可得范围内**；因此「某具体机型/芯片对应哪种 CSM 形态」的设备级映射，Apple 从未公开，正文不得给出机型-机制对照表【一手-源码：宏名与逻辑已核实 / 设备清单未核实】。可替代的、合法的观测路径只剩运行时那一条：读取 sysctl `security.codesigning.monitor`【一手-源码 src/kern_codesigning.c:115】，用实测读数而不是推测的机型表来刻画具体设备。

| 问题 | 本次可回答的程度 |
| --- | --- |
| CSM 形态如何选定 | 编译期三分支（`CONFIG_SPTM` / `XNU_MONITOR` / 其他），有源码原文 |
| 定义由谁提供 | `board_config.h`（源码注释原文指认） |
| 哪台设备对应哪种形态 | **未公开**（board_config 属内部文件，开源 config 不含全量设备清单） |
| 具体设备的现时形态 | 可用只读 sysctl `security.codesigning.monitor` 实测 |

## 8.4 平台差异精确化：PPL、SPTM/TXM 与「CSM 仅 iOS」的校正

平台差异是这一章最容易被写错的地方，错法有两种方向，都需要校正。

先看 PPL 的官方口径。指南对 PPL 的定义是："Page Protection Layer (PPL) in iOS, iPadOS, visionOS, and watchOS is designed to prevent user space code from being modified after code signature verification is complete"，紧接着是一句关键的自限："**This protection isn't offered in macOS** because PPL is only applicable on systems where all executed code needs to be signed."【一手-文档 指南p.59-60】。这句原文同时给出了 PPL 的平台集合（iOS/iPadOS/visionOS/watchOS）、它的目的（签名校验完成后防止用户态代码被修改），以及 macOS 不提供的原因（PPL 只适用于「所有执行代码都必须签名」的系统）。最后这半句其实比平台清单更有解释力：macOS 上存在允许未签名/第三方内核代码的合法形态，因此 PPL 模型的前提不成立。

再看 SPTM/TXM 的口径，它与 PPL 不是同一集合。指南把 SPTM/TXM 描述为 **iOS/iPadOS/macOS/visionOS 均有**的机制，并给出芯片界限的明示："In the A15 or later and M2 or later SOCs, SPTM (in combination with TXM) replaces the PPL"【一手-文档 指南p.59-60】。

于是第一个误解校正是：**「CSM 概念仅存在于 iOS」不成立**。源码侧的 CSM 类型枚举里确实有 TXM 分支（8.2），而 TXM/SPTM 按 Apple 自己的说法覆盖 macOS（A15/M2 及之后的 Apple silicon Mac）；PPL 分支确实只在 iOS 系。两句话合起来的正确表述是：**CSM 这个抽象层跨平台存在，只是它的具体承载形态按平台与芯片不同**——iOS 系上早期是 PPL、A15/M2 起由 SPTM+TXM 取代；macOS 上从来没有 PPL，但在支持 SPTM/TXM 的 Apple silicon 上同样有由 TXM 承载的页级签名监视。

第二个方向上要防的是把 PPL 当成「iOS 专属所以可以在 macOS 语境里删除」。正确写法是分别陈述：PPL 官方明确不提供于 macOS；「macOS 上没有 PPL」**不等于**「macOS 上没有页级签名校验」，后者在无 PPL 的语境下由普通内核路径完成（本次为部分核实，正文只作范围性陈述，不展开 macOS 细节）。

| 机制 | Apple 明示的平台 | 芯片/版本口径 | 依据 |
| --- | --- | --- | --- |
| PPL | iOS、iPadOS、visionOS、watchOS；**macOS 不提供** | 在 A15/M2 之后被 SPTM+TXM 取代 | 【一手-文档 指南p.59-60】 |
| SPTM + TXM | iOS、iPadOS、macOS、visionOS | A15 或更新、M2 或更新的 SOC | 【一手-文档 指南p.59-60】 |
| CSM（抽象层） | 跨平台；形态取 NONE/PPL/TXM | 设备映射未公开，见 8.3 | 【一手-源码 code_signing.h:35-37】 |

这类误读为什么会大量产生，机制上有一个清楚的原因：**「PPL」与「CSM」在日常表述里被当作同一个东西使用**。PPL 是页保护层这一具体硅上/内核分区机制，CSM 是内核代码里那个「页级签名校验该由谁执行」的抽象（8.2）。一旦把两者混同，就会连带推出「PPL 只在 iOS ⇒ CSM 只在 iOS」这样看似合理但错误的链条；而源码里的 `CS_MONITOR_TYPE_TXM` 与指南中 macOS 也有 SPTM/TXM 的表述同时否定了这条链。可安全引用的表述模板因此是三句：**PPL 是 iOS 系的（macOS 不提供）；SPTM+TXM 自 A15/M2 起跨 iOS 与 macOS 并取代 PPL；CSM 作为抽象层跨平台，其形态需实测**。

同一批误读也常延伸到「旧设备怎么办」。可核实的只有芯片口径本身——Apple 用「A15 or later and M2 or later SOCs」来界定取代关系【一手-文档 指南p.59-60】；至于每款更早机型分别运行哪种形态，属于 8.3 的设备映射缺口，正文不列机型表，也不以「A15 之前 = 都有 PPL」这类补集推论代替证据，因为官方与源码都没有给出该补集。

## 8.5 entitlement 的四段位置：解析、判定、授权效果、消费点

entitlement 常被当成一个「写在二进制里的开关」，但机制上它的生命周期被拆在四个不同位置，四段的执行者与证据层级各不相同。把它们分段写清，可以避免「entitlement 由签名决定」这类过度简化的表述。

**第一段：解析。** 在 exec/mmap 时，由内核从签名内嵌的 blob 中提取字段，落到 `csb_*` 一类的进程内状态上，例如 `csb_platform_binary`、`csb_teamid`【一手-源码 bsd/kern/ubc_subr.c，`csb_*` 字段见 :3160 等处】。这一段属于 BSD 层的 Mach-O 加载路径，产出的是「这个进程签名里声明了什么」。

**第二段：判定。** 「声明是否有效、能否兑现」落在 AMFI 内核接口与 CoreEntitlements V2 上【一手-源码 src/amfi.h:37-38、:245-253 的 `OSEntitlements_query` 等】；官方文档层面的对应句是生产设备上带 `research.` 前缀 entitlement 的进程"terminated… during signature validation"【一手-文档 指南p.85】。这一段属于 `security/`+AMFI 接口层（第 7 章），产出的是「内核承认这个进程持有什么」。

**第三段：授权效果。** 官方表述来自 trust cache 一节："When a binary runs… its code directory is extracted and hashed… platform privileges"，并指出具备平台特权的二进制"may possess any entitlement"【一手-文档 指南p.69】。**该段原文语境是 Apple silicon 的 Mac**，移用到 iOS 时必须写成类比或另引 iOS 章节，不得直接当作 iOS 条文。

**第四段：消费点。** 真正把 entitlement 变成访问许可的位置分散在内核各处：MACF 的各 `mpo_*` 调用点按 entitlement 放行（第 6 章），IOKit 用户客户端则在方法级检查 `checkEntitlement`、打开级检查 `kIOUserClientEntitlementsKey`（第 9 章）。框架文档中与之呼应的一句是 macOS 章的 "Mandatory Access Controls (MACs) require code signing to enable entitlements protected by the system"【一手-文档 指南p.119，**macOS 章节**，引用于 iOS 需加平台限定】。

| 段 | 执行位置 | 产出 | 证据层级 |
| --- | --- | --- | --- |
| 解析 | BSD 层 Mach-O 加载（`ubc_subr.c`，`csb_*`） | 签名中声明的字段 | 【一手-源码】 |
| 判定 | AMFI 内核接口 + CoreEntitlements V2 | 内核承认的 entitlement | 【一手-源码 + 一手-文档】 |
| 授权效果 | trust cache 命中 → platform privileges | 可持有任何 entitlement 的前提 | 【一手-文档，**Mac 语境**】 |
| 消费点 | MACF 调用点、IOUC 方法/打开级检查 | 具体访问是否放行 | 【一手-源码】 |

四段的「不覆盖什么」也一并说明：解析段不涉及授权、判定段的规则不公开（7.6）、授权效果段在 iOS 上没有一手可直接引文的对应表述、消费点段则受限于策略是否订阅相应 hook（6.3）。

行号口径提醒：本节引用的 `ubc_subr.c` 与 `IOUserClient.cpp` 在本地镜像中属**节选文件**，条目级行号须以上游完整文件为准；能承担事实表述的是「字段名与调用形状」（`csb_platform_binary`、`csb_teamid`、`OSEntitlements_query`、`copyEntitlementAsOSObjectWithProc`），而不是精确到行的定位。

四段模型对加固工作的实际用处，是它把「entitlement 相关审计」拆成四个可分别提问的位置：

| 段 | 应问的问题 | 可核实到什么程度 |
| --- | --- | --- |
| 解析 | 签名 blob 里到底写了哪些字段 | 字段名一手可核（`csb_*`） |
| 判定 | 该字段在当前设备形态下能否兑现 | 通道可核（AMFI/CoreEntitlements），规则不可核 |
| 授权效果 | 是否命中 trust cache 而获得平台特权 | 官方原文仅 Mac 语境，iOS 需限定 |
| 消费点 | 哪段内核代码读了它并据此放行 | 调用点可核（MACF 条目、IOUC 两级检查） |

这条链上最容易被忽略的是**第四段**：前三段都正确，只要某个消费点不去查询或自行解释 entitlement，实际效果仍然错误。第 9 章把 IOUC 的两级授权检查单独立节，正是因为它是这条链上少数有 Apple 逐字注释可引的消费点。

## 8.6 旗标与产品概念的分离及本章口径边界

Mach-O 的 code signature flags 是跨平台通用的编码，`osfmk/kern/cs_blobs.h` 中的定义可以直接引用【一手-源码 osfmk/kern/cs_blobs.h】：

| 旗标 | 值 | 头文件语义（原文措辞摘述） |
| --- | --- | --- |
| `CS_HARD` | `0x100` | "don't load invalid pages" |
| `CS_KILL` | `0x200` | 校验失败即杀进程 |
| `CS_GET_TASK_ALLOW` | `0x4` | 与相应 entitlement 关联的调试能力位 |
| `CS_REQUIRE_LV` | `0x2000` | "require library validation" |
| `CS_RUNTIME` | `0x10000` | "Apply hardened runtime policies" |
| `CS_KILLED` | `0x1000000` | "was killed by kernel for invalidity" |

关键口径是：**这些位本身是 Mach-O 编码通用，不区分平台；平台差异在「谁强制、默认策略是什么」**。把某一位的出现当作「该平台提供某功能」，是本章两个高风险误写的共同来源，需要双向校正。

**校正一：hardened runtime 不是 iOS 机制。** Apple 开发者文档对该功能的表述自限于 macOS："Protect the runtime integrity of your **macOS app**"【一手-文档 https://developer.apple.com/documentation/xcode/configuring-the-hardened-runtime】，Xcode 设置项 `ENABLE_HARDENED_RUNTIME` 同样处在 macOS 语境。iOS 上没有「开启 hardened runtime」的选项；iOS 的等效保护是由「强制签名 + PPL/SPTM + library validation + 沙箱」整体达成的，指南对 iOS 运行期的表述是 p.115 的逐页校验句。因此，`CS_RUNTIME` 位出现在 iOS 二进制里，只说明该位被编码进去了，**不等于 iOS 向开发者提供了 hardened runtime 这一功能开关**。

**校正二：library validation 也不是 macOS 专属概念。** 与上一例相反，这一项在 iOS 章节里有明文：指南在「App code signing process in iOS, iPadOS, tvOS, visionOS, and watchOS」一节写道，系统会对进程在启动时链接的全部动态库做签名校验，且"A program may link against any platform library that ships with the system or any library with the same team identifier in its code signature as the main executable."【一手-文档 指南p.114-115】。也就是说 iOS 上默认强制，判定依据是「平台库」或「与主可执行文件同 team identifier」。退订通道是一条 entitlement：`com.apple.security.cs.disable-library-validation`【一手-文档 https://developer.apple.com/documentation/bundleresources/entitlements/com.apple.security.cs.disable-library-validation】；该页面本身已核实存在，但其「在 iOS 上的可用性字段」等细节本次未抓取成功，故此处标注为【页面已核实/细节部分核实】，不据以推断可用平台范围。macOS 上 library validation 主要与 hardened runtime 绑定，属另一语境，两者不应互相移用结论。

最后一条是纯源码观察：**锁定模式与代码签名监视器在实现上是耦合的**。`kern_codesigning.c` 中存在 `csm_check_lockdown_mode()`，它调用 `CSM_PREFIX(enter_lockdown_mode)()`，把锁定模式状态同步给代码签名监视器，且与 `CS_CONFIG_MAP_JIT` 这类配置位相关【一手-源码 src/kern_codesigning.c:1108-1118】。Apple 官方文档没有对应的表述，因此本报告把它写成源码层面的实现事实，不赋予它任何产品承诺含义，也不描述其触发条件之外的细节。

**本章口径边界。** 可作事实书写的是：双层强制（指南 p.114 与 p.115 两句原文）、CSM 的源码实体（类型枚举、`cs_monitor`、只读 sysctl `security.codesigning.monitor`）、编译期三分支与 `board_config.h` 的注释指认、PPL 不提供于 macOS 与 SPTM+TXM 自 A15/M2 起跨 iOS 与 macOS 取代 PPL 的官方原文、entitlement 四段的执行位置、六个旗标的取值与含义。必须限定或记为缺口的是：具体机型的 CSM 形态映射（未公开，只可实测）、trust cache→platform privileges 原文的平台语境（Mac 语境，移用需限定）、"MACs require code signing to enable entitlements"（macOS 章节）、`disable-library-validation` 的 iOS 细节字段（页面已核实/细节部分核实）、macOS 无 PPL 时逐页校验的执行路径细节（部分核实）、锁定模式与 CSM 的耦合（仅源码观察）。一句话口径：**代码签名在 iOS 上是「exec 整体 + 运行期逐页」的双层强制，第二层由 PPL 或 TXM 承载且可实测，旗标编码通用而强制策略按平台不同。**

---

# IOKit 与用户客户端边界

## 9.1 用户态入口与 selector 派发路径

用户态进入驱动代码的主干道是 `IOConnectCallMethod` 一族调用。在内核侧，参数被组装成 `IOExternalMethodArguments`，其中调用方给出的**方法编号**被直接写入 `args.selector`【一手-源码 iokit/Kernel/IOUserClient.cpp:5055 等处】；框架随后带着这个 selector 去做两件事：先经过全局的过滤回调 `gIOUCFilterCallbacks->io_filter_applier(..., io_filter_type_external_method, selector)`【同上 :5092、:5214、:5328 三处，分别覆盖 external method 与 async method 类型】，再按 selector 索引驱动声明的方法数组，把控制权交给对应方法的处理代码（9.3）。

理解这条路径的关键，是认清 **selector 是一个由用户态提供的整数**。它不是句柄、不是经过校验的引用，而是索引；索引一旦可以被外部控制，「该索引是否存在、是否被允许、谁有权调用」就成为整条边界上最要紧的三问。框架在源码注释层面对这三问给出了明确的责任划分（9.3 引原文），而 9.6 所列举的公开研究成果，几乎全部落在这三问中某一问答得不好的地方。

本节引用的源码文件需要一条行号口径提醒：本地镜像中的 `IOUserClient.cpp`、`ubc_subr.c`、`vm_kern.c` 等属于**节选文件**，涉及缺口处应以 GitHub 上游完整文件为准；本节标注的行号对应镜像所对应的上游 main 分支提交（tree sha f6217f8），历史 xnu 发布标签的行号不同。

| 问题 | 口径 |
| --- | --- |
| 负责什么 | 把用户态调用映射为内核内的方法入口，并在映射前后提供集中式过滤与授权检查位 |
| 强制点在内核何处 | `iokit/Kernel/IOUserClient.cpp` 的 dispatch 路径（selector 落位与 `io_filter_applier` 调用点） |
| 生效前提 | 驱动实现 `externalMethod` 并经由 `dispatchExternalMethod()`；用户态已持有该 service 的连接（open 成功） |
| 不覆盖什么 | 不覆盖方法参数**内容**的语义校验，也不覆盖驱动自身在方法体内对资源的后续使用方式 |

作为交叉参照：MACF 也在 IOKit 路径上设了强制点——`mpo_iokit_check_open`、`mpo_iokit_check_open_service`、`mpo_iokit_check_set_properties`、`mpo_iokit_check_get_property` 与框架文件 `security/mac_iokit.c`【一手-源码 src/mac_policy.h:1099-1149，详见 6.5】。也就是说，「打开服务」这一动作上同时存在 MAC 层的策略检查与 IOUC 层的框架检查，二者属于不同机制、不同代码位置，不能相互替代。

## 9.2 参数形态、上限常量与异步通道

`IOExternalMethodArguments` 把一次用户态调用的输入按形态切成三类，头文件对结构本身的描述可直接引用【一手-源码 iokit/IOKit/IOUserClient.h:125-140】：

| 形态 | 字段 | 语义与边界 |
| --- | --- | --- |
| 标量 | `scalar`（输入/输出计数由方法表声明） | 数量存在上限常量 `kIOExternalMethodScalarInputCountMax`【一手-源码 IOUserClient.h:112-114 注释】 |
| 结构体外拷 | `structOOL` | 以 out-of-line 内存描述的结构体；大小由用户态给出，需框架与驱动共同约束 |
| 异步引用 | `asyncReference` | 用于异步方法的参数通道，对应 `io_filter_type_async_method` 一类过滤入口【一手-源码 IOUserClient.cpp:5214、:5328】 |

把这张表当作审计清单来读，可以得到三条机制结论。其一，**标量数量有编译期/接口层上限**——`kIOExternalMethodScalarInputCountMax` 的存在说明框架不允许方法表声明任意多的标量参数，这是唯一可以直接引用常量名的「输入规模约束」；其具体数值属源码事实，本次不给出跨版本数字。其二，**结构体与缓冲区走 OOL 通道**，意味着长度信息来自用户态描述，框架能核对形式而不能理解语义——「这个结构体的长度与内容在业务上是否合法」永远是驱动的问题。其三，**异步通道有独立的过滤入口**，即 9.1 里三处 `io_filter_applier` 调用覆盖不同 method 类型的事实；这排除了「只在同步 external method 上做过滤」的读法。

四问口径如下：负责什么——把不同形态的输入规范化成驱动可消费的统一结构，并给出规模上限与过滤钩子；强制点——`IOUserClient.cpp` 的组装与派发段，以及 `IOUserClient.h` 中声明结构与方法表字段的位置；生效前提——驱动以方法表声明每个 selector 的输入/输出形态与数量，声明与实际实现必须一致；不覆盖——不覆盖指针/句柄类参数在驱动内部的解引用生命周期问题（9.6 所列公开研究恰恰集中在此），也不覆盖跨多次调用才成立的状态机约束。

## 9.3 责任归属：dispatchExternalMethod 的注释原文

关于「谁必须校验用户客户端输入」，Apple 在 `IOUserClient.h` 的文档注释里给了明确措辞，这是本报告可以逐字引用的责任划分依据：

> "- the externalMethod override is required and must call dispatchExternalMethod() to do basic argument checking before calling subclass code… dispatchExternalMethod() … will index into the array for the given selector"

【一手-源码 iokit/IOKit/IOUserClient.h:549-551】。这句注释包含三层强制要求与一层实现揭示，需要分开读。

第一层是**「required」**：`externalMethod` 的覆写在框架看来是必须的，而不是可选便捷路径。第二层是**「must call dispatchExternalMethod()」**：驱动在把自己的代码接到调用方之前，必须先经过框架的基础参数检查——也就是说，「基础参数检查」这项职责被框架以调用约定的形式接管了下来，驱动跳过它即违反注释所述的义务。第三层是**时序**："before calling subclass code"，基础检查在前、子类逻辑在后。第四层则是最要紧的实现揭示：`dispatchExternalMethod()` 会**按 selector 索引驱动提供的方法数组**——这把 9.1 提到的「selector 是用户可控整数」直接变成了框架自己承认的事实，索引越界与否、该 selector 是否有效，取决于方法表的声明与框架的核对逻辑，而不取决于调用方的善意。

于是责任归属可以收口成一句话：**框架提供机制、驱动承担责任**。框架负责的是基础参数检查、方法索引派发、过滤钩子与授权检查位；驱动负责的是它自己的方法语义——参数取值范围、缓冲区长度与实际使用的匹配、对象引用的生命周期、跨调用状态的一致性。注释用词是 "basic argument checking"，这个 "basic" 就是边界本身：基础检查由框架做，业务级校验没人代做。

| 责任项 | 归属 | 依据 |
| --- | --- | --- |
| `externalMethod` 必须存在并转发给框架 | 驱动 | IOUserClient.h:549-551（"required"） |
| 基础参数检查（basic argument checking） | 框架（`dispatchExternalMethod()`） | 同上 |
| 按 selector 索引方法数组 | 框架 | 同上 |
| 方法语义级输入校验、越界与生命周期防护 | 驱动 | 「basic」一词的范围限定 + 9.6 研究证据 |
| 按方法声明授权 | 驱动声明、框架执行 | 见 9.4 |

需要校正的对应误解有两个方向。**方向一：「框架既然做了基础检查，驱动就不必再校验输入」**——注释的措辞是 "basic argument checking"，其范围只到形态与索引层面（9.2 已说明结构体长度、句柄类参数的语义都由驱动消费），因此「框架检查过」不构成输入正确的证明。**方向二：「外部工具能替驱动补上校验」**——过滤回调与两级 entitlement 检查决定的是「能不能调用」，不决定「调用内容是否被安全处理」；把访问控制当作输入校验的替代品，是把两道不同性质的防线混为一谈。这也是本章把「机制」与「责任」反复分列的原因：可核实的只有框架提供了哪些检查位，检查位是否被用好属于驱动实现质量问题，只能靠代码审计与崩溃侧的可观测性事后判断。

这一节的「不覆盖什么」同样重要：注释所要求的义务停留在「必须调用框架派发器」这一层，**它不构成对驱动实现质量的外部担保**。也正因如此，9.6 会讨论一个必须写成缺口的点：Apple 是否有现行开发者文档专章重申「驱动必须校验用户客户端输入/方法索引」，本次未定位到。

## 9.4 两级 entitlement 与框架—AMFI 的直接耦合

IOKit 用户客户端路径上的授权检查有两级，两级都写在同一份头文件的注释里，且都以 entitlement 为判定物。

**方法级**。承接 9.3 的派发语境，`IOUserClient.h` 对方法表条目的说明是：

> "If the checkEntitlement field… non-NULL, then the calling process must have the named entitlement key… or kIOReturnNotPrivileged is returned… per-selector entitlement checks"

【一手-源码 iokit/IOKit/IOUserClient.h:559-562】。可核实的语义是：每个 selector 可以声明一条必需的 entitlement 名，非空时框架在派发前检查调用进程是否持之，不持有则返回 `kIOReturnNotPrivileged`。注释里的 "per-selector entitlement checks" 说明这是**按方法粒度**的授权，而不是「能打开就什么方法都能调」。这直接回应了一类常见误解：把「open 成功」等同于「全方法可用」。

**打开级**。同一头文件另有 `kIOUserClientEntitlementsKey` 的说明："Entitlements required for a process to open the IOUC"【一手-源码 IOUserClient.h:546-547】——即某些用户客户端在**建立连接阶段**就要求调用方持有指定 entitlement。两级叠加后的模型是清晰的：先按打开级条件决定能否连接，再按方法级条件决定能否调用某个 selector。

两级检查共同的判定物「进程实际持有什么 entitlement」，其来源正是第 7 章的 AMFI 通道，而且这不是推论而是源码里可见的直接耦合：`IOUserClient.cpp` 在把 entitlement 交给使用前先做框架自身的入参校验，原文形如 `/* Validate input arguments */ if (task == kernel_task || entitlement == NULL) return NULL;`，随后经 `amfi->OSEntitlements.copyEntitlementAsOSObjectWithProc` 取出 entitlement 对象【一手-源码 iokit/Kernel/IOUserClient.cpp:1584-1595】。这段代码有三重价值：其一，它证明 **IOKit 核心与 AMFI 接口在用户客户端路径上直接耦合**；其二，它显示框架对传入的 `task` 与 `entitlement` 做了显式核对（包括拒绝对 `kernel_task` 走该通道），而不是无条件采信；其三，它是第 7 章「AMFI 主体在内核」定性的一处交叉证据——消费方就在 IOKit 派发代码里。

| 层级 | 检查物 | 失败结果 | 依据 |
| --- | --- | --- | --- |
| 打开级 | `kIOUserClientEntitlementsKey` | 无法建立到该用户客户端的连接 | IOUserClient.h:546-547 |
| 方法级 | 方法表条目的 `checkEntitlement` | `kIOReturnNotPrivileged` | IOUserClient.h:559-562 |
| entitlement 取值 | 经 `amfi->OSEntitlements` 通道 | 取不到即不进入授权判定 | IOUserClient.cpp:1584-1595 |

需要标注的范围是：框架执行的是「有没有这条 entitlement」的核对，而**每条 entitlement 由谁签发、在什么签名条件下才能兑现**，属于第 7/8 章的判定链；两处机制相接，但本章不重述其规则内容。

## 9.5 IOUserClient2022 与集中式方法过滤通道

前四节提到的加固注释并不是散落在旧接口上的建议，而是集中承载在一个派生类上：`IOUserClient2022 : public IOUserClient`【一手-源码 iokit/IOKit/IOUserClient.h:564】。9.3 的「必须经 `dispatchExternalMethod()`」、9.4 的两级 entitlement 检查等说明，都出现在这一族的类文档语境中。可核实的表述方式是：**IOKit 把更严格的调用约定挂在较新的派生类上**，而不是改写既有 `IOUserClient` 的对外语义。这既是渐进加固的实现方式，也给审计留下一个可操作的问题——某个具体驱动的类链条是否落在这些被加固的形态之内；该判断需要针对具体 kernelcache 中的类层次做，本章不给出任何驱动级结论。

另一条集中的通道是**全局方法过滤回调**。`IOUserClient.h` 声明了过滤回调类型 `io_filter_applier_t`【一手-源码 iokit/IOKit/IOUserClient.h:192】，配套存在 `IOUserClient::setFilterCallbacks` 一类的接口；内核侧则有一个全局对象 `gIOUCFilterCallbacks`，在派发路径上被调用三次，覆盖 external method 与 async method 等不同类型【一手-源码 iokit/Kernel/IOUserClient.cpp:5092、:5214、:5328】。它的意义在于把「按方法编号做集中过滤」这件事做成了框架级设施：不需要每个驱动自己实现一遍，策略可以在派发前统一介入。

| 设施 | 形态 | 可核实结论 |
| --- | --- | --- |
| `io_filter_applier_t` / `setFilterCallbacks` | 类型与注册接口 | 框架预留了集中式过滤的注册面 |
| `gIOUCFilterCallbacks` | 全局回调集合 | 派发路径上确有调用，覆盖 external/async method |
| `IOUserClient2022` | 派生类 | 承载派发/授权/过滤相关的加固注释 |

这里的缺口必须如实写出：**过滤回调的注册方（即哪个子系统在运行时注入策略）不在本次可核实的文件范围内**，本地镜像文件里看不到注册者的身份。因此正文只陈述「框架提供了集中过滤通道，且该通道在派发路径上被调用」，**不断言该通道由沙箱注册、也不断言其判定内容**。同样地，回调为空时派发是否照常完成、以何种默认结果返回，本次没有可引用的一手口径，不作推测。

从加固视角，这一节给出的可检查项是三个问题：目标驱动是否使用带加固约定的派生形态；其方法表是否为每个 selector 声明了必要的方法级 entitlement；框架级过滤通道在当前构建中是否被接入。三者都能从公开可得的机制描述里定位到检查位置，而不需要知道任何闭源策略的内容。

| 维度 | 口径 |
| --- | --- |
| 负责什么 | 把更严格的调用约定与集中式过滤做成框架设施，而非散落在各驱动 |
| 强制点在内核何处 | `IOUserClient.h` 的类约定与 `io_filter_applier_t` 类型、`IOUserClient.cpp` 派发路径上三处 `gIOUCFilterCallbacks` 调用 |
| 生效前提 | 驱动继承带加固约定的类、声明方法表、策略方在运行时注册过滤回调 |
| 不覆盖什么 | 注册方身份与其注入的判定内容未核实；回调为空时的默认语义未知 |

## 9.6 高暴露驱动的证据分级与官方口径缺口：本章口径边界

用户客户端边界之所以是内核安全分析的重点，历史上由公开研究反复指出。本节的目的不是复述漏洞细节，而是**示范证据分级**：同一句「某些驱动暴露面过大」，在不同来源层级上的可写程度差别极大。

第一梯队是可直接点名的公开研究。IOSurface 方面：Google Project Zero 的缺陷档案 "OS X/iOS kernel use-after-free in IOSurface [42452365]" 描述了 `IOSurfaceRootUserClient` 保存 task 指针而未持有相应引用的问题类别，并写出该用户客户端 "reachable from interesting sandboxes including safari and chrome"【公开研究 Project Zero，https://project-zero.issues.chromium.org/42452365】；Lane（Checkra1n 团队）在 Black Hat EU-17 的白皮书 "Rooten Apple's Vulnerability Heaven In The IOS Sandbox" 讨论了 AppleAVE.kext 的引用计数问题（CVE-2017-6989）【公开研究 Black Hat EU-17，https://blackhat.com/docs/eu-17/materials/eu-17-Donenfeld-Rooten-Apples-Vulnerability-Heaven-In-The-IOS-Sandbox-wp.pdf】；POC2019 的 "Exploiting IOSurface"（Liang）延续了同一方向的观察【公开研究 POC2019，https://papers.put.as/papers/ios/2019/LiangPOC.pdf】。三者的共同点是：结论出自研究者本人发表的一手文献（而非媒体转述），但在本报告的证据分级里仍统一归入【公开研究】层级，**不得写成 Apple 官方承认的攻击面**。本报告不复现任何触发路径或利用步骤。

第二梯队只有二级来源，须降级书写。GPU/AGX 方向：2025 年在野链 DarkSword 中的 AGX 部件 CVE-2025-43510、CVE-2025-43520 目前经安全媒体转述（8ksec 分析文）【公开研究/媒体转述 二级，https://www.8ksec.io/darksword-kernel-escalation-cve-2025-43510-43520/】；**一手应指向 Apple security releases（https://support.apple.com/100100）的组件名条目，本次因页面为 JS 渲染未取得**，因此「Apple 官方把这两个 CVE 归到哪个组件」是缺口，正文不代 Apple 命名组件。另有一条明确不采用的引用：0xdeadbeef 的《Your (IOSurface) Kernel is Mine》——本次搜索未命中原文 URL，按纪律**写成缺口而不引用**，不凭记忆转述其内容。

必须设一条红线：**「Apple 官方公布过某类驱动在 iOS X.Y 移出内核／收紧 IORegistry／收紧用户客户端权限」这一句式，本报告禁止使用**。它不是「暂时没找到」，而是**负面结果**：平台安全指南全文对 "IORegistry" 与 "user client" 的检索均无 iOS 版本化条目；同时，Apple 关于第三方内核扩展、AuxKC/UAKL、DriverKit 用户态化的全部官方表述都自限于 macOS 语境（详见第 15 章）。iOS 侧可核实的官方口径只有一条方向相反的：iOS 系根本不允许第三方驱动（App Store 模式语境，且开发者文档从未向 iOS 开放 kext）。任何「iOS 16 移除了某某驱动」的句子都属于编造。

在此边界内，可以给出的一条结构性判断必须标注其性质：**iOS 上没有第三方 kext，因此用户态可触达的 IOUserClient 集合由系统 kernelcache 决定**——此句是【分析推断（基于 B1/B5/B7 一手事实组合）】，即第 3 章的目录职责口径、第 7 章的 AMFI 内核内定性、以及本章的 IOUC 派发与授权机制三条一手事实的组合，而不是任何 Apple 文档原句。它的实用含义是：攻击面清单只能来自对具体 kernelcache 的类与用户客户端枚举，不能来自任何「官方公布的驱动收编时间表」。

把本节的方法论收敛成一张对照表，供后续章节统一口径：

| 证据梯队 | 典型条目 | 允许的写法 | 禁止的写法 |
| --- | --- | --- | --- |
| 一手-文档 | IOUserClient.h 注释原文、指南平台自限语句 | 逐字引用并注明页码/路径 | 引申为跨平台承诺 |
| 一手-源码 | selector 落位、`io_filter_applier` 调用、两级 entitlement 检查 | 描述机制、位置与生效前提 | 用行号跨版本照搬 |
| 公开研究（一手文献） | Project Zero 档案、Black Hat EU-17、POC2019 | 标注机构/年份转述其结论 | 挂名为 Apple 官方承认 |
| 二级转载 | AGX/DarkSword 媒体分析 | 标注「媒体转述」+ 缺口说明 | 当作 Apple 组件命名依据 |
| 未命中 | 0xdeadbeef 那篇 IOSurface 文献 | 记为缺口 | 凭记忆转述或引用 |
| 负面结果 | 「iOS X.Y 移除某类驱动」 | 明写「查无官方口径」 | 编造版本化举措 |

**本章口径边界。** 可作事实书写的是：`IOConnectCallMethod` → `args.selector` → `io_filter_applier(..., io_filter_type_external_method, selector)` 的派发与过滤路径（含三处调用覆盖 external/async method）、`IOExternalMethodArguments` 的 scalar/structOOL/asyncReference 三形态与 `kIOExternalMethodScalarInputCountMax`、`dispatchExternalMethod()` 承担基础参数检查与按 selector 索引的注释原文、`checkEntitlement` 非空即要求调用进程持之否则 `kIOReturnNotPrivileged` 的方法级授权、`kIOUserClientEntitlementsKey` 的打开级要求、框架对 `task`/`entitlement` 的显式校验与转交 AMFI 的代码、`IOUserClient2022` 与 `gIOUCFilterCallbacks` 的存在。必须记为缺口的五项是：Apple 现行开发者文档中「驱动必须校验用户客户端输入/方法索引」的专章（仅存档 IOKit Fundamentals 与旧 Driver Design Guidelines 的二级转载，一手 URL 未定位）、过滤回调的注册方身份、回调为空时的默认判定语义、AGX 两枚 CVE 的 Apple 官方组件名、以及那篇未命中 URL 的 IOSurface 文献。全部禁止书写的是：任何「某 iOS 版本官方移除某类驱动/收紧 IORegistry」的表述，以及把 IOSurface、AGX 的攻击面结论挂名为 Apple 官方口径。一句话口径：**IOKit 用户客户端边界上，框架提供派发、基础检查、过滤与两级授权机制，方法语义与资源生命周期的正确性由驱动承担，而驱动暴露面的事实描述只能来自分级后的公开研究。**

---
# 内核地址随机化：Apple 的说法与源码的事实

## 10.1 术语澄清：Apple 面向用户的文档只称 ASLR

**负责什么**：这一节负责的并非某个硬件或内核机制，而是本报告后续所有关于内核地址随机化的表述口径——先把名字定下来，才谈得上"Apple 说了什么"与"源码显示什么"的分离。

在 *Apple Platform Security*（落款 **August 2026**，本简报记为 APSG）全文以不区分大小写检索 `KASLR`、`kernel slide`、`randomization of the kernel`，**命中 0 次**。该文档在地址随机化上唯一使用的术语是 **"Address Space Layout Randomization (ASLR)"**，而它出现的位置是 "Security of runtime process in iOS, iPadOS, and visionOS" 一章下的子节，语境明确是进程而非内核【一手-文档 apsg.txt:5640-5650】：

> "Address Space Layout Randomization (ASLR) helps protect against the exploitation of memory corruption bugs. **Built-in apps use ASLR to help randomize all memory regions upon launch.** … Xcode, and the iOS or iPadOS development environments, automatically compile third-party programs with ASLR support turned on."

也就是说，Apple 面向用户的一手文档对"内核镜像本身被放到随机地址"这件事**没有单独命名，也没有专门段落**。与之相对，`KASLR` 在 XNU 开源树里是真实存在的**内部用语**，可点名的原文有三处：

| 位置 | 原文（照录） | 性质 |
|---|---|---|
| `bsd/kern/kern_sysctl.c:4313` | "Boolean indicating if KASLR is active." | sysctl 说明注释 |
| `osfmk/arm64/arm_vm_init.c:97` | "KASLR parameters" | 变量组注释头 |
| `osfmk/i386/i386_vm_init.c:285` | `kprintf("KASLR slide: 0x%016lx dynamic\n", vm_kernel_slide)` | 启动期内核打印 |

三处都是**代码注释与日志文本**，不是产品文档、不是开发者文档、也不是安全指南条目。据此，本报告统一采用如下收口写法：**源码与研究界称 KASLR；Apple 官方文档只称 ASLR，未单独为内核地址随机化命名**【一手-源码 上述三处；一手-文档 apsg.txt:5640-5650】。把带引号的 "KASLR" 当作 Apple 官方术语、或把它与 ASLR 并列写成"Apple 官方称之为 KASLR/ASLR"，都属于口径越界。

> **口径提示**：APSG 的落款版本要保留。本章引文均出自 August 2026 版，不应写成"Apple 最新版文档"。

**术语层面的三条界线**

- 可写：Apple 官方只用 ASLR 且语境是进程；`KASLR` 是 XNU 源码内部用语（三处一手原文可指认）。
- 不可写：Apple 官方使用/命名了 KASLR；内核地址随机化在 APSG 中有专章。
- 未公开不等于不存在：APSG 未记载，只说明"没有面向用户的官方口径"，不能反过来断言 iOS 内核不做地址随机化——源码侧的证据恰恰相反（见 10.2、10.3）。

## 10.2 `vm_kernel_slide`：内核随机量的正式名与只读化

**负责什么**：内核镜像（以及随它一起装载的 Apple kext 文本）在虚拟地址空间里的整体平移量，在 XNU 里的正式载体是全局变量 `vm_kernel_slide`。它是一个 `vm_offset_t` 数值，一旦定下，内核所有段的可寻址位置就由"链接期地址 + slide"决定；配套的 `vm_kext_*` 则描述 kext 区间是否落在同一位移上。

源码事实如下【一手-源码 osfmk/arm64/arm_vm_init.c:97-110】：`arm_vm_init.c:97` 的注释头是 "KASLR parameters"，其下 `arm_vm_init.c:105` 声明

```c
SECURITY_READ_ONLY_LATE(vm_offset_t) vm_kernel_slide;
```

同一注释组里的兄弟变量包括 `vm_kernel_base`/`vm_kernel_top`、`vm_kext_base`/`vm_kext_top`、`vm_kernel_stext`/`vm_kernel_etext`，以及 `vm_kernel_slid_base`/`vm_kernel_slid_top`（`arm_vm_init.c:98-110`）。需要注意两点读法：其一，`vm_kernel_slide` 是**源码层面的全局符号，不是 Apple 面向用户的术语**，报告引用它时必须带这一说明；其二，`vm_kernel_slid_base/top` 与 `vm_kernel_base/top` 是两组不同的量——从命名与所在注释组看，前者与 slide 的取值区间相关、后者描述内核镜像自身所占区间，但**各变量的精确语义本次未逐条核实**，因此混用二者（把"slide 的取值范围"写成"内核的大小"）属于超出依据的读法。

**在哪一层强制 / 生效前提**：`SECURITY_READ_ONLY_LATE()` 是本节真正值得强调的保护属性。它把变量放进"启动完成后即只读"的内核数据段：写完一次，后续内核代码路径不得再改。这与本报告前半部分 AMFI 只读函数表（`libkern/amfi/amfi.c` 中的 `SECURITY_READ_ONLY_LATE(const amfi_t*) amfi`）以及下文 13.3 的 `SECURITY_READ_ONLY_LATE(ipc_port_t) ux_handler_port` 构成一条一致的设计原则——**凡决定保护参数的内核全局量，先自问"能否被 READ_ONLY_LATE 化"**【一手-源码 osfmk/arm64/arm_vm_init.c:105】。因此"slide 在运行期被重新随机化"或"攻击者可改写 slide 使校验失效"这两种说法都不成立；slide 是启动期一次性确定的常量。

**覆盖与不覆盖**：`vm_kernel_slide` 覆盖的是内核与 kext 镜像的**地址布局**，它不提供页权限、不提供代码完整性校验，也不替代 pmap 与代码签名监视器的职责（交叉引用内核 VM 章节与第 15 章 SPTM/TXM）。随机化只让"目标地址不可预先知晓"，而签名校验与受保护页机制负责"改动了会被发现并终止"；把二者写作同一道防线属于口径合并。

| 符号 | 所在文件:行 | 属性 | 报告中的正确表述方式 |
|---|---|---|---|
| `vm_kernel_slide` | `osfmk/arm64/arm_vm_init.c:105` | `SECURITY_READ_ONLY_LATE` | 内核 slide 的正式名（源码事实） |
| `vm_kernel_base/top` | `osfmk/arm64/arm_vm_init.c:98-110` | 同组注释区 | 内核镜像区间 |
| `vm_kext_base/top` | 同上 | 同组注释区 | kext 区间 |
| `vm_kernel_stext/etext` | 同上 | 同组注释区 | 内核文本起止 |
| `vm_kernel_slid_base/top` | 同上 | 同组注释区 | 与 slide 取值区间相关（精确语义本次未逐条核实），勿与内核镜像区间混用 |

## 10.3 slide 不是内核自选：iBoot 装载阶段与两条反推算式

**负责什么**：本节回答"随机量究竟由谁产生"。常见叙述是"内核在启动时自己挑选一个 slide"，源码给出的却是相反分工：**slide 由 iBoot 在把内核装入内存时决定，内核只是反推出自己被打到了哪里**。

arm64 侧的入口是 `osfmk/arm/arm_init.c:231` 的 `arm_slide_rebase_and_sign_image()`。该函数按内核镜像的组织方式走两条不同算式【一手-源码 osfmk/arm/arm_init.c:246, 286, 305】：

| 内核镜像形态 | 源码算式（照录） | 位置 | 读法 |
|---|---|---|---|
| MH_FILESET（文件集式 kernelcache，现代 arm64 形态） | `slide = (uintptr_t)k_mh - seg->vmaddr;` | `arm_init.c:246` | 用 Mach-O 头实际落地址减去段链接期 `vmaddr` |
| 静态 kernelcache | `slide = (uintptr_t)k_mh - VM_KERNEL_LINK_ADDRESS;` | `arm_init.c:286` | 用固定链接地址作基准 |

两条路径的终点相同：`arm_init.c:305` 执行 `vm_kernel_slide = slide;`，把反推结果写入前述只读段。

**在哪一层强制 / 生效时机**：随机化实际发生在 **iBoot→kernel 交接口之前的镜像装载阶段**，而不是内核 VM 子系统初始化阶段。内核启动早期所做的只是"已知自己被放在哪、链接期地址是多少，因此位移是多少"的算术还原，随后再据此 rebase 与签名内部指针。这一点对威胁建模有直接含义：

- 决定 slide 的代码在**内核之外**（iBoot），因此 slide 的随机性来源与熵估计都必须回到启动链那一侧去讨论，而那一侧 Apple 未开源；
- 内核内的 `arm_vm_init.c` 后续内存映射（文本只读、kext 区间保护等）是**消费**已定的 slide，而不是产生它；
- 因为 slide 在装载期就固定且写入只读段，运行期任何"重新随机化"的说法均无源码依据。

**覆盖与不覆盖**：反推算式覆盖"内核自身与随其装载的 kext 文本的虚拟地址布局"。它**不覆盖**：内核堆（kalloc/zone，见内核内存章节）、`mach_voucher` 等运行期分配、线程栈、以及用户态进程的 ASLR（后者是 APSG 唯一正面记载的那一项，`apsg.txt:5640-5650`）。把这些统称为"KASLR 覆盖全内核"是越界表述；源码可支持的口径只是"内核与 kext 镜像整体位移"。

**误解校正（装载阶段的主体）**

- 常见说法："XNU 内核在启动时随机选取自己的加载地址。"
- 正确口径：slide 由 iBoot 在装载内核时决定，内核仅在启动早期反推并写入 `SECURITY_READ_ONLY_LATE` 的 `vm_kernel_slide`。
- 依据层级：`osfmk/arm/arm_init.c:231/246/286/305` 一手源码；iBoot 侧无公开源码，故"iBoot 如何取值"属未公开。

## 10.4 Kernel Integrity Protection 原文与「同一机制」的边界

**负责什么**：Kernel Integrity Protection（KIP）负责的是**物理内存写权限**：内核与 kext 的文本页放入一块由内存控制器守护的物理区域，普通设备与内核路径之外的写入被硬件拒绝。它是"改不动"，slide 是"找不到"，两者互补但在 Apple 的文档里并不合并。

APSG 关于 KIP 的一手原文可引（§"Kernel Integrity Protection"，`apsg.txt:2716-2726`）【一手-文档 apsg.txt:2716-2726】：

> "After the operating system kernel completes initialization, Kernel Integrity Protection (KIP) is enabled… The memory controller provides a protected physical memory region that **iBoot uses to load the kernel and kernel extensions**."

加粗部分对本章有两处直接价值：其一，Apple 自己写明**内核与 kext 是由 iBoot 装载进这块受保护物理区的**，这与 10.3 的"slide 在 iBoot 装载阶段确定"互相印证；其二，KIP 的表述语境是"受保护物理区"，它没有说这个区域的基地址是随机的，也没有把 KIP 与地址随机化描述成同一件事。

**必须守住的边界**：APSG **未把 KIP 与地址随机化写成同一机制**。因此报告里可以并列陈述"同一份文档同时记载了 KIP 与进程 ASLR"，不可以写"KIP 通过随机化内核地址来起作用""KIP 即 Apple 版 KASLR"这类合并句。收口写法建议为：

> APSG 写明存在一块由 iBoot 用于装载内核与 kext 的受保护物理区（KIP，August 2026 版原文），但 Apple 未把 Kernel Integrity Protection 与内核地址随机化写成同一机制；内核地址位移的口径只存在于 XNU 源码（`vm_kernel_slide`）。

**在哪一层强制 / 生效前提**：KIP 由内存控制器在**内核完成初始化之后**启用（原文明确 "After the operating system kernel completes initialization"），这留下一个可讨论的时间窗：启用前文本区是否已受保护，Apple 未公开细节，属报告不能填充的缺口。而 slide 的确定远早于此——它在装载时就已发生。两条时间线不可互换。

**覆盖与不覆盖**：KIP 覆盖物理区的写保护，不覆盖指针认证、不覆盖代码签名校验（那属于 PPL/SPTM 与代码签名监视器的职责，交叉引用代码签名章节与第 15 章）。地址随机化覆盖地址可预测性，不覆盖任何权限判定。本章只主张"两套机制在启动链上相邻"，不主张因果。

## 10.5 零售设备的可观测面：`kern.slide` 的是/否布尔

**负责什么**：随机化机制若不留下任何可观测面，防御方就无法回答"这台设备到底随机化了没有"。XNU 在 sysctl 层给出的答案非常克制：一个**只读的是/否布尔**，而不是数值。

源码事实【一手-源码 bsd/kern/kern_sysctl.c:4313, 4329-4330】：`kern_sysctl.c:4313` 的注释原文是 "Boolean indicating if KASLR is active."，紧随其后 `:4329-4330` 注册

```c
SYSCTL_PROC(_kern, OID_AUTO, slide, CTLTYPE_INT | CTLFLAG_RD …)
```

其 handler 返回的是"slide 是否非 0"这一判定结果。更细的暴露面则被编译条件挡住：`kern.kernel_slide`（具体位移值）与 `kern.kernel_text_exec_base*` 系列仅在 `#if DEBUG || DEVELOPMENT` 下编译【一手-源码 bsd/kern/kern_sysctl.c:4332-4351】。

由此得到本章关于可观测性的收口口径：**零售 iOS 上可读的只有 `kern.slide` 的是/否布尔值，不是具体 slide 值**；`kern.kernel_slide` 等在零售（RELEASE）构建中不会被编译进去。任何"从设备上读出内核 slide 数值"的写法，如果指向零售 iOS，就没有一手依据支撑；如果它读到的其实是崩溃记录里的某个字段，则必须另行标注（见下）。

| 暴露面 | 形态 | 零售 iOS 可见性 | 依据 |
|---|---|---|---|
| `kern.slide` | sysctl，只读 | 可见，且只是"非 0 与否" | 【一手-源码 `kern_sysctl.c:4329-4330`】 |
| `kern.kernel_slide` | sysctl，具体位移 | 不可见（`#if DEBUG \|\| DEVELOPMENT`） | 【一手-源码 `kern_sysctl.c:4332-4351`】 |
| `kern.kernel_text_exec_base*` | sysctl，文本基址 | 不可见（同上编译条件） | 【一手-源码 `kern_sysctl.c:4332-4351`】 |
| 崩溃记录 `translation` 字段 | panic 分析数据字段 | 语义未定（见下段） | 【未确证；Apple 未定义其语义】 |

**关于 `translation`**：iOS/macOS 分析数据的 panic 记录中确实被广泛引用有一个 `translation` 字段，但**Apple 未公开定义其语义**；把它等同于"内核 slide"属研究界推断，不是官方口径。本报告只能写成：`translation`（Apple 未公开定义其语义；把它等同于内核 slide 属研究界推断）。至于"`kern.slide` 之外是否还有其他零售可见的 slide 暴露面"，本次未获得可核实条目，不得反向断言不存在。

**在哪一层强制 / 生效前提**：sysctl 注册在内核 BSD/sysctl 层，读取需相应权限；`CTLFLAG_RD` 决定它不可写，因此"通过 sysctl 关掉随机化"这条路本身在源码层就被排除。

## 10.6 boot-args 考证、误解校正与随机强度的公开缺口

本节只处理一个考证问题：**"内核 slide 可以用 boot-args 关闭"这一流行说法，是否有 Apple 一手依据**。答案是分层的，且每一层都要写清依据等级。这里不给出任何可执行的参数设置流程。

**源码层（一手，负面结果）**：XNU 中**不存在** `PE_parse_boot_argn("slide"/"kaslr"/"kaslider")` 这类解析——arm64 路径没有 slide/kaslr 族 boot-arg 的读取点。全树检索能落到的只有两类残留：`OSKext.cpp` 里的 `kaslr:` 日志字符串，以及 x86 路径上对 `boot_args.kslide` **结构体字段**的判断【一手-源码 osfmk/i386/i386_vm_init.c:284-293】：当 `if (args->kslide)` 为假时打印 "KASLR slide: … **static and ignored**" 并 `vm_kernel_slide = 0`。关键区别在于：这是 **`boot_args` 结构（由 boot 加载器填充给内核的机器相关参数块）里的一个字段**，不是 boot-args NVRAM 字符串上的一个可写选项，而且它属于 x86（Intel Mac）路径，与 arm64 iOS 无关。

**二手文档层（L2，非 Apple 一手）**：第三方工具厂商文档（Hex-Rays《Debugging the XNU Kernel with IDA Pro》，https://docs.hex-rays.com/core/debugger/tutorials/xnu_debugger_primer ）确有以 `slide=0` 形式传入的参数写法，用途是 macOS 调试场景。就此必须限定：**macOS/调试环境用法，来自第三方厂商文档（Hex-Rays，L2），非 Apple 公开文档记载；iOS 零售设备无公开可关闭途径。**

**Apple 关于 boot-args 的一般约束（一手可引）**：APSG §"System Integrity Protection (sip3)" 原文（`apsg.txt:2128-2135`）【一手-文档 apsg.txt:2128-2135】：

> "If sip3 is present and true, **iBoot won't enforce its built-in allow list for the boot-args NVRAM variable**, which would otherwise filter the options passed to the kernel."

这句话可支撑两条结论：① boot-args 有 **iBoot 内建白名单**过滤，不是任意字符串都能到达内核；② 该表述在 APSG 中属 macOS/SIP-LocalPolicy 语境，**iOS 零售设备是否有等价公开路径，Apple 未公开**（不能据此推导 iOS 上存在同类放行方式，也不能断言其不存在）。

**误解校正（三条）**

| 常见说法 | 正确口径 | 依据层级 |
|---|---|---|
| "iOS 可用 boot-args（如 `slide=0`/`kaslider=off`）关闭内核随机化" | arm64 源码无该类 boot-arg 解析；`slide=0` 出自第三方文档的 macOS 调试用法，非 Apple 一手口径；iOS 零售设备无公开可关闭途径 | 【一手-源码 检索为负面结果】【L2 Hex-Rays】 |
| "x86 的 `kslide` 证明 Apple 提供了公开的 KASLR 开关" | `boot_args.kslide` 是加载器填充的结构字段，不是 NVRAM boot-args 选项，且属 x86 路径 | 【一手-源码 i386_vm_init.c:284-293】 |
| "每次启动都会换一个 slide，随机强度约 N bit" | 源码显示 slide 在每次启动装载时确定并写入只读段；**Apple 未公开其熵值与分布**，故不可给出 bit 数，也不可断言"每次都换" | 【一手-源码 arm_init.c:305】+ 缺口 |

**随机性本身的公开缺口**：`vm_kernel_slide` 由闭源的 iBoot 决定，其取值范围、熵源、分布形态，以及"是否每次冷启动必然变化"，Apple 均未公开记载。本报告能负责任地写出的只有：随机化发生在 iBoot→kernel 交接口的镜像装载阶段、结果写入只读段、零售设备只能通过 `kern.slide` 读到"是否生效"。

**本章口径边界**

- 一手可断言：`vm_kernel_slide` 及同组变量与 `SECURITY_READ_ONLY_LATE` 属性【`arm_vm_init.c:98-110`】；两条 slide 反推算式与写入点【`arm_init.c:246/286/305`】；`kern.slide` 布尔与 DEBUG/DEVELOPMENT 限定【`kern_sysctl.c:4313-4351`】；`KASLR` 三处源码原文；KIP 与 sip3 两段 APSG 原文。
- 必须并列两侧原文再收口：KASLR 术语（Apple 文档 vs 源码，见 10.1）；DriverKit 类平台口径冲突不在本章，但本章同样适用"prose/metadata 不合并"的纪律。
- 未公开（不得反向断言）：slide 熵值与分布；零售 iOS 上影响 slide 的公开途径；`kern.slide` 之外的其他零售可见暴露面；`translation` 字段语义（等同内核 slide 属研究界推断）。
- 禁止写法：把 "KASLR" 当 Apple 官方术语；把 KIP 与地址随机化写成同一机制；把第三方文档的 `slide=0` 写成 iOS 可用能力或操作步骤；写"每次启动换一个 slide"的定量结论。

---

# 指针认证：PAC 的键分配与覆盖面

## 11.1 Apple 的官方表述：五个 128-bit 秘密值与每进程 B keys

**负责什么**：Pointer Authentication Codes（PAC）把一个可执行/可解引用的指针连同其上下文一起，用一个秘密密钥算出校验码并编码进指针的高位空闲比特；解引用前认证，认证失败即产生异常。它负责的是"指针未被偷偷改写"，而不是"指针指向的页是否可执行"。

APSG（August 2026）关于 PAC 的正式段落原文逐字如下【一手-文档 apsg.txt:2759-2764】：

> "Pointer Authentication Codes (PACs) are used to protect against exploitation of memory corruption bugs. **System software and built-in apps use PAC to help prevent modification of function pointers and return addresses (code pointers). PAC uses five secret 128-bit values to sign kernel instructions and data, and each user space process has its own B keys.** Items are salted and signed as indicated below."

这段原文里有四个可以直接进入报告的口径，且每一个都限定了适用范围：

| 原文要点 | 可支撑的结论 | 不可支撑的写法 |
|---|---|---|
| "System software and built-in apps" | PAC 覆盖系统软件与内建 App | 不能推出"所有第三方 App 都同等覆盖"（该句未提第三方） |
| "function pointers and return addresses (code pointers)" | 代码指针（含返回地址）是官方点名的保护对象 | 不能据此认为只保护代码指针——同节表格列有数据指针（见 11.2） |
| "five secret 128-bit values to sign kernel instructions and data" | Apple 官方承认内核态启用 PAC，且**指令与数据都签** | "PAC 只在用户态生效"不成立 |
| "each user space process has its own B keys" | 用户态 per-process 密钥隔离由 B 键承担 | 不能改写为"每个进程有独立的五个密钥" |

APSG 卷首还把 PAC 与 KIP、Fast Permission Restrictions 并列为 Apple silicon 上的安全能力【一手-文档 apsg.txt:212-214】："security features powered by Apple silicon--such as Kernel Integrity Protection, **Pointer Authentication Codes**, and Fast Permission Restrictions--help thwart common types of exploitation."——值得注意的是：**这份列表里并没有 BTI 与 GCS**（该负面结果由第 12 章展开）。

**在哪一层强制**：PAC 由 CPU 的 PAuth 扩展在解引用指令处强制，签名值藏在指针高位；密钥保存在系统寄存器中。XNU 源码进一步显示，在启用虚拟化的配置下这些密钥的写入被交由更高特权级托管（见 11.5），也就是"内核自己也不能随意改自己的密钥状态"。

**生效前提**：目标二进制必须被编译/汇编为使用 PAC（arm64e ABI），并受 salt 与上下文参与计算；未被签名的指针、以数据形式复制的指针不获得同等保护。Apple 未公开"哪些零售系统组件已完整 arm64e 化"的清单，该覆盖面在报告里属缺口。

## 11.2 Apple 官方键-用途表照录及其版面限制

APSG 在 PAC 段落之后给出 **"Items are salted and signed as indicated below"** 的表【一手-文档 apsg.txt:2765-2786】。照录如下（Item 与 Key 列按原文，Salt 列为原文摘要）：

| Item（Apple 原文） | Key | Salt（Apple 原文摘要） |
|---|---|---|
| Function Return Address | **IB** | 0；盐为 storage address |
| Function Pointers | **IA** | storage address；`storage address + Class + Selector` |
| Block Invocation Function | **IA** | `storage address + 0xC0BB`；`storage address + 0x6AE1` |
| Block Descriptor Pointer | **DA** | `storage address + 0xB5AB`；`storage address + 0x57C2` |
| Objective-C Method Cache | **IB** | `storage address + 0x61F8` |
| Objective-C Isa Pointer | **DA** | `storage address + Hash (mangled method name)` |
| Objective-C Super Pointer | **DA** | `storage address + Hash (mangled base V-Table name)` |
| Selector Typed Objective-C ivars | **DB** | 该行 Salt 列在 PDF 文本层提取中为乱码字符（版面待核；提取中出现近似 `Hash (function name)` 的残迹） |
| Objective-C Read-only Class Data Pointer | **DA** | storage address |
| C++ V-Table Entries | **IA** | — |
| C++ V-Table Pointers | **DA** | — |

**必须随表附带的版面声明**：本表文字取自本地 `apsg.pdf` 的**文本层提取**，Item/Key/Salt 三列的换行归属系依据提取文本的行序判断，**PDF 版面未逐格复核**；尤其 "Selector Typed Objective-C ivars" 一行的 Salt 列在提取过程中为乱码，其准确取值须以版面复核为准。报告中引用该表时应写"据 Apple Platform Security 的 PAC 表格"，并把上述限制一并保留，不要把它当作逐格无争议的机器可读数据。

**读表要点（机制层面，可安全断言）**：

1. **键的分工是有语义的**：代码指针类条目落在 IA/IB，数据/对象指针类条目落在 DA/DB——这正是"五个 128-bit 秘密值"里 A 族与 B 族、code 与 data 的四象限（`IA/IB/DA/DB`）。**本表只出现这四族**，第五个 128-bit 秘密值在该表中没有列出对应条目；它是否被 Apple 用于某类指针，公开源码未披露（见 11.6 的 `APGA`/`GCCKEY` 红线）。
2. **salt 参与签名**意味着"同一指针值在不同存储位置上的签名不同"，因此把一个合法签名的指针原样搬到另一个字段（同一进程内）会认证失败。这是"条目按表加盐签名"这句官方话的实际含义。
3. **返回地址与 ObjC 方法缓存同走 IB**，而方法缓存本身是数据——按 ARM 习惯把它归为"数据指针"会读错表；Apple 的归属口径以本表为准。
4. 表中 **`Function Pointers` 行给出两种 salt 形态**（单纯 storage address，与 storage address + Class + Selector），对应"普通函数指针"与"带 selector 语义的函数指针"两类场景，报告里不宜压缩成一句"函数指针用 IA 加地址盐"。

**覆盖与不覆盖**：该表覆盖的是 Apple 点名的这些具体条目类型，它不是一份穷尽清单——表未列出的指针（例如内核内部各类链表的裸指针、非 ObjC/C++ 的自管理句柄）是否签名，Apple 未公开。把表格内容写成"PAC 只保护这 11 类东西"或反过来写成"所有指针都被签名"，都超出依据。

## 11.3 源码侧互证：`ptrauth.h` 枚举与 `pacibsp`/`retab` 常量

**负责什么**：本节的任务是把 11.2 的官方表"落到可实现"——编译器与内核到底用哪个键名、哪条指令去签与验。两份一手材料互证，且都不依赖任何第三方解读。

其一是 `EXTERNAL_HEADERS/ptrauth.h`（Apple 随 XNU 发布的 clang 头）中的枚举与别名映射【一手-源码 EXTERNAL_HEADERS/ptrauth.h:30-90】：枚举 `ptrauth_key_asia = 0, asib = 1, asda = 2, asdb = 3`，随后一组 `#define` 把语义名字挂到这四个键上：

| `ptrauth.h` 中的语义名 | 映射到 | 等价 AArch64 键 | 与 APSG 表的对应 |
|---|---|---|---|
| `ptrauth_key_process_independent_code` | `asia` | IA | Function Pointers → IA |
| `ptrauth_key_process_dependent_code` | `asib` | IB | Function Return Address / Method Cache → IB |
| `ptrauth_key_process_independent_data` | `asda` | DA | Isa/Super/V-Table Pointer/Block Descriptor → DA |
| `ptrauth_key_process_dependent_data` | `asdb` | DB | Frame Pointer；Selector Typed ivars → DB |
| `ptrauth_key_function_pointer` | `process_independent_code` | IA | Function Pointers → IA |
| **`ptrauth_key_return_address`** | **`process_dependent_code`** | **IB** | Function Return Address → **IB** |
| `ptrauth_key_frame_pointer` | `process_dependent_data` | DB | （APSG 表未单列该条目） |
| `ptrauth_key_block_function` | `asia` | IA | Block Invocation Function → IA |
| `ptrauth_key_cxx_vtable_pointer` | `asda` | DA | C++ V-Table Pointers → DA |

其二是真正的机器指令证据。`osfmk/arm64/corecrypto/ccarm_pac_bti_macros.h:52-64` 在 `__arm64e__` 条件下给出两个宏展开【一手-源码 osfmk/arm64/corecrypto/ccarm_pac_bti_macros.h:52-64】：`SIGN_LR` 展开为 **`pacibsp`**，`AUTH_LR_AND_RET` 展开为 **`retab`**。同一文件里另有 `hint #34 /* bti c */`（该条与第 12 章共用，见 12.3）。

这两条汇编常量是本章最硬的一手证据：**函数返回时用的是"以 B 键、以 SP 作 discriminator 认证并返回"的那条指令**，与 APSG 表里 "Function Return Address → IB" 完全对齐。也就是说，官方文档与内核汇编在这一点上是**同一条结论的两个来源**，报告可以放心把二者并列引用，而不必只挑一个。

**读法上的三条限制**：

- `pacibsp`/`retab` 只在 `__arm64e__` 分支展开为真实指令；非 arm64e 目标上这两个宏退化为空操作，因此"看到宏名"不等于"该构建一定启用了 PAC"，构建形态仍是决定项。
- `ptrauth.h` 的 `ptrauth_key_frame_pointer = process_dependent_data`（DB）这一条在 APSG 表中**没有对应行**——引用时应说明它来自头文件，不要写成"Apple 文档规定帧指针用 DB"。
- 表中 `C++ V-Table Entries`（IA）与 `C++ V-Table Pointers`（DA）是两类不同对象：表项内容（函数地址）用 IA 签，指向虚表的指针本身用 DA 签；`ptrauth_key_cxx_vtable_pointer = asda` 只对应后者。把二者合并成"vtable 用 DA"会丢一半。

**在哪一层强制**：签名与认证发生在编译期插入的指令上（prologue/epilogue、解引用点），由 CPU 执行；内核不参与逐次校验。这意味着 PAC 的保护**不依赖页权限或内存管理层的配合**，但反过来，它也不防御"签名的指针指向了错误对象"这类语义级缺陷。

## 11.4 误解校正：返回地址走 IB，不是 APIA/PACIA+RETAA

这是一处典型的**跨生态口径污染**，也是本报告最容易在评审时被挑出的错误，因此单独成节。

**常见说法**："Apple 用 APIA 密钥（PACIA）签名返回地址，函数退出时用 `RETAA` 认证"，或直接写"prologue 里 `pacia`、epilogue 里 `retaa`"。这种写法在 ARM 通用文档、Linux/Android 生态的 PAuth 讨论里是**正确的**，因为那些系统选择 A 键来保护返回地址；把它照搬到 Apple 平台即为错。

**正确口径**：Apple 平台上函数返回地址走 **key B**，即 **IB（PACIBSP / RETAB）**。三条一手依据同向：

| 来源 | 原文/内容 | 层级 |
|---|---|---|
| APSG PAC 表 | "Function Return Address → **IB**" | 【一手-文档 apsg.txt:2765-2786】 |
| `EXTERNAL_HEADERS/ptrauth.h` | `ptrauth_key_return_address = ptrauth_key_process_dependent_code`，而 `process_dependent_code = asib`（枚举值 1） | 【一手-源码 ptrauth.h:30-90】 |
| `ccarm_pac_bti_macros.h` | `SIGN_LR` → `pacibsp`；`AUTH_LR_AND_RET` → `retab` | 【一手-源码 ccarm_pac_bti_macros.h:52-64】 |

**冲突双方的原文并列**（本报告处理口径冲突的统一方式）：

- Apple 侧："Function Return Address" 对应 **IB**；内核汇编以 `pacibsp` 签、以 `retab` 认证返回。
- AArch64 架构/非 Apple 生态侧：架构**同时**定义 `RETAA` 与 `RETAB` 两条合法指令，Linux/Android 的 PAuth 惯例常用 **A key（`PACIA` + `RETAA`）**。
- 收口写法：**"Apple 返回地址走 IB（`PACIBSP`/`RETAB`）；`RETAA` 是 AArch64 架构的另一条合法指令、被非 Apple 生态常用，不可套用为 Apple 口径。"**

**依据层级与为什么重要**：三条依据全部是一手（一份官方 PDF 表格 + 两份 Apple 源码），不存在"只能靠推断"的环节。键选错的后果不只是名称不准：它会让后续所有分析（如何判定一次 PAC 失败属于 ROP 防护还是 JOP 防护、哪个键是 per-process 的、内核态用哪个键签数据指针）连带错位。11.5 的 `pac_asm.h` 注释正把 **B 键称为 "ROP key"**、**A 键称为 "JOP key"**，与"返回地址走 B"形成闭合自洽；若按 A 键口径写返回地址，就与这条注释互相矛盾。

**衍生的三条禁止表述**：① 不得写"iOS 内核用 PACIA 签名 LR"；② 不得把 `RETAB` 描述为"某种变体/别名"，它是架构里与 `RETAA` 并列的另一条指令，Apple 选它；③ 引用非 Apple 来源（工具、论文）中的 `retaa` 片段来"印证 iOS 行为"时，必须显式标注那是另一生态的编译配置，不是 Apple 的。

**生效前提与覆盖面提示**：上述 IB 口径在 arm64e 代码路径上成立；`SIGN_LR`/`AUTH_LR_AND_RET` 在非 `__arm64e__` 分支为空展开，故具体构建是否全面启用 PAC，Apple 未公开组件级清单。

## 11.5 内核态启用：EL1 A key、JOP/ROP 命名与虚拟化路径

**负责什么**：本节确定三件事——内核态是否真的启用 PAC、内核内部如何称呼这几个键、以及在启用虚拟化的平台上谁掌握密钥状态。

一手依据是 `osfmk/arm64/pac_asm.h`。该文件 `:50-110` 的注释里出现 Apple 的内部命名 **"JOP key"** 与 **"ROP key"**，并与 `task` 结构字段 `task->jop_pid`、`task->rop_pid` 对应；关键的原文是（`pac_asm.h:103-108`）【一手-源码 osfmk/arm64/pac_asm.h:103-108】：

> "the EL0 diversifier affects **both A (JOP) and B (ROP) keys**. We don't want SET_JOP_KEY_REGISTERS to have an impact on the **EL1 A key state, since these are the keys the kernel uses to sign pointers on the heap**."

这段注释可以直接支撑三条硬结论，且三条都属于源码事实而非 Apple 面向用户的表述（引用时须如此标注）：

1. **A = JOP、B = ROP 的分工**：Apple 在代码里把 A 族键的职责称作 JOP key（面向跳转类），B 族称作 ROP key（面向返回类）。这与 11.4 的"返回地址用 IB"完全一致，也让"哪个键管什么"有了一手锚点。
2. **内核态确实启用，并且覆盖数据不只代码**：原文说明 **EL1 A key 是内核用来签名堆指针的键**（"the keys the kernel uses to sign pointers on the heap"）。也就是说，内核堆上的**数据指针**同样处于 PAC 保护下；"PAC 只保护代码指针"这一常见预期在此被源码推翻。它与 APSG 表把 isa/super/vtable pointer/Block Descriptor 归到 DA/DB 相互印证，两条线各自独立可用【一手-文档 apsg.txt:2759-2786 + 一手-源码 pac_asm.h:103-108】。
3. **EL0 diversifier 实现 per-process 隔离**：`pac_asm.h` 描述的 EL0 diversifier 同时影响 A 与 B 两族键的用户态状态，对应 APSG 的"each user space process has its own B keys"；`jop_pid`/`rop_pid` 两个字段即"按进程派生"的载体。官方文档与源码在"每进程密钥不同"上口径一致。

**虚拟化路径（与 SPTM/TXM 呼应）**：同一文件使用 `VMAPPLE_PAC_SET_EL0_DIVERSIFIER` 与 `VMAPPLE_PAC_SET_B_KEYS` 两类 **HVC** 调用来完成键与 diversifier 的设置【一手-源码 osfmk/arm64/pac_asm.h】。机制含义是：在启用虚拟化监控的平台上，**PAC 密钥状态的写入被交由更高特权级托管**，内核表达"我要设成什么"，由更高特权层执行与校验。这与 APSG 关于 SPTM/TXM 的一节（`apsg.txt:2818-2830`）方向一致，构成"PAC 的信任根不在内核自己手里"这一层的可引证据；该主题的完整展开归入第 15 章（SPTM/TXM），本章只做接口层面的呼应，不重复其结论。

**失效模式与可观测性**：PAC 校验失败是**同步异常**——认证指令当场产生异常，而不是异步的错误标志。源码树内还有面向故障描述的 dtrace 脚本 `bsd/dev/dtrace/scripts/ptrauth_arm64.d`（存在性可核），`osfmk/kern/exception.c` 侧则定义 `TUNABLE(bool, pac_exception_telemetry, "-pac_exception_telemetry", false)` 与 `CA_EVENT(pac_exception_event, CA_INT exception, CA_INT exception_code_0, CA_INT exception_code_1, …)`（`:791-800`）。可以断言的是"机制与遥测事件定义存在于源码"；**是否出现在零售 iOS 的公开日志中，Apple 未公开**，须加限定，也不可反向断言"零售日志一定看不到"。至于"PAC 违规会显式写在 panic 字符串里"，本次未获得可核实条目，只能按研究界/维修界观测处理，不进入事实层。

**覆盖与不覆盖**：本节的覆盖面结论限于"代码指针 + 内核堆数据指针 + 每进程派生"。**未覆盖/未公开**的包括：哪些零售组件已 arm64e 化、密钥的具体生成与轮换策略、PAC 的芯片与 iOS 主版本对应关系（APSG 的 SoC 特性表把 PAC 一栏以勾选图形呈现，文本层未提取，版面待核）。

## 11.6 覆盖面边界：`APGA`/`GCCKEY` 红线、数据指针旁证与失效可观测

**红线一：`GCCKEY` / `APGA` 不得写成"Apple 用 GA/GCC 键签某类指针"。** AArch64 架构除 IA/IB/DA/DB 四族外还定义 generic key（`APGA`，供 `XPACQ` 与 generic 签名使用）；APSG 那句"five secret 128-bit values"在字面上与该第五族的存在相容，但**是否启用**是另一回事。XNU 全树检索结果【一手-源码 osfmk/arm64/proc_reg.h:1254-1255, 1363-1364】：`APGA` 仅出现在 **EL2 细粒度陷阱位定义**（`HFGRTR_APGAKEY`、`HFGWTR_APGAKEY`，同批还有 `APIAKEY`、`APDAKEY` 等位），这些是虚拟化场景下"访问这些系统寄存器是否陷入 EL2"的架构寄存器常量，**不是内核使用 GA 键签名的实现**；`GCCKEY` 一词在本 XNU 树检索 **0 命中**。据此，报告的正确写法是：**"`GCCKEY`/`APGA` 是否被 Apple 用于某类指针签名，公开源码未披露（属未核实）"**——既不写成事实，也不断言其不存在。

**红线二：数据指针认证有源码旁证，但要分清是哪一层证据。** Apple 随 XNU 发布了一个专测数据指针认证的 sysctl 测试向量：`bsd/tests/ptrauth_data_tests_sysctl.c:78` 使用 `ptrauth_key_process_independent_data`（即 DA）校验数据指针，宏名 `VALIDATE_DATA_PTR`【一手-源码 bsd/tests/ptrauth_data_tests_sysctl.c:78】。这条与 11.5 的 `pac_asm.h` 注释（EL1 A key 签堆指针）、11.2 的 APSG 表（isa/super/vtable/Block Descriptor 归 DA/DB）三线并列，才能把"数据指针也受 PAC"写成**已核实**。三线各自独立，任一条不足以单独承担该结论。

**"函数指针 vs 数据指针"的分别核实结论**：

| 对象 | 官方口径 | 源码口径 | 结论 |
|---|---|---|---|
| 函数指针 | APSG 明文 "function pointers … (code pointers)"，条目 → IA | `ptrauth_key_function_pointer = process_independent_code`(asia) | 已核实（双层） |
| 返回地址 | APSG 表 → IB | `pacibsp`/`retab`；`ptrauth_key_return_address = asib` | 已核实（双层） |
| 数据指针 | APSG 表 isa/super/vtable pointer/Block Descriptor → DA、Selector ivars → DB | EL1 A key "sign pointers on the heap"；`ptrauth_data_tests_sysctl.c` 用 DA 校验 | 已核实（双层） |

**失效模式的可观测边界（复述并收口）**：PAC 认证失败为同步异常；`ptrauth_arm64.d` 在树内、`pac_exception_telemetry` tunable 默认 false、`CA_EVENT(pac_exception_event, …)` 已定义——三者是**存在性**证据。零售设备上这些事件是否进入用户可得的日志、字段格式如何，Apple 未公开；不得写"从日志可读到 PAC 违规计数"。另可交叉参考：Apple 安全公告在 CVE-2025-31201 上把组件名写为 **`RPAC`** 且 Impact 原文含 "bypass Pointer Authentication"，并标注 "Apple is aware of a report that this issue may have been exploited in an extremely sophisticated attack against specific targeted individuals on iOS"（https://support.apple.com/en-us/122282 ）——这是 Apple 一手承认"PAC 可被绕过、且存在针对性利用报告"的唯一公开锚点，组件名以逐字 `RPAC` 为准，不要改写为 `Kernel` 或 `PAC`。

**本章口径边界**

- 一手可断言：APSG 的五个 128-bit 秘密值/每进程 B keys 原文【`apsg.txt:2759-2764`】；键-用途表【`apsg.txt:2765-2786`，附版面声明】；`ptrauth.h` 枚举与别名【`:30-90`】；`pacibsp`/`retab`【`ccarm_pac_bti_macros.h:52-64`】；`pac_asm.h` 的 JOP/ROP 命名与 EL1 A key 签堆指针【`:103-108`】；HVC 路径【`VMAPPLE_PAC_SET_EL0_DIVERSIFIER`/`SET_B_KEYS`】；`ptrauth_data_tests_sysctl.c:78`。
- 未核实（必须照实写，不得反向断言）：GA/`GCCKEY` 键的用途；APSG PAC 表的逐格版面归属（含 Selector ivars 行 Salt 列乱码）；PAC 一栏在 SoC 特性表中的芯片-版本对应（勾选图形未提取）；PAC 遥测事件在零售日志中的可见性；arm64e 化的组件清单。
- 禁止写法：`RETAA`/PACIA 口径套用到 Apple 返回地址；"PAC 只保护代码指针"；把第五个键写成某个业务用途；把未公开说成"不存在"。

---

# 分支与返回地址完整性：BTI 与 GCS 的公开证据边界

## 12.1 最硬的负面结论：APSG 对 BTI/GCS 命中 0

本章从一条**负面结论**开始，因为它决定后面五节能写什么。

在 *Apple Platform Security*（August 2026，`apsg.txt` 全文，不区分大小写）检索 `BTI`、`branch target`、`GCS`、`guarded control`、`control flow integrity`：**命中 0 次**。因此：

- **"Apple 对 Branch Target Identification 的公开表述"：本次未获得可核实条目**——Apple 面向用户的官方文档未记载 BTI；
- **"Apple 是否使用 'Guarded Control Stack' 这一术语"：本次未获得可核实条目**——本报告不得写"Apple 官方称 GCS 为 Guarded Control Stack"，也不得反向断言 Apple 不用该术语；
- 凡"BTI/GCS 属于 Apple 公开列名的硅上安全特性"这类写法**必须删除**。

Apple 在同一文档的 SoC 特性表里**确实逐条列名**的机制是另一组（`apsg.txt:317-325`、`:2689-2704`，表头分列 A10 / A11,S3 / A12-A14,S4-S10 / A15-A18 / M1 / M2-M4,M5 / A19）【一手-文档 apsg.txt:317-325, 2689-2704】：

| Apple 公开列名的硅上机制 | 本章/他章处理 |
|---|---|
| Kernel Integrity Protection | 第 10.4 节（与随机化的边界） |
| Fast Permission Restrictions | 内存权限章节（Apple 列名，非本章主题） |
| System Coprocessor Integrity Protection (SCIP) | Apple 列名；Coprocessor 侧展开不在本章 |
| Pointer Authentication Codes | 第 11 章 |
| Page Protection Layer (PPL) | 代码签名/内存章节 |
| Secure Page Table Monitor (SPTM) | 第 15 章 |
| Memory Integrity Enforcement with Enhanced Memory Tagging Extension (MIE-EMTE) | Apple 列名，芯片口径明示（A19/M5+） |

BTI 与 GCS **都不在这张表里**。这不是"源码里没有"——恰恰相反，BTI 在 arm64 内核里是有实现的（12.2、12.3）——而是"公开文档层面没有对应口径"。二者的差别必须写清：**机制在源码里存在（一手可证），Apple 面向用户的定性、开关取值与芯片对应关系未公开**。

**收口写法建议**（可直接采用）：

> Apple 面向用户的安全文档未记载 BTI 与 GCS（全文检索命中 0），其公开列名的硅上特性为 KIP、Fast Permission Restrictions、SCIP、PAC、PPL、SPTM 与 MIE/EMTE；BTI 在 XNU arm64 源码中存在实现，GCS 在该树内没有任何使能实现。本章据此把 BTI 写成"源码事实 + 构建开关未公开"，把 GCS 写成"公开证据缺失"。

**负责什么（架构侧口径，须标注非 Apple）**：按 AArch64 架构规范，Branch Target Identification（FEAT_BTI）约束**间接分支的落点**——以非 guarded 页上的 `BR/BLR` 跳入 guarded 页会被拒绝，从而压缩 JOP/ROP 可用的 gadget 入口；Guarded Control Stack（FEAT_GCS）提供**硬件影子栈**，让返回地址的存放位置对普通内存写不可见。这两句属**架构侧口径**，不是 Apple 的表述，正文引用时必须如此标注。

## 12.2 BTI 的源码实现：`BTI_ENFORCED`、guarded 页与 `ARM_PTE_GP`

**负责什么**：内核 VM 层负责把"哪些页是 guarded 页"这一判定编码进页表；BTI 的执行由硬件在间接分支目标检查时读取该位。XNU 侧的实现路径清晰可指认。

**页表位与语义（一手）**：`osfmk/arm64/arm_vm_init.c:988-990` 的结构体位域注释原文【一手-源码 osfmk/arm64/arm_vm_init.c:988-990】：

> "unsigned int guarded => flag indicating whether this range should be considered an ARM \"guarded\" page. **This enables BTI enforcement for a region.**"

映射时按编译开关落位（`arm_vm_init.c:1075-1082`）：`#if BTI_ENFORCED … ptmp |= ARM_PTE_GP;`。`ARM_PTE_GP` 定义为 `0x0004000000000000ULL /* value marking a guarded page */`【一手-源码 osfmk/arm64/proc_reg.h:2307】。可见"是否给某段加 BTI 保护"在这份源码里是**编译期决定 + 页表位承载**的组合，而不是运行期策略。

**被标为 guarded 的内核区间（一手）**：内核只读可执行文本段在建立映射时传入 `ARM64_PAGE_GUARDED`（该宏定义于 `arm_vm_init.c:375`），调用点覆盖四段【一手-源码 osfmk/arm64/arm_vm_init.c:1404, 1441, 1462, 1466】：

| 段 | 传入点 | 语义 |
|---|---|---|
| PLKTEXT | `arm_vm_init.c:1404` | 内核只读可执行文本（PLK 区） |
| `__TEXT_EXEC` | `arm_vm_init.c:1441` | kernelcache 文本执行段 |
| KLD | `arm_vm_init.c:1462` | kext 装载相关区间 |
| `__LAST` | `arm_vm_init.c:1466` | 内核镜像尾段 |

**关键限定（必须落到句子里）**：`BTI_ENFORCED` 在本公开源码树中**没有任何定义处**——它只以 `#if BTI_ENFORCED` 的形式被使用，取值来自不公开的内部构建配置。因此"零售 iOS 构建是否打开 BTI 强制"**无法从公开源码判定**。本报告统一采用："**源码具备 BTI 强制能力（`ARM_PTE_GP` guarded 页 + `bti c/j` 垫片），但构建开关 `BTI_ENFORCED` 的取值 Apple 未公开，零售 iOS 是否强制无法从公开源码判定。**"既不能写"iOS 内核全面启用了 BTI"，也不能写"iOS 未启用 BTI"。

**在哪一层强制 / 生效前提**：判定在 pmap/页表层落位、由 CPU 在间接分支时执行；生效前提是（1）构建时 `BTI_ENFORCED` 为真，（2）该页确实被标 `ARM_PTE_GP`，（3）目标指令序列以 BTI landing pad 开头（见 12.3）。三者缺一，"BTI 保护该段"就不成立。

**覆盖与不覆盖**：从上述调用点看，可断言覆盖的是**内核只读可执行文本区**；不覆盖的是**未标 guarded 的区间**，其中最典型、也是 Apple 自己写明的一处例外即 AuxKC 文本（12.5）。数据页、堆、以及第三方（macOS 语境）kext 文本不在本节可断言的覆盖范围内。

## 12.3 落地垫片与 PPL 入口：`bti c`/`bti j`/`hint #34`

**负责什么**：guarded 页只回答"这里检查不检查"，**能否被合法跳入**取决于目标地址处是否有一条 BTI landing pad 指令。这一节的源码事实是：XNU 在汇编层把 landing pad 作为宏垫片纳入了入口约定。

可指认的三处【一手-源码】：

| 位置 | 内容 | 作用 |
|---|---|---|
| `osfmk/arm64/asm.h:222-241` | 汇编入口宏中的 `bti c` / `bti j` 垫片 | 为函数入口（`bti c`：仅允许作为调用目标）与分支目标（`bti j`：允许 call 与 jump）声明合法落点 |
| `osfmk/arm/pmap/pmap_internal.h:92-96` | `PMAP_SUPPORT_PROTOTYPES_BTI_LANDING_PAD`，展开为 `"bti c\n"` | **PPL（Page Protection Layer）原型入口**显式插入 landing pad |
| `osfmk/arm64/corecrypto/ccarm_pac_bti_macros.h:64` | `hint #34 /* bti c */` | corecrypto arm64 汇编以架构提示编码形式写 `bti c`（`hint #34` 即 `bti c` 的编码形式），并与 11.3 的 `pacibsp`/`retab` 同处一文件 |

三条读法：

1. **PPL 入口专门做 landing pad** 说明这不是零散写法。`pmap_internal.h` 属于 pmap 内部，其宏名直接把"BTI landing pad"纳入 PPL 原型的入口约定，可在报告里作为"内核安全子系统自身的入口已按 BTI 语义整理"的一手证据。
2. `bti c` 与 `bti j` 的**区分本身是信息**：内核为不同入口类型选择不同 pad，意味着若某段最终被标 guarded，只有相应类型的间接转移才被允许落入。把两者统称"加了 bti"会丢掉这一层。
3. 同一份 `ccarm_pac_bti_macros.h` 同时承载 PAC 宏（11.3）与 BTI 提示（本节的 `hint #34`），文件名本身就说明 Apple 在 corecrypto 汇编层把两类控制流完整性手段放在一起处理；但**该文件的宏同样受 `__arm64e__` 等条件约束**，不能据此推断构建形态。

**在哪一层强制**：landing pad 是**指令级**约定，由汇编器/编译器写入文本；guarded 位是**页级**约定，由 pmap 写入页表。两者共同生效才构成 BTI 保护。因此"某函数入口有 `bti c`"不等于"该页受 BTI 保护"，反之"该页 guarded 而入口无 pad"会导致合法跳转被拒——这解释了 12.5 里 AuxKC 例外的动机。

**覆盖与不覆盖**：可断言"内核汇编入口约定包含 BTI 垫片、PPL 原型入口显式插入 landing pad"。不可断言"iOS 全部可执行文本都有 landing pad"或"用户态二进制被要求插入 pad"——后者在本次依据里没有任何一手支撑。

## 12.4 能力探测与纠正：`gARM_FEAT_BTI` 与 `CAP_BIT_FEAT_BTI = 36`

**负责什么**：硬件能力探测决定"这颗 SoC 是否支持 BTI"。XNU 确实读取架构 ID 寄存器并维护一个内部标志位。

一手依据：`osfmk/arm/commpage/commpage.c:633-653`，该处函数注释原文为 "Initializes all commpage entries and sysctls for EL0 visible features in ID_AA64PFR1_EL1"，实现依据 `ID_AA64PFR1_EL1_BT_MASK >= ID_AA64PFR1_EL1_BT_EN` 比较后置 `gARM_FEAT_BTI = 1`；对应能力位在 ABI 中预留为 `CAP_BIT_FEAT_BTI = 36`【一手-源码 osfmk/arm/commpage/commpage.c:633-653；osfmk/arm/cpu_capabilities_public.h:85】。

**误解校正（本节重点，属简报明确纠正的一条早前口径）**：

- **常见说法**："用户态可通过 `hw.optional.arm.FEAT_BTI` 查询本机是否支持 BTI。"这一写法在若干研究文章与工具文档中流传，形式上也符合 Apple 对其他 FEAT_* 位（`FEAT_PACIMP`、`FEAT_LSE2`、`FEAT_CSV2`、`FEAT_CSV3` 等）的命名习惯。
- **正确口径**：**未获证实，正文不得写"iOS 用户态可查询 BTI 支持"**。三条源码事实把它挡在门外：① `CAP_BIT_FEAT_BTI` 在公开源码中**没有第二处使用**；② `gARM_FEAT_BTI` 同样**没有第二处使用**（即没有可见的导出点被本树展示）；③ Apple 自写的 capability 测试表 `tests/arm_cpu_capabilities.c` 共 **48 项** `test_cpu_capability`，逐条比对后**不含 BTI 条目**。
- **依据层级**：全部为【一手-源码】，且属"负面检索结果"，因此结论只能写成"未获证实"，而不是"确认不可查询"。

**为什么这里必须区分两件事**：ABI 里**存在一个位槽**（`CAP_BIT_FEAT_BTI = 36`）与**该位是否被导出给可执行/查询路径**，是两个不同命题。位槽可能是历史预留、也可能是内部使用；把它直接读成"用户可见字符串 `hw.optional.arm.FEAT_BTI`"是一次未经支撑的跳跃。同理，`commpage.c` 那句注释提到 "sysctls for EL0 visible features"，但它覆盖的是该 ID 寄存器的特性族，**不构成本机 BTI 已导出给 EL0 的证据**。

| 命题 | 是否可断言 | 依据 |
|---|---|---|
| 内核读取 `ID_AA64PFR1_EL1` 的 BT 字段并置内部标志 | 可 | 【一手-源码 commpage.c:633-653】 |
| ABI 中存在 `CAP_BIT_FEAT_BTI = 36` 位定义 | 可 | 【一手-源码 cpu_capabilities_public.h:85】 |
| 该能力位被导出、用户态可查询 | **不可**（未获证实） | 无第二处使用；自测表 48 项不含 BTI |
| 用户态一定查不到 | **也不可断言** | 属公开源码未披露，非负面事实 |

**覆盖与不覆盖**：本节的覆盖面只到"源码存在探测代码与 ABI 位槽"。**芯片与主版本对应关系**属另一层缺口：BTI（以及 GCS）"首个启用芯片 / 首个 iOS 主版本"Apple 未在任何已核实一手来源给出（**不可用/未公开**）。作为对比，Apple 对另外两项是明示的：SPTM——"supported in the A15 or later and M2 or later SOCs and replaces Page Protection Layer on supported platforms"【一手-文档 apsg.txt:2706】；MIE——"available on A19 and M5 processors or later"【一手-文档 apsg.txt:2834-2836】。据此可以在报告中写："PAC/BTI/GCS 缺少与 SPTM、MIE 同等级的公开芯片-版本锚点。"

## 12.5 AuxKC 例外原文：失效模式与「第三方 kext 只在 macOS」的旁证

**可直接引用的失效模式原文**：`osfmk/arm64/arm_vm_init.c:1243-1250` 就 AuxKC（第三方 kext 集合）文本为何**不能**标为 guarded 写了完整理由【一手-源码 osfmk/arm64/arm_vm_init.c:1243-1250】：

> "We cannot mark auxKC text as guarded because doing so would enforce **BTI on oblivious third-party kexts and break ABI compatibility. Doing this defeats the purpose of BTI (branches to these pages are unchecked!)** but given both the relative rarity and the diversity of third-party kexts, we expect that this is likely impractical to exploit in practice."

这段注释在报告里有三重价值，每一重都要按依据层级写清：

1. **机制层面（一手）**：它说明 guarded 标记的后果是"对落入该页的间接分支强制检查"，而"未适配（oblivious）的第三方代码会被强制进而破坏 ABI 兼容"；Apple 因此选择让该段不进入 BTI 保护，并**明确承认**这等于"跳到这些页的分支不受检查（defeats the purpose of BTI）"。这是少见的、Apple 自己写下"某处保护被有意放弃"的一手文本。
2. **风险评价层面（一手，但属 Apple 的工程判断）**："given both the relative rarity and the diversity of third-party kexts, we expect that this is likely impractical to exploit in practice."——注意措辞是 Apple 的**预期（we expect… likely impractical）**，不是"已验证不可利用"，正文须保留这一不确定语气。
3. **平台差异层面（旁证）**：这段话同时是"**第三方 kext 只存在于 macOS**"的旁证——内核源码讨论的对象是 AuxKC/第三方 kext，而 AuxKC 的公开启用路径属 macOS 语境；**iOS 零售设备无 AuxKC 公开启用路径**。该限定必须与引用同现，否则读者会误推"iOS 上也有一块不受 BTI 检查的第三方文本"。

**违规如何表现（一手）**：内核自带 BTI 行为自测，`osfmk/arm64/platform_tests.c` 中有 "Capture BTI exceptions triggered by our target function" 一类注释与用例（`:2311-2349`，另见 `:1799`、`:1858`）【一手-源码 osfmk/arm64/platform_tests.c:1799, 1858, 2311-2349】。可以断言的是：**BTI 违规以异常形式被内核捕获**（与 PAC 校验失败同属同步异常，见 11.5）。**不可断言**的是这种异常在零售设备上的公开呈现形态——是否有专属异常码、是否进 panic 文本、是否被遥测上报，Apple 未公开；"panic 字符串里能看到 BTI 违规"这一写法没有一手依据，只能按研究界/维修界观测降级处理。

**与前文的呼应**：12.2 已给出"构建开关 `BTI_ENFORCED` 无定义处 → 零售是否强制无法判定"。本节的 AuxKC 例外进一步说明，即使该开关为真，覆盖也**不是全量的**——至少存在一处源码明文承认的例外区。两者叠加后的负责任口径是：

> 源码具备 BTI 强制能力，构建开关取值 Apple 未公开；且在公开源码的映射规划里，AuxKC 第三方 kext 文本被有意排除在 guarded 之外，Apple 自行判断该例外在实践中难以利用（属 Apple 的工程预期，非验证结论）。

## 12.6 GCS 的证据边界：公开树里只有 EL2 陷阱位常量

**负责什么（架构侧口径，非 Apple）**：按 AArch64 规范，FEAT_GCS 引入由硬件维护的**影子栈**：函数返回地址写入受保护的 Guarded Control Stack，`GCSPOPCX/GCSPUSHX` 等指令与 `GCSPR` 寄存器受独立权限约束，普通对主栈的内存写无法污染它。它与 FEAT_PAuth（Armv8.3 起的指针签名）保护对象不同，按架构文档属**可叠加的两层缓解**。以上是架构侧口径，正文引用时须标注"非 Apple 一手"。

**Apple/XNU 侧的检索结果（一手，负面）**：全树检索 `GCSSP`、`FEAT_GCS`、`guarded_control_stack`、"Guarded Control Stack"，**没有任何使能实现**；唯一命中的是 EL2 细粒度陷阱寄存器位常量【一手-源码 osfmk/arm64/proc_reg.h:1162-1165, 1293-1296】：

| 命中符号 | 位置 | 性质 |
|---|---|---|
| `HFGRTR_GCS_EL1` / `HFGRTR_GCS_EL0` | `proc_reg.h:1162-1165` | 读陷阱位（Fine-Grained Trap，EL2 配置用） |
| `HFGWTR_GCS_EL1` / `HFGWTR_GCS_EL0` | `proc_reg.h:1293-1296` | 写陷阱位 |

这些位与 `APIAKEY`、`APDAKEY`、`APGAKEY`（11.6）等 PAC 陷阱位**同批定义**，用途是"当 EL0/EL1 访问 GCS 相关系统寄存器时是否陷入 EL2"的架构寄存器常量表。看到它们**不能**推出 GCS 已被启用——它们只是虚拟化陷阱配置的一部分。

**由此得到的两条口径纪律**：

- **"GCS 是否在 iOS 内核/用户态启用"：未核实**（公开源码与 Apple 文档均无）。
- **"GCS 是否覆盖内核态"：本次未获得可核实条目**——既不能写"覆盖"，也不能写"不覆盖"，只能写"无公开证据"。

**GCS 与 PAC 的关系（Apple 无口径）**：APSG 未记载 GCS，也未记载两者是互补还是替代，属"本次未获得可核实条目"。可以在报告里安全写出的只有一条 XNU 可核对事实：Apple 的返回地址保护走 PAC（`pacibsp`/`retab`，见 11.3、11.4），源码内**没有影子栈实现**，因此**在 Apple 公开实现中，返回地址完整性由 PAC 承担**。若要讨论"若 GCS 启用则可与 PAC 叠加"，必须显式标注这是 Arm 架构侧推论，不是 Apple 的实现说明。

**芯片—版本对应**：GCS 与 BTI 各自的首个启用芯片、首个 iOS 主版本均为 **不可用（未公开）**；与 SPTM（A15/M2+ 明示）、MIE（A19/M5+ 明示）形成对照，说明"公开文档列名 + 明示芯片锚点"是 Apple 对部分特性的做法，PAC/BTI/GCS 未获得同等待遇。这不等于它们未实现，只等于**没有可引用的公开锚点**。

**本章口径边界**

- 一手可断言：APSG 对 BTI/GCS 及相关措辞检索命中 0；Apple 公开列名的硅上特性清单（KIP/FPR/SCIP/PAC/PPL/SPTM/MIE-EMTE）；guarded 页语义注释、`ARM_PTE_GP` 位值与四处 `ARM64_PAGE_GUARDED` 调用点；`bti c`/`bti j` 垫片与 PPL landing pad、`hint #34`；`ID_AA64PFR1_EL1_BT_MASK` 探测与 `CAP_BIT_FEAT_BTI = 36` 位定义；AuxKC 例外原文；`platform_tests.c` 的 BTI 异常自测；GCS 仅 EL2 陷阱位常量。
- 未核实/未公开（照实写，不反向断言）：Apple 对 BTI 的任何面向用户表述；Apple 是否使用 "Guarded Control Stack" 术语；`BTI_ENFORCED` 在零售构建的取值；用户态能否查询 BTI 能力；GCS 是否启用、是否覆盖内核态；GCS 与 PAC 的互补/替代关系；BTI 与 GCS 的芯片-主版本对应；BTI 违规在零售日志/panic 文本中的可见性。
- 必须删除或降级的写法："BTI/GCS 属 Apple 公开列名的硅上安全特性"；"可用 `hw.optional.arm.FEAT_BTI` 查询"；"iOS 已全面启用 BTI"；"GCS 覆盖/不覆盖内核态"；把架构侧描述当作 Apple 口径。

---

# Mach 异常与 BSD 信号：两级分层与能力收口

## 13.1 依据层级声明：本节没有 Apple 面向用户的机制文档

**先声明依据层级，再讲机制**。在 *Apple Platform Security*（August 2026）全文检索 `exception port`、`Mach exception`、`EXC_`：**命中 0 次**。也就是说，**"iOS 的 Mach 异常—BSD 信号分层是否有 Apple 面向用户的官方机制文档"：本次未获得可核实条目**。

因此本章的证据基座只有两类，且都属于 L1（一手）：

| 依据类型 | 具体载体 | 可支撑的内容 |
|---|---|---|
| XNU 开源源码 | `osfmk/kern/exception.c`、`osfmk/kern/ux_handler.c`、`bsd/uxkern/ux_exception.c`、`bsd/dev/arm/unix_signal.c`、`bsd/kern/kern_sig.c`、`osfmk/mach/*/exception.h`、`osfmk/mach/exception_types.h` | 分层顺序、类型/掩码编号、映射规则、端口能力收口 |
| Apple **随源码发布的官方 man page** | `osfmk/man/task_get_exception_ports.html` 等同目录页面 | 异常端口以 send right 处置的官方口径、API 签名与参数语义 |

第二类值得单独强调：`osfmk/man/task_get_exception_ports.html` 的首句原文是 **"Function - Return send rights to the target task's exception ports."**，`task` 参数标注 "**[in task send right]** The task for which to return the exception ports."【一手-官方doc osfmk/man/task_get_exception_ports.html】。这是 Apple 自己写下并随源码分发的文档，不是第三方手册转载或社区约定，因此"异常端口以 send right 形式返回/设置"可作一手引用。同目录还有 `task_set_exception_ports`（签名带 `exception_behavior_t behavior` 与 `thread_state_flavor_t flavor`）、`thread_get_exception_ports`、`thread_set_exception_ports`、`task_swap_exception_ports`、`catch_exception_raise` 等页，可安全支撑"task/thread 两套粒度 + swap（原子替换并取回旧值）"这一 API 形态描述。

**注意与前面章节的分工**：port right 的能力语义（send/receive/dead name、capability 措辞、pinned send rights、按端口类型绑定的 IPC space policy）已在前面 Mach IPC 一章展开，本章只把"异常端口"作为该能力模型的一个具体应用面；`IOT_EXCEPTION_PORT` 与 entitlement `com.apple.security.only-one-exception-port` 的细节属 IPC 章内容，不在此重复。

**一句话结构口径（本章主线，可安全采用）**：

> 硬件/CPU 故障 → 内核 `exception_triage`（thread→task→host 三级上抛，端口是 send right；iOS 未开开发者模式时降级为 read port）→ 未被用户态 handler 接住 → host 级 `ux_handler` kobject → `ux_exception()`/`machine_exception()` 映射 → BSD 信号投递（`sigaction`／死亡 + 崩溃报告）。

**本章口径边界（前置）**：所有分层结论均为 **XNU 源码事实**，须注明"Apple 无面向用户的 Mach 异常机制文档"；不得把源码结构转述成"Apple 官方设计说明"。

## 13.2 三级上抛：`exception_triage_thread()` 的 thread→task→host

**负责什么**：`exception_triage_thread()` 是内核内异常分派的总入口，负责决定"这次异常交给哪一级、按什么语义交"。分层顺序本身即安全属性：粒度越细的接收方权限越小、越贴近该线程的调试语义；host 级是最后的兜底，保证异常不会"无人认领"。

源码依据【一手-源码 osfmk/kern/exception.c:702-787】。函数头注释（`:703-716`）原文给出口径：

> "Routine: exception_triage_thread … We make an up-call to the thread's exception server … Returns: KERN_SUCCESS if exception is handled by any of the handlers."

三级尝试顺序与其数据结构落点如下：

| 级别 | 处理器来源 | 逐级下落的条件 |
|---|---|---|
| ① thread 级 | `tro->tro_exc_actions`（线程的 exception actions 数组） | 该级无匹配 handler，或上调用返回非 `KERN_SUCCESS` 且非 `MACH_RCV_PORT_DIED` |
| ② task 级 | `task->exc_actions` | 同上 |
| ③ host 级 | `host_priv_self()->exc_actions` | 兜底（`ux_handler` 即在此层，见 13.3） |

据此可安全写出："异常按 thread→task→host 三级上抛，host 级是最后的兜底。"**判定细节的口径价值**：源码把 `MACH_RCV_PORT_DIED` 与 `KERN_SUCCESS` 一并作为"不再继续上抛"的条件——接收端口已死亡意味着"这一级曾经被注册过但端口没了"，内核不会因此把它当成"没人管"而继续升级。这类边界条件在讨论"异常被谁吃掉"时是关键，不能简化成"失败就往上抛"。

**通知型异常没有续行语义**：`EXC_CRASH`、`EXC_RESOURCE`、`EXC_GUARD`、`EXC_CORPSE_NOTIFY` 在 triage 之后**不**调用 `thread_exception_return()`（`exception.c:781-786`）【一手-源码 osfmk/kern/exception.c:781-786】。可写口径：这四类是**通知型异常**，不存在"处理后继续执行"的语义；这与 13.4 的掩码事实（`EXC_MASK_ALL` 不含 CRASH/CORPSE_NOTIFY）互相印证。

**失效模式与可观测**：`exception.c:736-742` 存在 `TUNABLE panic_on_exception_triage`，其注释表明"进入该函数即 panic"的用法** intended as a debugging aid**——也就是一个调试辅助开关，不是零售策略；把它描述成"iOS 上异常分派会 panic"是错误的。PAC 异常遥测（`pac_exception_telemetry`、`CA_EVENT(pac_exception_event, …)`，`exception.c:791-800`）位于 triage 之后的 `__has_feature(ptrauth_calls)` 分支，与 11.5 同一条：**机制存在于源码，是否出现在零售日志中 Apple 未公开**。

**生效前提**：三级上抛的前提是对应级别确实注册过 exception port（`task_set_exception_ports`/`thread_set_exception_ports`，见 13.5）；未注册则该级直接跳过。覆盖面：**thread/task 级由用户态进程自行注册（受 entitlement 与 MACF 约束）**，host 级由内核内置。

## 13.3 host 级兜底就是信号翻译器：`ux_handler` kobject

**负责什么**：这一节回答"Mach 异常层与 BSD 信号层之间的接缝到底在哪里"。答案不是"内核在 fault 处理里直接 `postsig()`"，而是：**内核里有一个 Mach kobject，它作为 host 级异常 handler 收到 Mach 异常后，再把异常翻译成 Unix 信号。**

一手原文（`osfmk/kern/ux_handler.c:53-59`）【一手-源码 osfmk/kern/ux_handler.c:53-59】：

> "Mach kobject port to reflect Mach exceptions into Unix signals. **This is the default Mach exception handler for initproc, which then filters to all subprocesses as the host level exception handler for most Mach exceptions.**"

同一文件里三处结构事实值得点出：`ux_handler_port` 声明为 `SECURITY_READ_ONLY_LATE(ipc_port_t)`（`:62`）；启动期用 `ipc_kobject_alloc_port` / `ipc_kobject_make_send` 建立该 kobject 端口（`:76`、`:91`）。这与第 10.2 节 `vm_kernel_slide` 的只读化、AMFI 只读函数表属同一设计原则：**决定"谁能兜底"的内核全局量不允许运行期改写**。

> **误解校正**
> - **常见说法**："内核发生页错误后直接给进程投递 SIGSEGV/SIGBUS。"
> - **正确口径**：信号不是内核直接投递的，而是内核内的 Mach kobject `ux_handler` 作为 **host 级兜底 exception handler** 收到 Mach 异常后，经 `ux_exception()`/`machine_exception()` 翻译，再走 BSD 信号投递路径。
> - **依据层级**：【一手-源码 `osfmk/kern/ux_handler.c:53-59`】+【一手-源码 `osfmk/kern/exception.c:702-787` 的三级顺序】。Apple 无面向用户的对应机制文档（见 13.1）。

**这个写法的机制含义（防御视角）**：

1. **分层是可核对的，不是教科书类比**。thread/task 级 handler 与 host 级 handler 走的是**同一条** Mach 异常上调用通道（`catch_mach_exception_raise` 一类 MIG 例程），差别只在注册者与端口。也就是说，"BSD 信号层"在 XNU 里是 **Mach 异常层的一个订阅者**，而不是并列的第二套故障处理系统。
2. **兜底是 initproc 继承、再过滤到所有子进程**（原文明示 "default Mach exception handler for initproc, which then filters to all subprocesses"）。这解释了为何普通 App 无需注册任何异常端口也最终会收到信号。
3. **kobject 只认自己那一个端口**：`catch_mach_exception_raise()` 首行校验 `if (exception_port != ux_handler_port) return KERN_FAILURE;`（`:140-142`）——端口身份由内核侧自证，不接受"伪装的兜底 handler"。这条属能力收口，与 13.5 一并在端口层面展开。

**生效前提**：该路径要求异常类型确实被交给 host 级（13.2 的逐级下落），且行为/flavor 允许翻译（13.4）；被更高优先级 handler 接住的异常不会走到这里。覆盖面：**大多数需要变成信号的 Mach 异常**；原文明说 "for most Mach exceptions"，因此"全部异常都经此翻译"的写法超出依据。通知型四类（13.2）与崩溃类掩码（13.4）属其外的语义。

## 13.4 异常类型、掩码与 behavior/flavor 两级参数

**负责什么**：类型编号回答"这是什么异常"，掩码回答"我订阅哪些"，behavior/flavor 回答"以什么语义、多详细的状态交给我"。三层参数共同决定一次上抛的能力边界。

**类型与计数**：`EXC_TYPES_COUNT = 14`，注释原文 "incl. illegal exception 0"【一手-源码 osfmk/mach/arm/exception.h:34】；`osfmk/mach/exception_types.h:148` 进一步解释掩码为何是 31 位——"bit zero is unused, therefore 1 word = 31 exception types"。有效类型编号为 1..13，定义集中在 `exception_types.h:68-103`。下表按**简报逐条可指认的编号**给出（其余类型只列名与语义，不在此断言具体整数值）：

| 类型 | 编号依据 | 源码语义（原文摘录） |
|---|---|---|
| `EXC_BAD_ACCESS` | **=1**（明确可引） | "Code contains kern_return_t describing error. Subcode contains bad memory address."（`exception_types.h:68-70`） |
| `EXC_BAD_INSTRUCTION`、`EXC_ARITHMETIC`、`EXC_EMULATION`、`EXC_SOFTWARE`、`EXC_BREAKPOINT`、`EXC_SYSCALL`、`EXC_MACH_SYSCALL`、`EXC_RPC_ALERT`、`EXC_CRASH`、`EXC_RESOURCE` | 定义于 `exception_types.h:68-103`（整数取值此处不逐条断言） | 依次为非法指令、算术异常、仿真指令、软件异常（code 决定语义，见下）、断点、系统调用、Mach 系统调用、RPC 提醒、崩溃通知、资源超限 |
| **`EXC_GUARD`** | **=12**（明确可引） | "Violated guarded resource protections" |
| **`EXC_CORPSE_NOTIFY`** | **=13**（明确可引） | "Abnormal process exited to corpse state" |

`EXC_SOFTWARE` 的 code 空间有官方注释级划分："Codes 0 - 0xFFFF reserved to hardware / 0x10000 - 0x1FFFF reserved for OS emulation (Unix)"，其中 `EXC_SOFT_SIGNAL = 0x10003`（`exception_types.h:86-90, 186-193`）；MACF 专用区间为 `EXC_MACF_MIN/MAX = 0x20000/0x2FFFF`。这说明"同一个 `EXC_SOFTWARE` 编号下，code 决定它是硬件语义、Unix 仿真语义还是 MACF 语义"，报告写映射表时必须带 code（见 13.6）。

**掩码的一个关键事实**：`EXC_MASK_ALL` **不含** `EXC_MASK_CRASH` 与 `EXC_MASK_CORPSE_NOTIFY`，这两项只在 `KERNEL_PRIVATE` 编译的 `EXC_MASK_VALID` 里被附加（`exception_types.h:165-179`）【一手-源码 osfmk/mach/exception_types.h:165-179】。可安全写出的结论：**崩溃通知与 corpse 通知不是"普通订阅型"异常**，用户态无法用 `EXC_MASK_ALL` 一次性订到它们——这与 13.2 的"通知型不调用 `thread_exception_return()`"在语义上一致。

**behavior（第一级参数）**：`EXCEPTION_DEFAULT = 1`（原文 "Send a catch_exception_raise message including the identity."）、`EXCEPTION_STATE = 2`、`EXCEPTION_STATE_IDENTITY = 3`、`EXCEPTION_IDENTITY_PROTECTED = 4`、`EXCEPTION_STATE_IDENTITY_PROTECTED = 5`，外加高位标志 `MACH_EXCEPTION_BACKTRACE_PREFERRED = 0x20000000`、`MACH_EXCEPTION_ERRORS = 0x40000000`、`MACH_EXCEPTION_CODES = 0x80000000`（原文 "Send 64-bit code and subcode in the exception header"）【一手-源码 osfmk/mach/exception_types.h:110-146】。

**flavor（第二级参数）**与语义开关：`MACH_EXCEPTION_CODES` 即"**以 Mach 语义（64-bit code/subcode）上抛**"与"以 UNIX 语义上抛"的分界位；内核在分派前先做 `behavior &= ~MACH_EXCEPTION_MASK` 再进入 switch（`exception.c:268-270`）。这条源码事实说明：**behavior 的低两位（DEFAULT/STATE/…）与高位标志是正交的两层**，把它们混成一个枚举值来解读会误判"能否拿到 64-bit code/subcode"。

**在哪一层强制 / 覆盖面**：类型与掩码在 `osfmk/mach/` 的 UAPI 头中定义，Mach 层分派时消费；behavior/flavor 由注册方（`task/thread_set_exception_ports`）设定、内核按设定决定上调用形态。覆盖面是所有异常路径；**不覆盖**的是信号侧的处置（`sigaction`，属 BSD 层，见 13.6）。

## 13.5 端口能力收口：send right 与 iOS 上的 read port 降级

**端口以什么权利形态存在（官方 man page，一手）**：Apple 随源码发布的 `osfmk/man/task_get_exception_ports.html` 首句原文 **"Function - Return send rights to the target task's exception ports."**，参数 `task` 标注 "**[in task send right]**"，`exception_types` 为 "[in scalar] A flag word indicating the types of exceptions for which the exception ports are desired"，并逐项解释 `EXC_MASK_BAD_ACCESS`/`EXC_MASK_BAD_INSTRUCTION`/`EXC_MASK_ARITHMETIC` 等（13.1 已交代该来源的层级属性）。同页族里 `task_set_exception_ports` / `thread_set_exception_ports` 提供两套粒度，`task_swap_exception_ports` 的语义是**原子替换并取回旧值**——报告可安全引用这一 API 形态，因为它就在 Apple 自己分发的文档里。

**iOS 特有的能力收口（本章重点）**：XNU 在 `EXCEPTION_DEFAULT` 分支上写有一段决定性的注释（`osfmk/kern/exception.c:351-364`）【一手-源码 osfmk/kern/exception.c:351-364】：

> "**Only deliver control port if Developer Mode enabled, or task is a corpse. Otherwise we only deliver the (immovable) read port in exception handler (both in or out of process). (94669540)**"

实现上对应两条转换路径：`developer_mode_state() || task_is_a_corpse(task)` 为真时用 `convert_task_to_port()` / `convert_thread_to_port()`（**control port**）；否则用 `convert_task_read_to_port()` / `convert_thread_read_to_port()`（**read port**）。`ux_handler.c:143-144` 从兜底侧印证同一事实："thread_port can be a read port if Developer Mode is off"，随后调用 `convert_port_to_thread_read(thread_port)`。

**这一收口的安全含义（防御视角，可直接写）**：

| 交付的权利 | 能做什么 | 不能做什么 |
|---|---|---|
| control port（开发者模式开，或 task 已是 corpse） | 作为 handler 的消息携带可移动的发送权利，具备完整的线程/任务控制语义 | — |
| **read port**（未开开发者模式的默认情形） | 接收异常消息、观察线程/任务**身份** | **不能用来续行执行、不能改状态**；且原文明标 immovable（不可移动） |

因此"iOS 上任何进程注册了异常端口就能拿到别人的 task/thread control port"是不成立的：在零售设备的默认形态下，异常接收方得到的只是不可移动的 read port。工单号 `94669540` 随原文保留，可据此说明这是 Apple 有意的能力裁剪而非实现疏漏。

**兜底端口自身的三道防线（一手）**：① **kobject 自证身份**——`catch_mach_exception_raise()` 首行 `if (exception_port != ux_handler_port) return KERN_FAILURE;`（`ux_handler.c:140-142`）；② **MIG consume-on-success 协议**——成功路径上由内核释放 thread/task 端口引用（`ux_handler.c:153-168`），避免权利泄漏；③ **MACF 介入与特判**——`exception.c:283-287` 注释原文 "The global exception-to-signal translation port is safe to be an exception handler."，其余 handler 需过 `mac_exc_action_check_exception_send()`；此外 `osfmk/kern/ipc_tt.c:3882` 写作 `return IP_VALID(new_port) && !is_ux_handler_port(new_port);`，`osfmk/ipc/ipc_policy.c:1600` 对 `is_ux_handler_port(port)` 专门特判。

**收口结论**：`ux_handler` 端口在 IPC 策略层被**识别为内置、不可撤销的兜底 handler**——它不需要 MACF 放行就能工作，同时不被当作普通端口处置。配合 `SECURITY_READ_ONLY_LATE(ipc_port_t)`（13.3），构成"兜底路径既不可伪造、也不可被摘除"的双向保证。交叉参考：MACF 的 hook 框架与前半部分 MACF 一章一致，异常发送是其中一个被策略覆盖的检查点。

**覆盖面与不覆盖**：read/control 的区分发生在 `EXCEPTION_DEFAULT` 分支；对 `EXCEPTION_STATE*` 等其余 behavior 的权利交付形态，源码在此处未给出等价说明，属本章不覆盖的部分，不得类推。

## 13.6 Mach→信号映射表、反方向通道与不可恢复策略

**翻译器与调用次序**：`bsd/uxkern/ux_exception.c:49-53` 注释原文——"Translate Mach exceptions to UNIX signals. ux_exception translates a mach exception, code and subcode to a signal. **Calls machine_exception (machine dependent) to attempt translation first.**"投递入口是 `handle_ux_exception(thread, exception, code, subcode)`，其注释（`ux_exception.c:110-121`）写明 "Sends the corresponding UNIX signal to a thread that has triggered a Mach exception."，内部另有 guard 页特判（`KERN_PROTECTION_FAILURE` 且命中 guard 页时走 `SIGBUS`/`SIGSEGV` 的分支）。

**映射表（`ux_exception.c:56-108` 逐条照录）**【一手-源码 bsd/uxkern/ux_exception.c:56-108】：

| Mach 异常 | 条件 | UNIX 信号 |
|---|---|---|
| `EXC_BAD_ACCESS` | `code == KERN_INVALID_ADDRESS` | `SIGSEGV` |
| `EXC_BAD_ACCESS` | 其他 code | `SIGBUS` |
| `EXC_SYSCALL` | `send_sigsys` 为真 | `SIGSYS`（否则不翻译） |
| `EXC_BAD_INSTRUCTION` | — | `SIGILL` |
| `EXC_ARITHMETIC` | — | `SIGFPE` |
| `EXC_EMULATION` | — | `SIGEMT` |
| `EXC_SOFTWARE` / `EXC_UNIX_BAD_SYSCALL` | — | `SIGSYS` |
| `EXC_SOFTWARE` / `EXC_UNIX_BAD_PIPE` | — | `SIGPIPE` |
| `EXC_SOFTWARE` / `EXC_UNIX_ABORT` | — | `SIGABRT` |
| `EXC_SOFTWARE` / `EXC_SOFT_SIGNAL` | — | **`SIGKILL`** |
| `EXC_BREAKPOINT` | — | `SIGTRAP` |
| 其他 | — | `return 0`（无信号） |

表中 **`EXC_SOFTWARE` + `EXC_SOFT_SIGNAL` → `SIGKILL`** 一条源码原文如此，社区与二手资料常误写为其他信号，引用时应注明"源码原文如此，常被误写"。

**误解校正（machine 层的职责范围）**：常见说法"信号映射由 machine 层决定"不成立。arm 上 `machine_exception()` 只处理两种情形——`EXC_BAD_INSTRUCTION → SIGILL`、`EXC_ARITHMETIC → SIGFPE`，其余一律返回 0 交给上表【一手-源码 bsd/dev/arm/unix_signal.c:931-949】。因此正确的分层描述是：**机器相关层优先尝试、覆盖面极窄；绝大多数映射由与机器无关的 `ux_exception()` 完成**。

**反方向（信号→Mach 异常）只服务于调试**：`bsd/kern/kern_sig.c:2716-2732`——仅当 `p->p_lflag & P_LTRACED` 且 `P_LSIGEXC` 置位时，进程停住（`p->stat = SSTOP`）并调用 `do_bsdexception(EXC_SOFTWARE, EXC_SOFT_SIGNAL, signum)`（函数定义见 `kern_sig.c:3488`）。据此可写：**"信号层异常"是给 ptrace/调试器用的上行通道，普通进程收不到 `EXC_SOFT_SIGNAL`**——这条常被误认为"信号会被转成 Mach 异常广播"。

**不可恢复策略（新机制，失效模式要点）**：`exception.c:876-891`——若 `is_address_space_debugged(proc)` 则异常仍可交付调试器；否则注释原文 "**By policy, this exception is uncatchable by exception/signal handlers.** Therefore exit immediately."，实现为 `os_log_error("%s: process %s[%d] hit an unrecoverable exception")` 加 `exit_with_mach_exception(proc, info, PX_FLAGS_NONE)`，`os_reason` 硬编码 `OS_REASON_FOUNDATION`；上下文表明该路径针对用户态断言/`user_brk_*` 类事件。**口径**：存在一类内核判定为不可捕获的异常，处理方式是直接退出，既不经异常 handler 也不经信号 handler；这属于**进程退出原因**层（进 Crash Report），与内核 panic 是两条不同的可观测通道。

**通知型异常再确认**：`EXC_CRASH`/`EXC_RESOURCE`/`EXC_GUARD`/`EXC_CORPSE_NOTIFY` 不调用 `thread_exception_return()`（13.2），因此它们没有"处理后继续执行"的语义，也就不会以信号方式回到执行流。

**本章口径边界**

- 一手可断言：三级上抛顺序与下落条件【`exception.c:702-787`】；host 级兜底即 `ux_handler` kobject 及其 `SECURITY_READ_ONLY_LATE` 端口【`ux_handler.c:53-62`】；类型/掩码/behavior/flavor 的取值与 `EXC_MASK_ALL` 的排除项【`exception_types.h:68-179`】；异常端口为 send right【Apple 随源码发布的 `osfmk/man/task_get_exception_ports.html`】；iOS 未开开发者模式时仅交付 immovable read port（含工单号 94669540）【`exception.c:351-364`】；映射表、machine 层窄覆盖、反方向仅 ptrace、不可捕获策略原文。
- 未核实（照实写，不反向断言）：Apple 面向用户的 Mach 异常/信号机制文档**不存在于本次检索**（APSG 检索 `exception port`/`Mach exception`/`EXC_` 命中 0）；`EXCEPTION_STATE*` 等非 DEFAULT behavior 的权利交付形态；`developer_mode_state()` 在零售设备上的实际取值路径与开关细节。
- 禁止写法：把分层归为 Apple 官方设计说明；写"内核直接投递信号"；写"异常 handler 总能拿到 control port"；写"普通进程也能收到 `EXC_SOFT_SIGNAL`"；写"信号映射由 machine 层决定"；把 `panic_on_exception_triage`/`pac_exception_telemetry` 说成零售默认行为（后者源码默认 `false`）。

---
# task_for_pid 与调试权限：谁能拿到别人的内核对象

`task for pid` 是 Mach 提供的一种内核对象句柄获取能力：给定一个进程号，返回代表该进程的 task port。谁被允许拿到别人的 task port，由多条独立通道共同裁决——POSIX 层的凭证检查、`kern.tfp.policy` 全局策略、MACF（第 6 章）在 `mac_proc_check_get_task()` 处的强制点，以及沿历史上行的 taskgated/check_access 认证。本章只做一件事：把这些**准入规则与判定逻辑**逐条摆清——谁在什么凭证条件下被允许、由哪个检查点把关、命中后留下什么可观测信号。它不是操作步骤手册，也不给出任何"取得某进程 task port"的调用序列。

## 14.1 entitlement 字符串的逐字核实与口径冲突

先看 Apple 面向开发者的官方口径，再看源码里的逐字串，两者不能混用。

Apple 开发者文档里与"取他人 task port"直接相关的 entitlement 是 `com.apple.security.cs.debugger`，页面标题为 **"Debugging tool entitlement"**，归属层级是 **Bundle Resources → Entitlements → Hardened Runtime**。其 Discussion 原文可拆成五点【一手-文档 https://developer.apple.com/documentation/bundleresources/entitlements/com.apple.security.cs.debugger】：

| 要点 | 原文语义 |
|---|---|
| 能取谁的 task port | 带此 entitlement 的 app 可 retrieve a valid task port，前提是目标进程具有 `get-task-allow` entitlement |
| 即便有此 entitlement 也拿不到 | 调试器 "can't get the task ports of processes that don't have the `get-task-allow` entitlement"，这类进程因此受 System Integrity Protection 保护 |
| Xcode 行为 | 为调试构建自动加该 entitlement，"removing the entitlement before App Store submission" |
| 非 root 运行 | 弹出管理员授权对话框，授权后得到一个 10 小时会话 |
| 前置条件 | 需先启用 Hardened Runtime，并在 Runtime Exceptions 中勾选 Debugging Tool |

**关键的平台限定，也是本章第一条红线**：该页 metadata 原文只有 `platforms: [{"introducedAt":"10.7","name":"macOS"}]`——只声明 macOS 平台，**没有任何 iOS 条目**。上表里的"10 小时会话""管理员授权对话框"也都是 macOS 语境。

> **误解校正**：常见写法把 `com.apple.security.cs.debugger` 当作 iOS 上的调试授权手段，这是错的。Apple 该页 metadata 只声明 macOS（introducedAt 10.7），无 iOS 条目，因此不得把它描述为 iOS 的调试能力。上表五点全部属于 macOS  hardened runtime 语境。【一手-文档 metadata 同上 URL】

再看 entitlement 字符串本身的逐字核实。`get-task-allow` 的真实写法**没有反向域名前缀**：源码常量 `#define CS_GET_TASK_ALLOW 0x00000004 /* has get-task-allow entitlement */`【一手-源码 osfmk/kern/cs_blobs.h:38】，它属于代码签名位（同页 `CS_ENTITLEMENT_FLAGS` 宏包含它）。社区常写的 `com.apple.security.get-task-allow` 这种全名前缀写法，**本次未在任何一手来源看到**。

> **口径冲突（§F）**：一侧是源码常量注释给出的真名 `get-task-allow`（code-signing 位，无前缀）；另一侧是社区惯写的 `com.apple.security.get-task-allow` 全名。收口写法：**entitlement 真名为 `get-task-allow`；带 `com.apple.security.` 前缀的全名本次未在任何一手来源确证，按"公开一手未记载"处理**——既不得把它当成事实，也不得断言该全名不存在。【一手-源码 cs_blobs.h:38】

XNU 自带的测试 entitlement plist `bsd/tests/.../task_for_pid_entitlement.plist` 里真实出现、可逐字列举的字符串如下【一手-源码 tests/task_for_pid_entitlement.plist】：

| 字符串 | 注释语义（源码原文） |
|---|---|
| `task_for_pid-allow` | 允许对他进程调用 task_for_pid |
| `com.apple.system-task-ports` | 系统 task port 能力 |
| `com.apple.system-task-ports.control` | 注释称"Supercedes the two above in AzulE+, cross-platform" |
| `com.apple.security.get-movable-control-port` | 允许 `task_get_special_port(...TASK_KERNEL_PORT...)` |
| `com.apple.system-task-ports.token.read` | 允许 `task_identity_token_get_task_port(...TASK_FLAVOR_READ...)` |

**这些串在源码里真实存在，但"零售 iOS 上哪一个真被闭源 AMFI 接受"Apple 并未公布列表**。因此上面这张表只能限定为"源码测试向量所示"，不能写成"iOS 上可用这些 entitlement"。这一判定权属于第 7 章所述的 AMFI 闭源侧，公开一手资料在此处即为缺口。

把 14.1 的可用与不可用口径固定下来：

- 可说：`com.apple.security.cs.debugger` 五点（macOS 语境）、该页 metadata 只声明 macOS 10.7、`get-task-allow` 是无前缀的 code-signing 位；
- 不可说：不能把 `com.apple.security.cs.debugger` 当作 iOS 调试授权手段；不能把 `com.apple.security.get-task-allow` 写成已确证全名；
- 须限定：`task_for_pid-allow` 等清单"源码测试向量所示"，零售 AMFI 实际接受哪一条 Apple 未公开；
- 缺口不反断：上述"未确证"只表示本次未取到一手依据，不等于对应事实不存在。

## 14.2 内核侧准入规则：task_for_pid_posix_check 与 tfp.policy

抛开 entitlement，内核本身还有一条 POSIX 层的凭证判定。函数 `task_for_pid_posix_check()` 的原文规则【一手-源码 bsd/kern/kern_proc.c:5585-5601】：允许取得他人 task port 的条件是"**调用者为 root**"，或**同时满足**下面三条——目标进程的 ruid/euid/svuid 与调用者 euid 相同、目标进程的有效组集是调用者组集的子集、目标进程从未切换过凭证。任一不满足即拒绝。此外两类进程被直接挡下：僵尸进程（状态 `SZOMB`）一律拒绝；`P_SUGID` 置位（进程做过 setuid/setgid 而带有特权痕迹）也拒绝【一手-源码 kern_proc.c:5602-5664】。

在此之上叠一个全局策略开关 `kern.tfp.policy`【一手-源码 kern_proc.c:5580-5582，`SYSCTL_NODE(_kern, KERN_TFP, tfp ...)` + `SYSCTL_PROC(_kern_tfp, KERN_TFP_POLICY, policy ...)`】，取值见【一手-源码 bsd/sys/sysctl.h:887-889】：

| 取值 | 常量 | 语义 |
|---|---|---|
| 0 | `KERN_TFP_POLICY_DENY` | 默认拒绝；原文注释 "only root can get another proc's task port"【源码 kern_proc.c:5627-5633】 |
| 2 | `KERN_TFP_POLICY_DEFAULT` | 系统默认值 `static int tfp_policy = KERN_TFP_POLICY_DEFAULT;`【源码 kern_proc.c:5549】 |

两个取值共享一条原文限定："**All policies allow task port for self**"——无论哪种策略，进程取自己 task port 始终被允许【一手-源码 sysctl.h:887-889】。这句话界定了策略的边界：`kern.tfp.policy` 管的是"能不能取别人的"，从来不管"取自己的"。

这条自取豁免常被误读为"策略形同虚设"，其实相反：正因为它把"取自己"单列出来，跨进程取（`task_for_pid` 对他 pid）才成为可被策略收紧的那一半。DENY 模式下跨进程只有 root 能过，DEFAULT 模式放行更多但仍旧受 POSIX 凭证与 MACF 约束。把"取自己总是可以"和"取别人要过三重检查"分开，是理解 `kern.tfp.policy` 的关键，也是下面判定链条能成立的前提。

> **误解校正**：把 task_for_pid 的准入简化成"只看是不是 root"或"只看有没有某个 entitlement"，都不成立。现代 XNU 的实际判定是三重叠加——POSIX 凭证检查 `task_for_pid_posix_check()` + 全局 `kern.tfp.policy` 策略 + MACF 上行调用 `mac_proc_check_get_task()`，并且还要按 flavor 分级（见 14.3）。单看其中任何一层都会得出错误结论。【一手-源码 kern_proc.c:5585-5680】

把这几层串起来看，判定链条大致是这样（仅描述把关逻辑，非调用序列）：

- 全局策略 `kern.tfp.policy` 先定基调：`DENY=0` 下只有 root 能取他人 task port；`DEFAULT=2` 放行更多路径，但两种取值都保留 "All policies allow task port for self"；
- 进入 POSIX 层：调用者是 root，或目标 ruid/euid/svuid 与调用者 euid 相同、目标组集是调用者组集子集、目标未切换过凭证——三者须同时成立；
- 排除项：僵尸进程（`SZOMB`）直接拒绝，带 `P_SUGID`（曾 setuid/setgid）拒绝；
- 特殊放宽面：iOS 上仅对满足 `proc_is_third_party_debuggable_driver()` 的用户态 DriverKit 驱动进程放宽凭证检查（见 14.6）；
- 收口于 MACF：无论前几层结果如何，最终按 flavor 走 `mac_proc_check_get_task()` 强制点（见 14.3）。

这五步里没有一步是"给一份 entitlement 就放行"的开关：entitlement（如 `get-task-allow`）改变的是目标进程是否落入"受 SIP 保护、无法被取 task port"的那一类，而不是给调用者一把万能钥匙。这正是 14.1 里 `com.apple.security.cs.debugger` 页面"即便有此 entitlement 也拿不到无 `get-task-allow` 且受 SIP 保护进程"的底层原因。

## 14.3 按 flavor 细分的能力模型与 MACF 交汇

task port 并非一种能力，而是一族按 flavor 分级、强弱不同的能力。现代 XNU 在返回每一种 flavor 之前，都会走到同一个 MACF 强制点 `mac_proc_check_get_task()`【一手-源码 kern_proc.c:5772、5905、6007、6129、6348】：

| flavor | 能力语义 | 校验位置 |
|---|---|---|
| `TASK_FLAVOR_CONTROL` | 最强：可发送/接收消息、改状态、分配虚拟内存等 | `mac_proc_check_get_task()` @ kern_proc.c:5772 等 |
| `TASK_FLAVOR_NAME` | 名字端口，用于按 pid 反查 task | @ kern_proc.c:5905 |
| `TASK_FLAVOR_INSPECT` | 只读观测：内存布局、线程信息等 | @ kern_proc.c:6007、6129 |
| `TASK_FLAVOR_READ` | 只读接收，能力更弱、不可移动 | @ kern_proc.c:6348 |

这张表的意义在于：内核不是"给或不给 task port"这个二元判断，而是**按 flavor 逐级把关**——control、name、inspect、read 各自调用同一个 MACF 检查点，policy 可以对不同 flavor 给出不同裁决。这正好与第 4 章的 Mach port right 能力模型呼应：port right 本质是"能力句柄"，flavor 决定了句柄承载的能力上限；把 task port 笼统当成"调试权限"会丢掉这一层粒度。MACF 侧的具体策略挂载在 `mac_proc_check_get_task()`（第 6 章已述框架），本处只强调它是 task-for-pid 判定链上的强制交汇点。

一个能帮读者建立直觉的对照（分析类比，非同一机制）：第 13 章里 iOS 未开启开发者模式时，异常接收方从"control port"被降级为"不可移动的 read port"；这与 task port 按 flavor 分级是同一种设计取向——内核把"能对别人的 task 做什么"拆成可分别授权/分别收窄的等级，而不是一把全有或全无的钥匙。理解这一点，就不会把"拿到了 task port"直接等同于"拿到了全部调试能力"：拿到哪个 flavor，才决定拿到哪一档能力。

## 14.4 upcall 认证路径与可观测性信号

除了内核内的 POSIX 检查与策略位，历史上还有一条**向上调用（upcall）认证**通道：当 POSIX/策略层不能直接放行时，判定权可以交给 MACF 的 `mac_proc_check_get_task()`，并在其背后经 taskgated 的 `check_access` 上行调用完成一次用户态授权决策【一手-源码 kern_proc.c:5671-5680；bsd/security/mac_process.c:430】。这里要把握两点：其一，这条 upcall 是"由谁来裁"的机制入口，不是"怎样被绕过"的路径；其二，`com.apple.security.cs.debugger` 页面所述"非 root 需管理员授权对话框、授权后 10 小时会话"是这条 upcall 在 **macOS** 上的产品化表现，与 iOS 无对应公开文档（见 14.1 的平台限定）。

内核为每一次 task_for_pid 留下可观测计数。`extmod_statistics_incr_task_for_pid()` 会累加 `task_for_pid_count` 与 `task_for_pid_caller_count`【一手-源码 osfmk/kern/extmod_statistics.c:73-84；字段定义 osfmk/mach/vm_statistics.h:203-204】。读取这组统计需要特权，但它构成一个**主机侧信号**：进程是否频繁获取他人 task port、有多少不同调用者参与，都可以从中观测——这对防御性监控（而非利用）有价值。

还有一条继承规则常被忽略：`CS_GET_TASK_ALLOW` **不沿非 fork 路径继承**。源码原文注释 "All non-fork paths should not inherit GTA flag"【一手-源码 bsd/kern/kern_fork.c:1080-1084】。也就是说，`get-task-allow` 不会经由 exec 之类的路径随意传播到子镜像，从而避免"一个带调试位的进程派生出更多带调试位的进程"这类能力扩散。

对防御方而言，上面这些计数与规则可以汇成几个可观测问题（不涉及任何利用）：

- 某进程是否高频获取他人 task port（`task_for_pid_count` 增长速率）；
- 参与调用的调用者是否异常增多（`task_for_pid_caller_count`）；
- 读取这组统计本身需要特权，因此它是"主机侧/受控观测面"的信号，不面向普通 App；
- 结合 `kern.tfp.policy` 取值与 flavor 命中点，可判断某次获取走的是哪条准入路径。

这也解释了为什么把 task_for_pid 当纯调试便利来监控会漏判：它的每一次跨进程命中都会留痕，而"取自身"则被策略始终放行，两者不能混在同一个计数语义里解读。

需要提醒的是，可观测性这条线只到"内核留了什么计数、谁能读这些计数"为止，不延伸到任何"如何触发某次跨进程获取"的操作。计数机制对防御的价值在于提供主机侧的异常信号面，而非提供可被复制的调用配方——后者始终越出本报告的安全边界。判定链、策略位、flavor 校验点与计数四者合起来，才是"谁能拿到别人的内核对象"这一问题的完整、且可公开陈述的答案。

## 14.5 生产环境调试豁免红线与 boot-args 门控

有些 boot-args 能在开发/调试内核里放宽代码签名与调试约束，例如 `amfi`、`amfi_unrestrict_task_for_pid`、`amfi_get_out_of_my_way`、`cs_enforcement_disable`、`cs_integrity_skip`、`amfi_relax_profile_trust`、`amfi_dev_mode_policy`（这一族配置载体在第 7 章 AMFI 一节已列名，此处不重复）。关键是：**这套解析整体被编译开关 `#if DEVELOPMENT || DEBUG` 包住**。源码注释原文【一手-源码 bsd/kern/kern_codesigning.c:217-223】："We only ever need to parse for boot-args based exemption state on DEVELOPMENT or DEBUG builds as **this state is not respected by any code signing component on RELEASE builds**."

这句原文直接给出了生产环境的红线：**RELEASE 构建里没有任何代码签名组件会尊重这些基于 boot-args 的豁免状态**。也就是说，零售设备上这些调试豁免在架构层面就不生效，而不是"生效但被拒绝"。

在 SPTM/TXM 配置下还会被再削减一层。若 TXM 系统策略未开启本地签名，则 `config &= ~CS_CONFIG_LOCAL_SIGNING;`；若只读数据未允许无限制调试，则 `config &= ~CS_CONFIG_UNRESTRICTED_DEBUGGING;`【一手-源码 kern_codesigning.c:200-209、330-340】。即最终是否放行还由 TXM 侧的策略位决定，内核 config 只是被动接收削减结果。

归拢一下这层门控的形状：

- 受 `#if DEVELOPMENT || DEBUG` 包住的解析对象：`amfi`、`amfi_unrestrict_task_for_pid`、`amfi_get_out_of_my_way`、`cs_enforcement_disable`、`cs_integrity_skip`、`amfi_relax_profile_trust`、`amfi_dev_mode_policy`（与第 7 章 AMFI 配置载体同源）；
- RELEASE 构建的行为：解析即便存在，也 "not respected by any code signing component"——即这些豁免位在生产内核里不被任何签名组件读取；
- SPTM/TXM 的二次削减：`localSigning == false` 清 `CS_CONFIG_LOCAL_SIGNING`，`!allowUnrestrictedDebugging` 清 `CS_CONFIG_UNRESTRICTED_DEBUGGING`；
- 结论口径：生产环境是否放宽，最终取决于 TXM 策略位，而非 boot-args 字符串本身。

> **误解校正**：把 `amfi_get_out_of_my_way` 一类 boot-args 写成"在生产 iPhone 上可放宽代码签名/解锁调试"，不成立。相关解析被 `#if DEVELOPMENT || DEBUG` 包住，源码原文明说 RELEASE 构建里没有任何代码签名组件尊重这些豁免状态，且 SPTM/TXM 下 `localSigning`/`allowUnrestrictedDebugging` 还会再削减（kern_codesigning.c:200-209、217-223、330-340）。这里描述的是门控逻辑本身，不提供任何修改 boot-args 或规避门控的做法。

## 14.6 iOS 上的可调试驱动进程与研究设备边界

iOS 上确实存在一个"可被放宽调试约束"的新面，但它指向的是**用户态 DriverKit 驱动进程**，而非内核。判定函数 `proc_is_third_party_debuggable_driver()` 仅在 `#if XNU_TARGET_OS_IOS` 下为真，需同时满足四个条件：`proc_is_driver(p)` 为真、**不是** platform binary、持有 `kIODriverKitEntitlementKey`、且 csflags 含 `CS_GET_TASK_ALLOW`【一手-源码 bsd/kern/kern_proc.c:3268-3282】。命中这一判定时，内核会放宽 task_for_pid 的凭证检查（kern_proc.c:5638）、`ptrace`（bsd/kern/mach_process.c:545）与信号投递（bsd/kern/kern_sig.c:331）。

把四条件与三个放宽面拆开看：

- 四条件缺一不可：是驱动进程、非平台二进制、持 DriverKit entitlement、带 `get-task-allow` 签名位；
- 放宽的是 task_for_pid 凭证检查、ptrace、信号投递三处，均作用于该用户态驱动进程；
- 放宽的仍是"用户态调试"语义，与内核态调试无关；
- 对照第 13 章：iOS 未开启开发者模式时异常接收方只拿到不可移动的 read port，能力被降级——task port 的分级与此同源，都体现"能力按用途收窄"。

> **误解校正**：把"iOS 上可调试的驱动"理解成"可调试的内核态 kext"，是错的。`proc_is_third_party_debuggable_driver()` 要求 `proc_is_driver` 且持有 `kIODriverKitEntitlementKey` 且 `CS_GET_TASK_ALLOW`，指的是**用户态 DriverKit 驱动进程**（kern_proc.c:3268-3282）。放宽的也是用户态调试通道，与内核调试无关。第 15 章会展开 DriverKit dext 在 iOS 上的准入建模。

至于本章最该谨慎的一条——**现代零售 iPhone 是否有可用的内核调试通道**——Apple 未公开任何允许零售 iPhone 做内核远程调试的文档。XNU 里的远程内核调试实现（`osfmk/kdp/`，kdp_over_udp/serial，以及 `osfmk/arm64/dbgwrap.c`、`alternate_debugger.c`）在公开源码中**确实存在实现体**，但这些实现在 iOS 零售构建上的可用性、开关条件，Apple 未记载。这里只能写成"**公开资料仅显示实现存在，未见 Apple 官方声明其在零售 iOS 上可用**"，既不能断言有可用通道，也不能断言该通道不存在。

可作为部分核实旁证的是：APSG（August 2026）通篇**没有** "kernel debugging / kernel debugger / KDK" 章节；能核到的只是诊断环境的受限表述——recoveryOS/诊断单用户模式【一手-文档 apsg.txt:2235-2250】，以及 `com.apple.mobile.pcapd` 等诊断服务在生产环境受限【一手-文档 apsg.txt:2899-2903】。据此可写"Apple 公开文档未把内核调试列为 iOS 可用能力"。真正给"自定义内核缓存 + 调试任意进程"留口的，是下一专章讨论的 Security Research Device（`research.com.apple.license-to-operate` 可调试系统进程），那是研究设备专属、且在生产设备上被主动终止的能力，不是零售 iPhone 的能力。

把这条红线写成三句可核对的口径，避免越界：

- 实现存在 ≠ 零售可用：`osfmk/kdp/`、`dbgwrap.c`、`alternate_debugger.c` 是公开源码里的实现体，Apple 未声明其在零售 iOS 上可开启；
- 文档缺位 ≠ 通道不存在：APSG 无内核调试章节，只支持"未列为可用能力"这一陈述，不能据此断言内核调试通道绝无；
- 研究设备是受控例外：自定义 kernel cache 与"调试任意进程"是研究设备专属，生产设备由启动链与 AMFI 阻断（详见第 15 章），不构成"以研究设备绕过生产保护"的公开路径。

### 本章口径边界

- 官方 `com.apple.security.cs.debugger` 页 metadata 只声明 macOS（introducedAt 10.7），无 iOS 条目；"10 小时会话""管理员对话框"同为 macOS 语境，不得写成 iOS 调试手段。
- `get-task-allow` 真名无域名前缀（源码常量 cs_blobs.h:38）；`com.apple.security.get-task-allow` 全名本次未在任何一手来源确证，按公开未记载处理，不断言其存在也不断言其不存在。
- `task_for_pid-allow` 等字符串"源码测试向量所示"，零售 iOS 上哪个被闭源 AMFI 接受 Apple 未公布。
- 准入判定是三重叠加 + flavor 分级（root/凭证一致 + `kern.tfp.policy` + `mac_proc_check_get_task()`）；"所有策略都允许取自身 task port"须保留。
- 6 个 CS/调试 boot-args 被 `#if DEVELOPMENT || DEBUG` 包住，RELEASE 不尊重；SPTM/TXM 下再削减。
- 现代 iPhone 是否有可用内核调试通道：Apple 未公开；`osfmk/kdp/` 等实现存在≠零售可用。iOS 可调试"驱动"仅指用户态 DriverKit 驱动进程。

---

# 内核扩展与系统扩展：iOS 与 macOS 的真实差异

"iOS 能不能装第三方驱动"这个问题，答案取决于问的是**内核态 kext** 还是**用户态 dext**，也取决于把"官方文本范围"和"源码实现面"分开看。本章的核心方法是一条：**看 Apple 官方句子的标题带不带 "in macOS"**——凡是第三方 kext、AuxKC、UAKL、kext receipt、系统扩展的表述，其章节标题即自限于 macOS；而 iOS 内核里"扩展"的唯一形态，是 Apple 自家 kext 被预链接进 Boot Kernel Collection。同时，源码层面 iOS 的第三方 DriverKit dext 用户客户端准入是被明确建模的，所以"iOS 完全没有驱动扩展"这句也不成立。

## 15.1 官方文本范围：所有扩展表述都带 "in macOS"

Apple 平台安全指南（August 2026）里关于"扩展内核"的所有正面表述，标题都带 macOS：

| 官方原文（逐字摘录） | 出处 |
|---|---|
| 章节标题 "**Securely extending the kernel in macOS**"；正文首句 "Starting with macOS 11, if third-party kernel extensions (kexts) are enabled, they can't be loaded into the kernel on demand. Instead, they're merged into an **Auxiliary Kernel Collection (AuxKC)**" | apsg.txt:3511-3516【一手-文档】 |
| Important 框 "**Kexts are no longer recommended for macOS. Kexts risk the integrity and reliability of the operating system**" | apsg.txt:3520-3522 |
| 路线图 "...adopt system extensions **before kext support is removed from macOS for future Mac computers with Apple silicon**" | apsg.txt:1666-1668 |
| 系统扩展定义 "...system extensions that run in **user space rather than at the kernel level**" | apsg.txt:3562-3566 |
| 替代框架清单 "...including **DriverKit, EndpointSecurity, and NetworkExtension**...all without needing to write kexts" | apsg.txt:3569-3572 |
| DriverKit 章节标题 "**DriverKit security for macOS**"："Drivers built with DriverKit run in user space, rather than as kernel extensions" | apsg.txt:10689-10693 |

User-Approved Kernel Extension Loading、UAKL 的 SHA-384 写入 LocalPolicy、kmd 校验，以及 AuxKC Image4 + **kext receipt**（"used by subsystems such as Apple Pay to determine whether there are any kexts currently loaded that could interfere with the trustworthiness of macOS"）也全部在 macOS 章节【一手-文档 apsg.txt:3528-3559】。SIP 开合对 kext 签名强制的差异（"If SIP is turned on, the signature of each kext is verified before being included in the AuxKC. If SIP is turned off, the kext signature isn't enforced."）同样是 macOS 语境【apsg.txt:3542-3545】。

UAKL 这条链在 Apple 文本里也是纯 macOS 叙事，逐段为：1TR/安全策略 → Reduced Security → 勾选 "enable kernel extensions" → UAKL 决策的 SHA-384 写入 LocalPolicy → kmd 校验；自 macOS 10.13 起的用户同意流程、`spctl`、MDM 配置也写在同一 macOS 章节【一手-文档 apsg.txt:3528-3545、3577-3594】。iOS 章节内没有任何等价条目。把这条链读成"iOS 也可经用户批准加载第三方 kext"是没有一手依据的。

> **误解校正**：不能把 Apple 关于 kext 的政策当成"同时适用 iOS"。APSG 里所有第三方 kext / AuxKC / UAKL / kext receipt 表述都挂在标题 "Securely extending the kernel in macOS" 之下；iOS 章节里出现的只有 "the kernel, kernel extensions (kext)" 与 "Apple kernel extensions"，指的是 Apple 自家（见 15.2），"第三方"限定词只与 macOS 同现。以"官方文本范围"方式核实可安全写：**iOS/iPadOS 上第三方 kext 不存在公开可用路径**【一手-文档 apsg.txt:3511、1371-1374、2164-2166】。

把这条"文本范围"判定法总结成可复用的三条（本章后续沿用）：

- 看标题：凡扩展内核类小节，标题带 "in macOS" 即自限平台，不外推到 iOS；
- 看限定词同现：句子里 "third-party kext / AuxKC / UAKL / kext receipt" 只与 macOS 同段出现，与 iOS 段落不相交；
- 看被替代物：Apple 给出的替代路径（DriverKit / EndpointSecurity / NetworkExtension、user-space system extensions）本身就以"取代 kext"为语境，而这套取代叙事也写在 macOS 章节里。

三条合起来支持一个不越界的结论：iOS 上第三方 kext 无公开可用路径；至于源码层面对 iOS 第三方 dext 的建模（15.3）是另一条线，不因这条 kext 结论而被否定，也不反过来给第三方 kext 开门。

## 15.2 iOS 内核里唯一的扩展形态：Boot Kernel Collection

iOS 内核并非"没有 kext"，而是"只有 Apple 自家的 kext，且被预链接进启动用的内核集合"。iPhone/iPad 启动链原文把这层说得很直白："These components include the bootloaders, **the kernel, kernel extensions (kext)**, and cellular baseband firmware."【一手-文档 apsg.txt:1371-1374】——kext 出现在 iOS 启动组件清单里，但主语是 Apple 系统组件，不含第三方。

Boot Kernel Collection 的角色见另一段："iBoot places it into memory with the **Boot Kernel Collection** before locking the full memory region covering **the Boot Kernel Collection and AuxKC** with the System Coprocessor..."【一手-文档 apsg.txt:1495-1497】。需要限定：这句连同 AuxKC 一并出现，属 Apple silicon Mac 语境；iOS 侧对应的是 Boot Kernel Collection，AuxKC 是 macOS 才启用的第三方集合。x86 macOS 那侧还有一句可用来界定"Apple kernel extensions"的范围："the **immutablekernel** file represents the complete set of **Apple** kernel extensions required to boot macOS."【一手-文档 apsg.txt:2164-2166】——注意 `immutablekernel` 与"完整 Apple kext 集合"是 x86 macOS 语境，不要搬到 iPhone。

把这几句合起来可得出可安全引用的口径：**跨平台用词均为 "Apple kernel extensions"，"第三方"限定词只与 macOS 同现**。iOS 的"扩展"进不了内核态集合，能进 Boot Kernel Collection 的只有 Apple 自家 kext。

另一句可作侧证的是 Kernel Integrity Protection 一节："The memory controller provides a protected physical memory region that **iBoot uses to load the kernel and kernel extensions**."【一手-文档 apsg.txt:2716-2726】——它说明内核与 kext 都由 iBoot 装入受保护物理区（与第 10 章地址随机化里"slide 在 iBoot→kernel 装载阶段确定"同源），但 APSG 并未把 KIP 与随机化写成同一机制，也不把这里的 kext 解释成第三方可注入的东西。iOS 侧的加载面始终锁定在 Apple 自家预链接集合。

小结这一节的口径：可以说"iOS 内核有 kext"，但必须紧跟"这些是 Apple 自家、预链接进 Boot Kernel Collection 的 kext"；不可以说"iOS 内核能加载第三方 kext"，也不可以反过来滑向"iOS 内核完全没有扩展"（那是 15.3 的 dext 线）。两种越界都源自没把"谁写的 kext、以什么形态进内存"这两点钉住。

## 15.3 iOS 第三方 dext：源码里的用户客户端准入

上面是"官方文本范围"，下面要加一条限定：**不得据此写成"iOS 完全没有驱动扩展"**。内核侧的用户态驱动服务 `iokit/Kernel/IOUserServer.cpp` **专门实现了 iOS 第三方 dext 的用户客户端准入策略**【一手-源码 iokit/Kernel/IOUserServer.cpp:4760-4790】：

- 在 `#if XNU_TARGET_OS_IOS` 分支里，`checkiOS3pEntitlements = !fPlatformDriver;`——只对非平台驱动进程启用这套第三方判定；
- 随后要求调用方 App 持有 `com.apple.developer.driverkit.communicates-with-drivers`；
- 做 **Team ID 比对**：取 `csproc_get_teamid(current_proc())`，与被访问 dext 的 Team ID 比对，同 team 直接放行；
- 不同 team 时，要求该 dext 额外持有 `com.apple.developer.driverkit.allow-third-party-userclients`，否则拒绝用户客户端接入；
- 注释原文："When checking iOS 3rd party entitlements, do not fall through to other entitlement checks"；
- 非 iOS 分支 `checkiOS3pEntitlements = false;`，走 "first party dexts and third party macOS dexts" 路径（检查 `...allow-any-userclient-access` / `...userclient-access` 与 bundle ID 匹配）。

这段是第 9 章 IOKit 用户客户端边界在 iOS 上的具体收口：框架提供机制、准入以 entitlement + Team ID 收口，跨 team 还要求 dext 显式声明允许第三方 user-client。它推翻的是"没有驱动扩展"，**不推翻**"没有第三方 kext"——dext 是用户态，kext 是内核态，两条结论并行不悖。BTI 一节里"第三方 kext 只在 macOS"的旁证（AuxKC 文本不能标 guarded，否则会 "enforce BTI on oblivious third-party kexts"）也与此一致【一手-源码 osfmk/arm64/arm_vm_init.c:1243-1250，详见第 12 章】。

从加固角度看这段准入逻辑，有三点可提炼（分析层面，非新事实）：

- 第三方 dext 只在 `fPlatformDriver == false` 时才走这套判定，平台驱动不受影响，系统自有驱动不被第三方 user-client 随意接入；
- 跨 Team ID 默认关：不同开发团队的 App 想连到某个 dext，须该 dext 主动持有 `allow-third-party-userclients`，否则拒；这把"谁能连我的驱动"的控制权交给驱动作者显式声明；
- entitlement 判定不"fall through"：iOS 第三方检查失败即拒，不再回落到其他检查，避免误配宽松路径。

## 15.4 DriverKit entitlement 清单与平台口径冲突

DriverKit 相关 entitlement 名，报告写时应以源码逐字为准。公开侧（`iokit/IOKit/IOKitKeys.h:122-151`）与私有侧（`IOKitKeysPrivate.h:138`）逐字清单【一手-源码】：

| 类别 | 字符串 |
|---|---|
| 基础 | `com.apple.developer.driverkit` |
| 用户客户端 | `com.apple.developer.driverkit.userclient-access`、`com.apple.developer.driverkit.allow-any-userclient-access` |
| 管理类 | `com.apple.developer.driverkit.administrator` |
| 与驱动通信（iOS 第三方准入用） | `com.apple.developer.driverkit.communicates-with-drivers` |
| 允许第三方 user-client（跨 team 用） | `com.apple.developer.driverkit.allow-third-party-userclients` |
| 传输/族 | `com.apple.developer.driverkit.transport.usb`、`...transport.hid`、`...family.hid.device`、`...family.hid.eventservice`、`...builtin` |
| 私有 | `com.apple.private.driverkit.driver-access`、`com.apple.private.driverkit.test-driver`、`com.apple.private.driverkit.allows-publish` |

（`...driver-access` 的用途注释见 `IOUserServer.cpp:4749`："check if client has com.apple.private.driverkit.driver-access and the required entitlements match the driver's entitlements"。）

把这张清单再按用途分一下，有助于理解准入是怎么收口的：

- 决定"能不能连到驱动"：`...userclient-access`、`...allow-any-userclient-access`（非 iOS 分支走这条）；
- iOS 第三方 user-client 专用：`...communicates-with-drivers`（App 侧）与 `...allow-third-party-userclients`（dext 侧，跨 Team ID 时才需要）；
- 传输/族类只圈定可驱动的硬件范围：`transport.usb`、`transport.hid`、`family.hid.device`、`family.hid.eventservice`、`builtin`；
- 私有侧三串由系统内部消费，不面向第三方开发者声明；
- 注意 `...administrator`、`...allow-any-userclient-access` 这类高权限串属第一方/管理语境，不能仅凭字符串存在就推断 iOS 第三方 App 可用。

**§F 冲突必须并列写清**。同一个 DriverKit 主页（https://developer.apple.com/documentation/driverkit ）内部，正文 prose 与结构化 metadata 不一致：

- prose 侧原文："The base DriverKit framework is available **in macOS for Apple silicon and Intel-based Mac computers, and in iPadOS for devices with an M-series chip.**"，并补"In iPadOS, the system automatically discovers and upgrades drivers along with their host apps."——只点 macOS 与"带 M 系列芯片的 iPadOS"，未点 iPhone；
- metadata 侧：平台条目列出 `macOS 10.15`、`Mac Catalyst 16.0`、**`iOS 16.0`**、`iPadOS 16.0`、`DriverKit 19.0`——出现了 iOS（即含 iPhone）。

> **收口写法（不择一掩盖）**：Apple 面向开发者的正文只点明 **iPad（M 系芯片）+ Mac**，并说 iPadOS 随宿主 App 自动升级驱动；页面 metadata 另列 `iOS 16.0`/`iPadOS 16.0`。**iPhone 侧目前只有 XNU 源码含第三方 dext 的用户客户端判定逻辑（`IOUserServer.cpp:4760-4790`、`proc_is_third_party_debuggable_driver()`），Apple 面向开发者的可用性表述并未点名 iPhone**。报告须并列呈现两侧，不单独采信 metadata 断言"iPhone 支持 DriverKit 第三方驱动"。【一手-文档 developer.apple.com/documentation/driverkit】

为何要并列而不各取一是：prose 与 metadata 都是 Apple 同页产物，各有权威成分。metadata 里的 `iOS 16.0` 反映的是框架符号在 SDK 层面的平台标注，prose 里的"iPad M 系 + Mac"反映的是面向开发者的产品可用性叙述；源码里的 iOS 第三方 dext 判定则反映运行时确有相关代码路径。三者并不必然矛盾，但把它们压成一句"iPhone 支持/不支持第三方驱动"就必然失真。安全写法是各归各位——产品口径按 prose、运行时按源码、SDK 标注按 metadata——并明确"iPhone 面向开发者的可用性 Apple 未点名"这一留白。

## 15.5 跨平台政策缺口：tvOS、watchOS、bridgeOS、audioOS

Apple 有没有就 tvOS/watchOS/bridgeOS/audioOS 的第三方内核组件政策给出可核实条目？答案是**没有**。APSG（August 2026）里 `bridgeOS`、`audioOS` 命中 **0 次**；对多平台内核唯一的正面表述出现在 CPRNG 一节："Apple provides a trusted software CPRNG running in **the iOS, iPadOS, macOS, tvOS, visionOS, and watchOS kernels**."【一手-文档 apsg.txt:3849-3852】。这句能论证"这些平台共用 XNU 内核家族"，但**不能**用来论证扩展政策——它没提任何第三方 kext/dext 是否允许。

把这一节能确证与不能确证的分开：

- 可确证：iOS/iPadOS/macOS/tvOS/visionOS/watchOS 共用 XNU 内核家族（CPRNG 句）；
- 可确证（带限定）：MDA 字段跨平台列出，但其"Third-party kernel extensions allowed"语义原文只针对 Apple silicon Mac；
- 不可确证：tvOS/watchOS 的第三方内核扩展是否被允许——Apple 未公开；
- 完全不可得：bridgeOS、audioOS 的任何相关条目（APSG 命中 0 次）；
- 遗留缺口：visionOS 是否有等价 DriverKit/第三方 dext 路径，本次未抓到一手页面。

Managed Device Attestation 字段表里有一条看似跨平台的字段：`1.2.840.113635.100.8.13.3 Third-party kernel extensions allowed`，最小支持版本列 macOS 14 / tvOS 16 / visionOS 1.1 / watchOS 9 / iOS 16 / iPadOS 16【一手-文档 apsg.txt:11449-11457】。但该字段的**语义原文只写 "Represents whether or not third-party kernel extensions are allowed on a Mac with Apple silicon"**——字段虽跨平台列出，语义只对 Apple silicon Mac 成立。

> **口径边界（缺口，不反向断言）**：tvOS / watchOS / bridgeOS / audioOS 上的第三方内核组件政策，**本次未获得可核实条目**。报告不得断言"这些平台同样禁止第三方内核扩展"，也不得断言"允许"；同样，visionOS 是否有等价 DriverKit/第三方 dext 路径也属遗留缺口，本次未抓到一手页面。能安全引用的只有：共用内核家族这句（CPRNG），以及 MDA 字段"语义只对 Mac 成立"这一限定。

## 15.6 Security Research Device 与生产设备隔离

这是对本章与上一章两条结论（"iOS 内核只能装 Apple 自家 kext""零售 iPhone 无公开内核调试通道"）的**最强官方反证**，且是 Apple 自己给出的限定项，必须与正文同现。

APSG 对 Apple Security Research Device 的定性原文："The Apple Security Research Device is a **specially-fused iPhone** that allows security researchers to perform research on iOS **without having to defeat or inactivate the platform security features**… a researcher can side-load content that runs with **platform-equivalent permissions**…"【一手-文档 apsg.txt:3979-4053】。研究者能力清单逐条含：

- Side-load executable code … with **arbitrary entitlements at the same permission level as Apple operating system components**；
- **Start services at startup**；
- **Persist content across restarts**；
- Use the **`research.com.apple.license-to-operate`** entitlement to permit a process to **debug any other process on the system, including system processes**；
- **Personalize and restore a custom kernel cache**。

也就是说，"自定义 kernel cache"与"调试任意进程"在 Apple 公开口径里都是**研究设备专属**能力——研究设备靠特殊融合（fusing）拿到平台等同权限，而不是靠击破平台安全特性。

真正说明"研究面与生产面被分开"的，是生产设备隔离的四道机制原文【一手-文档 apsg.txt:3984-4053】：

| 机制 | Apple 原文要点 |
|---|---|
| 研究用 iBoot | "the research iBoot checks for a new fusing state and **enters a panic loop** if it's being run on non-research-fused hardware" |
| Boot Kernel Collection 变体 | "the policy changes are implemented in a variant of iBoot and in the Boot Kernel Collection, which **fail to boot on a production device**" |
| `research.` namespace | "respected only by the **RESEARCH variant of the AppleMobileFileIntegrity kernel extension; any process with this entitlement is terminated on a customer device during signature validation**" |
| cryptex / 个性化 | cryptex 四道防线（launchd 不加载 cryptexd plist、cryptexd abort、AppleImage4 不放发 anti-replay 值、签名服务器只对白名单设备个性化）；个性化时 "only the measurements (for example, hashes) of the executables or kernel cache and the security research device identifiers are sent to Apple" |

从防御视角读这几句：**研究面与生产面在硬件融合层就被分开**——AMFI 在生产构建上主动终止带 `research.` 前缀 entitlement 的进程，研究用内核缓存无法在生产设备上启动。研究设备的存在不意味着零售 iPhone 获得这些能力；恰恰相反，它是 Apple 划出的一条受控例外通道，其权限模型（特殊融合 + 平台等同权限）与生产设备隔离（panic loop / 启动失败 / 签名阶段终止 / 个性化白名单）被同时写进官方文档。本节只描述研究设备的权限模型与生产隔离机制，不提供在研究设备上做任何操作的步骤，更不构成"用研究设备绕过生产保护"的指引——按 Apple 原文，这类绕过在启动链与 AMFI 层就被阻断。

把这条"受控例外"的边界要点归拢如下：

- 研究设备的能力来源是**硬件融合状态（fusing）**，不是破解平台安全特性——APSG 原话即"without having to defeat or inactivate the platform security features"；
- 平台等同权限、任意 entitlement 侧载、启动服务、跨重启持久、`research.com.apple.license-to-operate` 调试任意进程、个性化并恢复自定义 kernel cache——这些在 Apple 口径里都限定于研究设备；
- 隔离靠四处独立阻断：研究 iBoot 在非研究融合硬件上进入 panic 循环、Boot Kernel Collection 变体在生产设备上无法启动、`research.` 前缀在生产设备签名校验阶段即被终止、个性化只上传度量值与研究设备标识；
- 对本章两条主结论（iOS 内核只装 Apple 自家 kext、零售 iPhone 无公开内核调试通道）而言，研究设备既是反证（证明这类能力物理上存在），又是限定（证明它只在受控例外面里、生产面被硬件层分开）——两者同时成立才是不失真的写法。

### 本章口径边界

- 第三方 kext / AuxKC / UAKL / kext receipt / 系统扩展表述整体为 **macOS-only**（APSG 章节标题即含 "in macOS"）；iOS 章节的 kext 只指 Apple 自家（apsg.txt:1371-1374、2164-2166）。
- 不得写"iOS 完全没有驱动扩展"：源码 `IOUserServer.cpp:4760-4790` 明确建模 iOS 第三方 dext 的用户客户端准入（Team ID 比对 + `communicates-with-drivers` / `allow-third-party-userclients`）。推翻的是"没有驱动扩展"，不推翻"没有第三方 kext"。
- DriverKit 平台：正文只点 iPad（M 系）+ Mac，metadata 另列 iOS 16.0；iPhone 侧仅源码有第三方 dext 判定逻辑，须并列两侧，不单独采信 metadata。
- tvOS/watchOS/bridgeOS/audioOS（及 visionOS 的 dext 路径）第三方内核组件政策：未获可核实条目，不断言允许或禁止。
- Security Research Device 是本章与上一章结论的官方限定项，须与正文同现；个性化只上传度量值与研究设备标识。

---

# 崩溃可观测性：panic 报告能看到什么、看不到什么

内核崩溃报告是事件响应里少数还能从设备侧拿到的直接信号，但它的"能看到什么"必须严格分级：Apple **正式记载**的部分极少，大量字段名、分区名来自**研究界与维修界的公开观测**。本章把这两级证据分开摆，逐个字段标注核实状态，并对最关键的红线（`translation` 语义、PAC/BTI 是否出现在 panic 里）给出降级写法。全章只谈"公开能读到什么"，不涉及任何触发崩溃、构造 panic 或绕过保护的步骤。

## 16.1 两级证据：Apple 正式记载与研究界观测

先给分级原则：

| 等级 | 含义 | 在 panic 主题下的覆盖 |
|---|---|---|
| Apple 正式记载（L1） | 出现在 APSG、开发者文档、法务/支持页等一手来源 | 分析数据开关名、查看路径；源码侧 paniclog 机制 |
| 研究界/维修界观测（L2） | 公开样本、维修站经验，非 Apple 口径 | `panic-full`、`panicString` 等条目/字段名 |
| 未确证 / 未获得可核实条目 | 本次未取到一手依据，不等于不存在 | `FallToPanic`、`loader_flags`、PAC/BTI 显示等 |

一条必须保留的定性：Apple 一手文档**没有** panic 报告的字段定义表，也**没有**逐条列举 `Analytics Data` 里会出现哪些条目名。因此凡列具体条目/字段名，整体都要加"据公开样本，Apple 未文档化"的限定【一手-文档：本次未获得可核实条目，见缺口清单 §D】。

这张三级分层的意义在于防止一种常见滑坡：把"社区都在用这个名字"读成"Apple 定义了这个名字"。panic 主题里几乎每个条目名都来自后者，若不分级，报告就会在字段含义、生成时机、分发范围上替 Apple 补出它从未写过的口径。下面各节都按这个框架给每个名字配一个状态标签。

分析数据的查看路径则是 Apple 一手记载的："You can review this information on your iOS device by going to **Settings > Privacy & Security > Analytics & Improvements** and tapping **Analytics Data**."【一手-文档 https://www.apple.com/legal/privacy/data/en/device-analytics/ 】；APSG 独立给出同一父路径【一手-文档 apsg.txt:2650-2655】。这是本主题少数可当作官方口径引用的句子。

把"Apple 正式记载"这一级到底覆盖了什么列清楚，才能防止把 L2 观测误升为官方：

- Apple 一手确证的：查看路径（Analytics & Improvements → Analytics Data）、四个分析开关逐字名、"仅用户同意才发往 Apple"这一发送条件；
- Apple 一手确证的（源码级，非用户文档）：paniclog 由内核在 panic 路径上不刷新地追加文本、是否含 kalloc 统计取决于 `panic_include_kalloc_types`；
- 属 L2/研究界观测的：`panic-full` 条目名、`panicString`/`panicFlags`/`bug_type` 字段、"内核崩溃多指向硬件"的经验；
- 属未确证/未获可核实条目的：`FallToPanic`、`retained-bytes-...`、`loader_flags`、`debugInfo`、`mntn_info`、`console-ramoops`、`avail-rambytes`、PAC/BTI 是否显式入 panicString。

分级纪律就一句：后两类不得越级写成"Apple 记载"；引用时句内须出现"Apple 未文档化 / 据公开样本 / 研究界观测 / 未确证"之一。

这条纪律在本章之所以格外重要，是因为 panic 主题恰好落在"Apple 记载极少、社区观测极多"的区间：用户文档层面几乎只剩"去哪看分析数据"和"哪个开关控制发送"两句，字段与条目全凭公开样本。若不做分级，报告很容易把维修圈流传的字段名当成 Apple 规范来引用，进而在事件响应里给出经不起复核的判断。因此本章每一处字段/条目，都按上表标好等级，宁可不写也不越级升格——这是可观测性写作与机制写作最不一样的地方。

## 16.2 数据分区名与文件名的核实结果

逐个核名，确证的与未确证的分开列：

| 条目名 | 核实状态 | 依据/说明 |
|---|---|---|
| `Analytics Data`（父目录） | 已核实 | Apple 法务页 + apsg.txt:2650-2655 一手路径 |
| `panic-full` | **部分核实** | 维修/研究界公开样本与讨论区以连字符形式指称（Apple 讨论区 thread/252205390 标题 "Panic-full, bug_type 210"、iFixit Wiki）；Apple 未在任何一手文档列名 |
| `FallToPanic` | **未核实（未获可核实条目）** | 精确串检索未命中任何 Apple 一手页面或权威来源，不得当作已确证分区名 |
| `retained-bytes-due-to-background-sandbox` | **未确证** | 带引号精确短语检索未命中 Apple 一手文档，拼写存疑 |

> **口径冲突（§F）**：`panic-full` 的拼写在二手来源内部就分歧——iFixit 正文写作 `panic.full`（点号），而 Apple 讨论区标题与多数样本写作 `panic-full`（连字符），且 **Apple 一手文档未列名该条目**，两侧皆非官方口径。收口写法：采用通行连字符 `panic-full`，并标注"Apple 未公开列举条目名，二手来源存在点号/连字符拼写分歧"。

Apple 对"这些条目何时出现"的官方表述同样未获可核实条目：APSG 检索 `panic log`/`panic report`/`Analytics Data` 均无"条目生成时机"的说明，支持文档只写"数据含 crash logs"【一手-文档 https://support.apple.com/en-ke/108971 】。所以报告不可断言"每次内核 panic 必产生某条目"。唯一可引的"何时会 panic"一手表述出现在 macOS 章节——"If an IOMMU detects an attempt by a peripheral to access memory that isn't mapped for that peripheral's use, **it institutes a kernel panic**."【一手-文档 apsg.txt:3486-3488】，须限定为 macOS/Apple silicon 外设 DMA 语境。

## 16.3 字段名逐个核实与未确证项

panic 报告内部的字段名，本简报逐个核过。汇总：**字段清单本身没有 Apple 一手口径**，凡列字段须整体标注"据公开样本，Apple 未文档化"。

| 字段 | 结论 | 依据 |
|---|---|---|
| `panicString` | 部分核实 | 研究/维修界公开样本普遍出现（iFixit Wiki）；Apple 一手无字段定义表 |
| `panicFlags` | 部分核实 | 同上；**取值位含义 Apple 未公开** |
| `bug_type` | 部分核实 | 出现在 Apple 讨论区样本标题（thread/252205390）；含义 Apple 未公开 |
| `loader_flags` | 未确证 | 本次未在 Apple 一手来源确证 |
| `debugInfo` | 未确证 | 本次未获一手依据 |
| `mntn_info` | 未确证 | 本次未获一手依据 |
| `console-ramoops` / `avail-rambytes` | 未确证 | 未命中 Apple 一手；本地 XNU 抓取树检索 `ramoops` **0 命中**（Linux 侧同名机制 docs.kernel.org/admin-guide/ramoops.html 属另一内核，不可混用） |
| `translation` | 未确证 + 语义须标注 | 见 16.4 |

源码侧可核的是 panic 文本的生成机制，而非字段表。内核在 panic 路径上以"不刷新"方式把诊断文本追加进 paniclog：`paniclog_append_noflush()`，逐字文本含 "Zone (suspected) leak report:\n"、"Largest zones:"、"Kernel Stacks"/"PageTables"/"Kalloc.Large"【一手-源码 src/zalloc.c:3463-3485、7614-7674】。这说明 paniclog 内容是**诊断文本快照**，不是结构化字段表——字段名来自上层解析工具与公开样本，而非内核直接输出的命名约定。

由此可推出一条方法性结论（分析推断，非事实断言）：

- 内核侧一手能确证的是"哪些文本被写进 paniclog"，不能确证的是"解析后叫哪个字段名"；
- `panicString`/`panicFlags`/`bug_type` 这类名字属上层/公开样本约定，故只能部分核实；
- `loader_flags`/`debugInfo`/`mntn_info`/`console-ramoops`/`avail-rambytes` 本次连一手出处都没确证，属未确证，须显式标"待核"；
- `ramoops` 在 Linux 侧是既有机制（docs.kernel.org），但本地 XNU 树检索 0 命中，不可把 Linux 口径搬来当 iOS 字段。

## 16.4 translation 字段与内核 slide 的推断红线

`translation` 是本主题里最容易被写成"事实"的一个字段，必须单列红线。可核实的部分是：该字段确在公开 panic 记录中被广泛引用。不可核实的部分是：**Apple 未公开定义其语义**。把它等同于"内核 slide（kernel slide）"，属于**研究界推断**，不是官方口径【一手-文档：语义未获可核实条目】。

这条与第 10 章（地址随机化）的口径直接挂钩：Apple 官方文档只称 ASLR，源码/研究界称 KASLR，随机量正式名是 `vm_kernel_slide`；零售 iOS 上可读的只有 `kern.slide` 的是/否布尔值，不是具体 slide 值。因此报告引用 `translation` 时的安全写法是——

> `translation`（Apple 未定义其语义；研究界普遍解读为与内核 slide 相关——此为推断，非官方口径）。

关于这个字段能安全说与不能说，边界是这样：

- 可说：该字段在公开 panic 记录中被广泛引用；其命名与布局无 Apple 一手文档定义；
- 不可说：不能把它等同于内核 slide、不能写成"KASLR 地址可从 panic 直接还原"；
- 与第 10 章一致的事实是：即便 `translation` 真与 slide 有关，零售侧可官方读到的也只是 `kern.slide` 的是/否布尔值，`kern.kernel_slide` 等仅在 DEBUG/DEVELOPMENT 编译；
- 因此任何"从 panic 还原具体内核基址"的结论都越过了公开口径，属分析推断，须显式标注，不得作为事实断言写入。

不得写"从 `translation` 可直接读出内核 slide"，也不得写"`translation` 就是 KASLR slide"。是否把该字段写入任何结论句，都必须带"Apple 未定义语义"的限定语。这是简报 §E 第 35 条与 §D 第 23 条对本章的硬约束。

## 16.5 官方分析数据开关与「私有报告」误解校正

Apple 一手逐字出现的分析开关名，只有下面这几个【一手-文档 apsg.txt:2650-2655；法务页/支持页】：

| 逐字开关名 | 语境 | 出处 |
|---|---|---|
| `Share iPad Analytics` | iPad | apsg.txt:2650 |
| `Share iPhone & Watch Analytics` | iPhone | apsg.txt:2652 |
| `Share Mac Analytics` | Mac | apsg.txt:2654 |
| `Share With App Developers` | 面向第三方开发者 | app-analytics 法务页、support/108971 |

配套原文限定："Analytics related to removal recommendations ... are sent to Apple **only if the user has agreed to share this information using the following settings**"【一手-文档 apsg.txt:2646-2649】。开发者可见范围按法务/支持页口径是"帮助开发者改进其 App"，即 **App 级崩溃诊断**；至于**内核 panic 记录是否会随该开关分发给第三方开发者，Apple 未公开**——这是缺口，不得断言会或不会。

这几个开关的语义要各归各位，不能混：

- `Share iPad Analytics` / `Share iPhone & Watch Analytics` / `Share Mac Analytics`：控制是否把分析数据发往 Apple，按设备形态分别命名（逐字须带 `&` 与大小写）；
- `Share With App Developers`：控制是否把（App 级）诊断分享给第三方开发者，与前三个是不同维度；
- "仅用户同意才发送"是这些开关共同的默认前提，报告描述分发时必须保留这一条件；
- 内核 panic 记录是否落入 `Share With App Developers` 的分发面：Apple 未公开，属缺口，不得写成"开发者可收到 panic 报告"。

> **误解校正**：把某个分析开关条目描述成"iCloud 私有报告 / 私有 iCloud 分析"一类，本次在 Apple 一手来源检索 `panic`/`App Developers`/`private reports` 组合中未出现该措辞（APSG 全文无匹配）。Apple 一手逐字名只有上表四个。"iCloud 私有报告"一类措辞 Apple 未使用，不得写入正文（归缺口，不反向断言该措辞指向的真实条目不存在）。

源码侧还有两处机制值得作交叉引用，但不在此重复展开：其一，panic 是否含 kalloc 类型统计，取决于 `bool panic_include_kalloc_types = false;`（默认关），在特定条件下被置 `true` 后 `if (panic_include_kalloc_types) { ... }` 才决定是否输出【一手-源码 src/zalloc.c:454、696、710、7790】——该值语义属第 5 章内核内存一节，此处仅引其"是否输出取决于该值"这一可观测结论；其二，不可捕获异常路径上 `exit_with_mach_exception()` 硬编码 `OS_REASON_FOUNDATION`【一手-源码 osfmk/kern/exception.c:876-891】，属**进程退出原因**层（进 Crash Report），与**内核 panic** 是两条不同可观测通道，须区分"App 崩溃报告"与"panic 报告"——分层细节见第 13 章。

这条区分对读报告很关键：`OS_REASON_*` 是"某个进程为什么退出"的编码，落在 App Crash Report；`panicString` 是"整机为什么内核崩溃"的快照，落在 panic 报告。两者字段族不同、生成路径不同、分发开关也不同。Apple 一手里明确的是 App 级崩溃诊断经 "Share With App Developers" 供开发者改进 App；内核 panic 记录是否会走同一分发面，Apple 未公开，属缺口。把 App Crash Report 里能看到的字段规律直接套到 panic 报告上，是本主题最常见的误读。

## 16.6 事件响应能支持与不能支持的判断

"看不到什么"最好用 Apple 自己的文档原文来支撑。随源码发布的 `src/doc/extensible_paniclog.md` 原文写明这套基础设施的边界："With this feature we want to provide an infrastructure for **kexts / dexts** to insert their system state into the paniclog. **Currently there is no way of knowing the kext or dext state unless we take a full coredump.**"；并且 "An instance is collected and put into the panic file **only if it's active**."；解码侧 "This mapping will be used to decode the data **in DumpPanic or a tool integrated into MPT**."（接口头文件 `iokit/IOKit/IOExtensiblePaniclog.h`）【一手-源码 src/doc/extensible_paniclog.md】。即：**Apple 自己承认 panic 报告不含完整内存取证**——驱动侧状态要么主动写入 paniclog、要么就得取全量 coredump，且只有 active 实例才进 panic file，解码依赖 DumpPanic/MPT 工具。

至于"PAC/BTI 违规是否会显式出现在 panicString 里"——**Apple 正式记载本次未获得可核实条目**。APSG（August 2026）无任何 panic 字段/`panicString` 语义章节，检索 `PAC`/`Pointer Authentication`/`BTI` 与 `panic` 同现表述为 0。源码侧只能核到"机制存在"层级：BTI 违规以异常形式被内核捕获（第 12 章 platform_tests.c）、`pac_exception_telemetry` 与 `CA_EVENT(pac_exception_event, ...)` 存在（第 13 章 exception.c:791-800）、`bsd/dev/dtrace/scripts/ptrauth_arm64.d` 在树内。因此这一条必须**降级为研究界/维修界观测**，不能写成 Apple 官方口径。维修/研究界普遍观测"iOS 内核崩溃几乎总是指向硬件问题、panic 计数持续增长是硬件故障经验依据"【L2 iFixit Wiki】，同样须标"研究界/维修界观测，Apple 未正式记载"。

> 禁写："从 panic 报告可读出 PAC/BTI 是否开启/是否被绕过"。可写："PAC 校验失败与 BTI 违规在源码层面走内核异常路径并有遥测事件定义，其在零售设备公开日志中的可见性 Apple 未公开。"

下面这张"能/不能"清单**整体标注为分析推断**（§F 段，非事实断言）：

| 能支持的判断 | 不能支持的判断 |
|---|---|
| 机型与 iOS/kernel 版本 | 还原攻击链（panic 是终止瞬间快照，非审计日志） |
| 是否发生非正常重启（时间戳对齐用户可感知重启） | 完整内存取证（Apple 原文："no way of knowing the kext or dext state unless we take a full coredump"） |
| 复发性与频率（同一 `panicString` 反复→偏稳定性/硬件，非单次入侵） | 区分"保护被触发"与"保护被绕过"（PAC/BTI 无 Apple 字段级口径） |
| 是否存在 kalloc/zone 异常膨胀（源码可写入 paniclog） | 据此判定"被在野利用"（须回到 Apple 安全公告原文，见第 17 章） |
| 是否有 dext/kext 主动落盘的状态块（仅 active 收集） | — |

分析推断的实操提示：分析数据需在用户开启 "Share iPhone & Watch Analytics" 时才发往 Apple，本地 `Analytics Data` 条目的**保留时长/轮转策略 Apple 未公开**，故响应流程宜在设备重启后尽快导出；`translation` 等未定义字段写入结论时必须带"Apple 未定义语义"限定。

把"看不到什么"再收紧一层，可归纳成三条被 Apple 文档本身锁死的边界：

- **深度边界**：panic 报告不含完整内存取证——Apple 原文 "Currently there is no way of knowing the kext or dext state unless we take a full coredump"，驱动状态要么主动写入、要么需全量 coredump；
- **覆盖边界**：只有 active 的实例才进 panic file（"An instance is collected and put into the panic file only if it's active."），非活跃态不留痕；
- **工具边界**：paniclog 里的扩展状态解码依赖 DumpPanic 或 MPT 集成工具，不是"打开就能读"的明文，这决定了自动化解析在公开侧并不完整。

这三条合起来说明：把 panic 报告当"内核行为审计日志"来用是高估了它。它是终止瞬间的诊断快照，能支撑"版本/是否重启/是否触发保护 fault"这类粗粒度判断，但不支撑"攻击链重建"或"保护被绕过"的细粒度结论——后者要么需要全量 coredump，要么需要 Apple 未公开的字段语义。整段的"能/不能"划分都应作为分析推断引用，而非事实断言。

### 本章口径边界

- 证据分三级：Apple 正式记载 / 研究界维修界观测 / 未确证；panic 报告字段清单整体"据公开样本，Apple 未文档化"。
- `panic-full` 采通行连字符写法并标"Apple 未列名、二手存在点号/连字符分歧"；`FallToPanic`、`retained-bytes-due-to-background-sandbox` 未确证，须标"待核"或不写。
- `loader_flags`/`debugInfo`/`mntn_info`/`console-ramoops`/`avail-rambytes` 未确证；本地 XNU 树 `ramoops` 0 命中。
- `translation`：Apple 未定义语义，等同内核 slide 属研究界推断，须与第 10 章口径一致并加限定。
- 开关名只用四个逐字集；"iCloud 私有报告"一类措辞 Apple 未使用，作误解校正处理。
- PAC/BTI 是否显式入 panicString：Apple 正式记载未获得，降级为研究界/维修界观测；事件响应"能/不能"清单整体标注为分析推断。

---

# 内核侧漏洞的公开口径：组件名、影响表述与利用标注

本章只到"Apple 官方安全公告（About the security content of …）里的**组件名、Impact 摘要与是否标注被利用**"这一层。**不描述任何一枚漏洞的利用 technique，不给出可利用路径**；所有条目均来自已逐条核对过 Apple 官方页面的核实表，**不新增任何 CVE**。这一层的价值在于：报告若要引用"内核侧组件"或"在野利用"，必须落在 Apple 的原文口径上，而不是第三方漏洞库的归类或社区措辞。

## 17.1 Apple 公告里的组件用词表

下表是在本次抓取的 iOS/iPadOS 公告页中确证出现过的内核侧组件名（全部为 Apple 页面原文，逐字）：

| 组件名（Apple 原文） | 出现语境（示例条目） |
|---|---|
| `Kernel` | CVE-2024-23225、-23265、-27815、-27840、-44239、2025-24159、-24144、-43520、-46285、2026-20626 等【一手-文档 support.apple.com】 |
| `AppleMobileFileIntegrity` | CVE-2024-27816 |
| `AppleKeyStore` | CVE-2026-20637 |
| `GPU Drivers`（复数） | CVE-2024-40854 |
| `IOMobileFrameBuffer` | CVE-2024-44241 / -44242 / -44299 |
| `RPAC` | CVE-2025-31201 |
| `CoreAudio` | CVE-2025-31200 |
| `Accessibility` | CVE-2025-24200 |
| `Messages` | CVE-2025-43200 |
| 具体驱动名（IOKit 类） | `IOSurface`、`IOSurfaceAccelerator`、`AppleAVD` 等 |
| 其他 | `Sandbox` / `Sandbox Profiles`、`libxpc` |

要点：Apple 从不把 IOKit 类漏洞写成一个笼统的 `IOKit` 组件名，而是落到 `IOSurface`/`IOMobileFrameBuffer`/`IOSurfaceAccelerator`/`AppleAVD` 这类具体驱动名（第 9 章已述这些是用户态可触达的高暴露驱动面）。发布日期一律保留页面原文形态，含 "Released February 10, 2025"、"Entry added June 11, 2025" 这类注记。

再用几个"首个修复版本"的例子说明这张表的可引用边界（均取页面标题原文，不改写）：`Kernel` 条目跨 iOS 17.4、17.5、18.1、18.3、26.1、26.2、26.3 多个版本持续出现；`AppleMobileFileIntegrity`（CVE-2024-27816）与 `AppleKeyStore`（CVE-2026-20637）是 Apple 单列出的内核侧组件名；`IOMobileFrameBuffer` 三连（-44241/-44242/-44299）的 Impact 落到 DCP 固件而非内核。每一行都应能回溯到 support.apple.com 的具体公告页，报告引用时把 URL 级出处带住，不跨库合并。

读这张组件表要避免三个常见动作：

- 不要把 `Kernel` 当成"什么内核问题都往里塞"的兜底名——Apple 同时单列 `RPAC`、`CoreAudio`、`GPU Drivers`、`AppleMobileFileIntegrity` 等，说明它做的是细粒度归类；
- 不要把具体驱动名概括回 `IOKit`——`IOSurface`/`IOMobileFrameBuffer`/`IOSurfaceAccelerator`/`AppleAVD` 是 Apple 用的名字，合并会丢信息；
- 不要按 Impact 反推组件——组件名与 Impact 各取各的原文，二者不互相改写。

## 17.2 明确未出现的组件写法

与 17.1 等重的，是把**没有出现过的写法**也列清楚。在本次抓取的 iOS/iPadOS 公告页里，以下组件写法**未出现**：

| 未出现的写法 | 说明 |
|---|---|
| `IOKit`（作为单一组件名） | Apple 用具体驱动名（IOSurface 等），不用笼统 `IOKit` |
| `Kernel Cache File` | 本次抓取的 iOS/iPadOS 页面未出现该组件名 |
| `Mach IPC` | 本次抓取的 iOS/iPadOS 页面未出现该组件名 |
| `GPU Driver`（单数） | Apple 原文用复数 `GPU Drivers`（见 17.5） |

> **口径红线**：报告若要写"Apple 用 `Kernel Cache File` / `Mach IPC` / 单一名 `IOKit` / `GPU Driver`（单数）作为组件名"，**须先补 macOS 公告的一手证据**，否则按"不可用"处理。本次未抓取 macOS 公告一手证据，所以这几种写法整体只能标为不可用，既不能采用，也不能断言它们绝不出现在别处。【一手-文档：本次抓取范围仅 iOS/iPadOS 公告页】

这份"未出现"清单的用途是给报告设一道闸：社区文章常把内核侧漏洞笼统标成 `IOKit`、`Mach IPC` 或 `Kernel Cache File`，但这些不是 Apple 在 iOS/iPadOS 公告里用过的组件名列值。报告若沿用这些写法而不补 macOS 一手出处，就会把"社区归类"混成"Apple 口径"。正确做法是要么改用 17.1 里确证过的具体驱动名，要么显式注明"此为第三方归类，非 Apple 公告组件名"。

## 17.3 Impact 表述的语义分层

Apple 的 Impact 摘要不是同质的一句话，它按"攻击者已处于什么前置状态"分层。下面每行都是**公告原文摘录**，报告须按其前置状态归类，**不得自行升级或降级**（把"An app may be able to ..." 写成"attacker achieves kernel code execution"就是升级，不允许）：

| Impact 原文（摘录） | 前置状态语义 | 示例条目 |
|---|---|---|
| "An app may be able to cause unexpected system termination or write kernel memory" | 本地 App 触发，含崩溃或写内核内存 | CVE-2024-23265、2025-43520 |
| "An app may be able to execute arbitrary code with kernel privileges" | 本地 App 获得内核权限执行 | CVE-2024-27815、2025-24159 |
| "An app may be able to leak sensitive kernel state" | 本地 App 泄露内核状态 | CVE-2024-44239、2025-24144 |
| "An attacker that has already achieved kernel code execution may be able to bypass kernel memory protections" | 已具内核执行权限后的进一步绕过 | CVE-2024-27840 |
| "An attacker with arbitrary kernel read and write capability may be able to bypass kernel memory protections" | 已具任意内核读写后的绕过 | CVE-2024-23225 |
| "An attacker with arbitrary read and write capability may be able to bypass Pointer Authentication" | 已具任意读写后的 PAC 绕过 | CVE-2025-31201（组件 `RPAC`） |
| "An app may be able to gain root privileges" / "A malicious app may be able to gain root privileges" | 提权 | CVE-2025-46285、2026-20626 |
| "An app may be able to cause unexpected system termination" | 崩溃/终止 | CVE-2024-40854（`GPU Drivers`）、2026-20637（`AppleKeyStore`） |
| "...unexpected system termination or arbitrary code execution in DCP firmware" | DCP 固件侧 | CVE-2024-44241/44242/44299 |

分层要点：`bypass kernel memory protections` 与 `bypass Pointer Authentication` 都以"已具备更高能力"为前提（arbitrary read/write、kernel code execution），它们描述的**不是**从零到内核的这一步；把它排在"execute arbitrary code with kernel privileges"之前，就误读了 Apple 的前置语义。这一层只归类表述，不解释任何一列是如何达成的。

引用 Impact 时守住这几条（均不引入 technique）：

- 逐字摘录，不改主语：Apple 写 "An app may be able to ..." 就不能写成 "an attacker gains ..."；
- 前置不省略：`already achieved kernel code execution`、`arbitrary read and write capability` 这类前提短语必须随 Impact 一起带上，否则读者会误以为该缺陷单独就能拿到内核；
- 组件与 Impact 配对引用：同一 CVE 的组件名（如 `RPAC`）与 Impact（如 "bypass Pointer Authentication"）应成对给出，避免只取一半造成归类漂移；
- 不合并跨条表述：`unexpected system termination` 与 `... or write kernel memory` 属不同句，不得只保留一半当作统一模板。

## 17.4 被标注利用条目的措辞区分

在本次核实表里，**只有 5 条**带 Apple 的"疑似被利用"措辞，且措辞分两档，须逐字区分，不得合并成"在野利用"一句带过：

| CVE | 组件（Apple 原文） | Apple 标注措辞（原文） |
|---|---|---|
| CVE-2024-23225 | `Kernel` | "Apple is aware of a report that this issue **may have been exploited**." |
| CVE-2025-31201 | `RPAC` | "...may have been exploited **in an extremely sophisticated attack against specific targeted individuals on iOS**." |
| CVE-2025-31200 | `CoreAudio` | 同上措辞（与 -31201 同批，iOS 18.4.1，April 16, 2025） |
| CVE-2025-24200 | `Accessibility` | "...may have been exploited **in an extremely sophisticated attack against specific targeted individuals.**"（iOS 18.3.1，Released February 10, 2025） |
| CVE-2025-43200 | `Messages` | 同上措辞（同页，Entry added June 11, 2025） |

两档的差异是有意的：`may have been exploited` 只声明"存在被利用的报告"，不附加场景；`extremely sophisticated attack against specific targeted individuals` 则进一步限定攻击复杂度与"针对特定个体"，其中 CVE-2025-31201/31200 原文还额外点明 "on iOS"。报告引用时必须照录所属档位，不能把前者补写成"定向攻击"，也不能把后者弱化成"可能有利用"。

其余条目（如 -23265、-27815、-27840、-44239、-40854、-44241/42/99、-24159、-24144、-43520、-46285、2026-20626、2026-20637）页面原文**没有** "Apple is aware of a report…" 句。

> **红线**：不得把未带该措辞的条目统称"已被在野利用"。"是否被利用"这一判断，只能回到 Apple 公告的措辞本身；本表之外的任何在野定性都不属于本章口径。

需要补一句口径边界：这 5 条里，Apple 用词是 "may have been exploited"（存在被利用的报告），并非 "confirmed exploited"；措辞强度以公告原文为准，报告不得替 Apple 升级成"确认在野利用"。同时，"被利用"标注与"前置能力"标注（17.3）是两回事：一条 CVE 可以同时带 `arbitrary read and write` 前置与 `may have been exploited` 标注（如 CVE-2025-31201），也可以带提权 Impact 却**无**任何利用标注（如本表里 Impact 为 "gain root privileges" 的 CVE-2025-46285、CVE-2026-20626 都无 "Apple is aware of a report…" 句）——两者各自照录，不可互推。

## 17.5 RPAC 与 GPU Drivers 的口径冲突与取证缺口

**§F 冲突之一：`RPAC` 组件归属。** Apple 公告把 CVE-2025-31201 的组件名单列为 `RPAC`（独立于 `Kernel`），Impact 原文明写 "bypass Pointer Authentication"，Description 为 "addressed by removing the vulnerable code"【一手-文档 https://support.apple.com/en-us/122282】。但其他漏洞库口径不一致：KEV 侧把它与 CoreAudio/RPAC 一起标为 PAC-bypass 类，另有数据库把它归为 XNU 的任意读写。

> 收口写法（并列两侧，不择一掩盖）：**组件名以 Apple 逐字 `RPAC` 为准**；如需表达"属内核/PAC 相关"，须另加解释性括注，不得改写 Apple 的组件名列。第三方库把它归到 `Kernel` 或标成"XNU 任意读写"，是另一套分类口径，可作为对照列出，但不能替换 Apple 原文组件名。同时，`RPAC` 具体指代哪个内核组件，Apple 未在公告里进一步展开——这是口径缺口，不得自行填充其内部含义。

**§F 冲突之二 + 取证缺口：`GPU Drivers`（复数）。** Apple 页面原文组件名为复数 **`GPU Drivers`**（CVE-2024-40854，https://support.apple.com/en-us/121563 ），社区/CVE 库常误写单数 `GPU Driver`。报告须逐字照录复数并括注"Apple 用复数"。至于把 GPU 驱动类漏洞关联到 2025 年在野链 DarkSword 的 AGX 部件，这类关联**本次只到二级来源（安全媒体转述）**，一手应指向 Apple security releases 的组件名，而该页 JS 渲染本次未抓到组件名【L2 媒体/年份】——因此 GPU/DarkSword 的驱动级归因只能标为"公开研究/媒体转述"，取证不闭合。这与第 9 章对高暴露驱动（IOSurface/AGX）"机制可核、具体在野归因待补一手"的处理一致，交叉引用第 9 章，不在此重复其技术面。

两处冲突的统一处理原则：

- 冲突并列，不择一掩盖：Apple 原文口径（`RPAC`、`GPU Drivers`）始终作为"组件名列"的唯一权威，第三方归类只作对照说明；
- 归因不越级：只到二级来源的关联（DarkSword/AGX）标"公开研究/媒体转述"，不得写成 Apple 口径或一手确证；
- 缺口不填充：`RPAC` 内部指向、macOS 侧是否另有 `Kernel Cache File` 等，Apple 未展开或未抓取到的，一律留为缺口，不臆测；
- 技术面不外溢：本章只到"组件名 + Impact 摘要 + 是否被标注利用"，不写任何 technique 或利用路径，驱动技术细节归第 9 章。

## 17.6 不可用条目与统计缺口

有些常被一并列入"内核侧漏洞"的 CVE，**本次未在已抓取的 Apple 页面确证组件归属**，因此不进入 17.1–17.5 的任何统计，按不可用处理：

| CVE | 处理 | 原因 |
|---|---|---|
| CVE-2024-27822 | 不纳入统计 | 本次未在已抓取 iOS/iPadOS 公告页确证组件归属 |
| CVE-2023-41064 | 不纳入统计 | 同上，组件归属未确证 |
| CVE-2024-23222 | 不纳入统计 | 在 iOS 17.3 页面中体现为 WebKit 侧 Impact 文本，内核组件归属未确证 |

> **红线（缺口不反向断言）**：以上三条只表示"本次未取到一手组件归属依据"，**不等于它们与内核无关、也不等于它们可归入内核**。统计口径必须与之对齐：本章所有计数、组件表、被利用标注，都只覆盖已逐条核对过的条目，不可用条目一律不计入。

由此把本章能产出与不能产出的东西界定清楚：

- 能产出：一张以 Apple 原文组件名为列、以 Impact 摘要与被利用标注为行的清单，覆盖 17.1–17.5 已确证条目；
- 能产出：`GPU Drivers`（复数）、`RPAC` 这类逐字组件写法，以及"未出现写法"的负向清单；
- 不能产出：任何一枚漏洞的利用 technique、可利用路径、组件内部实现细节；
- 不能产出：把未带 "Apple is aware of a report…" 的条目并入在野利用统计；
- 不能产出：对不可用条目（CVE-2024-27822、2023-41064、2024-23222）或 macOS 专属组件名（须先补一手）作任何肯定性归类。

同理，`Kernel Cache File`/`Mach IPC`/单一名 `IOKit`/`GPU Driver`（单数）是否出现在 **macOS** 公告页，本次未抓取 macOS 公告一手证据，整体按"不可用"处理（见 17.2）。这意味着：**本报告在"内核侧漏洞公开口径"这一章能安全给出的，只是一张以 Apple 原文组件名与 Impact 摘要为界的清单**；任何超出这层——利用 technique、组件内部实现、跨库合并归类——都不属于本章可用口径。本章自始至终不描述任何一枚漏洞的利用 technique，也不给出可利用路径。

### 本章口径边界

- 只使用已逐条核对过 Apple 官方"About the security content of …"页面的条目，**不新增任何 CVE**，不描述利用 technique。
- 组件名逐字照录，尤其 `GPU Drivers`（复数）、`RPAC`；Impact 句按原文摘录并按前置状态归类，不自行升级/降级。
- 只有 CVE-2024-23225、2025-31201、2025-31200、2025-24200、2025-43200 可写"Apple 标注可能被利用"，且须区分两档措辞；其余无 "Apple is aware of a report…" 句，禁止统称在野利用。
- `RPAC` 归属与他库口径冲突须并列两侧、以 Apple 逐字名为准；`GPU Drivers` 复数须照录；DarkSword/AGX 归因只到二级来源，作取证缺口，交叉引用第 9 章。
- CVE-2024-27822、2023-41064、2024-23222 组件归属未确证，不纳入统计；`Kernel Cache File`/`Mach IPC`/`IOKit` 单一名/`GPU Driver` 单数须先补 macOS 一手证据，否则按不可用处理。

---
# 横向汇总：机制 × 生效前提 × 可观测性

## 18.1 汇总方法：把每章的四元组投影到同一坐标系

第 3 章到第 17 章是按机制纵向展开的：每一章在结尾都固定回答了四个问题——该机制**负责什么**、**强制点落在哪一层**、**它生效需要哪些前提**、**它明确不覆盖什么**。纵向写法的好处是能把一条链路讲透（例如一次 `exec` 里 bsd → MACF → AMFI → pmap/CSM 的接力），代价是读者读完九章仍然无法回答一个更实用的问题：**这些机制在"是否需要额外条件才真正起作用"这件事上，是不是同一类东西？**

本章的汇总做法是把九个机制的这四个问题投影到同一组坐标轴上，形成一张矩阵。坐标轴的取法如下：

| 轴 | 含义 | 取自前文的哪一类结论 |
|---|---|---|
| 强制层级 | 该机制在内核里的强制点位于哪一层，以及该层的存在性本身是什么证据等级 | 第 3、6、8、9、13 章的"位置"小节 |
| 生效前提 | 机制从"代码里存在"变成"设备上执行"必须成立的条件 | 各章"失效模式"小节 |
| 零售设备可观测性 | 在不借助研究设备、不越狱、不调试内核的前提下，能否从设备侧读出该机制的状态 | 第 10、12、14、16 章的可见性讨论 |
| 依据层级 | 该格判断所依赖的来源类型（Apple 对外文档 / XNU 开源树 / 随源码发布的官方 doc 与 man page / 公开研究 / 分析推断） | 第 2 章的六级来源体系 |
| 缺口性质 | 若不成立或不可知，缺口属于哪一类：官方口径缺失、构建取值不公开、闭源实现不公开，还是本地镜像节选导致的取证限制 | 第 2 章的失真源清单与两份核查简报的缺口清单 |

投影时的两条纪律。第一，**矩阵不引入新事实**：每一个格子都必须能指回第 3–17 章已建立的机制陈述，本章只做归类与并排。第二，**空格不留**：当一格无法归类时写「证据不足」并说明是哪一类不足，而不是留白或按常识补全。这两条纪律来自本篇方法章确立的原则——负面结果（检索命中 0 次、URL 实测 404、宏在树内无定义处）本身是证据，而猜测不是。

## 18.2 判定值的四个档位与依据层级标注规则

矩阵里每格的**判定值**只允许四个档位，含义如下：

| 判定值 | 含义 | 使用时的约束 |
|---|---|---|
| 公开可证 | Apple 面向用户/开发者的对外文档、或 Apple 撰写并随源码公开的官方 doc 与 man page，直接支持该陈述 | 仍需保留版本口径（如 Apple Platform Security 落款 2026 年 8 月），不可写成"最新版" |
| 源码可证但 Apple 未承诺 | XNU 开源树里能读到实现、宏名、注释原文，但 Apple 对外文档没有把它写成一项承诺或规格 | 必须写成"源码事实/研究界用语"，禁止加引号当 Apple 术语（典型如 KASLR） |
| 未公开（审计缺口） | 该事实存在与否或取值如何，本次核查未取得一手依据 | 只陈述"未取得依据"，不反向断言存在或不存在 |
| 不适用（该平台无此机制） | 该机制在讨论的平台上不存在，或该格对该行机制没有意义 | 仅用于平台归属明确的条目（如第三方 kext/AuxKC 只在 macOS） |

**依据层级**这一列不做四档判定，而是照抄来源类型标签。本篇前文沿用的六级标签与两份核查简报内部的等级标记之间对应关系如下，附录 B 的逐行标注也以这张对照为准：

| 正文标签 | 前半部分简报内的写法 | 后半部分简报内的写法 | 说明 |
|---|---|---|---|
| 一手-文档 | 【一手-文档】 | L1（Apple 官方 PDF/支持页/开发者页） | Apple 对外发布物 |
| 一手-源码 | 【一手-源码】 | L1（XNU main 分支，tree sha f6217f8…） | 随 APSL 2.0 开源的代码 |
| 一手-官方doc | 【一手-官方doc】 | 归入源码侧一手 | `xnu` 仓库 `doc/` 下 Apple 撰写的说明，公开但非面向用户 |
| 手册页 | 【手册页】 | 随源码发布的 `osfmk/man/*.html` | 收录范围有限，部分 mach 手册页 URL 实测 404 |
| 公开研究 | 【公开研究】 | L2 | 会议论文、厂商文档、维修界经验，必须句内标源 |
| 分析推断 | 【分析推断】 | 标注为分析结论 | 只用于说明机制边界，不冒充事实 |

一个反复出现的判读要点：**"源码可证但 Apple 未承诺"不等于"Apple 否认"**。第 2.4 节已确立，源码讲实现但不讲构建取值，对外文档讲能力但不讲实现，二者之间是一段审计缺口，而不是一处矛盾。本章矩阵把大量格子落在第三档，正是这段缺口的形状。

## 18.3 面板 A：分层、IPC、内存分配、MACF 与 AMFI

面板 A 覆盖第 3 至第 7 章的五类机制。每格形式为「判定值（依据层级）：内容」。

| 机制 | 强制层级 | 生效前提 | 零售设备可观测性 | 依据层级 | 缺口性质 |
|---|---|---|---|---|---|
| XNU 分层与构建配置 | **公开可证**（一手-文档级自述，实为仓库 README）：hybrid kernel 三要素（CMU Mach + FreeBSD 组件 + IOKit C++）为 Apple 自述原文，目录职责 osfmk/bsd/libkern/security/pexpert 一一对应 | **源码可证但 Apple 未承诺**（一手-源码）：同一棵树经 `config/MASTER.arm64.*` 与功能开关（`SECURE_KERNEL` 等）产出不同内核；"大幅改动 Mach" 非 Apple 原话 | **未公开（审计缺口）**（一手-源码）：零售内核的具体 config 取值、对应 xnu 发布标签均不可从公开树判定，本地镜像为节选副本 | 一手-源码 `src/README.md`、`config/*`；一手-文档 Apple Platform Security（无对应章节） | **未公开**（构建取值与版本锚定缺口 + 本地节选取证限制） |
| Mach IPC 能力模型 | **源码可证但 Apple 未承诺**（一手-源码 + 一手-官方doc）：port name 在源码注释里直接称 capability，`doc/ipc_security_concepts.md` 称 confused deputies；开发者文档层面无该表述 | **源码可证但 Apple 未承诺**（一手-官方doc）：IPC space policy 按进程身份/entitlement 投影为不可变 `is_policy`（并做 dPAC 加固）；pinned send right 的违规反应由 boot-arg `ipc_control_port_options` 决定，hardened 进程硬执行、其他进程返回 `KERN_INVALID_CAPABILITY` | **未公开（审计缺口）**（一手-源码级观察）：port 名额、send right 引用计数上限、msgsend 资源限额无任何官方公开文档；`mach_msg(2)` 现行手册页未取到（历史 man URL 实测 404） | 一手-源码 `ipc_object.h`/`ipc_port.h`；一手-官方doc `ipc_security_concepts.md`、`port_types.md`、`guard_exceptions.md`；手册页缺失 | **未公开**（官方口径不存在，非取证遗漏） |
| 内核内存分配 | **源码可证但 Apple 未承诺**（一手-源码）：kalloc 现为 zone 之上的 size-class 分发（`kalloc.<size>`/`kalloc.type.<size>` 命名），zone 有专属 `zone_map_range`、chunk 尾 guard page、`zm_oob_offs` | **未公开（审计缺口）**（一手-源码）：`ZSECURITY_CONFIG(ZONE_TAGGING/READ_ONLY/SAD_FENG_SHUI/PGZ_OOB_ADJUST)` 与 typed kalloc 在 iOS 生产构建的启用程度需以 config 为准，未逐条核实 | **未公开（审计缺口）**（一手-源码）：zone 名曾可经 `zone_info` MIG/`zprint` 查询，近年收紧；panic 时是否输出 kalloc 类型统计取决于 `panic_include_kalloc_types`（源码默认 false，零售取值未公开） | 一手-源码 `zalloc.c`、`vm_kern.c`（节选）；一手-文档 security.apple.com 博客（hardened allocator 自述，标题级核实） | 未公开（构建取值）+ 取证限制（本地 `vm_kern.c` 节选，`kmem_allocate()` 本体需指上游） |
| 强制访问控制框架 MACF | **源码可证但 Apple 未承诺**（一手-源码）：框架在 `security/`，注册经 `mac_policy_register()`，ops 表按 `mpo_<object>_check_<operation>` 命名；Apple 以 `#warning` 自述 MACF **非公开 KPI**（QA1574） | **未公开（审计缺口）**（一手-源码 + 分析推断）：内核侧只有调用点封装，且按 `optional config_macf` 编译裁剪；iOS 运行时实际注册了哪些策略模块由闭源组件决定 | **未公开（审计缺口）**（公开研究/二级）：iOS 启用模块清单官方从未公布，只有 kernelcache 符号观察与研究文献列出 sandbox/amfi 两个注册策略 | 一手-源码 `mac_policy.h`、`security/conf/files`、`mac_iokit.c`；公开研究（Levin 等）作次级来源 | 未公开（清单不存在于官方口径，只能降级为研究观察） |
| AMFI | **公开可证**（一手-文档）：Apple Platform Security 原文称 AMFI 为 **AppleMobileFileIntegrity kernel extension**，并明言带 `research.` entitlement 的进程在客户设备上会在签名校验时被终止 | **源码可证但 Apple 未承诺**（一手-源码）：内核经只读函数表 `amfi_t` 一次性注册消费其实现（重复注册即 panic），entitlement 判定经 CoreEntitlements V2；配置载体是 boot-args 与 `CS_CONFIG_*` 位图 | **未公开（审计缺口）**：AMFI kext 本体与 CoreEntitlements 用户态库不公开；用户态 `amfid` 的现代职责 Apple 未公开文档描述，只能标研究级；间接可观测仅剩"违规进程被终止"这一结果 | 一手-文档（指南 2026-08 版）+ 一手-源码 `libkern/libkern/amfi/amfi.h`、`libkern/amfi/amfi.c`、`kern_codesigning.c` | 未公开（闭源实现）+ 官方口径缺失（amfid 职责） |

面板 A 的三点读法。其一，**"公开可证"只出现在两处**（XNU 分层自述、AMFI 的 kext 定性与研究 entitlement 处置），且都来自 Apple 对外文档的机制定性，而非实现细节。其二，**"生效前提"列的五格里没有一格能拿到"公开可证"（三格"源码可证"、两格"未公开"）**：这说明面板 A 的机制普遍存在"读得懂实现、读不出取值"的结构性落差。其三，MACF 与 AMFI 的缺口性质相同（闭源策略实体），但**后果不同**：MACF 的缺口让"谁在被强制"不可枚举，AMFI 的缺口让"如何判定"不可复核——第 19 章会把这两类分别归入前提二。

## 18.4 面板 B：代码签名、IOKit 边界、随机化/PAC 与控制流完整性

面板 B 覆盖第 8 至第 13 章的后四类机制（其中地址随机化与 PAC 并为一行、BTI/GCS 与异常分层并为一行，理由是二者共享同一个证据形态：Apple 对外文档命中 0、实现只在源码）。

| 机制 | 强制层级 | 生效前提 | 零售设备可观测性 | 依据层级 | 缺口性质 |
|---|---|---|---|---|---|
| 代码签名强制链与 CSM | **公开可证**（一手-文档 + 一手-源码）：指南 iOS 章明言"内核启动后控制哪些用户进程可运行、要求所有可执行代码由 Apple 证书签名"、"运行期对每个可执行内存页在校验后才允许加载"；CSM 在源码中是 PPL 或 TXM 承载的页级监视抽象（`CS_MONITOR_TYPE_NONE/PPL/TXM`） | **未公开（审计缺口）**（一手-源码）：哪台设备用哪种监视器由编译期 `board_config` 决定（`CONFIG_SPTM` 与 `XNU_MONITOR` 三分支），设备级白名单不在公开 config 中可得 | **源码可证但 Apple 未承诺**（一手-源码）：`security.codesigning.monitor` 为只读 sysctl，可读当前监视器类型；锁定模式经 `csm_check_lockdown_mode()` 同步给监视器属源码观察，官方无对应表述 | 一手-文档（指南 p.114-115、p.59-60 章节口径）；一手-源码 `code_signing.h`、`kern_codesigning.c`、`pmap_cs.h` | 未公开（设备—监视器映射）+ 待复核（指南内 Apple silicon/Mac 语境段落不可平移） |
| IOKit 用户客户端边界 | **源码可证但 Apple 未承诺**（一手-源码注释原文）：`externalMethod` 必须经 `dispatchExternalMethod()` 做基础参数检查后才进入子类代码；per-selector 的 `checkEntitlement` 与打开级 `kIOUserClientEntitlementsKey` 由驱动自行声明 | **源码可证但 Apple 未承诺**（一手-源码 + 分析推断）：框架提供机制、驱动承担责任；集中式过滤通道 `gIOUCFilterCallbacks`（覆盖 external/async method）存在，但其注册方不在取证范围内 | **未公开（审计缺口）**（分析推断）：iOS 无第三方 kext，用户态可触达的 IOUserClient 集合由系统 kernelcache 决定；MACF 的 `mpo_iokit_check_*` 覆盖 IOServiceOpen 与属性读写（机制可一手核实，规则属闭源策略） | 一手-源码 `IOUserClient.h`/`.cpp`、`mac_policy.h`、`mac_iokit.c`；公开研究（IOSurface/AGX 高暴露结论）；手册/存档页 | 未公开（官方专章未定位，现行开发者文档无输入校验章节命中）+ 官方口径不存在（"某 iOS 版本移除某类驱动"） |
| 地址随机化与 PAC | **公开可证**（一手-文档）：Apple 官方称 ASLR，并对 PAC 给出"系统软件与内置 App 用 PAC 保护函数指针与返回地址、使用五个 128-bit 秘密值签名内核指令与数据、每个用户态进程有自己的 B keys"的原文表述 | **源码可证但 Apple 未承诺**（一手-源码）：slide 由 iBoot 在装载阶段决定、内核反推后写入 `SECURITY_READ_ONLY_LATE` 只读段；PAC 键—用途表据 PDF 文本层提取，列归属未逐格版面复核 | **源码可证但 Apple 未承诺**（一手-源码）：零售可读的只有 `kern.slide` 的是/否布尔值，`kern.kernel_slide` 与 `kern.kernel_text_exec_base*` 仅在 DEBUG/DEVELOPMENT 编译；"KASLR" 一词 Apple 官方文档命中 0 | 一手-文档（ASLR 节、PAC 节与特性表）；一手-源码 `arm_vm_init.c`、`arm_init.c`、`kern_sysctl.c`、`ptrauth.h`、`pac_asm.h`、`ccarm_pac_bti_macros.h` | 未公开（slide 熵值/分布/是否每启动更换；`APGA`/`GCCKEY` 用途）+ 待复核（PAC 表版面、特性表中 PAC 为勾选图形） |
| BTI/GCS 与异常分层 | **源码可证但 Apple 未承诺**（一手-源码）：BTI 由 guarded 页（`ARM_PTE_GP`）+ `bti c`/`bti j`/`hint #34` 垫片实现，内核只读可执行段传 `ARM64_PAGE_GUARDED`；异常按 thread→task→host 三级上抛，host 级兜底是内核内 Mach kobject `ux_handler` | **未公开（审计缺口）**（一手-源码）：`BTI_ENFORCED` 在公开树中**只有 `#if` 使用、无定义处**，零售是否强制无法判定；GCS 全树无使能实现，仅见 EL2 细粒度陷阱位常量；异常端口交付取决于 `developer_mode_state()` 与是否为 corpse | **未公开（审计缺口）**：BTI/GCS 在 Apple 对外文档命中 0，`hw.optional.arm.FEAT_BTI` 是否导出未证实；PAC/BTI 违规在零售日志中的可见性 Apple 未公开（`pac_exception_telemetry` 源码默认 false） | 一手-源码 `arm_vm_init.c`、`proc_reg.h`、`asm.h`、`platform_tests.c`、`exception.c`、`ux_handler.c`、`ux_exception.c`、`unix_signal.c`；一手-文档命中为 0 的负面结果；手册页（随源码的 exception ports 系列） | 未公开（构建取值 + 术语缺席）；AuxKC 的 BTI 例外一格为**不适用（该平台无此机制）**——iOS 零售设备无 AuxKC/第三方 kext 公开启用路径 |

面板 B 里出现了全矩阵最硬的一条负面结论：Apple 对外发布的安全文档逐条列名的硅上特性是 Kernel Integrity Protection、Fast Permission Restrictions、System Coprocessor Integrity Protection、Pointer Authentication Codes、Page Protection Layer、Secure Page Table Monitor（SPTM）、Memory Integrity Enforcement with Enhanced Memory Tagging Extension（MIE/EMTE），其中**没有 BTI、没有 GCS**。这直接决定了第 12 章与本章的写法：凡涉及这两项，只能是"源码具备能力、取值未公开"。

另一处需要点明的结构性差异：代码签名链的"零售可观测性"格拿到了一个非"未公开"的判定（只读 sysctl 可读出监视器类型），而同样依赖硬件的随机化只拿到一个布尔。**可观测性的厚薄与机制强度不成正比**，它更像 Apple 对外沟通策略的副产品。

## 18.5 计数汇总：判定值分布说明什么

按 18.3、18.4 两张表，参与判定的实质格为 **4 列 × 9 行 = 36 格**（"依据层级"列记录来源类型，不参与四档计数，故单列）。分布为：

| 判定值 | 出现次数 | 占 36 格比例 | 集中位置 |
|---|---|---|---|
| 公开可证 | 4 | 约 11% | XNU 分层定性、AMFI 定性与 `research.` 处置、代码签名双层强制、PAC 官方表述 |
| 源码可证但 Apple 未承诺 | 12 | 约 33% | Mach IPC 语义、内核分配开关、MACF 框架、IOKit 责任注释、`security.codesigning.monitor`、`kern.slide` 布尔、异常三级上抛 |
| 未公开（审计缺口） | 20 | 约 56% | 全部九行的"缺口性质"列、面板 A 后三行的生效前提、BTI/GCS 的前提与可见性 |
| 不适用（该平台无此机制） | 0（主判定）/1（子格） | — | 仅在 BTI/GCS 行的"缺口性质"里作为子判定出现一次（AuxKC 例外只在 macOS，iOS 零售无该路径） |

合计 4 + 12 + 20 = 36 格（每格取其**主判定**）。按列汇总更能说明问题：**"强制层级"列 9 格里 4 格"公开可证"、5 格"源码可证"，是唯一有 Apple 对外定性支撑的列**；**"生效前提"列 5 格"源码可证"、4 格"未公开"，没有一格是"公开可证"**；**"零售设备可观测性"列 7 格"未公开"、2 格"源码可证"**（那两格即 `security.codesigning.monitor` 与 `kern.slide`，后者只是布尔）；**"缺口性质"列 9 格全部以"未公开"起头**。也就是说，Apple 愿意对外表述的部分高度集中在"这一层存在、它保护什么"，而几乎不延伸到"它在你的设备上以什么取值运行、你能从哪里验证它"。

三档比例的读法要避免两个极端。把 56% 的"未公开"读成"机制不可信"是错的——这些格子的含义只是本次核查未取到一手依据（见附录 D 的声明）。把 11% 的"公开可证"读成"只有 11% 的机制真实存在"同样是错的——"源码可证但 Apple 未承诺"这一档（33%）表示实现白纸黑字在 APSL 开源树里，只是 Apple 没有把它写成对外承诺。矩阵真正的信息量在**分布的形状**：定性容易、取值困难、验证几乎不可得。

## 18.6 收口表：哪些机制是 iOS 独有、哪些跨 Apple 平台

这张表把散在前文各章的平台口径集中收一次。判定口径以 Apple 原文的**章节标题与限定语**为准，而非以"XNU 树里有没有代码"为准；Apple 未记载的写"未公开"。

| 机制/特性 | iOS / iPadOS | macOS | visionOS / watchOS | tvOS / bridgeOS / audioOS | 依据层级 | 正文所在章 |
|---|---|---|---|---|---|---|
| XNU 同一棵树、不同 config 产出多平台内核 | 是 | 是 | 是（watchOS 有同族 config 文件） | 内核家族共用有官方旁证（CPRNG 节列 iOS/iPadOS/macOS/tvOS/visionOS/watchOS 内核），但**扩展政策未公开** | 一手-源码 + 一手-文档 | 第 3 章 |
| Mach IPC：IPC space policy、pinned send rights、按类型绑定策略 | 是（源码跨平台，无平台分支） | 是 | 是 | 未公开（Apple 未逐平台表述 Mach 语义差异） | 一手-官方doc | 第 4 章 |
| zone/kalloc 硬化分配器 | 是（机制源码可证；生产取值未公开） | 是 | 是 | 未公开 | 一手-源码 | 第 5 章 |
| MACF 框架 | 是（框架开源、策略闭源） | 是 | 是 | 未公开 | 一手-源码 | 第 6 章 |
| MACF 在 iOS 上注册的策略模块清单 | **未公开**（官方从未公布，只有符号/研究观察） | 未公开 | 未公开 | 未公开 | 公开研究/二级 | 第 6 章 |
| AMFI（AppleMobileFileIntegrity kext） | 是（官方点名，含 RESEARCH variant） | 有对应实现但官方段落以 iOS 系与研究设备为主 | 未公开 | 未公开 | 一手-文档 | 第 7 章 |
| 强制 Apple 证书签名 + 运行期逐页校验 | 是（iOS/iPadOS/tvOS/visionOS/watchOS 章原文） | 是（但语境与承载体不同） | 是 | 未公开 | 一手-文档 | 第 8 章 |
| PPL（Page Protection Layer） | **是**：官方列 iOS、iPadOS、visionOS、watchOS | **不适用**：官方明言 macOS 不提供（"only applicable on systems where all executed code needs to be signed"） | 是 | 未公开 | 一手-文档 | 第 8 章 |
| SPTM + TXM（自 A15/M2 起替代 PPL） | 是 | 是（跨 iOS 与 macOS） | 未公开逐平台列举 | 未公开 | 一手-文档 | 第 8、11、15 章交叉引用 |
| library validation | **是**：iOS 官方章明文（可链接系统平台库或同 team identifier 的库） | 是，但主要与 hardened runtime 绑定（另一语境） | 官方章标题含 tvOS/visionOS/watchOS | 未公开 | 一手-文档 | 第 8 章 |
| hardened runtime | **不适用**：Apple 开发者文档自限"your macOS app"；iOS 无该开关 | 是 | 不适用 | 未公开 | 一手-文档（开发者文档） | 第 8 章 |
| 第三方内核扩展（kext）/AuxKC/UAKL/kext receipt | **不适用**：无公开可用路径；iOS 章节只出现 Apple 自家 kext 预链接进 Boot Kernel Collection | 是（APSG 章节标题即 "Securely extending the kernel in macOS"；官方立场为"不再推荐"） | 未公开 | 未公开（APSG 检索 bridgeOS/audioOS 命中 0） | 一手-文档 | 第 15 章 |
| DriverKit 用户态驱动（dext） | 冲突口径，见附录 F：文档正文点明 macOS 与"带 M 系列芯片的 iPadOS"，metadata 另列 iOS 16.0；源码含 iOS 第三方 dext 准入逻辑 | 是（章节标题 "DriverKit security for macOS"） | 未公开（visionOS 等价路径未取到一手页面） | 未公开 | 一手-文档 + 一手-源码 | 第 9、15 章 |
| Kernel Integrity Protection（KIP） | 是（Apple silicon 特性表列名） | 是 | 未公开逐条 | 未公开 | 一手-文档 | 第 10 章 |
| Fast Permission Restrictions / SCIP | 是（Apple 逐条列名的硅上特性） | 是 | 未公开逐条 | 未公开 | 一手-文档 | 第 10、12 章 |
| MIE（EMTE） | A19/M5 及之后（官方明示芯片口径） | 同 | 未公开 | 未公开 | 一手-文档 | 第 12 章 |
| KASLR 术语与 `vm_kernel_slide` | 源码可证；Apple 官方只称 ASLR | 源码可证；x86 走 `boot_args.kslide` 结构字段 | 源码可证 | 未公开 | 一手-源码 | 第 10 章 |
| PAC | 是（官方表述 + 键表 + 源码互证） | 是 | 未公开逐平台 | 未公开 | 一手-文档 + 一手-源码 | 第 11 章 |
| BTI | 源码具备能力、`BTI_ENFORCED` 取值未公开；官方文档命中 0 | 源码具备能力，且 AuxKC 例外只在 macOS 有意义 | 未公开 | 未公开 | 一手-源码 + 负面结果 | 第 12 章 |
| GCS | 未公开（无使能实现，仅 EL2 陷阱位常量） | 未公开 | 未公开 | 未公开 | 一手-源码（旁证级） | 第 12 章 |
| 异常 thread→task→host 三级 + `ux_handler` 翻译 | 是；**iOS 特有收口**：未开开发者模式且非 corpse 时只交付不可移动 read port | 是（源码路径一致） | 未公开 | 未公开 | 一手-源码 + 手册页 | 第 13 章 |
| `task_for_pid` 三重检查 + `kern.tfp.policy` | 是（源码一致，且 iOS 分支另有可调试 dext 判定） | 是 | 未公开 | 未公开 | 一手-源码 | 第 14 章 |
| CS/AMFI boot-args 豁免 | 仅 DEVELOPMENT/DEBUG 解析；RELEASE 下"not respected by any code signing component" | 同源码口径；macOS 另有 iBoot boot-args 白名单与 sip3/LocalPolicy 表述 | 未公开 | 未公开 | 一手-源码 + 一手-文档 | 第 7、14 章 |
| Apple Security Research Device（自定义 kernelcache、调试任意进程） | **是且仅限研究设备**：官方逐条列明研究能力与生产设备 fail-to-boot/panic loop | 不适用（该节描述的是特别融合的 iPhone） | 未公开 | 未公开 | 一手-文档 | 第 7、15、16 章 |
| Analytics Data 与 panic 记录 | 路径官方可证；条目名与字段表 Apple 未列举 | 同（开关名逐字为 Share Mac Analytics） | 未公开 | 未公开 | 一手-文档 + 公开研究/维修界 | 第 16 章 |

## 18.7 本章口径边界

本章只做投影与计数，**没有任何一格引入简报之外的新事实**；所有格子都能指回第 3–17 章的对应小节。需要提醒读者的四条边界：

一，矩阵是**判定值的并排**，不是强度评分。"公开可证"格多不代表该行机制更强，"未公开"格多也不代表该行机制更弱，它只反映 Apple 对外沟通的覆盖范围。二，"依据层级"列在表格里被压缩成路径名，完整出处（含行号与 URL）在附录 B；矩阵读者若要引用具体宏名或原文，应回到正文对应章与附录 B，不要直接抄矩阵。三，本矩阵的时点口径是 XNU `main` 分支某 commit（tree sha f6217f8…）与 Apple Platform Security 落款 2026 年 8 月的那一版 PDF；行号会随版本漂移，判定值的归属不会随之改变，但**引用写法必须带版本**（见第 20.5 节）。四，平台收口表里的"未公开"格是本次核查的产物，不构成对 tvOS/watchOS/bridgeOS/audioOS 政策的任何断言——这一条在附录 C 与附录 D 中各声明了一次，因为它是全篇最容易被误读的格子类型。

# 失效模式与共性前提

## 19.1 归纳方式：机制存在与机制生效之间隔着六类前提

本章的目标不是罗列"iOS 内核的弱点"，而是回答一个更结构性的问题：**第 3–17 章描述的九类机制，从"实现存在于开源树/文档里"到"在我手上这台零售设备上真正强制执行"，中间需要哪些条件同时成立？** 把每章结尾的"失效模式"小节横向对齐后会发现，这些条件不是九套，而是反复出现的六类共性前提。

| 编号 | 共性前提 | 一句话表述 | 主要牵连机制（章号） |
|---|---|---|---|
| P1 | 依赖构建配置取值 | 源码里有开关 ≠ 零售构建打开了该开关 | 内核内存（5）、BTI（12）、异常遥测（13） |
| P2 | 依赖闭源策略实体 | 框架开源、策略在别处；判定点不可枚举 | MACF（6）、AMFI（7）、CSM 设备映射（8）、IOKit 过滤（9） |
| P3 | 依赖内核态豁免位为 0 | 大量放宽路径存在，但在 RELEASE 构建上根本不被读取 | AMFI/CS（7、8）、task_for_pid（14）、异常端口（13） |
| P4 | 依赖硬件融合形态 | 同一份软件在两种融合状态下能力差别极大 | 研究设备与生产设备的隔离（7、15、16） |
| P5 | 依赖调用方承担责任 | 框架提供机制，细粒度校验下放给驱动 | IOKit（9）、MACF 策略（6）、entitlement 消费点（8） |
| P6 | 框架级例外被自认 | Apple 在注释里写明为兼容性让渡的例外 | BTI 与 AuxKC（12）、第三方 kext（15） |

需要预先声明的是：**本章不写任何破坏这些前提的做法**。全章只做一件事——把"如果前提不成立，读者能从哪里看出来、看不出来时该如何表述"讲清楚。这既是本篇的纪律（第 1.3 节的覆盖/不覆盖边界），也是因为绝大多数前提在零售设备上**本来就不具备可自查的观测面**，写"如何破坏"既超出简报证据，也不可能得出可核实结论。

## 19.2 前提一：依赖构建配置取值

三类事实属于这一前提，且都在源码侧：

| 事实 | 源码证据形态 | 为何不足以支撑"已生效" | 判定值 |
|---|---|---|---|
| BTI 强制 | `#if BTI_ENFORCED` 包住 guarded 页设置（`arm_vm_init.c:1075-1082`），`ARM_PTE_GP` 位与 `bti c`/`bti j`/`hint #34` 垫片俱在 | 该宏在公开树里**只有 `#if` 使用、没有任何定义处**，取值来自不公开的构建配置 | 未公开（审计缺口） |
| typed kalloc 与 zone 安全开关 | `ZSECURITY_CONFIG(ZONE_TAGGING/READ_ONLY/SAD_FENG_SHUI/PGZ_OOB_ADJUST)`、`z_kalloc_type`、`kalloc.type.<size>` 命名族 | 机制名与结构真实存在，但 iOS 生产启用程度需以 config 为准，本次未逐条核实 | 未公开（审计缺口） |
| 异常遥测 | `TUNABLE(bool, pac_exception_telemetry, "-pac_exception_telemetry", false)`、`CA_EVENT(pac_exception_event, …)`、`TUNABLE panic_on_exception_triage` | 源码默认 false；即便被打开，是否出现在零售日志中 Apple 未公开 | 未公开（审计缺口） |

这里还有一个**容易滑步的中间层**：内核自身的行为分支也由配置决定。例如 `panic_include_kalloc_types` 初值为 false，在特定诊断分支被置 true，paniclog 是否输出 `kalloc` 类型统计取决于它；`kern.kernel_slide` 与 `kern.kernel_text_exec_base*` 只在 `#if DEBUG || DEVELOPMENT` 下编译。这些分支的存在说明**同一份源码可以编译出可观测性完全不同的两种内核**，而读者无法从外部知道手上这台设备是哪一种。

**谁会破坏该前提**：从机制角度，破坏者不是攻击方，而是**版本演进的正常路径**——某次构建把开关打开、或某次构建把它关掉，都不需要任何攻击行为。这也正是"源码具备能力 ≠ 零售启用"这句话的实际含义：它是一个关于构建管理的陈述，不是关于漏洞的陈述。

**破坏后（即取值为关时）可观测性如何变化**：可观测性**不会变差，只会保持不变**——因为零售设备上原本就读不到这些取值。变化的是表述的正确形态：当取值未知时，正确写法是"源码具备 X 能力，构建取值 Apple 未公开"；把它写成"iOS 启用了 X"就是超出证据的断言。第 12 章与第 20.5 节都以此为硬性检查点。

## 19.3 前提二：依赖闭源策略实体

第二类前提的特征是：**读得到调用点，读不到被调用者**。

| 闭源实体 | 开源侧能看到什么 | 缺口后果 |
|---|---|---|
| MACF 策略模块（沙箱、AppleMobileFileIntegrity 等） | `security/` 下 27 个 `mac_*.c` 全是调用点封装；`mac_policy_register()`、`mpo_*` ops 表、`optional config_macf` 裁剪开关；开源树内**零个策略模块实现** | iOS 运行时"到底注册了哪几个策略、订阅了哪些 hook"官方从未公布；任何模块清单只能标【公开研究/二级】 |
| AppleMobileFileIntegrity kext 本体 | 内核侧接口定义 `amfi_t` 函数表、`amfi_interface_register()`、注册唯一性 panic；`KERN_AMFI_INTERFACE_VERSION 7` | 判定逻辑（哪些 entitlement 在零售 iOS 上被实际接受）无法从公开物复核 |
| CoreEntitlements 用户态库 | `amfi.h` 内的 include 与 `OSEntitlements_query` 等接口字段；`IOUserClient.cpp` 经 `amfi->OSEntitlements.copyEntitlement…` 消费 | "entitlement 从哪来、如何被裁决"这一段的实现不公开 |
| `board_config` 设备清单 | `pmap_cs.h:81-82` 注释原文："XNU_MONITOR and PMAP_CS are both defined for the same targets in board_config.h"；三分支编译期逻辑 | 哪台设备用 PPL、哪台用 TXM 承载 CSM 无法逐机型确认（SPTM 的 A15/M2+ 是官方明示的唯一例外） |
| 驱动侧实现（含 IOKit 过滤回调注册方） | `gIOUCFilterCallbacks` 与 `io_filter_type_external_method` 的调用点存在 | 谁注入过滤策略未核实；不能写成"由沙箱注册" |

**谁会破坏该前提**：任何让"策略实体"缺席或替换的情形，在零售 iOS 上对应的公开实例恰好是 Apple 自己给出的那一条——带 `research.` 前缀 entitlement 的进程，在客户设备上会**在签名校验阶段被终止**，因为尊重该命名空间的是 AMFI 的 RESEARCH variant。这是 Apple 一手文档里少见的"闭源实体的行为差异"直接对外表述的例子，也说明这一类前提的强度取决于**装载的是哪一个策略实体变体**，而变体选择发生在源码之外。

**破坏后可观测性如何变化**：这一类前提被破坏时，可观测性变化是**单向恶化**的——正常设备上只有结果（进程被杀、`kIOReturnNotPrivileged`、系统终止），没有原因（哪条 hook、哪条规则命中）。这直接给第 16 章的事件响应口径设定了边界：panic/crash 记录是终止瞬间的诊断快照，不是审计日志，**不能重建命中路径**，Apple 自己在 `doc/extensible_paniclog.md` 里写明"除非取全量 coredump，否则无法知道 kext 或 dext 的状态"。

## 19.4 前提三：依赖内核态豁免位为 0

大量"放宽"路径在源码里确实存在，但它们与零售设备之间隔着一道编译期闸门：

| 豁免位/放宽机制 | 实名 | 生效条件（源码原文口径） |
|---|---|---|
| AMFI/CS boot-args 掩码 | `amfi`、`amfi_allow_any_signature`、`amfi_get_out_of_my_way`、`amfi_unrestrict_task_for_pid`、`cs_enforcement_disable`、`cs_integrity_skip`、`amfi_relax_profile_trust`、`amfi_dev_mode_policy`；位值如 `CS_AMFI_MASK_ALLOW_ANY_SIGNATURE 0x02`、`CS_AMFI_MASK_GET_OUT_OF_MY_WAY 0x80` | 解析整体被 `#if DEVELOPMENT || DEBUG` 包住，注释原文："this state is not respected by any code signing component on RELEASE builds" |
| 运行时代码签名配置位图 | `CS_CONFIG_UNRESTRICTED_DEBUGGING/ALLOW_ANY_SIGNATURE/ENFORCEMENT_DISABLED/GET_OUT_OF_MY_WAY/INTEGRITY_SKIP/RELAX_PROFILE_TRUST/DEV_MODE_POLICY/MAP_JIT/DEVELOPER_MODE_SUPPORTED` | 由 boot-args 与硬件形态共同填充；生产设备上这些位默认全 0 |
| SPTM/TXM 侧的二次削减 | `if (txm_cs_config->systemPolicy->featureSet.localSigning == false) config &= ~CS_CONFIG_LOCAL_SIGNING;`、`if (txm_ro_data && !txm_ro_data->exemptions.allowUnrestrictedDebugging) config &= ~CS_CONFIG_UNRESTRICTED_DEBUGGING;` | 即便前一层被越过，TXM 策略仍可把配置位清掉——是否放行由 TXM 决定 |
| boot-args 通道本身 | iBoot 对 `boot-args` NVRAM 变量有内建白名单过滤；`sip3` 为真时才不过滤（macOS 章节表述） | iOS 零售设备无对应公开路径；arm64 亦不存在 `PE_parse_boot_argn("slide"/"kaslr"/"kaslider")` |
| 开发者模式 | `developer_mode_state()` 决定异常端口交付 control port 还是不可移动 read port | 这是零售设备上**用户可见**的一个开关，也是少数"前提由用户设置决定"的例子 |

**谁会破坏该前提**：这一类前提最容易被误认为"攻击目标"，但它对应的现实破坏者主要是**设备形态与构建变体**——DEVELOPMENT/DEBUG 内核、研究设备、以及 Apple 明确描述的策略变体差异。零售 RELEASE 构建上，"豁免位为 0"不是靠运行时检查维持的，而是靠**相关代码不在构建里**维持的，这是本篇认为值得强调的一点：它意味着"豁免位是否被翻转"在零售设备上通常不是一个可观测状态，因为它根本没有被解析的代码。

**破坏后可观测性如何变化**：变化最剧烈的一格是异常处理链。开发者模式关闭时，异常接收方只拿到不可移动的 read port，因此**外部 handler 无法读取或修改寄存器状态、无法续行目标线程**；开启时（或目标是 corpse）才交付 control port。这条差异是"设备状态可在能力语义层直接读出"的少见例子——它不是日志，而是内核返回的能力种类，因此第 20.4 节把它列为合法的观测对象。

## 19.5 前提四与前提五：硬件融合形态与调用方承担责任

P4（硬件融合形态）在 Apple 一手文档里有完整表述：Security Research Device 是"特别融合的 iPhone"，让研究者"不必击败或停用平台安全特性即可开展研究"，可以侧载入具备 **platform-equivalent permissions** 的内容、使用 `research.com.apple.license-to-operate` 调试系统上任意进程、**个性化并恢复自定义 kernel cache**。而隔离机制同样写明：策略改动实现在 iBoot 的一个变体与 Boot Kernel Collection 中，在生产设备上**无法启动**；研究用 iBoot 检查新的融合状态，若运行在非研究融合硬件上则**进入 panic 循环**。

这一前提的读法有两层。第一层是**能力差异**：本篇反复出现的"iOS 无公开内核调试通道""iOS 内核只能装 Apple 自家 kext"这两条结论，其官方反证（自定义 kernelcache、调试任意进程）全部限定在研究设备语境，因此这两条结论必须与研究设备声明同现。第二层是**信号价值**：panic 在 Apple 口径里被用作策略违例的终止手段，这为第 16 章"把 panic 当状态信号而非取证源"的用法边界提供了官方依据。P4 是六条前提里唯一一条**读者可以自查**的——设备是否为研究融合形态，是用户购买与授权流程的既有事实，不需要任何技术手段确认。

P5（依赖调用方承担责任）集中在第 9 章，但牵连第 6、8 章：

| 责任下放点 | 框架侧提供 | 调用方须承担 |
|---|---|---|
| IOUserClient 方法派发 | `dispatchExternalMethod()` 做 selector 与基础参数检查（`IOUserClient.h` 注释原文："externalMethod override is required and must call dispatchExternalMethod() to do basic argument checking before calling subclass code"） | 细粒度校验、结构体 OOL 与 scalar 数量的实际使用约束 |
| 按方法授权 | `checkEntitlement` 非空则调用进程必须持有该 entitlement，否则返回 `kIOReturnNotPrivileged`；打开级 `kIOUserClientEntitlementsKey` | 驱动自行决定哪些 selector 需要哪个 entitlement，即授权粒度设计 |
| 访问控制裁决 | MACF 在 VFS/exec/IOKit 路径提供 `mpo_iokit_check_open*`、`mpo_iokit_check_set_properties`、`mpo_iokit_check_get_property` 等 hook | 具体规则由闭源策略实现；框架只保证"经过这里" |
| entitlement 判定效果 | 内核从签名内嵌 blob 解析（`ubc_subr.c` 的 csb_* 字段）、AMFI 接口判定、MACF 消费点放行 | 判定标准在闭源实体内，调用方无法自证 |

责任下放的直接后果是**风险集中**：攻击面评估的单位从"内核"细化到"某个驱动的某个 selector"，而公开研究给出的正是这个粒度的例子（IOSurface 的 use-after-free 档案、AppleAVE 引用计数问题、GPU 相关在野链条件）。**这些结论一律是【公开研究/媒体转述】，不得表述为 Apple 官方承认的攻击面**；Apple 公告页在本次抓取的 iOS/iPadOS 页面里也从不使用 `IOKit` 作为单一组件名，而是以 `IOSurface`、`IOMobileFrameBuffer`、`IOSurfaceAccelerator`、`AppleAVD` 等具体驱动名出现。P5 被破坏时（某驱动未做细粒度校验），可观测性的变化表现为**内核层毫无异常**：机制层一切正常，缺陷存在于责任层，只能回到公告的组件名粒度去观察。

## 19.6 前提六：框架级例外自认，以及六类前提的合并视图与本章口径边界

P6 与前五类的区别在于：它不是"未知"，而是 **Apple 自己写明的让步**。原文（`arm_vm_init.c:1243-1250`）逐字为："We cannot mark auxKC text as guarded because doing so would enforce BTI on oblivious third-party kexts and break ABI compatibility. Doing this defeats the purpose of BTI (branches to these pages are unchecked!) but given both the relative rarity and the diversity of third-party kexts, we expect that this is likely impractical to exploit in practice."

三点必须分开表述，否则就会滑步：

| 要素 | 原文可支持什么 | 原文不可支持什么 |
|---|---|---|
| 例外本身 | AuxKC 文本不能标 guarded，因此跳到这些页的分支不受检查——Apple 亲笔写明"defeats the purpose of BTI" | 不能据此推定其他页也未被检查 |
| 理由 | 兼容性：会对未意识的第三方 kext 强加 BTI 并破坏 ABI | 不能推定 iOS 零售设备存在该让步——该段同时是"第三方 kext 只在 macOS"的旁证 |
| 风险判断 | "we expect that this is likely impractical to exploit in practice"——Apple 的**工程预期** | 不能写成"经验证不可利用"；工程预期不等于验证结论，这是本篇的固定口径 |

这一条对**全篇其他章同样适用**，因为 Apple 对外文档里频繁出现的正是"设计目标式"表述（如 PPL "is designed to prevent…"）。设计目标陈述机制意图，验证结论需要测试数据；当只有前者时，判定值应停留在"公开可证（定性）"而不可升级为"公开可证（强度）"。

合并视图（**"是否可自查"指普通读者与企业环境通过公开配置与官方文档即可判断，不含任何绕过、提取或攻击准备**）：

| 前提 | 破坏条件（是什么让它不成立） | 破坏后零售可观测性 | 是否可自查 | 正文依据节 |
|---|---|---|---|---|
| P1 构建配置取值 | 构建未开启该开关，或开关取值随版本变化 | 无可观测面（原本就读不到取值） | **否**——只能降级表述 | 12.2、5.3、13.6 |
| P2 闭源策略实体 | 装载的策略变体不同、或策略规则本身变更 | 只见结果（进程被终止/返回码），不见规则命中 | **部分**——可见结果与公告组件名 | 6.6、7.6、8.3、9.4 |
| P3 内核态豁免位为 0 | 设备运行 DEVELOPMENT/DEBUG 变体，或 TXM 策略变更 | 能力语义会变（control port vs read port 可读） | **部分**——开发者模式状态可见 | 7.4、13.5、14.4 |
| P4 硬件融合形态 | 设备为研究融合形态（或反之） | 直接体现在可否个性化自定义 kernelcache 等能力上 | **是**——属采购与授权事实 | 7.5、15.4、16.4 |
| P5 调用方承担责任 | 某驱动的某 selector 未做细粒度校验 | 内核层无异常，只能回到组件粒度 | **否**——依赖公告与研究披露 | 9.3、9.6 |
| P6 框架级例外自认 | 兼容性让步存在 | 让步写在源码注释里，零售设备无从观测 | **否**——只能引原文并限定平台 | 12.5、15.2 |

**本章口径边界**：本章六条前提都是从两份核查简报已经核实的条目里归纳的，未增加任何新的机制断言；凡涉及"破坏条件"一栏，写法一律是**状态描述**（变体、取值、责任归属），不含任何操作路径。另外需要区分两种"可观测性变化"：P2、P5 属于"结果可见、原因不可见"，P1、P6 属于"结果与原因都不可见"。第二种更值得警惕，因为此时读者能做的唯一正确事情，是把判定值停在"未公开（审计缺口）"。

# 分层加固与研究路径

## 20.1 分层原则：只做配置、观测与流程

前两章把机制的"层级—强制点—前提—可见性"读完了，本章把它转成动作。转换只允许走三条通道：

| 通道 | 允许的内容 | 禁止的内容 |
|---|---|---|
| 配置 | 系统设置里存在的官方开关、描述文件与 MDM 可控项、开发者模式与分析数据共享的选择 | 任何改变内核构建取值、修改 boot-args、调整 LocalPolicy 的尝试（iOS 零售设备无公开路径） |
| 观测 | 设备/主机上官方或源码明示可读的量（`kern.slide` 布尔、`security.codesigning.monitor`、Analytics Data 条目、崩溃报告的既有字段） | 把未文档化字段（`translation` 等）当既定语义使用；把 panic 记录当审计日志使用 |
| 流程 | 更新节奏、证据保存顺序、引用与标注纪律、上报路径选择 | 任何绕过、提取、注入或攻击准备；任何"复现利用"的前置步骤 |

分层的依据是**读者能合法触达的接口面不同**：个人用户只有设置项，管理员只有描述文件与合规策略，研究者只有 Apple 明确授权的装置与主机侧可观测量。同一句建议放在错误的层会失效——例如把"核查开发者模式状态"给个人用户是有效的，把"读取主机侧 task_for_pid 计数"给个人用户则不可达。

## 20.2 个人用户层

| 建议 | 具体做法（均为官方存在的配置项） | 与本篇机制的耦合点 | 证据层级 |
|---|---|---|---|
| 保持在受支持版本 | 定期确认机型仍在 Apple 安全更新覆盖内；对同一 CVE 关注"首个修复版本"而非传言 | 第 17 章口径：修复版本与组件名以 Apple 公告原文为准 | 一手-文档（公告页） |
| 核查开发者模式状态 | 若从未在设备上做过开发调试，确认该状态为关 | 第 13.5 节：`developer_mode_state()` 直接决定异常接收方拿到 control port 还是不可移动 read port | 一手-源码（`exception.c:351-364` 原文） |
| 分析数据共享的选择 | 明确两项设置的区别：设备分析（逐字名 "Share iPhone & Watch Analytics" / "Share iPad Analytics"）与面向开发者的 "Share With App Developers"；路径为 Settings > Privacy & Security > Analytics & Improvements | 第 16 章：本地 Analytics Data 条目保留时长与轮转策略 Apple 未公开，因此是否共享应作为"证据可得性"决策，而非隐私装饰 | 一手-文档（法务页、支持文档、APSG 逐字开关名） |
| 及时导出诊断条目 | 若已开启共享，发生非正常重启后尽快在 Analytics Data 中查看并留存 | 第 19.3 节：panic 是终止瞬间快照，不含完整内存取证（Apple 原文） | 一手-文档（查看路径）+ 一手-官方doc（paniclog 机制） |
| 模式类设置的耦合关系 | 锁定模式与失窃设备保护在本篇只作交叉引用：其模式设计与用户处置建议属第 77 期，本篇不重复 | 与内核侧的耦合点只有一个源码事实：`csm_check_lockdown_mode()` 把锁定状态同步给代码签名监视器（含 MAP_JIT 相关），官方文档无对应表述 | 一手-源码（锁定模式耦合为"源码观察"） |

个人用户层**不该做的事**同样重要：不要因为某条第三方文章声称"关闭某项即可提升安全性"而修改系统项——本篇矩阵里"零售设备可观测性"一列 9 格中有 7 格为"未公开"，意味着绝大多数所谓调优建议无法在本篇证据链上落地。

## 20.3 企业与 MDM 管理员层

管理员最常犯的类别错误，是把 macOS 的管控语汇直接搬到 iOS。本篇第 15 章已经给出可核实的分界，这里转成策略文档层面的动作：

| 动作 | 依据（本篇已建立的机制事实） |
|---|---|
| 策略文档中所有 kext / AuxKC / UAKL / kext receipt / 系统扩展相关表述，一律限定 macOS | APSG 的第三方 kext 全部表述位于标题 "Securely extending the kernel in macOS" 之下；iOS 章节只出现 "the kernel, kernel extensions (kext)" 与 "Apple kernel extensions" | 
| 不要引用 "Secure Boot 策略里勾选 enable kernel extensions" 作为 iOS 合规项 | 该流程（1TR → Reduced Security → UAKL → LocalPolicy SHA-384 → kmd 校验）是 macOS/Apple silicon 语境 |
| 引用 Managed Device Attestation 字段时保留语义限定 | 字段 `1.2.840.113635.100.8.13.3 Third-party kernel extensions allowed` 的最小支持版本跨平台列出（macOS 14/tvOS 16/visionOS 1.1/watchOS 9/iOS 16/iPadOS 16），但 Apple 对它的语义解释是"在带 Apple silicon 的 Mac 上是否允许第三方 kext" |
| 把"iOS 无第三方内核扩展"写进基线，同时写出限定项 | 官方反证是研究设备：自定义 kernelcache 与调试任意进程都是研究设备专属能力，生产设备上研究用 iBoot 会进入 panic 循环 |
| 描述文件与 entitlement 相关合规项，避免使用未核实的 entitlement 名 | `task_for_pid-allow`、`com.apple.system-task-ports` 等字符串在 XNU 测试 plist 里逐字存在，但"零售 iOS 上哪些被闭源 AMFI 实际接受"Apple 未公布列表；`com.apple.security.cs.debugger` 的 Apple 页面 metadata 只声明 macOS（introducedAt 10.7） |
| DriverKit/dext 相关策略按"冲突口径"书写 | Apple 文档正文只点明 macOS 与"带 M 系列芯片的 iPadOS"，页面 metadata 另列 iOS 16.0；iPhone 侧仅有源码里的第三方 dext 准入判定逻辑，面向开发者的可用性表述未点名 iPhone（详见附录 F 第 1 条） |
| 不要基于 panic 记录做合规判定 | 内核 panic 条目名与字段表 Apple 未公开列举；是否经 "Share With App Developers" 分发给第三方开发者 Apple 未公开；因此"以设备是否产生 panic 作为安全事件指标"在 iOS 侧不成立，至多是稳定性信号（维修界经验口径，Apple 未正式记载） |

## 20.4 安全研究与事件响应层

这一层的建议全部落在合法装置、授权模型与既有可观测量上。

**（一）授权模型。** Apple 对研究者提供的合法路径是 Security Research Device：一台**特别融合的 iPhone**，其设计目标是让研究者"无需击败或停用平台安全特性"即可开展研究，可侧载入具备系统组件同级权限的内容、开机启动服务、跨重启持久化、使用 `research.com.apple.license-to-operate` 调试系统上任意进程、个性化并恢复自定义 kernel cache。响应流程里应把它当作**受 Apple 策略约束的授权环境**，而不是"更宽松的普通设备"：`research.` 命名空间只被 AMFI 的 RESEARCH variant 尊重，任何带该 entitlement 的进程在客户设备上都会在签名校验时被终止；个性化环节只会把可执行文件与 kernel cache 的度量值（如哈希）连同研究设备标识送交 Apple。

**（二）把 panic 与分析数据当状态信号而非取证源。** 可支持的判断与不可支持的判断，按第 16 章的口径固定成一张对照表：

| 可以支持 | 不能支持 |
|---|---|
| 机型与 iOS/kernel 版本归属 | 还原攻击链（快照不含跨时间的行为序列） |
| 是否发生非正常重启（时间戳与用户可感知的重启对齐） | 完整内存取证（Apple 原文："no way of knowing the kext or dext state unless we take a full coredump"） |
| 复发性与频率（同一条 `panicString` 反复出现偏向稳定性/硬件问题——维修界经验口径，Apple 未正式记载） | 区分"保护被触发"与"保护被绕过"（PAC/BTI 违规无 Apple 公开的字段级口径） |
| 是否存在 zone/kalloc 异常膨胀（源码显示该类文本可由 `paniclog_append_noflush()` 写入） | 判定"被在野利用"（只能回到公告原文的 "Apple is aware of a report…" 措辞） |
| 是否有 dext/kext 主动落盘的状态块（`extensible_paniclog`，仅 active 时收集） | 读取 PAC/BTI 是否开启（源码里有异常与 tracepoint 定义，零售日志可见性未公开） |

流程上唯一的硬性动作是**重启后尽快导出**，因为 Analytics Data 条目的保留时长与轮转策略 Apple 未公开；把 `translation` 之类字段写入任何结论时，必须携带"Apple 未公开定义其语义，把它等同于内核 slide 属研究界推断"这一限定语。

**（三）零售设备上少数可读的量。** 下表是本篇核查得到的、可在不改动机型或构建的前提下读取的状态量，以及它们的语义上限：

| 可观测量 | 能读出什么 | 读不出什么 | 依据 |
|---|---|---|---|
| `kern.slide` | 一个布尔：内核地址随机化是否生效（slide 是否非 0） | 具体 slide 值（`kern.kernel_slide`、`kern.kernel_text_exec_base*` 仅 DEBUG/DEVELOPMENT 编译） | 一手-源码 `kern_sysctl.c:4313、4329-4351` |
| `security.codesigning.monitor` | 当前代码签名监视器类型（只读 sysctl，`cs_monitor` 变量） | 该设备上 board_config 为何选中它；逐机型映射 | 一手-源码 `kern_codesigning.c:111-115`、`code_signing.h:35-37` |
| 主机侧 `task_for_pid` 计数 | `extmod_statistics_incr_task_for_pid()` 累加 `task_for_pid_count` / `task_for_pid_caller_count`，可作为"有进程在尝试取得他人 task port"的聚合信号（读取需特权） | 具体调用方身份与是否成功（须结合 `mac_proc_check_get_task()` 的上行调用与策略） | 一手-源码 `extmod_statistics.c:73-84`、`vm_statistics.h:203-204` |
| 异常端口交付的能力种类 | 收到的是 control port 还是不可移动 read port，可直接反映开发者模式状态 | 零售设备无公开接口枚举全部订阅者 | 一手-源码 `exception.c:351-364`、`ux_handler.c:143-144` |

**（四）研究路径的正确切分。** 由第 19 章的前提表可直接得到一条分工结论：P1（构建取值）与 P6（框架级例外）**不是研究课题，而是引用纪律问题**——它们的答案不公开，读者能做的只是不越界表述；P2（策略实体）、P5（驱动责任）才是可以靠 kernelcache 符号观察、公开研究与 Apple 公告组件名粒度推进的方向，且产出必须标【公开研究】层级。

## 20.5 证据卫生：所有读者共同的引用纪律

这一节适用于报告的所有读者，因为它约束的是"引用内核机制时的表述"，而不是设备配置。

**规则一：区分"源码可证"与"Apple 承诺"，并在同一句内完成。** 可用的三种安全句式为——"XNU 源码显示…，Apple 对外文档未表述该点"；"Apple 官方文档称…，实现细节未公开"；"公开源码具备…能力，构建取值 Apple 未公开"。禁止的写法是把源码符号加引号当官方术语（如把 `KASLR`、`vm_kernel_slide` 当作 Apple 面向用户的说法），或把源码注释里的工程预期（"we expect that this is likely impractical to exploit in practice"）写成验证结论。

**规则二：行号随版本漂移时的引用写法。** 本次核查锚定在 XNU `main` 分支某 commit（tree sha f6217f8…）与落款 2026 年 8 月的 Apple Platform Security PDF。因此行号引用的正确形态是"以该镜像/上游 main 为准，历史版本行号不同"，并且优先给出**可 grep 的稳定标识**（宏名、函数名、结构体字段名、原文片段），把行号降为辅助定位。指南类文献的引用同理：给章节标题与原文片段，页码只作二次定位——例如 PPL 的 macOS 排除表述要写成"Apple Platform Security 的 Page Protection Layer 一节原文"，而不是只写"p.59"。

**规则三：负面结果的表述方向。** 检索命中 0 次只能写成"本次核查未取得一手依据"，不可反写为"不存在"。本篇有若干条判定值来自负面结果（BTI/GCS 在对外文档中命中 0、`GCCKEY` 在树内命中 0、mach 手册页 URL 实测 404、`bridgeOS`/`audioOS` 命中 0），它们统一收在附录 D 并带同一条声明。

**规则四：组件名逐字照录。** Apple 公告的组件名是口径载体，不可"顺手规范化"：本次抓取中确认的写法包括 **`GPU Drivers`（复数）** 与把 CVE-2025-31201 的组件列为 **`RPAC`**（Impact 原文明写 "bypass Pointer Authentication"，Description 为"removing the vulnerable code"）；而在 iOS/iPadOS 页面中**未出现** `IOKit`（单一名）、`Kernel Cache File`、`Mach IPC`、`GPU Driver`（单数）。日期也保留原文形态（如 "Released February 10, 2025" 与页面注 "Entry added June 11, 2025"）。

**规则五：混合来源必须拆开。** 同一条陈述若同时有对外文档与源码依据（如"逐页签名校验"），要分开表述各自支持到哪一层：官方文档说"每个可执行内存页在校验后才加载"，源码说该职责由 PPL 或 TXM 承载的 CSM 执行、并由 `security.codesigning.monitor` 可观测；两句不可合并成"Apple 承诺由 PPL 逐页校验"这种跨层断言。

## 20.6 本章口径边界

本章所有建议均可回溯到第 3–17 章已核实的机制事实与两份核查简报，**未提出任何绕过、提取、注入或攻击准备类建议**，也未提出任何"调整内核构建/boot-args/策略字段"的操作。此外有三条边界需要保留：其一，个人用户层与建议的"开发者模式状态核查""分析数据共享"仅涉及 Apple 已有的公开设置项，其安全收益不应被夸大为对某一具体机制的加固——第 18 章的可观测性分布说明，多数机制的状态无法由用户验证。其二，企业层建议的价值在于**避免策略文档做出跨平台错误断言**，而非提升设备防护强度。其三，研究层列出的四个可观测量语义都很薄（一个布尔、一个类型值、两个计数器、一种端口能力），它们能支撑"状态对齐"与"聚合信号"，不能支撑"结论性判定"；任何把它们当判据的流程图都应在评审时被退回。

# 结论

## 21.1 本篇方法得到了什么

本篇把 iOS 内核安全读成了九条**机制链**，而不是一堆特性名。方法上的收获可以概括为"四元组"：每条机制都固定回答**负责什么、强制点落在哪一层、生效需要哪些前提、明确不覆盖什么**。坚持这个结构带来了四项可复用的产出。

第一，把职责边界从宣传语还原成目录事实。"XNU 是 combining CMU Mach、FreeBSD 组件与 C++ IOKit 的 hybrid kernel"是 Apple 在开源 README 里的自述；`osfmk`/`bsd`/`iokit`+`libkern`/`security`/`pexpert` 的目录描述就是职责口径。代码签名强制并非某层独占，而是 bsd exec → security/MACF → AMFI 接口 → pmap/CSM 的四段接力。

第二，把"能力模型"这类抽象说法落到可 grep 的实体上。port right 之所以能被称为 capability，是因为 `ipc_object.h` 的 copyin/copyout/destroy 注释与 `doc/ipc_security_concepts.md` 的 `is_policy`、pinned send rights 一手存在；异常分层之所以是分层，是因为 host 级兜底是内核内一个 Mach kobject（`ux_handler`），信号是它收到 Mach 异常之后翻译的产物。

第三，把硬件与构建的关系写清了。PPL 与 SPTM/TXM 是承载代码签名监视器的两种形态（`CS_MONITOR_TYPE_PPL/TXM`），选择发生在编译期由 `board_config` 决定；SPTM 与 MIE 的芯片口径是 Apple 明示的少数例子（A15/M2+、A19/M5+），而 PAC/BTI/GCS 的芯片—版本对应关系 Apple 未逐条公开。

第四，也是最实用的一项：把"知道"与"能验证"分开。全矩阵 36 个实质格里，"零售设备可观测性"列只有 2 格落在"源码可证"，且语义极薄——一个只读 sysctl 给出的 CSM 类型值（`security.codesigning.monitor`）、一个"随机化是否为真"的布尔（`kern.slide`）；其余 7 格全为"未公开"，其中 zone 名曾可经 `zone_info`/`zprint` 枚举、近年收紧这条恰恰是"曾经可观测、现在不可观测"的反向例子。这条分布本身就是本篇的结论之一。

## 21.2 Apple 公开口径的系统性边界在哪里

把第 3–17 章所有"公开可证"条目并排看，Apple 对外表述的边界呈现出稳定的形状：**面向用户的文档只讲能力与设计目标，不讲实现；源码讲实现，不讲构建取值。**

| 层级 | 典型表述形态 | 能支持到哪一步 |
|---|---|---|
| Apple 对外文档（Platform Security 2026-08、开发者文档、支持/法务页） | "is designed to prevent…"、"requires that all executable code be signed…"、逐条列名硅上特性（KIP/FPR/SCIP/PAC/PPL/SPTM/MIE） | 机制的存在、意图、平台归属；不能支持实现细节与强度 |
| XNU 开源树 + 仓库内 doc/ + 随源码发布的 man page | 宏名、函数名、注释原文、编译分支、异常与 sysctl 定义 | 实现存在与调用路径；不能支持零售构建是否启用（`BTI_ENFORCED` 无定义处即最干净的例子） |
| 公开研究/维修界 | IOSurface/GPU 驱动的攻击面档案、panic 条目字段样本、`translation` 解读 | 粒度更细的观察与经验；一律不得写成 Apple 官方承认 |

这个三段结构中间那段——"源码讲实现但取值不公开"——就是审计缺口所在，也是本篇反复强调**它是缺口而不是矛盾**的原因。举例：Apple 对外文档从未使用 "KASLR" 一词（只称 ASLR，且语境是进程级），而源码里 `vm_kernel_slide` 与 "Boolean indicating if KASLR is active" 俱在；两者并不冲突，只是各自讲话的对象不同。同理，MACF 框架开源而策略闭源、AMFI 定性公开而实现不公开、异常分层源码清楚而无面向用户的文档，都属同一形状。

还有一类边界值得单独记录：**Apple 会用文档"自认限制"**。QA1574 的 `#warning "MAC policy is not KPI"`、PPL 章节明写"macOS 不提供"、AuxKC 注释明写"defeats the purpose of BTI"、`extensible_paniclog.md` 明写"不取全量 coredump 就无从知道 kext/dext 状态"——这些是公开口径里最接近"承诺与不承诺的分界线"的材料，本篇把它们当作判定值的首选依据。

## 21.3 本篇的正确使用方式与不该如何使用

**应当如何使用。** 本篇的正确用法是把第 18 章的矩阵当**引用底座**：当一份外部材料声称"iOS 有 X 机制"时，先在该矩阵里找到 X 所在的行，读出它的判定值与依据层级，再决定你的行文能把它写到哪一层。当需要在文档里描述某个内核行为时，回到对应章找原文片段与可 grep 的标识符（附录 B 给全量出处），并按第 20.5 节的句式落笔。"层 × 前提"这两个维度是可迁移的：即使版本更新导致某些机制条目变化，这两个维度构成的提问方式仍然有效。

**不应当如何使用。** 一，不可当作"iOS 是否安全"的是/否答案。本篇没有任何一格给出强度评分，56% 的"未公开"也不是"不安全"的同义词——它只表示本次核查未取到一手依据。二，不可把"源码可证"当成"Apple 承诺"来要求厂商支持，也不可反过来把"Apple 未公开"当成"该机制不存在"。三，不可把第 19 章的前提表读成操作指南：那一章每一格都是状态描述，破坏条件的表述刻意不含任何做法，且六条前提里只有 P4（硬件融合形态/设备是否为研究装置）是读者可自查的。四，不可把第 20 章的四个可观测量当作检测规则上线：它们的语义过薄，用作"状态对齐"可以，用作"判定"会得出错误结论。五，不可把公开研究结论（IOSurface/AGX 高暴露、`panic-full` 字段清单、维修界经验）写成 Apple 官方立场——附录 B 对每一行都标了层级，越级引用即失效。

## 21.4 未解问题清单

以下条目挑自两份核查简报的缺口清单，按"对全篇结论影响最大"排序。每条都是**本次未取得一手依据**，不是"事实不存在"（完整去重合并清单见附录 D）。

| 序号 | 未解问题 | 为何关键 |
|---|---|---|
| U1 | `BTI_ENFORCED` 在零售 iOS 构建中的取值（公开树内只有 `#if` 使用、无定义处） | 决定第 12 章整章的判定值；也是"源码具备能力 ≠ 零售启用"的样板问题 |
| U2 | 现代零售 iPhone 是否存在任何公开的内核调试通道（`osfmk/kdp/`、`dbgwrap.c`、`alternate_debugger.c` 实现存在，可用性 Apple 未记载） | 牵动第 14、15 章两条结论的边界与研究设备限定项的必要性 |
| U3 | `amfid` 在当前 iOS 上的实际职责与 plist 载体的官方描述 | 决定第 7 章"内核组件 + 用户态守护进程并存"这句话能写到多细 |
| U4 | iOS 上 MACF 实际注册的策略模块清单（含 Sandbox/AMFI 的注册名） | 第 6 章的"谁在被强制"至今只能靠符号观察与研究文献，无法升级为官方口径 |
| U5 | `GCCKEY`/`APGA`（GA generic key）是否被 Apple 用于某类指针签名（`GCCKEY` 全树 0 命中，`APGA` 仅见于 EL2 陷阱位） | 第 11 章的覆盖面红线；误写会把 ARM 架构概念冒充 Apple 实现 |
| U6 | GCS 是否在 iOS 内核或用户态启用、是否覆盖内核态；GCS 与 PAC 是互补还是替代 | 第 12 章的后半至今只能写"无公开证据" |
| U7 | DriverKit 在 iPhone 上的官方可用性口径（文档 prose 只点 macOS 与 M 系芯片 iPadOS，metadata 列 iOS 16.0，源码含 iOS 第三方 dext 判定） | 附录 F 第 1 条冲突未收口，直接影响第 15 章与企业策略文档写法 |
| U8 | PAC、BTI、GCS 各机制的"芯片—iOS 主版本"对应关系（对比：SPTM 与 MIE 的芯片口径是明示的） | 没有该表，任何"从 iOS x 起生效"的说法都缺乏依据 |
| U9 | 内核 slide 的熵值、分布与"是否每次启动更换"；零售设备上除 `kern.slide` 外是否还有其他可见暴露面 | 第 10 章只能停在定性；随机强度是常被夸大的话题 |
| U10 | `Analytics Data` 条目名的官方列举（含 `panic-full` 的拼写口径）与 panic 字段表；`translation` 等字段的官方语义；条目保留时长/轮转策略 | 第 16 章的可观测性几乎全部依赖"据公开样本"层级，事件响应流程因此受限 |
| U11 | PAC/BTI 违规是否会显式出现在 panic 字符串中（Apple 正式记载缺失，只有源码级异常与 tracepoint 存在） | 决定"保护被触发"能否在设备侧被观测到 |
| U12 | Apple Platform Security 的 PAC 键—用途表 PDF 版面逐格归属（含 "Selector Typed Objective-C ivars" 行 Salt 列提取为乱码）与特性表中 PAC 勾选图形 | 表格是第 11 章的核心依据，版面归属未复核前须保留"部分核实"标注 |

## 21.5 收束

把本篇的方法压缩成一句话：**读 iOS 内核安全机制时，先确定它在哪一层被强制，再确定它生效依赖哪一类前提，最后确定你能从哪一处验证它——三者之中，第三项最经常是"无"。** 这份报告的价值不在给出答案的完整性，而在于把"哪些格子是空的、为什么是空的、空的时候该怎么写字"固定下来。九类机制、六类前提、四个判定值、一份去重后的缺口清单与一份误解校正表，构成的是同一件事：一个可以被逐格检查、也可以被逐格推翻的引用底座。后续版本更新时，最需要重跑的不是机制描述，而是第 18 章矩阵里那 20 个"未公开（审计缺口）"格——其中任何一格被 Apple 官方口径填上，本篇相应章的判定值就应当被改写。

# 附录

## 附录A 术语表

本表的核心价值在最后一列：**该术语出自哪一层**。请特别留意标为「XNU 源码」与「本报告自拟」的条目——前者是 Apple 写在开源树里的内部用语，读者容易误当成对外承诺；后者是为了本篇矩阵与前提归纳而临时建立的分类词，Apple 与学界都没有这个说法。

| 术语 | 中文释义 | 出自哪一层 |
|---|---|---|
| port right | 端口的"处置权利"：内核认可的能力（capability），持有它才可能对端口做发送/接收/销毁 | XNU 源码（`ipc_object.h` 注释直称 capability） |
| send right / naked send right | 发送权：可向该端口投递消息的权利，可复制、可引用计数；内核内部另有 "naked send right" 措辞，指未包裹在名称/端口结构中的裸发送权 | XNU 源码（`ipc_port.h` 注释）+ Apple 随源码发布的 man page |
| receive right | 接收权：从该端口取消息的权利，同一时刻唯一所有者 | XNU 源码 + man page |
| send-once right / immovable receive right | 一次性发送权：只能成功投递一次，用于 reply port 保证"恰好一个回复"；不可移动接收权：禁止被转移，用于服务端口与异常端口的绑定语义 | XNU 源码 + Apple 官方 doc（`port_types.md`） |
| dead name | 死名：receive right 已被销毁后剩余的引用形态；源码注释直接写作 "The object is dead"，并有 `ipc_object_alloc_dead()` 分配 dead-name entry | XNU 源码（`ipc_object.h`） |
| read port / control port | 只读端口（不可续行、不可改状态）与控制端口（可驱动线程续行）的能力差；iOS 上无开发者模式时只交付 read port | XNU 源码（`exception.c` 注释原文） |
| confused deputy / IPC space policy | 混淆代理人：把不同调用方的权限混为一谈导致的越权；端口空间策略：把进程身份/entitlement 投影成不可变枚举 `is_policy`，可无锁查询并做 dPAC 加固 | Apple 官方 doc（`ipc_security_concepts.md`） |
| pinned send right | 常驻发送权：task/thread control port 的 send right 被强制常驻，违规按进程 hardened 与否分别硬执行或返回 `KERN_INVALID_CAPABILITY` | Apple 官方 doc + XNU 源码 |
| guard exception / `ipc_control_port_options` | 端口类型或处置语义违规时经 `mach_port_exc_guard` 上抛的守卫异常；其反应形态由 boot-arg `ipc_control_port_options` 决定 | Apple 官方 doc + XNU 源码 |
| kalloc / zone | 内核小对象分配入口与按大小类建立的分配区；现代 XNU 中 kalloc 构建在 zone 分配器之上，zone 名族为 `kalloc.<size>`/`kalloc.type.<size>`/`early.kalloc` | XNU 源码 |
| zone_map / guard page | zone 分配器的专属地址范围（`zone_map_range`）与 chunk 尾、kmem 守卫字段所用的隔离页（`kmem_guard_t`、`zm_oob_offs`） | XNU 源码 |
| `ZSECURITY_CONFIG(...)` | zone 安全类编译配置宏族：`ZONE_TAGGING`/`READ_ONLY`/`SAD_FENG_SHUI`/`PGZ_OOB_ADJUST` | XNU 源码 |
| typed kalloc / kheap | 按 C++ 类型自动分 zone（`z_kalloc_type`、`kalloc.type.<size>`）；`KHEAP_ID_*` 枚举决定 zone 归属的堆名，进而决定名称可还原性 | XNU 源码 |
| MACF | Mandatory Access Control Framework：TrustedBSD 血统的强制访问控制框架，位于 XNU `security/` | XNU 源码（`mac_policy.h` 头注释自述 TrustedBSD） |
| `mpo_*` | 策略模块实现的钩子函数命名约定：`mpo_<object>_check_<operation>` | XNU 源码 |
| `MPC_*` / `mac_check_*` | 框架对多策略聚合的分派宏与内核调用点处的封装函数（用户态无公开 API） | XNU 源码 |
| QA1574 | Apple 技术问答编号，被 `mac_policy.h` 以 `#warning` 引用，声明 MAC policy 非公开 KPI | XNU 源码 + Apple 技术文档编号 |
| AMFI / `amfi_t` | AppleMobileFileIntegrity：Apple 官方称其为 kernel extension，负责代码签名/entitlement 执行路径；内核经只读函数表 `amfi_t` 与一次性注册消费其实现 | Apple 文档（定性）+ XNU 源码（接口） |
| `amfid` | 与 AMFI 同名的用户态守护进程（`/usr/libexec/amfid`）；现代职责无 Apple 官方文档描述 | XNU/文件系统事实 + 研究界（职责） |
| CoreEntitlements | 用户态 entitlements 库（V2 接口经 `OSEntitlements` 被 AMFI/IOKit 消费）；库本体不公开 | XNU 源码（接口名）+ 核查简报负面结果（本体不公开） |
| CSM / `CS_MONITOR_TYPE_*` | Code Signing Monitor：源码中由 PPL 或 TXM 承载的页级签名校验监视抽象；类型枚举为 NONE/PPL/TXM | XNU 源码 |
| PPL | Page Protection Layer：iOS/iPadOS/visionOS/watchOS 的页保护，Apple 明言 macOS 不提供 | Apple 文档 |
| SPTM | Secure Page Table Monitor：A15/M2 起与 TXM 一起替代 PPL | Apple 文档 |
| TXM | 与 SPTM 配套、在 A15/M2 起承接代码签名与系统策略的监视器；本系列两份核查简报均只用缩写 TXM，未记录 Apple 对该缩写的展开写法 | Apple 文档（缩写）+ 本报告自拟（功能描述汇总） |
| `CS_HARD`/`CS_KILL`/`CS_KILLED` | Mach-O 代码签名旗标："don't load invalid pages" / 违规即杀 / 曾因无效被内核杀死 | XNU 源码（`cs_blobs.h`） |
| `CS_GET_TASK_ALLOW`/`CS_REQUIRE_LV`/`CS_RUNTIME` | 与 `get-task-allow` 关联的签名位（0x4）/ 要求 library validation（0x2000）/ "Apply hardened runtime policies"（0x10000，编码通用，不代表 iOS 提供该开关） | XNU 源码 |
| `CS_CONFIG_*` | 内核运行时代码签名配置位图（UNRESTRICTED_DEBUGGING/ALLOW_ANY_SIGNATURE/ENFORCEMENT_DISABLED/GET_OUT_OF_MY_WAY/INTEGRITY_SKIP/RELAX_PROFILE_TRUST/DEV_MODE_POLICY/MAP_JIT 等） | XNU 源码 |
| library validation | 动态库签名校验：iOS 官方章明文（平台库或同 team identifier） | Apple 文档 |
| hardened runtime | macOS 产品概念，Apple 文档自限"your macOS app"；iOS 无此开关 | Apple 文档（macOS 限定） |
| IOUserClient / IOUserClient2022 | IOKit 用户客户端对象：用户态进入驱动的入口；后者为承载 dispatchExternalMethod、entitlement 与 filter 加固注释的派生类 | XNU 源码 + Apple 开发者文档 |
| `externalMethod` | 驱动侧方法派发重写点；Apple 注释要求它调用 `dispatchExternalMethod()` 做基础检查 | XNU 源码（注释原文） |
| `dispatchExternalMethod()` | 框架提供的基础参数/selector 校验入口 | XNU 源码 |
| `checkEntitlement` / `kIOUserClientEntitlementsKey` | 按 selector 的 entitlement 检查字段（不满足返回 `kIOReturnNotPrivileged`）与打开 IOUserClient 所需的 entitlement 键 | XNU 源码 |
| `gIOUCFilterCallbacks` | 按 method 编号过滤的全局回调表（`io_filter_type_external_method` 等）；注册方未核实 | XNU 源码 |
| kext | 内核扩展的通行简称；Apple 在 iOS 章节里用该词指 Apple 自家的 kernel extensions（预链接进 Boot Kernel Collection），"第三方"限定词只与 macOS 同现 | Apple 文档（用法）+ 研究界（简称） |
| AuxKC | Auxiliary Kernel Collection：macOS 上第三方 kext 的合并集合 | Apple 文档 |
| UAKL | User-Approved Kernel Extension Loading：macOS 的用户同意加载流程 | Apple 文档 |
| Boot Kernel Collection | 启动期内核与 Apple kext 的集合体 | Apple 文档 |
| dext | DriverKit 用户态驱动（"driver extension"）的通行简称 | 研究界 + Apple 源码（`kexts / dexts` 并见于 Apple 官方 doc） |
| DriverKit | 用户态驱动框架；APSG 章节标题为 "DriverKit security for macOS"，可用性 prose 与 metadata 冲突 | Apple 文档（口径冲突，见附录 F） |
| KIP | Kernel Integrity Protection：内核初始化完成后启用；受保护物理区由 iBoot 装载内核与 kext | Apple 文档 |
| Fast Permission Restrictions | Apple 硅上特性表逐条列名项之一（指南 p.59-60 同段另出现 APRR 缩写） | Apple 文档 |
| SCIP | System Coprocessor Integrity Protection：Apple 硅上特性表逐条列名项之一 | Apple 文档 |
| MIE / EMTE | Memory Integrity Enforcement with Enhanced Memory Tagging Extension：A19/M5 及之后（官方明示） | Apple 文档 |
| `vm_kernel_slide` | 内核地址随机化量的源码正式名（`SECURITY_READ_ONLY_LATE`） | XNU 源码 |
| KASLR / ASLR | 前者是内核地址随机化的通行叫法（Apple 官方文档不使用该词）；后者是 Apple 官方文档用词，语境为进程级"内置 App 用 ASLR 随机化启动时的所有内存区域" | XNU 源码 + 研究界（KASLR）/ Apple 文档（ASLR） |
| PAC | Pointer Authentication Codes：Apple 官方称用五个 128-bit 秘密值签名内核指令与数据，每用户进程有独立 B keys | Apple 文档 + XNU 源码 |
| IA / IB / DA / DB | PAC 四个键的 Apple 表用法：函数指针/返回地址/数据指针/选择器型 ObjC ivar 等 | Apple 文档（表）+ XNU 源码（`asia/asib/asda/asdb`） |
| `pacibsp` / `retab` 与 `RETAA` / `PACIA` | 前者是 Apple 返回地址的签名与认证指令组合（key B）；后者是 AArch64 架构的 A-key 组合，Linux/Android 生态常用，不可套用为 Apple 口径 | XNU 源码（ccarm 宏）/ ARM 架构规范（非 Apple 一手） |
| APGA / `GCCKEY` | 前者在 XNU 树中仅出现于 EL2 细粒度陷阱位；后者本次检索 0 命中。两者是否被 Apple 用于某类指针签名均**不得断言** | XNU 源码（旁证级）+ ARM 规范 / 研究界（未获一手依据） |
| diversifier | 每任务/每上下文的认证加扰值（JOP/ROP key pid、EL0 diversifier） | XNU 源码（`pac_asm.h` 注释） |
| BTI | Branch Target Identification：Arm 特性；Apple 对外文档未列名，源码以 guarded 页实现 | XNU 源码 + ARM 规范 |
| guarded page / `ARM_PTE_GP` / `bti c` / `bti j` / `hint #34` | 被标记为受 BTI 强制的页（PTE 位 0x0004000000000000）与落地垫片指令及其编码；PPL 入口与 ccarm 宏中均可见 | XNU 源码 |
| `BTI_ENFORCED` | 控制 guarded 页设置的构建宏；公开树内无定义处 | XNU 源码（取值未公开） |
| GCS | Guarded Control Stack：Arm 的影子栈特性；XNU 无使能实现，仅见 EL2 陷阱位常量 | ARM 规范（Apple 未使用此术语） |
| exception port / behavior / flavor | 接收 Mach 异常的端口，以 send right 形式返回/设置；`exception_behavior_t` 与线程状态 flavor 两级参数决定上抛携带内容与端口能力 | man page（随源码发布）+ XNU 源码 |
| `EXC_*` / `EXC_MASK_ALL` | 异常类型族（`EXC_BAD_ACCESS`…`EXC_GUARD`/`EXC_CORPSE_NOTIFY`，`EXC_TYPES_COUNT=14`）；`EXC_MASK_ALL` 不含 CRASH 与 CORPSE_NOTIFY（仅在 KERNEL_PRIVATE 的 VALID 集合附加） | XNU 源码 |
| `ux_handler` 与 `ux_exception()`/`machine_exception()` | 内核内 Mach kobject 作为 host 级兜底 handler，把 Mach 异常翻译成 BSD 信号；映射表与机器相关优先翻译层（arm 上只处理两类） | XNU 源码 |
| `EXC_SOFT_SIGNAL` / `OS_REASON_FOUNDATION` | 前者是信号→Mach 异常的反向通道，仅在被 ptrace 时产生；后者是不可捕获异常路径上硬编码的退出原因，属进程崩溃报告通道而非 panic 通道 | XNU 源码 |
| task_for_pid | 取得他进程 task port 的调用；受 POSIX 凭证检查、`kern.tfp.policy`、MACF 上行调用三重约束 | XNU 源码 + man page |
| `get-task-allow` | entitlement 真名（无域名前缀）；带 `com.apple.security.` 前缀的写法本次未在任何一手来源确证 | XNU 源码（`cs_blobs.h` 注释） |
| `kern.tfp.policy` | sysctl：`KERN_TFP_POLICY_DENY=0`/`DEFAULT=2`，原文"All policies allow task port for self" | XNU 源码 |
| `mac_proc_check_get_task()` / `extmod_statistics` | 按 `TASK_FLAVOR_CONTROL/NAME/INSPECT/READ` 分级的 MACF 检查点；内核为 task_for_pid 调用计数的机制（`task_for_pid_count` 等） | XNU 源码 |
| paniclog / extensible paniclog | 内核在 panic 路径以 `paniclog_append_noflush()` 追加的诊断文本快照（可含 zone/kalloc 统计，取决于 `panic_include_kalloc_types`）；供 kext/dext 注入状态的机制，Apple 明写"不取全量 coredump 就无从知道其状态" | XNU 源码 + Apple 官方 doc（仓库 `doc/`） |
| `panic-full` / `panicString` / `panicFlags` / `bug_type` | 通行连字符写法的分析条目名与公开样本中的常见字段；条目名列举、字段表与取值位含义 Apple 均未文档化，二手来源存在点号写法分歧 | 研究界/维修界 |
| `translation` | 公开 panic 记录中被广泛引用的字段；语义 Apple 未定义，等同内核 slide 属研究界推断 | 研究界（字段）+ 本报告自拟（限定语） |
| `security.codesigning.monitor` / `kern.slide` | 两个零售可读的只读 sysctl：分别给出当前 CSM 类型与"内核地址随机化是否为真"的布尔值 | XNU 源码 |
| 机制链 / 四元组 / 共性前提（P1–P6）/ 判定值四档 | 为汇总与归纳临时建立的分类词，Apple 与学界均无此说法 | 本报告自拟 |

## 附录B 机制—来源分级总表

判定列的三档含义：**已核实**＝取得一手依据（Apple 文档、XNU 源码、仓库内官方 doc、随源码发布的 man page）；**部分核实**＝一手依据存在但不完整（版面未复核、本地镜像节选、只到标题级可达）；**未核实**＝本次未取得一手依据，正文按"未公开"表述。出处格式约定：源码给 `路径:行号`，指南给 `apsg.txt:行号` 或章节名，网页给 URL；行号一律以 XNU `main` 分支 tree sha f6217f8… 与该 PDF（落款 August 2026）为准。

| # | 机制陈述 | 判定 | 依据类型与出处 | 正文所在章 |
|---|---|---|---|---|
| 1 | Apple 自述 XNU 为 combining CMU Mach、FreeBSD 组件与 C++ IOKit 的 hybrid kernel | 已核实 | 一手-源码 `src/README.md:4` | 第 3 章 |
| 2 | 目录即职责：osfmk（Mach 子系统）/bsd（BSD 语义）/libkern+iokit（驱动）/security（MAC 接口）/pexpert（平台） | 已核实 | 一手-源码 `src/README.md`「The XNU Source Tree」节 | 第 3 章 |
| 3 | "Apple 公开声明大幅改动 Mach"不成立；改动幅度只能由源码/历史观察并标注为分析结论 | 已核实（负面） | README 现文 grep 无 "extensively/heavily modified" | 第 3 章 |
| 4 | 同一 XNU 树经不同 config 分别产出 macOS/iOS 内核；按功能开关（`SECURE_KERNEL` 等）而非平台宏编译 | 已核实 | 一手-源码 `config/MASTER.arm64.iPhoneOS` 等；`README.md:470-480` | 第 3 章 |
| 5 | port right 即 capability 的措辞一手存在（源码注释 + 仓库官方 doc），但 Apple 开发者/支持文档无该表述 | 已核实（层级差） | `ipc_object.h:541,551,556,572,581`；`doc/ipc_security_concepts.md:49` | 第 4 章 |
| 6 | IPC space policy：`is_policy` 不可变枚举 + dPAC 加固 | 已核实 | 一手-官方doc `doc/ipc_security_concepts.md:10-37` | 第 4 章 |
| 7 | pinned send rights：hardened 进程硬执行，其他返回 `KERN_INVALID_CAPABILITY` + 非致命 guard exception；反应由 boot-arg 决定 | 已核实 | 一手-官方doc `doc/ipc_security_concepts.md:39-113` | 第 4 章 |
| 8 | 端口类型绑定策略（service/connection/reply/exception/带 port array 的连接端口） | 已核实 | 一手-官方doc `doc/port_types.md`（含 entitlement 名与 157-164 违规异常） | 第 4 章 |
| 9 | send right 引用计数（`IE_BITS_UREFS()`）在源码/官方 doc 可证 | 已核实 | `doc/ipc_security_concepts.md:92` | 第 4 章 |
| 10 | port 名额、send right 计数上限、msgsend 资源限额有官方数字 | 未核实（官方口径不存在） | 无任何 Apple 公开文档记载；`msgsend`/`max_urefs` 于本地头文件 0 命中 | 第 4 章 |
| 11 | `mach_msg(2)` 现行公开手册页 | 未核实 | opensource.apple.com/man 相关 URL 实测 404 | 第 4 章 |
| 12 | kalloc 构建在 zone 分配器之上，zone 命名 `kalloc.<size>`/`kalloc.type.<size>`/`early.kalloc` | 已核实 | 一手-源码 `zalloc.c:2847-2859`、`track_kalloc_zones()` | 第 5 章 |
| 13 | zone_map 有专属地址范围、chunk 尾 guard page、`zm_oob_offs` | 已核实 | `zalloc.c:399,804,194,233` | 第 5 章 |
| 14 | 安全类开关实名：`ZSECURITY_CONFIG(ZONE_TAGGING/READ_ONLY/SAD_FENG_SHUI/PGZ_OOB_ADJUST)`；不存在 "per-page guard" 公开宏 | 已核实 | `zalloc.c`（宏名逐一 grep）；`vm_kern.c:216` | 第 5 章 |
| 15 | `kmem_guard_t`/`kmem_entry_validate_guard` 存在；`kmem_allocate()` 本体在本地节选中缺失 | 部分核实 | `vm_kern.c:224-238,507-518`（本地镜像为节选） | 第 5 章 |
| 16 | kheap 分类与 typed kalloc 机制 | 已核实（机制）/部分核实（iOS 生产启用程度） | `zalloc.c:2449-2450,2861-2887,454-456,695-711` | 第 5 章 |
| 17 | Apple 自述 hardened kernel allocator 对抗真实利用（SockPuppet 假设文） | 部分核实 | https://security.apple.com/blog/what-if-we-had-sockpuppet-in-ios16/ （标题与可达性核实，正文段落未逐段引） | 第 5 章 |
| 18 | MACF 承自 TrustedBSD，仅存在于 XNU 树内、无独立开源发布物 | 已核实 | `mac_policy.h:65` FreeBSD CVS 标签、:73 "TrustedBSD MAC Framwork on Darwin" | 第 6 章 |
| 19 | 注册与 ops 表：`mac_policy_register()`、`MAC_POLICY_SET()`、`MAC_POLICY_OPS_VERSION 91`、`mpo_<object>_check_<operation>`、未订阅即跳过 | 已核实 | `mac_policy.h:6406,6565,5966,158-162,75-77` | 第 6 章 |
| 20 | MACF 非公开 KPI（Apple 自述警告引用 QA1574） | 已核实 | `mac_policy.h:79-81` `#warning` 原文 | 第 6 章 |
| 21 | IOKit 路径被 MACF 覆盖（open/set properties/get property 钩子） | 已核实 | `mac_policy.h:1099-1149`、`security/mac_iokit.c` | 第 6、9 章 |
| 22 | iOS 上已注册 MAC 策略模块清单 | 未核实（官方未公开） | 开源树内零策略模块；清单仅有 kernelcache 符号/研究文献（Levin 等） | 第 6 章 |
| 23 | AMFI 主体是内核内组件（官方称 AppleMobileFileIntegrity kernel extension） | 已核实 | 一手-文档 指南 §research.* entitlements（约 p.85 / apsg 对应段） | 第 7 章 |
| 24 | `amfi_t` 只读函数表 + `amfi_interface_register()`，重复注册即 panic | 已核实 | `libkern/libkern/amfi/amfi.h:38,245-265,294`；`libkern/amfi/amfi.c` | 第 7 章 |
| 25 | `amfid` 在现代 iOS 上的职责 | 未核实（无官方文档） | 守护进程存在于根文件系统；职责只有研究级（IHH、exploitreversing） | 第 7 章 |
| 26 | boot-args 实名与掩码位、映射到 `CS_CONFIG_*` | 已核实 | `kern_codesigning.c:219-315`；`code_signing.h:42-53` | 第 7 章 |
| 27 | 旁路解析仅 DEVELOPMENT/DEBUG；RELEASE 上"not respected by any code signing component" | 已核实 | `kern_codesigning.c:217-223` 注释原文 | 第 7、14 章 |
| 28 | `research.` 命名空间仅被 AMFI 的 RESEARCH variant 尊重；客户设备上该进程在签名校验时被终止 | 已核实 | 一手-文档 指南 p.85 原文 | 第 7、15 章 |
| 29 | 内核启动后控制可运行进程、要求 Apple 证书签名 | 已核实 | 一手-文档 指南 p.114（iOS/iPadOS/tvOS/visionOS/watchOS 章） | 第 8 章 |
| 30 | 运行期每个可执行内存页在校验后才加载 | 已核实 | 一手-文档 指南 p.115 + 源码 `pmap_cs.c/h` 互证 | 第 8 章 |
| 31 | CSM = PPL 或 TXM 承载的页级签名监视器；`security.codesigning.monitor` 只读 | 已核实 | `code_signing.h:35-37`、`kern_codesigning.c:111-115`、`pmap_cs.h:42,71-88` | 第 8 章 |
| 32 | 监视器选择由 `board_config` 编译期决定；设备级映射不公开 | 已核实（宏与逻辑）/未核实（清单） | `pmap_cs.h:70-88`，注释原文 "…defined for the same targets in board_config.h" | 第 8 章 |
| 33 | 锁定模式状态同步给 CSM（含 MAP_JIT 相关） | 已核实（源码级）/官方无对应表述 | `kern_codesigning.c:1108-1118` `csm_check_lockdown_mode()` | 第 8 章 |
| 34 | entitlement 四段位置：解析（`ubc_subr.c` csb_*）、判定（AMFI+CoreEntitlements V2）、授权效果（trust cache）、消费（MACF/IOUC） | 已核实（(c) 需平台限定） | `ubc_subr.c:3160`；`amfi.h:37-38,245-253`；`IOUserClient.cpp:1595`；指南 p.69（Apple silicon 语境） | 第 8 章 |
| 35 | library validation 在 iOS 官方文档有明文 | 已核实 | 指南 p.114-115 原文；退订 entitlement 专页（细节字段部分核实） | 第 8 章 |
| 36 | hardened runtime 是 macOS 产品概念，iOS 无该开关 | 已核实（负面：iOS 无） | https://developer.apple.com/documentation/xcode/configuring-the-hardened-runtime（"your macOS app"） | 第 8 章 |
| 37 | "MACs require code signing to enable entitlements" 属 macOS 章 | 已核实（须加平台限定） | 指南 p.119 | 第 8 章 |
| 38 | `dispatchExternalMethod()` 负责 selector 与基础参数检查；`externalMethod` 必须调用它 | 已核实 | `IOUserClient.h:549-562` 注释原文 | 第 9 章 |
| 39 | 两级 entitlement：打开级 `kIOUserClientEntitlementsKey` 与 per-selector `checkEntitlement`（否则 `kIOReturnNotPrivileged`） | 已核实 | `IOUserClient.h:546-547,559-562` | 第 9 章 |
| 40 | 框架与 AMFI 在用户客户端路径直接耦合（校验 task/entitlement 后转 `amfi->OSEntitlements…`） | 已核实 | `IOUserClient.cpp:1584-1595` | 第 7、9 章 |
| 41 | `gIOUCFilterCallbacks` 按 method 编号过滤；`IOUserClient2022` 承载加固注释 | 已核实（存在性）/未核实（注册方） | `IOUserClient.cpp:5055,5092,5214,5328`；`IOUserClient.h:192,564,112-114,125-140` | 第 9 章 |
| 42 | Apple 现行开发者文档有"驱动必须校验用户客户端输入"专章 | 未核实 | 仅存档 IOKit Fundamentals 与旧 Driver Design Guidelines 的二级转载，一手 URL 未定位 | 第 9 章 |
| 43 | IOSurface/AppleAVE/GPU 驱动为高暴露攻击面 | 已核实（研究级，不得写成 Apple 承认） | Project Zero 42452365；Black Hat EU-17 Donenfeld；POC2019 Liang；8ksec DarkSword（二级） | 第 9 章 |
| 44 | "某 iOS 版本官方移除某类驱动/收紧 IORegistry" | 未核实（官方口径不存在） | 指南全文 grep "IORegistry"/"user client" 无 iOS 版本化条目 | 第 9 章 |
| 45 | Apple 对外文档只称 ASLR，不使用 "KASLR" | 已核实（负面） | `apsg.txt:5640-5650`；全文检索 KASLR/kernel slide 命中 0 | 第 10 章 |
| 46 | 内核随机量正式名 `vm_kernel_slide`（`SECURITY_READ_ONLY_LATE`）及同组符号 | 已核实 | `osfmk/arm64/arm_vm_init.c:97-110,105` | 第 10 章 |
| 47 | slide 由 iBoot 装载阶段决定，内核反推（MH_FILESET 与静态 kernelcache 两条算式） | 已核实 | `osfmk/arm/arm_init.c:231,246,286,305` | 第 10 章 |
| 48 | KIP 原文（受保护物理区由 iBoot 装载内核与 kext）；KIP 与随机化不是同一机制 | 已核实 | `apsg.txt:2716-2726` | 第 10 章 |
| 49 | 零售可读的只有 `kern.slide` 布尔；`kern.kernel_slide`/`kernel_text_exec_base*` 仅 DEBUG/DEVELOPMENT | 已核实 | `kern_sysctl.c:4313,4329-4330,4332-4351` | 第 10、20 章 |
| 50 | arm64 无 `PE_parse_boot_argn("slide"/"kaslr"/"kaslider")`；x86 走 `boot_args.kslide`；`slide=0` 只见第三方文档 | 已核实（源码）+ L2（Hex-Rays） | `i386_vm_init.c:284-293`；https://docs.hex-rays.com/core/debugger/tutorials/xnu_debugger_primer | 第 10 章 |
| 51 | boot-args 有 iBoot 内建白名单；`sip3` 为真则不过滤（macOS 表述） | 已核实 | `apsg.txt:2128-2135` | 第 10、19 章 |
| 52 | PAC 官方表述：五个 128-bit 秘密值签名内核指令与数据、每用户进程有独立 B keys | 已核实 | `apsg.txt:2759-2764`（逐字）；卷首 `:212-214` | 第 11 章 |
| 53 | PAC 键—用途表（Function Return Address→IB 等 11 行） | 部分核实（版面归属未逐格复核） | `apsg.txt:2765-2786`（PDF 文本层） | 第 11 章 |
| 54 | `ptrauth.h` 枚举与官方表互证：返回地址 = `process_dependent_code`(asib) | 已核实 | `EXTERNAL_HEADERS/ptrauth.h:30-90` | 第 11 章 |
| 55 | `SIGN_LR`→`pacibsp`、`AUTH_LR_AND_RET`→`retab`：返回地址走 key B | 已核实 | `osfmk/arm64/corecrypto/ccarm_pac_bti_macros.h:52-64` | 第 11 章 |
| 56 | 内核态启用 PAC，EL1 A key 签堆上指针；A=JOP、B=ROP 的内部命名 | 已核实 | `osfmk/arm64/pac_asm.h:50-110,103-108` | 第 11 章 |
| 57 | SPTM/TXM 环境下 PAC key 由更高特权级托管（HVC） | 已核实（源码） | `pac_asm.h` 的 `VMAPPLE_PAC_SET_EL0_DIVERSIFIER`/`SET_B_KEYS`；`apsg.txt:2818-2830` | 第 11 章 |
| 58 | 数据指针也受认证（ObjC isa/super/vtable/Block descriptor + 内核测试 SPI） | 已核实 | `apsg.txt` 表格；`bsd/tests/ptrauth_data_tests_sysctl.c:78` | 第 11 章 |
| 59 | `GCCKEY`/`APGA` 被 Apple 用于某类指针签名 | 未核实（公开源码未披露） | `GCCKEY` 全树 0 命中；`APGA` 仅 `proc_reg.h:1254-1255,1363-1364` 陷阱位 | 第 11 章 |
| 60 | BTI/GCS 属 Apple 公开列名的硅上特性 | 未核实（负面：命中 0） | `apsg.txt` 检索 BTI/branch target/GCS/guarded control 命中 0；列名清单见 `:317-325`、`:2689-2704` | 第 12 章 |
| 61 | BTI 实现：guarded 页 + `ARM_PTE_GP` + `bti c/j` 垫片 + PPL 入口垫片 | 已核实 | `arm_vm_init.c:988-990,1075-1082,375,1404,1441,1462,1466`；`proc_reg.h:2307`；`asm.h:222-241`；`pmap_internal.h:92-96` | 第 12 章 |
| 62 | `BTI_ENFORCED` 在公开树内无定义处，零售是否强制无法判定 | 已核实（负面） | 全树仅 `#if` 使用 | 第 12、19 章 |
| 63 | 用户态可查询 `hw.optional.arm.FEAT_BTI` | 未核实（纠正早前口径） | `CAP_BIT_FEAT_BTI=36` 与 `gARM_FEAT_BTI` 无第二处使用；`tests/arm_cpu_capabilities.c` 不含 BTI | 第 12 章 |
| 64 | AuxKC 文本不能标 guarded（"defeats the purpose of BTI"）+ 工程预期措辞 | 已核实 | `arm_vm_init.c:1243-1250` 原文 | 第 12、19 章 |
| 65 | GCS 是否在 iOS 启用/覆盖内核态；GCS 与 PAC 关系 | 未核实（无公开证据） | 仅 `proc_reg.h:1162-1165,1293-1296` EL2 陷阱位常量 | 第 12 章 |
| 66 | SPTM 与 MIE 的芯片口径 Apple 明示（A15/M2+；A19/M5+） | 已核实 | `apsg.txt:2706`、`:2834-2836` | 第 8、12 章 |
| 67 | 异常按 thread→task→host 三级上抛 | 已核实 | `osfmk/kern/exception.c:702-787` | 第 13 章 |
| 68 | host 级兜底是 Mach kobject `ux_handler`，信号由其翻译而来 | 已核实 | `osfmk/kern/ux_handler.c:53-59,62,76,91` | 第 13 章 |
| 69 | `EXC_TYPES_COUNT=14`、掩码 31 位、`EXC_MASK_ALL` 不含 CRASH/CORPSE_NOTIFY | 已核实 | `osfmk/mach/arm/exception.h:34`；`exception_types.h:68-103,148,165-179` | 第 13 章 |
| 70 | 异常端口以 send right 返回/设置（含 behavior 与 flavor 两级参数、swap 原子替换） | 已核实 | `osfmk/man/task_get_exception_ports.html`、`task_set_exception_ports.html` 等 | 第 13 章 |
| 71 | iOS 无开发者模式且非 corpse 时只交付不可移动 read port | 已核实 | `exception.c:351-364` 原文（含工单号 94669540）；`ux_handler.c:143-144` | 第 13、19、20 章 |
| 72 | Mach→信号映射表；arm 上 `machine_exception()` 只处理两类 | 已核实 | `bsd/uxkern/ux_exception.c:49-121`；`bsd/dev/arm/unix_signal.c:931-949` | 第 13 章 |
| 73 | `EXC_SOFTWARE + EXC_SOFT_SIGNAL → SIGKILL`（源码原文如此，常被误写） | 已核实 | `ux_exception.c` 映射表 | 第 13 章 |
| 74 | 反方向（信号→Mach 异常）仅在被 ptrace 时发生 | 已核实 | `bsd/kern/kern_sig.c:2716-2732,3488` | 第 13 章 |
| 75 | 不可捕获异常按策略直接退出，`os_reason` 硬编码 `OS_REASON_FOUNDATION` | 已核实 | `exception.c:876-891` | 第 13、16 章 |
| 76 | `pac_exception_telemetry`（默认 false）与 `CA_EVENT(pac_exception_event…)`、`panic_on_exception_triage` 存在；零售日志可见性未知 | 已核实（存在性）/未核实（可见性） | `exception.c:736-742,791-800` | 第 13、16 章 |
| 77 | `task_for_pid` 三重约束：POSIX 凭证检查 + `kern.tfp.policy` + `mac_proc_check_get_task()` 上行调用，且按 flavor 分级 | 已核实 | `bsd/kern/kern_proc.c:5585-5601,5602-5664,5671-5680`；`sysctl.h:887-889`；`mac_process.c:430`；flavor 检查点 :5772,5905,6007,6129,6348 | 第 14 章 |
| 78 | `kern.tfp.policy` 默认 `KERN_TFP_POLICY_DEFAULT=2`；"All policies allow task port for self" | 已核实 | `kern_proc.c:5549,5580-5582`；`sysctl.h:887-889` | 第 14 章 |
| 79 | `get-task-allow` 真名无前缀；测试 plist 逐字含 `task_for_pid-allow` 等 | 已核实（字符串）/未核实（零售接受哪些） | `cs_blobs.h:38`；`tests/task_for_pid_entitlement.plist` | 第 14 章 |
| 80 | `com.apple.security.cs.debugger` 只声明 macOS（introducedAt 10.7），含 10 小时会话等 macOS 语境细节 | 已核实 | https://developer.apple.com/documentation/bundleresources/entitlements/com.apple.security.cs.debugger | 第 14、20 章 |
| 81 | 内核为每次 task_for_pid 计数（可作主机侧信号，读取需特权） | 已核实 | `extmod_statistics.c:73-84`；`vm_statistics.h:203-204` | 第 14、20 章 |
| 82 | `CS_GET_TASK_ALLOW` 不沿非 fork 路径继承 | 已核实 | `kern_fork.c:1080-1084` | 第 14 章 |
| 83 | 零售 iPhone 有无可用的内核调试通道 | 未核实（公开资料未披露） | `osfmk/kdp/`、`dbgwrap.c`、`alternate_debugger.c` 存在；APSG 无 kernel debugging 章节 | 第 14 章 |
| 84 | iOS 上"可调试的驱动"指用户态 DriverKit 驱动进程 | 已核实 | `kern_proc.c:3268-3282`（`#if XNU_TARGET_OS_IOS`）；`mach_process.c:545`；`kern_sig.c:331` | 第 14、15 章 |
| 85 | 第三方 kext 全部表述限定 macOS；官方立场为"不再推荐" | 已核实 | `apsg.txt:3511-3522,3528-3559,3577-3594,1666-1668` | 第 15 章 |
| 86 | iOS 内核扩展只有 Apple 自家（Boot Kernel Collection）；AuxKC 与研究设备语境限定 | 已核实 | `apsg.txt:1371-1374,1495-1497,2164-2166` | 第 15 章 |
| 87 | iOS 源码明确建模第三方 dext 的 user-client 准入（Team ID + 两个 entitlement） | 已核实 | `IOUserServer.cpp:4760-4790`；`IOKitKeys.h:122-151`、`IOKitKeysPrivate.h:138` | 第 9、15 章 |
| 88 | DriverKit 可用性 prose 与 metadata 冲突（iPhone 未点名为 prose） | 已核实（冲突本身） | https://developer.apple.com/documentation/driverkit | 第 15 章、附录 F |
| 89 | tvOS/watchOS/bridgeOS/audioOS 的第三方内核组件政策 | 未核实（Apple 未公开记载） | APSG 检索 bridgeOS/audioOS 命中 0；CPRNG 节 `:3849-3852` 只证明内核家族共用 | 第 15 章 |
| 90 | 研究设备授权模型与生产隔离（panic 循环、RESEARCH variant 终止、仅送度量值） | 已核实 | `apsg.txt:3979-4053,3990` | 第 7、15、20 章 |
| 91 | Analytics Data 查看路径与分析开关逐字名 | 已核实 | https://www.apple.com/legal/privacy/data/en/device-analytics/ ；`apsg.txt:2650-2655`；app-analytics 页与 support/108971 | 第 16、20 章 |
| 92 | `panic-full` 条目名与 panic 字段表可逐一在 Apple 文档查到 | 未核实 | Apple 未列条目名；`panicString`/`panicFlags`/`bug_type` 仅公开样本；`FallToPanic`、`loader_flags`、`debugInfo`、`mntn_info`、`console-ramoops` 未确证 | 第 16 章 |
| 93 | `translation` 语义 = 内核 slide | 未核实（研究界推断） | Apple 未定义；检索本地 XNU 树 `ramoops` 0 命中 | 第 16、20 章 |
| 94 | paniclog 内容由内核以不刷新方式追加诊断文本 | 已核实 | `zalloc.c:3463-3485,7614-7674` 的 `paniclog_append_noflush()` 调用与文本 | 第 16 章 |
| 95 | panic 报告不含完整内存取证（Apple 原文） | 已核实 | `src/doc/extensible_paniclog.md` 原文；`iokit/IOKit/IOExtensiblePaniclog.h` | 第 16、20 章 |
| 96 | iOS 内核崩溃"几乎总是指向硬件问题" | 已核实为 L2（Apple 未记载） | https://www.ifixit.com/Wiki/iPhone_Kernel_Panics | 第 16、20 章 |
| 97 | "存在名为 iCloud 私有报告 的分析开关" | 未核实（Apple 未使用该措辞） | APSG 与法务页逐字名仅四条 | 第 16 章 |
| 98 | Apple 公告的组件名词表：`Kernel`/`AppleMobileFileIntegrity`/`AppleKeyStore`/`GPU Drivers`/`RPAC`/具体驱动名 | 已核实 | §B15 表 18 行，URL 逐条（如 support.apple.com/en-us/120893、122282、125632） | 第 17 章 |
| 99 | 仅 CVE-2024-23225、2025-31201、2025-31200、2025-24200、2025-43200 可写"Apple 标注可能被利用" | 已核实 | 页面原文 "Apple is aware of a report…"；其余条目无该句 | 第 17 章 |
| 100 | `Kernel Cache File`/`Mach IPC`/单一 `IOKit`/`GPU Driver` 单数 可作组件名 | 未核实（本次抓取页面未出现，须先补 macOS 公告一手） | §B15 组件词表负面结果 | 第 17 章 |

## 附录C iOS / macOS / 其他平台差异表

填写规则：**凡 Apple 未记载的写「未公开」，不留空、不推断**；「不适用」只用于 Apple 明确排除或该平台确认无此机制的条目。visionOS/watchOS 两列在多数行只有间接证据（指南章节标题把它们与 iOS/iPadOS/tvOS 并列），因此列宽被压缩进"其他平台"一列，逐列断言反而超出证据。

| # | 机制/条目 | iOS / iPadOS | macOS | visionOS / watchOS | tvOS | bridgeOS / audioOS | 依据层级 | 章号 |
|---|---|---|---|---|---|---|---|---|
| 1 | XNU 内核家族共用 | 是 | 是 | 是 | 是 | 未公开（APSG 检索命中 0） | 一手-源码 config 文件族 + 一手-文档（CPRNG 节列六平台内核） | 3 |
| 2 | 强制 Apple 证书签名 | 是 | 是（实现路径不同） | 是（同章并列列出） | 是（同章并列列出） | 未公开 | 一手-文档 p.114 | 8 |
| 3 | 运行期逐页签名校验 | 是 | 未公开（无 PPL 语境下由普通内核路径完成，属限定表述） | 同 iOS 章口径 | 同 iOS 章口径 | 未公开 | 一手-文档 p.115 + 一手-源码 | 8 |
| 4 | PPL | 是（官方列 iOS/iPadOS/visionOS/watchOS） | **不适用**（官方明言不提供，理由：PPL 只适用于所有执行代码都需签名的系统） | 是 | 未公开（该段未列 tvOS） | 未公开 | 一手-文档 p.59-60 | 8 |
| 5 | SPTM + TXM（A15/M2+ 替代 PPL） | 是 | 是（跨 iOS 与 macOS） | 未公开逐条 | 未公开 | 未公开 | 一手-文档（"A15 or later and M2 or later SOCs"） | 8、11、15 |
| 6 | KIP / Fast Permission Restrictions / SCIP | 是（特性表列名） | 是 | 未公开逐条 | 未公开 | 未公开 | 一手-文档特性表 | 10、12 |
| 7 | MIE（EMTE） | A19/M5+ 明示 | 同 | 未公开 | 未公开 | 未公开 | 一手-文档 | 12 |
| 8 | PAC | 是（官方表述 + 键表） | 是 | 未公开逐平台 | 未公开 | 未公开 | 一手-文档 + 一手-源码 | 11 |
| 9 | BTI | 源码具备能力，构建取值未公开 | 源码具备能力；AuxKC 例外只在 macOS 有意义 | 未公开 | 未公开 | 未公开 | 一手-源码 + 负面结果 | 12 |
| 10 | GCS | 未公开（无使能实现） | 未公开 | 未公开 | 未公开 | 未公开 | 一手-源码（仅 EL2 陷阱位常量） | 12 |
| 11 | 内核地址随机化术语 | Apple 只称 ASLR；"KASLR" 为源码/研究界用语 | 同 | 同 | 同 | 未公开 | 一手-文档负面 + 一手-源码 | 10 |
| 12 | `kern.slide` 可读布尔 | 是（源码口径） | 是（另有 x86 `boot_args.kslide` 路径） | 未公开 | 未公开 | 未公开 | 一手-源码 | 10、20 |
| 13 | 用 boot-args 关闭内核 slide | 不适用（arm64 无该解析；零售无公开路径） | 第三方工具文档记载调试用法（非 Apple 公开文档） | 未公开 | 未公开 | 未公开 | L2（Hex-Rays）+ 一手-源码负面 | 10 |
| 14 | boot-args 白名单过滤 / `sip3` | 未公开（iOS 无等价公开路径） | 是（APSG SIP 一节原文） | 未公开 | 未公开 | 未公开 | 一手-文档 | 10、19 |
| 15 | CS/AMFI boot-args 豁免 | 仅 DEVELOPMENT/DEBUG 解析；RELEASE 不认 | 同源码口径 | 源码口径一致 | 源码口径一致 | 未公开 | 一手-源码注释原文 | 7、14 |
| 16 | library validation | 是（iOS 官方章明文，默认可退订需专列 entitlement） | 是（主要与 hardened runtime 绑定） | 官方章标题含 tvOS/visionOS/watchOS | 是（同章） | 未公开 | 一手-文档 p.114-115 | 8 |
| 17 | hardened runtime | **不适用**（Apple 文档自限 macOS app） | 是 | 不适用 | 不适用 | 未公开 | 一手-文档（开发者文档） | 8 |
| 18 | "MACs require code signing to enable entitlements" | 未公开（该句属 macOS 章，不可平移） | 是 | 未公开 | 未公开 | 未公开 | 一手-文档 p.119（须加限定） | 8 |
| 19 | 第三方 kext | **不适用**（无公开可用路径） | 是（但官方"不再推荐"，并给出移除路线图） | 未公开 | 未公开 | 未公开 | 一手-文档章节标题限定 | 15 |
| 20 | AuxKC | 不适用（零售无公开启用路径；源码注释语境对应 macOS 第三方 kext） | 是 | 未公开 | 未公开 | 未公开 | 一手-文档 + 一手-源码注释 | 12、15 |
| 21 | UAKL / kext receipt / `spctl` 同意流程 | 不适用 | 是 | 未公开 | 未公开 | 未公开 | 一手-文档 | 15 |
| 22 | Boot Kernel Collection | 是（启动链原文） | 是（Apple silicon 语境同段；x86 为 immutablekernel） | 未公开 | 未公开 | 未公开 | 一手-文档 | 15 |
| 23 | 系统扩展（user space 扩展） | 未公开（该表述属 macOS 10.15 章） | 是 | 未公开 | 未公开 | 未公开 | 一手-文档 | 15 |
| 24 | DriverKit / dext | prose 点明 iPadOS（M 系芯片），metadata 另列 iOS 16.0；源码含 iOS 第三方 dext 准入 | 是（章节标题 "DriverKit security for macOS"） | 未公开（visionOS 等价路径未取到页面） | 未公开 | 未公开 | 一手-文档（冲突，见附录 F 第 1 条）+ 一手-源码 | 9、15 |
| 25 | iOS 上可被调试的"驱动" | 是——但指用户态 DriverKit 驱动进程，非内核态 kext | 未公开逐条 | 未公开 | 未公开 | 未公开 | 一手-源码（`proc_is_third_party_debuggable_driver()`） | 14、15 |
| 26 | Apple Security Research Device | 是且**仅限研究设备**（特别融合的 iPhone） | 不适用（该节描述对象是 iPhone） | 未公开 | 未公开 | 未公开 | 一手-文档 | 7、15、20 |
| 27 | 自定义 kernelcache 个性化/恢复 | 仅研究设备 | 未公开 | 未公开 | 未公开 | 未公开 | 一手-文档 | 15、20 |
| 28 | `research.` entitlement 在生产设备上的处置 | 是——进程在签名校验时被终止 | 未公开逐平台 | 未公开 | 未公开 | 未公开 | 一手-文档 p.85 | 7 |
| 29 | Mach 异常三级上抛 / `ux_handler` | 是（源码一致，无平台分支证据） | 是 | 源码口径一致 | 源码口径一致 | 未公开 | 一手-源码 + 手册页 | 13 |
| 30 | 异常端口 read/control 差异（开发者模式） | **iOS 特有的能力收口**（源码注释明确） | 未公开对照表述 | 未公开 | 未公开 | 未公开 | 一手-源码原文（含工单号） | 13 |
| 31 | `task_for_pid` 三重检查与 `kern.tfp.policy` | 是 | 是 | 源码口径一致 | 源码口径一致 | 未公开 | 一手-源码 | 14 |
| 32 | `com.apple.security.cs.debugger` | **不适用**（Apple 页面 metadata 只声明 macOS 10.7） | 是 | 不适用 | 不适用 | 未公开 | 一手-文档 metadata | 14、20 |
| 33 | `com.apple.security.only-one-exception-port`、`com.apple.developer.allow-connection-port-with-port-array` | 是（端口类型 doc 中的 entitlement 要求，未标平台分支） | 未公开 | 未公开 | 未公开 | 未公开 | 一手-官方doc `port_types.md` | 4 |
| 34 | Analytics Data 与共享开关 | 是（逐字名 "Share iPhone & Watch Analytics" / "Share iPad Analytics"） | 是（"Share Mac Analytics"） | 未公开 | 未公开 | 未公开 | 一手-文档 + 法务页 | 16、20 |
| 35 | panic 条目名与字段表官方列举 | 未公开 | 未公开 | 未公开 | 未公开 | 未公开 | 一手-文档负面结果 | 16 |
| 36 | Managed Device Attestation 的第三方 kext 允许字段 | 字段跨平台列出，**语义只对 Apple silicon Mac 成立** | 是（语义源） | 字段列出（visionOS 1.1） | 字段列出（tvOS 16） | 未公开 | 一手-文档 | 15、20 |
| 37 | 内核组件公告词表（`Kernel`/`AppleMobileFileIntegrity`/`RPAC`/`GPU Drivers` 等） | 本次抓取为 iOS/iPadOS 页面 | 未抓取，不作断言 | 未公开 | 未公开 | 未公开 | 一手-文档（公告页逐字） | 17 |

## 附录D 未公开与未决事项汇总

**开头声明：本表仅表示本次核查未取到一手依据，不等于该事实不存在。** 表中每一条在正文里都被用作"降级表述"的理由，而不是"机制缺失"的证据。合并自两份核查简报的缺口清单（前半部分约 12 条、后半部分 28 条），已去重。

| 编号 | 缺口内容 | 为何重要 | 正文据此降级的位置 |
|---|---|---|---|
| D-01 | `BTI_ENFORCED` 在零售 iOS 构建中的取值（公开树只有 `#if`、无定义处） | 决定 BTI 能否被写成"已启用" | 12.2、18.4、19.2、21.4(U1) |
| D-02 | 用户态能否查询 BTI 支持（capability 自测表不含 BTI） | 阻断"可读设备能力位验证"的路径 | 12.4、18.4、20.4 |
| D-03 | Apple 对 BTI 的任何面向用户表述；是否使用 "Guarded Control Stack" 术语 | 决定第 12 章标题级写法 | 12.1、18.4、21.2 |
| D-04 | GCS 是否在 iOS 内核/用户态启用、是否覆盖内核态；GCS 与 PAC 互补还是替代 | 只能写"无公开证据"，不可写覆盖/不覆盖 | 12.6、18.6、21.4(U6) |
| D-05 | BTI 与 GCS 各自的首个启用芯片/首个 iOS 主版本 | 无法建立版本—机制对应表 | 12.6、18.6、21.4(U8) |
| D-06 | `GCCKEY`/`APGA` 是否被 Apple 用于某类指针签名 | 红线：禁止把 Arm 概念当 Apple 实现 | 11.6、21.4(U5) |
| D-07 | APSG 的 PAC 键—用途表 PDF 版面逐格归属（含 "Selector Typed Objective-C ivars" 行 Salt 列乱码）；特性表 PAC 为勾选图形 | 表格是第 11 章核心依据，须保留"部分核实" | 11.2、18.4、21.4(U12) |
| D-08 | 零售 iOS 关闭/影响内核 slide 的公开途径；boot-args 白名单在 iOS 的等价物 | 决定能否谈"随机化可调" | 10.6、19.4 |
| D-09 | slide 的熵值、分布、是否每次启动更换 | 禁止定量断言随机强度 | 10.3、18.5、21.4(U9) |
| D-10 | `kern.slide` 之外是否还有其他零售可见的 slide 暴露面 | 决定第 20 章可观测量清单是否完整 | 10.5、20.4 |
| D-11 | iOS 上 MACF 已注册策略模块的官方清单（含 Sandbox/AMFI 注册名） | "谁在被强制"不可枚举 | 6.6、18.3、19.3、21.4(U4) |
| D-12 | `amfid` 当前职责与其 plist 载体的官方描述 | AMFI 双层结构只能写到定性 | 7.1、7.6、21.4(U3) |
| D-13 | AMFI kext 本体、Sandbox.kext、board_config 设备清单、CoreEntitlements 用户态库均不公开 | 判定逻辑与逐机型 CSM 映射不可复核 | 5.3、7.2、8.3、18.3、19.3 |
| D-14 | 哪个 entitlement 在零售 iOS 上被闭源 AMFI 实际接受（只有源码测试向量） | 禁止把测试 plist 当生产支持列表 | 14.3、19.3、20.3 |
| D-15 | `com.apple.security.get-task-allow` 带前缀写法是否真实存在 | 字符串口径的唯一正确形态 | 14.2、附录 F 第 6 条 |
| D-16 | 现代零售 iPhone 是否有可用内核调试通道（`osfmk/kdp/` 等实现存在但可用性未记载） | 决定第 14 章与研究层建议的边界 | 14.5、19.4、20.4、21.4(U2) |
| D-17 | tvOS / watchOS / bridgeOS / audioOS 上的第三方内核组件政策 | 附录 C 大量"未公开"格的来源 | 15.5、18.6、附录 C 行 19-24 |
| D-18 | visionOS 是否有等价 DriverKit / 第三方 dext 路径 | 跨平台策略表的空格 | 15.4、附录 C 行 24 |
| D-19 | Apple 一手文档中"驱动必须校验用户客户端输入/方法索引"的现行专章 | IOKit 责任归属只有源码注释一层依据 | 9.3、9.6、19.5 |
| D-20 | `gIOUCFilterCallbacks` 的注册方（哪个子系统注入过滤策略） | 不能写成"由沙箱注册" | 9.5、19.3 |
| D-21 | "某 iOS 版本官方移除某类驱动 / 收紧 IORegistry"的官方口径 | 禁止版本化移除叙事 | 9.6、18.5 |
| D-22 | typed kalloc 与 `ZSECURITY_CONFIG(...)` 各开关在 iOS 生产的启用程度 | 内核内存加固不能写成已全面启用 | 5.5、5.6、19.2 |
| D-23 | zone 名可枚举性的现状（`zone_info`/`zprint` 已收紧） | 观测手段的时效性 | 5.6、18.3 |
| D-24 | `panic_include_kalloc_types` 在零售构建是否被置 true；`pac_exception_telemetry` 等 tracepoint 是否出现在零售日志 | 决定 panic 里能看到什么 | 13.6、16.4、19.2 |
| D-25 | `Analytics Data` 条目名的官方列举（含 `panic-full` 连字符 vs 点号）；`FallToPanic`、`retained-bytes-due-to-background-sandbox` 未确证 | 事件响应流程不能引用未证实名称 | 16.2、20.4、附录 E 行 26 |
| D-26 | panic 字段 `loader_flags`/`translation`/`debugInfo`/`mntn_info`/`console-ramoops`/`avail-rambytes` 的官方存在性与语义 | `translation` 被误当内核 slide 是高频错误 | 10.6、16.3、20.4、20.5 |
| D-27 | PAC/BTI 违规是否显式出现在 panic 字符串（Apple 正式记载缺失） | 决定"保护被触发"能否被观测 | 11.6、12.6、16.5、21.4(U11) |
| D-28 | `Analytics Data` 保留时长/轮转策略；内核 panic 是否经 "Share With App Developers" 分发 | 决定证据保存的顺序与可得性 | 16.6、20.2、20.4 |
| D-29 | Apple 对"某类 panic 条目何时出现"的官方表述（唯一可引的是 macOS IOMMU 与研究设备 panic loop） | 禁止"每次 panic 必产生某条目" | 16.2、19.5 |
| D-30 | Mach 异常—信号分层是否有 Apple 面向用户的机制文档（APSG 命中 0） | 第 13 章整章层级降级为源码 + man page | 13.1、18.4、21.2 |
| D-31 | `mach_msg(2)`/port 分配类 Darwin 手册页（相关 URL 实测 404）与现行传输语义文档 | OOL/deadline 语义只能以头文件与历史手册为限 | 4.6、18.3 |
| D-32 | port 名额、send right 计数上限、msgsend 资源限额的官方数值（结论性缺失） | 任何数字必须标"未公开/源码观察" | 4.6、18.3 |
| D-33 | 0xdeadbeef《Your (IOSurface) Kernel is Mine》原文 URL | 引用前需补核，暂不入正文出处表 | 9.6 |
| D-34 | DarkSword 的 AGX 部件 CVE-2025-43510/43520 在 Apple 公告页的官方组件名（页面 JS 渲染未取到） | GPU 相关只能标媒体转述级 | 9.6、17.4 |
| D-35 | CVE-2024-27822、CVE-2023-41064、CVE-2024-23222 的 Apple 组件归属 | 已按"不写入表"处理 | 17.3、附录 B 行 100 |
| D-36 | `Kernel Cache File`/`Mach IPC`/单一名 `IOKit`/`GPU Driver` 单数是否出现在 macOS 公告页 | 组件名引用须先补一手 | 17.3、20.5 |
| D-37 | 旧版 iOS Security Guide PDF 的现行官方 URL | 历史口径（iOS trustcache 表述）无法直引 | 8.1、8.3 |
| D-38 | 本地镜像节选导致：`kmem_allocate()` 函数体、`ipc_mqueue.c`、mach_port API 手册级描述需引上游完整文件 | 取证限制，非事实缺失 | 4.6、5.2、18.3 |
| D-39 | 源码结论对应的 xnu 发布版本号（xnu-12xxx 标签）未逐一锚定 | 行号引用必须带版本口径 | 2.3、20.5、附录 B 表头 |

**结尾声明：若读者取到官方原文，应以其为准修订本表，而不是沿用本报告的封顶判定。** 本表的任何一格被官方材料填上，都意味着正文对应章的判定值需要改写；反过来，若官方材料否认某机制，也仅是否认该条陈述的适用范围，不改变"本次核查未取到一手依据"这一方法结论。

## 附录E 误解校正汇总

合并两份简报的"被推翻的预期说法"（前半 10 条 + 后半 22 条），去重后 30 条。凡"常见说法"列中的表述出现在外部材料里，可据本表直接判定其是否需要修订；"依据层级"列给出正确口径能站到哪一层。

| # | 常见说法 | 正确口径 | 依据层级 | 正文所在章 |
|---|---|---|---|---|
| 1 | "Apple 官方说 XNU 是对 Mach 的大幅改动" | README 原文只称 hybrid kernel（CMU Mach + FreeBSD 组件 + IOKit）；改动幅度是源码/历史观察，须标分析结论 | 一手-源码；三方措辞来自 Wikipedia | 3.1、3.4 |
| 2 | "port right = capability 是 Apple 开发者文档里的说法" | 该表述在开源树内一手存在（源码注释 + 仓库 doc），Apple 面向开发者/支持文档无此表述 | 一手-源码 + 一手-官方doc | 4.2 |
| 3 | "send right 计数、port 名额、msgsend 限额有官方数字" | dead name 与 urefs 源码可证；名额与限额**无任何官方公开文档**，数字一律标"未公开/源码观察" | 一手-源码 + 结论性缺失 | 4.6 |
| 4 | "iOS 内核有 per-page guard 与 SECURITY_* 类编译选项" | 实名是 `ZSECURITY_CONFIG(ZONE_TAGGING/READ_ONLY/SAD_FENG_SHUI/PGZ_OOB_ADJUST)`、`SECURE_KERNEL`、zone chunk 尾 guard page；不存在 "per-page guard" 公开宏 | 一手-源码 | 5.3、5.4 |
| 5 | "AMFI 就是一个用户态守护进程" | 官方定性为 AppleMobileFileIntegrity **kernel extension**；`amfid` 存在但职责无官方文档；两者并存、主体在内核 | 一手-文档 + 一手-源码 | 7.1 |
| 6 | "hardened runtime 适用于 iOS" | Apple 文档自限 "your macOS app"；`CS_RUNTIME` 位出现在 iOS 二进制里不等于 iOS 提供该开关 | 一手-文档（开发者文档） | 8.6 |
| 7 | "library validation 只是 macOS 的东西" | iOS 官方章有明文（平台库或同 team identifier） | 一手-文档 p.114-115 | 8.5、8.6 |
| 8 | "可以列出 iOS 上 Apple 启用的 MAC 策略模块清单" | 开源树内零策略模块；清单只存在于 kernelcache 符号观察/研究文献，须标二级来源 | 一手-源码（框架）+ 公开研究（清单） | 6.6 |
| 9 | "MACF 是独立开源组件" | `mac_policy.h` 自述 TrustedBSD MAC Framework on Darwin，保留 FreeBSD CVS 标签；仅存在于 XNU 树内，Apple 警告其非 KPI（QA1574） | 一手-源码 | 6.1、6.4 |
| 10 | "Apple 公布过某类驱动在 iOS X.Y 移出内核/收紧 IORegistry" | 未获任何官方口径；指南全文检索无 iOS 版本化条目 | 负面结果 | 9.6 |
| 11 | "`mac_` 前缀 API 是给用户态用的" | 内核内部有 mpo_/mac_/MPC_ 三层；用户态无公开 API | 一手-源码 | 6.4 |
| 12 | "send right 的引用计数是官方文档概念" | urefs 只在源码/官方 doc 层可证（`IE_BITS_UREFS`） | 一手-官方doc | 4.1、4.6 |
| 13 | "Apple 文档里的 KASLR" | Apple 官方只称 ASLR（进程级语境）；KASLR 是源码/研究界用语 | 一手-文档负面 + 一手-源码 | 10.1 |
| 14 | "可以用 boot-args（`slide=0`/`kaslider`）关掉内核随机化" | arm64 无该解析；`slide=0` 只见第三方工具文档（macOS 调试语境）；iOS 零售无公开关闭途径 | 一手-源码 + L2 | 10.6 |
| 15 | "KIP 就是内核地址随机化" | APSG 写明 iBoot 用受保护物理区装载内核与 kext，但未把 KIP 与随机化写成同一机制 | 一手-文档 | 10.4 |
| 16 | "返回地址用 PACIA + RETAA" | Apple 走 IB（`pacibsp`/`retab`）；RETAA 是架构另一条合法指令、被非 Apple 生态常用，不可套用 | 一手-文档表 + 一手-源码 | 11.4 |
| 17 | "PAC 只保护代码指针" | 官方表把 isa/super/vtable pointer/Block descriptor 归 DA/DB；源码注释明写内核用 EL1 A key 签堆上指针 | 一手-文档 + 一手-源码 | 11.5、11.6 |
| 18 | "BTI/GCS 是 Apple 公开列名的硅上特性" | 列名清单是 KIP/FPR/SCIP/PAC/PPL/SPTM/MIE；BTI 与 GCS 在 APSG 命中 0 | 一手-文档负面 | 12.1 |
| 19 | "可以用 `hw.optional.arm.FEAT_BTI` 查本机 BTI 支持" | 一手源码里该 capability 位无第二处使用、自测表不含 BTI；ABI 位槽存在 ≠ 已导出 | 一手-源码 | 12.4 |
| 20 | "iOS 上返回地址由 GCS 影子栈保护" | 公开实现中返回地址完整性由 PAC 承担；GCS 无使能实现，只写"无公开证据" | 一手-源码 | 12.6 |
| 21 | "信号由内核直接投递" | 信号是内核内 Mach kobject `ux_handler` 作为 host 级兜底 handler 收到 Mach 异常后翻译的产物 | 一手-源码 | 13.3 |
| 22 | "信号映射完全由 machine 层决定" | arm 上 `machine_exception()` 只处理两类，其余回落 `ux_exception()` 映射表 | 一手-源码 | 13.6 |
| 23 | "异常接收方能拿到 task/thread control port 以便续行" | iOS 上仅开发者模式开启或目标为 corpse 时交付 control port，否则只交付不可移动 read port | 一手-源码（含工单号） | 13.5 |
| 24 | "`EXC_SOFT_SIGNAL` 普通进程也能收到" | 反向通道仅在被 ptrace 时产生 | 一手-源码 | 13.6 |
| 25 | "`get-task-allow` 全称是 `com.apple.security.get-task-allow`" | 源码常量的 entitlement 真名无域名前缀；带前缀写法本次未在任何一手来源确证 | 一手-源码 | 14.2 |
| 26 | "`com.apple.security.cs.debugger` 可在 iOS 上用于调试授权" | 该页 metadata 只声明 macOS（introducedAt 10.7）；"10 小时会话""管理员授权对话框"同为 macOS 语境 | 一手-文档 metadata | 14.4、20.3 |
| 27 | "task_for_pid 只看 root 或只看 entitlement" | 实为 POSIX 凭证检查 + `kern.tfp.policy` + MACF 上行调用三重，并按 CONTROL/NAME/INSPECT/READ 四种 flavor 分级 | 一手-源码 | 14.3 |
| 28 | "在零售 iPhone 上用 `amfi_get_out_of_my_way` 等可放宽代码签名" | 解析被 `#if DEVELOPMENT || DEBUG` 包住；RELEASE 上"not respected by any code signing component on RELEASE builds"，TXM 策略还会再削减 | 一手-源码（注释原文） | 7.4、14.5、19.4 |
| 29 | "iOS 完全没有驱动扩展" | 第三方 kext 确无公开路径；但源码明确建模 iOS 第三方 dext 的 user-client 准入（Team ID + 两个 entitlement）。推翻的是"没有驱动扩展"，不推翻"没有第三方 kext" | 一手-源码 | 15.3、15.4 |
| 30 | "iOS 上可调试的驱动是内核态 kext；Apple 的 kext 政策同样适用 iOS；panic 字段可在 Apple 文档查到；存在'iCloud 私有报告'开关；组件名写作 `GPU Driver`/PAC 绕过类组件名是 `Kernel`" | 分别校正为：可调试的是用户态 DriverKit 驱动进程；第三方 kext/AuxKC/UAKL/kext receipt 表述全部在标题含 "in macOS" 的章节下；panic 条目名与字段表 Apple 未公开列举（`panic-full` 系通行写法、二手来源存在点号分歧）；Apple 逐字开关名只有四条 Share 系列；组件名原文为 **`GPU Drivers`（复数）**，CVE-2025-31201 的组件名是 **`RPAC`** | 一手-文档 + 一手-源码 + 负面结果 | 14.6、15.2、15.4、16.2、16.3、17.3 |

## 附录F 术语口径冲突与系列内分工对照

### 一、八组口径冲突的收口写法

同一事实在不同层级来源间口径不一致时，本篇的做法是**两侧并列呈现 + 给一个不越界的收口句**。以下八条是本篇固定用法，附录 A/B/C 引用冲突处均指回此处。

| # | 冲突点 | 两侧原文口径 | 收口写法 |
|---|---|---|---|
| 1 | DriverKit 平台可用性（正文 prose vs 页面 metadata） | prose："The base DriverKit framework is available in macOS for Apple silicon and Intel-based Mac computers, and in iPadOS for devices with an M-series chip."（未点 iPhone）；metadata：列出 macOS 10.15、Mac Catalyst 16.0、**iOS 16.0**、iPadOS 16.0、DriverKit 19.0 | Apple 文档明确的是 iPad（M 系芯片）+ Mac；iPhone 侧仅 XNU 源码含 iOS 第三方 dext 判定逻辑（`IOUserServer.cpp:4760-4790`、`proc_is_third_party_debuggable_driver()`），面向开发者的可用性表述未点名 iPhone。两侧并列，不单独采信 metadata |
| 2 | KASLR 术语（官方文档 vs 源码） | 官方全文只用 ASLR，语境为进程级；源码注释/日志确有 "KASLR"、正式量名为 `vm_kernel_slide` | 源码与研究界称 KASLR；Apple 官方文档只称 ASLR，未单独为内核地址随机化命名。二者不可并列当作 Apple 官方术语 |
| 3 | 返回地址签名键与指令（Apple IB/RETAB vs ARM 生态 RETAA） | Apple 表 "Function Return Address → IB"，`ptrauth_key_return_address = process_dependent_code`，宏展开 `pacibsp`/`retab`；架构同时定义 `RETAA`/`RETAB`，非 Apple 生态常用 A key | Apple 返回地址走 IB（PACIBSP/RETAB）；RETAA 是 AArch64 的另一条合法指令、被非 Apple 生态常用，不可套用为 Apple 口径 |
| 4 | 组件名 `GPU Drivers`（Apple 复数 vs 惯常单数） | 公告页原文 `GPU Drivers`（CVE-2024-40854）；社区/CVE 库常写 `GPU Driver` | 逐字照录 `GPU Drivers`，并括注"Apple 用复数" |
| 5 | `RPAC` 组件归属（Apple 单列 vs 漏洞库归 Kernel/PAC） | Apple 组件名列为 `RPAC`，Impact 明写 "bypass Pointer Authentication"，Description 为 "removing the vulnerable code"；第三方多归入 `Kernel`/`PAC` | 组件名以 Apple 逐字 `RPAC` 为准；若要表达"属内核/PAC 相关"须另加解释性括注，不得改写 Apple 的组件名列 |
| 6 | `get-task-allow` 前缀（源码常量 vs 沙盒惯用全名） | 源码注释 `/* has get-task-allow entitlement */`；社区常写 `com.apple.security.get-task-allow` | entitlement 真名为 `get-task-allow`（code-signing 位）；带 `com.apple.security.` 前缀的全名本次未在任何一手来源确证，按公开未记载处理 |
| 7 | `panic.full` vs `panic-full`（二手来源内部分歧） | 一二手来源正文写点号 `panic.full`，Apple 讨论区标题与多数样本写连字符 `panic-full`；Apple 一手未列名该条目，两侧皆非官方口径 | 采用通行连字符 `panic-full`，并标注"Apple 未公开列举条目名，二手来源存在点号/连字符拼写分歧" |
| 8 | "私有报告"类措辞（预期说法 vs Apple 逐字开关名） | 预期描述为"iCloud 私有报告/私有 iCloud 分析"；Apple 一手逐字名只有 Share iPhone & Watch Analytics / Share iPad Analytics / Share Mac Analytics / Share With App Developers | 仅使用上述逐字名；"iCloud 私有报告"一类 Apple 未使用，不写入正文（同时登记为缺口与推翻项） |

另有两处**版本口径冲突**需长期保留：Apple Platform Security 需带"落款 August 2026"的版本限定，不可写成"最新版"；XNU 源码结论需带"以 main 分支 tree sha f6217f8… 为准，历史版本行号不同"的锚定说明。指南中 trust cache → platform privileges、"MACs require code signing to enable entitlements"、IOMMU 触发 panic 三段皆为 macOS/Apple silicon 语境，移用到 iOS 时必须改引或标注类比。

### 二、与系列相邻篇目的分工声明

本篇按**机制**组织内核内容，以下相邻篇目各有分工，本篇不重讲其主体：

| 相邻篇目 | 该篇承担什么 | 本篇因此不重讲什么 |
|---|---|---|
| `iOS 沙箱机制、权限提升与内核安全分析报告` | 横向的漏洞类别分布与提权路径分类 | 本篇第 18–19 章只做"层 × 前提"归纳，不重复提权类别清单；沙箱作为 MACF 策略实体只在第 6 章与 19.3 以"清单未公开"的方式出现 |
| `iOS系统内核安全漏洞分析报告` | 具体漏洞案例的内核侧成因与利用面 | 本篇不展开任何单一 CVE 的利用细节；第 17 章只处理公告的组件名与措辞口径 |
| `苹果SPTM、TXM与Exclaves安全架构解析` | SPTM、TXM 与 Exclaves 的主体架构解析 | 本篇只在第 8 章（CSM 承载体选择）、第 11 章（PAC key 由更高特权级托管）、第 15 章（第三方扩展与内核集合的平台归属）交叉引用 SPTM/TXM，不重讲其设计；Exclaves 本篇完全未涉及 |
| `iOS核心架构与系统层级` | XNU 之上的系统分层与组件拓扑 | 本篇第 3 章只处理内核树内的目录职责与构建配置，不重讲框架层拓扑 |
| `iOS核心底层技术支撑` | 底层技术栈的通用支撑能力 | 本篇不重讲编译/链接/运行时基础设施，只讲与强制点相关的构建开关 |
| `iOS内存安全基础架构` | 内存安全的总体基础（含用户态分配器与检查） | 本篇第 5 章只讲内核 zone/kalloc 与其加固开关，不重讲用户态内存安全 |
| `iOS访问控制与应用层交互限制` | DAC/授权/应用层交互边界与权限提示 | 本篇第 6 章只讲 MACF 框架的位置、注册与覆盖点；具体权限 UX 与授权流程不在本篇 |
| `iOS失窃设备保护`（第 77 期） | 失窃设备保护的模式设计、状态迁移与用户处置 | 本篇第 20 章只交叉引用其与内核机制的耦合关系一句话（锁定模式状态经 `csm_check_lockdown_mode()` 同步给 CSM，属源码观察），不重复第 77 期内容 |
| `iOS核心保护作用`（第 78 期） | 按**威胁组织**组织的保护作用映射 | 本篇按**机制**组织内核安全；两篇构成互补：该篇回答"某组织会被哪层挡住"，本篇回答"某层在什么前提下才生效" |

---
**联系方式**：Telegram: https://t.me/ios_one001
