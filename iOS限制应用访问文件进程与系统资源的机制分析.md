@@TITLE@@限制应用对文件、进程及系统资源的访问：Apple 平台上的多层访问约束机制
@@SUBTITLE@@从容器边界、授权键值、用户同意到内核强制的逐层核查
@@INFO@@报告日期|2026 年 10 月 21 日
@@INFO@@分析对象|iOS、iPadOS、macOS、visionOS 上限制第三方进程访问文件、进程与系统资源的机制
@@INFO@@视角定位|机制与边界本身：谁在哪一层拦、拦什么、拦不住什么
@@INFO@@分类|平台安全机制分析（防御性 / 教育性）
@@INFO@@难度|中—高（需要同时读懂平台安全指南与开发者授权文档）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@目录

# 0. 概述、口径与本篇分工

## 0.1 本篇要回答的问题

"限制应用对文件、进程及系统资源的访问"是一句高度压缩的描述。把它展开后其实是六个互相独立、且分别由不同主体执行的问题：

1. 一个进程的**地址空间与文件系统视图**被划到哪里为止（容器、主目录、宗卷）；
2. 该进程**被允许提出哪些类别的请求**（授权键值对）；
3. 即便类别被允许，**这一次具体访问**是否还需要用户点头（同意层）；
4. 访问对象本身**是否自带许可条件**（数据保护类、SIP 保护路径、POSIX 权限与 ACL）；
5. 进程能消耗多少**系统资源**（内存上限、可持续性能、后台执行、网络）；
6. 上述五层由谁执行、在什么时机执行、失败时以什么形式暴露（可观测性）。

本篇按这六问逐层核查，并明确标注每一层的证据等级。需要一开始就说清的是：Apple 公开文档把这六层分散在两份性质不同的资料里——面向普通用户的《Apple 平台安全保护》（下称"平台安全指南"）与面向开发者的 Apple 开发者文档。前者的中文正文给出的是**设计陈述**（"设计旨在""帮助确保""帮助阻止"），后者给出的是**接口事实**（键名、可用性起始版本、行为描述）。两类陈述不可互相加强，本篇逐条保留原始限定语。

## 0.2 与系列已有篇目的分工

本篇与已发布的若干篇目在对象上有重叠，因此先划清边界。

| 已有篇目 | 它回答的问题 | 本篇从它哪里接手 |
| --- | --- | --- |
| 《iOS 代码签名验证与相关授权属性》（#84） | 签名在校验什么、授权属性作为被校验对象的语法 | 授权属性作为**访问许可开关**的使用面 |
| 《iOS 代码签名与完整性执行体系组件分析》（#85） | 由哪些组件执行完整性校验、执行主体清单 | 完整性执行之外的**访问许可**执行主体 |
| 《iOS 程序可使用的特定能力与权限分析》（#86） | 能力目录本身：有哪些键、如何合成、门槛档位 | 键值之后**实际拦住什么**：路径、句柄、配额 |
| 《iOS 内核安全机制深度解析》（#79） | 内核侧机制总览 | 内核强制中与"访问许可"直接相关的子集 |
| 《iOS 沙箱机制、权限提升与内核安全分析报告》 | 早期综述，含攻击面与绕过叙事 | 本篇不重复攻击面叙事，只做机制与口径核查 |
| 《iCloud 高级数据保护核心原理》 | 服务端数据加密与密钥管辖 | 设备端**文件打开许可**（数据保护类） |

一句话定位：#84 是语法，#85 是执行器，#86 是词表，**本篇是围栏**——它描述每一道围栏的材料、位置、开门方式，以及公开资料没有说清的部分。

## 0.3 证据分级与三条写作纪律

| 级别 | 含义 | 本篇用法 |
| --- | --- | --- |
| A | Apple 官方文档正文的直接陈述（平台安全指南中文版页面、开发者文档页面） | 可逐句引用，保留原限定语 |
| B | Apple 官方场合出现但未在该页面正文展开（更新说明、支持文章、术语表） | 引用并注明层级 |
| C | 社区、研究者或第三方实现描述 | 不合并进正文结论，只在口径冲突表中并列 |
| D | 无来源的合理猜测 | 一律不写，改写入"未披露"表 |

三条纪律：

- **纪律一（限定语守恒）**：原文的"设计旨在 / 帮助确保 / 帮助阻止 / 可"不得升格为"保证 / 阻止 / 必然"。本篇正文中凡出现 A 级句子的转述，均保留原限定语。
- **纪律二（跨层不合并）**：平台安全指南说的是 iOS/iPadOS/visionOS 的第三方 App 全部沙盒化；开发者文档的 App Sandbox 页面标题明确限定为 macOS apps。两者不是同一命题，本篇分列，不互相外推。
- **纪律三（负面陈述标注）**：某机制在本次核查覆盖的页面中没有出现，只写"本次核查覆盖页面内未出现"，不写"Apple 未使用/不存在"。

## 0.4 本篇覆盖的核查范围

A 级来源共 18 个页面（平台安全指南 9 个、开发者文档 9 个），逐页编号、正文标题与发布日期见附录 A.1。核查日期为 2026 年 10 月 21 日。平台安全指南中文正文各页仍标注 2024 年内的发布日期（如运行时进程安全页为 2024 年 12 月 19 日），说明中文版的更新滞后于英文原稿，见 11.2 版次提醒。

# 1. 总览：六道围栏与执行主体

## 1.1 四种拦截性质

先按"拦截发生在哪个环节"分类，再按"由谁执行"分类，可以避免把不同层的机制混成"沙盒"一个词。

| 性质 | 拦截的是 | 典型层 | 若缺失会出现什么 |
| --- | --- | --- | --- |
| 视图型 | 进程"看得见"哪些路径与对象 | 容器、主目录随机分配、只读系统分区 | 任一进程可枚举全盘与其他 App 数据 |
| 能力型 | 进程能否发起某类请求 | 授权键值对、沙盒总开关 | 键值形同虚设，任何 API 都可调 |
| 同意型 | 某一次具体访问是否放行 | 隐私同意目录、开放/存储面板、全盘访问 | 能力型通过后即静默拿到数据 |
| 对象型 | 被访问对象自身是否带条件 | 数据保护类、SIP 保护路径、POSIX 权限与 ACL | 围栏之外无第二道防线 |
| 配额型 | 进程可消耗多少资源 | 内存上限、可持续性能、后台执行 API | 单进程耗尽设备资源 |
| 隔离型 | 不可信数据是否进入主进程 | BlastDoor、扩展项独立地址空间、用户空间驱动 | 解析漏洞直接命中 App 主进程 |

## 1.2 机制—层—执行主体—平台总表

| # | 机制 | 所在层 | 执行主体（公开口径） | 主要适用平台 | 证据 |
| --- | --- | --- | --- | --- | --- |
| 1 | App 沙盒化 | 视图 + 能力 | 系统（iOS 侧对第三方 App 强制） | iOS/iPadOS/visionOS | A |
| 2 | App 专属随机主目录 | 视图 | 安装期分配 | iOS/iPadOS/visionOS | A |
| 3 | 系统分区只读装载 | 视图 | 挂载方式 | iOS/iPadOS/visionOS | A |
| 4 | 非权限用户 `mobile` 身份 | 视图 + 对象 | UNIX 用户边界 | iOS/iPadOS/visionOS | A |
| 5 | 不提供远程登录等服务 | 视图 | 系统软件裁剪 | iOS/iPadOS/visionOS | A |
| 6 | API 不允许提升权限修改他 App | 能力 | 框架层约束 | iOS/iPadOS/visionOS | A |
| 7 | 声明的授权（键值对） | 能力 | 代码签名 + 内核载入检查 | 全平台 | A |
| 8 | App Sandbox 总开关键 | 能力 | 内核强制点（macOS） | macOS 10.7+ | A |
| 9 | 沙盒容器目录 | 视图 | 首次启动时由系统创建 | macOS | A |
| 10 | 容器与代码签名身份绑定 | 对象 | macOS 14+ 系统检查 | macOS 14+ | A |
| 11 | 开放/存储面板独立进程 | 同意 | 面板进程扩展沙盒 | macOS | A |
| 12 | security-scoped 访问 | 同意 + 对象 | URL 级访问作用域 | macOS | A |
| 13 | 带安全作用域的书签 | 同意持久化 | Foundation 书签 | macOS | A |
| 14 | 文档相对书签 | 同意扩展 | Foundation 书签 | macOS | A |
| 15 | 相关文件（`NSIsRelatedItemType`） | 同意扩展 | `NSFilePresenter`/`NSFileCoordinator` | macOS | A |
| 16 | App 组容器 | 视图共享 | 组标识 + 授权 + 分发条件 | 全平台 | A |
| 17 | 隐私同意目录 | 同意 | 设置中的授权状态 | iOS/iPadOS/macOS/visionOS | A |
| 18 | 数据保险箱 | 对象 | 文件系统级保护 | iOS/iPadOS/visionOS 13.4+ | A |
| 19 | 数据保护类 | 对象 | 类密钥 + 锁屏/登录状态 | 全平台 | A |
| 20 | 钥匙串数据保护 | 对象 | 独立密钥分组 | 全平台 | A |
| 21 | SIP | 对象 | 内核阻止修改特定路径 | macOS/iOS | A |
| 22 | 载入时按页代码签名检查 | 能力前置 | 内核 | 全平台 | A |
| 23 | 可写可执行页限制 | 能力 | 内核检查动态代码签名授权 | iOS/iPadOS | A |
| 24 | XN 与 ASLR | 隔离 | MMU / 装载器 | iOS/iPadOS 等 | A |
| 25 | 后台处理只能经系统 API | 配额 | 框架调度 | iOS/iPadOS/visionOS | A |
| 26 | 内存上限/扩展地址/可持续性能键 | 配额 | 内核与资源调度 | 视键而定 | A |
| 27 | BlastDoor | 隔离 | 独立实例 + 沙盒限制 | iOS 14/iPadOS 14/macOS 11/watchOS 7+ | A |
| 28 | 扩展项独立地址空间与容器 | 视图 + 隔离 | 系统框架协调 IPC | iOS/iPadOS/macOS/visionOS | A |
| 29 | 自定义键盘默认极受限沙盒 | 能力 | 系统默认策略 | iOS/iPadOS/macOS/visionOS | A |
| 30 | DriverKit / 系统扩展用户空间运行 | 视图 | 替代内核扩展 | macOS（DriverKit 亦及 iPadOS） | A |
| 31 | 加固运行时（Hardened Runtime） | 隔离 | 与 SIP 共同保护运行时完整性 | macOS | A |
| 32 | 受管理的打开方式 / 访问限制 | 策略 | 设备管理服务 | 全平台 | A |
| 33 | 锁定模式 | 降权总闸 | 系统限制若干类别 | iOS 16/iPadOS 16/macOS 13/watchOS 10+ | A |
| 34 | iCloud 云盘访问与逐 App 控制 | 同意 | 设置中的每 App 开关 | iOS/iPadOS/macOS | A |

