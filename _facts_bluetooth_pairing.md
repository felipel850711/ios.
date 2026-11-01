# 报告 #103《iOS 蓝牙与近场配对通道的安全机制分析》已核实口径

取数日期：2026-10-26。一手源 = 《Apple 平台安全保护》手册（zh 全量 179 页本地转储 `_apsgh/txt/`，EN 取样 18 页 `_gpen/`）+ Apple 安全响应顾问页转储 257 份 `_tmp/adv2/`（脚本 `_nf_table.pl` / `_nf_words.pl`）。
层级标注：**T1** 手册层 / **T2** 措辞层 / **T3** 源码层 / **T4** 第三方层。**跨层引用必须显式标层，这是本篇唯一的错误防线。**

---

## 一、T1 手册层：蓝牙（`sec82597d97e`，发布 2021-02-18）

**可直引原文（EN）**：
> "There are two types of Bluetooth in Apple devices, Bluetooth Classic and Bluetooth Low Energy (BLE). The Bluetooth security model for both versions includes the following distinct security features:"

六项定义逐条（EN → zh，同页）：
- Pairing: The process for creating one or more shared secret keys → 配对：创建一个或多个共享密钥的过程
- Bonding: The act of storing the keys created during pairing for use in subsequent connections to form a trusted device pair → 绑定：储存在配对期间创建的密钥以在后续连接中用于形成可信设备对的行为
- Authentication: Verifying that the two devices have the same keys → 认证：验证两个设备具有相同的密钥
- Encryption: Message confidentiality → 加密：消息机密性
- Message integrity: Protection against message forgeries → 消息完整性：防止消息伪造的保护措施
- Secure Simple Pairing: Protection against passive eavesdropping and protection against man-in-the-middle attacks → 安全简易配对：防止被动窃听和中间人攻击的保护措施

**同页可直引**：
> "Bluetooth version 4.1 added the Secure Connections feature to Bluetooth Classic (BR/EDR) physical transport."

**支持矩阵（该页表格，逐格可引）**：配对 = 经典蓝牙 P-256 椭圆曲线 / BLE = FIPS 批准的算法（AES-CMAC 和 P-256）；认证 = HMAC-SHA-256 和 AES-CTR；加密与消息完整性 = **在"控制器"（Controller）中执行 AES-CCM**；防被动窃听 = ECDHE（经典列为 Ephemeral ECDHE）；防 MITM = **两种用户辅助型数字方法：数字比较（Numerical Comparison）或密钥接入（Passkey Entry）**，并附一行「Pairings require a user response, including all non-MITM pairing modes / 配对需要用户的响应，包括所有非 MITM 配对模式」。
机型门槛（同表）：蓝牙 4.1 或更高 = iPhone 6 未列、列 iMac Late 2015 / MacBook Pro Early 2015；iOS 9 或更高、iPadOS 13.1 或更高、macOS 10.12 或更高、tvOS 9、watchOS 2。蓝牙 4.2 或更高 = iPhone 6 或后续机型。
⚠️ 该矩阵在 EN 与 zh 页面都存在**列错位**（"iPhone 6 or later" 落在 4.2 行的经典蓝牙列）。引用时要写"该页表格的对应单元格"，不要改写为因果句。

**BLE 隐私小节（EN 原文，逐句可引）**：
> "To help secure user privacy, BLE includes the following two features: address randomization and cross-transport key derivation."
> "The private address is generated using the device's identity resolving key exchanged during the pairing procedure."
> "On devices with iOS 13 and iPadOS 13.1 or later have the ability to derive link keys across transports, a feature known as cross-transport key derivation. For example, a link key generated with BLE can be used to derive a Bluetooth Classic link key."
zh 对照：「为帮助保护用户隐私，BLE 包括了两种功能：地址随机化和交叉传输密钥派生。」「私有地址使用配对过程中所交换的设备身份解析密钥而生成。」「在运行 iOS 13 和 iPadOS 13.1 或更高版本的设备上可以跨传输派生链接密钥」
⇒ **注意 EN 原文那句 "On devices with iOS 13 and iPadOS 13.1 or later have the ability…" 本身有语法瑕疵**（Apple 页面原文如此）。引用时保留原样并标注"原文如此"，不要"顺手修正"。
⇒ 手册层把 BLE 隐私机制限定为**这两项**，没有列第三方常谈的"Resolvable Private Address 轮换周期"。轮换周期属 T4，本篇只能标第三方。

