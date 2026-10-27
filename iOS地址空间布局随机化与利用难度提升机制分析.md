@@TITLE@@iOS 地址空间布局随机化与利用难度提升机制分析
@@SUBTITLE@@What Actually Raises The Cost Of Exploiting A Memory Corruption Bug On A Single Process — The Randomized Objects Catalogue (Main Image, Dependent Libraries, Dyld Shared Cache Slide, Thread Stacks, Guard Pages, Malloc Zones, Kernel And Zone Allocators), Where The Entropy Comes From And Why Almost No Bit Count Is Published, Pointer Authentication As A Verifiable-Direction Layer, Branch Target And Execute-Never Constraints, Memory Tagging, Allocator And Metadata Hardening, Page-In Validation And Library Validation As The Second Line Of Defense, Who Can Legally Weaken Any Of It, The Observability Trail In Crash Reports, And What Happens To Layout Information When Diagnostics Are Uploaded
@@INFO@@报告日期|2026年10月22日
@@INFO@@分析对象|随机化对象（主可执行文件与依赖库加载位置、dyld 共享缓存 slide、主线程与线程栈、guard page、堆区与 malloc zone、映射区、内核文本与区分配器位置）· 熵的来源与质量（密码学伪随机数生成器与熵源、恢复与快照场景、位数口径的公开程度）· 指向可验证化（指针认证码 PAC：签什么、密钥与上下文、编译与签名侧要求、验证失败后果、覆盖不到的原语、arm64e 与 arm64_32 的差项）· 执行目的约束（分支目标识别、不可执行页与可执行内存申请、即时编译例外通道、锁定模式的即时编译限制）· 标记与边界（内存标记扩展及其官方口径位置）· 分配器加固（堆元数据保护、区校验、双重释放与非法写检测、调试态与生产态差异、内核区分配类型检查）· 第二道防线（按需页校验、代码签名与授权属性、库校验、可加载代码边界、会削弱防护的构建与签名选项）· 强度变更主体（开发者、用户、组织、Apple、硬件世代）· 可观测性（崩溃报告字段、指标上报、诊断日志、逆向与检测的表达边界）· 上传（崩溃与诊断数据离开设备时携带的地址、偏移与符号信息及其三条去向）· 平台与世代差异（iOS/iPadOS、macOS 与 Apple 芯片/Intel、watchOS/tvOS/visionOS、32 位遗留、Mac Catalyst 与在 Mac 上运行的 iOS App）
@@INFO@@视角定位|防御与研究教育视角：回答"一次内存破坏要变成可控执行，中间隔着哪几道彼此正交的门槛、每道门槛的强度从哪里来、在什么条件下失效、公开文本究竟承认到哪一步"，并给出可用于加固评审、机型治理与崩溃取证分析的判定链、量化基线与自查清单。本篇不包含任何绕过指针认证、绕过签名校验或削弱随机化的操作方法，不含利用代码，不含针对具体目标或第三方设备的攻击步骤；涉及"如何降低防护强度"的部分只描述官方公开的构建选项、授权属性与其后果，用于配置审计与整改，不给出执行步骤。
@@INFO@@分类|内存安全 · 利用缓解 · 二进制与签名 · 崩溃可观测性与诊断上传
@@INFO@@难度|中级偏高级（需了解虚拟地址空间与页表、栈与堆布局、返回地址与函数指针、代码签名与 entitlement 基本概念、崩溃报告字段含义）
@@INFO@@联系方式|Telegram: https://t.me/one00190

@@TOC@@目录

# 0. 概述、口径与阅读方式

## 0.1 本篇要回答的六个问题

一个"能改写一个字"的内存破坏缺陷，要变成"能执行任意代码"，中间到底隔着多少道彼此独立的门槛？这就是本篇的主题。它不是漏洞盘点，也不是利用教程；它是一份**机制与强度边界的评审文档**，回答六个问题：

1. **随机化到底随机了哪些东西。** 主可执行文件、依赖库、dyld 共享缓存的整体搬移量、主线程栈、新建线程栈、保护页、堆区与内存区、映射区、内核文本与区分配器位置——逐项给出"官方文本承认证到哪一步"，以及"只有开源代码或根本没有公开出处"。
2. **熵从哪里来，为什么几乎问不到位数。** Apple 手册与开发者文档对地址随机化**不提供任何比特数**；本篇把这条沉默本身作为一项可审计的事实登记下来，并给出唯一一条能间接说明"随机地址"落在具体对象上的句子。
3. **"指向哪里"如何变成可验证属性。** 指针认证码（PAC）签的是代码指针，用的是每进程独立的密钥，验证失败会留下特定异常；本篇同时写清它**打不掉**哪一类原语，以及不同指令集变体的覆盖差。
4. **"这块内存能不能执行"由谁决定。** 不可执行页、可写可执行页的三道并列例外条件、按线程撤销执行权限的硬件原语、即时编译例外通道、锁定模式对这个通道的收紧。
5. **谁有权削弱它。** 开发者能改的构建与签名选项、用户能改的模式开关、组织通过配置描述文件与授权属性获得的能力、Apple 通过系统版本与指令集约定做的事、硬件世代决定"这件事能不能做"。
6. **一次崩溃上报时，布局信息去了哪里。** 崩溃与诊断数据离开设备时携带的地址、偏移、镜像清单，以及这三条去向各自的可见主体与保留口径。

## 0.2 三道彼此正交的门槛：位置不可知、指向可验证、越界不可用

本篇的分析骨架是三道**正交**门槛。正交的含义是：它们各自防的是不同性质的问题，过掉一道不等于过掉另外两道，而且一道被削弱不会自动使另外两道失效。

- **第一道：位置不可知。** 攻击者需要先知道某个地址（某个库的基址、某个栈槽的位置、某个堆块的大小与间距），才能把"任意写"变成"有用的写"。地址空间布局随机化、堆元数据距离随机化、可写可执行页的随机地址，都属于这一道。它的收益被 Apple 逐字写成**反批量复用**："随机安排内存地址增加了执行攻击的难度，**尤其是对多个设备的攻击**"。
- **第二道：指向可验证。** 即便攻击者猜对了地址、也拿到了写原语，被写入的指针本身还要能通过校验。指针认证码给代码指针加上由密钥与上下文共同计算的签名；内存标记扩展给可用内存加上一个可校验的分配标记。这一道防的是**篡改与误用**，而不是**预测**。
- **第三道：越界不可用。** 这一道包含两件不同的事：其一，"算出的地址是否越过了本次访问应当合法的范围"——页级边界、保护页、随指针携带的边界信息；其二，"到达之后能不能被执行"——不可执行页标记、按线程撤销执行权限、按需的页哈希校验与代码签名强制。

这三道门槛对**不同缺陷类型**的射程差别很大：一个纯信息泄露缺陷只要绕过第一道就达标，不需要第二、第三道；一个逻辑缺陷（错误的授权判定）三道全都不管；而一个越界写原语要变成可控执行，三道通常一道都绕不过去就得再加码。因此本篇对每个机制都要求回答同一组三问：**它拦什么／它不拦什么／它的强度来自哪里、在什么条件下失效。**

## 0.3 机制分层模型：硬件世代、内核、加载器、分配器、二进制自身

同一句"这台设备有内存安全保护"，实际上可能指五层中任意一层的东西。本篇一律标注层：

| 层 | 这一层的机制 | 强度由谁决定 | 典型失效原因 |
| --- | --- | --- | --- |
| 硬件世代 | 不可执行页标记、指针认证指令、快速权限访问寄存器、内存标记、按代际的芯片支持矩阵 | 芯片型号，**用户与开发者都改不了** | 设备过旧；官方矩阵中该代该项标为不可用 |
| 内核 | 强制执行代码签名、沙盒化、授权核对与地址空间布局随机化；按需页校验；区分配器的类型化分配与交叉引用检查 | 操作系统版本与内核实现 | 内核自身缺陷；调试类授权打开跨进程内存访问 |
| 加载器 | 主镜像与依赖库的装载位置、共享缓存、可加载代码的边界（库校验）、`__AUTH` 段与重定位 | 系统版本 + 二进制里的链接与签名元数据 | 构建产物未带必需段；非平台代码的依赖选择 |
| 分配器 | 用户态内存区的元数据布局、区校验、双重释放与非法写检测、调试类环境变量 | 分配器实现 + 进程内的调试开关 | 环境变量式调试路径；元数据保护被写原语同片覆盖 |
| 二进制自身 | 是否编译进随机化支持、是否带指针认证与分支防护、编译器工具链的内存安全改造 | **开发者**——这是唯一一层开发者能独立决定的 | 旧构建目标、第三方静态库、把例外通道当成便利长期开启 |

这个模型有一条重要推论：**只有第五层的强度可以被应用团队自行改造**，前四层要么是采购问题（机型治理），要么是平台问题（版本治理）。本篇 §15 的判定链就是按这五层逐项打分。

## 0.4 证据分级与引用规范（含源码级证据的用法与限度）

本篇全部引文按四档标注：

- **A 级**：Apple 一手页面正文的**逐字引文**，含《Apple 平台安全保护》中文手册页（逐页标注页面内发布日期）、developer.apple.com 文档页与技术说明、Apple 官方安全博客文章、支持文章。引文保留原页措辞，降级词（"旨在""有助于""大多数""可以"）不删。
- **A- 级**：Apple **官方开源仓库**里的代码、注释与手册页（XNU、dyld 等）。它是苹果写的，但它描述的是**实现**而不是**承诺**：可以证明"当前这么做"，不可以证明"承诺长期这么做"，也不可以证明"所有设备与所有版本都这么做"。本篇凡引此级都显式写作"源码可证但 Apple 未公开承诺"。
- **B 级**：Apple 页面的摘要、索引条目、字段清单，或口径存在但措辞未逐字取得。
- **C 级**：分析推断。必须与 A/A- 级内容物理分开，且标明"这是推断"。

三条引用纪律：

1. **位数缺口不许用二手资料填。** Apple 未公开地址随机化的熵值，就不写"某系统有 N 位随机化"。需要量化时用可复核的结构量（页大小、段是否存在、矩阵表中的可用世代），并在 16.2 登记为缺口。
2. **手册页的版次要按页标注，不按整本假设。** 本主题相关页面的发布日期并不一致：`sec15bfe098e`（运行时进程安全性）为 2024-12-19，`sec8b776536b`（操作系统完整性）与 `sec87716a080` 为 2026-01-28，`sec30d8d9ec1`（内存安全 iBoot 实施）为 2021-02-18，`sec93292bfa6`（术语表）**无发布日期标注**。引用时须带页与日期，不得写成"iOS 27 的行为"。
3. **检索命中为 0 是"缺口"，不是"不存在"。** 本篇登记了大量零命中检索词（例如 `KASLR`、`slide`、分支目标识别、`guarded control stack`），它们的正确读法是"Apple 公开文本对此没有表述"，不得写成"该机制不存在"或"该机制未启用"。

## 0.5 术语纪律：六个被混用的词必须分开说

中文技术写作里这六个词经常互相替换，替换之后判断就消失了。本篇固定口径：

| 术语 | 本篇用法 | 最常犯的混用 |
| --- | --- | --- |
| 地址空间布局随机化（ASLR） | 装载与运行时**地址**的随机安排。手册逐字口径是"随机安排所有内存区域"与"随机安排可执行代码、系统库和相关编程结构的内存地址" | 被用来指代**所有**内存安全机制，包括 PAC 与 MTE |
| 内核文本随机化（社区常称 KASLR） | 内核自身镜像的位置随机化。**`KASLR` 一词在手册 179 页中命中 0**，本篇只在标明"源码/社区用语"时使用 | 与用户态 ASLR 混为一谈，从而把内核的强度当成进程的强度 |
| 指针认证码（PAC） | 用密钥与上下文对指针计算签名并在认证时校验。**不是**加密，不保护数据内容 | 写成"指针加密"，或写成"防篡改的数据完整性机制" |
| 内存标记扩展（MTE）/ 增强型（EMTE） | 给可用内存附带分配标记，访问时校验匹配。**只作用于被标记为可标记的内存** | 当成"边界检查全部覆盖"，忽略未标记内存与读原语 |
| 不可执行页（Execute Never，XN） | 页属性：该页不作为指令取用。手册逐字口径是"将内存页标记为不可执行" | 与"不可写"合并成"只读"，或误当作 W^X 的全部含义 |
| 快速权限访问限制（APRR 寄存器） | 硬件原语：**基于每个线程**快速撤销内存的执行权限，无需系统调用与页表刷新 | 当成页属性开关，从而忽略"按线程"与"无需改页表"这两个关键点 |

另有两组容易被并读但不同层的东西：**"随机的只读位置"（macOS 门禁，防插件自动加载）**与**"随机地址"（可写可执行页）**；**"主目录随机分配"（安装时的文件系统路径）**与"启动时的内存区域随机安排"。这四句都含"随机"，作用对象各不相同，见 §2.6 与 §3.3 的并读要求。

## 0.6 与前序各期的分工关系

本系列中已有多篇与本篇相邻，分工如下：

- **《iOS 系统内核安全漏洞分析报告》**（编号 #79 一期的机制版即《iOS 内核安全机制深度解析》）从内核子系统出发，逐类回答"负责什么／强制点在内核何处"；本篇只取其中与**布局、指针、执行权限**相关的门槛，并把重点放在**用户态进程与二进制自身**。
- **《iOS 代码签名验证：签名在校验什么、授权属性如何决定 App 能做什么》**与**《iOS 代码签名与完整性执行体系的组件分析》**讲签名的语义与执行者；本篇只讲签名如何构成**内存门槛的第二道防线**（§10），即"随机化让你猜不到，页级校验让猜到也没用"。
- **《iOS 限制应用访问文件、进程及系统资源的机制分析》**讲访问围栏；本篇讲的"跨进程内存访问"是它的**特例且只在调试授权语境下出现**（§10.5）。
- **《iOS 网络通信机密性与完整性保护及上传机制分析》**讲数据在路上的保护；本篇 §14 讲**布局信息离开设备**时属于什么性质的暴露——它不是机密性失效，而是**研究面扩大**，处置手段与密钥归属完全不同。
- **《iOS 锁定模式安全漏洞分析报告》**把锁定模式当攻击面收缩来写；本篇只取它与**即时编译例外通道**相交的那一条（§7.4）。

一句话概括本篇的位置：**别的篇章讲"谁能访问什么"，本篇讲"一次越界写入要付出多少代价、这份代价由谁支付、在哪些条件下会被打折"。**

# 1. 总览：一次内存破坏从原语到执行要跨过的门槛

## 1.1 门槛—机制—失效条件—代价对照总表

| # | 门槛（要绕过它需要什么） | 提供该门槛的机制 | 一手出处等级 | 失效条件 | 攻击者为绕过它付出的代价类型 |
| --- | --- | --- | --- | --- | --- |
| 1 | 目标地址不可预知 | 用户态 ASLR：启动时随机安排所有内存区域，另随机安排可执行代码、系统库与相关编程结构的内存地址 | A（逐字） | 存在信息泄露；同一 exploit 复用同一次启动的布局；二进制未编译进支持 | 每台设备/每次启动都要重新计算偏移，**无法批量复用** |
| 2 | 依赖库位置不可预知 | 同上 + 装载期 slide；主二进制 slide 与 dyld slide 是**两个独立随机量** | A-（源码可证） | 库未重定位或共享映射位置一致 | 需要针对具体版本与机型分别准备 |
| 3 | 系统框架整体位置不可预知 | 共享缓存整体搬移量 | A-（官方设计文档给出量级）／手册 0 命中 | 手册**从不**表述该项 | 需按发布周期重取；跨缓存版本失效 |
| 4 | 栈位置与内容不可预知 | 主线程栈、新建线程栈与栈大小的随机安排；辅助向量与初始栈内容 | A-（源码）／**手册无逐项列举** | 固定栈大小约定；同一次启动内复用 | 泄露一次即可覆盖整个进程 |
| 5 | 越界先撞到墙 | 保护页（guard page） | **手册 0 命中**；仅实现层可见 | 大块连续写、跳过保护页的路径 | 需要更多原语或多次尝试，噪声上升 |
| 6 | 堆布局与对象间距不可预知 | 分配器元数据距离随机化；内存区与区分配器布局 | A-（含官方"动机注释"） | 分配器调试开关；可预测的分配序列 | 堆喷射或反复试探，可靠性显著下降 |
| 7 | 写坏的指针不能被拿来用 | 指针认证码：对代码指针（返回地址与函数指针）签名并在认证时校验 | A（逐字，含密钥数量与位数） | 泄露签名指针本身；未签名对象；只读数据被写 | 必须先获得一个**合法签名的指针**，即"先找现成的跳转" |
| 8 | 猜出来的跳转目标不能合法存在 | 分支防护（编译器侧的分支目标约束） | A（构建开关逐字）／硬件机制名手册 0 命中 | 目标本身在程序里就存在（面向复用的跳板） | 从"构造代码地址"转为"挑选已有代码地址" |
| 9 | 数据页不能变代码 | 不可执行页（XN）；按线程撤销执行权限的硬件原语 | A（逐字） | 拿到动态代码签名授权并申请可写可执行页 | 需要走例外通道，且该通道有明确痕迹 |
| 10 | 可执行内存的申请受授权门控 | W+X 的三道并列条件：内核检查 Apple 专有动态代码签名授权；仅单个 mmap 调用可用；该页随机地址 | A（逐字） | 系统组件持有授权 | 第三方 App 通常不可能长期持有 |
| 11 | 误用指针（越界/UAF）被标记捕获 | 内存标记（分配时打标记、访问时校验） | A（含芯片世代与范围句） | 未标记内存；纯读；标记值也被写 | 需要精确猜测/复用标记值，噪声与失败率上升 |
| 12 | 分配器元数据不可被顺手改写 | 堆数据与其元数据分开；双重释放等错误状态可准确检测 | A（逐字，但**范围仅限内存安全 iBoot 构建**） | 非 iBoot 组件；范围句外 | 从"改元数据即得写原语"转为需另找目标 |
| 13 | 内核对象不能任意跨类型改写 | 类型化分配与交叉引用检查 | A（官方博客） | 类型化未覆盖的分配路径 | 内核利用需针对具体类型，跨版本失效 |
| 14 | 页内容必须与签名一致 | 按需页哈希校验与代码签名强制 | A + A- | 签名校验之外的可写映射；调试授权 | 只能改**不执行**的数据，或先过签名关 |
| 15 | 只能加载被允许的代码 | 库校验与可加载代码边界 | A（macOS 侧明确；iOS 侧为平台约定） | 平台代码依赖；例外授权 | 注入面收窄到"被允许加载的库"之内 |

这张表是 §15.1 判定链的输入，也是 §17 建议的落点。

## 1.2 攻击者真正需要的三类信息

把上表压缩后，一次内存破坏要变成可控执行，需要三类彼此不同的东西：

- **类一：地址。** 某个可执行代码位置、某个栈槽、某个已签名指针。第一至六道门槛都只针对这一类，它们共同构成"位置不可知"。**信息泄露缺陷单独就能满足这一类**——这就是为什么"只泄露不写"的缺陷在这套体系里被当成高危：它把第一道门槛整行划掉。
- **类二：写能力。** 能写哪些字节、写多远、能写几次。第七、八、十一、十二、十三道门槛针对"写了不该写的东西之后还能不能用"，它们不限制写入本身，而是让写入产生不了效果。
- **类三：执行入口。** 被写的地址最终要能被 CPU 取指执行。第九、十、十四、十五道针对这一类。值得注意的是，这一类的强度在 Apple 文本里被**同时**描述为页属性（不可执行）、硬件原语（按线程撤销执行权限）与授权门控（动态代码签名授权），三者不是同一件事的不同说法。

三类的关系决定了评估口径：**只看类一的机制（ASLR 系列）会系统性高估强度**，因为一个信息泄露就能补齐类一；只看类三会低估强度，因为类三在系统组件之外基本没有合法通道。

## 1.3 为什么"缓解"不等于"修复"：本篇的评估口径

Apple 的措辞本身就是这条区分的证据。手册逐字写的是"**有助于**防止对内存损坏错误的利用"、"**帮助**确保运行时安全性"、"此类代码可**帮助**防止以下类中的**大多数**漏洞"、"这**旨在**防止……"。这些降级词是有意保留的，本篇一律不删。

于是评估口径固定为三条：

1. **判定只看"能不能提高代价"，不看"能不能阻断"。** 因此本篇大量使用"代价类型"而不是"是否安全"。
2. **缓解强度会随缺陷类型改变，而不是随设备改变。** 同一台设备上，ASLR 对一个越界写很强，对一个任意读几乎不存在。
3. **缓解可以被关闭、绕过或打折，而关闭通常有可见痕迹。** §11 列出主体与能力，§13 列出可观测字段。凡是"能合法削弱"的项，本篇都写成配置审计要点而非操作步骤。

## 1.4 这张表不能回答的问题

为避免误用，明确四条本篇不覆盖的结论：

- **不能回答"我的设备安全吗"。** 强度依赖具体缺陷类型、具体二进制与具体世代；§15 给的是打分方法，不是证书。
- **不能给出熵值。** 见 §5.3：Apple 一手文本不提供地址随机化的比特数，本篇不代填。
- **不能把 A- 级源码事实读成产品承诺。** 源码可证"当前实现这么做"，不能推出"该行为在下一版本、其他机型或其他平台上仍成立"。
- **不能给出任何绕过路径的操作细节。** 第 6、7、10 章里"如何降低强度"的部分，只描述官方公开的构建选项、授权属性及其后果，用途是配置审计与整改。

# 2. 随机化对象目录（一）：镜像、依赖库与共享缓存

## 2.1 主可执行文件的加载位置

A 级一手出处只有一段，来自《Apple 平台安全保护》"iOS、iPadOS 和 visionOS 中的运行时进程安全性"页（`sec15bfe098e`，页面发布日期 2024-12-19）的「地址空间布局随机化」小节，逐字：

> 地址空间布局随机化 (ASLR) 有助于防止对内存损坏错误的利用。内置 App 使用 ASLR 有助于在启动时随机安排所有内存区域。除了启动时工作，ASLR 还会随机安排可执行代码、系统库和相关编程结构的内存地址，以进一步降低遭受许多攻击的可能性。例如，"return-to-libc"攻击试图通过操纵堆栈和系统库的内存地址来诱使设备执行恶意代码。随机安排内存地址增加了执行攻击的难度，尤其是对多个设备的攻击。Xcode 和 iOS 或 iPadOS 开发环境可自动编译启用了 ASLR 支持的第三方程序。

这段里有四个必须拆开读的动作：

- **"在启动时随机安排所有内存区域"**——范围是"所有内存区域"，措辞没有例外。
- **"除了启动时工作，ASLR 还会随机安排……"**——官方明确这是**第二类**动作，不是同一句的重述。
- **"可执行代码、系统库和相关编程结构"**——前两项可落到具体对象（主镜像的 `__TEXT` 与依赖库），第三项"相关编程结构"是**兜底模糊词**，手册没有逐项列举栈、堆与映射区。这是本篇 §3 整章要处理的口径缺口。
- **"Xcode……可自动编译启用了 ASLR 支持的第三方程序"**——第三方 App 的随机化支持是**编译期默认获得**的，不是运行时开关。手册**没有**说明能否关闭、也没有说明未启用时的后果，因此"某个 App 没开 ASLR"这类判断在公开文本里既无法证实也无法证伪，只能靠二进制自身的段与元数据来判断（§13、§15）。

同页开篇句把 ASLR 与另外两项机制并列，值得单独记下，因为它是"三道门槛相互独立"的官方结构证据：

> iOS、iPadOS 和 visionOS 使用沙盒、声明的授权和地址空间布局随机化 (ASLR) 帮助确保运行时安全性。

沙盒（访问围栏）、声明的授权（能力）、ASLR（布局）三件事并列，说明在 Apple 的表述里 ASLR **不是**访问控制手段，它只改变"猜中地址的难度"。

**A- 级补充（实现层，不是承诺）**：主二进制 slide 由内核在装载路径上计算，`xnu/bsd/kern/mach_loader.c:785-802` 的注释逐字为

> 	 * Compute a random offset for ASLR, and an independent random offset for dyld.

即一次装载会**分别**取随机数生成主镜像 slide 与 dyld slide，两者互相独立。arm64 下主镜像 slide 的空间上限见 `xnu/osfmk/vm/vm_map.c:21919-21928`：

> 	/* Limit arm64 slide to 16MB to conserve contiguous VA space in the more
> 	 * limited embedded address space; this is also meant to minimize pmap
> 	 * memory usage on 16KB page systems.

据此，**主镜像 slide 的可选范围上限为 16MB**，且官方注释逐字给出这是**取舍**而非最大化：为了在"更紧张的嵌入式地址空间"里保留连续虚拟地址空间，并减少 16KB 页系统上 pmap 的内存占用。折算成页对齐档位数（同一份源码的算术，属 A- 级推导）：16KB 页约 1024 档、4KB 页约 4096 档。**这三行数字只能写作"源码给出的上限"，不得写成"Apple 承诺的熵位数"**（见 §5.3）。

## 2.2 依赖库与系统框架的加载位置

手册把依赖库放在同一句话里——"随机安排可执行代码、**系统库**和相关编程结构的内存地址"——因此"系统库位置随机"有 A 级支撑；但**它没有区分**三类性质完全不同的库：

1. **App 自带的私有库**（随 App 一起打包、安装进容器的那些），与主镜像同样走装载期 slide。
2. **独立发布的系统 dylib**，位于共享缓存内，其位置由**缓存整体 slide**决定，不是逐库随机（§2.3）。
3. **动态装载的库**（运行时按路径解析者），受"可加载代码的边界"约束（§10.3）。

这个区分对评审有直接后果：**"我的 App 开了 ASLR"并不等于"我依赖的系统库位置对我不可预测"**。共享缓存里的框架在同一台设备上、同一份缓存的所有进程中是**同一份只读映射**，官方文档逐字解释了这个设计的动机与代价（见 §2.3、§2.4）。

另一处 A- 级证据值得登记：dyld 的官方手册页（`dyld/doc/man/man1/ld.1`，随 dyld 仓分发，日期戳 `June 1, 2020`）对"位置无关可执行文件"的表述是逐字的因果句：

> This makes a special kind of main executable that is position independent (PIE).  On Mac OS X 10.5 and later, the OS
> the OS will load a PIE at a random address each time it is executed.  You cannot create a PIE from .o files compiled
> with -mdynamic-no-pic.  That means the codegen is less optimal, but the address randomization adds some
> security. When targeting Mac OS X 10.7 or later PIE is the default for main executables.

三个可用点：**"each time it is executed"**（随机发生在每次执行，不是每次安装）；**代价被官方逐字承认**——"the codegen is less optimal, but the address randomization adds **some** security"，注意用词是 some，不可放大；以及一个真实的构建期约束——用 `-mdynamic-no-pic` 编译的目标文件**不能**做成 PIE，因此"为了性能关掉位置无关代码"与"放弃地址随机化"在链接器层面是同一件事的两个说法。

`__PAGEZERO` 是这一节的另一个构建侧证据，同一手册页逐字给出量化默认值：

> By default the linker creates an unreadable segment starting at address zero named __PAGEZERO.  Its existence
> will cause a bus error if a NULL pointer is dereferenced. … By default on 32-bit architectures the page zero size
> is 4KB.  On 64-bit architectures, the default size is 4GB.

它的门槛性质属于**第三道**（越界不可用）而不是随机化：把低地址整段设为不可读，使"空指针解引用"与"小幅偏移的指针运算错误"直接撞墙。把它列在 §2.2 是因为它由**同一份产物的段结构**决定，评审时必须与"是否 PIE"一起看。

## 2.3 dyld 共享缓存的整体搬移量

**手册侧口径：这一项是缺口。** 检索台账（附录 A.4）记录 `slide` 在全部手册页命中 **0**，`共享缓存`/`shared cache` 无对应页面；`dyld` 一词在手册中仅命中 1 页（Rosetta 2 页 `secebb113be1`），且那里的用法是"内核将控制转交给 Rosetta 转译存根，而不是动态链接编辑器 dyld(1)"的对照，与缓存随机化无关。

**A- 级（官方设计文档）给出量级与原因。** `dyld/doc/CacheLayout.md` 逐字：

> At run time we choose a slide value, and move the cache to that location.  The maximum slide in the old layout was 1.5GB minus the TEXT mapping size.  But as the TEXT mapping was almost exactly 1.5GB, **the slide was effectively zero**.  On this new layout, we choose to make a new cache file for each 512MB of TEXT, so **the slide is about 512MB too** (so that we don't cross 1GB boundaries).

这条对评审很有价值，因为它说明**"缓存 slide"曾经实际为零**，是布局改版才拿到的随机性——把"框架位置一直随机"当默认事实是错的。同文档逐字给出页表共享约束：

> On x86_64 that sharable range is 1GB. On arm64 the range is 32MB range. To save lots of wired memory in the kernel, we arrange the read-only parts of the dyld shared cache to enable the kernel to share page tables. … For arm64, we get the same result by having the cache builder introduce 32MB of address space between r/w and r/o regions. Then, **no matter the ASLR slide, the r/o parts can never be in the same 32MB page table region as a r/w part**, and thus the kernel can share the page tables for the r/o parts of the dyld cache.

于是缓存 slide 的"格"由**页表共享的内存优化**决定，而不是由熵最大化决定；这与 §2.1 里主镜像 slide 的 16MB 上限属**两个不同主体的不同约束**，绝不可相加或合并成"一个 ASLR 熵"。

**主体三分口径（本篇强制）**：

| 随机化主体 | 谁算这个值 | 一手等级 | 范围/量级 |
| --- | --- | --- | --- |
| 主二进制 slide | 内核装载路径 `random()` | A- | arm64 上限 16MB（源码给出的上限，非承诺熵） |
| dyld 自身 slide | 内核，与主二进制**独立** | A- | loader slide 上限 4MB，注释自称"ensure at least 8 bits of independent entropy on 16KB page systems" |
| 共享缓存 slide | **用户态 dyld 选定后传给内核**映射/reslide | A- | 新布局"about 512MB"；旧布局"effectively zero" |

第三行的实现证据是 `xnu/osfmk/vm/vm_shared_region.c:204-208` 与 `:234-238`（`vm_shared_region_reslide_count` 记录"因事件而重算 reslide 的次数"，桌面平台逐字注释 "Only one cache gets to slide"）。因此"每次启动重新随机化"这句话在**进程镜像**与**共享缓存**上的含义并不相同，正文与 §2.5 都按此区分。

## 2.4 只读映射、共享映射与页级校验的耦合

§2.3 的页表共享有一条直接的门槛推论，且它由官方文档逐字支撑：**只读部分在不同进程之间可共享页表**，意味着同一台设备上、同一份缓存的 `__TEXT` 对各个进程是**同一份物理映射**。所以"攻破一个进程得到的只读布局知识，可以迁移到同设备的另一进程"——这是真实限制，且是 Apple 自陈的设计取舍（省 wired 内存）。

这与第二道防线的关系是：**共享映射降低的是"猜地址"的难度，而页级签名校验把"改内容"的难度顶住**。手册在"可执行内存页在载入时校验"页（`secf49cad4db`，2024-12-19）给出这一层的逐字口径：内核在页被使用时校验其哈希，而非在装载时一次性校验全镜像；Rosetta 2 页（`secebb113be1`，2021-02-18）另给出**出错时机**的表述。二者相加，得到本篇反复使用的组合句：

> 位置可以共享，内容不能篡改。

对评审的意义：当某人说"框架地址是共享的，所以 ASLR 对系统库无效"时，正确的回答不是"ASLR 有效"，而是**"这一层由页级校验与签名强制负责，不由随机化负责"**（§10.1）。随机化只在其作用域内计分，跨域外推是最常见的评估错误。

## 2.5 重随机化的时间尺度：每次启动、每次安装、每台设备

三种时间尺度在本篇必须分开，因为它们各自决定一类复用：

- **每次执行/每次启动。** A- 级逐字依据是链接器手册页的 "the OS will load a PIE at a random address **each time it is executed**"；内核侧依据是装载路径每次都调用 `random()`。尺度含义：**同一台设备上的两次崩溃，地址通常不同**，因此崩溃报告里的裸地址不能直接当偏移使用（§13.5、§14.2）。
- **每次安装（且仅一次）。** 这一条容易被写错。手册逐字把**容器主目录**的随机放在安装时：

  > 每个 App 还拥有唯一的主目录来存放其文件，主目录是在安装 App 时随机分配的。

  这是**文件系统路径**随机化，不是内存布局随机化；它决定"路径不可预测"这一层，与 §10.4 的构建选项无关。附录 A.4 的台账把这条单独登记，防止与 ASLR 混计。
- **每台设备。** A 级逐字依据正是那句收益表述——"随机安排内存地址增加了执行攻击的难度，**尤其是对多个设备的攻击**"。这是本篇唯一一处 Apple 亲自说明 ASLR 的**收益归属**：防的是**批量复用**，不是单台设备上的定向利用。据此，§17 给采购与机型治理的建议是把"同版本同机型大面积攻击"作为 ASLR 真正改变代价的场景，而不是把 ASLR 当成单点事件的防线。

共享缓存的尺度又不一样：它随**系统版本/缓存构建**而变，随 reslide 事件重算，因而"换一次系统"就足以让一份针对框架偏移的准备失效（§15.5 回归监测项）。

## 2.6 一手文本给了什么、没给什么

把 §2 收成一张"能不能写进正文"的对照表，这张表也是本篇证据纪律的样例：

| 命题 | 能否写成 Apple 口径 | 依据 |
| --- | --- | --- |
| ASLR 有助于防止对内存损坏错误的利用 | **能**（A 逐字） | `sec15bfe098e` |
| 启动时随机安排所有内存区域 | **能**（A 逐字） | 同页 |
| 还随机安排可执行代码与系统库的内存地址 | **能**（A 逐字） | 同页 |
| 随机安排"相关编程结构" | **能，但必须保留这个模糊词**，不得替换成"栈与堆" | 同页 |
| 官方点名的攻击示例是 "return-to-libc" | **能**（A 逐字，含引号） | 同页 |
| 收益主要是反多设备批量复用 | **能**（A 逐字） | 同页 |
| 第三方 App 编译期即获得 ASLR 支持 | **能**（A 逐字，措辞是"可自动编译启用"） | 同页 |
| ASLR 可以被某个设置关闭 | **不能**。手册未提关闭方式与后果 | — |
| 主镜像 slide 有 N 位熵 | **不能**。`slide` 在手册命中 0；源码只给"上限" | A- 需标"源码给出的上限" |
| 共享缓存每次启动重新随机 | **不能**。缓存 slide 由 dyld 选定，且旧布局"effectively zero" | A- |
| 系统框架地址对各进程不可预测 | **不能**。r/o 映射可共享页表，同一份缓存是同一物理映射 | A- |
| 内核自身镜像被随机装载（社区称 KASLR） | **不能**。`KASLR` 一词在手册 179 页命中 0 | 见 §4.1、§4.4 |

最后四行是本篇 §16.2 缺口清单的主体来源。**一条通用的写法模板**（本篇 §14、§16.6 反复使用）：

> Apple 公开手册确认用户态 ASLR 随机安排所有内存区域、可执行代码、系统库与相关编程结构，但**未公开其熵值、熵源，也未说明是否每次启动必然变化**。


# 3. 随机化对象目录（二）：栈、堆与映射区

## 3.1 主线程栈的位置

**手册口径：没有单独一句讲栈的随机化。** 手册对 ASLR 的全部措辞停在"随机安排所有内存区域""相关编程结构"这两处兜底表述上（§2.1）。因此"栈基址每次启动随机"这一句，**在本篇只能以 A- 级（源码）+ 手册兜底句的组合形式写出，不得写成 Apple 的逐字承诺**。

A- 级证据之一在 32 位 ARM 路径的机器参数定义里，`xnu/bsd/arm/vmparam.h:16` 逐字：

> #define USRSTACK        (0x27E00000)    /* ASLR slides stack down by up to 1MB */

这条注释有三点可直接使用：**方向**（stack slides **down**）、**幅度**（**up to 1MB**）、以及"这是一条真实存在于内核源码里的注释而非社区推测"。也必须立刻给出它的限定：这是 `bsd/arm/vmparam.h` 中 32 位 ARM（旧 armv7 与 arm64_32 相关路径）的定义，arm64 LP64 的对应值不在该文件，**故 1MB 这个数字不能写成"iOS 主线程栈的随机范围"**。这是本篇对"引用源码数字必须带架构路径"的典型处理。

## 3.2 新建线程栈与线程栈大小

主线程栈的**大小**有一手出处，来自链接器手册页（`dyld/doc/man/man1/ld.1`）对 `-stack_size` 的逐字说明：

> Specifies the maximum stack size for the main thread in a program.  Without this option a program has a 8MB stack. … The size should be a multiple of the architecture's page size (4KB or 16KB).

两个评审要点：**默认 8MB**（这是可以直接引用的一手量化值）；**取值必须是页大小的整数倍，而 Apple 的一手文本在这里明确把页大小写成 4KB 或 16KB**——这与 §2.3 的 16KB 页系统口径一致，可用于交叉校验"某产物按 4KB 假设做的布局计算在 16KB 页设备上是否成立"。

新建线程（pthread）栈的位置与大小，**手册与 developer 文档均无逐字表述**（登记为缺口）。本篇可用的只有一条**间接而重要**的一手句：链接器手册页在 `-allow_stack_execute` 条目里逐字承认栈的执行权限范围**包括 pthread 栈**：

> Marks executable so that all stacks in the task will be given stack execution privilege. This includes pthread stacks. This option is only valid when targeting architectures that support stack execution (i.e. Intel).

它的价值在两处：其一，"This includes pthread stacks" 说明在 Apple 的模型里"任务的栈"是一个整体策略单元，主线程与新建线程在执行权限上被并列处理；其二，**该选项只在支持栈执行的架构上有效，且被逐字点名为 Intel**——于是"iOS/arm64 上栈不可执行"不是一项可打开可关闭的配置，而是**构建层就没有这个选项**（§7.2）。这条是 §16.5 里"与流行口径相反"的样本之一：流行写法常把"栈不可执行"当作可被某个编译开关破坏的东西，而一手证据表明该开关在目标架构上根本无效。

## 3.3 保护页与栈越界的第一次撞墙

**术语冲突预警（必须先解决再引用）**：在 Apple 的一手材料里，"guard"/"保护页"有**两个互不相干**的含义。

- 含义甲：分配器在分配块两端放置的**不可访问页**，用来把越界读写立刻撞到墙上。这是社区所说的 guard page。
- 含义乙：ARM64 页表里的 **"guarded" page** 标志位，属于**分支目标识别（BTI）强制**的粒度，与"越界"无关。XNU 源码逐字：`unsigned int guarded => flag indicating whether this range should be considered an ARM "guarded" page. This enables BTI enforcement for a region.`

手册侧的检索结果是硬口径：**`guard` / `guard page` 在全部 179 页命中 0**（附录 A.4）。因此含义甲**没有 Apple 公开承诺**，正文必须写"Apple 未公开记载"，不得写"iOS 为每个栈/堆块设置保护页"。

含义甲**唯一**能落到的公开一手层，是 libmalloc 源码里的环境变量名（§9.4 详列）：`MallocGuardEdges`、`MallocDoNotProtectPrelude`、`MallocDoNotProtectPostlude`、`GuardMalloc`。这些是**源码字符串**，Apple 没有 developer 文档页逐条描述其语义（Instruments 文档另有 Guard Malloc 条目），引用时必须标"源码级、无公开文档页"。

**能写成 A 级的"第一次撞墙"其实不是保护页，而是 `__PAGEZERO`**（§2.2 已逐字引）：链接器默认在地址 0 起创建一段不可读的 `__PAGEZERO`，其存在使"解引用空指针"触发 bus error；32 位默认 4KB、64 位默认 **4GB**。这段的作用与保护页不同（它只封低地址），但它是一手里唯一被明确文档化的"越界立刻可见"屏障，评审时应当先看这段的尺寸与存在性，再看块级 guard。

栈越界的另一个可观测后果属于异常类型而非机制：`EXC_BAD_ACCESS` 一类崩溃报告形态（§13.2）。**注意 developer 侧检索未命中**：`EXC_BAD_ACCESS`、`EXC_GUARD` 等**作为文档条目**在本轮抓取的构建设置页与相关页命中 0——也就是说这套异常名的公开文档化程度低于其在实践中的使用频率，写作时不要把"字段一定存在"当成有 A 级支撑（§16.2）。

## 3.4 堆区、区分配器与内存区

本节是本篇**证据质量最高**的一节，因为 libmalloc 源码里有一条把"动机"写全的注释。它先给出机制的实现形状，再点名它要防的**具体相邻关系**。`libmalloc/src/vm.c:125-142` 逐字：

> // The "guarded range" is a synthetic VA range that we create in order to
> // randomize the distance of malloc metadata from the fixed mappings that
> // are laid out at process launch.
> //
> // We implement this by mapping a PROT_NONE region: the size of this region is
> // randomly chosen between 4M and 5M. Within that region, we then punch a 1M
> // carveout, by deallocating a random piece of the guarded region VA.
> // The address of this carveout is then passed as a hint in mvm_allocate_plat
> // (whenever the provided address is zero), to nudge the VM into serving the
> // requested mappings through the carveout.
> //
> // Specifically, this is aimed at randomizing the distance between the binary's
> // __DATA segment and the malloc metadata allocated at process launch, namely
> // the initial zone allocation. Moreover, by having a variable-length tail at
> // the end of the region, we also randomize the distance between __DATA and
> // the regions mapped after malloc's metadata.
> //
> // rdar://146578480

逐字要点（这一段几乎可以当作"随机化对象目录"的定义范式）：

- **随机化的对象是"距离"，不只是"基址"。** 官方原句是 "randomize the **distance** of malloc metadata from the fixed mappings"。这一句直接支撑 §3.7 的论点：复用顺序与相对间距同样是布局信息。
- **具体形状**：一段 `PROT_NONE` 区域，大小在 **4M–5M 之间随机**；在其中再打出一个 **1M** 的缺口（carveout），缺口位置随机；缺口地址作为 hint 传给分配器。
- **点名的目标对**：主镜像的 `__DATA` 段 ↔ 进程启动时分配的 malloc 元数据（即 initial zone allocation）。这是"A 与 B 的相对位置被随机"的一手实例，而 `__DATA` 正是全局指针与结构所在之处。
- **附带收益**：区域尾部的可变长度还随机化了 `__DATA` 与其后映射区之间的距离。
- **`PROT_NONE` 本身就是屏障**：这段合成虚拟地址区间不可访问，落入其中即出错——它同时服务于"距离随机"和"撞墙可见"两件事。

同一文件另给出堆熵的实现参数。`libmalloc/src/vm.c:36-46` 逐字：

> uint64_t malloc_entropy[2] = {0, 0};
>
> #define ENTROPIC_KABILLION 0x10000000 /* 256Mb */
> #define ENTROPIC_USER_RANGE_SIZE 0x200000000ULL /* 8Gb */
>
> // <rdar://problem/22277891> align 64bit ARM shift to 32MB PTE entries
> #if MALLOC_TARGET_IOS && MALLOC_TARGET_64BIT
> #define ENTROPIC_SHIFT 25
> #else // MALLOC_TARGET_IOS && MALLOC_TARGET_64BIT
> #define ENTROPIC_SHIFT SMALL_BLOCKS_ALIGN
> #endif

`libmalloc/src/vm.c:61-68` 逐字（arm64）：

> #elif defined(__arm64__)
> #if defined(__LP64__)
> 	uintptr_t stackbase = USRSTACK64;
> 	int entropic_bits = 7;
> #else // __LP64__
> 	uintptr_t stackbase = USRSTACK;
> 	int entropic_bits = 3;
> #endif

量化基线（一律标注"源码给出的参数，非 Apple 公开承诺的熵位数"）：iOS+64 位下 `ENTROPIC_SHIFT = 25`，即 **32MB** 的页表项对齐粒度（注释逐字写明这是为了"align 64bit ARM shift to 32MB PTE entries"，并与 §2.3 的 arm64 页表共享区间 **32MB** 是同一个数字——**两处 32MB 是同一约束的两个体现，不能当成两个独立事实各自加熵**）；arm64 LP64 的 `entropic_bits = 7`，32 位为 `3`。

最有价值的一条是"关闭时去掉伪装"，`libmalloc/src/vm.c:106-112` 逐字：

> 	} else {
> 		// zero slide when ASLR has been disabled by boot-arg. Eliminate cloaking.
> 		malloc_entropy[0] = 0;
> 		malloc_entropy[1] = 0;
> 	}
> #else // TARGET_OS_IPHONE && !TARGET_OS_SIMULATOR
> #error ASLR unhandled on this platform

三点结论：**分配器熵与内核 ASLR 状态是联动的**（不是各随机各的）；官方用词 "Eliminate cloaking" 承认在 ASLR 关闭时若仍保留熵会构成**伪装**；`#error ASLR unhandled on this platform` 说明 iOS 真机路径是被**显式覆盖**的目标，模拟器路径被排除在外（`!TARGET_OS_SIMULATOR`）——这条对"在模拟器上测出的布局行为"直接构成方法论警告（§12.4、§15.5）。

**内核区分配器与"内存区"的官方口径**：手册没有 `zone` / `malloc` / "内核区分配类型检查" 任何措辞（命中 0）。唯一可引的是"Apple 安全内存分配器"这个名称本身，以及内核分配器后备存储被保护那句（§4.2、§8.1 逐字给出）。因此 §9.5 讨论内核类型化分配时，一手依据是 **Apple 官方安全博客**文章而非手册。

## 3.5 映射区与保留空间

映射区（运行时按地址请求获得的虚拟地址区间）的随机性，一手证据仍来自 libmalloc 注释里那句"内核会怎么给地址"的描述，`libmalloc/src/vm.c:82-90` 逐字：

> 				/* If kernel VM user ranges are enabled mach_vm_allocate/map will provide memory
> 				 * in the upper VM address range. This range is randomized per process. For now
> 				 * we do not have this metadata plumbed through so we make a single allocation
> 				 * with the appropriate tag to determine where our heap is. If we are given an
> 				 * allocation above where we expect then we can safely assume VM ranges are enabled.
> 				 *
> 				 * If so we do not need to apply further entropy but do need to ensure
> 				 * we mask off the address to a PTE boundary.
> 				 */

可用点四条，且第四点最重要：`mach_vm_allocate`/`map` 在开启内核 VM 用户范围时把内存放在**上部 VM 地址范围**，**该范围按进程随机化**；分配器因此**不再叠加自身熵**（"we do not need to apply further entropy"），只做 PTE 边界对齐；分配器**无法直接读到这个元数据**（"we do not have this metadata plumbed through"），于是采用**试探性分配一次并根据返回地址判断**的策略。最后这条是一手里少见的"用户态组件对内核随机化状态**不可观测**，只能探测"的实例，直接支撑 §12 与 §15 的一条方法论结论：**同一个进程内的不同组件，对同一项随机化的可见性是不同的**，因此"我的代码看不到随机化状态"不等于"随机化未生效"。

保留空间的另一面是 `ENTROPIC_USER_RANGE_SIZE = 8Gb` 与 `ENTROPIC_KABILLION = 256Mb` 两个上限常量，以及 §2.3 里"为省 wired 内存而把 r/w 与 r/o 分在不同 32MB/1GB 区间"的布局约束。评审时应把这三者合起来读：**地址空间的可用形状、随机化幅度与内核内存开销是同一个设计问题的三面**，任何"把随机化调大一点"的表述都必须回答"多出来的连续虚拟地址空间与页表开销从哪里省"。

## 3.6 初始栈内容与辅助向量

辅助向量（auxiliary vector，进程启动时内核放在栈顶的一组信息：镜像地址、页大小、能力位等）与初始栈内容（`argv`/`envp`）是本篇**必须写成缺口**的一项。

- 手册侧：无相关内容（`envp`、辅助向量一类词不在手册话题范围）。
- developer 侧：本轮抓取的构建与诊断文档**没有**任何关于辅助向量随机化或其字段的公开页面。

因此关于"auxv 本身是否随机、其内容是否泄露镜像地址"，本篇**不作断言**。可以安全写出的只有间接一句：**只要辅助向量向进程自身暴露镜像地址，随机化的作用对象就是"进程之外的观察者"而非"进程内部的代码"**——这是从 §2.4（同一设备上各进程共享 r/o 页表）与 §12.6（跨平台差异）能够支持的结构性观察，必须标为 **C 级推论**并明确写"公开文本未描述 auxv"（§16.2 N 项）。这条推论有直接评审后果：**进程内部可读自身布局信息的合法通道（如 dyld 查询 API）会让"同一进程内的第二次利用"不需要猜地址**，所以 ASLR 的强度评估必须区分"跨进程/跨设备复用"与"同进程内利用"两种场景（§1.2 类一）。

## 3.7 对象复用顺序本身也是布局信息

前三节处理的都是"地址"。本节处理一个更容易被漏掉的对象类别：**分配与回收的顺序**。它是本篇把"随机化对象目录"扩展到第三类的理由。

结构论证如下（标为 C 级，但其依据全部为 A/A- 级事实）：

1. 若同一大小类别的连续分配落在可预测的相对位置上，则"知道上一次分配到了哪"就等价于"知道下一次分配的大致位置"，**随机化基址的收益被顺序知识替代**。
2. libmalloc 注释逐字把"距离"（distance）而不是"位置"（address）当成随机化目标（§3.4），说明 Apple 自己认为**相对间距**是独立需要打散的量——这等于承认"绝对基址随机"不足以覆盖"相对关系可预测"这一类信息。
3. 手册唯一一段把堆的确定性当成防护手段的文字是内存安全 iBoot 页里的"通过将堆数据和其元数据分开，以及准确检测诸如双重释放错误等错误状态"（§9.1 逐字）——它的思路是**用布局确定性与检测**替代随机，与"打散距离"方向相反。**这两句必须并读**（§16.4）：随机化与确定+可检测是同一问题的两种工程解法，混在一个段落里会让结论变成含糊的"都做了"。
4. 内核侧对应物是类型化分配，官方安全博客逐字表述其目的为把同类对象聚拢以缩小跨类型改写的可能（§4.2、§9.5）。同样是"用**确定性布局 + 校验**"而不是"用随机"。

于是 §1.1 表格里的第 6 道门槛（堆布局与对象间距）真正的度量单位不是"多少位熵"，而是**"攻击者是否需要每设备重新学习一次分配模式"**。这也是"尤其是对多个设备的攻击"（§2.1 A 级）这句收益表述在堆这一层的落点：**顺序知识的可迁移性，决定随机化的实际强度**——若某个 App 的分配序列完全由输入决定，同版本不同设备上的模式就相同，跨设备复用重新成立。

对开发者唯一正当、且不涉操作的结论：**不要在自有代码里假设分配顺序稳定**（例如用相邻性做对象归属推断、用固定步长遍历自有对象池），因为"当前可预测"既不是契约也不是保证；对评审者的结论：判断一个堆相关缺陷的严重性时，应显式回答"这个缺陷需要的是绝对地址还是相对关系"。


# 4. 随机化对象目录（三）：内核与运行时

## 4.1 内核文本与数据的位置

**手册通篇没有出现 `KASLR` 一词**，也没有 `slide`（附录 A.4 检索台账，179 页全库大小写不敏感）。手册里唯一把 ASLR 归给内核执行权的句子在**术语表**（`sec93292bfa6`，该页**无发布日期标注**），逐字：

> XNU
> Apple 操作系统中央的内核。默认为受信任状态，并强制执行代码签名、沙盒化、授权核对和地址空间布局随机化 (ASLR) 等安全措施。

这句的准确射程是"**内核是 ASLR 的执行者**"，它**不能**支撑"内核自身镜像被随机装载"。这是本篇 §16.1 术语冲突清单里的 C 组第一条：很多文章把这句当作"iOS 有 KASLR"的官方出处，属于把"执行 ASLR"读成"对自身执行 ASLR"。

A- 级（源码可证，Apple 未公开承诺）能提供三条事实：

1. **存在报告内核 slide 是否生效的 sysctl。** `xnu/bsd/kern/kern_sysctl.c:4311-4317` 逐字注释 `Boolean indicating if KASLR is active.`。即内核内部**确实使用 `KASLR` 这个词**，并有可查询的布尔状态。这是"该机制存在"的实现证据，**不是**手册对用户的描述。
2. **存在关掉它的启动参数。** `xnu/bsd/kern/bsd_init.c:293-294` 逐字：

> /* Prevent kernel-based ASLR from being used. */
> __private_extern__ TUNABLE(bool, bootarg_disable_aslr, "-disable_aslr", 0);

`bsd_init.c:629-630` 显示命中该 boot-arg 时给内核进程置 `P_DISABLE_ASLR`。**boot-arg 需要受控的启动参数写入通道**，因此这不是"用户可改的设置"，但它是 §11.4 的一条依据：同一份系统软件在不同启动配置下的随机化强度可以不同。
3. **存在按进程关闭的 posix_spawn 标志。** `xnu/bsd/kern/kern_exec.c:4343-4359` 的注释逐字为 "Disable ASLR for the spawned process."、"While embedded allows for a boot-arg (-disable_aslr)" 与 "useful or necessary to disable ASLR on a per-process"，实现是 `if (px_sa.psa_flags & _POSIX_SPAWN_DISABLE_ASLR) { OSBitOrAtomic(P_DISABLE_ASLR, &p->p_flag); }`。同段还逐字给出 `_POSIX_SPAWN_HIGH_BITS_ASLR` → `IMGPF_HIGH_BITS_ASLR`（"Randomize high bits of ASLR slide"）这条**增强**方向。两个后果值得登记：**关闭是按进程生效并随 fork 继承**（`xnu/bsd/kern/kern_fork.c:1038-1040` 把 `P_DISABLE_ASLR` 放进 p_flag 继承掩码），**而对 setuid 类进程内核会强制回开**——`kern_exec.c:6764-6768` 逐字注释 "Disable _POSIX_SPAWN_ALLOW_DATA_EXEC and _POSIX_SPAWN_DISABLE_ASLR"，实现为 `imgp->ip_flags &= ~(IMGPF_ALLOW_DATA_EXEC | IMGPF_DISABLE_ASLR);`。

这条"特权执行形态下撤销弱化请求"是 Apple 实现里少见的**反向保护**，与 §10.4 的"构建/签名选项削弱防护"构成一组对照：**能削弱的都是非特权路径，特权路径被强制复原。**

**正文写法**：内核文本位置随机化只能写成"源码可证、手册未收录、社区称 KASLR"三重限定，且不得给出位数。

## 4.2 内核区分配器与对象布局

这一节的一手证据质量高于全篇其他节，因为它来自 Apple 自己的安全研究博客，而且**博客开头就在批评随机化本身**。`Towards the next generation of XNU memory safety: kalloc_type`（security.apple.com，日期与署名逐字 **October 27, 2022**）逐字：

> We first shipped this new hardened allocator, called kalloc_type, in iOS 15, and this year we've expanded its use across our systems.

> Our fundamental strategy is to design an allocator that makes exploiting most memory corruption vulnerabilities inherently unreliable. This limits the impact of many memory safety bugs even before we learn about them, which improves security for all users. We also expect this work makes exploit techniques more bespoke and less reusable, which significantly increases the effort attackers have to invest when we fix a vulnerability they were using.

**"inherently unreliable"（本质上不可靠）**是 Apple 给自己定的目标措辞，而"more bespoke and less reusable"（更专用、更不可复用）与 §2.1 手册那句"尤其是对多个设备的攻击"是同一个思路在不同层的表述——**Apple 反复强调的收益都是"不可复用"，而不是"不可能"**。

紧接着是同页对随机化类缓解的官方批评，逐字：

> Mitigations based on randomization can sometimes be fragile. For example, ASLR is often criticized because leaking one pointer value gives the attacker the location of a great number of interesting objects, even ones unrelated to the code that contains the leak. And many hardened allocator proposals that rely on randomization can be defeated by simply spraying allocations to overwhelm the randomization and once again arrive at a stable heap pattern.

这三句是本篇最重要的口径锚点：**Apple 亲自承认泄露一个指针就能定位大量对象、亲自承认堆喷可把布局压回稳定模式**。因此本篇在 §1.1 把"位置不可知"列为独立一道门槛的同时，必须始终并列它的两个已知失效路径（泄露、喷洒）。

于是 Apple 给出的差异化做法是**类型分桶**，并明确它与地址随机化的不同，逐字：

> The randomization here is different. Even if the attacker learns the bucket assignment of every type on the system, that reveals only which replacement types for a given UAF would work; it doesn't enable success for a specific replacement type that the attacker chose *a priori* … There's nothing the attacker can do at runtime to make their preferred replacement type feasible if the system assigned it to a different bucket than the UAF type. And since bucketing is random for every boot…

> Another benefit of randomized bucketing is that it maximizes the value of kalloc_type's strict free semantics. Strict free means that the `kfree_type()` callsite is restricted to only ever freeing addresses that belong to the correct zone, which makes it harder to transfer a UAF outside of the bucket containing the vulnerable type.

三条要点：**随机粒度是"每次启动"**（"random for every boot"，不是每次分配——这一点对能否用泄露绕开起决定作用）；**目标是"让预先选定的替代类型失效"而不是"让地址不可知"**；以及**释放点被限定到正确 zone**（strict free）是独立的第二道检查，它不依赖随机性。

同页还逐字给出两处与本篇其他章节直接相关的实现细节：`packs some object pointers into 32 bits, which limits the range where pointed objects can live`（打包到 32 位从而**限制被指对象可存在的范围**——这是"边界"而非"随机"），以及 `for C++ objects, virtual method calls dispatch through a PAC-signed vtable pointer, which reduces the set of exploitable type confusions` 与 `We aggressively PAC-protected pointers from typed allocations to data allocations to minimize opportunities for second-order type confusion`（**PAC 被用来封二次类型混淆**，属 §6.7 的正面配合证据）。签名方案按 **8 byte granule** 编码 `KT_GRANULE_PADDING/POINTER/DATA/DUAL/PAC` 五种属性，逐字可见其把"这个位置到底该不该是指针、该不该带认证"当成元数据来强制。

配套的实证分析来自同博客第二年的一篇（**May 23, 2023**，署名逐字 "Posted by Apple Security Engineering and Architecture (SEAR)"），它把上述设计放到一个真实漏洞上做假设分析。逐字定位句：

> Our initial post about memory safety enhancements in the XNU kernel focused on kalloc_type — our new allocation API which provides randomized, bucketed type isolation to mitigate the exploitability of most use-after-free (UAF) vulnerabilities.

量化结论逐字：

> The conventional approach of using a single reallocation type would have just an **8% success rate** under kalloc_type, and our analysis suggests that attackers will run into a **fundamental reliability cap** for many use-after-free vulnerabilities.

样本口径必须照抄限定（逐字）："On an iPhone 13 Pro running iOS 16 beta 1, there are **194 regular (not variable-length) types** in that size class, with **119 unique signatures** and **90 signature groups** among them."，且"the allocator metadata for kalloc_type lives entirely outside the allocated region"。因此这三个数字与那个 8% **只属于"一个漏洞、一台设备、一个构建"**，不得写成"iOS 通用 UAF 成功率"（§16.6）。同页也逐字列出残留面："Of course, signature collisions, the data submap, and within-type UAFs are all still attractive targets under kalloc_type."——**官方自己写明三类仍在射程内的目标**，这与 §4.5 的"缓解不等于修复"完全一致。

## 4.3 内核栈、返回地址与调用约定保护

内核侧的返回地址保护属于**第二道门槛**（指向可验证），而非随机化。手册逐字（`sec8b776536b`，2026-01-28）：

> 指针认证代码 (PAC) 用来防止对内存损坏错误的利用。系统软件和内建 App 使用 PAC 来帮助防止修改函数指针和返回地址（代码指针）。PAC 使用五种 128 位密钥值来签名内核指令和数据，并且每个用户空间进程都有其自己的 B 类密钥。

因此**内核是被签名的对象**（"签名内核指令和数据"，五种 128 位密钥），逐字细节见 §6。返回地址签名的绑定对象在源码头文件里定义得非常具体：`xnu/EXTERNAL_HEADERS/ptrauth.h` 逐字注释指出用于签名栈上返回地址的密钥，其 discriminator 是**存储地址 + 8，即函数入口时的栈指针值**（帧指针为存储地址 + 16）。可用推论（A- 级，须标"源码可证"）：**把一个已签名的返回地址从 A 栈帧搬到 B 栈帧会认证失败**，因为签名与栈位置绑定；这等于用认证实现了"栈位置知识"的部分价值，且不依赖栈地址随机（§3.1 的栈 slide 缺口因此不是单点依赖）。

内核内存层还有三道与执行权限相关的保护，逐字（同页）：KIP（"内存控制器会拒绝对受保护的物理内存区域的写入"、MMU"被配置为帮助防止从受保护内存区域之外的物理内存中映射权限代码，并帮助防止对内核内存区域内的物理内存进行可写入映射"，且"为防止重新配置，用于启用 KIP 的硬件会在启动过程完成后锁定"）、PPL（"旨在防止代码签名验证完成后对用户空间代码进行修改"，且**macOS 不提供**）、SPTM+TXM（"设计为协同工作以协助保护用户和内核进程的页表免遭修改，**其中包括攻击者可写入内核和可绕过控制流程保护的情况**"）。

那一句加粗的限定是全篇分量最重的一手承认：**Apple 在公开手册里写明控制流程保护存在被绕过的情形**，并把 SPTM 定位为兜底。逐字口径是"可绕过控制流程保护的情况"，因此本篇不得把 PAC 写成阻断性防线（§6.9 误解清单第 1 条）。同页两条脚注逐字值得一起引：

> 1：安全页表监控器 (SPTM) 在 A15 或后续芯片和 M2 或后续芯片的 SOC 中受支持，并在支持的平台上替代了页面保护层。
> 2：页面保护层 (PPL) 和安全页表监控器 (SPTM) 会在 macOS 除外（因为 macOS 设计允许运行任何代码）的所有平台上强制执行受信任的签名代码。包括页表保护在内的所有其他安全属性均可用于所有受支持的平台。

"macOS 设计允许运行任何代码"是手册对 **iOS 与 macOS 利用难度差异根因**最直白的表述（§12.2 的主引文）。另需注意 SPTM 取代 PPL 那句里的硬件下限与"依赖新的芯片原语，它基于 PPL 所用的快速权限访问限制进行了改进"（逐字），把 §2.2 的 APRR 与 §4.3 的 SPTM 连成一条硬件演进线：**APRR（A11/S3 起）→ SPTM+TXM（A15/M2 起）**。

## 4.4 手册页在内核随机化上的沉默意味着什么

沉默分三种，必须区分，否则会把"缺口"写成"结论"：

- **术语级沉默**（`KASLR`、`slide`、`BTI`、`GCS`、`shadow stack`、`canary`、`malloc`、`zone`、`guard` 全库命中 0）：含义是**手册不使用这些工程术语**。它同时说明：面向用户与企业的文档不承担描述内核实现的职责，因此"手册没写"在这类主题上是**预期行为**，不是遮掩。简报另逐字记录：早前对 PDF 版的同类检索结论（BTI/GCS/KASLR 命中 0）在本次网页版一手页面上**独立复核成立**（文稿修订记录页 `secb82d6b274` 最新条目为"2026 年 3 月"）。
- **承诺级沉默**（ASLR 的位数、熵源、是否每次启动必然变化、能否关闭）：含义是**Apple 不做此类承诺**。这是 §15.2 量化基线表必须留空的原因，也是 §16.6 全部"必须加限定语"表述的来源。
- **反向沉默**（内核侧唯一那句 ASLR 出现在术语表而非正文）：含义是**内核执行 ASLR 这一事实被承认，但没有对应的正面描述段落**。评审时不能把术语表的一句当成机制章节引用，也不能因为它只在术语表就断言"内核随机化未实现"。

正确的正文句式（§16.2 模板）：

> Apple 公开手册（含 2026 年 3 月修订记录）未收录 KASLR、BTI、GCS 三个术语；因此内核镜像位置随机化与分支目标强制的**存在依据来自官方开源代码（源码可证、Apple 未公开承诺）**，其**强度与位数没有任何公开出处**，本篇不代填。

## 4.5 从可观测行为反推机制：允许与不允许的推断边界

**允许**：

1. **从段与元数据的存在性反推构建侧开关。** `__AUTH` / `__AUTH_CONST` / `__auth_stubs` / `__auth_got` / `__auth_ptr` 五个官方段/节名可在 dyld 源码与产物中核对（§6.4），"某二进制有 `__auth_stubs`"是可验证事实。
2. **从崩溃形态反推撞到了哪道墙**（§13.2），但只能到"类型"层，不能到"具体机制是否启用"层。
3. **从公开矩阵表反推某项硬件能力在某代芯片上可用**（§12.1），因为矩阵表本身是 A 级页面。
4. **从官方"覆盖空洞"自陈反推风险的官方态度。** 模范引文是 auxKC 文本被有意不标 guarded 那段：逐字给出后果（"branches to these pages are unchecked!"）、理由（"break ABI compatibility"）与风险评估（"likely impractical to exploit in practice"）——**三段必须一起引**，只引前半会得出与官方相反的结论（§7.1、§16.4）。

**禁止**：

1. **从"某次崩溃里地址看起来固定"反推"该设备 ASLR 关闭"。** 单次观察没有对照，且共享映射（§2.4）本来就使 r/o 位置在各进程间一致。
2. **从模拟器或 Mac 上的行为反推 iOS 真机。** libmalloc 的 `#if TARGET_OS_IPHONE && !TARGET_OS_SIMULATOR` 与 `#error ASLR unhandled on this platform` 逐字说明**模拟器不在该随机化路径内**；macOS 侧 PPL 官方缺席。
3. **从"A 机制存在"反推"B 机制被削弱"。** §1.1 的十五道门槛彼此正交，手册与源码都没有把它们写成替代关系。
4. **从第三方给出的位数反推任何结论。** 社区流传的"iOS ASLR = 21 位熵"一类数值，在本轮四个官方仓库（xnu / dyld / libmalloc / security 的 main tip）**无一手出处**，故本篇整体不使用（§5.3）。
5. **把源码 tag 与设备运行版本对齐。** A- 级引用的版本口径必须照登：xnu main `f6217f89`／最新 tag `xnu-12377.121.6`、dyld main `fd8d0c4d`／`dyld-1378`、libmalloc main `c49dafa2`／`libmalloc-812.100.31`、security main `db15acbe`／`Security-61901.120.67`；main tip **不等于**任何已发布 iOS 构建。

**方法论结论**：这套体系的公开程度决定了**评估只能做"结构核对 + 形态归类"，不能做"强度测量"**。§15 全部判定项都是前者。

# 5. 熵从哪儿来，质量怎么谈

## 5.1 密码学伪随机数生成器与熵源

手册有"随机数生成"页（`seca0c73a75b`，2024-12-19），确立内核为内核空间与用户空间提供安全随机数的 CPRNG 机制；该页由本系列《网络通信机密性与完整性保护》一期逐字取过，本篇不重抄全文，只登记一个**关键负面事实**：

> 该页**未出现**"随机化""ASLR""地址"任何一个词，**没有把 CPRNG 的熵与地址随机化关联**。

因此"ASLR 的 slide 由 CPRNG 提供熵"这一句在本篇属**未核实**。它是工程上高度合理的猜测（内核装载路径调用的正是 `random()`），但合理猜测不等于一手口径，本篇把它列进 §16.6"必须加限定语才能写出"的清单。

能写成 A 级的只有一条**相邻事实**：安全隔区侧的"每次启动生成随机临时密钥"，手册逐字（`sec59b0b31ff`，2024-12-19）：

> 当设备启动时，安全隔区 Boot ROM 会为内存保护引擎生成随机临时内存保护密钥。每当安全隔区写入到其专用内存区域时，内存保护引擎就会在 Mac XEX (xor-encrypt-xor) 模式中使用 AES 加密内存块，并为内存计算基于密码的消息认证码 (CMAC) 认证标签。

这是**内存加密密钥**，不是地址熵；但它是本篇唯一一处 Apple 逐字承诺"每次启动换随机值"的表述。写作时可以用它来说明"Apple 在愿意承诺时会明确写出随机值的生命周期与用途"——**反过来说，ASLR 没有这样的句子，属口径差异而非疏漏**（C 级观察，标明为推断）。同页还给出该失败路径的可观测后果："发生内存认证错误后，在系统重启前安全隔区会停止接受请求"——**一次性熔断，不产生崩溃报告字段**，这与 §13 的"内存防护命中留下什么痕迹"构成一组对照：有的防护会记事件，有的直接把子系统关到重启。

## 5.2 内核态与用户态取随机数的公开接口

三处实现层事实（A- 级）值得登记，因为它们说明"不同组件用的随机源不是同一个"：

- **内核装载路径用 `random()`。** `mach_loader.c:785-802` 里主镜像 slide、dyld slide 与 section 维度各取一次随机值（§2.1）。这是内核态通用随机接口，不是专用的"地址熵源"。
- **用户态分配器用 `arc4random_uniform()`。** `libmalloc/src/vm.c:117-123` 的实现是"上界除以页大小得页数，`arc4random_uniform(max_pages + 1)`，再乘回页大小"，即 guarded range 的 4M–5M 随机宽度与 1M 缺口位置都由**页对齐后的均匀取值**产生（§3.4）。这是"距离随机"的具体算法形状，且用的是用户态安全随机接口而非内核 `random()`。
- **Exclaves 走独立熵初始化。** `libmalloc/src/vm.c:52-53` 逐字 `#if MALLOC_TARGET_EXCLAVES` → `arc4random_buf(malloc_entropy, sizeof(malloc_entropy));`，并且该路径**不启用** guarded range 逻辑（`vm.c:116` 的 `#if !MALLOC_TARGET_EXCLAVES`，另有 `vm.c:33` 的同族条件）。这是"同一份分配器代码在不同执行环境下随机化路径不同"的一手证据，属 §12.3 平台差异的技术底稿。

**这三条合起来支持的口径**：谈"熵"必须同时说明**谁取的随机数、取来干什么、进程能否观测到**。手册不做这种区分，源码做；因此本篇凡涉及熵的段落都同时标注层（内核/用户态）与用途（地址/距离/密钥）。分配器熵还与内核 ASLR 状态联动（§3.4 的 "Eliminate cloaking"），说明**这不是三个互不相干的随机数**。

## 5.3 为什么"多少位熵"几乎没有官方数值

本篇对这条缺口的解释是三层的，缺一层就变成抱怨：

1. **面向用户的文档不承担量化职责。** 手册对 ASLR 的全部用词是"有助于""增加了执行攻击的难度"，是**效果描述**而非**参数描述**。同一页族里对硬件能力倒是有矩阵表（§12.1），说明手册愿意写"哪代支持"，不愿意写"多少位"。
2. **数值本身是内存优化与地址空间形状的函数。** §2.3 与 §3.4 给了两个一手反例：arm64 主镜像 slide 被**主动限制**到 16MB，逐字理由是"conserve contiguous VA space in the more limited embedded address space"与"minimize pmap memory usage on 16KB page systems"；缓存 slide 的格由 **32MB**（arm64）/ **1GB**（x86_64）页表共享区间决定。loader slide 的 4MB 上限则逐字写明目的是"ensure **at least 8 bits** of independent entropy on 16KB page systems"——这是全部材料里唯一一处 Apple 自己在随机化语境下写"bits"的句子，且它是**下限保证**而非位数宣称。
3. **公开位数等于给出预算。** 这一条本篇标为 **C 级推断**，不作主张——一手文本没有解释为什么不公布。

结论句（§16.6 模板，也是 §15.2 表格的写法依据）：

> Apple 未公开用户态与内核地址随机化的熵位数、熵源关联与重随机化保证；可复核的只有源码给出的**范围上限**（主镜像 16MB、loader 4MB、缓存"about 512MB"、分配器 guarded range 4M–5M / carveout 1M、`entropic_bits` 7 或 3）与一处 loader 注释自称的"at least 8 bits of independent entropy"。**这些是"当前实现的上限与自陈"，不是产品承诺。**

## 5.4 会被重复使用的随机化：备份恢复、快照、重启与克隆

评估"随机值是否真的每次都换"时，四个场景要分开问：

- **重启。** 主镜像与 dyld slide 在每次装载时重取；缓存 slide 依赖 reslide 事件计数（`vm_shared_region_reslide_count` 逐字记"the number of times an event has forced the recalculation of the reslide shared region slide"）。而内核类型分桶逐字是 **"random for every boot"**（§4.2）——粒度绑定**启动**而非分配，因此**一次泄露在同一次开机内对后续进程可能持续有效**。这是"泄露型绕过"与"重随机化"的真正交界，也是 §13 反复强调"按启动分组统计"的原因。
- **备份恢复。** 手册没有一句说明恢复后地址随机化如何初始化。可依据的结构性事实是：布局是**运行时属性**，不存储在备份内容中；因此"恢复后 slide 相同"这种担忧本篇**不作断言**，登记为缺口。真正有逐字出处的只有"主目录是在安装 App 时随机分配的"（§2.5）——**恢复流程是否会重新分配该项，手册未述**。
- **宗卷快照与文件克隆。** 属数据层，不影响运行时地址。本篇只提示一条常被混用的表述：**不要把"文件内容被复制"与"布局信息被复制"混为一谈**，前者受数据保护类管辖（系列 #89 篇），后者只在崩溃报告的**偏移**意义上存在（§14）。
- **批量部署与克隆镜像（企业场景）。** 监督式注册、批量安装的设备**共享同一系统版本与同一份共享缓存**；叠加 §2.4 官方自陈的"r/o 部分可共享页表"，可得**同批设备的只读布局高度一致**。这一句须写成 **C 级推论**（依据是官方设计描述，不是官方声明），但它是 §17.6 机型治理建议的直接根据：**批次一致性会削弱"尤其是对多个设备的攻击"这句收益**，因此批次越大，越应把"版本一致性治理"与"随机化强度"分开评估。

## 5.5 弱随机化的可观测症状与验证方式

不给测量手段，只给**可复核的验证清单**——每项都用一手来源或产物结构判断：

| 症状 | 可能含义 | 正确验证方式 | 不能得出的结论 |
| --- | --- | --- | --- |
| 同一 App 两次启动的关键地址相同 | 该二进制可能非位置无关（§2.2 `-mdynamic-no-pic` 路径），或该项本就属共享映射 | 核对产物 PIE 形态、比对 `__TEXT` 装载地址与镜像 UUID | 不能得出"设备关掉了 ASLR"（共享映射本就一致） |
| 崩溃报告里"地址 − 镜像基址"在不同设备上恒定 | **正常**：偏移是版本属性，随机化只改基址 | 用偏移比对而不是裸地址比对（§13.5） | 不能得出"随机化无效" |
| 大量同类崩溃在**同一裸地址**复现 | 可能是同一启动会话内复用，或某条路径未走随机化 | 按"每次启动／每次安装／每台设备"三尺度分组统计（§2.5） | 不能直接得出熵不足 |
| 分配行为在版本间突然变稳定 | 可能是分配器路径或分桶变化（§4.2 逐字 "expanded its use across our systems"） | 比对系统版本、矩阵表可用项与博客发布节奏（§12.1、§15.5） | 不能据此判断内核实现细节 |
| 用模拟器或 Mac 复现得到的布局与真机不同 | 逐字证据支持：模拟器被排除在分配器 ASLR 路径外；macOS 无 PPL | 结论只在真机上下文下达（§4.5 禁止第 2 条） | — |
| `-random_uuid` 被用作"随机化措施" | **误解**：它只影响 LC_UUID 的生成方式，官方还指出哈希式 UUID 对可复现构建更重要 | 检查该选项是否仅为加快调试构建 | 不能把它算进任何安全基线（§10.4） |

这张表把"随机化看起来没用"这类直觉拆成三个可分别证伪的命题：**二进制侧、观测尺度侧、平台路径侧**。§15.3 自查清单第 6 组直接引用它。

顺带登记一条**必须澄清的负面结论**：链接器手册页**没有** `-random` 这类"运行时随机化"选项；唯一含 random 的选项是 `-random_uuid`，而它是构建期标识生成方式，与安全属性无关。凡把 `-random_uuid` 写进加固清单的文章，都属于把名字当语义。

# 6. 指针认证：把"指向哪里"变成可验证属性

## 6.1 它解决的是哪一类问题，与随机化的分工

指针认证码（Pointer Authentication Code，Apple 官方中文页面写作"指针认证代码"，下称 PAC）与地址随机化解决的是**两个不同问题**，尽管 Apple 在两个独立页面上使用了**几乎相同的目的语**。

《Apple 平台安全保护》操作系统完整性页逐字（A 级，`sec8b776536b`，页面发布日期 2026-01-28）：

> 指针认证代码 (PAC) 用来防止对内存损坏错误的利用。系统软件和内建 App 使用 PAC 来帮助防止修改函数指针和返回地址（代码指针）。

而同一手册的 ASLR 页（A 级，`sec15bfe098e`）说的是 ASLR "有助于防止对内存损坏错误的利用"。**两句都成立，但拦的是同一条利用链上的不同环节**：

| 门槛 | 保护的是哪个属性 | 破坏它的原语 | 恢复它需要 |
|---|---|---|---|
| 地址随机化 | 指针的**值指向哪里**是不可预知的 | 信息泄露（读原语）、共用基址、重启复用 | 一次新的随机化事件（§2.5） |
| 指针认证 | 指针的**值是否被篡改过**是可验证的 | 任意写（哪怕只有一处、可控的写） | 拿到与目标指针匹配的签名，或找到一条不认证该指针的执行路径 |

这就是本篇"三道正交门槛"模型的第二道（§0.2）。它的关键性质是：**PAC 不要求攻击者猜不到地址，只要求攻击者改不了指向**。反过来也成立——PAC 对"攻击者原本就有权读的那个值"完全无感：把一个未被篡改的合法指针拿去用，认证会一路通过。因此"PAC 之后 ASLR 就没必要了"与"ASLR 之后就不需要 PAC"这两种论断都超出了一手证据能支持的范围。

**必须写进正文的一条主语限定**：上面那段逐字文本的主语是"**系统软件和内建 App**"。手册通篇没有把第三方 App 一律纳入 PAC 保护范围。据此能写出的最强口径仍然只是：**一手文本未承诺第三方代码全面使用 PAC**（这是缺失，不是 Apple 的否定表述，见 §16.2）。

## 6.2 被签名的对象与不被签名的对象

操作系统完整性页给出了一张"项目 | 键 | 加盐"三列表，是 Apple 一手文本中对"到底签什么"最完整的清单（A 级，逐行逐字转录）：

| 项目 | 键 | 加盐 |
|---|---|---|
| 函数返回地址 | IB | 存储地址 |
| 函数指针 | IA | 0 |
| 块调用函数 | IA | 存储地址 |
| 块描述符指针 | DA | 存储地址 + 0xC0BB |
| Objective-C 方法缓存 | IB | 存储地址 + 类 + 选择器 |
| Objective-C Isa 指针 | DA | 存储地址 + 0x6AE1 |
| Objective-C Super 指针 | DA | 存储地址 + 0xB5AB |
| 选择器类型的 Objective-C 实例变量 | DB | 存储地址 + 0x57C2 |
| Objective-C 只读类数据指针 | DA | 存储地址 + 0x61F8 |
| C++ 虚函数表条目 | IA | 存储地址 + 哈希值（损坏的方法名称） |
| C++ 虚函数表指针 | DA | 存储地址 + 哈希值（损坏的基类虚函数表名称） |
| 计算的 Goto 标签 | IA | 哈希值（函数名称） |
| 内核线程状态 | GA | • |
| 用户线程状态寄存器 | IA | 存储地址 |

从这张表可以直接读出四条结论，它们各自反驳一类流行说法：

1. **返回地址是被签的，且用 IB 键、以存储地址为加盐**。这意味着"篡改栈上的返回地址"不是被缓解而是被定义为可检测事件；也意味着**把一个已签名的返回地址从 A 槽搬到 B 槽会认证失败**（加盐随存储地址变化，§6.3）。
2. **虚表被签**。"PAC 只保护 C 函数指针，不碰 C++ 虚表"这个说法与官方表直接冲突：表内同时列出"虚函数表条目"（IA）与"虚函数表指针"（DA）。
3. **Objective-C 的 isa / super / 选择器类型实例变量 / 只读类数据指针 / 方法缓存全部在列**。这类字段正是"对象伪造"与"方法缓存投毒"的攻击面，官方把它们写进同一张表，是"PAC 覆盖面按语义类别而非按语言特性划定"的一手依据。
4. **签名与哈希值绑定，且官方承认该哈希是"损坏的"（mangled）** 名称/基类虚表名的哈希——即 discriminator 来自符号派生而非完整类型信息。这是表内自带的一处口径注意项，正文不应把它美化成"绑定完整类型"。

**不被签名的对象**同样有一手出处。`ptrauth.h`（A- 级）在描述 block 相关签名时逐字写明：

> Note that block object pointers themselves (i.e. the direct
> representations of values of block-pointer type) are not signed.

即被签的是 block 的**调用函数、拷贝/析构函数、`__block` 变量拷贝/析构函数**这些函数指针，而不是 block 对象指针本身。同文件末句逐字：

> /* Other pointers signed under the ABI uses private ABI rules. */

（原文 "Other pointers signed under the ABI use private ABI rules."）**结论口径**：官方明确承认存在"按私有 ABI 规则签名"的其余指针类别，因此**任何"PAC 完整清单"类文章都必然不完整**——包括本篇 §6.2 这张表，它是官方公布的那一部分，不是全集。

## 6.3 密钥、上下文与"签同一指针的两种身份"

手册在这一层给出了两段可逐字引用的表述。第一段是密钥数量与位数（A 级）：

> PAC 使用五种 128 位密钥值来签名内核指令和数据，并且每个用户空间进程都有其自己的 B 类密钥。

第二段是签名载体（A 级，同页）：

> 签名值储存在 64 位指针顶部未使用的填充位中。在使用签名前会进行验证，且填充会恢复以帮助确保指针地址正常工作。

这里有三点要精确处理，它们是正文最容易失守的地方：

- **官方列出的密钥种类是五个：IA、IB、DA、DB、GA**（表格"键"列的取值）。而 xnu 的 ABI 头文件 `ptrauth.h`（A- 级）列出的是**四个用户态 ABI 键**：`ptrauth_key_asia = 0`、`asib = 1`、`asda = 2`、`asdb = 3`。两套命名可对齐（IA↔asia、IB↔asib、DA↔asda、DB↔asdb），但 **GA（通用键，表中用于"内核线程状态"）不在 `ptrauth.h` 这四个 ABI 键的枚举里**。这是一处真实的口径差异（§16.1 C 类），不能靠"都是五个键"糊过去。
- **"128 位"是密钥的位数，不是签名的位数。** 手册只给出签名载体是"64 位指针顶部未使用的填充位"，**从未公布签名占多少位**。§16.2 登记此项为缺失，正文凡涉及"PAC 有 N 位签名/因此有 2^N 空间"的推算一律不写。
- **A 类与 B 类的进程相关性只说了一半**：官方逐字承诺"**每个用户空间进程都有其自己的 B 类密钥**"，但对用户空间 A 类密钥如何生成、是否共享、何时轮换**没有任何逐字说明**。这条不对称是正文可以直接引的硬事实，也是"函数指针（IA）签名跨进程不可移植性"无法从一手文本断言的原因。

**上下文（discriminator / 加盐）的官方定义**在 `ptrauth.h` 里比手册更具体（A- 级，逐字节选）：

>   /* The key used to sign return addresses on the stack.
>      The extra data is based on the storage address of the return address.
>      On ARM64, that is always the storage address of the return address plus 8
>      (or, in other words, the value of the stack pointer on function entry) */
>   ptrauth_key_return_address = ptrauth_key_process_dependent_code,
>
>   /* The key used to sign frame pointers on the stack.
>      The extra data is based on the storage address of the frame pointer.
>      On ARM64, that is always the storage address of the frame pointer plus 16
>      (or, in other words, the value of the stack pointer on function entry) */
>   ptrauth_key_frame_pointer = ptrauth_key_process_dependent_data,

对照手册表："函数返回地址 | IB | 存储地址"——两处一致，且源码给出了"存储地址 + 8 = 函数入口时的栈指针值"这一层等价关系。含义是**返回地址的签名绑定了它的栈位置**：这就是"ROP 式地搬运已签名返回地址"在架构层面被拦的直接依据，也是手册那句 ROP 表述（§6.6）的技术注脚。

**"同一指针的两种身份"**指的是 `ptrauth.h` 里两个键的语义分工（A- 级，逐字）：

>   /* A process-independent key which can be used to sign code pointers.
>      Signing and authenticating with this key is a no-op in processes
>      which disable ABI pointer authentication. */
>   ptrauth_key_process_independent_code = ptrauth_key_asia,
>
>   /* A process-specific key which can be used to sign code pointers.
>      Signing and authenticating with this key is enforced even in processes
>      which disable ABI pointer authentication. */
>   ptrauth_key_process_dependent_code = ptrauth_key_asib,

两条注释合起来是本节最重要的口径：

- **同一个"代码指针"可以是进程无关签名的，也可以是进程相关签名的**，二者强度不同、可移植性不同、失效条件也不同。写正文时必须先确定讨论的是哪一类，否则"PAC 可被关闭"与"PAC 不可被关闭"这两个都对的说法会互相抵消。
- **`asia`（IA，进程无关代码键）在"关闭 ABI 指针认证的进程"里是 no-op；`asib`（IB）则是 "enforced even in processes which disable ABI pointer authentication"。** 而返回地址恰好落在 `asib` 上。所以最精确的一句话是：**进程级关闭指针认证并不能取消对栈上返回地址的强制认证**（这是官方注释级依据，属 A- 级源码，不是文档承诺）。
- 反过来，`asda`（DA）与 `asdb`（DB）在被关闭的进程里同样是 no-op（同文件注释逐字），而 isa/super/虚表指针正落在 DA/DB 上。**"哪一类指针在降级进程中仍被保护"因此是可以逐项回答的**，答案就在键与表的对应关系里，不需要推测。
- C++ 虚表指针与函数指针的 extra data 官方定为**恒为 0**（`ptrauth.h` 逐字 "The extra data is always 0."，用于 `ptrauth_key_function_pointer` 与 `ptrauth_key_cxx_vtable_pointer`）。零 discriminator 意味着**同一个合法指针值的签名在任意位置都有效**——这是官方承认的覆盖限制（§6.7 第 3 条），也解释了手册表为什么给虚表**条目**额外绑定"存储地址 + 哈希值"：**指针本身弱绑定，条目强绑定**。

## 6.4 编译侧要求与自动覆盖范围

Xcode Build Settings Reference（A 级，`developer.apple.com/documentation/xcode/build-settings-reference`，页脚 © 2026）给出四个与本篇直接相关的开关，逐字如下：

> Enable Pointer Authentication
> Setting name: `ENABLE_POINTER_AUTHENTICATION`
> Builds the target with pointer authentication enabled. Adds an additional architectural slice (arm64e) with pointer authentication instructions to `ARCHS_STANDARD`. Has no effect if `ARCHS` has been overridden to not be based on `ARCHS_STANDARD`.

> Enable Hardware-Checked Pointer Arithmetic Slice
> Setting name: `ENABLE_HARDWARE_CHECKED_POINTER_ARITHMETIC_SLICE`
> Builds the target with hardware-checked pointer arithmetic enabled. Adds an additional architectural slice (arm64e.x1) with hardware-checked pointer arithmetic instructions to `ARCHS_STANDARD`. Has no effect if `ARCHS` has been overridden to not be based on `ARCHS_STANDARD`.

> Enable Enhanced Security
> Setting name: `ENABLE_ENHANCED_SECURITY`
> Enables a set of security build settings, including pointer authentication, typed allocator support, hardened C++ standard library, and security-related compiler warnings. These settings can be disabled individually.

> Enable Stack Zero Initialization
> Setting name: `CLANG_ENABLE_STACK_ZERO_INIT`
> Automatically initializes stack variables to zero as a security protection.

四条必须照搬的口径：

1. **PAC 在构建侧的官方形态是"追加一个架构切片"，不是"把现有 arm64 重新编译一遍"**。逐字 "Adds an additional architectural slice (arm64e) … to `ARCHS_STANDARD`"。这意味着一次构建产物里同时存在 arm64 与 arm64e 两份代码，运行时按 CPU 能力选用（选择逻辑见 §6.5）。凡是把 PAC 写成"编译开关一开就全二进制受保护"的文章，都忽略了切片级并存这一层。
2. **覆盖 `ARCHS` 会让该开关静默失效**（逐字 "Has no effect if `ARCHS` has been overridden…"）。这是评审里最容易漏的一项：目标看起来"启用了指针认证"，实际产物里没有 arm64e 切片。§15.4 构建审计据此把"核对产物切片清单"列为必检项，而不是核对 build setting。
3. **`arm64e.x1` 是与 `arm64e` 并列的新切片名**，官方描述为 "hardware-checked pointer arithmetic"（硬件检查指针运算）。这是 Apple 一手页面对该特性的唯一构建侧命名，正文保留英文原词、不译。
4. **`ENABLE_ENHANCED_SECURITY` 是伞形开关且逐项可关**（逐字 "These settings can be disabled individually"），它一次带入的四类东西分别是 pointer authentication、typed allocator support（类型化分配器，§9.5）、hardened C++ standard library 与安全相关编译器警告。**这一条把"加固"从一个布尔值还原成一组可分别关闭的策略**，与 §10.4 的 macOS 例外模型同构。

**一处重要的检索缺口（负面结论，A 级范围内的 0 命中）**：本轮在 developer.apple.com 的构建设置参考全文，以及 xnu、dyld、libmalloc、security 四个 apple-oss-distributions 仓库中检索 `-mbranch-protection`、`pac-ret`、`branch-target`、`bti`（作为构建设置取值），**命中均为 0**。也就是说：

- **Apple 一手页面没有给出 `-mbranch-protection` 的取值文档**（auto / standard / pac-ret / pac-ret+bti 这一套是 LLVM 侧的口径，不是 Apple 的一手口径）。
- 因此正文**不得**用编译器选项取值表来代表"Apple 的 PAC 编译要求"，只能引上面四个 Xcode 设置名与 §7.2 的内核构建变量。**这是本篇反复使用的一条纪律：一手证据缺席时，宁可写"未公布"，也不把上游工具链文档改写为 Apple 口径。**

## 6.5 签名、加载与运行时要求

PAC 不只是"编译期生成指令"，它在 Mach-O 头部、加载器与内核执行路径上都留下了可核对的位与条件。以下逐字来自 xnu（A- 级）。

**架构 subtype 内编码了 ptrauth ABI 版本**（`xnu/osfmk/mach/machine.h:383-402`）：

> #define CPU_SUBTYPE_ARM64E              ((cpu_subtype_t) 2)
>
> /* CPU subtype feature flags for ptrauth on arm64e platforms */
> #define CPU_SUBTYPE_ARM64_PTR_AUTH_MASK 0x0f000000
> #define CPU_SUBTYPE_ARM64_PTR_AUTH_VERSION(x) (((x) & CPU_SUBTYPE_ARM64_PTR_AUTH_MASK) >> 24)
> …
> #define CPU_SUBTYPE_ARM64_PTR_AUTHV0_VERSION 0
> #define CPU_SUBTYPE_ARM64_PTR_AUTHV1_VERSION 1
> #define CPU_SUBTYPE_ARM64_PTR_AUTH_CURRENT_VERSION CPU_SUBTYPE_ARM64_PTR_AUTHV0_VERSION
> #if XNU_TARGET_OS_OSX
> #define CPU_SUBTYPE_ARM64_PTR_AUTH_MAX_PREFERRED_VERSION CPU_SUBTYPE_ARM64_PTR_AUTHV1_VERSION
> #else /* XNU_TARGET_OS_OSX */
> #define CPU_SUBTYPE_ARM64_PTR_AUTH_MAX_PREFERRED_VERSION CPU_SUBTYPE_ARM64_PTR_AUTHV0_VERSION
> #endif /* XNU_TARGET_OS_OSX */

三点硬结论：

- **签名 ABI 有版本，且版本位就在 cpusubtype 里**（掩码 `0x0f000000`，右移 24 位，共 4 bit）。V0/V1 两个具名版本，**`CURRENT_VERSION` 仍是 V0**。
- **`MAX_PREFERRED_VERSION` 按平台分叉**：macOS 允许到 V1，非 macOS（即 iOS 系）只到 V0。这是"iOS 与 macOS 的 arm64e ABI 上限不同"的逐字一手依据，直接支撑 §12.6"同一份代码跨平台防护强度不等"的判断链。
- 内核据此判定是否按 ptrauth 路径加载（`kern_exec.c:1016-1029` 逐字）：`arm64_cpusubtype_uses_ptrauth()` 返回"subtype 等于 `CPU_SUBTYPE_ARM64E` **且** ABI 版本落在 `V0 … MAX_PREFERRED_VERSION` 区间内"。**版本超出该平台上限，就不是"按 ptrauth 加载"。**

**第三方 arm64e 在 macOS 上被拒载**（`kern_exec.c:1596-1615`，同段注释逐字）：

> 	/*
> 	 * ptrauth version 0 is a preview ABI.  Developers can opt into running
> 	 * their own arm64e binaries for local testing, with the understanding
> 	 * that future OSes may break ABI.
> 	 */

其后果逐字包含 `printf("%s: not running binary \"%s\" built against preview arm64e ABI\n", …)`、放行手段 "(to allow this, add \"-arm64e_preview_abi\" to boot-args)"，以及拒载时构造 `os_reason_create(OS_REASON_EXEC, EXEC_EXIT_REASON_BAD_MACHO)`。三处限定必须同时写：该逻辑整体包在 `defined(XNU_TARGET_OS_OSX)` 内、**platform binary 豁免**、可用 boot-arg 放行。**逐字结论 "future OSes may break ABI" 是 Apple 自己对 arm64e ABI 稳定性的表态**——正文任何地方都不可以把 arm64e ABI 细节当稳定规格陈述。

**代码签名侧的运行时挂钩**（A- 级 `cs_blobs.h` + A 级 TN3126 互证）：

- `CS_RUNTIME 0x00010000 /* Apply hardened runtime policies */`；TN3126 的示例输出逐字为 `flags=0x10000(runtime)`——即签名标志位与"应用加固运行时策略"是同一件事的两种表述。
- `CS_PLATFORM_BINARY 0x04000000 /* this is a platform binary */`——与上面 §6.5 的 arm64e 拒载豁免直接对应：**平台二进制身份本身就是一种强度差异**（§11.4）。

**arm64e 使用独立的共享缓存**（`kern_exec.c:1669-1680` 逐字）：

> 	/*
> 	 * arm64e uses pointer authentication, so request a separate
> 	 * shared region for this CPU subtype.
> 	 */
> 	cpu_subtype = p->p_cpusubtype & ~CPU_SUBTYPE_MASK;

同段注释还逐字给出选择依据："Determine which shared cache to select based on being told," / "matching a team-id or matching an entitlement."，并出现 `HARDENED_RUNTIME_CONTENT_ID`、`get_teamid_for_shared_region(imgp)`、`load_result.hardened_browser & BrowserWebContentEntitlementMask`。这一组句子的意义远超"PAC 有独立缓存"本身：**共享缓存的 slide 是 §2.3 里最大的一块随机化面，而它的面值按 team-id / entitlement 切分**——同一份系统上不同 App 可能落在不同的共享区域副本上，"某处泄露的共享缓存基址能否复用"因此不是全局问题而是分区问题（§2.4、§14.7）。

**内核用 "JOP" 指代用户态指针认证，并可按 task 关闭**（A- 级）：`kern_exec.c:1432-1443` 逐字在非 ptrauth 切片时设置 `imgp->ip_flags |= IMGPF_NOJOP;`，随后在 `:4426` / `:5470` 落成 `disable_user_jop` 与 `ml_task_set_disable_user_jop(new_task, …)`。口径：**这是源码内部命名，不是公开 API**，正文引用时必须这样标注；但它给出了"进程级降级"这条路径在加载器里确实存在的实现证据，与 §6.3 的 `ptrauth.h` no-op 注释互为印证。

## 6.6 验证失败的后果与留下的痕迹

手册对失败后果的逐字措辞止于此（A 级）：

> 验证失败会导致中止使用签名。这种验证提高了许多攻击的难度，如试图通过操纵储存在堆栈中的函数返回地址来欺骗设备恶意执行现有代码的面向返回编程 (ROP) 攻击。

这条句子的两个价值与一处缺口：

- **威胁模型点名**：官方在 PAC 页唯一具体命名的攻击类别是 **ROP**，且描述精确到"操纵储存在堆栈中的函数返回地址"。这是"代码复用攻击是 PAC 的主要目标"的一手命名证据，可直接用于 §1 门槛表的"拦什么"列。
- **"中止"是行为描述，不是实现规格**。手册**没有**写认证失败会产生什么异常类型、崩溃码、信号名，也没有出现 `EXC_BAD_ACCESS`、`EXC_GUARD` 或任何崩溃报告字段名。
- 因此"PAC 校验失败在崩溃报告里长什么样"在 support 手册侧是**缺口**（§16.2）。本篇 §13.2 给出的对应关系表明确标注为**从可观测字段反推的解释框架（B/C 级），不是 Apple 对 PAC 失败签名的承诺**——这条限定语必须保留。

架构层面，"失败会怎样"也不是单一形态：`arm_features.inc`（A- 级）里同时存在 `FEAT_FPAC` 与 `FEAT_FPACCOMBINE` 两个特性条目。二者属于 ARM 架构对"认证失败时的处理强度"的可选扩展；本篇只把它们登记为**内核向用户态暴露的能力位**（`hw.optional.arm.FEAT_*`，§6.8），**不解释其语义差别**——Apple 一手页未给出面向开发者的 FPAC 行为文档，任何具体描述都会越到二手口径。

## 6.7 指针认证打不掉的原语类别

本节全部是"由一手事实直接界定的边界"，不含推测。

1. **读原语与布局泄露**：PAC 验证的是"这个指针值有没有被改过"，不验证"这个指针值被看见了没有"。一个合法指针被读走，签名完好、认证通过。官方在博客侧对这一类风险的表述见 §4.2（逐字 "ASLR is often criticized because leaking one pointer value gives the attacker the location of a great number of interesting objects"）——那句话批评的是随机化，但对 PAC 同样成立：**签名信息本身是可以被搬运用的一种值**，只要攻击者能同时拿到指针与其合法签名形式。
2. **类型正确但语义错误的用**：PAC 不检查"这个指针该不该指向它指向的东西"。把 A 对象上一个通过认证的函数指针当成 B 对象的回调来调用，认证不会失败。这与 §9.5 类型化分配器要解决的问题是**两类正交的加固**（一个查指针完整性，一个查对象类型一致性）。
3. **extra data 恒为 0 的键位**：`ptrauth.h` 逐字规定函数指针（`ptrauth_key_function_pointer = ptrauth_key_asia`）与 C++ 虚表指针（`ptrauth_key_cxx_vtable_pointer = ptrauth_key_asda`）的 "The extra data is always 0."。零 discriminator = **签名不带位置信息**，因此同一个已签名值在进程内**可在任意地址存放后仍通过认证**。这不是实现缺陷，而是官方 ABI 定义的直接后果；也正是手册表把虚表**条目**单独绑到"存储地址 + 哈希值"的原因。
4. **未被签名的对象类别**：block 对象指针本身（§6.2 逐字）；以及"Other pointers signed under the ABI use private ABI rules."覆盖不到的其余类别——**清单不完整本身就是边界**。
5. **进程级降级路径**：`asia`/`asda`/`asdb` 在"disable ABI pointer authentication"的进程里逐字是 no-op。凡涉及"某进程关闭了 ABI 指针认证"的场景，DA/DB/IA 三类保护（含 isa、super、虚表指针、数据指针）在该进程内不成立，而 IB（返回地址）仍强制。
6. **不是代码复用的原语**：整数溢出导致的越界读写、逻辑缺陷、鉴权绕过、竞态——PAC 与这些不在同一层。§1.3 的"缓解 ≠ 修复"口径在此尤其重要：**一个被 PAC 完全保护的进程，仍然可以因一个非内存破坏缺陷而被完全控制。**
7. **依赖"是否真的编译进去了"**：§6.4 的 `ARCHS` 覆盖静默失效、§6.8 的 SoC 世代不支持、§12.4 的 32 位遗留路径——这三条都属于"PAC 名义上在、实际不在"，是本节最常用于评审的边界。

## 6.8 覆盖差：不同指令集变体与不同世代设备

**芯片世代**：手册的 SoC 矩阵（A 级，`sec87716a080` 与 `sec8b776536b` 含内容完全一致的同一张表）总纲句逐字为：

> 以下保护措施在运行 Apple 设计的受支持 SoC 的设备上可用，此类 SoC 包括 iOS、iPadOS、搭载 Apple 芯片的 Mac 上运行的 macOS、Apple tvOS、visionOS 和 watchOS。

"指针认证代码"一行的判定（从 HTML 内嵌图标 `IL_green_checkmark.png` / `IL_red_x.png` 逐格解码，属图片语义而非文字）：**A10 不支持、A11/S3 不支持，A12–A14 与 S4–S10 起支持，A15–A18、M1、M2–M4、A19/M5 均支持**。两点必须同时写：手册把"搭载 Apple 芯片的 Mac 上运行的 macOS"**明确纳入**适用范围；范围按 **SoC 世代**划定，手册**没有**给出"macOS 从哪个版本开始"的单独下限。

同时登记一处**口径冲突**（§16.1）：xnu 的 CPU family 注释（A- 级，`machine.h:413-420`）逐字明令禁止用型号推断特性——

> These are meant to identify the CPU's marketing name - an
> application can map these to (possibly) localized strings.
> NB: the encodings of the CPU families are intentionally arbitrary.
> There is no ordering, and you should never try to deduce whether
> or not some feature is available based on the family.

即**手册用"哪颗芯片"表述可用性，源码侧却要求"用特性位、不要用芯片型号"**。本篇的处置是：机型清单只用于采购与治理语境（§12、§17.6），**任何程序化判定一律写成 sysctl 特性口径**——官方公开路径为 `hw.optional.arm.FEAT_*` 与遗留名 `hw.optional.armv8_gpi`（= `FEAT_PACIMP`），全部 `CTLFLAG_RD` 且 `SECURITY_READ_ONLY_LATE`（`kern_mib.c:1143-1158` 逐字），另有 commpage 位 `CAP_BIT_FEAT_PAuth 19` / `PAuth2 20` / `FPAC 21` / `BTI 36`。手册全库对 `armv8.3` / `ARMv8.3` 这类 ARM 架构版本号 **0 命中**——Apple 一律用自家芯片型号表述，正文不应引入"ARMv8.3-A PAC"作为 Apple 口径。

**指令集变体**：`arm64_32`（watchOS 系 ABI，A- 级证据）在 libmalloc 里走 32 位分支，逐字 `int entropic_bits = 3;`（LP64 为 7）——堆随机化熵**按 ABI 变体分叉**。它同时具备三点一手属性：独立 subtype 家族（`CPU_SUBTYPE_ARM64_32_ALL/V8`）、与 arm64/armv7k 共享 struct 布局（`stat.h:375` 逐字 "supported ARM slices (arm64/armv7k/arm64_32) contain the same struct"）、签名侧同用 16K 页哈希（`machorep.cpp:576`）。**未核实项**：Apple 一手页未正面写"watchOS 使用 arm64_32"这句平台对应关系（见 §16.2）。

**被排除的运行时目标**：`dyld/common/Defines.h:195` 逐字把 `!TARGET_OS_EXCLAVEKIT && !TARGET_OS_SIMULATOR` 写在 arm64e 条件里；libmalloc 亦有 `#error ASLR unhandled on this platform`（模拟器排除）与 Exclaves 独立熵初始化路径（§3.4、§5.1）。口径：**Exclave 与模拟器不套用主 App 的随机化/PAC 模型**，在模拟器或 Mac 上得到的布局结论不可外推到真机（§4.5）。

## 6.9 常见误解清单

| 流行说法 | 一手文本的判定 | 依据 |
|---|---|---|
| "iOS 上所有二进制都启用了 PAC" | **不能写**：手册主语限于"系统软件和内建 App"；且 `ARCHS` 被覆盖时开关静默失效 | §6.1、§6.4 逐字 |
| "PAC 签名有 16 位，所以只有 2^16 种可能" | **一手未给签名位数**；只给密钥 128 位与"64 位指针顶部填充位"两处载体描述 | §6.3，§16.2 |
| "开了 PAC 就不需要 ASLR" | 两者拦的是不同属性（可否预知 vs 可否篡改），且关闭路径不同 | §6.1、§6.3 |
| "PAC 只保护返回地址" | 官方表列出 14 类项目，含函数指针、block、Objective-C isa/super/方法缓存、C++ 虚表条目与指针、计算 Goto 标签、内核/用户线程状态 | §6.2 表 |
| "PAC 拦不住 ROP，因为返回地址仍会被签" | 手册逐字把 ROP 列为被提高难度的攻击；且返回地址以存储地址加盐（= 函数入口 SP），跨栈帧搬运会认证失败 | §6.2、§6.3、§6.6 |
| "C++ 虚表不受 PAC 保护" | 官方表逐行列出"虚函数表条目 (IA)"与"虚函数表指针 (DA)" | §6.2 表 |
| "虚表指针和条目同等强度" | 指针键的 extra data 恒为 0（无位置绑定），条目则绑存储地址 + 哈希值 | §6.3、§6.7 第 3 条 |
| "arm64e 是稳定 ABI，可按文档长期依赖" | Apple 逐字：ptrauth V0 是 "a preview ABI"，"future OSes may break ABI" | §6.5 |
| "iOS 与 macOS 的 arm64e ABI 上限相同" | `MAX_PREFERRED_VERSION`：macOS = V1，非 macOS = V0 | §6.5 |
| "进程关掉指针认证就全都不生效" | `asib`（IB，含返回地址）逐字 "enforced even in processes which disable ABI pointer authentication"；其余三键为 no-op | §6.3 |
| "第三方 arm64e 在 macOS 上会被静默降级运行" | 实际是**拒载**并给出 `OS_REASON_EXEC / EXEC_EXIT_REASON_BAD_MACHO`，除非 platform binary 或 boot-arg 放行 | §6.5 |
| "按芯片型号判断特性可用性即可" | 源码注释逐字 "you should never try to deduce whether or not some feature is available based on the family" | §6.8 |
| "`-mbranch-protection` 的取值是 Apple 文档口径" | Apple 一手页与四仓对该选项 **0 命中**；可用口径只有 Xcode 设置名与内核构建变量 | §6.4 |
| "整个共享缓存只有一个 slide，泄露一处即全线泄露" | arm64e **有独立 shared region**，且缓存选择可按 team-id 或 entitlement 定制 → 泄露面是分区问题 | §6.5、§2.3 |
| "PAC 有 Apple 公布的失败签名/异常码" | 手册逐字止于"会导致中止使用签名"；崩溃侧对应关系是本篇的解释框架，非官方承诺 | §6.6、§13.2 |

# 7. 执行目的约束：分支目标、不可执行页与即时编译

## 7.1 分支目标识别的一手口径与检索结果

**先登记一条负面结论**（A 级检索，检索台账见附录 A.4）：对《Apple 平台安全保护》网页版全部 **179 个页面**做大小写不敏感全文检索，`BTI` 与 `branch target` **命中 0 页**（同批 0 命中还有 `GCS`、`shadow stack`、`KASLR`、`slide`、`guard`、`malloc`、`zone`、`canary`）。该结论在页面发布日期最新为 2026-01-28、文稿修订记录页最新条目为"2026 年 3 月"的版本上独立复核成立。

因此正文口径必须是："**Apple 公开手册（含 2026 年 3 月修订）未收录 BTI 这一术语**"，而**不能**写成"Apple 未实现 / 未启用 BTI"。**"手册未收录"与"平台未实现"是两件事**，本篇在 §16.3 把这条区分列为读者最容易越界的一处。

一手证据在源码侧（A- 级）。xnu 的构建变量文件 `makedefs/MakeInc.def:55-66` 逐字：

> ARM64_EXTRA_BUILD_FLAGS =
>
> # Enable BTI by default for all ARM64 targets
> BTI_BUILD = 1
>
> # -mkernel contains BTI by default
> ifeq ($(BTI_BUILD),1)
> 	ARM64_EXTRA_BUILD_FLAGS += -DXNU_BUILT_WITH_BTI
> else
> 	ARM64_EXTRA_BUILD_FLAGS += -fno-branch-target-identification -UXNU_BUILT_WITH_BTI
> endif

这是本轮找到的 `-fno-branch-target-identification` 与"BTI 默认开启"**唯一的一手出处**（构建变量文件，不是文档页）。逐字结论有两句：**"Enable BTI by default for all ARM64 targets"**、**"-mkernel contains BTI by default"**。

**强制粒度是页表位**。`arm_vm_init.c:988-990` 逐字：

>  * unsigned int guarded => flag indicating whether this range should be
>  * considered an ARM "guarded" page. This enables BTI enforcement for a region.

`arm_vm_init.c:1075-1082` 逐字：

> #if BTI_ENFORCED
> 				/*
> 				 * Set the 'guarded page' flag to enable ARM BTI enforcement.
> 				 */
> 				if (guarded) {
> 					ptmp |= ARM_PTE_GP;
> 				}
> #endif /* BTI_ENFORCED */

另有两处细节可佐证 BTI 已进入 ABI 层：`pmap_internal.h:94` 逐字 `#define PMAP_SUPPORT_PROTOTYPES_BTI_LANDING_PAD "bti c\n"`（原型函数插入着陆垫），`arm64/alternate_debugger_asm.s:61` 逐字 `/* BTI calling convention requires tail calls use x16/x17 */`（BTI 调用约定）。

**一处官方承认的覆盖空洞**（本系列最有价值的"缺口"句之一，`arm_vm_init.c:1244-1252` 逐字）：

> 		/* 
> 		 * We cannot mark auxKC text as guarded because doing so would enforce
> 		 * BTI on oblivious third-party kexts and break ABI compatibility. 
> 		 * Doing this defeats the purpose of BTI (branches to these pages are 
> 		 * unchecked!) but given both the relative rarity and the diversity of
> 		 * third-party kexts, we expect that this is likely impractical to
> 		 * exploit in practice.
> 		 */

引用纪律：**必须连同后半的风险评估一起引**，不可只留 "branches to these pages are unchecked!"。这段文字的结构是"为了 ABI 兼容性 → 有意不设防 → 承认这 defeats the purpose of BTI → 用稀有性与多样性论证实际不可利用"，是"缓解措施存在官方承认的空洞 + 官方以实用性论证可接受"的模范样本。同时注意适用范围：**auxKC 是 macOS 内核扩展概念**，iOS 上不存在第三方 kext，**不得**据此声称 iOS 有同一空洞（§12.2）。

**能力检测口径**：`arm_features.inc` 逐字含 `FEAT_BTI`；用户态经 `hw.optional.arm.FEAT_BTI` sysctl（`CTLFLAG_RD | CTLFLAG_KERN | CTLFLAG_LOCKED`）暴露，枚举来源是 `commpage.c:632-646` 对 `ID_AA64PFR1_EL1` 的 `ID_AA64PFR1_EL1_BT_MASK >= ID_AA64PFR1_EL1_BT_EN` 判定；另有位号 `CAP_BIT_FEAT_BTI 36`。**一处须写精确的差异**：BTI 有 `CAP_BIT_FEAT_BTI`，但 commpage 的 `kHasFeat*` 位图**未**给出对应 `kHasFeatBTI`（`grep -i "mte|memtag|gcs"` 于这两个头文件命中 0）——正文应写"BTI 经 sysctl / `cpu_capabilities` 位暴露，未在 kHasFeat 位图中出现"，不要把两条通道合并。

**术语冲突（登记入 §16.1）**：源码用 **"guarded page"** 指 BTI 强制页（`ARM_PTE_GP`），而本篇 §3.3 讨论的 **保护页（guard page）** 是"不可读写的哨兵页"，§3.4 的 libmalloc **guarded range** 又是第三种含义（随机化偏移的保留区间）。同一个英文词在同一份代码库里承载三个互不相干的机制，正文每次使用都必须当场定义。

**GCS（Guarded Control Stack）**：手册 0 命中，且 xnu、dyld、libmalloc、security 四仓 main tip 中 `GCS`（独立词）与 `guarded control stack` 命中 0（唯一 `GCs` 命中是 `bsd/sys/vadvise.h:76` 的 "Suitable during GCs in LISP"，无关）。**结论：不得把 GCS 归入 iOS 现有机制**（§16.2）。

## 7.2 不可执行页与"数据不能变代码"这条底线

手册对这条底线的唯一逐字表述在运行时进程安全性页（A 级，`sec15bfe098e`，2024-12-19，Subhead「Execute Never 功能」首句）：

> iOS 和 iPadOS 使用 ARM 的 Execute Never (XN) 功能提供进一步的保护，该功能会将内存页标记为不可执行。

三点口径：

- **XN 在全库只出现于这一页**，`不可执行` 命中 **1 页**（同页）。这是"数据页不可执行"门槛的唯一一手出处——它比随机化更"薄"：一条段落，无量化，无字段。
- 该段**逐字只写 "iOS 和 iPadOS"**，未提 visionOS / macOS / watchOS / tvOS。跨平台外推属**未核实**（§12）。
- 手册没有把 XN 与"堆溢出直接执行 shellcode"这类具体攻击名相连（与 PAC 页点名 ROP 形成对比，§6.6）。

构建侧与内核侧对此有可核对的位（A- 级）。`mach_loader.c:779-783` 逐字：

> 	/* Forcibly disallow execution from data pages on even if the arch
> 	 * normally permits it. */
> 	if ((header->flags & MH_NO_HEAP_EXECUTION) && !(imgp->ip_flags & IMGPF_ALLOW_DATA_EXEC)) {
> 		vm_map_disallow_data_exec(map);
> 	}

含义链条值得完整写出：**`MH_NO_HEAP_EXECUTION` 是构建期由链接器写进 Mach-O header 的位，内核在加载时读取它并据此关闭整个地址空间的数据页执行权限**——这是"构建侧决定运行时防护"最直白的一手证据。而它可以被 `IMGPF_ALLOW_DATA_EXEC` 推翻，该标志来自 `posix_spawn` 的 `_POSIX_SPAWN_ALLOW_DATA_EXEC`；`kern_exec.c:6764-6768` 又对特定进程清除该豁免。口径：**"能不能执行数据页"是一个在 exec 路径上被三层条件反复修正的属性，不是全局常量**（§10.4、§11.1）。

页校验侧同样产出执行权限判定（A- 级）：`ubc_subr.c:6011-6018` 的函数签名逐字为

> cs_validate_page(struct vnode *vp, memory_object_t pager, memory_object_offset_t page_offset, const void *data, int *validated_p, int *tainted_p, int *nx_p)

出参含 **`nx_p`**——即"这一页是否不可执行"是**载入时校验的产物之一**。这把 §7.2（执行权限）与 §10.1（页级校验）在实现层连成一件事，正文应写成"执行权限判定发生在页载入路径上"，而不是把 XN 与签名校验列为两个互不相干的门槛。

## 7.3 可执行内存申请及其例外通道

手册在 XN 段后半给出了**可写且可执行（W+X）内存的例外条件**，这是本篇最重要的一段逐字文本（A 级，同页）：

> 只有处于严格的控制条件下，App 才能使用标记为可写入和可执行的内存页：内核会检查 Apple 专有的动态代码签名授权是否存在。即使如此，也只有单个 mmap 调用能用于请求一个可执行且可写入的内存页，系统为可执行且可写入的内存页分配了随机地址。Safari 浏览器对其 JavaScript 即时 (JIT) 编译器使用了此功能。

逐字可拆出**三道并列条件**，缺一即不成立：

| # | 逐字条件 | 性质 | 对利用难度的含义 |
|---|---|---|---|
| ① | "内核会检查 **Apple 专有的动态代码签名授权**是否存在" | 授权门控（对象是**动态代码签名授权 = JIT**，不是 PAC） | W+X 不是可自行申请的属性，而是签名期被授予的例外 |
| ② | "只有**单个 mmap 调用**能用于请求一个可执行且可写入的内存页" | 数量约束 | 例外区是**一块**，不是任意多处 → 可写的执行代码区被集中管理 |
| ③ | "系统为可执行且可写入的内存页分配了**随机地址**" | 随机化 | **JIT 页位置本身也被随机化**的一手证据（§3.5） |

第 ③ 点是全篇少数"随机化被写进另一道门槛的例外条款里"的句子：**即使走 W+X 例外，位置仍不可预知**。第 ①② 点则说明这条例外**同时被授权与数量收窄**。末句把 **Safari JIT 点名为该例外的使用者**，在官方文本里直接把"JIT 需要 W+X"与"JIT 是攻击面"连上，衔接 §7.4。

**硬件层如何落实这条底线**（A 级，`sec8b776536b` Subhead「快速权限访问限制」逐字）：

> 自 Apple A11 仿生和 S3 后的 SoC 新增了硬件原语。此"快速权限访问限制"原语中包括可基于每个线程快速限制访问权限的 CPU 寄存器。通过快速权限访问限制（也称为 APRR 寄存器），支持的操作系统可从内存移除执行权限，无需通过系统调用和页表寻访或刷新。这些寄存器更进一步减少了来自网页的攻击，尤其是经过运行时编译（即时编译）的代码，因为内存无法在读写的同时有效执行。

拆解：

- **"因为内存无法在读写的同时有效执行"** 是手册中最接近 **W^X 定义**的逐字表述，且明确是**硬件保证**、**基于每个线程**。它不是"内核禁止申请 W+X"这种策略描述，而是"硬件上读写与执行不能同时有效"这种能力描述——两者强度差别很大。
- 收益被官方直接写成**减少来自网页的攻击、尤其是即时编译代码** → 与 §7.3 的 Safari JIT、§7.4 的锁定模式网页限制构成同一条证据链。
- 硬件范围句逐字：**"自 Apple A11 仿生和 S3 后的 SoC"**；矩阵表中 A11、S3 起可用、A10 不可用（§12.1）。
- 措辞纪律：本页用 **"即时编译"**，`JIT` 缩写全库仅命中 2 页（`sec15bfe098e`、`secebb113be1`）。

**macOS 侧的例外通道**（A 级，Hardened Runtime Runtime Exceptions，六项逐字）：`com.apple.security.cs.allow-jit`、`com.apple.security.cs.allow-unsigned-executable-memory`、`com.apple.security.cs.allow-dyld-environment-variables`、`com.apple.security.cs.disable-library-validation`、`com.apple.security.cs.disable-executable-page-protection`、`com.apple.security.cs.debugger`。与本节相关的前两项，官方 Abstract 逐字：

> A Boolean value that indicates whether the app may create writable and executable memory using the `MAP_JIT` flag.
> You can create memory that's both writable and executable by passing the `MAP_JIT` flag to the `mmap()` system function. The Hardened Runtime disallows this by default, because it creates a security risk.

> A Boolean value that indicates whether the app may create writable and executable memory without the restrictions imposed by using the `MAP_JIT` flag.
> [aside] Including this entitlement exposes your app to common vulnerabilities in memory-unsafe code languages. Carefully consider whether your app needs this exception.

三条硬口径：**① 这六项全部是 macOS-only**（TOPIC 归类即"Runtime Exceptions"，属 macOS Hardened Runtime 模型），**不得**移植成 iOS 的评审项；**② Apple 自己逐字定性** "because it creates a security risk" 与 "exposes your app to common vulnerabilities in memory-unsafe code languages" —— 这是"配置审计可判负"的官方依据；**③ `MAP_JIT` 与"绕过 MAP_JIT 限制"是两个不同强度**：前者仍受 JIT 写保护机制约束，后者直接取消限制。TN3126 还给出**架构分叉**（逐字）：

> The Disable Executable Memory Protection Entitlement only has this effect on Intel code. For Apple silicon code, this entitlement leaves page protection enabled, making it equivalent to the Allow Unsigned Executable Memory Entitlement.

即同一个 entitlement 在 Intel 与 Apple 芯片上**后果不同**。凡按 entitlement 名字判断"该 App 关掉了页保护"的评审脚本，在 Apple 芯片上会得出错误结论（§15.4）。

TN3126 对整族例外的评价逐字只有一句，值得原样放进加固清单：**"Don't do that!"**（针对 `Disable Executable Memory Protection Entitlement`）。

## 7.4 即时编译、锁定模式与降级运行

锁定模式是本篇里"用户可主动收窄执行面"的唯一一手通道。**手册页（A 级，`sec2437264f0`，2024-05-07）不含任何 WebKit/JIT 具体限制句**，只在受影响类目清单中逐字列出 "WebKit" 与 "Safari 浏览器"；真正的限制句在支持文章《关于"锁定模式"》（A 级，`support.apple.com/zh-cn/105120`，页面标注 2026-09-18），逐字：

> 网页浏览：某些复杂的网页技术会被阻止，这可能会导致一些网站载入速度变慢或者无法正常运行。此外，网页字体可能无法显示，图像可能会被图像缺失图标替代。

> 信息：除某些图像、视频和音频外，大多数信息附件类型都会被阻止。一些功能（如链接和链接预览）无法使用。

**措辞纪律（本节最重要的一条）**：官方**没有**逐字使用 "JIT"、"即时编译"或 "W^X"，用的是 **"某些复杂的网页技术会被阻止"**。因此：

- 正文**不得**把"锁定模式禁用 JIT"写成 Apple 口径。可写出的最强形式是**标注为推论的三句链条**：手册逐字确认 Safari JIT 使用 W+X 例外页（`sec15bfe098e`）；手册逐字确认 APRR 使"内存无法在读写的同时有效执行"并点名即时编译代码（`sec8b776536b`）；支持文章逐字确认锁定模式阻止"某些复杂的网页技术"，且把限制归因为 **"WebKit 相关限制"**（该逐字短语出现在"排除某个网站"段落，即实现层是 WebKit）。**链条中"复杂的网页技术＝JIT"这一步 Apple 未逐字定义，属缺口**（§16.2）。105120 页**未给**被阻止技术的清单。
- **失效路径可逐字写**：> 当你在 Safari 浏览器中排除某个网站后，相关网站将不再受 WebKit 相关限制的约束与影响。以及"排除某个 App，以便移除这个 App 的网页内容限制"；官方自陈 > 排除某个网站、App 或联系人会削弱锁定模式的保护效果，可能提升你的安全风险。**"用户可以用两次点击撤掉这道收窄"是治理篇必须写进去的事实**（§11.2）。

**降级运行**（执行面与性能之间的官方交换，A 级，`allow-jit` Discussion 逐字）：

> Without the [Allow execution of JIT-compiled code entitlement], frameworks that rely on just-in-time (JIT) compilation may fall back to an interpreter. Other code using JIT compilation may crash or behave in unexpected ways.

> Digital rights management (DRM) solutions that currently use unsigned executable memory should instead change to using the `MAP_JIT` flag and the entitlement.

两条口径：① 防护收窄后的结果是**退回解释器**或崩溃 —— 即"执行面收窄"在真实产品里是有成本的，这是为什么评审不能只写"必须开/必须关"，而要写出**降级后的行为差异**（§15.3 第 12 组）。② Apple 明确指引 DRM 类实现从"未签名可执行内存"**迁移到 `MAP_JIT` + entitlement**——即官方承认存在遗留的"不用例外的例外"路径，并给出收敛方向。

## 7.5 三者组合后剩下的可用跳板类型（描述性，不给步骤）

把随机化（第 2–5 章）、指针认证（第 6 章）、执行目的约束（本章）叠加之后，一手文本**自身**就指出了哪些位置仍然可达。本节只做"官方承认的覆盖面边界"的清点，**不含任何操作方法、不含任何针对具体目标的步骤**；其用途是加固评审时确认"哪一类风险不由这三道门槛负责"。

1. **已签名只执行页中的既有代码**。BTI 约束"能不能跳进来"、PAC 约束"这个指针有没有被改"，两者都不改变"合法代码本身仍然在那里"这一事实。§7.1 的 auxKC 注释逐字承认在特定区域 "branches to these pages are unchecked!"——这是官方文本内**唯一**一处直接描述"未受 BTI 检查的代码区域"的表述，且仅限 macOS 第三方 kext 语境。
2. **未被签名的指针类别**。§6.2 逐字：block 对象指针本身不签名；§6.2 末句逐字：其余指针"use private ABI rules"。落在这两类里的间接跳转载体，其完整性**不由 PAC 负责**（§6.7）。
3. **无 discriminator 的签名值**。函数指针与 C++ 虚表指针的 extra data 官方定为恒为 0（§6.3）：**同一合法签名值可跨位置复用**，这是 ABI 定义的直接后果而非实现缺陷。
4. **W+X 例外区本身**。§7.3 逐字把它收窄为"单个 mmap 调用"+"随机地址"，但它是一块**既可写又可执行**的区域；在该区域内，"数据不能变代码"这条底线不成立。手册对该区域给出的保护是**位置随机化 + 授权门控**，而不是禁止。
5. **解释执行路径**。§7.4 表明收窄 JIT 的后果是"退回解释器"。解释器把"执行任意代码"变成"驱动一个合法的字节码/AST 求值循环"，PAC 与 BTI 对这类**数据驱动的控制流**均无表态（Apple 一手文本未讨论该路径，属缺口而非承诺）。
6. **版本内恒定的偏移**。随机化改的是基址，不是镜像内部结构（§13.5）。"地址 − 镜像基址"跨设备恒定这一观测事实（§5.5）意味着：**任何已泄露的基址都会一次性还原整张布局**——这正是 §4.2 引的官方博客逐字句 "leaking one pointer value gives the attacker the location of a great number of interesting objects" 在用户态镜像上的等价形式。
7. **签名完好但语义错误的对象**。§6.7 第 2 条：通过认证的指针被用在错误的位置上，认证不会失败；这一类由 §9.5（类型化分配）与 §10.3（库校验）而非 PAC 负责。

**评估结论（本节唯一需要背下来的句子）**：这三道门槛叠加后，剩余可达面**几乎全部是"合法代码的合法用法"**——不是"未受保护的角落"，而是"由其他机制（类型检查、库校验、沙盒、代码质量）负责的角落"。所以 §15.1 的判定链不把"PAC/BTI/XN 全开"当作终点，而把"该进程还能加载谁的代码、还能执行什么类型的数据"作为后续判定项。

# 8. 内存标记与边界检查

## 8.1 内存标记扩展在官方文本中的位置

内存标记扩展（MTE）是**手册全库唯一命中的 ARM 新特性缩写**（对照：`BTI`、`GCS`、`shadow stack` 均 0 命中，§7.1）。它在三处一手文本中出现，位置不同、语气不同：

**（1）操作系统完整性页的三个 Subhead**（A 级，`sec8b776536b`，2026-01-28）。逐字第一段：

> Arm 在 2019 年发布了内存标记扩展 (MTE) 规范，作为硬件协助查找内存损坏错误的工具。MTE 是一种内存标记和标记检查系统，每一次内存分配都会用一个密钥进行标记。硬件保证了仅在后续内存访问请求中包含正确密钥时，才会向该请求授予许可。如果密钥不匹配，App 会崩溃且该事件会被记录。这可让开发者在内存损坏错误发生时立即识别。

**（2）"增强型内存标记扩展"与"内存完整性强制"两段**（同页，逐字）：

> EMTE 修复了导致 MTE 无法成为主动防护手段的漏洞，包括仅支持更安全的同步模式。此外，从已标记内存区域访问未标记内存需要已知该区域的标记，因此对于攻击者来说，通过直接修改未标记分配将动态标记内存中的越界错误作为一种规避 EMTE 手段的难度大大增加。

> 内存完整性强制 (MIE) 是用于 Apple 平台的全面内存安全防护技术，在 A19 和 M5 及后续处理器上提供。MIE 基于 Apple 安全内存分配器提供的坚实基础构建，与同步模式下的增强型内存标记扩展 (EMTE) 相结合，且受综合标记保密性强制策略支持。MIE 内建于 Apple 芯片，为内核等关键攻击面提供了无与伦比且始终在线的内存安全保护，同时保持了用户期望的处理能力和性能。

> 标记保密性强制会保护安全分配器实施免遭技术层面威胁，并保证 EMTE 标记的保密性，包括抵御侧信道和推测执行攻击。安全页表监控器会保护内核分配器后备存储区和标记存储区。系统还会确保当内核代表应用程序访问内存时，会受到与用户空间相同的标记检查规则约束。标记保密性强制还旨在减少由时序或推测攻击引起的标记泄露，甚至包括抵御 Spectre V1 的措施。

**（3）Apple 安全研究博客专文**（A 级，`security.apple.com/blog/memory-integrity-enforcement`，日期逐字 **September 9, 2025**，署名 "Posted by Apple Security Engineering and Architecture (SEAR)"）。开篇定性逐字：

> Memory Integrity Enforcement (MIE) is the culmination of an unprecedented design and engineering effort, spanning half a decade, that combines the unique strengths of Apple silicon hardware with our advanced operating system security to provide industry-first, always-on memory safety protection across our devices — without compromising our best-in-class device performance. We believe Memory Integrity Enforcement represents the most significant upgrade to memory safety in the history of consumer operating systems.

该页同时是**手册 MTE 段落的英文对译源**（"every memory allocation is tagged with a secret; … If the secrets don't match, the app crashes, and the event is logged."），也是手册末句"有关更多信息，请参阅 Apple Security Research 博客中的 Memory Integrity Enforcement…"的指向目标。

**（4）支持范围**（A 级矩阵 + 博客互证）：MTE/EMTE/MIE 行的判定为 **A19/M5 起**（手册逐字"在 A19 和 M5 及后续处理器上提供"；矩阵该行仅 A19/M5 列为支持图标，其余六列为不支持）。博客的设备口径逐字为 "MIE is built right into Apple hardware and software in **all models of iPhone 17 and iPhone Air**"。**注意两套表述分别按 SoC 与按机型**，正文不要把它们互相改写。

## 8.2 分配时打标记、访问时校验：它能拦住的错误类型

机制本身可拆成三步，且每一步都有逐字依据：

| 步骤 | 官方逐字 | 强度来源 |
|---|---|---|
| 分配时打标记 | "每一次内存分配都会用一个密钥进行标记" / "every memory allocation is tagged with a secret" | 密钥保密（"标记保密性强制…保证 EMTE 标记的保密性"） |
| 访问时校验 | "硬件保证了仅在后续内存访问请求中包含正确密钥时，才会向该请求授予许可" | **硬件**保证，不是软件约定 |
| 不匹配的后果 | "如果密钥不匹配，App 会崩溃且该事件会被记录" / "这可让开发者在内存损坏错误发生时立即识别" | 崩溃（阻断）+ 记录（可观测） |

**它拦的是哪一类错误**：手册把 MTE 定位为"硬件协助**查找**内存损坏错误的工具"，博客把它定位为 "a tool for hardware to help **find** memory corruption bugs"。也就是说，**规范级的 MTE 被官方描述为检测工具**，而非防护工具。真正把语气改成防护的是 Apple 自己的一句话（A 级手册逐字）：

> EMTE 修复了**导致 MTE 无法成为主动防护手段**的漏洞，包括仅支持更安全的同步模式。

**这是手册里罕见的一处：Apple 直接评价一项 ARM 标准的局限性。** 配套的两句使这个评价可被核验：

- 异步报告的问题（A 级博客逐字）：> Consider that MTE can be configured to report memory corruption either synchronously or asynchronously. In the latter mode, memory corruption doesn't immediately raise an exception, leaving a race window open for attackers. We would not implement such a mechanism. We believe memory safety protections need to be **strictly synchronous, on by default, and working continuously**.
- 未标记内存的问题（A 级博客逐字）：> A key weakness of the original MTE specification is that access to non-tagged memory, such as global variables, is not checked by the hardware. This means attackers don't have to face as many defensive constraints when attempting to control core application configuration and state.

由此可写出本篇对"标记类门槛"的分层结论：**MTE = 检测；EMTE + 安全分配器 + 标记保密性强制 = 强制（MIE）**。凡把两者混写成"iOS 有内存标记所以越界会被拦"的文章，都抹掉了 Apple 自己给出的这条演进与差别。

**"检查"的粒度是 granule，不是字节**（A- 级源码逐字，`xnu/san/memory/kasan.c:76-84`——这是软件仿真版，但给出官方认可的粒度与成本口径）：

>  * For each 16-byte granule in the address space, one byte is reserved in the
>  * shadow table. TBI (Top Byte Ignore) is used to associate a tag to each
>  * VA pointer and to match it with the shadow table backing storage. This
>  * mode of operation is similar to hardware memory tagging solutions (e.g. MTE)
>  * and is not available on x86-64. Cost: ~8% of memory. No need for redzones
>  * or quarantines. See kasan-tbi.c for details.

**一处必须守住的量化纪律**：一手来源里关于标记宽度的数字，只有这段**软件仿真**的 "16-byte granule / 1 byte shadow / Cost: ~8% of memory / not available on x86-64"。手册与博客**均未给出 EMTE 硬件标签的位数或色数**。因此正文**不得**写"4 位标签 / 16 种颜色"，也不得把 KASAN-TBI 的 8% 内存成本当作硬件 MIE 的成本（§16.2）。

## 8.3 它与读原语、逻辑缺陷无关这一点

内存标记检查的是"**这次访问用的标签，配不配得上这块内存的标签**"。三类问题因此天然不在它的射程内，且每一类都有一手文本支撑：

1. **分配内部的越界（intra-allocation）**。博客逐字：> Although some issues are able to survive MIE — for example, **intra-allocation buffer overflows** — such issues are extremely rare, and even fewer will lend themselves to a full end-to-end exploit. 标签按 **granule** 粒度绑定，一个对象内部的偏移若落在同一标签区间内，检查不会失败。Apple 把这类问题**逐字承认可以在 MIE 下存活**，这是"标记类门槛的官方边界"，正文应引用而不是淡化。
2. **读原语与信息泄露**。标记检查发生在**访问**时并要求标签匹配；一次合法的读（正确标签）不会失败，而标记值本身一旦泄露，"布局不可知"就退化成"标记不可知"——这正是 §5.3 里 PAC 侧信道那句话的同类问题（逐字见下条）。
3. **逻辑缺陷与鉴权绕过**。手册与博客从未把 MTE/MIE 描述为对"程序语义正确性"的保护。§1.3 的"缓解 ≠ 修复"在这里最直白：**一个被 MIE 完全覆盖的进程，仍可以因一个类型安全的逻辑错误被完全接管。**

**标记保密性为什么是必要条件**（A 级博客逐字，同段还给出 PAC 的官方失效条件）：

> For example, evaluating a pointer authentication instruction speculatively exposed timing differences in our original implementation of Pointer Authentication Codes (PAC), which would allow the valid signature to be isolated. During the design phase for Memory Integrity Enforcement, we identified and addressed the three speculative vulnerabilities that could undermine tag confidentiality.
> First, when EMTE is active, requests to access memory cause the hardware to check tags. It's crucial that evaluating a tag-checking instruction speculatively doesn't expose timing differences that would allow an attacker to isolate the valid tag.

这三句是本篇**"缓解措施自身被攻破并官方修复"的最强证据**，且它同时覆盖 PAC：官方承认**原实现中投机执行 PAC 指令暴露了时序差异，可用于隔离出合法签名**。任何把 PAC 写成"不可被侧信道探测"的表述都与这段一手文本冲突（§16.5）。

**内核代表 App 访问内存时也受同一规则约束**（A 级手册逐字）："系统还会确保当内核代表应用程序访问内存时，会受到与用户空间相同的标记检查规则约束。"——这句话排除了一类常见绕过思路（"借内核之手做未标记访问"），是"标记门槛的覆盖面包含内核代理路径"的唯一一手表述。

## 8.4 覆盖范围、编译要求与性能口径

**覆盖范围**：手册把 MIE 的对象写成"为**内核等关键攻击面**提供了无与伦比且**始终在线**的内存安全保护"——注意"始终在线"（always-on）+ "关键攻击面"两个限定；逐字未声明"全部用户态代码"。设备侧下限见 §8.1（A19/M5、iPhone 17 / iPhone Air 全系）。**因此 MIE 的覆盖差是双重合取：既受 SoC 世代限制，也受"关键攻击面"这一措辞限制**（§12.1、§16.6）。

**编译/开发侧入口**：一手文本里 MTE/EMTE **没有任何 developer 文档页**（A- 级检索：`arm_features.inc` 无 `MTE`/`TAG`/`MEMTAG` 条目，`cpu_capabilities.h` 的 `kHasFeat*` 位图无对应位，grep 命中 0）。开发者侧唯一逐字入口来自博客（A 级）：

> Notably, this includes making sure that this powerful new protection is available to third-party apps that are likely entry points for attackers — such as social networks, messaging apps, or any other app where a specific user can be targeted. Starting immediately with the launch of MIE, **any developer can begin testing this powerful protection for their app, including EMTE on hardware that supports it, using the Enhanced Security settings in Xcode.**

这一句把 §6.4 的 `ENABLE_ENHANCED_SECURITY` 与 MIE/EMTE 的开发者入口对上（"博客 ↔ Xcode 文档"互证），但**博客没有写出具体构建项名**，所以正文只能写到"Enhanced Security settings"这一层，不得臆造设置名。同一句还是**"第三方 App 可自愿启用"的一手依据**——它恰好补上了 §6.1 里"PAC 页主语限于系统软件和内建 App"的那处空缺：**Apple 的说法是"可开始测试"，而不是"已默认覆盖第三方 App"**。

**性能口径**：一手文本给的全是定性句，且方向一致：

- 承认有成本（A 级博客逐字）：> Because EMTE tag checking imposes a performance cost, we designed Memory Integrity Enforcement to take advantage of our secure allocators first and use **EMTE to protect only smaller individual allocations within a type bucket**, which software allocators can't defend on their own.
- 分配器侧声称无退化（A 级博客逐字）：> Our secure allocators set a new high-water mark of software protection against memory corruption, while preserving the same or better performance as the allocators they replaced.
- 手册侧（A 级逐字）："同时保持了用户期望的处理能力和性能"。

**由此得到一条设计口径（正文值得写）**：MIE 的分工是**软件安全分配器打底、硬件标记只补"分配器防不住的小对象"**（逐字 "use EMTE to protect only smaller individual allocations within a type bucket"）。所以"硬件标记 vs 分配器加固"不是二选一，而是**按对象尺寸分层的组合**——这也解释了 §9 为什么要单列一章讲分配器（§8.5）。

**量化缺口**：三篇博客与手册均未给出 MIE 的 CPU 开销百分比、EMTE 标签位数、覆盖率数字。正文凡需要数字，只能用 §4.2/§9.3 的 8% 与 194/119/90（并加样本限定），或 §8.2 的 KASAN-TBI 成本（并加"软件仿真"限定），其余一律写为缺口（§16.2、§17.2）。

## 8.5 与其它门槛的重叠和互补

| 门槛 | 检查的不变式 | 拦得住 | 拦不住 | 与标记门槛的关系 |
|---|---|---|---|---|
| 地址随机化（§2–5） | 位置不可预知 | 跨设备复用偏移、直接硬编码地址 | 一次指针泄露、堆喷压回稳定布局 | 被官方评为 fragile（§5.1 逐字）→ 由分配器与标记接替 |
| 指针认证（§6） | 指针值未被篡改 | 改返回地址、改函数/虚表指针、搬运动签名值 | 未签名指针类别、extra data 为 0 的可搬运签名、读原语 | **被官方承认存在投机侧信道**（§8.3）→ 由标记保密性强制补 |
| 执行目的约束（§7） | 跳入点合法 / 数据不可执行 | 盲目跳转、数据页执行 | 合法代码内的既有跳板、W+X 例外区自身 | 与标记无重叠，正交 |
| 分配器加固（§9） | 对象类型与区域一致 | 跨类型占位、错误区释放（strict free） | 同类型 UAF、签名碰撞 | **MIE 的基座**（逐字 "基于 Apple 安全内存分配器提供的坚实基础构建"） |
| 内存标记 / MIE（本章） | 访问所用标签与内存标签一致 | 越界访问已标记区域、访问未标记区域（EMTE 起） | 分配内部越界、逻辑缺陷 | 覆盖"分配器防不住的小对象" |
| 页表与执行完整性（KIP/PPL/SPTM+TXM，§10.5） | 页表与代码完整性不可被（哪怕被入侵的）内核破坏 | 内核被控后改页表 / 改已校验的用户代码页 | 内核权限内的合法操作 | SPTM **保护内核分配器后备存储区与标记存储区**（§8.2 逐字）→ 是标记门槛的前提 |

三条互补关系值得单独成句：

1. **标记门槛依赖页表门槛提供前提**。逐字："安全页表监控器会保护内核分配器后备存储区和标记存储区。"——即标记的保密性不是自证的，它由 SPTM 兜底。**移除 SPTM（低世代芯片）时，"标记保密性强制"这一层不成立**，这是"某道门槛的强度来自另一道门槛"的直接一手例子。
2. **标记门槛是随机化门槛的替代路线，而非加强版**。Apple 的官方叙事从"布局不可知"转向"**即使知道布局也无法完成利用**"（§5.1/§5.2 逐字），博客演进链逐字给出顺序：iOS 15 `kalloc_type` → iOS 17 用户态 `xzone malloc` → 2018 A12 起 PAC → 2019 Arm MTE → 2022 EMTE → 2025-09 MIE。正文应据此把本篇标题的因果改写清楚：**"随机化提升难度"是上一代口径，"检查/强制提升难度"是现役口径，两者在同一设备上并存但强度来源不同。**
3. **PAC 与标记检查在"投机执行"这一维度上同构脆弱**。§8.3 逐字句同时谈到 PAC 的时序差异与 tag-checking 的时序差异，并把二者放在同一个设计阶段里修复。这是"同一类硬件副作用（侧信道）击穿两类不同门槛"的官方表述，也是 §1.4"这张表不能回答的问题"里必须列出的一条。

# 9. 分配器与元数据加固

## 9.1 堆元数据保护的目标形态

**先划一手文本的边界**：手册全库对 `malloc`、`zone`、`guard page`、`canary` 全部 **0 命中**（§5.5、附录 A.4）。也就是说，**《Apple 平台安全保护》不描述用户态分配器的任何实现细节**；可引的官方措辞只有两处，且都不是"分配器文档"：

1. **堆数据与元数据分离 + 双重释放检测**（A 级，`sec30d8d9ec1`，2021-02-18，逐字，见 §3.7 上下文）：

> 堆利用（通过将堆数据和其元数据分开，以及准确检测诸如双重释放错误等错误状态）

2. **"Apple 安全内存分配器"作为既有基座**（A 级，`sec8b776536b`，2026-01-28，逐字两句）：

> MIE 基于 Apple 安全内存分配器提供的坚实基础构建

> 安全页表监控器会保护内核分配器后备存储区和标记存储区。

**手册没有描述这个"安全内存分配器"的分配随机化、隔离或校验细节**（§16.2）。它的存在与定位由博客补齐（A 级，`security.apple.com/blog/memory-integrity-enforcement`，2025-09-09，逐字）：

> In iOS 15, we introduced kalloc_type, a secure memory allocator for the kernel, followed in iOS 17 by its user-level counterpart, xzone malloc. These secure allocators take advantage of knowing the type — or purpose — of allocations so that memory can be organized in a way that makes exploiting most memory corruption vulnerabilities inherently difficult.

由此得到"目标形态"的一句话定义，且它逐字来自 Apple：**利用"知道分配的类型或用途"这一信息来组织内存，使得对大多数内存破坏漏洞的利用本质上变得困难**（"makes exploiting most memory corruption vulnerabilities inherently difficult"）。注意这句话的主语是**类型/用途知识**，不是随机性——这就是 §9.5 与 §9.3 必须分成两节的原因：**分配器加固与地址随机化是两种不同的强度来源**，前者靠"分区正确"，后者靠"位置不可知"。

时间线与层次逐字可引：内核侧 `kalloc_type`（iOS 15，2022-10-27 首次公开描述）→ 用户态 `xzone malloc`（iOS 17）→ MIE（2025-09，A19/M5）。三篇博客构成一条自述演进链，**这条链本身就是"随机化 → 强制"的口径转换证据**（§8.5）。

## 9.2 区校验、双重释放与非法写检测

**"准确检测双重释放"是手册级的唯一承诺**（§9.1 逐字 ①），但要注意它的语境：那句话描述的是 **iBoot 引导载入程序的编译工具链改造**，不是通用用户态分配器，且官方措辞带降级词"**可帮助防止以下类中的大多数漏洞**"。把它当作"iOS 用户态堆有双重释放检测"来引用，是本主题最常见的一处口径误用（§16.1）。

内核侧的"区校验"有明确的一手定义（A 级博客 `kalloc_type` 逐字）：

> Another benefit of randomized bucketing is that it maximizes the value of kalloc_type's **strict free semantics**. Strict free means that the `kfree_type()` callsite is only ever freeing addresses that belong to the correct zone, which makes it harder to transfer a UAF outside of the bucket containing the vulnerable type.

（原文逐字为 "Strict free means that the `kfree_type()` callsite is restricted to only ever freeing addresses that belong to the correct zone, which makes it harder to transfer a UAF outside of the bucket containing the vulnerable type. By randomizing the set of types that belong to a given zone, the space of invalid frees to the wrong type that won't be caught during normal use is much smaller."）

拆开这句可得三重含义，正文应逐条写：

- **"释放点被限制在正确 zone"** —— 这不是"检测到非法写"，而是**结构性禁止**：一个 `kfree_type()` 调用点在类型上只能释放属于该 zone 的地址。
- **它拦的是"把 UAF 转移出桶"** —— 即跨类型的占位/释放攻击；这与 §6.7 第 2 条（类型正确但语义错误）**恰好互补**：PAC 管指针完整性，strict free 管释放归属。
- **随机分桶让"正常使用时抓不到的错误释放"空间变小** —— 逐字 "the space of invalid frees to the wrong type that won't be caught during normal use is much smaller"。这句承认了**存在一类"正常使用时不会被发现"的非法释放**，而分桶只是缩小其空间，不是消除。

用户态侧可核对的是**保留区与哨兵**（A- 级源码字符串，非文档）：libmalloc 源码中出现的开关名逐字为 `MallocASLRForce`、`MallocASLRPrint`、`MallocLargeDisableASLR`、`MallocGuardEdges`、`MallocDoNotProtectPrelude`、`MallocDoNotProtectPostlude`、`MallocStackLogging` 系列、`GuardMalloc`。

**引用纪律（本节的硬约束）**：这些是**源码内的环境变量名，Apple 没有 developer 文档页逐个描述其语义**（Instruments 侧另有 Guard Malloc 条目，属工具文档，不等同于分配器承诺）。正文凡列出它们，必须标注"源码级、无公开文档页"，且**不得**据名字推断行为细节（`MallocDoNotProtectPrelude` / `Postlude` 只能说明"存在前后哨兵并被这些开关关闭"这一层，不能再往前）。

## 9.3 区域随机化与"分配可预测性"

本节有一手文本中最能支撑本篇标题的一段注释（A- 级，`libmalloc/src/vm.c:125-142` 逐字，含 rdar 编号）：

> // The "guarded range" is a synthetic VA range that we create in order to
> // randomize the distance of malloc metadata from the fixed mappings that
> // are laid out at process launch.
> //
> // We implement this by mapping a PROT_NONE region: the size of this region is
> // randomly chosen between 4M and 5M. Within that region, we then punch a 1M
> // carveout, by deallocating a random piece of the guarded region VA.
> // The address of this carveout is then passed as a hint in mvm_allocate_plat
> // (whenever the provided address is zero), to nudge the VM into serving the
> // requested mappings through the carveout.
> //
> // Specifically, this is aimed at randomizing the distance between the binary's
> // __DATA segment and the malloc metadata allocated at process launch, namely
> // the initial zone allocation. Moreover, by having a variable-length tail at
> // the end of the region, we also randomize the distance between __DATA and
> // the regions mapped after malloc's metadata.
> //
> // rdar://146578480

**这段注释是本篇"随机化的对象是距离/间隔，而不只是绝对基址"的官方原句**，也是 §0.5 术语纪律里"随机化位置 ≠ 随机化布局"的实现依据。实现方式同样值得写清楚：它**不是**给元址加一个随机 slide，而是**先建一段 `PROT_NONE` 的合成区间，再在其中随机打出一个 1MB 缺口，把这个缺口地址作为 hint 递给 VM**——即用"空洞位置"来推动 VM 把后续映射散布到不可预期的相对位置上。配套的取随机数实现逐字（`vm.c:117-123`）：

> mvm_random_page_aligned(uint32_t upper_bound)
> {
> 	uint32_t max_pages = upper_bound / PAGE_SIZE;
> 	uint32_t pages = arc4random_uniform(max_pages + 1);
> 	return pages * PAGE_SIZE;
> }

量化基线（全部 A- 级逐字，可直接进 §15.2）：

| 参数 | 值 | 出处 |
|---|---|---|
| guarded range 大小 | **随机 4M–5M**（`PROT_NONE`） | `vm.c:125-142` |
| 缺口（carveout） | **1M**，位置随机 | `vm.c:125-142` |
| iOS + 64bit 的熵位移 | `ENTROPIC_SHIFT 25` → **32MB PTE 对齐** | `vm.c:36-46`（逐字注释 "align 64bit ARM shift to 32MB PTE entries"，`rdar://22277891`） |
| `ENTROPIC_KABILLION` | `0x10000000` = **256MB** | `vm.c:36-46` |
| `ENTROPIC_USER_RANGE_SIZE` | `0x200000000` = **8Gb** | `vm.c:36-46` |
| arm64 LP64 堆熵 | `entropic_bits = 7` | `vm.c:61-68` |
| arm64_32（32-bit 路径）堆熵 | `entropic_bits = 3` | `vm.c:65-67` |
| x86_64 堆熵 | `16` | `vm.c` 同段 |
| ASLR 关闭时 | 逐字 "// zero slide when ASLR has been disabled by boot-arg. **Eliminate cloaking.**"，`malloc_entropy[0/1] = 0` | `vm.c:106-112` |
| 非覆盖平台 | `#error ASLR unhandled on this platform`（模拟器排除） | `vm.c:106-112` 同段 |

三点解读：

- **"Eliminate cloaking"（消除伪装）这句逐字注释值得单独引**：它说明在 ASLR 开启时，用户态看到的地址与内核实际布局之间存在**一层伪装/加扰**，而当 boot-arg 关闭 ASLR 时这层伪装被撤掉。这是"随机化不仅改变位置，还改变可观测性"的一手痕迹——但注释止于此，**伪装的实现细节未公开**（§16.2）。
- **`ENTROPIC_SHIFT 25` 与 §2.3 的共享缓存 slide 上限是同一类约束**：随机化的**粒度被页表条目尺寸钉住**（32MB PTE）。堆熵位数（7）× 该位移决定可区分的位置数，但**官方从未把它换算成"熵位数"**，本篇也不换算（§5.3）。
- **`entropic_bits` 按 ABI 分叉（7 vs 3）**：这是"同一份系统上不同指令集变体的堆随机化强度不同"的少数几个可量化一手点之一（§12.3、§12.4）。

**反向口径（必须并列写出）**：随机化分配的天花板由官方博客逐字给出（§5.1）：

> And many hardened allocator proposals that rely on randomization can be defeated by simply spraying allocations to overwhelm the randomization and once again arrive at a stable heap pattern.

即**堆喷可以把随机化压回稳定布局**——"随机化 vs 分配可预测性"这场对抗里，Apple 自己承认纯随机化会输，所以现役方案是**类型分区（§9.5）+ 严格释放（§9.2）+ 标记（§8）**，随机分桶只是其中一层。

## 9.4 调试态工具与生产态的差别

一手文本中存在三类"强度随构建/运行模式改变"的官方证据，正文必须分开陈述：

1. **构建条件级的页校验豁免**（A- 级，`ubc_subr.c:6035-6044`，外层 `#if DEVELOPMENT || DEBUG` 逐字）：
   > 	/*
   > 	 * This exemption is specifically useful for systems which want to avoid paying
   > 	 * the cost of verifying the integrity of pages, since that is done by computing
   > 	 * hashes, which can take some time.
   > 	 */
   > 	code_signing_configuration(NULL, &cs_config);
   > 	if (cs_config & CS_CONFIG_INTEGRITY_SKIP) {
   口径：**页校验有性能成本、开发构建可豁免**是官方自证；且该路径在正式 iOS 构建中**不编译**。这是"调试态 ≠ 生产态"的最硬一手例子（§10.1）。
2. **运行期调试状态位**（A- 级，`cs_blobs.h` 逐字）：`CS_DEBUGGED 0x10000000 /* process is currently or has previously been debugged and allowed to run with invalid pages */`。注意注释本身包含 **"or has previously been"**——**曾被调试**是一种会保留的状态，不是瞬时开关。同类还有 `CS_INVALID_ALLOWED …(macOS Only) Page invalidation allowed by task port policy`。
3. **诊断类分配器工具**：§9.2 的源码级开关（`GuardMalloc`、`MallocGuardEdges`、`MallocStackLogging` 系列）与 Instruments 的 Guard Malloc 条目属**调试工具**，其语义**没有** developer 文档页逐条描述（部分核实）。**不得**把它们的存在写成"生产环境启用的防护"。

另两条排除路径同样属于"环境差异"：内核侧 KASAN-TBI 是**软件仿真的标记检查**（§8.2，逐字含成本 "Cost: ~8% of memory"），而 libmalloc 对 Exclaves 走**独立熵初始化**（`vm.c:52-53` 逐字 `arc4random_buf(malloc_entropy, sizeof(malloc_entropy));`）且 guarded range 逻辑在 Exclaves 上**不启用**（`vm.c:33` / `:116` 的 `!MALLOC_TARGET_EXCLAVES`）。**结论：在模拟器、调试附加过的进程、Exclave 目标上观测到的分配行为，都不能作为真机生产态的基线**（§4.5 禁止第 2 条的分配器版本）。

## 9.5 内核侧的类型化分配与交叉引用检查

`kalloc_type` 的机制在博客里有可逐字引用的粒度定义（A 级）：

> So we designed the kalloc_type signature scheme to allow the segregation algorithm to reduce the number of pointer-data overlaps by encoding the following properties for each 8 byte granule of a type:
> ```c
> __options_decl(kt_granule_t, uint32_t, {
>  KT_GRANULE_PADDING = 0, /* Represents padding inside a record type */
>  KT_GRANULE_POINTER = 1, /* Represents a pointer type */
>  KT_GRANULE_DATA = 2, /* Represents a scalar type that is not a pointer */
>  KT_GRANULE_DUAL = 4, /* Currently unused */
>  KT_GRANULE_PAC = 8 /* Represents a pointer which is subject to PAC */
> });
> ```

三点必须照此写：

- **交叉引用检查的单位是"每个类型的 8 字节 granule"**，性质是四类具名取值（PADDING / POINTER / DATA / DUAL，其中 **DUAL 逐字 "Currently unused"**）加一个 `PAC` 位（"a pointer which is subject to PAC"）。**签名以 8 byte granule 为单位**是这一节唯一的量化一手值。
- **它优化的是"指针—数据重叠数"**（逐字 "reduce the number of pointer-data overlaps"）。这就是"把指针放在哪里"的结构化答案：内核**按类型知识把指针字段与标量字段分开排布**，使一个纯数据溢出碰不到指针 granule。**这条与 §4.2 的"泄露一个指针即可定位大量对象"是同一个问题的两侧**。
- `KT_GRANULE_DUAL` 逐字未使用 → 签名方案仍在演进，正文不应把这张枚举当稳定规格（对齐 §6.5 的 "future OSes may break ABI" 纪律）。

配套三条逐字（同篇博客）：

> (kalloc_type) … packs some object pointers into 32 bits, which limits the range where pointed objects can live.

> That being said, for C++ objects, virtual method calls dispatch through a PAC-signed vtable pointer, which reduces the set of exploitable type confusions.

> We aggressively PAC-protected pointers from typed allocations to data allocations to minimize opportunities for second-order type confusion.

这三句把 §6（PAC）与 §9（分配器）连上：**类型化分配决定"指针长在哪"，PAC 决定"指针改了会被发现"**；"把类型化分配里的指针 PAC 保护到数据分配"是为了压制**二阶类型混淆**（逐字 "second-order type confusion"）。"打包部分对象指针到 32 位"则是一处**用可预测性换检查能力**的取舍——它限制了被指对象可以存在的范围，正文要如实写成取舍而非缺陷。

**为什么类型随机分桶不同于地址随机化**（A 级逐字，本节最重要的一段）：

> The randomization here is different. Even if the attacker learns the bucket assignment of every type on the system, that reveals only which replacement types for a given UAF would work; it doesn't enable success for a specific replacement type that the attacker chose *a priori*, while developing the exploit. There's nothing the attacker can do at runtime to make their preferred replacement type feasible if the system assigned it to a different bucket than the UAF type. And since bucketing is random for every boot, the replacement types …

以及配套的实测样本（A 级逐字，样本限定必须照抄）：

> On an iPhone 13 Pro running iOS 16 beta 1, there are **194 regular (not variable-length) types** in that size class, with **119 unique signatures** and **90 signature groups** among them.

> Since the allocator metadata for kalloc_type lives entirely outside the allocated region, these 194 types are the only things that could ever colocate with the ip6_pktopts struct.

> As of iOS 16, out-of-line Mach ports arrays belong to a kalloc_type_var zone, and OSData buffers are allocated within the data submap.

> None of the primitives above would work under kalloc_type because the replacement allocations are served from separate parts of the virtual address space than the ip6_pktopts allocation.

**"随机分桶、每次启动"（逐字 "bucketing is random for every boot"）** 是强度来源；**"元数据完全位于分配区域之外"**（逐字 "allocator metadata … lives entirely outside the allocated region"）是 §9.1 那句"堆数据和其元数据分开"在内核侧的实现对应；**变长类型走 `kalloc_type_var`、OSData 走 data submap** 则说明分区维度不止"类型"一个，还有**区域归属**这一层。

## 9.6 分配器加固的失效表征

本节把"官方自己承认的残留面"集中登记，供 §15 判定链与 §16 缺口清单引用（全部 A 级逐字，出处为 SockPuppet 分析篇 2023-05-23 与 kalloc_type 篇 2022-10-27）：

| # | 残留面 | 官方逐字 | 应写的限定 |
|---|---|---|---|
| 1 | **签名碰撞** | "Of course, **signature collisions**, the data submap, and **within-type UAFs** are all still attractive targets under kalloc_type." | 同一 size class 的 194 类型 / 119 唯一签名 / 90 签名组是**该设备该构建**观测值，不是通用常数 |
| 2 | **data submap 不参与分区** | 同句 + "OSData buffers are allocated within the data submap" → "None of the primitives above would work…" 的另一面：submap 自身是独立可争夺面 | 只可写"官方点名为仍具吸引力的目标"，不得推测其当前规模 |
| 3 | **同类型内 UAF** | "within-type UAFs" | 类型化分区**不覆盖**同类占位，这是设计边界而非实现缺陷 |
| 4 | **堆喷压回稳定布局** | "can be defeated by simply spraying allocations to overwhelm the randomization and once again arrive at a stable heap pattern" | 该句针对"依赖随机化的加固方案"，对纯随机化路径成立；对类型分区路径 Apple 的回应见 §9.5 第 4 段 |
| 5 | **一次指针泄露定位大量对象** | "ASLR is often criticized because leaking one pointer value gives the attacker the location of a great number of interesting objects, even ones unrelated to the code that contains the leak." | 这是 Apple 对**随机化类**缓解的批评，不是对分配器的批评；两者不要串引 |
| 6 | **可靠性上限（非归零）** | "our analysis suggests that attackers will run into a **fundamental reliability cap** for many use-after-free vulnerabilities"；单类型重分配"just an **8% success rate**" | **8% 是 SockPuppet 这一漏洞、iPhone 13 Pro / iOS 16 beta 1 这一样本的分析值**，绝不写成"iOS 通用 UAF 成功率"（§15.2 用法纪律） |
| 7 | **绕过方式官方已描述** | "the only reliable exploit strategies we are aware of would require **writing several different exploits and then dynamically selecting which one to deploy based on the boot-time bucket assignment** of the affected types." | 只作**描述性**引用，用于说明"每次启动随机分桶把复用成本抬到'须备多套利用并运行时选择'"，不得扩写为方法 |
| 8 | **MIE 仍需补小对象** | "use EMTE to protect only smaller individual allocations within a type bucket, **which software allocators can't defend on their own**" | 逐字承认软件分配器有防不住的范围 → 分配器加固与标记检查是叠加而非替代 |
| 9 | **演进未结束** | "we expect that continued adoption and refinement of kalloc_type will make exploitation of most kernel use-after-free vulnerabilities unattractive"；§4.1 逐字 "expanded its use across our systems" | "采用范围在变"是官方表述 → **同一 iOS 大版本内不同构建的覆盖面可以不同**（§15.5 回归监测项） |

最后一行是本节真正的"失效表征"结论：**分配器加固的强度不是版本号的函数，而是"该类型是否已被纳入 typed zone"的函数**。因此评审里唯一可靠的判定不是"iOS ≥ X"，而是可观测证据（§13）加上 Apple 的逐字采用声明——而后者存在滞后期（§16.6 必须加限定语的表述）。

# 10. 第二道防线：页级校验、签名与库校验

## 10.1 按需页校验：随机化之外为什么还需要它

随机化只让"位置"难以预知，它**不检验内容**。如果一次写入能把已映射的执行页改成攻击者选定的字节，那么只要该页此前已被载入、攻击者知道它在版本内的偏移，随机化就完全不参与阻拦。第二道防线因此必须回答另一个问题：**这一页的内容，还是签过名的那份吗？**

手册的一句话给出了时机与对象（A 级，`secf49cad4db`，2024-12-19，逐字；本页标题限定 iOS、iPadOS 和 visionOS）：

> 运行时，所有可执行内存页的代码签名检查会在载入时进行，以帮助确保 App 自安装或上次更新之后未被修改过。

TN3126《Inside Code Signing: Hashes》（A 级，修订史逐字：2024-02-06 / 2022-05-24 / 2022-05-03 / 2022-03-14）把同一机制讲到实现粒度，其 Abstract 逐字："Look inside a code signature to see how it uses hashes to protect the code's executable pages, resources, and metadata from tampering."，核心机制句逐字：

> The non-negative slots are for per-page hashes: 0 is the hash for the first page of code, 1 for the second, and so on.
> This per-page architecture allows the kernel to check each page as it's loaded into memory. If a process takes a page fault on a memory mapped file that's code signed then, as part of satisfying that fault, the kernel may choose to verify the hash of the page's contents. This allows the system to run a code-signed executable and check its code signature lazily (in the computer science sense of that word).

**"lazily"必须译准并保留英文原词**：触发点是**缺页异常（page fault）**，而不是"每次访问"或"载入前一次性完成"。所以准确口径是"**页级按需（缺页时）校验**"。TN 自己给出了不可省略的限定（逐字）：

> macOS doesn't always check code as it's paged in. One key feature of the hardened runtime is that it opts the process into this checking by default. The Disable Executable Memory Protection Entitlement opts you out of this and other security features. Don't do that!

于是"页校验"在 macOS 上是**由 Hardened Runtime 默认加入、可用 entitlement 退出**的策略；在 iOS 侧手册则把它写成无条件句（"所有可执行内存页…会在载入时进行"）。**这个差异本身就是 §12.2 的一条硬结论**：同一道防线在两个平台的强制程度不同，且 iOS 侧的无条件句**没有**配套的"例外清单"。

源码侧可核对的三件物（A- 级，`ubc_subr.c`）：

- 校验入口签名逐字：`cs_validate_page(struct vnode *vp, memory_object_t pager, memory_object_offset_t page_offset, const void *data, int *validated_p, int *tainted_p, int *nx_p)` —— 出参同时产出"是否已校验""是否被污染""该页是否不可执行"，说明**页校验与执行权限判定在同一条路径上**（§7.2）。
- 可观测计数器逐字：`unsigned long cs_validate_page_no_hash = 0;` / `unsigned long cs_validate_page_bad_hash = 0;`，另有 `printf("CODE SIGNING: cs_validate_page: …")` 调试输出（`:5859/5908/5922`）。
- `DEVELOPMENT || DEBUG` 下的 `CS_CONFIG_INTEGRITY_SKIP` 豁免（§9.4 逐字）——正式 iOS 构建不编译该路径。

**页大小的口径冲突（必须写明，不可择一）**：TN3126 示例输出逐字 `Page size=4096`、`CodeDirectory v=20500 size=820 flags=0x10000(runtime) hashes=14+7 location=embedded`，并解释"14 per-page hash slots and 7 special hash slots"；而签名实现在 arm64/arm64_32 上**默认使用 16K 页哈希**（A- 级 `machorep.cpp:576` 逐字：`if (arch->cpuType() == CPU_TYPE_ARM64 || arch->cpuType() == CPU_TYPE_ARM64_32) {`）。TN 的例子取自 macOS Intel；**iOS 上的"一页"应按 16KB 理解**。§15.2 量化基线表同时登记两个值并标注语境。

**手册中唯一逐字写到"页出错时逐页验哈希"的段落来自 Rosetta 2**（A 级，`secebb113be1`，2021-02-18，逐字节选）：

> 内核仍会根据页出错所在二进制文件中所附的代码签名来验证每个 x86_64 页的代码哈希值。如果某个哈希值不匹配，内核会实施适用于该过程的修复策略。

"页出错"（page fault）在中文一手文本里出现，是这条机制最具体的时机描述；而后果措辞是含糊的"**实施适用于该过程的修复策略**"，**未给异常类型或崩溃码**（§13.2 的口径纪律由此而来）。另注意这是 **macOS（搭载 Apple 芯片）**侧页面，且文中 `dyld(1)` 仅作为"动态链接编辑器"的对照出现，**不构成 dyld 共享缓存随机化的证据**。

**签名侧的元数据绑定**（TN3126 逐字）："Slot -1 holds a hash of the Info.plist." / "Slot -3 holds a hash of the resources." / "Slot -5 holds a hash of the entitlements."——**entitlements 自身被哈希保护**，所以"改了 entitlement 还保持原授权效果"不成立。同时该系列自带引用纪律（逐字）：

> The Inside Code Signing technotes discuss code signing details that aren't considered API. The structure of a code signature has changed numerous times in the past and may well change again in the future. Don't encode this information in your product.

即**槽位编号不是稳定规格**；本篇引它只为说明机制，不为给出可编程依赖（§16.6）。

**为什么这道防线与只读映射耦合**：macOS 侧另有文件系统级保证（A 级，`secd698747c9` 逐字）："SSV 具有的内核机制会在运行时验证系统内容的完整性，并**拒绝不含来自 Apple 的有效加密签名的任何代码和非代码数据**"，且"每个 SSV SHA-256 哈希值储存在主文件系统元数据树中……因此根节点的哈希值（称为封章）覆盖了 SSV 中数据的每个字节"；iOS 侧对应句（A 级逐字）"**整个操作系统分区以只读方式装载**"。**可执行代码位置的不可修改性由宗卷/分区层提供，与随机性正交**——这就是 §2.4"只读映射、共享映射与页级校验的耦合"的官方一侧证据。

## 10.2 代码签名与授权属性对执行内存的约束

总纲句（A 级，`sec7c917bf14`，2024-12-19，逐字；该页覆盖 iOS、iPadOS、Apple tvOS、visionOS 和 watchOS，是本章平台覆盖最广的一句）：

> 内核启动后，它将控制哪些用户进程和 App 可以运行。为帮助确保所有 App 均来自批准的已知来源并且未被篡改，这些操作系统要求所有可执行代码均使用 Apple 颁发的证书进行签名。……强制性代码签名将信任链的概念从操作系统扩展至 App，并帮助防止第三方 App 载入未签名的代码资源或使用经 App 自身修改代码。

把手册句与源码位并列，就得到"签名/授权属性如何约束执行内存"的可核对清单。`cs_blobs.h` 逐字定义（A- 级，`:35-76` 节选）：

| 标志位 | 逐字注释 | 与执行内存的关系 |
|---|---|---|
| `CS_VALID 0x1` | `dynamically valid` | 有效性是运行时状态，不是加载时一次性结论 |
| `CS_HARD 0x100` | `don't load invalid pages` | **拒绝载入无效页**——页校验从"检测"变成"阻断" |
| `CS_KILL 0x200` | `kill process if it becomes invalid` | 失效即杀进程；`csops` 侧 `CS_OPS_MARKKILL` 注释逐字 `(sticky)` |
| `CS_KILLED 0x1000000` | `was killed by kernel for invalidity` | **可观测状态位**（§13） |
| `CS_DEBUGGED 0x10000000` | `process is currently or has previously been debugged and allowed to run with invalid pages` | **被调试 ⇒ 允许运行无效页**；且含"previously" |
| `CS_RUNTIME 0x10000` | `Apply hardened runtime policies` | 与 TN 示例 `flags=0x10000(runtime)` 数值互证 |
| `CS_LINKER_SIGNED 0x20000` | `Automatically signed by the linker` | 链接器自动签名产物的身份位 |
| `CS_REQUIRE_LV 0x2000` / `CS_FORCED_LV 0x10` | `require library validation` / `Library Validation required by Hardened System Policy` | **库校验可由系统策略强制**，不只依赖 entitlement（§10.3） |
| `CS_PLATFORM_BINARY 0x4000000` / `CS_PLATFORM_PATH 0x8000000` | `this is a platform binary` / `platform binary by the fact of path (osx only)` | 平台身份决定豁免范围（§6.5 arm64e、§11.4） |
| `CS_NO_UNTRUSTED_HELPERS 0x2000000` | `kernel did not load a non-platform-binary dyld or Rosetta runtime` | 内核可拒绝加载**非平台二进制的 dyld 或 Rosetta runtime** |
| `CS_INVALID_ALLOWED 0x20` | `(macOS Only) Page invalidation allowed by task port policy` | **macOS 独有**的"允许页失效"通道 |
| `CS_EXEC_SET_HARD 0x100000` / `CS_EXEC_SET_KILL 0x200000` | `set CS_HARD on any exec'ed process` / `…KILL…` | HARD/KILL **可跨 exec 传播** |

三点口径：

1. **"签名"不是一条检查，而是一组可分别开关的运行时策略**。`CS_HARD`（不载入无效页）与 `CS_KILL`（无效即杀）是两个独立位，手册句"所有可执行内存页…会在载入时进行"没有说明是哪一位在起作用——**这正是不能把手册句直接当实现规格的原因**（§16.6）。
2. **存在"降级状态位"**：`CS_DEBUGGED` 逐字承认"曾/正被调试并允许运行无效页"。也就是说**同一份二进制在被附加过调试器之后的运行，其页校验强度与未被调试时不同**，且该状态会保留（"or has previously been"）。这是 §15.3 自查清单里"取证时须先确认调试状态"的依据。
3. **`csops` 提供运行时读写通道**（A- 级 `codesign.h:40-61` 逐字）：`CS_OPS_STATUS`、`MARKINVALID`、`MARKHARD`、`MARKKILL (sticky)`、`CDHASH`、`ENTITLEMENTS_BLOB`、`MARKRESTRICT (sticky)`、`SET_STATUS`、`IDENTITY`、`CLEARPLATFORM (DEVELOPMENT-only)`、`TEAMID`、`CLEAR_LV`、`VALIDATION_CATEGORY`。其中 **`CS_OPS_CLEARPLATFORM` 限定 DEVELOPMENT-only**、**`CS_OPS_VALIDATION_CATEGORY` 说明存在"校验类别"这一维**（与 §10.3 的 Team ID / Apple 签名两条件相接）。

**执行内存申请侧的授权门控**已在 §7.3 逐字给出（"内核会检查 Apple 专有的动态代码签名授权是否存在"）。这里补一句归位：**该 entitlement 门控的对象是动态代码签名授权（= JIT），不是 PAC**——手册全库对 PAC 没有任何 entitlement 表述（§6.4），正文不得把这两者混作同一句证据。

## 10.3 库校验：可加载代码的边界

手册给出的是**跨进程侧**的约束（A 级，`sec7c917bf14`，逐字整段）：

> 为保护系统并防止其他 App 在其地址空间中载入第三方代码，系统将为启动时所有链接到进程的动态资源库执行代码签名验证。此验证过程通过团队标识符 (Team ID) 完成。团队标识符提取自 Apple 颁发的证书，是由 10 个字符组成的字母数字串，例如 1A2B3C4D5F。程序可链接到系统自带的任何平台资源库，也可以链接到代码签名中具有与主可执行文件相同团队标识符的资源库。因为作为系统一部分发布的可执行文件不具有团队标识符，所以它们只能链接到随系统本身发布的资源库。

这段是**全篇最重要的一段"地址空间内容不可由攻击者选择"的表述**，三点逐字结论：

- **"防止其他 App 在其地址空间中载入第三方代码"** —— 手册里唯一把**"地址空间"**与**代码注入防御**直接绑定的句子。dylib 注入 / 库劫持这类"不需要内存破坏、只需改变地址空间内容"的路径，官方对策是 **Team ID 匹配**，不是随机化（§7.5 剩余跳板第 7 条的责任方在此）。
- **两个可链接条件**：平台资源库（系统自带）**或**与主可执行文件**同 Team ID**。
- **系统进程更严**：逐字"作为系统一部分发布的可执行文件**不具有团队标识符**，所以它们**只能链接到随系统本身发布的资源库**"。即对系统进程而言，可加载集合被收窄到平台代码——**比随机化硬得多的一道门槛**。

developer 侧给出的是**macOS 进程内**的定义（A 级，`com.apple.security.cs.disable-library-validation` 页 Library validation 段逐字）：

> The [Hardened Runtime] enables library validation by default. This security-hardening feature prevents a program from loading frameworks, plug-ins, or libraries unless they're either signed by Apple or signed with the same Team ID as the main executable. The macOS dynamic linker (`dyld`) provides a detailed error message when the system prevents code from loading due to library validation. Use the [Disable Library Validation Entitlement] if your program loads plug-ins that are signed by other third-party developers.
> [aside] Because library validation is such an important security-hardening feature, Gatekeeper runs extra security checks on programs that have it disabled. If your program is blocked by Gatekeeper, check whether you've unnecessarily disabled library validation.

四处可核对：① 通过条件逐字为**"signed by Apple" 或 "same Team ID"**两个枚举值，与手册中文段的两个条件**完全对应**（跨 A 级双源互证，§16.4 必须并读的一簇）；② **Hardened Runtime 默认启用库校验**；③ 拒绝时 **dyld 会给出"detailed error message"**——这是"加载失败"可观测性的一手承诺（§13.4）；④ 关闭库校验会触发 **Gatekeeper 额外安全检查**，Apple 用"such an important security-hardening feature"为其定性。**差项提醒**：上述页面 platform 全部标注 **macOS-only**，且一手页**没有**"iOS 强制开启 library validation"这类正向表述；iOS 侧只能引手册 逐字段（§10.2 总纲）作为强制模型依据，正文须写"iOS 的对应约束来自手册的强制签名与 Team ID 段，而非开发者可选开关"（§12.2、§16.6）。

另两条与"谁能进入地址空间"相关的一手句：`CS_FORCED_LV` 注释逐字表明库校验**可由 Hardened System Policy 强制**（不依赖 App 自己的 entitlement）；`dyld(1)` 手册页列出 **20 个 `DYLD_*` 注入变量**，并逐字说明**受 SIP 保护的二进制会忽略上述全部变量**（§10.4 第 6 项、§11.3）。

## 10.4 会削弱防护的构建与签名选项（逐条列后果）

**本节只描述官方公开的选项与其后果，用于配置审计与整改，不含任何执行步骤。**

| # | 选项 / 属性 | 一手出处（级别） | 逐字后果 |
|---|---|---|---|
| 1 | `com.apple.security.cs.allow-jit` | developer entitlements 页（A，macOS-only） | 允许以 `MAP_JIT` 创建可写可执行内存；**Hardened Runtime disallows this by default, because it creates a security risk**。未授予时"frameworks that rely on JIT **may fall back to an interpreter**"（§7.4） |
| 2 | `com.apple.security.cs.allow-unsigned-executable-memory` | 同上（A） | 允许**不受 `MAP_JIT` 限制**地创建 W+X；官方 aside 逐字"exposes your app to **common vulnerabilities in memory-unsafe code languages**"；触发场景含 `NSCreateObjectFileImageFromMemory`（逐字 "which is fundamentally insecure"） |
| 3 | `com.apple.security.cs.disable-executable-page-protection` | 同上（A） | 逐字"disable **all** code signing protections while launching an app, and during its execution"；aside 逐字"**an extreme entitlement** that removes a fundamental security protection … making it possible for an attacker to **rewrite your app's executable code without detection**"；TN 追加"Don't do that!"；且**架构分叉**：对 Apple silicon 代码该 entitlement "leaves page protection enabled, making it equivalent to the Allow Unsigned Executable Memory Entitlement"（§7.3） |
| 4 | `com.apple.security.cs.disable-library-validation` | 同上（A） | 允许加载任意插件/框架；副作用：Gatekeeper 额外检查（§10.3） |
| 5 | `com.apple.security.cs.allow-dyld-environment-variables` | 同上（A） | 逐字"may be affected by dynamic linker environment variables, **which you can use to inject code into your app's process**"；且注入库若非同 Team ID 还需第 4 项（逐字"may still require another entitlement"） |
| 6 | `DYLD_*` 环境变量族 | `dyld(1)` 手册页（A，macOS） | 20 个变量构成运行期行为改道面；**受 SIP 保护的二进制忽略全部 `DYLD_*`**（逐字）。iOS 无对等概念，不得移植该条结论 |
| 7 | `-allow_stack_execute` | `ld(1)` 手册页（A） | 允许栈可执行；**Intel 专属**，且逐字涵盖 "**This includes pthread stacks**"（§3.1） |
| 8 | `MH_NO_HEAP_EXECUTION` 缺失 / `_POSIX_SPAWN_ALLOW_DATA_EXEC` | `mach_loader.c:779-783`、`kern_exec.c:6764-6768`（A-） | header 未置位则不强制关闭数据页执行；spawn 标志可推翻，特定进程再被内核清除该豁免（§7.2） |
| 9 | boot-arg `-disable_aslr` | xnu（A-） | 全局关闭 ASLR；libmalloc 侧逐字 "// zero slide when ASLR has been disabled by boot-arg. **Eliminate cloaking.**"（§9.3） |
| 10 | `_POSIX_SPAWN_DISABLE_ASLR` | `spawn.h`（A-） | 进程级关闭；`P_DISABLE_ASLR` **在 fork 时被继承**；**setuid 强制重新启用**（§4.1） |
| 11 | `-mdynamic-no-pic` | `ld(1)`（A） | 与 PIE 不兼容 → 关掉主镜像随机化；PIE 句逐字 "load a PIE at a random address each time it is executed"，收益措辞 "**adds some security**"（§2.1） |
| 12 | `-random_uuid` | `ld(1)`（A） | **不是安全特性**：只影响 `LC_UUID` 生成方式，官方说明其利于可复现构建（§5.5、§16.5） |
| 13 | `ENABLE_POINTER_AUTHENTICATION` 被 `ARCHS` 覆盖失效 | build-settings（A） | 逐字"Has no effect if `ARCHS` has been overridden…"→ **产物无 arm64e 切片且无告警**（§6.4） |
| 14 | `ENABLE_ENHANCED_SECURITY` 逐项关闭 | build-settings（A） | 逐字"These settings can be disabled individually"→ PAC / typed allocator / hardened libc++ / 警告可被分别摘掉（§6.4、§9.1） |
| 15 | `get-task-allow` / `com.apple.security.cs.debugger` | entitlements 页（A） | 允许 `task_for_pid()` 取得 task port；副作用是 `CS_DEBUGGED` 语义"allowed to run with invalid pages"（§10.5） |
| 16 | `CS_OPS_CLEARPLATFORM` | `codesign.h`（A-） | 清除平台二进制身份，注释逐字限定 **DEVELOPMENT-only** |
| 17 | `CS_CONFIG_INTEGRITY_SKIP` | `ubc_subr.c`（A-） | 跳过页完整性校验，外层 `#if DEVELOPMENT \|\| DEBUG` → 正式 iOS 构建不编译（§9.4、§10.1） |
| 18 | `CS_INVALID_ALLOWED` | `cs_blobs.h`（A-） | 注释逐字标注 **(macOS Only)**：task port 策略可允许页失效 |

三条审计口径：**①"削弱"绝大多数发生在 macOS 侧**（六项 Runtime Exception 全为 macOS-only），**iOS 侧的削弱面集中在构建选项与 boot-arg/spawn 标志**，不在开发者 entitlement 清单里；**② 有静默失效项**（第 6、13 项）——不报错、不告警，只能靠产物核对与运行时探测发现；**③ 同名选项跨架构后果不同**（第 3、7 项）——按名字判断强度的评审脚本必然出错。

## 10.5 调试授权与跨进程内存访问

一手文本对"读别人内存"的正面表述是**缺失**的（A 级检索：developer 文档与四仓中**没有**形如"未签名代码不能读取其他进程内存"的文档句；`task_for_pid`、`vm_read`、`mach_vm_read` **无文档页**）。可引的是五条间接句：

1. **前置条件是 task port，且受双重门控**（A 级，`com.apple.security.cs.debugger` 页逐字）：
   > Apps with the `Debugging tool entitlement` can call `task_for_pid()` to retrieve a valid task port for unsigned and third-party apps with the `Get task allow` entitlement set to `true`. However, even with the debugging tool entitlement, a debugger can't get the task ports of processes that don't have the `Get task allow` entitlement, and that are therefore protected by System Integrity Protection.
   两句含义同时成立：**开了 `get-task-allow` 的第三方/未签名 App 就是可被读取的对象**；**没开的进程受 SIP 保护而拿不到 port**。该页 platform 为 **macOS**。同页还有两条量化与流程事实：Xcode 自动为调试构建加入 `Get task allow`、**在 App Store 提交前移除**；非 root 用户运行带调试 entitlement 的 App 时**弹出管理员授权对话框**，成功后"**receives a 10-hour session before authorization expires**"。
2. **沙盒的官方目的是损害控制**（A 级，`security/app-sandbox` Abstract 逐字）："Restrict access to system resources and user data in macOS apps to **contain damage if an app becomes compromised**."——注意用词是"contain damage"，**不是**"隔离内存读取"，且标注 macOS。正文不得把它当作进程间内存隔离的一手定义。
3. **扩展项有独立地址空间**（A 级，`secabd3504cd` 逐字）："扩展项在其自己的地址空间运行……**它们无权访问彼此的文件或内存空间**。"——这是手册里唯一逐字写"自己的地址空间"的进程隔离句，它把"越出被攻破进程"的成本从"绕过 ASLR"提升为"跨越进程边界 + 绕过 IPC 校验"。
4. **随机化与沙盒被官方列为相互补强**（A 级博客逐字，§5.2）："kalloc_type and sandboxing **complement each other** and make exploiting this bug **from an unprivileged position** substantially harder."——这是"第二道防线"这个说法最直接的一手措辞之一。
5. **读取与校验弱化耦合在同一状态位**（A- 级，`CS_DEBUGGED` 逐字含 "allowed to run with invalid pages"）：**能被调试的进程，同时也是页校验被放宽的进程**。审计时这两项应合并检查，不能分开打分。

**跨进程可加载性方面另有一条常被忽略的一手事实**（A- 级，§6.5）：共享缓存选择可按 **team-id 或 entitlement** 定制（`kern_exec.c:1682-1696` 逐字）。口径应写成"**缓存布局的可见范围可按身份切分**"，**不要**写成"每个 App 都有独立随机布局"（后者无一手依据）。这条与 §14.7 直接相关：**一次布局泄露的可复用范围受身份分区约束**。

## 10.6 已签名代码不等于安全代码：责任回落到开发者

本章最后要把"签名能保证什么、不能保证什么"划清，因为这是所有加固评审里最容易被混用的一条界线。

**签名保证的是来源与未被篡改**。TN3126 逐字："This technology is absolutely central to code signing's core function: **protecting code from malicious modification**."；手册 SSV 逐字："拒绝不含来自 Apple 的有效加密签名的任何代码和非代码数据"。**签名不保证代码质量**——一手文本在这一点上同样直白：

- entitlement aside 逐字：memory-unsafe 语言存在 "**common vulnerabilities**"（§10.4 第 2 项）；
- 博客开篇逐字：> Because nearly all popular user devices today rely on code written in programming languages like C and C++ that are considered "memory-unsafe," meaning that they don't provide strong guarantees which prevent certain classes of software bugs, improving memory safety is an important objective for engineering teams across the industry. On Apple platforms, improving memory safety is a broad effort that includes **finding and fixing vulnerabilities, developing with safe languages, and deploying mitigations at scale**.
- 以及 Apple 对自己语言的表态：> We created Swift, an easy-to-use, memory-safe language, which we employ for new code and targeted component rewrites.

**由此得到本篇的责任分配结论**（A 级依据，措辞需带限定）：页级校验、Team ID 匹配与强制签名把"**别人的代码能不能进入你的地址空间**"变成了受控问题；而"**你自己的代码里的缺陷**"只能靠 §6/§7/§8/§9 的缓解来**降低可利用性**，不能靠签名消除。§1.3 的"缓解 ≠ 修复"在本节的具体形式就是：**一个签名完好、库校验严格、PAC 全开的 App，仍然可能因为自身一处 UAF 而被完全接管；此时所有缓解的作用是让这次接管变得不可靠、不可复用、代价高昂**（§9.1 逐字 "inherently unreliable" / "more bespoke and less reusable"）。

顺带登记一条**必须澄清的流行误用**：手册与 TN 中**没有**任何句子把"已签名"与"已加固"或"已审核"并列；相反，Apple 逐字提醒该系列"details that aren't considered API"、结构"may well change again in the future"。**把签名属性当成安全评审结论的做法，一手文本并不支持。**

# 11. 谁能改变一个进程的抗利用强度

## 11.1 开发者：编译标志、依赖库、签名与授权属性

开发者是**唯一能在构建期决定"这个进程有哪些门槛"的主体**，且其能力清单全部可核对：

| 能力 | 一手依据（级别） | 提升 | 削弱 |
|---|---|---|---|
| 是否产出 arm64e 切片 | `ENABLE_POINTER_AUTHENTICATION`（A，逐字"Adds an additional architectural slice (arm64e)… to `ARCHS_STANDARD`"） | 该切片内的返回地址/函数指针/虚表按 §6.2 表被签 | **覆盖 `ARCHS` 会让开关静默失效**（逐字），且无告警 |
| 是否启用硬件检查指针运算切片 | `ENABLE_HARDWARE_CHECKED_POINTER_ARITHMETIC_SLICE` → `arm64e.x1`（A） | 新增切片，独立于 arm64e | 同上（依赖 `ARCHS_STANDARD`） |
| 一次性启用一组加固 | `ENABLE_ENHANCED_SECURITY`（A，含 PAC、typed allocator、hardened libc++、安全警告） | 与 MIE/EMTE 的开发者入口逐字对上（§8.4） | 逐字"These settings can be disabled individually" |
| 栈变量零初始化 | `CLANG_ENABLE_STACK_ZERO_INIT`（A，逐字"Automatically initializes stack variables to zero as a security protection"） | 减少未初始化栈内容成为信息源的机会 | 关闭即无 |
| C++ 边界安全模型 | `ENABLE_CPLUSPLUS_BOUNDS_SAFE_BUFFERS`（A，逐字"rejecting raw pointer arithmetic … requiring the use of hardened C++ standard library"） | 把 `-Wunsafe-buffer-usage` 变成错误 | 不采用则无 |
| PIE / 主镜像随机基址 | `ld(1)`（A，逐字"load a PIE at a random address each time it is executed"、"adds some security"） | §2.1 | `-mdynamic-no-pic` 与之不兼容；非 PIE 即固定基址 |
| `__PAGEZERO`、`-stack_size`、页大小 | `ld(1)`（A） | 4KB/4GB 零页哨兵；栈尺寸声明（默认 8MB，须为 4KB 或 16KB 的倍数） | `-allow_stack_execute`（Intel 专属，逐字涵盖 "This includes pthread stacks"） |
| 第三方依赖库的强度 | Team ID / 平台库两个可链接条件（A，§10.3） | 库校验保证"进来的代码是谁" | **库校验不检验库自身有没有开 PAC/PIE**——依赖审计必须逐个产物查（§15.4） |
| 签名属性与 entitlement | `CS_RUNTIME`/`CS_REQUIRE_LV`/`CS_FORCED_LV`（A-）+ 六项 macOS Runtime Exceptions（A） | 加固运行时策略、库校验 | §10.4 全表 |
| 调试用 `get-task-allow` | entitlements 页（A，Xcode 调试构建自动加入、**提交前移除**） | — | 残留即让该 App 成为可被 `task_for_pid()` 读取、且 `CS_DEBUGGED` 语义下允许运行无效页的对象 |

**本节最重要的一句评审结论**：上表右列的**每一项都是"构建/签名期决定、运行期不可补救"的**。也就是说，**一个进程上线后的抗利用强度，绝大部分在 CI 里就已经定死了**（§15.4 的构建审计因此优先于任何运行时监测）。

## 11.2 用户：锁定模式、诊断数据、企业应用与恢复方式

用户能改变的量比开发者小，但有三条是**一手且可验证**的：

1. **锁定模式收窄执行面**（A 级，105120 逐字："某些复杂的网页技术会被阻止"；归因 "WebKit 相关限制"）。同时，用户**也能两次点击撤回它**：按网站排除、按 App 排除，官方自陈"会削弱锁定模式的保护效果，可能提升你的安全风险"（§7.4）。系统下限逐字（`sec2437264f0`）："锁定模式在 iOS 16、iPadOS 16、macOS 13、watchOS 10 或更高版本中可用。iOS 17、iPadOS 17、macOS 14 和 watchOS 10.1 更新或更高版本中提供了额外的保护。"
2. **是否共享分析数据**决定了崩溃现场（含地址、偏移、镜像清单）是否会离开设备（A 级：`sec87fc038c2` 逐字设备端分析系统与崩溃率统计；开关路径"设置 > 隐私与安全性 > 分析与改进"）。这是 §14 的入口条件，也是**用户侧唯一会改变"布局信息暴露面"而不是"布局本身"的操作**。
3. **系统更新与后台安全改进**（A 级逐字："iOS 26.1、iPadOS 26.1 和 macOS 26.1 及后续版本将支持和启用后台安全改进"）——用户接受更新即改变分配器与缓存版本（§2.5、§15.5）。

**必须写明的用户侧不能做之事**：一手文本**没有**任何面向用户的"提高随机化强度/关闭某道门槛"的配置项。手册给出的唯一"关闭防线"通道是**研究设备**（§11.5），而它被明确描述为"**无需破坏或取消激活平台安全功能**"、且载体是 iBoot 变体与启动内核集（生产设备无法启动）。**"越狱即得到不同布局"这一流行说法在 Apple 一手文本里没有对应页面**（§16.2）——正文应写"用户无法配置内存防线，只能通过是否接受更新、是否开启锁定模式、是否共享诊断数据来改变暴露面与攻击面"。

**恢复方式属于用户可决定的强度差异**：抹掉恢复 vs 从备份恢复，决定了"每次安装随机分配"的那批基址是否换新（§5.4）。这是治理文档里唯一能落到用户操作层面的"重随机化"建议。

## 11.3 组织：配置描述文件、注入与代理类能力

组织（MDM / 企业分发）的能力在一手文本里是**窄而具体**的：

- **可以把一条内存相关防线关掉**（A 级逐字）："在运行 iOS 18.4、iPadOS 18.4 或更高版本的设备上，**IdleRebootAllowed 设置可允许管理设备的管理员打开或关闭自动重启**"，且同页逐字"**自动重启在受监督设备上默认关闭**"。回顾 §10.5 引用的自动重启句："在重启过程中，设备会从内存清除敏感的安全密钥和瞬态数据"——**即"用生命周期收窄内存中敏感内容暴露窗口"这条防线，在受监督设备上默认不生效，且可由管理员开关**。这是本篇唯一一条"组织主体可直接改变内存暴露窗口"的一手证据。
- **企业分发的 App 仍是签名模型下的对象**：Team ID 匹配与强制签名对所有第三方进程一致适用（§10.3 逐字），一手文本**没有**"企业证书可绕过库校验"的表述。**不得**据"企业应用更容易被注入"这类二手说法写入本篇结论（§16.2）。
- **组织不能注入运行时环境变量**：`DYLD_*` 注入面（20 个变量）是 **macOS** 的动态链接器行为，且**受 SIP 保护的二进制忽略全部 `DYLD_*`**（A 级 `dyld(1)` 逐字）。iOS 侧无对应一手通道。
- **组织可强制系统版本与更新节奏**，从而间接决定 §12 的世代矩阵落在哪一列——这是治理上最有效的杠杆，但它改变的是**硬件世代与系统版本的组合**，不是某个进程的配置。

## 11.4 Apple：系统版本、指令集约定与硬件世代

Apple 自己是**唯一能同时改变三层**的主体，且一手文本给出了三层的例子：

- **系统版本层**：分配器采用范围随版本推进，逐字 "We first shipped this new hardened allocator, called kalloc_type, in iOS 15, and **this year we've expanded its use across our systems**"（A 级，2022-10-27）。**"expanded its use"是一句会过期的话**——它意味着同一 iOS 大版本内、不同构建之间的覆盖面可以不同（§9.6 第 9 行、§15.5 回归监测）。
- **指令集/ABI 约定层**：arm64e ABI 版本上限按平台分叉（macOS=V1、iOS=V0，§6.5）、第三方 arm64e 在 macOS 上被拒载、`arm64e 有独立 shared region` 且可按 team-id/entitlement 选取（§6.5）。**这些是"Apple 通过约定改变暴露面"的例子，而不是通过开关**。
- **硬件世代层**：见 §11.5 与 §12.1 矩阵。

另有一条常被忽略的**平台身份差**：`CS_PLATFORM_BINARY` / `CS_PLATFORM_PATH`（A- 级）与 arm64e 拒载逻辑中的 **platform binary 豁免**（§6.5）共同说明：**同一份系统上，"平台二进制"与"第三方二进制"适用的规则集不同**——这不是强度配置，而是身份属性。

## 11.5 硬件与固件：谁能提供什么、缺了谁都不成立

矩阵（A 级，`sec8b776536b` 与 `sec87716a080` 内容完全一致，均 2026-01-28）本身即"谁缺了谁都不成立"的清单：

| 功能（逐字行名） | A10 | A11、S3 | A12-A14 / S4-S10 | A15-A18 | M1 | M2-M4 | A19 / M5 |
|---|---|---|---|---|---|---|---|
| 内核完整性保护 | 支持 | 支持 | 支持 | 支持 | 支持 | 支持 | 支持 |
| 快速权限访问限制 | 不支持 | 支持 | 支持 | 支持 | 支持 | 支持 | 支持 |
| 系统协处理器完整性保护 | 不支持 | 不支持 | 支持 | 支持 | 支持 | 支持 | 支持 |
| **指针认证代码** | 不支持 | 不支持 | **支持** | 支持 | 支持 | 支持 | 支持 |
| 页面保护层 | 不支持 | 支持 | 支持 | 不支持 | 支持 | 不支持 | 不支持 |
| 安全页表监控器 | 不支持 | 不支持 | 不支持 | 支持 | 不支持 | 支持 | 支持 |
| 通过增强型内存标记扩展实现内存完整性强制 | 不支持 | 不支持 | 不支持 | 不支持 | 不支持 | 不支持 | **支持** |

（列名与行名逐字；支持与否取自单元格图标语义 `IL_green_checkmark.png` → 支持、`IL_red_x.png` → 不支持，**属图片语义而非文字表格**——这是本篇引用该表时必须保留的方法限定。总纲范围句逐字："以下保护措施在运行 Apple 设计的受支持 SoC 的设备上可用，此类 SoC 包括 iOS、iPadOS、搭载 Apple 芯片的 Mac 上运行的 macOS、Apple tvOS、visionOS 和 watchOS。"）

四条"缺了谁都不成立"的依赖关系：

1. **PAC 需要芯片**：逐字下限 A12 / S4（表格）；博客给出**部署年份**（A 级逐字）："In 2018, we were the first in the industry to deploy **Pointer Authentication Codes (PAC) in the A12 Bionic chip**, to protect code flow integrity in the presence of memory corruption."——**编译期有 arm64e 切片但运行在不支持的 SoC 上，该切片不会被选用**（§6.8）。
2. **MIE 需要 A19/M5，且需要 SPTM**：逐字"安全页表监控器会保护内核分配器后备存储区和**标记存储区**"（§8.2）——**标记保密性的前提是页表监控器存在**。对照矩阵：SPTM 在 **M1 列为不支持**、PPL 为支持。即第一代 Apple 芯片 Mac 走的是 PPL 路径，其"标记存储区保护"层级与 A19/M5 不同（§12.2）。
3. **PPL 与 SPTM 互斥对应**：A15-A18、M2-M4、A19/M5 列 PPL 不支持、SPTM 支持，由脚注逐字解释："在 A15 或后续芯片和 M2 或后续芯片的 SoC 中，SPTM（与 TXM 结合使用）**取代了 PPL**，它提供了更小的攻击面，且不依赖内核信任，即使在启动初期也是如此。"**不要把 PPL 的"不支持"格子当作防线消失的证据**；反之 M1 列（PPL 支持、SPTM 不支持）是矩阵里最容易被误读的一格，引用时须连列名一起给。
4. **macOS 缺两道防线，且是设计选择**：逐字"此保护在 **macOS** 上不提供，因为 PPL 只在所有执行代码都必须签名的系统上适用"；脚注 2 逐字"页面保护层 (PPL) 和安全页表监控器 (SPTM) 会在 **macOS 除外**（因为 **macOS 设计允许运行任何代码**）的所有平台上强制执行受信任的签名代码。包括页表保护在内的所有其他安全属性均可用于所有受支持的平台。"——**这是 Apple 对"iOS 与 macOS 利用难度差异根因"最直白的逐字表述**（§12.2、§16.5）。

固件/引导侧另有一手样本说明"防线可以被变更，但变更被锁在启动链工件层"（A 级，`seca7ff718d2`，2022-05-01，逐字）：

> Apple 安全性研究设备是一台接线特殊的 iPhone，可让安全研究员在 iOS 上执行研究，而无需破坏或取消激活平台安全功能。通过此设备，研究员可侧载需要平台对等权限来运行的内容，并在与生产设备上的平台更接近的平台上执行研究。
> 为帮助确保用户设备不受安全性研究设备执行策略的影响，策略更改会在 iBoot 变体和启动内核集中实施，而它们在生产设备上无法启动。研究 iBoot 会检查新的接线状态，如果在非研究目接线的硬件上运行，该 iBoot 会进入错误循环。

同页还逐字给出四封堵与一条签名服务器条件（"签名服务器会拒绝为不在明确允许列表上的设备定制 Cryptex 磁盘映像"）。**结论：Apple 把"放宽防线"实现为专用硬件 + 启动链工件，而不是提供关闭内存防线的配置项**——这既回答了"谁能削弱"，也是 §11.2"用户无配置项"的正面依据。

## 11.6 主体—能力—可观测性速查表

| 主体 | 能改变什么 | 一手依据 | 改后如何被发现（可观测性） |
|---|---|---|---|
| 开发者 | 切片是否存在（arm64e/arm64e.x1）、PIE、`__PAGEZERO`、栈尺寸、C++/栈零初始化、依赖库来源、签名属性与 entitlement | §11.1 全表（A/A-） | **产物侧**：Mach-O 切片清单与 cpusubtype（含 `0x0f000000` ABI 版本位）、header flags、`codesign --display` 的 `flags=0x…(runtime)`；**签名侧**：entitlements blob（Slot -5 哈希）|
| 开发者（负面） | 覆盖 `ARCHS` 使 PAC 开关静默失效 | §6.4 逐字 | **无告警**；只能靠核对产物切片清单发现 |
| 用户 | 锁定模式开/关、按网站/按 App 排除、是否共享分析数据、是否更新、恢复方式 | §11.2（A） | 设置项状态；分析数据是否上报（§14）；**布局本身不因用户配置而变** |
| 组织 | `IdleRebootAllowed`（自动重启开关，受监督设备默认关闭） | §11.3 逐字（A） | 描述文件配置审计；重启周期可观测为"内存中密钥残留时长" |
| 组织（不能做） | 注入 `DYLD_*`（macOS 专有且 SIP 保护进程忽略）；绕过库校验（无一手依据） | §11.3（A/缺口） | — |
| Apple | 分配器覆盖面、ABI 约定、平台二进制身份、共享缓存分区规则 | §11.4（A/A-） | 版本发布说明、博客逐字句（"expanded its use across our systems"）、崩溃样本统计的分组差异 |
| 硬件世代 | 是否提供 PAC / APRR / PPL / SPTM / MIE | §11.5 矩阵（A） | 运行时应查 `hw.optional.arm.FEAT_*` 与 commpage 位，**不查芯片型号**（§6.8 逐字禁止） |
| 内核自身（降级） | `-disable_aslr` boot-arg、`_POSIX_SPAWN_DISABLE_ASLR`（fork 继承、setuid 强制重开）、`IMGPF_ALLOW_DATA_EXEC`、`IMGPF_NOJOP`/`disable_user_jop` | §4.1、§7.2（A-） | `sysctl_slide`（逐字 "Boolean indicating if KASLR is active"）、`vm_shared_region_reslide_count`、`cs_validate_page_bad_hash` 等计数器 |

## 11.7 强度变更的审计线索清单

把 §11.1–§11.6 压成一份可执行的审计清单。**每条都标注线索来源级别**，以便区分"可核对事实"与"需推断项"：

1. **产物切片与 ABI 版本**：核对是否含 arm64e（及 arm64e.x1）；cpusubtype 的 ptrauth 版本位是否落在本平台 `MAX_PREFERRED_VERSION` 区间内（A-）。**不要**以 build setting 为证据。
2. **header flags**：PIE 位、`MH_NO_HEAP_EXECUTION`、`__PAGEZERO` 尺寸（A）。
3. **签名标志**：`flags=0x10000(runtime)` 之类；对照 `CS_RUNTIME`/`CS_REQUIRE_LV`/`CS_FORCED_LV` 语义（A + A- 互证）。
4. **entitlements 清单**：macOS 侧逐项比对六项 Runtime Exception；iOS 侧核对 `get-task-allow` 是否在提交构建中残留（A）。
5. **依赖库来源**：逐个第三方库确认 Team ID 关系与加固状态；**库校验只保证"谁签的"，不保证"开了什么"**（A，§11.1）。
6. **调试状态**：确认目标进程未被附加过调试器（`CS_DEBUGGED` 含 "previously"，A-）。
7. **运行时随机化状态**：`sysctl_slide`、共享缓存 reslide 计数（A-）；注意这些是**内核内部通道**，面向开发者文档未公布。
8. **能力位而非型号**：`hw.optional.arm.FEAT_PAuth/PAuth2/FPAC/FPACCOMBINE/PACIMP/BTI`、`armv8_gpi`、commpage `CAP_BIT_FEAT_*`（A-）。**登记为纪律**：`hw.cpufamily` 不得用于特性判定（逐字禁止，§6.8）。
9. **平台与 SoC 世代定位**：用 §12.1 矩阵与 §12.2 版本范围句**分别**核对（矩阵按 SoC、范围句按系统版本，两类口径不可混写，A）。
10. **锁定模式与其排除项**：是否开启、是否有按网站/按 App 排除（A，§7.4）。
11. **受监督设备的 `IdleRebootAllowed`**：默认关闭意味着内存中密钥清除窗口不生效（A，§11.3）。
12. **诊断上报配置**：决定是否产生 §14 的布局外泄面（A）。
13. **构建/开发态痕迹**：`DEVELOPMENT || DEBUG` 下的页校验豁免路径是否存在于被测系统镜像（A-）；**正式 iOS 构建不编译该路径**。
14. **版本一致性**：崩溃统计与加固结论必须绑定"系统版本 + 构建号 + SoC 列"三元组（§15.5）；引用官方博客数字时保留样本限定（§9.6）。

# 12. 平台与硬件世代差异

## 12.1 iOS 与 iPadOS 的世代矩阵

iOS/iPadOS 是本篇所有结论的**默认语境**，因为手册里关于随机化与内存防线的正面段落绝大多数限定于这两个平台（`sec15bfe098e` 标题为"iOS、iPadOS 和 visionOS 中的运行时进程安全性"，XN 段逐字只写 "iOS 和 iPadOS"）。

**按 SoC 世代**（矩阵，§11.5，方法与图标限定同处保留）：

| 世代列 | KIP | APRR | 协处理器完整性 | PAC | PPL | SPTM | MIE(EMTE) |
|---|---|---|---|---|---|---|---|
| A10 | ✓ | ✗ | ✗ | ✗ | ✗ | ✗ | ✗ |
| A11、S3 | ✓ | ✓ | ✗ | ✗ | ✓ | ✗ | ✗ |
| A12-A14 / S4-S10 | ✓ | ✓ | ✓ | ✓ | ✓ | ✗ | ✗ |
| A15-A18 | ✓ | ✓ | ✓ | ✓ | ✗ | ✓ | ✗ |
| M1 | ✓ | ✓ | ✓ | ✓ | ✓ | ✗ | ✗ |
| M2-M4 | ✓ | ✓ | ✓ | ✓ | ✗ | ✓ | ✗ |
| A19 / M5 | ✓ | ✓ | ✓ | ✓ | ✗ | ✓ | ✓ |

读法纪律四条：**①该表没有 macOS 版本列**（手册按 SoC 划定这些硬件防线，与 §12.2 的版本类范围句是两类口径，不可混写）；**②表内不存在 KASLR/内核随机化行，也不存在 BTI、GCS 行**（与附录 A 的 0 命中一致）；**③PPL/SPTM 两列互斥对应**（脚注逐字"SPTM…取代了 PPL"）；**④M1 列 PPL✓/SPTM✗ 是最易误读的一格**，引用时须连列名一起给。

**按系统版本的范围句**（全部逐字，标页与发布日期）：

| 机制 | 逐字范围句 | 页 / 发布日期 |
|---|---|---|
| 内存安全 iBoot 编译 | "在运行 iOS 14 和 iPadOS 14 或更高版本的设备上"；"搭载 **A13** 仿生芯片或后续型号的 **iPhone** 和搭载 **A14** 仿生芯片或后续型号的 **iPad** 上可用" | `sec30d8d9ec1` / 2021-02-18 |
| BlastDoor | "运行 iOS 14、iPadOS 14、macOS 11、watchOS 7 或更高版本的设备" | `secd3c881cee` / 2024-05-07 |
| 锁定模式 | "锁定模式在 iOS 16、iPadOS 16、macOS 13、watchOS 10 或更高版本中可用。iOS 17、iPadOS 17、macOS 14 和 watchOS 10.1 更新或更高版本中提供了额外的保护。" | `sec2437264f0` / 2024-05-07 |
| 自动重启 | "自动重启是 iOS 18.1、iPadOS 18.1 和/或更高版本中的一种安全机制"；"IdleRebootAllowed 设置可允许管理设备的管理员打开或关闭自动重启"（iOS/iPadOS 18.4 起）；"自动重启在受监督设备上默认关闭" | `secf5549a4f5` / 2026-01-28 |
| SKP | "SKP 仅在搭载以下 Apple 设计的 SoC 的设备上提供：A11 或后续型号 / S3 或后续型号 / M1 或后续型号" | `secf5549a4f5` / 2026-01-28 |
| 后台安全改进 | "iOS 26.1、iPadOS 26.1 和 macOS 26.1 及后续版本将支持和启用后台安全改进" | `sec87fc038c2` / 2026-01-28 |
| SSV | "在运行 iOS 15 和 iPadOS 15 或更高版本的设备上，iPhone 或 iPad 上的系统宗卷也获得了签名系统宗卷的加密保护。" | `secd698747c9` / 2022-05-01 |
| 安全隔区内存保护引擎 | "从 A11 和 S4 开始……"；"在 Apple A14、M1 或后续型号的 SoC 上，内存保护引擎支持两组临时内存保护密钥。" | `sec59b0b31ff` / 2024-12-19 |

**iBoot 行值得单独注意**：iPhone 与 iPad 的门槛芯片**不同代**（A13 vs A14），且条件是**三重合取**（系统版本 + 对象仅 iBoot + 芯片）。这是手册少见的细分范围句，引用时**不可合并成"A13 或后续"**（§16.1）。

## 12.2 macOS：Apple 芯片与 Intel 的差项

macOS 与 iOS 在这条主线上的差异**不是一句"版本不同"，而是四个方向相反的差项**，每一条都有一手依据：

1. **macOS 缺 PPL/SPTM 的强制签名保护，且是设计选择**（A 级脚注逐字）："页面保护层 (PPL) 和安全页表监控器 (SPTM) 会在 macOS 除外（**因为 macOS 设计允许运行任何代码**）的所有平台上强制执行受信任的签名代码。包括页表保护在内的所有其他安全属性均可用于所有受支持的平台。"——注意该句同时承诺"**其他安全属性均可用于所有受支持的平台**"，所以不能写成"macOS 内存防线整体弱于 iOS"，只能写**这两道页表/签名强制防线在 macOS 缺席**。PPL 页正文另给理由（逐字）："此保护在 macOS 上不提供，因为 PPL 只在所有执行代码都必须签名的系统上适用。"
2. **macOS 用户态随机化在手册中没有对应段落**（A 级缺口）。最接近的逐字句出现在门禁页且**语境是防插件自动加载、不是内存破坏利用**（逐字）："必要时，门禁会**从随机的只读位置**打开 App。这旨在防止自动加载与 App 一起分发的插件。"→ 可支撑"macOS 上存在'随机只读位置'这一装载行为"，**不可**支撑"macOS ASLR 强度等于/低于 iOS"（§16.2）。内核侧唯一可引句来自术语表 XNU 条目（该页**无发布日期标注**）："默认为受信任状态，并强制执行代码签名、沙盒化、授权核对和地址空间布局随机化 (ASLR) 等安全措施。"
3. **Intel 与 Apple 芯片的三处具体差项**：
   - `-allow_stack_execute` 是 **Intel 专属**选项，且逐字涵盖 "This includes pthread stacks"（A 级 `ld(1)`）。
   - `Disable Executable Memory Protection Entitlement` 的后果按架构分叉（A 级 TN3126 逐字）："only has this effect on **Intel** code. For **Apple silicon** code, this entitlement **leaves page protection enabled**, making it equivalent to the Allow Unsigned Executable Memory Entitlement."
   - 共享缓存页表共享粒度按架构分叉（A 级 `CacheLayout.md` 逐字）：arm64 为 **32MB**、x86_64 为 **1GB**，并逐字给出关键结论 "**no matter the ASLR slide, the r/o parts can never be in the same 32MB page table region as a r/w part**"（§2.4）。
   - 源码侧同步提示：`vm_map.c` 的 slide 计算含 x86_64 分支 `1 << (vm_map_is_64bit(map) ? 16 : 8)`，故 **16MB/4MB 是 arm64（iOS）数值**，不可当 macOS Intel 口径（§2.1–2.3）。
   - KASLR 方面：手册 0 命中，但 xnu 源码有 `sysctl_slide`（逐字 "Boolean indicating if KASLR is active"）与 `-disable_aslr` boot-arg（§4.1）——**术语在源码、承诺在缺口**。
4. **arm64e ABI 上限更高、但第三方 arm64e 会被拒载**（A- 级）：`MAX_PREFERRED_VERSION` macOS = V1、非 macOS = V0；第三方 preview-ABI arm64e 的拒载逻辑整体包在 `defined(XNU_TARGET_OS_OSX)` 内（§6.5）。**同一份 arm64e 产物在两个平台上的"允许版本"与"拒载行为"都不同。**

**Rosetta 2 是 macOS 侧独有的第三条路径**（A 级 `secebb113be1`，逐字）：x86_64 映像由"特殊的 Rosetta 转译存根"而非 dyld 接管、"内核仍会根据页出错所在二进制文件中所附的代码签名来验证每个 x86_64 页的代码哈希值"、不匹配则"内核会实施适用于该过程的修复策略"。含义：在 Apple 芯片 Mac 上运行 Intel 代码时，**执行的是转译产物，而页哈希校验仍绑定原始签名的 x86_64 页**——这是"签名校验与真实执行代码不是同一份字节"的一手样本（§10.1、§13.5 符号化时须注意）。

## 12.3 watchOS、Apple tvOS 与 visionOS

**共同范围句**（A 级矩阵总纲逐字）："以下保护措施在运行 Apple 设计的受支持 SoC 的设备上可用，此类 SoC 包括 iOS、iPadOS、搭载 Apple 芯片的 Mac 上运行的 macOS、Apple tvOS、visionOS 和 watchOS。"

**三条各自的一手差项**：

- **watchOS：S 系列 SoC 与 arm64_32 的堆熵差异**。矩阵列名把 S 系列与 A 系列并列（"A12-A14 / **S4-S10**"、"A11、**S3**"），PAC 行 S3 不支持、S4 起支持；APRR 下限句逐字含 S3；SKP 范围句含"S3 或后续型号"；安全隔区内存保护引擎下限含 S4。源码侧 `entropic_bits = 3`（32-bit 路径）对 `7`（LP64）→ **watchOS 系 ABI 变体的用户态堆随机化熵显著更低**（A- 级）。**须保留的缺口**：Apple 一手页**未**正面写"watchOS 使用 arm64_32"，该对应关系只能从 `CPU_SUBTYPE_ARM64_32_*` 与 "supported ARM slices (arm64/armv7k/arm64_32)" 间接支持（§16.2）。
- **Apple tvOS**：可链接平台清单在强制签名页标题逐字出现（"iOS、iPadOS、Apple tvOS、visionOS 和 watchOS"，§10.3），即 Team ID/库校验句覆盖 tvOS；但 **XN 段、PAC 段的逐字主语不含 tvOS**，BlastDoor 范围句也不含 tvOS/Apple tvOS。→ tvOS 的执行权限与 PAC 可用性属**未核实**（不等同于"不支持"）。
- **visionOS**：页标题逐字含 visionOS（`sec15bfe098e`"iOS、iPadOS 和 visionOS 中的运行时进程安全性"）、SSV 与只读句含 visionOS，但 **XN 段落笔只写"iOS 和 iPadOS"**。矩阵总纲含 visionOS，故 PAC/APRR 等按 SoC 可用；SPTM 逐字范围含 visionOS（"iOS、iPadOS、macOS 和 visionOS 中的安全页表监控器 (SPTM) 和可信执行监控器 (TXM)"），**PPL 逐字范围不含 visionOS**（"iOS、iPadOS、visionOS 和 watchOS 中的页面保护层 (PPL)" —— 此处含 visionOS，但**不含 macOS**；引用时逐字为准）。

**本节结论口径**：这三个平台**没有独立的内存防线文档段落**，其强度全部通过"SoC 世代 × 系统版本范围句"间接确定；正文凡涉及它们，应写成矩阵格 + 版本句，并显式标注"平台级正面表述缺失"。

## 12.4 32 位遗留、兼容运行与降级路径

一手证据支持的"降级"共有四类，强度来源各不相同：

1. **32 位/`arm64_32` 路径的随机化与签名**：`machine.h` 逐字列出 `CPU_SUBTYPE_ARM64_32_ALL/V8` 独立 subtype 家族；`stat.h:375` 逐字 "supported ARM slices (arm64/armv7k/arm64_32) contain the same struct"；`machorep.cpp:576` 逐字把 arm64_32 与 arm64 同归 16K 页哈希；`libmalloc` 32 位分支 `entropic_bits = 3`；`vm_map.c` 的 slide 位计算在非 64 位分支退化为 `1 << 8`。**合起来的口径**：**签名侧同页大小，随机化侧熵与 slide 幅度更小，且 32 位路径是栈随机化注释（"ASLR slides stack down by up to 1MB"，`bsd/arm/vmparam.h:16`）所在的分支**（§3.1）。
2. **兼容运行（Intel 代码在 Apple 芯片上）**：Rosetta 2 段（§12.2）逐字给出转译存根、按页哈希校验与"修复策略"含糊后果。**该路径上"执行的字节"与"被校验的字节"不同**，是所有跨架构兼容结论必须带的限定。
3. **PAC 的进程级降级**：`IMGPF_NOJOP` → `disable_user_jop` → `ml_task_set_disable_user_jop()`（A- 级，§6.5）；`ptrauth.h` 逐字 `asia/asda/asdb` 在"disable ABI pointer authentication"的进程中为 **no-op**，而 `asib`（返回地址所在键）**仍强制**（§6.3）。**这是"某些指针保护可关、某些不可关"的官方分工**。
4. **JIT 降级**：未授予 `allow-jit` 时逐字"frameworks that rely on just-in-time (JIT) compilation **may fall back to an interpreter**"（A 级，§7.4）——执行面收窄换性能损失，是**产品级降级**而非内核级。

**未核实项（不得补写）**：32 位 iOS 设备（armv7 系）上的 ASLR 行为、旧版本 iOS 的 slide 幅度、`armv7k` 的具体随机化参数——一手来源本轮均无对应页面，正文只能写"未公布"（§16.2）。

## 12.5 Mac Catalyst 与在 Mac 上运行的 iOS App

**本节主体是一条如实登记的缺口**。本轮在《Apple 平台安全保护》全库、developer.apple.com 文档站与 xnu/dyld/libmalloc/security 四仓中，**未取到任何把"Mac Catalyst"或"在 Mac 上运行的 iOS App"与内存随机化、PAC、库校验绑定陈述的一手句子**（检索词与结果见附录 A.4；相关 slug 探测为 404）。

因此正文的写法是：**不给出该场景的强度结论**，只给出两条**相邻且逐字**的事实，供评审自行判断：

1. **平台宏会改变内核行为**：`XNU_TARGET_OS_OSX` 是 arm64e ABI 上限（V1）与第三方 arm64e 拒载逻辑的**唯一分叉条件**（A- 级，§6.5）。凡产物按 macOS 目标构建，其 arm64e ABI 允许面与加载判定就与按 iOS 目标构建的同一份源码不同——**这是"同一份代码跨目标平台的防护强度差"的一手机制，不依赖任何 Catalyst 文档句**。
2. **entitlement 模型按平台分叉**：六项 Runtime Exception 的 `metadata.platforms` **全部标注 macOS-only**（A 级，§10.4、§6.3 差项记录）；developer 侧**没有**对应的 iOS 版本，说明"可按项关闭"这条路径在 iOS 上不作为开发者可选项存在。**"iOS App 在 Mac 上运行时是否继承 macOS 的例外模型"没有一手页可引**，属缺口。

**禁止写法（登记入 §16.5）**：把 Catalyst/Compatibility 运行环境写成"PAC 失效""ASLR 变弱"或"沿用 iOS 沙盒所以强度相同"的三种流行说法，**都缺一手依据**。可写的最强表述是："Apple 未公布该场景下内存防力的差异；可核对的是构建目标宏与 entitlement 平台标注两处按 macOS/iOS 分叉。"

## 12.6 跨平台同一份代码的防护强度差

把 §12.1–§12.5 汇总成一张"同一份源码、不同目标"的差项表。每格都标注一手出处级别，**空白即缺口**：

| 维度 | iOS/iPadOS（arm64/arm64e） | macOS（Apple 芯片） | macOS（Intel） | watchOS 系（arm64_32） | 依据 |
|---|---|---|---|---|---|
| ASLR 正面描述 | **有**（`sec15bfe098e` 逐字段） | **无对应段落**（仅门禁"随机的只读位置"间接句） | 同左 | 无独立段落 | A |
| 主镜像/加载器 slide 上限 | 16MB / 4MB | —（x86_64 分支不同） | 源码含 `1 << 16` 分支 | — | A-（`vm_map.c`） |
| 共享缓存页表共享粒度 | 32MB | 32MB（arm64） | **1GB**（x86_64） | 32MB | A（`CacheLayout.md`） |
| 用户态堆熵 | `entropic_bits = 7` | 16（x86_64） | 16 | **3** | A-（`libmalloc/src/vm.c`） |
| XN / W+X 例外三条件 | **逐字限定 "iOS 和 iPadOS"** | 未提 | 未提 | 未提 | A |
| PPL / SPTM 强制签名 | 有（PPL 逐字含 iOS/iPadOS/visionOS/watchOS；SPTM 逐字含 iOS/iPadOS/macOS/visionOS） | **逐字除外**（"macOS 设计允许运行任何代码"） | 同左 | 有（PPL） | A |
| PAC 可用性 | A12/S4 起（矩阵） | Apple 芯片支持（总纲句含"搭载 Apple 芯片的 Mac"）；**Intel 无对应格** | — | S4 起 | A（矩阵 + 总纲） |
| arm64e ABI 上限 | V0（`MAX_PREFERRED` 非 OSX 分支） | **V1** | — | — | A-（`machine.h`） |
| 第三方 arm64e 处理 | 无该拒载逻辑（包在 `XNU_TARGET_OS_OSX` 内） | **拒载** preview ABI，platform binary 豁免，boot-arg 可放行 | — | — | A- |
| 可按项关闭的 entitlement | **无（macOS-only）** | 6 项 Runtime Exceptions | 同左（含页保护例外效果差异） | 无 | A |
| 栈可执行选项 | 无对应选项 | 无 | **`-allow_stack_execute`**（Intel 专属） | — | A（`ld(1)`） |
| MIE/EMTE | A19/M5 起 | A19/M5（M5 列） | 不适用 | 未列 | A |
| 页校验的"并非总是" | 手册无条件句，未见例外清单 | **逐字 "macOS doesn't always check code as it's paged in"** | 同左 | — | A（TN3126） |
| 内核随机化 | 手册 0 命中；源码有 `sysctl_slide`/`-disable_aslr` | 同左 | 同左 | 同左 | A 缺口 / A- 源码 |

**这张表能支持的三条结论**（也是 §17.1 的组成部分）：

1. **平台差异主要来自三处结构性分叉，而不是版本新旧**：`XNU_TARGET_OS_OSX` 编译条件、`metadata.platforms` 标注、以及架构（arm64/arm64e/x86_64/arm64_32）自身。**同一 iOS 大版本内的不同设备差异，通常小于 iOS 与 macOS 之间的差异**。
2. **macOS 在"强制签名/页表防线"上少两道，但在"ABI 上限与例外通道"上多一层可配置性**：少的是 PPL/SPTM 的签名强制（逐字"设计允许运行任何代码"），多的是六项 Runtime Exception 与 `-allow_stack_execute`。**净结论不是一个"强/弱"字，而是"macOS 的强度更依赖配置与审计，iOS 的强度更依赖平台默认"**（§11、§15）。
3. **随机化的强度差是可量化的，其余多数是定性的**：本表中仅 slide 上限、堆熵位数、页表共享粒度三行有数字，且**全部来自 A- 级源码**——这与 §5.3 的结论一致：**Apple 不在公开文档里给随机化数值**，凡需量化只能引源码并标注源码语境。

# 13. 可观测性与取证痕迹

## 13.1 崩溃报告里必然存在的字段

**先划一手边界**（两条并列，缺一即失真）：

- **developer 侧**：MetricKit 的崩溃诊断对象给出了**字段级**的官方语义（A 级，见下）。
- **support 手册侧**：全库 179 页对 `EXC_BAD_ACCESS`、`SIGKILL`、`.ips`、`DiagnosticLogs`、崩溃报告**字段名****全部 0 命中**（附录 A）。**Apple 公开手册从未描述过崩溃报告的格式或字段**。
- **格式文档缺失**：developer.apple.com 上**不存在**描述 `.ips` 崩溃报告字段格式的官方文档页；`xcode/retrieving-crash-information-for-your-apps`、`xcode/improving-your-app-s-stability` 等猜测 slug **404**；曾被引用的 `TN2169` 实测标题为 **"High Precision Timers in iOS / OS X"**（修订史仅 2013-03-05），**与崩溃报告无关**——此前"TN2169 = About iOS crash reports"的假设**不成立**（§16.5）。

因此本节只写**有官方字段语义支撑**的内容（A 级，`documentation/metrickit/mxcrashdiagnostic`，Topics「Viewing exception details」逐字）：

> var exceptionType: NSNumber?
> The Mach exception type of the crash.
> var exceptionCode: NSNumber?
> The encoded processor-specific information for the crash.
> var signal: NSNumber?
> The signal associated with the crash.
> var exceptionReason: MXCrashDiagnosticObjectiveCExceptionReason?
> var terminationReason: String?
> The reason the app was terminated as a human-readable string.
> var virtualMemoryRegionInfo: String?
> Information about the region of memory an app accessed incorrectly, resulting in a bad-access crash.
> var callStackTree: MXCallStackTree
> The call stack for the crash.

四点口径纪律：

1. **三个层次在 Apple 一手 API 里是分开的字段**：`exceptionType`（逐字 "**Mach exception type**"）、`signal`（逐字 "signal"）、`terminationReason`（逐字 "as a human-readable string"）。正文与脚本**不得混用**这三层——把"Mach 异常"说成"信号"、或把 termination 文本当异常码解析，都会得出错误结论。
2. **`virtualMemoryRegionInfo` 是唯一把"内存区域"写进官方字段语义的项**，且定义逐字绑定 **bad-access crash**。中文建议表述："错误访问的内存区域信息（对应 bad-access 崩溃）"。这是"随机化/权限类失败在遥测侧有官方落点"的最强一手依据（§14.2）。
3. **`exceptionCode` 官方措辞逐字承认它是按处理器架构编码的**（"encoded **processor-specific** information"）。因此 PAC 失败这类架构特定信息**只能落在这一维**——但 Apple **没有**公开该编码的解析表（§13.2）。
4. **该类整体已弃用**：页面状态逐字 `iOS 14.0–27.2 Deprecated`（iPadOS 14.0–27.2、Mac Catalyst 14.0–27.0、macOS 12.0–27.2、visionOS 1.0–27.2 同），替代指引逐字 "**Use CrashDiagnostic instead.**"。正文凡引用这些字段，须写"截至 2026 该类型标注弃用、迁移至 `CrashDiagnostic`"；**`CrashDiagnostic` 的字段页本轮未取得逐字**（待补），所以不能声称新类型仍保留同一组语义（§16.2）。

同页 See also 逐字列出同类诊断对象（`MXAppLaunchDiagnostic`、`MXCPUExceptionDiagnostic`、`MXHangDiagnostic`、`MXDiskWriteExceptionDiagnostic`，**四者均标 Deprecated**）。MetricKit 索引分组逐字："## CPU and memory metrics (6) — CPUExceptionDiagnostic · PeakMemoryMetric · SuspendedMemoryMetric · MemoryExceptionDiagnostic"、"## Crash and hang diagnostics (3) — CrashDiagnostic · HangDiagnostic"、"## Termination metrics (2)"、"## Call stack data (4)"；框架 Abstract 逐字："Measure your app's performance using **daily** metric and diagnostic reports from **real users**."——**"日报粒度 + 真实用户"两个限定在写任何"生产可见性"结论时必须带上，不可暗示实时**（§13.3）。

## 13.2 异常类型与"撞到哪道门槛"的对应关系

**本节的性质必须置顶声明**：下表是**从官方字段语义 + 源码状态位反推的解释框架（B/C 级），不是 Apple 对"PAC/BTI/页校验失败会呈现为何种码"的承诺**。一手依据情况是：手册 0 命中、developer 无 `.ips` 格式文档、`EXC_PACI`/`PACI`/`PACD` 作为文档条目**命中 0**（§16.3）。

| 撞到的门槛 | 一手可引物 | **一手未给出**的东西 | 本篇允许的写法 |
|---|---|---|---|
| 指针认证（§6） | 手册逐字后果句："验证失败会导致**中止使用签名**"；`exceptionCode` 逐字 "processor-specific" | 具体异常码/子类型名；社区流传的 `EXC_BAD_ACCESS (SIGKILL)` + `PACI`/`EXC_PACI` 在本轮四仓 + developer 文档中**无一手出处** | "PAC 失败会走架构特定的异常路径，但 Apple 未公布其在崩溃报告中的呈现形式" |
| 页级校验（§10.1） | `cs_validate_page(…, int *tainted_p, int *nx_p)`（A-）；`CS_KILL`/`CS_KILLED`/`CS_HARD` 位（A-）；Rosetta 句逐字"内核会实施**适用于该过程的修复策略**" | 手册未给异常类型/崩溃码；`CS_KILLED` 是状态位而非报告字段 | "页校验失败可导致进程被内核杀掉并留下 `CS_KILLED` 状态；其在诊断报告中的字段形式未公开" |
| 执行权限（§7.2/§7.3） | `MH_NO_HEAP_EXECUTION` → `vm_map_disallow_data_exec`（A-）；W+X 三条件（A） | 无一手句说明违反 XN 时的报告形态 | "执行权限违规表现为访问失败；`virtualMemoryRegionInfo` 是官方字段中唯一描述'错误访问内存区域'的项" |
| 分配器/类型分区（§9） | 手册逐字（MTE）"如果密钥不匹配，**App 会崩溃且该事件会被记录**"；`kfree_type` strict free（A 级博客） | **事件名/日志字段名未给**；kalloc_type 未给失败时的报告形态 | "官方承认此类命中会崩溃并被记录，但未命名该记录" |
| 加载期拒载（§6.5） | `os_reason_create(OS_REASON_EXEC, EXEC_EXIT_REASON_BAD_MACHO)`（A- 级源码逐字） | 该 reason 在用户可见报告中的呈现 | "**加载期**失败有内核侧 reason 构造点，属源码级证据" |
| 跨进程读取（§10.5） | `com.apple.security.cs.debugger` 页逐字：拿不到未开 `get-task-allow` 进程的 task port | 失败返回值/错误码文档（`task_for_pid` 无 developer 文档页） | "前置条件失败，但一手未提供错误码表" |
| MTE/EMTE（§8） | 手册三 Subhead + 博客逐字同句 | 标签位数、异常细节、日志名 | 同"分配器"行处理 |

**唯一可以写成"必然存在"的字段集合**只有 MetricKit 那七个（§13.1）。凡在其他文档/实践中见到的额外字段（镜像基址表、寄存器组、`termination` 的结构化码），本篇一律标注为**实践中普遍存在但未在本轮一手页取证**，并作为**取证流程约束**而非事实陈述（§14.2 采取同样处理）。

## 13.3 指标上报与生产侧可见性

生产侧能看到什么，取决于通道粒度。**MetricKit 是日报**（Abstract 逐字 "daily … from real users"），因此：

- **可做的**：按设备机型（对应 §12.1 的 SoC 列）、系统版本、构建号对崩溃做**分组趋势**比较；用 `virtualMemoryRegionInfo`、`callStackTree` 判定"是否落在同一门槛"；用 `MXAppExitMetric` 的 `foregroundExitData`/`backgroundExitData`（A 级，Topics 逐字："The metrics for the foreground app exits" / "…background…"，类型 abstract "counts for the different types of foreground app exits"）观察退出类型计数变化。
- **不可做的**：把日报当实时告警；从日报里读出"PAC 命中的那一条"（无官方字段名）；用 `MXForegroundExitData` 的**逐项字段**做判定——其页面本轮未取得逐字（数据接口 404），**只确认类型名与 abstract**（§16.2）。
- **升级路径**：`MemoryExceptionDiagnostic` 存在于索引分组逐字列表中，但**其属性页未取得**（多次 404/大小写不确定）；`MXCrashDiagnostic` 全家弃用 → 迁移至 `CrashDiagnostic`（其字段页未取证）。**结论：指标侧的"内存类可观测性"一手口径正在换代，正文引用任何具体属性名都必须先复核当前页面状态。**

## 13.4 诊断日志采集与分析通道的边界

用户侧可见的分析通道只有一句一手依据（A 级，`sec87fc038c2`，2026-01-28，逐字）：

> 安装了后台安全改进的设备随后会使用**设备端分析系统**，检查其中一个受影响的应用程序在安装后台安全改进后**崩溃的频率是否有显著增加**。

同页逐字给出共享开关路径（iPad/iPhone/Mac 的"设置 > 隐私与安全性 > 分析与改进 > 共享…分析"），并逐字给出系统版本范围句"iOS 26.1、iPadOS 26.1 和 macOS 26.1 及后续版本将支持和启用后台安全改进"。**这三句能支持的结论**：① Apple 官方承认存在**设备端分析系统与崩溃率统计**；② 崩溃率被用于**验证后台安全改进是否引入回归**——即"防护变更会被崩溃率监测"是一手流程，可直接用于 §15.5 回归监测的正当性；③ 该统计与共享开关相连，属 §14 的上传前提。

**这三句不能支持的结论**：手册**未涉及** `.ips`、字段名、Exception 类型，也**未描述**日志保留期、去标识、转发对象。凡涉及这些的回答都出自其他一手域（App Store  Connect、隐私政策、前序期次取证），本篇不重复取证、不臆造（§14.3）。

**开发者侧的分析通道边界由签名与 entitlement 决定**，一手句可引两条：库校验拒绝时 "**`dyld` provides a detailed error message** when the system prevents code from loading due to library validation"（A 级，§10.3）——即"代码被拒加载"这件事本身有一手承诺的可观测输出；内核侧调试输出 `printf("CODE SIGNING: cs_validate_page: …")` 与计数器 `cs_validate_page_no_hash`/`cs_validate_page_bad_hash`（A- 级）**属内核内部，面向开发者文档未公布**，只能作为"机制存在且可计数"的证据，不能作为可依赖接口。

## 13.5 符号化、偏移还原与隐私含义

**一条一手事实决定了本节的整个逻辑**：随机化改变**基址**，不改变**镜像内部结构**。因此：

- 崩溃现场里真正**可跨设备复用**的信息是"**地址 − 镜像基址**"这个**偏移**，以及**镜像清单**（哪些库被加载、其版本）。§5.5 的观测口径正是由此而来——"地址 − 镜像基址 在不同设备上恒定"是**正常现象**，不能得出"随机化无效"。
- 但反过来：**只要基址泄露一次，偏移集合就还原成绝对地址集合**（§4.2 引官方博客逐字句 "leaking one pointer value gives the attacker the location of a great number of interesting objects"）。这就是 §14.4 的核心命题：**随机化保护的是运行时，不保护已上报的快照。**
- **符号化会扩大逆向面**：符号文件（dSYM 类）本身是**构建产物侧的信息**，它与崩溃偏移组合后，可把"哪个函数 + 哪个偏移"变成可读调用栈；上传符号文件或含符号的报告，等于**主动移交布局解释器**。一手页对"符号文件上传后由谁可见、保留多久"**未在本域取证**（§14.3、§16.2），所以本篇只写机制含义与取舍清单，不写平台承诺。
- **Rosetta 2 场景需额外限定**（§12.2 逐字）：在 Apple 芯片 Mac 上执行的是**转译产物**，而页哈希校验绑定原始 x86_64 签名页。**符号化时"执行现场的地址"与"原始二进制的偏移"不是一一对应**——这是跨架构结论必须带的限定语。
- **平台身份会影响可复用范围**：arm64e 有独立 shared region、且缓存选择按 **team-id 或 entitlement** 切分（§6.5）。因此"某 App 泄露的共享缓存基址"**不必然可直接用于另一个身份分区**；正文应写"缓存布局的可见范围可按身份切分"，**不要**写"每个 App 都有独立随机布局"（后者无一手依据）。

## 13.6 本篇不提供的内容（表达边界声明）

本篇**不提供**下列任何一项，且这不是排版取舍，而是内容边界：

1. **不提供**绕过指针认证、伪造签名、绕过页校验或库校验、削弱随机化配置的任何操作方法或参数组合。§10.4 只列**官方公开的构建选项、签名属性与其后果**，用于配置审计与整改，不给执行步骤。
2. **不提供**利用代码、payload、内存布局构造过程，也不提供任何具体漏洞的利用细节。§4.2/§9.5/§9.6 引用的数字（8%、194/119/90）与残留面名称**全部来自 Apple 官方博客的自述分析**，引用时保留其样本限定，不得扩写为方法说明。
3. **不提供**针对任何具体目标、第三方设备或未授权系统的攻击步骤；§12 的机型与世代矩阵仅用于**采购、治理与兼容性评审**。
4. **不推断**一手文本未给出的量化规格：ASLR 熵位数、PAC 签名位宽、EMTE 标签位数、MIE 性能开销百分比、崩溃报告中 PAC 失败的字段形态（§16.2、§17.2）。
5. **不代答**"某个 App 是否安全"：§10.6 已划清——签名与校验只界定"谁能进入地址空间"，代码质量责任仍在开发者。

**凡涉及"如何降低防护强度"，本篇的口径是：描述官方公开存在的开关、属性及其后果，并把它转化为审计项。**这一句同时是 §11、§15 与 §17.4 共用的评估纪律。

# 14. 布局信息的上传：诊断数据离开设备时带走了什么

## 14.1 三个上传方向在本篇的定义（U1 服务端、U2 iCloud、U3 开发者渠道）

本篇沿用系列口径，把"布局信息离开设备"分成三条**方向不同、可见主体不同、可撤销性不同**的通道。分区的意义在于：**同一份崩溃现场在三个方向上的携带内容并不相同**，所以"上传面"必须逐方向评估，不能合并成一个"数据外泄"结论。

| 方向 | 本篇定义 | 携带物（本篇域内可证部分） | 一手依据强度 | 触发条件 |
|---|---|---|---|---|
| **U1 · 发往 Apple 的分析数据** | 设备按用户同意向 Apple 汇集的**设备端分析**样本与统计 | 手册逐字只承诺"设备端分析系统…检查…崩溃的**频率**是否有显著增加"（`sec87fc038c2`，A 级）；**字段级内容本篇未取证** | **弱**（频率级，非字段级） | 用户侧共享开关（逐字路径："设置 > 隐私与安全性 > 分析与改进"） |
| **U2 · iCloud 与备份** | 用户数据、钥匙串与设备备份离开本机 | **本篇域内未取证**（内存布局快照是否入备份，无一手页） | **缺口** | 用户备份配置 |
| **U3 · 开发者可得的诊断** | App 自身/第三方 SDK 采集并经开发者渠道交付的崩溃与性能诊断 | MetricKit **日报、来自真实用户**（Abstract 逐字，A 级）；`MXCrashDiagnostic` 七字段（含 `virtualMemoryRegionInfo`、`callStackTree`、`exceptionType/Code/signal`、`terminationReason`） | **强**（字段级官方语义） | App 集成 MetricKit 或自建采集；用户同意与隐私声明 |

三点结构性差别，直接决定后续各节的评估方式：

1. **只有 U3 有字段级一手依据**。因此本篇 §14.2 的"携带什么"分析以 U3 为主，U1/U2 只能写成"机制存在、内容未公布"（§14.3）。**把 U1 的字段清单当既成事实来引用，是本主题最常见的越界写法。**
2. **可撤销性不同**：U3 由开发者自己在代码里决定采集范围（可通过不集成、字段过滤、上报前脱敏来控）；U1 由用户开关决定；U2 由用户备份策略决定。**同一个"偏移 + 镜像清单"，在 U3 上是开发者的设计选择，在 U1/U2 上是用户的授权选择。**
3. **保留期与转发对象**：三条方向在本轮取证域内**都没有一手页给出保留期**；手册明确"未涉及 `.ips`、崩溃报告字段、Exception 类型"（§13.4）。凡需要保留期数字，须引其他一手域（隐私政策、数据与隐私页、前序期次取证），本篇**不重复取证、不推算**。

## 14.2 崩溃与事件报告里地址、偏移与镜像清单的角色

以唯一有字段级依据的 U3 为基准，逐字段说明"布局信息含量"：

| 官方字段（逐字定义） | 携带的布局信息 | 随机化对它有效吗 | 脱敏时的取舍 |
|---|---|---|---|
| `callStackTree`（"The call stack for the crash"） | **一串返回地址 / 帧指针**（未符号化即裸地址） | **对基址有效**：跨设备绝对值不同；**对偏移无效**（§13.5） | 保留偏移即可符号化；裸地址一旦集中存放即等于布局库 |
| `virtualMemoryRegionInfo`（"region of memory an app accessed **incorrectly**, resulting in a **bad-access crash**"） | 被错误访问的**内存区域信息**（官方唯一"内存区域"字段） | 该字段本身就在描述"哪块区域"，是**最直接的布局泄露载体** | 是否上报需按 App 类别单独决策（§14.6） |
| `exceptionType` / `signal` / `terminationReason` | 类型/信号/人类可读文本，**不含地址** | 不涉及 | 可安全保留，用于 §13.2 的"撞哪道门槛"判断 |
| `exceptionCode`（"encoded **processor-specific** information"） | 架构相关编码信息（可能承载指针认证类失败的细节） | 未公开解析表 → 含义不确定，**但它不是纯计数** | 谨慎：不要假设它"只是一个数" |
| （实践中普遍存在的）镜像基址表 / 寄存器组 | **每个已加载镜像的基址 + 全部寄存器值** | **随机化的核心保护对象**——但**上报即失效**（§14.4） | 本篇域内**一手未取证**该格式，只作流程约束陈述（§13.2） |

**一条与 PAC 直接相关、常被漏掉的推论**：手册 PAC 表逐字列出"**用户线程状态寄存器 | IA | 存储地址**"（§6.2）。也就是说**线程状态里的寄存器值在架构上是被签名的对象**。崩溃报告若包含寄存器组，它携带的就不是"纯数值"，而是**包含已签名指针的现场**。这带来两点后果：

- **对逆向/取证**：符号化时把带 PAC 签名的寄存器值当干净地址做减法，会得到错误偏移（高位含签名/标签信息）。**这是一条纯机制性的注意项，不涉及任何绕过。**
- **对隐私**：寄存器组属于"可直接参与布局还原"的类别，其上传取舍应与 `callStackTree` 同级评估，而不是当作"额外调试信息"默认携带（§14.6）。

**镜像清单的角色独立于地址**：即使全部裸地址被剥掉，"加载了哪些库、各自哪个版本、按什么顺序出现"仍然是布局信息——它泄露的是**分区结构**（§2.4 的 r/o 与 r/w 永不同页表区域、§6.5 的 arm64e 独立 shared region）。**"删掉地址保留模块列表"不等于脱敏**，这是 §14.6 清单里最容易被误判的一条。

## 14.3 分析数据的存储、转发与保留期口径

**本节的诚实结论是：本篇取证域内无法给出保留期与转发对象。**可写的只有三句一手依据与一句缺口声明：

1. **存在设备端分析系统与崩溃率统计**（A 级逐字，`sec87fc038c2`）："安装了后台安全改进的设备随后会使用设备端分析系统，检查其中一个受影响的应用程序在安装后台安全改进后崩溃的频率是否有显著增加。"
2. **共享是用户侧开关**（同页逐字列出 iPad/iPhone/Mac 的"设置 > 隐私与安全性 > 分析与改进 > 共享…分析"路径）——即 U1 的方向由用户决定，且手册把开关路径公开为可核对项（§11.2）。
3. **后台安全改进的版本范围**（逐字）："iOS 26.1、iPadOS 26.1 和 macOS 26.1 及后续版本将支持和启用后台安全改进。"——它同时说明：**分析数据被用于验证"防护变更是否引入回归"**，这是 U1 通道的官方用途之一（§15.5）。
4. **缺口**：手册 179 页对 `.ips`、`DiagnosticLogs`、"分析日志"、崩溃报告字段名**全部 0 命中**；developer 侧无 `.ips` 格式文档页；`MemoryExceptionDiagnostic` 属性页与 `CrashDiagnostic` 字段页本轮**未取得逐字**（多次 404）。→ **任何"保留 N 天""转发给第三方"的具体数字，不得从本篇推导**；需要时引其他一手域或标为未核实（§17.2）。

另有一条**跨方向适用**的机制性提醒：U3 的粒度是**日报**（Abstract 逐字 "daily"）。**"日报"意味着一次崩溃现场会被组装、排队、延迟发送**，其携带内容在时间上是一个**已凝固的快照**——与 §14.4 的命题直接相关：快照一旦形成，随机化对它不再起任何作用。

## 14.4 上传前后：随机化保护的是运行时，不保护已上报的快照

本节是全章的主线句，也是本篇最容易被忽略的评估角度。

**随机化是运行时属性，不是数据属性。**它的作用机制是"让进程在**这一次运行**中的位置不可预知"（§1.2 的三类必需信息）。而上传产生的是**静态记录**：

- **对上报方（开发者/Apple）**：报告里的基址与偏移组合成一个**已还原的布局实例**。此后它变成一份**文档**，其保密性由存储与访问控制决定，**与设备上的随机化无关**。
- **对同一设备的后续运行**：旧报告里的基址通常**失效**（每次启动重新随机化，§2.5），所以"泄露一次基址"对**下一次**运行的价值有限——**但偏移集合永久有效**（版本属性）。**这就是为什么"报告里没有用"是一种危险的错觉：它的价值不在当前设备，而在同版本的其他设备与未来的逆向。**
- **对同版本的其他设备**：由于随机化只改基址，报告携带的**偏移 + 镜像清单**可直接迁移到任意同版本设备（§5.5 第一行口径）。**一次上报 = 为整个版本贡献一张地图。**
- **对备份恢复/克隆场景**：§5.4 的结论在此反向成立——如果某条路径会让布局跨设备/跨恢复复用，那么**上传的快照与实时布局的相关性会上升**（快照不再是历史，而是可用值）。**这两种情形应分开评估，不能一律写"报告只反映过去"。**

**由此得到本篇对"上传"的评估式（可直接用于 §15.3 第 20 组与 §17.5）**：

> 一份崩溃报告的布局外泄量 ≈ **（是否含裸基址）×（是否含完整镜像清单）×（同版本活跃设备数）×（该报告的可达主体数）×（保留时长）**
> 其中只有前三项可由 App/平台的设计决定，后两项由渠道与政策决定；**随机化对这一整式的影响为零**——它只出现在"是否含裸基址"的分母意义上（基址本来就每次不同）。

这条式的实践含义：**降低 §14.2 的字段集合是纯工程决策，与"是否加强随机化"无关；而"是否上传"是隐私决策，与安全强度无关。**两者混在一起谈，就会得出"少传数据能提升设备安全性"这种不成立的结论（§16.5）。

## 14.5 符号文件上传与逆向面扩大

**符号化是"偏移 → 可读语义"的转换器**。它的隐私与安全含义可以完全用机制描述，不需要任何二手推测：

- **组合效应**：裸偏移单独看只是数字；与符号文件组合后可读为"某函数 + 某行"。**上传符号文件等于同时移交"布局解释器"**，把 §14.4 的"一份地图"升级为"一份带路标的地图"。
- **方向差异**：U3 的符号化通常在**开发者自有系统**内完成（符号文件由开发者掌握），风险主要在企业内部访问控制；一旦使用第三方崩溃平台，符号文件与报告**同时离开**开发者边界——**此时可见主体数这一项发生变化**，而设备侧的防护完全不变。
- **对安全评审的具体含义**：**已公开的、被符号化的崩溃样本会长期指示"哪些位置在这台设备上会坏"**，这既是质量信息，也是攻击面信息。治理文档需要把"符号文件与报告成对可达"的场景**单独列出**，而不是并入一般"第三方 SDK 风险"。
- **与本篇其余章节的衔接**：`dyld` 在库校验拒绝时提供 "detailed error message"（§13.4）是**加载失败**侧的可观测信息；若把这类消息连同镜像清单一起上报，泄露的是"**该进程的可加载边界**"，与偏移无关，同样属于 §14.2 末段所说的"分区结构"泄露。
- **一手边界**：Apple 对本篇域内**未公布**符号文件上传后的可见主体与保留期（§14.3 缺口）。**不得**在治理文档里写"Apple 会在 N 天后删除 dSYM"一类承诺，除非引到该承诺的原始一手页。

## 14.6 隐私与安全评审的取舍清单

以下 8 条按"决策项 → 该问的问题 → 依据"组织，可直接用于评审表：

1. **是否上报裸基址？**——若只需定位代码位置，**偏移已足够**（§13.5：偏移是版本属性）。裸基址的额外价值仅在同一进程实例的调试，风险却覆盖整个版本（§14.4）。
2. **是否上报 `virtualMemoryRegionInfo`？**——它是官方唯一"内存区域"字段（A 级逐字，§13.1），对判定"撞哪道门槛"极有价值，也是最直接的布局载体。**建议按 App 类别分档**：高敏感沟通类（§8.4 博客逐字点名 "social networks, messaging apps, or any other app where a specific user can be targeted"）应默认从窄。
3. **是否上报寄存器组？**——须按 §14.2 的 PAC 表理解："用户线程状态寄存器"是**被签名的对象**，寄存器值含签名指针。默认携带前应确认下游是否会把它当干净地址处理。
4. **镜像清单能否裁剪？**——注意"删地址不删清单"**不等于脱敏**（§14.2 末段）：清单本身暴露分区结构与版本组合。
5. **保留多久、谁能看？**——这两项**不由设备防护决定**（§14.4 评估式的后两项）；若无法给出答案，则该通道应按"永久可达"做保守假设。
6. **是否集成 MetricKit？**——收益是官方字段语义（A 级），代价是"日报 + 真实用户"的默认携带范围（§13.3）；**弃用状态须复核**：`MXCrashDiagnostic` 系列逐字标注 14.0–27.2 Deprecated，指引 "Use CrashDiagnostic instead."（§13.1）。
7. **符号文件与报告的成对可达性**——第三方平台场景需单列风险项（§14.5）。
8. **上传与锁定模式/用户开关的关系**——U1 由用户共享开关决定、U3 由开发者集成决定（§14.1）；治理文档应把两者分开陈述，避免"用户关了共享就安全了"这类跨方向结论。

## 14.7 量化基线：一次上报通常包含多少布局信息

**先声明口径**：下表统计的是"**布局信息单元**"（可参与还原布局的独立项），**不是**字节数、不是概率、也不是成功率；"是否必含"一列只反映**一手依据**与**实践常见度**两种状态，混合标注。

| # | 单元 | 一手依据 | 通常是否包含 | 对还原布局的贡献 |
|---|---|---|---|---|
| 1 | 崩溃线程的调用栈帧（裸地址形式） | A（`callStackTree`） | **是** | 高：一组同镜像偏移，直接给出可执行流 |
| 2 | 每个栈帧所属镜像标识 | A-（实践常见，字段级一手未取证） | **是** | 高：与 #1 组合即得"哪张地图" |
| 3 | 错误访问的内存区域信息 | A（`virtualMemoryRegionInfo`） | 视字段是否为空 | 高：唯一官方"内存区域"字段 |
| 4 | Mach 异常类型 / 信号 / termination 文本 | A（三字段逐字定义） | **是** | 低：不含地址，但决定 §13.2 的归因 |
| 5 | `exceptionCode` | A（逐字 "processor-specific"） | **是** | 中：解析表未公开，含义不可假定 |
| 6 | 全部已加载镜像列表（含基址列） | 一手未取证 | 实践中常见 | **最高**：基址列 = 一次性还原全部镜像 |
| 7 | 寄存器组（含被签名的线程状态） | 手册 PAC 表逐字（"用户线程状态寄存器 \| IA"） | 实践中常见 | 高，且需按含签名指针处理 |
| 8 | 系统版本 + 构建号 + 机型 | A（MetricKit 上下文；矩阵按 SoC，§12.1） | **是** | 中：决定该样本可迁移到哪批设备 |
| 9 | Team ID / 身份分区线索 | A-（缓存按 team-id/entitlement 选取，§6.5） | 间接 | 中：影响共享缓存基址的可复用范围 |

**三条可直接使用的基线结论**：

- **单元 #1 + #2 + #8 的组合，已经足够为"同版本全体设备"生成一张偏移地图**——这也是为什么"报告里没有基址"并不构成脱敏（§14.4）。
- **单元 #6 是唯一的"一次性还原"项**：它的存在与否，决定同一次上报是"一条线索"还是"整张布局"。裁剪优先级应最高。
- **可迁移性有身份边界**：#9 提示共享缓存的可见范围可按身份切分（§6.5、§13.5），因此**不要把"共享缓存基址"当成全局唯一值来统计**；按 App/团队分组估计更符合一手机制。
- **数字类基线仍属缺口**：一次上报的平均字节量、保留期、可见主体数在本篇取证域内**无一手数值**（§14.3）；本表提供的是**单元清单与相对贡献**，不是绝对量。

# 15. 判定链、量化基线与自查清单

## 15.1 七道判定：一个进程当前的抗利用强度

按下列顺序回答，**每一问都必须落到一手依据或明确标注"未知"**；顺序本身有意义——前三问关于"能不能被改写"，第四五问关于"改写之后能不能用"，第六问关于"越出这个进程"，第七问关于"信息会不会自己出去"。

**判定 1 · 镜像是不是随机基址？**
核对：PIE（A 级 `ld(1)` "load a PIE at a random address each time it is executed"）、是否用了 `-mdynamic-no-pic`、主镜像 slide 上限（arm64 iOS 16MB / 加载器 4MB，A- 级 `vm_map.c`）。
判定：无 PIE → 主镜像基址固定，§2.1 整节门槛视为**不存在**。

**判定 2 · 是不是 arm64e，且 ABI 版本在本平台允许范围内？**
核对：产物切片清单（**不是** build setting，§6.4 的 `ARCHS` 静默失效）；cpusubtype 的 `0x0f000000` 版本位是否 `≤ MAX_PREFERRED_VERSION`（macOS=V1 / iOS=V0，A- 级）；运行时能力位 `hw.optional.arm.FEAT_PAuth`（**不查芯片型号**，§6.8 逐字禁止）。
判定：只有 arm64 切片 → 该进程**没有** PAC 用户态保护，§6 全章对其不适用。

**判定 3 · 数据能不能变代码？**
核对：`MH_NO_HEAP_EXECUTION` 是否置位（A- 级 `mach_loader.c:779-783`）、是否有 `IMGPF_ALLOW_DATA_EXEC` 类豁免、是否 Intel + `-allow_stack_execute`（A 级）。macOS 另查六项 Runtime Exceptions 是否含页保护例外。
判定：任一为"能执行数据"→ §7.2 底线在该进程内**不成立**。

**判定 4 · 有没有 W+X 区，以及它是几块？**
核对：JIT 授权（A 级手册逐字三条件：授权存在 / **单个** `mmap` / **随机地址**）；macOS 上 `allow-jit` 与 `allow-unsigned-executable-memory` 的区别（后者"without the restrictions imposed by using the `MAP_JIT` flag"，A 级）。
判定：**注意这不是"越多的例外越糟"的线性判断**——未开 JIT 会退回解释器（A 级逐字），解释路径同样有代价（§7.4）。要判的是"例外块数量与是否受 `MAP_JIT` 约束"。

**判定 5 · 页校验与库校验处于什么模式？**
核对：`CS_HARD` / `CS_KILL` / `CS_RUNTIME` / `CS_REQUIRE_LV` / `CS_FORCED_LV`（A- 级逐字注释）；macOS 侧 TN3126 逐字"macOS doesn't always check code as it's paged in. One key feature of the hardened runtime is that it **opts the process into this checking by default**"；`CS_DEBUGGED`（"or has previously been debugged and allowed to run with invalid pages"）。
判定：**曾被调试 = 页校验被放宽过**，取证与强度评估都必须先排除这一态（§10.2、§11.7 第 6 项）。

**判定 6 · 可加载边界与跨进程边界在哪里？**
核对：库校验两条件（"signed by Apple" 或 "same Team ID"，A 级双源互证）；系统进程逐字只能"链接到随系统本身发布的资源库"；扩展项逐字"在其自己的地址空间运行……无权访问彼此的文件或内存空间"。
判定：这一问决定 §7.5 剩余跳板里"合法代码的合法用法"该由谁负责。

**判定 7 · 布局信息会不会自己出去？**
核对：是否集成 MetricKit（A 级，日报、真实用户）、上报字段集合（§14.2）、裸基址列与镜像清单（§14.7 #6）、符号文件是否与报告成对可达（§14.5）。
判定：**前六问全过，第七问过不了，等于给同版本全体设备持续贡献一张地图**（§14.4）。

**输出格式建议**：七问各答「成立 / 不成立 / 未知（缺哪一手页）」三态，并把"未知"直接写进结论，而不是补一个推测值。

## 15.2 量化基线表

**纪律置顶**：下表只收**一手可逐字引用**的数值；凡"位数/百分比/熵"类无一手出处的项，一律列入表末"缺口行"，**不得用二手补齐**（§5.3）。

| 项 | 值 | 逐字出处 | 级别 | 使用注意 |
|---|---|---|---|---|
| 主镜像 slide 上限（arm64） | **16MB** | `vm_map.c:21919-21944` | A- | 仅 arm64；x86_64 走 `1 << 16` 分支，不可混用 |
| 加载器 slide 上限（arm64） | **4MB** | 同上 | A- | 同上；注释逐字含 "at least 8 bits of independent entropy on 16KB page systems" |
| dyld 共享缓存 slide | arm64 **32MB** / x86_64 **1GB**（页表共享粒度） | `CacheLayout.md` | A | 逐字 "no matter the ASLR slide, the r/o parts can never be in the same 32MB page table region as a r/w part" |
| 共享缓存 slide 的历史量级 | 从 "effectively zero" 到 **"about 512MB"** | `CacheLayout.md` | A | 表述为"文档自述的改进量级"，非当前保证 |
| 32 位路径栈 slide | 最多 **1MB** | `bsd/arm/vmparam.h:16` "ASLR slides stack down by up to 1MB" | A- | **该注释属 32 位路径**，不可当 arm64 LP64 口径 |
| 用户态堆熵位 | LP64 **7** / arm64_32 **3** / x86_64 **16** | `libmalloc/src/vm.c:61-68` | A- | 是"位"但不是"熵位数保证"（无一手换算） |
| 熵位移 | `ENTROPIC_SHIFT 25`（**32MB** PTE 对齐，仅 iOS+64bit） | `vm.c:36-46` | A- | 逐字附注 "align 64bit ARM shift to 32MB PTE entries"（`rdar://22277891`） |
| 熵区间常量 | `ENTROPIC_KABILLION` = **256MB**；`ENTROPIC_USER_RANGE_SIZE` = **8Gb** | `vm.c:36-46` | A- | 常量名是源码命名，不作对外口径 |
| malloc 元数据距离随机化 | guarded range **随机 4M–5M**、carveout **1M** | `vm.c:125-142` | A- | 逐字动机句："randomize the distance of malloc metadata from the fixed mappings" |
| 签名页大小 | **16K**（arm64/arm64_32）/ TN 示例 **4096**（macOS Intel 例） | `machorep.cpp:576`；TN3126 示例 | A- / A | **两处并存，不可择一**（§10.1） |
| 签名标志位 | `CS_RUNTIME 0x10000` ⇔ `flags=0x10000(runtime)` | `cs_blobs.h:54`；TN3126 | A- / A | 数值互证，可放心引用 |
| ptrauth ABI 位域 | 掩码 **`0x0f000000`**（右移 24 位）；V0/V1，`CURRENT_VERSION = V0` | `machine.h:383-402` | A- | macOS `MAX_PREFERRED`=V1、非 macOS=V0 |
| PAC 密钥位数 | **五种 128 位密钥**（内核指令与数据签名） | `sec8b776536b` | A | **不是**签名宽度；手册未给签名位数 |
| 签名载体 | "64 位指针顶部未使用的填充位" | 同上 | A | 载体有描述、宽度无数字 |
| 内核类型签名粒度 | **8 byte granule**；四类具名 + `PAC` 位；`DUAL` 逐字未使用 | kalloc_type 博客 | A | 枚举仍在演进（§9.5） |
| 指针打包 | "packs some object pointers into **32 bits**" | 同上 | A | 属可预测性换检查能力的取舍 |
| SockPuppet 分析样本 | **8%** 单类型重分配成功率；**194 类型 / 119 唯一签名 / 90 签名组** | SockPuppet 博客 | A | **必须带样本限定**：iPhone 13 Pro / iOS 16 beta 1 / `ip6_pktopts`(192B) 一个漏洞（§9.6） |
| MIE 下限 | **A19 和 M5 及后续**；"all models of iPhone 17 and iPhone Air" | 手册 + 博客 | A | SoC 口径与机型口径并存，不可互改 |
| PAC 部署起点 | **2018 年、A12 Bionic**（"first in the industry"） | MIE 博客 | A | 与矩阵"A12-A14/S4-S10 起支持"一致 |
| 分配器时间线 | iOS 15 `kalloc_type` → iOS 17 `xzone malloc` → 2022 EMTE → 2025-09 MIE | 三篇博客 | A | "expanded its use across our systems"（2022）为**会过期**表述 |
| KASAN-TBI 成本 | 16-byte granule / 1 byte shadow / **~8% of memory** / 不支持 x86-64 | `kasan.c:76-84` | A- | **软件仿真**数字，不可当硬件 MIE 成本 |
| 调试授权会话 | **10 小时** | `com.apple.security.cs.debugger` 页 | A | **macOS-only**，不可移植 iOS |
| 栈尺寸默认 | **8MB**（`-stack_size`，须为 4KB 或 16KB 倍数） | `ld(1)` | A | 与 §3.2 线程栈默认大小的语境分开 |
| `__PAGEZERO` | **4KB** / **4GB** | `ld(1)` | A | 哨兵，不是随机化项 |

**缺口行（必须原样列出，不能用任何数字补齐）**：iOS 用户态 ASLR 熵位数；arm64 LP64 栈 slide 上限；mmap 区域熵；PAC 签名位宽；EMTE 标签位数/色数；MIE 性能开销百分比；内核随机化（KASLR）的任何官方量化；崩溃报告 `.ips` 字段格式；一次上报的平均数据量与保留期；macOS 用户态 ASLR 的官方描述段落。

## 15.3 二十二条自查清单

分七组，每组对应 §15.1 的一道判定，可直接抄进评审模板。**每条后面的括号是判定依据章节**。

**A 组 · 构建产物（4 条）**
1. 产物切片清单是否含 `arm64e`（以及需要时的 `arm64e.x1`），且 cpusubtype 的 ptrauth 版本位在本平台允许区间内（§6.4、§6.5）。
2. 是否覆盖了 `ARCHS` 导致 PAC 开关静默失效——**这一条不能靠 build setting 判断**（§6.4 逐字 "Has no effect if ARCHS has been overridden"）。
3. 主镜像是否 PIE；是否残留 `-mdynamic-no-pic`（§2.1、§10.4 第 11 项）。
4. header 是否置 `MH_NO_HEAP_EXECUTION`；`__PAGEZERO` 是否存在且尺寸符合预期（§7.2、§2.1）。

**B 组 · 依赖与加载（4 条）**
5. 每个第三方 dylib/framework 是否独立核对加固状态——**库校验只保证签名身份，不保证加固开关**（§11.1）。
6. 依赖库是否满足两条件之一（Apple 签名 / 同 Team ID）；是否存在必须关闭库校验的场景（§10.3）。
7. 是否依赖 `DYLD_*` 注入（macOS；且受 SIP 保护的二进制忽略全部变量）（§10.4 第 6 项）。
8. 是否使用了共享缓存定制路径（按 team-id/entitlement 选取独立 shared region）——若是，须重估基址可复用范围（§6.5、§13.5）。

**C 组 · 执行与例外（4 条）**
9. 是否存在可执行数据路径；是否 Intel 平台且带 `-allow_stack_execute`（§7.2、§12.2）。
10. 若需要 JIT：是走 `MAP_JIT` 还是走"绕过 `MAP_JIT` 限制"的例外？两者强度不同（§7.3）。
11. macOS 六项 Runtime Exceptions 是否逐项给出书面理由；含 `disable-executable-page-protection` 者应判为**需整改**（官方逐字 "an extreme entitlement"、"Don't do that!"）（§10.4）。
12. 未开 JIT 时的降级行为是否被测试过（逐字 "may fall back to an interpreter"）（§7.4、§9.4 第 3 条）。

**D 组 · 运行时与设备（3 条）**
13. 特性判定是否全部使用 `hw.optional.arm.FEAT_*` / commpage 位，而**非** `hw.cpufamily`（§6.8 逐字禁止）。
14. 目标机型在 §12.1 矩阵中位于哪一列（PAC / APRR / PPL / SPTM / MIE 逐行）；结论是否带"按 SoC 而非按版本"的限定（§11.5）。
15. 是否存在 boot-arg / spawn 标志级别的关闭（`-disable_aslr`、`_POSIX_SPAWN_DISABLE_ASLR` 的 fork 继承、setuid 强制重开、`_POSIX_SPAWN_ALLOW_DATA_EXEC`）（§4.1、§7.2）。

**E 组 · 用户与治理（3 条）**
16. 是否引导用户开启锁定模式，并确认关键网站/App **没有**被排除（排除即逐字"不再受 WebKit 相关限制的约束与影响"）（§7.4、§11.2）。
17. 受监督设备的 `IdleRebootAllowed` 状态（自动重启在受监督设备上默认关闭）（§11.3）。
18. 恢复方式建议是否包含"抹掉后新装"以获得 §2.5 的"每次安装"级重随机化（§5.4）。

**F 组 · 分配器与代码质量（2 条）**
19. 高风险组件是否评估过 typed allocator / hardened libc++ / bounds-safe C++ / stack zero init 的启用状态（§6.4、§9.1）。
20. 是否存在依赖"堆布局可预测"的实现（对象复用顺序假设、缓存行假定）——这类代码会**主动放弃**随机化收益（§3.7）。

**G 组 · 观测与上传（2 条）**
21. 上报字段集合是否裁剪过裸基址列与完整镜像清单；`virtualMemoryRegionInfo` 与寄存器组是否按 App 类别分档（§14.2、§14.6、§14.7 #6）。
22. 符号文件与崩溃报告是否会同时到达第三方；MetricKit 类型弃用状态是否复核（`MXCrashDiagnostic` 逐字 14.0–27.2 Deprecated → "Use CrashDiagnostic instead."）（§13.1、§14.5）。

## 15.4 代码评审与构建审计要点

**优先级原则**：§11.1 已给出结论——强度绝大多数在构建期定死。因此审计顺序应为 **产物 → 签名 → 配置 → 代码 → 遥测**，而不是从代码走查开始。

1. **产物核对（最高优先，且唯一能发现静默失效）**：切片清单与 cpusubtype（含 `0x0f000000` 版本位）、header flags（PIE、`MH_NO_HEAP_EXECUTION`）、`__PAGEZERO`、`LC_UUID`（注意 `-random_uuid` 与安全无关，§5.5）。**建议进 CI 断言，而不是人工抽查。**
2. **签名核对**：`flags=0x10000(runtime)` 一类标志位；entitlements blob（其自身由 Slot -5 哈希保护，§10.1）；六项 Runtime Exceptions 是否存在、是否有书面理由；`get-task-allow` 是否残留于提交构建（A 级逐字：Xcode 调试构建自动加入、**提交前移除**）。
3. **构建设置核对**：以 `ENABLE_ENHANCED_SECURITY` 伞形开关为基线（逐字含 pointer authentication / typed allocator support / hardened C++ standard library / 安全警告），**逐项确认没有被单独关闭**（"These settings can be disabled individually"）。
4. **依赖审计表**：逐库记录"签名身份 + 加固状态"两列——**不可合并**，因为库校验只管前者（§11.1、§10.3）。
5. **架构分叉检查**：`disable-executable-page-protection` 在 Intel 与 Apple 芯片后果不同（逐字 "leaves page protection enabled" on Apple silicon）；凡按 entitlement **名字**判定的脚本须按架构分支（§7.3、§12.2）。
6. **平台可移植性检查**：六项 Runtime Exception 与 Gatekeeper/notarization/10 小时会话全部 **macOS-only**；iOS 侧的评审项不得从 macOS 文档移植（§10.4、§12.6）。
7. **代码评审落点**：PAC 覆盖不到的指针类别（block 对象指针本身、"private ABI rules" 项，§6.2/§6.7）；同类型 UAF（§9.6 第 3 行）；分配内部越界（MIE 逐字可存活，§8.3）；依赖布局的假设（§3.7）。**这四类是"缓解不覆盖"的一手清单，评审时按类打标签比按严重度排序更有用。**
8. **遥测与隐私联审**：按 §14.6 八条走；把"上传字段裁剪"列为安全工程任务而非合规任务，因为 §14.4 显示它同时影响逆向面与隐私面。
9. **禁止的评审写法**：以"PAC/PIE 开关为 true"作为通过依据；以芯片型号推断特性；把 `CS_DEBUGGED` 状态下的样本当生产态；把 8% / 194-119-90 当通用常数；在 iOS 语境引用 macOS-only 例外模型。

## 15.5 回归监测：升级、换机、换约定之后必须重测的项

**背景依据**：Apple 官方用崩溃率验证防护变更是否引入回归（A 级逐字，`sec87fc038c2`：设备端分析系统检查"崩溃的频率是否有显著增加"）——**这条流程本身说明"改防护会影响运行表现"是官方认可的因果关系**，所以升级后不重测加固结论，等于放弃了官方流程中对应的那一半。

| 触发事件 | 必测项 | 依据章节 |
|---|---|---|
| **iOS/iPadOS 大版本升级** | §12.2 版本类范围句逐条重核（锁定模式、BlastDoor、SSV、后台安全改进、自动重启）；分配器覆盖面是否变化（逐字 "expanded its use across our systems" 是**会过期**表述） | §11.4、§12.1、§9.6 第 9 行 |
| **换 SoC 世代（采购/换机）** | §12.1 矩阵所在列整体平移；重点：PAC 是否从 ✗→✓（A11→A12）、PPL/SPTM 是否互换（A14→A15）、MIE 是否出现（A19/M5） | §11.5、§12.1 |
| **Intel → Apple 芯片迁移** | `-allow_stack_execute` 不再适用；页保护例外语义变化；共享缓存页表粒度 1GB→32MB；堆熵 16→7 | §12.2、§12.6 |
| **改用 arm64e / 新增 arm64e.x1 切片** | 独立 shared region 生效、缓存基址可复用范围变化；第三方 arm64e 在 macOS 的拒载判定与 ABI 版本上限 | §6.5 |
| **切换构建目标宏（iOS ↔ macOS、含 Catalyst 类场景）** | `XNU_TARGET_OS_OSX` 分叉带来的 ABI 上限差；entitlement 平台标注差；**Catalyst 场景本身无一手页 → 视为需实测** | §12.5、§12.6 |
| **开启/关闭 `ENABLE_ENHANCED_SECURITY` 或其子项** | PAC、typed allocator、hardened libc++、警告四项**分别**复核；不可只看伞形开关 | §6.4、§15.4 第 3 条 |
| **上报字段集合变更** | §14.7 单元清单重算，特别是裸基址列与镜像清单；`MXCrashDiagnostic` 家族弃用与 `CrashDiagnostic` 迁移后的字段差异（**新类型字段本轮未取证，必须实测**） | §13.1、§14.2、§14.7 |
| **分配器/内存安全相关系统组件更新（含后台安全改进）** | 崩溃率分组比较（官方自身做法）；`virtualMemoryRegionInfo` 命中率变化；同裸地址复现的分组方式（§5.5 三尺度） | §13.4、§13.1 |
| **锁定模式策略变化（用户新增排除项）** | 网页类执行面的收窄是否失效（逐字："相关网站将不再受 WebKit 相关限制的约束与影响"） | §7.4、§11.2 |
| **MDM 策略变化** | `IdleRebootAllowed`；受监督默认关闭状态是否改变内存中密钥清除窗口 | §11.3 |

**监测指标的四条纪律**：① 一律按"系统版本 + 构建号 + SoC 列"三元组分组；② 模拟器/被调试过的进程/Exclave 目标排除在基线外（§9.4、§4.5）；③ 偏移比对优先于裸地址比对（§13.5）；④ 引博客数字必须带样本限定（§9.6）。

# 16. 口径冲突、证据缺口与相反结论

## 16.1 术语与口径冲突 C1–C13

| # | 冲突 | 一侧口径（出处） | 另一侧口径（出处） | 本篇处置 |
|---|---|---|---|---|
| **C1** | "随机化"的对象 | 位置/基址（§2.1 手册中文句"随机安排"） | **距离/间隔**（A- `vm.c:125-142` "randomize the **distance** of malloc metadata from the fixed mappings"） | 分开写"随机化位置"与"随机化距离"，不合并成一句 |
| **C2** | 密钥数量与命名 | 手册**五种** IA/IB/DA/DB/GA（A，`sec8b776536b` 表"键"列） | `ptrauth.h` **四个 ABI 键** asia/asib/asda/asdb（A-）；**GA 不在其中** | 并列给出两套命名 + 映射，明确 GA 只在手册内核语境出现 |
| **C3** | 用户态 A 类密钥归属 | 手册只承诺"每个用户空间进程都有其自己的 **B 类**密钥"（A） | `ptrauth.h` 定义 `asia` 为 **process-independent**（A-），未说明用户态如何管理 | 写作"手册对用户态 A 类密钥未作逐字说明"，不推断共享性 |
| **C4** | 特性判定依据 | 矩阵按**芯片型号**分列（A，`sec8b776536b`） | `machine.h` 逐字**禁止**按 family 推断特性（A-："you should never try to deduce whether or not some feature is available based on the family"） | 治理语境用矩阵、程序判定用 `hw.optional.arm.FEAT_*`；两条并存不合并 |
| **C5** | "guarded" 一词 | 源码用 **guarded page** 指 BTI 强制页（A- `ARM_PTE_GP`） | 本篇 §3.3 保护页、§3.4 libmalloc **guarded range**（随机距离区间） | 三义当场定义，禁止跨章复用 |
| **C6** | 页大小 | TN3126 示例 `Page size=4096`（A，macOS Intel 例） | 签名实现 arm64/arm64_32 默认 **16K** 页哈希（A- `machorep.cpp:576`） | 双值并登，标注语境；iOS 按 16K 理解 |
| **C7** | 页校验的强制程度 | 手册中文无条件句"所有可执行内存页…会在载入时进行"（A，限定 iOS/iPadOS/visionOS） | TN3126 "**macOS doesn't always check** code as it's paged in"（A） | 按平台分别陈述，不写"全局每页强制" |
| **C8** | 栈 slide 数值 | `vmparam.h` "up to **1MB**"（A-，**32 位路径**） | arm64 LP64 侧**无对应一手注释** | 明确标注为 32 位路径；LP64 写"未公布" |
| **C9** | 即时编译的措辞 | 手册用"**即时编译**"，JIT 缩写全库仅 2 页命中（A） | 开发者文档用 "JIT"/"just-in-time"（A，entitlement 页） | 引用时保留各自原词，不统一 |
| **C10** | 锁定模式限制的内容 | 支持文章逐字"**某些复杂的网页技术**会被阻止"（A） | 手册逐字确认 Safari 用 W+X 例外页 + APRR 点名即时编译（A） | 链条写作推论并标注"Apple 未逐字定义复杂网页技术＝JIT" |
| **C11** | MTE 的定位 | 手册/博客："硬件协助**查找**内存损坏错误的工具"（A） | 同页逐字 "EMTE 修复了导致 MTE **无法成为主动防护手段**的漏洞"（A） | 分层写：MTE=检测、MIE=强制；不写"iOS 有 MTE 所以拦越界" |
| **C12** | 库校验的强制来源 | macOS：Hardened Runtime 默认开、可用 entitlement 关（A） | iOS：手册逐字 Team ID 段，无"开发者可关"表述；**无"iOS 强制库校验"正向句**（A 缺口） | 分别成段；禁止把 macOS 例外模型移植到 iOS |
| **C13** | PPL 的"不支持"格 | 矩阵 A15-A18/M2-M4/A19-M5 列 PPL ✗（A，图标语义） | 同页逐字 "SPTM（与 TXM 结合使用）**取代了 PPL**"（A，脚注 1） | 连列名 + 脚注一起引，禁止单抽一格当"防线消失"证据 |

## 16.2 一手页缺失清单 N1–N24

**均为"本轮在指定一手域内未取到"，不等于"不存在"**；除注明者外，不得用二手补齐。

| # | 缺失项 | 检索域 |
|---|---|---|
| N1 | iOS 用户态 ASLR 的**熵位数**（含任何量化） | 手册 179 页 + developer 文档站（`aslr`/`entropy` 位数类词 0 命中） |
| N2 | **内核**地址随机化（KASLR）的任何官方描述 | 手册 `KASLR`/`slide` 0 命中；术语表 XNU 句仅说"强制执行…ASLR" |
| N3 | macOS 用户态随机化的**对应段落** | 手册无；最接近仅门禁"随机的只读位置"（语境为防插件自动加载） |
| N4 | arm64 LP64 **栈 slide 上限** | 源码只有 32 位路径的 1MB 注释 |
| N5 | **mmap 区域**熵/上限 | 无一手句 |
| N6 | 每次启动是否**必然**重随机化（用户态各对象） | 手册未表述；博客仅对 kalloc_type 分桶逐字 "random for every boot" |
| N7 | **PAC 签名位宽** | 手册只给密钥 128 位 + "指针顶部填充位"载体 |
| N8 | **EMTE 标签位数/色数** | 手册与博客均未给；仅 KASAN-TBI 的 16B/1B 仿真口径 |
| N9 | **MIE 性能开销百分比** | 只有定性句（"same or better performance"、"minimal performance impact"） |
| N10 | PAC 校验失败的**异常码/报告字段** | 手册 0 命中；developer 无 `.ips` 格式文档页；`PACI`/`EXC_PACI` 作条目 0 命中 |
| N11 | **`.ips` 崩溃报告字段格式**官方文档 | developer 文档站路径探测 + 站内搜索未命中；猜测 slug 404 |
| N12 | `CrashDiagnostic`（`MXCrashDiagnostic` 替代品）**字段页** | 本轮未抓取（多次 404/大小写不确定） |
| N13 | `MemoryExceptionDiagnostic` **属性** | 同上，未取到逐字 |
| N14 | `MXForegroundExitData` **逐项字段** | 数据接口 404，仅确认类型名与 abstract |
| N15 | `-mbranch-protection` / `pac-ret` / `bti` 的 **Apple 取值文档** | build-settings 全文 + 四仓 **0 命中** |
| N16 | **BTI / GCS 的手册条目** | 手册 `BTI`/`branch target`/`GCS`/`shadow stack` **0 命中** |
| N17 | MTE/GCS 作为**一等特性标志** | `arm_features.inc`（57 条）与 `cpu_capabilities*.h` 位图 0 命中 |
| N18 | "watchOS 使用 **arm64_32**"的正面表述 | 一手页无；只能由 subtype + 32 位路径间接支持 |
| N19 | **Mac Catalyst / 在 Mac 上运行 iOS App** 与内存防线的绑定陈述 | 手册 + developer + 四仓均未取到 |
| N20 | **A 系/M 系 × PAC/BTI/EMTE 支持矩阵**文档页 | 无表格；唯一型号句是博客 "PAC in the A12 Bionic chip" 与 "all models of iPhone 17 and iPhone Air" |
| N21 | "iOS 强制开启库校验"的正向句 | developer 无；iOS 侧只有手册 Team ID/强制签名段 |
| N22 | `task_for_pid` / `vm_read` / `vm_read` **文档页** | developer 无文档页；语义仅来自 `com.apple.security.cs.debugger` 页（macOS） |
| N23 | `GuardMalloc` / `MallocGuardEdges` / `MallocStackLogging` **文档页** | developer 命中 0，仅源码 env 名（**预期无一手页**：Instruments 帮助已迁出） |
| N24 | 分析数据的**保留期/转发对象/可见主体**；U2（iCloud）方向是否携带布局快照 | 本篇取证域无一手页；须引其他一手域 |

## 16.3 检索命中为 0 的负面结论汇总

**两个来源集合，分开记账**（合并会掩盖"文档未写"与"代码未实现"的区别）：

**集合一：《Apple 平台安全保护》网页版全 179 页**（大小写不敏感全文，页面发布日期最新 2026-01-28，修订记录页最新条目"2026 年 3 月"）——**0 命中词**：`BTI`、`branch target`、`GCS`、`shadow stack`、`KASLR`/`kaslr`、`slide`、`malloc`、`zone`、`guard`（含 `guard page`）、`canary`/`stack canary`、`armv8.3`/`ARMv8.3`、`branch-protection`、`PACI*` 助记符、`EXC_BAD_ACCESS`、`SIGKILL`、`.ips`、`DiagnosticLogs`、崩溃报告字段名；`JIT` 仅 2 页命中、`MTE` 仅 1 页命中（均已在正文引用处标注）。

**集合二：apple-oss-distributions 四仓 main tip + developer 文档站**（xnu `f6217f89`/`xnu-12377.121.6`、dyld `fd8d0c4d`/`dyld-1378`、libmalloc `c49dafa2`/`libmalloc-812.100.31`、security `db15acbe`/`Security-61901.120.67`）——**0 命中项**：`GCS`/`guarded control stack`（唯一 `GCs` 是 "Suitable during GCs in LISP"）；`MTE`/`memtag` 于 `arm_features.inc` 与 `cpu_capabilities.h`；`-mbranch-protection`/`pac-ret`/`bti`/`memory tagging`/`arm64_32` 于 build-settings 页全文（同页 `arm64e` 2 命中、`pointer authentication` 4 命中）；`aslr`/`"21 bits"`/熵位数类词于 developer 文档站；`.ips`/`DiagnosticLogs`/`crash report format` 于 developer 文档站；`task_for_pid`/`vm_read` 文档页；`Malloc*` env 文档页；`-random`（`ld(1)`，只有 `-random_uuid`）。

**三条方法学结论**：
1. **必须写成"未收录/未公布"，不得写成"未实现/不存在"**（集合一与集合二的区别正是这条）：Apple 手册**未收录** BTI/GCS/KASLR 三词 ≠ Apple **未启用** BTI/GCS——事实上 xnu 构建变量逐字 "Enable BTI by default for all ARM64 targets"。
2. **术语缺席处往往是"实现级证据更强"**：随机化数值、页表位、分配器熵全部来自源码（A-），而手册只给定性句。**这不构成"源码更权威"的许可**——源码注释（尤其含 "preview ABI"、"private ABI rules" 者）同样不是稳定规格承诺。
3. **`TN2169` 假设已证伪**：实测标题为 "High Precision Timers in iOS / OS X"（修订史仅 2013-03-05），**与崩溃报告无关**。此前把 TN2169 当"About iOS crash reports"引用的做法不成立（§13.1、§16.5 第 14 条）。

## 16.4 三簇必须并读的页面

单页引用会失真，以下三簇必须整体读：

**簇一：ASLR ↔ XN/W+X ↔ APRR（三处同讲"内存损坏"，目的语几乎相同）**
`sec15bfe098e`（ASLR 段 + Execute Never 段，2024-12-19）+ `sec8b776536b`（APRR 段与 PAC 段，2026-01-28）。
**为什么必须并读**：ASLR 页与 PAC 页都用"（有助于）防止对内存损坏错误的利用"这一目的语（§4.1 已标注），单看任一页都会误认为另一页可省；并读才能看出官方把**位置不可预知**与**指向可验证**列为两道不同门槛。而 APRR 句"因为内存无法在读写的同时有效执行"是**第三道**（执行目的）的硬件表述，且它解释了 W+X 例外为何必须"单个 mmap + 随机地址"。**三处合起来才是 §0.2 的三道正交门槛模型。**

**簇二：库校验 ↔ 强制签名 ↔ 研究设备（"谁能进入地址空间"的正反面）**
`sec7c917bf14`（强制签名 + Team ID，2024-12-19）+ `seca7ff718d2`（安全性研究设备，2022-05-01）+ `sec2437264f0` / 支持文章 `105120`（锁定模式，2024-05-07 / 2026-09-18）。
**为什么必须并读**：第一页给出"只能链接平台库或同 Team ID"的强约束；第二页逐字说明 Apple **变更执行策略时走的是 iBoot 变体 + 启动内核集**、且"无需破坏或取消激活平台安全功能"；第三页给出用户可主动收窄、也可逐项撤回的通道。并读才成立本篇 §11 的核心论点：**"放宽防线"在 Apple 模型里是启动链/身份层的事情，不是运行时配置项。**

**簇三：kalloc_type ↔ SockPuppet ↔ MIE 三篇博客（官方口径的自我修正链）**
`towards-the-next-generation-of-xnu-memory-safety`（2022-10-27）+ `what-if-we-had-sockpuppet-in-ios16`（2023-05-23）+ `memory-integrity-enforcement`（2025-09-09）。
**为什么必须并读**：第一篇**批评随机化**（fragile、一次指针泄露、堆喷压回稳定布局）；第二篇给出**随机化替代方案的实测局限**（8% 可靠性上限、signature collisions / data submap / within-type UAF 仍是目标）；第三篇承认 **PAC 自身在原实现中被投机侧信道攻破过**并引出 MIE。只读任一篇都会得出偏向结论：只读第一篇像"随机化已废弃"，只读第二篇像"分区已足够"，只读第三篇像"硬件已解决"。**三篇并读才支持本篇的评估口径：多道正交门槛叠加，且每一道都有官方承认的边界。**

## 16.5 与流行口径相反的十八条

| # | 流行说法 | 一手依据的反驳 |
|---|---|---|
| 1 | "iOS ASLR 有 21 位熵" | 无任何一手出处（N1）；源码只给 slide **上限**与 `entropic_bits`，**不做位数换算** |
| 2 | "KASLR 未被 Apple 实现" | 手册未收录（N2）**≠** 未实现；源码有 `sysctl_slide`（逐字 "Boolean indicating if KASLR is active"）与 `-disable_aslr` |
| 3 | "共享缓存基址每次启动随机，所以泄露无用" | slide 上限、页表共享粒度、以及**按 team-id/entitlement 分区**共同决定可复用范围；且 `CacheLayout.md` 逐字 r/o 与 r/w **永不同 32MB 页表区域**（§2.3、§6.5） |
| 4 | "开了 PAC 就防不住 ROP 了" | 手册逐字只说"**提高了许多攻击的难度**"，并点名 ROP；`ptrauth.h` 逐字承认部分键在降级进程中为 **no-op**（§6.3、§6.6） |
| 5 | "PAC 不保护虚表" | 手册表逐行列出"虚函数表条目 (IA)"与"虚函数表指针 (DA)"（§6.2） |
| 6 | "PAC 签名 16 位，暴力可破" | 签名位宽**未公布**（N7）；手册只给"128 位密钥"与"指针顶部填充位"两处描述 |
| 7 | "arm64e 是稳定 ABI，可按文档长期依赖" | Apple 逐字：V0 是 "a preview ABI"，"**future OSes may break ABI**"（§6.5） |
| 8 | "所有二进制都自动带 PAC" | 官方形态是"**追加 arm64e 切片**"，且覆盖 `ARCHS` 时**静默失效**；手册主语限于"系统软件和内建 App"（§6.1、§6.4） |
| 9 | "进程关闭指针认证后返回地址也不再被签" | `asib`（返回地址所在键）逐字 "**enforced even in processes which disable ABI pointer authentication**"（§6.3） |
| 10 | "macOS 只是少了 iOS 的沙盒，内存防线相同" | 差项具体且方向明确：**PPL/SPTM 逐字除外 macOS**（"macOS 设计允许运行任何代码"）、六项例外为 macOS-only、`-allow_stack_execute` 为 Intel 专属、页校验"macOS doesn't always check"（§12.2、§12.6） |
| 11 | "BTI 没被提到所以没开" | xnu 构建变量逐字 "Enable **BTI by default** for all ARM64 targets"；但 auxKC 有**官方承认的空洞**（§7.1） |
| 12 | "MTE 能防住越界，所以内存安全已解决" | 手册逐字承认 MTE 原本"**无法成为主动防护手段**"；博客逐字承认 **intra-allocation buffer overflow 可存活 MIE**（§8.1、§8.3） |
| 13 | "GCS/影子栈是 iOS 现有机制" | 手册与四仓 **0 命中**（N16）→ 不得归入现有机制 |
| 14 | "按 TN2169 可查 iOS 崩溃报告字段" | TN2169 实测是 "High Precision Timers in iOS / OS X"（§16.3 第 3 条） |
| 15 | "崩溃报告里没有基址就安全了" | **偏移 + 镜像清单**已足以给同版本设备生成地图（§14.7）；随机化不保护已上报快照（§14.4） |
| 16 | "`-random_uuid` 是安全措施" | `ld(1)` 逐字：只影响 LC_UUID 生成方式，且官方指出哈希式 UUID 对**可复现构建**更重要（§5.5） |
| 17 | "关闭 JIT 只有好处（少一块 W+X）" | 未授予 `allow-jit` 时逐字"may **fall back to an interpreter**"，或崩溃/行为异常——是**交换**不是纯收益（§7.4） |
| 18 | "越狱/研究设备 = 关掉 ASLR 与 PAC" | 研究设备逐字"**无需破坏或取消激活平台安全功能**"，策略变更载体是 **iBoot 变体 + 启动内核集**，生产设备无法启动（§11.5） |

## 16.6 必须加限定语才能写出的表述

| 想写的句子 | **不能**这样写 | 必须这样写（限定语加粗） |
|---|---|---|
| iOS ASLR 强度 | "有 N 位熵" | "公开手册确认用户态 ASLR 随机安排各内存区域、可执行代码、系统库与相关编程结构，**但未公开其熵值、熵源与是否每次启动必然变化**" |
| 内核随机化 | "Apple 未做内核 ASLR" | "手册**未收录** KASLR/slide 术语；**源码侧**存在 `sysctl_slide` 与 `-disable_aslr`，属实现证据而非官方承诺" |
| PAC 覆盖率 | "iOS 全面启用 PAC" | "手册主语为**系统软件和内建 App**；第三方代码需自行产出 arm64e 切片，且**博客口径为'可开始测试'**（Enhanced Security settings）" |
| PAC 强度 | "签名不可伪造/不可探测" | "**官方承认**原实现中投机执行 PAC 指令暴露时序差异，可用于隔离合法签名（MIE 设计阶段修复）" |
| 页校验 | "每页强制校验" | iOS 侧手册为无条件句；**TN3126 逐字 "macOS doesn't always check"**，且 Hardened Runtime 才默认 opt-in |
| 库校验（iOS） | "iOS 强制库校验" | "iOS 侧可引的是手册强制签名 + Team ID 段；**developer 文档无'iOS 强制开启 library validation'正向表述**" |
| 锁定模式 | "锁定模式禁用 JIT" | "官方措辞是'**某些复杂的网页技术**会被阻止'并归因 WebKit 相关限制；**'复杂的网页技术＝JIT'这一步 Apple 未逐字定义**" |
| MTE/MIE | "越界会被硬件拦下" | "MTE 被官方定位为**检测**工具；EMTE 修复其'无法成为主动防护'的缺陷；MIE **限 A19/M5 及后续**且措辞为'内核等**关键攻击面**'" |
| SockPuppet 数字 | "UAF 成功率降到 8%" | "**8% 与 194/119/90 是针对 SockPuppet 一个漏洞、iPhone 13 Pro / iOS 16 beta 1 一个样本的分析值**" |
| kalloc_type 覆盖 | "内核全部类型已分区" | "逐字 'iOS 15 首次引入、今年**扩大了使用范围**'——**采用范围随构建变化，不可当版本常量**" |
| 共享缓存泄露影响 | "泄露一处即全线泄露" | "slide 上限与页表共享粒度决定复用面；**arm64e 有独立 shared region 且缓存可按 team-id/entitlement 选取** → 应写作'分区问题'" |
| 分配器随机化 | "堆地址每次随机" | "guarded range 随机化的是**元数据与 `__DATA` 的距离**（4M–5M + 1M 缺口）；`entropic_bits = 7`（LP64）/`3`（arm64_32）是**源码常量，不是官方熵承诺**" |
| 崩溃归因 | "这条崩溃是 PAC 命中" | "**Apple 未公布 PAC 失败在崩溃报告中的呈现形式**；本报告对应关系表为解释框架（B/C 级）" |
| 上传保留期 | "Apple 保留 N 天" | "本篇取证域**无一手页**；须引其他一手域或标为未核实（N24）" |
| 机型支持 | "A13 及以后都有 PAC" | "矩阵按 SoC 列：**PAC 下限 A12/S4**；且源码逐字禁止用 `hw.cpufamily` 推断特性 → 程序判定用 `hw.optional.arm.FEAT_*`" |
| 缓解效果 | "开了这些就不用修漏洞" | "官方策略句是'使利用**本质上不可靠**'（inherently unreliable）、'更难复用'；**签名与校验不保证代码质量**（§10.6）" |
| iBoot 内存安全 | "iOS 14 起系统内存安全" | "三重合取：iOS/iPadOS 14+、**对象仅 iBoot**、**iPhone A13+/iPad A14+（门槛不同代）**；措辞为'以下类中的**大多数**漏洞'" |
| 研究设备 | "研究设备关闭防护" | "逐字'**无需破坏或取消激活平台安全功能**'，变更在 iBoot 变体与启动内核集实施且生产设备无法启动" |

# 17. 结论与建议

## 17.1 一手证据能支持的结论

下面十九条是本篇**唯一**认为可以直接写进对外文档、评审结论或整改单的句子。每条后面给出支撑它的证据档位与章节，超出这个范围表述就必须加限定语（见 §16.6）或改写成缺口（见 §16.2）。

1. **Apple 对手册口径下的地址随机化只给了定性描述，没有给任何比特数。** 逐字表述是"随机安排所有内存区域"与"随机安排可执行代码、系统库和相关编程结构的内存地址"（A 级，`sec15bfe098e`，2024-12-19）。凡是给出"N 位熵"的写法都不是 Apple 一手口径（§2.6、§5.3）。
2. **随机化的官方收益表述是反批量复用，而不是防单点利用。** 逐字："随机安排内存地址增加了执行攻击的难度，尤其是对多个设备的攻击"（A 级，同页）。这句决定了 §15.5 回归监测为什么必须跨设备而不是单设备（§0.2、§16.5）。
3. **手册把 ASLR 明确归为内核强制执行的安全措施。** 术语表 XNU 条目逐字：内核"默认为受信任状态，并强制执行代码签名、沙盒化、授权核对和地址空间布局随机化 (ASLR) 等安全措施"（A 级，`sec93292bfa6`，该页无发布日期标注）。它支持"ASLR 由内核实施"，**不**支持"内核镜像自身被随机化装载"（§1.2 手册侧）。
4. **可写且可执行的页存在，但被三道并列条件约束。** 逐字要点：需要 Apple 专有的动态代码签名授权、只有单个 `mmap` 调用、系统为这类页**分配随机地址**；Safari 的 JIT 在该页被点名（A 级，`sec15bfe098e`）。这三条是**合取**，任缺其一就不成立（§7.2、§7.3）。
5. **按线程撤销执行权限是一条硬件原语，且有明确的可用世代起点。** 逐字口径为"自 Apple A11 仿生和 S3 后的 SoC"提供快速权限访问限制，理由是"因为内存无法在读写的同时有效执行"（A 级）。这是本篇少见的、由手册直接给出的**世代门槛**（§7.3、§12.1）。
6. **指针认证在手册里被定义为"防止对内存损坏错误的利用"，且系统软件与内建 App 在用。** 逐字（A 级，`sec8b776536b`，2026-01-28）。它同时是**覆盖面承诺**（系统软件与内建 App）而非全量承诺——第三方代码要自己争取（§6.1、§6.4）。
7. **被签名的对象是一份可枚举的清单，而数据指针本身不在其中。** 手册给出 14 行"项目—密钥—额外数据"表：返回地址用 IB、函数指针用 IA、块调用函数、块描述符 DA+0xC0BB、ObjC 方法缓存 IB、`isa` DA+0x6AE1、`super` DA+0xB5AB、选择器实例变量 DB+0x57C2、只读类数据指针 DA+0x61F8、C++ 虚表条目 IA、虚表指针 DA、计算的 Goto 标签 IA、内核线程状态 GA、用户线程状态寄存器 IA（A 级）。**签的是"往哪儿跳/往哪儿指"，不是"往哪儿写数据"** ——这是 §6.2 与 §6.7 的分工线。
8. **签名用到五种 128 位密钥，其中用户空间进程的 B 类密钥每进程独立。** 逐字（A 级，手册 PAC 页 + `ptrauth.h` 注释）。这决定了"跨进程复用一段签名指针"不是天然可行（§6.3）。
9. **认证失败的后果由官方措辞写成"会导致中止使用签名"**，且**签名存在 64 位指针顶部未使用的填充位**里（A 级逐字）。两条合起来给出一个可审计推论：指针被截断、被重新构造或被搬到不带该填充位的表示里，签名就不存在（§6.6、§6.7）。
10. **`asib` 这一条是覆盖差的官方承认形式**：注释逐字说它"enforced even in processes which disable ABI pointer authentication"，其余密钥在那类进程里是空操作（A- 级，`ptrauth.h`）。**"关不掉的那一类"与"可以关掉的那一类"在同一份官方文件里被区分开**，这是评审时最应该引用的一句（§6.4、§10.4）。
11. **构建侧存在会改变切片与强度的官方选项，且它们的效果被文档明确限定。** 例如 `ENABLE_POINTER_AUTHENTICATION` 逐字"Adds an additional architectural slice (arm64e) … Has no effect if ARCHS has been overridden"（A 级）。**覆盖 `ARCHS` 会让这个开关静默失效**——这是 §15.3 清单里最容易漏的一项。
12. **不能从 `hw.cpufamily` 推断指令集特性。** 官方头文件明确禁止这一推断，能力项要走 `hw.optional.arm.FEAT_*`（A- 级，`machine.h`）。任何"按机型号判断是否支持某防护"的实现都属于官方点名禁止的写法（§11.5、16.1 C4）。
13. **按需页校验（page-in checking）是被手册与技术说明同时确认的机制。** 技术说明逐字："check each page as it's loaded into memory"，并明确它通常是**惰性的**、且"macOS doesn't always check…Don't do that!"；同一文档还写明页哈希槽位的取值差异与"这些不算 API"（A 级，TN3126）。→ **已签名不等于已校验**，校验时机取决于平台与配置（§10.1）。
14. **库校验的规则是"Apple 签名的，或同一 Team ID 的"，系统可执行文件因无 Team ID 而只能加载系统库。** （A 级，`sec7c917bf14`）。这是判断"一个进程能不能被塞进任意动态库"的**唯一一手依据**（§10.3）。
15. **削弱这些边界的例外通道存在，但按平台分档。** 六项 `com.apple.security.cs.*` 例外只见于 macOS 硬化运行时文档，逐项有官方后果描述（A 级，§10.4）；iOS 侧不存在同名清单。**把 macOS 的例外表当 iOS 的能力表用是常见错误。**
16. **调试类授权会把一个进程变成跨进程内存读取通道，且授权有时效。** 手册逐字描述调试器与 `task_for_pid()` 的关系，并给出会话时长口径（A 级，§10.5）。这属于**配置问题**，不属于平台弱点。
17. **分配器加固是有公开一手出处的独立一层。** 内核类型化分配页逐字："ASLR is often criticized because leaking one pointer value…"，并说明喷洒可以抵消随机化、类型化分配以 8 字节粒度对齐、每次启动随机（A 级，Apple 官方博客 2022-10-27）。**这条博客把"泄露一个指针"与"随机化被抵消"直接挂钩**，是本篇 §9 与 §14 之间的桥（§9.3、§14.4）。
18. **用户态分配器确实把元数据与 `__DATA` 拉开距离，且实现方式可复核。** 逐字：`guarded range`（4M–5M 的 `PROT_NONE` 带）、随机页对齐使用 `arc4random_uniform`（A- 级，libmalloc 源码）。**注意 `#error ASLR unhandled on this platform`**：该路径在部分平台上不存在，模拟器与 Mac 的结论不可外推到真机（§9.5、§4.5）。
19. **崩溃与诊断侧的可观测性是分层的：Mach 异常类型、处理器相关的异常码、信号、终止原因、错误访问的内存区域，各占一个字段。** MetricKit 的字段定义逐字（A 级，`MXCrashDiagnostic`），且该类型截至本篇日期整页标注弃用、须迁移 `CrashDiagnostic`。这决定了取证口径：**"地址 − 镜像基址"跨设备恒定是正常现象，不是随机化失效**（§13.1、§13.5）。

## 17.2 一手证据不能支持的结论

以下十四类说法在公开文本里找不到支撑，评审中若出现应当作**未经证实**处理。它们与 §16.2 缺口登记、§16.3 零命中清单一一对应。

1. **任何地址随机化的比特数、熵源与是否每次启动必然变化。** 手册通篇没有量化数值；`slide` 命中 0、`KASLR` 命中 0（§1.3、§5.3）。
2. **"内核文本每次启动被随机重定位"这一断言的 Apple 一手版本。** 手册唯一的正面依据是把 ASLR 归给内核执行（结论 3），**不能**由此推出内核镜像自身的装载位置随机化。内核侧说法只能标为源码/社区口径（§4.1）。
3. **"ASLR slide 的熵来自内核 CPRNG"。** 随机数生成页确认 CPRNG 存在，但该页未出现"随机化""ASLR""地址"任一词（§1.3、§5.2）。**两条事实之间的这根线是推断，不是承诺。**
4. **PAC 认证失败在崩溃报告里呈现为何种异常码/子类型。** 社区流传的 `PACI`/`EXC_PACI` 等在本篇抓取的一手仓库与文档中**无出处**；Apple 未公布 `exceptionCode` 的解析表（§4.3、§13.2）。
5. **"某机型/某版本关闭了某项防护"。** 零命中只能读成"公开文本没有表述"，读不出"没启用"（§0.4 纪律 3）。
6. **分支目标识别的产品承诺。** 开源构建配置可证 `BTI_BUILD = 1` 一类默认值、`ARM_PTE_GP` "guarded page"、以及 auxKC 例外逐字 "branches to these pages are unchecked!"（A- 级），但**手册没有面向产品的 BTI 段落**，且 `CAP_BIT_FEAT_BTI` 存在而 `kHasFeatBTI` 不存在（§7.1、§11.5）。
7. **"内存标记扩展覆盖了全部内存访问"。** 官方与源码口径都限定在**被标记的**内存上，分配内越界（intra-allocation overflow）在标记口径下仍可存活（§8.4、§16.5）。
8. **"MIE 只影响性能，不影响利用难度"或反之。** 官博给出的量化是开销侧（`~8% of memory` 属 KASAN-TBI 口径，与 MIE 不是一回事），强度侧没有量化承诺（§8.2、§15.2）。
9. **"锁定模式禁用全部即时编译"。** 逐字只支持"某些复杂的网页技术会被阻止"，且提供按站点/按 App 排除；排除本身被官方注明会削弱保护（§7.4）。
10. **"iOS 上存在硬化运行时例外授权"。** 六项 `com.apple.security.cs.*` 是 macOS 口径（§10.4）。
11. **"符号化后的偏移稳定 ⇒ 随机化无效"。** 偏移是版本属性，随机化只改基址（§13.5、15.5 回归表）。
12. **"MetricKit 能实时看到某台设备的崩溃"。** 逐字口径是**日报粒度、来自真实用户的聚合**（§13.3）。
13. **"存在一份描述 `.ips` 崩溃报告字段格式的官方文档"。** 检索未命中；`.ips`/`DiagnosticLogs` 作为 developer 文档条目命中 0（§13.4、§16.3）。
14. **"Rosetta 2、Mac Catalyst 或模拟器上的结论可外推到 iOS 真机"。** 模拟器被排除在分配器 ASLR 路径外、macOS 无 PPL、macOS 页校验策略与 Apple 芯片不同（§4.5、§9.5、§10.1、§12.5）。

## 17.3 给应用与系统开发者的建议

按"能改的层"排序：五层模型里只有二进制自身这层完全在团队手里（§0.3）。以下 14 条均为可直接执行的检查，不含任何削弱防护的操作。

1. **确认交付切片而不是确认开关。** 检查产物里是否真的存在 arm64e 指令与 `__AUTH` 段，而不是检查构建参数里写了什么。`ENABLE_POINTER_AUTHENTICATION` 在 `ARCHS` 被显式覆盖时**逐字无效**（§11.1、结论 11）。
2. **把依赖库当成本团队的一部分。** 第三方静态库与自带二进制分发（SDK、C++ 依赖、脚本引擎）常常停在旧切片上；一个进程强度取决于它加载的最弱那份代码的签名/校验边界（§10.3、§15.4）。
3. **审计"为便利而长期开启"的例外通道。** 需要可写可执行内存的自研解释器要对照 §7.3 的三道并列条件，并把它作为**评审项而非默认项**长期跟踪；能用官方即时编译通道解决的，不要自己申请。
4. **不要在自己的构建里保留调试类分配器与随机化干预的环境变量。** 这类名称在源码里可查到，但它们属于调试路径；生产配置里出现即视为缺陷（§9.5、§15.4）。
5. **核查库校验的实际设置。** 若业务确实需要加载非本 Team 动态库，应记录为**明确的、被批准的例外**，而不是靠关掉校验过关；例外带来的后果要写进风险登记（§10.3、§10.4）。
6. **核查调试授权。** 生产构建与生产配置描述文件不应携带允许跨进程内存访问的能力；该能力一旦存在，等价于给设备上任意内存读取开了通道（§10.5）。
7. **采用编译器侧的可选加固。** 官方公开的构建项包括栈清零初始化、C++ 边界安全缓冲区、硬件检查指针算术切片等；逐项评估兼容性后再开，并记录**未开启项与理由**（§11.1）。这些是官方文档中的构建选项，不是第三方补丁。
8. **区分 `__DATA` 与只读/共享映射的假设。** 只读映射、共享映射与页级校验耦合（§2.4）；跨进程共享的只读内容不会因为"在我这个进程里随机"而变得不可预测。
9. **不要按机型号做能力判定。** 走 `hw.optional.arm.FEAT_*`；`hw.cpufamily` 推断是官方头文件点名禁止的写法（结论 12）。
10. **不要在 CI 里用模拟器或 Mac 结果代替真机结论。** 分配器与页校验路径在这两类环境上不成立（§4.5、§9.5）。
11. **把版本升级当回归事件。** 指令集约定（V0/V1）、平台二进制判定、共享缓存布局、分配器实现换代都可能改变强度基线；每次系统大版本与工具链升级后按 §15.5 重测。
12. **建立"随机化不可依赖"的编码约束。** 类型混淆、双重释放、分配内越界这三类缺陷，随机化与标记都不必然拦住（§6.7、§8.4、§9.2）；把它们列进静态检查与代码评审条目。
13. **把诊断数据当敏感产物治理。** dSYM、映射表与崩溃上报里的镜像清单组合后等于把布局还原能力交给服务端（§14.5）。
14. **对外承诺只用可交付的句式。** 参见 §17.1 与附录 C.4 三条合格句式，避免"我们的 App 有 ASLR/PAC 保护"这类把平台默认能力当产品卖点的表述。

## 17.4 给安全评审与漏洞处置的建议

1. **归因到层，再谈严重性。** 每个内存破坏类缺陷应先落到 §0.3 五层之一：是硬件世代不支持（采购问题）、内核实现（平台问题）、还是本团队二进制未带必需切片（可整改）。同一缺陷在不同层的原因，处置责任人与时限完全不同。
2. **不要因为"设备有 PAC/有 MTE"降级。** 这两道防的是**指向篡改**与**标记不匹配**，对纯读取原语、对分配内越界、对未标记内存、对不含签名的数据指针都不必然有效（§6.7、§8.4）。§16.5 里已把"因缓解存在而降低优先级"列为与公开口径相反的表述。
3. **不要因为"有 ASLR"降级。** 官方博客逐字承认"泄露一个指针值"即可削弱随机化，喷洒可以抵消（§9.3）。**信息泄露 + 内存破坏**的组合必须一起评。
4. **区分"平台默认强度"与"目标实际强度"。** 判定链（§15.1）七问按目标二进制与目标机型逐项回答，任何一项无法取证就写"未取证"，不写"具备"。
5. **给"覆盖差"留位置。** 同一份代码在不同指令集变体、不同世代、不同平台上的防护并不相同（§6.8、§12.6）。多平台产品的评审结论应分开写，不做合并承诺。
6. **注意可关与不可关的分界。** `asib` 一类在禁用 ABI 指针认证的进程里仍被强制，其余不是（结论 10）。处置报告里凡写"该进程未启用 PAC"，必须同时写清**哪一类指针因此失去校验**。
7. **把"例外通道"当发现项。** 需要可执行内存、加载非本 Team 库、持有调试类授权，是三条应当在评审单上单独成项的配置（§7.3、§10.3、§10.5）。
8. **不要接受"公开文本没写 ⇒ 该机制不存在"的论证。** 零命中登记的意义是提示取证方向（如 BTI、GCS），不是给出否定结论（§16.3）。
9. **不要接受"偏移稳定 ⇒ 随机化无效"的论证。**（§13.5、§17.2 第 11 条）
10. **把缺口写成缺口。** 处置文档里凡涉及熵值、PAC 失败异常码、内核随机化，引用 §16.2 对应 N 项编号，避免把二手资料当依据。
11. **修复优先级仍以缺陷本身为准。** 缓解机制改变利用成本，不改变可达性与影响面；把缓解当"补偿控制"写进风险接受需要明确批准（§17.1 结论 2）。

## 17.5 给崩溃取证与遥测治理的建议

1. **用偏移比对，不用裸地址比对。** 跨设备的"地址 − 镜像基址"恒定是版本属性（§13.1、§13.5）。同一缺陷在不同设备上的判定应基于符号化偏移集合。
2. **按三个尺度分组统计。** 每次启动 / 每次安装 / 每台设备（§2.5）。大量同类崩溃落在**同一裸地址**复现，只提示"同一启动会话内复用"或"该路径未走随机化"，不能直接得出熵不足（§15.5）。
3. **区分异常层次。** 报告里 Mach 异常类型、处理器相关异常码、信号、终止原因是四个不同字段（§13.1、结论 19）。写"这是 SIGSEGV"和写"这是 EXC_BAD_ACCESS 且携带错误访问内存区域信息"不是一回事；后者才对应 bad-access 口径。
4. **不要把指标通道当实时告警。** MetricKit 逐字是日报、来自真实用户（§13.3）；用它做"某次攻击正在发生"的判断不成立。
5. **弃用类型要按弃用处理。** `MXCrashDiagnostic` 一类截至本篇日期整页标注弃用、指向 `CrashDiagnostic`（§13.1）。遥测管线若仍解析旧字段，须记录迁移状态。
6. **明确"PAC/标记失败在报告里长什么样"目前无法从一手文档断言。** 相关解析表未公开（§13.2、§17.2 第 4 条）。取证规则里凡依赖具体异常码字符串的判定，须标注为经验口径并可回退。
7. **治理符号文件的存放与可见范围。** dSYM 与镜像清单一旦和服务端上报数据同处一个可达域，等价于把偏移→源码的还原能力一并交出（§14.5）。
8. **在隐私说明里区分三条去向。** 服务端、iCloud、开发者渠道（U1/U2/U3）的可见主体与保留口径不同，其中只有 U3 有字段级一手来源（§14.1、§14.3）。对外文案不要合并成"上传给 Apple"。
9. **记住"随机化保护运行时，不保护已上报的快照"。** 上报完成后，地址与偏移就是一份静态数据，受存储与访问控制约束，不再受 ASLR 约束（§14.4）。
10. **保留"分析数据开关"的用户侧事实。** 用户可关闭诊断数据共享，这会影响取证样本量，应写进数据可用性预期而不是当成异常（§11.2）。
11. **注意采集通道自身的边界。** 诊断日志的官方字段格式文档未公开，任何解析器实现都属于逆向工程产物，需要版本回归测试（§13.4）。

## 17.6 给采购与机型治理的建议

1. **把硬件世代当准入门槛。** 五层里"硬件世代"这一层用户与开发者都改不了；本篇能确认的世代门槛包括：按线程撤销执行权限自 A11 仿生与 S3 之后的 SoC（结论 5）、增强型内存标记相关口径落在 A19/M5 及后续、指针认证的代际差异见 §6.8 与 §12.1。采购标准应引用这些**页内逐字口径**，不引用二手评测。
2. **建立"机型 × 机制"矩阵并留空。** 手册与官方矩阵并非每项都有值；空格表示"公开文本未表述"，不得填"不支持"或"支持"（§12.1、§16.3）。
3. **区分"能装"与"有防护"。** 32 位遗留、兼容运行与降级路径下，部分执行防护与认证机制的覆盖不同（§12.4）。仍在服役的旧机型应单独一条风险说明，而不是并入整体基线。
4. **注意 macOS 与 iOS 的差项。** 页校验策略、PPL 是否存在、硬化运行时例外授权只在哪一侧存在（§10.1、§12.2、§10.4）。混用会导致把 Mac 上的宽松当成移动端的基线。
5. **把锁定模式当作可由组织下发的强度开关。** 它收紧即时编译类网页技术、且排除项会削弱效果（§7.4、§11.3）；治理上应规定**排除需审批并留痕**。
6. **管理企业分发与描述文件。** 组织侧通过配置描述文件与授权属性可以改变一个进程的强度（§11.3）；这类变更应纳入变更管理，而不是长期停留在临时配置里。
7. **规划升级窗口。** 指令集约定与实现换代（分配器、内核区检查）在版本间变化，旧构建产物在新系统上的强度并不自动提升（§15.5、§11.4）。
8. **不要把预算花在"买更强的缓解"叙事上。** 随机化与认证的公开收益是抬高成本与反批量复用（结论 2），不是消除缺陷；采购文档应与代码侧整改（§17.3）成对出现。
9. **建立退役判据。** 当某世代的硬件门槛不再满足组织基线（例如缺少按线程执行权限撤销的原语）时，触发替换而不是靠配置弥补——这一层是五层里唯一无法用软件补的（§0.3、§11.5）。

# 附录 A 来源清单

本附录登记本篇**全部**一手来源的取证粒度：页/文件/行、页面内发布日期、逐字取用要点、正文引用位置与证据档位。凡标注"未取逐字"的条目即为缺口，正文不得引用其措辞。检索语料与检索方式见 A.4。

## A.1 《Apple 平台安全保护》手册页逐页登记

主语料为 `https://support.apple.com/zh-cn/guide/security/` 页面族（TOC 解析出 179 个 secid，逐页下载全部 HTTP 200）。下表为本篇**实际引用**的页面。

| secid | 页面（中） | 页面内发布日期 | 本篇取用的逐字要点 | 引用位置 | 档位 |
| --- | --- | --- | --- | --- | --- |
| `sec15bfe098e` | iOS、iPadOS 和 visionOS 中的运行时进程安全性 | 2024-12-19 | 「地址空间布局随机化」小节三句（随机安排所有内存区域／随机安排可执行代码、系统库和相关编程结构／"增加了执行攻击的难度，尤其是对多个设备的攻击"）；「Execute Never 功能」整段；可写可执行页三条件合取句与 Safari JIT 点名；"Apple 安全内存分配器"名称；操作系统分区只读装载句 | §0.2、§2.1、§2.6、§3.1、§7.2、§7.3、§9.1、§16.5 | A |
| `sec8b776536b` | 操作系统完整性 | 2026-01-28 | 「指针认证代码」段与 14 行"项目—密钥—额外数据"表；五种 128 位密钥、"每个用户空间进程都有其自己的 B 类密钥"、"64 位指针顶部未使用的填充位"、"会导致中止使用签名"；「快速权限访问限制」整段（"自 Apple A11 仿生和 S3 后的 SoC"、"因为内存无法在读写的同时有效执行"）；MTE／EMTE／"标记保密性强制"；锁定模式相关句；操作系统功能矩阵 | §6.1–6.8、§7.3、§8.1–8.4、§12.1、§16.1、§17.1 | A |
| `sec87716a080` | 与上页内容一致的概览页 | 2026-01-28 | 指针认证条目（与 `sec8b776536b` 逐字一致，作交叉印证） | §6.1、§12.1 | A |
| `seccd5016d31` | 系统安全性概览（含指针认证措辞） | 2026-01-28 | "指针认证"命中页之一，用于确认该词的官方中文写法与所在章节 | §6.1、§0.5 | A |
| `sec93292bfa6` | 术语表 | **页面内无发布日期标注** | XNU 条目逐字（"默认为受信任状态，并强制执行代码签名、沙盒化、授权核对和地址空间布局随机化 (ASLR) 等安全措施"）；ASLR 词条 | §0.5、§1.2（手册侧）、§17.1 结论 3 | A |
| `sec30d8d9ec1` | 内存安全性（iBoot 实施） | 2021-02-18 | "缓冲区溢出／类型混淆／释放后重用"三词唯一命中页；iBoot 工具链级内存安全改造与"堆数据与其元数据分开"口径 | §8.5、§9.1、§12.4 | A |
| `secf49cad4db` | 可执行内存页在载入时校验 | 2024-12-19 | 按需页校验整段 | §10.1、§2.4 | A |
| `sec7c917bf14` | 载入时对所有链接库做签名验证 | 2024-12-19 | Team ID 规则句（仅平台库或同 Team ID；系统可执行文件无 Team ID）；库校验定义与 dyld 错误消息、门禁附加检查 | §10.3、§17.1 结论 14 | A |
| `secebb113be1` | Rosetta 2 | 2021-02-18 | 转译存根与 `dyld(1)` 对照句；按页哈希校验与出错时机 | §10.1、§12.2、§17.2 第 14 条 | A |
| `secd698747c9` | 系统宗卷／系统分区只读与运行时完整性 | 2022-05-01 | 只读装载与签名系统宗卷相关句；SSV 与"macOS 设计允许运行任何代码"注脚 | §2.4、§10.1、§12.2 | A |
| `secabd3504cd` | 扩展项运行环境 | 2024-12-19 | "在其自己的地址空间运行……无权访问彼此的文件或内存空间" | §10.3、§14.6 | A |
| `secd3c881cee` | 消息处理与 BlastDoor | 2024-05-07 | BlastDoor 沙盒与内存安全验证相关句 | §7.2（沙盒总纲）、§1.1 | A |
| `sec5599b66df` | macOS 运行时隔离与门禁 | 2024-12-19 | "必要时，门禁会从随机的只读位置打开 App"（防插件自动加载口径，**非**内存破坏利用口径）；PPL 不存在于 macOS 的对照 | §0.5、§1.2（手册侧）、§12.2、16.1 C13 | A |
| `sec2437264f0` | 锁定模式 | 2024-05-07 | "某些复杂的网页技术会被阻止"、按站点/按 App 排除、排除削弱保护句 | §7.4、§11.2、§17.2 第 9 条 | A |
| `sec59b0b31ff` | 安全隔区 | 2024-12-19 | 安全隔区内存保护引擎相关句 | §1.1、§10.2 | A |
| `secf5549a4f5` | 密钥管理与保护 | 2026-01-28 | 密钥从内存擦除、SKP 场景相关句 | §1.1、§10.2 | A |
| `secb010e978a` | 数据保护 | 2024-12-19 | 数据密钥可用性相关句 | §10.2 | A |
| `seca0c73a75b` | 随机数生成 | 2024-12-19 | 内核 CPRNG 为内核与用户空间提供安全随机数；**该页未出现"随机化／ASLR／地址"任一词** | §5.2、§17.2 第 3 条 | A |
| `seca7ff718d2` | Apple 安全性研究设备 | 2022-05-01 | 研究设备放宽项中与内存/执行防护相关句 | §11.2、§15.4 | A |
| `sec87fc038c2` | App 分析与崩溃相关页 | 2026-01-28 | "堆／栈／崩溃"命中页；设备端分析系统句（不给出字段名） | §13.3、§13.4 | A/B |
| `sec114e4db04` | 系统安全性概览 | 2024-12-19 | 纯导航页，**无 ASLR 措辞**（登记为排除项） | §1.2 | A（负面） |
| `sec31e483abf` 等 | Wi-Fi／蓝牙／UWB 隐私页与面容 ID 页 | 各页页内标注 | "随机化"命中但属网络/传感隐私；"此 56 位随机偏移量"是 TSF 偏移量 | §1.4 注意排除项 | A（排除） |

支持文章另计：《关于"锁定模式"》`https://support.apple.com/zh-cn/105120`，页面标注 **2026-09-18**（与手册页体系不同源，引用时注明为支持文章）——§7.4、§11.2。
文稿修订记录页 `secb82d6b274`（无发布日期字段）最新条目 **"2026 年 3 月"**，用作"本手册 web 族内容截至 2026 年 3 月修订"的依据；PDF 版次与 web 族的一致性本篇**未核对**，登记为缺口（§0.4 纪律 2、§16.2）。

## A.2 developer 文档、技术说明与官方博客逐页登记

| 来源 | URL 归属 | 日期口径 | 本篇取用的逐字要点 | 引用位置 | 档位 |
| --- | --- | --- | --- | --- | --- |
| Xcode Build Settings Reference | developer.apple.com | 页脚 © 2026（记为"截至 2026 可用"） | `ENABLE_POINTER_AUTHENTICATION`（"Adds an additional architectural slice (arm64e) … Has no effect if ARCHS has been overridden"）、`ENABLE_HARDWARE_CHECKED_POINTER_ARITHMETIC_SLICE`（arm64e.x1）、`ENABLE_ENHANCED_SECURITY`（伞形、"disabled individually"）、`CLANG_ENABLE_STACK_ZERO_INIT`、`ENABLE_CPLUSPLUS_BOUNDS_SAFE_BUFFERS`；同页 `arm64e` 2 命中、`pointer authentication` 4 命中，`-mbranch-protection`/`bti`/`MTE` 等 0 命中 | §6.4、§10.4、§11.1、§16.3 | A |
| TN3126 Inside Code Signing: Hashes | developer.apple.com/library/archive | 修订史已逐条抄出 | 每页哈希、"check each page as it's loaded into memory"、惰性校验、"macOS doesn't always check…Don't do that!"、Intel 与 Apple 芯片差异、槽位 -1/-3/-5、`flags=0x10000(runtime)`、Page size=4096、"aren't considered API" | §10.1、§16.1 C6、§17.1 结论 13 | A |
| TN3125 / TN3127 / TN3161 | 同上 | 修订史逐条抄出；**正文分段细节未逐字取得** | 仅据其标题与结论级口径引用，未逐字引用正文 | §10.2、§10.5 | A（部分） |
| Hardened Runtime 总纲页 | developer.apple.com/documentation/security | 页脚 © 2026 | 硬化运行时定义与门禁附加检查关系 | §10.4、§12.2 | A |
| Runtime Exceptions 六项（`com.apple.security.cs.*`） | developer.apple.com/documentation/bundleresources/entitlements | 页脚 © 2026 | 六项**均为 macOS-only**，逐项官方后果描述；`com.apple.security.cs.debugger` 与调试器/`task_for_pid()` 关系及会话时长口径 | §10.4、§10.5、§17.1 结论 15 | A |
| Code Signing Services 与 launch constraints 页 | developer.apple.com/documentation/security | 页脚 © 2026 | URL 与标题已确认存在；**SecCode/SecStaticCode/CS_ 常量描述正文未抓取**（数据接口 404） | §10.2 | B（部分） |
| `applying-launch-environment-and-library-constraints` 等三页 | developer.apple.com | 页脚 © 2026 | 标题与存在性确认，**正文未抓取** | §10.3 | B（缺口） |
| MetricKit 索引页 | developer.apple.com/documentation/metrickit | 页脚 © 2026 | 框架 abstract"Measure your app's performance using daily metric and diagnostic reports from real users"；分组"CPU and memory metrics (6)""Crash and hang diagnostics (3)""Termination metrics (2)"等条目名 | §13.3、§14.7 | A |
| `MXCrashDiagnostic` 字段页 | developer.apple.com | 页面状态 `iOS 14.0–27.2 Deprecated` | 七个字段逐字（`exceptionType` "Mach exception type"、`exceptionCode` "encoded processor-specific information"、`signal`、`exceptionReason`、`terminationReason`、`virtualMemoryRegionInfo` "…resulting in a bad-access crash"、`callStackTree`）；"Use CrashDiagnostic instead."；同类对象 `MXAppLaunchDiagnostic` 等 | §13.1、§13.2、§17.1 结论 19 | A |
| `MXAppExitMetric` / `MXForegroundExitData` | developer.apple.com | 页脚 © 2026 | 仅 abstract 与类型名取得；**逐项字段页未取得（数据接口 404）** | §13.3、§16.2 N 项 | B（部分） |
| `CrashDiagnostic`（替代品） | developer.apple.com | 未取得 | **字段页未抓取**，登记为缺口 | §13.1、§16.2 | 缺口 |
| security.apple.com《Towards the next generation of XNU memory safety: kalloc_type》 | security.apple.com/blog | 2022-10-27 | "ASLR is often criticized because leaking one pointer value…"、喷洒抵消随机化、8 字节粒度 `kt_granule_t`（PADDING/POINTER/DATA/DUAL-unused/PAC）、32 位指针打包、PAC 签名 vtable、"second-order type confusion"、"random for every boot"、strict free（`kfree_type()`） | §9.2、§9.3、§14.4、§17.1 结论 17 | A |
| security.apple.com《What if we had the SockPuppet vulnerability in iOS 16?》 | 同上 | 2023-05-23 | 8%、194/119/90 三组量化、`ip6_pktopts` 192B、iPhone 13 Pro / iOS 16 beta 1、残余目标面 | §9.3、§15.2 | A |
| security.apple.com《Memory Integrity Enforcement》 | 同上 | 见页内标注 | MIE "在 A19 和 M5 及后续处理器上提供"、"all models of iPhone 17 and iPhone Air"、PAC 推测时序侧信道句（"would allow the valid signature to be isolated"）、分配内越界仍存活、iOS 15→17→2018→2019→2022→2025 演进链 | §8.2–8.4、§6.7、§12.1、§16.5 | A |
| security.apple.com 研究设备/PCC/Bounty 三篇 | 同上 | 未取得 | **未抓取**，仅登记为可延伸阅读方向 | §16.2 | 缺口 |

`dyld(1)`、`ld(1)` 两份手册页随 dyld 仓分发（见 A.3），但语义属官方手册，正文按 A- 级引用并标注日期戳 `June 1, 2020`（§2.2、§3.2、§10.4）。

## A.3 官方开源代码文件与行登记

版本口径（A- 级引用必带）：源码取自 `github.com/apple-oss-distributions` 各仓 **main 分支 tip**，与最新公开 tag 不完全一致——xnu `f6217f891…` / tag `xnu-12377.121.6`；dyld `fd8d0c4d5…` / `dyld-1378`；libmalloc `c49dafa25…` / `libmalloc-812.100.31`；security `db15acbe6…` / `Security-61901.120.67`。行号相对各仓根目录。**这一级证据可证"当前实现这么做"，不可证"承诺长期这么做"。**

| 仓 | 文件 : 行 | 逐字取用要点 | 引用位置 |
| --- | --- | --- | --- |
| xnu | `bsd/kern/mach_loader.c:779-783`、`:785-802` | 主镜像 slide 与 dyld slide 是**两个独立随机量**（含注释原句）；`:832` 的 `HARDENED_PROCESS` 检查 | §2.1、§10.1 |
| xnu | `osfmk/vm/vm_map.c:21919-21944` | arm64 slide 空间上限 16MB/4MB（arm64）与 x86-64 分支 `1 << (vm_map_is_64bit(map) ? 16 : 8)` | §2.1、§15.2、§17.2 |
| xnu | `osfmk/vm/vm_shared_region.c:204-208`、`:234-238` | `vm_shared_region_reslide_count`、"Only one cache gets to slide" | §2.3、§2.5 |
| xnu | `bsd/arm/vmparam.h:16` | 32 位 ARM 路径 "stack slides **down** / up to 1MB"（禁止外推为 iOS 主线程栈范围） | §3.1、§15.2 |
| xnu | `bsd/kern/kern_sysctl.c:4311-4317`、`bsd/kern/kern_fork.c:1038-1040`、`bsd/kern/bsd_init.c:293-294`、`:629-630` | 内核随机化相关实现与启动期事件 | §4.1、§4.4 |
| xnu | `bsd/kern/kern_exec.c:1016-1029`、`:1432-1443`、`:1596-1615`（`:1614` os_reason）、`:1669-1680`、`:1682-1696`、`:6764-6768` | arm64e preview ABI 拒载路径、`EXEC_EXIT_REASON_BAD_MACHO`、第三方 arm64e 仅在 `XNU_TARGET_OS_OSX` 下拒绝、`-arm64e_preview_abi` boot-arg | §6.5、§10.4、§12.4 |
| xnu | `EXTERNAL_HEADERS/ptrauth.h` | 五种密钥与 ABI 语义；`asib` "enforced even in processes which disable ABI pointer authentication"；额外数据规则（返回地址=存储地址+8、帧指针 addr+16、块对象指针本身不签名、虚表指针与函数指针 "extra data is always 0"）；"Other pointers signed under the ABI use private ABI rules." | §6.2、§6.3、§6.6、§17.1 结论 10 |
| xnu | `osfmk/mach/machine.h:383-402`、`:413-420` | cpusubtype 内 PAC ABI 版本掩码 `0x0f000000`>>24、V0/V1、CURRENT=V0、MAX_PREFERRED macOS=V1/iOS=V0；**逐字禁止**由 `hw.cpufamily` 推断特性 | §6.5、§11.5、16.1 C4 |
| xnu | `osfmk/kern/cs_blobs.h:35-76`（含 `:54`） | CS_ 标志位（CS_HARD/CS_KILL/CS_KILLED/CS_DEBUGGED/CS_RUNTIME/CS_REQUIRE_LV/CS_FORCED_LV/CS_PLATFORM_BINARY/CS_NO_UNTRUSTED_HELPERS、`CS_INVALID_ALLOWED` macOS-only、CS_EXEC_SET_*）；`CS_CONFIG_INTEGRITY_SKIP` 位于 `#if DEVELOPMENT \|\| DEBUG` | §10.2、§16.1 |
| xnu | `bsd/sys/codesign.h:40-61` | csops 操作码，含 `CS_OPS_CLEARPLATFORM`（DEVELOPMENT-only） | §10.2、§10.4 |
| xnu | `bsd/kern/ubc_subr.c:6011-6018`、`:6035-6044` | 页哈希粒度＝16K（arm64）及其官方理由 | §10.1、§16.1 C6 |
| xnu | `bsd/dev/arm/kern_machdep.c:56-57`、`osfmk/arm64/sptm/`、`arm_vm_init.c:988-990`、`:1075-1082`、`:1244-1252`、`pmap_internal.h:94`、`commpage.c:632-646` | 指针认证版本判定、页表与 commpage 布局相关实现 | §4.1、§4.4、§7.1 |
| xnu | `MakeInc.def:55-66`（makedefs）、`osfmk/arm/cpu_capabilities.h`（`CAP_BIT_FEAT_BTI 36`）、`osfmk/arm/arm_features.inc`（57 条特性全为 `ARM_FEATURE_FLAG`，含 `FEAT_PAuth/PAuth2/FPAC/FPACCOMBINE/PACIMP/BTI`） | "# Enable BTI by default for all ARM64 targets / `BTI_BUILD = 1`"、`-mkernel contains BTI by default`、`-fno-branch-target-identification`、auxKC 例外逐字（含 "branches to these pages are unchecked!" 与 "likely impractical to exploit in practice"）；**`kHasFeatBTI` 不存在** | §7.1、§11.5、§17.2 第 6 条 |
| xnu | `san/memory/kasan.c:76-84` | KASAN-TBI：16 字节粒度／1 字节 shadow／"Cost: ~8% of memory"／不在 x86-64 | §8.4、§15.2 |
| xnu | `bsd/sys/vadvise.h:76` | `GCS` 检索唯一命中为 "Suitable during GCs in LISP"（**误命中排除**） | §16.3 |
| xnu | `osfmk/arm64/alternate_debugger_asm.s:61` | "/* BTI calling convention requires tail calls use x16/x17 */"；落地垫片 `"bti c\n"` | §7.1、§13.4 |
| xnu | `bsd/sys/stat.h:375`、`bsd/kern/kern_mib.c:1143-1158` | 能力位与 sysctl 暴露路径（`IMGPF_NOJOP`→`disable_user_jop` 等） | §7.2、§11.5 |
| dyld | `common/Defines.h:195` | 共享缓存/arm64e 相关编译期口径 | §6.5、§12.6 |
| dyld | `doc/man/man1/ld.1`（`June 1, 2020`） | `-stack_size`、PIE 因果句、`-random_uuid` 为唯一含 random 的选项、**无 `-random` 类运行时随机化选项** | §2.2、§3.2、§10.4、§15.5 |
| dyld | `doc/man/man1/dyld.1:49-50` | 注入面清单；SIP 保护的二进制忽略全部 `DYLD_*` 变量（逐字） | §10.3、§15.4 |
| libmalloc | `src/vm.c:33`、`:36-46`、`:52-53`、`:61-68`、`:82-90`、`:106-112`、`:116`、`:117-123`、`:125-142` | `ENTROPIC_SHIFT 25`、`ENTROPIC_KABILLION` 256MB、`ENTROPIC_USER_RANGE_SIZE` 8Gb、`entropic_bits` 7/3/16、`guarded range`（4M–5M `PROT_NONE`、1M carveout、`rdar://146578480`）、`mvm_random_page_aligned` 使用 `arc4random_uniform`、"Eliminate cloaking"、`#error ASLR unhandled on this platform` | §3.3、§9.4、§9.5、§15.2 |
| libmalloc | `src/`（env 名） | `MallocASLRForce`、`GuardMalloc`、`MallocStackLogging`、`MallocGuardEdges` **仅源码可见，文档侧 0 命中**；Exclaves 路径 `arc4random_buf`、guarded range 未启用 | §9.5、§17.2 第 1 条 |
| security | `Security` 框架侧 | 授权属性与 platform/hardened 位互证（与 A 级文档交叉） | §10.2、§10.4 |

## A.4 取证方法、检索词与命中记录

**手册侧语料与方法**：从 `https://support.apple.com/zh-cn/guide/security/toc` 解析 179 个 secid（去重后计数，含术语表与版权页），逐页下载 `…/guide/security/<secid>/web`，全部 HTTP 200；正文抽取范围为 `<h1>` 至 `id="ac-globalfooter"` 之间、脚本化去标签；**未检索**导航区、页脚与 developer/security 子域。本地语料 179 个 `.txt` 留存于本工作区 `_apsgh/txt/` 目录，可复核。检索为大小写不敏感整词子串匹配，命中数按**页数**计。

命中 >0 的词（附页数）：ASLR 2、地址空间布局随机化 2、随机化 7、指针认证 3、PAC 1、MTE 1、Execute Never 1、不可执行 1、mmap 1、dyld 1、JIT 2、即时 4、缓冲区溢出／类型混淆／释放后重用 各 1、堆 6、栈 5、熵 1、崩溃 4。

**误命中排除（必须复核，否则结论会反）**：`XN` 的 6 页命中全部是 `XNU` 子串，含 Execute Never 语义的仅 `sec15bfe098e` 一页（术语表内该条 `grep -c` 复核为 0）；`堆`/`栈` 在信任缓存页与 IPv6 页为无关搭配；`随机化` 的 7 页中 5 页属面容 ID 与 Wi-Fi/蓝牙/UWB 隐私（`sec31e483abf` 的"此 56 位随机偏移量"是 TSF 偏移量，**不得**当地址熵）；`dyld` 唯一命中是 `dyld(1)` 对照用法。

命中 = 0 的词（179 页全库）：`KASLR`、`BTI`、`GCS`、`branch target`、`shadow stack`、`canary`、`stack canary`、`pointer authentication`（英文全称）、`branch-protection`、`-mbranch-protection`、`PACIA`/`PACIB`/`PACDA` 等 `PACI*` 助记符、`malloc`、`zone`、`guard`（含 `guard page`）、`shared cache`/`共享缓存`、`slide`、`EXC_BAD_ACCESS`、`.ips`、`DiagnosticLogs`、`armv8.3`/`ARMv8.3`。

**developer／源码侧方法**：developer 文档取自 `https://developer.apple.com/tutorials/data/documentation/<path>.json`（须先导航到该域任意页，否则 fetch 失败）；TN 与博客正文另经页面正文复核；源码取自各仓 main 分支 tarball，解压后本地定位，行号相对各仓根目录；三篇博客 HTML 与去标签文本留存本机可复抽。

零命中与负面结论登记（来源集合已标明）：

| # | 检索词 | 来源集合 | 结论 |
| --- | --- | --- | --- |
| 1 | `aslr`、`"iOS ASLR"`、`"21 bits"`、熵位数类表述 | developer 文档站（含构建设置全文、security 全书索引、82 条 TN 索引） | 命中 0；**iOS ASLR 熵位数无任何一手出处** |
| 2 | `-mbranch-protection`、`pac-ret`、`bti`、`branch-target`、`memory tagging`、`MTE`、`arm64_32` | Xcode 构建设置页全文（2303 行渲染文本逐词计数） | 全部 0 命中（同页 `arm64e` 2、`pointer authentication` 4） |
| 3 | `GCS`、`guarded control stack` | xnu／dyld／libmalloc／security 四仓 main tip | 命中 0（唯一 `GCs` 为 LISP 注释） |
| 4 | `MTE`、`memtag`、`gcs` | `arm_features.inc`、`cpu_capabilities.h`、`cpu_capabilities_public.h` | 命中 0；MTE/GCS **不是一等特性标志** |
| 5 | `EXC_BAD_ACCESS`、`EXC_GUARD`、`PACI`、`EXC_PACI`、`PACD` | developer 文档页抓取集 + MetricKit 索引 | 作为文档条目命中 0；仅 `virtualMemoryRegionInfo` 的 "bad-access crash" 间接提及 |
| 6 | `.ips`、`DiagnosticLogs`、`crash report format` | developer 文档站路径探测 + 站内搜索 | 未找到官方格式文档页；多个猜测 slug 404 |
| 7 | TN2169（假设为"About iOS crash reports"） | developer 归档 TN 列表 | 实测标题为 "High Precision Timers in iOS / OS X"，修订史仅 2013-03-05——**该假设不成立** |
| 8 | `-random` | `ld.1` 手册页 | 命中 0；仅 `-random_uuid`（构建期标识） |
| 9 | `secure_entry_point`、`secure-coding`、`arm64e`（作 slug）、`glossary` | developer JSON 路径探测 | 404；Apple **无**独立"PAC ABI"文档页，语义只能取自 `ptrauth.h` + `machine.h` |
| 10 | `task_for_pid`、`vm_read`、`mach_vm_read` | developer 文档站 | 无文档页；语义仅来自 `com.apple.security.cs.debugger` 页 |
| 11 | `MallocStackLogging`、`GuardMalloc`、`MallocGuardEdges` | developer 文档站索引 | 命中 0，仅源码 env 名 |
| 12 | 芯片 × PAC/BTI/EMTE 支持矩阵 | developer 站 + 四仓 + 三篇博客 | 无一手表格；仅有 MIE 的 "all models of iPhone 17 and iPhone Air"、"A19 和 M5 及后续" 与 "PAC in the A12 Bionic chip"（2018）三句范围口径 |
| 13 | `hdiutil`/FileVault 类文件保护名 | — | 本篇不使用"文件保险箱"作为 iOS 文件保护机制名称（口径纠正，见 §0.5 术语纪律） |

**编号页族踩坑记录**：`support.apple.com/zh-cn/100100` 系列仅 `100100`（《Apple 安全性发布》）存在，`100101–100110` 全部 404；后续取证应走 `guide/security/<secid>` 路径而非编号猜测。

# 附录 B 术语表

本表固定本篇口径。"官方措辞"列只收一手逐字或一手字段名；无逐字出处的项显式写"无一手页"。

| 术语 | 本篇定义 | 官方措辞与所在处 | 混用警示 |
| --- | --- | --- | --- |
| 地址空间布局随机化（ASLR） | 装载与运行时对**地址**的随机安排 | 手册："随机安排所有内存区域""随机安排可执行代码、系统库和相关编程结构的内存地址"（`sec15bfe098e`）；术语表把执行主体归给内核（`sec93292bfa6`） | 被当作全部内存安全机制的代称，吞掉 PAC/MTE/XN（§0.5） |
| slide | 镜像或共享缓存整体位移量 | **手册命中 0**；实现量取自 `mach_loader.c:785-802`、`vm_map.c:21919-21944`、`vm_shared_region.c:204-208` | 把主镜像 slide 与缓存 slide 相加当"一个熵"（§2.1、§2.3） |
| KASLR / 内核文本随机化 | 内核自身镜像位置随机化 | **`KASLR` 在 179 页命中 0**；只有"内核……强制执行……ASLR"一句可引 | 由此句推出"内核镜像被随机装载"（§1.2、§4.1） |
| 每次启动重新随机化 | 随机化的**时间尺度**之一 | 博客 "random for every boot"（kalloc_type，2022-10-27）针对内核区分配；缓存侧另有 reslide 计数 | 与"每次安装""每台设备"混用（§2.5） |
| dyld 共享缓存 | 系统框架的合并映射 | 手册 `共享缓存`/`shared cache` 命中 0；实现证据在 `vm_shared_region.c` | "共享 ⇒ 随机化对系统库无效"（§2.4、§17.2 第 14 条） |
| PIE / 位置无关可执行 | 可被装载到任意基址的产物形态 | `ld.1` 手册页因果句（日期戳 June 1, 2020） | 与 `-random_uuid` 混为"随机化选项"（§10.4） |
| 主线程栈 / 线程栈 | 执行帧所在区域 | 手册无单独随机化句；`bsd/arm/vmparam.h:16` 注释给出 32 位 ARM 路径 "up to 1MB"；`ld.1` 给 `-stack_size` | 把 32 位数字当 iOS arm64 主线程栈范围（§3.1） |
| guard page（保护页） | 页属性为不可访问的边界页 | **手册 `guard` 命中 0**；libmalloc 侧有 `guarded range`（`vm.c:125-142`） | "guarded" 一词在本篇有三义：保护页、guarded malloc 区域、GCS 的 guarded（§9.5、16.1 C5） |
| entropic bits | libmalloc 内部熵参数 | `ENTROPIC_SHIFT 25`、`entropic_bits` 7/3/16、`ENTROPIC_KABILLION`（`vm.c:36-46`） | 把内部参数读成"Apple 承诺的熵位数"（§5.3） |
| 可写可执行页（W+X） | 同一页可写又可执行 | 手册三条件合取句，Safari JIT 被点名（`sec15bfe098e`） | 忽略三条件为**合取**，写成"需要 entitlement 即可"（§7.2、§7.3） |
| Execute Never（XN） | 页不作为指令取用 | 手册「Execute Never 功能」段；`mmap` 一词唯一命中同页 | 与"只读"合并；`XN` 检索命中全为 `XNU` 子串（§7.2、A.4） |
| APRR / ERP | 按线程快速撤销执行权限的硬件原语 | 手册逐字"自 Apple A11 仿生和 S3 后的 SoC"、"因为内存无法在读写的同时有效执行"（`sec8b776536b`） | 当成页属性开关（§7.3） |
| JIT | 即时编译 | 手册与 Rosetta 2 页命中；TN 与文档口径 "may fall back to an interpreter" | "锁定模式禁用全部 JIT"——逐字只支持"某些复杂的网页技术会被阻止"（§7.4） |
| 锁定模式 | 用户/组织可开启的强化档 | 手册页 + 支持文章 105120（标注 2026-09-18）；排除项逐字"会削弱锁定模式的保护效果" | 把排除当无成本便利（§7.4、§11.2） |
| 指针认证码（PAC） | 用密钥与上下文对指针计算签名并在认证时校验 | 手册："PAC 用来防止对内存损坏错误的利用。系统软件和内建 App 使用 PAC…"（`sec8b776536b`） | 写成"指针加密"或"数据完整性机制"（§6.1、§6.2） |
| IA / IB / DA / DB / GA | 五类密钥的用途维度 | 手册 14 行表 + `ptrauth.h`；"五种 128 位密钥""每个用户空间进程都有其自己的 B 类密钥" | 与 `ptrauth.h` 里的**四个 ABI 密钥**混计（16.1 C2） |
| 额外数据（extra data） | 参与签名计算的第二操作数 | `ptrauth.h`：返回地址=存储地址+8、帧指针 addr+16、虚表相关 "extra data is always 0" | 忽略额外数据相同 ⇒ 两个指针签名可互换的风险（§6.3） |
| 指针载体（carrier） | 签名存放位置 | "64 位指针顶部未使用的填充位"（A 级逐字） | 未签名指针与签名指针在同一容器里被混用（§6.6） |
| arm64e | 带指针认证的指令集变体 | `ENABLE_POINTER_AUTHENTICATION` 逐字"Adds an additional architectural slice (arm64e) … Has no effect if ARCHS has been overridden" | 把"开了开关"当"产物里有切片"（§6.4、§11.1） |
| arm64_32 | watchOS 的 32 位 ABI | Xcode 文档 0 命中；口径来自 OSS 侧 | 把 watchOS 覆盖并入 iOS 基线（§12.3、§6.8） |
| PAC ABI 版本 V0/V1 | cpusubtype 内编码的约定版本 | `machine.h:383-402`，掩码 `0x0f000000`>>24，CURRENT=V0，MAX_PREFERRED macOS=V1/iOS=V0 | "future OSes may break ABI"被省略（§6.5） |
| `-arm64e_preview_abi` | 允许 preview ABI 二进制载入的启动参数 | `kern_exec.c` 拒载路径 + `OS_REASON_EXEC`/`EXEC_EXIT_REASON_BAD_MACHO` | 当成产品能力（§6.5） |
| 分支目标识别（BTI） | 间接分支需落到落地垫片 | **手册与产品文档 0 命中**；`MakeInc.def` "BTI_BUILD = 1"、`CAP_BIT_FEAT_BTI 36`、auxKC 例外逐字 | 把源码默认当 Apple 对产品的承诺；忽略 "branches to these pages are unchecked!"（§7.1） |
| GCS（guarded control stack） | 硬件影子栈 | **四仓 + 文档全部 0 命中**（唯一命中为 LISP 注释） | 把 ARM 规范特性当 iOS 已启用（§7.1、§16.3） |
| MTE / EMTE / MIE | 分配标记及其校验的不同档 | 手册 MTE/EMTE/"标记保密性强制"段；MIE 逐字"在 A19 和 M5 及后续处理器上提供""all models of iPhone 17 and iPhone Air" | 三者混为一句"内存标记"；MIE ≠ KASAN（§8.1–8.4） |
| 内存标记粒度 | 标记覆盖的字节数 | 博客与 `kasan.c:76-84`：KASAN-TBI 16 字节粒度、"Cost: ~8% of memory" | 把 KASAN 开销当 MIE 开销（§8.4） |
| 类型化内核区分配（kalloc_type） | 按类型分桶并交叉引用检查 | 博客逐字 "ASLR is often criticized because leaking one pointer value…"、8 字节 `kt_granule_t`、`kfree_type()` | 当成用户态 malloc 加固（§9.2、§9.3） |
| zone / 内存区 | 用户态分配器分区 | **手册 `zone` 命中 0**；仅有"Apple 安全内存分配器"名称 | 把内核 zone 与 libmalloc zone 混用（§9.1、16.1） |
| 库校验（Library Validation） | 可加载代码的身份边界 | `sec7c917bf14` 逐字：Apple 签名或同 Team ID；系统可执行文件无 Team ID | 与"沙盒"合并成一句"进程隔离"（§10.3） |
| 按需页校验（page-in checking） | 页载入时校验哈希 | `secf49cad4db` + TN3126 逐字 | "已签名 ⇒ 已校验"；忽略 macOS 与 Apple 芯片分叉（§10.1） |
| CS_ 标志 | 签名运行时强制位 | `cs_blobs.h:35-76`（CS_HARD/CS_KILL/CS_RUNTIME/CS_REQUIRE_LV/CS_PLATFORM_BINARY 等） | 源码位当产品承诺（§10.2） |
| 硬化运行时例外 | macOS 的六项 `com.apple.security.cs.*` | entitlements 文档逐项有后果描述 | 当成 iOS 能力（§10.4） |
| MXCrashDiagnostic | MetricKit 崩溃诊断对象 | 字段逐字；页状态 `iOS 14.0–27.2 Deprecated`、"Use CrashDiagnostic instead." | 当实时通道；忽略弃用状态（§13.1、§13.3） |
| `virtualMemoryRegionInfo` | 错误访问的内存区域信息 | 逐字 "…accessed incorrectly, resulting in a bad-access crash" | 当成 PAC 失败指示器（§13.2） |
| `.ips` | 崩溃报告载体格式 | **无官方格式文档页**（检索未命中） | 把社区字段表当一手规格（§13.4） |
| U1 / U2 / U3 | 本篇定义的三条上传方向（服务端／iCloud／开发者渠道） | 仅 U3 有字段级一手来源 | 合并成"上传给 Apple"（§14.1、C.3） |
| dSYM / 符号文件 | 把偏移还原为源码位置的符号产物 | 无本主题一手页；治理口径见 §14.5 | 与"崩溃日志"混写导致遗漏访问控制项（§14.5） |
# 附录 C 速查表

## C.1 机制—拦什么—不拦什么

| 机制 | 拦什么 | **不拦什么** | 一手出处所在章 |
| --- | --- | --- | --- |
| 用户态 ASLR | 预先写死的地址；跨设备批量复用的**一次性套用** | 任何一次指针/内容泄露之后的复用；同启动会话内的预测 | §2、§5 |
| 共享缓存整体位移 | 框架基址的跨启动稳定值 | 同一启动内所有进程共享同一位移这一事实 | §2.3 |
| 栈位置随机化 | 固定栈槽假设 | 帧内容本身的篡改（那是第二、三道的事） | §3.1、§3.2 |
| 保护页 / 分配器距离随机化 | 越界后**立刻**撞到元数据；固定间距假设 | 分配内越界；有读原语时的距离探测 | §3.3、§9.4 |
| 堆元数据加固与 zone 校验 | 直接改元数据、双重释放的多数形态 | 与应用对象同片的元数据覆盖；类型混淆 | §9.4、§9.5 |
| 内核类型化区分配 | 跨类型误用释放、喷洒式布局 | 单个指针泄露造成的定位削弱（官方逐字承认） | §9.2、§9.3 |
| PAC（IA/IB） | 被改写的**代码指针**、返回地址、虚表项 | 数据指针；不含填充位的重构指针；签名可复制的复用；纯读原语 | §6.2、§6.6、§6.7 |
| PAC（DA/DB/GA 与上下文） | isa/super/选择器/方法缓存等的定向篡改 | 未签名对象；关闭 ABI 认证后除 `asib` 外的类别 | §6.2、§6.4 |
| MTE / EMTE / MIE | 标记不匹配的**被标记**内存访问、多数空间越界 | 未标记内存；分配内越界；标记泄露；推测路径（官方时序侧信道句） | §8.1–8.4 |
| Execute Never / 页不可执行 | 把数据当代码取用 | 已有的可执行代码复用（ROP/JOP 类）；被批准的可执行映射 | §7.2 |
| APRR / ERP | 读写与执行同时成立的需求；线程级执行需求 | 该硬件世代之前的设备（A11/S3 为门槛）；已授权 JIT 路径 | §7.3、§12.1 |
| BTI | 落到非落地垫片的间接分支 | auxKC 例外的页（逐字 "branches to these pages are unchecked!"）；落地垫片后的序列 | §7.1 |
| 按需页校验 | 载入内存的页与签名哈希不符 | 校验时机为惰性、平台分叉带来的窗口 | §10.1 |
| 代码签名 / CS_KILL 等 | 未签名或被篡改代码的执行 | "已签名但写法危险"的代码（责任回落开发者） | §10.2、§10.6 |
| 库校验 | 加载非 Apple、非本 Team 的动态库 | 允许加载的库**内部**的缺陷 | §10.3 |
| 沙盒 / 扩展独立地址空间 | 跨进程、跨扩展的越界访问 | 单进程内的内存破坏 | §1.1、§10.3 |
| 锁定模式收紧 | 部分复杂网页技术（含 JIT 类）路径 | 被逐条排除的站点/App/联系人；非网页路径 | §7.4 |
| 调试授权治理 | 无权限方的跨进程内存读 | 已授予 `com.apple.security.cs.debugger` 一类能力的进程 | §10.5 |

## C.2 门槛—强度来源—失效条件

| 门槛 | 强度来源（哪一层给的） | 失效条件（可审计的形式） | 是否可被本团队整改 |
| --- | --- | --- | --- |
| 位置不可知（ASLR/slide/距离随机化） | 内核 + 加载器 + 分配器实现；熵源**未公开** | 一次指针/内容泄露；喷洒抵消；某路径未走随机化（`#error ASLR unhandled on this platform`）；旧产物形态 | 部分（产物形态、依赖库）——§17.3 第 1、2 条 |
| 指向可验证（PAC） | 硬件指令 + 每进程密钥 + ABI 约定 + 签名与切片 | 指针被重构且丢失顶部填充位；签名可复制；进程禁用 ABI 认证（`asib` 除外）；非 arm64e 切片；V0/V1 约定差 | **是**（切片与依赖库）——§6.4、§11.1 |
| 越界不可用（标记） | 硬件标记位 + 编译器插桩 + 内存标记模式 | 未标记内存；分配内越界；标记被读出；推测路径 | 部分（工具链档位）——§8.5 |
| 执行目的受限（XN/APRR/BTI） | 页属性 + 按线程硬件原语 + 指令集落地垫片 | 世代早于 A11/S3；被合取条件批准的 W+X 映射；auxKC 例外页；无产品口径的 BTI 承诺 | 部分（不自建可执行内存）——§7.3、§17.3 第 3 条 |
| 载入时代码完整性（页校验/签名） | 内核哈希校验 + 签名位强制 | 惰性校验窗口；`CS_CONFIG_INTEGRITY_SKIP` 类开发档；macOS 与 Apple 芯片分叉 | 否（平台）——§10.1、§10.2 |
| 可加载代码边界（库校验） | 签名身份（Apple 或同 Team ID） | 例外 entitlement；无 Team ID 的系统可执行文件的特例语义 | 部分（不引入外来库）——§10.3、§10.4 |
| 进程隔离（沙盒/扩展/独立地址空间） | 内核策略 + 描述文件 + 授权属性 | 调试类授权；组织下发的例外 | 部分（配置治理）——§10.5、§11.3 |
| 用户与组织档（锁定模式、诊断数据、恢复方式） | 设备设置 + 描述文件 | 逐条排除；关闭诊断数据影响取证样本量 | 否（但可下发）——§11.2、§11.3 |
| 硬件世代 | 芯片 | 世代早于矩阵门槛；无一手支持矩阵可查 | 否（采购）——§11.5、§17.6 |

## C.3 上传方向—携带的布局信息—可见主体

| 方向 | 本篇定义 | 携带的布局信息（字段级） | 可见主体 | 一手强度 |
| --- | --- | --- | --- | --- |
| **U1 服务端**（App 自有后端接收的诊断/日志） | 由本团队后端持有 | 崩溃线程栈帧、模块基址与偏移、镜像清单、可能的原始指针值 | 本团队工程与运维、以及获得该存储访问权的任何主体 | 无 Apple 一手页（本团队自身即数据控制者）——§14.1、§14.3 |
| **U2 iCloud／设备备份与共享诊断** | 用户档与设备侧共享 | 与设备诊断同族的地址/偏移集合；快照后不再受运行时随机化保护 | Apple 与用户（按共享开关），组织可能通过管理策略影响 | 手册侧口径存在但**不给字段名**；页格式无官方文档——§13.4、§14.4 |
| **U3 开发者渠道（MetricKit / 崩溃与事件报告）** | Apple 面向开发者的聚合上报 | `exceptionType`（Mach 异常类型）、`exceptionCode`（处理器相关编码）、`signal`、`terminationReason`、`virtualMemoryRegionInfo`（bad-access 的内存区域信息）、`callStackTree` | 开发者（按 App 归属）；Apple 作为处理方 | **字段级 A 级逐字**（本篇唯一有字段清单的一手通道）；粒度为**日报、来自真实用户**——§13.1、§13.3 |

配套三条判据：
1. **快照即静态数据。** 上报完成后，地址与偏移只受存储与访问控制保护（§14.4）。
2. **暴露量估算看"单元数"。** 一次上报通常含多少个布局信息单元，按 §14.7 的九单元基线估，不按"有没有 ASLR"估。
3. **符号文件是乘数。** dSYM/映射表与服务端数据同处一个可达域时，等价于交出偏移→源码还原能力（§14.5）。

## C.4 合格承诺句式在本篇的三次出现

对外文档、评审结论与采购说明中，**只有**这类带档位、带作用域、带时间尺度的句式可以成立。本篇正文三次给出该句式，位置与用途如下。

| 次序 | 出现位置 | 句式形状 | 为什么合格 |
| --- | --- | --- | --- |
| 第一次 | §0.4 证据分级与引用纪律 | "Apple 一手文本确认 X 的**存在与作用**，但未公开其**量化参数**；因此本篇只写定性口径并登记缺口。" | 把"没写"与"不存在"分开，避免了位数字段的代填 |
| 第二次 | §2.6 对照表（"能不能写进正文"） | "该句可写，但须限定**层级**（内核/加载器/分配器/二进制）与**尺度**（每次启动／每次安装／每台设备）。" | 层级与尺度是本篇所有强度结论的两个必要限定 |
| 第三次 | §17.1 十九条 + §17.2 十四类否定 | "可支持的结论逐条给档位与章节；不可支持的结论逐条给缺口编号。" | 承诺面与不承诺面**同时**交付，避免单向叙述 |

可直接复用的三条合格句式（改写自本篇逐字出处，不含任何能力夸大）：

1. "Apple 手册逐字确认地址空间布局随机化随机安排内存区域、可执行代码、系统库及相关编程结构，其公开收益口径是提高利用难度、尤其是对多设备的批量攻击；Apple 未公开其熵值与熵源。"
2. "指针认证用于防止对内存损坏错误的利用，系统软件与内建 App 使用该机制；它作用于代码指针的**方向可验证性**，不构成数据机密性保证，也不覆盖未签名的指针类别。"
3. "本报告的强度判定按五层模型（硬件世代／内核／加载器／分配器／二进制自身）逐项给出，任一项未取证即标注为未取证，不作为'具备'计入基线。"

**联系方式**：Telegram: https://t.me/one00190
