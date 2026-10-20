@@TITLE@@ iOS 系统网络安全漏洞分析报告
@@SUBTITLE@@ 技术分析视角 · 攻击面分析 / CVE 案例研究 / 防御对策

@@INFO@@ 报告版本 | v1.0
@@INFO@@ 发布日期 | 2026 年 10 月 2 日
@@INFO@@ 报告类型 | 安全技术分析报告
@@INFO@@ 适用范围 | iOS / iPadOS 18.x - 26.x
@@INFO@@ 密级 | 内部公开

@@TOC@@

# 一、报告概述

## 1.1 报告目的与范围

本报告面向安全工程师、渗透测试人员与移动应用开发者，从技术分析视角系统性梳理 iOS（含 iPadOS）系统的网络安全攻击面、历史典型漏洞与防护机制，并给出可落地的防御建议。报告范围覆盖：

- iOS 网络协议栈的架构分层与信任边界；
- 系统内置网络安全防护机制的原理与局限（ATS、证书信任、网络扩展、私有中继、BlastDoor、锁定模式等）；
- 无线接入层（Wi-Fi、蜂窝基带、蓝牙）与消息通道的历史重大漏洞案例分析（含 CVE 编号、技术机理、影响版本与修复版本）；
- 常见攻击技术与绕过手法（中间人、证书固定绕过、ATS 降级、无线侧攻击、VPN 泄漏等）；
- 面向用户、开发者与企业的分层防御建议。

> 声明：本报告仅用于安全研究与防御建设目的，所涉及的漏洞信息均来自 Apple 官方安全公告、NVD/CVE 公开数据库及公开安全研究文献。

## 1.2 iOS 网络安全体系总览

iOS 的网络访问路径自上而下可分为应用层、系统框架层、传输层、内核网络栈与无线固件层，各层均设有独立的信任边界与防护机制：

| 层级 | 主要组件 | 安全职责与机制 |
| --- | --- | --- |
| 应用层 | 第三方 App、Safari、iMessage | 沙箱隔离、数据保护、证书固定（App 自实现） |
| 系统框架层 | NSURLSession、CFNetwork、Network.framework | App Transport Security（ATS）、协议实现、代理配置 |
| 传输与会话层 | TLS / QUIC / HTTP/2 / HTTP/3 | 信任链校验、前向保密、CT 策略、会话恢复 |
| 网络扩展层 | NEPacketTunnelProvider、NEFilterDataProvider、Private Relay | VPN 隧道、内容过滤、双跳匿名中继 |
| 内核层 | XNU 网络栈、socket、pf 防火墙 | 内存安全加固、沙箱与熵、地址空间隔离 |
| 无线层 | Wi-Fi 固件、蜂窝基带（Qualcomm / Apple C1）、蓝牙控制器 | 固件隔离、MAC 随机化、WPA3、基带独立处理器 |

上述分层设计体现了 iOS 网络安全的两个核心理念：**纵深防御**（每一层独立设防）与**最小信任**（无线固件与基带不被主机视为可信组件）。后续章节中的绝大多数漏洞，本质上都是某一层的信任边界被突破。

## 1.3 术语与缩写

| 缩写 | 全称 | 说明 |
| --- | --- | --- |
| ATS | App Transport Security | 应用传输安全，强制 HTTPS 与强加密套件 |
| MITM | Man-in-the-Middle | 中间人攻击 |
| RCE | Remote Code Execution | 远程代码执行 |
| 0-click | Zero-click | 零点击利用，无需用户交互 |
| SPKI | Subject Public Key Info | 证书公钥信息，证书固定的比对对象 |
| MDM | Mobile Device Management | 移动设备管理 |
| AWDL | Apple Wireless Direct Link | 用于 AirDrop / AirPlay 的直连协议 |
| KEV | Known Exploited Vulnerabilities | CISA 已知被利用漏洞目录 |

# 二、iOS 网络安全防护机制分析

## 2.1 App Transport Security（ATS）

ATS 自 iOS 9 引入并在 iOS 10 起全面强制，是 iOS 应用网络流量的第一道系统级防线，其默认策略包括：

- 禁止明文 HTTP 连接，所有连接必须使用 HTTPS；
- 要求 TLS 1.2 及以上版本；
- 要求前向保密（Forward Secrecy）加密套件（如 ECDHE）；
- 强制服务器证书通过系统信任链校验，并对 RSA 密钥长度、签名算法强度提出最低要求。

ATS 通过 Info.plist 中的 `NSAppTransportSecurity` 字典进行配置，常见例外项与安全风险如下：

| 配置项 | 作用 | 安全风险 |
| --- | --- | --- |
| NSAllowsArbitraryLoads | 全局关闭 ATS | 等同于放弃系统级传输保护，App Store 审核需说明理由 |
| NSExceptionDomains | 按域名放行 | 域名字符串匹配处理不当可能扩大放行范围 |
| NSExceptionAllowsInsecureHTTPLoads | 对指定域允许明文 HTTP | 该域流量可被任意监听与篡改 |
| NSExceptionMinimumTLSVersion | 降低最低 TLS 版本 | 降级至 TLS 1.0 / 1.1，可受历史密码学攻击影响 |
| NSAllowsLocalNetworking | 放行本地网络 | 本地网段内攻击者可直接劫持流量 |