## 1.3 三个最容易混淆的说法

| 混淆 | 正确拆分 |
| --- | --- |
| "沙盒就是权限控制" | 沙盒决定**能不能发起该类请求**；同意决定**这一次是否放行**；数据保护类决定**对象此刻可否被解密打开**。三者在时间轴上先后不同。 |
| "数据保险箱 = 文件保险箱 = 钥匙串" | 中文里三个"保险箱"分别指 data vault（App 数据保护）、FileVault（macOS 宗卷加密）、钥匙串（凭证存储）。执行层与保护对象完全不同，见 4.2、6.5 与术语表。 |
| "没有沙盒就没有隔离" | 平台安全指南明确数据保险箱"甚至本身未沙盒化的进程也不可访问"，且 macOS"关键区域中的数据具有自我保护功能……无论尝试访问的 App 是否本身已沙盒化"。对象型约束独立于视图/能力型约束存在。 |

# 2. 第一道围栏：进程"看得见什么"

## 2.1 iOS/iPadOS/visionOS：随机主目录与服务白名单

A 级原文（运行时进程安全页，`sec15bfe098e`）给出的三条结构性约束：

- 「所有第三方 App 均已经过沙盒化。沙盒化的设计旨在防止 App 收集或修改其他 App 储存的信息或者篡改设备。」
- 「每个 App 还拥有唯一的主目录来存放其文件，主目录是在安装 App 时随机分配的。」
- 「如果第三方 App 需要访问除自身信息以外的其他信息，只能通过 iOS、iPadOS 和 visionOS 明确提供的服务来实现。」

第三条是**白名单结构**而非黑名单结构：默认看不见，被显式提供的服务才可达。这把"未列出=不允许"变成平台默认，与 macOS 上"沙盒是可选项"的模型（见 2.3、8.1）在起点上就不同。

## 2.2 系统自身的位置：只读分区、非权限用户与裁剪

同页（`sec15bfe098e`）对系统侧的四条陈述，共同构成"App 够不着系统"的边界：

- 「系统文件和资源也会与用户的 App 保持隔离。」
- 「绝大部分 iOS、iPadOS 和 visionOS 系统文件和资源以非权限用户 `mobile` 的身份运行。」
- 「整个操作系统分区以只读方式装载。」
- 「不必要的工具（如远程登录服务）未包含在系统软件中，并且 API 不允许 App 提升自己的权限来修改其他 App或者 iOS、iPadOS 和 visionOS。」

注意两处措辞的收缩面：「绝大部分」不是"全部"，因此本篇不能写成"所有系统进程均以非权限用户运行"；「API 不允许 App 提升自己的权限」是对 API 面的陈述，不是对内核强制的陈述——它描述的是"不提供给 App 这条路"，与"提供了但被拦截"是两种不同的口径，正文保持这一区分。

## 2.3 macOS：容器是首次启动时创建的，且 home 目录不在默认视图内

开发者文档"Protecting user data with App Sandbox"给出容器语义：

- 「The operating system creates a container directory when launching your sandboxed app, to which the app has unrestricted read and write access.」
- 「The sandboxed app doesn't have unrestricted access to the user's home folder.」
- 容器实际落点：「macOS creates a sandbox container on the file system (in `~/Library/Containers`) and associates it with your app.」
- 且「Your app has full read and write access to its sandbox container, and can run programs located there as well.」

三条组合出的边界值得注意：容器**在启动时才建立**（不是安装时），容器内**可执行代码**（"can run programs located there"），而容器外的 home 目录是"无 unrestricted 访问"而不是"完全不可见"。这两处宽松正是第 4 章外推机制（面板、书签、组容器）存在的原因。

## 2.4 容器归属与代码签名身份（macOS 14+）

A 级（accessing-files 页）把容器的归属判定交给了签名身份：

- 「In macOS 14 and later, the operating system uses your app's code signature to associate it with its sandbox container.」
- 跨容器访问触发用户确认：「If your app tries to access the sandbox container owned by another app, the system asks the person using your app whether to grant access.」
- 拒绝的两种后果不同时：运行中被拒「it can't read or write the files in the other app's sandbox container」；启动时被拒「your app fails to launch」。
- 无沙盒键的进程同样被拦：「The system also requests permission … if the app that's attempting to access the files doesn't have the app sandbox entitlement in its code signature.」
- 授予后的作用域是进程生命周期：「the app can access files in any app sandbox container until it exits.」
- 辅助工具与登录项被纳入同一追踪：「tracks the association between an app's code signing identity and its sandbox container for helper tools, including launch agents」，且「`launchd` starts the launch agent again and the operating system re-requests access」。

两个可复核的推论（本篇标为 A-推论）：其一，授权是**进程生命周期级**而非路径级——一次同意后到退出前对所有容器有效，这意味着"同意"的粒度在此处比文件粒度粗；其二，被拒后由 `launchd` 重启并重新询问，说明该门禁不是一次性判定，而是与启动流程耦合的重复判定。Apple 文档同时给出一个反例场景：同一 App 的两个版本若 designated requirement 不同（Mac App Store 版与 Xcode 本地产物并存、或转移到另一开发者团队导致签名身份变化），系统会重新请求许可。

## 2.5 扩展项：独立地址空间与"不共享文件与内存"

A 级（支持的扩展项页，`secabd3504cd`）：

- 「扩展项在其自己的地址空间运行。App 与其激活的扩展项之间的通信采用由系统框架协调的进程间通信。它们无权访问彼此的文件或内存空间。」
- 「扩展项的设计旨在将它们彼此隔离、与其包含的 App 隔离，并且与使用它们的 App 隔离。与其他第三方 App 类似，它们也经过沙盒化并且拥有的容器与含有 App 的容器隔开。」
- 但隐私权限有一处重要的例外继承：「扩展项与其容器 App 对隐私控制具有相同的访问权限。如果用户向 App 授予"通讯录"的访问权限，该 App 中嵌入的扩展项也会获得此权限，但由该 App 激活的扩展项不具有该权限。」

这里出现本篇最关键的一处**边界与权限的不对称**：文件与内存被隔开，同意状态却不按容器切分，而是按"嵌入"关系传递。"嵌入的扩展项"与"被该 App 激活的扩展项"因此是两种不同的访问主体——这一区分在评估数据可达面时不能省略。

## 2.6 不可信数据的前置隔离：BlastDoor

A 级（`secd3c881cee`）：

- 「BlastDoor 会隔离、解析、转码和验证"信息"、IDS 和其他向量中收到的不可信数据来帮助阻止攻击。」
- 「BlastDoor 通过采用沙盒限制和输出内存安全验证，为攻击者侵入操作系统的其他部分制造了极大的障碍。」
- 「"信息"会区别对待来自已知发件人和未知发件人的流量……并将已知和未知数据分隔到不同的 BlastDoor 实例中。」

在访问约束的视角下，BlastDoor 的意义是把围栏从"进程边界"前移到"数据边界"：解析不可信输入的代码本身被沙盒限制，且按发件人可信度**分实例**。原文限定语为"目的在于通过阻截攻击者……来保护系统安全"与"制造了极大的障碍"，不是"阻止成功"。

## 2.7 把不可信代码请出内核：DriverKit 与系统扩展

A 级（DriverKit 页，`seca48c92d43`）：

- 「使用 DriverKit 构建的驱动程序在用户空间中运行，而不是作为内核扩展运行，以提高系统的安全性和稳定性。」
- 「这些新扩展在用户空间中运行，安装时无需特殊的权限，并在捆绑的 App 被移到废纸篓时自动移除。」
- 「扩展仅在需要时才会启用」（用户下载 App 后，使用系统扩展或 DriverKit 时无需安装器）。
- 迁移建议口径：「建议使用需要内核扩展的设备驱动程序、云端储存解决方案、网络和安全性 App 的 IT 管理员升级到基于系统扩展而构建的更新版本。这些更新版本极大地降低了 Mac 内核崩溃的可能性，同时也减少了攻击面。」

"安装时无需特殊的权限"与"移到废纸篓时自动移除"两条一起构成访问约束上的收益：用户空间驱动不需要内核级访问权，其生命周期与捆绑 App 绑定，不会留下内核态残留。加固运行时页（`hardened-runtime`）则给出相反方向的约束：「Due to their privileged position in the system, macOS refuses to load system extensions that use Hardened Runtime exception entitlements」，仅 `com.apple.security.cs.allow-jit` 在非 DEXT 系统扩展中例外——即特权执行体被禁止携带放宽自身隔离的键。

# 3. 第二道围栏：能力型键值（授权决定"能否发起该类请求"）

## 3.1 授权的定义与两种口径

