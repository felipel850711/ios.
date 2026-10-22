@@TITLE@@iOS 凭据类数据的安全保存与上传同步机制分析
@@SUBTITLE@@Where Passwords, Keys, Certificates and Tokens Actually Live — Keychain Rows and Tables, Two AES-256-GCM Keys, Accessibility Classes, Access Groups, SecAccessControl, Passkeys and iCloud Keychain Sync Circles, Secure Enclave Keys, Trust Anchors, Device/App Attest Tokens, and What May Be Uploaded to a Server
@@INFO@@报告日期|2026年10月22日
@@INFO@@分析对象|密码（自动填充密码、自动强密码、密码监视）· 通行密钥与物理安全密钥 · 一次性验证码种子 · 证书与身份（PKCS#12、锚点、策略、撤销）· 密钥（软件密钥、安全隔区密钥、外部令牌密钥）· 令牌（APNs 设备令牌、DeviceCheck、App Attest、SSO 票证、会话令牌）· 钥匙串（类、访问组、访问控制对象、两套实现）· 同步与上传（iCloud 钥匙串信任圈、密钥包、备份、跨设备与跨 App 迁移）
@@INFO@@视角定位|防御与研究教育视角：回答"这份凭据究竟存在哪里、由哪把密钥保护、谁能读到它、什么条件下才被允许上传出去"，并给出防线设计与审计可直接使用的判定链与可观测痕迹
@@INFO@@分类|网络安全 · 凭据管理 · 密钥层级 · 端对端同步
@@INFO@@难度|中级偏高级（需了解对称加密与密钥封装基础、公钥基础设施与 X.509 链、iOS 代码签名与权限体系）
@@INFO@@联系方式|Telegram: https://t.me/ios_one001

@@TOC@@目录

# 0. 概述、分工与口径

## 0.1 本篇要回答的四个问题

一份密码存进钥匙串，并不意味着它从此安全；一个令牌交给服务端，也不意味着它本就该被持久化。本篇把"凭据类数据"拆成四个可以分别取证的问题：

1. **保存在哪里（落点）**：同一条凭据在官方文本里可能落在**六个不同位置**——钥匙串数据库的某一行、安全隔区管理的密钥包、外部物理介质（安全密钥/智能卡）、iCloud 钥匙串的端对端同步域、第三方密码管理 App 自己的容器、或者压根"不该保存"（如 APNs 设备令牌）。把这六个落点说成一个，是这类主题最常见的口径塌缩。
2. **由哪把密钥保护（强度）**：钥匙串项使用**两种不同的 AES-256-GCM 密钥**加密——表格密钥保护元数据、行独有密钥保护私密值；而类密钥又来自用户/设备/备份/托管/iCloud 云备份五个密钥包中的一个（A 级，见 §2.3、§12.1）。凭据的强度不是"加密了"三个字，而是这串密钥层级里最窄的一环。
3. **谁能读到（边界）**：读取由**三条独立的门**共同决定——访问组（谁能寻址到这一项）、保护类与访问控制对象（什么设备状态、什么用户认证下密钥才可用）、以及查询时是否允许认证 UI 与是否附带 `LAContext`（A 级，见 §4、§5）。
4. **什么条件下才允许上传（外发）**：本篇把"上传"分成三个方向——**上传给自己的服务端**（令牌、公钥、设备证明）、**上传到 iCloud 做跨设备同步**（`kSecAttrSynchronizable`、iCloud 钥匙串信任圈）、**上传给另一台设备或另一个人**（隔空投送发送凭证、Apple TV 经蓝牙索取、无线局域网密码共享）。每个方向的授权主体、加密机制和可观测痕迹都不同，且官方在三个方向上都给出了明确的"不许做什么"（如"Never cache the device token locally on the user's device"）。

## 0.2 与系列已有篇目的分工

| 已有篇目 | 它回答的问题 | 本篇与它的关系 |
| --- | --- | --- |
| #82 隔离的硬件安全子系统（安全隔区） | SEP 的处理器、固件、总线与独立存储长什么样 | 本篇只使用"SEP 能保管密钥"这一**结果**，不重述其硬件构成；§8.3、§11.14 需要回看 #82 的存储与测量口径 |
| #83 信任根与验证起点 | 谁第一个签名、信任从哪里开始 | 本篇的"锚点证书"（§9.5）是信任根在**TLS 场景**下的落点，与启动链的信任根是两套对象，不可互相引用 |
| #84/#85 代码签名与完整性执行体系 | 二进制与授权如何被执行时校验 | §9.12 用信任缓存承接"代码信任"这条线，凭据（证书/私钥）在签名体系中是**签名介质**而非被保护数据 |
| #86 程序可使用的特定能力与权限 | entitlement、授权书、能力清单如何签发 | §4.1、§4.2 的访问组边界**由代码签名保护的 entitlement 构成**，本篇只讲凭据侧后果，不重讲 entitlement 生命周期 |
| #87 限制 App 访问文件、进程及系统资源 | 沙盒、数据保险箱、进程围栏 | §7.6 的"凭证元数据存提供程序 App 容器内、卸载即移除"是 #87 容器语义的直接应用 |
| #88 隐私访问授权的管理 | 用户同意如何授予与撤销 | §6.4 的"三方同意"（开发者 + 网站 + 用户）与 #88 的 TCC 式授权是**不同的同意机制**，本篇不把它算作 TCC 授权 |
| #89 文件数据按保护级别与设备状态的访问控制 | 文件类密钥与 BFU/AFU 的关系 | 本篇 §3.1 给出钥匙串类与文件保护类的**官方对等映射表**，§12 复用其密钥包结论；两者密钥与函数不同，不能直接互换引用 |
| #77/#78 iCloud 高级数据保护与威胁映射 | 端对端加密覆盖哪些类别、HSM 承担什么 | §11.10 只讲"服务密钥被收进 iCloud 钥匙串保护域"这一条与凭据相关的因果，其余归 #77 |
| #80 设备真实性防线 | DeviceCheck / App Attest 能证明什么 | 本篇只取"私钥在 SEP、keyId 需自存"这一**保管口径**，能力边界与风控结论归 #80 |

## 0.3 证据分级

本篇沿用系列的四级口径，并对每个来源标注等级：

- **A 级（官方一手，可直接引用原句）**：Apple《平台安全保护》手册（support.apple.com/zh-cn/guide/security，含各页发布日期）与 developer.apple.com 文档（含 availability 元数据、技术说明 TN3137 的 Revision History）。本篇所有逐字引文与全部表格均落在 A 级。
- **B 级（官方二手/间接推导）**：官方文本未直接断言、但可由两处 A 级原句合成。例如"SEP 私钥不参与 iCloud 钥匙串同步"是由 `protecting-keys-with-the-secure-enclave` 与 `kSecAttrTokenID` 的 Important 合成，正文一律标注"推导"（见 §11.14、§16.2）。
- **C 级（社区/研究者口径）**：IMG4/passkey 生态细节、第三方密码管理器的实现习惯、把 device token 存进钥匙串的教程做法。C 级只出现在 §16.4 的对照表中，用于说明"官方口径与流传口径相反"，不作为事实支撑。
- **D 级（明确不采用）**：任何涉及绕过锁屏、提取钥匙串数据库、离线解密或凭据导出的操作性内容。本篇**不含**任何此类步骤，也不引用此类来源。

## 0.4 术语纪律：四个动词不能混

中文里"保存密码"这一个动作，在官方文本里被拆成四个必须分别表述的概念：

| 中文说法 | 官方对应 | 本篇用法 |
| --- | --- | --- |
| 保存 / 存储 | store、`SecItemAdd`、"store small bits of user data in an encrypted database called a keychain" | 数据落盘，且落盘时已被加密（§2.1） |
| 加密 | encryption、表格密钥 / 行独有密钥、类密钥、封装（class key） | 描述**用什么密钥**加密，不是描述"是否加密"（§2.3） |
| 授权 / 可读 | accessibility、`SecAccessControl`、access group、`kSecUseAuthenticationUI` | 描述**密钥在什么条件下可用**，与加密强度是两件事（§3、§4、§5） |
| 同步 / 上传 | synchronizes through iCloud、`kSecAttrSynchronizable`、send to your server | 描述**数据离开本机的路径**，官方把"iOS 行为"与"同步"刻意拆开（§4.7、§11） |

一条常见的塌缩是："存进钥匙串 = 加密 = 安全 = 不会外泄"。本篇 §16.4 会用官方原句逐条拆开这个等式。

## 0.5 全篇强结论预告

以下八条均可在正文中找到 A 级原句支撑，此处先给出以便定位：

1. 钥匙串不是单一密文库，而是**同一 SQLite 数据库被两把不同的 AES-256-GCM 密钥分别加密**：元数据用表格密钥（受 SEP 保护但缓存在应用处理器）、私密值用行独有密钥（始终需往返 SEP）（§2.3、§2.4）。
2. **"能不能读到"不由 keychain 单独决定**：项的属性与可访问性、查询时的作用域（access group / `kSecUseKeychain`）、查询时是否允许认证 UI 三者共同决定（§5.9、§15.1）。
3. 不设设备密码时，**默认的 `WhenUnlocked` 档等于没有额外保护**："A device without a passcode is considered to always be unlocked."（§3.3）。
4. **passkey 的密钥对存在 iCloud 钥匙串并跨设备同步**；反而是**安全隔区密钥只在创建它的那台设备上**且不可跨存储迁移（§7.1、§8.5）——与流传说法恰好相反。
5. **App Attest 的私钥永不离开 SEP，官方要求自存的只有 keyId，且示例是"写文件"而不是写钥匙串**；attestation 对象官方明令**不要**持久化（§10.5）。
6. **APNs 设备令牌官方明令不要在设备本地缓存**，把它写进钥匙串是官方点名的错误做法（§10.3）。
7. **证书装进钥匙串不等于被信任**：信任评估"does not search the keychain for anchor (root) certificates"，锚点必须显式 `SecTrustSetAnchorCertificates`（§9.5）；ATS 域名上信任要求**只能收紧不能放松**（§9.9）。
8. **iCloud 钥匙串的同步是设备自建信任圈的结果，不是 Apple 的服务**：同步身份为非对称椭圆密钥（P-384）、信任圈需双重签名、每个要同步的项必须显式打 `kSecAttrSynchronizable`、第三方 App 项默认不同步（§11.2、§11.5）。

## 0.6 范围与立场声明

本篇为防御性与研究教育性整理：只讨论**官方公开的存储落点、密钥层级、访问条件、同步与上传规则**，以及这些口径能给防线设计与审计提供的证据级别。全篇不涉及、不提供、也不指向任何提取凭据、绕过锁屏、离线解密钥匙串数据库、伪造凭据或绕过认证的操作步骤。凡官方未公开的实现（见 §16.2 的 U 组），本篇一律以"未披露"处理，不以推测填补。

# 1. 模型总览：四类凭据 × 六个保管落点

## 1.1 "密码、密钥、证书、令牌"在官方文本里分别叫什么

官方并没有一个统一的"凭据"类别。四类数据各自散落在不同框架、不同措辞下：

| 中文 | 官方名（一手出处） | 定性句（逐字） |
| --- | --- | --- |
| 密码 | 自动填充密码 / 钥匙串密码项（`kSecClassGenericPassword`、`kSecClassInternetPassword`） | "By default, Password AutoFill saves the user's login credentials on their current iOS device."（A：`security/password-autofill`） |
| 密钥 | 密码学密钥（`SecKey`、`kSecClassKey`）；通行密钥密钥对 | "Keys — Generate, store, and use cryptographic keys."（A：`security/keys`） |
| 证书 | 证书 / 身份（`SecCertificate`、`SecIdentity`、`kSecClassIdentity`） | "An identity consists of a private key packaged with the certificate that contains and vouches for the corresponding public key."（A：`security/identities`） |
| 令牌 | 设备令牌 / token / 认证令牌 / "external store" 的 token | "A globally unique token that identifies this device to APNs."（A：`UIApplicationDelegate`）；"A key whose value indicates that a cryptographic key is in an external store."（A：`kSecAttrTokenID`） |

**口径要点**：`kSecAttrTokenID` 里的 "token" 指**硬件令牌 / 安全隔区**，与"API 访问令牌""会话令牌"毫无关系；而 Apple 文档中的 `token` 一词至少同时指代 APNs 设备令牌、DeviceCheck 一次性令牌、App Attest 的 keyId 语境、以及 `ASCredentialImportToken` 这个 UUID 交接令牌。**同一份报告里出现四处 "token" 而分别定义，是主题本身的强制性要求**（详见 §10.1 与 §16.1 的 C 组冲突）。

## 1.2 六个保管落点

把 developer 文档与《平台安全保护》手册的原句合并，凭据的落点至少六处，且彼此**不能互相替代**：

| 编号 | 落点 | 谁写入 | 谁能读 | 是否可上传 |
| --- | --- | --- | --- | --- |
| L1 | **钥匙串数据库的一行**（SQLite，`SecItem`） | App / 系统 App | 同访问组进程，且密钥可用时 | 仅当显式标记 `kSecAttrSynchronizable`（§11.5） |
| L2 | **安全隔区管理的密钥包**（SEP，`kSecAttrTokenIDSecureEnclave`） | 系统经 SEP 生成 | 只有 SEP 能用；进程只拿引用 | 不可迁移、不可导出明文（§8.3） |
| L3 | **外部介质**（物理安全密钥 / 智能卡 / token） | 由介质自身 | 介质在时才可用 | 不上传；丢失即无法认证（§7.2） |
| L4 | **iCloud 钥匙串的端对端同步域**（iCloud Keychain） | 系统密码管理器 / passkey / TOTP 种子 | 信任圈内的用户设备 | 端对端加密上传，Apple 不可读（§11.11） |
| L5 | **App 自有存储 / App 组容器** | 第三方密码管理 App、provider extension | 该 App 及其组 | 由 App 自行决定；容器随卸载移除（§7.6） |
| L6 | **文件系统与密钥包**（备份/托管/iCloud 云备份密钥包） | 系统 | 视密钥包是否解封 | 备份时以新密钥重新加密（§12.1） |

L1 与 L2 的关系最容易说错：官方原句把 SEP 密钥描述为"**逻辑上属于钥匙串**"但"只有安全隔区能使用该密钥"（§8.4）——即**同一项在元数据层属于 L1，在密钥可用层属于 L2**。

## 1.3 一张对照表：凭据类型 → 落点 → 同步性 → 锚点原句

| 凭据 | 落点 | 官方口径的同步/上传行为 | 正文 |
| --- | --- | --- | --- |
| 网站/App 登录密码 | L1 + L4 | "iOS can sync these credentials securely across the user's devices using iCloud Keychain." | §6.1 |
| 自动强密码 | L1（"自动填充密码"钥匙串）+ L4 | "生成的密码保存在钥匙串中，并在启用了 iCloud 钥匙串的设备间保持更新。" | §6.2 |
| 通行密钥 passkey | L4（iCloud 钥匙串） | "the device stores its public-private key pair in the user's iCloud Keychain and syncs the keys across the user's devices" | §7.1 |
| 物理安全密钥 | L3 | "Security keys store the public-private key pair on a physical medium" | §7.2 |
| TOTP 种子（验证码） | L4 | "iCloud Keychain synchronizes codes across all of the user's devices." | §7.10 |
| 第三方密码管理器中的密码本体 | L5 | "凭证元数据储存在凭证提供程序 App 的容器内，App 卸载时会自动移除。" | §7.6 |
| 证书 / 身份 | L1 | "Store a certificate in the keychain for safekeeping." | §9.1 |
| 软件生成的私钥 | L1（明文短暂入内存） | "to use the key, you must briefly copy a plain-text version of it into system memory" | §8.2 |
| SEP 私钥 | L2 | "you never handle the plain-text key" | §8.3 |
| 设备卡号 / 证件认证私钥 | 安全元件（L2 族） | "完整的付款卡卡号不会储存在设备或 Apple Pay 服务器上" | §8.10 |
| App Attest 密钥 | L2；keyId 由 App 自存（L5/L6） | "The framework securely stores the key in the Secure Enclave." | §10.5 |
| APNs 设备令牌 | **官方要求不落本地** | "Never cache the device token locally on the user's device." | §10.3 |
| DeviceCheck 令牌 | 不落本地，一次性 | "An ephemeral token that identifies the current device." | §10.4 |
| Kerberos 票证 / SSO 令牌 | 系统 SSO 扩展管理 | "配置可允许设备管理服务将必要设置推送给设备" | §10.9 |

## 1.4 "凭据保存在哪"不是一个而是四个问题

developer 文档自身就提供了**四种互不兼容的答案**，任何单一表述都会漏掉三种：

- `kSecUseDataProtectionKeychain`：**"whether to treat macOS keychain items like iOS keychain items"**——落点由"哪套实现"决定（§13.1）。
- `kSecAttrTokenID`：**"Presence of this key indicates that the item is backed by an external store… An item without this attribute is stored as normal in the keychain database."**——落点由"是否在外部存储"决定（§8.5）。
- `kSecAttrSynchronizable`：**"whether the item synchronizes through iCloud"**——落点由"是否进入 iCloud 域"决定（§11.6）。
- `kSecAttrAccessGroup`：**"the access group the item is in"**——即使前三者相同，能读到它的进程集合仍不同（§4.1）。

这四条属性是**正交的**，且官方明确其中两两之间存在不兼容与互斥（`ThisDeviceOnly` 与同步不兼容、`kSecAttrAccessControl` 与 `kSecAttrAccess` 互斥，见 §3.5、§5.3）。§15.1 的判定链因此按"实现 → 属性 → 访问组 → 类 → 认证"顺序排列。

## 1.5 上传的三个方向及其授权主体

| 方向 | 谁批准 | 传输保护 | 典型凭据 |
| --- | --- | --- | --- |
| U1 **上传给自己的服务端** | App 开发者代码路径 | TLS + 服务端策略；App Attest 走"公钥 + keyId 上传，私钥永不离开设备" | 令牌、公钥、设备证明、断言 |
| U2 **上传到 iCloud 供其他设备同步** | 用户（开启 iCloud 钥匙串）+ 信任圈担保 | 端对端加密："钥匙串项通过 Apple 服务器在不同设备之间传输，但会进行端对端加密，使 Apple 和其他设备无法读取其内容。" | 密码、passkey、Wi-Fi 密码、TOTP 种子 |
| U3 **上传给另一台设备或另一个人** | 用户每次显式同意 | 隔空投送"始终在'仅限联系人'模式下进行"；Apple TV 场景需同账户自动加密或异账户 PIN 码 | 已存凭证、无线局域网密码 |

**关键不对称**：U2 与 U3 的批准主体都是**用户**，官方明确"不会将凭证信息透露给 App，除非用户同意将凭证发放给 App"（§6.5）；而 U1 完全落在**开发者自己的代码**里，官方在凭据保管语境下**从未对 U1 设置系统级闸门**——这是本篇对"上传"这一问题最重要的口径限定（§17.2）。

## 1.6 三个必须并读的概念簇（预告）

- **簇一**：`kSecUseDataProtectionKeychain`（iOS 行为）+ `kSecAttrSynchronizable`（同步）+ `ThisDeviceOnly`（不可迁移）。三者常被合称为"iCloud 钥匙串"，官方却刻意拆成三句（§4.7、§11.13）。
- **簇二**：passkey 落点（L4）+ SEP 落点（L2）+ 安全密钥落点（L3）。只读其中一句都会得出错误的"passkey 是否同步"结论（§7.3）。
- **簇三**：证书进钥匙串（L1）+ 锚点搜索范围 + ATS 下限。三者合起来才是"这张证书被不被信任"（§9.5、§9.9）。

# 2. 钥匙串的三件东西：一个数据库、一个监控程序、两把密钥

## 2.1 官方定性：一个"加密数据库"，且面向"小片段数据"

《平台安全保护》手册给钥匙串的定位是能力与规模的双重限定（A 级，发布日期 2024 年 12 月 19 日）：

> "许多 App 都需要处理密码和其他一些简短但比较敏感的数据，如密钥和登录令牌。钥匙串提供了储存这些项的安全方式。不同的 Apple 操作系统采用不同机制实施与各钥匙串保护类关联的保障。在运行 macOS 的设备上，数据保护不直接用于实施此类保障。"

developer 文档同一口径的英文原句（A 级，`security/keychain-services`，单页无发布日期，页脚 © 2026）：

> "Securely store small chunks of data on behalf of the user."
> "The keychain services API helps you solve this problem by giving your app a mechanism to store small bits of user data in an **encrypted database called a keychain**."

**"small chunks"是硬限定而非修辞**：钥匙串不是通用密文存储，官方给出的用途清单是密码、密钥、登录令牌、证书这类短数据。把大对象写入 `kSecValueData` 属于误用（与文件数据保护 #89 的分工见 §0.2）。

同页还有一句常被省略的扩展口径——钥匙串装的不止用户"知道"的秘密：

> "The keychain is not limited to passwords… You can store other secrets that the user explicitly cares about, such as credit card information or even short notes. You can also store items that the user needs but may not be aware of. For example, **the cryptographic keys and certificates that you manage with Certificate, Key, and Trust Services** enable the user to engage in secure communications and to establish trust with other users and devices. **You use the keychain to store these items as well.**"

## 2.2 实现形态：单一 SQLite 数据库 + securityd 决定每个进程能看什么

手册逐字（A 级，同页）：

> "钥匙串以储存在文件系统中的 SQLite 数据库的形式实现，而且数据库只有一个，securityd 监控程序决定每个进程或 App 可以访问哪些钥匙串项。钥匙串访问 API 将生成对监控程序的调用，从而查询 App 的 Keychain-access-groups、application-identifier 和 application-group 权限。访问组允许在 App 之间共享钥匙串项，而非将访问权限限制于单个进程。"

这四句话在本篇中的分量极重，因为它一次性给出：**存储介质（文件系统上的 SQLite）、实例数量（只有一个）、执行主体（securityd 这个独立监控程序）、判定输入（三类 entitlement）**。由此可推出三条对防线设计有用的结论：

1. **钥匙串不是一个文件集合**，"每个 App 有自己的钥匙串文件"的直觉错误；隔离由 securityd 在查询时按 entitlement 实施。
2. **API 调用不是本地读取**，而是一次跨进程请求（"钥匙串访问 API 将生成对监控程序的调用"）。这解释了为什么 §4.5 的错误码是"权限缺失"而不是"文件不可读"，也解释了 §5.8 关于主线程阻塞的官方警告。
3. **判定输入是代码签名里的 entitlement**，因此访问边界与 #86 的能力体系同源：改 entitlement 需要重新签名，而不是运行时申请。

## 2.3 两把 AES-256-GCM 密钥：表格密钥与行独有密钥

手册逐字（A 级，本篇最核心的机制句）：

> "钥匙串项使用两种不同的 AES-256-GCM 密钥加密：表格密钥（元数据）和行独有密钥（私密密钥）。钥匙串元数据（除 kSecValue 外的所有属性）使用元数据密钥加密以加速搜索，私密值 (kSecValueData) 使用私密密钥进行加密。元数据密钥受安全隔区保护，但会缓存在应用程序处理器中以便进行钥匙串快速查询。私密密钥则始终需要通过安全隔区进行往返处理。"

拆成可比对的四行：

| 对象 | 密钥 | 算法 | 保护位置 | 为什么这样设计 |
| --- | --- | --- | --- | --- |
| 元数据（除 `kSecValue` 外的所有属性） | 表格密钥 / 元数据密钥 | AES-256-GCM | 受 SEP 保护，**缓存在应用处理器** | "以加速搜索"——查询要按属性匹配，属性必须在可计算状态下 |
| 私密值 `kSecValueData` | 行独有密钥 / 私密密钥 | AES-256-GCM | **始终需往返安全隔区** | 数据本体不参与搜索，因此不必为性能让步 |

**"搜索需要代价"这一句是理解本篇许多口径的钥匙**：可搜索性要求元数据可被计算，而计算要求它在应用处理器侧可用（缓存）。因此**"这条凭据被加密了"并不等于"它的服务器名、账户名、标签也是不可见的"**。§15.4 的自测清单把这一点列为第一问。

developer 文档从 API 侧给出同一分的两半（A 级，`security/keychain-items`）：

> "When you want to store a secret such as a password or cryptographic key, you package it as a keychain item. Along with the data itself, you provide a set of publicly visible attributes both to control the item's accessibility and to make it searchable… **keychain services handles data encryption and storage (including data attributes) in a keychain, which is an encrypted database stored on disk. Later, authorized processes use keychain services to find the item and decrypt its data.**"

注意官方用词的分裂：属性一侧是 **"publicly visible"**（可见，用于控制可访问性与搜索），数据一侧是 **"decrypt its data"**。同页另一句明确了"哪些项算 secret、由谁判断加密"：

> "Use the `kSecValueData` key to indicate the data you want to store. **Keychain services takes care of encrypting this data if the item is secret, namely when it's one of the password types or involves a private key.**"

以及由类别推断机密性的口径（A 级，`adding-a-password-to-the-keychain`）：

> "The query dictionary's first key-value pair indicates that the item is an Internet password, **from which keychain services infers that the data is secret and requires encryption.**"

**这条推论机制很重要**：是否进入"行独有密钥"这条保护路径，官方说法是**由 item class 推断**（密码类、含私钥的项），而不是由开发者声明。这解释了 §9.1 中"证书可搜索"与 §6 中"密码值需解密"的差异。

## 2.4 类密钥来自密钥包：与文件数据保护的接口

手册逐字（A 级，"用于数据保护的密钥包"，发布日期 2024 年 12 月 19 日）：

> "在 iOS、iPadOS、macOS、Apple tvOS、visionOS 和 watchOS 中，文件和钥匙串数据保护类的密钥都在密钥包中进行收集和管理。这些操作系统使用以下密钥包：用户、设备、备份、托管和 iCloud 云备份。"
> "iOS、iPadOS 和 visionOS 不支持对用户独有的文件系统内容进行单独加密，这就意味着系统使用来自设备密钥包的类密钥对文件独有密钥进行封装。**而钥匙串则使用来自用户密钥包中的类密钥来保护用户钥匙串中的项目。**"

同页给出钥匙串与文件在密钥包归属上的**不对称**，以及一个必须留意的例外：

> "在配置为单用户使用（默认配置）的 iPad 和 iPhone 设备上，设备密钥包和用户密钥包是同一个，并受用户的密码保护。"
> "【注】在'共享 iPad'模式中配置的 iPad 设备有时需要在用户登录前访问凭证，因此需要一个不受用户密码保护的密钥包。"

对"共享 iPad"这一例外，本篇只登记事实与后果（**在该模式下存在一个不以用户密码为熵的凭据保护路径**），不推测其覆盖哪些项；其防线含义记入 §14.4 与 §17.6。

## 2.5 macOS 侧：数据保护不直接用于钥匙串

§2.1 引文最后一句"**在运行 macOS 的设备上，数据保护不直接用于实施此类保障**"与 developer 文档的两条口径互为印证：

- `kSecUseDataProtectionKeychain`：**"The data protection key affects operations only in macOS. Other platforms automatically behave as if the key is set to `true`, and ignore the key in the query dictionary. You can safely use the key on all platforms."**（A 级）
- TN3137：**"The file-based keychain uses access control lists (`SecAccess`). / The data protection keychain uses keychain access groups, supplemented by an optional access control object (`SecAccessControl`)."**（A 级，修订日期 2026-09-24）

因此 macOS 上"凭据保管强度"取决于**程序选择了哪套实现**（§13），而不是取决于保护类档位；把 iOS 的 accessibility 口径直接搬到 macOS 是本主题的常见错误（§16.1 C2）。

## 2.6 一张"读一次凭据"的机制时序

把 §2.2–§2.5 合成时序（每步的失败表现见 §15.3）：

1. 进程构造查询字典（class + 属性 + 可选的 `kSecUseDataProtectionKeychain` / `kSecUseAuthenticationUI` / `kSecUseAuthenticationContext`）。
2. API 生成对 **securityd** 的调用；securityd 读取调用进程的 `Keychain-access-groups`、`application-identifier`、`application-group` entitlement。
3. 用**表格密钥**（可能命中应用处理器缓存）在**唯一的** SQLite 数据库上按属性匹配，命中范围由访问组过滤。
4. 若该项带 `kSecAttrAccessControl` 且要求用户在场，触发认证（生物识别/口令/应用级口令），必要时经 `LAContext` 或系统 UI。
5. 用**行独有密钥**（始终往返 SEP）解密 `kSecValueData`，返回给被授权进程。

第 3 步与第 5 步使用了**两把不同的密钥、两种不同的可用性**，这就是为什么"能被搜到"与"能被读出"是两个独立问题（§15.1 的判定链据此拆分）。

# 3. 钥匙串保护类：把凭据的可用性绑到设备状态上

## 3.1 官方对等映射表

《平台安全保护》手册在"钥匙串数据保护"页直接给出与文件数据保护的对照表（A 级，发布日期 2024 年 12 月 19 日）。表头为"可用性 / 文件数据保护 / 钥匙串数据保护"：

| 可用性 | 文件数据保护 | 钥匙串数据保护 |
| --- | --- | --- |
| 未锁定状态下 | `NSFileProtectionComplete` | `kSecAttrAccessibleWhenUnlocked` |
| 锁定状态下 | `NSFileProtectionCompleteUnlessOpen` | （官方该格为空） |
| 首次解锁后 | `NSFileProtectionCompleteUntilFirstUserAuthentication` | `kSecAttrAccessibleAfterFirstUnlock` |
| 始终 | `NSFileProtectionNone` | `kSecAttrAccessibleAlways` |
| 密码启用状态下 | （官方该格为空） | `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly` |

同页的并读句（决定该表能否被当成"同一套东西"）：

> "系统用于保护钥匙串数据的类结构与文件数据保护中使用的类结构相似。**这些类具有与文件数据保护类对等的行为，但使用的密钥和函数不同。**"

**两个空单元格是信息而非缺陷**：钥匙串侧没有 `CompleteUnlessOpen` 的对等档（文件独有才需要"未打开时可用"），文件侧没有"密码启用状态下"的对等档（文件保护类的密码条件通过 `WhenPasscodeSet` 家族只在钥匙串侧存在）。跨 #89 与本篇引用时，只能说"行为对等"，不能说"同一把密钥"或"同一个 API"。

## 3.2 七档 accessibility 常量：官方按限制强度排序

