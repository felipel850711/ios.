@@TITLE@@管理相机、麦克风、照片等多类隐私访问授权
@@SUBTITLE@@授权状态如何产生、被谁变更、怎样撤销，以及用户/开发者/管理式三条治理路径
@@INFO@@报告日期|2026 年 10 月 21 日
@@INFO@@分析对象|Apple 平台上隐私敏感资源的访问授权（用途描述键、授权状态、同意管理、管理式限制）
@@INFO@@视角定位|授权状态的完整生命周期与三条治理路径（不是能力键目录，也不是围栏层次）
@@INFO@@分类|平台安全机制分析（防御性 / 教育性）
@@INFO@@难度|中（以文档比对与术语澄清为主）
@@INFO@@联系方式|Telegram: https://t.me/ios_one001
@@TOC@@目录

# 0. 概述、分工与口径

## 0.1 本篇要回答的问题

"管理相机、麦克风、照片等多类隐私访问授权"这句话里，真正的宾语不是"相机"，而是**授权状态**——一个由四个动作维系的对象：

1. **产生**：谁在什么时机把"未决定"变成"已授权/已拒绝"？提示语由谁写、写在哪里、由谁审？
2. **变更**：同一次访问在只读/读写/仅添加/选中的照片/使用期间/始终之间如何迁移？谁能推动迁移？
3. **撤销**：用户撤销、系统重置、家长或管理员限制、硬件断联，四种撤销路径的作用范围与粒度各是什么？
4. **可观测**：一个授权状态在设备上不打开任何界面时能否被第三方判定？答案在多数类别上是否定的。

本篇按这四问组织，并把"谁在管"拆成三条并列路径：用户侧（设置）、开发者侧（API 与声明）、管理侧（MDM 与屏幕时间）。三条路径管理的是同一个状态，但可见范围、粒度与优先级并不相同。

## 0.2 与系列已有篇目的分工

| 已有篇目 | 它回答的问题 | 本篇从它哪里接手 |
| --- | --- | --- |
| 《iOS 程序可使用的特定能力与权限分析》（#86） | 能力目录：有哪些授权键、合成链与门槛档位 | 键之后的**同意状态**：产生、迁移、撤销 |
| 《iOS 限制应用访问文件进程与系统资源的机制分析》（#87） | 六道围栏的层次与判定链，其中"同意型"是一层 | 把同意型这一层**单独放大**为完整生命周期 |
| 《iOS 代码签名验证与相关授权属性》（#84） | 授权属性作为被校验对象 | 用途描述键为何随签名一起固化 |
| 《iOS 代码签名与完整性执行体系组件分析》（#85） | 由哪些组件执行校验 | 由谁读写授权状态（除内核外的治理面） |
| 《iOS 数据采集能力深度分析》 | App 能采到哪些数据 | 采集前置的**许可闸门** |

一句话定位：#86 回答"App 被允许请求什么"，#87 回答"请求经过哪几道围栏"，**本篇回答"同意这件事本身如何被管理"**——它是一份状态机与治理面清单，而不是键目录。

## 0.3 证据分级与三条纪律

| 级别 | 含义 | 用法 |
| --- | --- | --- |
| A | Apple 官方页面正文的直接陈述（《Apple 平台安全保护》中文版、Apple 开发者文档） | 逐句引用，保留原限定语 |
| B | Apple 官方出现但未在正文展开（截图说明、WWDC 会话链接、支持文章） | 引用并注明层级 |
| C | 社区/第三方对内部实现（如存储路径、守护进程）的描述 | 不并入正文，仅入冲突表 |
| D | 无来源推断 | 只进"未披露"表 |

三条纪律：

- **纪律一（限定语守恒）**："必须""会""可能""设计可阻止""旨在"按其原始强度转述；不把"记住选择"升级为"不可更改"。
- **纪律二（平台不外推）**：iOS/iPadOS/visionOS 与 macOS 的同意面在类别、路径、粒度上都不同；两处的 A 级句子分列。
- **纪律三（负面陈述标注）**：写作"本次核查覆盖页面内未出现"，不写"Apple 未实现"。

## 0.4 本篇的强结论预告

四条可在 A 级来源直接支撑、但常被口语化表述掩盖的结论：

1. **同意是一次性记忆，不是每次判定**：「If the person grants permission, the system remembers the person's choice and doesn't prompt again.」因此授权状态是**持久对象**，需要被管理，而不是一个瞬时判断。
2. **`restricted` 与 `denied` 是两种不同来源的拒绝**：前者是「the user can't grant such permission」（用户无权改变），后者是用户明确拒绝。把两者合并称为"没权限"会掩盖管理式限制的存在。
3. **撤销有四条独立路径，作用范围差一个数量级**：iOS 的重置是**全设备全服务**，macOS 的 `tccutil reset` 是**按服务、影响所有 App**，用户的设置撤销是**按 App**，硬件麦克风断联是**按物理状态**。
4. **反推断设计确实存在于健康数据**：App「不能完全确定其是否获得了读取健康数据的权限」，无读取权限时「所有查询都不会返回数据，如同查询空数据库」。这是公开文档中少见的、以"阻止推断"为目的的授权语义。

# 1. 三类授权与四类管理者

## 1.1 先把三个词分开

| 词 | 载体 | 谁能给 | 能否被用户看到 | 能否被撤销 |
| --- | --- | --- | --- | --- |
| 授权属性（entitlement） | 代码签名内的键值对 | Apple 签发/开发者在 Xcode 声明并由账户与描述文件限定 | 需 `codesign` 才可见，不在设置中 | 只有重签名 |
| 用户同意（consent） | 系统内按 App×类别存储的授权状态 | 用户（或家长/管理员预先关闭通道） | 设置中的隐私界面 | 可（见 1.3） |
| 用途描述（purpose string） | App `Info.plist` 内的字符串 | 开发者撰写，App Review 审查 | 出现在系统提示中 | 随版本更新 |

三者关系是链式的，不是替代关系：用途描述让提示有意义，同意让访问放行，授权属性决定这条通道是否可能存在。「在获得许可之外，某些情况下你还必须另行声明意图，即在 App 中添加授权属性」是 A 级原文对二者并存的最短说明。

## 1.2 默认拒绝原则（A 级根陈述）

A 级（Requesting access to protected resources）：

- 「Apple's operating systems restrict access to protected data and resources by default. Apps can request access on a case-by-case basis, providing an explanation for why they need access. The person who uses the app decides whether to grant or deny the request.」
- 「Ensure your app accesses only what it needs to do its job.」
- 「Before your app can access certain protected resources, like the Bluetooth interface, location information, or the user's photos, the system asks the user for permission on behalf of your app.」

第三句的 "on behalf of your app" 值得单独注意：**提示由系统代替 App 生成**，标题亦由系统自动生成（「The system automatically generates the prompt's title, which includes the name of your app.」）。这意味着提示的可信面不取决于 App，App 只能提供正文一句话——这与 #87 中"面板由独立进程展示"是同一种设计思路。

## 1.3 四类管理者及其可管范围

| 管理者 | 可管范围 | 粒度 | 依据 |
| --- | --- | --- | --- |
| 用户 | 逐 App 授予/撤销；逐类别查看；部分类别可"仅一次/使用期间/选中照片" | App × 类别（照片可到资产集） | A：设置中的查看与撤销；`secc01781f46` |
| 开发者 | 撰写用途描述、选择请求时机、选择请求的访问级别、抑制自动提示、重置自身测试环境 | App 内 | A：purpose string 规范、PhotoKit 提示抑制 |
| 家长（家人共享组织者） | 查看与控制孩子设备的屏幕时间设置；须设密码使孩子无法更改；成年后可关闭监控 | 设备 × 功能 | A：`secd8831e732` |
| 组织（MDM） | 以配置负载下发设置与访问限制；受监督设备可把描述文件锁定到设备 | 设备/账户 × 策略 | A：`secf6fb9f053` |
| 硬件本身 | 机盖合上后在硬件层断联麦克风，不受任何软件授权影响 | 物理状态 | A：`secbbd20b00b` |

表中有意的"不对称"：用户可撤销的，管理员可以预先禁止其存在（`restricted`）；开发者可决定何时提示，但无法决定提示是否出现第二次；硬件断联不由任何一方管理，因而成为唯一**不可被软件授权覆盖**的一档。

# 2. 授权的产生：一次提示的完整链路

## 2.1 触发条件与时序

A 级要点（按原文时序整理）：

1. 「The first time your app attempts to access a protected resource, the system prompts the person using the app for permission.」——触发点是**第一次尝试访问**，不是安装、不是启动。
2. 未决定态：「If the user hasn't granted or denied capture permission, the authorization status is `AVAuthorizationStatus.notDetermined`.」
3. 显式请求：「use the `requestAccess(for:completionHandler:)` method to tell the system to prompt the user」；PhotoKit 侧为 `PHPhotoLibrary.requestAuthorization(for:handler:)`。
4. 系统也会自动请求：「the system automatically and asynchronously prompts the user for it」。
5. 记忆与不再提示：「After the user sets the app's authorization status, the system remembers their choice and won't prompt them again.」
6. 拒绝后的形态：「the access attempt that initiates the prompt, and any further attempts, fail in a resource-specific way.」

第 6 条是"失败方式随资源不同"的官方表述——它没有承诺统一的错误码，因此本篇不能把"被拒"写成一种可编程识别的通用状态。第 5 条与"改变主意"合看（「the user can change this choice at any time using the Settings app」）导出对开发者的硬约束（见 10.1）。

## 2.2 用途描述：内容、位置与技术下限

A 级（Provide a purpose string / Adhere to the requirements）：

- 定义句：「You supply a message called a *purpose string* or a *usage description* … to indicate the reason that your app needs the access.」
- 有效性四项：非空且不仅由空白字符组成；「The purpose string is shorter than 4,000 bytes」；类型与对应键要求一致（通常为字符串）；「accurate, meaningful, and specific about why the app needs to access the protected resource」。
- 缺失的后果：「Always provide a valid purpose string … If you don't, attempts to access the resource fail, and might cause your app to crash.」PhotoKit 侧更强：「Attempting to access the Photos library without a valid usage description causes your app to crash.」
- 落点：Xcode 在构建设置中以 `INFOPLIST_KEY_NSLocationWhenInUseUsageDescription` 形式写入，最终成为 `Info.plist` 中对应键的值。
- 本地化：「in addition to providing a purpose string in the Signing and Capabilities editor, localize the purpose string for each locale you support」，方式为 `InfoPlist.xcstrings` 字符串目录。

## 2.3 审查侧：机器与人工都参与

