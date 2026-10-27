@@TITLE@@iOS 应用网络连接传输安全要求的实施与上传机制分析
@@SUBTITLE@@How Transport Security Requirements Are Actually Imposed On A Single App's Outbound Connections — The ATS Baseline An App Inherits At Compile Time, Every Info.plist Key That Loosens Or Tightens It, Review And Export-Compliance Pressure, Server Trust Evaluation You May Tighten But Never Relax, Certificate Pinning And Its Rotation Cost, The Network.framework And URLSession Policy Surfaces, Local Network Permission, Who Else Can Change The Requirement, And The Three Upload Directions Seen From The App Side
@@INFO@@报告日期|2026年10月22日
@@INFO@@分析对象|默认基线（App 传输安全性 ATS 的自动适用判据、五条扩展检查、硬故障语义、不适用的调用路径）· 配置面（`NSAppTransportSecurity` 全部键：全局宽松四键、全局收紧一键、例外域字典与四键、第三方内容三键、转发保密密码套件顺序键、优先级与覆盖关系）· 外部压力（App Store 审核对安全连接的要求与例外理由、加密申报与出口合规）· 信任评估（默认四条、SecTrust 与 challenge 的责任划分、手工评估只能收紧、用户安装根证书、撤销与证书透明度的 App 侧可观测性）· 固定（`NSPinnedDomains` 两类键与 AND 语义、固定对象三种形态、锚证书替换、被官方承认的代理与内容过滤重加密通道）· 代码策略面（Network.framework 的 TLS 选项：版本上下界、服务器名与 ALPN、额外信任根、链校验与透明度策略、OCSP 阻断、PSK、会话恢复、量子安全开关、自定义评估回调；URLSession 的 `requiresTLS13`、重试与服务类型、凭据与客户端证书、HTTP/3 与 QUIC、后台会话上传）· 权限与可见性（本地网络权限、Bonjour 服务声明、拒绝后果）· 变更主体（开发者、用户、组织、Apple、对端服务器五方及各自可观测痕迹）· 上传（U1 服务端上传前加密、U2 iCloud 容器与加密档位、U3 对端与跨设备 API、上传中间文件的设备侧保护类、元数据与流量可见性）· 平台差异（ATS 适用平台与版本、API 可用性矩阵、macOS 沙盒与防火墙、watchOS 网络路径、Mac Catalyst）
@@INFO@@视角定位|防御与研究教育视角：回答"一个 App 的出站连接，传输安全要求是谁定的、哪一条可以由 App 自己放松或收紧、组织和用户能从哪里插进来、上传之后这些要求还成立吗"，并给出可直接用于配置审计、代码评审与合规申报的判定链、可观测痕迹与自查清单。本篇不包含任何绕过既有传输安全要求的操作方法、不含利用代码、不含针对特定目标或第三方系统的攻击与监听步骤。
@@INFO@@分类|网络安全 · 传输层安全 · 应用合规 · 数据上传与同步
@@INFO@@难度|中级偏高级（需了解 TLS 握手与密码套件、X.509 与信任锚、App Sandbox 与配置描述文件、iOS 权限模型基础；建议先读本篇系列中的网络通信机密性与完整性分析一期）
@@INFO@@联系方式|Telegram: https://t.me/one00190

@@TOC@@目录

# 0. 概述、口径与阅读方式

## 0.1 本篇要回答的六个问题

上一期（网络通信的机密性与完整性分析）回答的是"一条跨网络的连接，机密性与完整性分别由哪一层提供"。那一篇的结论里有一条被反复引用的官方句子：ATS **不适用于** App 通过 Network.framework 等更低层接口发起的调用。这句话留给了本篇一个更尖锐的问题集合——**同一台设备上，一个 App 的出站连接究竟受谁管、管到哪一步、谁有权松手**。本篇按六个可分别取证的问题组织：

1. **要求是谁定的（来源）**：一条 URL 请求所受的要求来自四个方向——系统在链接时按 SDK 版本自动附加的 ATS 基线、App 自己在 `Info.plist` 与代码里配置的例外与参数、运行环境（组织、用户、锁定模式）附加或插入的东西、以及**对端服务器实际能提供什么**。四者强度不同、失效条件不同，把它们合称"iOS 的网络加密要求"是本主题最常见的口径塌缩。
2. **App 能放松哪几条（放松面）**：现行 developer 文档逐字列出**只有五个键**会触发"必须提供理由、可能触发额外审核"：`NSAllowsArbitraryLoads`、`NSAllowsArbitraryLoadsForMedia`、`NSAllowsArbitraryLoadsInWebContent`、`NSExceptionAllowsInsecureHTTPLoads`、`NSExceptionMinimumTLSVersion`。清单外还有明确**不需要**理由的两个 NIAP 键。放松面是有清单的，不是"想怎么写就怎么写"。
3. **哪几条只能收紧（不对称）**：一手原句是 **"You cannot loosen server trust requirements for an ATS-protected domain, but you can tighten them"**。注意它的限定语 *for an ATS-protected domain*——这句既给出"收紧自由、放松受限"的不对称结论，也给出反证条件：一个域如果被例外豁免，就不再是 ATS-protected domain。本篇在 §2、§5、§6 反复使用这个条件式，而不是把它写成无条件禁令。
4. **API 选型如何改变责任归属（选型）**：`URLSession` 走 URL Loading System，ATS 由系统强制执行，App 什么都不做也能满足基线；一旦改用 Network.framework 或 CFNetwork 直调，一手文档写的是 **"you take responsibility for ensuring the security of the connection"**——Info.plist 里所有 ATS 键一概不生效。这是本篇与上一期最重要的交接点：**基线不是设备属性，而是"你调用哪个 API"的属性**。
5. **无效证书为什么永远救不回来（硬故障）**：`nscurl` 诊断页给了顺序证据——"if the server's certificate doesn't match the DNS name of the server, the connection fails **default server trust evaluation before ATS has a chance to impose its extended security checks**"。默认信任评估在前，ATS 扩展检查在后；例外只能放松后者。任何"加了例外就能连自签证书服务器"的说法都与之冲突（§5.3、§3.12）。
6. **上传之后这些要求还成立吗（上传）**：App 侧的上传有三条方向，分别由不同主体持有密钥与明文可见性。本篇把它们重新定义为 U1/U2/U3（§0.3），并在 §11 逐方向回答"传输要求由谁配置、要求是否在服务端落盘后仍然有效、元数据是否在被保护范围内"。

## 0.2 要求分层：系统强制层、App 自配层、运行环境插图层

本篇的分析骨架是三层。任何一次"这条连接安全吗"的判断，都必须先说清是在哪一层上说：

| 层 | 内容 | 谁写入 | 何时生效 | 能否被更上层覆盖 | 本篇章节 |
|---|---|---|---|---|---|
| **第一层：系统强制层** | 默认服务端信任评估（四条）+ ATS 基线（TLS ≥1.2、RSA ≥2048/EC ≥256、SHA-256、AES-128/256、必须 ECDHE） | Apple 系统实现，判据是 App **链接的 SDK 版本** | 链接即继承：针对 iOS 9.0 / macOS 10.11 SDK 或更高版本链接的 App 默认开启 | 链接早于该 SDK 则"无论运行在哪个系统版本都不启用"（一手 NOTE 原句） | §2、§5.1 |
| **第二层：App 自配层** | `NSAppTransportSecurity` 全部键、声明式固定 `NSPinnedDomains`、NIAP 合规模式、代码里的 TLS 参数与信任评估回调 | 开发者 | 启动时读入，按域与调用路径生效 | 放松方向需要理由并可能触发审核；收紧方向自由 | §3、§4、§6、§7、§8 |
| **第三层：运行环境插图层** | 配置描述文件与设备管理策略、代理与内容过滤器（组织可让流量在 App 之外被检查）、用户或管理员安装的根证书、锁定模式、蜂窝与计费网络许可、对端服务器能力 | 组织 / 用户 / Apple 服务 / 服务器运营方 | 运行时插入，App 通常无法拒绝也无法完全感知 | 这一层的存在正是"App 侧配置正确 ≠ 链路真实安全"的原因 | §10、§6.5、§8.7 |

三层的**判据不同**是第一层最值得注意的性质：它不看用户系统的版本、也不看 App 的部署目标（deployment target），只看**链接时用的 SDK**。这条口径在一手页里出现了两次且措辞一致（ATS 总纲页与 `NSAppTransportSecurity` 页的 Versioning 段），因此本篇把它当作可直接引用的硬事实，而不是工程惯例。

## 0.3 三个上传方向在 App 侧的重新定义

上一期用 U1/U2/U3 划分"数据离开设备"的三个方向，并按协议与密钥归属分析。本篇保留同一套代号，但**换成 App 开发者的可配置项**来定义——即"这个方向的传输安全要求，究竟由谁、用什么写入"：

| 代号 | 方向 | App 侧的写入点 | 传输要求的实际来源 | 服务端可见什么 | 本篇章节 |
|---|---|---|---|---|---|
| **U1** | 上传到自建或第三方服务端 | `URLSession` 上传任务、后台会话、第三方 SDK | 走 URL Loading System 时 = ATS 强制；改用 Network.framework 直调 = 全靠 App 自己 | 传输层终止后即明文可见（除非 App 在上传前自己加密） | §11.1、§11.2、§8 |
| **U2** | 同步进 iCloud（云盘、键值同步、CloudKit 容器） | `CloudKit` 数据库类型选择、iCloud 能力与容器配置、文件保护级别 | Apple 定义的记录密钥与服务密钥体系，**App 不能通过 ATS 键影响** | 取决于两类服务密钥：端对端加密档 Apple 拿不到明文；认证后可用档服务端可取 | §11.3–§11.5、§11.7 |
| **U3** | 发给另一台设备（对端） | 多端连接类 API、局域网访问、本地网络权限声明 | 本地链路的 TLS 与配对密钥由系统建立；App 能改的是"是否请求本地网络访问" | 中继类通道只见元数据；直连类由参与设备持有密钥 | §11.6、§9 |

这三条方向在同一次业务里常常串联：一张照片通过 U3 发给对方时，本体可能先以"设备侧加密、密钥走端对端消息"的形态经 U1 落进云端；同一次业务的崩溃日志又通过诊断通道离开设备。**传输要求保护的是"在路上的那一刻"，不保护已经落进某一方存储的副本**——这是本篇 §11.9 与 §14.5 反复强调的一条边界。

## 0.4 证据分级与引用规范

本篇只接受 Apple 一手文本作为 A 级证据。分级如下：

- **A 级（已核实）**：在 developer.apple.com 文档页、Apple 技术说明（TN）、或《Apple 平台安全保护》zh-cn 手册页逐字读到。手册页逐条标注**发布日期**；developer 文档页页脚统一为 `Copyright © 2026 Apple Inc. All rights reserved.`，本篇统一记为"截至 2026 可用"；TN 类页面若正文自带修订历史，则逐条抄出修订日期。**引文一律保持原文语言**：developer 与 TN 是英文，本篇照抄英文并另给中文释义；手册页是中文，直接抄中文。
- **B 级（部分核实）**：口径在官方文本里存在，但页面没有给出数值、默认值或适用范围。引用时必须写出"页面列出但未声明"的部分，**禁止补数字**。
- **C 级（未核实）**：找不到一手页面。C 级在本篇只能作为缺口出现（集中在 §14.2），**不得作为结论**。

三条取证事实必须交代清楚，否则读者复核时会踩同一个坑：

1. **`https://developer.apple.com/documentation/security/app_transport_security` 这个 URL 现在返回 404。** 现行 ATS 一手页是 `documentation/Security/preventing-insecure-network-connections`（集合页标题 *Preventing Insecure Network Connections*）与 `documentation/bundleresources/information-property-list/nsapptransportsecurity`（配置字典）。网上大量二手文章仍在引用前者，引用前请核对页面是否存在。
2. **developer 文档站是单页应用**，直接抓 HTML 会得到 "The page you're looking for can't be found."，正文实际来自 JSON 端点 `https://developer.apple.com/tutorials/data/documentation/<slug>.json`。本篇所有 developer 引文取自该端点的 `primaryContentSections`（`abstract` / `content` / `details` / `attributes` / `declarations`），即页面渲染所用的原始文本。
3. **手册页与 developer 文档在同一个口径上可能措辞不同**。最典型的一处：手册页写 ATS 作用于"NSURLConnection、CFURL 或 NSURLSession"，而现行 developer 页明确写 ATS **不适用**于 `Network` framework 与 `CFNetwork` 直调。两句都真实存在，本篇两条都引，并显式注明"两处口径不同"，不擅自统一到其中一条。

另外，引文中若出现页面自身的拼写或排版瑕疵（例如 ATS 总纲页第四条里的 `in turned`、NIAP 段落里的双空格），本篇**按原样照抄**并加注"页面原文如此"，不做"顺手修正"——修正会破坏逐字可核对性。

## 0.5 术语纪律：三组必须分开说的词

本篇严格按以下命名口径写作。第一组是本系列的通用纪律，后两组是本篇特有的塌缩点：

**（一）文件保护的三个名字不得混用**

- **数据保护（Data Protection）** 是 iOS 上按文件、按保护类加密用户数据的机制名。
- **文件保险箱 / FileVault** 是 macOS 时代的宗卷加密名称；手册页在描述 Mac 加密磁盘映像时确实用了"和文件保险箱一样，磁盘映像的内容是实时加密和解密的"这样的类比句，那是 Mac 语境。**它不是 iOS 文件保护机制的名字。**
- **数据保险箱（Data Vault / 数据保险库类容器）** 属于沙盒与容器语境，与上两者都不是同一件事。

**（二）"随机化"与"加密"与"授权"三种随机**

上传链路里"随机"一词至少指三件不同的事，混谈会导致结论完全错误：**密钥随机**（每次上传用新的数据密钥，属于密码学强度）、**标识随机**（私有无线局域网地址、隔空投送短身份哈希，属于隐私关联）、**地址与偏移随机**（内存布局随机化，属于抗利用，见本系列地址布局随机化一期）。传输安全要求只关心第一件。

**（三）本篇最容易被写错的五个短语**

| 错误写法 | 为什么错 | 正确写法 |
|---|---|---|
| "ATS 要求 TLS 1.3" | ATS 基线是 **TLS 1.2 或更高**；TLS 1.3 与量子安全套件属于另一条线（API 默认值与 `URLSession` 的 `requiresTLS13`） | "ATS 下限 TLS 1.2；更高版本由 API 默认与 App 自选决定" |
| "iOS 支持 TLS 1.0/1.1，所以可以用" | 手册页的"支持"是协议清单，ATS 的"必须 1.2 或更高"才是连接判据；`NSExceptionMinimumTLSVersion` 只是**例外通道**且需要审核理由 | 把"支持清单"与"允许下限"分开写 |
| "配了 ATS 例外就能连自签证书服务器" | 默认信任评估在 ATS 之前，名称不匹配即硬故障；例外放松不了它（NIAP 页与 `nscurl` 页各有一句一手证据）；正确做法是 App 内嵌信任锚并**收紧**评估 | "例外只作用于 ATS 扩展检查；信任锚要么系统内置，要么由用户或管理员安装" |
| "证书固定就是替换根证书" | 固定与替换锚是两种机制，`NSPinnedDomains` 的两类键（CA 与叶子）与 `SecTrustSetAnchorCertificates` 的语义不同，且固定是"叠加要求"而非"放松要求" | §6 分三小节写清 |
| "本地网络权限就是 ATS 的本地例外" | `NSAllowsLocalNetworking` 管的是**加密要求**，本地网络权限管的是**能不能访问局域网**（隐私关联），两条正交 | §4.3 与 §9.4 分别写 |

## 0.6 与前序各期的分工关系

本系列在"数据保护"这条线上已经写了四期，本篇是第五期，分工如下：

- **保护级别与设备状态控制文件访问一期**（#89）：文件在**静止时**由哪个保护类守着、解锁状态改变能读到什么。
- **凭据类数据的安全保存与上传同步一期**（#90）：密码、密钥、证书、令牌**存在哪儿**、谁能改变它的保管状态、上传时谁持有密钥。
- **网络通信的机密性与完整性保护一期**（#91）：数据**在路上**时，机密性与完整性分别由链路层、传输层、逐条消息加密、上传前封装哪一层提供。
- **本篇（#92）**：把视角换成**配置与责任**——一个 App 的连接要求由谁写入、哪个键放松哪一条、代码里的 TLS 参数能设什么上下界、组织和用户从哪里插进来、API 选型如何把责任从系统转移到开发者。
- 后续两期（内存布局随机化、内存页面执行限制）属于抗利用强度，与本篇只在"诊断数据上传时携带什么"这一点上相接。

一句话概括本篇的立场：**上一期讲"路上有什么保护"，这一期讲"保护是谁配置的、他是否知道自己配置了"。**

# 1. 总览：一条 App 连接从发起到断开经过哪些要求

## 1.1 目标—配置项—生效主体对照表

下表是全篇的索引。左列是"要达成的一条要求"，中列是"实现这条要求的具体配置项或机制"，右列是**谁写入、谁能改**。凡右列写"App"的，都是可以在代码评审与配置审计里直接核对的项；写"系统"或"环境"的，App 只能观测不能决定。

| 要求 | 机制／配置项 | 写入者 | 可放松？ | 可观测位置 | 章节 |
|---|---|---|---|---|---|
| 连接必须用 TLS | ATS 基线：TLS ≥ 1.2 | 系统（按链接 SDK 版本） | 是（`NSExceptionMinimumTLSVersion`，需审核理由） | 控制台 ATS 阻塞消息；`nscurl --ats-diagnostics` | §2.1、§3.8 |
| 服务器身份必须可信 | 默认信任评估四条（签名完整／未过期／名称匹配／链到受信任锚） | 系统 | **否**（App 侧不能放松；锚来源只有系统内置或用户与管理员安装两种） | 失败错误码（如 `NSURLErrorDomain Code=-1202`） | §5.1、§5.4 |
| 证书强度达标 | RSA ≥ 2048 或 EC ≥ 256；SHA-256 及以上 | 系统 + 服务器运营方 | 是（同上例外键） | 服务器证书本身 | §2.2 |
| 密钥交换不批量暴露 | 必须支持 ECDHE 前向保密 | 系统 + 服务器 | 是（`NSExceptionRequiresForwardSecrecy`，默认 YES） | 握手参数 | §3.10、§2.2 |
| 对称加密不落后 | 数据交换必须用 AES-128 或 AES-256 | 系统 + 服务器 | 部分（例外键不覆盖"密码套件白名单"这一条） | 握手协商结果 | §2.2 |
| 明文 HTTP 不得出现 | ATS 默认拒绝 http | 系统 | 是（`NSAllowsArbitraryLoads`、`NSExceptionAllowsInsecureHTTPLoads`、媒体与 Web 内容两条专用键） | 控制台消息逐字可见 | §3.2–§3.5、§4.4 |
| 更严的合规下限 | NIAP 合规模式 `NSRequiresNIAPTLSPackageVersion`（`none`／`FCP_v2.1`／`recommended`） | App（**收紧方向，无需审核理由**） | 不适用 | `nscurl` 会测 NIAP 相关组合 | §3.6、§7 |
| 证书可追溯 | 证书透明度（CT）要求 | 系统 + Apple 信任根签发线 | 组织侧存在官方承认的放宽路径 | 连接是否被放行 | §5.6、§10.3 |
| 不信任公共 CA | 声明式固定 `NSPinnedDomains`（两类键） | App | **不可放松，只能加严** | 固定失败即连接失败 | §6.1–§6.3 |
| 客户端 TLS 参数自主 | Network.framework 的 TLS 选项对象（版本上下界、SNI、ALPN、额外根、OCSP 阻断、PSK、量子安全开关、自定义评估回调） | App（**此时 ATS 不生效**） | 全部由 App 决定 | `NWParameters` 与协商结果字段 | §7 |
| 会话级强制 | `URLSession` 侧的 TLS 1.3 要求、凭据与客户端证书、重试与服务类型 | App | 只加严 | 会话配置与错误回调 | §8 |
| 不随意访问局域网 | 本地网络权限 + 用途描述键 + 服务声明键 | 系统（用户授权） | 否（拒绝即不可访问） | 授权弹窗与设置开关状态 | §9 |
| 上传前不泄露明文 | 上传前加密 + 密钥走端对端通道；CloudKit 记录密钥先在受信任设备生成并封装 | App / 系统（U2 由 Apple 体系决定） | U2 不取决于 App 配置 | 数据库类型与服务密钥档位 | §11.1–§11.5 |
| 计费网络与蜂窝许可 | 三项访问开关与后台任务的裁量标志 | App 声明 + 用户设置 | 是 | 连接被推迟或失败 | §8.7 |
| 组织可插层检查 | 代理、内容过滤器、逐 App VPN | 组织（配置描述文件与 MDM） | App 无从拒绝 | 证书链异常、连接被重加密迹象 | §6.5、§10.3 |

## 1.2 一次 URL 请求的判定顺序

把上表按时间轴排一次，就得到本篇最重要的心智模型。**顺序本身就是结论**，因为例外只作用于后半段：

1. **链接期决定基线是否启用**。针对 iOS 9.0 / macOS 10.11 SDK 或更高版本链接的 App 默认启用 ATS；一手 NOTE 逐字写着：链接早于这两个 SDK 的 App，**无论运行在哪个操作系统版本上，ATS 都不启用**。这一步已经发生，用户与开发者都无法在运行时补救。
2. **调用路径决定谁来负责**。走 URL Loading System（`URLSession`）则系统强制 ATS；改走 `Network` framework 或 `CFNetwork` 直调则 ATS **不适用**，安全责任整体转移到 App。
3. **确定目标域是否被 ATS 保护**。`NSExceptionDomains` 的逐域设置、`NSIncludesSubdomains` 的子域继承、以及媒体／Web 内容／本地网络三类专用键共同决定这一条连接落在"保护"还是"例外"里。
4. **默认服务端信任评估先行**。四条检查：数字签名完整、未过期、名称与服务器 DNS 匹配、链到受信任锚。此处失败是硬故障，错误先于任何 ATS 检查出现。
5. **ATS 扩展检查随后叠加**。TLS 版本下限、证书密钥长度与摘要算法、AES-128/256、ECDHE 前向保密；若 App 启用了 NIAP 合规模式，这里再叠一层更严的客户端行为要求。
6. **App 自己的加严项再叠加**。声明式固定的两类匹配、代码里的额外信任根、TLS 版本上下界、会话级强制、OCSP 阻断策略。这些只能加严，不能放松前面已通过的判定。
7. **握手与协商得到实际结果**。策略不等于结果：客户端"要求"与服务器"能提供"的交集才是这条连接真实的强度；系统仅在连接支持的服务器时才会用上较新的能力（上一期已逐字引过该条限定）。
8. **断开时的会话残留**。会话票据、预共享密钥、恢复上下文会在下一条连接里被复用——它们属于"上次判定结果的持久化"，评审时同样要看。

顺序里第 4 步与第 5 步的**不可交换性**，是 §3.12 误配清单与 §14.5 相反结论的共同来源：把例外写成"绕过全部检查"的开关，是错的。

## 1.3 这张表能回答什么、不能回答什么

能回答三类问题：**这条连接的要求由谁配置**（右列）、**开发者要改需要走哪个键**（中列）、**评审时能从哪里核对**（可观测位置列）。因此它适用于配置审计、代码评审、合规申报与故障定位。

不能回答四类问题，本篇在相应章节会明确标注缺口而不是给结论：

- **每个键在所有系统版本上的精确生效差异**。手册页与 developer 页对同一键的可用性描述不完全一致（例如证书透明度键在现行 developer 页已被标记为 obsolete，而手册页仍在描述 CT 连接要求），逐版本行为需要真机验证。
- **实现层的判据细节**：ATS 在什么时刻读 `Info.plist`、例外与固定冲突时哪条优先、`nscurl` 之外有没有别的可观测面——一手文档未写。
- **系统服务与 Apple 自身 App 的连接是否受同一套约束**：手册页口径是"互联网 App 会自动使用此协议"，但逐 App 范围不公开。
- **服务端日志侧的任何事实**：本表全部是设备侧视角，服务端如何记录、保留、转发不属于本篇范围，只在 §11 的"可见性"列里作为后果提及。

还有一条方法论提醒：**这张表里没有"强度数值"列**。TLS 版本与密钥长度是下限条件而非强度度量，AES-128 与 AES-256 在同一张基线表里被并列允许（一手原句就是"either the AES-128 or the AES-256 symmetric cipher"），把"AES-256"当作卖点写进安全说明并不构成高于基线的保证——除非它出现在 App 自己的加严项里。

## 1.4 怎么使用本篇

- **配置审计**：按 §3 的键目录逐项对照 `Info.plist`，再用 §13.3 的二十二条自查清单收尾；每个放松键都要能回答"理由是什么、审核时怎么写的"（§4.1）。
- **代码评审**：走 §2.5 确认调用路径，再按 §7／§8 对照 TLS 参数与是否被 ATS 覆盖；凡"用 Network.framework 直连生产服务"的代码，都要在评审记录里显式写明"ATS 不适用，责任在 App"。
- **故障定位**：先 §5.7 判断是默认评估失败还是扩展检查失败，再用 §1.7 提到的 `nscurl --ats-diagnostics` 逐组合验证。
- **研究与取证**：§14 的三张清单（口径冲突、证据缺口、与流行口径相反）是本篇唯一允许"直接引用结论"的部分，其余章节引结论时必须连带引文。