`item-attribute-keys-and-values` 的 Accessibility Values 分组声明（A 级）：**"listed from most to least restrictive"**。逐字（每条为该常量的官方定义句）：

| 常量 | 官方定义（逐字） | 平台可用性 |
| --- | --- | --- |
| `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly` | "The data in the keychain **can only be accessed when the device is unlocked. Only available if a passcode is set on the device.**" | iOS 4.0.0– / macOS 10.9.0– |
| `kSecAttrAccessibleWhenUnlockedThisDeviceOnly` / `kSecAttrAccessibleWhenUnlocked` | "The data in the keychain item **can be accessed only while the device is unlocked by the user.**" | iOS 4.0.0– / macOS 10.9.0– |
| `kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly` / `kSecAttrAccessibleAfterFirstUnlock` | "The data in the keychain item **cannot be accessed after a restart until the device has been unlocked once by the user.**" | iOS 4.0.0– / macOS 10.9.0– |
| `kSecAttrAccessibleAlwaysThisDeviceOnly` / `kSecAttrAccessibleAlways` | "The data in the keychain item **can always be accessed regardless of whether the device is locked.**" | **闭区间** iOS 4.0.0 – 12.0.0 / macOS 10.9.0 – 10.14.0 |

`kSecAttrAccessible` 键本身的定义句（A 级）：**"A key with a value that indicates when the keychain item is accessible."**

## 3.3 默认档，以及"没设密码就没锁"这一硬事实

`kSecAttrAccessibleWhenUnlocked` 页（A 级）：

> "Items with this attribute **are only accessible while the device is unlocked**. … **This is the default value for keychain items added without explicitly setting an accessibility constant.**"

指南页把默认档的保护强度直接取决于用户是否设密码（A 级，`restricting-keychain-item-accessibility`）：

> "**by default, you can only access keychain items when the device is unlocked. However, a device not protected by a passcode is always unlocked**, so you might want to restrict access further and insist that an item can only be accessed from a device protected by a passcode."
> "**A device without a passcode is considered to always be unlocked. This is the default accessibility when you don't otherwise specify a setting.**"
> "After First Unlock: **This condition becomes true once the user unlocks the device for the first time after a restart, or if the device does not have a passcode.** It remains true until the device restarts again."

三句合起来的口径是：**在没设密码的设备上，`WhenUnlocked` 与 `AfterFirstUnlock` 都等价于"一直可用"**。开发者拿不到"默认即安全"的保证（§16.4 ④）。

## 3.4 指南给出的四档可读名与选型建议

同一指南页把常量翻译成四档并给出建议（A 级，逐字）：

> - "**When Passcode Set**: If the user hasn't set a passcode, **you can't store an item with this setting. If the user removes the passcode from a device, any items with this setting are automatically deleted from the keychain.**"
> - "**When Unlocked**: …"
> - "**After First Unlock**: …"
> - "**Always**: The item is always accessible, regardless of the locked state of the device. **This option isn't recommended.**"
> "**Always use the most restrictive option that makes sense for your app. For extremely sensitive data that you never want stored in iCloud, you might choose `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly`.**"

**注意 `Always` 的双重口径**：符号页仍列出该常量、且未附弃用文字；指南页则写"不推荐"；availability 却给的是**闭区间到 iOS 12 / macOS 10.14**。本篇采用的写法是"官方已停止支持但页面保留"，不写成"Apple 从未提供"（§16.1 C4）。

## 3.5 `WhenPasscodeSetThisDeviceOnly` 的三条独占后果

手册对该档给出三条排他性说明（A 级，逐字）：

> "类 kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly 与 kSecAttrAccessibleWhenUnlocked 行为方式相同；但前者仅当设备配置了密码时可用。此类仅存在于系统密钥包中，且它们：
> - **不同步到 iCloud 钥匙串**
> - **不包括在托管密钥包中**
> - **如果密码被移除或重设，类密钥便会丢弃，这些项目也变得无法使用。**"

这是本篇**唯一一处官方直接写出"某类凭据一定不同步"**的口径，且给出三条独立的失效路径。与 §11.5 的"必须显式标记才同步"叠加后，可得到一条对防线有用的判定：**要让某项既不被 Apple 读到、又不离开设备、又在密码被移除时立刻失效，官方给出的组合只有这一档**（§17.3）。

第三句（密码移除即丢弃类密钥）与 §5.4 的 `applicationPassword` 是**两种不同的"口令绑定"**：前者绑定的是**设备口令的存在状态**，后者绑定的是**另一个独立口令**，二者常被混谈。

## 3.6 `ThisDeviceOnly`：备份可含、恢复不可迁移

手册逐字（A 级）：

> "其他钥匙串类都有对应的'仅限本设备'项目，**后者在备份期间从设备拷贝时始终通过 UID 加以保护，因此如果该项目恢复至其他设备将无法使用。**"

developer 侧同一机制的措辞（A 级，`restricting-keychain-item-accessibility`）：

> "If the attribute ends with the string `ThisDeviceOnly`, **the item can be restored to the same device that created a backup, but it isn't migrated when restoring another device's backup data.**"

两条合起来给出 `ThisDeviceOnly` 的准确语义：**它能进备份，但恢复时只在原设备可用**。§12.3 会看到这条属性与"加密备份才能迁移钥匙串项"如何互相咬合。

## 3.7 系统自带凭据的类分配表（最有实用价值的一页）

手册接着列出"以下列出的类保护针对钥匙串项执行"的实例表（A 级，原表逐行转录）。这是 Apple 公开材料里少见的"某项具体凭据被放在哪一档"的直接答案：

| 项目 | 可访问 |
| --- | --- |
| "接力"广播加密密钥 | 首次解锁后 |
| iCloud 令牌 | 首次解锁后 |
| iMessage 信息密钥 | 首次解锁后 |
| LDAP、CalDAV、CardDAV | 首次解锁后 |
| 邮件账户 | 首次解锁后 |
| Microsoft Exchange ActiveSync 账户 | 首次解锁后 |
| 社交网络账户令牌 | 首次解锁后 |
| VPN 密码 | 首次解锁后 |
| 无线局域网密码 | 首次解锁后 |
| 家庭共享密码 | 未锁定状态下 |
| Safari 浏览器书签 | 未锁定状态下 |
| Safari 浏览器密码 | 未锁定状态下 |
| 访达/iTunes 备份 | 未锁定状态下，不可迁移 |
| VPN 证书 | 首次解锁后，不可迁移 |
| Apple 推送通知服务 (APNs) 令牌 | 始终，不可迁移 |
| 蓝牙密钥 | 始终，不可迁移 |
| iCloud 证书和私钥 | 始终，不可迁移 |
| SIM 卡 PIN 码 | 始终，不可迁移 |
| "查找"令牌 | 始终 |
| 语音信箱 | 始终 |

三条读法要点：

1. **"不可迁移"列即 `ThisDeviceOnly` 家族**（§3.6），与"同步"是不同维度。
2. **APNs 令牌被系统放在"始终、不可迁移"档**——这与 §10.3 的"App 绝不要本地缓存设备令牌"**不矛盾但极易被误读**：系统需要跨重启、在解锁前就持有推送通道密钥，而官方对 App 侧的建议是不要保存这个易变值。二者主语不同（系统 vs App），本篇在 §16.1 C5 单列这条冲突。
3. 表中"始终"档共 6 项（APNs 令牌、蓝牙密钥、iCloud 证书和私钥、SIM 卡 PIN 码、"查找"令牌、语音信箱），说明**`Always` 在系统侧并非历史残留，而是为"锁屏前也必须可用的通道"保留的能力**；对第三方 App 则是 availability 已收敛（§3.4）。这一对照是 §16.4 的重要一条。

## 3.8 后台场景的官方建议

> "使用后台刷新服务的 App 可将 kSecAttrAccessibleAfterFirstUnlock 用于后台更新过程中需要访问的钥匙串项。"（A 级，手册同页）

以及 developer 侧的对称建议（A 级）：

> "Alternatively, **you can relax access restrictions to be able to access an item from a background process when the device is locked.**"

**两句共同划出 `AfterFirstUnlock` 的正当用途**（后台任务在锁屏后仍需凭据），也因此成为攻击面评估时最该单独审视的一档：它一旦设置，凭据在 BFU 之后、锁屏状态下即长期可用。

# 4. 谁能读到同一份凭据：访问组、前缀与两套访问控制模型

## 4.1 访问组的来源：三段 entitlement 拼成的"虚拟数组"

`sharing-access-to-keychain-items-among-a-collection-of-apps`（A 级）逐字：

> "If you develop a family of apps, all of which rely on the same password or cryptographic key, you can use access groups to securely share that password or key among those apps… **This kind of sharing doesn't require interaction with or permission from the user, but limits sharing to apps that are delivered by a single development team.**"
> "an app belongs to all the groups named in **a virtual array of strings that the system forms for each app as the concatenation of** the following items, evaluated in this order: **Keychain access groups**（`keychain-access-groups` entitlement）… **Application identifier**（`application-identifier`，**formed as the team identifier plus the bundle identifier**）… **Application groups**（`com.apple.security.application-groups`）…"

顺序本身即口径：**钥匙串访问组 → 应用标识符 → App 组**。手册从系统实现侧给出同一件事（A 级）：

> "钥匙串项只能在来自同一开发者的 App 之间共享。若要共享钥匙串项，要求第三方 App 在其应用程序组中使用包含前缀的访问组，**该前缀由 Apple Developer Program（Apple 开发者计划）分配。通过代码签名、预置描述文件和 Apple Developer Program（Apple 开发者计划）来强制实施对前缀的要求和应用程序组唯一性。**"

两条 prefix 细节（A 级，逐字）：

> "**Xcode automatically prefixes keychain groups with your team ID.**"
> "**Xcode doesn't prepend the app group with the team identifier.** Instead, it guards against the reuse of app group names across teams when you try to add an app group to a provisioning profile."

**这是最容易被写成"所有组都带 team ID 前缀"的一处**：钥匙串访问组带前缀，App 组不带（唯一性改由描述文件环节把守）。§15.4 把"你的共享组名里有没有 Team ID 前缀"列为检查项。

## 4.2 默认隔离的成因

> "Because app IDs are unique across all apps, and **because the app ID is stored in an entitlement protected by code signing, no other app can use it, therefore no other app is in this group. Any keychain items stored with this access group are private to App One.**"
> "**by default, each app's keychain items remains isolated from all other apps.**"

隔离的根据不是"沙盒目录不同"，而是**代码签名保护下的 entitlement 不可被他人复制**——这与 §2.2 的"securityd 查询 entitlement"是同一机制的两面。

## 4.3 默认组、顺序不可颠倒，以及 App 组永不为默认

> "**The system considers the first item in the list of access groups to be the app's default access group.**"
> "**An app group can't ever be the default, because the app ID is always present and appears earlier in the list. However, a keychain access group can be the default**… If you don't specify any keychain access groups, then **the app ID is the default.**"

**口径含义**：不设 `keychain-access-groups` 时，项落在"仅自己"的 app ID 组里；一旦设置，默认组变成**列表第一项**。迁移代码时若只加组不改顺序，读写会落在不同组（配合 §4.5 的两个不同错误码）。

## 4.4 一项只属一个组：disjoint groups

> "Unlike apps, which can belong to many access groups, **keychain items belong to a single group, identified by the `kSecAttrAccessGroup` attribute.** From the item's point of view, **the world is a collection of disjoint groups**, and the item belongs to exactly one of them."
> "**Any app in a given group can share keychain items with all the other apps in the same group. You can add an app to any number of groups, but the app is always part of at least one group that contains only itself.** … **Keychain items, on the other hand, are always part of exactly one group.**"

"项只属一个组"排除了"一项被多组 ACL 共享"的直觉模型；共享的实现方式是**多个 App 落进同一组**，而不是同一项被打多个标签。

## 4.5 失败语义：写入与读取的错误码不同

> "If you try to use an access group to which your app doesn't belong, **the operation fails and returns the `errSecMissingEntitlement` status. This includes attempts to 'prime' an entry using a zero-length string** as the value for the `kSecAttrAccessGroup` key, because **the empty string represents an invalid group.**"
> "If you specify a group to which your app doesn't belong, no items match and **the query returns the `errSecItemNotFound` status**. If you don't specify an access group in the query, **the search matches any of your app's groups.**"

三条可直接用于排障的口径：**写错组 = `errSecMissingEntitlement`；读错组 = `errSecItemNotFound`（不是权限错误）；不指定组 = 在本 App 全部组内搜索**。

## 4.6 共享形态的平台前提

> Important：**"This form of keychain item sharing applies to all iOS keychain items, and to macOS keychain items when you query with the `kSecUseDataProtectionKeychain` key, set the item's `kSecAttrSynchronizable` attribute, or both."**

macOS 上访问组只有在"走 data protection keychain 那条路"时才生效——与 §4.7 同一套三分口径。

## 4.7 macOS ACL 与访问组互斥；ACL 不能用于 iOS

`access-control-lists`（A 级）的定性：**"Control which apps have access to keychain items in macOS."**，并明确：

> "ACLs **are not available in iOS or in macOS apps that use the iCloud keychain. For keychain item sharing in those environments, use access groups instead.**"

TN3137 把这一分成实现级（A 级，逐字）：

> "Each keychain implementation uses its own access control model: / - **The file-based keychain uses access control lists (`SecAccess`).** / - **The data protection keychain uses keychain access groups, supplemented by an optional access control object (`SecAccessControl`).**"

`kSecAttrAccessControl` 符号页给出 API 级互斥（A 级）：

> Important：**"This attribute is mutually exclusive with the `kSecAttrAccess` attribute."**

**三层互斥（实现层 / 属性层 / 平台层）指向同一结论**：macOS 上"谁能读这条凭据"取决于选了哪套 keychain 实现，而不是某个可叠加的开关；而 iOS 上只有访问组 + `SecAccessControl` 这一条路（§5）。

## 4.8 `kSecUseDataProtectionKeychain`：把"iOS 行为"与"同步"拆开

符号页逐字（A 级）：

> "A key whose value indicates whether to **treat macOS keychain items like iOS keychain items.**"
> "Set the value for this key to `true` in the query dictionary when accessing a macOS keychain item that behaves like an iOS keychain item. For example, **use the data protection key when adding, searching for, or deleting an item to which the `kSecAttrAccessible` or `kSecAttrAccessGroup` attributes apply.**"
> Tip：**"It's highly recommended that you set the value of this key to `true` for all keychain operations. This key helps to improve the portability of your code across platforms. Use it unless you specifically need access to items previously stored in a legacy keychain in macOS."**
> 与同步的解耦句（本篇必引）：**"Items that you store or have stored in macOS with the `kSecAttrSynchronizable` attribute set to `true` also behave like iOS keychain items. However, a `true` value for that attribute additionally causes iCloud to synchronize the item across all the user's devices. Use `kSecUseDataProtectionKeychain` to get the iOS behavior without synchronization."**

availability：iOS 13.0.0– / iPadOS 13.0.0– / macOS 10.15.0– / tvOS 13.0.0– / watchOS 6.0.0– / visionOS 1.0.0–（开区间，现行）。

**最后一句是"用了数据保护 keychain 就等于同步"这一说法的官方否定**（§16.4 ⑥）。

## 4.9 查询侧的作用域与过滤

`search-attribute-keys-and-values`（A 级，**"Filter a keychain item search."**）列出可用于收窄/放宽读取范围的键：

> `kSecUseKeychain` — "**a keychain to operate on**."
> `kSecUseItemList` — "an array of items to search."
> `kSecMatchLimit` — "A key whose value indicates the match limit."（值 `kSecMatchLimitOne` / `kSecMatchLimitAll`）
> `kSecMatchTrustedOnly` — "A key whose value is a Boolean indicating **whether untrusted certificates should be returned**."
> `kSecMatchPolicy` — "A key whose value indicates **a policy with which a matching certificate or identity must verify**."
> `kSecMatchValidOnDate` — "A key whose value indicates the validity date."；`kSecMatchIssuers` — "a string to match against a certificate or identity's issuers."

三条要点：

1. **`kSecMatchTrustedOnly` 的布尔语义与键名反向**（值描述的是"是否返回**不受信任**的证书"），引用时须照引原句，不做"看起来更合理"的改写（§16.1 C7）。
2. `kSecMatchPolicy` 把 §9.3 的 `SecPolicy` 变成**查询过滤器**："证书/身份只有在满足指定策略时才被返回"有官方依据。
3. Discussion 明确这些键服务于 `SecItemCopyMatching` / `SecItemUpdate` / `SecItemDelete` 三个入口，即**访问边界的最后一环发生在查询时，而不是写入时**。

TN3137 补充 macOS 默认搜索范围（A 级）：

> "**`SecItemAdd(_:_:)` adds the item to the default keychain. Use `kSecUseKeychain` to override this.**" / "**Queries, like those done using `SecItemCopyMatching(_:_:)`, consult all keychains in the search list. Use `kSecMatchSearchList` to override this.**"

即：**写入落一处、读取扫整条搜索列表**——这个不对称在 macOS 上会导致"写进去读不到"和"读到别的 keychain 的同名项"两种相反故障（§15.4）。

# 5. 把"人"绑到凭据上：访问控制对象与认证复合条件

## 5.1 官方动机句：默认档不够的时候做什么

`restricting-keychain-item-accessibility`（A 级，标题句 **"Set the conditions under which an app can access a keychain item such as a password."**）：

> "When you use keychain services to store user secrets, **the framework's default behavior provides a reasonable trade-off between security and accessibility.** However, in some cases, you might want to make different choices."
> "Allowing access only when the device is unlocked (the default) **may not be secure enough in all cases**. If your app provides direct control over a bank account, **you can check for the presence of the authorized user at the very last minute before retrieving login credentials from the keychain.** This helps secure the account even if the user hands the device in an unlocked state to someone else. **Impose this restriction by supplying a `SecAccessControl` instance as the value for the `kSecAttrAccessControl` attribute when creating a keychain item.**"

**"at the very last minute before retrieving"** 是这段里最有价值的口径：user presence 保护的是**读取时刻**，而不是保存时刻；它不改变加密，只改变"何时才肯解密"。

## 5.2 构造入口：三个输入与一条"不必重复设置"

`SecAccessControlCreateWithFlags(_:_:_:_:)`（A 级；**"Creates a new access control object with the specified protection type and flags."**）：

> "This method takes two distinct access control inputs. **The first is an accessibility setting of the same kind you can alternatively set directly with the `kSecAttrAccessible` attribute**… **You don't need to set the accessible attribute directly if you set it as part of the access control attribute instead.**"
> "The second input is an access control flag that's the bitwise `OR` of one or more options given in `SecAccessControlCreateFlags`. **You can specifically require biometrics — like Touch ID or Face ID — or a passcode, but you typically use the `userPresence` option… to let the system choose a mechanism, depending on the current situation.**"
> "`protection` 参数：**"Protection class to be used for the item. Use one of the values that go with the `kSecAttrAccessible` attribute key**, namely those listed in Accessibility Values."
> "You use the result of this function as a value for the **`kSecAttrAccessControl` attribute in the `SecItemAdd(_:_:)`, `SecItemUpdate(_:_:)`, or `SecKeyGeneratePair(_:_:_:)` functions.**"

组合方式的官方示例（A 级，逐字，含查询字典）：

```swift
var error: NSError?
let access = SecAccessControlCreateWithFlags(NULL,  // Use the default allocator.
                                             kSecAttrAccessibleWhenUnlocked,
                                             kSecAccessControlUserPresence,
                                             &error);

var query: [String: Any] = [kSecClass as String: kSecClassInternetPassword,
                            kSecAttrAccount as String: account,
                            kSecAttrServer as String: server,
                            kSecAttrAccessControl as String: access,
                            kSecValueData as String: password]
```

## 5.3 flags 逐个：官方定义句与可用性

`SecAccessControlCreateFlags` 成员（A 级，逐字）：

| flag | 官方定义（逐字） | 可用性要点 |
| --- | --- | --- |
| `devicePasscode` | "The item is only accessible if the user successfully enters their device passcode." | iOS 9.0.0– / macOS 10.12.2– |
| `biometryAny` | "The item is accessible if the user successfully authenticates with **any** of their enrolled biometrics." | 现行 |
| `biometryCurrentSet` | "The item is accessible if the user successfully authenticates with biometrics, **and the set of enrolled biometrics is unchanged**. **The item is invalidated if fingers are added or removed for Touch ID, or if the user re-enrolls for Face ID.**" | 现行；语义是"指纹集合变更即失效" |
| `or` | "Combine requirements with a logical OR operation." | 与 `and` 一起构成复合语义 |
| `and` | "Combine requirements with a logical AND operation." | 同上 |
| `userPresence` | "**This is equivalent to specifying `biometryAny`, `or`, and `devicePasscode`.**" | 官方推荐的"由系统选机制"档 |
| `watch` | "Require Apple Watch user authentication." | **macOS 闭区间到 15.0.0** |
| `applicationPassword` | "Require an application-specific password to use this key." | 见 §5.4 |
| `privateKeyUsage` | "Indicate that a private key can be used with **no required usage** / An attempt to use this constraint while generating a key pair **outside the Secure Enclave fails**." | 见 §8.3 |
| `companion` | （符号页**无描述文字**，仅常量存在） | iOS 18.0.0–；记为部分核实 |
| `touchIDAny` / `touchIDCurrentSet` | 归入官方分组 **"Legacy Constraints"** | 现行页保留但已被 biometry 家族取代 |

**两条重要缺失**：官方 flags 集合中**没有** `device` 或 `admin` 之类的 flag（对应 slug 为 404）；`companion` 在符号页**没有描述句**。任何"用 X flag 要求管理员认证"的写法都没有文档依据（§16.2 N3）。

## 5.4 `applicationPassword`：只守这一项的口令，不是设备密码

指南页逐字（A 级）：

> "In addition to requiring a particular device state and user presence, **you can require an application-specific password. This is distinct from the passcode that unlocks the device: It specifically guards a particular keychain item.**"
> "Do this by including the `applicationPassword` option when defining access control flags… **the system prompts the user for a password when creating the item, and then again before retrieving it.** The item can only be retrieved if the user successfully enters the password, **independent of satisfying any other conditions.**"

官方示例（A 级，逐字）：

```swift
let access = SecAccessControlCreateWithFlags(NULL,  // Use the default allocator.
                                             kSecAttrAccessibleWhenPasscodeSet,
                                             kSecAccessControlBiometryAny
                                               | kSecAccessControlApplicationPassword,
                                             &error);
```

**限定语**：示例里的 `kSecAttrAccessibleWhenPasscodeSet` **没有对应文档页（404）**，官方 Accessibility Values 集合中的同族常量是 `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly`。本篇引用该示例时必须带此限定（§16.1 C6）。

## 5.5 复合语义与互斥

- 组合方式：flag 之间是**按位 OR**，但 `and` / `or` 两个 flag 决定多个要求之间的逻辑关系（§5.3）。
- `userPresence` 的定义句本身就是"biometryAny OR devicePasscode"的缩写（§5.3），因此**它不是"最严"档而是"最省事"档**；要"必须生物识别、不许口令回退"，官方对应的是 `biometryAny`（或 `biometryCurrentSet`）**不带** `devicePasscode`。
- `kSecAttrAccessControl` 与 `kSecAttrAccess`（macOS ACL 键）互斥（§4.7）。
- `privateKeyUsage` 在非 SEP 密钥生成中使用会失败（§8.3）——即**该 flag 的适用范围本身就是密钥保管落点的函数**。

## 5.6 生物识别档：LAPolicy 清单与一处不一致

`LAPolicy`（A 级，**"The set of available local authentication policies."**）：

> `deviceOwnerAuthenticationWithBiometrics` — "**User authentication with biometry.**"
> `deviceOwnerAuthentication` — "**User authentication with biometry, Apple Watch, or the device passcode.**"
> `deviceOwnerAuthenticationWithWatch` / `deviceOwnerAuthenticationWithBiometricsOrWatch` / `deviceOwnerAuthenticationWithWristDetection`（"User authentication with **wrist detection** on watchOS."）
> 新增 companion 档：`deviceOwnerAuthenticationWithBiometricsOrCompanion` — "**Device owner will be authenticated by biometry or a companion device e.g. Watch, Mac, etc.**"；`deviceOwnerAuthenticationWithCompanion`

不一致点（A 级）：**`kLAPolicyDeviceOwnerAuthentication` 的官方描述是 "User authentication with either biometry or the device passcode"，与同义枚举 case 的措辞（含 Apple Watch）不一致**。本篇按"两处都引、不合并"处理（§16.1 C9）。

## 5.7 `LAContext` 与钥匙串的真实关系

`LAContext`（A 级，LocalAuthentication 框架，**"A mechanism for evaluating authentication policies and access controls."**）：

> "You use an authentication context to evaluate the user's identity, either with biometrics like Touch ID or Face ID, or by supplying the device passcode. **The context handles user interaction, and also interfaces to the Secure Enclave, the underlying hardware element that manages biometric data.**"
> Important：**"Include the NSFaceIDUsageDescription key in your app's `Info.plist` file if your app allows biometric authentication. Otherwise, authorization requests may fail."**
> Topics **"Evaluating access controls"**：**`evaluateAccessControl(SecAccessControl, operation: LAAccessControlOperation, localizedReason:reply:)`** — "Evaluates an access control for a given operation."

`evaluateAccessControl(_:operation:localizedReason:reply:)` 页（A 级）把这条接口钉死在 `SecAccessControl` 上：

> `accessControl` 参数：**"The access control to be evaluated."**（类型为 `SecAccessControl`）
> "This method asynchronously evaluates an access control. **Evaluating an access control may involve prompting the user for various kinds of interaction or authentication. The actual behavior is dependent on the access control and device type. It can also be affected by installed configuration profiles.**"
> "**You should not assume that a previous successful evaluation of an access control necessarily leads to a subsequent successful evaluation.** Access control evaluation can fail for various reasons, **including cancelation by the user or the system.**"
> `localizedReason`：**"It should not contain the app name**, because that appears elsewhere in the authentication dialog. **In macOS this appears in the dialog title, and in iOS this appears in the dialog subtitle.**"

**必须纠偏的口径**：LocalAuthentication 文档**从未声称 `LAContext` 用于"解密"钥匙串项**。绑定用 `SecAccessControl` + `kSecAttrAccessControl`，`LAContext` 只是**评估该 access control** 的一种途径（B 级推导，见 §16.2 N4）。

## 5.8 把上下文交给钥匙串：`kSecUseAuthenticationContext` 的三条规则

A 级（`kSecUseAuthenticationContext`，availability iOS 9.0.0– / macOS 10.11.0– / watchOS 2.0.0–）：

> "A key whose value indicates **a local authentication context to use.**"
> "The corresponding value is of type `LAContext`… according to the following rules:
> - **If this key is not specified, and if the item requires authentication, a new context will be created, used once, and discarded.**
> - **If this key is specified with a context that has been previously authenticated, the operation will succeed without asking user for authentication.**
> - If this key is specified with a context that has not been previously authenticated, the system attempts authentication on the context. If successful, the context may be reused in subsequent keychain operations."

第二条是**"已认证上下文可复用"**这一性能与体验口径的唯一官方依据，也是"复用窗口"这一风险项的官方出处（§17.4）。配套的 `touchIDAuthenticationAllowableReuseDuration`（"The duration for which Touch ID authentication reuse is allowable."）与 `LATouchIDAuthenticationMaximumAllowableReuseDuration` 给出了可设上限。

## 5.9 是否弹认证 UI：现行键、废弃键与三个取值

A 级（`kSecUseAuthenticationUI`，availability iOS 9.0.0– 等全平台**开区间**）：

> "A key whose value indicates **whether the user is prompted for authentication.**"
> "**A default value of `kSecUseAuthenticationUIAllow` is assumed when this key is not present.**"

A 级（`kSecUseNoAuthenticationUI`，availability **全部为闭区间**：iOS 8.0.0 – 9.0.0、macOS 10.10.0 – 10.11.0 等）：

> "A key whose value is a Boolean indicating whether to disallow UI authentication." / "If provided with a value of kCFBooleanTrue, the error **`errSecInteractionNotAllowed`** is returned when the item is attempting to authenticate with UI."

三个取值（A 级，逐字，注意语义层级不同）：

> `kSecUseAuthenticationUIAllow` — "**user authentication is allowed.**"
> `kSecUseAuthenticationUIFail` — "**user authentication is disallowed.**"
> `kSecUseAuthenticationUISkip` — "**items requiring user authentication should be skipped.**"

**两个纠偏**：① 官方文本里被收敛的是 `kSecUseNoAuthenticationUI`（闭区间），**`kSecUseAuthenticationUI` 仍是现行键**——这与常见的"kSecUseAuthenticationUI 已废弃"说法相反（§16.4 ⑨）；② `Allow/Fail` 管"能不能弹 UI"，`Skip` 管"要不要跳过需认证的项"，三者不可互换。

同页另有 `kSecUseOperationPrompt`（"A key whose value is an operation prompt."）用于自定义提示语。

## 5.10 主线程与后台的官方警告

> "Accessing keychain items or performing operations on keys that are protected by access control objects **may block execution on the main thread**. Perform these actions in the background, or use them in combination with the **`kSecUseAuthenticationContext` and `kSecUseAuthenticationUI` attributes to manage user interactions.**"（A 级）

以及密钥生成侧的锁定态限制（A 级，`SecKeyCreateRandomKey`）：

> "…fails and returns **`errSecInteractionNotAllowed`** if you call it **in the background on iPhone or iPad while the device is locked**."

这两句合起来给出**"带访问控制的凭据操作"的运行时约束**：阻塞、需后台执行、锁屏后台直接失败。对防线设计的意义在于：一个把 `biometryCurrentSet` 用对的实现，其失败模式（`errSecInteractionNotAllowed`）与配置错误（`errSecMissingEntitlement`）在错误码上可区分（§15.3）。

## 5.11 `SecAccessControl` 与项生命周期的边界

- 访问控制对象在**创建时**通过 `kSecAttrAccessControl` 绑定；`kSecAttrTokenID` 则明确"You can't change this attribute after creating the keychain item"（§8.5）。二者共同构成**创建即定型**的属性族。
- `biometryCurrentSet` 的失效条件是**生物特征集合变化**（§5.3），即由用户 enrollment 变更驱动，而非由 App 或系统策略驱动——这是 §14.1 里唯一由用户操作直接使凭据失效的路径之一（另两条：移除设备密码使 `WhenPasscodeSet` 类密钥丢弃（§3.5）；关闭 provider extension 清空共享库（§7.7））。

