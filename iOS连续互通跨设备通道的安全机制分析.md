@@TITLE@@iOS 连续互通跨设备通道的安全机制分析
@@SUBTITLE@@接力的 BLE 4.2 带外配对与 AES-256-GCM 广告、AirDrop 的两级身份哈希、通话中继里那条"通知也走 iMessage 式端对端加密"，以及顾问页 230 个近场段落里 kernel 只出现在 Wi-Fi 段落（容错解析重测版，含一次推翻自己两条"0 结论"的自我纠错）
@@INFO@@报告日期|2026 年 10 月 26 日
@@INFO@@分析对象|《Apple 平台安全保护》zh-CN 指南 179 页全量转储 + 英文侧 20 页取样（连续互通概览、接力、隔空投送、通话中继、短信转发、智能互助、自动解锁、无线局域网密码共享、Apple Pay、服务安全性概览）；Apple 安全响应顾问页 257 份英文页面转储，其中连续互通相关组件段落 230 个（AirDrop 20、Handoff 6、Bluetooth 68、Core Bluetooth 25、CoreBluetooth - LE 11、Wi-Fi 79、Wi-Fi Connectivity 9、afclip 7、Clipboard 5）——本文初稿曾按严格正则得到 160 个，差额全部落在标记带后缀的 10 页，见第 12.1 与 13.5 节
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含针对他人设备的操作步骤、不含广告字节解密或身份哈希碰撞的实施细节
@@INFO@@分类|跨设备通道安全 / 近场发现与远端中继并存 / 官方手册层措辞与顾问页措辞对照
@@INFO@@难度|进阶（第 1 章的四层文本分层是全篇前提；跳过它会把手册层的类比句读成协议声明）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

"连续互通"（Continuity）是 Apple 用得最松、也最容易被误读的一个词。它听起来像一个协议，实际上是一组能力；它听起来只发生在设备之间，实际上有一半的通道要靠 Apple 的服务器中转；它听起来是"靠近就能用"，实际上手册层在每一条通道上都写了前置条件——同一个 Apple 账户、双重认证、通讯录里有对方、设备已解锁、App 在前台。

本篇要给出的，是把这七条通道逐一拆到"哪一段用了什么介质、哪一步由谁认证、密钥放在哪里、Apple 有没有给时限"这个颗粒度，然后回答第二件事：**当这些通道出问题的时候，Apple 在公开的安全响应文本里是怎么说的——以及它不说什么。**

第二个问题比第一个更有信息量。因为手册层的语言全是能力语言（配对、加密、端对端、安全隔区），而顾问页的语言全是后果语言（一个 App 可以……、处于特权网络位置的攻击者可以……）。这两套语言之间几乎没有交集，而交集的大小恰好能告诉我们：Apple 自己承认的连续互通缺陷，集中在哪些环节、回避了哪些环节。

## 0.1 本篇的实测数字

下表是全部结论的原始分母，2026-10-26 重测，不引用二手统计。

| 数字 | 是什么 | 分母与口径 |
|---:|---|---|
| 1 | 手册层"连续互通安全性概览"页的正文句子数 | `secce267dc10` 整页正文只有一句 |
| 7 | 本篇逐条拆解的连续互通通道数 | 接力、AirDrop、通话中继、短信转发、智能互助、Apple Pay 接力、自动解锁（另加两类近场凭据下发） |
| 256 / 4.2 | 接力广告密钥位长 / 所用的 BLE 加密版本 | `secf78dbe639`："symmetric 256-bit AES key"、"standard BLE 4.2 encryption" |
| 2048 | AirDrop 所使用的 iCloud RSA 身份位长 | `sec2261183f4`："a 2048-bit RSA identity is stored on the device" |
| 2 | AirDrop 身份哈希的层级数 | 同页：short identity hash（BLE 广播）→ long identity hash（点对点 Wi-Fi） |
| 160 → **230** | 顾问页里本篇计入的连续互通相关组件段落总数 | 257 份英文页面转储，按组件标题切块。**160 是本文初稿按严格正则得到的数**，容错解析后为 230（蓝牙 / 近场身份族 130 + Wi-Fi / 本地解析族 100），差额 70 段全部落在标记带后缀的 10 页（第 12.1、13.5 节） |
| 9,045 / 470 | 容错解析后的全语料段落总数 / 段落名总数 | 同一批 257 份镜像；严格解析只得 7,615 段 / 439 名，静默漏采 1,430 段（第 13.5 节） |
| **0** | 这 230 个段落里 "actively exploited"、"man-in-the-middle"、"eavesdrop"、"sandbox"、"zero-click"、"encrypt"、"in the wild" 各自的命中次数 | 容错解析下的段落内检索，两族同时为 0，见第 13 章 |
| 8 / 1 | "remote attacker" / "root privileges" 在蓝牙族的新命中段落数 | **本文初稿把这两个词也写在上面那行 0 里**；重测后全部落在 `Bluetooth` 段落下新增的两条 CVE（第 13.2 节结论二已撤销） |
| 23 / 0 | "kernel" 一词在 Wi-Fi 段落族的命中段落数 / 在蓝牙 + AirDrop + Handoff 段落族的命中段落数 | 100 段 vs 130 段，容错口径重测 |
| 0 | 手册层 179 页中出现 AWDL、Apple Wireless Direct Link、Nearby Interaction 的页数 | 中文全量 179 页；英文取样 20 页亦为 0 |

倒数第二行是本篇的落点：**Apple 的措辞层里，"内核"这个词属于 Wi-Fi 段落，不属于接力或隔空投送段落。** 所以本篇凡是谈"连续互通的内核面"，证据只能取自 Wi-Fi / afclip / Clipboard 段落（第 15 章），不能挂在 AirDrop 或 Handoff 上。这一行在重测中不仅没有动摇，分母还从 82 段涨到 130 段——**多出来的 48 段里，"kernel" 依旧是 0**。

但同一张表也记录了本篇最难堪的一次更正：初稿把 `remote attacker` 和 `root privileges` 也写进了那行 0。重测之后前者变成 8 段、后者变成 1 段，全部落在 `Bluetooth` 段落名下 2026 年 10 月新增的条目里。也就是说，**"近场族从不写远端攻击者"这句曾经的主轴论点已经不再成立**，第 13.2 节把它整条撤销，并在第 13.5 节登记原因。

## 0.2 四层文本与本篇的使用规则

| 层 | 具体文本 | 能支撑什么 | 不能支撑什么 |
|---|---|---|---|
| **T1 手册层** | 《Apple 平台安全保护》（zh 全量 179 页 / EN 取样 20 页） | 机制存在、算法与位长、密钥存放位置、默认值、前置条件、用户可控开关 | 任何缺陷、任何可达性、任何"可被攻破"的判断 |
| **T2 措辞层** | 顾问页组件段落 + Impact + Description + 致谢 + 条目备注 | 某 CVE 被归到哪个组件、Apple 怎么描述后果与修复类别、致谢对象、条目增改日期 | 缺陷类别、CVSS 分值、在野状态、实现层位置 |
| **T3 源码层** | XNU 与用户态开源部件 | 进程归属、调用关系、符号 | **本期不可用**：本地源码镜像为空目录，未取回任何符号（第 17 章 G1） |
| **T4 第三方层** | 厂商威胁报告、会议论文、CVE 分配方 | 研究结论、命名事件、评分 | Apple 是否认可——不可反推 |

三条硬规则：

1. **跨层引用必须显式标层。** 手册层说"这些消息的加密方式与 iMessage 类似"，这是一句**类比**，不是算法声明。本篇每次引用它都会带上"手册层以 iMessage 作类比"这一层。
2. **T3 缺失即降级。** 凡"某进程属于内核 / 某框架在用户态"的句子，本篇一律写成"顾问页把它归入某个组件段落名之下"。
3. **T4 只在具名且已核实时使用。** 本篇出现的机构名（CISPA SCy-Phy、360 漏洞研究院、Trend Micro Zero Day Initiative、Cyberserval、BREAKPOINT.SH、icmd.tech）全部来自 T2 致谢字段本身；**这些机构的研究结论一条也不写**（第 17 章 G12）。

## 0.3 怎么读这篇

- 想知道每条通道"谁发现、谁认证、谁传输"：第 2–9 章逐条，第 10 章给合图。
- 想知道"Apple 有没有说这些通道的密钥存在哪"：第 2.1、4.1、9.2、9.6 节，以及第 17 章缺口登记。
- 想知道"哪些关于连续互通的流行说法在官方文本里没有对应句式"：第 13 章普查、第 16 章结构性事实、附表 E 对照表。
- 只想要可引用的条目表：附表 B（顾问页条目索引）与附表 C（手册层页清单，含 `sec<hash>` 与发布日期）。

# 1 连续互通在手册层里的位置：一句话定义，加上它归属哪一章

## 1.1 整页只有一句的概览页

`secce267dc10`（连续互通安全性概览，中文页发布日期 2021-02-18）剥去导航与目录后，正文只有一句：

> Continuity takes advantage of technologies like iCloud, Bluetooth, and Wi-Fi to enable users to continue an activity from one device to another, make and receive phone calls, send and receive text messages, and share a cellular internet connection.

这一句给出三件事，本篇后面全部围绕它展开：

1. **介质清单：iCloud、蓝牙、Wi-Fi。** 注意 iCloud 被列在第一位——手册层自己就把"服务器/账户体系"当作连续互通的组成，而不是把它当旁路。
2. **四类活动：** 跨设备接续活动、接打电话、收发短信、共享蜂窝网络。本篇第 2 章（接续活动）、第 6 章（接打电话与收发短信）、第 7 章（共享蜂窝网络）逐条对应，另以第 5 章 AirDrop 作为近场传输对照。
3. **它不是一个协议。** 原文用 "takes advantage of technologies like…"，是能力集合的写法。

## 1.2 它被归在哪一章

`sec025128f1b`（服务安全性概览）的章述原文：

> This chapter covers security technologies used in iCloud, Sign in with Apple, Apple Pay, Apple Wallet, iMessage, Apple Messages for Business, FaceTime, Find My, and Continuity.

**Continuity 被放进"服务安全"章，与 iCloud、iMessage、FaceTime 并列，而不是放进无线或蓝牙所在的章节。** 这条归类事实本篇会反复用到，它的含义是：**手册层自己就把连续互通视为一个依赖账户与服务体系的能力**，这解释了为什么第 2 章的接力配对要走 APNs、第 6 章的通话中继通知要走 APNs、第 8 章的 Apple Pay 接力要走 IDS——**"跨设备"在 Apple 的实现里，很多时候是"跨设备 + 经过 Apple 的服务"。**

## 1.3 本篇据此确立的分析框架

七条通道全部按同一套四问拆解，这也让第 10 章的合图成为可能：

| 问 | 为什么要问 |
|---|---|
| 谁发起发现？用什么介质？ | 决定"未认证观察者能看到什么" |
| 谁做身份认证？依据是什么？ | 决定"信任从哪里来"——通讯录？账户？证书？ |
| 数据实际走哪条链路？ | 决定"加密在哪一段生效"——BLE？点对点 Wi-Fi？APNs？ |
| 密钥存在哪里？有没有时限？ | 决定"一次泄露的影响半径与持续时间"——这一问手册层最常沉默 |

# 2 接力（Handoff）：带外配对、对称广告密钥与"重放保护"这半句

接力是七条通道里手册层写得最细的一条，也是本篇的主证据页。`secf78dbe639`（接力安全性，中文页发布日期 2021-02-18）。

## 2.1 配对是带外建立的，通道是 APNs

手册层原文：

> When a user signs in to iCloud on a second Handoff-capable device, the two devices establish a Bluetooth Low Energy (BLE) 4.2 pairing **out-of-band using APNs**. The individual messages are encrypted much like messages in iMessage are. After the devices are paired, each device generates a symmetric 256-bit AES key that gets stored in the device's keychain. This key can encrypt and authenticate the BLE advertisements that communicate the device's current activity to other iCloud paired devices using AES-256 in GCM mode, with replay protection measures.

拆成五个独立事实：

| # | 事实 | 原文依据 |
|---|---|---|
| 1 | 触发条件是"在第二台支持接力的设备上登录 iCloud" | "When a user signs in to iCloud on a second Handoff-capable device" |
| 2 | 建立的是一次 **BLE 4.2 配对**，但**带外**完成，介质是 APNs | "establish a … BLE 4.2 pairing out-of-band using APNs" |
| 3 | 消息层另有一层加密，手册层用 iMessage 类比 | "The individual messages are encrypted much like messages in iMessage are" |
| 4 | 配对后**每台设备各自生成一把 256 位对称 AES 密钥**，存放于**钥匙串（keychain）** | "each device generates a symmetric 256-bit AES key that gets stored in the device's keychain" |
| 5 | 这把密钥同时用于广告的**加密与认证**，模式为 **AES-256-GCM**，并**带重放保护措施** | "encrypt and authenticate the BLE advertisements … using AES-256 in GCM mode, with replay protection measures" |

**事实 2 是整条通道里最容易被误读的一步。** 一般直觉认为 BLE 配对要在空口上协商（numeric comparison / passkey），而手册层写的是"带外、用 APNs"。也就是说，**两台设备之间的 BLE 配对密钥不是用户在两台设备上比对数字得来的，而是在 iCloud 登录这个既有信任关系之上，经 Apple 的推送通道完成的。** 这件事的含义本篇在第 10 章会与其它通道合起来讲：在连续互通里，**"配对"往往是既有账户关系的一次落地，而不是一次独立的身份验证。**

**事实 4 的"钥匙串"要精确引用。** 手册层写的是 keychain，不是 secure enclave。与之对照：第 9 章自动解锁页明确写了密钥材料"保留在安全隔区内"；第 8 章 Apple Pay 接力写了"这些密钥 Apple 也无法获取"。**接力页两处都没这样写。** 因此本篇的口径结论是：

> 手册层对接力广告密钥给出的存放位置是**设备钥匙串**（T1 原文），未说它是否进入安全隔区；同一份手册里另有两条通道被明确写进安全隔区。这三者不可互相推定。

**事实 5 里"with replay protection measures"是全手册层唯一一次在近场广播语境下提到重放保护。** 第 17 章 G4 会把它登记为缺口——手册层没有说明其余广播通道（Wi-Fi 口令共享的请求广播、热点的 DSID 广播、通话中继的停铃广播）是否也带重放保护，因此接力页这半句不可外推。

## 2.2 广告密钥换新时，第一次接收方要主动建链

> The first time a device receives an advertisement from a new key, it establishes a BLE connection to the originating device and performs an advertisement encryption key exchange. This connection is secured using standard BLE 4.2 encryption as well as encryption of the individual messages, which is similar to how iMessage is encrypted. **In some situations, these messages are sent using APNs instead of BLE.** The activity payload is protected and transferred in the same way as an iMessage.

四点：

1. **广告与连接是两种参与方式。** 收到新密钥的广告时，接收方**主动建立 BLE 连接**做一次广告加密密钥交换。也就是说光"听到广告"不足以解密活动详情，需要完成一次连接侧交换。
2. **这条连接上有两层**：BLE 4.2 标准加密 + 消息层独立加密。
3. **APNs 是 BLE 的备用承载**——"In some situations, these messages are sent using APNs instead of BLE"。手册层没有说明是哪些情形。
4. **活动载荷的保护与传输"与 iMessage 相同"**（第三次于用这个类比）。

第 3 点是本篇"跨设备其实常常经过服务器"这条主线的第一处硬证据。**同一条接力的密钥交换消息，介质可能是 BLE，也可能是 APNs**，两者都在手册层同一页的同一句里出现。任何把接力描述成"纯本地、离线、不经 Apple"的写法都与这句原文冲突。

## 2.3 手册层的三个类比句，以及它们不能用来支撑什么

同页出现的三次"像 iMessage 一样"：

| 出现处 | 原文 | 能支撑 | 不能支撑 |
|---|---|---|---|
| 消息层加密 | encrypted much like messages in iMessage are | 存在一层独立于 BLE 的消息加密 | 算法、密钥层级、是否端对端 |
| 连接上的消息 | similar to how iMessage is encrypted | 同上，作用在连接建立阶段 | 同上 |
| 活动载荷 | protected and transferred in the same way as an iMessage | 载荷的保护与传输复用同一套机制 | iMessage 的密钥托管、PGK、Secure Recovery 等是否一并复用 |

**这是本篇最需要读者接受的一条限制：手册层用类比代替声明。** 类比句可以支撑"这里还有一层加密"，**不能**支撑"这是端对端加密"。真正被手册层明确写成 end-to-end 的，本篇范围内只有两处：第 6 章的通话中继通知，与第 8 章的 Apple Pay 接力 IDS。英文取样 20 页里 "end-to-end" 共 4 次 / 3 页，**接力页一次也没有。**

# 3 接力的另一半：原生 App 与网页之间的接力，以及手册层自认的开放点

## 3.1 域名控制是准入条件

> Handoff allows an iOS, iPadOS, or macOS native app to resume user activity on a webpage in domains legitimately controlled by the app developer. … To help prevent native apps from claiming to resume websites not controlled by the developer, the app must demonstrate legitimate control over the web domains it wants to resume. **Control over a website domain is established using the mechanism for shared web credentials.** … The system must validate an app's domain name control before the app is permitted to accept user activity Handoff.

三个可读事实：

1. 这个方向的能力是"**原生 App 可以在网页上恢复活动**"，即 App ↔ Web 双向接力，而不只是设备 ↔ 设备。
2. 准入判据不是新的，**复用的是共享网页凭据（shared web credentials）那套域名控制机制**——同一套"你能证明这个域名归你吗"的判断被两个不相关的功能共用。手册层还给了指引："For details, see App access to saved passwords."
3. 校验时机被写死了：**"before the app is permitted to accept"**——先验证，后允许接收。

## 3.2 域名在广告字节里，而广告字节只有本人设备能解

> The source of a webpage Handoff can be any browser that has adopted the Handoff APIs. When the user views a webpage, the system advertises the domain name of the webpage in the encrypted Handoff advertisement bytes. **Only the user's other devices can decrypt the advertisement bytes.**

这句话是接力页里对"未认证观察者能看到什么"最直接的回答：**广播内容里确实带着域名，但它在加密广告字节内，只有同一用户的其它设备能解。** 结合第 2.1 节事实 5（同一把密钥做加密 + 认证 + GCM + 重放保护），接力广播的机密性与完整性都由那把 256 位对称密钥承担。

同页另一句关于接收侧：

> On a receiving device, the system detects that an installed native app accepts Handoff from the advertised domain name and displays that native app icon as the Handoff option. When launched, the native app receives the full URL and the title of the webpage. **No other information is passed from the browser to the native app.**

**"只传 URL 和标题，别的不传"** 是手册层给出的显式信息最小化声明。本篇把它登记为连续互通各通道里少有的、明确划定数据边界的句子。

## 3.3 手册层自己写下的开放点：fallback URL 不受域名控制约束

> In the opposite direction, a native app may specify a fallback URL when a Handoff receiving device doesn't have the same native app installed. In this case, the system displays the user's default browser as the Handoff app option (if that browser has adopted Handoff APIs). When Handoff is requested, the browser is launched and given the fallback URL provided by the source app. **There is no requirement that the fallback URL be limited to domain names controlled by the native app developer.**

这一句在整页里的地位特殊，因为它是**手册层主动写出的不对称**：

| 方向 | 是否要求域名控制 | 原文 |
|---|---|---|
| 网页 → 原生 App（App 想接收） | **要求**，且必须先验证 | "The system must validate an app's domain name control before…" |
| 原生 App → 浏览器（App 指定回退地址） | **不要求** | "There is no requirement that the fallback URL be limited to domain names controlled by…" |

本篇对这两句的处理，是整篇方法论的缩影：

- **可以写**：手册层在同一个功能里对两个方向给出了不同的域名控制要求，并明确说明回退方向不受此约束（T1 两处原文）。
- **不可以写**：因此存在一个可以把我导向钓鱼站的漏洞。这个推论需要两步——(a) 攻击者能控制源 App 或诱使用户触发接力；(b) 用户无法分辨。而**措辞层里没有任何一条 Handoff 条目涉及 fallback URL**（Handoff 段 6 段，1 个 CVE，且是 App 侧数据访问，见第 14 章）。手册层写下的是"没有这个要求"，不是"这会造成什么"。

# 4 大载荷接力与通用剪贴板：链路会换，默认值会变

## 4.1 大数据量时链路迁移到点对点 Wi-Fi

> In addition to using the basic feature of Handoff, some apps may elect to use APIs that support sending larger amounts of data over **Apple-created peer-to-peer Wi-Fi technology (much like AirDrop)**. For example, the Mail app uses these APIs to support handoff of a mail draft, which may include large attachments.
> When an app uses these APIs, the exchange between the two devices starts off just as in Handoff. But, **after receiving the initial payload using Bluetooth Low Energy (BLE), the receiving device initiates a new connection over Wi-Fi. This connection is encrypted (with TLS), and it derives trust through an identity shared through iCloud Keychain.** The identity in the certificates is verified against the user's identity. Further payload data is sent over this encrypted connection until the transfer is complete.

四点，每点都有原文：

1. **App 需要主动选用**这些 API（"some apps may elect to use"）——不是默认路径。
2. **两段式承载**：BLE 传初始载荷 → 接收方发起新的 Wi-Fi 连接传后续。
3. **Wi-Fi 段用 TLS**，且**信任来自 iCloud 钥匙串共享的身份**，证书身份再与用户身份比对。
4. 手册层的比喻："much like AirDrop"。**第 5 章会看到 AirDrop 的 TLS 与证书交换结构确实一致。**

**这里出现了本篇关于"密钥存放"的第二处精确对照**：接力大载荷的信任来源被写成 **iCloud Keychain**（钥匙串），而不是安全隔区。与第 2.1 节事实 4 一致，与第 9 章自动解锁相反。**手册层内部对三种密钥给了三种不同的存放表述：钥匙串（接力）、iCloud 钥匙串（大载荷接力的信任根）、安全隔区（自动解锁）。** 这不是矛盾，是三个不同对象；本篇拒绝把它们合并成"iOS 的密钥都在 SEP 里"这类句子。

## 4.2 通用剪贴板：默认共享，开发者可选择退出

> Universal Clipboard leverages Handoff to securely transfer the content of a user's clipboard across devices so they can copy on one device and paste on another. Content is protected in the same way as other Handoff data and **is shared by default with Universal Clipboard unless the app developer chooses to disallow sharing.**

> Apps have access to clipboard data regardless of whether the user has pasted the clipboard into the app. With Universal Clipboard, **this data access extends to apps on the user's other devices (as established by their iCloud sign-in).**

这一节里两句的份量都不小，且**都是手册层自己写的**：

| 句 | 手册层声明 | 本篇能写的 |
|---|---|---|
| 默认值 | 剪贴板内容默认可跨设备共享，除非开发者选择禁止 | **默认开启**；退出权在**开发者**手里，不在用户手里（这一句原文的主语是 app developer） |
| 访问条件 | App 无需用户粘贴即可读取剪贴板 | App 侧剪贴板读取不以"用户粘贴过"为前提 |
| 范围扩展 | 上述访问范围随 iCloud 登录扩展到用户**其它设备上的 App** | 通用剪贴板把"本机 App 可读剪贴板"这条既有事实，扩成了"同账户设备上的 App 可读" |

第三行是本节的核心。**手册层没有把通用剪贴板描述成一个新增的机密性机制，而是把它描述成一条既有访问语义的范围扩展。** 这是本篇能给出的、关于剪贴板最有用的一条口径事实，而且它完全来自 T1，不需要任何第三方结论。

同时必须标出：措辞层的 `Clipboard` 段落只有一条 CVE（CVE-2026-28866，第 15.4 节），**且它的 Description 与剪贴板语义并不连贯**。也就是说，手册层这段范围扩展的叙述，**在 T2 层没有任何对应缺陷条目支撑或否定**。本篇把它登记为沉默，而不是结论。

# 5 隔空投送（AirDrop）：BLE 只发信号，Wi-Fi 才传东西

`sec2261183f4`（隔空投送安全性，中文页发布日期 2021-02-18）。

## 5.1 介质与两条显式限定

> Apple devices that support AirDrop use Bluetooth Low Energy (BLE) and Apple-created peer-to-peer Wi-Fi technology to send files and information to nearby devices, including AirDrop-capable iPad and iPhone devices with iOS 7 or later and Mac computers with OS X 10.11 or later. **The Wi-Fi radio is used to communicate directly between devices without using any internet connection or wireless access point (AP). This connection is encrypted with TLS.**

两个限定值得单独抄下来：**"不用互联网、也不用无线接入点"** 与 **"此连接用 TLS 加密"**。前者界定了链路拓扑（设备间直连），后者界定了加密位置（Wi-Fi 段）。**BLE 段没有任何加密声明**——这与第 2 章接力页明确写 BLE 4.2 加密 + GCM 广告形成对比：**同是 BLE 广播，接力页给了算法细节，AirDrop 页没给。**