## 二、T1 手册层：物理配对模型（`secadb5b6434`，发布 2024-12-19）

可直引（EN）：
> "iOS and iPadOS use a pairing model to control access to a device from a host computer. This establishes a trust relationship between the device and its attached host using a public key exchange."
> "the host and device exchange and save 2048-bit RSA public keys. The host is then given a 256-bit key that can unlock an escrow keybag stored on the device."
> "The exchanged keys are used to start an encrypted SSL session, which the device requires before it sends protected data to the host or starts a service…"
> "To use this encrypted session for all communication, the device requires connections from a host over Wi-Fi, so it must have been previously paired over a physical connection (Thunderbolt or USB)."
> "On devices with iOS 11 and iPadOS 13.1, or later, if a pairing record hasn't been used for more than 30 days, it expires."
> "Certain diagnostic services, including com.apple.mobile.pcapd, are restricted to work only over USB. Additionally, the com.apple.file_relay service requires an Apple-signed configuration profile to be installed."
> "On devices with iOS 11 and iPadOS 13.1, or later, Apple TV can use the Secure Remote Password protocol to wirelessly establish a pairing relationship."
> "A user can clear the list of trusted hosts with the Reset Network Settings or Reset Location & Privacy options."
zh 对照齐全（`_apsgh/txt/secadb5b6434.txt`）。
⇒ 服务名 `com.apple.mobile.pcapd` / `com.apple.file_relay` 是**手册层直接给出的字符串**，可直引，不必标 T3。

## 三、T1 手册层：安全激活数据连接（`sec5044aad1b`，发布 2024-05-07）

可直引（EN/zh 同页）：接口枚举 = **雷雳、USB、Lightning（闪电）、智能接点（smart connector）、SDXC（macOS 13.3 或更高）**；「如果 iPad 或 iPhone 锁定或配件的数据连接终止超过一小时，则解锁设备前不允许建立任何新的数据连接」「这些配件会在上次连接后的 30 天内被记住」「如果一个数据连接通过配件建立了三天以上，设备从锁定时刻起将不会再允许新的数据连接」「用户可以选择在"设置"中重新启用始终打开数据连接（设置某些辅助设备时会自动启用）」。
Apple 自己给的两条设计理由（EN 原文）：
> "This feature limits the attack surface from physically connected devices, such as malicious chargers…"
> "…the accessory ecosystem does not provide a reliable cryptographic way to identify an accessory before a data connection is established."
⇒ 手册层原文用词是 **malicious chargers**。这是可直接引用 Apple 措辞的少数"威胁模型自述"之一，本篇第 X 章应以它为主要依据，而不是自造威胁场景。

## 四、T1 手册层：NFC 与 SE 平台（`secda20f3f41`）

可直引要点（EN 原文，均在同页）：Secure Element = "**an industry-standard integrated circuit that runs the Java Card platform. Certified by both EMVCo and Common Criteria**"；「During transactions, the terminal communicates directly with the Secure Element through the near-field-communication (NFC) controller」；NFC controller「helps ensure that contactless transactions are conducted using a terminal that's in close proximity to the device. **Only requests arriving from an in-field terminal are marked by the NFC controller as contactless transactions.**」；「contactless transaction details are contained to the local NFC field and **aren't exposed to the Application Processor**」；SE 与 SEP「**can communicate securely using a shared secret generated at runtime**」；applet 须「**go through a security review by an accredited third-party security evaluation laboratory**」；「To initiate a transaction, the iOS app must be in the foreground and the iPhone must be unlocked」；全部 SE 凭据会在「signs out of iCloud / removes passcode / erases device / requests Apple Account deletion」时被安全擦除。
iOS 18.1 起开发者可在 SE 存凭据（同页首段）。
⇒ 「Apple Pay 的 NFC 交易细节不经过应用处理器」是 T1 可直引结论，不需要退化为"据称"。

