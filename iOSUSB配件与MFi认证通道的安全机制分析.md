@@TITLE@@iOS 系统USB配件与MFi认证通道的安全机制分析
@@SUBTITLE@@配件认证那句"完全由定制集成电路来处理，而且对于配件本身是透明的"、数据连接那三个时间常数（1 小时 / 30 天 / 3 天）与 Apple 自认的"配件生态系统不提供在数据连接建立前识别配件的可靠加密方式"、以及两个反常事实：在 257 份顾问页里，"USB 受限模式"只出现在两个段落名下——`Accessibility` 四次、`Trusted Device` 一次，而 `USB`、`Lightning`、`MFi` 作为段落名一次都不存在；本族 71 段里唯一把"插上一个配件"这个动作本身写成 Impact 主语的条目是 CVE-2026-86924（容错重测后的三层文本实测版）
@@INFO@@报告日期|2026 年 10 月 26 日
@@INFO@@分析对象|《Apple 平台安全保护》zh-CN 指南 179 页全量转储 + 英文侧 27 页取样（配件验证、安全激活数据连接、直接内存访问保护、激活锁、DriverKit、Apple Configurator、硬件麦克风断联、HomeKit、安全研究设备等）；Apple 安全响应顾问页 257 份英文页面镜像（容错解析后 9,045 个段落 / 470 个不同段落名），配件族核心 71 个段落 + 逐条分析的相邻段落 44 个（CoreUtils 16、Power Services 8、DriverKit 8、MobileLockdown 4、Game Controllers 3、SDK 2、srd_tools 2、Trusted Device 1），另有 DiskArbitration 28、BiometricKit 8 只作段落名计数
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含针对他人设备的操作步骤、不含配件仿制、证书重放或认证集成电路克隆的实施细节
@@INFO@@分类|物理接口与配件信任链 / 认证授权门与数据连接门的双门结构 / 官方手册层措辞与顾问页措辞的**互不点名**对照
@@INFO@@难度|进阶（第 1 章的"两道门"区分与第 12 章的双向沉默是全篇前提；跳过它们会把手册层的机制句读成协议声明，把措辞层的段落名读成实现层）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

一条 USB-C 线插进一台锁定的 iPhone，会发生什么？

对这个问题，公开文本里有两套完全不同的回答，而且它们**互相不叫对方的名字**。

《Apple 平台安全保护》那一侧的回答是一组机制名：MFi 许可计划、iPod 配件协议 (iAP)、定制集成电路 (IC)、授权证明、质询、已签名的证书、模拟音频降级、安全激活数据连接、1 小时、30 天、3 天。它把这条通道拆成**两道门**——一道门管"这个配件是不是被批准的"，另一道门管"现在允不允许它建立数据连接"。

Apple 安全响应顾问页那一侧的回答是另一组词：`A physical attack may disable USB Restricted Mode on a locked device`、`USB Restricted Mode may not be applied to accessories connected during boot`、`A malicious accessory may be able to cause unexpected app termination`、`An attacker with a USB-C connection to an unlocked device`。它把这条通道拆成**一批后果**，然后把每条后果挂到一个组件段落名底下。

本篇要做的第一件事，是把这两套词汇逐条对上，并且**如实记下对不上的部分**。最刺眼的一处对不上是：**"USB Restricted Mode"这个名字在手册层 179 页里出现 0 次**，而手册层描述同一道防线时用的标题是"安全激活数据连接"；反过来，**"MFi"在 257 份顾问页里只出现在 1 页上，而且只出现在 `Available for: MFi Program` 这个取值里**——它是受影响对象，从来不是组件段落名。

第二件事比第一件更有信息量。前两篇（#103 蓝牙与近场配对、#104 连续互通）都得到一个负结果：近场段落族里 `actively exploited`、`man-in-the-middle`、`eavesdrop` 全是 0。本篇要给出这条规律的**边界**，而这条边界正好推翻了"物理接口族也没有在野声明"这个想当然的读法：在本期 257 份镜像里，`Apple is aware of a report` 共命中 **93 个段落 / 65 页**，其中 **4 段挂在 `Accessibility` 段落名下，而那 4 段的 Impact 句讲的就是 USB 受限模式被物理攻击禁用**。换句话说，**Apple 承认被在野利用的那条配件相关的缺陷，在措辞层里没有登记在任何"USB"名字之下，而是登记在辅助功能之下。**

这就是本篇的落点：**在 Apple 的公开文本体系里，"配件"这件事被拆给了两套互不点名的词汇，而真正被承认的攻击案例落在两套词汇的缝隙里。**

## 0.1 本篇的实测数字

下表是全部结论的原始分母，2026 年 10 月 26 日**用容错解析器重测**，不引用二手统计。全部"全语料"计数都指**本期 257 份镜像**（容错解析后真实分母 **9,045 段 / 470 个段落名**，已排除 1 份非安全内容页 `122085`），它不是 Apple 安全响应页面的全集（页号 118479–149229 之间有缺号）。全部措辞层计数口径为**字段级**：只取每个段落的 Impact 段与 Description 段，不含 `Available for`、致谢行与条目备注。**本篇初稿使用的解析器只识别旧版 HTML 标记，静默漏掉了 10 个最新页面上的 1,430 个段落，因此本篇的核心族分母从 62 上修到 71、三组合计从 140 上修到 151，并推翻了初稿的一条"0 次"论点**（第 14.6 节逐条登记）。

| 数字 | 是什么 | 分母与口径 |
|---:|---|---|
| 2 | 手册层为这条通道设立的门数 | `sec70a4f377d`（配件认证授权）+ `sec5044aad1b`（数据连接激活） |
| 4 | MFi 授权证明的握手步数 | 同页：证书 → 验证 → 质询 → 签名应答 |
| 4 | 手册层列出的配件—设备连接类型 | `sec70a4f377d`："雷雳、闪电、蓝牙…或者对于特定设备，则通过 USB-C" |
| 5 | 手册层列出的需要激活的数据连接接口 | `sec5044aad1b`：Thunderbolt、USB、Lightning、Smart Connector、SDXC（后者限 macOS 13.3+） |
| **1 小时 / 30 天 / 3 天** | 数据连接的三个时间常数 | 同一页；三个数字各有独立的失效规则 |
| 1024 / 2048 | 认证 IC 的 RSA 密钥位长 / iCloud AirDrop 身份的 RSA 位长 | `sec70a4f377d`（MFi-SAP 签名）；`sec2261183f4`（对照项，见第 12.4 节） |
| **0** | 手册层 179 页中出现"USB 受限模式 / Restricted Mode"的页数 | 中文全量 179 页 0；英文取样 27 页亦为 0 |
| **1** | 257 份镜像中出现 "MFi" 的页数 | 只有 `122403`，且只作为 `Available for: MFi Program` |
| **0** | 257 份镜像中 `USB` / `Lightning` / `Thunderbolt` / `MFi` / `Accessory` 作为**组件段落名**出现的次数 | 段落名以 `<h3 class="gb-header…">` 切块枚举（取块正则必须容忍 class 后缀，见第 14.6 节）；本次容错重测后这五个名字**仍为 0** |
| **9,045 / 470** | 容错解析后的全镜像真实分母：段落总数 / 不同段落名数 | 258 个 HTML 文件排除 1 份非安全内容页 ⇒ 257 份；本篇初稿的 7,620 / 442 是漏采错值 |
| 5 | 全语料提及 "USB Restricted Mode" 的段落数 | `Accessibility` 4 + `Trusted Device` 1，共 5 页 |
| 93 / 65 | 全语料 "Apple is aware of a report" 命中的段落数 / 页数 | 与 #103、#104 的近场族 0 形成对照 |
| 62 → **71** | 本篇计入的配件族核心段落数 | Dock 19、IOHIDFamily 27、MobileAccessoryUpdater **14**、RepairKit 2、Activation Lock 1、Baseband **8**；初稿的 62（MobileAccessoryUpdater 10、Baseband 3）是标记代次漏采造成的错值（第 14.6 节） |
| 44 | 本篇另外逐条分析的相邻段落数 | CoreUtils 16、Power Services 8、DriverKit 8、MobileLockdown 4、Game Controllers 3、srd_tools 2、SDK 2、Trusted Device 1 |
| 34 → **36** | 只作段落名存在性计数、不纳入分析的段落数 | DiskArbitration **28**（21 页）、BiometricKit 8（8 页）；三组相加 71 + 44 + 36 = **151 段**（第 13.1 节）；初稿记作 34 与 140 |
| **8** | 核心族内 `improved input validation` 的段数 | **本篇最重要的一处反转**：初稿写 0，实测 8 段（`MobileAccessoryUpdater` 4 + `Baseband` 4），分布在漏采的 149034、149035、149041、149042 四页上（第 10.3、14.1 节） |
| **4** | `Connecting a malicious accessory` 的段数 | CVE-2026-86924，`MobileAccessoryUpdater` 名下，页 149034（iOS 27 与 iPadOS 27）、149035（macOS Golden Gate 27）、149041（iOS 26.7 与 iPadOS 26.7）、149042（macOS Tahoe 26.7）；**全语料唯一一条把"连接"这个动作本身写成 Impact 主语的配件条目**（第 10.5 节） |
| 9 / 8 | 全语料 `in radio range` 段数 / 页数 | `Bluetooth` 8 + `Baseband` 1；后者即 CVE-2026-86885，也在漏采页 149034 上（第 10.4 节） |
| **3 / 4** | `MobileLockdown` 段落中**纯致谢、无 CVE** 的段数 / 总段数 | 121250、121837、122404 为纯致谢；只有 122371 带 CVE |

第三行、第四行与第七行是本篇的两个支点：**手册层给了四个连接类型和五个接口名，却一个都不给"受限模式"这个名字**；措辞层反过来只用这个名字，却从不把它挂到任何接口段落名下。

## 0.2 四层文本与本篇的使用规则

| 层 | 具体文本 | 能支撑什么 | 不能支撑什么 |
|---|---|---|---|
| **T1 手册层** | 《Apple 平台安全保护》（zh 全量 179 页 / EN 取样 27 页，正文已剥目录） | 机制存在、算法与位长、时间常数、接口清单、降级路径、用户可控开关、组织可限制项 | 任何缺陷、任何可达性、任何"可被攻破"的判断 |
| **T2 措辞层** | 顾问页组件段落 + Impact + Description + 致谢 + 条目备注 + Available for | 某 CVE 被归到哪个**段落名**、Apple 怎么描述后果与修复类别、致谢对象、条目增改日期、受影响机型 | 缺陷类别、CVSS 分值、在野状态（除显式声明外）、实现层位置 |
| **T3 源码层** | XNU 与用户态开源部件 | 进程归属、调用关系、符号 | **本期不可用**：本地源码镜像为空目录，未取回任何符号（第 17 章 G1） |
| **T4 第三方层** | 研究机构、会议论文、CVE 分配方 | 研究结论、命名事件、评分 | Apple 是否认可——不可反推 |

四条硬规则：

1. **跨层引用必须显式标层。** 手册层说"这个过程完全由定制集成电路 (IC) 来处理"，这是一句**执行位置声明**，不是硬件型号声明。本篇每次引用它都不补任何型号、厂商或固件细节。
2. **T3 缺失即降级。** 凡"某进程属于内核 / 某框架在用户态"的句子，本篇一律写成"顾问页把它归入某个组件段落名之下"。本篇因此**不**断言 `CoreUtils`、`MobileAccessoryUpdater`、`srd_tools` 各自运行在哪个特权级。
3. **段落名不是实现层。** 本篇最重要的一条纪律：`Accessibility` 是措辞层的一个**段落标题**，把它当作"缺陷在辅助功能框架里"是不允许的推断（第 15.1 节）。
4. **T4 只在具名且已核实时使用。** 本篇出现的机构名（Citizen Lab、GrapheneOS、Oligo Security、Trend Micro Zero Day Initiative、iVerify、Kandji、Trail of Bits、Renault、Cisco Talos、Talence Security、KAIST SysSec Lab、Google Threat Analysis Group、SyNSec Lab at Penn State）**全部来自 T2 致谢字段本身**；**这些机构的研究结论一条也不写**（第 17 章 G4）。

## 0.3 怎么读这篇

- 想知道"插上一根线到底要过几道门"：第 2–6 章逐门，第 11 章给合图。
- 想知道"Apple 有没有说认证密钥存在哪"：第 2.3、4.3 节，以及第 17 章 G2。
- 想知道"哪些关于配件攻击的流行说法在官方文本里没有对应句式"：第 12 章双向沉默、第 14 章普查、第 16 章结构性事实、附表 E 对照表。
- 想知道"Apple 承认过哪一条配件方向的在野利用"：第 15 章三条 CVE 深读，附表 B 条目索引。
- 只想要可引用的条目表：附表 B（顾问页条目索引）与附表 C（手册层页清单，含 `sec<hash>` 与发布日期）。

# 1 两道门：手册层把"配件"拆成了两个独立问题

本篇全部分析建立在一条手册层结构事实上：**《Apple 平台安全保护》没有"USB 安全"这一页，它把配件相关的内容分放在至少两个标题不同、发布日期相同、但机制完全不同的页面上。**

## 1.1 门一：这个配件是不是被批准的

`sec70a4f377d`「为 Apple 设备和服务验证配件」（中文页发布日期 2024-05-07）回答的是**信任问题**：

> MFi 许可计划允许接受过审查的配件生产企业使用 iPod 配件协议 (iAP) 和必要的支持硬件组件。

> MFi 配件与 iPad、iPhone 或 Apple Watch 通信时，配件必须向 Apple 证明其已接受过审查。（配件与设备通过雷雳、闪电、蓝牙连接，或者对于特定设备，则通过 USB-C 连接。）

这条页给出的连接类型是四种：**雷雳、闪电、蓝牙、USB-C**。注意第 1.2 节的门给出的接口清单与这个并不相同。

## 1.2 门二：现在允不允许它建立数据连接

`sec5044aad1b`「安全激活数据连接」（中文页发布日期同样是 2024-05-07）回答的是**状态问题**：

> 在 iPad 和 iPhone 设备以及搭载 Apple 芯片的 Mac 便携式电脑上，如果近期没有建立过数据连接，则用户必须使用面容 ID、触控 ID 或密码激活通过雷雳、USB、闪电、智能接点或 SD 扩展容量 (SDXC) 卡（macOS 13.3 或更高版本中）接口建立的数据连接。

这条页给出的接口是五个：**雷雳、USB、闪电、智能接点、SDXC**。

## 1.3 两份清单的差集，以及为什么这不是矛盾

把 1.1 与 1.2 的原句并排：

| 接口/连接类型 | 门一（配件验证） | 门二（数据连接激活） |
|---|:---:|:---:|
| 雷雳 (Thunderbolt) | 有 | 有 |
| 闪电 (Lightning) | 有 | 有 |
| 蓝牙 | 有 | **无** |
| USB-C | 有（限"特定设备"） | **无**（页内写的是 USB） |
| USB | **无** | 有 |
| 智能接点 (Smart Connector) | **无** | 有 |
| SDXC | **无** | 有（macOS 13.3+） |

这张差集表是本篇第一个不能省略的细节，原因有三：

1. 两页描述的是**不同的判定**，不是同一判定的两种说法。门一问"你是否是被批准的配件"，蓝牙设备要答这个问题（门一原文把蓝牙列进连接类型），但蓝牙不建立"数据连接"意义上的这条门。门二问"这台设备现在是否允许任何外设读写数据"，SDXC 卡不是配件、不申请 MFi，但要过这道门。
2. **门一写"USB-C"、门二写"USB"**，两页用词不同。本篇不把它们当作同一处笔误，也不据此推断"USB-C 与 USB 在实现上是否同一路径"——那是 T3 的问题，而 T3 本期不可用（G1）。
3. 两页各自都不引用对方。手册层没有一句话把"配件认证"与"数据连接激活"连成一个流程。**这个连不上的事实本身就是一条安全信息**：它意味着一个通过了门一的配件，仍然要受门二的状态判定；而一个根本没申请门一的配件（例如某根只充电的线），也仍然可能撞上门二。第 4.7 节会把这个组合展开成四象限。

## 1.4 本篇据此确立的分析框架

第 2–6 章逐条拆解，全部按同一套四问：

| 问 | 为什么要问 |
|---|---|
| 这道门在哪一层判定（谁发起、谁应答）？ | 决定"配件侧能看到什么、设备侧必须在线吗" |
| 信任从哪里来（证书？许可计划？用户生物特征？）？ | 决定"仿冒需要拿到什么"——注意本篇不评价能不能拿到 |
| 密钥/私料存放在哪里、位长多少？ | 决定"一次泄露的影响半径"——这一问手册层最常沉默 |
| Apple 有没有给它时限或失效条件？ | 决定"这道门会不会过期"——门二给了三个数字，门一一个也没给 |

第四问是本篇与 #103、#104 的方法差别所在。近场通道的核心变量是身份哈希与配对关系；配件通道的核心变量是**时间与状态**——1 小时、30 天、3 天、锁定态、需要密码重新启用生物认证的那个状态。手册层在这一层写得比前两篇具体得多，而在"密钥放在哪"这一层写得比前两篇含糊得多。
# 2 门一的握手：证书、验证、质询、签名应答

本章全部取自 `sec70a4f377d` 单页，不加任何外部推断。

## 2.1 原句与拆解

手册层的机制句只有两句，本篇按分句标号：

> 作为授权证明，配件会向设备发送 Apple 提供的证书，设备之后会进行验证。然后，设备发送一个质询，配件必须使用已签名的证书来响应。

拆出四步：

| 步 | 谁做 | 交出什么 | 本篇能说的 | 本篇不能说的 |
|---|---|---|---|---|
| 1 | 配件 | Apple 提供的证书 | 证书的**签发方是 Apple**，且证书在配件一侧持有 | 证书内容、有效期、撤销机制（G2） |
| 2 | 设备 | 验证结果（未述） | 验证发生在**设备侧** | 验证是否联网、是否查吊销列表（G2） |
| 3 | 设备 | 一个质询 | 质询由设备发起，方向是设备 → 配件 | 质询长度、随机数来源（G2） |
| 4 | 配件 | 已签名的证书（应答） | 应答必须由配件侧的私料签出 | 私料在哪、能否被读出（G2） |

**这是一次挑战-应答，不是一次相互认证。** 手册层的句式里，设备只发送质询、不签名任何东西给配件验证——与 #104 第 9.2 节自动解锁那页明确写的"相互认证的站对站（STS）协议"形成鲜明对照。本篇把这个差别记为**措辞层的协议方向差异**，不外推为安全性差异。

## 2.2 执行位置：手册层最重要的一句

> 这个过程完全由定制集成电路 (IC) 来处理，而且对于配件本身是透明的。执行处理操作的定制集成电路由 Apple 提供给许可配件生产企业。

这句给出三件事，也**只**给出三件事：