# 6. 密码：从生成、落库到跨设备与跨人传递

## 6.1 官方给这个库起的名字："自动填充密码"钥匙串

手册"密码安全性概览"（A 级，发布日期 2024 年 12 月 19 日）把"最好的密码管理是不用密码"作为第一句：

> "管理密码最好的方式就是不使用密码。'通过 Apple 登录'可让用户无需创建和管理额外的账户或密码即可登录第三方 App 和网站… 对于不支持'通过 Apple 登录'的站点，'**自动强密码**'功能可让用户的设备针对站点和 App 自动创建、同步和输入唯一的强密码。在 iPad、iPhone 和 Apple Vision Pro 上，**密码会保存到特殊的'自动填充密码'钥匙串中**，用户可在'设置' > '密码'中控制和管理。"
> "在 Mac 上，已存密码可在 Safari 浏览器的'密码'偏好设置中管理。此同步系统也可用于同步由用户手动创建的密码。"

**"特殊的'自动填充密码'钥匙串"是 Apple 对该存储位置的官方中文名**，与 §2 的通用钥匙串不是同一个抽象层次：前者是一个用途专化的密码库（macOS 上另有"iCloud 钥匙串密码管理器"称呼，见 §7.13）。

developer 侧的默认落点与同步性（A 级，逐字）：

> "**By default, Password AutoFill saves the user's login credentials on their current iOS device. iOS can sync these credentials securely across the user's devices using iCloud Keychain.**"
> "**Password AutoFill recommends credentials only for your app's associated domain, and the user must authenticate using Face ID or Touch ID before accessing these credentials.**"
> "Password AutoFill also provides credentials from **third-party password managers that implement a credential provider extension**."

两句合起来的准确表述：**默认本机保存，iCloud 同步是可选项；建议只限关联域；访问必过生物识别**。§16.4 ⑩ 会看到"默认就同步"与"必须认证"这两种加码说法都被官方文本否定。

## 6.2 自动强密码：默认 20 字符、71 位熵

手册（A 级，"自动强密码"，发布日期 2024 年 12 月 19 日）：

> "如果 iCloud 钥匙串已打开，当用户在 Safari 浏览器网站上注册或更改密码时，iOS、iPadOS、macOS 和 visionOS 会创建唯一的随机强密码。自动强密码生成也适用于 App。用户必须自行选择不使用强密码。**生成的密码保存在钥匙串中，并在启用了 iCloud 钥匙串的设备间保持更新。**"
> "**生成的密码长度默认为 20 个字符。其中包含一位数字、一个大写字符、两个连字符和 16 个小写字符。这些生成的密码均为包含 71 位熵的强密码。**"

71 位这个数字值得单独强调：**它是官方直接给出的密码强度量化口径**，也是本篇里少数几个可被防线指标直接采用的数值（§17.5）。注意前提句"**如果 iCloud 钥匙串已打开**"——未开启时生成分支是否可用，官方未述（§16.2 N6）。

## 6.3 让 App 与网页能被正确识别的两个字段

> "密码基于一种启发技术生成，这种启发技术可确定密码栏是否用于密码创建。**如果启发技术未能识别创建密码时使用的特定环境密码，App 开发者可在其文本栏上设定 `UITextContentType.newPassword`，网页开发者可在其 `<input>` 元素中设定 `autocomplete="new-password"`。**"（A 级，手册）
> "为帮助确保生成的密码与相关服务兼容，App 和网站可提供规则。**开发者使用 `UITextInputPasswordRules` 或输入元素上的 `passwordrules` 属性**来提供这些规则。设备随后会生成可以满足这些规则的最强的密码。"（A 级，手册）

developer 侧对同一件事的补充（A 级）：`customizing-password-autofill-rules` — "Modify the strong password rules for your app by adding your own restrictions."；`enabling-password-autofill-on-a-text-input-view`；`UITextContentType` 家族 `.username` / `.password` / `.newPassword` / `.oneTimeCode`。

工作流页还有一条常被忽略的硬前提（A 级）：

> "In iOS 12, the `passwordRules` property is supported only on `UITextField` objects, and the text field's **`isSecureTextEntry` property must be set to true**."

## 6.4 App 读到已存密码需要三方同意：AASA 是闸门

手册"App 访问已存密码的权限"（A 级）：

> "iOS、iPadOS、macOS 和 visionOS App 可以使用 `ASAuthorizationPasswordProvider` 和 `SecAddSharedWebCredential` 请求'自动填充密码'钥匙串来帮助用户登录。"
> "**只有 App 开发者和网站管理员同时批准且用户同意后，App 才可访问已存密码。** App 开发者通过在其 App 中加入授权，让系统获知他们需要访问 Safari 浏览器的已存密码。**授权书中列出了关联网站的完全限定域名，网站必须将文件放在其服务器上，并在其中列出已获 Apple 批准的 App 的唯一 App 标识符。**"
> "安装了带有 `com.apple.developer.associated-domains` 授权的 App 后，iOS、iPadOS 和 visionOS 向列表中的每个网站发出 TLS 请求来请求以下其中一个文件：**`apple-app-site-association`** / **`.well-known/apple-app-site-association`**"
> "如果文件列出被安装 App 的 App 标识符，则 iOS、iPadOS 或 visionOS 会将网站和 App 标记为具有信任关系。**只有在具有信任关系的情况下，调用这两个 API 时才会用户发出提示，用户同意之后，密码才能发放给 App，或者被更新或删除。**"

developer 侧工作流页给出同一条链（A 级，逐字）：

> "The system takes each domain from the `com.apple.developer.associated-domains`. It tries to download the **Apple App Site Association file (`apple-app-site-association`)** for that domain. If all the steps succeed, **the system associates the app with that domain, and enables Password AutoFill for that domain's credentials**."

**三方同意的主体分别是**：开发者（entitlement，需 Apple 批准的 App ID）+ 网站管理员（服务器上的 AASA 文件）+ 用户（提示后同意）。这构成"上传方向 U1 之外的第二条外发路径"：**密码从系统库流向 App**，官方把它设计成需要域名侧共同背书（§14.3）。

## 6.5 系统不主动把凭证交给 App

> "**'自动填充密码'不会将凭证信息透露给 App，除非用户同意将凭证发放给 App。凭证列表的下拉或显示不包括在 App 的流程中。**"（A 级，手册"自动填充密码安全性"）
> "当 App 和网站具有信任关系，且用户在 App 内提交了凭证时，iOS、iPadOS 和 visionOS **可能会提示用户将这些凭证保存到'自动填充密码'钥匙串中供后续使用**。"

**"凭证列表的显示不包括在 App 的流程中"这一句是防线评估里最有用的单句**：建议列表的呈现由系统进程完成，App 拿不到"用户有哪些站点密码"这一集合信息（与 §6.9 密码监视刻意隐藏数量是同一设计取向）。

## 6.6 快速输入栏出现的两个前提，与认证时的 App 状态

> "**The QuickType bar only appears if the user has at least one password saved on the iOS device and the Keychain AutoFill setting is enabled.** The key icon gives users access to all the credentials saved on the device, while the QuickType bar includes any credentials for your associated domains."（A 级）
> "When users select an item from the QuickType bar, **the system asks them to authenticate using Face ID or Touch ID. Your app becomes inactive when Face ID or Touch ID appears**, triggering `applicationWillResignActive(_:)` and `applicationDidBecomeActive(_:)`."（A 级）
> Note：**"You may want to hide sensitive information when an app becomes inactive. However, if the user hasn't logged in, no sensitive information is exposed, and this precaution should not be necessary."**（A 级）

三条推论（均标注为按用词推导，B 级）：出现建议栏本身就要求"本机已有密码 + 设置开启"；选中建议必有一次用户认证；**认证对话框出现时 App 已非活跃**，即 App 无法在认证完成前观察到填充动作。

## 6.7 安全码在快速输入栏上的可见窗口

> "If the system can parse a security code from an SMS message, **the QuickType bar shows the code for up to three minutes after it has been received.**"（A 级）

**"three minutes"是本篇少数几个官方给出的时序上限之一**；它与 §7.10 的 TOTP `period` 默认 30 秒是两个不同的窗口，不可混用（§16.1 C10）。

## 6.8 密码安全建议：三类标记

手册（A 级，"密码安全建议"，发布日期 2024 年 12 月 19 日）：

> "iOS、iPadOS、macOS 和 visionOS 中'自动填充密码'的密码列表会指示用户已存的哪些密码**在其他网站上重复使用**、哪些密码**强度视为弱**以及哪些密码**在数据泄露中被盗**。"
> "**如果已存密码在不同域间多次重复使用，则密码会被标记为重复使用。**"
> 弱口令检测口径（逐字）："iOS、iPadOS、macOS 和 visionOS 会检测用于创建易记密码的常见规律，例如使用在词典里找到的字词、替换常见字符（如用 p4ssw0rd 代替 password）、键盘上找到的规律（如 QWERTY 键盘上的 q12we34r）或重复的序列（如 123123）。"
> PIN 特判（逐字）："许多服务特别要求使用 4 位或 6 位的数字 PIN 码，因此这些短密码会通过不同的规则进行评估。如果 PIN 码为最常用的 PIN 码之一，或为升序或降序的序列（如 1234 或 8765），或者遵循重复的规律（如 123123 或 123321），则其强度会被视为弱。"
> "强度较弱、重复使用和已泄露的密码会在密码列表 (macOS) 中标识出来，或者显示在专门的'安全建议'界面（iOS、iPadOS 和 visionOS）。如果用户在 Safari 浏览器中使用以前保存的弱强度密码或在数据泄露中出现过的密码登录网站，则会出现提示，强烈建议用户升级到自动强密码。"

## 6.9 密码监视：本地列表 + 隐私保护集合交集

手册（A 级，"密码监视"，发布日期 2021 年 2 月 18 日）关键句：

> "'密码监视'功能是将储存在用户'自动填充密码'钥匙串中的密码与已知在不同在线组织的密码泄露中出现过的密码列表进行对比，**该列表将持续更新并精选**。"
> "用户的设备会持续轮替检查用户的密码，**查询间隔不受用户密码或密码管理器使用模式影响**… 为了帮助防止**泄露用户拥有多少个唯一密码的相关信息，查询请求会进行批处理且并行执行**。每次检查时会并行验证固定数量的密码，**如果用户拥有的密码数量少于此数量，则会生成随机密码并添加到查询以补齐数量差。**"
> "密码分为两个阶段进行匹配。**最常见的泄露密码包含在用户设备上的本地列表中。** 如果用户的密码包含在此列表中，用户会立即收到通知，无需执行任何外部交互。"
> "为了帮助确保合法用户的密码不会发送给 Apple，对比过程采用了**加密隐私保护集合交集**这种形式… 这样做旨在确保只会与 Apple 共享泄露风险较小的密码的少量信息，**对于用户密码，此信息仅限于加密哈希值的 15 位前缀。**"
> "基础协议会将精选密码列表（**编写本文时包含约 15 亿个密码**）细分为 **2^15 个不同的存储桶**。密码所属的存储桶取决于密码 SHA-256 哈希值的前 15 位。此外，每个泄露的密码 pw 与 **NIST P256 曲线**上的一个椭圆曲线点相关联：Ppw = ⍺·HSWU(pw)，其中 **⍺ 是仅为 Apple 所知的随机密钥**…"
> "设备生成自己的随机常量 β 并将点 Pc = β·HSWU(upw) 连同与 λ 对应的存储桶查询请求发送到服务器… 如果 ⍺Pc ∈ B'λ，则用户的密码已经泄露。"

**这段是本篇关于"凭据是否可以被上传"最严格的一条官方设计声明**：即使用户主动请求一次比对，协议也只允许 15 位哈希前缀离开设备，并用随机补齐隐藏数量信息。§17.2 会把"密码监视会上传用户密码"这类说法列为不能支持的结论。

## 6.10 上传方向 U3（人）：隔空投送发送已存凭证

手册（A 级，"将密码发送给其他用户或 Apple 设备"，发布日期 2021 年 2 月 18 日）：

> "Apple 借助隔空投送和 Apple TV 将密码安全地发送到其他用户或 Apple 设备。"
> "iCloud 启用时，用户可使用隔空投送将已存凭证发送到另一台设备。凭证包括用户名和密码，以及其所对应的网站。**无论用户的设置如何，通过隔空投送发送凭证操作始终在'仅限联系人'模式下进行。** 在接收设备上，用户同意后，凭证会储存在用户的'自动填充密码'钥匙串中。"

三个可核对的口径：**需 iCloud 启用 / 强制"仅限联系人"（不受用户 AirDrop 全局设置影响）/ 接收侧需同意后才入库**。

## 6.11 上传方向 U3（设备）：Apple TV 经 BLE 索取

> "用户在 Apple tvOS 中聚焦到用户名或密码文本栏时，Apple TV 会开始通过**低功耗蓝牙 (BLE)** 来广播'自动填充密码'请求。"
> "附近的所有 iPad 或 iPhone 都会显示提示，邀请用户与 Apple TV 共享凭证。以下是加密方法的建立方式：**如果设备和 Apple TV 使用同一个 iCloud 账户，设备间的加密会自动完成。** **如果设备登录的 iCloud 账户与 Apple TV 使用的不同，用户会收到通过使用 PIN 码来建立加密连接的提示。** iPhone 必须解锁，并且靠近与该 Apple TV 配对的 Siri Remote 遥控器，才能收到此提示。"
> "使用 BLE 链接加密建立加密的连接后，凭证会发送到 Apple TV，并自动填充到 App 中相关的文本栏内。"

**同账户自动加密、异账户降级为 PIN 协商**，且"iPhone 必须解锁"是硬前提——这三条一起构成 §15.1 判定链在跨设备场景下的实例。

## 6.12 无线局域网密码的共享：与隔空投送同族机制

手册（A 级，发布日期 2024 年 12 月 19 日）：

> "支持无线局域网密码共享的 Apple 设备使用与隔空投送类似的机制将无线局域网密码从一台设备发送到另一台设备。"
> "当用户选择一个无线局域网（请求方）且系统提示输入无线局域网密码时，Apple 设备发起低功耗蓝牙 (BLE) 广播… 附近处于唤醒状态且拥有所选无线局域网密码的其他 Apple 设备，会使用 BLE 连接正在请求的设备。"
> "拥有无线局域网密码的设备（授予方）要求请求方的联系信息，而请求方必须使用与隔空投送类似的过程来证明自己的身份。一旦身份获得证明，授予方会将用于加入网络的密码发送给请求方。"
> "**针对通过设备管理服务管理的设备或 App，组织可以限制无线局域网密码共享的使用。**"

最后一句是本篇少见的**组织侧对某条具体外发路径的显式否决权**，记入 §14.4。注意被共享的 Wi-Fi 密码在 §3.7 表里属"首次解锁后"档——**同一个值同时受"类"与"共享协议"两层控制**。

## 6.13 Shared Web Credentials：作用域由域名决定，且已被标注上界

`shared-web-credentials`（A 级）：

> "Users often save their username and password in their **iCloud keychain** when logging into websites in Safari… **With shared web credentials, the app can access the credentials stored for the website instead of requiring the user to reenter a username and password.** Users can also create new accounts, update passwords, or delete their accounts from within the app. **These changes are then saved and used by Safari.**"
> Note：**"Accessing shared web credentials requires permission from the app, the website, and the user."**
> 官方图注：**"Diagram showing a connection between your app and Safari through iCloud keychain."**

`SecAddSharedWebCredential(_:_:_:_:)`（A 级）的作用域与**可用性上界**：

> "This function adds a shared password item that will be **accessible by Safari and apps that have the specified fully qualified domain name in their `com.apple.developer.associated-domains`.**"
> "`password` 参数：**"Pass `NULL` to remove a shared password if it exists."**
> "Because a request involving shared web credentials **may potentially require user interaction or other verification to be approved**, this function is dispatched asynchronously."
> availability 逐字（**注意上界**）：`iOS: 8.0.0 - 26.2.0`、`iPadOS: 8.0.0 - 26.2.0`、`macCatalyst: 14.0.0 - 26.2.0`、`macOS: 11.0.0 - 26.2.0`、`visionOS: 1.0.0 - 26.2.0`

**两点必须同引**：可见性由 **associated-domains 校验**决定而非 access group；该 API 在最新 SDK 上已出现终止版本 26.2.0，配合 §7.12 的 `ASCredential*Manager`，说明官方正在把"App↔Web 凭据共享"迁出老路径（§16.4 ⑭）。另注意 macOS 侧起点是 11.0.0 而 iOS 是 8.0.0——**同一 API 的平台起点并不对称**。

## 6.14 密码与"上传到服务端"：官方没有任何一句要求上传

把 §6.1–§6.13 合并后可以看到：官方对**密码本体**给出的外发路径只有三条——**iCloud 钥匙串端对端同步（§11）**、**隔空投送/Apple TV 等用户显式同意的设备间发送**、**用户自己在表单中提交给网站**。developer 文档与手册中**都没有"App 应把用户密码上传到服务端保存"的口径**；相反，§6.1 的 "By default… on their current iOS device"、§7.13 的"升级成功后系统会从钥匙串中删除之前所使用的密码凭证"、§17.2 的"迁移到 passkey / 通过 Apple 登录"共同指向相反方向。

# 7. 通行密钥、验证码与凭证提供程序扩展

## 7.1 passkey 的落点：官方一句定死

`public-private-key-authentication`（A 级，标题句 **"Register and authenticate users with passkeys and security keys, without using passwords."**）：

> "**There are two forms of public-private key authentication: *passkeys* and *security keys.* With passkeys, the device stores its public-private key pair in the user's iCloud Keychain and syncs the keys across the user's devices. Security keys store the public-private key pair on a physical medium, such as a security card or a USB key.**"

三个术语的官方定义（A 级，逐字）：

> "Instead of using a password, **your macOS, iOS, or iPadOS device, known as the *authenticator*, generates a public-private key pair at account creation time, and sends the public key to the server. The server, known as the *relying party*, holds the public key for subsequent authentication, and uses *assertion* to challenge the authenticator to prove its identity is valid.**"

以及该框架总览里对能力的一句话（A 级，`authenticationservices`）：

> "Provide a **passwordless registration and authentication workflow** for apps and websites **using iCloud Keychain or a physical security key**."

**这是本篇关于"passkey 是否同步"的唯一直接依据**：官方写的是"存在 iCloud Keychain 并跨设备同步"。而 `connecting-to-a-service-with-passkeys` 一页**不含**任何关于存哪里的论述（该页 `role` 为 **sampleCode**，正文只有 Xcode 配置步骤，见 §7.4）——引用时不要把示例工程页当论述页。

## 7.2 物理安全密钥：落在外部介质，丢失即无法认证

`supporting-security-key-authentication-using-physical-keys`（A 级，**"Allow users to authenticate using NFC, USB, and Lightning security keys in your app or service."**）：

> "Security key authentication supports physical devices for authentication to web services, eliminating the need for passwords. Such devices are **especially useful in high-security contexts with apps and websites that are able to handle the complexity that comes with account recovery due to security key loss**."
> "Take special care when using physical devices for authentication. **If the user loses the device or someone steals it, there is no way to perform authentication with associated services. Have a backup strategy in place for such events.**"
> Important：**"You must have an associated domain with the `webcredentials` service type when making a registration or assertion request; otherwise, the request returns an error."**
> Note：**"Unlike platform public key requests and other types of authorization requests, a security key request always shows a sign-in user interface."**
> "**You're responsible for communicating with the server to obtain the challenge.**"

**两句限定值得单独标注**：官方把物理密钥的适用性明确绑定到"服务方能否承担找回复杂度"，并把**挑战值的获取责任划给开发者**（系统不代为取得）。UI 侧注册时"the device **displays a sheet asking the user to create a new credential**"。

## 7.3 三种落点的对照（必须并读，见 §1.6 簇二）

| 形态 | 密钥对存放 | 跨设备 | 类定义出处 |
| --- | --- | --- | --- |
| passkey | 用户 iCloud Keychain | **同步** | "the device stores its public-private key pair in the user's iCloud Keychain and syncs the keys across the user's devices" |
| 平台公钥凭据（provider 侧） | iCloud Keychain | 由 provider 访问 | "**The credential provider accesses public-private key pairs stored in iCloud Keychain** for registration or authentication with a relying party."（`ASAuthorizationPlatformPublicKeyCredentialProvider`） |
| 物理安全密钥 | 物理介质（卡 / USB） | **不同步；介质丢失即失效** | "Security keys store the public-private key pair on a physical medium, such as a security card or a USB key." |
| SEP 密钥（对比项，非 passkey） | 安全隔区 | **只在创建设备上**，不可跨存储迁移 | §8.3、§8.5 |

`ASAuthorizationPlatformPublicKeyCredentialProvider` 的标题句本身即口径：**"A mechanism for providing public key credential requests to an app or service with iCloud Keychain."**；其 `relyingPartyIdentifier` 定义为 **"The domain name of the service to register or authorize against."**（A 级）

## 7.4 webcredentials 关联域与 AASA：官方配置口径

`connecting-to-a-service-with-passkeys`（A 级，但 **`role: sampleCode`**、availability 只列 `Xcode: 14.0.0 -` / `iOS: 16.0.0 -` / `iPadOS: 16.0.0 -` / `macCatalyst: 16.0.0 -`，**无 macOS/tvOS/watchOS/visionOS 条目**）：

> "4. **Add the Associated Domains capability** using the '+ Capability' button in the same pane, and **specify your domain with the `webcredentials` service**."
> "5. **Ensure an `apple-app-site-association` (AASA) file is present on your domain in the `.well-known` directory, and that it contains an entry for this app's App ID for the `webcredentials` service.**"
> Note（该页把论述留在 WWDC）："This sample code project is associated with **WWDC22 session 10092: Meet passkeys** and **WWDC21 session 10106: Move beyond passwords**."

**用法限定**：本页只用于给出 `webcredentials` + `.well-known` AASA 这组配置口径；"passkey 保存在 iCloud Keychain"必须引 §7.1。**不要把该页 availability 当作 passkey 的全平台支持清单**（§13.7）。

## 7.5 provider 返回的是签名，不是私钥

`ASPasskeyAssertionCredential`（A 级；availability iOS 17.0.0– / macOS 14.0.0– / visionOS 1.0.0–）属性全集：

> `authenticatorData` / `clientDataHash` / `credentialID` / `relyingParty` / **`signature` — "The cryptographic signature of this credential."** / `userHandle` / `extensionOutput`
> "Create a passkey assertion credential to provide a response to a passkey authentication challenge **from your credential provider extension**."

**属性表里没有私钥字段**，返回值是签名结果；这与 §8.3 的 "You receive only the output of these operations" 是同一设计口径在两个框架里的独立表述（B 级合成，见 §16.2 N7）。

命名口径校正（A 级，404 证据）：**`ASPasskeyCredential` 这个类不存在**，官方分裂为 `ASPasskeyRegistrationCredential`（注册）与 `ASPasskeyAssertionCredential`（断言），另有 `ASPasskeyCredentialIdentity`（标识）；`ASPasskeyCredentialAssertion` 亦为 404（正确词序是 `ASPasskeyAssertionCredential`）。

## 7.6 凭证提供程序扩展：元数据不含密码、容器随卸载移除

手册（A 级，"凭证提供程序扩展"，发布日期 2021 年 2 月 18 日）——**这段是本节最决定性的口径**：

> "用户可以在'密码'设置中的'自动填充密码'（iOS 和 iPadOS）或者'系统设置'（macOS 13 或更高版本）或'系统偏好设置'（macOS 12 或更低版本）中的'扩展'设置内将合作的第三方 App 指定为凭证提供程序。此机制以 App 扩展为基础构建。凭证提供程序扩展需要提供用于选取凭证的视图。扩展可选择提供有关已存凭证的元数据，以便直接在快速输入栏（iOS 和 iPadOS）或自动补全建议 (macOS) 中提供凭证。**元数据包括凭证针对的网站和关联的用户名，但不包括密码。** 用户选择将凭证填充到 App 或 Safari 浏览器网站中时，iOS、iPadOS 和 macOS 会与扩展进行通信以获取密码。**凭证元数据储存在凭证提供程序 App 的容器内，App 卸载时会自动移除。**"

四件事一次说全：**系统侧只拿网站+用户名（明确"不包括密码"）/ 密码本体在扩展被选中时才向 App 索取 / 元数据落在 App 容器 / 容器随卸载移除**。developer 侧的对应机制（A 级）：

> "**Make credential identities available to users directly as AutoFill suggestions by adding them to the `shared` instance of the identity store.**"（`ASCredentialIdentityStore`，标题句 **"A container that your extension fills to provide credentials through the QuickType bar."**）
> `ASPasskeyCredentialIdentity.recordIdentifier` — "**A string used to correlate this identity to a record in your app's own database.**"

**注意口径来源的层级**：手册那句"但不包括密码"是 **A 级直接断言**；而 developer 文档侧只能由"identity 与 credential 的用词二分 + 属性表无秘密字段"推出同一结论。本篇以手册句为主、文档句为辅（§16.2 N1 记录该差异）。

## 7.7 共享库的清除语义：关扩展即清空

> "**When the user disables your extension, the system clears and disables your shared store.** So before making updates, check to see that the store's enabled to avoid unnecessary activity"（A 级）
> "**Be sure to update the shared store whenever your app's database changes to avoid showing stale identities as AutoFill suggestions.** Take advantage of the incremental change methods `saveCredentialIdentities(_:completion:)` and `removeCredentialIdentities(_:completion:)` **to avoid rewriting the entire store** every time you need to make a change."
> "You can also update the shared store from within your extension's **host app**."

三条防线含义：**系统侧存储不具持久保证**（关扩展即清）；**同步责任在 App**（官方要求"数据库一变就更新"）；**host app 也能写**，故一致性由 App 自己负责（`ASCredentialIdentityStoreState` 仅提供 `isEnabled` 与 `supportsIncrementalUpdates` 两个可观测字段）。

## 7.8 扩展的接入要求与真实类名

`ASCredentialProviderViewController`（A 级，**"A view controller that a credential manager app uses to extend AutoFill."**）四步集成：

> "1. **Add a Credential Provider Extension target**… Add the `com.apple.developer.authentication-services.autofill-credential-provider` entitlement **to both the extension and its containing app**. / 2. Override `prepareCredentialList(for:)`… / 3. Optionally add `ASPasswordCredentialIdentity` and `ASPasskeyCredentialIdentity` instances to the shared `ASCredentialIdentityStore`… Then override `provideCredentialWithoutUserInteraction(for:)` **to provide the associated credentials when the person taps a suggestion**. / 4. Optionally override `prepareInterfaceForExtensionConfiguration()` to specify a configuration interface shown **when people first enable your credentials manager in Settings**."

命名口径校正（A 级，404 证据）：**`AICredentialProviderExtension` / `AICredential` 在 developer 文档中不存在**；正确类名是 `ASCredentialProviderViewController` + `ASCredentialProviderExtensionContext`，凭据类是 `ASPasskeyRegistrationCredential` / `ASPasskeyAssertionCredential` / `ASPasswordCredential` / `ASOneTimeCodeCredential`。

保存/生成侧成员（A 级，逐字）：`performWithoutUserInteractionIfPossible(savePasswordRequest:)` — "**Attempt to save a password credential.**"；`performWithoutUserInteraction(generatePasswordsRequest:)` — "**Attempt to generate passwords based on developer-specified rules.**"；`prepareInterface(forPasskeyRegistration:)`；`prepareOneTimeCodeCredentialList(for:)`（"a list of **one-time passcodes (OTPs)**"）。

## 7.9 更新事件：官方要求 provider 改写自己的存储

> "Apps and websites that allow sign-ins can signal updates to the operating system with the `ASCredentialUpdater` class. The various 'report' methods of `ASCredentialUpdater` **work like the 'signal' methods of `PublicKeyCredential` when using WebAuthn on the web**… **Use these calls to update your manager's stored credential data or behavior.**"（A 级）
> `reportAllAcceptedPublicKeyCredentials(forRelyingParty:userHandle:acceptedCredentialIDs:)` — "…a relying party sent **a snapshot of all accepted credentials for an account**."；`reportPublicKeyCredentialUpdate(…)` — "…a relying party indicated that **a passkey's user name updated**."；`reportUnknownPublicKeyCredential(forRelyingParty:credentialID:)` — "…a relying party indicated **a passkey credential is invalid**."；`reportUnusedPasswordCredential(forDomain:userName:)` — "…a relying party **indicatd** that a password credential isn't needed anymore…"（官方原文拼写如此）
> Note：**"This class ignores calls from Mac apps built with Mac Catalyst."**

**最后一句是重要的平台例外**：Mac Catalyst App 的调用被忽略，即该一致性通道在 Catalyst 上不成立（§13.7）。

## 7.10 TOTP 种子的落点：官方把它也交给系统密码管理器

`securing-logins-with-icloud-keychain-verification-codes`（A 级，**"Use time-based codes generated on-device for a secure authentication experience."**）：

> "These codes are often sent over SMS… **Delivering codes over SMS entails a number of risks. For example, SMS-based verification codes can be snooped on carrier networks, or redirected to an attacker through SIM swapping attacks.**"
> "With iCloud Keychain verification codes, **iPhones, iPads, and Macs generate verification codes entirely offline, reducing the risk associated with sending them online. iCloud Keychain synchronizes codes across all of the user's devices.**"
> "Both iOS and macOS handle a special URL based on the industry standard otpauth specification. The Apple-specific version of this URL is identical to the standard, but **uses the `apple-otpauth:` scheme**. Embed this URL in your webpages and apps to allow users to **set up new code generators directly in the iCloud Keychain password manager** with minimal effort."
> "When the user taps on the URL, **the system prompts them to choose a credential. A new verification code is set up for that credential** using the parameters specified in the URL."
> "**The host portion of the URL must be `totp`**, and the path should be the proper name of your service, followed by a colon and the user name or email of the account."

参数口径（A 级，逐字）：`secret` — "An arbitrary key value encoded in Base32. **Secrets should be at least 160 bits.**"；`digits` — "**The value is either 6 or 8. The default is 6.**"；`period` — "**The default value is 30.**"；`issuer` — "**The password manager uses this field to suggest credentials when setting up a new code generator.**"