## 五、T1 手册层：超宽带（`sec1e6108efd`，发布 2021-02-18）— 全量引用

整页只有 3 句正文（EN）：
> "The new Apple-designed U1 chip uses Ultra Wideband technology for spatial awareness—allowing iPhone 11, iPhone 11 Pro, and iPhone 11 Pro Max or later iPhone models to precisely locate other U1-equipped Apple devices. Ultra Wideband technology uses the same technology to randomize data found in other supported Apple devices: MAC address randomization / Wi-Fi frame sequence number randomization"
⇒ **取数缺口（重要）**：手册层对 UWB **没有**任何关于信道加密、时间戳安全（STS）、UWB key 分发、测距欺骗防御的表述。凡是"UWB 抗中继/抗距离欺骗"的说法都来自 T4（FiRa / 学术侧），本篇必须写成第三方口径并标明。
⇒ 手册层的 UWB 页还把 UWB 归入**随机化**（MAC + Wi-Fi 帧序号）而非加密，这本身就是可引用的口径事实。

## 六、T1：近场作为"凭据通道"的三个页面（同属本篇）

- 隔空投送（`sec2261183f4`）：BLE 发信号 + **Apple 创建的点对点无线局域网**；「此连接使用 TLS 加密」；iCloud 登录时设备存**2048 位 RSA 身份标识**；按邮箱与手机号生成**隔空投送短身份标识哈希值**；默认"仅限联系人"；"所有人"模式下接收方仍会响应；长哈希比对通过后交换 iCloud 身份证书。
- 无线局域网密码共享（`secd2015ef35`，发布 2024-12-19）：「使用与隔空投送类似的机制」；请求方发 BLE 广播，授予方要求联系信息并要求证明身份后发送密码；组织可限制。
- 将密码发送给其他用户或 Apple 设备（`sec8839a8de2`）：「无论用户的设置如何，通过隔空投送发送凭证操作**始终在"仅限联系人"模式下**进行」；Apple TV 场景「附近的所有 iPad 或 iPhone 都会显示提示」；同 iCloud 账户 → 自动加密，不同账户 → **PIN 码建立加密连接**；「iPhone 必须解锁，并且靠近与该 Apple TV 配对的 Siri Remote 遥控器，才能收到此提示」。
- 接力（`secf78dbe639`）与连续互通概览（`secce267dc10`）：见 `_facts_continuity.md`（选题 E 的简报）。

## 七、T1 术语普查：手册层"没说什么"

对 `_apsgh/txt/` **全量 179 页**（zh）与 `_gpen/` **取样 18 页**（EN）：
| 词 | zh 179 页命中页数 | EN 18 页命中页数 | 用法结论 |
|---|---|---|---|
| AWDL / Apple Wireless Direct Link | 0 / 0 | 0 / 0 | 手册层**从不使用这两个词**，一律写作"Apple 开创/创建的点对点无线局域网技术"。凡出现 AWDL 必须是 T4 或开发者文档口径，且要标明 |
| Nearby Interaction / 邻近交互 | 0 | 0 | 手册层无此条目（EN/zh 均 0 页）⇒ 本篇不能把 Nearby Interaction 框架写成"手册层已记载的机制" |
| 超宽带 | 3 | 9（Ultra Wideband） | 存在专页，但内容仅第 五 节那 3 句 |
| 配对 / pairing | 33 | 11 | 手册层大量使用，可直引 |
| 绑定 / bonding | 20 | 1（EN 仅蓝牙页） | **EN 侧 bonding 只在蓝牙页出现** ⇒ 术语范围比中文窄，不能对译推定 |
⇒ 双口径规则：全量结论只能对 zh 179 页下；EN 侧只允许"取样的 18 页中 0 次"。

