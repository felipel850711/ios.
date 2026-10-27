@@TITLE@@iOS 限制特定内存页面执行指令的机制分析
@@SUBTITLE@@Which Pages May Execute Instructions, Who Decides, And What Apple Actually Says About It — Default Non-Executable Data, The Two Ways A Page Ever Becomes Executable (Signed File-Backed Mapping Vs Anonymous MAP_JIT), Write/Execute Exclusion And Its Documented Toggle, JIT As A Named Exception With Named Grantees, Per-Page On-Demand Code-Signature Validation, Which Configuration Choices Narrow The Coverage, Platform Asymmetry Across iOS/iPadOS/macOS/watchOS/visionOS/Catalyst And Intel Vs Apple Silicon, What A Violation Leaves Behind In Crash Reports, And What Page-Level Evidence Travels Off The Device Through Diagnostic Uploads
@@INFO@@报告日期|2026年10月22日
@@INFO@@分析对象|页面可执行性的默认状态（数据页不可执行）·硬件与内核侧执行权限来源（Execute Never、MMU 配置限制、per-thread 执行权限撤销）·谁来维持不可执行（页表保护、SPTM/TXM 与 PPL 口径、协处理器侧对称表述）·可执行页的两条取得路径（文件映射与签名页·匿名内存与受控例外）·写执行互斥与运行时切换（逐字官方 API 说明与设计意图段）·即时编译（JIT）例外的授予与撤销·锁定模式对网页技术侧的口径·页级按需校验（载入时代码签名检查、按页哈希验证、跨平台对照）·削弱面清单（构建选项与授权属性层面）·平台不对称·谁能改变限制状态·可观测性（崩溃报告字段与 MetricKit）·上传方向（诊断数据离开设备的官方通道）·已知限定与缺口
@@INFO@@视角定位|防御与研究教育视角：回答"iOS 上'某段内存能不能执行指令'这个判断由谁做出、依据哪些成文规则、一次违规会留下什么痕迹、以及哪些公开的构建选项与授权属性会把覆盖面收窄"，并给出可用于加固评审、构建审计与崩溃取证的判定链、量化基线与自查清单。本篇不包含任何绕过执行限制、伪造签名页或制造可执行内存的操作方法，不含利用代码，不含针对具体目标或第三方设备的攻击步骤；涉及"何种配置会降低覆盖"的部分只描述 Apple 公开的构建选项、授权属性与其文档所述的后果，用途是配置审计与整改，不给出执行步骤。
@@INFO@@分类|内存安全 · 执行防护 · 代码签名与按需页校验 · 即时编译授权 · 崩溃可观测性与诊断上传
@@INFO@@难度|中级偏高级（需了解虚拟内存与页表基本概念、 mmap/mprotect 语义、代码签名与 entitlement 基础、 Mach-O 段与加载流程、iOS 应用扩展与 Web 引擎的运行模型、崩溃报告字段含义）
@@INFO@@联系方式|Telegram: https://t.me/one00190

@@TOC@@目录

# 0. 概述、口径与阅读方式

## 0.1 本篇只回答一个问题

用户请求的原话是「限制特定内存页面执行指令」。这个表述在 iOS 上对应的不是一道开关，而是一串彼此独立的判断：

1. **默认状态下，一页内存可执行吗？** 依据哪一句官方文本作出回答？
2. **"可执行"这个属性是从哪里来的？** 是页表位、寄存器、还是签名？
3. **谁能把它改成可执行？** 改的代价是什么（系统调用？页表刷新？）？
4. **系统允许存在"既可写又可执行"的页吗？** 如果允许，谁被允许、以什么前提、能拿到几个？
5. **一次违规会留下什么？** 崩溃报告里能看见什么、这些证据会不会随诊断数据离开设备？

本篇按这五个问题展开，全部结论只落在 Apple 一手文本（《Apple 平台安全保护》手册页、developer.apple.com 文档、技术说明 TN、security.apple.com 博客、support.apple.com 支持文章）与 Apple 官方开源仓库（`apple-oss-distributions`）的源码注释与实现之上。凡在这两处找不到逐字句子的，一律标注为「未核实」并写明尝试过什么检索。

## 0.2 四个层次必须先分开

「执行限制」这个词在公开文本里同时指四种不同层面的东西，混起来谈必然得出错误结论。本篇自始至终按下列四层切分，任何一句引文都要能归到某一层：

| 层次 | 回答的问题 | 本篇的落点 | 官方文本给的名称 |
|---|---|---|---|
| ① 架构能力 | CPU 能不能把一页标成不可执行 | 第 2 章 | ARM **Execute Never (XN)** 功能 |
| ② SoC 原语 | 撤销执行权限要花多少代价 | 第 3 章 | **快速权限访问限制（APRR 寄存器）** |
| ③ 问责边界 | 谁有权改页表里的执行位 | 第 4 章 | **KIP** / **PPL** / **SPTM + TXM** / **SCIP** |
| ④ 准入边界 | 谁能拿到"既可写又可执行"的内存 | 第 5–8 章 | **动态代码签名授权** / `MAP_JIT` / `com.apple.security.cs.allow-jit` |

这四点区分是本篇的骨架。①只说"能不能标"，②只说"改起来便不便宜"，③只说"谁能改"，④只说"谁能拿到例外"。流传口径常把④当成全部（"iOS 有 DEP 所以不能执行注入代码"），或把③当成①（"页表保护就是硬件特性"），两者都会让人低估某一层的缺口。

## 0.3 术语纪律：Apple 用了什么词，没用什么词

- **「Execute Never (XN)」是手册里唯一直接描述"页被标记为不可执行"的正式术语**（secid `15bfe098e`，2024 年 12 月 19 日）。PXN、UXN、NX bit、DEP、"数据执行保护"这些同族词在《Apple 平台安全保护》中文手册全库（本地镜像 176 页）**命中 0**，本篇引用时只把它们当作叙述性同义词，绝不用作引文。
- **「需求页 / 强制页」（demand page / mandatory page）等中文术语：手册全库命中 0。** Apple 一手文本对页校验只说「代码签名检查会在载入时进行」（secid `f49cad4db`）与「验证每个 x86_64 页的代码哈希值」（secid `ebb113be1`）。
- **「弹性签名」**对应源码里的 `MAP_RESILIENT_CODESIGN`（A- 级）；手册未给中文术语，本篇首次出现时同时给出两个形态。
- **「代码签名监控器」**对应源码 `csm_*` / `code_signing.h`（A- 级）；手册侧的对应表述是「Apple 专有的动态代码签名授权」，两者不是同一个词，本篇不互换。
- 内核里 `printf("page marked to be NX, not letting it be mapped EXEC\n")`（`vm_fault.c:2838-2871`，A- 级）是**代码字符串里的 NX**，不能据此说"手册用了 NX 这个术语"。

## 0.4 证据档位与标注方式

| 档位 | 含义 | 本篇用法 |
|---|---|---|
| **A** | Apple 一手官方成文文本（手册页 / developer 文档 / TN / 官方博客 / 支持文章），逐字引用并给 secid 或 URL + 页面标注的发布或修订日期 | 所有"Apple 说……"的句子 |
| **A-** | Apple 官方开源仓库的一手源码（XNU、libpthread、dyld 等），给 `文件:行号` + 版本标签 | 所有"当前实现如此"的句子 |
| **B** | 二手或第三方（编译器上游文档、研究者博客、PoC） | 只作背景，绝不写成 Apple 的口径 |
| **未核实** | 检索过、没有一手来源 | 明写检索词与命中数，不给数字或结论 |

三条本篇内贯穿的纪律：

1. **A- 级源码不构成对外承诺。** 源码注释、策略宏、测试 plist 里的键名只能说明"当前实现这么写"，不能写成"Apple 公开规定"。第 7 章 iOS 侧 JIT 授权键就是典型：`dynamic-codesigning` 只出现在 libpthread 仓库的 iOS 测试授权文件里，公开 developer 文档页不存在。
2. **"官方没写"不等于"官方否认"。** 手册对 iOS 页哈希粒度、W^X 违规的具体信号行为、iOS 第三方 JIT 渠道都没有成文句子，本篇按缺口登记，不填推测数字。
3. **跨平台文本不得互推。** TN3126 自己写明「The examples in this technote are taken from macOS, but these concepts apply to all Apple platforms」，这是 Apple 自己的外许可；而 man page 里 iOS/watchOS/tvOS 与 macOS 对 W+X 的处理差异（第 5 章）恰恰证明**不能**默认各平台一致。外推必须有 Apple 明文，或由本篇标注为推论并给出依据。

## 0.5 三个上传方向（U1 / U2 / U3）

本篇与系列前几篇共用同一组上传方向记号，用于回答"设备内部掌握的执行限制证据，哪些会离开设备"：

- **U1 服务端通道**：诊断与崩溃信息经系统分析服务上送 Apple 服务端的那条路。
- **U2 iCloud 通道**：随 iCloud 备份 / 同步离开设备的内容。
- **U3 开发者通道**：开发者经 Xcode、Organizer、TestFlight 反馈、MetricKit 等途径获得的自己 App 的诊断数据。

第 12–13 章处理这三条通道各自携带什么。需要在一开始就说清的一条边界是：**"页不可执行"这一事实本身是设备本地的执行判定，它产生的可观测物是信号、异常与内核日志，而不是任何一条主动外发的记录**。外发的是崩溃报告与诊断指标，其内容范围由 Apple 公开的数据类别说明界定，本篇不把它写成"上传了内存内容"这种没有出处的断言。

## 0.6 本篇不包含什么

- 不包含任何绕过执行限制、伪造签名页、制造可写可执行内存的操作方法；不含利用代码；不含针对具体目标或第三方设备的攻击步骤。
- 涉及"何种配置会把覆盖面收窄"的部分（第 9 章）只描述 Apple 公开的构建选项、授权属性名称与其文档所述后果，用途是配置审计与整改，不给执行步骤。
- 第 2–8 章出现的 API 名（`mmap`、`mprotect`、`pthread_jit_write_protect_np` 等）是**为了说明官方对权限模型的描述**，其语义一律转述自 man page / 官方文档原文，不作为使用教程。

## 0.7 章节地图与阅读路径

| 想知道 | 读哪几章 |
|---|---|
| 一页默认能不能执行、依据哪句话 | 第 2 章 |
| 执行权限的硬件与内核来源、撤销代价 | 第 3 章 |
| 谁能改页表权限（KIP/PPL/SPTM+TXM/SCIP 分工） | 第 4 章、第 10 章的对照表 |
| 可执行页到底怎么来的（文件映射 vs 匿名内存） | 第 5 章 |
| 写与执行怎么互斥、切换接口与设计意图 | 第 6 章 |
| JIT 作为例外：谁被允许、如何授予与撤销、锁定模式 | 第 7 章 |
| 载入时才做的按页签名校验与违规后果 | 第 8 章 |
| 自己项目里哪些设置会让保护变弱 | 第 9 章 + 第 14 章自查清单 |
| iOS 与 macOS / watchOS / visionOS / Catalyst / 芯片代际的差异 | 第 10 章 |
| 崩溃报告里能看到什么、上传走哪条通道 | 第 12–13 章 |
| 评审时逐条打勾 | 第 14 章（七道判定 + 量化基线 + 22 条清单） |
| 哪些流行说法站不住、哪些必须加限定语 | 第 15 章 |
| 引用出处、术语、速查 | 附录 A / B / C |

## 0.8 与系列其他篇的接缝

执行限制不是孤立的一道防线，本篇只写"能不能执行"，相邻问题各有专篇：

- **位置层**（地址该在哪）见《iOS 地址空间布局随机化与利用难度提升机制分析》。本篇第 5 章只引「系统为可执行且可写入的内存页分配了随机地址」这一句，随机化的射程与熵的来源不在本篇展开。
- **指向层**（指针指向是否合法）见《iOS 特定指针认证与指针篡改风险降低机制分析》。PAC 处理"这个返回地址/函数指针有没有被改过"，本篇处理"改成功之后能不能真的执行"，两者是正交的两道门槛。
- **签名组件与授权属性**（谁给签名、属性怎么声明）见《代码签名与授权属性》《代码签名与完整性执行体系组件》两篇。本篇第 8 章只取"按页哈希封入 Code Directory、载入时校验"这一条与执行直接相关的机制。
- **能力与权限**、**围栏与访问控制**两篇处理进程能碰什么资源；执行限制属于"进程能跑什么代码"，是另一个维度。

## 0.9 一句话前置结论

在 iOS 上，"某页内存不能执行指令"不是一条由单个硬件位保证的规则，而是**四层判断叠加后的结果**：架构提供把页标为不可执行的能力（XN），SoC 提供低成本撤销执行权限的原语（APRR），页表权限的修改权被收给专门的可信组件（KIP/PPL/SPTM+TXM），而"既可写又可执行"这一例外被压缩到**一个地址空间一个区域、需要专门的动态代码签名授权、且只给点名过的用途**（手册逐字举的例子是 Safari 的 JavaScript JIT 编译器）。**这四层里任何一层的公开文本都没有声称"不可能执行攻击者写入的指令"**——Apple 的手册对执行限制的一手表述止于"提供保护""减少攻击面""只有处于严格的控制条件下才能……"这一强度；把这一机制读成绝对边界，是本篇第 15 章列出的第一条流传口径错误。


# 1. 总览：一段内存要执行指令，中间隔着什么

## 1.1 一次"执行未授权指令"要跨过的四道门槛

把"攻击者想让设备执行一段他自己放进内存的指令"这件事拆到底，需要同时满足四个条件，而 iOS 的限制正是逐条卡住这四个条件：

| 门槛 | 需要成立的条件 | 卡住它的机制 | 官方一手表述（档位） |
|---|---|---|---|
| **T1 页可执行** | 目标页在执行时的权限里含 EXECUTE | XN 把数据页标为不可执行（A）；APRR 让撤销执行权限不需要系统调用与页表刷新（A） | 「将内存页标记为不可执行」；「可从内存移除执行权限，无需通过系统调用和页表寻访或刷新」 |
| **T2 权限不可自行改** | 攻击者控制的路径能改页表执行位 | 页表权限修改权收给 PPL（A15/M2 前）或 SPTM+TXM（之后）；KIP 锁定启用配置（A） | 「只有 PPL 才能修改包含用户代码和页表的受保护页面」；「用于启用 KIP 的硬件会在启动过程完成后锁定」 |
| **T3 内容是签名的** | 该页内容能通过按页哈希校验 | 载入时的代码签名检查；无效页被拒绝映射或杀进程（A + A-） | 「所有可执行内存页的代码签名检查会在载入时进行」；「page marked to be NX, not letting it be mapped EXEC」 |
| **T4 例外拿得到** | 存在一块既可写又可执行的内存 | `MAP_JIT` + 「Apple 专有的动态代码签名授权」+ 每地址空间单区域 + 随机地址（A + A-） | 「内核会检查 Apple 专有的动态代码签名授权是否存在」；「一个地址空间只能有一个 JIT 区域」 |

**本篇的写法约定**：四道门槛之后分别对应第 2–3 章（T1）、第 4 章（T2）、第 8 章（T3）、第 5–7 章（T4）。第 9 章列出哪些公开设置会让 T3/T4 变弱，第 10 章给出平台与代际差异，第 14 章把四道门槛变成可执行的评审判定链。

## 1.2 一张"谁能做什么"的对照表

| 动作 | 常规 App | 有授权的例外进程（如 Safari 的 JIT） | 系统 / 平台库 | macOS（非硬化运行时） |
|---|---|---|---|---|
| 请求一个只读可执行的文件映射页 | 可以（须签名） | 可以 | 可以 | 可以 |
| 请求同时带 `PROT_WRITE` 与 `PROT_EXEC` 的匿名页 | 手册：只有"处于严格的控制条件下"且具备动态代码签名授权 | 手册逐字点名 Safari 的 JavaScript JIT | 依 entitlement | man page：不带 `MAP_JIT` 会失败（macOS 硬化运行时语境） |
| 在 iOS 上请求 W+X 而不带 `MAP_JIT` | 拿到「可写但不可执行」的映射（man page，A-） | 不适用 | 不适用 | macOS 上是 `MAP_FAILED`（A-） |
| 逐次切换"这线程能写还是能执行" | iOS 上面向第三方的切换 API 不存在（未核实到公开文档页） | WebKit/JavaScriptCore 内部机制，Apple 未公开说明 | — | `pthread_jit_write_protect_np` 可用（A- / A） |
| 让未签名的页参与执行 | 载入时校验拒绝或杀进程（A-） | JIT 区域走签名校验旁路（A-：`cs_bypass`） | 依策略 | 硬化运行时才默认按页校验（A：TN3126） |

表里两处刻意保留了"不适用/未核实"，因为它们是本篇最重要的射程边界：**iOS 上不存在面向第三方开发者的 JIT 切换 API 文档页**，而 **man page 明确把 iOS/watchOS/tvOS 与 macOS 的 W+X 失败行为写成两种不同结果**。

## 1.3 机制总表（含档位与章节落点）

| # | 机制 | 层面 | 一手来源 | 档位 | 章节 |
|---|---|---|---|---|---|
| 1 | ARM Execute Never (XN)：把页标为不可执行 | ① 架构能力 | 手册 `15bfe098e`（2024-12-19） | A | 2 |
| 2 | 强制性代码签名：所有可执行代码须 Apple 证书签名 | ④ 准入 | 手册 `7c917bf14`（2024-12-19） | A | 2、5 |
| 3 | KIP：MMU 配置限制受保护区外的权限代码映射；启动后锁定 | ③ 问责 | 手册 `8b776536b`（2026-01-28） | A | 3、4 |
| 4 | APRR 寄存器：per-thread 撤销执行权限，不需系统调用/页表刷新 | ② SoC 原语 | 手册 `8b776536b`（2026-01-28）、`87716a080`（2026-01-28） | A | 3、6 |
| 5 | PPL：只有它能改含用户代码与页表的受保护页 | ③ 问责 | 手册 `8b776536b` | A | 4 |
| 6 | SPTM + TXM：A15/M2 起取代 PPL | ③ 问责 | 手册 `8b776536b` | A | 4、10 |
| 7 | SCIP：协处理器"区外不可执行映射 / 区内不可写映射" | ③ 问责（外围） | 手册 `8b776536b` | A | 4 |
| 8 | 文件映射：`maxprot` 默认含 EXECUTE、内容受签名约束 | 实现 | `kern_mman.c:620-627`（`xnu-12377.121.6`） | A- | 5 |
| 9 | `MAP_JIT` 的准入与组合限制（必须匿名、禁共享、禁固定地址等） | ④ 准入 | `kern_mman.c:403-428` | A- | 5、7 |
| 10 | CSM 的 JIT 准入判定与拒绝打印 | ④ 准入 | `kern_mman.c:479-482`、`code_signing.h:706-730` | A- | 7 |
| 11 | 写执行互斥的 W^X 闸门（失败或降级两种策略） | 实现 | `vm_map.c:3180-3217`、`vm_map_internal.h:923-941` | A- | 6 |
| 12 | per-thread 写保护切换的三个对外函数与白名单收紧 | ② 接口 | libpthread `man/pthread_jit_write_protect_np.3`（2020-05-01） | A- | 6 |
| 13 | 载入时按页哈希校验（延迟校验） | T3 | TN3126（修订记录 2024-02-06）、手册 `f49cad4db` | A | 8 |
| 14 | NX 页不得映射为 EXEC / 污染页判定 / 无效页杀进程 | T3 后果 | `vm_fault.c:2838-2891`、`kern_cs.c:248-312` | A- | 8、12 |
| 15 | Rosetta 2 的转译期按页哈希验证（跨平台对照） | T3 | 手册 `ebb113be1`（2021-02-18） | A | 8 |
| 16 | 锁定模式停用 JIT 等复杂网页技术 | ④ 收紧 | 《守护你的个人安全》`ipsd5baf79d0`（2025-10）、支持文章 105120（2026-09-18） | A | 7 |
| 17 | 硬化运行时的"禁用可执行内存保护"类授权会削弱 T3/T4 | 削弱面 | TN3126 逐字（Intel 与 Apple 芯片效果不同） | A | 9 |
| 18 | 崩溃与诊断的可观测性与上传 | 观测 | 第 12–13 章来源登记 | A / A- / 未核实混排 | 12、13 |

## 1.4 本篇能给什么、不能给什么

**能给出**：四道门槛各自的一手文本出处；哪些说法是 Apple 明确写下的、哪些只是实现细节、哪些根本没有任何一手句子；一份可逐条打勾的配置审计清单（第 14 章）；违规在设备本地留下什么痕迹（第 12 章）；以及三条上传通道各自带走什么层级的证据（第 13 章）。

**不能给出**：这一限制的量化强度（Apple 不公布 XN 位宽、APRR 寄存器名与指令序列、iOS 页哈希粒度、第三方准入判据）；任何"绕过之后会怎样"的操作性描述；对具体设备或具体 App 的结论——本篇全部结论只到"机制与射程"这一层，个案判断需要第 14 章的判定链配合被测对象自身的构建产物。

---

## 1.5 本章要点回顾

1. 「限制内存页执行指令」在 iOS 上是**四道门槛的叠加**（页可执行 / 权限不可自改 / 内容已签名 / 例外拿得到），不是单一硬件位。
2. 讨论时必须分四层：**架构能力（XN）· SoC 原语（APRR）· 问责边界（KIP/PPL/SPTM+TXM/SCIP）· 准入边界（动态代码签名授权/MAP_JIT）**。
3. Apple 的一手强度表述止于「提供进一步保护」「减少攻击面」「只有处于严格的控制条件下才能……」，**没有任何一处写成绝对不可能**。
4. iOS 与 macOS 在 W+X 请求的失败形态、JIT 切换 API 的可用性、按页校验的默认开启条件上**三处明确不一致**，跨平台结论必须有 Apple 明文。


# 2. 默认状态：数据页为什么不能执行指令

## 2.1 唯一直接说"页被标为不可执行"的那句话

Apple 中文手册里，直接描述"内存页被标记为不可执行"的句子只有**一处**，出现在《iOS、iPadOS 和 visionOS 中的运行时进程安全性》页（secid `15bfe098e`，页面标注发布日期 2024 年 12 月 19 日，A 级）：

> iOS 和 iPadOS 使用 ARM 的 Execute Never (XN) 功能提供进一步的保护，该功能会将内存页标记为不可执行。只有处于严格的控制条件下，App 才能使用标记为可写入和可执行的内存页：内核会检查 Apple 专有的动态代码签名授权是否存在。即使如此，也只有单个 `mmap` 调用能用于请求一个可执行且可写入的内存页，系统为可执行且可写入的内存页分配了随机地址。Safari 浏览器对其 JavaScript 即时 (JIT) 编译器使用了此功能。

这一句在本篇的地位特殊，因为它同时说了四件事，而这四件事分属四个不同层次，正文与附录都按这个拆法引用：

1. **机制与归属**：能力来自 ARM 架构的 Execute Never (XN)，iOS 与 iPadOS「使用」它 —— 主语是操作系统使用架构能力，不是 Apple 自研的一道闸。
2. **默认结论**：该功能「会将内存页标记为不可执行」—— 这是本篇"默认不可执行"这一命题的唯一一手依据。
3. **例外条件（三连）**：「只有处于严格的控制条件下」+「内核会检查 Apple 专有的动态代码签名授权是否存在」+「只有单个 `mmap` 调用能用于请求」—— 例外不是"有 entitlement 就行"，而是**授权存在 + 调用形态受限 + 数量受限**。
4. **点名的使用者**：Safari 浏览器的 JavaScript JIT 编译器。这是手册里唯一被逐字点名使用这一例外的 Apple 组件。

**引用纪律**：第 3 项常被压缩成"iOS 允许 JIT"，第 4 项常被扩大成"第三方 App 也能申请"。手册两句都不支持这种读法；第 5–7 章处理例外，第 15 章把这压缩列为流传口径。

## 2.2 这句话的前提句：可执行代码一律要签名

同一句「不可执行」之所以能成立，靠的是强制性代码签名这个前提。《iOS、iPadOS、Apple tvOS、visionOS 和 watchOS 中的 App 代码签名过程》页（secid `7c917bf14`，2024 年 12 月 19 日，A 级）：

> 内核启动后，它将控制哪些用户进程和 App 可以运行。为帮助确保所有 App 均来自批准的已知来源并且未被篡改，这些操作系统要求所有可执行代码均使用 Apple 颁发的证书进行签名。……强制性代码签名将信任链的概念从操作系统扩展至 App，并帮助防止第三方 App 载入未签名的代码资源或使用经 App 自身修改代码。

《iOS、iPadOS 和 visionOS 中的 App 安全性介绍》页（secid `f49cad4db`，2024 年 12 月 19 日，A 级）把这条前提落到运行时：

> 运行时，所有可执行内存页的代码签名检查会在载入时进行，以帮助确保 App 自安装或上次更新之后未被修改过。

于是"默认不可执行"的真实含义不是"数据页硬件上不能执行"，而是：**可执行性是一份需要签名授予的资格，而签名是按页封进代码目录的**。这个区别在第 8 章会展开成"按需校验"的整章；本章只需要立住一点：手册把 XN 与强制性签名写在同一组页面上，两者互为条件。

## 2.3 术语射程：手册用了什么、没用什么

下表是本篇在本地手册镜像（同一份 zh-cn 手册页集合，含全部页面的正文抓取件）上跑出的检索记录。**"命中 0" 只表示这套检索词在该文本集合里没出现，不表示 Apple 否认该概念**。

| 检索词 | 命中页面数 | 说明 |
|---|---|---|
| `Execute Never` | **1** | 仅 secid `15bfe098e`。手册里没有第二处展开解释它的页面 |
| `不可执行` | **1** | 同上，唯一一处 |
| `\bXN\b`（词边界） | **1** | 同上；该页写作「Execute Never (XN)」 |
| `执行权限` | **1** | 仅 secid `8b776536b`（APRR 那句「从内存移除执行权限」） |
| `PXN` | **0** | 手册未使用 ARM 的 PXN/UXN 细分术语 |
| `UXN` | **0** | 同上 |
| `数据执行保护` | **0** | 手册不使用这个中文译名 |
| `DEP` | 1 页，但为 `ADEP`（Apple Developer Enterprise Program）子串 | **不构成"手册提到 DEP"** |
| `需求页` / `强制页` | **0 / 0** | 页校验相关中文术语不存在（对照第 8 章的实际表述「检查会在载入时进行」） |
| `即时` | **4** | `15bfe098e`、`8b776536b`、`ebb113be1`（Rosetta 转译，另一用法）、`c46f3562c`（"即时远程擦除"，与 JIT 无关） |
| `JIT` | 2 个手册页（`15bfe098e`、`ebb113be1`） | 其余命中来自 developer 侧缓存文件，不是手册页 |
| `硬化运行时` / `强化运行时` / `加固运行时` | **0 / 0 / 0** | 手册中文页没有这一术语的稳定译名；`Hardened` 仅出现在 developer 文档缓存件里（第 10 章处理这一不对称） |
| `库校验` | **0** | 手册不用该中文词描述 library validation（相关内容以别的措辞出现，见第 9 章的削弱面登记） |

三条推论：

1. 手册对"页不可执行"的成文强度**只有一句话**，且这句话与例外条款挤在同一段里。写任何超出这句话的表述（例如"所有数据页一律不可执行""任何写入的代码都无法执行"）都是**加词**。
2. 手册**从不使用 ARM 架构的细分术语**（PXN/UXN），也不使用业界通用名 DEP。因此本篇的规范措辞是「将内存页标记为不可执行（Execute Never, XN）」，其余为叙述性同义词。
3. macOS 侧的"硬化运行时"这一整套概念，在中文手册里**没有对应词条**；它的所有一手成文表述都在 developer 文档与技术说明里（第 5、9、10 章）。这直接导致一个引用陷阱：**在手册里找不到"iOS 有 Hardened Runtime"，不是因为 iOS 没有等价机制，而是因为手册根本不使用这个术语。**

## 2.4 「标记」与「撤销」是两件事

手册在同一页集合里对执行权限给了两种不同性质的表述，必须分开读：

| 形态 | 说的是什么 | 一手出处 | 代价 |
|---|---|---|---|
| **标记为不可执行** | 页的静态属性 | `15bfe098e`：「该功能会将内存页标记为不可执行」（A） | 建映射时确定 |
| **从内存移除执行权限** | 运行时的动态撤销 | `8b776536b`：「支持的操作系统可从内存移除执行权限，无需通过系统调用和页表寻访或刷新」（A） | 手册明确说**不需要**系统调用与页表刷新 |

第二行是 Apple SoC 的 APRR 原语带来的能力（第 3 章展开）。两者的差别对评审有实际意义：**一道只靠"标记"的保护，改起来要走系统调用与页表操作，因此天然有可观测的调用点；一道"无需系统调用即可撤销"的保护，把切换压到了线程态，代价低到可以在每次 JIT 写入前后各做一次**。第 6 章的写执行互斥正是建立在这个不对称之上。

## 2.5 违反之后，最低限度能说什么

关于"执行了一页不该执行的指令会发生什么"，能引的一手材料只有两条，且强度都很低：

- A- 级，《mprotect(2)`》手册页（`xnu/bsd/man/man2/mprotect.2`，页面日期 October 16, 2008）：

  > `When a program violates the protections of a page, it gets a SIGBUS or SIGSEGV signal.`

  同页另一句直接界定了粒度：`Not all implementations will guarantee protection on a page basis but Mac OS X's current implementation does.`（页级保证是"当前实现"的表述，不是跨平台承诺）

- A- 级，XNU 在页错误路径上的拒绝语句（`osfmk/vm/vm_fault.c:2838-2871`，tag `xnu-12377.121.6`）：注释逐字 `executable pages because that would provide a way to inject unsigned code`，代码在满足条件时 `printf("page marked to be NX, not letting it be mapped EXEC\n")` 并返回 `KERN_CODESIGN_ERROR`。该打印受调试位 `cs_debug` 控制，**默认不一定出现**。

因此本篇的规范表述是：**"执行受保护页"在用户态表现为 `SIGBUS` 或 `SIGSEGV`（man page 措辞），在内核页错误路径上表现为拒绝映射并返回 `KERN_CODESIGN_ERROR`（源码措辞）**。不能写成：

- 「页被撤销后立刻 SIGKILL」——`SIGKILL` 属于第 8 章的**签名校验失败**处置（`kern_cs.c` 的 `CS_KILL` 分支），与"执行了不可执行页"不是同一条路径；
- 「内核日志里一定能看到 NX 拒绝」——该打印受调试位控制；
- 「崩溃报告会写明是不可执行页违规」——第 12 章按公开字段逐项核对后只能说部分可观测。

## 2.6 手册没有说的四件事

以下四项在本篇全部记为**未核实**，第 15 章汇总时保持缺口状态：

