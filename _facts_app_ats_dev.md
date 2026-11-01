# 报告 #92 核查简报（developer.apple.com 侧）：iOS 应用网络连接的传输安全要求与上传机制分析

取证日期：2026-10-22。来源仅限 developer.apple.com 一手文档页面（含 Technical Notes）。所有引文为**英文原文逐字照抄**，中文释义放在「口径要点」中。每条带 URL；developer 文档页页脚统一为 `Copyright © 2026 Apple Inc. All rights reserved.`，记为"截至 2026 可用"；TN 类页面记录正文里的修订日期。
标注：**已核实**（逐字引文）/ **部分核实**（口径存在但措辞不完整，或仅抓到部分键）/ **未核实**（无可引页面 / 抓取失败 / 页面不存在）。
范围约定：support.apple.com 手册页侧由另一位同事负责，本文件不重复。

---

## 0. 抓取方法与页面清单

取证手段：`developer.apple.com` 的文档正文由 JSON 端点 `https://developer.apple.com/tutorials/data/documentation/<slug>.json` 提供（页面本身是 SPA，直接抓 HTML 会得到 "The page you're looking for can't be found."）。本简报所有引文均取自该 JSON 端点的 `primaryContentSections`（`abstract` / `content` / `details` / `attributes` / `declarations` 段），即页面渲染所用的原始文本，**逐字**。归档（archive）技术说明走 `https://developer.apple.com/library/archive/...` 的静态 HTML。

> 重要：**用户/同事给的 `https://developer.apple.com/documentation/security/app_transport_security` 返回 404**（JSON 端点亦 404，SPA 显示 "The page you're looking for can't be found."）。现行 ATS 一手页是：
> - `https://developer.apple.com/documentation/Security/preventing-insecure-network-connections`（collectionGroup，标题 *Preventing Insecure Network Connections*）
> - `https://developer.apple.com/documentation/bundleresources/information-property-list/nsapptransportsecurity`（配置字典）
> 写正文时**不要**引用 `app_transport_security` 这个 URL。

已核实抓取的页面（全部 HTTP 200，页脚统一 `Copyright © 2026 Apple Inc. All rights reserved.`）：

| # | URL | 内容 |
|---|---|---|
| 1 | /documentation/Security/preventing-insecure-network-connections | ATS 总纲（四条默认要求 + 扩展检查 + 例外结构 + 审核理由） |
| 2 | /documentation/Security/identifying-the-source-of-blocked-connections | `nscurl --ats-diagnostics` |
| 3 | /documentation/bundleresources/information-property-list/nsapptransportsecurity | ATS 字典 + Versioning |
| 4 | …/nsapptransportsecurity/nsallowsarbitraryloads | 全局例外 |
| 5 | …/nsapptransportsecurity/nsallowsarbitraryloadsinwebcontent | WebView 例外 |
| 6 | …/nsapptransportsecurity/nsallowsarbitraryloadsformedia | AVFoundation 例外 |
| 7 | …/nsapptransportsecurity/nsallowslocalnetworking | 本地网络例外（含 iOS 17 变化） |
| 8 | …/nsapptransportsecurity/nsexceptiondomains | 域名例外字典 + 域名书写规则 |
| 9 | …/nsapptransportsecurity/nspinneddomains | **ATS 声明式证书固定**（新发现，同事清单里没有） |
| 10 | /documentation/bundleresources/information-property-list/nsexceptionallowsinsecurehttploads | 域级键 |
| 11 | …/information-property-list/nsexceptionminimumtlsversion | 域级键 |
| 12 | …/information-property-list/nsexceptionrequiresforwardsecrecy | 域级键（默认 YES） |
| 13 | …/information-property-list/nsincludessubdomains | 域级键 |
| 14 | …/information-property-list/nsrequirescertificatetransparency | **已被标记 obsolete** |
| 15 | …/information-property-list/nsrequiresniaptlspackageversion | NIAP 合规模式（收紧方向） |
| 16 | …/information-property-list/nsexceptionrequiresniaptlspackageversion | 同上，域级 |
| 17 | /documentation/Foundation/performing-manual-server-trust-authentication | 手动服务端信任评估（含"ATS 下只能收紧不能放松"原话） |
| 18 | /library/archive/technotes/tn2232/_index.html | **HTTPS Server Trust Evaluation**（真正的 TN2232，非 TN2287） |

抓取失败 / 404 的候选（记为未核实）：
- `documentation/security/app_transport_security`（404）
- `documentation/security/app-transport-security`（404）
- `documentation/bundleresources/information-property-list/nsexceptionforwardsecrecyciphersuitesorder`（404）
- `documentation/bundleresources/information-property-list/nsthirdpartyexceptionallowsinsecurehttploads`（404）
- `documentation/bundleresources/information-property-list/nsthirdpartyexceptionminimumtlsversion`（404）
- `documentation/bundleresources/information-property-list/nsapptransportsecurity/nspinneddomains/nspinnedcaidentities`（404，内容并入父页 #9）
- `documentation/bundleresources/information-property-list/nsapptransportsecurity/nspinneddomains/nspinnedleafidentities`（404，同上）
- `documentation/foundation/nsurlsessionconfiguration`（404；现行为 `foundation/urlsessionconfiguration`）
- `documentation/technotes/tn2287-https-server-trust-evaluation`（404）

---

## 1. App Transport Security（ATS）总纲

### 1.1 ATS 定义与生效条件（Preventing Insecure Network Connections）
状态：**已核实**（逐字）
URL：https://developer.apple.com/documentation/Security/preventing-insecure-network-connections （页脚 © 2026）

Abstract：
> Enforce secure network links in your app by relying on App Transport Security.

正文首段：
> On Apple platforms, a networking security feature called App Transport Security (ATS) improves privacy and data integrity for all apps and app extensions. It does this by requiring that network connections made by your app are secured by the Transport Layer Security (TLS) protocol using reliable certificates and ciphers. ATS blocks connections that don’t meet minimum security requirements.

> ATS operates by default for apps linked against the iOS 9.0 or macOS 10.11 SDKs or later. In cases where you need to connect to a server that isn’t fully secure—and that you can’t reconfigure to make more secure—you can add exceptions to loosen some of the ATS requirements.

>>> [NOTE]
> If you link your app against an SDK older than iOS 9.0 or macOS 10.11, ATS is disabled no matter which version of the operating system your app runs on.

口径要点（写正文时用）：
- 生效判定是**链接时 SDK 版本**（linked against the iOS 9.0 … SDKs or later），不是运行时系统版本，也不是 App 部署目标。这三者在正文里必须区分开：`NSAppTransportSecurity` 页的 Versioning 段用词同样是 "apps linked against the iOS 9.0 or macOS 10.11 SDKs"。
- 「can't reconfigure」是官方给例外的**前置理由**，与 §1.5 的审核理由要求配套。
- ATS 的定位是"在默认 TLS 信任评估之上再加检查"（extended security checks，见 §1.3 与 §3），不是替代信任评估。

### 1.2 ATS 由哪个 API 层强制执行 / 哪些 API 不受 ATS 约束（本页最值钱的一段）
状态：**已核实**（逐字）—— 这是"传输安全要求由谁配置"的主证据句。

> The system enforces ATS when you use the standard `URL Loading System`. Instances of `URLSession` automatically negotiate the most secure connection available from the server. The only action your app must take is to use secure URLs, like those beginning with `https`. Otherwise, ATS denies the connection and prints a console message:

代码块（逐字）：
```
App Transport Security has blocked a cleartext HTTP (http://) resource
load since it is insecure. Temporary exceptions can be configured via
your app's Info.plist file.
```

> ATS doesn’t apply to calls your app makes to lower-level networking interfaces like the `Network` framework or `CFNetwork`. In these cases, you take responsibility for ensuring the security of the connection. You can construct a secure connection this way, but mistakes are both easy to make and costly. It’s typically safest to rely on the `URL Loading System` instead.

口径要点：
- **"ATS 自动应用于 URLSession/NSURLConnection/CFNetwork"这类说法在现行 developer 页上是反向的**：现行原话是 ATS *不适用* 于 Network framework 与 CFNetwork 直调；`URLSession`（URL Loading System）才是被强制的对象。老手册页（support 侧）写的是 "NSURLConnection、CFURL 或 NSURLSession"。两条都要引，正文里注明"手册页与 developer 文档口径不同"。
- 「most secure connection available from the server」= 客户端**协商**而非硬性要求；硬性下限来自 §1.3 的检查项。
- 「you take responsibility」= 用 Network.framework 上传时，ATS 的 Info.plist 键**一概不生效**，必须自己配 TLS 参数（与 §4、§6 相接）。

### 1.3 默认服务端信任评估（四条）与 ATS 扩展检查（五条）
状态：**已核实**（逐字，含列表全项）

> A secure server establishes its identity using an X.509 digital certificate. A connecting client examines this certificate to perform default server trust evaluation, which includes checking that the certificate:
> - Has an intact digital signature, showing that the certificate hasn’t been tampered with.
> - Isn’t expired.
> - Has a name that matches the server’s DNS name.
> - Is signed by another valid certificate, which is in turned signed by another, and so on back to a trusted anchor certificate, which must be issued by a Certificate Authority (CA). The anchor certificate must be part of the client operating system, as indicated in `Lists of available trusted root certificates in iOS`, or be installed on the client by the user or a system administrator.

（注：`in turned` 是页面原文拼写错误，引文按原样照抄，正文引用时不要"顺手修正"。）

> ATS requires all of these things, and then provides extended security checks:
> - The server certificate must be signed with either a Rivest-Shamir-Adleman (RSA) key of at least 2048 bits, or an Elliptic-Curve Cryptography (ECC) key of at least 256 bits.
> - The certificate must use the Secure Hash Algorithm 2 (SHA-2) with a digest length, sometimes called a *fingerprint*, of at least 256 bits (that is, SHA-256 or greater).
> - The connection must use Transport Layer Security (TLS) protocol version 1.2 or later.
> - Data must be exchanged using either the AES-128 or the AES-256 symmetric cipher.
> - The link must support perfect forward secrecy (PFS) through Elliptic Curve Diffie-Hellman Ephemeral (ECDHE) key exchange.

量化基线（可直接进"量化基线表"）：RSA ≥ 2048 bits 或 EC ≥ 256 bits；SHA-256 及以上；**TLS ≥ 1.2**；对称密码只有 AES-128/AES-256；必须 ECDHE 前向保密。默认信任评估四项 = 签名完整 / 未过期 / 名称匹配 DNS / 链到受信任 anchor（anchor 可来自系统内置或**由用户或系统管理员安装**）。

### 1.4 ATS 启用时"只能收紧、不能放松"信任评估（核心口径句，两处独立证据）
状态：**已核实**（逐字，同一口径出现于两个页面）

证据 A — `Preventing Insecure Network Connections`：
>>> [NOTE]
> `URLSession` automatically handles server trust evaluation for you, but enables you to customize the process, for example to extend trust to a self-signed certificate embedded in your app, or to bypass certificate expiry. When ATS is enabled, you can no longer loosen trust evaluation requirements that way, but you can still tighten them — for example, to implement certificate pinning. For more information, see `Performing manual server trust authentication`.

证据 B — `Performing manual server trust authentication`（https://developer.apple.com/documentation/Foundation/performing-manual-server-trust-authentication）：
>>> [NOTE]
> `URLSession` enforces `App Transport Security (ATS)`, if it is enabled for the domain you are connecting to. This applies security requirements for the certificates, TLS version, and cipher used by the connection. **You cannot loosen server trust requirements for an ATS-protected domain, but you can tighten them**, using the manual evaluation technique shown in this article. See `NSAppTransportSecurity` in `Information Property List Key Reference` for further details.

口径要点：这是"哪条能放松哪条只能收紧"的**唯一直接一手表述**，且**限定语是 "for an ATS-protected domain"**（即该域未被 Info.plist 例外豁免时才成立）。反过来说：一旦某域被 ATS 例外豁免（例如 `NSAllowsArbitraryLoads = YES`），就可以放松默认信任评估——这句反证由 `NSAllowsArbitraryLoads` 页提供（见 §2.2）。正文必须写成"条件式"，不能写成无条件禁令。

### 1.5 例外需 App Store 审核理由（键级清单）
状态：**已核实**（逐字）

> Adding certain ATS exceptions to your app’s `Information Property List` file requires you to provide justification, and might trigger additional App Store review for your app. Exceptions that require justification are:
> - Arbitrary connection exceptions (`NSAllowsArbitraryLoads`)
> - Media streaming exceptions (`NSAllowsArbitraryLoadsForMedia`)
> - Web content loads (`NSAllowsArbitraryLoadsInWebContent`)
> - Per-domain nonsecure connections (`NSExceptionAllowsInsecureHTTPLoads`)
> - Per-domain minimum TLS version (`NSExceptionMinimumTLSVersion`)

> `NSRequiresNIAPTLSPackageVersion` and `NSExceptionRequiresNIAPTLSPackageVersion` don’t require justification because they strengthen rather than weaken security requirements.

可接受的理由（逐字）：
> Some examples of justifications eligible for consideration are:
> - The app must connect to a server managed by another entity that doesn’t support secure connections.
> - The app must support connecting to devices that cannot be upgraded to use secure connections, and that must be accessed using public host names.
> - The app must display embedded web content from a variety of sources, but can’t use a class supported by the web content exception.
> - The app loads media content that is encrypted and that contains no personalized information.

> When submitting your app to the App Store, provide sufficient information for the App Store to determine why your app cannot make secure connections by default.

>>> [IMPORTANT]
> Always look for a way to avoid using exceptions as a first recourse. If you must use an exception, make it as limited in scope as possible.

口径要点：**需要理由的只有 5 个键**（清单逐字）；两个 NIAP 键明确"不需要理由，因为是加强而非削弱"——这是"收紧方向自由、放松方向需申报"的一手证据，可作为正文的"放松/收紧不对称"论点的支撑。**`NSAllowsLocalNetworking` 不在此清单**（正文别写成"所有例外都要理由"）。

### 1.6 NIAP 合规模式（ATS 唯一明确的"再收紧"开关）
状态：**已核实**（逐字）
URL：…/Security/preventing-insecure-network-connections 与 …/information-property-list/nsrequiresniaptlspackageversion