ATS 的关键局限有三点：

1. **覆盖范围有限**：ATS 仅作用于 NSURLSession、NSURLConnection 等 CFNetwork 高层 API，不约束直接使用 BSD Socket 或 Network.framework 底层接口的网络实现；
2. **依赖开发者配置**：例外项由开发者自行声明，历史上大量 App 为兼容旧服务端而使用 NSAllowsArbitraryLoads，使 ATS 形同虚设；
3. **不防御信任链内的中间人**：若设备安装了企业根证书或用户信任的代理 CA，ATS 校验会正常通过，恶意流量在 ATS 视角下完全合法。

## 2.2 证书信任链与证书固定（Certificate Pinning）

iOS 使用系统级信任存储进行 X.509 证书链校验。用户或 MDM 安装的根证书需要显式在"设置 - 通用 - 关于本机 - 证书信任设置"中启用后才生效。绕过系统信任链的常见方式，是诱导用户安装并信任攻击者 CA，此后设备上所有未做固定校验的 TLS 流量均可被解密。

证书固定是 App 层对抗信任链滥用的核心手段，常见实现方式：

- **SPKI 固定**：在 URLSession 的认证挑战回调中比较服务端证书公钥哈希；
- **证书固定**：直接比对叶子证书或中间证书内容；
- **系统能力**：通过 `URLSessionDelegate` 的认证挑战回调自行实现固定逻辑。

以下为典型的 SPKI 固定实现（Swift）：

```swift
func urlSession(_ session: URLSession,
                didReceive challenge: URLAuthenticationChallenge,
                completionHandler: @escaping (URLSession.AuthChallengeDisposition,
                                              URLCredential?) -> Void) {
    guard let trust = challenge.protectionSpace.serverTrust,
          let cert  = SecTrustGetCertificateAtIndex(trust, 0),
          let key   = SecCertificateCopyKey(cert),
          let keyData = SecKeyCopyExternalRepresentation(key, nil) as Data? else {
        completionHandler(.cancelAuthenticationChallenge, nil)
        return
    }
    let serverHash  = SHA256.hash(data: keyData)
    let pinnedHash: [UInt8] = [/* 预置的服务端公钥哈希 */]
    if Array(serverHash) == pinnedHash {
        completionHandler(.useCredential, URLCredential(trust: trust))
    } else {
        completionHandler(.cancelAuthenticationChallenge, nil)
    }
}
```

需要注意，证书固定并非绝对可靠：

- 在**越狱设备**上，攻击者可通过 Frida、objection、SSL Kill Switch 2 等工具 Hook 校验函数实现运行时绕过；
- 对**未越狱设备**，若结合恶意描述文件与企业签名重打包分发，同样可绕过固定逻辑；
- 固定策略本身存在**可用性风险**：证书轮换未同步更新固定列表会导致大面积客户端断连，因此必须设计备用固定哈希与灰度发布流程。

## 2.3 网络扩展框架与 VPN

iOS 8 起开放 NetworkExtension 框架，将 VPN、内容过滤、DNS 代理等能力收拢到受控扩展进程中：

- **NEPacketTunnelProvider**：实现 IP 层隧道，IKEv2、IPSec 及 WireGuard 等第三方协议 App 均基于此；
- **NEFilterDataProvider**：实现系统级流量过滤，是网络内容过滤类 App 的底座；
- **按需连接与 Always-On VPN**：企业可强制敏感流量走隧道，并在特定网络条件下自动建立连接。

该框架的安全意义在于：VPN 与过滤逻辑运行在受限的扩展沙箱内，且由系统统一管理隧道生命周期，第三方 App 无法直接篡改内核路由。但存在两类设计性局限：

1. **iOS 没有传统意义上的网络终止开关（Kill Switch）**：公开研究（2022 年 Michael Horowitz、Proton VPN 等）指出，VPN 隧道异常断开、网络切换或低电量挂起等场景下，流量可能在隧道重建前的窗口期走物理网卡直连，造成"流量泄漏"。企业环境应使用 Always-On VPN 与按需规则收窄该窗口；
2. **网络扩展本身是高价值攻击面**：获得网络扩展权限的恶意配置文件可在设备上建立任意代理通道，因此描述文件与 MDM 的安装控制至关重要。

## 2.4 iCloud 私有中继（Private Relay）

私有中继在 iOS 15 引入，为 Safari 流量与 DNS 查询提供双跳匿名化：

- 流量先经**入口中继**（知道用户 IP，但看不到访问内容）再经**出口中继**（知道访问目标，但看不到用户 IP）；
- 任何单一节点都无法同时获得"用户身份 + 访问目标"的组合，从而对抗网络侧流量画像；
- DNS 查询同样经过加密中继，避免明文 DNS 暴露访问意图。