A 级：「App Review checks for the use of protected resources, and rejects apps that contain code accessing those resources without a purpose string.」并给出拒绝消息样本，错误编号为 `ITMS-90683: Missing purpose string in Info.plist.`，其中包含一句对供应链管理的关键说明：「If you're using external libraries or SDKs, they may reference APIs that require a purpose string. While your app might not use these APIs, a purpose string is still required.」以及责任归属：「You're responsible for all access of protected resources, including external SDK and library access.」

这三句共同界定了"授权声明可被追溯"的边界：**代码里存在引用即触发要求**，与是否实际调用无关；引入第三方 SDK 不转移责任。评估一个 App 的授权面时，"它没有用到"与"它的二进制里没有引用"是两件事。

## 2.4 提示时机的设计规范（对体验与对安全都成立）

A 级：

- 「Avoid making PhotoKit calls when your app first launches, to avoid interrupting its startup or onboarding process. Instead, make these calls in response to direct user action.」
- 「Timing the authorization prompt to coincide with a user action provides greater context to the user about why your app is requesting access.」
- 「Request authorization only for the level of access required.」
- 「if photo or video capture isn't the main focus of your app, check for permission only when the user invokes your app's camera-related features.」

这四句在文档中面向开发者体验，但从治理角度看是**同意质量的约束**：与用户动作对齐的提示才有语境可比性；"只请求所需级别"是访问级别（`addOnly` vs `readWrite`、`WhenInUse` vs `Always`）成为可管理对象的前提。本篇把这三条视为管理面的规范基线，并在 10.3 用它们做检查表。

# 3. 受保护资源目录：按资源分组的用途描述键

A 级来源为 Protected resources 集合页（其标题句即「Control an app's access to protected system services and user data.」）。下表按该页原始分组列出，语义列为该页原文转述。

## 3.1 硬件与传感器类

| 键 | 语义（原文） |
| --- | --- |
| `NSCameraUsageDescription` | 「why the app is requesting access to the device's camera」 |
| `NSMainCameraUsageDescription` | 「access to the device's main camera」（Vision 侧另有机位限定） |
| `NSMicrophoneUsageDescription` | 「access to the device's microphone」 |
| `NSAudioCaptureUsageDescription` | 「requesting access to capture system audio on macOS」 |
| `NSMotionUsageDescription` | 「access to the device's motion data」 |
| `NSFallDetectionUsageDescription` | 「permission to access fall detection event data」 |
| `NFCReaderUsageDescription` | 「access to the device's NFC hardware」 |
| `NSBluetoothAlwaysUsageDescription` | 「why the app needs access to Bluetooth」 |
| `NSBluetoothPeripheralUsageDescription` | 「requesting the ability to connect to Bluetooth peripherals」 |
| `NSWorldSensingUsageDescription` | 「access to image tracking, plane detection, or scene reconstruction」 |
| `NSHandsTrackingUsageDescription` | 「access to track their hand position and location」 |
| `NSAccessoryTrackingUsageDescription` | 「access to track accessory position and location」 |

注意 `NSAudioCaptureUsageDescription` 被原文限定为 macOS 的**系统音频捕获**，与 `NSMicrophoneUsageDescription`（设备麦克风）不是同一通道；`NSMainCameraUsageDescription` 与 `NSEnterpriseMCAMUsageDescription`（弃用段）指向同一"主相机"概念但归属不同版本。

## 3.2 个人信息类

| 键 | 语义（原文） |
| --- | --- |
| `NSContactsUsageDescription` | 「access to their contacts」 |
| `NSCalendarsFullAccessUsageDescription` | 「access to read and write their calendar data」 |
| `NSCalendarsWriteOnlyAccessUsageDescription` | 「access to create calendar events」 |
| `NSRemindersFullAccessUsageDescription` | 「access to read and write their reminders data」 |
| `NSPhotoLibraryUsageDescription` | 「access to their photo library」 |
| `NSPhotoLibraryAddUsageDescription` | 「add-only access to their photo library」 |
| `NSAppleMusicUsageDescription` | 「access to their media library」 |
| `NSHomeKitUsageDescription` | 「access to their HomeKit configuration data」 |
| `NSLocationUsageDescription` / `NSLocationWhenInUseUsageDescription` / `NSLocationAlwaysAndWhenInUseUsageDescription` / `NSLocationAlwaysUsageDescription` | 分别为一般/前台/始终与前台/始终（最后一项列于同组） |
| `NSLocalNetworkUsageDescription` | 「access to the local network」 |
| `NSNearbyInteractionUsageDescription` / `NSNearbyInteractionAllowOnceUsageDescription` | 常规/一次性「begin an interaction session with nearby devices」 |
| `NSSpeechRecognitionUsageDescription` | 「requesting to send user data to Apple's speech recognition servers」 |
| `NSSiriUsageDescription` | 「requesting to send user data to Siri」 |
| `NSUserTrackingUsageDescription` / `NSUserTrackingMarkdownUsageDescription` | 「the purpose for accessing data that an app can use to track a person or device」 |
| `NSGKFriendListUsageDescription` | 「access to their Game Center friends list」 |
| `NSVideoSubscriberAccountUsageDescription` | 「access to their TV provider account」 |
| `NSCriticalMessagingUsageDescription` | 「why the app needs to send SMS messages」 |

日历与提醒事项各存在"全量"与"仅写"两把键（`NSCalendarsUsageDescription`、`NSRemindersUsageDescription` 已列入该页 Deprecated keys），说明**同一资源内的访问级别由不同键表达，而不是由同一个键的取值表达**——这是本篇第 4 章状态语义的源头之一。

## 3.3 健康与研究类

| 键 | 语义（原文） |
| --- | --- |
| `NSHealthShareUsageDescription` | 「requests permission to read samples from the HealthKit store」 |
| `NSHealthUpdateUsageDescription` | 「requested permission to save samples to the HealthKit store」 |
| `NSHealthClinicalHealthRecordsShareUsageDescription` | 「requested permission to read clinical records」 |
| `NSHealthRequiredReadAuthorizationTypeIdentifiers` | 「The clinical record data types that your app must get permission to read.」 |
| `NSSensorKitUsageDescription` | 「A short description of the purpose of your app's research study.」 |
| `NSSensorKitUsageDetail` | 「keys for the specific information your app collects」 |
| `NSSensorKitPrivacyPolicyURL` | 「a hyperlink to a webpage that displays the privacy policy for your app's research study」 |

SensorKit 三键是目录中唯一**强制附带隐私政策 URL** 的一组；健康读写两键的分离与 `secc01781f46` 侧的用户可见描述一致（见 8.1）。

## 3.4 文件、其他 App 与系统配置类

| 键 | 语义（原文） |
| --- | --- |
| `NSDesktopFolderUsageDescription` / `NSDocumentsFolderUsageDescription` / `NSDownloadsFolderUsageDescription` | 「needs access to their Desktop / Documents / Downloads folder」 |
| `NSNetworkVolumesUsageDescription` / `NSRemovableVolumesUsageDescription` | 网络宗卷 / 可移除宗卷上的文件 |
| `NSFileProviderDomainUsageDescription` / `NSFileProviderPresenceUsageDescription` | 文件提供程序管理的文件 / 「determine which files the person is currently accessing」 |
| `NSAppBundlesUsageDescription` | 「access the contents of other apps' bundles」 |
| `NSAppDataUsageDescription` | 「access files in other apps' sandbox containers」 |
| `NSAppleEventsUsageDescription` | 「the ability to send Apple events」 |
| `NSSystemAdministrationUsageDescription` | macOS 中「requesting to manipulate the system configuration」 |
| `NSSystemExtensionUsageDescription` / `OSBundleUsageDescription` | 安装系统扩展包 / 驱动扩展包 |
| `NSFaceIDUsageDescription` | 「the ability to authenticate with Face ID」 |
| `NSIdentityUsageDescription` / `NSIdentityUsageDescriptionDictionary` | 请求身份信息 / 多条消息的字典形态 |
| `NSFinancialDataUsageDescription` | 「financial data stored in Wallet」 |
| `NSUpdateSecurityPolicy` | 声明哪些 App 或安装包可写入自身 bundle（属策略而非同意） |
| `NSLocationTemporaryUsageDescriptionDictionary` | 「requesting temporary access to their location」的字典 |
| `NSLocationDefaultAccuracyReduced` | 是否默认请求降低精度 |
| `NSWidgetWantsLocation` | 布尔，指示小组件使用位置信息 |

`FileProviderPresence` 与 `NSAppDataUsageDescription` 两组把"访问"的客体从文件内容扩展到了**行为痕迹**（当前正在访问哪些文件）与**其他 App 的容器**，与 #87 第 2.4 节（macOS 14 起跨容器需确认）指向同一边界。

## 3.5 弃用组与其治理含义

A 级（Deprecated keys）：`NSCalendarsUsageDescription`、`NSRemindersUsageDescription`、`NSEnterpriseMCAMUsageDescription`。另在 #86 与 #87 中记录过授权侧的弃用键 `com.apple.security.files.all`。

弃用组的存在说明该目录是**会移动的**：同一资源的访问级别可能从"一个键"分裂为"两个键"（日历/提醒事项），从"一个提示"分裂为"一次/使用期间/始终"。因此任何以键名清单为基准的审计都必须带目录访问日期（见 A.1），否则结论会随目录改版失效。

# 4. 授权状态的语义学

## 4.1 三个框架给出的同一套状态词

A 级逐字对照（三个枚举页）：

| 状态 | `PHAuthorizationStatus` | `CLAuthorizationStatus` | `AVAuthorizationStatus` |
| --- | --- | --- | --- |
| `notDetermined` | 「The user hasn't set the app's authorization status.」 | 「The user has not chosen whether the app can use location services.」 | 「If the user hasn't granted or denied capture permission, the authorization status is `AVAuthorizationStatus.notDetermined`」 |
| `restricted` | 「The app isn't authorized to access the photo library, **and the user can't grant such permission**.」 | 「The app is not authorized to use location services.」 | 同族语义（该页未逐条摘录，见 4.4） |
| `denied` | 「The user explicitly denied this app access to the photo library.」 | 「The user denied the use of location services for the app **or they are disabled globally in Settings**.」 | — |
| `authorized` | 「The user explicitly granted this app access to the photo library.」 | 「The user authorized the app to use location services.」 | 「status == .authorized」 |
| `limited` | 「The user authorized this app for limited photo library access.」 | — | — |
| 级别维度 | `addOnly` / `readWrite` | `authorizedWhenInUse` / `authorizedAlways` | 按媒体类型（`.video` / `.audio`） |

三点读法：