# 2. 默认基线：ATS 是编译期继承来的要求

## 2.1 ATS 自动应用的判据是编译目标版本而非运行系统版本

ATS 总纲页给出的定义与生效条件，逐字如下（developer 文档，截至 2026 可用）：

> On Apple platforms, a networking security feature called App Transport Security (ATS) improves privacy and data integrity for all apps and app extensions. It does this by requiring that network connections made by your app are secured by the Transport Layer Security (TLS) protocol using reliable certificates and ciphers. ATS blocks connections that don't meet minimum security requirements.

> ATS operates by default for apps linked against the iOS 9.0 or macOS 10.11 SDKs or later. In cases where you need to connect to a server that isn't fully secure—and that you can't reconfigure to make more secure—you can add exceptions to loosen some of the ATS requirements.

页面紧接着用一条 NOTE 把"运行时补救"这条路堵死：

> If you link your app against an SDK older than iOS 9.0 or macOS 10.11, ATS is disabled no matter which version of the operating system your app runs on.

三条口径必须分开写，因为它们在工程讨论里长期被混为一谈：

1. **判据是"linked against … SDKs"**，即链接时使用的 SDK 版本。`NSAppTransportSecurity` 配置字典页的 Versioning 段用的是同一措辞（"apps linked against the iOS 9.0 or macOS 10.11 SDKs"），两处独立一致，可以当作硬事实引用。
2. **不是运行系统的版本**。一台 iOS 26 的设备上跑一个用 iOS 8 SDK 链接的老二进制，ATS 不启用——这是上一句 NOTE 的字面含义，也是本系列里"设备安全 ≠ 连接安全"最干净的一个证据。
3. **也不是 App 的部署目标（deployment target）**。部署目标决定可调用 API 范围与运行下限，不决定基线是否启用。把两者混写会让审计结论失效：一份"最低支持 iOS 12"的说明并不能证明 ATS 生效。

手册页侧的对应措辞是"App 传输安全性会自动应用到针对 iOS 9、iPadOS 13.1、OS X 10.11、Apple tvOS 9、watchOS 2、visionOS 1 或更高版本编译的 App"，用了"编译"一词并列了六个平台。两处都指向**构建期**，只是手册页把平台清单铺得更全（含 iPadOS、Apple tvOS、watchOS、visionOS），developer 页只提 iOS 与 macOS。写正文时两条都引，并说明 developer 页未铺全平台清单。

最后一条来自定义句本身：ATS 的作用方式是 **"requiring … secured by TLS … using reliable certificates and ciphers"** + **"blocks connections that don't meet minimum security requirements"**。也就是说 ATS 是一个**准入式（deny-by-default）策略**而不是建议式最佳实践：不满足下限就断开，而不是连上之后给个警告。这条性质在 §14.5 会被用来反驳"ATS 只是提示"的流传说法。

## 2.2 ATS 在默认信任评估之上叠加的检查项

一手页面把要求分成两层，顺序明确：先"默认服务端信任评估"（四条），再"ATS 扩展检查"（五条）。逐字如下：

> A secure server establishes its identity using an X.509 digital certificate. A connecting client examines this certificate to perform default server trust evaluation, which includes checking that the certificate:
> - Has an intact digital signature, showing that the certificate hasn't been tampered with.
> - Isn't expired.
> - Has a name that matches the server's DNS name.
> - Is signed by another valid certificate, which is in turned signed by another, and so on back to a trusted anchor certificate, which must be issued by a Certificate Authority (CA). The anchor certificate must be part of the client operating system, as indicated in `Lists of available trusted root certificates in iOS`, or be installed on the client by the user or a system administrator.

（`in turned` 为页面原文拼写错误，本篇按原样照抄。这一条其实很有用：它说明**锚证书只有两个来源**——系统内置，或由用户或系统管理员安装；App 自己想加锚只能走"收紧"路径，见 §5.3。）

> ATS requires all of these things, and then provides extended security checks:
> - The server certificate must be signed with either a Rivest-Shamir-Adleman (RSA) key of at least 2048 bits, or an Elliptic-Curve Cryptography (ECC) key of at least 256 bits.
> - The certificate must use the Secure Hash Algorithm 2 (SHA-2) with a digest length, sometimes called a *fingerprint*, of at least 256 bits (that is, SHA-256 or greater).
> - The connection must use Transport Layer Security (TLS) protocol version 1.2 or later.
> - Data must be exchanged using either the AES-128 or the AES-256 symmetric cipher.
> - The link must support perfect forward secrecy (PFS) through Elliptic Curve Diffie-Hellman Ephemeral (ECDHE) key exchange.

量化基线（本篇可直接引用、无需推断）：

| 项 | ATS 基线要求 | 性质 |
|---|---|---|
| TLS 版本 | **1.2 或更高** | 下限条件，非推荐值 |
| 服务器证书密钥 | RSA ≥ 2048 位 **或** EC ≥ 256 位 | 下限条件 |
| 摘要／指纹 | SHA-2 且摘要长度 ≥ 256 位（即 SHA-256 或更强） | 下限条件 |
| 对称加密 | AES-128 **或** AES-256 | 二者并列合规，AES-256 不构成额外保证 |
| 密钥交换 | 必须支持前向保密，且指明通过 **ECDHE** | 硬性；可用 `NSExceptionRequiresForwardSecrecy` 放松（默认 YES） |
| 证书有效性 | 签名完整、未过期、名称匹配 DNS、链到受信任锚 | 属于**默认评估**层，例外放松不了 |

三条解读：

- **"支持"与"要求"不是一回事**。手册页的 TLS 安全性段落列出系统支持 TLS 1.0、1.1、1.2、1.3 与 DTLS；ATS 这里只允许 1.2 或更高。所以"这台设备能讲 TLS 1.0"与"这个 App 被允许用 TLS 1.0"是两个独立事实，后者才是本篇关心的要求。唯一的例外通道是 `NSExceptionMinimumTLSVersion`，而它在**需要审核理由的五个键**清单里（§3.8、§4.1）。
- **对称加密这一条没有例外通道**。域级例外键覆盖"是否允许明文加载""最低 TLS 版本""是否要求前向保密"，并不同时把"AES-128/256"这条豁免掉；因此把"我们允许 RC4"当作 ATS 例外能做到的事，是误读（对照上一期引过的手册页历史禁用线：RC4 在 iOS 10 / macOS 10.12 起即未予使用）。
- **前向保密的措辞是"支持……通过 ECDHE"**，即一手文本直接把 PFS 与 ECDHE 绑定，而不是泛泛写"支持 PFS"。写正文时保留这个具体度。

## 2.3 硬故障语义：无效证书没有降级路径

ATS 扩展检查之上还有默认评估，而默认评估的失败**发生在 ATS 之前**。这条顺序关系的一手证据来自诊断页，逐字：

> If globally allowing arbitrary loads fails, the problem isn't related to ATS. For example, if the server's certificate doesn't match the DNS name of the server, the connection fails default server trust evaluation before ATS has a chance to impose its extended security checks:

手册页侧的对应句子（上一期已逐字取证）是"无效的证书始终导致硬故障和无连接"。两处合起来可以得到本篇最实用的判断规则：

**规则：连接被拒时，先分清是"评估失败"还是"策略失败"。**
- 评估失败的典型痕迹是证书类错误码，一手样例里出现 `NSURLErrorDomain Code=-1202`（服务器证书不被信任一类）。此时**加任何 ATS 例外都不会改变结果**。
- 策略失败的典型痕迹是控制台的 ATS 阻塞消息，逐字如下：

```
App Transport Security has blocked a cleartext HTTP (http://) resource
load since it is insecure. Temporary exceptions can be configured via
your app's Info.plist file.
```

这条消息有两个常被忽略的信息量：其一，它明确说被阻塞的是**明文 HTTP 资源加载**（cleartext），即 ATS 拒绝的对象是"根本没加密"这一类，而不是"加密得不够新"这一类（后者是握手版本问题）；其二，它把例外称为 **"Temporary exceptions"**——官方措辞自己就把例外定位成过渡手段，而不是长期架构。这句逐字引文在 §4.2 与 §14.5 会被用来反驳"ATS 例外是常规配置项"的实践。

排查工具是 `nscurl`，一手原文：

> The `nscurl` command accepts the `--ats-diagnostics` flag that asks it to check how a particular server responds to combinations of ATS exceptions.

```
$ /usr/bin/nscurl --ats-diagnostics --verbose https://example.com
```

> In addition to running with no exceptions at all, the tool tests the global exception keys in `NSAppTransportSecurity`, as well as a variety of combinations of the exception domain keys `NSExceptionMinimumTLSVersion`, `NSExceptionRequiresForwardSecrecy`, `NSExceptionAllowsInsecureHTTPLoads`, and `NSExceptionRequiresNIAPTLSPackageVersion`.

注意工具本身给出的键清单：它测的是**域级**四个键 + 全局例外键，不测固定、不测代码里的 TLS 参数、也不测本地网络权限。因此"`nscurl` 通过"不等于"这条链路在生产环境满足要求"——尤其当 App 走 Network.framework 时，`nscurl` 的结论与 App 的行为无关（§2.5、§7.10）。

## 2.4 基线与"能力清单"的差：为什么不能拿系统支持表当合规表

同一个平台上，"系统能协商什么"与"ATS 允许什么"存在三处系统性差异，逐条都有依据：

1. **协议版本**。系统支持 TLS 1.0/1.1（手册页清单），ATS 要求 1.2 或更高。 older 版本只能通过例外进入，而例外需要审核理由。
2. **API 层面的能力分裂**。上一期已逐字取证：针对 Network.framework 与 `URLSession`，较新系统默认启用 TLS 1.3；而"使用 SecureTransport API 的 TLS 客户端不能使用 TLS 1.3"。也就是说**同一个 App 里两条不同代码路径的连接可以落在不同协议代际上**，而 ATS 只管其中一条（§2.5）。
3. **较新能力的启用取决于对端**。量子安全 TLS（X25519MLKEM768）在 iOS 26 / iPadOS 26 / macOS 26 起对 `URLSession` 与 `Network` 默认启用，但手册页紧跟着写 **"系统仅在连接支持的服务器时支持量子加密"**。因此"我们的 App 已在新系统上运行"不构成"我们的上传链路具备后量子安全"的结论——这一点在 §14.5 单列。

这三条合起来给出本篇对"合规声明"的核心态度：**任何"我们全程 TLS 加密"的表述都必须补三样东西——哪条 API 路径、哪个版本下限、对端服务器是否支持**。缺任一项，就属于 §14.6 里"必须加限定语"的表述。

顺带一条常被引错的句子。手册页写 ATS "默认情况下……将密码选择范围限制为仅包括提供正向保密的密码套件"，并列出 GCM 与 CBC 两种模式；developer 页则把 PFS 具体要求写成 ECDHE。两者不矛盾（GCM/CBC 是分组密码工作模式，ECDHE 是密钥交换），但二手文章常把它们揉成"ATS 只允许 AES-GCM"，这是错的：**CBC 模式在官方清单里存在**。

## 2.5 ATS 不适用的调用路径

本节是全篇的责任转移起点，一手原句逐字：

> ATS doesn't apply to calls your app makes to lower-level networking interfaces like the `Network` framework or `CFNetwork`. In these cases, you take responsibility for ensuring the security of the connection. You can construct a secure connection this way, but mistakes are both easy to make and costly. It's typically safest to rely on the `URL Loading System` instead.

而强制侧的表述是：

> The system enforces ATS when you use the standard `URL Loading System`. Instances of `URLSession` automatically negotiate the most secure connection available from the server. The only action your app must take is to use secure URLs, like those beginning with `https`. Otherwise, ATS denies the connection and prints a console message.

把两句并起来，可以得到五条工程结论：

1. **责任边界由调用路径决定，不由系统设置决定。** 只要连接是经 `URLSession` 建立的，基线由系统写入；换成 `Network` framework 或 `CFNetwork` 直调，`Info.plist` 里整棵 `NSAppTransportSecurity` 树都不再作用于该连接。
2. **"most secure connection available from the server" 是协商语义**，不是"强制最高"。客户端提供集合与服务器支持集合的交集决定实际强度；服务器只支持较弱套件时，ATS 保证的是"不低于下限"，不是"用上最好"。
3. **App 唯一必须做的事是"用 https 开头的 URL"**。这句一手表述值得原样引用，因为它把"URL  scheme 写错"列成了 ATS 失效的常见原因之一，而且它意味着：如果 App 自己做 scheme 替换（例如把 `https` 改写为 `http` 走本地代理），保护就从这条路径上消失了。
4. **"mistakes are both easy to make and costly" 是官方风险声明**。评审时凡出现绕过 URL Loading System 的自管连接，应当要求作者书面说明如何覆盖版本下限、信任评估、SNI、撤销与超时——ATS 免费给的那些判定，此时都得自己实现。
5. **手册页与 developer 页口径不同要注明**。手册页 ATS 段写的是"App 在使用 NSURLConnection、CFURL 或 NSURLSession API 时会遵守安全连接的最佳实践"，未提 CFNetwork 与 Network framework 的排除关系；现行 developer 页则显式排除。本篇在 §14.1 把这条列为一处口径冲突，而不是挑一条当作唯一真相。

一张"谁负责"的分配表：

| 调用路径 | ATS 是否作用 | 版本下限由谁决定 | 信任评估由谁执行 | 固定/额外加严由谁写 |
|---|---|---|---|---|
| `URLSession`（URL Loading System） | 是 | 系统（ATS 基线 + 例外键） | 系统自动，App 只能加严 | `NSPinnedDomains` 或代码回调 |
| `NSURLConnection` / `CFURL`（手册页口径） | 手册页称遵守 ATS 最佳实践；现行 developer 页只提 URL Loading System | 同上，但口径需注明 | 系统 | 同上 |
| `CFNetwork` 直调 | **不适用**（developer 页原句） | App | App | App |
| `Network` framework | **不适用**（developer 页原句） | `NWParameters` 的 TLS 选项 | App 自定义或默认 | App |
| BSD Sockets 之上自搭 TLS | 不适用 | App | App | App |
| 系统提供的邮件／Safari 等互联网 App | 手册页口径：自动使用 TLS 建立加密通道 | 系统 | 系统 | 用户安装的证书可影响锚（§5.4） |

最后一行不是 App 开发者能配的范围，但它解释了"为什么用户装了一个根证书之后，某些原本失败的连接开始成功"这一现象（§5.4、§10.2）。

## 2.6 为什么"用系统默认"本身就是一项需要审计的配置

一个反直觉但成立的结论：**没有写任何 ATS 配置，不等于没有做安全决策**。因为默认值同时决定了四件事，而每一件都可以被别的东西改变：

- **默认是否启用**取决于链接时 SDK（§2.1）。一个为了兼容老库而降级 SDK 构建的二进制，会在用户毫无察觉时失去整条基线。审计方法是检查产物的链接版本，而不是问开发者"你们开了 ATS 吗"。
- **默认信任谁**取决于系统内置锚清单与"用户或系统管理员安装"这两条路（§2.2 第四条）。后者意味着运行环境可以改变信任结果，而 App 默认不拒绝这一改变（§5.4）。
- **默认允许什么套件**取决于系统对 ATS 的实现（§2.2 五条）。这些会随系统版本演进——上一期记录的"较新系统对 `URLSession` 与 Network 默认启用 TLS 1.3 并默认启用后量子密钥交换"就是默认值被系统改写的实例。
- **默认走哪条 API** 取决于代码结构。一次把 `URLSession` 换成 `Network` framework 的性能优化，就把责任从系统转移到 App（§2.5），而配置文件完全不变。

因此本篇建议把"网络传输安全默认值"当作一项有版本、有变更人、有回归风险的配置来登记，评审记录里至少要留下四栏：链接 SDK 版本、调用路径、是否声明例外、是否自行加严。缺任何一栏，"我们用的是系统默认"这句话不可核对（见 §13.4）。

# 3. ATS 配置键全目录

本章逐个键给出官方抽象、默认值、逐字描述与生效前提，并在 §3.11 汇总键与键之间的覆盖关系。**顺序按现行一手结构图，不按二手教程。**

## 3.1 键的落点与整体结构

第一条要纠正的常见误解是"键的位置"：**ATS 全部键的现行一手页在 `Bundle Resources / Information Property List` 命名空间下，不在 Security 框架文档里**。URL 有两种形态，混用会 404：

- 字典与容器类键：`…/bundleresources/information-property-list/nsapptransportsecurity/<key>`（`NSAllowsArbitraryLoads`、`NSAllowsArbitraryLoadsForMedia`、`NSAllowsArbitraryLoadsInWebContent`、`NSAllowsLocalNetworking`、`NSExceptionDomains`、`NSPinnedDomains`）
- 域级与顶层叶子键：`…/bundleresources/information-property-list/<key>`（`NSExceptionAllowsInsecureHTTPLoads`、`NSExceptionMinimumTLSVersion`、`NSExceptionRequiresForwardSecrecy`、`NSIncludesSubdomains`、`NSRequiresCertificateTransparency`、`NSRequiresNIAPTLSPackageVersion`、`NSExceptionRequiresNIAPTLSPackageVersion`）

容器键自身的官方描述（逐字）：

> **NSAppTransportSecurity** — A description of changes made to the default security for HTTP connections.

页面上的 `ideTitle` 是 *App Transport Security Settings*，值为 `dictionary`。正文段（逐字）：

> You can circumvent or augment these protections by adding the `NSAppTransportSecurity` key to your app's `Information Property List` file and providing an ATS configuration dictionary as the value.

>>> [IMPORTANT]
> Always look for ways to improve server security before adding ATS exceptions. Loosening ATS restrictions reduces the security of your app.

> All keys in the ATS configuration dictionary are optional, with default values that are suitable for most apps. Keys that define global exceptions apply to all network connections made by your app, except connections to domains specified in the `NSExceptionDomains` sub-dictionary. That sub-dictionary allows you to separately manage settings for individual domains.

一句话概括这段的三条信息：**每个键都是可选的**（不写就是默认值，而默认值即基线）；**全局键的作用域是"除例外域之外的全部连接"**；官方在 IMPORTANT 框里把"先改服务器、再考虑例外"写成顺序要求。

**Versioning 段**（逐字，全文）是本章最容易被跳过、却最能解释"为什么同一份 plist 在不同系统上行为不同"的一段：

> ATS operates by default for apps linked against the iOS 9.0 or macOS 10.11 SDKs or later. When you link your app against an older SDK, ATS is disabled no matter which version of operating system your app runs on.
> If you specify a value for any of the global exceptions besides `NSAllowsArbitraryLoads`, then the ATS behavior depends on the version of the OS on which your app runs:
> **iOS 9.0 or macOS 10.11**: ATS uses the `NSAllowsArbitraryLoads` value that you set, or NO by default, and ignores the other global exceptions.
> **iOS 10.0 or later or macOS 10.12 or later**: ATS ignores the `NSAllowsArbitraryLoads` value that you set and instead obeys the other key or keys.
> This behavior enables you to manage differences between OS versions. You provide a coarse exception (`NSAllowsArbitraryLoads`) for older versions, and a more targeted exception, like `NSAllowsArbitraryLoadsInWebContent`, for when it's available.

最后一段的官方字典结构（逐字代码块，出自 ATS 总纲页）：

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

**这张结构图本身就是取证证据**：图中**没有** `NSThirdPartyException*` 一族，也**没有** `NSExceptionForwardSecrecyCipherSuitesOrder`（§3.9、§3.10），而 `NSPinnedDomains` 分支只出现在它自己的键页里、不在这张总结构图里（§6.1）。审计时应当以"现行结构图 + 各键页"为准，而不是以教程文章列出的键表为准。

## 3.2 全局宽松键一：允许任意加载

官方抽象与默认值（逐字）：

> **NSAllowsArbitraryLoads** — A Boolean value indicating whether App Transport Security restrictions are disabled for all network connections.

页面属性段的默认值为 `NO`，`ideTitle` 为 *Allow Arbitrary Loads*，类型 boolean。正文（逐字）：

> Set this key's value to `YES` to disable App Transport Security (ATS) restrictions for all domains not specified in the `NSExceptionDomains` dictionary. Domains you specify in that dictionary aren't affected by this key's value.

>>> [IMPORTANT]
> You must supply a justification during App Store review if you set the key's value to `YES`, as described in `Provide Justification for Exceptions`. Use this key with caution because it significantly reduces the security of your app. In most cases, it's better to upgrade your servers to meet the requirements imposed by ATS, or at least to use a narrower exception.

> Disabling ATS means that unsecured HTTP connections are allowed. HTTPS connections are also allowed, and are still subject to default server trust evaluation, as described in `Ensure the Network Server Meets Minimum Requirements`. However, extended security checks—like requiring a minimum Transport Layer Security (TLS) protocol version—are disabled. Without ATS, you're also free to loosen the default server trust requirements, as described in `Performing manual server trust authentication`.

这一段是 §2.3 与 §5.3 的机制支点，三个不可省略的限定：

1. **关掉 ATS 之后，HTTPS 连接仍然受默认服务端信任评估约束**——"still subject to default server trust evaluation"。因此"我们关掉了 ATS 所以怎么连都行"是错的：证书签名、有效期、名称匹配、链到受信任锚这四条依旧生效，无效证书仍然失败。
2. **被关闭的是"扩展检查"**（版本下限、密钥长度与摘要、前向保密等），这正是 §2.2 的第二组五条。
3. **"Without ATS, you're also free to loosen the default server trust requirements"** 是 §1.4 那句"只能收紧不能放松"的**条件反证**：只有在该域不被 ATS 保护时，手动评估才可以放松。两句必须成对引用，单引任何一句都会得出错误结论。

还有一条优先级事实（一手，逐字）：

> In iOS 10 and later and macOS 10.12 and later, the value of the `NSAllowsArbitraryLoads` key is ignored—and the default value of `NO` used instead—if any of the following keys are present in your app's `Information Property List` file:

页面在此处接一个无序列表，本简报取到的 JSON 中该列表文本与正文分离，**未逐字取全**（据 Versioning 段与 §3.3／§3.4／§3.5 各页的条件句可交叉印证为另外三个全局键）。因此正文只写成：**在 iOS 10／macOS 10.12 及以后，写了更细的全局键会让 `NSAllowsArbitraryLoads` 被忽略而回落默认 `NO`**，具体清单以页面渲染为准（列在 §14.2 缺口里）。

## 3.3 全局宽松键二：Web 内容专用

> **NSAllowsArbitraryLoadsInWebContent** — A Boolean value indicating whether all App Transport Security restrictions are disabled for requests made from web views.

> Set this key's value to `YES` to exempt your app's `WKWebView` instances from App Transport Security restrictions without affecting your `URLSession` connections. Domains you specify in the `NSExceptionDomains` dictionary aren't affected by this key's value.

> In iOS 10 and later and in macOS 10.12 and later, if you include this key with any value, then App Transport Security ignores the value of the `NSAllowsArbitraryLoads` key, instead using that key's default value of `NO`.

三条口径：

- **"只对 `WKWebView` 内容生效、不影响 `URLSession` 连接"是逐字原文**。这意味着同一个 App 里"网页内容的传输要求"与"自己发出去的请求（包括上传）的传输要求"**可以分道配置**。写安全说明时把两者混为一句"我们对 WebView 放宽了 ATS"，会让人误以为上传也被放宽——这是 §14.5 收录的一条流行误解。
- **"with any value"值得注意**：只要写了这个键（哪怕值是 `NO`），`NSAllowsArbitraryLoads` 就被忽略。因此"我写了键但设为 NO 应该没影响"的想法会改变整体配置语义。
- 本键在 §1.5 的"需要审核理由"清单内（清单里写作 *Web content loads*）。它的默认值本简报未从属性段逐字取到，正文不写具体默认值（§14.2）。

## 3.4 全局宽松键三：媒体加载专用

> **NSAllowsArbitraryLoadsForMedia** — A Boolean value indicating whether all App Transport Security restrictions are disabled for requests made using the AV Foundation framework.

> Set this key's value to `YES` to disable App Transport Security restrictions for media loaded using the `AVFoundation` framework, without affecting your `URLSession` connections. Domains you specify in the `NSExceptionDomains` dictionary aren't affected by this key's value.

> Employ this key only for loading encrypted media—like files protected by FairPlay or by secure `HTTP Live Streaming`—that don't contain personalized information.

> In iOS 10 and later and in macOS 10.12 and later, if you include this key with any value, then App Transport Security ignores the value of the `NSAllowsArbitraryLoads` key, instead using that key's default value of `NO`.

官方**限定用途**句非常具体：只应用于加载"受 FairPlay 或安全 HLS 保护的加密媒体、且不含个性化信息"。这条限定是审核理由讨论的基础（§4.1），也是本篇判断"这个例外是否越界"的依据：**媒体键不是上传例外**——它作用于 AVFoundation 的媒体加载，与 `URLSession` 的 `uploadTask` 无关。把它写进"我们的上传通道需要放宽"的配置里，属于误用。

