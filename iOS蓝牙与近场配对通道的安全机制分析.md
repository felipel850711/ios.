@@TITLE@@iOS 蓝牙与近场配对通道的安全机制分析
@@SUBTITLE@@手册层 6 项蓝牙安全功能、物理配对模型的 2048 位 RSA 与 256 位保险库密钥、超宽带页整页只有 3 句话，以及顾问页 148 个近场族段落里「在野利用」「中间人」「窃听」三个词各出现 0 次（三层文本实测版）
@@INFO@@报告日期|2026 年 10 月 26 日
@@INFO@@分析对象|《Apple 平台安全保护》zh-CN 指南 179 页全量转储 + 英文侧 18 页取样（含蓝牙、物理配对模型、安全激活数据连接、NFC 与 SE 平台、超宽带、隔空投送、无线局域网密码共享、将密码发送给其他用户或 Apple 设备）；Apple 安全响应顾问页 257 份英文页面转储，其中蓝牙 / AirDrop / Handoff / Wi-Fi / 超宽带 / 热点等近场族段落 148 个，分布在 73 份页面上
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含针对他人设备的操作步骤、不含配对截获或中继实施细节
@@INFO@@分类|近场通信安全 / 配对与绑定机制 / 跨设备凭据通道 / 官方措辞与手册口径对照
@@INFO@@难度|进阶（需要先理解第 1 章的四层文本分层，否则会把手册层的防线名误读成漏洞结论）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

蓝牙在 iOS 安全讨论里是一个被过度使用的词。有人说"蓝牙是零点击攻击面"，有人说"BLE 配对可以被中间人拿下"，有人说"AirDrop 会暴露你的哈希"，还有人说"UWB 因为精确测距所以更难中继"。这四句话里，只有一句能够直接落到 Apple 自己的文本上，其余三句要么来自第三方研究，要么是对手册层措辞的过度延伸。

本篇不做立场判断，只做一件事：**把"近场配对通道"这件事在 Apple 公开文本里的三层证据全部摊开，并标出每一层到底说了什么、没说什么。**摊开之后的结果比任何单条 CVE 都更有用——因为它同时告诉你，哪些常见说法在 Apple 的官方措辞里根本没有对应句式。

本篇覆盖的通道共七类，全部以"设备与设备之间的短距离交互"为共同特征：

| 通道 | 无线介质 | 手册层的定位 | 本篇章节 |
|---|---|---|---|
| 蓝牙（经典 + BLE） | 2.4 GHz 跳频 / BLE 广播 | 独立安全模型页 | 第 2–6 章 |
| 物理配对模型（主机 ↔ 设备） | 雷电 / USB，之后可走 Wi-Fi | 归在"物理配对"页，不属蓝牙章 | 第 7 章 |
| 安全激活数据连接 | 有线接口 | 归在配件与接口页 | 第 8 章 |
| NFC 与 Secure Element | 13.56 MHz 近场 | 支付与证件页 | 第 9 章 |
| 超宽带（U1） | 脉冲无线电超宽带 | 一页，正文 3 句 | 第 10 章 |
| 隔空投送 AirDrop | BLE 探测 + 点对点 Wi-Fi 传输 | 服务安全章下的独立页 | 第 11 章 |
| 近场凭据下发（Wi-Fi 密码 / 密码 App / 热点） | BLE 广播 + 身份验证 | 三页，全部复用 AirDrop 式身份机制 | 第 12 章 |

后五类严格说不是"蓝牙"，但它们都**以 BLE 广播作为发现手段**，并且都以同一套 iCloud 身份哈希作为信任判据。把它们放在一起，才能看清 Apple 近场体系的真实结构：**BLE 负责"找到彼此"，iCloud 身份负责"确认是谁"，点对点 Wi-Fi 负责"真正传数据"。**这三段式中任何一段被误写成一个整体，都会得出错误结论。

## 0.1 本篇的实测数字

下表是本篇给出的每一个结论的原始分母。所有数字在 2026-10-26 重新测过，不引用任何二手统计。

| 数字 | 是什么 | 分母与口径 |
|---:|---|---|
| 6 | 《Apple 平台安全保护》蓝牙页列出的"独立安全功能"项数 | 页面 `sec82597d97e`，英文原句以 "includes the following distinct security features" 引出，逐项列出 |
| 2 | 手册层为 BLE 明确命名的隐私功能数 | 同页 BLE 小节："address randomization and cross-transport key derivation" |
| 2048 / 256 / 30 | 物理配对模型里的 RSA 位长 / 保险库密钥位长 / 配对记录过期天数 | 页面 `secadb5b6434`（发布日期 2024-12-19） |
| 3 | 超宽带页的正文句子数 | 页面 `sec1e6108efd` 全量正文（本篇第 10 章整页引用） |
| 0 | 手册层 179 页中出现"AWDL"或"Apple Wireless Direct Link"的页数 | zh 全量 179 页 + EN 取样 18 页均为 0 |
| 0 | 手册层 179 页中出现"Nearby Interaction / 邻近交互"的页数 | 同上，zh / EN 双侧 0 |
| 148 / 73 | 顾问页近场族段落数 / 含有至少一个近场族段落的页面数 | `_tmp/adv2/` 257 份英文顾问页转储，按组件标题切块 |
| **0** | 这 148 个段落里 "actively exploited"、"man-in-the-middle"、"eavesdrop"、"sandbox" 各自的命中次数 | 段落内精确词形检索，见第 13 章普查表 |
| 19 / 18 / 4 | 近场族里 "privileged network position" / "denial-of-service" / "kernel privileges" 的命中段落数 | 同上；19 = 蓝牙族 12 + Wi-Fi 族 7 |

最后一行的四个 0 与前一行的三个数字，共同构成本篇最重要的口径结论：**Apple 描述近场风险时用的是"处于特权网络位置的攻击者"这一句式，而不是"中间人""窃听""在野利用"这些行业通用词。**手册层讲防御能力，顾问页讲可达位置，这两套语言之间的落差，就是本篇后半部分要逐条拆开看的东西。

## 0.2 四层文本与本篇的使用规则

本系列的每一条陈述都归属于下面四层之一。这不是排版装饰，而是本篇唯一有效的错误防线——把 T1 的句子当成 T3 的实现、把 T2 的沉默当成 T4 的否认，是近场类文章最常见的两类硬伤。

| 层 | 具体文本 | 能支撑什么 | 不能支撑什么 |
|---|---|---|---|
| **T1 手册层** | 《Apple 平台安全保护》正文（zh 全量 179 页 / EN 取样 18 页） | 机制的存在、算法名与位长、功能边界、默认值、前置条件 | 任何缺陷、任何可达性、任何"可被攻破"的判断 |
| **T2 措辞层** | 安全响应顾问页的组件段落 + Impact + Description + 致谢 | 某个 CVE 被归到哪个组件、Apple 如何描述后果与修复类别、致谢对象、条目增改日期 | 缺陷类别、CVSS 分值、在野状态、实现层位置 |
| **T3 源码层** | XNU 与用户态开源部件 | 函数、类、调用关系 | **本期不可用**：本地源码镜像为空目录，未取回任何符号（见第 18 章缺口登记） |
| **T4 第三方层** | 厂商威胁报告、学术会议论文、CVE 编号发布方 | 研究结论、命名事件、评分 | Apple 官方是否认可——不能反推 |

三条硬规则，贯穿全篇：

1. **跨层引用必须显式标层。**写"手册层把防中间人列为安全简易配对的功能之一（T1）"，不写"Apple 承认蓝牙存在中间人风险"。
2. **T3 缺失时降级。**凡是"实现层在哪"的句子，本篇只能写成"顾问页把它归入某个段落名之下"，绝不写"这是内核里的某某子系统"。
3. **T4 只在具名时出现。**本篇凡出现研究人名、机构名，均来自 T2 致谢字段本身（那是 Apple 写下的），而不是来自第三方报告的结论；研究结论一律不写。

## 0.3 怎么读这篇

- 想弄清楚"配对和绑定到底有什么不一样、BLE 的隐私功能手册层给几项"：读第 2–6 章。
- 想知道"iPhone 用线接到电脑上，信任是怎么建立、什么时候失效的"：读第 7 章。
- 想弄清楚"AirDrop 用 BLE 干什么、用 Wi-Fi 干什么、身份是谁验的"：读第 11–12 章。
- 想知道"哪些关于近场的常见说法在 Apple 文本里根本没有对应"：读第 13–14 章的普查与第 18 章缺口登记。
- 只想取一张可以直接引用的条目表：附表 B（顾问页条目索引）与附表 C（手册层页清单）。

# 1 手册层的蓝牙安全模型：六项功能，不是一个整体

## 1.1 原文

手册层的蓝牙页（`sec82597d97e`，中文页发布日期 2021-02-18）开门第一句给出了体系的边界：

> There are two types of Bluetooth in Apple devices, Bluetooth Classic and Bluetooth Low Energy (BLE). The Bluetooth security model for both versions includes the following distinct security features:

英文侧随后逐项列出六项，中文页逐项对应。下表是六个术语在本页里的原文定义与中文译述，**定义全部为原文直引，不加解释**：

| 术语（EN） | Apple 英文原句（定义部分） | 中文页术语 | 中文页译述 |
|---|---|---|---|
| Pairing | The process for creating one or more shared secret keys | 配对 | 创建一个或多个共享密钥的过程 |
| Bonding | The act of storing the keys created during pairing for use in subsequent connections to form a trusted device pair | 绑定 | 储存在配对期间创建的密钥以在后续连接中用于形成可信设备对的行为 |
| Authentication | Verifying that the two devices have the same keys | 认证 | 验证两个设备具有相同的密钥 |
| Encryption | Message confidentiality | 加密 | 消息机密性 |
| Message integrity | Protection against message forgeries | 消息完整性 | 防止消息伪造的保护措施 |
| Secure Simple Pairing | Protection against passive eavesdropping and protection against man-in-the-middle attacks | 安全简易配对 | 防止被动窃听和中间人攻击的保护措施 |

## 1.2 从这六项里可以直接读出、以及读不出什么

**读得出的四条：**

1. **配对与绑定是两个动作，不是一个。**配对的产物是"一个或多个共享密钥"；绑定的产物是"把密钥存下来以便后续连接"，其结果被明确称为"可信设备对（trusted device pair）"。也就是说，手册层把"密钥协商"与"信任持久化"分成两级，本篇后面所有关于配对记录失效、重置的讨论都落在第二级。
2. **认证的定义被写成对称性检查，而不是身份证书链。**原文是"verifying that the two devices have the same keys"——验证双方**持有相同密钥**。这与很多人直觉的"认证 = 验证对方身份标识"不同，手册层的措辞是密钥一致性。
3. **消息完整性针对的是伪造，不是重放。**原文 "Protection against message forgeries"。重放防护在本页没有出现（它在接力页出现，见第 12.3 节的对照）。
4. **"防被动窃听"与"防中间人攻击"这两项，在手册层里只出现在 Secure Simple Pairing 的定义里**，而不是单独列为两项安全功能。这是一个容易被忽略的结构：Apple 把这两个防御能力**绑定在一个具体配对方法上**，而不是宣称蓝牙整体具备它们。

**读不出的三点（这三点是本篇第 13 章普查的直接动因）：**

1. **手册层没有说"防中间人"在任何条件下都成立。**Secure Simple Pairing 的防御能力依附于该配对方式本身；哪些配对模式属于 SSP、哪些不属于，需要看第 3 章的版本与支持矩阵。
2. **手册层没有把"被动窃听"与"主动窃听"做区分描述。**它只写了 passive eavesdropping 一个词形。
3. **手册层没有给出任何"配对可被绕过"的表述**，也**没有**给出任何在野风险的表述——这两件事在 T1 里都不存在，第一件在 T2 里存在（第 16.2 节 CVE-2024-44124），第二件在 T2 里全量为 0。

## 1.3 六项功能的实现分配：同一页的支持矩阵

手册层在同一页用一张矩阵把六项功能分配到具体的算法与执行位置。下表是逐格可引的内容（EN 与 zh 同页对照，**该矩阵在两种语言的页面上都存在列错位，见 1.4**）：

| 功能 | 经典蓝牙（BR/EDR） | BLE |
|---|---|---|
| 配对 | P-256 椭圆曲线 | FIPS 批准的算法：AES-CMAC 与 P-256 |
| 认证 | HMAC-SHA-256 与 AES-CTR | 同左（HMAC-SHA-256 与 AES-CTR） |
| 加密与消息完整性 | 在**控制器（Controller）**中执行 AES-CCM | 同左 |
| 防被动窃听 | 临时（Ephemeral）ECDHE | ECDHE |
| 防中间人攻击 | **两种用户辅助型数字方法**：数字比较（Numerical Comparison）或密钥接入（Passkey Entry） | 同左 |

矩阵里另有两行值得单独抄下来，因为它们是本篇少数能直接引用 Apple 原文、说明"配对需要人"的句子：

- 「Pairings require a user response, including all non-MITM pairing modes / **配对需要用户的响应，包括所有非 MITM 配对模式**」
- 加密与完整性一栏里的执行位置写的是 **Controller（控制器）**。

## 1.4 引用这张矩阵时的两条注意事项

**第一条：列错位。**该表格在英文页与中文页上都出现过单元格漂移——例如"iPhone 6 or later"这一格落在蓝牙 4.2 行的经典蓝牙列。引用时必须写成"该页表格对应单元格写的是……"，**不得**把它改写成因果句（"因为 iPhone 6 才支持蓝牙 4.2，所以……"）。这不是解读分歧，是原表版式问题，改写会造成事实错误。

**第二条：机型门槛与系统门槛是分开的两组。**同表可直引的两组门槛：

- 蓝牙 4.1 或更高：列 iMac（Late 2015）、MacBook Pro（Early 2015）；系统门槛为 iOS 9 或更高、iPadOS 13.1 或更高、macOS 10.12 或更高、tvOS 9、watchOS 2。
- 蓝牙 4.2 或更高：iPhone 6 或后续机型。

本篇不推断"某机型不支持某防御"，只照抄这两组门槛，并把"iPhone 6 是否列在 4.1 行"这一问题留作版式事实。

## 1.5 版本与特性：一句原文，一条边界

同一页还有一句关于 Secure Connections 的原文：

> Bluetooth version 4.1 added the Secure Connections feature to Bluetooth Classic (BR/EDR) physical transport.

这句话能支撑一个很具体的口径事实：**手册层把 Secure Connections 这个特性挂在了 4.1 的经典蓝牙物理传输上**，而不是把它写成 BLE 独有的东西。任何"BLE 才有 Secure Connections"的说法，在 T1 层面与这句原文不符。