1. **哪些页被标为不可执行**。手册只说 XN「会将内存页标记为不可执行」，没有给出"堆/栈/数据段一律 NX"这类目录式说明。本篇不从实现反推清单。
2. **XN 与内核态的对应关系**（内核自身页的 NX 策略）。手册未给表述；源码侧可见的是页表权限的问责边界（第 4 章），不是位宽或策略表。
3. **iOS 是否"始终"启用 XN、能否关闭**。手册没有"可关闭"的说法，也没有"不可关闭"的说法。**「不能关闭」这一断言在一手文本里找不到逐字依据**，本篇不使用。
4. **XN 的执行粒度**（页大小、是否随代际变化）。手册与 TN 均未给 iOS 数字（TN3126 只有 macOS 示例 `Page size=4096`；第 8 章与第 15 章各登记一次）。

## 2.7 关于"默认不可执行"的六条常见误读

| 误读 | 一手文本实际怎么说 | 档位 |
|---|---|---|
| 「iOS 有 DEP，所以内存里的代码一律不能执行」 | 手册不使用 DEP 一词；且同一句紧接着给出可写可执行的例外条件 | A（引文自 `15bfe098e`） |
| 「只要没有 entitlement 就不可能有可执行的新代码」 | 可执行页有两条取得路径，文件映射路径不需要任何例外（第 5 章） | A + A- |
| 「Safari 能用 JIT，所以第三方 Web 引擎也能」 | 手册逐字点名的是「Safari 浏览器对其 JavaScript 即时 (JIT) 编译器使用了此功能」，未提到第三方；iOS 侧公开文档未给出第三方 JIT 申请渠道（第 7 章零命中登记） | A + 未核实 |
| 「页被标 NX 之后再无改回的可能」 | 手册对 APRR 的描述正是"移除执行权限"这一可逆操作（第 3、6 章） | A |
| 「违规一定杀进程」 | `SIGKILL` 来自签名校验失败分支，NX 拒绝的返回码是 `KERN_CODESIGN_ERROR`；两条路径不同 | A- |
| 「iOS 与 macOS 在这一点上等价」 | man page 明写两个平台对 W+X 请求的处理结果不同（第 5 章）；TN3126 明写"禁用可执行内存保护"这一授权在 Intel 与 Apple 芯片上效果不同（第 9 章） | A- + A |

## 2.8 本章要点

1. 「默认不可执行」的一手依据是**一段话里的一句话**：`15bfe098e`（2024-12-19）「该功能会将内存页标记为不可执行」，且这句话与例外条款同段。
2. 这句话成立的前提是强制性代码签名（`7c917bf14`）与"所有可执行内存页的检查会在载入时进行"（`f49cad4db`）。
3. 手册不使用 PXN/UXN/DEP/"需求页" 等术语；引用时必须区分"手册用词"与"叙述性同义词"。
4. 「标记为不可执行」与「移除执行权限」是两种不同性质的操作，后者的代价被手册明确写成"无需系统调用和页表寻访或刷新"。
5. 违规的可观测结果只有两条一手表述（`SIGBUS`/`SIGSEGV` 与 `KERN_CODESIGN_ERROR`），**"立刻 SIGKILL" 与"日志必然可见"都是加词**。


# 3. 执行权限从哪来：硬件与内核侧的成文口径

## 3.1 两个承担描述的手册页

"一页内存凭什么变成可执行"这件事，手册侧只有两页成文：

| 页 | secid | 发布日期 | 本篇取用的内容 |
|---|---|---|---|
| 《操作系统完整性》 | `8b776536b` | 2026 年 1 月 28 日 | KIP 的 MMU 配置句、APRR 句、PPL 句、SPTM+TXM 句、SCIP 句、平台对照表注释 2 |
| 《Apple SoC 安全性》 | `87716a080` | 2026 年 1 月 28 日 | 与上一致地把 APRR 的收益描述为限制网页 JIT；给出 SoC 侧的机制归属 |

两页之外，「快速权限访问限制」这个中文术语在手册全库还命中一页《Apple 平台安全保护介绍》（`cd5016d31`，属导览性表述），「页面保护层」另命中《搭载 Apple 芯片的 Mac 的 LocalPolicy 文件》（`c745a0845`）与《系统完整性保护》（`b7ea06b49`）。**逐词命中页数为**：`APRR` 1 页、`PPL` 2 页、`SPTM` 2 页、`TXM` 1 页、`SCIP` 5 页、`Execute Never` 1 页。这一分布本身就说明问题：**执行限制的成文重心在《操作系统完整性》这一页上，其余页面是引用与平台变体。**

## 3.2 KIP：MMU 被"配置为"限制可执行映射的范围

《操作系统完整性》页（`8b776536b`，2026-01-28，A 级）：

> 操作系统内核初始化完成后，会启用内核完整性保护 (KIP) 来帮助防止对内核和驱动器代码进行修改。内存控制器提供了受保护的物理内存区域，供 iBoot 用于载入内核和内核扩展。启动完成后，内存控制器会拒绝对受保护的物理内存区域的写入。应用程序处理器的内存管理单元 (MMU) 被配置为帮助防止从受保护内存区域之外的物理内存中映射权限代码，并帮助防止对内核内存区域内的物理内存进行可写入映射。

> 为防止重新配置，用于启用 KIP 的硬件会在启动过程完成后锁定。

这段里与"执行"直接相关的是**第一个 MMU 用途**：不允许把受保护区域之外的物理内存映射为权限代码。逐字读它有三条边界：

1. 它管的是**内核与内核扩展代码的映射来源**（"供 iBoot 用于载入内核和内核扩展"），不是用户 App 的代码页。用户页的可执行性归第 5、8 章的签名路径。
2. 强度用词是「帮助防止」（help prevent），与手册对多数缓解机制一致；手册没有使用"阻止""杜绝"级别的措辞。
3. 「用于启用 KIP 的硬件会在启动过程完成后锁定」——锁定的是**启用配置**，不是页表内容。因此 KIP 提供的是第 4 章"谁能改"的起点，而不是本页"改了会不会被发现"的答案。

## 3.3 APRR：撤销执行权限不需要系统调用，也不需要页表刷新

同页（`8b776536b`，2026-01-28，A 级）逐字：

> 自 Apple A11 仿生和 S3 后的 SoC 新增了硬件原语。此"快速权限访问限制"原语中包括可基于每个线程快速限制访问权限的 CPU 寄存器。通过快速权限访问限制（也称为 APRR 寄存器），支持的操作系统可从内存移除执行权限，无需通过系统调用和页表寻访或刷新。这些寄存器更进一步减少了来自网页的攻击，尤其是经过运行时编译（即时编译）的代码，因为内存无法在读写的同时有效执行。

拆成可核对的断言：

| 断言 | 逐字依据 | 本篇用法 |
|---|---|---|
| 起点代际：A11 仿生与 S3 之后的 SoC | 「自 Apple A11 仿生和 S3 后的 SoC 新增了硬件原语」 | 第 10 章代际分层；早于该代际的设备不得写"具备 APRR" |
| 载体是 CPU 寄存器 | 「包括可基于每个线程快速限制访问权限的 CPU 寄存器」 | 线程粒度（per-thread），不是进程粒度 |
| 效果是"移除执行权限" | 「可从内存移除执行权限」 | 与第 2 章"标记为不可执行"是不同性质的操作 |
| 代价：不需要系统调用、不需要页表寻访或刷新 | 「无需通过系统调用和页表寻访或刷新」 | 第 6 章解释为什么每次 JIT 写入都能做一次切换 |
| Apple 自己给出的收益 | 「更进一步减少了来自网页的攻击，尤其是经过运行时编译（即时编译）的代码，因为内存无法在读写的同时有效执行」 | 这是**手册中把执行限制与网页/JIT 直接连起来的一句**，第 7 章引用 |

最后一句的因果结构值得单独强调：手册给的理由不是"写了也不能执行"，而是「**内存无法在读写的同时有效执行**」——它主张的是**写与执行不能同时成立**，而不是"执行被禁止"。这个差别是本篇第 6 章整章的落点，也是第 15 章列出的第二条流传口径错误（把互斥读成禁止）。

**未核实项**：APRR 的寄存器名、编程接口、生效范围（是否覆盖内核态）与世代差异，手册均未给；源码侧的检索也未落到公开文件（第 15 章 N 类登记）。因此本篇不写任何"APRR 通过 X 寄存器实现"式表述。

## 3.4 man page 里那句关于硬件限制的坦白

`mmap(2)` 手册页（`xnu/bsd/man/man2/mmap.2`，页面日期 February 14, 2020，A- 级）在 `PROT_EXEC` 条目下给出两句对开发者更有操作意义的表述：

> `Pages may be executed.`

> `Note that, due to hardware limitations, on some platforms PROT_WRITE may imply PROT_READ, and PROT_READ may imply PROT_EXEC.  Portable programs should not rely on these flags being separately enforceable.`

第二句必须连同第一句一起引用，因为它承认了一件事：**"读、写、执行"三个权限位并非在所有平台上都能被分别强制**，而 Apple 的建议是跨平台程序不要依赖它们可分别强制。这句话不是对 iOS 现状的描述（它写的是"on some platforms"），但它直接反驳了一种常见写法——"只要请求里没有 PROT_EXEC，就绝不可能执行"。

同族句子来自 `mprotect(2)`（页面日期 October 16, 2008，A- 级）：

> `Not all implementations will guarantee protection on a page basis but Mac OS X's current implementation does.`

即"页级粒度"是"当前实现"的陈述句，且句子主语是 Mac OS X。把它当作 iOS 的跨代承诺属于外推；本篇按限定语处理。

## 3.5 与相邻两层的接缝

第 0 章的四层模型在这里给出接缝的具体形态：

- ①（XN，`15bfe098e`，2024-12-19）给"能标成不可执行"这一能力；
- ②（APRR，`8b776536b`，2026-01-28）给"撤销执行权限的代价极低、且按线程"；
- ③（KIP/PPL/SPTM+TXM/SCIP，同页）给"谁能改页表权限"；
- ④（动态代码签名授权/`MAP_JIT`）给"谁能拿到写执行同时成立的内存"。

一个具体接缝值得单独记下：**②与③的关系**。APRR 让线程态能低成本撤销执行权限，而页表权限的**修改权**在 iOS 上归 PPL（A15/M2 之前）或 SPTM+TXM（之后）。手册把两者写成互补而非替代：前者改变"当前线程能不能执行这段内存"，后者约束"谁能把页表项改成允许执行"。第 4 章按这个口径展开。

## 3.6 本章要点

1. 执行权限的成文来源集中在《操作系统完整性》一页（`8b776536b`，2026-01-28）；`APRR` 一词全库仅 1 页命中。
2. KIP 的 MMU 句管的是**内核与内核扩展代码的映射来源**与**内核内存的可写映射**，不是用户 App 的代码页；强度用词是「帮助防止」。
3. APRR 的三条一手要点：**A11 仿生/S3 之后**、**按线程**、**撤销执行权限不需系统调用与页表刷新**；Apple 给的理由是「内存无法在读写的同时有效执行」，即写执行互斥，而非执行禁止。
4. man page 明确承认三个权限位在某些平台上不可分别强制，页级粒度也只是"当前实现"的陈述；这两句必须与任何"绝对"表述对消。
5. 寄存器名、编程接口、内核态是否同样受 APRR 约束：**未核实**，本篇不写。


# 4. 谁来维持不可执行：保护机制的分工

## 4.1 PPL：把"改页表"这件事收给一个组件

《操作系统完整性》页（`8b776536b`，2026-01-28，A 级）逐字：

> iOS、iPadOS、visionOS 和 watchOS 中的页面保护层 (PPL) 旨在防止代码签名验证完成后对用户空间代码进行修改。PPL 以 KIP 和快速权限访问限制为基础，通过管理页表权限覆盖确保只有 PPL 才能修改包含用户代码和页表的受保护页面。系统支持实施系统层面代码完整性检查（即使在内核遭到入侵的情况下），从而大大减小了攻击面。此保护在 macOS 上不提供，因为 PPL 只在所有执行代码都必须签名的系统上适用。

这一句是"问责边界"最清晰的官方表述，四条要点：

1. **PPL 的目标被写成时序性的**：防的是"**代码签名验证完成后**"对用户空间代码的修改。也就是说，PPL 与第 8 章的按需校验是一前一后的关系——校验负责判定，PPL 负责让判定结果不被绕过改页表的路径推翻。
2. **PPL 的立足点是前两层**：「以 KIP 和快速权限访问限制为基础」。手册明确写了依赖关系，因此不能把三者并列成三道独立闸门。
3. **手段是"页表权限覆盖"的独占管理权**：「只有 PPL 才能修改包含用户代码和页表的受保护页面」——受保护页面被分成"包含用户代码"与"包含页表"两类，这是本篇第 4.5 节分工表的一手依据。
4. **macOS 被明确排除，且给了理由**：「此保护在 macOS 上不提供，因为 PPL 只在所有执行代码都必须签名的系统上适用」。这句是第 10 章平台不对称的一手锚点：**iOS 有 PPL、macOS 没有，且原因是"macOS 允许运行任何代码"**，不是实现进度差异。

**未核实**：PPL 具体保护哪一类页表项、PPL 自身的失效后果、PPL 与页哈希校验之间的时序细节，手册均未给。

## 4.2 SPTM + TXM：A15/M2 起的取代关系

同页（`8b776536b`，2026-01-28，A 级）逐字：

> iOS、iPadOS、macOS 和 visionOS 中的安全页表监控器 (SPTM) 和可信执行监控器 (TXM) 设计为协同工作以协助保护用户和内核进程的页表免遭修改，其中包括攻击者可写入内核和可绕过控制流程保护的情况。……在 A15 或后续芯片和 M2 或后续芯片的 SoC 中，SPTM（与 TXM 结合使用）取代了 PPL……

三条与本章直接相关的读法：

- **平台范围换了**：PPL 那句的平台清单是 iOS/iPadOS/visionOS/watchOS（无 macOS），SPTM+TXM 这句是 iOS/iPadOS/**macOS**/visionOS（无 watchOS）。两句话的平台清单**不重合**，因此"取代 PPL"并不意味着"在所有原来有 PPL 的平台上都出现了 SPTM"。第 10 章按代际与平台分别处理，本篇不写"所有新设备都用 SPTM"。
- **威胁描述更具体**：「其中包括攻击者可写入内核和可绕过控制流程保护的情况」——这是手册少见地把机制与"已具备内核写能力 + 绕过控制流保护"这一攻击者位置对应起来。
- **代际边界是芯片，不是系统版本**：「在 A15 或后续芯片和 M2 或后续芯片的 SoC 中」。同一设备上运行新旧 iOS 不改变这一层，第 10 章据此给出分层建议。

## 4.3 平台对照表注释 2：签名代码强制的平台边界

同页平台对照表的注释 2（逐字，A 级）：

> 2：页面保护层 (PPL) 和安全页表监控器 (SPTM) 会在 macOS 除外（因为 macOS 设计允许运行任何代码）的所有平台上强制执行受信任的签名代码。包括页表保护在内的所有其他安全属性均可用于所有受支持的平台。

这一句有两个方向的用途：

1. 给出**"强制执行受信任的签名代码"这一属性**的平台清单（macOS 除外），并把原因写进括号：「macOS 设计允许运行任何代码」。
2. 反过来确认**"包括页表保护在内的所有其他安全属性均可用于所有受支持的平台"**——即页表保护这件事本身在 macOS 上也是可用的，被排除的是"签名代码强制"这一层。

对评审的实际意义：**"macOS 上没被签名授权的代码也能执行"是设计选择，不是防护缺陷**；而 iOS 侧这条强制是 PPL/SPTM 的立论前提。两者混用是第 15 章的一条反流行口径。

## 4.4 SCIP：协处理器侧的对称表述

同页（`8b776536b`，A 级）逐字：

> SCIP 的工作方式与 KIP 非常类似：启动时，iBoot 将每个协处理器的固件载入到独立于 KIP 区域并且受保护的保留内存区域。iBoot 会配置每个协处理器的内存单元，以帮助防止：
> - 协处理器的受保护内存区域部分外的可执行映射
> - 协处理器的受保护内存区域部分内的可写入映射

这是整套机制里**唯一把"可执行映射"与"可写入映射"作为一对对称限制成文写出**的段落，两条方向恰好相反：区外不许执行、区内不许写。把它单独列出有两个原因：

- 它说明这一限制模式的**通用形态**：不是"数据页不能执行"，而是"每个受保护区域有各自的允许方向"。
- 它出现在协处理器语境（`SCIP` 在手册全库命中 5 页：`59b0b31ff`《安全隔区》、`8b776536b》《操作系统完整性》、`93292bfa6》《术语表》、`ac71d5623》《搭载 Apple 芯片的 Mac 的启动过程》、`c7d85209d》《watchOS 系统安全性》），因此**不要把协处理器侧的表述直接当作主处理器用户页的规则引用**。

## 4.5 分工表：谁改权限、谁验内容、谁定例外

| 组件 | 负责的问题 | 一手表述（档位） | 平台与代际 |
|---|---|---|---|
| ARM Execute Never (XN) | 页能不能被标为不可执行 | 「该功能会将内存页标记为不可执行」（A，`15bfe098e`） | iOS、iPadOS（该句只点名这两个） |
| APRR 寄存器 | 撤销执行权限要花多少代价 | 「无需通过系统调用和页表寻访或刷新」（A，`8b776536b`） | A11 仿生 / S3 之后的 SoC |
| KIP | 谁能把保护区外的物理内存映射为权限代码 | MMU「被配置为帮助防止……映射权限代码」；启用硬件启动后锁定（A） | 内核代码语境 |
| PPL | 谁能改含用户代码与页表的受保护页 | 「只有 PPL 才能修改」（A） | iOS/iPadOS/visionOS/watchOS；**macOS 不提供** |
| SPTM + TXM | 同上（取代者） | 「协助保护用户和内核进程的页表免遭修改」（A） | iOS/iPadOS/macOS/visionOS；A15+/M2+ |
| SCIP | 协处理器固件区的执行/写入方向 | 区外不可执行映射、区内不可写入映射（A） | 各协处理器 |
| CSM（代码签名监控器） | 一次 `MAP_JIT` 请求该不该放行 | `csm_allow_jit_region()`；注释「An address space can only have a single JIT region, and only when it has the appropriate JIT entitlement.」（A-，`code_signing.h:706-713`） | 源码可见；手册以「Apple 专有的动态代码签名授权」指称 |
| 页校验（`cs_validate_page`） | 页内容是否匹配签名 | 「所有可执行内存页的代码签名检查会在载入时进行」（A）+ 调用链（A-，第 8 章） | 全平台（TN3126 明示概念跨平台） |

**读法**：执行限制的"问责"与"判定"是两个人。PPL/SPTM+TXM 管的是**页表权限的修改权**（问责），`cs_validate_page` 与 CSM 管的是**内容与准入的判定**（校验）。手册第 4.1 节那句「防止代码签名验证完成后对用户空间代码进行修改」正是把两者串起来的官方句子：**先判定，再由问责层保证判定不被改页表推翻**。

## 4.6 A- 级源码里的三个"问责落点"

手册不给实现细节，以下三处是源码侧的对应物（xnu main `f6217f89` / tag `xnu-12377.121.6`，A- 级）：

- **写保护立即生效**：`osfmk/vm/vm_map.c:6066` `pmap_options |= PMAP_OPTIONS_PROTECT_IMMEDIATE;`。这是"权限变更何时真正生效"的实现表达，手册无对应句。
- **监控器接管校验**：`osfmk/vm/vm_fault.c:2815-2828` 注释逐字 `When we have a code signing monitor, the monitor will evaluate the code signature for any executable page mapping. No need for the VM to also validate the page. In the code signing monitor we trust :)`。**这句很重要**：存在 CSM 时 VM 不再重复校验，责任被移交；因此"校验由谁完成"依环境而异，不能笼统写成"内核每次页错误都自己算哈希"。
- **例外区域的旁路**：`osfmk/vm/vm_fault.c:4773-4777` `fault_info->cs_bypass = ((*entry)->used_for_jit || (csm_address_space_exempt(...) == KERN_SUCCESS) || (*entry)->vme_resilient_codesign);` —— 三类地址空间不参与按页校验：JIT 用途的映射、被监控器豁免的地址空间、弹性签名映射。**手册没有任何一句话解释这三类旁路**，本篇把它列为第 15 章缺口，并明确：这三条是"实现事实"，不是"Apple 对外承诺的设计"。

## 4.7 本章要点

1. **PPL 的官方定义是时序的**：防"签名验证完成后"对代码的修改，且明确「以 KIP 和快速权限访问限制为基础」——三层不能并列成三道独立闸门。
2. **macOS 不提供 PPL，且被写成设计选择**（`8b776536b` 正文 + 注释 2「macOS 设计允许运行任何代码」）。
3. **SPTM+TXM 的平台清单与 PPL 不重合**，取代关系按芯片代际（A15+/M2+）而非系统版本判定。
4. **SCIP 给出的是"区外不可执行 / 区内不可写"这对称模式**，属协处理器语境，不得当作主处理器用户页规则引用。
5. 源码侧存在三类**校验旁路**（JIT 映射、监控器豁免、弹性签名），手册未解释；有 CSM 时 VM 不重复校验。这两条决定了第 9 章"覆盖面收窄"与第 15 章缺口清单的写法。


# 5. 可执行页的两条取得路径

## 5.1 只有两条路，且它们不同源

在 iOS 上，一页内存获得"可执行"属性只有两条路径，而这两条路径的**授权来源完全不同**：

| 对比项 | 路径一：文件映射 | 路径二：匿名内存 |
|---|---|---|
| 资格从哪来 | 代码签名（页哈希封入 Code Directory） | 「Apple 专有的动态代码签名授权」+ `MAP_JIT` |
| 谁常态使用 | 每一个 App 的每一个可执行页 | 手册逐字点名：Safari 的 JavaScript JIT 编译器 |
| 内容能不能改 | 不能（改了通不过载入时校验，第 8 章） | 能（这正是它存在的理由） |
| 一手出处 | `7c917bf14`、`f49cad4db`（A）+ `kern_mman.c`（A-） | `15bfe098e`（A）+ `kern_mman.c`、`vm_map.c`、`code_signing.h`、`mmap.2`（A-） |
| 是否需要例外授权 | 不需要 | **需要** |

这条分界是理解本篇其余章节的前提：**"iOS 禁止执行内存中的代码"是错的**——路径一每时每刻都在执行内存中的代码；**"iOS 允许 App 自备可执行内存"也是错的**——路径二需要专门的授权，且数量、形态、后续权限变更都被收窄。

## 5.2 路径一：可执行性由签名授予

《App 代码签名过程》页（`7c917bf14`，2024-12-19，A 级）：

> 内核启动后，它将控制哪些用户进程和 App 可以运行。为帮助确保所有 App 均来自批准的已知来源并且未被篡改，这些操作系统要求所有可执行代码均使用 Apple 颁发的证书进行签名。……强制性代码签名将信任链的概念从操作系统扩展至 App，并帮助防止第三方 App 载入未签名的代码资源或使用经 App 自身修改代码。

同一页"代码签名验证"小节给出**动态库这条子路径**的准入规则（发布日同上，A 级）：

> 为保护系统并防止其他 App 在其地址空间中载入第三方代码，系统将为启动时所有链接到进程的动态资源库执行代码签名验证。此验证过程通过团队标识符 (Team ID) 完成。……程序可链接到系统自带的任何平台资源库，也可以链接到代码签名中具有与主可执行文件相同团队标识符的资源库。因为作为系统一部分发布的可执行文件不具有团队标识符，所以它们只能链接到随系统本身发布的资源库。

这段是第 9 章"库校验"削弱面的一手锚点：它把"能载入哪些可执行页"限制成**平台库 + 同 Team ID 库**两类，并且解释了系统自身可执行文件为什么只能链接系统库（无 Team ID）。

**A- 级实现落点**（xnu tag `xnu-12377.121.6`）：

- 文件映射的上限权限直接从打开的文件描述符推导，且 EXECUTE 是默认上限的一部分：`bsd/kern/kern_mman.c:620-627`：

  > `maxprot = VM_PROT_EXECUTE;      /* TODO: Remove this and restrict maxprot? */`

  随后按可读位收紧（`if (fp->fp_glob->fg_flag & FREAD) { maxprot |= VM_PROT_READ; } else if (prot & PROT_READ) { … error = EACCES; }`）。

  **写法纪律**：这行 `maxprot = VM_PROT_EXECUTE` 带着一条 Apple 自己的 `TODO` 注释。它是"上限为何含 EXECUTE"的实现事实，**不得**被写成"iOS 允许任何映射可执行"——`curprot`（实际权限）与文件内容能否通过校验才决定该页是否真能执行（第 8 章）。同时，注释本身表明 Apple 视其为待收紧项，这属于一手文本可见的**自我限定**。

- 非匿名映射不得使用 `MAP_JIT`：`kern_mman.c:552-554`，文件映射分支内 `if (flags & MAP_JIT) { return EINVAL; }`。即第 5.3 节的例外路径**只适用于匿名映射**，两条路径不能杂交。

- 进入执行/校验前对被分页对象断言其为签名对象：`osfmk/vm/vm_fault.c:8926-8946` 的 `assert(object->code_signed);` 后接 `cs_validate_page(...)` 逐页校验（详见第 8 章调用链）。

- `mmap(2)` 手册页对 `PROT_EXEC` 的一行说明：`Pages may be executed.`（`mmap.2:114-115`，页面日期 2020-02-14，A- 级）——注意这是 BSD 接口文档的通用描述，不是 iOS 策略声明；iOS 上的实际条件是签名。

## 5.3 路径二：手册把例外条件写成三件事

《iOS、iPadOS 和 visionOS 中的运行时进程安全性》页（`15bfe098e`，2024-12-19，A 级）——把手册口径写到"单个 `mmap` 调用"的那一句：

> 只有处于严格的控制条件下，App 才能使用标记为可写入和可执行的内存页：内核会检查 Apple 专有的动态代码签名授权是否存在。即使如此，也只有单个 `mmap` 调用能用于请求一个可执行且可写入的内存页，系统为可执行且可写入的内存页分配了随机地址。

三个条件各自的可核对形态：

| 手册条件 | 源码/文档侧的对应物 | 档位 |
|---|---|---|
| 「内核会检查 Apple 专有的动态代码签名授权是否存在」 | `kern_mman.c:479-482`：`if ((flags & MAP_JIT) && (vm_map_csm_allow_jit(user_map) != KERN_SUCCESS)) { printf("[%d] code signing monitor denies JIT mapping\n", proc_pid(p)); return EPERM; }`；`vm_map.c:19889-19893` 把 `vm_map_csm_allow_jit()` 转调 `csm_allow_jit_region(vm_map_pmap(map))` | A- |
| 「只有单个 `mmap` 调用」/ 单区域 | `code_signing.h:706-713` 注释逐字：`Validate that an address space will allow mapping in a JIT region within the monitor environment. An address space can only have a single JIT region, and only when it has the appropriate JIT entitlement.` | A- |
| 「分配了随机地址」 | `vm_map_internal.h:943-958`：`VM_MAP_POLICY_ALLOW_JIT_RANDOM_ADDRESS()` 返回 `VM_MAP_IS_ALIEN(map)`；`VM_MAP_POLICY_ALLOW_MULTIPLE_JIT()` 在 `VM_MAP_IS_ALIEN(map)` 为真或 `map->single_jit` 时为 false | A- |

**必须保留的一个不确定性**：手册说"单个 `mmap` 调用"，源码注释说的是"一个地址空间只能有一个 JIT 区域"。二者语义相近但不同（一次调用可映射多页；一个区域可由多次调用扩成）。本篇按手册措辞写"单个调用请求"，并在第 15 章把这一处登记为**手册措辞与源码措辞的口径差**，不宣称两者等同。

## 5.4 例外的形态约束：哪些组合被直接拒绝

`bsd/kern/kern_mman.c:403-424`（A- 级）给出的组合禁令，是"严格控制条件"最具体的实现清单：

| 约束 | 源码条件 | 失败返回 |
|---|---|---|
| 必须与匿名映射同用 | `flags & MAP_ANON` 必须成立；否则 `EINVAL` | `EINVAL` |
| 禁止共享映射 | `!(flags & MAP_SHARED)` | `EINVAL` |
| 禁止与弹性签名合用 | 不得同时带 `MAP_RESILIENT_CODESIGN` / `MAP_RESILIENT_MEDIA` | `EINVAL` |
| 禁止与 `MAP_TPRO` 合用 | 同上清单 | `EINVAL` |
| 禁止固定地址 | `MAP_FIXED` 被拒；仅 arm64 且页大小为 4K 且非 exotic map 时例外，注释逐字 `/* MAP_FIXED with MAP_JIT is allowed for arm64/4k */` | `EINVAL` |

最后一行是本篇少见的"手册未提、源码可见"的细节：**手册说"分配了随机地址"，而源码在 arm64/4K 的窄条件下允许 `MAP_FIXED`**。本篇把它记为实现事实（A-），并明确：不能据此写"iOS 上可以请求固定地址的 JIT 映射"——条件、平台与 exotic map 判定都在内核内部，且手册口径优先作为对外承诺。

## 5.5 一旦到位就不许降级：非 macOS 的 RWX 一次性要求

`osfmk/vm/vm_map.c:3219-3262`（A- 级）注释逐字：

> `Native macOS processes and all non-macOS processes are expected to create JIT regions via mmap(MAP_JIT, RWX)`

> `We deal with these by letting the MAP_JIT stick in order to avoid CS violations when these pages are mapped executable down the line. … If the user-space application ever changes permissions to RWX, which they are allowed to since the mapping was originally created with MAP_JIT, then they'll switch over to using the XNU_USER_JIT type, and won't be allowed to downgrade any more after that. … When not on macOS, a MAP_JIT mapping without VM_PROT_ALL is strictly disallowed.`

实际拒绝点在 `:3249-3262`：`/* non-macOS: reject JIT regions without RWX */` → 打印 `CODE SIGNING: %d[%s] %s(%d): JIT requires RWX: failing.` 并返回 `KERN_PROTECTION_FAILURE`。

这段给出一个反直觉但重要的顺序：**在 iOS 上，JIT 映射必须一开始就要到 RWX（可读可写可执行），之后不允许降级**。它看起来与"最小权限"相反，但注释说清了动机——留着一个可降级的 JIT 映射，会导致后续映射为可执行时触发签名校验冲突。第 6 章解释这个"一次性到位"如何与线程态写保护共存：**页表层面始终 RWX，"能不能写"与"能不能执行"的切换发生在 APRR 这一层，而不是 `mprotect` 这一层**。

相关的两条策略（`vm_map_internal.h:943-988`，A-）：

- `VM_MAP_POLICY_ALLOW_JIT_COPY()`：alien map 为 false；配合 `vm_map.c:892-915` 的注释 `But for security reasons on some platforms, we don't want the new mapping to be "used for jit", so we reset the flag here.`——**跨拷贝/派生时"用于 JIT"标记被收回**。
- 继承与共享同样由策略函数表达（`…_JIT_INHERIT`、`…_JIT_SHARING`）。本篇不推断这些函数在各平台的返回值，只登记"这五类问题被集中在五个策略宏里"这一结构事实。

## 5.6 man page 里的平台分叉：同样被拒，结果不一样

`xnu/bsd/man/man2/mmap.2:217-234`（A- 级）逐字：