## 3.5 全局宽松键四：本地网络

> **NSAllowsLocalNetworking** — A Boolean value that indicates whether to allow local resources to load.

> The `NSAllowsLocalNetworking` key controls whether App Transport Security (ATS) allows your app to connect to unqualified domains, `.local` domains, and IP addresses using IPv4 or IPv6.

> In iOS 9 and macOS 10.11, ATS disallows connections to all three domain types. You can add exceptions for unqualified domains and `.local` domains in the `NSExceptionDomains` dictionary, but you can't add IP addresses. Instead you use `NSAllowsArbitraryLoads` when you want to load directly from an IP address.

> In iOS 10 through iOS 16, iPadOS 13.1 through iPadOS 16, and macOS 10.12 through macOS 13, ATS allows all three of these connections by default, so you no longer need an exception for any of them. However, if you need to maintain compatibility with older versions of the OS, set both of the `NSAllowsArbitraryLoads` and `NSAllowsLocalNetworking` keys to `YES`.

> In iOS 17, iPadOS 17, and macOS 14, ATS no longer allows connections to IP addresses by default. Add individual IP addresses and classless inter-domain routing (CIDR) ranges in the `NSExceptionDomains` dictionary.

> The local networking exception tells newer versions of the OS to ignore the arbitrary loads key, and enable access to unqualified domains, `.local` domains, and IP addresses that they would otherwise restrict. Meanwhile, the arbitrary loads key tells older versions of the OS, which don't process the local networking exception key, to bypass ATS completely. This allows your app to work on different OS versions while minimizing the use of the wider exception.

量化基线（三个版本窗口，一手）：

| 系统窗口 | 无限定域名 | `.local` 域 | IP 地址（IPv4／IPv6） |
|---|---|---|---|
| iOS 9 ／ macOS 10.11 | 默认禁止，可用例外域 | 默认禁止，可用例外域 | 默认禁止，且**不能写进例外域**，只能靠 `NSAllowsArbitraryLoads` |
| iOS 10–16 ／ iPadOS 13.1–16 ／ macOS 10.12–13 | 默认允许 | 默认允许 | 默认允许 |
| **iOS 17 ／ iPadOS 17 ／ macOS 14 起** | 默认允许 | 默认允许 | **默认不再允许**，须把单个 IP 或 CIDR 段写进 `NSExceptionDomains` |

最后一段"两个键跨版本协作"的表述是官方给出的**最小化例外**设计模式：细粒度键给新系统用、粗粒度键给不认识细粒度键的老系统兜底，"while minimizing the use of the wider exception"。这条模式在 §4.2 被提炼成配置策略，同时也是 §13.5 回归监测项（**iOS 17 这一改动会静默打断所有依赖 IP 直连的设备类 App**：局域网打印机、摄像头、本地网关、智能家居桥接）。

必须与 §9 区分：**本键管的是"要不要 TLS/加密要求"，本地网络权限管的是"允不允许访问局域网"**，两条正交。只配本键不解决授权问题，只给授权不解决明文 HTTP 被 ATS 拒绝的问题。

## 3.6 全局收紧键：NIAP 合规模式（与已废弃的证书透明度键）

ATS 唯一明确的"再加一层"开关是 NIAP 合规模式。一手描述（逐字）：

> ATS provides support to further restrict default TLS client behavior to help meet requirements outlined by the National Information Assurance Partnership (NIAP) in the Functional Package for Transport Layer Security. This compliance mode is opt-in only and provides additional options for regulated environments. To opt-in to this mode, use the `NSRequiresNIAPTLSPackageVersion` key, which can be set to enforce this mode globally, and the `NSExceptionRequiresNIAPTLSPackageVersion` key to configure exceptions to the global policy on a per-domain basis.

取值（逐字，页面的 termList）：

> **`none`**: Apply no additional requirements. This value results in the default ATS behavior.
> **`FCP_v2.1`**: Make your default client connections enforce additional requirements (described below) as specified in the `Functional Package for Transport Layer Security (TLS) Version 2.1`.
> **`recommended`**: Use the recommended version of the NIAP functional package for the operating system version your app is running on. Currently, this is equivalent to `FCP_v2.1`.

`FCP_v2.1` 在标准 ATS 之上追加的要求（逐字）：

> The `FCP_v2.1` value adds the following requirements on top of standard ATS:
> - The client offers the same TLS 1.3 ciphersuites as the default ATS policy. For TLS 1.2, the client offers only perfect forward secret (using ECDHE), AES-GCM with a SHA-256 or greater pseudorandom function (PRF).
> - The client no longer offers the signature algorithm `rsa_pkcs15_sha1`, as specified in `FCS_TLSC_EXT.1.4`.
> - The server's certificate signature must use a signature algorithm offered in the client hello, as defined in `RFC 8446 Section 4.2.3` and `RFC 5246 Section 7.4.2`.
> - TLS 1.2 connections must negotiate the Extended Master Secret extension, as specified in `RFC 7627`.

（页面第三条原文中 "signature" 与 "must" 之间有双空格，本篇按渲染版还原；引原文时以页面为准。）

两个关键口径：

1. **"opt-in only"** —— NIAP 模式默认不开。任何"我们的 App 满足 NIAP TLS 功能包"的声明都必须以 plist 中实际存在该键且取值为 `FCP_v2.1` 或 `recommended` 为前提。`recommended` 与 `FCP_v2.1` **当前等价**（一手原文 "Currently, this is equivalent to"），但"当前"是官方限定语，不能写成恒等。
2. **这两个键不需要审核理由**，理由是"因为它们加强而非削弱要求"（§1.5 逐字）。这是"放松方向需申报、收紧方向自由"最干净的一手证据，也是本篇"放松／收紧不对称"论点（§4.2、§14.5）的支点之一。

顺带把**已废弃**的证书透明度键放在这里对照，因为它常被误当作"全局收紧开关"：

> **NSRequiresCertificateTransparency** — **An obsolete Boolean value** indicating whether to require Certificate Transparency.

> The system now requires this behavior in all cases, making the key irrelevant.

>>> [WARNING]
> Don't use this key. The current system enforces this behavior, and new certificates can't meet the requirement on older systems.

它的历史语义（逐字）给出了一个量化基线：**CT 要求域服务器证书具备"至少两个 Apple 信任的 CT 日志"签发的 CT 时间戳**（并指向 RFC 6962）。现行状态是**系统强制、键无效果、官方明令不要用**。因此："我们配置了 CT 要求"这句话在今天的 iOS 上不构成任何额外承诺——CT 是系统在做的；而在老系统上，新证书反而可能**无法满足**该要求（WARNING 框第三句），这是一个真实的兼容性问题，属于 §13.5 的回归项。

## 3.7 例外域字典与子域包含

> **NSExceptionDomains** — Custom App Transport Security (ATS) configurations for named domains.

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

域名书写规则（逐字，五条）：

> - **Use lowercase** — Use `example.com`, not `EXAMPLE.COM`.
> - **Use a DNS domain name, IP address, or range of IP addresses** — In iOS 17, iPadOS 17, and macOS 14, you can use an IPv4 address, for example `192.168.42.63`, or an IPv6 address, for example `2001:db8:12::34`. You can also use a classless inter-domain routing (CIDR) range, for example `2001:db8:12::/48`.
> - **Don't include a port number** — Use `example.com`, not `example.com:443`.
> - **Don't include a trailing dot, unless you only want to match a domain string with a trailing dot**
> - **Don't use wildcard domains** — Don't use `*.example.com`. Instead, use `example.com` and set `NSIncludesSubdomains` to `YES`.

两条 NOTE 是本章最容易写错的机制点，逐字：

> If you exclude a DNS domain name and your app contacts a host by IP address, the ATS exclusion for the domain name doesn't apply to the connection even if a DNS query for the domain name would resolve to the IP address. If you exclude an IP address and your app contacts a host by DNS name that resolves to that IP address, the ATS exclusion for the IP address doesn't apply to the connection.

> If you specify an exception domain dictionary, ATS ignores any global configuration keys, like `NSAllowsArbitraryLoads`, for that domain. **This is true even if you leave the domain-specific dictionary empty and rely entirely on its keys' default values.**

第一条的含义：**例外的匹配对象是"App 实际用来建连的标识形式"，不是解析后的主机**。同一台服务器，用域名连和用 IP 连，落在不同的例外判定上——这与 §9 的本地网络行为、与设备类 App 的常见实现（先发现 IP、再直连）叠加后，会产生"配置里明明有例外但连接仍被拒"的现象。第二条的含义：**写一个空的例外域字典本身就是"退出全局键作用域"的动作**，不是无害占位。

**NSIncludesSubdomains**（逐字，含默认值与"只展开一层"的语义）：

> A Boolean value that indicates whether to extend the configuration to subdomains of the given domain.

> Set the value for this key to `YES` to apply the configuration for the given domain to all subdomains of the domain that have one additional path component. For example, if you set this value to `YES` and the domain name string is `example.com`, then the configuration applies to `example.com`, as well as `math.example.com` and `history.example.com`. However, it doesn't apply to the subdomains `advanced.math.example.com` or `ancient.history.example.com` because those subdomains have two additional path components.

> You can include this key in any of the domain-specific dictionaries that you add to the `NSExceptionDomains` and `NSPinnedDomains` dictionaries.

> If you create an `NSExceptionDomains` dictionary for an IP address or a range of addresses, the `NSIncludesSubdomains` key has no effect for that exception.

默认值 `NO`（页面属性段逐字）。三个量化结论可直接进正文表格：**只展开一层路径分量**（多级子域不继承）；本键同时服务于例外字典与固定字典两处；**对 IP／CIDR 例外完全无效**。工程上最容易踩的是第一条——服务名习惯用 `api.prod.example.com` 这类两级名字，而配置只写了 `example.com` + `NSIncludesSubdomains=YES`，于是例外不覆盖。

## 3.8 例外域内的四个键

**（一）NSExceptionAllowsInsecureHTTPLoads**

> A Boolean value indicating whether to allow insecure HTTP loads. 默认值 `NO`。

> Set the value for this key to `YES` to allow insecure HTTP loads for the given domain, **or to be able to loosen the server trust evaluation requirements for HTTPS connections to the domain**, as described in `Performing manual server trust authentication`.

> Using this key doesn't by itself change default server trust evaluation requirements for HTTPS connections, described in `Ensure the Network Server Meets Minimum Requirements`. Using only this key also doesn't change the TLS or forward secrecy requirements imposed by ATS. As a result, you might need to combine this key with the `NSExceptionMinimumTLSVersion` or `NSExceptionRequiresForwardSecrecy` key in certain cases.

这段是本篇关于"键之间不可互相替代"的最重要一手机制描述：**单个键只关自己那一维**。要放松版本下限必须另配版本键，要放松前向保密必须另配前向保密键；而想要对某个 HTTPS 域放松信任评估（例如内嵌自签证书），前提也是把该域放进例外。注意第一句里那个 "**or**"：本键同时是"明文加载开关"与"手动放松信任评估的入场券"，两种用途的审核后果不同（§4.1）。

**（二）NSExceptionMinimumTLSVersion**

> The minimum Transport Layer Security (TLS) version for network connections. 默认值 **`TLSv1.2`**（string）。

> This key is optional. The value is a string, with a default value of `TLSv1.2`.

>>> [IMPORTANT]
> You must supply a justification during App Store review if you use this key to set a protocol version lower than 1.2, as described in `Provide Justification for Exceptions`.

两条口径：默认值就是基线本身（写 `TLSv1.2` 不改变任何事）；**只有设成低于 1.2 才需要理由**。至于可接受字符串的完整取值集合（`TLSv1.0`／`TLSv1.1` 这类字面量），一手页正文并未列出——本简报记为**未核实**，正文不得把二手教程里的取值表当一手事实（§14.2）。

**（三）NSExceptionRequiresForwardSecrecy**

> A Boolean value indicating whether to override the perfect forward secrecy requirement. **默认值 `YES`**（一手确认，许多二手文章猜对了但没给出处）。

> Set the value for this key to `NO` to override the requirement that a server support perfect forward secrecy (PFS) for the given domain. **Disabling this requirement also removes the key length check** described in `Ensure the Network Server Meets Minimum Requirements`. However, it doesn't impact the TLS version requirement. To control that, use `NSExceptionMinimumTLSVersion`.

> This key is optional. The default value is YES, which limits the accepted ciphers to those that support PFS through Elliptic Curve Diffie-Hellman Ephemeral (ECDHE) key exchange.

**耦合关系是本键最实用的知识点**：把它设 `NO` 会连带取消"RSA ≥2048／EC ≥256"的密钥长度检查，于是一次放松触发两项，而配置者通常只预期一项。反过来它明确不影响 TLS 版本要求。这条一手句适合直接放进"放松链式反应"表（§13.4）。

**（四）NSRequiresCertificateTransparency**：见 §3.6 末段——已被标 obsolete，官方 WARNING 明令不要用；其历史量化基线（至少两个 Apple 信任的 CT 日志签名时间戳）仍然有效，但由系统强制。它出现在例外域字典的结构图里，属于历史遗留位置。

**（五）NSExceptionRequiresNIAPTLSPackageVersion**（域级收紧键，逐字）：

> A string that indicates the version to use for the NIAP Functional Package for TLS, applied to an exception domain. The `NSRequiresNIAPTLSPackageVersion` key applies additional ATS requirements globally. Use this key to apply a different behavior to a specific domain entry in `NSExceptionDomains`.

即：全局开 NIAP 模式后，可以用域级键**退回**到不同行为（`none`），这是"收紧模式下的局部例外"，方向与常规例外相反。

## 3.9 第三方内容三键与其生效前提

`NSThirdPartyExceptionAllowsInsecureHTTPLoads`、`NSThirdPartyExceptionMinimumTLSVersion`、`NSThirdPartyExceptionRequiresForwardSecrecy` 这一族键在本篇的状态是 **C 级（未核实）**，必须写成缺口而不是配置项：

- 尝试访问其在现行 Bundle Resources 命名空间下的页面，全部返回 404。
- **反向证据更强**：ATS 总纲页给出的完整字典结构代码块（§3.1）里不含任何 `NSThirdPartyException*`；`NSExceptionDomains` 键页的结构代码块（§3.7）同样不含。
- 这一族键存在于**归档文档** `…/library/archive/documentation/General/Reference/InfoPlistKeyReference/Articles/CocoaKeys.html`（Cocoa Keys）。该归档页同时含有与 §1.4 同口径的句子："If ATS is enabled for a domain, you cannot loosen the system's HTTPS server trust evaluation requirements."
- 归档页正文的逐字摘录本次未取到，因此**本篇不写出这三个键的语义细节**。

正文唯一允许的写法是：**"这一族键仅见于已归档的 Cocoa Keys 文档，现行 ATS 结构图不再列出，属于历史兼容项；当前语义与生效前提一手未证实（N 项缺口）。"** 任何在配置里依赖它们的做法都应当被评审标记为待验证。

## 3.10 转发保密密码套件顺序键

`NSExceptionForwardSecrecyCipherSuitesOrder` 同样是 **C 级（未核实）**：现行 Bundle Resources 命名空间下无该键页面（404），且 §3.1 与 §3.7 的两张结构图都不含它。手册页 ATS 段倒是逐字提到 CBC 与 GCM 两种模式并写"App 可以按域关闭正向保密要求，这种情况下 RSA_AES 会添加到可用密码集中"——但**没有任何一手页描述"自定义前向保密密码套件顺序"这个配置项**。

因此本篇的处置是：把"能否指定套件顺序"列入 §14.2 缺口，并给出一条可核对的工程结论——**在 ATS 层没有公开的套件顺序控制项**；需要精细控制套件与协议上下界时，一手文本指向的是代码层的 TLS 参数（§7），而不是 plist。

## 3.11 键之间的优先级与相互覆盖关系

把 §3.1–§3.8 的散落条件句汇总成一张关系表。每条都要能指回上面某条逐字引文，否则不写。

| 关系 | 一手依据（章节） | 后果 |
|---|---|---|
| 例外域字典**优先于**全局键 | §3.7 第二条 NOTE | 一旦某域出现在 `NSExceptionDomains` 中，所有全局配置键对**该域**失效，即使字典为空 |
| 写了细粒度全局键 ⇒ `NSAllowsArbitraryLoads` 被忽略（iOS 10／macOS 10.12 起） | §3.1 Versioning 段、§3.2、§3.3、§3.4、§3.5 | 粗粒度开关静默失效，配置者可能以为例外生效了 |
| `NSAllowsLocalNetworking` 与 `NSAllowsArbitraryLoads` 跨版本分工 | §3.5 末段 | 新系统走细粒度、老系统走粗粒度，二者同时存在是**官方推荐的最小化模式** |
| 一个键只关自己那一维 | §3.8（一）逐字两句 | 配了明文加载键不会改变 TLS 版本与前向保密要求 |
| 关前向保密 ⇒ 连带取消密钥长度检查 | §3.8（三）逐字 | 一次放松触发两项，需在评审记录中显式写明 |
| 固定不影响其他要求 | §6.1（NSPinnedDomains 页逐字："has no impact on other security requirements or configuration"） | 固定是叠加期望，不是替代基线 |
| NIAP 键不需要审核理由（因为是加强） | §1.5 逐字 | 收紧自由、放松需申报 |
| `NSPinnedCAIdentities` 与 `NSPinnedLeafIdentities` 同时给出 ⇒ ATS 要求**每类都匹配** | §6.1 逐字（"If you specify both, ATS requires a match in each category"） | 常见误解是当成"任一匹配"；实为 AND |
| 默认信任评估在 ATS 之前 | §2.3（nscurl 页逐字） | 例外救不了无效证书 |

其中最后一条与第一条组合出的实际结论是：**"ATS 例外能做到的事"比"ATS 配置键的数量"少**。五个需要审核理由的键（§1.5）里真正能改变连接成败的是明文加载、版本下限、前向保密三类；证书有效性、名称匹配、锚信任不在例外可及范围内。

## 3.12 八种常见误配模式

以下每一条都由本章逐字引文直接支持，可用作 §13.4 审计表的来源。

1. **用通配符写例外域**。一手明确 "Don't use wildcard domains — Don't use `*.example.com`"，正确写法是 `example.com` + `NSIncludesSubdomains = YES`。且注意 §3.7：该开关**只展开一层**，`advanced.math.example.com` 不在范围内。
2. **在域名里带端口或尾点**。"Don't include a port number"；尾点会形成**不同的匹配对象**（`example.com.` 与 `example.com` 互不匹配，一手各举一例）。
3. **以为例外能救无效证书**。默认评估在前（§2.3），名称不匹配即失败；配 `NSAllowsArbitraryLoads` 也"still subject to default server trust evaluation"（§3.2）。
4. **把 `NSAllowsArbitraryLoads` 当长期架构**。一手措辞包括控制台里的 "Temporary exceptions"、键页 IMPORTANT 里的 "significantly reduces the security of your app"、总纲里的 "Always look for a way to avoid using exceptions as a first recourse"。三句合起来是官方对该键的定位。
5. **把 WebView／媒体例外当作上传例外**。两个键各自逐字声明 "without affecting your `URLSession` connections"（§3.3、§3.4），因此它们不会放松 App 自身请求与上传的传输要求；反之，若真正的问题是第三方网页内容，用粗粒度键则是过度放松。
6. **iOS 17 之后依赖 IP 直连却什么都没配**。§3.5 版本窗口表：iOS 17／macOS 14 起 IP 地址默认不再被允许，必须逐个 IP 或 CIDR 写进例外域；而老系统又不认识这个细粒度键，需要 §3.5 末段的双键模式。
7. **域名例外与 IP 例外混用**。§3.7 第一条 NOTE：按 IP 建连时域名例外不适用，即使 DNS 解析结果相同；反之亦然。设备发现类 App 尤其容易踩。
8. **写了细粒度全局键，却指望粗粒度键仍然管用**。§3.1 Versioning 段与 §3.2–§3.5 各页的条件句：iOS 10／macOS 10.12 起，写了其它全局键会让 `NSAllowsArbitraryLoads` 被忽略并回落默认 `NO`；这会让"我以为整个 App 都放宽了"的预期完全落空，症状是**只有部分连接被拒**。

补一条与配置无关但同源的误用：**把 ATS 当作整条链路的安全保证**。ATS 只管 URL Loading System 这一条路径（§2.5），只管"在路上的那一刻"；服务端落盘、备份、内部流转、上传后密钥归属，全部不在它的射程里（§11）。

# 4. 要求从哪儿来：审核、申报与企业内网

前三章回答了"要求是什么"：操作系统层的 ATS 默认检查、可配置的例外键、以及"只对收紧方向开放"的信任评估边界。本章回答另一个常被误传的问题：**这些要求到底由谁强制执行**。直觉答案是"应用商店审核"，但一手取证给出的答案更微妙：现行审核指南正文里找不到传输安全条款，真正有强制力的是操作系统本身、例外键配套的申报义务，以及出口合规问卷——三者是三条互不替代的管线。

## 4.1 应用商店审核对安全连接的要求口径

状态：**已核实（负向取证）**。来源：https://developer.apple.com/app-store/review/guidelines/ （英文页，页脚 © 2026，正文标注 **Last Updated: October 16, 2026**）。在该页 DOM 全文 `document.body.textContent` 上检索：`/encrypt/i` 命中 0 次、`TLS` 命中 0 次、`ATS` 命中 0 次、`https` 命中 0 次；再逐条扫描全部带编号条目（id=`1`…`5.6.4`）中匹配 `HTTPS|TLS|encrypt|transport security|secure connection|network connection` 者——**0 条**。现行指南五个一级分区为 `1. Safety`／`2. Performance`／`3. Business`／`4. Design`／`5. Legal`，其中 `5.1` 是 Data Collection and Storage、`5.2` 是 Other Time-Sensitive Issues，均无安全连接要求。

与"网络连接"沾边的现行条款只有三条（逐字，英文原文）：

> 2.5.1 Apps may only use public APIs and must run on the currently shipping OS.

> 2.5.5 Apps must be fully functional on IPv6-only networks.

> 2.5.6 Apps that browse the web must use the appropriate WebKit framework and WebKit JavaScript.

因此本报告的口径是：**"审核要求必须用 HTTPS"在 2026-10-16 版指南正文中找不到一手支撑，属于二手流传说法**。传输安全要求的真实约束来源有三：(a) 操作系统层 ATS——不满足即连接失败，与审核无关，装上去就跑；(b) 例外键的申报义务——见 §4.2，这是审核管线里唯一与 ATS 直接挂钩的一手文本；(c) 出口合规问卷——见 §4.3。手册《关于 App Store 安全性》（`secb8f887a15`，2024-12-19）把审核描述为政策执行而非传输强度要求（逐字）：

> 在 App Store 中，App 均来自于同意遵循 Apple 准则且可识别身份的开发者，同时 App 通过加密保证以免受修改，从而安全分发给用户。每个 App 和每个 App 更新均经审查以评估其是否符合隐私、安保和安全要求。

注意其中"通过加密保证以免受修改"指的是**App 包的分发完整性**，不是出站连接——引用时不得混为 TLS。

## 4.2 例外必须最小化：逐域例外与全局开关的取舍

审核管线对 ATS 的直接抓手是**例外申报**。`Preventing Insecure Network Connections` 页逐字列出需要申报理由的**恰好五个键**：

> Adding certain ATS exceptions to your app's `Information Property List` file requires you to provide justification, and might trigger additional App Store review for your app. Exceptions that require justification are:
> - Arbitrary connection exceptions (`NSAllowsArbitraryLoads`)
> - Media streaming exceptions (`NSAllowsArbitraryLoadsForMedia`)
> - Web content loads (`NSAllowsArbitraryLoadsInWebContent`)
> - Per-domain nonsecure connections (`NSExceptionAllowsInsecureHTTPLoads`)
> - Per-domain minimum TLS version (`NSExceptionMinimumTLSVersion`)

反向的一条同样逐字：两个 NIAP 键**不需要**理由——

> `NSRequiresNIAPTLSPackageVersion` and `NSExceptionRequiresNIAPTLSPackageVersion` don't require justification because they strengthen rather than weaken security requirements.

这是"放松／收紧不对称"的一手证据：往严的方向配置自由，往松的方向配置必须书面申报。`NSAllowsLocalNetworking` 不在申报清单上，正文不得写成"所有例外都要理由"。

官方给出的**可接受理由样本**（逐字）同时暴露了审核方心里的"例外应该长什么样"：

> Some examples of justifications eligible for consideration are:
> - The app must connect to a server managed by another entity that doesn't support secure connections.
> - The app must support connecting to devices that cannot be upgraded to use secure connections, and that must be accessed using public host names.
> - The app must display embedded web content from a variety of sources, but can't use a class supported by the web content exception.
> - The app loads media content that is encrypted and that contains no personalized information.

> When submitting your app to the App Store, provide sufficient information for the App Store to determine why your app cannot make secure connections by default.