填充侧（A 级，逐字）：SwiftUI `textContentType(.oneTimeCode)` / UIKit `UITextContentType.oneTimeCode` / AppKit `NSTextContentType.oneTimeCode` / Web **"set the HTML attribute on the input element to `autocomplete=one-time-code`"**；"AutoFill on iOS and macOS **offer to fill verification codes automatically**."

**本节最重要的口径修正**：官方把 **TOTP 种子秘密**与密码、passkey 并列交给 **iCloud Keychain password manager**，App 侧只负责生成 `apple-otpauth:` 链接和标记输入框；**没有任何一句让 App 自行保存 secret**（§16.4 ⑫）。注意这里的同步属 **iCloud Keychain**（用户密码管理器），与 §11.12 的 `kSecAttrSynchronizable`（开发者 keychain 同步）是两条不同的官方通道。

## 7.11 凭据对象的形式约束

`ASAuthorizationCredential`（A 级，**"An interface that all credentials share."**）：

> `protocol ASAuthorizationCredential : NSCopying, NSSecureCoding, NSObjectProtocol`
> 该页**无 Overview 正文**；其 Conforming Types 即凭据全集（AppleID / 密码 / 一次性码 / SSO / passkey 注册与断言 / 平台与安全密钥公钥凭据 / 账户创建平台公钥凭据）。

`ASPasswordCredential`（A 级，**"A password credential."**）：属性只有 `user` 与 `password`（**明文 `String` 直接暴露给 App**），availability iOS 12.0.0– / macOS 10.15.0– / tvOS 15.0.0– / watchOS 6.0.0–。

> 口径要点：**官方未在任何页面建议把 `ASAuthorizationCredential` 存入 keychain**。因此"苹果规定 App 收到 `ASPasswordCredential` 后必须如何保存"这类说法没有文档依据；能引的只有 §6.1 的系统侧口径与 §3/§5 的钥匙串保护机制（§16.2 N2）。

## 7.12 跨密码 App 迁移：OS 中介、一次性 UUID、不落文件系统

`ASCredentialExportManager`（A 级；availability **iOS 26.0.0– / macOS 26.0.0– / visionOS 26.0.0–，无 tvOS / watchOS 条目**）：

> "`ASCredentialExportManager` allows your app to **exchange authentication credentials like passwords and passkeys with other apps**. Participating apps such as password managers can receive your exported credentials by using the `ASCredentialImportManager` class."
> **"Calling this method brings up an out-of-process system UI that guides the person through the export procedure. The system UI explains the risks of exporting credentials, and then allows them to choose an app to export to. The operating system acts as an intermediary to establish the identities of the password manager apps involved and performs the exchange; this process doesn't write any data to the file system."**
> "When a person chooses an importer app, the system launches it, sending an `NSUserActivity` whose `activityType` is `ASCredentialExchangeActivity`. The importing app responds to the launch activity by calling `ASCredentialImportManager.importCredentials(token:)`…"
> 配置键：`NSExtension > NSExtensionAttributes > ASCredentialProviderExtensionCapabilities > SupportsCredentialExchange`（Boolean `YES`）；`SupportedCredentialExchangeVersions`（"**Currently the only available version is `1.0`.**"）

`ASCredentialImportManager`（A 级）：

> "Your app needs to prepare for the system launching the app from this activity by **fetching the `ASCredentialImportToken` from the activity's `userInfo` dictionary. The value of this key is a `UUID` token**…"
> 接入要求：**"add the `NSUserActivityTypes` array to your app's information property list and add the item `ASCredentialExchangeActivityType` to the array."**
> 版本协商：**"`requestExport(for:)` returns an `ExportOptions` structure that describes **the credential data format version you should use to ensure the importer app can successfully decode the data**."**

**"doesn't write any data to the file system" 直接反驳**"iOS 密码 App 导出＝生成一个可保存的明文文件"这一流行说法（§16.4 ⑪）。迁移的可导入范围由 `ASImportableCredentialScope`（"**The scope for where a credential should be usable.**"）等类型描述，属 iOS 26 起的新入口。

## 7.13 升级路径：把密码账户换成强密码或 passkey

`ASAccountAuthenticationModificationController` / `...ViewController`（A 级，归入 AuthenticationServices 的 **"Automatic security upgrades"** 分组）：**"can upgrade user passwords to strong passwords, or convert accounts to use Sign in with Apple"**。手册侧的完整口径（A 级，§6.8 同页）：

> "采用账户认证修改扩展（在'认证服务'框架中）的 App 让你轻点一下按钮，即可将基于密码的账户轻松升级以切换为'通过 Apple 登录'或使用自动强密码。"
> "**'通过 Apple 登录'升级成功后，系统会从用户的钥匙串中删除之前所使用的密码凭证（如果凭证保存在此处）。**"
> "如果用户将账户从使用密码切换为使用'通过 Apple 登录'，系统会为该扩展提供新的'通过 Apple 登录'凭证以与该账户关联。**用户的 Apple 账户电子邮件不会作为凭证的一部分提供。**"
> "账户认证修改扩展可以在执行升级前选择执行额外的用户认证。对于在密码管理器中或登录 App 后发起的升级，扩展会提供账户的用户名和密码进行升级。**对于 App 内升级，则只会提供用户名。** …显示此用户界面的预期用例是让用户输入认证的第二个因素来认证升级。"
> "App 能够告诉系统在用户登录后不显示升级选项。"

三条与本篇强相关的结论：**升级会主动删除旧密码凭证**（数据消失是官方设计的一部分）；**"通过 Apple 登录"路径下邮箱不下发给 App**；**两种发起方式的输入信息不同**（密码管理器内＝用户名+密码，App 内＝仅用户名）。

# 8. 密钥：软件密钥、安全隔区密钥与外部介质密钥

## 8.1 一把 `SecKey` 是不是钥匙串项，官方用类型转换来判定

`security/keys`（A 级，**"Keys — Generate, store, and use cryptographic keys."**）给出的存储侧条目：

> `storing-keys-in-the-keychain` — "**Store and access cryptographic keys in the keychain.**"
> `protecting-keys-with-the-secure-enclave` — "**Create an extra layer of security for your private keys.**"
> `storing-keys-as-data` — "Create an external representation of a key for transmission."
> `getting-an-existing-key` — "Learn how to obtain an existing cryptographic key."

`SecKey` 符号页给出一个非常具体的判定口径（A 级，iOS 2.0.0– / macOS 10.0.0–）：

> "A `SecKey` instance that **represents a key that is stored in a keychain** can be safely cast to a `SecKeychainItem` for manipulation as a keychain item. On the other hand, **if the key is not stored in a keychain**, casting the object to a `SecKeychainItem` and passing it to Keychain Services functions **returns errors**."

**这把"密钥存在哪"变成一个可用类型系统检验的问题**：能否作为 `SecKeychainItem` 使用，即对应"是否落进 §2 的那个数据库"。而 `storing-keys-as-data` 一项说明官方也承认**密钥的不落盘形态**（外部表示用于传输），这正是 §8.3"不可导入既有密钥"的对面。

## 8.2 官方对"保存在钥匙串里"的残余风险评估

`protecting-keys-with-the-secure-enclave` 开篇（A 级，逐字）：

> "**Keeping a private key in a keychain is a great way to secure it. The key data is encrypted on disk and accessible only to your app or the apps you authorize. However, to use the key, you must briefly copy a plain-text version of it into system memory.** While this presents a reasonably small attack surface, there's still the chance that if your app is compromised, the key could also become compromised. As an added layer of protection, you can protect a private key using the Secure Enclave."

这四句是本篇最重要的**风险表述样本**：官方承认 L1 落点的强度来自"加密在盘上 + 只授权给你的 App"，但把**使用时明文短暂进内存**列为残余风险，并以此引出 L2。写报告时若只引前两句会变成"钥匙串足够安全"，只引后两句会变成"钥匙串不安全"——必须整段并读（§1.6 簇二、§16.3）。

## 8.3 安全隔区密钥的四条官方限制

> "The Secure Enclave is a **hardware-based key manager that's isolated from the main processor** to provide an extra layer of security. When you protect a private key with the Secure Enclave, **you never handle the plain-text key**, making it difficult for the key to become compromised. Instead, you instruct the Secure Enclave to create and encode the key, and later to decode and perform operations with it. **You receive only the output of these operations**, such as encrypted data or a cryptographic signature verification outcome."
> 三项限制（A 级，逐字）：
> - "Requires hardware support. **Only iOS devices with an A7 or later processor, or a Mac with the Touch Bar and Touch ID or with an M1 or later processor support this feature.**"
> - "**Works only with NIST P-256 elliptic curve keys.**"
> - "**Can't encode preexisting keys. You must use the Secure Enclave to create the keys. Not having a mechanism to transfer plain-text key data into or out of the Secure Enclave is fundamental to its security.**"

第四条来自 §5.5：`privateKeyUsage` 在非 SEP 生成路径上直接失败。第三条值得反复强调——**"不能导入既有密钥"被官方表述为安全性的根本前提，而不是实现缺陷**；任何"把已有私钥搬进 SEP"的说法都与该句冲突（§17.2）。

## 8.4 "逻辑上属于钥匙串，只有 SEP 能用"

> "Notice that you still receive a reference to the private key object, even though it's created by the Secure Enclave. **The private key is logically part of the keychain**, and you can later obtain a reference to it in the usual way. **But the key data is encoded, and only the Secure Enclave can make use of the key.**"（A 级）
> "This object includes a protection parameter of `kSecAttrAccessibleWhenUnlockedThisDeviceOnly`. As a result, **you can access the associated keychain item only on the device that created it** (a feature that's also inherent to using the Secure Enclave), and only when the device is unlocked."
> "By specifying the `privateKeyUsage` flag, you make the private key available for use in signing and verification operations inside the Secure Enclave. **Without the flag, key generation still succeeds, but signing operations that attempt to use it fail.**"
> "if you include the `biometryAny` flag, you instruct the system to **make the key available only when the system can authenticate the user with Touch ID or Face ID (or a fallback passcode)**."

**第二句里官方把"仅创建设备可用"同时归于 `ThisDeviceOnly`（§3.6）与"使用 SEP 本身固有"**——即两个机制各自都能达到该效果。这也是 §11.14"SEP 密钥不同步"结论的一半依据。

## 8.5 `kSecAttrTokenID`：外部存储，且不可跨存储迁移

A 级（availability iOS 9.0.0– / macOS 10.12.0–）：

> "A key whose value indicates that a cryptographic key is in **an external store.**"
> "The corresponding value is of type `CFString`, and may only be one of the constants specified in Token ID Values. **Presence of this key indicates that the item is backed by an external store, as uniquely identified by the value. An item without this attribute is stored as normal in the keychain database.**"
> Important：**"You can't change this attribute after creating the keychain item. It isn't possible to migrate existing items between stores. Setting `kSecAttrTokenID` when creating a keychain item in macOS makes it behave like an iOS keychain item, as if `kSecAttrSynchronizable` were also set."**
> "Use this attribute only in the **top-level parameter dictionary during key creation** and not in one of the private or public key sub-dictionaries given by `kSecPrivateKeyAttrs` or `kSecPublicKeyAttrs`."

Token ID Values 分组**只列出一个常量**（A 级）：**`kSecAttrTokenIDSecureEnclave` — "Specifies an item should be protected by the device's Secure Enclave."**

**三点必须注意**：① "不可跨存储迁移"是**硬约束**，与 §11.14 的同步结论同源；② Important 里那句"在 macOS 上设置它会使项表现得像 iOS 钥匙串项，**如同也设置了 `kSecAttrSynchronizable`**"是**行为等价的类比**，不等于真的开启同步——引用时不可省后半句；③ 该属性只能出现在密钥生成**顶层**参数字典。

两处官方示例的函数不一致（A 级，已就地标注）：`kSecAttrTokenIDSecureEnclave` 页指向 **"To generate a new key that's protected by the Secure Enclave, call `SecKeyGeneratePair(_:_:_:)`…"**（该函数在 Keys 主题被归入 **Legacy iOS Key Operations**），而 `protecting-keys-with-the-secure-enclave` 一文用的是 **`SecKeyCreateRandomKey(_:_:)`**。本篇并列引用、不判定孰对（§16.1 C8）。

## 8.6 外部令牌的访问组

A 级（`item-attribute-keys-and-values` 的 Access Group Values 分组）：**`kSecAttrAccessGroupToken` — "The access group containing items provided by external tokens."**

即：**由令牌/smart card 提供的项也有一个专属访问组**，与 §4.1 的 app 组体系并列。官方文档中**不存在** token/smart card 的专章教程页（`creating-a-token-and-smart-card-enabled-key-pair` 为 404）；在线口径只有这三个常量 + SEP 一文 + §7.2 的物理安全密钥一文（§16.2 N5）。

## 8.7 三个"性"属性：永久、敏感、可导出

A 级（逐字）：

> `kSecAttrIsPermanent` — "A key with a value that indicates the item's permanence."（默认 `kCFBooleanFalse`；密钥生成场景下决定是否落进钥匙串）
> `kSecAttrIsSensitive` — "A key with a value that indicates the item's sensitivity."
> `kSecAttrIsExtractable` — "A key with a value that indicates whether the item can be exported from its keychain. **Items of class `kSecClassKey` have this attribute.**"

**`kSecAttrIsExtractable` 是"这把密钥能不能被导出"的唯一属性级官方口径**；它与 §8.3 的"不可移出 SEP"、§8.10 的"设备卡号采用 Apple 无法访问的方式加密"属于同一族约束的三个不同层面（属性 / 硬件 / 密钥格式）。

## 8.8 PKCS#12：口令只用于本次解密

`SecPKCS12Import(_:_:_:)`（A 级，**"Returns the identities and certificates in a PKCS #12-formatted blob."**）：

> "Your application can import a PKCS #12–formatted blob (**a file with extension `.p12`**) containing certificates and identities, where **an identity is a digital certificate together with its associated private key**… **You can then use the Keychain Services API (see Keychain services) to put the identities and associated certificates in the keychain.**"
> Return Value："The function returns `errSecSuccess` if there were no errors, `errSecDecode` if the blob can't be read or is malformed, and **`errSecAuthFailed` if the password was not correct or data in the blob was damaged.**"

`keychain-import-and-export-options`（A 级）：**`kSecImportExportPassphrase` — "A passphrase (represented by a `CFStringRef` object) to be used when exporting to or importing from PKCS#12 format."**；`kSecImportExportKeychain` — "A keychain… to be used as **the target when importing or exporting**."；`kSecImportExportAccess` — "An initial access control list represented by a `SecAccess` object."

关键口径：**`SecPKCS12Import` 本身不写钥匙串**（官方措辞是"先取得对象，然后*你可以*用 Keychain Services API 放进去"），因此**口令不会因导入而被持久化**。与之形成措辞差异的是（A 级）：

> `SecItemImport(…)` — "Imports one or more certificates, keys, or identities and **optionally adds them to a keychain**."
> Legacy `SecKeychainItemImport` — "Imports one or more certificates, keys, or identities and **adds them to a keychain**."（无 "optionally"）

**这处 `optionally` 的有无是真实的新旧口径差异**，引用时必须照引（§16.1 C11）。

## 8.9 身份（`SecIdentity`）的存储与一对一约束

`security/identities`（A 级，**"Identities — Combine certificates and cryptographic keys into identities."**）：

> "You can also **use the keychain services API to store the identity to or retrieve it from a keychain, much as you would the certificate or key by itself**."
> `storing-an-identity-in-the-keychain` — "**Securely store an identity in the keychain.**"；正文："**Private keys have a one-to-many relationship with certificates.** That is, a single private key can be paired with multiple certificates, **but a given certificate corresponds to exactly one private key**. As a result, the fields that distinguish one identity from another are the same as those of the certificate it contains." / "Use **`kSecClassIdentity`** instead of `kSecClassCertificate` for the `kSecClass` attribute."

`SecIdentity` 符号页（A 级）："**A `SecIdentity` object contains a `SecKey` object and an associated `SecCertificate` object.**" 另有系统身份一侧：**`SecIdentityCopySystemIdentity(_:_:_:)` — "Obtains the system identity associated with a specified domain."**（归入 System Identities 分组）。

## 8.10 安全元件里的支付与证件密钥（手册口径）

手册"卡片预置安全性概览"（A 级，发布日期 2024 年 12 月 19 日）：

> "**完整的付款卡卡号不会储存在设备或 Apple Pay 服务器上，而是由发卡机构创建唯一的设备卡号并加密发送给 Apple，然后储存在安全元件中。此唯一的设备卡号采用 Apple 无法访问的方式加密。** 设备卡号是独一无二的，并与大部分信用卡或借记卡卡号不同，发卡机构或支付网络可使设备卡号无法在磁条卡、电话或网站上使用。**安全元件中的设备卡号绝不会储存在 Apple Pay 服务器或备份到 iCloud**"
> "配合 Apple Watch 使用的付款卡会进行特别注册并拥有自己的设备卡号，**该卡号储存在 Apple Watch 上的安全元件内**。"

手册"Apple 钱包中证件的安全性"（A 级，发布日期 2024 年 5 月 7 日）：

> "Apple 钱包中的证件认证使用设备签名来防止克隆证件和重放证件出示。**Apple 钱包将证件认证私钥储存在 iPhone 设备的安全元件中，这样证件会绑定到州签发机构创建证件时所针对的同一设备。**"
> "为了减少这些静态元素的可追踪性… **设备会为 Apple 钱包中的每个证件管理多个证件认证密钥。发证机构在签发和更新证件时，会为每个证件认证密钥生成唯一的 MSO 有效负载，这些密钥会在加密后发送到用户设备上并进行储存。** 在出示证件时，设备会选择随机的证件认证密钥及其相关的 MSO 用于交易。"
> "会话加密有助于确保… 所有个人可识别信息 (PII) 都已加密。加密过程由应用层执行。**因此会话加密的安全性不依赖于传输层（例如 NFC、蓝牙和无线局域网）提供的安全性。**"

这三段给出 L2/L3 之外的一种形态：**密钥由第三方（发卡机构 / 签发机构）生成、以加密载荷形式发送到设备、落在安全元件，并与设备绑定**。它是"凭据上传"的反向案例——**上传的是公钥与申请，私钥从未离开设备/签发方**。

## 8.11 管理式设备证明：硬件绑定密钥与可证明属性

手册"针对 Apple 设备的管理式设备证明"（A 级，发布日期 2026 年 1 月 28 日）：

> "管理式设备证明…**使用安全隔区提供有关设备身份及其安全态势的加密保障**。iPad、iPhone、Apple TV 设备需要搭载 A11 仿生或后续型号芯片，Mac 电脑则只有搭载 Apple 芯片才受支持。管理式设备证明有助于防范以下威胁：遭入侵的设备谎报属性 / 遭入侵的设备提供过期证明 / 遭入侵的设备发送其他设备的标识符 / **从异常设备上提取私钥使用** / 攻击者劫持证书请求以骗取证书颁发机构 (CA) 为其签发证书"
> "通过管理式设备证明，设备可请求获得来自 Apple 证明服务器的证明，Apple 证明服务器会返回**由叶证书和中间证书（植根于 Apple 企业证明根证书颁发机构）组成的数据数组**。"

手册"证明过程安全性"（A 级）给出密钥的实际位置与证明内容：

> "操作系统从安全隔区请求**硬件绑定密钥，称为已证明密钥**。对于 ACME 证明，此密钥属于**已颁发证书身份的一部分**… 操作系统从安全隔区请求硬件证明。它指定了已证明密钥、动态刷新码以及待证明属性。"
> "证明服务器验证请求后，要么拒绝颁发证明证书，要么生成**仅包含其可确认属性和值的证明**。证明证书的公钥与已证明密钥匹配。"
> "安全隔区在生成硬件证明时会包括以下及其他元素：**已证明密钥的公钥** / **以下软件组件的哈希值（在启动过程中计算得出并储存在硬件寄存器中，类似于密封密钥保护）：安全隔区固件 (sepOS)。附于 LLB 的 Image4 清单… 操作系统 Image4 清单…**"

**这就是 §1.5 的 U1 方向的官方范本**：上传的是**公钥 + 由 SEP 签名、由 Apple 背书的属性集合**，私钥与"已证明密钥"始终留在 SEP。它与 #83/#85 的信任根、Image4 清单口径直接相接。

## 8.12 密封密钥保护：凭据类密钥的封章

手册"密封密钥保护 (SKP)"（A 级，发布日期 2021 年 5 月 17 日）：

> "在支持数据保护的 Apple 设备上，**密钥加密密钥 (KEK) 既受系统上软件测量值的保护（或密封），又与只能从安全隔区获得的 UID 绑定。**"
> "由用户密码与长期 SKP 密钥和硬件密钥 1（安全隔区的 UID）配合使用而生成的密钥称为**密码派生密钥**。此密钥用于保护**用户密钥包**（在所有支持的平台上）和 KEK（仅限在 macOS 中），然后启用生物识别解锁或使用其他设备（如 Apple Watch）自动解锁。"
> "如果攻击者能够意外地更改任何上述测量的固件、软件或安全性配置组件，则也会修改储存在硬件寄存器中的测量值。测量值的修改会导致从加密硬件派生的**系统测量根密钥 (SMRK)** 派生出不同的值，从而**有效破坏密钥层级的封章**。这将导致无法访问系统测量设备密钥 (SMDK)，继而导致无法访问 KEK，也无法访问数据。"

**§2.4 的"用户密钥包"在这里被补全**：它由密码派生密钥解封，而密码派生密钥又受 SKP 封章约束。本篇只引用其对**凭据可用性**的后果（测量值变 → 类密钥不可得 → 凭据不可读），机制细节归 #82/#89。

# 9. 证书与信任：放进钥匙串与被信任是两件事

## 9.1 证书的官方定义与它的存储条目

`security/certificates`（A 级，**"Certificates — Manage digital certificates."**）：

> "A digital certificate is a collection of data used to securely distribute **the public half of a public/private key pair**… Along with structural information, the certificate contains name and contact information for both its issuer and its owner (or subject), plus the owner's public key. A date range indicates when the certificate is valid… When assembling the certificate, to vouch for its integrity, **the issuer digitally signs it using the issuer's own identity (private key and certificate).**"
> "To evaluate a certificate, you first verify its signature using the specified algorithm and the issuer's public key… A valid signature confirms that the certificate under evaluation, known as **the leaf certificate**, is unaltered. But in order to trust this result, you must also trust the issuer's certificate… and so on **in a chain back to a trusted root authority whose certificate, known as the anchor, which you trust implicitly.**"

存储侧条目（A 级，逐字）：`storing-a-certificate-in-the-keychain` — **"Store a certificate in the keychain for safekeeping."**；`getting-a-certificate` — "Obtain a certificate from an identity, from DER-encoded data, **or from the keychain**."；`storing-a-der-encoded-x-509-certificate` — "Import and export a certificate from a file."；Legacy `SecCertificateAddToKeychain(_:_:)` — "Adds a certificate to a keychain."（官方归入 **Legacy Symbols**）。

> "Certificates and identities… **You can also package a certificate together with its corresponding private key in an identity object that you keep secret.**"（A 级，`certificate-key-and-trust-services`）

## 9.2 三类凭据的官方切分

`certificate-key-and-trust-services`（A 级，**"Establish trust using certificates and cryptographic keys."**）把该主题切成三块：

> - "**Certificates and identities.** A certificate is a collection of data that identifies its owner in a tamper-evident way…"
> - "**Policies and trust services.** When you receive a certificate, before you can use the embedded public key, you have to answer the question, 'Can I trust this certificate?' You conduct **an evaluation of trust according to a set of criteria, or a trust policy**."
> - "**Cryptographic keys.** After you have a key whose origin you trust, you can begin to conduct cryptographic operations…"

官方给出的五分量 Topics 即 **certificates / keys / identities / policies / trust**，另有 `working-with-concurrency`（"Learn about thread safety issues related to the certificate, key, and trust services API."）。**顺序本身是逻辑链**：先有证书与身份，再有策略与信任评估，最后才是用密钥做运算——本篇 §15.1 的判定链按此排列。

## 9.3 策略：X509 / SSL / 撤销，以及一处反直觉参数

`security/policies`（A 级，**"Obtain policies for establishing trust."**）：

> "For a certificate that is deemed intact and valid (**because the chain of signatures is unbroken back to a trusted root certificate**), you evaluate it against a set of rules known as a **trust policy**. The policy indicates how particular fields or extensions of a certificate affect whether it should be trusted for a particular use. For example, the policy may state that **a certificate must not be expired or must be marked as valid for encryption, code signing, or some other specific purpose.**"
> "Usually you use a standard, predefined policy, such as **the basic X509 policy or the SSL policy.**"

`SecPolicyCreateSSL(_:_:)`（A 级；iOS 2.0.0– / macOS 10.6.0– / macCatalyst 13.1–）：

> 标题："Returns a policy object for **evaluating SSL certificate chains**."
> `server` 参数：**"Specify `true` on the client side to return a policy for SSL server certificates."**
> `hostname` 参数："If you specify a value for this parameter, **the policy will require the specified value to match the host name in the leaf certificate.**"

**纠偏**：`server: true` 不是"本机当服务器"，而是"被评估的对象是 SSL **服务器**证书"，且官方明写"**在客户端**指定 true"。另：三参数重载**不存在**（`secpolicycreatessl(_:_:_:)` 为 404）（§16.4 ①）。

撤销策略侧：`SecPolicyCreateRevocation(_:)` + `revocation-policy-constants`（A 级，Topics 逐字列出）；专用策略侧：`standard-policies-for-specific-certificate-types` — "Use special OIDs to cause a certificate to be evaluated based on security policies specific to a given type of certificate."

## 9.4 信任对象的构造与评估

`security/trust`（A 级，**"Trust — Evaluate trust based on a given policy."**）：

> "Before using a certificate, you evaluate its trustworthiness for a particular purpose. If you know that a certificate comes unaltered from its sender, you can be confident that its embedded public key does as well."
> `creating-a-trust-object` — "**Construct a trust object from a certificate and a policy.**"；`SecTrustCreateWithCertificates(_:_:_:)` — "Creates a trust management object based on certificates and policies."
> `discovering-why-a-trust-evaluation-failed` — "**Determine whether you can recover from a failed trust evaluation.**"；`SecTrustResultType` — "Trust evaluation result codes."
> `configuring-a-trust` — "**Work around a recoverable trust failure.**"（归入官方分组 **"Advanced Trust Configuation"**，官方拼写如此）

`SecTrustEvaluateWithError(_:_:)`（A 级；iOS 12.0.0– / macOS 10.14.0–）：

> "The method **validates a certificate by verifying its signature plus the signatures of the certificates in its certificate chain, up to the anchor certificate, according to the policy or policies included in the trust management object.**"
> "If the trust management instance lacks some of the certificates needed to verify the leaf certificate, `SecTrustEvaluateWithError(_:_:)` searches for certificates: **In the user's keychain. / Among any certificates you previously provided by calling `SecTrustSetAnchorCertificates(_:_:)`. / In a system-provided set of keychains provided for this purpose. / Over the network, if certain extensions are present in the certificate used to build the chain.**"
> "**Don't call `SecTrustEvaluateWithError(_:_:)` from your app's main run loop because it might require network access to fetch intermediate certificates, or to perform revocation checking.** To perform evaluation asynchronously, use `SecTrustEvaluateAsyncWithError(_:_:_:)` instead."
> "In the case of multiple certificate failures, the error contains a code from Security Framework Result Codes **representing the most serious**."

Legacy 侧 `SecTrustSetKeychains(_:_:)` — "Sets the **keychains searched for intermediate certificates** when evaluating a trust management object."（A 级）——**与 §9.5 的"不搜锚点"合起来，才构成"钥匙串在信任评估里只扮演中间证书来源"这一完整口径**。

## 9.5 锚点：唯一一句"不搜钥匙串"

> Note（A 级，逐字，本节核心）：**"Although this method searches the keychain for intermediate certificates, it does not search the keychain for anchor (root) certificates. To add an anchor certificate, you must call `SecTrustSetAnchorCertificates(_:_:)`."**

配套三条（A 级，逐字）：

> `SecTrustCopyAnchorCertificates(_:)` — "**Retrieves the anchor (root) certificates stored by macOS.**"
> `SecTrustSetAnchorCertificatesOnly(_:_:)` — "**Reenables trusting built-in anchor certificates.**"（即存在"内建锚点集合"）
> `SecTrustSetNetworkFetchAllowed(_:_:)` — "Specifies whether a trust evaluation is permitted to **fetch missing intermediate certificates from the network**."

**结论（A 级直接支持）**：在 iOS/macOS 的信任评估里，"把根证书装进钥匙串"不产生锚点效果；锚点要么由代码显式设置，要么来自系统内建集合。这是 §16.4 ⑦，也是本主题最常被写错的一条。

## 9.6 Trust Settings：macOS 侧唯一公开的可改信任设置

A 级（`security/trust` 的 Trust Settings 分组）：`SecTrustSettingsCopyCertificates(_:_:)`、`SecTrustSettingsCopyTrustSettings(_:_:_:)`、`SecTrustSettingsDomain`（"The trust settings domains."）、`SecTrustSettingsResult`、`kSecTrustSettingsDefaultRootCertSetting` — "A value indicating **the default root certificate trust settings**…"、`usage-constraints-dictionary-keys`。

**平台限定**：该组页面归属 macOS，未在 iOS 列出（§13.6）。同族 `SecTrustSetExceptions(_:)` — "Returns an opaque cookie containing **exceptions to trust policies** that will allow future evaluations of the current certificate to succeed."（A 级）——即"一次放行、后续沿用"的机制确实存在，且其内容对调用方是**不透明 cookie**（可观测性有限，见 §15.2）。

## 9.7 developer 文档没有根证书清单页

A 级（404 与缺席记录）：developer.apple.com **没有** "Apple Root Certification Authorities" 文档页（该清单位于 www.apple.com 的 certificateauthority 门户）；developer 侧可用的锚点口径只有 §9.5 两句。`performing-manual-server-trust-authentication` 使用了"**the system's trust store**"这一措辞，但**没有专门解释系统信任 store 组成/更新机制的页面**（§16.2 N8）。

系统侧的撤销与透明度更新口径来自手册"TLS 安全性"（A 级，发布日期 2026 年 1 月 28 日）：

