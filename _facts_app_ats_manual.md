# 报告 #92 核查简报（Apple 手册页 / support / 设备管理侧）：iOS 应用网络连接的传输安全要求与上传机制分析

取证日期：2026-10-22。来源仅限 Apple 一手页面：《Apple 平台安全保护》zh-cn 手册（`support.apple.com/zh-cn/guide/security/<secid>/web`，逐页标注发布日期）、support.apple.com 帮助中心文章、developer.apple.com 的 App Store 审核指南与加密合规页（后者只有页脚 © 年份，记为“截至 2026 可用”）。
标注：**已核实**（逐字引文）/ **部分核实**（口径存在但措辞不完整）/ **未核实**（无可引页面 / 抓取失败）。
范围约定：developer.apple.com 的 ATS 键表、Network.framework、TN2287、URLSession 由另一位同事负责（见 `_facts_app_ats_dev.md`），本文件不重复。报告 #91 简报 `_facts_net_channel.md` 已逐字覆盖的页（TLS 安全性 `sec100a75d12`、网络安全性概览 `sec79afd0274`、服务安全性概览 `sec02518f1b`、随机数 `seca0c73a75b`、数字签名和加密 `secd0da50a09`、量子安全加密 `secc7c82e533`、iMessage `secd9764312f`/`sec70e68c949`、FaceTime `seca331c55cd`、隔空投送 `sec2261183f4`、接力 `secf78dbe639`、CloudKit `sec3cac31735`、iCloud 云备份 `sec2c21e7f49`、iCloud 专用代理 `secad8ce3233`、Wi-Fi `sec8a67fa93d`/`secfd166f620`、VPN `sec802e8ab55`、IPv6 `seccb625dcd9`）不重抄，仅在需要处给交叉引用。
撰写阶段禁止联网；未核实项在正文中必须写成缺口，不得当作结论。

---

## 0. 抓取方法与页面清单

方法：browser-use `navigate_page` 先落在目标域（`support.apple.com` 或 `developer.apple.com`），再用同源 `evaluate_script` 以 **XMLHttpRequest**（该域页面的 `arms-rum-browser.js` 会劫持 `fetch`，导致 `TypeError: Failed to fetch`）批量取其余页面；手册页读 `#article-section` + `h1` + 正文尾部“发布日期：…”，support 文章读 `time[itemprop=datePublished]`，developer 页读 `/tutorials/data/documentation/<path>.json` 的 `primaryContentSections` + `legalNotices.copyright`。所有 id 均取自实际 URL 或目录页链接，未编造。

本简报新抓取并逐字引用的主要页面（下表 18 个）：

| # | 页面 | URL | 发布日期 |
| --- | --- | --- | --- |
| 1 | 配置执行（手册） | `guide/security/secf6fb9f053/web` | 2024-05-07 |
| 2 | Apple 服务的锁定模式安全性（手册，目录页） | `guide/security/sec2437264f0/web` | 2024-05-07 |
| 3 | 关于“锁定模式”（support） | `zh-cn/105120` | 2026-09-18 |
| 4 | Apple 设备的无线局域网隐私（手册） | `guide/security/sec31e483abf/web` | 2024-12-19 |
| 5 | 关于 App Store 安全性（手册） | `guide/security/secb8f887a15/web` | 2024-12-19 |
| 6 | App Store Review Guidelines（英文原版） | `developer.apple.com/app-store/review/guidelines/` | Last Updated 2026-10-16；页脚 © 2026 |
| 7 | Complying with Encryption Export Regulations（英文） | `developer.apple.com/documentation/security/complying-with-encryption-export-regulations` | 页脚 © 2026 |
| 8 | iCloud 安全性概览（手册） | `guide/security/secacde2d0da/web` | 2022-12-07 |
| 9 | iCloud 高级数据保护（手册） | `guide/security/sec973254c5f/web` | 2022-12-07 |
| 10 | iCloud 数据安全概览（support） | `zh-cn/102651` | 2026-01-08 |
| 11 | 在企业网络上使用 Apple 产品（support） | `zh-cn/101555` | 2026-07-31 |
| 12 | 为 Apple 设备过滤内容（部署手册） | `guide/deployment/dep1129ff8d2/web` | 2025-09-24 |
| 13 | 使用 Apple 设备内建的网络安全功能（部署手册） | `guide/deployment/depb59c050ef/web` | 2024-10-30 |
| 14 | “证书透明度”有效负载设置（部署手册） | `guide/deployment/depbafaa79ff/web` | 2024-03-07 |
| 15 | “全局 HTTP 代理”有效负载设置（部署手册） | `guide/deployment/dep7ba46fcd/web` | 2022-10-24 |
| 16 | “网络代理配置”设置（部署手册） | `guide/deployment/depb27492e34/web` | 2022-10-24 |
| 17 | “DNS 设置”有效负载设置（部署手册） | `guide/deployment/dep86469ba99/web` | 2024-03-07 |
| 18 | “DNS 代理”有效负载设置（部署手册） | `guide/deployment/dep500f65271/web` | 2024-03-07 |

同批抓到的其余一手页（在本简报中按节引用）：《中继》有效负载 `dep131693e6b`（2024-03-07）、《在 Apple 设备上使用网络中继》`dep91a6e427d`（2023-10-25）、《“蜂窝专用网络”有效负载设置》`depbf9c1aabd`（2023-10-25）、watchOS 系统安全性 `secc7d85209d`（2026-01-28）、macOS 中的防火墙安全性 `seca0e83763f`（2021-02-18）、其他 macOS 系统安全性功能 `sec2fd2cc63d`（2021-02-18）、《让你的网络为 TLS 中的量子安全加密做好准备》`zh-cn/122756`（2026-06-25）、《iOS 13 和 macOS 10.15 中的可信证书应满足的要求》`zh-cn/103769`（无 `datePublished` 标记，页脚只有 © 2026，见 §10.1 说明）、developer 侧 Device Management 载荷页（`com.apple.management`／`com.apple.restrictions`／`com.apple.webcontent-filter`／`com.apple.proxy.http.global`／`com.apple.SystemConfiguration`，页脚 © 2026）、Foundation `FileProtectionType`／`URLFileProtection`／`FileAttributeKey.protectionKey`、CloudKit `CKAsset`、UserNotifications 的 APNs 三页（均 © 2026）。

目录级取证：`guide/security/toc` 共 178 条、`guide/deployment/toc` 共 220 条链接；已逐一核对标题，用于证明“某主题没有一手专页”（见 §1.5、§4.1、§9.2、§12）。

抓取失败 / 已放弃的 URL（照实记录，正文不得引）：
- `developer.apple.com/documentation/deviceconnection` → “The page you're looking for can't be found.”；`.../deviceconnection/establishing-a-connection-to-apns.json`、`.../deviceconnection/setting-up-a-remote-notification-server.json` 均 404。正确前缀是 `usernotifications`（已改抓，见 §10.2）。
- `developer.apple.com/documentation/usernotifications/authenticating-your-provider-with-apns` → 404（APNs 根证书必须是 “AAA Certificate Services / SHA-2 Root: USERTrust RSA” 这句因此未取到逐字，见 §12）。
- `support.apple.com/zh-cn/guide/deployment/dep7ba46fcd/web` 等四页在浏览器被并发切走域时 XHR 返回 `st:-1`（跨域），重新 `navigate_page` 到 `support.apple.com` 后全部 200。
- 共享浏览器被并发导航到 `security.apple.com/blog` 时，一次 DocC JSON 批量请求返回 3 个假 404；改为“先读 `location.hostname` 再决定是否发请求”后成功。

---

## 1. 设备管理侧：组织能对 App 连接施加什么

交叉引用：#91 简报已逐字取过《安全设备管理概览》`sec38eb8731b`（2024-12-19，见 `_facts_net_channel.md` 第 324 行）与《设备管理安全性概览》`sec013b5d35d`（2024-12-19，含 APNs 那句，第 325 行）；《VPN 安全性》`sec802e8ab55` 的 per-App VPN／始终打开 VPN／“组织可以监控并过滤设备上传入或传出的流量”在第 219–227 行。**本节只补上述未覆盖的条目。**

### 1.1 配置执行（手册 `secf6fb9f053`，发布日期 2024-05-07）

状态：**已核实**（整页逐字）

> 配置是设备管理服务在管理式设备上提供和管理策略和访问限制的主要方法。如果组织需要配置大量设备或者向大量设备提供许多自定义电子邮件设置、网络设置或证书，那么可以通过配置安全可靠地执行此类操作。

> 配置是一种遵循特定结构并由有效负载组成的 XML 描述文件或 json 格式文件，这些有效负载可将设置和授权信息载入到 Apple 设备上。配置可自动化设置、账户、访问限制和凭证的配置。此类文件可由设备管理服务或 Mac 版 Apple Configurator 创建，也可通过手动创建。在向 Apple 设备发送配置前，组织需要使用注册描述文件在设备管理服务中注册该设备。

> 注册描述文件是包含设备管理服务有效负载的配置，可在特定于设备的设备管理服务中注册该设备。这允许服务将命令和配置发送到设备以及查询设备的特定方面。用户移除注册描述文件后，基于该注册描述文件的所有配置、用户设置和受管理的 App（取决于注册类型和所用配置）都将随之移除。一台设备上一次只能有一个注册描述文件。

> 配置在特定的有效负载中包含多项可以指定的设置，包括（但不限于）：密码策略／针对设备功能的访问限制／**网络和 VPN 设置**／Microsoft Exchange 设置／邮件设置／账户设置／LDAP 目录服务设置／CalDAV 日历服务设置／凭证和身份／证书／软件更新

**描述文件自身的保护强度（逐字，可直接进“配置描述文件安全性”段）：**

> 可以对配置描述文件进行签名来验证其来源，以及对其进行加密来帮助确保其完整性并保护其内容。Apple 设备的配置描述文件使用 RFC 5652 中规定的“密码讯息语法” (CMS) 进行加密，CMS 支持 3DES 和 AES-128。

> 配置可通过设备管理服务或由用户手动操作来安装在设备上。或者，Mac 版 Apple Configurator 也可用于将配置部署到 iPad、iPhone 和 Apple TV 设备。部分配置需要使用设备管理服务进行安装。
> 【注】在受监督的设备上，还可以将配置描述文件锁定到设备。这旨在防止其移除，或者可以只允许使用密码将其移除。

口径要点：
- **“网络和 VPN 设置”与“证书”是描述文件正规载荷类别** —— 组织对连接的影响面被限定为“设置 + 凭证”，页面**没有任何“策略可改写 App 的连接要求”** 的表述。
- 描述文件加密只到 **3DES/AES-128（CMS, RFC 5652）**，且签名目的是**来源验证**；这是“管理通道”侧的强度基线，与 App 数据通道的 TLS 强度是两码事。

### 1.2 MDM 通道自身的传输要求（developer Device Management《MDM》载荷，© 2026）

来源：https://developer.apple.com/documentation/devicemanagement/mdm

> `ServerURL` :: The URL that the device contacts to retrieve device management instructions. The URL must begin with the `https://` URL scheme, and may contain a port number (`:1234`, for example). When updating the payload, the value of this key must not change. Any change is an error, and the system rejects the update.
> `CheckInURL` :: The URL that the device should use to check in during installation. The URL must begin with the `https://` URL scheme and may contain a port number (`:1234`, for example). If not set, the system uses `ServerURL`. When updating the payload, the value of this key must not change. Any change is an error, and the system rejects the update.
> `Topic` :: The topic that MDM listens to for push notifications. The certificate that the server uses to send push notifications must have the same topic in its subject. The topic must begin with the ‘com.apple.mgmt.’ prefix. When updating the payload, the value of this key must not change. Any change is an error, and the system rejects the update.
> `AccessRights` :: Logical OR of the following bit flags: `1`: Allow inspection of installed configuration profiles. `2`: Allow installation and removal of configuration profiles. `4`: Allow device lock and passcode removal. `8`: Allow device erase. `16`: Allow query of device information (device capacity, serial number). `32`: **Allow query of network information (phone/SIM numbers, MAC addresses).** `64`: Allow inspection of installed provisioning profiles. `128`: Allow installation and removal of provisioning profiles. `256`: Allow inspection of installed applications. `512`: Allow restriction-related queries. `1024`: Allow security-related queries. `2048`: Allow manipulation of settings. `4096`: A…

口径要点：
- **设备管理协议本身被硬性要求 `https://`**，且三个关键值（ServerURL/CheckInURL/Topic）**更新时不可变**，改动即被系统拒绝 —— 这是“管理通道不能被静默改址”的一手证据（可反驳“描述文件可被中间人重定向”的说法）。
- MDM 推送证书的主题必须带 `com.apple.mgmt.` 前缀 → 通道与服务商绑定，可作自查项。
- `AccessRights` 位 `32` = 允许查询**网络信息（电话/SIM 号、MAC 地址）** → 组织可获得设备网络标识，属“可见性”而非“明文内容”。（原文位 4096 起被页面截断，引用时注明。）

### 1.3 网络可达性与蜂窝（Restrictions／NetworkUsageRules／VPN 载荷）

来源：https://developer.apple.com/documentation/devicemanagement/restrictions （210 个键；© 2026）

> `allowUntrustedTLSPrompt` :: If `false`, the system automatically rejects untrusted HTTPS certificates without prompting the user. Available: iOS 5+ | iPadOS 5+ | visionOS 1.1+
> `allowOTAPKIUpdates` :: If `false`, the system disables over-the-air PKI updates. **Setting this restriction to `false` doesn’t disable CRL and OCSP checks.** Available: iOS 7+ | iPadOS 7+
> `forceAirPrintTrustedTLSRequirement` :: If `true`, the system requires trusted certificates for TLS printing communication. Available: iOS 11+ | iPadOS 11+ Requires supervision: iOS
> `allowVPNCreation` :: If `false`, the system allows only managed apps to create VPN configurations. Prior to iOS 18, the system also allows unmanaged apps to create VPN configurations. Available: iOS 11+ | iPadOS 11+ | visionOS 2+ Requires supervision: iOS | visionOS
> `forceWiFiToAllowedNetworksOnly` :: If `true`, the system limits the device to only join Wi-Fi networks set up through a configuration profile. Available: iOS 14.5+ | iPadOS 14.5+ | visionOS 2+ Requires supervision: iOS | visionOS
> `forceWiFiWhitelisting` :: Use `forceWiFiToAllowedNetworksOnly` instead. Available: iOS 10.3+ | iPadOS 10.3+ Deprecated: iOS 14.5+ | iPadOS 14.5+
> `allowAppCellularDataModification` :: If `false`, the system disables changing settings for cellular data usage for apps. Available: iOS 7+ | iPadOS 7+ Requires supervision: iOS
> `allowGlobalBackgroundFetchWhenRoaming` :: If `false`, the system disables global background fetch activity when an iOS phone is roaming. Support for this restriction on unsupervised devices is deprecated. Available: iOS 4+ | iPadOS 4+
> `allowSatelliteConnection` :: If `false`, the system prohibits the connection to and use of satellite services. Available: iOS 18.2+ | iPadOS 18.2+ Requires supervision: iOS
> `deniedICCIDsForiMessageFaceTime` :: An array of strings representing ICCIDs of cellular plans. The device prevents use of any matching cellular networks in iMessage and FaceTime. The array must contain no more than 4 ICCID strings. Available: iOS 26+ | iPadOS 26+ Requires supervision: iOS
> `allowAirDrop` :: If `false`, the system disables AirDrop. Available: iOS 7+ | iPadOS 7+ | macOS 10.13+ | visionOS 2+ Requires supervision: iOS | visionOS
> `allowHostPairing` :: If `false`, the system disables host pairing with the exception of the supervision host. If there’s no configured supervision host certificate, the system disables all pairing. … Available: iOS 7+ | iPadOS 7+ Requires supervision: iOS
> `safariAllowJavaScript` :: If `false`, Safari doesn’t execute JavaScript. This restriction will require supervision in a future release. Available: iOS 4+ | iPadOS 4+