> ATS provides support to further restrict default TLS client behavior to help meet requirements outlined by the National Information Assurance Partnership (NIAP) in the Functional Package for Transport Layer Security. This compliance mode is opt-in only and provides additional options for regulated environments. To opt-in to this mode, use the `NSRequiresNIAPTLSPackageVersion` key, which can be set to enforce this mode globally, and the `NSExceptionRequiresNIAPTLSPackageVersion` key to configure exceptions to the global policy on a per-domain basis.

`FCP_v2.1` 附加要求（逐字，两页文字一致）：
> The `FCP_v2.1` value adds the following requirements on top of standard ATS:
> - The client offers the same TLS 1.3 ciphersuites as the default ATS policy. For TLS 1.2, the client offers only perfect forward secret (using ECDHE), AES-GCM ciphersuites with a SHA-256 or greater pseudorandom function (PRF).
> - The client no longer offers the signature algorithm `rsa_pkcs15_sha1`, as specified in `FCS_TLSC_EXT.1.4`.
> - The server’s certificate signature  must use a signature algorithm offered in the client hello, as defined in `RFC 8446 Section 4.2.3` and `RFC 5246 Section 7.4.2`.
> - TLS 1.2 connections must negotiate the Extended Master Secret extension, as specified in `RFC 7627`.

取值（逐字，termList）：
> **`none`**: Apply no additional requirements. This value results in the default ATS behavior.
> **`FCP_v2.1`**: Make your default client connections enforce additional requirements (described below) as specified in the `Functional Package for Transport Layer Security (TLS) Version 2.1`.
> **`recommended`**: Use the recommended version of the NIAP functional package for the operating system version your app is running on. Currently, this is equivalent to `FCP_v2.1`.

注：原文 "The server’s certificate signature  must use…" 中间有双空格（页面如此），照抄。

### 1.7 ATS 诊断工具 `nscurl`
状态：**已核实**（逐字）URL：…/Security/identifying-the-source-of-blocked-connections

> The `nscurl` command accepts the `--ats-diagnostics` flag that asks it to check how a particular server responds to combinations of ATS exceptions.
```
$ /usr/bin/nscurl --ats-diagnostics --verbose https://example.com
```
> In addition to running with no exceptions at all, the tool tests the global exception keys in `NSAppTransportSecurity`, as well as a variety of combinations of the exception domain keys `NSExceptionMinimumTLSVersion`, `NSExceptionRequiresForwardSecrecy`, `NSExceptionAllowsInsecureHTTPLoads`, and `NSExceptionRequiresNIAPTLSPackageVersion`.

> If globally allowing arbitrary loads fails, the problem isn’t related to ATS. For example, if the server’s certificate doesn’t match the DNS name of the server, the connection fails default server trust evaluation before ATS has a chance to impose its extended security checks:

口径要点：这段是**"默认信任评估先于 ATS 扩展检查"**的一手证据（"before ATS has a chance to impose its extended security checks"）——即无效证书永远是硬故障，加例外也救不回来。可配合示例错误码 `NSURLErrorDomain Code=-1202`（页面输出样例里逐字出现）。

---

## 2. ATS 配置键目录（Info.plist `NSAppTransportSecurity`）

### 2.1 键的所在位置与页面 URL
状态：**已核实**
所有键的**现行一手页在 `Bundle Resources / Information Property List` 命名空间**，不在 Security 框架下。两种 URL 形态：
- 字典与全局/域容器键：`https://developer.apple.com/documentation/bundleresources/information-property-list/nsapptransportsecurity/<key>`（`NSAllowsArbitraryLoads`、`NSAllowsArbitraryLoadsForMedia`、`NSAllowsArbitraryLoadsInWebContent`、`NSAllowsLocalNetworking`、`NSExceptionDomains`、`NSPinnedDomains`）
- 域级/顶层叶子键：`https://developer.apple.com/documentation/bundleresources/information-property-list/<key>`（`NSExceptionAllowsInsecureHTTPLoads`、`NSExceptionMinimumTLSVersion`、`NSExceptionRequiresForwardSecrecy`、`NSIncludesSubdomains`、`NSRequiresCertificateTransparency`、`NSRequiresNIAPTLSPackageVersion`、`NSExceptionRequiresNIAPTLSPackageVersion`）

容器键自身描述（逐字）：
> **NSAppTransportSecurity** — A description of changes made to the default security for HTTP connections.
DETAILS：`ideTitle = "App Transport Security Settings"`，value 类型 `dictionary`。

> On Apple platforms, a networking feature called App Transport Security (ATS) improves privacy and data integrity for all apps and app extensions. ATS requires that all HTTP connections made with the `URL Loading System`—typically using the `URLSession` class—use HTTPS. It further imposes extended security checks that supplement the default server trust evaluation prescribed by the Transport Layer Security (TLS) protocol. ATS blocks connections that fail to meet minimum security specifications. For additional details, see `Preventing Insecure Network Connections`.

> You can circumvent or augment these protections by adding the `NSAppTransportSecurity` key to your app’s `Information Property List` file and providing an ATS configuration dictionary as the value.

>>> [IMPORTANT]
> Always look for ways to improve server security before adding ATS exceptions. Loosening ATS restrictions reduces the security of your app.

> All keys in the ATS configuration dictionary are optional, with default values that are suitable for most apps. Keys that define global exceptions apply to all network connections made by your app, except connections to domains specified in the `NSExceptionDomains` sub-dictionary. That sub-dictionary allows you to separately manage settings for individual domains.

Versioning 段（逐字，全文）：
> ATS operates by default for apps linked against the iOS 9.0 or macOS 10.11 SDKs or later. When you link your app against an older SDK, ATS is disabled no matter which version of operating system your app runs on.
> If you specify a value for any of the global exceptions besides `NSAllowsArbitraryLoads`, then the ATS behavior depends on the version of the OS on which your app runs:
> **iOS 9.0 or macOS 10.11**: ATS uses the `NSAllowsArbitraryLoads` value that you set, or NO by default, and ignores the other global exceptions.
> **iOS 10.0 or later or macOS 10.12 or later**: ATS ignores the `NSAllowsArbitraryLoads` value that you set and instead obeys the other key or keys.
> This behavior enables you to manage differences between OS versions. You provide a coarse exception (`NSAllowsArbitraryLoads`) for older versions, and a more targeted exception, like `NSAllowsArbitraryLoadsInWebContent`, for when it’s available.

官方给出的**现行字典结构**（逐字代码块，注意其中**没有** `NSThirdPartyException*` 与 `NSExceptionForwardSecrecyCipherSuitesOrder`）：
```
NSAppTransportSecurity : Dictionary {
    NSAllowsArbitraryLoads : Boolean
    NSAllowsArbitraryLoadsForMedia : Boolean
    NSAllowsArbitraryLoadsInWebContent : Boolean
    NSAllowsLocalNetworking : Boolean
    NSRequiresNIAPTLSPackageVersion : String
    NSExceptionDomains : Dictionary {
        <domain-name-string> : Dictionary {
            NSIncludesSubdomains : Boolean
            NSExceptionAllowsInsecureHTTPLoads : Boolean
            NSExceptionMinimumTLSVersion : String
            NSExceptionRequiresForwardSecrecy : Boolean
            NSRequiresCertificateTransparency : Boolean
            NSExceptionRequiresNIAPTLSPackageVersion : String
        }
    }
}
```
（该代码块出自 `Preventing Insecure Network Connections` 页；`NSPinnedDomains` 分支只出现在 §2.9 的键页里，未出现在这个总结构图里。）

### 2.2 NSAllowsArbitraryLoads
状态：**已核实**（逐字）URL：…/nsapptransportsecurity/nsallowsarbitraryloads
ABSTRACT：
> A Boolean value indicating whether App Transport Security restrictions are disabled for all network connections.
ATTRIBUTES（默认值，来自页面 `attributes` 段）：`{"kind":"default","value":"NO"}`；ideTitle `Allow Arbitrary Loads`；类型 boolean。

> Set this key’s value to `YES` to disable App Transport Security (ATS) restrictions for all domains not specified in the `NSExceptionDomains` dictionary. Domains you specify in that dictionary aren’t affected by this key’s value.

>>> [IMPORTANT]
> You must supply a justification during App Store review if you set the key’s value to `YES`, as described in `Provide Justification for Exceptions`. Use this key with caution because it significantly reduces the security of your app. In most cases, it’s better to upgrade your servers to meet the requirements imposed by ATS, or at least to use a narrower exception.

> Disabling ATS means that unsecured HTTP connections are allowed. HTTPS connections are also allowed, and are still subject to default server trust evaluation, as described in `Ensure the Network Server Meets Minimum Requirements`. However, extended security checks—like requiring a minimum Transport Layer Security (TLS) protocol version—are disabled. Without ATS, you’re also free to loosen the default server trust requirements, as described in `Performing manual server trust authentication`.

> In iOS 10 and later and macOS 10.12 and later, the value of the `NSAllowsArbitraryLoads` key is ignored—and the default value of `NO` used instead—if any of the following keys are present in your app’s `Information Property List` file:
（页面此处为**无序列表**，列出的即 §2.3/§2.4/§2.5 三个全局键；本简报抓取到的 JSON 中该列表与渲染文本分离，**具体三项文本未逐字取到，标为部分核实**，但可由 §2.3–§2.5 各自页面的条件句交叉印证。）

口径要点：
- 「Without ATS, you’re also free to **loosen the default server trust requirements**」= §1.4 "只能收紧"的**条件反证**：只有在关掉 ATS（或该域未被 ATS 保护）时才允许放松。正文必须这样成对引用。
- 「HTTPS connections … are **still subject to default server trust evaluation**」= 关掉 ATS 不等于关掉证书校验；无效证书仍然失败。
- 「ignored if any of the following keys are present」= 全局键之间存在**互斥/优先级**，配了细粒度键会让粗粒度键失效（这是"哪条能放松"的机制性事实）。

### 2.3 NSAllowsArbitraryLoadsInWebContent
状态：**已核实**（逐字）URL：…/nsapptransportsecurity/nsallowsarbitraryloadsinwebcontent
ABSTRACT：
> A Boolean value indicating whether all App Transport Security restrictions are disabled for requests made from web views.
ATTRIBUTES：默认值页面 attributes 段本批量未打印（键页仅见 `[{"kind":"default","value":"NO"}]` 形态；此项**未逐字核实**，标部分核实）。

> Set this key’s value to `YES` to exempt your app’s `WKWebView` instances from App Transport Security restrictions without affecting your `URLSession` connections. Domains you specify in the `NSExceptionDomains` dictionary aren’t affected by this key’s value.

> In iOS 10 and later and in macOS 10.12 and later, if you include this key with any value, then App Transport Security ignores the value of the `NSAllowsArbitraryLoads` key, instead using that key’s default value of `NO`. For more information about how the OS version affects ATS behavior, see the `NSAppTransportSecurity` key’s Versioning section.

（该页另有一个 `[IMPORTANT]` 框，JSON 中 `content` 为空数组，正文文本未取到 —— **部分核实**；但 §1.5 的清单已逐字列明本键需要审核理由。）

口径要点：**"只对 WKWebView 内容生效、且不影响 URLSession 连接"是逐字原文**——即 WebView 与 URLSession 的传输安全要求可以**分道配置**，App 侧要区分"网页内容的例外"与"自己上传请求的例外"。

### 2.4 NSAllowsArbitraryLoadsForMedia
状态：**已核实**（逐字）URL：…/nsapptransportsecurity/nsallowsarbitraryloadsformedia
ABSTRACT：
> A Boolean value indicating whether all App Transport Security restrictions are disabled for requests made using the AV Foundation framework.

> Set this key’s value to `YES` to disable App Transport Security restrictions for media loaded using the `AVFoundation` framework, without affecting your `URLSession` connections. Domains you specify in the `NSExceptionDomains` dictionary aren’t affected by this key’s value.

> Employ this key only for loading encrypted media—like files protected by FairPlay or by secure `HTTP Live Streaming`—that don’t contain personalized information.

> In iOS 10 and later and in macOS 10.12 and later, if you include this key with any value, then App Transport Security ignores the value of the `NSAllowsArbitraryLoads` key, instead using that key’s default value of `NO`. For more information about how the OS version affects ATS behavior, see the `NSAppTransportSecurity` key’s Versioning section.

口径要点：官方限定用途是"加密媒体、不含个性化信息"；同样**不影响 URLSession**（与上传链路无关，正文别把它当作上传例外）。

### 2.5 NSAllowsLocalNetworking
状态：**已核实**（逐字，含 iOS 17 变化）URL：…/nsapptransportsecurity/nsallowslocalnetworking
ABSTRACT：
> A Boolean value that indicates whether to allow local resources to load.

> The `NSAllowsLocalNetworking` key controls whether App Transport Security (ATS) allows your app to connect to unqualified domains, `.local` domains, and IP addresses using IPv4 or IPv6.
> In iOS 9 and macOS 10.11, ATS disallows connections to all three domain types. You can add exceptions for unqualified domains and `.local` domains in the `NSExceptionDomains` dictionary, but you can’t add IP addresses. Instead you use `NSAllowsArbitraryLoads` when you want to load directly from an IP address.
> In iOS 10 through iOS 16, iPadOS 13.1 through iPadOS 16, and macOS 10.12 through macOS 13, ATS allows all three of these connections by default, so you no longer need an exception for any of them. However, if you need to maintain compatibility with older versions of the OS, set both of the `NSAllowsArbitraryLoads` and `NSAllowsLocalNetworking` keys to `YES`.
> In iOS 17, iPadOS 17, and macOS 14, ATS no longer allows connections to IP addresses by default. Add individual IP addresses and classless inter-domain routing (CIDR) ranges in the `NSExceptionDomains` dictionary.
> The local networking exception tells newer versions of the OS to ignore the arbitrary loads key, and enable access to unqualified domains, `.local` domains, and IP addresses that they would otherwise restrict. Meanwhile, the arbitrary loads key tells older versions of the OS, which don’t process the local networking exception key, to bypass ATS completely. This allows your app to work on different OS versions while minimizing the use of the wider exception. For more information about how global ATS exceptions interact across OS versions, see the `NSAppTransportSecurity` key’s Versioning section.

（注：`.local` 在 JSON 里是 codeVoice 引用，页面正文渲染时该处显示为域名样式；引文中以 `.local` 还原，正文引用时以页面渲染为准。**"The  key controls…"原文首词为键名引用**，已按 `NSAllowsLocalNetworking` 还原 —— 若需严格逐字，正文改引页面渲染版。）