1. **处理方是硬件**——"完全由定制集成电路 (IC) 来处理"。
2. **对配件的固件透明**——配件生产企业不接触握手实现。
3. **芯片由 Apple 提供给获许可厂商**——供应关系写在手册层里。

手册层**没有**说：芯片型号、厂商、是否含防篡改响应、密钥是否在制造时注入、是否可吊销、固件如何更新。本篇在第 17 章 G2 逐条登记这五项沉默，正文任何位置都不得补齐它们。

## 2.3 一句容易被误读的话

同一页还有一句常被引用为"MFi 保证安全"：

> 认证集成电路的设计旨在帮助确保只有经过批准的 MFi 配件才能获得对设备的完全访问权限。

这句的措辞层级需要如实标注：它写的是 **"设计旨在帮助确保"（is designed to help ensure）**，不是"确保"。而且它的宾语是"**完全访问权限**"（full access），不是"访问权限"。这与下一节的降级路径正好是一对：设计目标是把*完全*访问留给获批准配件，而**未批准的配件并未被写成"零访问"**。

## 2.4 降级路径：手册层明确写了"认证失败之后还能做什么"

> 经验证的 MFi 配件可以请求访问不同的传输方法和功能，例如，访问通过雷雳电缆传输的数字音频流，或者访问通过蓝牙提供的位置信息。

> 如果配件不支持认证，其访问仅限于模拟音频和一小部分串行 (UART) 音频播放控件。

这一对句子是本篇在 T1 层见到的**最完整的能力分级**——手册层罕见地同时写了通过者与未通过者各自能做什么：

| 类别 | 手册层授予的能力 | 原句依据 |
|---|---|---|
| 经验证的 MFi 配件 | 可**请求**访问不同传输方法与功能；举例两项：雷雳电缆的数字音频流、蓝牙提供的位置信息 | 上一句 |
| 不支持认证的配件 | 仅限**模拟音频** + **一小部分串行 (UART) 音频播放控件** | 下一句 |

三点必须标注：

1. 用词是"可以**请求**访问"（may request access），不是"可以访问"。授权判定仍在设备侧。
2. 降级一侧**不是零**——手册层给了一个具体的非零集合。这是"配件不认证会怎样"这个问题在官方文本里唯一的答案。
3. "一小部分"这个限定语没有清单。本篇不猜测是哪几个 UART 控件（G2）。

## 2.5 同一颗 IC 被复用到了另一个服务上

> "隔空播放"还使用认证集成电路来验证接收器已由 Apple 批准。

这句的价值在于**方向反转**：第 2.1 节的握手是"配件向设备证明"，而 AirPlay 这一句是"设备用同一颗 IC 去验证**接收器**"。同一个硬件信任根被用于两个不同方向的判定，而手册层用一句话把这两件事并列——它没说这两条判定共享协议、也没说它们不共享。本篇在第 17 章 G9 登记这一点。

# 3 MFi-SAP：手册层给出的一组具体密码学参数

配件通道的手册层文本里，唯一一段带算法名与位长的内容是安全关联协议。原句：

> "隔空播放"音频流和 CarPlay 车载视频流使用 MFi-SAP（安全关联协议），此协议使用 AES-128 在计数器 (CTR) 模式下对配件和设备之间的通信进行加密。作为端对端 (STS) 协议的一部分，临时密钥使用 ECDH 密钥交换 (Curve25519) 进行交换，并使用认证集成电路的 1024 位 RSA 密钥进行签名。

## 3.1 逐项拆表

| 项 | 手册层给出的值 | 本篇的使用方式 |
|---|---|---|
| 协议名 | MFi-SAP（安全关联协议） | 中文页两个名字各出现 1 次、同一页（第 12.1 节普查表） |
| 适用流 | AirPlay 音频流、CarPlay 车载视频流 | 未述 AirPlay 视频是否用同参数；本篇不合并 |
| 对称加密 | AES-128，CTR 模式 | 位长 128；模式 CTR |
| 密钥交换 | ECDH，曲线 Curve25519 | 临时密钥 |
| 签名密钥 | 认证 IC 的 **1024 位 RSA** 密钥 | 签名对象是临时密钥 |
| 协议归类 | "端对端 (STS) 协议的一部分" | STS 即 secure tunnel session；#104 第 9.2 节自动解锁页也用这个缩写 |

## 3.2 1024 位这件事必须怎么写

本篇全部四层语料里出现的非对称密钥位长只有三个：

| 位长 | 用途 | 出处 |
|---|---|---|
| **1024** | 认证 IC 的 RSA 密钥，用于签名 MFi-SAP 临时密钥 | `sec70a4f377d` |
| 2048 | 用户登录 iCloud 后存于设备的 RSA 身份（AirDrop 用它换证书） | `sec2261183f4` |
| 256 | 接力广告的对称 AES 密钥 | `secf78dbe639` |

**写法纪律**：本篇只陈述"手册层给出的位长是 1024"，并指出它是这三个数字里最短的非对称位长。本篇**不**写"因此可被分解""因此弱于 AirDrop"——那属于 T4 的评估结论，且 Apple 未在任何层认可过。第 17 章 G2 里"该密钥是否仍为 1024、是否随芯片代次变化"仍是缺口。

## 3.3 CTR 模式没有配完整性算法：这是一处沉默，不是一处判断

手册层那句只写了 "AES-128 在计数器 (CTR) 模式下…进行加密"，**没有**在同一句里写任何消息认证或 AEAD。这条观察只能作为**文本事实**记录（原文未提完整性），不能作为**协议事实**记录（不能写成"MFi-SAP 不提供完整性保护"）。第 12.3 节会给出对照：手册层在其它页（如接力）明确写了"加密**和**认证"两种动作，因此"没写"在文本层是可辨识的；但在协议层本篇不越界。

# 4 门二的三个时间常数：本篇最有引用价值的一页

`sec5044aad1b`「安全激活数据连接」是本报告在 T1 层引用最多的一页。以下按原句顺序拆。

## 4.1 触发条件

> 如果近期没有建立过数据连接，则用户必须使用面容 ID、触控 ID 或密码激活…建立的数据连接。

前置条件写得很窄也很具体：**近期没有建立过数据连接**。三种激活方式全部是**用户本机凭据**（面容 ID / 触控 ID / 密码），没有第四种。

## 4.2 手册层自己给出的理由，以及这句话为什么重要

> This hour period:
> - Helps ensure that frequent users of connections to a Mac or PC, to accessories, or wired to CarPlay won't need to enter their passcodes every time they attach their device
> - **Is necessary because the accessory ecosystem doesn't provide a cryptographically reliable way to identify accessories before establishing a data connection**

第二条中文页对应句：「十分必要，因为配件生态系统不提供在数据连接建立前识别配件的可靠加密方式」。

这一句是全篇 T1 层引用价值最高的句子，因为它同时给出三件事：

1. **Apple 自认门一不足以先行判定配件身份**——"在数据连接建立前"没有可靠密码学识别方式。
2. 因此门二的必要性被表述为**对门一缺位的补偿**，而不是独立功能。
3. 这是一句**能力自陈**而非缺陷声明：它没有说任何 CVE、任何攻击者、任何绕过。本篇在第 16 章把它列为"手册层少见的自认句式"，并明确它不能被读成"门一可被绕过"。

## 4.3 一小时

> 如果 iPad 或 iPhone 锁定或配件的数据连接终止超过一小时，则解锁设备前不允许建立任何新的数据连接。在这一个小时内，只允许来自之前设备处于解锁状态时已连接过的配件的数据连接。

一小时窗口的两条限定：

- 起算点有两个：**设备锁定时刻**，或**该配件的数据连接终止时刻**——取较近者。
- 窗口内**并非全开**：只放行"曾在设备解锁状态下连接过的配件"。

## 4.4 三十天与被连带失效的旧连接

> 这些配件会在上次连接后的 30 天内被记住。如果在此期间发生未知配件尝试打开数据连接的情况，则会取消激活通过这些连接建立的所有配件数据连接，直到设备被重新解锁。

两个独立事实：

| 事实 | 原句片段 | 为什么值得单列 |
|---|---|---|
| 记忆期 30 天 | "会在上次连接后的 30 天内被记住" | 这是整页唯一一个**配件身份**的时限；门一完全没给任何时限 |
| 连带失效 | "会取消激活…所有配件数据连接" | 未知配件的一次尝试，其后果是**该连接上全部**配件数据连接被取消激活，直到重新解锁。这是拒绝新连接之外的**主动回退** |

第二条是本篇愿意单独写一小节的原因：手册层在这里给的不是"访问被拒绝"，而是"已有授权被撤销"。这两者在运维上的含义完全不同（第 18.3 节有对应的检测项）。

## 4.5 三天与两个附加失效条件

> 此外，如果一个数据连接通过配件建立了三天以上，设备从锁定时刻起将不会再允许新的数据连接。这也增强了对不经常使用此类配件的用户的保护性。如果设备处于需要密码来重新启用生物认证的状态，这些数据连接也会变为不活跃。

三条：

1. **三天规则**服务于"不经常使用此类配件的用户"——手册层自己写了这句的适用人群。
2. **附加失效条件一**：设备处于"需要密码来重新启用生物认证"的状态时，这些数据连接也变为不活跃。这是一个**跨子系统的联动**：生物认证策略状态直接改变配件数据连接状态。手册层没写这个状态的完整触发清单（第 17 章 G11）。
3. 与 #100（本地访问前提）的接口：这一句是本报告能引到的、把**设备状态**当作访问控制输入的最明确的手册层文本之一。

## 4.6 两条手册层公开的旁路

> 用户可以选择在"设置"中重新启用始终打开数据连接（设置某些辅助设备时会自动启用）。

一句里两个事实，分量不等：

- **显式旁路**：用户可自行打开"始终数据连接"。手册层把它作为一个正常设置项陈述。
- **隐式旁路**：**设置某些辅助设备时会自动启用**。这是本篇在门二见到的唯一一处"用户未明确同意即改变防线状态"的描述，且手册层用括号一句话带过，未给设备清单。第 18.2 节把它转成一条可执行的核查项；第 17 章 G11 登记"哪些辅助设备会触发"这一沉默。

## 4.7 四象限：把两道门合起来看

这一节是本篇唯一的合成表。它的每一格都只由第 2 章与第 4 章已引过的原句支撑，不新增断言。

| | 门一：通过配件认证 | 门一：未通过 / 不支持认证 |
|---|---|---|
| **门二：数据连接已激活** | 手册层授予"可请求访问不同传输方法和功能"（第 2.4 节） | 手册层授予"仅限模拟音频 + 一小部分串行 (UART) 音频播放控件"（第 2.4 节） |
| **门二：未激活（超 1 小时 / 超 3 天 / 需密码态）** | 手册层未描述此格——两页之间没有任何交叉引用（G3） | 手册层未描述此格 |

右下两格空着，是**文本事实**：手册层没有一句话把认证状态与数据连接激活状态组合起来描述。本篇把这种空缺直接登记为缺口，而不是补一句"因此未激活时认证无意义"。

# 5 直接内存访问：手册层写"内核"的那一页

在 T1 层，"内核"一词命中 42 页；本篇只取其中与外设直接相关的一页 `seca4960c2b5`「Mac 电脑的直接内存访问保护」（中文页发布日期 2022-01-05）。

## 5.1 为什么这篇报告要写 DMA

第 14.2 节的普查会给出一条对照：措辞层里 `kernel` 一词在配件族段落命中 7 段，在近场族（#104）里 `kernel` 只在 Wi-Fi 族出现、蓝牙/AirDrop/Handoff 族为 0。也就是说，**"内核内存"这个后果在 Apple 的措辞层里同时属于 Wi-Fi 与配件两条线**，而本篇需要给配件这条线找到手册层侧的机制对应物。DMA 保护是唯一的候选，且它有一处关键限制（第 5.5 节）。

## 5.2 原句与拆表

> 为了在诸如 PCIe、FireWire、雷雳和 USB 等高速接口上实现高吞吐量，电脑必须支持外围设备的直接内存访问 (DMA)。也就是说，外围设备必须能够在没有 CPU 持续参与的情况下读取和写入 RAM。

> Apple 片上系统针对系统中的每个 DMA 代理（包括 PCIe 和雷雳端口）包含一个输入/输出内存管理单元 (IOMMU)。…通过 PCIe 或雷雳连接的外围设备只能访问明确映射给其使用的内存。外围设备无法访问属于系统其他部分（如内核或固件）的内存或者分配给其他外围设备的内存。**如果 IOMMU 检测到外围设备试图访问未映射给该外围设备使用的内存，会引发内核崩溃。**

> 基于 Intel 且支持 Intel 定向 I/O 虚拟化技术 (VT-d) 的 Mac 电脑在启动过程的非常早期阶段会初始化 IOMMU（启用 DMA 重映射）然后中断重映射…**Apple IOMMU 硬件开始以默认拒绝策略运行，因此系统接通电源时，IOMMU 会立即自动开始阻止来自外围设备的 DMA 请求。**

> 自 macOS 11 起，如果辅助 DMA 的 UEFI 驱动器与外部设备配对，所有搭载 Apple T2 安全芯片的 Mac 电脑都会在受限的 Ring 3 环境中运行此类固件。此属性有助于减少恶意设备在启动时以非预期方式与 UEFI 驱动器交互可能导致的安全性漏洞。它尤其减少了驱动器处理 DMA 缓冲时导致的漏洞的影响。

| 机制项 | 手册层给出的值 | 可直引的判断 |
|---|---|---|
| IOMMU 粒度 | **每个 DMA 代理一个**（含 PCIe 与雷雳端口） | 每单元有独立地址转换表 |
| 可访问范围 | 仅显式映射给该外设的内存 | 明确排除内核、固件、其它外设的内存 |
| 违规响应 | **引发内核崩溃** | 措辞是"崩溃"，不是"记录并放行" |
| Intel 侧初始化时机 | 启动过程非常早期；默认拒绝；**通电即开始阻断** | 早于软件初始化即生效 |
| T2 的 UEFI 驱动运行环境 | macOS 11 起，与外建设备配对时在**受限 Ring 3** 运行 | 手册层自己说明这是为降低"驱动处理 DMA 缓冲"类漏洞的影响 |

## 5.3 "引发内核崩溃"作为设计选择

这一句值得单独停一下，因为它是本篇见到的**唯一一处手册层把可观测的严重故障写成防护手段**的地方。它的运维含义在第 18.4 节展开：IOMMU 触发的内核崩溃是**这道门在工作**的证据，而不是这台设备被攻破的证据；两者需要分开处理，而分开处理需要的正是手册层这句原话。

## 5.4 "默认拒绝 + 通电即生效"是时序声明

Intel 侧那句给的是一个**顺序**：硬件策略先于软件初始化。本篇把它记为时序事实，不记为安全保证——它没有说覆盖哪些接口、也没有说 macOS 之后是否改变（G1，T3 缺失）。

## 5.5 本页的适用边界：它是 Mac 页

这一条限制本篇整段第 5 章。`seca4960c2b5` 的标题是「**Mac 电脑**的直接内存访问保护」，正文通篇主语是 Mac 电脑、Apple 芯片的 Mac、基于 Intel 的 Mac、搭载 T2 的 Mac，**没有任何一句写到 iPhone 或 iPad**。

因此：

- 本篇**不**把 IOMMU 句写到 iOS 设备上。
- 第 14.4 节会给出一个由此产生的具体缺口：措辞层把 `USB Restricted Mode` 相关条目同时挂在 iOS 遗留分支（`Accessibility` 4 页）与 macOS（`Trusted Device` 1 页）上，而 T1 层只有 Mac 侧的 DMA 机制文本——**iOS 侧这道门在手册层没有机制页，只有一个行为描述页（`sec5044aad1b`）**。这是 G3 的具体内容。

# 6 部件级绑定与绕过代码：激活锁页里的两句

`sec0f8dfd030`「激活锁」（中文页发布日期 2026-01-06）在本篇只承担两件事，都与配件相关而非与账户找回相关。

## 6.1 部件、维修与"校准受限"

> Apple 正将 iPhone 的激活锁扩展至覆盖其各个部件，这有助于阻止被盗部件流入市场。维修期间，如果 iPhone 检测到受支持的部件来自另一台已打开激活锁或丢失模式的 iPhone，则针对该部件的**校准会受到限制**。

拆出四个限定：

| 项 | 手册层给出的值 | 未给出的值（G10） |
|---|---|---|
| 覆盖对象 | iPhone 的各个部件 | 部件清单（"受支持的部件"未列举） |
| 触发时机 | **维修期间** | 是否也在日常启动时判定 |
| 触发条件 | 该部件来自另一台已开激活锁或丢失模式的 iPhone | 判定所依据的标识与匹配方式 |
| 后果 | **校准会受到限制**（calibration is restricted） | 受限的具体功能范围 |

后果的措辞值得精确引用：手册层写的是"**校准**会受到限制"，不是"部件无法使用"或"设备被锁定"。第 12.1 节的普查显示"校准"一词在中文 179 页只命中 3 页、"维修"只命中 1 页、"部件"只命中 1 页——**这套部件级绑定在手册层里由一页承载**，它是配件信任链在"设备内部"的延伸，但文本厚度远低于第 2 章与第 4 章。

## 6.2 绕过代码：三个数字与一个托管关系

> 在受监督的设备上，激活锁则默认不允许使用，但设备管理服务可允许用户将其启用。这使该服务可**托管**设备上的绕过代码…
> 另外，对于受管理且受监督的设备，设备管理服务还可直接联系 Apple 服务器启用激活锁。此操作完全在服务器端完成，且不依赖于任何用户操作或设备状态。设备管理服务需要创建一个 **31 字节的绕过代码**，然后在要启用设备激活锁时将其发送至 Apple 服务器。该服务为每台设备创建的绕过代码应为**随机且唯一**。

| 项 | 值 | 出处句 |
|---|---|---|
| 绕过代码位长 | **31 字节** | 服务端启用路径 |
| 生成方 | 设备管理服务（不是 Apple、不是设备） | 同上 |
| 熵要求 | 随机、每台唯一 | 同上 |
| 托管关系 | 服务**escrow**（托管）设备上的绕过代码 | 受监督设备路径 |
| 默认状态 | 受监督设备默认**不允许**使用激活锁 | 同上 |
| 依赖 | 服务端启用路径"完全在服务器端完成，不依赖用户操作或设备状态" | 同上 |

本篇把这一小节放在配件报告里的理由是明确的：**它是 Apple 公开文本中唯一一处把"物理设备的所有权判定"交给一个可托管凭据的机制**。第 9.1 节会看到措辞层确实有一个 `Activation Lock` 段落名，且那一段只有致谢、没有 CVE——这两件事合起来构成第 16 章的一条结构性事实。

## 6.3 手册层 MFi 的第三个现身处：硬件断麦