**iCloud 上传目的地开关（Restrictions，逐字）** —— 组织可关闭 App 数据上行：

> `allowCloudDocumentSync` :: If `false`, the system disables document and key-value syncing to iCloud. Requires a supervised device in iOS 13 and later, and Shared iPad doesn’t support it. Support for this restriction on unsupervised devices and with Managed Apple Accounts is deprecated. Available: iOS 5+ | iPadOS 5+ | macOS 10.11+ | visionOS 2+ Requires supervision: iOS | visionOS
> `allowManagedAppsCloudSync` :: If `false`, the system prevents managed apps from using iCloud sync. Available: iOS 8+ | iPadOS 8+ | visionOS 2+ Allowed for user enrollments: iOS | visionOS
> `allowCloudBackup` :: If `false`, the system disables backing up the device to iCloud. Support for this restriction on unsupervised devices is deprecated. Available: iOS 5+ | iPadOS 5+ | visionOS 2+
> `allowCloudKeychainSync` :: If `false`, the system disables iCloud Keychain synchronization. Support for this restriction on unsupervised devices and with Managed Apple Accounts is deprecated. Available: iOS 7+ | iPadOS 7+ | macOS 10.12+ | visionOS 2+
> `allowCloudPrivateRelay` :: If `false`, the system disables iCloud Private Relay. Support for this restriction on unsupervised devices and with Managed Apple Accounts is deprecated. Available: iOS 15+ | iPadOS 15+ | macOS 12+ | visionOS 2+ Requires supervision: iOS | visionOS
> `allowCloudDesktopAndDocuments` :: If `false`, the system disables iCloud Desktop and Document services. Available: macOS 10.12.4+
> `allowContentCaching` :: If `false`, the system disables content caching. This restriction is not supported on the user channel. Available: macOS 10.13+
> `allowOpenFromManagedToUnmanaged` :: If `false`, documents in managed apps and accounts open only in other managed apps and accounts. Available: iOS 7+ | iPadOS 7+ | visionOS 2+ Allowed for user enrollments: iOS | visionOS
> `allowOpenFromUnmanagedToManaged` :: If `false`, documents in unmanaged apps and accounts open only in other unmanaged apps and accounts. …
> `requireManagedPasteboard` :: If `true`, the `allowOpenFromManagedToUnmanaged` and `allowOpenFromUnmanagedToManaged` restrictions also limit copy-and-paste functionality. Available: iOS 15+ | iPadOS 15+ | visionOS 2+ Allowed for user enrollments: iOS | visionOS

per-App VPN 与“哪张 SIM 允许哪个 App 联网”（逐字）：

> `OnDemandMatchAppEnabled` :: If `true`, automatically connects the VPN when associated apps for this per-app VPN service initiate network communication. Otherwise, the user must initiate the connection manually before those apps can initiate network communication. If this key isn’t present, the value of the `OnDemandEnabled` key determines the status of per-app VPN On Demand. （https://developer.apple.com/documentation/devicemanagement/applayerVPN）
> `AssociatedDomains` :: An array with entries that must each specify a domain that triggers this VPN. The domains must also be part of the `apple-app-site-association` file… Available: iOS 14+ | iPadOS 14+ | macOS 11+ | visionOS 1.1+ | watchOS 10+
> `ExcludedDomains` :: An array with entries that each specify a domain that doesn’t trigger this VPN for connections to the domain. …
> `com.apple.vpn.managed.applayer` :: This profile defines per-app VPN behavior and applies only to VPN services of type `VPN`, `IPsec`, and `IKEv2`. All the properties of VPN apply to the top level of this profile as well.
> `ApplicationRules`（NetworkUsageRules）:: An array of application rules, that apply to only managed apps. ／ `SIMRules` :: An array of SIM rules, that apply to all apps. Available: iOS 13+ | iPadOS 13+
> `com.apple.networkusagerules` :: Network usage rules allow enterprises to specify how devices use networks, such as cellular data networks. iOS 9-12 require the application rules. In iOS 13, application rules, SIM rules, or both must be present.
> `AlwaysOn`／`Proxies`／`TransparentProxy`（VPN 载荷）:: The dictionary to use when `VPNType` is `AlwaysOn`. Not available in tvOS or watchOS. ／ The dictionary to use to configure `Proxies` for use with `VPN`. Available: iOS 4+ … watchOS 10+ ／ The dictionary to use when `VPNType` is `TransparentProxy`. Available: macOS 14+

网络中继载荷（**第三条组织侧路径，非 VPN 非代理**，逐字）：

> Specify `com.apple.relay.managed` as the payload type.（https://developer.apple.com/documentation/devicemanagement/relay）
> `RelayUUID` :: A globally unique identifier for this relay configuration. The system uses this UUID to route managed apps through the servers in `Relays`. This key is required for user enrollment. Available: iOS 17+ | iPadOS 17+ | tvOS 17+ | visionOS 1+ Deprecated: iOS 27+ | iPadOS 27+ | macOS 27+ | visionOS 27+
> `Relays` :: An array of dictionaries that describe one or more relay servers that the system can chain together. Deprecated: iOS 27+ | iPadOS 27+ | macOS 27+ | visionOS 27+
> `UIToggleEnabled` :: If `true`, the device allows the user to disable this network relay configuration. Available: iOS 26+ …
> `AllowDNSFailover` :: If `true`, the device allows the relay to failover to the default system DNS resolver. …

### 1.4 证书信任侧：组织可“降低”证书透明度要求（唯一一条官方放宽 TLS 信任要求的路径）

状态：**已核实**（逐字）《针对 Apple 设备的“证书透明度”设备管理有效负载设置》 https://support.apple.com/zh-cn/guide/deployment/depbafaa79ff/web ，发布日期 **2024-03-07**

> 使用“证书透明度”有效负载为 Apple 设备用户控制“证书透明度”实施的行为。此自定义有效负载不需要设备管理服务，也不要求设备的序列号在 Apple 校园教务管理或 Apple 商务中显示。
> iOS、iPadOS、macOS、Apple tvOS、watchOS 10 和 visionOS 1.1 具有“证书透明度”要求，以使 TLS 证书得到信任。“证书透明度”涉及将服务器的公开证书提交到对外公开的日志中。如果使用仅限内部服务器的证书，你可能无法显示这些服务器并由此无法使用“证书透明度”。因此，“证书透明度”要求会导致用户的证书信任失败。
> **此有效负载允许设备管理员有选择地为内部域和服务器降低“证书透明度”要求，以避免与内部服务器通信的设备发生此类信任失败。**
> 支持的有效负载标识符：com.apple.security.certificatetransparency
> 支持的操作系统和通道：iOS、iPadOS、“共享 iPad”设备、macOS 设备、Apple tvOS、watchOS 10、visionOS 1.1。
> 支持的注册方式：用户注册、设备注册、自动设备注册。
> 允许的重复项：True：可将多个“证书透明度”有效负载发送给某台设备。

设置表（逐字）：

> 为特定证书停用“证书透明度”实施 — 选择此选项以通过停用“证书透明度”实施来允许私有的不受信任证书。要停用的证书需要包含：(1) 签发者用于为证书签名的算法；(2) 与证书签发对象的身份关联的公共密钥。
> 算法 — 签发者用于为证书签名的算法。值需为“sha256”。
> subjectPublicKeyInfo 的哈希值 — 与证书签发对象的身份关联的公共密钥。
> 停用特定域 — 证书透明度被停用的域列表。前导句点可用于匹配子域，但域匹配规则无法匹配一个顶层域内的所有域。（不允许“.com”和“.co.uk”，但允许“.melardclothing.com”和“.melardclothing.co.uk”）。
> 【注】每个设备管理服务开发者对这些设置的执行情况不同。若要了解各种“证书透明度”设置如何应用到你的设备，请参阅开发者的设备管理服务文稿。

英文载荷页（https://developer.apple.com/documentation/devicemanagement/certificatetransparency ，© 2026）示例键：`DisabledForCerts`（`Algorithm` = `sha256` + `Hash`）、`DisabledForDomains`（`example.com`）；载荷类型 `com.apple.security.certificatetransparency`；Device channel：iOS, macOS, Shared iPad, tvOS, visionOS, watchOS；**Requires supervision：N/A**（不需要监督）。

口径要点：
- 这是 Apple 官方文档中**唯一明确写“允许管理员降低 TLS 证书相关安全要求”**的机制；且它降低的是 **CT 强制**（对应 #91 §1.1 的 iOS 12.1 CT 规则与 ATS 的 `NSRequiresCertificateTransparency`），**不是** ATS 的 TLS 版本/密码套件/密钥长度要求。
- **“此自定义有效负载不需要设备管理服务，也不要求设备的序列号…”** + **Requires supervision: N/A** + **“支持的注册方式：用户注册、设备注册、自动设备注册”** → 用户级注册（BYOD）也能下发，覆盖面比多数载荷大。
- 域匹配规则限制（禁 `.com`／允许 `.melardclothing.com`）= 放宽范围被刻意限定向具体域，可写进“最小化”口径。

### 1.5 关键问题：配置描述文件能否放宽或覆盖 ATS？

状态：**已核实（负向取证）**。方法：取 developer Device Management 载荷目录全量清单（https://developer.apple.com/documentation/devicemanagement/profile-specific-payload-keys ，© 2026）与 Restrictions 载荷**全部 210 个键名**，逐一检索 `ATS`／`transport`／`NSAppTransportSecurity` → **无任何键或载荷**。目录分组为：General／Accounts／AirPlay／App management／App Store／Apple TV／Authentication／Certificates／Ethernet／FileVault／Login／Mail／Managed devices／Media management／**Networking**（Cellular、CellularPrivateNetwork、ContentCachingService、DNSSettings、Domains、Firewall、NetworkUsageRules、Relay、WiFi、WiFiManagedSettings）／Parental controls／Preferences／Printing／Privacy／**Proxies**（DNSProxy、GlobalHTTPProxy、NetworkProxyConfiguration）／Restrictions／Security（Passcode、SecurityPreferences、SmartCard）／System configuration（含 FileProvider）／System policy／System migration／User experience／**VPN**（AppLayerVPN、AppToAppLayerVPNMapping、VPN）／Web（WebClip、WebContentFilter）／Xsan／Deprecated。

→ 结论口径（可写正文）：
- **能做的（组织侧）**：改**网络可达性与路径**（VPN/per-app VPN/AlwaysOn/Relay/代理/DNS/允许网络列表）、改**信任锚与撤销行为**（安装根证书、`CertificateTransparency` 载荷对内部域降低 CT、`allowUntrustedTLSPrompt=false` 让不受信证书**直接拒绝**、`allowOTAPKIUpdates=false` 关 OTA PKI 更新但**不影响 CRL/OCSP**）、改**上传目的地**（`allowCloudDocumentSync`/`allowManagedAppsCloudSync`/`allowCloudBackup`/`allowCloudKeychainSync`/`allowCloudPrivateRelay`）、改**流量可见性**（内容过滤 `FilterSockets`/`FilterBrowsers`/`FilterPackets`/URL 过滤）、限制非 HTTP App 与配对（`deniedICCIDsForiMessageFaceTime`、`allowAirDrop`、`allowPairedWatch`、`allowHostPairing`）。
- **不能做的**：**没有任何载荷或键可以放宽 App Transport Security 的 TLS 版本/前向保密/密钥长度/证书签名算法要求，也没有载荷可按域关闭 ATS。** 页面里唯一“降低要求”的官方措辞是证书透明度（§1.4），它只影响 CT 检查；802.1X 可设 min/max TLS 版本（《使用 Apple 设备内建的网络安全功能》：> 你还可以通过 EAP-TLS、EAP-TTLS、PEAP 和 EAP-FAST 给 802.1X 网络有效负载设定最低和最高 TLS 版本。**例如，你可以设定：最低和最高均为相同的特定 TLS 版本**）但那是**网络准入认证链路**，不是 App 出站连接。
- 组织能**收紧**（`allowUntrustedTLSPrompt=false` 去掉了用户“仍要连接”的逃生口），能**旁路观察**（代理/内容过滤），能**断掉目的地**（iCloud 开关），唯独**不能替 App 决定是否满足 ATS** —— 决定权在 App 自身的 `Info.plist`（同事侧键表）与运行时（锁定模式，§2）。
- 交叉引用：#91 §1.2 第 36 行“不满足这些要求的网络连接会失败，**除非 App 重写了 App 传输安全性**”——重写主体被官方文本限定为 App，而非描述文件。

---

## 2. 锁定模式：运行环境改变连接要求

取证说明：手册《Apple 服务的锁定模式安全性》（`sec2437264f0`，发布日期 2024-05-07）只是一份**目录页**，逐字全文如下（无逐条限制细节）：

> 锁定模式是一项可选的极端保护模式，专为极少数个人用户设计，这些人可能会由于他们的身份或工作而被一些最复杂的数字威胁所针对，如针对性雇佣间谍软件。大多数人从不会成为这类攻击的目标。
> 打开锁定模式时，设备不会像平时一样正常工作。为减小潜在可能被利用的攻击面，某些 App、网站和功能会受到严格限制以保证安全性，部分体验可能根本不可用。
> 锁定模式在 iOS 16、iPadOS 16、macOS 13、watchOS 10 或更高版本中可用。iOS 17、iPadOS 17、macOS 14 和 watchOS 10.1 更新或更高版本中提供了额外的保护。为利用锁定模式的更多功能，设备应更新到最新的操作系统。有关更多信息，请参阅 Apple 支持文章：关于“锁定模式”。
> 锁定模式权衡措施以牺牲功能和/或性能换取安全性的提高。这些权衡措施会影响：后台服务／连接性／设备管理／FaceTime 通话／GameCenter／邮件／信息／照片／Safari 浏览器／系统设置／WebKit

状态：**已核实**（逐字，来自 support 文章《关于“锁定模式”》 https://support.apple.com/zh-cn/105120 ，发布日期 2026-09-18）

限制条目原文（与网络连接/上传直接相关的逐条照抄）：

> 信息：除某些图像、视频和音频外，大多数信息附件类型都会被阻止。一些功能（如链接和链接预览）无法使用。

> 网页浏览：某些复杂的网页技术会被阻止，这可能会导致一些网站载入速度变慢或者无法正常运行。此外，网页字体可能无法显示，图像可能会被图像缺失图标替代。

> FaceTime 通话：FaceTime 通话的来电会被阻止，除非你在过去 30 天内曾呼叫过对方。“同播共享”和“实况照片”等功能无法使用。

> Apple 服务：Apple 服务的传入邀请（例如邀请你在“家庭”App 中管理家庭）会被阻止，除非你曾邀请过对方。专注模式和任何相关状态将无法正常使用。Game Center 也会被停用。