- **`restricted` 与 `denied` 的区别是"谁还能改"**。照片页的措辞最清楚：`restricted` 状态下用户无权授予，`denied` 是用户自己的选择。管理与家长限制落在 `restricted`，个人选择落在 `denied`。
- **`denied` 可以来自"全局关闭"**。位置页明确把「disabled globally in Settings」也归入 `denied`。因此一个 App 看到 denied 时无法区分"我被单独拒绝"与"该服务在设备上被整体关闭"。
- **访问级别不是状态**。`addOnly`/`readWrite` 与 `WhenInUse`/`Always` 是与状态正交的另一维（照片页标题句：「The app's level of access to the user's photo library.」），查询 API 必须带级别参数：`PHPhotoLibrary.authorizationStatus(for: .readWrite)`。

## 4.2 级别维度：同资源内的多档

| 资源 | 可用档位 | A 级依据 |
| --- | --- | --- |
| 照片库 | 仅添加 / 读写 / 受限（选中） | `PHAccessLevel` 两 case；`PHAuthorizationStatus.limited` |
| 位置 | 使用期间 / 始终 / 一次 / 临时精确 / 默认降精度 | `authorizedWhenInUse`、`authorizedAlways`、「Allow Once」、「The person can choose to allow access for one session only by tapping Allow Once.」 |
| 日历/提醒事项 | 全量读写 / 仅创建（仅写） | 两把 Full/WriteOnly 键 |
| 健康 | 逐类型 × 读 / 写 | 「App 将获得读取和写入数据的单独访问权限，以及对每种健康数据类型的单独访问权限。」 |
| 麦克风/相机 | 按媒体类型分别授权 | 「requesting authorization for … `.video` / `.audio`」 |

档位存在本身就否定了"权限是布尔值"的常见建模。审计时至少要记录三维：**状态 × 档位 × 来源（用户/管理/全局）**。

## 4.3 状态可变性与时序要求

A 级两句构成对开发者的时序约束：

- 「Because a person can change authorization at any time using Settings, always check the authorization status of a feature before accessing it.」
- 「In cases without a dedicated API, prepare your app to gracefully handle access failures.」
- 变更监听侧（照片受限库）：「To monitor changes to the user's limited-library selection, use the standard change observer API … Register your app to receive notifications and update your user interface as the system notifies it of state changes.」
- 位置侧：「Handle changes to authorization status in your location manager's delegate method, `locationManager(_:didChangeAuthorization:)`.」

即：**授权状态是异步可被外部修改的输入**，检查必须紧邻使用点，且部分类别提供回调、部分不提供（不提供者只能靠失败处理）。

## 4.4 语义缺口

`AVAuthorizationStatus` 的完整枚举成员（含 `restricted`、`denied`、`authorized`、`prohibitsAccess` 等）在本次核查覆盖的页面（AVFoundation 授权页）中只出现 `notDetermined` 与 `authorized` 两个值，其余值未在该页逐条列出。本篇因此不对相机/麦克风的 `restricted` 文案作断言，仅记为 U-03。

# 5. 相机与麦克风：授权、能力与硬件三层

## 5.1 双重要求：键 + 授权属性，缺一即终止

A 级（Requesting authorization to capture and save media）：

- 「In iOS and macOS 10.14 and later, the user must explicitly grant permission for each app to access the camera and microphone.」
- 「Before your app can use a capture device for the first time, the system presents an alert with an app-specific message that you specify…」
- 「Your app needs to contain the appropriate key in its `Info.plist` file, and the appropriate entitlement enabled in macOS, before it requests authorization or attempts to use a capture device. Otherwise, the system terminates your app.」
- macOS 所需两键：`com.apple.security.device.camera`、`com.apple.security.device.audio-input`（与 #87 第 3.5 节的能力型键是同一对象）。

这是整套体系中最"硬"的一条：**声明不全不是被拒绝，而是进程被终止**。它把"意图声明"与"用户同意"绑成串联而非并联——链条上任一环节缺失，访问都不成立。

## 5.2 提示与记忆

- 「The system remembers the user's response to each access alert, so subsequent uses of the corresponding capture device don't cause the alert to appear again.」
- 「The user can change permission settings for your app in Settings > Privacy in iOS and in System Preferences > Security & Privacy in macOS.」
- 相机/麦克风的提示按钮为两项（Don't Allow / OK），与位置提示的三项（Allow Once / Allow While Using App / Don't Allow）不同——**提示形态按类别不同**，这一事实来自该页截图说明，属 A 级（图示）而非正文。

## 5.3 使用前的强制自检

A 级：「Always test the `AVCaptureDevice.authorizationStatus(for:)` method before setting up a capture session.」并给出模式：先查状态，`notDetermined` 时才发起 `requestAccess(for:)`，否则据结果决定是否为 `authorized`。文档同时给出反例规避：非以拍摄为主的 App 应在用户调用相关功能时才检查。

## 5.4 运行时可见性：指示灯与其强度口径

A 级（Mac 屏幕相机指示灯页，页面标注发布日期 2026-03-11）：

- 「MacBook Neo 结合系统软件和 A18 Pro 内的专用芯片元素，为相机画面提供额外的安全保护。」
- 「该架构的设计可阻止任何不受信任的软件（即使在 macOS 中拥有根或内核权限）在不点亮屏幕相机指示灯的情况下启动相机。」

括号内的"即使拥有 root 或内核权限"是该陈述作用域的关键，且整句以"设计可阻止"表述，不应改写为"必然点亮"。

## 5.5 硬件断联：唯一不受授权管理的一档

A 级（`secbbd20b00b`）：

- 「所有基于 Apple 芯片的 Mac 笔记本电脑和所有基于 Intel 芯片且搭载 Apple T2 安全芯片的 Mac 笔记本电脑都具备硬件断联功能，该功能会在机盖合上后关闭麦克风。」
- 「这旨在防止任何软件（甚至是拥有 macOS root 或内核权限的软件以及 T2 芯片或其他固件上的软件）在机盖合上后启用麦克风。」
- 括号说明相机为何不断联：「（摄像头不会在硬件中断联，因为其可视区域在机盖合上后会被完全遮挡。）」
- iPad 侧：「2020 年及之后推出的 iPad 机型也支持硬件麦克风断联功能。将符合 MFi 标准的保护壳……连接到 iPad 并合上之后，麦克风会在硬件层面断联。」
- 实现口径：「每款支持硬件切断麦克风的产品都有一个或多个机盖传感器」「校准过程还会包括不可逆的硬件锁定，以阻止对传感器上敏感参数的任何后续更改」「此逻辑会在关闭麦克风前提供防抖、滞后和/或最高 500 毫秒的延迟」，实现方式为「关闭麦克风和片上系统 (SoC) 之间用于传输数据的线路」或「关闭连接到麦克风模块上允许其启用的其中一个输入线路，如时钟线路」。

在"谁管理授权"这张图上，这一档的地位是：**软件侧的任何授权状态、任何设置开关、任何 MDM 策略都无法在此物理条件下生效**。它是撤销语义的下限参照物——其余撤销都是逻辑的，这一条不是。

## 5.6 麦克风模式的管理权

A 级（Information Property List，AVFoundation 段）：`NSAlwaysAllowMicrophoneModeControl` — 「A Boolean value that indicates if a person can configure a microphone mode regardless of whether the microphone is in an active state.」

该键把"谁有权改麦克风模式（而非是否可采集）"这件事交给开发者声明、由用户配置，是目录中少数管理**配置权而非访问权**的键。

# 6. 照片：一档"选中"如何改变授权的粒度

## 6.1 默认状态与三档访问

A 级（Delivering an Enhanced Privacy Experience）：

- 「Due to built-in privacy protections, an app may only access the user's Photos library if they explicitly authorize it to do so.」
- 「Starting in iOS 14, PhotoKit further enhances user privacy controls with the addition of the limited Photos library, which lets users select specific assets and resources to share with an app.」
- 「In previous iOS releases, users either allowed or disallowed full access to their library. Most users don't want to give apps full access to their private data, and starting in iOS 14, they can share a limited subset of their photos and videos.」
- 影响面：「The limited Photos library affects all apps that use PhotoKit in iOS 14, **including those already published to the App Store**.」

最后一条在治理上重要：新粒度**回溯适用于已上架 App**，不需开发者重新申请。这与需要开发者主动采纳的能力键（#86 第 2 章四步合成链）形成对照。

## 6.2 选择器路径：不请求授权也能取图

A 级：「Because the system manages its life cycle in a separate process, it's private by default. The user doesn't need to explicitly authorize your app to select photos, which results in a simpler and more streamlined user experience.」且「`PHPickerViewController` is a new picker that replaces `UIImagePickerController`.」

机制与 #87 第 4.2 节一致：**独立进程 + 结果转交**。用户选择的资产被送到 App，App 从未取得图库访问权，因此不存在授权状态。这条路径把"访问资源"替换为"接收数据"，是授权管理中最好的降档做法，也解释了为什么目录中照片有两把键而选择器路径不需要权限。

## 6.3 受限库下的行为差异

A 级两条例外：

- 「The system automatically adds assets that your app creates with a `PHAssetCreationRequest` to the user's limited library selection.」
- 「You can't create or fetch user albums.」需 `limited` 时调整行为与界面。

即受限模式下写路径仍自动扩域（新建资产自动进入用户已选集合），而读侧的组织结构（相册）不可见。这与 #87 中"书签隐式扩域"是同一种模式：授权对象随操作自动更新，而非要求用户重复确认。

## 6.4 旧 API 与新语义的不兼容

A 级 Important 注记：「The `PHPhotoLibrary.authorizationStatus()` and `PHPhotoLibrary.requestAuthorization(_:)` methods aren't compatible with the limited library and return `PHAuthorizationStatus.authorized` when the user authorizes your app for limited access only.」

这是一条**可造成误判的兼容层行为**：旧 API 在受限授权下报告"已授权"。管理面上意味着：以旧 API 读数得出的"用户给了我全库"结论不可信，必须以 `authorizationStatus(for:)` 为准。本篇把这一条列入 14.2 冲突表（C-02）。

## 6.5 提示抑制与主动呈现

A 级：「By default, the system automatically prompts the user to update their limited-library selection once per app life cycle. This automatic presentation isn't the preferred user experience for most apps. Instead, apps need to suppress the automatic prompt and present it programmatically.」抑制方式是在 `Info.plist` 中添加对应键（该页以截图给出，名为 "Prevent limited photos access alert"），呈现方式为 `PHPhotoLibrary.shared().presentLimitedLibraryPicker(from:)`，iOS 15+ 起可带回调返回新选资产标识列表，且「This method only works when the user enables limited-library mode.」

值得记录的一处**管理权转移**：系统本来会周期性提示用户扩充共享范围，开发者可以声明抑制它。也就是说"是否提醒用户收紧/放宽共享"这一动作的发起权部分落到了 App 手里。