## 5.2 身份：一把 RSA 身份 + 两级哈希

> When a user signs in to iCloud, a **2048-bit RSA identity** is stored on the device, and when the user turns on AirDrop, an **AirDrop short identity hash** is created based on the email addresses and phone numbers associated with the user's Apple Account.

层级关系很清楚，本篇按原文顺序列：

| 时机 | 产生的对象 | 输入 |
|---|---|---|
| 登录 iCloud | 2048 位 RSA 身份（存在设备上） | iCloud 登录 |
| 打开 AirDrop | 短身份哈希 | 该 Apple 账户关联的**邮箱地址与电话号码** |

**手册层没有给出哈希算法（是否加盐、迭代次数、域参数）。** 这是本篇第 17 章登记的一条缺口（G5），也是所有"短哈希可否被离线爆破"类讨论无法在本篇落地的原因。

## 5.3 流程逐步（照抄手册层，不改写）

| 步 | 手册层原文（浓缩） | 结构含义 |
|---|---|---|
| 1 | the sending device emits an AirDrop signal over BLE that includes the user's AirDrop short identity hash | 广播只带**短哈希**，不带身份 |
| 2 | Other Apple devices that are awake, in close proximity, and have AirDrop turned on, detect the signal and **respond using peer-to-peer Wi-Fi**, so that the sending device can discover the identity of any responding devices | **响应在 Wi-Fi 侧发生**；发现方由此"看到"响应者的身份 |
| 3 | In Contacts Only mode, the received AirDrop short identity hash is compared with hashes of people in the receiving device's Contacts app. If a match is found, the receiving device responds over peer-to-peer Wi-Fi with its identity information. **If there is no match, the device doesn't respond.** | 判据是**接收方本地通讯录**；不匹配则完全静默 |
| 4 | In Everyone mode, the same overall process is used. However, **the receiving device responds even if there is no match** in the device's Contacts app | "所有人"只改变**是否响应**，流程不变 |
| 5 | The sending device then initiates an AirDrop connection using peer-to-peer Wi-Fi, using this connection to send a **long identity hash** to the receiving device | 短 → 长两级，**先在 BLE 上试，再在 Wi-Fi 上证** |
| 6 | If the long identity hash matches the hash of a known person in the receiver's Contacts, then the receiver responds with its long identity hashes | 第二轮仍是**通讯录比对** |
| 7 | the sending device initiates an encrypted (TLS) connection with the receiving device, which **exchanges their iCloud identity certificates**. The identity in the certificates is verified against each user's Contacts app | 证书交换在 TLS 之内；**验证仍然落在通讯录** |
| 8 | If the certificates are verified, **the receiving user is asked to accept the incoming transfer** from the identified user or device. If multiple recipients have been selected, this process is repeated for each destination | 最终由接收方点确认；多收件人则逐个重复 |

**第 3、6、7 步的共同点：AirDrop 的信任判据始终是"我的通讯录里有没有这个人"，不是"这台设备持有可信证书"。** 证书被交换并验证，但**验证的对象是它与通讯录的匹配关系**（"verified against each user's Contacts app"）。这一句是本篇关于 AirDrop 最重要的一条读法——它决定了"和一个陌生人互投"这件事在机制上意味着什么：第 4 步之后，双方**没有**通讯录匹配，于是第 6、7 步的比对必然不成立，最终呈现方式由 5.4 节那一段决定。

## 5.4 未验证设备如何显示：手册层唯一一处用户可视化区分

> If the hashes are verified, the recipient's first name and photo (if present in Contacts) are displayed in the sender's AirDrop share sheet. On devices with iOS and iPadOS, they are shown in the "People" or "Devices" section. **Devices that aren't verified or authenticated are displayed in the sender's AirDrop share sheet with a silhouette icon and the device's name, as defined in Settings > General > About > Name. They're placed in the "Other People" section of the AirDrop share sheet.**

这段给出三件可直引的界面事实：

1. 已验证 → 显示名字与照片（若通讯录里有），归入 "People" 或 "Devices" 分组；
2. 未验证 / 未认证 → **剪影图标 + 设备自设名**（来源被写得很具体：设置 > 通用 > 关于本机 > 名称）；
3. 未验证设备被放进 **"Other People"** 分组。

**这是手册层在整个连续互通章节里，唯一一处告诉用户"你可以自己看出对方没有被验证"。** 本篇据此可以写一条有 T1 依据的操作建议（第 18.2 节）：接收侧的可视化区分是设计的一部分，用户教育里应当包含它。

同样重要的是边界：原文给的是**分类**（verified / not verified），**不是风险评级**。**不得写成"显示剪影就代表攻击"或"显示名字就代表可信"。** 后者只说明哈希与通讯录匹配上了。

## 5.5 用户与组织侧的两道开关

> AirDrop is set to share with Contacts Only by default. Users can also choose to use AirDrop to share with everyone, or turn off the feature entirely. Organizations can restrict the use of AirDrop for devices or apps being managed by using a device management service.

三条：默认"仅限联系人"；用户可放开到"所有人"或整体关闭；**组织可通过设备管理服务限制**。第 7.1 节的 Wi-Fi 密码共享页出现了句式几乎相同的一句（「针对通过设备管理服务管理的设备或 App，组织可以限制无线局域网密码共享的使用」），**说明设备管理是手册层在近场通道上重复给出的同一道管控。** 同时记下负结果：第 7.2 节的凭证发送页与第 7.3 节的 Apple TV 填充页**没有**给出"组织可限制"这句——手册层的管控覆盖不均匀，不要假定所有近场通道都能用设备管理关掉。
# 6 通话中继与短信转发：服务器只负责把通知送到，媒体在近场直连

这一组通道最容易被误读成"所有内容都经过 Apple 服务器"。手册层给的其实是一个明确的分工：**信令走 APNs，媒体走两台设备之间的点对点连接**。下面逐句把这一层分工拆出来。

## 6.1 前置条件比"同一 Apple 账户"多一项

英文原句（`sec28a79bf17`，中文页发布日期 2021 年 2 月 18 日）：

> When a user's Mac, iPad, or HomePod is on the same Wi-Fi network as their iPhone, it can make and receive phone calls using the cellular connection on iPhone. Configuration requires the devices to be signed in to both iCloud and FaceTime using the same Apple Account.

中文对照：「当用户的 Mac、iPad 或 HomePod 与其 iPhone 接入相同的无线局域网时，便可以使用 iPhone 的蜂窝网络连接来拨打和接听电话。这样的配置要求使用相同的 Apple 账户，同时登录 iCloud 和 FaceTime 通话。」

拆出三个条件，缺一不可：同一 Wi-Fi 网络；同一 Apple 账户；该账户在 **iCloud 与 FaceTime 两项服务上都已登录**。本篇后面凡写到"同一账户的设备"，都要记得通话中继这一条还多一个 FaceTime 登录条件——这是第 10 章合图里"账户关系不等于服务关系"的实例。

## 6.2 通知走 APNs，但通知本身被端对端加密

> When an incoming call arrives, all configured devices are notified using the Apple Push Notification service (APNs), with each notification using the same end-to-end encryption as iMessage.

中文对照：「来电时，会通过 Apple 推送通知服务 (APNs) 来通知所有已配置的设备，每个通知都会使用与 iMessage 信息相同的端对端加密技术。」

这句话有两个层次，必须分开记：

| 层次 | 手册层说了什么 | 手册层没说什么 |
|---|---|---|
| 承载 | 通知由 APNs 投递，即经过 Apple 的推送基础设施 | APNs 在此链条里看到什么元数据（投递目标、时间、大小） |
| 内容 | 「每个通知都会使用与 iMessage 信息相同的端对端加密技术」 | 端对端的具体密钥持有者与算法参数，手册层在此页未展开，只以 iMessage 为类比 |

第二行的缺口在第 17 章登记。此处只强调一条口径纪律：**"通知经过 APNs"不能推出"通知内容 Apple 可读"**，反过来也不能凭这句话断言 Apple 完全看不到任何元数据——手册层只写了载荷加密。

## 6.3 接通之后：音频在近场直连里传

> When the user answers the call, the audio is seamlessly transmitted from the user's iPhone using a secure peer-to-peer connection between the two devices.

去电同样如此：「去电也会使用 APNs 中继到 iPhone，并通过安全的点对点链接在设备间采用类似的方式传输音频。」

也就是说，通话中继的架构是**双通道**：APNs 负责"叫谁来接"，点对点连接负责"接起来之后说的话"。手册层用的是 "secure peer-to-peer connection"（安全的点对点连接）——注意它在这里**没有**给出 TLS 之类的具体协议名，这与接力大载荷段（第 4.1 节明确写了 TLS）不同。这一处差异要如实记下：手册层对音频链路的加密只写到"安全"这个形容词。

## 6.4 一处跨页引用：停止响铃的广播字节复用接力的加密方法

这是本篇最有价值的一句，因为它证明**广告加密是一个被复用的原语，而不是每个功能各写一套**：

> When a call is answered on one device, ringing of nearby iCloud-paired devices is terminated by briefly advertising using Bluetooth Low Energy (BLE). The advertising bytes are encrypted using the same method as Handoff advertisements.

中文对照：「当在一台设备上接通电话时，该设备使用低功耗蓝牙 (BLE) 进行短暂广播，使附近的 iCloud 配对设备停止响铃。广播的字节使用与"接力"广播相同的方法来加密。」

三个可读事实：
1. 停铃靠**短暂广播**，不靠建立连接；
2. 广播是"附近的 iCloud 配对设备"，即作用域由账户配对关系界定；
3. 字节加密方法 = 接力广告的方法，也就是第 2 章那个 256 位对称 AES 密钥 + AES-256-GCM + 重放保护。

由此可以合理归纳（并标注为归纳而非 Apple 原文）：**能伪造停铃广播，需要先持有该账户设备的接力广告密钥**。而手册层从未说明这把密钥的轮换周期与备份形态（缺口 G4）。

## 6.5 关停入口在哪：FaceTime 设置，不在"设置"顶层

> Users can turn off phone call relay on a device by turning off iPhone Cellular Calls in FaceTime settings.

中文对照：「用户可以在 FaceTime 通话设置中关闭"iPhone 蜂窝网络通话"来关闭设备的电话中继功能。」

运维含义在第 18 章展开：这条开关**按设备**生效，而且位置在 FaceTime 的设置页里，不在通用设置顶层。审计时只看"设置"根页面会漏掉它。

## 6.6 短信转发：注册有两种路径，转发用的是 iMessage 那套方法

英文原句（`sec16bb20def`，中文页发布日期 2021 年 2 月 18 日）：

> Text Message Forwarding automatically sends SMS text messages received on an iPhone to a user's enrolled iPad or Mac. Each device must be signed in to the iMessage service using the same Apple Account. When Text Message Forwarding is turned on, enrollment is automatic on devices within a user's circle of trust if two-factor authentication is enabled. Otherwise, enrollment is verified on each device by entering a random six-digit numeric code generated by iPhone.

中文对照：「"短信转发"会自动将 iPhone 上接收的短信发送到用户注册的 iPad 或 Mac 上。每台设备均须使用相同的 Apple 账户登录 iMessage 信息服务。"短信转发"启用后，如果启用了双重认证，用户信任圈内的设备会自动注册。否则，会要求在每台设备上输入由 iPhone 随机生成的 6 位数字代码来验证注册。」

注册路径因此是二选一：

| 条件 | 注册方式 | 用户是否需要逐设备操作 |
|---|---|---|
| 启用双重认证，且设备在用户信任圈内 | 自动注册 | 否 |
| 不满足上述 | iPhone 生成随机 6 位数字代码，逐台设备输入验证 | 是 |

转发本身：

> After devices are linked, iPhone encrypts and forwards incoming SMS text messages to each device, utilizing the methods described in iMessage security overview. Replies are sent back to iPhone using the same method, and then iPhone sends the reply as a text message using the carrier's SMS transmission mechanism.

这里有一个必须写清楚的边界，也是这一节存在的意义：**被加密的是"转发链路"，不是"短信这种业务本身"**。手册层的说法是 iPhone 用《iMessage 信息安全性概览》所述方法把短信加密后转发给其它设备；回复则经同一方式回到 iPhone，再由 iPhone 使用**运营商的短信传输机制**发出。也就是说，链条的一端始终是运营商 SMS——那一端的可见性由运营商决定，不由 Apple 的转发加密决定。把"短信被端对端加密转发"读成"这条短信是端对端加密的"是越界。

关停入口：「用户可以在"信息"设置中打开或关闭"短信转发"。」

---

# 7 近场凭据通道三例：Wi-Fi 密码、已存网站凭证、热点连接信息

第 5 章的 AirDrop 身份识别机制不是孤例。手册层把它**至少复用到三种凭据的下发**上，本章逐个给出原句，并在 7.4 做对照表。这一组的重要性在于：它们都是"用近场完成身份识别，然后把一件敏感东西交出去"，因此共享同一套信任假设——一旦 AirDrop 那套 short/long 哈希的口径有变，这些通道会一起受影响。

## 7.1 无线局域网密码共享：四段原文

`secd2015ef35`，中文页发布日期 2024 年 12 月 19 日。整页四段，逐段可引：

> Apple devices that support Wi-Fi password sharing use a mechanism similar to AirDrop to send a Wi-Fi password from one device to another.

> When a user selects a Wi-Fi network (the requestor) and is prompted for the Wi-Fi password, the Apple device starts a Bluetooth Low Energy (BLE) advertisement indicating that it wants the Wi-Fi password. Other Apple devices that are awake, in close proximity, and have the password for the selected Wi-Fi network connect using BLE to the requesting device.

> The device that has the Wi-Fi password (grantor) requires the Contact information of the requestor, and the requestor must prove their identity using a process that's similar to AirDrop. After identity is proven, the grantor sends the requestor the passcode.

> Organizations can restrict the use of Wi-Fi password sharing for devices or apps managed through a device management service.

中文对照（第四段）：「针对通过设备管理服务管理的设备或 App，组织可以限制无线局域网密码共享的使用。」

结构上这是 AirDrop 流程的**单向化版本**：请求方 BLE 广播"我要密码"→ 附近持有该网络密码的设备连上来 → 授予方要求请求方的联系信息并验证身份 → 验证通过后单向下发口令。第 5.3 节的八个步骤在这里压缩成四步，但"通讯录前置 + AirDrop 式身份验证"这个核心没变。

一处口径纪律：手册层在这里用的是 "requires the Contact information of the requestor"（要求请求方的联系信息），与 AirDrop 的"把收到的短身份哈希与接收方通讯录里的哈希比对"**措辞不同**。不要把两处合并写成同一套比对算法——手册层没在 Wi-Fi 密码共享页复述哈希比对细节，只说"使用与隔空投送类似的过程来证明自己的身份"。

## 7.2 通过隔空投送发送已存凭证：手册层罕见地写"无论用户设置如何"

`sec8839a8de2`（英文取样页；中文页名「将密码发送给其他用户或 Apple 设备」）：

> When iCloud is enabled, users can use AirDrop to send a saved credential to another device. The credential includes the user's name and password and the websites it's saved for. **Sending credentials with AirDrop always operates in Contacts Only mode, regardless of the user's settings.** On the receiving device, after user consent, the credentials are stored in the user's Password AutoFill Keychain.

中文对照：「iCloud 启用时，用户可使用隔空投送将已存凭证发送到另一台设备。凭证包括用户名和密码，以及其所对应的网站。**无论用户的设置如何，通过隔空投送发送凭证操作始终在"仅限联系人"模式下进行。**在接收设备上，用户同意后，凭证会储存在用户的"自动填充密码"钥匙串中。」

加粗那句在本篇的分量很重，因为它是手册层里**极少数明确声明"忽略用户当前设置"的句子**。它意味着：即使用户把 AirDrop 临时切到"所有人"（第 5.5 节），凭证这一类载荷仍然只走"仅限联系人"路径。这是一种按**数据类型**而非按**用户开关**分级的设计，正文可以据此写一条判断：AirDrop 在这套文档里不是一个通道，而是**按载荷敏感度分级的多个通道**。

同时记下接收侧还有一道"用户同意"（after user consent），落地位置是"自动填充密码"钥匙串。

## 7.3 Apple TV 的密码自动填充：同账户自动加密，不同账户用 PIN

> Password AutoFill is available to fill credentials in apps on Apple TV. When the user focuses on a user name or password text field in tvOS, Apple TV begins advertising a request for Password AutoFill over Bluetooth Low Energy (BLE).

> Any nearby iPad or iPhone displays a prompt inviting the user to share a credential with Apple TV. Here's how the encryption method is established:
> - If the device and Apple TV uses the same iCloud account, encryption between the devices happens automatically.
> - If the device is signed in to an iCloud account other than the one used by Apple TV, the user is prompted to establish an encrypted connection through use of a PIN code. To receive this prompt, iPhone must be unlocked and in close proximity to the Siri Remote paired to that Apple TV.

> After the encrypted connection is made using BLE link encryption, the credential is sent to Apple TV and is automatically filled in to the relevant text fields on the app.

中文对照（第二条与承载句）：「如果设备登录的 iCloud 账户与 Apple TV 使用的不同，用户会收到通过使用 PIN 码来建立加密连接的提示。iPhone 必须解锁，并且靠近与该 Apple TV 配对的 Siri Remote 遥控器，才能收到此提示。」「使用 BLE 链接加密建立加密的连接后，凭证会发送到 Apple TV，并自动填充到 App 中相关的文本栏内。」

这一例给出三种此前没见过的要素：
1. **触发方式是 UI 焦点**——用户在 tvOS 里聚焦到用户名或密码文本栏，设备就开始广播请求。也就是说请求不是用户显式发起的，是界面焦点驱动的。
2. **附近所有 iPad 或 iPhone 都会显示提示**（Any nearby iPad or iPhone displays a prompt），手册层没有把提示范围限制在同一账户。
3. **跨账户时的信任建立方式是 PIN**，并且额外要求 iPhone 处于解锁状态、且靠近**与该 Apple TV 配对的 Siri Remote**。这最后半句是本篇唯一一处把"某个第三方配件的存在"写进解锁/授权前置条件的连续互通句子。
4. 承载加密被写成 "BLE link encryption"（BLE 链接加密）——这与第 3 章的 AES-256-GCM 广告加密是不同层次，属于链路层。

## 7.4 智能互助（个人热点）：DSID 广播 + 两套不同的密钥协商

`seca4b33e8c9`，中文页发布日期 2021 年 2 月 18 日。这一页是本篇唯一在同一页里给出**两套不同密码学路径**的近场通道。

发现阶段：

> Initially, when a user enters Wi-Fi settings on a device, it emits a BLE advertisement containing an identifier that all devices signed in to the same iCloud account agree upon. The identifier is generated from a DSID (Destination Signaling Identifier) that's tied to the iCloud account and rotated periodically. When other devices signed in to the same iCloud account are in close proximity and support Personal Hotspot, they detect the signal and respond, indicating the availability to use Instant Hotspot.

中文对照：「最初，当用户在设备上进入无线局域网设置时，设备会发起一个 BLE 广播，其中包含所有登录同一 iCloud 账户的设备都能认同的标识符。该标识符由绑定到该 iCloud 账户且定期轮换的 DSID（目标信令标识符）生成。」

同一账户开头还有一句范围界定：支持的设备「use Bluetooth Low Energy (BLE) to discover and communicate to all devices that have signed in to the same individual personal Apple Account or other personal Apple Accounts used with Family Sharing (in iOS 13 and iPadOS 13.1)」，兼容的 Mac（OS X 10.10 或更高）用同一技术发现并通信。

非家庭共享的连接请求：

> When a user who isn't part of Family Sharing chooses an iPad or iPhone for Personal Hotspot, a request to turn on Personal Hotspot is sent to that device. The request is sent across a link that's encrypted using BLE encryption, and the request is encrypted in a fashion similar to iMessage encryption. The device then responds across the same BLE link using the same per-message encryption with Personal Hotspot connection information.

家庭共享的连接信息共享——这一句是全篇 IDS 用法的重点：

> For users that are part of Family Sharing, Personal Hotspot connection information is securely shared using a mechanism similar to that used by HomeKit devices to sync information. Specifically, the connection that shares hotspot information between users is secured with an **ECDH (Curve25519) ephemeral key** that's authenticated with the users' respective device-specific **Ed25519 public keys**. The public keys used are those that had previously synced between the members of Family Sharing **using IDS** when the Family Share was established.

拆成表格：

| 场景 | 发现 | 链路层 | 消息层 | 密钥认证 |
|---|---|---|---|---|
| 同一 iCloud 账户 | 含 DSID 派生标识符的 BLE 广播，账户级、定期轮换 | BLE 加密 | 手册层未在此页展开 | — |
| 非同账户、非家庭共享（用户选择某台设备的个人热点） | 同上 | BLE 加密链路 | 「以类似 iMessage 加密的方式」加密请求；响应回传连接信息也用同一逐消息加密 | 手册层未展开 |
| 家庭共享成员之间 | 同上 | 手册层未展开 | 连接信息共享 | ECDH(Curve25519) 临时密钥，由成员各自的**设备级 Ed25519 公钥**认证；这些公钥在建立家庭共享时**通过 IDS** 同步 |

三点判断（第 10 章会汇总）：
- 这是手册层里唯一一处把 IDS 明确写成**跨设备公钥同步载体**的连续互通段落。它说明 IDS 在文档口径中承担的角色不止是身份目录，还包括成员间公钥分发。
- 家庭共享路径与 HomeKit 同步机制**同类**（"a mechanism similar to that used by HomeKit devices to sync information"）——这是手册层自己做的类比，不是本报告推测。
- "rotated periodically"（定期轮换）没有给出周期数值。DSID 轮换频率、轮换后旧标识符的失效窗口，手册层无表述（缺口 G5）。

## 7.5 四种凭据、四种"证明你是谁"的方式

| 载荷 | 发现方式 | 身份证明方式（手册层原句归纳） | 交出去的动作 | 手册层给的开关 |
|---|---|---|---|---|
| Wi-Fi 网络口令 | 请求方 BLE 广播"我要密码" | 授予方要求请求方联系信息 + 「与隔空投送类似的过程」 | 授予方单向下发口令 | 组织可用设备管理服务限制 |
| 已存网站凭证（用户名/密码/对应网站） | 复用 AirDrop | 「始终在"仅限联系人"模式下进行，无论用户设置如何」 | 接收方用户同意后存入"自动填充密码"钥匙串 | 无独立开关（依附 iCloud） |
| Apple TV 的自动填充凭证 | Apple TV 因 UI 焦点而 BLE 广播请求 | 同 iCloud 账户→自动加密；不同账户→PIN 码 + iPhone 解锁 + 靠近配对的 Siri Remote | 经 BLE 链接加密发送，自动填充到文本栏 | 手册层此页未给 |
| 个人热点连接信息 | 含 DSID 派生标识符的 BLE 广播 | 账户一致性（DSID 为账户级）；家庭共享则用设备级 Ed25519 公钥认证 | 经 BLE 加密链路与逐消息加密回传 | 手册层此页未给 |

四条合起来给出本篇的一个结构性观察：**Apple 在这些通道上"证明你是谁"的手段，全部依赖账户关系或通讯录关系，而不是依赖一次独立的、可撤销的设备级授权**。也就是说，一旦某个账户下的某台设备被纳入信任范围，这四类凭据的下发路径会同时可用；反过来，用户能撤销的东西也只有账户与通讯录（第 18 章的检测清单据此展开）。手册层没有为这四类凭据提供"逐通道授权"的设置项——这是负结果，不是我的推断。

---

# 8 Apple Pay 接力：手册层唯一一处写明"这些密钥 Apple 也拿不到"

## 8.1 承载协议被点名：端对端加密的 IDS

`secfbd5c0e54`（Apple Pay 安全性概览，中文页发布日期 2024 年 12 月 19 日）里，接力是一段附属于支付章节的内容：

> Apple Pay transactions can also start on a Mac and be completed on an Apple Pay-enabled iPhone or Apple Watch using the same iCloud account. If the user is transmitting payment-related information this way, Apple Pay Handoff uses the **end-to-end encrypted Apple Identity Service (IDS) protocol** to transmit payment-related information between the user's Mac and the authorizing device. The IDS client on Mac uses the user's device keys to perform encryption so no other device can decrypt this information. **These keys aren't available to Apple.**

中文对照：「如果用户以这种方式传输支付相关信息，Apple Pay 接力会使用经过端对端加密的 Apple 身份服务 (IDS) 协议，在用户的 Mac 与授权设备之间传输支付相关信息。Mac 上的 IDS 客户端使用用户的设备密钥来执行加密，因此没有其他设备可以解密这些信息。**这些密钥不提供给了 Apple。**」

最后一句在整部手册里的位置需要交代清楚：它是手册层少见的、直接对"Apple 自身能看到什么"作出否定的句子。它同时限定得很窄——**说的是 Apple Pay 接力这条路径上用于加密的设备密钥**，不是所有 IDS 密钥，也不是所有 iCloud 数据。第 8.4 节处理这个外推边界。