量化基线：三个版本窗口 —— iOS 9/macOS 10.11 全禁三类本地目标；iOS 10–16 / iPadOS 13.1–16 / macOS 10.12–13 默认全允许；**iOS 17/iPadOS 17/macOS 14 起 IP 地址默认不再允许**，须写进 `NSExceptionDomains`（支持单 IP 与 CIDR）。

### 2.6 NSExceptionDomains（域级例外的容器）
状态：**已核实**（逐字）URL：…/nsapptransportsecurity/nsexceptiondomains
ABSTRACT：
> Custom App Transport Security (ATS) configurations for named domains.
DETAILS：`ideTitle = Exception Domains`，类型 dictionary。

> The value for this key is a dictionary with keys that name specific domains, IP addresses, or IP address ranges for which you want to set exceptions. The value for each domain key is another dictionary that indicates the exceptions for that domain.
```
NSExceptionDomains : Dictionary {
    <domain-name-or-ip-address-string> : Dictionary {
        NSIncludesSubdomains : Boolean
        NSExceptionAllowsInsecureHTTPLoads : Boolean
        NSExceptionMinimumTLSVersion : String
        NSExceptionRequiresForwardSecrecy : Boolean
    }
}
```
> Follow these rules when setting a domain name string:
> - **Use lowercase** — Use `example.com`, not `EXAMPLE.COM`.
> - **Use a DNS domain name, IP address, or range of IP addresses** — In iOS 17, iPadOS 17, and macOS 14, you can use an IPv4 address, for example `192.168.42.63`, or an IPv6 address, for example `2001:db8:12::34`. You can also use a classless inter-domain routing (CIDR) range, for example `2001:db8:12::/48`. For information about how ATS handles IP addresses, see `NSAllowsLocalNetworking`.
>>> [NOTE]
> If you exclude a DNS domain name and your app contacts a host by IP address, the ATS exclusion for the domain name doesn’t apply to the connection even if a DNS query for the domain name would resolve to the IP address. If you exclude an IP address and your app contacts a host by DNS name that resolves to that IP address, the ATS exclusion for the IP address doesn’t apply to the connection.
> - **Don’t include a port number** — Use `example.com`, not `example.com:443`.
> - **Don’t include a trailing dot, unless you only want to match a domain string with a trailing dot** — For example, `example.com.` (with a trailing dot) matches “example.com.” but not “example.com”. Similarly, `example.com` matches “example.com” but not “example.com.”.
> - **Don’t use wildcard domains** — Don’t use `*.example.com`. Instead, use `example.com` and set `NSIncludesSubdomains` to `YES`.

> The values for the keys in each individual domain’s dictionary control how ATS treats connections made to that domain.
>>> [NOTE]
> If you specify an exception domain dictionary, ATS ignores any global configuration keys, like `NSAllowsArbitraryLoads`, for that domain. This is true even if you leave the domain-specific dictionary empty and rely entirely on its keys’ default values.

口径要点：**"例外域优先于全局键、即使字典为空"**是正文最容易写错的一条；以及 DNS 名与 IP 例外**互不通用**（逐字 NOTE）。

### 2.7 NSIncludesSubdomains
状态：**已核实**（逐字）URL：…/information-property-list/nsincludessubdomains
ABSTRACT：
> A Boolean value that indicates whether to extend the configuration to subdomains of the given domain.
ATTRIBUTES：`{"kind":"default","value":"NO"}`。

> You can include this key in any of the domain-specific dictionaries that you add to the `NSExceptionDomains` and `NSPinnedDomains` dictionaries. Adding the `NSIncludesSubdomains` key affects the applicability of the other configuration in the same domain-specific dictionary. The key is optional, with a default value of `NO`.

> Set the value for this key to `YES` to apply the configuration for the given domain to all subdomains of the domain that have one additional path component. For example, if you set this value to `YES` and the domain name string is `example.com`, then the configuration applies to `example.com`, as well as `math.example.com` and `history.example.com`. However, it doesn’t apply to the subdomains `advanced.math.example.com` or `ancient.history.example.com` because those subdomains have two additional path components. If the value is `NO` the configuration applies only to `example.com`.

> If you create an `NSExceptionDomains` dictionary for an IP address or a range of addresses, the `NSIncludesSubdomains` key has no effect for that exception.

量化基线：**只展开一层子域**（one additional path component），两层不生效 —— 这是常见误配点，适合进正文的"配置项语义"表。

### 2.8 NSExceptionAllowsInsecureHTTPLoads
状态：**已核实**（逐字）URL：…/information-property-list/nsexceptionallowsinsecurehttploads
ABSTRACT：
> A Boolean value indicating whether to allow insecure HTTP loads.
ATTRIBUTES：`{"kind":"default","value":"NO"}`。

> Set the value for this key to `YES` to allow insecure HTTP loads for the given domain, or to be able to loosen the server trust evaluation requirements for HTTPS connections to the domain, as described in `Performing manual server trust authentication`.

> Using this key doesn’t by itself change default server trust evaluation requirements for HTTPS connections, described in `Ensure the Network Server Meets Minimum Requirements`. Using only this key also doesn’t change the TLS or forward secrecy requirements imposed by ATS. As a result, you might need to combine this key with the `NSExceptionMinimumTLSVersion` or `NSExceptionRequiresForwardSecrecy` key in certain cases.

> This key is optional. The default value is `NO`.
>>> [IMPORTANT]
> You must supply a justification during App Store review if you set the key’s value to YES, as described in `Provide Justification for Exceptions`.

口径要点（正文最关键的一条机制描述）：**单个键只关自己那一维**——`NSExceptionAllowsInsecureHTTPLoads` 放开 http 与手动信任评估，但**不改 TLS 版本、不改前向保密**；要放松那两条必须另配对应键。这就是"配置项之间不可互相替代"的一手证据。

### 2.9 NSExceptionMinimumTLSVersion
状态：**已核实**（逐字）URL：…/information-property-list/nsexceptionminimumtlsversion
ABSTRACT：
> The minimum Transport Layer Security (TLS) version for network connections.
ATTRIBUTES：`{"kind":"default","value":"TLSv1.2"}`（类型 string）。

> This key is optional. The value is a string, with a default value of `TLSv1.2`.
>>> [IMPORTANT]
> You must supply a justification during App Store review if you use this key to set a protocol version lower than 1.2, as described in `Provide Justification for Exceptions`.

未核实项：页面**未列出可接受字符串的完整取值集合**（如 `TLSv1.0`/`TLSv1.1`/`TLSv1.3` 的字面量在本 JSON 正文里没有逐字出现）。写正文时不得把取值表当作一手事实；可在"待补"里注明需从 `nscurl` 输出或样例工程确认。

### 2.10 NSExceptionRequiresForwardSecrecy
状态：**已核实**（逐字）URL：…/information-property-list/nsexceptionrequiresforwardsecrecy
ABSTRACT：
> A Boolean value indicating whether to override the perfect forward secrecy requirement.
ATTRIBUTES：**`{"kind":"default","value":"YES"}`**（同事清单里猜的"默认 true"得到一手确认）。

> Set the value for this key to `NO` to override the requirement that a server support perfect forward secrecy (PFS) for the given domain. Disabling this requirement also removes the key length check described in `Ensure the Network Server Meets Minimum Requirements`. However, it doesn’t impact the TLS version requirement. To control that, use `NSExceptionMinimumTLSVersion`.

> This key is optional. The default value is YES, which limits the accepted ciphers to those that support PFS through Elliptic Curve Diffie-Hellman Ephemeral (ECDHE) key exchange.

口径要点：**关掉前向保密要求会连带取消密钥长度检查**（"also removes the key length check"）—— 一次放松触发两项，正文可用来说明键之间的耦合。

### 2.11 NSRequiresCertificateTransparency —— 注意：现行文档已标 obsolete
状态：**已核实**（逐字，含 WARNING 框）URL：…/information-property-list/nsrequirescertificatetransparency
ABSTRACT：
> **An obsolete Boolean value** indicating whether to require Certificate Transparency.
ATTRIBUTES：`{"kind":"default","value":"NO"}`。

> Certificate Transparency (CT) is a protocol that ATS can use to identify mistakenly or maliciously issued X.509 certificates. Earlier versions of the system used this key to enable or disable CT for a given domain. Specifically, CT requires a domain’s server certificates to have support from signed CT timestamps from at least two CT logs trusted by Apple. For more information about Certificate Transparency, see `RFC 6962`.

> The system now requires this behavior in all cases, making the key irrelevant.
>>> [WARNING]
> Don’t use this key. The current system enforces this behavior, and new certificates can’t meet the requirement on older systems.

口径要点（**事实核查纠正项**）：报告主线里若把 `NSRequiresCertificateTransparency` 当"可开可关的配置项"，与现行一手文档**不符**：CT 已是系统强制项，键本身无效果且官方明令不要用；量化基线是 **至少两个 Apple 信任的 CT 日志的签名 CT 时间戳**（逐字）。

### 2.12 NSPinnedDomains（ATS 声明式证书固定；清单外的高价值发现）
状态：**已核实**（逐字）URL：…/nsapptransportsecurity/nspinneddomains
ABSTRACT：
> A collection of certificates that App Transport Security expects when connecting to named domains.
DETAILS：类型 dictionary。

> The value for this optional key is a dictionary with keys that specify the domain names for which you want to set the expected certificates. The value for each domain name key is another dictionary that configures the expected certificates for that domain.
```
NSPinnedDomains : Dictionary {
    <domain-name-string> : Dictionary {
        NSIncludesSubdomains : Boolean
        NSPinnedCAIdentities : Array
        NSPinnedLeafIdentities : Array
    }
}
```
> For any domain that you specify, you must include one or more expected Certificate Authority (CA) or sub-CA certificates as the value for the `NSPinnedCAIdentities` key, one or more expected leaf certificates as the value for the `NSPinnedLeafIdentities` key, or both. If you specify both, App Transport Security (ATS) requires a match in each category.

> To specify a domain name string, follow the rules for domain names given in `NSExceptionDomains`. You can also extend the pinning to cover subdomains by setting the value for the `NSIncludesSubdomains` key to `YES`.

> Pinning a certificate for a given domain has no impact on other security requirements or configuration. For example, pinning a CA certificate doesn’t change the way the system evaluates that certificate’s suitability as an anchor certificate. For information about securing network connections, see `Preventing Insecure Network Connections`.

口径要点：这是"ATS 内的收紧方向"（固定=期望集合，且明确"不影响其他要求"）；`NSPinnedCAIdentities`/`NSPinnedLeafIdentities` 有各自的 JSON 页（404），其**逐字描述未核实**，语义仅由父页文字支撑。

### 2.13 NSRequiresNIAPTLSPackageVersion / NSExceptionRequiresNIAPTLSPackageVersion
状态：**已核实**（见 §1.6 逐字引文；两页 URL 与抽象）
ABSTRACT（顶层键）：
> A string that indicates the version to use for the NIAP Functional Package for TLS.
ABSTRACT（域级键）：
> A string that indicates the version to use for the NIAP Functional Package for TLS, applied to an exception domain.
> The `NSRequiresNIAPTLSPackageVersion` key applies additional ATS requirements globally. Use this key to apply a different behavior to a specific domain entry in `NSExceptionDomains`.

### 2.14 NSThirdPartyException* 一族 / NSExceptionForwardSecrecyCipherSuitesOrder
状态：**未核实（现行 developer 文档无此二族页面）**
- 尝试并 404 的 URL 见 §0 末尾清单。
- 反向证据：`Preventing Insecure Network Connections` 页给出的**完整字典结构代码块**（§2.1）中不含任何 `NSThirdPartyException*`，也不含 `NSExceptionForwardSecrecyCipherSuitesOrder`；`NSExceptionDomains` 页的结构代码块（§2.6）同样不含。
- 这族键存在于**归档页** `https://developer.apple.com/library/archive/documentation/General/Reference/InfoPlistKeyReference/Articles/CocoaKeys.html`（WebSearch 摘要确认该页含 ATS 键全表与 "If ATS is enabled for a domain, you cannot loosen the system's HTTPS server trust evaluation requirements." 一句）。该归档页正文的逐字摘录见 §12「待补」，**尚未取到，不要写入正文**。

口径要点：正文若要提 `NSThirdPartyException*`，必须写明"仅见于已归档的 Cocoa Keys，现行 ATS 结构图不再列出"，并注明为历史兼容项；否则按缺口处理。


---

## 3. 服务端信任评估：**TN2287 是误号，正确的是一手 TN2232**

### 3.0 重大核查纠正（写给报告作者）
状态：**已核实**（两条一手证据）

1. 同事清单里的「Technical Note **TN2287** HTTPS Server Trust Evaluation」，**编号错了**。Apple 归档站上 TN2287 的真实标题是：
 > Technical Note TN2287: **iOS 5 and TLS 1.2 Interoperability Issues**
 URL：https://developer.apple.com/library/archive/technotes/tn2287/_index.html
 页内 meta：`date = 2011-10-14`，`copyright = Copyright 2018 Apple Inc. All Rights Reserved.`，页脚：`Copyright © 2011 Apple Inc. All Rights Reserved. … Updated: 2011-10-14`，修订历史首行 `2011-10-14`。
2. 「HTTPS Server Trust Evaluation」的**真实编号是 TN2232**：
 > Technical Note TN2232: HTTPS Server Trust Evaluation
 URL：https://developer.apple.com/library/archive/technotes/tn2232/_index.html
 页内 meta：`date = 2014-07-29`，`copyright = Copyright 2018 Apple Inc. All Rights Reserved.`，页脚：`Copyright © 2014 Apple Inc. All Rights Reserved. Terms of Use | Privacy Policy | Updated: 2014-07-29`。
3. **TN2232 全文没有出现 "App Transport Security" / "ATS" 字样**（本简报对该页做了全文 grep，仅命中 "import" 等无关词）。因此不能用 TN2232 来证明 ATS 的任何要求；ATS 侧的一手证据只在 §1/§2 的 developer 文档页。
4. TN2287 与本报告主线的关联点（TLS 1.2 兼容性，正文若要引用需另取该页原文）本次**未逐字摘录**，列为待补。

口径要点：写正文时，引用"默认评估/证书固定/SecTrust 流程"必须标 **TN2232**，并把「TN2287」这个错误编号当作已发现的事实核查问题列出来。

### 3.1 TLS 的两条安全保证与"信任评估 = 客户端认证服务器"
状态：**已核实**（逐字，TN2232 §TLS Security Guarantees）