>>> [IMPORTANT]
> Always look for a way to avoid using exceptions as a first recourse. If you must use an exception, make it as limited in scope as possible.

把这段与 §3 的键语义合并，得到例外取舍的判定链：**先问能否升服务器（官方第一建议）→ 不能，则按理由样本对号入座选最窄的键（域级 `NSExceptionDomains` 优先于全局键）→ 全局键之间注意互斥（§3.2：写了细粒度全局键会让 `NSAllowsArbitraryLoads` 被忽略）→ 提交时把理由写足**。四条可接受理由里有两条直接对应"外部实体管理的服务器"与"不可升级的设备"——这正是 §4.4 企业内网遗留系统的官方出口。

## 4.3 加密申报与出口合规中的传输加密定位

状态：**已核实**（整节逐字，英文原文）https://developer.apple.com/documentation/security/complying-with-encryption-export-regulations （页脚 © 2026）。提交即触发申报（逐字）：

> When you submit your app to TestFlight or the App Store, you upload your app to a server in the United States. If you distribute your app outside the U.S. or Canada, your app is subject to U.S. export laws, regardless of where your legal entity is based. If your app uses, accesses, contains, implements, or incorporates encryption, this is considered an export of encryption software, which means your app is subject to U.S. export compliance requirements, as well as the import compliance requirements of the countries where you distribute your app.

> Every time you submit a new version of your app, App Store Connect asks you questions to guide you through a compliance review. You can bypass these questions and streamline the submission process by providing the required information in your app's `Information Property List` file.

申报键与**"用 HTTPS 即属豁免"的官方口径**（逐字）：

> Add the `ITSAppUsesNonExemptEncryption` key to your app's information property list with a Boolean value that indicates whether your app uses encryption. Set the value to `NO` if your app—including any third-party libraries it links against—doesn't use encryption, or if it only uses forms of encryption that are exempt from export compliance documentation requirements. Otherwise, set it to `YES`.

> Typically, the use of encryption that's built into the operating system—for example, when your app makes HTTPS connections using `URLSession`—is exempt from export documentation upload requirements, whereas the use of proprietary encryption is not.

> If your app uses exempt forms of encryption, you might alternatively be required to submit a year-end self-classification report to the U.S. government. (If you use non-exempt encryption and provide documentation to Apple, the self-classification report isn't necessary.)

非豁免路径（逐字）：

> If your app requires export compliance documentation, upload the required items to App Store Connect... After successfully reviewing the documents, Apple provides you with a code. Add this string as the value for the `ITSEncryptionExportComplianceCode` key in your app's information property list.

四个正文必须守住的口径：(1) 原文是 **"Typically … is exempt"**，即"通常"而非"一律"，不得写成"HTTPS 永远豁免"；(2) 豁免不等于零义务——豁免类仍可能要向美国政府提交**年度自分类报告**，而非豁免类向 Apple 交了文档反而**不需要**年报，这个反向关系是常见写错点；(3) 判定范围**含链接的第三方库**（"including any third-party libraries it links against"），这是自查清单里"第三方 SDK 是否带非豁免加密"一条的官方依据；(4) 术语纪律——本页的"upload"指**把 App 提交到美国境内服务器**，与用户数据上传链路（U1/U2/U3）是两件事，不得混用。

## 4.4 企业内网遗留依赖如何被合规压力挤出

组织侧（MDM）能不能替 App 关掉 ATS？**负向取证：不能**。方法：取 developer Device Management 载荷目录全量清单与 Restrictions 载荷全部 210 个键名，逐一检索 `ATS`／`transport`／`NSAppTransportSecurity`——无任何键或载荷。组织能做的与不能做的分界（可写正文口径）：

- **能做的**：改网络可达性与路径（VPN／per-app VPN／AlwaysOn／Relay／代理／DNS／允许网络列表）；改信任锚与撤销行为（安装根证书、`CertificateTransparency` 载荷对内部域降低 CT、`allowUntrustedTLSPrompt=false` 让不受信证书直接拒绝、`allowOTAPKIUpdates=false` 关闭 OTA PKI 更新但不影响 CRL/OCSP）；改上传目的地（`allowCloudDocumentSync`／`allowManagedAppsCloudSync`／`allowCloudBackup`／`allowCloudKeychainSync`／`allowCloudPrivateRelay`）；改流量可见性（内容过滤 `FilterSockets`／`FilterBrowsers`／`FilterPackets`／URL 过滤）。
- **不能做的**：**没有任何载荷或键可以放宽 ATS 的 TLS 版本／前向保密／密钥长度／证书签名算法要求，也没有载荷可按域关闭 ATS。** 页面里唯一"降低要求"的官方措辞是证书透明度载荷，且只影响 CT 检查。802.1X 可设最低／最高 TLS 版本（《使用 Apple 设备内建的网络安全功能》：> 你还可以通过 EAP-TLS、EAP-TTLS、PEAP 和 EAP-FAST 给 802.1X 网络有效负载设定最低和最高 TLS 版本），但那是**网络准入认证链路**，不是 App 出站连接。
- 组织能**收紧**（`allowUntrustedTLSPrompt=false` 去掉用户"仍要连接"的逃生口）、能**旁路观察**（代理／内容过滤）、能**断掉目的地**（iCloud 开关），唯独不能替 App 决定是否满足 ATS——决定权在 App 自身的 `Info.plist` 与运行时（锁定模式）。

这个分界解释了企业内网遗留依赖的真实处境：内网老服务器不满足 ATS 时，MDM 无法放行，压力全部传导到两条合规管线上——要么 App 在 `Info.plist` 里为内网域写**逐域例外**并走 §4.2 的申报（理由样本第 1、2 条正是为这种场景准备的），要么升级服务器（官方第一建议）。#91 报告 §1.2 引过的手册句在此复现一次口径：不满足要求的连接会失败，"除非 App 重写了 App 传输安全性"——**重写主体被官方文本限定为 App，而非描述文件**。

运行环境侧还有一条：锁定模式（《关于"锁定模式"》，https://support.apple.com/zh-cn/105120 ，2026-09-18）会在设备状态层面改变连接行为——逐字包括"打开'锁定模式'后，你的设备将不会自动加入不安全的无线局域网，并且会断开已连接的不安全无线局域网。iPhone 和 iPad 的 2G 和 3G 蜂窝网络支持会关闭"；且"在'锁定模式'下，无法安装配置描述文件，并且无法在移动设备管理或设备监督中注册设备"，但"如果启用'锁定模式'之前在移动设备管理中注册了设备，则这台设备仍处于受管理的状态。系统管理员可以在这台设备上安装和移除配置描述文件"。两点正文纪律：锁定模式解除限制的对象是 **WebKit 网页内容**（"你可以在 iOS 或 iPadOS 上排除某个 App，以便移除这个 App 的网页内容限制"），不得写成"改 TLS 版本要求"；锁定模式**不是 MDM 可下发／可配置的选项**，组织无法强制员工开或关。

## 4.5 申报项与配置项错位时的后果

把前三节的三条管线并排，可以列出一张"错位表"——每条错位都是一个具体的、可观测的故障形态：

| 错位 | 配置侧事实 | 申报侧事实 | 后果 |
|---|---|---|---|
| 配了例外、没写理由 | `NSAllowsArbitraryLoads=YES` 键合法、连接正常 | §4.2 清单要求申报理由 | "might trigger additional App Store review"（逐字），可能被要求补充说明或退回 |
| 写了理由、键配错 | 真实问题在内网老设备 → 应配逐域 `NSExceptionAllowsInsecureHTTPLoads`，却配了全局 `NSAllowsArbitraryLoads` | 理由样本允许该场景 | 审核可过，但全 App 所有域的扩展检查一并关闭（§3.2），过度放松成为长期技术债 |
| 以为审核指南会要求 ATS | §4.1：指南正文 0 命中 | ATS 由 OS 强制 | 开发者按"审核条款"心态准备豁免材料，实际连接在旧 SDK 链接（§3.1 Versioning：iOS 9.0/macOS 10.11 之前 SDK 则 ATS 整体关闭）或低层 API 路径（§2.5）上根本没有被 ATS 检查——保护缺失但审核无恙 |
| HTTPS 即填豁免、库里有自有加密 | `ITSAppUsesNonExemptEncryption=NO` | §4.3：判定范围含第三方库 | 申报失实，属合规问题而非技术问题；年报义务方向也随申报翻转 |
| 以为 MDM 能救内网连接 | App 侧没配例外 | §4.4：无载荷可放宽 ATS | 员工设备在内网连不上老系统，工单被错误地派给 IT；正确修复点在 App 的例外域或服务器升级 |

一句话收束本章：**ATS 的"要求"来自操作系统执行，审核与出口合规只是把"你放松了什么"变成书面义务；组织侧既不能代为放松也不能代为申报**。下一章回到技术主线：当 ATS 不在场或被豁免时，默认服务端信任评估还剩哪四条、App 对它唯一开放的操作方向是什么。

# 5. 服务端信任评估：App 能改的只有收紧方向

ATS 是叠在"默认服务端信任评估"之上的加强层；把加强层关掉（或走 §2.5 的不适用路径）之后，仍然在场的是 TLS 自己的客户端认证服务器环节。本章以 Apple 归档技术说明 **TN2232《HTTPS Server Trust Evaluation》**（最后修订 **2014-07-29**，页脚 © 2014）与现行 Foundation 文章为骨架。先记录一条取证纠正：

**事实核查纠正（状态：已核实）**：流传材料常把这份文档写成 "TN2287"。逐字核查结果——TN2287 的真实标题是 `Technical Note TN2287: iOS 5 and TLS 1.2 Interoperability Issues`（更新于 2011-10-14）；"HTTPS Server Trust Evaluation" 的真实编号是 **TN2232**（更新于 2014-07-29）。并且 **TN2232 全文没有出现 "App Transport Security"/"ATS" 字样**（对页全文 grep 仅命中 "import" 等无关词），因此它不能用来证明任何 ATS 要求；引用"默认评估／证书固定／SecTrust 流程"必须标 TN2232，同时注明其 API 名停留在 iOS 7／OS X 10.9 时代，不得当作当前推荐做法。

## 5.1 默认评估由谁执行、检查什么

TN2232 先给出 TLS 的两条官方保证（逐字）——这是"上传通道的机密性与服务器真实性"的二分口径：

> HTTPS inherits its security guarantees from TLS. By default these are:
> - on-the-wire privacy — This guarantees that the data is secure from a passive attacker (someone who can see but can't modify the packets exchanged between the client and server).
> - client-authenticates-server authentication — This protects the client from an active attacker (someone who can both see and modify the packets exchanged between the client and the server). Most importantly, it protects the client from various forms of man-in-the-middle attack, including impostor servers...

> TLS server trust evaluation is the part of TLS that implements client-authenticates-server authentication. If you disable it, you lose this second, extremely important security guarantee.

双向认证（客户端证书）在本页被**明确划出范围**：

> Note: TLS also supports server-authenticates-client authentication. This authentication is optional at both ends... it is outside the scope of this document and thus not discussed further here.

评估本身是"两步＋递归收敛到锚"（逐字）：

> TLS server trust evaluation consists of two basic steps:
> - basic X.509 certificate trust evaluation
> - TLS-specific additions

> X.509 certificate trust evaluation is a recursive two-step process:
> - check the validity of the certificate itself — ... a) verifying the digital signature, and b) checking that the verify date (typically the current date) is within the certificate's valid date range.
> - check the validity of the issuer — This involves finding the issuer's certificate and (recursively) checking its validity.

> Trust evaluation can succeed in only one case: if it hits a **trusted anchor**. A trusted anchor is a certificate that the system trusts implicitly, typically because it's the root certificate of a well-known certificate authority that has been baked in to the system.

签名有效即"签发者对'主体持有该公钥对应私钥'的担保"（逐字）：

> If the digital signature is valid then the certificate is a guarantee from the issuer that the subject holds the private key associated with the public key in the certificate.

X.509 通过后叠加 TLS 专属检查（逐字）：

> ...checking that the DNS name that you are attempting to connect to matches the DNS name in the certificate. There are, however, a few wrinkles:
> - Typically you would expect to find this DNS name in the subject's Common Name field, but it can also be in a Subject Alternative Name extension. If a name is present in the Subject Alternative Name extension, it takes priority over the Common Name.
> - The Subject Alternative Name extension may also contain IP addresses, which the client consults if it connected via an IP address rather than a DNS name.
> - The DNS name in the certificate might be a wildcard name, for example, "*.apple.com".
> - The Extended Key Usage extension is expected to include the Server Authentication value.

五类常见失败（逐字）与前三个的适用范围（逐字 Important 框）：

> - **missing issuer certificate** — For any given certificate (except the trusted anchor), the system must be able to locate the certificate of the issuer.
> - **date problems** — For any given certificate, the verify date must be within the certificate's valid date range.
> - **self-signed certificate** — ...if the certificate is self-signed, it will cause evaluation to fail (unless it's a trusted anchor).
> - **no trusted anchor** — The system must be able to follow the path of issuer certificates leading to a trusted anchor.
> - **DNS name mismatch** — The DNS name that you're trying to connect to must match the name in the server certificate...

> Important: The first three criteria apply to all certificates in the path of certificates leading from the server certificate to the trusted anchor.

量化基线：典型失败错误码 `NSURLErrorDomain Code=-1202` = `NSURLErrorServerTrustEvaluation` 语义下的 `NSURLErrorServerCertificateUntrusted`（逐字：「In this case error -1202 in the NSURLErrorDomain domain is NSURLErrorServerCertificateUntrusted, which means that server trust evaluation has failed.」）。把这段与 §2.2 合并即得完整口径：**developer 页的"默认评估四条"是 ATS 视角的摘要，TN2232 的"两步＋五类失败"是机制视角的展开**；SAN 优先于 CN、通配符合法、EKU 须含 Server Authentication 三条细节只在 TN2232 有一手文本。

## 5.2 挑战回调与默认处理的责任划分

现行 Foundation 文章《Handling an authentication challenge》（© 2026）划定了"系统默认处理"与"App 接管"的责任边界（逐字）：

> When your app makes a request with a `URLSessionTask`, the server may respond with one or more demands for credentials before continuing. The session task attempts to handle this for you. If it can't, it calls your session's `delegate` to handle the challenges.

> Implement the `urlSession(_:didReceive:completionHandler:)` method of `URLSessionDelegate` to handle **session-wide** challenges. These are challenges like **Transport Layer Security (TLS) validation**. Once you've successfully handled this kind of challenge, your action remains in effect for all tasks created from that `URLSession`.
> Implement the `urlSession(_:task:didReceive:completionHandler:)` method of `URLSessionTaskDelegate` to handle **task-specific** challenges. These are challenges like demands for username/password authentication.

三条必须写对的机制：(1) **TLS 服务器信任挑战是 session-wide**——处理一次，对该 session 的全部后续任务（含上传任务）持续有效，这直接决定"多个上传复用同一 session 时信任决策的作用域"；(2) 未实现 delegate ≠ 不处理，`.performDefaultHandling` 会先尝试满足挑战、否则进入下一个挑战（逐字："Telling the task to use its default handling may satisfy the challenge; otherwise, the task will move on to the next challenge in the response and call this delegate again."）；(3) 凭据作用域逐字：`URLCredential.Persistence.forSession` 的凭据 "only stored by the URLSession instance that created the task"。不实现任何 delegate 的后果也有原文：请求会被服务器拒绝并收到 HTTP `401`。

## 5.3 手工信任评估的正确形态

《Performing manual server trust authentication》（© 2026）给出的两种合法动机（逐字）：

> - You want to accept server credentials that would otherwise be rejected by the system. For example, your app makes a secure connection to a development server that uses a self-signed certificate, which would ordinarily not match anything in the system's trust store.
> - You want to reject credentials that would otherwise be accepted by the system. For example, you want to "pin" your app to a set of specific keys or certificates under your control, rather than accept any valid credential.

> In most cases, you should let the URL Loading System's default handling evaluate the server trust.

官方示例代码的守门形态（逐字）：先查认证方法与主机，不匹配一律 `.performDefaultHandling`；拿到 `serverTrust` 后自行判定，接受走 `URLCredential(trust:)` + `.useCredential`，拒绝走 `.cancelAuthenticationChallenge` 并"Show a UI here warning the user the server credentials are invalid, and cancel the load"。这与 §2.3 的硬故障语义、§3.6 的 `certificateValidator` WARNING（"Most apps should not override the default system handling, as doing so can result in insecure network connections and major security vulnerabilities."）三方一致。

TN2232 侧的两条禁令级原文（逐字）：

> Warning: Disabling server trust evaluation puts the user's security at risk. Do not do it in any code you intend to ship to end users. If server trust evaluation is failing, you must understand why it's failing and decide on the best way to resolve that failure.

> Warning: +[NSURLRequest setAllowsAnyHTTPSCertificate:forHost:] is not a public API and is thus not supported by Apple; moreover, use of non-public APIs is not allowed in App Store apps.

再叠加 §3 的 ATS 限定句（"You cannot loosen server trust requirements for an ATS-protected domain, but you can tighten them"），本篇对手工评估的总结口径是：**对受 ATS 保护的域，手工评估的正确形态只有"追加拒绝条件"；"追加接受条件"只在域已被例外豁免时才在 OS 层面可能，且在审核层面要按 §4.2 申报**。

## 5.4 信任锚从哪里来：内置根、用户安装与组织下发

锚的集合由三方共同决定。系统内置（TN2232："baked in to the system"）之外：

- **组织可安装根证书**（描述文件"凭证和身份／证书"正规载荷类别，§4.4），并且能**去掉用户的逃生口**——Restrictions 键逐字：`allowUntrustedTLSPrompt` :: "If `false`, the system automatically rejects untrusted HTTPS certificates without prompting the user."（iOS 5+）；`allowOTAPKIUpdates` :: "If `false`, the system disables over-the-air PKI updates. **Setting this restriction to `false` doesn't disable CRL and OCSP checks.**"（iOS 7+）。注意方向：这两条一个收紧、一个只影响根证书更新而不影响撤销。
- **App 可自定义锚**——`SecTrustSetAnchorCertificates` 的排他语义（TN2232 逐字加粗句）：

> **SecTrustSetAnchorCertificates sets a flag that prevents the trust object from trusting any other anchors; if you also want to trust the default system anchors, call SecTrustSetAnchorCertificatesOnly to clear that flag**

即"替换"而非"追加"，漏调 `SecTrustSetAnchorCertificatesOnly` 会拒掉全部系统根——这是 §6.3 的主题，这里先记锚来源。另两条一手细节：「Adding arbitrary intermediate certificates to a trust object is always safe because their validity is cryptographically assured.」；`kSecTrustResultProceed` 与 `kSecTrustResultUnspecified` 在 iOS 上无区别，用户可配置信任偏好的 Keychain Access 仅 macOS 有。
- **管理通道自身的锚要求**独立成线：MDM 载荷的 `ServerURL`／`CheckInURL` 逐字要求 "The URL must begin with the `https://` URL scheme"，且与 `Topic`（须带 `com.apple.mgmt.` 前缀）三键"更新时不可变，改动即被系统拒绝"——设备管理的信任链不给静默改址留缝。

## 5.5 撤销信息在 App 侧能看到什么

一手可拼出的撤销面有四条：

1. 系统层面撤销检查默认在场，且**关 OTA PKI 不会关撤销**（§5.4 逐字：不 disable CRL and OCSP checks）。
2. 可在 App 内**主动加严**：TN2232 逐字——"`SecPolicyCreateRevocation` lets you create a security policy that specifically checks for certificate revocation (for example, via OCSP or a CRL)."
3. 连接层开关在 C API：`sec_protocol_options_set_tls_ocsp_enabled` :: "Enable or disable OCSP support."、`sec_protocol_metadata_access_ocsp_response`（§7 清单）——**这些是 ATS 不适用路径上的工具**（§2.5），ATS 路径本身不提供"关撤销"的键。
4. 撤销校验自身依赖出网。《在企业网络上使用 Apple 产品》列出的撤销／证书验证主机（逐字清单节选）：`certs.apple.com／crl.apple.com／crl3.digicert.com／crl4.digicert.com／ocsp.apple.com／ocsp.digicert.cn（中国大陆的证书验证）／ocsp.digicert.com／ocsp2.apple.com／valid.apple.com`。由此可推导（**本篇标注为推论，非官方原话**）：防火墙挡掉这些主机会让 TLS 信任评估失败——"断网挡域名能杀死连接"的机制在撤销这一环也成立。

## 5.6 证书透明度检查的触发条件

现行一手口径合并三条：(1) ATS 侧——`NSRequiresCertificateTransparency` 已被官方标 **obsolete**（§3.6：CT 要求的量化基线为"至少两个 Apple 信任的 CT 日志的签名 CT 时间戳"，逐字 "CT requires a domain's server certificates to have support from signed CT timestamps from at least two CT logs trusted by Apple"；"The system now requires this behavior in all cases, making the key irrelevant."）。(2) 连接层——`sec_protocol_options_set_tls_sct_enabled` :: "Enable or disable SCT (signed certificate timestamp) support."（属 ATS 不适用路径）。(3) 组织侧——"证书透明度"设备管理有效负载（deployment 手册，2024-03-07）是**Apple 官方文档中唯一明确"允许管理员降低 TLS 证书相关安全要求"的机制**（逐字）：

> **此有效负载允许设备管理员有选择地为内部域和服务器降低"证书透明度"要求，以避免与内部服务器通信的设备发生此类信任失败。**

配套限定同样逐字："停用特定域 — 证书透明度被停用的域列表。前导句点可用于匹配子域，但域匹配规则无法匹配一个顶层域内的所有域。（不允许'.com'和'.co.uk'……）"。正文口径：**2018-10-15 后签发的证书，CT 检查对 ATS 路径已不可关；唯一的官方"降低"入口是组织侧 CT 载荷，且只降 CT、不降 TLS 版本／套件／密钥长度**（呼应 §4.4 负向取证）。

## 5.7 把评估结果记录下来才算可审计

评估的"结果面"是审计的最低要求。一手可用的记录点：`SecTrustCopyResult` 逐字——"lets you determine whether extended validation was done as part of the trust evaluation."；`SecCertificateCopyValues`（macOS）可用于把证书属性／扩展值落日志；失败侧的锚点即 `-1202` 与五类失败原因；连接成立后的实测口径在 §7.11（`sec_protocol_metadata_get_negotiated_tls_protocol_version` 等"策略不等于结果"字段）与 §13.2 的可观测证据清单。给评审的可操作定义：**一次上传的完整审计记录 = 目标域 + 例外命中情况（§3）+ 信任评估结果与失败原因（§5.1/5.2）+ 协商出的实际 TLS 版本与套件（§7.11）+ 撤销/CT 检查状态（§5.5/5.6）**；缺任何一项，"连接是安全的"都只是未取证的断言。

# 6. 证书固定与被固定的边界

固定（pinning）是"收紧方向"上最有代价的手段：它把"任何受信任 CA 签发的有效证书都可以"收窄成"只有我预期的那几张可以"。本章给出现行两条固定路径——ATS 声明式 `NSPinnedDomains` 与代码级 SecTrust 自定义——以及固定的三条边界：**只叠加不替换、锚替换是排他的、组织插层可以让固定按设计失效**。

## 6.1 域名锁定字典的两类键与匹配语义

`NSPinnedDomains` 是 ATS 配置字典里唯一的声明式固定入口（状态：已核实，逐字）：

> A collection of certificates that App Transport Security expects when connecting to named domains.

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

四条语义要点：(1) 两类键至少填一类，**同时填则两类各自必须命中**（"a match in each category"，AND 而非 OR）；(2) 域名字符串规则完全复用 `NSExceptionDomains`（小写、不含端口、不含尾点、不用通配符、iOS 17 起可写 IP/CIDR），子域展开同样只加一层（`NSIncludesSubdomains`）；(3) 身份的具体写法（SPKI/公钥哈希的数组格式）在 `NSPinnedCAIdentities`／`NSPinnedLeafIdentities` 子页未取到逐字（状态：未核实，标缺口），语义仅由父页文字支撑；(4) 这是**收紧**——与 §4.2 申报清单呼应，`NSPinnedDomains` 不在需要理由的五个键之列。

## 6.2 三种固定对象形态与各自的轮换代价

TN2232 把固定归入 "stricter"（逐字）：

> Customizing server trust evaluation is not just about working around problems; you can also use it to make security tighter. If you're working on a high-security program, you might want to go beyond the default server trust evaluation and add some more checks of your own.

官方术语定义（逐字，TN2232 Glossary）给出两种形态的区分：

> - **certificate authority pinning** — A stricter form of trust evaluation that requires that the server's certificate be issued by a specific certificate authority (not just any trusted certificate authority).
> - **certificate pinning** — A stricter form of trust evaluation that requires that the server use a specific certificate or a specific public key.

第三种形态是"证书属性检查"（逐字：检查特定属性值／扩展，macOS 上 `SecCertificateCopyValues` 有帮助）。代码侧对应的固定对象与 API（TN2232 逐字）：CA 根用 `SecTrustSetAnchorCertificates`、公钥用 `SecTrustCopyPublicKey` 抽取比对、结果与扩展校验状态用 `SecTrustCopyResult`。现行 Foundation 文章给出的**轮换代价指南**是正文的工程落点（逐字）：

> - Compare the server's credentials against a public key, instead of storing a single certificate in your app bundle. This will allow you to reissue a certificate for the same key and update the server, rather than needing to update the app.
> - Compare the issuing certificate authority's (CA's) keys, rather than using the leaf key. This way, you can deploy certificates containing new keys signed by the same CA.
> - Use a set of keys or CAs, so you can rotate server credentials more gracefully.