# 7. 位置授权：档位、精度与"一次打断"

位置是本篇最适合作为完整样本的授权类别——它同时具备**档位轴**（使用时/始终）、**精度轴**（全精度/降级精度）与**全局开关**三条独立控制线，官方文稿对每条都给出了可引用的语义。

## 7.1 一次打断，之后不再问

官方对 Core Location 授权流程的定性是一句可以直接引用的机制描述：

> "This authorization process involves a one-time interruption, during which the system prompts the device owner to grant or deny your app's request for location data. After the initial interruption, the system stores your app's authorization status and doesn't prompt again."

这句话有三个后果值得分别记下：

1. **提示权只在第一次**。之后任何一次"要权限"的动作都不是提示，而是读取已存储状态；状态为已决定时，调用请求方法不会产生弹窗。
2. **状态是被"存储"的**，不是每次访问时重新协商的。因此授权的作用域、有效期与撤销路径都属于状态管理问题，而不是访问控制问题——本篇正是从这个切口进入的。
3. **"不再问"不等于"不会变"**。用户可以随时在系统设置里改动，App 必须靠状态变化通知而不是靠缓存感知，见 §7.7。

## 7.2 两档的官方语义与四平台对"在使用中"的不同定义

| 档位 | 官方语义 | 支持平台（文稿表） | 是否自动拉起被终止的 App |
|---|---|---|---|
| When in Use（使用时） | "The user authorized the app to start location services while it is in use."；官方明确"this authorization is the preferred choice, because it has better privacy and battery life implications" | All | "No. The user must launch the app." |
| Always（始终） | "The user authorized the app to start location services at any time."；并"lets the system launch your app quietly to handle some updates" | All platforms except tvOS and visionOS | "Yes for significant location change, visits, and region monitoring services; no for others" |

"在使用中"这一条件本身在四个平台上是不同的定义，这不是实现细节，而是**同一个授权名在四个平台上授予了不同的实际时间窗**：

- **iOS**："in use when it's in the foreground and for a short time when it transitions from the foreground to the background"。若开启后台位置更新，持有 When in Use 的 App 在定位服务活跃期间可继续在后台运行；定位服务不在运行时回到正常挂起规则。系统终止或未在运行的 App，不会被以 When in Use 身份拉起投递新数据。
- **macOS**："When in Use and Always authorizations are functionally equivalent"，理由是 Mac App 首次启动后持续在后台运行，因此"always in use"。
- **watchOS**：复杂功能可获得位置更新，但 App 必须至少运行过一次才能发起授权请求；若该复杂功能位于当前表盘，系统视其为"在使用中"。即使持有 Always 访问，"The system doesn't launch watchOS apps"。
- **visionOS**：App 在被注视期间以及停止注视后的一小段时间内算"在使用中"；Always 档位在该平台不可用。

由此可得一条本篇的判断：**在 macOS 上把两档当作有不同隐私含义来讨论是错误的**，而在 watchOS 上把 Always 当作"后台自启许可证"同样是错误的。档位差异的真实内容主要是"是否自动拉起被终止的 App、以及拉起哪些服务类型"。

## 7.3 升级只能申请一次

关于从使用时提升到始终，官方给出的是硬性次数限制：

> "If your app already has When in Use authorization, you can make a separate request for Always authorization later. However, you can make the request only once."

这条规则把"始终"变成了一种**一次性可获取、但可被用户单方面撤销且难以再次自然索取**的状态。管理侧的推论是：升级请求的时机价值高于任何文案优化——一旦用掉这次机会，后续只能通过设置页外链引导，而不能靠弹窗。

## 7.4 位置键的缺失后果是"请求立即失败"，不是终止

Core Location 的用途描述键与要求（官方表）：

| 键 | 官方标注的必需条件 |
|---|---|
| `NSLocationWhenInUseUsageDescription` | "The app requests When in Use or Always authorization." |
| `NSLocationAlwaysAndWhenInUseUsageDescription` | "The app requests Always authorization." |
| `NSLocationUsageDescription` | "(macOS only) Your macOS app uses location services." |

流程要求与失败语义：

> "Add all usage description keys to your app's Information Property List before you make any authorization requests. Authorization requests fail immediately if the required keys aren't present."

这里要和第 5 章相机/麦克风的口径**并读而不是混用**：相机那一类是"键与授权属性必须串联，否则 the system terminates your app"；位置这一类是"请求立即失败"。同为"配置不齐"，一处后果落在进程存活，一处落在调用返回值。把两者统一说成"崩溃"或统一说成"失败"都会失真（记入第 14 章冲突表 C 组）。

## 7.5 精度是独立第二轴

`CLAccuracyAuthorization` 只有两个取值："fullAccuracy — The user authorized the app to access location data with full accuracy." 与 "reducedAccuracy — The user authorized the app to access location data with reduced accuracy."

关键在于**它和档位正交**：用户可以先给"使用时"再关掉"精确位置"，于是 App 拿到的是"能定位但精度低"的组合状态。开发者侧可用 `NSLocationDefaultAccuracyReduced` 决定默认档位方向：

> "Set the key value to `true` to prompt the user for reduced accuracy by default; set it to `false` to prompt for full location accuracy. If you don't include that key in your `Info.plist`, that's equivalent to setting it to `false`."

设为 `true` 时官方列出的可观测后果是三处：所有 Core Location 服务（位置更新、访问点监控、重要位置变化、围栏监控）都按降级精度供给；授权弹窗中的地图显示近似位置；以及该 App 在"设置 > 隐私 > 位置服务"里"Precise Location"处于关闭状态。此外还有一条**不改动全局授权即可临时降级**的手段：把 `desiredAccuracy` 设为 `kCLLocationAccuracyReduced`，官方表述是"causes `startUpdatingLocation()` to deliver results as if the app were authorized for approximate location until you change the ... constant again"——注意措辞是"如同"，这是**App 侧自缚**而非系统授予，权限真值未变。

用户主权方面，官方两句话把边界钉死：

> "Setting `NSLocationDefaultAccuracyReduced` determines the default type of authorization your app gets, but users can override it any time in Settings. Users always control the level of location accuracy they want to share."

## 7.6 全局开关把状态压成 denied

`CLAuthorizationStatus.denied` 的定义包含两种截然不同的成因："The user denied the use of location services for the app **or they are disabled globally in Settings**."

也就是说"整机关掉定位服务"和"只拒绝这个 App"在 API 上收敛为同一个值。而 `restricted` 的官方定义在位置这一层只有一句"The app is not authorized to use location services."，未给出成因；`notDetermined` 则是"The user has not chosen whether the app can use location services."。三条合起来构成第 4 章所指的**语义缺口**：App 无法区分"我个体被拒"与"整机被关"，也无法区分 `restricted` 与 `denied` 的来路。官方示例代码干脆把两者合并处理：`case .restricted, .denied: // Location services currently unavailable.`

管理含义有二：一是**引导文案必须同时覆盖两种修复路径**（进设置改本机开关 / 改该 App 条目）；二是**任何"用户拒绝了我们"的统计都会被整机开关污染**，做隐私合规度量时应把它当作不可分事件。

## 7.7 状态变化如何被送达

官方把变化通知设计为"多实例广播 + 首次自动上报"两条：

> "a newly configured `CLLocationManager` object also reports your app's current authorization status to its delegate's `locationManagerDidChangeAuthorization(_:)` method automatically."

> "People can change your app's authorization status at any time in system settings. If your app is running when the change happens, each of your app's `CLLocationManager` objects reports the change to that delegate method. The location manager also reports your app's current authorization at other times. For example, the location manager also calls the method when a suspended iOS app starts running again."

第三句最容易被忽略，也最重要：**被挂起后重新运行的 App 会收到一次状态上报**。这正是"用户在设置里改完了，App 醒来发现世界变了"的机制落点，也是本篇"状态是易变的、缓存是危险的"这一论点最直接的技术支撑。较新的文稿还把观察方式迁移到异步序列：`CLLocationUpdate` 与 `CLMonitor`（"Your app receives events, including authorization changes, by observing asynchronous sequences from `CLLocationUpdate` and `CLMonitor`"）。

## 7.8 把"何时弹"交还 App：挂起授权请求

Core Location 另有一篇专门处理提示时机的文稿，做法是显式获取会话再让弹窗出现：

> "You can inhibit the auto-prompting in your app by creating a `CLServiceSession` at a convenient time in your app, then iterating over its diagnostics property to determine the level of authorization the person using your app selects."

开关方式是一条 Info.plist 键：`Add the CLRequireExplicitServiceSession property to your app's Info.plist file to opt into this control behavior.`

示例代码里还有一个值得记录的状态字段：`session.diagnostics` 逐条产出诊断，`if !diagnostic.authorizationRequestInProgress { // A denial occurred. break }`——即"请求进行中"结束有两种结局，官方注释直接称其中一种为 denial。另有一条独立说明：`CLServiceSession` 是"An object that provides diagnostics about an app's authorization to use location services."

这是**授权管理权分配**的一处清晰例证：默认由系统在功能被调用时自动弹；加上这个键之后，弹不弹、何时弹变成 App 的显式动作。它给引导流程（onboarding）让路，同时也意味着 App 需要自己承担"忘了弹"的责任。

## 7.9 位置授权的四条撤销路径

| 路径 | 作用范围 | 性质 |
|---|---|---|
| 设置 > 隐私与安全性 > 定位服务 > 某 App，改档位或关"精确位置" | 单 App，可细到精度 | 常规管理 |
| 设置 > 隐私与安全性 > 定位服务，关闭总开关 | 整机所有 App | 一键压制为 `denied`（§7.6） |
| 设置 > 通用 > 传输或还原 iPhone > 还原 > 还原位置与隐私 | 整机全部 App 的隐私同意状态 | 抹掉历史同意，回到 `notDetermined` |
| `tccutil reset <service>` | 按服务的跨全部 App 重置 | 命令行，开发/排障与外部管理场景 |

四条路径的**范围相差一个量级**（单 App → 单服务全域 → 整机），这一点在第 9 章会被提炼为"撤销粒度"论题。本篇不给出具体命令用法细节，只保留"存在按服务粒度的重置入口"这一事实口径。

---

# 8. 健康：把"不可探测"当作设计目标

《Apple 平台安全保护》"保护对用户健康数据的访问"一节（发布日期 2026-01-28）给出了健康授权在整套同意体系里最特殊的一组语义——它主动剥夺 App 的自知能力。

## 8.1 逐类型、读写分离

> "但对于健康数据，App 将获得读取和写入数据的单独访问权限，以及对每种健康数据类型的单独访问权限。用户可以在"设置">"健康">"数据访问权限与设备"中，查看和撤销已授予的访问健康数据的权限。"