A 级（Entitlements 主题页）：「Key-value pairs that grant an executable permission to use a service or technology.」讨论段进一步说明：「An *entitlement* is a right or privilege that grants particular capabilities to an executable」，且「An app stores its entitlements as key-value pairs embedded in the code signature of its binary executable」。

平台安全指南中文页给出同一对象的另一种表述（`sec15bfe098e`）：「授权是签名到 App 的键值对，允许对运行时因素之外的内容（如 UNIX 用户 ID）进行认证。授权已经过数字签名，因此无法更改。」以及用途陈述：「系统 App 和监控程序广泛应用授权来进行特定权限操作，如果不使用授权，则进程需要以 root 用户身份运行才能进行这些操作。这极大降低了遭入侵的系统 App 或监控程序提升权限的可能性。」

两句合起来是本篇的支点：**授权把"需要 root 的能力"改写成"需要一枚签名过的键"**，从而让最小特权成为可静态审查的对象。它的强度不在加密，而在"无法更改"（键在签名里，改了签名即失效——该因果属于 #84 的校验范围，本篇不重复论证）。

## 3.2 macOS 沙盒总开关

A 级（`com.apple.security.app-sandbox` 键页，可用性 macOS 10.7.0-）：

- 「A Boolean value that indicates whether the app may use access control technology to contain damage to the system and user data if an app is compromised.」
- 添加方式：「To add this entitlement to your app, enable the App Sandbox capability in Xcode.」

App Sandbox 集合页（`Security/app-sandbox`）给出该机制的目标陈述与强制场景：

- 标题句：「Restrict access to system resources and user data in macOS apps to contain damage if an app becomes compromised.」
- 概览句：「App Sandbox provides protection to system resources and user data by limiting your app's access to resources requested through entitlements.」
- 强制条件：「To distribute a macOS app through the Mac App Store, you must enable the App Sandbox capability.」
- 开发者文档补充动机句：「App Sandbox — a requirement for distributing your app on the App Store — limits the scope for an attacker to abuse platform features via your app.」

由此可得出一个必须谨慎表达的差集：在 macOS 上，**经 Mac App Store 分发**的 App 必须启用该键；从互联网分发的 App 是否启用由开发者决定。因此"macOS App 都被沙盒化"不成立，而"沙盒化在 macOS 上是可选能力 + 特定分发渠道强制"是 A 级可支持的表述（推论部分标为 A-推论）。

## 3.3 文件类键：把"哪个文件夹、读写还是只读"变成键粒度

A 级（Security entitlements 与 App Sandbox 集合页 File Access 段）。文件访问被切成"标准文件夹 + 读写方向"的矩阵，每一格一个键：

| 键（省略 `com.apple.security.`） | 语义（A 级原文转述） |
| --- | --- |
| `files.user-selected.read-only` | 只读访问用户经开放/存储面板选中的文件 |
| `files.user-selected.read-write` | 读写访问用户选中的文件 |
| `files.downloads.read-only` / `.read-write` | "下载"文件夹的只读/读写 |
| `assets.pictures.read-only` / `.read-write` | "图片"文件夹的只读/读写 |
| `assets.music.read-only` / `.read-write` | "音乐"文件夹的只读/读写 |
| `assets.movies.read-only` / `.read-write` | "影片"文件夹的只读/读写 |
| `files.all` | 「whether the app may have access to all files」——列在 Deprecated entitlements 段下 |
| `developer.security.privileged-file-operations` | 允许创建符号链接、替换文件与设置文件属性 |

两个要点。第一，标准文件夹键是"避开逐文件征询"的机制：A 级原文「Apps that work with common types of file including Music, Movies, or Photos, can request capabilities that grant access to the standard folders where these files are located, avoiding the need to individually request consent to access each file」。第二，`files.all` 仍在文档中但被归入弃用清单，这与"全盘访问"的实际路径不同——见 5.6。

另外存在一个专门控制"能不能在选中位置执行程序"的键：A 级原文「Your app can't run programs in locations outside its app bundle, sandbox container, or app group containers using the entitlements to access user-selected files. To write programs in user-selected locations, add the `com.apple.security.files.user-selected.executable` entitlement with the Boolean value `YES`.」即"用户选中的文件"默认只带数据语义，不自动带可执行语义——需要另一枚键显式提升。

## 3.4 网络类键：出向与入向分离

A 级（`com.apple.security.network.server` / `.network.client`）：

- server：「A Boolean value indicating whether your app may listen for incoming network connections.」
- client：「A Boolean value indicating whether your app may open outgoing network connections.」

沙盒违规日志样例（diagnosing 页）正是 `network-outbound` 被拒的实例，说明出向连接是策略条目而非默认放行。网络类键与"配置网络设置"的禁令（见 6.1）不同层：前者是"能否建立连接"，后者是"能否改动系统网络配置"。

## 3.5 设备类键：硬件能力按设备分格

A 级（Security entitlements 页 Device access 段）逐键口径：

| 键 | 语义（原文） |
| --- | --- |
| `device.audio-input` | 「may record audio using the built-in microphone and access audio input using Core Audio」 |
| `device.camera` | 「may interact with the built-in and external cameras, and capture movies and still images」 |
| `device.microphone` | 「may use the microphone」 |
| `device.usb` | 「may interact with USB devices」 |
| `device.bluetooth` | 「may interact with Bluetooth devices」 |
| `print` | 「may print a document」 |
| `smartcard` | 「has access to smart card slots and smart cards」 |

`audio-input` 与 `microphone` 两键并存（麦克风既可录音也可作 Core Audio 输入），是键粒度切分的直接证据；而 App Sandbox 集合页的 Hardware 段只列 camera / microphone / usb / print / bluetooth 五项，两处清单不完全一致（见 11.2 冲突表 C-04）。

## 3.6 个人信息类与自动化类键

A 级（Personal information 段与 Automation 段）：

- `personal-information.addressbook`：「read-write access to contacts in the user's address book」
- `personal-information.calendars`：「read-write access to the user's calendar」
- `personal-information.location`：「may access location information from Location Services」
- `personal-information.photos-library`：「read-write access to the user's Photos library」
- `automation.apple-events`：「may prompt the user for permission to send Apple events to other apps」

注意最后一键的措辞是"may **prompt** the user"，不是"may send"。这与 6.1 中"Sending Apple Events to arbitrary apps"被禁止并列阅读，可得出一条一致的读法：在 macOS 沙盒里，Apple events 的键授予的是**发起征询的资格**，实际放行仍属同意层。

## 3.7 共享类键与继承规则

A 级：

- `application-groups`：「A list of identifiers specifying the groups your app belongs to.」
- `keychain-access-groups`：「The identifiers for the keychain groups that the app may share items with.」
- 继承规则（hardened-runtime 页）：「You add entitlements only to executables. Shared libraries, frameworks, and in-process plug-ins inherit the entitlements of their host executable.」
- 默认值规则：「The default value of these Boolean entitlements is false. When Xcode signs your code, it includes an entitlement only if the value is true.」并附操作要求「Don't include an entitlement if the value is false.」

"仅可执行文件带键、库与进程内插件继承宿主"这条继承规则解释了 2.5 的不对称来源：与主进程共享地址空间的代码不获得独立身份，而**独立地址空间的扩展项**才被单独切分容器。默认值为 false 且"值为 false 时不要写入该键"，使签名中的键集合本身成为可审计的最小特权证据——审查时应看"有哪些键"，而不是"键是否被显式关闭"。

# 4. 第三道围栏：把允许范围扩出去（macOS 的外推机制）

## 4.1 外推不是漏洞而是设计

平台安全指南的 macOS 文件访问页（`secddd1d86a6`）与开发者文档共同说明：默认视图很窄，因此提供六种受控的外推方式。A 级（accessing-files 概览）列出可扩展方向：

- 「Read and write user-selected documents.」
- 「Share files between running processes.」
- 「Maintain access to files when your app next runs.」
- 「Work with supporting files related to other documents.」
- 「Share files between apps in an App Group.」
- 「Use common file categories stored in standard locations.」

## 4.2 标准交互：面板由独立进程展示

A 级两句构成机制：

- 「The operating system displays open and save panels in a separate process, and extends your app's sandbox to include the selected URLs.」
- 「The operating system starts security-scoped access on URLs passed from open panels, save panels, or items dragged to your app's icon in the Dock, as if you called `startAccessingSecurityScopedResource()`.」
- 目录扩展是递归的：「When the URL your app receives from a standard user interface interaction represents a folder, the operating system extends your app's sandbox to items within that folder, and recursively in nested folders.」

面板进程独立意味着"选择"这一动作本身不在被约束进程的视图内完成——这是同意层可信性的结构基础（用户看到的文件名由不受该 App 控制的进程绘制）。同时"as if you called startAccessing…"标明了隐式作用域的开始，收尾责任在 App：「When you've finished with the resources, call `stopAccessingSecurityScopedResource()` to revoke your app's access.」

## 4.3 显式作用域与持久化：安全作用域书签

A 级（persist 段）给出隐式与显式的分界：

- 「Unlike the bookmarks with implicit security scope that your app receives from standard system interactions or from communication with other processes, when you resolve a security-scoped bookmark that you retrieve from a stored representation, the system doesn't automatically extend your app's sandbox to include the bookmarked file.」
- 因此需显式包裹：解析前 `startAccessingSecurityScopedResource()`，用完 `stopAccessingSecurityScopedResource()`。
- 只读降级可用：创建时加 `securityScopeAllowOnlyReadAccess`。
- 失效处理：解析返回 `bookmarkDataIsStale` 为真时需重建并更新 stored version。
- 需要持久化时才用安全作用域：「You only need to create security-scoped bookmarks when your app might try to access the bookmarked resource after it exits and re-launches.」