三条的排序即代价排序：**钉叶子证书最脆（换证书=换 App），钉公钥次之（同钥重签可救），钉 CA 最宽但强度最低**；"用一组而不是一个"是唯一被官方文本直接背书的降风险手段。对照 `NSPinnedDomains`：CA 类与叶子类同时钉 = 官方结构里允许的最严形态，也意味着两条轮换路径同时被锁。

## 6.3 替换锚证书：正确用法与最常见的误用

`SecTrustSetAnchorCertificates` 的排他语义在 §5.4 已逐字引用（"sets a flag that prevents the trust object from trusting any other anchors"）。本节给出误用形态的完整描述：把固定实现为"只认我自己的根"却不调 `SecTrustSetAnchorCertificatesOnly` 清除标志，结果是该 trust object 对**所有系统内置根**判失败——症状不是"我的服务器连不上"而是"这个 session 的 HTTPS 全军覆没"（CDN、第三方 API、推送中继全部 `-1202`）。TN2232 的正确用法序列（逐字）是"从证书数据建 `SecCertificateCreateWithData` → `SecTrustSetAnchorCertificates` 设为锚 → 照常评估"，并补一句安全边界：「Adding arbitrary intermediate certificates to a trust object is always safe because their validity is cryptographically assured.」——追加中间证书不会扩大信任，追加**根**才会。

## 6.4 固定与其他安全要求的关系：只能叠加不能替换

`NSPinnedDomains` 页的边界句（逐字）：

> Pinning a certificate for a given domain has no impact on other security requirements or configuration. For example, pinning a CA certificate doesn't change the way the system evaluates that certificate's suitability as an anchor certificate.

这句话否定了两种流传口径：(1) "固定了证书就等于自己定义了全部信任要求"——不对，ATS 的 TLS 版本／前向保密／密钥长度检查照常执行，固定只是**叠加的期望集合**；(2) "钉了某张 CA 它就自动成为可信锚"——不对，锚的资格仍按系统评估。同一逻辑适用于代码级固定：在挑战回调里返回 `.useCredential` 前，连接早已通过 ATS 扩展检查；你只能在"通过基线"的候选里再筛，不能替基线放行。这与 §2.3 硬故障语义、§5.3"接受路径需要域已被豁免"构成同一不变式：**固定改变的是期望集合，不是地板**。

## 6.5 组织插层：代理与内容过滤的重加密通道

企业网络里"插一个代理"能对固定造成什么影响，官方口径分三层（均一手）：

- **Apple 自己的服务不允许插层**（《在企业网络上使用 Apple 产品》101555，2026-07-31，逐字）：「**Apple 服务将中断任何使用 HTTPS 拦截（SSL 检查）的连接。如果 HTTPS 流量遍历某个网页代理，请对本文列出的主机停用 HTTPS 拦截。**」且该页的"支持代理"列显示 **iCloud/CloudKit 数据面与绝大多数证书吊销校验主机被标记为不支持代理**——组织能对自己的域做 TLS 中间人（装根 CA + 代理），不能对 Apple 服务域这么做。
- **对普通 App 的插层有前置条件**（《为 Apple 设备过滤内容》dep1129ff8d2，2025-09-24）：全局 HTTP 代理要求 iPhone/iPad/Apple TV 为**受监督设备**；高级内容过滤走内容过滤提供者扩展，"The content filter processes network traffic for managed apps..."——非受监督设备上的普通 App 流量不在官方支持的拦截路径内。
- **插层对固定方案的后果**：组织代理做重加密时，服务器证书换成内部 CA 签发——若 App 钉了公有 CA，连接按设计失败（这是固定该有的行为，不是 bug）；若组织装的根恰好被 App 的 CA 钉集合接受，固定就被"合法"绕过。**结论口径：`NSPinnedLeafIdentities`（钉叶子/公钥）能挡住组织插层，`NSPinnedCAIdentities` 钉公有 CA 挡不住"企业 CA 重签"这条路**——这条是前述一手句的合取推论，正文标注为推论。

## 6.6 固定失效的可观测痕迹

固定引发的失败与普通信任失败共享错误面，区分要靠痕迹：(1) 连接层报 `-1202 NSURLErrorServerCertificateUntrusted`（§5.1），但**证书链本身在系统钥匙串视角完全有效**——这是"被固定拒了"而非"证书坏了"的第一判别式；(2) `SecTrustCopyResult` 显示扩展校验已完成（§5.5 句），失败点落在自定义比较环节；(3) 服务端换证/换 CA 后**旧版本 App 集中报错、新版本正常**的时间线形态（钉叶子→证书到期即爆，钉 CA→换 CA 才爆，爆炸半径与 §6.2 代价排序一一对应）；(4) 后台会话侧：信任失败不会自动重试到成功，`waitsForConnectivity` 只管连通性不管信任（§8.3），因此固定失配表现为**上传任务持续终局失败**而非延迟；(5) 组织插层形态的特征是证书签发者变为内部 CA、且与 MDM 载荷下发时间对齐。看不到的一项也要声明：App 内 `.useCredential` 的自定义判定过程本身没有系统级日志出口，可审计性取决于开发者自己埋点（§5.7）。

## 6.7 固定方案评审清单

合并 §6.1–§6.6 的一手依据，评审一张固定方案至少要问九个问题：

1. 声明式（`NSPinnedDomains`）还是代码级（挑战回调）？两条路径的失败面不同，前者全 App 一致、后者可分 session。
2. 钉的对象是哪一类：叶子证书／公钥／CA？对应 §6.2 三档轮换代价，且官方建议逐字偏向"公钥 + 一组"。
3. 集合里有没有**备用的第二把钥匙/第二个 CA**（"Use a set of keys or CAs"）？只有一个 = 单点故障。
4. 两类键同时钉时，是否理解 AND 语义（§6.1）会同时锁两条轮换路径？
5. 固定是否与例外共存？同一域既有 `NSExceptionAllowsInsecureHTTPLoads` 又有钉证书要求时，等于"允许明文但要证书对得上"，需确认是有意为之（§3.8、§6.4"地板与期望集合"分开）。
6. 组织插层场景：受监督设备 + 代理环境下，方案预期是"失败"还是"接受内部 CA"？两种都要写成显式决定，不能留给巧合。
7. 失效可观测性：`-1202`、失败原因、当时的服务器链指纹是否落日志（§5.7、§6.6）？
8. 升级路径：固定数据在 App 包里，**换钥匙需要发版**——回滚窗口、灰度策略、以及"App 停止维护后固定仍生效"的长期风险是否评估过？
9. 后台上传：固定失配 + 后台会话 = 静默的持续失败（§6.6 第 4 条），有没有对"完成但从未成功"的任务做监测？

# 7. Network.framework：把要求写进代码

§2.5 已确立：ATS 不适用于 Network framework 调用，责任移交 App（"you take responsibility"）。本章展开这份责任的清单——现行文档里 Network 侧到底有哪些可调项、哪些流传名字其实不存在、以及每一项的一手语义。取证方法说明：本节存在性核查逐一经 `https://developer.apple.com/tutorials/data/documentation/<slug>.json` 端点探测（200/404），404 清单同样是一手结论。

## 7.1 参数对象与 TLS 选项的结构：三层入口

现行文档给出的 TLS 配置面是三层转发结构（均逐字，© 2026）：

- 第一层，默认栈：`NWParameters.tls` :: "A set of default parameters for connections and listeners that use TLS and TCP."（iOS 12.0／macOS 10.14 起）；`NWParameters` 本身 :: "An object that stores the protocols to use for connections, options for sending data, and network path constraints."
- 第二层，桥接口：`NWProtocolTLS.Options` :: "A container of options for configuring how TLS is used on a connection."——其**公开子项只有 `init()` 与 `securityProtocolOptions`**；后者 :: "The handshake security options TLS uses."，类型 `sec_protocol_options_t`。`NWProtocolTLS` :: "A network protocol for connections that use Transport Layer Security."，仅两个子项（`Options` 与 `Metadata`）。
- 第三层，真正可调项：§5 的 `sec_protocol_options_*` C API（Apple 官方把该页命名为 **Security Options**，collectionGroup 页正文无散文、只列 C 符号——即官方承认的正统入口）。
- iOS 26 补了一层 Swift builder：`Network.TLS` :: "The system definition of the Transport Layer Security (TLS) protocol." / "Supports sending and receiving encrypted byte streams."，方法逐项见 §7.2–7.9（availability 全部 iOS 26／macOS 26）。QUIC 亦有对应入口：`NWParameters.quic(alpn:)`（iOS 15.0 起）与 builder `QUIC`／`QUIC.TLS` :: "The set of TLS options available when using QUIC."

口径：**文档层面 Network 的"TLS 选项"最终都汇流到 `sec_protocol_options_t`**；正文描述任何细粒度能力时先说"经 `securityProtocolOptions` 调 C API（或 iOS 26 起用 `Network.TLS` builder）"，不要给出不存在的 Swift 属性名。

## 7.2 协议版本上下界

C 侧真实函数（逐字）：`sec_protocol_options_set_min_tls_protocol_version(_:_:)` :: "Set the minimum support TLS version."（iOS 13.0；"support" 为原文拼写）、`sec_protocol_options_set_max_tls_protocol_version(_:_:)`、旧的 `sec_protocol_options_set_tls_min_version`（iOS 12.0 **deprecated 13.0**，参数类型 `SSLProtocol`——Secure Transport 遗留枚举，这条弃用线即新旧 API 分界）。版本枚举 `tls_protocol_version_t` :: "The collection of supported TLS and DTLS versions."（iOS 13.0 起），case 逐字含 `TLSv10`（"The TLS 1.0 protocol."，**iOS 15／macOS 12／watchOS 8 起 deprecated**）、`TLSv11`（同 iOS 15 起弃用）、`TLSv12`、`TLSv13`（均 iOS 13.0 起、未弃用），另有 `DTLSv10`／`DTLSv12`。量化基线：**TLS 1.3 自 iOS 13 起可表达；1.0／1.1 的枚举 case 自 iOS 15 起被标记弃用**——正文写"最低版本可设 TLS 1.0"时必须带上这条时间锚。

两个默认值读取器（逐字）：`sec_protocol_options_get_default_min_tls_protocol_version()` :: "Get the system default minimum TLS protocol version."／max 同型。**页面提供函数但不写出默认值本身**（状态：部分核实——不得臆写默认值是 1.2 或 1.3；要实测口径可走 §13.2）。Swift 侧对应 `TLS.version(min:max:)`，其 abstract／discussion 在 JSON 中为空（**部分核实**，语义仅由签名与 C 函数页支撑）。

## 7.3 服务器名称、下一层协议与应用层协议协商

- SNI／校验名（逐字）：`sec_protocol_options_set_tls_server_name(_:_:)` :: "Set the server name to be used when verifying the peer's certificate. **This will override the server name obtained from the endpoint.**"——注意这是双刃：改 SNI 同时改**证书校验目标**，与 §5.1 DNS 名匹配环节直接联动；它也是一手给出的"域名不匹配时可显式覆写"机制（与收紧/放松双向操作配合，正文不写操作步骤，只记语义）。
- ALPN（逐字）：`sec_protocol_options_add_tls_application_protocol(_:_:)` :: "Add an application protocol supported by clients of this protocol instance."；Swift builder `applicationProtocols(_:)` :: "Set application protocols supported by clients of this protocol." ＋ "Application layer protocol negotiation (ALPN) tokens describe the application protocol in use above TLS."。
- 套件（逐字）：`cipherSuites(_:)` :: "Set TLS ciphersuites to the set of enabled ciphersuites."、`cipherSuiteGroups(_:)` :: "Set TLS ciphersuite groups to the set of enabled ciphersuites."，C 侧对应 add／append 四个函数与 `sec_protocol_options_set_tls_diffie_hellman_parameters`（**iOS 13 弃用**）。

## 7.4 流传名字负向取证：现行文档查无此页

以下名字在开发者社区材料中常被当作 Network.framework TLS API 引用，经逐项探测**全部 404**（状态：已核实之负向结论）：`NWProtocolOptions.TLS`、`minimumTLSVersion`、`maximumTLSVersion`、`NWSecurityProtocol`、`requireTLS13Use`、`requiredTLS13Version`、`allowedALPNProtocols`、`additionalTrustedRoots`、`certificateChainVerification`、`certificateTransparencyPolicy`、`domainName`、`ocspResponsesBlockingPolicy`、`policies`／`NWPolicy`、`preSharedKeys`／`NWPSKey`、`tlsMinimumSupportedProtocolVersion`（Network 侧；Foundation 侧存在，见 §8.1）、`NWTLSEnabledEncryptionSchemes`、`sessionTicketKeys`／`sessionTicketsEnabled`、`supportsLocalNetwork`、`trustManagerIdentifier`、`NWTLSTrustEvaluation`、`evaluateServerTrust`、`onMount`。C API 侧同样有一批："set_local_certificates"（真名 `sec_protocol_options_set_local_identity`）、"add_trusted_names"、"set_tls_psk"（真名 `..._add_pre_shared_key`）、"set_ocsp_responses_blocking_enabled"、"set_tls_sct_policy"（真名 `..._set_tls_sct_enabled`）、"set_force_clear_text"、"set_create_secret_block"（真名 `sec_protocol_metadata_create_secret`）。**现行等价物对照表**：版本→`..._min_tls_protocol_version`／`TLS.version(min:max:)`；ALPN→`applicationProtocols(_:)`；票据→`ticketsEnabled(_:)`；信任自定义→`certificateValidator(_:)`／`set_verify_block`；本地身份→`local_identity`；PSK→`add_pre_shared_key`。正文不得把 404 名字当 Apple API 写；评审代码时这份清单可直接用作"名字真伪"筛查。

## 7.5 自定义服务端信任评估回调

两条入口，语义一致：C 侧 `sec_protocol_options_set_verify_block(_:_:_:)` :: "Set the verify block."，回调类型 `sec_protocol_verify_t` :: "Block to be invoked when the protocol instance must verify the peer." ＋ 页内 NOTE（逐字）："this may be called one or more times for a given connection."；Swift 侧（iOS 26）`certificateValidator(_:)` :: "Set a closure to provide custom verification of the peer's credentials during the TLS handshake. This closure may be called multiple times for each connection. It should return `true` if the credentials should be trusted and the handshake should proceed, `false` otherwise." 附全篇最重的官方劝阻（WARNING 逐字）：

> Most apps should not override the default system handling, as doing so can result in insecure network connections and major security vulnerabilities.

与 §2.3/§5.3/§6.4 的口径三方印证；另外"可能被多次调用"意味着**回调必须幂等**——把计数、弹 UI、写日志等副作用放进 verify block 是文档层面的已知坑。

## 7.6 会话恢复、票据、False Start、重协商与降级信号

逐字清单（全部 iOS 12.0 起）：`sec_protocol_options_set_tls_resumption_enabled` :: "Enable or disable TLS session resumption."；`set_tls_tickets_enabled` :: "Enable or disable TLS session ticket support."（Swift: `ticketsEnabled(_:)`）；`set_tls_false_start_enabled` :: "Enable or disable TLS False Start."；`set_tls_sct_enabled` :: "Enable or disable SCT (signed certificate timestamp) support."；`set_tls_ocsp_enabled` :: "Enable or disable OCSP support."；`set_tls_renegotiation_enabled` :: "Enable or disable TLS (1.2 and prior) session renegotiation. **This defaults to `true`.**"（页面罕见的自带默认值句）。降级信号 `set_tls_is_fallback_attempt` 的代码块（逐字，含 MUST NOT）：

> A fallback attempt is one following a previously failed TLS connection due to version or parameter incompatibility, e.g., when speaking to a server that does not support a client-offered ciphersuite. **Clients MUST NOT enable fallback for fresh connections.**

这条与 TLS 降级保护（RFC 8701 语义的 Apple 侧一手表述）呼应：降级重连必须显式声明，新连接禁止打 fallback 标。

## 7.7 预共享密钥

逐字：`sec_protocol_options_add_pre_shared_key(_:_:_:)` :: "Add a pre-shared key (PSK) and its identity to the options."（iOS 12.0）；`set_tls_pre_shared_key_identity_hint` :: "Set the PSK identity hint to use by servers when negotiating a PSK ciphersuite. See https://tools.ietf.org/html/rfc4279..."（iOS 13.0）；`set_pre_shared_key_selection_block` :: "Set the PSK selection block."（iOS 13.0）。PSK 属"用密钥换证书"的信任形态，与 §5 的信任评估链不同路——正文只需立住口径：**Network 路径上 PSK 是一等公民而 ATS 对此完全无感**（ATS 只管 URL Loading System，§2.5），这是"低层 API 责任自负"的具体一例。

## 7.8 对端认证与本地身份：两处默认值口径打架

mTLS 客户端侧唯一入口（逐字）：`sec_protocol_options_set_local_identity(_:_:)` :: "Set the local identity to be used for this protocol instance."（无讨论正文）；类型 `sec_identity_t` 及 `sec_identity_create_with_certificates`。要求对端出示证书：`sec_protocol_options_set_peer_authentication_required(_:_:)` :: "Enable or disable peer authentication. **Clients default to true, whereas servers default to false.**"（iOS 12.0）。而 iOS 26 Swift 枚举 `TLS.PeerAuthentication` 写的是：

> PeerAuthentication specifies how to authenticate the peer end of the connection. For clients, the default is `none`. For servers, the default is `required`.

**两个官方页面的客户端/服务端默认值表述正好相反**（C 页：client 默认 true；Swift 页：client 默认 none、server 默认 required）。本篇纪律：两处并引、标注冲突（进 §14.1 冲突表），以实测与头文件为准，不得只挑一条写。子项逐字：`none` — "Do not authenticate the peer."；`optional` — "Requests the peer certificate, but if none is provided, proceed with the connection." ＋ "This is for servers. Clients will ignore this option."；`required` — "Always authenticate the peer."

## 7.9 早期数据（0-RTT）与上传的相容性

`TLS.earlyDataEnabled(_:)` 的 WARNING 全篇直给上传结论（逐字）：

> Enable early data (0-RTT) for TLS.
> This may have security implications for application data. In particular, TLS early data is replayable by a network attacker. You must account for this when sending data before the handshake is confirmed. See RFC 8446 for more information. You MUST NOT enable fast open without a specific application profile that defines its use.

Foundation 侧对应新属性 `URLSessionConfiguration.enablesEarlyData`（iOS 26／macOS 26，正文文本未取到，**部分核实**）。口径：**0-RTT 数据可被网络攻击者重放是 Apple 一手警告**——任何"握手确认前就发上传体"的设计都在官方明示的重放面内；正文可据此判定"上传通道用 0-RTT 保幂等"不成立，这是本篇"机制与语义必须分开"纪律的典型样本。

## 7.10 可读的协商结果：策略不等于结果

配置读出来不代表协商成那样。`NWProtocolTLS.Metadata` :: "A handle you can use to inspect a connection's TLS state."（→ `sec_protocol_metadata_t`）。一手 getter 清单（逐字名）：`get_negotiated_protocol`／`get_server_name`／`get_negotiated_tls_protocol_version`／`get_negotiated_tls_ciphersuite`／`get_negotiated_protocol_version`／`get_negotiated_ciphersuite`／`get_early_data_accepted`（0-RTT 是否被服务器接受就看这个）／`copy_peer_public_key`；挑战面 `access_peer_certificate_chain` :: "Get the certificate chain of the protocol instance peer."、`access_supported_signature_algorithms`、`access_ocsp_response`；密钥导出 `create_secret(_:_:_:_:)` :: "Export a secret, e.g., a cryptographic key, derived from the protocol metadata using a label string."——即 TLS exporter（EKM），一手依据就在这句（若正文谈"把 TLS 密钥材料派生用于上传签名"，引此句，不引外部概念）。另注：Security 根页 Swift 导入名与 C 名同存（`copy_negotiated_protocol` vs `get_negotiated_protocol`），引用以具体函数页 URL 为准。**审计立场结论：配置项是意图，metadata 才是事实；上传日志应记协商后的版本＋套件＋`early_data_accepted`，而不是记自己设了什么 min/max。**

## 7.11 与 ATS 的关系：更强的默认值、更少的自动护栏

把本章合并回主线：Network 路径的默认协商由 `sec_protocol_options` 的系统默认决定（下限可用 §7.2 的 default getter 读取），支持 DTLS／QUIC／PSK／0-RTT／mTLS 这些 ATS 语义之外的能力，同时**失去** §2.2 的五条扩展检查、§3 的全部键、§4.2 的申报护栏。`NWParameters` 还附带路径约束与隐私项（逐字名，节选）：`requiresDNSSECValidation`、`preferNoProxies`、`setPrivacyContext(_:)`／`PrivacyContext`、`includePeerToPeer`、`allowUltraConstrainedPaths`——注意**没有 `supportsLocalNetwork`**，本地网络权限走 Info.plist/TN3179 口径（§9）。一句话章旨：选 Network.framework 的那一刻，"传输安全要求"就从"系统强制＋ plist 申报"切换成了"开发者自证"——§13 的判定链要为这类连接走分支 B。

# 8. URLSession：最常见的实施面

§2.5 的另一半：URLSession 是 ATS 的主场，因此"App 侧传输安全要求"的多数配置实际上落在这里。本章逐条清点 `URLSessionConfiguration` 与任务层的一手语义，并把"哪些项真的碰传输安全、哪些只碰连通性/调度"分开——这正是 §1.3 声明的"这张表不能回答的问题"在本章的兑现。

## 8.1 会话配置中与传输安全直接相关的项

`URLSessionConfiguration` 页（© 2026）"Setting security policies" 分组**实际列出**的成员（逐字）：`tlsMinimumSupportedProtocolVersion`、`tlsMaximumSupportedProtocolVersion`、`urlCredentialStorage`、`tlsMinimumSupportedProtocol`、`tlsMaximumSupportedProtocol`、`requiresDNSSECValidation`。TLS 两键（均 iOS 13.0／macOS 10.15 起）：

> The minimum TLS protocol version that the client should **accept** when making connections in this session.
> The maximum TLS protocol version that the client should **request** when making connections in this session.

逐字注意 **min 用 "accept"、max 用 "request"** 的动词不对称——下限约束接受面、上限约束 Offer 面，正文不得统一译成"支持"。旧 `SSLProtocol` 版两键（"This property determines the minimum supported TLS protocol version for tasks within sessions based on this configuration."）**iOS 27.2 起弃用**（AVAILABILITY 逐字 "iOS from 7.0 deprecated 27.2"）——旧 API 淘汰线的量化事实。`requiresDNSSECValidation`（iOS 16.0 起）abstract／discussion 在 JSON 中为空，**部分核实**，仅签名可引。此外 `enablesEarlyData`（iOS 26.0，正文为空，部分核实；语义与 §7.9 WARNING 配套）、`allowsUltraConstrainedNetworkAccess`（iOS 26.1）、`usesClassicLoadingMode`（iOS 18.4）均无正文文本。**除这四类外，分组里没有任何其它 TLS 参数**——套件、ALPN、SNI、会话票据在 URLSession 层**无一手属性**，需要它们就必须换 Network 路径（§7），这是一条常被忽略的"选型即安全面"事实。

## 8.2 强制 TLS 1.3 这一项的真实覆盖范围

流传代码里的 `URLSessionConfiguration.requiresTLS13` **不存在**（状态：已核实之负向结论——探测 `…/urlsessionconfiguration/requirestls13.json` 返回 404，且全页 JSON 无 `requiresTLS13` 字样）。现行文档中"强制 TLS 1.3"的唯一一手路径是 **`tlsMinimumSupportedProtocolVersion = .TLS13`**（枚举 case 逐字 "The TLS 1.3 protocol."，iOS 13.0 起）。三点覆盖范围口径：(1) 作用域是**该 configuration 派生的 session**，不是进程级；多 session App 需逐个设置。(2) 它只动版本下界，**不替代** ATS 的证书/密钥长度/前向保密检查（§6.4"地板与期望集合"不变式在会话层同样成立）。(3) 与 `NSExceptionMinimumTLSVersion` 是两套体系：前者按 session、代码配置；后者按域、`Info.plist` 配置且默认值 `TLSv1.2`（§3.8）——**同一个 App 里两者可以打架**，实际下限取谁属未核实（§14.2 缺口），评审时禁止依赖巧合行为。

## 8.3 连接等待、重试上限与服务类型