`secbbd20b00b`（中文页发布日期 2025-07-14）是 179 页里三处含"MFi"之一，也是唯一一处 MFi 出现在**物理断联**要求上的：

- 该页写明硬件断开即使拥有 root 或内核权限、即使 T2 芯片或其它固件中的软件也无法重新路由；
- **2020 年或以后推出的 iPad 需要一款符合 MFi 规格的保护壳**才能实现硬件断开；
- 摄像头不做硬件断联。

这一处给本篇一个与前两篇都不同的观察：**MFi 在这里不是配件的申请对象，而是"要获得物理断联，你必须先买一个通过 MFi 的壳"**。认证计划同时是能力门槛与配件市场门槛，手册层在同一页把这两件事并列写出。第 12.2 节给术语对照。
# 7 门二在措辞层的样子：`USB Restricted Mode` 只有五个段落

第 5.5 节留下一个问题：iOS 侧这道门在手册层只有行为描述、没有机制页。本篇现在从措辞层这一侧回看同一道门，得到的第一个结果是**它出现的次数极少、位置极散**。

## 7.1 全语料检索结果

在本期 257 份镜像里，短语 `restricted mode` 命中 **5 个段落 / 5 页**，无一遗漏：

| 页号 | 产品与版本 | 段落名 | Impact（原句） |
|---|---|---|---|
| 122173 | iPadOS 17.7.5 | **Accessibility** | A physical attack may disable USB Restricted Mode on a locked device. Apple is aware of a report that this issue may have been exploited in an extremely sophisticated attack against specific targeted individuals. |
| 122174 | iOS 18.3.1 and iPadOS 18.3.1 | **Accessibility** | 同上（逐字相同） |
| 122345 | iOS 15.8.4 and iPadOS 15.8.4 | **Accessibility** | 同上 |
| 122346 | iOS 16.7.11 and iPadOS 16.7.11 | **Accessibility** | 同上 |
| 125110 | macOS Tahoe 26 | **Trusted Device** | USB Restricted Mode may not be applied to accessories connected during boot |

三条立即可用的结论：

1. **这五个段落分属两个段落名**，且这两个名字都不是 `USB`、不是 `MobileLockdown`、不是 `Dock`。
2. **`physical attack` 这个措辞在全语料只出现 4 次，恰好就是上面这 4 段**（逐格分解见第 14.3 节）——它是本批次里边界最干净的一个措辞。
3. **两条陈述的是不同的失效模式**：iOS 那 4 段说"这道门被一次物理攻击**禁用**了"；macOS 那段说"这道门**没有施加到**启动时连接的配件上"。前者是被关闭，后者是覆盖范围有洞。措辞层把这两件事放在了两个不同的段落名下。

## 7.2 `Trusted Device` 段落：全语料只有一段，且致谢里出现了跨项目名字

125110 上这整段的完整字段：

- **Available for**：Mac Studio (2022 and later), iMac (2020 and later), Mac Pro (2019 and later), Mac mini (2020 and later), MacBook Air with Apple silicon (2020 and later), MacBook Pro (16-inch, 2019), MacBook Pro (13-inch, 2020, Four Thunderbolt 3 ports), and MacBook Pro with Apple silicon (2020 and later)
- **Impact**：USB Restricted Mode may not be applied to accessories connected during boot
- **Description**：A permissions issue was addressed with additional restrictions.
- **CVE-2025-43262**：Pyrophoria, an anonymous researcher of GrapheneOS, James J Kalafus, Michel Migdal, ken super
- **条目备注**：Entry updated November 3, 2025

四个观察：

1. **段落名 `Trusted Device`（受信任设备）**——这个名字本身就暗示了一条"哪些外设被信任"的名单机制，而手册层第 4 章写的是"过去 30 天连接过的配件会被记住"。**这是本篇见到 T1 与 T2 最接近的一次语义对齐**（第 12.4 节把这条列为唯一可作弱对应的对照，并声明它仍不是实现层证据）。
2. **修复类别写的是 permissions issue + additional restrictions**，不是内存安全类别。这与同一机型清单里 `IOHIDFamily`/CVE-2025-43302 的"out-of-bounds write"形成同页对照（第 10.2 节）。
3. **Available for 按机型划分**，不是按 macOS 版本划分。这一页同时列出三款 2019/2020 的 Intel 机型与 Apple silicon 机型，所以"启动时配件连接"这一问题被 Apple 划给了特定硬件世代。
4. **致谢里出现 `an anonymous researcher of GrapheneOS`**——一个第三方移动操作系统项目的成员。本篇只把这个名字作为 T2 致谢字段的事实引用，**不引 GrapheneOS 的任何结论**（第 0.2 节规则 4）。

## 7.3 手册层为什么不叫这个名字

第 12.1 节会给出普查：中文 179 页里"受限模式"0 次、"Restricted Mode"在英文取样 27 页里 0 次。本篇对这一沉默的处理方式是：

- **不**写成"Apple 把手册里的机制改了名"——没有文本支持任何改名叙事；
- **不**写成"USB Restricted Mode 是一个非官方词"——它出现在 Impact 原句里，就是官方措辞；
- 写成一条**术语分布事实**：同一道防线在 T1 层的标题是「安全激活数据连接」（`sec5044aad1b`，2024-05-07），在 T2 层的措辞是 "USB Restricted Mode"（最早见于本篇镜像的 122345，iOS 15.8.4）。二者之间**没有任何一层引用另一层**。

这条事实的实际用处在第 18.1 节：做资产核查或写合规问答时，"这台设备上有没有开 USB 受限模式"这个问题**无法在手册层找到答案页**，必须先在两层之间手工建立映射。

# 8 `MobileLockdown`：全语料唯一把 USB-C 写进 Impact 主语的段落

第 7.1 节那五段的前置条件是"设备已锁定"。本篇现在给出条件正好相反的一条，它也是全语料唯一一处把接口类型写进 Impact 主语位置的条目。

## 8.1 条目全文

**页 122371（iOS 18.4 and iPadOS 18.4），段落名 `MobileLockdown`：**

- **Available for**：iPad Pro 13-inch, iPad Pro 12.9-inch 3rd generation and later, iPad Pro 11-inch 1st generation and later, iPad Air 4th generation and later, iPad 10th generation and later, and iPad mini 6th generation and later
- **Impact**：An attacker with a USB-C connection to an **unlocked** device may be able to programmatically access photos
- **Description**：This issue was addressed with improved authentication.
- **CVE-2025-24193**：Florian Draschbacher

## 8.2 三个"唯一"

| 唯一性 | 内容 | 对照项 |
|---|---|---|
| 全语料唯一一次 `USB-C` 出现在 Impact 里 | USB-C 在整个 257 份镜像的段落体里总共只出现 **1 次**，就是这一段 | 手册层 `sec70a4f377d` 把 USB-C 写成四种连接类型之一（限"特定设备"） |
| 唯一一次 USB 前置条件写作 **unlocked** | 第 7.1 节五段全部写 `locked device` | 二者覆盖的是门二两侧的不同状态：锁定态走"数据连接激活"判定，解锁态不受该判定约束 |
| 唯一一条**机型清单只列 iPad、没有 iPhone** 的配件方向条目 | Available for 六项全是 iPad 型号 | 同页 122371 其它段落（如 Power Services）的清单以 "iPhone XS and later" 开头 |

第三条本篇只能登记为缺口（G6）：无法判定这是机型范围限制，还是该条在 iPhone 上不适用，还是清单编排差异。正文只按页陈述。

## 8.3 "improved authentication" 是本篇唯一的认证类修复句式

配件方向全部条目里，Description 归到认证类别的只有这一条。其它同类条目的修复类别分布见第 10.3 节汇总表。本篇把这一点写成措辞层事实：**Apple 在这一条上说的是"改进了认证"，而第 2 章的门一整套认证机制（证书/质询/签名应答）在手册层由 IC 承担**——但本篇**不**据此断言该缺陷发生在认证 IC 上（T3 缺失，G1）。

# 9 相邻段落族：Apple 把配件相关缺陷登记在哪些名字下

本章枚举本篇另外逐条计入的 44 个段落（`DiskArbitration` 28、`BiometricKit` 8 只作第 13.1 节的段落名计数，不在本章）。之所以要枚举，是因为第 13 章的负结果（`USB` / `Lightning` / `MFi` 段落名不存在）必须靠这些**实际存在的名字**来填充——否则"不存在"就只是一句抱怨。**这一族的 44 个段落在容错重测后没有变化**：它们全部登记在旧版标记的页面上；发生变化的是第 10 章的核心族与第 13.1 节的存在性计数。

## 9.1 `Activation Lock`：1 段，纯致谢

页 124147 上只有一句：`salemdomain for their assistance.`，**没有 CVE、没有 Impact、没有 Description**。

对照第 6 章：手册层为激活锁写了两套完整机制（部件校准 + 31 字节绕过代码 + 托管关系），措辞层给它的只有 1 个段落、0 条 CVE、1 处致谢。**这是本篇"文本厚度差"最大的一个主题对**（第 16 章事实 4）。

## 9.2 `DriverKit`：8 段 4 页，与手册层同名成对

| CVE | Impact（原句） | Description | 页号 |
|---|---|---|---|
| CVE-2026-43753 | An attacker with **physical access to a locked device** may be able to view sensitive user information | An out-of-bounds read was addressed with improved bounds checking. | 128066（iOS 26.6）、128067（macOS Tahoe 26.6）、128071（macOS Sequoia 15.7.8）、128072（macOS Sonoma 14.8.8） |
| CVE-2026-43793 | An app may be able to cause unexpected system termination | An issue existed in the handling of environment variables. This issue was addressed with improved validation. | 128067、128071、128072 |

另有 128067 一处 `DriverKit` 纯致谢（Niels Hofmans，与 43753 同人）。⇒ 段落名实测分布为 128066 一处、128067 三处、128071 两处、128072 两处，合计 **8 段 4 页**（两个 CVE 的 4 + 3 段加一处纯致谢）。本篇第一次统计时把页数当成了段数，此处的 8 是重测结果。

本篇要用的结构事实只有一条：**这是全篇唯一一处"手册层机制页标题 = 措辞层段落名"的正面配对**。手册层 `seca48c92d43` 讲 DriverKit 把第三方扩展驱动放到用户态、由守护进程管理、需 entitlement；措辞层 `DriverKit` 段落下的两条后果分别是"锁定设备物理访问 ⇒ 看到敏感信息"与"意外系统终止"。两者对得上名字，但**本篇不用手册层去解释措辞层的成因**（配对 ≠ 因果）。

第二可用的事实：CVE-2026-43793 的 Description 句式 "An issue existed in the handling of environment variables" 在配件族里与 `MobileAccessoryUpdater`/CVE-2024-54536 的 "improved validation of environment variables"（NVRAM 变量）是同族句式，但两个段落名不同、后果不同（一个是系统终止，一个是可编辑 NVRAM 变量）。第 14.5 节给这个对照。

## 9.3 `Game Controllers`：3 段，手册层完全没有这个词

| 页号 | 产品与版本 | Impact（原句） |
|---|---|---|
| 121564 | macOS Sequoia 15.1 | An attacker with physical access can input Game Controller events to apps running on a locked device |
| 121568 | macOS Ventura 13.7.1 | 同上 |
| 121570 | macOS Sonoma 14.7.1 | 同上 |

CVE-2024-44265（Ronny Stiftel）｜Description：The issue was addressed by **restricting options offered on a locked device.**

三件事：

1. 手册层中文 179 页里"游戏控制器"**0 次 / 0 页**——一个在措辞层承载 3 段后果的输入通道，在手册层连名字都没有（第 12.1 节普查表最后一行）。
2. 修复句式 "restricting options offered on a locked device" 是本篇见到的**最接近第 4 章门二精神的一句 T2 措辞**：它改变的是"锁定态下还提供哪些选项"，不是任何内存或权限结构。
3. 三段全部是 macOS，无 iOS/tvOS/watchOS 分支——这与第 9.2 节 43753 同时出现在 iOS 与 macOS 上的覆盖方式不同。

## 9.4 `Power Services`：8 段一个 CVE，且它打破了前两篇的一条负结果

CVE-2025-24173｜Mickey Jin (@patch1t)｜Impact「An app may be able to **break out of its sandbox**」｜Description「This issue was addressed with additional entitlement checks.」｜页 122371（iOS 18.4）、122372（iPadOS 17.7.6）、122373（macOS Sequoia 15.4）、122374（macOS Sonoma 14.7.5）、122375（macOS Ventura 13.7.5）、122376（watchOS 11.4）、122377（tvOS 18.4）、122378（visionOS 2.4）。

本篇在第 14.1 节必须做一处自我修正：#103 与 #104 报告都写过 `sandbox` 一词在相应段落族里 0 次。`Power Services` 段落名下的这 8 段说明：**"物理/配件方向没有 sandbox 措辞"这个说法只有按段落名分别统计才成立**；把它合并成"接口相关族都没有 sandbox"是错的。本篇把这条列入第 16 章事实 6，作为方法论教训而不是新发现。

## 9.5 `CoreUtils`：16 段，以及一处主语从 attacker 变成 user 的措辞变异

| CVE | Impact（原句） | Description | 页数 | 致谢 |
|---|---|---|---|---|
| CVE-2025-31203 | An attacker on the local network may be able to cause a denial-of-service | An integer overflow was addressed with improved input validation. | 7（122371–122374、122376–122378） | Uri Katz (Oligo Security)；Entry added April 28, 2025 |
| CVE-2026-28886 | **A user in a privileged network position** may be able to cause a denial-of-service | A null pointer dereference was addressed with improved input validation. | 8（126792–126799） | Etienne Charron (Renault) and Victoria Martini (Renault) |

另有 124149 一处 `CoreUtils` 纯致谢：Csaba Fitzl (@theevilbit) of Kandji。

**CVE-2026-28886 的措辞值得单独分析**。全语料两种"特权网络位置"句式：

| 句式 | 段落数 | 页数 | 该词族下的前三大段落 |
|---|---|---|---|
| an attacker in a privileged network position | **63** | 48 | Kernel 16、802.1X 8、Wi-Fi 7 |
| **a user** in a privileged network position | **10** | 10 | **CoreUtils 8**、Passwords 1、lldb 1 |

三条纪律：

1. `user` 句式在全语料只有 10 段，其中 8 段在 `CoreUtils`。它不是排版差异，是一个**可见的用词变异**。
2. 本篇**不**解释 Apple 为什么在这里写 user。可能的相关事实是这两位致谢对象来自一家汽车厂商（Renault），而 `CoreUtils` 段落同时出现在 iOS/iPadOS/macOS/tvOS/watchOS/visionOS 六条产品线上——但把这两件事连成因果是没有文本支持的推断（G7）。
3. 值得注意的是 `CoreUtils` 两条都是**拒绝服务**，且两条的 Description 都是输入校验类（整数溢出 / 空指针解引用）。**本篇初稿在这里写过一句"输入校验不属于核心族的修复类别，字段级重测里 `improved input validation` 在核心族为 0 次"——这句已经被容错重测推翻**：核心族内该句式实测 **8 段**（`MobileAccessoryUpdater` 4 段 = CVE-2026-86924，`Baseband` 4 段 = CVE-2026-86879、86885、86963×2），全语料为 **638 段 / 149 页**。0 的成因不是口径，而是解析器漏掉了登记这些条目的 149034、149035、149041、149042 四页（第 14.6 节）。⇒ 本篇把这一处改写为一条更硬的纪律：**任何"本族内某句式 0 次"的负结果，都要先确认解析器认全了所有页面的标记，再断言它属于相邻段落。** 核心族内次数最多的修复类别仍是 `improved bounds checking`（17 段，第 10.3 节）。

## 9.6 `srd_tools`：2 段全纯致谢，与手册层的安全研究设备页配对

页 122371、122373，内容均为：`We would like to acknowledge Joshua van Rijswijk, Micheal ogaga, hitarth shah for their assistance.`

手册层 `seca7ff718d2`（Apple Security Research Device，中文页发布日期 2022-09-06）给出的是硬件熔丝与启动约束：

> 安全性研究设备只能在充电期间启动。充电可使用闪电线缆或兼容 Qi 的充电器。如果设备启动期间未在充电，会进入恢复模式。

> 它启动一个特殊变体的 iBoot 和一个在生产设备上无法启动的 Boot Kernel Collection。

本篇把这一对放在一起的理由是它是**第二处弱配对**（第一处是第 9.2 节的 DriverKit），且它给出的接口事实很具体：**一台为安全研究特制的 iPhone，其启动依赖闪电线缆或 Qi 充电**——这是全篇唯一一处 Lightning 同时出现在 T1 与 T2 主题上的情形（而 T2 的 "Lightning" 计数仍是 0，见第 13.2 节）。

## 9.7 `SDK`：2 段，MFi 在措辞层唯一的现身方式

页 `122403`，页标题不是 iOS/macOS 版本号，而是三个 SDK/插件版本号：

> About the security content of **AirPlay audio SDK 2.7.1, AirPlay video SDK 3.6.0.126, and CarPlay Communication Plug-in R18.1** Updates

| CVE | Impact（原句） | Description | 致谢 |
|---|---|---|---|
| CVE-2025-24132 | An attacker on the local network may cause an unexpected app termination | The issue was addressed with improved memory handling. | Oligo Security Research Team |
| CVE-2025-30422 | 同上 | A buffer overflow was addressed with improved input validation. | Uri Katz (Oligo Security) |

两段 **Available for 均为 `MFi Program`**。

四条：

1. **MFi 在 257 份镜像里只出现在这一页**，且只作为 Available for 的取值。它**从未**作为组件段落名出现（第 13.1 节）。
2. 这两条**没有任何在野声明**——第 15.4 节明确这一点，因为"CarPlay 插件出过被利用的漏洞"是网上流传但没有 Apple 文本支持的说法。
3. 这一页证明措辞层存在**以 SDK/插件版本号命名的安全内容页**，它们不落在任何 iOS/macOS 版本序列里；所以"这个 CVE 在哪个 iOS 版本修了"这类问题在这两行上没有答案（G8）。
4. 与第 3 章对接：手册层 MFi-SAP 覆盖的正是"AirPlay 音频流和 CarPlay 车载视频流"——同一对服务，T1 给密码学参数、T2 给两条内存类后果。这是本篇第二个"T1/T2 主题重合但层间不互引"的实例（第一个是第 7.1 节的门二）。

## 9.8 相邻段落合计

| 段落名 | 段数 | 有 CVE 的段数 | 纯致谢段数 | 修复类别（原句片段） |
|---|---|---|---|---|
| MobileLockdown | 4 | 1 | 3 | improved authentication |
| CoreUtils | 16 | 15 | 1 | integer overflow / null pointer dereference |
| Power Services | 8 | 8 | 0 | additional entitlement checks |
| DriverKit | 8 | 7 | 1 | out-of-bounds read / handling of environment variables |
| Game Controllers | 3 | 3 | 0 | restricting options offered on a locked device |
| Trusted Device | 1 | 1 | 0 | permissions issue + additional restrictions |
| srd_tools | 2 | 0 | 2 | — |
| SDK (MFi Program) | 2 | 2 | 0 | memory handling / buffer overflow |
| **合计** | **44** | **37** | **7** | — |