## 8.2 设备发现阶段广播什么、不广播什么

> Device discovery for Apple Pay Handoff contains the type and unique identifier of the user's credit cards along with some metadata. The Device Account Number of the user's card isn't shared, and it continues to remain stored securely on the user's iPhone or Apple Watch. Apple also securely transfers the user's recently used contact, shipping, and billing addresses over iCloud Keychain.

手册层在这里给出一张少见的"内容枚举表"，正面列出发现阶段包含什么：卡片类型、卡片唯一标识符、部分元数据；负面列出不包含什么：**设备的 Device Account Number 不共享**，继续安全保存在用户的 iPhone 或 Apple Watch 上。另外，最近使用的联系人、收货与账单地址经 iCloud 钥匙串传输。

这一节对本篇的意义不在支付本身，而在方法论：它是手册层**唯一一处把某个连续互通通道的发现阶段载荷逐项列出**的地方。第 5 章 AirDrop 只列了短身份哈希，第 7.4 节热点只列了 DSID 派生标识符。因此"近场发现广播里到底放了什么"这个问题，只有 Apple Pay 接力这一条能给出可读清单——其余通道应写成缺口，不能拿这份清单去类推。

同一页另一句关于"卡片唯一标识符"的性质需要引在这里，避免读者把它当成 PAN 的同义词（中文页可直引）：「每个卡号的密文替代项都会安全地储存在设备上的安全隔区内。Device Account Number 不会同步到 iCloud，也不会提供给 Apple。」也就是说，第 8.2 节里"被共享的标识符"与"被藏在 SEP 里的 Device Account Number"是两个不同的东西。

## 8.3 这条通道与其它接力在结构上的两点差别

| 维度 | 通用接力（第 2–4 章） | Apple Pay 接力（本节） |
|---|---|---|
| 承载 | BLE 4.2 加密 + 消息层加密；APNs 在部分情形下作备用承载 | 手册层写"经过端对端加密的 IDS 协议"，未提 BLE 承载 |
| 密钥位置 | 256 位对称广告密钥存于**钥匙串** | 加密使用"用户的设备密钥"，且明写"这些密钥不提供给了 Apple" |
| 发现载荷 | 广告字节承载"当前活动"或网页域名，仅本人设备可解 | 卡片类型 + 唯一标识符 + 元数据；Device Account Number 排除在外 |
| 授权端 | 接收活动的设备本身 | 另一台设备作为"授权设备"（authorizing device） |

第四行值得多说一句：手册层在这里把 iPhone/Apple Watch 称为 **authorizing device**（授权设备），这是全篇唯一一处把接力描述成"一台设备发起、另一台设备授权"的关系，而不是"两台设备接续同一活动"。它与第 9 章自动解锁里"发起端/目标端"的划分是同一类结构。

## 8.4 不许外推的一条

"end-to-end encrypted IDS" 与 "These keys aren't available to Apple" 只出现在 Apple Pay 接力这一段。**不得**把它们外推到：通用接力的活动载荷、通用剪贴板、AirDrop、热点连接信息、通话中继通知。后四者的手册层措辞分别是"类似 iMessage 的加密"（第 2 章）、TLS + iCloud 钥匙串身份（第 4.1 节）、与 iMessage 相同的端对端加密（第 6.2 节）——这些是各页独立写的句子，本篇按页引用，不做统一。这条纪律来自第 10.3 节要处理的"类比句不是一个算法声明"问题。

---

# 9 自动解锁与 iPhone 镜像：STS 隧道在两台安全隔区之间，唯一的例外是"没有安全隔区的 Mac 在内核里终止它"

这一页是本篇结构最完整的一页，也是手册层**唯一一处把连续互通的密钥材料明确放回安全隔区**的地方（对照第 2 章：接力的广告密钥只说"储存在设备钥匙串中"）。中文页发布日期 2026 年 1 月 28 日，是第 1 章表格里最新的一页。

## 9.1 五种解锁关系

英文页列出五个使用场景（中文对照照抄）：

| 关系 | 谁解锁谁 |
|---|---|
| Apple Watch 由 iPhone 解锁 | iPhone → Apple Watch |
| Mac 由 Apple Watch 解锁 | Apple Watch → Mac |
| 用户检测到口鼻被遮挡时，iPhone 由 Apple Watch 解锁 | Apple Watch → iPhone |
| iPhone 由 Apple Vision Pro 解锁 | Vision Pro → iPhone |
| iPhone 被解锁并在 Mac 上查看（iPhone 镜像） | Mac → iPhone |

## 9.2 共同基础：相互认证的 STS 协议 + 长期密钥在启用时交换

> All use cases are built upon the same basic foundation: a mutually authenticated Station-to-Station (STS) protocol, with Long-Term Keys exchanged at time of feature enablement and unique ephemeral session keys negotiated for each request. Regardless of the underlying communication channel, the STS tunnel is negotiated directly between the Secure Enclaves in both devices, and all cryptographic material is kept within that secure domain (**with the exception of Mac computers without a Secure Enclave, which terminate the STS tunnel in the kernel**).

中文对照：「所有使用场景都建立在相同的基础上：相互认证的站对站 (STS) 协议，它会在启用功能时交换长期密钥，并为每个请求协商唯一的临时会话密钥。无论使用哪种底层通信通道，STS 隧道都是在两台设备中的安全隔区之间直接协商，且所有加密材料都保留在该安全域内（未搭载安全隔区的 Mac 电脑除外，此类 Mac 电脑会在内核中终止 STS 隧道）。」

这一句给出四个可拆的事实，以及一个全篇独有的例外：
1. STS 是**相互认证**的，两端都要证明身份；
2. 长期密钥（Long-Term Keys）在**功能启用时刻**交换，不是每次会话协商；
3. **每个请求**协商唯一的临时会话密钥——即前向性由"每请求一次临时密钥"提供；
4. 承载方式与隧道终结点**解耦**："无论使用哪种底层通信通道"，STS 隧道都在两个 SEP 之间直接协商；
5. 例外：未搭载安全隔区的 Mac，STS 隧道**在内核中终止**。

第 5 点是本篇第 12 章"内核这个词只出现在哪里"的关键素材之一——注意它来自 **T1 手册层**，而不是顾问页措辞层。措辞层里蓝牙/AirDrop/Handoff 段落的 kernel 计数是 0（第 13 章实测），而手册层的自动解锁页明确写了 "terminate the STS tunnel in the kernel"。这两件事不矛盾，但必须分别标层：手册层说的是"某类 Mac 上密钥材料的处理位置下沉到内核"，顾问页从不把连续互通条目归到内核组件段落下。

## 9.3 两个前提与"逐条启用"

> To unlock one device with another, both devices need to be signed into the same Apple Account using two-factor authentication, and the user needs to enable each specific unlocking relationship between the two devices.

中文对照：「要用另一台设备解锁一台设备，两台设备都需要使用双重认证登录相同的 Apple 账户，并且用户需要在两台设备之间启用每一种具体的解锁关系。」

"enable each specific unlocking relationship" 是这一节最要紧的运维事实：**解锁关系不是随账户自动开放的，每一种都要单独启用**。第 18 章的审计项直接来自这句。

## 9.4 解锁序列：32 字节随机解锁密钥、PDK 封装、用完丢弃

手册层把完整序列拆成两阶段（发起端 initiator / 目标端 target），准备与执行各有细节：

> To prepare for devices to automatically unlock, the devices connect to each other using a Bluetooth Low Energy (BLE) connection. Then a 32-byte unlock secret randomly generated by the target device is sent to the initiator over the STS tunnel. During the next biometric or passcode unlock, the target device wraps its passcode-derived key (PDK) with the unlock secret and discards the unlock secret from its memory.

> To perform the unlock, the devices initiate a new BLE connection and then use peer-to-peer Wi-Fi to securely approximate the distance between each other. If the devices are within the specified range and the required security policies are met, the initiator sends its unlock secret to the target through the STS tunnel. The target then generates a new 32-byte unlock secret and returns it to the initiator. If the current unlock secret sent by the initiator successfully decrypts the unlock record, the target device is unlocked and the PDK is rewrapped with a new unlock secret. Finally, the new unlock secret and PDK are then discarded from the target's memory.

把它整理成时序：

| 步骤 | 动作 | 承载 | 手册层的原句要点 |
|---|---|---|---|
| 准备-1 | 两台设备建立 BLE 连接 | BLE | 「设备之间会使用低功耗蓝牙 (BLE) 建立连接」 |
| 准备-2 | 目标设备随机生成 32 字节解锁密钥，经 STS 隧道发给发起端 | STS 隧道 | 密钥由**目标**生成 |
| 准备-3 | 下一次生物识别或密码解锁时，目标用该解锁密钥封装 PDK，并从内存丢弃解锁密钥 | — | 「丢弃其内存中的解锁密钥」 |
| 执行-1 | 发起新的 BLE 连接 | BLE | 每次解锁都是新连接 |
| 执行-2 | 使用点对点无线局域网**安全估算**彼此距离 | 点对点 Wi-Fi | 「安全估算彼此之间的距离」——测距方法未公开（缺口 G3） |
| 执行-3 | 在范围内且满足安全策略时，发起端经 STS 隧道发送解锁密钥 | STS 隧道 | 距离与策略是两个并列门槛 |
| 执行-4 | 目标生成新的 32 字节解锁密钥返回发起端 | STS 隧道 | **每次解锁后密钥更换** |
| 执行-5 | 当前解锁密钥能成功解密解锁记录 → 目标解锁，PDK 用新解锁密钥重新封装 | — | 判定条件是"能否解密" |
| 执行-6 | 目标内存中的新解锁密钥与 PDK 被丢弃 | — | 结束时同样丢弃 |

三个可归纳的性质（标注为归纳）：密钥**一次一换**（准备一次、每次执行后再换一次）；两端内存最终都不保留；封装对象是 **PDK**（密码派生密钥），所以这条通道传递的是"能否解开解锁记录"的能力，而不是设备密码本身。

第 6 步的"估算距离"值得单独强调：手册层用的是 "securely approximate"（安全估算），既没有说这是超宽带测距，也没有说测距误差范围（除下一节的门限值外）。第 9.5 节的距离门限是**策略数值**，不是测距精度声明。

## 9.5 每种关系的策略门槛

| 关系 | 手册层列出的全部条件 |
|---|---|
| Apple Watch 解锁 Mac | 戴上并解锁 Apple Watch 后 Mac 至少用其它方式解锁过一次；Mac 与 Watch 距离不超过 2–3 米；Watch 处于解锁状态；Watch 不能处于睡眠模式 |
| Apple Watch 解锁 iPhone | 戴上并解锁 Watch 后 iPhone 至少用其它方式解锁过一次；iPhone 在**过去 6.5 小时内**解锁至少一次；Watch 或 iPhone 最近解锁过，或 Watch 检测到佩戴者活动（例如未睡眠）；传感器能检测到**口鼻被遮挡**；距离不超过 2–3 米；Watch 不能处于睡眠模式；iPhone 处于允许面容 ID 执行解锁的状态 |
| Apple Vision Pro 解锁 iPhone | iPhone 自启动后至少用其它方式解锁过一次；Vision Pro 已解锁且正在使用；Vision Pro 通过光学检测到 iPhone 距用户**不超过约一米**且**用户正注视该 iPhone**；测量距离不超过约一米 |

第二行的"口鼻被遮挡"条件容易被误读。按手册层原文，它是这一解锁关系的**必要条件**（对应英文 "Sensors need to be able to detect that the nose and mouth are covered"），也就是这套解锁是为戴口罩场景设计的——而不是"必须戴口罩才能解锁"的日常要求。

**锁定命令**（Watch 与 Vision Pro 两条关系各写了一次，句式相同）：

> If the user taps the Lock iPhone button in the notification, the watch sends the iPhone a lock command over BLE. When the iPhone receives the lock command, it locks and disallows both Face ID and unlock using other devices. The next iPhone unlock needs to be performed with the iPhone passcode.

中文对照：「如果用户轻点通知中的"锁定 iPhone"按钮，手表会通过 BLE 向 iPhone 发送锁定命令。iPhone 收到锁定命令时，会锁定且不允许面容 ID 和使用其他设备解锁。下一次解锁 iPhone 需要使用 iPhone 密码。」

这一句同时给出**能力边界**：「Apple Watch 无法用于代替 iPhone 上的面容 ID 进行其他操作，如 Apple Pay 或 App 授权。」（Vision Pro 那条同样声明不能代替面容 ID 用于 Apple Pay。）也就是说，远端解锁关系**不产生认证断言**，它只解开设备锁，不授权支付与应用内认证。这是本篇第 10 章"信任边界"结论最硬的一条依据。

## 9.6 Apple Watch 由 iPhone 解锁：长期托管记录进 Watch keybag，密钥进 iPhone 钥匙串

> For added convenience, Apple Watch can be unlocked by an iPhone directly after initial startup, without requiring the user to enter the passcode on the Apple Watch itself. To achieve this, the random unlock secret (generated during the very first unlock sequence after enablement of the feature) is used to create a long-term escrow record, which is stored in the Apple Watch keybag. The escrow record secret is stored in the iPhone keychain and used to bootstrap a new session after each Apple Watch restart.

这一条与第 9.4 节的"用完即弃"是**相反的持久化策略**：这里的解锁密钥被用来创建**长期托管记录**，存进 Apple Watch 的密钥包（keybag），而托管记录密钥存在 **iPhone 钥匙串**中，用于每次 Watch 重启后重新引导会话。原因是场景不同——Watch 重启时无法自己输入密码。

对手册层口径的影响：本篇因此不能说"自动解锁的所有密钥材料都在安全隔区里"。第 9.2 节那句"所有加密材料都保留在该安全域内"限定在 STS 隧道的密码材料；而这一节的托管记录密钥被明确写进 iPhone 钥匙串。两处都是手册层原文，第 18 章的审计项要按这两个位置分别检查。

## 9.7 iPhone 镜像的通知转发：身份被"固定"，私钥在安全隔区

> iPhone Mirroring lets users forward notifications from their iPhone to a Mac using the same Apple Account. Users signed in to devices with the same Apple Account exchange cryptographic identities using a local peer-to-peer protocol, encrypted using keys stored in iCloud using end-to-end encryption. When the user enables iPhone Mirroring and enters their passcode on the iPhone, the current cryptographic identity for the Mac is recorded. The private key for this identity is protected in the Secure Enclave. **This identity is pinned so that if it changes, notifications aren't forwarded to the Mac.** Notifications are encrypted in transit using end-to-end encryption.

中文对照：「用户通过相同 Apple 账户登录的设备使用本地点对点协议交换加密身份，期间使用储存在 iCloud 中的密钥进行端对端加密。用户启用 iPhone 镜像并在 iPhone 上输入其密码时，Mac 的当前加密身份会被记录。此身份的私钥保护在安全隔区中。**此身份已固定，因此当其更改时，通知便不会转发到 Mac。**通知在传输过程中使用端对端进行加密。」

这一段的三个要素在手册层里都算罕见：身份**固定**（pinned）并在变更时**停止转发**（这是一条明确的失效行为，不是策略建议）；私钥**在安全隔区**；密钥本身**存在 iCloud 但端对端加密**。另外这一页开头还有一句对会话可视性的说明：「在被 Mac 远程使用期间，iPhone 保持锁定状态，用户在 iPhone 的锁屏上看到一条持久通知；会话结束后首次解锁时会显示横幅。」

## 9.8 iPhone 镜像的远程解锁：复用同一协议，但不需要安全测距

> Remote unlock for iPhone Mirroring uses the same remote unlock protocol as iPhone unlock using Apple Watch but is initiated by the user launching the iPhone Mirroring app on their paired Mac. **Secure ranging isn't required for iPhone Mirroring.**

中文页对应段落说明，首次设置时用户要选「自动认证」或「每次均询问」，并且「Mac 上的安全隔区会强制执行此用户选择」，认证策略完成后「Mac 会使用本地无线点对点连接与 iPhone 连接，并且解锁 iPhone 密钥包以在远程会话期间启用远程访问」。

加粗那句是本节要点：**同一套远端解锁协议，在 iPhone 镜像场景下明确不做安全测距**。也就是说第 9.4 节执行-2 那一步不是协议的固有环节，而是**按使用场景选择的策略**。对照第 9.5 节 Vision Pro 需要光学检测 + 用户注视，可以归纳出：手册层把"距离"作为部分解锁关系的门槛，而不是全部；iPhone 镜像这一路以"用户主动启动 App + Mac 上 SEP 强制的认证策略"取代测距。这个不对称本身是可审计的事实，第 18 章据此设一条检查项。

## 9.9 为什么这一页在本篇权重最高

一是它是唯一把 SEP 写进连续互通链路终结点的手册页；二是它是唯一给出"每次解锁换密钥 + 用完从内存丢弃"这类密钥生命周期描述的手册页；三是它同时给出了两个反向事实——未搭载 SEP 的 Mac 在内核终止隧道、iPhone 镜像不需安全测距。手册层愿意在一页里同时写下机制与其例外，这让第 17 章的缺口清单显得更可信：它没写的部分（测距方法、密钥备份形态）是真的没写。
---

# 10 合图：谁发现、谁认证、谁传输、密钥落在哪

本章把第 2–9 章的手册层事实压成三张表，然后给出四条判断。所有表格单元格的来源都在括号里标了章节号；没有章节号可标的，说明手册层没说，写"未述"。

## 10.1 主矩阵

| 通道 | 发现/寻址 | 认证靠什么 | 载荷承载 | 密钥或身份落在哪 | 手册层给的关停入口 |
|---|---|---|---|---|---|
| 接力（通用） | BLE 广告（广告字节承载"当前活动"）（2.1） | BLE 4.2 标准加密 + 消息层加密，消息层"类似 iMessage"（2.1） | BLE；部分情形改由 APNs 承载（2.1）；大载荷改点对点 Wi-Fi + TLS（4.1） | 每设备 256 位对称 AES 密钥，存**设备钥匙串**（2.1） | 未述（本篇取样页内） |
| 原生 App ↔ 网页接力 | 同上 | 系统校验 App 对域名的控制权后才允许接收（3.1） | 同上 | 域名控制机制 = 共享网页凭据机制（3.1） | 未述 |
| 通用剪贴板 | 复用接力（2.5） | 同上 | 同上 | 同上 | App 开发者可禁止共享（2.5） |
| AirDrop | BLE 发信号（含短身份哈希）+ 点对点 Wi-Fi（5.1、5.3） | 两级身份哈希 → iCloud 身份证书 → 与通讯录比对（5.2、5.3） | 点对点 Wi-Fi，**TLS**，不经互联网或 AP（5.1） | 2048 位 RSA 身份，登录 iCloud 时存于设备（5.2） | 用户三档设置 + 组织设备管理服务（5.5） |
| Wi-Fi 口令共享 | 请求方 BLE 广播（7.1） | 「与隔空投送类似的过程」+ 要求联系信息（7.1） | BLE 连接（7.1） | 未述 | 组织限制（7.1） |
| 已存凭证发送 | 复用 AirDrop（7.2） | **强制"仅限联系人"，无论用户设置**（7.2） | 未述 | 接收方"自动填充密码"钥匙串（7.2） | 依附 iCloud 开关（7.2） |
| Apple TV 自动填充 | tvOS 焦点触发 BLE 广播请求（7.3） | 同账户自动加密；跨账户 PIN + iPhone 解锁 + 靠近配对的 Siri Remote（7.3） | **BLE 链接加密**（7.3） | 未述 | 未述 |
| 智能互助热点 | 含 DSID 派生标识符的 BLE 广播，账户级、定期轮换（7.4） | 账户一致性；家庭共享用设备级 Ed25519 公钥认证（7.4） | BLE 加密链路 + 逐消息加密（7.4） | 家庭共享公钥经 **IDS** 同步（7.4） | 未述 |
| 通话中继 | 同 Wi-Fi + 同账户（6.1） | 未述（前置条件承担） | 信令 APNs、音频"安全的点对点连接"（6.2、6.3）；停铃用 BLE 广播（6.4） | 未述 | FaceTime 设置里的"iPhone 蜂窝网络通话"，按设备（6.5） |
| 短信转发 | — | 双重认证 + 信任圈内自动注册；否则随机 6 位码逐设备验证（6.6） | 「《iMessage 信息安全性概览》所述方法」（6.6） | 未述 | "信息"设置（6.6） |
| Apple Pay 接力 | 未述（发现载荷有内容枚举，见 8.2） | 未述 | **端对端加密的 IDS 协议**（8.1） | Mac 侧用"用户的设备密钥"，「这些密钥不提供给了 Apple」；Device Account Number 留在 iPhone/Watch 的 SEP（8.1、8.2） | 未述 |
| 自动解锁 | 新 BLE 连接 + 点对点 Wi-Fi 估算距离（9.4） | **相互认证的 STS 隧道，在两台 SEP 之间协商**（9.2） | STS 隧道（承载不限，9.2） | 隧道密码材料在 SEP；例外：无 SEP 的 Mac 在**内核**终止隧道（9.2）；Watch 场景的托管记录密钥在 **iPhone 钥匙串**（9.6） | 逐条启用每种解锁关系（9.3）；锁定命令可即时收回（9.5） |
| iPhone 镜像通知转发 | 本地点对点协议交换加密身份（9.7） | 身份**已固定**，变更即停止转发（9.7） | 端对端加密（9.7） | 私钥在 **SEP**；密钥存 iCloud 但端对端加密（9.7） | 未述 |
| iPhone 镜像远程解锁 | 用户主动启动 App，**不需要安全测距**（9.8） | Mac 上 SEP 强制执行"自动认证/每次均询问"（9.8） | 本地无线点对点连接（9.8） | 会话中解锁 iPhone 密钥包（9.8） | 首次设置时选择认证策略（9.8） |

表格里有 8 处"未述"。这不是行文偷懒，而是第 17 章缺口清单的来源——把它们集中呈现，是为了让读者看见手册层的覆盖形状：**它擅长说明"密钥怎么协商"，几乎不说明"密钥怎么撤销、怎么备份、留在服务器上的部分是什么"**。

## 10.2 第二个维度：这条链路经过 Apple 基础设施吗

| 是否经过 | 通道 | 手册层依据 |
|---|---|---|
| 明确经过（APNs 承载） | 接力配对（带外）、接力消息（"部分情形"）、通话中继的信令（来电与去电） | 2.1、6.2、6.3 |
| 明确**不**经过 | AirDrop 的数据承载："不经互联网连接或无线接入点" | 5.1 |
| 不经空口、但经账户基础设施分发密钥 | 家庭共享热点公钥（IDS 同步）、iPhone 镜像密钥（存 iCloud，端对端加密） | 7.4、9.7 |
| 经运营商 | 短信转发的两端（进/出均为运营商 SMS），iPhone 只做加密转发 | 6.6 |
| 手册层未说明 | Wi-Fi 口令共享、凭证发送、Apple TV 填充、Apple Pay 接力的发现阶段 | — |

第二行与第三行的对比值得单独看：**"数据不经服务器"与"密钥经服务器分发"是可以并存的**。AirDrop 属于前者，其身份却来自登录 iCloud 时存入设备的 RSA 身份（5.2）；iPhone 镜像属于后者，其密钥存在 iCloud 但被端对端加密（9.7）。把这两件事混成一句"依赖 iCloud 所以不安全"或"端对端所以与服务器无关"，都是越界。

## 10.3 四条判断

**判断一：连续互通的信任根有三层，而且它们不等价。** 手册层的表述分别是：账户（iCloud 登录 / 同一 Apple 账户）、通讯录（AirDrop 与 Wi-Fi 口令共享的联系信息比对）、硬件（STS 隧道两端的安全隔区、pinned 身份）。第 6.1 节说明"同一账户"还不足以启用通话中继，还要 FaceTime 也登录；第 9.5 节说明远端解锁不产生认证断言。三层各有各的失效方式，本篇不接受"因为同账户所以全可信"这类一句话结论。

**判断二：手册层描述安全时大量使用类比，而不是算法声明。** 全篇出现的类比句至少有四种："与 iMessage 信息中的消息加密方式很相似"（2.1）、"以类似 iMessage 加密的方式"（7.4）、"使用与'接力'广播相同的方法来加密"（6.4）、"使用与隔空投送类似的过程"（7.1）。真正给出算法名与位长的句子只在三处：AES-256-GCM + 重放保护（2.1）、BLE 4.2 标准加密（2.1）、TLS（4.1、5.1）。这个比例本身就是本报告的方法论依据：**凡类比句，不得当作密码套件引用**。第 11 章的术语普查会给出这些类比句的分布。

**判断三：APNs 是接力的备用承载，这使"近场通道"不是一条封闭链路。** 手册层的原句 "In some situations, these messages are sent using APNs instead of BLE" 没有说明是哪些情形（缺口 G6）。它带来的分析后果是：审计接力流量时不能只看射频侧。至于服务端在这条路径上能看到什么，手册层没写；措辞层也没有任何一条 AirDrop/Handoff 条目涉及服务器侧（第 14 章实测）。