四项逐字基线：`timeoutIntervalForRequest`／`timeoutIntervalForResource`（分组在列，本轮未逐字取正文，引用前复核）；`httpMaximumConnectionsPerHost` :: "The maximum number of simultaneous HTTP/1.1 connections to make to a given host." ＋ "The default value is `6`." ＋ "**HTTP/2 and later run multiple requests over a single connection and thus ignore this property.**"；`waitsForConnectivity`（iOS 11.0 起）:: "This property is relevant only during the establishment of a connection. If a connection is established and then drops, the completion handler or delegate receives an error, such as `NSURLErrorNetworkConnectionLost`." ＋ "**This property is ignored by background sessions, which always wait for connectivity.**"。`networkServiceType` :: "The type of network service for all tasks within network sessions to enable Cellular Network Slicing."——蜂窝网络切片需 entitlements（`5G Network Slicing App Category`／`Traffic Category`）＋属性双设，缺一不生效（逐字 "your apps network connections won't be using Cellular Network Slicing"）。这些全部是**连通性/调度层**参数，不改变传输安全等级——但它们决定失败形态（立即失败 vs 等待 vs 掉线报错），进 §13 判定链的"失败归因"分支。

## 8.4 凭据、挑战与客户端证书

§5.2 的会话级/任务级划分在此收口。补充一手：session-wide 挑战处理成功后的效果句（逐字）"your action remains in effect for all tasks created from that `URLSession`"；`URLCredential.Persistence.forSession` 作用域句（逐字）"only stored by the URLSession instance that created the task"。mTLS 客户端证书在 URLSession 路径的入口是凭据形态（`URLCredential(forIdentity:)` 存在于 developer 侧，本轮未逐字取页 → **部分核实**）；TN2232 明确不管这块（§5.1 Note 句）。`urlCredentialStorage` 在 security policies 分组内，但页面仅列出键名、本轮未取到逐字正文。评审要点：**同一 session 里第一个请求的信任决定会污染后续全部请求**——上传复用 session 时，任何"开发期放宽"（接受自签名）若留在生产代码路径，作用域是整个 session 的全部任务，这正是 §5.3 官方 WARNING 强调"不要 ship 给最终用户"的机制原因。

## 8.5 下一层协议协商与 HTTP/3、QUIC

URLSession 层**没有** ALPN／HTTP/3 开关属性（§8.1 分组全清单为负向证据）；QUIC 的官方入口在 Network 层（`NWParameters.quic(alpn:)` iOS 15.0 起、builder `QUIC`／`QUIC.TLS`，§7.1）。HTTP/2+ 对连接数的影响逐字见 §8.3。因此正文口径：**"App 用没用 HTTP/3"在 URLSession 路径上不由开发者声明、由协商决定**——与 §7.10"策略不等于结果"同构；可观测手段只有 metadata/网络侧痕迹（§13.2）。QUIC 语义与上传的关系（可靠/不可靠流、0-RTT 重放面）在 developer 页无逐文散文，标缺口；#91 §QUIC 章节口径可交叉引用但不得互相冒充一手。

## 8.6 后台会话与上传任务的运行条件

后台会话的进程模型（`background(withIdentifier:)`，iOS 8.0，逐字）：

> A configuration object that causes the system to perform upload and download tasks in a separate process. ... A session configured with this object **hands control of the transfers over to the system, which handles the transfers in a separate process**. In iOS, this configuration makes it possible for transfers to continue even when the app itself is suspended or terminated.

终止语义（逐字）："If the user terminates the app from the multitasking screen, the system cancels all of the session's background transfers." 且系统不会自动重启被强杀的 App。调度语义：`isDiscretionary` :: 默认 `false`；对后台配置"give the system control over when transfers should occur"；**App 在后台时启动的传输，系统一律按 discretionary 处理**（逐字 "the system assumes this property is `true` and ignores any value you specified"）；大流量传输官方鼓励设 `true`。`sessionSendsLaunchEvents` :: 默认 `true`（完成后唤醒/启动 App，走 `application(_:handleEventsForBackgroundURLSession:completionHandler:)`）。上传任务本体（`URLSessionUploadTask`，逐字）："A URL session task that uploads data to the network in a request body."，用于 POST/PUT；NOTE :: "Unlike data tasks, you can use upload tasks to upload content in the background." ＋ 关键机制句 "**In iOS, when you create an upload task for a file in a background session, the system copies that file to a temporary location and streams data from there.**"——临时副本的位置与保护属性见 §8.8。

上传的三种喂数据形态与续传（三篇 "Uploading" 实操文，全部 © 2026 逐字）：内存数据（`uploadTask(with:from:)`，HTTP 头自设但 "**except the `Content-Length` header. The session figures out content length automatically from the size of your data.**"）；流（`uploadTask(withStreamedRequest:)` + `urlSession(_:task:needNewBodyStream:)`，bound pair 双流、示例缓冲 4096 字节）；文件（`uploadTask(with:fromFile:)`）。可恢复上传（iOS 17 起，逐字）："New in iOS 17 and aligned OS versions, `URLSession` supports resumable uploads according to the `latest resumable upload protocol draft`. This protocol is currently being developed in the HTTP Working Group at the IETF. To take advantage of resumable uploads, **the server you're connecting to must also support this protocol**."；暂停 = `cancel(byProducingResumeData:)`；IMPORTANT 框逐字："uploads that use a background configuration handle resumption automatically, so manual resuming is only needed for non-background uploads"；瞬时中断且服务器仍可达时 URLSession 自动续传；失败后从 `userInfo[NSURLSessionUploadTaskResolver… ]` 更正：`error.userInfo` 键 `NSURLSessionUploadTaskResumeData`／便捷属性 `URLError.uploadTaskResumeData`。`uploadTask(withResumeData:)` 的讨论段另含文件前提："If the upload file is from a file URL, the file must still exist and be unmodified."

## 8.7 蜂窝与计费网络访问三项开关

逐字基线：`allowsCellularAccess` :: 默认 `true`；`allowsExpensiveNetworkAccess`（iOS 13.0）:: "iOS 13 considers most cellular networks and personal hotspots expensive"，无可用非计费接口且本项为 `false` 时任务失败、错误带 `networkUnavailableReason = NSURLErrorNetworkUnavailableReasonExpensive`；`allowsConstrainedNetworkAccess` :: 对应用户侧"低数据模式"，失败原因 `...Constrained`。官方 TIP（逐字）："Prefer basing your app's policy logic around the `allowsConstrainedNetworkAccess` property rather than this one. People using your app can use the 'Low Data Mode' setting to set the constrained status..."组合模式亦有原文：两个访问标志设 `false` ＋ `waitsForConnectivity` 设 `true` ＝"waits for a suitable interface to become available before sending or receiving data"。测试钩子逐字："Settings > Developer > Network Override"。正文定位：这三项是**目的地选择/成本控制**，与传输安全正交；但在"上传何时发生"的审计里必须先排除它们——低数据模式下"上传没发生"不是故障也不是保护。

## 8.8 上传任务与数据保护类的耦合点

上传文件在设备侧的落点有两处一手可查、一处必须标缺口：

- `URLFileProtection`（© 2026）摘要逐字："Protection-level values for a URL resource key."——即通过 `URLRequest` 的资源值键为上传文件指定文件保护档位（各档定义属 #89/#91 取证范围，此处交叉引用）。该页 Overview 正文未能逐字复原（DocC 抽取缺陷），**部分核实**。
- 后台会话的文件副本机制句（§8.6 逐字："the system copies that file to a temporary location"）——临时副本位于受管理后台会话存储，其保护档位**未找到任何 Apple 一手页直述**（缺口 N："NSFileProtectionComplete 是否阻断后台上传／background session 用哪一档保护"）。正文只能写成推断："上传中间副本的保护级别不由 `URLFileProtection` 键保证，锁定屏幕时后台上传能否继续**无一手因果文本**"，禁止断言。
- CloudKit 附件侧的对应物（`CKAsset` 摘要逐字 "An external file that belongs to a record."，用途句逐字："Use assets to incorporate external files into your app's records, such as photos, videos, and binary files. Alternatively, use assets when a field's value is more than a few kilobytes in size."）——staging 位置属性名未取全，**部分核实**；U2 方向的完整口径在 §11.3/§11.5。

一句话章旨：**URLSession 把"要求"折叠成了默认值，把"选择"折叠成了 session 生命周期**——§8.4 的信任决定污染全 session、§8.6 的一次拷贝改变文件保护语义、§8.2 的一行下界与 plist 键的潜在打架，都发生在开发者最少阅读的配置面上。这也是 §13 自查清单要求"session 创建点集中、逐 session 记录 TLS 参数"的原因。

# 9. 本地网络权限与出站可见性

本地网络是 U3 方向在 App 侧最像"上传"的形态：数据不跨互联网，但跨设备。iOS 14 起这条边界要单独授权，而它的授权体系与 ATS 的本地网络例外（§3.5）是**两套互不替代的机制**。本章取证补充说明：以下 plist 键语义与"Local Network TLS"文章文本为本章现场抓取（DocC JSON 端点，© 2026），不在两份前置简报内，逐字标注照旧。

## 9.1 为什么局域网访问需要单独授权

一手入口是 `NSLocalNetworkUsageDescription`（Privacy - Local Network Usage Description，String；AVAILABILITY 逐字：iOS 14.0／iPadOS 14.0／macOS 11.0／tvOS 14.0／visionOS 1.0）。抽象句（逐字）：

> A message that tells people why the app is requesting access to the local network.

讨论段给出了官方对"什么算使用本地网络"的最宽界定（逐字）：

> Any app that uses the local network, directly or indirectly, should include this description. This includes apps that use Bonjour and services implemented with Bonjour, as well as direct unicast or multicast connections to local hosts.

"directly or indirectly"与"unicast or multicast"两个短语把间接调用（SDK 内嵌发现逻辑）与单播都纳入了范围。授权单独存在的动机在本报告体系中由两条已核实事实支撑：本地网络上的对端可被用于**跨网络关联与设备指纹**——#91 §6.2 与本章 §9.6 的私有无线局域网地址机制（三档模式＋56 位 TSF 偏移随机化）正是同一威胁面的链路层对应物；以及本地服务往往**用不上海网 CA 证书**，信任评估需要专门设计（§9.4）。

## 9.2 用途描述键与服务声明键

第二个键是 `NSBonjourServices`（抽象句逐字）：

> Bonjour service types browsed by the app.

讨论段逐字：

> The value associated with this key is an array of strings that represent Bonjour service types. Include all service types that your app expects to use. Bonjour service type strings look like `_ipp._tcp`, and `_myservice._udp`, where the first substring identifies the application protocol and the second identifies the transport protocol.

两键分工：**描述键管"人看到什么"（授权弹窗文案），声明键管"能发现什么"（服务类型白名单）**。注意一手文本只说 "Include all service types that your app expects to use"，通配符 `"*"` 的匹配规则在本页未出现（流传说法，**未核实**，评审时不要当一手依据）。另注意拼写取证细节：DocC slug 为 `nsbonjourservices`（`nsbonjour-services` 404）——名字真伪筛查同样适用于 URL。

## 9.3 触发条件、拒绝后果与可观测状态

触发面由 §9.1 引用句界定（Bonjour 浏览、单播／组播直连、间接使用）。授权后的运行边界由 `NWParameters` 的真实属性表达（名字逐字取自 NWParameters 页分组，§7.11）：`includePeerToPeer`、`acceptLocalOnly`、`requiredLocalEndpoint`。拒绝后果的精确错误面（哪个 `NSURLError`／是否静默超时）在现行 developer 文档**无逐字专页**，标缺口；但"本地网络 TLS"文章给了信任面的可观测样本——未装根证书时客户端报（逐字示例日志）：

> // [BoringSSL] ... SSL routines:OPENSSL_internal:CERTIFICATE_VERIFY_FAILED

这条样本的价值在于**把"权限被拒"与"信任失败"分开**：后者有明确的 BoringSSL 握手错误与 §5.1 `-1202` 面，前者属连接建立前的策略拦截，审计时不可混记。系统设置侧用户可查看与管理本地网络开关（Settings > Privacy > Local Network，属用户界面事实，本轮未逐字取页，标部分核实）。

## 9.4 与 ATS 本地网络例外的分工

一张表说清两套体系（全部一手依据在前）：

| 维度 | 本地网络权限（本章） | `NSAllowsLocalNetworking`（§3.5） |
|---|---|---|
| 管什么 | **能不能**发现/连接局域网对端 | 连本地目标时**TLS 要求放不放松**（非限定域名、`.local`、IP） |
| 载体 | 运行时授权＋两个 plist 键 | ATS 字典内布尔键 |
| 默认 | iOS 14 起未授权即不可访问 | iOS 17 起 IP 直连默认**不再允许**，需逐 IP/CIDR 进例外域 |
| 相互替代 | 否——有了权限仍要满足 ATS；有了 ATS 例外仍要被权限拦 | 否 |

同一条连接要同时过两道闸，缺一即失败，且症状不同（策略拦截 vs ATS 硬失败）。**本地侧 TLS 的正解不是放宽而是补锚**：文章给出官方推荐形态（逐字）——"distribute the root certificate to all of the iOS client devices ... so that they can establish the chain of trust during the handshake process, **and trust evaluation doesn't need to be overridden**"；NOTE（逐字）："If the root certificate from the CA is installed on the device, you can use the code above without implementing `sec_protocol_options_set_verify_block(_:_:_:)` to override trust evaluation."对 IP 直连还给出证书侧要求（逐字）："the server needs to use a leaf certificate that lists the IP in the 'IP Address' field of the Subject Alternative Name"（与 §5.1 SAN-IP 句、§3.6 NOTE"域名例外与 IP 例外互不通用"三处一致）。对"装不了根证书"的场景，官方给的绕行是 verify block 自建校验，并当场定性（逐字）："This isn't the recommended path, but could be done in extreme cases."——示例代码里 "Cannot do hostname here because of IP." 的注释也是同一语义：按 IP 连就没有主机名可查，主机名匹配这一维只能放弃、改查证书本身。

## 9.5 组播、广播与点对点能力的边界

一手可立的三条：(1) `NWConnectionGroup` **支持组播但明确不含广播**（#91 简报 §254 行逐字口径，本页交叉引用）；(2) 点对点 Wi-Fi 面（隔空投送/隔空播放/personal hotspot）的地址由系统随机生成，每次建立接口都换新地址（§9.6 引用的无线局域网隐私页逐字）；(3) 组播/点对点流量同样落在本地网络权限的 "direct unicast or multicast" 触发范围内（§9.1 逐字）。正文边界句：**App 能声明"我要用组播"（服务类型＋权限），不能声明"我要用广播"（无一手 API）**；跨互联网方向的多播在 ATS/URLSession 层没有任何键可表达，属未核实主题。

## 9.6 地址随机化与流量关联

出站可见性的链路层一半在这里（手册《Apple 设备的无线局域网隐私》`sec31e483abf`，2024-12-19，要点已在 §4.4/前篇取证，此处摘三条硬事实）：私有无线局域网地址自 iOS 14 起可用、自 iOS 18 起按网络三档选择（关闭／固定／轮替），**"固定"档默认绑定 WPA2 及以上的安全网络、"轮替"档默认绑定 WPA/OWE/WEP/受限门户/开放网络**；点对点和热点接口的 MAC 每次新生成、TSF 起始偏移量 56 位随机化（防用 TSF 值破解地址随机）；**"此功能可通过设备管理服务关闭"**，关闭后系统在网络里显示隐私警告——组织侧能改变设备的网络标识，这是"连接要求随设备状态变化"的又一条实证。隐藏网络方向的口径同样逐字：设备只在网络已隐藏时才发送含 SSID 的探测请求，"这可帮助防止设备广播用户之前接入过的隐藏网络的名称"。对上传审计的含义：**同一 App 的两次"局域网上传"在 Wi-Fi 层可能呈现完全不同的设备标识形态**，链路层观测不能替代传输层证据（§5.7、§13.2 的记录点才是同一主体判定的依据）。

# 10. 谁能改变一个 App 的连接要求

本章不做新取证，把 §2–§9 的能力面按"主体"重新切一遍——因为审计现场最常见的错误不是不懂机制，而是**找错责任主体**：把 App 配置问题报给 IT，把系统版本问题报给开发者。每个主体的能力都给出"能做到／做不到／可观测性"三列口径，出处全部为前文编号。

## 10.1 开发者：配置文件与代码两条通道

能做到：改 `Info.plist` ATS 字典（§3 全目录：四条全局键、例外域四键、NIAP 两键、`NSPinnedDomains`）；改会话与连接参数（§8.1 TLS 上下界、§7 全部 C API/builder）；接管信任评估（§5.2/5.3、§7.5）；换 API 路径从而整体离开 ATS（§2.5——**这是开发者最大的"能力"也是最隐蔽的风险**）。做不到：让 ATS 在受保护域上接受低于要求的连接（§2.3 硬失败、§5.3 只能收紧）；绕过审核申报（§4.2 五键清单）；对用户已授予的权限再降一等（§9 权限体系不由 plist 控制）。可观测性：这一切都落在 App 二进制与其配置文件里——**设备侧静态可审计**（`Info.plist` 可直接解出，代码路径可由日志证）。

## 10.2 用户：根证书、本地网络开关与锁定模式

能做到：安装/信任描述文件与根证书（改变锚集合，§5.4；`allowUntrustedTLSPrompt` 未关闭时还有"仍要连接"的历史弹窗面）；授予/拒绝本地网络权限（§9）；开**锁定模式**（§4.4 末：断不安全 Wi-Fi、关 2G/3G、限制 WebKit 内容、阻描述文件安装）；关 Wi-Fi/蜂窝/热点改变物理路径。做不到：改 App 的 ATS 键（无用户界面）；改系统默认 TLS 版本。可观测性：锁定模式状态与"排除"列表在设备侧可见但 App 无 API 直接读取（未核实是否有运行时信号，标缺口）；用户的证书信任决定会体现在系统信任库，属设备侧可审计面。

## 10.3 组织：配置描述文件、VPN、代理与内容过滤

能做到（全部 §4.4/§5.4/§6.5 一手）：改可达性与路径（VPN/per-app VPN/AlwaysOn/Relay/代理/DNS/`forceWiFiToAllowedNetworksOnly`）；改信任锚与撤销行为（装根、CT 载荷按域降低、`allowUntrustedTLSPrompt=false` 收紧、`allowOTAPKIUpdates=false`）；断上传目的地（五个 iCloud 开关）；插层观察自家域（监督设备＋代理/内容过滤）；收设备网络标识（MDM `AccessRights` 位 32 可查电话/SIM/MAC）。做不到：**放宽任何一条 ATS 要求（210 键零命中，负向取证）**；强制开/关锁定模式（"并不是一个可由系统管理员配置的移动设备管理选项"，逐字）；对 Apple 服务域做 SSL 检查（Apple 服务将中断此类连接，逐字）；改任何 App 的 `Info.plist`。可观测性：下发什么载荷、何时下发，在设备"已安装描述文件"面与 MDM 服务端日志双向可查——审计时先取这个时间线，再解释连接行为突变。

## 10.4 Apple：系统版本、策略、撤销列表与审核

能做到：随 OS 推进默认值（§2.1 ATS 按链接 SDK 生效、§4.3 iOS 17 起 IP 默认禁止、§7.2 TLS 1.0/1.1 枚举 iOS 15 起弃用、§8.1 旧 SSLProtocol 键 iOS 27.2 弃用——**要求强度是系统版本的单调函数，这与"配置放松"方向相反**）；维护内置根与撤销聚合清单；按申报处理例外（§4.2）；对 Apple 自家服务改 TLS 政策（如 CT 强制化、iOS 26 起 0-RTT 开关）。做不到：不替单个 App 改配置，也（按现行文本）不因审核指南另设传输安全条款（§4.1 零命中）。可观测性：每次 OS 升级都是一次"要求变更事件"，§13.5 的回归监测针对的正是这个主体。

## 10.5 对端服务器：协议、套件、证书链与后量子支持

服务器决定连接"能"协商成什么：支持的 TLS 版本集合、是否支持 PFS 套件、证书链完整性与有效期、SAN 是否覆盖实际连接名/IP（§5.1 五类失败里三类是服务器侧问题）、CT 日志收录状态、以及后量子密钥协商的支持（口径见 #91 后量子章，交叉引用）。**服务器能力清单 ≠ App 合规清单**（§2.4）：服务器支持 TLS 1.0 不代表任何 ATS 路径允许用它；服务器换新 CA 不代表固定方案跟得上（§6.6）。可观测性：用 `nscurl --ats-diagnostics`（§3.12 已给）与 §7.10 的 metadata 实测面，把"服务器实际给了什么"记录在案。

## 10.6 主体—能力—可观测性速查表

| 主体 | 改变连接要求的杠杆 | 关键一手出处 | 审计第一动作 |
|---|---|---|---|
| 开发者 | `Info.plist` ATS 键／API 选型／会话与连接参数 | §3、§7、§8 | 解包读 `Info.plist`＋枚举 session 创建点 |
| 用户 | 证书信任／本地网络权限／锁定模式 | §5.4、§9、§4.4 | 查锁定模式与已装描述文件/证书 |
| 组织 | 路径·锚·目的地·插层（不可碰 ATS） | §4.4、§5.4、§6.5 | 拉 MDM 载荷下发时间线 |
| Apple | OS 版本推进默认值、撤销/根清单、申报处理 | §2.1、§4.2、§7.2 | 对齐故障与 OS 升级窗口 |
| 服务器 | 可协商集合（版本/套件/链/CT/PQC） | §5.1、§10.5 | `nscurl` 诊断＋协商结果留痕 |

## 10.7 配置描述文件不能做到的事

把最容易被错误期待的一条单独收口（全部为负向取证，出处 §4.4）：**没有任何配置描述文件载荷可以放宽 ATS、按域关闭 ATS、降低 TLS 版本/前向保密/密钥长度/证书签名算法要求**。描述文件能做的是网络路径、信任锚、目的地开关、流量可见性四类；其中唯一"降低要求"的官方措辞属于 CT 载荷且只降 CT。与之对称的开发者侧误区是反过来把 ATS 当万能护栏（§2.5：低层 API 完全不经过它；§4.1：它也不是审核条款）。**"描述文件救不了 ATS 失败，ATS 也背不了整条链路的锅"——这两句合成本章全部结论。**

# 11. 三个上传方向：要求如何随方向改变

§0.3 定义的 U1（自建服务端）、U2（iCloud/CloudKit）、U3（对端设备）在本篇的视角下各不相同：U1 的"要求"由 ATS/信任评估＋服务器能力决定；U2 的"要求"变成**密钥归属问题**；U3 的"要求"叠加本地网络权限与逐条加密。章线提醒：出口合规页的 "upload"（提交 App 到美国服务器）与本章的"上传"（用户数据离机）是两个动作，全部引文已分开。

## 11.1 U1 服务端上传：谁负责上传前的加密

一手能立住的边界是三句：ATS 只管"HTTP 连接"这一层的 HTTPS 与扩展检查（§2.2），URLSession 的职责到"negotiate the most secure connection available from the server"为止（§2.5 逐字），TLS 的两条保证到对端服务器为止（§5.1 on-the-wire privacy + client-authenticates-server）。合起来的推论（机制结论，非某页原话）：**U1 方向上，"数据离开设备后服务器是否可见明文"完全由 App 决定**——ATS 体系内没有任何键/检查涉及"上传前内容加密"；服务器侧落盘、日志、内部流转、备份全部在射程外（§3.12 末条误用的机制版）。申报面反而是齐的：系统内置加密走 HTTPS 属通常豁免（§4.3 逐字），第三方库一并计入判定。评审 U1 链路时的正确问法不是"用了 TLS 没有"而是"**除了 TLS 还有没有一层，那层的密钥谁管**"。

## 11.2 先加密后上传的设计范式与密钥分发

Apple 文本里"先加密后上传"的一手范式句来自《iCloud 安全性概览》（`secacde2d0da`，2022-12-07，逐字）：

> **大部分 iCloud 数据首先会在用户设备上通过设备生成的 iCloud 密钥加密，然后才上传到 iCloud 服务器。**对于未采用端对端加密的数据，用户设备会将这些 iCloud 密钥安全地上传到 Apple 数据中心的 iCloud 硬件安全模块。……**在用户设备和 iCloud 服务器之间传输的数据会通过 TLS 单独加密，且 iCloud 服务器会使用额外的加密层来储存静态用户数据。**

三层顺序（设备加密→密钥分档→TLS 另加一层）是官方给出的完整栈，正文不得简写成"Apple 掌握密钥"或"Apple 完全看不到"；最后一句同时反驳"有静态加密就不需要 TLS"的错误推论——**存储加密与传输加密是两层独立机制**。#91 已取证的两处同构范式（iMessage 附件"先 AES-CTR 加密再上传、密钥随 U3 通道分发"；CloudKit"记录密钥先在受信任设备生成并封装到密钥层级，然后才会上传数据"）与此互证：Apple 自家服务的默认形态就是信封加密＋通道保护分离。第三方 App 在 U1 上复制这一形态时，密钥分发面（谁能解开上传物）成为独立设计题，ATS/传输层帮不了它——这正是"上传是密钥归属权的重新分配"（#91 章旨）在 App 侧的落点。

## 11.3 U2 iCloud 容器、云盘与键值同步