# 2 BLE 的两项隐私功能：手册层只给了两项

## 2.1 原文

蓝牙页的 BLE 小节全文可直引，英文原句如下（三句）：

> To help secure user privacy, BLE includes the following two features: address randomization and cross-transport key derivation.
> The private address is generated using the device's identity resolving key exchanged during the pairing procedure.
> On devices with iOS 13 and iPadOS 13.1 or later have the ability to derive link keys across transports, a feature known as cross-transport key derivation. For example, a link key generated with BLE can be used to derive a Bluetooth Classic link key.

中文页逐句对应：

> 为帮助保护用户隐私，BLE 包括了两种功能：地址随机化和交叉传输密钥派生。
> 私有地址使用配对过程中所交换的设备身份解析密钥而生成。
> 在运行 iOS 13 和 iPadOS 13.1 或更高版本的设备上可以跨传输派生链接密钥，例如使用 BLE 生成的链接密钥可以派生出经典蓝牙链接密钥。

## 2.2 一个必须保留的原文瑕疵

第三句的英文原文 "On devices with iOS 13 and iPadOS 13.1 or later have the ability to…" 本身有语法瑕疵（"On devices with … have the ability" 主谓不搭）。这是 Apple 页面原文如此。

本篇的处理规则是：**引用时保留原样，并标注"原文如此"，不"顺手修正"。** 理由不是学术洁癖——如果把它改写成通顺的英文再引用，读者就无法核对页面，而这条句子恰好带着一个真实的门槛信息（iOS 13 / iPadOS 13.1 起），改动句子结构很容易连带改动这个门槛的归属。

## 2.3 这两项功能能支撑什么结论

**地址随机化。** 手册层给的信息是：私有地址由**身份解析密钥（identity resolving key, IRK）**生成，而这个 IRK 是**在配对过程中交换**的。这句话的结构含义比字面更重要——它把"隐私"与"配对"绑在了一起：**能解析某个随机地址的前提，是双方已经配过对。** 换言之，随机地址对已配对设备并不隐藏身份（它本来就是给这些设备认的），它隐藏的是对**未配对观察者**的可识别性。

**交叉传输密钥派生（CTKD）。** 手册层给的方向是单向可推的：BLE 生成的链接密钥**可以**用于派生经典蓝牙链接密钥。这条能力在 iOS 13 / iPadOS 13.1 起存在。

它的体系意义是：**两种蓝牙传输不再是两套互不相干的信任。** 用户在一处完成配对，另一处即可复用派生密钥；配合第 1 章"绑定 = 存储配对密钥形成可信设备对"的定义，跨传输派生实际上扩大了"一次绑定"的作用半径。这是本篇在讨论近场风险时唯一能从 T1 直接支撑的、关于"信任传递"的表述。

## 2.4 手册层没有给的第三项

行业讨论中最常被列为 BLE 隐私机制的第三项是**可解析私有地址的轮换周期**（resolvable private address rotation interval）。

本篇普查结果：手册层 179 页全量**没有**给出该轮换周期，也没有把它列为独立功能——它只写了"BLE 包括两种功能"这一句。**轮换周期属 T4（蓝牙核心规范与第三方研究口径），本篇不写具体数值。**

这是本篇第一条"手册层沉默"，后面还有若干条（第 5 章汇总）。这类沉默的价值在于：当一个论述依赖"Apple 建议每 N 分钟轮换一次"这种句子时，它并不来自 Apple 的用户安全手册，而写作时应说明它来自哪里。

# 3 配对方式的两种用户辅助手段：数字比较与密钥接入

## 3.1 手册层给出的防中间人手段只有两种，且都要人参与

从第 1.3 节的支持矩阵里，"防中间人攻击"一栏写明的是**两种用户辅助型数字方法**：

| 方法 | 手册层术语 | 结构特征（由术语本身可读） |
|---|---|---|
| 数字比较 | Numerical Comparison | 两台设备各自显示同一个数，由人比对是否一致 |
| 密钥接入 | Passkey Entry | 一个数在一台设备上显示，在另一台设备上输入 |

同页还有一句被单列在矩阵里的话，本篇认为它是整页最值得引用的一句：

> Pairings require a user response, including all non-MITM pairing modes.
> 配对需要用户的响应，包括所有非 MITM 配对模式。

**这句是手册层自己承认的对称表述**：它把"非 MITM 的配对模式"也一并归入"需要用户响应"。这句话对分析的意义是双向的——一方面，Apple 承认存在"非 MITM 配对模式"这一类；另一方面，它声明即使是这些模式也要人响应。**手册层没有列出这些模式的名称，也没有说明哪些模式在什么场景下被选用。**

## 3.2 与 Secure Connections 的关系

第 1.5 节那句原文（蓝牙 4.1 把 Secure Connections 加入经典蓝牙物理传输）与本页的算法分配合起来，可以得到一个自洽的 T1 描述：

- 配对的密钥协商用 **P-256 椭圆曲线**（经典）与 **AES-CMAC + P-256 的 FIPS 批准算法**（BLE）；
- 防被动窃听用 **（临时）ECDHE**；
- 认证用 **HMAC-SHA-256 + AES-CTR**；
- 加密与完整性用 **AES-CCM，执行位置写在控制器（Controller）**；
- 防中间人靠**人的比对动作**（数字比较 / 密钥接入）。

**这五条之间没有一条是"设备单方面可完成的"。** 除加密与完整性落在控制器之外，防中间人这一项的强度直接取决于用户是否真的执行了比对。这是本篇对"配对通道"的核心结构性判断，且**完全由 T1 支持**，不需要任何第三方结论。

## 3.3 加密执行位置写成"控制器"意味着什么

手册层把 AES-CCM 的执行位置标为 Controller。这个信息在本篇中的作用是**限定**而不是推论：

- 可以写：手册层把加密与消息完整性的执行位置标为"控制器"（T1）。
- 不可以写：所以密钥在协处理器固件里、所以主机侧无法审计、所以某类攻击只能打到控制器——这三句都需要 T3 或 T4，而本篇 T3 不可用、T4 未核。

同一页的"防被动窃听 = ECDHE"与"加密 = 控制器内 AES-CCM"是两个不同环节的分配，混写成一句（"密钥交换也在控制器完成"）是本类文章常见的越界，本篇避免。

# 4 术语口径：中英两侧的宽窄不一样

## 4.1 一次全量与一次取样的差别

本篇所有"手册层从不使用某词"的断言，分母有两套，**不能合并**：

| 口径 | 范围 | 用途 |
|---|---|---|
| 中文全量 | 179 页正文区（中文转储） | 允许下"手册层 179 页里 0 次 / N 次"的全量结论 |
| 英文取样 | 18 页正文（英文转储） | 只允许下"取样的 18 页中 0 次 / N 次"的局部结论 |

取样侧的取法是：`https://support.apple.com/guide/security-pdf/<sec-hash>/1/web/1` 直接返回英文正文；而带 `/en-us/` 的常规地址会 301 到不带 locale 的地址，并按访问地理由返回中文。这条取数通道在本篇之前一直没打通——过去只能引中文手册。

## 4.2 三个术语的双侧计数

| 词 | zh 命中页数（全量 179） | EN 命中页数（取样 18） | 口径结论 |
|---|---:|---:|---|
| 配对 / pairing | 33 | 11 | 手册层大量使用，可直引 |
| 绑定 / bonding | 20 | 1 | **英文侧 bonding 只在蓝牙页出现**：术语范围比中文窄，不能以对译推定 |
| 超宽带 / Ultra Wideband | 3 | 9 | 存在专页，但内容仅第 10 章那 3 句；英文侧计数高来自目录与交叉引用 |

**"绑定 / bonding"这一行的意义。** 中文 20 页 vs 英文 1 页——这不等于"中文手册多谈了 19 页绑定"。它更可能来自中文页面对"配对 / 绑定 / 关联"等词的译词分布，以及英文取样面本身就窄。本篇把它登记为**术语口径冲突**，处理方式只有一个：**要谈"绑定"这个概念，就只引蓝牙页那一处的英文定义，不去数其它页面。**

## 4.3 两个全量 0

| 词 | zh 全量 179 页 | EN 取样 18 页 | 后果 |
|---|---:|---:|---|
| AWDL / Apple Wireless Direct Link | 0 | 0 | 手册层从不使用这两个词，一律写作"Apple 开创的点对点 Wi-Fi 技术" |
| Nearby Interaction / 邻近交互 | 0 | 0 | 手册层无此条目 |

第一条的实际写法约束很具体：AirDrop 页与接力页的原文分别是 "Apple-created peer-to-peer Wi-Fi technology"。**引用手册层时只能这样写**；"AWDL" 这个词要出现，必须标为第三方或开发者文档口径，并说明它不出现在用户安全手册里。

第二条更严：Nearby Interaction 是开发者框架，手册层根本没有对应章节。**本篇不得把它写成"手册层已记载的近场机制"**，也不得用它来解释 UWB 的用途（见第 10 章）。

# 5 手册层的"没说什么"清单

把前面各章出现的沉默集中列在这里，供第 13 章之后的措辞层对照使用。每一条都是"在给定分母内检索为 0"，不是"Apple 否认"。

| 编号 | 沉默项 | 分母 | 本篇允许的最强写法 |
|---|---|---|---|
| S1 | Resolvable Private Address 轮换周期 | zh 179 页 | 手册层未给出轮换周期 |
| S2 | 非 MITM 配对模式的名称清单 | 蓝牙页 | 手册层承认存在该类模式，未列名称 |
| S3 | UWB 的信道加密、时间戳安全（STS）、密钥分发、测距欺骗防御 | 超宽带页全量 3 句 | 手册层对 UWB 安全机制没有任何表述 |
| S4 | AWDL 这一名词 | zh 179 + EN 18 | 手册层不使用该词 |
| S5 | Nearby Interaction 框架 | zh 179 + EN 18 | 手册层无此条目 |
| S6 | 蓝牙链路密钥是否存放于安全隔区 | 蓝牙页、物理配对页 | 手册层未说明该存放介质 |
| S7 | 近场通道的 CVSS 分值 | 顾问页近场族 148 段 | 顾问页一贯不给出分值；任何分值须标 NVD 或第三方 |
| S8 | 近场缺陷的在野状态 | 顾问页近场族 148 段 | 顾问页从未声明在野利用（第 13 章普查 0 次） |

S6 值得多说一句。第 7 章会引到的物理配对页，把主机与设备之间的密钥材料说成"交换并保存 2048 位 RSA 公钥""主机获得一个 256 位密钥，可解锁设备上储存的保险库密钥（escrow keybag）"。这些是**关于主机配对**的表述，不是关于蓝牙链路密钥的表述。**手册层没有一处把蓝牙配对产物与保险库密钥或安全隔区写成同一件事。** 把三者连成一句（"蓝牙密钥也在 SEP 里派生"）在本篇没有 T1 依据。

# 6 把七类通道合成一张图：BLE 找人、身份认谁、Wi-Fi 传物

前六章分别看的是单个通道。本篇在这里给出把它们放在一起之后的结构判断——这也是全篇后续章节反复回用的分析骨架。

## 6.1 三段式

把第 1、2 章的蓝牙页、第 7 章的物理配对页、以及第 10、11 章的 AirDrop 与凭据下发页放在一起读，Apple 近场体系的分工高度一致：

| 段 | 承担介质 | 手册层原文依据 | 承担的安全职责 |
|---|---|---|---|
| **发现 / 触发** | BLE 广播 | AirDrop 页："emits an AirDrop signal over BLE"；Wi-Fi 密码共享页："starts a Bluetooth Low Energy (BLE) advertisement indicating that it wants the Wi-Fi password" | 广播方**只暴露最少信息**（AirDrop 是短身份哈希，密码共享是"我要口令"这一意图） |
| **身份确认** | iCloud 服务 + 通讯录比对 | AirDrop 页：2048 位 RSA 身份、短哈希 / 长哈希两级、"exchanges their iCloud identity certificates" | 判定"这台设备是不是我认识的人" |
| **数据传输** | Apple 自研点对点 Wi-Fi + TLS | AirDrop 页："communicate directly between devices without using any internet connection or wireless access point (AP). This connection is encrypted with TLS" | 承载真实内容，与互联网、与接入点都无关 |

三段式里最容易被压扁的是第一段与第三段的差别：**很多人把 AirDrop 说成"蓝牙传文件"，而手册层明确是"蓝牙发信号、点对点 Wi-Fi 传数据"。** 这两段的安全属性完全不同（广播无连接、TLS 有会话），混在一起就会得出错误的攻击面判断。

## 6.2 物理配对与有线接口是同构的，不是另一件事

第 7 章的物理配对页写的是：线缆建立信任 → 保存双向 RSA 公钥 → 之后允许经 Wi-Fi 复用该会话。把它与 6.1 的三段式并排，结构完全一致：

| 段 | 无线近场 | 有线近场 |
|---|---|---|
| 建立 | BLE 广播 + 响应 | 雷电 / USB 物理连接 |
| 凭据 | iCloud 身份哈希 + 证书 | 双向 2048 位 RSA 公钥 + 256 位保险库密钥 |
| 复用 | 点对点 Wi-Fi + TLS | Wi-Fi 上的加密 SSL 会话 |
| 时限 | 手册层未给（S 类沉默） | **30 天未使用即过期**（T1 原文） |

最后一行的对比，是本章最想留下的一条事实：**Apple 在有线主机配对上明确写了有效期，在无线近场绑定上没有任何有效期表述。** 第 7.5 节会再强调这一点，避免把 30 天误推广到蓝牙。

## 6.3 "需要人"是贯穿三段的一致设计

三段式里，凡是**改变信任状态**的动作，手册层都要求人参与：配对要用户响应（第 3 章）；AirDrop 接收要"receiving user is asked to accept the incoming transfer"（第 10 章）；NFC 交易要 App 在前台且设备已解锁（第 9 章）；NFC 密码下发场景要 iPhone 解锁并靠近遥控器（第 11 章）；自动解锁类关系要逐条启用（第 11 章）。

本篇据此给出全篇的核心判断，并且它**只依赖 T1**：

> **Apple 近场体系的信任边界，不是密钥强度，而是"人的确认动作有没有真的发生"。** 密钥算法（P-256、AES-CCM、ECDHE、RSA-2048）都是给"确认之后"用的；确认本身由用户完成。

这句话同时是第 13 章措辞层普查的解释工具——顾问页近场族里绝大多数条目的主语是"an app"或"an attacker in a privileged network position"，而不是"绕过用户确认"。两类主语的差别，正好对应"防线是否被真的触发"。