> 设备连接：要将你的 iPhone 或 iPad 连接到配件或另一台电脑，需要解锁设备。要将搭载 Apple 芯片的 Mac 笔记本电脑连接到配件，你需要解锁 Mac 并提供明确的批准。

> 无线连接：打开“锁定模式”后，你的设备将不会自动加入不安全的无线局域网，并且会断开已连接的不安全无线局域网。iPhone 和 iPad 的 2G 和 3G 蜂窝网络支持会关闭。

> 配置描述文件：在“锁定模式”下，无法安装配置描述文件，并且无法在移动设备管理或设备监督中注册设备。

> 打开“锁定模式”时，电话通话和纯文本信息可以继续工作，但是来电不会在配对的 Apple Watch 上响铃。紧急联络功能（例如 SOS 紧急呼叫）不受影响。

「配置描述文件和受管理的设备」整节逐字（这是组织侧与锁定模式关系的唯一一手口径）：

> 如果设备处于“锁定模式”，则无法安装新的配置描述文件，也无法在移动设备管理或设备监督中注册设备。如果用户想安装配置描述文件或管理描述文件，则需要关闭“锁定模式”，安装相应描述文件，然后在必要时重新启用“锁定模式”。这些限制可以防止攻击者尝试安装恶意配置文件。
> 如果启用“锁定模式”之前在移动设备管理中注册了设备，则这台设备仍处于受管理的状态。系统管理员可以在这台设备上安装和移除配置描述文件。
> “锁定模式”专为可能会遭受极端网络攻击的极少数个人用户设计，因此并不是一个可由系统管理员配置的移动设备管理选项。

「排除」机制逐字（说明 WebKit 限制可按站点/按 App 解除）：

> 排除某个网站、App 或联系人会削弱“锁定模式”的保护效果，可能提升你的安全风险。请仅排除受信任的网站、App 或联系人，且仅在必要时进行这个操作。仅当“锁定模式”处于开启状态时，这些选项才会显示。

> 当你在 Safari 浏览器中排除某个网站后，相关网站将不再受 WebKit 相关限制的约束与影响。

> 当你的设备处于锁定模式时，iOS 和 iPadOS 上的 App 会出于安全考虑受到严格限制，部分体验将完全不可用。你可以在 iOS 或 iPadOS 上排除某个 App，以便移除这个 App 的网页内容限制。

> 这个列表中仅显示自启用“锁定模式”以来打开过且功能受限的 App。

「排除联系人」逐字（要求 iOS 27/iPadOS 27/macOS 27 或更高版本）：

> 当你将某个联系人从“锁定模式”中排除时，这个联系人的信息限制会被关闭。
> 如果你从排除列表中移除某个联系人，信息限制会自动恢复。如果联系人的电话号码或电子邮件地址发生变更，则信息限制会自动恢复，并且你会收到通知。

口径要点（写正文时用）：
- 锁定模式是**运行环境改变出站连接要求**的直接证据：不加入/主动断开“不安全的无线局域网”、关闭 2G/3G 蜂窝支持 = 物理层/链路层的可用性等于是设备状态的函数。
- 对 App 开发者最相关的一条：**“你可以在 iOS 或 iPadOS 上排除某个 App，以便移除这个 App 的网页内容限制”** —— 受限的是 **WebKit 网页内容渲染路径**，不是 ATS/TLS 本身；正文不得把锁定模式写成“改 TLS 版本要求”。
- 锁定模式**阻断描述文件安装与 MDM 注册**，但**不撤销既有管理状态**，管理员仍可增删描述文件；且它不是 MDM 可下发/可配置的选项 → 组织侧无法强制员工开或关锁定模式。
- 附件方向：锁定模式在**接收侧**阻止大多数信息附件类型（属“入站附件解析面”），与“上传”无关，引用时不要混。
- 版本口径注意：本页出现的“iOS 27 / macOS 27 / OS 27”是 2026 年当前版本线，手册页正文仍写 iOS 16/17 起，两者并存时以 support 文章 2026-09-18 版本为准。

### 2.2 support.apple.com 锁定模式文章

见 §2.1（同一条引文来源 `https://support.apple.com/zh-cn/105120`，发布日期 2026-09-18）。

---

## 3. 网络标识与隐私（私有无线局域网地址）

状态：**已核实**（逐字，手册《Apple 设备的无线局域网隐私》`sec31e483abf`，发布日期 2024-12-19）
（与 #91 §6.2 `secfd166f620`、§7 待补的 `secb9cb3140c` 不同页，本页是“私有地址 + 隐藏网络 + TSF 随机化”。）

> Apple 设备拥有旨在协助设备保持私密无线局域网连接的功能。

私有无线局域网地址（逐字）：

> 运行 iOS 14、iPadOS 14、macOS 14、visionOS 1、watchOS 7 或更高版本的设备接入无线局域网时，这类设备可通过唯一且随机的无线局域网 (MAC) 地址标识自己。这可协助减少设备跟踪和增强用户隐私。
> 【注】此功能可通过设备管理服务关闭（需要 visionOS 1.1 或更高版本）。如果关闭该功能，操作系统会在“设置”中显示隐私警告，表示该网络的隐私保护措施已降低。
> 在运行 iOS 18、iPadOS 18、macOS 15、visionOS 2、watchOS 11 或更高版本的设备上，私有无线局域网地址功能得以更新，支持以下三种运行模式，各模式可基于每个网络进行选择：
> - 关闭：使用设备的硬件无线局域网地址，允许网络和附近无线局域网设备跟踪。
> - 固定：固定私有地址会在网络上使用唯一的无线局域网地址减少跨网络跟踪。可作为默认选项用于安全网络连接，例如 WPA2 个人级、WPA2 企业级、WPA3 个人级、WPA3 企业级、WPA3 企业级 192 位安全模式和 R3 WPA3 个人级。
> - 轮替：轮替私有地址会定期更改此设备在网络上的无线局域网地址以减少跟踪。可作为默认选项用于使用较弱认证或加密方法的连接，例如 WPA、OWE、WEP、受限门户和开放性网络。

隐藏网络（逐字）：

> 无线局域网由其网络名称（即服务集标识符 (SSID)）标识。部分无线局域网被配置为隐藏其 SSID，导致无线接入点不广播网络的名称。这些网络称为隐藏网络。Apple 设备会在网络隐藏时自动检测。如果网络已隐藏，设备会发送包括 SSID 的探测请求，否则不会发送。这可帮助防止设备广播用户之前接入过的隐藏网络的名称，从而进一步保证了隐私。

个人热点/互联网共享/P2P 随机地址 + TSF 偏移量随机化（逐字）：

> Apple 为用于隔空投送和隔空播放的点对点无线局域网连接生成随机 MAC 地址。随机地址还用于 iOS 和 iPadOS（带 SIM 卡）中的个人热点，以及 macOS 中的互联网共享。每次建立这些网络接口均会生成新的随机地址，且会根据需要为每个接口独立生成唯一的地址。
> 为了提高用户隐私和减少设备指纹记录影响，Apple 设备会为所有点对点和接入点模式随机化定时同步功能 (TSF) 的起始偏移量。此 56 位随机偏移量有助于防止攻击者使用 TSF 值破解 MAC 地址随机化。只要接口 MAC 地址发生改变，接口的 TSF 偏移量就会随机化。运行 iOS 15、iPadOS 15、macOS 12.0.1、Apple tvOS 15、visionOS 1 或更高版本的以下设备增加了对 TSF 偏移量随机化的支持：

量化基线：私有地址三档模式（关闭/固定/轮替）自 iOS 18 起**按网络逐个选择**；**“固定”档与网络加密强度绑定**（WPA2 及以上）；**“轮替”档是弱认证/开放网络的默认**（WPA、OWE、WEP、受限门户、开放性网络）；TSF 偏移量 56 位随机。
口径要点：
- **“此功能可通过设备管理服务关闭”** = 组织能让设备回退到硬件 MAC（可被网络侧跟踪），并伴随系统级隐私警告；这是“组织侧能改变设备网络标识”的一条实证，与 §1 组织侧能力边界同章。
- 三档模式的默认值是按**网络认证强度**分配的，不是按用户偏好；正文可用来反驳“私有地址=一律随机轮换”的流传说法。

---

## 4. App Store 审核指南：ATS 要求的来源

来源：https://developer.apple.com/app-store/review/guidelines/ （英文页；页脚 `Copyright ©  2026 Apple Inc. All rights reserved.`；正文标注 **Last Updated: October 16, 2026**）。

### 4.1 关键取证结论：现行指南正文中**没有** HTTPS/ATS/TLS/加密条款

状态：**已核实**（负向取证）。取数方法：在指南页面 DOM 上对全文 `document.body.textContent` 做关键词检索，命中数为 0 ——
`/encrypt/i` → 0 次；`TLS` → 0 次；`ATS` → 0 次；`https` → 0 次；并逐条扫描所有带编号的指南条目（id=`1`…`5.6.4`）中匹配 `HTTPS|TLS|encrypt|transport security|secure connection|network connection` 的条目 → **0 条**。
现行指南的五个一级分区为：`1. Safety`／`2. Performance`／`3. Business`／`4. Design`／`5. Legal`（旧式“5.A/5.A.1”编号体系在现页面中不存在，`5. Legal` 下的 `5.1`＝Data Collection and Storage、`5.2`＝Other Time-Sensitive Issues，均无安全连接要求）。

→ 写作口径：**“ATS 是审核要求”这一说法在 2026-10-16 版指南正文中找不到一手支撑**；传输安全要求的约束来自（a）操作系统层 ATS（手册 `sec100a75d12`「App 传输安全性」段，见 #91 §1.2 第 29–39 行；(b) `Info.plist` 例外键文档（同事侧）；(c) 审核实操与 App Store Connect 提交流程（见 §5）。正文若要写“审核要求必须用 HTTPS”，必须标为**未核实/二手口径**。

### 4.2 现行指南中与“网络连接”直接相关的条目（逐字，英文原文）

`2.5.1`（API 与运行版本）：

> 2.5.1 Apps may only use public APIs and must run on the currently shipping OS.

`2.5.5`（**唯一一条硬性网络协议要求：IPv6-only**）：

> 2.5.5 Apps must be fully functional on IPv6-only networks.

`2.5.6`（WebKit 引擎与例外申请；影响“网页内容受不受 ATS/受限环境约束”）：

> 2.5.6 Apps that browse the web must use the appropriate WebKit framework and WebKit JavaScript.

`2.5.3`（不得传播有害代码）：

> 2.5.3 Apps that transmit viruses, files, computer code, or programs that may harm or disrupt the normal operation of the…

（注：2.5.3 原文更长，正文引用时请以页面为准；本轮只逐字截取到条目编号句首。）

### 4.3 手册侧“审核要求从哪来”的可用一手句

状态：**已核实**（逐字，手册《关于 App Store 安全性》`secb8f887a15`，发布日期 2024-12-19）

> 在 App Store 中，App 均来自于同意遵循 Apple 准则且可识别身份的开发者，同时 App 通过加密保证以免受修改，从而安全分发给用户。每个 App 和每个 App 更新均经审查以评估其是否符合隐私、安保和安全要求。此流程还在不断改进，其设计目的在于将恶意软件、网络犯罪和诈骗阻挡在 App Store 之外，从而保护用户安全。

> 手动检查：检查 App 并未不必要地请求访问敏感数据，并额外评估针对儿童的 App 以帮助确保其符合严格的数据收集和安全条例。

> 修正和移除流程：若发生 App 进入 App Store 后被发现存在违反准则的情况，Apple 会与开发者一道快速解决问题。在涉及欺诈和恶意活动的危险情况下，会立即从 App Store 中移除 App，且下载了该 App 的用户会收到有关 App 恶意行为的通知。

> App 审查强制实施 App Store 政策，旨在保护用户免受可能试图伤害用户或诱使其授权访问敏感数据的 App 的影响。

> 虽然 App Store 安全保护措施单方面来说不可能完美无缺，但作为平台安全保护深度防御战略的一部分，这些措施会使针对 iOS、iPadOS 和 visionOS 用户的大规模攻击变得不切实际，而且对于以窃取钱财为目的的攻击者来说也不划算。

口径要点：手册把审核描述为**政策执行**而非**传输强度要求**；“加密保证以免受修改”指的是**分发完整性（App 包）**，不是出站连接 —— 引用时不要混为 TLS。

---

## 5. 加密声明与出口合规

状态：**已核实**（整节逐字，英文原文）https://developer.apple.com/documentation/security/complying-with-encryption-export-regulations （页脚 © 2026，记为“截至 2026 可用”）

> When you submit your app to TestFlight or the App Store, you upload your app to a server in the United States. If you distribute your app outside the U.S. or Canada, your app is subject to U.S. export laws, regardless of where your legal entity is based. If your app uses, accesses, contains, implements, or incorporates encryption, this is considered an export of encryption software, which means your app is subject to U.S. export compliance requirements, as well as the import compliance requirements of the countries where you distribute your app.

> Every time you submit a new version of your app, App Store Connect asks you questions to guide you through a compliance review. You can bypass these questions and streamline the submission process by providing the required information in your app’s `Information Property List` file.

Declare Your App’s Use of Encryption（逐字）：

> Add the `ITSAppUsesNonExemptEncryption` key to your app’s information property list with a Boolean value that indicates whether your app uses encryption. Set the value to `NO` if your app—including any third-party libraries it links against—doesn’t use encryption, or if it only uses forms of encryption that are exempt from export compliance documentation requirements. Otherwise, set it to `YES`.

**最关键的一句（“用 HTTPS 即属豁免”的官方口径）：**

> Typically, the use of encryption that’s built into the operating system—for example, when your app makes HTTPS connections using `URLSession`—is exempt from export documentation upload requirements, whereas the use of proprietary encryption is not. To determine whether your use of encryption is considered exempt, see `Determine and upload app encryption documentation`.

> If your app uses exempt forms of encryption, you might alternatively be required to submit a year-end self-classification report to the U.S. government. (If you use non-exempt encryption and provide documentation to Apple, the self-classification report isn’t necessary.) To learn more, see `How to file an Annual Self Classification Report`.

Provide Compliance Documentation（逐字）：

> If your app requires export compliance documentation, upload the required items to App Store Connect, as described in `Determine and upload app encryption documentation`. After successfully reviewing the documents, Apple provides you with a code. Add this string as the value for the `ITSEncryptionExportComplianceCode` key in your app’s information property list.

口径要点（写正文时用）：
- **“上传链路用了 TLS 后申报什么”**：走系统内置加密（例：`URLSession` 的 HTTPS）→ 属**豁免**→ `ITSAppUsesNonExemptEncryption = NO`；用**自有/私有加密**→ 非豁免 → `YES` + 文档 + `ITSEncryptionExportComplianceCode`。
- 原文措辞是 **“Typically … is exempt”**，即**通常**而非“一律”；正文不得写成“HTTPS 永远豁免”。
- 豁免**不等于**零义务：豁免类仍可能需向美国政府提交 **year-end self-classification report**；而非豁免类提交了文档反而**不需要**该年报 —— 这个反向关系是常被写错的一点。
- 判定范围包含**链接的第三方库**（“including any third-party libraries it links against”）→ 自查清单里“第三方 SDK 是否带非豁免加密”一条的官方依据。
- 术语纪律：本页的“upload”指**把 App 提交到美国境内服务器**，与“用户数据上传链路”是两件事，正文不得混用（可作为一条防混淆注记）。