## 八、T2 措辞层：蓝牙 / AirDrop / Handoff / Wi-Fi 段落全量普查

基数：`_tmp/adv2/` **257 份**顾问页转储，其中近场族段落 **148 个**，分布在 **73 份**页面上。逐段落词频（同一次普查）：

| Apple 词组 | 在近场族段落中出现次数 | 可写的结论 |
|---|---|---|
| actively exploited | **0** | 顾问页**从未**在任何蓝牙/AirDrop/Handoff/UWB/Nearby/Wi-Fi 段落声明在野利用 ⇒ 本篇不得写"某蓝牙漏洞被在野利用"，只能写"Apple 未声明在野" |
| man-in-the-middle | **0** | T1 蓝牙页把 MITM 防御列为卖点，但 T2 全量 0 次 ⇒ "中间人"只能作为分析术语，不得标 Apple 口径 |
| eavesdrop | **0** | 同上（"被动窃听"仅存在于 T1） |
| sandbox | **0** | 近场族段落里没有沙盒措辞；沙盒句式属其它组件段 |
| privileged network position | 19 | T2 里近场风险的**主导句式**，可直引 |
| denial-of-service | 18 | 近场族的第二主句式 |
| kernel privileges | 4 | **全部来自同一个 CVE-2026-28819 的 `Wi-Fi` 段落**（见下） |
| in Bluetooth range | 2 | 仅 CVE-2024-27867 与 CVE-2025-20701 |
| bypass pairing | 1 | 仅 CVE-2024-44124 |
| previously paired | 1 | 仅 CVE-2024-27867 |

### 蓝牙栈（段落名 `Bluetooth`）

| CVE | 首次修复页 | Impact 原文（Apple） | Description 原文 | 致谢 | 判定 |
|---|---|---|---|---|---|
| CVE-2024-23277 | **iOS/iPadOS 17.4（/120893）**，另 macOS Sonoma 14.4（/120895） | An attacker in a privileged network position may be able to inject keystrokes by spoofing a keyboard | The issue was addressed with improved checks. | Marc Newlin of SkySafe | Entry added March 7, 2024；Apple **未给缺陷类别** |
| CVE-2024-27867 | AirPods/Beats 固件 6A326、6F8（/120907） | When your headphones are seeking a connection request to one of your previously paired devices, an attacker in Bluetooth range might be able to spoof the intended source device and gain access to your headphones | An authentication issue was addressed with improved state management. | Jonas Dreßler | **Available for 是耳机固件而非 iOS** ⇒ 不得写成 iOS 条目 |
| CVE-2026-20650 | **iOS/iPadOS 26.3（/126346）**，另 macOS/tvOS/watchOS/visionOS 26.3（/126348、126351–126353） | An attacker in a privileged network position may be able to perform denial-of-service attack using crafted Bluetooth packets | A denial-of-service issue was addressed with improved validation. | jioundai | 同页 `Bluetooth` 段另有**无 CVE 的纯致谢**：Tommaso Sacchetti |
| CVE-2025-20701 | Beats 固件更新 1B211（/127557） | An attacker within Bluetooth range may be able to listen through the microphone of a device which is not yet paired and actively seeking pair requests | "This is a vulnerability in open source code and Apple Software is among the affected projects. The CVE-ID was assigned by a third party." | Dennis Heinze and Frieder Steinmetz of ERNW GmbH | **Available for: Beats Studio Buds**；Apple 明确写"开源代码 + 第三方分配的 CVE"⇒ 该编号不是 Apple 作为 CNA 分配的 |
| （无 CVE） | iOS 18.2（/121837）、macOS 15.2（/121839）、watchOS 11.2（/121843）、visionOS 2.2（/121845） | — | — | Sophie Winter | Entry added March 17, 2025；`Bluetooth` 段存在但**只有致谢，没有条目** |
| （无 CVE） | iOS/iPadOS 18.6（/124147）等 4 个平台页 | — | — | LIdong LI, Xiao Wang, Shao Dong Chen, and Chao Tan of Source Guard | 同上 |
| （无 CVE） | iOS/iPadOS 26.4（/126792）等 5 个平台页 | — | — | Hamid Mahmoud | 同上 |