**判断四：用户可视化区分只有一处，撤销入口只有一处。** 唯一一处把"未验证"状态呈现给用户的是 AirDrop 的剪影图标 + 用户自设名 + "其他人员"分组（5.4）；唯一一处把"变更即拒绝"写成机制的是 iPhone 镜像的 pinned 身份（9.7）。其余通道在手册层里**没有给出等价的可视化区分**。这一判断直接决定第 18 章的形态：能检测的点很少，多数只能落在配置与策略层，而不是运行时观测层。

## 10.4 与报告 #103 的分界

《iOS 蓝牙与近场配对通道的安全机制分析》处理的是**配对与绑定本身**：蓝牙安全模型的六项功能、数字比较与密钥接入、escrow 公钥与主机配对、安全激活数据连接、NFC/SE 与 UWB。本篇处理的是**配对之上**的活动接续与凭据下发：谁在广告里说什么、身份怎么被两级哈希确认、承载什么时候从 BLE 换成 Wi-Fi 或 APNs。两篇共用一份手册层语料，但引用页不重叠；唯一刻意重复的是"点对点 Wi-Fi + TLS"这一句，因为它同时是 AirDrop 的承载和接力的扩容承载，属于共享原语。蓝牙段落与 Core Bluetooth 段落的 CVE 逐条分析在 #103，本篇不复述；本篇新增的是 Wi-Fi / afclip / Clipboard 段落族（第 15 章）。

---

# 11 手册层的沉默：术语普查与两套口径

本章全部是负结果。负结果在本篇的地位不低于正文引用，因为它决定了后面几章能说什么、不能说什么。

## 11.1 先把口径讲清楚：中文全量 179 页，英文只取样 20 页

任何"手册层从不使用某个词"的断言都必须附带口径。本篇的取数条件不对称：

| 侧 | 语料 | 页覆盖 | 允许的结论强度 |
|---|---|---|---|
| 中文 | 《Apple 平台安全保护》zh-CN 全量转储 | 179 页（全部） | 可下"全量 0 次"结论 |
| 英文 | 走 `security-pdf/<sec>/1/web/1` 端点逐页取回，剥掉页内目录块后的正文 | 20 页（取样） | 只能下"取样 20 页中 0 次"结论 |

英文侧只取样 20 页是本轮的真实局限（第 17 章 G8）。凡本报告写"英文侧 0 次"，一律指这 20 页。

## 11.2 普查结果

英文侧计数基于剥目录后的正文（未剥时会把整块目录算进去，"Continuity" 会虚高到 23 次/10 页——这个坑本报告踩过，第 16.5 节交代方法时写明）。

| 术语 | 中文（179 页全量：次数 / 页数） | 英文（20 页取样正文：次数 / 页数） | 判读 |
|---|---|---|---|
| 隔空投送 / AirDrop | 31 / 7 | 29 / 6 | 有专页，跨页引用多 |
| 接力 / Handoff | 32 / 5 | 28 / 3 | 有专页 |
| 通用剪贴板 / Universal Clipboard | 4 / 1 | 4 / 1 | **单页承载**，别处不提 |
| 通话中继 | 3 / 2 | — | 有 |
| 短信转发 | 6 / 2 | — | 有 |
| 自动解锁 / Auto-unlock | 13 / 5 | 1 页 | 有 |
| 连续互通 / Continuity | 4 / 2 | 4 / 3 | 概念名，无独立机制页 |
| 点对点 | 14 / 6 | peer-to-peer 13 / 5 | 有，但从不点名技术 |
| **AWDL** | **0 / 0** | **0 / 0** | **手册层两侧都不使用这个词** |
| **Nearby Interaction / 近场交互** | **0 / 0** | **0 / 0** | 手册层无此框架条目 |
| 端对端 | 65 / 26 | end-to-end 4 / 3 | 中文侧广泛使用 |
| 钥匙串 / keychain | 202 / 39 | 6 / 4 | 大量使用 |
| 安全隔区 / Secure Enclave | 322 / 51 | 12 / 2 | 大量使用，但**连续互通相关取样页里只有 2 页**（自动解锁页与 iPhone 镜像所在页） |
| 身份服务 / IDS | 13 / 11；IDS 24 / 14 | IDS 4 / 2；Identity Service 1 / 1 | 有 |
| APNs | 24 / 8 | 4 / 2 | 有 |
| 双重认证 | 39 / 20 | — | 有 |
| 信任圈 | 18 / 2 | — | 集中在两页（短信转发与账户相关页） |
| 重放 | 63 / 17 | replay（接力页 1 处） | 中文侧高频 |
| 轮换 | 5 / 2 | — | 低频，且**从不给周期数值** |
| 剪贴板 | 7 / 1 | clipboard（接力页内） | 单页 |
| 剪影 | 1 / 1 | silhouette（AirDrop 页内） | **仅 AirDrop 页一处** |
| **隔离** | 10 / 6，但六页语义均为进程/内存/扩展隔离或 macOS 门禁托管项里的"文件隔离" | **quarantine 0 / 0**（取样 20 页） | 手册层**没有**"AirDrop 收到的文件带隔离标记"这类表述 |
| 超宽带 / Ultra Wideband | — | 3 / 1 | 取样里仅蓝牙那组页出现 |
| 符号链接 | **0 / 0** | — | 见第 15.4 节，这个词只在措辞层 |

## 11.3 三条必须落到正文里的沉默

**沉默一：手册层从不点名 AWDL。** 它在三个不同位置都需要用到这条链路，三次的写法分别是「Apple 开创的点对点无线局域网技术」（接力大载荷，4.1）、「Apple 创建的点对点无线局域网技术」（AirDrop，5.1）、「点对点无线局域网」（自动解锁，9.4）。术语 AWDL 属于开发者文档与第三方研究口径，本篇凡涉及该链路一律沿用手册层写法，并在需要指出技术名时显式标注"第三方/开发者文档口径"。

**沉默二：手册层没有 Nearby Interaction 条目，也没有把超宽带写进任何一条连续互通链路。** 第 9.4 节执行-2 那句「使用点对点无线局域网来安全估算彼此之间的距离」是全篇唯一一处"测距"表述，而它点名的是 Wi-Fi，不是 UWB。这意味着：凡是"AirDrop/接力靠 UWB 精确定位"的通俗说法，在手册层找不到依据；第 9.5 节的距离门限（2–3 米、约 1 米）是**策略数值**，不是测距技术声明。手册层的超宽带专页（见报告 #103 第五节）整页只有三句，且把 UWB 归入**随机化**而非加密或测距安全。

**沉默三：手册层没有一处把"密钥如何撤销、如何备份、轮换周期多长"写成机制。** 「轮换」中文全量仅 5 次于 2 页，且都不给数值；第 7.4 节的 "rotated periodically" 就是典型——它声明了轮换存在，没声明周期。第 2.1 节的 256 位对称广告密钥没有轮换、备份、失窃影响的任何说明。第 18 章之所以只能给出配置层与策略层检查项，根源就在这条沉默。

## 11.4 一个方向性发现：手册层讲机制，措辞层讲攻击者位置

把第 11.2 节与第 13 章放在一起看，会出现一个值得单独指出的落差：

| | 手册层 | 措辞层（顾问页） |
|---|---|---|
| 高频词 | 配对、绑定、密钥、加密、身份、认证、钥匙串、安全隔区 | privileged network position、physical proximity、denial-of-service、corrupt kernel memory |
| 完全缺席 | —（手册层不评估漏洞） | actively exploited、man-in-the-middle、eavesdrop、sandbox、zero-click、encrypt、in the wild（本族 230 段全 0）；remote attacker 与 root privileges 初稿也列在此处，重测后分别有 8 段与 1 段，见第 13.2 节 |

也就是说，Apple 在手册层用**机制语汇**描述这些通道，在措辞层用**攻击者位置语汇**描述它们的缺陷，而两套语汇之间没有桥接词。漏洞条目不会说"配对被绕过"，手册页也不会说"处于特权网络位置的攻击者"。本篇第 14、15 章逐条分析时，这个落差是解释"为什么某类叙述无法用 Apple 原话支撑"的主要工具。

---

# 12 措辞层的段落名：一份存在性清单和一批不存在的名字

## 12.1 普查条件

语料：Apple 安全响应顾问页英文转储 **257 份**（覆盖 iOS/iPadOS、macOS、tvOS、watchOS、visionOS、AirPods/Beats 固件、Security Update 等；转储共 258 份，剔除 1 份非安全响应页）。段落切分方式（**容错版**，第 13.5 节解释为什么必须容错）：以 `<h3 class="gb-header`…`">组件名</h3>` 为块首，块尾取下一个同级标题、`#disclaimer` 或文件结束；段内取 `<p class="gb-paragraph…">`（同样必须前缀匹配，类名后面可能跟 `alignment horizontal-align-left`）顺序取 Available for / Impact / Description / 致谢行 / 条目备注。标题与段落两处的类名都必须写成**前缀匹配**，因为标记带后缀的那 10 页用的是 `<h3 class="gb-header alignment horizontal-align-left">`；写成完全相等会在这 10 页上命中 0 块且不报错，第 13.5 节整节处理这件事（同一节还说明：这 10 页**不是**语料里最新的一批页面）。

同一把容错切块刀在整个语料上得到 **9,045 段 / 470 个段落名**；本文初稿那把严格切块刀只得到 7,615 段 / 439 名，静默漏掉 1,430 段。

与本篇相关的段落名与段数（容错口径，括号内是初稿严格口径）：**Wi-Fi 79（61）、Bluetooth 68（35）、AirDrop 20（20）、Core Bluetooth 25（15）、CoreBluetooth - LE 11（6）、Wi-Fi Connectivity 9（5）、afclip 7（7）、Handoff 6（6）、Clipboard 5（5）**，合计 **230 段（160）**（蓝牙族与 Wi-Fi 族的分工见第 13 章）。差额 70 段全部来自标记带后缀的 10 页（149034–149043）。另有 RepairKit 2 段，与本篇无关。

段落数之外同时记录**页数**，因为它决定一个词出现的"独立性"：`Bluetooth` 68 段分布在 36 页，`Wi-Fi` 79 段分布在 55 页，`AirDrop` 20 段刚好 20 页，`CoreBluetooth - LE` 11 段 / 11 页，`afclip` 7 段 / 7 页，`Wi-Fi Connectivity` 9 段 / 9 页，`Clipboard` 5 段 / 5 页，`Core Bluetooth` 25 段 / 20 页，`Handoff` 6 段 / 4 页。

## 12.2 不存在的段落名（本篇的硬约束）

在 257 份页面里，以下组件名**从未作为段落名出现**：

Continuity、Personal Hotspot、Instant Hotspot、Ultra Wideband、Nearby、Call Relay、iPhone Cellular Calls、Text Message Forwarding、Auto-unlock、Universal Clipboard、iCloud Keychain（作为近场段落）、Pairing、Bonding。

由此得到三条写法纪律：

1. **不得**写"Apple 在某组件下披露了个人热点漏洞 / 通话中继漏洞 / 自动解锁漏洞"。这些通道在措辞层没有归属段落；手册层有页、措辞层无段，这个不对称本身就是发现。
2. 反过来，**可以**写"某条 Wi-Fi 段落的条目，其可达性措辞是物理邻近"（第 15.2 节），因为 Wi-Fi 段落真实存在。但这不等于该条目属于连续互通——Apple 没说，本篇也不说。
3. **不得**把 afclip 段落写成 AirDrop 段落。它俩是不同的段落名，第 15.3 节单列。

## 12.3 段落名的三种蓝牙写法

同一族内，Apple 自己用了三个不同的段落名：`Bluetooth`（68 段 / 36 页）、`Core Bluetooth`（25 段 / 20 页）、`CoreBluetooth - LE`（11 段 / 11 页）。第 16.1 节展开它的后果。此处先记一个事实：报告正文引用条目时必须照抄该页段落名，不得统一化。重测后这三者的相对比例也变了——严格口径下 `Bluetooth` 占蓝牙三个段落名合计 56 段的 62%（35/56），容错口径下占 104 段的 65%（68/104）：新增可见的蓝牙段落里，25 段挂在 `Bluetooth`、10 段挂在 `Core Bluetooth`、5 段挂在 `CoreBluetooth - LE`（后者这 5 段全是纯致谢，没有 CVE）。

---

# 13 两族词频对照：130 段与 100 段，以及"kernel"落在哪一边

## 13.1 分族

把第 12.1 节的 230 段按承载技术分成两族：

| 族 | 段落名 | 段数（初稿） | 页数 |
|---|---|---|---|
| 蓝牙/近场身份族 | Bluetooth、Core Bluetooth、CoreBluetooth - LE、AirDrop、Handoff | **130**（82） | 36+20+11+20+4，去重后按段落名分列见第 12.1 节 |
| Wi-Fi/本地解析族 | Wi-Fi、Wi-Fi Connectivity、afclip、Clipboard | **100**（78） | 55+9+7+5 |

这个分法是本篇的分析构造，不是 Apple 的分类，正文一律标注为"本报告分族"。分族依据是第 11.3 节与第 12.1 节：手册层把 AirDrop 与接力的数据承载都写成点对点 Wi-Fi，但其**身份识别**走 BLE；而 Wi-Fi 段落的条目措辞围绕另一回事。分族之后出现的结果差异常超预期，见下表。

两族的增量不相等：蓝牙族一次重测多出 **48 段**（82→130，+59%），Wi-Fi 族只多出 **22 段**（78→100，+28%）。原因是标记带后缀的 10 页里的新条目集中挂在 `Bluetooth`、`Core Bluetooth` 与 `Wi-Fi` 三个名下，而 `AirDrop`、`Handoff`、`afclip`、`Clipboard` 四个名在这 10 页里**一条新条目也没有**（AirDrop 20、Handoff 6、afclip 7、Clipboard 5 四个数在两种口径下完全相同）。这个"四个名纹丝不动"的形状本身就是发现：**Apple 近半年在措辞层新增的近场条目，没有一条被归到 AirDrop 或 Handoff。** 70 个新增可见段落在五个段落名上的分布是：`Bluetooth` 33、`Wi-Fi` 18、`Core Bluetooth` 10、`CoreBluetooth - LE` 5、`Wi-Fi Connectivity` 4；其中 **53 段带 Impact + Description、17 段是纯致谢**，`CoreBluetooth - LE` 那 5 段全部属于后者。

## 13.2 全量词频（同族段落内，Impact + Description + 致谢 + 备注合并文本）

口径先说清：下表统计的是**命中该词形的段落数**，不是出现次数；同一个 CVE 在 8 条产品线页面上各占一段，就会被计 8 次。最后一列给出该词形在**全语料 9,045 段**里的段落数，用来说明前两列的 0 到底是"本族回避"还是"Apple 整体不用这个词"。大小写不敏感检索；"Beacon protection" 必须按正文的小写 p 检索，写成 `Beacon Protection` 会得 0。

| 词或短语 | 蓝牙/近场身份族（130 段） | Wi-Fi/本地解析族（100 段） | 全语料（9,045 段） |
|---|---|---|---|
| actively exploited | **0** | **0** | 18 段 / 13 页 |
| man-in-the-middle | **0** | **0** | **0** |
| eavesdrop | **0** | **0** | **0** |
| zero-click | **0** | **0** | **0** |
| in the wild | **0** | **0** | **0** |
| encrypt | **0** | **0** | 5 段 / 5 页 |
| sandbox | **0** | **0** | 474 段 / 119 页 |
| remote attacker | **8**（初稿记为 0） | **0** | 194 段 / 89 页 |
| root privileges | **1**（初稿记为 0） | **0** | 233 段 / 74 页 |
| **kernel** | **0 段** | **23 段** | 647 段 / 125 页 |
| physical proximity | **3**（初稿记为 0） | 19 段 | 22 段 / 21 页 |
| wireless proximity | 0 | **8** | 8 段 / 8 页 |
| radio range | **8** | 0 | 9 段 / 8 页 |
| paired | 8 | 0 | 15 段 / 13 页 |
| a paired Bluetooth device | **6** | 0 | 6 段 / 6 页 |
| privileged network position | 12 段 | 7 段 | 93 段 / 56 页 |
| denial-of-service | 10 段 | 13 段 | 425 段 / 152 页 |
| spoof | 3 段 | 0 | 72 段 / 58 页 |
| pairing | 1 段 | 0 | 8 段 / 8 页 |
| arbitrary code execution | **8** | 0 | 198 段 / 90 页 |
| out-of-bounds | **16** | 25 段 | 813 段 / 170 页 |
| bounds checking | **16** | 25 段 | 699 段 / 154 页 |
| input validation | **14**（初稿 5） | 0 | 673 段 / 149 页 |
| stack overflow | **6** | 0 | 22 段 / 22 页 |
| memory corruption | **3** | 0 | 384 段 / 123 页 |
| use after free | 0 | 7 段 | 127 段 / 49 页 |
| symlink | 0 | 5 段 | 117 段 / 56 页 |
| Beacon protection | 0 | 3 段 | 3 段 / 3 页 |
| entitlement | **1** | 0 | 131 段 / 74 页 |

三张硬结论（其中第二张在重测后被撤销，见下文）：

**结论一（重测后更强）：措辞层的"内核"完全在 Wi-Fi 族，蓝牙/AirDrop/Handoff 三族一次都没写 kernel（0/130）。** 因此本篇凡要引"内核态后果"的证据，只能落在 Wi-Fi / afclip / Clipboard 段落，**不能**挂在蓝牙段落上。这条是第 15 章的组织原则。它在重测中没有动摇，而且分母变大了：初稿是 0/82，现在是 0/130——**新增的 48 段里 `kernel` 依旧为 0**，同时新增的 22 段 Wi-Fi 里 `kernel` 也全为 0（Wi-Fi 族那 23 段一条不落全在旧页）。唯一例外来自手册层：第 9.2 节「未搭载安全隔区的 Mac 电脑会在内核中终止 STS 隧道」是 T1 措辞，不是漏洞条目，引用时标 T1。

**结论二（撤销）：初稿写过"'物理邻近'这个可达性描述只在 Wi-Fi 族（14 段），蓝牙族不用 proximity 措辞"。这句话现在是错的。** 容错重测后蓝牙族有 **3 段** `physical proximity`（CVE-2026-84547，页 149035、149042、149043），其 Impact 原文是「An attacker in physical proximity may be able to corrupt process memory」。**Apple 从 2026 年 10 月起开始在蓝牙条目里使用物理邻近措辞。** 结论二被本节就地作废，不得再在任何后续文章里复述。

修订后仍然成立的部分是：**两个距离措辞都不重叠地各自绑定到具体 CVE**，且新条目带来了一个第三种距离措辞。全语料 `in physical proximity` 共 22 段 / 21 页，恰好等于本两族的 3 + 19——**这个短语在全语料里只出现在这两族的段落中**，别处一段都没有。`in radio range` 共 9 段，本族 8 段（CVE-2026-84613，全在 `Bluetooth`）+ 报告 #105 的 `Baseband` 1 段。`in wireless proximity` 共 8 段，**全部在 `Wi-Fi` 名下**（CVE-2026-86957），这是措辞层新出现的一种距离限定写法，此前从未被本篇使用。所以距离措辞的分布确实按组件划分，只是**划分线移动了**。

**结论三（改写）：蓝牙族并非"不涉及加密强度或配对之外的密钥问题"，但涉及的方式很窄。** 重测后蓝牙族新增的 48 段全部走同一套修复句式：`bounds checking` 16 段、`input validation` 9 段（其中 `Core Bluetooth` 6 段来自 CVE-2026-86919 的 "A stack overflow was addressed with improved input validation"）、`memory corruption` + `input validation` 3 段（CVE-2026-84547）、`entitlement` 1 段（CVE-2026-84631 的 "additional entitlement checks"）。**没有一段提到加密、密钥交换或认证被绕过。** 换言之，结论三的原始观察在重测后依然成立，但它成立的方式是"新增条目也没有引入加密语汇"，而不是"该族历史上没有加密语汇"。同时 `pairing` 一词在全语料也只有 8 段，本族 1 段（CVE-2024-44124）；`encrypt` 全语料 5 段，本两族 0 段——**Apple 的措辞层几乎不使用加密词根描述任何缺陷**，这不是近场族的特征，是全语料特征。

## 13.3 数字的完全分解：两族每一格都能落到具体 CVE

这一小节是本章的可信度来源。第 13.2 节两列非零数字，逐个都能被具名 CVE 的段落数解释；本文初稿只对 Wi-Fi 族做过这张表，蓝牙族那列登记为局限（旧 G11），本次补全。

**Wi-Fi/本地解析族（100 段）**

| 词 | 段数（初稿） | 构成（CVE → 段数） |
|---|---|---|
| kernel | 23（23） | CVE-2024-44227/54546 → 121238；CVE-2024-44227 → 121250；CVE-2025-43374 → 122069、122070、122404、122405、122716、122720、122721、122722（8）；CVE-2025-43373 → 125634、125635、125636（3）；CVE-2026-20621 → 126346–126350、126353（6）；CVE-2026-28819 → 127111、127115、127116、127117（4） |
| physical proximity | 19（14） | CVE-2025-43374 → 8；CVE-2026-64726 → 128066–128070、148287（6）；**CVE-2026-86953 → 149034–149038（5）** |
| wireless proximity | **8（0）** | CVE-2026-86957 → 149034–149038、149041–149043（8），全在 `Wi-Fi` 名下；全语料也只有这 8 段 |
| privileged network position | 7（7） | CVE-2026-28994 → 127110、127111、127115、127116、127117、127118、127119（7） |
| use after free | 7（7） | 同上，CVE-2026-28994 的 Description |
| denial-of-service | 13（8） | CVE-2026-28994 → 7；CVE-2024-23237 → 1；**CVE-2026-86953 → 5** |
| out-of-bounds | 25（12） | CVE-2025-43374（read，8）+ CVE-2026-28819（write，4）+ **CVE-2026-86957（read，8）+ CVE-2026-86953（write，5）** |
| bounds checking | 25（12） | 与上一行逐格同源：43374（8）+ 28819（4）+ 86957（8）+ 86953（5） |
| symlink | 5（5） | CVE-2026-28866 → Clipboard 段落 126792–126796 |
| Beacon protection | 3（3） | CVE-2024-40856 → 121238、121248、121250；**全语料仅此 3 段，是封闭集合** |

**蓝牙/近场身份族（130 段）**

| 词 | 段数（初稿） | 构成（CVE → 段数，段落名） |
|---|---|---|
| remote attacker | **8（0）** | CVE-2026-65414 → 149034–149038、149041、149042、149043（`Bluetooth`） |
| arbitrary code execution | **8（0）** | 同上，CVE-2026-65414 的 Impact |
| radio range | **8（0）** | CVE-2026-84613 → 同 8 页（`Bluetooth`）；全语料 9 段，另 1 段在 `Baseband`（见 #105） |
| out-of-bounds | **16（0）** | CVE-2026-65414（write，8）+ CVE-2026-84613（write，8）（`Bluetooth`） |
| bounds checking | **16（0）** | 与上一行逐格同源 |
| input validation | **14（5）** | CVE-2026-43667 → 5（`AirDrop`）；CVE-2026-84547 → 3（`Bluetooth`）；CVE-2026-86919 → 6（`Core Bluetooth`） |
| stack overflow | **6（0）** | CVE-2026-86919（`Core Bluetooth`） |
| a paired Bluetooth device | **6（0）** | 同上，CVE-2026-86919 的 Impact；全语料恰好也是 6 段 / 6 页 |
| paired | 8（2） | CVE-2024-27867 → 1、CVE-2025-20701 → 1（`Bluetooth`）+ CVE-2026-86919 → 6（`Core Bluetooth`） |
| physical proximity | **3（0）** | CVE-2026-84547 → 149035、149042、149043（`Bluetooth`） |
| memory corruption | **3（0）** | 同上，CVE-2026-84547 的 Description |
| root privileges | **1（0）** | CVE-2026-84631 → 149035 一页（`Bluetooth`） |
| entitlement | **1（0）** | 同上，CVE-2026-84631 的 Description |
| denial-of-service | 10（10） | CVE-2026-20650 → 5（`Bluetooth`）+ CVE-2026-43667 → 5（`AirDrop`） |
| privileged network position | 12（12） | CVE-2026-43667 → 5（`AirDrop`）+ CVE-2026-20650 → 5（`Bluetooth`）+ CVE-2024-23277 → 2（`Bluetooth`） |
| spoof | 3（3） | CVE-2024-23277 → 2、CVE-2024-27867 → 1（`Bluetooth`） |
| pairing | 1（1） | CVE-2024-44124 → 121250（`Core Bluetooth`）；全语料 `pairing` 仅 8 段 |