> `MAP_JIT`
> `Allow mapping pages both PROT_WRITE and PROT_EXEC when the hardened runtime is enabled. Without this flag an attempt to create a mapping with both PROT_WRITE and PROT_EXEC set will fail with MAP_FAILED on macOS. A writable, but not executable mapping is returned on iOS, watchOS and tvOS.`
> `Usage of this flag requires the caller to have the com.apple.security.cs.allow-jit entitlement on macOS.`

同页 `:122-137`（逐字）：

> `When the hardened runtime is enabled … the protections cannot be both PROT_WRITE and PROT_EXEC without also having the flag MAP_JIT and the process possessing the com.apple.security.cs.allow-jit entitlement`

同页 `:399-409` COMPATIBILITY 段（逐字）：

> `On macOS 10.14 Mojave the hardened runtime restricts pages from having both the PROT_WRITE and PROT_EXEC protections without the caller also setting the MAP_JIT flag and possessing the com.apple.security.cs.allow-jit entitlement.`

三条结论，其中第一条是本章最重要的平台差异：

1. **同样是"请求 W+X 而不带 `MAP_JIT`"，macOS 返回 `MAP_FAILED`，iOS/watchOS/tvOS 返回"可写但不可执行"的映射。** 前者是失败，后者是静默降级。这个差别**在《Apple 平台安全保护》手册里没有对应表述**，只能引 man page（A-），因此本篇把它写成"接口文档所述行为"，不写成"Apple 的安全承诺"。
2. 这段全部条件都以「**when the hardened runtime is enabled**」为前提，而"hardened runtime"是 macOS 概念（第 10 章）；把它当作 iOS 的默认前提会错。
3. COMPATIBILITY 句给出**带日期的起点**：macOS 10.14 Mojave。这是本篇"通道/规则带版本起点"式表述的一手模板。

`mprotect(2)`（页面日期 2008-10-16，A- 级）另两句：

> `When a program violates the protections of a page, it gets a SIGBUS or SIGSEGV signal.`
> `EACCES — The requested protection conflicts with the access permissions of the process on the specified address range.`

后者解释了第 5.5 节"不许降级"在用户态的可见形态：`mprotect` 直接 `EACCES`。

## 5.7 未签名的"数据"能不能变成"代码"

手册给的结论性句子在 `7c917bf14`（A 级）：强制性代码签名「帮助防止第三方 App 载入未签名的代码资源或使用经 App 自身修改代码」。

内核侧的三条落点（A- 级，均在第 8 章展开）：

- 拒绝把被标记 NX 的页映射为 EXEC：`vm_fault.c:2838-2871`；
- 污染页（tainted）即视为无签名代码：`vm_fault.c:2873-2891` 注释逐字 `- pmapping an unsigned page executable - this means unsigned code;` 与 `- writeable mapping of a validated page - the content of that page can be changed without the kernel noticing, therefore unsigned code can be created`；
- 判定失败后的处置由 `CS_KILL` / `CS_HARD` 决定（`kern_cs.c:248-312`）。

**第二条评论句值得单独引用**，因为它是整套机制里对"为什么可写与可执行不能同时存在"给出**内核自己那套因果解释**的文本：可写映射会让内容在"内核没有察觉"的情况下改变，因此等于允许制造未签名代码。这句话把第 6 章的写执行互斥与第 8 章的按页校验连成一条因果链，也是本篇反对"这只是缓解措施之一"这种淡化表述的一手依据。

## 5.8 本节缺口（未核实）

1. **iOS 侧"动态代码签名授权"的公开文档页不存在**：`https://developer.apple.com/tutorials/data/documentation/bundleresources/entitlements/com.apple.security.dynamic-codesigning.json` 返回 HTTP 404；手册只用中文描述"Apple 专有的动态代码签名授权"，未给英文名。因此本篇**不写**任何 iOS entitlement 键名作为"公开授权属性"。
2. **developer.apple.com 站内没有 `MAP_JIT`、`mmap`、`mprotect`、`pthread_jit_write_protect_np` 的符号文档页**：六种前缀（`pthreads/`、`pthread/`、`darwin/`、`kernel/`、`bsd/`、`system/pthread/`）各取该符号名实测均 404。这些 API 的官方一手说明只存在于 XNU 仓库 man page、libpthread 仓库 man page 与《Porting just-in-time compilers to Apple silicon》文章内。
3. **iOS 测试授权文件里的 `dynamic-codesigning` 键**（libpthread main `42d026df5b07`，tag `libpthread-539.100.4`，`tests/pthread_jit_write_with_callback-ios-entitlements.plist` 逐字含 `<key>dynamic-codesigning</key><true/>`）是 A- 级"当前实现/测试这么用"，**不等于** Apple 对外承诺的公开 entitlement 名；同名 macOS plist 只有 `com.apple.security.cs.jit-write-allowlist`。

## 5.9 本章要点

1. 两条取得路径不同源：**文件映射的资格来自签名，匿名映射的资格来自例外授权**；`MAP_JIT` 不能与文件映射杂交（`EINVAL`）。
2. 手册把例外写成**三件事同时成立**：授权存在 + 单个 `mmap` 调用 + 随机地址；且**唯一被点名使用者是 Safari 的 JIT 编译器**。
3. 例外被收窄的具体形态：禁共享、禁固定地址（arm64/4K 有窄例外）、禁与弹性签名合用、非 macOS 上必须一次到位 RWX 且之后不许降级、每地址空间单区域、跨拷贝时收回"用于 JIT"标记。
4. **iOS 与 macOS 对同一失败请求的结果不同**（降级 vs `MAP_FAILED`），且这条只见于 man page。
5. iOS 侧的 JIT 授权键**没有公开文档页**；引用测试 plist 时必须标注 A- 且加"不构成对外承诺"限定。


# 6. 写与执行的互斥：运行时切换及其设计意图

## 6.1 互斥发生在两个不同的层

「写与执行不能同时」这件事，在本篇的证据里出现在两个层面，混谈会造成严重误判：

| 层 | 表现 | 一手出处 | 档位 |
|---|---|---|---|
| **映射层**（`mmap` 请求时） | 不带 `MAP_JIT` 就要么失败（macOS）要么被降级成"可写不可执行"（iOS/watchOS/tvOS） | `mmap.2` 三段（第 5.6 节） | A- |
| **线程层**（运行时切换） | 「MAP_JIT 区域永远不会同时可写与可执行」，由 per-thread 开关决定当前是写态还是执行态 | libpthread `man/pthread_jit_write_protect_np.3`（2020-05-01） | A- |

映射层管"这块内存有没有资格存在"，线程层管"当前这一刻这个线程能写还是能执行"。**关键结论：在 iOS 上 JIT 页的页表权限可以始终是 RWX（第 5.5 节），而写/执行的互斥由线程层的开关维持**。这就是为什么"看到某进程有 RWX 映射"不等于"写执行互斥已失效"——两者在不同层，第 12 章的可观测性讨论据此展开。

## 6.2 唯一逐字的官方 API 说明：libpthread 的 man page

来源是 Apple 官方开源仓库 libpthread 的手册页 `man/pthread_jit_write_protect_np.3`（`.Dd May 1, 2020`；main `42d026df5b07825070f60134b980a1ec2552dfee`，最新 tag `libpthread-539.100.4`）。这是 A- 级（Apple 一手、可证当前实现），**不是** developer.apple.com 网页文档——后者不存在（第 5.8 节）。

NAME 行逐字：

> `pthread_jit_write_protect_supported_np , pthread_jit_write_protect_np , pthread_jit_write_with_callback_np , pthread_jit_write_freeze_callbacks_np — thread JIT region write protection settings`

DESCRIPTION 逐字（四句构成完整语义）：

> `The pthread_jit_write_protect_supported_np function returns whether per-thread write protection on the MAP_JIT region is supported on this platform.`

> `On platforms where pthread_jit_write_protect_supported_np is true, MAP_JIT regions are never writeable and executable simultaneously. When write protection is enabled for the thread, writes by the thread to the MAP_JIT region are denied and the MAP_JIT region is executable. When write protection is disabled for the thread, writes by the thread to the MAP_JIT region are allowed and the MAP_JIT region is not executable.`

> `The pthread_jit_write_protect_np function sets whether MAP_JIT region write protection is enabled for this thread. Pass a non-zero value for the enabled parameter to enable thread JIT region write protection and allow execution. Pass a zero value for the enabled parameter to disable thread JIT write protection and deny execution.`

> `On platforms where pthread_jit_write_protect_supported_np is false, MAP_JIT regions are simultaenously writeable and executable. Calls to pthread_jit_write_protect_np are no-ops on unsupported platforms.`

（最后一句里的 `simultaenously` 是原文拼写，引用时保留并注明。）

四个要点：

1. **粒度是"线程"而不是"页"**：`enabled for this thread`。同一片 JIT 区域，A 线程可写时 B 线程仍可能处于执行态。任何"整块 JIT 内存要么可写要么可执行"的表述都不如这句准确。
2. **两个方向都有明确后果**：写保护开 → 写入被拒 + 区域可执行；写保护关 → 允许写 + 区域不可执行。这是手册 APRR 句「内存无法在读写的同时有效执行」的接口形态（第 3.3 节），两者互为表里：**前者是 A 级承诺，后者是 A- 级实现语义**。
3. **是否存在"不支持"的平台**：man page 显式为 false 的情况留了位置（「Calls … are no-ops on unsupported platforms」）。**哪些平台/配置为 false，本篇未核实**——文档给的是判定函数而不是判定表。第 15 章登记。
4. **`with_callback` 的语义**（同页逐字）：

   > `The pthread_jit_write_with_callback_np function disables write protection, calls the supplied callback with ctx to write to the JIT region after validating its input for safety according to application logic, and then re-enables write protection. That is, it's roughly equivalent to: pthread_jit_write_protect_np(false); int rc = callback(ctx); pthread_jit_write_protect_np(true); return rc;`

   它的价值在于把"切换"从调用方手里拿走，只暴露一个可枚举的写入口——下一节的白名单收紧正是围绕它。

## 6.3 官方 SECURITY CONSIDERATIONS 段：可直接引用的设计意图

同页（A- 级）逐字：

> `The purpose of per-thread JIT region write protection is to serve as a hardening measure against attacks that attempt to gain code execution by exploiting vulnerabilities to write unintended code into the JIT region.`

> `Some attacks may attempt to defeat JIT write protection by inducing unexpected/unintended calls to pthread_jit_write_protect_np, e.g. dynamically via dlsym. To further harden against such attacks, the pthread_jit_write_with_callback_np interface is intended to allow even tighter control over code permitted to write to the JIT region; in applications that adopt the com.apple.security.cs.jit-write-allowlist entitlement, only callbacks specifically enumerated in an invocation of PTHREAD_JIT_WRITE_ALLOW_CALLBACKS_NP can do so, and pthread_jit_write_protect_np is disabled.`

> `By default, JIT write callbacks are not allowed in frameworks/dylibs dynamically loaded at runtime via dlopen.`

> `Allowed callbacks should assume that their input is attacker-controlled and make an effort to validate that the instructions to be written should be permitted.`

这一整段在本篇的地位很高，因为它同时做到三件事：

1. **给机制定强度**：「serve as a **hardening measure**」——Apple 自己把它归为加固措施，不是边界保证。这是本篇拒绝任何"不可能写入并执行 JIT 代码"表述的直接依据。
2. **给出它明确设想过的对抗路径**：通过 `dlsym` 之类的动态手段诱导**非预期的切换调用**。这一段是官方文本里少见的、承认"机制的弱点在于谁能调用它"的句子。
3. **给出加固顺序**：默认允许任意代码切换 → 采纳 `jit-write-allowlist` 后切换函数被禁用、只走白名单回调 → `dlopen` 进来的框架/动态库默认拿不到回调资格 → 白名单回调自身仍要假设输入攻击者可控。这四步是第 14 章"能支持哪些结论"与第 9 章"哪些配置会收窄"的直接材料。

ENTITLEMENTS 段逐字（两个键，注意其档位是 A-，来自 man page 而非 developer 授权属性目录）：

> `com.apple.security.cs.jit-write-allowlist — A Boolean value that indicates whether pthread_jit_write_protect_np should be disabled and pthread_jit_write_with_callback_np must be used instead to write to the JIT region.`

> `com.apple.security.cs.jit-write-allowlist-freeze-late — A Boolean value that indicates whether the runtime should wait for an explicit call to pthread_jit_write_freeze_callbacks_np before "freezing" the set of allowed JIT write callbacks…`

**"冻结"这个概念值得单列**：白名单不是一次声明就永久开放，存在一个显式的"集合定稿"动作。第 9 章的审计清单据此有一条可打勾项：是否使用了 `-freeze-late` 因而必须自己负责在正确时点调用冻结。

## 6.4 developer 文档侧的唯一成文口径

来源：《Porting just-in-time compilers to Apple silicon》，https://developer.apple.com/documentation/apple-silicon/porting-just-in-time-compilers-to-apple-silicon （页脚 © 2026，记「截至 2026 可用」，A 级）。逐字：

> `The Hardened Runtime capability prohibits the execution of code in a memory page, unless that code is accompanied by a valid code signature. Because a JIT compiler doesn't sign the code it generates, it technically violates the rules of the Hardened Runtime. To fix this issue, you need to adjust your Hardened Runtime settings to allow JIT-related activities.`

这一句是全套表述里最直白的一条：**硬化运行时禁止执行没有有效签名伴随的页内代码，而 JIT 生成的代码按定义没有签名，因此它在技术上就违反规则**。Apple 没有把它写成"安全的例外"，而是写成"需要你调整设置才允许"。

同页其余四句（A 级，逐字）：

> `When your app has the Hardened Runtime capability and the com.apple.security.cs.allow-jit entitlement, it can only create one memory region with the MAP_JIT flag.`

> `When memory protection is enabled, a thread cannot write to a memory region and execute instructions in that region at the same time. Apple silicon enables memory protection for all apps, regardless of whether they adopt the Hardened Runtime. Intel-based Mac computers enable memory protection only for apps that adopt the Hardened Runtime.`

> `[IMPORTANT] On macOS, when you add the com.apple.security.cs.jit-write-allowlist entitlement, your app can no longer call pthread_jit_write_protect_np(). The pthread_jit_write_protect_np() function isn't available on iOS.`

> `Your app crashes if you call pthread_jit_write_with_callback_np() with a callback function that isn't in the allowlist.`

> `If you add the com.apple.security.cs.jit-write-allowlist-freeze-late entitlement, you need to call pthread_jit_write_freeze_callbacks_np() before your first call to pthread_jit_write_with_callback_np(). If you call pthread_jit_write_with_callback_np() before freezing the callbacks allowlist, the system terminates your process with an error.`

其中两处冲突与一处代际分叉必须点明：

- **「`pthread_jit_write_protect_np()` isn't available on iOS」与 libpthread man page 的 "supported platforms" 表述并存**（man page 用 `…_supported_np()` 做运行时判定，文章直接断言 iOS 不可用）。本篇的写法是：**面向第三方开发者的 iOS JIT 切换接口，按 developer 文档为"不可用"**；而 man page 描述的是接口的通用语义与支持判定，两者不是同一层的句子。iOS 上 WebKit/JavaScriptCore 如何切换，Apple 未公开说明（第 6.7 节缺口）。
- **代际分叉**：Apple 芯片对**所有** App 启用 memory protection，不论是否采纳硬化运行时；Intel Mac 只对采纳硬化运行时的 App 启用。这句同时管第 9 章（削弱面的代际差异）与第 10 章（平台矩阵），是本篇少见的、Apple 明文写出的"同一规则在两代硬件上适用范围不同"。
- **违规后果被写成两句不同的话**：回调不在白名单 → 「Your app crashes」；冻结之前调用 → 「the system terminates your process with an error」。两者都不是"返回错误码"，本篇在第 12 章据此区分"崩溃"与"终止"两种可观测形态。

## 6.5 内核侧的 W^X 闸门：两种策略，取决于地址空间类型

`osfmk/vm/vm_map.c:3180-3217`（xnu main `f6217f89` / tag `xnu-12377.121.6`，A- 级）是映射层互斥的实现落点。条件链（简化转述，逐字要素保留）：`cur_protection` 同时含 `VM_PROT_WRITE|VM_PROT_EXECUTE`，且（macOS 侧 `cs_process_global_enforcement()` 或 `vm_map_cs_enforcement(map)`，arm64 上再或 `!VM_MAP_IS_EXOTIC(map)`），且 `csm_address_space_exempt(map->pmap) != KERN_SUCCESS`，且 `VM_MAP_POLICY_WX_FAIL(map) || VM_MAP_POLICY_WX_STRIP_X(map)`，且 `!entry_for_jit`。命中后：

> `printf("CODE SIGNING: %d[%s] %s: curprot cannot be write+execute. %s\n", …, (vm_protect_wx_fail ? "failing" : "turning off execute"))`

随后 `cur_protection &= ~VM_PROT_EXECUTE;`；若 `vm_protect_wx_fail` 则 `return KERN_PROTECTION_FAILURE`。

策略选择来自 `vm_map_internal.h:923-941`（逐字语义）：`VM_MAP_POLICY_WX_FAIL()` 对 alien map 返回 false、其余返回 true；`VM_MAP_POLICY_WX_STRIP_X()` 对 alien map 返回 true、其余 false。**即：常规用户地址空间直接失败，exotic/alien 地址空间降级为不可执行。**

三点读法：

1. 这条闸门与第 5.6 节 man page 的分叉并不矛盾：man page 讲的是 `mmap` 系统调用层的返回形态，`vm_map.c` 讲的是内核映射建立处的策略；两者都写"W 与 X 不能同时"，但**"同时"在这里指页表权限，不含线程层开关**（第 6.1 节）。
2. `!entry_for_jit` 是豁免条件之一——即带 `MAP_JIT` 的条目走第 5.5 节的 JIT 路径，不走这里。
3. `csm_address_space_exempt()` 决定"这个地址空间是否被监控器豁免"。这与第 4.6 节的 `cs_bypass` 三来源一致，属**同一类未成文的旁路**：手册没有任何一句解释"哪些地址空间会被豁免"。第 9 章把"存在豁免类地址空间"列为需向 Apple 侧或代际确认的审计项，第 15 章列为缺口。

## 6.6 另一处同名打印：拷贝后重置 JIT 标记

`osfmk/vm/vm_map.c:892-915`（A- 级）位于 `vm_map_entry_reinit_code_signing_after_copy()`，注释逐字：

> `The "used_for_jit" flag was copied from OLD to NEW in vm_map_entry_reinit_after_copy(). But for security reasons on some platforms, we don't want the new mapping to be "used for jit", so we reset the flag here.`

条件是 `!VM_MAP_POLICY_ALLOW_JIT_COPY(map) && entry->used_for_jit`（对应 `vm_map_internal.h:980-988`：alien map 为 false）。同一函数附近还有与 W^X 同名的打印。**这条的实现意义是：JIT 资格不随映射复制自动继承**，因而"派生出一个带 JIT 属性的新映射"不是天然可行的路径。"for security reasons on some platforms" 这个措辞本身也说明 Apple 知道平台差异存在，但没有成文说明是哪些平台——本篇按未核实处理。

## 6.7 本节缺口

1. `pthread_jit_write_protect_np` 等符号**没有 developer.apple.com 网页文档**（实测 6 种 slug 全 404），因此第 6.2、6.3 节的逐字引文全部来自 libpthread 仓库 man page（A-）。
2. **iOS 上 per-thread 切换执行权限的实现文件未见于公开源码**：man page 只给语义，APRR 相关实现位于闭源的 libsystem/Darwin 层。检索词：`APRR`、`pthread_jit_write_protect`、`mach_absolute`（libpthread 目录树 JSON 内无同名 man page）。
3. **Apple 是否公开说明 WebKit 在 iOS 上如何在不使用 `pthread_jit_write_protect_np` 的情况下切换 W/X**：检索命中 0。手册侧唯一相关句是「Safari 浏览器对其 JavaScript 即时 (JIT) 编译器使用了此功能」（`15bfe098e`）。
4. **哪些平台/配置下 `pthread_jit_write_protect_supported_np` 返回 0**：无成文清单（第 6.2 节）。

## 6.8 本章要点

1. 写执行互斥**分两层**：映射层（`mmap` 与内核 `vm_map_enter` 的 W^X 闸门）与线程层（APRR 支撑的 per-thread 写保护）。iOS 上 JIT 页的页表权限可以是 RWX，互斥靠线程层维持。
2. 官方接口只有四个函数；语义由 libpthread man page 逐字给出，档位是 A-，**因为 developer 站内没有对应网页文档**。
3. Apple 自己给机制定的强度是「**hardening measure**」，并且明文设想了"诱导非预期切换调用（例如经 `dlsym`）"这条对抗路径；对应的收紧是 `jit-write-allowlist`（禁用切换函数、只走枚举回调）与 `dlopen` 框架默认不获资格。
4. 「Apple 芯片对所有 App 启用 memory protection，Intel Mac 只对采纳硬化运行时的 App 启用」——这是代际适用范围的一手分叉。
5. 内核侧存在**两类未成文的豁免**（地址空间被监控器豁免、`MAP_JIT` 条目走独立路径），手册未解释；审计时它们是覆盖面问题，不是缺陷断言。


# 7. 即时编译作为例外：谁被允许、如何授予与撤销

## 7.1 手册给的"谁被允许"只有一句，且点了名

《iOS、iPadOS 和 visionOS 中的运行时进程安全性》页（`15bfe098e`，2024-12-19，A 级）里那句已被本篇反复引用的话，在 JIT 这一章的要害是它的**最后一句**：

> 只有处于严格的控制条件下，App 才能使用标记为可写入和可执行的内存页：内核会检查 Apple 专有的动态代码签名授权是否存在。……Safari 浏览器对其 JavaScript 即时 (JIT) 编译器使用了此功能。

手册在整套 iOS 执行限制文本里，**唯一逐字点名使用可写可执行内存的组件是 Safari 的 JavaScript JIT 编译器**。而《操作系统完整性》页（`8b776536b`，2026-01-28，A 级）与《Apple SoC 安全性》页（`87716a080`，2026-01-28，A 级）把 APRR 的收益也指向同一处：

> 这些寄存器更进一步减少了来自网页的攻击，尤其是经过运行时编译（即时编译）的代码，因为内存无法在读写的同时有效执行。

两句话合起来给出本篇第 7 章的定位：**JIT 例外不是"给开发者的灵活口子"，而是 Apple 为一个自己点名的组件设计、并用硬件原语把其风险压到"读写与执行不同时"的一处受控缺口**。

## 7.2 授予侧：Apple 成文的例子清单

macOS 侧唯一成文的授权属性页《Allow execution of JIT-compiled code entitlement》（`com.apple.security.cs.allow-jit`，https://developer.apple.com/documentation/bundleresources/entitlements/com.apple.security.cs.allow-jit ，页脚 © 2026，「截至 2026 可用」，A 级）摘要句逐字：

> `A Boolean value that indicates whether the app may create writable and executable memory using the MAP_JIT flag.`

正文逐字给出的**用途清单**（这是 Apple 一手文本里唯一一份"谁会需要 JIT"的枚举）：

> `You can create memory that's both writable and executable by passing the `MAP_JIT` flag to the `mmap()` system function. The Hardened Runtime disallows this by default, because it creates a security risk. However, some apps and system frameworks rely on this functionality, typically for performance reasons. Examples include:`
> - `The fast-path of the `JavaScriptCore` framework`
> - `Certain Python frameworks`
> - `Perl-compatible regular expressions (PCRE)`
> - `An app that creates a dynamically-compiled, proprietary macro language`

同一页两句后果与迁移指引（A 级，逐字）：

> `Without the `Allow execution of JIT-compiled Code entitlement`, frameworks that rely on just-in-time (JIT) compilation may fall back to an interpreter. Other code using JIT compilation may crash or behave in unexpected ways.`

> `Digital rights management (DRM) solutions that currently use unsigned executable memory should instead change to using the `MAP_JIT` flag and the entitlement.`

以及"如何添加"（A 级，逐字）：

> `To add the entitlement to your app, first enable the Hardened Runtime capability in Xcode, and then under Runtime Exceptions, select Allow Execution of JIT-compiled Code.`

四点读法：

1. **默认拒绝的理由被写成"creates a security risk"**，不是"不支持"。
2. **降级形态有官方说法**：没有该授权时，依赖 JIT 的框架「may fall back to an interpreter」——这是本篇第 14 章"能支持的结论"里少见的**性能与安全的可核对交换**。
3. **DRM 那句是一条明确的迁移指令**：Apple 承认存在"当前使用未签名可执行内存"的实现，并要求改为 `MAP_JIT` + 授权。第 9 章把它列为削弱面的对偶项（不走正门的路径确实存在过）。
4. **例子清单全部是 macOS 语境**（JavaScriptCore fast-path、Python 框架、PCRE、自有宏语言）。清单里**没有任何 iOS 应用形态**——这与第 7.4 节的 iOS 侧零命中相互印证。

《Porting just-in-time compilers to Apple silicon》（A 级，截至 2026 可用）补两句授予边界：

> `When this entitlement is present, the system allows your app to call `mmap` with the `MAP_JIT` flag. If you don't have this entitlement, calls using that flag return an error.`

> `The `com.apple.security.cs.allow-jit` entitlement is required only when an app adopts the Hardened Runtime capability. If you don't adopt this capability, you don't need the entitlement to use the `MAP_JIT` flag.`

第二句是 macOS 语境下的**条件性**表述：不采纳硬化运行时的 App 不需要该授权就能用 `MAP_JIT`。这句话**不能被搬到 iOS**（iOS 没有硬化运行时这一层，见第 10 章），但在 macOS 审计里它是"授权是否真的在起作用"的判定入口。

## 7.3 授予侧：内核里的判定与两类区域

手册说「内核会检查 Apple 专有的动态代码签名授权是否存在」，源码侧的落点（A- 级）：

- 准入判定发生在 `mmap` 路径：`bsd/kern/kern_mman.c:479-482`——`vm_map_csm_allow_jit(user_map)` 不成功时 `printf("[%d] code signing monitor denies JIT mapping\n", proc_pid(p));` 并 `return EPERM;`。
- `osfmk/vm/vm_map.c:19889-19893`：`vm_map_csm_allow_jit()` 直接转调 `csm_allow_jit_region(vm_map_pmap(map))`，即判定权在**监控器（monitor）**侧，不在 VM 层自行决定。
- `bsd/sys/code_signing.h:706-730`（注释逐字，三段）：

  > `Validate that an address space will allow mapping in a JIT region within the monitor environment. An address space can only have a single JIT region, and only when it has the appropriate JIT entitlement.`

  > `Associate a JIT region with an address space in the monitor environment. An address space can only have a JIT region if it has the appropriate JIT entitlement.`

  > `Associate a debug region with an address space in the monitor environment. An address space can only have a debug region if it is currently being debugged.`

**最后一段容易被漏读，但对第 12 章很重要**：手册所说"动态代码签名授权"在源码层对应**两个不同概念**——JIT 区域与调试区域。后者取决于"该进程当前是否正在被调试"，与前者的授权键无关。因此"某进程存在被监控器认可的可执行内存例外"这件事，有两种来源，审计时必须区分；把"调试区域"读成"JIT 授权被打开"是常见误判。

## 7.4 iOS 侧的第三方渠道：零命中登记

以下四项是本章的射程边界，全部按未核实/零命中处理：

1. **iOS 上第三方 App 申请 JIT 的公开渠道**：developer.apple.com 站内检索命中 0——`MAP_JIT` / `allow-jit` 相关页面均只在 macOS 硬化运行时语境出现；`com.apple.security.cs.allow-jit` 文档页的 `platforms` 字段实测为空数组。Apple 一手文本里出现的只有第 7.2 节那份 macOS 例子清单。
2. **iOS 侧的授权键名**：唯一可见证据是 libpthread 仓库的 iOS 测试授权 plist（tag `libpthread-539.100.4`）逐字包含 `<key>dynamic-codesigning</key><true/>`，而同名 macOS plist 只有 `com.apple.security.cs.jit-write-allowlist`。这是 A- 级"当前实现/测试这么用"，**不构成对外承诺的公开 entitlement 名**。
3. **`pthread_jit_write_protect_np()` 在 iOS 上不可用**：developer 文章逐字 `The pthread_jit_write_protect_np() function isn't available on iOS.`（A 级）。与第 6.2 节 man page 的支持判定语句并存，本篇以文章口径为准。
4. **WebKit 在 iOS 上如何切换 W/X**：检索命中 0；手册唯一相关句是「Safari 浏览器对其 JavaScript 即时 (JIT) 编译器使用了此功能」。
5. **tvOS 的 JIT 条目**：手册全库 grep「即时」仅命中 4 页（`15bfe098e`、`8b776536b`、`c46f3562c` 属"即时远程擦除"与本题无关、`ebb113be1` 属 Rosetta 2），**无 tvOS JIT 条目**，本篇不据外部资料补写。

**这一段是本章的核心结论**：在 iOS 上，"JIT 例外"是一处**由 Apple 保留、给点名组件使用**的缺口，而不是一项第三方可申请的能力；任何"iOS App 可以开启 JIT"的表述都缺少一手依据。

## 7.5 撤销与收窄：六道已见的收口

把第 5、6 章的收口条件汇总成一张"例外被收窄到多远"的清单（全部 A- 级源码，除最后两条为 A/A-）：

| # | 收口 | 一手落点 |
|---|---|---|
| 1 | 每地址空间**单区域** | `code_signing.h:706-713`；文章侧「it can only create one memory region with the MAP_JIT flag」 |
| 2 | 不得与文件映射、共享、固定地址、`MAP_TPRO`、弹性签名并用 | `kern_mman.c:403-424` |
| 3 | 非 macOS 上必须一次到位 RWX，之后**不许降级** | `vm_map.c:3219-3262` |
| 4 | 跨拷贝/派生时收回"用于 JIT"标记 | `vm_map.c:892-915` + `VM_MAP_POLICY_ALLOW_JIT_COPY()` |
| 5 | 白名单化：采纳 `jit-write-allowlist` 后切换函数被禁用，只允许枚举回调 | man page ENTITLEMENTS + SECURITY CONSIDERATIONS（A-）；文章 `your app can no longer call pthread_jit_write_protect_np()`（A） |
| 6 | 冻结与 `dlopen` 限制：默认不允许运行时动态载入的框架/动态库持有 JIT 写回调；`-freeze-late` 需显式冻结 | man page 逐字 + 文章逐字（违反即 crash / 进程被终止） |

**第 3 条与第 5–6 条方向相反，这正是本机制最容易被误读之处**：页表层面给了最宽的权限（RWX 不许降级），而**运行时层面把"谁能把它变成可写"收到极窄**（线程开关 + 白名单回调 + 冻结）。读成"RWX 就是没防护"，或读成"不许降级说明权限只增不减、防护形同虚设"，两头都错；正确读法在第 6.1 节的两层模型里。

## 7.6 锁定模式：三份一手文本必须一起引

锁定模式是本章唯一"**用户侧可收紧例外**"的成文手段，三份文本措辞不同、不可互相替代：