局限：并非所有流量都在覆盖范围内（部分 App 与协议直连）；网络管理员可通过屏蔽中继域名强制流量走本地出口；该功能在部分国家和地区不可用。私有中继解决的是**隐私**问题，不提供对抗终端侧恶意软件的完整性保护。

## 2.5 消息与内容解析隔离（BlastDoor）

iMessage 等消息通道是零点击攻击的重灾区，原因是富媒体消息需要在渲染前解析大量复杂格式（图片、PDF、Wallet Pass、压缩归档等）。iOS 14 引入 **BlastDoor**：

- 将消息附件的解析放入独立沙箱进程，解析进程无网络访问、无文件写入能力，仅向消息进程输出安全化的渲染数据；
- 解析器漏洞被利用时，攻击者首先被困在 BlastDoor 沙箱内，难以直接升级为系统级控制；
- 后续 Apple 对消息链的加固（如锁定模式下禁用附件自动解析）均可视为该思路的延伸。

历史教训：CVE-2023-41064 / CVE-2023-41061（BLASTPASS 利用链）所利用的 Wallet Pass 解析路径未被完全纳入 BlastDoor 隔离范围，导致零点击链仍然打通。这说明**解析隔离的有效性取决于其覆盖完整性**，任何绕过隔离的解析路径都是潜在突破口。

## 2.6 锁定模式（Lockdown Mode）

锁定模式面向记者、人权工作者等高危人群，是 iOS 16 引入的极限加固开关：

- 默认屏蔽绝大多数消息附件类型（保留图片并剥离元数据）、禁用链接预览；
- 禁用 WebKit 的 JIT 编译，显著提高浏览器漏洞利用难度；
- 阻断来自陌生人的 FaceTime 呼叫与邀请；
- 在设备锁定状态下断开有线数据连接，并禁止安装配置描述文件。

锁定模式并不修复漏洞本身，而是通过削减功能面（减少解析器数量、关闭 JIT）将攻击面压缩到最小，是纵深防御思想在系统级的集中体现。

# 三、攻击面与威胁模型

## 3.1 攻击面分层分析

| 攻击面 | 投递方式 | 所需条件 | 典型漏洞 / 事件 |
| --- | --- | --- | --- |
| 蜂窝基带 | 恶意或伪造基站信令、异常网络参数 | 需处于蜂窝网络链路中的特殊位置 | CVE-2025-31214（C1 基带，流量劫持） |
| Wi-Fi 固件与驱动 | 恶意 802.11 管理帧、畸形信标 / 响应帧 | 攻击者在物理无线覆盖范围内 | CVE-2017-9417（Broadpwn） |
| WPA2 协议实现 | 密钥重装攻击（4 次握手重放） | 攻击者控制信道并重放握手报文 | CVE-2017-13077 等（KRACK） |
| 消息通道（iMessage） | 精心构造的消息与附件 | 掌握目标号码 / 账号，无需用户交互 | CVE-2021-30860（FORCEDENTRY）、CVE-2023-41064/41061（BLASTPASS） |
| 浏览器与 Web 内容 | 恶意网页、广告链、被动加载 | 诱导访问或被动加载 | CVE-2025-24201（WebKit 越界写） |
| 无线近场与设备配置 | 物理接触、邻近攻击 | 短时物理接触设备 | CVE-2025-31216（覆写 Wi-Fi 配置）、CVE-2025-43374（内核越界读） |
| 企业配置与描述文件 | 恶意描述文件、MDM 滥用 | 用户确认安装或已受管设备 | 配置型攻击（代理 + 根证书组合） |

## 3.2 威胁模型

结合攻防实践，iOS 网络安全的对手可分为四类，其能力与目标各不相同：

- **远程零点击攻击者**（如商业间谍软件厂商）：通过消息与网络服务投递 0-click 链，目标是持久化控制高价值设备。防御重点在消息解析隔离、锁定模式与及时更新；
- **无线近场攻击者**：在 Wi-Fi / 蓝牙 / 蜂窝覆盖范围内利用协议栈与固件漏洞，目标是中间人、流量劫持或代码执行。防御重点在 WPA3、MAC 随机化与固件更新；
- **物理接触攻击者**：利用 USB 访问、配置篡改或取证工具提取数据。防御重点在锁定模式、数据保护与受控配件策略；
- **供应链与企业配置滥用**：通过恶意描述文件、MDM、企业证书完成大规模流量劫持。防御重点在安装管控、证书透明度与终端检测。

需要强调的共性结论是：**iOS 的防线强度取决于最低配置项而非默认能力**——即使用户未开启锁定模式、开发者关闭了 ATS、企业未强制 Always-On VPN，系统其余部分再安全也无法阻止对应层级的攻击成立。

# 四、典型漏洞案例分析

## 4.1 基带流量劫持：CVE-2025-31214（Apple C1）

**基本信息**：影响 iPhone 16e（首发搭载 Apple 自研基带 C1）；修复于 iOS 18.5（2025 年 5 月）；Apple 安全公告描述为"处于特权网络位置的攻击者可能能够拦截网络流量"。

