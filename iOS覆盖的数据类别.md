@@TITLE@@iOS 覆盖的数据类别全景：设备数据资产分类、保护强度与治理映射
@@SUBTITLE@@Data Asset Taxonomy, Protection Strength and Governance Mapping for iOS Devices
@@INFO@@报告日期|2026年10月18日
@@INFO@@分析对象|iOS 设备及其云/伴生生态所覆盖的数据类别：存储位置、保护级别、流转路径、删除语义与合规映射
@@INFO@@影响范围|iPhone、iPad、Apple Watch、AirPods、Apple TV、CarPlay、HomeKit 与空间计算设备构成的 Apple 设备生态
@@INFO@@分类|网络安全 · 数据治理 · 隐私保护
@@INFO@@难度|中级偏高级（需了解 iOS 数据保护机制与移动隐私合规基础）
@@INFO@@联系方式|Telegram: https://t.me/ios_one001

@@TOC@@目录

# 概述

## 1.1 为什么资产清单必须先于 iOS 安全工程

任何针对 iOS 设备的安全评估、审计或合规落地，最终都要回答一个三元组问题：**某一类数据，位于某个存储位置，处于某种保护强度之下**。功能视角的描述（"已开启双重认证""已加密备份"）无法回答这类问题，因为它把保护当作设备的属性而非数据的属性。同一台 iPhone 上，一条短信正文、一个钥匙串项、一份缩略图缓存与一条诊断日志所受的约束可以相差数个数量级；把它们统称为"设备数据已加密"是在掩盖风险而非陈述事实。

资产清单视角（asset-centric inventory）的价值有三点。第一，它把**保护强度与数据类别绑定**，使"某类数据是否值得额外控制"成为可判定的问题：《Apple Platform Security》（26.5 版，下称《PSG》）把第三方 App 数据的默认文件保护类明确为 `NSFileProtectionCompleteUntilFirstUserAuthentication`，这意味着 unlocked-after-first-boot 的取证窗口对绝大多数 App 数据是结构性存在的，而不是某个 App 的实现缺陷。第二，它把**流转路径独立成列**：一份本机处于最高保护类的文件，一旦被纳入 iCloud 备份，其密钥保护域即从"用户口令 + 设备 UID"迁移到 iCloud Backup keybag 与 Apple 数据中心 HSM，资产的实际暴露面随之改变。第三，它让**残余与删除语义**成为可讨论对象：苹果文档只对少数场景给出明确表述（HealthKit 临时 journal 在合并完成时删除、开启 Advanced Data Protection 时 HSM 内 available-after-authentication 密钥的删除是"immediate, permanent, and irrevocable"），其余多数类别的删除后果属公开资料未披露，清单方法能把这些空格显式标为空格，而不是默认成"已擦除"。

本报告采用这一视角，把 iOS 设备与其云/伴生生态覆盖的数据类别拆解为可登记的资产行，逐行填写存储基质、保护级别、流转路径、删除语义与法定分类五个字段。

## 1.2 与系列既有"采集视角"分析的分工

本系列此前的若干篇已覆盖攻击者视角的数据采集问题：特定监控软件在设备上读取了哪些数据、以何种权限模型接入、如何被归属与指派。本报告**不重复该角度**，也不提供采集能力的排名或案例复盘。两者的分工可以概括为下表。

| 维度 | 采集视角（系列既有分析） | 资产视角（本报告） |
|---|---|---|
| 起点 | 恶意程序/入侵集合的权限与行为 | 设备上真实存在的数据类别 |
| 核心问题 | 攻击者能拿到什么 | 我们持有什么、受什么保护、如何离开设备 |
| 单位 | 样本、家族、利用链 | 资产行、保护类、密钥域 |
| 输出 | 检测特征、 IOC、归属判断 | 分类框架、风险标签、治理映射 |
| 缺失时的后果 | 检测盲区 | 控制错配、合规口径不可辩护 |

因此，盗窃、执法调取、供应链失陷等场景在本报告中**只作为资产行上的风险标签出现**（例如"设备失窃且口令强度不足"），不作为叙事主线，也不构成操作指引。报告不含利用代码、不含提取或绕过流程、不含操作性攻击步骤。凡涉及"某类数据在失陷情形下的可得性"，均以保护类与密钥域的存在性判断表述，而非以攻击可行性表述。

本报告的使用方式随读者角色不同而不同，下表给出三种读法，以避免把清单当作阅读顺序。

| 读者角色 | 建议读法 | 主要取用的章 |
|---|---|---|
| 平台/移动安全负责人 | 先读第 2 章框架与各章的登记表，再回到第 1 章核对置信级 | 1、2，之后按需取 3–9 |
| 隐私合规与法务 | 以第 2.5/2.6 的映射与调和规则为主，配合各章"出设备路径"列 | 2、4、6、8 |
| 设备管理与企业客户端 | 以 3.6、4.1、6.2、6.6、9.5、9.6 的控制点为主 | 3、6、9 |
| 个人风险自查 | 以每章末尾的治理结论与风险标签为检查表 | 5、6、7、8 |

需要提醒的是，各章末尾的"治理结论"是**推断层**，其强度取决于该章一手条款的密度：健康（6.2）、备份与 ADP（4.1、4.2）、生物特征（7.1）三处一手支持最厚；位置历史（5.2）、索引留存（9.3）与外设数据类别（9.6 后半）最薄。据此，前者的结论可直接进入控制文档，后者应进入"待向厂商问询"清单。

## 1.3 证据基线、版本文落差与置信分级

本报告的机制性陈述以**苹果一手文档**为准，并遵循一个硬性约束：文档名 + 版本文号必须与结论同现。这里存在一个不可忽略的版本落差——本次核验所加载的《PSG》PDF，其文内版本文号为 iOS/iPadOS/macOS/tvOS/visionOS/watchOS **26.5**；而苹果 *Apple security releases* 页面在核验时点（2026-10-18）载明当前最新版本为 **iOS 与 iPadOS 27.0.1**（发布于 2026-09-28，且该更新无公开 CVE 条目），iOS 27 与 iPadOS 27 的发布日期为 2026-09-14，安全内容文档适用于 iPhone 11 及后续机型。

因此，凡引用《PSG》的机制描述，本报告一律写作"**《PSG》（26.5 版）描述……**"，绝不写作"iOS 27 的行为"。27.0.1 相对 26.5 在数据类别与保护类语义上是否有变更，本次未逐项核对，读者应以最新一版《PSG》为准复核。

全文采用三级置信标记，与源简报一致：

| 标记 | 含义 | 写作处理 |
|---|---|---|
| 已核实 | 已实际加载苹果一手页面/PDF 并摘录，含精确属性名与条款位置 | 直接陈述，附文档与版本 |
| 部分核实 | 仅从检索摘要或跨文档间接得到，未逐条加载原文 | 加限定语：「按苹果 iPhone 用户指南所述」「按该支持页所述」 |
| 未核实 | 一手来源中未定位到披露 | 写作「公开资料未披露」「苹果未公开其内部实现细节」「公开分析认为」 |

另有一条独立约束：本次**未核验任何法律条文页面**（apple.com/legal/privacy 系列仅 Apple Advertising 一项被加载）。因此第 2 章及各章的监管映射均须读者理解为**分析性推断**，用于治理落地时的对照工具，不构成法律意见，也不代表苹果对自有数据作了法定分类。

## 1.4 每个类别必须回答的四个问题

本报告为每一资产行规定了四问，作为清单的最小完备集，并附加第五问用于合规。四问及其证据来源类型如下。

| 问题 | 需要落到什么粒度 | 主要一手证据类型 | 典型误答 |
|---|---|---|---|
| 一、在哪里 | 区分文件系统/数据容器、钥匙串、Secure Element/Secure Enclave、索引库、iCloud 服务域、本地或云端备份、内存驻留 | 《PSG》Data Protection 章节、iCloud Backup security、各服务专章 | "在 App 沙盒里"（未指明密钥域） |
| 二、受什么保护 | Data Protection 类名（如 `NSFileProtectionComplete`）、keychain accessibility 属性（如 `kSecAttrAccessibleWhenUnlocked`）、keybag 类型、是否端到端 | 《PSG》§Data Protection classes（pp.92–93）、§Keychain data protection（p.101）、*iCloud data security overview* | "已加密"（未说明密钥在谁手里） |
| 三、怎么离开设备 | 同步（iCloud 域）、备份（iCloud/本地）、共享（链接/协作/相册）、诊断（分析数据上传）、MDM 受管通道、伴生设备链路 | 《PSG》各同步章节、§Device management（pp.230–237）、iCloud 安全总览表 | 只列 iCloud，忽略备份与遥测两条降级通道 |
| 四、删了还剩什么 | 密钥销毁 vs 密文销毁、临时文件合并后删除、内存类密钥丢弃、可恢复窗口 | 《PSG》明确表述的少数条目 | 把"用户删除"等同于"数据不可恢复" |
| 五（附加）、法定分类 | 是否构成个人信息/敏感个人信息、特殊类别数据 | 由撰写者作分析性推断（见 1.3 约束） | 把 App 隐私标签等同于法定分类 |

四问的意义在于**保护强度与流转路径是两条独立的轴**。iOS 的一个反复出现的结构性事实是：本机保护类很高但云端密钥域更弱的类别（典型是 iCloud 备份所纳入的、处于锁定态不可访问类别的文件，其 per-file key 由 iCloud Backup keybag 包裹后上传），以及本机与云端均端到端但元数据不覆盖的类别（典型是 iMessage 内容与其时间戳、APNs 路由信息）。清单化能防止把"该类别已加密"误当成"该类别受控"。

## 1.5 核心发现前置

以下 12 条为全篇结论摘要，均可在后续章节中找到对应的文档出处与置信标记。

1. **默认即弱类**：《PSG》（26.5 版）载明 `NSFileProtectionCompleteUntilFirstUserAuthentication` 是所有第三方 App 数据的默认类，且该类的密钥在上锁/登出后不移除。清单中"未显式指派保护类"应一律按 Class C 计，而非按最高类计。
2. **备份是主要降级通道**：iCloud 备份按原文包含"device settings, app data, photos, and videos in the Camera Roll, and conversations in the Messages app"；锁定态不可访问类别的文件，其 per-file key 改用 Curve25519 的 iCloud Backup keybag 包裹，且**不使用 iCloud 口令做加密**，改口令不会使旧备份失效。
3. **端到端覆盖的口径存在文档冲突**：*iCloud data security overview* 当前表述为"标准保护下 15 个类别端到端加密，开启 ADP 后升至 25 个类别"，而《PSG》（26.5 版）表述为"从 14 增至 23"。本报告以支持页为主口径并保留该冲突，不混用两组数字。
4. **互通性决定的永久例外**：iCloud Mail、Contacts、Calendars 因需与全球邮件/日历/通讯录系统互通，两处来源一致明确其不支持端到端加密；Reminders 经 CalDAV 同步的部分同样例外。
5. **元数据是系统性短板**：iMessage 的时间戳与 APNs 路由信息不加密；CloudKit 的修改日期/时间（用于排序）与文件/照片校验和（用于去重，即收敛加密）不端到端；"anyone with a link" 分享的标题与代表缩略图以标准数据保护存储；Apple Invites 的时间/日期、城市级位置与海报图发布后始终以标准保护。
6. **少数类别的本机保护强度高于其内容类**：Face ID/Touch ID/Optic ID 数据（含数学表征与采集影像）按《PSG》明确不入备份；Apple Pay 的 Device Account Number"never stored on Apple Pay servers or backed up to iCloud"；本地钥匙串虽可入备份但用 Secure Enclave UID 根密钥派生密钥加密，只能还原到同一台设备。
7. **健康数据的保护类取决于"是否在锁屏下需要可用"**：HealthKit 中心库为 Protected Unless Open，上锁 10 分钟后放弃访问权，但 `HKWorkoutSession` 期间锁屏下可持续访问；管理数据（App 访问权限、连接的设备名、唤起调度）落在较弱的 Until First User Authentication 类。
8. **删除语义的一手支持极其有限**：苹果只支持"密钥级删除"的少数表述（Class D 密钥置于 Effaceable Storage 以实现快速远程抹除、Protected Unless Open 文件关闭即从内存清除 per-file 密钥、HealthKit 临时 journal 在合并完成时删除、ADP 开启时 HSM 密钥立即永久不可撤销删除）。闪存级残留与取证可行性属公开资料未披露，任何"已安全擦除"的断言都缺乏一手依据。
9. **MDM 的采集面在苹果文档中是"授权式"而非"枚举式"**：注册描述文件明确授权服务端"send commands and configurations to the device and to query certain aspects of the device"，但苹果未在此列出可查询字段清单；命令级清单需另行核对 MDM 协议参考。任何"MDM 可读短信/照片"的断言在本次一手材料中无依据。
10. **索引与遥测是最易被低估的资产层**：Spotlight 索引、Siri 建议、键盘个人词典、Safari 历史、屏幕使用时间、HomeKit 事件日志等在单项上常被当作"派生缓存"降级处理，但它们彼此可在设备端聚合成人物级画像。苹果在 iOS 27 安全内容文档中已登记多起与索引/遥测相邻的越权读取缺陷（见 9.1、9.4 的引用），说明这一层不是抽象风险。
11. **标识符是一级类别而非附属属性**：iOS 27 安全内容文档登记的 CloudKit 条目 "A local app may be able to read a persistent account identifier"（CVE-2026-86895）与 iCloud 条目 "An app may be able to identify a user across reinstalls"（CVE-2026-84606）表明，持久化标识符本身即是被苹果承认为独立泄露对象的资产。
12. **Apple Intelligence 与 Private Cloud Compute 不在本次证据基线内**：《PSG》（26.5 版）全文检索 "Apple Intelligence" 无命中，故本报告不以《PSG》描述其数据外发语义；相关边界属公开资料未披露，须以苹果专项页面另行核对。

## 1.6 范围边界与苹果未公开的内容

明确边界比扩大声明更有用。本报告的范围限定如下。

- **只覆盖数据类别**，不覆盖传感器与认证子系统的安全工程细节；生物特征一章仅处理"表征数据作为资产"的存储/流转/删除属性，认证实现与活体检测由系列其他篇覆盖。
- **不覆盖macOS 服务器端、企业自建 CloudKit 之外的第三方云**；第三方 App 只在类别意义上出现。
- **不给出法定分类结论**；监管映射为对照工具，标注为分析性推断。
- **机型可用性以文档为限**：涉及具体功能与机型门槛（如 Apple Intelligence 支持机型）时按苹果 iPhone 用户指南所述引用，并保留本次未逐条加载正文的限定。

以下事项在本次核验的一手材料中**没有可引用的披露**，全篇以限定语处理，读者在据此制定控制措施时应视为"未知"而非"无风险"：第三方 App 推送载荷的一般留存时长与 Apple 侧可见字段；设备令牌与账号/设备标识的关联策略；Siri 与听写音频/转写的具体留存天数；RCS 的服务器留存、附件加密、群聊、备份与跨平台历史迁移，以及三星一侧的 RCS 表述；Sign in with Apple 邮件中继的转发地址格式、速率与收件人上限、停用与回收、与 Hide My Email 及 iCloud+ 的关系；AdSupport 层面 IDFA 的当前可用性与限制表述、AdAttributionKit 与 SKAdNetwork 的替代时间表；CarPlay、AirPods、vision 类外设所产生数据类别的清单（含 Persona、空间捕捉、眼动与手部追踪数据）；HomeKit Secure Video 的具体保存天数与事件上限；MDM 命令级可采集字段；App Privacy Details 的数据类别枚举与"是否关联身份"矩阵；隐私清单（PrivacyInfo.xcprivacy）机制正文；Lockdown Mode 的逐条完整限制清单；文件内容级/闪存级的删除残留与取证可行性；透明度报告是否覆盖内容型数据请求。

为便于后续章节引用，本报告使用的机制术语速查如下（均以《PSG》（26.5 版）或所列支持文档的口径为准）。

| 术语 | 英文 | 在本报告中的含义 |
|---|---|---|
| 数据保护类 | Data Protection class | 文件级加密档位，由类密钥的可用时机定义；四档为 `NSFileProtectionComplete`、`NSFileProtectionCompleteUnlessOpen`、`NSFileProtectionCompleteUntilFirstUserAuthentication`、`NSFileProtectionNone` |
| 每文件密钥 | per-file key | 每个文件（或 extent）独立的密钥，由类密钥包裹（wrap） |
| 密钥袋 | keybag | 存放类密钥的结构；iOS 上类型为 user、device、backup、escrow、iCloud Backup 五种 |
| 可抹除存储 | Effaceable Storage | 存放 Class D 类密钥的位置，其价值在于可被快速销毁以实现远程抹除 |
| 设备唯一标识密钥 | device UID | 硬件绑定密钥，与用户口令共同参与 Class A 类密钥派生 |
| 安全飞地 | Secure Enclave (SEP) | 处理生物特征数学表征与类密钥的隔离执行环境；配套 Secure Neural Engine 每次执行后重置状态 |
| 安全元件 | Secure Element | Apple Pay 的 Java Card 平台（EMVCo 与 Common Criteria 认证），存放 Device Account Number |
| 托管密钥袋 | escrow keybag | 涉证/口令恢复用途的密钥结构，会暴露"类密钥列表：Key UUID 与所属 Data Protection class" |
| 高级数据保护 | Advanced Data Protection (ADP) | 把更多 iCloud 类别转为端到端加密的账户级开关 |
| 认证后可用密钥 | available-after-authentication key | 默认存于苹果数据中心 HSM 的 iCloud 备份密钥类别 |
| 身份服务 | Apple Identity Service (IDS) | 存放 iMessage 公钥、APNs 地址与关联电话号码的目录服务 |
| 推送服务 | Apple Push Notification service (APNs) | 唤醒与中继通道；设备管理场景原文声明"不传输机密或专有信息" |
| 云数据框架 | CloudKit | App 侧云存储与同步机制；字段需开发者显式声明为 encrypted 才受端到端保护 |
| 收敛加密 | convergent encryption | 以校验和实现去重的机制，苹果据此说明文件/照片校验和不端到端 |
| 应用追踪透明度 | App Tracking Transparency (ATT) | 追踪授权门控框架，符号如 `ATTrackingManager` |
| 归因回传 | postback（AdAttributionKit） | 安装/转化事件的回传机制，与 IDFA 追踪不同 |
| 设备账号 | Device Account Number (DAN) | 发卡方生成的令牌化卡号，存于 Secure Element，不入服务器与备份 |
| 安全视频 | HomeKit Secure Video (HSV) | 经 home hub 本地分析并以 AES-256-GCM 加密上传的家居录像链路 |
| 家庭中枢 | home hub | Apple TV / HomePod 类设备，承担中继与本地事件分析 |
| 受管注册 | MDM enrollment（User Enrollment / Device Enrollment / ADE） | 设备管理登记形态，决定配置与查询面及数据分离程度 |
| 隐私标签 | App Privacy Details | App Store 上的开发者自我声明数据收集矩阵，非法定分类 |

---

# 数据资产分类框架

## 2.1 四条分类轴及其交叉

单轴分类（仅按敏感度或仅按来源）无法表达 iOS 的真实控制差异。本报告用四条正交轴，任何资产行都是四轴上的一个坐标点。

**轴一：敏感度层级（sensitivity tier）**。见 2.2。它是治理结论轴，回答"该类别的泄露后果量级"。

**轴二：生成主体（generating subject）**。它决定责任归属与授权模型，也决定"谁有权对该类别作变更"。

| 生成主体 | 定义 | iOS 例 | 治理含义 |
|---|---|---|---|
| 系统服务（system） | 由 OS 或苹果服务直接产生与持有 | iMessage 数据库、Data Protection keybag、诊断日志、Spotlight 索引 | 用户只有开关级控制，无字段级控制；保护语义以《PSG》为准 |
| 用户显式输入（user-explicit） | 用户在 UI 中主动创建 | 备忘录、通讯录条目、健康 App 手动录入项、Medical ID | 责任在用户；删除语义最接近"用户意图" |
| 第三方 App 与 SDK（app/SDK） | App 进程或其嵌入 SDK 产生 | App 容器文件、App 钥匙串项、AdAttributionKit postback 相关数据、上传给开发者的分析 | 受 entitlement、沙盒、隐私授权与 ATT 约束；默认文件类为 Class C |
| 传感器与外设（sensor/companion） | 由设备传感或伴生设备产生 | Face ID 采集的数学表征、HealthKit 来自 Watch 的记录、HomeKit 摄像头剪辑、车机与耳机链路数据 | 数据可能不经 App 层，隐私授权模型对其约束有限 |
| 派生推断（derived-inference） | 由上述输入经模型或聚合生成 | 照片库人物/场景/宠物分类索引、Siri 建议、键盘学习词典、常去地点与个性化建议、屏幕使用时间聚合 | 敏感度跃升的主要来源，也是最常漏登记的一类 |

派生推断类是清单的核心难点：它没有独立的"录入"动作，因而在传统设置盘点中不可见；但它的聚合能力高于任何单一原始类别。

**轴三：存储基质（storage substrate）**。它决定保护机制的可引用证据类型。

| 存储基质 | 保护机制参照 | 代表类别 |
|---|---|---|
| 文件系统（加密文件 + per-file key） | `NSFileProtectionComplete` / `NSFileProtectionCompleteUnlessOpen` / `NSFileProtectionCompleteUntilFirstUserAuthentication` / `NSFileProtectionNone` | App 容器数据、媒体、下载内容 |
| 数据库/中心库 | 由所属子系统指派类（如 HealthKit 中心库为 Protected Unless Open） | 消息库、健康库、通讯录库 |
| 钥匙串（keychain） | `kSecAttrAccessibleWhenUnlocked`、`kSecAttrAccessibleAfterFirstUnlock`、`kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly` 等 accessibility 属性 + 访问组 | 凭证、令牌、通行密钥 |
| 安全硬件内 | Secure Enclave（数学表征、类密钥）/ Secure Element（Device Account Number） | 生物特征表征、支付令牌 |
| 云端服务域 | 标准保护 vs ADP；端到端密钥在受信设备或 Apple HSM | iCloud Drive、Photos、Notes、Keychain |
| 备份 | iCloud Backup keybag（Curve25519）/ 本地备份加密 | 设备与消息备份 |
| 索引/建议层 | 多数未逐项披露，需按派生资产推定 | Spotlight、Siri 建议、照片分类索引 |
| 内存驻留 | 类密钥的丢弃时机（如上锁约 10 秒后丢弃 Class A 类密钥、Protected Unless Open 文件关闭即清除 per-file 密钥） | 解锁后驻留的解密密钥、临时 journal |

**轴四：生命周期阶段（lifecycle stage）**：创建 → 驻留 → 索引/派生 → 同步 → 备份 → 共享 → 归档 → 删除 → 残留。治理控制必须绑定到阶段而非类别，因为同一类别在不同阶段的保护强度不同（iMessage 内容在"驻留"阶段受本机最高类保护，在"备份"阶段则视 iCloud 备份开关而落入不同密钥域）。

## 2.2 敏感度分层模型

本报告使用五级模型。层级判定采用**后果导向**（泄露后果 + 可聚合性 + 不可撤销性），而非内容主观判断。T 级与《PSG》保护类之间**没有对应关系**，两者必须分别填写：高 T 级配弱类是 iOS 上真实存在且常见的组合。

| 层级 | 名称 | 判定要点 | iOS 代表类别 |
|---|---|---|---|
| T4 | 特殊/不可撤销 | 一旦泄露无法通过更换消除；或直接指向身体、心智、性、精确轨迹 | Face ID/Optic ID 数学表征、健康记录、生育周期数据、精确定位轨迹、Device Account Number 之外的支付凭证材料 |
| T3 | 高敏感授权材料 | 可直接导致其他资产失控 | 钥匙串凭证与通行密钥、iCloud Keychain 内容、恢复密钥、Siri 语音原始音频（如留存） |
| T2 | 个人内容 | 指向特定个人的表达与关系 | 消息正文、备忘录、照片原片、邮件、语音留言、联系人关系图谱 |
| T1 | 半公开/受限派生 | 单独看后果有限，聚合后跃升 | 应用列表、书签与阅读列表、Spotlight 索引、崩溃报告、诊断日志、HomeKit 事件 |
| T0 | 设备/运行元数据 | 主要服务于运维与互通 | 机型与版本、设备名、硬件兼容信息、Wi-Fi 配置的技术字段 |

三条判定规则值得单独强调。**其一，不可撤销性优先于内容敏感度**：设备标识类（如持久化账户标识符）在内容上不含个人信息，但因不可更换，其在治理上应按 T2/T3 处理；苹果把 "identify a user across reinstalls"（CVE-2026-84606）作为独立缺陷条目登记，正说明这一点。**其二，聚合跃升规则**：三个及以上 T1 类在同一设备上可被同一主体关联到自然人时，登记层级应上调一级；轨迹重建即典型案例（见 5.6）。**其三，元数据独立计层**：不因"只是元数据"而默认 T0。

T 级与苹果的保护档之间不存在换算关系，但把"高 T 低档"与"低 T 高档"两组实例并列出来，可以最快说明为什么必须两列同时填写。

| 组合类型 | 实例 | 一手依据 | 治理启示 |
|---|---|---|---|
| 高 T + 强档 | Medical ID 之外的健康记录：T4，且中心库为 `NSFileProtectionCompleteUnlessOpen`、上锁 10 分钟后失去访问 | 《PSG》（26.5 版）§HealthKit | 少数"层级与档位一致"的正例，可作为对外举证模板 |
| 最高 T + 明文排除流转 | Face ID/Optic ID 数学表征：T4，且不离开设备、不入备份 | 《PSG》（26.5 版）§Optic ID, Face ID, and Touch ID 与备份章节 | 唯一类"结构性不外流"，适合承载不可撤销机密 |
| 高 T + 弱档 | 第三方 App 内的私密文档：T3/T4，但默认 `NSFileProtectionCompleteUntilFirstUserAuthentication` | 《PSG》（26.5 版）默认类为 Class C | 需要按 App 逐个确认是否显式提升；默认即弱 |
| 中 T + 弱档 + 服务端可见 | 通讯录与日历：T2/T3，且 iCloud Contacts/Calendars 不支持端到端 | *iCloud data security overview* | 用户最难以通过开关改善的一类 |
| 低 T + 强档 | Wi-Fi 密码、W1/H1 蓝牙密钥、Memoji：单独看为 T1，但列在标准即端到端集内 | 102651 表格 | 苹果对"可重建设备关系"的项给予端到端，可援引为内部先例 |
| 高 T + 本机不可移植 | 备份中的本地钥匙串：T3，且以 Secure Enclave UID 根密钥派生密钥加密，只能还原到同一设备 | 《PSG》（26.5 版）§iCloud Backup security | 备份纳入不等于可读，须区分"入备份"与"可解密" |

## 2.3 标识符与元数据作为一级类别

传统盘点把标识符与元数据当作其他资产的属性附注，iOS 的实际情况要求把它们提升为独立资产行。理由有三：其一，它们的**存储位置与主数据不同**（iMessage 公钥、APNs 地址与关联电话号码存放在 Apple Identity Service 目录中，而非设备上的消息库内）；其二，它们的**保护级别常低于主数据**（《PSG》（26.5 版）明确 iMessage 的时间戳与 APNs 路由信息不加密；CloudKit 的修改时间与校验和不作端到端加密）；其三，它们的**流转与删除由服务端控制**，用户侧开关无法覆盖。

| 标识符/元数据类别 | 存放位置（按文档所述） | 保护级别 | 可见方 | 删除语义 |
|---|---|---|---|---|
| Apple Account 标识符 | 账户体系与各服务域 | 服务域标准保护为主 | 苹果、受管环境（视注册类型） | 需删除账户流程；本次未核对具体时限 |
| iMessage 公钥、APNs 地址、关联电话号码 | Apple Identity Service（IDS）目录 | 目录条目，加密语义未逐项披露 | 苹果；接收方据此匹配姓名 | 苹果未公开逐项披露 |
| 消息时间戳、APNs 路由信息 | 传输层 | 按《PSG》（26.5 版）**不加密** | 链路上各中继 | 不适用（传输即产生） |
| 持久化账户标识符（CloudKit 侧） | 服务端/容器 | 未逐项披露 | 开发者的容器后端 | 苹果以 CVE-2026-86895 承认其为独立泄露对象 |
| 设备名 | 配置/服务域 | 未逐项披露 | 配对设备、部分服务 | 改名不必然清除历史引用 |
| 广告标识符 IDFA | 由 App 通过 ATT 授权读取 | 授权门控（见 3.3） | 第三方广告 SDK | 用户可重置；当前可用性表述本次未核对 |
| 开发者侧标识 IDFV | App 由系统获得的供应商标识 | 沙盒作用域 | 同一供应商的 App | 供应商全部 App 卸载后重新生成（按公开文档所述） |
| 分享标题与缩略图 | iCloud 分享操作元数据 | 标准数据保护（密钥在 Apple） | 苹果、链接持有者 | 苹果称承诺让更多此类元数据端到端 |
| 活动邀请的时间/日期、城市级位置、海报图 | Apple Invites | 发布后**始终**标准保护 | 苹果、受邀与公开可见者 | 不随 ADP 变化 |

"元数据不是残余，而是并行数据集"应成为清单表结构中的显式约定：每一主数据行必须至少伴随一行元数据行，否则清单不可用于评估泄露后果。

## 2.4 盘点方法与登记表结构

清单构建采用五步法，面向企业资产管理与个人风险审计两种规模均可复用。

第一步，**枚举存储基质**。以《PSG》Data Protection 与 keychain accessibility 章节、iCloud 安全总览表、各服务专章为骨架，列出设备侧、安全硬件侧、云侧、备份侧与伴生设备侧五个基质域，保证不遗漏"不在 App 沙盒里"的类别（如 HomeKit Secure Video 的 home hub 本地解密与分析产物）。

第二步，**逐类填四问**。对每一类别填写 1.4 的四问字段，并显式标注置信级。字段结构建议如下表，它同时是本报告各章的书写模板。

| 列 | 内容 | 填写约束 |
|---|---|---|
| 资产名 | 中文类别名（英文术语） | 用苹果文档用词，避免自造术语 |
| 基质 | 文件系统/数据库/钥匙串/安全硬件/云/备份/索引/内存 | 可多值，但须分别说明 |
| 保护 | 精确类名或 accessibility 属性名；云侧写端到端与否 | 无一手依据时写"未披露" |
| 密钥域 | 用户口令+设备 UID / 受信设备 / Apple HSM / 第三方开发者 | 决定合规谈判对象 |
| 出设备路径 | 同步/备份/共享/诊断/MDM/伴生链路 | 全量枚举，不允许"无"以外留空 |
| 删除与残留 | 苹果明确表述的删除时机；未知则写未披露 | 禁止写"已安全擦除" |
| 风险标签 | 失窃、胁迫解锁、账户接管、受管注册、备份密钥外泄 | 仅标签，不展开攻击步骤 |
| 法定分类推定 | 见 2.5 | 标注分析性推断 |

第三步，**默认类判定**。凡"未显式指派"的文件类资产一律记为 `NSFileProtectionCompleteUntilFirstUserAuthentication`；凡钥匙串项未指定 accessibility 的按行为最弱档记，并同时检查是否为 `*ThisDeviceOnly` 变体（后者按《PSG》不 iCloud 同步、不备份、不入 escrow keybag）。

第四步，**降级通道打分**。对每一类别计算"最弱保护阶段"：取本机、云、备份三条路径中的最低保护档作为该资产的实际控制强度评分。此规则会立刻暴露 iCloud 备份纳入项与元数据行的真实位置。

第五步，**聚合复核**。把 T1 及以上的派生类交叉检查一次是否可在同一主体下关联，应用 2.2 的聚合跃升规则，必要时上调层级。

下面给出三行示例登记，作为后续各章的书写范式（示例只使用可引用口径，未知项一律写"未披露"）。

| 字段 | iMessage 会话内容 | HealthKit 中心库记录 | 第三方 App 容器文档 |
|---|---|---|---|
| 在哪里 | 设备侧消息数据库；附件可上传 iCloud；会话可同步入 Messages in iCloud | 设备侧健康中心库；可选 iCloud 同步；可入加密本地备份 | App 沙盒容器；可随 app data 入 iCloud 备份 |
| 受什么保护 | 内容端到端（苹果表述）；备份纳入时密钥域变化；元数据不加密 | `NSFileProtectionCompleteUnlessOpen`；上锁 10 分钟后失去访问；`HKWorkoutSession` 期间例外 | 默认 `NSFileProtectionCompleteUntilFirstUserAuthentication`（第三方默认类） |
| 密钥域 | 受信设备 / iCloud Backup keybag / Apple HSM（视开关） | 满足 iOS 12+ 与双重认证后端到端，密钥在受信设备 | 类密钥在 keybag，锁屏后仍可用 |
| 怎么离开设备 | 同步、备份、转发（Continuity 经 APNs）、导出 | 同步、加密本地备份、Health Sharing / Share with Provider | 备份、导出、分享扩展、开发者云 |
| 删了还剩什么 | 送达即从 APNs 删除；离线队列最长 30 天；关闭 iCloud 备份后旧容器密钥仍留存 | 临时 journal 在合并完成时删除；主记录删除后的残留未披露 | 记录删除后的闪存级残留未披露；文件关闭即清除 per-file 密钥仅适用于 Class B |
| 风险标签 | 账户接管、备份密钥域、元数据聚合 | 未加密备份、授权矩阵误配、共享对象变更 | 解锁后窗口、SDK 越权、备份把多 App 归一 |
| 置信 | 已核实（《PSG》26.5 版 §Messages + 新闻室稿） | 已核实（《PSG》26.5 版 §HealthKit） | 已核实（《PSG》26.5 版 §Data Protection classes） |

清单常见的登记错误与其校正规则：

| 常见错误 | 后果 | 校正规则 |
|---|---|---|
| 把"设备已加密"当作类别级结论 | 忽略 Class C 默认类与备份密钥域迁移 | 每一行必须写出类名或 accessibility 属性名 |
| 只登记本机路径 | 漏掉同步、备份、共享、诊断、MDM 五条出设备路径 | "出设备路径"列禁止留空，无路径需写"无（并注明依据）" |
| 把端到端当作内容全域覆盖 | 忽略附件、群聊、历史容器密钥与元数据 | 按 4.6 的三项检查逐一确认适用范围 |
| 索引/派生行缺失 | 无法解释聚合后的敏感度跃升 | 每一主数据行强制伴随索引与元数据行 |
| 用"用户已删除"推断不可恢复 | 与苹果仅支持的"密钥销毁"表述不符 | 删除列只写苹果明文支持的时机，其余写未披露 |
| 以营养标签代替清单 | 系统服务与设备端派生数据完全漏登 | 按 2.6 四条调和规则执行 |

## 2.5 法规映射：把资产行对齐到法定概念

本节所有映射均为**分析性推断**：本次核验未加载任何法律原文页面，苹果亦未对其数据类别作法定分类。映射用途是治理对照，不构成法律意见；实际合规结论须由具备资质的主体按适用法域复核。

| 法规框架 | 核心概念（本报告使用方式） | 与 iOS 资产行的对齐要点 |
|---|---|---|
| NIST SP 800-122 | PII、PII 影响级别、linking / single-linking identifier、可链接性 | 2.3 的标识符表可直接映射为 linking identifier 登记；"可链接性"给出把元数据行升级为受控对象的官方语言 |
| GDPR Art.4(1)（personal data） | 任何与已识别或可识别自然人相关的数据 | 设备标识、APNs 地址、IDS 中的关联电话号码、位置与照片内嵌地理标记均落入宽定义 |
| GDPR Art.4(5)（假名化） | 需额外信息才能归因 | 未授权状态下的 IDFV、归因 postback 数据在结构上接近假名化，但"额外信息"若由同一主体持有则不构成匿名 |
| GDPR Art.9（special categories） | 健康、生物识别（用于唯一识别）、性取向/生育相关等 | 6 章与 7 章的多数行在此口径下需单独合法性基础；健康 App 与 Face ID 表征是典型对象 |
| CCPA/CPRA | personal information 与 sensitive personal information（含精确地理位置、健康与生物识别信息等） | 5 章的精确轨迹与 7 章的生物识别信息对应敏感个人信息口径；注意其以"出售/共享"为中心的义务结构与苹果生态的关系需另行判定 |
| 《个人信息保护法》 | 个人信息（第 4 条口径）与敏感个人信息（第 28 条口径：生物识别、医疗健康、金融账户、行踪轨迹等；不满十四周岁未成年人个人信息一律为敏感） | 5 章行踪轨迹、6 章医疗健康、7 章生物识别、8 章支付金融信息为四类高概率敏感项；单独同意与最小必要的落点 |
| ISO/IEC 27701 | PII 资产（PII asset）与 PII 识别关联（PII identification association）、控制主体/处理者生命周期 | 与本登记表结构最接近；建议把"出设备路径"列直接映射为跨边界流转控制 |
| 苹果 App Store 要求 | App Privacy Details（营养标签）：开发者声明收集的数据类别、用途、是否关联身份 | 见 2.6；它是**开发者自我声明**体系，不是法定分类，也不覆盖系统服务与派生索引 |

映射时须注意三个"不可直接套用"：其一，法定分类以"是否与自然人相关"判定，而 iOS 的保护类以"密钥归属与可用时机"判定，两者不构成等级换算；其二，同一条数据可能因处理目的改变分类（同一份步数数据在健康评估目的下属敏感，在设备容量规划目的下属运行元数据），故映射要写到"用途 × 类别"粒度；其三，中国法域下的"单独同意"、欧盟下的 Art.9 处理条件与美国州法的敏感个人信息清单，对同一 iOS 类别给出的义务集合并不相同，治理上应做**并集**而非交集。

## 2.6 与 App Store 隐私标签体系的调和规则

*App Privacy Details* 页面存在性与定位已核实（标题为 "App Privacy Details - App Store - Apple Developer"），其数据类别枚举与"是否关联身份"矩阵本次未逐条抓取，写作时须限定为"具体枚举以 developer.apple.com/app-store/app-privacy-details/ 正文为准"。诚实的结论是：**苹果的标签体系与法规体系不是一一对应关系**，原因有四。

| 差异点 | 具体表现 | 调和规则 |
|---|---|---|
| 粒度不同 | 标签以"类别 + 用途 + 是否关联身份 + 是否用于追踪"声明；法规以"数据是否指向自然人 + 处理目的"判定 | 以法规粒度为主档，标签作为第三方 App 的输入证据，不作为分类依据 |
| 覆盖面不同 | 标签只覆盖 App 开发者的收集行为；系统服务、派生索引、home hub 本地分析产物不在其内 | 清单必须为"系统/派生"类另立登记块，不能以标签为准做全量结论 |
| 视角不同 | 标签描述"App 收集了什么"，本报告描述"设备上存在什么、如何离开设备" | 标签的"不收集"不等于设备上不存在该类别（如仅在设备端使用） |
| 端侧处理不可见 | 设备端分类、端侧索引与不上传的推断结果通常不在标签中呈现 | 对设备端派生数据按 2.2 聚合跃升规则独立评级，而非依赖标签的"是否收集" |