### 5.1 App Store Connect 提交流程侧
**部分核实**：本页逐字说明“每次提交新版本 App Store Connect 都会提问以引导合规审查”，并提供用 plist 键跳过提问的路径。**未抓取** App Store Connect Help（`support.apple.com/hostedphotos/...`/App Store Connect Help 的 Encryption 条目）——见 §12。

---

## 6. App 数据上传：iCloud 云盘 / 键值同步 / CloudKit 补充

状态：**已核实**（逐字，手册《iCloud 安全性概览》`secacde2d0da`，发布日期 2022-12-07）
（本页 #91 未取；#91 §5.1 取的是 `sec3cac31735` CloudKit 加密页，见 `_facts_net_channel.md` 第 161–168 行。本页给出**总纲口径**，尤其“先加密后上传”与“传输期 TLS 另加一层”两句。）

> iCloud 可以储存用户的通讯录、日历、照片、文稿和其他内容，并让这些信息在其设备间自动保持最新。它也可以供第三方 App 使用，来储存和同步文稿以及由开发者所定义的 App 数据关键值。用户通过登录 Apple 账户并选取想要使用的服务来设置 iCloud。IT 管理员可以使用来自设备管理服务的配置描述文件关闭部分 iCloud 功能，例如 iCloud 云盘和 iCloud 云备份。

> iCloud 使用较强的安全措施并采用严格的策略来保护用户数据。**大部分 iCloud 数据首先会在用户设备上通过设备生成的 iCloud 密钥加密，然后才上传到 iCloud 服务器。**对于未采用端对端加密的数据，用户设备会将这些 iCloud 密钥安全地上传到 Apple 数据中心的 iCloud 硬件安全模块。这将允许 Apple 协助用户恢复数据，并在用户需要时（例如登录新设备、从备份恢复或者通过网页访问 iCloud 数据）代表用户解密数据。**在用户设备和 iCloud 服务器之间传输的数据会通过 TLS 单独加密，且 iCloud 服务器会使用额外的加密层来储存静态用户数据。**

> 提供给 Apple 的加密密钥安全存放在 Apple 数据中心。处理储存在第三方数据中心的数据时，这些加密密钥仅可由在安全服务器上运行的 Apple 软件获取，并且仅在处理必要流程时才可获取。为了加强隐私和安全保护，许多 Apple 服务使用端对端加密，这意味着只有用户本人才能访问自己的 iCloud 数据且只能在通过其 Apple 账户登录的受信任设备上访问。

> 针对用户储存在 iCloud 中的数据，Apple 提供了两种加密和保护选择：
> - 标准数据保护（默认设置）：用户的 iCloud 数据会被加密，而加密密钥安全储存在 Apple 数据中心，以便 Apple 协助恢复数据和账户。只有特定的 iCloud 数据（包括“健康”数据和 iCloud 钥匙串中的密码等 14 个数据类别）会采用端对端加密。
> - iCloud 高级数据保护：提供 Apple 最高级别云端数据安全性的可选设置。如果用户选择打开“高级数据保护”，则只有其受信任的设备才能访问大部分 iCloud 数据的加密密钥，因此这些数据会受到端对端加密保护。用户打开“高级数据保护”后，采用端对端加密的数据类别会上升到 23 个，包括 iCloud 云备份、“照片”、“备忘录”等。

> Apple 支持文章 iCloud 数据安全概览中列出了采用端对端加密进行保护的特定 iCloud 数据类别。

口径要点（写正文时用）：
- 本页是“**上传方向上谁持有密钥**”的总纲句：**密钥由设备生成 → 数据先加密后才上传 → 非 E2EE 类别的密钥再交给 Apple HSM**。三层顺序不能简写成“Apple 掌握密钥”或“Apple 完全看不到”。
- “**在用户设备和 iCloud 服务器之间传输的数据会通过 TLS 单独加密**”= 存储加密与传输加密是**两层独立机制**；可用来反驳“iCloud 有静态加密所以不需要 TLS”的错误推论。
- 量化基线：标准数据保护 **14** 个端到端加密类别；高级数据保护 **23** 个；差值即 ADP 扩展部分。（**注意**：support 102651 于 2026-01-08 已把这两个数更新为 **15 → 25**，正文以较新值为准并注明手册页未同步，见 §6.3。）
- 手册**没有**独立的“iCloud 云盘”或“iCloud 键值数据存储”页（`guide/security/toc` 全部条目与全量 secid 清单中均不存在）；iCloud 云盘的传输保护口径只能落到本页 + `sec3cac31735`。
- **IT 管理员可以使用来自设备管理服务的配置描述文件关闭部分 iCloud 功能，例如 iCloud 云盘和 iCloud 云备份** —— 组织侧能“断掉上传目的地”，但本页没有任何“修改上传加密强度”的权限表述。这是 §1.5「描述文件能做什么/不能做什么」的对偶证据。

### 6.1 iCloud 云盘文件同步的传输保护
状态：**部分核实**。可用句＝本页“在用户设备和 iCloud 服务器之间传输的数据会通过 TLS 单独加密”＋ #91 §5.1“当数据写入 CloudKit 时，所有记录密钥均会在用户受信任设备上生成并封装到对应的密钥层级，然后才会上传数据”。**无独立“iCloud 云盘安全性”页**。

### 6.2 iCloud 键值数据存储
状态：**部分核实**。逐字可用句：> 它也可以供第三方 App 使用，来储存和同步文稿以及由开发者所定义的 App 数据关键值。（手册原词“App 数据关键值”）手册层面无键值同步独立加密档位页。

### 6.3 上传方向的密钥档位：《iCloud 高级数据保护》与《iCloud 数据安全概览》

状态：**已核实**（逐字，手册《iCloud 高级数据保护》 https://support.apple.com/zh-cn/guide/security/sec973254c5f/web ，发布日期 **2022-12-07**）

> iCloud 高级数据保护是提供 Apple 最高级别云端数据安全性的可选设置。当用户打开“高级数据保护”后，只有其受信任的设备才能访问大部分 iCloud 数据的加密密钥，因此这些数据会受到端对端加密保护。对于打开“高级数据保护”的用户，采用端对端加密进行保护的数据总类别会由 14 个上升到 23 个，包括 iCloud 云备份、“照片”、“备忘录”等。

> “高级数据保护”在概念上很容易理解：在设备上生成且随后上传到 Apple 数据中心的认证后可用 iCloud 硬件安全模块 (HSM) 的所有 CloudKit 服务密钥会从这些 HSM 中删除，并且会完全保存在 Apple 账户的 iCloud 钥匙串保护域中。其处理方式和现有的端对端加密服务密钥一样，这意味着 Apple 不可再读取或访问这些密钥。**“高级数据保护”还会自动保护第三方开发者选择标记为加密的 CloudKit 字段以及所有 CloudKit 资源。**

> 即使打开“高级数据保护”，iCloud 也会储存部分不受用户特定 CloudKit 服务密钥保护的数据。**在容器的模式中，CloudKit 记录字段必须明确声明为加密才能受保护，而读取和写入加密字段需要使用专门的 API。**文件或对象的修改日期和时间用于对用户的信息进行排序，而文件和照片数据的校验和则用于帮助 Apple 进行用户 iCloud 和设备储存空间的数据去重和优化，所有操作均不可访问文件和照片本身。

> 在推出 iCloud 服务时，其最初的设计决策之一便是使用校验和来进行数据去重，这项广为人知的技术称为收敛加密。此元数据会始终加密，但加密密钥由 Apple 采用标准数据保护进行储存。

> 2. 用户设备会将所有认证后可用服务的服务密钥安全地上传到 Apple 数据中心的 iCloud HSM，但采用标准数据保护的端对端加密服务（例如 iCloud 钥匙串和“健康”）的密钥绝不会上传。设备会同时上传在“高级数据保护”打开前生成的原始服务密钥和在用户打开该功能后生成的新服务密钥。

> 在接下来的一小时，此设备会接受来自特定 Apple 服务器的请求以上传单个服务密钥，但仅限于通常可在 iCloud.com 上访问的服务列表所对应的密钥。……每次上传服务密钥时，该密钥会使用与用户授权的网页会话绑定的临时密钥加密，且用户设备上会出现一则通知，显示哪项 iCloud 服务的数据正临时可供 Apple 服务器访问。

> 对于每次共享操作，Apple 会采用标准数据保护来储存标题和示意缩略图以向接收用户显示预览。启用协作时选择“任何拥有链接的用户”这一选项会采用标准数据保护来使内容可供 Apple 服务器访问，因为服务器需要能够为打开 URL 的任何用户提供访问权限。

> 当用户协作处理 Keynote 讲演、Numbers 表格或 Pages 文稿或者打开 iCloud 云盘中共享文件夹内的此类文稿时，文稿的加密密钥会安全上传到 Apple 数据中心的服务器。这是因为在 Keynote 讲演、Numbers 表格或 Pages 文稿中实时协作需要服务器端处理来协调参与者之间的文稿更改。

状态：**已核实**（逐字，support 文章《iCloud 数据安全概览》 https://support.apple.com/zh-cn/102651 ，发布日期标记 **2026 年 01 月 08 日**；页面小节：iCloud 数据安全与加密 / 标准数据保护 / iCloud 高级数据保护 / 数据类别和加密方式（附加备注・某些元数据和使用信息的加密方式・共享和协作・iCloud.com 和网页上的数据访问・第三方 App 数据）/ 关于第三方数据中心）

> 为了提供更高的隐私和安全保护，15 个数据类别（包括 iCloud 钥匙串中的健康数据和密码）会进行端对端加密。Apple 没有这类数据的加密密钥，如果你无法访问自己的账户，我们无法协助你恢复这些数据。

> 借助“高级数据保护”，使用端对端加密的数据类别数量将增加到 25 个，其中包括“iCloud 云备份”、“照片”、“备忘录”数据等。

> **在你打开“高级数据保护”后，储存在 iCloud 云备份以及 CloudKit 加密字段和资源中的第三方 App 数据会进行端对端加密。**

> 关闭这项功能后，你的设备会安全地将所需的加密密钥上传到 Apple 服务器，而你的账户将再次使用标准数据保护。

> 来自你受信任设备的加密密钥会安全储存在 Apple 数据中心，因此只要你需要，Apple 便可以代表你解密你的数据，例如当你在新设备上登录时、从备份恢复时或在你忘记密码后恢复数据时。

> 例如，文件或对象的修改日期和时间用于对你的信息进行排序，文件和照片数据的校验和用于帮助 Apple 删除你 iCloud 和设备储存空间中的重复数据并优化这些储存空间；所有这些操作都无法访问文件和照片本身。这类元数据始终会加密，但加密密钥仍然由 Apple 储存。

> 当你使用这些功能时，共享内容的加密密钥会安全地上传到 Apple 数据中心，以便 iCloud 可以协助进行实时协作或网页共享。这意味着，即使启用了“高级数据保护”，共享内容也不会进行端对端加密。

> 在处理储存在第三方数据中心的数据时，加密密钥仅会由安全服务器上运行的 Apple 软件在进行必要的处理时进行访问。这些密钥始终会储存在 Apple 数据中心并受到保护。

口径要点（写正文时用）：
- **数值以 support 2026-01-08 为准（15 → 25）**，手册 `secacde2d0da`／`sec973254c5f`（均 2022-12-07）写的是 14 → 23；两处差异必须写成“手册页未同步更新”，不得混用。
- 这是全份证据里对“上传方向谁持密钥”最正面的一手回答：**设备生成密钥 → 先加密再上传 → 非 E2EE 档把密钥上传到 Apple HSM → E2EE 档把密钥留在 iCloud 钥匙串保护域**；而“打开 ADP”这个动作本身就是**一次删除服务器侧密钥的操作**（“会从这些 HSM 中删除”），关闭则是一次**重新上传密钥**的操作。
- **CloudKit 不是自动全加密**：字段要开发者**明确声明为加密**，且读写要用**专门的 API**；资源（`CKAsset` 那一类）则由 ADP 统一覆盖。这条推翻了“数据进了 CloudKit 就端对端加密”的常见说法。
- 校验和/去重（收敛加密）+ 修改时间 + 标题缩略图 + “任何拥有链接的用户”共享 + iWork 实时协作，是四类**即使开了 ADP 也退回标准数据保护**的明文可见面，正文写“谁能看到明文”时必须逐个列出。
- 网页访问那条给了一个可引的“临时上传单个服务密钥 + 一小时窗口 + 设备端通知”模型，可用来反驳“开启 E2EE 后 Apple 在任何情况下都无法解密”。
- **未找到**「iCloud 专用 App 隐私」的一手页：`guide/security/toc`（178 条）与 `guide/deployment/toc`（220 条）里没有任何标题含“专用 App”的条目，support 侧也未检索到；正文若需要该口径只能写成缺口（见 §12）。

### 6.4 文件保护级别对上传临时文件的影响

状态：**已核实**（逐字英文，developer.apple.com DocC，页脚 `Copyright © 2026 Apple Inc. All rights reserved.`）

https://developer.apple.com/documentation/foundation/fileprotectiontype （`struct FileProtectionType`，摘要“Protection level values that can be associated with a file attribute key.”，可用范围 iOS 2.0+ / macOS 10.0+ / watchOS 2.0+）：

> These values are associated with the protectionKey key.

> `static let complete: FileProtectionType` — The file is stored in an encrypted format on disk and cannot be read from or written to while the device is locked or booting.

> `static let completeUnlessOpen: FileProtectionType` — The file is stored in an encrypted format on disk after it is closed.

> `static let completeUntilFirstUserAuthentication: FileProtectionType` — The file is stored in an encrypted format on disk and cannot be accessed until after the device has booted.

> `static let none: FileProtectionType` — The file has no special protections associated with it.

https://developer.apple.com/documentation/foundation/fileattributekey/protectionkey ：

> The key in a file attribute dictionary whose value identifies the protection level for this file.

https://developer.apple.com/documentation/foundation/urlfileprotection （摘要逐字）：

> Protection-level values for a URL resource key.

（该页 Overview 正文只有一句、且句中两处引用链接文本被 DocC 抽成 identifier，未能逐字复原，故此处只标**部分核实**。）

状态：**已核实**（逐字英文，https://developer.apple.com/documentation/cloudkit/ckasset ，© 2026；摘要“An external file that belongs to a record.”）

> Use assets to incorporate external files into your app’s records, such as photos, videos, and binary files. Alternatively, use assets when a field’s value is more than a few kilobytes in size.

> CloudKit stores only an asset’s data. If you require its filename, or any other file-system metadata, use one or more separate fields on the record to store it.

> CloudKit stores an asset’s data separately from a record that references it, but maintains an association with that record. **When you save a record that has an asset, CloudKit saves both the record and the asset to the server.** Similarly, when you fetch the record, the server returns the record and the asset.

> When you fetch a record that contains an asset, CloudKit stores the asset’s data in a staging area accessible to your app. Use the asset’s [属性名在 DocC JSON 中是引用链接，抽取时丢失，未逐字复原] property to access its staged location. **The system regularly deletes files in the staging area to reclaim disk space.** To avoid this behavior, move the data into your app’s container as soon as you fetch it.