# 7 物理配对模型：主机与设备之间的信任是怎么建立、又怎么失效的

## 7.1 这一页为什么属于"近场通道"

`secadb5b6434`（发布日期 2024-12-19）标题是"iPad 和 iPhone 的物理配对模型安全性"。它讲的不是蓝牙，但它讲的东西和蓝牙页共用同一套结构：**先用一种介质建立一次信任（这里是线缆），把密钥持久化，之后让另一介质（这里是 Wi-Fi）复用这份信任。** 手册层在这一页把这条逻辑写得很清楚：

> To use this encrypted session for all communication, the device requires connections from a host over Wi-Fi, so it must have been previously paired over a physical connection (Thunderbolt or USB).

## 7.2 建立过程：逐句可引

> iOS and iPadOS use a pairing model to control access to a device from a host computer. This establishes a trust relationship between the device and its attached host using a public key exchange.
> the host and device exchange and save 2048-bit RSA public keys. The host is then given a 256-bit key that can unlock an escrow keybag stored on the device.
> The exchanged keys are used to start an encrypted SSL session, which the device requires before it sends protected data to the host or starts a service…

拆成四点，每点都有原文对应：

1. **方向**：配对模型控制的是"主机对设备的访问"，不是"设备对主机"。原文主语始终是 device 对 host 的要求。
2. **算法与位长**：2048 位 RSA 公钥，双向交换并保存。
3. **主机拿到什么**：一个 **256 位密钥**，其作用是**解锁设备上储存的保险库密钥包（escrow keybag）**。手册层在这里第一次也是本篇范围内唯一一次给出 escrow keybag 这个对象。
4. **配对之后还有强制的一道**：设备要求**加密 SSL 会话**，然后才向主机发送受保护数据或启动服务。

## 7.3 失效与限制：三条可直引的硬边界

**第一条：30 天过期。**

> On devices with iOS 11 and iPadOS 13.1, or later, if a pairing record hasn't been used for more than 30 days, it expires.

这是一条与第 1 章"绑定"直接呼应的机制：**绑定形成的信任不是永久性的**，未被使用的配对记录会在 30 天后失效。本篇把它登记为"手册层给出的、近场信任持久化存在时限的唯一样本"。

**第二条：诊断服务的两种限制方式。**

> Certain diagnostic services, including com.apple.mobile.pcapd, are restricted to work only over USB. Additionally, the com.apple.file_relay service requires an Apple-signed configuration profile to be installed.

这两个服务名字符串是**手册层直接给出的**（T1），不需要退化为"据称"。它们的对照关系也很干净：一个靠**介质限制**（只允许 USB），一个靠**授权限制**（需要 Apple 签名的配置描述文件）。这是本篇能引用的、Apple 自己给出的"同一台设备上两类不同防线"的最小说明。

**第三条：重置入口。**

> A user can clear the list of trusted hosts with the Reset Network Settings or Reset Location & Privacy options.

手册层给出的是用户侧的清除路径，共两个入口。**这是第 19 章运维建议里唯一有 T1 依据的清除动作。**

## 7.4 一个不属于 iOS 但同页的例外：Apple TV 与 SRP

同一页还有一句：

> On devices with iOS 11 and iPadOS 13.1, or later, Apple TV can use the Secure Remote Password protocol to wirelessly establish a pairing relationship.

这句话在本篇的价值是**对照**：在同一份手册里，主机配对用的是 RSA 公钥交换 + 256 位保险库密钥；而 Apple TV 的**无线**配对用的是 SRP。手册层没有解释为什么两种场景选不同协议，也没有给 SRP 的参数。本篇因此只登记这个差异，不作解释。

## 7.5 引用本页时的两条禁忌

- **不要把 escrow keybag 写成"蓝牙配对密钥"**。本页通篇讲的是主机与设备之间，与蓝牙链路密钥无关（S6）。
- **不要把 30 天过期推广到蓝牙绑定**。原文的条件是 "pairing record hasn't been used for more than 30 days"，出现于物理配对页；**手册层的蓝牙页没有给出任何绑定的有效期。** 这是本篇最容易写错的一句，第 13 章的措辞对照会再确认一次。

# 8 安全激活数据连接：Apple 自己写出的威胁模型

## 8.1 这一页在本篇里的地位

`sec5044aad1b`（发布日期 2024-05-07）讲的不是无线近场，而是**物理近场**——设备与线缆配件之间的数据连接。它在本篇的地位却很高，因为**这是手册层极少数直接写出攻击者画像的地方**。

## 8.2 覆盖的接口

手册层在同页枚举的接口：**雷雳、USB、Lightning（闪电）、智能接点（smart connector）、SDXC（macOS 13.3 或更高）**。

## 8.3 四条规则（逐条为 T1 原文口径）

| # | 规则（中文页原述） | 触发条件 |
|---|---|---|
| 1 | 如果 iPad 或 iPhone 锁定，或配件的数据连接终止超过一小时，则解锁设备前不允许建立任何新的数据连接 | 锁定 **或** 断开超过 **1 小时** |
| 2 | 这些配件会在上次连接后的 30 天内被记住 | 记住期 **30 天** |
| 3 | 如果一个数据连接通过配件建立了三天以上，设备从锁定时刻起将不会再允许新的数据连接 | 累计连接超过 **3 天** + 之后进入锁定 |
| 4 | 用户可以选择在"设置"中重新启用始终打开数据连接（设置某些辅助设备时会自动启用） | 用户显式开启 |

**这三条时限（1 小时 / 30 天 / 3 天）是手册层给出的、关于物理近场信任的完整时限表。** 与第 7 章的"配对记录 30 天过期"合起来，本篇可以写一句有依据的话：

> 手册层对"持久化的近场信任"给了三个不同的时限：主机配对记录 30 天未使用即过期、已连接配件记住 30 天、数据连接建立超过 3 天后一旦锁定即不再允许新连接、断开超过 1 小时后需解锁才能新建连接。

## 8.4 两句可直引的设计理由

> This feature limits the attack surface from physically connected devices, such as malicious chargers…
> …the accessory ecosystem does not provide a reliable cryptographic way to identify an accessory before a data connection is established.

第二句尤其重要，它是手册层给出的**因果**：因为"在建立数据连接之前，配件生态没有可靠的密码学方式识别配件"，所以要靠"锁定时限 + 用户显式开启"这类策略性限制来补偿。

这两句在本篇的用法是：**它们是可以直接引用 Apple 措辞的威胁模型表述**，不需要像第 6 章那样自造场景。反过来说，手册层的威胁模型也**只**写到 malicious chargers 这一层，没有列举具体攻击工具、没有给攻击成功条件，正文不得超出这个范围补写。

## 8.5 与"验证配件"页的关系

同属配件防线的还有 `sec70a4f377d`（"为 Apple 设备和服务验证配件"，发布日期 2024-05-07）。本篇把它登记为**相邻页**：它属于配件认证通道，是选题 C（USB 配件与 MFi 认证通道）的主材料，本篇不在此展开，只在附表 C 列出页号与发布日期。

# 9 NFC 与 Secure Element：近场的另一个方向

## 9.1 方向差别

前八章的近场都是"设备 ↔ 设备"。`secda20f3f41`（NFC 与 SE 平台安全性，发布日期 2024-12-19）讲的是"设备 ↔ 终端"。这个方向差别的实际后果是：**这里没有配对，只有交易；没有可信设备对，只有被评审过的 applet。**

## 9.2 逐条可直引（英文原句，均在同页）

| 项 | 原文要点 |
|---|---|
| SE 的定义与认证 | an industry-standard integrated circuit that runs the Java Card platform. Certified by both EMVCo and Common Criteria |
| 交易路径 | During transactions, the terminal communicates directly with the Secure Element through the near-field-communication (NFC) controller |
| "非接触"是由控制器判定的 | Only requests arriving from an in-field terminal are marked by the NFC controller as contactless transactions |
| 交易细节的边界 | contactless transaction details are contained to the local NFC field and aren't exposed to the Application Processor |
| SE 与 SEP 的关系 | can communicate securely using a shared secret generated at runtime |
| applet 准入 | go through a security review by an accredited third-party security evaluation laboratory |
| 发起条件 | To initiate a transaction, the iOS app must be in the foreground and the iPhone must be unlocked |
| 凭据清除 | 注销 iCloud 账户、移除密码、抹掉设备、申请删除 Apple Account 四种场景下全部 SE 凭据被安全擦除 |

## 9.3 三条本篇可以直接下的结论

1. **"Apple Pay 的近场交易细节不经过应用处理器"是 T1 可直引结论**，不需要退化为"据称"。原文是 "aren't exposed to the Application Processor"。
2. **"非接触"这个属性不是由远程服务器判定，而是由本地 NFC 控制器判定**——原文用 "Only requests arriving from an in-field terminal are marked … as contactless transactions"。这是一个**由硬件控制器做的物理邻近性断言**，是手册层里最接近"距离证明"的一句。
3. **SE 与 SEP 之间用的是"运行时生成的共享密钥"**。这句话只说明"存在一条安全通道且密钥每次运行时生成"，**没有**说明算法、没有说明密钥协商方式。本篇不补。

## 9.4 与蓝牙页的对照：两种"防邻近伪造"的手法

把手册层的两处放在一起看，会出现一个有意思的分工：

| 目标 | 蓝牙页的手法 | NFC 页的手法 |
|---|---|---|
| 隐藏身份 | 地址随机化（IRK 派生） | 交易细节不出本地场 |
| 确认对方 | 用户比对数字 / 输入密钥 | 控制器判定"在场终端" |
| 密钥归属 | 链路密钥（介质未说明，S6） | SE 内、Java Card 平台、经第三方实验室评审的 applet |

**结论：手册层对"近距离"给出的保护，一类靠不可识别（随机化），一类靠不可达（限定处理器与本地场）。** 这两类都不依赖"距离测量"。这与第 10 章 UWB 页把 UWB 归入随机化而不是加密，形成一条一致的设计取向。

# 10 超宽带（U1）：整页只有三句话，这是本篇最重要的负结果

## 10.1 整页引用

`sec1e6108efd`（发布日期 2021-02-18）是手册层里唯一专门谈 UWB 的页面，其正文在剥去导航与目录后只有三句。英文原句逐字如下：

> The new Apple-designed U1 chip uses Ultra Wideband technology for spatial awareness—allowing iPhone 11, iPhone 11 Pro, and iPhone 11 Pro Max or later iPhone models to precisely locate other U1-equipped Apple devices. Ultra Wideband technology uses the same technology to randomize data found in other supported Apple devices: MAC address randomization / Wi-Fi frame sequence number randomization

三句话提供的信息量：一颗芯片名（U1，Apple 设计）、一种用途描述（spatial awareness / 空间感知）、一个机型门槛（iPhone 11 / 11 Pro / 11 Pro Max 或后续 iPhone）、一处归类（把 UWB 归入**随机化**，与 MAC 地址随机化、Wi-Fi 帧序号随机化并提）。

## 10.2 三句话里没有的东西

这正是本篇最需要读者注意的一节。**手册层关于 UWB 没有任何下述表述：**

- 没有说 UWB 无线帧是否加密；
- 没有提"时间戳安全（STS）"这一概念，也没有提任何测距密钥的分发方式；
- 没有说 UWB 能抵抗距离欺骗或中继；
- 没有说 UWB 被哪些功能使用（没有列 AirDrop、没有列数字钥匙、没有列接力）；
- 没有给出信道、频段、功率等任何射频参数。

因此，凡是"UWB 因为精确测距所以更难被中继""UWB 用了加密时间戳序列所以抗重放"这类句子，**在 T1 层面没有依据**。它们属于 T4（无线规范侧与学术侧）。本篇的写法只有一种：

> 手册层说 U1 提供空间感知、可精确定位其它配备 U1 的 Apple 设备（T1 原文）；关于 UWB 抗中继与抗距离欺骗的论证，不来自 Apple 用户安全手册。

## 10.3 "把 UWB 归入随机化"这件事本身是可引用的口径

10.1 那句原文里一个容易被略过的结构：Apple 在讲 U1 空间感知之后，紧接着说 UWB "uses the same technology to randomize data found in other supported Apple devices"，然后列出 MAC 地址随机化与 Wi-Fi 帧序号随机化。

也就是说，**手册层对 UWB 的安全定位不是"加密"，而是"随机化"**——它被放在隐私类手段（减少可识别性）一侧，而不是机密性一侧。这与第 2 章 BLE 地址随机化形成一条一致的手法：手册层在讨论"近距离无线电"时，凡是涉及隐私的表述都落在**降低可识别性**上，而不是落在**防止被听懂**上。这条取向与第 9.4 节的结论一致。

## 10.4 车钥匙页的一条补充（同族、不同页）

`secf64471c16`（"iOS 中的车钥匙安全性"，发布日期 2026-01-28）在同一年更新，属近场凭据族。本篇只在附表 C 登记该页，正文不展开——它的主线是数字钥匙而非配对通道，且相关细节需要与 NFC / BLE / UWB 三条介质的使用条件一起读，超出本篇范围。

# 11 隔空投送（AirDrop）：三段式最完整的样本

## 11.1 介质与默认值

`sec2261183f4`（隔空投送安全性，发布日期 2021-02-18）。英文原句给出介质组合与"不走网络"这一关键限定：

> Apple devices that support AirDrop use Bluetooth Low Energy (BLE) and Apple-created peer-to-peer Wi-Fi technology to send files and information to nearby devices… The Wi-Fi radio is used to communicate directly between devices without using any internet connection or wireless access point (AP). This connection is encrypted with TLS.

同一页对可用性边界：

> AirDrop is set to share with **Contacts Only** by default. Users can also choose to use AirDrop to share with everyone, or turn off the feature entirely. Organizations can restrict the use of AirDrop for devices or apps being managed by using a device management service.

三条可读事实：默认是"仅限联系人"；用户可放开到"所有人"，也可整体关闭；**组织侧有设备管理这一条控制路径**。

## 11.2 身份：两级哈希 + 一套 RSA 身份

> When a user signs in to iCloud, a **2048-bit RSA identity** is stored on the device, and when the user turns on AirDrop, an **AirDrop short identity hash** is created based on the email addresses and phone numbers associated with the user's Apple Account.

注意这句里两个对象的层级不同：**RSA 身份是登录 iCloud 时产生的；短身份哈希是打开 AirDrop 时才产生的，其输入是邮箱地址与电话号码。** 手册层没有给出哈希算法（盐、迭代、域）——这是本篇的一条 T1 沉默。