手册对第三方 App 的 U2 面只有一行总纲（逐字）："它也可以供第三方 App 使用，来储存和同步文稿以及由开发者所定义的 App 数据关键值。"取证级负面事实：**手册没有独立的"iCloud 云盘安全性"或"键值同步加密档位"页**（178 条 security 目录＋secid 全清单核对，状态：已核实的缺失）；云盘传输口径只能落到 §11.2 两句。键值同步在手册原词是"App 数据关键值"——命名纪律：正文别用流传词"键值对存储"冒充一手术语。组织侧能力对偶成立："IT 管理员可以使用来自设备管理服务的配置描述文件关闭部分 iCloud 功能，例如 iCloud 云盘和 iCloud 云备份"（§4.4 五开关）——**能断目的地，不能改上传加密强度**。

## 11.4 CloudKit 数据库类型与公共库的明文口径

数据库三型（私有/共享/公共）与安全语义的对应，一手句在 #91 §5.1（`sec3cac31735` CloudKit 加密页，交叉引用，此处不复述）。本篇补两刀常被忽略的：(1) 公共数据库的"写入即对读取者可见"属明文口径——传输再安全也不改变"目的地是公开容器"这一事实，U1/U2 判定里容器类型先于通道类型；(2) 即使开了高级数据保护，ADP 页逐字承认"即使打开'高级数据保护'，iCloud 也会储存部分不受用户特定 CloudKit 服务密钥保护的数据"——U2 不是全有全无，见 §11.8 的逐项清单。

## 11.5 记录密钥、两类服务密钥与 App 的选择权

服务密钥分档的一手定义（ADP 页 `sec973254c5f`，2022-12-07，逐字节选）：

> 在设备上生成且随后上传到 Apple 数据中心的认证后可用 iCloud 硬件安全模块 (HSM) 的所有 CloudKit 服务密钥会从这些 HSM 中删除，并且会完全保存在 Apple 账户的 iCloud 钥匙串保护域中。其处理方式和现有的端对端加密服务密钥一样，这意味着 Apple 不可再读取或访问这些密钥。**"高级数据保护"还会自动保护第三方开发者选择标记为加密的 CloudKit 字段以及所有 CloudKit 资源。**

> **在容器的模式中，CloudKit 记录字段必须明确声明为加密才能受保护，而读取和写入加密字段需要使用专门的 API。**

两条 App 侧选择权由此立住：**字段级加密是开发者的显式决定**（"必须明确声明"逐字），推翻"数据进了 CloudKit 就端对端加密"的流传说法；`CKAsset` 资源则被 ADP 统一覆盖（"以及所有 CloudKit 资源"逐字），资源与记录的存取耦合见 `CKAsset` 页（逐字："When you save a record that has an asset, CloudKit saves both the record and the asset to the server."；文件名等文件系统元数据不由 CloudKit 存放，需自设字段——"CloudKit stores only an asset's data."）。"打开 ADP"这个用户动作本身的机制是一次**服务器侧密钥删除**、关闭是一次**密钥重新上传**（support 102651 逐字："关闭这项功能后，你的设备会安全地将所需的加密密钥上传到 Apple 服务器"）——U2 的保密等级随用户开关来回变化，App 若在合规文档里承诺"用户数据端对端加密"，必须把这两档写成条件句。

## 11.6 U3 对端上传的 API 面与弃用状态

对端方向现存的 App 可用一手面：本地网络权限＋Bonjour 声明（§9.1/9.2）、`NWConnectionGroup`（支持组播、**明确不含广播**，#91 取证）、点对点参数（`includePeerToPeer`/`acceptLocalOnly`，§9.3）。已被官方移出面的是 MultipeerConnectivity——2026 年弃用（口径出处 #91 §API 选型章；本篇交叉引用，注意其弃用不影响系统服务自身的对端通道——隔空投送/接力的逐条加密在 #91 §9–§12）。U3 的"要求"与前两方向的本质差异：对端往往**没有公有 CA 证书**，§9.4 的"装根补锚"是官方推荐解，§9.4 末的 verify block 是官方定性的非常规解（"isn't the recommended path"）。逐条消息级加密（iMessage/FaceTime）属系统服务协议栈，App 无 API 复用之——正文按"不可用"写，出处即"无一手页存在该能力入口"（缺口型负向）。

## 11.7 上传中间文件在设备侧的保护类可用性

上传链路在设备侧至少产生两类中间物：App 自选的暂存文件、后台会话的系统副本（§8.6 逐字 "the system copies that file to a temporary location"）。前者由开发者用文件属性键控档（一手四档逐字，`FileProtectionType`，© 2026）：

> `complete` — The file is stored in an encrypted format on disk and cannot be read from or written to while the device is locked or booting.
> `completeUnlessOpen` — The file is stored in an encrypted format on disk after it is closed.
> `completeUntilFirstUserAuthentication` — The file is stored in an encrypted format on disk and cannot be accessed until after the device has booting.（原文 "until after the device has booted"）
> `none` — The file has no special protections associated with it.

键名逐字：`protectionKey` :: "The key in a file attribute dictionary whose value identifies the protection level for this file."；URLRequest 资源侧 `URLFileProtection` :: "Protection-level values for a URL resource key."。两个必写口径：(1) **`completeUnlessOpen` 的档位语义按句界翻转**——"文件一经关闭即加密"，意味着**上传进行中的文件天然处于可读窗**，锁屏拦不住正在被自己打开的上传暂存文件；(2) 后台会话系统副本落在哪一档、`complete` 类文件能否被后台进程继续上传，**没有任何 Apple 一手页直述因果**（状态：缺口，正文只能写成"推断/未知"，禁止断言"锁屏后台上传必然失败"）。

## 11.8 元数据、路由信息与流量可见性

上传"内容之外"的可见面有两套一手清单。云端侧（ADP 页逐字汇总，即"开了 ADP 仍退回标准数据保护"的五项）：文件/对象的**修改日期与时间**（排序用）、文件与照片数据的**校验和**（去重用，官方点名"这项广为人知的技术称为收敛加密"，且"此元数据会始终加密，但加密密钥由 Apple 采用标准数据保护进行储存"）、共享操作的**标题与示意缩略图**、"任何拥有链接的用户"式共享（"服务器需要能够为打开 URL 的任何用户提供访问权限"）、iWork **实时协作**（"文稿的加密密钥会安全上传到 Apple 数据中心的服务器"）。网页访问另有一个可引的临时模型：一小时内按需上传单个服务密钥、用与网页会话绑定的临时密钥加密、设备端弹通知显示哪项服务临时可访问——**反驳"开启 E2EE 后 Apple 在任何情况下都无法解密"必须用这条**。网络路径侧：代理/内容过滤可见域名与元数据（§6.5，前置条件=监督/受管理），撤销校验出网主机暴露"你在连谁"（§5.5），设备主动外拨有一手确认（101555 逐字："与下列主机之间的网络连接是由设备（而不是 Apple 运营的主机）发起的"）。审计口径：**上传的保密性评审必须分内容、元数据、路由三层各问一遍"谁能看到"**，任何一层用"传输已加密"打发都是不合格。

## 11.9 上传失败与降级重试对要求的影响

三类"重"的语义必须分开：(1) **连通性重**——`waitsForConnectivity` 等待、后台会话恒等待（§8.3 逐字 ignored 句）、瞬时中断由 URLSession 自动续传（§8.6 逐字）；(2) **协议降级重**——TLS 层的 fallback 有 MUST NOT 约束（§7.6 逐字 "Clients MUST NOT enable fallback for fresh connections"），这是防"失败后降版本重连"被中间人利用的一手护栏；ATS 路径本身不给降级：§2.3 硬失败语义下，扩展检查不过就是不过，重试不改变结果，改变结果的唯一途径是 App 配置（§3）或服务器升级。(3) **业务重**——App 自己写的失败重试逻辑。风险恰好集中在第三类与 §2.5 的交叠处：走低层 API 的 App 为了"传得上去"自行放宽验证（换 min TLS、装 verify block 恒真、接受自签），**在 ATS 视野外发生、在申报体系里也不留痕**（§4.2 管的是 plist 键）。因此自查清单里对应一条硬性检查：任何失败重试路径不得修改会话/连接的 TLS 参数——参数在创建时定死，重试只复用（工程建议，非一手原话，正文按建议标注）。

# 12. 平台差异与 API 分裂

"iOS 上的传输安全要求"其实是一个平台族的要求，各族的适用范围、API 可用性与管理面都不同。本章把散落在各页 availability 行与手册页里的差异集中登记，供 §13 判定链按平台分支。

## 12.1 ATS 的适用平台与最低编译目标

一手生效句只有两个平台名（§3.1 Versioning 逐字）："ATS operates by default for apps linked against the iOS 9.0 or macOS 10.11 SDKs or later. When you link your app against an older SDK, ATS is disabled no matter which version of operating system your app runs on." **tvOS／watchOS／visionOS/iPadOS 均未出现在这句里**（iPadOS 页名晚于该句写成，实务上随 iOS；其余三者 ATS 是否按同样 SDK 判据生效，现行页无逐字表述——缺口 N 项）。键级平台标注可部分补位：`NSAllowsLocalNetworking` 逐字提 iOS/iPadOS/macOS；`NWParameters.tls` 的 availability 行逐字含 **watchOS from 5.0／visionOS from 1.0／tvOS 12.0**（§7.1）——即"低层 API 在这些平台存在"有一手，"ATS 在这些平台的强制细节"没有。**口径纪律：正文写"ATS 适用于所有 Apple 平台"属未核实外推；适用范围只写 iOS/macOS（一手句）＋ iPadOS（随 iOS，注明为推定）。**

## 12.2 API 可用性矩阵

| 能力 | 一手 availability（逐字要点） | 出处 |
|---|---|---|
| ATS 默认生效 | linked SDK ≥ iOS 9.0／macOS 10.11 | §3.1 |
| `NWParameters.tls`、`sec_protocol_options` 桥 | iOS 12.0／macOS 10.14／watchOS 5.0／tvOS 12.0／visionOS 1.0 | §7.1 |
| `tls_protocol_version_t`、min/max 新函数 | iOS 13.0／macOS 10.15／watchOS 6.0 | §7.2 |
| TLSv10/11 枚举 **deprecated** | iOS 15／macOS 12／watchOS 8 起 | §7.2 |
| `NWParameters.quic(alpn:)` | iOS 15.0／watchOS 8.0／visionOS 1.0 | §7.1 |
| `tlsMinimumSupportedProtocolVersion`（URLSession） | iOS 13.0／Mac Catalyst 13.1 | §8.1 |
| 旧 `tlsMinimumSupportedProtocol` **deprecated** | iOS 27.2／macOS 27.2 起 | §8.1 |
| 可恢复上传 `uploadTask(withResumeData:)` | iOS 17.0／macOS 14.0／watchOS 10.0 | §8.6 |
| `Network.TLS` builder、`enablesEarlyData` | iOS 26／macOS 26 | §7.2/§8.1 |
| 锁定模式 | iOS 16／iPadOS 16／macOS 13／**watchOS 10** 起（visionOS 未列） | §4.4 |
| CT 要求（"以使 TLS 证书得到信任"） | iOS、iPadOS、macOS、Apple tvOS、**watchOS 10、visionOS 1.1** | 手册 CT 载荷页 |
| 私有无线局域网地址三档 | iOS 18／macOS 15（=26 线版本号并存，见 §14.1） | 手册无线隐私页 |

矩阵的两条读法提醒：数值全部来自 availability 行/手册正文，**"未列平台"一律读作"未取证"而不是"不支持"**；watchOS 上 `NWParameters` 可用但 ATS 键的 watchOS 适用性未取证——低层路径与强制层的关系在 Watch 上是文档空白，评审 Watch App 时按"自证责任"处理。

## 12.3 macOS：应用沙盒网络权限、防火墙与代理

三条已核实差异：(1) **内建防火墙只管入站**——手册《macOS 中的防火墙安全性》（`seca0e83763f`）五个配置项全部限定传入连接，"任何'macOS 防火墙管出站'的说法在本简报来源里无据"（负向取证，正文写"防火墙可拦出站上传"必须标缺口）；配置途径含防火墙有效负载/描述文件（组织可改入站策略）。(2) **用户信任设置在 macOS 有 UI、iOS 没有**：`kSecTrustResultProceed` vs `Unspecified` 的区别逐字限定 "on OS X the user can use Keychain Access to create trust settings"（§5.4）；`SecCertificateCopyValues` 等 TN2232 工具也标注 OS X-only。(3) 系统侧的透明代理与中继：`TransparentProxy`（VPN 载荷，macOS 14 起）、网络中继 iOS 17/macOS 14 起支持 MASQUE 内建中继（"无需任何自定义 App 代码"逐字，可代理全部 TCP/UDP）；macOS 内容过滤走 `ParentalControlsContentFilter` 或第三方提供者。**应用沙盒的网络出入站权限键（`com.apple.security.network.client/server`）本轮两份简报均未逐字取证——正文引用前须回 developer App Sandbox 页复核，当前标部分核实。**

## 12.4 watchOS：App 的网络路径

流传说法"Watch 流量一律经 iPhone 代理"被手册两句否掉（`secc7d85209d`，2026-01-28，逐字）：

> 为支持需要流传输数据的 App，加密采用了 FaceTime 通话安全性中所描述的方法，即使用配对 iPhone 所提供的 Apple 身份服务 (IDS) **或直接互联网连接**。

> Apple Watch 和 iPhone 未在通信范围内时，Apple Watch 会**直接接入 iCloud 和 Gmail 服务器**来取回邮件，而不是通过互联网与配对 iPhone 同步邮件数据。

配套的链路层句（逐字）："iPhone 和 Apple Watch 间的所有通信（通过加密蓝牙、无线局域网和提供二级加密层的蜂窝链路）使用以上列出的一种方法加密"——那是**设备间通道**的保护，不覆盖 Watch 独立联网场景。令牌作用域样本一并收录（逐字）：Gmail OAuth 令牌"以加密格式通过 IDS 发送到 Apple Watch……此 OAuth 令牌绝不会用于从配对 iPhone 连接 Gmail 服务器"。对 U3/U1 审计的含义：同一 Watch App 的上传在不同时刻可能走两条完全不同信任模型的路径（配对 IDS 通道 vs 直连互联网），排查时必须先定位路径再谈要求。

## 12.5 Mac Catalyst 与在 Mac 上运行的 iOS App

一手碎片：URLSession TLS 键 availability 逐字含 "Mac Catalyst from 13.1"；MDM 载荷平台清单多处含 Mac Catalyst（§4.4 引页）。**"Catalyst App 的 ATS 按 iOS 还是 macOS 判据生效"、"运行于 Apple 芯片 Mac 的 iOS App 用哪套防火墙/沙盒网络规则"均无一手指向页（缺口）。** 正文处理：写"随 iOS SDK 判据（§3.1 句中的 iOS）"并标注为推定；Mac 侧管理面差异（防火墙入站句只提 macOS）单独列出。

## 12.6 visionOS 与 Apple tvOS 的差项

可立的一手差项：CT 要求覆盖 tvOS 与 visionOS 1.1（§12.2 行）；锁定模式页未列 visionOS；Relay 载荷 availability 含 tvOS 17（且 `RelayUUID` **iOS 27 起弃用**——组织侧中继路径自身在换代，§14.1 冲突表素材）；`NWParameters.tls` 含 visionOS 1.0。**两平台的 App 网络路径专页与代理限制未取证（简报缺口清单第 8 条原样保留）。** tvOS 侧还有一条管理面负向句可用：per-app VPN On Demand 的 `OnDemandMatchAppEnabled` 与 AlwaysOn 载荷逐字 "Not available in tvOS or watchOS"——组织在 TV/Watch 上连"用 VPN 改变 App 路径"这条路都收窄了。

# 13. 判定链、可观测性与自查清单

本章是前三部分的汇合点：给出一条连接的七道判定、App 侧能看到/看不到的证据边界、以及可直接执行的 22 条自查。所有判定的事实依据都指回前文章号，不另引新页。

## 13.1 七道判定：一条连接是否满足传输安全要求

按顺序执行，任何一道"否"都改变结论与责任主体：

1. **路径判定**：这条连接走 URL Loading System 吗？否（Network/CFNetwork/BSD/MultipeerConnectivity）→ ATS 不适用，转分支 B（§2.5）。
2. **生效判定**：App 链接的 SDK ≥ iOS 9.0/macOS 10.11？否 → ATS 整体关闭（§3.1），这本身就是配置事实，按"未实施"记录。
3. **例外命中判定**：目标域（注意 DNS 名与 IP 例外互不通用，§3.7）是否命中 `NSExceptionDomains`／任一全局键？命中 → 记下**具体哪把键放松了哪一维**（§3.8 单键单维表）；未命中 → 五条扩展检查全量适用。
4. **服务器满足度判定**：TLS 版本/PFS/密钥长度/签名算法/CT 达标吗？工具面：`nscurl --ats-diagnostics`（§3.12）＋ §10.5 服务器能力清单。不达标 → 硬失败（§2.3），修复在服务器或（受申报约束的）例外，不在重试逻辑（§11.9）。
5. **信任评估判定**：链到锚、日期、签发者、主机名/SAN、EKU 五关（§5.1）；失败对照五类原因归责（§5.5 撤销主机可达性也算进来）。
6. **自定义代码判定**：有没有 delegate/verify block/`certificateValidator`？有 → 它在 ATS 保护域上只允许"更严"（§5.3），检查是否存在"更宽"分支（尤其失败重试路径，§11.9 第三类）。
7. **固定判定**：`NSPinnedDomains`/代码固定在场吗？命中则验证 AND 语义与备用密钥（§6.7），并确认失败形态已被监测（§6.6）。

分支 B（ATS 不适用路径）用同一套骨架但把第 3、4 道换成：min/max 版本与套件**实测**（§7.10 metadata 读取，不读配置意图）、PSK/0-RTT/mTLS 语义确认（§7.7–7.9）、以及"开发者自证"文档（§10.1）。

## 13.2 可观测证据：系统日志、连接状态与能看到与看不到的边界

**能看到**：错误面——`-1202`/五类失败原因（§5.1）、`networkUnavailableReason`（Expensive/Constrained，§8.7）、`NSURLErrorNetworkConnectionLost`（§8.3）；协商面——Network 路径 metadata 全套（版本/套件/SNI/`early_data_accepted`/peer 链，§7.10）；本地日志样本——BoringSSL `CERTIFICATE_VERIFY_FAILED`（§9.3）；设备侧管理面——已装描述文件/证书清单、锁定模式状态、本地网络授权（§10.2）；申报面——App Store Connect 提交记录（§4.2/4.3）。**看不到（一手边界）**：URLSession 路径无 metadata 等价 API（协商结果只能从服务器日志或抓包侧证，§8.5）；App 内自定义 `.useCredential` 判定过程无系统级日志（§6.6 末）；后台会话传输的系统调度决策不可编程读取；被拒绝的本地网络访问的精确错误面无逐字专页（§9.3 缺口）。**推论标记**：撤销主机被挡 → 信任评估失败的因果是"可推导"，正文已按纪律标注（§5.5）。

## 13.3 二十二条自查清单

配置侧（`Info.plist` 直读可验）：
1. ATS 字典是否存在；存在则逐键对照 §3.11 优先级表，确认没有"配了却不生效"的键组合。
2. 全局键与例外域是否共存冲突（写了细粒度全局键→`NSAllowsArbitraryLoads` 失效，§3.2）。
3. 每个例外域注明：放松了哪一维、对应 §4.2 四条理由中的哪一条、有无下线计划。
4. `NSExceptionMinimumTLSVersion` 低于 1.2 的域逐个列出（触发申报，§3.8）。
5. iOS 17+ 的 IP 直连是否全部显式进入例外域（含 CIDR 写法核对，§3.5/§3.7）。
6. `NSRequiresCertificateTransparency` 是否残留（obsolete 键，删，§5.6）。
7. `NSPinnedDomains` 的 CA/leaf AND 语义与备用密钥集合核对（§6.1/§6.2）。
代码侧：
8. 枚举全部 session 创建点，标注每个的 TLS 参数（§8.1/8.2）与信任 delegate 挂接。
9. 任何 `didReceive challenge` 实现：非目标 host 是否一律 `.performDefaultHandling`（§5.3 守门形态）。
10. 是否存在接受"更宽"信任的分支；ATS 保护域上的宽分支为硬性违规（§5.3）。
11. verify block/certificateValidator 是否幂等（可多次调用，§7.5）。
12. 重试路径不得改 TLS 参数（§11.9 工程约定，违规即记）。
13. 低层 API 连接（Network/CFNetwork）清单与 §13.1 分支 B 文档是否齐备。
14. `enablesEarlyData`/`earlyDataEnabled` 若开启，重放防护的业务设计在哪（§7.9）。
15. mTLS：两处默认值口径相反（§7.8），以实测为准并写下实测结果。
运行/申报侧：
16. `ITSAppUsesNonExemptEncryption` 判定含第三方库（§4.3）；豁免≠年报免除核对。
17. 本地网络：两个 plist 键与授权弹窗文案审计（§9.1/9.2）；Bonjour 服务类型最小化。
18. 上传暂存文件的保护档与 `completeUnlessOpen` 可读窗评估（§11.7）。
19. 后台上传：`isDiscretionary`/`waitsForConnectivity` 语义与业务预期对齐（§8.6/8.3）。
20. U2 是否声明了 CloudKit 加密字段（"必须明确声明"，§11.5）；ADP 两档文案是否为条件句。
平台/环境侧：
21. §12.2 矩阵按 App 实际支持的平台逐行核对，未取证平台按自证处理（§12.1/12.4）。
22. 企业环境差异：MDM 载荷时间线、代理/中继/内容过滤路径、锁定模式影响是否纳入故障归因（§10.3/§4.4）。

## 13.4 配置审计与代码评审要点

审计视角的三个"只看事实"：**(a) 键的作用域即风险的作用域**——全局键影响所有未列外域（§3.2），session 级信任决定影响该 session 全部任务（§8.4），一次 `performDefaultHandling` 缺失可能把非目标主机也接进自定义逻辑（§5.3）；**(b) 放松必须有纸面**——五个申报键（§4.2）之外的"放松"都发生在 ATS 视野外（低层 API、代码分支），要评审的是**代码而不是 plist**；**(c) 收紧也要测**——NIAP 键、钉 CA、min TLS 1.3 都可能因服务器不配合而变硬失败（§2.3），"更严"不等于"更稳"，发布前按 §13.1 第 4 道逐域实测。

## 13.5 回归监测：升级后必须重测的项

一手已证的"单调收紧线"就是回归清单本身：iOS 15（TLS1.0/1.1 枚举弃用）、iOS 17（IP 直连默认关，§3.5；可恢复上传上线）、iOS 26（`Network.TLS` builder、0-RTT 开关、`deniedICCIDsForiMessageFaceTime`）、iOS 27.2（旧 `SSLProtocol` 会话键弃用，§8.1）、iOS 27（`RelayUUID` 弃用，§12.6）。每轮 OS 大版本发布后至少重跑：例外域全量 `nscurl` 诊断、固定方案的证书轮换演练（§6.7 第 8 问）、watchOS/visionOS 直连路径抽测（§12.4）、组织载荷与新默认值的相互作用（CT 载荷、`allowUntrustedTLSPrompt`）。**监测责任主体是开发者**——Apple 侧的收紧不会另行通知单个 App（§10.4），错过版本窗口的典型症状是"没改任何代码，连接突然开始失败"。

# 14. 口径冲突、证据缺口与相反结论

## 14.1 术语与口径冲突 C1–C13

| 号 | 冲突 | 两侧一手出处 | 本篇处置 |
|---|---|---|---|
| C1 | 对端认证默认值：C API 页 "Clients default to true, whereas servers default to false" vs Swift `TLS.PeerAuthentication` "For clients, the default is `none`. For servers, the default is `required`" | §7.8 两页逐字 | 并引、标冲突，以实测/头文件为准 |
| C2 | 版本号线：2026 年 support 页出现 "iOS 27/macOS 27"，手册正文仍写 iOS 16/17 起 | 锁定模式两页（§4.4） | 以 support 2026-09-18 为准，注明并存 |
| C3 | Relay：载荷页 `RelayUUID` iOS 27 起弃用，部署手册同时推荐内建中继为 VPN 替代 | §12.6 两侧 | 写"路径本身在换代"，评审跟踪 |
| C4 | ATS 适用面：旧口径"适用于 URLSession/NSURLConnection/CFNetwork" vs 现行"不适用于 Network framework 与 CFNetwork" | §6.6 三句（developer 页） | 按现行文本；旧口径判为过时 |
| C5 | "ATS 是审核要求" vs 审核指南正文 0 命中 | §4.1 负向取证 | 约束来自 OS＋申报义务，审核条款说法标二手 |
| C6 | `NSRequiresCertificateTransparency` 当开关用 vs 官方 obsolete＋"Don't use this key" | §5.6 | 判残留配置错误 |
| C7 | 端对端加密类别数 14→23（手册 2022）vs 15→25（support 2026-01-08） | §11.2/§11.5 | 取新值，注明手册页未同步 |
| C8 | 服务端信任评估 TN 编号：流传 "TN2287" vs 真实 TN2232（TN2287 是 iOS5/TLS1.2 兼容文） | §5 章首 | 全文统一 TN2232 并记该纠错 |
| C9 | `NSThirdPartyException*` 一族：归档 Cocoa Keys 有、现行结构图与键页无 | §3.9 | 只作历史兼容项，注"未逐字核" |
| C10 | `URLSessionConfiguration.requiresTLS13`：代码圈常见 vs DocC 404 | §8.2 | 判不存在，给真路径 |
| C11 | "后台会话不支持 multipart/in-memory body"：社区经验 vs Foundation 全文 grep 0 命中 | §8.6 取证注 | 只能写"未获一手文档支持" |
| C12 | `NSAllowsLocalNetworking` 讨论中 `.local` 为键名引用，渲染随页面变 | §3.5 注 | 引文还原为代码体，注明以渲染为准 |
| C13 | Secure Transport 与 TLS 1.3："SecureTransport 不支持 TLS 1.3"仅 support 手册页有，developer 侧无对应原话 | dev §5.3 取证 | 只引手册页并标注 developer 侧无原话 |