两族各自相加都能与第 13.2 节对齐，没有剩余格。这意味着第 13.2 节那两列数字不是"某些段落里出现过"的模糊统计，而是**几条 CVE 在多产品线上的重复计数**。由此可得一条通用提醒：措辞层的词频不能当"频次"读，它是 **CVE × 产品线覆盖数** 的乘积。CVE-2025-43374 一条就贡献了 8 段 kernel、8 段 physical proximity、8 段 out-of-bounds；CVE-2026-65414 一条就贡献了 8 段 remote attacker、8 段 arbitrary code execution、8 段 out-of-bounds、8 段 bounds checking。**"某词出现 8 次"和"存在 8 个缺陷"之间没有对应关系。**

产品线覆盖差异本身可读，且在本篇里第一次成了判据：同批新增条目中，CVE-2026-86953 覆盖 149034–149038 共 **5** 页（iOS/iPadOS、MacBook Neo 机型的 macOS、Apple TV、Apple Watch、Apple Vision Pro），而 CVE-2026-86957 覆盖 8 页（多出 149041、149042、149043 三条遗留分支）；CVE-2026-86919 覆盖 6 页，**独独不含 Apple TV 与 Apple Watch 两页**。这三组页码不能互换，也不表示 Apple Watch 上没有同一 CVE——它只表示顾问页在该产品线页面上没有把这条列进去。本篇一律按"该页有/无该段"陈述，不做机型推断。

## 13.4 近场族可达性措辞的完整光谱：从"已配对设备"到"不限位置"

把第 13.3 节的攻击者限定语按位置由近到远排一次，是本章真正的产出。蓝牙/近场身份族 130 段里，Impact 中**带位置或状态限定**的说法共有六种：

| 限定语（原文照抄） | 段数 | 出处 CVE | 段落名 |
|---|---|---|---|
| An attacker **with a paired Bluetooth device** | 6 | CVE-2026-86919 | Core Bluetooth |
| an attacker **in Bluetooth range** / **within Bluetooth range** | 2 / 1 | CVE-2024-27867 / CVE-2025-20701 | Bluetooth |
| An attacker **in radio range** | 8 | CVE-2026-84613 | Bluetooth |
| An attacker **in physical proximity** | 3 | CVE-2026-84547 | Bluetooth |
| An attacker **in wireless proximity** | 8 | CVE-2026-86957 | Wi-Fi |
| An attacker **in a privileged network position** | 12（本族）/ 7（Wi-Fi 族） | 43667、20650、23277 / 28994 | AirDrop、Bluetooth、Wi-Fi |
| **A remote attacker**（无任何位置限定） | 8 | CVE-2026-65414 | Bluetooth |

三个结论：

1. **本族第一次出现了完全不带位置限定的蓝牙条目。** CVE-2026-65414 的 Impact 原文是「A remote attacker may be able to cause unexpected app termination or arbitrary code execution」，Description 是「An out-of-bounds write issue was addressed with improved bounds checking」，段落名 `Bluetooth`，8 个产品线页面全部一致，**且没有致谢、没有条目备注**。本文初稿据此写过"近场族没有 remote attacker"，该句已作废（第 13.2 节结论二同款处理）。
2. **距离措辞在 2026 年 10 月这批条目里同时出现了三种新写法**：`in radio range`（84613）、`in physical proximity`（84547）、`in wireless proximity`（86957）。三者分别绑定不同 CVE、不同段落名，看不出是同义替换的用意；`in wireless proximity` 在全语料只存在这 8 段，是新造的词形。本篇不推断 Apple 为何在这一批里同时引入三种说法，只登记它们各自只出现在一个组件名下。
3. **`in physical proximity` 全语料 22 段 / 21 页，恰好被本篇两族覆盖完（19 + 3）**，别的一段都没有；`in wireless proximity` 8 段全在 `Wi-Fi`。这两个短语是"距离限定"这一措辞在整个顾问页语料里的全部落点，且**只落在 Wi-Fi 与蓝牙两个组件名下**。

## 13.5 一次自我纠错：标记换代让初稿静默漏采 70 段

本节记录本篇（也是本系列）最重要的一次方法学更正，供后续所有普查引用。

**症状。** 本文初稿第 12.1 节报出 160 段（Bluetooth 35、Wi-Fi 61、Core Bluetooth 15、Wi-Fi Connectivity 5、CoreBluetooth - LE 6），第 13.2 节据此给出两条"0 结论"：近场族 `remote attacker` = 0，`physical proximity` = 0。两者在当时都完全自洽、都能逐格分解、都没有报错。

**原因。** 切块正则写成**属性完全相等**的严格匹配：

- 块首 `<h3 class="gb-header">组件名</h3>`
- 段内 `<p class="gb-paragraph">…</p>`

而 Apple 在标记带后缀的这 10 页（149034、149035、149036、149037、149038、149039、149040、149041、149042、149043）给这两个标签都追加了排版类。以 149034 为例，实测该页 `<h3>` 的类名只有一种取值：`gb-header alignment horizontal-align-left`（209 个，**没有一个等于 `gb-header`**）；`<p>` 的类名有四种取值，其中三种以 `gb-paragraph` 开头——`gb-paragraph alignment horizontal-align-left` 639 个、裸 `gb-paragraph` 37 个、`gb-paragraph alignment horizontal-align-right` 1 个，合计 677 个，另有一种 `subheader gb-subheader alignment horizontal-align-left` 1 个不属于段落序列。**严格正则在整整 10 页上命中 0 个块首，且不抛异常、不产生警告**——脚本会安静地把这些页当成"没有段落"跳过。作为对照，旧页 148287 与 127110 的严格计数与容错计数完全相同（110/110、81/81），说明这不是"新页面结构不同"而是"新页面多了后缀类名"。

**修法（三条，缺一不可）。**

1. 块首改前缀匹配：`<h3 class="gb-header[^"]*"[^>]*>`
2. 段内改成前缀匹配，兼容带排版后缀的类名：`<p class="(?:gb-paragraph|paragraph)[^"]*"[^>]*>`
3. 块尾必须给出终止锚点，否则最后一页的块会吞掉页脚：`(?=<h3 class="gb-header|<div id="disclaimer"|<footer|$)`

**影响范围。** 全语料层面：严格口径 7,615 段 / 439 名 → 容错口径 **9,045 段 / 470 个段落名**，静默漏采 **1,430 段**，即 15.8% 的语料从未进入统计。这 10 页在容错口径下的逐页块数基线为：149034→209、149035→314、149036→118、149037→128、149038→165、149039→13、149040→9、149041→103、149042→181、149043→190，严格口径下这 10 页**全部为 0**。这十个数逐页相加正好是 **1,430**——也就是说，全语料漏采的 1,430 段**一段不多、一段不少，全部来自这 10 页**，不存在第二处结构性漏采点。本篇层面：160 → **230**，其中蓝牙族 82 → 130、Wi-Fi 族 78 → 100。段落名层面的损失比段落数更值得注意：严格口径只认得出 439 个名，容错口径 470 个，**有 31 个段落名是这次重测才第一次进入统计的**，其中包含 `rapportd`（3 段）、`Identity Services`（1 段）、`AutoFill`（1 段）、`Local Authentication`（1 段）、`Remote Control`（1 段）、`CoreDrag`（3 段）、`DeviceCheck`（24 段）、`Apple Intelligence`（10 段）、`RealityKit`（42 段）、`Accelerate Framework`（44 段）等——第 15.6 节处理与本篇相邻的那七个。一个段落名"在语料里不存在"和一个段落名"解析器看不见"，在旧口径下完全无法区分。

**被推翻的论点（本篇）。** `remote attacker` 0→8、`root privileges` 0→1、`physical proximity` 0→3、`out-of-bounds` 0→16、`bounds checking` 0→16、`input validation` 5→14、`stack overflow` 0→6、`arbitrary code execution` 0→8、`memory corruption` 0→3、`entitlement` 0→1、`radio range` 0→8、`wireless proximity`（Wi-Fi 族）0→8。第 13.2 节的结论二整条作废，结论一与结论三在更正分母后仍然成立。

**一条方向性观察，也是本篇给出的方法学规则：漏采只会把非 0 压成 0，不会把 0 抬成非 0。** 因此重测后**仍然为 0** 的行（kernel 0/130、actively exploited、man-in-the-middle、eavesdrop、sandbox、zero-click、encrypt、in the wild）比初稿的 0 可信得多——它们在覆盖完整语料的解析下依然为 0。反过来，初稿里每一个"0 结论"都必须重新问一次。

**由此得到一条通用纪律，写进附表 D：** 任何"全语料 0 次"或"某族 0 段"的结论，除了给出检索词和分母，还必须额外回答一句"**带后缀类名的那批页面被解析器看见了吗**"。最省事的自检法是取一个已知大页做探针：`149034` 在容错口径下实测 209 段，任何解析器若在该页命中 0 段或远小于 209 段，一律作废，不得引用其任何计数。

**但探针必须按类名选，不能按页号选。** 本篇复算时发现一个反例，值得单独写下来：语料里按 `Released` 字段最新的一批是 **149226（iOS/iPadOS 26.7.1）、149228（macOS Tahoe 26.7.1）、149229（macOS Sequoia 15.8.1）**，三页均 `Released September 28, 2026`，页号比上面那十页更大，但它们用的是**旧代裸类名** `<h3 class="gb-header">`（实测每页 1 个块首、9 个 `gb-paragraph`），两种解析器都照样看见。反过来，149034–149043 这十页的 `Released` 字段是 September 14, 2026，条目日期却是 October 19, 2026。**结论：类名换代与页面新旧、页号大小、Released 日期、Entry 日期四者都不同步**，所以"取最新页做探针"这种写法本身就是缺陷——探针要选的是"已知含带后缀类名的那一页"。本篇全部计数不受影响（这三页各 1 段 `CoreGraphics`，不属于本篇两族；1,430 段差额仍逐页闭合在十页内），但本篇原先"按页号取最新批"的表述已在第 12.1、13.5、16.3、17、18 章与附表 D 统一改为"标记带后缀的那批页面"。

**跨篇登记。** 报告 #100–#103（含 #103 的近场 148 段普查）已按旧分母发出，无法撤回；它们的全部"0 结论"都受同一缺陷影响，其中 #103 的"近场族无 remote attacker"一句已被本篇第 13.2 节推翻。本篇不修改已发布文件，只在读者可能把 #103 与 #104 并读的位置交叉引用。第 17 章缺口登记与边界声明同步记录此事。
另登记一条本篇在复算时才发现的**转录误差**：#105 与 #106 已发布文本把严格口径的旧值写成 **7,620 段 / 442 个段落名**，本篇用同一套两种解析重跑同一镜像，实测严格值是 **7,615 段 / 439 名**（容错值 9,045 / 470 两篇均正确，漏采 1,430 段也正确）。也就是说这两篇里"漏采数 = 十页之和"的论证不受影响，受影响的是它们正文引用的那个旧基数值本身——引用时应以 7,615 / 439 为准。该误差属于本篇第 16.5 节"普查方法本身要交代"的同类问题，登记于此而不回头修改已发布文件。

## 13.6 因重测而可见的 11 条条目：原文照抄

那 70 个新增可见段落可以按字段完整拆开：**53 段带 Impact + Description，17 段是纯致谢**（`CoreBluetooth - LE` 的 5 段全部为纯致谢，`Bluetooth` 8 段、`Wi-Fi` 4 段）。53 段背后是 **11 条本报告此前没有引用的 CVE**，逐条列在下面，Impact / Description 原样照抄，页号给全。

| CVE | 段落名 | 段数 / 页号 | Impact 原文 | Description 原文 | 条目备注 |
|---|---|---|---|---|---|
| CVE-2026-65414 | Bluetooth | 8 / 149034–149038、149041、149042、149043 | A remote attacker may be able to cause unexpected app termination or arbitrary code execution | An out-of-bounds write issue was addressed with improved bounds checking. | 无 |
| CVE-2026-84613 | Bluetooth | 8 / 同上 8 页 | An attacker in radio range may be able to cause unexpected system termination | An out-of-bounds write issue was addressed with improved bounds checking. | Entry added October 19, 2026 |
| CVE-2026-84547 | Bluetooth | 3 / 149035、149042、149043 | An attacker in physical proximity may be able to corrupt process memory | A memory corruption issue was addressed with improved input validation. | Entry added October 19, 2026 |
| CVE-2026-84560 | Bluetooth | 5 / 149034–149038 | An app may gain unauthorized access to Bluetooth | An authorization issue was addressed with improved state management. | 无 |
| CVE-2026-84631 | Bluetooth | 1 / 149035 | An app may be able to gain root privileges | This issue was addressed with additional entitlement checks. | 无 |
| CVE-2026-86919 | Core Bluetooth | 6 / 149034、149035、149038、149041、149042、149043 | An attacker with a paired Bluetooth device may be able to execute arbitrary code | A stack overflow was addressed with improved input validation. | Entry added October 19, 2026 |
| CVE-2026-86891 | Core Bluetooth | 4 / 149035、149037、149042、149043 | An app may be able to access Bluetooth device information | An authorization issue was addressed with improved state management. | 无 |
| CVE-2026-86957 | Wi-Fi | 8 / 149034–149038、149041、149042、149043 | An attacker in wireless proximity may be able to cause unexpected system termination | An out-of-bounds read was addressed with improved bounds checking. | Entry added October 19, 2026；**149037 那页写的是 Entry updated** |
| CVE-2026-86953 | Wi-Fi | 5 / 149034–149038 | An attacker in physical proximity may be able to cause a denial-of-service | An out-of-bounds write issue was addressed with improved bounds checking. | Entry added October 19, 2026 |
| CVE-2026-43674 | Wi-Fi | 1 / 149034 | An attacker with physical access to an unlocked device may be able to view Wi-Fi passwords without authentication | An authentication issue was addressed with improved state management. | 无 |
| CVE-2026-84636 | Wi-Fi Connectivity | 4 / 149034、149036、149037、149038 | An app may be able to access sensitive user data | An authorization issue was addressed with improved state management. | **四页全部是 Entry updated** October 19, 2026 |

这批条目给出四个本篇必须记录的事实。

**一、CVE-2026-84560 的 Impact 是本族所有"以施事者为主语"的句子里唯一省略 `be able to` 的一条。** 其余一律写 "An app may **be able to** …" / "An attacker … may **be able to** …"，这一条写 "An app may gain unauthorized access to Bluetooth"。顺带记录句式对照：两族里**主语不是攻击者也不是 app** 的 Impact 只有两种，共 10 段——`A file received from AirDrop may not have the quarantine flag applied`（AirDrop 3 段）与 `Parsing a file may lead to an unexpected app termination`（afclip 7 段）。这是模板外的措辞，本篇照抄、不做规范化，也不推断是笔误还是有意区分。

**二、授权类修复句式在这批里集中出现。** `An authorization issue was addressed with improved state management.` 一条句式覆盖 CVE-2026-84560（5 段）、CVE-2026-86891（4 段）、CVE-2026-84636（4 段）、CVE-2026-43674（1 段）；全族 `authorization issue` 命中蓝牙族 9 段 + Wi-Fi 族 4 段 = 13 段，`state management` 命中 19 + 5 = 24 段。**这批新条目里，"app 越权访问蓝牙 / 访问蓝牙设备信息 / 访问敏感用户数据"三种后果共用同一个修复描述**，Apple 没有区分它们各自的授权模型。

**三、CVE-2026-43674 是全族唯一一条把"看到 Wi-Fi 密码"写成后果的条目，且只出现在 1 个产品线页面（149034）。** 它的可达性主语是 `An attacker with physical access to an unlocked device`、条件是 `without authentication`。这条与第 7.1 节手册层的"无线局域网密码共享"在主题上相邻，但**措辞层没有把它归入任何连续互通通道**，本篇也不做这个连接；它在此只作为"物理接触 + 已解锁 + 免认证"这一新型前置条件被登记。全语料 `physical access to an unlocked device` 在本两族仅此 1 段。

**四、条目备注字段本身会不一致。** 同一 CVE 的不同产品线页面上，149037 那页对 CVE-2026-86957 写 `Entry updated`，其余 7 页写 `Entry added`；而 CVE-2026-84636 的四页全部写 `Entry updated`。引用时必须点名是哪一页的哪一个字段，不得把"Entry added October 19, 2026"当成该 CVE 的统一日期。这也是第 17 章把"页面发布日期"与"条目增改日期"列为两个独立字段的原因。

## 13.7 句式清点：230 段里有 150 段带 Impact，而 Impact 只有 30 种句子

把两族每一段的 Impact 原文取全并按句子归并，得到的分布是本篇最容易被读者低估的一条：**措辞层的近场叙述不是"很多条不同的说明"，而是 30 个模板句在 150 个位置上的填空**。剩下 80 段是纯致谢（无 CVE、无 Impact）：蓝牙族 54 段、Wi-Fi 族 26 段。

下面这张表按"段落名 × 句子"逐行展开，因此是 **35 行**而不是 30 行——差出来的 5 行来自两句话跨了多个段落名：`An app may be able to access sensitive user data` 占 5 个段落名（贡献 4 行重复）、`An attacker in physical proximity may be able to corrupt process memory` 占 2 个段落名（贡献 1 行重复）。按段落名列行是为了让"某个后果挂在哪条通道名下"可读，**去重后的句子种数才是 30**。

| 段落名 | Impact 句子原文 | 段数 |
|---|---|---|
| Bluetooth | A remote attacker may be able to cause unexpected app termination or arbitrary code execution | 8 |
| Bluetooth | An attacker in radio range may be able to cause unexpected system termination | 8 |
| Bluetooth | An app may be able to access sensitive user data | 6 |
| Bluetooth | An app may gain unauthorized access to Bluetooth | 5 |
| Bluetooth | An attacker in a privileged network position may be able to perform denial-of-service attack using crafted Bluetooth packets | 5 |
| Bluetooth | An attacker in physical proximity may be able to corrupt process memory | 3 |
| Bluetooth | An attacker in a privileged network position may be able to inject keystrokes by spoofing a keyboard | 2 |
| Bluetooth | An app may be able to gain root privileges | 1 |
| Bluetooth | An attacker within Bluetooth range may be able to listen through the microphone of a device which is not yet paired and actively seeking pair requests | 1 |
| Bluetooth | When your headphones are seeking a connection request to one of your previously paired devices, an attacker in Bluetooth range might be able to spoof the intended source device and gain access to your headphones | 1 |
| Core Bluetooth | An attacker with a paired Bluetooth device may be able to execute arbitrary code | 6 |
| Core Bluetooth | An app may be able to access sensitive user data | 5 |
| Core Bluetooth | An app may be able to access Bluetooth device information | 4 |
| Core Bluetooth | A malicious Bluetooth input device may bypass pairing | 1 |
| CoreBluetooth - LE | An app may be able to access Bluetooth-connected microphones without user permission | 4 |
| AirDrop | An app may be able to read arbitrary file metadata | 6 |
| AirDrop | An attacker in a privileged network position may be able to cause a denial-of-service | 5 |
| AirDrop | A file received from AirDrop may not have the quarantine flag applied | 3 |
| Handoff | An app may be able to access sensitive user data | 2 |
| Wi-Fi | An app may be able to cause unexpected system termination or corrupt kernel memory | 11 |
| Wi-Fi | An attacker in physical proximity may be able to cause an out-of-bounds read in kernel memory | 8 |
| Wi-Fi | An attacker in wireless proximity may be able to cause unexpected system termination | 8 |
| Wi-Fi | An attacker in a privileged network position may be able to perform denial-of-service attack using crafted Wi-Fi packets | 7 |
| Wi-Fi | An attacker in physical proximity may be able to corrupt process memory | 6 |
| Wi-Fi | An attacker in physical proximity may be able to cause a denial-of-service | 5 |
| Wi-Fi | An app may be able to execute arbitrary code with kernel privileges | 4 |
| Wi-Fi | An attacker may be able to force a device to disconnect from a secure network | 3 |
| Wi-Fi | An attacker with physical access to a device may be able to override managed Wi-Fi profiles | 2 |
| Wi-Fi | A non-privileged user may be able to modify restricted network settings | 1 |
| Wi-Fi | An app may be able to cause a denial-of-service | 1 |
| Wi-Fi | An app may be able to read sensitive location information | 1 |
| Wi-Fi | An attacker with physical access to an unlocked device may be able to view Wi-Fi passwords without authentication | 1 |
| Wi-Fi Connectivity | An app may be able to access sensitive user data | 4 |
| afclip | Parsing a file may lead to an unexpected app termination | 7 |
| Clipboard | An app may be able to access sensitive user data | 5 |

四件事直接可以从这张表读出来：

1. **`An app may be able to access sensitive user data` 横跨五个段落名**（Bluetooth 6、Core Bluetooth 5、Handoff 2、Wi-Fi Connectivity 4、Clipboard 5 = 22 段），在全语料更是 632 段 / 119 页。这是措辞层复用率最高的一句。**同义句在 `Core Bluetooth` 与 `Bluetooth` 之间还各写了一遍**，因此"该句属于哪条通道"无法从段落名反推——它只能说明某个 app 越权访问了某类数据，访问对象由该页段落名给出，仅此而已。
2. **以攻击者为行为主体的句子两族各有 8 种**（蓝牙族 8：remote attacker、radio range、physical proximity、PNP×2、within Bluetooth range、in Bluetooth range、with a paired Bluetooth device；Wi-Fi 族 8：physical proximity×3、wireless proximity、PNP、未加限定的 force-disconnect、physical access to a device、physical access to an unlocked device）；`CoreBluetooth - LE` 名下**一种都没有**，只有 4 段 app 越权访问麦克风。也就是说：这一族里最接近"框架 API"的段落名，措辞层给出的全部是本地 app 越权，一条近场攻击者都没有。
3. **`A file received from AirDrop may not have the quarantine flag applied` 有一句同构孪生，但不在本篇两族里**：`Files downloaded from the internet may not have the quarantine flag applied`，段落名 `iCloud`，页 122068。全语料该句式只有 4 段，AirDrop 3 + iCloud 1。**这是措辞层里唯一一处把"下载/接收文件的隔离标记"这个后果写成两种来源**，也是本篇第 11.2 节"手册层没有任何 quarantine 表述"这一负结果在措辞层侧的对照物。引用时仍受第 12.2 节约束：`iCloud` 段落与 AirDrop 段落是两个段落名，不得互推。
4. **句子数量与缺陷数量无关。** 30 句 × 若干产品线 = 150 段；CVE-2026-65414 那句 "unexpected app termination or arbitrary code execution" 在全语料出现 89 段 / 46 页，是一条被大量复用的模板句，不代表该后果更常见或更罕见。

---

# 14 AirDrop 与 Handoff 段落：三个 CVE、一段纯致谢，以及"没有的一条"

## 14.1 全量清单

AirDrop 段落共 **20** 段，其中带 CVE 的条目 3 个；Handoff 段落共 **6** 段，其中带 CVE 的条目 1 个。逐条（Impact / Description 均为顾问页原句）：

**CVE-2024-54564｜AirDrop｜首次出现在 iOS/iPadOS 17.6（页 120909）**
- Impact：「A file received from AirDrop may not have the quarantine flag applied」
- Description：「This issue was addressed through improved state management.」
- 同 CVE 亦见 macOS Sonoma 14.6（120911）、visionOS 1.3（120915）
- 条目备注：Entry added March 20, 2025

这一条的措辞值得逐词读。它说的是**文件没有被施加隔离标记**（quarantine flag），后果是"may not have … applied"——不是"文件可执行"，不是"代码被执行"。第 11.2 节已测得手册层取样页 0 次出现 quarantine，中文全量的"隔离"六处语义均与此无关；因此"隔离标记"这个概念**只在措辞层存在**，全篇引用它时必须给页号。

本次重测补上一条同构句：`Files downloaded from the internet may not have the quarantine flag applied`，段落名 `iCloud`，页 122068。全语料 `may not have the quarantine flag applied` 只有 4 段（AirDrop 3 + iCloud 1），**换句话说，措辞层里"隔离标记未施加"这一后果只被写给两个来源：隔空投送收到的文件，与从互联网下载的文件**。这条并列是本篇能支持的最强表述；它**不**表示两者共用同一实现，也不表示修一处会影响另一处（`iCloud` 段落同样受第 12.2 节约束，与 AirDrop 是两个段落名）。

**CVE-2025-24097｜AirDrop｜首次出现在 iOS/iPadOS 18.4（页 122371）**
- Impact：「An app may be able to read arbitrary file metadata」
- Description：「A permissions issue was addressed with additional restrictions.」
- 致谢：Ron Masas of BREAKPOINT.SH
- 同 CVE 覆盖 macOS 15.4（122373）、macOS 14.7.5（122374）、watchOS 11.4（122376）、tvOS 18.4（122377）、iPadOS 17.7.7（122405）

这一条把 **AirDrop 段落与"app 可读任意文件元数据"**连起来。注意主语是 app，不是近场攻击者：措辞层没有说这需要处于附近。第 13.2 节显示蓝牙族 `privileged network position` 有 12 段，但这一条不在其中。把这条写成"邻近攻击者可通过 AirDrop 读取元数据"是越界。