该段的可复核要点是**默认方向相反**：跨进程传递的书签隐式扩域，而跨越自身生命周期的书签必须显式扩域。设计上把"进程活着时的临时信任"和"重启后仍有效的长期信任"分成两种，后者多一道显式调用。

## 4.4 跨进程共享：URL 书签与推迟扩域

A 级（share 段）：「The receiving process automatically attempts to extend its sandbox to include the bookmarked resource when it uses the security-scoped URL, as if it called `startAccessingSecurityScopedResource()` after resolving the bookmark.」并可推迟：「To defer extending the process's sandbox until you explicitly call `startAccessingSecurityScopedResource()`, pass the option `withoutImplicitStartAccessing`.」

即沙盒可以被**由消息传递触发**地扩展，且扩展时机可控。这一机制与 2.5 的"扩展项无权访问彼此文件"并不矛盾：前者需要发送方持有并主动交出带安全作用域的书签，后者是不带书签时的默认。

## 4.5 文档相对书签与相关文件

A 级：「Foundation creates a bookmark that any process with access to the resource at the `relativeTo` URL can resolve.」——书签的可达性锚定在父文档而非绝对路径，因此"工程文件 + 其引用的源码/图片"可以随文档本身流转。

相关文件系统则走另一条路：在 `CFBundleDocumentTypes` 字典中把 `NSIsRelatedItemType` 设为 `YES`，配合 `NSFilePresenter`（`primaryPresentedItemURL` = 文档 URL，`presentedItemURL` = 支持文件 URL）交给 `NSFileCoordinator`，A 级结论句为「The operating system automatically extends your app's sandbox to give your app access to the supporting file.」

## 4.6 App 组容器：四个条件与一次征询

A 级（app group containers 段）逐条：

- 「Apps that are members of the same app group can create any number of shared containers that they use to exchange files.」
- 取得位置必须由 `containerURL(forSecurityApplicationGroupIdentifier:)`；
- App 必须声明 `com.apple.security.application-groups` 且值包含该组标识；
- 「Your app is distributed through the Mac App Store, or notarized.」
- 「If your app is notarized, the app group's identifier is contained in your app's provisioning profile or prefixed with your Apple Developer team identifier.」
- 「In other situations, the system displays a request for someone to authorize your app's access to the app group container.」

四条并列说明组容器的钥匙不是单一键值，而是**键值 + 分发状态 + 描述文件（或团队标识前缀）**三者同时成立；任何一条不满足就退回"向用户征询"。平台安全指南侧（`sec1a976c067`）另给两条 iOS 口径：「宗卷上共享的存储容器，只要有 App 组内至少有一个 App 被安装，它就会一直保留在设备上」，以及「Apple 开发者网站有助于保证 App 组 ID (GID) 在整个 App 生态系统中的唯一性。」

# 5. 第四道围栏：同意层与对象自带的许可

## 5.1 同意目录：平台差异不是细节而是结论

A 级（`secc01781f46`）先给机制定位：「除了加密静态数据外，Apple 设备还使用数据保险箱等各种技术帮助阻止 App 在未获得许可的情况下访问用户的个人信息」，并说明用户「可以查看已批准哪些 App 访问特定信息，也可以授予或撤销未来的访问权限。以下情况会强制执行访问」。原文按平台分列三类：

| 适用范围 | 类别（原文列举） |
| --- | --- |
| iOS、iPadOS、macOS、visionOS | 日历、相机、通讯录、麦克风、照片、提醒事项、语音识别 |
| iOS、iPadOS、visionOS | 蓝牙、家庭、媒体、媒体 App 及 Apple Music、运动与健身 |
| macOS | 输入监视（例如键盘笔画）、提示、屏幕录制（例如静态截屏和视频）、系统设置/系统偏好设置 |

这张表本身就是"三平台交集 vs macOS 独有"的证据：屏幕录制与输入监视只在 macOS 目录中被列出。iOS 15/iPadOS 15 起新增类别（同段）：「本地网络、附近交互、研究传感器与使用数据以及专注模式。」

## 5.2 数据保险箱：连未沙盒化的进程也拦

A 级两句（`secc01781f46`）：

- 「在运行 iPadOS 13.4 或更高版本和 iOS 13.4 或更高版本的设备上，所有第三方 App 的数据自动受到数据保险箱的保护。数据保险箱有助于防止未经授权访问数据，甚至本身未沙盒化的进程也不可访问。」
- 「如果用户登录 iCloud，则会默认授予 iOS 和 iPadOS 中的 App 访问 iCloud 云盘的权限。用户可以在"设置"的 iCloud 选项中控制每个 App 的访问权限。」

第一句是对象型约束独立性的直接证据：保护附着在数据上，不附着在调用者的沙盒状态上。第二句给出一个**默认为开**的同意项（iCloud 云盘），与"默认拒绝"的视图型围栏方向相反——评估可达面时，这一项必须单独确认，不能从"沙盒默认拒绝"外推。

macOS 侧的对应陈述来自 `sec35dd877d0`：「在 macOS 中，关键区域中的数据具有自我保护功能，这可帮助确保用户仍可以控制对"桌面"、"文稿"、"下载"和所有 App 其他区域中文件的访问，无论尝试访问的 App 是否本身已沙盒化。」

## 5.3 管理侧的隔离：受管理与用户 App 之间

A 级（`secc01781f46`）：「iOS 和 iPadOS 还提供了访问限制，旨在阻止设备管理服务所安装的 App 和账户与用户所安装的 App 和账户之间的数据移动。」

扩展项页（`secabd3504cd`）给出同一思路在扩展上的落地：「对于在设备管理服务中注册的设备，文稿和键盘扩展项将遵循"受管理的打开方式"规则。例如，设备管理服务可帮助阻止用户将受管理的 App 中的文稿导出到未受管理的文稿提供程序，或阻止他们在受管理的 App 中使用未受管理的键盘。另外，App 开发者可阻止在其 App 中使用第三方键盘扩展项。」

三层主体在此各持有一道闸门：设备管理服务（策略）、系统（执行）、App 开发者（可选择拒绝第三方键盘）。

## 5.4 锁定模式：把围栏整体收紧一档

A 级（`sec2437264f0`）：「打开锁定模式时，设备不会像平时一样正常工作。为减小潜在可能被利用的攻击面，某些 App、网站和功能会受到严格限制以保证安全性，部分体验可能根本不可用。」原文列出受影响类别：后台服务、连接性、设备管理、FaceTime 通话、GameCenter、Safari 浏览器、系统设置、WebKit、信息。可用性口径为 iOS 16/iPadOS 16/macOS 13/watchOS 10 或更高，且「iOS 17、iPadOS 17、macOS 14 和 watchOS 10.1 更新或更高版本中提供了额外的保护」。

本篇不逐项复述各版本收紧了什么（原文以折叠列表给出，需逐版对照，属另一篇范围），只保留一个结构结论：锁定模式证明这些围栏**不是常量而是可被整体重配置的默认值**——因此任何"某 App 能访问 X"的结论都应带上"在何种模式下"。

## 5.5 macOS 文件同意模型：两个版本、两种粒度

A 级（`secddd1d86a6`）：

- 「在 macOS 10.15 或更高版本中，系统强制执行此模型，以帮助确保所有 App 在获得用户同意后才能访问"文稿"、"下载"、"桌面"、iCloud 云盘和网络宗卷中的文件。」
- 「在 macOS 10.13 或更高版本中，必须在"系统设置"（macOS 13 或更高版本）或"系统偏好设置"（macOS 12 或更低版本）中明确添加需要访问整个存储设备的 App。」
- 「此外，辅助功能和自动化功能需要用户许可，以帮助确保它们不会绕过其他保护措施。」
- 原文注释补充路径范围：「包括"桌面"、"文稿"、"下载"、网络宗卷和可移除宗卷」
- 设置路径：macOS 13+ 为"系统设置 > 隐私与安全性 > 隐私"，macOS 12 及更低为"系统偏好设置 > 安全性与隐私 > 隐私"。

三条读法：其一，10.15 的模型是**按区域**（文稿/下载/桌面/iCloud 云盘/网络宗卷）而非按路径的同意；其二，10.13 起的全盘访问是**按 App 显式添加**，粒度为整个存储设备；其三，「帮助确保它们不会绕过其他保护措施」这句是 Apple 自己对辅助功能/自动化与其他层关系的表述——它承认存在"绕过"的必要性风险，因此单独设门。

## 5.6 全盘访问：不能靠代码或键拿到

A 级（accessing-files 页）：「Your app can't automatically gain full disk access through an entitlement or with code: the person using your app must choose to grant access in System Settings > Privacy & Security.」并附防御性要求：「In situations where your app tries to use a file outside of its sandbox, program defensively by handling failure cases, as someone may choose not to grant full disk access to your app.」

这是"能力型"与"同意型"必须分离的最强证据：存在一条**任何键都换不到**的访问，只能由人在设置里授予。与之并读 `files.all`（弃用段）——键仍在文档中，但与全盘访问的实际授予不是同一路径（见 11.2 冲突表 C-06）。

## 5.7 数据保护类：对象自己决定"此刻能否被打开"

A 级（`secb010e978a`）：「在支持数据保护的设备上创建新文件时，创建它的 App 会为其分配一个类。每个类使用不同的策略来确定数据何时可被访问。」四个类与要点：

| 类 | 触发不可访问的条件（原文口径） |
| --- | --- |
| `NSFileProtectionComplete` | 「用户锁定设备后不久（如果"需要密码"设置为"立即"，则为 10 秒钟），解密的类密钥会被丢弃」；macOS 上「上一位用户退出登录不久后」 |
| `NSFileProtectionCompleteUnlessOpen` | 锁定/退出时允许写完已打开的文件；「一旦文件关闭，文件独有密钥就会从内存中擦除」；macOS 上「已登录或认证的任何系统用户均可访问」其私有部分 |
| `NSFileProtectionCompleteUntilFirstUserAuthentication` | 锁定或退出登录时类密钥**不**从内存删除；「这是未分配给数据保护类的所有第三方 App 数据的默认类」 |
| `NSFileProtectionNone` | 密钥仅受 UID 保护，「采用该类加密的唯一好处就是可以进行快速远程擦除」；「macOS 不支持该类」 |