> If you no longer require an asset that’s on the server, you don’t delete it. Instead, orphan the asset by setting any fields that contain the asset to [同上，抽取时丢失] and then saving the record. CloudKit periodically deletes orphaned assets from the server.

口径要点（写正文时用）：
- “上传临时文件”这条链条在 Apple 一手文档里可落地为三句：① 落盘临时文件受 `FileProtectionType` 约束，`complete` 档在**设备锁定时连读写都不行**——因此任何“锁屏后仍要继续上传”的后台任务，其输入文件若标 `NSFileProtectionComplete` 就会失败，这是 App 侧自己选的取舍，不是系统或组织能改的；② CloudKit 明确“**存记录时同时把资源上传到服务器**”，即 `CKAsset` 是一次独立的对象上传；③ 下载侧资源落在 **staging area**、系统会定期删除，所以“服务器返回的明文对象在设备上的驻留位置”并不在 App 容器保护域内，需自行搬进容器。
- 与 §6.3 合起来可写成一句完整链条：字段级加密由开发者显式声明 → 资源级由 ADP 统一覆盖 → 传输层由 TLS（`secacde2d0da` 那句）→ 本地驻留层由 FileProtection / staging area 决定。
- **未取到**任何 Apple 一手页直接写“`NSFileProtectionComplete` 会阻断后台上传任务”或“URLSession background session 使用哪一档保护级别”——正文只能写成缺口（见 §12）。

---

## 7. 网络流量拦截与重加密（官方口径）

### 7.1 Apple 服务对 HTTPS 拦截的处置（最关键一手句）

状态：**已核实**（逐字）https://support.apple.com/zh-cn/101555 《在企业网络上使用 Apple 产品》，发布日期 **2026-07-31**

> 了解在企业网络上使用 Apple 产品时需要访问哪些主机和端口。
> 本文适用于企业和教育机构的网络管理员。

> Apple 产品需要访问本文列出的互联网主机，才能使用各种服务。下面介绍了你的设备如何连接到主机并使用代理：
> - 与下列主机之间的网络连接是由设备（而不是 Apple 运营的主机）发起的。
> - **Apple 服务将中断任何使用 HTTPS 拦截（SSL 检查）的连接。如果 HTTPS 流量遍历某个网页代理，请对本文列出的主机停用 HTTPS 拦截。**

APNs 与代理（逐字）：

> 了解如何对 Apple 推送通知服务 (APNS) 的连接问题进行故障诊断。对于通过 HTTP 代理发送所有流量的设备，你可以在设备上手动配置这个代理，也可以使用设备管理服务进行配置。如果将设备配置为通过代理自动配置 (PAC) 文件来使用 HTTP 代理，则设备可以连接到 APNS。

设备管理通道所需主机/端口（逐字，节选）：

> 已在设备管理服务中注册的 Apple 设备需要访问以下主机和域。
> *.push.apple.com — 443、80、5223、2197 — 推送通知
> deviceenrollment.apple.com — iOS、iPadOS、Apple tvOS 和 macOS — DEP 暂时注册
> iprofiles.apple.com — 托管当设备通过设备注册在 Apple 校园教务管理或 Apple 商务中注册时使用的注册描述文件
> mdmenrollment.apple.com — 当客户端通过设备注册在 Apple 校园教务管理或 Apple 商务中注册时，供设备管理服务用来上传客户端使用的注册描述文件，还可用来查找设备和账户
> *.appattest.apple.com — 管理式设备证明
> axm-adm-mdm.apple.com — 设备管理服务 ／ axm-adm-scep.apple.com — SCEP 服务器 ／ axm-adm-enroll.apple.com — DEP 注册服务器

撤销/证书验证所需主机（逐字，可接 #91 §1.3 撤销链路）：

> 为了验证这篇文章列出的主机所使用的数字证书，Apple 设备必须能够连接到以下主机。
> certs.apple.com ／ crl.apple.com ／ crl3.digicert.com ／ crl4.digicert.com ／ ocsp.apple.com ／ ocsp.digicert.cn（中国大陆的证书验证）／ ocsp.digicert.com ／ ocsp2.apple.com ／ valid.apple.com

加密 DNS（逐字）：

> iOS 14、iPadOS 14、Apple tvOS 14 和 macOS Big Sur 及更高版本中的加密域名系统 (DNS) 解析使用以下主机。
> doh.dns.apple.com — 用于 DNS over HTTPS (DoH)

口径要点：
- **“Apple 服务将中断任何使用 HTTPS 拦截（SSL 检查）的连接”** = 官方承认中间人式重加密**在现实中会发生**（所以需要域名豁免清单），同时给出 Apple 自有服务侧的对抗措施（检测到拦截即断连）。正文可写：组织能对自己的域名做 TLS 中间人（装根 CA + 代理），但**不能对 Apple 服务域名这么做**。
- **“与下列主机之间的网络连接是由设备（而不是 Apple 运营的主机）发起的”** = 出站方向由设备发起的一手确认（对“上传是设备主动外拨”的官方支撑）。
- APNs 走 443/80/5223/2197，且 **PAC 配置的代理下设备仍可连接 APNS** → 推送通道对代理的可用性被官方确认。
- 撤销检查（CRL/OCSP）本身也需要出网到列出的主机 → “断网/挡域名会让 TLS 信任评估失败”是可推导的（正文应标为推论）。

### 7.2 内容过滤：组织可在 App 不知情时检查其流量

状态：**已核实**（逐字）《为 Apple 设备过滤内容》 https://support.apple.com/zh-cn/guide/deployment/dep1129ff8d2/web ，发布日期 **2025-09-24**

> iOS、iPadOS、macOS 和 visionOS 1.1 支持多种形式的内容过滤，包括访问限制、全局 HTTP 代理、过滤的 DNS、DNS 代理以及高级内容过滤。

内建过滤器（逐字）：

> Apple 设备可以将 Safari 浏览器和第三方 App 限制到特定网站。内容过滤需求简单或有限的组织可以使用此功能。对于复杂或法律强制的内容过滤需求，组织应当使用全局 HTTP 代理或第三方内容过滤 App 提供的高级内容过滤选项。
> 设备管理服务可使用以下选项配置内建过滤器：所有网站：不过滤网页内容。／限制成人内容：自动限制对于许多成人网站的访问。／已阻止的站点：允许访问所有网站，可自定义阻止列表中的网站除外。／仅特定网站：限制对于预定网站的访问，可自定义。
> 你可使用 iOS、iPadOS 和 visionOS 1.1 中的 WebContentFilter 有效负载以及 macOS 中的 ParentalControlsContentFilter 有效负载配置内建过滤器。通过设备管理服务管理内建过滤器还会限制在 Safari 浏览器中清除浏览历史记录和网站数据的访问许可。

全局 HTTP 代理（逐字，含**绕过 HTTP 代理的 App**这一关键限定）：

> Apple 设备支持全局 HTTP 代理配置。全局 HTTP 代理通过指定的代理服务器发送大部分的设备网络流量，或者通过应用于所有无线局域网、蜂窝网络和以太网的设置进行发送。这一功能常用于 K–12（指幼儿园到高三教育）或企业，在组织拥有（一对一）的部署中，用户会将设备带回家，因而这些机构或企业使用该功能来过滤互联网内容。它使设备在学校/企业和家中都能过滤内容。全局 HTTP 代理要求 iPhone、iPad 和 Apple TV 设备为受监督设备。
> 【注】有些 App（例如 FaceTime 通话）不使用 HTTP 连接，也无法由 HTTP 代理服务器来代理，因而会绕过全局 HTTP 代理。你可以使用高级内容过滤来管理不使用 HTTP 连接的 App。
> Apple 服务会停止任何使用 HTTPS 拦截（SSL/TLS 检查）的连接。如果 HTTPS 流量遍历某个网络代理，你需要对 Apple 支持文章“在企业网络上使用 Apple 产品”中列出的主机关闭 HTTPS 拦截。

代理规划要点（逐字，节选）：

> 外部可访问性：如果设备要在组织网络之外访问服务器，则组织的代理服务器需要可从外部访问。
> 代理 PAC：全局 HTTP 代理支持通过指定代理服务器的 IP 地址或 DNS 名称来手动配置代理，或使用代理 PAC URL 自动配置。代理 PAC 文件配置可以指导客户端自动选取合适的代理服务器来获取给定的 URL，包括在想要时绕过代理。考虑使用 PAC 文件以实现更大的灵活性。
> 受限无线局域网兼容性：全局 HTTP 代理配置可允许客户端临时绕过代理设置，以加入受限无线局域网。这些网络在授予互联网访问许可前，要求用户通过某个网站同意条款或支付费用。
> 通过缓存服务使用代理：考虑使用 PAC 文件来配置客户端以控制其使用缓存服务的时间。配置不当的过滤解决方案可能会导致客户端绕过你所在组织网络上的缓存服务或导致设备在用户家时无意中使用缓存服务过滤内容。

高级内容过滤 / 内容过滤提供者（逐字，**socket 级拦截**）：

> iOS、iPadOS 和 macOS 支持对网络流量和套接字流量进行高级内容过滤的插件。设备端网络内容过滤会在用户网络内容通过网络堆栈时对其进行检查。然后内容过滤会确定要阻止该内容还是允许其传递到最终目标位置。与设备管理服务一起安装的 App 会提供内容过滤提供者。过滤数据提供程序在受限沙盒中运行，因此用户的隐私会得到保护。App 中实现的过滤控制提供程序可动态更新过滤规则。

URL 过滤（逐字，iOS 26/macOS 26 的新机制，可匹配**完整 URL 含路径与查询**）：

> iOS 26、iPadOS 26 和 macOS 26 或更高版本中可用的 URL 过滤器会基于完整 URL 做出内容过滤决定。URL 过滤通过你的 URL 数据集实现并使用设备端 Bloom 过滤器，如需通过远程服务器查询，则使用私有信息检索 (PIR)。系统首先会咨询 Bloom 过滤器以快速检查 URL，必要时可能会通过 Apple 托管的“Oblivious HTTP 中继”与 PIR 服务器进行设备外的数据库查询。采用此方法，“网络扩展”URL 过滤器可为消费者和管理式部署提供高效且保护隐私的解决方案。URL 过滤器提供了一种解决方案，支持过滤全系统 HTTPS 和 HTTP 请求，与请求的完整 URL 匹配，并可阻止特定的互联网资源。

VPN/数据包隧道（逐字，**监控与过滤的官方承认**）：

> 设备将网络流量通过 VPN 或数据包隧道发送时，网络活动可能会被监控和过滤。此配置类似于设备直接接入专用网络和互联网之间的流量被监控和过滤的网络。

内建网络安全功能与中继（逐字） https://support.apple.com/zh-cn/guide/deployment/depb59c050ef/web 《使用 Apple 设备内建的网络安全功能》，发布日期 2024-10-30：

> 运行 iOS 17、iPadOS 17、macOS 14、Apple tvOS 17 或更高版本的设备中的内建中继可作为替代 VPN，用于保护使用加密 HTTP/3 或 HTTP/2 连接的流量。网络中继是一种特殊类型的代理，已针对性能进行优化且使用最新的传输和安全协议。它可用于保护特定 App、整台设备以及访问内部资源时 TCP 和 UDP 流量的安全。多个网络中继（包括 iCloud 专用代理）可同时使用，无需 App。

DNS 设置载荷（逐字，DoH/DoT + 可按 SSID/域分流）：

> 你可以为设备管理服务中注册的 iPhone、iPad（包括“共享 iPad”）、Mac 和 Apple Vision Pro 设备用户配置“DNS 设置”有效负载设置。确切地说，你可以使用此有效负载来配置通过 HTTP 的 DNS（也称为 DoH）或通过 TLS 的 DNS（也称为 DoT）。这样做可通过加密 DNS 流量来强化用户隐私，还可让你使用已过滤的 DNS 服务。该有效负载可以识别使用指定 DNS 服务器的特定 DNS 查询，也可应用到所有 DNS 查询。该有效负载还可以指定无线局域网 SSID，供特定 DNS 服务器用于查询。
> 【注】使用设备管理服务安装有效负载时，设置将仅应用到受管理的无线局域网。

网络代理配置（macOS 专属载荷，逐字）：

> 代理服务器在单个电脑用户和互联网之间起着协调的作用，从而使网络能够确保安全、管理控制和高速缓存服务。使用“网络代理”设备管理有效负载为设备管理服务中注册的 Mac 电脑配置代理设置。此有效负载支持为以下协议配置代理：HTTP／HTTPS／FTP／RTSP／SOCKS／Gopher
> 部分协议。

### 7.3 载荷键的英文一手口径（developer.apple.com Device Management，© 2026）

`com.apple.webcontent-filter`（WebContentFilter 载荷，https://developer.apple.com/documentation/devicemanagement/webcontentfilter ）：

> The system matches URLs using string-based matching. A URL matches an allow list, deny list, or permitted list pattern if the exact characters of the pattern appear as a substring of the URL requested in the web browser. For example, if the system doesn’t allow `test.com/a`, it blocks `test.com/a`, `test.com/apple`, and `test.com/a/b`.
> The system matches list entries that terminate with a `/` character explicitly; if the system blocks or allows `test.com/a/`, it blocks or allows `test.com/a` and `test.com/a/b`.
> Matching discards a `www` subdomain prefix if present, so if the system doesn’t allow `www.test.com`, it also blocks `m.test.com`.
> All filtering options are active simultaneously. The system only permits URLs and sites that pass all rules.

键（逐字）：

> `FilterBrowsers` :: If `true`, the system enables filtering WebKit traffic. Use when `FilterType` is `Plugin`. At least one of `FilterBrowsers` or `FilterSockets` needs to be `true`. Available: iOS 7+ | iPadOS 7+ | visionOS 1.1+
> `FilterSockets` :: If `true`, enables the filtering of socket traffic. Use when `FilterType` is `Plugin`. At least one of `FilterBrowsers` or `FilterSockets` needs to be `true`.
> `FilterPackets` :: If `true` and `FilterType` is `Plugin`, the system enables filtering network packets. … At least one of `FilterPackets` or `FilterSockets` needs to be `true`. Available: macOS 10.15+
> `FilterURLs` :: If `true`, the system filters URL requests. Use when `FilterType` is `Plugin`. Available: iOS 26+ | iPadOS 26+ | macOS 26+
> `ContentFilterUUID` :: A globally unique identifier for this content filter configuration. The content filter processes network traffic for managed apps with the same `ContentFilterUUID` in their app attributes. Use when `FilterType` is `Plugin`. This key must be present for unsupervised devices and user enrollment. Available: iOS 16+ | iPadOS 16+ | visionOS 1.1+
> `DenyListURLs` :: An array of URLs that are inaccessible. Use when `FilterType` is `BuiltIn`. Limit the number of these URLs to no more than 500. Available: iOS 14.5+ | iPadOS 14.5+ | visionOS 1.1+
> `PermittedURLs` :: An array or URLs that are accessible whether or not the automatic filter allows access. Use when `FilterType` is `BuiltIn`. Requires that `AutoFilterEnabled` is `true`.
> `FilterGrade` :: The system uses this value to derive the relative order of content filters. Filters with a grade of `firewall` see network traffic before filters with a grade of `inspector`. However, the system doesn’t define the order of filters within a grade. Available: macOS 10.15+
> `SafariHistoryRetentionEnabled` :: If `true`, this payload enforces a policy which requires retention of browsing history. This causes Safari to disable clearing of browsing history, and prevents the use of private browsing mode because that mode doesn’t keep browsing history. Available: iOS 26+ | iPadOS 26+ | macOS 26+ | visionOS 26+
> `FilterDataProviderBundleIdentifier` :: The bundle identifier string of the filter data provider system extension. … Required if `FilterSockets` is `true`.
> 载荷平台可用性（declarations）：iOS, iPadOS, Mac Catalyst, macOS, visionOS