> HTTPS inherits its security guarantees from TLS. By default these are:
> - on-the-wire privacy — This guarantees that the data is secure from a passive attacker (someone who can see but can't modify the packets exchanged between the client and server).
> - client-authenticates-server authentication — This protects the client from an active attacker (someone who can both see and modify the packets exchanged between the client and the server). Most importantly, it protects the client from various forms of man-in-the-middle attack, including impostor servers (servers who pretend to be the real server and thus acquire confidential information from the client).

> TLS server trust evaluation is the part of TLS that implements client-authenticates-server authentication. If you disable it, you lose this second, extremely important security guarantee.

> Note: TLS also supports server-authenticates-client authentication. This authentication is optional at both ends: the server must specifically request a certificate from the client, the client may choose to apply a client identity (and thus supply its client certificate to the server, if the server requested it), and the server may choose to allow or deny connections based on whether the client applied a client identity and, if it did, the particulars of the identity's certificate. This is an important technique but it is outside the scope of this document and thus not discussed further here.

口径要点：正文讲"上传通道的机密性与服务器真实性"时，这两条是官方二分口径（passive/active attacker 分别对应 on-the-wire privacy 与 client-authenticates-server）。客户端证书（双向认证）在 TN2232 里**明确声明不讨论** —— 若报告涉及 mTLS，不能用 TN2232 作依据（developer 侧的 mTLS 证据见 §5 `sec_protocol_options_set_peer_authentication_required` 与 §6 `URLCredential(forIdentity:)`）。

两个 WARNING 框（逐字，TN2232 Introduction）：
> Warning: Disabling server trust evaluation puts the user's security at risk. Do not do it in any code you intend to ship to end users. If server trust evaluation is failing, you must understand why it's failing and decide on the best way to resolve that failure.

> Warning: +[NSURLRequest setAllowsAnyHTTPSCertificate:forHost:] is not a public API and is thus not supported by Apple; moreover, use of non-public APIs is not allowed in App Store apps.

### 3.2 "默认评估"的两步结构与失败原因（TN2232 §Trust Evaluation Fundamentals / §Common Failures）
状态：**已核实**（逐字）。注意：同事清单里的"默认评估四条"其实是 §1.3 developer 页的列表；TN2232 的口径是"两步 + 五类失败"。

> TLS server trust evaluation consists of two basic steps:
> - basic X.509 certificate trust evaluation
> - TLS-specific additions

> An X.509 certificate has five critical attributes:
> - information about the subject
> - information about the issuer
> - information about the certificate itself (for example, the valid date range, which is the range of dates for which the certificate is valid)
> - a public key
> - a digital signature

> If the digital signature is valid then the certificate is a guarantee from the issuer that the subject holds the private key associated with the public key in the certificate.

> X.509 certificate trust evaluation is a recursive two-step process:
> - check the validity of the certificate itself — This involves various things, but the two most important are a) verifying the digital signature, and b) checking that the verify date (typically the current date) is within the certificate's valid date range.
> - check the validity of the issuer — This involves finding the issuer's certificate and (recursively) checking its validity.

> Clearly this recursive process must terminate eventually. Trust evaluation can succeed in only one case: if it hits a **trusted anchor**. A trusted anchor is a certificate that the system trusts implicitly, typically because it's the root certificate of a well-known certificate authority that has been baked in to the system.

> On the other hand, trust evaluation can fail for a variety of reasons:
> - if it hits an invalid certificate
> - if it can't find the certificate for an issuer
> - if it hits a self-signed certificate that is not a trusted anchor

> If X.509 trust evaluation is successful, the system then applies additional TLS-specific checks. In practice this involves checking that the DNS name that you are attempting to connect to matches the DNS name in the certificate. There are, however, a few wrinkles:
> - Typically you would expect to find this DNS name in the subject's Common Name field, but it can also be in a Subject Alternative Name extension. If a name is present in the Subject Alternative Name extension, it takes priority over the Common Name.
> - The Subject Alternative Name extension may also contain IP addresses, which the client consults if it connected via an IP address rather than a DNS name.
> - The DNS name in the certificate might be a wildcard name, for example, "*.apple.com".
> - The Extended Key Usage extension is expected to include the Server Authentication value.

> If you're interested in the details of these TLS-specific checks, see RFC 2818.