### 蓝牙框架 / App 面（段落名 `Core Bluetooth`、`CoreBluetooth - LE`）

| CVE | 首次修复页 | Impact 原文 | Description 原文 | 致谢 | 判定 |
|---|---|---|---|---|---|
| CVE-2024-23250 | iOS/iPadOS 17.4（/120893）、watchOS 10.4（/120881）、tvOS 17.4（/120882）、macOS 14.4（/120895） | An app may be able to access Bluetooth-connected microphones without user permission | An access issue was addressed with improved access restrictions. | Guilherme Rambo of Best Buddy Apps (rambo.codes) | 段落名 **`CoreBluetooth - LE`**（带连字符与 LE），非 `Bluetooth` |
| CVE-2024-44124 | iOS/iPadOS 18（/121250） | **A malicious Bluetooth input device may bypass pairing** | This issue was addressed through improved state management. | Daniele Antonioli | 这是 T2 全量里 **唯一** 一句含 "bypass pairing" 的 Impact |
| CVE-2025-31212 | iOS/iPadOS 18.5（/122404）、macOS 15.5、tvOS 18.5、visionOS 2.5、watchOS 11.5 | An app may be able to access sensitive user data | This issue was addressed through improved state management. | Guilherme Rambo | 段落名 `Core Bluetooth`（带空格）⇒ **Apple 自己在这个段落名上有三种写法**：`CoreBluetooth - LE` / `Core Bluetooth` / `Bluetooth`，须逐页照抄 |
| CVE-2025-43354 | iOS/iPadOS 26（/125108）及 tvOS/watchOS/visionOS 26、macOS Tahoe 26（/125110） | An app may be able to access sensitive user data | **A logging issue was addressed with improved data redaction.** | — | 段落名 `Bluetooth`，但后果是"App 访问敏感用户数据"⇒ **段落名不等于实现层**，这句要单独讲 |
| （无 CVE） | macOS Tahoe 26（/125110）、iOS/iPadOS 26.5（/127110）等 | — | — | Leon Böttger；Nathaniel Oh (@calysteon)；Luke Noble；Yiğit Can YILMAZ 与 Csaba Fitzl of Iru（后两者在 `Bluetooth` 段，Entry updated December 12, 2025） | 纯致谢 |

### AirDrop / Handoff（详见 `_facts_continuity.md`）

CVE-2024-54564（iOS 17.6 /120909，「A file received from AirDrop may not have the quarantine flag applied」，Linwz of DEVCORE，Entry added 2025-03-20）；CVE-2025-24097（iOS 18.4 /122371，「An app may be able to read arbitrary file metadata」，Ron Masas of BREAKPOINT.SH）；CVE-2026-43667（iOS 26.5 /127110 + iOS 18.7.10 /148287，「An attacker in a privileged network position may be able to cause a denial-of-service」，「A reachable assertion was addressed with improved input validation」，Arash Ale Ebrahim / CISPA SCy-Phy，Entry added 2026-08-25）；CVE-2025-30463（Handoff，iOS 18.4）。/120893 的 `AirDrop` 段为**纯致谢**（Cristian Dinca, "Tudor Vianu" National High School, Romania）。

### 唯一一条把 Wi-Fi 段落与内核权限连起来的条目