由此得出四条可操作的调和规则。**规则一（声明—实测双轨）**：把营养标签声明作为第三方数据流的预期基线，把 MDM 可查询面、备份纳入面与诊断上传面作为实测面，二者差值即为需复核项；标签存在不等于控制有效。**规则二（密钥域优先）**：法定归因判定优先看密钥在谁手里——端到端且密钥在受信设备的类别（如按 102651 表格在标准保护下即端到端的 Health data、Screen Time、Maps 等）在处理者义务上更接近"纯数据主体侧"，而密钥在 Apple 的类别则需要把苹果纳入处理链评估。**规则三（元数据补齐）**：凡引用标签结论，须同时检查该类别对应的元数据行（2.3），因为标签不声明苹果侧留存的时间戳与路由信息。**规则四（追踪与归因分离）**：App Tracking Transparency 的授权门控针对"追踪"，AdAttributionKit 的归因回传是另一套机制，两者的数据集合与法定概念映射不同（见 3.3、3.4），不可合并为一行登记。

需要提示的失效场景：把营养标签直接当作"数据类别清单"使用，会同时漏掉系统服务、设备端派生索引与元数据三个大块，从而得出"设备数据类别远少于实际"的错误盘点结论；这是本报告坚持四轴框架的直接原因。

---

# 身份、账户与设备标识数据

## 3.1 Apple Account 标识符与账户工件

Apple Account（旧称 Apple ID）是 iOS 数据生态的**归因锚点**：几乎所有 iCloud 服务域、密钥域与备份集都以它为根。清单上必须把"账户标识符"与"账户内的数据"分开登记，因为前者的泄露无法通过更换内容来缓解。

| 资产 | 基质与位置 | 保护级别 | 出设备路径 | 删除与残留 |
|---|---|---|---|---|
| 账户主标识（Apple Account 邮箱/用户名） | 账户体系；设备侧以配置与令牌形式存在 | 服务域标准保护 | 登录、同步、恢复、购买、支持流程 | 需账户删除流程；本次未核对苹果披露的具体清除时限 |
| 账户令牌与会话凭证 | 钥匙串/系统域 | accessibility 属性未逐项披露 | 各 iCloud 客户端 | 退出登录后令牌失效，但服务端会话记录属未披露 |
| 完整购买历史 | 与 Apple Account 关联 | 服务域 | App Store/iTunes 恢复流程 | 《PSG》（26.5 版）明确备份**不含已购内容本体**，只含相关信息，恢复时重新下载 |
| 恢复联系人 / 恢复密钥引用 | 账户恢复体系 | 见 3.6 | 恢复流程 | 移除恢复联系人会影响 ADP 前提条件 |
| 设备与账户的绑定关系 | 设备注册、Find My、受管注册记录 | 服务域 | MDM、Apple 服务 | 解绑不等于服务端历史删除（未披露） |

治理要点有三。**其一**，账户是"高价值单点"：iCloud 备份的密钥保护不使用 iCloud 口令（备份 keybag 由随机密钥保护，随机密钥与备份集同存），这意味着**账户口令泄露并不自动等价于备份内容可读**，但双重认证被绕过仍是端到端加密体系（ADP 需双重认证为前提）的关键风险标签。**其二**，账户标识与设备标识会互相引用：Apple Advertising 与隐私页面所述"查看广告定位信息"面板（iOS/iPadOS/visionOS 路径为 Settings > Privacy & Security > Apple Advertising > View Ad Targeting Information）呈现的正是这两类标识的关联结果，关闭 "Personalized Ads" 开关只改变用途，不撤销已有归因关系。**其三**，购买历史作为账户级资产常在盘点中被忽略：它揭示设备使用序列与应用安装史，属于典型的行为元数据，按 2.2 规则应独立计行。

身份与账户数据的治理判定最终落在"密钥域归属"上，因为同一标识在不同密钥域下的谈判对象完全不同。

| 密钥域 | 谁能解密 | 变更该域的成本 | 适用身份/账户资产 |
|---|---|---|---|
| 用户口令 + 设备 UID | 仅本人（在场解锁） | 修改设备口令会影响由口令派生的类密钥；但 iCloud 口令变更不会使旧 iCloud 备份失效（见 4.1、4.6） | Class A 文件类、解锁相关类密钥 |
| 生物特征子系统 | 本机已注册外观/指征 | 重置生物特征即失效，不改变数据本身 | 被生物特征包裹的类密钥（见 7.5） |
| Secure Enclave UID 根密钥 | 仅同一台设备 | 无法迁移；恢复只能回到同一设备 | 备份中的本地钥匙串 |
| Secure Element | 令牌化设备账号所在硬件 | 卡片删除/重签发 | Device Account Number |
| 受信设备集合（端到端） | 用户与受信任设备 | 增删受信设备与恢复材料直接影响可解密性 | Passwords and Keychain、Health data、Maps、Screen Time 等 15 项 |
| Apple HSM（available-after-authentication） | 苹果在认证后可用 | 开启 ADP 后立即、永久、不可撤销删除并逐服务轮换 | 默认状态下的 iCloud 备份服务密钥 |
| 第三方开发者 | 开发者后端 | 由开发者策略决定，用户侧不可强制 | App 上传数据、CloudKit 未声明 encrypted 的字段 |

这里有一个可执行的优先级结论：**ADP 的前提条件本身就是身份层控制项**——账户需双重认证 + 设备密码、系统需 iOS 16.2 / iPadOS 16.2 / macOS 13.1 / tvOS 16.2 / watchOS 9.2 及以后、且需至少一个恢复联系人或恢复密钥；恢复失败时苹果明确表示无法帮助恢复端到端加密数据。也就是说，端到端覆盖的收益与其不可逆风险来自同一组前提，治理文档必须把"恢复材料就绪"作为开启 ADP 的前置验收项，而不是事后条款。

## 3.2 联系人身份卡与"自我身份"的多副本

用户自身的身份信息在 iOS 上以多副本形式存在：通讯录中的"我的卡片"（Me card）、健康 App 的个人资料、Wallet 的身份与年龄声明、Apple Pay 开通时经发卡方完成的身份验证，以及 Apple Cash 所依赖的 iCloud 家庭内已验证身份（《PSG》在 Apple Pay/Wallet 相关段落述及其需家庭内已验证身份）。这些副本的保护级别差异很大，且大多不被同一套授权模型覆盖。

| 副本 | 位置 | 保护与可见性 | 特殊风险 |
|---|---|---|---|
| 通讯录 Me 卡片 | 通讯录数据库（同步至 iCloud Contacts） | iCloud Contacts **不支持端到端加密**（互通性原因，两处来源一致） | 姓名、电话、邮箱、地址、生日集中一处 |
| 健康 App 个人资料（性别、出生日期、血型等） | 健康库 | 端到端（标准保护下即端到端） | 决定生育周期与用药类数据的推断质量，属派生输入 |
| Apple Pay 身份验证结果 | 发卡方与 Secure Element 配置数据 | Device Account Number 存于 Secure Element，苹果称无法访问 | 卡片配置数据经 Apple 服务器以证书链验证 |
| Apple Cash / Wallet 身份声明 | Wallet 服务域 | 服务域保护；DAN 不入 Apple Pay 服务器也不入 iCloud 备份 | 与家庭共享身份体系耦合 |
| 锁屏可见身份件（Medical ID） | 独立存储 | 处于 `NSFileProtectionNone` 语义一类（无需密码可访问，见 6.5） | 为急救可用性而刻意弱化保护 |

清单结论：iOS 有意在"急救可用性"与"机密性"之间作不对称选择（Medical ID 从锁屏 Emergency 按钮可查看即为典型），治理上应以"允许锁屏可见的字段白名单"作为控制点，而不是假设用户理解其中的取舍。

## 3.3 设备标识符：UDID、IDFV、IDFA 与 App Tracking Transparency

三种标识符作用域不同，混用是隐私评估里最常见的错误。

| 标识符 | 作用域 | 可获得性 | 重置语义 | 置信 |
|---|---|---|---|---|
| UDID（设备唯一标识） | 全局、跨 App | 不向普通 App 提供；出现在设备注册、配置描述文件、支持流程等场景 | 不可重置 | 部分核实（本次未加载逐项披露其接口的原文） |
| IDFV（identifierForVendor） | 同一"供应商"的 App 集合 | App 直接可取 | 该供应商全部 App 卸载后重建 | 部分核实 |
| IDFA（广告标识符） | 跨 App、广告用途 | 需 ATT 授权；文档原文为"Request authorization to access app-related data that your app can use to track the person or the device." | 用户可在设置中重置 | 未核实（本次未加载 AdSupport 文档，故不断言其"已下线"或当前限制细节） |
| 持久化账户标识符 | 开发者 CloudKit 容器侧 | 苹果已登记本地 App 可读取该标识符的缺陷（CVE-2026-86895） | 不适用 | 已核实（缺陷 Impact 原文） |

App Tracking Transparency 的框架级事实已核实：符号清单包含 `ATTrackingManager`、`ATTrackingManager.AuthorizationStatus`、`requestTrackingAuthorization(completionHandler:)`、`trackingAuthorizationStatus`、`NSUserTrackingUsageDescription` 与 `NSUserTrackingMarkdownUsageDescription`，并与 "App Privacy Details" 并列出现于同一 topics。ATT 数据对象的清单登记要点是：它门控的是**跨 App 与跨网站的追踪用途**，不是"数据读取"本身；因此"用户拒绝 ATT"不等于"该 App 无法读取设备上的其他数据"。这是资产视角与权限视角的分界。

风险标签：设备失窃时，标识符本身不构成主要暴露；但在受管注册场景（见第 9 章末尾与后续受管章节）中，设备标识与账户标识的绑定关系会成为合规谈判对象。苹果透明度报告所载 **Device Requests** 一类的原文说明其通常寻求"与客户及客户设备与 Apple 服务的连接信息"相关的信息（例如失窃设备调查），并载明苹果定期收到与欺诈调查相关的**多设备请求（multi-device requests）**——这正是标识符关联关系在执法口径下的体现。

## 3.4 归因数据：AdAttributionKit 与 SKAdNetwork

苹果当前文档化的归因机制框架是 **AdAttributionKit**，框架描述原文为："Present, process, and register postbacks for in-app ads in the App Store and alternative app marketplaces."，其 topics 覆盖 Ad network registration and configuration、Configuring a publisher app、Configuring an advertised app、Configuring attribution rules for your app、Generating JWS impressions、`AppImpression`、Creating postbacks in developer settings、Identifying the parameters in a postback、Identifying conversion values with conversion tags、`CoarseConversionValue`、Ad attribution testing 与 `AdAttributionKitError`。文档中**仍出现 `SKAdNetwork` 字样**，因此准确表述是：AdAttributionKit 为当前框架（覆盖 App Store 与替代应用市场），SKAdNetwork 作为既有/并行机制仍在文档中被引用；两者的替代时间表属未核实。

作为数据类别，归因数据的登记特征值得单独说明。**其一**，它是"事件 + 标识 + 转化值"的组合：应用安装/重安装事件、广告印象（以 JWS 形式生成的凭据）、粗粒度转化值，构成可推断用户行为的最小集。**其二**，它的服务端与设备侧处理链未在本次一手材料中逐项披露，因此清单上"出设备路径"应写"由框架定义的回传机制（具体留存与时限未披露）"。**其三**，它与 IDFA 是两套数据：未获 ATT 授权时归因回传仍可能按框架规则发生，这在合规映射中常被误判为"已获同意"。

| 归因资产 | 类别映射（分析性推断） | 治理落点 |
|---|---|---|
| 广告印象凭据（JWS impression） | 可链接标识/事件记录 | 需在"是否构成追踪"判定中显式评估 |
| 安装/重安装回传（postback） | 行为元数据 | 与 App 隐私标签的"App 活动"用途对齐 |
| 转化值（含粗粒度转化值） | 推断类数据 | 单独看非个人，聚合后可指向购买/使用模式 |
| 广告定向信息（苹果自有平台） | 账户 + 设备标识的关联结果 | Settings > Privacy & Security > Apple Advertising 面板查看与关闭个性化 |

## 3.5 Sign in with Apple 与邮件中继语义

框架级描述已核实：**"Provide users the ability to sign in to your apps and websites using their Apple Account."**，topics 包含 On-device support、Web support、Transfers across teams，另有 "Implementing User Authentication with Sign in with Apple" 与 Sign in with Apple JS。

作为数据类别，Sign in with Apple 产生三类工件：身份断言（用于第三方服务建立账户）、用户可披露的姓名与邮箱字段（用户在首次授权时选择）、以及**中继邮箱地址**（"隐藏我的邮箱"式转发地址）。清单上必须把中继邮箱登记为**别名标识符**而非联系方式：它是稳定、可回指到真实账户的映射键，泄露后果与普通邮箱不同（可被用于跨服务关联），但它不包含真实邮箱。

以下具体语义本次未加载原文，写作与实施时须显式标注为"以 developer.apple.com 的 Sign in with Apple 邮件中继文档与 iCloud+ Hide My Email 支持文档为准，本次调研未核对"：转发地址格式与域名白名单、发信频率与收件人数量上限、App 侧可见字段是否严格限于转发地址、用户修改或停用转发地址的生效时机、转发地址回收策略、"Hide My Email" 与 iCloud+ 的订阅关系。

治理上可确定的两点是：**其一**，中继机制把"标识符暴露"转化为"苹果持有的映射"，因此合规评估时需要把苹果纳入该映射的处理链；**其二**，用户停用转发地址属于删除语义的一部分，但服务端映射表的保留期属公开资料未披露，不应假定即时生效。

## 3.6 通行密钥、凭证库与恢复/验证材料

凭证库是 iOS 上保护语义最复杂的一类，因为它同时存在于文件类之外的钥匙串子系统、iCloud Keychain 同步域与备份集中，而且存在"绝对不流转"的子集。

《PSG》（26.5 版）§Keychain data protection（p.101）给出行为与文件类等同但密钥与函数独立的对照表：

| 可用状态 | 对应文件类 | 钥匙串 accessibility 属性 |
|---|---|---|
| When unlocked | `NSFileProtectionComplete` | `kSecAttrAccessibleWhenUnlocked` |
| While locked | `NSFileProtectionCompleteUnlessOpen` | `kSecAttrAccessibleAfterFirstUnlock`、`kSecAttrAccessibleAlways` |
| After first unlock | `NSFileProtectionCompleteUntilFirstUserAuthentication` | `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly` |
| Always | `NSFileProtectionNone` | （无对应保护档） |

其中 `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly` 有三条决定性的"不"，按原文：**Don't sync to iCloud Keychain**、**Aren't backed up**、**Aren't included in escrow keybags**；它只存在于 system keybag，行为同 `kSecAttrAccessibleWhenUnlocked` 但仅在设备已设密码时可用，且**口令被移除或重置后，项因丢弃密钥而失效**。这条属性是全篇治理价值最高的单点：它是 iOS 上少数"结构性不进入任何密钥在 Apple 手里的域"的类别，因此适合承载高 T 级机密密钥（注意：口令重置即失效，是可用性与机密性的刻意交换）。

`*ThisDeviceOnly` 变体（如 `kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly`）在《PSG》其他章节出现，已核实该字符串存在；六个完整变体的逐一语义本次未加载开发者文档原文，须引用 developer.apple.com 关于 restricting keychain item accessibility 的开发者文档并标注为开发者文档而非《PSG》。后台刷新类 App 被建议用 `kSecAttrAccessibleAfterFirstUnlock`——这一建议本身就是清单判据：凡是"锁屏后仍需工作"的凭证，其保护档必然低于最高档。

| 凭证类资产 | 同步 | 备份 | 端到端 | 备注 |
|---|---|---|---|---|
| iCloud Keychain（密码、通行密钥） | 是（跨设备同步域） | 本地钥匙串入备份但用 Secure Enclave UID 根密钥派生密钥加密，只能还原到同一台设备，苹果无法读取 | 标准保护下即端到端（102651 表格 "Passwords and Keychain"） | iCloud 侧存在 keybag escrow；钥匙串同时是 ADP 服务密钥的存放保护域 |
| `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly` 项 | 否 | 否 | 本机 | 不入 escrow；口令重置即失效 |
| 通行密钥（passkey） | 随 iCloud Keychain 域 | 同上 | 同 iCloud Keychain | 应用侧只获得认证是否成功，无法访问生物特征数据 |
| escrow keybag | 不适用 | 不适用 | 法证/解锁用途 | 《PSG》涉证条款载明密码学 escrow 会暴露"类密钥列表：Key UUID、所属文件或钥匙串 Data Protection class" |
| 恢复联系人 / 恢复密钥 | 账户恢复体系 | 不适用 | 不适用 | ADP 前提：至少一个恢复联系人或恢复密钥 |
| Web 授权时的密钥上传 | 受限 | 不适用 | 不适用 | 开启 ADP 后 iCloud.com 默认关闭；授权后设备"武装"1 小时，仅接受对可见服务 allow-list 的密钥上传，每次上传通知用户；**Health 与 iCloud Keychain 密码永不上传** |

删除与残留语义：关闭 ADP 时设备会把服务密钥上传 HSM（但从不包含标准即端到端的类别，如 iCloud Keychain 与 Health）；开启 ADP 时，HSM 内 available-after-authentication 密钥的删除是"immediate, permanent, and irrevocable"，随后异步逐服务轮换密钥，失败会重试。凭证库的"删除"因此在苹果文档中主要指**密钥销毁**而非记录抹除；恢复失败时苹果明确表示无法帮助恢复端到端加密数据。

**（本小节的延续）电话与邮箱验证工件。**注册、登录与恢复流程会产生一批短生命周期的标识工件：验证码短信、被关联进 IDS 的电话号码、账户受信任设备列表、以及双因素认证下的设备令牌。已核实的相关事实是：Apple Identity Service 目录存放 iMessage 公钥、APNs 地址与**关联电话号码**，接收方会将来话号码/邮箱与通讯录匹配以显示姓名。也就是说，电话号码在苹果体系中同时是路由标识与身份标识。

| 验证工件 | 生命周期 | 可引用披露 | 风险标签 |
|---|---|---|---|
| 关联电话号码（IDS） | 长期，随账户/设备配置 | 《PSG》§Messages 与词汇表 | 号码换主后的目录残留属未披露 |
| iMessage 公钥 | 随密钥轮换 | 同上；开启 ADP 时容器密钥轮换 | 旧容器密钥留存问题见 4.2 |
| APNs 设备令牌 | 随系统更新/重装变化 | 令牌与账号/设备标识的关联及保留策略：公开资料未披露 | 长期稳定的推送令牌可被用作关联标识（第三方 SDK 场景） |
| 短信/邮件验证码 | 短暂 | 苹果未公开逐项披露 | 转发到伴生设备时进入其他类别的保护域（Watch/Continuity 经 APNs 且端到端） |
| 受信任设备列表 | 账户级 | 支持文档口径 | 列表管理是 ADP 端到端密钥的实际信任根 |

这一组资产行的治理结论是：**验证工件的价值在于其短暂性**，任何延长其可关联性的机制（令牌长期稳定、目录残留、备份保留旧密钥）都会把 T1 行升级为 T2/T3。由于苹果对上述项的保留策略多数不披露，清单应以"假定存在残留"为保守默认，并在控制设计中避免把验证工件当作可回收资源。

---

# 通信与消息数据

## 4.1 消息的数据形态、保留设置与备份纳入

通信类是用户感知最强、而实际保护差异最大的一组。本节先确定"形态"，后续各节分别处理内容、传输与元数据。

| 形态 | 位置与基质 | 保护 | 出设备路径 | 备注 |
|---|---|---|---|---|
| 会话与正文（本机） | 设备侧数据库（具体路径与表结构属公开资料未逐项披露） | 由 Data Protection 类决定；被 iCloud 备份明确纳入（原文含 "conversations in the Messages app"） | 备份、Messages in iCloud 同步、iTunes/Finder 备份 | 备份纳入意味着密钥域迁移 |
| 附件 | 本机附件归档 + 上传时的 iCloud 存储 | 上传附件以 **AES-CTR + 随机 256-bit 密钥**加密；密钥与 URI 与密文哈希随消息内容传递 | iCloud、消息内容 | 超长正文或含附件时走"密文上传 + 密钥随消息"模式 |
| 缩略图与预览 | 索引层 | 未逐项披露 | 备份、共享、通知 | 派生行，按 2.2 聚合规则计 |
| 会话元数据 | 传输与目录层 | 时间戳与 APNs 路由信息**不加密** | APNs、IDS | 见 4.6 |
| 短信（SMS/传统蜂窝） | 同一数据库形态 | 无端到端可言（协议本身） | 备份、转发到伴生设备 | 与 iMessage 的隐私等级差异必须分行登记 |

保留设置：按苹果 iPhone 用户指南所述，"信息"设置中提供消息保留时长选项（典型为较短期限与"永久"两类档位的选择），该设置作用于本机会话存储；其在云端与备份集中的对应保留期属公开资料未披露。备份纳入的苹果原文口径需同时引用两处：《PSG》（26.5 版）载明 iCloud 备份包含 Messages App 中的会话；而 *If you're missing information after you restore your iPhone or iPad* 载明**已开启相应 iCloud 功能时**，Messages 与 Voice Memos 的内容"aren't included in your backup"（因已在 iCloud 中）。这两条不矛盾，是同一枚硬币的两面：**内容只被存一次，但其密钥域取决于你用的是同步还是备份**。

由于消息类同时受"iCloud 备份开关""Messages in iCloud 开关""ADP 开关"三个设置影响，清单应以矩阵而非单值登记其归属。下表把前述已核实条款组合成决策矩阵（单元格为条款组合的推定结果，各单项条款本身均已核实）。

| iCloud 备份 | Messages in iCloud | 账户加密档位 | 净效果 |
|---|---|---|---|
| 开 | 开 | 标准保护 | 消息本体已在 iCloud，不入备份；备份中包含 Messages in iCloud 加密密钥的副本 → 该期间内容不属端到端 |
| 关 | 开 | 标准保护 | 端到端；设备生成新密钥保护后续消息，该新密钥只存于 iCloud Keychain、仅受信设备可用；旧容器密钥仍留存 |
| 开 | 关 | 标准保护 | 会话按备份原文（含 Messages App 会话）入 iCloud 备份；锁定态不可访问类的 per-file key 由 iCloud Backup keybag 包裹后按原始密文上传 |
| 任意 | 开 | 开启 ADP | Messages in iCloud 始终端到端；开启时 iCloud Backup service key 与 Messages in iCloud container key 均被轮换 |
| 任意 | 任意 | 关闭 ADP | 服务密钥被上传 HSM；关闭动作会削弱端到端状态，治理上应视为降级事件并留痕 |

配套的两条不可变事实值得单独登记：iMessage 的元数据（时间戳、APNs 路由信息）不加密；离线设备的排队消息在 Apple 服务器上最长保存 30 天。

## 4.2 iMessage 端到端加密：苹果文档所述的机制与边界

《PSG》（26.5 版）§Messages（pp.188–190）给出可引用的机制事实：设备持有公钥/私钥对并使用 ECDSA 签名；自 iOS 13 / iPadOS 13.1 起可选用 **ECIES** 替代 RSA。经 APNs 中继的载荷上限为 **4 KB 或 16 KB**（随 iOS/iPadOS 版本而异）；正文过长或含附件时，附件以 AES-CTR 加随机 256-bit 密钥加密后上传 iCloud，随后把 **AES 密钥 + URI + 密文 SHA-1 哈希**作为 iMessage 内容发送。苹果对端到端加密的书面表述见其新闻室稿：**"iMessage was built with privacy in mind and has always been end-to-end encrypted. It remains the best way to communicate between Apple devices."**

关键边界必须与结论同现。其一，**元数据不加密**：按《PSG》（26.5 版），时间戳与 APNs 路由信息不加密，与 APNs 之间使用前向保密 TLS 信道。其二，**离线队列留存**：消息送达即从 APNs 删除，但 iMessage 会为离线设备排队，按《PSG》（26.5 版）**在 Apple 服务器上最长保存 30 天**。其三，**目录依赖**：IDS 存放 iMessage 公钥、APNs 地址与关联电话号码，群聊场景对每个接收者及其设备重复该流程。

同步与备份层的密钥语义是治理重点，也是最容易误读的部分。按苹果文档口径，Messages in iCloud 保持全部消息历史在各设备可用；**标准保护下仅当 iCloud 备份关闭时才是端到端加密**。开启 iCloud 备份时，备份中包含 Messages in iCloud 加密密钥的副本（其设计目的是在丢失 iCloud Keychain 与受信设备后仍可恢复）；关闭 iCloud 备份时，设备生成新密钥保护后续消息，该新密钥只存于 iCloud Keychain、仅受信设备可用，新数据无法用旧容器密钥解密——但**旧容器密钥仍留存**，属可被 Apple 读取的历史。这一"历史与新数据不同密钥域"的分裂是清单上必须写明的：同一条会话在不同时间段可能落在不同保护档。在 Advanced Data Protection 下，Messages in iCloud 始终端到端，且开启 ADP 时 iCloud Backup service key 与 Messages in iCloud container key **均被轮换**（轮换方向是"由 Apple 可读变为仅受信设备可读"，这是本报告中最强的单点控制收益）。

## 4.3 RCS：beta 阶段的端到端加密与未披露项

已核实的苹果一手来源是新闻室稿 *End-to-end encrypted RCS messaging begins rolling out today in beta*（2026-05-11）：自 **iOS 26.5** 起，iPhone 用户"begin seeing a new lock icon in RCS chats indicating that messages in the chat are end-to-end encrypted"；生效条件为**supported carriers** 加上 Android 端最新版 **Google Messages**；状态为 **beta**。原文关键句为："Apple and Google have led a cross-industry effort to bring end-to-end encryption to Rich Communication Services (RCS)…"以及"**When RCS messages are end-to-end encrypted, they can't be read while they're sent between devices.**"

作为资产行，RCS 与 iMessage 必须**分列登记**，不可共用"端到端"结论。差异如下表。

| 维度 | iMessage（按《PSG》26.5 版） | RCS（本次可引用范围） |
|---|---|---|
| 端到端 | 苹果书面表述见 4.2 | 仅在"消息在设备之间传输时不可被读取"的锁形图标标识场景成立，且为 beta |
| 覆盖条件 | Apple 设备之间 | 受支持的运营商 + 对方为最新版 Google Messages |
| 服务器端排队/留存 | 离线队列最长 30 天（已核实） | 公开资料未逐项披露 |
| 附件加密 | AES-CTR + 随机 256-bit 密钥上传 iCloud（已核实） | 公开资料未逐项披露 |
| 群聊 | 对每个接收者及设备重复流程（已核实） | 公开资料未逐项披露 |
| 备份 | 会话入 iCloud 备份；密钥域见 4.1/4.2 | 苹果未公开逐项披露 |
| 跨平台历史迁移 | 不适用（在 Apple 生态内） | 公开资料未披露；三星一侧表述本次未获取一手文档 |
| 回落（fallback）到 SMS | 不适用 | 未披露回落时的保护级别；治理上应假定回落为非端到端 |

治理结论：由于**同一会话线程内可能存在端到端与非端到端消息交替**（承运商支持状态、对方客户端版本、beta 启用状态均可变），清单不能把"RCS"记为单一保护档。落地的做法是在登记表中把 RCS 拆为"已标识端到端"与"未标识/回落"两行，并将用户指南级别的提示（按苹果 iPhone 用户指南所述，聊天中出现锁形图标表示该聊天端到端加密）作为识别依据。

## 4.4 通话、FaceTime、语音留言与邮件、通讯录、日历

| 类别 | 内容形态 | 同步/备份 | 端到端状态 | 备注 |
|---|---|---|---|---|
| 通话记录（本机与 Continuity 转发） | 号码、时长、方向；可跨设备转发 | 可经 Continuity 经 APNs 中继 | 《PSG》连续性章节述及相关通知"使用相同的端到端加密"（已核实） | 承运商侧记录不属苹果控制域 |
| FaceTime 音视频 | 媒体流 + 联系人关联 | 通话历史类元数据 | 苹果公开描述为端到端（本次未在已加载的《PSG》段落中逐项摘录，故按苹果公开材料所述处理） | Lockdown Mode 会限制 FaceTime（见 9.6 交叉） |
| 语音留言（Visual Voicemail） | 音频 + 转写 | 载体为运营商与设备，具体本次未逐项核对 | 公开资料未逐项披露 | 与语音转写类同属音频派生资产 |
| iCloud Mail | 邮件正文与附件 | 邮件**不进入 iCloud 备份**（"isn't included"），但**账户设置被包含** | **不支持端到端**（互通性；S/MIME 为可选替代） | 第三方邮件账户另计 |
| Contacts | 联系人图谱与关系 | 同步 | **不支持端到端** | 关系图可推断社交结构，实际敏感度高于常规评级 |
| Calendars | 事件、参与者、位置字段 | 同步 | **不支持端到端** | 事件标题与地点可重建轨迹（见 5.6） |
| Reminders | 任务文本与提醒上下文 | 同步 | 经 CalDAV 同步的部分**不支持端到端** | 与位置提醒耦合（见 5.3） |

这三项"互通性例外"（Mail、Contacts、Calendars）是治理谈判的核心：苹果在两处来源中一致说明其原因为需与全球邮件/日历/通讯录系统互通。合规映射上的实际含义是，**通讯录与日历往往是用户最低估、而服务端可见度最高的两类通信资产**：它们完整离开设备、密钥在 Apple（按 102651 表格为 "In transit & on server / Apple"）、且用户无法通过开启 ADP 改变其状态。风险标签应覆盖：账户接管导致的图谱外泄、日历与位置数据交叉导致的轨迹重建。

通信类数据还有一条常被漏登的流出路径：**伴生设备转发**。iPhone 的来电、短信与通知可经 Continuity 转到 Apple Watch 等设备，这类转发同样经 APNs 中继，《PSG》（26.5 版）连续性章节述及相关通知"使用相同的端到端加密"（已核实）。

| 伴生链路 | 已核实的机制事实 | 未披露项 | 登记处理 |
|---|---|---|---|
| Apple Watch 通知/通话/短信 | 经 APNs 中继且使用端到端加密；可用 iPhone 解锁 Apple Watch；iCloud 备份还原到同一 iPhone 可保留既有 Watch 配对 | Watch 本机历史消息的保留与删除 | 与 iPhone 同档登记，但注明"配对关系可跨还原存活" |
| AirPods 与近场配件 | 经 BLE 4.2 配对并用 APNs 建立初始连接，部分场景以 APNs 替代 BLE 传输 activity payload；W1/H1 蓝牙密钥端到端 | 音频、降噪与听力相关数据类别 → 公开资料未披露 | 存在性登记、保护档留空 |
| CarPlay | 有线 CarPlay 连接后设备可在一定时间内免密码解锁（原文表述）；CarPlay 视频流与 AirPlay 音频使用 MFi-secure association protocol | 传输/缓存的数据类别（联系人、消息、音乐、导航）无逐项披露 | 标记为"解锁策略例外 + 数据面未知"双行 |
| Apple TV / 家居中枢 | Siri-enabled 配件音频帧经专用本地连接送往 HomePod/Apple TV，Apple TV 不访问除音频流以外的 Siri 请求数据、不在其上存储用户数据 | 遥控使用记录与事件时间线 | 按家居事件类登记（见 9.6） |
| 空间计算设备 | 参与 Apple Pay、Lost Mode/Find My、设备注册与解锁；全部数据加密（Class D 段落点名 Apple Vision Pro） | Persona、空间捕捉、眼动与手部追踪数据 | 存在性明确、保护语义未披露 |

其中 CarPlay 一条在治理上最容易被忽略：苹果原文描述"有线连接到 CarPlay 的设备不必每次插入都输入口令"，这构成**解锁策略的场景性例外**，其后果是车机上可达的数据面（消息、联系人、导航）在本机保护档上等同于"已解锁状态"。清单必须把这类"场景性免解锁"单独记行，否则高 T 级行会被误评为强保护。

## 4.5 APNs、已读回执与输入指示：传输与状态层数据

APNs 在 iOS 数据流中的位置常被误认为"仅用于推送通知"。已核实的文档事实包括：设备管理中 APNs 用于唤醒设备并触发其与设备管理服务直连，且原文声明 **"No confidential or proprietary information is transmitted over APNs."**；iMessage 场景中 APNs 是端到端加密的中继通道（4 KB/16 KB 上限、送达即删、离线队列最长 30 天、路由与时间戳元数据不加密、前向保密 TLS）；连续性（Continuity / Watch / 通话与短信转发）也使用 APNs，且相关通知"使用相同的端到端加密"。

| 状态类数据 | 是否离开设备 | 可引用披露 | 未披露项 |
|---|---|---|---|
| 推送载荷（第三方 App） | 是 | 《PSG》未逐项披露一般推送的保留策略 | **公开资料未披露**留存时长与 Apple 侧可见字段 |
| 设备令牌 | 注册于 APNs | 词汇表定义 APNs 为全球性服务 | 令牌与账号/设备标识的关联与隐私政策：**未核实** |
| 送达状态 | 是 | 送达即从 APNs 删除（iMessage 场景） | 第三方送达回执的保留未披露 |
| 已读回执/输入指示 | 是 | 本次未在已加载来源中逐项摘录其协议字段；作为 iMessage 内容的一部分可推定受端到端保护 | 服务端对状态事件的时间序列保留：**未披露** |
| MDM 唤醒 | 是 | "No confidential or proprietary information is transmitted over APNs." | 唤醒时机与命令时间线在服务端的记录：未披露 |

清单结论：状态层数据的价值在于**时间序列**。单条"输入中"事件无信息量，但持续的时间序列可以还原作息、关系强度与响应模式。按 2.2 的聚合跃升规则，状态层应至少登记为 T1，并在有第三方推送服务持有令牌的场景下上调。苹果已把本地分析数据的访问列为缺陷（watchOS 7.6 安全内容文档中 Analytics — CVE-2021-30871，Impact 为 "A local attacker may be able to access analytics data"；该文档为 2021 年历史版本，仅作"分析数据本身即受保护对象"的例证，不可用于当前版本线）。

## 4.6 元数据与内容：必须分开治理的两个风险对象

本节是全篇的结构性论题：**内容与元数据是两个独立资产，其保护等级、流转路径与法律后果都不同，把它们合并登记会导致系统性低估。**

| 对象 | 内容侧 | 元数据侧 |
|---|---|---|
| iMessage | 正文端到端（按《PSG》26.5 版机制与苹果新闻室表述） | 时间戳与 APNs 路由信息不加密；IDS 存放公钥、APNs 地址与关联电话号码；离线队列最长 30 天 |
| RCS | 传输期"不可被读取"（beta，受条件限制） | 留存与路由：公开资料未逐项披露 |
| 邮件 | 内容随互通性不端到端 | 头信息、路由与账户设置（后者入备份） |
| 通讯录/日历 | 内容随互通性不端到端 | 参与者、事件时间、事件地点（可交叉轨迹） |
| 照片分享 | ADP 下照片端到端 | Shared Albums 不支持 ADP；分享操作的标题与代表缩略图以标准数据保护存储 |
| 云盘/文档 | ADP 下 iCloud Drive、Notes 端到端 | CloudKit 需开发者显式声明为 encrypted 才受保护；修改日期/时间与文件、照片校验和不端到端（即收敛加密，密钥由 Apple 以标准保护保管） |
| 活动邀请 | Apple Invites 在特定条件下端到端 | 时间/日期、城市级位置、海报图发布后**始终**标准保护 |
| HomeKit 事件 | 剪辑经 home hub 本地解密后以 AES-256-GCM 加密上传 | 元数据（含加密密钥）经 CloudKit 以 iCloud 端到端加密上传（这是少数元数据也端到端的正例） |
| 备份集 | 备份内容按原始密文上传 | 备份 keybag 与随机密钥与备份集同存；不使用 iCloud 口令；改口令不使旧备份失效 |

苹果自己也承认这是一个改进方向：按 *iCloud data security overview* 的表述，上述 CloudKit 元数据虽加密但密钥由 Apple 以标准保护保管，苹果称"承诺让更多此类元数据端到端加密"。治理上应把这句话读成"当前尚未"，并据此设定预期。

三条可执行结论。**第一**，风险评估表至少要为通信类维护两个字段组：content-protection 与 metadata-protection，两者不得互相覆盖。**第二**，任何"端到端"声明的适用范围要检查三项：是否含附件（iMessage 附件走独立的 AES-CTR + 随机密钥 + iCloud 上传路径）、是否含群聊（IDS 需为多接收者维护目录）、是否含历史（关闭 iCloud 备份后旧容器密钥仍留存）。**第三**，在法律口径上，通信元数据常对应"流量数据/可链接标识"这一独立类别（此处为分析性推断，本次未核验法律原文），其调取门槛与告知义务在多数法域与内容数据不同；把两者混为一谈会让治理文档失去可用性。

---

# 位置、轨迹与环境感知数据

## 5.1 定位信号源与精度层

位置数据的敏感度不由"是否知道你在哪"决定，而由**精度 × 时长 × 连续性**三个因子决定。技术上 iOS 设备可用的定位源可归纳为下表；需要说明的是，《PSG》（26.5 版）**未以"数据类别清单"形式**逐项披露这些信号源及其留存方式，因此本表为机制归纳与可引用披露的混合，置信级逐行标注。

| 信号源 | 典型精度层 | 数据形态 | 是否离开设备 | 可引用披露 |
|---|---|---|---|---|
| GNSS（GPS/GNSS 及辅助信息） | 数米级，户外 | 经纬度 + 时间戳 + 精度半径 | 视 App/服务而定：可能仅本机融合，也可能上传辅助请求 | 《PSG》未逐项披露 |
| Wi-Fi 接入点/网络数据 | 数十米级 | BSSID/SSID 与位置映射、所在网络记录 | 可能存在众包式的热点定位数据贡献（公开材料长期描述，本次未逐条核对） | 部分核实 |
| 蜂窝基站 | 百米至公里级 | 基站标识与时间序列 | 承运商侧必然可见（不属苹果控制域） | 公开资料未披露苹果侧处理 |
| 蓝牙（含 BLE 靠近检测） | 房间/近距离级 | 配件接近事件、信标识别 | 经配件链路 | 《PSG》载有 W1/H1 蓝牙密钥端到端；配件接近数据类别未逐项披露 |
| 超宽带级精度定位（UWB-class precision finding） | 厘米至分米级的方向/距离指引 | 设备间测距与方向 | 近距离设备之间，不经云端 | 本次未在已加载来源中定位到逐项披露 → 公开资料未披露 |
| IP 与城市级推断 | 城市级 | 粗粒度位置 | 服务端天然可见 | Apple Invites 的城市级位置按 *iCloud data security overview* 发布后**始终**标准保护 |

治理要点是**把"精度层"作为登记字段**，而不是把"位置"当作单一类别。同样一条"位置"记录，城市级与厘米级在法定分类（如中国法域下的"行踪轨迹"与欧盟/美国州法下的"精确地理位置"表述——分析性推断，本次未核验原文）中的后果差别极大，而在 iOS 上二者可能存在于不同存储基质、受不同保护。

## 5.2 历史轨迹类存储与个性化索引

设备上的"位置历史"与"当前位置"是两类资产：前者是可持续重建生活规律的派生库，后者是一次性上下文输入。清单必须区分。