另有两条范围限定：「基于 Apple 芯片的 Mac 电脑不支持 D 类：无保护」；「在运行 macOS 的设备上，对于未对应已启动操作系统的宗卷，只要该宗卷已装载，即可访问所有数据保护类。默认数据保护类为 NSFileProtectionCompleteUntilFirstUserAuthentication。」

在访问约束的图上，这一层的价值在于**与调用者无关**：无论沙盒、无论同意状态，锁屏后 `Complete` 类文件对任何进程都不可读。平台安全指南的应用侧口径（`sec1a976c067`）同时给出默认落点：「用户安装的 App 若没有选择加入某个特定数据保护类，则默认接受"首次用户认证前保护"。」即默认类是四档中最宽的一档之一，这一事实常被"iOS 上文件都加密"的说法掩盖。

## 5.8 对象层的其余三位：POSIX 权限、ACL、SIP

A 级（accessing-files 诊断段）把"沙盒没拦但打不开"分成四种，并给出可区分的错误码：

- POSIX 权限：「The app receives an error with the domain `NSPOSIXErrorDomain` and code 13 (`EACCES`).」
- POSIX.1e ACL：同样为 code 13（`EACCES`）
- SIP：「The operating system prevents software from modifying certain parts of the file system to prevent damage to installed software」，错误为 code 1（`EPERM`）
- 数据保护：「the file's data protection class is `complete` and the device is locked」，错误为 code 1（`EPERM`）

同段前置句：「Even in situations where App Sandbox permits your app to work with a file, discretionary access controls such as POSIX permissions or access control lists, or mandatory access controls built into macOS, may cause errors when your app attempts to use a file.」

这四种错误码构成实操上唯一可机器区分"哪一层拦住了我"的 A 级依据；沙盒自身的拒绝不走这些码，而走第 8 章的违规日志。

# 6. 第五道围栏：内核与运行时前置条件

## 6.1 载入时校验是访问许可的前提

A 级（`secf49cad4db`）：「运行时，所有可执行内存页的代码签名检查会在载入时进行，以帮助确保 App 自安装或上次更新之后未被修改过。」同页另一句给出分发侧前提：「（欧盟以外）而是所有 App 均必须下载自 App Store，在其中所有 App 都来自可识别身份的开发者且必须通过自动和人工审查。」

在访问约束的视角下这条的含义是：围栏所约束的那个进程**身份**本身是先被验证的。若身份校验可被绕过，第 3 章的全部键值判断都失去意义（谁在执行）——这正是 #85 中"执行主体"与本篇"许可判定"的接合点。

## 6.2 可执行内存：默认不可写又不可执行

A 级（`sec15bfe098e`）：

- 「iOS 和 iPadOS 使用 ARM 的 Execute Never (XN) 功能提供进一步的保护，该功能会将内存页标记为不可执行。」
- 「只有处于严格的控制条件下，App 才能使用标记为可写入和可执行的内存页：内核会检查 Apple 专有的动态代码签名授权是否存在。」
- 「即使如此，也只有单个 mmap 调用能用于请求一个可执行且可写入的内存页，系统为可执行且可写入的内存页分配了随机地址。Safari 浏览器对其 JavaScript 即时 (JIT) 编译器使用了此功能。」

三条一起构成对"访问内存这一系统资源"的最强约束：能力（键）→ 调用面（仅单个 mmap）→ 地址（随机）。macOS 侧的对应物是加固运行时的 JIT 与可写可执行内存键（`com.apple.security.cs.allow-jit` 用 `MAP_JIT`、`allow-unsigned-executable-memory` 可绕开 `MAP_JIT` 限制、`disable-executable-page-protection` 「to disable all code signing protections while launching an app, and during its execution」），三者的收紧程度依次下降。

## 6.3 加固运行时拦的是哪一类风险

A 级（hardened-runtime）：「The Hardened Runtime, along with System Integrity Protection (SIP), protects the runtime integrity of your software by preventing certain classes of exploits, like code injection, dynamically linked library (DLL) hijacking, and process memory space tampering.」以及范围限定：「The Hardened Runtime doesn't affect the operation of most apps, but it does disallow certain less common capabilities, like just-in-time (JIT) compilation.」与使用要求：「Make sure to use only the entitlements that are absolutely necessary for your app's functionality.」

Runtime Exceptions 六键（allow-jit / allow-unsigned-executable-memory / allow-dyld-environment-variables / disable-library-validation / disable-executable-page-protection / cs.debugger）本质上是**把围栏逐条让回去**的开关。其中 `com.apple.security.cs.debugger` 的语义直接涉及跨进程访问：「whether the app is a debugger and may attach to other processes or get task ports」。

## 6.4 Hardened process 与硬件内存标记：自愿加入的更细约束

A 级（Security entitlements 页 Hardened process / Hardware memory tagging 段）：

- `com.apple.security.hardened-process`：「whether the executable opts in to additional security checks」
- `.platform-restrictions`（整数）/`.platform-restrictions-string`：「the level of additional runtime security protections your app or extension opts in to」
- `.hardened-heap`：「additional hardening for heap allocations」
- `.dyld-ro`：「marks memory used for internal platform state as read-only」
- `.no-guard-objects`：「turns off guard objects for the process」
- `.checked-allocations`：「enables tagging of pointers and memory allocations」（MTE）
- `.checked-allocations.soft-mode`：「whether to log memory-tagging faults as a simulated crash, instead of terminating the process」
- `.checked-allocations.enable-pure-data` / `.no-tagged-receive`：分别控制纯数据内存打标与「prevent receiving tagged memory from other processes」
- `enforce-checked-pointer-arithmetic-overflow`：「enables checking pointers for arithmetic overflow」

这一组键在"访问约束"图上的位置特殊：它们约束的不是"能访问什么外部对象"，而是"进程自己的内存可否被破坏"。`.no-tagged-receive` 是唯一一条把边界重新拉回跨进程的键——拒绝接收其他进程的标志内存。注意全部为 opt-in，因此不能作为平台默认保护来陈述。

## 6.5 系统完整性与只读结构

A 级：SIP 的拦截结果由 5.8 的 `EPERM` 体现；平台安全指南侧（`sec35dd877d0`）给出的执行口径为「端点保护；对 App 强制执行代码签名以帮助确保仅运行受信任的软件」。操作系统分区只读（`sec15bfe098e`）与签名系统宗卷（`secd698747c9`，#85 已详述）共同使"改系统文件"这一类访问从一开始就不在许可集合内。本篇不重复 #85 的执行主体分析，只标记接合点：SIP 属对象型约束（路径自带保护），而非 App 能力的补集。

## 6.6 进程间访问：默认不可达

A 级依据分散在三处，合起来给出跨进程访问的默认答案：

- 「它们无权访问彼此的文件或内存空间。」（扩展项，`secabd3504cd`）
- 「API 不允许 App 提升自己的权限来修改其他 App」（`sec15bfe098e`）
- 「Sending Apple Events to arbitrary apps」属被禁止活动（protecting-user-data 清单）；「Terminating other running apps」同列
- macOS 侧「When an app that uses App Sandbox launches a helper tool directly using `Process` or fork and exec, the helper tool inherits the same sandbox capabilities as the launching app.」

最后一条是重要的**能力扩散路径**：直接 fork/exec 出去的子工具并不减弱，而是全盘继承。因此"拆分组件以降低风险"必须换成特定形态才生效（见 8.2）。

# 7. 第六道围栏：系统资源配额

## 7.1 后台执行不是可申请的自由

A 级（`sec15bfe098e`）：「此外，App 只能通过系统提供的 API 来执行后台处理。这就使 App 能够继续运行，而不会降低性能或显著影响电池续航能力。」这句是配额层的根陈述：后台不是一个可申请移除的限制，而是"只能通过系统提供的 API"这一结构性约束。

## 7.2 内存与地址空间：四枚键改写上限

A 级（Bundle Resources Entitlements 页 Memory / Metal / Enterprise 段）：

| 键 | 原文语义 |
| --- | --- |
| `com.apple.developer.kernel.increased-memory-limit` | 「whether core features of your app may perform better with a higher memory limit on supported devices」 |
| `com.apple.developer.kernel.extended-virtual-addressing` | 「whether the app may access an extended address space」 |
| `com.apple.developer.sustained-execution` | 「whether your app performs consistently when the system constrains it to a sustainable performance level」 |
| `com.apple.developer.app-compute-category` | 「allows an app to adjust thresholds that balance thermal dissipation and performance against fan noise and other factors」 |

四条中只有第一条与第二条直接抬高上限；`sustained-execution` 的措辞是"在被系统约束到可持续水平时仍表现一致"，属**行为声明**而非配额授予，`app-compute-category` 则是"调整阈值"。把四条并排可以避免社区常见的"申请即得更多内存"这一过度表述。内存上限的具体数值不在这些页面上，属未披露（U-04）。

## 7.3 GPU、神经引擎与传感器类配额

A 级同页可确认存在按硬件单元切分的键：Background GPU Access（`continued-processing.gpu`，「the entitlement the system requires for a continuous background task to use the GPU」）、Background Inference（在 Neural Engine 上跑推理）、`com.apple.developer.coreml.neural-engine-access`（「can use the Apple Neural Engine to speed up CoreML」）、`com.apple.developer.sensorkit.reader.allow`（限于「required by your app's preapproved research study」）。传感器键的限定语（预先核准的研究项目）说明该类配额连"申请"路径都不对普通开发者开放。