**技术分析**：基带（Baseband）负责蜂窝协议栈（LTE / 5G NR）的信令与用户面处理，运行在独立处理器与独立操作系统上。所谓"特权网络位置"，指攻击者能够控制或伪造蜂窝网络侧的信令节点——例如恶意或伪基站、受控的网络链路。该漏洞意味着基带在特定网络参数或信令序列下，会以不安全的方式处理用户面数据，导致攻击者可以拦截本应受加密保护的用户流量。

**安全意义**：

- C1 是 Apple 首款自研基带，此前 iPhone 长期使用高通 / 英特尔基带。自研基带提升了供应链可控性，但新代码库必然伴随新的攻击面，该 CVE 是 C1 的首个公开安全修复；
- 基带漏洞具有**绕过上层全部防护**的特性：无论 ATS、证书固定、VPN 配置多么严格，流量在离开基带进入运营商链路前的处理若被攻破，上层加密结论将不再成立；
- Apple 未在公告中标注在野利用，但事件再次提示：蜂窝链路本身不应被视为可信信道，敏感应用必须坚持端到端加密。

**修复与缓解**：升级至 iOS 18.5 及以上；对流量完整性有极高要求的场景，叠加应用层端到端加密（E2EE）。

## 4.2 Wi-Fi 固件远程代码执行：Broadpwn（CVE-2017-9417）

**基本信息**：影响使用 Broadcom BCM43xx 系列 Wi-Fi 芯片的设备（iPhone 5 至 iPhone 7 时代的多数机型同样使用该系列芯片）；修复于 iOS 10.3.3（2017 年 7 月）；由 Exodus Intelligence 的 Nitay Artenstein 在 Black Hat USA 2017 公开。

**技术分析**：漏洞是 Wi-Fi 固件中对 802.11 帧解析的堆缓冲区溢出。攻击者无需认证、无需用户交互，只要在无线覆盖范围内发送精心构造的 Wi-Fi 帧，即可在 Wi-Fi SoC 上执行代码。该漏洞的两个关键特性使其危害被放大：

1. **蠕虫化传播**：被攻陷的 Wi-Fi 芯片可自动扫描并感染覆盖范围内的其他设备，无需任何用户操作，理论上可形成"空中蠕虫"；
2. **内外边界突破**：Wi-Fi SoC 通过 DMA 与主机内存通信。在当时的硬件架构下，攻陷 Wi-Fi 固件后可进一步对主机内核实施内存操作，实现从"无线固件"到"应用处理器"的越权，最终达成主机层面的代码执行。

**安全意义**：该案例确立了"无线固件是主机的信任边界之外、但物理上连接主机的组件"这一威胁模型，也促使业界在后续芯片设计中强化 IOMMU 隔离与固件完整性校验。

**修复与缓解**：升级至已修复固件版本；Wi-Fi 芯片固件随系统更新分发，因此**及时更新系统是唯一的用户侧缓解手段**。

## 4.3 WPA2 密钥重装攻击：KRACK（CVE-2017-13077 等）

**基本信息**：影响 WPA2 协议实现的系列漏洞（CVE-2017-13077 至 CVE-2017-13088）；由 KU Leuven 的 Mathy Vanhoef 于 2017 年 10 月公开；Apple 在 iOS 11.1、macOS 10.13.1 等版本中完成修复。

**技术分析**：KRACK 利用的是 **802.11 标准本身的缺陷**而非某个实现的编码错误。WPA2 四次握手过程中，攻击者（位于同一信道、可充当中继）重放握手的第 3 条消息，可诱使客户端重复安装已经使用过的会话密钥（Key Reinstallation）。密钥重装会重置数据包的随机数（nonce）与重放计数器（replay counter），进而产生三个后果：

- **解密**：nonce 复用使部分加密算法下的密钥流被重复使用，攻击者可解密截获的流量；
- **重放**：重放计数器重置后，历史帧可被重放攻击；
- **注入**：结合上述能力可向连接中注入伪造帧。

对 iOS 设备而言，受影响的是客户端侧握手实现。攻击需要攻击者在无线信道中建立中间人位置（如伪造同 SSID 的热点），并非可远程从互联网发起。

**安全意义**：KRACK 是"协议缺陷 ≠ 实现缺陷"的经典案例——即使实现完全正确，标准本身的设计错误仍会波及全部合规产品。这也加速了 WPA3 的推广（WPA3 的 SAE 握手从设计上抵抗离线字典与部分重放攻击）。

**修复与缓解**：升级 iOS（当时为 11.1 及后续版本）；优先连接启用 WPA3 的网络；避免在无信任的无线环境中处理敏感业务。

## 4.4 零点击消息链：FORCEDENTRY（CVE-2021-30860）

**基本信息**：CoreGraphics / JBIG2 解码器漏洞；通过 iMessage 零点击投递；被 NSO Group 的 Pegasus 间谍软件用于攻击记者与活动人士；修复于 iOS 14.8（2021 年 9 月）；Google Project Zero 在 2021 年 12 月发布深度分析报告。

**技术分析**：