1. **唯一逐字点名 JIT 的一手页**——《守护你的个人安全》手册页，https://support.apple.com/zh-cn/guide/personal-safety/ipsd5baf79d0/web ，页面标注发布日期 **2025 年 10 月**（A 级）：

   > 网页浏览： 即时 (JIT) JavaScript 编译等某些复杂的网络技术会停用，除非用户将受信任的站点从锁定模式中排除。

2. **支持文章**《关于"锁定模式"》，https://support.apple.com/zh-cn/105120 ，页面标注**发布日期 2026 年 09 月 18 日**（A 级），措辞更宽、不含"JIT"字样：

   > 网页浏览： 某些复杂的网页技术会被阻止，这可能会导致一些网站载入速度变慢或者无法正常运行。此外，网页字体可能无法显示，图像可能会被图像缺失图标替代。

   > 当你在 Safari 浏览器中排除某个网站后，相关网站将不再受 WebKit 相关限制的约束与影响。

3. **《Apple 服务的锁定模式安全性》手册页**（secid `2437264f0`，发布日期 **2024 年 05 月 07 日**，A 级）只给范围清单，**全页未出现"JIT/即时编译"字样**：

   > 锁定模式是一项可选的极端保护模式，专为极少数个人用户设计，这些人可能会由于他们的身份或工作而被一些最复杂的数字威胁所针对，如针对性雇佣间谍软件。……打开锁定模式时，设备不会像平时一样正常工作。为减小潜在可能被利用的攻击面，某些 App、网站和功能会受到严格限制以保证安全性，部分体验可能根本不可用。

   > 锁定模式权衡措施以牺牲功能和/或性能换取安全性的提高。这些权衡措施会影响：后台服务 / 连接性 / 设备管理 / FaceTime 通话 / GameCenter / 邮件 / 信息 / 照片 / Safari 浏览器 / 系统设置 / WebKit

   同页另注逐字：`锁定模式在 iOS 16 、 iPadOS 16 、 macOS 13 、 watchOS 10 或更高版本中可用。`

**正确的搭配写法**：`2437264f0` 证明"Safari 浏览器/WebKit 在权衡范围内"；`ipsd5baf79d0` 才逐字点名"即时 (JIT) JavaScript 编译等某些复杂的网络技术会停用"；105120 用"某些复杂的网页技术会被阻止"并给出**按站点排除**的机制。三者不可互相替代，也不宜写成"锁定模式关闭 JIT"这种超出引文的断言——一手文本的形态是"**停用某些复杂网络技术，除非把受信任站点从锁定模式中排除**"，因此**排除项本身就是一个可被打开的口子**（第 9 章的削弱面清单里，这是唯一由用户手工控制的一项）。

**缺口**：`security.apple.com/blog/lockdown-mode/` 与 `blog/announcing-lockdown-mode/` 抓取返回的是 Apple Security Research 通用落地页正文（未取得文章主体），其中与 JIT/网页技术相关的逐字句未取得；blog RSS 中 grep `JIT` 命中 0。**因此本篇不引用博客侧的锁定模式 JIT 表述。**

## 7.7 本章要点

1. iOS 侧的 JIT 例外**由 Apple 保留并点名给 Safari 的 JavaScript JIT 编译器**；第三方渠道在 developer 文档里零命中，`allow-jit` 的 `platforms` 字段为空数组。
2. 授予的成文强度：「The Hardened Runtime disallows this by default, because **it creates a security risk**」；未获授权时依赖 JIT 的框架「may fall back to an interpreter」。
3. 手册那句"动态代码签名授权"在源码里对应 **JIT 区域与调试区域两个概念**，审计时不得混同。
4. 例外被六道收口压窄（单区域 / 组合禁令 / 不许降级 / 拷贝重置 / 白名单 / 冻结与 `dlopen` 限制），其中"页表给最宽、运行时收最窄"这一反向结构是误读高发点。
5. 锁定模式是三份一手文本的合成，且**按站点排除**是用户可操作的口子；博客侧未取到逐字，本篇不引。


# 8. 页级按需校验：载入时才做的签名检查

## 8.1 判定发生在页错误时，不发生在启动时

第 2 章立住的前提（可执行性由签名授予）需要一个执行时机上的答案：**这个授予是在什么时候被检查的？** 手册与技术说明给的答案是同一条：**载入时、按需、逐页**。

《iOS、iPadOS 和 visionOS 中的 App 安全性介绍》页（`f49cad4db`，2024-12-19，A 级）：

> 运行时，所有可执行内存页的代码签名检查会在载入时进行，以帮助确保 App 自安装或上次更新之后未被修改过。

《App 代码签名过程》页（`7c917bf14`，2024-12-19，A 级）就动态库这一子路径重复了同一时机：

> 为保护系统并防止其他 App 在其地址空间中载入第三方代码，系统将为启动时所有链接到进程的动态资源库执行代码签名验证。

TN3126《Inside Code Signing: Hashes》（https://developer.apple.com/documentation/technotes/tn3126-inside-code-signing-hashes ，修订记录最新条目 **2024-02-06**，A 级）把这件事的机制说清：

> `This technote explains how code signing uses hashes to protect the code's executable pages, resources, and metadata from tampering.`

> `Within a code directory there are a set of **hash slots**. … This means there are 14 per-page hash slots and 7 special hash slots.`

> `The non-negative slots are for per-page hashes: 0 is the hash for the first page of code, 1 for the second, and so on.`

> `This per-page architecture allows the kernel to check each page as it's loaded into memory.  If a process takes a page fault on a memory mapped file that's code signed then, as part of satisfying that fault, the kernel may choose to verify the hash of the page's contents.  This allows the system to run a code-signed executable and check its code signature lazily (in the computer science sense of that word).`

这段里三个词的强度要照着引文写，不能加码：「the kernel **may choose to** verify」「check its code signature **lazily**」。也就是说，**按页校验是页错误处理路径上的一个可选动作，而非每次取指都做一次的重检查**。第 8.4 节会看到 Apple 自己把这句话说成"macOS 并不总是校验"。

## 8.2 与"签名系统宗卷"的分界

《签名系统宗卷安全性》页（`d698747c9`，发布日期 **2022 年 5 月 13 日**，A 级）给出两种保护的分工，这是评审里常被混为一谈的一对：

> 代码签名仍存在并由内核执行。签名系统宗卷在从内置存储设备上读取任何字节时提供保护。相反，代码签名在 Mach 对象在内存中映射为可执行时提供保护。所有读取和执行路径上的可执行代码均会受到 SSV 和代码签名的保护。

关键句是第二句的对照：**SSV 保护"从内置存储读取字节"，代码签名保护"Mach 对象在内存中映射为可执行"**。因此"这个页能不能执行"的判定属于代码签名一侧，而"这个字节有没有被换过（在存储上）"属于 SSV 一侧。两者都覆盖执行路径，但**作用时刻不同**，第 14 章判定链据此分两条勾。

## 8.3 校验并不默认开启：TN3126 的那三句

TN3126（A 级）里最重要、也最容易被漏掉的三句：

> `macOS doesn't *always* check code as it's paged in.  One key feature of the **hardened runtime** is that it opts the process into this checking by default.  The Disable Executable Memory Protection Entitlement opts you out of this and other security features.  **Don't do that!**`

> `The Disable Executable Memory Protection Entitlement only has this effect on Intel code. For Apple silicon code, this entitlement leaves page protection enabled, making it equivalent to the Allow Unsigned Executable Memory Entitlement.`

第一句做三件事：① 承认"并不总是校验"；② 把"默认按页校验"定位成**硬化运行时的一项特性**（即它是被 opt-in 的，而不是无条件成立的）；③ 指出存在一个会把这项连同其它安全特性一起关掉的授权，并用「**Don't do that!**」这句少见的官方语气作评价。

第二句给出**代际差异**，且差异方向出人意料：这条授权在 Intel 代码上能关掉按页保护，在 Apple 芯片代码上**不会**关掉页保护，其效果变成等同于"允许未签名可执行内存"那条授权。第 9 章与第 10 章把这条列为"同一键名在两代硬件上语义不同"的范本。

**引用纪律**：TN3126 通篇是 macOS 示例，但它自己也写了外许可：

> `The examples in this technote are taken from macOS, but these concepts apply to all Apple platforms.`

因此本篇把"硬化运行时才默认按页校验"**限定在 macOS**（因为该句主语是 macOS），把"按页哈希结构 / 延迟校验"作为跨平台概念引用（因为有上面那句），并明确标注：iOS 上等价开关为何，Apple 未成文（第 8.7 节）。

同一页另两处哈希槽与版本事实（A 级，逐字）：

> `Slot -1 holds a hash of the [*Info.plist*] … Slot -3 holds a hash of the resources. … Slot -5 holds a hash of the entitlements.`

> `Code that targets macOS 10.12 or later has a single code directory that uses SHA-256 hashing … Apple silicon debuted with macOS 11, and thus Apple silicon code never includes a legacy SHA-1 code directory.`

TN 自己的限定也必须一起引（这是本篇处理" internals 类技术说明"的通用纪律）：

> `The *Inside Code Signing* technotes discuss code signing details that aren't considered API.  The structure of a code signature has changed numerous times in the past and may well change again in the future.  Don't encode this information in your product.`

因此本篇中所有 TN3126 的结构细节（槽位编号、`14+7`）一律按"**非 API、可能变化**"引用，不得用于任何外部工具的判定依据。示例输出里的 `Page size=4096`、`hashes=14+7 location=embedded` 属 TN 的**样例数值**，不是承诺（第 8.7 节）。

## 8.4 Rosetta 2：同一模式的跨架构对照

《搭载 Apple 芯片的 Mac 上的 Rosetta 2》页（`ebb113be1`，发布日期 **2021 年 2 月 18 日**，A 级）逐字：

> 然后转译存根会在映像执行期间转译 x86_64 页。此转译完全在该过程中进行。内核仍会根据页出错所在二进制文件中所附的代码签名来验证每个 x86_64 页的代码哈希值。如果某个哈希值不匹配，内核会实施适用于该过程的修复策略。

> 如果原始 x86_64 映像的代码目录哈希值与编码到 AOT 转译成品签名中的哈希值不匹配，此结果会被视为等同于无效代码签名，此时会执行适当的实施操作。

这两句的价值在于它们把"**按页出错时的哈希验证**"与"**不匹配时的处置**"两个动作都写成了成文句子，并给出一个可迁移的结构：**转译产物也参与同一套校验**（代码目录哈希不匹配 = 等同于无效代码签名）。这是手册里对"执行前逐页验内容"最直白的描述，虽然它的适用对象是 Rosetta 2 而非原生 iOS 页——本篇据此只作对照，不写成 iOS 的直接证据。

## 8.5 内核实现：调用链与六处逐字

调用链（xnu main `f6217f89` / tag `xnu-12377.121.6`，A- 级）：

`vm_fault` → `vm_fault_enter` → `vm_fault_validate_cs` → `vm_fault_cs_check_violation` → `vm_page_validate_cs` → `cs_validate_page`

1. 函数头注释（`osfmk/vm/vm_fault.c:2780-2795`）逐字：

   > `Check if the page being entered into the pmap violates code signing.`

2. 主判定（`:2803-2835`）：`if (!cs_bypass && vm_fault_cs_need_validation(...))` → `vm_page_validate_cs(m, fault_page_size, fault_phys_offset)`。

3. **谁不参与校验**（`vm_fault_cs_need_validation`，`:2712-2735`）逐字两条：

   > `if (pmap == kernel_pmap) { /* 1 - not user space */ return false; }`

   > `if (!page_obj->code_signed) { /* 3 - page does not belong to a code-signed object */ return false; }`

   即**内核自身页与被标记为未签名对象的分页内容不在这条路径上**。（注释编号跳号是源码原样，本篇按原样引用。）

4. **有监控器时 VM 不再重复校验**（`:2815-2828`）注释逐字：

   > `When we have a code signing monitor, the monitor will evaluate the code signature for any executable page mapping. No need for the VM to also validate the page. In the code signing monitor we trust :)`

5. **页级三态记账**（`osfmk/mach/memory_object_types.h:491-496` + `vm_fault.c:8925-8950`）：`#define VMP_CS_BITS 4`、`VMP_CS_ALL_FALSE 0x0`、`VMP_CS_ALL_TRUE 0xF`，页结构体逐字：

   > `cs_validated:VMP_CS_BITS, /* CODE SIGNING: page was validated */ cs_tainted:VMP_CS_BITS, /* CODE SIGNING: page is tainted */ cs_nx:VMP_CS_BITS, /* CODE SIGNING: page is NX */`

   校验调用点：`/* verify the SHA1 hash for this page */` 后 `cs_validate_page(vnode, pager, mo_offset, …, &validated, &tainted, &nx)`，结果写回上述三位。**注释里的 SHA1 与 TN3126 的 SHA-256 并存**——这是**注释陈旧**，不得据此推断运行时算法；第 15 章按口径冲突登记（属注释与文档的冲突，不是两个 Apple 文档互相矛盾）。

   分块校验路径另有一处（`:9105-9118`）：`/* verify the signature for this chunk */ … cs_validate_range(..., chunk_size, &tainted)`。**4 位子区间 + chunk 循环**是"VM 页大于 4K 时按块记账"的证据，但**不足以断言任何具体页大小搭配**（第 8.7 节）。

6. **NX 页不许映射为 EXEC**（`:2838-2871`）：注释逐字

   > `executable pages because that would provide a way to inject unsigned code`

   代码 `if (cs_enforcement_enabled && vm_fault_cs_page_nx(m, …) && (prot & VM_PROT_EXECUTE)) { … printf("page marked to be NX, not letting it be mapped EXEC\n"); … return KERN_CODESIGN_ERROR; }`（打印受调试位 `cs_debug` 控制）。

7. **污染页即视为未签名代码**（`:2873-2891`）注释逐字：

   > `- pmapping an unsigned page executable - this means unsigned code;`
   > `- writeable mapping of a validated page - the content of that page can be changed without the kernel noticing, therefore unsigned code can be created`

   判定顺序：`if (cs_bypass) { *cs_violation = FALSE; } else if (VMP_CS_TAINTED(...)) { *cs_violation = TRUE; } else if (!cs_enforcement_enabled) { … }`。**"可写映射本身"就足以让一页被判为污染**，这正是第 5.7 节那条评论句的实现形态。

8. **拒绝无效页时的内核日志字段**（`:3063-3068`，逐字格式串）：

   > `CODE SIGNING: process %d[%s]: rejecting invalid page at address 0x%llx from offset 0x%llx in file "%s%s%s" (cs_mtime:%lu.%ld %s mtime:%lu.%ld) (signed:%d validated:%d tainted:%d nx:%d wpmapped:%d dirty:%d depth:%d)`

   这一条是第 12 章可观测性里最有用的字段清单：**`signed / validated / tainted / nx / wpmapped / dirty / depth` 七个状态位在同一行日志里**，且带地址、文件内偏移与 mtime 对照。

9. **处置由进程级标志决定**（`bsd/kern/kern_cs.c:248-312`，`cs_invalid_page`）注释逐字：

   > `/* CS_KILL triggers a kill signal, and no you can't have the page. Nothing else. */`
   > `/* CS_HARD means fail the mapping operation so the process stays valid. */`

   `CS_KILL` 命中 → `flags |= CS_KILLED; cs_procs_killed++; send_kill = 1;`；否则清 `CS_VALID`（`cs_procs_invalidated++`、`cs_process_invalidated(NULL)`）；最终 `threadsignal(current_thread(), SIGKILL, EXC_BAD_ACCESS, FALSE);`。相关标志与接口（`bsd/sys/codesign.h:41-61`，A- 级逐字）：`CS_OPS_MARKINVALID 1 /* invalidate process */`、`CS_OPS_MARKHARD 2 /* set HARD flag */`、`CS_OPS_MARKKILL 3 /* set KILL flag (sticky) */`、`CS_OPS_SET_STATUS 9`、`CS_OPS_CLEAR_LV 15 /* clear the library validation flag */`、`CS_OPS_VALIDATION_CATEGORY 17`；`kern_cs.c:220 / :245` 两处读取式 `flags = proc_getcsflags(p) & ~(CS_KILL | CS_HARD);`、`return (proc_getcsflags(p) & (CS_KILL | CS_HARD)) == 0;`。

   **这两句注释是整章最有解释力的对照**：`CS_KILL` = 给你杀信号且页不给；`CS_HARD` = 让映射操作失败以保持进程有效。同一"发现无效页"事件因此有**两种截然不同的用户可见后果**，取决于进程被赋予的标志。第 12 章解释崩溃报告形态时必须带上这一条。

## 8.6 后果矩阵

| 触发形态 | 一手可见结果 | 出处 | 档位 |
|---|---|---|---|
| 执行了被标 NX 的页 | 用户态 `SIGBUS` 或 `SIGSEGV` | `mprotect.2:83` | A- |
| 内核拒绝把 NX 页映射为 EXEC | 返回 `KERN_CODESIGN_ERROR`；调试位开启时打印 `page marked to be NX, not letting it be mapped EXEC` | `vm_fault.c:2838-2871` | A- |
| 页校验判定为污染/无效，进程带 `CS_KILL` | `SIGKILL`（伴随 `EXC_BAD_ACCESS`），页不交付 | `kern_cs.c:248-312` | A- |
| 同上，进程带 `CS_HARD` | 映射操作失败，进程保持有效 | 同处注释 | A- |
| 存在代码签名监控器 | VM 不再重复校验该页，由监控器评估可执行页映射 | `vm_fault.c:2815-2828` | A- |
| `used_for_jit` / 监控器豁免 / 弹性签名 | `cs_bypass` 为真，该校验被整体跳过 | `vm_fault.c:4773-4777` | A- |

**手册侧的对应强度只有一句**：「所有可执行内存页的代码签名检查会在载入时进行」（`f49cad4db`，A）。上表其余全部是实现层事实，本篇在任何"结论"章节引用它们时都标 A-。

## 8.7 本节缺口（未核实 / 零命中）

1. **iOS 页哈希粒度是否为 16 KB**：TN3126 全篇只出现示例 `Page size=4096`（macOS），grep `16384` / `16K` / `pagesize` 在该 TN 命中 0；《Apple 平台安全保护》手册未给数字。xnu 侧只能证明"VM 页内按 chunk 记账、位宽 4"，**不足以断言 16K/4096 的具体搭配**。→ 记为未核实，不写入量化基线表。
2. **「需求页 / 强制页」（demand page / mandatory page）等中文术语**：手册全库 grep 命中 0（本篇在同一镜像上复核：`需求页` 0 页、`强制页` 0 页）。Apple 一手文本对页校验只用「代码签名检查会在载入时进行」「验证每个 x86_64 页的代码哈希值」这类表述。
3. **iOS 上"默认开启按页校验"的等价开关**：TN3126 的 opt-in 语句主语是 macOS 的 hardened runtime；iOS 侧是否存在同名概念、由谁 opt-in，**一手文本未给**。
4. **`CS_KILL` / `CS_HARD` 是否可由开发者配置、以及各平台上进程默认取哪一支**：源码给出了标志与 `CS_OPS_*` 接口，但成文说明缺失（第 15 章 N 类）。
5. **`KERN_CODESIGN_ERROR` 在崩溃报告里以何字段呈现**：需与第 12 章的字段清单对照，本篇未取到"该错误码 → 报告字段"的官方映射句。

## 8.8 本章要点

1. 判定时机是**载入时、按需、逐页**；Apple 自己的措辞是「the kernel **may choose to** verify」与「lazily」，强度止于"可选动作"。
2. **默认开启不是无条件的**：TN3126 明文 macOS 不总是校验，"默认按页校验"是硬化运行时的特性，且存在一条把它连同其它安全特性一起关掉的授权（官方语气「**Don't do that!**」）。
3. 该授权**在两代硬件上语义不同**：Intel 代码上关页保护，Apple 芯片代码上不关、效果转为"允许未签名可执行内存"。
4. 内核侧三条"不参与"（内核页、非签名对象、`cs_bypass`）与两条处置分支（`CS_KILL` / `CS_HARD`）决定了"发现无效页"的可见后果并不唯一。
5. **可写映射本身就足以让页被判污染**（源码注释逐字：内容可在内核未察觉的情况下改变，因此等于制造未签名代码）——这是把第 5、6、8 章串成一条因果链的那句话。
6. 校验结构细节属"非 API"，Apple 明确要求不要固化进产品；页大小只有 macOS 示例值，iOS 粒度未核实。


# 9. 覆盖面会被什么收窄：配置审计清单

## 9.1 本章的边界：只登记"Apple 自己点名的开关"与其后果句

先把三条边界写在最前面，因为这一章最容易被读成它不愿意成为的东西：

1. **本章只登记 Apple 公开文档点名的构建能力开关、授权属性与平台策略**，以及 Apple 自己写明的后果句。不含操作步骤、不含配置教程、不含代码，更不含任何针对具体设备的判定与利用方法。用途只有一个：拿到一份 entitlements 或一个 Xcode 能力配置，判断"执行限制这一层的覆盖面有没有被自己人收窄"。
2. **"削弱面"这个词指的是覆盖面收窄，不是漏洞。** 六项例外里每一项都是 Apple 为真实需求（JIT、调试器、插件宿主）提供的通道，Apple 的原文语气是"carefully consider whether your app needs this exception"，本篇完整照抄这一语气，不写成"不应开启"。
3. **本章的结论只能是"该键出现时 Apple 承诺了什么后果"**，不能是"该键出现时保护完全失效"。后者需要实现层证据，而实现层证据（第 8 章）本身是 A- 级，不构成对外承诺。

## 9.2 macOS 侧的六个可逐项关闭的口子

《Hardened Runtime》文档页（https://developer.apple.com/documentation/security/hardened-runtime ，页脚 © 2026，A 级）的总纲句逐字：

> `The Hardened Runtime doesn't affect the operation of most apps, but it does disallow certain less common capabilities, like just-in-time (JIT) compilation. If your app relies on a capability that the Hardened Runtime restricts, add an entitlement to disable an individual protection.`

关键在最后一句：这套模型的官方动词是 **disable an individual protection**——一次关一项。下表六项全部取自各授权属性文档页的逐字文本，且每项 `metadata.platforms` 字段实测逐字为 `[{"introducedAt":"10.7","name":"macOS"}]`（本地缓存 `_tmp/pe/ent_*.json`）——**六项一律 macOS-only，iOS 一项都没有**。

| # | 授权键（逐字） | Xcode 界面名（逐字） | Apple 写明的"放开后得到什么"（逐字） | Apple 写明的风险句（逐字） |
|---|---|---|---|---|
| 1 | `com.apple.security.cs.allow-jit` | Allow Execution of JIT-compiled Code | may create writable and executable memory using the `MAP_JIT` flag | The Hardened Runtime disallows this by default, **because it creates a security risk.** |
| 2 | `com.apple.security.cs.allow-unsigned-executable-memory` | Allow Unsigned Executable Memory | may create writable and executable memory **without the restrictions imposed by using the `MAP_JIT` flag** | Including this entitlement **exposes your app to common vulnerabilities in memory-unsafe code languages.** Carefully consider whether your app needs this exception. |
| 3 | `com.apple.security.cs.disable-executable-page-protection` | Disable Executable Memory Protection | whether to **disable all code signing protections** while launching an app, **and during its execution** | is an **extreme entitlement** that removes a fundamental security protection from your app, **making it possible for an attacker to rewrite your app's executable code without detection.** Prefer narrower entitlements if possible. |
| 4 | `com.apple.security.cs.disable-library-validation` | Disable Library Validation | whether the app loads arbitrary plug-ins or frameworks, **without requiring code signing** | Because library validation is such an important security-hardening feature, **Gatekeeper runs extra security checks on programs that have it disabled.** |
| 5 | `com.apple.security.cs.allow-dyld-environment-variables` | Allow DYLD Environment Variables | may be affected by dynamic linker environment variables, **which you can use to inject code into your app's process** | Injecting libraries or changing search paths with this feature **may still require another entitlement**. |
| 6 | `com.apple.security.cs.debugger` | Debugging Tool | whether the app is a debugger and may attach to other processes or get task ports | the debugger receives a **10-hour session** before authorization expires. |

表中与"执行限制"直接相关的是第 1、2、3 项：它们分别对应第 5 章的正门（`MAP_JIT`）、绕过正门的宽门（不受 `MAP_JIT` 约束的未签名可执行内存）、以及把签名保护整体摘掉（launch 期间与执行期间）。第 4 项属于载入准入而非页权限，但它是第 2、5 项的**前置放大器**，故一并登记。第 5、6 项本篇只登记不展开——前者是 dyld 侧注入口，后者是"调试区域"来源（第 7.3 节）。

## 9.3 三条关系：这三项不能当作彼此独立的勾

审计里最常见的错误是把六项当成六个独立开关。Apple 的原文给出了两条依赖与一条代价：

**其一，第 3 项包含第 2 项，但不包含第 4 项**（`com.apple.security.cs.disable-executable-page-protection` 页，A 级，逐字）：

> `The entitlement effectively encompasses the behavior provided by the [Allow Unsigned Executable Memory Entitlement], but not the [Disable Library Validation Entitlement].`

→ 一句判定：**同时勾选 2 与 3 属于冗余**；而**勾了 3 不等于放开了库验证**。审计脚本里若把 3 拆成"2 + 4"来统计，会把风险面算大一项。

**其二，库验证的准入条件只有两条**（第 4 项页，A 级，逐字）：

> `This security-hardening feature prevents a program from loading frameworks, plug-ins, or libraries unless they're either **signed by Apple** or **signed with the same Team ID** as the main executable.`

→ 这一句同时是第 8 章"签名页才可执行"的**载入侧对偶**：执行位由签名授予，载入权由 Apple 签名或同 Team ID 授予。关掉第 4 项时，被放开的是后者，前者依然由签名体系约束——**不要把库验证读成"关掉就能执行未签名代码"**，Apple 原文只说"loads arbitrary plug-ins or frameworks, without requiring code signing"，即"不要求签名"，落在载入准入层。

**其三，第 5 项不足以单独生效**（A 级，逐字）：

> `Injecting libraries or changing search paths with this feature may still require another entitlement. For example, you also need the [Disable Library Validation Entitlement] if an injected library isn't signed with the expected team ID.`

→ Apple 在这里**主动写明了两项的组合关系**。审计时看到单独的 `allow-dyld-environment-variables` 应记为"能力受库验证约束"，而不是"注入通道已打开"。

## 9.4 缺省代价句：为什么开发商更可能勾第 2 项而不是第 1 项

第 1 项页逐字给出**不加的代价**（A 级）：

> `Without the Allow execution of JIT-compiled code entitlement, frameworks that rely on just-in-time (JIT) compilation may fall back to an interpreter. Other code using JIT compilation may crash or behave in unexpected ways.`

第 2 项页同样有不加的后果句：`Otherwise, the app might crash or behave in unexpected ways.` 两句合起来解释了现实里的分布：**功能降级压力（解释器变慢、崩溃）落在开发者身上，而收窄幅度的差异（第 1 项受 `MAP_JIT` 六道收口约束，第 2 项明确"without the restrictions"）需要审计者自己去读原文才能察觉**。

同页一条迁移指令（A 级，逐字）值得单独登记：

> `Digital rights management (DRM) solutions that currently use unsigned executable memory should instead change to using the MAP_JIT flag and the entitlement.`

Apple 承认存在"当前使用未签名可执行内存"的实现，并把它们指向正门。对审计的含义是：**在现网样本里看到第 2 项时，"历史遗留的 DRM 迁移未完成"是 Apple 自己给出的一个合法解释**，不应直接判定为疏忽。

## 9.5 页级按需校验被关掉的唯一官方路径

第 8 章已引 TN3126 三句，此处按"削弱面"重排（A 级，https://developer.apple.com/documentation/technotes/tn3126-inside-code-signing-hashes ，修订记录最新条目 2024-02-06，逐字）：

> `macOS doesn't *always* check code as it's paged in. One key feature of the hardened runtime is that it opts the process into this checking by default. The Disable Executable Memory Protection Entitlement opts you out of this and other security features. Don't do that!`

> `The Disable Executable Memory Protection Entitlement only has this effect on Intel code. For Apple silicon code, this entitlement leaves page protection enabled, making it equivalent to the Allow Unsigned Executable Memory Entitlement.`

两句给出本章最重要的一条量化事实：**同一个键在不同架构上的削弱幅度不同**。

| 代码架构 | 该键出现时 | 出处 |
|---|---|---|
| Intel（x86_64） | 按页校验不再默认执行（"opts you out of this"），页保护被关 | TN3126，A |
| Apple 芯片（arm64） | **页保护保持开启**，效果退化为等同于第 2 项（可拿未签名可执行内存） | TN3126，A |

→ 因此第 3 项是六项里唯一被 Apple 逐字描述为**使改写代码"不被检测到"**（`rewrite your app's executable code without detection`）的项，也是唯一效果需按芯片世代分别记账的项。`Don't do that!` 是 Apple 文档里少见的直接劝阻语气，本篇按原样引用，不转译成"建议不要"。

## 9.6 收紧方向的两项：把"削弱面"反过来读

覆盖面既能被关窄，也能被主动收窄。两项已见的收口在本章登记，因为**缺席本身应当作为审计发现**：

- **`com.apple.security.cs.jit-write-allowlist` 缺席**＝默认形态是"任意线程可自行切换执行权限"。libpthread 的 `pthread_jit_write_protect_np.3`（A- 级，`.Dd May 1, 2020`）SECURITY CONSIDERATIONS 段逐字：

  > `Some attacks may attempt to defeat JIT write protection by inducing unexpected/unintended calls to pthread_jit_write_protect_np, e.g. dynamically via dlsym.`

  > `By default, JIT write callbacks are not allowed in frameworks/dylibs dynamically loaded at runtime via dlopen.`

  → 审计含义：**JIT 宿主进程若被 `dlopen` 载入第三方 dylib，回调白名单默认并不覆盖它**；这一条即使不采纳 allowlist 也成立，采纳 allowlist 后才具备"枚举白名单回调"的能力（代价见第 7.5 节第 5 条：`pthread_jit_write_protect_np()` 本身被禁用）。
- **`-freeze-late`**：man page 与 developer 文章均把"冻结"写成需要显式动作的选项；不冻结＝白名单可在运行期继续变。属于"未收口即风险敞口"的登记项。

## 9.7 iOS 侧的削弱面：可枚举项远少于 macOS

这是本章和流传口径差距最大的一节。**iOS 上不存在"可逐项关掉执行防护"的开发者开关**，六项 Runtime Exceptions 的平台标注全部为 macOS（9.2）。逐条登记 iOS 侧唯一可谈的几点：

1. **申请 W+X 在 iOS 上不报错，而是拿到"可写但不可执行"**（`mmap.2:217-224`，A- 级，逐字）：

   > `Without this flag an attempt to create a mapping with both PROT_WRITE and PROT_EXEC set will fail with MAP_FAILED on macOS. A writable, but not executable mapping is returned on iOS, watchOS and tvOS.`

   → 后果：iOS 的准入判定不发生在"报错/不报错"这一层，而发生在代码签名监控器：`bsd/kern/kern_mman.c:479-482`（A- 级）`vm_map_csm_allow_jit(user_map) != KERN_SUCCESS` → 打印 `[%d] code signing monitor denies JIT mapping` → `EPERM`。**"削弱 iOS 执行限制"在公开 API 层没有对应的开发者开关**，只有"是否被授予那个未公开键名的授权"这一条判定。