这一句确立了三点：**授权粒度是"类型 × 方向"**；请求方式与通讯录/照片同类（"与请求访问"通讯录"、"照片"和其他 iOS 数据源类似"）；**管理入口是可查看与可撤销的成对能力**。

## 8.2 读写权限的不对称继承

| 官方规则 | 原文要点 |
|---|---|
| 写自动带"读自己写的" | "如果获得写入数据的权限，App 还可以读取其写入的数据。" |
| 读是全来源的 | "如果获得读取数据的权限，则 App 可以读取所有来源写入的数据。" |
| 不能窥视他人权限 | "但 App 不能决定其他 App 所获得的访问权限。" |
| 不能确认自己的读权限 | "此外，App 不能完全确定其是否获得了读取健康数据的权限。" |
| 无读权限时的表现 | "如果 App 没有读取权限，所有查询都不会返回数据，如同查询空数据库。" |
| 这条设计的目的 | "这旨在阻止 App 通过学习用户所跟踪的数据类型来推断用户的健康状况。" |

"**如同查询空数据库**"是整篇本篇里最值得单独引用的一句：授权被拒不以错误码、不以异常状态呈现，而是呈现为**数据本身为空**。这是一种把"拒绝"伪装成"没有数据"的反推断设计——它保护的不是设备，而是**用户从 App 侧可被推断出的属性**。

工程后果是明确的：健康 App 无法用"查询返回空"来判定权限，必须走显式的授权请求结果与设置页；而把"空"当作"用户没数据"来做产品决策会系统性误判。

## 8.3 管理数据与临时日志各自的保护类

> "HealthKit 还会收集和储存管理数据，如 App 的访问权限、连接到 HealthKit 的设备名称，以及新数据可用时，用来开启 App 的计划信息。此数据储存在"首次用户认证前保护"数据保护类中。"

> "临时日志文件储存设备锁定时（例如用户锻炼时）所生成的健康记录。这些文件使用"未打开文件的保护"数据保护类储存。设备解锁后，这些临时日志文件会导入进主要健康数据库，并会在合并完成后被删除。"

这两段把"授权状态本身"降格为一种**被储存的数据**：它有明确存放位置（管理数据）、有明确保护类（Before First Unlock），并且与设备名、唤醒计划一起存放。第 12 章的"授权状态储存在哪里、由谁守护"这条未披露问题正是从这里提出的——公开资料只说它被储存，未说它的密码学与进程隔离细节。

## 8.4 备份与 iCloud 的两个条件

> "健康数据可以储存在 iCloud 中。"健康"数据的端对端加密要求 iOS 12 或更高版本以及双重认证。如果不满足这两个要求，用户的数据仍会在储存和传输过程中加密，但不是端对端加密。"

> "如果用户使用"访达"（macOS 10.15 或更高版本）或 iTunes（macOS 10.14 或更低版本）备份其设备，只有在备份加密时才会储存健康数据。"

第二条对管理者的意义大于安全宣传：**未加密的电脑备份里不含健康数据**。这既是隐私保护，也是取证/合规场景里常被误认为"数据丢失"的正常表现。

## 8.5 临床记录：OAuth 2 与 TLS 1.3

> "用户可在"健康" App 内登录支持的健康系统，以获取其临床健康记录的副本。当用户接入健康系统时，会通过 OAuth 2 客户端凭证进行认证。接入后，将通过受 TLS 1.3 保护的连接直接从健康机构下载临床健康记录数据。下载完成后，临床健康记录会与其他健康数据一起安全储存。"

注意这里的授权**主体变了**：不是设备上的 App 请求，而是用户登录第三方健康系统。它落在设备授权体系之外，靠标准 Web 授权协议完成，但落库后与其余健康数据同保护。这是第 14 章冲突表里"隐私授权"一词外延不一致的实例之一。

## 8.6 记录来源元数据与数字签名

> "储存在数据库中的数据包括用于追踪每条数据记录起源的元数据。该元数据包括 App 标识符，用以识别储存了该记录的 App。此外，可选元数据项还可能包含记录的数字签名副本，从而保持受信任设备生成记录的数据真实性。数字签名采用在 RFC 5652 中描述的"密码讯息语法"(CMS) 格式。"

这是**授权体系与签名体系的接点**：谁写的（App 标识符）+ 是否可信（可选 CMS 签名）。第 84–87 期讨论的是"能不能写"，此处补上"写了之后如何被追溯"。签名被官方标注为"可选"，因此不可把它当作默认可得的审计证据。

## 8.7 两类共享：人与机构

> "在运行 iOS 15 或更高版本的 iPhone 上，"健康" App 可让用户选择与其他用户共享其"健康"数据。"健康"数据通过 iCloud 端对端加密在两位用户之间共享，Apple 无法访问通过"健康"共享发送的数据。若要使用该功能，收发用户数据的设备必须使用 iOS 15 或更高版本且打开双重认证。"

> "用户还可以使用"健康" App 中的"与提供者共享"功能来与其医疗服务提供者共享"健康"数据。使用此功能共享的数据通过端对端加密的方式仅提供给用户所选的健康机构，Apple 不会保留或访问加密密钥..."

两条都是**条件式端对加密**：条件不满足时功能或加密形态退化。管理侧要记住的是"端对端"在这里是版本 + 双重认证的合取，而不是一个恒真属性。

## 8.8 医疗急救卡：一个被官方明说的权衡

> "在锁定屏幕上轻点"紧急情况"按钮可查看医疗急救卡信息。该信息使用"无保护"数据保护类储存于设备上，因此无需输入设备密码就可进行访问。"医疗急救卡"作为一项可选功能，可让用户权衡安全性和隐私二者之间的关系。在 iOS 13 或更低版本中，此数据备份在 iCloud 云备份中。在 iOS 14 或更高版本中，医疗急救卡使用 CloudKit 在设备间同步并具备与其他健康数据相同的加密特性。"

这是全系列少见的、**Apple 自己把某功能写成"权衡"而非"保护"**的段落。它在第 12 章作为"隐私授权体系里被有意留下的洞"引用：不是所有健康信息都在同意体系覆盖之下，锁屏可见的那部分是主动例外。

## 8.9 授权之外的使用限制

> "App 只有通过授权才能访问 HealthKit API，且必须遵守数据使用方式的访问限制。例如，不允许 App 使用健康数据投放广告。App 还需要向用户提供隐私政策，详细说明其如何使用健康数据。"

**授权 + 使用条款是两层**，且第二层不是技术上强制的。把它记下来是为了防止一种常见误述："用户授权了，所以怎么用都合规"。

## 8.10 其他高敏数据源的归位

通讯录、日历、提醒事项、备忘录、媒体库、 Siri 与语音、广播服务等类别的用途描述键目录已在第 3 章给出，其在《Apple 平台安全保护》同意目录中的位置已在 #87 §5 处理过。本篇不重复列举，只保留一条分工结论：**这些类别共享同一套状态语义（第 4 章）、同一个记忆规则（一次决定长期生效）与同一个管理入口（设置中的隐私目录），差异体现在是否有档位、是否有子集授权、是否有硬件指示器**。健康是唯一把"拒绝"实现为"空数据"的，照片是唯一把"部分同意"实现为一等档位（limited）的，位置与运动是唯一把"精度"做成独立轴的。

---

# 9. 用户侧管理面：范围、粒度与可见性

## 9.1 三级管理范围

用户可用的管理动作分布在三个不同范围上，且**每个范围的重置代价不同**：

| 范围 | 典型入口 | 影响面 | 代价 |
|---|---|---|---|
| 单个 App 的单个类别 | 设置 > 隐私与安全性 > 各类别 > 该 App | 只此 App 此类别 | 可再改，历史决定被覆盖 |
| 整类（跨所有 App） | 该类别页顶部的总开关（如定位服务） | 所有 App，多数表现为 `denied` | 一键关闭，影响功能面广 |
| 整机的同意状态 | 还原位置与隐私 | 所有类别、所有 App 的同意记录 | 抹掉全部历史同意，回到未决定态 |

## 9.2 用户实际能做的五种动作

1. **授予/拒绝**（首次弹窗，一次决定长期生效，§7.1）。
2. **改档位**（使用时 ↔ 始终；照片的全量与所选；健康的逐类型读写勾选）。
3. **改精度**（精确位置开关）。
4. **撤销**（把某 App 从允许列表移除；或在整类页关掉它）。
5. **批量清零**（还原位置与隐私；`tccutil` 类重置）。

第 5 项常被忽略，但它是唯一能让状态回到 `notDetermined` 的路径——前四项只能在"已决定"的集合里换值。对开发排障而言，这一区别决定"要不要重装 App"；对家长式管理而言，它是绕开设备级限制的可用出口之一，因此第 11 章的受监督锁定设计明确把它收窄。

## 9.3 用户看得见的信号

| 信号 | 覆盖类别 | 强度 |
|---|---|---|
| 弹窗文案（用途描述 + 档位选项） | 全部需同意的类别 | 一次性、可被开发者措辞影响 |
| 设置 > 隐私与安全性 的分类目录与每类 App 列表 | 全部 | 持续可查，是权威视图 |
| 状态栏/指示灯（相机指示灯、麦克风使用指示） | 相机、麦克风 | 硬件级，第 5 章已给出"即使拥有根或内核权限也无法绕过"的表述 |
| 状态 API 读回 | App 内可见 | 有语义缺口，第 4/7 章 |

## 9.4 用户看不见的部分

用户侧管理面存在三处**结构性不透明**，本篇按口径统一记作未披露或不承诺，不猜测：

- 某个 App 在**何时**、**因为哪一次调用**取得过某项授权——公开资料未提供面向用户的逐次访问审计视图。
- 授权状态的**存储位置与守护方式**——官方仅说明"系统存储你的 App 的授权状态"（"the system stores your app's authorization status"），未披露其数据结构与访问控制细节。
- **已被撤销的授权是否曾留下可导出的历史记录**——未披露。

---

# 10. 开发者侧管理面：把请求放在它该在的位置

## 10.1 时机是首要控制变量

官方对时机的表述带有明确的效果判断：

> "To help people understand why you need location data, make authorization requests only when someone engages a part of your app that requires that data. Making the request immediately before it's needed increases the likelihood of the person granting the request. If you make a request immediately at app launch, or in a part of your app that doesn't clearly use location data, the person might misinterpret your intent and deny the request."

这里同时给出**正向建议**（紧邻使用点）与**负向后果**（启动即请求 → 被误解 → 被拒）。它也是"授权管理"作为工程问题而非合规问题的核心证据：管理得不好，直接影响授予率。

## 10.2 配置必须先于请求