## 14.2 一手页缺失清单 N1–N14

以下主题在本篇两份简报＋现场补抓后仍无逐字一手页，正文相应处均已按缺口/推定/工程建议标注，禁止升级为结论：N1 watchOS/tvOS/visionOS 上 ATS 的 SDK 判据；N2 Mac Catalyst 与"iOS App 跑在 Mac"的 ATS/防火墙适用规则；N3 后台会话系统临时副本的保护档；N4 `NSFileProtectionComplete` 与后台上传成败的因果；N5 本地网络权限被拒的精确错误码；N6 `NSBonjourServices` 通配符 `"*"` 规则；N7 同域同时存在 plist 例外与会话级 TLS 下界时的实际生效顺序；N8 App 侧"检测被 SSL 检查"的官方指引；N9 CloudKit `CKAsset` staging 属性名逐字；N10 App Store Connect Help 的出口合规字段级原文（help.apple.com 域未抓）；N11 APNs 提供方信任根逐字句（页面 404）；N12 URLSession 路径读取协商结果的公开 API；N13 tvOS/visionOS App 网络路径专页；N14 VisionOS 锁定模式适用性。

## 14.3 三簇必须并读的页面

**簇一（放松的条件式）**：`NSAllowsArbitraryLoads` 页 "Without ATS, you're also free to loosen the default server trust requirements" × `Performing manual server trust authentication` 页 "You cannot loosen... for an ATS-protected domain" × 手册"除非 App 重写了 App 传输安全性"——三句同读才得 §2.3/§5.3 的"条件式放松"结论；单引任何一句都会写成半truth。
**簇二（适用面）**：`NSAppTransportSecurity` 定义句（URL Loading System 用 HTTPS）× Preventing 页 enforcement/escape 两句 × manual `sec100a75d12`——得 §2.5 责任移交结论。
**簇三（上传密钥归属）**：iCloud 概览三层顺序句 × ADP 的 HSM 删除/重上传两句 × 102651 的 15/25 与网页临时密钥段——得 §11.5 "上传即密钥再分配、E2EE 是条件态"完整口径；缺簇三任一句，"Apple 能否解密"就会写错方向。

## 14.4 与流行口径相反的十八条

1. 审核指南没有 HTTPS/TLS/ATS 条款（§4.1）——"不上 HTTPS 审核不过"是二手传说。
2. ATS 不是审核环节的东西，是 OS 运行时强制（§4.5 表第 3 行）。
3. MDM 描述文件**不能**放宽或关闭 ATS（§10.7，210 键负向取证）。
4. 关掉 `NSAllowsArbitraryLoads` 不等于关掉证书校验：HTTPS 仍走默认信任评估（§3.2 逐字）。
5. 单个例外键只关一维：`NSExceptionAllowsInsecureHTTPLoads` 不改 TLS 版本也不关 PFS（§3.8）。
6. `NSExceptionRequiresForwardSecrecy=NO` 会**连带**取消密钥长度检查（§3.8 耦合句）。
7. 例外域一旦声明，全局键对该域全部失效——**哪怕字典为空**（§3.7 NOTE）。
8. `NSIncludesSubdomains` 只展开**一层**子域（§3.7）。
9. 域名例外与 IP 例外互不通用，即使解析到同一地址（§3.7）。
10. CT 键已 obsolete：CT 是系统强制，不是可选项（§5.6）。
11. "HTTPS 永远豁免出口申报"错：原文 Typically＋豁免仍可能要年报＋第三方库计入（§4.3）。
12. `requiresTLS13` 不存在（§8.2）。
13. Network.framework 与 CFNetwork 路径**不经过** ATS（§2.5）。
14. Watch App 不总是经 iPhone 中转（§12.4）。
15. 进了 CloudKit ≠ 端对端加密：字段必须显式声明加密＋专门 API（§11.5）。
16. 开了 ADP 仍有五类元数据退回标准保护（§11.8）。
17. 锁定模式限制的是 WebKit 网页内容等，**不是** TLS 版本要求（§4.4）。
18. "iCloud 有静态加密所以 TLS 可省"错：两层独立机制（§11.2 传输句）。

## 14.5 必须加限定语才能写出的表述

以下句式在本篇只以限定形态出现，摘出来供引用者自查：（a）"系统内置加密**通常**豁免"（Typically，§4.3）；（b）"固定**不能替 ATS 决定地板**"是 §6.4 叠加句的展开而非某页原话；（c）"挡撤销主机会致信任失败"标推论（§5.5）；（d）"组织 CA 重签可绕过 CA 钉"标合取推论（§6.5）；（e）"iPadOS 随 iOS 适用 ATS"标推定（§12.1）；（f）"重试路径不改 TLS 参数"是工程建议（§11.9）；（g）"私有地址=按网络三档、默认随认证强度"必须带"自 iOS 18"时间锚（§9.6）；（h）TN2232 的一切 API 表述带"文档年代 2014"限定（§5 章首）；（i）锁定模式条目引文随 support 页更新，手册目录页无逐条细节（§4.4 取证说明）；（j）"Apple 无法解密 E2EE 数据"必须带"除网页会话临时密钥等 §11.8 场景"的限定。

# 15. 结论与建议

## 15.1 一手证据能支持的结论

以下每一条都有前文编号的逐字或负向取证支撑，可作为确定语气引用：

1. ATS 的生效判据是**链接 SDK**（iOS 9.0/macOS 10.11），与运行系统版本无关；老 SDK 链接的 App 整体不受保护（§3.1）。
2. ATS 只覆盖 URL Loading System；Network/CFNetwork 等低层路径由开发者自负全责，且这不是漏洞而是官方明示的设计（§2.5）。
3. 例外体系的结构是"单键单维＋例外域优先＋全局键互斥"：每个键只关自己那一维、例外域字典哪怕为空也让全局键对其失效、写细粒度全局键会使 `NSAllowsArbitraryLoads` 被忽略（§3.2/§3.7/§3.8/§3.11）。
4. 放松方向的五个键需要书面申报理由，收紧方向的 NIAP 键明确不需要——**放松/收紧不对称是制度化的**（§4.2）。
5. 对 ATS 保护域，App 能对手工信任评估做的只有加严；放松只在域已被豁免时技术上可能（§5.3 条件式）。
6. MDM 无任何载荷可放宽或关闭 ATS；组织能动的四样是路径、锚、目的地、可见性（§4.4/§10.7）。
7. 审核指南正文（2026-10-16 版）不含 HTTPS/TLS/ATS/加密条款；"审核要求 HTTPS"无一手文本（§4.1）。
8. CT 已是系统强制（obsolete 键＋"Don't use this key"）；唯一官方"降低"入口是组织侧 CT 载荷且只降 CT（§5.6）。
9. 系统内置加密（URLSession HTTPS）**通常**豁免出口文档义务，但判定含第三方库、豁免类仍可能需年报（§4.3）。
10. 0-RTT 早期数据可被重放是 Apple 一手警告，握手确认前的上传数据自担重放风险（§7.9）。
11. U2 的密钥顺序是"设备生成→先加密后上传→密钥按档分发"；CloudKit 字段加密须显式声明；开/关 ADP 本身是密钥删除/重上传动作（§11.2/§11.5）。
12. `NSPinnedDomains` 只叠加期望集合、不改变其他要求；代码级固定同构（§6.4）。

## 15.2 一手证据不能支持的结论

以下流行说法在本篇证据体系内**不成立或不可断言**（详表见 §14.4/14.5）："ATS 是审核条款""MDM 能救 ATS 失败""HTTPS 永久豁免申报""进了 CloudKit 就端到端""锁定模式改 TLS 版本要求""requiresTLS13 存在""钉证书替代信任评估""Watch 流量必经 iPhone""后台会话必然被锁屏保护类切断（无一手因果，N3/N4）""Apple 在一切情况下都无法解密 E2EE 数据（§11.8 临时密钥场景）"。另有一类"看起来是结论其实缺一手页"的方向（§14.2 全部 N 项），写作与答辩时一律按缺口处理。

## 15.3 给 App 开发者的建议

按投入产出排序：(1) 把 `Info.plist` ATS 字典当作**对外合规资产**管理——每个例外键一行台账（域、维度、§4.2 理由编号、下线计划）；(2) session 创建点集中化，TLS 参数在创建时定死、重试只复用（§11.9）；(3) 信任自定义代码守 §5.3 门形（非目标 host 一律默认处理），并给自定义判定加自有日志（§6.6）；(4) 固定走"公钥＋集合"（§6.2 官方三句），并演练换钥不发包路径；(5) iOS 17+ 复查所有 IP 直连；(6) 低层 API 连接为每条路径写"分支 B"文档（§13.1）；(7) 出口申报自查含第三方 SDK 加密盘点（§4.3）；(8) 大版本 OS 发布后按 §13.5 重测。

## 15.4 给安全评审与配置审计的建议

评审顺序建议按 §13.1 七道走，但取证抓手要抓三样最低成本、最高信息量的物证：**解包出来的 `Info.plist` ATS 字典**（静态、即时可见全部放松面）、**MDM 载荷下发时间线**（解释"突然连不上"归因，§10.3）、**协商实测记录**（`nscurl`＋服务器日志版本/套件字段，§13.2）。把"是否用了低层 API"列为必答问题——它是 ATS 审计的最大盲区（§2.5）；把 §13.3 的 22 条做成 checklist 工具，任何"未取证"项显式留空而不是打勾。

## 15.5 给企业 IT 与设备管理者的建议

三条边界要写进内部政策文本：描述文件**改不了** App 的 ATS（工单正确去向是 App 厂商或服务器升级，§4.4/§10.7）；自家域要做 TLS 插层须同时满足监督设备＋代理/过滤路径，且**永远避开 Apple 服务主机清单**（§6.5 逐字中断句）；装根证书收紧用户提示（`allowUntrustedTLSPrompt=false`）前先验证内部 CA 链与撤销基础设施——收紧是把用户的逃生口焊死，锚配错等于批量断连（§5.4）。遗留内网系统的合规出路只有两条：推动 App 侧逐域例外＋申报，或升级服务器满足 §2.2 五检查。

## 15.6 给研究与合规申报的建议

研究侧：本篇最有复用价值的是三份负向清单（§7.4 API 名 404、§4.4 210 键、§4.1 指南 0 命中）与 §14.1 冲突表——引用任何名字前先过 404 探测，比任何二手文章便宜且可靠。申报侧：出口合规问卷回答前跑一遍"内置加密 vs 自有加密"盘点（含每个 SDK，§4.3），把 `ITSAppUsesNonExemptEncryption` 的取值与 §15.3(1) 的例外台账放在同一份审计包里——**"连接放宽了什么"与"加密申报了什么"在制度上是两条独立管线，但在答辩时必须能互相解释**。

## 15.7 量化基线表

| 主题 | 数值/判据 | 出处 |
|---|---|---|
| ATS 生效线 | linked SDK ≥ iOS 9.0／macOS 10.11 | §3.1 |
| ATS 扩展检查 | 5 条（版本/PFS/密钥长度/签名算法/CT） | §2.2 |
| 需申报例外界 | 5 个键；可接受理由样本 4 条 | §4.2 |
| 例外域全局优先 | 声明即失效全局键（含空字典） | §3.7 |
| 子域展开 | 仅 1 层 path component | §3.7 |
| `NSExceptionMinimumTLSVersion` 默认 | `TLSv1.2`（<1.2 触发申报） | §3.8 |
| `NSExceptionRequiresForwardSecrecy` 默认 | `YES`；关→连带免密钥长度检查 | §3.8 |
| IP 直连默认 | iOS 17/macOS 14 起关（单 IP＋CIDR 可例外） | §3.5 |
| CT | 系统强制；≥2 个 Apple 信任日志的 SCT | §5.6 |
| TLS1.0/1.1 枚举 | iOS 15／macOS 12／watchOS 8 起 deprecated | §7.2 |
| 会话旧 SSLProtocol 键 | iOS 27.2／macOS 27.2 起 deprecated | §8.1 |
| 强制 TLS1.3 唯一一手路径 | `tlsMinimumSupportedProtocolVersion=.TLS13` | §8.2 |
| 重协商默认 | `true`（唯一页面自带默认值句） | §7.6 |
| fallback | 新连接 MUST NOT | §7.6 |
| 可恢复上传 | iOS 17＋服务器支持 IETF 草案；后台自动 | §8.6 |
| HTTP/1.1 每主机连接数默认 | 6（HTTP/2+ 忽略） | §8.3 |
| 后台启动事件默认 | `true`；discretionary 默认 `false`、后台起传视为 `true` | §8.6 |
| 蜂窝访问默认 | `true` | §8.7 |
| MDM 通道 | ServerURL/CheckInURL 必须 https、三键不可变 | §5.4 |
| 描述文件能力 | ATS 相关键命中 0（Restrictions 210 键全查） | §4.4 |
| 审核指南安全连接条款 | 命中 0；网络相关仅 2.5.1/2.5.5/2.5.6 | §4.1 |
| iCloud 密钥顺序 | 设备生成→先加密→密钥分档→TLS 另加一层 | §11.2 |
| E2EE 类别数 | 15→25（support 2026-01-08；手册 14→23 未同步） | §11.5 |
| CloudKit 字段加密 | 必须显式声明＋专门 API；资源由 ADP 覆盖 | §11.5 |
| 固定匹配 | CA/leaf 同钉＝各类必中（AND） | §6.1 |
| 撤销主机 | 9 个 Apple/DigiCert 主机名 | §5.5 |
| 私有 Wi-Fi 地址 | 三档，默认随网络认证强度；TSF 56 位 | §9.6 |
| 信任评估典型错误码 | `-1202 NSURLErrorServerCertificateUntrusted` | §5.1 |

# 附录 A 来源清单

## A.1 《Apple 平台安全保护》手册页逐页登记

| 页 | secid/URL | 发布日期 | 本篇用途 |
|---|---|---|---|
| App 传输安全性（"除非 App 重写"段） | `sec100a75d12` | 见 #91 登记 | §2.2/§4.4 交叉 |
| 关于 App Store 安全性 | `secb8f887a15` | 2024-12-19 | §4.1 审核定性 |
| 关于"锁定模式" | support/105120 | 2026-09-18 | §4.4 逐条限制 |
| 锁定模式安全性（手册目录页） | `sec2437264f0` | 2024-05-07 | §4.4 取证说明 |
| Apple 设备的无线局域网隐私 | `sec31e483abf` | 2024-12-19 | §9.6 |
| 证书透明度设备管理有效负载 | deployment/depbafaa79ff | 2024-03-07 | §5.6/§12.2 |
| iCloud 安全性概览 | `secacde2d0da` | 2022-12-07 | §11.2 |
| iCloud 高级数据保护 | `sec973254c5f` | 2022-12-07 | §11.5/§11.8 |
| iCloud 数据安全概览 | support/102651 | 2026-01-08 | §11.5 数值口径 |
| watchOS 系统安全性 | `secc7d85209d` | 2026-01-28 | §12.4 |
| macOS 中的防火墙安全性 | `seca0e83763f` | 2021-02-18 | §12.3（入站负向） |
| 在企业网络上使用 Apple 产品 | support/101555 | 2026-07-31 | §5.5/§6.5 |
| 为 Apple 设备过滤内容 | deployment/dep1129ff8d2 | 2025-09-24 | §6.5 |
| 网络中继（部署） | dep91a6e427d／dep131693e6b | 2023-10-25／2024-03-07 | §12.3/§12.6 |

## A.2 developer.apple.com 文档与技术说明逐页登记

| 页 | URL（前缀 developer.apple.com/documentation/） | 版权行 | 本篇用途 |
|---|---|---|---|
| ATS 配置字典 | bundleresources/information-property-list/nsapptransportsecurity | © 2026 | §3.1 全目录 |
| 全局/容器/域键 11 页 | 同前两分支（见 §3 各节 URL） | © 2026 | §3.2–§3.10 |
| Preventing Insecure Network Connections | security/preventing-insecure-network-connections | © 2026 | §2.2/§4.2/§1.6 NIAP |
| Performing manual server trust authentication | foundation/performing-manual-server-trust-authentication | © 2026 | §5.3 |
| Handling an authentication challenge | foundation/handling-an-authentication-challenge | © 2026 | §5.2 |
| 出口合规 | security/complying-with-encryption-export-regulations | © 2026 | §4.3 |
| TN2232 HTTPS Server Trust Evaluation | library/archive/technotes/tn2232 | © 2014，Updated 2014-07-29 | §5 全章 |
| TN2287（纠错对照页） | library/archive/technotes/tn2287 | Updated 2011-10-14 | §5 章首 |
| App Store Review Guidelines | app-store/review/guidelines | © 2026，Last Updated 2026-10-16 | §4.1 |
| NWParameters／NWProtocolTLS／Network.TLS／QUIC | network/… | © 2026 | §7.1/§7.2 |
| Security Options（C API 目录） | network/security-options | © 2026 | §7 全章 |
| tls_protocol_version_t | security/tls_protocol_version_t | © 2026 | §7.2 |
| URLSessionConfiguration 及成员页 | foundation/urlsessionconfiguration/… | © 2026 | §8.1–§8.3 |
| 上传三篇＋URLSessionUploadTask 等 | foundation/uploading-data-to-a-website 等 | © 2026 | §8.6 |
| FileProtectionType／protectionKey／URLFileProtection | foundation/… | © 2026 | §11.7 |
| CKAsset | cloudkit/ckasset | © 2026 | §11.5 |
| Device Management 载荷目录／Restrictions／MDM／VPN／Relay | devicemanagement/… | © 2026 | §4.4/§5.4 |
| NSLocalNetworkUsageDescription／NSBonjourServices | bundleresources/… | © 2026 | §9.1/§9.2（现场补抓） |
| Creating an Identity for Local Network TLS | network/… | © 2026 | §9.3/§9.4（现场补抓） |

## A.3 抓取与取证方法说明

三类手法贯穿全篇：(1) **DocC JSON 直读**——developer 文档为 SPA，正文一律取自 `tutorials/data/documentation/<slug>.json`，代码块/aside 框与正文分离时逐字标注"未取到"而非补写；(2) **存在性探测**——对每个键名/函数名/流传 API 打端点记 200/404，404 清单作为负向结论入文（§7.4/§8.2）；(3) **全文关键词命中计数**——对手册 secid 全量页与审核指南做 `encrypt/TLS/ATS/https` 检索（§4.1），"命中 0"与"页面写了什么"同级取证。手册页逐条登记发布日期；developer 页仅有页脚 © 年份，统一记"截至 2026 可用"；TN 页登记 Updated 行。引用凡经推理性合并（合取/外推）一律标"推论"，凡缺口按 §14.2 编号回链，正文禁止把推论升格为事实。

# 附录 B 术语表

| 术语 | 口径 |
|---|---|
| ATS（App 传输安全性） | OS 对 URL Loading System 的 HTTPS＋扩展检查强制层；键表见 §3 |
| 默认服务端信任评估 | TLS 自带的链到锚验证＋主机名匹配（§5.1），独立于 ATS 存在 |
| 扩展检查 | ATS 在默认评估上叠加的 5 条（§2.2） |
| 例外键 | `Info.plist` 中放松 ATS 的键，其中 5 个需申报（§4.2） |
| 申报（justification） | 审核管线对例外键的书面理由义务（§4.2） |
| 出口合规申报 | `ITSAppUsesNonExemptEncryption`／`ITSEncryptionExportComplianceCode` 管线（§4.3），与数据上传无关 |
| 证书固定 | CA pinning／certificate pinning／属性检查三形态（§6.2） |
| NSPinnedDomains | ATS 声明式固定字典，CA/leaf AND 匹配（§6.1） |
| 硬失败 | ATS 检查不过即连接失败、无降级路径（§2.3） |
| 豁免（出口合规） | 系统内置加密"通常"免除文档上传义务，≠年报免除（§4.3） |
| 认证后可用／端对端 | CloudKit 服务密钥两档（§11.5）；ADP 把前者转后者 |
| U1/U2/U3 | 服务端上传／iCloud 同步备份／对端传输三方向（§0.3） |
| 插层（HTTPS 拦截） | 代理/内容过滤对 TLS 的重加密；Apple 服务域被逐字中断（§6.5） |
| 0-RTT／早期数据 | 握手确认前发送的数据，官方逐字警示可重放（§7.9） |
| NIAP TLS FP | ATS 唯一"再收紧"开关（`none|FCP_v2.1|recommended`，§3.6） |
| 锁定模式 | 运行环境层：改变设备连接行为、阻描述文件安装，非 TLS 键（§4.4） |

# 附录 C 速查表

## C.1 键—默认值—放松或收紧对照

| 键 | 默认 | 方向 | 申报 |
|---|---|---|---|
| `NSAllowsArbitraryLoads` | NO | 全局放松（关扩展检查） | 是 |
| `NSAllowsArbitraryLoadsInWebContent` | NO* | 仅 WKWebView 放松 | 是 |
| `NSAllowsArbitraryLoadsForMedia` | NO* | 仅 AVFoundation 放松 | 是 |
| `NSAllowsLocalNetworking` | — | 本地目标放松（版本敏感） | 否 |
| `NSExceptionAllowsInsecureHTTPLoads` | NO | 单域放行 http＋手评 | 是 |
| `NSExceptionMinimumTLSVersion` | TLSv1.2 | 单域降版本 | <1.2 时是 |
| `NSExceptionRequiresForwardSecrecy` | YES | 单域关 PFS（连带密钥长度） | 否（但属放松） |
| `NSRequiresCertificateTransparency` | NO | **obsolete，勿用** | — |
| `NSRequiresNIAPTLSPackageVersion` | 不启用 | 全局收紧 | 否 |
| `NSPinnedDomains` | 无 | 叠加期望集合 | 否 |
（*两键页默认值未逐字取全，标部分核实，§3.3/§3.4。）

## C.2 API—是否受 ATS 约束—可设置的上下界

| API 路径 | ATS | 版本上下界一手入口 |
|---|---|---|
| URLSession／URL Loading System | 是 | `tlsMinimum/MaximumSupportedProtocolVersion`（§8.1） |
| WKWebView 内容 | 是，可全局豁免（§3.3） | 无独立键 |
| AVFoundation 媒体 | 是，可全局豁免（§3.4） | 无 |
| Network.framework | 否 | `sec_protocol_options_set_min/max_tls_protocol_version`／`TLS.version(min:max:)`（§7.2） |
| CFNetwork | 否（现行原话点名） | 经底层同上 |
| BSD Sockets | 否（无 TLS 直供，#91 口径） | 自建 |
| QUIC（`NWParameters.quic`） | 否 | `QUIC.TLS` 子集（§7.1） |

## C.3 上传方向—密钥归属—服务端可见明文

| 方向 | 密钥在哪 | 服务端/中继可见什么 |
|---|---|---|
| U1 自建服务端 | App 决定（TLS 只到对端） | 除非 App 先加密，服务器见明文 |
| U2 iCloud/CloudKit | 设备生成；认证后可用档入 Apple HSM，E2EE 档留钥匙串保护域；ADP 移动边界 | 明文档位不可见内容；校验和/时间/标题缩略图等元数据可见（§11.8） |
| U3 对端 | 逐条消息密钥/本地身份在参与设备 | 中继（IDS/APNs）不可解密（系统服务协议，App 无复用 API） |

## C.4 合格承诺句式在本篇的四次出现

正文可安全复用的句式模板（均可回链一手出处）：（1）"该连接的传输安全由 ATS 保证，**前提是其走 URL Loading System 且目标域未被例外键豁免**（§2.5/§3）"；（2）"上传数据的 TLS 保护止于对端服务器，**落盘与内部流转由服务方承诺**（§11.1）"；（3）"iCloud 备份类数据是否端对端加密**取决于用户是否开启高级数据保护及数据类别**（§11.5，15→25 口径）"；（4）"系统内置加密**通常**满足出口豁免，**判定含第三方库，且豁免不必然免除年报**（§4.3）"。四句共同结构是"主张＋限定从句＋回链"，这也是本篇对一切传输安全承诺的最低表述标准。

**联系方式**：Telegram: https://t.me/one00190