**CVE-2026-43667｜AirDrop｜首次出现在 iOS/iPadOS 26.5（页 127110）**
- Impact：「An attacker in a privileged network position may be able to cause a denial-of-service」
- Description：「A reachable assertion was addressed with improved input validation.」
- 致谢：Arash Ale Ebrahim（CISPA SCy-Phy）
- 同 CVE 见 macOS 26.5（127115）、watchOS 26.5（127119）、visionOS 26.5（127120），以及**遗留分支 iOS/iPadOS 18.7.10（148287）**
- 条目备注：Entry added August 25, 2026

这是本篇唯一一条把"特权网络位置"与 AirDrop 连起来的条目，也是唯一一条 Description 写「A reachable assertion was addressed」（一处可达断言）的近场条目。断言类缺陷的后果被 Apple 写成 denial-of-service，本篇照此引用，不推断其可否升级。

**CVE-2025-30463｜Handoff｜iOS/iPadOS 18.4（页 122371）**
- Impact：「An app may be able to access sensitive user data」
- Description：「The issue was addressed with improved restriction of data container access.」
- 致谢：mzzzz__

Handoff 段落里唯一的 CVE。Description 的修复对象是**数据容器访问范围**（data container access），这是措辞层里唯一一次把接力与"容器隔离范围"连起来。它同样不宣称任何近场前提。

**纯致谢段落（真实存在，但没有 CVE）**
- AirDrop｜iOS/iPadOS 17.4（120893）：致谢 Cristian Dinca（Tudor Vianu 国立计算机科学中学，罗马尼亚）；Entry added March 7, 2024
- Handoff｜iOS 18.4 / macOS 15.4 / watchOS 11.4 / tvOS 18.4（122371、122373、122376、122377）：致谢 Kirin and FlowerCode

凡引用这类段落，只能写成"Apple 在某页致谢了某人，且未给出对应 CVE"，不得反推存在某机制缺陷。

## 14.2 关键负结果：这六类叙述无法用 Apple 自己的措辞支撑

在 AirDrop 与 Handoff 两个段落名的全部 26 段里（AirDrop 20 + Handoff 6；容错口径与严格口径对这个数没有差别，因为标记带后缀的 10 页里没有一条 AirDrop 或 Handoff 条目），没有任何一条 Impact 或 Description 出现下列语义。表末一行为对照而列，口径是整个蓝牙/近场身份族 130 段：

| 想写的话 | 措辞层是否存在 | 依据 |
|---|---|---|
| "AirDrop/接力的配对或身份验证被绕过" | **在这 26 段内不存在** | pairing 在两族 230 段内仅 1 段——`Core Bluetooth` 名下 CVE-2024-44124，Impact 原句 `A malicious Bluetooth input device may bypass pairing`（见 #103）；AirDrop/Handoff 26 段内 0 次；`pairing` 在全语料也只有 8 段 |
| "AirDrop 广告/流量被窃听" | **不存在** | eavesdrop 两族 0/230；且该词形在**全语料 9,045 段内也是 0**，属于措辞层整体不用词 |
| "接力被中间人利用" | **不存在** | man-in-the-middle 两族 0/230；同样是**全语料 0** |
| "某近场漏洞被在野利用" | **不存在** | actively exploited 两族 0/230，但**全语料有 18 段 / 13 页**——所以这是"本族回避"，不是"Apple 从不写" |
| "近场通道导致内核代码执行" | **不存在**（在 AirDrop/Handoff 段落里）；kernel 两族 0/130 | 第 13.2 节结论一 |
| "AirDrop/Handoff 段落的条目被写成远端攻击者可达" | **不存在，但整条近场族已不再成立** | `remote attacker` 在 AirDrop 20 段与 Handoff 6 段内为 0，但在 `Bluetooth` 名下有 8 段（CVE-2026-65414）；本行初稿写的是"两族 0"，已在第 13.5 节作废 |

这一节的写法约定是：本篇可以陈述这些沉默，并把它作为"读者应如何评价外部说法"的判据；不可以用报告 #103 里 `Bluetooth` 段落的条目去填空——那是不同段落名、不同组件。

## 14.3 三个 CVE 的共同形状

把 14.1 的三条 CVE 排在一起，会出现一个一致的模式：**AirDrop 段落的缺陷全部落在"文件与元数据的处理与标记"上，没有一条落在"身份识别与协商"上**。第 5 章花整章描述的两级身份哈希、RSA 身份、证书交换，在措辞层是零条目。

这不是"AirDrop 身份层无漏洞"的证据——只能说 Apple 披露的 3 条都在文件侧，第 17 章 G7 登记。同时它也解释了为什么第 18 章的检测建议偏向文件系统与隔离标记核验，而不是射频侧观测。

---

# 15 Wi-Fi / afclip / Clipboard 段落：近场可达性与内核后果的交汇只有一处

本章处理第 13 章分出来的另一族。这一族之所以进本篇，是因为它承载了近场条目里**唯一**的内核证据；它同时也承载"物理邻近"证据的多数（19/22 段），但**不再是唯一**——容错重测后蓝牙族也有 3 段 `physical proximity`（第 13.2 节结论二作废）。这一族还最容易写错——段落名不等于产品通道。

## 15.1 Wi-Fi 段落里与本篇判断相关的条目

| CVE | 页号与产品 | Impact 原句 | Description 原句 | 致谢 |
|---|---|---|---|---|
| CVE-2024-44227 / CVE-2024-54546 | 121238 macOS Sequoia 15；121250 iOS/iPadOS 18（44227） | An app may be able to cause unexpected system termination or corrupt kernel memory | The issue was addressed with improved memory handling. | Tim Michaud（Moveworks.ai）；Antonio Zekic 与 ant4g0nist |
| CVE-2024-40770 | 121238 | A non-privileged user may be able to modify restricted network settings | A permissions issue was addressed with additional restrictions. | Yiğit Can YILMAZ |
| CVE-2024-23237 | 121238 | An app may be able to cause a denial-of-service | The issue was addressed with improved memory handling. | Charly Suchanek |
| CVE-2024-44134 | 121238 | An app may be able to read sensitive location information | This issue was addressed with improved redaction of sensitive information. | — |
| CVE-2024-40856 | 121238、121248、121250 | An attacker may be able to force a device to disconnect from a secure network | **An integrity issue was addressed with Beacon Protection.** | Preet Dsouza、Domien Schepers |
| CVE-2025-43374 | 122069、122070、122404、122405、122716、122720、122721、122722 | **An attacker in physical proximity may be able to cause an out-of-bounds read in kernel memory** | An out-of-bounds read was addressed with improved bounds checking. | jioundai（360 漏洞研究院）、chen fengjiao（HBC） |
| CVE-2025-31216 | 122404、122405 | An attacker with physical access to a device may be able to override managed Wi-Fi profiles | The issue was addressed with improved checks. | Thibaud Kehler |
| CVE-2025-43373 | 125634、125635、125636 | An app may be able to cause unexpected system termination or corrupt kernel memory | The issue was addressed with improved memory handling. | Wang Yu（Cyberserval） |
| CVE-2026-20621 | 126346–126350、126353 | 同上（corrupt kernel memory） | 同上（improved memory handling） | Wang Yu（Cyberserval） |
| CVE-2026-28819 | 127111 iOS 18.7.9；127115 macOS 26.5；127116 Sequoia 15.7.7；127117 Sonoma 14.8.7 | **An app may be able to execute arbitrary code with kernel privileges** | An out-of-bounds write issue was addressed with improved bounds checking. | Wang Yu |
| CVE-2026-28994 | 127110–127119 | An attacker in a privileged network position may be able to perform denial-of-service attack using **crafted Wi-Fi packets** | A use after free issue was addressed with improved memory management. | Alex Radocea |
| CVE-2026-64726 | 128066–128070 iOS/macOS/watchOS/tvOS/visionOS 26.6；148287 iOS 18.7.10 | **An attacker in physical proximity may be able to corrupt process memory** | The issue was addressed with improved memory handling. | Mathis Mansière、Peter Malone |
| CVE-2026-86957 | 149034–149038、149041、149042、149043（8） | **An attacker in wireless proximity may be able to cause unexpected system termination** | An out-of-bounds read was addressed with improved bounds checking. | — |
| CVE-2026-86953 | 149034–149038（5，不含遗留分支与 Mac 老分支） | **An attacker in physical proximity may be able to cause a denial-of-service** | An out-of-bounds write issue was addressed with improved bounds checking. | — |
| CVE-2026-43674 | 149034（仅 1 页） | An attacker with physical access to an unlocked device may be able to **view Wi-Fi passwords without authentication** | An authentication issue was addressed with improved state management. | — |

`Wi-Fi` 段落名共 79 段，其中 **58 段带 Impact、21 段是纯致谢**。这 21 段的页号可以完整列出：120903、121248、125108、125109、125110、125111、125112、125114、125115、125116（10 段，致谢 Aobo Wang、Csaba Fitzl of Kandji、Noah Gregory、Wojciech Regula、匿名研究者等）、126792、126794、126797、126798、126799（5 段，致谢 Kun Peeks、匿名研究者）、127110（Yusuf Kelany）、127115（Kun Peeks），以及**因重测才可见的 4 段：149034、149036、149037、149038**。注意 125113 不在其中——该页没有 `Wi-Fi` 纯致谢块，凡是把 "125108–125116" 当成 9 段来引用的写法都要回到页号清单。

## 15.2 Wi-Fi 族全部 8 种攻击者主语句：可达性与后果的完整配对

第 13.7 节的句式清点给出 Wi-Fi 段落名下 8 种以攻击者为主体的 Impact。把它们与后果、修复对象排在一起，是本族唯一一张可以穷尽的配对表（不存在"还有第九条没列"的可能，因为该表来自全部 79 段的字段抽取，而不是取样）：

| CVE | 段数 | 可达性（Apple 原话） | 后果（Apple 原话） | 修复对象 |
|---|---|---|---|---|
| CVE-2025-43374 | 8 | **an attacker in physical proximity** | **an out-of-bounds read in kernel memory** | improved bounds checking |
| CVE-2026-86957 | 8 | **an attacker in wireless proximity** | unexpected system termination | out-of-bounds read + improved bounds checking |
| CVE-2026-28994 | 7 | **an attacker in a privileged network position** | denial-of-service（crafted Wi-Fi packets） | use after free + improved memory management |
| CVE-2026-64726 | 6 | **an attacker in physical proximity** | corrupt **process** memory | improved memory handling |
| CVE-2026-86953 | 5 | **an attacker in physical proximity** | a denial-of-service | out-of-bounds write + improved bounds checking |
| CVE-2024-40856 | 3 | an attacker（未加位置限定） | force a device to disconnect from a secure network | **Beacon Protection** |
| CVE-2025-31216 | 2 | an attacker **with physical access to a device** | override managed Wi-Fi profiles | improved checks |
| CVE-2026-43674 | 1 | an attacker **with physical access to an unlocked device** | **view Wi-Fi passwords without authentication** | authentication issue + improved state management |

这张表给出本篇在第 15 章的全部可写内容：

- **要写"近场可达性造成内核内存越界读取"，唯一证据仍是 CVE-2025-43374**（8 段，全在 `Wi-Fi` 名下；覆盖 macOS 14.7.3、13.7.3、iOS 18.5、iPadOS 17.7.7、macOS 15.5、tvOS 18.5、visionOS 2.5、watchOS 11.5，备注 Entry added November 21, 2025）。**这条"唯一"没有因为重测而失效**：新增的 CVE-2026-86957 虽然同为 out-of-bounds read + 无线邻近，但它的后果写的是 unexpected system termination，**Impact 里没有出现 kernel 一词**（第 13.2 节结论一：全族 kernel 23 段一条未增）。引用必须写成"Apple 把这条归入 `Wi-Fi` 段落"，不得写成"Apple 说这是蓝牙/AirDrop 漏洞"。
- **"物理邻近"现在有四种不同后果**：内核越界读（43374）、进程内存损坏（64726）、拒绝服务（86953）、以及蓝牙族新增的一条（84547，进程内存损坏）。它们之间不可互换，且 86953 只覆盖 5 个产品线页面、64726 覆盖 6 个。
- **`physical proximity` 与 `wireless proximity` 在同族同期并存**（86953 用前者、86957 用后者，两条 CVE 同属 `Wi-Fi`、同日 Entry added October 19, 2026，后果一个 DoS 一个系统终止）。Apple 没有说明两个词的区别，本篇也不推断；能写的只有"两者各只绑定一条 CVE"。
- **要写"内核代码执行"，唯一证据是 CVE-2026-28819，但它的主语是 app**（An app may be able to execute arbitrary code with kernel privileges），与"需要靠近"无关。更重要的是：这条只出现在 iOS 18.7.9 与 macOS 三个老分支（26.5 / 15.7.7 / 14.8.7），**未出现在 iOS 26.5**（第 16.3 节展开）。
- **CVE-2024-40856 是本族唯一一条 Description 给出具体防护机制名的**（Beacon Protection），Impact 是"强制设备从安全网络断开"。这句在措辞层里独一份：Apple 在这里既没说内存也没说校验，而是点名了一个 802.11 层的保护特性；全语料 `Beacon protection` 也只有这 3 段。
- **CVE-2026-43674 把"看到 Wi-Fi 密码"写成后果，且前提是"物理接触 + 已解锁 + 免认证"三件事同时成立**，只出现在 1 个产品线页面。这条与第 7.1 节手册层的"无线局域网密码共享"主题相邻，但措辞层没把它归入任何连续互通通道，本篇也不做这个连接。

## 15.3 afclip：一个属于近场文件栈、但措辞层没把它说成 AirDrop 的段落

CVE-2025-43186 出现在 **7** 份页面的 `afclip` 段落名下：iOS/iPadOS 18.6（124147）、124149、124150、124151、124153、124154、124155（覆盖 macOS 15.6 / 14.7.7 / 13.7.7、tvOS 18.6、visionOS 2.6、watchOS 11.6）。
- Impact：「Parsing a file may lead to an unexpected app termination」
- Description：「The issue was addressed with improved memory handling.」
- 致谢：Hossein Lotfi（Trend Micro Zero Day Initiative）

三点必须如实处理：
1. 段落名 `afclip` 是 Apple 直接给出的字符串，可原样引用；**但**它在措辞层与哪条产品通道对应，Apple 未说明（第 17 章 G9）。
2. Impact 的主语与后果是"解析文件导致**某个 app** 意外终止"——不是系统终止，不是内核。第 13.2 节已测得该族 kernel 有 23 段，但 `afclip` 这 7 段不在其中。
3. 进程名 afclip 属于本地文件共享栈，这属于系统与开发者文档层面的常识；本篇按纪律写成"第三方/文档口径"，不写成手册层结论，也不据此说它就是 AirDrop 的实现。它与 CVE-2024-54564（AirDrop 段落、文件隔离标记）之间的联想尤其危险：两者主题相邻、语义相似，但段落名不同、后果不同、版本不同，**不得**互相指认。

## 15.4 Clipboard：一处措辞层模板复用造成的语义不连贯

CVE-2026-28866 出现在 **5** 份页面的 `Clipboard` 段落名下：iOS/iPadOS 26.4（126792）、iOS/iPadOS 18.7.7（126793）、macOS 26.4（126794）、Sequoia 15.7.5（126795）、Sonoma 14.8.5（126796）。
- Impact：「An app may be able to access sensitive user data」
- Description：「This issue was addressed with improved validation of symlinks.」
- 致谢：Cristian Dinca（icmd.tech）

第 11.2 节测得「符号链接」在中文手册全量 179 页里 **0 次**。也就是说，这条 Description 的核心词在手册层不存在，只在这一族措辞层出现 5 段。Impact 说"访问敏感用户数据"，Description 说"改进符号链接校验"，两者之间缺少一句说明；本篇**不替 Apple 圆场**，也不推断它实际修的是哪一层。这条的价值恰恰是反面教材：措辞层是模板化文本，Impact 与 Description 之间存在复用错配，引用时必须两句同引、不得只取其一。

另一个可引用的关联事实：致谢对象 Cristian Dinca 同时出现在 AirDrop 纯致谢段落（120893，Entry added March 7, 2024）与这条 Clipboard 条目。同一个人、两个不同段落名、相隔两个版本年。**这不能用来推断两条缺陷同源**，但可以作为"近场文件与剪贴板栈被同一批研究者覆盖"这一观察的支撑，并且要标注为观察。

## 15.5 Wi-Fi Connectivity：一个原本"只有致谢"的段落名，现在也有 CVE 了

容错重测前，`Wi-Fi Connectivity` 共 5 段、全部**没有 CVE**：126792、126794、126797、126798、126799，致谢 Alex Radocea（Supernetworks, Inc）。而同一位研究者 Alex Radocea 是 CVE-2026-28994（`Wi-Fi` 段落、crafted Wi-Fi packets、use after free）的致谢对象。

重测后该段落名 **9 段 / 9 页**：旧的 5 段纯致谢不变，新增 4 段带完整字段——**CVE-2026-84636**，Impact「An app may be able to access sensitive user data」，Description「An authorization issue was addressed with improved state management」，致谢 Sindre Sorhus、Jian Lee，四页（149034、149036、149037、149038）**备注全部是 Entry updated October 19, 2026**。

这给出一个必须写下来的方法学教训：**"某段落名全部是纯致谢、没有 CVE"是一类极易被采集缺陷造出来的假发现。** 严格解析器看不见标记带后缀的 10 页，于是这个段落名看起来"历史上从未有过条目"；补上 4 段之后，它不仅有 CVE，而且四条都是同一句复用率最高的模板句。第 13.5 节的自检法（用 149034 的 209 段做探针）正是针对这类假发现。

对第 12.2 节的纪律则是一个正面补充：段落名之间存在**同一组件被拆成两个名字**的情况（Wi-Fi 与 Wi-Fi Connectivity），而 Apple 没解释两者区别；同一句 Impact 也会同时出现在这两个段落名下（149034 一页里 `Wi-Fi` 与 `Wi-Fi Connectivity` 两个块并存）。因此引用时不能假定段落名有稳定的工程含义——第 16.1 节给出这个论断的完整表述。

## 15.6 七个"相邻段落名"：连续互通的组件在顾问页上有名无条目

容错重测除了找回本篇两族的 70 段，还让 **31 个段落名第一次进入统计**（严格口径下 439 名 → 容错口径 470 名）。这 31 个名里有七个与本篇主题直接相邻，全部集中在标记带后缀的那 10 页（149034–149043）：

| 段落名 | 段数 / 页号 | 有无 CVE | 原文（照抄） |
|---|---|---|---|
| `rapportd` | 3 / 149041、149042、149043 | **无** | 纯致谢 Tae Woo Kim |
| `Identity Services` | 1 / 149035 | **无** | 纯致谢 Ismael Esquilichi、Pablo Redondo（cmdresearch） |
| `AutoFill` | 1 / 149034 | **无** | 纯致谢 Bistrit Dahal、Oussama Barbar、SalahAldeen Yousef |
| `Local Authentication` | 1 / 149035 | **无** | 纯致谢 Ashish Kunwar |
| `Remote Control` | 1 / 149036 | **无** | 纯致谢 Ruslan Dautov |
| `CoreDrag` | 3 / 149035、149042、149043 | CVE-2026-43683 | Impact「An app may be able to cause unexpected process termination or disclose process memory」；Description「An out-of-bounds read was addressed with improved bounds checking」；致谢 Nathaniel Oh（@calysteon） |
| `DeviceCheck` | 24 / 8 页 | CVE-2026-84612 等 | Impact「An app may be able to read persistent device identifiers」；Description「An authorization issue was addressed with **improved access control**」 |

三条只能写到这里的读法，和三条必须挡住的推断。

**读法一：这七个名里有五个是纯致谢。** `rapportd`、`Identity Services`、`AutoFill`、`Local Authentication`、`Remote Control` 五个段落名在本轮语料里**一条 CVE 都没有**，只有"Apple 在某页致谢某人"。它们的主题分别是接续与发现的守护进程、身份服务、密码自动填充、本机认证、遥控——正好是第 2、7.2、7.3、7.4、9 章手册层反复描述的那几条通道。本篇据此能说的只有一句话：**Apple 为这些组件开设了顾问页段落名，但在本轮 257 份页面里，它们没有对应条目**。这与第 14.2 节的负结果同构：手册层描述得越细的通道，措辞层越安静。

**读法二：`rapportd` 是本篇两族之外唯一以"进程名"作段落名的例子（与 `afclip`、`autofs`、`odproxyd`、`tapi` 同一类命名法）。** 第 15.3 节对 `afclip` 立的规矩在这里原样适用：**Apple 没有说明 `rapportd` 对应哪条产品通道**，因此不得写成"AirDrop/接力的实现组件 `rapportd` 被致谢"。可写的形式只有"`rapportd` 段落名出现在 149041–149043 三页、只有致谢、无 CVE"。注意这三页恰好都是遗留分支（iOS/iPadOS 26.7、macOS Tahoe 26.7、macOS Sequoia 15.8），而 27 主线的五页（149034–149038）里没有这个名——这与第 16.3 节"段落名在某线缺席"是同一现象，不能作为"该组件只在旧线"的证据。

**读法三：`CoreDrag` 是拖放，不是剪贴板。** 第 15.4 节处理过 `Clipboard` 名下"Impact 与 Description 语义不连贯"；本轮出现的 `CoreDrag` 名下三条（149035、149042、149043）字段完整、后果写"进程终止或泄露进程内存"。**手册层的"通用剪贴板"（2.5、4.2）与这两个名都没有对应关系可查**，本篇不得把 `CoreDrag` 读成"跨设备拖放"，也不得把 `Clipboard` 读成"通用剪贴板"——第 16.1 节的规则同样禁止这两个方向。

**挡住的推断一：`DeviceCheck` 与接力的设备真实性无关。** 手册层没在连续互通任何一页提到 DeviceCheck；该名的 24 段全部来自其它主题。**挡住的推断二：`Local Authentication` 名下只有致谢，没有条目**，因此不能用它支撑"本机认证被绕过过"，也不能用它支撑"没被绕过"。**挡住的推断三：`Identity Services` 与手册层的 IDS 是两个来源的两个词。** 手册层第 7.4 节（热点公钥同步）与 8.1 节（Apple Pay 接力承载）明写 "Apple Identity Service (IDS)"；顾问页第 149035 页另有一个 `Identity Services` 段落名且只有致谢。**本篇不做这两个词是同一组件的断言**——它们分属 T1 与 T2，跨层同名不是证据（G1：源码层本轮不可用）。

顺带记录一个修复句式的新变体：`DeviceCheck` 名下 CVE-2026-84612 的 Description 写 "improved **access control**"，而本篇两族里同类授权修复一律写 "improved **state management**"（13 段）。全语料两个词形的分布本轮未做穷尽清点（G11），因此这里只登记"存在第三种修复措辞"，不做句式偏好结论。

---

# 16 结构性事实：六条可以从措辞层直接得出的规则

本章把第 12–15 章的观察压缩成规则，供第 17、18 章与附表 E 直接引用。

## 16.1 段落名不是实现层，而且它自身不稳定

证据链：同一族内 `Bluetooth` / `Core Bluetooth` / `CoreBluetooth - LE` 三种写法并存；`Wi-Fi` 与 `Wi-Fi Connectivity` 两个名字指向相邻语义、Apple 未给区别；`Bluetooth` 段落里出现过"App 可访问敏感用户数据"（CVE-2025-43354，见 #103）与"日志脱敏"类修复；`afclip` 段落的影响主语是 app 终止。

容错重测把这条规则加强了一层：**同一句 Impact 会在五个不同段落名下各写一遍**。`An app may be able to access sensitive user data` 在本篇两族共 22 段，分布在 `Bluetooth`（6）、`Core Bluetooth`（5）、`Handoff`（2）、`Wi-Fi Connectivity`（4）、`Clipboard`（5）——五个名字、一个句子、四类完全不同的数据。全语料该句 632 段 / 119 页。**因此"某条目挂在 AirDrop 名下"能给读者的信息量，比直觉小得多。**

规则：正文引用条目时，段落名照抄、并明确"这是 Apple 的归类，不是实现层判定"。**不得**由段落名推导内核态/用户态、框架/驱动、进程归属；也**不得**由"同一句 Impact"推导两个段落名同源。

## 16.2 词频是"CVE × 产品线覆盖数"的乘积

第 13.3 节的完全分解已经证明：一条 CVE-2025-43374 就贡献 8 段 kernel、8 段 physical proximity、8 段 out-of-bounds；一条 CVE-2026-65414 就贡献 8 段 remote attacker、8 段 arbitrary code execution、16 段（两条 CVE 合计）out-of-bounds 与 bounds checking。所以任何"某词在近场段落里出现 N 次"都必须同时给出它背后的 CVE 数与页号，否则读者会把它读成频次。本报告全文遵守这一规则。

## 16.3 遗留分支与主线并行获补丁，且不是所有条目都进主线