- 根因是 JBIG2 图像压缩格式解码器中算术解码逻辑的**整数溢出**，属于逻辑漏洞而非典型的内存安全编码错误，因此规避了当时部署的部分内存安全缓解机制；
- 攻击者将携带畸形 JBIG2 流的 PDF 伪装为图片附件通过 iMessage 发送。iMessage 在**渲染预览前自动解析附件**，解析过程无需用户点击，触发漏洞后攻击者获得代码执行能力，随后组合其他漏洞完成沙箱逃逸与持久化；
- 该链的投递利用了 iMessage 的内容处理自动化特性，是"消息通道 = 零点击攻击高速公路"的代表性证据。

**修复与缓解**：升级至 iOS 14.8 及以上；高风险人群启用锁定模式（可显著收窄消息附件解析面）。

## 4.5 BLASTPASS：Wallet Pass 零点击链（CVE-2023-41064 / CVE-2023-41061）

**基本信息**：CVE-2023-41064（ImageIO 缓冲区溢出）与 CVE-2023-41061（Wallet 校验问题）组合成的零点击利用链；由 Citizen Lab 于 2023 年 9 月捕获并披露；同样用于投递 NSO Pegasus；修复于 iOS 16.6.1（2023 年 9 月 21 日）。

**技术分析**：

- 投递方式为向目标发送**携带恶意图片的 Wallet Pass 附件**，iMessage 自动处理该附件并触发 Pass 预览解析流程，全程无需用户交互；
- 与 FORCEDENTRY 的关键差异在于解析路径：BLASTPASS 利用的 Pass 解析管线未被 BlastDoor 完全覆盖，ImageIO 在处理恶意图片时发生缓冲区溢出，随后结合 Wallet 校验缺陷完成利用链；
- 该案例证明：**BlastDoor 的隔离效果取决于覆盖完整性**——只要存在一条绕过隔离的解析路径，零点击链即可复活。事后 Apple 将更多解析流程收拢进隔离组件。

**修复与缓解**：升级至 iOS 16.6.1 及以上；企业可对高价值目标下发"禁用消息自动预览"策略。

## 4.6 Operation Triangulation 中的内核链（CVE-2023-32434）

**基本信息**：Kaspersky 于 2023 年 6 月披露的长期高级攻击行动（感染最早可追溯至 2019 年）；利用链包含 CVE-2023-32434（XNU 内核整数溢出）、CVE-2023-32435 与 CVE-2023-32439（WebKit）；修复于 iOS 16.5.1（2023 年 6 月）。

**技术分析**：

- 投递入口同样为 iMessage：向目标发送附带恶意附件的消息触发零点击链；
- 链式利用结构为：WebKit 漏洞获得渲染进程代码执行，随后利用 XNU 内核中的整数溢出实现物理内存任意读写，进而完成内核级控制与持久化植入（植入物 TriangleDB）；
- 攻击者使用多级验证器（validator）检查目标环境、混淆与清除痕迹，并在相当长时间内未被发现，代表了国家背景攻击者的工程化水平。

**安全意义**：该案例展示了"浏览器引擎 + 内核"这一经典组合式攻击在 iOS 上的完整形态，也说明单一层级的加固无法阻止链式利用——每一环都必须是防线。

## 4.7 近场与配置类漏洞：CVE-2025-31216 与 CVE-2025-43374

**基本信息**：两个 Wi-Fi 相关漏洞均随 iOS 18.5（2025 年 5 月）修复，Apple 未标注在野利用：

- **CVE-2025-31216**：具有物理接触条件的攻击者可以覆写设备上受管的 Wi-Fi 配置（managed Wi-Fi profiles）；
- **CVE-2025-43374**：邻近位置的攻击者可以造成内核内存的越界读取（out-of-bounds read）。

**技术分析**：

- CVE-2025-31216 对企业受管设备威胁显著：若能物理接触受管设备并篡改其 Wi-Fi 配置（如指向恶意热点或代理），后续即可在受管设备的网络路径上实施中间人与情报收集，而设备表面仍显示"已按企业策略配置"；
- CVE-2025-43374 的越界读取位于**内核 Wi-Fi 处理路径**，由邻近攻击者通过网络帧触发。信息泄露类原语单独使用价值有限，但在攻击链中常作为绕过 ASLR 与堆布局探测的组件，与内存破坏漏洞组合后危害倍增。

**修复与缓解**：升级 iOS 18.5+；对受管设备启用配置完整性校验与终端审计；高价值设备建议评估启用锁定模式。

## 4.8 案例横向对比