## 11.3 工作流程：逐步照抄手册层

| 步 | 手册层原文（浓缩） | 结构含义 |
|---|---|---|
| 1 | the sending device emits an AirDrop signal over BLE that includes the user's AirDrop short identity hash | 广播只携带**短哈希**，不携带身份本身 |
| 2 | Other Apple devices that are awake, in close proximity, and have AirDrop turned on, detect the signal and respond using peer-to-peer Wi-Fi, so that the sending device can discover the identity of any responding devices | 响应发生在 **Wi-Fi** 侧，不在 BLE 侧 |
| 3 | In Contacts Only mode, the received AirDrop short identity hash is compared with hashes of people in the receiving device's Contacts app. If a match is found, the receiving device responds over peer-to-peer Wi-Fi with its identity information. **If there is no match, the device doesn't respond.** | 匹配判据是**接收方本地通讯录**；不匹配则静默 |
| 4 | In Everyone mode, the same overall process is used. However, the receiving device responds even if there is no match in the device's Contacts app | "所有人"只改变**是否响应**，不改变流程 |
| 5 | The sending device then initiates an AirDrop connection using peer-to-peer Wi-Fi, using this connection to send a **long identity hash** to the receiving device | 短哈希 → 长哈希，**两级由近及远** |
| 6 | the sending device initiates an encrypted (TLS) connection with the receiving device, which **exchanges their iCloud identity certificates**. The identity in the certificates is verified against each user's Contacts app | 证书交换发生在 TLS 之内；比对仍是**双向通讯录** |
| 7 | If the certificates are verified, the receiving user is asked to accept the incoming transfer from the identified user or device. If multiple recipients have been selected, this process is repeated for each destination | **最终仍要接收方点确认**（第 6.3 节的核心判断在此得到原文支持） |

## 11.4 未验证设备如何呈现：手册层给用户的唯一视觉区分

> Devices that aren't verified or authenticated are displayed in the sender's AirDrop share sheet with a **silhouette icon** and the device's name, as defined in Settings > General > About > Name. They're placed in the "Other People" section of the AirDrop share sheet.

这段在本篇里地位很高，因为它是**手册层唯一一处告诉用户"你可以自己看出对方未被验证"**的界面事实：剪影图标 + 用户自设设备名 + 被放到 "Other People" 分组。相对地，已验证设备显示的是通讯录里的名字与照片。

写作约束：这一段只能描述为"发送方分享面板里的显示方式"，**不得**据此推断"剪影设备就是攻击者"或"显示名字就是可信"。手册层的原话是 "aren't verified or authenticated"，它给出的是**分类**，不是**风险评级**。

## 11.5 从 AirDrop 页能得到的三条口径

1. **AirDrop 不是蓝牙文件传输。** BLE 只负责携带短哈希发信号；身份发现、证书交换、内容传输都在点对点 Wi-Fi + TLS 上。
2. **信任判据是"你的通讯录里有没有这个人"，而不是"这台设备持有合法证书"。** 证书被交换并验证，但**是否响应的第一道判据是本地哈希匹配**（步 3）。这一区分在讨论"被陌生人投送"时是关键。
3. **"所有人"模式改变的只是响应行为**（步 4），手册层没有说它改变加密或证书交换。任何"所有人模式会导致不验证身份"的说法都没有 T1 依据。

# 12 近场作为凭据通道：三套系统复用同一套身份识别

第 11 章的 AirDrop 流程不只用于投送文件。手册层在另外三处明确写了"与 AirDrop 类似"，本篇把这四处合起来读，得到近场体系最有价值的一条结构结论：**Apple 把一整套"BLE 广播 + 通讯录身份匹配 + 近场确认后下发"的机制复用成了通用凭据通道。**

## 12.1 无线局域网密码共享

`secd2015ef35`（发布日期 2024-12-19）整页可引，四段：

> Apple devices that support Wi-Fi password sharing use a **mechanism similar to AirDrop** to send a Wi-Fi password from one device to another.
> When a user selects a Wi-Fi network (requestor) and is prompted for the Wi-Fi password, the Apple device starts a Bluetooth Low Energy (BLE) advertisement indicating that it wants the Wi-Fi password. Other Apple devices that are awake, in close proximity, and have the password for the selected Wi-Fi network connect using BLE to the requesting device.
> The device that has the Wi-Fi password (grantor) **requires the Contact information of the requestor**, and the requestor must prove their identity using a process that's similar to AirDrop. After identity is proven, the grantor sends the requestor the passcode, which can be used to join the network.
> Organizations can restrict the use of Wi-Fi password sharing for devices or apps being managed through a device management service.

三条要点：**请求方广播的是"我要口令"这一意图**（不是哈希换文件）；**授予方硬性要求请求方的联系信息**；**方向是单向下发**（口令从持有者流向请求者）。第四段再次出现设备管理这一控制路径，与 11.1 一致。

## 12.2 将密码发送给其他用户或 Apple 设备

`sec8839a8de2`（发布日期 2021-02-18）。这一页给出的是一条**强制性约束**，本篇认为是近场凭据通道里最硬的一句：

> 无论用户的设置如何，通过隔空投送发送凭证操作**始终在"仅限联系人"模式下**进行。

对照 11.1 那句"AirDrop 默认为仅限联系人，用户可选择所有人"，两页合起来的意思非常清楚：**AirDrop 的可见性开关是用户可控的，但当它被用来传凭据时，Apple 强制走仅限联系人。** 这是一条真实存在的"同一机制、两种信任门槛"的设计，且完全由 T1 支持。

同页其它可引要点：

- Apple TV 场景下"附近的**所有** iPad 或 iPhone 都会显示提示"；
- 同一 iCloud 账户 → 自动加密；不同账户 → 用 **PIN 码建立加密连接**；
- "iPhone 必须解锁，并且靠近与该 Apple TV 配对的 Siri Remote 遥控器，才能收到此提示"。

**这一组条件（解锁 + 近场 + 已配对的遥控器）是手册层给出的、少见的多因子近场前置条件。** 它与第 9.2 节"App 前台 + iPhone 解锁"、第 6.3 节"人的确认"属于同一条设计线。

## 12.3 与接力页的对照：重放防护只在接力页出现

`secf78dbe639`（接力安全性，发布日期 2021-02-18）在 BLE 广告上写的是：

> each device generates a symmetric 256-bit AES key that gets stored in the device's keychain. This key can encrypt and authenticate the BLE advertisements … using AES-256 in GCM mode, **with replay protection measures**.

对照第 1 章蓝牙页："Message integrity: Protection against message forgeries"——**没有提重放**。对照第 11 章 AirDrop 页——**也没有提重放**。

本篇由此得到一条精确的口径结论：

> 手册层里"重放保护"这一表述**只在接力页出现**（针对 BLE 广告字节，AES-256-GCM）；蓝牙页讲完整性时只讲"防止消息伪造"，AirDrop 页未提重放。

这条结论同时限定了本篇的推论边界：**不能把接力页的"有重放保护"外推成"所有 BLE 广播都有重放保护"。**

另外三条接力页要点，本篇需要它们来完成对照（其完整分析属选题 E）：

- 配对是**带外**建立的："the two devices establish a Bluetooth Low Energy (BLE) 4.2 pairing **out-of-band using APNs**"——不经过蓝牙空口协商；
- 密钥存放在**钥匙串**（keychain），手册层没说安全隔区；
- 广告字节的可读范围："**Only the user's other devices can decrypt the advertisement bytes.**"

## 12.4 智能互助与自动解锁：两条近场的凭据侧写

`seca4b33e8c9`（智能互助 / 个人热点安全性，发布日期 2021-02-18）给热点发现机制两条可引原文：

> 标识符 "generated from a DSID (Destination Signaling Identifier) that's tied to the iCloud account and **rotated periodically**"
> 家庭共享场景 "secured with an ECDH (Curve25519) ephemeral key that's authenticated with the users' respective device-specific **Ed25519 public keys** … previously synced between the members of Family Sharing **using IDS**"

**"rotated periodically" 没有给周期数值**（S1 的同类沉默）；**Ed25519 / Curve25519 是手册层少见的显式曲线命名**，值得在本篇附表 A 登记。

`sec6ab47ebfc`（自动解锁 Apple 设备，发布日期 2026-01-28）给的是近场信任里最强的一组约束，中文页可直引：**相互认证的站对站（STS）协议**"在两台设备中的安全隔区之间直接协商，且所有加密材料都保留在该安全域内（未搭载安全隔区的 Mac 电脑除外，此类 Mac 会在内核中终止 STS 隧道）"；需同 Apple 账户 + 双重认证 + **逐条启用每种解锁关系**；32 字节随机解锁密钥 + PDK 封装；用 BLE 建连并以点对点无线局域网"安全估算彼此之间的距离"。

这一页在本篇里的价值有两点：

1. **它是手册层唯一一处把"密钥材料保留在安全隔区内"写成近场通道属性的地方**——也就是说，S6（蓝牙链路密钥是否进 SEP）的**反面证据不存在，正面证据也不存在**；只有自动解锁这一条被明确放进 SEP。
2. **"安全估算彼此之间的距离"没有说明测距方法**（是否用 UWB、如何防距离欺骗均未写）。结合第 10 章，本篇可以写一句完整的口径结论：**手册层里"距离"出现过两次（U1 空间感知、自动解锁距离估算），两次都没有配任何安全机制描述。**

# 13 措辞层普查：148 个近场族段落里，三个行业常用词各出现 0 次

本章是本篇的分界。前 12 章讲的是"Apple 说机制是什么"，从这里开始讲"Apple 说缺陷是什么样子"。两者用的是不同的语言，而这个差别很少被写下来。

## 13.1 语料与切块

基数：Apple 安全响应顾问页英文转储 **257 份**，按页面里的组件标题（形如 `Bluetooth`、`AirDrop`、`Wi-Fi` 的段落头）切块。本篇统计的近场族段落由以下组件名命中构成：`Bluetooth`、`Core Bluetooth`、`CoreBluetooth - LE`、`AirDrop`、`Handoff`、`Ultra Wideband`（无命中）、`Nearby`（无命中）、`Hotspot`（无命中）、`Cellular Calls`（无命中）、`Text Message Forwarding`（无命中）、`Continuity`（无命中）、`Pairing`（无命中）、`afclip`、`Clipboard`、`Wi-Fi`、`Wi-Fi Connectivity`。

结果：**近场族段落 148 个，分布在 73 份页面上。**

## 13.2 词频（同一次普查，段落内精确词形）

| Apple 词组 | 命中段落数 / 出现次数 | 本篇能写的结论 |
|---|---|---|
| **actively exploited** | **0 / 0** | 顾问页从未在任何近场族段落声明在野利用 ⇒ 本篇不得写"某蓝牙漏洞被在野利用"，只能写"Apple 未作在野声明" |
| **man-in-the-middle** | **0 / 0** | T1 蓝牙页把 MITM 防御列为卖点，T2 全量 0 次 ⇒ "中间人"只能作为**分析术语**，不得标为 Apple 口径 |
| **eavesdrop** | **0 / 0** | 同上（"被动窃听"仅存在于 T1） |
| **sandbox** | **0 / 0** | 近场族段落里没有沙盒措辞；沙盒句式属其它组件段 |
| **remote attacker** | **0 / 0** | 顾问页近场族不使用该主语 |
| **root privileges** | **0 / 0** | 近场族没有一条写"root 权限" |
| zero-click | **0 / 0** | 该词不属 Apple 口径（本篇与 KEV 数据双侧一致） |
| privileged network position | 19 / 19 | T2 里近场风险的**主导句式**，可直引 |
| denial-of-service | 10 段 / 15 次 | 近场族的第二主句式 |
| physical proximity | 14 / 14 | **全部落在 Wi-Fi 族**，蓝牙族为 0 |
| kernel | 23 / 23 | **全部落在 Wi-Fi 族**，蓝牙 / AirDrop / Handoff 族为 0 |
| kernel privileges | 4 / 4 | 全部来自同一个 CVE-2026-28819 的 `Wi-Fi` 段落（第 17.3 节） |
| out-of-bounds | 12 段 / 20 次 | 集中在 Wi-Fi 族 |
| use after free | 7 / 7 | 集中在 Wi-Fi 族 |
| symlink | 5 / 5 | 集中在 `Clipboard` 段落（第 17.4 节） |
| Beacon Protection | 3 / 3 | 仅 CVE-2024-40856 一条（第 17.2 节） |
| in Bluetooth range | 2 / 2 | 仅 CVE-2024-27867 与 CVE-2025-20701，**两者都是耳机固件页** |
| bypass pairing | 1 / 1 | 仅 CVE-2024-44124 |
| previously paired | 1 / 1 | 仅 CVE-2024-27867 |
| spoof | 3 / 3 | 蓝牙族 3 段（键盘欺骗、耳机欺骗） |
| pairing | 1 / 1 | 蓝牙族里只有 CVE-2024-44124 一条写了 pairing |
| encryption | **0 / 0** | 近场族**没有任何一条**用 encryption 描述修复方式 |

最后一行值得停一下：**修复句式里从不出现"加密"**。Apple 在近场族用的修复动词是 improved checks / improved state management / improved access restrictions / improved validation / improved memory handling / improved bounds checking / improved data redaction。**"加把加密"不是 Apple 在近场缺陷上给出的修复类别。**

## 13.3 两族对照：蓝牙族与 Wi-Fi 族是两种完全不同的措辞动物

按组件名把 148 段拆成两族，各自重测（蓝牙 / AirDrop / Handoff 族 **82 段**；Wi-Fi / Wi-Fi Connectivity / afclip / Clipboard 族 **78 段**）：

| 词 | 蓝牙族（82 段） | Wi-Fi 族（78 段） |
|---|---:|---:|
| actively exploited | 0 | 0 |
| man-in-the-middle | 0 | 0 |
| eavesdrop | 0 | 0 |
| remote attacker | 0 | 0 |
| root privileges | 0 | 0 |
| sandbox | 0 | 0 |
| zero-click | 0 | 0 |
| **kernel** | **0** | **23** |
| physical proximity | 0 | 14 |
| privileged network position | 12 | 7 |
| denial-of-service | 15 次 / 10 段 | 8 |
| spoof | 3 | 0 |
| pairing | 1 | 0 |
| out-of-bounds | 0 | 20 |
| use after free | 0 | 7 |
| symlink | 0 | 5 |
| Beacon Protection | 0 | 3 |

**三条可直接进正文的结构性结论：**