| 轨迹类资产 | 形态 | 保护与流转 | 置信与措辞 |
|---|---|---|---|
| 常去地点式历史存储（significant-locations 类） | 系统级时间-地点序列，用于预测与建议 | 是否端到端、保留时长与清除时机均须以苹果专项文档为准 | 本次未核实 → 按苹果 iPhone 用户指南所述处理，其余写"公开资料未披露" |
| 常去地点的推断结果 | 由历史存储派生的统计对象 | 进入建议、地图与提醒的决策链 | 其独立留存未核实 |
| 记录/日记类应用内位置（Journal 数据） | 用户内容 + 内嵌位置字段 | *iCloud data security overview* 表格中 **Journal data** 在标准保护下即端到端、密钥在受信设备 | 已核实（该行存在） |
| Maps 相关请求与收藏/历史 | 查询、路线、收藏 | **Maps** 在标准保护下即端到端、密钥在受信设备（102651 表格） | 已核实（该行存在） |
| 照片与视频内嵌地理标记 | 随媒体文件存放 | 随照片库；iCloud Photos 在 ADP 下端到端，Shared Albums 不支持 ADP | 已核实（保护域差异） |
| 通知历史中的位置上下文 | 短期 | 保留期未披露 | 未核实 |

关键结构性事实：位置类里"最强的一行"与"最弱的一行"并存于同一功能域。Maps 与 Journal 落在"标准保护即端到端"集内，而照片的地理标记会随共享相册、外链分享与 "anyone with a link" 路径离开保护域，且分享操作的标题与代表缩略图另以标准数据保护存储。因此，**位置治理不能只设一个开关**，必须按基质分别设置。

## 5.3 地理围栏、位置提醒与基于位置的服务请求

基于位置的功能可归为四类，其数据后果依次递增。

| 功能类 | 输入 | 产生的资产 | 保护与流转 |
|---|---|---|---|
| 地理围栏通知（"到达/离开时提醒我"） | 地点 + 事件文本 | 提醒项 + 位置条件绑定 | Reminders 经 CalDAV 同步的部分不支持端到端；提醒文本本身即内容（可泄露习惯与关系） |
| 基于位置的建议 | 当前与历史位置 | 建议候选集（应用、路线、联系人） | 派生索引层，多数留存未披露 |
| 精确查找/设备定位服务 | 附近设备的无线信号 | 距离与方向判断 | 本次未在已加载来源逐项核对，公开资料未披露 |
| 车载与配件位置上下文 | CarPlay/配件链路 | 导航目的地、行程上下文 | 《PSG》对 CarPlay 传输/缓存的数据类别（联系人、消息、音乐、导航）**无逐项披露** → 公开资料未逐项披露 |

治理上最容易被忽略的是**位置提醒的文本内容**：一条"到某地时提醒我"的提醒本身即携带地点语义，且它经由互通性例外（CalDAV）以非端到端方式同步。这类"位置被写在内容字段里"的情形说明：按字段而不是按 App 登记资产是必要的（见 2.1 轴三）。

位置授权本身的粒度也必须作为登记字段，否则"某 App 有位置权限"这类记录无法用于风险评估。按苹果 iPhone 用户指南所述，位置授权提供分档选择（含仅在使用期间、本次使用会话、以及始终允许等档位）与"精确位置"开关；指南级表述之外的实现细节本次未逐条加载正文，须以该用户指南为准。

| 授权粒度 | 产生的资产 | 持续时长 | 治理判定 |
|---|---|---|---|
| 单次会话允许 | 短期轨迹段 | 会话结束即停止采集 | 最低暴露；仍需登记"会话内精度" |
| 使用期间允许 | 前台活动期轨迹 | 前后台切换决定实际窗口 | 需检查后台定位 entitlement（属开发者声明范围） |
| 始终允许 | 连续轨迹序列 | 长期 | 应按 T4 处理，并要求定期复核必要性 |
| 精确位置关闭 | 粗粒度位置 | 随授权 | 把 T4 降为 T2 的有效手段，应作为默认策略评估项 |
| 系统级历史存储（常去地点类） | 时间-地点序列 | 未披露保留期 | 保护档本次未核实，按"假定存在且服务端不可见"保守登记 |
| 服务请求侧的位置上下文 | 查询词 + 粗位置 | 未披露 | 与 9.3 的服务端聚合交叉 |

需要明确的一条负面事实：**用户侧的授权粒度只约束 App，不约束系统服务与派生索引**。系统级历史存储、Maps 与 Siri 建议所消费的位置数据走的是苹果自身的服务域（其中 Maps 属标准即端到端），这与"我把某 App 的位置权限改成'使用期间'"不是同一控制面。清单应把这两类控制面分别记行，避免用 App 权限表推断系统数据流。

## 5.4 共享位置、Find My 与家人共享

共享位置是唯一一类"由用户主动、持续地把高敏数据交给特定他人"的资产，其治理难点在于授权是**关系型**而非**对象型**。

| 共享形态 | 可见方 | 苹果可引用披露 | 未披露项 |
|---|---|---|---|
| 与家人共享的位置 | 指定家庭成员 | Find My / Lost Mode 覆盖 Apple Watch 等设备的交叉条目（已核实） | 家庭圈内位置历史的可查看范围 |
| Find My 网络（设备定位与遗失模式） | 用户与被共享者 | Lost Mode/Find My 覆盖 iPhone、iPad、Apple Watch、Apple Wallet 与 Apple Vision Pro 等条目（已核实） | 网络中继设备侧的元数据形态与保留：公开资料未披露 |
| 通过链接的位置共享 | 链接持有者 | 不适用 | 苹果未公开逐项披露 |
| 配件与耳机类定位 | 用户 | 《PSG》无 AirPods 专章；可引用的仅有 W1/H1 蓝牙密钥端到端与 BLE 4.2 配对 + APNs 建立初始连接 | AirPods 音频/降噪/听力相关数据类别：公开资料未披露 |
| 车内位置上下文 | 车机与用户 | 无逐项披露 | 公开资料未逐项披露 |

关于 Find My 网络的加密性质，社区与厂商材料存在多种表述，本次核验的已加载来源未提供可逐条引用的原文，因此本报告不作机制断言，只登记两项可确认事实：**其一**，该功能使设备位置可被特定他人持续查看；**其二**，它跨越 iPhone、iPad、Apple Watch 与空间计算设备，属典型的"多设备共享同一敏感对象"类别。落地控制应以"共享关系盘点"为对象：谁在什么期限内能看到哪一层精度。

## 5.5 媒体内嵌位置、隐私请求导出与伴生设备派生位置

媒体文件的位置字段是"最容易被无意识外泄"的一类：它随文件本体流转，脱离任何系统开关的控制。EXIF/GPS 与 XMP 层面的地理标记在 AirDrop、邮件附件、第三方上传时都会被带走；即使照片本体在 iCloud 中端到端，一旦用户通过可公开访问的路径分享，其内嵌位置就进入接收方可控范围。治理手段只有两条：在分享前剥离（按苹果 iPhone 用户指南所述，分享时可查看并关闭位置信息），或以"不导出原片"为默认策略。

隐私请求导出渠道在治理上具有重要意义：它是把"设备外服务端留存"变为**用户可见清单**的常规途径。本次核验未加载该渠道的数据项说明，因此其覆盖范围应写作"按苹果隐私页面所述可提交数据下载请求，具体包含的数据项本次未逐项核对"。实践建议是把导出结果当作审计输入：导出项集合与本报告清单的差集，即是需要向苹果或供应商补充问询的空格。

伴生与车机派生位置有三条来源链：**其一**，Apple Watch 与 iPhone 的位置上下文随配对关系传递，Watch 的健身记录（见第 6 章）内含时间-活动-距离序列，实质上是轨迹的间接表达；**其二**，CarPlay 场景下导航目的地与行程上下文进入车机，而《PSG》未逐项披露 CarPlay 传输/缓存的数据类别；**其三**，HomeKit 与家居配件的"到家/离家"类自动化会生成事件（见 9.6），这类事件是"是否在家"的粗粒度位置替代指标。清单必须为这三条链分别建行，因为它们的控制点位于不同界面，部分甚至完全不由用户设置项控制。

位置类的删除语义需要单独说明，因为它是全篇"用户动作"与"实际后果"落差最大的一类。可执行的区分如下（除 Maps/Journal 的端到端状态与 Apple Invites 的城市级位置条款为已核实外，其余时机项多为未披露）。

| 删除/收回动作 | 预期效果 | 一手可引用性 |
|---|---|---|
| 清除系统级历史存储（常去地点类） | 本机历史序列被清空 | 按苹果 iPhone 用户指南所述存在清除入口；对已派生建议的影响与残留未披露 |
| 关闭"精确位置" | 后续采集降为粗粒度 | 按用户指南所述为开关；对历史数据无效 |
| 关闭基于位置的建议 | 停止新增派生建议 | 入口按用户指南所述；索引重建与保留未披露 |
| 撤销他人共享位置 | 接收方失去可见性 | 生效时区与服务端副本处理未披露 |
| 删除媒体内嵌位置 | 该文件不再携带地理标记 | 分享前剥离可控；已导出副本不可控（见 8.1） |
| 提交隐私数据下载请求 | 获得服务端留存的用户数据副本 | 按苹果隐私页面所述可提交请求；包含项本次未逐项核对 |
| 删除账户 | 终止服务域访问 | 服务端清除时限未披露，不得写成即时删除 |

由此得出一条治理规则：**位置类的"删除"应理解为"停止新增 + 收回访问"，而不是"抹除历史"**。任何依赖"我已清除"的对外陈述都需要配套两项证据：清除动作的时间记录，以及无法验证残留时的保守假设声明。这与第 4 章的备份语义、第 6 章的 journal 语义共同构成本报告关于"删除不等于不可恢复"的一致表述。

## 5.6 端侧与云端处理边界，以及"重构个人轨迹"的暴露评估（案例）

苹果在多篇材料中把"尽量在设备端处理"作为隐私设计原则，但可引用的逐项边界很窄：本次能确认的仅有 HomeKit Secure Video 的**事件分析在 home hub 本地完成**（home hub 解密音视频并在本地做事件分析，仅将加密剪辑与端到端加密的元数据上传），以及 HealthKit 的记录存储与推断在设备端主库完成。其余涉及人脸/场景识别、建议生成、位置聚合的处理是否严格端侧，属公开资料未披露或以苹果专项页面为准。

以下给出一个**派生敏感度跃升**的完整评估样例，展示清单如何用于结论而非罗列。评估对象：由多类资产重建的"个人轨迹"（reconstructed personal trajectory）。

| 输入资产 | 单项层级 | 单项可控性 |
|---|---|---|
| 常去地点式历史存储 | T2（保护档本次未核实） | 用户可在设置中清除/关闭（按 iPhone 用户指南所述） |
| 照片与视频内嵌地理标记 | T2 | 随文件外流即失控 |
| 日历事件时间与地点字段 | T2（同步不端到端） | 服务端可见 |
| HomeKit 到家/离家事件 | T1 | 端到端（Home data 标准即端到端） |
| 屏幕使用时间的时段分布 | T1（标准即端到端） | 端到端 |
| 提醒的位置条件文本 | T2（CalDAV 部分不端到端） | 服务端可见 |
| 导航/路线历史（Maps） | T2 | 端到端 |

聚合分析：上表 7 行中有 5 行落在端到端保护域内，但**只要有一个主体能同时读到其中三行（例如设备处于解锁状态时的一次本地读取，或一个持有照片原件的接收方），时间-地点-活动即可互相校准**。校准后的轨迹对象可在数周内确定居住地、工作地、作息、社交节点与特定场所到访规律，属典型 T4。

结论三条。**第一，聚合层级独立评级**：重构轨迹应按 T4 登记，而不是取各输入的最低层级——这是 2.2 聚合跃升规则的直接应用。**第二，端侧/云侧混合本身不构成保护**：因为跃升发生在读取侧而非存储侧；有效控制是"限制同一主体可同时读取的类别集合"（典型受影响面为受管注册带来的查询面，以及备份把多条路径归一到同一密钥域）。**第三，备份是风险归一化的关键节点**：iCloud 备份按原文包含设备设置、App 数据、相机卷照片视频与消息会话；一旦开启，照片地理标记等原本分散在多密钥域的类别被归入同一备份集，其可关联性由备份密钥域单一决定——这是把"备份开关"列为高优先级控制项的原因。

风险标签（不作为操作指引）：设备失窃且处于可读取状态；账户接管导致备份可读；受管注册（Device Enrollment/ADE）带来更大的配置与查询面；本地未加密备份使多数类别以可还原形式落在校主机（对照 6.6 中"健康数据仅在加密备份中被纳入"的例外）。

---

# 健康、健身与医疗数据

## 6.1 类别组与数据形态

健康数据在 iOS 上不是一个库的别名，而是**多来源汇聚的中心库 + 一组按类型细分的记录对象**。记录形态至少包括数值型测量、事件型记录、分类样本（症状、睡眠阶段等）、序列型（心电/心律类样本）与文档型（临床记录、免疫记录）。

HealthKit 的"类别组"（category groups）与全部 `HK*Type` 名称本次未加载开发者文档逐项核对，因此本节的组名列表须写作"以 developer.apple.com 的 HealthKit 数据类型参考页为准，本次调研未逐项核对"。可作为结构性参考的组名（常见枚举，未逐项核实）包括 Activity、Body、Mind-Body、Nutrition、Symptoms、Vital Signs、Hearing 等；具体类型在开发者文档中以 `HKCategoryTypeIdentifier…`、`HKQuantityTypeIdentifier…` 形式命名。

| 数据组（示例） | 代表内容 | 直觉层级 | 实际治理注意 |
|---|---|---|---|
| 活动与步数 | 步数、距离、能量消耗、锻炼时长 | T2 | 高频序列可推断作息与停留（与 5.6 交叉） |
| 心率与心脏功能 | 静息心率、心率区间、ECG 类样本 | T4 | 机型与地区可用性有门槛，见 6.6 |
| 睡眠 | 睡眠阶段、入睡/醒来时间 | T3 | 长期序列揭示作息与用药效果 |
| 听觉健康 | 环境声音暴露、耳机音量；AirPods 侧本次未披露 | T3 | 与设备链路耦合，控制点分散 |
| 行动能力 | 步态、稳定性、跌倒类事件 | T4 | 与照护/共享场景耦合 |
| 生育与激素 | 周期天数、观察症状、相关标记 | T4 | 最高敏感度组，法律口径下多为特殊类别 |
| 营养与症状 | 饮食、水合、疼痛与情绪标记 | T3 | 主观输入，用户常不理解其可导出性 |
| 临床与免疫 | 化验、报告摘要、免疫记录 | T4 | 来自外部卫生系统摄取（见 6.5） |

## 6.2 存储位置、保护类与残留

《PSG》（26.5 版）§HealthKit（pp.108–110）给出本类最完整的保护描述，也是全篇少见的"存储 + 时机 + 残留"三项齐备的类别。

| 对象 | Data Protection 类 | 可用时机 | 关键机制 |
|---|---|---|---|
| HealthKit 中心库 | **Protected Unless Open**（`NSFileProtectionCompleteUnlessOpen`） | 设备上锁 **10 分钟后放弃访问权**，需再次输入密码或用 Face/Touch ID 解锁恢复 | 允许锁定状态下写入；per-file 密钥在文件关闭时即刻从内存清除 |
| 运动会话期间 | 同上，但期间锁屏下可持续访问 | 使用 `HKWorkoutSession` API 期间 | 用于锁屏运动数据展示；可在 Settings > Face ID/Touch ID & Passcode > Workout Health Data 管理 |
| 管理数据 | **Protected Until First User Authentication** | 首次解锁后长期可用 | 含 App 访问权限、连接的设备名、App 唤起调度信息 |
| 临时 journal 文件 | **Protected Unless Open** | 锁定时产生的健康记录（如运动中）暂存 | 解锁时并入主数据库，**在合并完成时删除** |
| Medical ID | **No Protection** 类语义 | 锁屏 Emergency 按钮即可查看 | 手动录入，**不与健康数据库同步** |

管理数据落在最弱档是值得写进治理文档的细节：**"哪些 App 有健康授权、连接了哪些设备、什么时机被唤起"这三类元数据，其本机保护强度低于健康记录本身**。按 2.3 的元数据独立登记原则，应把它们单列为高敏行，而不是随健康记录一并估算。

删除与残留方面，苹果明确支持的表述只有两条：临时 journal 在合并完成时删除；Protected Unless Open 的 per-file 密钥在关闭时从内存清除。主库记录被用户删除后的闪存级残留、云端与共享副本的删除传播时限，属公开资料未披露；不得写"已安全擦除"。

## 6.3 HealthKit 授权模型：按类别、按读写

HealthKit 的授权是 iOS 中最细粒度的隐私授权模型，其设计意图（防推断）值得完整记录。以下为已核实口径。

| 规则 | 内容 | 治理含义 |
|---|---|---|
| Entitlement 前置 | 访问由 entitlement 控制，用户须逐次授权 | 无 entitlement 的 App 即便有授权 UI 也不可用 |
| 读写分别授权 | 读取与写入各自独立授权 | "能写"不等于"能读全库" |
| 每种类型分别授权 | 每种健康数据类型分别授权 | 授权清单必须按类型落档，不能记为"已授权健康" |
| 写者自读 | 有写权限即可读自己写的数据 | 支持回写校验，无额外暴露 |
| 读者全读 | 有读权限可读**所有来源**数据（含其他 App 写入的） | 一个心率读取授权即覆盖手表、第三方设备与手动录入 |
| 授权状态不可互见 | App 无法得知其他 App 的授权状态 | 削弱侧信道，但不消除类型组合推断 |
| 空结果不可区分 | 无读权限时**所有查询返回空数据**，与空库不可区分 | 明确为防止推断用户健康状况的设计 |
| 用途限制 | **"apps aren't allowed to use health data for advertising"**，且须提供说明健康数据用途的隐私政策 | 可作为供应商合同与审核锚点 |
| 用户查看/撤销 | Settings > Health > Data Access & Devices | 唯一常规盘点入口 |

清单结论：应把"授权矩阵"本身作为资产登记（哪些 App、哪些类型、读还是写），因为它既是派生数据的来源记录，又是弱保护档上的管理数据。审计动作是把该矩阵与相应 App 的营养标签中健康类声明做交叉，差值即为需复核项（映射本身为分析性推断）。

按授权组合而不是按 App 名单来评估健康暴露面，能更准确定位风险。以下矩阵为本报告的分析性归纳。

| 授权组合 | 可得数据范围 | 主要风险 | 建议控制 |
|---|---|---|---|
| 仅写入（无读取） | 只能读回自己写入的数据 | 低；但仍构成"该 App 已安装 + 记录存在"的推断 | 允许写入的 App 名单需定期复核 |
| 单类型读取（如步数） | 该类型的**所有来源**数据 | 中：跨设备来源合并（手表 + 手机 + 手动）后时间序列完整度上升 | 优先只授予写入，避免默认勾选读取 |
| 多类型读取 | 跨组关联（活动 + 心率 + 睡眠） | 高：可推断用药、疾病管理与作息 | 视为 T3 及以上，须业务必要性说明 |
| 生育/激素类读取 | 周期与观察记录 | 最高：法律口径下的特殊类别（分析性推断） | 独立审批、禁止共享与导出 |
| 临床记录来源数据 | 经 OAuth 2 + TLS 1.3 摄取的机构数据 | 高：机构侧真实性与撤回权不在苹果控制内 | 争议时向机构主张，同时校验 CMS（RFC 5652）签名（若存在） |
| Health Sharing / Share with Provider | 端到端，苹果无法访问 | 中：暴露面转移到接收者设备 | 定期复核共享名单与有效期 |

撤销授权的传播语义须谨慎表述：苹果文档明确用户可在 Settings > Health > Data Access & Devices 查看与撤销授权，这约束的是**后续访问**；已导出至 App 自有后端或第三方云的数据，其删除依赖开发者策略与营养标签声明，属苹果不披露的范围。治理文档应把"撤销授权"与"要求删除已收集数据"写成两条独立动作，并对后者保留向开发者主张的条款入口。

## 6.4 运动、睡眠、听觉与行动能力，以及生育周期

这几组的共同特征是**高频、长期、被设计为持续采集**，因而聚合价值高于任何单点。运动数据由 iPhone 与 Apple Watch 协作产生，经 HealthKit 进入中心库；睡眠与行动能力（步态、稳定性、跌倒类事件）同样是长期序列；听觉类依赖耳机侧采集，而《PSG》无 AirPods 专章，其数据类别属公开资料未披露。

生育周期追踪（cycle tracking）类记录必须**单列成行**：其数据组合（周期天数、观察症状、可推断的亲密关系时间）在多数法域下属最严格保护的一类（分析性推断，本次未核验法律原文），而在技术上它与其余健康数据同处一个中心库、同一保护类、同一授权模型。这构成一个典型治理矛盾：**同一保护机制承载了后果差别极大的两类记录**。落地做法是把生育周期相关类型设为"独立审批 + 不共享 + 不导出"的强控子集，通过 6.3 的按类型授权与 6.6 的共享/备份控制实现，而非依赖库级加密。

采集源决定授权模型能否覆盖，这一点在健康生态里特别明显：并非所有进入健康库的数据都经过 HealthKit 的逐类型授权。

| 采集源 | 进入健康库的路径 | 授权模型是否覆盖 | 备注 |
|---|---|---|---|
| Apple Watch 运动与健身记录 | 经 HealthKit 写入 | 写入侧由系统授权；读取侧需逐类型授权 | 与 4.4 的 Watch 配对存活事实交叉：还原后仍可继续写入 |
| iPhone 传感器（计步、运动类型） | 系统写入 | 同上 | 高频序列，聚合价值见 5.6 |
| 第三方设备与配件 | 经厂商 App 写入 | 该 App 的读/写授权决定后续可见性 | "连接的设备名"属管理数据，落在 Class C（见 6.2） |
| 卫生系统临床记录 | OAuth 2 client credentials + TLS 1.3 直接下载 | 不经第三方 App 授权模型 | 撤回与完整性向机构主张（见 6.5） |
| 用户手动录入（症状、用药、周期观察） | Health App 直接写入 | 有读权限的 App 均可读取 | 主观输入，法律口径下后果最重（分析性推断） |
| Medical ID | 独立存储，不与健康库同步 | 不适用 | 处于 No Protection 类，见 6.2 |

这张表的治理用途是识别"授权模型盲区"：手动录入与临床摄取两类都不受"某 App 是否有写权限"的约束，却对有读权限的 App 完全可见。因此健康暴露面的正确问法不是"哪些 App 写入了健康数据"，而是"**哪些 App 拥有哪些类型的读权限**"。

## 6.5 临床摄取、Medical ID 与研究类数据

临床记录路径（已核实）：在 Health App 内登录支持的卫生系统，使用 **OAuth 2 client credentials**，经 **TLS 1.3** 从机构直接下载，随后与其他健康数据同处安全存储。治理含义是双重的：数据源是**机构**而非苹果，因此数据完整性与撤回权需向机构主张；进入 Health 后即与其余记录同库同保护，用户容易误以为"来自医院的数据有额外一层保护"。

数据溯源与真实性（已核实）：每条记录含元数据，标注记录来源 **App identifier**；可选元数据可携带 **CMS（RFC 5652）** 数字签名。这是 iOS 健康数据中少见的"真实性可控"设计，适合用于对关键测量（化验、免疫、临床摘要）做来源核验与争议追溯。

Medical ID（已核实且重要）：手动录入、**不与健康数据库同步**、从锁屏 Emergency 按钮可查看、存于 **No Protection** 类；**iOS 13 及更早会进入 iCloud Backup，iOS 14 及以后改用 CloudKit 同步，加密特性与其他健康数据一致**。这一版本分界说明：同一类别在不同机制下可跨越"零保护本机 + 入备份"与"随健康域端到端"两种状态，因此清单必须记录机制而不只是类别名。

Health Sharing 与 Share with Provider（已核实）：iOS 15+ 且双方均需 2FA，两人之间为端到端 iCloud 加密、苹果无法访问；Share with Provider 仅对用户所选医疗机构可用，端到端加密且苹果不保管也无法访问密钥。研究类数据（经研究类框架采集的知情同意记录）本次未在已加载来源中逐项核对，应写"公开资料未逐项披露其留存与导出边界"。

## 6.6 同步、备份、机型可用性与法定特殊类别

| 路径 | 状态（可引用口径） |
|---|---|
| iCloud 同步 | 健康数据可存于 iCloud；端到端需 **iOS 12 或更高 + 双重认证**，否则仅传输与静态存储加密、非端到端；满足条件后数据被迁移至端到端加密。*iCloud data security overview* 表格把 **Health data** 列为标准保护下即端到端、密钥在受信设备 |
| 本地（Finder/iTunes）备份 | **仅当备份被加密时才包含健康数据**（已核实） |
| iCloud 备份 | 与同步互为替代路径；对照 102325 的"已在 iCloud 中则不入备份"逻辑处理 |
| 网页授权 | **Health 与 iCloud Keychain 密码永不因网页授权上传服务密钥**（allow-list 排除，已核实） |
| 关闭 ADP 时的 offload | 上传的服务密钥**从不包含**标准即端到端的类别，如 Health 与 iCloud Keychain |
| 共享 | Health Sharing / Share with Provider 均为端到端 |

机型与功能可用性须带限定：ECG、心率区间提醒、跌倒检测、行动能力与睡眠呼吸暂停类功能并非在所有 iPhone/iPad/Apple Watch 机型与所有地区可用；Apple Intelligence 支持机型按苹果 iPhone 用户指南所述为 iPhone 15 Pro、iPhone 15 Pro Max 或 iPhone 16 机型及以后（第三方汇总另称含后续若干机型，本次未逐条加载该页面正文）。凡涉及"某功能是否采集某类健康数据"，应先判定设备与地区可用性，再进入清单。

法定分类（分析性推断，本次未核验法律原文）：健康与医疗数据在 GDPR Art.9 下通常落入特殊类别，在 CCPA/CPRA 下通常落入敏感个人信息，在《个人信息保护法》第 28 条口径下属敏感个人信息。三条落地映射：**其一**，端到端且密钥在受信设备的路径（如 Health Sharing）在处理者义务分配上更接近用户侧控制，可作为对外举证的最强事实；**其二**，"未加密本地备份不含健康数据"这条规则实际是合规正收益（苹果以保护类判定决定纳入），值得写入面向用户的治理说明；**其三**，苹果"不得用于广告"的审核条款不等于法定处理合法性基础，两者不可互相替代。

---

# 生物特征与认证材料

本章只处理**生物特征数据作为资产**的存储位置、保护强度、流转路径与删除语义。认证子系统的工程细节（安全飞地实现、匹配算法、活体检测与对抗样本）由系列其他篇覆盖，此处不重复；盗窃与胁迫场景仅作为资产行上的风险标签出现。

## 7.1 Face ID：数学表征、部分采集影像与失败匹配记录

《PSG》（26.5 版）§Optic ID, Face ID, and Touch ID（pp.31–42）给出本类最明确的两条治理事实：**其一**，眼/面/指纹数据由 Secure Enclave 处理，设备上保存的是**数学表征（mathematical representation）**，匹配在 SEP 内完成；**其二**，原文表述为 Face ID 数据（含数学表征）**不离开设备且不进入备份**（"…never leave the device and aren't stored in backups"）。这是全篇唯一一类被苹果明文排除在所有备份域之外的用户生物数据，也是清单上保护强度最高的行。

但"最高保护"不等于"最小集合"。按《PSG》（26.5 版）描述，为 Face ID 加密保存、仅供 Secure Enclave 使用的内容包括：**注册时的人脸数学表征、部分采集影像（partial captures）与相关信息、有助于后续匹配的失败尝试（failed attempts）、调试信息**。用于计算人脸影像在算出数学表征后即被丢弃。

| 资产 | 位置 | 是否离开设备 | 是否入备份 | 更新/删除时机 |
|---|---|---|---|---|
| 人脸数学表征（注册数据） | Secure Enclave 加密存储 | 否（原文） | 否（原文） | 重置 Face ID 时移除 |
| 部分采集影像与相关信息 | Secure Enclave 加密存储 | 否 | 否 | 苹果未公开逐项披露其保留时长 |
| 失败匹配尝试（有助于后续匹配者） | Secure Enclave 加密存储 | 否 | 否 | 用户停止匹配则丢弃新数据；选择 reset 则重置 |
| 调试信息 | Secure Enclave 加密存储 | 否 | 否 | 未披露 |
| 采集影像（原始帧） | 内存态 | 否 | 否 | 计算数学表征后即丢弃 |
| 增强用新表征 | Secure Enclave | 否 | 否 | 成功匹配后可能用于增强注册数据；失败但质量高于阈值且用户随即输入密码时再次采集并增强 |

"增强机制"是本类的关键治理点：注册数据不是一次性快照，而是**随使用持续演进的集合**。这意味着任何"某时点之后不再匹配旧外观"的合规陈述都需注明数据在变。误匹配率的可引用原文是：Optic ID 或 Face ID（含戴口罩模式）低于 1/1,000,000；Touch ID（同一手指多指征）或**两个 Face ID 外观**时为 1/500,000；5 次失败匹配后要求密码；双胞胎/近亲及遮挡下半脸时误匹配率更高。这些数字描述的是**认证决策的错误率**，不应被误读为"生物数据泄露风险"，两者是不同风险轴——清单中应分别落在"完整性风险"与"机密性风险"两列。

## 7.2 TrueDepth 派生数据与外部通路约束

Face ID 依赖深度成像子系统，其派生数据在报告口径下分两类：一类是服务于认证的人脸数学表征（见 7.1），另一类是**非认证用途的深度/影像派生产物**（如表情与动效类应用产物）。前者有明确的"不入备份、不离开设备"表述，后者的保护归属须按其所处 App 与文件类判定，不能沿用生物特征的结论。本次核验的已加载来源未提供对后者的逐项披露，写作时应标注"公开资料未逐项披露"。

《PSG》（26.5 版）提供了一条可直接用于控制的隔离表述：Secure Enclave 每次执行后会重置 **Secure Neural Engine** 状态以保持 Optic/Face/Touch ID 数据安全；且外部存储路径（如外接显示器路径）**不承载生物特征数据**（原文为 "don't include Optic ID, Face ID, or Touch ID data"）。治理价值在于：它把"生物特征数据可否随输出通路外流"这一问题给出了肯定的否定答案，是对外举证时可直接引用的一手条款。

## 7.3 Touch ID

Touch ID 的资产结构与 Face ID 同构：指纹数学表征存于 Secure Enclave，匹配在 SEP 内完成，App 侧只得到匹配成功/失败、无法访问生物特征数据，并可查询是否已注册。差异点主要在错误率口径与多指征注册（同一手指可多次录入，计入 1/500,000 档）以及可注册指征数量对攻击面的影响（属认证强度问题，本章不展开）。

| 属性 | Face ID | Touch ID | Optic ID |
|---|---|---|---|
| 表征类型 | 人脸数学表征 | 指纹数学表征 | 眼部（虹膜）数学表征 |
| 匹配位置 | Secure Enclave | Secure Enclave | Secure Enclave（配合 Secure Neural Engine 做眼部数据分析） |
| 明文"不入备份" | 是（原文） | 是（《PSG》在备份章节把三类一并排除） | 是（同左） |
| 已引用错误率 | <1/1,000,000（含口罩模式）；两外观时 1/500,000 | 1/500,000（多指征/两外观档） | <1/1,000,000 |
| 机型限定说明 | 依机型 | 依机型 | Apple Vision Pro 类设备 |
| 置信 | 已核实 | 已核实 | 部分核实（本次仅抓取到章节位置与关键句，未整段摘录；定稿前建议再对《PSG》§Optic ID security 取原文） |

## 7.4 Optic ID（限定表述）

Optic ID 的位置已定位（《PSG》§Optic ID, Face ID, and Touch ID 与 §Optic ID security/matching），可引用的要点是：SEP 与 Secure Neural Engine 用于眼部数据分析，**眼睛影像在算出数学表征后即被丢弃**（原文与 Face ID 同构），误匹配率低于 1/1,000,000，且其数据不离开设备、不进入备份。但本次**未整段摘录该节原文**，因此凡涉及 Optic ID 的具体注册数量、增强机制、失败尝试保留范围等细节，本报告一律标注为"本次仅定位到章节与关键句，细节以《PSG》相应版本原文为准"。

同类需要限定的还有空间计算设备的其他派生数据：**Persona、空间捕捉（Spatial Capture）、眼动与手部追踪数据**在本次已加载文本中未定位到对应披露，属公开资料未披露。这三类在直觉上常被归为"生物特征相关"，但在没有一手条款支持的情况下，清单上应记为"存在性明确、保护语义未知"的占位行，而不是套用 Face ID 的"不入备份"结论——这是资产视角下最容易犯的一类错误：用相邻类别的保护结论填空。

## 7.5 认证决策输出、口令回落与密钥绑定

从数据类别看，生物特征子系统真正对外输出的是**决策结果**而非特征本身，其清单行包括：匹配成功/失败、是否已注册、以及由生物特征解锁的密钥可用性状态。已核实机制如下。

| 输出/状态 | 定义 | 谁可见 | 与密钥的关系 |
|---|---|---|---|
| 匹配成功/失败 | App 只得到布尔结果 | App | `kSecAccessControl*` 类密钥可由生物特征解锁 |
| 是否已注册 | App 可查询 | App | 构成设备配置指纹的一部分 |
| 重启后的口令要求 | 生物特征无法解锁的密钥已丢弃 | 系统/用户 | 重启即回到"需密码"状态 |
| 连续 48 小时未用生物特征等条件 | 回落到要求密码 | 系统/用户 | 周期性强制口令，压缩生物特征解锁的长期可用性 |
| 5 次失败匹配 | 要求密码 | 系统/用户 | 与自动抹除策略可联动（Watch/iPad/Vision Pro 支持多次失败后自动抹除） |
| 戴口罩模式有效期 | 开启后有效 **6.5 小时** | 系统/用户 | 到时后需密码重验 |

与数据保护的耦合点是：生物特征开启时，**类密钥不被丢弃，而是用交给生物特征子系统的密钥包裹**（《PSG》§Optic ID, Face ID, Touch ID, passcodes and passwords）。这条机制说明生物特征在本机密码学体系中扮演"密钥可用性门"的角色，因此"生物特征数据不离开设备"与"类密钥受口令 + 设备 UID 保护"两条事实要同时登记，缺一即会高估或低估保护强度。

另一条常被忽略的流转面是**涉证 escrow**：iOS 密码学 escrow 中会暴露"类密钥列表：Key UUID、所属文件或钥匙串 Data Protection class"（已核实）。这不构成生物特征数据外流，但构成**保护结构的元数据外流**——它告诉持有者哪些文件属于哪一类，从而把"哪些数据在锁屏下仍可读"变成可查表问题。治理结论：任何依赖"最高保护类"作为唯一控制的数据，应同时评估其在 escrow/受管场景下的类归属可见性。

把上述机制汇成一张"认证材料 → 密钥可用性"的回落矩阵，可以直接用于资产行的保护档判定（依据为《PSG》（26.5 版）§Optic ID, Face ID, Touch ID, passcodes and passwords 与 Data Protection classes 两节）。

| 设备状态 | Class A（`NSFileProtectionComplete`） | Class C（第三方默认） | Class D（`NSFileProtectionNone`） | `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly` 项 |
|---|---|---|---|---|
| 已解锁 | 可读 | 可读 | 可读 | 可读 |
| 刚上锁（Class A 类密钥尚未丢弃；原文为 Require Password = Immediately 时约 10 秒） | 短暂可读 | 可读 | 可读 | 不可读 |
| 锁定且类密钥已丢弃 | 不可读 | **仍可读** | 可读（密钥仅受 UID 保护） | 不可读 |
| 重启后（首次解锁前） | 不可读 | 不可读 | 结构上依赖 UID，具体可读性未逐项披露 | 不可读 |
| 生物特征不可用于解锁密钥的场合（如重启后） | 需口令 | 需口令 | 需口令 | 需口令 |
| 口令被移除或重置 | 派生链改变，视服务而定 | 视服务而定 | 苹果以"快速远程抹除"描述其价值 | **项因丢弃密钥而失效** |
| 连续 48 小时未用生物特征等条件触发 | 回落到口令 | 不变 | 不变 | 不变 |
| 5 次失败匹配 | 要求口令（可联动自动抹除策略） | 同左 | 同左 | 同左 |

矩阵的治理读法只有一句：**"锁定"不等于"保护"**。Class C 与 Class D 在锁定状态下仍可读，而 Class D 的密钥全部在设备上、加密主要服务于快速抹除；因此凡在清单中被标为"设备已锁定即安全"的行，都必须回查其类归属，尤其要检查是否存在未指派保护类而被默认落到 Class C 的第三方数据。

## 7.6 Siri 语音数据、听写与语音个性化

语音是生物特征与用户内容的交叉类别：声纹具生物识别属性，转写文本具内容属性，二者在 iOS 上的处理路径不同，必须分行登记。

| 资产 | 位置/流转 | 可引用口径 | 置信与措辞 |
|---|---|---|---|
| Siri 交互音频 | 默认不外发留存 | 按苹果隐私页面所述："By default, Apple does not retain audio recordings of Siri and Dictation interactions." | 部分核实（本次未取得该页正文，仅检索摘要） |
| 计算机生成转写 | 用于改进 Siri 与听写 | 同页原文："Computer-generated transcripts are used to improve Siri and Dictation." | 部分核实 |
| "改进 Siri 与听写"选项 | 选择加入（opt-in） | 按苹果隐私页面所述为可选加入 | 部分核实 |
| Siri 信息（iCloud 类别） | 密钥在受信设备 | *iCloud data security overview* 表格中 **Siri information** 在标准保护下即端到端 | 已核实 |
| SiriKit 上报的 App/用户专属词汇 | 由 App 提供给 Siri 以提升识别（如联系人姓名） | 《PSG》（26.5 版）§SiriKit：保留期至**设备删除该 App、用户在 Settings 关闭该 App 的 Siri 集成，或相关会话结束**为止 | 已核实 |
| Siri 对受保护数据的授权前置 | 未授权通讯录时不会解析"我哥哥" | 《PSG》（26.5 版）§SiriKit：Siri 在向 App 交付受保护数据前检查该 App 的用户授权 | 已核实 |
| 某些请求的临时上下文 | 如导航请求会临时提供联系人位置 | 同上 | 已核实（该句存在） |
| 具体留存天数 | — | 未取得原文 | **未核实**：写作"苹果公开页面未披露具体留存天数"，不得引用"数月/数年"之类数字 |
| 语音个性化（个性化声音/端侧声纹建模） | — | 《PSG》（26.5 版）不含 Apple Intelligence 章节 | 未核实：须以苹果专项页面为准，本次不作断言 |

SiriKit 一条在治理上非常实用：它是苹果文档中**少数给出明确删除触发条件的 App 可上报数据**。三档触发（删除 App / 关闭该 App 的 Siri 集成 / 会话结束）可直接转成企业侧的清理验收标准；而第三方语音类 App（录音笔、会议记录）不在此列，需按第 8 章的内容类与索引类分别登记。

风险标签（仅作标注）：耳机与配件在长时间开启语音唤醒状态下产生的误触发片段属公开资料未披露类别；声纹推断风险来自长期转写集合而非单条命令，因此评估时应以"是否存在可选加入的改进集合"为分界，而不是以"是否使用过 Siri"为分界。