2. **iOS 侧唯一的例外通道**是手册所说的「Apple 专有的动态代码签名授权」（`15bfe098e`，A 级）。键名未公开（`com.apple.security.dynamic-codesigning` 文档页实测 HTTP 404，见附录 A 的抓取失败登记）；可见证据只有 libpthread iOS 测试 plist 里的 `<key>dynamic-codesigning</key><true/>`（A- 级）。→ **审计位置：嵌入的 entitlements 中是否出现该键**。但 Apple 未公开其申请条件，**"出现即异常"不能作为结论**，只能记为"需人工确认来源"。
3. **锁定模式的按站点排除**是 iOS 上唯一由**用户侧**收窄（并可被用户侧再打开）执行限制的公开开关（A 级，两句逐字已在第 7.6 节给出：「当你在 Safari 浏览器中排除某个网站后，相关网站将不再受 WebKit 相关限制的约束与影响」／「即时 (JIT) JavaScript 编译等某些复杂的网络技术会停用，**除非用户将受信任的站点从锁定模式中排除**」）。→ 它是本章清单里**唯一不属于开发者可控项**的一条，审计时归属"设备配置基线"而非"构建配置"。
4. **内核实施位不是开发者可选项**。`CS_KILL` / `CS_HARD` 决定"发现无效页"的处置强度（A- 级，`kern_cs.c:248-312` 注释逐字：`CS_KILL triggers a kill signal, and no you can't have the page.` / `CS_HARD means fail the mapping operation so the process stays valid.`），`vm_fault.c:2873-2891` 的分支以 `cs_enforcement_enabled` 为条件。**手册与 developer 文档均未把该标志列为可配置项**（记未核实，第 15 章 N 类）。本篇只登记形态，不登记"如何改变它"。

## 9.8 一张"看到键该怎么判"的对照表

判据本身来自四条 A 级原文，先列出：

- 布尔默认值与写入规则（《Hardened Runtime》页，A 级，逐字）：`The default value of these Boolean entitlements is false. When Xcode signs your code, it includes an entitlement only if the value is true. … Don't include an entitlement if the value is false.`
- 归属粒度（同页，A 级，逐字）：`You add entitlements only to executables. Shared libraries, frameworks, and in-process plug-ins inherit the entitlements of their host executable.`
- 系统扩展禁令（同页，A 级，逐字）：`macOS refuses to load system extensions that use Hardened Runtime exception entitlements. There's one exception to this general rule: macOS allows the Allow execution of JIT-compiled code entitlement in non-DEXT system extensions.`
- 公证强制（同页 `[IMPORTANT]` 块，A 级，逐字）：`To upload a macOS app to be notarized, you must enable the Hardened Runtime capability.`

由此得到五条可执行判据：

| 观察 | 判读 | 依据 |
|---|---|---|
| 键**不存在** | 该保护**处于生效状态**（布尔默认 false 且不写入签名） | 9.8 第 1 条 |
| 键值为 `false` 却出现在签名里 | 与 Apple 的签名规则不符，属**构建脚本手工注入**，应回溯来源 | 9.8 第 1 条 |
| 只在 dylib/framework 的 plist 里看到键 | **无效审计结论**：授权只加在可执行文件，动态库继承宿主 | 9.8 第 2 条 |
| 系统扩展携带六项中除 `allow-jit` 外的任一项 | 与 Apple 成文禁令冲突（非 DEXT 系统扩展仅允许 `allow-jit`） | 9.8 第 3 条 |
| macOS 目标同时存在第 2 与第 3 项 | 冗余，按第 3 项记账，并按芯片分别评估削弱幅度 | 9.3、9.5 |

**注意第 3 行的措辞**：`hardened.txt` 的 identifier 在本轮抓取中被误解析过一次——该禁令句中"唯一例外"所引用的键，本地 JSON 的 identifier 逐字为 `com.apple.security.cs.allow-jit`，而更早一份简报把它记成了 "Allow DYLD environment variables entitlement"。**本篇以 identifier 为准**，并把这次纠正登记在第 15 章的"口径修正"条目里。

## 9.9 汇总判据

1. 削弱面**几乎全部集中在 macOS 的六项 Runtime Exceptions**；iOS 侧公开可枚举项为 0（唯一用户级例外是锁定模式的按站点排除）。
2. 每一项的后果**只引 Apple 原话**，不得自行扩写为"可被利用来执行任意代码"。
3. `disable-executable-page-protection` 是唯一被逐字描述为**使改写"不被检测到"**的项，且效果按芯片分叉。
4. **缺省即安全**：布尔默认 false、false 不写入签名 → 审计只需判断"键是否存在"，不需要判断值。
5. 反向收口（`jit-write-allowlist`、冻结）缺席属"未加强"，与"削弱"须分栏记账。

## 9.10 本章要点

1. macOS 是"默认开、可按项关"的例外模型，六个键全部标注 macOS 10.7 起；iOS 没有对应的可关项集合。
2. 六项之间有包含与前置关系（3 ⊇ 2 但不含 4；5 常需搭配 4），把它们当独立开关会算错风险面。
3. 按页校验被关掉的官方路径只有一条，且其**削弱幅度依芯片架构而不同**——这是本章唯一需要分平台记账的量化结论。
4. iOS 侧"是否被削弱"落在两处：动态代码签名授权是否存在（键名未公开、出现须人工确认来源），以及内核处置强度（`CS_KILL`/`CS_HARD`，非开发者可配）。
5. 审计判据以"键是否存在"为主，粒度必须到宿主可执行文件；系统扩展与公证各自有一条 Apple 成文的额外约束。

# 10. 平台不对称：iOS/iPadOS、macOS、watchOS、visionOS、Catalyst 与芯片代际

## 10.1 同一份内核代码，两套准入模型

第 2–9 章的引文里，macOS 与 iOS 的出现频率接近，但两者的**判定模型完全不同**：macOS 是"默认开、可按项关"，iOS 是"默认开、无可关项、例外由签名与监控器决定"。下表把差异钉在逐字出处上（除标注外均为 A 级）。

| 维度 | macOS | iOS / iPadOS | 逐字出处与档位 |
|---|---|---|---|
| 请求 W+X（不带 `MAP_JIT`） | `MAP_FAILED`（失败） | 返回**可写但不可执行**的映射（不报错） | A- 级 `mmap.2:217-224`：`A writable, but not executable mapping is returned on iOS, watchOS and tvOS.` |
| `MAP_JIT` 的前置条件 | 需 `com.apple.security.cs.allow-jit`，而该键**仅在采纳硬化运行时的 App 上才要求** | 手册口径为"内核检查 Apple 专有的动态代码签名授权"（键名未公开） | A- 级 `mmap.2:222-224`；A 级《Porting JIT》；A 级手册 `15bfe098e` |
| JIT 区域的权限形态 | 允许后续升降级 | **必须一次到位 RWX，此后不得降级** | A- 级 `vm_map.c:3219-3262`，注释含 `non-macOS: reject JIT regions without RWX` |
| 每线程写保护切换 | `pthread_jit_write_protect_np` 可用（采纳 `jit-write-allowlist` 后不可用） | **该函数在 iOS 上不可用** | A 级：`The pthread_jit_write_protect_np() function isn't available on iOS.` |
| W+X 的失败/降级策略 | 常规地址空间**直接失败**，alien/exotic 地址空间**降级为不可执行** | 同一份代码按 `VM_MAP_IS_ALIEN` 分叉 | A- 级 `vm_map_internal.h:923-941` |
| 内存保护的启用范围 | Intel：仅对采纳硬化运行时的 App；Apple 芯片：**所有 App** | 所有 App | A 级《Porting JIT》：`Apple silicon enables memory protection for all apps, regardless of whether they adopt the Hardened Runtime. Intel-based Mac computers enable memory protection only for apps that adopt the Hardened Runtime.` |
| 页表权限修改权的问责层 | **无 PPL、无 SPTM 覆盖** | PPL（A15/M2 之前的芯片）→ SPTM + TXM（之后） | A 级 `8b776536b`：「此保护在 macOS 上不提供，因为 PPL 只在所有执行代码都必须签名的系统上适用」 |
| 是否要求"所有可执行代码由 Apple 证书签名" | 手册**无此表述**；macOS 侧口径是"开发者 ID 签名 + 公证，供门禁判定" | 有该强制句 | A 级：iOS 句见 `7c917bf14`；macOS 句见 `3ad8e6e53` 逐字「不在 App Store 中分发的所有 App 必须由开发者使用 Apple 签发的开发者 ID 证书（和专用密钥）进行签名并由 Apple 公证，以便在默认的"门禁"设置中运行」 |
| 可按项关闭执行防护 | **六项 Runtime Exceptions**（第 9.2 节），`introducedAt` 全部 10.7、平台全部 macOS | 公开可枚举项为 **0** | A 级 entitlements 页 `platforms` 字段逐字 |
| 公证强制 | `To upload a macOS app to be notarized, you must enable the Hardened Runtime capability.` | 无对应概念 | A 级《Hardened Runtime》`[IMPORTANT]` 块 |

表格最后一行之外，最值得复读的是倒数第四行与倒数第三行的**组合**：macOS 既可以选择退出页保护（第 9.5 节），又没有页表问责层兜底；iOS 两者都没有——**"iOS 更严"在这一层的准确依据是"退出通道不存在 + 改页表的权力被收给可信组件"，不是"iOS 多了一道检查"**。

macOS 侧还有一条与本层直接相关的门禁行为（A 级，《macOS 中的门禁和运行时保护》`5599b66df`，发布日期 2024 年 12 月 19 日，https://support.apple.com/zh-cn/guide/security/5599b66df/web ）：

> 必要时，门禁会从随机的只读位置打开 App。这旨在防止自动加载与 App 一起分发的插件。

→ 这句是第 9.3 节库验证的**运行侧对偶**：库验证管"能不能载入"，门禁这句管"从哪儿载入"。两者都在削弱面清单之外（不可由同一组键关闭），但审计"插件目录是否被自动加载"时应一并查阅。

## 10.2 watchOS：唯一对口页只点名了三种技术

《watchOS 系统安全性》`c7d85209d`（发布日期 **2026 年 1 月 28 日**，https://support.apple.com/zh-cn/guide/security/c7d85209d/web ，A 级）逐字：

> Apple Watch 使用与 iOS 和 iPadOS 相同的许多基于硬件的平台安全性功能。例如，Apple Watch ：执行安全启动和安全软件更新 / 维持操作系统完整性 / 帮助保护设备上以及与配对的 iPhone 和互联网通信期间的数据

> 支持的技术包括"系统安全性"中所列技术（例如 KIP、SKP 和 SCIP）以及数据保护、钥匙串和网络技术。

**口径限制（必须保留）**：该页**只点名 KIP、SKP、SCIP**，全页未出现 Execute Never、APRR、PPL、SPTM、页表、代码签名或页校验（逐词 grep 命中 0，见附录 B）。因此"watchOS 也有 XN 与按页签名校验"只能是**跨页组合推论**，其依据三条：`87716a080`/`8b776536b` 的 SoC 世代矩阵包含 S3 至 S10/A19；`8b776536b` 总述句把 watchOS 列入适用范围；`7c917bf14` 标题含 watchOS。**不得写成 watchOS 专页的承诺。**

唯一一条把 watchOS 显式并列进来的一手硬证据来自 man page（A- 级）：`mmap.2` 把 watchOS 与 iOS、tvOS 同置于"返回可写不可执行映射"的平台集合内。

## 10.3 Apple tvOS：没有专页

本轮在本地手册镜像上做了一次标题级检索：TOC 去重后的 **207 个页面名中，含 "tvOS" 者为 0**。tvOS 只出现在跨平台总述句里，例如 `8b776536b` 逐字：「此类 SoC 包括 iOS、iPadOS、搭载 Apple 芯片的 Mac 上运行的 macOS、Apple tvOS、visionOS 和 watchOS」。

→ 于是 **tvOS 的执行限制没有专页**（负面结论，附录 B 登记）。可用的一手材料只有三处：`mmap.2` 的平台分叉句、`7c917bf14` 的标题（"iOS、iPadOS、Apple tvOS、visionOS 和 watchOS 中的 App 代码签名过程"），以及第 4.3 节对照表注释 2 的"macOS 以外所有平台"表述。手册全库 grep「即时」命中 4 页且**无 tvOS JIT 条目**（第 7.4 节第 5 项）。

**这一节的写法纪律**：不得写"tvOS 与 iOS 相同"；只能写"Apple 把 tvOS 列进总述句的适用平台集合，且 man page 在 W+X 处理上把 tvOS 与 iOS 同组，但没有 tvOS 层的执行限制专述"。

## 10.4 visionOS：与 iOS 同页族，但 XN 句没点它的名

visionOS 在标题层面与 iOS 同族：《iOS、iPadOS 和 visionOS 中的运行时进程安全性》`15bfe098e`、《iOS、iPadOS 和 visionOS 中的 App 安全性介绍》`f49cad4db`，PPL 与 SPTM 段落亦逐字含 visionOS。

**但 XN 段落逐字只写「iOS 和 iPadOS 使用 ARM 的 Execute Never (XN) 功能」**（`_tmp/sec15bfe098e.txt:246`）。→ "visionOS 有 XN"属**未核实外推**，本篇正文一律写作"该页标题含 visionOS，而 XN 句只点名 iOS 与 iPadOS"。这一处的价值在于示范：**页面标题的平台集合 ≠ 段落断言的平台集合**，本篇在第 2.3 节登记的"术语射程"就是为这类错位准备的。

## 10.5 Mac Catalyst 与"在 Mac 上运行的 iOS App"：无一手页

手册内 "Mac Catalyst" 命中 2 页，逐字上下文分别是商标清单（`2727afbbc`《版权和商标》）与自动填充的键盘差异（`7aefe77c3`《自动填充密码安全性》），**均与本层无关**。developer 侧 Catalyst 只作为**可用性标签**出现在平台元数据里，例如 MetricKit 的 `MXCrashDiagnostic` 标注 `Mac Catalyst from 14.0 deprecated 27.0`。

→ 结论：**Catalyst 下执行限制走 iOS 口径还是 macOS 口径，Apple 未公开表述（未核实）**。第 12 章可以引用 CrashDiagnostic 的平台清单里"含 Mac Catalyst"，但**不得据此推断执行权限模型**。这条缺口同时提醒：可观测性文档的平台集合与安全性文档的平台集合是两份不同的清单，不可互换。

## 10.6 Intel 与 Apple 芯片：两处一手分叉，加一处坦白

1. **能力范围句**（A 级，`8b776536b`，2026-01-28）逐字：「以下保护措施在运行 Apple 设计的受支持 SoC 的设备上可用」→ **Apple 设计的 SoC 是这些运行时保护的前提**。
2. **世代门**（A 级，`87716a080` 与 `8b776536b`，均 2026-01-28）：「自 Apple A11 仿生和 S3 后的 SoC 新增了硬件原语」；对照表列首格为 **A10**（该列对「快速权限访问限制」为不可用），其后依次 A11、S3 / A12-A14、S4-S10 / A15-A18、M1 / M2-M4 / A19 / M5。SPTM 另有一条注释逐字：「安全页表监控器 (SPTM) 在 A15 或后续芯片和 M2 或后续芯片的 SOC 中受支持，并在支持的平台上替代了页面保护层。」
3. **`disable-executable-page-protection` 的效果按芯片分叉**（A 级，TN3126）：「only has this effect on Intel code. For Apple silicon code, this entitlement leaves page protection enabled」——详见第 9.5 节的分平台记账表。
4. **内存保护的启用范围按芯片分叉**（A 级，《Porting JIT》）：逐字见 10.1 表倒数第五行。
5. **man page 的"硬件限制"坦白**（A- 级，`mmap.2:118-120`）逐字：

   > `Note that, due to hardware limitations, on some platforms PROT_WRITE may imply PROT_READ, and PROT_READ may imply PROT_EXEC.`

   → Apple 一手承认"某些平台上读可执行位无法单独强制"，但**未点名是哪些平台或哪代芯片**。本篇只把它引作"存在平台差异的自认"，**不得**据此断言 Intel 或某具体 SoC（附录 B 记为口径缺口）。
6. **Rosetta 2 的反向证据**（A 级，`ebb113be1`，2021-02-18）：Apple 芯片上 x86_64 页仍**逐页校验代码哈希**，且「如果原始 x86_64 映像的代码目录哈希值与编码到 AOT 转译成品签名中的哈希值不匹配，此结果会被视为等同于无效代码签名」→ **Intel 指令集代码在 Apple 芯片上并未失去页级校验**（第 8.4 节）。

## 10.7 三个平台一句话口径

- **iOS / iPadOS**：没有"可按项关掉"的开发者开关，准入由签名与代码签名监控器决定；违反的可见形态是"拿到不可执行映射"或内核拒绝（`EPERM` / `KERN_PROTECTION_FAILURE`）。
- **macOS**：默认开、可按项关（六项 Runtime Exceptions），且**页表完整性层（PPL/SPTM）缺席**，公证机制强制采纳硬化运行时。
- **watchOS / tvOS / visionOS**：Apple 把它们放进总述句与标题，但**没有一层的专述句**；跨页组合推论必须标注为推论。

## 10.8 本章要点

1. 平台差异的真正落点不是"有没有 XN"，而是**四件事的组合**：退出通道（macOS 有六项、iOS 为零）、问责层（macOS 无 PPL/SPTM）、JIT 区域的权限形态（非 macOS 不许降级）、每线程切换 API 的可用性（iOS 不可用）。
2. 芯片代际差异只有两处一手分叉句（启用范围、`disable-executable-page-protection` 幅度），加上世代门 A11/S3 与 SPTM 的 A15/M2；其余"芯片差异"多为二手扩写。
3. Apple tvOS 无专页、watchOS 对口页只点名 KIP/SKP/SCIP、visionOS 的 XN 句未点名 visionOS、Catalyst 无一手页——**这四项是本章的射程边界，全部按未核实或"跨页推论"处理**。
4. `mmap.2` 那句"due to hardware limitations, on some platforms"是 Apple 对平台不对称的**唯一自认**，但它不给平台名，本篇不填。

# 11. 谁能改变执行限制的状态

## 11.1 换一种排法：把前九章按"权力归属"重列

第 2–9 章按**层次**展开（架构能力 → SoC 原语 → 问责边界 → 准入边界）。本章换成第二个轴：**这件事的当前状态，谁能改、改了留什么痕迹、依据哪句原文**。这个视角的实际用途是事故与配置评审时回答一句话——"这个变化是我们自己造成的，还是系统决定的？"

先把六类主体列出来：Apple（定义与签发）、开发者（构建期选择）、用户（设备期开关）、App 自身（运行期动作）、内核与代码签名监控器（最终判定）、调试器与开发工具（外部改变状态）。

## 11.2 Apple：定义能力边界与签发例外

Apple 在本层握有三项独占权力，全部有一手句：

1. **授予"可执行"这个属性的规则本身**：「这些操作系统要求所有可执行代码均使用 Apple 颁发的证书进行签名」（`7c917bf14`，A 级）。可执行性由签名派生，签名的证书由 Apple 签发——这条链条上没有第三方位置。
2. **例外通道的键名与准入**：「内核会检查 Apple 专有的动态代码签名授权是否存在」（`15bfe098e`，A 级）。**该授权的公开键名不存在**（第 9.7 节第 2 项：`com.apple.security.dynamic-codesigning` 文档页实测 HTTP 404），申请条件亦未公开。→ 换言之 iOS 上这条例外"谁能拿到"这件事**完全在 Apple 一侧**。
3. **硬件代际与问责层的能力边界**：「自 Apple A11 仿生和 S3 后的 SoC 新增了硬件原语」、「SPTM 在 A15 或后续芯片和 M2 或后续芯片的 SOC 中受支持，并在支持的平台上替代了页面保护层」（`8b776536b` / `87716a080`，A 级）。第 10.6 节的世代门即由此而来——**开发者与用户对"这一层有没有 APRR/SPTM"都没有话语权**。

另有一条常被忽略：Apple 通过**公证**把 macOS 侧的规则变成事实上的强制（「To upload a macOS app to be notarized, you must enable the Hardened Runtime capability.」，A 级）。公证不改变设备上的页权限，但它决定了"带六个例外的 macOS App 能否被分发"，因此属于本章的权力图谱。

## 11.3 开发者：只有构建期一次机会，且只在 macOS 上有多选项

- **macOS**：第 9.2 节六项 + `jit-write-allowlist` / `-freeze-late` 两项收紧。全部发生在**签名之前**，运行期不可更改（授权属性封入签名，TN3126 逐字：`Slot -5 holds a hash of the entitlements`，A 级 → 授权内容本身受签名保护）。
- **iOS**：可枚举项为 **0**。开发者在 iOS 本层唯一能做的是**不去要求** W+X 映射（第 9.7 节第 1 项：iOS 上申请 W+X 不报错，因此"申请了但没拿到"这种状态在 API 返回值上不可见）。
- 审计粒度提醒（第 9.8 节）：「You add entitlements only to executables」——**开发者无法"只给某个 framework 开例外"**，任何此类 plist 都不生效。

**一句必须写下的限定**：开发者改变的是**覆盖面**，不是**判定逻辑**。判定逻辑（`vm_map_csm_allow_jit`、`cs_enforcement_enabled`、`CS_KILL`/`CS_HARD`）在成文文档里从未被列为开发者可配置项。

## 11.4 用户：本层唯一由用户控制的两个开关

1. **锁定模式与按站点排除**（第 7.6 节三句逐字）。这是本层唯一"用户可关窄、也可再打开"的口子，形态是：默认停用「即时 (JIT) JavaScript 编译等某些复杂的网络技术」，用户可**把受信任站点从锁定模式中排除**使其恢复。→ 该开关作用于 Safari/WebKit 的网页 JIT，**不影响任何 App 的 `MAP_JIT` 判定**（一手文本无此类句，第 15 章）。
2. **分析共享开关**（「共享 iPhone 分析 / 共享 iPhone 与手表分析」，`87fc038c2`，A 级）——它不改变页权限，但决定第 13 章 U1 通道是否开启。本章登记它，是因为"用户能改变本层的可见性（证据是否外发）却不能改变本层的判定"这一区分，正是第 12–13 章的骨架。

用户**不能**做的事，同样要登记：手册与 developer 文档里没有任何"用户可关闭代码签名校验 / 可执行内存保护"的 iOS 句；macOS 侧的用户权力是"门禁"层面的（第 10.1 节末句），不是页权限层面。

## 11.5 App 自身：运行期能做的三件事与一件不能

| App 的动作 | 一手描述 | 档位 |
|---|---|---|
| 请求一个 W+X 映射 | `mmap` + `MAP_JIT`；「When this entitlement is present, the system allows your app to call `mmap` with the `MAP_JIT` flag. If you don't have this entitlement, calls using that flag return an error.」 | A（《Porting JIT》）+ A-（`kern_mman.c:403-424, 479-482`） |
| 逐线程撤销/恢复本页执行权 | 「When write protection is disabled for the thread, writes by the thread to the MAP_JIT region are allowed and the MAP_JIT region is not executable.」 | A-（`pthread_jit_write_protect_np.3`）；**iOS 上该函数不可用**（A 级） |
| 把写入收给白名单回调 | 「only callbacks specifically enumerated in an invocation of PTHREAD_JIT_WRITE_ALLOW_CALLBACKS_NP can do so, and pthread_jit_write_protect_np is disabled.」 | A-（同 man page SECURITY CONSIDERATIONS） |
| 修改已建立的 JIT 区域权限 | **非 macOS 上不允许降级**：「When not on macOS, a MAP_JIT mapping without VM_PROT_ALL is strictly disallowed.」＋ `non-macOS: reject JIT regions without RWX` → `KERN_PROTECTION_FAILURE` | A-（`vm_map.c:3219-3262`） |

第四行是本章的要点：**App 的运行期自由度被压缩成"一次性的开关动作"**（写保护开/关），权限形态本身不可协商。这与第 6.1 节"两层模型"完全一致——**页表层的权限固定，线程层的可见性可变**；把"我能切换"误读成"我能改权限"是这条机制最常见的误解。

## 11.6 内核与代码签名监控器：最终判定者，以及它的三条旁路

成文文本把判定主体写成内核，源码把判定权进一步交给监控器（A- 级）：

- `vm_map_csm_allow_jit()` 直接转调 `csm_allow_jit_region(vm_map_pmap(map))`（`vm_map.c:19889-19893`）→ **VM 层不自行决定 JIT 准入**。
- 有监控器时 VM 不再重复校验该页：`No need for the VM to also validate the page. In the code signing monitor we trust :)`（`vm_fault.c:2815-2828`）。
- `cs_bypass` 的三个来源（`vm_fault.c:4773-4777`）：`(*entry)->used_for_jit` **或** `csm_address_space_exempt(...) == KERN_SUCCESS` **或** `(*entry)->vme_resilient_codesign`。→ **旁路不是"某进程被豁免"，而是三类映射形态**，其中后两类（监控器认定的地址空间豁免、弹性签名映射）在面向开发者的文档层完全没有描述（附录 B 零命中）。
- 处置强度由进程标志决定：`CS_KILL` / `CS_HARD`（`kern_cs.c:248-312`），分支条件 `cs_enforcement_enabled`（`vm_fault.c:2873-2891`）。

**"谁改变状态"在这一节的答案是：内核按签名与监控器答复，但内核自身的行为宽度由三样东西决定——进程的 CS 标志、地址空间是否为 alien/exotic、映射是否带 `used_for_jit`。** 三者都不在公开可配置清单里。

## 11.7 调试器与开发工具：一条独立于 JIT 授权的例外

源码注释把两个概念分开写（A- 级，`bsd/sys/code_signing.h:725-730`）：

> `Associate a debug region with an address space in the monitor environment. An address space can only have a debug region if it is currently being debugged.`

→ **"某地址空间存在被监控器认可的可执行例外"有两种来源：JIT 授权，或该进程正在被调试。** 因此：

- 看到某进程持有可执行例外时，**不能推断"它有 JIT 授权"**；须先判定是否在调试会话中。
- macOS 侧 `com.apple.security.cs.debugger`（Debugging Tool，A 级）把这条路径制度化：「When a non-root user runs an app with the debugging tool entitlement, the system presents an authorization dialog… the debugger receives a **10-hour session** before authorization expires.」→ **10 小时**是本章唯一"例外的时间边界"的一手数字。
- `PMAP_OPTIONS_XNU_USER_DEBUG`（`vm_map.c:11030`，A- 级）在本篇只登记常量存在，用途未取证（第 15 章 N 类）。
- 反方向也成立：Xcode 调试器会拦截崩溃（A 级，「the debugger intercepts the crash … detach the debugger … This allows the app to finish crashing and lets the operating system generate the crash report.」）→ **调试状态不仅改变执行例外，还改变第 12 章的可观测性**。这一点常被忽略，实际取证时是第一个要排除的干扰。

## 11.8 谁不在名单上：MDM、配置描述文件与"开发者模式"

本轮在手册镜像上做了针对性检索（基数 `_tmp/sec*.txt` **179 页**，逐词命中页数）：

| 检索词 | 命中页数 | 与本层的交集 |
|---|---|---|
| `配置描述文件` | 9 页 | **与本层 7 个相关页交集为 0**（`15bfe098e`、`8b776536b`、`87716a080`、`7c917bf14`、`f49cad4db`、`ebb113be1`、`2437264f0` 逐页 grep 均为 0） |
| `监督式` | **0** | — |
| `移动设备管理` / `MDM` | **0 / 0** | — |
| `执行权限` | 1（`8b776536b`） | 即 APRR 段本身 |
| `可执行内存` | 1（`f49cad4db`） | 即「所有可执行内存页的代码签名检查会在载入时进行」 |
| `不可执行` / `Execute Never` | 各 1（`15bfe098e`） | 全库唯一出处 |
| `页错误` / `签名页` | **0 / 0** | 手册不使用这两个词 |

→ 结论（按未核实写法）：**Apple 一手文本没有把"执行限制"与配置描述文件、监督式管理或 MDM 写在任何同一句里**。因此本篇不声称"MDM 可远程关掉执行防护"，也不声称"MDM 与此无关"；只登记"无一手关联句"。同样，`开发者模式` 在手册全库命中 0 页，故本篇不涉及。

## 11.9 权力—证据—档位总表

| 主体 | 能改变什么 | 一手依据 | 档位 | 改变后留下的痕迹 |
|---|---|---|---|---|
| Apple | 规则本身、例外签发、硬件代际 | `7c917bf14`、`15bfe098e`、`8b776536b` | A | 签名与 entitlement 内容（`Slot -5`） |
| 开发者 | macOS 六项例外 + 两项收紧 | 授权属性页族、《Hardened Runtime》 | A | 嵌入的 entitlements；签名里的值 |
| 用户 | 锁定模式（含按站点排除）、分析共享 | `ipsd5baf79d0`、105120、`87fc038c2` | A | 设备设置；不进入构建产物 |
| App（运行期） | `MAP_JIT` 请求、逐线程写保护、白名单回调 | man page 族、《Porting JIT》 | A / A- | 内核策略判定，DTrace 探针 `cs_wx`（A-） |
| 内核 + 监控器 | 最终判定与处置强度 | `kern_mman.c`、`vm_map.c`、`vm_fault.c`、`kern_cs.c` | A- | 内核日志（第 12 章七状态字段）、`EPERM`/`KERN_PROTECTION_FAILURE`/`KERN_CODESIGN_ERROR` |
| 调试器 | 调试区域 + 崩溃是否成报告 | `code_signing.h:725-730`、《Acquiring crash reports…》 | A- / A | 10 小时会话（A）；调试期内无崩溃报告 |

## 11.10 本章要点

1. **本层有六类主体，但只有三类能真正改变判定结果**：Apple（规则与例外）、内核/监控器（执行判定）、硬件世代（能力有无）。开发者改覆盖面，用户改网页侧例外与证据外发，App 只能在给定形态内动作。
2. iOS 侧"谁能拿到例外"这件事**没有任何第三方接口**——键名未公开、申请条件未公开、`allow-jit` 文档页 `platforms` 为空数组。
3. **调试区域与 JIT 区域是两个独立例外来源**，审计与取证时必须先区分；调试状态还会让崩溃报告消失（第 12 章）。
4. `cs_bypass` 的三条来源与 `CS_KILL`/`CS_HARD` 的处置分支**均不在公开可配置清单**里，本篇只登记形态。
5. MDM / 配置描述文件 / 监督式与本层**无一手关联句**（第 11.8 节命中表），两个方向的断言都不写。

# 12. 可观测性：违规与崩溃留下的痕迹

## 12.1 先纠正一条旧结论：`.ips` 有官方格式文档

系列前几篇曾登记"developer 站不存在描述 `.ips` 崩溃报告字段格式的官方文档页"。**该结论不成立**，本篇予以纠正。三篇 A 级文档逐字定义了字段与异常类型（页脚均 © 2026，记"截至 2026 可用"）：