三条纪律，分别来自第 5/7 章的失败语义：

1. 用途描述键全部就位后再请求（"Add all usage description keys ... before you make any authorization requests. Authorization requests fail immediately if the required keys aren't present."）。
2. 键与所需能力属性串联：相机/麦克风类须同时具备用途描述与授权属性，否则"the system terminates your app"；#86 已论证"声明能力≠获得能力"，本篇补充"两者缺一时失败形态不同"。
3. 本地化用 `InfoPlist.xcstrings` 与 `INFOPLIST_KEY_…` 构建设置项（§2.4），避免只翻译界面而漏译权限文案。

## 10.3 自检而不是缓存

`#87` 记录了"系统在启动时执行检查"的一般原则；本篇在授权层的对应做法是：**每次进入需要该数据的功能前读回状态**，并把状态变化通知当作唯一可信的更新源。理由是 §7.7——挂起恢复会被上报、设置页随时可改。把首次授予结果写进 App 自己的持久化存储并在后续依赖它，等价于制造一份必然过期的副本。

## 10.4 被拒之后的 UI 降级

| 情形 | 正确降级 | 反模式 |
|---|---|---|
| `denied`（个体被拒或整机关闭，不可区分） | 说明两种可能并分别给出修复路径；提供跳转设置的入口 | 反复触发请求（无效，§7.1） |
| `restricted`（成因不明） | 只说明"当前无法访问，可能受策略限制" | 把 `restricted` 表述成"您拒绝了" |
| 照片 `limited` | 走 PHPicker 或所选子集；不要假定 `authorized` 意味全量 | 用旧 API 的返回值判断全量访问 |
| 健康读权限缺失 | 呈现"无数据"视图，同时不谎称已取得授权 | 用查询是否为空来探测权限 |
| Always 申请已用尽 | 只能在设置页引导 | 重复请求升级（"only once"） |

## 10.5 审查与自动化检查

第 2 章已记录 App Review 侧的 `ITMS-90683`：缺用途描述会被上传阶段拦截，且**只要链接了相关 SDK 就可能触发**，即使代码未调用。这条把"隐私授权管理"从运行期问题前移为构建期问题。配套的工程结论是：**权限目录应纳入构建校验**（每条被链接 SDK 对应的键是否齐备、值是否为空、是否已本地化），而不是等上架失败再补。

## 10.6 三处最容易踩的语义陷阱

1. **照片**：旧 PhotoKit 在 limited 下可能报出 `authorized`，与"用户只给了部分"这一事实冲突（第 6 章）。
2. **位置**：`denied` 混装了个体拒绝与整机关闭；`restricted` 与 `denied` 在示例代码里被有意合并（第 7 章）。
3. **健康**：读取权限被设计为不可完全确定（第 8 章）。

三条的共同形态是：**API 返回的状态不是用户决定的完整编码**。因此本篇对"权限模型"的总判断是——它是**意图模型**（描述用户想给什么），不是**可验证事实模型**（描述 App 实际能拿到什么）；两者在多处以不同方式分离。

# 11. 管理式与家长式治理：把"同意"从用户手里拿走的部分

第 1.3 节列出的五类管理者中，只有用户与开发者是"逐次"的；家长与组织是**结构性**的——他们改变的不是某次答复，而是某次答复能否发生。本章给出这一层的官方口径。

## 11.1 配置负载：授权信息的下发载体

《Apple 平台安全保护》"配置执行"（发布日期 2024-05-07）对配置的定义里包含"授权"一词：

> "配置是一种遵循特定结构并由有效负载组成的 XML 描述文件或 json 格式文件，这些有效负载可将设置和授权信息载入到 Apple 设备上。配置可自动化设置、账户、访问限制和凭证的配置。"

"访问限制"作为配置的四大自动化对象之一被单独列出，说明它不是附加功能而是主要用途：

> "访问限制可由管理员添加，在部分情况下也可移除。它的作用是帮助阻止用户访问设备管理服务中已注册 Apple 设备的特定 App、服务或功能。作为配置的一部分，访问限制有效负载中的访问限制会被发送到设备。iPhone 上的某些访问限制可以镜像到配对的 Apple Watch 上。"

三处限定值得逐字保留：一是**"在部分情况下也可移除"**——即某些限制一旦下发，设备侧不可自行解除；二是它的目标是"阻止用户访问…特定 App、服务或功能"，属于**能力封禁**而非**代替用户同意**；三是可跨设备镜像到配对手表（本篇不宣称所有类别都能镜像，原文只说"某些"）。

## 11.2 载体本身的可信性

> "可以对配置描述文件进行签名来验证其来源，以及对其进行加密来帮助确保其完整性并保护其内容。Apple 设备的配置描述文件使用 RFC 5652 中规定的"密码讯息语法"(CMS) 进行加密，CMS 支持 3DES 和 AES-128。"

这与 §8.6 健康记录的 CMS 签名是同一格式标准在不同治理面的复用。与 #84/#85 的结论一致：**管理策略的信任来源是签名，不是传输通道**。

## 11.3 注册形态决定治理强度

| 注册形态 | 官方定位 | 对隐私授权治理的含义 |
|---|---|---|
| 用户注册（BYOD） | "专为用户所拥有的设备而设计且与管理式 Apple 账户集成"；"以加密方式分离出受管理的数据" | 治理作用于受管理侧，个人 App 与个人账户使用个人 Apple 账户；隐私同意大体仍归用户 |
| 设备注册 | "允许组织让用户手动注册设备并管理设备使用的各个不同方面"；"具有大量可应用到设备的配置和访问限制" | 组织可大范围施加访问限制；移除注册描述文件连带移除所有配置、设置与受管理 App |
| 自动设备注册 | "从设备开箱时起就可配置和管理设备。这些设备称为受监督的设备，用户可以选择阻止设备管理服务描述文件被用户移除" | 最强形态：可把描述文件锁定在设备上；"受监督"是能否强制治理的关键分界 |

另外两条硬约束：**"一台设备上一次只能有一个注册描述文件"**；以及用户主动移除注册描述文件时"基于该注册描述文件的所有配置、用户设置和受管理的 App（取决于注册类型和所用配置）都将随之移除"。第二条是治理与用户主权之间唯一的常规出口，因此受监督形态才需要提供"锁定到设备"的机制：

> "【注】在受监督的设备上，还可以将配置描述文件锁定到设备。这旨在防止其移除，或者可以只允许使用密码将其移除。"

注意官方措辞是"这**旨在**防止其移除"——按 A.3 口径，不得改写为"保证无法移除"。

## 11.4 家长式治理：屏幕时间承接了"访问限制"

> ""屏幕时间"替代了 iOS 和 iPadOS 早期版本中的"访问限制"功能以及 macOS 早期版本中的"家长控制"功能。"（《"屏幕时间"安全性》，发布日期 2021-02-18）

这一句在授权管理上的意义是**历史入口迁移**：老手册里"设置 > 通用 > 访问限制"的说法对应到今天的"设置 > 屏幕时间"，若继续按旧路径描述家长式限制，会与第 4 章的 `restricted` 态产生出处错配。

该页同时给出三条与治理强度直接相关的规则：

- **密码是治理生效的前提**："为孩子打开"屏幕时间"时，家长需要设定密码以让孩子无法进行更改。"
- **治理对孩子是可见的**："家长打开"屏幕时间"时孩子会收到通知，孩子也可以监控自己的使用情况。"
- **治理有法定终止点**："孩子可在成年（成年年龄取决于国家或地区）后关闭此监控。"

三者构成一组少见的**双向知情设计**：管理者需要凭据才能施加限制，被管理者会被告知限制已被施加，且限制自带到期机制。

## 11.5 治理数据自身的隐私

屏幕时间的使用数据不是明文上送的：

> "使用数据和配置设置使用端对端加密的 Apple 身份服务 (IDS) 协议在家长和孩子的设备间传输。加密数据可能会短暂储存在 IDS 服务器上，直到接收设备读取了此数据（例如，如果 iPad 或 iPhone 处于关机状态，则只要开机，就视为已读取数据）。Apple 无法读取此数据。"

以及同步条件与删除语义："对于管理自己的设备使用情况的用户，"屏幕时间"控制项目和使用数据可通过 CloudKit 端对端加密在关联了相同 iCloud 账户的设备间同步。此功能需要用户账户启用双重认证（同步默认打开）。"；"用户清除 Safari 浏览器历史记录或删除 App 时，对应的使用数据也会从该设备和所有同步的设备中移除。"

**治理工具本身的日志被端对加密保护**，这是全系列"元数据也是受保护数据"论点的一个具体样本，与 #77 期的覆盖面分析可并读。

该页还列出被收集的两类匿名使用统计，并明确边界："Apple 不会收集特定 App 或网页的使用数据。用户在"屏幕时间"使用信息中看到 App 列表时，App 图标是直接从 App Store 拉取的，不会保留这些请求中的任何数据。"

## 11.6 `restricted` 态是治理留下的可见痕迹

第 4 章记录的 `restricted` 语义（"the user can't grant such permission"）在此处获得它的制度解释：它不是"某个未知原因导致的不可用"，而是**家长式/管理式封禁在状态 API 上的投影**。三种治理来源都可能把它写进状态：屏幕时间的内容与隐私访问限制、MDM 访问限制负载、以及设备侧的全局类别开关。

公开资料未提供"如何区分这三者"的 API 或字段（记入 U 组）。因此本篇对 `restricted` 的处置建议与 §10.4 一致：只描述为"当前无法访问，可能受策略限制"，不猜测来源。

## 11.7 三层优先级的实际后果

把 §9 与本章合起来，可得到一条治理优先级描述（本篇以"观察到的机制组合"提出，不作为官方规则引用）：

1. **硬件层**（合盖断联麦克风，§5.5）在任何软件状态之上，且对任何管理者都不可越出。
2. **治理层**（访问限制负载、屏幕时间、受监督锁定）决定**用户能否作出某个选择**。
3. **用户层**（弹窗、设置页、还原位置与隐私）决定**在允许选择的范围内选哪一个**。

因此"管理员 vs 用户谁赢"是个误设问——他们不在同一层竞争，而是分别控制"选项集"和"选项值"。这也解释了 §9.2 中"批量清零只清用户层"的原因：还原位置与隐私不会解除访问限制负载或屏幕时间密码。

## 11.8 与锁定模式的关系

#87 §5 已记录锁定模式对九类功能的影响，其中包含部分同意路径被收窄。本篇补充的是**定位差异**：锁定模式是**用户自己可以打开的一键治理**，其效果在类别层面而非 App 层面，且可随时关闭；MDM/屏幕时间是**他人施加的治理**，其效果可跨类别且需要凭据才能变更。两者共享"用改变选项集来降低风险"的同一手法，但发起者不同，这也是第 15 章分角色建议中"个人用户可用的最强手段"这一结论的依据。