---

# 媒体、文档与用户内容

## 8.1 照片库：原片、HEIC 派生与内嵌元数据

照片是"一份资产、多重副本"的典型：同一张影像在原片、缩略图、编辑版本、分类索引、分享副本与备份集中共存，而它们的保护级别互不相同。

| 副本 | 基质 | 保护/流转 | 可引用口径 |
|---|---|---|---|
| 原片（HEIC/JPEG/DNG 等） | 相机卷与照片库 | iCloud 备份按原文包含"photos, and videos in the Camera Roll" | 已核实 |
| 高效编码与派生版本 | 照片库 | 派生版本是否独立加密未在已加载来源逐项披露 | 公开资料未披露 |
| 缩略图与预览索引 | 索引层 | 随备份与同步间接流转 | 未逐项披露 |
| EXIF/XMP 与内嵌 GPS | 随文件本体 | 一旦导出即完全由接收方控制 | 见 5.5 |
| 分析类元数据（校验和） | 服务端 CloudKit | 文件、照片校验和不端到端（用于去重/存储优化的收敛加密），密钥由 Apple 以标准保护保管 | 已核实 |
| 共享相册副本 | iCloud Shared Albums | **不支持 ADP**（可公开网页分享） | 已核实 |
| 链接分享副本 | "anyone with a link" | 内容以标准数据保护交付 Apple 服务器；每次分享操作的标题与代表缩略图以标准数据保护存储 | 已核实 |

治理结论：照片类风险主要由**副本与元数据**决定，而不是由"iCloud Photos 是否端到端"决定。iCloud 照片开启时，照片与视频已在 iCloud 中，因而**不进入 iCloud 备份**（原文以 iOS 8.1 / iPadOS 13.1 / OS X 10.10.3 为界）——这条规则常被误读为"照片没备份"，实际含义是同一对象在两个域之间不重复存储，需要按"照片由 Photos 服务域保护、其余由备份域保护"来登记。

## 8.2 端侧视觉索引：人物、场景、宠物分类与回忆

照片库的语义索引（人物聚类、场景与物体识别、宠物识别、以及基于这些索引生成的回忆/Memories 集合）在资产上属于**派生推断类**，其特点有三：不随原片删除而必然同步消失（索引是独立对象）、可被其他系统功能消费（搜索、建议、共享提示）、以及即使原片不离开设备，索引也可能揭示用户未意识到的关联（例如把某人物与特定地点/时间聚类）。

可引用的口径有限：《PSG》（26.5 版）未以清单形式披露端侧视觉索引的存储位置、保护类与删除时机；人物/场景分类的端侧执行边界属"按苹果公开宣传与用户指南所述在设备端完成"，本次未逐条加载对应页面正文，因此写作时必须使用「按苹果 iPhone 用户指南所述」「公开资料未披露其索引留存与删除传播」。清单处理方式是把索引单列为 T2/T3 行（聚合后按 T3 处理），并检查两个控制点：是否允许基于人物的共享建议、是否允许回忆类自动合辑（按用户指南所述可关闭）。

一个易被忽略的关联对象是 **Memoji**：在 *iCloud data security overview* 表格中 Memoji 被列为标准保护下即端到端的类别（已核实）。这与"表情符号数据可由面部参数派生"的直觉相关，说明苹果对某些派生表征采用了与内容同级的保护，可作为治理文档中"派生数据也应端到端"的内部先例引用。

## 8.3 iCloud Photos、共享与 ADP 例外

| 路径 | 标准保护 | 开启 ADP | 依据 |
|---|---|---|---|
| iCloud Photos（库本体） | 传输与静态存储加密，密钥在 Apple（"In transit & on server / Apple" 口径） | 端到端 | *iCloud data security overview*（已核实） |
| Shared Albums | 不支持端到端 | **仍不支持 ADP** | 两处来源一致（可公开网页分享） |
| 链接分享（anyone with a link） | 标准数据保护交付 | 分享选择该模式时内容按标准保护交付；标题与缩略图同理 | 已核实 |
| 分析校验和 | 非端到端（收敛加密，密钥在 Apple） | 同左 | 已核实 |
| 备份中的照片 | 相机卷照片视频入备份 | 若 iCloud Photos 已开，则不入备份（避免重复） | 已核实 |

这里有一条必须原样保留的口径冲突：*iCloud data security overview* 当前表述为"标准保护下已有 15 个类别端到端加密，开启 ADP 后升至 25 个"，而《PSG》（26.5 版）表述为"从 14 增至 23"。两组数字不可混用，本报告以支持页为主口径并注明核验日期，同时在合规文档中保留"苹果不同文档给出 25 / 23 两种口径"的事实，避免因引用差异导致对外陈述不一致。

## 8.4 视频、屏幕录制、语音备忘与转写

| 类别 | 存储与流转 | 可引用口径 | 置信 |
|---|---|---|---|
| 视频（相机卷） | 入 iCloud 备份；或随 iCloud Photos 同步 | 《PSG》备份原文含 "videos in the Camera Roll" | 已核实 |
| 屏幕录制 | 作为媒体文件进入照片/文件类路径 | 其是否含敏感画面残留（通知、密码自动填充可视）取决于录制时状态 | 本次未逐条核对，按 iPhone 用户指南所述处理 |
| 语音备忘 | 可同步至 iCloud；**ADP 新增端到端集**包含 Voice Memos；开启相应 iCloud 功能时"aren't included in your backup" | 两处来源（108756/102651 与 102325） | 已核实 |
| 语音转写文本 | 随音频或索引存放 | 具体留存、模型端侧边界与可删除性未逐项披露 | 公开资料未披露 |
| 音频类听写片段 | 见 7.6 | 按苹果隐私页面所述默认不留存音频 | 部分核实 |

屏幕录制在清单上的特殊性在于它是**跨类别的瞬时快照**：一次录制可同时把通信内容、凭证自动填充界面、位置信息与健康数据写入媒体类别，从而把它们从原本各自的强保护域搬运到"媒体文件"这一可自由导出的弱边界类别。治理上应以"是否允许录屏出现在共享/导出路径"作为控制点，而不是把录屏文件本身按原始类别评级。

## 8.5 文件、文档存储、扫描与 OCR 文本、下载内容

| 类别 | 基质 | 默认保护 | 流转 | 备注 |
|---|---|---|---|---|
| App 容器内文档 | 文件系统 | **`NSFileProtectionCompleteUntilFirstUserAuthentication`（第三方 App 默认类）** | 备份（app data 入 iCloud 备份）、导出、分享扩展 | 默认类意味着"首次解锁后长期可读"，是清单上最大面积的弱档 |
| iCloud Drive 文件 | 云 + 本机缓存 | 标准保护；**ADP 新增端到端** | 同步、协作、web 访问 | 协作/实时协同（Keynote/Numbers/Pages）需服务端中介，不支持 ADP |
| Freeform 画布 | 云 | ADP 新增端到端；但 **"Send a Copy" 链接有效期内以未加密状态存放**（与账户加密设置无关） | 分享 | 少数"分享动作可击穿加密设置"的实例 |
| 扫描与 OCR 文本 | App 容器/照片库/文档 | 随宿主类别 | 备份、导出 | OCR 文本可被索引消费，形成二次派生 |
| 下载内容（浏览器） | App 容器 | 未逐项披露其指派类 | 备份、共享 | Safari 在标准即端到端集内，书签另列为 ADP 新增项 |
| Wallet 通行证 | Wallet 域 | **Wallet passes 属 ADP 新增端到端**；Apple Pay Device Account Number 不入备份、不入 Apple Pay 服务器 | 同步/共享受限 | 支付类与卡券类需分行 |

两条值得强调的一手事实。其一，Apple Pay 的令牌化描述原文为："Device Account Number is created by the card issuer, sent encrypted to Apple, and then stored in the Secure Element. This unique Device Account Number is encrypted in such a way that Apple can't access it."，并明确 **"The Device Account Number in the Secure Element is never stored on Apple Pay servers or backed up to iCloud."**；交易时与 DAN 一起计算 one-time code（一次性密文），商户侧得到的是经 DAN + 一次性密文的凭证而非真实卡号。其二，苹果对交易信息的表述是否定式的——大意是**苹果不收集可回溯到用户的交易信息，支付交易发生在用户、商户与发卡方之间**；撰写时必须保持否定式，不得写成肯定句。

文件类本身是可以"选"的，这一点对自建应用、企业交付物与设备端工具同样成立。下表把《PSG》（26.5 版）§Data Protection classes 的机制描述转成选择规则（规则列为本报告的分析性归纳，类名与时机列为文档事实）。

| 需求场景 | 应选类 | 关键代价 | 机制要点 |
|---|---|---|---|
| 凭证、健康类、私密文档 | `NSFileProtectionComplete` | 后台任务不可读 | 类密钥由用户密码 + 设备 UID 派生；上锁后类密钥从内存丢弃 |
| 锁屏下仍需写入（如后台下载邮件附件） | `NSFileProtectionCompleteUnlessOpen` | 已打开的文件不可在锁定期继续写 | Curve25519 ECDH + NIST SP 800-56A One-Pass Diffie-Hellman；KDF 为 Concatenation KDF（SP 800-56A §5.8.1 Approved Alternative 1），PartyUInfo/PartyVInfo 为临时与静态公钥，哈希 SHA-256；文件关闭即从内存清除 per-file 密钥 |
| 一般业务数据（默认档） | `NSFileProtectionCompleteUntilFirstUserAuthentication` | 首次解锁后长期可读 | 与 Class A 同类但上锁/登出后不移除类密钥；第三方 App 默认 |
| 需要"快速可抹除"而非保密的场景 | `NSFileProtectionNone` | 密钥全在设备上 | 类密钥仅受 UID 保护，存于 Effaceable Storage；macOS 不支持 Class D；未指派类的文件仍以密文存储（iPhone/iPad/Apple Vision Pro 皆如此） |

钥匙串侧的选择规则与文件侧同构但密钥与函数独立：需要"绝不进备份、绝不进 iCloud Keychain、绝不进 escrow"的机器本机密，应使用 `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly`（并接受口令重置即失效的代价）；需要后台刷新工作的凭证则按《PSG》建议使用 `kSecAttrAccessibleAfterFirstUnlock`——这条建议同时给出了该档的实际弱点：锁屏后仍可用。至于 `*ThisDeviceOnly` 六个变体的逐一语义，应引用 developer.apple.com 的相关开发者文档并明确标注其为开发者文档而非《PSG》。

## 8.6 书签与阅读列表、备忘录、剪贴板与第三方 App 用户内容

| 类别 | 保护与流转（可引用） | 治理注意 |
|---|---|---|
| Safari 历史与网站数据 | *iCloud data security overview* 表格中 **Safari** 在标准保护下即端到端 | 但历史类是否等价于该行的覆盖范围须以该页表格逐项核对（本次已核实该行存在） |
| Safari 书签 | 列为 **ADP 新增端到端** 项 | 与上一行并列出现在同一表格，说明苹果把"书签"与"Safari"作为不同条目处理；报告须并列引用而非合并 |
| 阅读列表/阅读器状态 | 未逐项披露 | 按内容类保守登记 |
| Notes（备忘录） | 本地存储的备忘录**会被包含**在备份中（102325 已核实）；iCloud Drive/Notes 同步；**Notes 属 ADP 新增端到端** | 备忘录常含凭证片段、地址与个人计划，聚合后可支撑轨迹重建 |
| 锁定备忘录 | 语义为对单条备忘录加锁 | 本次未加载一手条款说明其密钥归属与是否入备份 → 公开资料未逐项披露；不得推定其等同于钥匙串级保护 |
| 剪贴板（Pasteboard） | 跨设备接续（Handoff）场景可把内容带到其他设备；系统会在 App 读取剪贴板时向用户提示访问 | 本次未加载对应开发者/支持页正文 → 按苹果公开文档所述处理，具体提示条件与留存未逐项披露 |
| 第三方 App 用户内容 | 处于 App 沙盒容器，默认 Class C；钥匙串项仅可在**同一开发者的 App** 间共享（经 Apple Developer Program 分配前缀 + access groups，由代码签名/描述文件强制） | 内容外发由开发者决定；营养标签是其唯一常规用户侧声明渠道（见 2.6） |

剪贴板应被登记为**短生命周期但跨设备的高敏通道**：它在设计上是临时缓冲，但实际常承载凭证、验证码与私密文本；一旦发生跨设备接续，原本在本机内存中的内容就进入了另一台设备的存储域。苹果公开的粘贴访问提示机制在治理上属于"可观测性控制"而非"保密性控制"——它让用户知道被读取，但不阻止读取，两者的差别必须在控制说明中写清（本次未取得该机制的一手条款原文，措辞须限定为按苹果公开文档所述）。

本章小结性结论：媒体与用户内容类的风险不来自"存储是否加密"，而来自**副本扩散与派生索引**。原片可以端到端，但校验和、分享标题、缩略图、链接副本与 OCR 文本各自处在更弱的路径上；清单必须把每一次复制与派生都写成独立行，否则"照片受保护"这类结论在实务中不可辩护。

---

# 设备使用、遥测与索引数据

本章论题：**索引与遥测类数据在治理上被系统性低估，而它是全设备中最容易被聚合的一层。**理由有三：它没有"用户可见的原始形态"，因此不出现在常规设置盘点里；它默认被视为缓存，因此在风险评估中被降档；它在时间维度上连续，因此单条无意义、序列有意义。iOS 上恰好存在一组反例——键盘学习词典、屏幕使用时间、Safari 等条目被苹果列在"标准保护下即端到端"集内，说明苹果自己把这些派生/遥测类当作高价值资产。

## 9.1 诊断与分析数据：包含内容、查看与退出路径

可引用的结论分两层。机制层：诊断/分析类数据由系统组件产生并可上传，其字段构成在《PSG》（26.5 版）中未被逐项列举；用户侧控制层：按苹果隐私页面所述，可在设置中查看与分享诊断与使用数据，并存在关闭共享的开关；具体开关名称、数据字段清单与保留时长本次未逐条加载一手页面核对，因此写作时须限定为「按苹果隐私页面所述」「公开资料未逐项披露」。

| 项目 | 是否可引用一手披露 | 本报告处理 |
|---|---|---|
| 崩溃日志的字段构成 | 未逐项披露 | 公开分析认为可包含路径、模块与标识类信息，不作为事实句 |
| 分析数据的上传频率与保留 | 未披露 | 记为"未披露"，控制上假定已上传 |
| 用户查看入口 | 按苹果隐私页面所述存在 | 采用限定语 |
| 退出（opt-out）效果 | 未逐项披露其范围 | 写作"关闭后新数据的采集停止，已上传副本的处理未披露" |
| 与第三方 App 分析 SDK 的边界 | 属开发者声明范围 | 由营养标签与隐私清单核对（枚举本次未核对） |

苹果已登记的缺陷为"分析数据是独立受保护对象"提供了一手佐证：watchOS 7.6 安全内容文档中组件 **Analytics** 的 **CVE-2021-30871**，Impact 为 "A local attacker may be able to access analytics data"（logic issue，通过改进限制修复）。该文档为 2021 年历史版本，仅作类别论证，不可用于当前版本线。

遥测层的可执行控制清单如下（"依据强度"列区分一手条款与用户指南级/未披露项，避免把不确定项写成硬约束）。

| 控制动作 | 覆盖的资产行 | 依据强度 |
|---|---|---|
| 关闭诊断与分析数据共享 | 系统分析、崩溃日志上传 | 中（按苹果隐私页面所述存在开关；字段与保留期未逐项披露） |
| 定期查看已收集的诊断内容 | 本机诊断归档 | 中（按苹果隐私页面所述可查看与分享） |
| 对第三方 App 的分析 SDK 做声明核对 | Usage Data、Diagnostics 类收集 | 中（营养标签为开发者自我声明，枚举本次未逐项核对） |
| 关闭可选加入的 Siri/听写改进 | 转写语料 | 强（按苹果隐私页面所述默认为不留存音频、改进项为 opt-in） |
| 评估 HomeKit 自动化事件保留 | 到家/离家事件、HSV 剪辑 | 中（端到端状态强；保存天数与事件上限未一手确认） |
| 复核 ADP 开启状态与恢复材料 | 备份、照片、备忘录、语音备忘、快捷指令、Wallet 通行证 | 强（覆盖与前提均有明文） |
| 在受管场景选择注册形态 | 设备查询面与数据分离 | 强（User Enrollment 与 Device Enrollment/ADE 差异有明文） |
| 对高价值目标评估 Lockdown Mode | 附件、网页技术、FaceTime、邀请、共享相册、外设连接、Wi-Fi/2G-3G、配置描述文件 | 强（存在性与功能域清单已核实；逐条完整清单为摘要级，需回原文复核） |

## 9.2 崩溃报告与内嵌上下文

崩溃报告的价值与风险同源：它为了可诊断性而携带大量上下文（进程与线程状态、加载的库、路径信息、部分配置值），而这些上下文可能把其他类别的存在性泄露出来（例如某文件路径暴露某 App 已被使用、某键名暴露某功能已开启）。清单上应把崩溃报告登记为**跨类别的存在性证明**（presence proof），而非单一内容对象。

同一逻辑也适用于 iOS 27 与 iPadOS 27 安全内容文档中与本层相邻的条目：组件 **Accounts** 的 CVE-2026-65404（"A malicious application may be able to bypass Privacy preferences"，authorization issue）、**Accessibility** 的 CVE-2026-84616 与 **BackgroundAssets** 的 CVE-2026-65414 与 **Camera** 的 CVE-2026-84533（均为 "An app may be able to access sensitive user data"）、**Bluetooth** 的 CVE-2026-84560（"An app may gain unauthorized access to Bluetooth"）。这些条目的共同结构是：**敏感数据访问的边界位于授权/状态管理，而非加密**。同时必须保留一条负向结论：该文档中 "may have been exploited" 命中数为 0，即苹果未将 iOS 27 安全内容文档中的任何条目登记为在野利用——这一"零登记"事实本身是治理文档中防止过度声称的必要引用。

## 9.3 Spotlight 与 Siri 建议索引

索引类资产的特征是**它把分散在各类别中的信息以可检索形式合并**。Spotlight 索引聚合 App 内容、联系人、邮件、日历、媒体与设置项；Siri 建议进一步基于使用时序、位置与社交关系做排序。二者都不新增原始数据，却显著降低"跨类别关联"的成本——这正是 5.6 中"跃升发生在读取侧"的机制。

| 索引/建议资产 | 生成依据 | 保护语义可引用性 | 控制点 |
|---|---|---|---|
| Spotlight 本地索引 | 各数据源内容与元数据 | 本次未加载逐项披露，保护类未知 | 按 iPhone 用户指南所述可在设置中关闭特定类别的搜索结果 |
| 索引中的敏感字段（消息、健康、密码） | 由被索引类别带入 | 索引是否延续原类别保护未披露 | 治理上假定"索引 = 最低共同保护"，并以此为整改目标 |
| Siri 建议与快捷操作 | 使用时序 + 位置 + 关系 | **Siri information** 在标准保护下即端到端（102651 已核实） | 关闭建议、重置建议历史（按用户指南所述） |
| SiriKit 意图与词汇上报 | App 主动提供 | 保留期至删除 App/关闭集成/会话结束（《PSG》26.5 版 §SiriKit，已核实） | 唯一有明确删除触发条件的行 |
| 搜索建议的服务端聚合 | 查询词与设备上下文 | 未披露 | 记为"未披露"，避免以"端侧处理"默认结论填空 |

一条实践规则：**评估任何"设备端学习/推荐"功能时，先问索引写到哪个基质、由哪个类保护，再问它是否上传**。多数争议（例如"是否把用户习惯送到服务端"）源于第一个问题被跳过。

索引层尽调可采用七问式清单，它把"看不见的数据"转成可问、可记、可复核的条目。

| 尽调问题 | 期望答案类型 | 无法回答时的处理 |
|---|---|---|
| 索引写到哪个基质（本机文件/数据库/服务端） | 明确位置 | 按最弱基质假定 |
| 该基质由哪个 Data Protection 类保护 | 类名 | 按第三方默认类 `NSFileProtectionCompleteUntilFirstUserAuthentication` 假定 |
| 索引是否包含其他类别的原文片段 | 是/否 + 示例类别 | 按"包含"假定（如通知历史含正文） |
| 索引是否随备份流转 | 备份纳入矩阵 | 参照 4.1 矩阵按开关组合判定 |
| 索引能否被其他 App 或扩展读取 | 读接口是否存在 | 假定可读，并检查已登记的缺陷类型（授权/状态管理类） |
| 索引的服务端对应物是否存在 | 服务端聚合同类信息与否 | 记"未披露"，不写"不存在" |
| 关闭功能后索引是否被清除 | 清除时机 | 记"未披露"，并把"重建成本"列为风险标签 |

七问的落点是把索引层从"性能话题"移回"数据治理话题"。在本报告的登记表结构中，索引层应作为独立登记块存在，其行数以被索引的数据源数量为单位增长——这也是为什么索引层的盘点工作量常被低估。

## 9.4 键盘个人词典、预测文本与 Safari 数据

键盘是 iOS 上被低估程度最高的子系统：它以用户输入为训练源，长期累积词汇、共现关系与个人术语（医学名、内部项目名、亲昵称谓），其学习词典的泄露可以直接暴露健康与关系信息。苹果的口径给出了明确信号——**QuickType Keyboard learned vocabulary** 与 **Safari**、**Screen Time**、**Maps**、**Health data**、**Home data**、**Passwords and Keychain**、**Messages in iCloud**、**Payment information**、**Apple Card transactions**、**Journal data**、**Siri information**、**Wi-Fi passwords**、**W1 and H1 Bluetooth keys**、**Memoji** 一同被列入 *iCloud data security overview* 表格中"标准保护下即端到端"的 15 项（已核实，逐项摘自该表）。这意味着键盘词典作为派生资产被苹果按高敏感等级处理，而清单上把"预测文本模型"降为 T1 是错误的。

| 使用数据 | 层级判定（本报告） | 端到端状态（可引用） | 备注 |
|---|---|---|---|
| 键盘学习词汇/个人词典 | T3 | 标准保护下即端到端 | 与设备语言设置、第三方输入法分别登记 |
| 候选与纠正历史（预测） | T3 | 未逐项披露 | 常与词典同库，保守按同档 |
| Safari 浏览历史与网站数据 | T3 | Safari 行：标准保护下即端到端 | iOS 26.2 安全内容文档登记 CVE-2025-46277（"An app may be able to access a user's Safari history"）作为类别价值的独立佐证 |
| Safari 书签 | T2 | 列为 ADP 新增端到端项 | 与上一行在同一表格并列出现，须分别引用 |
| 网站数据（Cookie、存储） | T2/T3 | 覆盖范围以该表逐项核对 | 与追踪标识交叉 |
| 隐私浏览会话 | T3 | 未逐项披露 | 按苹果公开材料所述不写入常规浏览历史与同步；本次未加载对应条款原文，故按"仅本机 + 内存驻留"保守登记为派生行 |

## 9.5 App 使用遥测与屏幕使用时间：一条被误当便利功能的强证据链

屏幕使用时间（Screen Time）是"设备使用遥测"在用户侧的显式形态，也是本章论证聚合力的最佳样例。它同时记录：应用启动与前台时长（行为）、时段分布（作息）、类别归类（兴趣）、"停用"与限额突破（自控意图与失败点）、以及"查看我的活动"下的日/周聚合。这些字段单条皆属 T1，但联合后能推断睡眠时段、工作日程、注意力对象与生活习惯，实际接近 T3/T4。苹果的处置口径可作为治理依据：**Screen Time 被列入标准保护下即端到端集**（已核实，102651 表格），即其云端副本的密钥在受信设备。

| 使用遥测 | 记录内容 | 是否离开设备 | 治理注意 |
|---|---|---|---|
| 屏幕使用时间明细与聚合 | 使用时序、类别、突破事件 | 同步（端到端）；不属互通性例外 | 家庭共享/儿童场景下对第三方（家庭成员）可见，是本类最大暴露面 |
| App 内使用与分析（开发者侧） | 由 SDK 定义 | 是 | 以营养标签"Usage Data"类声明核对（枚举本次未核对） |
| 系统与设备分析 | 运行状态、崩溃、性能 | 是（可关闭） | 字段未逐项披露 |
| 通知历史 | 内容片段 + 时间 | 视功能而定 | 片段可承载消息正文，需按内容档登记 |
| 受管注册的设备查询面 | 设备/App/证书/合规状态 | 是 | 见下段 |

受管设备的使用遥测必须单列，因为它是唯一由**第三方（雇主/学校）主动拉取**的类别。《PSG》（26.5 版）§Device management（pp.230–237）口径为：设备管理可"enroll Apple devices in an enterprise or educational environment, wirelessly configure and update settings, monitor compliance, manage software updates, and even remotely wipe or lock managed devices"；注册描述文件明确授权服务端"send commands and configurations to the device and to **query certain aspects of the device**"。苹果**未在此列出可查询字段清单**，因此任何"MDM 可读取短信/照片/健康"的断言在本次一手材料中均无依据；命令级清单（如设备信息、合规状态、应用列表、证书列表、设备位置查询、日志上传等类别）须以 developer.apple.com 的 MDM 协议参考逐条核对后再落档。

可确定的结构性事实是两条：其一，**User Enrollment**（用户自有设备，需 Managed Apple Account）以加密方式隔离受管数据，而 **Device Enrollment / Automated Device Enrollment（ADE）** 具备更大权限集（含可阻止用户移除 enrollment profile、抹除设备）；iOS 17 / iPadOS 17 / macOS 14 / visionOS 1.1 起账户驱动的 Device Enrollment 也有类似数据分离机制。其二，设备证明环节：ADE 流程中设备向证明服务器索取证书，签发方为 **Apple Enterprise Attestation Root CA**，叶子证书按设备类型包含特定属性，且属性值仅在证明服务器能验证时包含——但 **freshness code 不做验证即包含**。这属于"设备身份元数据在证明链中的暴露"，治理上应把 ADE 设备视为具有独立可追踪标识的资产。移除描述文件时，其下所有配置与设置（视注册类型与配置而定，也含受管 App）一并移除，这是受管场景中唯一被明文承诺的"删除传播"。

## 9.6 HomeKit 事件日志、Lockdown Mode 与本层的控制映射

家居数据是使用遥测中"物理世界映射"最强的部分。可引用的机制事实（《PSG》（26.5 版）§HomeKit，pp.219–224）：HomeKit Secure Video 的运动检测触发后，剪辑经 home hub 与摄像头之间的专用本地连接（**per-session HKDF-SHA-512 派生密钥对**，在 HomeKit 会话上协商）发送；home hub **解密**音视频并在**本地**做事件分析；检出重要事件后用随机生成的 **AES-256 密钥 + AES-256-GCM** 加密视频剪辑，每个剪辑的 poster frame 用同一 AES-256 密钥加密；加密后的 poster 与音视频上传 iCloud，**相关元数据（含加密密钥）通过 CloudKit 以 iCloud 端到端加密上传**；**人脸分类数据**（人名与用于分类的人脸图像，可来自用户照片库或既往摄像头分析）以 iCloud 端到端加密存于 CloudKit，识别信息被写入剪辑元数据；每个剪辑会话可再分子段，每子段使用各自唯一的加密密钥。IP 摄像头（非 HSV）在远程访问时由 home hub **仅做中继不解密**，显示由独立系统进程安全渲染，**App 无法访问或存储视频流，也不允许截屏**。

| HomeKit 事件类 | 保护（可引用） | 未披露项 |
|---|---|---|
| Home data | 标准保护下即端到端（102651 已核实） | 事件保留天数与条数上限 |
| HSV 加密剪辑与 poster | AES-256-GCM 上传；密钥随元数据经 CloudKit 端到端 | 具体保存天数：《PSG》未披露；按 iCloud 用户指南所述"全部端到端加密，且视频不计入 iCloud 容量"；"10 天"仅见于第三方材料，本次未确认 |
| 人脸分类数据（人名 + 分类图像） | iCloud 端到端存于 CloudKit | 与照片库索引的双向同步细节 |
| 自动化触发时间线 | 未逐项披露 | 事件序列可用于推断"是否在家"，应按 T2 登记 |
| 送往 Siri 的家居控制 | 提供给 Siri 的是**最少的**家居配置信息且**匿名**；音频可能含具体配件或命令，但不与 home hub 上存储的配置信息关联 | — |
| Siri-enabled 配件经 Apple TV/HomePod | 音频帧经专用本地连接（同 per-session HKDF-SHA-512）送往 HomePod/Apple TV；Apple TV 解密后转交 Siri App，按对所有 Siri 音频输入的相同隐私保护处理；Apple TV **不访问除音频流以外的 Siri 请求数据、不在其上存储用户数据** | — |

本章的控制映射汇总如下，它把"索引/遥测被低估"这一论题转成可执行条目。

| 控制点 | 对应资产行 | 一手依据强度 |
|---|---|---|
| 优先开启 ADP，并确认恢复材料就绪 | 备份、照片、备忘录、语音备忘、快捷指令、Wallet 通行证等 ADP 新增端到端项 | 强（需双重认证 + 设备密码 + iOS 16.2 及以后 + 至少一个恢复联系人或恢复密钥） |
| 明确"备份开关"的降级后果 | iCloud 备份纳入的全部内容类与 Messages 容器密钥 | 强（备份密钥存于 Apple HSM，属 available-after-authentication；ADP 下转端到端） |
| 关闭可选加入的分析改进项 | Siri/听写转写的改进集合、诊断上传 | 中（按苹果隐私页面所述，具体效果未逐项披露） |
| 收紧受管注册形态选择 | 设备查询面与数据分离程度 | 强（User Enrollment 与 Device Enrollment/ADE 的权限集差异有明文） |
| 以"最低共同保护"评估索引 | Spotlight、Siri 建议、照片索引、HomeKit 事件 | 弱（保护类多数未披露，只能保守假定） |
| 对高价值目标启用 Lockdown Mode | 消息附件、网页技术、FaceTime、Apple 服务邀请、共享相册、外设连接、Wi-Fi 与 2G/3G 连接、配置描述文件等被限制条目 | 强（支持文档 *About Lockdown Mode* 已加载，为摘要级；《PSG》给出的功能域清单为 Background services、Connectivity、Device management、FaceTime、Game Center、Mail、Messages、Photos、Safari、System Settings、WebKit；两份清单不可混为同一列表） |

两点必须并列保留：Lockdown Mode 的定位原文面向"因人身份或工作而可能被最复杂的针对性雇佣间谍软件（targeted mercenary spyware）攻击的极少数人"，其代价是功能与性能；且开启 Lockdown Mode 时 **iCloud Private Relay 不工作**（《PSG》（26.5 版）Private Relay 条件之一为 "The device isn't in Lockdown Mode."）。治理文档若把"更强隐私"与"更多控制开关"当作单调递增关系，会在此处得出错误建议。

最后回到本章论题的证据闭环：苹果在 iOS 27 安全内容文档中登记了 **CloudKit — CVE-2026-86895**（"A local app may be able to read a persistent account identifier"）与 **iCloud — CVE-2026-84606**（"An app may be able to identify a user across reinstalls"），并在 iOS 26.2 文档中登记 **Messages — CVE-2025-43475**（"An app may be able to access sensitive user data"）。前者说明标识符与遥测关联结构被承认为独立泄露对象，后者说明消息类内容的访问边界属于可缺陷化的授权问题。需要同时记录的负向结论是：在本次已加载的苹果一手文档范围内，**没有**以照片或通讯录元数据泄露为 Impact 的确认条目，**没有** APNs 载荷留存或令牌追踪类的公开确认案例，iCloud/本地备份相关的数据暴露也**没有**公开确认案例（最接近的是上述"跨重装识别用户"条目）。第三方材料中的若干条目本次未加载苹果/NVD 原文核对，不纳入本报告事实。治理结论因此应当谨慎而非夸张：索引与遥测的风险主要来自**可聚合性与标识持久性**，其控制手段是分类登记、密钥域收敛与聚合层级复核，而不是把它们描述为"已在野被采集"。

# 应用容器、凭据与配置数据

第 2 章给出了分类轴与登记表结构，第 3 章处理了身份与标识符。本章把镜头拉近到**第三方 App 实际写下的东西**：容器里的文件、钥匙串里的凭证、WebView 里的会话状态、描述文件里的策略。这一层的盘点价值在于它是唯一"开发者可控、用户不可见"的层——用户能看见 App 图标，却看不见其容器内数百个文件的保护档差异。本章遵循第 1.4 节的四问模型，逐类落位，并在每节末尾给出一条可直接写入登记表的判定规则。

## 10.1 容器目录语义：清单的第一层坐标系

苹果面向开发者的文档把 App 的可见文件系统划分为若干**容器（container）**，其语义要点不在路径字符串，而在"谁可写入、谁可读回、代码签名如何约束"。可概括为四类。

| 容器类型 | 面向开发者的取得方式 | 典型内容 | 清单判定 |
|---|---|---|---|
| App 包容器（bundle container） | `Bundle.main.bundleURL` / App Store 安装管理 | 可执行代码、资源包、签名与描述文件 | 只读、由代码签名覆盖；不是用户数据载体，但**资源包中硬编码的密钥/样例数据属于数据资产** |
| 数据容器（data container） | `FileManager` 的标准目录 URL 接口（Documents、Library、tmp 等） | App 数据库、下载文件、缓存、偏好设置、日志 | 第三方数据的主体；默认保护类按第 1.5 节第 1 条一律记 Class C |
| App Group 容器（shared container） | `containerURL(forSecurityApplicationGroupIdentifier:)` | 主 App 与扩展之间的共享数据、共享缓存 | 可读范围由 access group 与代码签名决定，跨进程边界即扩大 |
| 文件提供程序容器（File Provider storage） | File Provider 扩展自身的存储目录 | 云盘类 App 在本机的物化文件与占位文件 | 同一份内容可能同时存在"占位于文件 App + 实体在扩展容器 + 副本在云端"三处 |

必须记录三点。第一，**路径字符串本身是实现细节**：数据容器目录名中包含随安装变化的 UUID，苹果的开发者文档要求 App 通过 API 解析路径而非硬编码；因此清单里以"容器类型 × 数据用途"为登记单位，而不是以绝对路径为登记单位。第二，tmp 与缓存类目录在开发者规则中被明确视为"不保证持久、不应承载需长期保留的用户内容"，但**是否被纳入备份、以何保护类落盘，本次证据基线内没有逐项披露**，故按「公开资料未披露」登记，并在风险评估时按"可能进入备份"的保守假定处理。第三，容器边界不等于密钥边界：同一容器内不同文件可以被写入方指派不同的 Data Protection 类，因此清单必须落到"文件组"粒度，而非"App"粒度。

**判定规则 R10-1**：凡未指明 Data Protection 类的第三方 App 文件行，保护档记为 `NSFileProtectionCompleteUntilFirstUserAuthentication`（《PSG》26.5 版所述的第三方默认类），并注明"若取证发生在首次解锁之后的锁定态，该类仍可用"。

## 10.2 App Group、扩展与共享目录：边界的合法性与代价

App Group 与 access group 是 iOS 上唯一被制度化允许的"跨 App 数据共享"通道。其约束由代码签名承担：组标识符由 Apple Developer Program 分配前缀，主 App 与扩展、同团队 App 必须在描述文件中声明同一组，运行时由系统强制校验。第 3.6 节已给出钥匙串侧的"仅同一开发者的 App 间共享"这一事实；本节补充**文件侧的对称规则**及其治理后果。

| 共享机制 | 参与进程 | 可被读到的数据 | 治理注意 |
|---|---|---|---|
| App Group 文件容器 | 同组的主 App、扩展、辅助 App | 对方写入的全部文件 | 只要有一个组内成员被攻陷或被恶意复制，组内全部数据即视为可达 |
| 钥匙串 access group | 同组 App | 组内共享的凭证项 | 共享项的 accessibility 由**写入时**决定，读取方无法提升；清单须按最弱档归组 |
| 分享扩展 / 操作扩展（Share/Action Extension） | 宿主 App 与被拉起扩展 | 用户主动交出的选择内容、临时拷贝 | 用户在系统界面里的分享动作即是"离开原容器"事件，应在流转路径列单记 |
| 键盘等输入扩展 | 扩展进程与接收输入的宿主 App | 输入流本身、扩展自身的持久词典 | 第 9.4 节已把键盘词典按 T3 登记；跨 App 输入聚合发生在扩展侧而非宿主侧 |
| 文件 App 与第三方文档提供程序 | 系统进程、提供程序扩展 | 物化的文档副本与元数据 | 一份文档的"位置"至少三处（见 10.1 第 4 行），删除需三处分别确认 |
| URL scheme / 通用链接 / 粘贴板 | 两个 App 进程 | App 主动交出的内容 | 属"应用间消息"而非"存储"，但常成为日志与历史残留的来源 |

这里的核心治理问题是**保护类不继承**。开发者文档给出的机制是：写入方在创建文件时指派保护类；从一个较高保护档的源容器把内容复制到 App Group 容器时，副本的保护档由**新写入方**决定，而不是由源文件决定。苹果未提供"保护类随复制传递"的公开承诺，因此本报告的清单规则是：凡经共享通道复制出来的副本，一律按目标容器写入方的默认档（即 Class C）登记，并把源文件的高档视为"仅对原件成立"。这条规则在实务上的影响是：一个把加密相册导出为共享缓存缩略图的 App，其原件可能处于 Class A，而缩略图长期停留在 Class C——这正是第 16.3 节残留分析要处理的情形。

**判定规则 R10-2**：共享容器中的每一行必须回答"原件在哪、副本档为何、删除时两处是否都清"，三问缺一即记为盘点未完成。

## 10.3 钥匙串项模型：App 侧凭证的四种落位

第 3.6 节已经列出《PSG》（26.5 版）§Keychain data protection 的可用性对照表与 `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly` 的三条"不"。本节不重复该表，而是把钥匙串作为**App 凭证的存储产品**做类别拆解。

钥匙串项在开发者模型中按类别（class）组织：通用密码（generic password，最常见的第三方令牌载体）、互联网密码（internet password，与域名/账户绑定）、密钥（key，密码学对象）、证书（certificate）与身份（identity）。逐项属性名以 developer.apple.com 的钥匙串参考为准（本次未逐条加载该页正文）。真正决定风险的是三组属性的组合：**accessibility（何时可用）**、**是否同步到 iCloud Keychain**、**是否可被 App 之外的主体读取**。

| 凭证形态 | 典型 accessibility | 同步/备份后果 | 撤销的实际效力 |
|---|---|---|---|
| 长期刷新令牌（refresh token） | 多为 `kSecAttrAccessibleAfterFirstUnlock`（《PSG》对需后台工作场景的建议） | 若非 `*ThisDeviceOnly`，可随 iCloud Keychain 同步并进入密钥域更宽的域 | 本机删除项只切断"再取用新令牌"的能力；服务端未撤销的令牌在其有效期内对他方仍有效 |
| 短期访问令牌（access token） | 常同上，或落在容器内文件/数据库 | 若落文件则按 Class C 入备份 | 有效期自身即撤销机制；清单须同时登记有效期字段 |
| 会话 Cookie（由 App 自持） | 视实现 | 同上 | 服务端会话销毁与本机删除相互独立 |
| 机器本地密钥（如 App 自生成加密主密钥） | 应使用 `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly` | 该属性明确：不 iCloud 同步、不备份、不入 escrow keybag；仅存在于 system keybag | 口令被移除或重置即因密钥丢弃而失效——这是可恢复性与机密性的刻意交换 |