| CVE 编号 | 组件 | 漏洞类型 | 投递方式 | 用户交互 | 影响 | 修复版本 |
| --- | --- | --- | --- | --- | --- | --- |
| CVE-2017-9417 | Broadcom Wi-Fi 固件 | 堆溢出 | 无线帧 | 无 | Wi-Fi 芯片 RCE，可蠕虫化 | iOS 10.3.3 |
| CVE-2017-13077 等 | WPA2 协议实现 | 协议缺陷 | 信道中间人 | 无 | 解密 / 重放 / 注入 | iOS 11.1 |
| CVE-2021-30860 | CoreGraphics（JBIG2） | 整数溢出（逻辑缺陷） | iMessage 附件 | 零点击 | 远程代码执行 | iOS 14.8 |
| CVE-2023-32434 | XNU 内核 | 整数溢出 | iMessage 附件 | 零点击 | 物理内存任意读写 | iOS 16.5.1 |
| CVE-2023-41064 / 41061 | ImageIO + Wallet | 缓冲区溢出 + 校验缺陷 | iMessage Pass 附件 | 零点击 | 远程代码执行 | iOS 16.6.1 |
| CVE-2025-24201 | WebKit | 越界写 | 恶意 Web 内容 | 需加载页面 | 沙箱逃逸组件（在野利用） | iOS 18.3.2 |
| CVE-2025-31214 | Apple C1 基带 | 网络流量处理缺陷 | 特权网络位置 | 无 | 流量拦截 | iOS 18.5 |
| CVE-2025-31216 | Wi-Fi 配置管理 | 配置校验缺陷 | 物理接触 | 无 | 受管 Wi-Fi 配置被覆写 | iOS 18.5 |
| CVE-2025-43374 | 内核 Wi-Fi 路径 | 越界读取 | 邻近无线攻击 | 无 | 内核内存信息泄露 | iOS 18.5 |

## 4.9 2025-2026 年态势

- **2025 年**：Apple 修复了包括 CVE-2025-24201（WebKit 越界写，确认在野利用于针对特定个人的"极其复杂"攻击）在内的多个在野利用漏洞；自研基带 C1 出现首个公开漏洞（CVE-2025-31214），标志自研基带正式进入漏洞研究的视野；
- **2026 年**：Apple 于 2 月修复了当年首个在野利用零日 **CVE-2026-20700**——dyld（动态链接器）中的内存破坏问题，可导致任意代码执行，被用于"极其复杂的定向攻击"，修复版本为 iOS 26.3 / macOS Tahoe 26.3。该漏洞组件并非网络协议本身，但再次确认了高级定向攻击链持续活跃，且攻击者偏好复用系统关键组件中影响面广的漏洞。

**趋势判断**：

1. 零点击消息链仍是高价值目标的头号威胁，但 Apple 通过 BlastDoor / 锁定模式持续抬高成本，攻击者开始转向组合使用多个中危漏洞；
2. 无线与基带层级（自研基带、Wi-Fi 固件）随着硬件自研化进入新一轮漏洞披露周期；
3. 利用链的"组件化"趋势明显：信息泄露原语 + 内存破坏原语 + 沙箱逃逸分离在不同 CVE 中，单点修补难以阻断整条链。

# 五、攻击技术与绕过手法

## 5.1 中间人攻击与证书固定绕过

中间人是 iOS 网络攻击的通用形态，完整链条通常为：控制网络路径（恶意热点 / ARP 欺骗 / 恶意代理配置）→ 使设备信任攻击者 CA（诱导安装描述文件）→ 解密并改写流量。

在测试与攻防实践中，绕过证书固定的常见手段包括：

- **运行时 Hook**：在越狱设备上使用 Frida / objection / SSL Kill Switch 2 Hook `SecTrustEvaluate` 或认证挑战回调，使 App 固定逻辑失效；
- **重打包**：通过企业证书重签名修改后的 App，直接移除固定代码后投放安装；
- **降级探测**：对同一服务的多个端点逐一测试，寻找未实施固定的接口（大量 App 的训练 / 配置接口是薄弱点）。

对防御方的启示：证书固定必须**全端点覆盖**，且校验逻辑要能抵抗 Hook——例如将校验关键结果与业务逻辑深度耦合，或以服务器端证明（如基于应用层令牌的完整性校验）作为补充。

## 5.2 ATS 绕过与降级利用

ATS 作为系统机制无法被"漏洞绕过"，实际攻击遵循"找配置弱点"的思路：

1. **静态分析**：解包目标 App 读取 Info.plist，枚举 `NSExceptionDomains` 与 `NSAllowsArbitraryLoads`。凡是被放行的域名，即为可明文监听的候选目标；
2. **通道识别**：定位不经过 CFNetwork 高层 API 的流量（BSD Socket、部分游戏引擎与 P2P / 音视频库），这类流量天然不受 ATS 约束；
3. **降级组合**：若某域名允许 TLS 1.0/1.1，可结合历史版本协议弱点或证书信任滥用实施降级攻击；
4. **强制门户场景**：设备连接 Wi-Fi 时的强制门户探测与登录窗口不受常规加密保护（探测域名 captive.apple.com 走明文），攻击者可在该窗口实施钓鱼与注入。

## 5.3 无线与近场攻击面