CVE-2026-28819｜段落 `Wi-Fi`｜Impact「An app may be able to execute arbitrary code with kernel privileges」｜Description「An out-of-bounds write issue was addressed with improved bounds checking.」｜致谢 Wang Yu｜**iOS/iPadOS 18.7.9（/127111，Available for: iPhone XS、XS Max、XR、iPad 7th generation）**，另 macOS Tahoe 26.5（/127115）、Sequoia 15.7.7（/127116）、Sonoma 14.8.7（/127117）。
⇒ 这条是本篇"近场通道 → 内核后果"唯一可由 T2 直引的组合，但**注意它是遗留分支版本**，不是 iOS 26 主线；引用时须带机型与分支。
⇒ **Apple 未给该条目的在野声明**（同上一行普查：近场族 "actively exploited" = 0）。

## 九、T2 结构性事实（可直接进正文）

1. 段落名不是实现层。`Bluetooth` / `Core Bluetooth` / `CoreBluetooth - LE` 三种写法并存，且 `Bluetooth` 段落里出现过"App 访问敏感用户数据"（CVE-2025-43354）。
2. Apple 在近场族里的句式高度集中在 **`privileged network position`**（19/148），这在手册层没有对应表述——手册层讲配对/绑定/加密，顾问页讲"处于特权网络位置的攻击者"。两层的语义落差本身就是分析对象。
3. 音频配件（AirPods/Beats 固件页）自成一组 `Bluetooth` 条目，其 Available for 只写耳机型号，**不具备 iOS 版本可比性**。
4. 大量 `Bluetooth` / `AirDrop` 段落**只有致谢、没有 CVE**（本篇普查中至少 9 处）。凡引用这类段落，只能写"Apple 在某页致谢了某人，且未给出对应 CVE"，不得反推机制。

## 十、缺口登记（本篇必须逐条落地）

1. **手册层无 UWB 安全细节**（第五节）——不得写"UWB 使用加密测距/抗中继"。
2. **手册层无 Nearby Interaction 条目**（EN/zh 全 0 页）——不得把该框架写成手册层机制。
3. **手册层从不写 AWDL**——AWDL 一词只能标第三方或开发者文档口径。
4. **T2 无 "actively exploited"、无 "man-in-the-middle"、无 "eavesdrop"**（近场族 148 段全量）。
5. **T2 无 CVSS**（顾问页一贯不给分）——任何分值须标 NVD。
6. **源码层本期不可用**：本地 XNU 镜像 `x7/` 为空，本轮未取回任何 IOKit/BlueZ 侧类名或符号；凡涉及内核实现的句子一律降级为"未核实"或转标第三方。
7. EN 手册侧仅取样 18 页，其余 161 页的 EN 措辞未核——EN 全量结论一律不写。
8. CVE-2024-23277 / -44124 / -27867 / -20701 / 2026-20650 / 2026-28819 的第三方研究对应关系待代理核验（`_facts_tier4_bluetooth.md`）；未核回之前，正文只写 Apple 侧措辞，不写研究名。

## 十一、需加限定语的清单

- "蓝牙配对过程可被中间人攻击"→ 写成"手册层把防 MITM 列为安全简易配对的功能之一（T1），而顾问页在近场族段落中从未使用中间人一词（T2 全量 0 次）"。
- "Apple 修复了某个蓝牙零点击漏洞"→ 不成立；近场族无在野声明，且"零点击"一词不属 Apple 口径。
- "UWB 用于精确测距因而更难中继攻击"→ 只能写"手册层说 U1 提供空间感知（T1 原文），关于 UWB 抗中继的论证属第三方"。
- "CoreBluetooth 是内核组件"→ 手册层与顾问页都不给实现层；只能说"顾问页把它归入 `Core Bluetooth` / `CoreBluetooth - LE` 段落"。

## 十二、T4 第三方核查结果（2026-10-26 代理回填）

结论先说：**八项里只有三项能从一手来源确认存在公开工作，且没有任何一项由 Apple 或作者本人声明与某个 iOS CVE 对应。**因此报告 #103 正文一律不写研究结论，本节只作为后续选题的准备材料。