两个次生事实需要进入清单。其一，**escrow（涉证密钥袋）会暴露类密钥列表**：按《PSG》（26.5 版）对 iOS 密码学 escrow 的描述，其中包含"类密钥列表：Key UUID、所属文件或钥匙串 Data Protection class"。也就是说，即便内容密钥本身不可得，"哪些项存在、属于哪一档"这一元数据在特定流程中是可导出的结构化情报。其二，**iCloud Keychain 是一个独立的保护域**：《PSG》§iCloud Keychain 描述其跨设备同步范围（密码与通行密钥域）以及 iCloud 侧的 keybag escrow，并在 Advanced Data Protection 章节把 iCloud Keychain 指定为**ADP 服务密钥的存放保护域**（原文表述为服务密钥"kept entirely within the Apple Account's iCloud Keychain protection domain"）。这条对第 13 章至关重要：一旦 ADP 的服务密钥位于端到端保护域内，钥匙串就不再只是"凭证仓库"，而是整个 iCloud 密钥体系的锚点。

`*ThisDeviceOnly` 后缀变体（如 `kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly`）在《PSG》其他章节出现，本次已核实该字符串存在；六个变体的逐一语义本次未加载开发者文档原文，须以 developer.apple.com 关于 restricting keychain item accessibility 的开发者文档为引用对象，并明确标注其为开发者文档而非《PSG》。在清单中，本报告把 `*ThisDeviceOnly` 统一按"不同步、不备份"的结构性隔离处理——这一处理方向与《PSG》对唯一被逐条核实的那个变体的表述一致。

**判定规则 R10-3**：每个凭证行必须写出 accessibility 属性名（或"未指定，按最弱档假定"），并单独一列记录"服务端撤销是否独立于本机删除"。凡二者不同的行，删除语义不得写"已删除"。

## 10.4 网页存储：Cookie、localStorage、IndexedDB 与令牌

浏览器态数据是"看起来小、聚合起来极强"的一类：Cookie 承载会话与追踪标识，localStorage 与 IndexedDB 可被站点写入任意结构化数据（包括离线缓存的用户生成内容与令牌），Service Worker 缓存则把整份网页资源留在本机。iOS 上必须区分三个存储域。

| 存储域 | 归属进程/框架 | 与同步/备份的关系 | 清单注意 |
|---|---|---|---|
| Safari 网站数据（WebKit） | Safari 与系统 WebKit 数据域 | 第 9.4 节已按 102651 表格登记：Safari 行在标准保护下即端到端，Safari 书签列为 ADP 新增端到端项；书签与网站数据须分别引用 | 隐私浏览会话的落盘与同步细节本次未加载条款原文，按保守假定登记 |
| App 内 WebView 数据 | 由 App 持有的数据存储（默认存储与站点自有存储可分离；具体隔离规则以开发者文档为准） | 落在 App 容器内，按第三方默认类判定，并可能随 iCloud 备份的"app data"外流 | 这是最容易被漏记的一行：同一个网站的用户名、令牌与浏览内容可能在 App 内留有一份，与 Safari 中的那份保护域完全不同 |
| 站点凭证（iCloud Keychain 中的密码/通行密钥） | 系统凭证域 | 进入 iCloud Keychain 保护域 | 与 10.3 第四行的"机器本地密钥"不可混为一档 |

在 OAuth 与单点登录语境下，第三方 App 常见的三类令牌（授权码、访问令牌、刷新令牌）与前述四类凭证形态交叉。需要特别记录的是 **Sign in with Apple** 这一路径的数据分布：该框架在开发者文档中的定位是"Provide users the ability to sign in to your apps and websites using their Apple Account"，并区分端侧支持与 Web 支持、跨团队迁移三类场景。其后果是：开发者拿到的是与该 App 绑定的用户标识与（用户选择提供时）姓名/邮箱，而账户真实标识留在 Apple 侧；当用户启用隐藏邮箱时，开发者侧的联系人字段变成一个转发地址。至于转发地址格式、发送速率与收件人上限、可转发域名白名单、地址停用与回收，以及 Hide My Email 与 iCloud+ 的关系，本次未加载对应原文，一律按「具体参数以 developer.apple.com 的 Sign in with Apple 邮件中继文档与 iCloud+ 支持文档为准，本次调研未核对」处理，不作为事实句。

从资产视角，Sign in with Apple 值得单独登记的理由是它**把同一自然人在不同 App 中的标识切断**，从而降低跨服务可聚合性；这与第 3.3 节 ATT/IDFA 那一类"可跨 App 关联标识"形成治理上的对照。清单里应把"是否使用 Sign in with Apple 而非社交账号直连"作为一列可评估属性，它直接影响 17.2 节评级方法中的"可聚合性"因子。

## 10.5 应用数据库、日志、收据与备份纳入

App 的数据主体通常是本地数据库（关系型或对象图存储）加一组资源文件。可确定的机制事实只有一条：**它们处于 App 容器内、默认档、并被《PSG》（26.5 版）§iCloud Backup security 的原文纳入范围**——备份内容原文为"device settings, app data, photos, and videos in the Camera Roll, and conversations in the Messages app"。"app data"是一个未细化的集合，苹果未在该条款内给出排除项清单，因此第 13 章的备份矩阵对第三方 App 行只能写"按 app data 纳入，细项未逐项披露"。数据库的日志文件（预写式日志与事务日志）与之同处一个容器，是否被指派与主库不同的保护类属公开资料未披露，按同档假定。

App 自身产生的日志与崩溃信息是第二类。第 9.1、9.2 节已处理系统侧诊断与崩溃报告；App 侧日志的上传对象是**开发者及其嵌入的 SDK**，不是 Apple，其内容与留存完全由第三方策略决定，唯一常规的用户侧声明渠道是营养标签（见 18.5）。清单上应把"App 日志"独立成行并至少标注三件事：是否包含用户标识、是否写入 App Group 容器（从而可被同组其他进程读取）、是否进入备份。

第三类是收据与交易数据。App Store 购买在设备侧表现为与交易相关的本地记录与签名凭证，而其权威记录在账户侧。两条被文档化的事实构成本类清单的边界：**iCloud 备份不含已购内容本体**，只含已购内容的相关信息，恢复时从 iTunes Store/App Store/Apple TV/Books 重新下载，部分地区不自动下载，退款或下架内容不可用；**完整的购买历史与 Apple Account 关联**。由此得出的治理结论是：购买记录具有"账户侧长期可复原"属性，删除本机凭证或重装 App 都不影响服务端历史；第 12 章会把这一条与支付令牌化并置。

## 10.6 配置描述文件、已安装证书与受管注册数据

配置描述文件（profile）是"策略即数据"的一类，其资产属性常被忽略：它一旦安装，就同时改变设备行为与其他类别的可读范围。按《PSG》（26.5 版）§Device management（pp.230–237），配置可以以 .xml plist 或 json profile 下发，载荷示例包括"Passcode and password policies"、"Restrictions on device features"、"Network and VPN settings"、"certificates"、自定义邮件设置，并支持大量设备批量配置。

| 受管工件 | 谁可写入 | 用户可见性与可移除性 | 移除后的效果（可引用） |
|---|---|---|---|
| 注册描述文件（enrollment profile） | MDM 服务端下发 | 可见；Device Enrollment/ADE 情形下可被配置为阻止用户移除 | 移除 profile 时，其下所有配置与设置（视注册类型与配置而定，也含受管 App）一并移除——这是受管场景中唯一被明文承诺的"删除传播" |
| 已安装根证书/中间证书 | MDM 或用户确认安装 | 在设置中的证书信任列表可见 | 卸载后信任链改变；但**已建立的 TLS 会话与缓存凭据的失效时机未披露** |
| Wi-Fi/VPN 配置与凭据 | MDM | 部分字段对用户不透明 | 随 profile 移除 |
| 受管 App 与 Books/媒体 | MDM | 可被强制安装 | 随注册类型与配置而定 |
| 设备证明证书 | 由 Apple Enterprise Attestation Root CA 签发（ADE 流程） | 用户侧不可见 | 属设备身份元数据，见下段 |

三个可引用的结构性事实构成 MDM 数据面的边界。**其一**，注册描述文件明确授权服务端"send commands and configurations to the device and to **query certain aspects of the device**"——苹果用的是授权式表述，未在该处列出可查询字段清单；命令级清单（设备信息、安全状态、描述文件列表、应用列表、证书列表、设备位置查询、配件信息、日志上传、可用系统更新等类别）与各自返回字段，须以 developer.apple.com 的 MDM 协议参考逐条核对后再落档，本报告的证据基线内未包含该页正文。**其二**，注册形态决定数据分离程度：User Enrollment 面向用户自有设备、需 Managed Apple Account、以加密方式隔离受管数据；Device Enrollment 与 Automated Device Enrollment（ADE）具备更大权限集（含可阻止用户移除 enrollment profile、抹除设备）；iOS 17 / iPadOS 17 / macOS 14 / visionOS 1.1 起账户驱动的 Device Enrollment 也有类似的数据分离机制。**其三**，设备证明环节存在标识暴露面：ADE 流程中设备向 Apple 的证明服务器索取证书，签发方为 Apple Enterprise Attestation Root CA，叶子证书按设备类型包含特定属性，属性值仅在证明服务器能验证时包含——但 **freshness code 不做验证即包含**。治理上应把 ADE 设备视为具有独立可追踪标识的资产，其行应进入 17.2 节评级中的"可聚合性"高分组。

因此，本报告对"MDM 能采集什么"的正式立场是：**授权范围宽（可下发配置、监控合规、管理更新、远程抹除/锁定，并查询设备某些方面），字段清单未在一手材料中枚举**。任何"MDM 可读短信/照片/健康"的断言在本次一手材料中均无依据；同样，任何"MDM 只能看设备型号"的断言也缺乏依据——两个方向的过度确定都是清单工作的失败。

**判定规则 R10-4**：受管设备必须作为独立资产域登记，至少含注册形态、可查询面状态（写明"按苹果授权式表述，字段未枚举"）、证书与网络配置行、以及"移除注册后是否仍留服务端记录"（后者属未披露）。

---

# 家庭、车载、可穿戴与空间计算设备数据

本章的组织论点只有一句：**一个人的数据域不再由一台设备承载**。iPhone 是账户的锚点，但家居事件发生在摄像头与 home hub 上，驾驶中的通知与媒体由车机呈现，健康与运动数据源于手表，虹膜与空间映射数据位于头戴设备，而家庭共享把这些中最敏感的一部分变成**对其他自然人可见**的数据。任何只做 iPhone 侧盘点的清单，都会在这几类上系统性低估覆盖面；附录B 为此对这些设备单列行，附录F 则把机型与系统版本依赖集中呈现。

## 11.1 HomeKit 配置数据：家、房间、配件、场景与自动化

HomeKit 的数据主体不是内容而是**结构**：谁的家、有哪些房间与配件、配件的能力与位置、场景如何编排、自动化在什么时间或状态下触发。这些字段单条无害，联合起来却是一份关于住宅布局、作息与在离场的说明书。可引用的机制事实有三条。第一，**Home data 在 iCloud 中属于标准保护下即端到端的类别**，密钥在受信设备（摘自 *iCloud data security overview* 表格，已核实）。第二，非 HomeKit Secure Video 的 IP 摄像头：本地网络内的视频与音频流经设备与摄像头各自随机生成、经 HomeKit 会话交换的密钥加密；远程访问时由 **home hub 仅做中继不解密**；显示由**独立系统进程**安全渲染，**App 无法访问或存储视频流，也不允许截屏**。第三，送往 Siri 的家居控制数据被明确最小化：提供给 Siri 的是**最少的**家居配置信息且**匿名**；送往 Siri 的音频可能包含具体配件或命令，但此类 Siri 数据不会与 home hub 上存储的配置信息关联。

| HomeKit 数据类 | 主要位置 | 云端状态（可引用） | 未披露项 | 敏感度层级（本报告） |
|---|---|---|---|---|
| 家/房间/配件元数据 | 家庭参与者的设备、home hub | 标准保护下即端到端 | 服务端副本的历史版本留存 | T2（结构可推断居住形态） |
| 场景与自动化定义 | 同上 | 随 Home data | 触发条件的日志化程度 | T2 |
| 自动化触发时间线 | 设备与 home hub | 未逐项披露 | 事件保留天数与条数上限 | T2（时间序列可直接推断是否在家） |
| 摄像头实时流（非 HSV） | 不持久化，会话态 | 不入云（中继不解密） | 会话日志 | T2/T3 |
| 参与者与邀请记录 | 家庭配置 + 账户 | 随 Home data | 被移除参与者保留哪些历史 | T3（关系图） |

清单价值最高的观察是**参与者即泄露路径**：HomeKit 的多用户模型让"谁能看到什么"由家庭邀请决定，而不是由设备的 Data Protection 类决定。任何针对个人的暴露评估都必须把"共同居住者/受邀者可见"作为一个独立威胁列，它不依赖任何攻击能力，且第 17 章在矩阵中单列了这一维度。

## 11.2 HomeKit Secure Video：剪辑、poster 与人脸分类数据

HSV 是全篇少数具备**完整可引用加密链路**的数据类。按《PSG》（26.5 版）§HomeKit（pp.219–224）：运动检测触发后，剪辑经 home hub 与摄像头之间的专用本地连接发送，该连接使用在 HomeKit 会话上协商的 **per-session HKDF-SHA-512 派生密钥对**；home hub **解密**音视频并在**本地**做事件分析；检出重要事件后，用随机生成的 **AES-256 密钥 + AES-256-GCM** 加密视频剪辑，每个剪辑的 poster frame 使用同一 AES-256 密钥加密；加密后的 poster 与音视频上传至 iCloud 服务器，**相关元数据（含加密密钥）通过 CloudKit 以 iCloud 端到端加密上传**；**人脸分类数据**（人名、用于分类的人脸图像，可来自用户照片库或既往摄像头分析）以 iCloud 端到端加密存于 CloudKit，分析会话把识别信息写入前述剪辑元数据；Home App 观看时从 iCloud 下载，密钥在设备本地解包，每个剪辑会话可再分子段，**每子段使用各自唯一的加密密钥**。

这一链路在清单上有三个非显然的后果。第一，**人脸分类是双向数据流**：照片库中的人脸索引被输送给家居事件分析，同时识别结果被写入剪辑元数据——这意味着"照片库的敏感等级"会外溢到"摄像头记录的敏感等级"，两行必须交叉引用而不是各自独立定级。第二，**分析发生在 home hub 明文态**：剪辑在 home hub 上先解密再分析再加密，因此 home hub 本身是该链路的可信边界；治理上应把 home hub 按"承载明文的设备"登记，其丢失/物理访问后果等同于一个已解锁设备（见 17.1）。第三，**保留策略不在《PSG》内**：录像保存时长与事件数量上限《PSG》未披露；iCloud 用户指南页 *Store encrypted security camera footage in iCloud with HomeKit Secure Video* 的检索摘要称其为"全部端到端加密，且视频不计入 iCloud 存储容量"，而"10 天"这一具体数字本次只出现在第三方与 Discussions 材料中。本报告的正式表述限定为：**按 iCloud 用户指南所述，HSV 视频端到端加密且不计入 iCloud 容量；具体保存天数与套餐差异未在本次苹果一手来源中确认**。

## 11.3 CarPlay 与车辆派生数据

车辆是数据域中最难盘点的一环，因为它同时是**显示器**（把 iPhone 的类别投影到车机）、**输入器**（麦克风、触屏、物理按键）与**独立记录者**（车厂自己的远程信息处理系统）。《PSG》（26.5 版）**未以"数据类别清单"形式描述** CarPlay 产生哪些数据，本章因此只写跨设备链路上被文档化的机制项。

| 已被文档化的 CarPlay 事实 | 出处口径 | 清单含义 |
|---|---|---|
| 有线 CarPlay 连接后，设备在一定时间内免密码解锁（原文："wired to CarPlay won't need to enter their passcodes every time they attach their device"） | 《PSG》（26.5 版） | 这是一个**保护档切换事件**：车内连接期间 Class A/B 类密钥可能被留在内存，形成物理访问窗口 |
| CarPlay 视频流与 AirPlay 音频使用 MFi-secure association protocol | 《PSG》（26.5 版） | 链路机密性由配件认证体系保证；数据可读范围随配对配件扩展 |
| CarPlay 作为词汇表主题条目存在 | 《PSG》（26.5 版） | 仅证明其被纳入安全架构叙述，不提供数据面细节 |

由此必须写出本报告的核心负向结论：**CarPlay 所传输与缓存的数据类别（联系人、消息通知、音乐、导航请求、通话与语音助手交互）无逐项披露**。清单的可行做法是"按投影登记"——把 iPhone 上已存在的类别，凡可能经由 CarPlay 界面呈现者，加一列"车机侧临时副本/渲染缓存"，状态写「公开资料未逐项披露」，风险假定写"车机侧不受 iOS Data Protection 类约束"。第二个必须单列的是**车辆侧独立采集**：行车轨迹、驾驶行为评分、车内音频等若存在，其数据控制者是车厂或运营商而非 Apple，属本报告范围之外的第三方域，但资产盘点时必须以"关联控制者"形式登记，否则会出现"iPhone 侧已删除、车厂侧仍在留存"的典型盲区。

## 11.4 Apple Watch：健康的原始源与配对关系

Apple Watch 在本清单中的定位是**健康与健身数据的原始产生源**加一个**具有自主通信能力的伴生终端**。第 6 章处理了健康数据的存储与授权；本章只登记 Watch 侧特有的四件事。

第一，配对与恢复关系可跨还原保留：《PSG》（26.5 版）载明 **iCloud 备份还原到同一 iPhone 可保留既有 Watch 配对**；同时存在 §Apple Watch unlock using iPhone 所述的"用 iPhone 解锁 Apple Watch"能力，以及 Watch 与 iPhone 均支持的"多次失败后自动抹除"（该条款亦覆盖 Vision Pro）。这两条合起来意味着：**Watch 的数据命运绑定在 iPhone 的备份与配对状态上**，清单里不应把 Watch 当作独立所有者，而应记为 iPhone 资产行的从属行。第二，中继通道的加密状态：Watch 的通知、通话与短信经 APNs 中继且相关通知"使用相同的端到端加密"（《PSG》Continuity 章节）。第三，无线密钥的云端档位：**W1 and H1 Bluetooth keys** 列在标准保护下即端到端的集合中（102651 表格）。第四，运动期间锁屏可用性：第 6.2 与 6.7 节已引用《PSG》§HealthKit 的 `HKWorkoutSession` 条款——运动期间锁屏下可持续访问健康数据，可在 Settings > Face ID/Touch ID & Passcode > Workout Health Data 管理。

关于跌倒检测（Fall Detection）与车祸检测（Crash Detection）这类**事件信号**：本次证据基线内未定位到《PSG》对这类信号产生何种服务端记录的披露；这类信号会产生紧急呼叫、短信与联系人通知，因此其数据外溢对象是**应急服务与紧急联系人**，属"经第三方通道传递的敏感状态数据"，须以「公开资料未逐项披露」登记并保留其风险假定。蜂窝网络的地址隐私 likewise：Wi-Fi 地址随机化/私有地址策略在 Watch 上的具体条款本次未加载，写作「本次证据基线未包含该条款，不据此断言其行为」。

治理上，Watch 最容易被漏记的一行是**健康共享的接收方**：第 6 节处理过的 Health Sharing（iOS 15+，双方均需双重认证，两人之间端到端 iCloud 加密，Apple 无法访问）与 Share with Provider（仅对用户所选医疗机构可用，端到端加密，Apple 不保管也无法访问密钥）都把"另一个人或一家机构"变成数据的合法持有人。清单必须为每一份共享关系建立一行，记录接收方、开始时间、可撤销性与撤销后的服务端效果（后者未披露）。

## 11.5 AirPods、Apple TV 与家庭共享中的儿童账户

《PSG》（26.5 版）**无 AirPods 专章**。可引用的只有间接项：W1/H1 蓝牙密钥的端到端档位，以及 Continuity 章节中"经 BLE 4.2 配对 + 用 APNs 建立初始连接、部分场景以 APNs 替代 BLE 传输 activity payload"。AirPods 相关的音频、降噪与听力健康数据类别，属「公开资料未披露」；本报告不据第三方评测推断。真正值得进入清单的是"耳机作为麦克风阵列"这一属性：它可能承载 Siri 唤醒音频与（在支持机型上）听力辅助数据，而其存储与处理归属 iPhone 侧的 Siri/健康行，因此登记方式是"在 iPhone 的对应行加注输入源=配件"，而不是新建一个孤立类别。

Apple TV 的可引用事实集中在两处：设备数据全量加密这一通用条款（Class D 段落点名 iPhone/iPad/Apple Vision Pro，tvOS 亦在《PSG》的版本线覆盖内），以及 §HomeKit 中对 Siri-enabled 配件与遥控链路的描述——音频帧经专用本地连接（同 per-session HKDF-SHA-512 密钥）送往 HomePod/Apple TV，Apple TV 解密后转交 Siri App，按对所有 Siri 音频输入的相同隐私保护处理；Apple TV **不访问除音频流以外的 Siri 请求数据、不在其上存储用户数据**。清单价值在于它给出一个**明确负例**：某些伴生设备被文档化为"不存储用户数据"，这与"未披露是否存储"是两种不同的登记状态，必须区分。

**家庭共享（Family Sharing）与儿童账户**是本报告中唯一"法定式差异"的设备生态议题：同一类数据在成人账户与儿童账户下的授权、可见性与可用安全功能并不相同。可引用的是三条。其一，功能可用性差异：按《PSG》与支持文档口径，**Managed Apple Accounts 与儿童账户（因地区而异）不支持 Advanced Data Protection**——这意味着受儿童账户约束的设备，其 iCloud 数据的密钥归属结构性地停留在标准保护档。其二，跨成员身份验证依赖：《PSG》在 Wallet/Apple Cash 相关段落述及 **Apple Cash 需 iCloud 家庭内已验证身份**。其三，可见性差异：第 9.5 节已把"屏幕使用时间在家庭共享/儿童场景下对其他家庭成员可见"列为该类最大暴露面。至于家长同意流程的具体数据字段、请求支持时传递的内容，本次未加载对应条款，按「按苹果公开页面所述，具体字段未逐项披露」处理。清单规则因此是：**凡资产行涉及家庭共享参与者，其"是否离开设备"不能只判"是否到 iCloud"，还须判"是否到另一个自然人的设备"**——后者绕过了所有加密强度讨论。

## 11.6 空间计算设备与多设备盘点规则

visionOS 设备在本报告中的位置是"生物特征 + 空间映射"两类高敏感数据的唯一载体，而其数据面披露的稀疏程度恰好与敏感度成反比。可引用的事实：Optic ID 的眼部数学表征在 Secure Enclave 内处理，设备保存的是加密后的表征，眼睛影像在算出数学表征后即被丢弃（《PSG》（26.5 版）§Optic ID, Face ID, and Touch ID 章节；本次抓取到章节位置与关键句，未整段摘录，Optic ID 段落按部分核实处理），误匹配率低于 1,000,000 分之一，且该章节与 §Face ID 的表述同构地包含"不入备份"；每次执行后重置 Secure Neural Engine 状态以保持 Optic/Face/Touch ID 数据安全；外部存储路径（如外接显示器）不得承载生物特征数据；设备全部数据皆处于加密存储（Class D 段落点名 Apple Vision Pro）；Vision Pro 参与 Apple Pay、Lost Mode/Find My、设备注册与解锁；其扩展机制与 iOS/iPadOS/macOS 共享（Extensions 沙箱、与容器 App 共享隐私授权等）。

不可引用但必须登记的是**空间与身体派生数据**：房间几何的空间映射（mesh/平面/锚点）、手部追踪与眼动追踪所产生的时序数据、以及用于虚拟形象与姿态的个性化表征。本次未在已加载文本中定位到对应披露，Persona/空间捕捉/眼动与手部追踪数据一律按「公开资料未披露」写。这里给出一条保守但有依据的定级理由：第 7 章已确认生物特征表征被文档化为"不入备份、不离开设备"，而空间/身体派生数据在结构上是**同一类可辨识生理信号的连续观测序列**，其可辨识性来自时间维度而非单帧，因此本报告按 T3 登记并显式标注"定级依据为分析性推断，非苹果声明"。多设备清单的收尾规则如下。

| 规则 | 内容 | 对 iPhone 侧清单的影响 |
|---|---|---|
| M-1 | 以"人"而非"设备"为盘点主键，账户为连接键 | 同一类别需按设备重复登记，且取**最弱保护档**作为该类别的域级档位 |
| M-2 | 区分三类伴生设备 | "只中继不存储"（部分 CarPlay/Apple TV 条款）、"存储但档位未披露"（Watch/AirPods 部分场景）、"存储且档位明确"（home hub 明文态、Vision Pro 全量加密） |
| M-3 | 配对关系是数据资产 | 备份还原可保留 Watch 配对；配对丢失/重配对影响历史可读性，须在删除语义列记录 |
| M-4 | 家庭成员是持有者 | 家庭共享、Health Sharing、家居邀请都构成"合法的第三方持有"，其暴露评估独立于加密强度 |
| M-5 | 空间/生理连续信号按最高档假定 | 未披露不等于低敏感；本报告中所有 visionOS 派生行按 T3 且注明推断依据 |

---

# 支付、钱包与金融数据

支付数据是"令牌化改变类别归属"的教科案例：同一个动作（刷卡）产生的信息被切成三段，分别落在用户的 Secure Element、商户的收单记录与发卡方的账户流水中，三段的敏感级与法定身份完全不同。本章的写法是先把机制钉住，再回答"谁最终持有什么"。

## 12.1 令牌化架构与设备账号的所在

《PSG》（26.5 版）§Apple Pay（pp.151 起）给出的核心机制可逐句引用：**Device Account Number 由发卡方创建，加密发送给 Apple，随后存于 Secure Element；这个唯一的 Device Account Number 的加密方式使 Apple 无法访问它**（原文："Device Account Number is created by the card issuer, sent encrypted to Apple, and then stored in the Secure Element. This unique Device Account Number is encrypted in such a way that Apple can't access it."）。设备账号与卡片卡号不同，不用于磁条、电话或网站场景的替代。最要紧的一句是：**"The Device Account Number in the Secure Element is never stored on Apple Pay servers or backed up to iCloud."**

| 环节 | 可引用事实 | 清单结论 |
|---|---|---|
| 开通 | 验证用户身份后，DAN 与 unique security code 由发卡方提供并写入 Secure Element；卡片配置数据经 Apple 服务器以证书链验证，密钥生成于 Secure Enclave 并可被 Apple Pay server 验证 | 发卡方是 DAN 的创建者与权威记录者；Apple 位于传输与验证链上而非保管位 |
| 存放 | Secure Element 为 Java Card 平台，EMVCo 认证、Common Criteria 认证 | 该类数据的存储基质与其他一切类别不同：不在文件系统，因而**不受 Data Protection 类管辖**，也不受备份/同步路径影响 |
| 使用 | 交易时与 DAN 一起计算 one-time code（一次性密文）；授权靠 Face ID/Touch ID 或 macOS 密码 + 靠近读卡器；事务中有"确认用户意图"机制 | 每一次支付产生一个不可复用的凭证，重放与聚合能力被结构性削弱 |
| 应用侧 | App 内支付只得到"认证成功与否"，无法访问生物特征数据；App 可查询用户是否有可用于 Apple Pay 的信用卡/借记卡（PassKit 能力查询） | "是否有可用卡"本身是一个可泄露的布尔属性，应作为独立元数据行登记 |

## 12.2 三方各见什么：商户、平台、发卡方

苹果在《PSG》中的表述以否定式为主，这是本章措辞的硬约束。可引用的是：**Apple 不收集可回溯到用户的交易信息**，原文为 "…collect any transaction information that be tied back to the user. **Payment transactions are between the user, the merchant, and the card issuer.**"（撰写口径：这是"Apple 不会……"的否定式表述，不可写成肯定式）。由此三方视图如下。

| 参与方 | 正向可见（可引用） | 明确不可见（可引用） | 未逐项披露 |
|---|---|---|---|
| 商户/收单侧 | 经 DAN 与一次性密文构成的支付凭证、交易金额与时间、商户自身订单信息 | 真实卡号（令牌化的定义性后果） | 商户是否收到姓名、账单地址、设备相关标识——苹果以否定式表述为主，**未给出逐项字段表**，本报告写作「公开资料未逐项披露」 |
| Apple | 设备侧的配置与验证过程、App Store/订阅与购买历史（与 Apple Account 关联） | DAN 明文与可回溯到用户的支付交易信息 | Apple Pay 相关元数据与 DAN 的关联方式在《PSG》中有专门段落，但其服务端留存时长未披露 |
| 发卡方 | 账户与授权关系、DAN 的生命周期、交易流水 | 设备内其他任何数据 | 发卡方风控所需字段清单，属发卡方隐私声明范畴，不在本报告证据基线 |

对清单的实际后果是：**"支付数据"在 iOS 资产表上必须拆成至少三行**——支付凭证（Secure Element，不入备份、不可同步）、支付结果元数据（本机 Wallet 记录，可见性与保护档随 Wallet 行）、账户侧购买历史（Apple Account 关联，账户侧长期可复原）。把三者合成一行"支付信息"会让 17 章的评估失去分辨力。

## 12.3 Wallet 通行证与证件类凭证

Wallet 通行证（登机牌、电影票、优惠券、会员卡、门禁与交通卡类）在保护档位上有一个明确锚点：**Wallet passes 被列为 Advanced Data Protection 新增端到端加密类别之一**（摘自 *iCloud data security overview* 表格，已核实）。这意味着标准保护档下的 Wallet 通行证副本处于"in transit & on server / 密钥在 Apple"这一族，而 ADP 开启后其密钥移入受信设备域——这是清单里少数可以写出"开启某开关即可改变密钥归属"的行之一。

证件类凭证（驾照与各州身份证件加入 Wallet）必须用限定语处理：其可用性由**当地法规、地区与机型**限定，本次证据基线内未加载对应条款正文。报告写作口径为「按苹果公开资料所述，证件类通行证的可用性依地区与机型而定；本次未逐项核对」。从资产视角，证件类凭证值得单列的理由有三条：其字段是一次性给出全量身份属性（姓名、照片、地址、出生日期、证件号、身体属性），其使用模式通常是**呈现（presentation）**因而涉及第三方观察者，其法定身份使其在 18 章的合规映射中落在最高一档。三条都指向同一个结论：证件类凭证是"数据最小化"原则在 iOS 上最可验证的用例——呈现时是否只暴露所选字段，是评估该类资产时的第一个问题，具体字段选择机制本次未加载原文，按未披露登记。

## 12.4 Apple Card、交易记录与金融类留存

在云端档位上，**Apple Card transactions 与 Payment information 都在"标准保护下即端到端"的集合中**（102651 表格，已核实）。这一条在本报告中的作用是把"金融类同步数据"归入密钥在受信设备的一族，与 Mail/Contacts/Calendars 那族形成对照。金融数据的其他留存维度——交易在苹果侧、发卡方侧与商户侧各自的保留期限——属公开资料未逐项披露；本报告只保留一条有依据的推断方向：既然 Apple 称不收集可回溯到用户的交易信息，则"交易记录的法定持有者"主要是发卡方与商户，而非平台，这一分配直接影响第 18 章删除请求应指向谁。

分期与融资类产品（如账单分期、先买后付形态）在本章只作为**类别占位**出现：其具体条款、留存与征信交互本次未加载苹果一手页面，一律按「公开资料未披露」写。清单上应登记的不是其机制而是其后果：金融类数据的每一次呈现（在 Wallet 中查看、通过 Share 导出收据、把交易截图发进聊天）都产生一份**脱离原密钥域**的副本，且该副本会沿第 11 章与第 16 章的路径继续复制。

## 12.5 购买、退款、收据与订阅：账户侧的长记忆

支付数据中最容易被个人用户忽略的是"账户侧历史不可清除"。可引用的是 10.5 节的两条同源事实：iCloud 备份不含已购内容本体，恢复时从各商店重新下载，**部分地区不自动下载，退款或下架内容不可用**，且**完整的购买历史与 Apple Account 关联**。三者的清单含义分别是：其一，购买记录是账户级资产，不随设备抹除而消失；其二，退款/下架会让"备份恢复"这件事失去内容可得性，即备份并不是内容可得性的保证；其三，对已购内容做删除请求时，本机删除与服务端历史删除是两个动作，后者需走第 18.4 节的隐私请求流程。订阅与收据类（发票、订单确认）常以邮件、消息与截图三种形态存在于第 4 章与第 12 章的交叉处，邮件形态尤其值得注意——它同时落在 13.5 节所确认的"iCloud Mail 不支持端到端加密"这一互通性例外之中。

## 12.6 令牌化为何改变了"谁持有哪一类"

本章收束于一个概念结论。令牌化把"卡号"这一**长期有效、跨商户可关联、泄露即不可撤销**的静态标识，替换为"设备账号 + 一次性密文"这一**作用域受限、单次有效、可在设备内失效**的凭证。其资产层面的后果是三类数据的持有者重新分配：静态账号信息的主要持有者从商户收单体系退回发卡方；一次性的交易授权凭证只存在于设备与读卡器之间；平台处于验证链上而不处于保管位。这正是第 2.5 节"密钥域优先"规则的收益示例——把"是否端到端"和"密钥在谁手里"作为归因主轴，才能区分"Apple 参与交易"与"Apple 掌握交易内容"这两件事。

| 支付相关类别 | 存储基质 | 备份/同步 | 可引用档位 | 敏感度层级 |
|---|---|---|---|---|
| Device Account Number | Secure Element | 不入 Apple Pay 服务器、不入 iCloud 备份 | 加密方式使 Apple 无法访问 | T4（但泄露面极窄） |
| 一次性交易密文 | 会话态 | 不持久 | 交易时与 DAN 一起计算 | T1（单次有效） |
| Wallet 通行证 | 设备 + iCloud 通行证域 | ADP 下端到端 | Wallet passes 列于 ADP 新增端到端项 | T2/T3（证件类更高） |
| 支付信息与 Apple Card 交易（云端副本） | iCloud 服务域 | 属于端到端集合 | Payment information、Apple Card transactions 标准即端到端 | T3 |
| 购买历史与收据 | Apple Account 侧 | 与账户关联，不随设备抹除 | 《PSG》备份条款 | T2（聚合后可推断消费模式） |
| App 内支付能力查询结果 | 商户/开发者侧 | 由第三方决定 | PassKit 可查询是否有可用卡 | T1/T2（布尔值但可用于指纹化） |

**判定规则 R12-1**：凡涉及支付、钱包与金融的资产行，除四问之外必须额外回答"该类别是静态标识还是单次凭证"，因为可撤销性不同的两类在 17.2 节的影响评级中应取不同的可恢复性系数。

---

# 云端落点、备份与同步

本章是全报告的重心。理由是：**本机保护强度决定"设备丢失"的后果，云端落点决定"账户被入侵"与"密钥域转移"的后果**，而后者对绝大多数用户是更长期的暴露面——一次备份会在云端停留数月，一次同步会在多台设备上复制，一个共享链接会把内容交给没有账户的读者。本章先把内容矩阵钉住，再把密钥机制钉住，最后给出"数据离开设备"的可操作定义。全章的引用口径已在 1.3 节声明：《PSG》条款一律写作"《Apple Platform Security》（26.5 版）"。

## 13.1 iCloud 的三档密钥归属：把"同步"拆成三种不同风险

*iCloud data security overview*（支持页 102651）的表格给出一个可直接当作清单模板的三分结构：类别 × 加密与密钥存储位置 × 是否受 ADP 影响。三档归属如下。

| 密钥归属档 | 表格口径 | 覆盖类别（逐项摘自该表） | 风险后果 |
|---|---|---|---|
| 一、标准保护下即端到端，密钥在受信设备 | In transit & on server，但密钥仅在受信设备 | Passwords and Keychain、Health data、Journal data、Home data、Messages in iCloud、Payment information、Apple Card transactions、Maps、QuickType Keyboard learned vocabulary、Safari、Screen Time、Siri information、Wi-Fi passwords、W1 and H1 Bluetooth keys、Memoji | 服务端副本对 Apple 不可读；账户被入侵不直接等于内容泄露，但受信设备与恢复材料成为新的关键资产 |
| 二、非端到端，密钥在 Apple | 原文列值"In transit & on server / Apple" | iCloud Mail、Contacts、Calendars | 服务端可读；政府/法务请求、账户劫持、内部访问均构成有效暴露面 |
| 三、默认非端到端、开启 ADP 后端到端 | ADP 新增端到端项 | iCloud Backup（含设备与 Messages 备份）、iCloud Drive、Photos、Notes、Reminders、Safari Bookmarks、Shortcuts、Voice Memos、Wallet passes、Freeform、Apple Invites（附条件） | 存在一个明确的可执行控制：开启 ADP 即迁移密钥域；未开启时按第二档评估 |

需要指出的是第 9.4 与 11.1 节已把第一档中若干"看起来不像内容"的类别（键盘词典、屏幕使用时间、家居数据）按高敏感登记，本节补上其结构性理由：**第一档是"Apple 无法读"的类别集合，第三档是"取决于用户是否做了那一步"的类别集合**。清单里把第二、第三档混写成"已同步"是最常见的错误，因为二者的账户入侵后果完全不同。

## 13.2 iCloud 备份内容矩阵与文档化排除项

《PSG》（26.5 版）§iCloud Backup security 的原文把备份内容界定为：**device settings, app data, photos, and videos in the Camera Roll, and conversations in the Messages app**；触发条件是每日经 Wi-Fi，且设备**已上锁 + 接电源 + 有 Wi-Fi**。同一文档与支持页 *If you're missing information after you restore your iPhone or iPad* 共同给出下列纳入/排除矩阵。

| 数据类 | 是否进入 iCloud 备份 | 依据与说明 |
|---|---|---|
| 设备设置 | 是 | 《PSG》原文"device settings" |
| 第三方 App 数据 | 是（按"app data"整体纳入） | 苹果未在该条款内细分子类；细项属公开资料未逐项披露 |
| Camera Roll 中的照片与视频 | 是（但见下一行） | 《PSG》原文表述 |
| 开启 iCloud Photos 后的照片与视频 | 否 | 原文理由：照片与视频已在 iCloud 中；原文以 **iOS 8.1 / iPadOS 13.1 / OS X 10.10.3** 为版本界 |
| 已购内容本体（音乐、影片、App 等） | 否 | 只含已购内容的相关信息；恢复时从 iTunes Store/App Store/Apple TV/Books **重新下载**；部分地区不自动下载；退款或下架内容不可用 |
| Messages 会话 | 视开关 | 《PSG》把"conversations in the Messages app"列为备份内容；支持页另载：**已开启 Messages for iCloud 时消息内容不在备份中**（因已在 iCloud）；两种口径的适用条件不同，须按用户开关组合判定 |
| Voice Memos | 视开关 | 支持页原文：已开启对应 iCloud 功能时"aren't included in your backup" |
| 备忘录 | 本地存储的备忘录**会被包含** | 支持页原文 |
| iCloud Mail | **不在备份内**，但**邮件账户设置**在 | 支持页原文理由：邮件本就在 iCloud 中 |
| 本地钥匙串 | 是，但被密封 | 用由设备 Secure Enclave **UID 根密钥**派生的密钥加密；该密钥设备独有、Apple 不知晓 → **只能还原到同一台设备**，任何人（包括 Apple）都无法读取 |
| Face ID / Touch ID / Optic ID 数据 | 永不 | 《PSG》原文含数学表征与采集影像，明确排除于备份 |
| Apple Pay Device Account Number | 永不 | 原文"never stored on Apple Pay servers or backed up to iCloud" |