`Activation Lock` 的那 1 段纯致谢**不在本表内**——它已经计入第 10.1 节的核心 71 段。本篇第一次列这张表时把它同时算进了两侧，并把 DriverKit 的页数当成段数；上表是去重与重测后的结果：4 + 16 + 8 + 8 + 3 + 1 + 2 + 2 = **44**，其中 37 段带 CVE、7 段纯致谢，无剩余。

第 16 章事实 2 直接引用这张表的最后两列：**44 个段落里有 7 段是纯致谢**，其中 `MobileLockdown` 一个段落就占 3 段、`srd_tools` 全部 2 段都是。

# 10 配件族核心 71 段逐条

本章给出第 0.1 节分母 71 的构成与关键条目。完整可检索表由本篇的取数脚本生成，附表 B 给索引。

## 10.1 构成

按段落名：Dock 19、IOHIDFamily 27、MobileAccessoryUpdater **14**、RepairKit 2、Activation Lock 1、Baseband **8** = **71 段**，分布在 257 份镜像的 **57 页**上（页号并集实测）。其中 MobileAccessoryUpdater 与 Baseband 两个名字在容错重测后分别从 10 涨到 14、从 3 涨到 8（第 14.6 节）。

按 CVE 反向分解同一个 71（本族内**每个段落只承载一个 CVE**，这一点与 #106 的字体族不同——那边有一段落两个编号；所以本族段数 = CVE 段数 + 纯致谢段数，无重叠）：

| CVE | 段数 | 段落名 | CVE | 段数 | 段落名 |
|---|---:|---|---|---:|---|
| CVE-2025-43302 | 8 | IOHIDFamily | CVE-2025-24095 | 2 | RepairKit |
| CVE-2026-28992 | 8 | IOHIDFamily | CVE-2025-30455 | 2 | Dock |
| CVE-2026-43807 | 8 | MobileAccessoryUpdater | CVE-2025-43198 | 2 | Dock |
| CVE-2026-28943 | 7 | IOHIDFamily | CVE-2026-86963 | 2 | Baseband |
| CVE-2024-27799 | 4 | IOHIDFamily | **无 CVE（纯致谢）** | **3** | 三个段落名 |
| **CVE-2026-86924** | **4** | MobileAccessoryUpdater | CVE-2024-54536 | 1 | MobileAccessoryUpdater |
| CVE-2024-44177 | 3 | Dock | CVE-2024-54554 | 1 | Dock |
| CVE-2024-54547 | 3 | Dock | CVE-2025-31214 | 1 | Baseband |
| CVE-2025-31187 | 3 | Dock | CVE-2026-28874 | 1 | Baseband |
| CVE-2025-43420 | 3 | Dock | CVE-2026-28875 | 1 | Baseband |
| CVE-2024-23244 | 2 | Dock | **CVE-2026-86879** | **1** | Baseband |
| | | | **CVE-2026-86885** | **1** | Baseband |

**22 个 CVE（68 段）+ 3 段纯致谢 = 71 段**（8+8+8+7+4+4+3+3+3+3+2+2+2+2+2 = 68，加 3 = 71 ✓）。三段纯致谢分别是：

1. `Activation Lock` 的 124147（salemdomain）；
2. `MobileAccessoryUpdater` 的 122373（Claudio Bozzato 与 Francesco Benvenuto of Cisco Talos，与 CVE-2024-54536 同一批致谢对象、另一条产品线上的重复致谢）；
3. **`Baseband` 的 149034**（Kai Tu、Tianchang Yang、Xiaotian Zhou、Ali Ranjbar、Abdullah Al Ishtiaq、Tianwei Wu、Yilu Dong、Syed Rafiul Hussain —— SyNSec Lab at Penn State）。

**第 3 段是容错重测后才出现的**，它同时带来一个方法论后果：本族"纯致谢段"的数量从 2 涨到 3，而且新增的这段落在**本族条目最密的那一页**——149034（iOS 27 与 iPadOS 27）一页在本族里就有 **5 个段落**：`Baseband` 三条带 CVE 的条目（86885、86879、86963）+ `Baseband` 一条纯致谢 + `MobileAccessoryUpdater` 一条（86924）。它是"同一页、同一段落名下既有带 CVE 的条目又有纯致谢行"的实例，不构成第二条缺陷（第 10.6 节）。

## 10.2 关键条目

| CVE | 段落名 | Impact（原句，节选） | Description（原句） | 页号 | 条目备注 |
|---|---|---|---|---|---|
| CVE-2024-27799 | IOHIDFamily | An unprivileged app may be able to log keystrokes in other apps including those using **secure input mode** | This issue was addressed with additional entitlement checks. | 120898（iOS 8/8+/X、iPad 5th、iPad Pro 9.7″/12.9″ 1st）、120899（macOS Monterey）、120900（Ventura）、120903（Sonoma） | Entry added June 10, 2024 |
| CVE-2024-23244 | Dock | An app from a standard user account may be able to **escalate privilege after admin user login** | A logic issue was addressed with improved restrictions. | 120884（Monterey）、120895（Sonoma） | Csaba Fitzl (@theevilbit) of OffSec |
| CVE-2024-54536 | MobileAccessoryUpdater | An app may be able to **edit NVRAM variables** | The issue was addressed with improved validation of environment variables. | 121839（macOS Sequoia） | Entry added January 27, 2025；Claudio Bozzato and Francesco Benvenuto of Cisco Talos |
| CVE-2025-24095 | **RepairKit** | An app may be able to **bypass Privacy preferences** | This issue was addressed with additional entitlement checks. | 122371（iOS 18.4）、122378（visionOS 2.4） | Mickey Jin (@patch1t) |
| CVE-2025-30455 | Dock | A malicious app may be able to access private information | The issue was addressed with improved checks. | 122373（Sequoia）、122374（Sonoma） | Mickey Jin (@patch1t), and an anonymous researcher |
| CVE-2025-31187 | Dock | An app may be able to **modify protected parts of the file system** | This issue was addressed by removing the vulnerable code. | 122373、122374、122375（Ventura） | Rodolphe BRUNETTI (@eisw0lf) of Lupus Nova |
| CVE-2025-43302 | IOHIDFamily | An app may be able to cause unexpected system termination | An **out-of-bounds write** issue was addressed with improved bounds checking. | 125108–125112、125114–125116 共 **8 页** | Keisuke Hosoda；覆盖 iPhone/iPad/Mac/Apple TV/Vision Pro/Watch |
| CVE-2025-31214 | Baseband | An attacker in a privileged network position may be able to **intercept network traffic** | This issue was addressed through improved state management. | 122404（iPhone 16e） | 致谢为中文姓名：秦若涵、崔志伟、崔宝江 |
| CVE-2026-28943 | IOHIDFamily | An app may be able to **determine kernel memory layout** | A logging issue was addressed with improved data redaction. | 127110–127117、127118、127119 共 **7 页** | 致谢直接写机构名：**Google Threat Analysis Group** |
| **CVE-2026-43807** | MobileAccessoryUpdater | **A malicious accessory** may be able to cause unexpected app termination | A buffer overflow was addressed with improved bounds checking. | 127594、127595、128068–128072、**148287** 共 **8 页** | Entry added July 27, 2026；Tristan Madani (@TristanInSec) of Talence Security |
| **CVE-2026-86924** | MobileAccessoryUpdater | **Connecting a malicious accessory** may cause unexpected system termination | A memory corruption issue was addressed with **improved input validation**. | 149034（iOS 27 与 iPadOS 27）、149035（macOS Golden Gate 27）、149041（iOS 26.7 与 iPadOS 26.7）、149042（macOS Tahoe 26.7）共 **4 页** | （本次镜像内无条目备注行） |
| **CVE-2026-86885** | Baseband | **An attacker in radio range** may be able to cause unexpected system termination | An input validation issue was addressed with improved input validation. | 149034 | 同上 |
| **CVE-2026-86879** | Baseband | A **remote attacker** may be able to cause a denial-of-service | A denial-of-service issue was addressed with improved input validation. | 149034 | 同上 |
| **CVE-2026-86963** | Baseband | An attacker in a **privileged network position** may be able to cause a denial-of-service | A denial-of-service issue was addressed with improved input validation. | 149034、149041 共 **2 页** | **Entry added October 19, 2026** |

最后四行全部是容错重测后才进入本族的条目，它们共同构成第 10.5 节与第 10.6 节的内容。

## 10.3 修复类别分布（71 段内，按 Description 原句词族）

统计口径先说清楚：只取每个段落的 **Impact 段与 Description 段两个字段**，不含 `Available for`、致谢行与条目备注；匹配区分大小写不敏感、整段只记一次。**71 段里有 68 段带 Description**（3 段纯致谢没有），下表按"每段归入其 Description 的主干句式"完全分解，**逐行相加 = 68，无剩余**。

| 修复句式 | 段数 | 完全分解 |
|---|---:|---|
| **improved bounds checking** | **17** | CVE-2025-43302 的 8 页 + CVE-2026-43807 的 8 页 + CVE-2026-28875 的 1 页 |
| **improved input validation** | **8** | CVE-2026-86924 的 4 页（`MobileAccessoryUpdater`）+ CVE-2026-86879、86885、86963×2 的 4 页（`Baseband`）——**初稿把这一行写成 0** |
| improved locking | 8 | CVE-2026-28992 的 8 页 |
| improved data redaction | 7 | CVE-2026-28943 的 7 页 |
| improved checks | 6 | CVE-2024-54547 的 3 页 + CVE-2025-30455 的 2 页 + CVE-2026-28874 的 1 页 |
| additional entitlement checks | 6 | CVE-2024-27799 的 4 页 + CVE-2025-24095 的 2 页 |
| **removing the vulnerable code** | 5 | CVE-2025-31187 的 3 页 + CVE-2025-43198 的 2 页（均 `Dock`） |
| removing sensitive data | 3 | CVE-2024-44177 的 3 页（均 `Dock`；与上一行**不是同一句式**，一个是删代码、一个是删数据） |
| improved state handling | 3 | CVE-2025-43420 的 3 页（同 CVE 的 `race condition` 亦为 3 段） |
| improved restrictions | 2 | CVE-2024-23244 的 2 页（同族句式 `logic issue` 亦为这 2 页） |
| improved state management | 1 | CVE-2025-31214 的 1 页 |
| improved validation of environment variables | 1 | CVE-2024-54536 的 1 页（`NVRAM` 亦 1 段） |
| improved handling of symlinks | 1 | CVE-2024-54554 的 1 页 |
| **合计** | **68** | = 71 − 3 段纯致谢 ✓ |
| improved memory handling | **0** | 该句式不属于核心族；它出现在相邻的 `SDK`（CVE-2025-24132） |
| improved authentication | **0** | 属相邻的 `MobileLockdown`（第 8.3 节） |
| improved validation of symlinks | **0** | 全语料该句式集中在 `PackageKit` 等段落，核心族 0 次 |

后果侧的词族同一口径下的数字：`buffer overflow` **9**（CVE-2026-43807 的 8 + CVE-2026-28875 的 1）、`memory corruption` **12**（CVE-2026-28992 的 8 + CVE-2026-86924 的 4）、`out-of-bounds` **8**（全部是 CVE-2025-43302 的 "out-of-bounds write"；`out-of-bounds read` 在核心族 **0**）、`malicious accessory` **12**（CVE-2026-43807 的 8 + CVE-2026-86924 的 4）、`denial-of-service` **4**（全部 `Baseband` 新增四条）。**`crafted` 一词在本族 71 段内 0 次**——配件族没有一条写成"maliciously crafted <对象>"，这与 #106 的字体族 54 段全部命中该句式形成方法对照（零结果行见第 14.1 节末）。

三条读法：

1. **"removing the vulnerable code"（移除易受攻击代码）出现 5 段**——这是措辞层里最接近"这条能力被砍掉了"的说法，本篇在第 18 章把它转成一条运维核查项：升级后某功能消失，可能对应这一句式，而不是配置错误。另有 3 段是"removing sensitive data"，两者不可合并计数。
2. **entitlement 类 6 段**与第 9.4 节 `Power Services` 的 8 段合起来，是配件方向最常见的修复类别之一；手册层对 entitlement 没有定义页（它属于开发者文档），本篇因此不解释 entitlement 的语义，只统计。
3. `redaction`（脱敏）7 段全部服务于同一 Impact——"determine kernel memory layout"。这条与 #102（内核状态）、#96（KASLR）交叉，本篇只在配件族范围内引用它。

## 10.4 `Baseband` 的八段：本族里唯一一组带"距离"的条目

`Baseband` 段落名在本族实测 **8 段 / 4 页**（122404、126792、149034、149041），初稿只看到 3 段 / 2 页。八段的 Impact 主语完全分解：

| Impact 主语（原句起首） | 段数 | CVE |
|---|---:|---|
| A remote attacker | **3** | 2026-28874（unexpected app termination）、2026-28875（denial-of-service）、2026-86879（denial-of-service） |
| An attacker in a privileged network position | **3** | 2025-31214（intercept network traffic）、2026-86963 × 2 页（denial-of-service） |
| **An attacker in radio range** | **1** | 2026-86885（unexpected system termination） |
| （无 Impact：纯致谢段） | 1 | 无编号（149034，SyNSec Lab at Penn State 一行八人） |

⇒ 本族内**带距离/位置限定的 Impact 主语共 7 段**，全部在 `Baseband` 名下。三条读法：

1. 配件族里以 **remote attacker** 为主语的条目现在有 3 段（初稿为 2 段）。#103、#104 都得到"近场段落族没有 remote attacker"的负结果；本篇用它划定该规律的边界——在同为物理/硬件族的 `Baseband` 段落里，remote attacker **有**，所以"硬件族没有远程攻击者措辞"不是规律，只是分段现象。
2. **`in radio range` 是本期镜像里新出现的一个距离措辞**：全语料 9 段 / 8 页，其中 `Bluetooth` 8 段、`Baseband` 1 段。本篇不推断"无线电距离"等于哪个技术（G14）；只登记措辞本身。
3. 28875 与 86879、86885 的 `Available for` 都只写 **iPhone 16e** 或 **iPhone 11 and later** 一类机型清单，不含 iPad/Mac——本族内只有 `Baseband` 与 `RepairKit` 出现这种"机型清单窄于同页其它条目"的形态。

## 10.5 `Connecting a malicious accessory`：措辞层第一次把"插"这个动作写成主语

CVE-2026-86924（`MobileAccessoryUpdater`，4 段 / 4 页：149034、149035、149041、149042）字段文本逐字：

- Impact：**Connecting a malicious accessory** may cause unexpected system termination
- Description：A memory corruption issue was addressed with **improved input validation**.

这条为什么在本篇占一节：**它是全语料 9,045 段里唯一一条把"连接一个配件"这个动作本身写成 Impact 主语的配件条目。** 对照本族另一条配件条目 CVE-2026-43807，主语是 "**A malicious accessory** may be able to cause unexpected app termination"——施事者是配件；86924 的施事者是"连接"这件事。

由此可写与不可写的边界：

1. **可写**：措辞层在 2026 年 10 月这一批页面上，用两种不同的主语描述了同为"恶意配件"的两条缺陷；其中一条的主语与手册层第 1.2 节"门二"的语法位置相同（门二管的是"现在允不允许建立数据连接"，而不是"这个配件是不是被批准的"）。
2. **不可写**：不能由此说"Apple 承认连接动作本身存在缺陷"。主语换成动名词是**措辞变化**，不是责任归属变化；官方文本没有任何一句把 86924 与"安全激活数据连接"这页联系起来（第 12 章双向沉默）。
3. **不可写**：不能把 86924 与手册层的认证 IC、质询握手连成因果链（T3 源码层本期不可用，G1）。

**并且：这条条目落在两个 4 页清单的交集里**——它的 `Available for` 在 iOS 分支写 iPhone 11 and later 系列，在 macOS 分支写 MacBook Neo (2026) 等机型清单；这四页全部是容错解析器重测后才可见的页面。

## 10.6 一页五个本族段落：149034 的密度

149034（iOS 27 与 iPadOS 27）在本族里承载 5 个段落：`Baseband` 86885、`Baseband` 86879、`Baseband` 86963、`Baseband` 纯致谢一段、`MobileAccessoryUpdater` 86924。这一页同时把第 9 章的 `Terminal` 纯致谢也带了出来（第 13.3 节）。

⇒ 方法事实：**本页面上本族段落数是"一个 CVE 一页"的 5 倍**，所以任何按页取数（"这一页有多少条配件相关缺陷"）与按段取数（"本族有多少段"）在此页分别得 1 与 5；本篇全部计数按**段落**取，页数只在需要说明覆盖分支时给出。同类问题在 #106 的第 16.5 节里表现为"同一页同一个段落名出现三次"，两篇因此在附表 D 里共用了同一条取数纪律。

# 11 合图：一条物理连接上的十八行判定

本章把第 1–10 章合成一张矩阵。矩阵里每一个单元格要么有原句支撑，要么写"未述"。

## 11.1 主矩阵