1. 《Interpreting the JSON format of a crash report》 https://developer.apple.com/documentation/xcode/interpreting-the-json-format-of-a-crash-report
2. 《Examining the fields in a crash report》 https://developer.apple.com/documentation/xcode/examining-the-fields-in-a-crash-report
3. 《Understanding the exception types in a crash report》 https://developer.apple.com/documentation/xcode/understanding-the-exception-types-in-a-crash-report

来源一的三条基础句（逐字）：

> `Starting with iOS 15 and macOS 12, apps that generate crash reports store the data as JSON in files with an .ips extension. Tools for viewing these files, such as Console, translate the JSON to make it easier to read and interpret.`

> `The IPS file for a crash report contains two JSON objects: an object containing IPS metadata about the report incident and an object containing the crash report data. … If the bug_type property of the metadata object is 309, the log type for crash reports, you can extract the JSON for the crash report data from the remainder of the text.`

> `The crash report data is made up of additional objects pertaining to OS version, bundle, store, exception, termination, threads, frames, and binary images.`

`bug_type` 的两个一手取值：**309** = crash report，**288** = stackshot（同页逐字：`Type 288, for instance, is a stackshot.`）。元数据对象属性里与本层相关的还有 `cpuType`（取值集合逐字 `ARM-64, ARM, X86-64, or X86`）与 `crashReporterKey`（逐字 `An anonymized per-device identifier. … This identifier is reset upon erasing the device.`）——后者是本篇唯一一条关于"报告里的设备标识形态"的一手句，第 13 章引用它。

**这条纠正同时定了一条方法纪律**（第 15 章第 18 条）：任何"无一手来源"的结论必须附检索词、文件集与命中数，否则不得下结论。旧结论之所以错，是因为检索范围只覆盖了 `guide/security` 手册全库（那 179 页里确实 0 命中），而未覆盖 Xcode 文档族。

## 12.2 与本层相关的异常类型：Apple 的逐字释义

来源三的键名与释义（逐字，A 级）——本表只列与本层判定有关的行：

| 键名 | Apple 释义（逐字） |
|---|---|
| `EXC_BAD_ACCESS` | A bad access to memory terminated the process. |
| `EXC_BAD_ACCESS (SIGSEGV)` | A memory segmentation fault terminated the process, often because the process tried to access an invalid or out-of-bounds address in memory. |
| `EXC_BAD_ACCESS (SIGBUS)` | A bus error terminated the process, often because the process tried to access a misaligned or invalid address in memory, **or due to a pointer authentication failure**. |
| `EXC_BREAKPOINT (SIGTRAP)` / `EXC_BAD_INSTRUCTION (SIGILL)` | A trace trap or **invalid CPU instruction** interrupted the process, often because the process violated a requirement or timeout. |
| `EXC_CRASH (SIGKILL)` | The operating system terminated the process, often because a background task violated a requirement, device resources were limited, or the user force quit the app. |
| `EXC_CRASH (SIGABRT)` | The process terminated because it received an abort signal. |
| `EXC_GUARD` | The process violated a guarded resource protection, often related to file descriptors. |
| `EXC_RESOURCE` | The operating system stopped the process because the process exceeded a limit on resource consumption, like CPU time or memory. |

该页共 **13 行**条目、**14 个键名**（`EXC_BREAKPOINT (SIGTRAP)` 与 `EXC_BAD_INSTRUCTION (SIGILL)` 合并释义）。三处与本层直接相关：

- `SIGSEGV`/`SIGBUS` 两个释义正好对应 man page 那句「When a program violates the protections of a page, it gets a SIGBUS or SIGSEGV signal.」（`mprotect.2:83`，A- 级）——**手册层没有这句话，A 级文档给出异常名，A- 级给出信号名**，两者在本篇配对使用但不混档。
- `EXC_CRASH (SIGKILL)` 的释义是"操作系统终止了进程"，与第 8.5 节 `threadsignal(current_thread(), SIGKILL, EXC_BAD_ACCESS, FALSE)`（`CS_KILL` 路径）在形态上一致。**但 Apple 未公开"`CS_KILL` 命中 → 该字段值"的映射句**，本篇只写"形态一致"，不写"据此可判定"。
- `EXC_BAD_ACCESS (SIGBUS)` 的释义把 **pointer authentication failure** 与 misaligned/invalid address 并列 → 与《iOS 特定指针认证》第 4 章同源：这一行**不能**单独用来区分"执行限制违规"与"PAC 失败"。

## 12.3 非执行页被执行时，报告里能读到什么

来源：《Investigating memory access crashes》 https://developer.apple.com/documentation/xcode/investigating-memory-access-crashes （A 级，© 2026）。本章核心四句（逐字）：

> `The Exception Subtype field in the crash report contains a kern_return_t value describing the error and the address of the memory that was incorrectly accessed`

> `KERN_PROTECTION_FAILURE. The crashed thread tried to use a valid memory address that's protected. Some types of protected memory include read-only memory regions, or nonexecutable memory regions.`

> `There are two categories of memory access issues: invalid memory fetches and invalid instruction fetches. … To determine which type of memory access issue caused a crash, focus on the program counter … On ARM CPU architectures, this is the pc register.`

> `If the program counter register is the same as the exception address, the crash is due to an invalid instruction fetch.`

第四句是本篇**唯一由 Apple 给出的"如何判定是在执行非执行页"的方法**：比较 `pc` 与异常地址。给方法而不给结论，这一条在第 15 章列为必须保留的写法纪律（第 12 条限定表述）。

同页两条支撑：

> `Process crashes due to a memory access issue contain information about the virtual memory regions in the VM Region Info field.`

> `The PRT column shows the current permission attributes for the memory regions, with r indicating the memory is readable, w indicating the memory is writable, and x indicating the memory is executable.`

页面样例（属**示例数值**，非承诺，但可直接用作字段形态说明）：

```
VM Region Info: 0x16c070a30 is in 0x16c070000-0x16c074000;  bytes after start: 2608  bytes before end: 13775
      REGION TYPE                      START - END             [ VSIZE] PRT/MAX SHRMOD  REGION DETAIL
      Stack                  000000016bfe8000-0x…016c070000 [  544K] rw-/rwx SM=COW  thread 12
--->  STACK GUARD            000000016c070000-0x…016c074000 [   16K] ---/rwx SM=NUL  ...for thread 11
```

以及 `__TEXT  0000000100b7c000-0x0000000100b84000 [   32K] r-x/r-x SM=COW  ...pp/MyGreatApp`。

**读法**：`PRT/MAX` 是"当前权限 / 最大权限"两列。样例中 `rw-/rwx` 表示"当前可写不可执行、上限可执行"——**这正是第 9.7 节那条平台分叉句（iOS 上申请 W+X 拿到"可写但不可执行"）在崩溃报告里的可见形态**；`---/rwx` 则是"零权限但 MAX 为 rwx"的守护页。`__TEXT` 行的 `r-x/r-x` 展示签名代码段的常态。三行合起来，是本篇能给读者的最直接的"执行限制在报告中的长相"。

两条字段落位差异（逐字）：

> `On macOS, the Exception Codes field contains the exception subtype`

→ 同一信息在 macOS 与 iOS 落在**不同字段名**，跨平台的解析规则不可直接移植。arm64e 相关三句须整组引用：

> `A crash due to a possible pointer authentication failure uses the KERN_INVALID_ADDRESS exception subtype with an additional message on the end`

样例：`KERN_INVALID_ADDRESS at 0x00006f126c1a9aa0 -> 0x000000126c1a9aa0 (possible pointer authentication failure)`；限定句逐字：`An invalid memory access, where high-order bits are erroneously set, can look like a pointer authentication failure`。→ **possible 一词不可删**，且该文档自己给出了反向解释。这类行在第 15 章登记为"须加限定语"。

## 12.4 内核侧的三个出口与报告字段的对应（含缺口）

第 8 章的实现事实对应到可读形态：

| 内核出口 | 返回值 / 动作 | 一手出处 | 报告侧对应 |
|---|---|---|---|
| 写执行互斥闸门 | `KERN_PROTECTION_FAILURE`，打印 `curprot cannot be write+execute. failing` | `vm_map.c:3180-3216`（A-） | `Exception Subtype: KERN_PROTECTION_FAILURE`（A，12.3 首句支撑） |
| NX 页不许映射为 EXEC | `KERN_CODESIGN_ERROR`，打印 `page marked to be NX, not letting it be mapped EXEC`（受 `cs_debug` 控制） | `vm_fault.c:2838-2871`（A-） | **无公开映射句**（12.4 末） |
| 无效页 + `CS_KILL` | `SIGKILL` 伴随 `EXC_BAD_ACCESS` | `kern_cs.c:248-312`（A-） | `Termination Reason` 可含 invalid code signature 文案（A，12.5） |
| 无效页 + `CS_HARD` | 映射失败、进程继续存活 | 同处注释（A-） | **不产生崩溃**，故报告侧不可见 |

第三行需要来源二的逐字支撑（A 级）：

> `Termination Reason`: Exit reason information specified when the operating system quits a process. … Examples of the information you can find in this field are messages about **an invalid code signature**, a missing dependent library, or accessing privacy sensitive information without a purpose string.

→ 这是 Apple 一手文本里**唯一**把"代码签名无效"与某个崩溃报告字段直接写在一起的句子。它证实形态存在，但**没有给出 `KERN_CODESIGN_ERROR` 应出现在哪一字段**——手册全库 grep `KERN_CODESIGN_ERROR` 命中 0（附录 B）。**本节缺口登记**：该错误码在 `.ips` 中的呈现位置为未核实，第 15 章 N 类。

另两行字段（A 级，逐字）在本章只作边界用：`Exception Note`「If this field contains `EXC_CORPSE_NOTIFY`, the crash didn't originate from a hardware trap, either because the process was explicitly quit by the operating system or the process called abort().」；`Code Type`「If the app executes through Rosetta translation, this field appends `(Translated)`; otherwise, it appends `(Native)`」。后者与第 8.4 节 Rosetta 逐页校验配对使用：**报告里的 `(Translated)` 说明该进程走过转译，但并不意味着页校验被跳过**。

## 12.5 系统日志侧：一行内核日志里有七个状态位

面向设备侧取证（非崩溃报告）的一手可用事实只有一条，来自源码（A- 级，`vm_fault.c:3063-3068` 逐字格式串）：

> `CODE SIGNING: process %d[%s]: rejecting invalid page at address 0x%llx from offset 0x%llx in file "%s%s%s" (cs_mtime:%lu.%ld %s mtime:%lu.%ld) (signed:%d validated:%d tainted:%d nx:%d wpmapped:%d dirty:%d depth:%d)`

括号内七个状态位（`signed / validated / tainted / nx / wpmapped / dirty / depth`）加两个时间戳（`cs_mtime` 与文件 `mtime`）与文件名，是"这一页当时处于什么状态"的最完整一手描述。三条限制必须同时写下：

1. 这是 `printf`，**不是对外 API**；A- 级，不构成承诺。`page marked to be NX…` 那条明确受 `cs_debug` 控制，本条是否受同等调试位约束，本轮未取证。
2. 用户态可访问性、日志在设备上的存放位置与保留期：**一手文本零命中**（第 12.8 节）。
3. `cs_mtime` 与 `mtime` 并列这一形态说明内核会**比对签名时间戳与文件时间戳**（与第 8 章"自安装或上次更新之后未被修改"呼应），但本篇不把它读成"文件被改动即可检测"的断言——检测时机仍是"按需、载入时"。

## 12.6 最重要的一条限制：这一类崩溃不进开发者后台

《Acquiring crash reports and diagnostic logs》 https://developer.apple.com/documentation/xcode/acquiring-crash-reports-and-diagnostic-logs （A 级，© 2026）逐字：

> `The following crash report types aren't available through the Crashes organizer, but are available by other means. …`
> `- Watchdog events, such as those from slow app launch times`
> `- Invalid code-signature crashes`
> `- Thermal events, where a device overheats because an app uses too much CPU`
> `- Jetsam events, where an app has high memory use`

→ **本篇关于可观测性最硬的一条结论**：第 8 章 `CS_KILL` 路径（签名校验失败导致的终止）产生的崩溃，Apple 明示**不在开发者 Crashes organizer 通道内**。任何"上线后用后台崩溃率监控执行防护命中情况"的方案，在这一类事件上默认失效；可用的替代只有第 12.7 节用户手动分享路径（12.9 第 4 步）与设备侧日志。

同页另三条（A 级，逐字）界定谁的数据会到开发者手上：

> `TestFlight and the App Store collect crash reports for every submitted version of your app. Crash reports automatically contain identifiable symbol information if you include symbol information when submitting a build to the App Store.`

> `The Crashes organizer presents crash reports from customers who share diagnostic and usage information … TestFlight users of your app automatically share crash reports with you, regardless of the device settings for sharing diagnostic and use data.`

> `Crash reports from watchOS are available on the paired iPhone.`

第四条与调试有关（第 11.7 节末的呼应）：

> `If you encounter a crash while debugging your app using Xcode, the debugger intercepts the crash … This allows the app to finish crashing and lets the operating system generate the crash report.`

## 12.7 MetricKit 侧：没有执行权限专用字段

A 级（developer.apple.com/documentation/metrickit/ 属性页）：

- `MXCrashDiagnostic`——「An object representing a diagnostic report for an app crash.」属性逐字：`exceptionType`「The Mach exception type of the crash.」、`exceptionCode`「The encoded processor-specific information for the crash.」、`signal`「The signal associated with the crash.」、`virtualMemoryRegionInfo`「Information about the region of memory an app accessed incorrectly, resulting in a bad-access crash.」、`terminationReason`「The reason the app was terminated as a human-readable string.」、`callStackTree`「The call stack for the crash.」
- 该类整体标注弃用：「Deprecated / Use CrashDiagnostic instead.」；可用性逐字 `iOS 14.0–27.2 Deprecated`（含 Mac Catalyst 14.0–27.0）。
- 新类型 `CrashDiagnostic`：abstract「A diagnostic report that describes a crash that occurred.」，Discussion 逐字「This carries a … along with crash-specific metadata including the exception type, exception code, signal, and optional Objective-C exception reason.」；`metadata.platforms` 逐字 `[{"introducedAt":"27.0","name":"iOS"}, iPadOS, Mac Catalyst, macOS, visionOS]`——**列表内无 watchOS、无 tvOS**（与第 10 章的平台不对称相互印证，且这是"平台集合在不同文档里不一致"的实例）。
- 框架 abstract 逐字「Measure your app's performance using daily metric and diagnostic reports from real users.」→ **粒度为日**，本篇不写实时。

**负面结论（检索台账）**：在 `MXCrashDiagnostic`、`CrashDiagnostic` 属性页检索 `executable` / `protection` / `MAP_JIT` / `jit` / `page` / `nx` → **全部 0 命中**。在 `examining-the-fields-in-a-crash-report` 内 `executable` 命中 1 次，逐字上下文为「The executable name for the process」（非权限义）、`protect` 命中 1 次（guarded/protected 义）。→ **MetricKit 没有"执行权限/页校验"专用字段**；可观测维度只有 exception、code/signal、`virtualMemoryRegionInfo`、`terminationReason` 与回溯。

## 12.8 缺口与零命中（本章）

1. **`KERN_CODESIGN_ERROR` 在 `.ips` 中的呈现位置**：手册与 Xcode 文档族均未给映射（grep `KERN_CODESIGN_ERROR` 在 `_tmp/sec*.txt` 命中 0 页）。
2. **`.ips` 字段的枚举常量表**（各 `kern_return_t` 取值全集、`Termination Reason` 文案全集）：无公开页。
3. **崩溃/诊断数据的保留期、可见主体、服务端处理时限**：在四篇 Xcode 崩溃文档 + `CrashDiagnostic` 页 + MetricKit 缓存内 **0 命中**。
4. **`DiagnosticLogs` 目录的官方说明页**：未取到。
5. **一行内核日志在用户可访问日志中的可见性与留存**：无一手句；本篇不假设 Console 或任何采集工具能拿到它。
6. **iOS 上 `EXC_BAD_ACCESS (SIGBUS)` 与 NX 违规的实际对应**：Apple 未给映射表（105120 等支持文章亦无），12.2 的释义并列关系不足以替代它。
7. **watchOS / tvOS 的报告形态**：`CrashDiagnostic` 平台清单不含二者；唯一一手句是「Crash reports from watchOS are available on the paired iPhone.」

## 12.9 可用的判读顺序（五步，均落回上文出处）

1. 先确认报告类型与元数据：`bug_type` 是否为 309（12.1）；`cpuType`、`Code Type` 是否 `(Translated)`（12.4）。
2. 读 `Exception Type`：落在 `EXC_BAD_ACCESS` 族还是 `EXC_CRASH (SIGKILL)` 族（12.2）。
3. 读 `Exception Subtype`：是否为 `KERN_PROTECTION_FAILURE`；**若报告自称 possible pointer authentication failure，先按 12.3 的限定句排除"高位被误置的普通越界"**。
4. 比较 `pc` 与异常地址：相等 → Apple 判定为 invalid instruction fetch（12.3 第四句）。这一步是本层唯一"由 Apple 给出的"归因方法。
5. 读 `VM Region Info` 的 `PRT/MAX` 与 `REGION DETAIL`：确认命中的是 `rw-/rwx`、`---/rwx` 还是 `r-x/r-x` 区域（12.3 样例）。

**若前四步指向签名校验失败**：按 12.6，**不要期待在后台看到它**；改走"请用户在 `设置 > 隐私与安全性 > 分析与改进 > 分析数据` 中定位 `<AppBinaryName>_<DateTime>` 并以邮件分享"这条一手路径（第 13 章 U3），并同时检查该设备是否处于调试会话（11.7）。

## 12.10 本章要点

1. 崩溃报告的字段格式**有** A 级官方文档（12.1 三篇），本层的可读出口是 `Exception Subtype` + `VM Region Info` 的 `PRT/MAX` + `Termination Reason`。
2. 判定"是在执行非执行页"的**方法**是 Apple 给的（`pc` 与异常地址比较），不是本篇推的；本篇不提供结论式判据。
3. `CS_KILL` 与 `CS_HARD` 使"同一事件的可见后果并不唯一"，其中后者根本不留崩溃。
4. **`Invalid code-signature crashes` 不进 Crashes organizer**——本层最实用的可观测性限制，写监控方案前必读。
5. MetricKit 无页权限字段、粒度为日、`CrashDiagnostic` 平台清单不含 watchOS/tvOS；内核那行七状态日志是 A- 级实现细节，不是对外通道。

# 13. 上传方向：页与地址层面的证据如何离开设备

## 13.1 记号声明与本章要回答的问题

**U1 / U1′ / U2 / U3 是本系列为"数据离开设备"设立的自定义分类，不是 Apple 的官方分类**。正文引用时必须落回 Apple 原句，不得把三档命名写成 Apple 术语（沿用系列前几篇的定义）：

- **U1** 设备 → Apple 服务端（用户同意后的分析/诊断共享）
- **U1′** 代码 → Apple（构建期与公证提交，非运行期）
- **U2** 设备 → iCloud / 账户侧
- **U3** 设备 → 开发者侧（Xcode Organizer、TestFlight、MetricKit、用户手动发送）

本章要回答的问题只有一个：**第 12 章那些"页层面的证据"（地址、区域、权限列、错误码），有多少、经由哪条通道、在什么同意条件下会离开设备？**

先给一条边界声明，因为它决定了本章所有句子的强度上限：**"页不可执行"这个判定本身不产生任何主动外发的记录**。外发的是崩溃报告与诊断指标；其内容范围由 Apple 公开的数据类别说明界定，本篇不写"上传了内存内容"这类无出处断言。

## 13.2 本层的载体到底是什么（逐项对齐字段）

把第 12 章的字段按通道重排，得到本篇的实际射程：

| 证据形态 | 是否含页内容 | 所属通道 | 一手依据 |
|---|---|---|---|
| `Exception Type` / `Exception Subtype`（如 `KERN_PROTECTION_FAILURE`） | 否 | U3（+ U1 若用户同意） | A，12.3 |
| `VM Region Info` 的 `PRT/MAX` 权限列与区域名（`__TEXT`、`STACK GUARD` 等） | 否，仅地址区间与权限字母 | U3 | A，12.3 样例 |
| `Termination Reason`（可含 invalid code signature 文案） | 否 | U3 | A，12.4 |
| 回溯地址 + `binaryImages` | 否（地址） | U3；**符号化需另交 dSYM/debug symbols** | A，13.6 |
| 一行内核日志（七个状态位 + 偏移 + mtime） | 否 | **通道未证实**（13.7 缺口） | A-，12.5 |
| `MAP_JIT` 区域的实际字节 | — | **本篇无任何一手句支持其外发** | — |

**读法**：本层证据离开设备时，形态是"地址 + 权限字母 + 错误码 + 区域名"，而不是页内容。这一句是本章能给的最强表述，且它是**从字段清单的射程归纳**出来的，不是 Apple 的逐字承诺——Apple 逐字给的只有第 13.4 节那三条数据类别。

## 13.3 U1′：构建期把代码副本交给 Apple（本层唯一"内容级"外发）

《macOS 中的 App 代码签名进程》secid `3ad8e6e53`（发布日期 **2021 年 2 月 18 日**，A 级）两句逐字：

> 在运行 macOS 10.15 的设备上，不在 App Store 中分发的所有 App 必须由开发者使用 Apple 签发的开发者 ID 证书（和专用密钥）进行签名并由 Apple 公证，以便在默认的"门禁"设置中运行。

> 公证可由软件分发链中的任何人执行，并证明已向 Apple 提供代码副本以检查恶意软件且未发现已知的恶意软件。公证的输出为票据，储存在 Apple 服务器上，可选择（由任何人）包含在 App 中，而不会使开发者的签名失效。

与本层的连接：第 8 章的按页哈希封在 Code Directory 里，而 Code Directory 属于被签名的 Mach-O 本体 → **U1′ 交出去的是包含页哈希结构的代码本身**。这是本篇唯一一条"页层面的结构信息以完整形态到达 Apple"的通道，但它发生在**构建期而非运行期**，且仅覆盖 macOS 分发链（iOS 侧走 App Store 提交，机制相近但一手句在此页属 macOS 语境，本篇不跨平台套用）。

## 13.4 U1：法务页给出的内容类别与两条限定

来源：Apple 法务页《Device Analytics & Privacy》 https://www.apple.com/legal/privacy/data/en/device-analytics/ ，页面尾部日期逐字 **2026-09-14**（A 级）。四句关键原文（逐字）：

> `iPhone Analytics may include details about hardware and operating system specifications, performance statistics, and data about how you use your devices and applications. This information is used to help Apple improve and develop its products and services. None of the collected information identifies you personally. Personal data is not logged at all, is subject to privacy-preserving techniques such as differential privacy, or is removed from any reports before they're sent to Apple.`

> `You can review this information on your iOS device by going to Settings > Privacy & Security > Analytics & Improvements and tapping Analytics Data.`

> `If you have consented to provide Apple with this information, and you have Location Services turned on, the location of your devices may also be sent to help Apple analyze performance issues…`

> `If you agree to send analytics information to Apple from multiple devices that use the same iCloud account, we may correlate some usage data about Apple apps across those devices by syncing using end-to-end encryption. We do this in a manner that does not identify you to Apple.`

四点读法（正文必用）：

1. Apple **自陈**的上传类别是"硬件与操作系统规格 / 性能统计 / 使用数据"三类。列表里**没有**"页权限""区域清单"，但也**没有**排除"性能统计"可容纳异常与回溯 → 只能写"未见一手句声明页权限字段是否随分析外发"，**不得**写"一定不外发"。
2. **可见性在设备侧**：那句"你可以自行在 `Analytics Data` 中审阅"与 13.6 的开发者文档指向同一入口，构成"用户可自行审阅将要离开设备的内容"的一手承诺——这是本层证据的**唯一用户侧审计入口**。
3. 隐私限定词有三档，须原样保留：`not logged at all` / `differential privacy` / `removed from any reports before they're sent to Apple`。**不得简化为"已匿名化"**。
4. 定位数据是**条件性**外发（需同意 + 定位服务开启），法务页用 `may also be sent`——本层证据若含地址，与"定位"不是同一类别，本篇不把二者合并叙述。

技术侧唯一取到的"同意 + 上传"口径仍是《Apple 操作系统中的后台安全改进》secid `87fc038c2`（发布日期 2026-01-28，A 级）两句：

> 与移除建议相关的分析（例如，触发建议的 App 或导致建议显示的原因）会发送给 Apple，前提是用户通过以下设置同意共享此类信息：iPhone："设置">"隐私与安全性">"分析与改进">"共享 iPhone 与手表分析"

> 安装了后台安全改进的设备随后会**使用设备端分析系统**，检查其中一个受影响的应用程序在安装后台安全改进后崩溃的频率是否有显著增加。

同族句（A 级，《屏幕时间安全性》secid `d8831e732`）：「如果用户打开了"共享 iPhone 与手表分析"，Apple **只会收集以下匿名数据**」→ 手册侧对上传内容的限定词是**匿名**与**结论性信息**；**没有任何句子承诺上传页权限、区域清单或偏移**。

**与本层的连接方式**：本层机制（XN / APRR / `MAP_JIT` / 页校验）触发的崩溃可能被计入"崩溃率升高"，而该页同时给出**回滚口径**（「可让用户有机会移除所有后台安全改进并将设备恢复为其上次软件更新的版本」）→ Apple 自己承认安全改进可能引入崩溃回归，这是"运行时执行防护会产生可观测崩溃"的一手佐证。但手册**未**把执行权限与上传写在同一句里 → 正文须用"未见直接关联句"的措辞。

## 13.5 U2：只有一句法务口径可用

13.4 第四句是本篇关于 U2 的**全部**一手材料：同一 iCloud 账户下多台设备的分析数据可被关联，关联同步使用**端到端加密**，且逐字 `We do this in a manner that does not identify you to Apple.`

→ 该句只讲 **correlate some usage data about Apple apps**，**不是**"崩溃报告存入 iCloud"的承诺。因此 U2 从"未核实"上调为**部分核实**，且必须写成这样：**Apple 一手只承诺了跨设备分析数据的关联方式，未给崩溃报告或诊断文件是否经 iCloud 备份的任何表述。**

负面检索登记：`iCloud 分析`、`iCloud 诊断`、`崩溃报告 iCloud`、`DiagnosticReports backup` 在 `_tmp/sec*.txt`（179 页）与 `_tmp/pe/*` 缓存内 → **0 命中**（附录 B）。

## 13.6 U3：四条一手规则、一条脱敏义务与一个符号化配对

来源：《Acquiring crash reports and diagnostic logs》（A 级，© 2026）四条已在 12.6 逐字给出，此处按"上传方向"重排：

1. **提交即收集**：`TestFlight and the App Store collect crash reports for every submitted version of your app.`
2. **同意形态有两种**：Crashes organizer 呈现"共享诊断与使用信息"的顾客报告；而 `TestFlight users of your app automatically share crash reports with you, regardless of the device settings` → **TestFlight 是不依赖设备设置的自动通道**，本层证据若发生在 TestFlight 用户身上，默认离开设备。
3. **本层最关键的一条**：`Invalid code-signature crashes` 不在 Crashes organizer 内（12.6 逐字）→ **第 8 章页校验失败这一类崩溃，在 U3 的默认通道上不可见**。
4. **watchOS 的报告经配对 iPhone 取得**（`Crash reports from watchOS are available on the paired iPhone.`）→ 与 10.2 的"watchOS 无执行限制专页"叠加时，注意这是**通道事实**而非**机制事实**。

**第二种同意形态：用户手动外发**（同页逐字路径）：

> `To locate and email crash reports for iOS, iPadOS, tvOS, visionOS, and watchOS apps: Open the Analytics & Improvements section of Settings on the device. … Tap Analytics Data. … Locate the log for your app. The log name starts with <AppBinaryName>_<DateTime> for crash reports, or JetsamEvent_<DateTime> for high-memory use crashes. … Tap the Share icon, and select Mail to send the crash report as a mail attachment.`

→ 这条路径同时是 12.9 末尾"当后台看不到时该怎么办"的一手答案，也是 13.4 第 2 点（设备侧可审阅）的落地点。

**符号化配对**（《Adding identifiable symbol names to a crash report》，A 级，逐字）：

> `If you include debug symbols when you upload your app to App Store Connect for distribution in the App Store or TestFlight, the Crashes organizer in Xcode automatically symbolicates the crash reports for you.`

同页两句脱敏义务（逐字，必须一起引）：

> `Always look for and redact sensitive information in your shared crash reports. Replace your app name and bundle ID throughout the crash report with redacted characters…`

> `Use Apple crash reports whenever possible, as third-party crash reports may omit necessary information.`

→ 后果（可写）：**U3 会把"设备侧地址 + Apple 侧符号文件"配对**，因此地址随机化的产物在服务端可被还原为函数与行号。这句是本系列第 93 篇（地址布局随机化）与本篇的接缝：**随机化提高的是猜测成本，U3 通道 + dSYM 会把它在工作流层面还原回去**；同时 Apple 逐字提示了脱敏义务，本篇据此给出 13.8 的清单第 6 项。

## 13.7 U3 的另一形态：MetricKit（先到 App，去向由 App 决定）

A 级：框架 abstract 逐字「Measure your app's performance using **daily** metric and diagnostic reports **from real users**」；`MXCrashDiagnostic` / `CrashDiagnostic` 字段见 12.7。

→ 该通道的"上传方向"由 **App 自己**决定（Apple 只把日报交付给 App），本篇**不能**把它写成"Apple 收到"；粒度为**日**，不含实时性承诺。此外 12.7 的负面结论（无页权限专用字段）在此同样适用：**MetricKit 不能用来统计"执行防护命中次数"**。

**通道缺口登记（13.2 末行的兑现）**：那行七状态内核日志是否随诊断外发、以何形式存放、用户能否在 `Analytics Data` 中看到——**一手文本 0 命中**。`.ips` 上传端点/接口亦无公开文档页；本轮可引的只有"用户经邮件分享"与"Crashes organizer 呈现"两种形态。

## 13.8 三通道汇总（含档位与缺口）

| 通道 | 本层可外发的载体 | 一手覆盖度 | 关键限制（逐字来源） |
|---|---|---|---|
| U1 设备 → Apple | 「触发建议的 App 或导致建议显示的原因」；`Analytics Data` 目录里的 `<AppBinaryName>_<DateTime>` 文件由用户主动经邮件分享 | **部分核实** | 判定在设备端（「使用设备端分析系统」）；类别清单无"页权限"但也未排除（13.4 第 1 点） |
| U1′ 代码 → Apple | Mach-O 与其 Code Directory（页哈希、签名、授权属性） | **已核实** | 「已向 Apple 提供代码副本以检查恶意软件」＋「公证的输出为票据，储存在 Apple 服务器上」（`3ad8e6e53`） |
| U2 设备 → iCloud | 与执行限制层**无一手关联句** | **部分核实**（仅一句法务口径） | 端到端加密的跨设备**关联**，非"报告存入 iCloud"承诺（13.5） |
| U3 设备 → 开发者 | exception / subtype / `VM Region Info`（含 `PRT/MAX`）/ `Termination Reason` / 回溯地址；符号化需 dSYM 另行上传 | **已核实** | **invalid code-signature 崩溃不进 organizer**（12.6、13.6 第 3 条）；TestFlight 自动共享（第 2 条） |