> "对 TLS 证书信任状态的评估依照既定的行业标准（如 RFC 5280 所列）执行，且整合了新出现的标准，如 RFC 6962（证书透明度）。**在运行 iOS 11 或更高版本以及 macOS 10.13 或更高版本的设备上，Apple 设备会定期更新当前已撤销和受限证书的列表。该列表由 Apple 信任的每个内建根证书颁发机构及其从属的 CA 签发者所发布的证书撤销清单 (CRL) 聚合而成**，列表还可能根据 Apple 的要求酌情包括其他限制。每当使用网络 API 函数进行安全连接时都会使用此信息。如果需要单独列出的 CA 撤销证书太多，信任评估可能需要在线证书状态响应 (OCSP)，否则将无法通过信任评估。"
> "在运行 iOS 12.1 的设备上，2018 年 10 月 15 日之后由系统信任根证书签发的证书**必须在可信任证书透明度日志中记录**才可进行 TLS 连接。"

同页另有下限口径（A 级）：**"除非用户信任，否则不再允许将 SHA-1 证书用于 TLS 连接。也不允许使用 RSA 密钥短于 2048 位的证书。"** 以及 **"在运行 iOS 26、iPadOS 26 或更高版本的设备上，针对 URLSession 框架和 Network API 默认启用具有量子安全加密的 TLS 1.3（采用 X25519MLKEM768 密钥交换算法）。"**

## 9.8 ATS：只能收紧，不能放松

`performing-manual-server-trust-authentication`（A 级，**"Performing manual server trust authentication — Evaluate the server's security credentials in your app."**）：

> "When you use a secure connection (such as `https`) with a URL request, your `URLSessionDelegate` receives an authentication challenge with an authentication type of `NSURLAuthenticationMethodServerTrust`. **Unlike other challenges where the server is asking your app to authenticate itself, this is an opportunity for you to authenticate the server's credentials.**"
> "In most cases, **you should let the URL Loading System's default handling evaluate the server trust.**"
> 两个适用场景（A 级，逐字）："You want to **accept** server credentials that would otherwise be **rejected** by the system. For example… a self-signed certificate, which would ordinarily not match anything in **the system's trust store**." / "You want to **reject** credentials that would otherwise be **accepted** by the system. For example, **you want to "pin" your app to a set of specific keys or certificates under your control, rather than accept any valid credential.**"
> ATS 限定（A 级，逐字）：**"You cannot loosen server trust requirements for an ATS-protected domain, but you can tighten them, using the manual evaluation technique shown in this article."**

手册侧 ATS 的硬要求（A 级）：

> "服务器必须支持 TLS 1.2 和正向保密，且证书必须有效并使用 SHA-256 或更强的加密算法签名，以及包含至少 2048 位 RSA 密钥或 256 位椭圆曲线密钥。**不满足这些要求的网络连接会失败，除非 App 重写了 App 传输安全性。无效的证书始终导致硬故障和无连接。**"

**"无效的证书始终导致硬故障"**与"可以 accept 被系统拒绝的凭据"并不矛盾：前者描述 ATS 默认路径，后者描述手动评估场景；但 ATS 域名上的"放松"被上一句明确否决。§16.4 ⑧ 记录这对常被误读的关系。

## 9.9 pinning 不是官方命名机制

A 级（404 记录）：**`/documentation/security/certificate-pinning` 不存在**；"certificate pinning" 在 developer 文档中**没有独立页面**，唯一在线表述是 §9.8 中带引号的 "pin"（作为"手动评估的适用场景之一"出现）。

官方给出的三条长期策略（A 级，逐字，本节最可操作）：

> - "**Compare the server's credentials against a public key, instead of storing a single certificate in your app bundle.** This will allow you to reissue a certificate for the same key and update the server, rather than needing to update the app."
> - "**Compare the issuing certificate authority's (CA's) keys, rather than using the leaf key.** This way, you can deploy certificates containing new keys signed by the same CA."
> - "**Use a set of keys or CAs, so you can rotate server credentials more gracefully.**"

**这三句是"上传/分发凭据到 App 内"这一做法的官方替代建议**：不要内置单张叶子证书，优先比对公钥、比对 CA 密钥、并使用集合。

## 9.10 认证挑战处理的粒度与凭据持久化

`handling-an-authentication-challenge`（A 级）：

> "Implement the `urlSession(_:didReceive:completionHandler:)` method of `URLSessionDelegate` to handle **session-wide** challenges. These are challenges like **Transport Layer Security (TLS) validation**. Once you've successfully handled this kind of challenge, **your action remains in effect for all tasks created from that `URLSession`**."
> "…`urlSession(_:task:didReceive:completionHandler:)` method of `URLSessionTaskDelegate` to handle **task-specific** challenges. These are challenges like demands for username/password authentication."
> "In this example, the returned `URLCredential` has **`URLCredential.Persistence.forSession` persistence, so it's only stored by the `URLSession` instance that created the task.** You would need to supply new `URLCredential` instances for tasks created by other session instances, **and on future runs of the app.**"
> "**eventually you must call the completion handler** to complete the challenge and allow the task to proceed, even if you're choosing to cancel."

`url-loading-system` 的 Authentication and credentials 分组（A 级）：`URLCredential` — "An authentication credential consisting of information specific to the type of credential **and the type of persistent storage to use, if any**."；**`URLCredentialStorage` — "The manager of a shared credentials cache."**；`URLProtectionSpace` — "A server or an area on a server, commonly referred to as a **realm**, that requires authentication."

**口径**：URL Loading System 侧的凭据缓存是**会话级共享缓存**，`forSession` 明确不跨启动；官方把"用哪种持久存储"写进 `URLCredential` 的定义句里，却**没有说它落在哪个受保护落点**（§16.2 N9）。这条与 §2 的钥匙串是两个体系，不应混称"URLSession 的凭据存在钥匙串"。

## 9.11 服务器证书在钥匙串里以什么形态存在

`keychains.md`（A 级，macOS 侧 **keychains** 分组）：iOS 只有一个钥匙串，且：

> "This keychain is **automatically unlocked when the user unlocks the device and then locked when the device is locked.**"
> 定性："…which **logically encompasses the iCloud keychain**"

结合 §3.7 表中"VPN 证书（首次解锁后，不可迁移）"与"iCloud 证书和私钥（始终，不可迁移）"两行，可以得到：**证书类项在系统钥匙串里按用途被分到不同可访问性档，其中长期通道类证书位于"始终"档**。这一组合是 §15.4 检查表的一项。

## 9.12 代码信任与凭据信任的分界

手册"信任缓存"（A 级，发布日期 2021 年 2 月 18 日）：

> "**静态信任缓存是安全启动链中包括的一个对象，它是所有装入签名系统宗卷的 Mach-O 二进制文件的受信任记录。每个 Mach-O 由一个代码目录哈希值表示。**"
> "**非平台二进制文件（例如已公证的第三方代码）必须拥有有效的证书链才能执行，它们可拥有的授权受 Apple 开发者计划向开发者签发的签名描述文件限制。**"

**同一份"证书"在两套体系里的角色完全不同**：在 TLS 体系里它是**待评估对象**（§9.4）；在代码签名体系里它是**授权介质**（签名身份 + 描述文件）。本篇第 9 章只覆盖前者，后者归 #84/#85；混用两套口径（例如用钥匙串里的证书解释 SIP 或信任缓存）是错误来源（§16.1 C12）。

# 10. 令牌：四种 "token" 的官方口径互不通用

## 10.1 先把四个词分开

| 词 | 官方语境 | 谁保管 | 是否上传 |
| --- | --- | --- | --- |
| token（`kSecAttrTokenID`） | **外部存储/硬件令牌**（§8.5） | 令牌本身 / SEP | 不上传；项在钥匙串中只是引用 |
| device token（APNs） | 推送寻址句柄 | **官方要求 App 不本地缓存** | 必须上传服务端 |
| token（DeviceCheck `generateToken`） | **ephemeral + single-use** 的设备证明 | 不保管，即用即弃 | 一次性上传服务端 |
| token（`ASCredentialImportToken`） | 迁移交接用的 **UUID** | 系统中介 | 只在两个 App 之间一次性交接 |

此外，`Security/password-autofill` 与 §3.7 表中的"iCloud 令牌""社交网络账户令牌""'查找'令牌"是**日常意义的访问令牌**，官方把它们当作普通钥匙串项对待。**"令牌"一词在本主题里至少指这四类互不通用的东西**，任何跨类引用都会产生错误结论（§16.1 C13）。

## 10.2 访问令牌 / 会话令牌的官方口径：作为 generic password 存入

developer 文档**没有**"令牌存储"专页。可用口径由两条合成（B 级）：

> "Keychain services also offers the related `kSecClassGenericPassword` item class. Generic passwords are similar in most respects to Internet passwords, **but they lack certain attributes specific to remote access** (for example, they don't have a `kSecAttrServer` attribute). **When you don't need these extra attributes, use a generic password instead.**"（A 级）
> "You use the `kSecClass` key with a suitable value to tell keychain services whether the data you want to store represents **a password, a certificate, a cryptographic key, or something else**."（A 级，`SecItemAdd`）
> "**Keychain services takes care of encrypting this data if the item is secret**, namely when it's one of the password types or involves a private key."（A 级）

即：**令牌按官方口径应作为 `kSecClassGenericPassword` 项存入，因而自动进入 §2.3 的"行独有密钥"加密路径**；手册 §2.1 的"如密钥和登录令牌"与 §3.7 的三行令牌实例（iCloud 令牌 / 社交网络账户令牌 / "查找"令牌）从系统侧印证这一归类。

`SecItemAdd` 的类别清单（A 级，`item-attribute-keys-and-values` Note）：**"Not all attributes apply to every item class. You can find the list of attributes applicable to a given class in the relevant item class value definition, namely in `kSecClassGenericPassword`, `kSecClassInternetPassword`, `kSecClassCertificate`, `kSecClassIdentity`, or `kSecClassKey`."**

**推论（B 级）**：令牌类项能用的属性集合比证书/密钥类窄（例如 `kSecAttrIsExtractable` 明确只属 `kSecClassKey`，§8.7），因此"给访问令牌设置可导出性"这类说法在文档上没有落点。

## 10.3 APNs 设备令牌：官方明令不要本地缓存

`application(_:didRegisterForRemoteNotificationsWithDeviceToken:)`（A 级）：

> `deviceToken` 参数：**"A globally unique token that identifies this device to APNs. Send this token to the server that you use to generate remote notifications. Your server must pass this token unmodified back to APNs when sending those remote notifications." / "APNs device tokens are of variable length. Do not hard-code their size."**
> Discussion：**"Never cache the device token locally on the user's device. Device tokens can change periodically, so caching the value risks sending an invalid token to your server.** If the device token hasn't changed, registering with APNs and returning the token happens quickly."
> "UIKit calls this method when the user launches an app after having restored a device from data that is not the device's backup data. In this exceptional case, **the app won't know the new device's token until the user launches it.**"

`registerForRemoteNotifications()`（A 级）："…**passes it a device token. You should pass this token along to the server you use to generate remote notifications.**"

三条可核对口径：**变长、不可硬编码长度、不要本地缓存**；恢复备份后的例外情形官方也点明（App 要等用户启动才拿到新 token）。

**与 §3.7 的关系必须并读**：系统自己把"APNs 令牌"放在钥匙串"始终、不可迁移"档（服务通道需要），而**对 App 的建议是不要保存它**。这两句主语不同，不构成矛盾，但把任一句当作另一句的证据就会产生错误结论（§16.1 C5、§16.4 ③）。

## 10.4 DeviceCheck：短时效、一次性、对开发者不可读

`DCDevice`（A 级，**"A representation of a device that provides a unique, authenticated token."**）与 `generateToken(completionHandler:)`：

> `token` 参数：**"An ephemeral token that identifies the current device."**
> "**You should treat the token you receive in the completion block as single-use. Although the token remains valid long enough for your server to retry a specific request if necessary, you should not use a token multiple times.** Instead, use this method to generate a new token."
> Note：**"The app you use to generate the token must be associated with your developer account; otherwise, the generation request fails."**
> "Apple records the bits for you, and reports the bits back to you, but **you're responsible for keeping track of what the bits mean.**"

服务端字段口径（A 级，`accessing-and-modifying-per-device-data`）：

> 表格逐字：`device_token` | String (must not contain line breaks) | **"Base 64–encoded representation of encrypted device information"** | Required: Yes
> "**The authentication key must use the ES256 algorithm and be in the Base 64 URL–encoded JSON web token format.** If your token doesn't use this format, you receive a `BAD_AUTHENTICATION_TOKEN` HTTP error."
> 端点：`/v1/query_two_bits`、`/v1/update_two_bits`、`/v1/validate_device_token`；错误码 `400 Bad Device Token`（"The device token is missing or badly formatted"）、`401 Invalid Authorization Token`、`401 Authorization Token Expired`、`429 Too Many Requests`
> Important：开发基址 `https://api.development.devicecheck.apple.com` 仅用于测试；生产必须用 `https://api.devicecheck.apple.com`

框架定性（A 级，**"DeviceCheck — Reduce fraudulent use of your services by managing device state and asserting app integrity."**）与能力边界句：

> "**No single policy can eliminate all fraud. For example, App Attest can't definitively pinpoint a device with a compromised operating system.** Instead, the DeviceCheck services provide information that you can integrate into **an overall risk assessment** for a given device."

**保管结论（A 级支持）**：DeviceCheck 令牌是**"生成—立即使用—丢弃"**的对象，官方既要求一次性，又说明它是加密后的信息（开发者侧不可读）。把它当稳定设备标识缓存，同时违背两句官方口径（§16.4 ②）。

## 10.5 App Attest：私钥在 SEP，keyId 由 App 自己持久化

`DCAppAttestService`（A 级，**"A service that you use to validate the instance of your app running on a device."**）三步：

> - "**Create a cryptographic key in the Secure Enclave** by calling the `generateKey(completionHandler:)` method.
> - **Ask Apple to certify the key** by calling the `attestKey(_:clientDataHash:completionHandler:)` method.
> - **Prepare an assertion of your app's integrity** to accompany any or all server requests using the `generateAssertion(_:clientDataHash:completionHandler:)` method."

`generateKey` 的 `keyId` 定义（A 级）：**"An identifier that you use to refer to the key. The framework securely stores the key in the Secure Enclave."**

整段最关键的持久化口径（A 级，逐字）：

> "**Record the identifier in persistent storage — for example, by writing it to a file — because there's no way to use the key without the identifier, and no way to get the identifier later. The device automatically stores the associated private key in the Secure Enclave, from where the App Attest service can use it to create signatures, but from where no process can ever directly read or modify it, ensuring its security.**"
> "**be sure to persistently store the key identifier — not the attestation object — in your app for future use in signing server requests.**"
> App Clip 场景 Important：**"If you create a key pair in an App Clip, use the same key pair in the corresponding app. To support this, be sure to store the identifier in a shared container accessible from your full app."**

**三点纠偏（§16.4 ⑤）**：① 官方给的持久化示例是**写文件/共享容器**，developer 文档**没有一处要求把 keyId 放进钥匙串**；② 官方明令**不要**保存 attestation 对象；③ 秘密不可读、标识符需自存，是这一族凭据的**二分设计**。

## 10.6 App Attest 的生命周期与使用纪律

> 生命周期（A 级，逐字）："The keys that you generate **remain valid through regular app updates, but don't survive app reinstallation, device migration, or restoration of a device from a backup.** In these cases, you need to start the process from the beginning and generate a new key. **Try to limit new key generation to only these events, or to the addition of new users. Keeping the key count low on a device helps when trying to detect certain kinds of fraud.**"
> 每账户一把（A 级）："Create a **unique key for each user account on a device**. Otherwise it's hard to detect an attack that uses a single compromised device to serve multiple remote users running a compromised version of your app." / "**Don't reuse a key among multiple users on a device because this weakens security protections.**"
> 为何校验必须在服务端（A 级）："You **can't rely on your app's logic to perform security checks on itself because a compromised app can falsify the results**. Instead, you use the `shared` instance of `DCAppAttestService` in your app to create a **hardware-based, cryptographic key** that uses Apple servers to certify that the key belongs to a valid app instance."
> challenge 下限（A 级）："**attestation embeds the hash of a unique, one-time challenge from your server.** You can create a suitable value using the `SHA256` implementation in CryptoKit. **The challenge should be at least 16 bytes in length to ensure sufficient entropy to ensure guessing them is infeasible.**"
> assertion（A 级）："There's **no restriction on the number of assertions that you can make with a key.** Nevertheless, you typically reserve assertions for requests made at sensitive moments…" / "…signing the request… **to avoid replay attacks**"
> 扩展支持 Important（A 级）：**"Action, extensible SSO, and watchOS extensions are supported. All other extension types are not supported, even if `isSupported` is true."**
> 错误恢复（A 级）：`serverUnavailable` → "try attestation again later **with the same key**. For any other error, **discard the key identifier and create a new key**." / "Retrying with the same inputs **helps to preserve the risk metric for a given device**."

**生命周期一句直接决定 §12.8 的结论**：App Attest 密钥**不随备份恢复而存活**，与 passkey（随钥匙串同步/恢复）行为相反。这是"哪些凭据能跨设备迁移"这一问题最干净的对照样本。

## 10.7 SSO 票证与设备管理服务推送

手册"单点登录安全性"（A 级，发布日期 2026 年 1 月 28 日）：

> "iOS、iPadOS、macOS 和 visionOS 支持通过单点登录 (SSO) 对企业网络进行认证。SSO 与身份提供商 (IdP) 配合…"
> "iOS、iPadOS、macOS 和 visionOS 支持使用 Kerberos 对企业网络进行认证。SSO 支持基于开源 Heimdal 项目，并使用 SPNEGO 令牌和 HTTP Negotiate 协议… **同时还支持基于证书的认证，如 PKINIT。**"
> Kerberos SSO 扩展支持的加密类型："AES-128-CTS-HMAC-SHA1-96" / "AES-256-CTS-HMAC-SHA1-96"
> "**为了配置 SSO，有一个配置可允许设备管理服务将必要设置推送给设备。** 这包括设置用户主体名称（Active Directory 用户账户）和 Kerberos 领域设置，以及配置应允许哪些 App 和 Safari 浏览器网站 URL 使用 SSO。"
> "开发者可提供两类扩展：**重定向扩展，可重定向至 HTTPS** / **质询/响应扩展，如 Kerberos**… 这使可扩展单点登录支持 Open ID Connect (OIDC)、OAuth、SAML 2.0 和 Kerberos 认证方案。SSO 扩展还可能通过采用平台 SSO 来支持 macOS 上文件保险箱和登录窗口中的认证，**这可让 Mac 在登录过程中获取 SSO 令牌**。"

**本节口径**：票证类凭据（Kerberos 票、SSO 令牌）由**系统 SSO 扩展**管理，developer 侧的钥匙串口径不直接适用；组织侧的控制点是**设备管理服务推送配置**（§14.4），而非 App 权限。

## 10.8 网页认证会话：令牌经回调 URL 交给调用方 App

`ASWebAuthenticationSession`（A 级，**"A session that an app uses to authenticate a user through a web service."**）：

> "**When the user starts the authentication session, the operating system shows a modal view telling them which domain the app is authenticating with and asking whether to proceed.** If the user proceeds… **a browser loads and displays the page, from which the user can authenticate. In iOS, the browser is a secure, embedded web view. In macOS, the system opens the user's default browser** if it supports web authentication sessions, or Safari otherwise."
> **"On completion, the service sends a callback URL to the session with an authentication token. The session passes this URL back to the app through a completion handler. `ASWebAuthenticationSession` ensures that only the calling app's session receives the authentication callback, even when more than one app registers the same callback URL scheme."**

cookie / 浏览数据边界（A 级，逐字）：

> "This setting **asks the browser to avoid using any existing browsing data, like cookies, during the authentication process. It also asks that the browser avoid retaining any data collected during the authentication attempt beyond the lifetime of the attempt, or sharing it with any other session.** An ephemeral session may improve security but **prevents reusing the result of a previously successful authentication**… **it's typically best to let the user choose whether to request ephemeral browsing.**"
> "**When not using an ephemeral session, all cookies except session cookies are available to the browser.**"
> "**Safari always respects the request. In macOS, the user can choose a different default browser that might or might not respect the request.**"
> `prefersEphemeralWebBrowserSession`：**"The value of this property is `false` by default." / "Set this property before you call `start()`. Otherwise it has no effect."**

**必须照引、不宜加码**（§16.4 ⑪）：官方措辞全部是"**asks the browser**"，**没有任何一句"该会话不保存凭据/cookie"的绝对断言**。令牌侧的官方保证只有一句：**只有调用方的会话能收到回调**。旧式 `init(url:callbackURLScheme:completionHandler:)` 已被列入 **Deprecated symbols**，新写法是 `init(url:callback:completionHandler:)`（用 `Callback` 对象判定跳转）。

## 10.9 "令牌要不要上传"的官方答案分形

| 令牌 | 官方是否要求上传 | 原句锚点 |
| --- | --- | --- |
| APNs 设备令牌 | **要求上传**，且要求不本地缓存 | "Send this token to the server…" / "Never cache the device token locally…" |
| DeviceCheck 令牌 | **要求上传**（一次性），服务端换回两个 bit | "…your server uses HTTP `POST` commands to send requests…" |
| App Attest keyId | **要求上传公钥/attestation**，keyId 本机持久化，attestation 对象本机**不要**存 | §10.5 |
| passkey | **只上传公钥**，私钥留设备/iCloud 钥匙串 | "sends the public key to the server. The server… holds the public key" |
| 访问令牌/会话令牌 | 官方无上传口径；仅规定"存则作为 generic password 项" | §10.2 |
| Kerberos/SSO 票证 | 由系统扩展与 IdP 协商，developer 无保管口径 | §10.7 |

**由此得到本篇对"上传"最有用的一条判定**：官方把"可上传"的边界统一划在**公钥/一次性证明/寻址句柄**这一侧，把**私钥、密码本体、TOTP 种子**留在设备侧或端对端同步域。任何把后者设计为上传对象的做法，都没有 A 级依据（§17.2）。

# 11. iCloud 钥匙串：跨设备"上传"的信任圈与恢复机制

## 11.1 两项服务与三个威胁前提

手册"iCloud 钥匙串安全性概览"（A 级，发布日期 2024 年 12 月 19 日）：

> "iCloud 钥匙串可让用户在 iPad、iPhone、Mac、Apple Watch 和 Apple Vision Pro 之间安全地同步密码，**而不会将其泄露给 Apple**。除了强大的隐私保护和安全性，iCloud 钥匙串设计和架构的其他目标还包括易于使用，**且即使用户的所有设备都无法访问，也能恢复钥匙串内容**。iCloud 钥匙串包含两项服务：**钥匙串同步**和**钥匙串恢复**。"
> "iCloud 钥匙串和钥匙串恢复的设计可使用户的密码在下列情况下仍然受到保护：**用户的 iCloud 账户被盗。/ iCloud 遭到外部攻击者或员工的入侵。/ 第三方访问用户账户。**"
> "生成的密码会储存在钥匙串中并同步到其他设备。**钥匙串项通过 Apple 服务器在不同设备之间传输，但会进行端对端加密，使 Apple 和其他设备无法读取其内容。**"

**最后一句是"上传到 iCloud"的准确表述**：服务器承担传输，不承担可读性。§11.9 的托管与 §11.10 的高级数据保护都是这一原则的延伸。

## 11.2 同步身份：P-384 椭圆密钥，存在设备钥匙串里

手册"钥匙串安全同步"（A 级，发布日期 2024 年 5 月 7 日）：

> "当用户首次在双重认证账户上打开 iCloud 钥匙串时，**设备会为自己创立同步身份。同步身份包括非对称椭圆密钥（使用 P-384），且储存在设备的钥匙串中**。每台设备均维护包含用户其他设备同步身份的本机列表，**且使用其中一个身份密钥为此列表签名**。这些列表储存在 **CloudKit** 中，以让用户的设备在相互之间安全同步钥匙串数据上达成一致。"

## 11.3 信任圈的双重签名与来自 iCloud 密码的 P-256 密钥

> "为了兼容较旧的 iCloud 设备，设备将创建类似的同步信任圈并建立其他同步身份。**同步身份的公钥放置在信任圈中，该信任圈已签名两次：第一次由同步身份的私钥签署，第二次由来自用户 iCloud 账户密码的非对称椭圆密钥（使用 P-256）签署。随信任圈一起储存的还有参数（随机盐和迭代次数），用于创建基于用户 iCloud 密码的密钥。**"
> "对于双重认证的账户，每台设备的受信任设备列表会储存在 CloudKit 中。**如果不知道用户的 iCloud 密码，就无法读取列表；如果没有已拥有设备的私钥，就无法修改私钥。**"
> "同样，签名的同步信任圈储存在用户的 **iCloud 键值存储区域**，如果不知道用户的 iCloud 密码，就无法对其进行读取，而且如果没有信任圈成员同步身份的私钥，就无法对其进行有效地修改。"

**两条读法**：① 信任圈的强度来自**两类密钥的合签**（设备侧 P-384 + 口令派生 P-256），因此"设备被盗"或"口令被盗"单独都不足以伪造信任圈；② CloudKit 与键值存储**分别承载列表与信任圈**，这是官方少见地说明 iCloud 钥匙串与 CloudKit 的分工（§11.13）。

## 11.4 新设备如何加入：配对担保或恢复

> "新设备登录 iCloud 时，可通过这两种方式之一加入 iCloud 钥匙串同步信任圈：**与现有的 iCloud 钥匙串设备配对并获得其担保，或者使用 iCloud 钥匙串恢复。**"
> "在配对流程中，申请设备为同步信任圈和同步列表（针对双重认证账户）新建同步身份，并将其出示给担保设备。**担保设备将新成员的公钥添加至同步信任圈，并且使用其同步身份和派生自用户 iCloud 密码的密钥再次签名。** 新的同步信任圈放置在 iCloud 中，该信任圈的新成员也是在此处对其进行签名。在双重认证账户中，担保设备还为加入设备提供**使用其身份密钥签名的凭证，表明申请设备应被信任**。然后，它会更新其受信任同步身份的单独列表，以包括申请设备。"
> "现在，签名信任圈有两个成员… 它们现在开始通过 CloudKit 或 iCloud 键值存储（视情境而定）交换各个钥匙串项。**如果两个信任圈成员都更新了相同的项目，其中一个会被选中，最终获得一致的结果。每个同步的项目都会加密，使只有用户信任圈内的设备才能解密，任何其他设备或 Apple 都无法解密。**"
> "新设备加入同步信任圈时会重复此'加入流程'… **此设计旨在确保所有成员拥有相同的钥匙串项。**"

**"由担保设备签名背书"就是 iCloud 钥匙串的信任建立方式**，它不依赖 Apple 的审批，而是依赖既有成员（这也是 §14.5 中 Apple 角色被限定为"传输与托管"的依据）。

## 11.5 必须显式标记：`kSecAttrSynchronizable`

> "部分钥匙串项特定于设备，如 iMessage 信息密钥，因而必须保留在设备上。**若要防止意外传输数据，每个将要同步的项目都必须使用 kSecAttrSynchronizable 属性明确标记。**"
> "**Apple 为 Safari 浏览器用户数据（包括用户名、密码和信用卡号）、无线局域网密码、HomeKit 加密密钥以及其他支持端对端 iCloud 加密的钥匙串项设置了该属性。**"
> "另外，**在默认情况下，第三方 App 添加的钥匙串项不会进行同步。将项目添加至钥匙串时，开发者必须设置 kSecAttrSynchronizable 属性。**"

这是本篇对"上传"问题最硬的三条口径之一：**默认不同步、需显式标记、且 Apple 只为特定类别设置该属性**。§11.8 给出该属性的八条限制。

## 11.6 属性侧的同步定义

A 级（`item-attribute-keys-and-values`）：

> `kSecAttrSynchronizable` — "A key with a value that's a string indicating **whether the item synchronizes through iCloud.**"
> `kSecAttrSynchronizableAny` — "Specifies that **both synchronizable and non-synchronizable results should be returned from a query.**"
> `kSecAttrSyncViewHint` — "A key with a value that's a string that provides **a sync view hint.**"（该页未进一步说明边界 → §16.2 N10）

`kSecAttrSynchronizable` 符号页给出的**默认值与查询语义**（A 级）：项默认为不可同步；在查询中把该键设为 `kSecAttrSynchronizableAny` 才能同时命中两类；**设置该键为 true 时，更新/删除会影响所有设备上的副本**。

## 11.7 类覆盖：macOS 11 是分界

TN3137（A 级，2026-09-24）：

> "**The data protection keychain can hold all keychain item classes (Internet password, generic password, certificate, key). macOS 11 and later synchronize all classes; earlier versions synchronize only the password classes.**"

手册与 developer 文档在同一件事上的另一句（A 级，`kSecAttrSynchronizable` 注意事项，逐字）：

> "**Starting in iOS 14, macOS 11, and watchOS 7, the keychain synchronizes passwords, certificates, and cryptographic keys. Earlier OS versions synchronize only passwords.**"

**两句共同确定"哪些凭据类别可被同步"的版本下限**，且都未给出"一定不同步"的完整清单——写任何"证书一定会/永不同步"的句子都需要版本限定语（§17.4）。

## 11.8 同步与设备状态的交叉限制

A 级（`kSecAttrSynchronizable` 符号页，八条注意事项中与保管相关者逐字）：

> - "tvOS **never** synchronizes keychain items."
> - "You **can't combine** this key with `kSecAttrAccessible` values that end in `ThisDeviceOnly`."（同步与"仅限本设备"互斥，见 §3.6）
> - 更新与删除作用于所有副本（§11.6）。
> - "**Do not use persistent references to synchronizable items.**" / 可同步项不能由持久引用指向。
> - 可同步项**不能**与 `SecAccess`（macOS ACL）组合使用（与 §4.7 的互斥链一致）。
> - 查询可同步项时，**匹配键仅限于 class 与属性集合，且只有 `kSecMatchLimit` 可用**。
> - **`kSecAttrSynchronizableAny`** 用于同时搜索两类（§11.6）。

**最后两条容易被忽略**：一旦项进入同步域，查询能力被收窄；这构成"同步"与"可检索性"之间的真实权衡。

## 11.9 钥匙串恢复：托管记录、安全码与二次因子

手册"iCloud 钥匙串安全恢复"（A 级，发布日期 2022 年 5 月 13 日）：