| # | 判定 | 发起/执行侧 | 凭据或密钥 | 存放位置（手册层） | 时限或失效条件 | 用户可关 | 组织可限制 | 原句出处 |
|---|---|---|---|---|---|---|---|---|
| 1 | 配件是不是被批准的 | 配件发起，设备验证 | Apple 提供的证书 + 已签名应答 | **定制集成电路 (IC)**，Apple 提供给获许可厂商 | **未述** | 未述 | 未述 | `sec70a4f377d` |
| 2 | 通过后能请求什么 | 配件请求，设备授权 | 同上 | 未述 | 未述 | 未述 | 未述 | 同上 |
| 3 | 未通过时能做什么 | — | — | — | — | — | — | 同上：仅限模拟音频 + 一小部分 UART 播放控件 |
| 4 | AirPlay/CarPlay 流的会话密钥 | 双向 | ECDH(Curve25519) 临时密钥 | **认证 IC 的 1024 位 RSA 密钥签名** | 未述 | 否 | 未述 | 同上 |
| 5 | 接收器是否被批准（AirPlay） | 设备验证接收器 | 同一认证 IC | 未述 | 未述 | 未述 | 未述 | 同上 |
| 6 | 现在是否允许建立数据连接 | 设备（需用户本机凭据） | 面容 ID / 触控 ID / 密码 | 未述（凭据侧另有 SEP 体系，本篇不跨引） | **1 小时**（锁后或连接终止起算） | 可（设置中开"始终"） | 未述 | `sec5044aad1b` |
| 7 | 该配件是否被记住 | 设备 | "曾在解锁状态下连接过"这一事实 | 未述 | **30 天**（自上次连接） | 未述 | 未述 | 同上 |
| 8 | 长期连接后的收紧 | 设备 | — | — | **3 天**后锁定即禁新连接 | 未述 | 未述 | 同上 |
| 9 | 未知配件尝试的后果 | 设备 | — | — | 该连接上**所有**配件数据连接取消激活，直到重新解锁 | 未述 | 未述 | 同上 |
| 10 | 生物认证状态联动 | 系统 | — | — | 设备处于"需密码重新启用生物认证"态时，这些数据连接不活跃 | 未述 | 未述 | 同上 |
| 11 | 辅助设备自动开启 | 系统 | — | — | 设置某些辅助设备时自动启用"始终数据连接" | — | 未述 | 同上 |
| 12 | 外设能否访问未映射内存 | IOMMU | — | 每 DMA 代理一个 IOMMU，独立转换表 | 越界即**引发内核崩溃** | 未述 | 未述 | `seca4960c2b5`（仅 Mac） |
| 13 | 启动早期是否已阻断 | 硬件 | — | — | Intel 侧 default-deny，**通电即开始** | 未述 | 未述 | 同上（仅 Intel Mac） |
| 14 | T2 的 UEFI 驱动运行环境 | 固件 | — | 受限 **Ring 3** | macOS 11 起 | 未述 | 未述 | 同上（仅 Mac） |
| 15 | 部件是否属于本机 | iPhone 自身 | 部件来源判定 | 未述 | 未述 | 未述 | 未述 | `sec0f8dfd030`：校准受限 |
| 16 | 激活锁的绕过凭据 | 设备管理服务 | **31 字节绕过代码** | 服务侧**托管（escrow）**/ Apple 服务器 | 三种情形生成新代码 | 否（受监督默认不允许启用） | 由设备管理服务决定 | 同上 |
| 17 | 研究设备的启动约束 | iBoot 变体 | 特熔丝 + Boot Kernel Collection | 未述 | 未在充电即进入恢复模式 | — | — | `seca7ff718d2` |
| 18 | 硬件断麦的前置配件 | 硬件开关 | — | — | — | 否 | 未述 | `secbbd20b00b`：2020+ iPad 需符合 MFi 的保护壳 |

## 11.2 三个从合图读出的分布事实

1. **"未述"集中在两列**：存放位置（18 行里 13 行未述）与组织可限制（18 行里 16 行未述）。手册层对配件通道写得最细的是**时间与状态**，最粗的是**密钥落点与可管理性**。这与 #104 的合图正好相反——连续互通那七条通道的密钥落点大多给了（钥匙串 / iCloud / IDS / SEP），时限大多没给。**两篇合起来可以给出一条一般性观察：Apple 手册层倾向于交代"信任存在哪"或"门开多久"其中之一，很少两者都给。** 本篇把它标为观察而非规律（第 16 章事实 5）。
2. **只有第 6 行同时给了"用户可关"与时限**。这条是门二。它是本篇唯一一条既是防线、又可被用户在设置里关掉、且手册层自己说明了为什么要设时限的机制。
3. **措辞层在本表 18 行里能对上的只有 4 行**：第 6/7 行 ↔ `Trusted Device` 与 `Accessibility`（第 7.1 节，5 段）；第 16 行 ↔ `Activation Lock`（1 段纯致谢）；第 1 行 ↔ 无（`MFi` 段落名不存在）；第 12–14 行 ↔ 无（这些是 Mac 固件/硬件行为，且 T3 不可用）。**其余 14 行在措辞层没有对应条目。** 这张"对不上"的清单比任何单条 CVE 都更能说明：Apple 承认的配件方向缺陷，与手册层描述的配件防线，交集极小。
# 12 双向沉默：手册层不说攻击，措辞层不说机制

前两篇各做一次单向普查（手册层说了什么 → 措辞层有没有对应词）。本篇做**双向**，因为这条通道的两层之间出现了两个方向相反的缺口。

## 12.1 手册层中文全量 179 页普查

普查工具与口径见附表 D。中文侧为**全量 179 页**，所以这里的 0 可以是全称结论；英文侧只有 27 页取样，0 只能是取样结论（第 12.5 节）。

| 术语 | 次数 / 命中页数 | 本篇的用法 |
|---|---|---|
| MFi | **8 / 3** | 只在三页：配件验证、HomeKit、硬件断麦 |
| 配件 | 98 / 9 | 承载页极少：9 页 |
| 雷雳 | 11 / 8 | Thunderbolt 的中文译名（见 12.2） |
| 闪电 | 4 / 4 | Lightning 的中文译名 |
| USB-C | **2 / 2** | 只在配件验证页与另一页 |
| USB（英文原形） | 15 / 13 | |
| 集成电路 | 9 / 4 | "定制集成电路 (IC)"集中于配件验证页 |
| 质询 | 4 / 4 | |
| 授权证明 | **1 / 1** | 只出现在配件验证页 |
| MFi-SAP / 安全关联协议 | **1 / 1**（各一次，同页） | 全套密码学参数由一句承载 |
| 模拟音频 | **1 / 1** | 降级路径同样只有一句 |
| 直接内存访问 / IOMMU | 11 / 9；12 / 4 | |
| 激活锁 | 65 / 7 | |
| 校准 | **4 / 3** | 部件校准句只在激活锁页 |
| 维修 | **2 / 1** | **同一主题的全部文本厚度：一页** |
| 部件 | 5 / 1 | 同上，只一页 |
| 安全隔区 | 322 / 51 | 对照项：本篇主线一页都没用到 |
| 内核 | 166 / 42 | |
| 端对端 | 65 / 26 | |
| **受限模式 / USB Restricted Mode** | **0 / 0** | 第 7.3 节 |
| **通用串行总线**（USB 中文全称） | **0 / 0** | 手册层不用全称 |
| **验证件** | **0 / 0** | |
| **游戏控制器** | **0 / 0** | 对照第 9.3 节：措辞层有该段落名、3 段 |
| **隔离标记 / quarantine** | **0 / 0** | 对照 #104 第 14 节：AirDrop 的 CVE-2024-54564 正是隔离标记 |
| **配对模式** | 1 / 1 | |

三条硬结论：

1. **一页承载一条防线**：`授权证明`、`MFi-SAP`、`模拟音频` 三个词都只出现在同一页、各出现 1 次。门一的机制描述在手册层里的总厚度就是**一段半话**。
2. **"维修 / 部件 / 校准"三词各自命中 1 页**，且是同一页（`sec0f8dfd030`）。第 6.1 节讨论的部件级绑定，在整本手册里是**一处孤文**。
3. **两个"0"是本篇最重要的负结果**：手册层既没有"受限模式"这个名字（第 7.3 节），也没有"隔离标记"这个词。前者造成 T1/T2 无法互查，后者说明措辞层的一些修复概念（隔离标记类）在手册层完全没有对应叙事。

## 12.2 中英译名错配：一条会让任何统计失真的坑

同一接口在两层文本里的四种写法：

| 接口 | T1 中文页写法 | T1 英文取样写法 | T2 措辞层写法 |
|---|---|---|---|
| Thunderbolt | 雷雳（11 / 8 页） | Thunderbolt（7 / 4 页） | 仅出现在机型清单（94 次，1 页） |
| Lightning | 闪电（4 / 4 页） | Lightning（3 / 3 页） | **0 次** |
| USB | USB（15 / 13 页）；全称"通用串行总线"**0** | USB（8 / 6 页） | USB（6 次 / 6 页） |
| USB-C | USB-C（2 / 2 页） | USB-C | **1 次**（Impact 主语位） |

本篇必须把这条写成一节而不是脚注，因为它是**方法级**的：如果只按英文词检索中文 179 页，`雷雳` 会被统计成 Thunderbolt 0 次、`闪电` 会被统计成 Lightning 0 次，于是得出"手册层完全不提这两种接口"的错误结论。**任何跨中英双语的手册层普查都必须先建立译名表**，本篇的译名表进附表 A。

同理，措辞层 Thunderbolt 的 94 次**全部在 Available for 的机型描述里**（如第 7.2 节那条 "MacBook Pro (13-inch, 2020, Four Thunderbolt 3 ports)"），一次也不在 Impact 或 Description 里。按段落体计数而不区分字段，会把机型清单读成技术措辞——本篇的检索脚本因此对字段做了顺序拆分（附表 D 方法 3）。

## 12.3 完整性句式的对照：为什么"没写"在文本层是可辨识的

第 3.3 节留下一个方法问题：手册层在 MFi-SAP 那句里没写完整性算法，这能不能算一条文本事实？本篇用同一语料库内的正向对照来回答——手册层**确实会**在同一句里并列写出两种动作：

| 页 | 原句（节选） | 并列了哪两种动作 |
|---|---|---|
| `secf78dbe639`（接力，#104 第 2.1 节） | "This key can encrypt **and authenticate** the BLE advertisements…" | 加密 + 认证，同一把密钥 |
| `secf78dbe639` | "with **replay protection** measures" | 另有重放保护，明示 |
| `sec70a4f377d`（本篇） | "使用 AES-128 在计数器 (CTR) 模式下…**进行加密**" | **只写加密，未写认证、未写重放保护** |

结论的强度到此为止：**"会写"这一事实可以在语料内部被证明，因此"这一页没写"是一个可辨识的文本差异。** 本篇仍不把它升级为协议结论（MFi-SAP 可能通过其它机制提供完整性，手册层没写而已）。这条差异登记为 G2 的一部分。

## 12.4 T1 与 T2 用词的四组正面对照

前两节讲错位，这一节讲本篇能找到的全部对齐点——它们很少，正因为少才有引用价值：

| T1 机制句 | T2 措辞 | 对齐程度 |
|---|---|---|
| 「这些配件会在上次连接后的 30 天内被记住」（门二） | 段落名 **`Trusted Device`** | **弱对齐**：语义相近（谁被信任），但 T2 那条讲的是"启动时连接不受 USB Restricted Mode 保护"，未提记忆期 |
| 「用户必须使用面容 ID、触控 ID 或密码激活…数据连接」 | 「An attacker with a USB-C connection to an **unlocked** device…」（122371） | **强对齐**：T2 的前置条件（已解锁）正是 T1 那道门的**反面状态**；这条缺陷发生在门已开的时候 |
| 「If the IOMMU detects… it institutes a **kernel panic**」 | 「An app may be able to cause unexpected **system termination**」（`MobileAccessoryUpdater`、`DriverKit` 多处） | **不可对齐**：T1 说的是防护性崩溃，T2 说的是缺陷后果。同一个"崩溃"在两层里语义相反（第 18.4 节据此设计核查项） |
| MFi-SAP 覆盖「AirPlay 音频流和 CarPlay 车载视频流」 | `122403` 页标题「AirPlay audio SDK 2.7.1, AirPlay video SDK 3.6.0.126, and CarPlay Communication Plug-in R18.1」 | **主题对齐**：同一对服务，T1 给参数、T2 给两条内存类后果；层间无互引 |

第二行是本篇最有用的一次跨层对齐：**T1 解释为什么解锁态是危险的边界，T2 恰好给了一条只在解锁态才成立的缺陷。** 但本篇把它写成"两条文本在同一维度上相遇"，不写成"T2 的缺陷由 T1 的机制设计导致"。

## 12.5 取样口径与两条必须自我限制的句子

本篇的中英口径不对称，必须在正文里出现两次以上：

- **中文 179 页 = 全量**。所以"手册层没有 X"在中文词形上可以是全称结论。
- **英文 27 页 = 取样**。所以英文侧任何 0 只能写成"本期英文取样的 27 页里为 0"。

由此，本篇自我禁止两种写法：

1. 不写"Apple 手册层从不使用 USB Restricted Mode 这个英文词"——英文侧只是取样；本篇的正式表述是"中文全量 179 页无'受限模式'，英文取样 27 页无 'Restricted Mode'"。
2. 不写"措辞层从未提及 MFi"——`122403` 确实提及了；本篇的正式表述是"MFi 在 257 份镜像中仅出现于 1 页，且仅作为 `Available for` 的取值"。

# 13 段落名存在性与不存在清单

本章是第 0.2 节规则 3（"段落名不是实现层"）的操作化：把可用名字与不可用名字都列出来，让正文无法越界。

## 13.1 与本篇相关的段落名：存在

| 段落名 | 段数 | 页面数 | 主要 CVE |
|---|---|---|---|
| IOHIDFamily | 27 | 20 | 2024-27799、2025-43302、2026-28992、2026-28943 |
| DiskArbitration | **28** | **21** | （本篇只作计数，不逐条） |
| Dock | 19 | 17 | 2024-23244、2024-44177、2024-54554、2024-54547、2025-30455、2025-31187、2025-43198、2025-43420 |
| CoreUtils | 16 | 16 | 2025-31203、2026-28886 |
| MobileAccessoryUpdater | **14** | **14** | 2024-54536、2026-43807、**2026-86924** |
| Power Services | 8 | 8 | 2025-24173 |
| BiometricKit | 8 | 8 | （本篇只作计数） |
| DriverKit | 8 | 4 | 2026-43753、2026-43793（+ 1 段纯致谢） |
| **Baseband** | **8** | **4** | 2025-31214、2026-28874、2026-28875、**2026-86879、2026-86885、2026-86963**（+ 1 段纯致谢） |
| MobileLockdown | 4 | 4 | 2025-24193（+ 3 段纯致谢） |
| Game Controllers | 3 | 3 | 2024-44265 |
| RepairKit | 2 | 2 | 2025-24095 |
| srd_tools | 2 | 2 | （全纯致谢） |
| SDK | 2 | 1 | 2025-24132、2025-30422 |
| Activation Lock | 1 | 1 | 124147（纯致谢） |
| Trusted Device | 1 | 1 | 2025-43262 |
| **合计** | **151** | — | — |

本表 16 个段落名合计 **151 段**（初稿为 140），逐格分解为：配件族核心 **71**（第 10.1 节）+ 逐条分析的相邻 **44**（第 9.8 节，已去重）+ 只作计数不纳入分析的 **36**（`DiskArbitration` 28、`BiometricKit` 8）= 151，无剩余。`Activation Lock` 只算一次，算在核心 71 里。**增量的全部来源是解析器标记代次，不是新抓取页面**（第 14.6 节）：核心族 +9（MobileAccessoryUpdater +4、Baseband +5）、存在性组 +2（DiskArbitration +2）、相邻组 +0。

## 13.2 不存在的段落名（禁止使用清单）

以下名字在本期 257 份镜像里**不作为 `<h3 class="gb-header">` 组件段落出现**：

`USB`、`USB-C`、`Lightning`、`Thunderbolt`、`MFi`、`MFi Program`、`Accessory`、`iAP`、`CarPlay`、`NFC`、`Secure Element`、`Hotspot`、`Universal Clipboard`、`Pairing`、`Bonding`、`Continuity`、`Personal Hotspot`、`Ultra Wideband`、`Nearby`、`Call Relay`、`Text Message Forwarding`、`Auto-unlock`。

⇒ 由此直接禁止的句式（本篇一条也不写）：

- "Apple 在 USB 组件下披露了……"
- "MFi 组件的漏洞"
- "CarPlay 组件修复了 CVE-2025-24132"（该条实际段落名是 `SDK`，且 `Available for: MFi Program`）
- "Lightning 接口相关的 CVE……"（`Lightning` 在全语料 0 次）

## 13.3 `Terminal` 段落：一次自我纠错的实证

本篇在检索过程中曾按一条既有笔记假定：存在一条"Terminal.app 不遵守 USB Restricted Mode"的顾问条目。实测结果是：

- 笔记指向的页号 `122085` 抓取后，页面**不含任何安全内容段落**（其 `<h3>` 只有 `Warning`、`Tools`、`Important`），不是一份"About the security content of …"页；
- 段落名为 `Terminal` 的段落共有，实测 **6 段 / 5 页**：121238（Koh M. Nakagawa (@tsunek0h)，Entry added March 3, 2025）、122373（Paweł Płatek (Trail of Bits)，Entry added May 28, 2025）、125634（Johann Rehberger）、**149034**、**149035 ×2**。前五段是纯致谢；**第 6 段带完整字段**——149035（macOS Golden Gate 27）的 CVE-2026-28937，Impact「An app may be able to access sensitive user data」，Description「This issue was addressed through improved state management.」。**初稿写过"Terminal 段落全部是纯致谢"，这句在容错重测后不成立**：多出的 3 段全部落在漏采的 149034/149035 两页上。
- 但本篇的核心负结果**不变**：全语料 `restricted mode` 的 5 段（第 7.1 节）里**没有一段**挂在 `Terminal` 下，`USB Restricted Mode` 仍然只在 `Accessibility`（4 段）与 `Trusted Device`（1 段）两个段落名下出现。

⇒ 本篇把这条作为第 17 章 G12 与第 16 章事实 3 的实例，并给出方法论结论：**任何"某条目讲 X"的既有笔记都必须回原文重测后才能进正文**；而且**重测必须连同解析器一起重测**——一条"全部是纯致谢"式的全称句，在换用容错解析器之前是不成立的。本篇因此不引 `122085`。

# 14 措辞层普查：配件族词频，以及前两篇负结果的边界

本章是全篇的数字核心。第 14.3 节给出的逐格分解意味着：**本节每一个非零计数都能被具名 CVE 完全解释，没有剩余。**

## 14.1 配件族 71 段的族内词频