**未核实项（一律不入量化基线）**：`.ips` 数据保留期、可见主体、服务端处理时限、上传端点；崩溃报告是否进入 iCloud 备份；内核日志的外发形态；`DiagnosticLogs` 目录的官方说明页。

**一条抓取失败登记**：developer 文档《Analyzing crash reports of embedded apps》本地缓存 `_tmp/pe/analyzing-embedded-app-crash-reports.json` 实测为"该页面需要 JavaScript"的 SPA 落地页正文，**未取得文章主体** → 本篇不引用该页，嵌入式 App 的崩溃通道不写结论。

## 13.9 本章要点

1. 本层证据离开设备时的形态是**地址、权限字母、错误码、区域名**，不是页内容；Apple 逐字给的内容类别只有法务页那三类（13.4 第 1 点）。
2. **U1 的同意入口在设备侧，且用户可自行审阅**（`Analytics Data`）；隐私限定词三档（不记录 / 差分隐私 / 发送前移除）必须原样保留。
3. **U1′ 是唯一"内容级"通道**：交出去的是含页哈希结构的代码副本，但它发生在构建期、属 macOS 公证语境。
4. **U2 只有一句**可用（跨设备关联走端到端加密），崩溃报告是否入 iCloud 仍为 0 命中。
5. **U3 上本层最相关的那一类崩溃默认不可见**（invalid code-signature），可用替代是 TestFlight 自动共享与用户手动邮件分享；dSYM 配对会把随机化后的地址还原成符号，且 Apple 逐字要求脱敏。
6. MetricKit 的"上传方向"由 App 决定，不是 Apple 收到；粒度为日，且没有页权限字段。

# 14. 判定链、量化基线与自查清单

## 14.1 七道判定：从"有一页要执行"到"结论"

这七问按第 0.2 节的四层模型排列，每问都给出"能回答到什么强度"。用途是评审时逐条打勾，不是操作指引。

| # | 判定问题 | 一手依据 | 能得出的最强结论 | 不能得出的结论 |
|---|---|---|---|---|
| 1 | 这页内存属于哪一类对象？（签名文件映射 / 匿名 `MAP_JIT` / 其他） | `7c917bf14`、`15bfe098e`、`mmap.2:217-224` | 只有两条路径能取得可执行性；其他形态无一手依据 | "任何映射都可通过 `mprotect` 变可执行" |
| 2 | 该路径的前提是否满足？（签名有效 / 授权存在 / 单个 `mmap` / 随机地址） | `15bfe098e` 三条件、`kern_mman.c:479-482` | 手册三条件为合取，缺一即不符合口径 | "有授权就可多区域"（源码逐字：单区域） |
| 3 | 权限形态是否被允许？（W+X 组合、是否降级） | `vm_map.c:3180-3216`、`:3219-3262`、`vm_map_internal.h:923-941` | 非 macOS 上 JIT 区域必须一次到位 RWX，之后不许降级 | "iOS 上申请 W+X 会报错"（实际拿到可写不可执行） |
| 4 | 执行权限被撤销靠的是哪一层？（页表 / 每线程） | `8b776536b` APRR 句、`pthread_jit_write_protect_np.3` | 每线程撤销不需系统调用与页表刷新；页表权限修改权归 PPL/SPTM | "APRR 就是硬件 NX 位"、"切换线程开关等于改权限" |
| 5 | 载入时的按页校验是否实际执行？ | TN3126 三句、`vm_fault.c:2712-2735, 2803-2835` | 校验是**按需、可选**动作；默认开启依赖硬化运行时（macOS） | "每次页错误都会校验" |
| 6 | 若校验失败，进程会看到什么？ | `kern_cs.c:248-312`、`mprotect.2:83`、12.2–12.4 | 两种分支：`CS_KILL` → 终止；`CS_HARD` → 映射失败、进程继续 | "立刻 SIGKILL"（分支条件未公开为可配） |
| 7 | 这件事会不会留下可外发的证据？ | 12.6、13.4–13.6 | 报告字段有官方格式文档；但本层最相关一类崩溃不进 organizer | "后台能看到全部执行防护命中" |

**七问的顺序有意义**：1–3 是**准入**，4–5 是**维持**，6–7 是**后果**。流传口径几乎都错在把顺序打乱——例如用第 5 问的实现细节去回答第 2 问的准入条件。

## 14.2 量化基线（只收有出处的条目）

凡"未核实"一律不入表（14.4 单独列）。档位标 A / A-，与第 0.4 节一致。

| 项目 | 一手数值 / 范围 | 出处 | 档 |
|---|---|---|---|
| 非执行页标记的正式术语 | ARM 的 **Execute Never (XN)** 功能 | `15bfe098e`，2024-12-19 | A |
| XN 句点名平台 | 仅 **iOS 和 iPadOS**（同页标题含 visionOS） | 同页 | A |
| 写执行互斥的硬件原语 | **APRR（快速权限访问限制）**，"可基于每个线程"，"无需通过系统调用和页表寻访或刷新" | `8b776536b`，2026-01-28 | A |
| APRR 硬件起点 | **自 Apple A11 仿生和 S3 后的 SoC**；对照表首列 **A10** 为不可用 | `87716a080` / `8b776536b` | A |
| 世代矩阵列 | A10 ／ A11、S3 ／ A12-A14、S4-S10 ／ A15-A18、M1 ／ M2-M4 ／ A19 ／ M5 | `8b776536b` | A |
| SPTM 取代 PPL 的芯片 | **A15 或后续芯片和 M2 或后续芯片**（注释逐字） | 同页注释 1 | A |
| PPL / SPTM 点名平台 | PPL：iOS、iPadOS、visionOS、watchOS ／ SPTM+TXM：iOS、iPadOS、macOS、visionOS ／ 注释 2：macOS 以外所有平台（三处不一致，见第 15 章） | 同页 | A |
| JIT 例外的三道条件 | ① 内核检查 **Apple 专有的动态代码签名授权** ② 只有**单个 `mmap` 调用** ③ **随机地址** | `15bfe098e` | A |
| 每地址空间 JIT 区域数 | 注释逐字：**only have a single JIT region**, and only when it has the appropriate JIT entitlement | `bsd/sys/code_signing.h:706-713` | A- |
| JIT 策略函数数量 | **5 个**：`ALLOW_MULTIPLE_JIT` / `_RANDOM_ADDRESS` / `_INHERIT` / `_SHARING` / `_COPY` | `vm_map_internal.h:943-988` | A- |
| WX 策略函数 | **2 个**：`VM_MAP_POLICY_WX_FAIL`（非 alien 直接失败）、`VM_MAP_POLICY_WX_STRIP_X`（alien 降级） | `:923-941` | A- |
| `MAP_JIT` 允许的 `MAP_FIXED` 条件 | 注释逐字：MAP_FIXED with MAP_JIT is allowed for **arm64/4k** | `kern_mman.c:403-424` | A- |
| 非 macOS 上 JIT 区域必需权限 | **RWX 一次到位，此后不得降级**，违反返回 `KERN_PROTECTION_FAILURE` | `vm_map.c:3219-3262` | A- |
| `MAP_JIT` 禁止的组合 | `MAP_SHARED`、非 `MAP_ANON`、`MAP_RESILIENT_CODESIGN`、`MAP_RESILIENT_MEDIA`、`MAP_TPRO` → `EINVAL`；CSM 拒绝 → `EPERM` | `kern_mman.c:403-424, 479-482` | A- |
| 文件映射的 maxprot 基线 | `maxprot = VM_PROT_EXECUTE;`（带 TODO 注释）＋ FREAD 才加读位 | `kern_mman.c:620-627` | A- |
| 页校验的三态位宽 | `VMP_CS_BITS 4`、`ALL_FALSE 0x0`、`ALL_TRUE 0xF`；`cs_validated` / `cs_tainted` / `cs_nx` 各 4 位 | `memory_object_types.h:491-496` | A- |
| 校验失败的两种终止形态 | `CS_KILL` → `SIGKILL`；`CS_HARD` → 仅让映射操作失败 | `kern_cs.c:248-312` | A- |
| NX 页禁止映射为 EXEC 的返回值 | `KERN_CODESIGN_ERROR` ＋ 文案 `page marked to be NX, not letting it be mapped EXEC` | `vm_fault.c:2838-2871` | A- |
| 内核拒绝日志字段数 | 尾部括号 **10 项**（signed/validated/tainted/nx/wpmapped/dirty/depth + cs_mtime/mtime + 文件名） | `vm_fault.c:3063-3068` | A- |
| 页哈希槽（TN3126 示例） | **14 per-page hash slots and 7 special hash slots**；槽 −1 Info.plist、−3 resources、−5 entitlements | TN3126 | A（示例） |
| 页大小 | TN 示例仅 `Page size=4096`（macOS）；**iOS 侧未给数字** | TN3126 code listing | A（示例） |
| 哈希算法句 | targets macOS 10.12 或更新 → **SHA-256**；Apple 芯片代码 **never includes a legacy SHA-1** code directory；内核注释仍写 `verify the SHA1 hash for this page` | TN3126 / `vm_fault.c:8925` | A / A- |
| TN3126 修订记录 | 最新条目 **2024-02-06**；自我限定 these details aren't considered API | TN3126 | A |
| macOS 可逐项关闭的例外数 | **6 项** Runtime Exceptions；`introducedAt` 全部 **10.7**、平台全部 **macOS** | 授权属性页族 | A |
| 授权属性默认值 | default value … is **false**；includes an entitlement **only if the value is true** | 《Hardened Runtime》 | A |
| 调试授权会话时长 | **10 小时** | Debugging Tool 页 | A |
| 库验证通过条件 | 两个枚举项：**signed by Apple** 或 **same Team ID** | Disable Library Validation 页 | A |
| man page 日期戳 | `mmap.2` February 14, 2020；`mprotect.2` October 16, 2008；`pthread_jit_write_protect_np.3` May 1, 2020 | 各 man page `.Dd` | A- |
| 源码版本标签 | xnu main `f6217f89` / tag **`xnu-12377.121.6`**；libpthread main `42d026df5b07` / tag **`libpthread-539.100.4`** | 官方开源仓库 | A- |
| 页违反保护后的信号 | When a program violates the protections of a page, it gets a **SIGBUS or SIGSEGV** signal. | `mprotect.2:83` | A- |
| `.ips` 起点与类型码 | Starting with **iOS 15 and macOS 12**；`bug_type` **309** = crash、**288** = stackshot | Xcode 崩溃文档族 | A |
| `cpuType` 取值集合 | **ARM-64 / ARM / X86-64 / X86** 四值；Rosetta 下 `Code Type` 追加 `(Translated)`，否则 `(Native)` | 同上 | A |
| 异常类型释义条目数 | 该页以 **13 行**列出 **14 个键名** | Understanding the exception types | A |
| MetricKit 崩溃字段 | `MXCrashDiagnostic` **5＋1** 字段；标注 Use CrashDiagnostic instead；`iOS 14.0–27.2 Deprecated` | MetricKit 属性页 | A |
| `CrashDiagnostic`（新） | `introducedAt 27.0`，平台 iOS / iPadOS / Mac Catalyst / macOS / visionOS（**无 watchOS、无 tvOS**） | 同上 | A |
| 锁定模式可用版本 | iOS 16、iPadOS 16、macOS 13、watchOS 10 或更高版本 | `2437264f0`，2024-05-07 | A |
| 锁定模式相关页日期 | 105120 **2026-09-18**；`ipsd5baf79d0` **2025-10**；`2437264f0` **2024-05-07** | 各页 | A |
| watchOS 对口页点名技术 | 仅 **KIP、SKP 和 SCIP** | `c7d85209d`，2026-01-28 | A |
| 不进 Crashes organizer 的报告类型 | **4 类**：Watchdog / **Invalid code-signature crashes** / Thermal / Jetsam | Acquiring crash reports… | A |
| 法务页数据类别 | 三类：硬件与操作系统规格 / 性能统计 / 使用数据；限定词三档 | device-analytics，2026-09-14 | A |
| 手册镜像检索基数 | `_apsgh/` 189 个 HTML、`_tmp/sec*.txt` **179 个文本页**；TOC 去重页面名 **207**，含 "tvOS" 标题 **0** | 本轮实测 | 检索事实 |
| Rosetta 2 页校验句 | 内核仍会**验证每个 x86_64 页的代码哈希值** | `ebb113be1`，2021-02-18 | A |

## 14.3 22 条自查清单

**A. 构建与签名（1–8，适用于 macOS；iOS 侧多数项为"确认不存在"）**

1. 嵌入的 entitlements 中是否出现 `com.apple.security.cs.allow-jit`？出现则确认是否真的采纳了硬化运行时（未采纳时该键不参与判定）。
2. 是否同时出现 `allow-unsigned-executable-memory` 与 `disable-executable-page-protection`？后者 encompass 前者 → 冗余，按后者记账。
3. 是否存在 `disable-executable-page-protection`？若存在，**分别**评估 Intel 与 Apple 芯片代码上的削弱幅度（第 9.5 节）。
4. 是否存在 `disable-library-validation`？确认是否属于插件宿主这一 Apple 明示用途。
5. `allow-dyld-environment-variables` 单独出现时，确认库验证仍开启（否则两项叠加，第 9.3 节第三条）。
6. 授权是否加在**宿主可执行文件**上（dylib/framework 的 plist 不生效）？
7. 系统扩展是否携带除 `allow-jit`（非 DEXT）之外的任何例外？与 Apple 成文禁令冲突。
8. 是否出现值为 `false` 的例外键？与"false 不写入签名"的规则不符 → 回溯手工注入来源。

**B. JIT 例外与运行时（9–14）**

9. JIT 宿主是否只有**单个** `MAP_JIT` 区域（一手承诺为单区域）。
10. 是否使用了被禁的组合（`MAP_SHARED`、文件映射、`MAP_TPRO`、弹性签名）？
11. 非 macOS 上是否存在"先 RWX 再降级"的期望？一手实现明确拒绝降级。
12. macOS 目标是否采纳 `jit-write-allowlist`？采纳后 `pthread_jit_write_protect_np()` 不可再调用。
13. 白名单是否覆盖运行期 `dlopen` 的第三方 dylib？默认**不覆盖**。
14. iOS 目标是否误以为可用 `pthread_jit_write_protect_np`？一手文本：该函数在 iOS 不可用。

**C. 可观测性与取证（15–19）**

15. 崩溃解析规则是否区分平台？（macOS：`Exception Codes` 含 subtype；iOS：`Exception Subtype`）。
16. 是否把"possible pointer authentication failure"当作确定归因？须按 Apple 的反向提示复核高位。
17. 是否用 `pc` 与异常地址比较判定 invalid instruction fetch（12.9 第 4 步）。
18. 是否期待在后台看到签名类崩溃？Apple 明示该类**不进** Crashes organizer → 走 `Analytics Data` + 邮件分享。
19. 取证前是否确认设备不在调试会话（调试会拦截崩溃、且引入"调试区域"这一独立例外）。

**D. 上传与合规（20–22）**

20. 分享崩溃报告前是否按 Apple 逐字要求脱敏（App 名与 bundle ID 替换为 redacted 字符）。
21. 是否理解 dSYM 上传后 Xcode 会自动符号化 → 随机化地址在服务端可还原为符号。
22. U1 是否开启（`共享 iPhone 分析` / `共享 iPhone 与手表分析`）；用户可通过 `Analytics Data` 自行审阅将外发的内容。

## 14.4 明确不入表的项

以下七项**常被写成数字，但本篇未取到一手数值**，故不进 14.2：iOS 页哈希粒度（16 KB 说法）、iOS 上按页校验的默认开启主体、`KERN_CODESIGN_ERROR` 的报告字段落点、崩溃数据保留期与可见主体、Catalyst 下的执行权限模型、APRR 寄存器名与切换代价、`.ips` 上传端点。逐项出处与检索词见第 15 章 N 类与附录 B。

## 14.5 本章要点

1. 七道判定按"准入 → 维持 → 后果"排序，流传误读多因顺序错乱。
2. 量化基线里**只有两档**：A 级（Apple 成文）与 A- 级（当前实现）；所有"示例值"（页大小 4096、槽 14+7）已在表内标注为示例而非承诺。
3. 22 条清单中，1–8 与 20–22 属于"配置与合规"，9–14 属于"例外形态"，15–19 属于"取证"——**三组不可互相替代**，尤其不可用 15–19 的报告字段去反推 1–8 的配置状态。
4. 14.4 那七项是本篇的射程边界，任何补齐它们的数字都必须另找一手来源。

# 15. 口径冲突、缺口与必须加限定语的表述

## 15.1 口径冲突：Apple 在不同文本里说法不一致的八处

"冲突"在这里指**同一发布方给出范围不一致的两组表述**，本篇一律并列登记、不自行调和。

| # | 冲突对 | 一侧原文 | 另一侧原文 | 本篇处理 |
|---|---|---|---|---|
| C1 | PPL 与 SPTM 的平台集合 | 正文段：PPL 点名 iOS、iPadOS、visionOS、watchOS；SPTM+TXM 点名 iOS、iPadOS、macOS、visionOS | 同页对照表注释 2：PPL 与 SPTM「会在 macOS 除外……的所有平台上强制执行受信任的签名代码」→ 含 tvOS 与 watchOS | **不补齐**。哪个集合生效属未核实；第 10 章只写"三处集合不一致" |
| C2 | 页哈希算法 | TN3126：targets macOS 10.12 或更新 → 单一 code directory 使用 SHA-256；Apple 芯片代码 never includes a legacy SHA-1 directory | 内核注释：`/* verify the SHA1 hash for this page */`（`vm_fault.c:8925-8950`） | 判为**注释陈旧**，不得据注释推断运行时算法（A 级文档优先） |
| C3 | iOS 上能否调用切换函数 | 《Porting JIT》：逐字 `The pthread_jit_write_protect_np() function isn't available on iOS.` | libpthread man page：以 `pthread_jit_write_protect_supported_np` 返回 0/1 表达"平台支持与否"，且 unsupported 平台上调用为 no-op | 以**文章口径为准**（A 级），man page 作为实现层并存登记（A- 级），不写成矛盾结论 |
| C4 | 页校验的默认强度 | 手册：「运行时，所有可执行内存页的代码签名检查会在载入时进行」（A 级，无条件句） | TN3126：`macOS doesn't *always* check code as it's paged in`，且默认校验是 hardened runtime 的 opt-in 特性 | **两句并引**。手册句给"应然"，TN 给"实然与开关"，第 8.3 节按此写 |
| C5 | 读/执行位能否单独强制 | `mmap.2:118-120`：`due to hardware limitations, on some platforms PROT_WRITE may imply PROT_READ, and PROT_READ may imply PROT_EXEC`（并提示可移植程序不要依赖单独强制） | 手册 APRR 段与《Porting JIT》的"读写与执行不能同时"表述（未提及平台例外） | 保留 man page 那句为**Apple 自认的平台差异**，不点名平台；W^X 一律加"有效"限定 |
| C6 | visionOS 的射程 | 页标题《iOS、iPadOS 和 visionOS 中的运行时进程安全性》 | 同页 XN 段逐字只写「iOS 和 iPadOS 使用 ARM 的 Execute Never (XN) 功能」 | 按**标题集合 ≠ 段落断言集合**处理（第 10.4 节），"visionOS 有 XN"标未核实 |
| C7 | 崩溃报告的平台可达性 | `CrashDiagnostic` 的 `platforms` 清单：iOS / iPadOS / Mac Catalyst / macOS / visionOS，**无 watchOS、无 tvOS** | 《Acquiring crash reports…》：`Crash reports from watchOS are available on the paired iPhone.` | 二者不矛盾（不同层面：SDK 可用性 vs 报告取得路径），但**不可互换引用**；第 12/13 章分别使用 |
| C8 | "非 DEXT 系统扩展唯一例外"到底是哪个键 | 本地 JSON 的 identifier 逐字 `com.apple.security.cs.allow-jit` | 更早一份简报记作 "Allow DYLD environment variables entitlement" | **以 identifier 为准**；旧记录判为链接标题误解析（15.2 第 3 条） |

另有一处**不构成冲突但必须说明的强度差**：手册说「内核会检查 Apple 专有的动态代码签名授权是否存在」，源码侧对应的是 `csm_allow_jit_region` 与"JIT 区域/调试区域"两个概念（第 7.3、11.7 节）。**手册没写"两个来源"，源码没写"Apple 专有的动态代码签名授权"这一中文词**——这不是矛盾，而是两套词汇，本篇不互换。

## 15.2 本轮推翻的三条旧结论（务必按纠正后写作）

1. **「`.ips` 无官方格式文档」不成立**。三篇 A 级 Xcode 文档逐字定义字段与异常类型（第 12.1 节）。旧断言的检索范围只覆盖 `guide/security` 手册全库，那一侧的 0 命中**属正确留白**，被写成"developer 站不存在"才是错误结论。→ 系列前篇 §14.3 里"符号化流程不属一手可核"的措辞，现可改为引用这三篇。
2. **「iOS 上无 `pthread_jit_write_protect_np`」与「man page 讨论 supported platforms」并不矛盾**（C3）；但旧简报据此把结论扩写成"匿名内存可执行无 developer 文档"这一更宽命题，仍属缺口。本轮**新增**可用一手：《Investigating memory access crashes》逐字给出 `nonexecutable memory regions` 与 `invalid instruction fetch` 两条判读口径，可替代"无一手表述"的写法。
3. **Hardened Runtime 页那条"唯一例外"的所指以 identifier 为准**（C8）。
4. **「上传内容范围无一手来源」已部分补齐**：法务页 device-analytics（2026-09-14）给出数据类别与三档隐私限定词（第 13.4 节）；U2 由此从"未核实"上调为**部分核实**。

这四条的共同教训写在方法层：0 命中只代表**检索范围**，不代表范围外不存在文档。第 12.1 节那条纪律（任何"无一手来源"必须附检索词、文件集与命中数）就是它的制度化。

## 15.3 未核实清单（N1–N13）

以下十三项本篇**不下结论、不给数字**，括号内为已尝试的检索或抓取。

1. **N1 iOS/iPadOS 页哈希粒度是否 16 KB**（TN3126 内 grep `16384`/`16K`/`pagesize` → 0；示例只有 `Page size=4096`，且该值仅存在于 TN 的 JSON 变体中，转本文本丢失代码块）。
2. **N2 visionOS 的 XN**（XN 段逐字只点名 iOS 与 iPadOS）。
3. **N3 watchOS / Apple tvOS 的 XN、APRR、PPL、页级校验是否适用**（只能靠总述句 + 世代矩阵 + 注释 2 跨页组合；`mmap.2` 是唯一的显式并列；watchOS 对口页 grep `Execute Never`/`PPL`/`SPTM`/`页表` → 0）。
4. **N4 PPL/SPTM 平台集合冲突中哪个集合生效**（C1）。
5. **N5 Mac Catalyst / 在 Mac 上运行的 iOS App 走哪套口径**（手册内 Catalyst 命中 2 页，均与本层无关）。
6. **N6 iOS 侧"动态代码签名授权"的公开键名与申请条件**（`com.apple.security.dynamic-codesigning` 文档 JSON 实测 HTTP 404；手册只给中文描述）。
7. **N7 APRR 的寄存器名、指令序列、切换代价（周期数/TLB 行为）**（手册与公开源码均无；`APRR` 在 libsystem 仓库树内 grep → 0）。
8. **N8 `KERN_CODESIGN_ERROR` 在 `.ips` 中的呈现位置**，及它与 `Termination Reason` 文案的对应（手册全库 grep 该错误码 → 0 页）。
9. **N9 `CS_KILL` / `CS_HARD` 是否可由开发者配置、各平台进程默认取哪一支**（`codesign.h` 存在 `CS_OPS_*`，成文说明缺失）。
10. **N10 崩溃/分析数据的保留期、可见主体、服务端处理时限、上传端点**（四篇 Xcode 崩溃文档 + `CrashDiagnostic` + MetricKit 缓存内 0 命中）。
11. **N11 `MXForegroundExitData` 逐项字段与 `MemoryExceptionDiagnostic` 属性**（承系列前篇未核实项）。
12. **N12 内核 `PMAP_OPTIONS_XNU_USER_DEBUG`（`vm_map.c:11030`）与本层的关系**（仅登记常量存在）。
13. **N13 `cs_validate_page` / `cs_validate_range` 的实现体**（本轮缓存内只有调用点与头文件声明；疑在 `bsd/kern/kern_codesigning.c`，未展开新仓库抓取）。

## 15.4 必须加限定语的十八种表述

格式：**误读写法 → 应写成 → 约束来源**。

1. **"iOS 有 DEP/NX"** → Apple 一手只写「iOS 和 iPadOS 使用 ARM 的 Execute Never (XN) 功能提供进一步的保护，该功能会将内存页标记为不可执行」；手册全库不使用 DEP/数据执行保护/PXN/UXN/NX（零命中，`DEP` 字符串唯一命中上下文是「ADEP，Apple 开发者企业计划」）。
2. **"XN 与 APRR 是一回事"** → 必须分层：XN 是架构能力，APRR 是 Apple SoC 的每线程寄存器原语，官方收益句逐字「可从内存移除执行权限，无需通过系统调用和页表寻访或刷新」，硬件范围「自 Apple A11 仿生和 S3 后的 SoC」。不得写"APRR 就是硬件 NX 位"。
3. **"W^X 是绝对保证"** → 必须保留手册的降级词**"有效"**：「因为内存**无法在读写的同时有效执行**」；且 W+X 例外的三条件必须**合取**给出。
4. **"申请可写可执行页会失败"** → 分平台：iOS/watchOS/tvOS 上是"A writable, but not executable mapping is returned"，macOS 上是"will fail with MAP_FAILED"。此差异手册无对应句，只能引 man page，且**不得把手册与 man page 混为同一档**。
5. **"iOS 应用可以用 `pthread_jit_write_protect_np` 切换"** → 错；A 级逐字「isn't available on iOS」。iOS 侧 WebKit 的切换方式 Apple **未公开**（0 命中）。
6. **"每次页错误都会做签名校验"** → 必须引 TN 的自我设限句「macOS doesn't *always* check code as it's paged in」并说明 lazily 含义；有 CSM 时另有实现侧旁路（「In the code signing monitor we trust :)」，A- 级）。
7. **"页校验失败进程立刻被杀"** → 必须分两档并保留前提：`CS_KILL` 与 `CS_HARD` 的注释逐字不同；判定链以 `cs_enforcement_enabled` 为分支。手册只承诺「会在载入时进行」，**未**承诺 SIGKILL。
8. **"PPL/SPTM 在所有平台都管页表"** → 必须引 macOS 例外理由句原样（「此保护在 macOS 上不提供，因为 PPL 只在所有执行代码都必须签名的系统上适用」＋注释 2），并保留 C1 的集合冲突。
9. **"锁定模式关闭 JIT"** → 只能三句并读：`ipsd5baf79d0`（点名 JIT，含"除非排除受信任站点"）、105120（"某些复杂的网页技术会被阻止"＋按站点排除）、`2437264f0`（只给范围清单，**未出现"JIT"**）。三者不可互替。
10. **"Intel 与 Apple 芯片在这一层等价"** → 两句一手分叉（启用范围、`disable-executable-page-protection` 幅度）；但 man page 那句"on some platforms"未点名平台，不得指定 Intel 或某 SoC。
11. **"iOS 页哈希按 16 KB 计算"** → 不得给数字（N1）。可写上限只有：TN 示例 `Page size=4096`（macOS）＋ A- 级 `VMP_CS_BITS 4` 的分块记账。「14 per-page hash slots and 7 special hash slots」是**示例结构不是承诺**，且 TN 自陈「these details aren't considered API」。
12. **"崩溃报告能直接看出是被非执行页拦下"** → 必须给方法而非结论：`KERN_PROTECTION_FAILURE` 的释义含"nonexecutable memory regions"，再用 `pc` 与异常地址比较区分 invalid instruction fetch；权限读法只有 `PRT/MAX` 三字母可用。
13. **"possible pointer authentication failure 就是 PAC 命中"** → 必须保留 possible 与 Apple 的反向提示：「An invalid memory access, where high-order bits are erroneously set, can look like a pointer authentication failure」。
14. **"MetricKit 能上报页权限事件"** → 不得写。检索 `executable`/`protection`/`MAP_JIT`/`jit`/`page`/`nx` 在相关页 → 0 命中；可观测维度只有 exception/signal/virtualMemoryRegionInfo/terminationReason + 回溯；`MXCrashDiagnostic` 已标注「Use CrashDiagnostic instead.」，框架粒度为**日报**。
15. **"页权限信息会随诊断上传给 Apple"** → 手册未承诺。U1 只承诺「触发建议的 App 或导致建议显示的原因」，且判定在设备端（「使用设备端分析系统」）；保留期/可见主体 0 命中。
16. **"invalid code-signature 崩溃能在开发者后台看到"** → 错，Apple 明示该类**不进 Crashes organizer**。这是本章最实用的一条限定。
17. **"macOS 有硬化运行时所以比 iOS 更严"** → 反向。一手事实是 iOS 侧「要求所有可执行代码均使用 Apple 颁发的证书进行签名」且无可逐项关闭的公开开关；macOS 是"默认开、可按项关"，且 PPL/SPTM 不适用于 macOS。
18. **"Apple 一手没有对 X 的表述"** → 本轮出现反例须引以为戒（15.2 第 1 条）。任何此类结论必须附检索词、文件集与命中数，否则不得下。

## 15.5 五条最常见的读法错误（简版，供快速自查）

1. 把"默认不可执行"读成"注入的代码一定执行不了"——手册强度止于「提供进一步的保护」「减少攻击面」。
2. 把 JIT 例外读成"开发者可申请的能力"——iOS 侧一手文本只点名 Safari 的 JavaScript JIT 编译器。
3. 把"RWX 不许降级"读成"没有防护"——真正的收口在每线程写保护与白名单回调（第 6.1、7.5 节）。
4. 把手册的无条件句与 TN 的 opt-in 句只引其一（C4）。
5. 期待后台看到签名类崩溃（15.4 第 16 条）。

## 15.6 本章要点

1. 八处口径冲突**全部并列登记、不调和**；其中 C1（平台集合）、C2（SHA-1/SHA-256）、C4（校验强度）三处最容易被写成单侧结论。
2. 本轮推翻四条旧结论，核心是一条方法纪律：0 命中要写清范围。
3. 十三项未核实里，**N1、N6、N8、N10** 四项是流传数字与"确定结论"的主要来源，评审时优先复核。
4. 十八种表述里，第 6、7、11、14、16 条直接决定"能不能把某现象写进结论"；第 17 条决定跨平台比较的方向。

# 16. 结论与分角色建议

## 16.1 回到第 0.1 节那五个问题