| 致谢对象（T2 原文） | 可确认的公开工作 | 与 iOS CVE 的对应关系 | 使用限制 |
|---|---|---|---|
| Marc Newlin of SkySafe（CVE-2024-23277） | "Hi, My Name Is Keyboard"（SkySafe reblog，2023-12，CVE-2023-45866）与续篇 CVE-2024-0230；ShmooCon 2024 演讲 | **未确认**。作者自己的表把 iOS 侧修复记为 17.2 / CVE-2023-45866，不是 17.4 / -23277；把两者相连只是推断 | 检索未发现名为 "DeepPwn" 的工作，**不得写入** |
| Daniele Antonioli（CVE-2024-44124） | 该 CVE 无对应公开工作。主题最近的是 BLERP: BLE Re-Pairing Attacks and Defenses（Sacchetti + Antonioli, EURECOM, NDSS 2026） | **无**。BLERP 晚于 2024 年补丁且未提 Apple CVE | 不得把 BLERP 写成 -44124 的来源 |
| **Tommaso Sacchetti**（/126346 等页 `Bluetooth` 段纯致谢） | 即 BLERP 第一作者 | 纯致谢，无 CVE | 可写"Apple 致谢了后续发表 BLE 重配对研究同一作者"，**不得暗示因果** |
| Jonas Dreßler（CVE-2024-27867） | GNOME 博客短文（2024-06-26）：在 Linux 上重实现 Apple 私有 "Fast Connect" 时发现该缺陷；无论文 | 未由作者声明 | 只有短文可引 |
| Sophie Winter（/121837 等 4 页纯致谢） | **未确认**（同名者多，未能从一手来源核实其研究方向） | 无 CVE | **不得写其研究领域** |
| Dennis Heinze & Frieder Steinmetz of ERNW（CVE-2025-20701） | 已确认：insinuator.net 安全公告（2025-06-26，Airoha 蓝牙耳机）+ 完整披露 "Bluetooth Headphone Jacking"（2025-12-28） | CVE 由 **MediaTek CNA** 分配，受影响为 Airoha AB156x/157x/158x/159x 蓝牙音频 SDK；**不是 Zephyr/BlueZ** | 与 Apple 页面那句"CVE 由第三方分配"吻合，可合并引用；但主体是耳机不是 iOS |
| "Linwz of DEVCORE"（CVE-2024-54564 致谢） | **未找到**公开工作（DEVCORE 研究页与博客均无） | — | 不得写成"DEVCORE 公开披露了该缺陷" |
| Ron Masas of BREAKPOINT.SH（CVE-2025-24097） | **未找到**（其研究页无 AirDrop 元数据主题） | — | 不得写成"由 BREAKPOINT 披露" |
| Arash Ale Ebrahim / CISPA SCy-Phy（CVE-2026-43667 致谢） | 已确认论文："Protocol Prying"（Ale Ebrahim + Tippenhauer, CISPA, USENIX WOOT 2026）：AirDrop 的 Sharing.framework HTTP 路由对未识别路径落入 `fatalError`，未认证请求即触发进程中止 | **推断**：与 Apple "A reachable assertion was addressed with improved input validation" 描述吻合，但论文未列 Apple CVE 号 | 可写"存在一篇 WOOT 2026 的 AirDrop 系统性研究"，不得写"Apple 修复的就是论文里那条" |

三条否定性核查（防误引）：**"BitSnooping" 与 "WhisperLink" 均未找到**；名字最接近的真实工作是 KU Leuven COSIC 的 *WhisperPair*（针对 Google Fast Pair，**不是 CISPA、不是 Apple**）。**任何把 WhisperPair 写成 iOS/AirDrop 或 CISPA 成果的表述都是错的。**

对本篇正文的影响：G12 仍然成立（对应关系均为推断），报告 #103 已按"只写 Apple 侧措辞 + 致谢名单本身"交付，无需改动。