## 7.4 虚拟化与共享内存的转移

A 级：Hypervisor 段四键（`com.apple.security.hypervisor`、`com.apple.vm.hypervisor`、`com.apple.vm.device-access`、`com.apple.vm.networking`）分别控制创建虚拟机、捕获 USB 设备进 guest、以及「manages virtual network interfaces without escalating privileges to the root user」；`com.apple.security.virtualization` 控制 Virtualization framework。浏览器引擎段中 `com.apple.developer.memory.transfer_accept` / `transfer_send` 允许「increase memory」与「transfer memory」在渲染/Web Content 扩展之间转移——这是**配额在进程间转移**的显式机制，值得单列。

# 8. 明确被禁止的活动与三条替代路径

## 8.1 沙盒下由操作系统禁止的九类活动

A 级（protecting-user-data："Review functionality that is incompatible with App Sandbox"）：「Certain activities are forbidden by the operating system when an app runs in a sandbox.」原文清单：

1. 使用 Authorization Services API；
2. 在辅助功能类 App 中使用 accessibility API；
3. 向任意 App 发送 Apple Events；
4. 在分布式通知的 `userInfo` 字典中向其他 task 发送该字典；
5. 加载内核扩展；
6. 在开放/存储对话框中模拟用户输入；
7. 访问或修改其他 App 的偏好设置；
8. 配置网络设置；
9. 终止其他正在运行的 App。

这份清单的分析价值在于它的**共性**：九条全都是"以某个其他主体为作用对象"的动作（提权、注入、代答、改动他人配置、杀掉他人进程）。它与第 5.5 的"辅助功能和自动化需要用户许可，以帮助确保它们不会绕过其他保护措施"形成呼应——第 2 条与第 3 条正是那两个绕过通道的能力面。

## 8.2 三条被文档认可的解决路径

A 级（diagnosing-violations）给出三种处置：「Extend the app's capabilities to cover the attempted task, by adding a capability to the App Sandbox entitlement for the app executable / Distribute a separate helper tool with your app that uses the additional capability / Remove the code that accesses the additional capability if it's not necessary for implementing your app's feature set.」

关于第二条的机制细节，同页给出**继承与隔离的分界**：直接 `Process` 或 fork/exec 会「inherits the same sandbox capabilities as the launching app」；要为不同组件配置不同能力，须使用以下三种形态之一：XPC service、login item、或把 `LSBackgroundOnly` 设为后台的 helper app。设计动机句为：「Separating the features makes it harder for an attacker who compromises the app through the network to gain access to someone's camera, or to exploit the potentially unsafe processing code.」

## 8.3 嵌入式工具的默认继承

A 级两句共同决定嵌入形态的安全收益：

- 「If your macOS app embeds a command-line tool, that tool must inherit the containing app's sandbox configuration.」
- 6.6 的宿主继承规则（库/框架/进程内插件继承宿主可执行文件）

因此"把高危代码放进 App 内部"不产生隔离收益；只有独立地址空间（扩展项、XPC、独立进程）才被重新切分容器与权限。

# 9. 判定顺序、失效传播与失败形态

## 9.1 一个访问请求经过的判定链

把前述各层按时间排序，可得到本篇的核心模型。以"macOS 沙盒 App 打开用户此前授权过的一个文档"为例：

| 步 | 判定 | 依据层 | 依据（A 级出处） | 失败形态 |
| --- | --- | --- | --- | --- |
| 1 | 该可执行页签名有效、身份成立 | 完整性执行 | `secf49cad4db` | 页无法载入（#85） |
| 2 | 该 App 是否带沙盒总开关 | 能力 | `com.apple.security.app-sandbox` | 无该键则不进入沙盒模型，但仍受 5.5/5.6 同意层与对象层约束 |
| 3 | 目标路径是否在容器/组容器/已扩展作用域内 | 视图 + 能力 | accessing-files 各段 | 沙盒违规日志（第 10.3 节形态） |
| 4 | 是否需要面板/书签带来的作用域 | 同意 | 4.2–4.5 | 隐式作用域缺失 → 拒读 |
| 5 | 该路径是否属受保护区域、是否已获同意 | 同意 | `secddd1d86a6` | 提示或静默失败，取决于是否已授予 |
| 6 | 是否为 SIP 保护路径 | 对象 | accessing-files 诊断段 | `EPERM`(1) |
| 7 | POSIX 权限/ACL 是否允许该用户 | 对象 | 同上 | `EACCES`(13) |
| 8 | 文件的数据保护类此刻是否可解密 | 对象 | `secb010e978a` | `EPERM`(1) |
| 9 | 若涉及硬件/个人信息，是否另有键与同意 | 能力 + 同意 | 3.5/3.6/5.1 | 请求失败或被拒 |

链条的关键结论：**第 2 步通过不代表第 5 步通过**。不存在"沙盒外即自由"的短路——这是 macOS 与"沙盒是唯一门禁"这一直觉最大的差别（依据即 5.2 的"无论尝试访问的 App 是否本身已沙盒化"）。

## 9.2 iOS 侧的等价链条

iOS/iPadOS/visionOS 上顺序不同：视图围栏（随机主目录、白名单服务）在能力键之前，同意层由 5.1 的目录统一执行，对象层由数据保险箱 + 数据保护类执行。第 3 步的"扩展作用域"机制在 iOS 侧在本次核查覆盖的页面中没有对应描述（U-02）。

## 9.3 三层失效传播

| 若前置失效 | 受影响层 | 传播方式 |
| --- | --- | --- |
| 载入时代码签名校验被绕过 | 1 之后所有层 | 未签身份可携带任意已获批键值，能力型围栏退化为装饰 |
| 同意状态存储被篡改或系统设置被绕过 | 5.1–5.6 | 能力键 + 对象可读 → 数据静默可达；数据保护类成为最后防线 |
| 锁屏后仍保留 `Complete` 类解密能力（即类密钥未丢弃） | 5.7 | 唯一与调用者无关的约束消失 |
| 面板进程被冒充或书签被劫持 | 4.2–4.4 | 隐式扩域会把信任直接送进进程，无需用户再次确认 |
| `Process`/fork 继承被用于扩散能力 | 8.2/8.3 | 子工具拿到父进程全部能力，拆分设计失效 |

第 4、5 行是本篇特别强调的两条：它们的失效不需要提权，只需要**按设计使用设计**（把书签传出去、把工具 fork 出去）。

## 9.4 失败信息的可读性分级

| 分级 | 内容 | 可否机器判定 |
| --- | --- | --- |
| 强 | POSIX 错误域 + code 13 / code 1 | 可，且可区分 ACL/权限 vs SIP/数据保护（但不能在 `code 1` 的两个来源之间区分） |
| 中 | 沙盒违规日志（`Violation: deny(1) …`） | 可，含进程、动作、路径与回溯 |
| 弱 | 系统库写入 stderr 的提示（如「please add an exception」） | 半可，文本非契约 |
| 无 | 同意层被拒后的静默失败、iOS 侧沙盒内部判定 | 不可（详见第 10 章） |

`code 1` 在 SIP 与数据保护两处共用，是公开文档给出的唯一"无法区分"情形；诊断时须借助其它信号（路径是否属 SIP 保护集合、设备是否锁定）交叉判定，这两条辅助路径本身不在开发者文档中，属推论（A-推论）。

# 10. 可观测性：能查到什么、查不到什么

## 10.1 查"是否被沙盒化"：两种官方手段

A 级（protecting-user-data："Verify that your app uses App Sandbox"）给出两条路径：

- 活动监视器：「Choose View > Columns, and choose Sandbox among the list of possible columns to monitor」，然后「verify that the value in the Sandbox column is Yes」。
- 命令行：「To verify your app's App Sandbox configuration in Terminal, use the `codesign` command」，即 `codesign -dvvv --entitlements - <path to your app>`，带该键时输出中出现 `[Key] com.apple.security.app-sandbox` 与 `[Bool] true`。

两条的差别值得记录：活动监视器看的是**运行时状态**（进程是否处于沙盒中），`codesign` 看的是**签名内声明**。二者不一致时（例如声明有键但运行时未进入沙盒），本篇不给出判定规则——公开文档未描述该情形（U-05）。

## 10.2 查"是谁拦下了我"

A 级（diagnosing-violations）给出 Console 的三条检索：

```
type:error
subsystem:com.apple.sandbox.reporting
category:violation
```

并说明输出结构：「The message names the process that violated the App Sandbox restrictions, the violating action, and provides additional information about the violation where possible.」样例首行为 `Sandbox: SampleApp(12447) deny(1) network-outbound /private/var/run/mDNSResponder`，且「The line in the Console message that starts with "Violation:" describes the app's behavior that violated its App Sandbox restrictions.」

两条关于该日志的性质陈述尤其重要：

- 「The backtrace is similar to a crash report, even though the system doesn't terminate apps that violate App Sandbox restrictions.」——违规日志不是崩溃报告，进程继续存活。
- 「If an App Sandbox constraint stops a system library from performing a task, that library may log a message to your app's standard error stream to report the problem.」——用词是 **may**，因此 stderr 提示不保证存在。样例中同时出现 mDNSResponder 套接字连接失败、「Sandbox is preventing this process from reading networkd settings file … please add an exception」与「Sandbox does not allow access to com.apple.dnssd.service」三种表述形态，说明同一原因在日志里没有统一格式。

## 10.3 可观测性的三档归纳

| 档 | 内容 | 说明 |
| --- | --- | --- |
| 一档（结构化、可自动化） | 签名中的键集合（`codesign --entitlements -`）、沙盒违规日志的 `deny(N) 动作 路径`、POSIX `errno` | 可直接进流水线断言 |
| 二档（半结构化） | stderr 文本提示、活动监视器 Sandbox 列、设置中的同意开关状态 | 需要人工或启发式解析 |
| 三档（不可观测） | iOS 侧沙盒内部判定过程、被拒后未提示的访问、数据保护类的类密钥驻留时机（仅有"锁定后不久""10 秒钟"等描述） | 只能按文档口径推断，不得当作可验证事实 |