---

# 12. 可观测性：谁能看见授权发生过

## 12.1 四个观测面及其强度

| 观测面 | 观测者 | 覆盖 | 强度与局限 |
|---|---|---|---|
| 硬件指示灯（相机） | 在场观察者 | 相机 | 官方表述为设计可阻止未使用相机时点亮，"即使在 macOS 中拥有根或内核权限"也无法绕过（§5.4） |
| 硬件断联（麦克风） | 在场观察者 | 麦克风 | 不产生任何可记录事件——它使事件不可能发生（§5.5） |
| 设置 > 隐私与安全性 目录 | 用户 | 全部需同意类别 | 权威当前态视图；不呈现历史与逐次访问 |
| 状态 API（PH/CL/AV 等） | App 自身 | 各类别形态不一 | 存在 §10.6 的三处失真；只能读自身状态 |
| 系统日志 / 崩溃类报告 | 设备管理、研发 | 与拒绝相关的诊断 | #87 §10 处理过沙盒违规日志；授权拒绝是否进入同一可读通道，公开资料口径不完整（U 组） |

## 12.2 "读不到真值"的三种形态

第 10.6 节的三条陷阱在观测面上的统一表述是：**状态 API 的返回值可能是"用户意图的编码"、也可能是"系统降级后的意图编码"、还可能被有意设计成不可判定**。三者对审计的价值不同：

- 意图编码可用于产品逻辑，不可用于合规举证。
- 降级编码（如 limited 报 authorized）会**主动误导**审计，必须以实际访问结果反推。
- 有意不可判定（健康读权限）意味着**不存在正确的探测代码**，只有正确的错误处理。

## 12.3 与 #87 的分工

#87 回答"访问被拦时会看到什么"（沙盒违规、errno、面板拒绝），本篇回答"访问被允许/未被允许的状态从哪来、谁能改"。两者相接处只有一条：**同意层在沙盒之上**。#87 §5 记下的"数据保险箱甚至本身未沙盒化的进程也不可访问"与"无法通过授权属性或代码签名获得全盘访问"说明：即便越过围栏层，仍有一层由同意状态决定的边界；反之，用户给了同意也不会让围栏失效。任何"用户同意了所以能访问 X 路径"的推论都必须再走一遍 #87 的九步判定链。

## 12.4 面向运维的可观测性建议（防御向）

1. 把**权限目录快照**纳入 MDM 报表：受监督设备上的类别开关与访问限制负载是可下发的，也应是可核对的（与 §11.1 的"部分情况下可移除"配对——先确认哪些在你策略里可移除）。
2. 对**指示灯不可覆盖的场景**（无灯设备、远程会话）不要把"没有点亮记录"当成"没有访问"的证据。
3. 授权变更缺少统一的外部事件通道，因此**任何"权限变更审计"需求都应以设备侧配置快照与用户侧设置导出为证据**，而不是以 App 行为日志为证据。

---

# 13. 平台与版本差异

| # | 差异项 | iOS / iPadOS | macOS | watchOS | tvOS | visionOS |
|---|---|---|---|---|---|---|
| 1 | 位置两档差别 | 实质不同（前后台与拉起规则） | "functionally equivalent" | 复杂功能视同在用；即使 Always 也不拉起 App | 不支持 Always | 不支持 Always；"在看"定义在用 |
| 2 | 用途描述键（位置） | WhenInUse（请求任一档都必需）+ AlwaysAndWhenInUse | 另需 `NSLocationUsageDescription` | 按 Core Location 可用性 | — | 同 iOS 键名 |
| 3 | 缺失键的后果 | 相机/麦克风类：系统终止 App；位置类：请求立即失败 | 同 iOS 口径（各框架按自身文档） | 未逐框架披露 | 未逐框架披露 | 未逐框架披露 |
| 4 | 照片受限档 | iOS 14 起 `limited`，回溯适用已上架 App | PhotoKit 行为存在，档位口径以框架文档为准 | 不适用 | 不适用 | 不适用 |
| 5 | 选择器路径 | PHPicker 独立进程、无需授权 | 有相应选择器路径 | — | — | — |
| 6 | 精度轴 | `NSLocationDefaultAccuracyReduced`（iOS 14+） | 该键可用性：visionOS 1.0+、watchOS 7+、iOS/iPadOS 14+，macOS 未列 | watchOS 7+ 列于键的可用性 | 未列 | visionOS 1.0+ |
| 7 | 麦克风硬件断联 | 不适用（无相应机盖结构） | MacBook Neo（A18 Pro）机型配指示灯与断联设计 | 不适用 | 不适用 | 不适用 |
| 8 | 家长式治理 | 屏幕时间（iOS 12+） | 屏幕时间（macOS 10.15+），替代"家长控制" | 部分功能（watchOS 6+） | 未披露 | 未披露 |
| 9 | 注册形态 | 用户/设备/自动设备注册（iOS 13、iPadOS 13.1+） | 同（macOS 10.15+） | 经由配对 iPhone 镜像部分访问限制 | — | visionOS 1.1+ 列入注册支持 |
| 10 | 健康共享 | iOS 15+ 两人共享、与提供者共享，均需双重认证 | 以"健康"App 形态存在，共享条件按 iCloud 双重认证 | 数据可入健康库，授权由 iPhone 侧管理 | 不适用 | 不适用 |

表中"—/未披露"表示公开资料未在本篇取证范围内给出对应平台的明确口径，不表示该行为不存在。

## 13.1 版次提醒

本篇引用的《Apple 平台安全保护》页面**发布日期各不相同**：健康数据页 2026-01-28、Mac 屏幕相机指示灯页 2026-03-11、硬件麦克风断联页与屏幕时间页均为 2021-02-18、配置执行页 2024-05-07、设备管理安全性概览页 2024-12-19。开发者文档页脚统一标注 "Copyright © 2026 Apple Inc."，但**单页正文未标发布日期**。

因此不可再沿用"整本指南统一滞后到某一年"的说法（此前篇目曾如此假设）。**跨页比较时效时，必须以各页自身发布日期为准**；对仅有页脚年份的开发者文档，应记为"截至 2026 年可用"而非"2026 年更新"。

---

# 14. 口径冲突、未披露与必须并读

## 14.1 口径冲突表（C 组）

| 编号 | 冲突 | 口径 A | 口径 B | 本篇处置 |
|---|---|---|---|---|
| C-01 | 缺用途描述的后果 | 相机/麦克风："the system terminates your app" | 位置："Authorization requests fail immediately" | 按框架分述，禁止合并为"会崩溃" |
| C-02 | "权限"指什么 | #86/#84 语境：entitlement（授权属性） | 隐私语境：user consent（同意） | 全篇使用 §1.1 三词分离，出现"权限"必带限定 |
| C-03 | `restricted` 的成因 | PH/CL 框架：不说明成因 | 口语常等同"受家长/管理限制" | 仅描述为不可由用户授予；不推断来源 |
| C-04 | 部分同意是否为一等状态 | PhotoKit：`limited` 是一档状态 | 多数类别：无子集档，只有允许/拒绝 | 把"子集授权"记为照片特有，不外推 |
| C-05 | "记住同意"的适用范围 | "the system remembers the person's choice and doesn't prompt again" | 位置/照片存在再次提示、扩充提示、系统周期性提示 | 记忆规则成立，但**再提示路径存在**，两者并列陈述 |
| C-06 | 端对加密是否为默认 | 健康 iCloud E2E 需 iOS 12+ 且双重认证 | 屏幕时间同步"默认打开"且需双重认证 | 一律写成条件式，不写"Apple 默认端到端加密" |
| C-07 | 受监督锁定强度 | "这旨在防止其移除" | 常被复述为"无法移除" | 保留"旨在"；同时记录"可只允许密码移除"分支 |
| C-08 | 撤销范围 | "还原位置与隐私"（整机全服务） | `tccutil reset <service>`（按服务） | 分列于 §7.9 表，不合并为"重置权限" |
| C-09 | 访问限制可否移除 | "在部分情况下也可移除" | 受监督下描述文件可锁定 | 分述：限制项本身与描述文件是两个对象 |

## 14.2 未披露项清单（U 组）

| 编号 | 未披露内容 | 已披露到的边界 |
|---|---|---|
| U-01 | 同意状态的存储位置、格式与守护进程 | 仅"系统存储"与"健康的管理数据存于 Before First Unlock 类" |
| U-02 | 授权变更是否产生面向外部可查询的统一事件/日志 | 状态 API 与设置视图可查当前态，历史通道未披露 |
| U-03 | `AVAuthorizationStatus.restricted` 与各治理来源之间的映射 | 各状态定义均不注明成因 |
| U-04 | 哪些访问限制项在设备侧可被用户或管理员移除的完整清单 | 仅"部分情况下也可移除" |
| U-05 | 各框架"子集授权"能否统一到同一模型 | 仅照片给出 `limited` 一等档 |
| U-06 | 位置"精确位置"关闭时的实际精度量级 | 官方仅称"approximate location"，未给半径 |
| U-07 | 抑制自动提示（`CLRequireExplicitServiceSession`）下若 App 从不建会话的最终行为 | 仅给出启用方式与示例 |
| U-08 | tvOS / visionOS 上多数用途描述键的必需性与失败语义 | 开发者文档以 iOS/iPadOS/macOS 为主 |
| U-09 | 已被撤销的同意是否留有可导出的历史记录 | 未披露 |
| U-10 | 上述清单为穷尽性说明 | 本篇只列出取证过程中明确撞到的缺口，不宣称已穷尽全部未披露项 |

## 14.3 三组必须并读的结论

**第一组：本篇 §1.1 三词分离 × #86 能力声明 × #87 §3 键语义。**
只读本篇会把"用户同意了"当成访问成立的充分条件；只读 #86 会把 entitlement 当成权限本身；只读 #87 会漏掉同意这一层的可变性。三者并读才得到完整链：**能力是否存在（签名/授权属性）→ 是否被围栏允许（沙盒/数据保险箱）→ 是否被主体同意（本篇状态）**。

**第二组：本篇 §11.7 三层优先级 × #87 §5 同意目录 × #80 设备真实性。**
治理层能改选项集，但选项集的默认值与可改性取决于设备是否受监督、是否登记（#80 的注册/证明路径）。脱离设备管理形态谈"能不能强制关闭相机权限"是无效的。