| 词/词族 | 命中段数 | 说明 |
|---|---|---|
| "improved bounds checking" | **17** | 43302 的 8 + 43807 的 8 + 28875 的 1（第 10.3 节）——核心族第一大修复句式 |
| **"improved input validation"** | **8** | **86924 的 4（`MobileAccessoryUpdater`）+ 86879、86885、86963×2 的 4（`Baseband`）；初稿写 0** |
| "malicious accessory" | **12** | 43807 的 8 + **86924 的 4**（后者的主语是 "Connecting a malicious accessory"） |
| memory corruption | **12** | 28992 的 8 + **86924 的 4** |
| buffer overflow | 9 | CVE-2026-43807 的 8 + CVE-2026-28875 的 1，无剩余 |
| privilege（含 unprivileged） | **9** | 27799 的 4（"An unprivileged app"）+ 23244 的 2（"escalate privilege"）+ 31214 与 86963×2 的 3（"privileged network position"） |
| out-of-bounds | 8 | 全部是 CVE-2025-43302 的 "out-of-bounds write"；`out-of-bounds read` 在本族 **0** |
| improved locking | 8 | 全部 CVE-2026-28992 |
| kernel / "kernel memory layout" | 7 / 7 | 全部是 CVE-2026-28943 的 7 页（逐格分解见 14.2） |
| "additional entitlement checks" / entitlement | 6 / 6 | CVE-2024-27799 的 4 + CVE-2025-24095 的 2 |
| "improved checks" | 6 | 54547 的 3 + 30455 的 2 + 28874 的 1 |
| "removing the vulnerable code" | 5 | = CVE-2025-31187 的 3 页 + CVE-2025-43198 的 2 页（均为 `Dock`），无剩余 |
| "removing sensitive data" | 3 | CVE-2024-44177 的 3 页（`Dock`）；与上一行**是不同句式**，不可合并 |
| denial-of-service | **4** | 全部 `Baseband`：28875、86879、86963×2 |
| secure input / keystroke | 4 / 4 | 全部是 CVE-2024-27799 的 4 页 |
| race condition | 3 | 全部 CVE-2025-43420（`Dock`，同一 CVE 的句式是 "improved state handling"） |
| bypass / Privacy preferences | 2 / 2 | CVE-2025-24095 的 2 页 |
| symlink | 1 | CVE-2024-54554 的 "improved handling of symlinks"；`improved validation of symlinks` 在本族 **0** |
| NVRAM | 1 | CVE-2024-54536 |
| remote attacker | **3** | 全在 `Baseband`（2026-28874、28875、**86879**） |
| **in radio range** | **1** | CVE-2026-86885（`Baseband`，149034）；全语料 9 段 / 8 页，另 8 段在 `Bluetooth` |
| **USB** | **0** | 配件族 71 段内一次没有 |
| **Lightning** | **0** | — |
| Thunderbolt | **0** | 字段级为 0；全语料 94 次 `Thunderbolt` 全部落在 `Available for` 机型清单里，且集中在 125110 一页（第 12.4 节与附表 D 方法 3） |
| **crafted** | **0** | 本族没有一条写成 "maliciously crafted <对象>"；对照 #106 字体族 54 段全部命中该句式 |
| sandbox | **0** | 本族 0；相邻的 `Power Services` 8 段里 8 次（第 9.4 节） |
| actively exploited / Apple is aware of a report | **0 / 0** | 见 14.4 |
| man-in-the-middle / eavesdrop / spoof / pairing / authenticat- / encryption / physical proximity / "physical access" / "locked device" | **0** | 全族（`improved authentication` 的 1 段属相邻 `MobileLockdown`，第 8.3 节） |

统计口径与第 10.3 节一致：**只取 Impact 段与 Description 段**，不含 `Available for`、致谢行与条目备注。这一口径直接决定了 `Thunderbolt` 与 `symlink` 两行的数字——把整块文本算进来会得到更大的数，属于机型清单污染词频（第 12.4 节）。

**本表与初稿的差在四处**：`improved input validation` 从 0 变 8、`malicious accessory` 从 8 变 12、`memory corruption` 从 8 变 12、`remote attacker` 从 2 变 3；另外新增 `in radio range` 与 `crafted` 两行。**前四项全部是漏采造成的低估，最后一项（`crafted` = 0）是重测后仍然成立的负结果**——这两件事必须分开写：漏采会让 0 变成非 0，但不会让非 0 变成 0，所以重测后仍为 0 的行比上修的行更可信。

最后一行是本篇要**限定**而不是重申的一行。它在配件族核心 71 段内成立；第 14.4 节说明它在全语料不成立。

## 14.2 `kernel` 的 7 段：逐格分解

| CVE | 段落名 | Impact 里的 kernel 用法 | 页数 |
|---|---|---|---|
| CVE-2026-28943 | IOHIDFamily | An app may be able to determine **kernel memory layout** | 7 |

配件族内 `kernel` 的 7 段 = **CVE-2026-28943 的 7 页，无剩余**。这条分解的价值在于它与 #104 的 Wi-Fi 族形成互补：#104 的 Wi-Fi 族 23 段 `kernel` 里包含 "corrupt kernel memory"、"execute arbitrary code with kernel privileges"、"out-of-bounds read in kernel memory" 三类**后果**；而配件族唯一的 kernel 用法是**信息泄露类**（determine kernel memory layout），修复类别是 logging issue + improved data redaction。

⇒ 可用结论（限定在措辞层）：**Apple 在配件族段落里对内核只做一件事——承认 App 能看出内核内存布局；它没有在任何配件族段落里写"内核代码执行"。** 这条与 #102（内核状态被非法修改）交叉引用，但本篇不把它扩展到 Wi-Fi 族。

## 14.3 五个关键措辞的逐格分解（全语料）

| 措辞 | 全语料段落数 | 完全分解 |
|---|---|---|
| `physical attack` | **4** | = CVE-2025-24200 的 4 页（122173、122174、122345、122346），无剩余 |
| `USB Restricted Mode` / `restricted mode` | **5** | = 24200 的 4 页 + CVE-2025-43262 的 125110，无剩余 |
| `supplementary fix` | **8** | 全部在 `WebKit`，其中 122345 与 122346 两页与 24200 同页出现 |
| `user in a privileged network position` | **10** | = `CoreUtils` 8（2026-28886 的 8 页）+ Passwords 1 + lldb 1 |
| `attacker in a privileged network position` | **63** | 前三大段落：Kernel 16、802.1X 8、Wi-Fi 7 |

## 14.4 反转：`Apple is aware of a report` = 93 段 / 65 页

这是本篇相对 #103、#104 的**唯一实质性方法结论**：

| 句式 | 段落数 | 页数 | 段落分布（前列） |
|---|---|---|---|
| Apple is aware of a report | **93** | **65** | WebKit 27、Messages 9、Kernel 8、RTKit 8、CoreMedia 8、ImageIO 7、dyld 5、CoreAudio 5、JavaScriptCore 5、RPAC 4、**Accessibility 4**、CoreGraphics 3 |
| extremely sophisticated attack against specific targeted individuals | 59 | 42 | 上表子集，含 Accessibility 4 |
| actively exploited | **18** | 13 | CoreMedia 8、WebKit 5 等；**配件族核心 71 段内 0** |

三条限定，一条都不能省：

1. **`actively exploited` 与 `Apple is aware of a report` 是两个不同句式，不能互换统计。** 前者 18 段、后者 93 段。#103/#104 报的是前者的 0；本篇报的是后者的存在。**Apple 对 CVE-2025-24200 用的是后者。**
2. **93 段的绝大多数在软件解析栈（WebKit、Messages、ImageIO、CoreMedia、JavaScriptCore），不在配件栈。** 本篇不夸大为"配件方向有大量在野利用"——配件相关的只有 `Accessibility` 4 段。
3. **这两条规律都只在"段落族"尺度上成立。** 把近场族、配件族、软件族合并统计会同时抹掉 #103/#104 的负结果与本篇的正结果。第 16 章事实 1 把这条写成方法论。

## 14.5 一处同族句式、两个段落名

| 页 | 段落名 | Description | Impact |
|---|---|---|---|
| 121839 | MobileAccessoryUpdater | The issue was addressed with **improved validation of environment variables** | An app may be able to edit **NVRAM variables** |
| 128067/128071/128072 | DriverKit | An issue existed in the **handling of environment variables**. This issue was addressed with improved validation. | An app may be able to cause unexpected system termination |

两段的 Description 是同一族句式（环境变量校验），但段落名不同、后果不同（可编辑 NVRAM 变量 / 系统终止）。本篇引用这一对的唯一目的是说明：**措辞层的 Description 是模板化文本，不能靠 Description 相似度推断缺陷同源**（第 17 章 G5）。

## 14.6 本篇最严重的一次自我纠错：解析器只认一代 HTML 标记

本篇初稿的全部 T2 数字是用同一个正则切段落得出的：

1. 段落起点：`<h3 class="gb-header">`——**闭引号紧跟 `gb-header`**；
2. 段落内文本：`<p class="gb-paragraph">`——同样要求闭引号紧跟；
3. 段落终点：下一个 `<h3 class="gb-header">`。

这套写法在 247 份旧页面上工作正常。但本期镜像里最新的 10 页（149034 iOS 27 与 iPadOS 27、149035 macOS Golden Gate 27、149036 tvOS 27、149037 watchOS 27、149038 visionOS 27、149039 Safari 27、149040 Xcode 27、149041 iOS 26.7 与 iPadOS 26.7、149042 macOS Tahoe 26.7、149043 macOS Sequoia 15.8）把这两个 class 写成了带对齐后缀的形式：`<h3 class="gb-header alignment horizontal-align-left">` 与 `<p class="gb-paragraph alignment horizontal-align-left">`。**闭引号前多了字符，严格正则在这 10 页上命中 0。**

改为容错写法（`gb-header[^"]*"`、`(?:gb-paragraph|paragraph)[^"]*"`，终点判断只取 `gb-header` 前缀）后重测：

| 页号 | 页面 | 旧解析器段数 | 容错后段数 |
|---:|---|---:|---:|
| 149034 | iOS 27 与 iPadOS 27 | 0 | 209 |
| 149035 | macOS Golden Gate 27 | 0 | 314 |
| 149036 | tvOS 27 | 0 | 118 |
| 149037 | watchOS 27 | 0 | 128 |
| 149038 | visionOS 27 | 0 | 165 |
| 149039 | Safari 27 | 0 | 13 |
| 149040 | Xcode 27 | 0 | 9 |
| 149041 | iOS 26.7 与 iPadOS 26.7 | 0 | 103 |
| 149042 | macOS Tahoe 26.7 | 0 | 181 |
| 149043 | macOS Sequoia 15.8 | 0 | 190 |
| **合计** | — | **0** | **1,430** |

全镜像分母因此从 **7,620 段 / 442 个段落名** 更正为 **9,045 段 / 470 个段落名**（258 个 HTML 文件排除非安全内容页 122085 ⇒ 257 份）。

这次漏采对本篇造成的具体 distortion，逐条列出：

| 项目 | 初稿 | 容错重测 | 差 |
|---|---:|---:|---:|
| 配件族核心段落 | 62 | **71** | +9 |
| `MobileAccessoryUpdater` | 10 | **14** | +4（CVE-2026-86924 的 4 段） |
| `Baseband` | 3 | **8** | +5（86879、86885、86963×2 + 1 段纯致谢） |
| `DiskArbitration` | 26 | **28** | +2 |
| 存在性计数组 | 34 | **36** | +2 |
| 三组合计 | 140 | **151** | +11 |
| 核心族 `improved input validation` | 0 | **8** | 一条负结果被推翻 |
| 核心族 `malicious accessory` | 8 | **12** | +4 |
| 核心族 `memory corruption` | 8 | **12** | +4 |
| 核心族 `remote attacker` | 2 | **3** | +1 |
| `Terminal` 段落构成 | 3 段全纯致谢 | **6 段 / 5 页，5 纯致谢 + 1 条 CVE-2026-28937** | 一条全称句被推翻 |
| 相邻族 | 44 | **44** | 0（这些段落全部登记在旧格式页面上） |

四条危险性，按严重程度排列：

1. **漏采不报错。** 严格正则在 10 页上命中 0，与"这 10 页本来就没有段落"在脚本输出里完全不可区分。任何一次为 0 的计数都必须先回答"分母是否包含全部标记代次"。
2. **它专挑最新页面失效。** 缺的恰好是 iOS 27 / macOS 27 与 26.7 / 15.8 这批页，也就是说漏采系统性抹掉了语料里时间最近的一层。本篇初稿把配件族读成"停在 2026 年 7 月的 CVE-2026-43807"，正是因为 2026 年 10 月新增的四条被整批看不见。
3. **它会把负结果变成假象。** 本篇初稿最强的两条方法结论——"输入校验不属于配件族"与"Terminal 段落全部是纯致谢"——都是 0 型结论，都来自这 10 页看不见的前提。**低估使风险判断偏乐观**，这与 #106 第 6.2 节的教训同向。
4. **它跨篇传播。** #100–#103 与本篇初稿共用同一解析器与同一分母 7,620，其中 #100、#101、#102、#103 已经按该口径发出且无法撤回。本篇在第 16 章与边界声明里登记这一事实：**已发布各篇的 T2 计数一律应读作"旧解析器口径下的下界"，不是本期镜像的全量**。

由此固化的三条做法（附表 D 方法 1、7、8）：

1. 取块正则必须写成 `<h3 class="gb-header[^"]*"[^>]*>`，字段正则必须写成 `<p class="(?:gb-paragraph|paragraph)[^"]*"[^>]*>`；
2. 每次换语料或隔一段时间重跑，**先单独对 149034 跑一次**：命中为 0 或远小于 209 段，说明解析器不合格，本轮全部数字作废；
3. 每个非零计数都必须能落到具名 CVE 或具名页号；每个 0 计数都必须额外回答一次"这 10 页被解析器看见了吗"。

# 15 三条 CVE 深读

本章把第 7、8、10 章分散出现的三条合成三个完整叙述。每条都写满：页号、段落名、机型清单差异、同页并列条目、致谢字段原文。

## 15.1 CVE-2025-24200：`Accessibility` 段落下的物理攻击

**Impact 原句（四页逐字相同）：**

> A physical attack may disable USB Restricted Mode on a locked device. Apple is aware of a report that this issue may have been exploited in an extremely sophisticated attack against specific targeted individuals.

**Description 原句：** An authorization issue was addressed with improved state management.
**致谢原文：** Bill Marczak of The Citizen Lab at The University of Toronto's Munk School（HTML 中为 `&rsquo;` 实体，须先解实体再检索）

**四页的 Available for 逐页不同**——这是本篇要求按页号陈述的直接原因：

| 页号 | 产品与版本 | Available for 摘要 |
|---|---|---|
| 122345 | iOS 15.8.4 and iPadOS 15.8.4 | iPhone 6s（全系）、iPhone 7（全系）、iPhone SE（第 1 代）、iPad Air 2、iPad mini 4、iPod touch（第 7 代） |
| 122346 | iOS 16.7.11 and iPadOS 16.7.11 | iPhone 8、iPhone 8 Plus、iPhone X、iPad 5th generation、iPad Pro 9.7-inch、iPad Pro 12.9-inch 1st generation |
| 122173 | iPadOS 17.7.5 | iPad Pro 12.9-inch 2nd generation、iPad Pro 10.5-inch、iPad 6th generation |
| 122174 | iOS 18.3.1 and iPadOS 18.3.1 | iPhone XS and later、iPad Pro 13-inch、iPad Pro 12.9-inch 3rd and later、iPad Pro 11-inch 1st and later、iPad Air 3rd and later、iPad 7th and later、iPad mini 5th and later |

**四页共同覆盖**：这 **4 页**是 2026-01 前后 Apple 为遗留分支与主线同批发布的一轮补丁；每一页除本条外都另含**至少一条同类在野声明**：

- 四页均含 `Messages` 条目：A logic issue existed when processing a maliciously crafted photo or video shared via an iCloud Link.（同类在野声明句式）
- 122345 与 122346 另含 `WebKit` 条目，Impact 带全语料唯一句式：**This is a supplementary fix for an attack that was blocked in iOS 17.2.**

**"段落名 ≠ 实现层"在这里的准确表述**：Apple 把这条缺陷登记在 `Accessibility` 段落名之下。本篇能说的只有"登记在此"。**不能**说：缺陷在辅助功能框架的代码里；USB 逻辑经由辅助功能；用辅助功能可禁用受限模式。**也不能**反向说"段落名标错了"——那是一个没有文本依据的判断。

**为什么它仍属于本篇**：Impact 句里唯一被点名的机制是 **USB Restricted Mode**，唯一被点明的前置条件是 **a locked device**、唯一被点明的攻击位置是 **a physical attack**。这三项全部对应第 4 章门二的手册层描述。段落名是什么，不影响这三项。

## 15.2 CVE-2025-43262：`Trusted Device` 段落下的启动窗口

已述于第 7.2 节。补充两点：

1. **Impact 是一个"覆盖范围"陈述而不是"被关闭"陈述**：`may not be applied to accessories connected during boot`。它与第 5.4 节 Intel Mac 的"default-deny、通电即开始阻断"处在同一个时序区间（启动早期），但 T1 说的是 IOMMU/DMA、T2 说的是 USB Restricted Mode，**两者在两层文本里互不引用**。本篇不把它们合并成"启动阶段的设备信任问题"这一叙事，只在第 17 章 G5 记录这一区间同时出现在两层。
2. **机型清单跨 Intel 与 Apple silicon**：第 7.2 节列出的八项里，MacBook Pro 16-inch 2019、MacBook Pro 13-inch 2020（Four Thunderbolt 3 ports）、Mac Pro 2019、iMac 2020 是 Intel 世代机型，其余是 Apple silicon。所以这道启动窗口的覆盖范围与 T1 第 5.2/5.3 节的两种不同实现（IOMMU per agent vs VT-d）**都**有关联可能，但 T3 缺失使本篇无法判定（G1）。

## 15.3 CVE-2026-43807：`MobileAccessoryUpdater` 段落下的"恶意配件"

**Impact 原句（八页逐字相同）：** A malicious accessory may be able to cause unexpected app termination.
**Description：** A buffer overflow was addressed with improved bounds checking.
**致谢：** Tristan Madani (@TristanInSec) from Talence Security｜**条目备注：Entry added July 27, 2026**

八页：127594（iOS/iPadOS 26.x 分支）、127595（macOS Tahoe）、128068（Apple Watch Series 6 and later）、128069（Apple TV HD and Apple TV 4K all models）、128070（Apple Vision Pro all models）、128071（macOS Sequoia）、128072（macOS Sonoma）、148287（iPhone XS/XS Max/XR、iPad 7th generation 遗留分支）。

三条：

1. 这是**全 257 份镜像里唯一一处把 "malicious accessory" 写进 Impact 主语位置的措辞**。第 14.1 节的 8 段命中就是它。
2. **后果等级不高**：Impact 只说 unexpected app termination，未说访问数据、未说提权。但它是**唯一一条以配件（而不是以设备上运行的 App）为攻击者位置**的条目——这一点对第 16 章事实 1 很关键：**措辞层有能力描述"来自配件的攻击者"，但它只在一条条目上这样做了，而且是在一个名字里就带着 Accessory 的段落（`MobileAccessoryUpdater`）上。**
3. **覆盖六条产品线加一条遗留分支**（iPhone/iPad 主线、遗留 iPhone XS 分支、macOS 三代、watchOS、tvOS、visionOS）。按第 0.2 节规则，本篇不说"iOS 修了这条"，而说"这条出现在 127594 与 148287 两个页号上，分别是 iPhone XS 及以后机型与遗留分支"。

## 15.4 一句必须写下的禁止

`122403` 上两条 MFi Program 条目（CVE-2025-24132、2025-30422）**都没有任何在野声明**。第 15.1 节的在野声明不涉及 CarPlay 插件或 AirPlay SDK。⇒ 本篇明确禁止："CarPlay 通信插件存在被在野利用的漏洞"这类句子在 Apple 文本里没有支撑。

# 16 七条结构性事实

这一章是全篇可迁移的部分——它们不只适用于配件通道。

**事实 1：同一道防线可以完全不在同一套词汇里。** T1 叫「安全激活数据连接」，T2 叫 "USB Restricted Mode"，两层互不引用。跨层核查必须先做人工映射表，否则统计与合规问答都会落空。这条与 #104 的"AWDL 从不被手册层点名"同型。