其余"不含项"（如健康数据、App 隐藏状态等）本次未逐条抓取该页全部条目，本报告**不自造清单**：读者应以支持页 102325 正文为唯一来源复核。这里给出一条对治理有用的读法——**备份不是保险**：iCloud Photos 开启时照片不进备份而进同步域，已购内容不备份而靠重新下载，钥匙串备份只对同一台设备有意义。三件事合起来说明：备份矩阵回答的是"哪些内容能以密钥域较弱的方式被恢复"，而不是"哪些内容丢了能找回"。

## 13.3 iCloud 备份的 keybag 机制与恢复流程

这是本章技术上最关键的一节。按《PSG》（26.5 版）描述：处于**锁定时不可访问**的 Data Protection 类的文件，其 per-file key 被用 **iCloud Backup keybag**（Curve25519 非对称密钥）包裹，然后按原始密文上传。该 keybag 由一个随机密钥保护，该随机密钥与备份集共同存放；**不使用 iCloud 口令做加密**，因此**修改 iCloud 口令不会使旧备份失效**。备份密钥默认存于 Apple 数据中心的 HSM 中，属于 **available-after-authentication** 类；开启 ADP 后 iCloud Backup service key 转为端到端加密。恢复流程亦为原文可引用：取回文件 + iCloud Backup keybag + keybag 密钥 → 解开 per-file key → 作为**新文件**写回文件系统，并按其各自的 Data Protection 类重新加密。

| 环节 | 机制（可引用） | 清单后果 |
|---|---|---|
| 密钥来源 | iCloud Backup keybag，Curve25519 | 与本机 user keybag 分属不同密钥域；备份的可解密性不依赖设备 UID |
| keybag 保护 | 随机密钥，与备份集同存 | 拿到备份集与 keybag 密钥即可解密；"同一份文件、两个密钥域" |
| 口令关系 | 不用于加密；改口令不使旧备份失效 | **口令轮换不是备份撤销手段**——这是全篇最常被误解的一条 |
| 服务端保管 | 默认在 Apple HSM，available-after-authentication | 认证后的账户访问即构成解密能力的来源（未开 ADP 时） |
| ADP 影响 | iCloud Backup service key 端到端 | 密钥域从 HSM 迁移到受信设备/iCloud Keychain 保护域 |
| 恢复效果 | 文件作为新文件写回并按类重新加密 | 恢复到新设备时保护档按目标设备的类体系重建；escrow 与口令状态改变会影响可读性 |

由此得出的两条硬结论。**第一**，把"备份已加密"写成控制措施是不够的，必须写成"备份密钥由谁在何条件下可用"；在标准档下，该条件是"完成账户认证"。**第二**，备份一旦形成即具有**密钥独立性**：本机删除高保护类文件、修改口令、甚至注销设备管理都不会使已上传的备份不可读；能改变这件事的只有删除备份本身与开启 ADP（后者伴随密钥轮换，见 13.4）。这一"删除语义不对称"是第 16 章残留分析的入口。

## 13.4 Advanced Data Protection：端到端清单与两处口径冲突

ADP 的覆盖清单已在 13.1 第三档列出，此处补全其前提与密钥事件语义。**前提条件**（支持页 *How to turn on Advanced Data Protection for iCloud* 与《PSG》一致）：Apple Account 需双重认证 + 设备密码；设备系统为 iOS 16.2 / iPadOS 16.2 / macOS 13.1 / tvOS 16.2 / watchOS 9.2 及以后；至少一个恢复联系人或恢复密钥；恢复失败时 **Apple 无法帮助恢复端到端加密数据**。**开启时的密钥事件**：HSM 内 available-after-authentication 密钥的删除是"immediate, permanent, and irrevocable"，随后异步逐服务轮换密钥，失败会重试；关闭 ADP 时设备把服务密钥上传 HSM，但**从不包含标准保护下即端到端的类别**（如 iCloud Keychain 与 Health）。开启 ADP 时 **iCloud Backup service key 与 Messages in iCloud container key 均被轮换**。

**必须写明的版本口径冲突**：*iCloud data security overview*（102651，核验时点 2026-10-18）表述为"标准保护下已有 **15** 个类别端到端加密，开启 ADP 后升至 **25** 个类别"；而《Apple Platform Security》（26.5 版）表述为"**从 14 增至 23**"。二者不可混用。本报告采用支持页 102651 为主口径并注明核验日期，同时保留《PSG》口径的差异记录；读者在制作合规文档时应写"苹果在不同文档中给出 25 / 23 项口径"，而不是任选其一当作权威数字。数量差异本身不是错误，但它说明**类别计数是会随版本漂移的次要信息，逐项清单才是可靠信息**——本报告全篇以逐项清单为准。

ADP 的可用性还有两类结构性限制：**Managed Apple Accounts 与儿童账户（因地区而异）不支持 ADP**；以及 **Apple Intelligence / Private Cloud Compute 相关的数据外发语义不在本次证据基线内**——《PSG》（26.5 版）全文检索"Apple Intelligence"无命中，故本报告不以《PSG》描述其边界，相关部分按「公开资料未披露 / 须以苹果专项页面为准」处理（参见 1.6 节）。

## 13.5 ADP 明确不覆盖的对象：互通性、协作与元数据

两处来源（102651 与《PSG》§Advanced Data Protection）一致给出下列排除项，它们共同构成 ADP 的真实边界。

| 不覆盖对象 | 原因（可引用） | 治理处理 |
|---|---|---|
| iCloud Mail、Contacts、Calendars | 需与全球邮件/通讯录/日历系统互通（CalDAV/CardDAV；S/MIME 为可选替代） | 这三类永远按"密钥在 Apple"评估；账户入侵后果最重的一族 |
| Reminders 经 CalDAV 同步的部分 | 同上互通性 | 与 Reminders 的 ADP 端到端行分开登记 |
| Keynote/Numbers/Pages 实时协作 | 需服务端中介 | 协作会话按"服务端可读"假定 |
| Photos 共享相册（Shared Albums） | 可经公开网页分享 | 与 iCloud Photos 主库（ADP 覆盖）严格区分 |
| 选择 "anyone with a link" 的分享 | 内容以标准数据保护交付给 Apple 服务器；**每次分享操作的标题与代表缩略图以标准数据保护存储** | 链接分享是一次明确的密钥域降级事件，且元数据独立留存 |
| Freeform "Send a Copy" | 链接有效期内以**未加密状态**存放（与账户加密设置无关） | 唯一被明文承认的"链接期内不加密"场景，须按最高风险窗口登记 |
| Apple Invites | 发布后多数数据转标准保护；仅当主持人及全部参与者开启 ADP 才延续端到端；**时间/日期、城市级位置、海报图发布后始终以标准保护** | "全员端到端"在现实协作中不可保证；按最弱参与者定级 |
| CloudKit 字段 | 需开发者显式声明为 encrypted 才受保护 | 第三方 App 云数据不得默认按端到端登记（见 13.6） |
| 元数据（修改日期/时间用于排序、文件与照片校验和用于去重，即收敛加密） | 不端到端；虽加密但密钥由 Apple 以标准保护保管；苹果称**承诺让更多此类元数据端到端加密** | 元数据行独立于内容行，按第二/第三档处理 |
| iCloud.com 网页访问 | 开启 ADP 后默认关闭 Web 访问；授权后设备"武装"**1 小时**，仅接受针对 iCloud.com 可见服务 allow-list 的密钥上传，每次上传都通知用户；**Health 与 iCloud Keychain 密码永不可上传** | 网页端是一次有意识、有时限的密钥域降级；1 小时窗口应被登记为风险时段 |

这份表的意义在于把 ADP 从"更强的加密"变成一个**有明确孔洞的覆盖图**：互通性服务、协作中介、链接分享、开发者未声明的字段、排序与去重所需元数据、Web 访问窗口。任何把 ADP 写成"全量端到端"的文档都是错误的。

## 13.6 其余离机路径与"数据离开设备"的定义

**CloudKit 与第三方 App 云数据**。CloudKit 是 App 侧云存储与同步的机制；其端到端与否取决于开发者是否为字段声明 encrypted（13.5 已述）。修改日期/时间与校验和不受端到端保护。此外，本次证据基线内包含两条与 CloudKit 相关的缺陷登记（iOS 27 安全内容文档：**CloudKit — CVE-2026-86895**，"A local app may be able to read a persistent account identifier"；另有 CloudKit 条目"An app may be able to read device name"），说明"App 云数据里的标识与设备名"应作为独立资产行。

**共享云库（shared cloud libraries）**：iCloud 共享类功能的共同特征是"合法的多持有者"。可引用的有：Health Sharing 两人之间端到端 iCloud 加密、Apple 无法访问；Share with Provider 仅对用户所选医疗机构可用、端到端、Apple 不保管也无法访问密钥；Photos 共享相册不支持 ADP；Apple Invites 取决于全员 ADP。清单上"共享"与"同步"必须分列——共享的接收方是**另一个账户主体**，其副本脱离本清单的控制范围。

**Messages in iCloud 的删除与密钥语义**：该功能保持全部消息历史在各设备可用。标准保护下，**仅当 iCloud 备份关闭时**消息才是端到端加密；开启 iCloud 备份时，备份中包含 Messages in iCloud 加密密钥的副本（其设计目的是在丢失 iCloud Keychain 与受信设备后仍可恢复）；关闭 iCloud 备份时，设备生成新密钥保护后续消息，该新密钥**只存于 iCloud Keychain**、仅受信设备可用、新数据无法用旧容器密钥解密——而**旧容器密钥仍留存，故旧内容属可被 Apple 读取的历史**。ADP 下 Messages in iCloud 始终端到端，且开启 ADP 会轮换 container key。这段是全报告中"删除语义"最微妙的条目：用户关掉备份不会让已上传的历史变私密，只会让**之后的**消息变私密。

**iCloud Private Relay**：其定位是网络层隐私（隐藏 IP、分离域名与 IP 的处理者），不是数据类别。可引用的条款只有一条与本报告直接相关：《PSG》（26.5 版）Private Relay 的启用条件之一是 "The device isn't in Lockdown Mode."——即更强的锁定态与网络层隐私之间存在互斥。关于其"不保留日志/不记录浏览"的具体承诺表述，本次未加载该专项页面正文，故按「按苹果公开页面所述，本报告未逐条核对」处理。清单上 Private Relay 的登记项是"网络元数据暴露面"而非"内容类别"，其效果与 iMessage 的"时间戳与 APNs 路由信息不加密"（第 4.6 节）属于同一问题的两侧。

**设备到设备迁移与本地加密备份**：迁移（由旧设备向新设备传输）与经 Finder/iTunes 的本地备份都是"离机但可能不经 Apple 服务器"的路径。可引用的只有两条：《PSG》明确 iCloud 备份还原到同一 iPhone 可保留既有 Apple Watch 配对；健康数据的本地备份规则为——**用 Finder/iTunes 备份时，仅当备份被加密才包含健康数据**。迁移过程的传输细节与密钥协议本次未加载原文，按「公开资料未披露」登记，风险假定写"迁移期间的接收设备获得与源设备等价的读取能力"。

以下小节给出本章的可操作定义。**"数据离开设备"在本报告中按密钥域判定，而不是按字节位置判定**，共六种命题，每条有独立的证据要求。

| 离机路径 | 密钥在谁手里（标准档） | 开启 ADP 后 | "已离开"的判定标准 |
|---|---|---|---|
| 同步（第一档类别） | 受信设备 | 不变 | 内容已离开，但接收方限于持有账户设备的主体；Apple 为密文保管者 |
| 同步（第二档：Mail/Contacts/Calendars） | Apple | 不变（互通性例外） | 服务端可读，账户认证即可获取；法务请求与账户劫持均构成有效暴露 |
| 同步（第三档：Photos/Notes/iCloud Drive 等） | Apple | 迁移至受信设备 | 未开启前按第二档判定；开启后按第一档判定 |
| iCloud 备份 | Apple HSM（available-after-authentication），不经 iCloud 口令 | iCloud Backup service key 端到端 | 一旦上传即完成密钥域转移；改口令与本机删除都不撤销 |
| 共享/链接/协作 | 视功能，常为标准保护 | 部分仍为标准保护（见 13.5） | 出现"另一个主体可读"即判定离开，与加密档无关 |
| 诊断/遥测、MDM 通道、伴生设备链路 | Apple / 企业管理员 / 配件与车机侧 | ADP 不改变 | 按接收方类别判定；MDM 属"由第三方主动拉取"，见 10.6 |
| 法务与取证获取 | 依请求类别 | 端到端类别不可应求解密（Apple 无法访问） | 只要服务端可读即可能经合法程序离开，见 18.5 透明度报告口径 |

**判定规则 R13-1**：任何资产行的"是否离开设备"必须写成"经哪条路径 + 密钥在谁手里 + 该状态是否随 ADP 开关改变"三元组；三者缺一即视为该行盘点未完成。这条规则同时给出全章的实践结论：**对一个已开启 iCloud 备份而未开启 ADP 的普通账户，本机保护最强的那批文件（Class A/B）恰恰是在云端最容易被解开的那批。**

---

# 派生数据、推理标签与模型产物

派生数据（derived data）指由原始数据经受控或自动处理产生的第二手信息：说明文字、人物与场景标签、转写文本、摘要、向量表征（embeddings）、建议索引与推断结论。第 8.2 与 9.3 节已处理照片视觉索引与 Spotlight/Siri 建议索引的具体形态，本章不再复述其内容，而处理一个更上位的问题：**这类资产在分类学上应被放在哪一档，以及它们是否继承其输入的 protections**。这是 iOS 数据治理中最容易被"技术中立"叙述绕过的一环——派生产物常被当作缓存或性能结构，而它们在法律与风险意义上往往是**新的、更强的**个人信息。

## 14.1 派生数据的形态与可引用的档位

下表把主要派生形态按"是否有可引用的保护/留存表述"分栏。凡列入"未披露"者，本报告按第 2.2 节模型取保守档，并注明为分析性推断。

| 派生形态 | 生成源（输入类别） | 云端/保护档位（可引用性） | 说明 |
|---|---|---|---|
| 键盘学习词汇与个人词典 | 全部文本输入 | 标准保护下即端到端（102651 表格"QuickType Keyboard learned vocabulary"） | 苹果把它放在与 Health、Keychain 同一集合，是全篇最强的档位信号 |
| Safari 浏览数据与网站数据 | 浏览行为 | Safari 行标准即端到端；书签列为 ADP 新增项 | 见 9.4 与 10.4 |
| 家居事件的人脸分类数据 | 照片库人脸索引 + 摄像头剪辑 | **以 iCloud 端到端加密存于 CloudKit**；识别信息写入剪辑元数据（《PSG》26.5 版 §HomeKit） | 少数被明文端到端保护的派生产物 |
| 屏幕使用时间明细与聚合 | 前台/启动事件 | 标准保护下即端到端 | 见 9.5 |
| Siri/听写的机器生成转写 | 语音输入 | "Siri information" 列标准即端到端；按苹果隐私页面所述，默认不保留 Siri 与听写的音频录音，机器生成的转写用于改进 Siri 与听写；"Improve Siri and Dictation" 为选择加入 | 具体留存天数属公开资料未披露；本次未加载该页正文，写作时以"按苹果隐私页面所述"限定 |
| SiriKit 提交给 Siri 的 App/用户专属词汇（如联系人姓名） | 通讯录与 App 数据 | 保留期至**设备删除该 App、用户在设置中关闭该 App 的 Siri 集成，或相关会话结束**为止（《PSG》26.5 版 §SiriKit） | 这是 iOS 上少数有明确留存终点的派生数据条款 |
| 照片人物/场景/宠物标签、回忆编排 | 照片与视频 | 端侧索引的保护类与是否随备份流转：未逐项披露 | 见 8.2；按"含原文级信息"假定 |
| 摘要、说明文字、语义向量 | 内容类与通信类 | 存储位置与保护类未披露 | 见 14.6 与 19.4；本报告不以《PSG》描述相关技术栈（26.5 版无该章节） |
| 健康推断（如活动/睡眠/经期推断） | 传感器与自报数据 | 中心库为 Protected Unless Open；管理数据为 Until First User Authentication（第 6.2 节） | 推断结果与其输入落在不同保护档，是"不继承"的直接例证 |
| 建议与预测索引（App、联系人、路线） | 使用遥测 + 通信 + 位置 | 索引层保护档未披露；跨类别聚合是其本质 | 见 9.3 |

## 14.2 为什么派生数据常比输入更敏感

三个机制。**聚合（aggregation）**：单个标签低敏，标签序列高敏——"每周三 20:15 出现某人名标签"不指向任何受保护属性，但同一人物在医疗场所照片上的标签序列可以推断就诊关系。**推理（inference）**：派生数据的定义就是"由已知推未知"，因此它天然携带输入中不显式存在的信息；健康推断、关系推断、作息推断都属此类，而这些正是多数法定的特殊类别判定所指向的对象。**稳定化（stabilization）**：原始内容会被编辑与删除，而其派生索引常以更长的周期存在（建议模型、词典、分类器缓存），从而成为**关于已删除内容仍然存在的事实**的载体。第三点与第 16.3 节的残留分析直接相接。

一个可引用的旁证来自第 17 章会用的缺陷登记：iOS 27 安全内容文档中 **Accounts — CVE-2026-65404**（"A malicious application may be able to bypass Privacy preferences"）与 **Accessibility — CVE-2026-84616**（"An app may be able to access sensitive user data"）表明，授权/状态管理与辅助功能通路都能触达"敏感用户数据"这一大类，而派生索引常正是通过这些通路被消费。这里不作能力推断，仅指出：苹果用"bypass Privacy preferences"描述该缺陷，说明派生数据的消费路径确实受授权模型约束，且该约束是可缺陷化的。

## 14.3 保护继承：派生产物不自动继承输入的档位

iOS 的保护机制是**按写入方指派**的（第 10.2 节已给出文件侧规则，钥匙串侧同理由 accessibility 属性决定）。由此形成一个必须写进清单的负向事实：**不存在被文档化的"保护类随派生传递"机制**。健康数据是最清楚的例子：中心库处于 Protected Unless Open，而 App 访问权限、连接的设备名、唤起调度等**管理数据**处于较弱的 Protected Until First User Authentication 类；由这些管理数据派生出的索引与状态并不自动获得中心库的档位。同理，一张 Class A 的原图被导出为共享缓存中的缩略图后，其保护档由导出方决定。

治理上应当采用**"派生即新资产"原则**：每一次派生操作（建立索引、生成标签、写入缓存、上传特征）都视为产生了一行新资产，需独立填写四问，并默认其档位等于**写入方所在容器的默认档**（第三方即 Class C），除非有明确证据表明写入方指派了更高档。该原则的另一半是"派生不可逆推定"：不能因为输入敏感就断定派生物不敏感，也不能因为派生物是"标签"就断定它可公开。

## 14.4 派生数据在备份与同步中的位置

按第 13 章的矩阵可以直接判定派生行的落点：凡落在第三方 App 容器内的派生文件，属于《PSG》原文中的 **app data**，随 iCloud 备份上传；而由第一档服务自建的派生物（键盘词典、屏幕使用时间、Safari 数据）则随其所属服务同步并端到端。由此产生两类值得警惕的组合。其一是**同一派生信号的双重落点**：一个第三方输入法的个人词典可能进入备份（密钥域为 HSM），而系统键盘词典是端到端的——同名功能、不同归属。其二是**索引作为内容的替身**：备份中的索引若包含消息正文片段或通知片段（第 9.3 节已把"通知历史含正文"列为保守假定），则即使内容类本体被排除在备份外，其语义仍可从索引中恢复。本报告不将此写成断言，而写成一条清单检查项：**"该派生行是否包含其他类别的原文片段？若否，请给出依据。"**

## 14.5 重识别与再关联潜力

派生数据的第三重风险是它可以把**已匿名或已隔离**的数据重新绑定到人。三种典型路径：其一，人脸/声纹/姿态类标签与人名直接关联（HSV 的人脸分类数据即含人名，见 11.2）；其二，语义向量虽不含原文，但可与已知文本集比对以还原来源，故向量应被视为**可逆性未定**的编码而非哈希；其三，行为型索引（使用时序、作息、路线）与设备/账户标识结合即可完成个体识别，这正是第 3 章把标识符列为一类资产的原因，也是 iOS 27 文档登记 **iCloud — CVE-2026-84606**（"An app may be able to identify a user across reinstalls"）的类别意义所在。清单处理方式是把"可再识别性"作为独立列，取值限于：直接含身份字段 / 可与同设备内其他行关联 / 需外部数据才能关联 / 未披露。凡前两档，该行在任何共享、导出与备份决策中都要按其**关联对象**的敏感级而非自身字段敏感级定级。

## 14.6 标签的治理分类法与一个交叉引用

给出一套可直接使用的标签分类规则，作为附录C 模板中"派生层"的填写标准。

| 标签类型 | 定义 | 定级规则 | 典型处置 |
|---|---|---|---|
| L0 描述性标签 | 不含推断（如文件时长、分辨率） | 按 T1 | 可自由流转 |
| L1 内容分类标签 | 由内容推得的主题/场景类别 | 不低于输入内容一级，最低 T2 | 共享/导出前需确认接收方 |
| L2 身份关联标签 | 含人名、关系、地点归属 | 直接按 T3；若涉健康/宗教/性取向/种族推断则按特殊类别处理 | 默认不外发；记录消费者 |
| L3 状态推断标签 | 作息、位置常驻、在离场、健康趋势 | 按 T3，且可聚合性系数取高 | 需明确同意与留存终点（参照 SiriKit 条款式的终点写法） |
| L4 模型表征 | 向量、嵌入、个性化参数 | 可逆性未定 → 按其所代表内容最高档假定 | 视为"新的原始数据"，纳入删除请求范围 |
| L5 索引与缓存 | 上述各项的存储结构 | 按最弱基质假定（第 9.3 节七问） | 重建成本与残留窗口须注明 |

这套分类法的落点是：**分类标签本身不是元数据，而是内容的一种压缩形态**。因此"只传缩略图""只留标签""只上传特征"这三种常见说法，在本报告的框架里都不构成风险降级论证。关于端侧/云端智能技术栈（含其服务器侧计算形态）的攻击面、数据外发边界与验证机制，本系列已有专篇《iOS 智能识别》处理该栈的实现与威胁模型；本章只在分类学意义上占位，并保留 1.6 节的证据基线声明：《PSG》（26.5 版）未包含该主题章节，凡涉及其数据边界的断言，均须以苹果专项页面另行核对。

---

# 保护机制与强度分层

前十三章按类别横向盘点，本章改为按机制纵向拆解：iOS 用哪些层来保护这些类别，每一层的实际强度边界在哪里。第 1.5 节与第 8.6 节已分别给出"默认即弱类"与钥匙串/容器对照，本章不重复其结论，而是补全机制栈的其余部分，并以一张分层表与一段坦率的弱点评估收尾。

## 15.1 文件级加密：per-file key、类密钥与包裹关系

机制主干是两级密钥：**每个文件（或 extent）有独立的 per-file key**，per-file key 由**类密钥（class key）**包裹；类密钥按 Data Protection 类决定其可用性。这一结构有三个清单后果。第一，加密粒度是文件级而非全盘级，因此**"已加密"这一表述必须细化为"以何类密钥包裹"**。第二，per-file key 独立意味着备份与恢复可以逐文件重建档位（13.3 所述恢复流程正是"解开 per-file key 后作为新文件按类重新加密"）。第三，类密钥被销毁时，其所包裹的全部 per-file key 同时失效——这是 iOS 上"加密即密钥管理"的实现方式，也是"快速抹除"的唯一被明文支持的机制（Class D 密钥置于 Effaceable Storage 以便销毁）。

## 15.2 四类 Data Protection 及其精确可用性语义

下表为《PSG》（26.5 版）§Data Protection classes（pp.92–93）口径的整理，含该类真正重要的"何时可用"细节。