**第三组：本篇 §8.2 反推断设计 × #77 数据采集覆盖面 × §12.4 审计证据选择。**
健康层"以空数据代替拒绝"的设计，使得任何"以 App 可见行为推断授权状态"的审计方法在该类别上必然失真；而 #77 的覆盖面结论提醒：**App 拿不到的数据类型不等于不存在的数据**，两者是同一种推理错误的两个实例。

---

# 15. 结论

## 15.1 公开资料足以支持的四类结论

1. **同意是被持久化的状态对象**，由系统记忆、用户可在设置中改、并有批量清零路径；因此"管理"是这套体系的一等需求，而不是合规附加物。
2. **授权的粒度在不同类别上是设计选择而非统一规则**：照片可细到资产集，健康可细到"类型 × 方向"，位置可细到"档位 × 精度"，多数类别只有开关。
3. **存在有意牺牲可探测性的设计**：健康读取权限不可完全确定，"如同查询空数据库"，官方说明目的在阻止推断用户健康状况。
4. **硬件与治理层在软件同意之外**：机盖断联麦克风与访问限制负载分别构成"授权无法触及"与"用户无法决定"的两类边界。

## 15.2 公开资料不足以支持的四类常见说法

1. "用户授权后 App 就能访问该路径" —— 忽略了围栏层与数据保险箱（须并读 #87）。
2. "App 能通过 API 判断自己有没有某项权限" —— 在照片、位置、健康三处均为假或不可靠。
3. "MDM 可以远程撤销某 App 的相机权限" —— 公开资料只支持"下发访问限制以阻止访问特定 App、服务或功能"，未支持"改写既有同意状态"这一表述。
4. "Apple 端到端加密所有隐私数据" —— 已披露的 E2E 均是条件式的（版本 + 双重认证），且备份形态另有前提。

## 15.3 分角色建议

**App 开发者 / 安全负责人**
- 把权限目录纳入构建校验（键齐备、值非空、已本地化、SDK 引用与键对应），把 `ITMS-90683` 当成本地检查项而非上架意外。
- 一律以状态变化通知为唯一更新源；不缓存首次结果；每次进入功能前读回。
- 拒绝路径的文案必须覆盖"个体被拒"与"整机关闭"两种可能；`restricted` 不写成"您已拒绝"。
- 需要引导流程时用官方给出的会话/显式请求机制控制提示时机，而不是提前批量请求；记住 Always 只有一次机会。
- 对健康数据，不要写探测权限的代码，改为"无数据"视图 + 明确的授权入口。

**企业 / 教育机构 IT**
- 先确定注册形态（用户注册 / 设备注册 / 自动设备注册），因为它决定能否把描述文件锁定到设备；BYOD 场景下个人 App 的同意不属于你的管辖范围。
- 建立"访问限制项可移除性"清单（U-04），把策略项标注为"设备侧可改"或"不可改"，避免事后按错误假设排障。
- 权限审计以配置快照与设置导出为证据，不指望存在统一的权限变更日志通道（U-02）。
- 定期核对：屏幕时间密码策略、锁定模式使用率、受监督状态是否因重新注册而丢失。

**家长 / 个人用户**
- 施加孩子限制必须先设屏幕时间密码，否则限制可被改动；同时知道孩子会收到"已开启"的通知，且成年后可自行关闭。
- 个人最弱也最快的一键手段是锁定模式与各类别总开关；最强的是"还原位置与隐私"——它把全部历史同意清零，代价是所有 App 都要重新问一遍。
- 无指示灯的访问场景不能靠"没看到灯"作证据；麦克风类应结合机型是否具备硬件断联来判断。

**安全研究者 /  defenders**
- 把同意层当作**状态机**而不是检查点建模：它的输入是弹窗与设置，输出是持久状态，转移由用户、家长、管理员、还原动作四方触发。
- 关注"不可探测"设计的两侧：它是用户隐私保障，也同时是检测盲区——任何依赖"App 知道被拒"的推断都要重新验证。
- 任何绕过同意的说法都必须先证明它改变的是状态来源（持久化存储）而不是仅仅读取失败后的降级路径；此类验证应限于自有设备与授权环境。

## 15.4 一句话总结

这套体系管理的从来不是"能不能访问"，而是**"谁有权决定用户可以决定什么"**：硬件划定上限，治理层划定选项集，用户在集合内作出被系统长期记住的一次选择，开发者只能争取这次选择的时机与措辞——而 Apple 在某些类别上，宁可让 App 无法确认自己拿到了什么，也不让用户被反推出来。

---

# 附录

## A.1 来源与版次

**《Apple 平台安全保护》（support.apple.com/zh-cn/guide/security/...，中文页，含各页自身发布日期）**

| secid | 页面 | 发布日期 | 本篇用途 |
|---|---|---|---|
| `secc01781f46` | 保护 App 对用户数据的访问 | 见页脚（2026 版权年） | §1.2 默认拒绝、同意目录 |
| `sec88be9900f` | 保护对用户健康数据的访问 | 2026-01-28 | §8 全章 |
| `secbbd20b00b` | 硬件麦克风断联 | 2021-02-18 | §5.5 |
| `sec75a2d237d` | Mac 屏幕相机指示灯 | 2026-03-11 | §5.4 |
| `sec525461d19` | 相机安全性 | 见页脚 | §5 |
| `secd8831e732` | "屏幕时间"安全性 | 2021-02-18 | §11.4、§11.5 |
| `secf6fb9f053` | 配置执行 | 2024-05-07 | §11.1—§11.3 |
| `sec013b5d35d` | 设备管理安全性概览 | 2024-12-19 | §11.3 |
| `sec38eb8731b` | 安全设备管理概览 | 见页脚 | §11 上下文 |
| `sec2437264f0` | 锁定模式安全性 | 见页脚 | §11.8（主体在 #87 §5） |
| `sece8608431d` / `secb010e978a` | 数据保护 / 数据保护类 | 见页脚 | §8.3 保护类口径 |
| `secddd1d86a6` | 控制 App 对文件的访问 | 见页脚 | §12.3 与 #87 相接处 |
| `sec8a37b4cb2` | 管理式设备证明 | 见页脚 | §14.3 第二组 |

**Apple 开发者文档（developer.apple.com，页脚统一为 Copyright © 2026 Apple Inc.，单页无发布日期）**

| 页面 | 本篇用途 |
|---|---|
| Technology Overviews > Security > Requesting access to protected resources | §1.2、§2 授权三类别与提示生成规则 |
| Information Property List > Protected resources（受保护资源键目录） | §3 全部四组键表 |
| Information Property List > 各 `NS*UsageDescription` 单页（含 `NSLocationDefaultAccuracyReduced`、`NSAlwaysAllowMicrophoneModeControl`） | §3.1、§5.6、§7.5 |
| Core Location > Requesting authorization to use location services | §7.1—§7.7 |
| Core Location > Suspending authorization requests | §7.8 |
| Core Location > CLAuthorizationStatus / CLAccuracyAuthorization / CLServiceSession | §7.6、§7.5、§7.8 |
| Core Location 框架总览（Essentials / Authorization 主题分组） | §7 结构、§7.7 的 `CLLocationUpdate`/`CLMonitor` 口径 |
| HealthKit / PhotoKit（`PHPickerViewController`、`PHAuthorizationStatus`）/ AVFoundation（`AVAuthorizationStatus`） | §4、§5.3、§6 |
| App Store Review Guidelines 与上传校验信息（`ITMS-90683`） | §2.3、§10.5 |
| BundleResources > Entitlements 两份目录 | §1.1 三词分离（与 #86 并读） |

访问日期：2026-10-22。所有中文引号内译文为 Apple 中文页原文；英文引号内为 Apple 英文开发者文档原文，未作改写。

## A.2 术语表

| 术语 | 本篇定义 |
|---|---|
| 授权属性 / entitlement | 代码签名内的能力声明，决定通道能否存在；不由用户授予（#84/#86） |
| 用户同意 / consent | 系统按 App × 类别持久存储的授权状态；由用户作出并被记忆 |
| 用途描述 / purpose string | `Info.plist` 中的说明字符串，随请求出现在系统提示中 |
| 提示 / prompt | 由系统代替 App 生成的弹窗；标题由系统自动生成 |
| 一次打断 / one-time interruption | 首次请求产生的唯一提示；此后系统不再提示 |
| 档位 / access level | 同一资源内的授权强度（When in Use / Always；照片的全量/所选） |
| 精度轴 / accuracy authorization | 与档位正交的 `fullAccuracy`/`reducedAccuracy` 维度 |
| `notDetermined` | 用户尚未作出选择 |
| `restricted` | App 未被授权且用户无权授予（成因不披露） |
| `denied` | 用户拒绝，或该类服务在设置中被整体关闭 |
| `limited` | 照片的受限库子集授权 |
| 反推断设计 | 以"如同查询空数据库"代替"返回被拒"的授权语义 |
| 访问限制 / restriction payload | 配置负载中阻止用户访问特定 App、服务或功能的部分 |
| 受监督 / supervised | 经自动设备注册等形态获得的设备管理强度，决定描述文件可否被锁定 |
| 注册描述文件 / enrollment profile | 在设备管理服务中注册设备的负载；一台设备一次只能有一个 |
| `CLServiceSession` | 提供 App 使用位置服务授权诊断的对象，也是挂起自动提示的入口 |
| 硬件断联 | 由物理状态（机盖/结构）在硬件层切断麦克风，软件授权无法覆盖 |

## A.3 口径声明

1. 本篇为**防御性、教育性**机制分析，不含任何绕过授权、伪造状态、篡改同意存储或规避审查的方法、脚本与步骤；不提供利用链、PoC 或检测规避点位。
2. 所有机制性结论以 Apple 官方公开文本为准；引号内为原文（中文页为 Apple 官方译文，英文为开发者文档原文）。**"设计为/旨在/帮助防止"一律不升级为"保证/一定/无法"**；出现"旨在"处均保留原限定词。
3. 公开资料未披露的项目在 §14.2 以 U 组明确标注，正文不推测、不以"通常认为"替代。
4. 术语区分：授权属性（entitlement）≠ 用户同意（consent）≠ 用途描述（purpose string）；"权限"一词在本篇不单独使用，出现时必带其中一种限定。
5. 不同框架对同一状态词可能给出不同含义，同一失败在不同框架可能给出不同后果；本篇按框架分述，合并口径一律记入 §14.1 C 组而不采为结论。
6. 本篇不评价任何具体 App 的隐私合规性，不涉及未公开的内部实现、私有 API 或逆向所得；跨平台对比表中的"—/未披露"仅表示取证范围内未获口径。
7. 引用页发布日期分散（2021—2026，见 §13.1），读者复核时应以当次访问页面为准；若 Apple 后续修改原文，应以新版为准并重新核对本篇受影响小节。

---

**联系方式**：Telegram: https://t.me/ios_one001