1. **默认状态下，一页内存可执行吗？** 不可执行，且依据只有一句：「iOS 和 iPadOS 使用 ARM 的 Execute Never (XN) 功能提供进一步的保护，该功能会将内存页标记为不可执行」（`15bfe098e`，A）。这是手册全库**唯一**直接描述"页被标为不可执行"的句子（`不可执行` 与 `Execute Never` 各命中 1 页）。
2. **"可执行"这个属性从哪里来？** 从签名来。可执行性的常态授予路径是「这些操作系统要求所有可执行代码均使用 Apple 颁发的证书进行签名」（`7c917bf14`，A）；文件映射一侧的实现基线是 `maxprot = VM_PROT_EXECUTE;`（A-）。**执行位不是先验属性，而是签名体系派生的结果**——这是本篇最重要的一条结构性认识。
3. **谁能把它改成可执行？** 两条路径，权力都在别人手上：文件映射由签名决定，匿名例外由「Apple 专有的动态代码签名授权」决定，而该授权的键名与申请条件**未公开**。App 自己在运行期只能做"逐线程开/关写保护"这一动作，不能改变权限形态。
4. **系统允许存在"既可写又可执行"的页吗？** 允许，但被压到极窄：单个区域、单个 `mmap`、随机地址、非 macOS 上必须一次到位且不许降级、每线程互斥、白名单回调与冻结，且一手文本点名的使用者只有 Safari 的 JavaScript JIT 编译器。
5. **一次违规会留下什么？会离开设备吗？** 留下 `KERN_PROTECTION_FAILURE` / `EXC_BAD_ACCESS (SIGBUS 或 SIGSEGV)` 形态的报告与一行七状态内核日志（后者为 A- 级）；报告字段有 A 级格式文档；但**签名类崩溃不进 Crashes organizer**，而 U1 是否开启取决于用户在"分析与改进"里的同意。

## 16.2 四层各自的强度上限

| 层次 | Apple 一手表述达到的强度 | 本篇能给的最强结论 | 不能给的结论 |
|---|---|---|---|
| ① 架构（XN） | 「提供进一步的保护」「将内存页标记为不可执行」 | 数据页默认不可执行是成文规则 | "架构保证攻击者写入的字节永不执行" |
| ② SoC 原语（APRR） | 「可从内存移除执行权限，无需通过系统调用和页表寻访或刷新」「无法在读写的同时**有效**执行」 | 撤销执行权限代价低，且面向网页 JIT 场景 | "APRR 是绝对互斥"（man page 自认某些平台读位可推可执行） |
| ③ 问责（KIP/PPL/SPTM+TXM/SCIP） | 「旨在防止……」「大大减小了攻击面」，macOS 明确不适用 | 改页表执行位的权力被收给可信组件（iOS 侧） | "所有平台都由同一组件管页表"（C1 冲突） |
| ④ 准入（签名 / `MAP_JIT` / 授权） | 「只有处于严格的控制条件下」；「The Hardened Runtime disallows this by default, because it creates a security risk」 | 例外是具名、单区域、可审计的 | "iOS 第三方也能申请 JIT" |

**四行合起来读**：这一层的公开承诺是"默认拒绝 + 具名例外 + 有人负责维持"，**而不是"不可能"**。任何把它写成后者的表述，无论出自厂商材料还是研究博客，都超出一手文本射程。

## 16.3 分角色建议

**iOS / iPadOS App 开发者**

1. 默认前提下不要设计"需要可写可执行内存"的功能路径：一手文本里第三方 JIT 渠道 0 命中，`allow-jit` 文档页 `platforms` 为空数组。若依赖解释型/正则/脚本引擎，按 Apple 的原话预期其降级形态（「may fall back to an interpreter」）。
2. 不要把手册句「申请 W+X」当作会失败的操作：iOS 上它**静默返回可写不可执行**（第 9.7 节），错误检查逻辑容易写错位置。
3. 需要 JIT 的自建引擎若只在 macOS 分发，走 `MAP_JIT` + `allow-jit` 正门，并预期采纳 `jit-write-allowlist` 后 `pthread_jit_write_protect_np()` 被禁用这一代价。

**macOS App 开发者 / 发布负责人**

4. 把第 14.3 节 A 组八项纳入构建检查，判据是"**键是否存在**"（默认 false 且不写入签名），而不是"值是否为 true"。
5. 见到 `disable-executable-page-protection` 时，分别评估 Intel 与 Apple 芯片两片语义（第 9.5 节），并按 TN3126 的官方语气对待（`Don't do that!`）。
6. 系统扩展只允许 `allow-jit`（非 DEXT），其余例外键与 Apple 成文禁令冲突（第 9.8 节）。
7. 公证路径意味着必须采纳硬化运行时——所以"不采纳 HR 就能用 `MAP_JIT`"这条条件句在需公证的分发形态上不可用。

**安全评审 / 配置审计**

8. 审计粒度到宿主可执行文件；不要把 dylib 的 plist 当证据（继承规则，第 9.8 节）。
9. 区分三种"可执行例外"来源：签名页、JIT 区域、调试区域（第 11.7 节）。把第三类读成第二类是最常见的误判。
10. 报告"覆盖面收窄"时只引 Apple 的后果句，不外写利用含义（第 9.1 节第 2 条）。
11. 把用户侧的锁定模式与其"按站点排除"记入**设备基线**，不记入构建产物。

**事件响应 / 取证**

12. 按 12.9 的五步顺序读报告；第 4 步（`pc` 与异常地址比较）是 Apple 给的唯一判定方法。
13. 不要期待后台出现签名类崩溃；改走 `Analytics Data` + 邮件分享，并确认设备当时不在调试会话。
14. 遇到 `possible pointer authentication failure` 文案时，先按 Apple 的反向提示复核高位误置，再考虑与本篇第 95 篇（指针认证）联合归因。
15. 内核那行七状态日志是 A- 级实现细节，**不要**在对外报告里把它当公开接口引用。

**研究与教学**

16. 引用执行限制时先声明层次（第 0.2 节四层），否则"iOS 有 DEP"这类旧词会把讨论带偏。
17. 跨平台外推必须有 Apple 明文；TN3126 的外许可句（examples taken from macOS, concepts apply to all Apple platforms）是 TN 自己的话，而 man page 的平台分叉句正好证明**不能默认一致**（第 0.4 节第 3 条）。
18. "无一手来源"必须附检索词、文件集与命中数（15.4 第 18 条）。

## 16.4 本篇能支持与不能支持的判断

**能支持**（逐条有 A 或 A- 出处）：默认不可执行的成文规则；可执行性由签名派生；例外的三条件与六道收口；macOS 六项例外及其后果句；按页校验为按需且可被 opt-out；页校验失败的两种处置分支；崩溃报告字段与判读方法；四类不进 organizer 的报告；三条上传通道的内容射程。

**不能支持**：任何"某页一定能/一定不能执行"的绝对判断；iOS 页哈希粒度、保留期、上传端点等未核实数值；把 `CS_KILL`/`CS_HARD` 说成可配置；把 C1 的三个平台集合之一当定论；任何绕过、伪造或制造可执行内存的方法（本篇始终未写，第 0.6 节的边界在结论处同样适用）。

## 16.5 结语

「限制特定内存页面执行指令」在 Apple 一手文本里不是一句承诺，而是**四段彼此独立的说明**：架构能标记、SoC 能低成本撤销、可信组件负责维持修改权、签名与具名授权决定谁可以例外。真正可读的规则是这四点叠加后的形态——**默认不可执行、例外具名且被压窄、校验按需而非始终、后果依进程标志分叉**。把这套机制当"绝对边界"会低估例外面，当"可关掉的选项"会高估第三方权力，两个方向的误读都源于跳过了某一层的原文。

# 附录 A 来源清单

**方法说明**：以下每一行都在本篇正文中被逐字引用过。手册页给出 secid、页面标题（取自本地镜像首行）与页面标注的发布日期；developer 文档多数只在页脚标注 © 2026，本篇记为"截至 2026 可用"；源码一律给仓库、分支/标签与 `文件:行号`。抓取失败与 404 单独列于 A.6，不得作为"内容不存在"的证据。

## A.1 《Apple 平台安全保护》手册页（zh-cn，A 级）

| secid | 页面标题 | 发布日期 | 本篇用途 |
|---|---|---|---|
| `15bfe098e` | iOS、iPadOS 和 visionOS 中的运行时进程安全性 | 2024-12-19 | XN 句、JIT 三条件、Safari 点名（第 2、5、7 章） |
| `7c917bf14` | iOS、iPadOS、Apple tvOS、visionOS 和 watchOS 中的 App 代码签名过程 | 2024-12-19 | 强制签名句、动态库验证、Team ID 规则（第 2、5、8 章） |
| `f49cad4db` | iOS、iPadOS 和 visionOS 中的 App 安全性介绍 | 2024-12-19 | 「所有可执行内存页的代码签名检查会在载入时进行」（第 8 章） |
| `8b776536b` | 操作系统完整性 | 2026-01-28 | KIP、APRR、PPL/SPTM、世代矩阵、注释 1/2（第 3、4、10 章） |
| `87716a080` | Apple SoC 安全性 | 2026-01-28 | APRR 中文表述与硬件起点（第 3 章） |
| `d698747c9` | 签名系统宗卷安全性 | 2022-05-13 | 文件级校验与内存级校验的分界（第 8.2 节） |
| `ebb113be1` | 搭载 Apple 芯片的 Mac 上的 Rosetta 2 | 2021-02-18 | 转译后仍逐页校验代码哈希（第 8.4 节） |
| `5599b66df` | macOS 中的门禁和运行时保护 | 2024-12-19 | 「从随机的只读位置打开 App」（第 10.1 节） |
| `3ad8e6e53` | macOS 中的 App 代码签名进程 | 2021-02-18 | 开发者 ID 签名 + 公证（第 13.3 节） |
| `c7d85209d` | watchOS 系统安全性 | 2026-01-28 | watchOS 点名技术集合仅 KIP/SKP/SCIP（第 10.2 节） |
| `2437264f0` | Apple 服务的锁定模式安全性 | 2024-05-07 | 锁定模式定义、权衡清单、可用版本（第 7.6 节） |
| `87fc038c2` | Apple 操作系统中的后台安全改进 | 2026-01-28 | 分析上传口径、设备端判定、回滚（第 13.4 节） |
| `d8831e732` | 屏幕时间安全性 | 2021-02-18 | 「只会收集以下匿名数据」的限定词（第 13.4 节） |

手册页 URL 形态：`https://support.apple.com/zh-cn/guide/security/<secid>/web`；《守护你的个人安全》属另一子系列：`https://support.apple.com/zh-cn/guide/personal-safety/ipsd5baf79d0/web`（页面标注发布日期 **2025-10**，A 级，唯一逐字点名 JIT 的锁定模式页）。

## A.2 developer.apple.com 文档（A 级，"截至 2026 可用"）

| 页面 | URL 路径 | 本篇用途 |
|---|---|---|
| Hardened Runtime | `documentation/security/hardened-runtime` | 总纲句、默认 false、只加可执行文件、系统扩展禁令、公证强制（第 9 章） |
| Allow execution of JIT-compiled code entitlement | `documentation/bundleresources/entitlements/com.apple.security.cs.allow-jit` | 摘要句、用途清单、降级句、DRM 迁移句、`platforms` 空数组（第 7.2、9.2 节） |
| Allow Unsigned Executable Memory Entitlement | `…/com.apple.security.cs.allow-unsigned-executable-memory` | 六项表第 2 行 |
| Disable Executable Memory Protection Entitlement | `…/com.apple.security.cs.disable-executable-page-protection` | 六项表第 3 行、包含关系句 |
| Disable Library Validation Entitlement | `…/com.apple.security.cs.disable-library-validation` | 六项表第 4 行、Apple 签名/同 Team ID 两句 |
| Allow DYLD Environment Variables Entitlement | `…/com.apple.security.cs.allow-dyld-environment-variables` | 六项表第 5 行、组合提示 |
| Debugging Tool Entitlement | `…/com.apple.security.cs.debugger` | 六项表第 6 行、10 小时会话（第 11.7 节） |
| Porting just-in-time compilers to Apple silicon | `documentation/apple-silicon/porting-just-in-time-compilers-to-apple-silicon` | 授予边界、启用范围按芯片分叉、iOS 不可用句、白名单代价（第 6、7、10 章） |
| Interpreting the JSON format of a crash report | `documentation/xcode/interpreting-the-json-format-of-a-crash-report` | `.ips` 起点、两个 JSON 对象、`bug_type` 309/288、元数据属性（第 12.1 节） |
| Examining the fields in a crash report | `documentation/xcode/examining-the-fields-in-a-crash-report` | 字段定义、`Termination Reason` 的 invalid code signature、`Code Type`（第 12.1、12.4 节） |
| Understanding the exception types in a crash report | `documentation/xcode/understanding-the-exception-types-in-a-crash-report` | 13 行 / 14 键名释义表（第 12.2 节） |
| Investigating memory access crashes | `documentation/xcode/investigating-memory-access-crashes` | `KERN_PROTECTION_FAILURE`、invalid instruction fetch、`PRT/MAX`、arm64e 限定句（第 12.3 节） |
| Acquiring crash reports and diagnostic logs | `documentation/xcode/acquiring-crash-reports-and-diagnostic-logs` | 四类不进 organizer、TestFlight 自动共享、watchOS 经配对 iPhone、`Analytics Data` 手动分享（第 12.6、13.6 节） |
| Adding identifiable symbol names to a crash report | `documentation/xcode/adding-identifiable-symbol-names-to-a-crash-report` | debug symbols 自动符号化、脱敏义务（第 13.6 节） |
| MetricKit：`MXCrashDiagnostic` / `CrashDiagnostic` / 框架首页 | `documentation/metrickit/…` | 六个属性、弃用标注、平台清单、日报粒度（第 12.7 节） |

## A.3 技术说明（A 级）

TN3126《Inside Code Signing: Hashes》 https://developer.apple.com/documentation/technotes/tn3126-inside-code-signing-hashes ，修订记录最新条目 **2024-02-06**。本篇引用：per-page 与 special 哈希槽（14 + 7）、载入时按需校验、`macOS doesn't *always* check`、Intel 与 Apple 芯片语义分叉、槽 −1/−3/−5、SHA-256 与 legacy SHA-1，以及 TN 的自我限定「these details aren't considered API」。示例值 `Page size=4096` 取自 TN 的 JSON 变体（转本文本丢失代码块），引用时须注明。

## A.4 支持文章与法务页（A 级）

| 页面 | URL | 标注日期 | 本篇用途 |
|---|---|---|---|
| 关于"锁定模式" | https://support.apple.com/zh-cn/105120 | 2026-09-18 | 「某些复杂的网页技术会被阻止」＋按站点排除 |
| Device Analytics & Privacy | https://www.apple.com/legal/privacy/data/en/device-analytics/ | 页尾 2026-09-14 | 数据类别三档、设备侧可审阅、定位条件、iCloud 账户关联与端到端加密（第 13.4、13.5 节） |

## A.5 Apple 官方开源仓库（A- 级）

| 仓库 | 版本 | 本篇引用位置 |
|---|---|---|
| `apple-oss-distributions/xnu` | main `f6217f89` / tag `xnu-12377.121.6` | `bsd/kern/kern_mman.c:403-424, 434-476, 479-482, 552-554, 620-627`；`bsd/kern/kern_cs.c:220, 245, 248-312`；`bsd/sys/codesign.h:41-61`；`bsd/sys/code_signing.h:706-730`；`osfmk/vm/vm_map.c:892-915, 3180-3216, 3219-3262, 11030, 19889-19893`；`osfmk/vm/vm_map_internal.h:923-941, 943-988`；`osfmk/vm/vm_fault.c:2712-2735, 2780-2795, 2803-2835, 2838-2871, 2873-2891, 3063-3068, 4773-4777, 8925-8950, 9105-9118`；`osfmk/mach/memory_object_types.h:491-496` |
| `xnu/bsd/man/man2`（同仓库） | 同上 | `mmap.2`（`.Dd February 14, 2020`）:114-115, 118-120, 122-137, 217-234, 399-409；`mprotect.2`（`.Dd October 16, 2008`）:80-81, 83, 110-113 |
| `apple-oss-distributions/libpthread` | main `42d026df5b07825070f60134b980a1ec2552dfee` / tag `libpthread-539.100.4` | `man/pthread_jit_write_protect_np.3`（`.Dd May 1, 2020`）NAME/DESCRIPTION/ENTITLEMENTS/SECURITY CONSIDERATIONS；`tests/pthread_jit_write_with_callback-ios-entitlements.plist` 与 `-macos-…` 两份测试授权文件 |

**A- 级的使用限制**（第 0.4 节第 1 条）：源码注释、策略宏与测试 plist 只说明"当前实现这么写"，不构成对外承诺。本篇凡引用之处均标 A-。

## A.6 抓取失败与 404 登记（不作为"不存在"的证据）

1. `documentation/bundleresources/entitlements/com.apple.security.dynamic-codesigning` 的文档 JSON → **HTTP 404**（返回 404 HTML）。
2. `pthread_jit_write_protect_np` 的 developer 网页文档：slug 前缀 `pthreads/`、`pthread/`、`darwin/`、`kernel/`、`bsd/`、`system/pthread/` 各取该符号名 → **6/6 HTTP 404**；`MAP_JIT`、`mmap`、`mprotect` 的符号页同样无命中。
3. `security.apple.com/blog/lockdown-mode/` 与 `blog/announcing-lockdown-mode/` → 返回 Apple Security Research 落地页正文，未取得文章主体；blog RSS 内 grep `JIT` → 0。
4. `documentation/xcode/analyzing-crash-reports-of-embedded-apps` → 本地缓存为"该页面需要 JavaScript"的 SPA 落地页，未取得主体（第 13.8 节）。
5. `cs_validate_page` / `cs_validate_range` 实现体未在本轮缓存内（N13）。
6. `com.apple.security.cs.allow-jit` 页 `platforms` 实测为空数组——本轮按"iOS 侧无公开 JIT 授权文档标注"使用，但需复核是否抓取截断（附 B 待补第 5 项）。

## A.7 检索基数与镜像

`_apsgh/` **189 个 HTML**（手册 zh-cn 镜像）、`_tmp/sec*.txt` **179 个文本页**（去导航转本）、`_tmp/pe/` 与 `_tmp/` 下的 developer / TN / 源码 / 支持文章 / 法务页缓存；TOC 去重页面名 **207** 个，含 "tvOS" 标题 **0** 个。凡"页级命中数"一律以 `_tmp/sec*.txt` 为准（对 HTML 直接 grep 会命中导航条目，高计数不可用）。

# 附录 B 术语表

## B.1 本篇使用的术语与对应一手形态

| 术语（本篇写法） | 一手形态（逐字） | 出处与档位 | 说明 |
|---|---|---|---|
| 执行限制 / 不可执行页 | 「使用 ARM 的 Execute Never (XN) 功能……将内存页标记为不可执行」 | `15bfe098e`，A | 本篇的"①架构层"，不用 DEP/NX 作主语 |
| 快速权限访问限制（APRR） | 「快速权限访问限制（也称为 APRR 寄存器）」 | `8b776536b`，A | ②SoC 原语层；缩写仅出现在该页，另一页用中文全称 |
| 内核完整性保护（KIP） | 「操作系统内核初始化完成后，会启用内核完整性保护 (KIP)」 | `8b776536b`，A | ③层起点；MMU 配置句在此 |
| 页面保护层（PPL） | 「iOS、iPadOS、visionOS 和 watchOS 中的页面保护层 (PPL)」 | 同页，A | ③层，A15/M2 之前 |
| 安全页表监控器 / 可信执行监控器（SPTM/TXM） | 「安全页表监控器 (SPTM) 和可信执行监控器 (TXM) 设计为协同工作」 | 同页，A | ③层，之后取代 PPL |
| 安全协处理器完整性（SCIP） | 「SCIP 的工作方式与 KIP 非常类似」 | 同页，A | 协处理器侧对称表述 |
| 动态代码签名授权 | 「内核会检查 Apple 专有的动态代码签名授权是否存在」 | `15bfe098e`，A | ④层准入；键名未公开（N6） |
| 代码签名监控器 | `csm_*` / `code signing monitor denies JIT mapping` | `vm_map.c:19889-19893`、`kern_mman.c:479-482`，A- | 与上一行**不是同一个词**，本篇不互换 |
| JIT 区域 / 调试区域 | 「An address space can only have a single JIT region…」/「…only have a debug region if it is currently being debugged」 | `code_signing.h:706-730`，A- | 例外的两种来源（第 11.7 节） |
| `MAP_JIT` | 「Allow mapping pages both PROT_WRITE and PROT_EXEC when the hardened runtime is enabled」 | `mmap.2:217-234`，A- | 手册层不含任何 API 名（grep 0） |
| 每线程写保护 | 「MAP_JIT regions are never writeable and executable simultaneously」 | `pthread_jit_write_protect_np.3`，A- | 与 APRR 互为表里 |
| JIT 写回调白名单 | `com.apple.security.cs.jit-write-allowlist` / `-freeze-late` | 同 man page ENTITLEMENTS，A- | 收紧方向（第 9.6 节） |
| 硬化运行时 | 「The Hardened Runtime disallows this by default, because it creates a security risk」 | `com.apple.security.cs.allow-jit` 页，A | macOS-only；iOS 无对应概念 |
| Runtime Exceptions | 「under Runtime Exceptions, select Allow Execution of JIT-compiled Code」 | 同上，A | 六项例外的界面容器名 |
| 载入时代码签名检查 | 「所有可执行内存页的代码签名检查会在载入时进行」 | `f49cad4db`，A | ④层校验；手册强度句 |
| 按需校验 | 「the kernel may choose to verify the hash of the page's contents … check its code signature lazily」 | TN3126，A | 强度止于"可选动作" |
| 按页哈希 / 哈希槽 | 「14 per-page hash slots and 7 special hash slots」 | TN3126，A（示例） | 示例结构，非承诺 |
| 弹性签名 | `MAP_RESILIENT_CODESIGN`（手册无中文术语） | `kern_mman.c:434-476`，A- | 首次出现给两形态（第 0.3 节） |
| 污染页 | 「CODE SIGNING: page is tainted」/ `VMP_CS_TAINTED` | `memory_object_types.h`、`vm_fault.c:2873-2891`，A- | 可写映射即可判污染 |
| `CS_KILL` / `CS_HARD` | 两句注释逐字 | `kern_cs.c:248-312`，A- | 处置分支；非开发者可配（N9） |
| 库验证 | 「signed by Apple or signed with the same Team ID」 | Disable Library Validation 页，A | 载入准入，与执行位不同源 |
| 签名系统宗卷（SSV） | 「所有读取和执行路径上的可执行代码均会受到 SSV 和代码签名的保护」 | `d698747c9`，A | 文件级 vs 内存级分界 |
| 锁定模式 | 「即时 (JIT) JavaScript 编译等某些复杂的网络技术会停用，除非……」 | `ipsd5baf79d0`，A | 唯一用户侧开关（第 7.6 节三句并引） |
| `.ips` 崩溃报告 | 「store the data as JSON in files with an `.ips` extension」 | Interpreting the JSON format…，A | `bug_type` 309/288 |
| 无效指令取指 | 「invalid instruction fetches … If the program counter register is the same as the exception address」 | Investigating memory access crashes，A | 本层唯一 Apple 给的判定方法 |
| `PRT/MAX` | 「current permission attributes … r / w / x」 | 同页，A | 权限列读法（第 12.3 节样例） |
| U1 / U1′ / U2 / U3 | 无（本系列自定义记号） | 第 13.1 节 | **不是 Apple 术语** |

## B.2 本篇不使用的词（负面术语登记）

下列词在流传讨论里高频出现，但**在《Apple 平台安全保护》中文手册全库（179 页）命中 0**，本篇只在需要指认误读时提及，绝不用于陈述机制：

| 词 | 命中数 | 说明 |
|---|---|---|
| `DEP` / `数据执行保护` | 0 有效 | `DEP` 字符串唯一命中上下文为「ADEP，Apple 开发者企业计划」 |
| `PXN` / `UXN` / ` NX `（作独立词） | 0 / 0 / 0 | 内核代码串里有 NX，但手册不用 |
| `需求页` / `强制页` | 0 / 0 | Apple 只说「会在载入时进行」「验证每个 x86_64 页的代码哈希值」 |
| `MAP_JIT` / `pthread_jit_write_protect` | 0 / 0 | 手册层不含 API 名 |
| `dynamic-codesigning` / `com.apple.security.cs` / `Hardened` | 0 / 0 / 0 | 手册不提任何 entitlement 键名 |
| `崩溃报告` / `分析数据` / `诊断数据` / `符号文件` / `dSYM` / `Analytics` | 0 ×6 | 手册无诊断与上传章节 → 需引 developer 文档与法务页 |
| `KERN_CODESIGN_ERROR` | 0 | 内核返回值在公开文档层无落点（N8） |
| `BTI` / `分支目标` / `GCS` / `Guarded` | 0 ×4 | 不得写"Apple 启用 BTI/GCS" |
| `页错误` / `签名页` | 0 / 0 | 实现概念，非成文术语 |
| `监督式` / `移动设备管理` / `MDM` / `开发者模式` | 0 ×4 | 与本层无一手关联句（第 11.8 节） |

**可用的低计数术语**（判别力强）：`不可执行` 1 页、`Execute Never` 1 页（均 `15bfe098e`）；`APRR` 1 页（`8b776536b`）；`页表` 2 页；`执行权限` 1 页；`可执行内存` 1 页（`f49cad4db`）；`即时`（JIT 相关）4 页其中有效 3 页。

# 附录 C 速查表

## C.1 四层一览

| 层 | 一句话 | 一手词 | 章 |
|---|---|---|---|
| ① 架构 | 页可以被标成不可执行 | Execute Never (XN) | 2 |
| ② SoC | 撤销执行权限不需系统调用与页表刷新 | APRR / 快速权限访问限制 | 3 |
| ③ 问责 | 改页表执行位的权力被收给可信组件（macOS 除外） | KIP / PPL / SPTM+TXM / SCIP | 4 |
| ④ 准入 | "既可写又可执行"要专门的授权，且只有一个区域 | 动态代码签名授权 / `MAP_JIT` / `allow-jit` | 5–8 |

## C.2 关键数字（全部有出处，示例值已标注）

| 项 | 值 | 档 |
|---|---|---|
| 手册里"页标记为不可执行"的句子数 | **1**（`15bfe098e`） | A |
| APRR 起点 | **A11 仿生与 S3 之后**；A10 不可用 | A |
| SPTM 起点 | **A15 或后续 / M2 或后续**，并取代 PPL | A |
| JIT 例外条件 | **3** 条（授权 + 单个 `mmap` + 随机地址） | A |
| 每地址空间 JIT 区域 | **1** | A- |
| JIT / WX 策略函数 | **5** 个 / **2** 个 | A- |
| `MAP_JIT` 禁止组合 | `MAP_SHARED`、非 `MAP_ANON`、`MAP_RESILIENT_CODESIGN`、`MAP_RESILIENT_MEDIA`、`MAP_TPRO` → `EINVAL` | A- |
| `MAP_FIXED` + `MAP_JIT` | 仅 **arm64/4k** 注释允许 | A- |
| 页三态位宽 | `VMP_CS_BITS` = **4**（validated / tainted / nx） | A- |
| 拒绝日志尾字段 | **10** 项（含 7 个状态位） | A- |
| 页哈希槽（示例） | **14 + 7**；特殊槽 −1 / −3 / −5 | A（示例） |
| 页大小（示例） | **4096**，且属 macOS 示例；iOS 未给数字 | A（示例） |
| macOS 可关例外 | **6** 项，全部 macOS 10.7 起 | A |
| 例外布尔默认 | **false**，false 不写入签名 | A |
| 调试授权会话 | **10 小时** | A |
| 库验证通过条件 | **2** 条（Apple 签名 / 同 Team ID） | A |
| `.ips` 起点与类型码 | iOS 15 / macOS 12；**309** crash、**288** stackshot | A |
| 异常类型释义 | **13 行 / 14 键名** | A |
| 不进 organizer 的报告 | **4** 类（含 invalid code-signature） | A |
| `CrashDiagnostic` 平台 | iOS / iPadOS / Mac Catalyst / macOS / visionOS（**无 watchOS、无 tvOS**），27.0 起 | A |
| 手册里 tvOS 专页标题 | **0 / 207** | 检索事实 |

## C.3 三平台一句话

- **iOS / iPadOS**：无可逐项关闭的公开开关；W+X 请求**静默返回可写不可执行**；JIT 例外由 Apple 具名保留；页表修改权归 PPL → SPTM+TXM。
- **macOS**：默认开、可按项关（六项）；无 PPL/SPTM；公证强制硬化运行时；页保护关闭幅度按芯片分叉。
- **watchOS / tvOS / visionOS**：Apple 只把它们放进总述句与标题，**无一层专述**；跨页组合推论必须标注。

## C.4 崩溃判读五步

1. `bug_type` = 309？`Code Type` 是否 `(Translated)`。
2. `Exception Type` 属 `EXC_BAD_ACCESS` 族还是 `EXC_CRASH (SIGKILL)` 族。
3. `Exception Subtype` 是否 `KERN_PROTECTION_FAILURE`；遇 possible PAC 文案先复核高位。
4. `pc` 与异常地址是否相同 → Apple 定义 invalid instruction fetch。
5. `VM Region Info` 的 `PRT/MAX`（`rw-/rwx`、`---/rwx`、`r-x/r-x`）与区域名。

**若指向签名校验失败**：后台看不到（不进 organizer）→ 请用户在 `设置 > 隐私与安全性 > 分析与改进 > 分析数据` 找 `<AppBinaryName>_<DateTime>` 并邮件分享；同时确认当时不在调试会话。

## C.5 上传四通道速查

| 通道 | 本层载体 | 覆盖度 | 硬限制 |
|---|---|---|---|
| U1 设备→Apple | 崩溃/分析条目 | 部分核实 | 同意在设备侧；判定也在设备侧；类别清单未提页权限也未排除 |
| U1′ 代码→Apple | Mach-O + Code Directory（页哈希） | 已核实 | 构建期，macOS 公证语境 |
| U2 设备→iCloud | 无一手关联句 | 部分核实 | 只有"跨设备关联走端到端加密"一句 |
| U3 设备→开发者 | exception / subtype / `PRT/MAX` / termination / 地址（+dSYM 还原符号） | 已核实 | **签名类崩溃默认不可见**；TestFlight 自动共享 |

## C.6 七项常被误写成数字的未核实项

iOS 页哈希粒度（16 KB 说）· 按页校验在 iOS 的默认开启主体 · `KERN_CODESIGN_ERROR` 的报告字段落点 · 崩溃数据保留期与可见主体与上传端点 · Catalyst 走哪套口径 · APRR 寄存器名与切换代价 · `.ips` 字段枚举常量表。

## C.7 引用纪律三条

1. A 级与 A- 级不互换：手册/API 文档给"应然"，源码给"当前实现"。
2. 标题的平台集合 ≠ 段落断言的平台集合（C1、C6 的教训）。
3. 任何"无一手来源"必须附检索词、文件集与命中数（第 15.2 节的教训）。

---

**联系方式**：Telegram: https://t.me/one00190