> "iCloud 钥匙串会将用户的钥匙串数据交予 Apple 托管，**但不允许 Apple 读取密码和钥匙串包含的其他数据**。即使用户只有一台设备，钥匙串恢复也可提供一个防止数据丢失的安全网。**当 Safari 浏览器用于为网络账户生成随机强密码或通行密钥时，这尤其重要，因为这些密码的唯一记录位于钥匙串中。**"
> "钥匙串恢复包含两大基本要素：**二次身份认证**和**安全托管服务**… 用户的钥匙串通过强密码进行加密，**只有满足一系列严格的条件，托管服务才会提供钥匙串副本。**"
> "如果用户账户打开了双重认证，则使用设备密码来恢复托管的钥匙串。如果未设置双重认证，用户需要通过提供六位数字密码来创建 **iCloud 安全码**…"
> "密码建立后… 设备先导出用户的钥匙串副本，然后**将其与密钥加密封装于非对称钥匙包中**，并放置在用户的 iCloud 键值存储区域。**密钥包被用户的 iCloud 安全码和储存托管记录的硬件安全模块 (HSM) 集群公钥所封装。** 它会变成用户的 iCloud 托管记录。对于双重认证账户，钥匙串还储存在 CloudKit 中并**封装到只能通过 iCloud 托管记录的内容才能恢复的中间密钥**…"
> 注："除了创建安全码，用户还需要为其 iCloud 账户注册电话号码。这在钥匙串恢复过程中提供了第二重认证。**用户收到短信后，需要回复才能继续恢复。**"

**"这些密码的唯一记录位于钥匙串中"一句是恢复机制存在的理由**，也是 §17.5 建议把"用户是否开启恢复"列为防线指标的依据。

## 11.10 托管侧的强度：HSM、SRP、10 次上限与固件

手册"iCloud 钥匙串的托管安全性"（A 级，发布日期 2024 年 5 月 7 日）：

> "iCloud 背后部署的是**硬件安全模块 (HSM) 集群**，这些集群为托管记录提供保护。如上所述，集群的每位成员都有一个密钥，用于对其监管下的托管记录进行加密。"
> "要恢复钥匙串，用户必须使用其 iCloud 账户和密码进行身份认证，并对发送至其注册电话号码的短信进行回复。回复完成后，用户必须输入其 iCloud 安全码。**HSM 集群利用安全远程密码 (SRP) 协议验证用户是否知道其 iCloud 安全码；密码本身不会发送给 Apple。**"
> "**托管服务只允许对托管记录认证和取回最多 10 次。** 多次尝试失败后，记录将被锁定，用户必须联系 Apple 技术支持部门才能进行更多尝试。**第 10 次尝试失败后，HSM 集群将销毁托管记录，钥匙串将永久丢失。** 这种方式以牺牲钥匙串数据为代价，防止通过暴力破解方式来取回记录。"
> "**这些策略已编入 HSM 固件中。允许更改固件的管理访问卡已经被销毁。任何尝试更改固件或访问私钥的操作，都会导致 HSM 集群删除私钥。** 如果发生这种情况，受集群保护的每个钥匙串的所有者都会收到信息，告知其托管记录已经丢失。他们随后可以选择重新注册。"

四条可核验的强度声明：**认证never 收到口令本身（SRP）/ 尝试次数为 10 / 超限即销毁 / 策略固化在固件且管理卡已销毁**。这是本篇中"上传到云端的数据在什么条件下会被永久销毁"的唯一官方答案。

## 11.11 高级数据保护：把服务密钥收进 iCloud 钥匙串保护域

手册"iCloud 高级数据保护"（A 级，发布日期 2022 年 12 月 7 日）：

> "在设备上生成且随后上传到 Apple 数据中心的认证后可用 iCloud 硬件安全模块 (HSM) 的所有 CloudKit 服务密钥**会从这些 HSM 中删除，并且会完全保存在 Apple 账户的 iCloud 钥匙串保护域中**。其处理方式和现有的端对端加密服务密钥一样，这意味着 **Apple 不可再读取或访问这些密钥**。"
> "具体是通过**将设备本机密钥签名的新值写入其 iCloud 钥匙串设备元数据**来实现。在该设备与用户的其他设备同步期间，**Apple 服务器无法移除或修改此证明。**"
> "**'高级数据保护'还会自动保护第三方开发者选择标记为加密的 CloudKit 字段以及所有 CloudKit 资源。**"
> "即使用户授权网页登录，服务器请求也无法诱使用户设备上传不可在 iCloud.com 上查看的数据（例如'健康'数据或**iCloud 钥匙串中的密码**）的服务密钥… 每次上传服务密钥时，该密钥会使用与用户授权的网页会话绑定的**临时密钥**加密，且用户设备上会出现一则通知…"

**最后一段是"上传"问题上最严格的官方约束样本**：连"授权网页访问"这一用户主动动作，都被限制到"按服务粒度、用会话临时密钥加密、并强制通知"。类别覆盖数（14→23）与其余机制归 #77。

## 11.12 一个 iOS 钥匙串还是两个

`keychains.md`（A 级）关于 iOS 侧的表述：iOS 只有一个钥匙串，**"which logically encompasses the iCloud keychain"**，且 **"automatically unlocked when the user unlocks the device and then locked when the device is locked."**

TN3137（A 级）关于 macOS 可见性的表述：

> "It displays the latter as either ***iCloud Keychain* or *Local Items*, depending on whether the user has enabled iCloud Keychain.**" / "**Keychain Access displays all keychain items in file-based keychains but only password items in the data protection keychain.**" / "The keychain support in the `security` command-line tool is **primarily focused on the file-based keychain**."

**iOS 上"钥匙串"与"iCloud 钥匙串"是逻辑包含关系，macOS 上是显示层二选一**——这句话决定了 §13 的整章结构。

## 11.13 同步边界没有完整清单（必须加限定语）

合并 §11.2–§11.12 可得官方表达同步边界的**三层结构**：**属性层**（`kSecAttrSynchronizable`）+ **实现层**（data protection keychain）+ **类别与版本层**（macOS 11 前后、tvOS 从不）。developer 文档**没有任何一处给出"哪些凭据类别一定同步/一定不同步"的完整清单**（§16.2 N11）。

## 11.14 安全隔区密钥不在同步范围（标注为推导）

官方两句（A 级）：

> "you can access the associated keychain item **only on the device that created it** (a feature that's also inherent to using the Secure Enclave)"
> "You can't change this attribute after creating the keychain item. **It isn't possible to migrate existing items between stores.**"

**developer 文档没有一句"SEP 密钥不参与 iCloud Keychain 同步"的直白断言**；本篇按 B 级合成处理，引用时标注"推导"（§16.2 N12）。

# 12. 备份、恢复与迁移中的凭据

## 12.1 五个密钥包对钥匙串的作用

§2.4 已给出总表（用户 / 设备 / 备份 / 托管 / iCloud 云备份）。本节补三条与凭据直接相关的边界（A 级，逐字）：

> "**钥匙串则使用来自用户密钥包中的类密钥来保护用户钥匙串中的项目。**"
> "**不可迁移钥匙串项仍使用 UID 派生密钥封装，以使其可以恢复到最初备份它们的设备，但在其他设备上不可访问。**"（这与 §3.6 的 `ThisDeviceOnly` 是同一机制的备份侧描述）
> "备份密钥包在'访达'（macOS 10.15 或更高版本）或 iTunes（macOS 10.14 或更低版本）进行加密备份时创建，并**储存在设备被备份到的电脑中**。新密钥包是通过一组新密钥创建的，备份的数据会使用这些新密钥重新加密。"

## 12.2 为什么"不设备份密码就无法迁移钥匙串项"

> "如果用户选择不加密备份，那么不管备份文件属于哪一种数据保护类，备份文件都不加密，**但钥匙串仍使用 UID 派生密钥获得保护。这就是只有设置备份密码才能将钥匙串项迁移到新设备的原因。**"（A 级）

**这句是整篇里对"备份能否带走凭据"最直接的官方因果句**：钥匙串项的保护强度不随"未加密备份"而降级；能迁移的前提是加密备份所生成的**备份密钥包**。

## 12.3 备份密钥包的派生强度与一条理论弱点

> "密钥包通过设置的密码加以保护，且加密过程会运行**一千万次 PBKDF2 密钥派生函数的迭代**。虽然迭代次数非常多，但是**密钥包并未捆绑特定设备**，因此在理论上，尝试在多台电脑上对备份密钥包并行展开暴力破解是可行的。而安全性足够高的密码可以减小这一威胁。"（A 级）

**官方自己点出"未绑定设备"这一残余风险**，并给出唯一缓解（口令强度）。这是 §15.4 检查表的一项，也是 §17.6 对合规侧的具体建议依据。

## 12.4 托管密钥包：无需用户口令的同步与远程清除

> "托管密钥包用于通过 USB 与'访达'或 iTunes 进行同步，**还用于设备管理服务**。此密钥包允许'访达'或 iTunes 执行备份和同步，**而无需用户输入密码**，它还**允许设备管理服务远程清除用户密码**。它储存在用于与'访达'或 iTunes 进行同步的电脑上，或者在远程管理设备的设备管理服务上。"（A 级）

结合 §3.5（`WhenPasscodeSetThisDeviceOnly` **不包括在托管密钥包中**），可得到一条完整的官方口径：**组织托管通道可以完成备份同步与口令重置，但被选为该最严档的凭据不进入该通道**。这两句必须并读（§1.6、§16.3）。

## 12.5 macOS 26.4：keychain 文件可能需要"受保护熵文件"

TN3137 新增章节"Backing up a file-based keychain"（A 级，**该节即 2026-09-24 修订新增的内容**）：

> "**Starting in macOS 26.4 a keychain file might not be a standalone item.**" / "In macOS 26.4 and later that's no longer the case. **A keychain file might reference a protected entropy file. To unlock such a keychain you need both the keychain password and the protected entropy file.**"
> Warning：**"The location, name, and format of these protected entropy files are not considered API. Don't encode details about these files into your product."**
> "These protected entropy files are stored in **`/var/db/SystemKeys`**. If you're creating a backup product, make sure to back up that directory. **Without it, the keychain files that you back up might be useless.**"
> "The `/var/db/SystemKeys` directory is **not directly accessible, even when running as root**… To access it, **disable System Integrity Protection (SIP)**."
> "**Every keychain file has salt, but not every keychain file has an associated protected entropy file.**"

**这是本篇对"凭据能否被备份工具带走"给出的最新、也最容易被漏掉的一条口径**：macOS 26.4 起，钥匙串口令本身可能不再是唯一因子，且官方明确禁止把该文件的路径/名称/格式写进产品。

## 12.6 macOS 的登录钥匙串与默认钥匙串

TN3137（A 级）：

> "Every context has a keychain search list and a default keychain. **In a user context the search list includes a per-user *login* keychain and a single *System* keychain, with the former being the default. In the system context the search list includes just the *System* keychain.**" / "**Each user gets exactly one data protection keychain. The system selects the correct keychain based on the context of the caller.**"
> 路径口径：登录钥匙串为 `~/Library/Keychains/login.keychain-db`

`SecKeychainCreate` 符号页（A 级）："The system ensures that a default keychain is created for the user **at login**…" 与 iOS 侧"用户解锁设备时自动解锁、锁定时自动锁定"（§11.12）构成**"口令解锁"与"设备锁屏状态驱动"两种解锁触发**的对照。

**冲突登记**：TN3137 写"**`SecKeychainCreate` was deprecated in the macOS 12 SDK**"，而该符号页 availability 为 **`macOS: 10.2.0 - 10.10.0`**、正文无 "Deprecated" 字样。本篇采用"TN3137 明示 macOS 12 SDK 标记废弃"并加注 availability 不一致，**不同时断言"10.10 起不可用"**（§16.1 C14）。

## 12.7 备份对凭据有效性的连带影响（跨章汇总）

| 凭据 | 备份/恢复后的状态 | 依据 |
| --- | --- | --- |
| `ThisDeviceOnly` 家族项 | 仅原设备可恢复 | §3.6、§12.1（UID 派生密钥） |
| `WhenPasscodeSetThisDeviceOnly` | 密码被移除/重设即类密钥丢弃 | §3.5 |
| 普通密码/令牌项 | 需加密备份才能迁移到新设备 | §12.2 |
| passkey | 随 iCloud 钥匙串同步/恢复；恢复机制存在的理由正在于此 | §7.1、§11.9 |
| App Attest 密钥 | **不存活于重装、迁移、从备份恢复** | §10.6 |
| APNs 设备令牌 | 恢复后需等用户启动 App 才拿到新 token | §10.3 |
| 设备卡号 / 证件私钥 | **绝不备份到 iCloud**；卡需重新预置 | §8.10 |
| iCloud 钥匙串本体 | 由托管记录 + 安全码 + 短信二次因子恢复，超限即销毁 | §11.9、§11.10 |

## 12.8 迁移类 API 的两种路径

老路径是 §6.13 的 `SecAddSharedWebCredential`（availability 上界 26.2.0），新路径是 §7.12 的 `ASCredentialExportManager` / `ASCredentialImportManager`（iOS 26+）。二者共同点是**都没有"导出一份可保存的明文凭据文件"这一设计**；不同点是前者作用于 App↔Safari 的域名共享，后者作用于**密码 App 之间**的一次性交接。§16.4 ⑭ 把这条演进登记为口径变化而非缺陷。

# 13. 平台差异与 API 分裂：同一句"存进钥匙串"在不同平台上不是同一件事

前 12 章的口径以 iOS/iPadOS 为默认主语。本章把"换了平台，哪句话会失效"逐条列清——这一章不是补充材料，而是前文多条结论的**适用边界**。

## 13.1 macOS 的官方三分口径：三套 API、两种实现

A 级（Tech Note **TN3137 "On Mac keychain APIs and implementations"**，副标题逐字 **"Learn how the keychain on macOS differs from other Apple platforms."**；Revision History：2026-09-24 / 2022-11-01 / 2021-12-10）：

> "All Apple platforms support the keychain. **On iOS, tvOS, and watchOS the keychain story is very simple: There's a single SecItem API with consistent behavior. This is not the case on macOS.** The keychain on macOS has a long history, dating back to the previous millennium."

> "macOS has three keychain APIs:
> - **Keychain.** This originated on traditional Mac OS. Mac OS X supported it as a compatibility measure, and that support persists all the way through to the latest versions of macOS.
> - **SecKeychain.** This was introduced with Mac OS X and was the standard macOS keychain API until the introduction of the SecItem API.
> - **SecItem.** This was part of the original iOS SDK. macOS gained support for it in macOS 10.6."

> "macOS has two keychain implementations:
> - **File-based keychain**
> - **Data protection keychain**
> The file-based keychain has its origins on traditional Mac OS. **The data protection keychain originated on iOS and came to macOS with the advent of iCloud Keychain on macOS 10.9.**"

> Important: "**iOS, tvOS, and watchOS only support the SecItem API with the data protection keychain implementation.**"

**这就是本篇把 macOS 单独设章的理由**：在 iOS 上，"用 `SecItem`" 与"用数据保护钥匙串"是同一件事的两个说法；在 macOS 上是**两个独立决策**（选 API、选实现），组合空间为 3×2。任何跨平台报告若把 keychain 写成单一子系统，就与 Apple 自己的三分口径冲突（§16.4 ⑯）。

同时，TN3137 给出了演进方向（A 级逐字）：

> "The **file-based keychain is on the road to deprecation**. It's not officially deprecated, but some of the APIs surrounding it are. For example, **`SecKeychainCreate` was deprecated in the macOS 12 SDK**. Moreover, **new features, like iCloud Keychain, require the data protection keychain.**"
> "Choosing a keychain API is easy: **Use the SecItem API.** … Choosing a keychain implementation is more nuanced. **Default to targeting the data protection keychain** because: It behaves consistently across all Apple platforms. It benefits from new features, like iCloud Keychain. **It has an access control model that's both easier to understand and better aligned with current platform norms.**"

## 13.2 默认落点：`SecItem` 在 macOS 上默认打到 file-based

A 级（TN3137，逐字）：

> "The **Keychain and SecKeychain APIs always target the file-based keychain**. The SecItem API can target either implementation. **It defaults to targeting the file-based keychain.** To target the data protection keychain, **set the `kSecUseDataProtectionKeychain` attribute or the `kSecAttrSynchronizable` attribute to true.**"

这一段有三条可直接用于排障的推论：

1. **iOS 代码原样搬到 macOS，默认落点就变了。**§2–§5 的全部保护口径（两把 AES-256-GCM 密钥、SEP 往返、access group）描述的是数据保护实现；file-based 实现是另一套模型。
2. **`kSecAttrSynchronizable` 本身就是一个"切换开关"**，而且它的副作用（进入 iCloud 同步域）与它的机制效果（换实现）绑在同一个键上——这正是 §4.7 与 §11.5 必须并读的原因：只想换实现不想同步时，官方给的键是 `kSecUseDataProtectionKeychain`（"Use `kSecUseDataProtectionKeychain` **to get the iOS behavior without synchronization**."）。
3. `kSecUseDataProtectionKeychain` 的定义句本身就承认了平台差异：**"A key whose value indicates whether to treat macOS keychain items like iOS keychain items."**（§4.7）

A 级（同一符号页 availability 逐字）：`iOS: 13.0.0 - / iPadOS: 13.0.0 - / macCatalyst: 13.1.0 - / macOS: 10.15.0 - / tvOS: 13.0.0 - / visionOS: 1.0.0 - / watchOS: 6.0.0 -`。**注意 iOS 上该键要 iOS 13 才存在**，而"iOS 行为"在 iOS 上一直是默认——这解释了为什么它在 iOS 侧只是向前兼容的显式声明，而在 macOS 侧是真正的分流开关。

## 13.3 只有数据保护实现才提供的三项能力

A 级（TN3137，逐字）：

> "Some keychain features **require the data protection keychain**, including:
> - **iCloud Keychain.** See the `kSecAttrSynchronizable` attribute.
> - **Protecting an item with biometrics (Touch ID and Face ID).** See `restricting-keychain-item-accessibility`.
> - **Protecting a key with the Secure Enclave.** See `protecting-keys-with-the-secure-enclave`."

> "The data protection keychain **can hold all keychain item classes** (Internet password, generic password, certificate, key). **macOS 11 and later synchronize all classes; earlier versions synchronize only the password classes.**"

**这张"能力—实现"依赖表是 macOS 防线的核心事实**：本篇第 5 章（`SecAccessControl` + 生物识别）、第 8 章（SEP 私钥）、第 11 章（iCloud 钥匙串同步）三条最强结论，在 macOS 上都有一个共同前置——程序必须选择数据保护实现。反之，§3 的 accessibility 档位口径不能直接搬到 file-based 实现上（§16.1 C2）。

TN3137 还给出该实现"只在用户上下文可用"的硬边界（A 级逐字）：

> "Programs that run **outside of a user context, like a `launchd` daemon, must target the file-based keychain**. **The data protection keychain is only available to programs running in a user context, like an app or an app extension.**"

也就是说：**macOS 上守护进程保管凭据，天生拿不到第 5 章与第 8 章的两项能力**。这不是配置选择，而是官方文档写明的可用域限制。

## 13.4 强制与反向强制：四种"没得选"的情形

A 级（TN3137，逐字）：

> "In some environments **your only option is the data protection keychain**, including:
> - **Mac Catalyst apps**
> - **iOS Apps on Mac**
> In these environments **you don't need to supply the `kSecUseDataProtectionKeychain` attribute or the `kSecAttrSynchronizable` attribute** to target the data protection keychain. **It's the default and only option. The system ignores the `kSecUseDataProtectionKeychain` attribute in these environments.**"

> "In other situations **your only option is the file-based keychain**:
> - Programs that **read existing items in a file-based keychain** must target the file-based keychain. If you find yourself in this situation, **consider migrating items to the data protection keychain as you update your app.**
> - Programs that run outside of a user context, like a `launchd` daemon…"

**四条口径合并后的判定表**（本篇采用）：iOS/iPadOS/tvOS/watchOS = 只有数据保护；Mac Catalyst 与 iOS Apps on Mac = 只有数据保护且属性被忽略；macOS 普通 App = 默认为 file-based、可两处切换；macOS 非用户上下文程序 = 只能 file-based。这条判定直接决定第 3、5、8、11 章结论是否适用。

## 13.5 访问控制模型也随实现分裂

A 级（TN3137，逐字）：

> "Each keychain implementation **uses its own access control model**:
> - The **file-based keychain uses access control lists (`SecAccess`)**.
> - **The data protection keychain uses keychain access groups, supplemented by an optional access control object (`SecAccessControl`).**"
> "macOS **builds the list of data protection keychain access groups available to your program from its code signing entitlements**. … These entitlements **must be authorized by a provisioning profile**. Your program **needs an app-like bundle structure in which to embed that profile**. This is standard for app and app extensions **but not for command-line tools**."
> "If you're building **library code, its data protection keychain access is determined by the entitlements of the host process's main executable**."
> "The SecItem API is well aligned with the data protection keychain. However, when you use it to **target the file-based keychain it operates through a shim. That shim has limitations.** … the access control model of the file-based keychain is completely different … **some limitations are just bugs. To avoid such problems, target the data protection keychain. This is particularly important when you're porting keychain code from iOS.**"

**这一段把第 4 章的结论限定住了**：§4.1–§4.5 的虚拟数组顺序、Team ID 前缀与 `errSecMissingEntitlement` 语义，全部属于数据保护实现。macOS 上还有第三种模型（`SecAccess` ACL，键为 `kSecAttrAccess`），且与 `kSecAttrAccessControl` **互斥**（§4.6）。另外两句是防线审计里最容易被漏掉的：**动态库的凭据可见域由宿主主可执行文件的 entitlements 决定**（不是库自己的签名），以及 **命令行工具默认拿不到嵌入描述文件的 bundle 结构**，因此默认拿不到数据保护钥匙串的组。

§8.8 的 PKCS#12 导入口径也在同一分裂里：`SecKeychainItemImport`（"adds them to a keychain"）与 `SecItemImport`（"**optionally** adds them"）分属两套 API，措辞差异不可合并（§16.1 C11）。

## 13.6 显示层：管理员看到的是两个名字、一类条目

A 级（TN3137，逐字）：

> "The Keychain Access application supports both file-based keychains and the data protection keychain. The keychain list shows **all the file-based keychains in the search list for the current user—typically this is just *login* and *System*—and the data protection keychain. It displays the latter as either *iCloud Keychain* or *Local Items*, depending on whether the user has enabled iCloud Keychain.**"
> "**Keychain Access displays all keychain items in file-based keychains but only password items in the data protection keychain.**"
> "Keychain Access **sometimes fails to reflect changes made to the keychain by other apps** (r. 82556933). If you see unexpected results, quit and relaunch Keychain Access."

三条防线含义：① macOS 上"iCloud 钥匙串"与"Local Items"是**同一个实现的两种显示名**，与 §11.12 的"iOS 上逻辑包含、macOS 上显示二选一"完全对应；② **图形工具看不到数据保护钥匙串里的证书与密钥类项**——"我在 Keychain Access 里没找到"不能作为"该凭据不存在/未保管"的证据（§15.4 的一项）；③ 官方明确承认工具刷新不可靠，任何以工具截图为依据的结论都要加"重启工具复核"这一步。

另外两项 macOS 专有能力也属显示/管理面（A 级，`security/trust-settings` 组归属 macOS，未列 iOS）：**Trust Settings**（用户/管理员对单个证书的"始终允许/永不信任"覆盖）与 §9.6 的 exceptions。§9 的信任评估结论在 iOS 上没有这条管理通道（§15.2）。

## 13.7 tvOS、watchOS 与 visionOS：三个"看起来一样"的例外

A 级（`kSecAttrSynchronizable` 符号页 Discussion，逐字）：

> "You can set the `kSecAttrSynchronizable` key on tvOS, but **tvOS doesn't synchronize your app's keychain items through iCloud.** Items that you store on tvOS **never leave the device where you create them**, and items that you store on other devices **don't synchronize to tvOS devices**."
> 同页注意事项另一句："tvOS **never** synchronizes keychain items."

A 级（`kSecUseUserIndependentKeychain` 单页）：定义句 **"A key with a value that indicates whether to store the data in a keychain available to anyone who uses the device."**；availability 逐字为 **`tvOS: 16.0.0 -`（只有 tvOS 一个条目）**。这是本篇唯一一处"跨用户共享凭据"的官方键，且它只在 tvOS 上有文档存在性——引用时不得写成 iOS 可用（§15.4）。

A 级（availability 平台清单不一致，逐字）：
- `LAContext`：`iOS: 8.0.0 - / iPadOS: 8.0.0 - / macCatalyst: 13.1.0 - / macOS: 10.10.0 - / visionOS: 1.0.0 - / watchOS: 3.0.0 -`——**没有 tvOS 条目**。第 5 章"用生物识别/密码保护单项凭据"的路径在 tvOS 上没有对应的 LocalAuthentication 页面依据。
- `SecAccessControlCreateFlags` 各成员多数只列 `iOS / macOS / watchOS`，例如 `devicePasscode` 为 `iOS: 9.0.0 - / macOS: 10.11.0 - / watchOS: 2.0.0 -`；`biometryCurrentSet` 同理。**部分 flag 连 iPadOS/tvOS/visionOS 条目都没有**，这属页面元数据口径而非功能断言（§16.2 N3 相关）。
- `SecKeychain` 集合页：`macCatalyst: 13.0.0 - / macOS: 10.0.0 -`——**macOS/Catalyst 专属**，与 §13.1 的"三套 API 只存在于 macOS"一致。
- `SecAddSharedWebCredential(_:_:_:_:)`：`iOS: 8.0.0 - 26.2.0 / macOS: 11.0.0 - 26.2.0`——同一 API 的平台起点不同（iOS 8 vs macOS 11），且两端都出现终止版本 26.2.0（§6.13、§16.4 ⑭）。

## 13.8 Availability 元数据的正确用法（方法论）

本篇把 availability 当作**版本存在性证据**而不是功能证明，规则如下：

| 观测形态 | 能得出 | 不能得出 |
| --- | --- | --- |
| 开区间（`iOS: 9.0.0 -`） | 该 SDK 起提供、当前未标终止 | "所有平台都支持"（要逐条看是否有条目） |
| **闭区间**（`iOS: 8.0.0 - 9.0.0`、`iOS: 8.0.0 - 26.2.0`） | 官方已收敛该 API | 正文常**没有** "Deprecated" 字样，弃用判定只能来自区间（§16.1 C4、C14） |
| **缺失某平台条目**（LAContext 无 tvOS） | 该页未声明支持 | "该平台一定不可用"（可能与同族页面清单不一致） |
| 单页只有标题句、无 Discussion | 语义需从他处合成 | 不应替官方补写语义（§16.2 各项） |

配套的两条抓取口径（A 级，本篇 404 清单见 §16.1 与附录 A.3）：
- **符号页 slug 必须驼峰**：`/documentation/Security/kSecAttrTokenIDSecureEnclave.md` 可取，`ksecattribletokenidsecureenclave.md` 返回 404。
- **文章/集合页大小写不稳定**：`security/certificate-key-and-trust-services.md` 404，改 `Security/...` 后 200；集合页（collectionGroup）不能假定与文章页同规则。

# 14. 谁能改变凭据的保管状态：五类主体与他们的官方控制点

第 2–13 章回答"凭据被怎样保管"。本章换一个轴：**哪些主体、通过哪个官方入口、能把保管状态改成另一种状态**。这一轴决定了防线的责任归属——很多问题不是开发者的 bug，而是另一个主体的正常操作。

## 14.1 用户：五个日常动作都会改变凭据的可读性

| 用户动作 | 官方后果（逐字锚点见括号） | 影响面 |
| --- | --- | --- |
| **不设置设备密码** | "A device without a passcode is considered to always be unlocked. **This is the default accessibility** when you don't otherwise specify a setting."（§3.3） | 默认档 `WhenUnlocked` 的保护强度归零 |
| **移除已设置的密码** | 选中 `WhenPasscodeSetThisDeviceOnly` 的项，其类密钥被丢弃、项**不可恢复**（§3.5） | 单项凭据永久失效 |
| **变更/新增生物识别特征** | `biometryCurrentSet` 类 flag 使项与"当前 enrolled 特征集合"绑定，集合变化后需重新认证/重建（§5.3、§5.11） | 单项凭据失效或需重建 |
| **关闭 iCloud 钥匙串** | 同步域退出；已同步项在各设备的副本处理由系统管理；托管侧记录仍在（§11.5、§11.9） | 类别为密码/passkey/TOTP 种子、Wi-Fi 密码、HomeKit 密钥 |
| **关闭凭证提供程序扩展** | "**When the user disables your extension, the system clears and disables your shared store.**"（§7.7） | 第三方密码 App 的系统侧索引库被清空 |

另有两项与"上传"直接相关的用户决定：**是否设置备份口令**决定钥匙串项能否迁移到新设备（§12.2："只有设置备份密码才能将钥匙串项迁移到新设备"），以及**是否同意把凭证发放给某个 App**——手册口径为不会透露给 App 除非用户同意（§6.5），iCloud 钥匙串恢复流程还额外要求用户**主动提供安全码并在受信任设备上确认**（§11.9）。

TN3137 补充了 macOS 用户的直接操作面（A 级逐字）："**Users can create additional keychains, or change the default, by using the Keychain Access application.**"——即 §12.6 的搜索列表与默认钥匙串可被用户改写，这是 macOS 特有的、App 无法预期的状态变化（§15.4 检查项）。

## 14.2 开发者：在"创建时"定型，之后能改的很少

开发者的控制点几乎全部集中在**写入那一刻的属性组合**：`kSecClass`（决定加密与查询形态，§10.2）、`kSecAttrAccessible`（§3.2–§3.6）、`kSecAttrAccessControl` + flags（§5.2–§5.5）、`kSecAttrAccessGroup`（§4.1–§4.4）、`kSecAttrTokenID`（§8.5）、`kSecAttrSynchronizable`（§11.5）、`kSecUseDataProtectionKeychain`（§13.2），以及查询侧的 `kSecUseAuthenticationUI` / `kSecUseAuthenticationContext` / `kSecMatchLimit`（§4.8–§4.9、§5.8–§5.9）。