五类常见失败（逐字，§Common Failures）：
> - **missing issuer certificate** — For any given certificate (except the trusted anchor), the system must be able to locate the certificate of the issuer.
> - **date problems** — For any given certificate, the verify date must be within the certificate's valid date range.
> - **self-signed certificate** — For any given certificate, if the certificate is self-signed, it will cause evaluation to fail (unless it's a trusted anchor).
> - **no trusted anchor** — The system must be able to follow the path of issuer certificates leading to a trusted anchor.
> - **DNS name mismatch** — The DNS name that you're trying to connect to must match the name in the server certificate, as described in the previous section.

> Important: The first three criteria apply to all certificates in the path of certificates leading from the server certificate to the trusted anchor. For example, if server trust evaluation fails due to a date problem, you must check the valid date range of the server certificate, any intermediate certificates leading to the trusted anchor, and the trusted anchor itself.

量化基线：典型故障错误码 `NSURLErrorDomain Code=-1202` = `NSURLErrorServerCertificateUntrusted`（逐字：「In this case error -1202 in the NSURLErrorDomain domain is NSURLErrorServerCertificateUntrusted, which means that server trust evaluation has failed.」）。

口径要点：SAN 优先于 Common Name、通配符名合法、EKU 需含 Server Authentication —— 这三条是"域名匹配"的细节口径；developer 页 §1.3 只写了"Has a name that matches the server's DNS name"，两者要合并引用。

### 3.3 SecTrust 流程与 `SecTrustSetAnchorCertificates` 语义
状态：**已核实**（逐字）

> iOS and OS X support trust evaluation by way of trust objects (of type SecTrustRef). It's possible to create trust objects from scratch but, in the case of TLS server trust evaluation, it's typically the case that the API you're using already does some default server trust evaluation and you want to customize that.

判定代码口径（逐字 Important 框）：
> Important: This code allows the connection if the trust result is either `kSecTrustResultProceed` or `kSecTrustResultUnspecified`. In most cases the result is actually the latter, indicating that the user hasn't specified a trust preference, so your app should do the default thing (that is, connect). The distinction between these two values is irrelevant on iOS, but on OS X the user can use Keychain Access to create trust settings, whereupon you might also see a trust result of `kSecTrustResultProceed`.

`SecTrustSetAnchorCertificates` 的**排他语义**（逐字，§Custom Certificate Authority）：
> - once you have the trust object, create a certificate object from the certificate data (`SecCertificateCreateWithData`) and then set that certificate as a trusted anchor for the trust object (`SecTrustSetAnchorCertificates`)
> - **SecTrustSetAnchorCertificates sets a flag that prevents the trust object from trusting any other anchors; if you also want to trust the default system anchors, call SecTrustSetAnchorCertificatesOnly to clear that flag**
> - evaluate the trust object as per usual

另一处（§Trusting One Specific Certificate / 早期正文）用法：
> You can then customize the server trust evaluation as you see fit. For example, if you're talking to a server whose certificate was issued by a certificate authority that's not trusted by the system, you can call `SecTrustSetAnchorCertificates` to set the anchors for the trust object to be that certificate authority's root certificate.

> Note: Adding arbitrary intermediate certificates to a trust object is always safe because their validity is cryptographically assured.

口径要点（正文易错点）：**`SecTrustSetAnchorCertificates` 默认是"替换"而非"追加"**——不额外调用 `SecTrustSetAnchorCertificatesOnly` 就会拒掉全部系统根。这句话是"上传时自定义信任 = 收紧但可能误伤"的关键机制。另可注意 `kSecTrustResultUnspecified` 在 iOS 上与 `Proceed` 无区别（用户信任偏好仅 macOS 有）。

### 3.4 证书固定章节（TN2232 §Enforcing Stricter Server Trust Evaluation）
状态：**已核实**（逐字，整节）

> Customizing server trust evaluation is not just about working around problems; you can also use it to make security tighter. If you're working on a high-security program, you might want to go beyond the default server trust evaluation and add some more checks of your own.

> For example, you might want to not only check that the server's certificate was issued by a trusted certificate authority, but that it was issued by a specific certificate authority (a technique known as **certificate authority pinning**). It's easy to accomplish this using a trust object. The procedure is as follows:
> - include a copy of the certificate authority's root certificate in your program
> - once you have the trust object, create a certificate object from the certificate data (`SecCertificateCreateWithData`) and then set that certificate as **the only trusted anchor** for the trust object (`SecTrustSetAnchorCertificates`)
> - evaluate the trust object; if the evaluation succeeds, you know that the server's certificate is valid and was issued by the certificate authority that you specified

> Certificate authority pinning is just one example of how you can enforce stricter server trust evaluation. There are many other checks that you might consider. For example:
> - You can implement **certificate pinning** by extracting the public key from the server's certificate using `SecTrustCopyPublicKey`.
> - You could check for the presence of certain attribute values or extensions in the certificate; on OS X the `SecCertificateCopyValues` routine can be very helpful when implementing such checks.
> - `SecTrustCopyResult` lets you determine whether extended validation was done as part of the trust evaluation.
> - `SecPolicyCreateRevocation` lets you create a security policy that specifically checks for certificate revocation (for example, via OCSP or a CRL).

术语定义（逐字，附录 Glossary）：
> - **certificate authority pinning** — A stricter form of trust evaluation that requires that the server's certificate be issued by a specific certificate authority (not just any trusted certificate authority).
> - **certificate pinning** — A stricter form of trust evaluation that requires that the server use a specific certificate or a certificate containing a specific public key.

> Note: On OS X the trust exception API is only available on OS X 10.9 and later. You can do equivalent things on earlier systems (use SecTrustGetResult to get detailed trust results and record information about the failures you're seeing), but it's a lot more work.

口径要点：TN2232 把固定归入"**stricter**（更严）"一侧，与 §1.4「ATS 下只能收紧」完全一致；且给出三种可固定的对象（CA 根 / 公钥 / 证书属性）。**注意本页没提"公开 Key Pinning 头/EXPECT-CT"之类**，不要把外部概念挂到 Apple 一手页上。

### 3.5 TN2232 修订历史（逐字，页尾表格）
状态：**已核实**

| Date | Notes |
|---|---|
| 2014-07-29 | Corrected the guidance in the "HTTP Live Streaming" section (r. 16538960). Fixed the column ordering in the table in Appendix A (r. 16057325). |
| 2014-03-13 | Updated for Security API changes in iOS 7 and OS X 10.9. Added the NSURLSession section. Added three news hints and tips. Updated to reference CustomHTTPProtocol sample code and TN2326. Expanded the "Enforcing Stricter Server Trust Evaluation" section. Other minor editorial changes. |
| 2013-02-07 | Clarified the nature of +setAllowsAnyHTTPSCertificate:forHost:. |
| 2012-10-10 | New document that describes how you can resolve HTTPS server trust evaluation, for example, to work with a server with a missing intermediate certificate. |

页脚：`Copyright © 2014 Apple Inc. All Rights Reserved. … Updated: 2014-07-29`
口径要点：**该 TN 最后修订于 2014-07-29**，正文里所有"iOS 7 / OS X 10.9"时代的 API 名（`SecTrustEvaluate`、`SecTrustCopyPublicKey`）都**没有**跟进 iOS 12+ 的 `SecTrustEvaluateWithError`、iOS 14+ 的 `sec_protocol_options`、或 ATS 时代的变化。引用它时必须写明"文档年代 2014"，不能当作当前推荐做法。

### 3.6 现行 developer 侧对手动信任评估的最新口径（Foundation 文章）
状态：**已核实**（逐字）URL：https://developer.apple.com/documentation/Foundation/performing-manual-server-trust-authentication （© 2026）

Abstract：
> Evaluate the server's security credentials in your app.

> When you use a secure connection (such as `https`) with a URL request, your `URLSessionDelegate` receives an authentication challenge with an authentication type of `NSURLAuthenticationMethodServerTrust`. Unlike other challenges where the server is asking your app to authenticate itself, this is an opportunity for you to authenticate the server's credentials.

> In most cases, you should let the URL Loading System's default handling evaluate the server trust. You get this behavior when you either don't have a delegate or don't handle authentication challenges. However, performing your own evaluation may be useful for scenarios like the following:
> - You want to accept server credentials that would otherwise be rejected by the system. For example, your app makes a secure connection to a development server that uses a self-signed certificate, which would ordinarily not match anything in the system's trust store.
> - You want to reject credentials that would otherwise be accepted by the system. For example, you want to "pin" your app to a set of specific keys or certificates under your control, rather than accept any valid credential.

> `Figure 1` illustrates how an app performs manual credential evaluation by providing a delegate method to handle the authentication challenge. This bypasses the default handling. Instead, the delegate directly compares the server certificate or its public key against a copy of the certificate or key (or a hash of either of these) stored in the app bundle itself. If the delegate decides the server credential is valid, it accepts the server trust and allows the connection to continue.

代码口径（逐字，页面两段代码块）：
```
let protectionSpace = challenge.protectionSpace
guard protectionSpace.authenticationMethod ==
    NSURLAuthenticationMethodServerTrust,
    protectionSpace.host.contains("example.com") else {
        completionHandler(.performDefaultHandling, nil)
        return
}
```
```
guard let serverTrust = protectionSpace.serverTrust else {
    completionHandler(.performDefaultHandling, nil)
    return
}
if checkValidity(of: serverTrust) {
    let credential = URLCredential(trust: serverTrust)
    completionHandler(.useCredential, credential)
} else {
    // Show a UI here warning the user the server credentials are
    // invalid, and cancel the load.
    completionHandler(.cancelAuthenticationChallenge, nil)
}
```

> Once the code determines the validity of the server trust, it takes one of two actions:
> - If the server trust's credential is valid, create a new `URLCredential` instance from the server trust. Then call the `completionHandler` with the `URLSession.AuthChallengeDisposition.useCredential` disposition, passing in the newly-created credential. This tells the system to accept the server's credentials.
> - If the challenge's credential is invalid, call the `completionHandler` with the `URLSession.AuthChallengeDisposition.cancelAuthenticationChallenge` disposition. This tells the system to reject the server's credentials.

固定实践指南（逐字，页面末节）：
> If you determine that you need to evaluate server trust manually in some or all cases, plan for what your app will do if you need to change your server credentials. Keep the following guidelines in mind:
> - Compare the server's credentials against a public key, instead of storing a single certificate in your app bundle. This will allow you to reissue a certificate for the same key and update the server, rather than needing to update the app.
> - Compare the issuing certificate authority's (CA's) keys, rather than using the leaf key. This way, you can deploy certificates containing new keys signed by the same CA.
> - Use a set of keys or CAs, so you can rotate server credentials more gracefully.

口径要点：**"先查 `authenticationMethod` 与 `host`，否则一律 `.performDefaultHandling`"**是官方给出的默认处理边界；`URLCredential(trust:)` + `.useCredential` 是接受路径，`.cancelAuthenticationChallenge` 是拒绝路径。三条固定指南（用公钥而不是证书、用 CA 密钥而不是叶子、留一组以便轮换）是正文"上传固定策略工程建议"的直接一手依据。

---

## 4. Network.framework 的 TLS 配置

### 4.0 API 名称核实结论（**必须先读：同事清单里的多个名字在现行文档里不存在**）
状态：**已核实**（存在性核查 = 逐一 curl JSON 端点，200/404）

现行 developer 文档里，Network.framework 侧与 TLS 有关的**真实类型/入口**只有：
- `NWParameters`（Class）— https://developer.apple.com/documentation/Network/nwparameters
- `NWParameters.tls`（Type Property）、`NWParameters.dtls`、`NWParameters.quic(alpn:)`、`NWParameters.quicDatagram(alpn:)`、`init(tls:tcp:)`、`init(dtls:udp:)`、`init(quic:)`
- `NWProtocolTLS`（"A network protocol for connections that use Transport Layer Security."）→ 只有两个子项：`NWProtocolTLS.Options`、`NWProtocolTLS.Metadata`（+ `definition`）
- `NWProtocolTLS.Options`（"A container of options for configuring how TLS is used on a connection."）→ **公开子项只有 `init()` 与 `securityProtocolOptions`**
- iOS 26 新增的 builder 结构体 `Network.TLS`（"The system definition of the Transport Layer Security (TLS) protocol." / "Supports sending and receiving encrypted byte streams."）：`init()`、`init(_:)`、`version(min:max:)`、`applicationProtocols(_:)`、`certificateValidator(_:)`、`cipherSuiteGroups(_:)`、`cipherSuites(_:)`、`earlyDataEnabled(_:)`、`localIdentity(_:)`、`peerAuthentication(_:)`、`ticketsEnabled(_:)`、枚举 `TLS.PeerAuthentication`
- `QUIC`（builder 结构体）与 `QUIC.TLS`（"The set of TLS options available when using QUIC."）：`certificateValidator(_:)`、`cipherSuites(_:)`、`ciphersuiteGroups(_:)`、`localIdentity(_:)`、`peerAuthentication(_:)`

**以下同事列出的名字在 developer 文档里查无此页（全部 404），正文不得当作 Apple 一手 API 名使用**：
`NWProtocolOptions.TLS`（真实名是 `NWProtocolTLS.Options` 与 `Network.TLS`）、`minimumTLSVersion`、`maximumTLSVersion`、`NWSecurityProtocol`、`requireTLS13Use`、`requiredTLS13Version`、`allowedALPNProtocols`、`additionalTrustedRoots`、`certificateChainVerification`、`certificateTransparencyPolicy`、`domainName`、`ocspResponsesBlockingPolicy`、`policies` / `NWPolicy`、`preSharedKeys` / `NWPSKey`、`tlsMinimumSupportedProtocolVersion`、`NWTLSEnabledEncryptionSchemes`、`sessionTicketKeys` / `sessionTicketsEnabled`、`supportsLocalNetwork`、`trustManagerIdentifier`、`NWTLSTrustEvaluation`、`evaluateServerTrust`、`onMount`。
（探测 URL 形如 `https://developer.apple.com/tutorials/data/documentation/network/nwprotocoltls/options/<name>.json`，逐项 404；见 §0/§12。）
唯一可用的**现行等价物**：版本用 `sec_protocol_options_set_min_tls_protocol_version` / `TLS.version(min:max:)`；ALPN 用 `applicationProtocols(_:)` / `sec_protocol_options_add_tls_application_protocol`；会话票据用 `ticketsEnabled(_:)` / `sec_protocol_options_set_tls_tickets_enabled`；信任自定义用 `certificateValidator(_:)` / `sec_protocol_options_set_verify_block`；本地身份（mTLS）用 `localIdentity(_:)` / `sec_protocol_options_set_local_identity`；PSK 用 `sec_protocol_options_add_pre_shared_key`。

### 4.1 NWParameters 与 TLS 入口
状态：**已核实**（逐字）
`NWParameters`：
> An object that stores the protocols to use for connections, options for sending data, and network path constraints.
DECL：`final class NWParameters`
AVAILABILITY：iOS,iPadOS,Mac Catalyst,macOS,tvOS,visionOS,watchOS（符号页未标 introducedAt；页面 © 2026）

`NWParameters.tls`：
> A set of default parameters for connections and listeners that use TLS and TCP.
DECL：`final class var tls: NWParameters { get }`
AVAILABILITY：**iOS from 12.0 | iPadOS from 12.0 | Mac Catalyst from 12.0 | macOS from 10.14 | tvOS from 12.0 | visionOS from 1.0 | watchOS from 5.0**

`NWParameters.quic(alpn:)`：
> Returns a set of default parameters for connections and listeners that use QUIC, with a set of supported Application-Layer Protocol Negotiation values.
DECL：`final class func quic(alpn: [String]) -> NWParameters`
AVAILABILITY：**iOS from 15.0 | iPadOS from 15.0 | Mac Catalyst from 15.0 | macOS from 12.0 | tvOS from 15.0 | visionOS from 1.0 | watchOS from 8.0**

`NWProtocolTLS.Options.securityProtocolOptions`：
> The handshake security options TLS uses.
DECL：`var securityProtocolOptions: sec_protocol_options_t { get }`
AVAILABILITY：iOS from 12.0 | iPadOS from 12.0 | Mac Catalyst from 12.0 | macOS from 10.14 | tvOS from 12.0 | visionOS from 1.0 | watchOS from 5.0

`NWProtocolTLS.Options.init()`：
> Initializes a default set of TLS connection options.
（同 availability iOS 12.0…）

`NWProtocolTLS.Metadata`：
> A handle you can use to inspect a connection’s TLS state.
子项：`securityProtocolMetadata`（→ `sec_protocol_metadata_t`）

口径要点：Network.framework 的 TLS 细粒度配置**在文档层面是一个转发口**：`NWProtocolTLS.Options` 只暴露 `securityProtocolOptions`，真正可调项在 §5 的 C API；iOS 26 才补了一层 Swift builder（`Network.TLS`）。正文应写成"三层：NWParameters.tls 默认栈 → NWProtocolTLS.Options 桥接 sec_protocol_options_t → iOS 26 TLS builder 方法"。

### 4.2 Network.TLS builder（iOS 26 起；逐项 availability 均为 iOS 26 / macOS 26）
状态：**已核实**（逐字）URL 前缀 https://developer.apple.com/documentation/Network/tls

- 结构体本身：
> The system definition of the Transport Layer Security (TLS) protocol.
> Supports sending and receiving encrypted byte streams.
DECL：`struct TLS`
- `version(min:max:)`：
DECL：`func version(min: tls_protocol_version_t? = nil, max: tls_protocol_version_t? = nil) -> TLS`
（**该页 abstract 与 discussion 文本在 JSON 中为空**，除签名外无可引原话 → 语义仅能由 §5 的 min/max 函数页支撑；此项标**部分核实**。）
- `applicationProtocols(_:)`：
> Set application protocols supported by clients of this protocol.
> Application layer protocol negotiation (ALPN) tokens describe the application protocol in use above TLS.
DECL：`func applicationProtocols(_ protocols: [String]) -> TLS`
- `cipherSuites(_:)`：
> Set TLS ciphersuites to the set of enabled ciphersuites.
DECL：`func cipherSuites(_ suites: [tls_ciphersuite_t]) -> TLS`
- `cipherSuiteGroups(_:)`：
> Set TLS ciphersuite groups to the set of enabled ciphersuites.
DECL：`func cipherSuiteGroups(_ groups: [tls_ciphersuite_group_t]) -> TLS`
- `ticketsEnabled(_:)`：
> Enable TLS session ticket support.
DECL：`func ticketsEnabled(_ enabled: Bool) -> TLS`
- `localIdentity(_:)`：
> Set the local identity TLS uses during the handshake.
DECL：`func localIdentity(_ identity: sec_identity_t) -> TLS`
- `peerAuthentication(_:)`：
> Specify a preference for how to authenticate the peer.
DECL：`func peerAuthentication(_ preference: TLS.PeerAuthentication) -> TLS`
- `init()` / `init(_:)`：
> Create a TLS protocol to use in a protocol stack.
DECL：`init(@ProtocolStackBuilder<TCP> _ builder: () -> TCP) -> TLS`
- `certificateValidator(_:)`（**含 WARNING 框，本页最值钱**）：
> Set a closure to provide custom verification of the peer’s credentials during the TLS handshake.
> This closure may be called multiple times for each connection. It should return `true` if the credentials should be trusted and the handshake should proceed, `false` otherwise.
>>> [WARNING]
> Most apps should not override the default system handling, as doing so can result in insecure network connections and major security vulnerabilities.
DECL：`func certificateValidator(_ handler: @escaping @isolated(any) @Sendable (sec_protocol_metadata_t, sec_trust_t) async -> Bool) -> TLS`
- `earlyDataEnabled(_:)`（含 WARNING 框）：
> Enable early data (0-RTT) for TLS.
>>> [WARNING]
> This may have security implications for application data. In particular, TLS early data is replayable by a network attacker. You must account for this when sending data before the handshake is confirmed. See RFC 8446 for more information. You MUST NOT enable fast open without a specific application profile that defines its use.
DECL：`func earlyDataEnabled(_ enabled: Bool) -> TLS`
- 枚举 `TLS.PeerAuthentication`：
> PeerAuthentication specifies how to authenticate the peer end of the connection.
> For clients, the default is `none`. For servers, the default is `required`.
  - `none` — "Do not authenticate the peer."
  - `optional` — "Requests the peer certificate, but if none is provided, proceed with the connection." ＋ "This is for servers. Clients will ignore this option."
  - `required` — "Always authenticate the peer."

口径要点（**两条可直接进正文的高价值结论**）：
1. **0-RTT 早期数据不可用于"上传"**：官方 WARNING 明确 TLS early data 可被网络攻击者重放（replayable），并要求 "You MUST NOT enable fast open without a specific application profile that defines its use."。这是"上传通道不能用 0-RTT 保证幂等"的一手依据。
2. **自定义证书校验默认被劝阻**：`certificateValidator` 的 WARNING 与 §1.4/§3.6 的"只能收紧"形成三方印证；且该 closure "may be called multiple times for each connection"（与 §5 `sec_protocol_verify_t` 的 NOTE 同口径）。
3. `TLS.PeerAuthentication` 的默认值方向（client=none / server=required）与 §5 C API `sec_protocol_options_set_peer_authentication_required` 页的 "Clients default to true, whereas servers default to false." **表述相反**。两处都逐字引，正文必须标注"Apple 两个页面默认值口径不一致，需以实测/头文件为准"，不得只挑一条写。

### 4.3 TLS 版本枚举 `tls_protocol_version_t`
状态：**已核实**（逐字）
URL：https://developer.apple.com/documentation/Security/tls_protocol_version_t
ABSTRACT：
> The collection of supported TLS and DTLS versions.
DECL：`enum tls_protocol_version_t`；ObjC：`enum tls_protocol_version_t : uint16_t;`
AVAILABILITY：**iOS from 13.0 | macOS from 10.15 | watchOS from 6.0 | visionOS from 1.0**（© 2026）
Cases（逐字 abstract + availability）：
- `TLSv10` — "The TLS 1.0 protocol." AVAIL: **iOS from 13.0 deprecated 15.0** | macOS from 10.15 deprecated 12.0 | watchOS from 6.0 deprecated 8.0
- `TLSv11` — "The TLS 1.1 protocol." AVAIL: **iOS from 13.0 deprecated 15.0** | macOS from 10.15 deprecated 12.0
- `TLSv12` — "The TLS 1.2 protocol." AVAIL: iOS from 13.0（未弃用）
- `TLSv13` — "The TLS 1.3 protocol." AVAIL: iOS from 13.0（未弃用）
- `DTLSv10`、`DTLSv12` 亦存在（页面 Protocol Versions 组）

量化基线：**TLS 1.0/1.1 的枚举 case 自 iOS 15 / macOS 12 / watchOS 8 起被标记 deprecated**；TLS 1.3 自 iOS 13 / macOS 10.15 起可用。这是"报告里说最低版本可设 TLS 1.0/1.1"时必须补的时间锚。

### 4.4 本地网络与身份（Network 侧的安全/隐私三篇）
状态：**部分核实**（仅取到标题与 abstract，正文文本待补）
Network 框架 "Network Security and Privacy" 分组下的三篇一手页面（© 2026）：
- `Security Options` — "Configure security options for TLS handshakes." → https://developer.apple.com/documentation/Network/security-options （**该 collectionGroup 页正文无散文，只列出 §5 的全部 C API 符号**，即 Apple 官方把 sec_protocol_options 视为 Network TLS 配置的正统入口）
- `Privacy Management` → https://developer.apple.com/documentation/Network/privacy-management
- `Creating an Identity for Local Network TLS` → https://developer.apple.com/documentation/Network/creating-an-identity-for-local-network-tls

`NWParameters` 中与本地/路径约束有关的真实属性名（逐字来自页面分组）：`requiredInterfaceType`、`requiredInterface`、`requiredLocalEndpoint`、`prohibitConstrainedPaths`、`prohibitExpensivePaths`、`prohibitedInterfaceTypes`、`prohibitedInterfaces`、`allowUltraConstrainedPaths`、`includePeerToPeer`、`acceptLocalOnly`、`allowLocalEndpointReuse`、`requiresDNSSECValidation`、`preferNoProxies`、`allowFastOpen`、`expiredDNSBehavior`、`multipathServiceType`、`serviceClass`、`attribution`、`setPrivacyContext(_:)`、`NWParameters.PrivacyContext`。
（**注意：没有 `supportsLocalNetwork`**；本地网络权限属 Info.plist/TN3179 口径，见 §7。）

---

## 5. Secure Transport / sec_protocol_options C API

### 5.1 官方函数目录（Security Options 页给出的**完整清单**，即唯一可靠的名字来源）
状态：**已核实**（逐字照抄分组与函数名）
URL：https://developer.apple.com/documentation/Network/security-options （collectionGroup，© 2026；同批符号亦见 https://developer.apple.com/documentation/Security）

- **Configuring TLS Handshake Options**：`sec_protocol_options_t`、`OS_sec_protocol_options`、`sec_protocol_options_set_tls_server_name(_:_:)`、`sec_protocol_options_add_pre_shared_key(_:_:_:)`、`sec_protocol_options_add_tls_application_protocol(_:_:)`、`sec_protocol_options_append_tls_ciphersuite(_:_:)`、`sec_protocol_options_append_tls_ciphersuite_group(_:_:)`、`sec_protocol_options_add_tls_ciphersuite(_:_:)`、`sec_protocol_options_add_tls_ciphersuite_group(_:_:)`、`sec_protocol_options_set_tls_diffie_hellman_parameters(_:_:)`、`sec_protocol_options_are_equal(_:_:)`
- **Configuring TLS Versions**：`sec_protocol_options_set_min_tls_protocol_version(_:_:)`、`sec_protocol_options_set_max_tls_protocol_version(_:_:)`、`sec_protocol_options_get_default_min_tls_protocol_version()`、`sec_protocol_options_get_default_max_tls_protocol_version()`、`sec_protocol_options_get_default_min_dtls_protocol_version()`、`sec_protocol_options_get_default_max_dtls_protocol_version()`、`sec_protocol_options_set_tls_min_version(_:_:)`、`sec_protocol_options_set_tls_max_version(_:_:)`
- **Configuring TLS Behavior**：`sec_protocol_options_set_tls_resumption_enabled(_:_:)`、`sec_protocol_options_set_tls_tickets_enabled(_:_:)`、`sec_protocol_options_set_tls_false_start_enabled(_:_:)`、`sec_protocol_options_set_tls_sct_enabled(_:_:)`、`sec_protocol_options_set_tls_ocsp_enabled(_:_:)`、`sec_protocol_options_set_tls_renegotiation_enabled(_:_:)`、`sec_protocol_options_set_peer_authentication_required(_:_:)`、`sec_protocol_options_set_tls_is_fallback_attempt(_:_:)`、`sec_protocol_options_set_tls_pre_shared_key_identity_hint(_:_:)`
- **Handling TLS Events**：`sec_protocol_options_set_verify_block(_:_:_:)`、`sec_protocol_verify_t`、`sec_protocol_verify_complete_t`、`sec_protocol_options_set_challenge_block(_:_:_:)`、`sec_protocol_challenge_t`、`sec_protocol_challenge_complete_t`、`sec_protocol_options_set_key_update_block(_:_:_:)`、`sec_protocol_key_update_t`、`sec_protocol_key_update_complete_t`、`sec_protocol_options_set_pre_shared_key_selection_block(_:_:_:)`、`sec_protocol_pre_shared_key_selection_t`、`sec_protocol_pre_shared_key_selection_complete_t`
- **Inspecting TLS State**：`sec_protocol_metadata_t`、`OS_sec_protocol_metadata`、`sec_protocol_metadata_get_negotiated_protocol(_:)`、`sec_protocol_metadata_get_server_name(_:)`、`sec_protocol_metadata_get_negotiated_tls_protocol_version(_:)`、`sec_protocol_metadata_get_negotiated_tls_ciphersuite(_:)`、`sec_protocol_metadata_get_negotiated_protocol_version(_:)`、`sec_protocol_metadata_get_negotiated_ciphersuite(_:)`、`sec_protocol_metadata_get_early_data_accepted(_:)`、`sec_protocol_metadata_copy_peer_public_key(_:)`
- **Handling TLS Challenges**：`sec_protocol_metadata_access_distinguished_names(_:_:)`、`sec_protocol_metadata_access_ocsp_response(_:_:)`、`sec_protocol_metadata_access_peer_certificate_chain(_:_:)`、`sec_protocol_metadata_access_supported_signature_algorithms(_:_:)`、`sec_protocol_metadata_access_pre_shared_keys(_:_:)`、`sec_protocol_metadata_create_secret(_:_:_:_:)`、`sec_protocol_metadata_create_secret_with_context(_:_:_:_:_:_:)`、`sec_protocol_metadata_peers_are_equal(_:_:)`、`sec_protocol_metadata_challenge_parameters_are_equal(_:_:)`
- **Handling Certificates**：`sec_certificate_t`、`OS_sec_certificate`、`sec_certificate_create(_:)`、`sec_certificate_copy_ref(_:)`
- **Handling Identities**：`sec_protocol_options_set_local_identity(_:_:)`、`sec_identity_t`、`OS_sec_identity`、`sec_identity_create(_:)`、`sec_identity_create_with_certificates(_:_:)`、`sec_identity_copy_ref(_:)`、`sec_identity_access_certificates(_:_:)`、`sec_identity_copy_certificates_ref(_:)`
- **Handling Trust**：`sec_trust_t`、`OS_sec_trust`、`sec_trust_create(_:)`、`sec_trust_copy_ref(_:)`
- **Managing Security Objects**：`sec_release(_:)`、`sec_retain(_:)`、`sec_object_t`、`OS_sec_object`

**同事清单里这几个名字不存在（404）**：`sec_protocol_options_set_local_certificates`（真实名 `sec_protocol_options_set_local_identity`）、`sec_protocol_options_add_trusted_names`、`sec_protocol_options_set_tls_psk`（真实名 `sec_protocol_options_add_pre_shared_key`）、`sec_protocol_options_set_ocsp_responses_blocking_enabled`、`sec_protocol_options_set_tls_sct_policy`（真实名 `sec_protocol_options_set_tls_sct_enabled`）、`sec_protocol_options_set_force_clear_text`、`sec_protocol_options_set_create_secret_block`（真实名 `sec_protocol_metadata_create_secret`）、`sec_protocol_metadata_copy_negotiated_*`（真实前缀是 `sec_protocol_metadata_get_negotiated_*`）。
另注：Security 根页 "Functions" 组只列了三个 Swift 名：`SecIdentityCreate(_:_:_:)`、`sec_protocol_metadata_copy_negotiated_protocol(_:)`、`sec_protocol_metadata_copy_server_name(_:)` —— 与 Security Options 页的 `get_negotiated_protocol` / `get_server_name` **同时存在**（Swift 导入名差异），正文引用时以具体函数页 URL 为准。

### 5.2 各函数页正文逐字（含所有出现的注释框）
状态：**已核实**（逐字；页脚 copyright 全部为 `Copyright © 2026 Apple Inc. All rights reserved.`）
共通说明：这批函数页**正文极短**（多数只有一句），"警告/重要事项"框**并非每条都有**；已把实际存在的框逐字列出，未列出的即页面没有框（不要虚构）。

- `sec_protocol_options_t`（Type Alias；AVAIL iOS from 2.0 … macOS from 10.0）
> A `sec_protocol_options_t` instance is a container of options for security protocol instances, such as TLS. Protocol options are used to configure security protocols in the network stack. For example, clients may set the maximum and minimum allowed TLS versions through protocol options.
DECL(swift)：`typealias sec_protocol_options_t = any OS_sec_protocol_options`；(occ)：`typedef NSObject<OS_sec_protocol_options> * sec_protocol_options_t;`

- `sec_protocol_options_set_min_tls_protocol_version(_:_:)`（AVAIL iOS 13.0 / macOS 10.15）
> Set the minimum support TLS version.
DECL：`func sec_protocol_options_set_min_tls_protocol_version(_ options: sec_protocol_options_t, _ version: tls_protocol_version_t)`
（"support" 为页面原文拼写，无 s。）

- `sec_protocol_options_set_tls_min_version(_:_:)` — **已弃用**
> Set the minimum support TLS version.
DECL：`func sec_protocol_options_set_tls_min_version(_ options: sec_protocol_options_t, _ version: SSLProtocol)`
AVAILABILITY：**iOS from 12.0 deprecated 13.0 | macOS from 10.14 deprecated 10.15 | watchOS from 5.0 deprecated 6.0**（同 `set_tls_max_version`）
口径要点：现行推荐 = `…_min_tls_protocol_version` + `tls_protocol_version_t`；旧 `SSLProtocol` 版本自 iOS 13 弃用。**这是 Secure Transport 与 Network 的分界**（`SSLProtocol` 属 Secure Transport legacy）。

- `sec_protocol_options_get_default_min_tls_protocol_version()`（AVAIL iOS 13.0 / macOS 10.15）
> The default minimum TLS version. ／ Get the system default minimum TLS protocol version.
DECL：`func sec_protocol_options_get_default_min_tls_protocol_version() -> tls_protocol_version_t`
- `sec_protocol_options_get_default_max_tls_protocol_version()`
> The default maximum TLS version. ／ Get the system default maximum TLS protocol version.
口径要点：**这两个函数是"默认下限/上限可由程序读取"的一手证据**，但页面**没有写出默认值本身**（未核实；不要臆写 TLS1.2/TLS1.3）。DTLS 亦有对应四个函数。

- `sec_protocol_options_set_peer_authentication_required(_:_:)`（AVAIL iOS 12.0 / macOS 10.14）
> Enable or disable peer authentication. **Clients default to true, whereas servers default to false.**
DECL：`func sec_protocol_options_set_peer_authentication_required(_ options: sec_protocol_options_t, _ peer_authentication_required: Bool)`
（与 §4.2 Swift `TLS.PeerAuthentication` 的默认值表述相反 —— 已在该节标注。）

- `sec_protocol_options_set_tls_server_name(_:_:)`（AVAIL iOS 12.0 / macOS 10.14）
> Set the server name to be used when verifying the peer’s certificate. **This will override the server name obtained from the endpoint.**
口径要点：SNI/校验名可被程序改写 —— 正文若谈"域名不匹配的手动绕过"，此处 + §3.2「SecPolicyCreateSSL 覆写 DNS name（TN2232 第 377 行原话）」构成两条一手依据；同时说明"覆写 server name 会同时改变证书校验目标"，属收紧/放松双向操作。

- `sec_protocol_options_set_verify_block(_:_:_:)`（AVAIL iOS 12.0 / macOS 10.14）
> Set the verify block. ／ 参数注释逐字：`A `sec_protocol_verify_t` block.` ／ `A `dispatch_queue_t` on which the verify block should be called.`
DECL(swift)：`func sec_protocol_options_set_verify_block(_ options: sec_protocol_options_t, _ verify_block: @escaping sec_protocol_verify_t, _ verify_block_queue: dispatch_queue_t)`
DECL(occ)：`void sec_protocol_options_set_verify_block(sec_protocol_options_t options, sec_protocol_verify_t verify_block, dispatch_queue_t verify_block_queue);`

- `sec_protocol_verify_t`（Type Alias；AVAIL iOS from 2.0 … macOS from 10.0）
> Block to be invoked when the protocol instance must verify the peer.
代码块内 NOTE（逐字，这是页面唯一的"注"形式，非 aside 框）：
```
 NOTE: this may called one or more times for a given connection.
```
实际逐字为：`NOTE: this may be called one or more times for a given connection.`
DECL：`typealias sec_protocol_verify_t = (sec_protocol_metadata_t, sec_trust_t, @escaping sec_protocol_verify_complete_t) -> Void`

- `sec_protocol_options_set_local_identity(_:_:)`（AVAIL iOS 12.0）
> Set the local identity to be used for this protocol instance.
（= mTLS 客户端证书的唯一入口；无讨论正文。）

- `sec_protocol_options_set_tls_resumption_enabled(_:_:)`（AVAIL iOS 12.0）
> Enable or disable TLS session resumption.
- `sec_protocol_options_set_tls_tickets_enabled(_:_:)`（AVAIL iOS 12.0）
> Enable or disable TLS session ticket support.
- `sec_protocol_options_set_tls_false_start_enabled(_:_:)`（AVAIL iOS 12.0）
> Enable or disable TLS False Start.
- `sec_protocol_options_set_tls_sct_enabled(_:_:)`（AVAIL iOS 12.0）
> Enable or disable SCT (signed certificate timestamp) support.
- `sec_protocol_options_set_tls_ocsp_enabled(_:_:)`（AVAIL iOS 12.0）
> Enable or disable OCSP support.
- `sec_protocol_options_set_tls_renegotiation_enabled(_:_:)`（AVAIL iOS 12.0）
> Enable or disable TLS (1.2 and prior) session renegotiation. **This defaults to `true`.**
- `sec_protocol_options_set_tls_is_fallback_attempt(_:_:)`（AVAIL iOS 12.0）
> Signal if this is a TLS fallback attempt.
代码块（逐字，含 MUST NOT）：
```
 A fallback attempt is one following a previously failed TLS connection
 due to version or parameter incompatibility, e.g., when speaking to a server
 that does not support a client-offered ciphersuite.
 Clients MUST NOT enable fallback for fresh connections.
```
- `sec_protocol_options_set_tls_diffie_hellman_parameters(_:_:)` — AVAIL **iOS from 12.0 deprecated 13.0** ／ macOS 10.14 deprecated 10.15
> Set the supported Diffie-Hellman parameters.
- `sec_protocol_options_add_pre_shared_key(_:_:_:)`（AVAIL iOS 12.0）
> Add a pre-shared key (PSK) and its identity to the options.
- `sec_protocol_options_set_tls_pre_shared_key_identity_hint(_:_:)`（AVAIL iOS 13.0）
> Set the PSK identity hint to use by servers when negotiating a PSK ciphersuite. See https://tools.ietf.org/html/rfc4279 for more details.
- `sec_protocol_options_set_pre_shared_key_selection_block(_:_:_:)`（AVAIL iOS 13.0）
> Set the PSK selection block. ／ `A `dispatch_queue_t` on which the PSK selection block should be called.`
- `sec_protocol_options_set_challenge_block(_:_:_:)`（AVAIL iOS 12.0）
> Set the challenge block. ／ `A `sec_protocol_challenge_t` block.` ／ `A `dispatch_queue_t` on which the challenge block should be called.`
- `sec_protocol_options_set_key_update_block(_:_:_:)`（AVAIL iOS 12.0）
> Set the key update block. ／ `A `dispatch_queue_t` on which the key update block should be called.`
- `sec_protocol_options_add_tls_application_protocol(_:_:)`（AVAIL iOS 12.0）
> Add an application protocol supported by clients of this protocol instance.
- `sec_protocol_metadata_access_peer_certificate_chain(_:_:)`（AVAIL iOS 12.0）
> Get the certificate chain of the protocol instance peer. ／ Returns true if the peer certificates were accessible, false otherwise.
- `sec_protocol_metadata_create_secret(_:_:_:_:)`（AVAIL iOS 12.0）
> Export a secret, e.g., a cryptographic key, derived from the protocol metadata using a label string. ／ Returns a dispatch_data_t object carrying the exported secret.
（= TLS exporter，即 EKM；正文若谈"把 TLS 密钥派生用于上传签名"可引此句。）

### 5.3 TLS 1.3 与 Secure Transport 的关系（developer 侧）
状态：**部分核实 / 存在口径缺口**
- developer 侧**没有**找到「使用 Secure Transport API 的 TLS 客户端不能使用 TLS 1.3」这一句（该句属 support 手册页 `sec100a75d12`，由同事负责）。
- developer 侧能给的一手旁证是：`tls_protocol_version_t.TLSv13` 存在且 AVAIL iOS 13.0 / macOS 10.15（§4.3），而 Secure Transport legacy 枚举 `SSLProtocol` 只服务已弃用的 `set_tls_min_version`/`set_tls_max_version`（§5.2）。
- 未找到的页面（探测 404，记为待补）：`Security/secure-transport` 下的 TLS 1.3 声明、`Security/ksslprotocol`。
结论：正文若要写"SecureTransport 不支持 TLS 1.3"，**必须只引 support 手册页**，并注明 developer 侧无对应原话。

---

## 6. URLSession 与上传

### 6.1 URLSessionConfiguration：TLS 相关真实属性（**`requiresTLS13` 不存在**）
状态：**已核实**（存在性 + 逐字 abstract）
URL 前缀：https://developer.apple.com/documentation/Foundation/urlsessionconfiguration

页面 "Setting security policies" 分组实际列出的成员（逐字）：
`tlsMinimumSupportedProtocolVersion`、`tlsMaximumSupportedProtocolVersion`、`urlCredentialStorage`、`tlsMinimumSupportedProtocol`、`tlsMaximumSupportedProtocol`、`requiresDNSSECValidation`。

- `tlsMinimumSupportedProtocolVersion`
> The minimum TLS protocol version that the client should accept when making connections in this session.
AVAILABILITY：**iOS from 13.0 | iPadOS from 13.0 | Mac Catalyst from 13.1 | macOS from 10.15 | tvOS from 13.0 | visionOS from 1.0 | watchOS from 6.0**
- `tlsMaximumSupportedProtocolVersion`
> The maximum TLS protocol version that the client should request when making connections in this session.
AVAILABILITY：同上（iOS 13.0…）
（口径要点：**min 用 "accept"、max 用 "request"** —— 两个动词不对称，逐字抄下即可，正文别统一成"支持"。）
- `tlsMinimumSupportedProtocol` / `tlsMaximumSupportedProtocol`（旧 `SSLProtocol` 版）
> The minimum TLS protocol to accept during protocol negotiation.
> This property determines the minimum supported TLS protocol version for tasks within sessions based on this configuration.
AVAILABILITY：**iOS from 7.0 deprecated 27.2** | macOS from 10.9 deprecated 27.2（即 iOS 27.2 起弃用；这对"旧 API 正在被淘汰"是可用的量化事实）
- `requiresDNSSECValidation` — AVAILABILITY iOS from 16.0 … watchOS from 9.0（**页面 abstract 与 discussion 为空，仅签名可引 → 部分核实**）
- **`requiresTLS13` 未核实/不存在**：探测 `https://developer.apple.com/tutorials/data/documentation/foundation/urlsessionconfiguration/requirestls13.json` → **404**；且 `urlsessionconfiguration.json` 全页无 `requiresTLS13` 字样。现行文档中"强制 TLS 1.3"的唯一一手路径是 `tlsMinimumSupportedProtocolVersion = .TLS13`（或 `tls_protocol_version_t.TLSv13`，§4.3）。**正文不得把 `requiresTLS13` 当现行 API。**
- `enablesEarlyData`（新）— AVAILABILITY **iOS from 26.0 | macOS from 26.0**（abstract/discussion 在 JSON 中为空 → 部分核实；语义与 §4.2 `TLS.earlyDataEnabled` 的 WARNING 配套，0-RTT 重放风险）。
- `allowsUltraConstrainedNetworkAccess` — AVAILABILITY **iOS from 26.1 | macOS from 26.1**（无正文文本 → 部分核实）。
- `usesClassicLoadingMode` — AVAILABILITY **iOS from 18.4 | macOS from 15.4**（无正文文本 → 部分核实）。

其余分组（逐字来自页面）：`identifier`、`httpAdditionalHeaders`、`networkServiceType`、`timeoutIntervalForRequest`、`timeoutIntervalForResource`、`sharedContainerIdentifier`、`waitsForConnectivity`、`usesClassicLoadingMode`、`httpCookieAcceptPolicy`、`httpShouldSetCookies`、`httpCookieStorage`、`urlCache`、`requestCachePolicy`、`sessionSendsLaunchEvents`、`isDiscretionary`、`shouldUseExtendedBackgroundIdleMode`、`protocolClasses`、`multipathServiceType`、`httpMaximumConnectionsPerHost`、`httpShouldUsePipelining`、`proxyConfigurations`、`connectionProxyDictionary`、`allowsConstrainedNetworkAccess`、`allowsExpensiveNetworkAccess`、`backgroundSessionConfiguration(_:)`（Deprecated）。
（**没有 `URLSessionTaskNetworkServiceType` 这一页**；`networkServiceType` 的类型写作 `NSURLRequest.NetworkServiceType` —— 见下。）

### 6.2 连接性/网络访问许可标志（含默认值与失败错误码，逐字）
状态：**已核实**

- `allowsCellularAccess`（AVAIL iOS 7.0…）
> A Boolean value that determines whether connections should be made over a cellular network.
> This property controls whether tasks in sessions based on this session configuration are allowed to make connections over a cellular network.
> The default value is `true`.
> For more information, read `Restrict Cellular Networking Correctly`.

- `allowsExpensiveNetworkAccess`（AVAIL iOS 13.0…）
> A Boolean value that indicates whether connections may use a network interface that the system considers expensive.
> The system determines what constitutes “expensive” based on the nature of the network interface and other factors. iOS 13 considers most cellular networks and personal hotspots expensive. If there are no nonexpensive network interfaces available and the session’s `allowsExpensiveNetworkAccess` property is `false`, any task created from the session fails. In this case, the error provided when the task fails has a `networkUnavailableReason` property whose value is `NSURLErrorNetworkUnavailableReasonExpensive`.
> You can limit your app’s of use of expensive network access to user-initiated tasks, and put off discretionary tasks until a nonexpensive interface becomes available. To do this, set `allowsExpensiveNetworkAccess` (and `allowsConstrainedNetworkAccess`) to `false` and `waitsForConnectivity` to `true`. This way, your `URLSessionTask` waits for a suitable interface to become available before sending or receiving data.
> To test the behavior of this property, you can override the device’s current values for cellular and Wi-Fi cost in Settings > Developer > Network Override.
>>> [TIP]
> Prefer basing your app’s policy logic around the `allowsConstrainedNetworkAccess` property rather than this one. People using your app can use the “Low Data Mode” setting to set the constrained status, and thereby choose to use a potentially expensive network.

- `allowsConstrainedNetworkAccess`（AVAIL iOS 13.0…）
> A Boolean value that indicates whether connections may use the network when the user has specified Low Data Mode.
> In iOS 13 and later, users can set their device to use Low Data Mode as one of the Cellular Data Options in the Settings app. … If there are no nonconstrained network interfaces available and the session’s `allowsConstrainedNetworkAccess` property is `false`, any task created from the session fails. In this case, the error provided when the task fails has a `networkUnavailableReason` property whose value is `NSURLErrorNetworkUnavailableReasonConstrained`.
> Limit your app’s of use of constrained network access to user-initiated tasks, and put off discretionary tasks until a nonconstrained interface becomes available. To do this, set `allowsConstrainedNetworkAccess` (and `allowsExpensiveNetworkAccess`) to `false` and `waitsForConnectivity` to `true`. This way, your `URLSessionTask` waits for a suitable interface to become available before sending or receiving data.

- `waitsForConnectivity`（AVAIL iOS 11.0…）
> A Boolean value that indicates whether the session should wait for connectivity to become available, or fail immediately.
> Connectivity might be temporarily unavailable for several reasons. For example, a device might only have a cellular connection when `allowsCellularAccess` is set to `false`, or the device might require a VPN connection but none is available. If the value of this property is `true` and sufficient connectivity is unavailable, the session calls the `urlSession(_:taskIsWaitingForConnectivity:)` method of `URLSessionTaskDelegate` and waits for connectivity. When connectivity becomes available, the task begins its work and ultimately calls the delegate or completion handler as usual.
> If the value of the property is `false` and connectivity is unavailable, the connection fails immediately with an error, such as `NSURLErrorNotConnectedToInternet`.
> This property is relevant only during the establishment of a connection. If a connection is established and then drops, the completion handler or delegate receives an error, such as `NSURLErrorNetworkConnectionLost`. For help dealing with dropped connections, see `Handling “The network connection was lost” Errors`.
> **This property is ignored by background sessions, which always wait for connectivity.**

- `isDiscretionary`（AVAIL iOS 7.0…）
> A Boolean value that determines whether background tasks can be scheduled at the discretion of the system for optimal performance.
> For configuration objects created using the `background(withIdentifier:)` method, use this property to give the system control over when transfers should occur. **This property is ignored for configuration objects created using other methods.**
> When transferring large amounts of data, you are encouraged to set the value of this property to `true`. Doing so lets the system schedule those transfers at times that are more optimal for the device. For example, the system might delay transferring large files until the device is plugged in and connected to the network via Wi-Fi. The default value of this property is `false`.
> The session object applies the value of this property only to transfers that your app starts while it is in the foreground. For transfers started while your app is in the background, the system always starts transfers at its discretion—in other words, **the system assumes this property is `true` and ignores any value you specified**.

- `httpMaximumConnectionsPerHost`（AVAIL iOS 7.0…）
> The maximum number of simultaneous HTTP/1.1 connections to make to a given host.
> This property determines the maximum number of simultaneous HTTP/1.1 connections made to each host by tasks within sessions based on this configuration.
> This limit is per session, so if you use multiple sessions, your app as a whole may exceed this limit. Additionally, depending on your connection to the Internet, a session may use a lower limit than the one you specify.
> The default value is `6`.
> **HTTP/2 and later run multiple requests over a single connection and thus ignore this property.**

- `urlCache`（AVAIL iOS 7.0…）
> The URL cache for providing cached responses to requests within the session.
> This property determines the URL cache object used by tasks within sessions based on this configuration.
> To disable caching, set this property to `nil`.
> For default sessions, the default value is the shared URL cache object.
> For background sessions, the default value is `nil`.
> For ephemeral sessions, the default value is a private cache object that stores data in memory only, and is destroyed when you invalidate the session.

- `shouldUseExtendedBackgroundIdleMode`（AVAIL iOS from 9.0 **deprecated 18.4** | macOS deprecated 15.4）
> A Boolean value that indicates whether TCP connections should be kept open when the app moves to the background.
> In addition to requesting that the connection be kept open, setting this value to `true` asks the system to delay reclaiming the connection when the app moves to the background.

- `networkServiceType`（AVAIL iOS 7.0…）
> The type of network service for all tasks within network sessions to enable Cellular Network Slicing.
> To enable Cellular Network Slicing, you need to set the appropriate entitlements and properties.
> Set the entitlements in your property list for `5G Network Slicing App Category` and `5G Network Slicing Traffic Category`. If you don’t entitle your app by specifying both these entitlements, your apps network connections won’t be using Cellular Network Slicing, even if supported by the carrier.
> At the time of network flow creation, set this to the appropriate `NSURLRequest.NetworkServiceType` for your application type.

- `sharedContainerIdentifier`（AVAIL iOS 8.0…）
> The identifier for the shared container into which files in background URL sessions should be downloaded.
> To create a URL session for use by an app extension, set this property to a valid identifier for a container shared between the app extension and its containing app.
>>> [IMPORTANT]
> If you try to create a URL session from your app extension but fail to set this property to a valid value, the URL session is invalidated upon creation.

- `sessionSendsLaunchEvents`（AVAIL iOS 7.0…）
> A Boolean value that indicates whether the app should be resumed or launched in the background when transfers finish.
> For configuration objects created using the `background(withIdentifier:)` method, you can use this property to control the launching behavior for an iOS app. This property is ignored for configuration objects created using other methods.
> The default value of this property is `true`. When the value of this property is `true`, the system automatically wakes up or launches the iOS app in the background when the session’s tasks finish or require authentication. At that time, the system calls the app delegate’s `application(_:handleEventsForBackgroundURLSession:completionHandler:)` method…

口径要点（上传正文可直接用）：
- 量化基线表：`allowsCellularAccess` 默认 **true**；`isDiscretionary` 默认 **false**；`sessionSendsLaunchEvents` 默认 **true**；`httpMaximumConnectionsPerHost` 默认 **6**（且 HTTP/2+ 忽略）。
- **三条"仅在后台会话生效/被忽略"条件句**逐字在手：`isDiscretionary`、`sessionSendsLaunchEvents` 都写明 "ignored for configuration objects created using other methods"；`waitsForConnectivity` 写明 "ignored by background sessions, which always wait for connectivity"。这是"配置项的作用域由会话类型决定"的最好证据。
- 传输层与许可层的分工：以上全部标志只决定"能不能用某类接口"，不涉及 TLS；TLS 只在 §6.1 的三个属性 + ATS。

### 6.3 后台会话与上传任务
状态：**已核实**（逐字）

`URLSessionConfiguration.background(withIdentifier:)`（AVAIL **iOS from 8.0**）
> Creates a session configuration object that allows HTTP and HTTPS uploads or downloads to be performed in the background.
> A configuration object that causes the system to perform upload and download tasks in a separate process.
> Use this method to initialize a configuration object suitable for transferring data files while the app runs in the background. A session configured with this object **hands control of the transfers over to the system, which handles the transfers in a separate process**. In iOS, this configuration makes it possible for transfers to continue even when the app itself is suspended or terminated.
> If an iOS app is terminated by the system and relaunched, the app can use the same `identifier` to create a new configuration object and session and to retrieve the status of transfers that were in progress at the time of termination. This behavior applies only for normal termination of the app by the system. **If the user terminates the app from the multitasking screen, the system cancels all of the session’s background transfers.** In addition, the system does not automatically relaunch apps that were force quit by the user. The user must explicitly relaunch the app before transfers can begin again.
> You can configure an background session to schedule transfers at the discretion of the system for optimal performance using the `isDiscretionary` property.

`URLSessionConfiguration.backgroundSessionConfiguration(_:)` — 已弃用
AVAILABILITY：**iOS from 7.0 deprecated 8.0 | macOS from 10.9 deprecated 10.10**
> Returns a session configuration object that allows HTTP and HTTPS uploads or downloads to be performed in the background.
> A URL session configuration object that causes upload and download tasks to be performed by the system in a separate process.

`URLSessionUploadTask`（AVAIL iOS 7.0…）
> A URL session task that uploads data to the network in a request body.
> The `URLSessionUploadTask` class is a subclass of `URLSessionDataTask`, which in turn is a concrete subclass of `URLSessionTask`. The methods associated with the `URLSessionUploadTask` class are documented in `URLSessionTask`.
> Upload tasks are used for making HTTP requests that require a request body (such as `POST` or `PUT`). They behave similarly to data tasks, but you create them by calling different methods on the session that are designed to make it easier to provide the content to upload. As with data tasks, if the server provides a response, upload tasks return that response as one or more `NSData` objects in memory.
>>> [NOTE]
> Unlike data tasks, you can use upload tasks to upload content in the background.
> When you create an upload task, you provide a `URLRequest` instance that contains any additional headers that you might need to send alongside the upload, such as the content type, content disposition, and so on. **In iOS, when you create an upload task for a file in a background session, the system copies that file to a temporary location and streams data from there.**
> While the upload is in progress, the task calls the session delegate’s `urlSession(_:task:didSendBodyData:totalBytesSent:totalBytesExpectedToSend:)` method periodically to provide you with status information.
> When the upload phase of the request finishes, the task behaves like a data task, calling methods on the session delegate to provide you with the server’s response—headers, status code, content data, and so on.

`uploadTask(with:from:)`（AVAIL iOS 7.0…）
> Creates a task that performs an HTTP request for the specified URL request object and uploads the provided data.
> An HTTP upload request is any request that contains a request body, such as a `POST` or `PUT` request. Upload tasks require you to create a request object so that you can provide metadata for the upload, like HTTP request headers.
> After you create the task, you must start it by calling its `resume()` method. The task calls methods on the session’s delegate to provide you with the upload’s progress, response metadata, response data, and so on.

`uploadTask(with:fromFile:)`（AVAIL iOS 7.0…）
> Creates a task that performs an HTTP request for uploading the specified file.
（discussion 与 `uploadTask(with:from:)` 同文。）

`uploadTask(withStreamedRequest:)`（AVAIL iOS 7.0…）
> Creates a task that performs an HTTP request for uploading data based on the specified URL request.
> …The session’s delegate must have a `urlSession(_:task:needNewBodyStream:)` method that provides the body data to upload.

`uploadTask(withResumeData:)`（AVAIL **iOS from 17.0** | macOS from 14.0 | watchOS from 10.0）
> Creates an upload task from a resume data blob. Requires the server to support the latest resumable uploads Internet-Draft from the HTTP Working Group, found at https://datatracker.ietf.org/doc/draft-ietf-httpbis-resumable-upload/ If resuming from an upload file, the file must still exist and be unmodified. If the upload cannot be successfully resumed, URLSession:task:didCompleteWithError: will be called.
> A new session upload task, or nil if the resumeData is invalid.

异步（async/await）上传入口（逐字来自 URLSession 页 "Performing asynchronous transfers" 分组）：`upload(for:from:delegate:)`、`upload(for:fromFile:delegate:)`、`upload(for:from:)`、`upload(for:fromFile:)`。

**`multipartFormData` / 后台会话限制：状态 = 未核实。**
- 在 `Foundation` 相关 JSON 页（`urlsessionconfiguration`、`urlsession`、`urlsessiontask`、`urlsessionuploadtask`、`urlrequest`、`httpurlrequest`）中 **grep "multipart" 命中 0 次**；不存在 `Foundation/multipartformdata` 之类页面。
- 因此"后台会话不支持 multipart 表单/不能用 in-memory body"这类常见说法，**在现行 developer 文档里没有一手出处**；正文若要写，只能写成"社区经验/未获一手文档支持"。

### 6.4 上传实操三篇（URL Loading System "Uploading" 分组，全部逐字）
状态：**已核实**
URL：https://developer.apple.com/documentation/Foundation/uploading-data-to-a-website
> Post data from your app to servers.
> Many apps work with servers that accept uploads of files like images or documents, or use web service API endpoints that accept structured data like JSON. To upload data from your app, you use a `URLSession` instance to create a `URLSessionUploadTask` instance. The upload task uses a `URLRequest` instance that details how the upload is to be performed.
> The data to upload can be the contents of a file, a stream, or data, as is the case in the following example.
> An upload task requires a `URLRequest` instance. As shown in the following example, set the `httpMethod` property of the request to `"POST"` or `"PUT"`, depending on what the server supports and expects. Use the `setValue(_:forHTTPHeaderField:)` method to set the values of any HTTP headers that you want to provide, **except the `Content-Length` header. The session figures out content length automatically from the size of your data.**
> To begin an upload, call `uploadTask(with:from:completionHandler:)` on a `URLSession` instance … Because tasks start in a suspended state, you begin the network loading process by calling `resume()` on the task. … The handler checks for transport and server errors before using any returned data.
> As an alternative to the completion handler approach, you can instead set a delegate on a session you configure, and then create the upload task with `uploadTask(with:from:)`. In this scenario, you implement methods from the `URLSessionDelegate` and `URLSessionTaskDelegate` protocols. These methods receive the server response and any data or transport errors.
（示例代码块逐字含 `let url = URL(string: "https://example.com/post")!`、`request.setValue("application/json", forHTTPHeaderField: "Content-Type")`。）

URL：…/Foundation/uploading-streams-of-data
> Send a stream of data to a server.
> Streaming media apps and long-running apps that send continual updates use an ongoing stream to upload data, rather than sending a single block of data or a flat file. You can configure an instance of `URLSessionUploadTask` (a subclass of `URLSessionTask`) to work with a stream that you provide, and then fill this stream with data indefinitely.
> The task gets the stream by calling your session’s delegate, so you need to create a session and set your own code as its delegate.
> You provide the streaming data to the upload task as an `InputStream`. The task reads data from this stream and uploads it to the destination.
> A good way to provide data to the input stream is to use a *bound pair* of streams. …
> When you create the bound pair, make sure you specify a buffer size large enough to hold any data you write to the output stream, prior to the data being read from the input stream. The following example uses a 4096-byte buffer.
>>> [TIP]
> Your implementations of `StreamDelegate` and `URLSessionTaskDelegate` may be in the same class or in different classes, whichever makes more sense for your app’s architecture.

URL：…/Foundation/pausing-and-resuming-uploads
> Pause and resume an upload without starting over, even when the connection is interrupted.
> Your app or a person using it might need to cancel an in-progress upload and resume it later. By supporting resumable uploads, you save both the user’s time and network bandwidth.
> **New in iOS 17 and aligned OS versions**, `URLSession` supports resumable uploads according to the `latest resumable upload protocol draft`. This protocol is currently being developed in the HTTP Working Group at the IETF. To take advantage of resumable uploads, the server you’re connecting to must also support this protocol.
> You can use this technique to manually pause and resume an upload, or even resume an upload that failed due to a temporary loss of connectivity. In both cases, `URLSession` needs information on how to resume the upload, which is stored in a `resumeData` object.
> You can effectively pause a `URLSessionUploadTask` by calling `cancel(byProducingResumeData:)`. This method cancels the task and passes a `resumeData` parameter to its completion handler. If `resumeData` is not `nil`, you can use this token later to resume the upload.
>>> [IMPORTANT]
> You can’t resume all uploads. The server must support the `latest resumable upload protocol draft` from the HTTP Working Group at the IETF. Also, **uploads that use a background configuration handle resumption automatically, so manual resuming is only needed for non-background uploads.**
> If there’s only a momentary network interruption, but the server is still reachable, `URLSession` automatically tries to resume the upload for you. No extra code is needed.
> When the upload fails, the session calls your `urlSession(_:task:didCompleteWithError:)` delegate method. If `error` is not `nil`, look in its `userInfo` dictionary for the key `NSURLSessionUploadTaskResumeData`. … You can conveniently access resume data using the `uploadTaskResumeData` property of `URLError`.

口径要点：可恢复上传 = **依赖服务器支持 IETF HTTP Working Group 的 resumable-upload 草案**（一手原话两处），且**后台会话自动处理续传**；这是"上传机制"一节的机制骨架。同时"瞬时中断由 URLSession 自动重试"给了重试层次划分。

### 6.5 认证挑战的默认处理与 `performDefaultHandling`
状态：**已核实**（逐字）URL：https://developer.apple.com/documentation/Foundation/handling-an-authentication-challenge

> Respond appropriately when a server demands authentication for a URL request.
> When your app makes a request with a `URLSessionTask`, the server may respond with one or more demands for credentials before continuing. The session task attempts to handle this for you. If it can’t, it calls your session’s `delegate` to handle the challenges.
> Implement the delegate methods described in this article to answer challenges issued by a server that your app connects to. If you don’t implement a delegate, your request may be denied by the server, and you receive a response with HTTP status code `401` (Forbidden) instead of the data you expect.

> - Implement the `urlSession(_:didReceive:completionHandler:)` method of `URLSessionDelegate` to handle **session-wide** challenges. These are challenges like **Transport Layer Security (TLS) validation**. Once you’ve successfully handled this kind of challenge, your action remains in effect for all tasks created from that `URLSession`.
> - Implement the `urlSession(_:task:didReceive:completionHandler:)` method of `URLSessionTaskDelegate` to handle **task-specific** challenges. These are challenges like demands for username/password authentication. Each task created from a given session may issue its own challenges.
>>> [NOTE]
> See `NSURLProtectionSpace authentication method constants` for a guide to which authentication methods are session-wide or task-specific.
> If you connect via `https`, you also receive a server trust challenge. See `Performing manual server trust authentication` for information on handling this type of session-wide challenge.

> You respond to the challenge by directly invoking the completion handler passed in to the challenge, passing an `URLSession.AuthChallengeDisposition` indicating your response to the challenge. You use the disposition argument to provide a credential, cancel the request, or allow the default handling to proceed, whichever is appropriate.
> The following example tests the authentication method to see if it is the expected type, HTTP Basic. If the `authenticationMethod` property indicates some other kind of challenge, it calls the completion handler with the `URLSession.AuthChallengeDisposition.performDefaultHandling` disposition. **Telling the task to use its default handling may satisfy the challenge; otherwise, the task will move on to the next challenge in the response and call this delegate again.**
```
let authMethod = challenge.protectionSpace.authenticationMethod
guard authMethod == NSURLAuthenticationMethodHTTPBasic else {
    completionHandler(.performDefaultHandling, nil)
    return
}
```
> In this example, the returned `URLCredential` has `URLCredential.Persistence.forSession` persistence, so it’s only stored by the `URLSession` instance that created the task. You would need to supply new `URLCredential` instances for tasks created by other session instances, and on future runs of the app.
> - If you can’t create a credential, or if the user explicitly canceled, call the completion handler and pass the `URLSession.AuthChallengeDisposition.cancelAuthenticationChallenge` disposition.
> - If you can create a credential instance, use the `URLSession.AuthChallengeDisposition.useCredential` disposition to pass it to the completion handler.
> If you supply a credential that is accepted by the server, the task begins uploading or downloading data.

口径要点（三条必须写对的机制）：
1. **TLS 服务器信任挑战是 session-wide**（走 `URLSessionDelegate`），处理一次对该 session 的所有后续任务持续有效 —— 直接影响"上传任务复用同一 session 时信任决策的作用域"。
2. 未实现 delegate ≠ 不处理：系统有自己的默认处理（`performDefaultHandling` 会先尝试满足挑战，否则进入下一个挑战）。
3. 凭据持久性 `URLCredential.Persistence.forSession` 的作用域逐字为 "only stored by the URLSession instance that created the task"。
（`URLAuthenticationChallenge`、`URLCredential`、`URLProtectionSpace` 三页存在（HTTP 200，© 2026）：/documentation/foundation/urlauthenticationchallenge、…/urlcredential、…/urlprotectionspace；本次未逐字摘录其 discussion → 列入待补。）

### 6.6 URLSession 与 ATS 的关系（一手原话汇总）
状态：**已核实**（三处独立一手表述，互相一致）
1. `NSAppTransportSecurity` 页（§2.1）：
> ATS requires that all HTTP connections made with the `URL Loading System`—typically using the `URLSession` class—use HTTPS.
2. `Preventing Insecure Network Connections`（§1.2）：
> The system enforces ATS when you use the standard `URL Loading System`. Instances of `URLSession` automatically negotiate the most secure connection available from the server. The only action your app must take is to use secure URLs, like those beginning with `https`.
> ATS doesn’t apply to calls your app makes to lower-level networking interfaces like the `Network` framework or `CFNetwork`.
3. `Performing manual server trust authentication`（§1.4）：
> `URLSession` enforces `App Transport Security (ATS)`, if it is enabled for the domain you are connecting to. This applies security requirements for the certificates, TLS version, and cipher used by the connection. You cannot loosen server trust requirements for an ATS-protected domain, but you can tighten them, using the manual evaluation technique shown in this article.

口径要点：**"ATS 自动应用于 URLSession/NSURLConnection/CFNetwork"这类旧口径在现行 developer 文档中不存在**，现行原话是"ATS 由 URL Loading System 强制、不适用于 Network framework 与 CFNetwork"。三者合并即可完整回答"传输安全要求由谁配置"：Info.plist（App 级、只对 URLSession）／`URLSessionConfiguration`（会话级）／`sec_protocol_options`＋`Network.TLS`（连接级、ATS 不参与）。


---

## 7. 本地网络权限

（待填）

---

## 8. QUIC / HTTP3

（待填）

---

## 9. 技术说明与 API 选择

（待填）

---

## 10. 其它高价值一手句

（待填）

---

## 11. 量化基线表（可直接进正文）

（待填）

---

## 12. 待补 / 预期无一手页

（待填）