由此得出本篇的观察纪律：**数据（键与日志）→ 结果（成功/errno）→ 组件（哪一层拦的）**，前两者可查，第三者常需推断。第 9.4 的分级表即此纪律的展开。

## 10.4 本篇未使用的手段

未包含任何"越狱检测点位""规避沙盒的做法""伪造授权属性"内容。第 9.3 的失效传播只用于说明**防御纵深为何需要各层同时成立**，不构成操作步骤；如需评估具体设备状态，应交由合规的取证流程。

# 11. 平台差异与文档差异

## 11.1 iOS/iPadOS/visionOS 与 macOS 的十项差异

| 维度 | iOS/iPadOS/visionOS | macOS | 依据 |
| --- | --- | --- | --- |
| 沙盒强制性 | 「所有第三方 App 均已经过沙盒化」 | 由开发者选择；仅 Mac App Store 分发强制 | A：`sec15bfe098e` / app-sandbox 页 |
| 容器创建时机 | 安装时分配唯一主目录（随机） | 首次启动沙盒 App 时创建容器 | A：`sec15bfe098e` / accessing-files |
| 外推机制 | 只能通过系统明确提供的服务 | 面板、书签、组容器、相关文件等六类 | A：`sec15bfe098e` / 4.1 |
| 文件区域同意 | 数据保险箱 + 同意目录 | macOS 10.15+ 按区域同意（文稿/下载/桌面/iCloud 云盘/网络宗卷） | A：`secc01781f46` / `secddd1d86a6` |
| 全盘访问 | 文档未使用该概念 | 需在"系统设置 > 隐私与安全性"显式授予，且不能靠键或代码 | A：`secddd1d86a6` / accessing-files |
| 内核扩展 | 文档不在第三方 App 语境讨论 | 沙盒下禁止加载；建议迁移到系统扩展/DriverKit | A：8.1 / `seca48c92d43` |
| 加固运行时 | 页面未涉及 | 公证上传前必须启用 | A：hardened-runtime |
| 数据保护类 `None` | 支持（可快速远程擦除） | 「macOS 不支持该类」 | A：`secb010e978a` |
| 退出登录边界 | 以锁定/解锁为条件 | 以登录/退出登录为条件 | A：`secb010e978a` |
| 未装载宗卷 | 文档未讨论 | 「只要该宗卷已装载，即可访问所有数据保护类」 | A：`secb010e978a` |

## 11.2 口径冲突表

| 编号 | 冲突表述 | 各自出处 | 本篇处理 |
| --- | --- | --- | --- |
| C-01 | "App Sandbox" 在平台安全指南中作为 iOS 设计原则出现（"严格的沙盒化"），在开发者文档中标题限定为 macOS apps | `sec35dd877d0` / `Security/app-sandbox` | 分平台陈述，不外推 |
| C-02 | "所有 App 默认需要 Apple 公证才能启动"（macOS 10.15+）与"用户可运行完全未签名和不受信任的代码"并存 | `sec35dd877d0` / `secf826eff27` | 两者并列，指出前者可被用户/管理策略改变，原文未给出优先级 |
| C-03 | 键盘扩展"默认运行在非常受限的沙盒"与"开发者可要求其扩展项拥有'开放存取'权限" | `secabd3504cd` | 记为同一机制的默认值与可申请值，避免"键盘一定无网络" |
| C-04 | App Sandbox 集合页 Hardware 段列 5 项，Security entitlements 页 Device access 段列 7 项（多 `audio-input`、`smartcard`） | 两页 | 取并集并标注来源，不称"官方清单即此" |
| C-05 | 数据保险箱（data vault）与文件保险箱（FileVault）在中文里同称"保险箱" | `secc01781f46` / `sec4c6dc1b6e` | 术语表分列，正文不互换 |
| C-06 | `com.apple.security.files.all` 仍在文档但归入 Deprecated；全盘访问又称不能通过 entitlement 获得 | Entitlements 弃用段 / accessing-files | 记为两条不同路径，不以键的存在推断可得性 |
| C-07 | "授权"（entitlement）与"许可/同意"（consent）在中文页中均被译作"权限" | `sec15bfe098e` / `secc01781f46` | 全文使用"能力型键"与"同意层"两个术语区分 |
| C-08 | 沙盒违规"系统不终止 App"与违规日志"similar to a crash report" | diagnosing-violations | 保留两句原文，说明形式与后果不同 |
| C-09 | 平台安全指南中文页发布日期为 2024 年，页面归属的指南为 2026 年版权 | 各页脚注 | 见 11.3 版次提醒 |

## 11.3 版次提醒与三组必须并读的页面

中文页面页脚标注的发布日期（如 2024 年 12 月 19 日、2024 年 5 月 7 日）与该指南的版权年份（2026）不同步。**如何应用**：任何"某机制自某版本起存在"的结论都应以英文原页与最新指南 PDF 复核后再落库；本篇所有版本号（iOS 13.4/15/16、macOS 10.7/10.13/10.15/13/14/11、watchOS 7/10）直接摘自上述页面，未经英文版复核前不应用作合规判定依据。

三组必须并读：

- `sec15bfe098e` + `secddd1d86a6` + `secc01781f46`：单读任一页都会得出"沙盒/同意/数据保险箱其中之一即全部限制"的错误结论。
- `Security/app-sandbox` + `protecting-user-data` + `accessing-files`：三页分别是集合页、约束清单、外推机制，缺任一项就会把"禁止"与"需扩展"混淆。
- `secabd3504cd` + `secd3c881cee` + `hardened-runtime`：隔离层的三种形态（扩展项、数据前处理、运行时），共同说明"隔离"不等于"沙盒"。

## 11.4 未披露项清单（U-01 — U-08）

下列条目在本篇核查覆盖的 18 个 A 级页面中**没有对应陈述**。列出它们是为了阻止后续引用把这些当作已知事实。

| 编号 | 未披露内容 | 常被误当作已知的形式 | 若要证实需查 |
| --- | --- | --- | --- |
| U-01 | iOS/iPadOS/visionOS 上第三方 App 沙盒的实现结构（策略描述语言、守护进程、profile 下发与校验路径） | 直接以 macOS 的 App Sandbox 机制套写到 iOS | 未在公开指南中披露；只能在系统开源页（若相关组件公开）与实测定位中找 |
| U-02 | iOS 侧是否存在与 macOS 面板/书签等价的"作用域外推"机制及其形式 | 把 macOS 的 security-scoped 语义写成全平台通用 | 平台安全指南 iOS 页只给"只能通过明确提供的服务"，未展开 |
| U-03 | 同意状态存储的位置、格式与保护方式（macOS 与 iOS 各自） | 断言同意状态"不可篡改"或"存于某文件" | 未在核查页面中出现 |
| U-04 | 各资源配额的具体数值：内存上限阈值、后台执行时间预算、可持续性能判据 | 引用社区测量值当作 Apple 规定值 | 开发者文档只给键语义，无数值 |
| U-05 | "签名中有沙盒键但运行时未进入沙盒"这一不一致情形的官方判定规则 | 假定两者必然一致 | 两页分别只描述各自的观测方式 |
| U-06 | 未沙盒化 macOS App 在默认情况下可访问的确切路径集合边界 | 写成"未沙盒即可读全盘"（与 5.5/5.6 冲突） | 文档给"无 unrestricted 访问 home"（沙盒）与"关键区域自我保护"，未给未沙盒进程的正向清单 |
| U-07 | 数据保险箱与数据保护类在文件系统层的实现耦合方式（元数据、扩展属性） | 使用第三方实现细节冒充官方口径 | 未在核查页面中出现 |
| U-08 | 各围栏层的相对优先级与冲突仲裁顺序（如同意与 MDM 策略冲突时） | 自行编排"第 1 层必在第 2 层之前"的确定顺序 | 本篇 9.1 链条是基于文档陈述建立的**分析模型**，非官方顺序定义 |

配套的不确定性说明：5.1 与 5.5 的类别清单是**枚举式**而非"完整封闭集"表述，原文未声明清单是否穷尽；因此不可把"某类别未被列出"读成"该类别不受同意层强制执行"（U-09 归入本条）。

# 12. 结论

## 12.1 公开口径能支持的四类结论

1. **多主体、多层，不存在单一"沙盒"。** 六道围栏（视图、能力、同意、对象、配额、隔离）由不同主体执行：装载期（随机主目录）、签名与内核载入检查（键与页校验）、系统框架（面板进程、扩展项协调 IPC）、文件系统与密钥层（数据保险箱、数据保护类）、设置存储（同意状态）、以及资源调度（后台、内存、热）。把它们统称"沙盒"会掩盖三处关键差异（C-01、C-05、C-07）。
2. **iOS 与 macOS 的默认方向不同，且这一不同有 A 级依据。** iOS 侧是白名单结构（「只能通过……明确提供的服务来实现」），macOS 侧是窄默认视图 + 六种受控外推。因此同一句"该 App 无法访问用户文件"在两个平台上的成立方式与失效条件完全不同。
3. **对象型约束独立于调用者状态。** 「甚至本身未沙盒化的进程也不可访问」（数据保险箱）与「无论尝试访问的 App 是否本身已沙盒化」（macOS 关键区域自我保护）是同一设计意图的两处表述：把最后一道防线放在数据上，而非放在进程上。
4. **围栏的默认值可被整体重配置，且部分能力永不可申请。** 锁定模式一次性收紧若干类别（5.4），而全盘访问明确无法通过键或代码获得（5.6），传感器访问仅限预先核准研究（7.3）。"最小特权"在 Apple 文档里不是修辞，而是有明确不可越界项的清单。