`com.apple.proxy.http.global`（GlobalHTTPProxy，https://developer.apple.com/documentation/devicemanagement/globalhttpproxy ）：

> Specify `com.apple.proxy.http.global` as the payload type. There can only be one payload of this type on the device at any time.
> `ProxyType` :: The proxy type. For a manual proxy type, the profile contains the proxy server address, including its port, and optionally a user name and password. For an auto proxy type, you can enter a PAC URL.
> `ProxyPACURL` :: The URL of the PAC file that defines the proxy configuration. Starting in iOS 13 and macOS 10.15, only URLs that begin with `http://` or `https://` are allowed. This is only used if `ProxyType` is set to `Automatic`, and is ignored if `ProxyType` is set to `Manual`.
> `ProxyPACFallbackAllowed` :: If `true`, allows connecting directly to the destination if the proxy autoconfiguration (PAC) file is unreachable.
> `ProxyCaptiveLoginAllowed` :: If `true`, allows the device to bypass the proxy server to display the login page for captive networks. Available: iOS 7+ | iPadOS 7+ | macOS 10.9+ | tvOS 9+ | visionOS 2+
> `ProxyPassword` / `ProxyUsername` :: The password (user name) used to authenticate to the proxy server. The device only uses this if `ProxyType` is set to `Manual`, and ignores it if `ProxyType` is set to `Automatic`.
> 平台可用性：iOS, iPadOS, Mac Catalyst, macOS, tvOS, visionOS

`com.apple.SystemConfiguration`（NetworkProxyConfiguration，macOS 专属）：

> Specify `com.apple.SystemConfiguration` as the payload type. 平台可用性：Device channel — macOS
> 示例键（逐字，来自页面 Profile example）：`Proxies` → `Exceptions`（`*.local, 169.254/16`）、`HTTPEnable` = 1、`HTTPProxy` = proxy.example.com、`HTTPPort` = 8080、`FTPPassive` = 1

口径要点：
- **“全局 HTTP 代理要求 iPhone、iPad 和 Apple TV 设备为受监督设备”** + **“The content filter processes network traffic for managed apps…”** → 组织侧拦截能力的**前置条件是监督/受管理**，正文必须写清“非受监督设备上的普通 App 流量不在官方支持的拦截路径内”。
- **`FilterSockets`=套接字级、`FilterBrowsers`=WebKit 流量级、`FilterPackets`=数据包级（macOS）** 三档，逐字可引 —— 报告写“组织可在 App 不知情时检查其连接”时用这三条，而不是笼统说“代理”。
- **URL 过滤（iOS 26+）能看到完整 URL（含路径与查询串）**，且走 Bloom + PIR + Apple 托管 Oblivious HTTP 中继 —— 这是“上传请求的 URL 层面可见性”的官方口径；**页面只承诺 URL 级过滤，未承诺读取请求体**。
- `ProxyPACURL` 自 iOS 13/macOS 10.15 起**只允许 `http://` 或 `https://`** —— PAC 分发协议本身的限制，可进自查清单。
- 载荷**只有一条**全局 HTTP 代理（“There can only be one payload of this type on the device at any time”）→ 组织侧代理与用户手工代理的优先级/冲突面，页面未说明（缺口）。

### 7.4 组织可使用的“路径改写”载荷全表（部署手册，逐字中文）

以下 7 页全部来自 `https://support.apple.com/zh-cn/guide/deployment/<depid>/web`，均已核实（逐字）。每页末尾都有同一句免责：**“每个设备管理服务开发者对这些设置的执行情况不同。若要了解各种……设置如何应用到你的设备，请参阅开发者的设备管理服务文稿。”**

**《针对 Apple 设备的“全局 HTTP 代理”设备管理有效负载设置》`dep7ba46fcd`，发布日期 2022-10-24**

> 使用“全局 HTTP 代理”有效负载为设备管理服务中注册的 iPhone、iPad、“共享 iPad”、Mac 或 Apple TV 指定所有 HTTP 进出流量的代理。如果选取“手动”代理类型，则需要代理服务器地址（包括其端口），以及用于登录代理服务器的用户名和密码（可选）。如果你选取“自动”代理类型，你可以输入一个代理自动配置 (PAC) URL。

> 支持的有效负载标识符：com.apple.proxy.http.global …… 支持的注册方式：自动设备注册。允许的重复项：False：只能将一个“全局 HTTP 代理”有效负载发送给某台设备。

> 代理类型 | 使用需要认证的“手动”配置的代理。| 是
> 主机名 | 代理服务器的 IP 地址或完全限定域名 (FQDN)。在代理类型为“手动”时需要。| 是
> 端口 | 代理服务器的端口号。在代理类型为“手动”时需要。| 是
> 代理服务器 URL | 在代理类型为“自动”时需要。| 是
> 认证 | 代理服务器的用户名称。此设置仅对“手动”代理类型可用。| 否
> 密码 | 代理服务器的认证密码。此设置仅对“手动”代理类型可用。| 否
> 在 PAC 无法连接时允许直接连接 | 设备将在 PAC 文件无法访问时绕过代理服务器。| 否
> 允许忽略代理以访问受限网络 | 设备将忽略代理设置来为受限网络显示登录页。| 否

**《针对 Apple 设备的“网络代理配置”设备管理设置》`depb27492e34`，发布日期 2022-10-24（macOS 专属）**

> 你可以为设备管理服务中注册的 Mac 用户配置“网络代理配置”有效负载设置。“网络代理配置”设置允许设备使用代理服务器访问网站。支持的有效负载标识符：com.apple.SystemConfiguration 支持的操作系统和通道：macOS 设备 支持的注册方式：设备注册、自动设备注册。

> 启用自动代理配置 | 启用“自动代理配置”（如果正在使用代理自动配置 (PAC) 文件）。在 URL 栏中输入 PAC 文件地址。例如，https://www.melardclothing.com/filename.pac。| 否
> PAC 回退 | 指定代理无法连接时是否允许设备接入网站。| 否
> 网络代理自动发现 (WPAD) 配置 | 从弹出式菜单中选取“自动”。如果你将“代理服务器 URL”栏留空，则设备将使用 DHCP（通过 252 条目）或 DNS（通过名为“WPAD”的 A 记录）来请求 wpad.dat 文件。| 否
> 代理类型 | 选择要使用的代理类型：HTTP HTTPS FTP RTSP SOCKS Gopher | 是
> 代理异常 | 选取忽略互联网（主机）上的特定电脑和互联网（域）的分段的代理设置……若要忽略单个域，请输入域名，比如 apple.com。若要忽略一个域中的所有网站，请在域名前使用星号，比如 *apple.com。若要忽略一个域的特定部分，请指定每个部分，比如 store.apple.com。| 否
> 被动 FTP 模式 (PASV) | 启用 FTP 被动模式 (PASV)。| 否

**《针对 Apple 设备的“DNS 设置”设备管理有效负载设置》`dep86469ba99`，发布日期 2024-03-07**

> 你可以为设备管理服务中注册的 iPhone、iPad、“共享 iPad”、Mac 或 Apple Vision Pro 用户配置“DNS 设置”设置。使用“DNS 设置”有效负载指定需要使用特定 DNS 设置的 App。

> 支持的有效负载标识符：com.apple.dnsSettings.managed 支持的操作系统和通道：iOS、iPadOS、“共享 iPad”设备、macOS 设备、visionOS 1.1。支持的注册方式：设备注册、自动设备注册。允许的重复项：True：可将多个“DNS 设置”有效负载发送给某台设备。

> 协议 | 用于与 DNS 服务器通信的加密传输协议。选项为“HTTPS”或“TLS”。| 是
> 服务器地址 | DNS 服务器 IP 地址字符串列表，可以是 IPv4 和 IPv6 地址的混合。| 否
> 服务器 URL | 用于验证服务器证书的 URL（以 https:// 开头）。如果此栏为空，将使用 URL 中的主机名或地址确定服务器地址。在“协议”为“HTTPS”时需要。| 否
> 匹配域 | 用于确定哪些 DNS 查询使用 DNS 服务器的域列表。如果此列表缺失，将使用默认 DNS 服务器。你还可以使用单个通配符 * 前缀。例如，*.melardclothing.com 和 melardclothing.com 都会匹配 mydomain.melardclothing.com 和 your.domain.melardclothing.com，但不会匹配 mydomain-melardclothing.com。| 否

**《针对 Apple 设备的“DNS 代理”设备管理有效负载设置》`dep500f65271`，发布日期 2024-03-07**

> 使用“DNS 代理”有效负载指定提供 DNS 代理网络扩展和任何供应商特定值的 App。【注】可使用多个 DNS 代理，但无法合并 App 单独代理和系统范围代理。支持的安装方法：需要设备管理服务才能安装。支持的有效负载标识符：com.apple.dnsProxy.managed …… 支持的注册方式：自动设备注册。

> App 捆绑包 ID | 包含 DNS 代理信息的 App（根据 App 的捆绑包 ID）。| 是
> DNS 代理 UUID（iOS 16 或更高版本 / iPadOS 16.1 或更高版本）| 此 DNS 代理配置的全局唯一标识符。对于 App 属性中 DNS 代理 UUID 相同的受管理 App，其 DNS 查找流量通过代理处理。| 否
> 提供商捆绑包 ID | DNS 代理扩展捆绑包 ID。| 否

**《针对 Apple 设备的“中继”设备管理有效负载设置》`dep131693e6b`，发布日期 2024-03-07**

> 使用“中继”有效负载支持安全透明的流量隧道，是访问内部资源时 VPN 的高级替代。支持的有效负载标识符：com.apple.relay.managed 支持的操作系统和通道：iOS 17、iPadOS 17、“共享 iPad”设备、macOS 14 设备、macOS 14 用户、visionOS 1.1。允许的重复项：True：可将多个“中继”有效负载发送给某位用户或某台设备。

> 中继描述 | 可链接在一起的一个或多个中继服务器的词典数组。| 是
> RelayUUID | 此中继配置的全局唯一标识符。此 UUID 用于通过“中继”中包含的服务器路由受管理 App。| 否
> 匹配域 | 用于确定哪个连接应通过“中继”中包含的服务器路由的域字符串列表。与域完全匹配或属于所列域的子域的任何连接都会使用中继服务器，除非它们与排除域匹配。如果未列出任何域，所有域的流量（与排除域匹配的除外）将路由到中继服务器。运行 iOS 18.4、iPadOS 18.4、macOS 18.4、Apple tvOS 18.4、visionOS 2.4 或更高版本的设备支持 FQDN（主机名），以及基于域的规则。……如果你的组织指定了 MatchDomains，则被排除的 FQDN 需要是子域才能生效。如果你的组织没有指定 MatchDomains 或 FQDN，所有流量（排除域除外）都将通过中继。组织也可以排除 FQDN。完全匹配项会绕过中继。| 否

**《在 Apple 设备上使用网络中继》`dep91a6e427d`，发布日期 2023-10-25**

> 运行 iOS 17、iPadOS 17、macOS 14、Apple tvOS 17 或更高版本的设备上可用的网络中继是一种特殊类型的代理，可用于远程访问和隐私解决方案。它们支持安全透明的流量隧道，是访问内部资源时 VPN 的新式替代。**这些中继内建于操作系统的网络堆栈，无需任何自定义 App 代码。网络中继基于现代标准化 MASQUE 协议构建，可用于代理所有 TCP 和 UDP 流量。**这允许十分高效地建立网络中继连接，无需为设备分配另一个 IP 地址。此外，可同时使用多个中继，包括可将已配置的网络中继与 iCloud 专用代理合并。

> 中继配置可以基于匹配域和排除域路由流量。……网络中继也可以通过不定义任何匹配域来应用到整台设备。在这种情况下，所有域的流量（与排除域匹配的除外）将路由到中继。**另外，受管理 App 可以分配到特定网络中继配置。**若要无缝认证中继并建立连接，配置可以参考由分发证书中所述的其中一种可用机制提供的身份证书。

**《针对 Apple 设备的“蜂窝专用网络”设备管理有效负载设置》`depbf9c1aabd`，发布日期 2023-10-25**

> 使用此有效负载提供有关专用网络部署的设备信息，包括地理位置、无线局域网偏好和网络部署类型。支持的有效负载标识符：com.apple.cellularprivatenetwork.managed 支持的操作系统和通道：iOS 17、iPadOS 17、“共享 iPad”设备。

> 首选蜂窝数据 | 优先使用蜂窝专用网络而非无线局域网。| 否

口径要点：
- 组织侧对 App 出站连接的**全部合法改道手段**就是这一组：全局 HTTP 代理 / macOS 网络代理配置 / 加密 DNS（DoT-DoH）/ per-App DNS 代理 / 网络中继（MASQUE，可按域、可按受管理 App）/ per-App VPN / 蜂窝专用网络。**没有一条能改写 TLS 参数**。
- 三个“App 粒度”入口值得单独写：**`com.apple.dnsSettings.managed`「指定需要使用特定 DNS 设置的 App」**、**`com.apple.dnsProxy.managed`「按 App 捆绑包 ID + DNS 代理 UUID 代理该 App 的 DNS 查找」**、**`com.apple.relay.managed`「RelayUUID 用于通过中继路由受管理 App」**——即“组织能指定某个 App 走哪条路”，但仍是路由层，不是安全层。
- “中继”页那句“**无需任何自定义 App 代码**”“**受管理 App 可以分配到特定网络中继配置**”是官方承认的“App 不必配合即可被改道”的最强表述；同时“**可将已配置的网络中继与 iCloud 专用代理合并”**说明中继与用户侧隐私代理可叠加。
- 代理类载荷普遍要求**自动设备注册（即监督）**，只有 `com.apple.dnsSettings.managed` 与 `com.apple.relay.managed`、`com.apple.SystemConfiguration`、`com.apple.cellularprivatenetwork.managed` 接受普通“设备注册/用户注册”。正文写“企业能否静默代理你的 App 流量”时要按载荷逐条区分，不能一刀切。

### 7.5 《在企业网络上使用 Apple 产品》主机矩阵中的“支持代理”列

来源：https://support.apple.com/zh-cn/101555 （已核实逐字，发布日期 2026-07-31；§7.1 已取该页 HTTPS 拦截句）。该页每张表都有第 6 列“**支持代理**”，取值只有“是”与“—”。

> Apple 产品需要访问本文列出的互联网主机，才能使用各种服务。下面介绍了你的设备如何连接到主机并使用代理：

> 除了上面列出的 Apple 账户主机外，Apple 设备必须能够连接到以下域的主机才能使用 iCloud 服务。

> 在安装和更新 App、使用某些 App 功能以及下载附加内容时，Apple 设备需要访问以下主机和域。一些附加内容也可能托管在第三方内容分发网络上。