三条边界值得单列：
1. **组不在自己手里**：可用的钥匙串访问组"由 macOS/iOS 从代码签名 entitlements 构建"，且"**entitlements must be authorized by a provisioning profile**"（§13.5）——开发者写得出组名，不等于运行时会拥有它。
2. **动态库没有自己的可见域**：其凭据访问权限由宿主主可执行文件的 entitlements 决定（§13.5）。把凭据封装进 SDK 并不缩小可读它的进程集合。
3. **"上传"这一步没有系统闸门**：官方从未对 App 把自己 keychain 项发给自建服务端设置前置条件（§1.5 的关键不对称、§17.2）。这一条使"开发者"在 U1 方向上成为唯一责任人。

## 14.3 网站与 App 域名管理者：AASA、规则与关联域

三方同意机制（§6.4）把域名侧列为独立主体：**开发者**需要 Apple 批准的 App ID 与 `associated domains` entitlement，**网站管理员**在服务器上放 AASA 文件，**用户**在提示后同意。域名侧还可控的两项（A 级，§6.3）：`passwordrules` 属性（"the password rules for the website"，由域名的 `passwordRules` 提供）与 `newPassword` 提示；自动强密码建议只作用于关联域（§6.2、§6.4）。

§6.13 的 Shared Web Credentials 作用域同样由**域名**决定；其 availability 上界 26.2.0 意味着这条域名侧通道正在被 §7.12 的 App↔App 迁移机制接替（§16.4 ⑭）。

## 14.4 组织：四类官方控制点

A 级口径按控制对象归为四组：

1. **限制某条凭据外发路径**（最直接的一句，§6.12）："**针对通过设备管理服务管理的设备或 App，组织可以限制无线局域网密码共享的使用。**"——这是本篇唯一一处"组织可对具体凭据外发协议行使否决权"的直白表述。
2. **接管票证类凭据的配置**（§10.7）："**为了配置 SSO，有一个配置可允许设备管理服务将必要设置推送给设备。**"——包括用户主体名称、Kerberos 领域，以及"应允许哪些 App 和 Safari 网站 URL"使用扩展。票证由系统扩展保管，组织的入口是配置而非 App 权限。
3. **改变设备形态从而改变密钥包归属**：§2.4 登记的"共享 iPad"模式例外——"在'共享 iPad'模式中配置的 iPad 设备**有时需要在用户登录前访问凭证，因此需要一个不受用户密码保护的密钥包**。"这是组织配置直接导致"存在一条不以用户密码为熵的凭据保护路径"的官方说明，防线含义记入 §17.6。
4. **设备真实性与远程处置**：§8.11 的管理式设备证明（已证明密钥、ACME、Image4 测量）为组织提供"这台设备值得信任到可以下发凭据"的判据；配套的受管理丢失模式/远程擦除属设备管理面（手册"安全设备管理概览"页，发布日期 2024 年 12 月 19 日）。**擦除会连带销毁钥匙串**，但本篇未取到"擦除后各落点凭据逐项后果"的官方清单，不在此虚构（§16.2 N11 同族缺口）。

**组织与用户控制点的关键差异**：组织能限制"外发协议"和"设备形态"，但**没有任何官方口径允许组织替代用户同意**（§6.5 的同意主体始终是用户），也不能替开发者设置 keychain 属性。把 MDM 写成"可强制同步/强制共享凭据"没有依据。

## 14.5 Apple：被官方限定为传输、托管与命名

从各章 A 级句子可以合成一份**克制的**Apple 角色清单：

- **传输不承担可读性**："钥匙串项通过 Apple 服务器在不同设备之间传输，但会进行端对端加密，使 Apple 和其他设备无法读取其内容。"（§11.1）
- **不审批信任圈的加入**：新设备经"**与现有的 iCloud 钥匙串设备配对并获得其担保，或者使用 iCloud 钥匙串恢复**"进入（§11.4）——担保方是既有成员，不是 Apple。
- **托管侧只做验证与限额**：恢复需要托管记录 + 安全码 + 短信二次因子；口令验证走 **SRP（口令不发给 Apple）**，**最多 10 次**、第 10 次失败即销毁托管材料（§11.10）。高级数据保护进一步把 CloudKit 服务密钥收进钥匙串保护域（§11.11）。
- **签发与撤销信任基础**：根证书清单、CRL/OCSP、CT、SHA-1/RSA-2048/X25519MLKEM768 等要求属系统信任基础（§9.7），且 developer 文档**没有**根证书清单页（§16.2 N8）。
- **决定 API 的生死与命名**：availability 区间（§13.8）、TN3137 的迁移建议（§13.1）、以及"certificate pinning 没有专页""`ASPasskeyCredential` 类不存在"这类命名事实（§16.4 ⑮）。

**这条边界的用处**：写"Apple 是否会读到某项凭据"时，主语要落到具体机制（是否在同步域、是否用 `ThisDeviceOnly`、是否属系统内建类别），而不是笼统的"Apple 政策"。§16.3 的簇一就是为这个错配准备的。

## 14.6 动作 → 生效范围 → 可观测痕迹

| 主体 | 动作 | 生效范围 | App 侧能否观测 |
| --- | --- | --- | --- |
| 用户 | 移除设备密码 | 选中最严档的单项凭据 | 可：读取返回 `errSecItemNotFound`/认证失败类错误（§15.3） |
| 用户 | 变更生物识别集合 | `biometryCurrentSet` 绑定的项 | 可：需重新认证或重建 |
| 用户 | 关闭 iCloud 钥匙串 | 全部可同步项的同步域 | **不可**：官方无"同步被关闭"的回调口径 |
| 用户 | 拒绝发放凭证 | 单次自动填充/passkey 断言 | 可：Authorization 流程返回失败/取消 |
| 用户 | 关闭凭证提供扩展 | **整个共享 identity store 被清空** | 部分：扩展下次启动时发现库为空 |
| 开发者 | 更换 access group | 该项的可读进程集合 | 可：错组表现为写侧 `errSecMissingEntitlement`、读侧 `errSecItemNotFound` |
| 网站管理员 | 撤下 AASA | 该域名的自动填充/关联域能力 | 不可（在域名侧生效） |
| 组织 | 限制 Wi-Fi 密码共享 / 推送 SSO 配置 / 启用共享 iPad | 受管设备与 App | 部分：可探测配置状态，但官方无逐项通知口径 |
| Apple | 信任圈内的**其他成员设备**变更 | 同步冲突合并结果 | 不可：合并语义只在系统侧 |

**"App 侧无法观测"这一列是本报告给防线设计的最重要提醒之一**：用户关闭同步、信任圈合并冲突、域名侧规则变更三类事件都不会变成开发者可读的错误码；任何依赖"检测到异常再处理"的策略在这三类上必然失效，只能改为**持续声明 + 定期核对**（§17.3、§15.4）。

# 15. 判定链与可观测性：一项凭据要过几道门，每道门留下什么痕迹

## 15.1 七道判定（顺序即因果链）

把 §2–§13 的口径重排为一条**单向依赖链**：任一环节的取值决定下一环节是否有意义。排列依据是官方自身的分组顺序（A 级：Security 框架把证书、密钥与信任服务并列为 "certificate, key, and trust services API"，且§8 章已述"先有证书与身份，再有策略与信任评估，最后用密钥做运算"）。

| 道 | 判定问题 | 决定输入 | 失效表现 | 详见 |
| --- | --- | --- | --- | --- |
| ① | **落在哪套实现/哪个落点**（L1–L6） | 平台、是否 `kSecUseDataProtectionKeychain`、是否 `kSecAttrTokenID` | macOS 默认 file-based，导致 ③⑤⑥ 全部不适用 | §13.2、§1.2 |
| ② | **属于哪个类，用哪把密钥加密** | `kSecClass`、`kSecValueData` 是否为私密值 | 元数据用表格密钥、私密值用行独有密钥——可搜索≠可读 | §2.3、§10.2 |
| ③ | **当前设备状态能否满足它的可访问性档** | `kSecAttrAccessible` + 锁屏/首次解锁/密码是否存在 | 默认档在无密码设备上等于无保护 | §3.2–§3.4 |
| ④ | **哪个进程集合被允许读** | `kSecAttrAccessGroup` + entitlements + 描述文件 | 写错组 `errSecMissingEntitlement`，读错组 `errSecItemNotFound` | §4.1–§4.5 |
| ⑤ | **是否需要"用户存在"，以何种方式** | `SecAccessControl` flags、`applicationPassword`、`LAContext` 复用 | 后台/锁屏时 `errSecInteractionNotAllowed`；主线程阻塞 | §5.2–§5.10 |
| ⑥ | **是否进入同步域，信任圈里有没有这台设备** | `kSecAttrSynchronizable` + 用户开启 iCloud 钥匙串 + 圈内成员 | 未标记则默认不同步；`ThisDeviceOnly` 与同步互斥 | §11.2–§11.8 |
| ⑦ | **能否活过备份/恢复/重装/迁移** | 档的 `ThisDeviceOnly` 后缀、备份口令、SEP/外部存储绑定 | App Attest keyId 不存活重装；SEP 项只在创建设备可用 | §12.1–§12.8、§8.5 |

**证书类凭据还要多过一道门**：上述七道解决"能不能读出这张证书"，不解决"它是否被信任"。信任评估是独立链（锚点集合 + 策略 + 搜索范围 + ATS 下限 + Trust Settings），且**信任结果与 keychain 可读性正交**（§9.5、§9.12）。§9.6 的 exceptions 内容对调用方是**不透明 cookie**——即这道门本身可观测性有限，这是本篇把"可观测性"单列为本章的理由。

## 15.2 每道门的可观测字段

| 道 | 可回读/可观测 | 官方无口径（不可观测） |
| --- | --- | --- |
| ① | `kSecUseDataProtectionKeychain` 是否被设置；macOS 上 Keychain Access 的搜索列表（显示名 *iCloud Keychain* / *Local Items*） | "某项实际落在哪套实现"没有回读 API；工具只显示密码类项 |
| ② | 属性字典回读（class、label、account、server 等元数据） | 行独有密钥的密钥材料、SEP 侧状态一律不可见（"no process can ever directly read or modify it"） |
| ③ | 读取成功/失败；`AfterFirstUnlock` 与 `WhenUnlocked` 的时序差 | **设备是否设置密码**在多数 App 语境下没有官方直读口径 → 只能用行为推 | 
| ④ | `errSecMissingEntitlement`（写）/`errSecItemNotFound`（读）；`kSecAttrAccessGroup` 回读 | 其他 App 的组内是否存在同名项——不可枚举 |
| ⑤ | `errSecInteractionNotAllowed`；`LAContext` 的 `wasmProtected`/错误码；认证 UI 是否弹出 | 上一次成功认证的时间戳不返回给 App |
| ⑥ | 属性回读 `kSecAttrSynchronizable`；`kSecAttrSynchronizableAny` 搜索结果分桶 | 同步是否被用户关闭、信任圈成员列表、冲突合并结果——全部无回调 |
| ⑦ | 恢复后读取是否成功（一次性验证）；App Attest keyId 是否仍存在 | 备份口令是否被设置——App 侧无官方判据 |

**这张表的读法**：越靠后的道，App 侧可观测性越低。第 ⑥⑦ 两道（也就是"上传/迁移"最相关的两道）恰好是**只能事后发现、不能事前确认**的——这直接推出 §17.3 的"把不可观测项改为显式声明 + 冷启动核对"建议。

## 15.3 `errSec*` 语义清单（本篇核实到的原句）

A 级，逐字：

- 组与 entitlement（`sharing-access-to-keychain-items-among-a-collection-of-apps`）："If you try to use an access group to which your app doesn't belong, **the operation fails and returns the `errSecMissingEntitlement` status. This includes attempts to 'prime' an entry using a zero-length string** as the value for the `kSecValueData` key."
- 同页读侧："If you specify a group to which your app doesn't belong, no items match and **the query returns the `errSecItemNotFound` status.** If you **don't specify an access group** in the query, **the search matches any of your app's groups.**"
- 认证 UI（`kSecUseAuthenticationUI`）："If provided with a value of `kCFBooleanTrue`, the error **`errSecInteractionNotAllowed`** is returned when the item is attempting to authenticate with UI."
- `kSecUseAuthenticationUIFail`："When you specify this value, if user authentication is needed, **the function returns the `errSecInteractionNotAllowed` error.**"
- 后台创建 SEP 密钥（`SecKeyCreateRandomKey(_:_:)`）："**`SecKeyCreateRandomKey(_:_:)` fails and returns `errSecInteractionNotAllowed` if you call it in the background on iPhone or iPad while the device is locked.**"
- PKCS#12 导入（`SecPKCS12Import(_:_:_:)` Return Value）："The function returns **`errSecSuccess`** if there were no errors, **`errSecDecode`** if the blob can't be read or is malformed, and **`errSecAuthFailed` if the password was not correct or data in the blob was damaged.**"

**四条排障规则**（由上表可直接推出，属 B 级合成）：
1. **权限类与存在类可区分**：写侧权限错误是 `errSecMissingEntitlement`，读侧组不匹配是 `errSecItemNotFound`——后者**不是权限错误**，把"读不到"写成"没权限"是错误归因。
2. **交互类错误集中在一个码**：设备锁定、后台调用、UI 被禁三种情形的返回值同为 `errSecInteractionNotAllowed`，**单靠错误码无法区分原因**，需结合调用上下文（是否在扩展/后台、是否设置了 UI 键）。
3. **口令类失败有两义**：`errSecAuthFailed` 同时覆盖"口令不对"与"数据被损坏"，不可据此判断口令对错（这是官方口径，不是实现缺陷）。
4. **`SecPKCS12Import` 成功不等于"进了钥匙串"**：该函数返回解密后的身份数组，导入是**可选**动作（§8.8）。

## 15.4 防线自测清单（只问"是否成立"，不含任何提取或绕过步骤）

以下 20 条均可由本篇 A 级句子判定"是/否/未知"，用于自查现有实现是否把保护建立在官方没给的前提上。

**属性与档位**
1. 有没有凭据使用默认 `kSecAttrAccessible`？若有，**在没设密码的设备上**它实际等于什么（§3.3）？
2. 需要后台/推送前读取的项，是否落在 `AfterFirstUnlock` 而非 `WhenUnlocked`（§3.8）？
3. 是否用了 `Always` 系两档？若用，是否知道其 availability 为**闭区间**（iOS 12 / macOS 10.14 终止）且指南页标为不推荐（§3.4、§16.1 C4）？
4. 选中 `WhenPasscodeSetThisDeviceOnly` 的项，是否设计了"密码被移除即永久失效"后的重建路径（§3.5）？
5. 选中 `ThisDeviceOnly` 的项，是否理解"可进备份但只有原设备能用"（§3.6）？

**作用域与实现**
6. 共享组的每一项名是否带 **Team ID 前缀**，而 App 组是否误以为也带前缀（§4.1、§4.4）？
7. 是否存在"写进 A 组、期望 B 组读到"的用法（项只属于一个组，§4.3）？
8. macOS 版本：程序是否**显式**设置 `kSecUseDataProtectionKeychain`，还是默认落到了 file-based（§13.2）？
9. macOS 版本：是否有以 Keychain Access 截图作为"凭据不存在"证据的流程——注意它**只显示密码类项**且官方承认刷新不可靠（§13.6）？
10. 守护进程类组件是否被误设计为需要生物识别或 SEP 密钥（该实现只在用户上下文可用，§13.3）？

**认证与时序**
11. 需要用户存在的读取是否可能在主线程发生、是否有 `errSecInteractionNotAllowed` 的降级路径（§5.10）？
12. 是否复用已认证的 `LAContext`？若复用，是否定义了**复用窗口**的责任边界（§5.8、§17.4）？
13. 仍在读 `kSecUseNoAuthenticationUI` 的代码是否已迁移到 `kSecUseAuthenticationUI`（后者才是现行键，§5.9）？

**同步与迁移**
14. 每一项"应该同步"的凭据是否**显式**设置了 `kSecAttrSynchronizable`（第三方默认不同步，§11.5）？
15. 是否存在"既标同步又用 `ThisDeviceOnly`/`SecAccess`/持久引用"的非法组合（§11.8 八条限制）？
16. 依赖"凭据会随备份到新设备"的功能，是否处理了"用户未设备份口令时钥匙串项不迁移"（§12.2）？
17. App Attest：是否把 **attestation 对象**当作需要长期保存的东西、并把 keyId 当作稳定设备标识（官方两条都反对，§10.5–§10.6）？
18. APNs 设备令牌是否被写入本地存储（官方 "Never cache the device token locally"，§10.3）？

**信任与外部主体**
19. 自签/企业 CA 场景：是否**显式**调用 `SecTrustSetAnchorCertificates(_:_:)`——把根证书放进钥匙串不产生锚点效果（§9.5）？
20. ATS 域名上是否存在"用自定义 challenge handler 放行"的代码——官方口径是**不能放松、只能收紧**（§9.8）；是否理解 pinning 在 developer 文档里不是被命名的机制（§9.9）？

**跨主体项**（无一条属开发者可控，但必须写进策略）
- 用户是否可能关闭凭证提供扩展而使共享库被清空（§7.7）？
- 受管设备上是否可能因组织限制而使某条外发路径不可用（§14.4）？
- "共享 iPad"模式下是否存在不以用户密码为熵的凭据保护路径（§2.4）？
- tvOS 目标上是否使用了 `kSecUseUserIndependentKeychain`（"available to anyone who uses the device"，且仅 tvOS 有该键，§13.7）？

## 15.5 三类假信号

1. **"读不到 = 被保护了"**。`errSecInteractionNotAllowed`（设备锁着）、`errSecItemNotFound`（组不同）、`ThisDeviceOnly`（换设备）三种"读不到"都不是加密强度；把任一者写进安全说明都是虚报（§15.3）。
2. **"能搜到 = 能读出"**。可搜索性建立在**元数据可被计算**之上，而元数据由表格密钥加密并缓存在应用处理器侧；私密值才走行独有密钥与 SEP 往返（§2.3）。查询返回属性但取不到 `kSecValueData` 是设计，不是漏洞。
3. **"存进钥匙串 = 被信任 / 会被迁移 / 会同步"**。三个"="分别被 §9.5（锚点需显式设置）、§3.6 与 §12.2（迁移条件）、§11.5（需显式标记）逐句否定；§4.7 的 "iOS behavior **without synchronization**" 正是为拆掉这串等式而存在的句子。

# 16. 口径冲突、未披露与必须并读

本章不做新论证，只做**登记**：同一件事在两处官方文本里说法不同（16.1）、官方文本没有给出断言（16.2）、以及必须同时读两句才不产生错误结论的组合（16.3）。16.4 汇总本篇"与流传口径相反"的条目。

## 16.1 口径冲突表 C1–C14

| 编号 | 冲突点 | 口径 A | 口径 B | 本篇采用 | 禁止的写法 |
| --- | --- | --- | --- | --- | --- |
| **C1** | "钥匙串是否受数据保护"在 macOS 上的含义 | 手册："文件和钥匙串数据保护类的密钥都在密钥包中进行收集和管理"（§2.4） | 手册 macOS 段："数据保护不直接用于实施此类保障"（§2.5） | 分平台各引各句，不合并为"Apple 用数据保护机制保护钥匙串" | "macOS 上文件与钥匙串同受数据保护约束" |
| **C2** | accessibility 档位的适用范围 | iOS 符号页与指南把七档作为通用属性（§3.2） | TN3137：数据保护实现"uses its own access control model"，file-based 用 `SecAccess`（§13.5） | 档位口径默认限 iOS/iPadOS 与 macOS 的数据保护实现；macOS 另列 ACL | 把 §3 结论无条件套到 macOS 任意 App |
| **C3** | "iCloud 钥匙串"是一个还是两个 | iOS 侧：单一钥匙串，同步域是它的属性（§11.12） | macOS 侧：显示层二选一（*iCloud Keychain* / *Local Items*），另有 file-based 的 login/System（§13.6） | iOS 表述为"逻辑包含"，macOS 表述为"多实现并列 + 显示切换" | "iPhone 上有两个钥匙串" |
| **C4** | `Always` 档到底是现行/不推荐/已终止 | 符号页仍列出该常量、正文无 "Deprecated"（§3.4） | 指南页写"不推荐"；availability 为闭区间 iOS 4.0.0–12.0.0 / macOS 10.9.0–10.14.0 | "官方已停止支持但页面保留，且系统内建项仍在'始终'档使用"（§3.7） | "Apple 从未提供该档"或"该档仍可用于第三方" |
| **C5** | APNs 设备令牌该不该保存 | 系统把它放在"始终、不可迁移"档（§3.7） | 对 App 的建议是 "Never cache the device token locally"（§10.3） | **两句并存**：主语不同（系统服务通道 vs App 侧），不构成矛盾但极易互证错误 | 用任一句否定另一句 |
| **C6** | 示例里的属性名是否存在 | `restricting-keychain-item-accessibility` 示例用 `kSecAttrAccessibleWhenPasscodeSet`（§5.2） | Accessibility Values 集合里只有 `…ThisDeviceOnly` 版本；该 slug 为 404 | 引用示例时带"该常量在官方集合页中无对应条目"限定 | 把它当作可用常量直接推荐 |
| **C7** | `kSecMatchTrustedOnly` 的布尔语义 | 键名字面暗示"只要受信任的" | 值描述的是"是否返回**不受信任**的证书"（§4.8） | 照引原句、按原句取反使用 | 按键名"合理化"改写 |
| **C8** | 生成 SEP 密钥该调哪个函数 | `kSecAttrTokenIDSecureEnclave` 页指向 `SecKeyGeneratePair(_:_:_:)`（被归入 Legacy iOS Key Operations） | `protecting-keys-with-the-secure-enclave` 用 `SecKeyCreateRandomKey(_:_:)`（§8.5） | **两处并列引用、不判定孰对**，实现口径以指南一文为准并注明差异 | 声称"官方明确规定只有 X 是正确的" |
| **C9** | `kLAPolicyDeviceOwnerAuthentication` 覆盖范围 | 该常量描述："User authentication with either biometry or the device passcode" | 同义枚举 case 的措辞含"或配对 Apple Watch"（§5.6） | 两处都引、不合并 | 只引其中一处并当作唯一定义 |
| **C10** | 验证码的时间窗 | iCloud 钥匙串验证码 SMS 路径：**three minutes**（§6.7） | TOTP 默认 `period`：**30 秒**（§7.10） | 两个窗口分别命名，不可互换 | "Apple 的验证码有效期是 3 分钟/30 秒"单一说法 |
| **C11** | PKCS#12 导入是否写钥匙串 | `SecKeychainItemImport`："adds them to a keychain" | `SecItemImport`："**optionally** adds them"（§8.8） | 以"是否写入取决于调用方后续动作"为共同口径，并保留措辞差异 | 混引两句声称"导入即入库" |
| **C12** | "证书"一词的双重角色 | TLS 体系：证书是**待评估对象**（§9.4） | 代码签名体系：证书是**授权介质**（签名身份/描述文件，归 #84/#85） | 第 9 章只覆盖前者并声明边界 | 用钥匙串里的证书解释 SIP/信任缓存 |
| **C13** | "令牌"指哪一个 | 四种互不通用的 token（§10.1） | 日常访问令牌在 §3.7 表中被当作普通钥匙串项 | 每次出现都带限定词（设备/会话/证明/寻址） | 无修饰地使用"iOS 令牌" |
| **C14** | `SecKeychainCreate` 的弃用版本 | TN3137："deprecated in the **macOS 12 SDK**"（§13.1） | 符号页 availability 为 `macOS: 10.2.0 - 10.10.0`，正文无 "Deprecated"（§12.6） | 采用 TN3137 的"macOS 12 SDK 标记废弃"，并加注 availability 不一致 | 同时断言"10.10 起不可用"与"macOS 12 才废弃" |

## 16.2 未披露 / 无一手页面支撑清单 N1–N12

下列各项**不是**本篇的推测被否定，而是官方文本里找不到可直接引用的断言。引用时必须带"限定语"列的说法。

| 编号 | 缺口 | 现有依据的层级 | 必须使用的限定语 |
| --- | --- | --- | --- |
| **N1** | developer 文档没有"系统侧共享库不含秘密"的直白句 | 手册为 A 级断言；文档侧靠"identity/credential 用词二分 + 属性表无秘密字段"推导（§7.6、§7.8） | "手册明确，文档侧为属性表推导" |
| **N2** | 官方未规定 App 收到 `ASPasswordCredential` 之后如何保存 | 只有 §6.1 的系统侧口径与 §3/§5 的通用机制 | "苹果没有规定 App 侧的保存方式，本篇给出的是可选落点" |
| **N3** | `SecAccessControlCreateFlags` 无 `device`/`admin` 类 flag；`companion` 成员无描述句 | 集合页成员清单（§5.3）；对应 slug 404 | "官方未提供该类 flag 的文档依据" |
| **N4** | `LAContext` 是否"解密"钥匙串项 | 文档从未如此表述；绑定由 `SecAccessControl` 承担（§5.7） | "LAContext 只评估、不解密（B 级推导）" |
| **N5** | 无 token/smart card 专项教程页 | 在线口径只有 `kSecAttrTokenID*` 三常量 + SEP 一文 + 物理安全密钥一文（§8.5、§7.2） | "旧 docs.apple PDF 有专章，developer 文档没有" |
| **N6** | 未开启 iCloud 钥匙串时自动强密码分支是否可用 | 官方建议句以"如果 iCloud 钥匙串已打开"为前提（§6.2） | "前提之外的分支官方未述" |
| **N7** | passkey 私钥的"不外泄"没有一句直白断言 | 属性表无私钥字段 + "you receive only the output of these operations"（§7.5、§8.3） | "两处独立表述合成，属 B 级" |
| **N8** | developer 文档没有根证书清单页，也没有解释系统 trust store 组成/更新机制的页面 | 只有 `SecTrustCopyAnchorCertificates(_:)` 与 `SecTrustSetAnchorCertificatesOnly(_:_:)` 两句（§9.5、§9.7） | "根证书清单属 www.apple.com 门户，不属 developer 文档" |
| **N9** | `URLCredential` 持久缓存落在哪个受保护落点 | 定义句只说明"由持久存储保存"，`forSession` 不跨启动（§9.10） | "不应称'URLSession 的凭据存在钥匙串'" |
| **N10** | `kSecAttrSyncViewHint` 的语义边界 | 单页仅给定义句（§11.6） | "官方未说明其用途与约束" |
| **N11** | 没有"哪些凭据类别一定同步/一定不同步"的完整清单；也没有远程擦除后各类凭据的逐项后果 | 版本分界 + tvOS 从不 + `ThisDeviceOnly` 互斥三句合成（§11.7–§11.8、§14.4） | "任何'一定/永不'的类别断言都需版本限定语" |
| **N12** | 没有"SEP 密钥不参与 iCloud 钥匙串同步"的直白断言 | 由 `kSecAttrTokenID` 的 Important（不可跨存储迁移）+ "only on the device that created it" 合成（§8.5、§11.14） | "本篇按推导处理并标注" |

**另有两项常被误当作已披露**：`ASWebAuthenticationSession` 的隐私模式**没有**"绝不落地 cookie"的绝对断言，官方措辞是 "asks the browser"（§10.8、§16.4 ⑪）；passkey 的**全平台可用性**也没有单一依据——示例页 availability 仅列 `Xcode: 14.0.0 - / iOS: 16.0.0 - / iPadOS: 16.0.0 - / macCatalyst: 16.0.0 -`，**无 macOS/tvOS/watchOS/visionOS 条目**，macOS 支持须另引相应符号页（§7.4）。

## 16.3 三组必须并读的概念簇

**簇一：iOS 行为 / 同步 / 仅限本设备（§4.7 + §11.5 + §3.6）**

| 句 | 官方原句 | 单独读会得出 | 并读后的口径 |
| --- | --- | --- | --- |
| 实现选择 | "Use `kSecUseDataProtectionKeychain` to **get the iOS behavior without synchronization**." | "这个键是开同步的" | 它只切换实现；同步由另一个键决定 |
| 同步选择 | "每个将要同步的项目都**必须**使用 `kSecAttrSynchronizable` 属性…" | "打了这个键就安全地上了云" | 该键同时是 macOS 上的实现切换手段（§13.2），且与 `ThisDeviceOnly` 互斥 |
| 迁移选择 | "items can be restored to the same device… **but isn't migrated** when restoring another device's backup" | "`ThisDeviceOnly` = 不同步" | 它管的是**恢复/迁移**，与同步是两个正交决定 |

**簇二：passkey / 安全隔区 / 外部安全密钥（§7.1 + §8.3–§8.5 + §7.2）**

| 句 | 官方原句 | 单独读会得出 | 并读后的口径 |
| --- | --- | --- | --- |
| passkey 落点 | "the device stores its public-private key pair in **the user's iCloud Keychain and syncs the keys**" | "passkey 是设备内的硬件凭据" | 它是**账户级、可同步**的软件凭据（除非改用物理安全密钥） |
| SEP 落点 | "you can access the associated keychain item **only on the device that created it**" | "SEP 也能同步，反正都在钥匙串里" | SEP 项天生不可迁移，且不可跨存储迁移（Important 句） |
| 安全密钥落点 | 手册与 `supporting-security-key-authentication-using-physical-keys`：**外部介质**，丢失即无法认证 | "安全密钥就是一种 passkey 实现细节" | 它是**第三方主体**（用户随身设备），改变的是"谁持有秘密"这一层 |

三句合起来才回答"passkey 到底在不在设备里"：**默认在 iCloud 钥匙串里并可同步；选安全隔区则只在单设备；选物理安全密钥则根本不在 Apple 设备上**。任何只引一句的版本都是错的（§16.4 ⑬）。

**簇三：证书入库 / 锚点搜索 / ATS 下限（§9.11 + §9.5 + §9.8）**

| 句 | 官方原句 | 单独读会得出 | 并读后的口径 |
| --- | --- | --- | --- |
| 入库 | 证书可作为 keychain 项存在，系统内建项按用途分档（§3.7、§9.11） | "装上证书就被信任" | 入库只解决"取得到" |
| 锚点 | "it **does not search the keychain for anchor (root) certificates**… you **must** call `SecTrustSetAnchorCertificates(_:_:)`" | "中间证书也要手工加" | 中间证书会被搜索，根证书不会 |
| 下限 | "**You cannot loosen** server trust requirements for an ATS-protected domain, **but you can tighten them**" | "自定义 challenge handler 可以任意放行" | ATS 域名上放松无效；pinning 场景须走手动评估（§9.9） |

## 16.4 与流传口径相反的条目（①–⑯）