1. **措辞层的"内核"只出现在 Wi-Fi 族，蓝牙 / AirDrop / Handoff 族一次都没写 kernel（0 / 82）。** 因此本篇所有"内核态"证据必须落在 Wi-Fi 段落，不能挂在蓝牙段落上。
2. **两族都不写在野利用、中间人、窃听、沙盒、远程攻击者、root 权限、零点击。** 这七个词只能作为本报告的**分析术语**出现，且每次出现都要说明"这是本报告用词，非 Apple 原文"。
3. **可达位置的表述只有两种**："an attacker in a privileged network position"（19 段）与"an attacker in physical proximity"（14 段，全在 Wi-Fi 族）。**顾问页近场族里不存在"remote attacker"这一句式。**

## 13.4 组件段落名的存在性：一组重要的负结果

在 257 份顾问页里，与近场 / 连续互通相关的组件段落名**只有这些**（括号内为该段落名出现的段数）：

`Wi-Fi`（61）、`Bluetooth`（35）、`AirDrop`（20）、`Core Bluetooth`（15）、`afclip`（7）、`CoreBluetooth - LE`（6）、`Handoff`（6）、`Wi-Fi Connectivity`（5）、`Clipboard`（5）。

**以下段落名不存在**：Continuity、Personal Hotspot / Hotspot、Ultra Wideband、Nearby、Call Relay / Cellular Calls、Text Message Forwarding、Auto-unlock、Pairing、Bonding。

后果非常具体：**正文不得写"Apple 在超宽带 / 热点 / 通话中继组件下披露了漏洞"，因为这些组件名在措辞层不存在。** 近场族里唯一与 UWB 沾边的公开条目，只能经由 T1（手册层 UWB 页）说明，而 T1 不含缺陷信息。

# 14 段落名不等于实现层：Apple 自己有三四种写法

## 14.1 三种蓝牙段落名并存

同一件事（蓝牙相关缺陷）在顾问页里出现在三个不同的段落名下：

| 段落名写法 | 段数 | 典型条目 |
|---|---:|---|
| `Bluetooth` | 35 | CVE-2024-23277、CVE-2026-20650、CVE-2025-43354 |
| `Core Bluetooth`（带空格） | 15 | CVE-2024-44124、CVE-2025-31212 |
| `CoreBluetooth - LE`（带连字符与 LE） | 6 | CVE-2024-23250 |

**必须逐页照抄段落名**，不能统一成一种。这三种写法在 Apple 页面上并存，且各自的条目集合不同。

## 14.2 段落名与后果不匹配的两个反例

**反例一：段落名是 `Bluetooth`，后果是 App 读取数据。** CVE-2025-43354 位于 `Bluetooth` 段落，Impact 是 "An app may be able to access sensitive user data"，Description 是 "A logging issue was addressed with improved data redaction."。一条**日志脱敏**修复被放在名为 Bluetooth 的段落里。

**反例二：段落名是 `afclip`，但 Impact 的主语是 app。** CVE-2025-43186 的 Impact 是 "Parsing a file may lead to an unexpected app termination"。虽然 afclip 属于近场文件共享一侧的进程，**措辞层没有把它说成 AirDrop，也没有说成内核。**

由此得到本篇第 14 章的结论，它是第 17 章逐条解读所有条目的前提：

> **顾问页的段落名是"发布归类"，不是"实现层位置"。** 在本篇 T3 源码层不可用的前提下，任何关于"某缺陷在蓝牙协议栈的哪一层"的判断，都不能由段落名支撑，只能写成"Apple 把它归入某段落"。

## 14.3 手册层与措辞层的语义落差

| 手册层（T1）说的话 | 顾问页（T2）说的话 | 落差 |
|---|---|---|
| 配对 / 绑定 / 认证 / 加密 / 完整性 / SSP 六项功能 | 从未出现 bonding、SSP、passkey、numeric comparison 等词 | T1 讲能力名，T2 讲后果句 |
| 防被动窃听（ECDHE）、防中间人（数字比较 / 密钥接入） | 从未出现 eavesdrop / man-in-the-middle | **T1 的卖点在 T2 里没有对应的缺陷类别** |
| "配对需要用户的响应，包括所有非 MITM 配对模式" | 只有一条提到 pairing（CVE-2024-44124） | 用户响应这件事在缺陷措辞里完全不出现 |
| 加密执行在控制器（Controller） | 无 Controller / host 侧表述 | 执行位置在 T2 不存在 |

这张表是**本篇最想交付的东西**：它说明"Apple 说它防住了什么"与"Apple 说它修了什么"是两套不互相引用的文本。用第一套的词汇去描述第二套的条目（例如"这条 CVE 说明 Apple 的 Secure Simple Pairing 被绕过了"），在本篇的证据结构里没有依据——顾问页 CVE-2024-44124 的原句是 "A malicious Bluetooth input device may bypass pairing"，它**没有**指向任何 T1 命名功能。

# 15 `Bluetooth` 段落逐条：七组条目，三种性质

本章按"首次出现的页面"列出条目，并给出版本、Impact 原句、Description 原句、致谢与判定。所有原句为英文页面逐字引用。

## 15.1 CVE-2024-23277：注入按键（键盘欺骗）

| 项 | 内容 |
|---|---|
| 段落 | `Bluetooth` |
| 首次修复页 | iOS 17.4 and iPadOS 17.4（页 120893，Entry added March 7, 2024）；另 macOS Sonoma 14.4（页 120895） |
| Impact | An attacker in a privileged network position may be able to inject keystrokes by spoofing a keyboard |
| Description | The issue was addressed with improved checks. |
| 致谢 | Marc Newlin of SkySafe |

判定要点：**"privileged network position" + "spoofing a keyboard"。** 这是近场族里第一条"攻击者不需要物理接触、但需要处于某个网络位置"的句式。Apple 未给该条目的缺陷类别（Description 只有 improved checks）。**本页 Entry 是后补的（March 7, 2024），引用时要说明这是条目添加日期而非发布日期。**

## 15.2 CVE-2026-20650：构造蓝牙包造成拒绝服务

| 项 | 内容 |
|---|---|
| 段落 | `Bluetooth` |
| 首次修复页 | iOS 26.3 and iPadOS 26.3（页 126346）；另 macOS Tahoe 26.3（126348）、tvOS 26.3（126351）、watchOS 26.3（126352）、visionOS 26.3（126353） |
| Impact | An attacker in a privileged network position may be able to perform denial-of-service attack using crafted Bluetooth packets |
| Description | A denial-of-service issue was addressed with improved validation. |
| 致谢 | jioundai |

同页 `Bluetooth` 段落另有**无 CVE 的纯致谢**：Tommaso Sacchetti。

判定要点：这是近场族"crafted X packets"句式在蓝牙侧的对应物（Wi-Fi 侧见第 17.3 节 CVE-2026-28994）。**后果被写成 denial-of-service，没有写内存破坏、没有写代码执行。** 这一点必须在正文里保留——它是本篇不将蓝牙包缺陷升级叙述的直接依据。

## 15.3 CVE-2024-27867：耳机被伪装成已配对设备（不是 iOS 条目）

| 项 | 内容 |
|---|---|
| 段落 | `Bluetooth` |
| Available for | **AirPods Firmware Update 6A326、AirPods Firmware Update 6F8、Beats Firmware Update 6F8**（页 120907） |
| Impact | When your headphones are seeking a connection request to one of your previously paired devices, an attacker in Bluetooth range might be able to spoof the intended source device and gain access to your headphones |
| Description | An authentication issue was addressed with improved state management. |
| 致谢 | Jonas Dreßler |

判定要点（这一条最容易写错）：**Available for 是耳机固件版本号，不是 iOS 版本。** 该条目**没有 iOS / iPadOS / macOS 版本对应关系**，写成"iOS 修复了某蓝牙漏洞"是错的。措辞本身倒极有价值：它同时给出**攻击时机**（耳机正在寻找与此前配对设备的连接请求）、**攻击位置**（in Bluetooth range）与**后果类型**（authentication issue + state management），是近场族里少见的"三要素齐备"句子。

## 15.4 CVE-2025-20701：未配对设备麦克风被监听（Beats 固件 + 第三方分配 CVE）

| 项 | 内容 |
|---|---|
| 段落 | `Bluetooth` |
| Available for | **Beats Firmware Update 1B211**（页 127557，Beats Studio Buds） |
| Impact | An attacker within Bluetooth range may be able to listen through the microphone of a device which is not yet paired and actively seeking pair requests |
| Description | This is a vulnerability in open source code and Apple Software is among the affected projects. The CVE-ID was assigned by a third party. |
| 致谢 | Dennis Heinze and Frieder Steinmetz of ERNW GmbH |

判定要点两条：

1. **该条同样不是 iOS 条目**，Available for 只写耳机。
2. **Apple 明确写了"这是开源代码中的漏洞，Apple 软件是受影响项目之一；CVE 编号由第三方分配"**。这句话在本篇的作用是口径示范：**并非所有 CVE 编号都由 Apple 作为 CNA 分配**，引用编号时必须核对 Description 里有没有这句。

同时这也是近场族里唯一一条 Apple 原文写到"可以监听麦克风"的条目——**注意其主语是"尚未配对、正在主动寻找配对请求的设备"，不是 iOS 设备。**

## 15.5 只有致谢、没有 CVE 的 `Bluetooth` 段落

| 页号 | 产品与版本 | 致谢 | 条目备注 |
|---|---|---|---|
| 121837 | iOS 18.2 and iPadOS 18.2 | Sophie Winter | Entry added March 17, 2025 |
| 121839 | macOS Sequoia 15.2 | Sophie Winter | 同上 |
| 121843 | watchOS 11.2 | Sophie Winter | 同上 |
| 121845 | visionOS 2.2 | Sophie Winter | 同上 |
| 124147 / 124149 / 124153 / 124154 / 124155 | iOS 18.6、macOS 15.6、tvOS 18.6、visionOS 2.6、watchOS 11.6 | LIdong LI, Xiao Wang, Shao Dong Chen, and Chao Tan of Source Guard | 无 CVE |
| 126792 / 126794 / 126797 / 126798 / 126799 | iOS 26.4、macOS 26.4、tvOS 26.4、watchOS 26.4、visionOS 26.4 | Hamid Mahmoud | 无 CVE |

**这些段落是真实存在的，只是没有 CVE。** 本篇的处理规则：**可以写"Apple 在某页的蓝牙段落致谢了某人，且未给出对应 CVE"，不得反推缺陷机制。** 第 17.5 节会把这类段落的总数给出来。

# 16 `Core Bluetooth` / `CoreBluetooth - LE` 段落逐条：面向 App 的那一侧

这一族的共同句式是 **"An app may be able to…"**——主语是 App，不是攻击者，不是远程方。这条句式在整个近场族里最具区分度：**它说明缺陷的触发者是本机上一个普通应用**，因此权限与数据访问类措辞占多数。

## 16.1 CVE-2024-23250：无需许可访问蓝牙麦克风

| 项 | 内容 |
|---|---|
| 段落 | **`CoreBluetooth - LE`** |
| 首次修复页 | iOS 17.4 and iPadOS 17.4（120893，Entry added March 7, 2024）；watchOS 10.4（120881）；tvOS 17.4（120882）；macOS Sonoma 14.4（120895） |
| Impact | An app may be able to access Bluetooth-connected microphones without user permission |
| Description | An access issue was addressed with improved access restrictions. |
| 致谢 | Guilherme Rambo of Best Buddy Apps (rambo.codes) |

判定要点：后果写的是 **access issue**，修复写的是 **improved access restrictions**。这是权限访问类，不是内存破坏类。同时它也是本篇"蓝牙 + 麦克风"两条麦克风条目之一（另一条是 15.4 的耳机麦克风，两者不可混写：一条是 **App 无许可访问已连接的蓝牙麦克风**，一条是 **近距离攻击者监听未配对耳机的麦克风**）。

## 16.2 CVE-2024-44124：恶意蓝牙输入设备可绕过配对

| 项 | 内容 |
|---|---|
| 段落 | `Core Bluetooth` |
| 首次修复页 | iOS 18 and iPadOS 18（121250） |
| Impact | **A malicious Bluetooth input device may bypass pairing** |
| Description | This issue was addressed through improved state management. |
| 致谢 | Daniele Antonioli |

判定要点：这是 T2 近场族 148 段里**唯一一句含 "bypass pairing" 的 Impact**（第 13 章计数 = 1）。它同时是近场族里唯一一条**把攻击者描述成一个设备（input device）**的条目。

与本篇第 1 章的关系必须写成这样：**手册层把"配对需要用户响应"写成设计前提（T1），顾问页有一条缺陷被描述为"恶意蓝牙输入设备可绕过配对"（T2）。Apple 没有在任何文本里把这两句连起来，也没有说明被绕过的是哪种配对模式。** 把这个连接自行补上（"说明 SSP 的数字比较可被绕过"）就是本篇禁止的那类推论。

## 16.3 CVE-2025-31212：敏感用户数据访问

| 项 | 内容 |
|---|---|
| 段落 | `Core Bluetooth`（带空格写法） |
| 首次修复页 | iOS 18.5 and iPadOS 18.5（122404）；macOS Sequoia 15.5（122716）；tvOS 18.5（122720）；visionOS 2.5（122721）；watchOS 11.5（122722） |
| Impact | An app may be able to access sensitive user data |
| Description | This issue was addressed through improved state management. |
| 致谢 | Guilherme Rambo of Best Buddy Apps (rambo.codes) |

同页（122404 / 122716 / 122720 / 122721 / 122722）`AirDrop` 段落另有**无 CVE 的纯致谢**：Dalibor Milanovic。

判定要点：Impact 句式 "access sensitive user data" 在整个近场族里出现多次（16.4、17.4 也是这一句），**而 Description 各不相同（state management / data redaction / restriction of data container access / validation of symlinks）**。这说明同一句后果描述可以对应完全不同的修复动作，正文不得由 Impact 反推缺陷类型。

## 16.4 CVE-2025-43354：日志脱敏

| 项 | 内容 |
|---|---|
| 段落 | **`Bluetooth`**（注意：不是 Core Bluetooth） |
| 首次修复页 | iOS 26 and iPadOS 26（125108）；macOS Tahoe 26（125110）；tvOS 26（125114）；visionOS 26（125115）；watchOS 26（125116） |
| Impact | An app may be able to access sensitive user data |
| Description | **A logging issue was addressed with improved data redaction.** |
| 致谢 | — |

判定要点：这是第 14.2 节"段落名不等于实现层"的主要样本——**段落名叫 `Bluetooth`，Description 说的是日志脱敏。** 另外，同批页面的 `Core Bluetooth` 段落还各自带一条**无 CVE 的纯致谢**：Leon Böttger（125108 / 125110 / 125114 / 125115 / 125116，Entry added November 3, 2025）。