> 为了验证这篇文章列出的主机所使用的数字证书，Apple 设备必须能够连接到以下主机。

> iOS 14、iPadOS 14、Apple tvOS 14 和 macOS Big Sur 及更高版本中的加密域名系统 (DNS) 解析使用以下主机。

> Apple 设备必须能够连接到以下主机，才能在 iOS 14、iPadOS 14 和 macOS Big Sur 及更高版本中使用关联域。关联域是通用链接的基础，通用链接功能允许在 App 中呈现内容，而不是在 App 的全部或部分网站中呈现内容。接力、轻 App 和单点登录扩展都使用关联域。

“支持代理”列的取证读数（按表格列序 主机/端口/协议/OS/描述/支持代理 解析）：
- `*.push.apple.com` 443、80、5223、2197 —— **是**（与 §7.1 的 PAC 那句互相印证：APNs 可走代理）。
- 设备设置类 `albert/captive/gs/humb/static.ips/apple.com`、`iprofiles`、`mdmenrollment`、`vpp.itunes`、`gdmf`、`identity.apple.com` —— 是；`deviceenrollment`、`deviceservices-external`、`setup.icloud`、`*.appattest`、`axm-servicediscovery` —— —。
- iCloud 服务类 `*.apple-cloudkit.com`、`*.icloud.com`、`*.icloud.com.cn`、`*.icloud.apple.com`、`*.icloud-content.com`、`*.apzones.com`、`*.cdn-apple.com`、`*.gc.apple.com`、`mask.icloud.com`、`mask-h2.icloud.com` —— **全部为“—”**；同段例外：`mask-api.icloud.com`、`probe.icloud.com`、`pong.icloud.com`、`metrics.icloud.com`、`apple-native-relay.apple.com` —— 是。
- 证书验证类 `certs.apple.com`(80/443)、`crl.apple.com`(80)、`crl3.digicert.com`、`crl4.digicert.com`、`ocsp.apple.com`(80)、`ocsp.digicert.com`、`ocsp.digicert.cn`(中国大陆)、`ocsp2.apple.com`(443) —— **全部为“—”**，同段只有 `valid.apple.com`(443) 为“是”。
- `gateway.icloud.com` 443 TCP/UDP —— 描述为“**CloudKit 内容，包括 XProtect 更新和语音控制资源**”，支持代理“—”。
- `doh.dns.apple.com` 443（DoH）—— 是。
- `api.apple-cloudkit.com` 443（macOS）—— 描述“App 公证”，支持代理“—”。

口径要点：
- 这是 Apple 自己给出的**“哪些 Apple 服务连接可以被代理、哪些不行”的官方矩阵**，比任何第三方抓包教程权威。正文可写成：企业网络里“插一个代理”并不等于能覆盖 Apple 服务面——**iCloud/CloudKit 数据面与绝大多数证书吊销校验主机被标记为不支持代理**。
- 由此可推出一条可操作的审计口径：**若代理链路只放行了 `*.apple-cloudkit.com` 却没放行 `certs.apple.com`/`crl.apple.com`/`ocsp*.apple.com`，App 或服务端的证书校验会先失败**（与 §10.1 的“违反要求的 TLS 服务器连接将失败”呼应）。
- `*.appattest.apple.com`（App 验证/设备证明）标“—”，与 `ppq.apple.com`（企业 App 验证）不同段，正文若要谈“上传凭据之外的设备证明通道”可引本行，但**该页只给主机/端口，不给协议细节**（缺口）。
- 读数方法说明：以上取值来自该页表格文本的列序解析；`101555` 正文没有单独句子解释“支持代理”的定义，因此**不得把“—”解读为“被禁止走代理”**，只能写“Apple 在本表中未标注其支持代理”。


---

## 8. watchOS：App 的网络路径是否必须经 iPhone

状态：**已核实**（逐字，手册《watchOS 系统安全性》`secc7d85209d`，发布日期 **2026-01-28**）
（#91 §9 已取本页配对通道与 ML-KEM 部分，见 `_facts_net_channel.md` 第 308 行；此处只补**网络路径/直连**相关句。）

> 支持的技术包括“系统安全性”中所列技术（例如 KIP、SKP 和 SCIP）以及数据保护、钥匙串和网络技术。

App 流传输的加密与 IDS/直连二选一（**关键句**）：

> 为支持需要流传输数据的 App，加密采用了 FaceTime 通话安全性中所描述的方法，即使用配对 iPhone 所提供的 Apple 身份服务 (IDS) 或直接互联网连接。

链路层加密的继承关系（逐字）：

> 蓝牙会话密钥被丢弃，iPhone 和 Apple Watch 间的所有通信（通过加密蓝牙、无线局域网和提供二级加密层的蜂窝链路）使用以上列出的一种方法加密。

无线局域网/蜂窝接管（逐字）：

> 当 Apple Watch 不在蓝牙通信范围内时，可以转而使用无线局域网或蜂窝网络。如果配对 iPhone 已经加入过某个无线局域网，且其网络凭证在两台设备处于相互覆盖范围内时已经同步到 Apple Watch，Apple Watch 会自动加入此网络。此“自动加入”行为可稍后在 Apple Watch “设置” App 中的“无线局域网”部分对每个网络进行配置。两台设备从未加入过的无线局域网可在 Apple Watch “设置” App 中的“无线局域网”部分手动加入。

服务端直连（逐字，**Watch 不总是经 iPhone 中转**的一手证据）：

> Apple Watch 和 iPhone 未在通信范围内时，Apple Watch 会直接接入 iCloud 和 Gmail 服务器来取回邮件，而不是通过互联网与配对 iPhone 同步邮件数据。对于 Gmail 账户，用户必须在 iPhone 上 Watch App 中的“邮件”部分进行 Google 认证。从 Google 收到的 OAuth 令牌会以加密格式通过 Apple 身份服务 (IDS) 发送到 Apple Watch，用于取回邮件。此 OAuth 令牌绝不会用于从配对 iPhone 连接 Gmail 服务器。

口径要点：
- **“使用配对 iPhone 所提供的 Apple 身份服务 (IDS) 或直接互联网连接”** 与 **“Apple Watch 会直接接入 iCloud 和 Gmail 服务器”** 两句一起，否掉“Watch 上 App 的流量一律经 iPhone 代理”的流传说法；配对链路加密只是**设备间通道**的保护，不覆盖 Watch 独立联网场景。
- OAuth 令牌下发路径（Google → 经 IDS 加密通道 → Watch，且**绝不再用于从 iPhone 连接 Gmail**）= 令牌作用域被刻意限定的官方样本，可与“上传凭据”话题相接。
- 本页 H2 清单（供正文对齐）：更新 watchOS／手腕检测／激活锁／与 iPhone 安全配对／使用 Apple Watch 在 macOS 中批准／安全使用无线局域网、蜂窝网络、iCloud 和 Gmail。

---

## 9. macOS 防火墙与入站/出站连接控制

### 9.1 macOS 中的防火墙安全性

状态：**已核实**（整页逐字，`seca0e83763f`，发布日期 2021-02-18）

> macOS 包括内建防火墙，以保护 Mac 免受网络访问攻击和拒绝服务攻击。若要进行配置，请前往“系统设置”>“隐私与安全性”（macOS 13 或更高版本）、“系统偏好设置”的“安全性与隐私”面板（macOS 12 或更低版本），或者使用随防火墙有效负载手动安装或设备管理服务提供的配置描述文件。支持以下配置：
> - 无视 App 而阻止所有传入连接。
> - 自动允许内建软件接收传入连接。
> - 自动允许已下载的签名软件接收传入连接。
> - 基于用户指定的 App 添加或拒绝访问。
> - 阻止 Mac 对 ICMP（互联网控制消息协议）探查和端口扫描请求作出响应。

口径要点：
- 五个配置项**全部限定为传入连接（inbound）**；本页**没有任何出站过滤**表述 —— 这是“macOS 内建防火墙不约束 App 出站上传”的一手依据（正文若写“防火墙可管出站”必须标为缺口）。
- “**已下载的签名软件**”作为允许条件之一 = 入站许可与代码签名状态绑定，可与代码签名篇交叉引用。
- 配置途径三选：系统设置／手动安装的**防火墙有效负载**／设备管理服务提供的配置描述文件 → 组织可改入站策略（“谁能改变连接要求”的组织分支）。

### 9.2 其他 macOS 系统安全性功能

状态：**已核实**（整页逐字，`sec2fd2cc63d`，发布日期 2021-02-18）—— 本页是目录页，**不含任何网络连接控制条目**。

> macOS 可在更大范围的一系列硬件上运行（例如基于 Intel 的 CPU、基于 Intel 的 CPU 和 Apple T2 安全芯片的组合以及基于 Apple 芯片的 SoC），且支持各种各样的通用计算使用场景。部分用户仅使用预装 App 或 App Store 中提供的 App，另一些用户则是内核专家，他们需要将平台保护措施置于不活跃状态，从而以最高信任级别运行和测试自己的代码。大部分用户都处于这两者之间，且许多用户拥有需要不同访问级别的外围设备和软件。

> Apple 在设计 macOS 平台时采用的是硬件、软件和服务相集成的方法，该平台在设计上安全可靠，并可方便地进行配置、部署和管理，但也保留了用户需要的可配置性。macOS 还具备 IT 专业人员所需的关键安全技术，方便他们保护公司数据以及集成到安全的企业网络环境中。

> 以下功能可支持且有助于保护 macOS 用户的各种需求，其中包括：签名系统宗卷安全性／系统完整性保护／信任缓存／外围设备保护／搭载 Apple 芯片的 Mac 上的 Rosetta 2（自动转译）支持和安全性／DMA 支持和保护／内核扩展 (Kext) 支持和安全性／Option ROM 支持和安全性／基于 Intel 的 Mac 电脑的 UEFI 固件安全性

→ 结论（可写正文）：**手册 macOS“其他安全性功能”清单里没有网络/连接控制项**；macOS 侧与连接有关的条目只有 §9.1 防火墙页与 #91 §6.3 VPN 页。锁定模式的 Mac 行为在 support 文章（§2.1），不在本页。

---

## 10. support 侧网络文档：TLS 量子安全准备（服务器侧前提）

状态：**已核实**（整篇逐字，https://support.apple.com/zh-cn/122756 《让你的网络为 TLS 中的量子安全加密做好准备》，发布日期 2026-06-25）
（本页就是 #91 §1.1 末句与 §3.1“系统仅在连接支持的服务器时支持量子加密”所指向的那篇文章。）

> 了解 TLS 中的量子安全加密，明确如何检查所在组织的网页服务器是不是已准备就绪。
> 本文适用于企业和教育机构的网络管理员。
> 在 iOS 26、iPadOS 26、macOS Tahoe 26 和 visionOS 26 中，受 TLS 保护的连接将自动声明对 TLS 1.3 中量子安全混合密钥交换的支持。这样，便可与支持量子安全密钥交换算法的 TLS 1.3 服务器就这一算法进行协商，同时有助于与尚不支持这一新算法的服务器保持兼容。使用量子安全加密（也称为“后量子加密”），旨在防止攻击者记录 TLS 连接流量并在以后使用未来的量子电脑来解密内容。
> 来自 iOS 26、iPadOS 26、macOS Tahoe 26 和 visionOS 26 设备的 ClientHello 信息将在 supported_groups 扩展中包含 X25519MLKEM768（请参见注册表），并在 key_share 扩展中包含相应的密钥共享。服务器可以选择 X25519MLKEM768（如果支持），也可以使用 ClientHello 信息中声明的另一个组。

检查服务器是否支持（逐字）：

> 从 macOS Tahoe 26 开始，你可以使用以下命令检查自己的 HTTPS 服务器是不是支持 X25519MLKEM768 密钥交换算法：
> nscurl --tls-diagnostics https://test.example
> 支持量子安全加密的服务器将返回以下信息：
> Negotiated TLS version (codepoint): 0x0304
> Negotiated TLS key exchange group (name): X25519MLKEM768
> Negotiated TLS ciphersuite (codepoint): 0x1302
> 不支持量子安全加密的服务器将在 TLS 握手期间选择其他支持的组，以允许 iOS 26、iPadOS 26、macOS Tahoe 26 和 visionOS 26 设备进行连接。

故障诊断（逐字）：

> 如果 TLS 的实现不正确 ，无法读取大型 ClientHello 信息，装有 iOS 26、iPadOS 26、macOS Tahoe 26 和 visionOS 26 的设备可能无法连接到某些旧版服务器。

量化基线：客户端在 **ClientHello 的 supported_groups** 声明 `X25519MLKEM768`、**key_share** 带对应共享；协商成功的可观测判据＝TLS 版本 codepoint **0x0304**（TLS 1.3）＋组名 **X25519MLKEM768**＋密码套件 codepoint **0x1302**（TLS_AES_256_GCM_SHA384）。
口径要点：
- 这是 Apple 一手、面向“**你自己的服务器**”的上传对端核查文档：客户端只“声明”，**选择权在服务端**，与 #91 §3.1“系统仅在连接支持的服务器时支持量子加密”闭环。
- 自查命令 `nscurl --tls-diagnostics`（macOS Tahoe 26 起）可直接进自查清单。
- 本页只列 **iOS/iPadOS/macOS Tahoe/visionOS 26**，**未列 Apple tvOS 与 watchOS**（手册 `secc7c82e533` 那页列了 tvOS 26/watchOS 26）；引用适用范围时两页措辞不同，需按页标注。
- “无法读取大型 ClientHello 信息”= Apple 官方承认**中间盒/旧版服务器会因 ClientHello 变大而阻断连接** —— 属“链路中第三方设备能改变连接结果”的证据，与 §7 代理/内容过滤器同章。

### 10.1 可信证书要求（support 文章，ATS 之外的系统级证书硬约束）

状态：**已核实**（整篇逐字，https://support.apple.com/zh-cn/103769 《iOS 13 和 macOS 10.15 中的可信证书应满足的要求》，`<time itemprop="datePublished">` = **2023 年 11 月 06 日**，页脚 Copyright © 2026 Apple Inc. 保留所有权利。）
（本页是手册 `sec100a75d12` TLS 安全性点名链接的支持文章；#91 只取了手册页，未取本文。）

> 了解 iOS 13 和 macOS 10.15 中的 TLS 服务器证书应满足的新安全要求。

> 在 iOS 13 和 macOS 10.15 中，所有 TLS 服务器证书都必须符合这些新的安全要求：

> 使用 RSA 密钥的 TLS 服务器证书和签发证书的 CA 必须使用长度大于或等于 2048 位的密钥。TLS 不再信任所用 RSA 密钥长度小于 2048 位的证书。

> TLS 服务器证书和签发证书的 CA 必须在签名算法中使用 SHA-2 系列中的哈希算法。TLS 不再信任 SHA-1 签名的证书。

> TLS 服务器证书必须在证书的“使用者备用名称”扩展中显示服务器的 DNS 名称。证书的 CommonName 中的 DNS 名称不再受信任。

> 此外，所有在 2019 年 7 月 1 日后签发的 TLS 服务器证书（如证书的 NotBefore 字段中所示）必须遵循以下准则：

> TLS 服务器证书必须包含一个内含 id-kp-serverAuth OID 的 ExtendedKeyUsage (EKU) 扩展。