- **Evil Twin / 伪造热点**：以相同 SSID 与更强信号诱使设备关联，配合恶意 DHCP / DNS 实施流量劫持；若目标网络使用 WPA2-Enterprise 且受害设备配置了自动连接，还可设置伪造认证服务器捕获认证材料；
- **强制门户劫持**：篡改门户页面诱导输入凭据或安装描述文件；
- **802.11 帧注入类攻击**：基于 wpa_supplicant 生态的过往漏洞与协议缺陷，实施去认证风暴、握手捕获与离线破解；
- **AWDL / AirDrop 通道**：学术界（2020 年起多篇论文）证明 AWDL 协议存在可被用于中间人与跟踪的缺陷，Apple 后续通过引入接触验证（Closer 加密）修复部分问题；
- **蓝牙**：蓝牙协议栈历史上多次成为近场攻击入口（如 2017 年 BlueBorne 系列漏洞影响多家平台）。iOS 通过蓝牙地址随机化（iOS 12 起对外扫描随机化）、强制配对加密等降低风险，但关闭不必要的蓝牙暴露面仍是最佳实践。

## 5.4 VPN 泄漏与流量旁路

2022 年多家机构（Michael Horowitz、Proton VPN 等）公开指出 iOS VPN 的设计性泄漏问题，典型场景包括：

- 隧道进程终止或系统挂起后，**没有 Kill Switch** 阻止流量走物理网卡直连；
- 网络切换（Wi-Fi 与蜂窝互切）瞬间，新旧接口重叠导致流量从预期之外的接口发出；
- 部分系统服务（DNS、推送、Captive Portal）默认不经隧道。

对企业的意义：员工自觉开启的"个人 VPN"不能作为合规控制点，必须依赖 **Always-On VPN + 按 App VPN** 的受管策略，并配合终端上的代理规避检测（Proxy Bypass Detection）验证实际路径。

## 5.5 企业配置与描述文件滥用

描述文件（Configuration Profile）是 iOS 上权限最大的运维对象，可包含根证书、HTTP 代理、VPN、DNS 设置、Web 内容过滤等。滥用路径：

1. 通过社工、钓鱼页面或受控 Wi-Fi 门户诱导用户安装"网络加速 / 企业凭证"描述文件；
2. 描述文件内注册攻击者根证书并指向代理，此后设备上全部未固定证书的 HTTPS 流量均可被解密；
3. 若设备已加入 MDM，攻击者可通过受管通道下发同样配置，隐蔽性更强。

防御要点：iOS 会在设置中显示"已安装描述文件"提示，企业应通过 MDM 强制审计描述文件清单，并定期导出设备配置基线进行比对。

# 六、防御建议

## 6.1 终端用户

1. **及时更新系统**：绝大部分公开在野利用漏洞的唯一有效缓解就是升级；开启自动更新；
2. **管理证书信任**：定期检查"设置 - 通用 - 关于本机 - 证书信任设置"，移除不明根证书；不安装来路不明的描述文件（设置 - 通用 - VPN 与设备管理）；
3. **高风险人群启用锁定模式**：记者、人权工作者、企业高管建议评估启用，并同步在 Mac 等其他设备开启；
4. **开启 iCloud 私有中继与 Safari 反追踪**：降低网络侧流量画像能力；
5. **谨慎使用公共 Wi-Fi**：关闭自动加入，外出办公优先使用蜂窝网络或个人热点；连接公共网络时避免登录敏感账户。

## 6.2 开发者

1. **收紧 ATS**：除确有理由，禁用 `NSAllowsArbitraryLoads`；对例外域名设置过渡期与定期复核机制；关注 Xcode 静态检查与 App Store 审核对例外项的质询；
2. **实施并维护证书固定**：优先使用 SPKI 固定并内置**备用固定串**（备份 CA / 新公钥），设计证书轮换演练流程；对高敏感接口叠加应用层签名机制；
3. **使用现代传输协议**：启用 TLS 1.3、HTTP/3（QUIC）；敏感数据同时启用应用层端到端加密，不依赖单一传输层保障；
4. **不信任设备侧可被篡改的判断**：关键安全决策（风控、授权）应在服务端完成，客户端固定的主要价值是提高攻击成本而非绝对防御；
5. **凭据安全管理**：令牌与密钥存入 Keychain 并设置正确的访问属性（kSecAttrAccessibleWhenUnlockedThisDeviceOnly 等）；避免在日志、崩溃报告与备份中泄露敏感字段；
6. **安全测试**：在 CI 中集成动态分析（如越狱环境下的固定绕过测试）与依赖审计，持续验证上述控制的有效性。

## 6.3 企业与组织

1. **MDM 基线加固**：强制密码策略与设备合规检查；禁用非受管描述文件安装；启用受管 Wi-Fi 配置并锁定 `CVE-2025-31216` 同类覆写风险面；
2. **网络路径控制**：部署 Always-On VPN / 按 App VPN，统一出口，禁止分流；对高价值岗位试点锁定模式并纳入支持流程；
3. **补丁管理 SLA**：以 Apple 安全公告与 CISA KEV 目录为输入，对"已确认在野利用"的漏洞设置小时级处置 SLA，普通高危漏洞 7 天内完成覆盖；
4. **监测与威胁狩猎**：采集 VPN 连接日志、证书与配置变更、异常代理设置变化；对移动管理通道进行配置差异比对，及时发现植入性配置；
5. **人员与制度**：对高价值目标开展定向攻击意识培训（描述文件、门户钓鱼、假冒支持人员）；建立设备失窃后的远程擦除与凭据吊销预案；
6. **供应链审视**：审核移动端 SDK 的网络行为与数据流向（第三方 SDK 是 ATS 例外与明文上报的高发区）。