实例：
- CVE-2026-43667（AirDrop，privileged network position DoS）同时出现在 **127110（iOS/iPadOS 26.5）** 与 **148287（iOS/iPadOS 18.7.10）**。
- CVE-2026-64726（Wi-Fi，physical proximity → process memory）同时出现在 **128066–128070（各线 26.6）** 与 **148287（18.7.10）**。
- CVE-2026-28819（Wi-Fi，app → kernel privileges）**只**出现在 **127111（iOS 18.7.9）** 与 macOS 的 127115 / 127116 / 127117，**未出现在 iOS 26.5**。

规则：说"iOS 修复了 X"必须给页号与版本；涉及 18.7.x 的条目要点名它是遗留分支及机型范围（127111 的 Available for 为 iPhone XS、XS Max、XR、iPad 第 7 代）。第 18 章的补丁核验项据此设计。

重测后同一现象在本篇新增条目里更明显，且**方向相反**（不是"老分支独享"，而是"只在新分支"或"只在 Mac 线"）：

| CVE | 段落名 | 出现的页 | 缺席的页 | 可读出的（仅止于） |
|---|---|---|---|---|
| CVE-2026-84547 | Bluetooth | 149035、149042、149043 | 149034（iOS/iPadOS 27）、149036（tvOS 27）、149037（watchOS 27）、149038（visionOS 27）、149041（iOS/iPadOS 26.7） | 该页面上没有这一条；不推断机型是否受影响 |
| CVE-2026-84631 | Bluetooth | 149035 | 其余 7 页 | 同上 |
| CVE-2026-86891 | Core Bluetooth | 149035、149037、149042、149043 | 149034、149036、149038、149041 | 同上 |
| CVE-2026-86919 | Core Bluetooth | 149034、149035、149038、149041、149042、149043 | 149036（tvOS）、149037（watchOS） | 同上 |
| CVE-2026-86953 | Wi-Fi | 149034–149038 | 149041、149042、149043（三条遗留分支） | 该条不进遗留分支 |
| CVE-2026-86957 | Wi-Fi | 149034–149038、149041–149043 | 无 | 主线 + 遗留分支全覆盖 |
| CVE-2026-43674 | Wi-Fi | 149034 | 其余 7 页 | 单页面条目，引用时必须写明只见于 iOS/iPadOS 27 那一页 |
| CVE-2026-84636 | Wi-Fi Connectivity | 149034、149036、149037、149038 | 149035、149041–149043 | 不含任何 macOS 线页面 |

**表里的"其余 7 页"是有依据的，不是笔误。** 这批页面共 10 个：149034（iOS/iPadOS 27）、149035（macOS Golden Gate 27）、149036（tvOS 27）、149037（watchOS 27）、149038（visionOS 27）、149039（Safari 27）、149040（Xcode 27）、149041（iOS/iPadOS 26.7）、149042（macOS Tahoe 26.7）、149043（macOS Sequoia 15.8）。其中 **149039 与 149040 两页完全没有本篇两族的段落名**（前者 6 个段落名、后者 7 个，全是 Safari/Xcode 自身组件），所以任何"近场条目在 10 页里出现几页"的计数都必须先把这两页剔掉，否则会把"该组件不在这条产品线发布"误读成"该缺陷未影响该页面"。另外三条遗留分支（149041–149043）**没有 `Wi-Fi Connectivity` 也没有 `CoreBluetooth - LE` 段落名**——这两个名的条目只在 27 主线页上出现，这是本篇两族里唯二"零遗留分支覆盖"的段落名。

**同一批（同日 Entry added October 19, 2026）条目，产品线覆盖面从 1 页到 8 页不等。** 这是第 16.2 节"段数 = CVE × 覆盖数"的第二个证据：8 段的 84613 与 3 段的 84547 后果都写"unexpected/system 或 memory"级别，段数差异完全来自覆盖页面数，不来自严重性。

## 16.4 措辞层的可达性说法有八种，而且其中一种不设位置上限

本节是**初稿被推翻得最彻底的一条**。初稿写的是"措辞层的可达性上限只有两种说法（privileged network position 与 physical proximity），没有 remote attacker"。容错重测后，仅在本篇两族里就数得出八种攻击者限定，且 `remote attacker` 有 8 段。第 13.4 节给出完整光谱，此处只保留规则：

| 可达性说法（Apple 原话片段） | 两族段数 | 绑定 CVE |
|---|---|---|
| a remote attacker（无位置限定） | 8 | 2026-65414 |
| in a privileged network position | 19（蓝牙族 12 + Wi-Fi 族 7） | 2026-43667、2026-20650、2024-23277、2026-28994 |
| in physical proximity | 22（Wi-Fi 19 + 蓝牙 3） | 2025-43374、2026-64726、2026-86953、2026-84547 |
| in wireless proximity | 8 | 2026-86957 |
| in radio range | 8 | 2026-84613 |
| in / within Bluetooth range | 2 | 2024-27867、2025-20701 |
| with a paired Bluetooth device | 6 | 2026-86919 |
| with physical access to a device / to an unlocked device | 3 | 2025-31216、2026-43674 |

规则：本篇讨论攻击前提时只能用表中某一句并注明是 Apple 原话，**且必须同时给出它绑定的 CVE**——因为八句话里没有任何一句是跨条目通用的。八句相加 77 段，占两族 150 个带 Impact 段落的一半以下；其余是 app 主语与文件/设备主语。另有两处口径要挡住外推：`in Bluetooth range` 在全语料还有 1 段落在本篇两族之外；`radio range` 全语料 9 段里 1 段在 `Baseband`（见 #105）。仍然没有的说法：零点击类措辞、`eavesdrop`、`man-in-the-middle`（后两者全语料 0）、在野声明（`actively exploited` 全语料 18 段，但本篇两族 0）。写"远程""无需交互""在野"时必须声明这是本报告的用词或第三方来源，而 T4 本轮未回填，因此这些词在正文里实际不允许出现。

## 16.5 普查方法本身要交代，因为它会造错数

三处踩过的坑值得写进报告，因为它们都会系统性地制造**虚高或假阴性**：

1. **英文手册页的目录块会污染词频**。逐页取回的正文里，"目录/See also" 之前的部分是页内导航，包含整部手册的章节名。未剥时 "Continuity" 在取样 20 页里计得 23 次/10 页；剥掉后是 4 次/3 页。本篇英文侧数字全部来自剥目录后的正文。
2. **中文全量结论不能对译到英文侧**。例如「绑定 / bonding」在中文手册 20 页出现，英文侧取样里只出现在蓝牙那一页（见 #103 第七节）。中英两侧的措辞覆盖不对称，任何"术语 X 手册层不用"的断言必须在两侧分别测。
3. **顾问页的 HTML 类名会换代，而换代是静默的**（第 13.5 节）。严格属性匹配在标记带后缀的 10 页命中 0 块且不报错，本篇据此产出的两条"0 结论"全部作废。规则：**每个 0 都必须附带一次覆盖性证明**，最省事的是标记探针（已实测含带后缀类名的那一页 149034 有 209 段；解析器在该页命中 0 段即作废，且不得按页号大小选探针页）。这一条同样适用于本系列已发布的 #100–#103，它们的 0 结论未做该证明。

## 16.6 纯致谢段落是一等公民，而且量不小

第 13.7 节的字段清点给出精确数：本篇两族 230 段里，**150 段带 Impact、80 段是纯致谢**（蓝牙族 54、Wi-Fi 族 26）。也就是说近场段落名下的三分之一以上是"Apple 致谢某人而不给缺陷条目"。逐段落名：`Bluetooth` 28、`Wi-Fi` 21、`CoreBluetooth - LE` 7、`Core Bluetooth` 9、`AirDrop` 6、`Handoff` 4、`Wi-Fi Connectivity` 5，而 `afclip` 与 `Clipboard` 为 0。

第 14.1、15.1、15.5 节列出的实例包括 AirDrop 120893、Handoff 122371 等 4 页、Wi-Fi 21 页全清单（含因重测才可见的 149034、149036、149037、149038）、Wi-Fi Connectivity 旧 5 页。

规则：引用它们时只能写"Apple 在某页致谢某人且未给 CVE"。反过来，也不能因为没有 CVE 就认为该段落不存在——统计里它们占真实份额，第 13 章的段数基数已包含它们。**但要警惕它的否定形式**："某段落名全是纯致谢"这类结论对采集缺陷最敏感，第 15.5 节的 `Wi-Fi Connectivity` 就是刚被推翻的一例。

---

# 17 缺口登记：公开材料没有说明的十四件事

本章不是免责声明，而是本篇的结论之一。每一条都写明"缺什么、为什么必须有、本篇因此不能写什么"。**本轮重测后，原 G11 已闭合**（蓝牙族逐格分解见第 13.3 节），但同一轮重测暴露出一个新的、方法论层面的缺口，登记为 G14；因此本章仍是十四条，只是第十四条换了一个来源。

**G1 源码层整体不可用。** 本轮本地 XNU 镜像为空目录，未取得任何类名、符号或调用路径。因此本篇凡是本应由实现层回答的问题——`afclip` 进程归属、点对点 Wi-Fi 栈在内核里的边界、IDS 客户端与 APNs 客户端的关系、接力广告密钥的内存生命周期——全部降级为"本报告未核对源码"。**影响**：第 15.3 节不能断言 afclip 与 AirDrop 的实现关系；第 10.2 节"是否经过 Apple 基础设施"只能按手册页措辞回答。

**G2 点对点 Wi-Fi 的技术参数未公开。** 手册层三次提到这条链路（4.1、5.1、9.4）都不给频段、信道选择、协商过程、是否使用 5 GHz，也不点名 AWDL（第 11.3 节沉默一）。**影响**：不能评估该链路的可观测性与干扰面。

**G3 自动解锁的"安全估算距离"方法未说明。** 第 9.4 节执行-2 只有 "securely approximate the distance"。距离门限（2–3 米、约 1 米）是策略数值，不是测距精度。**影响**：不能讨论测距本身能否被欺骗；同时不得引用第三方 UWB 测距结论并标为 Apple 口径。

**G4 接力广告密钥的生命周期未说明。** 第 2.1 节给出"每设备一个 256 位对称 AES 密钥，存于钥匙串，用于 AES-256-GCM 广告并带重放保护"，但没说轮换周期、是否随 iCloud 钥匙串备份、被窃取后的影响半径。**影响**：第 6.4 节归纳"伪造停铃广播需先持有该密钥"之后无法继续评估——手册层没给这把密钥的可撤销路径。

**G5 两类哈希/标识符的算法参数未公开。** AirDrop 短身份哈希的盐、迭代、域（第 5.2 节）；DSID 的轮换周期数值（第 7.4 节）。手册层只写 "rotated periodically"。**影响**：不能讨论通讯录哈希的离线匹配可行性；任何此类第三方结论都必须标 T4，而本轮 T4 未回填。

**G6 APNs 备用承载的触发情形未说明。** 原句是 "In some situations, these messages are sent using APNs instead of BLE"，"some situations" 没有列举。**影响**：第 10.3 节判断三只能说"存在服务器中继路径"，不能说何时发生，也不能说服务端可见哪些元数据。

**G7 措辞层在 AirDrop/Handoff 这两个段落名下是身份协商零条目。** 第 14.2 节：AirDrop/Handoff 全部 26 段没有一条涉及配对、身份验证、窃听或中继。这条是本轮重测中**唯一一条新旧分母完全相同**的负结果——标记带后缀的 10 页里没有一条 AirDrop 或 Handoff 条目，容错切块给这 26 段没有增加任何内容。**影响**：不能写"AirDrop/接力的身份识别被绕过过"；也不能反过来说它安全——只能说 Apple 在这两个名下披露的三条都在文件与元数据处理侧（第 14.3 节）。
本条在重测后必须加一个**范围限定**：把口径从 26 段放宽到两族 230 段，"身份协商侧零条目"就**不再成立**。230 段里与身份协商直接相关的有三段：`Core Bluetooth` 名下 CVE-2024-44124 的 Impact 原句是 `A malicious Bluetooth input device may bypass pairing`（全族唯一命中 `pairing` 的那一段）；`Bluetooth` 名下页 120907 的 CVE-2024-27867 写 `an attacker in Bluetooth range might be able to spoof the intended source device and gain access to your headphones`，修复句式是 `An authentication issue was addressed with improved state management.`；`Wi-Fi` 名下页 149034 的 CVE-2026-43674 同用 "authentication issue" 句式。全族 `authentication` 命中 2 段（蓝牙族 1 + Wi-Fi 族 1）、`trust` / `certificate` / `passkey` 在 230 段内均为 0。这几个 0 的口径要写清：`authentication` 在**全语料有 142 段 / 82 页**，`certificate` 全语料 11 段 / 8 页，`trust` 全语料 9 段 / 9 页，只有 `passkey` 全语料也是 0——所以前三个词是"本两族回避"，不是"Apple 措辞层不用"（与第 13.2 节 `actively exploited` 同一类判读）。因此本篇的正确写法是：**连续互通产品名（AirDrop/Handoff）在措辞层确实回避身份协商，但"近场身份协商从未被披露缺陷"这句话是错的**——那三段属于别的组件名，第 14.2 节末段的"不跨名填补"约定同时禁止本报告把它们挪来填空，也禁止本报告用它们去支撑"零条目"的扩大版说法。

**G8 英文手册侧只有 20 页取样，中文侧 179 页全量。** 其余 159 页的英文措辞未核。**影响**：全篇"英文侧 0 次"仅指取样；所有"从不使用某词"的全量断言只在中文侧成立（第 11.1 节）。

**G9 `afclip` 与产品通道的对应关系 Apple 未说明。** 见第 15.3 节。**影响**：该段落的 7 条条目只能按段落名引用。

**G10 Family Sharing 与 IDS 的公钥同步细节未公开。** 第 7.4 节只说公钥在建立家庭共享时经 IDS 同步。**影响**：无法讨论成员被移出后旧公钥的撤销传播；第 7.5 节"四类凭据共享同一信任根"的判断因此缺少撤销维度。

**G11 修复句式（Description）侧未按段落名穷尽。** 本条在重测后**部分闭合**：蓝牙族与 Wi-Fi 族的词频逐格分解都已完成（第 13.3 节两张表，各给出 CVE × 产品线覆盖页数的完全分解），Impact 侧也已做到穷尽——第 13.7 节把 150 个带 Impact 段落归为 **30 种句子**（表按"段落名 × 句子"展开为 35 行）并逐名列表。**仍缺的是 Description 侧**：本篇统计的修复句式（`bounds checking` / `input validation` / `state management` 等）只到词根命中，没有做成"每种修复句式 × 每个段落名"的同规格矩阵，因此不能说"某段落名的修复方式历史上只有两类"这类封闭结论。**影响**：第 13.6 节第二条事实只写到"三种后果共用同一个修复描述"这一观察，不升级为句式覆盖度的整体判断。

**G14 本篇的段落计数依赖 HTML class 属性，而 Apple 会改这些属性。** 这是本轮重测新登记的缺口，成因见第 13.5 节：初稿用严格属性相等匹配页块标题，标记带后缀的 10 页（149034–149043）的 `class="gb-header alignment horizontal-align-left"` 一条也没被识别，静默丢 70 段，且解析器不报错。容错三条修法（标题前缀匹配、`<p>` class 前缀匹配、块尾三选一）目前只在**本篇两族与 Wi-Fi 族词频矩阵**上重跑过，全语料的 470 个段落名分布、其余 246 页的历史数字未逐一复测覆盖性。**影响**：本篇的覆盖性证明只到"探针页 149034 实测 209 段、10 页块数之和正好等于全语料漏采数 1,430"这一层（附表 D 第 7 项）；引用本报告任一"某名 N 段"时，应视为该轮解析器版本下的测量值，而不是 Apple 的固定属性。由此得出本篇的通用纪律，第 13.5 节已写入正文：**任何"全语料 0 次"或"某族 0 段"的结论，除检索词与分母外，必须额外回答"含带后缀类名的那批页面被解析器看见了吗"——而不能用"最新的那批页面"来指代它（第 13.5 节的 149226 反例）。**

**G12 第三方研究对应关系未回填。** 第 14、15 章致谢对象（Ron Masas、mzzzz__、Arash Ale Ebrahim/CISPA SCy-Phy、jioundai/360、Wang Yu/Cyberserval、Alex Radocea、Hossein Lotfi/Trend Micro ZDI、Cristian Dinca/icmd.tech、Mathis Mansière、Peter Malone、Thibaud Kehler、Preet Dsouza、Tim Michaud、Antonio Zekic 等）的公开报告未逐条核对。**影响**：致谢姓名可引（T2），研究结论不可引；正文里凡涉及"研究者是谁、他们说了什么"到 CVE 的映射，一律不写。

**G13 无 CVSS。** 顾问页一贯不给出严重性分值。本篇不出现任何分值；如需分值，来源是 NVD 且必须显式标为第三方。

---

# 18 运维与检测：公开材料能支撑的二十项

本章每一项都注明它来自哪一节。凡是手册层没给的机制（G2–G6），本章就不提供对应检查项——这一章的长度受第 17 章约束，这是设计而非遗漏。另受 G14 约束：本章引用的页号、段落计数都是本轮容错解析器版本下的测量值，复核时应重跑而不是照抄。

## 18.1 配置层（10 项）

1. **AirDrop 接收模式**默认"仅限联系人"，用户可切"所有人"或关闭；托管设备可由设备管理服务限制。检查项：当前接收模式、是否存在组织级限制策略。（依据 5.1、5.5）
2. **凭证发送不受该开关影响**：通过 AirDrop 发送已存网站凭证始终走"仅限联系人"，无论用户设置。检查项：不要试图用 AirDrop 模式推断凭证通道状态。（依据 7.2）
3. **接力关系是账户级的、逐设备生效**。检查项：同一 Apple 账户下的设备清单，而不是本机配对记录。（依据 2.1、2.5）
4. **通话中继的开关在 FaceTime 设置里的"iPhone 蜂窝网络通话"，按设备生效**；顶层通用设置里没有它。检查项：逐台设备核对 FaceTime 设置。（依据 6.1、6.5）
5. **通话中继前置条件是三合一**：同一 Wi-Fi + 同一 Apple 账户 + iCloud 与 FaceTime 均已登录。检查项：只满足前两项也可能不生效，审计时应三项都查。（依据 6.1）
6. **短信转发的开关在"信息"设置**；启用双重认证时信任圈内设备自动注册，否则需逐台输入 iPhone 生成的随机 6 位码。检查项：转发目标设备清单，以及该清单是否由"自动注册"而非人工确认产生。（依据 6.6）
7. **自动解锁关系需要逐条启用**，且两端须以双重认证登录同一 Apple 账户。检查项：每种"谁解锁谁"的组合单独核对，不能凭"同账户"推定。（依据 9.3）
8. **iPhone 镜像的认证策略在首次设置时选定**（"自动认证"或"每次均询问"），由 Mac 上的安全隔区强制执行。检查项：该策略值；另外记住这一路**不做安全测距**。（依据 9.8）
9. **Wi-Fi 口令共享可被设备管理服务限制**。检查项：托管配置里是否存在该限制。（依据 7.1）
10. **Apple TV 的填充请求由 UI 焦点触发，附近所有 iPad/iPhone 都会显示提示**。检查项：家庭/会议室里 Apple TV 与手机的空间共存关系；跨账户场景需 PIN，且 iPhone 必须解锁并靠近配对的 Siri Remote。（依据 7.3）

## 18.2 状态与失效信号（4 项）

11. **AirDrop 分享面板里的剪影图标**是手册层唯一一处把"未验证/未认证"可视化给用户的信号：未通过验证的设备以剪影 + 该设备在"设置 > 通用 > 关于本机 > 名称"里的自设名出现在"其他人员"分组。（依据 5.4）
12. **iPhone 镜像的身份固定是硬失效**：pinned 身份变更后通知不再转发到 Mac，而不是降级转发。因此"通知突然不转发"是一个可用的完整性信号。（依据 9.7）
13. **"锁定 iPhone"按钮的效果可核验**：轻点后 Watch/Vision Pro 经 BLE 发出锁定命令，iPhone 锁定并同时禁止面容 ID 与其它设备解锁，下次必须输入密码。运维上这是一条即时收回手段，值得写进入职/离职与设备丢失处置流程。（依据 9.5）
14. **远端解锁不产生认证断言**：Apple Watch / Apple Vision Pro 不能代替面容 ID 用于 Apple Pay 或 App 授权。检查项：不要在流程文档里把"设备已解锁"等同于"用户已强认证"。（依据 9.5）

## 18.3 补丁与版本核验（6 项）

15. **按页号与版本核对，不按"iOS"整体核对**。同一 CVE 常覆盖 5–8 条产品线（第 16.2 节），而某些条目**只**在遗留分支：CVE-2026-28819 在 iOS 18.7.9 与 macOS 三个老分支，未出现在 iOS 26.5（第 16.3 节）。检查项：以本机具体版本号 + 顾问页页号为准。
16. **遗留分支机型范围要单独确认**。127111 的 Available for 为 iPhone XS、XS Max、XR、iPad 第 7 代；这些设备不在 iOS 26 主线，其"已更新"结论只能来自 18.7.x 系列页面。（依据 16.3）
17. **文件侧条目要落到隔离标记核验**。AirDrop 披露的三条都在文件与元数据侧（第 14.3 节），其中 CVE-2024-54564 的后果是"收到的文件可能没有被施加隔离标记"。检查项：对经 AirDrop 接收的文件核验其隔离属性状态，而不是只看来源记录。（依据 14.1）
18. **标记带后缀的那 10 页（Entry added/updated October 19, 2026）要按八页分别核对**。这批页面依次是 149034（iOS/iPadOS 27）、149035（macOS Golden Gate 27）、149036（tvOS 27）、149037（watchOS 27）、149038（visionOS 27）、149041（iOS/iPadOS 26.7）、149042（macOS Tahoe 26.7）、149043（macOS Sequoia 15.8）；同批的 149039（Safari 27）与 149040（Xcode 27）**不含任何本篇两族的段落名**。检查项：只看本机 OS 对应那一页有没有该条，不要由"CVE 已公开"推定所有产品线页面都列了它。（依据 13.6、16.3）
19. **段落名在某页缺席 ≠ 该产品线不受影响**。在本批八页里，`Core Bluetooth` 这个段名在 149036（tvOS 27）整页不存在，`Wi-Fi Connectivity` 与 `CoreBluetooth - LE` 两个名在三条遗留分支页（149041–149043）上都不存在，`Wi-Fi Connectivity` 也不在 149035（macOS 27）。检查项：报告中要写成"Apple 未在该页列出该段落名"，不得写成"该产品线无此组件"或"该产品线不受该条影响"。（依据 16.3）
20. **授权与提权类条目要落到配置核验，而不是只看版本**。本批 `An authorization issue was addressed with improved state management.` 一句覆盖三种后果（app 越权访问蓝牙 CVE-2026-84560、读取蓝牙设备信息 CVE-2026-86891、访问敏感用户数据 CVE-2026-84636），`authorization issue` 两族合计 13 段；另有 CVE-2026-84631 一条 root privileges，修复句式是 additional entitlement checks；CVE-2026-43674 的前置条件是"物理接触 + 已解锁 + without authentication"，后果是查看 Wi-Fi 口令。检查项：① app 蓝牙用途声明与 entitlements 范围；② 自动锁定超时与"锁定时需要密码"设置；③ 不要把"版本已更新"当成授权面已收敛——同一修复句式覆盖三种不同后果，Apple 未区分其授权模型。（依据 13.6 第二、三条事实）

## 18.4 本章不能提供什么

不提供：射频侧观测方法、广告字节解析、身份哈希比对工具、测距欺骗检测、配对状态机审计。原因是第 17 章的 G2–G6——公开材料没有给出可据以设计检查项的参数；以及本篇口径：不含针对他人设备的操作步骤。

---

# 附表 A 术语口径（39 行）