> TLS 服务器证书的有效期必须为 825 天或更短（如证书的 NotBefore 和 NotAfter 字段中所示）。

> 违反上述新要求的 TLS 服务器连接将失败，并可能导致网络出现故障、应用无法运行，以及在 iOS 13 和 macOS 10.15 的 Safari 浏览器中无法载入网站。

口径要点：
- 这是**系统级（非 ATS）**证书准入清单，且是“**所有 TLS 服务器证书**”而非“仅 App 的 TLS”，可用于回答“传输安全要求由谁定”里的“操作系统”一层：RSA ≥ 2048、签名算法属 SHA-2 系列、SAN 必须承载 DNS 名（CommonName 不再受信任）、2019-07-01 后签发需含 `id-kp-serverAuth` EKU、有效期 ≤ 825 天。
- “**违反上述新要求的 TLS 服务器连接将失败**”= 硬失败口径，与 §1.5（描述文件无法放宽）、§4.1（指南无 HTTPS 条款）共同构成“要求来自 OS，不来自审核政策”的三段论。
- 注意本页的“825 天”是 **2019 年 Apple 政策口径**；正文若还要谈更短的现行 CA/B 有效期限制，属于另一个来源，本简报未取证（见 §12）。
- 本页**没有**任何关于“App 如何为单个域放宽这些要求”的表述——放宽机制只在 Info.plist（见同事简报 `_facts_app_ats_dev.md` §2）。

### 10.2 APNs 与推送通道（提供方服务器侧的连接要求）

状态：**已核实**（逐字英文，developer.apple.com UserNotifications 文档，页脚 `Copyright © 2026 Apple Inc. All rights reserved.`；本文只取与“出站连接条件/上传通路”直接相关的句子）

https://developer.apple.com/documentation/usernotifications/sending-notification-requests-to-apns （“Sending notification requests to APNs”，摘要“Transmit your remote notification payload and device token information to Apple Push Notification service (APNs).”）

> **Use HTTP/2 and TLS 1.2 or later to establish a connection between your provider server and one of the following servers:**

> Use the production server for your shipping apps and the development server for testing. When sending many remote notifications, you can establish multiple connections to these servers to improve performance. **You can also use port 2197 (instead of port 443) on either server when communicating with APNs. You might use this port to allow APNs traffic through your firewall but to block other HTTPS traffic.**

> APNs allows multiple concurrent streams for each connection, but don’t assume a specific number of streams. The exact number varies based on server load and whether you use a provider certificate or an authentication token.

> As a best-effort service, APNs may reorder notifications you send to the same device token.

> Upon receiving your server’s POST request, APNs validates the request using either the provided authentication token or your server’s certificate. If validation succeeds, APNs uses the provided device token to identify the user’s device. It then tries to send your JSON payload to that device.

https://developer.apple.com/documentation/usernotifications/establishing-a-connection-to-apns （“Establishing a connection to Apple Push Notification service (APNs)”，摘要“Secure your communications with APNs to send API requests.”）

> Based on the type of request you’re using, there’s a different development and production endpoint. If you’re shipping apps, use the production end points. If you’re testing, use the development endpoints.

> **APNs requires the use of HPACK (header compression for HTTP/2), which prevents repeatedly storing header keys and values. APNs maintains a small dynamic table for HPACK.** To avoid filling up that table, encode your headers in the following way—especially when using many streams:

> If you experience a revoked provider certificate, or if you revoke your authentication token, close all connections to APNs, fix the problem, and then open new connections.

https://developer.apple.com/documentation/usernotifications/establishing-a-certificate-based-connection-to-apns （摘要“Secure your communications with Apple Push Notification service (APNs) by installing a certificate on your provider server.”）

> With certificate-based authentication, you use a provider certificate to establish a secure connection between your provider server and APNs. You obtain this certificate from Apple through your developer account. Because you establish trust at the server level, individual notification requests contain only your payload and a device token. They don’t include an authentication token, which reduces the size of each notification request slightly.

> You can use a provider certificate to send notifications to a single app, the Apple Watch complication, or to background VoIP services associated with a single app. To send remote notifications to multiple apps, you must create separate certificates for each app.

> Certificates must be tied to a specific app. Tie a different provider certificate to each app, whose App ID you specify when creating the certificate.

> Install both the certificate and the private key on your provider server.

交叉引用：#91 已逐字取过手册侧《设备管理安全性概览》`sec013b5d35d` 的 APNs 那句与 `sec101555` 的主机端口表（见 `_facts_net_channel.md` 第 325 行、本简报 §7.1/§7.5），此处不重抄。

口径要点：
- 正文写“推送这条上传通道的传输安全要求由谁定”时，可引两句不同层：**设备→APNs** 由 Apple 服务与主机矩阵定义（§7.1 的 PAC/代理那句、§7.5 的 `*.push.apple.com` 443/80/5223/2197）；**你的服务器→APNs** 由本节的 HTTP/2 + TLS 1.2 or later + HPACK + 2197 定义。两条都是 Apple 单方规定，App 开发者与用户都无法放宽。
- “**2197 端口可用来只放行 APNs 而屏蔽其它 HTTPS**”是官方给出的防火墙口径，直接可用于“组织能施加什么”一章（能限制，不能改写）。
- 证书与令牌两种信任模型要分开写：证书 = 服务器层建立信任、请求体更小、**一 App 一证书**；令牌 = 请求内自带认证（详见 B7/APNs device token 口径在 `_facts_dev_certs_tokens.md` 第 369–386 行，本期不重抄）。
- “**APNs allows multiple concurrent streams…don’t assume a specific number**”“**best-effort service, APNs may reorder**”可用来支撑“推送不是可靠上传通道”的论断。

---

## 11. 量化基线表（可直接进正文）

| 项目 | 一手数值/口径 | 来源（发布日期） | 本简报小节 |
| --- | --- | --- | --- |
| 配置描述文件载体加密 | CMS（RFC 5652），支持 3DES 与 AES-128 | 手册 `secf6fb9f053`（2024-05-07） | §1.1 |
| MDM 通道 | 仅 HTTPS；证书与 Check-In 密钥不可更换；AccessRights 位 32 | developer Device Management《MDM》（© 2026） | §1.2 |
| 全局 HTTP 代理载荷数量 | 同一时刻只能有一个（`There can only be one payload of this type`）；重复项 False | dep7ba46fcd（2022-10-24）＋载荷页 | §7.3/§7.4 |
| PAC 分发协议 | iOS 13/macOS 10.15 起仅 `http://`、`https://` | `com.apple.proxy.http.global`（© 2026） | §7.3 |
| 内容过滤档位 | `FilterBrowsers` / `FilterSockets` / `FilterPackets` / `FilterURLs` / `FilterGrade` | dep1129ff8d2（2025-09-24）＋载荷页 | §7.2/§7.3 |
| URL 过滤隐私保护 | 完整 URL + Bloom 计数 + PIR，经 Apple 托管 Oblivious HTTP 中继 | dep1129ff8d2（2025-09-24） | §7.2 |
| 网络中继 | MASQUE；代理全部 TCP/UDP；iOS 17/iPadOS 17/macOS 14/tvOS 17 起；18.4/2.4 起支持 FQDN 规则 | dep91a6e427d（2023-10-25）、dep131693e6b（2024-03-07） | §7.4 |
| TLS 证书准入（系统级） | RSA ≥ 2048；SHA-2 签名；DNS 名必须在 SAN；2019-07-01 后需 `id-kp-serverAuth` EKU；有效期 ≤ 825 天 | support 103769（2023-11-06） | §10.1 |
| 证书验证主机 | certs/crl/ocsp.apple.com、ocsp2.apple.com、crl3/crl4.digicert.com、ocsp.digicert.com、ocsp.digicert.cn（中国大陆）、valid.apple.com | support 101555（2026-07-31） | §7.5 |
| 后量子 TLS 客户端声明 | ClientHello `supported_groups` 含 `X25519MLKEM768` + `key_share`；成功判据 0x0304 / X25519MLKEM768 / 0x1302 | support 122756（2026-06-25） | §10 |
| APNs 提供方连接 | HTTP/2 + TLS 1.2 or later；HPACK 必需；可选 2197 替代 443 | usernotifications 两页（© 2026） | §10.2 |
| 推送存储 | APNs 保存通知 ≤ 30 天；每个 bundle ID 只存一条 | `sending-notification-requests-to-apns`（© 2026） | §10.2 |
| 锁定模式网络项 | 不自动加入不安全 Wi-Fi 并断开已连接的不安全 Wi-Fi；关闭 2G/3G；禁止安装描述文件与 MDM/DEP 注册；可按联系人限制信息；可按 App 排除 WebKit 网页内容限制；**不是可配置的 MDM 选项** | support 105120（2026-09-18） | §2 |
| 私有无线局域网地址 | 三档（关闭/固定/轮替，iOS 18+）；隐藏网络探测抑制；可由设备管理服务关闭（visionOS 1.1+） | 手册 `sec31e483abf`（2024-12-19） | §3 |
| iCloud 上传加密顺序 | 设备生成密钥 → 先加密后上传 → 传输层 TLS 另加一层 → 静态再一层 | 手册 `secacde2d0da`（2022-12-07） | §6 |
| E2EE 数据类别数 | 手册 14 → 23；support（较新）15 → 25 | `secacde2d0da`/`sec973254c5f`（2022-12-07）；support 102651（2026-01-08） | §6/§6.3 |
| CloudKit 字段加密 | 字段必须被开发者显式声明为加密，读写需专门 API；资源（asset）由 ADP 统一覆盖 | `sec973254c5f`（2022-12-07）＋102651（2026-01-08） | §6.3 |
| 明文可见面（即使开 ADP） | 校验和（收敛加密）/修改时间/共享标题与缩略图/“任何拥有链接的用户”/iWork 实时协作/邮件·通讯录·日历 | 同上 | §6.3 |
| 文件保护级别 | `complete` 锁定即不可读写；`completeUnlessOpen` 关闭后加密；`completeUntilFirstUserAuthentication` 开机后首个用户认证前不可访问；`none` | Foundation `FileProtectionType`（© 2026） | §6.4 |
| CloudKit 资源上传 | 存记录时同时上传 record 与 asset；取回时资源落在会被定期清理的 staging area | CloudKit `CKAsset`（© 2026） | §6.4 |
| 出口合规豁免 | “Typically, the use of encryption that’s built into the operating system—for example, when your app makes HTTPS connections using `URLSession`—is exempt…”；键 `ITSAppUsesNonExemptEncryption`、`ITSEncryptionExportComplianceCode` | Security《Complying with Encryption Export Regulations》（© 2026） | §5 |
| watchOS App 网络路径 | 流传输类 App 用配对 iPhone 提供的 IDS **或直接互联网连接**；Apple Watch 直接接入 iCloud 与 Gmail 服务器 | 手册 `secc7d85209d`（2026-01-28） | §8 |
| macOS 防火墙 | 五个配置项全部只作用于**传入**连接；可经防火墙有效负载/描述文件配置 | 手册 `seca0e83763f`（2021-02-18） | §9.1 |
| App Store 审核指南 | 现行版正文 0 处 HTTPS/TLS/ATS/encrypt 相关条款（全文检索为负） | Guidelines（Last Updated 2026-10-16，© 2026） | §4.1 |

---

## 12. 待补 / 预期无一手页

**A. 预期就没有一手专页（已用目录级取证证明，正文须写成“Apple 未就此发布专页”，不得写成结论性缺失）**
1. **配置描述文件放宽/覆盖 ATS**：`guide/deployment/toc` 220 条 + Restrictions 210 键全量核对，无 ATS 相关载荷或键（§1.5）。
2. **App Store 审核指南中的 ATS/HTTPS 条款**：任务书假设的 5.A.1/5.A.2 安全连接文本在现行版正文检索为 0 命中；“审核要求从哪来”只能落到手册 `secb8f887a15` 与 ATS 键级文档（§4.1/§4.3）。
3. **iCloud 云盘 / iCloud 键值数据存储独立安全性页**：`guide/security/toc` 178 条中不存在；口径只能落在 `secacde2d0da` + `sec3cac31735`（§6）。
4. **「iCloud 专用 App 隐私」专页**：两个 TOC 均无标题含“专用 App”的条目，support 侧亦未检索到（§6.3）。
5. **macOS“其他系统安全性功能”内的连接控制项**：`sec2fd2cc63d` 目录页清单不含网络项（§9.2）。
6. **手册版“锁定模式”细节页**：`sec2437264f0` 只是目录页，逐条限制只在 support 105120（§2.1）。
7. **Apple 内建防火墙的出站过滤**：`seca0e83763f` 全篇只谈入站；任何“macOS 防火墙管出站”的说法在本简报来源里无据（§9.1）。
8. **App 侧“抓包/SSL 检查”官方指引**：Apple 只有 §7.1 那句“将中断任何使用 HTTPS 拦截（SSL 检查）的连接”，没有面向开发者的“如何检测被拦截”一手页。

**B. 本轮抓取失败/未取全，正文引用前需复核**
1. **APNs 提供方信任根**（“证书链须由 AAA Certificate Services / SHA-2 Root: USERTrust RSA 签发”一句）：`usernotifications/authenticating-your-provider-with-apns` 404，未取到逐字。待试：`.../establishing-a-certificate-based-connection-to-apns` 全文后半（本轮只取前 2200 字符）、`.../deviceconnection-requirements`、support《APNs 连接问题故障诊断》（101555 正文点名“了解如何对 Apple 推送通知服务 (APNS) 的连接问题进行故障诊断”，本轮未跟链）。
2. **`101555` 的“近期修订记录”表**：正文尾部只解析到 TOC 项，未取到逐条修订行；页脚仅有 © 年份。若要写“该矩阵的变更时间线”需补抓。
3. **`URLFileProtection` Overview 原句**：DocC JSON 中该句两处引用链接文本被抽成 identifier，仅摘要可逐字（“Protection-level values for a URL resource key.”），Overview 句标**部分核实**（§6.4）。
4. **`CKAsset` 的属性名**（staging location 的属性、orphan 时赋的空值）：同上，抽取时丢失，正文如需引用请回页面复核（§6.4）。
5. **825 天之后的证书有效期现行上限**（CA/Browser Forum 398/397 天等）：非 Apple 一手来源，本简报不取证；正文若提到需另立来源或写成缺口。
6. **App Store Connect Help 的 Export Compliance 条目**（提交界面字段级原文）：属 `help.apple.com` 域，本轮未抓（§5.1）。
7. **`NSFileProtectionComplete` 阻断后台上传 / URLSession background session 使用哪一档保护**：未找到任何 Apple 一手页直述此因果（§6.4 末），正文只能写成推断或缺口。
8. **visionOS / Apple tvOS 侧对应条目**：§8 只取了 watchOS；tvOS/visionOS 的 App 网络路径与代理限制未取证。
9. **`sec28a79bf17`（iPhone 蜂窝网络通话中继安全性）**：在 security TOC 中已定位，但与本报告的“App 出站连接”主线关联弱，未抓；如正文要谈“通话中继是否借用 Wi-Fi/蜂窝上传”需补。
10. **`guide/personal-safety/ipsc169ffeb0`（将数据安全储存在 iCloud 中）**：搜索命中的是 Apple 一手页（用户安全指南域），本轮未抓；若正文需要“个人用户侧”措辞可比 §6 更贴近读者。