## 16.5 只有致谢的 `Core Bluetooth` / `CoreBluetooth - LE` 段落

| 页号 | 产品与版本 | 段落 | 致谢 | 备注 |
|---|---|---|---|---|
| 121238 | macOS Sequoia 15 | `Core Bluetooth` | Nicholas C. of Onymos Inc. (onymos.com) | 无 CVE |
| 121250 | iOS 18 and iPadOS 18 | `Core Bluetooth` | Nicholas C. of Onymos Inc. | 无 CVE（同页有 16.2 的 CVE 条目） |
| 125110 | macOS Tahoe 26 | `Core Bluetooth` | Nathaniel Oh (@calysteon) | 无 CVE |
| 125110 | macOS Tahoe 26 | `Core Bluetooth` | Leon Böttger | Entry added November 3, 2025 |
| 125110 | macOS Tahoe 26 | `Bluetooth` | Yiğit Can YILMAZ (@yilmazcanyigit) and Csaba Fitzl (@theevilbit) of Iru | **Entry updated December 12, 2025** |
| 126794 | macOS Tahoe 26.4 | `Core Bluetooth` | Nathaniel Oh (@calysteon) | 无 CVE |
| 127110 | iOS 26.5 and iPadOS 26.5 | `CoreBluetooth - LE` | Luke Noble | Entry added August 25, 2026 |
| 127119 | watchOS 26.5 | `CoreBluetooth - LE` | Luke Noble | Entry added August 25, 2026 |

**关于日期字段的一条硬规则。** 每一页顾问页同时具备两个日期：页面发布日期与条目备注里的 "Entry added / updated …"。上表最后一行的 August 25, 2026 是**条目添加日期**；macOS Tahoe 26 那一行的 December 12, 2025 是**条目更新日期**。引用任何一条时必须点名用的是哪个字段——本系列过去出现过的口径错误，多数源于把这两个日期当同一个。

# 17 Wi-Fi 族、AirDrop / Handoff 族与纯致谢条目

前两章把蓝牙族讲完，本章把 148 段里剩下的两组讲完。之所以必须讲，是因为第 13.3 节那条"内核只在 Wi-Fi 族出现"的结论，只有把 Wi-Fi 族逐条摊开才能让读者看到证据。

## 17.1 AirDrop 与 Handoff 条目：三条 CVE、两处纯致谢

| 页号 | 产品与版本 | 段落 | CVE | Impact（原句） | Description（原句） | 备注 |
|---|---|---|---|---|---|---|
| 120893 | iOS 17.4 and iPadOS 17.4 | `AirDrop` | （无 CVE） | — | — | 致谢 Cristian Dinca of Tudor Vianu National High School of Computer Science, Romania；Entry added March 7, 2024 |
| 120909 | iOS 17.6 and iPadOS 17.6 | `AirDrop` | CVE-2024-54564 | A file received from AirDrop may not have the quarantine flag applied | This issue was addressed through improved state management. | Entry added March 20, 2025 |
| 120911 | macOS Sonoma 14.6 | `AirDrop` | CVE-2024-54564 | 同上 | 同上 | 跨平台同 CVE |
| 120915 | visionOS 1.3 | `AirDrop` | CVE-2024-54564 | 同上 | 同上 |  |
| 122371 | iOS 18.4 and iPadOS 18.4 | `AirDrop` | CVE-2025-24097（Ron Masas of BREAKPOINT.SH） | An app may be able to read arbitrary file metadata | A permissions issue was addressed with additional restrictions. |  |
| 122373 / 122374 / 122376 / 122377 / 122405 | macOS Sequoia 15.4 / macOS Sonoma 14.7.5 / watchOS 11.4 / tvOS 18.4 / iPadOS 17.7.7 | `AirDrop` | CVE-2025-24097 | 同上 | 同上 | 同一 CVE 覆盖五条产品线 |
| 127110 | iOS 26.5 and iPadOS 26.5 | `AirDrop` | CVE-2026-43667 | An attacker in a privileged network position may be able to cause a denial-of-service | A reachable assertion was addressed with improved input validation. | Entry added August 25, 2026 |
| 127115 / 127119 / 127120 | macOS Tahoe 26.5 / watchOS 26.5 / visionOS 26.5 | `AirDrop` | CVE-2026-43667 | 同上 | 同上 |  |
| 148287 | iOS 18.7.10 and iPadOS 18.7.10 | `AirDrop` | CVE-2026-43667 | 同上 | 同上 | **遗留分支同样获补丁** |
| 122371 | iOS 18.4 and iPadOS 18.4 | `Handoff` | CVE-2025-30463（mzzzz__） | An app may be able to access sensitive user data | The issue was addressed with improved restriction of data container access. |  |
| 122371 / 122373 / 122376 / 122377 | iOS 18.4 / macOS Sequoia 15.4 / watchOS 11.4 / tvOS 18.4 | `Handoff` | （无 CVE） | — | — | 致谢 Kirin and FlowerCode |

三条判定：

1. **AirDrop 族 20 段里只有三个 CVE**，其中两个后果落在"文件属性 / 元数据"（隔离标记未施加、任意文件元数据读取），一个落在拒绝服务。**没有任何一条措辞提到配对绕过、身份哈希被破解或 TLS 被降级**——这三件事是第 11 章流程里最核心的环节，也是 T2 完全沉默的地方。
2. **Handoff 族 6 段里只有一个 CVE**，且它是 App 侧数据访问（"restriction of data container access"），不是接力信道本身。
3. **CVE-2026-43667 同时出现在 iOS 26.5（主线）与 iOS 18.7.10（遗留分支）**，而本章稍后的 CVE-2026-28819 只出现在遗留分支。**引用 AirDrop 或 Wi-Fi 条目时必须逐页给出产品与版本**，"iOS 已修复"这种无版本表述在本篇不成立。

## 17.2 Beacon Protection：一条以协议特性作为修复说明的条目

| 项 | 内容 |
|---|---|
| 段落 | `Wi-Fi` |
| 页号 | tvOS 18（121248）、iOS 18 and iPadOS 18（121250）、macOS Sequoia 15（121238）；Entry updated March 3, 2025 |
| CVE | CVE-2024-40856 |
| Impact | An attacker may be able to force a device to disconnect from a secure network |
| Description | **An integrity issue was addressed with Beacon Protection.** |
| 致谢 | Preet Dsouza (Fleming College, Computer Security & Investigations Program), Domien Schepers |

这是近场族 148 段里**唯一一条把修复写成某个协议特性**的条目（第 13 章计数 = 3 段，同一条 CVE 跨三页）。它的价值在两点：

- **后果句式独一份**："An attacker **may be able to force a device to disconnect** from a secure network"——主语是 attacker，动词是 disconnect，没有 app、没有 memory。整个近场族里"攻击者直接影响连接状态"的句子只有这一条与 15.1（键盘注入）。
- **修复写成特性名**：Apple 不说"改了什么代码"，而说"用 Beacon Protection 解决了一个完整性问题"。这是 T2 少见的、可以直接对接 T1 语言体系的句子（T1 的无线局域网页在 `secfd166f620`），但**手册层的无线局域网页与这句之间，Apple 没有做交叉引用**。本篇登记为口径缺口。

## 17.3 近场可达性与内核后果：本篇唯一合法的两组组合

第 13.3 节说"kernel 只在 Wi-Fi 族出现"。这里给出全部 kernel 条目中**与近场可达性绑定**的那些。

**组合一：physical proximity → 内核越界读。**

| 项 | 内容 |
|---|---|
| 段落 | `Wi-Fi` |
| CVE | CVE-2025-43374（jioundai of 360 Vulnerability Research Institute, chen fengjiao of HBC） |
| Impact | **An attacker in physical proximity may be able to cause an out-of-bounds read in kernel memory** |
| Description | An out-of-bounds read was addressed with improved bounds checking. |
| 页号 | macOS Sonoma 14.7.3（122069）、macOS Ventura 13.7.3（122070）、iOS 18.5（122404）、iPadOS 17.7.7（122405）、macOS Sequoia 15.5（122716）、tvOS 18.5（122720）、visionOS 2.5（122721）、watchOS 11.5（122722） |

**组合二：privileged network position → 构造无线包造成释放后使用。**

| 项 | 内容 |
|---|---|
| 段落 | `Wi-Fi` |
| CVE | CVE-2026-28994（Alex Radocea） |
| Impact | An attacker in a privileged network position may be able to perform denial-of-service attack using **crafted Wi-Fi packets** |
| Description | A use after free issue was addressed with improved memory management. |
| 页号 | iOS 26.5（127110）、iOS 18.7.9（127111）、macOS Tahoe 26.5（127115）、macOS Sequoia 15.7.7（127116）、macOS Sonoma 14.8.7（127117）、tvOS 26.5（127118）、watchOS 26.5（127119） |

**组合三：内核代码执行，但只在遗留分支。**

| 项 | 内容 |
|---|---|
| 段落 | `Wi-Fi` |
| CVE | CVE-2026-28819（Wang Yu） |
| Impact | **An app may be able to execute arbitrary code with kernel privileges** |
| Description | An out-of-bounds write issue was addressed with improved bounds checking. |
| 页号 | iOS 18.7.9（127111，Available for: iPhone XS、XS Max、XR、iPad 7th generation）、macOS Tahoe 26.5（127115）、macOS Sequoia 15.7.7（127116）、macOS Sonoma 14.8.7（127117） |

这三组是本篇"近场通道 → 内核后果"的全部合法证据。三条限定必须同时写：

1. **主语决定性质。** 组合一、二的主语是 attacker（外部可达），组合三的主语是 **an app**（本机前置）。把三者并列写成"Wi-Fi 驱动里的三个远程内核漏洞"是错的。
2. **组合三是遗留分支条目。** iOS 26.5（127110）的 `Wi-Fi` 段落里没有 CVE-2026-28819；它出现在 18.7.9 与三条 macOS 老版本上，且 127111 的机型列表只有 iPhone XS / XS Max / XR / iPad 第 7 代。**本篇不得写成"当前 iOS 存在内核代码执行缺陷"。**
3. **Apple 未对任何一组作在野声明**（第 13 章：actively exploited 在近场族 0 次）。

另外两组 kernel 相关条目属于"App 触发 + 内核内存"型，不构成近场可达性，本篇仅作登记：CVE-2024-44227（Tim Michaud of Moveworks.ai）与 CVE-2024-54546（Antonio Zekic、ant4g0nist）在 macOS Sequoia 15（121238）；CVE-2025-43373（Wang Yu of Cyberserval）在 macOS 26.1 / 15.7.2 / 14.8.2（125634 / 125635 / 125636）；CVE-2026-20621（Wang Yu of Cyberserval）在 iOS 26.3、iOS 18.7.5、macOS 26.3 / 15.7.4 / 14.8.4、visionOS 26.3（126346–126353）。三者的 Impact 同句："An app may be able to cause unexpected system termination or corrupt kernel memory"。

## 17.4 `afclip`、`Clipboard` 与两处措辞异常

**`afclip`（7 段）**：CVE-2025-43186（Hossein Lotfi (@hosselot) of Trend Micro Zero Day Initiative），Impact "Parsing a file may lead to an unexpected app termination"，Description "The issue was addressed with improved memory handling."，出现在 iOS 18.6、macOS 15.6 / 14.7.7 / 13.7.7、tvOS 18.6、visionOS 2.6、watchOS 11.6（124147、124149–124155）。

**`Clipboard`（5 段）**：CVE-2026-28866（Cristian Dinca (icmd.tech)），Impact "An app may be able to access sensitive user data"，Description "This issue was addressed with improved validation of symlinks."，出现在 iOS 26.4、iOS 18.7.7、macOS 26.4 / 15.7.5 / 14.8.5（126792–126796）。

两处措辞异常，本篇如实标注，不替 Apple 圆场：

1. **`Clipboard` 段落 + "improved validation of symlinks"**：Impact 说 App 可访问敏感用户数据，Description 说符号链接校验。这是措辞层模板复用造成的语义不连贯，**不能推断其实际修复对象**。
2. **`afclip` 这个段落名**。它是近场文件共享栈一侧的进程名，但 Apple 既没写 AirDrop、也没写内核；Impact 主语是 app、后果是"意外终止"。**本篇不得把 `afclip` 写成"AirDrop 组件的漏洞"。**

顺带一条：致谢名单里 **Cristian Dinca** 同时出现在 120893 的 `AirDrop` 纯致谢段（身份为高中生， Romania 的 Tudor Vianu 国家计算机科学中学）与 CVE-2026-28866 的 `Clipboard` 条目。这是本篇能引用的、"同一研究者从致谢走向署名 CVE"的唯一样本——**但两者之间没有任何机制关联，不得据此叙述成长度或风险。**

## 17.5 纯致谢段落的总量与它意味着什么

本篇在 148 个近场族段落中，**至少 9 处段落只有致谢、没有 CVE**（分布见 15.5、16.5、17.1，另有 `Wi-Fi` / `Wi-Fi Connectivity` 段落 5 处：iOS 26 / 18.7、macOS Tahoe 26 / Sequoia 15.7 / Sonoma 14.8 等页致谢 Aobo Wang (@M4x_1997)、Csaba Fitzl (@theevilbit) of Kandji、Noah Gregory (wts.dev)、Wojciech Regula of SecuRing、an anonymous researcher；iOS 26.4 等五条产品线页致谢 Kun Peeks (@SwayZGl1tZyyy) 与匿名研究者；`Wi-Fi Connectivity` 五条产品线页致谢 Alex Radocea of Supernetworks, Inc）。

这个数字的解释必须保守：**它意味着 Apple 收到并致谢了一些未成为公开条目的近场研究。** 本篇能写的只有一句：**这些段落存在，且不提供机制信息。** 从致谢名单推断"存在未公开的蓝牙缺陷类别"没有依据。

## 17.6 本章的四条结构性事实（供第 19、20 章回用）

1. **段落名不是实现层。** `Bluetooth` / `Core Bluetooth` / `CoreBluetooth - LE` 三种写法并存，且 `Bluetooth` 段落里出现过"App 访问敏感用户数据 + 日志脱敏"（CVE-2025-43354）。
2. **Apple 在近场族里的句式高度集中在 `privileged network position`（19 / 148）**，而手册层没有对应表述——手册层讲配对、绑定、加密，顾问页讲"处于特权网络位置的攻击者"。**两层的语义落差本身就是分析对象。**
3. **音频配件（AirPods / Beats 固件页）自成一组 `Bluetooth` 条目**，其 Available for 只写耳机型号，**不具备 iOS 版本可比性**（15.3、15.4）。
4. **大量近场段落只有致谢、没有 CVE**（17.5）。凡引用这类段落，只能写"Apple 在某页致谢了某人，且未给出对应 CVE"，不得反推机制。