| 术语 | 本篇用法 | 来源层 | 不得混用 |
|---|---|---|---|
| 连续互通 / Continuity | 能力集合，非协议 | T1 概览页（1.1） | 不当作某个具体服务 |
| 接力 / Handoff | 活动接续机制 | T1（2.1） | 不与 `Handoff` 段落名互推 |
| 带外配对 | 配对经 APNs 建立，不经空口协商 | T1（2.1） | 不含"离线配对"含义 |
| 广告字节 / advertisement | BLE 广播内容，承载当前活动或网页域名 | T1（2.1、3.2） | 不指 iBeacon 类定位帧 |
| 重放保护 | 手册层原语 "with replay protection measures" | T1（2.1） | 未说明实现 |
| 域名控制 | App 须证明对所接力网站域名的控制 | T1（3.1） | 机制本体 = 共享网页凭据 |
| fallback URL | 接收端无同名 App 时的回退地址 | T1（3.3） | 手册层明写不要求域名控制 |
| 大载荷接力 | BLE 传初始载荷后转点对点 Wi-Fi + TLS | T1（4.1） | 不写 AWDL |
| 通用剪贴板 | 复用接力的剪贴板跨设备传输 | T1（2.5、4.2） | 默认共享，App 可退出 |
| 点对点 Wi-Fi | "Apple 创建/开创的点对点无线局域网技术" | T1（4.1、5.1） | AWDL 为第三方口径 |
| 短身份哈希 | 由账户邮箱与手机号生成，BLE 广播 | T1（5.2） | 算法未公开（G5） |
| 长身份哈希 | 建连后由发送方提交 | T1（5.3） | 两级顺序不可颠倒 |
| iCloud 身份证书 | TLS 连接中交换，与通讯录比对 | T1（5.3） | 不是蓝牙配对密钥 |
| 2048 位 RSA 身份 | 登录 iCloud 时存于设备 | T1（5.2） | 不写成 escrow 公钥 |
| 剪影图标 | 未验证设备的唯一可视化标识 | T1（5.4） | 不代表"已拦截" |
| 仅限联系人 | AirDrop 默认档；凭证发送强制此档 | T1（5.1、7.2） | 两者方向不同 |
| 授予方 / 请求方 | Wi-Fi 口令共享的两端 | T1（7.1） | 手册层原话 |
| Password AutoFill Keychain | 凭证接收后的存放位置 | T1（7.2） | 中文"自动填充密码钥匙串" |
| BLE 链接加密 | Apple TV 填充路径的承载加密 | T1（7.3） | 与广告加密不同层 |
| DSID | Destination Signaling Identifier，账户级、定期轮换 | T1（7.4） | 周期数值未公开（G5） |
| IDS | Apple 身份服务；本篇只引两处：热点公钥同步、Apple Pay 接力承载 | T1（7.4、8.1） | 两处不互推 |
| ECDH(Curve25519)+Ed25519 | 家庭共享热点连接信息的密钥协商 | T1（7.4） | 手册层唯一一处 |
| 端对端加密（通知） | 通话中继通知"与 iMessage 相同" | T1（6.2） | 不推断元数据可见性 |
| 停铃广播 | 短暂 BLE 广播，加密法同接力广告 | T1（6.4） | 复用原语的证据 |
| 信任圈 | 短信转发自动注册的范围 | T1（6.6） | 手册层未定义边界 |
| 授权设备 | Apple Pay 接力中执行授权的一端 | T1（8.1） | 与"发起端"不同页不同词 |
| Device Account Number | 卡号密文替代项，存于 SEP，不共享 | T1（8.2） | 与"卡片唯一标识符"不同 |
| STS 隧道 | 相互认证站对站，两端 SEP 协商 | T1（9.2） | 唯一例外是无 SEP 的 Mac |
| 长期密钥 / 临时会话密钥 | 启用时交换 LTS；每请求协商临时密钥 | T1（9.2） | 手册层原语 |
| PDK | 密码派生密钥，被解锁密钥封装 | T1（9.4） | 解锁传递能力非密码 |
| 32 字节解锁密钥 | 目标随机生成，每次解锁后更换 | T1（9.4） | — |
| 托管记录 / keybag | Watch 场景的长期托管记录存 Watch keybag，密钥存 iPhone 钥匙串 | T1（9.6） | 与 9.4 的"用完即弃"相反 |
| pinned 身份 | iPhone 镜像身份固定，变更即停转发 | T1（9.7） | 手册层唯一硬失效机制 |
| 安全测距 | "Secure ranging"，iPhone 镜像明确不需要 | T1（9.8） | 方法未公开（G3） |
| rapportd | 仅作顾问页**段落名**照抄，本轮 3 段全为纯致谢 | T2（15.6） | 不得写成"接力的实现组件"；Apple 未说明对应通道（同 G9 的 afclip 情形） |
| Identity Services | 出现两种身份：T1 的 Apple Identity Service（IDS，7.4、8.1）与 T2 的段落名 `Identity Services`（1 段纯致谢） | T1 + T2 | 跨层同名不是同一组件的证据；本篇不互推 |
| CoreDrag / Clipboard | 两个顾问页段落名；`Clipboard` 名下是符号链接校验条目，`CoreDrag` 名下是 CVE-2026-43683 | T2（15.4、15.6） | 都不等于手册层的"通用剪贴板"（2.5、4.2） |
| improved state management | 授权类修复句式，两族 `authorization issue` 13 段 / `state management` 24 段 | T2（13.6、13.7） | 与 `improved access control`（`DeviceCheck` 名下）是两种措辞，不合并 |
| Entry added / Entry updated | 顾问页**条目备注**字段，逐页可不同 | T2（13.6 事实四） | 与"页面发布日期"分属两个字段；不得当作该 CVE 的统一日期 |

（表内"隔离标记"未列，因手册层无此词；它只在措辞层 CVE-2024-54564 出现。）

---

# 附表 B 顾问页条目索引（近场与本地解析族）

| CVE | 段落名 | 首次页面 → 产品版本 | 可达性主语 | 后果（关键词） | 备注日期 |
|---|---|---|---|---|---|
| CVE-2024-54564 | AirDrop | 120909 → iOS/iPadOS 17.6 | A file received from AirDrop | 隔离标记未施加 | Entry added March 20, 2025 |
| CVE-2025-24097 | AirDrop | 122371 → iOS/iPadOS 18.4 | An app | 读取任意文件元数据 | — |
| CVE-2026-43667 | AirDrop | 127110 → iOS/iPadOS 26.5；148287 → 18.7.10 | attacker in privileged network position | 拒绝服务（reachable assertion） | Entry added August 25, 2026 |
| CVE-2025-30463 | Handoff | 122371 → iOS/iPadOS 18.4 | An app | 访问敏感用户数据（data container） | — |
| （无 CVE） | AirDrop | 120893 → iOS/iPadOS 17.4 | — | 纯致谢 Cristian Dinca | Entry added March 7, 2024 |
| （无 CVE） | Handoff | 122371 / 122373 / 122376 / 122377 | — | 纯致谢 Kirin、FlowerCode | — |
| CVE-2024-44227 | Wi-Fi | 121238 → macOS Sequoia 15；121250 → iOS 18 | An app | 系统终止或内核内存损坏 | Entry added March 3, 2025 |
| CVE-2024-54546 | Wi-Fi | 121238 | An app | 同上（与 44227 同段共呈） | 同上 |
| CVE-2024-40770 | Wi-Fi | 121238 | A non-privileged user | 修改受限网络设置 | — |
| CVE-2024-23237 | Wi-Fi | 121238 | An app | 拒绝服务 | — |
| CVE-2024-44134 | Wi-Fi | 121238 | An app | 读取敏感位置信息（脱敏） | — |
| CVE-2024-40856 | Wi-Fi | 121238 / 121248 / 121250 | An attacker | 强制设备断开安全网络 | Entry updated March 3, 2025；修复点名 Beacon Protection |
| CVE-2025-43374 | Wi-Fi | 122069 / 122070 / 122404 / 122405 / 122716 / 122720 / 122721 / 122722 | **attacker in physical proximity** | **内核内存越界读取** | Entry added November 21, 2025 |
| CVE-2025-31216 | Wi-Fi | 122404 / 122405 | attacker with physical access | 覆盖托管 Wi-Fi 配置 | Entry added November 21, 2025 |
| CVE-2025-43186 | **afclip** | 124147 / 124149–124151 / 124153–124155 | Parsing a file | 某个 app 意外终止 | — |
| CVE-2025-43373 | Wi-Fi | 125634 / 125635 / 125636 | An app | 内核内存损坏 | — |
| CVE-2026-20621 | Wi-Fi | 126346–126350 / 126353 | An app | 内核内存损坏 | — |
| CVE-2026-28866 | **Clipboard** | 126792–126796 | An app | 访问敏感用户数据；修复写符号链接校验 | — |
| CVE-2026-28819 | Wi-Fi | **127111 → iOS 18.7.9（仅遗留分支）**；127115 / 127116 / 127117 | An app | **以内核权限执行任意代码**（越界写） | — |
| CVE-2026-28994 | Wi-Fi | 127110 → iOS 26.5；至 127119 | attacker in privileged network position | 伪造 Wi-Fi 帧致拒绝服务（use after free） | — |
| CVE-2026-64726 | Wi-Fi | 128066–128070 → 各线 26.6；148287 → 18.7.10 | **attacker in physical proximity** | **进程内存损坏**（非内核） | — |
| CVE-2026-65414 | Bluetooth | 149034–149038、149041–149043（8 页） | **a remote attacker** | app 意外终止或任意代码执行（越界写） | 条目内无 Entry 备注 |
| CVE-2026-84613 | Bluetooth | 同上 8 页 | **an attacker in radio range** | 系统意外终止（越界写） | Entry added October 19, 2026（8 页一致） |
| CVE-2026-84547 | Bluetooth | 149035、149042、149043 | an attacker in physical proximity | 进程内存损坏 | Entry added October 19, 2026 |
| CVE-2026-84560 | Bluetooth | 149034–149038 | An app | 越权访问蓝牙（授权问题） | 无 Entry 备注；Impact 省略 be able to |
| CVE-2026-84631 | Bluetooth | 149035（仅 1 页） | An app | **获得 root 权限**（additional entitlement checks） | 无 Entry 备注 |
| CVE-2026-86919 | Core Bluetooth | 149034、149035、149038、149041–149043 | **an attacker with a paired Bluetooth device** | 任意代码执行（stack overflow） | Entry added October 19, 2026 |
| CVE-2026-86891 | Core Bluetooth | 149035、149037、149042、149043 | An app | 访问蓝牙设备信息（授权问题） | 无 Entry 备注 |
| CVE-2026-86957 | Wi-Fi | 149034–149038、149041–149043（8 页） | **an attacker in wireless proximity** | 系统意外终止（越界读） | Entry added（7 页）/ **Entry updated（仅 149037 watchOS 27）** |
| CVE-2026-86953 | Wi-Fi | 149034–149038（不进遗留分支） | an attacker in physical proximity | 拒绝服务（越界写） | Entry added October 19, 2026 |
| CVE-2026-43674 | Wi-Fi | 149034（仅 iOS/iPadOS 27 一页） | **an attacker with physical access to an unlocked device** | 免认证查看 Wi-Fi 口令 | 无 Entry 备注 |
| CVE-2026-84636 | Wi-Fi Connectivity | 149034、149036、149037、149038 | An app | 访问敏感用户数据（授权问题） | **四页全部 Entry updated** October 19, 2026 |
| CVE-2024-44124 | Core Bluetooth | 121250 → iOS/iPadOS 18 | **a malicious Bluetooth input device** | **may bypass pairing** | 两族内唯一 `pairing` 命中（见 #103） |
| CVE-2024-27867 | Bluetooth | 120907 → AirPods 固件 6A326 / 6F8、Beats 固件 6F8 | an attacker in Bluetooth range | 冒充目标设备、接入耳机 | 两族内 `authentication issue` 句式两段之一；该页不是手机/电脑 OS 页 |
| （无 CVE） | Wi-Fi | 120903 | — | 纯致谢 Adam M. | Entry added July 29, 2024 |
| （无 CVE） | Wi-Fi | 121248 / 125108–125116 / 126792–126799 / 127110 / 127115 | — | 纯致谢（多组研究者） | — |
| （无 CVE） | Wi-Fi Connectivity | 126792 / 126794 / 126797–126799 | — | 纯致谢 Alex Radocea | — |

（蓝牙与 Core Bluetooth 段落在 2026 年之前的条目索引在报告 #103 附表 B，本篇只补入重测后可见的新条目与身份协商相关的两条，不重复旧条目。表中"—"表示该页条目备注未给出对应字段，不代表日期不存在。149034–149043 这批页与产品版本的对应见第 16.3 节。）

---

# 附表 C 本篇引用的手册页清单

| 手册页（sec hash） | 页面名 | 中文页发布日期 | 本篇引用章节 |
|---|---|---|---|
| secce267dc10 | 连续互通概览 | 2021-02-18 | 1.1、1.2 |
| sec025128f1b | 服务安全性概览 | 取样页 | 1.2 |
| secf78dbe639 | 接力安全性 | 2021-02-18 | 2、3、4 |
| sec2261183f4 | 隔空投送安全性 | 2021-02-18 | 5 |
| secd2015ef35 | 无线局域网密码共享安全性 | 2024-12-19 | 7.1、7.5 |
| sec8839a8de2 | 将密码发送给其他用户或 Apple 设备 | 取样页 | 7.2、7.3、7.5 |
| seca4b33e8c9 | 智能互助（个人热点）安全性 | 2021-02-18 | 7.4、7.5 |
| sec28a79bf17 | iPhone 蜂窝网络通话中继安全性 | 2021-02-18 | 6.1–6.5 |
| sec16bb20def | iPhone 短信转发安全性 | 2021-02-18 | 6.6 |
| secfbd5c0e54 | Apple Pay 安全性概览 | 2024-12-19 | 8 |
| sec6ab47ebfc | 自动解锁 Apple 设备 | 2026-01-28 | 9 |
| sec82597d97e | 蓝牙安全性 | 2021-02-18 | 10.4 分界引用（主体在 #103） |
| secadb5b6434 | 物理配对 | 2024-12-19 | 10.4 分界引用（主体在 #103） |
| sec1e6108efd | 超宽带 | 2021-02-18 | 11.3 沉默二 |
| sec31e483abf | Wi-Fi 隐私 | 取样页 | 11.2 随机化对照 |
| secfd166f620 | Wi-Fi | 取样页 | 11.2 |
| sec15bfe098e / sec35dd877d0 / sec5599b66df / sec59b0b31ff / secabd3504cd / secd3c881cee | "隔离"六页（运行时进程、App 概览、macOS 门禁、安全隔区、扩展项、BlastDoor） | — | 11.2（证明该词与近场无关） |

中文侧 179 页全量转储；英文侧取样 20 页。日期字段一律用"中文页发布日期"，与顾问页的 Entry added/updated 分属两套语义（第 16.5 节）。

---

# 附表 D 普查方法与可复算说明

1. **手册层取数**：中文全量以逐页转储文本为语料（179 页）。英文逐页取回后需剥掉页内目录块——正文起点判定为 "Copyright and trademarks" 之后；未剥时目录会把全书章节名计入，导致词频虚高（第 16.5 节实例：Continuity 23/10 → 4/3）。
2. **顾问层切块（容错版，本篇最终采用）**：块首 `<h3 class="gb-header[^"]*"[^>]*>` 之后到 `</h3>` 的文本即段落名；块尾为下一个同级 `<h3 class="gb-header`、`<div id="disclaimer"` 或 `<footer>`。块内取值用 `<p class="(?:gb-paragraph|paragraph)[^"]*"[^>]*>`，HTML 实体解码后再匹配，排除 `Available for` 开头那段。**初稿用的是属性完全相等（`class="gb-header"` 与 `class="gb-paragraph"`），这一写法在标记带后缀的 10 页上命中 0 块且不报错**，改因与影响见第 13.5 节、G14。
3. **CVE 归属判定**：致谢行形如 `CVE-xxxx-nnnnn: 姓名(机构)`。当 Impact 或 Description 先于致谢行出现时，以紧邻其上的 Impact/Description 对该 CVE 归属；无 Impact 的段落判为"纯致谢"。共现多 CVE 于一段时按页面原始顺序保留共现，不拆分。
4. **计数口径**：所有"次数"为正则不分大小写匹配次数；"段数"为命中该词的段落个数。两者差值主要来自 Impact 与 Description 各写一遍（第 13.3 节的 out-of-bounds 即为此例）。检索词一律先 `lc` 再 `\Q…\E`，否则 `Beacon protection` 这类大小写混排词会假报 0。
5. **分族**：本报告构造，非 Apple 分类（第 13.1 节）。分族表可复算：Bluetooth 68 + Core Bluetooth 25 + CoreBluetooth - LE 11 + AirDrop 20 + Handoff 6 = **130**；Wi-Fi 79 + Wi-Fi Connectivity 9 + afclip 7 + Clipboard 5 = **100**；合计 **230**。初稿的 82 + 78 = 160 已由第 13.5 节作废。
6. **页号 → 产品版本映射**：由 257 份页面的 `Available for` 字段与页标题实测取得；本篇引用的页号—版本对照已在附表 B 与各章内给出，未映射页不引用。149034–149043 这批十页中有两页（149039 Safari 27、149040 Xcode 27）不含本篇任何段落名，计数时必须先剔除。
7. **覆盖性探针（本轮新增，也是本篇对解析器的唯一实证）**：对页 149034 逐页统计两种标记——严格 `class="gb-header"` 命中 **0** 块、容错 `class="gb-header[^"]*"` 命中 **209** 段；再把 149034–149043 十页的容错块数相加（209+314+118+128+165+13+9+103+181+190 = **1,430**），与"全语料容错 9,045 段 − 严格 7,615 段 = 1,430"逐页对齐，证明**全部漏采集中在这十页**，语料其余 246 页在两种解析下段数一致。任何后续复算都应先跑这个探针再跑词频。
8. **未做的事**：未取源码层（G1）；未回填第三方研究（G12）；英文手册仅取样 20 页（G8）；Description 修复句式未按段落名穷尽（G11）；容错解析只在本篇两族与 Wi-Fi 族词频矩阵上重跑过，470 个段落名的全分布未逐一复测（G14）；未记录任何 CVE 分值（G13）。

---

# 附表 E 允许写 / 不许写对照

| 想写的话 | 判定 | 依据与正确写法 |
|---|---|---|
| "接力配对是通过 APNs 带外建立的" | ✅ 可写 | T1 原句（2.1） |
| "接力广告用 AES-256-GCM 并带重放保护" | ✅ 可写 | T1 原句（2.1）；但不得补"每次会话换密钥"——手册层没说（G4） |
| "接力广告密钥存在安全隔区" | ❌ 不可写 | 手册层只说钥匙串（2.1）；SEP 只出现在自动解锁页（9.2） |
| "网页域名在广告字节里，只有本人其它设备能解" | ✅ 可写 | T1 原句（3.2） |
| "fallback URL 可被用来把用户接力到任意网站，构成钓鱼" | ❌ 不可写 | 手册层只写"不要求域名控制"（3.3）；后半句是推断，且 T2 无对应条目（G7） |
| "通用剪贴板默认共享，App 无需用户粘贴即可读取" | ✅ 可写 | T1 原句（2.5）；须同句写出"开发者可选择退出" |
| "AirDrop 用 AWDL 传输" | ❌ 不可写 | 手册层两侧均无此词（11.2）；写"Apple 创建的点对点无线局域网技术，该链路的开发者文档名称为 AWDL（第三方口径）" |
| "AirDrop 靠 UWB 精确测距发现附近设备" | ❌ 不可写 | 手册层写的是 BLE 发信号 + 点对点 Wi-Fi（5.1、5.3），UWB 页与 AirDrop 无关（11.3） |
| "AirDrop 的短身份哈希可被离线穷举" | ❌ 不可写 | 算法参数未公开（G5），任何此类结论须标第三方且本轮未回填（G12） |
| "通话中继的通知经过 Apple 服务器，所以 Apple 能听到内容" | ❌ 不可写 | 手册层写 APNs 承载 + 与 iMessage 相同的端对端加密（6.2）；能说的是承载路径，不能推断内容可见性 |
| "短信转发让短信获得端对端加密" | ❌ 不可写 | 被加密的是转发链路；链条两端是运营商 SMS（6.6） |
| "远端解锁可以代替密码授权支付" | ❌ 不可写 | 手册层明写 Watch / Vision Pro 不能代替面容 ID 用于 Apple Pay 与 App 授权（9.5） |
| "自动解锁的所有密钥都在安全隔区" | ❌ 不可写 | Watch 场景的托管记录密钥在 iPhone 钥匙串（9.6）；无 SEP 的 Mac 在内核终止隧道（9.2） |
| "iPhone 镜像靠测距防止远端解锁被滥用" | ❌ 不可写 | 手册层明写 "Secure ranging isn't required for iPhone Mirroring"（9.8） |
| "Apple 披露了 AirDrop 的身份验证绕过" | ❌ 不可写 | 这 26 段内 0 条涉及配对/身份验证（14.2、G7）；但**不得外推到整族**——见下面第 3 行 |
| "Apple 修复了 AirDrop 文件隔离标记未施加的问题" | ✅ 可写（带三件套） | CVE-2024-54564，页 120909，iOS/iPadOS 17.6，Entry added March 20, 2025（14.1） |
| "Apple 修复了 AirDrop 的零点击漏洞" | ❌ 不可写 | zero-click 全族 0 次，且**全语料 9,045 段内也是 0**（13.2、16.4） |
| "某蓝牙/Wi-Fi 近场漏洞已被在野利用" | ❌ 不可写 | actively exploited 在两族 230 段内为 0；但全语料有 18 段 / 13 页，所以只能写"本族无在野声明"，不能写"Apple 从不写在野"（13.2、16.4） |
| "近场族里没有远端攻击者可达的条目" | ❌ **初稿论点，本轮已撤销** | `remote attacker` 在 `Bluetooth` 名下 8 段（CVE-2026-65414，149034–149038、149041–149043）；成因与登记见 13.5 |
| "physical proximity 这种说法只出现在 Wi-Fi 族" | ❌ **初稿论点，本轮已撤销** | 蓝牙族 `Bluetooth` 名下 3 段（CVE-2026-84547）；Wi-Fi 族 19 段（13.4、16.4） |
| "近场身份协商从未被 Apple 披露缺陷" | ❌ 不可写 | `Core Bluetooth` 名下 CVE-2024-44124 的 Impact 原句是 `A malicious Bluetooth input device may bypass pairing`；`Bluetooth` 名下 CVE-2024-27867 写 spoof 冒充设备（G7 范围限定段） |
| "蓝牙名下的条目修的是内核代码执行" | ❌ 不可写 | `kernel` 在两族里 23 段**全在 `Wi-Fi` 名下的旧页**，蓝牙族 130 段内为 0（13.2 结论一）；蓝牙名下的提权条目是 CVE-2026-84631 的 root privileges 1 段，句式不写 kernel |
| "Apple 说某个近场缺陷可造成内核内存越界读取且需要靠近" | ✅ 可写（唯一一条） | CVE-2025-43374，`Wi-Fi` 段落，8 页，Entry added November 21, 2025（15.2） |
| "Apple 说 afclip 是 AirDrop 的实现组件" | ❌ 不可写 | Apple 未说明对应通道（G9）；Impact 主语是 app 终止（15.3） |
| "`rapportd` 是接力/AirDrop 的实现组件" | ❌ 不可写 | 该段落名本轮才可见，3 段全是纯致谢、无 CVE，且 Apple 未说明对应通道（15.6）；与 afclip 同规 |
| "顾问页的 `Identity Services` 段落就是手册层的 IDS" | ❌ 不可写 | 跨层同名不是证据（T1 与 T2 分别来源，G1 源码层不可用）；该段只有致谢、无 CVE（15.6） |
| "剪贴板条目修的是符号链接，所以是文件系统的漏洞" | ❌ 不可写 | Impact 与 Description 语义不连贯（15.4），两句必须同引且不推断 |
| "iOS 26.5 修了 Wi-Fi 内核代码执行漏洞" | ❌ 不可写 | CVE-2026-28819 只在 iOS 18.7.9 与 macOS 老分支，未出现在 iOS 26.5（16.3） |
| "CVE-2026-86957 的统一日期是 Entry added October 19, 2026" | ❌ 不可写 | 149037（watchOS 27）那页写的是 **Entry updated**；日期必须逐页给（13.6 事实四） |
| "这条 CVE 有 CVSS 7.5" | ❌ 不可写 | 顾问页无分值（G13）；任何分值须标 NVD |

---

**边界声明：** 本报告全部技术结论来自两层可核对的公开文本：《Apple 平台安全保护》官方指南（中文 179 页全量转储、英文 20 页取样正文）与 Apple 安全响应顾问页（257 份英文页面镜像，容错解析后全语料 9,045 段 / 470 个段落名，其中本篇两族 230 段），逐条标注页号、章节号与日期字段；手册层未说明的部分在第 17 章登记为缺口，不以推断填充。源码层本轮不可用，凡涉及内核实现的表述均已降级为"未核对"。第 13、16 章的统计为本报告对公开文本的自建语料普查，属本报告的测量结果，不是 Apple 的表述；这些数字依赖本报告解析器对顾问页 HTML 标记的识别方式，本轮已因标记换代把初稿分母从 160 段更正为 230 段、并撤销初稿两条"0 结论"（第 13.5 节、G14），后续复算须先跑附表 D 第 7 项的 149034 探针。所有第三方研究者姓名仅作措辞层致谢引用，其研究结论本轮未核对，不构成本报告依据。本报告为防御性技术分析，不含利用代码、概念验证、绕过方法、广告字节解密或身份哈希碰撞的实施细节，也不含针对他人设备的任何操作步骤；所引漏洞信息仅用于说明公开修复事实与检测配置。如需就本报告任一结论索取取数脚本或段落级原始文本，或指出任何事实与措辞问题，请联系。

Telegram: https://t.me/one00190