| 编号 | 流传口径 | 官方口径（逐字要点） | 出处 |
| --- | --- | --- | --- |
| ① | "`SecPolicyCreateSSL` 的 `server` 参数表示'本机是服务器'" | "**Specify `true` on the client side** to return a policy for SSL server certificates."（且**只有两参**，三参重载 404） | §9.3 |
| ② | "DeviceCheck 令牌是稳定的设备标识，可以缓存复用" | "**An ephemeral token** that identifies the current device" / "you should **not use a token multiple times**"；服务端字段是"encrypted device information" | §10.4 |
| ③ | "APNs 设备令牌应当存进钥匙串以便下次上传" | "**Never cache the device token locally** on the user's device. Device tokens can change periodically…" | §10.3 |
| ④ | "用默认的可访问性档就足够安全" | 默认档是 `WhenUnlocked`，而"**A device without a passcode is considered to always be unlocked**"；同族另一档也写"or if the device does not have a passcode" | §3.3、§3.4 |
| ⑤ | "App Attest 要保存 attestation 对象；keyId 要放进钥匙串" | "**be sure to persistently store the key identifier — not the attestation object**"；"Record the identifier in persistent storage — **for example, by writing it to a file**"；私钥"from where **no process can ever directly read or modify it**" | §10.5 |
| ⑥ | "启用数据保护钥匙串 = 上 iCloud 同步" | "Use `kSecUseDataProtectionKeychain` to get the **iOS behavior without synchronization**"；同步只由 `kSecAttrSynchronizable` 触发 | §4.7、§11.5 |
| ⑦ | "把根证书装进钥匙串，系统就会信任它签发的证书" | "it **does not search the keychain for anchor (root) certificates.** To add an anchor certificate, you must call `SecTrustSetAnchorCertificates(_:_:)`." | §9.5 |
| ⑧ | "实现了自定义 challenge handler 就能自行决定是否接受证书" | ATS 保护域名上"**cannot loosen**…**can tighten**"；手册侧另有一句"无效的证书始终导致硬故障"（两句主语不同，须并读） | §9.8 |
| ⑨ | "`kSecUseAuthenticationUI` 已废弃，应该用 `kSecUseNoAuthenticationUI`" | 关系恰好相反：`kSecUseNoAuthenticationUI` 的 availability 是**闭区间 iOS 8.0.0–9.0.0**，`kSecUseAuthenticationUI`（iOS 9.0.0-）才是现行键 | §5.9 |
| ⑩ | "自动强密码默认就上 iCloud；任何 App 都能取用已存密码" | 手册口径为**默认本机保存、iCloud 同步是用户可选项**；建议只限关联域；访问已存密码必过认证（生物识别/口令）且需三方同意 | §6.2、§6.4、§6.5 |
| ⑪ | "隐私会话不保存任何 cookie；凭据迁移会导出一个可保存的文件" | 两句在官方文本里都是**请求级/无落盘**表述："This setting **asks the browser** to avoid using any existing browsing data…"、"**all cookies except session cookies are available**"（非隐私时）、迁移"**doesn't write any data to the file system**"、交接令牌是**一次性 UUID** | §10.8、§7.12 |
| ⑫ | "TOTP 种子必须由 App 或服务端保管" | 官方在线路径是"**iCloud Keychain synchronizes codes** across all of the user's devices"、设备"**generate verification codes entirely offline**"，App 只提供 `apple-otpauth:` 链接与 `oneTimeCode` 标记 | §7.10 |
| ⑬ | "passkey 锁在设备里不会同步 / 用 SEP 就能同步" | passkey 密钥对"**stores… in the user's iCloud Keychain and syncs**"；SEP 项"**only on the device that created it**"、"It **isn't possible** to migrate existing items between stores." | §7.1、§8.5、§11.14 |
| ⑭ | "Shared Web Credentials 是长期稳定的官方机制" | availability 已出现**终止版本**：`iOS: 8.0.0 - 26.2.0`、`macOS: 11.0.0 - 26.2.0`；新入口是 `ASCredentialExportManager` / `ASCredentialImportManager`（iOS 26+） | §6.13、§7.12 |
| ⑮ | "Apple 有一套叫 certificate pinning 的机制 / `ASPasskeyCredential` 类 / `AICredential*` 类 / token-smart card 专章 / developer 根证书清单页" | **这五个名字在 developer 文档里都不存在**：pinning 只在手动评估一文里以带引号的 "pin" 出现一次；类名分裂为 `ASPasskeyRegistrationCredential` 与 `ASPasskeyAssertionCredential`；扩展侧只有 `ASCredentialProviderViewController`/`ASCredentialProviderExtensionContext`；token/smart card 只有三个常量；根证书清单属 www.apple.com 门户 | §9.9、§7.5、§7.8、§16.2 N5、N8 |
| ⑯ | "keychain 是一个子系统，一套 API" | macOS 官方口径为**三套 API（Keychain / SecKeychain / SecItem）× 两种实现（file-based / data protection）**；iOS/tvOS/watchOS 只有 SecItem + 数据保护实现 | §13.1、§13.2 |

# 17. 结论与分角色建议

## 17.1 本篇能支持的结论（每条有 A 级原句）

1. **钥匙串是"一个数据库 + 两把密钥"**：元数据由表格密钥加密（受 SEP 保护但缓存在应用处理器），私密值由行独有密钥加密（始终往返 SEP），两者都是 AES-256-GCM。因此"可搜索"与"可读"是两个独立问题（§2.1–§2.3）。
2. **可访问性档的保护强度依赖设备状态，而设备状态里有一个开发者拿不到的前提**："A device without a passcode is considered to always be unlocked."——默认档在无密码设备上不提供额外保护（§3.3–§3.4）。
3. **`WhenPasscodeSetThisDeviceOnly` 是唯一一条官方给出"三条失效路径"的档**：不同步、不进托管密钥包、密码移除即丢弃类密钥（§3.5）。
4. **谁能读到一项凭据由"实现 + 组 + entitlement + 查询键"四层共同决定**，且写侧与读侧的错误表现不同（`errSecMissingEntitlement` vs `errSecItemNotFound`）（§4、§15.3）。
5. **"用户存在"是绑定在凭据上的对象（`SecAccessControl`），不是 App 的一次调用**；`LAContext` 只承担评估，且官方明确"已认证上下文可复用"（§5.7–§5.8）。
6. **passkey 默认存 iCloud 钥匙串并跨设备同步；SEP 密钥只在创建设备上且不可跨存储迁移**（§7.1、§8.5）。
7. **证书进钥匙串不等于被信任**：信任评估不搜索钥匙串里的根证书，锚点必须显式设置；ATS 域名上信任要求只能收紧（§9.5、§9.8）。
8. **"令牌"至少四类互不通用**，其中 APNs 设备令牌官方明令不要本地缓存，DeviceCheck 令牌要求一次性，App Attest 要求保存 keyId 而**不要**保存 attestation 对象（§10.1–§10.6）。
9. **iCloud 钥匙串的同步是设备侧自建信任圈的结果**：同步身份为非对称椭圆密钥（P-384）、信任圈需两次签名（另一次用派生自 iCloud 密码的 P-256）、Apple 服务器只承担传输；恢复侧有 SRP、HSM、10 次上限与销毁语义（§11.2–§11.10）。
10. **第三方 App 的项默认不同步**，且 Apple 只为 Safari 数据、Wi-Fi 密码、HomeKit 设置等类别显式设置该属性（§11.5）。
11. **钥匙串项能否迁移到新设备取决于用户是否设置备份口令**；备份密钥包未绑定设备，官方自己点出这一残余风险并把缓解系于口令强度（§12.2–§12.3）。
12. **macOS 上"选了哪套实现"决定第 3/5/8/11 章的结论是否适用**；守护进程只能使用 file-based，因此天生拿不到生物识别保护与 SEP 密钥（§13.2–§13.3）。
13. **凭据交接/迁移路径都不产生"可保存的明文文件"**：新机制用一次性 UUID 令牌且"doesn't write any data to the file system"（§7.12、§12.8）。
14. **官方正在收敛旧的 App↔Web 凭据共享路径**：`SecAddSharedWebCredential` 出现终止版本 26.2.0，多个认证 UI 相关键呈闭区间（§6.13、§5.9、§16.4 ⑭）。

## 17.2 本篇不能支持的结论（同样重要）

1. **不能支持"官方规定了 App 该怎么保存自己的访问令牌"**——developer 文档只规定"存则作为 generic password 项"，对上传与保存策略没有口径（§10.2、N2）。
2. **不能支持"U1（上传给自己的服务端）有系统级闸门"**——官方从未在该方向设置前置条件，责任完全在开发者代码里（§1.5、§14.2）。
3. **不能支持"把私钥/密码本体/TOTP 种子上传服务端是官方认可的做法"**——可上传一侧的官方范本只有公钥、一次性证明与寻址句柄（§10.9）。
4. **不能支持"启用了数据保护钥匙串就会同步"或"同步了就不能只在原设备"**——两句分别由 §4.7 与 §11.8 的互斥条款否定（簇一）。
5. **不能支持"Apple 能读到同步中的钥匙串项"或"Apple 一定能读到"**——前者有端对加密原句，后者缺少可引用的直白断言，正确做法是按落点与档位逐案判定（§11.1、N11）。
6. **不能支持"passkey 全平台可用"**——示例页 availability 无 macOS/tvOS/watchOS/visionOS 条目（§7.4、§16.2 补充段）。
7. **不能支持"证书 pinning 是 Apple 的一项命名机制"**——developer 文档没有该页面，只有一句带引号的场景描述（§9.9、⑮）。
8. **不能支持"`kSecAttrTokenID` 是 passkey 的实现方式"或"token/smart card 有官方教程"**——在线口径只有三个常量（§8.5–§8.6、N5）。
9. **不能支持"远程擦除/关闭同步后各落点凭据的逐项后果"**——官方无清单（N11）。
10. **不能支持"macOS 上的 accessibility 档位与 iOS 完全等价"**——实现分裂导致 ACL 模型不同（C2、§13.5）。
11. **不能支持任何"绕过某道门"的做法**——本篇范围仅为保管与上传机制的口径核实，不提供利用、绕过或提取凭据的步骤（§0.6）。

## 17.3 给 App 开发者的建议

**四条与"官方只给了一个组合"直接相关的判断**：

1. **按"是否可能被 Apple 读到 / 是否可能离开设备 / 是否随密码移除而失效"三问选档**。要让三项同时成立，官方文本里只剩 `WhenPasscodeSetThisDeviceOnly` 一档，且它必然随密码移除失效、必然不同步、必然不迁移——这是一个**取舍声明**，不是配置技巧（§3.5、§11.8）。
2. **把"同步"当作显式声明而不是隐式属性**：第三方项默认不同步；需要跨设备的凭据必须逐项设 `kSecAttrSynchronizable`，并且要知道它带来的限制（不能用 `ThisDeviceOnly`、不能用持久引用、不能配 `SecAccess`、查询键受限）（§11.5、§11.8）。
3. **不要保存官方明令不要保存的东西**：APNs 设备令牌即时交给服务端、不本地缓存；App Attest 的 attestation 对象不必存、keyId 需持久化（官方示例是写文件）；DeviceCheck 令牌按一次性使用（§10.3–§10.5）。
4. **把"不可观测事件"改为冷启动核对**：用户关闭同步、信任圈变化、备份口令缺失都不产生可读事件，只能在每次启动时按 §15.4 的清单验证凭据是否仍在、是否仍可解（§14.6）。
5. **macOS 目标必须显式选实现**，并检查是否运行在非用户上下文；把凭据封装进动态库不缩小可见域（§13.2–§13.5）。
6. **不要依赖被收敛的 API**：凡 availability 呈闭区间的键/函数，都应登记迁移计划（§13.8）。

## 17.4 给服务端与平台团队的建议

1. **服务端应存"标识"而非"秘密"**：passkey 只存公钥与 credential id；App Attest 存 keyId 与公钥（官方口径"not the attestation object"）；DeviceCheck 只消费短时效一次性令牌；不要要求客户端上传密码本体、TOTP 种子或私钥（§10.9、§7.1、⑤）。
2. **把令牌的时效性设计进协议**：官方对 DeviceCheck 是"single-use"，对 APNs 是"can change periodically"，对 passkey 断言是一次性 challenge——服务端不应假设任何设备侧句柄稳定可复用（§10.3–§10.4、§7.5）。
3. **管理"已认证上下文复用窗口"**：官方允许复用（"the operation will succeed without asking user"），这既是体验优化也是风险项，应由平台侧给出窗口策略（§5.8、§14.1）。
4. **写同步相关文案时带版本限定语**：类别覆盖以 iOS 14 / macOS 11 / watchOS 7 为界，tvOS 从不（§11.7、§11.8）。
5. **上传通道的信任要求按"只能收紧"设计**：ATS 域名上不要指望客户端放行策略能放松校验；需要严格校验走手动评估 + 显式锚点（§9.5、§9.8）。

## 17.5 给安全工程与产品侧的建议

1. **可直接采用的量化口径**：自动强密码默认 20 字符、约 **71 位熵**；TOTP 种子 **至少 160 位**、默认 6 位/30 秒；SMS 验证码窗口 **3 分钟**；App Attest challenge **不少于 16 字节**；恢复尝试**最多 10 次**（§6.2、§7.10、§6.7、§10.6、§11.10）。这些是少数官方直接给出的数值，适合作为指标基线。
2. **把"用户是否开启 iCloud 钥匙串恢复"当作风险项度量**：官方口径"这些密码的唯一记录位于钥匙串中"意味着恢复机制是可用性与安全的耦合点（§11.9）。
3. **密码监视的隐私设计可作参考范式**：只上传 **15 位前缀**、2^15 分桶、约 15 亿规模、NIST P-256 OPRF、随机补齐——"能检测泄露且不暴露完整密码"是一条可复用的构造口径（§6.9）。
4. **不要设计依赖"绕过同意"的流程**：手册明确不会把凭证透露给 App 除非用户同意，且访问已存密码必过认证（§6.5、§10）。

## 17.6 给审计与合规侧的建议

1. **审计三张表**：① 凭据→落点→档位→是否同步→是否可迁移（§1.3）；② 主体→动作→生效范围→可观测性（§14.6）；③ 防线自测清单的"是/否/未知"（§15.4）。
2. **必查的两个用户侧前提**：设备是否设有密码（否则默认档保护归零，§3.3）、备份是否设置口令（否则钥匙串项不迁移，§12.2）。
3. **两种组织形态要单独登记**："共享 iPad"模式下存在**不以用户密码为熵的凭据保护路径**（§2.4）；受管设备可限制某条凭据外发协议并推送 SSO 配置（§14.4）。
4. **报告口径合规**：任何"Apple 无法读取"的断言必须限定到具体机制与落点；任何"一定同步/永不同步"的断言必须带版本限定语（N11、C3）。
5. **工具证据不足**：macOS 的 Keychain Access **只显示数据保护钥匙串里的密码类项**，且官方承认刷新问题；不能以工具未显示作为"未保管"结论（§13.6）。

## 17.7 给研究者的下一步

1. **跟踪 availability 闭区间的收敛轨迹**：本期已出现 `26.2.0` 上界（§6.13）与多个 8.0.0–9.0.0 区间，是判断"官方正在拆哪条路"的最早可观测信号（§13.8）。
2. **优先补齐 N1–N12 里的两处**：系统侧 identity store 是否含秘密（N1 只有手册直白句）、以及凭据类别的同步完整清单（N11）。
3. **用 §16.1 的 C-表做反向验证**：每处两文本不一致，都是潜在的语义漂移点；TN3137（唯一自带修订日期的文档）是时间轴锚。
4. **抓取方法论可复用**：符号页必须驼峰、集合页大小写不稳定、下划线 slug 一律 404、指南页发布日期在页内且单页 API 文档无日期（附录 A.1、§13.8）。

# 附录

## A.1 来源与版次

**访问日期：2026 年 10 月 22 日。** 本篇一手来源只有两类：Apple《平台安全保护》手册（support.apple.com/zh-cn/guide/security，**每页正文自带"发布日期"**）与 developer.apple.com 文档（含 availability 元数据；**单页无发布日期，页脚统一为 Copyright © 2026 Apple Inc. All rights reserved.**）。**未使用搜索引擎类二手来源。**

### A.1.1 《平台安全保护》手册页（含各页正文标注的发布日期）

| 页面 | 发布日期 | secid（URL 段） | 本篇主要用途 |
| --- | --- | --- | --- |
| 钥匙串数据保护 | 2024-12-19 | `secb0694df1a` | §2.1–§2.3、§3.1（含两张官方表） |
| 用于数据保护的密钥包 | 2024-12-19 | `sec6483d5760` | §2.4、§12.1 |
| 加密和数据保护概览 | 2024-12-19 | `sece3bee0835` | 主题组参照页（正文就地标注具体页） |
| 数据保护概览 | 2024-12-19 | `secf6276da8a` | §89 期口径衔接、§3.1 |
| 数据保护类 | 2024-12-19 | `secb010e978a` | §3.1 对等映射 |
| 安全隔区 | 2024-12-19 | `sec59b0b31ff` | §2.3、§8.3–§8.4 |
| 密封密钥保护 (SKP) | 2021-05-17 | `secdc7c6c88e` | §8.12 |
| 在备选启动模式中保护密钥 | 2024-12-19 | `sece49ec4098` | 主题组参照页（§12.5 相邻口径） |
| 密码安全性概览 | 2024-12-19 | `sec3d3610acc` | §6.1 |
| 自动强密码 | 2024-12-19 | `secc84c811c4` | §6.2、§6.3 |
| 自动填充密码安全性 | 2024-12-19 | `sec7aefe77c3` | §6.4–§6.6 |
| App 访问已存密码的权限 | 2024-12-19 | `sec8762eb992` | §6.5 |
| 密码安全建议 | 2024-12-19 | `sec7f0432063` | §6.8 |
| 密码监视 | 2021-02-18 | `sec78e79fc3b` | §6.9（PPIA 构造） |
| 将密码发送给其他用户或 Apple 设备 | 2021-02-18 | `sec8839a8de2` | §6.10–§6.11 |
| 无线局域网密码共享安全性 | 2024-12-19 | `secd2015ef35` | §6.12 |
| 隔空投送安全性 | 2021-02-18 | `sec2261183f4` | §6.10 |
| 凭证提供程序扩展 | 2021-02-18 | `sec6319ac7b9` | §7.6–§7.7 |
| iCloud 钥匙串安全性概览 | 2024-12-19 | `sec1c89c6f3b` | §11.1 |
| 钥匙串安全同步 | 2024-05-07 | `sec0a319b35f` | §11.2–§11.5 |
| iCloud 钥匙串安全恢复 | 2022-05-13 | `secdeb202947` | §11.9 |
| iCloud 钥匙串的托管安全性 | 2024-05-07 | `sec3e341e75d` | §11.10 |
| iCloud 高级数据保护 | 2022-12-07 | `sec973254c5f` | §11.11 |
| iCloud 云备份安全性 | 2022-12-07 | `sec2c21e7f49` | 主题组参照页（§12 备份密钥包相关句以就地标注为准） |
| 卡片预置安全性概览 | 2024-12-19 | `sec0f005981a` | §8.10 |
| Apple 钱包中的证件 | 2026-01-28 | `secc50cff810` | §8.10 |
| Apple 钱包中证件的安全性 | 2024-05-07 | `secb569bf393` | §8.10（MSO 轮换） |
| 针对 Apple 设备的管理式设备证明 | 2026-01-28 | `sec8a37b4cb2` | §8.11 |
| 证明过程安全性 | 2026-01-28 | `sec97eb9e2f2` | §8.11 |
| TLS 安全性 | 2026-01-28 | `sec100a75d12` | §9.7–§9.8 |
| 信任缓存 | 2021-02-18 | `sec7d38fbf97` | §9.12 |
| 单点登录安全性 | 2026-01-28 | `sec0c87ccc6d` | §10.7 |
| Apple TV 的 HomeKit 安全性 | 2021-02-18 | `sec5d283419e` | §6.11 |
| 视控 ID、面容 ID、触控 ID 和密码 | 2024-12-19 | `sec9479035f1` | 主题组参照页（§5 引文取自 developer 文档符号页） |
| iPadOS 中的"共享 iPad"安全性 | 2021-02-18 | `secd99f373ef` | §2.4、§14.4 |
| 安全设备管理概览 | 2024-12-19 | `sec38eb8731b` | §14.4 |
| 受管理的丢失模式和远程擦除 | 2024-12-19 | `secc46f3562c` | 主题组参照页（§14.4 引文来自"安全设备管理概览"） |
| 文稿修订记录 | 最新条目 **2026 年 3 月** | `secb82d6b274` | 手册整体版次 |

**手册 URL 形式**：`https://support.apple.com/zh-cn/guide/security/<secid>/web`。标注为"主题组参照页"的行表示本篇未直接引用该页文字、仅用于登记同一主题组的版次；正文每一处引文以就地标注的页名为准。

### A.1.2 developer.apple.com 文档来源

| 主题组 | 代表页面 | 版次口径 |
| --- | --- | --- |
| Keychain Services 概览与项目 | `security/keychain-services`、`security/keychain-items`、`security/using-the-keychain-to-manage-user-secrets`、`security/adding-a-password-to-the-keychain` | 单页无发布日期，页脚 © 2026 |
| 属性与查询键 | `security/item-attribute-keys-and-values`、`security/search-attribute-keys-and-values`、`kSecAttrAccessible` 及 7 个 accessible 常量单页、`kSecAttrSynchronizable`、`kSecUseDataProtectionKeychain`、`kSecUseAuthenticationUI`/`…Context`/`…UIFail`/`…UISkip`/`kSecUseNoAuthenticationUI`、`kSecUseUserIndependentKeychain` | 同上（语义 + availability 逐字） |
| 访问组与 ACL | `security/sharing-access-to-keychain-items-among-a-collection-of-apps`、`bundleresources/entitlements/keychain-access-groups`、`security/access-control-lists`（macOS）、`security/SecAccessControl*` | 同上 |
| SecItem / SecKey / PKCS#12 | `security/SecItemAdd(_:_:)`、`security/SecKeyCreateRandomKey(_:_:)`、`security/SecPKCS12Import(_:_:_:)`、`security/SecItemImport(_:_:_:_:_:)`、`security/protecting-keys-with-the-secure-enclave`、`Security/kSecAttrTokenID` 系 | 同上 |
| 本地认证 | `localauthentication/LAContext`、`LAPolicy` 成员页 | 同上 |
| 证书与信任 | `Security/certificate-key-and-trust-services`（集合页，需大写形式）、`security/certificates`、`security/policies`、`security/trust`、`security/sectrustevaluatewitherror(_:_:)`、`security/sectrustsetanchorcertificates(_:_:)`、`security/secpolicycreatessl(_:_:)`、`security/trust-settings`（macOS）、`foundation/performing-manual-server-trust-authentication` | 同上 |
| AuthenticationServices | `authenticationservices/securing-logins-with-icloud-keychain-verification-codes`、`supporting-security-key-authentication-using-physical-keys`、`asauthorizationcredential`、`aspasskeyregistrationcredential`/`aspasskeyassertioncredential`/`aspasskeycredentialidentity`、`ascredentialproviderviewcontroller`、`ascredentialidentitystore`、`ascredentialupdater`、`ascredentialexportmanager`/`importmanager`、`aswebauthenticationsession` | 同上 |
| DeviceCheck / UIKit | `devicecheck/dcdevice/generatetoken(completionhandler:)`、`dcappattestservice/generatekey(completionhandler:)`、`uikit/uiapplicationdelegate/application(_:didregisterforremotenotificationswithdevicetoken:)` | 同上 |
| **技术说明** | **`technotes/tn3137-on-mac-keychains`** | **唯一自带修订日期：2026-09-24 / 2022-11-01 / 2021-12-10** |

**引用规则**：符号页 URL 用驼峰形式（如 `/documentation/Security/kSecAttrTokenIDSecureEnclave.md`）；文章与集合页 slug 为小写连字符，但**集合页大小写表现不稳定**（见 A.3）。

## A.2 术语表

| 术语 | 官方英文 | 本篇口径 |
| --- | --- | --- |
| 钥匙串 | keychain | "encrypted database" + `securityd` 系统代理；不是单一密文库（§2.1–§2.2） |
| 数据保护钥匙串 | data protection keychain | 起源于 iOS 的实现；macOS 上需显式选择（§13.1–§13.2） |
| 基于文件的钥匙串 | file-based keychain | macOS 传统实现，"on the road to deprecation"（§13.1） |
| 可访问性类 | accessibility class (`kSecAttrAccessible`) | 控制"设备状态能否满足读取条件"，不控制"谁能读"（§3） |
| 访问控制对象 | `SecAccessControl` | 绑定在项上的"用户存在"条件，创建时定型（§5.2–§5.5） |
| 钥匙串访问组 | keychain access group | 决定可读进程集合，Team ID 前缀只加在钥匙串组（§4.1） |
| 表格密钥 / 行独有密钥 | (手册未给英文名，逐句描述) | 前者加密元数据并缓存在应用处理器，后者加密 `kSecValueData` 且始终往返 SEP（§2.3） |
| 密钥包 | keybag（用户/设备/备份/托管/iCloud 云备份） | 钥匙串使用**用户密钥包**中的类密钥（§2.4、§12.1） |
| 同步身份 | sync identity（P-384） | 存在设备钥匙串；信任圈需双重签名（§11.2–§11.3） |
| 信任圈 | sync trust circle | 成员自担保或经恢复加入；Apple 不审批（§11.4） |
| 通行密钥 | passkey | 密钥对存 iCloud 钥匙串并同步；断言只返回签名（§7.1、§7.5） |
| 安全密钥 | (security key，物理) | 外部介质、丢失即无法认证、总是弹 UI（§7.2） |
| 验证码 | verification code (TOTP) | 种子由 iCloud 钥匙串保管、离线生成、`secret` ≥160 位（§7.10） |
| 凭证提供程序扩展 | Credential Provider Extension | target 类型；系统侧 store 只存 identity；关扩展即清库（§7.6–§7.8） |
| 锚点证书 | anchor (root) certificate | 不会被钥匙串自动采信，须显式设置（§9.5） |
| 证书固定 | "pin"（带引号的场景描述） | **developer 文档没有 pinning 专页**（§9.9、⑮） |
| 设备令牌 | device token (APNs) | 易变、即时交服务端、不得本地缓存（§10.3） |
| 证明密钥 | app attest key（SEP）/ 已证明密钥（设备证明） | 私钥不出 SEP；keyId 需持久化（§10.5、§8.11） |
| 一次性令牌 | `ASCredentialImportToken`（UUID） | 迁移交接用，不落盘（§7.12） |
| 受保护熵 | protected entropy | macOS 26.4 起 keychain 文件"可能"需要；官方声明"not considered API"（§12.5） |

## A.3 口径声明

1. **引文规则**：英文原句一律逐字引用并保留官方大小写与标点；中文引文取自手册简体中文页原文。翻译与解释一律与原文分列，不在引文内部改写。
2. **官方自身的文本问题保留原样并加注**：`security/trust` 分组标题 "Advanced Trust Configuation"（拼写错误）、个别页面的 "indicatd" / "sever"、部分符号页页末多余下划线 `__`；`kSecAttrTokenIDSecureEnclave` 页与 `protecting-keys-with-the-secure-enclave` 一文给出不同生成函数（C8）；`kLAPolicyDeviceOwnerAuthentication` 描述与枚举 case 措辞不一致（C9）。
3. **availability 只作版本存在性证据**：闭区间用于判定"已收敛"，缺失平台条目不用于判定"不可用"（§13.8）。凡以 availability 推出的结论均标注为口径而非功能断言。
4. **推导与直接断言分级**：B 级合成/推导一律就地标注（如 §5.7 的 "只评估不解密"、§7.5 的"无私钥字段"、§11.14 的"SEP 不参与同步"），并在 §16.2 的 N1–N12 登记缺口。**社区流传材料（C 级）与推测（D 级）不进入本篇任何表格与结论。**
5. **404 与不存在项清单**（本篇逐一实测，用于防止引用不存在的文章/符号名）：
   - `security/customizing-the-evaluation-of-a-server-certificate.md` → 404（证书链/锚点/策略正文分散在 §9.2 四处）
   - `security/trust-services.md` → 404（正确 slug 为 `security/trust`）
   - `security/trust-evaluation.md` → 404（无此集合页）
   - `security/secpolicycreatessl(_:_:_:).md` → 404（**只有两参重载**）
   - `security/creating-a-token-and-smart-card-enabled-key-pair.md` → 404（developer 无 token/smart card 专章）
   - `security/ksecattribletokenidsecureenclave.md` → 404（符号页须驼峰）
   - `security/certificate-pinning.md` → 404（**无 pinning 专页**）
   - `foundation/performing-manual-trust-evaluations.md` → 404（正确 slug `performing-manual-server-trust-authentication`）
   - `security/data-protection-keychain.md` → 404（权威口径在 TN3137）
   - `security/filevault.md` → 404（FileVault 不在 developer 文档口径内）
   - `authenticationservices/autofill-credentials-for-passwords.md` → 404（入口为 `security/password-autofill` 系）
   - `authenticationservices/aspasskeycredential.md` 与 `aspasskeycredentialassertion.md` → 404（类名分裂为两个，词序不同）
   - `authenticationservices/aicredentialproviderextension.md` → 404（**`AICredential*` 类不存在**）
   - `security/kSecAttrAccessibleWhenPasscodeSet.md`（示例用名）→ 404（集合中只有 `…ThisDeviceOnly` 版本）
   - `security/apple-root-certificate-authorities` 类页面 → 未见（根证书清单属 www.apple.com 门户）
   另有若干 slug 需按 identifier 大小写访问（`Security/certificate-key-and-trust-services` 大写返回 200、小写 404），故本篇把"大小写不稳定"记为抓取方法论事实（§13.8）。
6. **三处官方页面存在但无 Overview 正文**：`security/seckeychain`、`authenticationservices/asauthorizationcredential`、`ascredentialidentitystorestate`——相关结论标为部分核实。
7. **安全边界**：本篇只引用官方防御性表述、存储落点、配置要求与错误语义；**未写入任何攻击、绕过、提取凭据或规避保护的操作步骤**；§15.4 清单全部为"是否成立"的自查问句。
**联系方式**：Telegram: https://t.me/ios_one001