**事实 2：段落名的分布与缺陷方向不成比例。** 44 个相邻段落里 7 段是纯致谢（`MobileLockdown` 4 段中占 3、`srd_tools` 2 段全是、`DriverKit` 8 段中 1 段、`CoreUtils` 16 段中 1 段）。**一个方向的公开协作量可以远大于该方向的公开 CVE 数。** 只统计 CVE 会低估 Apple 从配件/锁定态方向收到的报告量。

**事实 3：任何未回原文重测的既有笔记都不能进正文。** 第 13.3 节记录的 `122085` 是一次实际的自我纠正：该页在镜像里根本不含安全内容段落。本篇因此把"重测后再写"作为本篇写作流程的一条硬约束，写进附表 D 方法 7。

**事实 4：手册层最厚的主题与措辞层最厚的主题不是同一个。** T1 最厚的是门二（一条页里给了三个时间常数 + 五个接口 + 两条失效联动）；T2 在该主题上只有 5 段、2 个 CVE。反之，T1 对 `IOHIDFamily` 承载的输入子系统几乎没有机制页，T2 却有 27 段。**两层的信息量在同一主题上经常反向。**

**事实 5：Apple 倾向给出"门开多久"或"信任存在哪"之一，很少两者都给。** 第 11.2 节第 1 点。本篇的 18 行合图里，同时给出存放位置与时限的行数为 **0**；只给时限的是门二（3 个数字），只给存放位置的是 #104 的连续互通诸通道。**这条写成观察，不写成规律。**

**事实 6：分族负结果不能合并。** #103 与 #104 报的 `actively exploited = 0` 与 `sandbox = 0` 在配件族核心 71 段内同样成立；但本篇第 9.4 节给出反例（`Power Services` 8 段全写 sandbox escape），第 14.4 节给出 `Apple is aware of a report` 的 93 段。**把两篇的负结果外推成"物理接口方向 Apple 从不承认在野利用"是明确的错误。**

**事实 7：任何"全语料 0 次"的结论，先要证明解析器看见了全部页面。** 本篇初稿的 `improved input validation = 0`、`Terminal 段落全部是纯致谢`、`核心族 62 段` 三处，全部出自同一个只识别旧版 HTML 标记的取块正则；该正则在 149034–149043 这 10 个最新页面上命中 **0 段且不报错**，一次静默漏采 1,430 段（第 14.6 节）。这条事实的可迁移部分是**方向性的**：漏采只会把非 0 压成 0，永远不会把 0 抬成非 0——所以本篇重测后**仍然为 0** 的行（`crafted`、`USB`、`Lightning`、`MFi`、`Thunderbolt` 字段级、`out-of-bounds read`、`actively exploited`）比上修的行更可信，而任何**由该解析器得出的 0** 在被引用前必须重跑一次。**这一条是本篇写作流程里唯一由工具缺陷而不是由 Apple 文本产生的事实，因此它属于方法层，不属于机制层；放在本章是因为它对任何用同一镜像的人都有用。**

**方法论附注（不计入七条）**：本篇全部计数可用附表 D 的九条命令重算。中英混检必须先套附表 A 的译名表，否则 `雷雳`、`闪电` 会被漏计（第 12.2 节）；重算时**必须先执行附表 D 方法 1 的容错切块**，否则最新 10 页会整批消失（第 14.6 节）。
# 17 缺口登记：本篇不能回答的十五件事

每条给出：现有文本到哪一步、缺什么、为什么不能用推断补。

| # | 缺口 | 现有文本到哪一步 | 为什么不能补 |
|---|---|---|---|
| **G1** | **T3 源码层完全不可用**：本地 XNU 镜像为空目录，未取回任何符号 | 只能写"顾问页把它归入某段落名之下" | 进程归属、调用关系、内核/用户态划分全部无依据；本篇所有实现层表述因此降级 |
| **G2** | 认证 IC 的型号、厂商、密钥注入方式、固件更新通道、吊销机制 | T1 只写"完全由定制集成电路 (IC) 来处理，而且对于配件本身是透明的"、"由 Apple 提供给许可配件生产企业"、"1024 位 RSA 密钥" | 手册层在四项上全部沉默；任何型号或流程描述都是外部信息 |
| **G3** | 「安全激活数据连接」（T1）与 "USB Restricted Mode"（T2）是否同一套代码 | 两层用词不同名，且**互相不引用**；T1 的机制页只有 Mac 侧 DMA，iOS 侧只有行为描述页 | 建立映射只能靠语义近似，不能作为技术事实 |
| **G4** | CVE-2025-24200 为何登记在 `Accessibility` 段落名下 | 只有段落名 + Impact + Description + 致谢四件文本 | T3 缺失；且段落名的语义边界 Apple 从未公开定义 |
| **G5** | CVE-2025-43262 修复前"启动时连接的配件"具体暴露面 | 只有 Impact 一句 + permissions issue 的修复类别 | Description 是模板化句式（第 14.5 节），不能反推修复对象 |
| **G6** | 122371 `MobileLockdown` 条目的 Available for 全为 iPad、无 iPhone，是机型限制还是清单差异 | 只有该页清单本身 | 无法判定；本篇按页陈述（第 8.2 节） |
| **G7** | MFi Program 的准入、审计、证书签发与撤销流程 | T2 只有 `Available for: MFi Program` 这个取值；T1 只有"MFi 许可计划"一名 | 属 Apple 与厂商之间的协议条款，非公开文本；不得由致谢对象来自汽车厂商（Renault）这一事实推断范围 |
| **G8** | `122403` 三条 SDK/插件版本与 iOS/macOS 主系统补丁的对应关系 | 该页以 AirPlay audio SDK 2.7.1 / AirPlay video SDK 3.6.0.126 / CarPlay Communication Plug-in R18.1 命名，无 iOS 版本号 | 页与页之间没有跨引；写"某个 iOS 版本修了这条"没有依据 |
| **G9** | 软件认证（T1 HomeKit 页："不搭载 MFi 芯片的配件可内建对 iOS 11.3 或更高版本中软件认证的支持"）与 IC 认证的安全等价性 | 手册层只给这一句 | 无对照文本；不得写"软件认证等价于/弱于硬件认证" |
| **G10** | "校准受限"（calibration is restricted）到底限制哪些功能 | T1 只有该词，同页无展开；"校准"全 179 页仅 4 次 / 3 页 | 手册层不定义；外部维修文档不属本篇任何一层 |
| **G11** | 哪些辅助设备会**自动**启用"始终打开数据连接" | T1 只有一句括号：setting up some assistive devices does this automatically | 手册层不给清单；而这条恰是第 4.6 节识别出的唯一"未经用户明确同意改变防线状态"的路径 |
| **G12** | 既有笔记"Terminal.app 与 USB Restricted Mode"不可核实 | 122085 实测不含安全内容段落；`Terminal` 段落 6 段 / 5 页里 5 段为纯致谢、第 6 段是 CVE-2026-28937（敏感数据访问），没有一段提到受限模式 | 见第 13.3 节；本篇不引该页 |
| **G13** | 镜像非全集：257 份页号范围 118479–149229，其间存在缺号 | 所有计数口径为"本期 257 份镜像" | 任何全称结论（"Apple 从未……"）在本篇一律禁止 |
| **G14** | "in radio range"（无线电距离范围内）指哪一种无线电、距离量级几何 | T2 只在 CVE-2026-86885 的 Impact 一句里用该词，全语料 9 段 / 8 页（`Bluetooth` 8 + `Baseband` 1） | 手册层无对应术语；T3 不可用；不得换算成米数或技术名 |
| **G15** | 顾问页 HTML 标记模板在两代之间变更的原因（改版？新模板？还是历史页未回填） | 只有两代 class 写法的事实本身：`gb-header` 与 `gb-header alignment horizontal-align-left`；受影响的是 149034–149043 这 10 页 | Apple 未述；本篇只能给出"取块正则必须容忍后缀"这条方法后果（第 14.6 节），不能解释为什么 |

一条总则：以上十五缺口中，G1、G13、G15 是**结构性**的（本期无法解决：G1 缺源码镜像、G13 缺页面全集、G15 缺的是一份工具层解释且它根本不在 Apple 安全文本里），其余十二条是**文本性**的（Apple 没写，或写的层级本篇取不到）。本篇对两类缺口使用同一句式：写成缺口，不写成推断。

# 18 运维与检测：本篇能支持的十八项动作

本章每一项都必须能回指到本篇已引过的原句。凡回指不了的，本章不写。

## 18.1 配置基线（10 项）

| # | 核查动作 | 依据 | 本章的边界 |
|---|---|---|---|
| 1 | 确认资产台账里"是否启用始终数据连接"为**已知项**而不是默认假设 | `sec5044aad1b`：用户可在设置中重新启用 | 手册层未说明该设置项在 MDM 上的对应键（G11） |
| 2 | 把"辅助设备自动启用"列为**变更事件**并留痕 | 同页括号句：setting up some assistive devices does this automatically | 无清单可核查，只能核查变更 |
| 3 | 核查 30 天记忆期内已授权配件集合是否被审计过 | `sec5044aad1b`：配件在上次连接后 30 天内被记住 | 手册层未给导出该名单的用户侧路径 |
| 4 | 把"1 小时窗口"写入物理响应流程的时限假设 | 同页：超过一小时需解锁 | 手册层未说窗口是否可配置 |
| 5 | 对"三天规则"的低频使用者单独验证（这类用户保护更强） | 同页：三天后锁定即禁新连接 | 手册层自陈这是针对不常使用者 |
| 6 | 把"需密码重新启用生物认证"状态纳入连接性监控 | 同页：该状态下数据连接不活跃 | 该状态的触发清单手册层未列全（G11） |
| 7 | 受监督设备上，把"激活锁默认不允许"作为策略基线核对 | `sec0f8dfd030` | 未述默认值可否被强制覆盖 |
| 8 | 31 字节绕过代码的托管方、随机性与每台唯一性作为审计项 | 同页：应为随机且唯一，由设备管理服务创建并发送至 Apple 服务器 | 手册层未述轮换周期（只说三种情形生成新代码） |
| 9 | 维修流程中把"来自他机的受支持部件 ⇒ 校准受限"作为部件来源核查依据 | `sec0f8dfd030` | 未述"受支持的部件"清单（G10） |
| 10 | 需要硬件断麦的 iPad（2020+）采购时把"符合 MFi 规格的保护壳"列为必备件 | `secbbd20b00b` | 未述壳的型号与认证查询路径（G7） |

## 18.2 状态与失效核查（4 项）

| # | 核查动作 | 依据 |
|---|---|---|
| 11 | 判定一次"未知配件接入"事件是否已触发**连带失效**（该连接上全部配件数据连接被取消激活） | `sec5044aad1b`：则会取消激活通过这些连接建立的所有配件数据连接，直到设备被重新解锁 |
| 12 | 在事故复盘中区分"锁定态接入"与"解锁态接入"两条完全不同的风险路径 | 第 8.2 节：122371 的条目前置条件是 unlocked device；第 7.1 节五段前置条件是 locked device |
| 13 | 若某功能在升级后消失，先查该条 CVE 的 Description 是否为 "removing the vulnerable code" | 第 10.3 节：该句式在配件族 5 段（CVE-2025-31187 的 3 页 + CVE-2025-43198 的 2 页，均为 `Dock`） |
| 14 | 检查 Mac 机型的启动阶段配件处理是否在已修版本上（CVE-2025-43262 按机型清单划分） | 第 7.2 节：Available for 是机型名清单，且同时含 Intel 与 Apple silicon 机型 |

## 18.3 补丁覆盖核验（4 项）

| # | 核查动作 | 依据 |
|---|---|---|
| 15 | **遗留分支与主线并行核对**：同一 CVE 在不同分支上是不同页号、不同机型清单 | CVE-2025-24200 = 122345（iOS 15.8.4）/ 122346（16.7.11）/ 122173（iPadOS 17.7.5）/ 122174（18.3.1）；CVE-2026-43807 = 127594–127595、128068–128072、148287 |
| 16 | 核对"该修复是否落到本机所在产品线"：同 CVE 常覆盖 5–8 条线，也常**不**覆盖某条 | 第 10.2 节：CVE-2025-43302 八页；CVE-2026-28943 七页；CVE-2024-23244 只有 Monterey 与 Sonoma 两页 |
| 17 | 引用条目时**点名两个日期字段之一**：页面发布日期 vs「Entry added/updated」 | 第 10.2 节备注列：24200 无条目备注；43262 是 Entry updated November 3, 2025；43807 是 Entry added July 27, 2026；27799 是 Entry added June 10, 2024；31203/24132/30422 是 Entry added April 28, 2025 |
| 18 | **确认覆盖核对所用的页面集合包含最新一批**：本族 22 个 CVE 里有 **4 个**只登记在最新 10 页（149034–149043）之内 | 第 10.2 节最后四行：CVE-2026-86924（149034/149035/149041/149042）、86885（149034）、86879（149034）、86963（149034/149041，Entry added October 19, 2026）。**如果核对脚本沿用初稿那套严格切块正则，这 4 条会整批消失**——它们不构成漏报，因为页面本身就是最新代标记（第 14.6 节） |

## 18.4 内核崩溃作为**正向**信号

第 5.3 节的手册层句给出一个反直觉但可执行的判读规则：

- **IOMMU 触发的内核崩溃**（`seca4960c2b5`："会引发内核崩溃"）是**这道门在工作**的证据；
- **措辞层所说的 unexpected system termination**（`DriverKit`/CVE-2026-43793、`IOHIDFamily`/CVE-2025-43302）是**缺陷后果**。

同为崩溃日志，运维判读完全相反。第 12.4 节指出这是两层语义相反的同词项，因此任何自动化分诊规则都必须把"崩溃由 IOMMU 越界检测触发"作为独立类别单列，而不是并入内核崩溃告警池。

## 18.5 跨层核查表模板

把第 11.1 节合图当模板用时，每一行必须填三格：**机制名（T1）/ 段落名（T2）/ 对应程度（强 / 弱 / 无）**。第 12.4 节给了本篇全部四次尝试，其中一次判为不可对齐。填不出对应程度的行，进 G3 类缺口，不进结论。

## 18.6 本章不能提供什么

- 不提供任何绕过、解锁、配件仿制、证书重放或测距/时序规避的操作步骤；
- 不提供针对他人设备的检测或取证流程；
- 不判定任何具体设备"当前是否安全"——本章所有项都以"能否核查某项公开文本所述状态"为限；
- 不提供 CVSS 或风险评分：措辞层无分值字段（第 13.1 节段落名清单即为该层的全部结构）；
- 不提供"MFi 认证配件是否比非认证配件安全"的结论：T1 只给了能力分级（第 2.4 节），T2 在 `MFi` 名下**没有任何 CVE**（第 13.2 节），二者都不支持该比较。

# 附表 A：术语对照（37 项，含中英日译名与首次出处）

| 术语 | 层 | 定义来源 | 本篇口径 |
|---|---|---|---|
| MFi 许可计划 | T1 | `sec70a4f377d` | Apple 的配件厂商许可计划；不作质量认证解读 |
| iAP / iPod 配件协议 | T1 | 同页 | 获许可厂商可使用的配件协议；版本未述 |
| 定制集成电路 (IC) / authentication IC | T1 | 同页 | 握手与签名的执行方；无型号 |
| 授权证明 | T1 | 同页（全 179 页仅 1 次） | 证书→验证→质询→签名应答四步的合称 |
| MFi-SAP / 安全关联协议 | T1 | 同页（各 1 次） | AirPlay 音频流与 CarPlay 视频流的会话协议 |
| STS / 端对端 | T1 | 同页；`sec6ab47ebfc`（#104） | secure tunnel session；两处都用该缩写 |
| 雷雳 / Thunderbolt | T1 中/英 | `sec70a4f377d`、`seca4960c2b5` | 中文 11/8 页；英文取样 7/4 页 |
| 闪电 / Lightning | T1 中/英 | `sec70a4f377d`、`seca7ff718d2` | 中文 4/4 页；英文取样 3/3 页；**T2 全语料 0** |
| USB-C | T1 + T2 | T1 `sec70a4f377d`；T2 122371 Impact | T2 全语料唯一 1 次 |
| 智能接点 / Smart Connector | T1 | `sec5044aad1b` | 五个接口之一；无其它页展开 |
| SDXC | T1 | 同页（限 macOS 13.3+） | 唯一被写明版本条件的接口 |
| 安全激活数据连接 | T1 | `sec5044aad1b` 页标题 | 本篇称"门二" |
| 1 小时 / 30 天 / 3 天 | T1 | 同页 | 三个独立常数，不合并解释 |
| 恶意充电器 / malicious chargers | T1 | 同页 | **T2 无对应句式（0 段）** |
| 配件生态无密码学识别方式 | T1 | 同页 | Apple 自认句式；非缺陷声明 |
| USB Restricted Mode | T2 | 122173/122174/122345/122346/125110 | 5 段 / 2 段落名；**T1 无此名（0 页）** |
| 物理攻击 / physical attack | T2 | 4 段，全 `Accessibility` | 与"物理访问 (physical access, 135 段)"不同措辞，不互换 |
| 已解锁设备 / unlocked device | T2 | 122371 | 与 locked device（101 段）互为反条件 |
| DMA / 直接内存访问 | T1 | `seca4960c2b5` | 11/9 页；**全篇仅 Mac** |
| IOMMU | T1 | 同页 | 每 DMA 代理一个；越界⇒内核崩溃 |
| VT-d / 默认拒绝 | T1 | 同页 | Intel Mac；通电即阻断 |
| Ring 3 受限环境 | T1 | 同页 | macOS 11 起 T2 的 UEFI 驱动 |
| 校准受限 / calibration is restricted | T1 | `sec0f8dfd030` | 激活锁的部件面后果；范围未述（G10） |
| 绕过代码 / bypass code | T1 | 同页 | 31 字节、随机、每台唯一、服务侧托管 |
| escrow / 托管 | T1 | 同页 | 设备管理服务持有绕过代码的关系 |
| `MobileLockdown` | T2 段落名 | 4 段（1 CVE + 3 纯致谢） | 不解释为 lockdown 框架的实现 |
| `MobileAccessoryUpdater` | T2 段落名 | 14 段（13 CVE + 1 纯致谢） | 不解释为某进程；本篇不判定其特权级（G1） |
| `CoreUtils` | T2 段落名 | 16 段 | 特权级不明（G1） |
| `Trusted Device` | T2 段落名 | 1 段 | 与 T1 的 30 天记忆弱对齐 |
| `srd_tools` | T2 段落名 | 2 段全纯致谢 | 与 `seca7ff718d2` 弱配对 |
| Security Research Device / SRD | T1 | `seca7ff718d2` | 特熔丝 iPhone；闪电线或 Qi 才可启动 |
| `Available for` | T2 字段 | 全条目 | 机型/产品清单，可为机型名或 `MFi Program` |
| Entry added / updated | T2 字段 | 见第 10.2 节 | 与"页面发布日期"是两个字段（第 18.3 节项 17） |
| 软件认证 | T1 | `sec3a881ccb1` | iOS 11.3+ 路径；与 IC 认证等价性未述（G9） |
| Connecting a malicious accessory | T2 | CVE-2026-86924 的 Impact 起首，4 段 / 4 页 | 全语料唯一以"连接"这一**动作**为主语的配件条目（第 10.5 节）；不读成"任意连接即有风险" |
| in radio range / 无线电距离范围内 | T2 | CVE-2026-86885 的 Impact | 全语料 9 段 / 8 页（`Bluetooth` 8 + `Baseband` 1）；不换算成技术名或米数（G14） |
| 标记代次 | 方法层 | 第 14.6 节 | 顾问页 `<h3>`/`<p>` 的 class 有 `gb-header` 与 `gb-header alignment horizontal-align-left` 两代；取块正则必须容忍后缀，否则最新 10 页整批漏采 |