## 12.2 公开口径不能支持的四类结论

1. 不能给出 iOS 沙盒的实现结构（策略语言、守护进程、profile 下发路径）——本次核查覆盖的页面内未出现，属 U-01。
2. 不能给出各类资源上限的具体数值（内存阈值、后台时间预算）——U-04。
3. 不能断言"某 App 已越出围栏"或把违规日志、errno 单独作为设备被入侵的证据——日志非契约（10.2）、`code 1` 不能区分来源（9.4）。
4. 不能把 opt-in 键（6.4 全部、加固运行时异常键）当作平台默认保护，也不能把"未沙盒化的 macOS App"当作违反 Apple 文档——后者在 Mac App Store 之外是文档承认的合法选择（3.2）。

## 12.3 分角色建议

**给评估者（做平台机制核查时）**

- 先固定四件输入再作判断：哪一版指南、哪一版系统、目标 App 的分发渠道、该 App 签名中的键集合。缺任一项，第 9.1 的链条无法定位。
- 报告结论按"层"而非"机制名"归类，并显式写明每层在两个平台上的差异。
- 遇到"绕过"类描述（5.5 的辅助功能与自动化、8.1 的九项禁令），把它读成 Apple 自己对通道风险的承认，而不是可执行路径。

**给开发者（做最小特权设计时）**

- 以"值为 false 时不写入该键"为签名审查基线（3.7），使键集合可直接审计。
- 拆分组件时必须选择 XPC service / login item / 后台 helper 之一；`Process` 或 fork/exec 不产生隔离收益（8.2）。
- 需要跨重启的访问再引入安全作用域书签；跨进程传书签时考虑 `withoutImplicitStartAccessing` 以推迟扩域（4.3、4.4）。
- 声明的键与用户可见用途应保持一致（#86 第 10 章的同一纪律），不申请 `files.all` 类弃用路径。

**给管理与合规**

- 设备侧应把"锁定模式"与"访问限制（受管理与用户 App 隔离）"视为可配置纵深，而非可选装饰（5.3、5.4）。
- 迁移内核扩展类软件到系统扩展/DriverKit 是 Apple 明确建议的方向（2.7），在评估遗留软件风险时可直接引用该口径。
- 对依赖"沙盒已启用"结论的策略，应同时校验分发渠道（Mac App Store / 公证）与组容器四条件（4.6）。

## 12.4 一句话总结

限制应用访问的机制在 Apple 文档中不是一个"沙盒"，而是一条**由身份校验、视图切分、能力键、用户同意、对象自带条件与资源配额组成的判定链**：链条上任一环单独成立都不足以支撑"数据安全"这一结论，而其中数据保护类与同意层是仅有的两处与调用者状态无关的防线。

# 附录 A：来源、术语与口径声明

## A.1 来源与版次

**A 级——《Apple 平台安全保护》（中文版，版权 2026，访问日期 2026-10-21）**

| 页面编号 | 标题 | 页内标注发布日期 | 本篇用途 |
| --- | --- | --- | --- |
| `sec35dd877d0` | App 安全性概览 | 未在提取段显示 | 设计原则、公证默认要求、关键区域自我保护 |
| `secf49cad4db` | iOS、iPadOS 和 visionOS 中的 App 安全性介绍 | 2024-12-19 | 载入时按页校验、分发前提 |
| `sec15bfe098e` | iOS、iPadOS 和 visionOS 中的运行时进程安全性 | 2024-12-19 | 沙盒、随机主目录、`mobile`、只读分区、授权、后台、ASLR、XN |
| `sec1a976c067` | iOS、iPadOS 和 visionOS 中的 App 保护和 App 组 | 2024-12-19 | SDK 数据保护、默认类、组容器与 GID |
| `secc01781f46` | 保护 App 对用户数据的访问 | 未在提取段显示 | 数据保险箱、同意目录、iOS 15 新类、iCloud 云盘、访问限制 |
| `secddd1d86a6` | 在 macOS 中控制 App 对文件的访问 | 未在提取段显示 | 10.15 文件同意模型、10.13 全盘、辅助功能与自动化 |
| `secb010e978a` | 数据保护类 | 2024-12-19 | 四类保护与平台差异 |
| `secabd3504cd` | iOS、iPadOS、macOS 和 visionOS 中支持的扩展项 | 2024-12-19 | 扩展项地址空间、权限继承、键盘沙盒、受管理的打开方式 |
| `secd3c881cee` | "信息"和 IDS 的 BlastDoor | 未在提取段显示 | 数据前隔离 |
| `sec2437264f0` | Apple 服务的锁定模式安全性 | 2024-05-07 | 整体降权模式与受影响类别 |
| `seca48c92d43` | macOS 的 DriverKit 安全性 | 未在提取段显示 | 用户空间驱动 |
| `secf826eff27` | macOS 中的 App 安全性介绍 | 未在提取段显示 | 用户可选择运行未签名代码 |
| `secd698747c9`、`sec4c6dc1b6e` | 签名系统宗卷安全性、使用文件保险箱加密宗卷 | 未展开引用 | 仅作术语对照（5.2、11.1） |

**A 级——Apple 开发者文档（访问日期 2026-10-21）**

| 页面 | 用途 |
| --- | --- |
| `documentation/Security/app-sandbox` | App Sandbox 目标陈述与 Mac App Store 强制 |
| `documentation/Security/protecting-user-data-with-app-sandbox` | 容器语义、九项禁止活动、验证手段 |
| `documentation/Security/accessing-files-from-the-macos-app-sandbox` | 容器路径、macOS 14 签名关联、面板、书签、组容器、四类打不开原因 |
| `documentation/Security/discovering-and-diagnosing-app-sandbox-violations` | Console 检索、违规日志结构、三种处置、helper 形态 |
| `documentation/Security/hardened-runtime` | 加固运行时定义、继承规则、系统扩展限制、公证要求 |
| `documentation/BundleResources/Entitlements` | 授权定义、合成链、各段键清单 |
| `documentation/BundleResources/security-entitlements` | 文件/网络/设备/个人信息/自动化/hardened-process/MTE/组键 |
| `documentation/BundleResources/Entitlements/com.apple.security.app-sandbox` | 总开关语义与 macOS 10.7 可用性 |
| `documentation/BundleResources/Entitlements/com.apple.security.files.user-selected.read-only` | 用户选中文件语义与 Xcode 启用路径 |

**B/C 级材料说明**：本篇正文未采信社区或第三方对沙盒实现（策略语言、守护进程名、profile 结构）的描述。若后续需引入，应作为待验证条目写入 11.2 冲突表，而不是并入正文。

## A.2 术语表

| 术语 | 本篇含义 | 不等同于 |
| --- | --- | --- |
| 能力型键（授权属性） | 签名内键值对，决定能否发起某类请求 | 用户同意 |
| 同意层 | 设置/面板中由人授予的一次或长期放行 | 授权键 |
| 对象型约束 | 附着在文件、路径或宗卷自身上的条件 | 进程沙盒状态 |
| 沙盒容器 | 首次启动时为沙盒 App 创建的目录（macOS 在 `~/Library/Containers`） | iOS 上安装期分配的"唯一主目录"（相关但不等价） |
| 数据保险箱 | data vault，阻止未经授权访问 App 数据，对未沙盒化进程亦有效 | 文件保险箱 FileVault |
| 文件保险箱 | FileVault，macOS 宗卷加密 | 数据保险箱、钥匙串 |
| 安全作用域访问 | URL 级访问作用域，`start/stopAccessingSecurityScopedResource()` | 路径权限 |
| 带安全作用域的书签 | 跨进程退出仍有效的访问凭证 | 普通书签数据 |
| 面板（开放/存储） | 由独立进程展示、用于扩展沙盒作用域的界面 | App 内文件选择器 |
| App 组 / GID | 多 App 共享容器的标识，唯一性由开发者网站保证 | 钥匙串访问组 |
| 扩展项 | 独立地址空间、系统协调 IPC 的已签名可执行代码 | 内核扩展 |
| 加固运行时 | 与 SIP 共同保护运行时完整性的一组默认限制 | App Sandbox |
| 违规日志 | `subsystem:com.apple.sandbox.reporting` 下的 deny 记录 | 崩溃报告 |
| `EACCES`(13) / `EPERM`(1) | POSIX 权限与 ACL / SIP 与数据保护 | 沙盒拒绝 |
| 锁定模式 | 可选的整体降权模式 | MDM 配置 |
| 未沙盒化进程 | 不带沙盒总开关的 macOS 进程，仍受对象型与同意层约束 | 提权进程 |

## A.3 口径声明

1. 本篇为**防御性、教育性**的平台机制分析，不包含任何利用方法、绕过步骤、越狱或重签名流程、伪造授权属性或设备证明的做法，也不提供用于规避风控、检测或审批的建议。
2. 所有 A 级引句摘自 2026-10-21 访问的 Apple 官方页面中文正文或英文原文，标注页面编号；引用长度为单句，用于口径核对，不构成对整页内容的复述或替代。
3. 原文限定语（"设计旨在""帮助确保""帮助阻止""可""绝大部分""可能"）在转述中保留，任何升格均为误读。
4. "本次核查覆盖页面内未出现"仅表示该主题不在本篇所核查页面的正文中，不表示该机制不存在或不成立。
5. 版本号为相应页面所载，未经英文原页与最新指南 PDF 复核前不应用于合规或安全判定结论（见 11.3）。
6. 失效传播（9.3）与判定链（9.1）是本篇为说明防御纵深依赖关系而建立的分析模型，标注为推论的部分不属 Apple 官方陈述。
7. 本篇不构成对任何具体设备、具体 App 或具体版本的安全状态判定；此类判定需在该设备上按第 10 章手段实测。

**联系方式**：Telegram: https://t.me/one00190