# 七、结论与趋势展望

iOS 的网络安全体系建立在分层防御与最小信任的原则之上：系统框架层的 ATS 与信任链、消息通道的 BlastDoor 隔离、硬件层的固件隔离与基带独立化，共同构成了业界领先的移动网络防护基线。然而从 2017 年的 Broadpwn、KRACK，到 2021-2023 年的 FORCEDENTRY、BLASTPASS、Operation Triangulation，再到 2025 年 C1 基带的首个漏洞（CVE-2025-31214）与 2026 年在野利用的 CVE-2026-20700，公开案例反复验证三条规律：

1. **投递通道集中**：消息与无线通道是零点击攻击的主要入口，防护重点应围绕"解析隔离 + 功能面收缩"持续投入；
2. **防御取决于最低配置**：ATS 例外、未固定的接口、未强制 VPN 的设备，是攻击者最常利用的"短板"，安全效果取决于策略落地率而非系统默认能力；
3. **攻击链组件化**：单个漏洞的价值在于其在链中的位置，任何一层修补都只是抬高成本，纵深防御与快速更新仍是最可靠的组合策略。

对防御方而言，可执行度最高的三件事是：**保持系统更新、启用锁定模式（高风险人群）、在企业内落地"受管网络路径 + 配置审计 + KEV 驱动的补丁 SLA"**。

# 八、参考资料

Apple 官方安全公告与内容：

- About the security content of iOS 18.5 and iPadOS 18.5（CVE-2025-31214 / 31216 / 43374 来源）- https://support.apple.com/en-il/122404
- 关于 iOS 18.5 和 iPadOS 18.5 的安全性内容 - https://support.apple.com/zh-cn/122404
- About the security content of iOS 18.3.2 and iPadOS 18.3.2（CVE-2025-24201）- https://support.apple.com/en-ge/122281
- About the security content of iOS 26 and iPadOS 26 - https://support.apple.com/en-us/125108

公开漏洞库与研究文献：

- NVD：CVE-2025-24201 详情 - https://nvd.nist.gov/vuln/detail/CVE-2025-24201
- Qualys ThreatProtect：Apple 修复影响 iOS 设备的 WebKit 零日漏洞（CVE-2025-24201），2025-03 - https://threatprotect.qualys.com/2025/03/12/apple-addressed-webkit-zero-day-vulnerability-impacting-ios-devices-cve-2025-24201/
- CSIRT 公告：WebKit 越界写与沙箱逃逸分析 - https://csirt.cy/cve/2025/out-of-bounds-write-vulnerability-in-webkit-leading-to-sandbox-escape
- OffSeq Radar：CVE-2025-31216（Wi-Fi 配置覆写）条目 - https://radar.offseq.com/threat/cve-2025-31216-an-attacker-with-physical-access-to-4be80ab9
- Help Net Security：Apple 修复被在野利用的零日 CVE-2026-20700（dyld 内存破坏），2026-02 - https://www.helpnetsecurity.com/2026/02/12/apple-zero-day-fixed-cve-2026-20700/
- Security Affairs：Apple 修复 2026 年首个在野利用零日 - https://securityaffairs.com/187890/security/apple-fixed-first-actively-exploited-zero-day-in-2026.html
- CyberScoop：Apple 2025 年 5 月大规模安全更新（含 C1 基带隐私修复）报道 - https://cyberscoop.com/apple-security-update-c1-modem-privacy-fixes-may-2025/
- CISA Known Exploited Vulnerabilities（KEV）目录 - https://www.cisa.gov/known-exploited-vulnerabilities-catalog

技术案例原始研究（按报告正文引用顺序）：

- Nitay Artenstein（Exodus Intelligence），"Broadpwn: Remotely Compromising Android and iOS via a Bug in Broadcom's Wi-Fi Chipsets"，Black Hat USA 2017；
- Mathy Vanhoef（KU Leuven），"Key Reinstallation Attacks: Forcing Nonce Reuse in WPA2"，CCS 2017（项目主页：krackattacks.com）；
- Google Project Zero，"A deep dive into an NSO zero-click iMessage exploit: Remote Code Execution"（FORCEDENTRY 分析），2021；
- Citizen Lab（多伦多大学），"BLASTPASS: NSO Group iPhone Zero-Click, Zero-Day Exploit Captured in the Wild"，2023；
- Kaspersky GReAT，"Operation Triangulation"系列报告，2023-2024；
- Michael Horowitz / Proton VPN 关于 iOS VPN 泄漏的公开研究，2022。

> 说明：本报告中的 CVE 编号、影响版本与修复版本均以 Apple 官方安全公告及 NVD 记录为准；技术机理描述综合上述公开研究文献。

---

咨询ios系统请咨询 telegram：https://t.me/one00190