# 附表 B：顾问页条目索引（本篇引用到的全部段落）

| 页号 | 产品与版本 | 段落名 | CVE | Impact（原句节选） | 条目备注 |
|---|---|---|---|---|---|
| 120884 | macOS Monterey | Dock | CVE-2024-23244 | escalate privilege after admin user login | — |
| 120895 | macOS Sonoma | Dock | CVE-2024-23244 | 同上 | — |
| 120898 | iOS 8/8+/X、iPad 5th 等 | IOHIDFamily | CVE-2024-27799 | log keystrokes… secure input mode | Entry added June 10, 2024 |
| 120899/120900/120903 | macOS Monterey/Ventura/Sonoma | IOHIDFamily | CVE-2024-27799 | 同上 | Entry added June 10, 2024 |
| 121238 | macOS Sequoia 15 | Dock | CVE-2024-44177 | access user-sensitive data | — |
| 121564 | macOS Sequoia 15.1 | **Game Controllers** | CVE-2024-44265 | input Game Controller events… locked device | — |
| 121568 | macOS Ventura 13.7.1 | Game Controllers | CVE-2024-44265 | 同上 | — |
| 121570 | macOS Sonoma 14.7.1 | Game Controllers | CVE-2024-44265 | 同上 | — |
| 121839 | macOS Sequoia | MobileAccessoryUpdater | CVE-2024-54536 | edit NVRAM variables | Entry added January 27, 2025 |
| 121839/121840/121842 | Sequoia/Sonoma/Ventura | Dock | CVE-2024-54547 | access protected user data | Entry added January 27, 2025 |
| 122173 | iPadOS 17.7.5 | **Accessibility** | CVE-2025-24200 | physical attack may disable USB Restricted Mode… in-the-wild | 无条目备注 |
| 122174 | iOS/iPadOS 18.3.1 | Accessibility | CVE-2025-24200 | 同上 | 无 |
| 122345 | iOS/iPadOS 15.8.4 | Accessibility + Messages + WebKit | 2025-24200 / — / — | 三条；WebKit 条含 supplementary fix | 无 |
| 122346 | iOS/iPadOS 16.7.11 | Accessibility + Messages + WebKit | 同上结构 | 同上 | 无 |
| 122371 | iOS/iPadOS 18.4 | RepairKit / Dock / **MobileLockdown** / Power Services / CoreUtils / srd_tools | 2025-24095、2025-30455、2025-31187、2025-24193、2025-24173、2025-31203 | 见正文 | srd_tools 为纯致谢 |
| 122372–122378 | iPadOS 17.7.6 / macOS 15.4 / 14.7.5 / 13.7.5 / watchOS 11.4 / tvOS 18.4 / visionOS 2.4 | Power Services、CoreUtils | CVE-2025-24173、CVE-2025-31203 | sandbox escape；local network DoS | 31203 Entry added April 28, 2025 |
| 122403 | AirPlay audio SDK 2.7.1 / AirPlay video SDK 3.6.0.126 / CarPlay Communication Plug-in R18.1 | **SDK**（Available for: MFi Program） | CVE-2025-24132、CVE-2025-30422 | unexpected app termination（两条） | 无在野声明 |
| 122404 | iOS 18.5 / iPadOS 17.7.7 | Baseband / MobileLockdown | CVE-2025-31214 / 纯致谢 | intercept network traffic / — | MobileLockdown 致谢 iVerify |
| 124147 | — | **Activation Lock** | （无 CVE） | 纯致谢：salemdomain | — |
| 124149/124150 | macOS Sequoia 15.6 / Sonoma 14.7.7 | Dock、CoreUtils | CVE-2025-43198 / 纯致谢 | removing the vulnerable code / — | — |
| 125108–125116 | iOS/iPadOS/macOS/tvOS/visionOS/watchOS 八页 | IOHIDFamily | CVE-2025-43302 | unexpected system termination（out-of-bounds write） | — |
| **125110** | macOS Tahoe 26 | **Trusted Device** | CVE-2025-43262 | USB Restricted Mode may not be applied… during boot | Entry updated November 3, 2025 |
| 126792–126799 | 八条产品线 | CoreUtils | CVE-2026-28886 | **A user** in a privileged network position… DoS | — |
| 127110–127120 | 七至八页 | IOHIDFamily | CVE-2026-28943 / 2026-28992 | determine kernel memory layout / app termination | 28992 Entry updated August 25, 2026 |
| 127594/127595 | iOS 26.x / macOS Tahoe | MobileAccessoryUpdater | CVE-2026-43807 | **A malicious accessory**… app termination | Entry added July 27, 2026 |
| 128066–128072 | iOS 26.6 / macOS 26.6 / 15.7.8 / 14.8.8 等 | DriverKit、MobileAccessoryUpdater | CVE-2026-43753、2026-43793、2026-43807 | 见第 9.2、10.2 节 | — |
| 148287 | iPhone XS/XS Max/XR、iPad 7th | MobileAccessoryUpdater | CVE-2026-43807 | 同上 | 遗留分支 |
| **149034** | iOS 27 与 iPadOS 27 | **Baseband** | CVE-2026-86885 | **An attacker in radio range**… unexpected system termination | 无条目备注 |
| 149034 | iOS 27 与 iPadOS 27 | Baseband | CVE-2026-86879 | A **remote attacker**… denial-of-service | 无 |
| 149034 | iOS 27 与 iPadOS 27 | Baseband | CVE-2026-86963 | privileged network position… denial-of-service | **Entry added October 19, 2026** |
| 149034 | iOS 27 与 iPadOS 27 | Baseband | （无 CVE） | 纯致谢：SyNSec Lab at Penn State，一行八人 | — |
| 149034 | iOS 27 与 iPadOS 27 | **MobileAccessoryUpdater** | CVE-2026-86924 | **Connecting a malicious accessory**… unexpected system termination | 无 |
| 149034 | iOS 27 与 iPadOS 27 | Terminal | （无 CVE） | 纯致谢（第 13.3 节） | — |
| **149035** | macOS Golden Gate 27 | MobileAccessoryUpdater | CVE-2026-86924 | 同上 | 无 |
| 149035 | macOS Golden Gate 27 | **Terminal** | CVE-2026-28937 | An app may be able to **access sensitive user data**；improved state management | 无 |
| 149035 | macOS Golden Gate 27 | Terminal | （无 CVE） | 纯致谢（该页 Terminal 段落共 2 段，1 带 CVE + 1 纯致谢） | — |
| **149041** | iOS 26.7 与 iPadOS 26.7 | MobileAccessoryUpdater、Baseband | CVE-2026-86924、CVE-2026-86963 | 见第 10.2 节 | 无 |
| **149042** | macOS Tahoe 26.7 | MobileAccessoryUpdater | CVE-2026-86924 | 同上 | 无 |

最后八行全部落在 149034–149043 这组**最新代 HTML 标记**页面上，本篇初稿的解析器在这组页面上命中 0 段，因此这八行（含 4 个新 CVE 与 2 段新纯致谢）在初稿附表里不存在（第 14.6 节）。

# 附表 C：手册层页清单（本篇实际引用，含发布日期）

| `sec<hash>` | 中文页标题 | 中文页发布日期 | 本篇用途 | 英文取样 |
|---|---|---|---|---|
| `sec70a4f377d` | 为 Apple 设备和服务验证配件 | 2024-05-07 | 第 2、3 章主证据 | 有 |
| `sec5044aad1b` | 安全激活数据连接 | 2024-05-07 | 第 4 章主证据 | 有 |
| `seca4960c2b5` | Mac 电脑的直接内存访问保护 | 2022-01-05 | 第 5 章（仅 Mac） | 有 |
| `sec0f8dfd030` | 激活锁 | 2026-01-06 | 第 6 章部件与绕过代码 | 有 |
| `secbbd20b00b` | 硬件麦克风断联 | 2025-07-14 | 第 6.3 节 MFi 第三处 | 有 |
| `seca7ff718d2` | Apple 安全研究设备 | 2022-09-06 | 第 9.6 节 SRD 配对 | 有 |
| `seca48c92d43` | DriverKit | 2025-03-04 | 第 9.2 节同名配对 | 有 |
| `sec3a881ccb1` | HomeKit | 2021-09-23 | 第 17 章 G9 软件认证 | 有 |
| `sece6ced019e` | Apple Configurator | — | 第 1.4 节 USB 作管理通道 | — |
| `secf78dbe639` | 接力（#104） | 2021-02-18 | 第 12.3 节"加密和认证"对照 | 有 |
| `sec2261183f4` | 隔空投送（#104） | 2021-02-18 | 第 3.2 节 2048 位对照 | 有 |
| `sec6ab47ebfc` | 自动解锁（#104） | 2026-01-28 | 第 2.1 节相互认证 STS 对照 | 有 |
| `secadb5b6434` | 物理配对模型 | — | 第 11 章合图弱相关（2048 位 RSA、256 位 escrow keybag、30 天） | — |
| `sec025128f1b` | 服务安全性概览 | — | #104 归类事实，本篇仅交叉引用 | 有 |

英文取样页共 **27 页**（`_gpenb/`，正文已剥目录）。中英口径差异见第 12.5 节。

# 附表 D：普查方法（9 条，可重算）

1. **顾问页切块（容错版，本篇初稿在此处出错）**：以 `<h3 class="gb-header[^"]*"[^>]*>` 切段——**前缀不能带闭引号**，因为 149034–149043 这 10 页的 class 是 `gb-header alignment horizontal-align-left`；块尾用 `(?=<h3 class="gb-header|<div id="disclaimer"|<footer|$)`；块内按 `<p class="(?:gb-paragraph|paragraph)[^"]*"[^>]*>` 顺序取 Available for / Impact / Description / CVE+致谢 / 条目备注。**必须先解 HTML 实体**（`&rsquo;`、`&amp;`、`&agrave;`），否则含所有格的英文短语与 `Fontà` 一类拉丁名必漏（第 15.1 节致谢字段）。
2. **编码**：镜像文件按 UTF-8 解码。中文姓名条目（如 CVE-2025-31214 的秦若涵、崔志伟、崔宝江）未设编码层时输出为 mojibake。
3. **字段区分**：机型清单在 Available for 里，会把 `Thunderbolt` 计入 94 次。任何接口名词频都必须区分"出现在 Impact/Description"与"出现在机型清单"（第 12.4 节）。第 10.3 节与第 14.1 节两张表全部按字段级口径重测过：`improved bounds checking` 因此是 17 而不是 8，`Thunderbolt`、`sandbox`、`improved memory handling` 因此是 0。
4. **中文手册普查**：179 页全量，检索词需按 UTF-8 字符传入（脚本内建 `use utf8` + ARGV 解码两步，缺任一全返 0）。
5. **英文手册普查**：27 页取样，且**必须剥目录**——以 `Copyright and trademarks` 之后的部分为正文，否则词频虚高。
6. **逐格分解要求**：任何非零计数都必须能写成"某 CVE 的 N 页 + 某 CVE 的 M 页 = 总数，无剩余"。第 14.3 节五个措辞与第 10.3 节 68 段全部通过该检验；未通过的写入缺口。
7. **既有笔记重测**：任何来自笔记、记忆或二手转述的"某条目讲 X"，必须回 `_tmp/adv2/<id>.html` 原文重验；重验失败即写入 G12 类缺口并**不引用该页**（第 13.3 节）。
8. **每个 0 都要在两代标记上重测**：任何"全语料 0 次 / 段落 X 全部是纯致谢 / 某句式不在本族"的结论，在写入正文前必须额外回答一次"**这 10 页被解析器看见了吗**"。做法是把同一条查询分别用严格正则与容错正则跑一遍，两者段数差即为该结论的漏采风险；严格版在 149034–149043 上返回 0 属正常现象，不是"这些页没有内容"（第 14.6 节）。
9. **149034 自检法**：每次改动切块正则后，单独对 `_tmp/adv2/149034.html` 跑一次段落计数。**该页实测 209 段**；命中 0 或远小于 209 的解析器一律作废，不得用于本篇任何计数。同一组十页的实测段数为 209 / 314 / 118 / 128 / 165 / 13 / 9 / 103 / 181 / 190（149034→149043，合计 1,430），可一并作为回归基线。

# 附表 E：本篇允许写 / 不许写对照（28 行）

| 允许写（有文本） | 不许写（无文本） |
|---|---|
| 认证握手是证书→验证→质询→签名应答四步 | 握手是相互认证（T1 未如此表述该页） |
| 握手完全由 Apple 提供给获许可厂商的定制 IC 处理 | IC 的型号、厂商、防篡改能力 |
| 手册层给出认证 IC 的 RSA 密钥位长为 1024 | 1024 位因此可被分解 / 弱于 AirDrop 的 2048 |
| MFi-SAP 使用 AES-128-CTR；该句未提完整性 | MFi-SAP 不提供完整性保护 |
| 未认证配件降级为模拟音频 + 一小部分 UART 播放控件 | 未认证配件被完全拒绝 |
| 门二给 1 小时 / 30 天 / 3 天三个常数 | 这些常数可被配置或延长 |
| Apple 自陈"配件生态不提供连接前可靠的密码学识别方式" | 因此门一可被绕过 |
| 未知配件尝试会取消激活该连接上全部配件数据连接 | 该行为等同强制禁用 USB |
| 用户可在设置中重新启用"始终数据连接" | 具体设置路径的截图或步骤 |
| 部分辅助设备设置会自动启用该开关 | 是哪些设备（G11） |
| IOMMU 越界访问会引发内核崩溃（**仅 Mac**） | iPhone/iPad 有同一 IOMMU 叙述 |
| Intel Mac 的 IOMMU 通电即以默认拒绝运行 | 该时序覆盖 USB 数据连接判定 |
| macOS 11 起 T2 的 UEFI 驱动在受限 Ring 3 运行 | Ring 3 与 iOS 任何组件的关系 |
| 激活锁可覆盖 iPhone 部件；他机部件致校准受限 | 哪些功能被限制（G10） |
| 设备管理服务可托管 31 字节绕过代码 | 绕过代码的强度评估或泄露后果 |
| 2020+ iPad 硬件断麦需符合 MFi 规格的保护壳 | 该壳的认证查询或真伪判定方法 |
| 顾问页把 CVE-2025-24200 登记在 `Accessibility` 段落名下 | 该缺陷位于辅助功能框架 / USB 逻辑经过辅助功能 |
| 该条 Impact 明写"a physical attack"、"a locked device"、"USB Restricted Mode" | 攻击所用工具、厂商或具体手法 |
| Apple 在该条声明"aware of a report… extremely sophisticated attack" | 该声明对应哪起公开事件或哪个客户 |
| CVE-2025-43262 说启动时连接可能不受 USB Restricted Mode 保护 | 启动阶段的暴露面清单（G5） |
| CVE-2026-43807 是唯一以"a malicious accessory"为主语的 Impact，后果为应用终止 | 配件可造成数据泄露或提权 |
| `122403` 两条 MFi Program 条目均无在野声明 | CarPlay 插件 / AirPlay SDK 被在野利用 |
| MFi 在 257 份镜像里只出现在 1 页，作为 Available for 取值 | "Apple 从不使用 MFi 一词"（全称禁止） |
| 本期镜像 257 份、页号 118479–149229，其间有缺号 | 任何"全部顾问页"式全称结论 |
| 核心族 71 段内有 **8 段** "improved input validation"（`MobileAccessoryUpdater` 4 + `Baseband` 4） | 本篇初稿那句"配件族没有一条输入校验类修复"——它已由容错重测推翻（第 14.6 节） |
| 全语料 9,045 段 / 470 个段落名是**容错解析器在本期 257 份镜像上**的口径 | 把这两个数当作 Apple 安全响应页面的全集计数（G13） |
| CVE-2026-86924 的 Impact 主语是 "Connecting a malicious accessory"，后果写的是 unexpected system termination | 因此"插入任意配件即会致系统终止"，或该条与认证门一/门二有因果关系（条目未述触发条件） |
| `Terminal` 段落实测 6 段 / 5 页，其中 1 段带完整字段（CVE-2026-28937） | `Terminal` 与 USB 受限模式有关——全语料 `restricted mode` 的 5 段无一挂在 `Terminal` 下（第 13.3 节） |

---

**边界声明：** 本报告全部机制表述取自《Apple 平台安全保护》中文全量 179 页与英文取样 27 页的正文原句（附 `sec<hash>` 与页面发布日期），全部缺陷表述取自 Apple 安全响应顾问页本期 257 份镜像的 Impact、Description、致谢、条目备注与 Available for 字段原句（附页号与产品版本）。本报告不含绕过方法、不含利用代码、不含有针对性他人设备的操作步骤、不含配件仿制或证书重放的实施细节；第 18 章仅支持"能否核查某项公开文本所述状态"级别的运维动作。源码层（XNU 及用户态开源部件）本期不可用，故第 9、11 章全部实现层归属均降级为"顾问页将其归入某组件段落名之下"，段落名不是实现层证据。第三方机构名（Citizen Lab、GrapheneOS、Oligo Security、Trend Micro Zero Day Initiative、iVerify、Kandji、Trail of Bits、Renault、Cisco Talos、Talence Security、KAIST SysSec Lab、Google Threat Analysis Group、SyNSec Lab at Penn State）一律只作为顾问页致谢字段中出现的名字被引用，本报告不引用、不转述、不背书这些机构的任何研究结论；第 17 章 G1–G15 与第 13.3、14.6 节记录的自我纠正在先：本篇初稿因取块正则只识别旧版 HTML 标记，静默漏掉最新 10 页的 1,430 个段落，核心族分母、若干词频与两条"0 次"论点已在本版更正，更正清单见第 14.6 节。凡本报告未登记为已核实的表述，均应视为缺口而非结论。

Telegram: https://t.me/one00190