# 18 缺口登记

这一节逐条列出本篇查不到、因此正文不能写的事情。它的价值高于任何一条结论——近场类文章的可信度问题，绝大多数来自把这里的某一条"补全"了。

| 编号 | 缺口 | 现状 | 正文处理方式 |
|---|---|---|---|
| G1 | **源码层整体不可用** | 本地 XNU 镜像本轮为空目录，未取回任何符号、类名或调用关系 | 凡涉及实现层的句子一律写"本报告未核对源码"，或降级为"顾问页把它归入某段落" |
| G2 | 手册层无 UWB 安全细节（S3） | 超宽带页整页 3 句 | 不得写"UWB 使用加密测距 / 抗中继"；只能写空间感知 + 归入随机化 |
| G3 | 手册层无 Nearby Interaction 条目（S5） | zh 179 + EN 18 双侧 0 页 | 不得把它写成手册层机制 |
| G4 | 手册层从不写 AWDL（S4） | 双侧 0 页 | 该词只能标第三方或开发者文档口径 |
| G5 | 手册层未给 RPA 轮换周期（S1） | 无 | 不写具体数值 |
| G6 | 手册层未列"非 MITM 配对模式"名称（S2） | 只有一句承认其存在 | 不得点名具体配对模式 |
| G7 | 蓝牙链路密钥 / 接力广告密钥的存储介质 | 手册层：蓝牙页未说；接力页说钥匙串；自动解锁页说安全隔区 | 三者不可互换推定；不得写"蓝牙密钥在 SEP" |
| G8 | 近场绑定是否有有效期 | 物理配对页给了 30 天；蓝牙页无任何时限 | **不得把 30 天推广到蓝牙绑定** |
| G9 | T2 无在野 / 无 CVSS / 无 MitM / 无窃听 / 无 encryption 修复句式 | 近场族 148 段全量检索 0 次 | 这些词只能作为本报告用词，且须标注 |
| G10 | 措辞层无 Continuity / Hotspot / UWB / Call Relay 等组件段落名 | 257 份页面段落名清单 | 不得写"某组件下披露的漏洞" |
| G11 | 英文手册仅取样 18 页，其余 161 页英文措辞未核 | 取样面所限 | 英文侧只允许"取样的 18 页中 N 次" |
| G12 | 第 15.1–15.4、16.2、17.3 各 CVE 与第三方研究的对应关系 | 待逐条核验；**未核回之前，正文只写 Apple 侧措辞，不写研究结论** | 致谢人名可引（属 T2 原文），研究结论不可引 |
| G13 | Beacon Protection 与手册层无线局域网页的关系 | Apple 未交叉引用 | 只登记两侧各自的原文，不合并 |

# 19 从口径到配置：本篇材料能支持的运维动作

本章只做一件事：把前面各章有 T1 依据的**控制点**列成可执行清单。**每一条都对应本篇某节的原文，凡无依据的动作不列。**

## 19.1 主机与配件（有线近场）

| 动作 | 依据 | 备注 |
|---|---|---|
| 定期清理受信任主机 | 第 7.3 节：设置中的"重置网络设置"或"重置位置与隐私" | T1 直接给出的两个入口 |
| 认知配对记录会自行失效 | 第 7.3 节：30 天未使用即过期（iOS 11 / iPadOS 13.1 起） | **仅限主机配对，不外推（G8）** |
| 知道两个诊断服务的限制方式 | 第 7.3 节：`com.apple.mobile.pcapd` 仅限 USB；`com.apple.file_relay` 需 Apple 签名描述文件 | 一为介质限制，一为授权限制 |
| 理解锁定时对数据连接的三重时限 | 第 8.3 节：断开 1 小时 / 记住 30 天 / 连接满 3 天后锁定即拒 | 第 8.4 节给出 Apple 自述理由（恶意充电器、配件无可靠密码学识别方式） |
| 检查"始终打开数据连接"是否被显式开启 | 第 8.3 节第 4 条：可在设置中重新启用，部分辅助设备会自动启用 | "**会自动启用**"这一句值得纳入配置审计 |

## 19.2 无线近场

| 动作 | 依据 |
|---|---|
| AirDrop 保持"仅限联系人"；需要投送时才临时放开 | 第 11.1 节：默认即 Contacts Only |
| 知道凭据类 AirDrop 操作不受该开关影响 | 第 12.2 节：**"无论用户的设置如何，通过隔空投送发送凭证操作始终在'仅限联系人'模式下进行"** |
| 用设备管理策略限制 AirDrop 与 Wi-Fi 密码共享 | 第 11.1、12.1 节：两页各自写了"组织可限制" |
| 配对时真的去比对数字 | 第 1 章、第 3.1 节：手册层的防 MITM 手段**只有**数字比较与密钥接入，且**都要求人响应** |
| 不假定随机地址等于不可识别 | 第 2.3 节：私有地址由配对时交换的 IRK 派生，已配对方本就能解析 |
| 明白一次绑定可能跨两种传输复用 | 第 2.3 节：CTKD，BLE 链接密钥可派生经典蓝牙链接密钥（iOS 13 / iPadOS 13.1 起） |
| 知道"剪影图标"代表未验证 | 第 11.4 节：手册层给出的唯一视觉区分 |
| 近场凭据下发要设备解锁 | 第 9.2、12.2 节：NFC 交易要前台 + 解锁；给 Apple TV 发密码要 iPhone 解锁并靠近已配对遥控器 |
| 账户注销 / 移除密码 / 抹掉 / 申请删除账户会清除 SE 凭据 | 第 9.2 节四种场景 |

## 19.3 面向检测与响应侧的三条可用信号

这三条不是配置，而是**基于口径的观测建议**，且刻意保持防御视角：

1. **补丁核对只能按页号 + 产品 + 版本三件套做。** 本篇出现的同 CVE 跨 5–8 条产品线、以及"主线未收录而遗留分支收录"（17.3 组合三）两种情形说明：以"某 iOS 版本是否包含某 CVE"为单一判据会漏判。核对源应为具体顾问页，逐页抄版本列表。
2. **不要把条目当作攻击面清单。** 近场族 148 段里 `encryption` 修复句式为 0、`kernel` 在蓝牙族为 0（13.2、13.3）。若某内部文档把蓝牙条目列为"内核远程攻击面"，那与 Apple 自己的措辞不符，需要重新核据。
3. **条目日期的两个字段要分开登记。** 页面发布日期与 "Entry added / updated" 是两个不同时间（16.5）。做漏洞管理时若把"条目添加日"当作"修复日"，会低估暴露窗口——CVE-2024-54564 的条目是 2025 年 3 月 20 日补进 iOS 17.6（120909）页面，而该页面本身发布更早。

# 20 附表 A：本篇出现的术语中英对照

| 英文（Apple 原文用词） | 中文（Apple 中文页用词） | 出处层 | 备注 |
|---|---|---|---|
| Pairing | 配对 | T1 蓝牙页 | 创建一个或多个共享密钥的过程 |
| Bonding | 绑定 | T1 蓝牙页 | EN 侧仅蓝牙页出现（4.2） |
| Authentication | 认证 | T1 蓝牙页 | 验证两设备持有相同密钥 |
| Encryption | 加密 | T1 蓝牙页 | 消息机密性 |
| Message integrity | 消息完整性 | T1 蓝牙页 | 防止消息伪造 |
| Secure Simple Pairing | 安全简易配对 | T1 蓝牙页 | 防被动窃听 + 防中间人 |
| Secure Connections | 安全连接 | T1 蓝牙页 | 蓝牙 4.1 加入经典蓝牙物理传输 |
| Numerical Comparison | 数字比较 | T1 蓝牙页 | 用户辅助型 |
| Passkey Entry | 密钥接入 | T1 蓝牙页 | 用户辅助型 |
| Controller | 控制器 | T1 蓝牙页 | AES-CCM 的执行位置 |
| identity resolving key (IRK) | 身份解析密钥 | T1 蓝牙页 | 私有地址由其生成 |
| cross-transport key derivation | 交叉传输密钥派生 | T1 蓝牙页 | BLE 链接密钥可派生经典链接密钥 |
| escrow keybag | 保险库密钥（包） | T1 物理配对页 | 由主机持有的 256 位密钥解锁 |
| pairing record | 配对记录 | T1 物理配对页 | 30 天未使用即过期 |
| Secure Remote Password (SRP) | 安全远程密码协议 | T1 物理配对页 | Apple TV 无线配对 |
| smart connector | 智能接点 | T1 安全激活页 | 接口枚举之一 |
| Secure Element (SE) | 安全元件 | T1 NFC 页 | Java Card 平台，EMVCo + Common Criteria |
| in-field terminal | 在场终端 | T1 NFC 页 | 仅其请求被标记为非接触交易 |
| Application Processor | 应用处理器 | T1 NFC 页 | 交易细节不暴露给它 |
| spatial awareness | 空间感知 | T1 超宽带页 | U1 的唯一用途描述 |
| Apple-created peer-to-peer Wi-Fi technology | Apple 开创的点对点无线局域网技术 | T1 AirDrop / 接力页 | **手册层不用 AWDL**（G4） |
| AirDrop short / long identity hash | 隔空投送短 / 长身份标识哈希值 | T1 AirDrop 页 | 两级，先探测后确认 |
| quarantine flag | （措辞层英文原句，无中文对应） | T2 AirDrop 条目 | CVE-2024-54564 |
| DSID (Destination Signaling Identifier) | 目的地信令标识符 | T1 智能互助页 | 与 iCloud 账户绑定并周期性更换 |
| IDS (Apple Identity Service) | Apple 身份服务 | T1 智能互助 / Apple Pay 接力页 | 手册层用英文 IDS 缩写 |
| ECDH (Curve25519) / Ed25519 | 同名 | T1 智能互助页 | 手册层少见的显式曲线命名 |
| privileged network position | 处于特权网络位置 | T2 措辞 | 近场族主导句式（19 / 148） |
| physical proximity | 物理邻近 | T2 措辞 | **仅 Wi-Fi 族**（14 段） |
| Beacon Protection | 同名（未译） | T2 措辞 | CVE-2024-40856 的修复说明 |
| crafted Bluetooth / Wi-Fi packets | 精心构造的蓝牙 / Wi-Fi 数据包 | T2 措辞 | CVE-2026-20650 / CVE-2026-28994 |
| bypass pairing | 绕过配对 | T2 措辞 | **仅 CVE-2024-44124 一处** |
| in Bluetooth range / within Bluetooth range | 在蓝牙范围内 | T2 措辞 | **仅耳机固件页** |
| data redaction | 数据编辑（脱敏） | T2 措辞 | CVE-2025-43354 |
| reachable assertion | 可触发的断言 | T2 措辞 | CVE-2026-43667 |

# 21 附表 B：顾问页近场族条目索引（页号｜产品与版本｜段落｜CVE｜致谢｜条目备注）

下表是本篇引用的全部条目的**核对表**。页号即 `support.apple.com/ht2<页号>` 的数字部分；未标注 CVE 的行是纯致谢段落。