| 类名（API） | 官方名称 | 类密钥保护 | 上锁后行为 | 关键语义 |
|---|---|---|---|---|
| `NSFileProtectionComplete` | Class A: Complete Protection | 用户口令 + 设备 UID 派生 | 上锁后（Require Password = Immediately 时约 10 秒）内存中的解密类密钥被丢弃，直至再次输入口令或用 Optic/Face/Touch ID 解锁 | 唯一同时依赖"你知道的"与"设备独有的"两因子的一档 |
| `NSFileProtectionCompleteUnlessOpen` | Class B: Protected Unless Open | Curve25519 ECDH | 锁定态仍可**写入**（如后台下载邮件附件），文件一旦关闭即从内存清除其 per-file 密钥 | 构造细节（可引用）：NIST SP 800-56A One-Pass Diffie-Hellman；KDF 为 Concatenation KDF（Approved Alternative 1，SP 800-56A §5.8.1）；AlgorithmID 省略；PartyUInfo/PartyVInfo 为临时与静态公钥；哈希 SHA-256 |
| `NSFileProtectionCompleteUntilFirstUserAuthentication` | Class C: Protected Until First User Authentication | 与 Class A 同类 | **上锁/登出后不移除类密钥** | **所有第三方 App 数据的默认类**；启动后未解锁即可读取，构成结构性取证窗口 |
| `NSFileProtectionNone` | Class D: No Protection | 仅受设备 UID 保护，密钥存放于 Effaceable Storage | 始终可用 | 加密只带来"快速远程抹除"价值；未指派类的文件仍以密文存储（iPhone/iPad/**Apple Vision Pro** 全部数据皆如此）；macOS 不支持 Class D |

Class B 的存在值得一次解释性强调：它是一个**为了功能而设计的可用性让步**——允许锁定态创建新文件，但不允许读取既有内容。HealthKit（第 6 章）、邮件附件、后台下载的锁屏可用性都建立在它之上。这也说明本系列反复出现的判断：iOS 的保护档不是按"敏感度"排序，而是按"锁屏后是否需要工作"排序；凡是需要在锁定态工作的数据类别，其档位必然让步。

## 15.3 keybag 类型：user、device、backup、escrow、iCloud Backup

《PSG》（26.5 版）列出 iOS/iPadOS/macOS/tvOS/visionOS/watchOS 上的密钥袋类型：**user、device、backup、escrow、iCloud Backup**；文件与钥匙串的类密钥均在 keybag 中管理。清单上最关键的一条是：**user keybag 本身存放于 No Protection 类（Class D）**。这并非缺陷，而是设计上的必要——系统必须在用户认证之前就能读到 keybag 才能开始认证过程；其保护来自"类密钥仅受 UID 保护，故密钥全在设备上"这一事实。但它带来一个必须写进评估的推论：**对 keybag 文件的物理读取加上口令离线猜测，是与"逐文件破解"完全不同量级的攻击路径**，其强度取决于口令质量与设备是否落入未解锁状态（第 15.6 节与系列《iOS 多层防暴力破解与安全锁定机制》专篇相关，此处不重复其实现细节）。

五种 keybag 的资产语义对照如下。

| keybag | 保护的对象 | 何时进入图景 | 清单含义 |
|---|---|---|---|
| user keybag | 用户类密钥（文件与钥匙串） | 常规使用；本身存于 Class D | 口令强度是全部 Class A 档的实际下限 |
| device keybag | 设备类密钥（与用户无关的系统数据） | 系统数据、无用户参与场景 | 用户抹除不等于此类数据消失（未披露其服务端对应物） |
| backup keybag | 本地备份相关密钥 | 经 Finder/iTunes 备份 | 健康数据仅在加密备份中被包含（第 6/13 章）与此直接相关 |
| escrow keybag | 涉证/口令恢复用的密钥袋 | 密码学 escrow 流程 | 会暴露"类密钥列表：Key UUID 与所属 Data Protection class"（已核实的条款级事实） |
| iCloud Backup keybag | 上传备份中锁定态不可访问类的 per-file key | iCloud 备份 | Curve25519 非对称密钥；随机密钥保护且与备份集同存；不经 iCloud 口令（见 13.3） |

## 15.4 钥匙串、Secure Enclave 与代码签名的三重耦合

第 3.6 与 10.3 节已给出 accessibility 对照与凭证落位，本节只补机制耦合关系，因为它是 iOS 上"数据访问控制"真正的执行点。三重耦合是：**accessibility 属性**决定密钥何时可用；**Secure Enclave** 承担生物特征解锁类密钥（《PSG》述及生物特征开启时类密钥不被丢弃，而是用交给生物特征子系统的密钥包裹，并在连续 48 小时未用生物特征等条件下回落到要求口令；重启后需口令，因生物特征无法解锁的密钥已丢弃）；**代码签名 + Apple Developer Program 前缀 + access groups** 决定哪个进程有权向钥匙串发起查询（钥匙串项仅可在同一开发者的 App 间共享，由签名/描述文件强制）。三者的乘积意味着：**"谁能读"取决于进程身份与硬件绑定，"何时能读"取决于用户认证状态**。清单里凡遇到"跨 App 共享凭证"的行，必须同时写出这两轴，否则该行的保护评估无意义。

App 侧的授权输出亦属此层：应用只得到匹配成功/失败的结论，无法访问生物特征数据，但可以查询是否已注册；App 内支付同样只得到认证成功与否（第 12.1 节）。这一"结论式接口"设计是本报告中少数被文档化的"数据不外泄给第三方进程"的正面机制。

## 15.5 沙盒、逐类别授权、BlastDoor 与 Lockdown Mode

数据访问的 containment 由沙盒负责：每个进程只能访问自身容器与被显式授予的组容器；扩展在独立沙盒中运行并与容器 App 共享隐私授权（该共享机制在 visionOS 上与 iOS/iPadOS/macOS 一致）。授权层则是逐类别的 TCC 模型，其关键性质是**授权决定"能否发起访问"，不决定"访问结果如何留存"**。因此撤销权限的实际效果必须写清：撤销阻止后续访问；对**已经写入 App 容器/服务端/SDK 后端的副本**没有任何追溯效力，苹果的公开材料中也未承诺"撤销即通知第三方删除"。唯一被文档化的近似机制是 SiriKit 的保留终点（App 被删除、用户关闭其 Siri 集成、或会话结束时相关词汇不再保留），可作为一种**范式**引用，而非普适承诺。健康类授权模型（第 6.3 节）是这一层最完整的实现：按类别、按读写、无读权限时所有查询返回空数据且与空库不可区分（以防推断用户健康状况），且 App 无法得知其他 App 的授权状态。

消息处理路径的加固是 BlastDoor。需要标明出处性质：该机制是苹果在公开会议材料中描述的消息处理沙盒化设计，**不在本报告本次加载的一手文档条款内**；本系列《iOS 系统防御与机制绕过》《iOS 短信链接触发漏洞》等专篇已处理其实现与绕过研究现状。本报告只在分类学上取其一义：**把解析不可信输入的进程与可读用户数据的进程分离，使得"某类数据可被远程触发读取"成为一个需要独立论证的命题**，而不是默认可得。

Lockdown Mode 的口径已在第 9.6 节给出两份不可混用的清单（《PSG》的功能域清单：Background services、Connectivity、**Device management**、FaceTime、Game Center、Mail、Messages、Photos、Safari、System Settings、WebKit；支持文档 *About Lockdown Mode* 的行为条目要点：消息附件、网页技术、FaceTime 通话、Apple 服务的邀请、共享相册、外设/设备连接、Wi-Fi 与 2G/3G 连接、配置描述文件）。此处补充其定位与一处互斥：面向"因人身份或工作而可能被最复杂的针对性雇佣间谍软件（targeted mercenary spyware）攻击的极少数人"，以功能与性能换取攻击面收缩；可用版本为 iOS 16 / iPadOS 16 / macOS 13 / watchOS 10 及以后，iOS 17 / iPadOS 17 / macOS 14 与 watchOS 10.1 起提供额外防护；开启时 **iCloud Private Relay 不工作**。逐条完整清单（含内存检查保护、可信证书数量限制等表述）本次仅取得摘要级，须回原文复核后方可写入治理文档。

## 15.6 强度分层表与最弱处

综合以上机制，给出本报告的**保护强度分层**（T 级为敏感度层级，见 2.2；本表为"机制实际强度"层级，二者在 17 章相乘使用）。

| 层级 | 机制组合 | 代表类别 | 失效条件 |
|---|---|---|---|
| P0 硬件隔离且不入任何副本 | Secure Element + 令牌化；Secure Enclave 内生物表征 | Device Account Number；Face/Touch/Optic ID 数学表征 | 物理侧信道或硬件级攻击（本报告不作能力评估） |
| P1 端到端且密钥仅受信设备 | Class A + 第一档同步类别 | Health data、钥匙串密码与通行密钥、Home data、Maps、屏幕使用时间、键盘词典 | 受信设备被解锁访问；恢复材料被社会工程取得 |
| P2 口令 + 设备绑定 | Class A/B 本机文件 | 系统级内容、需锁屏不可读的缓存 | 未解锁取证窗口结束前；口令强度不足 |
| P3 首次解锁即可用 | Class C（第三方默认）+ `AfterFirstUnlock` 类钥匙串 | 绝大多数 App 数据与凭证 | 设备曾解锁且未重启——现实中最常见的窗口 |
| P4 密钥与内容同域交付 | iCloud 备份（标准档）、`*ThisDeviceOnly` 之外的同步项 | app data 备份、Drive/照片/备忘录（未开 ADP） | 账户认证被完成一次即可，口令轮换无效 |
| P5 互通性所迫的明文可读 | Mail/Contacts/Calendars、链接分享、协作中介 | 邮件全文与附件、日历事件、"anyone with a link" 内容 | 服务端可读是设计属性，无法通过开关改变 |
| P6 不受 iOS 保护类管辖 | 车机、配件、第三方 SDK 后端、应急/关系通道副本 | CarPlay 渲染缓存、SDK 上报、共享给自然人的副本 | 完全取决于对方域，本清单不可控 |

**弱点坦诚评估**：这套模型的四条真实薄弱线是——**元数据**（时间戳、APNs 路由信息不加密；CloudKit 修改时间与校验和不端到端；分享操作的标题与代表缩略图以标准保护存储；Apple Invites 的日期/城市级位置/海报发布后始终标准保护）；**备份**（密钥域随上传而转移、口令变更不撤销、Messages 关闭备份只保护之后的消息）；**派生索引**（档位未披露、不继承输入、可含原文片段、可再识别）；**第三方 SDK 与 App 自身**（默认 Class C、营养标签为自我声明、外发后彻底脱离本清单）。这四条不构成"iOS 不安全"的论证，而是限定清单工作的边界：把它们写成空格而不是结论，是本报告的方法要求。

---

# 数据流转路径与生命周期

前四章讨论"在哪里、受什么保护、怎么离开"，本章补上第四个维度中最容易被省略的一段：**时间**。同一份数据在生成的第 5 秒与第 500 天，其位置集合、密钥域集合与可读主体集合都不同。生命周期视角的直接价值是它能把第 13 章与第 15 章的静态结论变成动态结论——例如"开启 ADP 使端到端覆盖变宽"这条陈述，只对开启**之后**新产生的数据成立。

## 16.1 七阶段模型

本报告的登记表为每一资产行增加生命周期列，取值来自下列七阶段。

| 阶段 | 发生什么 | 已文档化的机制锚点 | 清单必答 |
|---|---|---|---|
| 一、产生（generation） | 传感器采样、用户输入、网络接收、系统服务写入 | 捕获即加密（iPhone/iPad/Apple Vision Pro 全部数据皆密文存储） | 首次落盘时的容器与保护类由谁指派 |
| 二、处理与建索引（processing/indexing） | 视觉分类、转写、建议、事件检测、OCR | HSV 的 home hub 本地分析（明文态出现在 home hub）；SiriKit 词汇提交 | 中间明文出现在哪个进程/哪台设备；是否留下 L 级派生行 |
| 三、本机存储（at rest on device） | 长期驻留于容器/数据库/钥匙串 | 四类 Data Protection、五种 keybag | 稳态档与"锁定后是否仍可用" |
| 四、云复制（replication to cloud） | 同步、备份、上传附件 | 13.1 三档、13.3 iCloud Backup keybag、iMessage 附件以 AES-CTR + 随机 256-bit 密钥加密后上传 iCloud | 密钥域是否已从"口令 + 设备 UID"迁移到服务端可保管的域 |
| 五、共享与导出（sharing/export） | 链接、协作、相册、AirDrop、邮件转发、截图 | "anyone with a link" 以标准保护交付；Freeform "Send a Copy" 链接期内未加密；共享相册不支持 ADP | 接收方是账户、自然人还是匿名持有链接者 |
| 六、删除（deletion） | 用户删除、撤销授权、抹掉设备、关闭功能 | Class D 密钥置于 Effaceable Storage 以支持快速远程抹除；Class B 文件关闭即从内存清除 per-file key；HealthKit 临时 journal 在合并完成时删除；ADP 开启时 HSM 密钥删除为 immediate/permanent/irrevocable | 删的是明文、密文、密钥还是"一份副本" |
| 七、法务与取证获取（acquisition） | 设备取证、涉证 escrow、政府请求 | iOS 密码学 escrow 暴露类密钥列表（Key UUID 与 Data Protection class）；透明度报告口径见 18.5 | 端到端类别不可应求解密；可读类别可经程序获取 |

阶段之间的**降级点**是本报告最重要的观察对象。可引用的降级点至少有五处：上传 iCloud 备份（13.3）、任何"任何人可通过链接访问"的分享、Freeform 的发送副本、iCloud.com 网页授权（武装 1 小时）、以及关闭 ADP（设备把服务密钥上传 HSM，但不含标准即端到端类别）。反向的**升级点**只有一处，即开启 ADP，且它伴随密钥轮换（iCloud Backup service key 与 Messages in iCloud container key 均被轮换），因此其效力边界仍是"新数据"。

## 16.2 各路径的删除语义

删除在 iOS 上不是单一动作，而是四件事：切断可读性、销毁密钥、删除副本、请求他方删除。下表把已被文档化的部分与必须留空的部分并列。

| 删除动作 | 已文档化的效果 | 未披露/不成立的部分 |
|---|---|---|
| 用户删除一条消息 | 本机列表移除；同步至其它设备 | 是否从 iCloud 备份集合中移除既有副本：未披露；备份内既有容器密钥不因删除动作而改变 |
| 关闭 Messages for iCloud / 关闭 iCloud 备份 | 关闭备份后设备生成新密钥保护**后续**消息，新密钥只存于 iCloud Keychain、仅受信设备可用；新数据无法用旧容器密钥解密 | 旧容器密钥仍留存 → 历史内容仍属可被 Apple 读取的一类；关闭 ADP 时上传的密钥集合"从不含标准即端到端的类别" |
| 删除 App | 容器随之移除；SiriKit 场景下相关提交词汇的保留期结束（三条件之一即"设备删除该 App"） | 开发者/SDK 后端已接收的数据完全不受影响；服务端历史由第三方策略决定 |
| 撤销某项权限（逐类别授权） | 阻止后续访问；健康类撤销后按无读权限处理（查询返回空数据） | 对已导出副本、已建索引、已上传备份无追溯效力；苹果未承诺"撤销即通知第三方删除" |
| 移除配置描述文件 | 其下所有配置与设置（视注册类型与配置而定，也含受管 App）一并移除 | MDM 服务端自身的记录与历史：未披露 |
| 抹掉设备（远程/本地） | Class D 类密钥置于 Effaceable Storage，其设计价值即"快速远程抹除" | 已上传的 iCloud 备份、已同步的云端副本、已共享出去的副本全部不受影响 |
| 修改 iCloud 口令 | 常规认证材料更新 | **不会使旧备份失效**（13.3 明文：备份加密不使用 iCloud 口令） |
| 删除 iCloud 备份 | 备份集合与其 keybag 从服务端移除（这是唯一能撤销 13.3 密钥域转移的用户动作） | 删除的生效时点与不可恢复性：本次未加载逐项条款 |
| 提交隐私请求以删除账户数据 | 见 18.4 | 端到端类别本就不需 Apple 删除即可保证不可读；互通性类别与其他账户数据的实际处置范围以苹果公开流程说明为准 |

这里最需向读者强调的一句是：**"备份中的旧副本"与"本机的删除操作"分属两个世界**。第 13.3 节的密钥独立性在此完全体现：删除本机文件不会使已上传的密文与 keybag 消失，改口令不会使其不可解，抹掉设备更不会。治理文档若把"删除"写成单值布尔，就是在掩盖风险。

## 16.3 残留分析（事实陈述，非操作指南）

"一张被删除的照片还剩什么"是本节要回答的原型问题。可确定的事实分若干基质。

| 残留基质 | 是否可能留存 | 依据强度 |
|---|---|---|
| 缩略图与预览缓存 | 可能（由系统服务与 App 各自生成，落点与档位未逐项披露） | 弱（按"可能"登记，不写"必然"） |
| 视觉/语义索引与标签 | 可能（人物与场景标签、回忆编排、HSV 的人脸分类数据均为独立资产行） | 中（HSV 人脸分类数据被文档化为独立存储于 CloudKit 的端到端数据） |
| 消息与通知历史 | 可能（正文过长或含附件时《PSG》有明确处理路径；通知历史含正文属保守假定） | 中 |
| 日志与诊断 | 可能（崩溃报告与分析数据的字段构成属公开资料未逐项披露） | 弱 |
| 云端备份与同步副本 | iCloud 备份与同步域的副本留存，本机删除不影响 | 强（13.2、13.3 条款） |
| 他方持有副本 | 共享相册、链接、AirDrop、聊天附件、家庭成员设备 | 强（结构性事实，与加密档无关） |
| 闪存级/文件内容级残留 | **公开资料未披露** | 无（本报告禁止在此写"已安全擦除"或"可完整恢复"任一断言） |

关于最后一条的立场必须重申：苹果的一手材料只支持**密钥级**删除表述（Effaceable Storage 中的 Class D 密钥、Class B 关闭即清除内存中的 per-file key、HealthKit 临时 journal 在合并完成时删除、ADP 开启时 HSM 密钥的立即永久不可撤销删除）。文件内容级与闪存级残留的物理可恢复性属未公开议题，任何断言都超出证据基线；本报告的处理方式是把"密文是否仍在介质上"与"密钥是否仍可得"作为两个独立登记项，后者可判定，前者留空。

## 16.4 有据可依的留存窗口

把散落在各条款中的时间量集中成一张表，是本报告少数可直接复用的成果之一。凡未列入者即为"未见披露"，不等于"不留存"。

| 时间量 | 数值/条件 | 出处口径 |
|---|---|---|
| iCloud 备份触发 | 每日，经 Wi-Fi；条件为设备已上锁 + 接电源 + 有 Wi-Fi | 《PSG》（26.5 版） |
| Class A 类密钥在内存中的保留 | 上锁后约 10 秒（Require Password 设为 Immediately 时） | 《PSG》（26.5 版） |
| HealthKit 中心库在锁定态的访问 | 上锁 10 分钟后放弃访问权；`HKWorkoutSession` 期间可持续 | 《PSG》（26.5 版）§HealthKit |
| iMessage 离线排队 | 在 Apple 服务器上最长保存 **30 天**；消息送达即从 APNs 删除 | 《PSG》（26.5 版）§Messages |
| APNs 载荷上限 | 4 KB 或 16 KB（随 iOS/iPadOS 版本而异） | 《PSG》（26.5 版） |
| 生物特征替代口令的时间 | 连续 48 小时未用生物特征等条件下回落到要求口令；戴口罩模式下 Face ID 有效期 6.5 小时 | 《PSG》（26.5 版） |
| 重启后的密钥状态 | 需口令；生物特征无法解锁的密钥已丢弃 | 《PSG》（26.5 版） |
| ADP 下 iCloud.com 授权的"武装"窗口 | 1 小时，且仅接受针对 iCloud.com 可见服务 allow-list 的密钥上传，每次上传通知用户 | 《PSG》（26.5 版）与支持页 |
| SiriKit 提交词汇的保留期 | 至设备删除该 App、用户关闭该 App 的 Siri 集成，或相关会话结束 | 《PSG》（26.5 版）§SiriKit |
| 链接分享场景 | Freeform "Send a Copy" 在链接有效期内以未加密状态存放 | 支持页 102651 口径 |
| HSV 剪辑保存天数 | **未在本次苹果一手来源中确认**（"10 天"仅见于第三方材料） | 见 11.2 |
| 第三方推送载荷留存 | 公开资料未披露 | 见 1.6 |
| Siri/听写转写与音频天数 | 公开页面未披露具体留存天数 | 见 14.1 |

## 16.5 生命周期末段：法务与取证获取

阶段七在本报告中只作为**清单属性**处理，不写操作流程。可引用的机制事实有三条：其一，iOS 的密码学 escrow 流程会暴露"类密钥列表：Key UUID、所属文件或钥匙串 Data Protection class"，即类别结构本身可成为可导出的情报；其二，本地钥匙串虽进入 iCloud 备份，但由 Secure Enclave **UID 根密钥**派生密钥加密，**只能还原到同一台设备**，任何主体（包括 Apple）都无法读取其内容；其三，第一档端到端类别在设计上不可经由请求获得明文，苹果亦载明恢复失败时**无法帮助恢复**端到端加密数据。这三条共同说明：**同一法律程序作用于不同资产行时，其可获得性可以相差数档**——因此"这台设备上存了什么"与"这台设备上有什么能被依法取出"是两个不同的清单。附录B 用"是否可端到端加密"与"监管归类"两列分别承载这两个问题（监管映射属分析性归纳，见附录D 注）。

## 16.6 实践结论：生命周期盲的清单会低估暴露

把本章压缩成三条可执行判据。**判据一**：每一资产行必须填写"当前档"与"开启 ADP 后档"两列，且凡涉及删除语义的行须再加"该动作对旧副本是否有效"列。**判据二**：降级点（备份上传、链接分享、协作中介、Web 授权、关闭 ADP）应在风险登记册中作为**事件**而非**状态**记录，因为它们有明确的发生时刻与可审计性。**判据三**：残留问题按基质分列，能判定密钥的判定密钥，不能判定密文的留空并注明"公开资料未披露"。三判据合起来的效果是：生命周期视角把"这台设备很安全"这一类陈述从文档中清除出去，替换成可核对的时间序列——这正是第 17 章做风险评估所需要的输入形态。

---

# 暴露风险与影响评估

本章把前十几章的事实转成评估。范围纪律重申一次：这里评估的是**暴露的结构性可能性与其后果**，不评估攻击能力、不提供获取路径，威胁场景只以列标签形式出现。凡涉及定向监控软件的能力描述，一律指向系列既有专篇（漏洞利用套件与链条组合类、数据采集能力类、商业间谍软件与监控技术类、零日漏洞与定向攻击类），本章只记录"该列对本报告类别的结构性含义"。

## 17.1 数据类别 × 威胁场景矩阵

列标签：**S1** 丢失/被盗设备（锁定态）；**S2** 已解锁设备取证（首次解锁后窗口成立）；**S3** 云账户被入侵（含口令、会话与恢复材料）；**S4** 越权或恶意 App（含其嵌入 SDK）；**S5** 企业 MDM 可见性（合法受管通道）；**S6** 定向监控软件；**S7** 供应链（构建、分发、维护环节）；**S8** 备份介质丢失（本地加密/未加密备份、他人持有的导出副本）。取值：**H** 高、**M** 中、**L** 低、**—** 结构性不适用、**?** 未披露（按保守假定计入）。

| 数据类别 | S1 | S2 | S3 | S4 | S5 | S6 | S7 | S8 |
|---|---|---|---|---|---|---|---|---|
| Device Account Number | — | — | — | — | — | ? | M | — |
| Face/Touch/Optic ID 数学表征 | L | L | — | — | — | L | ? | — |
| 钥匙串密码与通行密钥 | M | M | M | M | M | ? | ? | M |
| `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly` 凭证 | L | L | — | M | — | ? | ? | — |
| 第三方 App 数据文件（默认 Class C） | L | H | H | H | M | H | H | H |
| App 凭证令牌（AfterFirstUnlock） | M | H | H | H | M | H | H | H |
| 会话 Cookie / localStorage / IndexedDB | M | H | H | H | L | H | H | M |
| iMessage 正文 | L | M | M（未开 ADP 的历史为 H） | M | L | H | L | M |
| 消息元数据（时间戳、路由、IDS 目录项） | L | M | H | M | M | M | L | M |
| RCS 消息 | M | M | ? | M | L | ? | L | M |
| 邮件（iCloud Mail） | L | H | H | M | H | M | M | H |
| 通讯录 / 日历 | M | H | H | H | M | H | M | H |
| 位置历史与轨迹 | M | H | M | H | M | H | M | M |
| 照片原片 | M | M | M（未开 ADP 为 H） | H | L | H | L | M |
| 照片派生标签与视觉索引 | ? | H | ? | M | L | H | ? | H |
| 视频与语音备忘 | M | M | M（未开 ADP 为 H） | H | L | H | L | M |
| 健康与健身记录 | M | M | L | H | M | H | M | M（未加密本地备份为 H） |
| Medical ID | L | L | L | M | — | M | — | L |
| 屏幕使用时间与通知历史 | M | H | L | M | H | H | L | M |
| 键盘个人词典 | M | H | L | M | L | H | M | M |
| HomeKit 配置与自动化时间线 | M | M | L | M | L | M | L | M |
| HSV 剪辑与人脸分类数据 | M | M | L | M | L | M | M | M |
| Wallet 通行证（含证件类） | M | M | M（未开 ADP 为 H） | M | L | M | L | M |
| 购买历史与收据 | L | M | H | M | M | M | L | M |
| 配置描述文件与已装证书 | M | H | M | L | H | H | H | M |
| MDM 受管域数据 | L | M | M | L | H | M | H | L |
| 诊断与崩溃日志 | M | M | M | M | H | M | H | M |
| iCloud 备份内容（app data 等） | — | — | H | — | M | ? | ? | H |
| 共享/链接中的内容 | M | M | M | — | L | M | — | H |
| 派生摘要与向量表征 | ? | H | ? | M | ? | H | ? | ? |
| 车机侧渲染与缓存 | M | — | — | — | — | ? | M | H |
| 家庭共享可见项 | M | M | M | M | L | M | L | H |

矩阵的三条读数规则。**第一**，H 集中在 S2 与 S4 不是巧合，而是 15.2 节 Class C 默认档与 15.4 节"授权决定能否访问、不决定结果如何留存"这一分离的直接后果；S1 多为 M/L 则体现"未解锁 + 强口令"的实际保护力。**第二**，S3 一列在备份行上的 H 是全表唯一可被单一用户动作大幅改变的值（开启 ADP 或删除旧备份），这也是 17.6 节把 ADP 列为首位处置的评估依据。**第三**，S5 一列的 H 只出现在受管工件、配置与遥测类而非内容类——这与 10.6 节"字段清单未枚举"并不矛盾：苹果文档以授权式表述给出的是宽查询面，而内容类读取在本报告证据基线内始终没有依据，因此该列的 H 应读作"设备结构与合规信息的可见性高"，不是"内容可读"。

## 17.2 影响评级方法：四因子

本报告采用**暴露评级 = 敏感度层级 × 可聚合性 × 可恢复性 × 监管权重**。前三因子来自清单字段，第四来自附录D 的映射（属分析性归纳）。为抑制"凭印象定级"，取值规则固定如下；实际计分采用**四因子加和**（1–4 分制，区间 4–16），以便逐步复核与争论单因子而非总分。

| 因子 | 1 | 2 | 3 | 4 |
|---|---|---|---|---|
| 敏感度层级 T | T1 低 | T2 中 | T3 高 | T4 极高/特殊类别 |
| 可聚合性 A | 单条无意义、无法关联 | 可与本机少量类别关联 | 可跨设备/跨时间构造个人画像 | 可直接指向受保护属性（健康、性取向、宗教、行踪常驻） |
| 可恢复性 R | 结构性不入副本，删除即不可恢复 | 本机可恢复窗口有限 | 云端副本/备份内可被再次获取 | 账户侧长记忆或他方持有，用户动作不改变 |
| 监管权重 C | 非个人信息 | 一般个人信息 | 敏感个人信息（含生物特征、健康） | 特殊类别 + 跨域传输 + 未成年等加重因素 |

分数区间：4–7 常规、8–11 高、12–16 最高。可恢复性因子的判定完全依赖 16.2，这是"生命周期列必须先填"的方法学原因：不填删除语义的清单，其评级结果必然偏高或偏低且不可解释。

## 17.3 工作示例（一）

**① 第三方 App 数据文件（默认 Class C）**
T=3（可含通信内容、健康与关系信息）；A=4（同一 App 内即可跨时间构造画像，跨 App 时经标识符再关联）；R=3（本机删除后，iCloud 备份中的既有副本仍属可再次获取，且改口令不撤销）；C=3。**加和 13 → 最高档**。结构性结论：本类高分由 R 与 A 贡献，而非由"文件未加密"贡献——它一直是密文；真正的问题是密钥域与副本集合。

**② 健康与健身记录（HealthKit 中心库）**
T=4；A=4（可指向健康状态、生育周期、行动能力）；R=2（云端第一档端到端；本地备份仅在加密时包含；管理数据档位较低但其自身敏感度有限）；C=4。**加和 14 → 最高档**。本类风险重心不在 S3（密钥在受信设备），而在 S4/S6/S8：授权撤销无追溯效力，未加密本地备份会包含健康数据。

**③ 消息元数据与 IDS 目录项（时间戳、APNs 地址、关联电话号码、公钥）**
T=3（元数据可直接指向关系与在场性，且不随内容一起端到端）；A=4（通信图本身即高敏结构）；R=3（目录项随账户存在；送达即从 APNs 删除，但离线队列可在 Apple 服务器上最长保存 30 天，且元数据不加密为明文条款）；C=3。**加和 13 → 最高档**。本类是 15.6 节"元数据薄弱线"的量化体现：内容行得分低而元数据行得分高，只治理正文的策略必然漏掉这一行。

## 17.4 工作示例（二）

**④ 配置描述文件与已安装证书（受管域）**
T=3（改变信任链即改变其它所有类别的机密性前提）；A=3（暴露组织结构、内部服务地址与策略）；R=3（移除 profile 会连带移除其下配置，但服务端记录未披露）；C=2。**加和 11 → 高档**。S5/S7 两列主导。

**⑤ HomeKit Secure Video 剪辑与人脸分类数据**
T=3（含住宅内影像与可识别第三人）；A=4（时间序列直接推断在离场与作息）；R=2（端到端上传、密钥经 CloudKit 端到端、剪辑与会话子段各自唯一密钥；但 home hub 存在明文态）；C=3（涉住宅内他人与可能的未成年）。**加和 12 → 最高档**。本类高分不由"云端可读"贡献，而由聚合强度与明文态所在设备贡献。

**⑥ 派生摘要/向量表征与索引层**
T=3（可含原文语义且可再识别）；A=4（其存在理由就是跨源聚合）；R=?（存储基质、是否随备份流转、删除后清除时机均公开资料未披露 → 按 3 计入并注明保守假定）；C=3。**加和 ≥13 → 最高档（含假定项）**。这是全表唯一一行其不确定性主要来自证据缺失而非机制，恰好印证 15.6 的"派生索引"薄弱线。

**⑦ Device Account Number（对照低暴露行）**
T=4（金融标识本身极高）；A=1（一次性密文不可复用，DAN 与卡号不同、不用于磁条/电话/网站场景）；R=1（存于 Secure Element，"never stored on Apple Pay servers or backed up to iCloud"）；C=3。**加和 9 → 高档但因 A/R 双低而实际暴露可控**。本行与第①行构成方法学对照：**敏感度最高的类别可以是暴露最低的类别**，前提是它的可聚合性与可恢复性被架构（而非流程）压低。

## 17.5 S6 列的处理方式与评估边界

定向监控软件一列在本报告不承担能力描述义务：其利用链构造、涉及组件与漏洞类别、在野利用的归属方法已由系列既有专篇处理，重复描述只会稀释证据基线并造成两处不一致。本章只保留三条与类别视角直接相关的结构性陈述。**其一**，一旦取得设备内的执行位置，Data Protection 类的差异对"是否可读"不再有决定意义（解锁态下类密钥在内存中，见 15.2），这解释了 S6 列在多数内容类上取 H/M 而非 L。**其二**，端到端类别在此列仍有独立价值：第一档类别在设计上不存在"通过平台侧获取明文"的路径，这是资产行的固有属性而非对抗结果。**其三**，苹果对该类威胁的自我定位可引用：Lockdown Mode 面向"因人身份或工作而可能被最复杂的针对性雇佣间谍软件（targeted mercenary spyware）攻击的极少数人"，其代价是功能与性能，且开启时 Private Relay 不工作。评估边界同时写明：本矩阵不预测任何具体个体是否会被针对，不排序供应商，不推断任何未公开能力；S6 列表达的是"若发生设备内读取，该类别的敏感度与聚合后果"，而不是"该类别已被读取"。

## 17.6 处置优先级

把全表按"分数 × 可操作性"排序，得到四项优先处置。**一，密钥域收敛**：开启 ADP，或至少定期删除旧的 iCloud 备份——因为 13.3 表明口令轮换不是撤销手段。**二，App 与 SDK 数量收敛**：S2/S4/S7 三列的共同上游，直接压低默认 Class C 资产行的总量。**三，逐类别授权审计与最小化**：唯一能改变 S4 列的用户侧动作，同时必须接受"撤销无追溯效力"这一限制。**四，共享与链接行为的登记化**：把 13.5 的每个降级点当作事件记录，含 Freeform 链接期未加密与"anyone with a link"的元数据留存。高价值个体另加第五项：**Lockdown Mode 评估**，并明确其与 Private Relay 的互斥与功能代价。附录C 的模板按这五项反向设计字段，使清单一次填写即可支撑评估与处置。

---

# 治理、合规与实务

本章把评估转为义务与动作。约束前置：本次核验**未加载任何法律条文页面**（apple.com/legal/privacy 系列仅 Apple Advertising 一项被实际加载），因此本章所有法定概念对应关系均为**分析性归纳**，用于治理落地的对照，不构成法律意见，也不代表 Apple 对其数据作了法定分类。同样，凡涉及苹果隐私请求流程的具体范围，本章只写可公开核对的位置与常识级流程，并把未加载正文的部分明确标注。

## 18.1 问责义务的落点：清单本身就是证据

多数隐私框架的问责条款（accountability）不规定结果而规定"可证明的过程"。对本报告而言，可证明的过程就是资产登记表的完成度。把前十几章的判定规则汇总成合规证据形态如下。

| 义务 | 需要什么证据 | 本报告的对应产出 |
|---|---|---|
| 处理活动记录 | 类别 × 位置 × 保护 × 流转 × 保留 | 附录B 矩阵、附录C 模板 |
| 目的限定与最小化 | 每类数据的采集理由与字段范围 | 18.2 的逐类别授权表 |
| 存储限制 | 保留期与到期动作 | 16.4 留存窗口表（未披露项按"未知"记录） |
| 完整性与保密性 | 保护措施与实际强度 | 15.6 分层表（P0–P6） |
| 数据主体权利响应 | 可获取、可纠正、可删除的范围 | 18.3、18.4 |
| 跨境与第三方转移 | 接收方清单与依据 | 13.5 互通性例外 + 18.5 营养标签核对 |
| 影响评估 | 高风险处理的事前评估 | 第 17 章四因子法 |
| 安全措施的可辩护性 | 不夸大、不臆断 | 全篇三值置信标记 |

其中最后一行是方法而非条目：**清单里的每个空格都比一个错误的确定值更有合规价值**。第 10.6 节（MDM 字段未枚举）与第 16.3 节（闪存级残留未披露）是两处最典型的例子——把它们写成结论会制造无法兑现的承诺，把它们写成空格则可审计、可复核、可随证据更新。

## 18.2 同意与逐类别授权：范围必须按类别而非按 App

iOS 的授权模型是逐类别的（第 6.3 节以 HealthKit 为最完整实现：读取与写入分别授权、每种数据类型分别授权、无读权限时查询返回空数据且与空库不可区分、App 无法得知其他 App 的授权状态）。治理上有四条推论。

| 治理动作 | 依据 | 注意 |
|---|---|---|
| 把同意粒度对齐到类别粒度 | 逐类别授权是系统实现的事实 | 一份"同意全部权限"的界面在法律上通常不成立 |
| 记录"授权—消费—留存"三段 | 撤销只影响第一段（15.5） | 必须同时记录第三方侧的留存政策 |
| 追踪授权类缺陷 | iOS 27 安全内容文档 **Accounts — CVE-2026-65404**（"A malicious application may be able to bypass Privacy preferences"） | 授权机制是可缺陷化的实现，不是不可变保证 |
| 把追踪与归因分开征同意 | ATT 的授权门控针对"追踪"（`ATTrackingManager`、`NSUserTrackingUsageDescription`）；AdAttributionKit 是另一套机制（覆盖 App Store 与替代应用市场，文档中仍出现 SKAdNetwork 字样） | 二者迁移时间表公开资料未披露；不得写"IDFA 已下线"这类断言 |
| 优先采用可切断关联的登录方式 | Sign in with Apple 把同一自然人在不同 App 的标识分离 | 降低 17.2 的可聚合性因子，是可量化的收益 |

追踪类同意的用户侧可核对位置是可引用的：**Settings > Privacy & Security > Apple Advertising > View Ad Targeting Information** 可查看用于广告定向的信息，同面板内有 **Personalized Ads** 开关（已加载的 Apple Advertising & Privacy 页面口径）；健康类授权在 **Settings > Health > Data Access & Devices** 查看与撤销。这两处是少数"用户可自助核对的类别级面板"，应在治理文档中被写成具体路径而非抽象建议。

## 18.3 最小化、留存与删除请求

最小化在 iOS 上的实现抓手有三个：**字段级**（App 只申请其功能必需的类别；证件类凭证的字段选择式呈现是范例，见 12.3）、**副本级**（避免同一内容在容器、组容器、云端、备份四处留档；10.2 的"派生即新资产"原则是其操作形式）、**留存级**（优先采用有明确保留终点的机制，如 SiriKit 词汇的三个终止条件，而不是无界保存）。

删除请求的实务难点在于**"删除"必须被拆成四条指令**：删除本机内容、删除云端副本（同步域）、删除云端备份集、向第三方（开发者、医疗机构、家庭成员、车厂、应急服务）发出删除请求。第 16.2 节已经证明这四条互不蕴含。清单上应把删除动作按资产行写成矩阵而非写成一条政策；对监管响应，则应把"已删除"的表述限定到能被证实的那一层（通常是密钥可得性，而非介质残留）。

## 18.4 用户能从苹果的隐私请求流程得到什么

苹果面向用户提供的隐私自助通道，公开层面的口径可归纳为三类请求：**获取副本**（请求一份个人数据的拷贝）、**更正**、**删除或停用账户**。具体入口、可选数据项、交付格式与处理时长均以该流程页面为准，**本次证据基线未加载其正文**，因此本章只写结构性结论并标注"按苹果公开隐私页面所述，本次未逐条核对"。可依据已核实材料写出的，是这类请求的**效力边界**，而这恰是治理文档最需要的部分。

| 请求事项 | 可依赖的已核实事实 | 实务结论 |
|---|---|---|
| 数据副本的范围 | iCloud 备份不含已购内容本体但**完整购买历史与 Apple Account 关联**；IDS 目录存放 iMessage 公钥、APNs 地址与关联电话号码 | 副本应包含账户侧的结构与标识数据；具体项以流程页面为准 |
| 端到端类别的"副本" | 第一档类别密钥仅在受信设备，Apple 无法读取其内容 | 平台侧无法交付其无法解密的明文；用户侧交付依赖受信设备与恢复材料 |
| 删除的可达范围 | 改口令不使旧备份失效；备份密钥默认存于 Apple 数据中心 HSM | 要求删除必须指向备份集合本身，而非账户口令 |
| 恢复材料 | ADP 需双重认证 + 设备密码 + 至少一个恢复联系人或恢复密钥；恢复失败时 Apple 无法帮助恢复 | 恢复材料是数据可得性的组成部分，应纳入离职/身故等场景预案 |
| 第三方 App 与 SDK 的数据 | 营养标签为开发者自我声明（18.5）；外发后脱离本清单 | 需向开发者另行主张，不能期待平台代为删除 |
| 政府请求的历史 | 透明度报告披露 Device Requests 与 Financial Identifier Requests 的类型与数量 | 可核对"被请求的规模"，不可核对"内容是否被交付"（见 18.5） |

## 18.5 苹果侧合规工具与其局限

**App Privacy Details（营养标签）**：页面存在性与定位已核实（developer.apple.com 的 App Store 板块，标题为 "App Privacy Details - App Store - Apple Developer"），且该术语与 App Tracking Transparency 文档 topics 中的 "User Privacy and Data Use" 并列出现。其数据类别枚举、"是否关联身份 / 是否用于追踪"矩阵正文本次未逐条抓取，隐私清单（PrivacyInfo.xcprivacy）机制本次未加载，故第 2.6 节的四条调和规则中凡依赖类别枚举的部分均须标为**待复核**。使用纪律是：**标签是声明，不是控制**；标签存在不等于收集受限，标签缺失也不等于未收集。

**App Review 与分发约束**：App Review 对权限申请与数据使用设有规则，但其规则文本、执行率与个案均未在本次证据基线内，故本报告不写"审核保证了什么"，只写"审核是声明的校验环节之一"。可引用的相邻事实是《PSG》对健康的**用途限制**原文："apps aren't allowed to use health data for advertising"，且 App 须提供说明其健康数据使用的隐私政策——这类**平台侧的行为禁令**比审核流程本身更适合写入治理文档。

**透明度报告**：已加载的页面口径是对**政府请求**做统计披露，含 **Device Requests**（通常寻求与客户及客户设备与 Apple 服务的连接信息相关的信息，例如失窃设备调查；并载明定期收到与欺诈调查相关的多设备请求）与 **Financial Identifier Requests**（通常寻求与疑似欺诈交易相关的信息，自 2016 年 7–12 月报告期开始披露）。据此可写的结论是：**该报告的口径是请求的类型、数量与账号数，不披露内容型数据的访问细则**；其余小节本次未核对，不得据以推断"苹果拒绝了内容请求"。

**机型与功能可用性作为合规变量**：安全功能的可用性并非全量一致（详见附录F），最要紧的一条是 **Managed Apple Accounts 与儿童账户（按地区）不支持 ADP**——这使得未成年与受管场景的密钥归属结构性停留在标准保护档，治理文档必须为此单独设定补偿控制（设备用途限制、App 数量、备份开关策略）。

## 18.6 BYOD 边界、分层行动清单与残余风险

企业 BYOD 的治理核心是**受管域与非受管域的分界**，而这条分界在苹果文档中是可写的：User Enrollment 面向用户自有设备、需 Managed Apple Account、以加密方式隔离受管数据；Device Enrollment/ADE 具备更大权限集且可阻止用户移除注册描述文件；iOS 17 / iPadOS 17 / macOS 14 / visionOS 1.1 起账户驱动的 Device Enrollment 也有类似数据分离机制。移除注册描述文件时其下所有配置与设置（视注册类型与配置而定，也含受管 App）一并移除。据此可给出 BYOD 的三条边界规则：**一**，企业不应主张对非受管域内容的可见性（一手材料亦无此依据）；**二**，企业应在政策中写明其可查询面的性质（"查询设备某些方面"，字段以 MDM 协议参考为准）；**三**，企业应把"证书与网络配置由 MDM 下发"作为对员工透明的事项披露，因为证书信任的改变会影响员工全部流量的机密性前提。

| 主体 | 优先动作 | 依据章节 |
|---|---|---|
| 个人 | 开启双重认证与设备密码；评估并开启 ADP（先备好恢复联系人/恢复密钥）；定期删除旧 iCloud 备份；逐类别审查权限与连接设备；谨慎对待链接分享与协作；仅在需要时保留未加密本地备份 | 13.4、15.2、16.2、17.6 |
| 开发者 | 为敏感文件显式指派更高 Data Protection 类而非依赖默认 Class C；凭证按 accessibility 分档，机器本密密钥用 `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly`；CloudKit 字段按需声明 encrypted；避免把敏感内容写入 App Group 或缓存目录；营养标签与隐私政策对齐实际数据流；派生索引登记落点与清除时机 | 10.1–10.5、13.5、14.6 |
| 企业 | 选择注册形态并记录其权限差异；把 ADE 设备视为具有可追踪标识的资产；证书与 VPN 配置最小化；受管与非受管域分离政策；离职流程同时覆盖 MDM 侧记录与备份删除（后者属未披露需注明）；高价值人员评估 Lockdown Mode 及其与 Private Relay 的互斥代价 | 10.6、15.5、18.6 |

**残余风险（治理不能解决的部分）必须诚实列出**：其一，**元数据不可端到端**的类别（互通性服务、排序用修改时间、去重用校验和、分享标题与代表缩略图、Apple Invites 的日期/城市级位置/海报）不在任何开关的控制范围内；其二，**备份的密钥独立性**意味着历史暴露不可追溯消除，只能向前改善；其三，**账户侧长记忆**（购买历史、IDS 目录项、注册与证明记录）不因本机动作而消失；其四，**他方持有**（家庭成员、受邀协作者、医疗机构、车厂、开发者与 SDK、应急服务）在本清单之外；其五，**未披露项**——派生索引的保护档、闪存级残留、推送载荷留存、外设数据面——是不可消除的认知残余，任何声称"已通过政策覆盖"的表述都是虚假保证。治理的正确目标因此不是"零暴露"，而是**把每一行资产的暴露状态写成可核对、可更新、可辩护的形式**。

---

# 结论

## 19.1 主要发现

本报告以资产清单视角把 iOS 及其云/伴生生态的数据类别拆解为可登记行，主要发现如下，均可回溯到前文的具体条款与置信标记。

1. **保护强度按"锁屏后是否需要工作"排序，而非按敏感度排序**。凡是需要在锁定态工作的类别（Class B 的 Curve25519 构造、`AfterFirstUnlock` 类凭证、HealthKit 的 `HKWorkoutSession` 例外），其档位必然让步；这是 iOS 数据保护设计的第一性事实。
2. **默认即弱类**：第三方 App 数据的默认类为 `NSFileProtectionCompleteUntilFirstUserAuthentication`，其类密钥在上锁后不移除。清单里"未指派"必须按此档计。
3. **备份是最主要的密钥域降级通道**：iCloud 备份以原文包含 device settings、app data、Camera Roll 照片视频、Messages 会话；锁定态不可访问类的 per-file key 改由 Curve25519 的 iCloud Backup keybag 包裹上传，不经 iCloud 口令保护，**改口令不使旧备份失效**。
4. **端到端覆盖有一张"逐项清单"和两个"数量口径"**：支持页 102651 为 15→25，《PSG》（26.5 版）为 14→23；逐项清单可靠，计数会漂移。
5. **ADP 是有孔洞的覆盖图**：互通性（Mail/Contacts/Calendars）、协作中介、链接分享、Web 授权窗口、开发者未声明的 CloudKit 字段、排序与去重元数据都在孔洞内；Managed Apple Accounts 与儿童账户（按地区）不适用 ADP。
6. **少数类别的本机强度高于其内容类同行**：DAN 存于 Secure Element 且不入服务器与备份；生物特征表征不入备份；本地钥匙串备份由 SEP UID 根密钥派生密钥密封，只能还原到同一设备。
7. **派生数据不继承输入的保护档**，且其分类学地位是"新资产"：健康中心库与管理数据分处两类是已核实例证；索引与向量应视为内容的压缩形态而非元数据。
8. **删除是四条互不蕴含的指令**（本机、云端副本、云端备份集、他方），而苹果一手材料只支持密钥级删除表述，闪存级残留属未披露。
9. **多设备是清单的必需维度**：HomeKit、CarPlay、Watch、AirPods、Apple TV、Vision 类设备各自引入新持有者、新明文态（home hub）或新未披露面（车机、空间数据）；家庭共享与 Health Sharing 把"另一个自然人"变成合法持有者。
10. **元数据是系统性短板**：时间戳与 APNs 路由信息不加密、离线队列可在 Apple 服务器上最长保存 30 天、IDS 存放公钥/地址/关联电话号码。
11. **授权模型决定"能否访问"，不决定"结果如何留存"**：撤销无追溯效力，唯一被文档化的近似机制是 SiriKit 的三个保留终点。
12. **MDM 采集面是授权式而非枚举式**，两个方向的过度确定都缺乏一手依据；ADE 设备证明环节存在标识暴露（freshness code 不做验证即包含）。
13. **令牌化示范了"架构压低暴露"的路径**：敏感度最高（T4）的类别可以因可聚合性与可恢复性双低而成为暴露最窄的行。
14. **证据基线本身是发现的一部分**：Apple Intelligence 与 Private Cloud Compute、外设数据面、隐私清单、Lockdown Mode 逐条清单在 26.5 版《PSG》与本次已加载页面中均无对应条款，这些空格是可审计的未知，而非无风险。

## 19.2 四问模型在全资产域上的适用结果

把第 1.4 节四问施加到附录B 的 75 行上，得到三个统计性结论。**问一（在哪里）**的答案分布显示，"存储位置"至少有八种基质：文件系统容器、数据库、钥匙串、Secure Enclave、Secure Element、索引/向量库、iCloud 服务域、备份集，外加伴生设备与第三方后端两类域外基质——任何只列"本机/云端"二分的清单都不够用。**问二（受什么保护）**的答案里，可精确写出类名或属性名的行约占三分之一，其余需要写"未披露"或"按默认档假定"，这解释了为什么清单必须以三值置信标记为字段而非脚注。**问三（怎么离开）**的七条路径（同步、备份、共享/链接、诊断、MDM、伴生链路、法务获取）中，只有前两类有可引用的密钥归属条款，后五类的服务端记录状态几乎全部未披露。**问四（删了还剩什么）**的可判定项最少，且几乎全部是密钥级判定而非内容级判定——这是全报告最集中的知识缺口，也是 19.4 的首要研究问题。

## 19.3 强保护与弱保护：一张对照收尾

| 维度 | 强（可辩护） | 弱（需补偿控制） |
|---|---|---|
| 存储基质 | Secure Element / Secure Enclave 内对象 | 第三方容器内的 Class C 文件、索引与缓存 |
| 密钥归属 | 第一档端到端类别；`*ThisDeviceOnly` 凭证 | 备份集（HSM、available-after-authentication）；互通性三服务 |
| 生命周期 | 有明确终点的机制（SiriKit、Class B 内存清除、ADP 密钥轮换） | 账户侧长记忆、他方持有、备份中的旧副本 |
| 访问控制 | 逐类别逐读写授权、代码签名 + access group 强制 | 授权撤销无追溯、营养标签为自我声明 |
| 攻击面 | Lockdown Mode 可选、BlastDoor 类处理隔离（另篇评估） | 元数据、外设域、供应链与受管域 |

## 19.4 未决研究问题

八个问题在本报告的证据基线内无法回答，值得后续专项核验：**一**，索引与派生层（照片标签、建议模型、摘要、向量）的实际 Data Protection 档与是否随备份流转。**二**，文件内容级/闪存级残留的可恢复性边界——需要独立于厂商声明的研究。**三**，第三方 App 推送载荷在 Apple 侧的留存与可见字段。**四**，MDM 命令级可采集字段的完整枚举（须逐条核对协议参考）。**五**，CarPlay/AirPods/Vision 外设的数据面清单，含空间映射、手部与眼动追踪、Persona 与空间捕捉。**六**，Apple Intelligence 与服务器侧计算形态的数据边界（须另找一手页面并核对版本）。**七**，HomeKit Secure Video 的保留天数与事件上限。**八**，ADP 元数据端到端承诺（"承诺让更多此类元数据端到端加密"）的实际落地范围与时间表。

## 19.5 方法与局限复核

本报告的方法承诺是：机制陈述附文档名与版本文号；未加载原文处用限定语；法律映射标为分析性归纳；不写利用流程与获取步骤。其局限同样明确：《PSG》版本为 26.5 而系统最新为 27.0.1，二者间的数据类别语义差异本次未逐项核对；支持页类别计数与《PSG》不一致处已并列保留；机型可用性以文档为限；监管映射未经验证。读者在据此制定控制措施时，应把每一个"公开资料未披露"当作**需要监测的开放项**，而不是当作可以忽略的空格——这既是本报告的结论，也是它的方法。

---

# 附录

## 附录A 术语表

本表以第 10–19 章新增术语为主（第 1.6 节已给出 Data Protection、keybag、APNs、CloudKit 等基础术语的速查，此处不重复其定义而只做补充与中英对照）。

| # | 中文 | 英文 | 在本报告中的含义 |
|---|---|---|---|
| 1 | 数据容器 | data container | App 的用户数据写入区，第三方数据默认保护档所在 |
| 2 | 包容器 | bundle container | 代码与资源所在，受代码签名覆盖 |
| 3 | 应用组容器 | app group container | 同组 App 与扩展共享的文件区，边界由签名强制 |
| 4 | 访问组 | access group | 钥匙串与组容器的共享许可单元，由开发者前缀限定 |
| 5 | 文件提供程序 | File Provider | 云盘类 App 在文件 App 中的物化/占位机制 |
| 6 | 钥匙串项类别 | keychain item class（generic password 等） | 凭证项的类型划分，属性名以开发者文档为准 |
| 7 | 可访问性属性 | keychain accessibility（`kSecAttrAccessible*`） | 决定凭证何时可被读取的钥匙串属性 |
| 8 | 仅本机变体 | `*ThisDeviceOnly` | 不同步、不备份的一类属性取值；被逐条核实的取值不入 escrow keybag |
| 9 | 托管密钥袋 | escrow keybag | 涉证/口令恢复密钥结构；暴露类密钥列表（Key UUID 与所属类） |
| 10 | 备份密钥袋 | iCloud Backup keybag | Curve25519 非对称密钥，包裹上传文件的 per-file key |
| 11 | 认证后可用密钥 | available-after-authentication key | 默认存于苹果数据中心 HSM 的 iCloud 备份密钥类别 |
| 12 | 密钥域 | key domain | 同一份内容在不同位置所归属的密钥保管集合（本报告的核心分析单位） |
| 13 | 保护档分层 | protection tier（P0–P6） | 本报告对机制实际强度的分层，与敏感度层级 T 相乘使用 |
| 14 | 敏感度层级 | sensitivity tier（T1–T4） | 第 2.2 节定义的内容敏感度分级 |
| 15 | 可聚合性因子 | aggregability | 四因子法中衡量联合推断能力的因子 |
| 16 | 可恢复性因子 | recoverability | 衡量删除动作实际效力的因子 |
| 17 | 监管权重因子 | regulatory weight | 法定归类加重程度（分析性归纳） |
| 18 | 派生即新资产 | derivation-as-new-asset | 本报告规则：派生操作产生新资产行，不继承输入档位 |
| 19 | 标签分级 | label taxonomy（L0–L5） | 第 14.6 节的派生标签治理分类法 |
| 20 | 再识别 | re-identification | 由派生数据或标识把匿名/隔离数据重绑到自然人 |
| 21 | 收敛加密 | convergent encryption | 以校验和实现去重；苹果据此说明校验和不端到端 |
| 22 | 互通性例外 | interoperability exception | Mail/Contacts/Calendars 因协议互通而无法端到端 |
| 23 | 链接分享 | "anyone with a link" | 内容以标准保护交付 Apple 服务器，且分享标题与代表缩略图独立留存 |
| 24 | 副本发送 | Send a Copy（Freeform） | 链接有效期内以未加密状态存放的场景 |
| 25 | 网页授权武装 | iCloud.com arming | 开启 ADP 后授权 Web 访问时设备被"武装"1 小时的机制 |
| 26 | 允许清单 | allow-list | 授权期间仅接受针对 iCloud.com 可见服务的密钥上传 |
| 27 | 恢复材料 | recovery contact / recovery key | ADP 前提之一；恢复失败时 Apple 无法帮助恢复端到端数据 |
| 28 | 身份服务目录 | Apple Identity Service (IDS) directory | 存放 iMessage 公钥、APNs 地址与关联电话号码 |
| 29 | 家庭中枢 | home hub | 承担 HSV 本地分析与中继的设备，链路的明文态所在 |
| 30 | 安全视频 | HomeKit Secure Video (HSV) | 剪辑以 AES-256-GCM 加密上传、元数据经 CloudKit 端到端 |
| 31 | 人脸分类数据 | face classification data | 人名与分类图像，端到端存于 CloudKit 的派生资产 |
| 32 | 会话密钥派生 | per-session HKDF-SHA-512 | 摄像头与 home hub 之间专用本地连接的密钥协商 |
| 33 | 设备账号 | Device Account Number (DAN) | 发卡方创建、存于 Secure Element、不入服务器与备份 |
| 34 | 一次性密文 | one-time cryptogram | 交易时与 DAN 一起计算的一次性支付凭证 |
| 35 | 证件类凭证 | ID-class Wallet pass（驾照/州身份证） | 可用性依地区与机型；呈现式使用与字段选择是评估要点 |
| 36 | 受管注册 | MDM enrollment | User Enrollment / Device Enrollment / ADE 三形态决定权限与分离 |
| 37 | 设备证明 | device attestation | ADE 流程中由 Apple Enterprise Attestation Root CA 签发证书的环节 |
| 38 | 新鲜度码 | freshness code | 证明中不做验证即包含的属性值，构建设备标识暴露面 |
| 39 | 逐类别授权 | per-category authorization | 系统按数据类型与读写分别许可的模型 |
| 40 | 消息处理隔离 | BlastDoor | 苹果公开会议材料所述的消息处理沙盒化设计（不在本次一手条款内） |
| 41 | 锁定模式 | Lockdown Mode | 面向针对性雇佣间谍软件风险的最小化配置，开启时 Private Relay 不工作 |
| 42 | 追踪授权 | App Tracking Transparency (ATT) | `ATTrackingManager` 等符号所代表的追踪门控 |
| 43 | 归因回传 | postback（AdAttributionKit） | 安装/转化回传机制，与追踪授权分属不同数据集合 |
| 44 | 隐私标签 | App Privacy Details | 开发者在 App Store 的自我声明矩阵，非法定分类 |
| 45 | 生命周期七阶段 | lifecycle stages | 产生→处理→存储→云复制→共享→删除→获取 |

## 附录B 数据类别 × 存储位置 × 保护级别 × 是否离开设备 对照矩阵

填写约定：**保护列**只写可引用的类名/属性名，未披露处写「未披露」；**备份列**指 iCloud 备份（除注明本地备份者）；**同步列**指是否进入 iCloud 服务域；**端到端列**取值"已是（标准档）/可（开启 ADP）/否（互通性或协作例外）/未披露"；**监管列**为分析性归纳，非苹果声明。

| # | 数据类别 | 主要存储位置 | 默认 Data Protection / Keychain 保护 | 是否进入备份 | 是否同步到云 | 是否可端到端加密 | 敏感度层级 | 监管归类（分析性） |
|---|---|---|---|---|---|---|---|---|
| 1 | Apple Account 标识与账户工件 | 账户服务域 + 本机账户记录 | 未披露（本机侧非文件类） | 部分（device settings） | 是 | 否（账户结构数据） | T2 | 个人信息 |
| 2 | 设备序列号/UDID 类硬件标识 | 系统域、注册与证明流程 | 未披露 | 是（device settings 范围内） | 是 | 否 | T2 | 个人信息（设备标识） |
| 3 | IDFV 与归因回传数据（AdAttributionKit postback） | App 容器/系统 API；归因数据另在开发者与广告网络后端 | 文件按默认 Class C；归因数据不受 iOS 保护类管辖 | 是（app data）；归因不入备份 | 视 App；归因非 iCloud 路径 | 未披露 | T2 | 个人信息（视声明） |
| 4 | 通讯录（他人条目与自身身份卡） | Contacts 数据 + 同步域 | 未披露（系统域类档） | 是（走同步域而非 app data） | 是 | **否（互通性例外）** | T3 | 个人信息 |
| 5 | 日历事件 | Calendar 数据 + 同步域 | 未披露 | 是 | 是 | **否（CalDAV 互通）** | T2/T3 | 个人信息 |
| 6 | 邮件正文与附件 | Mail 服务域 + App 容器 | Class C（App 侧缓存） | 是（App 缓存） | 是 | **否**；S/MIME 为可选替代 | T3 | 个人信息（可能含特殊类别） |
| 7 | iMessage 正文 | 消息数据库 + Messages in iCloud | 未逐项披露（内容级） | 视开关（开启 iCloud 消息时不入备份） | 是 | 已是（但开启 iCloud 备份时密钥副本入备份） | T3 | 个人信息（通信内容） |
| 8 | 消息附件 | iCloud（AES-CTR + 随机 256-bit 密钥） | 不适用（对象存储） | 视开关 | 是 | 部分（密钥容器语义见 13.6） | T3 | 个人信息 |
| 9 | 消息元数据（时间戳、路由） | 服务端与设备 | 明文条款：**不加密** | 不适用 | 是 | 否 | T3 | 个人信息（流量数据） |
| 10 | IDS 目录项（公钥、APNs 地址、电话号码） | Apple Identity Service | 不适用 | 不适用 | 是 | 否（未声明端到端） | T3 | 个人信息（标识与地址） |
| 11 | RCS 消息 | 消息数据库 + 运营商/平台侧 | 未披露 | 视开关 | 部分（服务器留存未披露） | 可（iOS 26.5 起 beta，条件为支持承运商与对端版本） | T3 | 个人信息（通信内容） |
| 12 | 通话记录与 FaceTime 记录 | 系统数据库 | 未披露 | 是 | 部分 | 未披露 | T2 | 个人信息 |
| 13 | 语音留言（Voice Memos） | 系统容器 + iCloud | 未披露（系统域） | 开启 iCloud 功能时不入备份 | 是 | 可（ADP） | T3 | 个人信息（含声纹可能） |
| 14 | 通知历史 | 系统通知存储 | 未披露 | 是（视 App 数据） | 否 | 未披露 | T2/T3（可含正文） | 个人信息 |
| 15 | 位置历史与轨迹 | Maps/系统位置域 | 未披露（Maps 行端到端） | 是 | 是 | 已是（Maps 列标准即端到端） | T3 | 敏感个人信息（行踪轨迹） |
| 16 | 共享位置 / Find My | 设备与账户关系域 | 不适用（服务侧） | 不适用 | 是 | 未逐项披露 | T3 | 个人信息（行踪） |
| 17 | 照片原片（Camera Roll） | 媒体库 + 容器 | 系统域档；第三方按 Class C | 是（未开 iCloud Photos 时） | 视开关 | 可（ADP；未开启时密钥在 Apple） | T3 | 个人信息（可能含生物特征） |
| 18 | 视频与屏幕录制 | 媒体库 | 同上 | 同上 | 视开关 | 可（ADP） | T3 | 个人信息 |
| 19 | 照片派生标签、人脸/场景索引、回忆编排与 OCR 文本 | 索引库与临时缓存（落点未披露） | 未披露 → 按 Class C 假定 | 可能（索引随 app data） | 未披露 | 未披露 | T3 | 敏感个人信息（生物特征识别） |
| 20 | 相机拍摄参数与内嵌位置 | 媒体文件元数据 | 随文件（Class C 或系统档） | 是 | 是 | 随文件（ADP 下端到端） | T2/T3 | 个人信息 |
| 21 | 听写与 Siri 转写 | 服务侧与本机 | 未披露 | 不适用 | 是 | 已是（Siri information 标准即端到端） | T3 | 敏感个人信息（可能） |
| 22 | SiriKit 提交的 App/用户词汇 | Siri 域 | 未披露 | 不适用 | 是 | 未披露 | T2 | 个人信息 |
| 23 | 键盘个人词典与预测模型 | 系统输入域 | 未披露 | 是（device settings 范围） | 是 | 已是（标准即端到端） | T3 | 个人信息 |
| 24 | Safari 浏览历史与隐私浏览会话 | Safari 数据域（隐私会话条款未加载） | 未披露 | 视开关 | 是 | 已是（Safari 行标准即端到端）；隐私会话未披露 | T3 | 个人信息（行为数据） |
| 25 | 网站数据（Cookie、localStorage、IndexedDB） | Safari 或 App 内 WebView 容器 | App 侧默认 Class C | 是（app data 时） | 部分 | 部分（Safari 端到端；App 侧由备份档决定） | T2/T3 | 个人信息 |
| 26 | Safari 书签 | Safari 数据域 | 未披露 | 视开关 | 是 | 可（ADP 新增项） | T2 | 个人信息 |
| 27 | iCloud Drive 与文件 App 文档 | 文件域 + iCloud Drive | 按写入方指派；第三方默认 Class C | 是（app data） | 是 | 可（ADP） | T2/T3 | 视内容 |
| 28 | 备忘录 | Notes 容器/服务域 | 未披露 | 是（本地存储的备忘录会被包含） | 是 | 可（ADP） | T3 | 视内容（可能特殊类别） |
| 29 | 提醒事项 | 提醒数据 + CalDAV 部分 | 未披露 | 视开关 | 是 | 可（ADP），但 CalDAV 同步部分除外 | T2 | 个人信息 |
| 30 | 快捷指令与自动化脚本 | Shortcuts 数据域 | 未披露 | 是 | 是 | 可（ADP） | T2（可读取其他类别） | 视内容 |
| 31 | Freeform 画板 | 服务域 + 容器 | 未披露 | 是 | 是 | 可（ADP）；**Send a Copy 链接期内未加密** | T2/T3 | 视内容 |
| 32 | Journal 日志内容 | Journal 数据域 | 未披露 | 视开关 | 是 | 已是（Journal data 标准即端到端） | T4 | 个人信息（高度主观） |
| 33 | 第三方 App 数据文件 | App 数据容器 | `NSFileProtectionCompleteUntilFirstUserAuthentication`（默认） | **是（app data）** | 视 App | 否（由 App 自行决定） | T3 | 视内容 |
| 34 | App 数据库与缓存、日志 | 容器内 Database/Cache/Logs | 同上；日志档未披露 | 可能（细项未披露） | 视 App | 未披露 | T2/T3 | 个人信息 |
| 35 | App Group 共享容器 | 组容器 | 由写入方指派（不继承原件档） | 是 | 视 App | 未披露 | T3 | 视内容 |
| 36 | App 凭证令牌（钥匙串通用密码） | 钥匙串 | 常为 `kSecAttrAccessibleAfterFirstUnlock` | 是（非 ThisDeviceOnly 时） | 可（iCloud Keychain） | 否（视属性） | T3 | 个人信息（认证数据） |
| 37 | `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly` 机器本密凭证 | 钥匙串（仅 system keybag） | 该属性 | **否**（明文不入备份） | **否**（不 iCloud 同步） | 不适用（结构性隔离） | T4 | 认证数据 |
| 38 | iCloud Keychain 密码与通行密钥 | 钥匙串 + iCloud Keychain 保护域 | 钥匙串项 + 端到端 | 本机钥匙串可入备份但被 SEP UID 派生密钥密封（仅同机还原） | 是 | **已是**（标准即端到端） | T4 | 认证数据（特殊） |
| 39 | HealthKit 中心库 | 健康数据库 | Protected Unless Open（锁 10 分钟后不可访问） | 本地加密备份才含健康数据 | 是 | **已是**（Health data 标准即端到端，需 iOS 12+ 与双重认证） | T4 | 敏感个人信息/特殊类别 |
| 40 | HealthKit 管理数据（授权、连接设备、调度） | 健康管理数据 | Protected Until First User Authentication | 视开关 | 是 | 未逐项披露 | T3 | 个人信息（健康关联） |
| 41 | Medical ID | 健康 App 记录 | **No Protection 类**（锁屏可访问） | iOS 13 及更早入 iCloud 备份；iOS 14 起走 CloudKit | 是（CloudKit） | 随健康数据加密特性 | T3 | 敏感个人信息 |
| 42 | 临床记录（机构摄取） | 健康数据库 | 同中心库；经 OAuth 2 与 TLS 1.3 摄取 | 同上 | 是 | 同健康数据 | T4 | 特殊类别（医疗健康） |
| 43 | 运动与健身记录（Watch 源） | HealthKit + Watch | 中心库档；`HKWorkoutSession` 期间锁屏可用 | 加密本地备份时含 | 是 | 已是 | T3 | 健康数据 |
| 44 | 睡眠、经期等推断 | HealthKit | 中心库档 | 同上 | 是 | 已是 | T4 | 特殊类别 |
| 45 | 健康共享与 Share with Provider | CloudKit 端到端容器 | 同中心库 | 不适用（双方各自） | 是（共享容器） | **已是**（Apple 无法访问、不保管密钥） | T4 | 特殊类别（含接收方义务） |
| 46 | Face ID 表征与部分采集影像 | Secure Enclave | 加密且仅 SEP 可用 | **否**（明文排除） | 否 | 不适用（不入任何副本） | T4 | 生物特征识别数据（特殊类别） |
| 47 | Touch ID 表征 | Secure Enclave | 同上 | **否** | 否 | 不适用 | T4 | 同上 |
| 48 | Optic ID 表征 | Secure Enclave + Secure Neural Engine | 同上（影像算出表征后即丢弃；本次为部分核实） | **否** | 否 | 不适用 | T4 | 同上 |
| 49 | Device Account Number | Secure Element | 硬件保管；加密方式使 Apple 无法访问 | **否**（不入备份、不在 Apple Pay 服务器） | 否 | 不适用 | T4 | 金融账户数据（特殊） |
| 50 | 一次性交易密文 | 会话态 | 单次有效 | 否 | 否 | 不适用 | T1 | 支付凭证（单次） |
| 51 | Wallet 通行证（票证、卡） | Wallet 数据 + 通行证域 | 未披露 | 是（Wallet passes 为 ADP 项） | 是 | 可（ADP） | T2/T3 | 个人信息 |
| 52 | 证件类凭证（驾照/州身份证） | Wallet + 发证机构 | 未披露；可用性依地区与机型（本次未加载条款） | 未披露 | 部分 | 未披露 | T4 | 法定身份/敏感个人信息 |
| 53 | 支付信息、Apple Card 交易与 Apple Cash（含家庭身份验证） | iCloud 支付域 + 支付与家庭关系域 | 未披露（Apple Cash 需 iCloud 家庭内已验证身份） | 不适用（Payment information 属端到端集合） | 是 | **已是**（标准即端到端）；Apple Cash 部分未披露 | T3 | 金融数据 + 关系数据 |
| 54 | 购买历史与收据 | Apple Account 侧 | 不适用 | **否**（已购内容不重复备份） | 是 | 否（账户结构数据） | T2 | 个人信息（消费记录） |
| 55 | HomeKit 配件与家配置 | Home 数据域 + home hub | 未披露 | 视备份 | 是 | **已是**（Home data 标准即端到端） | T2/T3 | 个人信息（居住信息） |
| 56 | 自动化触发时间线 | Home 数据/日志 | 未披露 | 未披露 | 未披露 | 未披露 | T2 | 个人信息（在离场推断） |
| 57 | HSV 视频剪辑与 poster | 摄像头→home hub→iCloud | 上传前 AES-256-GCM；剪辑关闭后明文即弃 | 不适用（不计入 iCloud 容量的独立域） | 是 | **已是**（密钥经 CloudKit 端到端；保存天数未确认） | T3 | 个人信息（住宅影像） |
| 58 | HSV 人脸分类数据 | CloudKit | 端到端加密存储 | 不适用 | 是 | **已是** | T4 | 生物特征识别数据 |
| 59 | CarPlay 会话与车机侧缓存 | 车机（域外） | **不受 iOS Data Protection 管辖** | 否（车厂域） | 否 | 数据类别未逐项披露 | T2/T3 | 转移至第三方控制者 |
| 60 | Apple Watch 配对与备份关系 | iPhone 备份 + Watch | 未披露 | 是（还原可保留既有配对） | 是 | 部分 | T2 | 个人信息 |
| 61 | Watch 通知与通话中继 | APNs 通道 | 不适用（传输中） | 否 | 部分 | **已是**（Continuity 通知端到端） | T2/T3 | 个人信息（通信） |
| 62 | W1/H1 蓝牙密钥 | 系统密钥域 + iCloud | 未披露 | 不适用 | 是 | **已是**（标准即端到端） | T2 | 设备关联数据 |
| 63 | AirPods 音频与听力相关数据 | 配件与 iPhone 侧 | 未披露（《PSG》无 AirPods 专章） | 未披露 | 未披露 | 未披露 | T2/T3 | 可能涉健康数据 |
| 64 | 空间映射（房间几何/锚点） | visionOS 设备 | 未披露；设备数据全量加密 | 未披露 | 未披露 | 未披露 | T3（分析性推断） | 可能涉住宅信息 |
| 65 | 手部与眼动追踪派生序列 | visionOS 设备 | 未披露 | 未披露 | 未披露 | 未披露 | T3（分析性推断） | 可能涉生物特征 |
| 66 | 屏幕使用时间与类别聚合 | 系统 + 家庭共享 | 未披露 | 是（device settings 范围） | 是 | **已是**（标准即端到端） | T3 | 行为数据；儿童场景加重 |
| 67 | 诊断与分析数据、崩溃报告 | 系统分析域 + 本机日志 + 开发者后端 | 未披露 | 否（不属于用户内容备份集，按未披露处理）；崩溃上下文可能随日志 | 是（可关闭） | 否 | T2 | 可能含个人信息（分析性） |
| 68 | Spotlight/Siri 建议索引 | 系统索引库 | 未披露 → 按最低共同保护假定 | 未披露 | 部分 | 未披露 | T3 | 个人信息（聚合） |
| 69 | 派生摘要与语义向量 | 未披露 | 未披露 | 未披露 | 未披露 | 未披露 | T3（L4 档） | 视内容；可逆性未定 |
| 70 | 配置描述文件 | 系统配置域 | 未披露 | 是（device settings） | 是（MDM 下发） | 否 | T2/T3 | 企业数据 + 个人信息 |
| 71 | 已安装根/中间证书 | 信任存储 + 钥匙串 | 视密钥是否可导出；未披露 | 部分 | 是（随 profile） | 否 | T3（改变全部流量前提） | 安全控制数据 |
| 72 | MDM 受管域数据与查询响应 | 受管容器 + 企业后端 | User Enrollment 下加密隔离受管数据 | 视注册类型 | 是（受管通道） | 否 | T2/T3 | 雇佣关系数据（分析性） |
| 73 | iCloud 备份集（含 keybag） | Apple 数据中心 | 备份密钥默认在 HSM，available-after-authentication | 本身即备份 | 是 | **可**（开启 ADP） | T4（内容混合体） | 承载所有类别的容器 |
| 74 | 本地加密/未加密备份 | 电脑介质 | backup keybag；未加密时健康数据不含 | 本身即备份 | 否 | 取决于口令 | T4 | 同上（介质风险） |
| 75 | 共享/链接中的内容 | Apple 服务器 + 接收方 | 标准保护（不随 ADP 改变） | 否 | 是 | **否**（协作与链接例外） | T3 | 已对外提供的个人信息 |

本表共 75 行。两处以脚注形式处理而不单列：**剪贴板**等非持久内存状态已在 8.6 与 16.1 讨论（不构成长期资产行）；**iCloud Mail 的账户设置**（入备份）与邮件正文（不入备份但属互通性域）已合并为第 6 行。凡"未披露"均为需要监测的开放项，不得读作"不具备该属性"。

## 附录C 数据资产盘点模板

模板以"一行一资产"为单位，字段设计直接支撑第 17 章的四因子与第 18 章的义务映射。

| 字段 | 取值域 | 填写要求 |
|---|---|---|
| 资产 ID / 类别名 | 文本 | 与附录B 行号对齐 |
| 所属主体域 | 用户 / App / 系统 / iCloud / 受管 / 伴生设备 / 第三方后端 | 决定后续列是否可判定 |
| 主要存储位置 | 容器类型、数据库、钥匙串、SEP/SE、索引库、服务域、备份集 | 允许多值（一份内容多处落点必须全列） |
| 保护档（可引用） | 类名 / accessibility 属性 / keybag 类型 / 端到端状态 | 未披露必须写「未披露」，不得留空 |
| 指派方 | 系统 / App 显式 / 未指派（按默认） | 未指派一律按 Class C |
| 出设备路径 | 同步/备份/共享/诊断/MDM/伴生/法务，七选多 | 禁止留空，无路径需注明依据 |
| ADP 敏感性 | 不变 / 未开→开 变化 / 不适用 | 对应 13.4、13.5 |
| 生命周期阶段 | 产生/处理/存储/复制/共享/删除/获取 | 至少标注当前阶段与最长阶段 |
| 删除语义 | 密钥级可判定 / 副本级不可判定 / 他方持有 | 对应 16.2 |
| 残留基质 | 缩略图/索引/日志/备份/他方/未披露 | 对应 16.3 |
| 派生关系 | 输入类别 → 本行；标签级别 L0–L5 | 对应 14.6 |
| 可聚合性 A / 可恢复性 R | 1–4 | 对应 17.2 |
| 监管归类 C | 分析性判定 + 依据缺失注 | 对应附录D |
| 置信标记 | 已核实 / 部分核实 / 未核实 | 与出处同现 |
| 出处 | 文档名 + 版本文号 | 《PSG》一律写"（26.5 版）" |

**示例行集（某健康管理 App，虚构但按真实机制填写）**

| 资产 | 位置 | 保护档（可引用） | 备份 | 同步 | 端到端 | 删除语义 | A/R/C | 置信 |
|---|---|---|---|---|---|---|---|---|
| 用户账号与会话 | 数据容器 + 钥匙串 | 文件默认 Class C；令牌 `kSecAttrAccessibleAfterFirstUnlock` | 是（app data） | 否（自有后端） | 否 | 本机删除不撤销服务端会话 | 4/3/3 | 已核实（档与备份条款）+ 未核实（服务端） |
| 同步到健康的记录 | HealthKit 中心库 | Protected Unless Open；锁 10 分钟后不可访问 | 加密本地备份时含 | 是（Health） | 已是（需 iOS 12+ 与双重认证） | 撤销授权只阻后续访问，不追溯已上传 | 4/2/4 | 已核实 |
| 授权状态与连接设备列表 | HealthKit 管理数据 | Protected Until First User Authentication | 可能 | 是 | 未逐项披露 | 未披露 | 3/3/3 | 已核实（类档）/未核实（删除） |
| 运动期间的锁屏显示 | 内存 + 临时 journal | journal 处于 Protected Unless Open，合并完成时删除 | 否（合并前窗口） | 否 | 不适用 | 有明文清除点（唯一范例） | 2/1/3 | 已核实 |
| 推送通知（含健康提示语） | App 侧 + APNs | 不受文件类管辖 | 否 | 否 | 未披露 | 送达即从 APNs 删除；一般推送留存未披露 | 2/2/3 | 部分核实 |
| 用户备注中的症状描述 | App 数据库 | Class C（未显式指派） | **是** | 视 App | 否 | 云端备份副本不受本机删除影响 | 4/3/4 | 已核实（默认类与备份） |

## 附录D 合规框架映射表

**注：本映射为分析性归纳，本次未核验任何法律条文页面；用途是把资产行对齐到法定概念以便启动讨论，不构成法律意见，也不代表 Apple 对自有数据的法定分类。** 表中 GDPR 指 Regulation (EU) 2016/679 之概念（personal data、special categories、pseudonymisation、controller/processor）；CCPA/CPRA 指加州消费者隐私权利法体系之概念（personal information、sensitive personal information、sale/share）；个保法指《中华人民共和国个人信息保护法》之概念（个人信息、敏感个人信息、单独同意、委托处理/共同处理）；NIST SP 800-122 指其 PII 与 re-identifiability 处置框架；ISO/IEC 27701 指其 PIMS 与 controller/processor 角色扩展。

| 资产类别族 | GDPR 概念 | CCPA/CPRA 概念 | 个保法概念 | NIST SP 800-122 | ISO 27701 处置要点 |
|---|---|---|---|---|---|
| 身份与账户标识 | personal data；标识符即充分 | personal information（identifier） | 个人信息 | directly / linked PII | 控制者识别与目的登记 |
| 设备与 App 标识（IDFV、归因） | online identifier | 视商业用途 | 个人信息（可关联） | re-identifiability 高 | 处理者义务与下游共享约束 |
| 通信内容与元数据 | personal data；元数据亦属 traffic data 议题 | personal information | 通信内容属敏感范畴（分析性） | 内容 + 关联数据 | 访问控制与保留期（Art.30 类记录） |
| 位置与轨迹 | personal data；批量轨迹风险高 | personal information（geolocation） | 行踪轨迹为敏感个人信息 | 高再识别性 | 数据最小化与目的限制 |
| 健康与健身 | special category（Art.9） | sensitive personal information（health） | 健康信息为敏感个人信息 | 敏感 PII | 需单独同意式机制与 DPIA |
| 生物特征（表征/分类） | biometric data for unique identification（Art.9） | biometric information（CPRA 明文） | 生物识别信息为敏感个人信息 | 不可撤销标识 | 最高控制层 + 影响评估 |
| 支付与金融 | personal data + 金融监管并行 | financial information（敏感类） | 金融账户为敏感个人信息 | 敏感 PII | 分段控制者（发卡方/商户/平台） |
| 儿童与家庭共享 | 儿童数据特殊保护（Art.8 类） | 未成年人强化规则 | 未成年人为加重因素 | 需降低可聚合性 | 监护人同意的证据化 |
| 派生标签、摘要与向量 | 可能构成新 personal data；假名化非匿名化 | 由原数据推得的信息亦在范围内 | 推断信息亦属个人信息（分析性） | re-identification 风险载体 | 需登记派生关系与清除时机 |
| 凭证与密钥材料 | 安全控制措施（Art.32 类） | 安全事件相关定义 | 加密与去标识化义务对象 | 不直接适用 | 密钥域即控制边界（本报告 P0–P6） |
| 备份集 | 承载全部类别的处理活动 | 同 | 同 | 同 | 需按最内含类别定级，不按备份本身定级 |
| 受管域与 MDM 数据 | 雇佣关系下同意有效性受限 | 员工数据（CPRA 已扩展） | 人力资源管理场景 | 工作场所 PII | 必须与个人域分离（对应 User Enrollment） |
| 诊断与遥测 | 可能为 personal data（视标识与内容） | 视是否可关联 | 视是否可识别 | linked 或 retrievable | 目的限定 + 可退出证明 |

## 附录E 参考资料

本节仅列**本次核验中实际加载成功**的一手来源（与简报记录一致），以及被引用条款所指向的标准文档标题。未加载正文的页面一律标注限定；URL 仅在核验时被实际访问到的条目下列出。

| # | 标题 | 发布方 | 使用章节 | 链接 |
|---|---|---|---|---|
| 1 | Apple Platform Security（PDF，文内版本 iOS/iPadOS/macOS/tvOS/visionOS/watchOS 26.5） | Apple | 全篇机制主源（10–17 章） | https://help.apple.com/pdf/security/en_US/apple-platform-security-guide.pdf |
| 2 | Apple security releases | Apple | 1.3、附录F（27.0.1 无公开 CVE） | https://support.apple.com/en-us/100100 |
| 3 | About the security content of iOS 27 and iPadOS 27 | Apple | 10.6、14.2、17（缺陷登记） | https://support.apple.com/en-us/149034 |
| 4 | About the security content of iOS 26.2 and iPadOS 26.2 | Apple | 10.4（Safari 历史条目）、在野利用表述 | https://support.apple.com/en-us/125884 |
| 5 | About the security content of watchOS 7.6 | Apple | 9.1 相关（历史例证，不用于当前版本线） | https://support.apple.com/en-us/102763 |
| 6 | iCloud data security overview | Apple | 13.1、13.4、13.5（含完整表格） | https://support.apple.com/en-us/102651 |
| 7 | How to turn on Advanced Data Protection for iCloud | Apple | 13.4 | https://support.apple.com/en-us/108756 |
| 8 | If you're missing information after you restore your iPhone or iPad | Apple | 13.2（备份排除项） | https://support.apple.com/en-qa/102325 |
| 9 | About Lockdown Mode | Apple | 15.5（摘要级引用） | https://support.apple.com/en-us/105120 |
| 10 | End-to-end encrypted RCS messaging begins rolling out today in beta | Apple Newsroom | 16.4、附录F（iOS 26.5、beta 条件） | https://www.apple.com/newsroom/2026/05/end-to-end-encrypted-rcs-messaging-begins-rolling-out-today-in-beta/ |
| 11 | Apple Advertising & Privacy | Apple | 18.2（查看广告定向信息与开关位置） | https://www.apple.com/legal/privacy/data/en/apple-advertising/ |
| 12 | Privacy — Transparency Report | Apple | 18.5（Device / Financial Identifier Requests） | https://www.apple.com/legal/transparency/ |
| 13 | App Tracking Transparency（framework 文档） | Apple Developer | 18.2 | https://developer.apple.com/documentation/apptrackingtransparency |
| 14 | AdAttributionKit（framework 文档） | Apple Developer | 18.2 | https://developer.apple.com/documentation/adattributionkit |
| 15 | Sign in with Apple（framework 文档） | Apple Developer | 10.4 | https://developer.apple.com/documentation/signinwithapple |
| 16 | App Privacy Details — App Store | Apple Developer | 18.5（存在性与定位；类别枚举未核对） | https://developer.apple.com/app-store/app-privacy-details/ |
| 17 | DeviceCheck（含 App Attest） | Apple Developer | 10.6/15.4（设备完整性与令牌旁证） | https://developer.apple.com/documentation/devicecheck |
| 18 | Keychain Items：Restricting keychain item accessibility | Apple Developer | 10.3、15.4（**本次未成功加载正文**，仅按简报以开发者文档名义引用） | 无（未核验可达性） |
| 19 | Store encrypted security camera footage in iCloud with HomeKit Secure Video | Apple（iCloud 用户指南） | 11.2（**检索摘要级**，未加载正文，故此处不列链接） | 无 |
| 20 | Recommendation for Pairing-Based Cryptography（SP 800-56A） | NIST | 15.2（Class B 的 ECDH 与 KDF 构造，经《PSG》引用） | 无 |
| 21 | Guideline for Identifying Privacy and Security Risks to PII（SP 800-122） | NIST | 附录D（PII 与再识别概念） | 无 |
| 22 | Regulation (EU) 2016/679（GDPR） | 欧盟 | 附录D（**本次未核验条文**） | 无 |
| 23 | CCPA / CPRA（加州民法典相关条款） | 美国加利福尼亚州 | 附录D（**本次未核验条文**） | 无 |
| 24 | 《中华人民共和国个人信息保护法》 | 全国人民代表大会常务委员会 | 附录D（**本次未核验条文**） | 无 |
| 25 | ISO/IEC 27701（隐私信息管理体系） | ISO/IEC | 附录D（**未核验标准文本**，仅按公开标题引用） | 无 |

补充说明三条。**一**，本报告中"《Apple Platform Security》（26.5 版）描述……"一律指条目 1，不得读作 iOS 27 的行为；26.5 与 27.0.1 之间在数据类别语义上的差异本次未逐项核对。**二**，条目 18–25 或为未加载正文的引用名义条目，或未核验文本的法规/标准标题，凡据其得出结论的句子均已在正文中以「按××所述」「本次未核对」「分析性归纳」标注。**三**，**监管映射（附录D 与第 18 章）部分为分析性归纳**，不构成法律意见；第三方材料（安全厂商、独立研究博客）中的条目本次未加载原文，一律不进入本表，也不作为正文事实句。

## 附录F 机型与系统版本差异矩阵

本表回答"哪些数据类别的存在与否取决于硬件或系统版本"。凡标注"未加载条款原文"者，读者须以最新一手页面复核。

| 能力/类别 | 依赖条件（可引用） | 出处口径 | 对清单的影响 |
|---|---|---|---|
| iOS 27 支持机型 | iPhone 11 及以后；iPad Pro 12.9 第 4 代及以后、iPad Pro 11 第 2 代及以后、iPad Air 第 4 代及以后、iPad 第 9 代及以后、iPad mini 第 6 代及以后 | *About the security content of iOS 27 and iPadOS 27* | 缺陷登记（10.6、14.2、17）仅适用于此机型集合 |
| 当前版本线 | iOS/iPadOS 27.0.1（2026-09-28，无公开 CVE）；iOS 27 发布于 2026-09-14 | *Apple security releases* | 机制条款版本（26.5）与发布线（27.0.1）落差须并存 |
| 高级数据保护 ADP | iOS 16.2 / iPadOS 16.2 / macOS 13.1 / tvOS 16.2 / watchOS 9.2 及以后；账户需双重认证 + 设备密码 + 至少一个恢复联系人或恢复密钥 | 支持页 108756 与《PSG》（26.5 版） | 旧机型无法把第三档类别迁入端到端；恢复材料缺失即不可恢复 |
| ADP 不可用对象 | Managed Apple Accounts；儿童账户（因地区而异） | 支持页 102651 /《PSG》（26.5 版） | 受管与未成年场景必须另设补偿控制 |
| Lockdown Mode | iOS 16 / iPadOS 16 / macOS 13 / watchOS 10 及以后；iOS 17 / iPadOS 17 / macOS 14 与 watchOS 10.1 起提供额外防护 | 《PSG》（26.5 版） | 功能域清单随版本扩展；逐条清单为摘要级 |
| RCS 端到端加密 | iOS 26.5 起分批 beta；条件为支持该功能的承运商 + Android 侧最新版 Google Messages | Apple Newsroom（2026-05-11） | 对端与运营商决定同一类别是否端到端 |
| iMessage ECIES 替代 RSA | iOS 13 / iPadOS 13.1 及以后 | 《PSG》（26.5 版）§Messages | 老设备共存时加密构造不同（本报告不推断具体协商细节） |
| APNs 载荷上限 | 4 KB 或 16 KB，随 iOS/iPadOS 版本而异 | 《PSG》（26.5 版） | 附件外发行为随版本变化 |
| iCloud 照片与备份互斥的版本界 | iOS 8.1 / iPadOS 13.1 / OS X 10.10.3 | 《PSG》（26.5 版）§iCloud Backup security | 备份矩阵的行归属需按此界核对历史备份 |
| 健康数据端到端 | iOS 12 或更高 + 双重认证；满足后方迁移至端到端 | 《PSG》（26.5 版）§HealthKit | 迁移前历史副本的可读性为遗留问题 |
| Medical ID 落点 | iOS 13 及更早进入 iCloud 备份；iOS 14 及以后改用 CloudKit 同步 | 《PSG》（26.5 版） | 同一路径在不同世代设备上的备份归属不同 |
| Health Sharing | iOS 15+，双方均需双重认证 | 《PSG》（26.5 版） | 接收方版本决定共享数据的端到端状态 |
| 账户驱动的 Device Enrollment 数据分离 | iOS 17 / iPadOS 17 / macOS 14 / visionOS 1.1 起 | 《PSG》（26.5 版）§Device management | BYOD 分界强度取决于系统版本 |
| 自动抹除（多次失败） | iPhone、iPad、Apple Watch、Apple Vision Pro 均支持 | 《PSG》（26.5 版） | 政策类控制可用性跨设备一致 |
| Class D 支持范围 | iPad/iPhone/Apple Vision Pro 全部数据密文存储；**macOS 不支持 Class D** | 《PSG》（26.5 版）§Data Protection classes | 跨平台备份/迁移场景的保护档不可直接类比 |
| Optic ID 与虹膜数据 | 具备 Optic ID 的头戴设备；表征存于 SEP，不入备份 | 《PSG》（26.5 版） | 仅该类设备产生此行；空间派生数据未披露 |
| Apple Intelligence 机型 | 按苹果 iPhone 用户指南所述，Siri AI 以 beta 形式在 iOS 27 及以后、支持 Apple Intelligence 的 iPhone 15 Pro / 15 Pro Max / iPhone 16 及以后机型可用；第三方汇总另称含 iPhone Air / 16e / 17e | 部分核实（本次未逐条加载正文） | 相关数据外发与服务器侧计算边界不在本次证据基线（《PSG》26.5 无该章节） |
| 超宽带精确定位类能力 | 依赖具备相应硬件的机型；本次证据基线未包含相关条款 | 未核实（公开资料未逐项披露） | 该类别是否产生可留存数据，须另行核验后再入清单 |
| PassKit 能力查询 | App 可查询用户是否有可用于 Apple Pay 的卡 | 《PSG》（26.5 版） | "布尔型设备状态"应作为独立元数据行 |
| Face ID 与口罩模式 | 开启后有效期 6.5 小时；含口罩模式的误匹配率表述 | 《PSG》（26.5 版） | 影响"生物特征解锁类密钥"的实际窗口长度 |
| 旧系统维护线 | 同一发布列表可见 iOS 26.7 / 26.5.2 等条目及 iPadOS 15.8.8 类长期条目 | 部分核实（未逐条核对） | 长期支持设备仍可能停留在较弱的机制版本 |

**联系方式**：Telegram: https://t.me/ios_one001