| 页号 | 产品与版本 | 段落 | CVE | 致谢 | 条目备注 |
|---|---|---|---|---|---|
| 120881 | watchOS 10.4 | CoreBluetooth - LE | CVE-2024-23250 | Guilherme Rambo |  |
| 120882 | tvOS 17.4 | CoreBluetooth - LE | CVE-2024-23250 | Guilherme Rambo |  |
| 120893 | iOS 17.4 and iPadOS 17.4 | Bluetooth | CVE-2024-23277 | Marc Newlin of SkySafe | Entry added March 7, 2024 |
| 120893 | iOS 17.4 and iPadOS 17.4 | CoreBluetooth - LE | CVE-2024-23250 | Guilherme Rambo | Entry added March 7, 2024 |
| 120893 | iOS 17.4 and iPadOS 17.4 | AirDrop | （无 CVE） | Cristian Dinca（高中生，罗马图尔·维亚努国家计算机科学中学） |  |
| 120895 | macOS Sonoma 14.4 | Bluetooth | CVE-2024-23277 | Marc Newlin |  |
| 120895 | macOS Sonoma 14.4 | CoreBluetooth - LE | CVE-2024-23250 | Guilherme Rambo |  |
| 120903 | macOS Sonoma 14.5 | Wi-Fi | （无 CVE） | Adam M. | Entry added July 29, 2024 |
| 120907 | AirPods 固件 6A326 / 6F8、Beats 固件 6F8 | Bluetooth | CVE-2024-27867 | Jonas Dreßler | **非 iOS 条目** |
| 120909 | iOS 17.6 and iPadOS 17.6 | AirDrop | CVE-2024-54564 | Linwz of DEVCORE | Entry added March 20, 2025 |
| 120911 | macOS Sonoma 14.6 | AirDrop | CVE-2024-54564 | 同上 |  |
| 120915 | visionOS 1.3 | AirDrop | CVE-2024-54564 | 同上 |  |
| 121238 | macOS Sequoia 15 | Core Bluetooth | （无 CVE） | Nicholas C. of Onymos Inc. |  |
| 121238 | macOS Sequoia 15 | Wi-Fi | CVE-2024-44227 / 2024-54546 / 2024-40770 / 2024-23237 / 2024-44134 / 2024-40856 | Tim Michaud；Antonio Zekic 与 ant4g0nist；Yiğit Can YILMAZ；Charly Suchanek；（44134 无署名）；Preet Dsouza 与 Domien Schepers | 部分条目 Entry added March 3, 2025 |
| 121248 | tvOS 18 | Wi-Fi | CVE-2024-40856；另 CVE-2024-44227 / 2024-54546 | 同上 |  |
| 121250 | iOS 18 and iPadOS 18 | Core Bluetooth | CVE-2024-44124 | Daniele Antonioli |  |
| 121250 | iOS 18 and iPadOS 18 | Core Bluetooth | （无 CVE） | Nicholas C. of Onymos Inc. |  |
| 121250 | iOS 18 and iPadOS 18 | Wi-Fi | CVE-2024-44227 / CVE-2024-40856 | Tim Michaud；Preet Dsouza 与 Domien Schepers |  |
| 121837 / 121839 / 121843 / 121845 | iOS 18.2 / macOS 15.2 / watchOS 11.2 / visionOS 2.2 | Bluetooth | （无 CVE） | Sophie Winter | Entry added March 17, 2025 |
| 122069 / 122070 | macOS Sonoma 14.7.3 / Ventura 13.7.3 | Wi-Fi | CVE-2025-43374 | jioundai of 360；chen fengjiao of HBC | Entry added November 21, 2025 |
| 122371 | iOS 18.4 and iPadOS 18.4 | AirDrop | CVE-2025-24097 | Ron Masas of BREAKPOINT.SH |  |
| 122371 | iOS 18.4 and iPadOS 18.4 | Handoff | CVE-2025-30463 | mzzzz__ |  |
| 122371 / 122373 / 122376 / 122377 | iOS 18.4 / macOS 15.4 / watchOS 11.4 / tvOS 18.4 | Handoff | （无 CVE） | Kirin and FlowerCode |  |
| 122373–122405 | macOS 15.4 / macOS 14.7.5 / watchOS 11.4 / tvOS 18.4 / iPadOS 17.7.7 | AirDrop | CVE-2025-24097 | Ron Masas |  |
| 122404 / 122405 | iOS 18.5 and iPadOS 18.5 / iPadOS 17.7.7 | Core Bluetooth | CVE-2025-31212 | Guilherme Rambo |  |
| 122404 / 122405 | 同上 | AirDrop | （无 CVE） | Dalibor Milanovic |  |
| 122404 / 122405 | 同上 | Wi-Fi | CVE-2025-31216 / CVE-2025-43374 | Thibaud Kehler；jioundai 与 chen fengjiao | CVE-2025-31216 Entry added November 21, 2025 |
| 122716 / 122720 / 122721 / 122722 | macOS 15.5 / tvOS 18.5 / visionOS 2.5 / watchOS 11.5 | Core Bluetooth | CVE-2025-31212 | Guilherme Rambo |  |
| 122716–122722 | 同上 | Wi-Fi | CVE-2025-43374 | jioundai、chen fengjiao |  |
| 124147–124155 | iOS 18.6、macOS 15.6 / 14.7.7 / 13.7.7、tvOS 18.6、visionOS 2.6、watchOS 11.6 | Bluetooth | （无 CVE） | LIdong LI, Xiao Wang, Shao Dong Chen, Chao Tan of Source Guard |  |
| 124147 / 124149–124155 | 同上 | afclip | CVE-2025-43186 | Hossein Lotfi of Trend Micro ZDI |  |
| 125108 / 125110 / 125114 / 125115 / 125116 | iOS 26、macOS Tahoe 26、tvOS 26、visionOS 26、watchOS 26 | Bluetooth | CVE-2025-43354 | — |  |
| 125108–125116 | 同上 | Core Bluetooth | （无 CVE） | Leon Böttger | Entry added November 3, 2025 |
| 125110 | macOS Tahoe 26 | Bluetooth | （无 CVE） | Yiğit Can YILMAZ 与 Csaba Fitzl (@theevilbit) of Iru | **Entry updated December 12, 2025** |
| 125110 | macOS Tahoe 26 | Core Bluetooth | （无 CVE） | Nathaniel Oh (@calysteon) |  |
| 125108–125116 | iOS 26 / iOS 18.7 / macOS 26 / 15.7 / 14.8 等 | Wi-Fi | （无 CVE） | Aobo Wang、Csaba Fitzl of Kandji、Noah Gregory、Wojciech Regula、匿名研究者 |  |
| 125634 / 125635 / 125636 | macOS 26.1 / 15.7.2 / 14.8.2 | Wi-Fi | CVE-2025-43373 | Wang Yu of Cyberserval |  |
| 126346–126353 | iOS 26.3、iOS 18.7.5、macOS 26.3 / 15.7.4 / 14.8.4、visionOS 26.3 | Bluetooth | CVE-2026-20650 | jioundai |  |
| 126346 / 126348 / 126351–126353 | 同上 | Bluetooth | （无 CVE） | Tommaso Sacchetti |  |
| 126346–126353 | 同上 | Wi-Fi | CVE-2026-20621 | Wang Yu of Cyberserval |  |
| 126792 / 126794 / 126797–126799 | iOS 26.4、macOS 26.4、tvOS 26.4、watchOS 26.4、visionOS 26.4 | Bluetooth | （无 CVE） | Hamid Mahmoud |  |
| 126792–126796 | iOS 26.4、iOS 18.7.7、macOS 26.4 / 15.7.5 / 14.8.5 | Clipboard | CVE-2026-28866 | Cristian Dinca (icmd.tech) |  |
| 126792–126799 | 五条产品线 | Wi-Fi | （无 CVE） | Kun Peeks (@SwayZGl1tZyyy)、匿名研究者 |  |
| 126792–126799 | 五条产品线 | Wi-Fi Connectivity | （无 CVE） | Alex Radocea of Supernetworks, Inc |  |
| 126794 | macOS Tahoe 26.4 | Core Bluetooth | （无 CVE） | Nathaniel Oh (@calysteon) |  |
| 127110 | iOS 26.5 and iPadOS 26.5 | AirDrop | CVE-2026-43667 | Arash Ale Ebrahim / CISPA SCy-Phy | Entry added August 25, 2026 |
| 127110 | iOS 26.5 and iPadOS 26.5 | CoreBluetooth - LE | （无 CVE） | Luke Noble | Entry added August 25, 2026 |
| 127110–127119 | iOS 26.5 / 18.7.9、macOS 26.5 / 15.7.7 / 14.8.7、tvOS 26.5、watchOS 26.5 | Wi-Fi | CVE-2026-28994 | Alex Radocea |  |
| 127111 / 127115 / 127116 / 127117 | iOS 18.7.9（iPhone XS / XS Max / XR / iPad 7）、macOS 26.5 / 15.7.7 / 14.8.7 | Wi-Fi | **CVE-2026-28819** | Wang Yu | 近场族里唯一 kernel privileges 条目，**遗留分支** |
| 127115 / 127119 / 127120 | macOS 26.5 / watchOS 26.5 / visionOS 26.5 | AirDrop | CVE-2026-43667 | 同 127110 | Entry added August 25, 2026 |
| 127119 | watchOS 26.5 | CoreBluetooth - LE | （无 CVE） | Luke Noble | Entry added August 25, 2026 |
| 127557 | **Beats 固件更新 1B211（Beats Studio Buds）** | Bluetooth | CVE-2025-20701 | Dennis Heinze and Frieder Steinmetz of ERNW GmbH | Apple 注明"开源代码 + CVE 由第三方分配" |
| 128066–128070 / 148287 | iOS 26.6、macOS 26.6、watchOS 26.6、tvOS 26.6、visionOS 26.6、iOS 18.7.10 | Wi-Fi | CVE-2026-64726 | Mathis Mansière、Peter Malone | Impact：物理邻近 → 破坏进程内存 |
| 148287 | iOS 18.7.10 and iPadOS 18.7.10 | AirDrop | CVE-2026-43667 | 同 127110 | 遗留分支获补丁 |

# 22 附表 C：本篇引用的手册层页面清单

| 页面标识 | 中文页标题 | 中文页发布日期 | 本篇章节 | 英文取样 |
|---|---|---|---|---|
| `sec82597d97e` | 蓝牙安全性 | 2021-02-18 | 第 1、2、3、14 章 | 有 |
| `secadb5b6434` | iPad 和 iPhone 的物理配对模型安全性 | 2024-12-19 | 第 6、7、19 章 | 有 |
| `sec5044aad1b` | 安全激活数据连接 | 2024-05-07 | 第 8、19 章 | 有 |
| `sec70a4f377d` | 为 Apple 设备和服务验证配件 | 2024-05-07 | 第 8.5 节（仅登记） | 有 |
| `secda20f3f41` | NFC 与 SE 平台安全性 | 2024-12-19 | 第 6、9、19 章 | 有 |
| `sec1e6108efd` | iOS 中的超宽带安全性 | 2021-02-18 | 第 10 章（整页） | 有 |
| `sec2261183f4` | 隔空投送安全性 | 2021-02-18 | 第 11 章 | 有 |
| `secd2015ef35` | 无线局域网密码共享安全性 | 2024-12-19 | 第 12.1 章 | 有 |
| `sec8839a8de2` | 将密码发送给其他用户或 Apple 设备 | 2021-02-18 | 第 12.2 章 | 有 |
| `secf78dbe639` | 接力安全性 | 2021-02-18 | 第 12.3 章 | 有 |
| `seca4b33e8c9` | 智能热点安全性 | 2021-02-18 | 第 12.4 章 | 有 |
| `sec6ab47ebfc` | 自动解锁 Apple 设备 | 2026-01-28 | 第 12.4 章 | 有 |
| `secce267dc10` | 连续互通安全性概览 | 2021-02-18 | 第 4.3 节（AWDL 对照） | 有 |
| `sec025128f1b` | 服务安全性概览 | 2021-02-18 | 附表 C 登记（连续互通归入服务安全章） | 有 |
| `secfd166f620` | Apple 设备的无线局域网安全性 | 2024-12-19 | 第 17.2 节（G13） | 有 |
| `sec31e483abf` | Apple 设备的无线局域网隐私 | 2024-12-19 | 第 4.2 节随机化对照 | 有 |
| `secf64471c16` | iOS 中的车钥匙安全性 | 2026-01-28 | 第 10.4 节（仅登记） | 有 |
| `secfbd5c0e54` | 通过 Apple Pay 使用付款卡支付 | 2024-12-19 | 第 12.3 节 IDS 对照 | 有 |

**"有"的含义受 G11 约束**：英文取样共 18 页，全量结论只在中文 179 页口径内成立。

# 23 附表 D：普查方法与复现口径

1. **手册层中文全量**：179 个页面正文区（`==H1==` / `==DATE==` / `==BODY==` 三段式转储），统计前剥离导航与目录。日期字段取"发布日期"。
2. **手册层英文取样**：端点 `security-pdf/<sec-hash>/1/web/1` 返回英文正文；正文须从 "Copyright and trademarks" 之后截取，否则会把整块目录计入。共 18 页。
3. **顾问页语料**：257 份英文顾问页；按组件标题切块后统计段落名与词频。段落内词频为"段落级出现次数 + 命中段落数"两列。
4. **切分口径**：同一条 CVE 跨多页会形成多段，因此**"段落数" ≠ "缺陷数"**。本篇凡给计数都注明是段落口径。
5. **词形**：全小写精确词组匹配（含连字符变体分别计数，如 man-in-the-middle 与 man in the middle）。
6. **未做**：中文顾问页对照、CVSS 取值、KEV 交叉、第三方研究结论核对（G12）。

# 24 附表 E：允许写 / 不许写对照表

| 常见写法 | 本篇判定 | 依据 |
|---|---|---|
| "Apple 手册把蓝牙安全分为配对、绑定、认证、加密、消息完整性、安全简易配对六项" | **可写** | 第 1.1 节 T1 原文 |
| "BLE 的随机地址让第三方难以追踪设备" | **可写，但须补一句**"已配对方仍能解析" | 第 2.3 节 |
| "蓝牙链路密钥存放在安全隔区里" | **不可写** | G7：手册层未说 |
| "iPhone 的蓝牙绑定 30 天后失效" | **不可写** | G8：30 天属物理配对记录 |
| "AirDrop 通过蓝牙传文件" | **不可写** | 第 11.3 节：BLE 只发信号，数据走点对点 Wi-Fi + TLS |
| "AirDrop 所有人模式不再验证身份" | **不可写** | 第 11.3 节步 4：模式只改变是否响应 |
| "UWB 采用加密时间戳序列所以抗中继" | **不可写** | G2：手册层无任何 UWB 安全表述 |
| "AWDL 是 AirDrop 使用的接口" | **仅可标第三方 / 开发者口径** | G4：手册层不用该词 |
| "某蓝牙漏洞被在野利用" | **不可写** | G9：近场族 actively exploited = 0 |
| "Apple 说这条是中间人攻击" | **不可写** | 第 13.2：man-in-the-middle 0 次 |
| "Apple 给了这条 CVE 的 CVSS 分值" | **不可写** | S7：顾问页不给分值 |
| "iOS 内核存在蓝牙远程代码执行漏洞" | **不可写** | 第 13.3：蓝牙族 kernel = 0；且组合三是 app 前置 + 遗留分支 |
| "Wi-Fi 组件里有一条内核代码执行条目（CVE-2026-28819，iOS 18.7.9 / iPhone XS 等机型）" | **可写** | 第 17.3 组合三，须带机型与分支 |
| "Beats 耳机的麦克风缺陷也影响 iPhone" | **不可写** | 第 15.4：Available for 只有耳机 |
| "Apple 修复了蓝牙加密缺陷" | **不可写** | 第 13.2：近场族修复句式里没有 encryption |
| "恶意蓝牙输入设备可绕过配对（CVE-2024-44124，iOS 18）" | **可写** | 第 16.2，且须补"Apple 未说明涉及哪种配对模式" |
| "配对过程需要用户响应" | **可写** | 第 3.1 节 T1 原句 |
| "Apple 的蓝牙防中间人能力完全依赖用户是否真的比对数字" | **可写成"手册层的防 MITM 手段只有两种用户辅助方法"**，不写成"完全依赖" | 第 3.1 节：T1 未说其它保障不存在 |

---

**边界声明：** 本报告是一份面向防御与安全研究的机制与口径分析。全文材料只来自两类 Apple 公开文本——《Apple 平台安全保护》用户安全手册（中文 179 页全量 + 英文 18 页取样）与 Apple 安全响应顾问页的公开条目（257 份英文页面），加上对这两类文本自身的计数。报告不提供任何漏洞利用代码、不提供概念验证程序、不描述配对截获、中继、距离欺骗、身份哈希破解或任何针对他人设备的操作步骤，不构造攻击链、不给出绕过任何具体防线的实施方法。第 13–17 章列出的 CVE 条目只用于说明 Apple 在公开文本里"把缺陷归到哪个组件、用哪个句式描述后果、以及没有用哪些句式"，不构成对任何机型当前可被攻破的判断；第 19 章的控制点全部对应 Apple 手册层已经写明的设置项与既有机制，属常规配置与补丁核对，不是对抗手段。凡报告中出现的"可以""能够""允许""意味着"，若加了 T1 标记即为 Apple 原文措辞的直接引用或从原文结构可直接读出的内容；若未加标记则为本报告的分析判断，须以第 18 章缺口登记为限重新核对。本报告明确声明：源码层（XNU）本期不可用，因此全部关于实现层位置的表述均已降级为"顾问页归类"；凡第 18 章登记的十三项缺口，本报告均未作出结论。

Telegram: https://t.me/one00190
