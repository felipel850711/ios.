@@TITLE@@iOS 设备安全与极客防线：设备真实性防线与极客对抗全景
@@SUBTITLE@@Device Integrity Versus the Modding Underground — Apple's DeviceCheck and App Attest Signals, Enterprise Distribution Controls, and What Jailbreak-Detection Research Actually Shows
@@INFO@@报告日期|2026年10月19日
@@INFO@@分析对象|DeviceCheck / App Attest / Managed Device Attestation / 设备标识与审核政策 / 越狱与篡改检测的公开研究 / 未授权分发生态的公开观测
@@INFO@@视角定位|设备真实性防线的四层结构（端侧—平台侧—服务端—生态侧）与对手能力类别的对照评估
@@INFO@@分类|网络安全 · 设备完整性 · 风控与反滥用
@@INFO@@难度|中级偏高级（需了解 iOS 代码签名、MDM 与服务端风控的基本概念）
@@INFO@@联系方式|Telegram: https://t.me/ios_one001

@@TOC@@目录

# 概述

## 1.1 为什么「设备真实性」是这一系列的空白

本系列已有篇目的覆盖方式可以归纳为四条轴：漏洞类别轴（按 CVE 与利用链组织）、机制轴（按内核、代码签名、Secure Enclave、沙箱组织）、数据类别轴（按可采集字段与外泄路径组织）、威胁映射轴（按「威胁类型→保护层」矩阵组织）。四条轴都写到了「设备被改动之后会怎样」，但没有任何一篇把**服务端如何获知这台设备是真的**当成独立研究对象来写。这不是疏忽，而是这一条防线长期缺少可核验的一手文本：Apple 关于 DeviceCheck、App Attest 与 Managed Device Attestation 的公开材料散落在 developer.apple.com 的接口文档、MDM Protocol Reference 与部署指南里，且从未汇总成一章【L1 Apple 文档；简报逐页核对】。

把系列篇目逐项对照后，缺口位置很清晰：

| 已有篇目与其相关章节 | 它实际写到的层面 | 它没有写的部分 |
| --- | --- | --- |
| 《iOS典型漏洞攻击链安全分析报告》第 5 章「越狱攻击链分析」 | 具体越狱工具的版本区间与漏洞组合 | 这些能力存在之后，服务端还能不能判断设备可信 |
| 《iOS系统防御与机制绕过》第 11 章「越狱防御与绕过」 | 检测点位与绕过现象的机制描述 | 这些检测的有效性有没有被公开测量过 |
| 《iOS开发与技术栈》12.6「越狱检测」、第十四章企业分发与证书管理 | SDK 与工程实现清单 | Apple 规则层的准入、吊销生效窗口与责任归属 |
| 《iOS供应链安全分析报告》4.1、4.3、4.4 | 证书滥用谱系、第三方商店与侧载风险 | 同行评审测得的灰产结构与设备身份资产化的量化口径 |
| 《iOS核心保护作用》1.6 十三类威胁总览、2.1 七要素复用模式 | 威胁与保护层的映射矩阵 | 「不可信设备」作为跨类别前提的独立展开 |
| 《iOS身份认证与通道加密》第 2 章「设备级身份认证」 | 通道与证书层面的设备身份 | 该章的「设备级身份」是 TLS/Apple ID 语义，与本篇的三条 Apple 服务端可验证通道不是同一对象 |

因此本篇的定位不是「再讲一次越狱」，而是把**设备真实性防线**这一横切层单独建起来：它给什么信号、信号的语义上限在哪、判定成本落在谁身上、公开研究对端侧检测的可靠性说了什么。该层在既有框架里长期只以一句话出现——例如威胁映射中「对手没有越狱、没有漏洞、没有绕过任何一层隔离」这类前提式表述——本篇要做的就是把这些前提的证据与责任分配写清楚。

需要预先声明的是本篇的**口径独立性**：凡涉及机制语义，只引 Apple 一手文档原文；凡涉及检测有效性，只引带会议与年份的同行评审论文；两者之间不做因果补链。Apple 没说「因此重签名可绕过检测」，公开研究也没说「App Attest 已被绕过」，这两句都不能由本篇拼出来。

## 1.2 本报告要回答的四个问题

全篇围绕四个问题组织，每个问题的答案都有明确的来源层级约束，且其中两个的答案是「缺口」而不是「结论」。

**问题一：Apple 到底能让服务端知道什么？**答案限于三条通道：DeviceCheck 的每设备两个二进制位、App Attest 的密钥证明与断言、MDM 侧的 Managed Device Attestation【L1 Apple 文档】。框架页对前两者的定位原文是一句话同时覆盖的："Reduce fraudulent use of your services by managing device state and asserting app integrity."；第三条则来自 MDM Protocol Reference 的 `DevicePropertiesAttestation` 字段。除这三条之外，Apple 没有公开第四种「服务端可验证的设备真实性输入」。

**问题二：这些信号的语义上限在哪里？**上限由 Apple 自己写下，且只有两句涉及系统被改动："App Attest can't definitively pinpoint a device with a compromised operating system"（框架页）与 "an attacker that modifies the device's operating system might bypass restrictions"（`assessing-fraud-risk`）【L1 Apple 文档原句】。两句都必须以「Apple 原文」限定引用，不得扩写为「App Attest 无法防模拟器/越狱」这类 Apple 未说过的结论。

**问题三：公开研究对「检测能否站住」说了什么？**可核实的答案来自三篇同行评审工作：EuroS&P 2019 对银行类应用越狱检测的测量、EuroS&P 2025 的 SoK（工具名 HALY）对跨平台加固技术的静态/动态双口径测量、USENIX Security '26 对未授权分发灰产的测量【L2 EuroS&P 2019 / L2 EuroS&P 2025 / L2 USENIX Security 2026】。三者共同指向：端侧检测的可靠性取决于实现与是否多层叠加，而**服务端可验证机制的实际采用率极低**（HALY 测得 App Attest 在 iOS 应用中出现率为静态 4.1% / 动态 1.1%，作者自述该数字应视为下界）。

**问题四：判定与处置责任实际落在谁身上？**Apple 的答复是明文的责任外推："No single policy can eliminate all fraud. … the DeviceCheck services provide information that you can integrate into an overall risk assessment for a given device."【L1 Apple 文档】即平台提供信号与一个近似计数指标，阈值与处置规则归开发者；而在企业侧，纳管与证明链校验归 MDM 管理员。第 4 章专门处理这条口径及其边界。

| 问题 | 答案类型 | 主要依据层级 |
| --- | --- | --- |
| Apple 能让服务端知道什么 | 正面结论（三条通道） | L1 Apple 文档 |
| 信号的语义上限 | 正面结论 + 原句限定 | L1 Apple 文档原句 |
| 检测能否站住 | 部分结论，含量化口径冲突 | L2 同行评审论文 |
| 判定与处置责任 | 正面结论（平台外推责任） | L1 Apple 文档 |

四个问题里最难回答的其实是第一个的反面：Apple **不**让服务端知道什么。这个问题的答案只能靠逐字段核对得到，见 3.3 与 2.5。

## 1.3 范围声明：本篇不覆盖什么

范围声明在本篇不是礼节性段落，而是证据分工的前提。三类内容一律不写：

**其一，不重讲漏洞利用链。**越狱工具的具体版本区间、漏洞组合与利用条件，见《iOS典型漏洞攻击链安全分析报告》第 5 章；越狱里程碑案例见《著名iOS漏洞案例》第二章。本篇只把这些能力作为「对手能力存在性」的时间锚点引用，不复述案情，也不重复工具名与版本的对应关系。

**其二，不重讲底层机制。**Secure Enclave 的内部密钥层级、代码签名在内核中如何被强制、SPTM 与启动链细节，分别归《iOS钥匙串窃取》第 2–8 章、《iOS内核安全机制深度解析》与《iOS核心底层技术支撑》。本篇引用 Secure Enclave 只为支撑一句话：App Attest 私钥的驻留位置与「任何进程无法直接读取或修改」的 Apple 原述【L1 Apple 文档 `establishing-your-app-s-integrity`】。硬件与启动链原理一律以「详见该篇」处理。

**其三，不写任何对抗实现。**这是硬约束。凡涉及对手能力，本篇只允许写三件事——**能力存在性、失效结论、证据强度**——不写检测点位清单、不写 hook 位置、不写绕过用的组件名称与用法、不写设备机群的搭建方式。同行评审论文中确实列举过的检测点位与规避组件，本篇仅以「论文自陈存在此类组件，且多数绑定特定越狱与特定 iOS 版本、不开源」的形式提及结论层，实现层标注「本报告不收录」。同理，重签名与改造包只作为「App 完整性」概念的被测量对象出现，不涉及签名流程本身。

除内容边界外，还有四条**术语边界**必须预先划清，否则与既有篇目会产生语义撞车：

| 易撞车术语 | 本篇采用的含义 | 不承担的含义 |
| --- | --- | --- |
| 「设备真实性」 | DeviceCheck / App Attest / Managed Device Attestation 三条 Apple 服务端可验证通道 | TLS 与证书层面的设备身份、Apple ID 认证（归《iOS身份认证与通道加密》第 5、8 章） |
| 「识别」 | 平台侧真实性判定信号 | 端侧机器学习识别栈（Core ML、Vision、Apple Intelligence，归《iOS智能识别》） |
| 「设备数据」 | 标识符的官方语义与政策条款 | 采集能力矩阵、字段清单、隐私标签（归《iOS数据采集能力深度分析》） |
| 「保护有效性」 | 单个机制的官方语义与已公开的失效模式 | 汇总打分与最终矩阵（仍由《iOS核心保护作用》第 16 章承担，本篇不输出矩阵） |

另一处必须提前说明的是「越狱」一词本身：**Apple 平台安全指南（August 2026 版）全文出现 "jailbreak" 0 次**，其口径是 untrusted code / unsigned apps / compromised；DeviceCheck 文档口径是 "a device with a compromised operating system"【L1；负面结果由本地全文核对支撑】。本篇使用「越狱」时，指称的是社区与论文用法，不代表 Apple 文本中存在以该词建立的技术条目或判定接口。同时须区分三个在中文报道里常被混写的词：越狱、解锁（运营商锁或 Android 的 bootloader 解锁）、修改/魔改（论文用于 modified apps）——三者在本篇不互相指代。

## 1.4 读者画像与阅读路径

本篇面向三类读者，三类人需要的东西差别很大，因此给出不同的入口。

**第一类：做 iOS App 的服务端与风控工程。**核心需求是「我该信任客户端上报的哪一部分、服务端要自己建哪一部分」。入口是 3.2、3.5 与第 4 章。需要带走的硬结论有三条：Apple 明文否定了应用自证路线（"You can't rely on your app's logic to perform security checks on itself because a compromised app can falsify the results."）【L1】；App Attest 的私钥不跨重装、迁移与备份恢复存活，因此服务端必须支持同一用户多次重新证明并保存多组 (key, receipt)；风险指标是「近 30 天该设备上的 attested key 近似计数」，需要自行调阈值【L1】。集成侧的运维项（ES256 JWT 密钥体系、challenge/counter 状态机、429/503/304 退避、Apple 私有 CA 根证书托管）在本篇只列存在性，实现清单归工程类篇目。

**第二类：做企业设备管理与合规评估。**入口是 3.3、3.4 与 4.3。关键区别在于：设备完整性级别的证明能力（Managed Device Attestation）只在**受管设备**上存在，且以 MDM 注册与相应 access right 为前提；BYOD 与个人设备没有任何对应字段。硬件门槛（A11 Bionic 及以后、全部 M 系列）与 iOS/macOS 可证明属性集的不对称（SIP、Secure Boot 模式、第三方 kext 许可三项**仅 macOS 14.2+**）是直接决定评估结论的两条事实。

**第三类：做安全评估与威胁建模的研究者。**入口是第 2 章与 3.6。需要带走的是证据分级纪律本身：iOS 侧对抗面的证据密度显著低于 Android，而这一不对称主要源于可观测性与工具链限制，不能推定为风险更低。

对只想快速核对口径的读者，本篇的三个「最易被下游写手写错」结论集中在：`isSupported` 是**只读属性**而非方法调用；DeviceCheck 的两个 bit **由开发者服务端改写**，「不可重置」这句 Apple 原话指的是 App Attest 的指标而非 bit；App Attest 的曲线与密钥长度、模拟器行为、速率上限、SLA 均为**Apple 未公开**，任何把这三项写成事实的二手文章都属口径错误。

阅读路径上建议线性读完第 2 章后再进第 3 章：第 3 章的每个论断都挂了来源层级标注，若跳过分级体系，读者无法判断「已核实」与「未获得可核实条目」之间的差别，而本篇相当一部分内容正是后者。

## 1.5 诚实边界：对抗面证据的系统性缺口

本篇对抗面的证据大量来自学术材料与厂商材料，且存在**系统性缺口**——不是零星缺引用，而是若干整类问题在公开文献里没有可核实条目。报告会把这些写成审计缺口而不是沉默，也不会因为查不到就反向断言其不存在。下表是全篇缺口清单，后文凡涉及这些项均回指此处。

| 缺口项 | 缺口性质 | 影响的正文位置 |
| --- | --- | --- |
| 「A12 及以后长期无公开可用越狱」的时间边界；bootrom 类利用的芯片代际边界 | 未取得 Apple 一手或学术直接表述，仅有间接旁证 | 3.3、3.6 |
| iOS 越狱检测**误报率**的任何测量数据 | 未取得同行评审来源；只有厂商承认与社区个案 | 3.6 |
| App Attest / DeviceCheck 的独立弱点或绕过研究 | 本次检索**零篇**同行评审论文 | 3.2、4.2 |
| Apple 公布的企业证书吊销数量或年度统计 | 未取得；Apple 以规则与准入审查表述 | 4.3 |
| 以 iOS 为对象的设备农场/群控大规模测量；iOS 模拟器或云手机被滥用的任何一手证据 | 未取得；可核实的同类研究均为 Android 或跨平台账号自动化 | 3.5 |
| 把检测信号按「易伪造程度」**量化排序**的公开研究；重签名包检出率/误报率的第三方评测 | 未取得，仅有定性判据 | 3.6 |
| Apple 关于模拟器/虚拟化环境下 DeviceCheck 与 App Attest 行为的说明 | 属「Apple 未公开」，非「不适用」 | 3.2 |
| App Attest 私钥曲线与密钥长度、支持机型枚举、`risk metric` 的计算定义、服务端速率上限与 SLA、receipt 保留期 | Apple 未公开 | 3.2、4.1 |
| 两篇 2026 年 iOS 第三方市场测量论文（ACM WiSec 2026、ACM IMC 2026）与一篇 ESORICS 2025 workshop 论文的样本量与结论数字 | 仅完成题录核对，全文未取得 | 3.5 |

缺口本身要区分两种性质，二者的写作义务不同。**「未取得」**意味着检索做了但没拿到可引用依据，正文只能写「本次核查未获得可核实条目」，并保留该项可能存在证据的空间；**「Apple 未公开」**意味着主体本身不发这类口径，正文可以写「Apple 未公开 X」，但不能写「不存在 X」。把这两类混成一句「没有证据表明」是最常见的失真来源，尤其容易发生在 MDM 字段清单这类可以逐字段穷举核对的项目上——穷举核对得到的否定结论是硬证据，而检索失败得到的否定结论不是。

还有一层诚实边界必须写明：**证据密度不对称不等于风险不对称。**iOS 侧对抗面叙述之所以薄，主要因为大样本应用分析依赖可用越狱覆盖（EuroS&P 2025 的作者自述其越狱仅覆盖到 iOS 16、实验机型为 A11【L2】）、自动化动态分析依赖闭源付费虚拟化平台（同一论文记录 MobSF 的 iOS 动态分析限定于 Corellium VM【L2】），以及分发端审核要求压制此类样本进入商店。三条都是可观测性限制，与「iOS 上的滥用更少」无逻辑关系。全篇按此口径行文。

为了让缺口清单可复核，这里列出本篇实际执行过的四类核对动作，它们决定了一个否定结论属于哪种强度：

- **接口层逐页抓取**：DeviceCheck 框架页、`DCDevice` 及其成员、`generateToken`、位图读写页、`DCAppAttestService` 及 5 个成员、四篇指南（建立 App 完整性、服务端校验 App、证明对象校验指引、风险评估、接入准备）与 entitlement 资源页；每页取 JSON 渲染前文本，引句保留英文原文。
- **封闭集合穷举**：MDM `QueryResponses` 的 95 个键名、`SecurityInfo` 字段、`InstalledApplicationList` 项、OID 清单（按引入版本分组）、`DCError` 的 5 个 case、`validationCategory` 的 0–10 档、receipt 的 8 个字段号。这些是「不存在某字段」这一类结论的全部依据来源。
- **全文关键词检索**：`jailbreak`（DeviceCheck 文档、Device Management 文档、平台安全指南三处均零命中）、`fingerprint` 与 `tracking`（DeviceCheck 文档零命中、Guidelines 零命中）、`simulator`/`emulator`/`bot`/`automation`/`device farm`（DeviceCheck 文档零命中）、`seKey`（零命中，检索到的同名项均为第三方项目）、`deprecated`（DeviceCheck 全部页面零出现）。
- **论文取证**：对四篇核心论文取 PDF 转全文后逐句摘录关键句，保留作者的局限自陈段落；对三篇仅完成题录核对的研究（一篇 workshop 论文与两篇 2026 年测量论文）显式标注「全文未取得」。

## 1.6 端侧—平台侧—服务端—生态侧：四层结构的预先约定

本篇的视角定位是「设备真实性防线的四层结构与对手能力类别的对照评估」。四层在本篇有固定含义，后文所有结论都会归位到其中一层，避免「Apple 有没有防住」这类提问在层间漂移。

| 层 | 承担者 | 提供的东西 | 本篇给它的能力上限口径 |
| --- | --- | --- | --- |
| 端侧 | App 自身代码 | 本地特征检查、自证式完整性判断 | Apple 明文否定该层自证："a compromised app can falsify the results"【L1】 |
| 平台侧 | Apple 的机制与政策 | 三条服务端可验证通道（DeviceCheck 位、App Attest 证明与断言、Managed Device Attestation）＋签名与吊销规则 | 只提供信号与一个近似计数指标，不提供越狱/篡改判定【L1】 |
| 服务端 | 开发者与业务方 | challenge 生成与记忆、证明链校验、keyId/公钥/receipt 存储、阈值与处置 | Apple 把处置责任显式交给开发者："it's your responsibility to decide how to handle the request."【L1】 |
| 生态侧 | 分发通道与审核制度 | App Store 审核、TestFlight、Ad Hoc、ADEP in-house、OCSP 吊销 | Apple 自陈 "Though App Store security measures alone can never be perfect"【L1，平台安全指南 August 2026】 |

第四层与第三层的交界是本报告最容易被误写的地方：企业证书通道的吊销属于生态侧规则，其生效条件（首次启动回连 Apple OCSP 服务、响应在设备侧缓存 3–7 天、吊销连坐该证书签名的全部应用）是平台侧机制事实【L1，部署指南，发布日期 2026-09-17】，但「是否因此拒绝一台设备」是服务端与 MDM 管理员的判断。第 4 章的责任分配以此四层为骨架。

与四层结构对照的是**对手能力类别**。本篇只给类别、不给实现，且每个类别都限定其可表述的三件事（存在性／失效结论／证据强度）：

| 对手能力类别 | 本篇允许表述的内容 | 主要依据层级 |
| --- | --- | --- |
| 单机改造使用者（个人越狱、自用于定制设备） | 该形态存在且长期被社区文档记录；公开研究在其上做大样本分析时受越狱版本覆盖限制 | L5 社区文档（仅作历史注解）＋ L2 实验设置自述 |
| 应用内改造与功能增补（修改版 App 的流通） | 存在性由同行评审测量支撑，数量为该研究观测口径下的下界；对客户端校验的失效结论为条件式 | L2 USENIX Security 2026 |
| 检测规避能力（针对端侧越狱/篡改检测的压制） | 存在性由论文自陈；失效结论附作者所用能力条件；组件多为闭源且绑定特定越狱与 iOS 版本 | L2 EuroS&P 2019 / 2025 |
| 设备身份与配额的商品化 | UDID 注册配额、账号密钥池并行派发、「吊销保险」式预付码结构已被测量；单台绑定成本与终端加价倍率为观测口径 | L2 USENIX Security 2026 |
| 企业证书通道滥用与吊销对抗 | 状态判断（曾被广泛用于公开分发、现受严格管制）与两个已公开失效条件（回连被阻断、3–7 天缓存）；Apple 侧吊销是连坐式处置 | L1 一手规则 ＋ L2 论文记述 |
| 自动化与设备机群 | iOS 侧仅有「真机设备云作为测试产品可规模化租用」与「虚拟化平台存在且经司法确认」两类，均非滥用证据 | L3 厂商文档（能力存在性）＋ L4 司法与产业记录 |
| 未授权分发通道的整体谱系 | 按通道、机制类别与有效期分类的学术表可用（见 3.5）；社区形态术语仅作一次性历史注解 | L2 USENIX Security 2026 Table 2 ＋ L5 |

**本章口径边界**

- 本章的四条问题、三个术语边界与缺口清单是全篇的写作约束，后续章节不得在其中任一项上给出比本章更强的结论。
- 「系列空白」的判定依据是本地已有篇目的实际章节编号核对，属工程分工判断，不是文献综述结论。
- 缺口清单中所有条目在本篇一律以「未获得可核实条目」或「Apple 未公开」两种表述之一出现，不写「不存在」。
- Apple 关于系统被攻陷的原句只有两句，任何超出这两句的「Apple 承认/Apple 认为」均属越界。

---

# 方法与证据分级

## 2.1 本报告的六级体系与其构造方式

本篇的事实核查由两份独立底稿支撑：一份针对平台侧机制（Apple 文档通道），一份针对对抗面与检测生态（学术与公开报道通道）。两份底稿各自定义了来源等级，且**对「L2」的定义不同**，因此合并时必须先声明映射规则，否则全篇标注会自相矛盾。

平台侧底稿把 developer.apple.com 文档接口列为 L1、Apple 平台安全指南列为 L1，而把 App Store Review Guidelines 与 MDM Protocol Reference 列为「L2（一手但间接）」，第三方一律 L3 且不作主依据。对抗面底稿把 Apple 全部一手材料列为等级 1，把同行评审论文列为等级 2，厂商技术报告 3、媒体调查报道 4、二手转述 5、无法定级 6。冲突点在于：同一份 MDM Protocol Reference 在前者记 L2、按后者口径应记 L1。**本篇采用的合并体系**如下，遇到分歧时以体系内的层级细分优先、原简报标注并存于括号中：

| 等级 | 类别 | 可信度上限（本篇赋予的表述强度） |
| --- | --- | --- |
| L1 Apple 一手文档 | developer.apple.com 的 DeviceCheck / App Attest 文档、MDM Protocol Reference、Apple 部署指南、平台安全指南、Apple 白皮书 | 可作为「Apple 官方口径」直接引用；只能表述 Apple 说了什么，不能推定其未说的部分 |
| L1 Apple 协议与 API 文档（间接） | App Store Review Guidelines、开发者计划条款、账号侧密钥说明 | 条款条目号与原文可引，须注明「以抓取当日版本为准，条款可能修订」 |
| L2 同行评审论文 | EuroS&P 2019、EuroS&P 2025、USENIX Security '24/'26、ACM WiSec/IMC、ESORICS workshop 论文集 | 可作为「公开研究结论」引用，**必须带会议、年份、样本量与作者自陈局限** |
| L3 厂商安全报告 | MTD/RASP 厂商、移动风控厂商研究部门与产品文档 | 见 2.2 的上限条款 |
| L4 媒体调查报道 | 有具名记者与一手素材的严肃媒体、执法与监管公开材料 | 可作为「事件存在性」引用；规模数字须核对口径 |
| L5 社区文档与工程博客 | The Apple Wiki、维基百科、开发者问答、厂商博客的科普条目 | 只可用于提出待核实线索与一次性历史注解，不得作为事实来源 |
| 分析推断 | 由上述来源合出的推理 | 必须显式标注「分析推断」，不得混入事实句 |

三条本篇特有纪律在 2.2–2.4 逐条给出，它们不是通用科研规范，而是针对本篇材料构成（一手文档语义细碎、对抗面证据稀疏、厂商利益相关）设定的硬约束。

关于取证通道也需说明：Apple 的文档页需要 JS 渲染，直接抓取会失败，因此平台侧底稿的引文全部来自 `tutorials/data/documentation/**.json` 接口返回的 JSON（渲染前文本与代码 token 完整）；对抗面底稿的论文则采用「下载 PDF 后转全文文本再逐句取句」的方式，并保留了本地取证副本（HALY、Kellner、USENIX '26 灰产论文、USENIX '24 抢票论文、Apple 白皮书、DeviceCheck 与 App Attest 文档 JSON、ADEP 页与部署指南 HTML、平台安全指南全文）。这决定了本篇「已核实」的实际含义是**关键句可回到原文逐字复核**，而不是「检索结果支持该说法」。

各层级在本篇中的实际使用密度与允许承载的句子类型如下，它同时是本报告的「哪些话只能说到这个强度」对照表：

| 层级 | 在本篇中的主用途 | 能承载的最强句子 |
| --- | --- | --- |
| L1 Apple 一手文档 | 机制语义、字段清单、能力上限原句、服务端接口与规则 | 「Apple 文档规定/声明 X」「Apple 自我限定 X」 |
| L1 Apple 协议与 API 文档 | 条款条目号与原文、准入条件、标识符使用规则 | 「条款 5.1.2(iii) 禁止 X」 |
| L2 同行评审论文 | 检测有效性的测量、灰产结构与规模下界、分发型通道分类表 | 「某研究在某口径下测得 X，作者自陈局限 Y」 |
| L3 厂商安全报告 | 检测项存在性、产品能力宣称的定位 | 「该厂商声称在其自设测试口径下观察到 X」 |
| L4 媒体调查报道 | 事件存在性、司法与产业记录、企业证书吊销个案 | 「事件发生过，规模数字口径待核」 |
| L5 社区文档与工程博客 | 形态术语的一次性历史注解 | 「社区按 X 口径称呼该形态」 |
| 分析推断 | 跨层合出的判断（如运行期校验语义与重新签名之间的关系） | 显式标注「分析推断」，不得写成 Apple 承认 |

一处必须写明的层级失衡：本篇的正面机制结论几乎全部来自 L1，而对抗面的量化结论几乎全部来自 L2 的**三篇**论文（EuroS&P 2019、EuroS&P 2025、USENIX Security 2026），L3/L4 只用于事件与存在性。这意味着对抗面叙述的稳健性上限受样本来源数量制约——三篇论文的样本池、年份与判据互不相同，任何把它们的数字合并叙述的写法都会丢掉口径信息，因此本篇一律分列。

## 2.2 纪律一：厂商博客与营销报告的可信度上限

对抗面底稿在其来源等级一节写入了明文约束，本报告整体继承，并把它作为第 3、4 章引用厂商材料时的强制句式：所有 L3 来源对「检测/防护产品有效性」的自评结论，最高只能支撑**「该厂商声称在其自设测试口径下观察到 X」**，不得升格为「业界研究表明」或「实测证明」；凡出现市场规模、灰产规模、损失金额且来源为厂商或咨询机构营销白皮书的，一律标注**「不可用于事实陈述」**，仅作趋势参照；但 L3 来源对**对手能力存在性**的陈述（例如「存在针对某机制的绕过研究」）比其有效性量化更可信，允许单源使用，前提是必须标注口径。

这条纪律的实际效果体现在数字句的写法上。本篇允许出现的规模类数字只有两类，且都必须附「观测口径＋作者局限」：

| 数字 | 允许出现的写法 | 必须同时给出的局限声明 |
| --- | --- | --- |
| 3,359 个活跃签名站点、2,118 个唯一工具名、28 个上游证书站点、8,216 个非官方 IPA 条目、1,023 个存在非官方变体的应用【L2 USENIX Security 2026】 | 「该研究在其观测口径下识别出的**下界**」 | 论文自陈地理聚焦中国、仅覆盖 Ad Hoc 自助签名这一条通道、敏感清单不公开故第三方无法完整复核、发现已上报 Apple |
| 每周约 10 万个应用由 500 余名专家审核、约 100 万个问题应用被拒、47 万个开发者团队被驱逐、2.44 亿账号被停用、阻止逾 15 亿美元潜在欺诈交易【L1 Apple 白皮书】 | 「Apple **自述**的处置行为计数」 | 无方法学附录、无第三方复核、口径为处置行为而非滥用发生量、数据截至 2020 年，不能用于当前趋势推断 |

反过来，本篇明确**禁止**出现的数字有三类：任何 iOS 越狱检测的误报率（未取得同行评审测量）；任何厂商博客里的设备农场占比、广告欺诈损失额与「XX% 的移动欺诈来自模拟器」（互相转引、无一手方法学）；以及 Kellner 2019 转引的「Cydia 称超过 3,000 万设备使用其系统」（2018 口径的服务方自述，无独立测量，标注为不可用于事实陈述）。同类禁令也适用于把 Android 侧的检出率换算到 iOS——底稿记录的重打包检测体系化研究（NDSS 2011 至 CCS 2020 一线）绝大多数以 Android 为对象，其指标与 iOS 的「改造版应用」不可互相换算。

还有一项容易忽略的上限约束：L3 材料即使只用于「存在性」，也要区分它谈论的是**产品能力**还是**现象**。例如多家 MTD/RASP 厂商在产品文档中把 repackaging / re-signing detection 列为标准检测项，这一句可用，因为它支撑的是「该检测项在商业产品中普遍存在」；但其检出率与误报率**无公开可核实的第三方测量**，故任何量化都必须缺位。反之，「iOS 云手机可在云端运行原生 iOS 应用」这类表述在可检索范围内全部来自产品营销页与 SEO 内容，底稿判定为不可用于事实陈述，本篇因此完全不引用其能力主张，只在需要说明「市场上存在此类宣称但无可核证技术支撑」时提及宣称本身的存在。

## 2.3 纪律二：对手能力只记录存在性、失效结论与证据强度

第二条纪律限定「怎么写对手」。本篇涉及对抗能力的句子，一律只允许承载三类信息：

**其一，能力存在性。**即「某类能力是否被公开证实存在」。可用句式示例：「同行评审论文自陈存在针对越狱检测的现成函数 hook 路径，且相关规避组件多为闭源、绑定特定越狱与特定 iOS 版本」【L2 EuroS&P 2019】；「Apple 一手承认修改设备操作系统的攻击者可能绕过限制」【L1，`assessing-fraud-risk` 原句】。禁用句式：给出任何使能该能力的前提参数、工具选择、点位或流程。

**其二，失效结论。**即「在什么条件下某道防线不再成立」。这一类是本篇最有工程价值的部分，且底稿提供了**条件式**结论，可直接落地而不触及实现：「In-house distribution relies on enterprise certificates and has been widely misused for public app distribution … was once common but is now tightly regulated」【L2 USENIX Security 2026】；同一论文的条件句「This approach only bypasses client-side checks and works only in apps that lack server-side validation」——它把「客户端校验失效」与「服务端校验缺失」绑成充要条件，是全篇责任分配章节的核心依据【L2】。

**其三，证据强度。**每条能力句都要注明它建立在什么之上：同行评审主会（高）、workshop 后论文集（中，须降格使用）、厂商自评（低，受 2.2 上限）、社区文档（仅历史注解）。举例来说，「rootless 越狱使传统基于特征文件的检测失效」这一命题已进入同行评审议程，因为 ESORICS 2025 International Workshops（Springer LNCS，pp. 558–577）有一篇以 rootless 越狱为条件重新评估 iOS 银行应用越狱检测有效性的论文【L2-workshop，部分核实】；但其样本量与绕过成功率数字本次未取得全文，因此本篇只可写「有此类研究存在」，**不得引用其任何百分比**。

配套三条禁令在本篇全程生效：不写检测点位清单与绕过手法的机制描述（该项已由《iOS系统防御与机制绕过》第 11 章占位，本篇只做效果评估）；不写设备机群或签名基础设施的搭建方式（灰产结构只以「谁支撑谁、集中到什么程度、身份配额如何被计价」的关系描述出现）；不写「绕过成本」结论——Apple 未公布该口径，学术侧亦无测量，任何成本估算在本篇都属未公开。

最后一层约束是**条件完整性**：凡「检测可被绕过」的句子必须同时给出被绕过的条件，即是否需要一台已越狱的设备、是否需要运行期注入、是否因为缺少服务端校验。条件齐备的结论可以写（上表三条均满足），条件不齐备的结论即使来源存在也只能降级为「作者在特定实验设置下观察到」。这条纪律的意义在于防止读者把「在作者所用 hook 能力与检测实现条件下，34 个银行应用中除 1 个外全部可被完全攻破」【L2 EuroS&P 2019】误读为「越狱检测在任何条件下都无意义」——那是本篇不会做出的推论。

## 2.4 纪律三：Apple 平台安全指南不能充当 DeviceCheck / App Attest 的出处

第三条纪律针对一个具体的出处错误。本地留存有一份 Apple Platform Security Guide 全文文本副本（版权页标注 **August 2026**），此前系列篇目大量以它作为「Apple 一手」依据，读者与下游写手很自然会假设它也论述了设备真实性机制。**实际核对结果是否定的**：对该副本全文检索 `DeviceCheck`、`Device Check`、`App Attest`、`DCAppAttest`，命中次数为 **0**【负面结果：本地全文逐词检索】。也就是说，本版 Apple 平台安全指南**不设 DeviceCheck / App Attest 章节**。

由此产生的硬规则有两条：

1. **凡引用 DeviceCheck 与 App Attest 的机制语义、字段、接口、原文句子，出处只能是 developer.apple.com 的文档页面**（框架页、`DCDevice`、`DCAppAttestService` 及其成员页、`establishing-your-app-s-integrity`、`validating-apps-that-connect-to-your-server`、`assessing-fraud-risk`、`preparing-to-use-the-app-attest-service`、entitlement 资源页）。把「Apple 平台安全指南论述了 App Attest」写进任何句子都是错误出处，属底稿 F 节明确记录的常见引用错误。
2. **平台安全指南仍可用于它确实论述过的相邻主题**，且这些主题在本篇有实际作用：Secure Enclave 与主处理器隔离、宿主内核被攻破时 SE 内数据仍受保护的设计意图（"designed to keep sensitive user data secure even when the Application Processor kernel becomes compromised"）；运行期代码签名校验的对象与范围（"At runtime, code signature checks of all executable memory pages are checked as they're loaded to help ensure that an app hasn't been modified since it was installed or last updated."）；企业 in-house 应用首次启动需获得 Apple 的正面确认；App Store 防护措施的自我定位（"Though App Store security measures alone can never be perfect"）。这些句子支撑的是 3.5 的 App 完整性与 4.3 的生态侧责任，不支撑任何 App Attest 语义。

这条纪律的直接后果是：本篇在写「SE 提供密钥保护」时用的是平台安全指南的通用论述，而在写「App Attest 私钥存放在 SE」时用的是 `generateKey` 页原文 "The framework securely stores the key in the Secure Enclave."【L1 Apple 文档】。两份材料各说各的层次，不能合并成一句「Apple 平台安全指南保证 App Attest 密钥不可导出」——那句在本篇不成立，也不该成立。

同类需警惕的还有 MDM 侧：`DevicePropertiesAttestation`（Managed Device Attestation）的出处是 MDM Protocol Reference 与 `validating-a-managed-device-attestation-attestation` 页，与 App Attest 是**两套不同机制**，信任根也不同（前者为 Apple Enterprise Attestation Root CA，后者为 App Attest root，经 Apple Private PKI Repository 下载）。本篇全程分立叙述，不互相引证据。

## 2.5 为什么负面结果在本篇尤其重要

多数报告章节的推动力是「查到了什么」，本篇有相当一部分推动力来自「查不到什么」。原因很直接：设备真实性防线的核心争议恰恰是**判定权归属**——如果 Apple 提供了某个判定接口，责任就在平台；如果没有，责任就在开发者。这类「有没有」的问题，负面结果本身就是答案。但负面结果分两种强度，必须分开写。

**第一种是穷举型负面结果**：核对对象是可穷举的封闭集合，因此否定结论具有硬证据地位。本篇有三例。

| 核对对象 | 方法 | 可写的结论 | 不可反向写成的结论 |
| --- | --- | --- | --- |
| MDM `DeviceInformation` 响应的 `QueryResponses` 键名（本次共 95 项） | 逐字段列出并检索 jailbreak / root / Cydia / integrity / trust-level / risk | 该命令的响应键名中不存在任何越狱、完整性或风险判定字段；整个 Device Management 文档在本次抓取范围内无 `jailbreak` 字样 | 「Apple 不提供任何设备完整性信号」——不成立，`DevicePropertiesAttestation` 确实存在，只是它证明的属性集有限 |
| App Store Review Guidelines 全文（本次抓取页面 240,818 字节） | 全文检索 `fingerprint`、`IDFA`、`IDFV`、`vendor`、`serial number` | `fingerprint` 一词出现次数为 **0**；可用条款只有 5.1.2(iii)「不得秘密构建用户画像 / 识别匿名用户 / 重建用户画像」与 5.1.2(i) ATT 授权口径 | 「Apple 政策禁止设备指纹」——该句在本篇不成立，条目号与措辞都不支持这么写 |
| DeviceCheck 全部已取页面（框架页、`DCDevice`、`generateToken`、位图读写页） | 全文检索 `fingerprint` / `tracking` / 「不可重置」 | 这些页面**没有**「不得用于设备指纹或追踪」的限制句，最接近的只有 "while maintaining user privacy" 与 token 的 ephemeral/单用属性；也没有「设备端无法清除 bit」这句 | 「DeviceCheck 可自由用于指纹」——同样不成立，限制只是不在该文档名下 |

**第二种是检索型负面结果**：核对对象是开放文献空间，查不到只说明本次未取得依据，不说明对象不存在。本篇有两例，且都影响结论强度。其一是 **App Attest / DeviceCheck 的独立弱点或绕过研究：本次未检索到任何同行评审论文，零篇**【负面结果：多轮英文检索 + HALY 相关工作一节核对】。可写的口径是「公开研究对这套服务端防线的弱点尚无同行评审证据」，并且必须补一句：因此本报告**不假装知道 App Attest 的实际绕过成本**。HALY 自身也承认这块证据不足——作者写道 device binding 的实现高度依赖具体应用、使自动检测困难，这些技术的流行度需要更多研究【L2 EuroS&P 2025】。其二是 **iOS 侧模拟器滥用测量的缺失**：本次未发现任何以 iOS 模拟器或 iOS 云手机为对象给出滥用规模测量的同行评审论文或执法材料【负面结果】。可写的只有「iOS 虚拟化技术栈存在且有司法与产业记录」（Apple 2019 年起诉 Corellium、2021 年撤诉和解、2023 年第十一巡回法院拒绝恢复版权主张、2023 年 12 月余部和解【L4 多家严肃媒体，口径相互一致】）与「iOS 侧自动化分析依赖闭源付费虚拟化平台」【L2】。据此**不能**推定「黑产可自由租用 iOS 虚拟化实例」，也不能推定「iOS 无模拟器滥用」。

两种负面结果的差别决定了限定语的选择：穷举型可以写成「不存在该字段」（并附核对范围），检索型只能写成「本次核查未获得可核实条目」。混用会把一份审计报告变成断言集。

作为第三类负面结果，「Apple 未公开」清单同样具有可写价值，因为它界定了工程决策中**必须自建**的部分。本篇核到未公开的项目如下：

| 未公开项 | 影响 |
| --- | --- |
| DeviceCheck query/update 与 App Attest 指标接口的 QPS/日配额、服务可用性 SLA、延迟与故障公告 | 容量规划只能按 Apple 的 rule of thumb 自建退避，无法引用承诺值 |
| risk metric 的计算定义、精度、是否可被 Apple 事后修正 | 阈值只能按自身观测分布调，无法解释异常值的成因 |
| 越狱/篡改判定的任何官方阈值 | 判定与处置完全归业务侧（见第 4 章） |
| App Attest 支持的机型枚举、`isSupported` 的机型白名单、模拟器与虚拟化环境下的行为 | 兼容性矩阵需自建；「模拟器上必为 false」一类说法属推断或第三方 |
| 私钥曲线与算法（文档只给 77 字节 COSE 公钥与 X9.62 未压缩点格式）、SE 内 key 数量上限 | 不得写成「官方规定 P-256」 |
| 允许的时间偏移量、`403 Forbidden` 的触发条件、`last_update_time` 更新粒度、bit 值历史可查性 | 时钟与错误处理逻辑需自行兜底 |
| attestation receipt 在 Apple 侧的存储与保留期 | 无法据 Apple 侧数据做长周期回溯 |
| Apple 服务端对同一 key 重复 attestation 的次数限制 | 重试策略只能按「同 key 同 clientDataHash 重试以保住指标」这句原文执行 |
| 对具体绕过手段（越狱、重签名工具、企业证书分发）的公开结论与胜率数据 | 本篇不得给出任何「防住概率」 |

## 2.6 跨研究比较的口径三件套

把两份底稿合并后出现的第二类风险是「数字看起来矛盾」。对策是固定的三件套：**任何跨研究比较必须逐条标注样本池、判据、年份**，缺一项即视为口径错误。

以越狱检测流行度为例，两个都合法但表面冲突的数字：

| 数字 | 样本池 | 判据 | 年份 |
| --- | --- | --- | --- |
| 59% 的应用含某种形式越狱检测（银行类 53%） | 跨全部类别的 3,482 个最流行应用；银行类另取 34 个 | 二进制/资源中存在检测形式 | 论文时点 2018-11，发表 EuroS&P 2019【L2】 |
| 静态 96.7% / 动态 24.9% | 每 OS 各 2,646 个应用（自 3,322 池筛出） | 静态＝字符串/符号存在；动态＝运行时真的检查 root 相关文件与目录权限 | EuroS&P 2025【L2】 |

两者不矛盾，但必须写清「**静态字符串存在 ≠ 运行时真的执行检测**」，否则读者会认为其中一篇算错了。HALY 作者自己就把 25% 这一动态口径与 Kellner 的 59% 并列对比，并据此给出「检测信号滞后于攻击面变化」的结论：应用按越狱名称做特征检测时主要检查 blackra1n（5.5%）与 unc0ver（1.4%），**未发现任何应用带有针对 iOS 15（2021 年 9 月发布）及更新版本越狱的专用检测**——该句限定为「针对 iOS 15 及之后发布的越狱未发现专用特征检测」，不得写成「所有检测都不更新」。同类陷阱还有 Android root 检测流行度的四个数字（Evans 2015 的样本口径、Kellner 2019、Ruggia 2024 的 61%、HALY 2025 的 92.3%）：四个数字来自四个样本池与四种判据，**不可排成趋势线**，本篇也不排。

同一来源内部的计数不一致也要保留而非择一。Kellner 2019 导言称「15 out of 34 银行应用完全不使用该措施」，结论称「only 18 out of 34 使用越狱检测」，两者相加为 33 而非 34；本篇引用时给出「44% 的银行应用未尝试检测」这一摘要口径，并同时暴露两处原文数字【L2】。Apple 文档内部也有同类情形：`establishing-your-app-s-integrity` 的提示框称 "Action, extensible SSO, and watchOS extensions are supported"，而 `DCAppAttestService.isSupported` 页称 "The `generateKey` method fails when you call it from an app extension … The only app extensions that support App Attest are watchOS extensions in watchOS 9 or later."【L1】两处不一致，本篇**并列原文并标注「Apple 文档内部口径不一致」**，不替 Apple 选一个答案。

术语层面还需固定六项译名与口径，避免下游写手出错：attestation＝证明、assertion＝断言（一次使用证明），首次出现附英文；`isSupported` 一律写作**只读属性**（ObjC 侧键名是 `supported`，getter 才是 `isSupported`），且两个框架语义不同（DeviceCheck 是「该设备支持 DeviceCheck API」，App Attest 是「该特定设备提供 App Attest 服务」）；bit 的官方字段名是 `bit0`/`bit1`，第三方写作 `bit1/bit2` 与官方不符；`RP ID`（Apple 写法，含空格）与 `rpIdHash`（WebAuthn 与多数校验库写法）指同一字段，须并列；「环境/新鲜度」在三个机制里分别是 `aaguid`（`appattestdevelop` / `appattest`+7×`0x00`）、entitlement 取值（`development`/`production`）与 MDM 的 `DeviceAttestationNonce` freshness code，三者不可互换；风险指标在文档里有三个名字（`risk metric`、`fraud metric`、`fraud assessment metric`），首次出现统一为「同设备唯一 attest 的近似计数（risk metric）」。最后，Apple 原文从未使用 "device authenticity" 这一固定术语，它分别用 authenticity、validity、legitimacy、integrity 四个词——本报告中文标题里的「设备真实性」是编辑性汇总词，不是 Apple 术语。

**本章口径边界**

- 本章的分级体系、三条纪律与三件套是全篇唯一合法的引用尺度；后文任何句子若不符合此处口径，即为写作失误而非可接受的简化。
- 两份底稿对「L2」的定义不同（一手间接文档 vs 同行评审），本章采用合并体系并保留原标注；引用时层级以合并体系为准。
- 穷举型负面结果与检索型负面结果的表述必须分开，前者可写「不存在该字段（附核对范围）」，后者只能写「本次核查未获得可核实条目」。
- 厂商材料的自评有效性一律降格为「该厂商声称在其自设测试口径下观察到」；营销白皮书的规模数字一律标注「不可用于事实陈述」。
- DeviceCheck / App Attest 的出处不得为 Apple 平台安全指南；该指南仅用于 SE 通用论述、运行期签名校验、企业应用首启动确认与 App Store 措施自我定位。

---

# 概念切分：真实性、完整性、身份与 App 完整性

## 3.1 为什么必须先把四个概念拆开

「设备是不是真的」这一句话里其实压着四个不同问题，它们在 Apple 文本中由四个不同的词指称，且各自的验证者、强度与不可验证项完全不同。混用的代价很实际：把「设备完整性」当成「设备真实性」，会以为存在一个可以调用一次就得到「未越狱＝放行」的官方接口；把「设备身份」当成「设备真实性」，会以为拿到 IDFV 就拿到了可信设备锚点；把「App 完整性」当成前两者，会以为跑的是官方包就等于设备没被动过。

Apple 原文里并不存在 "device authenticity" 这一固定术语，它分别用四个词落在四个位置："asserting app integrity"（框架页 abstract，指 App 完整性）、"the validity of a generated cryptographic key"（`attestKey`，指密钥真实性）、"the legitimacy of an instance of your app running on a device"（`DCAppAttestService` 与 `generateAssertion`，指某一次请求合法）、"a device with a compromised operating system"（框架页，指系统被改动）。本报告的中文概念切分与这四个词的映射如下：

| 本篇概念 | 对应 Apple 语义载体 | 一句话定义 | 该概念的失效面 |
| --- | --- | --- | --- |
| 设备真实性 | App Attest 证明与断言、Managed Device Attestation | 服务端可密码学验证地得知：某个 key 由真实 Apple 设备上的指定 App 实例生成／某台受管设备的若干属性由 Apple 证明服务器签发 | 系统被改动者可能绕过（Apple 原文）；不提供「未越狱」判定 |
| 设备完整性 | 无官方判定服务（负面结果）；MDM 侧仅有限可证明属性 | 设备操作系统是否被越狱或篡改 | Apple 不提供布尔判定；MDM 无相关字段；iOS 侧可证明属性不含 SIP/Secure Boot/kext |
| 设备身份 | IDFV / IDFA / 序列号 / UDID / DeviceCheck token | 把一次请求归到某台设备或某个 vendor 的能力 | 全部标识符都有官方失效条件；App Attest key 有意不跨重装存活 |
| App 完整性 | `apple_validation_category_01`、`apple_bundle_version_01`、运行期签名校验 | 跑的是不是未被改动、以何种签名身份分发的包 | 类别标签不等于「被改动」判定；端侧无法查原始签名者 |

四个概念各自要回答三件事：**谁能验证、验证到什么强度、谁不能验证**。这是本章每一节的小结骨架，也是第 4 章责任分配的前置——只有先确定某件事「谁能验证」，才谈得上「责任落在谁身上」。判定强度在本篇按四档描述：**密码学可验证**（有 Apple 签发的证明对象或证书链，服务端可离线验链）、**规则可验证**（Apple 提供分发与吊销规则，符合性可由设备回连确认）、**启发式**（端侧特征检查，无第三方能复核其结论）、**不可验证**（本次核查未发现任何官方途径）。这四档不是量化排序表，而是来源层级的分类标签：前两档的支撑材料是 L1 Apple 文档，第三档的支撑材料是 L2 论文对其有效性的**测量**（而非对其机制的复述），第四档是负面结果。

四个概念之间还有三条依赖关系，它们决定了防线的短板位置，也决定了后文的责任归属：

- **App 完整性依赖设备真实性才能成立。**端侧的完整性判断（是否存在随包描述文件、是否加载了意外动态库）预设了「运行我的那段代码的系统没被改过」；EuroS&P 2025 把这一依赖写进了机制描述——iOS 设备只允许执行 Apple 签名的二进制，「除非该检查被越狱禁用」【L2】。前提失效时，端侧结论不可信，这正是 Apple 要求把结果送到服务端的原因。
- **设备真实性不依赖设备完整性，但被它限制。**App Attest 的验证逻辑不询问「系统是否被改动」，它验证的是密钥与 App 实例；然而当系统被改动时，其结论的解释力下降——这一点由 Apple 原文自己承认（"an attacker that modifies the device's operating system might bypass restrictions"），而不是由本报告推断。
- **设备身份与前三者正交。**身份回答「是不是同一个」，不回答「是不是可信」。本篇要求任何风控策略在写下标识符时同时写下它的官方失效条件（见 3.4 的表），否则会把一个会变动的值当成稳定锚点。

这三条关系可以直接转写成风控语言：**可验证的最强事实（密钥与 App 实例来源）不覆盖设备状态问题；覆盖设备状态问题的（越狱检测）强度只有启发式；而标识符既不稳定也无判定力。**任何声称同时满足三者的方案，都需要在本篇口径之外提出额外证据。

## 3.2 设备真实性：被证明的是密钥与 App 实例，不是「未越狱」

这一节回答「Apple 到底能让服务端知道什么」。答案分三条互不替代的通道，它们的信任根、可见字段与适用前提都不同，混用证据是最常见的错误。

| 维度 | DeviceCheck 两位状态位 | App Attest 证明与断言 | Managed Device Attestation |
| --- | --- | --- | --- |
| 面向 | 开发者服务端 ↔ Apple 服务端 | App ↔ 开发者服务端 ↔ Apple 指标服务 | MDM 服务器 ↔ 受管设备 |
| 提供什么 | 每设备两个二进制位（`bit0`/`bit1`），语义由开发者自定义，Apple 只负责存储与回读 | 密钥证明对象（WebAuthn 结构的 authenticator data + attestation statement）与后续断言 | 一串 DER 证书，叶证书内含描述设备的自定义 OID |
| 信任根 | 无证书链，靠 JWT 鉴权的 S2S 接口 | App Attest root（Apple Private PKI Repository） | Apple Enterprise Attestation Root CA |
| 适用前提 | `DCDevice.isSupported`；App 须注册在开发者账号下 | entitlement `com.apple.developer.devicecheck.appattest-environment`；`isSupported` | MDM 注册 ＋ 相应 access right ＋ A11 Bionic 或 M 系列及以上 |
| 判定性质 | 开发者自维护的状态记录，不自带语义 | 密码学可验证的密钥与 App 实例来源 | 密码学可验证的设备属性声明 |

**App Attest 证明里确切含有什么。**authenticator data 的官方字段清单为：`RP ID`（32 字节，App ID 的 SHA256；App ID ＝ 10 位 Team ID ＋ 句点 ＋ `CFBundleIdentifier`，macOS 上以 TN3127 的 code signing requirement 取代 bundle ID）、`counter`（4 字节，报告该 attested key 被用于签发断言的次数，证明阶段必须为 0）、`aaguid`（16 字节，区分 development / production 环境的常量）、`credentialId`（32 字节，公钥部分的哈希）、`Encoded key`（77 字节 COSE 格式公钥）、`extensions`（变长）【L1 Apple 文档 `validating-apps-that-connect-to-your-server`】。证书链按 `x5c` 数组自叶证书起追溯到 Apple 根；nonce 绑定在叶证书的扩展 OID `1.2.840.113635.100.8.2`。由此可得出两条常被写错的口径：**证明中含的是被哈希的 App ID（Team ID 只作为其前缀间接出现），没有独立的 Team ID 字段**；曲线名与密钥长度 Apple 未公开，不得写成「官方规定 P-256」。

**风险指标的确切形态。**Apple 提供的唯一量化信号是 receipt 字段 17，官方定义为「该设备上与本 App 相关的 attested key 在近 30 天的数量」的**近似值**："The receipt represents the metric as a string that indicates the number of attested keys associated with a given device over the past 30 days. Look for this value to be a low number. Note that the metric can grow if a user reinstalls your app, restores from a backup, or transfers a device to another user."【L1】Receipt 由 PKCS #7 容器承载 ASN.1 编码载荷，字段 2 App ID、3 attested 公钥、4 client hash、5 token、6 类型（`ATTEST`/`RECEIPT`）、12 创建时间、17 风险指标、19 生效时间、21 到期时间；本地校验要求验签、链到 App Attest 根、字段 2 必须是本 App、字段 12 不早于五分钟（原文以 "This helps to thwart replay attacks." 说明目的）、字段 3 与首次证明存储的公钥一致。只有服务端申请的 `RECEIPT` 才带字段 17。指标可通过重装、备份恢复、设备转让正当推高，Apple 明确要求开发者自行调阈值（"you'll have to tune your risk assessment logic based on the typical numbers that you see over time"）。

**Managed Device Attestation 能证明什么、不能证明什么。**iOS 16/iPadOS 16/tvOS 16/watchOS 10/visionOS 1/macOS 14 引入的 OID 覆盖序列号（`.9.1`）、UDID（`.9.2`，Mac 上等于 `ProvisioningUDID` 且与 MDM 协议其他处使用的 UDID 不一致）、sepOS 版本（`.9.4`）、freshness code（`.10.2`，对应 `DeviceAttestationNonce`）；iOS 17.2 / macOS 14.2 起增加 SoftwareUpdateDeviceID、OS 版本与 LLB（Low Level Bootloader）固件版本。**SIP 状态（`.13.1`）、Secure Boot 模式（`.13.2`，取值 Full/Reduced/Permissive Security）、第三方 kext 是否许可（`.13.3`）三项由 Apple 明确标注为「introduced in macOS 14.2」，iOS 侧无对应项**【L1；逐 OID 核对】。校验语义同样一手："If the leaf certificate doesn't chain to this CA, the attestation isn't trustworthy. There's no scenario during normal operations in which this validation step fails. You can choose to ignore the attestation entirely, or flag it as an incident that needs investigation."，以及「缺失 OID 或空白值表示 Apple 证明服务器无法可靠确定该属性」。

**这一层的四条上限**，前三条为 Apple 原文、第四条为负面结果：其一，"No single policy can eliminate all fraud. For example, App Attest can't definitively pinpoint a device with a compromised operating system."【L1 框架页】；其二，"A compromised version of your app running on a genuine, unmodified Apple device can't create valid assertions. However, an attacker that modifies the device's operating system might bypass restrictions."【L1 `assessing-fraud-risk`】；其三，并非所有设备可用——"Not all devices can use the App Attest service"，且不支持时 Apple 要求服务端**不得再把断言作为必要条件**（"your server — which can no longer require assertions"），即官方给出的处置是降级放行而不给替代判定；Mac 上读取 `isSupported` 结果为 `false`（含 Mac Catalyst 与在 Apple silicon 上运行的 iOS/iPadOS App）。其四，模拟器与虚拟化环境下的行为 Apple 未公开：DeviceCheck 与 App Attest 全部已取页面均无 `simulator`/`emulator` 字样【负面结果：逐页检索】。

**谁能验证／验证到什么强度／谁不能验证**：服务端可通过验链与字段比对得到密码学强度的结论；Apple 是签发方；**没有任何一方能据此验证「该设备未越狱」**——该判定不存在于任何一条通道中。设备自身也不是验证者，这正是 Apple 否定应用自证路线的原因。

第一条通道（两个 bit）的服务端形态也需要写清，因为它是「Apple 能让服务端知道什么」中唯一由开发者自维护的那一项。接口为三类 POST：query、update 与 device-validation，基址分别为生产 `https://api.devicecheck.apple.com` 与开发 `https://api.development.devicecheck.apple.com`；鉴权头是 JWT，"The authentication key must use the ES256 algorithm and be in the Base 64 URL–encoded JSON web token format."，密钥创建流程复用 APNs provider token，请求体强制 `timestamp`（UTC 毫秒）。响应码集合本身透露了这套接口的语义边界：

| 状态码 | Apple 给的说明 | 可用于什么判断 |
| --- | --- | --- |
| 400 | Bad Device Token / Bad Bits / Bad Timestamp | 输入合法性，不含风险信息 |
| 401 | Invalid Authorization Token / Authorization Token Expired | 自身凭据与时钟问题 |
| 403 | Forbidden（"The specified action isn't allowed"；具体触发条件未公开） | 权限配置问题 |
| 404 | The bit state wasn't found | 「此前未写过 bit」这一状态，可作首次交互标记 |
| 405 / 429 / 500 / 503 | Method Not Allowed / Too Many Requests / Server Error / Service Unavailable | 工程退避，不得解读为设备风险信号 |

查询响应还包含 `last_update_time`（格式 `YYYY-MM`）。也就是说，这套接口能告诉服务端的只有「这台设备上有你写入的两个位，最后更新时间在某月」，**不提供位的语义，也不提供任何风险判断**。可用性判定则靠 `DCDevice.isSupported` 这一**只读属性**；按文档 platform 元数据，`DCDevice` 的门槛是 iOS 11.0 / iPadOS 11.0 / Mac Catalyst 13.1 / macOS 10.15 / tvOS 11.0 / visionOS 1.0 / watchOS 9.0，而 `DCAppAttestService` 的门槛是 iOS 14.0（watchOS 9.0 起才有扩展支持）——两者相差三个大版本，这解释了「老设备只有 bit 可用」的现实差异，但 Apple 文档没有把它写成演进或替代叙述，两个类也都**未被标记弃用**【L1；`deprecated` 字段零出现】。

## 3.3 设备完整性：一个没有官方判定服务的概念

「设备完整性」在 iOS 平台侧是一个**有名字但没有接口的概念**。核查过程中得到的否定结论比正面结论多，这本身就是本节要报告的内容。

四条穷举型负面结果：DeviceCheck 与 App Attest 的全部文档页检索 `jailbreak` 零命中，公开成员清单只有 `current`/`shared`、`isSupported`、`generateToken`、`generateKey`、`attestKey`、`generateAssertion` 与 `DCError`【L1；逐页核对】；`DeviceInformation` 命令响应的 95 个键名中不存在 jailbreak、root、Cydia、integrity、trust-level、risk 任何一类字段【L1；逐字段核对】；整个 Device Management 文档在本次抓取范围内无 `jailbreak` 字样；Apple 客户端错误码只有五个粗粒度 case（`featureUnsupported`、`invalidInput`、`invalidKey`、`serverUnavailable`、`unknownSystemFailure`），**不存在「检测到篡改/越狱」类错误码**，因此客户端错误只能作重试依据、不能作风险判定输入【L1】。此外，Apple 平台安全指南全文出现 `jailbreak` 0 次，其口径是 untrusted code、unsigned apps、compromised。

正面侧能拿到的最接近「完整性信号」的东西有三件，且每一件都要限定：

| 信号 | 所在位置 | 它实际告诉你什么 | 它不告诉你什么 |
| --- | --- | --- | --- |
| `DevicePropertiesAttestation` | MDM，受管设备 | Apple 证明服务器签发了这台设备的序列号/UDID/sepOS/OS 版本/LLB 版本/freshness | 不告诉你是否被越狱；iOS 上不含 SIP、Secure Boot、第三方 kext（三项仅 macOS 14.2+） |
| `SecurityInfo` 命令字段 | MDM | `SystemIntegrityProtectionEnabled`、`AuthenticatedRootVolumeEnabled`、`SecureBoot`、`FirmwarePasswordStatus`、FileVault 相关字段 | 这些**几乎全为 macOS 专属**；iOS 侧只有 `PasscodePresent`/`PasscodeCompliant` 类通行码合规位与 `HardwareEncryptionCaps` |
| `InstalledApplicationList` 项 | MDM | `AdHocCodeSigned`、`AppStoreVendable`、`IsValidated`、`BetaApp`、`Source`、`DistributorIdentifier` 等**来源与签名类别标签** | 不是「该 App 被改动」的判定；且仅对受管设备可见，个人与 BYOD 设备无此可见性 |

适用面必须写成一句限定：以上所有 MDM 能力都以「设备完成 MDM 注册且下发对应 access right」为前提，**没有任何 MDM 字段可用于未受管的个人设备**【负面结果：本次抓取范围内不存在】。这意味着企业场景里的完整性可见性是纳管换来的，而不是操作系统免费提供的；未纳管设备的完整性判定只能落回端侧启发式（其可靠性见 3.6）或业务侧行为模型。

**谁能验证／验证到什么强度／谁不能验证**：Apple 的证明服务器能对「设备属性声明」的签发负责（密码学强度），但它**拒绝**对「是否被篡改」发表判定；MDM 管理员能验证受管设备的属性证明链与来源标签（规则＋密码学混合强度）；普通 App 的服务端不能在任何层面验证设备完整性——它能拿到的只有 risk metric 这个间接计数，以及自家端侧代码上报的、Apple 已明文警告可被伪造的自检结果。

硬件门槛与「iOS 与 macOS 可证明属性集的不对称」是这一节最需要落到清单上的两条事实。门槛按 Apple 的字段表逐档核实：支持 Managed Device Attestation 的是 A11 Bionic 及以后的 iPhone/iPad、全部 M 系列 Mac、Apple TV A12 及以后、Apple Watch S4 及以后；不支持的是 A10x Fusion 及更早的 iPhone/iPad 与 Intel Mac、S3 及更早的 Watch，Vision Pro 一栏标注为 "none"；在不支持的设备上，`DevicePropertiesAttestation` 与 `DeviceAttestationNonce` 两个键会被**直接忽略**【L1 MDM Protocol Reference】。

| 可被 Apple 证明服务器签发的属性 | iOS / iPadOS | macOS |
| --- | --- | --- |
| 序列号（OID `.8.9.1`） | 是 | 是 |
| UDID（`.8.9.2`） | 是 | 是（等于 `ProvisioningUDID`，与 MDM 协议其他处使用的 UDID 不一致） |
| Secure Enclave 操作系统版本（`.8.9.4`） | 是 | 是 |
| freshness code（`.8.10.2`，对应 `DeviceAttestationNonce`，缓存可能导致不匹配） | 是 | 是 |
| SoftwareUpdateDeviceID、OS 版本、LLB 固件版本（`.8.10.1`、`.8.10.3` 等） | iOS 17.2+ 起 | macOS 14.2+ 起 |
| SIP 状态（`.8.13.1`，取值 0/1） | **无对应项** | macOS 14.2 引入 |
| Secure Boot 状态（`.8.13.2`，Full / Reduced / Permissive Security） | **无对应项** | macOS 14.2 引入 |
| 第三方内核扩展是否许可（`.8.13.3`，0 ＝ 不允许） | **无对应项** | macOS 14.2 引入 |

这张表是「设备完整性」概念在本篇的核心证据：Apple 在 iOS 上提供的是**设备属性**的证明，不是**系统状态**的证明；把 macOS 侧才有的三项完整性属性写成 iPhone 也能证明，属对 Apple 文档的误读，而这类误读在企业采购文档中相当常见。与之相配的另一句原文也值得引在这里，因为它说明了为什么「证明服务器拒绝签发」本身是一种信号："If Apple's attestation servers are unable to verify a device property they generate a blank value, omit the OID entirely, or refuse to issue an attestation certificate."，以及「缺失 OID 或空白值表示 Apple 无法可靠确定该属性」【L1】。也就是说，属性缺失有三种成因且 Apple 不区分它们——既可能是设备状态异常，也可能只是系统版本或注册类型不支持，这限制了「把空白值当判定」这一做法。

同类限制也适用于 `InstalledApplicationList`：`AdHocCodeSigned`、`AppStoreVendable`、`IsValidated` 三个字段合起来能勾勒「这个 App 是从哪条通道、以哪类签名身份装上的」，但它们是**来源与签名类别标签**，不是「该 App 内容被改动」的判定，且只对受管设备可见。把前者写成后者，是完整性叙述里第二常见的越界。

## 3.4 设备身份：标识符、序列号与政策条款的实际边界

设备身份是四个概念里最容易被当作真实性代理的一项，而它恰恰是官方**主动削弱**稳定性的一类能力。逐个看失效条件：

| 标识符 | 取得者 | Apple 明文写的失效条件 | 能否作为可信设备锚点 |
| --- | --- | --- | --- |
| IDFA | App（需 ATT 授权） | iOS/iPadOS 14.5 起必须先取得跟踪授权；返回全零的情形包括「在 Simulator 中无论任何设置」、macOS、在 visionOS 内运行的兼容 iPad/iPhone App、未请求授权、用户拒绝、"When a profile or configuration restricts access to the advertising identifier."；官方最佳实践直接写"don't store the advertising identifier value"，且用户可在 设置 > 隐私 > 跟踪 随时变更授权 | 否（规则可验证但随时失效） |
| IDFV（`identifierForVendor`） | App | 同一 vendor 同设备同值；用户删光该 vendor 全部 App 后重装会变；用 Xcode 安装测试构建或以 ad-hoc 分发安装也会变；重启后、用户解锁前为 `nil`；非商店安装（企业 App 与开发中 App）按 bundle ID 推导 vendor | 否（可作同 vendor 会话锚点，不可作跨时间设备锚点） |
| DeviceCheck token | App 生成、服务端使用 | 官方描述为 ephemeral 且应视为单次使用（"You should treat the token you receive in the completion block as single-use."）；生成它的 App 必须关联开发者账号 | 否（一次性凭据，不返回设备标识） |
| App Attest keyId | App 与服务端 | "For privacy reasons, App Attest keys stored on device don't survive these events"——重装、迁移、从备份恢复后必须重新生成 | 否（Apple 有意使其不跨事件存活） |
| 序列号 / UDID / IMEI / MEID / ICCID / MAC 类字段 | **仅 MDM，且需 Device Information 或 Network Information access right** | 未受管设备完全不可见；Mac 上 UDID 型 OID 等于 `ProvisioningUDID`，与 MDM 协议其他处使用的 UDID 不一致 | 规则可验证，但仅在企业纳管前提下 |
| 两个 bit（`bit0`/`bit1`） | 服务端 | 由开发者服务端改写，Apple 明确把重置责任交给开发者（举例："when a user sells the device to someone else"） | 状态记录，不是身份 |

关于「公开 API 不提供序列号」这一条，口径必须精确：`UIDevice` 的标识与设备属性页只列出 `identifierForVendor`、`localizedModel`、`model`、`name`、`systemName`、`systemVersion`、`userInterfaceIdiom`，不含序列号或 UDID【L1；API 清单穷举】。因此可写的是「公开 SDK 未暴露该能力」；Apple **未发布**任何一句「App 不得读取序列号」的原文，所以不得写成「Apple 明令禁止」。政策侧可引的条目只有四条已核到原文的：2.5.1（App 只能使用公开 API 并须运行在当前发布系统上）、2.3.1(a)（以误导方式营销可致下架与终止账号，原文明确把 "promoting content or services that it does not actually offer (such as iOS-based virus and malware scanners)" 列为理由）、5.1.1（数据收集与存储）、5.1.2(i)（必须通过 App Tracking Transparency API 取得明确授权）与 5.1.2(iii)（不得试图秘密构建用户画像，不得尝试、促成或鼓励他人识别匿名用户或据已收集数据重建用户画像）【L1 条款；以抓取当日版本为准】。

这里要放一条**误解校正**：因为 Guidelines 全文 `fingerprint` 命中为零，「Apple 政策禁止设备指纹」这一句在本篇**不成立**。可成立的最强表述是：Apple 政策禁止的是**在未获 ATT 授权的情况下识别匿名用户或重建用户画像**（5.1.2(iii)），并把硬件级标识符的可见性限制在 MDM 与需授权的接口之内；至于「设备指纹」这一技术类别本身，既没有被 DeviceCheck 文档背书（那些页面不含任何指纹限制句），也没有被 Guidelines 单独命名。同时 2.3.1(a) 提供了另一个方向的证据：Apple 不承认第三方安全/扫描类 App 的能力宣称，这与「不提供越狱判定服务」的口径一致——判定权既不在 Apple 接口里，也不在第三方扫描 App 的宣传里。

设备身份的另一面是它**已被计价**。同行评审测量记录，Ad Hoc 自助签名这条通道把 Apple 的设备注册机制当作可交易资产运作：个人开发者账号每年最多注册 100 台设备（前 10 台即时通过、其后需 24–72 小时审核），而论文记述对 macOS 的执行放松使实践中可绑定至约 200 台，据此折算单台设备的绑定成本约 3.4 元人民币，终端支付为该成本的 10–30 倍，中间层加价形成 900%–3,000% 的利润率；上游证书站点维护跨多账号的密钥池并行或随机派发证书以稀释可识别的绑定模式，并提供「保修期内证书被吊销可免费更换」的预付码机制【L2 USENIX Security 2026；研究观测下界、地理聚焦中国、仅覆盖 Ad Hoc 通道，且作者已将发现上报 Apple、敏感清单不公开故第三方无法完整复核】。这一组数字对防线设计的含义不在于规模本身，而在于**结构**：设备身份（UDID）与账号配额不是身份凭证，而是可批量采购、可转售、带吊销对冲的商品。因此任何把「这台设备注册过」当作可信依据的策略，其强度不超过该配额的采购成本。

与此相对，Apple 自己提供的「设备相关但非身份」的信号在设计上刻意避免稳定：`assessing-fraud-risk` 写明 "For privacy reasons, App Attest keys stored on device don't survive these events, forcing your app to generate a new key on the same device."【L1】这句是判断「App Attest 能否当设备 ID 用」的直接依据——**不能**，且不能的原因是 Apple 有意为之。因此「Apple 通过 App Attest 提供设备唯一 ID」是错误口径；平台侧根本不提供跨 App 的稳定设备 ID，能提供的只有「同一 App 在同一设备上的密钥与断言历史」这一有限视图。

**谁能验证／验证到什么强度／谁不能验证**：Apple 能验证一次 DeviceCheck 请求来自其账号体系下的自家 App（框架页原文："The server-to-server APIs also let you verify that the token you receive comes from your app on an Apple device."）；MDM 管理员能在纳管前提下验证序列号与 UDID 属性；**任何人都不能把上述任一标识符当作「设备真实性」的证明**——它们回答的是「是不是同一台/同一个 vendor」，不是「这台设备是否可信」。

## 3.5 App 完整性：签名类别、分发版本与「改造版应用」

Apple 对「被改动的 App」有一段完整、逐字可引的威胁模型，值得整段引用，因为它同时给出了威胁、机制与场景词："Someone who modifies your app and distributes it outside the App Store can add unauthorized features like game cheats, ad removal, or access to premium content. The App Attest service gives your app a way to assert its validity so that your server can more confidently provide access to sensitive resources."【L1 框架页】配套的三句决定了架构选择：端侧自检不可信（"You can't rely on your app's logic to perform security checks on itself because a compromised app can falsify the results."）；自行上报结果可被绕过（"A compromised version of your app could falsify the verification result, thus circumventing App Attest."）；被篡改副本在**真实未改动设备**上无法产生有效断言（见 3.2 第二句）【均为 L1 原句】。场景词清单到此为止：`game cheats`、`ad removal`、`access to premium content`、`sensitive or premium content`、`promotional offer`、`flag a device that you've determined to be fraudulent`、`advertising fraud detection`。DeviceCheck 与 App Attest 文档中**未出现** `bot`、`automation`、`emulator`、`device farm`、`jailbreak`【负面结果：逐页检索】——报告不得把这些词写成 Apple 官方场景表述。

平台机制对「跑的是哪个包」的确切刻画只有一处，即证明对象里的 `apple_validation_category_01`（`UInt32`）与 `apple_bundle_version_01`（`String`）。档位表照抄如下【L1 `validating-apps-that-connect-to-your-server`】：

| 值 | Apple 原文描述 |
| --- | --- |
| 0 | Invalid. |
| 1 | An operating system executable. |
| 2 | An executable distributed through TestFlight. |
| 3 | An executable signed by a development code signing identity. |
| 4 | An executable distributed through the App Store. |
| 5 | An executable distributed using an Enterprise Universal Provisioning Profile, or ad-hoc distribution. |
| 6 | An executable signed using Developer ID. |
| 7、8、9 | These values aren't appropriate as they represent categories of binaries that the system generates in certain restricted situations. |
| 10 | An executable signed with a code signing identity that doesn't match any other category. |

服务端须 "Verify the `validationCategory` value within the CBOR dictionary in the authenticator data."，并可据此制定「只接受第 4 或第 2 档」这类策略。**但 Apple 未给出「重签名一定落哪一档」的断言**，因此本篇只能写成「Apple 在证明里额外报告签名身份类别，服务端可据此自行制定策略」，任何把某档与某类改造包直接对应起来的说法都是推断。

端侧为什么做不到同等判断，由一篇同行评审 SoK 给出机制层解释：**"On iOS, one cannot check the signature of an app directly. All apps from the App Store are re-signed with Apple's signature, and iOS devices only allow execution of Apple-signed binaries, unless the check is disabled by a jailbreak. However, apps could be re-signed and executed on a jailbroken device, which is why apps could test whether an `embedded.mobileprovision` file is present. Apple does provide an App Attest Service though to verify apps are unmodified"**【L2 EuroS&P 2025】。三条口径由此落地：端侧没有「查原始签名者」这条路，只能退化为「随包描述文件是否存在」这类弱特征；Apple 把「验证应用未被修改」的能力放在服务端；端侧完整性检查的前提（设备未被越狱）与服务端证明互为依赖，任一失效即整体失效。

生态侧的公开观测则说明「改造版应用」是规模化现象而非个案：在 2,654 个实际下载的非官方应用中有 **442 个**在描述里明确且仅以「破解 VIP」为卖点；对抽取出的 **8,085 个 dylib** 做 VirusTotal 扫描，**仅 3 个被标记为恶意**，作者据此写「多数注入组件是为增加功能而非分发显性恶意代码；但其行为破坏了应用逻辑并削弱 iOS 安全隔离」【L2 USENIX Security 2026，研究观测下界】。同一论文给出「以运行期加载方式规避以重打包为基础的检测机制」的能力存在性结论，但需注意其被引方法绝大多数以 Android 为对象，证据强度受类比限制。企业证书侧则有两个已公开的吊销失效条件：OCSP 响应在设备侧缓存 **3–7 天**（Apple 一手部署指南，发布日期 2026-09-17），以及同行评审论文记述的「存在通过阻断设备回连 Apple 证书校验端点使已吊销签名继续可用的做法」——本篇只记录该结论与缓存窗口，不写任何实现【L1 ＋ L2】。

两点缺口必须并列写出：**本次未获得把「iOS 重签名包」作为独立研究对象并给出检测方法的同行评审论文**（可核实的体系化重打包检测研究集中在 Android），因此正确表述是「iOS 侧重签名检测的公开研究密度显著低于 Android」，**不是**「iOS 重签名威胁不严重」；同时厂商产品普遍列有 repackaging/re-signing 检测项，但**无公开可核实的第三方评测**验证其检出率与误报率。此外，题录已核但全文未取得的两篇 2026 年研究（ACM WiSec 2026 关于第三方 iOS 应用市场的大规模研究、ACM IMC 2026 关于伊朗第三方 iOS 应用商店的研究）只能作为「此类研究对象已存在」的证据，不得引用其任何数字。

**谁能验证／验证到什么强度／谁不能验证**：服务端可密码学验证签名身份类别与分发版本号（前提是自己完成 App Attest 集成）；设备在执行层由代码签名强制保证「安装或上次更新之后未被修改」（L1，运行期逐页校验）；**没有人能在设备端验证「原始签名者是谁」**，也没有第三方能验证商业检测产品的实际检出率。

App 完整性还要放在「它从哪条通道装上来」这个更大的谱系里看。可核实的分类骨架不是社区的形态术语，而是一份同行评审论文给出的通道表（USENIX Security '26 Table 2，机制类别与有效期）【L2】：

| 通道 | 论文给的机制类别 | 有效期口径 | 与本篇防线的关系 |
| --- | --- | --- | --- |
| In-House（企业开发者证书） | 企业分发证书 | 1 年 | `validationCategory` 第 5 档；受 OCSP 首启动校验与 3–7 天缓存约束 |
| AltStore | 免费开发者账号 | 7 天 | 第 3 档（开发签名身份）；周期短使其难以支撑长期订阅类滥用 |
| TestFlight | 官方 Beta | 90 天 | 第 2 档；属「合法但可被误用」的通道 |
| Jailbreak | 论文原文机制栏 "Root access via iOS vulnerabilities" | Permanent | 使端侧完整性检查的前提失效；本身不提供跨设备规模化的应用分发 |
| TrollStore | 论文原文机制栏 "iOS CoreTrust exploit" | Permanent，但论文明确「仅限较旧 iOS 版本」 | 绕过审核与账号绑定的本地安装路径 |
| WebClip | 主屏快捷方式 | Permanent | 无 App 包参与，完整性防线对其不适用；已有预印本记录其被用于赌博、诈骗与色情内容分发（等级 2/5，未获同行评审确认） |
| Ad Hoc | 个人开发者证书 | 1 年 | 第 5 档；本次灰产测量的主要对象 |

术语分级在这里产生实际差别：社区形态词（tethered 系列、rootful/rootless）属 L5，只作一次性历史注解；通道＋机制＋有效期这张表属 L2；而 Apple 自己的文本完全不使用上述任何术语，平台安全指南只按 in-house / App Store / Ad Hoc / TestFlight 等分发口径发言【L1】。因此本篇的叙述顺序固定为：先用 L2 通道表定义谱系，再用 L1 分发口径描述规则，社区术语仅在必须交代历史时一句带过，且不写成分类学表格。同一论文另有一句可用于「时代口径」的判断——企业证书通道 "was once common but is now tightly regulated"，以及越狱 "introduces severe security risks and is impractical for most users"【L2】；两句都是研究者的状态描述，不是 Apple 口径，引用时须保留这一差别。

## 3.6 误解校正：「设备真实性 = 越狱检测」

这是本篇要校正的第一号混淆，因为它的后果最直接：如果一个团队相信「设备真实性就是越狱检测」，它会把整条防线建在一段从社区复制来的特征检查上；而 Apple 与公开研究从两个不同方向否定了这条路。

**Apple 的否定是定义层的。**平台侧从未提供「是否越狱」的判定：DeviceCheck 与 App Attest 文档没有该类 API，MDM 响应键名里没有该类字段，客户端错误码里没有该类码（见 3.3）。Apple 提供的是另一样东西——由它签发、可被服务端验证的密钥与设备属性证明。「设备真实性」因此是**密码学可验证**的：有明确签发方、有证书链、验证过程可离线复现、失败可归因。

**公开研究的否定是效果层的。**越狱检测是**启发式**的：它检查文件是否存在、字符串是否出现、动态库是否被加载，结论没有任何第三方可复核途径。两篇同行评审工作测量了它的效果：

| 研究 | 口径与样本 | 失效结论（附条件） |
| --- | --- | --- |
| Kellner 等，EuroS&P 2019【L2】 | 银行类 34 个 App（其中 18 个实现越狱检测，即 44% 未尝试检测）；跨类别另取 3,482 个流行 App；59% 含某种形式检测（银行类 53%） | 「34 个银行应用中除 1 个之外全部可被完全攻破」；「试图检测越狱的应用除 1 个之外全部检测失败」；绕过路径被描述为现成的函数 hook，「既不需要逆向工程也不需要修改应用本身」；作者归因于开发者从社区平台复制粘贴检测代码。**条件：2018–2019 年、作者所用 hook 能力与当时的检测实现下** |
| Steinböck 等（HALY），EuroS&P 2025【L2】 | 每 OS 各 2,646 个 App（自 3,322 池筛出）；静态＋动态双口径；作者自述结果应视为**下界** | 静态 96.7% 存在 root/jailbreak 检测，但动态口径仅 24.9% 真的检查 root 相关文件与目录权限、6.8% 检查是否装了 root 相关应用；名称特征检测集中于 blackra1n（5.5%）与 unc0ver（1.4%），**未发现任何针对 iOS 15（2021-09）及之后发布越狱的专用检测**；结论为「单个或少数几项加固技术说明不了自防护的有效性，目前的采用状况可能给用户和开发者一种虚假的安全感」「各项防护单独可被绕过，有效性取决于实现与是否多层叠加」 |
| Irfan 与 Uto，ESORICS 2025 International Workshops（Springer LNCS pp. 558–577）【L2-workshop，部分核实】 | 以 rootless（无根）越狱为条件重新评估 iOS 银行应用越狱检测有效性 | 只可写「该命题已进入同行评审议程」；样本量与绕过成功率数字本次未取得全文，**不得引用其任何百分比** |

两个概念混淆之后会连带产生四个次生错误，一并列入本篇的校正表：

| 常见说法 | 校正后的口径 | 依据 |
| --- | --- | --- |
| 「DeviceCheck 的两个 bit 不可重置」 | bit 由开发者服务端改写，Apple 原文明确把重置责任交给开发者（含「用户把设备卖给别人」的例子）；Apple 带 "can't reset" 措辞的是 **App Attest 指标**，两者是不同的对象，不得混写 | L1 `DCDevice`、`preparing-to-use-the-app-attest-service` |
| 「App Attest 是 DeviceCheck 的演进或替代」 | `DCAppAttestService` 与 `DCDevice` 同属 DeviceCheck 框架，文档从未称替代；两者门槛不同（iOS 11 与 iOS 14）且**均未被标记 deprecated**；官方口径是并存、共同喂给开发者的风险评估 | L1 框架页与 platform 元数据；负面结果：无「取代」叙述 |
| 「Apple 不提供任何设备风险信号」 | 不提供越狱/篡改判定，但**提供**一个近似计数指标（近 30 天该设备上的 attested key 数量）并附定性提示「高于预期可能意味着…」 | L1 `assessing-fraud-risk` |
| 「MDM 协议里应当有越狱/完整性字段」 | 95 个响应键名里没有任何该类字段；真实存在的完整性信号是 `DevicePropertiesAttestation`，且 iOS 上可证明属性不含 SIP、Secure Boot、第三方 kext | L1 逐字段核对 |
| 「Apple 政策禁止设备指纹」 | Guidelines 全文 `fingerprint` 零命中；可引的是 5.1.2(iii)「识别匿名用户/重建画像」与 5.1.2(i) ATT 授权 | L1 条款全文检索 |
| 「研究表明越狱检测普遍无效」 | 需逐年、逐年口径地引：Kellner 2019 的「除 1 外全部可绕」限作者条件下；HALY 2025 给出「静态检出 ≠ 动态执行」的口径修正；且论文测的是应用内实现与其运行时行为，**不是商业检测库产品** | L2 两篇；C5 修正 |
| 「检测存在大量误报」 | 误报被公开承认为工程事实（规范文档把「即使牺牲漏报也要最小化误报」列为设计约束、厂商知识库承认），但 iOS 侧发生率**无公开可核实数据**，本篇不出现任何误报率数字 | L2 转述 OWASP ＋ L3 厂商 ＋ 负面结果 |
| 「A12 及以后绝无公开越狱／长期无公开越狱」 | 未取得 Apple 一手或学术直接表述，bootrom 类利用的芯片代际边界同样只有社区与媒体口径；可写的只有「公开可用的绕过通道存在明确的版本/机型上界」——由 HALY 自述「越狱受限于 iOS 16、实验机型 iPhone 8（A11）」与 USENIX '26 的「TrollStore 仅限较旧 iOS 版本」两条支撑 | L2 ＋ 负面结果（禁止绝对断言） |
| 「corebaseless 是一种越狱形态」 | 简报推翻：该拼写在社区文档、百科与论文中均无出处，疑为误记；可核实的社区术语只有 tethered 系列与 rootful/rootless（L5），而分类骨架应改用 USENIX Security '26 Table 2 的通道分类（In-House 1 年、AltStore 7 天、TestFlight 90 天、Jailbreak 永久、TrollStore 永久但限旧版本、WebClip 永久、Ad Hoc 1 年）【L2】 | 检索零命中 ＋ L2 分类表 |
| 「越狱是小众极客行为」 | Kellner 摘要原文写 "jailbreaks gain tremendous popularity among regular users"，而其转引的「Cydia 称超过 3,000 万设备」是服务方自述、无方法学，标注为不可用于事实陈述 | L2 ＋ 口径限制 |
| 「重签名包必然落入 `validationCategory` 的某一档」 | Apple 未作此断言；只能写「证明里报告签名身份类别，服务端可自行制定策略」 | L1 表格照抄，不推断 |
| 「iOS 侧对抗证据少，说明风险更低」 | 相反：这是可观测性与工具链限制（越狱覆盖、闭源虚拟化、分发端审核压制样本），本篇按 B6 口径整段声明不得推定风险更低 | L2 ＋ 口径声明 |

本节唯一不允许出现的写法是**自制「信号—易伪造度」量化排序表**。可行的写法只有：定性分层并逐条挂来源（具体越狱名称＝强指标，`fork` 与 `_dyld_*` 类调用＝用途不唯一，HALY 实测约 95.7% 的 iOS App 调用 `_dyld_image_count`、74% 同时调用它与 `_dyld_get_image_name`，作者据此把相关结果标为「高不确定性」【L2】）；任何超出来源的强度差异显式标注「分析推断」；检出率、误报率与绕过成本三列一律标「未公开」。此外，同一信号可跨类别（检测某 hooking 框架既算 hooking 检测又算 root 检测），故各论文的信号计数加总不等于 100%，不得把不同来源的计数相加或排成互斥等级。

**谁能验证／验证到什么强度／谁不能验证**：设备真实性可由服务端验链达到密码学强度；越狱检测只能由**产生它的那台设备上的那段代码**「验证」，强度是启发式，且 Apple 已明文警告该结论可被伪造。把后者当作前者，就是本节的误解本身。

**本章口径边界**

- 四概念的定义域由本章表格锁定；后文出现「真实性/完整性/身份/App 完整性」时一律按此映射，不按社区习惯混用。
- 判定强度四档（密码学可验证／规则可验证／启发式／不可验证）是来源层级标签，不是量化排序，不得转换为分数。
- 「未获得可核实条目」与「不存在」在本书写中始终分列；穷举型负面结果可写「不存在该字段」，检索型只写「未取得」。
- Apple 关于系统被攻陷的可引原句只有两句，超出即需标注为分析推断或删除。
- 越狱检测的一切量化数字必须带样本池、判据、年份三件套；误报率一律空缺。

---

# 责任分配：平台给信号与规则，判定留给开发者

## 4.1 Apple 的明文口径：信号与一个近似计数指标

责任分配不是本报告的推测，而是 Apple 写进文档的句子。核心原文出现在 DeviceCheck 框架页，三段结构值得逐段拆开：

> "No single policy can eliminate all fraud. For example, App Attest can't definitively pinpoint a device with a compromised operating system. Instead, the DeviceCheck services provide information that you can integrate into an overall risk assessment for a given device."【L1 Apple 文档 `https://developer.apple.com/documentation/devicecheck`】

| 分句 | 它声明了什么 | 它把什么留给了谁 |
| --- | --- | --- |
| 没有单一策略能消除全部欺诈 | 平台不承诺覆盖完备性 | 「够不够」的判断留给使用方 |
| App Attest 无法确定性地指认一台操作系统已被攻陷的设备 | 平台**自我限定**了判定能力，用词是 definitively pinpoint | 任何「这台设备被改了」的结论都不是 Apple 下的 |
| 这些服务提供供你并入自身整体风险评估的信息 | 平台的产出被定义为**输入**，不是判决 | 风险评估的所有权属于开发者（原文主语为 you，且明确写为「你的整体风险评估」） |

配套的三句原文把「处置」这一层同样推给开发者。断言校验失败时："If the assertion object fails verification, it's your responsibility to decide how to handle the request."；证明校验失败时的唯一指令是丢弃密钥标识符（"If your server fails to verify the attestation object, discard the key identifier."）；而两个 bit 的语义自始至终归开发者——"Apple records the bits for you, and reports the bits back to you, but you're responsible for keeping track of what the bits mean."，重置时机同样归开发者（"You're also responsible for determining when to reset the bits for a given device; for example, when a user sells the device to someone else."）【均为 L1 原句】。

Apple 唯一一次主动给出「处置建议」的场景，方向是**降级**而非升级：当设备不支持 App Attest 时，App 应 "gracefully bypass the service"，服务端则 "can no longer require assertions"【L1】。这条容易被工程团队忽略，但它是官方口径：**可用性缺失时的默认处置是放行，Apple 不提供替代判定手段**。把它读成「Apple 建议对不支持设备加严」是反向误读。

量化输入侧，Apple 给的东西只有一件，且自带限定措辞："Although it isn't possible to detect fraudulent activity with absolute certainty, App Attest does provide a metric to assess its likelihood. Specifically, you can get an approximate count of unique attestations for your app on a particular device. A count that's higher than expected might be an indication of a compromised device that's serving multiple compromised instances of your app."【L1 `assessing-fraud-risk`】注意三个词的性质：`isn't possible … absolute certainty`（否认可靠性）、`approximate`（承认非精确）、`might be an indication`（拒绝因果）。同页还给出唯一被点名的滥用形态："you might need to guard against an attack that uses a single compromised device to serve assertions to many subscribers"——一台被攻陷设备为大量订阅者供给断言。这是 Apple 文档里最接近「威胁画像」的一句话，也仅此一句。

把三条通道的「给到什么／不给什么」并列，责任分配就已经被 Apple 的文本预先决定了：

| 通道 | Apple 给出的东西 | Apple 明确不给出的东西 |
| --- | --- | --- |
| DeviceCheck 两个 bit | 每设备两位的持久存储与回读、`last_update_time`、以及「token 来自你账号下的自家 App 且来自 Apple 设备」这一验证 | 位的语义（"you're responsible for keeping track of what the bits mean"）、重置时机、位的取值建议 |
| App Attest 证明与断言 | 由 Apple 签发的证明对象与断言、`validationCategory` 与 bundle 版本、counter 单调性、近 30 天 attested key 近似计数 | 「设备是否被改动」的判定、阈值、断言失败后的处置（"it's your responsibility to decide how to handle the request"）、不支持设备上的替代信号 |
| Managed Device Attestation | 由 Apple Enterprise Attestation Root CA 链上的叶证书及其设备属性 OID、无法确定属性时的空白值/省略/拒签行为 | iOS 侧的 SIP/Secure Boot/kext 等价属性、对属性缺失原因的区分、任何「可信/不可信」的结论（Apple 只说「可作为信任评估的一部分」） |

同一张表还揭示了一条容易被忽略的对称性：三条通道都把**验证动作**（验签、验链、验字段）设计成可机械执行的，而把**解释动作**（这个值意味着什么、该怎么处置）设计成不可外包的。这不是文档缺陷，而是 Apple 的责任声明方式——它给出的是可验证的原始材料，不给出材料到结论的推理权归属。

## 4.2 该口径的边界：不得反向写成「Apple 不给任何量化输入」

「Apple 把判定推给开发者」这句话很容易在传播中翻转过载，因此需要一条边界说明，防止两个方向的误读。

**方向一：不得写成「Apple 完全不给任何量化输入」。**这是核查过程中被明确修正的一处。Apple 确实给了一个指标——同一设备上与本 App 相关的唯一 attest 的**近似计数**，覆盖窗口为近 30 天，通过 receipt 字段 17 经服务器到服务器接口取回，并附带定性提示「高于预期可能意味着一台被攻陷设备在服务多个被攻陷实例」【L1】。同时它还给了若干**规则级**量化约束，都是明文要求而非建议：challenge 至少 16 字节、receipt 创建时间不得超过五分钟、attestation 阶段的 `counter` 必须为 0、后续断言的 `counter` 必须单调递增、指标接口在字段 19 日期之前刷新只会得到 304、过期后 Apple 服务端可能不再受理。把这些一并否认掉，等于把「Apple 不给判定」夸大成「Apple 什么都不给」，那是错误口径。

**方向二：不得反向写成「Apple 已经能识别被攻陷设备」。**指标的三个限定词（approximate / might be / isn't possible with absolute certainty）都是 Apple 自己加的，且正当增长来源被明文列举：重装、从备份恢复、设备转让给另一用户。同页还有一句必须与「SE 隔离」并置的上限：修改操作系统「difficult and an unlikely source of widespread fraud」——这是**Apple 文档自评，不是测量结论**，本篇不得据此降低风险等级，也不得把它转写成「iOS 上这类攻击罕见」。

**方向三：不得用「未公开」冒充「不存在」，也不得假装知道绕过成本。**Apple 未公开的项包括：risk metric 的计算定义与精度、服务端速率上限与 SLA、越狱/篡改判定的任何官方阈值、模拟器与虚拟化环境下的行为、`403 Forbidden` 的触发条件、receipt 在 Apple 侧的保留期、SE 内 App Attest key 的数量上限、对具体绕过手段（越狱、重签名工具、企业证书分发）的公开结论与胜率数据。仅有的两个数字（每 App 每日渐进放量不超过 1,000 万用户、跨全部安装调用建议低于 100 req/s）原文自称 rule of thumb，且 Apple 声明阈值会动态浮动以保护其证书签发基础设施——它们不是配额也不是 SLA【L1 `preparing-to-use-the-app-attest-service`】。在这个空缺之上，独立学术绕过研究为零篇（见 1.5 与 2.5），因此「App Attest 已被绕过」与「App Attest 无法被绕过」两种断言在本篇口径下**同样不成立**。

一处必须保留的口径冲突也要说明：DeviceCheck 与 App Attest 的框架文档里，与「不得用于追踪/指纹」最接近的表述只有 "while maintaining user privacy" 与 token 的 ephemeral/单次使用属性；政策侧的硬依据在 Guidelines 5.1.2(iii) 与 5.1.2(i)，而 Guidelines 全文不出现 `fingerprint` 一词【L1；负面结果】。所以「Apple 在 DeviceCheck 文档里禁止设备指纹」这句在本篇不成立——限制存在，但**不在这份文档名下**，取证位置必须换。

## 4.3 三层责任的实际落点

把 4.1 的口径落到具体事项上，三层责任的边界相当清晰，且每一层都带着自己那一层的缺口。

| 层 | 承担者 | 已经承担的具体责任（有 L1 依据） | 该层的责任缺口 |
| --- | --- | --- | --- |
| 机制与政策 | Apple | 签发证明与断言验证通道、维护两个 bit 的存储与回读、下发 ES256 JWT 鉴权与 dev/prod 环境分离、维护 App Attest 与 Enterprise Attestation 两套 PKI、定义 challenge/counter/时效等规则、执行企业证书吊销（首次启动回连 OCSP、吊销连坐该证书签名的全部应用）、以规则与准入审查管理 ADEP | 不提供越狱/篡改判定、不提供阈值、不提供处置规则、不发布吊销数量统计；不支持时官方指令是降级放行 |
| 判定与处置 | 开发者与业务方 | 生成并记忆一次性 challenge、验链与字段比对（RP ID、`aaguid`、`counter`、`validationCategory`、bundle 版本、receipt 字段 2/3/12/19/21）、持久化 per-user-per-device 的 keyId/公钥/receipt 且 dev 与 prod 分份、自建 ES256 密钥与轮换、429/503/304 退避与指标刷新调度、下载并长期信任 Apple 私有 CA 根、重装/迁移/恢复后的重新证明流、**定义两个 bit 的含义并决定何时重置**、依据 risk metric 自行调阈值、决定断言校验失败后如何处理该请求 | 阈值与误报容忍度只能自建；risk metric 计算定义未公开；无法验证设备是否被改动，只能验证密钥与 App 实例 |
| 纳管与证明链校验 | 企业与 MDM 管理员 | 完成 MDM 注册并下发 Device Information / Network Information access right、校验 `DevicePropertiesAttestation` 叶证书是否链到 Apple Enterprise Attestation Root CA、对「缺失 OID 或空白值」按 Apple 说明处理（可整体忽略或标记为需调查事件）、选用 Device information attestation 或 ACME `device-attest-01` 两种工作流之一、执行企业应用的 HTTPS 与 `itms-services://` 分发并保证设备能回连 Apple | 能力只对受管设备存在；iOS 上可证明属性不含 SIP、Secure Boot、第三方 kext（这三项 Apple 只写作 macOS 14.2+）；`SystemIntegrityProtectionEnabled` 亦仅 macOS；硬件门槛 A11/M 系列及以上 |

ADEP 一侧的规则层责任还有一条需要单独说明，因为它决定了「企业证书为何天然是滥用高发面」：Apple 把该计划定位为 App Store、Apple Business Custom Apps、Ad Hoc 与 TestFlight 都无法满足时才使用的兜底通道，并要求「只用于组织内部专有 App、只私发给员工、组织须具备确保仅员工可下载并保护成员凭据的系统」；准入门槛是 100 名及以上员工的法人实体（不接受 DBA/虚拟主体/分店）、D-U-N-S 编号、与组织关联的公开网站域名、299 美元会员年费，外加通过 Apple 的验证访谈与持续评估、续签时重新核验、以及「Apple 保留自行拒绝申请的权利」【L1，developer.apple.com 企业计划页】。换言之，**这条通道的合规性完全靠组织自身的分发控制与 Apple 的事后吊销，不靠设备端机制**；而吊销存在 3–7 天的设备侧缓存窗口，且同行评审论文明确记述了「回连被阻断则已吊销签名仍可用」这一已公开失效条件【L1 ＋ L2】。iOS/iPadOS 18 及以后，不经设备管理服务手动安装的 in-house 应用还需重启设备才能完成对描述文件的信任，而「该 App 的开发者在你设备上不受信任」这一手动信任步骤是攻击链上的关键人类决策点【L1 部署指南与 Apple 支持文档】。

一处可核实性与责任错配值得单独点出：**Managed Device Attestation 是平台级信号，但它不等同于 macOS 级的完整性证明。**企业在 macOS 上可以拿到 SIP 状态、Secure Boot 模式与第三方 kext 许可三项由 Apple 证明服务器签发的属性；在 iOS 上拿到的只有序列号、UDID、sepOS 版本、SoftwareUpdateDeviceID、OS 版本、LLB 版本与 freshness。任何「我们的 MDM 能证明 iPhone 未被越狱」的表述都超出了 Apple 文档给出的属性集，属不成立口径。

开发者侧的责任清单可以再具体一层。按 Apple 文档要求，完成一条端到端的服务端防线至少需要自建以下六项，而 Apple **未对其中任何一项**提供托管方案、参考实现或兼容性承诺【负面结果：本次抓取范围内不存在】：

1. ES256 JWT 签发、密钥托管与轮换（DeviceCheck 与 App Attest 指标接口同源于 APNs provider token 流程，且申请 App Attest 用的加密密钥时必须启用 DeviceCheck 服务）；
2. per-user-per-device 的 keyId／公钥／receipt 三元组存储，开发环境与生产环境分份保存（"keep development pairs separate from production pairs"），且不得在同一设备的多用户间复用同一 key（原文指出这会削弱安全保护）；
3. challenge 的生成与记忆、`clientDataHash` 的构造，以及 counter 单调递增的状态机（counter 需持久化用于下一次断言比较）；
4. 429/503/304 与 `DCError.serverUnavailable` 的差异化退避策略——证明阶段联系不上服务时应以**同一 key 与同一 clientDataHash** 重试（原文说明这样「有助于保住该设备的风险指标」），其余错误则丢弃 keyId 重新生成；
5. Apple 私有 CA 根证书的下载与长期信任（App Attest 走 Apple Private PKI Repository，MDM 侧走 Apple Enterprise Attestation Root CA，两者不可互换）；
6. 重装、设备迁移、从备份恢复之后的重新证明流，包括 App Clip 与主 App 共用同一 key 并把标识符存入共享容器的处理。

这六项里没有任何一项是「判定逻辑」，全都是凭据、状态与重试的工程设施。换句话说，**Apple 让服务端能验证，但把「验证所需的全部基础设施」交给了接入方**；这也是为什么本篇把采用率极低（HALY 测得静态 4.1%／动态 1.1%【L2】）视为一个结构性结果而非偶然——接入成本与运维负担都在开发者侧，而收益是一个需要自行调阈值的近似计数。

## 4.4 用责任分配框架改写「Apple 有没有防住 X」

上述三层不是描述性分类，而是提问过滤器。本篇采用的规则是：**任何「Apple 有没有防住 X」的提问，都要先改写成三个子问题——哪一层负责、该层给到什么信号、判定成本落在谁身上**。不经过这一步的提问在本篇无法回答，因为「防住」在四层里的判据完全不同。

| 原始提问 | 改写后的可答形式 | 本篇能给的答案 |
| --- | --- | --- |
| 「Apple 能不能防住越狱设备上的薅羊毛？」 | 平台侧提供什么信号 → 服务端如何解释 → 阈值谁定 | 平台不提供越狱判定；服务端只能拿到两个 bit、断言合法性与近 30 天 attested key 近似计数；阈值与处置全归业务方【L1】 |
| 「App Attest 能不能挡住重签名包？」 | 被改动副本在哪一层失效 | 在真实未改动设备上，被篡改的 App 无法产生有效断言（Apple 原文）；系统被改动者「可能绕过限制」（Apple 原文，能力存在性由厂商自认）；独立绕过研究零篇，成本未公开 |
| 「MDM 能不能看出员工 iPhone 被动过？」 | 该能力是否只对受管设备存在、iOS 属性集含什么 | 只对受管设备存在；可证明属性不含 SIP/Secure Boot/kext；对「被动过」没有布尔字段；缺失 OID 只表示 Apple 无法确定该属性【L1】 |
| 「企业证书被吊销了，App 是不是立刻停？」 | 生效条件在哪一层（网络侧与缓存窗口） | 首次启动回连 OCSP；响应在设备侧缓存 3–7 天；吊销连坐该证书全部应用；回连被阻断时该机制不生效（论文记述的已公开失效条件）【L1＋L2】 |
| 「用 IDFV 当设备锚点行不行？」 | 该标识符的官方失效条件由谁控制 | 官方明文列出会变的情形（删光同 vendor App 后重装、Xcode 测试构建、ad-hoc 安装）与 `nil` 窗口（重启后未解锁）；不能作跨时间锚点【L1】 |
| 「越狱检测能不能作为风控输入？」 | 该结论强度属哪一档、由谁测过 | 属启发式；同行评审测量显示其有效性取决于实现与多层叠加（EuroS&P 2019 与 2025，各带年份与口径）；Apple 明文否定应用自证路线；误报率数据空缺【L1＋L2】 |

改写规则还带来一个副作用值得写明：它把「要不要信任这台设备」从**技术判断题**变成**成本归属题**。在同一台设备上，Apple 能给出的最强事实是「某个 key 由真实设备上指定签名类别的 App 实例生成」，而「这够不够放行一次兑换」需要业务方自带误报容忍度——Apple 文档里那句 "you'll have to tune your risk assessment logic based on the typical numbers that you see over time" 就是把这项成本写进协议的句子。因此本篇对「防线是否成立」的最终表述形式始终是条件式的：在**已完成 App Attest 集成、设备支持该服务、且服务端自建了阈值与处置**的前提下，平台侧信号能排除掉哪一类对手；以及当这三项前提任一缺失时，责任回落到哪一层。

最后给出本篇结论的天花板，也是合并时供后续章节引用的边界：可安全写入报告的正面结论只有「三条通道各自能证明什么」与「Apple 自我限定判定能力」两类；对抗面可安全写入的只有带年份与样本口径的检测有效性测量、带作者局限声明的灰产结构观测，以及分发规则的失效条件。**任何「Apple 已防住/未防住某类攻击」的绝对句、任何绕过成本估算、任何误报率数字，都在本篇的口径体系之外。**

供合并稿与后续各章直接使用的检查清单如下，每条对应本节的一处依据：

- 这个提问问的是**哪一层**？（端侧启发式／平台侧信号／服务端判定／生态侧规则）——若答案跨层，先拆成两个提问。
- 该层给出的信号是**密码学可验证、规则可验证、启发式还是不可验证**？只有前两档能支撑「验证」这个词。
- 结论需要**阈值**吗？若需要，阈值来自哪里？本篇范围内没有任何一处阈值由 Apple 提供，risk metric 只有「look for this value to be a low number」这一句定性提示。
- 该结论是否依赖**设备支持**？`isSupported` 为假时 Apple 的官方处置是服务端不再要求断言，这会让同一套策略在不同机型上给出不同覆盖。
- 是否涉及**受管前提**？MDM 的一切能力都以注册与 access right 为前提，个人设备不适用。
- 引用量化数字时，**样本池／判据／年份**三件套是否齐备？缺失即改写成定性表述或删除。
- 若答案是「查不到」，写的是「本次核查未获得可核实条目」还是「不存在」？两者不可互换。

按这份清单执行的结果是：本篇能对「Apple 让服务端知道什么」给出封闭清单式的回答，也能对「某道端侧检测是否可靠」给出带年份与口径的部分回答，但对「某类攻击当前是否被防住」只能回答「该提问需要改写」。这不是回避，而是现有公开材料的真实分辨率。

**本章口径边界**

- 「Apple 不提供判定」与「Apple 不提供任何量化输入」是两句不同的话，本篇只主张前者；risk metric 与规则级数字（≥16 字节 challenge、5 分钟 receipt 时效、counter 单调递增、30 天窗口）均为 Apple 明文。
- Apple 关于「修改操作系统不常见」的表述一律标注为厂商自评，不用于降低风险等级。
- 三层责任的每一项都附 L1 依据；无依据的分配（如「厂商应负责检出率」）不写入本篇。
- 「未公开」清单以 1.5 与 4.2 为准；超出该清单的范围性否定（不存在、已被绕过、无法被绕过）在合并稿中亦不得出现。
- 改写规则是方法性约定，供后文各章在提出「防线有效性」判断前统一执行，不构成对新证据的排斥。

---
# DeviceCheck：两个比特位的官方语义与责任归属

## 5.1 官方定位与 API 形态：客户端 `DCDevice` 与服务端 REST 接口

本部分（第 5–8 章）转入平台侧机制本体：Apple 到底把「设备真实性」做成了什么样的接口、每个字段的确切语义是什么、哪些口径是 Apple 原话、哪些是社区转述。第 1–4 章已完成概念切分与责任分配框架，本部分不再重复其结论，只在需要处置口径处回指第 4 章。

先固定取证方式，后文所有 Apple 文档引文均受此约束：Apple 开发者文档页面为 JavaScript 渲染页，直接抓取 HTML 得到的是空壳；本章引文一律取自 Apple 文档的原始 JSON 接口（`developer.apple.com/tutorials/data/documentation/**.json` 通道），渲染前文本与代码 token 完整，故引文是文档源文本而非网页渲染文本。版本口径统一为「截至 2026 年 8 月版 Apple 文档」。另需说明一项取证上的重要事实：Apple Platform Security Guide（本地 `apsg.txt`，版权页标注 August 2026）经全文检索，对 `DeviceCheck`、`Device Check`、`App Attest`、`DCAppAttest` 均为**零命中**——即本版平台安全指南不设这两项机制的章节，凡涉及 DeviceCheck/App Attest 的一手口径，只能引 developer.apple.com 文档，不得伪造该出处；社区文章中「Apple 平台安全指南论述了 App Attest」属于错误引用。

DeviceCheck 框架页对自身的定位原文如下（abstract，逐字）：

> "Reduce fraudulent use of your services by managing device state and asserting app integrity."
> 【L1 Apple 文档：documentation/devicecheck】

这一句话同时覆盖了本章与第 6 章的两项能力：前半句「managing device state」对应 DeviceCheck 的两个比特位，后半句「asserting app integrity」对应 App Attest。框架页对系统组成的原文把客户端与服务端两侧一次性说清：

> "The DeviceCheck services consist of both a framework interface that you access from your app and an Apple server interface that you access from your own server. Using the `DCDevice` class in your app, you can get a token that you use on your server to set and query two binary digits of data per device, while maintaining user privacy. For example, you might use this data to identify devices that have already taken advantage of a promotional offer that you provide, or to flag a device that you've determined to be fraudulent."
> 【L1 Apple 文档：documentation/devicecheck】

据此可整理出 DeviceCheck 的 API 形态：

| 侧别 | 形态 | 官方要素 | 依据 |
| --- | --- | --- | --- |
| App 侧 | `DCDevice` 类 | `DCDevice.current` 单例入口（ObjC `currentDevice`）；`generateToken(completionHandler:)` 取得一次性 token | 【L1：documentation/devicecheck/dcdevice】 |
| 服务端侧 | Apple 提供的 REST 接口 | query / update / device-validation 三类 POST；鉴权为 ES256 签名的 JWT | 【L1：documentation/devicecheck/accessing-and-modifying-per-device-data】 |
| 数据面 | 每设备两个二进制位 | 服务端 JSON 字段 `bit0` / `bit1`；存储于 Apple 服务端 | 同上 |

注意官方措辞的精确性：Apple 写的是 "two binary digits of data per device"、"two per-device binary digits (bits)"，即「每设备两个二进制位」，而**不是**「两个语义固定的可写字段」。位的语义归属问题留给 5.2 展开；服务端凭据与错误码留给 5.6。框架页同时给出了这套接口的能力上限原文，这句话也是全篇「防线必须上移服务端」论述的第一块基石：

> "The server-to-server APIs also let you verify that the token you receive comes from your app on an Apple device."
> 【L1 Apple 文档：documentation/devicecheck】

即：token 的服务端可验证性，是 DeviceCheck 提供的全部「真实性」内容——它验证「这个 token 来自你 App 在一台 Apple 设备上的一次生成」，不验证、也不声称验证该设备的操作系统是否完好。

最后为第 5–6 章建立一个统一的阅读框架：在 Apple 公开文档的同一框架内，两项能力分别占据「状态通道」与「凭据通道」的位置。DeviceCheck bits 是一条**低容量的持久状态通道**——两个开发者自定义的布尔位，存于 Apple 服务端，读写均需一次性 token 过桥；App Attest 是一条**高保障的使用凭据通道**——SE 密钥、Apple 出具的证明、密钥签署的断言，服务端以密码学校验代替状态存储。两者都只输出「原料」，判定与处置均在开发者服务端完成；框架页把这一共同定位收束为一句原文（见 6.1 引用）。这个分工也解释了为什么第 5 章的争议集中在「状态的可变性」、第 6 章的争议集中在「凭据的可信范围」。

### 5.1 小结与取证边界

本节只确立了官方定位原文与「客户端取 token、服务端用 token」的两段形态。凡超出上述引文的定性（例如「DeviceCheck 能识别模拟器」「DeviceCheck 是设备指纹方案」）均无文档依据；`apsg.txt` 对本机制零命中这一负面结果，约束了后文不得将任何 DeviceCheck 论述挂到平台安全指南名下。


## 5.2 「两个比特位」的官方术语、字段与语义归属

「两个比特位」在 Apple 文档里没有单一名称，而是一组并存措辞，撰写与复核时必须按下列清单对齐，避免把第三方叫法当官方术语：

| 出现位置 | 官方原文措辞 | 说明 |
| --- | --- | --- |
| 框架页 | "two binary digits of data per device" | 抽象描述 |
| `DCDevice` 页 | "two per-device binary digits (bits)" | 类级描述 |
| 服务端指南 | "query and modify two per-device binary digits"、"persistent bits" | 强调可查可改、持久 |
| 服务端 JSON 请求/响应 | 字段名 `bit0` / `bit1` | 唯一落地的机器可读命名 |
| 响应字段说明 | "the first bit" / "the second bit" | 自然语言别名 |

【L1 Apple 文档：documentation/devicecheck、documentation/devicecheck/dcdevice、documentation/devicecheck/accessing-and-modifying-per-device-data】

需要显式记录的口径冲突是：第三方文章与 SDK 封装普遍写作 `bit1/bit2` 或 `deviceCheckBit1/2`，这与官方 JSON 字段名 `bit0`/`bit1` 不符；本报告一律采用官方字段名，并提醒下游读者在检索社区代码时做名称换算。

服务端接口的请求与响应字段全集如下（均为已核实条目）：请求侧包含 `device_token`（客户端生成的 token）、`transaction_id`、`timestamp`（"Long UTC timestamp from the associated server, in milliseconds since the Unix epoch"）、update 请求另有 `bits` 字典（内含 `bit0`、`bit1`）；query 响应返回 `bit0`、`bit1` 与 `last_update_time`，日期格式 `YYYY-MM`。【L1 Apple 文档：documentation/devicecheck/accessing-and-modifying-per-device-data】

比特的语义归属是这一机制最容易被误读的地方，Apple 在 `DCDevice` 页写得毫不含糊：

> "Apple records the bits for you, and reports the bits back to you, but you're responsible for keeping track of what the bits mean."
> 【L1 Apple 文档：documentation/devicecheck/dcdevice】

也就是说，Apple 服务端只承担「按设备存两个布尔值、并在被查询时原样返回」的存储职责；`bit0` 意味着「领过促销」、`bit1` 意味着「我方判定为欺诈」这类语义，完全是开发者单方面的约定，不随接口传递，也没有跨 App 的公共语义。框架页给出的两个官方用例（识别已薅过促销的设备、标记你已判定为欺诈的设备）也印证了这一点：其一是对业务历史的记录，其二是对开发者自有判定结果的**回写**——「被标记为欺诈」是开发者把结论存进去，而不是 Apple 把结论算出来。

由此得到本报告统一采用的位描述（后文与第 8 章均按此口径）：**每设备两个二进制位（服务端 JSON 字段 `bit0`/`bit1`），语义由开发者自定义，存储于 Apple 服务端，可由开发者服务端改写。** 由此也应当自然推论出责任分配：位的内容管理（何时置位、何时清零、清零后如何审计）属于业务侧，这与第 4 章把「阈值与处置」归入开发者责任的结论一致。至于「设备端能否清除比特位」，Apple 文档没有给出对应表述，属于 5.4 处理的缺口，不得反向补写。

### 5.2 小结与边界

本节未对「比特位能否被设备侧篡改」作出任何陈述——该问题在 Apple 公开文档中无对应条目，见 5.4。`last_update_time` 的更新粒度、两个 bit 的历史值是否可查，均属本次核查未获得可核实条目的事项。

## 5.3 `generateToken`：调用条件、一次性语义与重试窗口

`generateToken` 是 App 侧唯一的取凭据入口，Swift 声明为 `func generateToken(completionHandler: ((Data?, (any Error)?) -> Void)?`，另有 async 变体 `generateToken() async throws -> Data`。返回值的官方描述只有一句："An ephemeral token that identifies the current device."【L1 Apple 文档：documentation/devicecheck/dcdevice/generatetoken(completionhandler:)】

「ephemeral（临时）」这个词承担了两层语义，Apple 在 Discussion 里逐字写明：

> "Your server uses the generated token in its requests to get or set the persistent bits for the current device. You should treat the token you receive in the completion block as single-use. Although the token remains valid long enough for your server to retry a specific request if necessary, you should not use a token multiple times. Instead, use this method to generate a new token. The app you use to generate the token must be associated with your developer account; otherwise, the generation request fails."
> 【L1 Apple 文档：documentation/devicecheck/dcdevice/generatetoken(completionhandler:)】

拆解如下：

| 要素 | 官方口径 | 工程含义 |
| --- | --- | --- |
| 用途 | 服务端用它 query 或 set 当前设备的 persistent bits | token 是「设备侧生成、服务端消费」的桥 |
| 使用次数 | "treat the token … as single-use" | 每次服务端调用前应重新生成 |
| 存活窗口 | "remains valid long enough for your server to retry a specific request" | 允许对**同一请求**做一次重试，不允许多次消费 |
| 调用前提 | App 必须关联开发者账号；"your app must have an app ID that you register on the [Apple Developer] website" | 未注册的 App ID 调用直接失败 |

三点需要按原文边界来理解。第一，token「标识当前设备」不等于「暴露设备标识符」：它是每次新生成的临时值，DeviceCheck 的全部接口都不返回任何可跨请求、跨 App 稳定使用的设备 ID，这一点在 5.5 的政策讨论中还会用到。第二，「single-use + 短重试窗口」是 Apple 文档口径，但 token 的具体有效期数值（多少秒失效）**本次核查未获得可核实条目**，未公开。第三，调用前提是账号侧配置而非运行时能力：生成失败的直接原因是 App ID 未注册在开发者账号下，这与 5.6 讨论的运行期能力判定（`isSupported`）是两个层面的问题。

顺带核对一个易错口径：`DCDevice` 的入口是类属性 `current`（ObjC 侧为 `currentDevice`），`isSupported` 是只读属性而非方法；社区代码文章中写成 `isSupported()` 调用属于以讹传讹（详见第 7 章 7.2 对两个框架的统一澄清）。

### 5.3 小结与边界

本节的四个要素（用途、单用、重试窗口、账号前提）均有 L1 原文。未公开项：token 有效期秒数、同一设备的 token 生成频率限制、Apple 侧对 token 的存储时长——本次核查均未获得可核实条目，正文不作任何数值假设。

## 5.4 误解校正一：「两个 bit 不可重置」不成立

在中文与英文的二手资料里，DeviceCheck 常被概括为「Apple 在服务器端存了两个用户删不掉、重置不了的位」。这个说法在本次对 DeviceCheck 全部文档页的逐页核查中**找不到任何原文支撑**，而且与 Apple 的明文表述方向相反，属于本报告必须校正的第一条流行误解。

Apple 在 `DCDevice` 页把重置责任直接写给了开发者：

> "You're also responsible for determining when to reset the bits for a given device; for example, when a user sells the device to someone else."
> 【L1 Apple 文档：documentation/devicecheck/dcdevice】

配合服务端 update 请求本身就接受改写 `bit0`/`bit1`（见 5.2 的字段清单），官方口径的完整链条是：**比特位持久存于 Apple 服务端，改写与清零的权力与责任都在开发者的服务端**；Apple 甚至给出了清零时机的示例（设备易主）。换句话说，「不可重置」恰恰是 Apple 让开发者**不要**做的事——如果你从不重置，那两个位就会变成陈旧状态的来源，责任同样在你。

这条误解的来源可以定位到一次张冠李戴。Apple 文档中唯一带 "can't reset" 措辞的句子，说的是 **App Attest 的指标**，不是 DeviceCheck 的 bit：

> "The App Attest service records device metrics that you can't reset."
> 【L1 Apple 文档：documentation/devicecheck/preparing-to-use-the-app-attest-service】

两个句子的宾语是不同对象（bit vs metric）、不同机制（DeviceCheck vs App Attest）、不同责任方（开发者服务端可重置 vs 开发者不可重置）。二手文章把后一句的「不可重置」挪到前一句的主语上，就产生了「DeviceCheck 的 bit 不可重置」这一流行讹误。第 8 章讨论 risk metric 时会回到这句原文：「不可重置」在 Apple 口径里是一项**指标属性**，其用意是防止开发者用清零手段掩盖风险历史，而非某种设备侧的防篡改承诺。

同时必须按负面结果纪律处理另一半问题：本次核查在 DeviceCheck 全部页面中**未检索到**「设备端无法清除/篡改 bit」这样的表述——即 Apple 既没有说「不可重置」（那是误传），也没有正面写下「设备端绝对无法影响这两个位」。前者已可断言为不成立，后者只能写成缺口：「Apple 关于 DeviceCheck bit『设备端无法清除』的原文，本次核查未获得可核实条目」。据此，正确的报告口径是：**「两个 bit 由 Apple 服务端持久存储，语义与重置责任归开发者；Apple 未公开设备侧对这两个位的可见性与影响面。」**

### 5.4 小结与边界

本节是误解校正，不引入新事实断言。约束：不得反向写成「bit 完全不防篡改」；「不可重置」一语今后只能挂在 App Attest 指标上引用，且引用时必须附原句宾语（metrics），不得脱上下文转述。

## 5.5 误解校正二：DeviceCheck 文档没有「禁止设备指纹/追踪」条款

第二条需要校正的流行口径是「Apple 明令禁止把 DeviceCheck 用作设备指纹」。这个说法在合规讨论中经常被引用，仿佛 DeviceCheck 文档里有一条红字禁令；本次逐页核查的结果是：**该禁令不存在于 DeviceCheck 文档中**。「DeviceCheck 不应被当成跨 App 设备 ID 服务」是一个可以从官方语义**推出的正确结论**，但把它写成「Apple 一手口径」就是把推断冒充实证。

核查结果如下。DeviceCheck 框架页、`DCDevice`、`generateToken(completionHandler:)`、`accessing-and-modifying-per-device-data` 四篇文档的全文中，`fingerprint`、tracking 禁止类措辞、追踪限制条款均**零出现**；与该预期最接近的文字只有两处：框架页组成句中的 "while maintaining user privacy"，以及 5.3 所述 token 的 "ephemeral" 与 "single-use" 属性。这两处都是设计取向的描述，不是行为规范条款。

【L1 Apple 文档：documentation/devicecheck 及其上述四篇】

那么「能不能拿 DeviceCheck 做用户追踪/画像重建」这个问题上，真正可引的硬依据在哪里？在本次抓取的 App Store Review Guidelines（240,818 字节页面）中，`fingerprint` 一词同样是**零出现**，也没有任何针对 DeviceCheck 的专条；可落地的政策锚点是以下三条已核到原文的条款：

| 条款号 | 官方原文（节引） | 与 DeviceCheck 的关系 |
| --- | --- | --- |
| 5.1.2(iii) | "Apps should not attempt to surreptitiously build a user profile based on collected data and may not attempt, facilitate, or encourage others to identify anonymous users or reconstruct user profiles based on data collected from Apple-provided APIs…" | 唯一可作「禁止借 API 数据重建用户画像/识别匿名用户」硬依据的条款 |
| 5.1.2(i) | "You must receive explicit permission from users via the App Tracking Transparency APIs to track their activity." | 追踪类用途的授权前提（ATT），与 DeviceCheck 是否被使用无关地适用 |
| 2.5.1 | "Apps may only use public APIs and must run on the currently shipping OS." | 私有 API 条款的条目号；任何绕过公开接口的取标识行为按此条处理 |

【L2 App Store Review Guidelines：developer.apple.com/app-store/review/guidelines/，条目号与引文以抓取当日版本为准，条款可能修订】

因此本报告的合规口径应当这样组织：政策上约束「用任何 Apple 提供 API 的数据去识别匿名用户、重建画像」的是 **5.1.2(iii)**，约束「跨 App/跨网站追踪行为」的是 **5.1.2(i) 的 ATT 授权要求**，约束「手段合法性」的是 **2.5.1**；而 DeviceCheck 文档自身只提供了机制语义（两位状态、一次性 token、不返回标识符）。把「Apple 禁止用 DeviceCheck 做指纹」写成一手口径，属于给 Apple 安上一条它没有写过的条款；反过来，「DeviceCheck 没有禁令所以可以放心做指纹」的推论同样不成立——5.1.2(iii) 对包括 DeviceCheck token 在内的 API 数据一律适用。

还有一个机制层面的事实让「DeviceCheck=设备 ID」从根上就是误读：DeviceCheck 与 App Attest 两项能力**不返回任何标识符**。token 是临时单用值；`bit0`/`bit1` 只有查询方自己知道含义；App Attest 返回的是密钥标识符且密钥有意不跨重装存活（见 6.5）。凡报告或产品文档中出现「Apple 通过 DeviceCheck 提供设备唯一 ID」的表述，即为错误口径，需按本节校正。

### 5.5 小结与边界

本节的负面结果（DeviceCheck 文档与 Guidelines 均无 `fingerprint` 词）是「未检索到」级别的核查结论，写作时不得升格为「Apple 允许设备指纹」的反向断言；Apple 对该主题的规范意图只能经由 5.1.2(iii)、5.1.2(i)、2.5.1 三条间接确立，且 Guidelines 版本随时间修订。

## 5.6 可用性判定、服务端凭据形态与未公开边界

**平台门槛与运行期判定。** `DCDevice` 的文档平台元数据为：iOS 11.0 / iPadOS 11.0 / Mac Catalyst 13.1 / macOS 10.15 / tvOS 11.0 / visionOS 1.0 / watchOS 9.0 起【L1 Apple 文档：documentation/devicecheck/dcdevice 页元数据】。运行期能力判定用 `DCDevice.isSupported`，官方声明是只读属性 `var isSupported: Bool { get }`（ObjC `@property (readonly, getter=isSupported) BOOL supported;`），描述为 "A Boolean value that indicates whether the device supports the DeviceCheck API."【L1 Apple 文档：documentation/devicecheck/dcdevice/issupported】。与第 7 章将详述的 App Attest 同名属性相比，这一句的措辞对象是「支持 DeviceCheck API」，两者语义不可互换（E 节口径第 4 条）。

**entitlement 口径。** DeviceCheck 位图能力的全部文档页**未列出任何需要声明的 entitlement**（需要 entitlement 的是 App Attest 的环境选择，见 6.5）。此处严格区分两种写法：本报告记为「文档未见要求」，**不写成**「Apple 官方声明无需 entitlement」——后者在文档中不存在对应句。

**服务端凭据与端点。** 服务端调用共 query / update / device-validation 三类 POST；鉴权头为 JWT，官方原文要求 "The authentication key must use the ES256 algorithm and be in the Base 64 URL–encoded JSON web token format."，密钥来自 "the authentication key you receive from Apple in a JSON web token (JWT)"，创建流程复用 APNs provider token 的步骤【L1 Apple 文档：documentation/devicecheck/accessing-and-modifying-per-device-data】。基址为生产 `https://api.devicecheck.apple.com` 与开发 `https://api.development.devicecheck.apple.com`（后者官方限定 "only for testing during development"）。两环境不可混用。

**错误码表。** 服务端响应码是文档给出的唯一故障词典：

| HTTP 码 | Apple 附带语义 | 解读 |
| --- | --- | --- |
| 400 | Bad Device Token / Bad Bits / Bad Timestamp | token 失效或格式错、bit 值非法、服务端时钟偏移 |
| 401 | Invalid Authorization Token / Authorization Token Expired | JWT 密钥/签名问题或过期 |
| 403 | Forbidden："The specified action isn't allowed" | **具体触发条件未公开** |
| 404 | "The bit state wasn't found" | 该设备从未置位，bit 状态不存在 |
| 405 | Method Not Allowed | 方法/路径错误 |
| 429 | Too Many Requests | 被限流；上限数字未公开 |
| 500 / 503 | Server Error / Service Unavailable | Apple 侧故障；无 SLA 承诺 |

【L1 Apple 文档：documentation/devicecheck/accessing-and-modifying-per-device-data】

其中对风控工程最有信息量的是 404 的存在本身：它说明「该设备没有位状态」是一个可查询的正常返回值，位记录不是随设备出厂预置的。而 429/503 只告诉开发者「会发生限流与不可用」，不告诉开发者阈值在哪——DeviceCheck 服务端的 QPS/日配额、Apple 服务可用性与延迟承诺，均属「本次核查未获得可核实条目」。

**本机制的责任链条至此闭合。** 把 5.1–5.6 合起来读：App 侧生成一次性 token（前提：App ID 注册在开发者账号下）→ 服务端持 ES256 JWT 消费 token 读写两个位（前提：自建密钥托管与时钟同步）→ 位的语义、置位时机、重置时机全部由开发者定义与维护。DeviceCheck 提供的是「Apple 背书的一段按设备持久化的开发者自有状态」，而不是任何意义上的设备真伪鉴定书。

### 本章口径边界

第 5 章可用事实止于上述引文与字段表。以下各项在本章均为「本次核查未获得可核实条目」，不得据缺口反向断言：token 有效期数值；`403 Forbidden` 触发条件；`last_update_time` 更新粒度与 bit 历史可查性；DeviceCheck 服务端速率上限与 SLA；「设备端无法清除 bit」的原文；DeviceCheck 文档中任何指纹/追踪禁止条款。本章两处误解校正（5.4、5.5）已给出替代口径，下游章节引用 DeviceCheck 时必须采用校正后的表述。

---

# App Attest：密钥生成、证明与断言三段式

## 6.1 同框架内的两项并存能力：`DCAppAttestService` 的官方定位

App Attest 在 Apple 文档中的落点是 DeviceCheck 框架下的一个服务类，`DCAppAttestService` 页的 abstract 原文："A service that you use to validate the instance of your app running on a device."【L1 Apple 文档：documentation/devicecheck/dcappattestservice】注意宾语是 **app 的实例**（instance of your app），不是设备——这个措辞在第 7 章会升级为方法论结论：App Attest 验证的直接对象是「跑在这台设备上的这个 App 实例及其密钥」，「设备真实性」只是它的代理推论。

入口是类属性 `class var shared: DCAppAttestService`（ObjC `+sharedService`），官方一句话给出三段式骨架："Use the shared instance of the service to generate and to certify a cryptographic key, and then to assert your app's validity using that key."【L1 Apple 文档：documentation/devicecheck/dcappattestservice】即：生成密钥（generate）→ 请 Apple 证明密钥（certify/attest）→ 用密钥断言 App 有效性（assert）。三段分别对应 `generateKey(completionHandler:)`、`attestKey(_:clientDataHash:completionHandler:)`、`generateAssertion(_:clientDataHash:completionHandler:)` 三个方法，本章 6.2–6.4 逐一展开。

Apple 引入这项能力的动机，框架页给了完整的威胁模型原文：

> "Someone who modifies your app and distributes it outside the App Store can add unauthorized features like game cheats, ad removal, or access to premium content. The App Attest service gives your app a way to assert its validity so that your server can more confidently provide access to sensitive resources."
> 【L1 Apple 文档：documentation/devicecheck】

场景词以官方清单为准：`game cheats`、`ad removal`、`access to premium content`，加上 bits 侧的 `promotional offer` 与 `flag a device that you've determined to be fraudulent`。需要记录一项负面结果：DeviceCheck/App Attest 全部文档页中 `bot`、`automation`、`emulator`、`device farm`、`jailbreak` 均零命中，因此本报告描述对抗场景时只能使用「欺诈/作弊/内容盗取类」口径，不得把上述词汇伪装成 Apple 官方场景表述。

**误解校正（演进/替代口径）。** 社区叙事常把 App Attest 写成「DeviceCheck 的下一代/替代品」。核查结果不支持：其一，`DCAppAttestService` 与 `DCDevice` 同属 DeviceCheck 框架（文档标识 `doc://com.apple.DeviceCheck/documentation/DeviceCheck`），框架页 abstract 一句话同时涵盖两者，管理 device state 与 asserting app integrity 是并列职能；其二，本次抓取的 DeviceCheck 全部页面 JSON（框架页、`DCDevice` 及其成员、`DCAppAttestService` 及其 5 个成员、4 篇指南）中 `deprecated` / `deprecatedAt` 字段**零出现**，两项能力均未标记弃用；其三，Apple 文档中**没有任何一句**写明「App Attest 取代/演进自 DeviceCheck bits」或应优先选用其一——该叙述本次核查未获得可核实条目。Apple 实际给出的关系句是收束式的：

> "No single policy can eliminate all fraud. … Instead, the DeviceCheck services provide information that you can integrate into an overall risk assessment for a given device."
> 【L1 Apple 文档：documentation/devicecheck】

正确口径：**同框架下并存、互补的两项能力，共同作为开发者自建风险评估的输入**。引入版本差异（`DCDevice` iOS 11.0 起、`DCAppAttestService` iOS 14.0 起，相差三个大版本）解释了老设备只有 bits 可用的现实，但 Apple 未把它写成替代叙述。

| 维度 | DeviceCheck bits（第 5 章） | App Attest（本章） |
| --- | --- | --- |
| 验证对象 | 一次性 token 来自你 App 在 Apple 设备上的生成 | 设备上你 App 的实例与其 SE 密钥 |
| 载体 | Apple 服务端存的两个开发者自定义位 | SE 内密钥 + 证明/断言密码学对象 |
| 引入版本 | iOS 11.0 起 | iOS 14.0 起（watchOS 扩展 9.0 起） |
| entitlement | 文档未见要求 | `com.apple.developer.devicecheck.appattest-environment` |
| 服务端消费 | JWT 调 query/update REST | 校验 attestation/assertion 对象 + receipt 取指标 |

为便于第 6 章后文交叉引用，先把三段式在「何时调、用什么入参、产出什么、谁消费」四个维度上钉成一张总表（各单元格内容的原文依据在 6.2–6.4 逐节给出）：

| 阶段 | 方法 | 触发时机（官方口径） | 关键入参 | 产出 | 消费方 |
| --- | --- | --- | --- | --- | --- |
| 建钥 | `generateKey(completionHandler:)` | 新用户、新设备、重装/迁移/恢复之后 | 无 | `keyId`（密钥存于 SE） | App 与服务端立即记录 |
| 证明 | `attestKey(_:clientDataHash:completionHandler:)` | 建钥后随即一次（每密钥一次） | `keyId` + 服务端挑战的 SHA256（≥16B） | attestation object（authenticator data + Apple 证明语句 + receipt） | 服务端验链、验字段、登记公钥 |
| 断言 | `generateAssertion(_:clientDataHash:completionHandler:)` | 生命周期关键节点（如访问 premium content），次数不受限 | `keyId` + 本次客户端数据的 SHA256（≥16B） | assertion object（SE 私钥签名 + counter） | 服务端用已登记公钥验签、比挑战、查 counter |

【L1 Apple 文档：documentation/devicecheck/dcappattestservice 及其三个方法页、documentation/devicecheck/establishing-your-app-s-integrity、documentation/devicecheck/validating-apps-that-connect-to-your-server】

### 6.1 小结与边界

「并存非替代」在本章是三重核实的结果（同框架、零弃用标记、无替代叙述原文），但注意其性质是「文档未如此写」：不能据此断言 Apple 内部没有演进规划，只能断言公开一手口径不支持替代叙事。DCAppAttestService 的 macOS/visionOS 等平台元数据在本次核查中未按页逐项展开，涉及平台面时以第 7 章的 `isSupported` 与扩展口径为准。

## 6.2 `generateKey`：密钥生成、`keyId` 与按用户建钥约束

`generateKey(completionHandler:)` 的官方摘要："Creates a new cryptographic key for use with the App Attest service."，成功回调返回 `keyId: String`，其官方定义一句话包含两个机制事实："An identifier that you use to refer to the key. The framework securely stores the key in the Secure Enclave."【L1 Apple 文档：documentation/devicecheck/dcappattestservice/generatekey(completionhandler:)】——**密钥本体进 Secure Enclave，App 拿到的只是句柄型标识符**。第 7 章 7.1 将专门处理这句的原文地位，本节先把它的工程后果讲完。

因为私钥不可被任何进程读取，`keyId` 就成了唯一的操作入口，Apple 对丢失后果的原文同样是明写："Because there's no way to use or retrieve the key without the identifier, you'll want to either record it in your app or on your server right away."【L1 Apple 文档：同上】这决定了集成侧的存储义务：`keyId` 必须在生成后立即持久化（App 侧或服务端侧至少一处），且服务端必须持久化 `keyId` 与公钥的配对记录——公钥会在证明对象中回传（见 6.3），此后每次断言的验签都依赖这份配对。

建钥粒度是 Apple 少见的直接下达的工程规范，原文两句："Call this method to request the creation of a secure, unattested key pair on a device for a specific user." 与 "Create a unique key for each user account on a device."【L1 Apple 文档：documentation/devicecheck/dcappattestservice/generatekey(completionhandler:)】注意定语 "unattested"（未经证明的）——刚生成的密钥只是一个 SE 内的密钥对，尚未获得 Apple 的背书，背书发生在下一步 `attestKey`。反向约束同样有原文："Don't reuse a key among multiple users on a device because this weakens security protections."【L1 Apple 文档：documentation/devicecheck/establishing-your-app-s-integrity】设备共用多账号（家庭共享 iPad、企业多用户场景）时，一账号一密钥不是建议而是安全属性的前提，因为 risk metric 与后续处置推断全部建立在「密钥↔用户↔设备」三元对应关系上。App Clip 场景的口径是 Clip 与主 App 共用同一密钥，且 "store the identifier in a shared container accessible from your full app."【L1 Apple 文档：documentation/devicecheck/establishing-your-app-s-integrity】

生成时机上，Apple 把建钥频率本身也变成了风控变量："Try to limit new key generation to only these events, or to the addition of new users. Keeping the key count low on a device helps when trying to detect certain kinds of fraud."【L1 Apple 文档：documentation/devicecheck/assessing-fraud-risk】哪些事件构成正当的重新建钥时机（重装、迁移、备份恢复），在 6.5 展开；该句与第 8 章 risk metric 的「正当增长应保持在低位」互为印证——**客户端建钥纪律直接决定服务端指标的可用性**。

失败路径方面，`generateKey` 的错误以 `DCError` 域返回（全集 5 个 case 见 6.6），其中与建钥直接相关的是 `featureUnsupported`（"DeviceCheck is unavailable on this device."，设备不支持）与 `unknownSystemFailure`。另有一条平台限制在此只做预告并在第 7 章详述：从 app extension 调用 `generateKey` 的成败判定在 Apple 两处文档中口径不一致。

### 6.2 小结与边界

本节的建钥粒度、`keyId` 存储义务、共用禁令、App Clip 共用口径均有 L1 原文。未公开项：单设备可存的 App Attest 密钥数量上限——Apple 文档未给出，本次核查未获得可核实条目；密钥的具体算法参数见 6.6。

## 6.3 `attestKey`：证明对象的字段构成与挑战约束

`attestKey(_:clientDataHash:completionHandler:)` 的官方摘要："Asks Apple to attest to the validity of a generated cryptographic key."——请 Apple 对一个已生成密钥的有效性出具证明。入参 `clientDataHash` 的定义："A SHA256 hash of a unique, single-use data block that embeds a challenge from your server. Should be at least 16 bytes in length."；出参 `attestationObject` 的定义："A statement from Apple about the validity of the key associated with `keyId`. Send this to your server for processing."【L1 Apple 文档：documentation/devicecheck/dcappattestservice/attestkey(_:clientdatahash:completionhandler:)】三个要点已在此句中锁死：挑战由**服务端**发起、客户端只做哈希、证明对象的消费方是**你自己的服务器**。

**证明对象的结构。** 服务端指南原文：attestation object "consists of authenticator data and an attestation statement according to the [WebAuthn] specification."【L1 Apple 文档：documentation/devicecheck/validating-apps-that-connect-to-your-server】即 Apple 借用了 WebAuthn 的容器格式，但字段语义为 App Attest 专用。authenticator data 的官方字段清单如下：

| 字段 | 长度 | 官方语义 |
| --- | --- | --- |
| `RP ID` | 32B | App ID 的哈希；App ID = 10 位 Team ID + 句点 + `CFBundleIdentifier`；macOS 上以 TN3127 的 code signing requirement 取代 bundle ID |
| `counter` | 4B | "reports the number of times your app has used the attested key to sign an assertion"；**在证明对象中必须为 0** |
| `aaguid` | 16B | App Attest 专用常量，标记环境：`appattestdevelop` 或 `appattest`+7 个 `0x00`（development/production） |
| `credentialId length` | 2B | 下一字段长度 |
| `credentialId` | 32B | 公钥部分的哈希 |
| `Encoded key` | 77B | COSE 格式公钥 |
| `extensions` | 变长 | 含 `apple_validation_category_01`（UInt32，签名身份类别）与 `apple_bundle_version_01`（String，分发 App 版本号） |

【L1 Apple 文档：documentation/devicecheck/validating-apps-that-connect-to-your-server】

**关于「证明里有没有 App ID / 团队 ID」这一常见疑问，核实结果是限定式成立**：证明内容确实包含 App ID 的哈希（`RP ID` 字段），Team ID 作为 App ID 的**前缀被一并哈希进去**，以「被哈希的 App ID 组成部分」这一间接形式出现；**不存在一个独立的明文 Team ID 字段**。服务端校验动作是比对 `RP ID` 是否等于己方 App ID 的哈希。另注意拼写口径：Apple 文档写作 `RP ID`（含空格），WebAuthn 标准与多数第三方校验库写作 `rpIdHash`，同一字段两种名字，检索实现时应并列。`aaguid` 用环境标记、`counter` 在证明时必须为 0，都是服务端验证明时的硬校验点；`apple_validation_category_01` 报告的是签名身份类别（0–10 档，逐档原文表见本篇对抗面相关章节的处理范围），Apple 未承诺「重签名必落某一档」，服务端只能据此制定自己的策略。

**证书链与 nonce 绑定。** 原文："Verify that the `x5c` array contains the intermediate and leaf certificates for App Attest, starting from the credential certificate in the first data buffer in the array (`credcert`). Verify the validity of the certificates using Apple's [certificate authority]."【L1 Apple 文档：同上】链序为 credcert（leaf）→ 中间证书 → Apple 根；根证书需从 Apple 私有 CA 仓库（www.apple.com/certificateauthority/private/）取得并长期信任（运维项见 8.4）。本次提交的 nonce 绑定在 `credCert` 的扩展 OID `1.2.840.113635.100.8.2` 中；macOS 上另有 `aclBlob` OID `1.2.840.113635.100.8.6`。

**挑战与随机数的服务端义务。** Apple 对服务端写了两条正面要求：为每次证明/断言 "provide a randomized data value, and remember the value for use when verifying the corresponding attestation or assertion objects the client sends"，且每次都是 "a unique, one-time challenge"；长度理由也给了原文："should be at least 16 bytes in length to ensure sufficient entropy to ensure guessing them is infeasible."【L1 Apple 文档：documentation/devicecheck/validating-apps-that-connect-to-your-server、establishing-your-app-s-integrity】换言之，challenge 不可复用、不可由客户端自备、服务端必须保存待比对（断言校验时 "Verify that the embedded challenge in the client data matches the earlier challenge to the client."）。这是一台无状态服务做不了 App Attest 校验端的根本原因。

**失败语义分两类，处置方向相反。** 其一，服务端验证明失败："If your server fails to verify the attestation object, discard the key identifier."——丢弃 keyId，重新走建钥。其二，Apple 服务联系失败（`DCError.serverUnavailable`，官方定义 "An error that indicates a failed attempt to contact the App Attest service during an attestation."）时**不要**新建密钥："retry attestation again using the same key and client data hash later to avoid unnecessarily generating new keys. Retrying with the same inputs helps to preserve the risk metric for a given device."【L1 Apple 文档：documentation/devicecheck/dcappattestservice/attestkey(_:clientdatahash:completionhandler:)】这半句顺带暴露了 risk metric 的一个性质：**同 key 同输入的重试不推高指标**，即该计数统计的是密钥维度的证明事件而非请求次数（计数的完整语义见 8.1–8.2）。

### 6.3 小结与边界

证明对象字段表、证书链序、nonce OID、挑战三约束、两类失败处置均有 L1 原文。未公开项：曲线与密钥算法参数（见 6.6）；Apple 服务端对同一 key 重复 attestation 的次数限制；nonce 若不做服务端记录的官方容错建议——Apple 只写了「remember the value」，未给存储方案。

## 6.4 `generateAssertion`：官方对「证明真实性」与「证明某次使用」的区分

`generateAssertion(_:clientDataHash:completionHandler:)` 的官方摘要："Creates a block of data that demonstrates the legitimacy of an instance of your app running on a device."【L1 Apple 文档：documentation/devicecheck/dcappattestservice/generateassertion(_:clientdatahash:completionhandler:)】与 `attestKey` 表面相似（同样收 `clientDataHash`、同样产出密码学对象），语义上却处在证明链的两个不同环节。Apple 自己的区分措辞分布在三处，逐字列出：

| 环节 | Apple 原文 | 中文口径 |
| --- | --- | --- |
| 证明（attestation） | "Asks Apple to attest to the validity of a generated cryptographic key." | Apple 为一个**密钥**的来源与有效性作保 |
| 断言（assertion） | "Each verified assertion reestablishes the legitimacy of the client." | 每次被验证的断言**重新确立**这次请求的客户端合法性 |
| 框架总纲 | "You use the `DCAppAttestService` class to generate a special cryptographic key on the device, and have Apple attest to the validity of that key. You then use that key to assert the validity of your app whenever you request sensitive data from your server." | 一次证明，多次断言；断言是密钥的持续使用凭证 |

【L1 Apple 文档：documentation/devicecheck/dcappattestservice/attestkey(_:clientdatahash:completionhandler:)、documentation/devicecheck/validating-apps-that-connect-to-your-server、documentation/devicecheck】

因此「attestation 证明真实性、asserting 证明某次使用」这个区分在 Apple 一手口径里**确实存在且有原文**，本报告中文固定译法：attestation=证明、assertion=断言（一次使用之证明），首次出现保留英文。社区还把 assertion 混译为「声明」「凭据」，并常从 WebAuthn 借来 `claim` 一词——Apple 文档本身不使用 claim，属需并列的口径冲突。

断言的运行条件与证明不同之处值得逐条核对。第一，**入参定义换了宾语**：`generateAssertion` 的 `clientDataHash` 官方定义改为 "A SHA256 hash of a unique, single-use data block that represents the client data to be signed with the attested private key. Should be at least 16 bytes in length."——被签的是本次请求的客户端数据，用**已证明过的私钥**签名，而不再需要 nonce。第二，**频次不受限**："There's no restriction on the number of assertions that you can make with a key."【L1 Apple 文档：documentation/devicecheck/establishing-your-app-s-integrity】第三，**使用时机的官方建议**是生命周期关键点："you can use the key at critical moments in your app's life cycle — like when a user tries to access premium content — to reaffirm the legitimacy of a given instance of your app."【L1 Apple 文档：同上】即 Apple 不要求每次请求都断言，而是把断言设计为对「敏感资源发放」这类决策点的复核手段。

服务端消费断言对象的核验三件套与证明对称：用首次证明时存储的公钥验签；"Verify that the embedded challenge in the client data matches the earlier challenge to the client."；以及计数器单调性——"Verify that the authenticator data's `counter` value is greater than the value from the previous assertion, or greater than `0` on the first assertion."，并要求 "Store `counter` to use in step 5 when verifying the next assertion."【L1 Apple 文档：documentation/devicecheck/validating-apps-that-connect-to-your-server】`counter` 在证明对象中恒为 0、随每次断言递增（语义即 6.3 表中 "the number of times your app has used the attested key to sign an assertion"），因此它承担的是**防重放与使用连续性**的检查，而不是活跃度统计。

失败处置的官方口径再次落在开发者身上，且比 6.3 更直白："If the assertion object fails verification, it's your responsibility to decide how to handle the request."【L1 Apple 文档：documentation/devicecheck/validating-apps-that-connect-to-your-server】Apple 把「验不过来的断言是拒绝、降权、还是二次核实」完全留给业务侧——这与第 4 章的责任分配口径一致，本章只是把它落到具体调用点上。

### 6.4 小结与边界

本节全部区分句均为 L1 原文，可直接引用。约束两条：其一，`counter` 的官方语义是「该密钥签名断言的次数」，不要引申为「用户活跃证据」；其二，「断言次数不受限」不等于「Apple 承诺断言接口无限流」——断言走的是 App 本地 SE 签名，仅 challenge 生成与校验在己方服务端，Apple 侧对断言本身没有公开速率口径（本次核查未获得可核实条目）。

## 6.5 密钥失效条件、环境 entitlement 与重新证明流

App Attest 密钥的存活边界是设计出来的，Apple 在 `establishing-your-app-s-integrity` 页给了完整原文："The keys that you generate remain valid through regular app updates, but don't survive app reinstallation, device migration, or restoration of a device from a backup. In these cases, you need to start the process from the beginning and generate a new key."【L1 Apple 文档：documentation/devicecheck/establishing-your-app-s-integrity】即：**常规更新不清钥；卸载重装、设备迁移、从备份恢复三种事件必清钥**，且清理后必须整段重走「建钥→证明→登记」流程。

为什么 Apple 要让密钥主动「死掉」？`assessing-fraud-risk` 页把动机写明："For privacy reasons, App Attest keys stored on device don't survive these events, forcing your app to generate a new key on the same device."【L1 Apple 文档：documentation/devicecheck/assessing-fraud-risk】注意这个设计的双向后果。隐私方向上：密钥有意不跨重装存活，意味着 App Attest 机制**不能被用作跨设备、跨用户的稳定设备 ID**——它与 5.5 的结论（两项能力均不返回标识符）拼合后，「Apple 通过 App Attest 提供设备唯一 ID」这一说法的谬误就完整了。指标方向上：每一次正当的重装/恢复/转让都会在 risk metric 上留下增量（见 8.2），Apple 宁可让指标被正当行为推高，也不给出一个可长期追踪的标识符——这是理解该机制一切口径时应当放在背景里的取舍。

服务端为这套失效模型要付出的存储代价同样是原文要求："Be prepared to store multiple (key, receipt) pairs for each user. Store one pair for each device the user uses… keep development pairs separate from production pairs."【L1 Apple 文档：documentation/devicecheck/validating-apps-that-connect-to-your-server】一个用户多设备、每设备多轮重建、开发环境与生产环境分账，三件事叠加后「(用户, 设备, 代次, 环境) → (keyId, 公钥, receipt)」是服务端必须自建的状态表，Apple 不提供任何托管（见 8.4）。缓解手段回到 6.2 的建钥纪律："Try to limit new key generation to only these events, or to the addition of new users."——把非必要的重新建钥压下去，既省状态表，也保住指标的信噪比。

**环境选择由 entitlement 承担**：`com.apple.developer.devicecheck.appattest-environment`，取值 `development` / `production`。官方规则三句："If you omit the entitlement during development, your app uses the App Attest sandbox servers by default."；"After distributing your app through TestFlight, the App Store, or the Apple Developer Enterprise Program, your app ignores the entitlement you set and uses the production environment."；沙箱与生产的密钥、收据不可互换（"You can't use a receipt generated in one environment to request a new receipt in the other."）。【L1 Apple 文档：documentation/bundleresources/entitlements/com.apple.developer.devicecheck.appattest-environment、documentation/devicecheck/assessing-fraud-risk】也就是说环境在分发那一刻被硬切换，开发期靠 entitlement、分发后靠签名渠道，二者不存在「运行时协商」通道。App Clip 须为完整 App 与 Clip **都**声明该能力，app extension 需单独配置（扩展支持的口径冲突见第 7 章 7.3）。

一个术语层面的坑需要记录：环境标记在 Apple 生态里有三套互不相通的词汇——App Attest 证明对象里用 `aaguid`（`appattestdevelop` / `appattest`+7×`0x00`）表示环境，entitlement 与叙述文字用 `development`/`production` 字样，MDM 的 Managed Device Attestation 则完全没有对应概念（用 `DeviceAttestationNonce`/freshness code 表示新鲜度）。三者不可互换引用，也不可将 MDM attestation 与 App Attest 合并成「苹果设备证明」一个筐——前者信任根是 Apple Enterprise Attestation Root CA、面向受管设备，后者信任根是 App Attest 证书链、面向 App 服务端，机制与证据链各自独立。

### 6.5 小结与边界

失效三事件、隐私动机、多对存储义务、entitlement 三规则均为 L1 原文。未公开项：SE 内 App Attest 密钥的数量上限；密钥被清除时设备侧是否存在任何可查询痕迹——本次核查均未获得可核实条目，「重装后 Apple 是否记得旧密钥」这类问题只能作为缺口对待。

## 6.6 误解校正：`seKey` 是第三方术语；曲线与密钥长度未公开

**校正一：`seKey` 不是 Apple 术语。** 部分中文技术文章与示例代码把 App Attest 生成的密钥称为 `seKey`，读起来像 Apple 的官方命名。本次核查在 DeviceCheck 全部文档页文本中检索 `seKey`，**零命中**。Apple 的口径始终是：`generateKey` 返回一个 `keyId: String`，它是"an identifier that you use to refer to the key"，密钥对本体"The framework securely stores … in the Secure Enclave"，成功时"the method provides your app with an identifier that represents the key pair stored in the Secure Enclave"【L1 Apple 文档：documentation/devicecheck/dcappattestservice/generatekey(completionhandler:)】。因此正文的准确写法是：**Apple 文档称其为存于 Secure Enclave 的密钥对及其标识符（`keyId`）；社区另有 `seKey` 的叫法，属第三方术语**。还需防两类撞名：检索到的 `sekey` 一名同时指向 GitHub 上的 Secure Enclave SSH 项目与学术界名为 SEKEY 的密钥管理方案，都与 App Attest 无关（等级 L3，不可当作 Apple 口径）。该词收进本篇口径冲突表述清单，供术语表引用。

**校正二：曲线与密钥长度未公开。** 高频流传的说法是「App Attest 密钥为 P-256/ECDSA」。核查结果：上述全部文档页文本中 `crv`、`P-256`、`alg` 均未出现；Apple 明文给出的只有三个尺寸事实——`Encoded key` 为 **77 字节 COSE 格式公钥**（6.3 字段表）、公钥点格式为 X9.62 uncompressed point format、`clientDataHash` 输入的挑战数据 **≥16 字节**。【L1 Apple 文档：documentation/devicecheck/validating-apps-that-connect-to-your-server】77 字节的 COSE EC2 公钥结构与 P-256 兼容是工程读者的合理联想，但**本次核查未获得可核实条目支持「官方规定曲线为 P-256」**，正文不得写成 Apple 口径，只能写「Apple 未公开曲线名与算法标识，仅给出 77 字节 COSE 公钥」。

**配套事实：客户端错误码全集只有 5 个粗粒度 case**，列全如下【L1 Apple 文档：documentation/devicecheck/dcerror-swift.struct/code】：

| `DCError` case | 官方描述 | 可用用途 |
| --- | --- | --- |
| `featureUnsupported` | "DeviceCheck is unavailable on this device." | 能力判定，转降级路径 |
| `invalidInput` | "An error code that indicates when your app provides data that isn't formatted correctly." | 入参自检 |
| `invalidKey` | "An error caused by a failed attempt to use the App Attest key." | 触发重新建钥/证明 |
| `serverUnavailable` | "An error that indicates a failed attempt to contact the App Attest service during an attestation." | 同参重试（见 6.3） |
| `unknownSystemFailure` | "A failure has occurred, such as the failure to generate a token." | 兜底重试 |

错误域常量 `DCErrorDomain`；ObjC 侧为 `DCError : NSInteger`。这张表本身就是一个小结论：**五个 case 中不存在任何「检测到篡改/越狱」类错误码**，客户端错误按 Apple 口径只能用作重试与流程分支依据，不能被包装成风险判定信号上报服务端充当证据。

最后统一一组术语用词：Apple 在真实性语义上交替使用 "authenticity"（"Your app uses the App Attest service to assert its authenticity."）、"validity"、"legitimacy"、"integrity" 四个英文词，**没有** "device authenticity" 这一固定术语；本篇标题所称「设备真实性」需在术语表中映射回这四个原文词，并始终携带「以 App/密钥真实性为代理指标」的限定（E 节口径第 6 条）。

### 本章口径边界

第 6 章的三段式流程、字段表、区分句、失效条件、entitlement 规则均有 L1 原文支撑。本章留出的缺口：曲线/算法标识、`seKey` 之外的任何 SE 内部实现描述、Apple 服务端对同一 key 重复证明的次数限制、断言接口的 Apple 侧速率口径——一律为「本次核查未获得可核实条目」；凡社区文本对上述项给出「官方规定」式表述，均按本章两条误解校正处理。

---

# Secure Enclave 依赖与可用性边界

## 7.1 私钥驻留 Secure Enclave 的一手原文与依据等级

App Attest 对 Secure Enclave 的依赖不是推断，而是两处 L1 原文明写的机制事实。第一处在建钥返回值定义处：

> "An identifier that you use to refer to the key. The framework securely stores the key in the Secure Enclave."
> 同页 Discussion："On success, the method provides your app with an identifier that represents the key pair stored in the Secure Enclave. Because there's no way to use or retrieve the key without the identifier, you'll want to either record it in your app or on your server right away."
> 【L1 Apple 文档：documentation/devicecheck/dcappattestservice/generatekey(completionhandler:)】

第二处在 App 侧集成指南，也是本章标题句的出处：

> "The device automatically stores the associated private key in the Secure Enclave, from where the App Attest service can use it to create signatures, but from where no process can ever directly read or modify it, ensuring its security."
> 【L1 Apple 文档：documentation/devicecheck/establishing-your-app-s-integrity】

"**no process can ever directly read or modify it**"是 Apple 原话，可以逐字引用。同时要做一项措辞级别的校正（第 4 类误解）：中文资料普遍把这一性质转述为「密钥不可导出（non-exportable）」。Apple 原文用的动词是 **read or modify**——不可被任何进程直接读取或修改；「不可导出」是对该句的合理转述而非原文，撰写时应保留原文措辞，把「不可导出」作为译述标注。这一字之差在威胁建模上有实际意义：「不可读取/修改」约束的是宿主进程空间内的操作，而「任何条件下不可导出」是一个更强的硬件攻击命题，后者 Apple 并未书面承诺。

侧证一条，注意出处边界。Apple Platform Security Guide（August 2026 版，本地 `apsg.txt`，"The Secure Enclave / Overview" 段）原文："The Secure Enclave is isolated from the main processor to provide an extra layer of security and is designed to keep sensitive user data secure even when the Application Processor kernel becomes compromised."【L1 Apple Platform Security（通用 SE 论述）】这句话支撑「宿主内核被攻破时 SE 内数据仍受设计性保护」的意图叙述，比任何第三方转述都硬。但必须同时声明：该指南全文对 DeviceCheck/App Attest 零命中（5.1 已述），所以此句只能用于解释 SE 的通用隔离设计，**不得**写成「平台安全指南论述了 App Attest 的密钥驻留」——App Attest 的驻留依据只在上引两处 developer.apple.com 原文。

依据等级因此可以定标：「私钥驻留 SE」「任何进程不可直接读取或修改」为 **L1 原文级**；「SE 硬件在物理/侧信道攻击下不可提取」为 **Apple 未公开**（本次核查未获得可核实条目，既不承诺也不否认）；SE 内部的密钥层级、与 Keychain 的关系属于其他篇目的范围（详见《iOS 核心底层技术支撑》《iOS 钥匙串窃取》相关章），本章仅为「App Attest 私钥的驻留位置与不可读改性」这一定位引用 SE，不重述其结构。

### 7.1 小结与边界

本节确立的是本章唯一的「硬件依赖」事实及其等级。后两节（7.2–7.3）处理的可用性判定同样有 L1 原文，但口径一致性明显更差，需要并列而非择一。

## 7.2 `isSupported` 的语义：只读属性，两个框架两种措辞

两个框架各有一个 `isSupported`，且**都是只读属性而非方法**——这是社区文章错误率最高的一处 API 口径，值得用声明原文钉死：

| 框架 | 声明 | ObjC 形态 | 官方描述原文 |
| --- | --- | --- | --- |
| DeviceCheck bits | `var isSupported: Bool { get }`（`DCDevice` 实例属性） | `@property (readonly, getter=isSupported) BOOL supported;` | "A Boolean value that indicates whether the device supports the DeviceCheck API." |
| App Attest | `class var isSupported: Bool { get }`（`DCAppAttestService` 类属性） | `@property (class, readonly, getter=isSupported) BOOL supported;` | "A Boolean value that indicates whether a particular device provides the App Attest service." |

【L1 Apple 文档：documentation/devicecheck/dcdevice/issupported、documentation/devicecheck/dcappattestservice/issupported】

App Attest 侧的用法原文："You check for availability by reading the `isSupported` property."【L1 Apple 文档：documentation/devicecheck/establishing-your-app-s-integrity】——"reading … property"，动词是「读取」。二手文章把调用写成 `isSupported()`，是把属性误当方法；ObjC 侧还有一层易错点：键名是 `supported`，`isSupported` 只是 getter 名。顺带说明，Apple 自己的行文也不完全统一（7.3 将引用的 Important 框里出现 "the `isSupported` method property" 这种自相矛盾的写法，属其措辞瑕疵），但**声明与「reading the property」的口径是明确的：属性**。

两个框架的描述句存在刻意的语义差：DeviceCheck 问的是「这台设备支持 DeviceCheck API 吗」，App Attest 问的是「这台设备**提供 App Attest 服务**吗」。后者把主语从 API 挪到了 service，配合 "Not all devices can use the App Attest service"（7.4 引用），含义是该能力受设备条件约束、而非仅受系统版本约束。两个布尔值不可互相代入：`DCDevice.isSupported == true` 不蕴含 `DCAppAttestService.isSupported == true`（前者 iOS 11 起即可为真，后者 iOS 14 起且另有设备门槛），本报告在可用性叙述中始终分开引用。

App Attest 的 `isSupported` 页还给出一个跨平台硬事实："If you read from an app running on a Mac device, the value is `false`. This includes Mac Catalyst apps, and iOS or iPadOS apps running on Apple silicon."【L1 Apple 文档：documentation/devicecheck/dcappattestservice/issupported】即 Mac 原生、Mac Catalyst、Apple silicon 上运行的 iOS/iPadOS App 三类场景一律返回 false——这与 5.6 记录的 `DCDevice` 平台元数据（macOS 10.15 / Mac Catalyst 13.1 在列）形成对照：**bits 侧文档列出了 macOS 平台，Attest 侧则明写 Mac 读值为 false**。两项机制的平台面并不重合，合并成「DeviceCheck 全家桶支持 macOS」是错误口径。

### 7.2 小结与边界

本节的属性/方法之辨、两框架描述差、Mac 恒 false 三条均为 L1 原文。`isSupported` 为 false 的具体成因清单（哪些机型、哪些配置）Apple 未提供，属 7.5 的缺口；该属性也不携带任何失败原因信息——它只有一个布尔值。

## 7.3 口径冲突并列：Apple 两处文档在扩展支持上互相矛盾

第 4 类需要处理的问题不是社区误读，而是 **Apple 文档自身的内部矛盾**：同一批 DeviceCheck 文档中，两处关于「哪些 app extension 能用 App Attest」的原文互不相容。按纪律，两处原文必须并列给出，不得择一掩盖。

**甲方口径**——`establishing-your-app-s-integrity` 页 Important 提示框，逐字：

> "Action, extensible SSO, and watchOS extensions are supported. All other extension types are not supported, even if the `isSupported` method property is `true`."
> 【L1 Apple 文档：documentation/devicecheck/establishing-your-app-s-integrity】

**乙方口径**——`DCAppAttestService.isSupported` 页 Discussion，逐字：

> "The `generateKey` method fails when you call it from an app extension, regardless of the value of `isSupported`. The only app extensions that support App Attest are watchOS extensions in watchOS 9 or later."
> 【L1 Apple 文档：documentation/devicecheck/dcappattestservice/issupported】

两处直接冲突：甲方承认 Action 扩展与 extensible SSO 扩展两类可用；乙方声称**唯一**可用的扩展是 watchOS 9 或更新的扩展，并且 `generateKey` 在**任意** app extension 中一律失败——若乙方为真，甲方的 Action/extensible SSO 支持便无从谈起。两处还共享一个奇怪的细节：甲方那句把 `isSupported` 写作 "method property"（7.2 已述其措辞瑕疵属性），乙方则明确称 `generateKey` 为 method。本次核查对两段引文逐 token 复核，确认均属实文，不存在抓取失真；这是简报层面明确记录的「Apple 文档内部口径不一致」条目（F-2）。

本报告不替 Apple 裁决该矛盾，采用如下**收口写法**：其一，两处原文并列为唯一安全口径，任何引用其中单独一处并当作 Apple 完整立场的文章（含本报告下游章节、产品文档）都不合规；其二，工程测试基线应取两口径的**交集**——watchOS 9+ 扩展是双方一致承认的支持面，Action 与 extensible SSO 扩展属于「一处承认、一处否认」的争议面，接入方须以自有设备实测行为为准，并理解实测结果不构成对 Apple 口径的确认；其三，统一的支持矩阵在本次核查中**不存在**：App Clip、各类扩展、watchOS 之外的平台对 App Attest 的支持情况，Apple 文档只有零散表述，未获得可核实条目支持任何汇总表（D-14）。可以确认的一致点只有一条：两处都以「`isSupported` 为 true 不代表扩展环境可用」为前提——能力判定与调用位置合法性是两道独立的门。

### 7.3 小结与边界

本节的产出是「冲突登记表」性质的：口径冲突表述一节需收录 F-2 全文并列。约束：不得以其中一处原文推出「iOS App 的某扩展里可以放心调用 App Attest」，也不得反向写「App Attest 在所有扩展中均不可用」——后者超出两处原文的并集所能支撑的范围。

## 7.4 不支持设备的后果：官方要求的「降级放行」与服务端处置

设备不提供 App Attest 服务时，Apple 给出的官方指令只有一条，且 App 侧与服务端侧各表述了一次。App 侧原文：

> "Not all devices can use the App Attest service, so it's important to have your app run a compatibility check before accessing the service. If the user's app doesn't pass the compatibility check, gracefully bypass the service."
> 【L1 Apple 文档：documentation/devicecheck/establishing-your-app-s-integrity】

服务端侧原文：

> "You change the behavior of both your app … and your server — which can no longer require assertions — when you find that the device doesn't support the service."
> 【L1 Apple 文档：documentation/devicecheck/validating-apps-that-connect-to-your-server】

两句话合起来的官方要求是明确的：**兼容性检查前置于服务调用；一旦判定不支持，服务端就「不能再把断言作为必要条件」**。注意 Apple 用词的强度——"can no longer require assertions" 是陈述式规则而不是建议选项：在这条能力线上，Apple 自己规定了降级放行路径，即不支持的设备走一条不要求断言的服务通道。同时必须看到这条指令的另一面：Apple **没有**给出「不支持设备上该如何替代判定」的任何规则——补什么信号、降级通道要不要加人工审核、风险权重怎么设，全部空白。按第 4 章的责任分配框架，这块空白归开发者：处置规则留给业务侧自行设计，本报告按同一口径落笔，不虚构 Apple 立场。

由此可以量化一项对防线设计至关重要的结论：**App Attest 的可用性面在文档上是逐层收窄的**——iOS 14.0 起才有 API（6.1 版本元数据）；`isSupported` 在 Mac 原生、Mac Catalyst、Apple silicon 跑 iOS App 三类场景恒为 false（7.2）；扩展环境存在未决矛盾且 `isSupported==true` 也不担保扩展可调用（7.3）；机型层面连「哪些设备会返回 false」的枚举都没有（7.5）。每一层收窄都意味着一部分真实用户被官方指令推入「无断言」通道。因此凡以 App Attest 为**唯一**服务端防线的方案，其覆盖面按 Apple 自己的文档就必然存在盲区；合规的设计口径是把「无断言通道」当作风险评估里一个**显式的、单独赋权的类别**来对待——它在体系上等价于「该请求没有这项证据」，而不是「该请求证据为零分」，两者在处置上的差别由业务按第 4 章的阈值责任自行标定。Apple 未对降级通道提供任何判定输入或建议权重（本次核查未获得可核实条目）。

bits 侧的对应物简单一档：DeviceCheck 的能力判定就是 `DCDevice.isSupported` 一个布尔值（7.2），运行期不支持时相关调用以 `DCError.featureUnsupported`（"DeviceCheck is unavailable on this device."）报错；同样地，Apple 未公开哪些设备/配置会走到这个分支。两套机制在「不支持即降级、降级即无证据」这一点上共享同一处置逻辑。

### 7.4 小结与边界

「降级放行」是 Apple 明文要求，不是本报告的策略建议，引用时保留 "can no longer require assertions" 原文强度。边界：不得写成「不支持 App Attest 的设备即可视为高危设备」——Apple 未提供该推论的任何依据；也不得写成「降级通道无风险」——覆盖面收窄的事实与处置空白的事实同时成立。

## 7.5 机型与模拟器支持矩阵：本次核查的负面结果

本节是整个第 7 章唯一的「无内容可写」节点，按纪律如实登记为缺口清单，并说明每一项缺口的准确形状。

**机型支持枚举：未公开。** Apple 文档未给出「哪些机型支持/不支持 App Attest」的任何枚举；`isSupported` 只回答「这台设备当前提不提供该服务」，不回答「服务消失的设备边界在哪」。本次核查在 DeviceCheck 全部文档页中未检索到机型清单，结论写作：**App Attest 的 `isSupported` 机型判定条件，本次核查未获得可核实条目**。

**模拟器行为：未公开，而非「不适用」。** DeviceCheck 全部文档页中 `simulator` 与「模拟器」字样零命中。这意味着两件事：「Xcode 模拟器上 `isSupported` 返回 false」这类在工程社区近乎常识的说法，**没有 Apple 一手出处**，只能标注为推断或第三方经验，不可标 Apple 官方；同时也不得反向写成「Apple 说明了模拟器不受影响」，或据零命中断言任何行为——正确措辞是「Apple 未公开模拟器/虚拟化环境下 DeviceCheck 与 App Attest 的行为」。同理，iOS 虚拟化平台（研究用途的虚拟化产品一类）上这两项机制的表现，Apple 文档亦无任何表述；该主题的可核实证据止步于第三方与司法记录层面，属对抗面篇目的处理范围，本机制篇不越界引申。

**统一支持矩阵：不存在。** App Clip（6.5 有 entitlement 要求但无完整行为矩阵）、各类 app extension（7.3 矛盾未决）、watchOS 之外的平台面（5.6/7.2 只给出片段元数据与 Mac 恒 false），三份拼不出矩阵；本次核查未获得可核实条目支持一张统一的 App Attest 平台支持表。

一条重要的**证据隔离规则**需要在此申明，防止下游误引：MDM 生态的 Managed Device Attestation（`DevicePropertiesAttestation`）确实公布过硬件门槛（A11 Bionic 及以后的 iPhone/iPad、全部 M 系列芯片、Apple TV A12 及以后、Apple Watch S4 及以后，更早芯片不支持并会直接忽略相关 attestation key），但那属于**另一个机制、另一条信任根**（Apple Enterprise Attestation Root CA，面向受管设备，详见本篇第 9 章之后的受管设备部分），按口径纪律不得把 MDM 的 A11 门槛移植、外推或暗示为 App Attest 的机型矩阵——两者同源于 Secure Enclave 是直觉，但在证据上是两回事。App Attest 支持面的合法描述只到：「iOS 14.0 起的 API 可用性 + `isSupported` 运行期布尔判定 + Mac 恒 false + 机型细分未公开」这四件事为止。

### 7.5 小结与边界

本节的每一条都是负面结果登记，价值在于划出「本报告中哪些空格是 Apple 没写、而不是我们没找到」。缺口与简报 D 节对应条目（机型枚举、模拟器取值、统一矩阵）一一对应；凡外部资料在这三处给出确定性表格的，读者应能据本节判定其必为推断或第三方口径。

## 7.6 核心张力：SE 提供密钥保护，不提供宿主系统真实性判定

第 7 章的全部材料收敛到 Apple 自己写下的一组并置句上。这组句子是本报告「设备真实性防线以 App/密钥真实性为代理」这一限定语的直接出处，必须完整给出：

> "A compromised version of your app running on a genuine, unmodified Apple device can't create valid assertions. However, an attacker that modifies the device's operating system might bypass restrictions. Although modifying the operating system is difficult and an unlikely source of widespread fraud, you might need to guard against an attack that uses a single compromised device to serve assertions to many subscribers."
> 【L1 Apple 文档：documentation/devicecheck/assessing-fraud-risk】

> "No single policy can eliminate all fraud. For example, App Attest can't definitively pinpoint a device with a compromised operating system."
> 【L1 Apple 文档：documentation/devicecheck】

把 7.1 与本节并排读，张力就显形了：SE 侧原文说私钥"from where no process can ever directly read or modify it"（7.1），攻陷侧原文说"an attacker that modifies the device's operating system **might** bypass restrictions"（本节）。两句不矛盾——前者的量词是「任何进程」，约束的是正常启动链之下的用户态/内核态读取；后者点名的对象是**被修改的操作系统本身**，即启动链与系统完整性已不在前提集合内的场景。Apple 在这两个句子之间没有写第三句来缝合，缝合处留给开发者：**SE 保护的是密钥材料，不保护「此刻执行签名代码的这台系统是原版的」这一前提**。App Attest 证明的边界条件因此可以精确陈述为：在真实未修改的 Apple 设备与原版系统上，被篡改的 App 实例产不出有效断言（第一句的正面能力）；系统被修改后，该保证由 Apple 明文降级为 "might bypass" 的可能性表述。

限定语纪律在此全部生效。其一，Apple 关于「修改操作系统」的原文只有上引两句（`assessing-fraud-risk` 的 might bypass 句与框架页的 can't definitively pinpoint 句），本报告一切越狱/篡改相关叙述只能挂在这两句上并标注「Apple 原文」；第三方推断（例如「App Attest 无法防越狱/模拟器」的确定性表述）不得写成 Apple 结论。其二，"difficult and an unlikely source of widespread fraud" 是**厂商定性自评，非测量结论**，不得据此调低风险等级，Apple 自己紧接着就点了 "a single compromised device to serve assertions to many subscribers" 这一具体滥用形态。其三，两句中的 "jailbreak"/"越狱" 一词 Apple 均未使用——其口径始终是 "a device with a compromised operating system" / "an attacker that modifies the device's operating system"，「越狱」是本报告沿用社区与论文用词的转述（首次出现处的术语声明见本篇概念章）。

对防线架构的落地含义（与第 4 章责任分配并读）：正因为设备端只剩「密钥读不走」这一条硬保证、而系统级前提无法自证，真实性判定的**全部可验证材料**才被设计为出设备、进服务端的两类密码学对象（证明对象与断言对象），设备侧不提供任何可直接采信的自检结论——本节因此同时是第 5–7 章机制描述的收束，和第 9 章之后极客对抗部分讨论绕过证据时的机制基线。

### 本章口径边界

第 7 章事实基线：SE 驻留与不可读改有 L1 原文（措辞为 read or modify，「不可导出」系转述）；`isSupported` 为只读属性、两框架语义不同、Mac 恒 false 有 L1 原文；扩展支持两处文档互相矛盾（F-2，已并列）；不支持时官方要求服务端降级放行、处置规则空白归开发者；机型枚举、模拟器行为、统一支持矩阵三项为本次核查未获得可核实条目的缺口；SE 对宿主系统真实性的沉默同样属缺口而非否认——Apple 只写了 "might bypass"，未写「必然绕过」或「无法绕过」，任何一方向的强化表述都越出本节依据。

---

# Apple 给的唯一量化：risk metric 与速率口径

## 8.1 risk metric：Apple 公开的唯一量化风险指标

在进入本节细节前，先完成一条对流行预期的修正。风控圈流传「Apple 从不给任何风险信号，DeviceCheck/App Attest 只是纯密码学管道」——该说法**不成立**。核查结论是：Apple 确实提供了**一个**公开的风险量化指标，名为 risk metric，形态是**近 30 天该设备上本 App 的 attested key 的近似计数**。它不是评分、不是判定、不附阈值，但它真实存在且有一整套服务端取回协议。对预期「Apple 不提供任何设备风险信号」，本节按一手原文推翻；修正后的完整口径留到 8.2 收束。

指标的定义句来自 `assessing-fraud-risk`，逐字：

> "Although it isn't possible to detect fraudulent activity with absolute certainty, App Attest does provide a metric to assess its likelihood. Specifically, you can get an approximate count of unique attestations for your app on a particular device. A count that's higher than expected might be an indication of a compromised device that's serving multiple compromised instances of your app."
> 【L1 Apple 文档：documentation/devicecheck/assessing-fraud-risk】

**取得方式是一段独立的服务端到服务端调用。** App 从 attestation object 中取出 receipt，服务端把**整段 receipt 以 Base64 作为请求体** POST 给 Apple，Apple 返回新 receipt。端点为生产 `https://data.appattest.apple.com`、沙箱 `https://data-development.appattest.apple.com`；鉴权同样是 APNs 风格的 JWT，且有一条容易漏掉的前置要求："Be sure to enable the DeviceCheck service when requesting the cryptographic key that you use to encrypt the App Attest token."【L1 Apple 文档：documentation/devicecheck/assessing-fraud-risk】即 App Attest 的指标接口在凭据层面反向依赖 DeviceCheck 服务的开通。环境隔离规则与 6.5 一致："You can't use a receipt generated in one environment to request a new receipt in the other."

**receipt 本体与字段清单。** 容器描述为 "a PKCS #7 container that contains a signature, certificate chain, and an ASN.1–encoded payload"；payload 的官方字段表（逐字核到）：

| 字段号 | 名称/内容 | 说明 |
| --- | --- | --- |
| 2 | App ID | 如 `A1B2C3D4E5.com.example.appname` |
| 3 | Attested Public Key | 须与首次证明时存储的公钥一致 |
| 4 | Client Hash | 对应挑战哈希 |
| 5 | Token | — |
| 6 | Receipt Type | `ATTEST`（首次证明附带）或 `RECEIPT`（服务端申请的新收据） |
| 12 | Creation Time | 本地校验要求 "no more than five minutes old. This helps to thwart replay attacks." |
| 17 | **Risk Metric** | 本节指标；**只有服务端申请的 `RECEIPT` 才携带**——"Only the latter provide the risk metric in field 17." |
| 19 | Not Before | 早于此日期刷新会得到 304 |
| 21 | Expiration Time | 逾期后 "the Apple server might not honor the request"，故指标必须周期性取回 |

【L1 Apple 文档：documentation/devicecheck/assessing-fraud-risk】

服务端本地校验的官方清单：验签并追溯到 App Attest 的 Apple public root certificate；字段 2 必须是本 App 的 App ID；字段 12 创建时间不超过五分钟；字段 3 公钥与首次证明时存储的一致。四项全部有原文，属于合规集成的必答项。

**术语统一。** 同一事物在 Apple 文档中有三个名字：`risk metric`（receipt 字段 17 名）、`fraud metric` 与 `fraud assessment metric`（`validating-apps-that-connect-to-your-server`），外加 8.1 引用的描述语 "an approximate count of unique attestations…"。本报告首次出现即用 **risk metric（风险指标）**，其余两名在口径冲突表中并列收录，不得让下游读者误以为是三个指标。

### 8.1 小结与边界

本节事实链（存在一个指标、如何取回、字段表、校验项、环境隔离）均为 L1 原文。未公开项先登记两项：字段 17 计数「approximate」的计算定义与精度、Apple 是否/如何事后修正已发收据——本次核查未获得可核实条目。

## 8.2 指标的解释边界：它是计数，不是评分，也不是越狱判定

字段 17 的官方语义定义逐字如下：

> "The receipt represents the metric as a string that indicates the number of attested keys associated with a given device over the past 30 days. Look for this value to be a low number. Note that the metric can grow if a user reinstalls your app, restores from a backup, or transfers a device to another user."
> "This growth should be modest, but you'll have to tune your risk assessment logic based on the typical numbers that you see over time. You can help to keep the number small by only generating new keys when absolutely necessary."
> 【L1 Apple 文档：documentation/devicecheck/assessing-fraud-risk】

据此给出本指标的三条定性边界，每条都对应原文的措辞特征：

| 常见误读 | 原文依据 | 正确口径 |
| --- | --- | --- |
| 「Apple 给了个风险评分」 | 定义是 "the number of attested keys… over the past 30 days" | 它是一个**窗口计数**（近 30 天该设备上的 attested key 数），无归一化、无评分语义 |
| 「计数升高=设备被攻陷」 | "A count that's higher than expected **might be** an indication of…"；且重装/备份恢复/转让三种正当行为都会推高 | 计数只是**可被正当行为污染的间接信号**，Apple 亲自用 might 措辞降格其证明力 |
| 「可以照抄某个阈值」 | "you'll have to tune your risk assessment logic based on the typical numbers that you see over time" | Apple 明文把阈值标定责任交给开发者，未提供任何官方阈值 |

与 5.4 的校正闭环在此完成：「The App Attest service records device metrics that you can't reset.」【L1 Apple 文档：documentation/devicecheck/preparing-to-use-the-app-attest-service】——不可重置的正是这个计数；其设计意图可从上下文读出不难：若开发者能清零，"higher than expected" 就退化为可被掩盖的历史。这也解释了 6.3 的重试指引为何强调同参重试 "helps to preserve the risk metric"。

**修正的另一半同样要写足：「Apple 不提供越狱/系统篡改判定服务」这半边是成立的**，且属已核实为否定结论的条目——DeviceCheck/App Attest 全部文档页 grep `jailbreak` 零命中；`DCDevice` 与 `DCAppAttestService` 的公开成员全集只有 `current`/`shared`、`isSupported`、`generateToken`、`generateKey`、`attestKey`、`generateAssertion` 与 `DCError`，不存在任何「设备完整性判定」「风险评分」类 API；客户端错误码里也没有篡改/越狱类 case（6.6 表）。因此报告口径必须同时携带两句：**「Apple 提供信号与一个近似计数指标，但不提供阈值与处置规则；Apple 不提供越狱或系统篡改的布尔判定服务」**——前句修正预期，后句保留预期，缺一即失真。

两点工程性推论直接从原文推出。其一，指标是 App Attest 集成的**专属副产品**：没接 App Attest 就没有 receipt，也就没有字段 17——对全 App 生态而言，这条唯一量化信号的可得性本身受 7.4 的可用性盲区与采用率约束，未集成者只能回到自有信号。其二，刷新是调度义务而非可选优化：既要 "wait until after the date found in field 19"（早取得 304），又要在字段 21 到期前取回（逾期 "the Apple server might not honor the request"，Apple 原话补了一句 "if you want to receive metrics at all, you need to do so periodically"）；而 receipt 在 Apple 侧的存储时长/保留策略，本次核查未获得可核实条目。

### 8.2 小结与边界

本节全部限定语（approximate、might be、tune your own、can't reset）均为 Apple 原词，引用时不得替换为更确定的中文（「识别出被攻陷设备」「风险评分」均属违规改写）。未公开项：计数算法精确定义、是否可被 Apple 追溯修正、字段 17 的字符串格式边界。

## 8.3 仅有的两个速率明文锚点：「rule of thumb」不是保证

Apple 在速率与规模主题上的公开文字高度集中，全部出自 `preparing-to-use-the-app-attest-service` 一节，逐字引用如下（这是本篇范围内 Apple 关于吞吐的**全部**明文数字）：

> "Apple servers might throttle attestation traffic from a particular app to avoid becoming overwhelmed if too many instances of your app make this call simultaneously."
> "As a rule of thumb, we suggest gradually and uniformly ramping up no more than 10 million users per day per app."
> "To avoid encountering rate limits, we generally recommend that your apps call this method fewer than 100 request per second across all installations of your apps. The rate limit threshold may fluctuate dynamically to protect Apple's certificate signing infrastructure. Be prepared to handle these errors and to have a mechanism to pull back on requests from your apps if this occurs."
> "After the initial rollout, your app makes requests only for new users, new devices, and reinstallations. This kind of traffic shouldn't result in any throttling."
> 【L1 Apple 文档：documentation/devicecheck/preparing-to-use-the-app-attest-service】

两个锚点的准确性质必须按 Apple 自己的框定语言来写，不得升格：

| 锚点 | 原文框定词 | 可以写 | 不可以写 |
| --- | --- | --- | --- |
| ≤1000 万用户/日/App | "As a rule of thumb, we suggest" | Apple 给出的**渐进放量建议值**，面向初次全量推送场景 | 「Apple 支持每日千万级证明」「配额为 1000 万」 |
| <100 req/s（全安装合计） | "we generally recommend"；紧接 "The rate limit threshold may fluctuate dynamically" | Apple 的**调用速率建议**，且明示真实阈值动态浮动、以保护其证书签发基础设施 | 「限流线是 100 QPS」「低于 100 QPS 必不被限流」 |

尤其要注意第三句原文自带的两个义务："Be prepared to handle these errors" 与 "have a mechanism to pull back on requests from your apps"——Apple 把「实现退避」与「具备源头降压机制」直接写成开发者的分内事。这两句与 8.4 的失败模式表衔接：限流在工程上的可观测形式只有 5.6 已列的 429 Too Many Requests（服务端 REST 侧）与 `DCError.serverUnavailable`/重试路径（App 侧证明调用），Apple 没有公布任何「距离限流还有多少」的可查询水位。

其余速率类问题一律按未公开处理，包括：DeviceCheck query/update 与 `data.appattest.apple.com` 指标接口的具体 QPS/日配额数字、Apple 两项服务的可用性 SLA 与延迟指标、对同一 key 重复 attestation 的次数限制、故障公告渠道——以上各项本次核查均未获得可核实条目。第四句"shouldn't result in any throttling" 同样只是对稳态流量的定性预期，不是承诺。本报告中凡出现「Apple 保证/承诺」字样的速率陈述均属违规措辞；正确句式只有一种：**「Apple 给出的建议阈值（rule of thumb）为 X，并称真实阈值可能动态浮动。」**

### 8.3 小结与边界

本节把「1000 万/日」「100 req/s」两数钉死为建议值口径（对应 E 节数字限定条款），同时登记稳态流量预期的原文性质。缺口清单中「服务速率上限、SLA、延迟」三项在此定案为：仅有建议值与 429/503 错误码，无承诺数字。

## 8.4 服务端集成成本、失败模式与未公开限制清单

把第 5–7 章的机制事实换算成服务端工程语言，可以得出一张「Apple 给了什么、没给什么」的分界表。Apple 给出的是协议、字段、端点与校验清单；以下六个部分被文档明文或隐式地划给开发者自建，构成集成成本的主体——这是「需要开发者自行设计的部分」，不是缺陷控诉：

| # | 自建项 | 文档依据要点 |
| --- | --- | --- |
| ① | ES256 JWT 签发与密钥托管轮换 | 两套服务端接口（DeviceCheck REST、App Attest 指标）都要求 APNs 同级 provider-token 体系；申请密钥时须启用 DeviceCheck 服务（8.1） |
| ② | (用户, 设备, 代次, 环境) → (keyId, 公钥, receipt) 状态表 | "Be prepared to store multiple (key, receipt) pairs for each user… keep development pairs separate from production pairs."（6.5） |
| ③ | challenge 与 counter 状态机 | 一次性随机挑战须生成并记存比对；`counter` 须持久化并校验单调递增（6.3、6.4） |
| ④ | 429/503/304 退避与指标刷新调度 | "Be prepared to handle these errors and to have a mechanism to pull back…"（8.3）；receipt 须按字段 19/21 窗口周期刷新（8.2） |
| ⑤ | Apple 私有 CA 根证书的下载与长期信任 | 验链使用 www.apple.com/certificateauthority/private/ 的 App Attest root（6.3）；属隐性运维项，含根证书轮换跟踪 |
| ⑥ | 重装/迁移/恢复后的重新证明流 | 密钥不跨三事件存活，客户端与服务端都要处理「同一用户反复重证明」的历史归并（6.5） |

负面结果同样登记完整：Apple 文档对以上任何一项**均未提供托管方案、参考实现或兼容性承诺**（本次核查未获得可核实条目）。

**失败模式按处置方向可分为四类**，全部有原文：

| 失败面 | Apple 口径 | 处置归属 |
| --- | --- | --- |
| 证明联系失败（`serverUnavailable`） | 同 key、同 clientDataHash 稍后重试，避免无谓新建密钥以保全 risk metric | 客户端/服务端协同，策略明文 |
| 证明产出但服务端验不过 | "If your server fails to verify the attestation object, discard the key identifier." | 处置动作明文，但「为何失败、要不要拉黑设备」留白 |
| 断言验不过 | "it's your responsibility to decide how to handle the request." | 完全归开发者（与第 4 章责任分配一致） |
| 时钟与凭据漂移 | DeviceCheck 三请求体强制 `timestamp`；400 Bad Timestamp、401 Authorization Token Expired 为文档列明错误码；receipt 早取 304、逾期不保证受理 | 偏移容忍量 Apple 未公开，NTP 纪律归开发者 |

**未公开限制总清单**（本章及第 5–7 章汇总，凡列于此者正文一律写「未公开/本次核查未获得可核实条目」，不得写「不存在」）：DeviceCheck 与 App Attest 服务端的 QPS/日配额具体数字；Apple 服务可用性 SLA 与延迟；越狱/篡改判定的任何官方阈值（判定服务本身不存在，7.6/8.2）；risk metric 的计算定义与精度；attestation receipt 在 Apple 侧的保留/删除策略；SE 内 App Attest 密钥数量上限；两个 bit 的历史与审计可见性；`isSupported` 的机型白名单；模拟器/虚拟化行为；同一 key 重复证明的次数限制；`403 Forbidden` 的具体触发条件；`last_update_time` 更新粒度；token 有效期秒数；App Clip/扩展/平台的统一支持矩阵；曲线与算法标识；以及 Apple 对任何具体绕过手段的公开结论与攻防胜率数据。

这份清单与第 9 章之后的对抗面叙述构成分工：机制篇到此为止，凡清单内项目，对抗篇同样只能引用 Apple 的两条原则性上限句（7.6）作机制边界，不能代 Apple 填空。

### 本章口径边界

第 8 章是量化锚点的一章，也是「量化稀缺」本身被坐实的一章：全篇可写的硬数字只有 30 天窗口、字段 12 五分钟、挑战 ≥16 字节、counter 首证为 0/此后递增、≤1000 万用户/日与 <100 req/s 两个建议值——其余皆为本清单所限的未公开区。任何超出该清单边界的产品宣称或研究结论，在本报告口径下必须降级为「第三方观察」或「推断」，并注明出处等级。

---
# Managed Device Attestation：企业侧真正拿得到的平台级证明

## 9.1 `DevicePropertiesAttestation` 的定位：第三条、也是唯一一条 MDM 侧平台级证明通道

第 5 章与第 6 章讨论的两项能力（DeviceCheck 的每设备两位状态位、App Attest 的密钥证明与断言）都住在 DeviceCheck 框架里，调用方是**开发者的 App 与其服务端**。企业侧走的是另一条完全独立的通道：MDM 协议 `DeviceInformation` 命令响应里的 `DevicePropertiesAttestation` 键，社区与部分工程文档把它称为 **Managed Device Attestation**。它的官方定义句是「The key to get an attestation of the device’s properties.」【L1 https://developer.apple.com/documentation/devicemanagement/deviceinformationresponse/queryresponses-data.dictionary】。

这条通道与 App Attest 的差别不是"更严一档"或"演进版本"，而是**从所属协议、信任根到可见字段全都不同**。本篇把它列为必须分立的口径项：把两者的证据互相引用会造成信任根错位的判断错误。

| 维度 | App Attest（第 6 章） | Managed Device Attestation（本章） |
| --- | --- | --- |
| 所属协议 | DeviceCheck 框架 + DeviceCheck/App Attest 服务端接口 | MDM Protocol Reference（Device Management） |
| 谁发起查询 | App 端生成 key，开发者服务端校验 | MDM 服务端向受管设备下发 `DeviceInformation` 命令 |
| 证明载体 | WebAuthn 风格的 attestation object（authenticator data + `x5c` 证书链） | leaf→中间→根的**DER 格式证书数组** |
| 信任根 | App Attest root（Apple Private PKI Repository） | `Apple Enterprise Attestation Root CA` |
| 被证明对象 | 「某个 key 由指定签名类别的 App 实例在真实设备上生成」 | 「若干项**设备属性**由 Apple 证明服务器判定并签发」 |
| 环境标记词 | `aaguid` = `appattestdevelop` / `appattest`+7×`0x00`；entitlement 用 `development`/`production` | 无环境概念，用 `DeviceAttestationNonce` 对应的 freshness code 表达新鲜度 |
| 新鲜度/防重放 | 一次性 challenge、`counter` 单调递增、receipt 创建时间 ≤ 5 分钟 | freshness code（`.10.2`），细节未公开 |

三点必须在此定住。第一，MDA 证明的是**设备属性可归因于 Apple 的签发**，它不是"该设备未越狱"的判定；写"设备真实性"时需保留第 8 章已经确立的限定——以 App/密钥真实性或属性签发为代理指标。第二，Apple 对这条通道的自我定位是"信任评估的一个输入"：「The feature provides a cryptographic declaration of device properties that you can use as part of a trust evaluation. This feature helps protect against various threats, such as a compromised device providing false information.」【L1 https://developer.apple.com/documentation/devicemanagement/validating-a-managed-device-attestation-attestation】。这句里的威胁对象是"被攻陷的设备向管理端谎报属性"，与第 10 章要写的负面结果（MDM 字段清单里根本没有越狱判定项）恰好构成一组对照：**Apple 在 MDM 侧愿意承诺的是"属性不被伪造"，不愿意承诺的是"属性里包含完整性判定"。** 第三，这三条通道在覆盖面上互不包含：DeviceCheck bits 不需要纳管、App Attest 不需要纳管、MDA 必须有 MDM 注册；一台 BYOD 设备可以同时给出合法 bits 与合法 App Attest 证明，却永远给不出 MDA，反之亦然。

## 9.2 证明的载体形态与签发规则：证书链、OID 三态与校验责任

载体形态的官方描述是逐句可引的，本节把它按"结构 → 条件 → 失败行为"三层拆开。

结构层原文：「The value is an array of certificates in DER form that forms a certificate chain. The chain is rooted with the Apple CA [Apple Enterprise Attestation Root CA]. The first array item is the leaf certificate. The leaf certificate contains custom OIDs describing a device.」【L1 https://developer.apple.com/documentation/devicemanagement/deviceinformationresponse/queryresponses-data.dictionary】。四个要点全部落在这句话里：值类型是**证书数组**、编码是 **DER**、数组首项是 **leaf**、设备属性写在 **leaf 证书的自定义 OID** 上，而不是写在 MDM 响应的普通键值里。这意味着企业服务端拿到的不是一个可以直接比较的字符串，而是一段需要建链与验签的密码学材料。

条件层原文：「The OS version of the device, and the type of enrollment, determine which OIDs are present in the certificate.」同一句里紧跟着给出了无法判定时的三种行为：「If Apple’s attestation servers are unable to verify a device property they generate a blank value, omit the OID entirely, or refuse to issue an attestation certificate.」

**OID 因此是三态信号，不是布尔信号。** 校验页把这一条写成了明确的解读规则：「Any missing OIDs or blank values indicate that Apple’s attestation servers were unable to reliably determine the corresponding property.」——缺失与空白含义是"无法可靠判定"，既不是"判定为假"，也不是"判定为真"。策略层如果把三态压成两态（例如把缺失当"允许"或当"违规"），就已经在替 Apple 做 Apple 没做过的判定。

失败行为层（校验责任与处置口径）原文：「If the leaf certificate doesn’t chain to this CA, the attestation isn’t trustworthy. There’s no scenario during normal operations in which this validation step fails. You can choose to ignore the attestation entirely, or flag it as an incident that needs investigation.」这段里 Apple 给了两句分量很不一样的话：前一句是判定规则（链不到这里就不可信），后一句是**把处置权完全交出**（忽略，或立案调查，二选一由管理方决定）。这与第 6 章服务端断言校验的口径一致：「If the assertion object fails verification, it’s your responsibility to decide how to handle the request.」【L1 https://developer.apple.com/documentation/devicecheck/validating-apps-that-connect-to-your-server】。Apple 在两条通道上都不提供阈值、不提供处置建议、不提供风险等级，这是本篇第 4 章责任分配结论在 MDM 侧的复现。

工程上还必须写出信任材料本身的位置：链根**不随响应返回**。本次核查核实到的原文口径是，MDM 侧 DeviceInformation attestation 的链根「Apple Enterprise Attestation Root CA… isn’t provided in the response, but is available from the [Apple Private PKI Repository]」。与之并列的是 App Attest 侧要用 `https://www.apple.com/certificateauthority/private/` 取根。两者合起来构成一条隐性运维项：**企业需要自行下载并长期信任 Apple 私有 CA 根证书**，包括其更新、轮换与本地存储；Apple 文档未对根证书的轮换节奏与兼容窗口提供承诺（本次未核实到相关条目）。

## 9.3 OID 实际覆盖项逐条清单（iOS 侧能证明的只有这些）

下表是本次逐条核实到的 OID 全量清单。凡未在表中出现的属性，一律视为"本次未核实到 Apple 会为其签发"，不得推测补全。OID 编号一律照抄文档写法。

| OID（完整前缀 `1.2.840.113635.100.8.`） | 覆盖项 | 引入版本 | 文档口径与核实状态 |
| --- | --- | --- | --- |
| `.9.1` | 序列号 | iOS 16 / iPadOS 16 / tvOS 16 / watchOS 10 / visionOS 1 / macOS 14 | 已核实；与 `QueryResponses` 的 `SerialNumber` 同源，但此处为 Apple 签发值 |
| `.9.2` | UDID | 同上 | 已核实；文档特别注明在 Mac 上该值等于 `ProvisioningUDID`，并写明「doesn’t match the UDID used elsewhere in the MDM protocol」——跨通道比对 UDID 前必须读这句 |
| `.9.4` | sepOS version | 同上 | 已核实；原文「the version of the operating system running on the Secure Enclave when the attestation is generated」，即证明时刻的 SE 系统版本 |
| `.10.2` | freshness code | 同上 | 已核实；对应请求侧的 `DeviceAttestationNonce`，文档提示缓存可能导致不匹配；**取值集合与判定阈值本次未核实** |
| `.10.1`、`.10.3` | 该批次引入的属性编号 | iOS 17.2 / macOS 14.2 等 | 本次核查只核实到这两个 OID 编号出现在清单中，以及该批次引入的三项属性为 `SoftwareUpdateDeviceID`、OS Version、LLB Version；**编号与属性的一一对应关系本次未逐条核实，不写入结论** |
| （同上批次） | `SoftwareUpdateDeviceID` | iOS 17.2 / macOS 14.2 等 | 已核实为该批次引入项；与 `QueryResponses` 同名字段对应 |
| （同上批次） | OS 版本 | iOS 17.2 / macOS 14.2 等 | 已核实为该批次引入项 |
| （同上批次） | LLB Version | iOS 17.2 / macOS 14.2 等 | 已核实；Low Level Bootloader 固件版本，文档指向 Apple Platform Security 的 boot process 章节 |

三点解读，均需按原文边界来读。

第一，**这份清单里没有"启动链完整性判定"这一项，只有"启动链上的固件版本号"**。LLB 版本与 sepOS 版本是版本字符串，不是"该版本是否可信"的结论；把它们变成结论需要管理方自己维护一份版本白名单与漏洞窗口，而 Apple 未公布任何这样的判定阈值（本次未核实）。第 8 章已确立的"Apple 提供信号不提供结论"口径在这里同样成立。

第二，UDID 一项要按文档原话加限定。`.9.2` 在 Mac 上落的是 `ProvisioningUDID`，文档明确写它与 MDM 协议其他位置使用的 UDID 不一致【L1 https://developer.apple.com/documentation/devicemanagement/validating-a-managed-device-attestation-attestation】。凡是把"证明里的 UDID"与"设备记录里的 UDID"直接做相等判断的实现，在 Mac 平台上会稳定失败；这不是设备异常，是 Apple 已经写明的口径差异。

第三，序列号与 UDID 出现在**可被 Apple 密码学签发的属性**里，这一事实与第 11 章的口径不矛盾：Apple 不向 App 暴露硬件标识，但**向 MDM 服务端暴露**，且暴露路径要求 Device Information access right 与 MDM 注册。两者的差异是"谁能拿到"，不是"存不存在"。

## 9.4 关键边界写实：SIP、Secure Boot、第三方 kext 三项只在 macOS 14.2+

这是本章最容易被下游写手写错、也最容易被产品宣传语模糊掉的一条边界，因此单独成节。

本次核查的核实结果是：**`1.2.840.113635.100.8.13.1`（SIP status）、`.13.2`（Secure boot status）、`.13.3`（Third party kernel extensions allowed）三项 OID，Apple 只写作 "introduced in macOS 14.2"。** 换言之，iOS、iPadOS、tvOS、watchOS、visionOS 上**不存在等价项**，Managed Device Attestation 在这些平台上无法证明系统完整性配置。三项的核实取值如下（照抄文档，不改写）：

| OID 尾号 | 属性 | 取值 | 平台 |
| --- | --- | --- | --- |
| `.13.1` | SIP status | `0` / `1` | 仅 macOS 14.2+ |
| `.13.2` | Secure boot status | `Full Security` / `Reduced Security` / `Permissive Security` | 仅 macOS 14.2+ |
| `.13.3` | Third party kernel extensions allowed | `0` = 不允许 | 仅 macOS 14.2+ |

把两侧能力并排：

| 可被 attestation 的属性 | iOS（含 iPadOS 同代） | macOS |
| --- | --- | --- |
| 序列号（`.9.1`） | 有（iOS 16+） | 有（macOS 14+） |
| UDID（`.9.2`，Mac 上为 `ProvisioningUDID`） | 有 | 有 |
| sepOS 版本（`.9.4`） | 有 | 有 |
| freshness code（`.10.2`） | 有 | 有 |
| `SoftwareUpdateDeviceID` / OS 版本 / LLB 版本 | 有（iOS 17.2+） | 有（macOS 14.2 等） |
| SIP 状态 | **无等价项** | 有（`.13.1`，macOS 14.2+） |
| Secure Boot 模式 | **无等价项** | 有（`.13.2`，macOS 14.2+） |
| 第三方 kext 许可 | **无等价项** | 有（`.13.3`，macOS 14.2+） |

同一条边界在 `QueryResponses` 普通键值层也有对应物：`SystemIntegrityProtectionEnabled` 文档标注为**仅 macOS 10.12+**【L1 https://developer.apple.com/documentation/devicemanagement/deviceinformationresponse/queryresponses-data.dictionary】。`SecurityInfo` 命令侧的完整性类字段同样是 macOS 独占：`AuthenticatedRootVolumeEnabled`（macOS 11+）、`SecureBoot`（macOS 10.15+）、`FirmwarePasswordStatus`、`BootstrapTokenAllowedForAuthentication` / `BootstrapTokenRequiredForKernelExtensionApproval` / `BootstrapTokenRequiredForSoftwareUpdate`（Apple silicon macOS 11+）、`FDE_Enabled` 系列、`IsRecoveryLockEnabled`（macOS 11.5+）；iOS 侧能取到的只有通行码合规类（`PasscodePresent`、`PasscodeCompliant`、`PasscodeCompliantWithProfiles`、`PasscodeLockGracePeriod(Enforced)`，iOS 4+）与 `HardwareEncryptionCaps`（原文口径为 "Block-level encryption / File-level encryption / Both"，iOS 4+）【L1 https://developer.apple.com/documentation/devicemanagement/securityinforesponse/securityinfo-data.dictionary】。

由此得到两条禁令式口径，写作与产品设计都应遵守。其一，**不得把 macOS 的 SIP/Secure Boot/kext 字段借用为 iOS 设备的完整性结论**，包括"iOS 设备查不到 SIP 所以按关闭处理"这类映射——查不到与关闭是两件不同的事，Apple 文档只把这三项写作 macOS 引入。其二，**不得把 `PasscodeCompliant` 或 `HardwareEncryptionCaps` 当作系统完整性信号使用**。前者的语义是通行码策略合规，后者描述的是设备支持的加密粒度类别；两者都与"系统分区是否被改动"无关，本次核查未见任何原文把它们与完整性判定关联。

**版本口径统一**：本节全部平台/版本判断按"截至 2026 年 8 月版 Apple 文档"成立——MDM attestation 基线为 iOS 16 / macOS 14，部分 OID 为 iOS 17.2 / macOS 14.2。Apple 未提供这些 OID 的向后兼容承诺或历史行为说明（未核实）。

## 9.5 支持面与适用前提：硬件门槛、纳管前提与两种工作流

MDA 不是"所有受管设备都能给"的能力，它同时受三条前提约束：芯片门槛、纳管状态、请求侧字段。本次核查按表逐项核实。

硬件门槛（逐表核实结果）：

| 支持 | 不支持 |
| --- | --- |
| A11 Bionic 及以后的 iPhone / iPad；全部 M 系列 Mac；Apple TV A12 及以后；Apple Watch S4 及以后 | A10x Fusion 及更早的 Intel Mac；A10x Fusion 及更早的 iPhone / iPad；S3 及更早的 Apple Watch；Vision Pro 文档记为 "none" |

不支持的设备的回退行为是明确写出的：它们会「ignore the `DevicePropertiesAttestation` and `DeviceAttestationNonce` keys」——即响应里不会出现该键，而不是出现一个降级值。服务端必须把"键不存在"实现为一条独立分支，且不能把它等同于"设备异常"，因为在 A10 及更早的机型上这是正常行为。

纳管前提：该能力住在 MDM 协议里，`DeviceInformation` 命令本身要求设备已完成 MDM 注册，多数查询项另需 "the Device Information access right"。**因此任何未受管设备（BYOD、个人设备、员工自带 iPhone）都不可能给出 MDA**——这一条常被"用 MDM 字段做风控"的方案忽略：风控要覆盖的恰恰主要是未纳管设备，而 MDA 只在纳管设备里可用。

请求侧字段：证明与新鲜度由 `DeviceAttestationNonce` 驱动（与 `DevicePropertiesAttestation` 同为该命令的相关键）。本次核查只核实到"存在该键、不支持的机型会忽略它、freshness code 与之对应、缓存可能导致不匹配"这四点；nonce 的长度约束、生成方职责、Apple 侧是否记录，均**未公开/本次未核实**。

Apple 文档给出的是两条并列工作流，而非一条：

1. **Device information attestation**：MDM 服务端下发 `DeviceInformation` 查询，设备在响应里带回证书链。这是本节全部字段与 OID 清单所属的路径。
2. **ACME attestation**：走 `device-attest-01` challenge。其官方价值表述是「provides a chain of trust from the CSR back to the hardware-bound private key」——信任链从证书签名请求一直落到硬件绑定私钥。

两条路径的信任根同为 `Apple Enterprise Attestation Root CA`，但可见内容与调用形态不同。本次核查对 ACME 侧只核实到上述一句定位与 challenge 名称，其余细节（记录类型、有效期、错误语义）未获得可核实条目；报告不据 ACME 页面补写 DeviceInformation 路径的字段行为，两者不互为证据。

**运维侧的隐性成本项**（由本节事实组合得出，标注为分析推断）：一个要长期使用 MDA 的企业系统，需要同时维护"机型支持矩阵 + 纳管状态 + 根证书材料 + 键缺失分支"四份状态，其中机型矩阵只能来自 Apple 文档表格本身，Apple 未提供任何可编程查询的支持性接口。

## 9.6 校验责任在服务端、freshness 的语义与未公开项

MDA 的责任分配结构与第 4 章确立的总体分工完全同构，只是把当事方从"App 服务端"换成"MDM 服务端"。Apple 做的事只有一件：把若干设备属性写进 leaf 证书并以企业证明根 CA 签发。剩下的全部都在管理方一侧——取回证书数组、按 DER 解析、建链到 `Apple Enterprise Attestation Root CA`、逐 OID 读出属性、把三态（有值 / 空白或缺失 / 整份证明被拒发）映射为自己的策略。Apple 对链校验失败只留了两个选项：「You can choose to ignore the attestation entirely, or flag it as an incident that needs investigation.」

把这一句与第 6 章引过的两句并置，可以得到本篇最稳定的一条判断：**Apple 三条平台级通道（bits、App Attest、MDA）在服务端侧都只提供"可验证的材料"，没有一条提供"可用的结论"**。而设备侧的自检被 Apple 明文否定——「You can’t rely on your app’s logic to perform security checks on itself because a compromised app can falsify the results.」【L1 https://developer.apple.com/documentation/devicecheck/establishing-your-app-s-integrity】。MDA 的价值正好补在这条否定的另一头：它防的就是"设备端上报的属性可以被伪造"，即 Apple 自己说的那句 "helps protect against various threats, such as a compromised device providing false information"。**但这句原文的保护对象是"属性不被伪造"，不是"属性里包含完整性判定"**——后者是第 10 章要处理的缺口。

freshness 的语义按本次核查核实到的写法只能记到这里：请求侧提供 `DeviceAttestationNonce`，leaf 证书里对应出现 `.10.2` freshness code，文档提示因缓存可能导致两者不匹配。**freshness code 的取值集合、各取值的含义、"多旧算不新鲜"的阈值、缓存窗口长度，本次均未获得可核实条目**，因此本节不写任何"freshness=0 表示重放"之类的解释，也不把它做成规则表。可比照的是 App Attest 侧明确写出的防重放三件套（一次性 challenge、`counter` 单调递增、receipt 创建时间不超过 5 分钟），但那是另一套机制的原文，不能移作 MDA 依据。

本章遗留的未公开/未核实项汇总：

| 项目 | 状态 |
| --- | --- |
| freshness code 取值语义与判定阈值 | 未公开（本次未获得可核实条目） |
| `DeviceAttestationNonce` 长度与生成方职责 | 未公开 |
| `.10.1` / `.10.3` 与 `SoftwareUpdateDeviceID`、OS 版本、LLB 版本的对应关系 | 本次未逐条核实 |
| MDA 证书的有效期、签发频率、Apple 侧存储与保留期 | 未公开 |
| OID 集合的向后兼容承诺 | 未公开 |
| Apple Enterprise Attestation Root CA 的轮换节奏与公告渠道 | 未公开 |

表外另记一条**口径纪律**（非缺口）：Managed Device Attestation 与 App Attest 是两个不同机制——前者属 MDM/企业 PKI，链根为 `Apple Enterprise Attestation Root CA`；后者属 DeviceCheck 框架，链根为 App Attest root。**两者不得合并叙述，也不得互相引用证据。**

**本章口径边界**

- 本章只覆盖 `DevicePropertiesAttestation`（Managed Device Attestation）在 MDM 协议中的官方语义、OID 覆盖项、平台边界与支持面；SE 的内部结构、密钥层级与启动链原理归《iOS 核心底层技术支撑》与《iOS 钥匙串窃取》第 6 章，本章不重述。
- 本章所有平台/版本判断按"截至 2026 年 8 月版 Apple 文档"成立：基线 iOS 16 / macOS 14，扩展项 iOS 17.2 / macOS 14.2。
- 本章不写"MDA 可用于判断设备是否越狱"：其 OID 集合内没有越狱或系统完整性判定项，iOS 侧连 SIP/Secure Boot/kext 三项都没有等价项。
- 本章不写"MDA 可覆盖全部企业设备"：它同时受 A11/M 系列门槛与 MDM 纳管前提约束，未纳管设备与 A10 及更早机型拿不到。
- 表中未列出的 OID、未核实的字段名与未公开的阈值，一律以"未公开/本次未核实"呈现，不作为"不存在"表述，也不作推测补全。

---

# 负面结果：MDM 设备信息里没有越狱与完整性字段

## 10.1 `DeviceInformation` 命令的真实适用前提与权限口径

本章的对象是一个"查不到什么"的问题，因此先把命令本身的适用面钉清楚，否则后面的负面结果会被读成"Apple 没有设备信息能力"——那恰好是反的。

命令官方定位一句：`DeviceInformation` = 「Get detailed information about a device.」响应对象 `DeviceInformationResponse` 内的 `QueryResponses` 字典承载全部查询项【L1 https://developer.apple.com/documentation/devicemanagement/device-information-command】。本次从页面元数据逐字核实的可用性如下表，四个维度各自独立，不要合并理解。

| 元数据维度 | 核实值 |
| --- | --- |
| Device channel | iOS、macOS、Shared iPad、tvOS、visionOS、watchOS |
| User channel | macOS、Shared iPad |
| Requires supervision | `N/A` |
| Allowed in user enrollment | iOS、macOS、visionOS |
| access right 标注 | "Special Case" |
| 多数查询项的前置权限 | "the Device Information access right" |
| 网络类查询项的前置权限 | "Network Information access right" |

这里要特别处理 `Requires supervision = N/A` 这一格。它只说明命令本身不以监督模式为准入门槛，**不等于**"所有查询项在任何纳管形态下都会返回值"；哪些键在 unsupervised 或用户级注册下会缺省，本次核查未逐项核实，本章不做推测。同理，"Allowed in user enrollment" 列出 iOS/macOS/visionOS，只表示命令在用户级注册中允许下发，不表示 MDA 与硬件标识类字段一定会出现。

结论式的适用面前提有三条：其一，本命令及其全部响应键**只在受管设备上可用**，前提是 MDM 注册完成并授予相应 access right；其二，本次抓取范围内**未发现任何可用于未受管（BYOD/个人）设备的 MDM 字段**，上述能力都以 MDM 注册与相应 access right 为前提；这是"MDM 协议内未见该能力"的核实结果，不等于 Apple 在其他渠道没有相关信息；其三，命令可见 ≠ 字段可信——第 9 章已经说明 `DevicePropertiesAttestation` 的价值正在于"属性需要被密码学签发才可信"，而 `QueryResponses` 里的普通键值本质上是设备侧回报的字符串，二者可信度等级不同，混用会把"设备自报"当成"平台签发"。

**分析推断（非 Apple 口径）**：一个只做 MDM 查询、不校验证书链的实现，等价于把设备端的自我陈述当作管理端事实；这一风险恰是第 9 章引文里 "a compromised device providing false information" 所指的对象。

## 10.2 核对方法与核心负面结果：95 个响应键名里没有任何越狱或完整性字段

这一节是本篇最有价值的产出之一，因此先写方法，再写结论；结论必须能被读者复现。

核对方法（三步，均为本次实际执行动作）：

1. **取全量键名清单**。以 `QueryResponses` 文档页列出的全部 `name` 为核对范围，逐条抄录（**本次核对的键名总量为 95 个**），而不是只挑"看起来像安全字段"的项；10.3 的归类表为本次抄录到的条目，少数纯运维/网络类键未在此重复展开。
2. **在键名层做关键词排查**。以 `jailbreak`、越狱、`root`、`Cydia`、`integrity`、`trust-level`、`risk` 为检索词逐一比对 95 个键名。结果为**零命中**——注意 `SystemIntegrityProtectionEnabled` 含 "Integrity" 字样，但它被文档明确限定为 **macOS 10.12+ 专属**，不构成 iOS 侧完整性字段，因此不计入命中。
3. **在文档层做词频核查**。对本次抓取范围内的 Device Management 全部页面 grep `jailbreak`，同样**零命中**。

核心负面结果（可直接引用的口径写法）：

> 在截至 2026 年 8 月版 MDM 文档中，`DeviceInformation` 响应的 `QueryResponses` 全量 95 个键名里，**不存在任何越狱、root、系统完整性、信任等级或风险评分字段**；同一批 Device Management 页面中 `jailbreak` 一词零命中。Apple 在 MDM 侧提供的完整性相关能力，只有第 9 章的 `DevicePropertiesAttestation`（且 iOS 侧不含 SIP/Secure Boot/kext）与 macOS 独占的 `SecurityInfo` 完整性字段两类。

必须同时写下的方法论限定：**负面结果 ≠ 证明不存在**。本节成立的范围仅为——"在本次可核实的 Apple 公开 MDM 文档范围内，未获得该字段"。以下三种推论都不在本节支持范围内，也不得以任何形式出现：

| 不得写的推论 | 为什么不能写 |
| --- | --- |
| "Apple 内部不采集设备是否越狱" | 公开文档缺席不等于内部数据不存在；本次只能陈述公开文档 |
| "越狱设备无法被 MDM 识别" | 未核实；本节只证明"没有该字段"，没有证明"没有其他识别途径" |
| "Apple 计划补充该字段 / 不会补充该字段" | 无路线图依据，两个方向都是推测 |

反向也要防止一种更常见的误读：把 10.3 归类表里那些**看起来与安全相关的键**当作缺失字段的替身。`IsSupervised` 对应监督模式，`IsActivationLockEnabled` / `IsActivationLockSupported` 对应激活锁，`IsMDMLostModeEnabled` 对应丢失模式，`DiagnosticSubmissionEnabled` 与 `AppAnalyticsEnabled` 是提交开关——它们共同构成"配置面"，与"系统是否被篡改"没有文档层面的对应关系。`EACSPreflight` 在本次核实范围内只确认其作为键名存在，**文档语义未核实**，因此本节不为它赋予任何安全含义。把配置面读成完整性面，是 10.6 误用警示的第一类根因。

另有一条与本章结论直接相关、也常被写错的边界：`PasscodePresent` 一类字段**不在 `DeviceInformation` 命令内，属 `SecurityInfo` 命令**。两个命令的响应字典是不同文档页（`queryresponses-data.dictionary` 与 `securityinfo-data.dictionary`），实现时按错误页取键会得到空值，而空值容易被误当作"未设通行码"或"设备异常"。这是纯粹由字段归属页混淆造成的误判，与设备状态无关。

## 10.3 这份清单实际能看到什么：字段归类与"看似安全字段"的真实语义

为了让"没有越狱字段"这个结论可被检验，本节把 `QueryResponses` 的键名按用途归类列出。分类标签是本篇为了行文所加，**Apple 文档本身不做这样的分组**；键名一律照抄。

| 归类（本篇添加） | 键名（照抄文档） |
| --- | --- |
| 身份与硬件标识 | `SerialNumber`、`UDID`、`ProvisioningUDID`、`DeviceID`、`EASDeviceIdentifier`、`SoftwareUpdateDeviceID`、`BluetoothMAC`、`WiFiMAC`、`EthernetMAC`、`IMEI`、`MEID`、`ICCID`、`PushToken` |
| 型号与系统版本 | `Model`、`ModelName`、`ModelNumber`、`ProductName`、`BuildVersion`、`SupplementalBuildVersion`、`SupplementalOSVersionExtra`、`OSVersion`、`ModemFirmwareVersion` |
| 纳管与设备形态 | `IsSupervised`、`IsAppleSilicon`、`IsMultiUser`、`AwaitingConfiguration`、`OrganizationInfo`、`MDMOptions`、`ActiveManagedUsers`、`AutoSetupAdminAccounts`、`CurrentConsoleManagedUser` |
| 锁与安全开关（配置面） | `IsActivationLockEnabled`、`IsActivationLockSupported`、`IsMDMLostModeEnabled`、`IsDeviceLocatorServiceEnabled`、`IsCloudBackupEnabled`、`LastCloudBackupDate`、`EACSPreflight`、`DiagnosticSubmissionEnabled`、`AppAnalyticsEnabled`、`PINRequiredForEraseDevice`、`PINRequiredForDeviceLock`、`SupportsLOMDevice`、`SupportsiOSAppInstalls` |
| macOS 专属完整性字段 | `SystemIntegrityProtectionEnabled`（macOS 10.12+） |
| 平台级证明 | `DevicePropertiesAttestation`（第 9 章） |
| 策略与设置回显 | `OSUpdateSettings`、`SoftwareUpdateSettings`、`AccessibilitySettings`、`ServiceSubscriptions`、`TimeZone`、`HostName`、`LocalHostName`、`DeviceName` |
| Shared iPad 会话类 | `ResidentUsers`、`MaximumResidentUsers`、`EstimatedResidentUsers`、`QuotaSize`、`OnlineAuthenticationGracePeriod`、`TemporarySessionOnly`、`TemporarySessionTimeout`、`UserSessionTimeout`、`SkipLanguageAndLocaleSetupForNewUsers` |
| 网络与运营商类 | `IsNetworkTethered`、`IsRoaming`、`VoiceRoamingEnabled`、`PersonalHotspotEnabled`、`DataRoamingEnabled`、`CellularTechnology`、`CarrierSettingsVersion`（iOS 16 弃用 / iOS 26 移除） |
| 账号与商店 | `iTunesStoreAccountHash`、`iTunesStoreAccountIsActive`、`ManagedAppleIDDefaultDomains` |
| 电池与容量 | `HasBattery`、`BatteryLevel`、`DeviceCapacity`、`AvailableDeviceCapacity`、`IsDoNotDisturbInEffect` |

对照"看不到"的部分——下列语义在该清单中**没有任何对应键名**：越狱/root 状态布尔值、系统分区完整性、信任等级（trust level）、SIP/Secure Boot/第三方 kext 许可（iOS 侧）、启动链验证结果的布尔表达、任何风险评分或风险等级、任何 Apple 侧对"该设备是否可信"的判定输出。

四点补充口径（均为字段语义层面的澄清，不含新功能讲解）：

- **`IsAppleSilicon` 与 `IsSupervised` 不是完整性字段。** 前者是芯片形态描述，后者是纳管形态描述；一台 Apple silicon 设备在监督模式下完全可以同时是被改动过的系统（本篇不讨论改动方式，只说明字段语义不含判定）。
- **`OSVersion` / `BuildVersion` 是版本字符串，不是"该版本是否受支持/是否有已知缺陷"的结论。** 版本比对策略由管理方自行维护，Apple 未在这些键上给出判定语义。
- **`iTunesStoreAccountHash` 一类账号字段属于可见性事实，不在本篇讨论范围**；其在数据类别与合规维度上的意义归《iOS 数据采集能力深度分析》，本篇只把它作为"清单里确实有账号类键"的证据使用。
- 锁类与安全开关字段的**功能讲解**（激活锁、丢失模式、失窃设备保护的机制与用户流程）归《iOS 失窃设备保护》；本篇只取它们作为"MDM 可见字段清单"的证据，不复述功能。

`SecurityInfo` 命令侧的分布进一步印证本节结论：完整性类字段几乎全为 macOS 专属（`SystemIntegrityProtectionEnabled`、`AuthenticatedRootVolumeEnabled`、`SecureBoot`、`FirewallSettings`、`FirmwarePasswordStatus`、`RemoteDesktopEnabled`、Bootstrap token 三项、`FDE_Enabled` 系列、`IsRecoveryLockEnabled`、`ManagementStatus`），iOS 侧只有 `PasscodePresent` / `PasscodeCompliant` / `PasscodeCompliantWithProfiles` / `PasscodeLockGracePeriod(Enforced)` 与 `HardwareEncryptionCaps`，外加 Shared iPad 的 `AutoLockTime`（iOS 17+）【L1 https://developer.apple.com/documentation/devicemanagement/securityinforesponse/securityinfo-data.dictionary】。**把两个命令合起来看，Apple 在 MDM 侧对 iOS 设备的"安全可见面"= 通行码合规 + 加密能力位 + 纳管/锁/备份开关 + 一份只覆盖少量属性的 attestation，不含任何篡改判定。**

## 10.4 `InstalledApplicationList`：MDM 侧最接近"非商店来源"的信号及其性质

如果企业一定要在 MDM 侧找一个与"这台设备上的 App 是不是正常分发来的"沾边的东西，唯一候选是 `InstalledApplicationList` 命令的响应项。其字段清单（照抄）为：`AdHocCodeSigned`、`AppStoreVendable`、`BetaApp`、`IsValidated`、`DistributorIdentifier`、`Source`、`DeviceBasedVPP`、`Identifier`、`ShortVersion`、`Version`、`ExternalVersionIdentifier`、`Path`、`BundleSize`、`DynamicSize`、`HasUpdateAvailable`、`Installing`、`IsAppClip`、`Name`、`Note`【L1 https://developer.apple.com/documentation/devicemanagement/installedapplicationlistresponse/installedapplicationlistitem】。

其中与安全判定相关的是四个：`AdHocCodeSigned`、`AppStoreVendable`、`IsValidated`、`DistributorIdentifier`（另有 `Source`、`DeviceBasedVPP`、`BetaApp` 描述安装来源与渠道）。本次核查的定性结论是：**这是 MDM 侧最接近"被篡改/非商店来源 App"的可用信号，但它给出的只是"来源/签名类别"标签，不是"该 App 被改动"的判定**；且仅对受管设备可见。

这个区别为什么关键：App 被改动的检测需要回答"运行时代码与分发时代码是否一致"，而上述四个字段回答的是"这个包是以哪一类签名身份/哪一条分发渠道装进来的"。两者之间没有 Apple 承诺过的映射关系。第 12 章会看到 Apple 在 App Attest 证明里同样只给"签名身份类别"（`apple_validation_category_01`），并且**未给出"重签名一定落哪一档"的断言**——两条通道在这一点上的克制是一致的。

三条使用规则（按本次核实到的文档语义写）：

| 字段（本次仅核实到键名） | 可以支持的判断 | 不可以支持的判断 |
| `AdHocCodeSigned` | 该 App 以 ad-hoc 签名形态安装 | "该 App 被第三方重签名/被篡改" |
| `AppStoreVendable` | 该 App 是否属于商店可售形态（按键名与文档简述理解） | "值为 false 即代表违规安装"（企业内部分发本就非商店可售） |
| `IsValidated` | 该 App 的校验状态位取值 | "值为 false 即代表系统被越狱" |
| `DistributorIdentifier` / `Source` | 分发者标识与安装来源归类 | 任何关于包内容完整性的结论 |

**分析推断（非 Apple 口径）**：在企业自有设备为主的环境里，`AppStoreVendable=false` 与 `AdHocCodeSigned=true` 是**正常态**——ADEP 内部分发、开发构建、TestFlight 都会产生非商店形态。也就是说这组信号在同一种环境里天然与"异常"混淆，把它直接接到告警规则上会产生大量结构性误报；若要使用，需按"该设备本应允许哪些分发形态"预先建立基线，而不是对字段值本身设阈值。本次核查未获得 Apple 对这组字段的取值语义细则（各字段的文档描述在本次抓取中仅为键名与简要说明），因此不写具体判定条件。

## 10.5 Apple 不提供官方越狱/风险判定服务——与第 8 章 risk metric 的严格区分

负面结果不止 MDM 一处。把范围从 MDM 扩到 DeviceCheck/App Attest，本次核查得到的同样是零命中：

| 检索面 | 关键词 | 结果 |
| --- | --- | --- |
| DeviceCheck / App Attest 全部已抓取文档页 | `jailbreak` | 零命中 |
| `DCDevice` 与 `DCAppAttestService` 公开成员清单 | — | 只有 `current`/`shared`、`isSupported`、`generateToken`、`generateKey`、`attestKey`、`generateAssertion`、`DCError`；不存在"是否越狱""设备完整性判定""风险评分"类 API |
| Device Management（MDM）全部页面 | `jailbreak` | 零命中 |
| `DCError` 错误码全集（5 个 case） | — | `featureUnsupported`、`invalidInput`、`invalidKey`、`serverUnavailable`、`unknownSystemFailure`；不存在"检测到篡改/越狱"类错误码 |

最后一行的工程含义值得单独写：客户端错误码只有 5 个粗粒度值，且描述都是"不可用/输入不合法/密钥失效/服务联系失败/未知系统故障"这一类**运维语义**，没有任何一个携带风险语义。因此客户端异常只能作为**重试与降级的依据**，不能作为风险判定输入。`serverUnavailable` 的官方处置建议甚至要求用同一 key 与同一 `clientDataHash` 重试，理由是 "Retrying with the same inputs helps to preserve the risk metric for a given device"【L1 https://developer.apple.com/documentation/devicecheck/dcappattestservice】——把它当成"疑似攻击"上报会直接破坏第 8 章那个指标的可用性。

**必须与第 8 章区分的口径**。这两句话看起来矛盾，实际是两个不同层面的核实结果，混淆会导致下游写手把"没有判定服务"写成"没有任何信号"，或把"有一个指标"写成"Apple 会告诉你设备是否越狱"：

| | 第 10 章这句 | 第 8 章那句 |
| --- | --- | --- |
| 内容 | Apple **不提供**越狱/篡改的布尔判定服务，也**不提供**风险评分服务 | Apple **提供**一个指标：receipt 字段 `17`（`Risk Metric`），即"近 30 天该设备上与本 App 关联的 attested key 近似计数" |
| 形态 | 无 API、无字段、无错误码 | 一个需要服务端凭 Base64 receipt 向 `https://data.appattest.apple.com` 换取新 receipt 才能取回的原始计数 |
| 语义 | 缺失（负面结果） | 概率性、可被正当行为推高（重装、从备份恢复、设备转让都会增长），Apple 要求开发者自行"tune your risk assessment logic" |
| 可用性前提 | — | 依赖 App Attest 集成；未集成即无从获得；`isSupported` 为 false 时服务端被要求不得再强制断言 |

正确的合并表述只有一句：**"没有判定"成立，"没有任何风险信号"不成立。** 第 8 章已按此写过，本节把它与 MDM 侧的空缺并列，是为了防止读者以为企业还能从管理通道补上一个判定值——第 9、10 两章合起来的答案是：**补不上**。

另附一条 Apple 自己给出的定性说明，供引用时保留其口径性质：「No single policy can eliminate all fraud. For example, App Attest can’t definitively pinpoint a device with a compromised operating system. Instead, the DeviceCheck services provide information that you can integrate into an overall risk assessment for a given device.」【L1 https://developer.apple.com/documentation/devicecheck】。这里 "integrate into an overall risk assessment" 的主语是开发者自己的评估，不是 Apple 的评估服务。

## 10.6 为什么这条缺口对风控设计是坏消息，以及字段误用的六类典型误判

本节整段为**分析推断**，依据是 10.1–10.5 的核实结果，不代表 Apple 口径。

企业的设备真实性判断只有三种取材方式：**纳管状态**（这台设备在不在管理范围里）、**平台级证明**（第 9 章的 MDA、第 6 章的 App Attest）、**App 侧上报**（端检测）。MDM 没有越狱字段这条缺口之所以是坏消息，不是因为"少了一个字段"，而是因为它把这三条路径的盲区同时暴露出来：

- **纳管状态与系统完整性正交。** 一台 enrolled、`IsSupervised=true`、通行码合规、激活锁开启的设备，其系统是否被改动在 MDM 侧没有任何可见表达。管理强度不等于完整性，这是本节最核心的一条。
- **MDA 补不上这个洞。** 由 9.3/9.4 的 OID 清单可见，iOS 侧 Apple 肯为其签发的属性只有序列号、UDID、sepOS 版本、`SoftwareUpdateDeviceID`、OS 版本、LLB 版本与 freshness code；SIP/Secure Boot/kext 三项只在 macOS 14.2+。因此**"设备能交出合法 MDA 证书链"与"该设备系统未被改动"之间不存在文档层面的蕴含关系**。MDA 保证的是这些属性不被伪造，不保证这些属性里含完整性。
- **App 侧上报被 Apple 明文否定为可信来源。** 「You can’t rely on your app’s logic to perform security checks on itself because a compromised app can falsify the results.」于是形成闭环缺口：可信的两条（纳管、平台证明）不含完整性，含完整性的那条（端检测）默认不可信。
- **唯一的官方风险信号不在这条链上。** 第 8 章的近似计数指标依赖 App Attest 集成，覆盖不到未集成该机制的场景，也不是布尔判定。
- **未纳管设备连前两条都没有。** BYOD 与个人设备既无 MDM 字段也无 MDA，只剩 App Attest 与 bits。

由此得到本篇第 4 章责任分配在 iOS 企业侧的具体落点：**"这台设备是否被篡改"不是一个可以从 Apple 平台直接取到的事实，只能是一个由业务侧组合多路信号后做出的判定；而"是否纳管"必须是这个判定里显式分开的一维，否则会把管理强度误读为安全强度。**

误用警示清单——把 MDM 字段当完整性判定依据会造成什么：

| 误用写法 | 实际语义 | 后果 |
| --- | --- | --- |
| "`QueryResponses` 没有越狱字段，所以该设备未越狱" | 字段缺失是"未提供该信息"，不是"结论为否" | 放行被改动设备，且把放行写成了"经平台确认" |
| "iOS 设备查不到 `SystemIntegrityProtectionEnabled`，说明 SIP 被关闭/失效" | 该键仅 macOS 10.12+ | 对全部 iOS 设备产生同向误判，规则整体失效 |
| "`IsSupervised=false` 即设备不可信" | 仅描述监督模式 | 把未纳管设备一律拒止，误伤 BYOD 正常用户 |
| "`AdHocCodeSigned=true` 即 App 被重签名篡改" | 签名/分发形态标签 | 企业内部分发环境持续误报（见 10.4） |
| "`PasscodeCompliant=true` 即系统完整性合规" | 通行码策略合规 | 用配置面替代完整性面，评估报告出现虚假安全感 |
| "`DevicePropertiesAttestation` 键不存在即设备异常" | 不支持的机型会忽略该键（A10 及更早） | 对老机型批量误判为异常 |

表外还有一条通用纪律：attestation 的空白值与缺失 OID 的含义是"Apple 证明服务器无法可靠判定该属性"，**必须保留为第三态**；把它并入"通过"或"不通过"任一侧，都是用两态实现冒充三态事实。

**本章口径边界**

- 本章的结论是"未获得"，不是"不存在"：所有负面结果均限于本次可核实的 Apple 公开文档范围（MDM Protocol Reference / Device Management 与 DeviceCheck 全部已抓取页面）。
- 核对口径固定为：`DeviceInformation` 的 `QueryResponses` 全量 95 个键名逐项核对 + 关键词排查 + 文档层 `jailbreak` 词频核查；版本按"截至 2026 年 8 月版 Apple 文档"。
- 10.6 全节标注为分析推断；10.4 末段与 10.5 表外的工程含义亦为分析推断，不得作为 Apple 口径引用。
- 越狱与绕过手法的机制层描述归《iOS 系统防御与机制绕过》第 11 章，越狱链与工具版本归《iOS 典型漏洞攻击链安全分析报告》第 5 章；本章只处理"Apple 在管理通道里能看到什么、看不到什么"。
- 激活锁、丢失模式、失窃设备保护的功能讲解归《iOS 失窃设备保护》；`PasscodeCompliant` 一类字段的采集与合规含义归《iOS 数据采集能力深度分析》；本章仅将其作为字段清单证据使用。

---

# 设备标识符与审核政策：能引的条款与不能引的口径

## 11.1 IDFA：`ASIdentifierManager.advertisingIdentifier` 的官方语义与用途闭集

设备真实性判断常常被业务方包装成"先给我一台设备的稳定 ID"。本篇因此需要先说明 Apple 在标识符上的官方口径：它是**广告用途专用**且**不稳定**的，任何把它当设备真实性锚点的做法，都会同时踩到语义错位与政策错位。

IDFA 的官方定义（逐字）：「The `advertisingIdentifier` is an alphanumeric string that’s unique to each device, and which you only use for advertising. Use this string for frequency capping, attribution, conversion events, estimating the number of unique users, advertising fraud detection, and debugging.」【L1 https://developer.apple.com/documentation/adsupport/asidentifiermanager/advertisingidentifier】。

这句话里有三个对风控有用的限定：

| 限定 | 原文依据 | 对"设备真实性"的影响 |
| --- | --- | --- |
| 用途被限定在广告域 | "which you only use for advertising" | 把 IDFA 用作设备身份判定属于超出官方声明用途的使用 |
| 允许用途是闭集 | "frequency capping, attribution, conversion events, estimating the number of unique users, advertising fraud detection, and debugging" | 清单里没有"设备完整性判定""反作弊通用标识"这类条目 |
| 唯一性范围是"每台设备" | "unique to each device" | 唯一性成立，但下面的失效条件决定了它**不可依赖** |

值得注意的是，Apple 官方在 IDFA 页给出的场景词里包含 "advertising fraud detection"。这是本篇第 12 章场景词清单的一部分：**Apple 自己把 IDFA 的欺诈检测用途限定在广告域**，而不是"用 IDFA 识别可疑设备"。风控设计里常见的"拿 IDFA 做设备维度累犯统计"这一写法，在 Apple 文档中既没有依据，也与其 "only use for advertising" 相冲突。

术语层面需要与系列其他篇目切开：IDFA/IDFV 的**采集能力矩阵、字段清单与隐私标签**归《iOS 数据采集能力深度分析》；本章只保留"官方语义与限制"以及"政策可引条目"两层，不重复列举采集面。

## 11.2 IDFA 的失效条件：授权门槛与六类全零返回值

"限制"这一节如果只写"需要授权"就失真了。本篇的写作纪律是**必须保留具体失效条件**，否则读者会得出"授权后即稳定"的错误结论。

授权门槛（原文）：「On devices running iOS 14.5 and later and iPadOS 14.5 and later, your app must request tracking authorization before it can get the advertising identifier.」【L1 https://developer.apple.com/documentation/adsupport/asidentifiermanager/advertisingidentifier】。

返回全零 `00000000-0000-0000-0000-000000000000` 的情形，按页面逐条核实为六类：

| # | 情形 | 口径说明 |
| --- | --- | --- |
| 1 | "In Simulator, regardless of any settings." | 无论任何设置均为全零——AdSupport 文档对模拟器行为有明确表述（对照：DeviceCheck/App Attest 文档未出现 `simulator` 字样，见 12.3，两者不可互推） |
| 2 | macOS 内 | 在 Mac 上取该标识得到全零 |
| 3 | 在 visionOS 内运行的 iPad/iPhone 兼容 App | 兼容运行形态下不返回值 |
| 4 | 未请求 tracking 授权 | 授权流程未发起 |
| 5 | 用户拒绝授权 | 授权被拒 |
| 6 | "When a profile or configuration restricts access to the advertising identifier." | 由配置文件/策略限制 |

第 6 类是本章唯一需要跨出一步的一条：Apple 把它写成"配置文件或描述文件可以限制对该标识的访问"。这是**设备管理面能够直接影响标识符**的官方依据。配置文件与 TCC 授权模型本身的机制细节不在本章展开（归《iOS 访问控制与应用层交互限制》第 6 章），本章只取该句证明"策略层可以切断标识符"这一事实。

另外两条常被漏写的稳定性条件：

- 用户可随时更改授权：「Users can change their authorization for tracking at any time in Settings > Privacy > Tracking.」——即该标识的可见性是**运行时可变**的，不是一次性属性。
- Apple 直接建议**不要持久化该值**：「As a best practice, don’t store the advertising identifier value; access `advertisingIdentifier` instead.」这句对风控设计的含义很硬：**Apple 的官方口径是"每次现读"，因此任何把 IDFA 落库作为设备主键的实现都与官方最佳实践相反**，并且会随授权变化产生无法解释的历史断点。

综合本节的核实结论（沿用文档语义的中文表述）：**IDFA 不是稳定可依赖的设备指纹载体**。唯一性描述与"不可依赖"并不矛盾——前者说的是同一时刻同一设备上的取值形态，后者说的是它在跨时间、跨授权状态、跨运行环境（模拟器/Mac/visionOS）上的持续性。

## 11.3 `identifierForVendor`：厂商口径、变化条件与 `nil` 语义

`identifierForVendor`（IDFV）常被当作"IDFA 不可用时的替代设备 ID"，但它的作用域与存活条件都比这个用途窄。官方定义：「An alphanumeric string that uniquely identifies a device to the app’s vendor.」【L1 https://developer.apple.com/documentation/uikit/uidevice/identifierforvendor】——注意 "to the app’s vendor"：**它标识的是"某厂商在某设备上的应用集合"，不是"设备"**。

作用域（原文）：「The value of this property is the same for apps that come from the same vendor running on the same device. A different value is returned for apps on the same device that come from different vendors, and for apps on different devices regardless of vendor.」

"vendor" 如何推导，是这一节最容易被写错的地方。官方口径：「Normally, the vendor is determined by data provided by the App Store. If the app wasn’t installed from the app store (such as enterprise apps and apps still in development), then a vendor identifier is calculated based on the app’s bundle ID.」（文档另有 iOS 6 / iOS 7 取 bundle ID 前缀差异的表格）。这条对**企业自研 App 与开发中的 App 尤其重要**：非商店安装时 vendor 由 bundle ID 推导，因此同一组织内不同 bundle ID 的 App 之间**拿不到同一个 IDFV**——它不具备"厂商内跨 App 打通"的确定性。

变化与失效条件（全部为原文逐条核实）：

| 条件 | 原文依据 |
| --- | --- |
| 同一 vendor 的应用仍安装在设备上时保持不变 | "remains the same while the app (or another app from the same vendor) is installed on the iOS device" |
| 用户删除该 vendor 的**全部** App 后再重装其中一个 → 值改变 | "The value changes when the user deletes all of that vendor’s apps from the device and subsequently reinstalls one or more of them." |
| 用 Xcode 安装测试构建 → 可能改变 | "The value can also change when installing test builds using Xcode" |
| 以 ad-hoc 分发安装 → 可能改变 | "or when installing an app on a device using ad-hoc distribution." |
| 设备已重启但用户尚未解锁 → 返回 `nil` | "This happens, for example, after the device has been restarted but before the user has unlocked the device." |

Apple 自己给出的处置建议是：「Therefore, if your app stores the value of this property anywhere, you should gracefully handle situations where the identifier changes.」——即 Apple 假定该值会变。

对设备真实性防线的三条结论：

1. **IDFV 不能作为"设备身份"的长期主键**。删光同 vendor 的全部应用后重装、用 Xcode 安装测试构建、以 ad-hoc 分发安装，这三种情形都会改变取值；而被改造或被重新打包的分发链路恰好频繁使用重装与更换安装渠道。换句话说，**IDFV 发生变化的时机，与对手希望它发生变化的时机高度重合**（此句为本篇分析，非 Apple 表述）。
2. **`nil` 条件必须实现**，且不能把 `nil` 当"设备可疑"处理。首启解锁前取值为空是正常态；把它接进风险判定会产生结构性误报，这与第 10 章"把三态压成两态"是同一类错误。
3. **IDFV 与第 6/8 章的 keyId 不是一回事**。后者是 App Attest 密钥标识符，Apple 明说"没有 identifier 就无法使用或取出该密钥"，且密钥"出于隐私原因不跨重装/迁移/备份恢复存活"（"For privacy reasons, App Attest keys stored on device don’t survive these events"）——两者都会因重装而变，但一个是标识符、一个是密钥句柄，混用会导致把"keyId 变了"读成"设备换了"。

## 11.4 Apple 不把稳定硬件标识交给 App：措辞纪律与"平台机制不返回设备标识符"的事实

这一节的核实结果是**部分核实**，因此措辞纪律比结论本身更重要。

事实层（可核）：`UIDevice` 的标识/设备属性页列出的成员为 `identifierForVendor`、`localizedModel`、`model`、`name`、`systemName`、`systemVersion`、`userInterfaceIdiom`——**不含序列号，不含 UDID**【L1 https://developer.apple.com/documentation/uikit/uidevice/identifierforvendor 所在 `UIDevice` 标识与设备属性清单】。"公开 API 未提供序列号"这一负面结论**由 API 清单支撑**。

未获依据层（必须承认）：Apple **没有发布任何一句"App 不得读取序列号"式的原文**。序列号出现在两个地方：MDM 侧 `QueryResponses.SerialNumber`（需 Device Information access right）与第 9 章的 attestation OID `1.2.840.113635.100.8.9.1`；面向普通用户则只有 Apple 支持页的自助查询入口。

由此得到本篇的措辞纪律：

| 正确写法 | 禁止写法 | 理由 |
| --- | --- | --- |
| "公开 SDK 未暴露该能力" | "Apple 明令禁止读取序列号" | 前者是 API 清单可核的事实，后者无原文 |
| "本次核查未在 Guidelines 中找到相关条目" | "Apple 政策不允许设备标识采集" | 见 11.6，`fingerprint` 一词在 Guidelines 中出现 0 次 |
| "Apple 未提供跨 App 的稳定设备 ID" | "Apple 不存在设备唯一标识" | IDFA/IDFV 确实存在，只是不可依赖且有用途限定 |

另一个常见错误是把 DeviceCheck 与 App Attest 当成"Apple 发的设备 ID"。核实结论恰好相反：**这两项能力都不提供可用于跨 App、跨时间关联的设备标识符**——它们返回的是请求凭据与密钥句柄，而不是设备身份。

- DeviceCheck 的 token 被官方描述为 "An ephemeral token that identifies the current device"，且要求按单次使用处理："You should treat the token you receive in the completion block as single-use." 它是**请求凭据**，不是设备主键；把它存下来做设备关联，与 Apple 的 ephemeral/单用语义直接冲突。
- App Attest 返回的是 key 的标识符（`keyId`），Apple 的表述是"没有该标识符就无法使用或取出密钥"，作用域是**一把密钥**，不是一台设备；且第 8 章已引用其隐私取向："For privacy reasons, App Attest keys stored on device don’t survive these events, forcing your app to generate a new key on the same device."
- 两句合起来的正确口径是：**Apple 有意让这些材料不能作为跨设备、跨用户的稳定 ID**。报告若写"Apple 通过 App Attest 提供设备唯一 ID"即为错误口径。

于是本篇的"设备标识可得性"矩阵可以定表（企业侧 = MDM 通道，App 侧 = 公开 SDK）：

| 标识 | App 侧可得 | 企业 MDM 侧可得 | 平台证明侧可得 | 稳定性 |
| --- | --- | --- | --- | --- |
| IDFA | 可得（受 ATT 与全零条件约束） | 不适用 | 不适用 | 低（授权可变、可全零、不建议存储） |
| IDFV | 可得 | 不适用 | 不适用 | 中（删光重装/Xcode/ad-hoc 会变，重启未解锁为 `nil`） |
| 序列号 | **公开 SDK 未暴露** | `SerialNumber`（需 access right） | OID `.9.1`（需纳管 + A11/M 系列+） | 高（在可得的两侧均为硬件级） |
| UDID | **公开 SDK 未暴露** | `UDID` / `ProvisioningUDID` | OID `.9.2`（Mac 上等于 `ProvisioningUDID`，与协议其他处不一致） | 高，但跨通道比对需谨慎 |
| App Attest `keyId` | 可得 | 不适用 | 属另一机制 | 低（按 Apple 设计不跨重装存活） |
| DeviceCheck token | 可得（一次性） | 不适用 | 不适用 | 极低（ephemeral/单用） |

这张表的读法是：**稳定性与可得性成反比**——越接近硬件级、越稳定的标识，越只出现在需要纳管与权限的通道里；App 侧能拿到的两个标识，恰好都是 Apple 明确说明会变的。

## 11.5 政策侧可引条目：2.5.1、5.1.2(iii)、5.1.2(i) 与附带的 2.3.1(a)

App Store Review Guidelines 中与本主题相关的可引条目只有三类（外加一条附带可用）。条目号与原文均已核到，**引用需注明"以抓取当日版本为准，条款可能修订"**【L1 https://developer.apple.com/app-store/review/guidelines/】。

| 条目号 | 标题/主题 | 原文（逐字） | 在本主题里的可用性 |
| --- | --- | --- | --- |
| **2.5.1** | 私有 API | "Apps may only use public APIs and must run on the currently shipping OS." | 可引。用于说明：任何绕开公开 API 去取硬件标识或做环境探测的实现路径，在审核政策上直接不合规；后半句为"应使用 API/框架的其预期用途"类表述，引用时以页面原文为准 |
| **5.1.2(iii)** | 数据使用与共享 | "Apps should not attempt to surreptitiously build a user profile based on collected data and may not attempt, facilitate, or encourage others to identify anonymous users or reconstruct user profiles based on data collected from Apple-provided APIs or any data that you say has been collected in an 'anonymized,' 'aggregated,' or otherwise non-identifiable way." | **最接近"设备指纹"限制的一条**，可引；但注意它的落点是"识别匿名用户/重建用户画像"，落点不是"识别设备" |
| **5.1.2(i)** | ATT | "You must receive explicit permission from users via the App Tracking Transparency APIs to track their activity." | 可引，为 11.2 授权门槛的政策依据 |
| 5.1.1 / 5.1.2 | 章节标题 | "Data Collection and Storage" / "Data Use and Sharing" | 仅作归属定位使用，不带判定语义 |
| **2.3.1(a)** | 误导宣传（附带可用） | 原文把 "marketing your app in a misleading way, such as by promoting content or services that it does not actually offer (such as iOS-based virus and malware scanners)" 定为下架与终止账号的理由 | 可引，用于说明"Apple 不承认第三方安全/扫描类 App 的能力宣称"这一政策立场——这与第 10 章"Apple 不提供越狱判定服务"是同一取向的两面 |

三点使用注意。

其一，**2.5.1 与 5.1.2(iii) 覆盖的是不同侧面**：前者管"用什么接口取"，后者管"取到之后用来做什么"。风控方案里常见的错误是只引 2.5.1 来说明"不能做设备指纹"——2.5.1 讲的是 API 合法性，与指纹无涉；而只引 5.1.2(iii) 来说明"不能用私有 API"也错了，那属于 2.5.1。

其二，2.3.1(a) 里 Apple 直接点名 "iOS-based virus and malware scanners" 作为"宣传其实际不提供的内容或服务"的例子。这句话对本篇的价值是：**Apple 在审核政策层面把"我能扫出 iOS 上的恶意/风险"这类宣称本身视为误导**。这与第 10 章 MDM 侧没有判定字段、第 12 章"文档不存在 `jailbreak`/`bot` 等场景词"三条并置时，构成一条相当一致的平台立场描述（本句为分析归纳，非 Apple 原话）。

其三，条款文本里出现的是 "should not attempt to surreptitiously build"（不应秘密构建）与 "may not attempt, facilitate, or encourage others to identify anonymous users or reconstruct user profiles"（不得尝试/促成/鼓励他人识别匿名用户或重建画像）。把 "should not" 与 "may not" 混译为"禁止"会改变条款强度；引用时应保留两层措辞的差别，**这是解读层面的区分，不是原文层面的合并**。

## 11.6 误解校正：Guidelines 全文 `fingerprint` 出现 0 次，"明文禁止设备指纹"不成立

这是本节，也是本章最需要写足的一条**纠正性结论**。社区与部分厂商材料里流传着一个说法："Apple 政策明文禁止设备指纹"。**该说法不成立。**

核实数据：本次抓取的 App Store Review Guidelines 页面共 **240,818 字节**，其中：

| 检索词 | 出现次数 |
| --- | --- |
| `fingerprint` | **0** |
| `IDFA` | 0 |
| `IDFV` | 0 |
| `vendor` | 0 |
| `serial number` | 0 |

同一组负面结果也出现在 DeviceCheck 侧：框架页、`DCDevice`、`generateToken`、`accessing-and-modifying-per-device-data` 四个页面全文**未出现** `fingerprint` / `tracking` / 任何"禁止指纹"字样，最接近的只有 "while maintaining user privacy" 与 token 的 ephemeral/单用属性【L1 https://developer.apple.com/documentation/devicecheck】。也就是说：**"不得用 DeviceCheck/App Attest 做设备指纹或追踪"这句话，在 DeviceCheck 文档与 Guidelines 两个来源里都没有原文。**

这条误解校正的完整表述应包含三层：

1. **不成立的部分**：不存在"Apple 政策以'设备指纹'为对象设立条款"这一事实；任何以"Apple 明令禁止设备指纹"为前提推出的合规结论都需要重写。
2. **成立的部分**：存在可引的**邻近条款**——5.1.2(iii)（禁止秘密构建用户画像、禁止识别匿名用户或重建画像）、5.1.2(i)（ATT 显式授权）、2.5.1（仅限公开 API）。设备指纹在 Apple 政策语境里是**通过"用户识别/画像重建"这一路径被约束的**，不是通过"指纹"这一命名被约束的。
3. **必须标注的部分**：把 5.1.2(iii) 用于设备指纹场景，是**解读**，不是原文。正确写法形如："Guidelines 未出现 fingerprint 一词；本处依据 5.1.2(iii) 的 'identify anonymous users / reconstruct user profiles' 口径进行解读，属合规推断而非条款原文。"

同样需要一并校正的三个衍生口径：

| 常见写法 | 校正 |
| --- | --- |
| "DeviceCheck 被 Apple 定位为反隐私滥用的机制" | DeviceCheck 文档只说 "while maintaining user privacy"，未设隐私限制条款；限制要从 Guidelines 与许可协议取证 |
| "Apple 通过 App Attest 提供设备唯一 ID" | 见 11.4，key 有意不跨重装存活，不构成稳定 ID |
| "采集序列号违反 Apple 政策" | 公开 SDK 未暴露该能力；Apple 未发布"禁止读取序列号"原文；序列号在 MDM 通道内是正常可查字段（需 access right） |

ATT 与本主题的边界只用一句话交代：**App Tracking Transparency 的授权模型、SDK 侧实现与隐私合规细节属隐私系列篇目，本章只在"IDFA 取用需授权、政策依据为 5.1.2(i)"这一交叉点上使用它，不重述其机制。**

**本章口径边界**

- 本章讨论的是标识符的**官方语义与限制**以及**可引政策条目**；采集能力矩阵、字段清单、隐私标签归《iOS 数据采集能力深度分析》，TCC 与配置文件授权模型归《iOS 访问控制与应用层交互限制》第 6 章。
- 11.1–11.4 的失效条件（全零清单、`nil` 于重启未解锁、删光同 vendor App 后重装会变、Xcode 测试构建与 ad-hoc 会变）为**必须保留**项：省略其中任一条都会让"限制"论述失真。
- 11.5 条目号仅引用已核到原文的 2.5.1、2.3.1(a)、5.1.1、5.1.2(i)/(iii)；引用须标注"以抓取当日版本为准，条款可能修订"。
- 11.6 的负面结果按"未获得相关字样"表述，**不得反向断言"Apple 不限制设备指纹"**；政策立场的真实分布（许可协议、各地法规）不在本章核查范围内。
- "公开 SDK 未暴露"与"Apple 明令禁止"两种措辞不得互换；本章统一使用前者。

---

# Apple 对被篡改 App 的公开口径：场景词与它没说的部分

## 12.1 威胁模型原文与场景词逐字清单

Apple 关于"被篡改的 App"的公开论述，集中在 DeviceCheck 框架页的一段话里。这段是本报告能引用到的、关于该威胁面最完整的官方表述（逐字）：

> "Someone who modifies your app and distributes it outside the App Store can add unauthorized features like game cheats, ad removal, or access to premium content. The App Attest service gives your app a way to assert its validity so that your server can more confidently provide access to sensitive resources."
> 【L1 https://developer.apple.com/documentation/devicecheck】

它的结构值得逐层拆出来看，因为后面所有口径边界都由这个结构决定：

| 层 | Apple 的说法 | 核实状态 |
| --- | --- | --- |
| 对手行为 | 修改 App 并在 App Store 之外分发 | 原文陈述，未给判定机制 |
| 后果 | 加入未授权功能 | 原文给出三个例子（下表） |
| 防御落点 | App Attest 让 App "assert its validity"，由**服务端**更放心地放行敏感资源 | 已核实，与第 4 章责任分配一致 |

场景词逐字清单（本次核实到的全部官方场景表述，不含任何补全）：

| 场景词（照抄英文原词） | 出现页面 |
| --- | --- |
| `game cheats` | DeviceCheck 框架页 |
| `ad removal` | DeviceCheck 框架页 |
| `access to premium content` | DeviceCheck 框架页 |
| `sensitive or premium content` | DeviceCheck 框架页 / App Attest 相关页 |
| `promotional offer` | DeviceCheck 框架页（bits 用例："identify devices that have already taken advantage of a promotional offer that you provide"） |
| `flag a device that you’ve determined to be fraudulent` | DeviceCheck 框架页（bits 用例） |
| `Reduce fraudulent use of your services` | DeviceCheck 框架页 abstract |
| `advertising fraud detection` | `ASIdentifierManager.advertisingIdentifier` 页（限广告域，见 11.1） |
| `asserting app integrity` | DeviceCheck 框架页 abstract |
| `premium content`（断言时机示例："like when a user tries to access premium content"） | `DCAppAttestService.generateAssertion` 相关叙述 |

配套两句常被忽略但必须同引的原文，它们决定"防线画在哪里"：

- 端侧自检被官方否定：「You can’t rely on your app’s logic to perform security checks on itself because a compromised app can falsify the results.」【L1 https://developer.apple.com/documentation/devicecheck/establishing-your-app-s-integrity】
- 由被篡改 App 自行上报证明结果可被绕过：「A compromised version of your app could falsify the verification result, thus circumventing App Attest.」（`attestKey` 段落）【L1 https://developer.apple.com/documentation/devicecheck/dcappattestservice】

清单之外的任何场景词都不是 Apple 说过的话；下一节把这条边界量化为"哪些词在文档里不存在"。

## 12.2 负面结果：文档中不存在的词，以及 Apple 在越狱侧只有两句原话

**不存在的场景词。** 本次核查在 Apple DeviceCheck / App Attest 全部已抓取文档页中对以下词做词频核查，结果均为**零命中**：

| 词 | 结果 | 常见误写 |
| --- | --- | --- |
| `bot` | 零命中 | "Apple 已把机器人流量列为 App Attest 的防护场景" |
| `automation` | 零命中 | "App Attest 用于识别自动化脚本" |
| `emulator` | 零命中 | "Apple 说明模拟器环境下 App Attest 的判定行为" |
| `device farm` | 零命中 | "Apple 官方把设备农场纳入 App Attest 威胁模型" |
| `jailbreak` | 零命中（DeviceCheck 全部页面） | "Apple 在 App Attest 文档里提供了越狱判定" |

这条负面结果的约束力很强：**报告不得把上述词写成 Apple 已经考虑的威胁面。** Apple 官方口径能覆盖的是"欺诈 / 作弊 / 内容盗取类"（12.1 清单），bot、模拟器、设备农场这些对抗面在 Apple 文档层面**没有被命名**。第 13 章之后若讨论这些威胁面，来源必须显式落在研究文献或厂商材料上，不得挂在 Apple 名下。

**一处常见的错出处**也要在此校正：Apple Platform Security Guide（本地核对版本为 2026 年 8 月）全文对 `DeviceCheck` / `Device Check` / `App Attest` / `DCAppAttest` 的检索结果为**零命中**——该指南不设这两项机制的章节，只论述 Secure Enclave、启动链与代码签名。因此"Apple 平台安全指南论述了 App Attest"是**错误出处**；涉及这两项机制的一手依据只能来自 developer.apple.com 文档。

**Apple 在"系统被改动"这一侧只有两句原话**，两句都必须带"Apple 原文"限定语使用：

| 句 | 出处 | 口径性质 |
| --- | --- | --- |
| "App Attest can’t definitively pinpoint a device with a compromised operating system." | DeviceCheck 框架页（完整句为 "No single policy can eliminate all fraud. For example, …"） | 能力上限自述 |
| "However, an attacker that modifies the device’s operating system might bypass restrictions." | `assessing-fraud-risk` | 绕过可能性自述（"might"） |

第一句的完整上下文是"没有任何单一策略能消除全部欺诈"，因此它的读法是**"平台侧不存在充分判定"**，不是"App Attest 无效"。第二句的措辞是 "might bypass restrictions"，既不能升格为"已被绕过"，也不能降格为"不会被绕过"；同段还有两句必须一起引，否则会被读成风险等级结论：

> "Although modifying the operating system is difficult and an unlikely source of widespread fraud, you might need to guard against an attack that uses a single compromised device to serve assertions to many subscribers."
> 【L1 https://developer.apple.com/documentation/devicecheck/assessing-fraud-risk】

其中 "an unlikely source of widespread fraud" 是 **Apple 文档自评，不是测量结论**，不得据此在报告或产品里降低风险等级（这是本篇沿用的一致性纪律）。而 Apple 唯一**点名**的滥用形态是后半句：**一台被攻陷的设备为大量订阅者提供断言**。这一句对第 13 章之后的意义是——平台方自己承认的规模化威胁模型是"一台真机做种子、向外供凭据"，而不是"海量虚假设备"。

术语层面还要记一笔：Apple 原文在这一主题上交替使用 "authenticity"（"Your app uses the App Attest service to assert its authenticity"）、"validity"、"legitimacy"、"integrity" 四种词，**没有 "device authenticity" 这一固定术语**。本篇中文标题里的"设备真实性"需按此映射理解，不能当作 Apple 定义的既有概念引用。

## 12.3 签名身份类别字段与能力上限：Apple 公开口径里最接近"识别被篡改副本"的机制

在 App Attest 的 attestation object 中，与"这份 App 是哪一类签名身份产出的"直接相关的字段是 `apple_validation_category_01`（`UInt32`，位于 authenticator data 的 CBOR dictionary 内）。服务端要求为一句原文：「Verify the `validationCategory` value within the CBOR dictionary in the authenticator data.」【L1 https://developer.apple.com/documentation/devicecheck/validating-apps-that-connect-to-your-server】

档位表（**逐字照抄文档，不加解释性推断**）：

| 值 | Apple 原文描述 |
| --- | --- |
| `0` | "Invalid." |
| `1` | "An operating system executable." |
| `2` | "An executable distributed through TestFlight." |
| `3` | "An executable signed by a development code signing identity." |
| `4` | "An executable distributed through the App Store." |
| `5` | "An executable distributed using an Enterprise Universal Provisioning Profile, or ad-hoc distribution." |
| `6` | "An executable signed using Developer ID." |
| `7, 8, or 9` | "These values aren’t appropriate as they represent categories of binaries that the system generates in certain restricted situations." |
| `10` | "An executable signed with a code signing identity that doesn’t match any other category." |

同一批扩展字段里另有 `apple_bundle_version_01`（`String`），用于校验分发出去的 App 版本号。两者合起来，就是 Apple 公开口径里**最接近"识别被篡改/非商店副本"的机制描述**：它把"签名身份类别 + 分发版本号"作为证明中的可校验字段交给服务端，而不是给出"是否被篡改"的结论。

三条使用纪律（也是三条不得越界的地方）：

1. **档位与描述只能照抄，不得推断"重签名一定落 `10` 档"。** 正确措辞是："Apple 在证明里额外报告签名身份类别，服务端可据此自行制定策略。" Apple 未给出该字段与任何具体篡改手法的对应关系。
2. **策略归服务端，阈值归业务。** "只接受 `2`/`4` 档""对 `3`/`5` 档降级放行"这类规则都是业务侧组合，Apple 未提供推荐值，也未承诺某档必然代表良性或恶性。这与第 8 章 risk metric "you’ll have to tune your risk assessment logic" 的口径一致。
3. **代码签名在内核层如何被强制，归《iOS 内核安全机制深度解析》**；本篇只把签名类别当作 App Attest 证明里的一个可校验字段引用，并复用该篇已确立的"源码可观察 ≠ Apple 承诺"口径。

**Apple 没有说的部分（本节核心缺口）。** 文档未公开说明该机制**无法防住哪些情形**。本次核查到的缺口如下，一律按"Apple 未公开"表述，不得写成"不存在风险"或"已被证实可绕"：

| 缺口项 | 表述纪律 |
| --- | --- |
| App Attest 的攻防胜率、被绕过案例 | Apple 未公开；文档只给原则性上限句（12.2） |
| 对具体越狱工具 / 重签名方案的结论 | Apple 未公开 |
| 重签名副本必落哪一档位 | Apple 未公开；只能按"签名身份类别"表述 |
| 各档位的服务端推荐处置策略 | Apple 未公开，完全归开发者（"it’s your responsibility to decide how to handle the request"） |
| `isSupported` 为 false 时的替代判定手段 | Apple 未提供；官方建议是 App 与服务端**降级放行**（服务端 "can no longer require assertions"） |
| 支持机型枚举、模拟器环境下的行为 | Apple 未公开（DeviceCheck 全部文档页无 `simulator` 字样） |
| 私钥曲线/密钥长度、`seKey` 术语 | 文档未出现；只给 77 字节 COSE 公钥与 ≥16 字节 challenge；`seKey` 非 Apple 文档术语 |

其中第 5 行是覆盖面层面最硬的约束：`isSupported` 为只读属性，Mac 上的 App（含 Mac Catalyst 与在 Apple silicon 上运行的 iOS/iPadOS App）读到的值为 `false`，此时 Apple 明确要求服务端**不得把断言作为必要条件**。也就是说，**任何以 App Attest 为唯一防线的方案都存在 Apple 官方认定的覆盖盲区**，而盲区里没有任何平台级替代判定。扩展类型支持上 Apple 文档内部口径不一致（一处列 Action、extensible SSO 与 watchOS 扩展受支持，另一处称仅 watchOS 9+ 扩展可用且 `generateKey` 在任意 app extension 中失败），本篇按纪律**并列两处原文、不择一采信**。

## 12.4 企业证书分发：Apple 一手机制口径、吊销规则原文与它不公开的那一半

**这一节只写到"机制存在与规则原文"这一层。** 企业证书的分发操作步骤、规避吊销的具体做法不在本篇收录范围内；本节所有表述均为"Apple 公开规则 + Apple 公开机制 + 可核实事件"三类。

为什么企业证书要放在"被篡改 App"这一章收尾：12.1 的威胁模型原文把对手行为定义为"modifies your app and **distributes it outside the App Store**"——后半句是一条**分发通道**属性，而不是包内容属性。Apple Developer Enterprise Program（ADEP）正是这样一条合法的商店外通道，因此"这条通道在规则上被限定成什么、Apple 用什么机制让它失效"就构成了平台侧对被篡改副本的上游约束。

**一、用途边界（Apple 一手规则原文）。** Apple Developer Enterprise Program（ADEP）页把该计划限定为内部使用：「allows large organizations to develop and deploy proprietary, internal-use apps to their employees」，并要求「Use the program **only** to create proprietary, in-house apps **for internal use**, and to distribute these apps **privately and securely to employees within the organization**」，组织「must have systems in place to ensure **only employees can download** your internal-use apps, and to protect membership credentials and assets」【L1 https://developer.apple.com/programs/enterprise/】。

准入与持续审查（同一页）：**100 名及以上员工**、须为法人实体（不接受 DBA/虚拟主体/分店）、需 D-U-N-S 编号与与组织关联的公开网站域名、会员年费 **299 USD**；「Participate in and pass Apple’s **verification interview and continuous evaluation process**」；续约时「your organization will be **verified again**」；「Apple reserves the right to reject your application **at its sole discretion**」。该页同时把 ADEP 定位为 App Store、Apple Business Custom Apps、Ad Hoc、TestFlight 都无法满足时才使用的兜底通道——**这正是"企业证书天然是高滥用面"的一手依据**：它的信任授予对象是"组织内部"，而分发技术本身并不区分内部与外部。

**二、吊销如何生效（Apple 一手部署指南，Published Date: September 17, 2026）。**

| 机制 | 原文口径 |
| --- | --- |
| 首启动校验 | 「The first time a user opens an app, the distribution certificate is validated by **contacting the Apple OCSP server**. If the certificate has been revoked, **the app won’t launch**」；设备网络需可访问 `ocsp.apple.com` |
| 缓存窗口 | 「The OCSP response is **cached on the device** for the period of time specified by the OCSP server—**currently between three and seven days**. The revocation status of the certificate isn’t checked again until the cached response has expired.」 |
| 连坐式吊销 | 「**Revoking a distribution certificate invalidates all of the apps you’ve signed with it.** You should revoke a certificate only as a last resort—if you’re sure the private key is lost or you think **the certificate has been compromised**.」 |
| 分发侧限制 | 需以 in-house 描述文件系统签名、经 HTTPS 站点与 `itms-services://` 清单安装；「Their network configuration needs to allow devices to access a server at Apple」；iOS/iPadOS 18 及以后，**不经设备管理服务手动安装**的 in-house 应用需重启设备才能完成对描述文件的信任 |
| 有效期口径 | 分发描述文件有效期 12 个月；发行证书自签发起 3 年或随会员到期（以先到者为准）；**已签名应用不受发行证书到期影响** |

最后一行要与"吊销"严格分开：**到期与吊销是两件不同的事**——前者不影响已签名应用的运行，后者会让应用无法启动。把两者混写成"证书过期导致应用失效"是常见口径错误。

**三、交叉印证（Apple 平台安全指南，August 2026 版）。**「On first launch of any proprietary in-house app, **the device needs to receive positive confirmation from Apple that the app is allowed to run**」；同页写明经设备管理服务安装的应用被**隐含信任**，否则用户需在"设置"中手动信任描述文件，组织亦可禁止用户信任未知开发者应用。Apple 支持文档另有一手确认：首次打开手工安装的企业应用会出现"该 App 的开发者在你设备上不受信任"的提示【L1 https://support.apple.com/en-mo/118254】。

**分析推断（非 Apple 口径）**：把一手规则并列起来可以看到一件事——企业通道的信任链上存在一个由人完成的确认环节（手动安装时需用户在"设置"里信任描述文件），而经设备管理服务安装则跳过该环节。因此"是否由 MDM 通道安装"决定了这条链上是否有可被人误点的位置。本篇只作机制层描述，不讨论该环节被利用的方式。

**四、两个已被公开的失效条件。** 一是**缓存窗口**（3–7 天，Apple 一手）；二是**设备无法回连 Apple 校验端点**——同行评审研究明确记述存在"通过阻断设备访问 Apple 证书校验端点（`ocsp.apple.com`、`crl.apple.com`）使已吊销证书签名的应用继续可用"的做法。**本篇只记录该结论的存在性，不写任何实现方式。** 二者共同说明：Apple 的吊销是**最终一致**而非即时生效。

**五、半推翻的口径（本节最重要的纠正项）。** 预期说法"Apple 自己公开过吊销企业证书的机制与若干公告"——**机制一半成立，公告一半不成立**。机制有明确一手文档（见二、三）；但**Apple 不发布吊销公告，也不发布吊销数量统计**，其公开表述方式只有"规则 + 吊销后果 + 准入审查"。正确写法：**"Apple 未公开该口径数据"**，不得反向断言"Apple 不吊销证书"或"吊销规模很小"。可核实的公开吊销事件**只能落在媒体报道层面，未见 Apple 一手公告**：2019 年 1 月有报道称 Apple 吊销 Facebook 企业证书；2019 年 2 月的另一篇报道称 Google 企业证书同期被阻断**后恢复**。两处的"是否恢复"口径不一致，本篇按原文双述，不合并、不做推断。另有媒体报道称 Apple 于 2025 年 2 月对特定地域的开发者关闭 ADEP 准入——同样只可用于"Apple 会按地域政策收紧准入"这一存在性表述，**未见 Apple 一手公告**。更晚的若干企业证书投放事件本次仅完成事件级定位、未取得可直接引用的原文页面，故不在本章展开。

**本章口径边界**

- 本章的 Apple 口径止于 12.1 清单所列场景词；`bot`、`automation`、`emulator`、`device farm`、`jailbreak` 在 DeviceCheck/App Attest 文档中不存在，不得写成 Apple 已考虑的威胁面。
- "Apple 未公开 X"是本章统一的缺口写法；不得写成"不存在 X"，也不得据第三方推断补全（尤其"App Attest 无法防模拟器/越狱"这类句子没有一手依据）。
- 12.2 两句越狱侧原话必须带"Apple 原文"限定；"an unlikely source of widespread fraud" 标注为 Apple 文档自评、非测量结论，不得据此降低风险等级。
- 12.3 的 `validationCategory` 档位表只能照抄，不得推断重签名落哪一档；`isSupported` 一律写作只读属性。
- 12.4 只写机制与规则原文，不写任何证书滥用、绕过分发校验的操作步骤；ADEP 与 OCSP 数字标注来源与发布日期（部署指南 2026-09-17；平台安全指南 2026-08 版）。
- 越狱与绕过手法的机制层归《iOS 系统防御与机制绕过》；供应链侧证书滥用谱系归《iOS 供应链安全分析报告》第四章；本章只取 Apple 一手规则与一手机制口径。

---
# 越狱攻防的历史脉络：可核实的术语边界与不能写的断言

## 13.1 为什么要先立术语边界：本篇只写能力存在性、失效结论与证据强度

第 1 至 12 章把「设备真实性」这条防线拆成了四层：平台提供的密码学可验证信号（DeviceCheck 两位状态位、App Attest 的密钥证明与断言、Managed Device Attestation 的属性签发）、这些信号的官方语义边界，以及 Apple 在文档里明文交还给开发者的判定责任。从本章起，叙述对象换成另一侧：**这条防线的对手是什么、关于对手的公开说法里哪些可核实、哪些不能写**。

这一侧的写作风险与其他章节不同。越狱、检测绕过、重签名、灰产分发这些主题，任何一段"具体怎么做"的描述，其本身就已经是操作指引；把它们写进一份公开流转的技术报告，与本报告的安全边界直接冲突。因此本篇第四部分自始至终只承认三类可写入的信息：

| 允许写入 | 含义 | 本篇的写法约束 |
| --- | --- | --- |
| 能力存在性 | 某类绕过、某类改造、某类通道**是否存在**，由谁在何口径下说出 | 只写"存在/被记述/被自认"，不写成立条件与操作路径 |
| 失效结论 | 某项防线在特定条件下**不成立** | 必须同时给出失效条件（是否需已越狱设备、是否需运行期替换、是否因缺少服务端校验） |
| 证据强度 | 该结论来自哪一级来源、样本多少、作者自陈什么局限 | 每条附等级标注；等级 5 不得作为事实来源，等级 3 的自评一律降级为营销 |

反过来，以下六类内容本篇一律不收录，也**不因读者要求而收录**：越狱步骤或工具用法；绕过检测的实现路径；重签名与二次打包的流程；检测点位清单与 hook 点位；设备农场/群控的搭建方式；伪造证明、伪造设备标识或规避风控的做法。凡某条信息"说出来就等于教程"，本篇只记结论，并在出现处标注**「正文不记实现」**。这条纪律不是排版选择，而是本篇与其他章节的分工前提：机制层与绕过现象的描述已由系列内既有篇目承担，本篇的职责是**评估这些机制的效果与证据等级**，因此本篇不需要、也不应该复现任何操作细节。

还有一个只有本章要处理的问题：**这一领域的中文与英文流通语汇里，有大量看起来像事实、实际无出处的断言。** 例如某个越狱形态的"专有名词"、某个芯片代际"绝对没有越狱"的边界、某个灰产市场规模的具体金额、某个检测库"普遍可绕"的总结。这些句子在读者认知里已经稳定存在，但按本篇的六级来源体系去回溯，多数只能落到社区 wiki（等级 5）或厂商营销材料（等级 3 上限）。本篇第四部分的主要工作因此不是"补充更多对抗细节"，而是**把这些句子逐条退回它们能被支撑的位置**：能落到同行评审研究的，写明样本与年份；只能落到社区口径的，标注为社区记述且不作事实来源；完全找不到出处的，直接声明删除并解释删除理由。13.3、13.5、14.5、15.5、16.6、17.1 这六节，本质上都是同一件事的不同实例。

## 13.2 可核实的社区形态术语及其来源等级

社区按**重启后是否需要重新触发利用**这一条轴，把公开越狱分成若干形态；这是本节唯一要使用的形态描述，且只作历史脉络的一句话引用。可核实的术语集合有明确边界：tethered、semi-tethered、semi-untethered、untethered，以及另一条正交轴上的 rootful 与 rootless。

| 术语 | 分类轴 | 可核实出处 | 来源等级 | 本篇允许的用法 |
| --- | --- | --- | --- | --- |
| tethered / semi-tethered / semi-untethered / untethered | 重启后是否需重新触发利用 | The Apple Wiki「Jailbreak」词条【L5 https://theapplewiki.com/wiki/Jailbreak】；Wikipedia「iOS jailbreaking」条目的分类段落【L5 https://en.wikipedia.org/wiki/IOS_jailbreaking】 | 等级 5（社区维护 wiki / 三级百科） | 只作"社区曾按重启后是否需重新触发利用把公开越狱分为若干形态"这一句历史注解 |
| rootful / rootless | 是否修改系统卷（读写隔离之下的root可见性） | 同上两处社区文档【L5】；学术侧仅 HALY 在实验设置里借用该层口径【L2 https://martina.lindorfer.in/files/papers/haly_eurosp25.pdf】 | 社区等级 5；学术借用等级 2 | 可用于说明"rootless 使基于系统路径特征的检测失效"这一命题已进入同行评审议程（见 14.4），不用于形态学表格 |

三处必须定住的限制。

**第一，这套形态术语不是学术术语体系。** 本次核查在同行评审论文中**未找到**以 tethered/untethered 这套词作为分类框架的学术定义；最接近的学术用法是 HALY 在实验设置一节直接自述其设备所用方案属于 rootful 一类，即只借用 rootful/rootless 这一层口径，没有把它发展成分类型【L2 EuroS&P 2025】。因此如果正文把这套词排成一张分类学表格，就等于给一个等级 5 的社区词汇表赋予学术外观——这正是本篇要禁止的写法。上表之所以只给"用法列"而不给定义细节，原因也在此。

**第二，Apple 官方文本不使用这套词，也不使用"越狱"本身。** 一个容易被忽略但影响全篇的事实：Apple 平台安全指南（August 2026 版，本地全文核对）出现 `jailbreak` 一词 **0 次**【L1 https://help.apple.com/pdf/security/en_US/apple-platform-security-guide.pdf】；其口径是 `untrusted code`、`unsigned apps`、`compromised`，分发口径则是 App Store / Ad Hoc / TestFlight / ADEP in-house。DeviceCheck 文档里对应"系统被改动"的官方表述是 `a device with a compromised operating system`【L1 https://developer.apple.com/documentation/devicecheck】。这条术语落差有直接后果：**读者熟悉的"越狱"在 Apple 官方判定文本里根本没有对应条目**，所以任何"Apple 已防御越狱"或"Apple 明确处理越狱"的句式都必须改写成"Apple 在 X 文档里对 Y 机制做了什么"。首次出现处需说明：本篇使用"越狱"一词指称社区与论文用法，Apple 文档不使用该词。

**第三，中文语境还有三个词常被混进同一层。** "越狱"不等于"解锁"（unlocked，在中文报道里常指运营商锁，在 Android 语境里另指 bootloader 解锁），也不等于论文所称的"修改/魔改版应用"（modified apps）。三者被混写时，会产生"某机型已解锁因此已越狱"这类既无依据也无意义的推断。本篇的处理办法是把它们分派到不同层：越狱只出现在本章的时代口径里，解锁不出现，modified app 作为第 16 章的对象术语（其定义见 16.4）。

**本章口径边界**

- 本章所有形态词一律标注等级 5，并只作一次性历史注解；不得扩写为分类学表格，不得写实现（**正文不记实现**）。
- Apple 文档不使用"越狱"与上述形态词，是本章引用 Apple 口径时的硬约束；任何"Apple 已防住越狱"句式不予采用。
- "越狱"≠"解锁"≠"改造版应用"，三词分属不同章、不同层，不得互相指代。

## 13.3 `corebaseless`：一个必须从骨架里删除的术语

预期说法里把 `corebaseless` 列为越狱形态之一。本节记录核查结果与处置决定，因为它是本篇"流通语汇与可核实事实脱钩"最干净的一个样本。

**核查结果：未获得可核实条目。** 本次在多轮检索中，**未在任何社区维护文档、百科条目或同行评审论文中找到拼写为 `corebaseless` / `corebaseless jailbreak` 的形态定义**。可核实的近似术语只有 13.2 所列的两组（tethered 系列与 rootful/rootless）。按简报纪律，这属于**等级 6（无法定级：原文取不到，仅有搜索摘要）**，一律写入缺口清单，不进入正文。疑为该说法是对社区口径的误记或混淆——最可能被混淆的对象是两个真实存在的相邻概念：其一是 Apple 文档与论文里的 `CoreTrust`（代码签名信任策略模块），其二是 rootful/rootless 这层关于系统卷读写口径的区分。但**混淆的具体路径无法核实，本篇不推测填充**。

**处置决定（三项）。** 术语本身：从本篇术语表、分类讨论与下游图表中**删除**（等级 6，无任何可引用出处）。替代骨架：分类骨架改用 USENIX Security '26 的通道谱系表（见 13.4，等级 2 同行评审口径，且带机制类别与生命周期两栏）。记录位置：保留本节，写明"该拼写无出处、已删除"——沉默处理会让该词在后续版本复活，显式记录才能阻断传播。同时须区分两个真实存在的相邻概念以免误替换：`rootless` 在 iOS 语境里指"不修改系统卷"的越狱形态，与 Android 的 root、macOS 的 SIP 关闭**不是同一件事**；`untethered` 与中文报道里的"永久越狱"也不严格等价（后者的表述已接近形态实现细节，本篇不展开）。

**本章口径边界**

- `corebaseless` 全篇不出现于任何分类表述；本节是其唯一出现处，且以"无出处、已删除"的身份出现。
- 等级 6 条目一律不进正文事实层，只进缺口与更正记录；不得以"最接近的近似概念"名义把它换个写法塞回正文。
- 对无法核实来源的术语，本节确立处理范式：删除该术语 + 保留删除记录 + 给出替代骨架，三者缺一不可。

## 13.4 分类骨架改用同行评审的通道谱系表

删掉社区术语表之后，本章需要一个**有等级支撑的分类骨架**。本次核查找到的唯一一份把越狱与其他绕过通道并列、并同时给出机制类别与生命周期的学术材料，是 USENIX Security '26 论文第 2 节的 Table 2「Common sideloading methods」【L2 USENIX Security 2026 https://www.usenix.org/system/files/usenixsecurity26-liu-yijing.pdf】。下表按该表照录机制类别栏与有效期栏的口径。

| 通道（照录论文条目） | 论文给出的机制类别 | 论文给出的有效期 | 本篇的使用方式 |
| --- | --- | --- | --- |
| In-House（企业开发者证书） | 企业开发者证书签名分发 | 1 年 | 第 15 章主题；论文口径为「was once common but is now tightly regulated」 |
| AltStore | 免费开发者账号 | 7 天 | 只作通道存在性与生命周期口径 |
| TestFlight | 官方 Beta 通道 | 90 天 | 与第 12 章 `validationCategory` 中「经 TestFlight 分发的可执行文件」一档呼应 |
| Jailbreak | 论文原文机制栏："Root access via iOS vulnerabilities" | Permanent | 本章唯一可用的"越狱"学术定位：**一条以系统缺陷获取 root 的通道，被与其他分发通道并列** |
| TrollStore | 论文原文机制栏："iOS CoreTrust exploit" | Permanent | 论文对版本边界给出可引用表述：「is attractive for sideloading but **is limited to older iOS versions**」 |
| WebClip | 主屏快捷方式 | Permanent | 第 16 章引用其作为分发载体的测量文献 |
| Ad Hoc | 个人开发者证书 | 1 年 | 第 16 章全部数字的口径边界（论文只覆盖这一条通道） |

四点解读，都需要按来源等级来读。

**第一，这份表的价值不在"列出了七种方法"，而在它给出了一个同行评审口径的分类轴。** 社区术语表的分类轴是"重启后是否需要重新触发利用"，它对防线设计毫无用处（防线不关心重启行为）；而这张表的分类轴是**通道 + 机制类别 + 生命周期**，三栏全都能直接对接风控问题：这条通道是否依赖 Apple 签发的证书（是则可被 Apple 吊销）、其有效期由谁决定（Apple 期限、证书 12 个月/3 年、还是永久）、以及它是否落在 Apple 的分发口径内（In-House/Ad Hoc/TestFlight 在 Apple 文本里有名字；越狱与 TrollStore 没有）。**这也是本篇优先使用等级 2 与等级 1 口径、把社区术语降为一次性历史注解的实质理由。**

**第二，论文对两条"永久"通道的评价句同样是可引用的等级 2 表述。** 对越狱：「While effective, it **introduces severe security risks and is impractical for most users**」；对企业证书通道：「once common but is now tightly regulated」。这两句合起来给出一个对第 16、17 章很关键的判断：**在同行评审口径里，越狱被描述为对多数用户不可行的通道，而非默认路径**——这是"iOS 侧对抗面证据稀疏"的另一条间接依据，且它是**研究者的表述**，不是风险的测量。

**第三，有效期栏不能读成"保证可用时长"。** 表中的 1 年、7 天、90 天是 Apple 侧证书/描述文件/测试通道的时间限制，Permanent 指不存在这类时间限制；两者**不等价于**"该副本在此期间必然可运行"。第 15 章会给出一个具体的反例：经企业证书分发的应用，其可运行性取决于首启动时能否从 Apple 取得正面确认，而该结果被缓存 3–7 天【L1 https://support.apple.com/en-il/guide/deployment/depce7cefc4d/web，Published Date September 17, 2026】。也就是说，**生命周期是"证书/描述文件的期限"，不是"应用可用时长的上界"**，两者混写是本领域最常见的口径错误之一。

**第四，术语提醒仍然要写在表下。** 该表条目（AltStore、TrollStore、WebClip）是社区与论文通用的**通道名**，本篇引用它们只为定位"哪条分发路径"，不指涉任何具体实现或工具用法；凡"该通道具体如何走通"的问题，一律属**正文不记实现**范围。企业证书滥用的通用谱系与第三方商店的政策面已由《iOS 供应链安全分析报告》第四章承担，本篇不重述，只取该表的通道分类与第 16 章的同行评审测量。

## 13.5 「A12 及以上长期缺少公开越狱」：三条间接依据与唯一正确写法

这是第四部分风险最高的一句断言，也是最容易被写坏的一句。核查结论是：**该说法可以得到间接的权威二级支撑，但不能写成绝对断言，也不能写成具体芯片边界断言。**

**一、可核实的间接依据恰好三条，且都是"上界"性质的证据。**

| 序号 | 依据 | 原文口径 | 等级 |
| --- | --- | --- | --- |
| 依据 1 | HALY（EuroS&P 2025）实验设置自述 | 实验设备为 iPhone 8（A11 芯片）、iOS 16.4.1，采用其自述的 rootful 越狱；190 个（5.7%）iOS 应用因需在已越狱设备上做内存转储而无法解密；部分应用因版本不兼容无法安装，并明确写「**our jailbreak was limited to iOS 16**」 | 等级 2【https://martina.lindorfer.in/files/papers/haly_eurosp25.pdf】 |
| 依据 2 | USENIX Security '26 对 TrollStore 的版本表述 | 「is attractive for sideloading but **is limited to older iOS versions**」 | 等级 2【https://www.usenix.org/system/files/usenixsecurity26-liu-yijing.pdf】 |
| 依据 3 | ESORICS 2025 International Workshops 论文议题 | 以 **rootless 越狱**为条件重新评估 iOS 银行应用的越狱检测有效性 | 等级 2（workshop 后论文集，强度低于主会）【https://dl.acm.org/doi/10.1007/978-3-032-16165-9_33】 |

三条合起来能支撑什么、不能支撑什么，必须写死。**能支撑**：公开可用的绕过通道存在明确的**版本/机型上界**，且这个上界会随时间把大样本研究排除在外（依据 1 尤其直接——它是一位研究者"在 2023 年版本口径下能用于大规模应用分析的越狱只覆盖到 iOS 16"的自述，是可核实的**研究可行性**陈述）。**不能支撑**：任何以芯片代际为单位的绝对边界。

**二、为什么"checkm8 只影响 A7–A11，所以 A12 及以后没有可用越狱"这条推理链在本篇不能用。** 该推理的前提本身在本次检索中**未取得 Apple 一手或学术来源直接表述**；可检索到的是社区与二手转述（等级 5）。更要紧的是**边界提醒**：Apple 平台安全指南这类一手文档从来不会以"某芯片代是否有公开越狱"的口径发言，所以这一项**天然只能由等级 2/3/4 支撑**，而本次没有拿到任何等级 2 材料直接说出这条边界。同理，「截至某年某月 A12+ 无任何公开越狱」这类**时间边界**也未取得可核实条目——相关工具页与社区状态页属等级 5，不作为事实来源。按纪律不推测填充。

**三、唯一正确写法。** 本篇采用下面这句，并把它同时当作本章结论与第 17 章的入口：

> 按公开研究与其自述的实验条件，iOS 侧公开可用的绕过通道存在明确的系统版本与机型**上界**（有等级 2 支撑）；这构成"iOS 对抗面证据稀疏"的原因之一。本报告不断言任何芯片代际"绝对没有"公开越狱，也不给出该说法的时间边界。

对应的禁用句式清单：

| 禁用写法 | 问题 | 应改为 |
| --- | --- | --- |
| 「A12 及以上长期缺少公开越狱」 | 绝对断言，无一手/学术直接依据 | 「公开研究自述可用的绕过通道覆盖到其论文所示的版本上界」 |
| 「A12 以上绝无越狱」「彻底没有」 | 简报明令禁止的措辞 | 「按公开研究与公开工具口径，截至论文自述时点」+ 上界表述 |
| 「checkm8 只影响 A7–A11，故新机型安全」 | 前提为等级 5，且推论超出前提 | 删除，或改写为"社区与媒体普遍如此记述，本篇未取得可核实来源" |
| 「因为无法越狱，所以某风险在 iOS 上不存在」 | 把可观测性当风险量 | 交回 17.2 的口径声明：证据密度低 ≠ 风险低 |

**四、为什么这条断言必须在报告里被退回。** 它与本篇核心论点直接冲突。第 3 至 12 章确立的口径是：Apple 把不可信设备的判定责任交回开发者，只提供信号；而"新机型无法越狱"如果被写成事实，读者会推出"所以服务端不需要为被改动过的操作系统做处置"——这句话与 Apple 文档的原文正面相撞：「**an attacker that modifies the device's operating system might bypass restrictions**」【L1 https://developer.apple.com/documentation/devicecheck/assessing-fraud-risk】。厂商自己承认存在这条绕过语义，报告不能拿一个等级 5 的芯片断言去把它抵消。风险结论只能落在"公开可用的绕过通道有多窄"这一层，不能落在"绕过是否存在"这一层。

## 13.6 时间锚点、叙述边界与「正文不记实现」的处置

本章到此只完成了一件事：把可用的口径与可用的时间锚点定住。下面这张锚点表是本报告后续所有章节引用"时代"时允许使用的全部时点，凡表中未列的年份与版本组合，一律视为未核实。

| 时间锚点 | 可核实内容 | 等级 | 出处 |
| --- | --- | --- | --- |
| 2015 | Evans 等把 root 检测自身作为被测对象（16 个安全类应用 + 19 个 MDM 应用，Android 与 iOS） | 2（转述核对） | HALY 相关工作一节【https://martina.lindorfer.in/files/papers/haly_eurosp25.pdf】 |
| 2018-10-30 / 2018-11 | Kellner 论文写作时点；其时最新 iOS 版本为 iOS 12.1，作者称该版本一周内"有越狱被宣布并公开展示" | 2 | 【https://intellisec.org/research/ios/2019-eurosp.pdf】。**这是 2019 年论文的当时口径，不可外推为当前状态** |
| 2019 | Kellner EuroS&P 论文发表：34 个银行应用口径（见 14.2） | 2 | IEEE 记录页文档号 8806743【https://ieeexplore.ieee.org/document/8806743】 |
| 2021-09 | iOS 15 发布日期。HALY 的"陈旧化"结论以此为界：该版本及之后发布的越狱，未发现专用特征检测 | 2 | 【https://martina.lindorfer.in/files/papers/haly_eurosp25.pdf】 |
| 2023-03 / iOS 16.4.1 | HALY 实验机型与系统版本（iPhone 8 = A11）；其越狱覆盖上界为 iOS 16 | 2 | 同上 |
| 2025 | HALY（EuroS&P 2025）发表；ESORICS 2025 workshop 出现以 rootless 越狱为条件的复测 | 2 | 会议程序页 https://eurosp2025.ieee-security.org/program.html ；https://dl.acm.org/doi/10.1007/978-3-032-16165-9_33 |
| 2026-08 | USENIX Security '26 论文（Baltimore，2026-08-12~14）给出通道谱系表与灰产测量 | 2 | 【https://www.usenix.org/conference/usenixsecurity26/presentation/liu-yijing】 |

**关于"越狱群体是谁"，本章要更正一条预期说法。** 常见叙述把越狱用户描述为小众极客。可核实的等级 2 材料给出的却是相反方向的表述：Kellner 2019 摘要原文写「jailbreaks **gain tremendous popularity among regular users** for customizing their devices」。同一论文转引 Cydia 方面"超过 3,000 万设备使用其系统"的说法——该数字是**服务提供方自述、无方法学**，本报告标注为**不可用于事实陈述**。因此本篇的处置是：既不写"小众极客"，也不写"数千万用户规模"，只写"同行评审论文在其写作时点把越狱描述为已具有大众用户基础的定制行为，其规模无可核实测量"。这个更正对防线设计的意义会在第 16 章显出：如果对手画像被写成"少数极客"，灰产基础设施（证书站点、密钥池、预付码与"吊销保险"）就没有位置可放；而 USENIX '26 的测量恰恰说明**这条路径上的主要能动性属于商业化服务链，不属于个人**。

**形态描述的最终处置。** 13.2 的社区形态词、13.4 的通道机制类别，在本篇只承担**历史脉络与分类口径**两个功能。凡某条信息"说出来就等于教程"——包括形态如何达成、通道如何走通、检测点位如何选取、绕过组件如何加载——本篇**只记结论，正文不记实现**。具体到越狱链本身：若干代际攻击链的技术构成与 CVE 组合，已由《iOS 典型漏洞攻击链安全分析报告》第 5 章逐条承担，越狱里程碑案例叙事已由《著名 iOS 漏洞案例》第二章承担；本篇只引用它们作为时间锚点，不重述任何链条与版本细节。检测点位与绕过手法的机制描述归《iOS 系统防御与机制绕过》第 11 章；本篇只做这些机制的**效果评估与证据分级**，两侧互不重复列举。

还有一条与社区规避组件相关的结论需要以"存在性"方式保留：Kellner 2019 在其相关工作一节记述，社区中**存在**用于压制此类检测的专用规避组件；HALY 2025 的采用面测量与之相互印证。按纪律，本篇只写这条存在性结论与其限定条件——**论文自陈此类组件多数绑定特定越狱与特定 iOS 版本、且不开源**——不写组件名称、不写加载方式（**正文不记实现**）。"绑定特定版本且不开源"这一限定不是附注，它本身是一条证据强度判断：这类组件的流行度无法被独立测量，因此任何"绕过已成规模"的量化说法都没有可复核来源。

**本章口径边界**

- 可核实的社区形态术语只有 tethered / semi-tethered / semi-untethered / untethered 与 rootful / rootless，全部为等级 5，只作历史脉络一句话引用；**`corebaseless` 无出处，已删除**。
- 分类骨架以 USENIX Security '26 Table 2（等级 2）为准；Apple 文档不使用"越狱"及任何形态词，其分发口径只有 App Store / Ad Hoc / TestFlight / ADEP in-house（等级 1）。
- 「A12 及以上长期缺少公开越狱」不写为绝对断言、不写为芯片边界断言；唯一可用句式是"公开可用绕过通道存在明确的版本/机型**上界**（等级 2 支撑）"。
- "越狱群体属小众极客"不予采用；Cydia 自述的 3,000 万设备标注为**不可用于事实陈述**。
- 所有越狱可用性表述统一加限定语：**按公开研究与公开工具口径，截至论文自述时点**；不得使用"绝无 / 彻底没有"。
- 本章零实现内容：不写步骤、不写工具用法、不写检测点位、不写绕过组件名称（**正文不记实现**）。
- 越狱链技术构成归《iOS 典型漏洞攻击链安全分析报告》第 5 章，案例编年归《著名 iOS 漏洞案例》第二章，检测与绕过机制归《iOS 系统防御与机制绕过》第 11 章；本章只承担时代口径与证据分级。

---

# 检测能不能站住：公开研究的结论与口径差异

## 14.1 本节要防止的一句错话，与可用的研究清单

本章要防止的错话只有一句，但它在本领域的普及率极高：**"研究表明越狱检测普遍无效。"** 这句话的方向没错，作为报告结论却不合格——它把四篇样本、判据、年份与结论强度各不相同的研究压成了一个无法回溯的断言，也让读者无法判断"无效"到底指什么对象的无效。核查结论（简报推翻项之一）明确写着：论文测的是**应用内实现与其运行时行为**，**不是商业检测库产品**；"普遍可绕"是 Kellner 2019 在其自身 hook 能力条件下得到的结论。因此本章按"逐年、逐口径"的方式引用，禁止合并成一句。

可用研究清单如下。凡未列入本表的材料，本章不使用。

| 研究 | 年份/ venue | 对象与样本 | 判据 | 结论方向 | 等级 |
| --- | --- | --- | --- | --- | --- |
| Evans, Benameur, Shen, "All your root checks are belong to us: The sad state of root detection" | 2015，ACM Mobility 系列 | 16 个安全类应用 + 19 个 MDM 应用（Android 与 iOS） | 检测**流行度**（是否使用） | 安全应用 13 个、MDM 应用 15 个使用 root 检测 | 2（**部分核实**：标题作者经 Kellner 参考文献 [55] 与 HALY 双重核对；本次未取原文 PDF，样本口径依 HALY 转述） |
| Kellner, Horlboge, Rieck, Wressnegger, "False Sense of Security: A Study on the Effectivity of Jailbreak Detection in Banking Apps" | 2019，4th IEEE EuroS&P | 银行类 34 个（其中 18 个实现检测）；跨类别 3,482 个最流行应用 | 应用内检测实现 + 现成函数 hook 条件下的**绕过结果** | 34 个银行应用中除 1 个外全部可被完全攻破；试图检测者除 1 个外全部检测失败 | 2【https://intellisec.org/research/ios/2019-eurosp.pdf ；https://ieeexplore.ieee.org/document/8806743】 |
| Steinböck, Troost, van Beijnum, Seredynski, Bos, Lindorfer, Continella, "SoK: Hardening Techniques in the Mobile Ecosystem — Are We There Yet?"（工具 HALY） | 2025，IEEE EuroS&P | 3,322 跨平台热门池 → iOS 与 Android 各 2,646（合计 5,292） | 静态特征 + **动态执行**双口径 | 静态 96.7% vs 动态 24.9%；检测清单陈旧化；整体自防护"不足以说明有效性" | 2【https://martina.lindorfer.in/files/papers/haly_eurosp25.pdf ；实现 https://github.com/utwente-scs/haly-hardening-analyzer】 |
| Irfan & Uto, "Revisiting the Effectiveness of Jailbreak Detection" | 2025，ESORICS International Workshops（Springer LNCS，pp. 558–577） | iOS 银行应用；以 **rootless 越狱**为条件 | 未取得全文，判据不明 | 只可引其研究问题与方向；**任何百分比不得引用** | 2（workshop 后论文集，强度低于主会；**部分核实**）【https://dl.acm.org/doi/10.1007/978-3-032-16165-9_33 ；卷首 https://link.springer.com/content/pdf/10.1007/978-3-032-16165-9.pdf】 |
| Sun 等（Android 对照） | 年份未在本次核实 | 182 个 Android 应用、多种 root 检测技术 | 有效性 | 「这些方法无效」 | 2（**转述**，依 HALY；不得写成 iOS 结论） |

**三点使用说明。第一，等级逐项标注，且厂商自评不进这张表。** 上表全部为等级 2。等级 3（MTD/RASP 与移动风控厂商技术报告）在本章只允许出现在两处：其一，"存在针对某机制的绕过研究"这类**能力存在性**陈述（简报设定的可信度上限：单源可用但须标口径）；其二，厂商承认检测会产生误报这一**工程事实**（见 14.5）。凡厂商对**自家产品有效性**的量化自评，一律降级为营销，不进入本章任何结论。

**第二，跨研究比较必须带"三件套"。** 样本池 / 判据（静态字符串 vs 运行时执行）/ 年份，三项缺一即视为口径错误。这一纪律的直接后果写在 14.3：iOS 越狱检测流行度的四个流通数字（Kellner 59%、HALY 静态 96.7%、HALY 动态 24.9%，以及 Android 侧的 61% 与 92.3%）来自四个不同样本与四种不同判据，**不可排成趋势线**。

**第三，本章只写三类信息。** 能力存在性、失效结论、证据强度。检测点位清单、hook 框架名称、绕过所用组件名称一律不进入正文——包括论文自己列举的那些名称。凡确需保留的，以"存在此类组件、多绑定特定版本、不开源"的形式记述（13.6 已确立此处置）。

## 14.2 Kellner 2019：样本、结论原文，以及结论生效的条件

Kellner 等 2019 年的 EuroS&P 论文是本章的核心一手，也是"检测普遍可绕"这句话唯一有资格被引用的来源——但引用它必须带上它自己的三项条件：样本、判据、以及作者所使用的绕过能力。

**样本与口径（原文自述）。** iOS App Store 银行类应用 **34 个**，其中 **18 个**实现了越狱检测；另跨全部应用类别抓取 **3,482 个**最流行应用做扩展分析。含某种形式越狱检测的应用比例为 **59%**，银行应用为 **53%**【L2 EuroS&P 2019 https://intellisec.org/research/ios/2019-eurosp.pdf】。这组数字里有两点要提醒下游写手：其一，59% 与 53% 两处口径由 HALY 2025 转述核对，两篇一致，可交叉引用；其二，**该论文内部计数不一致**——导言称"15 out of 34 银行应用完全不使用该措施"，结论称"only 18 out of 34 使用越狱检测"，两者相加为 33 而非 34。本篇的处置是：同时给出两处原文数字，或以摘要口径"44% 的银行应用连尝试检测都没有"表述，**不择一硬化为唯一事实**。

**失效结论（逐句可核，原文方向）。** 三条：「**34 个银行应用中，除 1 个之外全部可被完全攻破**」；「**试图检测越狱的应用，除 1 个之外全部检测失败**」；绕过手段是「现成的函数 hook（out of the box function hooking）」，作者强调「**既不需要逆向工程，也不需要修改应用本身**」。第三条是这项研究的证据强度所在：它把失效条件的门槛说得很低——**前提是攻击者已具备对该设备的运行期函数替换能力**（即以设备已被越狱为前置条件）。这句话必须与结论同段出现，否则"除 1 外全部可绕"会被读成"检测在正常设备上也可绕"，那是论文没有说过的。

**作者给出的机理性归因（可作论据，非实现细节）。** 三句都是防线设计的直接输入：「许多应用开发者直接从 StackOverflow 等社区平台**复制粘贴**越狱检测代码，使这些检测方案成为轻易的目标」；「由于普遍使用**简单检测策略**，检测经常无法成功」；「银行应用**并未超出全站平均**的检测使用率」。第三句尤其值得引用：它切断了"高合规行业必然有更严格设备校验"这一常见假设——至少在 2018 至 2019 年的观测口径下，金融行业在越狱检测上的投入并不高于全站平均。

**时间口径的硬限制。** 该论文写作时点为 2018 年 11 月，称当时最新 iOS 12.1（2018-10-30 发布）一周内即"有越狱被宣布并公开展示"。**这是 2019 年论文的当时口径，不可外推为当前状态**（13.6 锚点表已把这一条钉住）。同理，任何"今天越狱检测同样可绕"的句子在本篇都必须改写成"Kellner 2019 在其样本与 hook 能力条件下测得……，HALY 2025 在另一套静态/动态口径下……"，把时点与口径留在句子里。

**证据强度评级：高。** 同行评审（IEEE EuroS&P 2019，会议录用清单可查 https://www.ieee-security.org/TC/EuroSP2019/accepted.php ）、样本量明确、结论自限于作者所用检测点位与 hook 能力之下。这三项正好是后文引用它时必须复述的三项限定。

## 14.3 HALY 2025：静态 96.7% 与动态 24.9% 的口径修正

HALY（SoK: Hardening Techniques in the Mobile Ecosystem — Are We There Yet?, IEEE EuroS&P 2025）对本章的贡献不是"又测了一次检测"，而是**给"检出"这个词装了两套互不等价的口径**，并据此修正了包括它自己在内的前序结论。

**样本与口径。** 3,322 个跨平台热门应用池，最终按每个 OS 各 2,646 个（合计 5,292）做静态 + 动态分析；作者明确写其结果应视为**下界（lower bounds）**【L2 https://martina.lindorfer.in/files/papers/haly_eurosp25.pdf】。

**核心口径修正：静态字符串存在 ≠ 运行时真的执行检测。** iOS 侧的关键数字如下表。

| 指标 | 静态分析口径 | 动态分析口径 | 读法 |
| --- | --- | --- | --- |
| root/jailbreak 检测出现率 | **96.7%** 的 iOS 应用 | —— | 二进制里能扫到相关字符串或符号 |
| 运行时检查 root 相关文件/系统目录权限 | —— | **24.9%** | 执行期真的发生了一次可观测检查 |
| 运行时检查是否安装了 root 相关应用 | —— | **6.8%** | 同上，且这一类信号本质上依赖第三方 App 的存在 |
| 作者据此写出的结论 | —— | 「我们的动态分析在 iOS 应用中**只发现 25% 实现了越狱检测**」 | 与 Kellner 的 59% 被作者自己显式对比 |

不写清这套双口径，96.7% 与 24.9% 看起来像互相矛盾的两项研究；写清了就知道它们是**同一个 2,646 应用池上的两种判据**，且差异本身就是结论：**绝大多数 iOS 应用带了检测代码，其中约四分之三并没有在运行时真正执行它。** 对防线设计，这句话的含义比"检测可绕"更难受——它说明行业里大量"已实现设备校验"的合规声明，可能只是链接了一个含相关字符串的库。

**检测清单陈旧化（可直接引用的等级 2 依据）。** 应用按越狱发行名做特征检测时，覆盖面高度集中在两类旧特征名上：分别出现在 **5.5%** 与 **1.4%** 的应用中（具体名称见论文，本篇不收录）；另有少量针对其他发行名。**未发现任何应用带有针对 iOS 15（2021 年 9 月发布）及更新版本越狱的专用检测**，作者据此写出：「**新越狱技术发布后，应用内的越狱检测并不常更新**」（jailbreak detection in apps is not updated very often after new jailbreaking techniques are released）。这条限定要按原文范围写：**"针对 iOS 15 及之后发布的越狱，未发现专用特征检测"**，不得写成"所有检测都不更新"。它与 14.2 的"复制粘贴社区代码"归因构成同一因果链的两侧——前者解释信号为何陈旧，后者测量陈旧到什么程度。这是"检测信号滞后于攻击面变化"的最强一手依据。

**总体有效性结论（原文方向）。** 两句必须一起引：「**单个或少数几项加固技术说明不了自防护的有效性，质疑了当今应用的整体安全性；更糟的是，目前的采用状况可能给用户和开发者一种虚假的安全感（false sense of security）**」；「**各项防护单独可被绕过；有效性取决于实现与是否多层叠加，正如 OWASP 所建议**」。第二句里的"取决于实现与是否多层叠加"是 HALY 相对 Kellner 的关键差别：Kellner 报告的是**结果**（除 1 外全部可绕），HALY 报告的是**条件**（单层必失效，多层与实现质量改变结果但不足以构成可信根）。

**与设备真实性防线直接相关的采用率。** HALY 测得 Apple App Attest 在 iOS 应用中的出现率为**静态 4.1% / 动态 1.1%**——这是本次核查中关于 App Attest 采纳度的**唯一可核实测量值**。同批数据里，iOS 侧仅 **1 个**应用采用全部七项被分析技术（Android 侧 26 个），**73.6%** 的 iOS 应用采用少于 MASVS 建议一半的自防护技术（Android 为 24.1%）。这组采用率数字合起来给出一条本报告反复使用的对照：**密码学可验证信号（App Attest）采用率极低，而启发式端侧检测的"字符串采用率"极高。** 防线现状恰好与其有效性排序相反。

**工具侧局限（本章与第 17 章共用）。** HALY 一手写明，MobSF 的 iOS 动态分析**限定于 Corellium VM——一个闭源、付费的服务提供平台**，而 HALY 自身要求"任意可越狱的 iOS 设备"。加上其越狱覆盖到 iOS 16、实验机型 A11，这条自述同时构成两件事实：iOS 侧自动化对抗研究依赖闭源虚拟化平台；iOS 大样本运行时研究依赖越狱覆盖上界。第 17 章的证据密度论证就建立在这两句之上。

## 14.4 起点与复测：Evans 2015、ESORICS 2025 workshop 与 Android 对照

两篇补充研究的作用是**给 14.2 与 14.3 提供时间纵深与"仍在被复测"的证据**，而不是新增可用数字。

**Evans 等 2015：把检测自身当被测对象的起点。** 该论文标题本身即是"root 检测的悲惨现状"（All your root checks are belong to us: The sad state of root detection）。可核实口径（由 HALY 相关工作一节转述核对）：**在 16 个安全类应用与 19 个企业移动管理（MDM）应用（Android 与 iOS）中研究 root 检测流行度，发现 13 个安全应用、15 个 MDM 应用使用了 root 检测**【L2 部分核实：标题与作者经 Kellner 2019 参考文献 [55] 与 HALY 双重核对；本次未取到论文原文 PDF，样本口径依 HALY 转述】。引用路径：https://intellisec.org/research/ios/2019-eurosp.pdf ；https://martina.lindorfer.in/files/papers/haly_eurosp25.pdf 。这项工作的价值在方法论起点：**它是最早一批把"设备真实性检测自身的可靠性"当作研究对象的学术工作**，也是本章"检测是启发式而非可信根"这一论点的历史锚点。它的局限同样要写：它测的是流行度，不是有效性。

**ESORICS 2025 workshop：复测已进入同行评审议程。** Irfan & Uto, "Revisiting the Effectiveness of Jailbreak Detection", 收录于 *Computer Security — ESORICS 2025 International Workshops*, Springer LNCS, pp. 558–577【https://dl.acm.org/doi/10.1007/978-3-032-16165-9_33 ；卷首页 https://link.springer.com/content/pdf/10.1007/978-3-032-16165-9.pdf 】。检索到的摘要方向：该研究评估 iOS 银行应用越狱检测的有效性，**特别是新型 rootless（无根）越狱如何绕过检测**。意义有两层：其一，"rootless 越狱使传统基于特征文件的检测失效"这一命题**已进入同行评审议程**，不再是厂商博客口径；其二，它与 14.3 的"针对 iOS 15 及之后发布的越狱未发现专用特征检测"形成呼应——如果按发行名匹配的清单长期不更新，那么不修改系统卷的一类绕过自然落在检测器的视野之外。

**该篇的使用限制（必须严格执行）。** 等级 2 中强度较低的一档（workshop 后论文集，低于主会）；**本次未取得可全文核对的版本**（相关页面 403/303 重定向），因此正文**只可写"有 ESORICS 2025 workshop 论文以 rootless 越狱为条件重新评估了 iOS 银行应用越狱检测的有效性"，不得引用其任何百分比、样本量或结论数字**。这条限制不是谨慎，而是纪律：一个未核对全文的百分比一旦进入报告，就会在下游变成"最新研究测得绕过率 X%"。

**Android 对照（仅用于口径不对称论证）。** Sun 等对 **182 个 Android 应用**检验多种 root 检测技术，结论为"这些方法无效"（由 HALY 转述，本次未取原文，等级 2 转述）。另可对照的是一条跨研究的流行度序列（简报口径为 Android 侧 root 检测序列）：Evans 2015（16 安全 + 19 MDM 应用）→ Kellner 2019 → 2024 年一项研究报 **61%** → HALY 2025 报 **92.3%**。**这些数字来自四个不同样本与四种不同判据，不可排成趋势线，也不可用于"检测能力在提升或退化"的任何叙述。** 它在本篇唯一合法的用途，是支撑 17 章的口径不对称：**同类研究在 Android 侧更充分，iOS 侧的运行时测量始终受越狱覆盖与闭源虚拟化两项门槛限制。**

**另有一处口径事实需要在本章立住：论文自述"越狱与 hook 检测在 iOS 上界限模糊"**（the distinction between hooking and root detection on iOS is blurry，HALY 用 root/jailbreak detection 统称）【L2】。这决定了本篇不能把"越狱检测"与"运行期注入检测"当成两个可分别计数的独立防线——同一个信号同时属于两类，其类别计数加总因此不等于 100%（详见 17.4 素材 3）。

## 14.5 误报：被公开承认的工程事实，与 iOS 侧没有数据的比率

"检测存在大量误报（正常设备被判越狱）"是本章核查中被**推翻**的一条：方向有支撑，量级没有任何支撑。核查结果是明确的负面结论——**本次未发现任何同行评审论文对 iOS 越狱检测误报率做过测量并给出比例**。因此本篇的规范句式是：**误报被公开承认为工程事实，但其发生率在 iOS 侧无公开可核实数据**。

可核实的支撑只有两类，且都要按等级降级使用。

| 支撑类型 | 可核实内容 | 等级 | 出处 |
| --- | --- | --- | --- |
| 规范文档把误报列为**设计约束** | HALY 记述 OWASP MASVS 的要求：「**即使以牺牲漏报为代价，也要最小化误报**」（minimize false positives, even at the cost of possible false negatives） | 2（对标准文档的转述） | https://martina.lindorfer.in/files/papers/haly_eurosp25.pdf |
| 厂商知识库承认该现象 | 多家移动安全厂商在其公开知识库中写明 root/jailbreak 检测会产生误报 | 3（厂商，仅可用于"承认存在"） | https://promon.io/resources/knowledge-center/jailbreaking-and-root-detection ；https://www.onespan.com/blog/root-and-jailbreak-detection-in-mobile-banking-security |
| 开发者社区个案 | 有开发者报告"客户设备从未越狱却被检测判为越狱"并寻求处置 | 5（**只可提出线索，不得作为事实来源**） | https://stackoverflow.com/questions/78517778 |

三条解读。

**第一，等级 2 那条支撑的是"应当最小化"，不是"经常发生"。** OWASP 移动应用安全验证标准把"即便牺牲漏报也要压低误报"写成设计约束，这本身就说明该领域把误报视为需要主动防御的工程后果；但**规范条款是应然，不是测得**。把"标准里写了要最小化误报"改写成"标准承认误报很常见"，是从等级 2 的规范性表述里榨出描述性结论，属本篇禁止的推断。

**第二，等级 3 与等级 5 两类材料在数量上很多，在证据上很少。** 厂商承认误报存在，带有明显的立场成分（承认误报是为配套"我们提供规则可调的检测"这一产品能力做铺垫）；社区个案则天然只证明"发生过"，不证明频率。二者相加仍得不到一个率。**因此本章全文不出现任何误报率数字**——包括"通常有百分之几""大量""普遍"这类变相量化。凡下游需要误报成本，只能回到自身业务日志去测，这是第 4 章"阈值与处置归业务侧"结论的组成部分。

**第三，误报问题与"检测不是可信根"是同一枚硬币的两面。** 14.3 给出的机制解释在这里直接可用：HALY 实测约 **95.7%** 的 iOS 应用调用 `_dyld_image_count`、**74%** 同时调用 `_dyld_image_count` 与 `_dyld_get_image_name`，而这两个函数既被用于枚举已加载动态库以发现注入组件，也被大量正常代码使用，作者因此把相关结果标记为**高不确定性（uncertain）**【L2】。也就是说，**同一批模糊信号既造成漏检（因为无法据此判定），也造成误判（因为正常行为会命中）**。iOS 侧没有误报率的公开数据，但有一条同等重要的公开测量：**本质无法区分正常与异常的信号在 iOS 应用里极为普遍。** 这条测量在 17.4 会被当作"信号可靠性分层"的主要素材使用。

**处置规则（供下游直接引用）。** 涉及误报时，允许写："误报是公开承认的工程事实（有规范文档与厂商口径支撑），iOS 侧发生率未公开、无同行评审测量。" 禁止写：任何数字、任何"普遍/大量/少数"的量级词、任何"因此我们不应使用端侧检测"的推论（正确推论见 14.6：端侧检测可作启发式输入，不可作判定根）。

## 14.6 这些结论对防线设计意味着什么：检测不是可信根

本章不是"给端侧检测判死刑"，而是给它定位置。四篇研究合起来能支撑的设计结论有五条，每一条都可回溯到 14.2–14.5 的具体口径。

**一，端侧检测的性质是启发式输入，不是判定。** Kellner 的绕过前提是攻击者已具备运行期函数替换能力【L2 EuroS&P 2019】；HALY 的双口径显示大量检测字符串并未在运行时执行【L2 EuroS&P 2025】。两条叠加：一个把结论写在客户端进程里的判定，其可信度不超过该进程自身的完整性。Apple 文档在这点上口径一致且更早：「You can't rely on your app's logic to perform security checks on itself because a compromised app can falsify the results.」【L1 https://developer.apple.com/documentation/devicecheck/establishing-your-app-s-integrity】——注意这是**厂商文档口径，不是研究结论**，但与两项独立测量方向相同，构成本篇最扎实的一处交叉。

**二，端侧检测可被削弱，且削弱不需要专门知识。** 14.2 的"复制粘贴社区检测代码"归因是本章最重要的一条工程教训：检测代码的来源不可控，导致检测方案本身成为"轻易的目标"，且这一模式在同一批评测里跨行业成立（银行应用未超出全站平均）。对设计的直接含义：**把端侧检测当作合规项打勾，等于把不可控来源的代码接入判定路径。**

**三，检测信号会滞后于攻击面。** 针对 iOS 15（2021-09）及之后发布的越狱未发现专用特征检测【L2 EuroS&P 2025】，而 rootless 一类绕过恰好使按系统路径/发行名匹配的旧特征失效【L2 ESORICS 2025 workshops，方向级】。任何依赖"清单齐全"的方案都有一个不可消除的尾部：**清单是在攻击面变化之后才更新的。**

**四，端侧检测不能当可信根，与第 5–9 章形成对照——这是本篇的核心论点之一。**

| 维度 | 端侧启发式检测（本章） | 服务端可验证信号（第 5–9 章） |
| --- | --- | --- |
| 判定发生位置 | 设备上的 App 进程内 | 开发者服务端，校验 Apple 签发的材料 |
| 结论形态 | 布尔猜测（"看起来像越狱"） | 密码学可验证的**签发事实**（key 由指定签名类别的 App 实例生成；若干设备属性由 Apple 签发） |
| 是否可被宿主削弱 | **是**（前提：运行期函数替换能力） | 部分：Apple 一手承认修改操作系统者"可能绕过限制"，但要求 SE 内私钥不可被任何进程直接读取或修改 |
| 公开有效性证据 | 有：两篇同行评审主会 + 一篇 workshop 复测（本章） | **零篇**同行评审绕过/弱点研究（见 17.3） |
| 覆盖面 | 静态 96.7% 但动态仅 24.9%（iOS） | App Attest 静态 4.1% / 动态 1.1%（iOS） |
| 误报/漏报可核程度 | 误报率**无公开数据** | risk metric 被 Apple 自己标注为 approximate，精确定义未公开 |
| 可作防线角色 | 启发式输入、风险信号之一 | 可作**准入约束的输入**，但仍不构成"设备未越狱"的判定 |

表格最后一行需要重复一遍以免被误读：服务端可验证信号也**不是**"设备真实性判定器"。第 3 章已确立的口径是——它证明的是密钥与 App 实例的真实性，设备真实性只是以之为代理指标。本章补上另一半：**端侧检测连代理都算不上，因为它连自己是否被执行都不保证。**

**五，正确的责任分工由此确定。** USENIX Security '26 的结论是条件式的：「仅依赖客户端机制是不够的。关键逻辑（如权益状态与环境校验）应交由服务端校验」；其另一句更精确——客户端绕过「**只在缺少服务端校验的应用中有效**」【L2 USENIX Security 2026】。据此，端侧检测的合理角色是**提高成本与提供线索**，服务端与平台签发材料是**准入约束的落点**；两者不构成替代关系，也不允许互相冒充实证。第 16 章的灰产测量之所以重要，正因为它提供了这条分工的**反面实证**：凡权益判定留在客户端，该应用就出现在改造版清单里。

**本章口径边界**

- 本章全部失效结论逐年逐口径给出，**不得合并为"研究表明检测普遍无效"**；被测对象是应用内实现与运行时行为，**不是商业检测库产品**。
- 引用 Kellner 2019 必须带条件：「在 2018–2019 年、作者所用检测点位与 hook 能力（需已具备运行期函数替换）条件下」；其内部计数不一致（导言 15/34、结论 18/34）需并列或采用摘要口径"44% 未尝试检测"。
- HALY 的 96.7% 与 24.9%/25% 必须写明**静态分析口径 vs 动态执行口径**；陈旧化结论限定为"针对 iOS 15（2021-09）及之后发布的越狱未发现专用特征检测"，不得写成"所有检测都不更新"。
- ESORICS 2025 workshop 论文只可引研究问题与方向，**不得引用其任何百分比**；Evans 2015 与 Sun 等（182 个 Android 应用）均为转述级，须标注且后者不得写成 iOS 结论。
- **本章不出现任何误报率数字**；误报的合法表述止于"被公开承认为工程事实，iOS 侧发生率无公开可核实数据"。
- 等级标注纪律：L2 同行评审（Kellner、HALY、Evans 转述、workshop 复测）／L3 厂商（承认误报、能力存在性）／L5 社区（个案，只作线索）；厂商对自家产品有效性的自评一律降级为营销，不进入本章结论。
- 检测点位清单、hook 框架名称、绕过组件名称一律不写（**正文不记实现**）；机制描述归《iOS 系统防御与机制绕过》第 11 章。

---

# 企业证书与未授权分发：Apple 的机制口径与公开观测边界

## 15.1 in-house 在 Apple 分发口径里的位置：兜底通道为何天然是滥用面

本章的写作边界先说清：**本章不含任何企业证书滥用、绕过分发校验或未授权分发的操作流程。** 允许写入的只有三类内容——Apple 的一手机制与规则原文、同行评审研究对现状的判断、媒体报道层的可核实事件；能力存在性与失效结论按 13.1 的纪律给出，实现层一律标注**「正文不记实现」**。

与第 12 章的分工需要先立住。12.4 已经并列过 ADEP 的用途边界与 OCSP 吊销的机制原文，那里是**作为"被篡改 App 的上游分发约束"顺带引用**；本章则把企业通道当作独立对象处理，重点从"机制原文"移到**"机制口径与观测口径的分界"**——也就是 15.5 要写的半推翻结论：机制有明确一手文档，公告与统计则完全缺席。两章的引用会有必要重叠，但分工是明确的：规则原文以本章为准，威胁模型与签名类别以第 12 章为准；证书滥用的通用谱系与第三方商店的政策面归《iOS 供应链安全分析报告》第四章，本章不重述那张表。

**先看 Apple 自己怎么给这条通道命名。** Apple 平台安全指南（August 2026 版）与开发者文档在分发口径上只使用四类词：App Store、Ad Hoc、TestFlight、in-house（经 Apple Developer Enterprise Program，简称 ADEP）【L1】。**Apple 文本里没有"侧载（sideloading）"这个分类词，也没有"越狱"。** 这个差别不是修辞问题：读者用"侧载"想象的是一件事，Apple 用"in-house / Ad Hoc"描述的是另一件事——后者是**合法签发的分发通道**，其证书、描述文件、设备注册全部在 Apple 的 PKI 与规则之内。第 13 章那张 USENIX '26 通道谱系表之所以有价值，正是它在同行评审口径里把 In-House、Ad Hoc、TestFlight、AltStore、越狱、TrollStore、WebClip **并列**为通向设备的不同路径，让"合法通道被用于非授权目的"与"非法通道被用于任何目的"成为两个可以分别讨论的对象。

**这条通道的定位本身就是滥用面的来源，且这一点有 Apple 一手依据。** ADEP 页把该计划明确排在他项之后：它被定位为 **App Store、Apple Business Custom Apps、Ad Hoc、TestFlight 都无法满足需求时才使用**的兜底通道【L1 https://developer.apple.com/programs/enterprise/】。同一页给出用途边界的原文：允许范围是「allows large organizations to develop and deploy proprietary, **internal-use** apps to their **employees**」，约束是「Use the program **only** to create proprietary, in-house apps **for internal use**, and to distribute these apps **privately and securely to employees within the organization**」，并要求组织「must have systems in place to ensure **only employees can download** your internal-use apps, and to protect membership credentials and assets」。

把这三句并列，就能看出一条结构性事实，而这条事实**不需要任何关于滥用的假设**即可成立：**ADEP 的信任授予对象是"组织内部"，但它签发的分发技术在机制层面不识别"内部"。** 边界靠组织自身的管理系统去保证（Apple 的原文正是这样要求的），而不是靠签名与安装环节去强制。这正是"企业证书天然适合面向公众分发"的一手依据——不是某个人的评价，而是规则文本自己暴露的缺口。同一口径还解释了后果为什么是连坐式的：既然强制点不在通道上，Apple 唯一能施加的处置就落在**证书**这个对象上，于是"吊销一张证书使该证书签署的全部应用失效"（15.3）既是治理手段，也是 collateral damage 的来源。

**同行评审侧对现状的判断（等级 2，可直接引用）。** USENIX Security '26 论文的表述是：「In-house distribution relies on enterprise certificates and **has been widely misused for public app distribution**」，并给出状态判断：「This method **was once common but is now tightly regulated**」【L2 https://www.usenix.org/system/files/usenixsecurity26-liu-yijing.pdf】。两句合起来是本章的基线口径：**曾被广泛滥用、现处于严格监管之下**。要注意这两句都没有量化——论文没有、也无法给出"滥用规模"的数字，因为分母不可观测（详见 16.2 与 16.6）。因此本篇不能把"tightly regulated"读成"问题已基本解决"，也不能把"widely misused"读成任何量级。

## 15.2 ADEP 准入与持续审查：可引用的规则数字与其口径

本节的数字全部来自 Apple 一手页面，可逐句引用；但**引用时必须带上它们的性质**：这些是**准入规则条款**，不是审核通过率、封禁数量或任何统计量。

| 条款 | Apple 一手口径 | 备注 |
| --- | --- | --- |
| 员工规模 | **100 名及以上员工** | 门槛值是"组织人数"，与设备数无关（见下方易混点） |
| 主体资格 | 必须是**法人实体**；不接受 DBA（经营别名）、虚拟主体、分店 | 这一条把"个人可申请企业证书"排除在规则之外 |
| 身份核验材料 | 需 **D-U-N-S 编号**，以及与组织关联的**公开网站域名** | D-U-N-S 是第三方商业主体编号；Apple 用它做主体一致性核验 |
| 费用 | 会员年费 **299 USD / 会员年** | 与 99 USD 的标准开发者年费分列两类计划，不得互相引用价格 |
| 持续审查 | 「Participate in and pass Apple's **verification interview and continuous evaluation process**」 | 原文即"访谈 + 持续评估"，是一次以上而非一次性 |
| 续约 | 「If you choose to renew your membership, your organization will be **verified again**」 | 续约触发再次核验，与"审核只在申请时发生"的常见印象相反 |
| 拒绝权 | 「Apple reserves the right to reject your application **at its sole discretion**」 | 自由裁量条款；Apple 未公开拒绝率或裁量细则 |

**一个必须显式拆开的易混点。** 本领域存在**两个"100"**，其对象完全不同，混用会直接导致结论错误：

| 数字 | 对象 | 出处与性质 |
| --- | --- | --- |
| 100 | ADEP 申请组织的**最低员工人数** | Apple ADEP 页，准入规则条款【L1 https://developer.apple.com/programs/enterprise/】 |
| 100 | **个人开发者账号每设备年度可注册的 UDID 台数**（前 10 台即时通过、其后需 24–72 小时审核；论文记述对 macOS 的执行放松使实践中可绑定至约 200 台） | USENIX Security '26 论文对其研究观测的记述【L2】，属第 16 章口径 |

第一个是"谁能拿到企业证书"，第二个是"Ad Hoc 通道一年能绑多少台设备"。前者与灰产的"超级签名"计价无关，后者才是第 16 章设备身份资产化论证的落点。中文报道常把两者写成同一句"Apple 限制 100 台"，属口径错误。

**准入规则能支撑什么结论、不能支撑什么结论。** 能支撑的是**存在性**：这条通道的授予条件包含主体资格、独立编号、可核验域名、付费、面试与持续评估，因此 Apple 对"谁拿到企业证书"施加了不低的先验门槛，并把**吊销**（而非事前拦截每一次安装）作为主要事后处置。不能支撑的是任何**关于门槛有效性的量化**：Apple 未公开 ADEP 的批准率、拒绝数、存续主体数，也未公开因违规被清除的会员数量；同行评审研究同样无法观测这些分母。因此本篇只写"规则严格且事后处置严厉"，不写"因此滥用已被管住"。

**内部使用义务的实际落点。** 15.1 引到的「must have systems in place to ensure only employees can download」值得单独强调：Apple 把访问控制义务**写在组织侧**。这意味着，一项把内部应用直接暴露给公网可下载链接的部署，即使没有任何第三方改造，也已偏离 ADEP 规则原文；本报告只作规则层描述，**不讨论任何人如何利用这一偏离**（正文不记实现）。这条义务同时解释了 15.3 的连坐处置为何在设计上必要：当强制点在组织侧而非平台侧时，平台唯一可靠的作用对象就是证书本身。

## 15.3 吊销如何生效：首启动 OCSP 校验、连坐式处置

这一节的骨架来自 Apple 部署指南「Distribute proprietary in-house apps to Apple devices」（文档号 depce7cefc4d，页尾标注 **Published Date: September 17, 2026**）【L1 https://support.apple.com/en-il/guide/deployment/depce7cefc4d/web】，与平台安全指南（August 2026 版）交叉印证。所有数字均为一手，须带来源与版本发布日期使用。

**一、首启动即校验。** 原文：「The first time a user opens an app, the distribution certificate is validated by **contacting the Apple OCSP server**. If the certificate has been revoked, **the app won't launch**」；设备需能够访问 `ocsp.apple.com`。平台安全指南的对应表述是：「On first launch of any proprietary in-house app, **the device needs to receive positive confirmation from Apple that the app is allowed to run**」。**注意两句的方向差异**：部署指南写"未吊销则可运行"，平台安全指南写"须取得 Apple 的正面确认"——后者语义更强，即**取不到确认也不等于允许运行**。这一点决定了 15.4 里"网络侧失效"的两种可能方向。

**二、吊销是连坐式的。** 原文：「**Revoking a distribution certificate invalidates all of the apps you've signed with it.** You should revoke a certificate only as a last resort—if you're sure the private key is lost or you think **the certificate has been compromised**.」这一句是本报告"Apple 的处置粒度是证书，不是应用"的一手依据，也是理解第 16 章灰产结构的钥匙：既然处置粒度落在证书上，**批量分发就必然要求证书池**（而非单个证书），而吊销风险也必然被当作一项成本计入商业模型。Apple 在同一句里给出的处置门槛（仅作为最后手段、私钥丢失或怀疑被攻陷）也顺带解释了为什么吊销不是日常操作：一次大规模吊销必然同时使合规的内部应用停摆。

**三、分发侧的形式约束（照录口径，不给步骤）。** 企业应用需以 in-house 描述文件系统签名，经 HTTPS 站点与 `itms-services://` 清单安装；「Their network configuration needs to allow devices to access a server at Apple」；**iOS/iPadOS 18 及以后，不经设备管理服务手动安装的 in-house 应用需重启设备才能完成对描述文件的信任**。此外，经设备管理服务安装的应用被**隐含信任**，否则用户需在"设置"中手动信任描述文件；组织亦可禁止用户信任未知开发者应用【L1 平台安全指南 August 2026】。Apple 支持文档另有一手确认：首次打开手工安装的企业应用会出现"该 App 的开发者在你设备上不受信任"的提示【L1 https://support.apple.com/en-mo/118254】。

**四、有效期口径。** 分发描述文件有效期 **12 个月**；发行证书自签发起 **3 年**或随会员到期（以先到者为准）；**已签名应用不受发行证书到期影响**。这三条必须与"吊销"分开使用，详见 15.4。

（本节的"人工确认环节"分析与 12.4 同源：手动安装需用户在"设置"中信任描述文件、经设备管理服务安装则隐含信任；Apple 从未把该环节称作弱点，把它读作风险敞口属**分析推断**，本篇不讨论其被利用的方式。）

## 15.4 到期不等于吊销，以及两个已被公开的失效条件

**一、到期 ≠ 吊销，这条边界必须写死。**

| 事件 | 一手口径 | 对已签名应用的影响 |
| --- | --- | --- |
| 发行证书**到期**（自签发起 3 年，或随会员到期，以先到者为准） | Apple 部署指南：已签名应用不受发行证书到期影响 | **不影响运行** |
| 分发描述文件到期（12 个月） | 同上，属描述文件有效期口径 | 需以有效描述文件重新签发（本报告不写操作方式） |
| 发行证书被**吊销** | 「If the certificate has been revoked, the app won't launch」；且「invalidates all of the apps you've signed with it」 | **无法启动，且连坐该证书签署的全部应用** |

中文技术写作里最常见的错误是把前两行与第三行合并成一句"证书过期导致应用无法打开"。按 Apple 一手口径，这两件事的后果方向完全不同：**到期不使已签名应用失效，吊销使该证书下所有应用立即无法启动**。任何把"企业应用打不开"归因于证书到期的分析，在事实层面就站不住。

**二、失效条件一：3–7 天的缓存窗口（Apple 一手）。** 原文：「The OCSP response is **cached on the device** for the period of time specified by the OCSP server—**currently between three and seven days**. The revocation status of the certificate isn't checked again until the cached response has expired.」这句话是"Apple 吊销 ≠ 即时失效"的一手依据，也是本篇要求所有下游引用必须带上的口径：**吊销的生效时间受设备侧缓存窗口支配，Apple 明文给出"当前为三到七天"这一区间，且区间长度由 OCSP 服务端指定、可变动。** 引用时不得把它写成固定 7 天，也不得写成"吊销后立即全网失效"。

**三、失效条件二：设备无法回连 Apple 校验端点（等级 2，能力存在性）。** USENIX Security '26 论文明确记述：**存在**通过阻断设备访问 Apple 证书校验端点（`ocsp.apple.com`、`crl.apple.com`）使已吊销证书签名的应用继续可用的做法。本报告的处置是**只记录该结论的存在性，不写任何实现方式**（正文不记实现）；同时把它与第二条并置，得到本章最重要的机制结论：

> Apple 的企业证书吊销是**最终一致**而非即时生效的机制，其两个已公开的失效条件分别是：**设备侧 OCSP 响应缓存（3–7 天，L1）**，以及**设备无法向 Apple 校验端点回连（L2 记述其存在）**。

这两条合起来也解释了 15.3 里"正面确认"语义的分量：如果取不到确认在某些部署下等价于允许继续运行，那么校验点就从"平台强制"退化为"网络条件"。**这条推论属于本报告的机制层分析，Apple 未在任何文档里作出等价表述**，故必须按分析推断使用，不得写成 Apple 已承认的事实。

**四、分发链约束不等于运行期证明。** 企业通道能给风控提供三类可用输入：证书是否在有效期内、吊销状态（考虑缓存窗口后）是否已知、安装路径是否为受管通道（决定是否有隐含信任与是否需要人工确认）。三类都不能给出"该设备可信"的结论，只能给出"该副本的签发链条目前能否追到 Apple"这一局部事实。与之对照，第 5–9 章的 App Attest 与第 9 章的 Managed Device Attestation 提供的是**由 Apple 签发的可验证材料**。前者是**分发链约束**，后者是**运行期证明**，两者不可互相替代，也不可在文档里互相冒充实证。

## 15.5 半推翻的口径：机制有一手文档，公告与统计没有

这是本章最重要的一条更正，也是整章标题里"公开观测边界"五个字的落点。预期说法是："Apple 自己公开过撤销/吊销企业证书的机制与若干公告。"**核查结果：一半成立，一半被推翻。**

**成立的一半：机制。** 15.3 与 15.4 所列全部内容都有一手文档支撑——首启动 OCSP 校验、缓存窗口 3–7 天、连坐式失效、in-house 描述文件与 HTTPS 清单、iOS 18 起手动安装需重启完成信任、描述文件 12 个月、证书 3 年、到期不影响已签名应用。这些可以直接以"Apple 文档口径"引用。

**被推翻的一半：公告与统计。** 本次核查确认：

| 常被假定的 Apple 公开物 | 核查结论 | 正确写法 |
| --- | --- | --- |
| Apple 发布企业证书吊销公告 | **未获得任何条目**。Apple 在 ADEP 页与部署指南中均以"规则 + 吊销后果 + 准入审查"表述，不点名被处置主体 | "Apple 不发布企业证书吊销公告" |
| Apple 公布吊销数量/年度统计 | **不存在可核实条目**（属缺口清单第 4 项） | "Apple 未公开该口径数据"，**不得反向断言"吊销规模很小"或"Apple 不吊销证书"** |
| Apple 公布企业证书滥用被拦截的量化 | 未获得。Apple 的公开量化集中在 **App Store 审核与开发者账号侧**（拒绝上架数、驱逐开发者团队数、停用账号数），且该组数字口径为 2020 年、厂商自述、无方法学附录（详见 17.3） | 不得把审核侧数字当作企业证书吊销侧数字引用 |

为什么这条边界必须单独成节而不是一句附注，理由有三。**第一，它决定了可核实事件的上限等级。** 既然 Apple 不发公告，任何"某公司企业证书被吊销"的事件只能落在媒体报道层（等级 4），而等级 4 在本篇体系里只可用于**事件存在性**，规模与后续状态须另行核对（15.6 就是处理这个）。**第二，它阻断了"从缺席推定不存在"的读法。** 报告最容易犯的错误是：因为查不到吊销统计，就写"企业证书吊销并不常见"；或者反过来，因为查得到几起著名事件，就写"吊销频繁发生"。两个方向都没有数据支撑，正确处置是写下"该口径不可观测"。**第三，它划出本篇与其他篇目的分工线。** 供应链视角的证书滥用谱系（谁在用、用在什么内容上、政策面如何演变）已由《iOS 供应链安全分析报告》第四章承担；本章只交付"Apple 的处置机制原文 + 该机制的可观测边界"。

顺带记一笔同源的方法论教训，供下游写手核对：本系列早期篇目把 Apple 白皮书《Building a Trusted Ecosystem for Millions of Apps: A threat analysis of sideloading》的发布日期写作"2024 年"，该处与原文不符——本地取到的 PDF 封面标注 **June 2021**，而外部学术与监管引用（BRUEGEL 工作论文 WP 03/2024、澳大利亚 ACCC 提交材料）口径为 **October 2021**，疑为两个版本并存。正确表述是**"2021 年（存在 June 封面版与 October 引用版两种口径）"**。这条更正与 15.5 属于同一类问题：**引用 Apple 的公开材料时，先核对其发布日期与统计口径，再引用其中任何数字。**

## 15.6 可核实的公开事件与必须双述的口径冲突

既然 Apple 不发公告（15.5），可核实的吊销事件就只能落在媒体报道层。下表把本次核查到的事件与其**可用范围**一并列出——这张表的用处不是"举几个例子"，而是标明**每一条能被写成什么强度的句子**。

| 事件（时间） | 可核实内容 | 等级 | 本篇允许写成 | 不允许写成 |
| --- | --- | --- | --- | --- |
| 2019-01，Facebook 企业证书被吊销 | Ars Technica（2019-01-30）报道 Apple 吊销 Facebook 企业证书，涉其以研究用途向外部人员发放的应用 | 4【https://arstechnica.com/gadgets/2019/01/facebook-and-google-offered-gift-cards-for-root-level-access-to-ios-users-data/】 | "有严肃媒体报道称 Apple 于 2019 年 1 月吊销该主体企业证书" | 不得写成 Apple 已确认或已公告；不得据其推断吊销规模 |
| 2019-02，Google 企业证书被阻断**后恢复** | Forbes（2019-02-01）标题与正文明确写"now restored"；Malwarebytes 博客对 Facebook 事件作二次报道（等级 3） | 4 / 3【https://www.forbes.com/sites/davidphelan/2019/02/01/apple-blocks-google-and-facebook-ios-enterprise-certificates-now-restored/ ；https://www.malwarebytes.com/blog/news/2019/01/apple-pulls-facebook-enterprise-certificate】 | **双述**：Ars Technica 记 Facebook 被吊销；Forbes 记 Google 企业证书同期被阻断**随后恢复**；两处对"是否恢复"口径不一致，本篇按原文并列，不择一 | 不得写成"Google 企业证书 2019 或 2020 年被吊销并长期失效"（该写法来自中文二手转述，等级 5） |
| 2019-07，WhatsApp 案中的企业证书投放 | 研究机构通报与多家严肃媒体记述：某商业监控软件借助 Apple 企业开发者证书把载荷投递到目标 iPhone，无需经 App Store 审核，Apple 随后吊销相关证书 | 4 + 3。**状态：部分核实**——本次仅完成事件级定位，未取得可直接逐句引用的原始页面 | "多来源记述该企业证书投放事件存在"，并标注未取得原文引用位 | 不得引用任何具体日期段落、域名清单或投递方式描述；**国家级监控的检测困境归《iOS 商业间谍软件与监控技术》第十章，本篇不重述该叙事** |
| 2020-08，Zoom 企业证书静默标识案 | 媒体报道记述 Zoom 通过企业证书在 iOS 上维持设备级标识以绕开 App Store 审核，Apple 表示将在 iOS 14 中切断该做法 | 4。**状态：部分核实**（未取得可引用原文页面） | "有媒体报道称该做法曾被采用，且 Apple 表示将在后续版本切断" | 不得把"Apple 表示将在 iOS 14 切断"写成已核实的版本变更事实；不得据此推断企业标识方案的效果 |
| 2025-02，对特定地域开发者关闭 ADEP | appleworld.today 报道 Apple 对俄罗斯开发者关闭 ADEP | 4（媒体报道，**未见 Apple 一手公告**） | 仅用于"Apple 会按地域政策收紧 ADEP 准入"这一**存在性** | 不得写成 Apple 官方政策声明；不得外推为其他地域同样处理 |
| 2016，安全会议议题 | "Enterprise Apps: Bypassing the iOS Gatekeeper" 列入 Black Hat Asia 2016 议题 | 3/4（未取到讲稿正文） | 只作"该议题至少自 2016 年已进入安全会议议程"的**时间点**证据 | 不得引用讲稿中的任何方法或案例细节 |

**这张表要说清的一件事是"事件 ≠ 证据"。** 五起可核实（或半可核实）的记述，全部只能支撑"Apple 会在个案上吊销企业证书""吊销会经由媒体被公众知晓"这两条存在性结论。它们不能支撑任何频率、规模、趋势判断，而且原因不是样本小，是**分母不可观测**（15.5）。这也是为什么 15.6 必须与 15.5 成对出现：先说明 Apple 的公开口径里没有吊销统计，再列举事件，读者才不会把事件清单读成趋势图。

**口径纪律（本表统一适用）。** 凡媒体报道，句子必须包含"据报道/有媒体记述"标记；凡状态为"部分核实"的条目，引用前必须回到原文逐句核对，未核对前不得升格为事实；涉及企业证书被用于投放商业监控软件的段落，其攻击链与检测困境一律交回《iOS 商业间谍软件与监控技术》，本篇只保留"企业证书是一条可被用于绕过商店审核的分发载体"这一句存在性表述，**不写任何投放实现**。

**本章口径边界**

- 本章只写 Apple 一手机制与规则原文、等级 2 的现状判断、等级 4 的事件存在性；**不含任何滥用流程、绕过校验做法或分发操作步骤**（正文不记实现）。
- ADEP 与 OCSP 的全部数字（100 名员工、D-U-N-S、299 USD/会员年、描述文件 12 个月、证书 3 年、缓存 3–7 天、iOS 18 起手动安装需重启）来源为 Apple 一手文档，须标注版本与发布日期：平台安全指南 August 2026 版；部署指南 Published Date September 17, 2026。
- 「100」有两个不同对象（ADEP 最低员工数 / Ad Hoc 年度可注册设备数），不得合并；「到期」与「吊销」后果方向相反，不得混写。
- Apple **不发布吊销公告、不发布吊销数量统计**；缺口一律写"Apple 未公开该口径数据"，禁止反向断言"吊销不常见"或"Apple 不吊销"。
- 缓存窗口按原文写作"当前为三到七天"、区间由 OCSP 服务端指定，不得写固定值，也不得写"吊销即时全网生效"。
- Facebook / Google 两案对"是否恢复"的口径不一致，按原文双述；WhatsApp 与 Zoom 两案状态为**部分核实**，只可作事件级引用并标注未取得原文。
- "tightly regulated"不得读作问题已解决，"widely misused"不得读作任何量级——两句都是等级 2 的状态判断，均无量化。

---

# 改机与分发生态的公开观测：数字的口径与不可用范围

## 16.1 唯一可用的骨架：USENIX Security '26 的方法学与自陈局限

第 16 章的对象是"改机与分发生态"，而这一领域里可核实的材料极少、不可核实的数字极多。本次核查找到的最强一份来源，同时也是本章全部可用数字的**唯一来源**：Liu Yijing, Zhang Yiming, Liu Baojun, Duan Haixin（清华大学 & BNRist），"Cracks in the Walled Garden: Dissecting the Gray-Market of Unauthorized iOS App Distribution via Ad Hoc Sideloading", **35th USENIX Security Symposium (USENIX Security '26)**, 2026-08-12~14, Baltimore【L2 论文页 https://www.usenix.org/conference/usenixsecurity26/presentation/liu-yijing ；开放获取 PDF https://www.usenix.org/system/files/usenixsecurity26-liu-yijing.pdf ；录用清单 https://www.usenix.org/conference/usenixsecurity26/cycle1-accepted-papers 】。

**为什么必须先写方法学，再写任何一个数字。** 该论文的观察口径决定了它每个数字的含义上限，方法学要素如下（照论文自述）：

| 环节 | 论文口径 | 对数字的影响 |
| --- | --- | --- |
| 采集视角 | 用户视角采集（非平台侧、非流量侧） | 只能看到"公开可发现"的服务，看不见私域与闭源交付 |
| 域名扩展 | passive DNS 扩展，日志窗口 **2025 年 3–5 月** | 时间窗只有三个月；窗口内不活跃的站点必然缺失 |
| 应用样本 | 对 **14 个仓库**提取 IPA 条目 | IPA 总数是这 14 个仓库之和，不是全网 |
| 工具分析 | 对 **12 个签名工具**做源码/逆向分析，付费采购总支出 **RMB 500** | 工具结构结论限于这 12 个样本；采购额本身说明该市场对研究者几乎无门槛 |
| 抽样与饱和判定 | 定性"理论饱和"分析：随机抽 10 个应用，扩样至 15 个，至无新方法涌现为止 | 类型学结论是**定性**的，不可换算为比例 |

**作者的三项自陈局限，必须原样保留在本章每一次数字引用里。** 第一，**地理聚焦中国**——这不是可选项，它直接决定 3,359 个站点这批数字不能读作全球规模。第二，**只覆盖 Ad Hoc 自助签名这一条通道**，不等于全部未授权侧载；企业证书（In-House）、TestFlight、WebClip、AltStore 等通道不在其观测范围内（对照 13.4 那张表可看出，该论文覆盖的是表中一行）。第三，**第三方无法完整复核其样本**：论文明确写其敏感清单（站点全表、IPA、dylib）**不公开以避免助长滥用**，且已把发现上报 Apple。

第三项局限的分量比它看起来重。它意味着本章的数字**不可复算**：读者无法自行验证 3,359 这个数，也无法在同一口径上做出第二个数。这不影响其等级（同行评审、方法自述完整），但影响其**可反驳性**——一个不可复核的下界仍然是下界，却不能变成"总量"。同时它也说明作者自身如何权衡披露与滥用风险：**这是一份把"不助长滥用"置于"完整可复核"之上的研究**，本篇沿用其数字时采取同样取舍，不复现、也不要求其公开任何其拒绝公开的材料。

**第四项局限关乎本章的结论边界。** 论文写明，独立灰产应用（非改造版正版应用）的核心逻辑普遍**"重度混淆、设计异构，系统化分析不可行"**，作者明确留作未来工作。因此本章能谈的是**结构与激励**，不能谈**内容**——即这些改造包在实现层面做了什么，本次没有可核实的系统研究，任何此类描述都属推测。

**本章的写作边界重申。** 本章只描述生态结构（谁提供什么、价格如何形成、风险如何被对冲）与激励（为什么值得做成服务），**不写运作方法**：不写站点如何运营、如何取得证书、如何完成签名与分发、如何规避吊销；凡此类问题一律标注**「正文不记实现」**。中文媒体对该论文的转述（如安全内参 https://www.secrss.com/articles/94476 ）属等级 5，**只用于交叉核对，不作事实来源**——本章所有数字一律回到论文 PDF 口径。

## 16.2 可核实数字清单：全部按「研究观测下界」写

下表是本章允许使用的全量数字。**每一条都是"研究观测下界"，不是灰产总量**；表格最后一列是引用时必须一起带走的口径三件套（观测口径 + 时间窗 + 作者局限）。

| 观测项 | 数值（下界） | 论文口径与限定 |
| --- | --- | --- |
| 活跃签名站点 | **3,359** 个 | 从 8,358 个候选域名中筛出；passive DNS 窗口为 2025 年 3–5 月；地理聚焦中国；仅 Ad Hoc 自助签名通道 |
| 唯一工具名 | **2,118** 个 | 与站点数同源，指被观测到的工具名称数，不是工具实现数 |
| 上游"证书站点" | 仅 **28** 个 | 全部下游签名站点的证书来源高度集中于这 28 个上游节点——**这是本章最重要的结构性数字** |
| 集中度 | 一个证书站点最多支撑 **135 倍以上**的下游站点；最大集群单站支撑 **843** 个签名站点 | 集群口径由论文给出，"支撑"指证书派发依赖关系，非流量或用户数 |
| 非官方 IPA 条目 | **8,216** 个 | 来自 14 个仓库；条目数 ≠ 实际下载数 |
| 含直链条目 / 实际下载 | **2,660** / **2,654** 个应用 | 论文区分"可定位直链"与"实际取回"，本篇引用时保留两个数 |
| 唯一 baseapp | **1,883** 个 | 被改造的正版应用底数 |
| 存在非官方变体的 baseapp | **1,023** 个 | 即约半数被观测到的正版应用存在至少一个非官方变体（该换算为**本篇分析推断**，论文未作此比例陈述） |
| 变体最多的对象 | WeChat **492**、小红书 **141**、抖音 **100**、X **97**、TikTok **91** | 均为变体条目数，不是安装量、不是用户数、不是收入 |
| 以"解锁 VIP"为唯一卖点 | **442 / 2,654** 个下载应用 | 依应用自述描述统计，见 16.4 |
| 注入组件的恶意标记 | 8,085 个抽取 dylib 中仅 **3** 个被标记 | 扫描口径与局限见 16.4，**不得据此作安全性判断** |

**四点读法约束。**

**第一，28 与 3,359 的关系是"结构"，不是"规模"。** 上游 28 个节点支撑下游数千站点，说明这条链的**瓶颈在证书供给端而非站点数量端**；单站最多支撑 843 个下游则说明**吊销处置的落点非常集中**——这两条合起来才是对防线设计有用的结论。它同时给出一条不可观测的边界：论文的观测无法给出"这 28 个节点服务了多少终端用户"，任何此类换算都没有来源。

**第二，变体条目数极易被误读为安装量。** "WeChat 492 个变体"这句话在下游传播中最常见的变形是"微信被 492 台设备破解安装"，两者毫无关系：492 是被观测到的**不同 IPA 条目数**，与任何用户或设备数量无关。引用本表任何一行的变体数时，必须紧跟"条目数，非用户数"限定。

**第三，所有数字共享同一组局限，不可拆分引用。** 把 3,359 单独拿出来配一句"iOS 灰产站点规模达数千"，同时丢掉"地理聚焦中国 + 仅 Ad Hoc 通道 + 三个月窗口 + 下界"四项限定，是本章判定为不合规的写法。三件套（观测口径、时间窗、作者自陈局限）在本章属于**强制同现**，不是脚注。

**第四，"下界"一词按论文原意使用。** 论文标 lower bounds 的理由是可观测性（用户视角 + 三个月 DNS 窗口 + 14 个仓库），不是保守修辞：可以说"至少这么多"，不可以说"数量级约为"或"约等于总量"——后两种写法把一个有明确采集边界的下界，伪装成了总量估计。

## 16.3 设备身份被资产化：UDID 配额、密钥池与「吊销保险」

本节是第 16 章与"设备真实性防线"真正相接的地方，也是本篇最有价值的一条一手结论。论文观察到的机制是：**这条通道把 Apple 官方的设备注册机制（UDID 绑定）当作可交易资产来运作。**

**观测到的配额与成本结构（照论文记述）。**

| 观测项 | 论文口径 | 在本报告中的用途 |
| --- | --- | --- |
| 个人开发者账号年度可注册设备数 | **每年最多 100 台**；前 10 台即时通过，其后需 **24–72 小时**审核 | 说明"设备身份"在平台侧是有额度的稀缺资源 |
| 执行面的放松 | 论文记述对 macOS 的执行放松使实践中可绑定至**约 200 台** | 说明配额与实际可绑定量之间存在已被观测到的差值 |
| 单台绑定成本 | 折算**约 RMB 3.4**（按 688 元 / 200 台计） | 成本项，非售价 |
| 终端定价 | 终端用户支付为该成本的 **10–30 倍** | 加价发生在中间层 |
| 中间层利润 | 加价形成 **900%–3,000%** 利润率 | **该区间为论文对其观测样本的测算，不是行业平均利润率** |
| 供给组织方式 | 证书站点**维护跨多账号的密钥池（`.p8`）并行或随机派发证书**，以稀释可被识别的绑定模式 | 记为"已被同行评审研究观测到的结构特征"，**正文不记实现** |
| 风险对冲产品化 | 提供**"保修期内若容器被吊销可免费更换证书"的预付码机制** | "被吊销"已被**计价与对冲** |

**由此可得的本报告可用口径（一句）：设备身份（UDID）与账号配额本身已成为可批量采购、可转售、带"吊销保险"的商品；这是设备真实性防线被商业化的直接证据。** 这句话的重量在"直接证据"四字——它不来自厂商威胁情报的形容词，而来自一份同行评审研究对采购价格、配额、账期与售后条款的测量。

**四个要点，每个都对应一条防线判断。**

**一，绑定成本 3.4 元与终端价 10–30 倍之间的差距，说明防线的失效不是"技术难度低"造成的，而是"服务化"造成的。** 个人要完成一次设备绑定也许并不复杂，但真正让这条路可规模化的，是有人把它定价、包装、并承诺售后。第 13 章已把"少数极客"的对手画像退回（Kellner 2019 明确写越狱在其时点已具大众用户基础）；本节把同一退回用在改造分发上：**能动性在服务商，不在个别用户。** 风控若仍按"识别个别异常设备"设计，就会持续错过真正的规模化单元。

**二，密钥池与"随机/并行派发"这一条只能记为存在性与结构，不可写机制细节。** 论文的结论层含义是：**以证书或账号为单位做识别与封堵的防线，会遇到"供给端多账号池化"的结构性对抗**，因而单点封堵的命中率会被稀释。这是防线设计的一条重要结论（等级 2），而"池化如何具体运作"属**正文不记实现**。引用本节时允许说"已被同行评审研究记述"，禁止给出任何配置、参数或流程。

**三，"吊销保险"是本章最能说明机制与市场对峙状态的一条。** 它意味着：**Apple 的吊销（第 15 章）在商业模型里被当作一项可预期、可对冲的运营成本**。结合 15.4 的缓存窗口与 15.5 的"吊销无公开统计"，可以推出一个必须写进报告的分层判断：吊销**有效**（使应用无法启动）、吊销**延迟可预期**（缓存窗口）、吊销**频率不可观测**（无统计），三者叠加的结果就是"保险"这种产品能够成立。防线不能因为对手能买保险就认定其无效，但**必须以"对手已把处置计入成本"为前提做设计**。

**四，本节数字全部沿用 16.1 的局限。** 地理聚焦中国、仅 Ad Hoc 自助签名通道、2025 年 3–5 月窗口、下界、样本不可完整复核。特别是那组价格与利润率：**它们是论文观测样本上的成本测算，不是任何行业平均口径**，禁止被引用为"iOS 灰产平均毛利"。另需注明一处与第 15 章的呼应：本节"每年 100 台"是 Ad Hoc 通道的设备注册配额，与 ADEP 的"100 名及以上员工"是两个不同对象（15.2 已立此条），不得混用。

## 16.4 改造版应用与检测生态的失明：卖点分布与恶意标记

**术语先行（首次出现处定义，之后不得换用）。** **重签名**＝以另一个有效证书对应用重新签名并分发；**二次打包**＝包内容被改动后重新打包；本报告沿用 USENIX '26 的**"改造版应用（modified app）"**口径指称 iOS 侧被改动并重新分发的副本，**不与 Android 文献的 repackaging 指标互相换算**。这条定义纪律来自简报的术语冲突项：Android 文献用 repackaging（AppInk、DroidMarking、VAhunt 等），iOS 文献用"正版应用的修改版 + 动态库注入"，HALY 用 re-signed，厂商把上述全部现象统一叫"repackaging / re-signing detection"。四套词指的是不完全重叠的对象。

**一，端侧没有"查原始签名者"这条路——这是所有 iOS 重签名检测的根本约束。** HALY 的原文是本报告见过的最清晰表述：「On iOS, one cannot check the signature of an app directly. All apps from the App Store are re-signed with Apple's signature, and iOS devices only allow execution of Apple-signed binaries, unless the check is disabled by a jailbreak. However, apps could be re-signed and executed on a jailbroken device, which is why apps could test whether an `embedded.mobileprovision` file is present. Apple does provide an App Attest Service though to verify apps are unmodified.」【L2 EuroS&P 2025】。三条可落地口径由此直接得出：**(a)** 端侧只能退化为"检查随包描述文件是否存在"这类**弱特征**；**(b)** Apple 把"验证应用未被修改"的能力放在**服务端**的 App Attest 上，与第 4 章责任分配一致；**(c)** 端侧完整性检查的前提条件是"设备未被越狱"，**该前提与服务端证明互为依赖，任一失效即整体失效**。

**二，Apple 一手机制界定了"运行期校验到底在校验什么"。** 平台安全指南（August 2026 版）原文：「At runtime, code signature checks of all executable memory pages are checked as they're loaded to help ensure that an app **hasn't been modified since it was installed or last updated**.」【L1】。这句话的边界必须逐字读：**校验对象是"自安装/上次更新之后是否被改动"，绑定的是当时生效的签名，而不是"App Store 审核时的那一份"。** 同页另述第三方代码需通过 Team ID 校验才能被进程链接，App Store 分发具备「cryptographic guarantees against modification」。

**分析推断（必须标注，非 Apple 口径）**：把 (a) 与本节第二点并列，可得"**经合法证书重新签名的副本可以正常通过运行期完整性校验**"这一命题——Apple 文档给出了机制依据，但 **Apple 从未写过"因此重签名包可绕过检测"**；该推论属本报告的分析推断，**不得写成 Apple 已承认的漏洞**，也不得写成"Apple 已防住重签名"。同一条推论同时解释了为什么 App Attest 的 `validationCategory`（第 12 章）是以"签名身份类别"而非"是否被改"呈现：平台能如实报告的是签名身份，判定权仍在服务端。

**三，改造版在真实分发链中"通过客户端检查"的实证（等级 2，条件式）。** 论文测得 **442 / 2,654** 个被下载应用在描述中**明确且仅以"破解 VIP"为卖点**——口径提醒：这是按应用自述描述统计的**条目数**，与安装量无关。论文对失效条件的表述是可直接引用的一句：「This approach only bypasses client-side checks and **works only in apps that lack server-side validation**」，并归纳出"仅依赖客户端机制是不够的，关键逻辑（如 VIP 状态与环境校验）应交由服务端校验"。这一句与 14.6 的第一条设计结论在证据层面互锁：**客户端校验失效 ⟺ 服务端校验存在**，是条件式而非概率式。

**四，杀检生态对此类改造包基本失明（等级 2）。** 论文对**全部 8,085 个抽取出的 dylib** 做恶意代码扫描，**仅 3 个被标记**。作者据此写的结论方向是：**多数注入组件是为增加功能而非分发显性恶意代码**；但其行为（改写运行时内存、操纵流量、绕过系统控制）**破坏了应用逻辑并削弱 iOS 的安全隔离**。这条结论的合法用法要精确：**它测的是"以恶意代码特征为判据的检测体系对这批组件几乎无反应"**，因而不能读作"这些组件无害"，也不能读作"改造包普遍无害"。它同时也解释了 15.5/17.3 里厂商口径的一个空缺：以恶意样本库为核心的产品叙事，在这类对象上没有可展示的检出率。

**五，规避以重打包为基础的检测——能力存在性结论，及其证据强度的限制。** 论文指出改造普遍依赖注入动态库，并记述现成工具与插件式框架可把逻辑在**运行期加载**，从而「规避以重打包为基础的检测机制（repackaging-based detection mechanisms）」。**正文不记实现**：不写加载方式、不写框架名称、不写组件形态。此处必须同时写出证据强度的限制——论文所引的重打包检测工作（AYPE NDSS 2011、AppInk ASIACCS 2013、DroidMarking ASE 2014、Shao 等 ACSAC 2014、Repackage-proofing DSN 2016、VAhunt CCS 2020、ModZoo APWG eCrime 2024）**绝大多数以 Android 为对象**，因此"iOS 改造可规避 repackaging 检测"这一结论的**方法是从 Android 生态类比而来的**，其强度低于论文自身的直接测量部分。

**六，口径不对称与两条负面结果（本节最关键）。** 第一，**本次未获得任何把"iOS 重签名包"作为独立研究对象并给出检测方法的同行评审论文**；可核实的体系化重打包检测研究集中在 Android。正确写法是「**iOS 侧重签名检测的公开研究密度显著低于 Android**」，**不得写成"iOS 重签名威胁不严重"**。第二，多家 MTD/RASP 厂商在产品文档中把 repackaging / re-signing detection 列为标准检测项，但**本次未取得任何一份带公开方法学与可复核样本量的第三方评测**来验证其检出率；因此只能写"该检测项在商业产品中普遍存在，其检出率与误报率无公开可核实的第三方测量"。第三，题录级补充：**"iOSModZoo: A Large-Scale Study of Third-Party iOS App Markets"（ACM WiSec 2026）**【https://dl.acm.org/doi/10.1145/3765613.3797450】与 **"Taking a Bite Out of the Forbidden Fruit: Characterizing Third-Party Iranian iOS App Stores"（ACM IMC 2026, pp. 175–187）**【https://dl.acm.org/doi/10.1145/3777912.3839789】两篇均以 iOS 第三方市场/改造应用为对象，**但本次未取到全文，只可引用"此类研究已存在"，不得引用其任何数字**。这三条并置可支撑一个趋势表述（等级 2，题录级）：改造应用生态的研究重心**正在从 Android 向 iOS 迁移**——注意这是"研究在迁移"，不是"威胁在迁移"。

## 16.5 时间纵深与本土定性：2015 年至今的可核实条目

本节用来处理一个容易被忽略的判断：**这条生态不是新现象，而且"不是新现象"这件事本身有等级 2 的证据。** 时间纵深一旦立住，"iOS 侧不存在成规模改造分发"这类叙述就自动失效，而不需要任何当前数据。

| 时间 | 可核实条目 | 等级 | 出处与可用口径 |
| --- | --- | --- | --- |
| 2015 | Zheng Min 等，"Enpublic Apps: Security threats using iOS enterprise and developer certificates", ACM **ASIACCS** 2015, pp. 463–474 | 2 | 由 USENIX '26 论文引用并核对结论方向：企业/开发者证书被用于**面向公众**的分发。**"面向公众分发"这一命题在 2015 年已被同行评审测量** |
| 2016 | "Enterprise Apps: Bypassing the iOS Gatekeeper"，Black Hat Asia 2016 议题 | 3/4（未取讲稿正文） | 仅作时间点证据：该议题至少自 2016 年已进入安全会议议程【https://infocondb.org/con/black-hat/black-hat-asia-2016/enterprise-apps-bypassing-the-ios-gatekeeper】 |
| 2022-03 | 最高人民检察院官网转载报道《"超级签名"揭露地下网络犯罪生态》 | **4（检察系统公开材料）** | 【https://www.spp.gov.cn/spp/zdgz/202203/t20220328_552074.shtml】。可用：该业态**已被司法机关作为犯罪生态立案报道**。不可用：其涉案金额与人数属**个案司法统计**，不得外推为行业规模 |
| 2022 | Hu Qinyu 等，"Measurement of the Usage of Web Clips in Underground Economy"，arXiv:2209.03319 | 2/5（**预印本，本次未见同行评审确认**） | 记述 WebClip 被用于赌博、诈骗、色情类内容传播。引用须标预印本状态，且不得据其推定量级 |
| 2024 | Guo Yanhui 等，"Beyond App Markets: Demystifying Underground Mobile App Distribution via Telegram"，**POMACS** 8(3) | 2 | 记录即时通信渠道上的地下分发，**含 TestFlight、WebClip 等通道**——说明观测面比"越狱用户"宽得多 |
| 2026 | USENIX Security '26（本章骨架）；iOSModZoo（ACM WiSec 2026）；Iranian iOS App Stores（ACM IMC 2026） | 2 | 前者的数字见 16.2–16.4；后两篇仅题录级（全文未取，见 16.4 第六点） |

**关于 2022 年那份检察系统公开材料，需要专门写清可用范围。** 它在本篇体系里的价值是**定性**：一种以证书与设备注册为核心的中介业态，已被检察机关以公开报道形式作为犯罪生态处理，且被同行评审论文（USENIX '26 参考文献 [34]）引为其"超级签名/证书代签"现象的来源之一。这条"司法定性 + 学术引用"的组合，比任何规模数字都更能支撑"该业态结构性存在"的判断。它的不可用范围同样明确：**报道中的涉案金额、涉案人数属具体案件的司法统计口径**，与"iOS 改造分发行业规模"之间没有任何可换算关系；把它们写成行业数字，是简报判定的"普遍不可靠数字"的典型形态。

**三条时间纵深带来的读法，供本章收尾使用。** 第一，企业/开发者证书被用于面向公众分发，**自 2015 年即有同行评审测量**，因此"这是近年才出现的问题"不成立。第二，分发载体在 2022–2024 年间被观测到跨越 Ad Hoc、In-House、TestFlight、WebClip 与即时通信渠道，因此"把防线集中在某一条通道上"在证据层面不成立。第三，**同一时间段内没有出现过一份把 iOS 重签名检测当作独立研究对象的同行评审论文**（16.4 第六点），即：**滥用侧有 2015 年起的连续观测，检测侧的学术观测在 iOS 上是空档。** 这条不对称是本报告"iOS 侧证据密度低于 Android"的一个具体实例，其完整口径声明见 17.2。

## 16.6 哪些关于灰产规模的说法本报告拒绝采用

本节以清单形式列出**本报告拒绝采用**的说法及其拒绝理由。写成清单而不是散在正文里，是为了让下游写手可以直接据此校对：凡出现在左列的句式，一律不合规。

| 拒绝采用的说法 | 常见来源 | 拒绝理由 | 允许替代写法 |
| --- | --- | --- | --- |
| "移动黑产市场规模 XX 亿（元/美元）" | 各类咨询机构与厂商营销白皮书 | 等级 3 上限；简报明确判定：**凡市场规模、灰产规模、损失金额且来源为厂商/咨询机构营销白皮书的，一律不可用于事实陈述**；且此类报告普遍无方法学、互相转引 | 只可写"某机构（及其商业立场）如此声称"，作为趋势参照 |
| "设备农场占全部欺诈案例 5.8%" | 广告风控厂商博客的二手转引 | 数字无一手测量、无方法学，属互相转引；**任何此类百分比不得作为事实陈述** | 只可写"行业把设备农场/机器人/模拟器视为共同滥用载体"（存在性），以及"厂商口径普遍把 Android 列为主要载体"（趋势参照） |
| "超过 3,000 万设备使用越狱系统" | Kellner 2019 转引 Cydia 方面说法 | **服务提供方自述、无方法学**，2018 年口径；无可对比的独立测量 | 不引用；如需说明规模感知，写"论文转引服务方自述，标注不可用于事实陈述" |
| "iOS 灰产站点共 3,359 个 / 改造包 8,216 个" | 对 USENIX '26 数字的脱口径引用 | 把**研究观测下界**写成总量；丢掉地理、通道、时间窗三项局限 | "在 2025 年 3–5 月 passive DNS 窗口、地理聚焦中国、仅 Ad Hoc 自助签名通道的观测口径下，识别出不少于 3,359 个活跃签名站点" |
| "iOS 侧没有抢票/自动化滥用" | 对 USENIX '24 论文局限的误读 | 原文只写"我们只关注 Android 应用"，并把 iOS 样本稀缺归因于**App Store 审核要求**——这是**作者的因果解释，不是测量结论** | "研究者以 App Store 审核要求解释 iOS 样本稀缺；本篇不据此推定 iOS 无此类滥用" |
| "因为新机型无法越狱，所以改造分发的市场不存在" | 13.5 的芯片断言 + 16 章结构 | 前提本身无一手/学术依据；且 16.2/16.3 观测到的通道**不以越狱为前提** | 分开表述：绕过通道有版本上界；改造分发观测集中于证书与设备注册配额 |
| "App Attest 已被破解 / App Attest 无懈可击" | 工程博客、社区讨论 | **零篇同行评审绕过或弱点研究**（17.3）；两个方向的断言都超出可核实范围 | "本报告不假装知道该机制的实际绕过成本" |
| 站点全表、IPA 清单、dylib 样本 | 论文的敏感清单 | 作者**为免助长滥用而拒绝公开**，已上报 Apple | 不复现、不索取、不"补全"；只引用论文给出的汇总数字 |
| "行业平均毛利率 900%–3,000%" | 对 16.3 利润率的升格引用 | 该区间是论文对其观测样本的**加价测算**，非行业平均；地理与通道均受限 | "论文在其观测样本上测得中间层加价形成 900%–3,000% 的利润率" |

**拒绝清单背后的三条判据，可推广到本章之外的所有灰产材料。判据一：来源等级与陈述强度必须匹配。** 等级 5（社区、聚合站、AI 摘要）只能提出待核实线索；等级 4（有具名记者与一手素材的媒体报道、执法/监管公开材料）可支撑事件存在性，其规模数字须核对口径；等级 3 对**对手能力存在性**的陈述可单源使用但须标口径，对**自家产品有效性**的自评一律降级为营销。这三条是简报 A 节的硬约束，本章逐条执行。

**判据二：凡缺失分母的量级叙述一律拒收。** 灰产规模之所以难以核实，是因为可观测面（站点、条目、价格）与不可观测面（用户数、安装量、金额）之间没有换算桥梁。16.2 的全部数字都落在可观测侧；一旦叙述跨到不可观测侧，就必须有独立的估计方法——本次没有取得任何这样的方法。

**判据三：拒绝的写法必须显式记录，不能只是不写。** 本表的价值在于：读者在别处看到这些数字时，报告里已有一份"为什么这里没有"的说明。沉默会让被拒的说法在下一版复活——这与 13.3 对 `corebaseless` 的处置是同一条纪律。

**本章口径边界**

- 本章所有数字同源于 USENIX Security '26 一篇等级 2 论文，全部为**研究观测下界**；引用时**观测口径 + 时间窗 + 作者自陈局限**三件套强制同现（地理聚焦中国、仅 Ad Hoc 自助签名通道、2025 年 3–5 月窗口、样本不可完整复核）。
- 变体数与条目数不是安装量、用户数或收入；"约半数"一类换算须标注为**本篇分析推断**；利润率区间不得升格为行业平均。
- 等级 3/5 的规模、金额、百分比一律标注**不可用于事实陈述**；检察系统公开材料只用于司法定性与事件存在性，其涉案金额与人数不得外推。
- 本章只写生态结构与激励，**不含任何运作方法**（正文不记实现）；论文拒绝公开的敏感清单不复现、不补全。
- 术语统一：iOS 侧沿用"改造版应用（modified app）"口径，不与 Android 的 repackaging 指标互相换算；重签名与二次打包的定义见 16.4 首段。
- 证书滥用谱系与第三方商店政策面归《iOS 供应链安全分析报告》第四章；本篇只取同行评审测量数据与 Apple 一手处置机制（第 15 章）。

---

# 证据密度的不对称：模拟器、云真机与自动化在 iOS 侧的缺口

## 17.1 本次检索的负面结果与三类可用依据

本章是全篇的证据学收尾。它要交付的不是"iOS 上模拟器与自动化滥用有多少"，而是一个更难看但更诚实的结论：**这一子领域在本次核查中几乎是空的，而它的空，是可观测性造成的，不是风险不存在造成的。**

**一、负面结果（先立住，再谈可用依据）。本次核查明确未取得：**

| 未取得的条目 | 具体状态 |
| --- | --- |
| 以 **iOS 模拟器 / iOS 云手机**为对象的滥用规模测量 | **零篇**。无任何同行评审论文或执法材料以此类对象给出测量 |
| 以 **iOS 设备为对象**的设备农场/群控大规模测量 | **零篇**。可核实的设备农场研究均为 Android 或跨平台账号自动化（Mirian et al. WWW 2019；DeKoven et al. IMC 2018，两篇均由 USENIX '26 论文引用，**均非以 iOS 为对象**） |
| App Attest / DeviceCheck 的**独立弱点或绕过研究** | **零篇**（详见 17.3） |
| 各检测信号的**误报率或可伪造成本量化排序** | **零篇**（14.5、17.4） |
| Apple 关于**模拟器/虚拟化环境下 DeviceCheck/App Attest 行为**的说明 | 本次已取的全部 Apple 文档页 **均无 "simulator"/"emulator" 字样** → 属"**Apple 未公开**"，不是"不适用"，也不是"已防御" |
| 中文可检索的"iOS 云手机/ARM 云手机原生支持 iOS 应用"表述 | 全部来自产品营销页与技术博客（如 https://www.yun88.com/product/7720.html 、CSDN 论坛文），**等级 5，判定为不可用于事实陈述**；同批检索中亦存在相反口径的宣称（"目前没有合法的、能在云端运行的 iOS 模拟器"），来源同为 SEO 页面，**同样不构成依据** |

最后一条值得多写一句：**同一命题上同时存在"已实现"与"不可能"两种公开宣称，且两者都来自不可定级的来源**，这是本报告判定该主题"不可用"的最直接理由——不是缺乏讨论，而是讨论双方都没有可核查的技术支撑。允许写入的表述止于："市场上存在此类宣称，但无可核证技术支撑。"

**二、可用依据一：iOS 虚拟化在技术上存在，且有明确的司法与产业记录（等级 4，多源相互一致）。** Apple 于 2019 年 8 月起诉一家提供 iOS 等设备虚拟化的安全研究平台，称其复制 iOS 构成侵权【https://arstechnica.com/tech-policy/2019/08/apple-sues-virtual-iphone-vendor-that-helps-hackers-find-ios-bugs/】；地方法院 2020 年驳回 Apple 主要主张；2021 年 8 月 Apple 撤诉并达成**未披露条款的和解**【https://techcrunch.com/2021-08-11/apple-drops-its-lawsuit-against-maker-of-iphone-emulation-software/ ；https://www.forbes.com/sites/thomasbrewster/2021-08-11/apple-v-corellium-iphone-copyright-lawsuit-settles/ ；https://www.washingtonpost.com/technology/2021-08-10/apple-drops-corellium-lawsuit/】；2023 年 5 月美国第十一巡回上诉法院拒绝恢复 Apple 的版权主张【https://www.reuters.com/legal/apple-loses-bid-revive-us-copyright-claims-over-ios-simulation-2023-05-08/】；2023 年 12 月双方就该案余部和解【https://www.theregister.com/software/2023-12-18/apple-corellium-settle-ios-virtualization-case/】。

**可用口径**：iOS 镜像可在非 Apple 硬件上以虚拟化方式运行，这一点由诉讼与和解事实确认；**但该服务的合法性论述建立在"安全研究用途"之上，且其商业形态闭源、付费**。**不得推定"黑产可自由租用 iOS 虚拟化实例"**——这是本报告在本节最重要的否定式约束：司法结果确认了"能跑"，没有确认"能以什么条件被谁跑到多大规模"。

**三、可用依据二：学术一手确认"iOS 侧自动化分析依赖闭源虚拟化平台"这一结构性限制（等级 2）。** HALY 写明 MobSF 的 iOS 动态分析**限定于 Corellium VM——一个闭源、付费的服务提供平台**，而 HALY 自身要求"任意可越狱的 iOS 设备"，同时其越狱覆盖受限于 iOS 16、实验机型为 A11【L2 https://martina.lindorfer.in/files/papers/haly_eurosp25.pdf】。**这两条合起来是"iOS 对抗面研究门槛高于 Android"的最强一手依据**，也是 17.2 口径声明的两根支柱：运行时研究要么买闭源虚拟化，要么受越狱版本上界约束，两条路都有硬门槛。

**四、可用依据三：测试用途的"云真机"是公开产品（等级 3，厂商文档）。** BrowserStack 宣称提供 2 万+ 真实 Android/iOS 设备远程测试【https://www.browserstack.com/real-device-cloud 、https://www.browserstack.com/test-on-iphone】；Google Firebase Test Lab 公布 iOS 真机测试机型清单【https://firebase.google.com/docs/test-lab/ios/available-testing-devices】。**可用范围**：支撑"真实 iPhone 机群可被按需租用、且以设备池形式商业化"这一**能力存在性**。**不可用范围**：两家产品定位均为测试，其规模数字属厂商自述（等级 3 上限），**不得用作滥用规模**，也不得由"可租用"推出"已被用于滥用"。

**五、F2 的术语分层必须在本章落地。** 三类对象在中文报道里长期互相指代，本篇分开命名：

| 类别 | 定义 | 可核实状况 |
| --- | --- | --- |
| 以牟利为目标的设备机群（灰产设备农场/群控） | 成批真机的集中控制与复用 | **iOS 侧为本次缺口**；Android 侧有测量研究 |
| 以测试为卖点的设备云（商业服务） | 真机远程测试资源池 | 有等级 3 厂商文档；用途定位合法 |
| 以虚拟化为核心的研究平台 | 在非 Apple 硬件上虚拟化运行 iOS | 有等级 4 司法与产业记录，闭源付费 |

三者的证据等级与用途各不相同，**不得互相指代**：拿第三类证明第一类的规模，或拿第二类的存在证明第一类的可行，是本章判定为不合规的两类推断。

## 17.2 不对称的三个结构性来源与必须整段落地的口径声明

**一、整段口径声明（按核查简报要求落地，本篇正文照此表述，不得缩写）。**

> 本报告的对抗面叙述在 iOS 侧证据密度低于 Android；这一不对称主要源于可观测性与研究工具链的限制（虚拟化依赖闭源付费平台、大样本分析依赖越狱覆盖、应用商店审核压制此类样本进入分发端），**不得据此推定 iOS 侧风险更低**。

这句话在本篇出现三次：1.5（诚实边界）、本章、以及第 18 章之后的结论汇总。它不是免责声明，而是一条**方法论结论**——17.1 的三类可用依据与五项负面结果就是它的证据。

**二、三个结构性来源，逐个给出可核实的支撑句。**

| 来源 | 支撑证据 | 等级 | 强度评估 |
| --- | --- | --- | --- |
| **虚拟化依赖闭源付费平台** | MobSF 的 iOS 动态分析"限定于 Corellium VM——一个闭源、付费的服务提供平台"（HALY 原文）；该平台的合法性论述建立在安全研究用途之上 | 2（技术栈限制）+ 4（司法记录） | **强**：一手论文自述 + 独立司法事实相互印证，且都指向"研究者无法自建、只能租用"这一结构 |
| **大样本分析依赖越狱覆盖** | HALY：其越狱"limited to iOS 16"、实验机型 iPhone 8（A11）、**190 个（5.7%）iOS 应用因需在已越狱设备上做内存转储而无法解密**、部分应用因版本不兼容无法安装；13.4 的通道谱系表亦记述 TrollStore 仅限较旧 iOS 版本 | 2 | **强**：这是研究者对"我为什么拿不到这批样本"的直接自述，可核算为具体百分比（5.7%） |
| **分发端的准入压制** | USENIX Security '24 抢票生态论文在局限性一节明文写道"我们只关注 Android 应用，未纳入 iOS 应用，主要原因是 iOS 应用商店的更严格要求导致此类应用数量本身有限" | 2（**作者的因果解释，不是测量结论**） | **中**：方向可用，强度受限。必须写成"研究者以 App Store 审核要求解释 iOS 样本稀缺"，**不得写成"iOS 无抢票/自动化滥用"** |

第三个来源的强度需要再强调一次，因为它是三处里唯一容易被写坏的。前两个来源的表述是**事实自述**（我用的工具只能这样、我的越狱覆盖只能到这里）；第三个是**因果解释**（我认为样本少是因为审核严）。二者在证据学上不同级：前者可复核，后者不可。因此本篇把第三个来源写成"解释"而不是"原因"，并把它的适用面限定在"应用侧样本进入分发端"这一环节，不外推到"滥用行为本身不发生"。

**三、把不对称摊开：Android 有而 iOS 没有的四类研究。** 这张表的用途是让"iOS 证据少"变成一个可检查的清单，而不是一句形容词。

| 研究类型 | Android 侧可核实条目 | iOS 侧可核实条目 |
| --- | --- | --- |
| 设备农场/群控规模测量 | 有（如抢票生态 USENIX '24：758 个应用、3,121 个版本；账号自动化与设备农场类研究 WWW 2019、IMC 2018） | **本次未取得任何以 iOS 设备为对象的大规模设备农场测量** |
| 重打包/改造检测的体系化研究 | 有完整谱系：AYPE（NDSS 2011）、AppInk（ASIACCS 2013）、DroidMarking（ASE 2014）、Shao 等（ACSAC 2014）、Repackage-proofing（DSN 2016）、VAhunt（CCS 2020）、ModZoo（APWG eCrime 2024） | **未取得把"iOS 重签名包"作为独立研究对象并给出检测方法的同行评审论文**；最接近的是 USENIX '26 对 dylib 注入改造的测量与 ASIACCS 2015 对证书滥用的测量 |
| 平台级设备证明采用率的专门测量 | 有：AttestLens（ACM WiSec 2026）专门测量 Play Integrity 采用率 | **只找到 HALY 顺带给出的 App Attest 采用率（静态 4.1% / 动态 1.1%）**【题录级来源：http://wisec26.events.cispa.de/program-detailed-program/ 】 |
| root/越狱检测有效性的多年复测 | 多轮：Evans 2015 → 2024 年研究 → HALY 2025；Sun 等对 182 个 Android 应用判定"这些方法无效" | Kellner 2019 → HALY 2025 → ESORICS 2025 workshop 复测（后一篇仅方向级） |

**四、由此得出的正确读法：这是"可观测性低"，不是"风险低"。** 三项结构性来源全部落在**研究者一侧**（买不买得到虚拟化、越狱覆盖到哪里、样本进不进得来分发端），没有一项落在**对手一侧**（对手是否不具备这些能力）。因此任何把证据缺口读成安全结论的句子，在本篇体系里都是口径错误。反例自查清单：

- 「iOS 上没有设备农场研究」→ 正确句子是「本次未取得 iOS 设备农场测量；可核实的同类研究均以 Android 或跨平台账号为对象」。
- 「iOS 模拟器滥用未被记录，所以不构成威胁面」→ 正确句子是「iOS 模拟器/云手机滥用的公开测量缺失；Apple 亦**未公开**模拟器环境下 DeviceCheck/App Attest 的行为（其文档无 simulator 字样）」。**"未公开"不是"不适用"，更不是"已防住"。**
- 「App Store 审核严，所以改造应用少」→ 正确句子是「研究者以审核要求解释 iOS 样本稀缺；同时改造分发的可观测面（16.2）说明样本并未缺席，只是不进入 App Store 侧的观测视野」。

**五、这条口径对本报告设计结论的实际影响。** 它不改变任何一条设计结论，只改变**论证方式**：正因为 iOS 侧对抗证据稀疏，本篇把可验证性押在平台签发材料（第 5–9 章、第 9 章 MDA）与业务侧服务端校验（14.6、16.3）上，而**不押在"端侧环境检测已经覆盖足够多攻击面"这一未经证实的前提上**。换句话说，证据缺口本身就是把责任推向服务端的最强论据之一——**在无法观测的地方，不要假设自己已经防住。**

## 17.3 自动化滥用的证据口径，与 App Attest 弱点研究的零篇结果

**一、iOS 侧自动化/刷量的公开证据，主要来源是平台方自述，而非第三方风控报告——这条修正要先写。** 核查中对预期说法"iOS 侧证据主要来自平台方风控报告"的判定是**需要修正**：可取得的**最强一手来源是 Apple 自己的公开白皮书**，属**平台方自述**；而可核实的学术测量（USENIX '24 抢票生态）**明确排除 iOS**。两者合起来才是本节的真实口径。

**Apple 白皮书的数字与口径限制（等级 1，自述）。** 来源：《Building a Trusted Ecosystem for Millions of Apps》，本地取到的 PDF 封面标注 **June 2021**，外部学术与监管引用口径为 October 2021（两种版本并存，见 15.5 末段），**统计口径为 2020 年**【https://www.apple.com/privacy/docs/Building_a_Trusted_Ecosystem_for_Millions_of_Apps.pdf 】。原文数字：平均每周由 500 余名专家审核约 10 万个新应用与更新；约 100 万个有问题的新应用（及相近数量的更新）被拒绝或下架，其中 **>150,000** 因垃圾/仿冒/误导用户、**>215,000** 因违反隐私准则、**>48,000** 因含隐藏或未记录功能、约 **95,000** 因欺诈类违规（主要是 "bait and switch"）；阻止超过 **15 亿美元**的潜在欺诈交易；以欺诈相关理由驱逐 **470,000 个开发者团队**、拒绝近 **205,000 次**开发者注册申请；因欺诈与滥用行为（**含虚假评论**）停用 **2.44 亿个用户账号**、拒绝 **4.24 亿次**账号创建尝试。

**这组数字的四条限制必须与数字同时出现：** ①**Apple 单方自述**，无方法学附录、无第三方复核；②口径是「**Apple 主动处置行为计数**」，**不等于滥用行为的实际发生量**（被拦截的与被漏掉的都不在其中）；③**截至 2020 年**，不得用于"当前趋势"推断；④本次**未取得同口径的后续版本**——Apple 在此后的公开发布中未以同一口径重复这组数字。另需注明：这组数字属于 **App Store 审核与开发者账号侧**，不可移用为企业证书吊销侧的量化（15.5）。

**二、可核实的学术测量为什么在 iOS 上不可用，以及它仍给出的一条结论。** USENIX Security 2024「Tickets or Privacy? Understand the Ecosystem of Chinese Ticket Grabbing Apps」测得 **758 个抢票应用、3,121 个版本**，把生态拆为"抢票应用—代抢服务—目标平台"三方，其动态分析使用 Android 模拟器（LDPlayer），并**排除在模拟器中表现不佳的应用、部分应用需 root 环境**；论文在局限性一节明文写"只关注 Android 应用，未纳入 iOS 应用"【L2 https://www.usenix.org/system/files/usenixsecurity24-liu-yijing.pdf 】。该论文对本篇仍有一条可直接使用的等级 2 结论：**目标平台"持续改进检测机制"，自动化应用相应演化规避手段**（其 RQ3 即关注应用使用何种方法规避目标平台检测）。**这是"平台检测与自动化规避构成持续竞赛"这一表述的唯一等级 2 支撑**；至于规避方法本身，**正文不记实现**。账号黑产的基线文献（Mirian et al. WWW 2019；DeKoven et al. IMC 2018）**均非以 iOS 为对象**，引用时须标注。

**三、厂商风控材料的可用范围（等级 3，方法学透明度低）。** 可核实存在性的来源：AppsFlyer 移动广告欺诈指南【https://www.appsflyer.com/blog/measurement-analytics/mobile-ad-fraud-for-marketers/】、Fingerprint 设备农场检测指南【https://fingerprint.com/blog/how-to-detect-device-farm-fraud/】、Branch【https://www.branch.io/resources/blog/how-device-farms-fuel-ad-fraud-in-fintech/】、myTracker【https://tracker.my.com/blog/update-a-single-metric-to-detect-bots-and-emulated-devices】、Scalarr【https://scalarr.io/blog/articles/the-anatomy-of-mobile-ad-fraud-device-farms/】（其中出现"2018 年设备农场占全部欺诈案例 5.8%""Android 设备更常用于设备农场"这类**二手转引、未标注一手测量**的数字）。**判定**：可用于"行业把设备农场/机器人/模拟器视为 iOS 与 Android 共同的滥用载体"这一**存在性**表述，以及"厂商口径普遍把 Android 列为设备农场主要载体"的**趋势参照**；**其中任何百分比都不得作为事实陈述**。

**四、iOS 侧唯一可量化的一条相关事实，量的不是滥用而是防护覆盖。** HALY 在 2,646 个 iOS 应用上测得：运行时检查 root 相关文件/目录权限者为 **24.9%**，检查是否安装了 root 相关应用者为 **6.8%**【L2】。这两个数字的正确用法是**量化"iOS 应用自身对自动化/篡改环境的实际防护覆盖极低"**，而不是量化自动化滥用的规模——**iOS 侧没有任何一项被核实的滥用规模测量**，这一点必须同时说清，否则读者会把防护覆盖率误读为案件率。

**五、App Attest / DeviceCheck 的公开弱点讨论：零篇同行评审研究。** 预期说法是"公开弱点讨论多为工程博客"；核查结果被**加强**为：**本次未检索到任何同行评审的 App Attest 或 DeviceCheck 绕过/弱点研究（数量为零）**。检索行为包括多轮英文检索（App Attest bypass / DeviceCheck abuse / attestation weakness iOS research）以及 HALY 的相关工作一节。可核实的只有一条**学术界的自我承认**：HALY 把 App Attest 归入 device binding 类技术，并明确写道 **device binding 的实现高度依赖具体应用，使得自动检测困难，这些技术的流行度需要更多研究**【L2】——即**学术界自陈这一块证据不足**。其余可检索到的讨论均为等级 3–5（厂商博客、工程博客、社区帖、AI 生成的聚合内容），且大多为**观点性表述、无可复核实验**，**不得据以撰写任何"绕过成本"结论**。

**在此前提下，本报告允许写入的只有三类句子：**

| 类别 | 允许的句子 | 等级与措辞要求 |
| --- | --- | --- |
| Apple 自认的能力边界 | 「A compromised version of your app running on a genuine, unmodified Apple device can't create valid assertions. **However, an attacker that modifies the device's operating system might bypass restrictions.**」 | L1，须标"Apple 原文"；后半句是**厂商自认的能力存在性** |
| Apple 的自评式定性 | 「Although modifying the operating system is difficult and **an unlikely source of widespread fraud**…」 | L1，须标"**Apple 文档自评、非测量结论**"，**不得据此降低风险等级**；同段 Apple 点名的滥用形态是"**一台被攻陷设备为大量订阅者提供断言**" |
| Apple 承认的另一条绕过语义 | 「A compromised version of your app could falsify the verification result, thus **circumventing App Attest**.」（attestKey 页） | L1；这正是"结果必须由服务端校验"的一手依据，与 14.6 第一条互锁 |

**六、这条缺口的意义必须写进正文，而不是省略。** 由于既无独立绕过研究、又无 Apple 的攻防数据披露（Apple 未公开 App Attest 的攻防胜率、被绕过案例、对具体越狱或重签名方案的结论；risk metric 的计算定义亦未公开，仅有 "approximate count" 措辞），本报告的唯一诚实立场是：

> **本报告不假装知道 App Attest / DeviceCheck 的实际绕过成本。**

这句话同时封堵两个方向的过度结论：**不能宣称"该机制已被破解"**（无任何同行评审绕过研究，也无 Apple 承认的具体绕过案例）；**也不能宣称"该机制无懈可击"**（Apple 自己写明修改设备操作系统者**可能**绕过限制，且该机制的判定语义被厂商标为 approximate / might be，其宿主前提——操作系统未被改动——恰好落在第 13 章所讨论的那条通道上）。**一条既无攻击研究、又无防御数据的机制，其风险位置只能标为"未公开"**，交由后续建议章节以"按自身误报容忍度设定阈值并持续自测"的方式处理。这也是本篇与第 5–9 章的分工边界：机制语义有 L1 一手依据，攻防效果**没有**。

## 17.4 检测信号可靠性分级：可依据素材与不得自制排序表的纪律

预期说法是"很难找到把检测信号按易伪造程度排序的严肃来源"。核查判定为**部分推翻**：**量化排序表确实不存在**，但存在可支撑**定性分层**的等级 2 依据。本节因此做两件事：把可依据素材摊开，并立一条纪律——**不得自制排序表冒充事实**。

**一、五组可依据素材（逐条挂来源与等级）。**

| 编号 | 素材 | 原文口径 | 等级 |
| --- | --- | --- | --- |
| 素料 1 | **信号强弱判据：用途唯一性** | HALY 为每个发现打置信度分，依据是"**该信号存在的其他合理用途**"，并举例：在 root 检测里，**搜索某个具体越狱名称是一个强指标，而调用 `fork` 则可能有别的用途**（searching for a specific jailbreak name is a strong indicator, whereas calling fork may serve other purposes as well） | 2 |
| 素材 2 | **模糊信号会被正常代码淹没（实测）** | 约 **95.7%** 的 iOS 应用调用 `_dyld_image_count`、**74%** 同时调用 `_dyld_image_count` 与 `_dyld_get_image_name`；这两个函数既被用于枚举已加载动态库以发现注入组件，也被大量正常代码使用，作者把相关结果标记为**高不确定性（uncertain）** | 2 |
| 素材 3 | **同一信号跨类别，计数不可加总** | 针对某越狱配套 hooking 框架的检测**既算 hooking 检测又算 root 检测**（因为该框架只能装在越狱设备上），故各类别计数**加总不等于 100%**；HALY 亦写明"越狱与 hook 检测在 iOS 上界限模糊" | 2 |
| 素材 4 | **条件式可靠性判据（最强的一条可落地结论）** | USENIX '26：客户端权益/环境校验"**只在缺少服务端校验的应用中有效**"；"**仅依赖客户端机制是不够的**"；**运行时完整性检查（监控非预期内存改动）对 dylib 类篡改有效** | 2 |
| 素材 5 | **Apple 一手对信号性质的自我标注** | risk metric 用 **approximate** / **might be an indication** 措辞；DeviceCheck 的两位数据「you're responsible for keeping track of what the bits mean」，并需在用户转让设备时复位 | 1 |

素材 5 值得单独评一句：**Apple 自己的文档就是"信号不自带语义、可靠性取决于使用方"的最佳依据**——一个被厂商标为 approximate 的计数，和一个由开发者自定义含义并自行复位的比特位，都无法在任何排序表里占据"强信号"的位置，除非使用者自己给出阈值与其误报代价。这与 14.5 的"误报率无公开数据"共同构成本节的核心限制。

**二、负面结果：量化排序表不存在。** 本次检索**未发现**任何同行评审研究给出"iOS 各越狱/篡改检测信号的**误报率或可伪造成本量化排序表**"。「检测存在大量误报」仍停留在等级 3（厂商）与等级 5（社区个案）层面（14.5）。标准文档侧同样不提供排序：**OWASP MASVS/MASTG 只规定应实现哪些韧性类别与如何测试**——可引用的归纳为四个 Resilience 类别：R1 平台完整性校验（root/仿真/截屏/键盘检测）、R2 防篡改与证书固定、R3 抗静态分析、R4 抗动态分析【L2，转述自 HALY】——**但不给信号之间的可靠性排序**。Android 侧存在专门测量平台证明采用率的研究（AttestLens，ACM WiSec 2026），iOS 侧只有 HALY 顺带给出的 App Attest 数字，这本身也是 17.2 那张不对称表的又一行。

**三、纪律条款（三条，全部为硬约束）。**

1. **不得自制"信号—易伪造度"排序表冒充事实。** 任何把"越狱检测信号"排成 1、2、3 级并标注"易伪造程度：低/中/高"的表格，若无同行评审来源，即属本篇禁止的写法。
2. **允许"公开研究可支撑的定性分层 + 逐条挂来源与等级"**；表内任何超出来源的强度差异，一律显式标注**「分析推断」**。
3. **全部量化列（检出率／误报率／绕过成本）标注「未公开」**；不得用"较低/较高"等形容词变相填充实测值，也不得把不同论文的信号计数相加或排成互斥等级（素材 3）。

**四、按上述纪律，本节唯一允许输出的分层形态如下（定性、逐条挂来源、量化列一律未公开）。**

| 层级（自上而下） | 分层依据 | 依据等级 | 该层能给出的最强句子 | 检出率／误报率／绕过成本 |
| --- | --- | --- | --- | --- |
| 服务端可验证的平台签发材料 | 素材 4 的"仅依赖客户端机制是不够的"；素材 5 的 Apple 自我标注；第 5–9 章机制语义 | L2 + L1 | 关键逻辑与环境校验应交由服务端；客户端结果不可自证 | **未公开**（17.3：零篇同行评审绕过研究） |
| 运行时完整性监控 | 素材 4 原文："对 dylib 类篡改**有效**" | L2 | 监控非预期内存改动对注入类改造有效 | **未公开**（论文给方向，未给检出率/误报率） |
| 用途唯一的端侧特征 | 素材 1：具体越狱名称搜索为"强指标" | L2 | 该类信号误报可能性低（判据为"其他合理用途少"） | **未公开**；且受 14.4 的陈旧化限制（清单不随攻击面更新） |
| 用途不唯一的端侧信号 | 素材 1 的 `fork` 例；素材 2 的 95.7% / 74% 实测 | L2 | **本质上无法区分正常与异常**，作者标为 uncertain | 误报率**无公开数据**（14.5） |
| 端侧特征文件/路径存在性 | 14.4 的陈旧化测量 + 素材 3 的界限模糊 | L2 | 只作线索输入，不可作判定 | **未公开** |

**这张表读作的三件事**：它是**方向性**分层，不是强度量化；第四、五行与"可绕"结论的关系由第 14 章支撑，但**支撑方式是"这些信号在实现与执行层面不可靠"，不是"已测得其误报率"**；表中"服务端可验证"一行不等于"设备真实性已判定"——第 3 章已确立该机制证明的是密钥与 App 实例，设备真实性只是代理指标，且 Apple 明文声明无法确定性指认操作系统被攻陷的设备。

**本章口径声明与本章边界**

- 本章的核心口径声明（17.2 整段落地方）：**本报告的对抗面叙述在 iOS 侧证据密度低于 Android；这一不对称主要源于可观测性与研究工具链的限制（虚拟化依赖闭源付费平台、大样本分析依赖越狱覆盖、应用商店审核压制此类样本进入分发端），不得据此推定 iOS 侧风险更低。**
- 五项负面结果一律以"本次未取得／零篇"表述；**"Apple 未公开 X"不得写成"不存在 X"，也不得写成"X 不适用"**（尤其模拟器/虚拟化环境下 DeviceCheck 与 App Attest 的行为）。
- 三个术语类别不得互相指代：牟利导向设备机群（iOS 侧为缺口）／测试导向设备云（等级 3 厂商文档）／虚拟化研究平台（等级 4 司法记录）；由第二、三类的存在推定第一类的规模，属不合规推断。
- Apple 白皮书全部数字须标注：**Apple 自述、口径为 2020 年、无方法学附录、代表处置行为计数而非滥用发生量**；不得用于当前趋势，不得移用于企业证书吊销侧。
- 厂商风控材料中任何百分比一律标注**不可用于事实陈述**；"Android 更常用于设备农场"只作趋势参照；USENIX '24 的"iOS 样本少因审核严"是**作者因果解释**，不得写成"iOS 无此类滥用"。
- App Attest / DeviceCheck：**零篇同行评审绕过或弱点研究**；报告立场固定为"**不假装知道实际绕过成本**"，两个方向的断言（已破解／无懈可击）均不予采用；Apple 的 "unlikely source of widespread fraud" 必须标注为**厂商自评、非测量结论**。
- 信号可靠性只输出**定性分层 + 逐条挂来源等级**，**禁止自制量化排序表**；不同论文的信号计数不得相加、不得排成互斥等级；量化列统一标注**未公开**；超出来源的强度差异标注**分析推断**。
- 全篇不涉及越狱实现、检测点位、重签名流程、伪造证明或规避风控做法；凡对手能力只记**存在性、失效结论与证据强度**，实现层一律标注**「正文不记实现」**。
---
# 防线交叉矩阵：四类对抗 × 四层防线的有效性判定

## 18.1 汇总方法：把第 5–17 章的结论投影到同一坐标系

前十几章是按机制逐条展开的：每一项 Apple 能力、每一份公开研究、每一组灰产观测，各自在自己的章节里被核实、被限定、被降级。本章的任务不是新增事实，而是把第 5–17 章已经落定的结论**投影到同一个二维坐标系**上，使「某类对抗能力在某层防线上究竟处于什么状态」这个问题第一次可以在一张表里被读到。投影过程遵循三条约束：

1. **不引入两份核查简报之外的新事实。** 每一格的判定值都必须能回溯到前文某一章已给出依据等级与出处的陈述；无法回溯的格子一律写「公开证据不足」，不得留空，不得由读者或作者自行填补。
2. **不合并口径。** 静态检出率与动态执行率不合并、不同研究样本不合并、厂商自述不升格为实测、下界观测不外推为总量。这条约束直接来自第 14 章与第 17 章确立的口径纪律。
3. **不给绝对排序。** 本章只提供条件式判断（第 18.7 节），因为可支撑分级的严肃来源本身缺失。

坐标系的两轴按以下方式定义。**行轴为四类对抗能力**，按「对手在破坏什么」而非「对手用了什么工具」划分：

| 行 | 对抗能力类 | 该行的本体问题 | 结论主要来自 |
| --- | --- | --- | --- |
| R1 | 被篡改的 App 副本 | 运行在服务端面前的，还是那份被审核与分发过的代码吗 | 第 12、14、16 章 |
| R2 | 越狱或被改动的系统 | 宿主操作系统本身是否仍可被信任为信号的采集环境 | 第 7、13、14 章 |
| R3 | 非真实或批量设备 | 屏幕后是否真的存在一台独立、真实、由人操作的终端 | 第 8、16、17 章 |
| R4 | 身份与标识伪造 | 设备身份、序列号、标识符、证书与账号是否被冒充或复用 | 第 9、10、11、15、16 章 |

需要说明的是，Apple 文档在 R2 这一行**不使用「越狱」一词**（平台安全指南与 DeviceCheck 全部已取页面 `jailbreak` 零命中，第 13 章），其官方措辞是 "a device with a compromised operating system"。本矩阵沿用行标签「越狱或被改动的系统」是为读者便利，实际依据一律取自 Apple 的 compromised 口径与论文的 root/jailbreak detection 口径。

**列轴为四层防线**，按「判定发生在哪一层、由谁执行」划分：

| 列 | 防线层 | 执行主体 | 结论主要来自 |
| --- | --- | --- | --- |
| C1 | 端侧 App 自检 | 开发者的 App 代码本身 | 第 12、14 章 |
| C2 | 平台侧密码学可验证信号 | Apple 提供、开发者校验：DeviceCheck、App Attest、Managed Device Attestation | 第 5–10 章 |
| C3 | 服务端判定与风控 | 开发者的服务器与风控体系 | 第 3、8、12、15 章 |
| C4 | 生态侧政策与分发控制 | Apple 的政策条款、审核、证书与账号准入 | 第 11、15、16 章 |

投影结果拆为两个面板（第 18.3、18.4 节），原因是 R1/R2 的判定主要围绕「宿主与包完整性」展开，而 R3/R4 的判定围绕「身份与规模化」展开，两组格子引用的是完全不同的机制与章节；强行合并成一张 4×4 大表会让每格的限定语失去可读空间。矩阵之后给出判定值计数汇总（第 18.5 节）、对「公开证据不足」一格的误读警告（第 18.6 节），以及强度排序为何只能是条件式的（第 18.7 节）。

## 18.2 判定值四档与依据等级标注约定

每格只允许出现以下四个判定值之一。四档之间的分界是**依据的性质**，不是「强弱程度」：

| 判定值 | 定义 | 成立条件 | 不可读作 |
| --- | --- | --- | --- |
| 有官方一手信号支撑 | 存在 Apple 一手文档明确定义的、可在服务端校验的字段、流程或明文声明，且该信号与本行对抗类之间存在官方陈述或本报告可核的关联 | 依据等级 1，且能给出文档页与字段名 | 「该对抗已被防住」 |
| 仅启发式证据 | 只有基于特征、路径、字符串、统计的端侧或第三方判定，无密码学绑定；或官方信号被厂商用 approximate / might 类措辞自我限定 | 依据等级 2、3，或等级 1 的自评限定句 | 「无效」或「可靠」 |
| 公开证据不足 | 本次核查未取得可核实条目；两种成因需在依据要点中分清：① Apple 明文不提供该判定（责任层归属问题）② 公开检索与研究供给缺失（观测层问题） | 依据等级 5、6 或缺口清单条目 | 「安全」「不存在该风险」 |
| 不在该层管辖范围 | 该层在机制设计上不针对本行对抗类提供手段，或其成立前提（纳管、集成、可用性）在此完全不适用 | 由 Apple 一手文档的能力边界陈述支撑 | 「该层失职」 |

**依据等级沿用对抗面简报的六级体系**：等级 1 = Apple 一手（平台安全指南、开发者文档、开发者计划协议、支持页、安全公告）；等级 2 = 同行评审论文与学位论文；等级 3 = 安全厂商技术报告；等级 4 = 有具名记者与一手素材的媒体调查、执法与监管公开材料；等级 5 = 二手转述；等级 6 = 无法定级。等级 3 对「产品有效性」的自评一律降格为营销，只有对「对手能力存在性」的陈述允许单源使用并须标注口径。

必须提醒一处**跨简报的等级标度冲突**：机制面简报另用 L1/L2/L3 标记「取文通道」——其 L1 指 developer.apple.com 文档接口返回内容与平台安全指南本地副本，L2 指 App Store Review Guidelines、MDM Protocol Reference 等「一手但间接」材料，L3 指第三方。这与对抗面简报的「等级 2 = 同行评审论文」**同名不同义**。本矩阵统一采用六级体系；凡引用机制面简报材料时，其 L1/L2 内容一律折算为等级 1，其「未核实（公开资料未披露）」折算为等级 6。该冲突在附录 F 中并列记录。

每格还需附第三类标注：**适用前提**。C2 一列的多数格子隐含「设备受管 / App 已集成 / 系统版本达标 / 证明链可校验」等前提，前提不成立时该格判定值自动降级；降级规则统一写在依据要点里，而不另设第五种判定值，以免破坏四档封闭性。

## 18.3 面板 A：被篡改的 App 副本、越狱或被改动的系统 × 四层防线

### 18.3.1 R1 被篡改的 App 副本

Apple 对该对抗的威胁建模是一手且直白的：框架页原文 "Someone who modifies your app and distributes it outside the App Store can add unauthorized features like game cheats, ad removal, or access to premium content."（等级 1，第 12 章）。场景词只到 `game cheats`、`ad removal`、`access to premium content`、`promotional offer`、`fraudulent` 为止，Apple 未使用 `bot`、`emulator`、`device farm` 等词（第 12 章负面结果）。

| 防线层 | 判定值 | 依据等级 | 回指章号 | 依据要点与适用前提 |
| --- | --- | --- | --- | --- |
| C1 端侧 App 自检 | 仅启发式证据 | 1 + 2 | 第 12、14 章 | Apple 一手否定该层可信性："You can’t rely on your app’s logic to perform security checks on itself because a compromised app can falsify the results."（等级 1）；`attestKey` 页另写 "A compromised version of your app could falsify the verification result, thus circumventing App Attest."（等级 1）。HALY 实测静态口径 96.7%、动态执行口径仅 24.9%（等级 2，两口径不可合并）。前提：端侧任何自检结论均不可作为放行依据 |
| C2 平台侧密码学可验证信号 | 有官方一手信号支撑 | 1 | 第 6、12 章 | App Attest 证明对象包含 `apple_validation_category_01`（签名身份类别 0–10，逐档原文见第 12 章）与 `apple_bundle_version_01`（分发版本号）；服务端须校验 `RP ID`（App ID 的 SHA256）、`x5c` 证书链与 `credentialId`。限定一：Apple **未**给出「重签名必落哪一档」的断言，只能按「签名身份类别」表述。限定二：该格成立要求 App 已集成 App Attest 且设备 `isSupported` 为真（第 7 章）。DeviceCheck bits 不针对本行（仅两枚开发者自定义位）；MDA 不覆盖 App 层 |
| C3 服务端判定与风控 | 有官方一手信号支撑 | 1 | 第 6、8、12 章 | Apple 把处置点明确放上服务端：`isSupported=false` 时服务端「can no longer require assertions」，验证明失败即丢弃 keyId，断言失败「it’s your responsibility to decide how to handle the request」（等级 1）。可校验集合为一次性 challenge、`counter` 单调递增、receipt 创建时间 ≤5 分钟、字段 3 公钥与首次存库一致。限定：Apple 不提供阈值与处置规则，该层信号可校验但结论需自定 |
| C4 生态侧政策与分发控制 | 有官方一手信号支撑 | 1 | 第 11、12 章 | 政策侧可引条款为 2.5.1（仅可用公开 API、须运行于当前发布系统）与 2.3.1(a)（把推广 iOS 病毒/恶意软件扫描类能力列为处置理由）（等级 1，以抓取当日版本为准）。机制侧平台安全指南（August 2026）写明运行期对**每个可执行内存页**在载入时校验签名，对象是「自安装或上次更新后是否被改动」（等级 1）。限定：这既非针对篡改副本的检测服务，也不覆盖「以另一有效证书重新签名后」的包 |

面板 A 中 R1 行的整体读法：**该对抗是四层里唯一在 C2/C3/C4 都拿到一手可校验信号的行**，但这三层的信号共同依赖同一个前提——App 真的集成了 App Attest。第 14 章引用的实测采用率为静态 4.1% / 动态 1.1%（等级 2，本简报关于 App Attest 采纳度唯一可核实的测量值），意味着多数 iOS 应用实际停在 C1 一格里。

### 18.3.2 R2 越狱或被改动的系统

本行的关键事实是：**Apple 对宿主系统真实性不提供判定服务**，并两次明文承认上限（等级 1，第 7、12、14 章）：

- 框架页："No single policy can eliminate all fraud. For example, App Attest can’t definitively pinpoint a device with a compromised operating system."
- `assessing-fraud-risk`："A compromised version of your app running on a genuine, unmodified Apple device can’t create valid assertions. However, an attacker that modifies the device’s operating system might bypass restrictions."

| 防线层 | 判定值 | 依据等级 | 回指章号 | 依据要点与适用前提 |
| --- | --- | --- | --- | --- |
| C1 端侧 App 自检 | 仅启发式证据 | 2 | 第 14 章 | Kellner 等（EuroS&P 2019）：34 个银行应用中实现检测者「除 1 个之外全部检测失败」，绕过条件是现成的函数 hook，无需逆向亦无需修改应用本身（等级 2，限定于 2018–2019 年作者的 hook 能力与检测实现条件下）。HALY（EuroS&P 2025）：针对 iOS 15（2021-09）及之后发布的越狱，未发现任何专用特征检测（等级 2）。ESORICS 2025 workshop 论文把 rootless 越狱作为条件重估检测有效性，但其样本量与百分比本次未取得，故本格不引用其任何数字。误报率无公开测量（等级 3 厂商承认 + 等级 5 个案） |
| C2 平台侧密码学可验证信号 | 公开证据不足 | 1（否定式）+ 6 | 第 7、9、10 章 | 三种缺失并列：① DeviceCheck/App Attest 文档不存在「是否越狱」类 API，全部页面 grep `jailbreak` 零命中（等级 1 负面结论）；② MDA 在 iOS 侧可证明的属性只有序列号、UDID、sepOS 版本、freshness code，以及 iOS 17.2+ 的 SoftwareUpdateDeviceID、OS 版本、LLB 版本，**SIP 状态、Secure Boot 模式、第三方 kext 许可三项 Apple 只写作 macOS 14.2 引入**（等级 1）；③ 独立学术绕过研究零篇（等级 6 缺口）。限定：本格成因为「Apple 明文不提供该判定」，属责任层归属，不等于「已证明平台信号在该对抗下必然失效」 |
| C3 服务端判定与风控 | 仅启发式证据 | 1 | 第 8 章 | 唯一的官方间接输入是 receipt 字段 17：近 30 天该设备上 attested key 的**近似**计数，Apple 措辞为 "A count that’s higher than expected might be an indication of a compromised device"（等级 1），且同一文档写明 "it isn’t possible to detect fraudulent activity with absolute certainty"。该计数会被重装、备份恢复、设备转让正当推高，Apple 要求开发者自行 "tune your risk assessment logic"。限定：本格降为启发式，是因为信号被厂商自述为 approximate / might，不自带判定语义 |
| C4 生态侧政策与分发控制 | 公开证据不足 | 5 + 6 | 第 13、17 章 | Apple 的政策与统计口径集中在分发与账号侧（App Store 审核、开发者驱逐、账号停用），未公开任何针对「宿主被改动」的处置口径或量化数据（等级 6）。可用的只有等级 5 的社区形态叙述（tethered 系列、rootful/rootless），而同行评审中无以该术语为分类框架的学术定义（第 13 章）；「A12 及以上长期无公开越狱」未获一手或学术依据，禁止写成绝对断言（等级 6）。限定：本行「越狱覆盖面存在机型与系统版本上界」这一抽象结论有等级 2 支撑，但由此推出「政策压力充足」属外推 |

面板 A 中 R2 行与 R1 行构成对照，也构成本篇的**核心张力**：同一批 Apple 文档，一处写 "no process can ever directly read or modify it"（SE 内私钥，等级 1），另一处写 "an attacker that modifies the device’s operating system might bypass restrictions"（等级 1）。两句必须并置引用，任何只引其一的表述都会失真。

## 18.4 面板 B：非真实或批量设备、身份与标识伪造 × 四层防线

### 18.4.1 R3 非真实或批量设备

本行是整张矩阵最空的一行。原因不是「这类对抗不存在」，而是**iOS 侧关于模拟器、虚拟化与云真机被大规模滥用的可核实证据在本次核查中几乎不存在**，而 Apple 文档对虚拟化与非真机环境下的机制行为**从未公开说明**（全部已取 DeviceCheck/App Attest 页面 `simulator` / `emulator` 零命中，第 7、17 章）。

| 防线层 | 判定值 | 依据等级 | 回指章号 | 依据要点与适用前提 |
| --- | --- | --- | --- | --- |
| C1 端侧 App 自检 | 公开证据不足 | 6 | 第 14、17 章 | 未取得任何以 iOS 非真机环境（模拟器/虚拟化/云机）为对象、评估端侧识别有效性的同行评审测量（等级 6 缺口）。可参照的只有 Android 侧结论（Sun 等 182 个应用「这些方法无效」，等级 2 转述），**禁止换算为 iOS 结论**（第 14、17 章口径不对称）。限定：本格是「研究供给缺失」型证据不足，与 R2/C2 的「Apple 明文不提供」型不同成因 |
| C2 平台侧密码学可验证信号 | 公开证据不足 | 1（否定式）+ 6 | 第 7、8 章 | App Attest 证明的是「key 由真实 Apple 设备上、指定签名类别的 App 实例生成」（等级 1），因此对**批量真实设备**没有区分能力：每台真机都能合法取得各自的 key。对**非真机环境**，Apple 未公开 `isSupported` 的机型白名单、未公开模拟器上的取值、未公开虚拟化环境行为（等级 6，第 7 章「未公开」而非「不适用」）。限定：不得把「Apple 未说明」写成「Apple 已阻断」，也不得写成「Apple 已允许」 |
| C3 服务端判定与风控 | 仅启发式证据 | 1 | 第 8 章 | 可核的官方输入仍是近 30 天 attested key 近似计数（receipt 字段 17，等级 1）——它恰好对应 Apple 一手点名的滥用形态："an attack that uses a single compromised device to serve assertions to many subscribers"（等级 1）。防重放三件套（一次性 challenge、`counter` 递增、创建时间 ≤5 分钟）是服务端可校验的，但**它们防的是复用而非规模化**。限定：Apple 未公开该指标的计算定义与精度，阈值必须按自身业务分布自调 |
| C4 生态侧政策与分发控制 | 公开证据不足 | 2 + 4 + 6 | 第 15、16、17 章 | 分发控制确实在起作用，但其强度对本报告不可见：Apple 白皮书 470,000 个开发者团队被驱逐、2.44 亿账号被停用等数字，是**Apple 单方自述、口径为 2020 年、无方法学附录、代表处置行为计数而非滥用发生量**（等级 1 文档但自评口径受限，第 16 章）。以 iOS 设备农场为对象的大规模测量未取得；「iOS 云手机可跑原生 iOS 应用」的中文表述全部来自营销页与技术博客（等级 5，判定不可用于事实陈述）。厂商风控把设备农场视为共同滥用载体（等级 3），其任何百分比均不可用于事实陈述 |

### 18.4.2 R4 身份与标识伪造

本行的特殊性在于：Apple 在**机制上有意不提供跨 App 的稳定设备 ID**，同时在**纳管场景提供密码学可证的设备身份**。两者方向相反，必须分开表述。

| 防线层 | 判定值 | 依据等级 | 回指章号 | 依据要点与适用前提 |
| --- | --- | --- | --- | --- |
| C1 端侧 App 自检 | 不在该层管辖范围 | 1 | 第 11 章 | 标识符由系统返回，端侧代码无从辨别其真伪：IDFA 在模拟器、macOS、visionOS 兼容运行、未请求授权、用户拒绝、配置描述文件限制等情形返回全零；IDFV 在删光同 vendor 应用后重装、Xcode 测试构建、ad-hoc 安装时会变，且重启未解锁前为 `nil`（等级 1，第 11 章）。DeviceCheck token 被官方定义为 ephemeral / single-use，App Attest key 有意不跨重装存活（等级 1）。限定：端侧在此行不是「弱」，而是**不承担该判定** |
| C2 平台侧密码学可验证信号 | 有官方一手信号支撑 | 1 | 第 9 章 | Managed Device Attestation 提供 OID `1.2.840.113635.100.8.9.1`（序列号）、`.9.2`（UDID）、`.9.4`（sepOS 版本）、`.10.2`（freshness code），链根为 Apple Enterprise Attestation Root CA，Apple 自述其用途是 "help protect against various threats, such as a compromised device providing false information"（等级 1）。硬性前提四条：设备受管并具备相应 access right、A11/M 系列及以上、`x5c` 可追溯到该 CA、freshness code 与请求时下发的 `DeviceAttestationNonce` 相匹配；前提任一不成立即整格降为「公开证据不足」。已知瑕疵：Mac 上 `.9.2` 等于 `ProvisioningUDID`，与 MDM 协议他处的 UDID 不一致（等级 1） |
| C3 服务端判定与风控 | 有官方一手信号支撑 | 1 | 第 6、9、15 章 | 服务端可校验的身份要素：`RP ID`（= SHA256(App ID)，App ID = 10 位 Team ID + "." + bundle ID；**没有独立的 Team ID 字段**）、`x5c` 链、`aaguid` 环境标记、receipt 字段 2（须为本 App 的 App ID）；证书吊销侧有 Apple 一手机制陈述——首次启动联系 OCSP、被吊销则应用不启动、响应在设备缓存 3–7 天、吊销发行证书会使以其签名的全部应用失效（等级 1，第 15 章）。限定：缓存窗口与「网络侧无法回连 Apple」构成两个已公开的失效条件 |
| C4 生态侧政策与分发控制 | 有官方一手信号支撑 | 1 | 第 11、15 章 | 可引原文：5.1.2(i) "You must receive explicit permission from users via the App Tracking Transparency APIs to track their activity."；5.1.2(iii) 禁止秘密构建用户画像、禁止识别匿名用户或据 Apple 所提供 API 的数据重建画像；ADEP 页限定「仅用于组织内部专有应用、仅私密安全地分发给员工」，并要求 100 名及以上员工、法人实体、D-U-N-S 编号、持续审查（等级 1）。**关键限定**：Guidelines 全文 `fingerprint` 出现 0 次，故不得写成「Apple 禁止设备指纹」，只能写 5.1.2(iii) 的画像重建口径 |

面板 B 中 R4 行的 C2/C4 两格，是全矩阵中「政策与机制齐备」程度最高的一组，但其成立前提也最重：C2 依赖纳管与硬件代际，C4 依赖条款解释口径而非技术强制。

## 18.5 判定值计数汇总

| 判定值 | 格数 | 涉及格子（行/列） | 主要依据等级分布 |
| --- | --- | --- | --- |
| 有官方一手信号支撑 | 6 | R1/C2、R1/C3、R1/C4、R4/C2、R4/C3、R4/C4 | 等级 1 × 6 |
| 仅启发式证据 | 4 | R1/C1、R2/C3、R3/C3、R4 无 | 等级 1（自评限定）× 2、等级 2 × 1、等级 1+2 × 1 |
| 公开证据不足 | 5 | R2/C2、R2/C4、R3/C1、R3/C2、R3/C4 | 等级 1 否定式 × 2、等级 2 × 1、等级 5–6 × 3（部分格子跨级） |
| 不在该层管辖范围 | 1 | R4/C1 | 等级 1 × 1 |
| 合计 | **16** | 4 行 × 4 列，无空格 | — |

计数本身可读出的三条结构性事实（均为对上表的算术描述，不引入新事实）：

1. **等级 1 出现在 12/16 格里，但方向不一致。** 其中 2 格是 Apple 的**否定式**一手陈述（不提供该判定、能力上限自认），3 格是 Apple 的**限定式**自述（approximate / might / rule of thumb）。也就是说，一手材料的主要作用是把边界画清楚，而不是把防线强度抬高。
2. **「公开证据不足」5 格全部落在 R2 与 R3 两行**，即宿主被改动与非真机/批量设备。这两行恰好是业务侧最关心、而公开材料最稀薄的区域。
3. **C1 一列没有任何一格取得「有官方一手信号支撑」**，且其中一格被判为「不在该层管辖范围」。这是 Apple 一手原文 "You can’t rely on your app’s logic to perform security checks on itself"（等级 1）在矩阵上的直接投影，而不是本报告的倾向性结论。

## 18.6 「公开证据不足」为什么不能读作「安全」

矩阵里 5 格写着「公开证据不足」。这五格最容易被误读成「该对抗在该层不起作用，因此可以放心」，必须逐条拆掉这种读法。误读的来源是把「证据不足」当成了「风险不存在」，而两者在方法论上根本不同：**前者描述的是我们的观测条件，后者描述的是对手的能力边界。**

五个格子的成因还各不相同，混为一谈会造成二次误读：

| 格子 | 「证据不足」的真实成因 | 若误读为「安全」会付出什么代价 |
| --- | --- | --- |
| R2/C2 | Apple **明文不提供**该判定（等级 1 否定式结论 + 独立绕过研究零篇） | 把「平台不说越狱」当成「平台能防越狱」，进而把全部放行决策压在 App Attest 单一信号上；而 Apple 已写明改机者「might bypass restrictions」 |
| R2/C4 | Apple 不以「某芯片代是否有公开越狱」的口径发言，该项天然只能靠等级 2–4 支撑 | 用「政策已收紧」替代「技术已覆盖」，忽视分发通道清单（USENIX '26 Table 2 把 Jailbreak、TrollStore 列为 Permanent 通道，等级 2） |
| R3/C1 | 同行评审研究供给缺失；Android 同类结论不可换算 | 认为端侧没有识别需求，从而放弃本可补充服务端证据的启发式提示（须标注不可信根） |
| R3/C2 | Apple **未公开**模拟器/虚拟化下的机制行为（「未公开」≠「不适用」，也 ≠「已阻断」） | 默认非真机一定拿不到合法断言；而批量真实设备场景下每台都能合法取得 key，这一格连「未公开」都不是，是直接无区分能力 |
| R3/C4 | 未取得以 iOS 设备农场为对象的测量；营销级「iOS 云手机」表述被判定不可用 | 用厂商白皮书百分比或市场规模数字替代缺失的测量，把等级 3/5 材料升格为事实 |

三条通用规则：

- **规则一：证据不足格子的默认处置是「提高监控与保留独立证据链」，不是「降低防护」。** 第 20 章据此把「不可因无信号就假定安全」写成服务端层的强制项。
- **规则二：不得用「本报告未找到反例」构造正面结论。** 机制面简报的纪律是「凡未公开一律写 Apple 未公开 X，不得写不存在 X」（其 E 节第 12 条）；对抗面简报同样规定缺口「不得以推测填补，也不得据此断言事实不存在」。
- **规则三：下界数字不可外推。** R3/C4 与 R2/C4 里唯一可引的量化观测（3,359 个活跃签名站点、8,216 个非官方 IPA、28 个上游证书站点等）全部是**研究观测下界**，作者自陈地理聚焦中国、仅覆盖 Ad Hoc 自助签名这一条通道，且敏感清单不公开、第三方无法完整复核（等级 2）。这些数字用于说明「该通道存在且已资产化」，不用于说明规模总量。

## 18.7 四层防线的强度排序为什么只能给条件式答案

本节回答一个必然被问到的问题：四层防线能不能排个强弱。答案是**不能给绝对排序，只能给条件式排序**，理由是一条负面结果：本次核查**未发现任何同行评审研究给出 iOS 各越狱/篡改检测信号的误报率或可伪造成本量化排序表**（第 14、17 章，等级 6）。可获得的只有**定性判据**，因此任何形如「平台侧 > 服务端 > 端侧」的通用排序，都是在给一个不存在的量化表背书。本报告不制作这样的表。

可以站的住的是下面这一组**条件式陈述**，每条都挂具体来源与等级，且都不声称覆盖全部情形：

| 条件 | 在该条件下的可支撑排序方向 | 依据等级与出处 | 条件不成立时的后果 |
| --- | --- | --- | --- |
| App 已集成 App Attest、设备 `isSupported` 为真、服务端完成证明链与 `counter` 校验 | 平台侧可验证信号 + 服务端校验 > 端侧自检 | 1（Apple 一手「不可依赖应用自证」+ 断言流程）、2（USENIX '26：客户端校验「只在缺少服务端校验的应用中有效」） | 该排序整体不适用：Apple 明确要求服务端「can no longer require assertions」，即降级放行，且不提供替代判定 |
| 设备已 MDM 注册、具备 Device Information access right、硬件为 A11/M 系列及以上 | 平台侧 MDA 在设备身份字段上优先于其余三层 | 1（MDM Protocol Reference OID 清单与校验页） | 未受管（BYOD/个人）设备无任何可用字段，该层直接归零 |
| 对抗目标为 dylib 注入类的运行期改动 | 运行时完整性监控（监控非预期内存改动）对这类篡改「有效」，但前提仍是设备未被改动 | 2（USENIX '26 原文结论，等级 2 唯一给出的正向有效性判断） | 「有效」的口径来自作者样本与方法，不外推为通用结论 |
| 判定信号本身为通用 API 调用 | 具体越狱名称检索是强指标；`fork`、`_dyld_image_count` 类调用因用途不唯一而弱 | 2（HALY 置信度判据；实测 95.7% 的 iOS 应用调用 `_dyld_image_count`、74% 同时调用二者，作者把相关结果标为高不确定性） | 该判据只支持定性分层，不含检出率/误报率，不可换算成百分比强度 |
| 对抗类为批量真实设备（R3） | 没有任何一层取得可校验的区分能力，只剩计数与统计输入 | 1（Apple 自述 approximate / might）+ 6（无 iOS 农场测量） | 排序问题在该行退化为「采用率与阈值设定问题」 |

除来源缺失之外，还有两处结构性原因使绝对排序不成立：

- **采用率使排序失去意义。** 平台侧信号若未被集成便不存在。唯一可核的 iOS 采用率测量是 App Attest 静态 4.1% / 动态 1.1%（等级 2）；同一研究还测得 24.9% 的 iOS 应用真正检查 root 相关文件/目录权限、6.8% 检查是否安装 root 相关应用（等级 2）。一张「机制强度表」若不看采用率，会系统性高估生态整体防护水平。
- **类别互斥性不成立。** HALY 自陈检测 Cydia Substrate 同时属于 hooking 检测与 root 检测，故其各类别计数加总不等于 100%（等级 2）。这意味着不同层的证据**不能相加、不能排成互斥等级**，任何「总分」都是伪造。

因此本矩阵输出的最终形态是：**每格一个判定值 + 一个依据等级 + 一组适用前提 + 一个章号回指**，由读者按自身业务条件在格子上取值，而不是由本报告替读者算出一个可比较的分值。这也是本篇与《iOS 核心保护作用》第 16 章那种「威胁 × 保护层」打分矩阵的分工差别：本篇不提供分数，只提供分数的**证据上限**（等级 1 口径，见附录 F 的系列分工对照）。

## 本章口径边界

本章的全部材料来自两份核查简报与第 5–17 章已落定的结论，**未新增任何事实**。判定值只有四档，无法归类的格子一律写「公开证据不足」，不存在空格、不存在推测性填充、不存在自制排序。依据等级采用对抗面简报的六级体系，机制面简报的 L1/L2 取文通道标记已折算，两者同名不同义的冲突记录于附录 F。版本口径统一为「截至 2026 年 8 月版 Apple 文档」与「截至 2026-10-19 的检索」。三处不可逾越的边界须在本章层面重申：其一，矩阵中的任何格子都不构成「某对抗已被防住」或「某对抗不受威胁」的判断；其二，等级 1 出现次数最多，但其内容有一半是 Apple 的否定式与限定式陈述，一手依据的密度不等于防护强度；其三，「公开证据不足」的成因在 R2 与 R3 两行不同（官方不提供判定 vs 观测供给缺失），后续任何加固动作对这两类成因的响应方式也不同，第 20 章按此分别给动作。

# 失效模式与共性前提

## 19.1 共性前提一：判定责任外包——Apple 给信号与近似计数，不给阈值与处置

本篇讨论的所有机制共享第一条前提：**机制的「存在」不等于防线的「生效」，两者之间隔着一层由开发者自担的判定责任。** 这层责任不是推测出来的，是 Apple 一手文档明文分配的。

先把口径校准准确。常见说法是「Apple 不提供任何设备风险信号」，这一说法**不成立**；正确口径（第 8 章的修正结论）是：**Apple 提供信号与一个近似计数指标，但不提供阈值与处置规则。** 三处原文共同支撑这条修正：

| 原文（英文为 Apple 措辞） | 出处层级 | 它分配了什么责任 |
| --- | --- | --- |
| "No single policy can eliminate all fraud. … Instead, the DeviceCheck services provide information that you can integrate into an overall risk assessment for a given device." | 等级 1（DeviceCheck 框架页） | 「政策」由开发者制定；Apple 只交付信息 |
| "Although it isn’t possible to detect fraudulent activity with absolute certainty, App Attest does provide a metric to assess its likelihood. Specifically, you can get an approximate count of unique attestations for your app on a particular device." | 等级 1（`assessing-fraud-risk`） | 提供的是**似然指标**而非判定；且用 approximate 自我限定 |
| "This growth should be modest, but you’ll have to tune your risk assessment logic based on the typical numbers that you see over time." | 等级 1（同上） | 阈值标定明确归开发者，且要求以自身历史分布为依据 |
| "If the assertion object fails verification, it’s your responsibility to decide how to handle the request." | 等级 1（服务端校验指南） | 失败处置不给建议，全部归业务侧 |
| "Apple records the bits for you, and reports the bits back to you, but you’re responsible for keeping track of what the bits mean." 及 "You’re also responsible for determining when to reset the bits for a given device; for example, when a user sells the device to someone else." | 等级 1（`DCDevice`） | bit 的语义与**重置时机**都在开发者服务端 |

因此，任何「Apple 没防住 X」的表述都必须先做一次**责任层落位**：该主张指向的是 Apple 未提供机制（真缺口，如不提供越狱判定服务，等级 1 负面结论），还是 Apple 提供了机制但未提供判定（责任分配设计，等级 1 明文），抑或开发方未集成、未校验、未设阈值（业务侧缺口，如第 14 章的采用率测量）。三种情形在矩阵里落在不同格子上，混用会得出完全错误的加固方向。

- **谁会破坏该前提**：把平台信号当结论使用的风控设计；把「Apple 没说不能」当「Apple 已允许」或把「Apple 没提供」当「Apple 已防住」的合规叙述；未在服务端完成校验、仅收集客户端上报的实现。
- **破坏后哪些可观测信号随之改变**：风控决策不再能追溯到具体校验步骤，于是同一断言结果在不同业务线得到不同处置；误报投诉无法用阈值变更来解释；risk metric 被直接当布尔量硬卡后，重装、备份恢复、设备转让三类正当增长会立刻表现为批量拒绝（Apple 一手已列举这三个增长来源）。这些变化都可从服务端日志自查，不需要外部证据。

## 19.2 共性前提二：平台侧信号依赖纳管与可校验的证明链

第二条前提是范围性前提：**平台侧最强的设备完整性信号，只在设备受管且证明链可校验时存在。** Managed Device Attestation 的整条价值链上有四个必须同时成立的环节（等级 1，第 9、10 章）：MDM 注册并具备 Device Information access right → 硬件为 A11 Bionic / 全部 M 系列及以后（A10x 及更早的 iPhone/iPad、Intel Mac、Apple TV、Watch S4 之前的设备直接忽略 `DevicePropertiesAttestation` 与 `DeviceAttestationNonce` 两个键）→ `x5c` 链可追溯到 Apple Enterprise Attestation Root CA（该根**不在响应里**，需从 Apple Private PKI Repository 自行取得并长期信任）→ freshness code 与本次下发的 nonce 匹配。

Apple 对这条链的表述本身就把判定权交给了校验方："If the leaf certificate doesn’t chain to this CA, the attestation isn’t trustworthy. **There’s no scenario during normal operations in which this validation step fails.** You can choose to ignore the attestation entirely, or flag it as an incident that needs investigation." 以及 "Any missing OIDs or blank values indicate that Apple’s attestation servers were unable to reliably determine the corresponding property."——即 Apple 允许「完全忽略」，也允许「按事件上报」，二选一由管理员定，Apple 不指定。

必须并列的第二半是**纳管场景之外的空白**：第 10 章的负面结果清单显示，`DeviceInformation` 响应的 95 个键名中不存在任何 jailbreak / 越狱 / root / Cydia / integrity / trust-level / risk 字段；整个 Device Management 抓取范围无 `jailbreak` 字样；没有任何 MDM 字段适用于未受管（BYOD/个人）设备。而 iOS 侧可 attestate 的属性里**没有 SIP/Secure Boot/第三方 kext 等价项**——Apple 只把这三项写作 macOS 14.2 引入；`SystemIntegrityProtectionEnabled` 同样仅 macOS。

该前提同时解释了 iOS 与 macOS 在字段可用性上的差异：macOS 受管设备可以把 SIP 状态（0/1）、Secure Boot 模式（Full/Reduced/Permissive Security）、第三方 kext 许可（0/1）作为可校验断言；iOS 受管设备只能断言序列号、UDID、sepOS 版本、OS 版本、LLB 版本、SoftwareUpdateDeviceID 与 freshness。策略文档若按 macOS 的字段假设 iOS 也可得，会在评审时直接失效。

- **谁会破坏该前提**：BYOD 或未注册 MDM 的设备；A10x 及更早硬件；未下载或未正确信任 Apple 私有 CA 根的服务端；忽略空值/缺 OID 语义而把「空白」当「否」的解析实现。
- **破坏后哪些可观测信号随之改变**：`DevicePropertiesAttestation` 键不出现在查询响应中；OID 缺项或值为空（Apple 明文三种可能：生成空值、整体省略 OID、拒绝签发证明证书）；链验证在到达 Apple 根之前中断；旧硬件设备上两个 attestation 键被静默忽略，表现为「字段永久缺失」而非「校验失败」。这几类都可以在 MDM 侧日志与解析层自查，区分「前提不成立」与「设备异常」需要对照硬件代际表。

## 19.3 共性前提三：密码学信号只证明「设备 + App 安装」这一事实，不证明运行时未被削弱

这是本篇的核心张力，因此要把两句 Apple 一手原文并排放在一起，不允许只引其中一句：

| 一侧：密钥保护的强陈述 | 另一侧：宿主判定的弱陈述 |
| --- | --- |
| "The device automatically stores the associated private key in the Secure Enclave, from where the App Attest service can use it to create signatures, but **from where no process can ever directly read or modify it**, ensuring its security."（等级 1，`establishing-your-app-s-integrity`） | "**an attacker that modifies the device’s operating system might bypass restrictions.**"（等级 1，`assessing-fraud-risk`） |
| "The Secure Enclave is isolated from the main processor to provide an extra layer of security and is designed to keep sensitive user data secure even when the Application Processor kernel becomes compromised."（等级 1，Apple 平台安全指南 August 2026） | "App Attest can’t definitively pinpoint a device with a compromised operating system."（等级 1，DeviceCheck 框架页） |

两句不矛盾，因为它们的主语不同：前者的主语是**密钥**，后者的主语是**发出断言的那个运行时**。App Attest 证明的对象，按 Apple 自己的措辞是 "a statement from Apple about the validity of the key associated with `keyId`"、"demonstrates the legitimacy of an instance of your app running on a device"——即**某个 key 由真实 Apple 设备上、指定签名类别的 App 实例生成**。这就是为什么第 6 章把「设备真实性」限定为「以 App/密钥真实性为代理指标」：证明里没有任何一项是「宿主未越狱」。

同一文档还给出了这套代理指标**唯一被点名的滥用形态**："you might need to guard against an attack that uses a single compromised device to serve assertions to many subscribers."——一台被改动的设备为大量订阅者供给断言。这句话与「密钥不可读改」并不冲突：密钥不需要被读出，只要签名动作发生在被改动的宿主上、被转发出去即可。

与之对照的是越狱检测的启发式性质（第 14 章）。两条边界放在一起看：**端侧检测在原则上是可伪造的（Apple 一手："a compromised app can falsify the results"），平台侧密码学信号在范围上是不完整的（Apple 一手：无法确定性指认被攻陷系统）。** 因此「把检测移到服务端」并不等于「获得了宿主判定」，它只等于「获得了可校验的密钥与签名来源」。

- **谁会破坏该前提**：把断言通过等同于「设备干净」的风控规则；未把 `validationCategory`、`counter`、receipt 字段 2/3/12 全项校验就完成了放行；在 `isSupported` 为假时仍把 App Attest 当唯一防线（Apple 明确要求服务端「can no longer require assertions」，此时该层根本没有信号）。
- **破坏后哪些可观测信号随之改变**：同一 `keyId` 出现多个来源 IP/会话而 counter 仍单调递增（说明断言在被转发）；risk metric 与单设备账号数明显背离；服务端存的公钥与首次证明公钥不一致；`validationCategory` 分布整体偏离该 App 的正常分发结构。这些都需要服务端把校验项逐项落库才可观测——落库缺失本身就是该前提被破坏的证据。

## 19.4 共性前提四：检测有效性受测量口径限制

第四条前提关于**我们如何知道防线有效**。三个已核实的口径差异，使「检测有效性」这个词在 iOS 侧几乎必须带限定语使用：

- **静态检出 ≠ 动态执行。** HALY 在同一 2,646 应用池内测得 iOS 侧 root/jailbreak 检测在静态口径出现于 96.7% 的应用，但动态口径仅 24.9% 真正检查 root 相关文件/系统目录权限、6.8% 检查是否安装 root 相关应用（等级 2）。作者自己据此写「我们的动态分析在 iOS 应用中只发现 25% 实现了越狱检测」，并显式与 Kellner 的 59% 对比。**不写清这两个口径，两个数字看起来互相矛盾；写清了才明白：字符串存在不等于运行时会执行。**
- **不同研究不可合并。** Kellner 2019（34 个银行应用 + 3,482 个最流行应用；59%/53%）、Evans 2015（16 个安全应用 + 19 个 MDM 应用）、HALY 2025（2,646 个/每 OS）、ESORICS 2025 workshop（样本量未取得）四个来源的样本池、判据与年份各不相同；Android 侧同类计数（Sun 等 182 个应用结论为「无效」；Ruggia 2024 的 61%；HALY 的 92.3%）来自四种判据，**不可排成趋势线**（等级 2，第 14 章）。
- **同一论文内部计数不一致。** Kellner 导言称「15 out of 34 银行应用完全不使用该措施」，结论称「only 18 out of 34 使用越狱检测」，两者相加为 33 而非 34（等级 2，第 14 章）。引用时须同时给出两处原文数字，或只引摘要口径「44% 未尝试检测」。

还有一层来自**工具链**的口径限制：HALY 写明 MobSF 的 iOS 动态分析限定于 Corellium VM——一个闭源、付费的服务提供平台，而 HALY 要求任意可越狱的 iOS 设备；同时该团队的越狱覆盖仅到 iOS 16、实验机型为 A11（iPhone 8，iOS 16.4.1，rootful palera1n），并自述 190 个（5.7%）iOS 应用因需在已越狱设备上做内存转储而无法解密（等级 2）。**这意味着「iOS 检测有效性」的测量本身受可观测性约束**，不是单纯的样本量问题。

- **谁会破坏该前提**：把静态检出率当作防护覆盖率呈现的评估报告；跨研究拼接百分比的综述；把 Android 结论换算成 iOS 结论的类比论证；把作者自陈的下界当成总体估计。
- **破坏后哪些可观测信号随之改变**：同一指标在不同段落给出不同数字且无口径标注；表格出现「合计 100%」而各来源类别本就互斥不成立；评估结论的置信度与样本年份明显不匹配（例如以 2018 年口径描述当前状态）。这三类都可在文稿层面自查，属于写法纪律而非取证问题。

## 19.5 共性前提五：生态观测存在地理与通道偏差

第五条前提关于**灰产观测数字的可外推范围**。本次核查中，iOS 侧唯一一组结构完整的灰产基础设施测量来自 USENIX Security '26 的 Ad Hoc 自助签名通道研究，其作者自陈的口径限制必须与该组数字**始终同段出现**（等级 2，第 16 章）：

| 该观测允许说明的 | 该观测不允许说明的 |
| --- | --- |
| 通道存在且高度集中：3,359 个活跃签名站点（从 8,358 候选域名筛出）、2,118 个唯一工具名、仅 28 个上游证书站点负责签发；一个证书站点最多支撑 135 倍以上下游站点，最大集群单站支撑 843 个签名站点 | 灰产总量。全部数字是**研究观测下界** |
| 设备身份已被资产化：个人开发者账号每年最多注册 100 台设备（前 10 台即时通过、其后 24–72 小时审核），对 macOS 的执行放松使实践中可绑定约 200 台，单台绑定成本约 RMB 3.4，终端支付为该成本 10–30 倍，中间层形成 900%–3,000% 利润率 | 全球或全通道规模。作者**自陈地理聚焦中国**、**仅覆盖 Ad Hoc 自助签名这一条通道** |
| 改造包对杀检基本失明：8,085 个抽取 dylib 做 VirusTotal 扫描仅 3 个被标记恶意；2,654 个下载应用中 442 个仅以「破解 VIP」为卖点 | 「多数改造包无害」。作者原结论是「注入组件为增加功能而非分发显性恶意代码；但其 hook 运行时内存、操纵流量、绕过系统控制的行为破坏了应用逻辑并削弱 iOS 安全隔离」 |
| 该清单不可完整复核：站点全表、IPA、dylib 等敏感清单不公开，发现已上报 Apple | 第三方对该样本的独立再验证 |

第二层偏差是**平台方数字的口径**。Apple 白皮书《Building a Trusted Ecosystem for Millions of Apps》的处置规模（每周约 10 万个新应用与更新由 500 余名专家审核、约 100 万个有问题的新应用被拒或下架，其中 >150,000 因垃圾/仿冒/误导、>215,000 因违反隐私准则、>48,000 因含隐藏或未记录功能、约 95,000 因欺诈类违规；阻止超过 15 亿美元潜在欺诈交易；驱逐 470,000 个开发者团队、拒绝近 205,000 次注册；停用 2.44 亿个用户账号、拒绝 4.24 亿次账号创建）属等级 1 文档，但：**Apple 单方自述、无方法学附录、统计口径为「Apple 主动处置行为计数」，不等于滥用行为实际发生量，且数据口径为 2020 年**，不可用于当前趋势推断；本次未取得同口径后续版本。另需注意该白皮书在系列内曾被误标为 2024 年发布：本地 PDF 封面为 June 2021，外部机构引用口径为 October 2021（第 15 章、附录 F）。

第三层偏差是**「证据稀疏」不等于「风险低」**。必须整段保留的口径声明是：本篇的对抗面叙述在 iOS 侧证据密度低于 Android，该不对称主要源于可观测性与研究工具链的限制（虚拟化依赖闭源付费平台、大样本分析依赖越狱覆盖、应用商店审核压制此类样本进入分发端），**不得据此推定 iOS 侧风险更低**（第 17 章）。同一方向的另一条一手旁证是 USENIX Security '24 抢票生态研究在局限一节明文写道「我们只关注 Android 应用，未纳入 iOS 应用，主要原因是 iOS 应用商店的更严格要求导致此类应用数量本身有限」——这是**作者的因果解释，不是测量结论**，不可写成「iOS 无抢票/自动化滥用」。

- **谁会破坏该前提**：把单一国家、单一通道的下界数字写成行业总量的叙述；把厂商或咨询机构营销白皮书的市场规模数字用作事实陈述（本简报判定为**不可用于事实陈述**，只可作趋势参照并写明发布方商业立场）；把 Apple 处置计数当滥用发生量；把「未检索到」当「不存在」。
- **破坏后哪些可观测信号随之改变**：同一份文档里来源等级跨度极大却标注相同置信度；百分比来自互相转引（例如「2018 年设备农场占全部欺诈案例 5.8%」「Android 设备更常用于设备农场」这类未标一手测量的二手转引）；结论从「该通道存在」直接跳到「规模有多大」。这三类都可在引用层面自查。

## 19.6 共性前提六：证据缺口本身是风险，以及「前提 → 破坏条件 → 是否可自查」汇总

第六条前提最容易在实践中被忽略：**在关键防线上没有可核实的公开证据，本身就是一种需要计入的风险状态，而不是中性状态。** 本篇有三处这样的缺口，且都落在决策链上：

1. **App Attest 的独立弱点研究为零。** 本次未检索到任何同行评审的 App Attest / DeviceCheck 绕过或弱点研究（等级 6）；可引的只有 Apple 一手自认的上限句，以及 HALY 自陈「device binding 的实现高度依赖具体应用，使得自动检测困难，这些技术的流行度需要更多研究」（等级 2）。后果是：**报告不能假装知道 App Attest 的实际绕过成本**，任何「已被绕过」或「无法被绕过」的断言在本篇口径下同属未公开。
2. **iOS 模拟器/虚拟化滥用无任何测量。** 未发现任何同行评审论文或执法材料以 iOS 模拟器、iOS 云手机为对象给出滥用规模测量；中文可检索到的相关表述全部来自营销页与技术博客（等级 5，判定不可用于事实陈述）；Apple 全部已取文档页无 `simulator`/`emulator` 字样（等级 1 否定式，属「未公开」而非「不适用」）。
3. **MDM 响应无完整性字段。** `QueryResponses` 的 95 个键名内不存在任何 jailbreak/越狱/root/Cydia/integrity/trust-level/risk 字段（等级 1 否定式），且 iOS 侧可 attestate 的属性不含 SIP/Secure Boot/第三方 kext 等价项。企业若按「MDM 应当报告设备完整性」撰写策略，该策略在 iOS 上**没有可执行字段**。

三处缺口的共同点是：它们都不是「机制被证明无效」，而是「我们无从知道机制在此情形下的表现」。风险恰恰来自这种未知被默认填成「安全」。

把六条前提汇总成一张可核对的表。表中「破坏条件」只记录**前提在何种配置或事实下不再成立**，不含任何破坏做法：

| 前提 | 成立所依赖的条件 | 破坏条件（谁 / 在什么配置或事实下） | 破坏后的可观测变化 | 是否可自查 |
| --- | --- | --- | --- | --- |
| ① 判定责任外包 | 开发方已把信号并入自身风险评估并设定阈值 | 风控把平台信号直接当结论；或从未设阈值也未记录依据 | 处置无法追溯到校验步骤；重装/恢复/转让引发批量拒绝 | **可自查**（服务端配置与变更记录） |
| ② 平台信号依赖纳管 | MDM 注册 + access right + A11/M 系列 + 链可校验 + nonce 匹配 | BYOD 或未纳管；A10x 及更早硬件；未信任 Apple 私有 CA 根；把空值当「否」 | attestation 键缺失、OID 缺项或空值、链在到达根之前中断 | **可自查**（MDM 侧日志与解析层） |
| ③ 密码学信号的证明对象 | 校验方理解证明仅覆盖「设备 + App 实例 + 签名类别」 | 把断言通过等同宿主未越狱；`isSupported=false` 时仍以之为唯一防线 | 同一 keyId 跨来源复用而 counter 仍递增；metric 与账号数背离；公钥不一致 | **可自查**（前提：校验项已逐项落库） |
| ④ 检测有效性受口径限制 | 引用时标明样本池、判据（静态/动态）、年份 | 静态与动态混用；跨研究合并百分比；Android 结论换算为 iOS 结论 | 同一指标出现多个数字且无口径标注；出现不成立的「合计 100%」 | **可自查**（文稿与引用层） |
| ⑤ 生态观测的地理与通道偏差 | 数字带「观测下界 + 作者局限」两件套 | 下界外推为总量；营销白皮书数字当事实；Apple 处置计数当发生量 | 来源等级与置信度不匹配；结论从「存在」跳到「规模」 | **可自查**（引用层） |
| ⑥ 证据缺口本身是风险 | 缺口被显式承认为未知 | 用默认假设填补；把「未检索到」写成「不存在」或「安全」 | 加固清单出现无依据项；矩阵格子被留空或猜测填充 | **部分可自查**：缺口清单本身可核对（附录 D），缺口的真实分布需外部证据 |

「是否可自查」这一列的实际含义是：**六条前提里有五条可以在不引入外部证据的情况下，仅凭自身配置、日志与文稿完成核对。** 这是本章能给读者的最实用结论——多数防线失效不是因为对抗能力超出预期，而是因为前提从未被验证过。

## 本章口径边界

本章只做**归纳**，不做新增：六条共性前提全部是第 5–17 章已核实陈述的重排，未引入任何简报外事实。表中不包含破坏手法、绕过步骤、检测规避方式或攻击准备，只记录「前提在何种配置或事实下不再成立」与「随之改变的可观测信号」，这是本章与任何实现层文档的根本分界。三处必须保留的限定：其一，Apple 的 "an attacker that modifies the device’s operating system might bypass restrictions" 与 "an unlikely source of widespread fraud" 同属**厂商文档自评**，前者只支撑能力存在性、后者不得被用来降低风险等级；其二，「误报存在」有等级 2/3 的公开承认（MASVS 口径「即使以牺牲漏报为代价，也要最小化误报」与厂商知识库陈述），而「误报率高」在 iOS 侧无任何可核实测量，故本章涉及误报的表述只到工程事实层面；其三，六条前提彼此独立且可叠加，一条被破坏不自动意味着其余失效，但第 18 章矩阵 C2 一列的多格以②③为前提——这两条同时不成立时，平台侧多数格子会整体降为「公开证据不足」，第 20 章据此为不受管场景单列动作项。

# 分层加固：服务端、App 侧、企业管控与治理

## 20.1 分层加固的总原则与角色边界

本章按**角色**而非按机制给出动作。理由是第 19 章的结论：防线失效多数发生在「前提未被验证」，而前提的验证责任分散在四个不同角色手里。四条总原则约束本章全部建议：

1. **只涉及配置、验证流程、监控与治理。** 本章不写检测点位清单、不写绕过与规避手段、不写任何可复现的实现步骤。凡涉及对手能力，只以「前提是否成立」「信号可否观测」的语言表述。
2. **信号降级原则。** 凡 Apple 用 approximate / might / rule of thumb 自我限定的输入，一律不得升格为放行或拒绝的唯一依据（等级 1 口径，第 8 章）。
3. **无信号不等于安全。** 在 `isSupported` 为假、设备未纳管、硬件代际不达标、指标接口未取得回执等「无信号」状态下，正确动作是转入备用证据链与加强监控，而不是默认放行。Apple 在 `isSupported=false` 时**不提供替代判定手段**（等级 1，第 7 章），这一空白必须由业务侧策略填补。
4. **口径与责任同处记录。** 每一次阈值设定、每一次降级放行，都应在工单或变更记录里写明依据的文档版本与章号，使未来复核时能区分「Apple 未提供」与「我方未采用」。

| 角色 | 可控范围 | 不可控范围（须向何处取证） |
| --- | --- | --- |
| 服务端与风控 | 校验流程、密钥与状态管理、阈值、降级处置、监控 | Apple 服务内部判定逻辑、risk metric 计算定义、吊销统计（均未公开） |
| App 侧 | 是否集成、集成范围、启发式结果的定位与降级 | 宿主系统是否被改动（Apple 明文不判定）、端侧结论的可信性 |
| 企业与 MDM 管理员 | 纳管覆盖、access right、证明链校验、字段解析与告警 | iOS 侧不存在的 SIP/Secure Boot/kext 等价字段（结构性缺失，非配置问题） |
| 研究与评估者 | 评估口径、样本与判据标注、负面结果的呈现 | 未取得全文的论文数字、Apple 未公开的采用率与误报率 |

## 20.2 服务端与风控层：把真实性信号降级为输入

### 20.2.1 attestation 校验流程与失败降级处置

服务端是四层中唯一能拿到「可校验」结论的层，因此它的加固重点是**把校验项做全并留痕**，而不是提高判定强度。按 Apple 一手清单，一次完整证明校验应覆盖下列项，且每项都需落库以便事后追溯（等级 1，第 6、8 章）：

| 校验项 | 一手要求 | 建议的失败处置（策略归业务侧自定） |
| --- | --- | --- |
| `x5c` 证书链 | 验证数组含中间与叶证书，自 `credcert` 起，用 Apple 证书机构验证有效性 | 链不可追溯到 App Attest 根 → 拒绝并要求重新证明；根证书缺失属运维故障，不得记为设备风险 |
| `RP ID` | 32 字节，为 App ID 的哈希（App ID = 10 位 Team ID + "." + bundle ID；macOS 用 code signing requirement 取代 bundle ID） | 不匹配 → 拒绝；注意无独立 Team ID 字段，勿按「团队 ID 字段」写校验 |
| `counter` | attestation 中必须为 0；后续断言须大于上次值，并存回新值 | 非单调递增 → 记录并按账号级处置，勿仅按设备级处置 |
| `aaguid` | 指示 development 或 production 环境（`appattestdevelop` / `appattest` + 7×`0x00`） | 环境串用（用沙箱收据请求生产指标）→ 直接拒绝，Apple 明文「You can’t use a receipt generated in one environment to request a new receipt in the other.」 |
| `validationCategory` | 须校验 CBOR 字典中的该值（0–10 档，逐档原文见第 12 章） | 可按业务需要只接受 App Store / TestFlight 档；但不得把「重签名必落某档」当既成事实写进规则 |
| receipt 本地校验 | 验签、追溯到 Apple 根、字段 2 为本 App 的 App ID、字段 12 创建时间 ≤5 分钟、字段 3 公钥与首次存库一致 | 5 分钟窗口超期属重放防护设计，处置应为重新取回执而非判风险 |
| nonce / challenge | 服务端生成并记住一次性随机值；校验嵌入 challenge 与先前下发值一致 | 不一致 → 拒绝；该项属流程完整性，不计入设备风险分 |

失败降级路径要与「无信号」区分开：**Apple 明确要求服务端在设备不支持该服务时改变自身行为、不得再把断言作为必要条件**（等级 1）。因此需要两条独立分支：`isSupported=false`（能力不可用，走降级放行 + 备用证据）与校验失败（能力可用但结论为否，走拒绝复核）。把两者混为同一分支，会让不支持的机型被系统性误判为高风险。

### 20.2.2 nonce 与计数器管理

推荐以「账号 × 设备 × 密钥」三元组为状态单元，而非以设备或账号单独为单元，依据是 Apple 一手的两条要求："Create a unique key for each user account on a device."、"Don’t reuse a key among multiple users on a device because this weakens security protections."，以及 "Be prepared to store multiple (key, receipt) pairs for each user. Store one pair for each device the user uses… keep development pairs separate from production pairs."（等级 1）。运维上还需注意：`keyId` 必须在客户端可持久取回（"there’s no way to use or retrieve the key without the identifier"），App Clip 与主 App 共用同一 key 且 "store the identifier in a shared container accessible from your full app."；重装、迁移、从备份恢复会使 key 失效，需支持同一用户多次重新证明并保留历史（等级 1，第 6 章）。

`DCError.serverUnavailable` 的处置须单独规定：Apple 原文要求**用同一 key 与同一 clientDataHash 稍后重试**，理由是 "Retrying with the same inputs helps to preserve the risk metric for a given device."（等级 1）。随意新建 key 会人为推高指标，等于自污染阈值基线。客户端错误码只有 5 个粗粒度 case（`featureUnsupported`、`invalidInput`、`invalidKey`、`serverUnavailable`、`unknownSystemFailure`），**不存在「检测到篡改/越狱」类错误码**，因此客户端错误只能作为重试依据，不得作为风险判定输入（等级 1，第 7 章）。

### 20.2.3 risk metric：只能当输入，不能当结论

落地规则三条（全部由等级 1 原文支撑）：第一，指标语义是「近 30 天该设备上 attested key 的近似数量」，合法增长来源已被 Apple 列举（重装、备份恢复、转让设备），故阈值必须以自身历史分布标定并保留调整记录；第二，只有服务端申请的 `RECEIPT` 类型才带字段 17，且刷新须等过字段 19 的 Not Before（否则得 `304 Not Modified`）并在字段 21 到期前使用，指标因此是**周期采样而非实时值**；第三，Apple 明文 "The App Attest service records device metrics that you can’t reset."——该句主语是指标不是 bit，**不得与 DeviceCheck bit 的重置责任混写**（第 5、8 章）。

### 20.2.4 保留独立证据链

把设备真实性纳入风控时，须保留与该信号**相互独立**的证据链（账号行为、交易与设备绑定关系、业务侧历史、渠道与安装来源等）。依据是等级 2 的条件式结论：客户端破解手法「此法仅绕过客户端校验，只在缺少服务端校验的应用中有效」，以及 Apple 一手点名的滥用形态「一台被攻陷设备为大量订阅者提供断言」。二者共同说明：单一信号一旦前提被破坏，该维度即无残余项可交叉验证。同理，**不可因无信号就假定安全**：未纳管、`isSupported=false`、未集成 App Attest 三类情形下，该项在评分模型中应记为「缺失」并显式降权，而不是记为 0 分风险。

**可立即自查项（服务端与风控，无需外部证据）**

- [ ] 校验项清单是否 7 项全部实现并逐项落库，能否对任意一次放行回答「依据哪几项」。
- [ ] `isSupported=false`、链验证失败、指标缺失三条分支是否在代码与策略文档中互不混淆。
- [ ] dev / prod 收据与 key 是否物理分份存储；是否存在跨环境复用。
- [ ] `DCError.serverUnavailable` 是否按「同 key 同 clientDataHash 重试」实现，而非直接新建 key。
- [ ] 阈值变更是否有带时间戳的变更记录，是否说明依据的指标窗口（30 天）与刷新节奏。
- [ ] 是否已按 ≤1000 万用户/日/App 渐进放量、按 <100 req/s 控制调用，并实现 429/503/304 退避（须标注这两个数字是 Apple 的 rule of thumb，Apple 称阈值可能动态浮动）。
- [ ] 时钟偏移监控：是否观测到 `400 Bad Timestamp`、`401 Authorization Token Expired`、`304 Not Modified` 的异常聚集（Apple 未公开允许偏移量）。
- [ ] 是否已从 Apple Private PKI Repository 取得 App Attest 根与 Apple Enterprise Attestation Root CA，并纳入到期与替换流程。

**需要外部证据才能确认项（服务端不得自行假定）**

- [ ] risk metric 的计算定义、精度与是否会被 Apple 修正——Apple 未公开，只能以自身观测分布近似。
- [ ] 两个接口的真实 QPS/日配额、服务可用性 SLA、延迟与故障公告——仅有建议值与错误码，无承诺数字。
- [ ] `403 Forbidden`（"The specified action isn’t allowed"）的具体触发条件——未公开。
- [ ] receipt 在 Apple 侧的存储与保留期、bit 值历史可查性、SE 内 key 数量上限——均未公开。
- [ ] App Attest 的实际绕过成本——无同行评审研究，任何数字都不可写入模型文档。
- [ ] 具体越狱工具/重签名方案对应哪一档 `validationCategory`——Apple 未给断言，须以自身样本观测累积。

## 20.3 App 侧：把越狱检测定位为启发式提示而非可信根

### 20.3.1 定位：提示，不是判定

App 侧唯一站得住的定位是**把端侧检测结果作为上报给服务端的启发式提示**，而不是本地放行条件。依据是三处等级 1 原文：「You can’t rely on your app’s logic to perform security checks on itself because a compromised app can falsify the results.」「A compromised version of your app could falsify the verification result, thus circumventing App Attest.」「App Attest can’t definitively pinpoint a device with a compromised operating system.」配套的效果证据是等级 2 的：具体越狱名称检索属强指标，而 `fork`、`_dyld_image_count` 类调用因用途不唯一而被研究者标为高不确定性（实测 95.7% 的 iOS 应用调用后者、74% 同时调用二者）。**因此端侧信号在策略文档里应显式标注「不可信根、仅作证据之一」**，这既是准确性要求，也是避免对用户作出无法兑现承诺的要求。

集成侧还有一组必须写进工程基线的 Apple 一手条件：`com.apple.developer.devicecheck.appattest-environment`（development / production；开发期省略该 entitlement 时默认使用沙箱服务；经 TestFlight、App Store 或 ADEP 分发后应用忽略所设值而使用 production）；App Clip 须给 App 与 App Clip 都加能力并把标识存入共享容器；**扩展类型口径存在 Apple 文档内部不一致**——`establishing-your-app-s-integrity` 称 "Action, extensible SSO, and watchOS extensions are supported"，而 `DCAppAttestService.isSupported` 页称 `generateKey` 在任意 app extension 中均失败、仅 watchOS 9+ 扩展支持。两处并列引用、按最保守口径设计（即假定除 watchOS 9+ 扩展外不可用），并在自家文档注明这是 Apple 文档内部口径冲突而非我方推断（第 7 章、附录 F）。

### 20.3.2 误报的用户代价与降级策略

误报的代价是**真实用户被拒**，而 iOS 侧误报率无任何可核实测量（等级 6 缺口）；可引的只有工程共识：MASVS 口径被研究者转述为「即使以牺牲漏报为代价，也要最小化误报」（等级 2）。因此 App 侧的降级策略应遵循四条：

1. **提示不得单独触发不可逆动作。** 端侧结果为「疑似被改动」时，只应提高验证强度或转人工，不应直接封号——因为该结果本身可被伪造（等级 1），且误报率未知。
2. **可用性检查前置。** Apple 要求访问服务前先做兼容性检查，未通过则 "gracefully bypass the service"（等级 1）。此路径必须实现且默认开启，否则老设备与不支持机型直接失去功能。
3. **重装/迁移/恢复的重新证明流要平滑。** key 不跨越重装、设备迁移与从备份恢复（等级 1），端侧须能识别「这是同一用户的正常重新证明」而不是「新用户新设备」。
4. **放量与限频在端侧就要设防。** 依据 Apple 的渐进放量建议与 <100 req/s 的建议调用频率，端侧应实现退避与合并请求，并把「Apple 称阈值可能动态浮动」写进容错设计（等级 1，附限定）。

### 20.3.3 政策条款引用纪律

App 侧文档若需要引用可引的政策条款，只允许使用已核到原文的条目号：**2.5.1**（仅可用公开 API，须运行于当前发布系统）、**5.1.2(iii)**（不得秘密据采集数据构建用户画像、不得识别匿名用户或据 Apple 所提供 API 的数据重建画像）、**5.1.2(i)**（须通过 App Tracking Transparency API 取得显式许可方可追踪），并附「以抓取当日版本为准，条款可能修订」（等级 1，第 11 章）。另可引 2.3.1(a)：把推广内容或服务的误导（明文举例 "promoting content or services that it does not actually offer (such as iOS-based virus and malware scanners)"）列为下架与终止账号的理由——这是「Apple 不承认第三方安全/扫描类 App 能力宣称」的政策依据。

**两条禁止的写法**：不得写「Apple 禁止设备指纹」——App Store Review Guidelines 全文（本次抓取 240,818 字节页面）`fingerprint` 出现 **0 次**，亦无 `IDFA`/`IDFV`/`vendor`/`serial number` 字样；DeviceCheck/App Attest 全部文档页亦无禁止指纹或追踪的限制句，最接近的只有 "while maintaining user privacy"。不得写「Apple 明令禁止 App 读取序列号」——正确口径是「公开 SDK 未暴露该能力」（由 `UIDevice` 属性清单支撑的负面结论），序列号获取只在 MDM（`SerialNumber`，需 Device Information access right）与用户自助查询页出现。同理，私有 API 相关表述只可引 2.5.1 原文，不可自行扩展。

**可立即自查项（App 侧）**

- [ ] 端侧检测结果是否全部只上报、不参与本地放行决策；代码与文档是否一致。
- [ ] `isSupported`（只读属性，非方法调用）是否前置检查，降级路径是否已实现并测试。
- [ ] entitlement 环境值在分发后行为是否被正确理解（分发后忽略所设值、使用 production）；沙箱与生产物料是否严格分份。
- [ ] App Clip / 扩展支持面是否按最保守口径实现，并注明 Apple 文档两处不一致。
- [ ] key 失效三类事件（重装、迁移、备份恢复）的重新证明流是否可自动完成且不打扰用户。
- [ ] 策略文档中的政策引用是否只使用已核到原文的条目号，并附版本限定。
- [ ] 是否出现「禁止设备指纹」「不可重置」「Apple 已阻断模拟器」等本篇已推翻的表述。

**需要外部证据才能确认项（App 侧）**

- [ ] 自家端侧检测的真实误报率——需自行以受控设备样本测量；iOS 侧无公开可核实基准可参照。
- [ ] 模拟器与虚拟化环境上 `isSupported` 的取值与机制行为——Apple 未公开，须自行以合法渠道测量后方可写入结论。
- [ ] 机型支持矩阵（哪些设备不提供 App Attest / DeviceCheck）——Apple 未给枚举清单，任何版本只能来自自家实测。
- [ ] App Attest 私钥曲线与算法（是否为 P-256）——Apple 文档只给 77 字节 COSE 公钥，未公开曲线名；不得按「官方规定 P-256」实现。
- [ ] 重签名副本实际落入哪一 `validationCategory` 档——需自家样本观测累积，Apple 未给结论。

## 20.4 企业与 MDM 管理员：纳管、证明链校验与字段能力边界

### 20.4.1 纳管与 MDA 证明链校验

Managed Device Attestation 是企业侧唯一具备密码学强度的设备属性断言通道，其运维要点全部来自等级 1 原文：把 `DevicePropertiesAttestation` 作为 `DeviceInformation` 的查询键请求；返回值是 **DER 形式证书数组构成的链，第一项为叶证书**，链根为 Apple Enterprise Attestation Root CA（**响应不含根，需从 Apple Private PKI Repository 自行取得**）；叶证书内含描述设备的自定义 OID，**设备 OS 版本与注册类型决定哪些 OID 出现**；Apple 证明服务器无法确认某属性时会「生成空值、整体省略该 OID，或拒绝签发证明证书」；freshness code（OID `.10.2`）对应请求时下发的 `DeviceAttestationNonce`，缓存可能导致不匹配。

Apple 给管理员的处置口径是二选一："There’s no scenario during normal operations in which this validation step fails. You can choose to ignore the attestation entirely, or flag it as an incident that needs investigation."（等级 1）因此策略文档必须把「链验证失败」显式定义为**事件（incident）**而非普通解析异常——这是 Apple 自己提供的选项之一，也是最容易被漏配的一项。若企业同时使用证书自动化，可另走 ACME attestation 工作流（`device-attest-01` challenge），Apple 称其"provides a chain of trust from the CSR back to the hardware-bound private key"；但须注意 MDM 侧完全没有 dev/prod 环境概念，新鲜度由 nonce/freshness 表达，**不得与 App Attest 的 `aaguid` 环境标记互换引用**。

硬件代际须写入纳管基线：支持为 A11 Bionic 及以后、全部 M 系列、Apple TV A12 及以后、Watch S4 及以后；不支持的是 A10x Fusion 及更早的 Intel Mac 与 iPhone/iPad、S3 及更早的 Apple Watch、Vision Pro（文档记 "none"）。不支持的设备会**静默忽略** `DevicePropertiesAttestation` 与 `DeviceAttestationNonce` 两个键——监控上表现为「字段永久缺失」，与「校验失败」不同，须分别告警。

### 20.4.2 明确 MDM 字段里没有越狱状态

这是企业侧最容易出错的一条。已核实的清单是：`DeviceInformation` 响应 `QueryResponses` 的 95 个键名中**不存在**任何 jailbreak / 越狱 / root / Cydia / integrity / trust-level / risk 字段；整个 Device Management 抓取范围内无 `jailbreak` 字样；`SecurityInfo` 的完整性类字段（`SystemIntegrityProtectionEnabled`、`AuthenticatedRootVolumeEnabled`、`SecureBoot`、FileVault 相关、bootstrap token 相关、`IsRecoveryLockEnabled`）**几乎全为 macOS 专属**，iOS 侧只有通行码合规类与 `HardwareEncryptionCaps` 加密能力位。

因此两条纪律：其一，**不得据 MDM 字段「未报告越狱」得出「设备未越狱」**——该字段根本不存在，其缺席是结构性缺失而非阴性结果。其二，最接近「被篡改/非商店来源 App」的可用信号是 `InstalledApplicationList` 的 `AdHocCodeSigned`、`AppStoreVendable`、`IsValidated`、`DistributorIdentifier`、`Source`，但 Apple 把它们定义为**来源/签名类别标签**，不是「该 App 被改动」的判定，且仅对受管设备可见（等级 1）。企业安全告警若建立在这几个字段上，措辞须是「非商店来源/临时签名类别应用」，不得写成「检测到被篡改应用」。

### 20.4.3 iOS 与 macOS 字段差异对策略文档的影响

策略文档若统一表述为「校验设备完整性状态（SIP、Secure Boot、第三方内核扩展许可）」，在 macOS 上可执行、在 iOS 上不可执行，因为 Apple 只把 OID `.13.1`（SIP status 0/1）、`.13.2`（Secure boot status：Full/Reduced/Permissive Security）、`.13.3`（Third party kernel extensions allowed 0/1）写作 **macOS 14.2 引入**。iOS 上可 attestate 的是：序列号（`.9.1`）、UDID（`.9.2`）、sepOS 版本（`.9.4`）、freshness code（`.10.2`），以及 iOS 17.2 / macOS 14.2 起引入的 OS 版本、`SoftwareUpdateDeviceID` 与 LLB 版本（Low Level Bootloader 固件版本，文档指向 Apple 平台安全指南的启动流程章节）。

治理上的处理办法是**把策略拆成两栏基线**：macOS 栏可引用 SIP/Secure Boot/kext 三项断言；iOS 栏只能引用硬件与固件标识类断言 + 注册类型 + 受管状态，其余「宿主未被改动」的判断必须显式标注为**本层不提供**，并说明由哪一层（服务端业务证据或端侧启发式提示）补充。还须记录一处已知不一致：Mac 上 OID `.9.2` 的值等于 `ProvisioningUDID`，Apple 明文 "doesn’t match the UDID used elsewhere in the MDM protocol"——任何把该 OID 与 MDM 他处 UDID 直接比对做资产对账的流程，在 Mac 上会产生假阳性差异。

另有一组与设备状态相关但属于「状态可见性」而非「完整性判定」的字段（`IsActivationLockEnabled`、`IsMDMLostModeEnabled`、`IsSupervised`、`IsAppleSilicon`、`PasscodePresent`/`PasscodeCompliant` 等），其功能讲解归《iOS 失窃设备保护》，本篇只作为「MDM 可见字段清单」的证据引用（第 10 章、附录 F）。

**可立即自查项（企业与 MDM 管理员）**

- [ ] 是否已对全部受管设备请求 `DevicePropertiesAttestation`，并区分「键缺失」「OID 空值」「链不可追溯」三类不同告警。
- [ ] 叶证书 → 中间 → Apple Enterprise Attestation Root CA 的校验是否实现；根证书来源、到期与替换是否纳入运维日历。
- [ ] 「链验证失败」是否已在流程上定义为安全事件而非解析异常（Apple 明文正常操作无失败场景）。
- [ ] 硬件代际清单是否与纳管基线一致；A10x 及更早设备是否被标注为「无 attestation 能力」而非「无风险」。
- [ ] 策略文档是否已按 iOS/macOS 双栏拆分，且 iOS 栏不含任何 SIP/Secure Boot/kext 断言。
- [ ] 是否避免了「MDM 未报越狱 = 未越狱」的推定；`InstalledApplicationList` 告警措辞是否为「来源/签名类别」而非「被篡改」。
- [ ] Mac 设备对账是否已考虑 `ProvisioningUDID` 与 MDM 他处 UDID 不一致。
- [ ] `DeviceInformation` 的 access right（Device Information / Network Information）与 user channel / device channel 适用范围是否按文档配置；是否知晓 `CarrierSettingsVersion` 类字段 iOS 16 弃用、iOS 26 移除。

**需要外部证据才能确认项**

- [ ] iOS 上「宿主被改动」的判定：本层不存在该字段，任何结论需转向其他层取证。
- [ ] Apple 证明服务器为何对某属性生成空值/省略 OID/拒签的具体条件——未公开，只能按「无法可靠确定该属性」的官方语义处置。
- [ ] 各 OID 在不同 OS 版本与注册类型下的完整出现矩阵——文档零散，需以自家设备样本累积。
- [ ] 未受管（BYOD/个人）设备的一切完整性输入——Apple 明确以 MDM 注册 + access right 为前提，无替代通道。
- [ ] Apple 企业证书吊销数量与年度统计——Apple 不发布该口径数据（第 15 章），企业侧不可据此设定「吊销风险基线」。

## 20.5 研究与评估者：合法路径与负面结果的写法纪律

### 20.5.1 合法路径

评估者可用且应当使用的路径有三类。第一类是**官方文档与 API 层面的核验**：Apple 文档接口可取到渲染前完整文本，MDM Protocol Reference、App Store Review Guidelines、Apple 平台安全指南、开发者计划页与部署指南均可复核；本篇大量结论正是靠「全文 grep 某词零命中」这类**负面结果**建立的。第二类是**公开同行评审研究**：Kellner（EuroS&P 2019）、HALY（EuroS&P 2025，工具开源）、USENIX Security '26 与 '24、IMC/WiSec 2026 等，其中工具侧限制须如实记录（iOS 动态分析依赖闭源付费虚拟化平台、大样本分析依赖越狱覆盖范围）。第三类是**自家受控样本**：在自己拥有的设备与账号上测量 `isSupported` 取值、指标分布、`validationCategory` 分布——这是唯一能填补本篇多项「未公开」缺口的合法途径。

涉及对手能力的部分，评估者应遵循本篇已确立的记录边界：**只记能力存在性、失效结论与证据强度，不收录实现**。两份简报的自查条款均已声明全文未收录可复现步骤、参数、脚本或点位清单，本研究纪律应在评估报告中同样声明。

### 20.5.2 把负面结果当结论写

负面结果是本篇最有价值的产出类型之一，应显式命名而不是省略。可直接作为结论写出的负面结果包括：Apple 平台安全指南（August 2026）**通篇无 DeviceCheck / App Attest 章节**（全文 grep 零命中，因此「平台安全指南论述了 App Attest」是错误出处）；Guidelines 无 `fingerprint` 一词；DeviceCheck/App Attest 文档无 `bot`/`automation`/`emulator`/`device farm`/`jailbreak`/`simulator` 字样；MDM 95 键无完整性字段；`deprecated` 字段在 DeviceCheck 全部页面零出现（即两项能力均未被标记弃用）；App Attest 弱点研究零同行评审；iOS 侧设备农场与模拟器滥用测量零取得。

写法上须区分三种「没有」：**Apple 明文不提供**（可写成责任分配结论）、**Apple 未公开说明**（必须写「未公开」，不得写「不存在」或「不适用」）、**本次检索未取得**（必须写「本次核查未获得可核实条目」，并注明检索范围与日期）。本篇 E 节纪律「任何『未公开』结论都必须写成 Apple 未公开 X，不得写成不存在 X」应作为评估报告的通用规则。

### 20.5.3 引用纪律

三项最低要求。第一，**引原文并附出处层级**：英文原文保留，中文标注为译述；每条结论标等级（1–6）与取文通道；给 URL 或字段名/条目号；Apple 文档需注明版本（「截至 2026 年 8 月版」），部署指南类需注明 Published Date（如 in-house 分发指南为 September 17, 2026）。第二，**数字必带口径三件套**：样本池、判据（静态字符串 vs 运行时执行）、年份；跨研究比较逐条标注，不得合并（例如 Kellner 59% 与 HALY 静态 96.7%/动态 24.9% 必须分别标注口径）。第三，**厂商材料的可信度上限须随引文重复出现**：等级 3 对「产品有效性」的自评一律降格为营销，「该厂商声称在其自设测试口径下观察到 X」是唯一允许的写法；市场规模与灰产规模若来自营销白皮书一律标「不可用于事实陈述」。涉及未取得全文的论文（ESORICS 2025 workshop、iOSModZoo WiSec 2026、Iranian iOS App Stores IMC 2026）时，**只可引其研究问题与存在性，不得引用任何百分比或样本量**。

**可立即自查项（研究与评估者）**

- [ ] 每条结论是否带等级标注与出处（URL 或字段名/条目号），文档版本是否注明。
- [ ] 负面结果是否被显式命名，并区分「不提供 / 未公开 / 未检索到」三种成因。
- [ ] 每个数字是否带样本池、判据、年份三件套；是否出现跨研究合并或趋势线。
- [ ] 等级 3 材料是否重复标注了可信度上限；百分比是否被误用为事实。
- [ ] 未取得全文的论文是否只被引用存在性与研究方向，未引用任何数字。
- [ ] 是否避免了实现层描述（检测点位、绕过组件名、参数、脚本）。
- [ ] 「iOS 证据密度低于 Android」是否与「不得推定风险更低」的口径声明同段出现。

**需要外部证据才能确认项**

- [ ] 误报率的任何测量——iOS 侧未取得，需自行设计受控样本或等待同行评审研究。
- [ ] 未取得全文论文的具体数字（样本量、检出率、绕过成功率）。
- [ ] App Attest/DeviceCheck 的绕过成本——无同行评审来源，不得凭厂商博客定级。
- [ ] Apple 服务侧内部判定逻辑与阈值——未公开，只能以黑盒观测近似并标注为观测。

## 20.6 跨层依赖、执行顺序与治理基线

四层动作之间有明确的方向性依赖，乱序执行会让加固看起来做了、实际没生效：

| 顺序 | 动作 | 为什么必须在前 | 依赖的上一章结论 |
| --- | --- | --- | --- |
| 1 | 清点覆盖：哪些业务/机型/注册状态实际能取得哪一层信号 | 未集成与未纳管是**覆盖率问题**，先算覆盖率再谈阈值，否则阈值无意义 | 第 14 章采用率（静态 4.1%/动态 1.1%）、第 10 章未受管设备无字段 |
| 2 | 补齐服务端校验项并逐项落库 | 校验不全时，任何后续判定都无可追溯依据 | 第 6、8 章校验清单 |
| 3 | 定义三分支：能力不可用 / 校验失败 / 校验通过 | 把 `isSupported=false` 与校验失败混用会造成系统性误判 | 第 7 章「Apple 不提供替代判定」、第 3 章责任分配 |
| 4 | 标定阈值并记录依据与变更 | Apple 明文要求自行 tune，且指标会被正当行为推高 | 第 8 章 risk metric 语义 |
| 5 | 把端侧结果改为纯提示并接入独立证据链 | 端侧结论可被伪造（等级 1），只能作辅助输入 | 第 12、14 章 |
| 6 | 治理固化：文档双栏、引用纪律、缺口登记 | 防止口径在版本迭代中漂移或被下游写手写错 | 第 17 章口径不对称、附录 D/E |

治理基线中三项必须固化到文档：**(a) 双栏策略**（iOS 与 macOS 的可用性断言分栏，见 20.4.3）；**(b) 术语口径表**（attestation=证明、assertion=断言，首次出现附英文；`isSupported` 写作只读属性；bit 统一为「每设备两个二进制位、语义由开发者自定义、存储于 Apple 服务端、可由开发者服务端改写」；`seKey` 若使用须标第三方口径；Managed Device Attestation 与 App Attest 分立不得互引证据；「设备真实性」需加「以 App/密钥真实性为代理指标」限定——全部见附录 A 与 F）；**(c) 缺口登记册**（把附录 D 条目接入内部风险登记，标注「待外部证据」而非「已闭合」，并规定若日后取得官方原文或新研究，应以其为准修订而非沿用本篇封顶判定）。

本章所有建议的动作类型限于配置、验证流程、监控与治理四项，不涉及任何绕过、伪造、检测规避或攻击准备；这一限制同时是本章可用的边界——凡需要「让某信号不出现」才能达成合规的场景，均不在本篇提供范围内，应转交相应制度流程处理。

## 本章口径边界

本章只把第 5–19 章已核实的机制语义、能力边界与共性前提转译为角色动作，**未新增任何事实，也未给出任何 Apple 未提供的判定强度承诺**。三处必须在阅读本章时同时保有的限定：其一，本章提到的所有 Apple 数字（≤1000 万用户/日/App、<100 req/s、challenge ≥16 字节、receipt 创建时间 ≤5 分钟、指标窗口 30 天、OCSP 缓存 3–7 天、ADEP 100 名员工与 299 USD 年费、描述文件 12 个月、发行证书 3 年）中，前两个是 Apple 自称的 rule of thumb 且称阈值可能动态浮动，其余是明文要求，两类均**不构成 SLA**；其二，「可立即自查项」之所以能自查，是因为其依据全部来自公开一手文档与自家日志，而「需要外部证据才能确认项」在本篇口径下一律为未公开或未检索到，不得由本章的清单反推为已知；其三，本章不评估任何具体厂商产品的检出率——iOS 侧重签名/改造包检出率与误报率无公开可核实的第三方评测（等级 6），商业产品中该检测项普遍存在这一事实本身不构成有效性证据。

# 结论

## 21.1 拆分之后得到了什么

本篇的起点是一个无法直接回答的问题：「iOS 能不能防住灰产」。第 1–17 章的处理方式是先把「设备真实性」拆成四个互不等价的概念，再把它们分配到四层防线上，最后逐格核实证据。拆分之后得到的是四类可核对的结论，而不是一个答案。

**概念层的收获：四个「真实性」不能互换。** 其一，**密钥真实性**——App Attest 证明的是「某个 key 由真实 Apple 设备上、指定签名类别的 App 实例生成」，私钥驻留 SE 且 "no process can ever directly read or modify it"（等级 1）。其二，**宿主真实性**——Apple 明文不提供该判定，"App Attest can’t definitively pinpoint a device with a compromised operating system"（等级 1），文档 `jailbreak` 零命中。其三，**身份真实性**——分两条方向相反的路：纳管场景由 MDA 的序列号/UDID/sepOS OID 提供密码学断言（等级 1），未纳管场景则由 IDFA/IDFV 的官方失效条件说明「跨 App 稳定设备 ID 不存在」（等级 1）。其四，**运行期真实性**——平台安全指南写明运行期逐页校验可执行内存签名，对象是「自安装或上次更新后是否被改动」（等级 1），这与「是否还是审核通过的那份代码」不是同一件事。四个概念在矩阵里分别落在不同行，任一格的结论不能沿概念借用。

**防线层的收获：四层各自的天花板由 Apple 的自述划定，而非由攻击案例划定。** 端侧自检的天花板是「可被伪造」（等级 1 明文，故 R4/C1 记为不在该层管辖范围）；平台侧的天花板是「只证明密钥与实例、不证明宿主」；服务端的天花板是「可校验但无阈值可引」；生态侧的天花板是「有条款、有准入、有吊销机制，但无判定服务、无量化公告」。第 18 章的计数（6 / 4 / 5 / 1）是这四条天花板的算术投影。

**责任层的收获：判定责任是显式分配的。** Apple 提供机制、信号与一个近似计数指标，同时明文把政策、阈值与处置归给开发者与管理员（等级 1，三处原文），并允许管理员「完全忽略」证明或「按事件上报」（等级 1）。这意味着「Apple 没防住」这一类表述在本篇里几乎都要重写为「该判定的责任层在别处，且该层是否履行可以从自身日志自查」——第 19 章汇总表里六条前提中五条为「可自查」。

**方法层的收获：负面结果是本篇的主要产出。** 本篇最可复用的结论，多数形如「X 在 Apple 文档中零命中」「Y 类研究本次未取得」：平台安全指南无 DeviceCheck/App Attest 章节；Guidelines 无 `fingerprint`；DeviceCheck/App Attest 文档无 `bot`/`emulator`/`device farm`/`jailbreak`/`simulator`；MDM 95 键无完整性字段；`deprecated` 零出现（两项能力均未标记弃用）；App Attest 弱点研究零同行评审；iOS 设备农场与模拟器滥用测量零取得；检测误报率测量零取得；企业证书吊销数量公告零取得。这些负面结论把「Apple 没打算承诺的范围」勾勒了出来，其信息量高于任何一条正面陈述。

## 21.2 Apple 公开口径的系统性边界

把第 5–17 章的等级 1 材料横向看，Apple 的公开口径呈现一个稳定结构：**提供机制与信号，提供政策条款与准入规则，但不提供判定与阈值。** 这一结构在四组事实上重复出现：

| 结构维度 | Apple 提供的 | Apple 不提供的（等级 1 可核） |
| --- | --- | --- |
| 信号 | DeviceCheck 两枚 per-device bit；App Attest 证明与断言；MDA 属性证书链；receipt 字段 17 的近似计数 | 布尔形的「是否越狱」「是否被篡改」判定；任何风险评分服务 |
| 判定 | 校验清单（链、`RP ID`、`counter`、`validationCategory`、receipt 五项本地校验） | 阈值（明文要求开发者自行 "tune your risk assessment logic"）；失败处置建议（明文 "it’s your responsibility to decide"） |
| 政策 | 2.5.1、2.3.1(a)、5.1.1、5.1.2(i)、5.1.2(iii)；ADEP 用途边界与准入条件；OCSP 首启动校验与吊销连坐机制 | `fingerprint` 相关条款（该词零命中）；吊销数量与年度统计公告 |
| 承诺 | 集成建议数字（并自称 rule of thumb、可动态浮动） | 速率上限、SLA、延迟、机型支持矩阵、模拟器行为、指标计算定义、receipt 保留期 |

由此可得一条可操作的判读规则：**文档零命中的词，就是 Apple 没打算承诺的范围。** 这不是「该风险不存在」，而是「该范围不在官方承诺之内，责任落在集成方」。`jailbreak`、`bot`、`emulator`、`device farm`、`fingerprint`、`simulator` 六个词的零命中，各自对应一个必须改由业务侧承担的判定；`seKey` 的零命中则对应一个必须标注为第三方口径的术语。同一规则的另一面是：Apple 明文写出的两句越狱侧上限句（"can’t definitively pinpoint"、"might bypass restrictions"）与一句自评（"an unlikely source of widespread fraud"），**都属厂商文档表述**——前两句只支撑能力存在性，第三句不得被用来降低风险等级，因为它是自评而非测量。

一处需要特别标注的口径边界：Apple 文档中存在**内部不一致**，本篇采取并列而非择一的写法。最重要的两处是扩展类型支持（`establishing-your-app-s-integrity` 承认 Action 与 extensible SSO 扩展可用，`DCAppAttestService.isSupported` 页称仅 watchOS 9+ 扩展可用且 `generateKey` 在任意扩展中失败），以及「不可重置」这句话的主语（指标 "device metrics that you can’t reset" 与 bit 的开发者重置责任是两个不同对象）。把任一处当作既定事实引用，都会把 Apple 的口径瑕疵转嫁为读者的错误。

## 21.3 本篇的正确使用方式

本篇不是「iOS 能否防住灰产」的是/否答案，也**不应被引用为该问题的答案**。它的正确用法是把结论按「层 × 前提 × 证据强度」三元组取出，三者缺一即构成误用。

- **按层取用**：先确定自己在问哪一层。问「能不能判定越狱」→ 本篇的答案是「Apple 不提供该判定，责任层在别处」（等级 1 否定式），而不是「能」或「不能」。
- **带前提取用**：第 18 章每一格都有适用前提。C2 的结论离开「集成 + `isSupported` 为真 + 链可校验 + 纳管/代际达标」即失效；C4 的结论离开「受管 + access right」即归零。引用一格结论时，前提必须同段出现。
- **按证据强度取用**：等级 1 与等级 2 不可互相补位。厂商（等级 3）有效性自评不得升格为「业界研究表明」；媒体与执法材料（等级 4）只可支撑事件存在性；社区形态叙述（等级 5）只可作一次性历史注解；等级 6 一律留在附录 D，不进入正文。
- **禁止的四种用法**：把「公开证据不足」读作「安全」（第 18.6 节）；把本篇格子值合成一个总分或排序（第 18.7 节，无严肃分级来源，禁止自制排序表冒充事实）；把下界观测当总量、把静态检出率当覆盖率、把 Android 结论换算成 iOS 结论；把「Apple 未公开 X」写成「不存在 X」。
- **推荐的落点**：作为风控与合规文档的**证据上限引用表**使用——每条规则后面标注它引用的机制、版本（截至 2026 年 8 月版 Apple 文档）、等级与前提；作为评估报告的**写法规范**使用（附录 B/C/E 可分别用于口径核对、研究对照与误解校正）；作为策略文档的**双栏基线来源**使用（iOS 与 macOS 的可用性断言分栏）。

## 21.4 未解问题清单

下列条目是本次核查未取得可核实依据、且**直接影响本篇封顶判定**的问题。它们按「若取得答案会改变哪一节结论」排列，完整去重清单见附录 D。

| # | 未解问题 | 为何关键 | 影响正文哪一节 |
| --- | --- | --- | --- |
| 1 | App Attest 私钥的曲线与密钥长度（`crv`/`P-256`/`alg` 未出现于任何已取文档页，只有 77 字节 COSE 公钥与 X9.62 非压缩点格式） | 决定互操作实现与验证库的算法假设 | 第 6 章、20.3 |
| 2 | App Attest / DeviceCheck 支持的机型枚举与 `isSupported` 的机型白名单 | 决定「无信号」是能力缺失还是配置问题 | 第 7 章、18.4、20.2 |
| 3 | iOS 模拟器与虚拟化环境下 `isSupported` 的取值及两机制的行为 | R3 行整行的判定基础 | 18.4、20.2、20.3 |
| 4 | `isSupported` 在扩展类型上的两处口径矛盾（Action/extensible SSO 是否可用）以何者为准 | 直接决定集成范围能否覆盖登录与快捷指令场景 | 第 7 章、20.3 |
| 5 | risk metric 的计算定义、精度、是否会被 Apple 修正 | 服务端阈值的可解释性 | 第 8 章、20.2 |
| 6 | Apple 企业证书吊销数量/年度统计与吊销公告口径 | 生态侧控制强度不可见，只能靠研究观测 | 第 15、16 章、18.4 |
| 7 | iOS 越狱检测误报率的任何测量 | 降级策略的用户代价无法量化 | 第 14 章、20.3 |
| 8 | App Attest / DeviceCheck 的独立弱点或绕过研究（本次零篇同行评审） | 「绕过成本」在本篇不可知，任何断言均属未公开 | 第 12、14、17 章、18.3 |
| 9 | 以 iOS 为对象的设备农场/群控大规模测量 | R3 行的生态侧控制判定无依据 | 第 16、17 章、18.4 |
| 10 | 检测信号「易伪造程度」的量化排序研究（仅有定性判据） | 本篇无法给出强度排序的根本原因 | 18.7、20.1 |
| 11 | 重签名/改造包检出率与误报率的第三方独立评测 | 商业 MTD/RASP 能力不可验证 | 第 14、17 章、20.5 |
| 12 | 「A12 及以上长期无公开可用越狱」的时间边界与一手/学术依据；checkm8 类 bootrom 影响的芯片代际边界 | 决定 R2 行的现实威胁面描述能否具体化 | 第 13 章、18.3 |
| 13 | ESORICS 2025 workshop、iOSModZoo（WiSec 2026）、Iranian iOS App Stores（IMC 2026）的全文数字 | rootless 时代检测有效性与第三方市场结构均只有题录级依据 | 第 13、14、16 章、附录 C |
| 14 | DeviceCheck 与 App Attest 接口的速率上限、SLA、延迟、`403 Forbidden` 触发条件、receipt 保留期、SE 内 key 数量上限、bit 历史可查性 | 运维容量规划与故障判定无承诺可依 | 第 9 章、20.2 |
| 15 | 信号可靠性分级的官方依据（Apple 未做任何分级；仅有 HALY 的定性判据与 USENIX '26 的条件式结论） | 本篇只能用条件式答案，不能给出排序 | 18.7、20.1 |

## 21.5 面向后续版本维护的收束陈述

本篇的结论本质上是**时点性的**，其可维护性依赖三条记录习惯。第一，版本锚点必须随结论一起保存：Apple 机制侧口径锚定「截至 2026 年 8 月版文档」，DeviceCheck iOS 11.0 起、App Attest iOS 14.0 起、`DCAppAttestService` watchOS 9.0 起、MDM attestation iOS 16 / macOS 14 起、部分 OID iOS 17.2 / macOS 14.2 起；ADEP 与吊销机制锚定部署指南 Published Date September 17, 2026 与平台安全指南 August 2026；研究侧锚定各论文自述的样本年份（Kellner 为 iOS 12.1/2018-10 口径，HALY 为 iOS 16.4.1/2023-03 且自陈越狱仅覆盖到 iOS 16）。任何「当前状态」表述都必须写明其依据年份。第二，负面结论须可重检：本篇所有「零命中」类结论都给出了检索对象与范围，后续版本若在新版文档中检出该词，应直接推翻本篇的封顶判定并记录变更。第三，冲突口径不得被单方面消解：附录 F 记录的两类冲突（术语口径、简报对 L2 定义）与 Apple 文档内部两处不一致，都是**并列保留**而非择一采纳；若日后 Apple 自行统一，应以统一后的原文替换并列表述。

最终一条收束判断：本篇把「设备真实性防线」从「有没有」的问题，改写成了「在哪一层、依哪个前提、以什么证据强度成立」的问题。对读者的实际意义不是某个安全结论，而是一份可执行的核对义务——第 19 章的六条前提里五条可以自查，第 20 章的清单里绝大多数项目不需要任何外部证据就能完成。**防线最常见的失效方式，不是被高明的对抗突破，而是前提从未被验证、缺口从未被登记、口径从未被记录。**

**本章口径边界**：本章全部陈述为第 1–20 章与两份核查简报的重述与汇总，未引入新事实，未给出任何 Apple 未承诺的判定强度；本章出现的每个数字均携带其口径与限定；清单与计数属汇总动作，不构成排序或评分。

# 附录

## 附录A 术语表

本表的价值在最后一列：每个术语**出自哪一层**。同一句话里混用不同层的术语（例如把社区的 `rootless` 与 Apple 的 "compromised operating system"、把 MDM 的 freshness 与 App Attest 的 `aaguid` 并列）是本主题最常见的口径错误。「层」取值六档：**Apple 一手文档**（平台安全指南、开发者文档叙述、支持页与公告）、**Apple 协议与 API**（接口名、字段名、entitlement、MDM 键名、Guidelines 条目号）、**同行评审研究**、**厂商报告**、**社区与第三方**、**本报告自拟**（仅为阅读便利而设，不构成官方分类）。

| 术语 | 中文释义 | 出自哪一层 | 依据等级 / 回指 |
| --- | --- | --- | --- |
| DeviceCheck | 由 App 侧框架接口与 Apple 服务端接口共同构成的设备状态管理能力，官方定位为 "managing device state and asserting app integrity" | Apple 一手文档 | 1 / 第 5 章 |
| `DCDevice` | DeviceCheck 的 App 侧入口类，单例为 `DCDevice.current`（ObjC `currentDevice`） | Apple 协议与 API | 1 / 第 5 章 |
| bit0 / bit1 | 每设备两个二进制位的服务端 JSON 字段名；语义由开发者自定义、存储于 Apple 服务端、可由开发者服务端改写。官方措辞为 "two binary digits of data per device" / "two per-device binary digits (bits)" / "persistent bits"，响应说明写作 "the first bit"/"the second bit"；第三方常误写为 `bit1/bit2`、`deviceCheckBit1/2`，与官方字段名不符 | Apple 协议与 API（误写属社区与第三方） | 1 / 第 5 章、附录 F |
| `generateToken` | 取得 "An ephemeral token that identifies the current device"；官方要求按单次使用对待，重复使用应重新生成 | Apple 协议与 API | 1 / 第 5 章 |
| App Attest | 让 App 证明其实例合法性、使服务端更放心授予敏感资源访问的能力；住在 DeviceCheck 框架内，非独立框架 | Apple 一手文档 | 1 / 第 6 章 |
| `DCAppAttestService` | App Attest 的服务入口类，共享实例为 `shared`（ObjC `+sharedService`） | Apple 协议与 API | 1 / 第 6 章 |
| `generateKey` | 在设备上为特定用户账号创建安全未证明密钥对，返回 `keyId`；私钥存入 Secure Enclave | Apple 协议与 API | 1 / 第 6 章 |
| `attestKey(_:clientDataHash:)` | "Asks Apple to attest to the validity of a generated cryptographic key."，返回需送服务端处理的 `attestationObject` | Apple 协议与 API | 1 / 第 6 章 |
| `generateAssertion(_:clientDataHash:)` | 生成证明某一次请求合法性的数据块；同一 key 的断言次数无限制 | Apple 协议与 API | 1 / 第 6 章 |
| attestation（证明） | 对密钥来源与有效性的陈述；本报告固定译法「证明」，首次出现附英文 | Apple 一手文档（译法为本报告自拟） | 1 / 第 6 章、附录 F |
| assertion（断言） | 对某一次使用合法性的证明；本报告固定译法「断言」。Apple 文档不使用 `claim`（该词多来自 WebAuthn） | Apple 一手文档（译法为本报告自拟） | 1 / 第 6 章、附录 F |
| risk metric | receipt 字段 17，近 30 天该设备上 attested key 的**近似**数量；同物另有 `fraud metric`、`fraud assessment metric` 两名 | Apple 协议与 API | 1 / 第 8 章、附录 F |
| receipt | 以整段 Base64 为 body 向 Apple 换取的 PKCS #7 容器（签名 + 证书链 + ASN.1 编码负载），含字段 2/3/4/5/6/12/17/19/21 | Apple 协议与 API | 1 / 第 8 章 |
| `validationCategory` / `apple_bundle_version_01` | 前者为证明的 CBOR 字典中报告的签名身份类别，取值 0–10 档逐档有官方描述（扩展键名 `apple_validation_category_01`，UInt32）；后者为证明内附带的分发 App 版本号（String），服务端可校验 | Apple 协议与 API | 1 / 第 12 章 |
| nonce / `clientDataHash` | nonce 为服务端每次生成并记住的一次性随机值；App 侧 hash 后作为 `clientDataHash` 传入，官方定义为 "A SHA256 hash of a unique, single-use data block…"，要求至少 16 字节 | Apple 一手文档 / Apple 协议与 API | 1 / 第 6、9 章 |
| `counter` | 4 字节，报告该 attested key 被用于签名断言的次数；证明中必须为 0，断言校验要求单调递增 | Apple 协议与 API | 1 / 第 6 章 |
| `aaguid` | 16 字节 App Attest 常量，指示密钥属 development 或 production 环境（`appattestdevelop` / `appattest` + 7×`0x00`） | Apple 协议与 API | 1 / 第 6 章 |
| `credentialId` | 32 字节，公钥部分的哈希；长度字段 `credentialId length` 为 2 字节 | Apple 协议与 API | 1 / 第 6 章 |
| `x5c` | 证明对象中的证书链数组，自 `credcert` 起含叶与中间证书，需用 Apple 证书机构验证 | Apple 协议与 API | 1 / 第 6 章 |
| COSE 公钥（`Encoded key`） | 证明中 77 字节的 COSE 格式公钥；**曲线名与算法未公开**，不得写成「官方规定 P-256」 | Apple 协议与 API | 1 / 第 6 章 |
| `RP ID` | 32 字节，App ID 的哈希；App ID = 10 位 Team ID + "." + `CFBundleIdentifier`（macOS 用 code signing requirement 取代 bundle ID）。**证明内无独立 Team ID 字段** | Apple 协议与 API | 1 / 第 6 章 |
| `rpIdHash` | WebAuthn 标准对同一字段的名称；第三方 App Attest 校验库多用此拼写 | 同行评审研究 / 社区与第三方 | 2–5 / 附录 F |
| `isSupported` | **只读属性**（非方法）：DeviceCheck 侧意为「设备支持 DeviceCheck API」，App Attest 侧意为「该设备提供 App Attest 服务」；ObjC 键名为 `supported` | Apple 协议与 API | 1 / 第 7 章 |
| `seKey` | Apple DeviceCheck/App Attest 文档零命中；社区用它指代 SE 私钥句柄，另有无关的 `sekey` SSH 项目与学术 SEKEY 方案 | 社区与第三方 | 5–6 / 附录 F |
| Secure Enclave（安全隔区） | 与主处理器隔离、用于保护敏感数据的协处理器；宿主内核被攻破时仍可保护内部数据 | Apple 一手文档 | 1 / 第 7 章 |
| entitlement | 应用能力声明；App Attest 相关为 `com.apple.developer.devicecheck.appattest-environment`，DeviceCheck bits 侧未见要求列出 | Apple 协议与 API | 1 / 第 6 章 |
| `appattest-environment` | 取值 development / production；开发期省略则默认沙箱；经 TestFlight、App Store 或 ADEP 分发后忽略所设值改用 production | Apple 协议与 API | 1 / 第 6 章 |
| `DCError` | 客户端错误域，仅 5 个粗粒度 case（`featureUnsupported`/`invalidInput`/`invalidKey`/`serverUnavailable`/`unknownSystemFailure`）；**无篡改/越狱类错误码** | Apple 协议与 API | 1 / 第 7 章 |
| MDA（Managed Device Attestation） | MDM/企业 PKI 侧的设备属性密码学断言机制，链根为 Apple Enterprise Attestation Root CA；与 App Attest 为**两个不同机制**，不得互引证据 | Apple 一手文档 | 1 / 第 9 章 |
| `DevicePropertiesAttestation` | `DeviceInformation` 的查询键，返回 DER 证书数组构成的链，叶证书含描述设备的自定义 OID | Apple 协议与 API | 1 / 第 9 章 |
| `DeviceAttestationNonce` / freshness code | 新鲜度机制：nonce 由请求方下发，证书内 OID `.10.2` 为其对应 freshness；缓存可能导致不匹配 | Apple 协议与 API | 1 / 第 9 章 |
| OID | 证书内的对象标识符。iOS 侧：`.9.1` 序列号、`.9.2` UDID、`.9.4` sepOS 版本、`.10.2` freshness，iOS 17.2+ 另有 OS 版本/LLB 等；macOS 14.2 独有 `.13.1` SIP、`.13.2` Secure Boot、`.13.3` 第三方 kext 许可 | Apple 协议与 API | 1 / 第 9、10 章 |
| sepOS version | 「证明生成时运行在 Secure Enclave 上的操作系统版本」，官方逐字定义 | Apple 协议与 API | 1 / 第 9 章 |
| LLB 版本 | Low Level Bootloader 固件版本，文档指向 Apple 平台安全指南的启动流程章节 | Apple 一手文档 | 1 / 第 9 章 |
| `device-attest-01` | ACME attestation 工作流使用的 challenge 类型，Apple 称其提供从 CSR 回溯到硬件绑定私钥的信任链 | Apple 协议与 API | 1 / 第 9 章 |
| UDID / `SerialNumber` | 设备唯一标识与序列号：MDM 侧 `UDID` 与 `ProvisioningUDID` 并存，Mac 上 OID `.9.2` 等于 `ProvisioningUDID`，Apple 明文它与 MDM 协议他处的 UDID 不一致；`SerialNumber` 需 Device Information access right，公开 SDK 未暴露序列号读取能力（不得写成「Apple 明令禁止」） | Apple 协议与 API | 1 / 第 9–11 章 |
| `SystemIntegrityProtectionEnabled` | SIP 状态字段，**仅 macOS 10.12+**；`SecurityInfo` 的完整性类字段亦几乎全为 macOS 专属 | Apple 协议与 API | 1 / 第 10 章 |
| `InstalledApplicationList` 标签组 | `AdHocCodeSigned`、`AppStoreVendable`、`IsValidated`、`DistributorIdentifier`、`Source` 等为**来源/签名类别标签**，非「该 App 被改动」的判定 | Apple 协议与 API | 1 / 第 10 章 |
| IDFA | 每设备唯一、仅用于广告的可逆标识；iOS/iPadOS 14.5+ 须先取得追踪授权；多情形返回全零 | Apple 一手文档 | 1 / 第 11 章 |
| IDFV | 对 App 供应商唯一的设备标识；删光同 vendor 应用后重装会变、Xcode 测试构建与 ad-hoc 安装会变、重启未解锁前为 `nil` | Apple 一手文档 | 1 / 第 11 章 |
| ATT（App Tracking Transparency） | 追踪授权机制；政策侧可引口径为 5.1.2(i) 的显式许可要求 | Apple 协议与 API | 1 / 第 11 章 |
| 私有 API | 政策条款 **2.5.1** 的规制对象："Apps may only use public APIs and must run on the currently shipping OS." | Apple 协议与 API | 1 / 第 11 章 |
| 2.5.1 / 5.1.2(iii) / 5.1.2(i) / 2.3.1(a) | 本篇仅使用已核到原文的条目号；5.1.2(iii) 是最接近「禁止设备指纹」的表述，但 Guidelines 全文 `fingerprint` 零命中 | Apple 协议与 API | 1 / 第 11 章 |
| ADEP（Apple Developer Enterprise Program） | 面向大型组织向内部分发专有内部应用的计划；准入含 100 名及以上员工、法人实体、D-U-N-S、299 USD/年、持续审查 | Apple 一手文档 | 1 / 第 15 章 |
| 企业证书（in-house 发行证书） | ADEP 分发所用证书；发行证书自签发起 3 年或随会员到期（先到者为准），**已签名应用不受到期影响**（与被吊销是两件事） | Apple 一手文档 | 1 / 第 15 章 |
| OCSP | 首次打开 in-house 应用时设备联系 Apple OCSP 服务器校验证书；被吊销则应用不启动；响应在设备缓存 **3–7 天** | Apple 一手文档 | 1 / 第 15 章 |
| 吊销连坐 | "Revoking a distribution certificate invalidates all of the apps you've signed with it."，故 Apple 称吊销应为最后手段 | Apple 一手文档 | 1 / 第 15 章 |
| 超级签名 / 证书代签 | 中国检察系统公开报道所使用的业态称谓，被同行评审研究引为该现象来源之一；涉案金额与人数字属个案司法统计 | 执法/监管公开材料（等级 4）+ 同行评审研究 | 2、4 / 第 16 章 |
| tethered / semi-tethered / semi-untethered / untethered | 社区按「重启后是否需重新触发利用」区分的公开越狱形态；同行评审中无以该术语为分类框架的学术定义 | 社区与第三方 | 5 / 第 13 章 |
| rootful / rootless | 社区对系统分区是否可写的区分；最接近的学术用法是研究在实验设置里直接写明所用越狱种类 | 社区与第三方（学术仅借用一层） | 5、2 / 第 13 章 |
| corebaseless | **本次核查未获得任何出处**；疑为对社区口径的误记，可核实的近似术语只有 rootful/rootless 与 tethered 系列 | 无法定级 | 6 / 第 13 章、附录 E |
| TrollStore | 学术通道分类表中的侧载方式之一，机制栏记为 iOS CoreTrust exploit、生命周期 Permanent，并被记述「仅限较旧 iOS 版本」 | 同行评审研究 | 2 / 第 13 章 |
| 静态检出 / 动态执行 | 两种不可合并的测量判据：包内字符串存在 ≠ 运行时真的执行检测（96.7% vs 24.9% 的口径差） | 同行评审研究 | 2 / 第 14 章 |
| 设备农场 | 厂商风控口径中的广告欺诈基础设施；以 iOS 设备为对象的大规模测量本次未取得 | 厂商报告（等级 3）；iOS 侧属缺口 | 3、6 / 第 16、17 章 |
| 群控 | 中文灰产口径，指对成批真机的集中控制软件/服务；与设备农场、云手机不得互相指代 | 社区与第三方 | 5 / 第 16 章、附录 F |
| 云真机 / real device cloud | 以测试为卖点的商业设备云（如 BrowserStack、Firebase Test Lab）；可支撑「真机机群可按需租用」的能力存在性，规模数字属厂商自述 | 厂商报告 | 3 / 第 17 章 |
| Corellium | 被司法与产业记录确认的 iOS 等设备虚拟化平台；闭源、付费，且研究者自陈 iOS 动态分析受限于该平台 | 同行评审研究 + 媒体报道 | 2、4 / 第 17 章 |
| 重签名 | 本报告定义：以另一有效证书重新签名并分发（论文用 re-signed） | 同行评审研究（定义为本报告自拟） | 2 / 第 12、14 章 |
| 二次打包 / repackaging / 改造版应用 | 本报告定义「二次打包」为包内容被改动后重新打包；Android 文献主用 repackaging，iOS 文献用 modified versions of legitimate apps + dynamic library injection（USENIX '26 口径：合法应用的非官方变体，即「改造版应用」），HALY 用 re-signed，厂商统一叫 repackaging/re-signing detection；本篇沿用「改造版应用」口径，**不与 Android 指标互相换算** | 同行评审研究（定义为报告自拟） | 2 / 第 14、16 章、附录 F |
| 启发式检测 | 基于特征、路径、字符串、统计的判定，无密码学绑定；Apple 一手明确其结论可被伪造 | 本报告自拟（依据为 Apple 一手） | 1 / 第 12、14、18 章 |
| 可信根 | 本报告用词，指可被服务端验证的信任起点：App Attest 根 / Apple Enterprise Attestation Root CA / Apple Private CA 根 | Apple 一手文档（术语为报告自拟） | 1 / 第 6、9 章 |
| 设备真实性 / device authenticity | Apple 原文只用 authenticity、validity、legitimacy、integrity 四词，**无 "device authenticity" 这一固定术语**；本报告标题需在术语上映射到这四词并加「以 App/密钥真实性为代理指标」限定 | Apple 一手文档（措辞映射为报告自拟） | 1 / 第 2、18 章 |
| MASVS / Resilience 1–4 | 被研究者转述的规范口径：R1 平台完整性校验、R2 防篡改与证书固定、R3 抗静态分析、R4 抗动态分析；**不给信号可靠性排序** | 同行评审研究（转述标准文档） | 2 / 第 14、17 章 |

## 附录B 一手口径摘录与来源分级总表

判定列三值：**已核实**（取得可复核的一手或同行评审原文）、**部分核实**（结论方向与题录已核，关键数字或全文未取得）、**未核实**（本次未取得可核实条目，一律视为「公开资料未披露」，不据以断言事实不存在）。出处列给 URL 通道、字段名或条目号；等级沿用对抗面简报六级体系。

### 附录B.1 机制与文档口径（Apple 一手为主）

| # | 机制陈述 | 判定 | 依据类型与出处 | 正文所在章 |
| --- | --- | --- | --- | --- |
| 1 | 平台侧只有三条可服务端验证的真实性通道：DeviceCheck 两位状态位、App Attest 密钥证明与断言、MDM 的 Managed Device Attestation；三者适用前提与信任根互不相同，不得混用证据 | 已核实 | 1（developer.apple.com 文档 JSON 通道 + MDM Protocol Reference） | 第 5–9 章 |
| 2 | DeviceCheck 组成原文：App 侧框架接口 + 自有服务端接口，"get a token that you use on your server to set and query two binary digits of data per device, while maintaining user privacy"，示例为促销滥用与标记欺诈设备 | 已核实 | 1（框架页、`DCDevice`） | 第 5 章 |
| 3 | bit 的字段名为 `bit0`/`bit1`（另有 `device_token`、`transaction_id`、`timestamp`；查询响应含 `last_update_time`），语义由开发者自定义，且「何时重置」的责任明确归开发者服务端；Apple 无「设备端无法清除 bit」表述 | 已核实（前半）/ 未核实（禁令句） | 1（`DCDevice`、服务端文档）+ 6 | 第 5 章、附录 E |
| 4 | 唯一带 "can’t reset" 措辞的对象是指标而非 bit："The App Attest service records device metrics that you can’t reset." | 已核实 | 1（`preparing-to-use-the-app-attest-service`） | 第 5、8 章 |
| 5 | `generateToken` 返回 "An ephemeral token that identifies the current device"，官方要求按单次使用对待、重试应重新生成；生成方 App 须注册在开发者账号下；服务端接口另可验证 token 来自「你的 App 运行在 Apple 设备上」 | 已核实 | 1（`generateToken` 页 + 框架页） | 第 5 章 |
| 6 | 服务端凭据形态：query / update / device-validation 三类 POST，鉴权用 ES256 的 Base64URL JWT（复用 APNs provider token 流程）；基址 api.devicecheck.apple.com 与 api.development…；响应码族含 400（Bad Device Token/Bad Bits/Bad Timestamp）、401、403、405、429、500、503、404；位图能力文档未列出任何需声明的 entitlement（记为「未见要求」，非「官方声明不需要」） | 已核实 | 1（服务端文档页） | 第 5、9 章 |
| 7 | `isSupported` 为只读属性（两框架皆然，ObjC 键名 `supported`），且两框架语义不同：DeviceCheck = 「设备支持 DeviceCheck API」，App Attest = 「该设备提供 App Attest 服务」；单例入口 `DCDevice.current` | 已核实 | 1（属性页元数据） | 第 5、7 章 |
| 8 | 能力上限总纲："No single policy can eliminate all fraud. For example, App Attest can’t definitively pinpoint a device with a compromised operating system. Instead, the DeviceCheck services provide information that you can integrate into an overall risk assessment for a given device." | 已核实 | 1（框架页） | 第 3、12、18 章 |
| 9 | DeviceCheck 框架页、`DCDevice`、`generateToken`、`accessing-modifying-per-device-data` 四页全文无 fingerprint / tracking / 禁止指纹字样；相关约束须改从 Guidelines 5.1.2(iii) 与 5.1.2(i) 取证 | 已核实（推翻原预期） | 1（四页全文检索） | 第 5、11 章、附录 E |
| 10 | `DCAppAttestService` 定位："A service that you use to validate the instance of your app running on a device."；入口 `shared`；闭环叙述为「在设备上生成特殊密钥 → 由 Apple 证明其有效性 → 之后每次请求敏感数据时用该密钥断言 App 合法性」 | 已核实 | 1（类页 abstract、框架页） | 第 6 章 |
| 11 | `generateKey` 返回 `keyId`，"The framework securely stores the key in the Secure Enclave."；无标识符即无法使用或取回该密钥，故须立即记录；要求每用户账号建唯一 key；key 不跨越重装、设备迁移与从备份恢复，Apple 建议把新建 key 限制在这三类事件与新用户以压低设备上 key 数量 | 已核实 | 1（`generateKey`、`establishing…`、`assessing-fraud-risk`） | 第 6、7、8 章 |
| 12 | 私钥不可读改的一手原文："from where no process can ever directly read or modify it, ensuring its security."（Apple 用词是 read or modify，「不可导出」属转述）；侧证："The Secure Enclave is isolated from the main processor … even when the Application Processor kernel becomes compromised." | 已核实（措辞需注意） | 1（`establishing…` + 平台安全指南 SE 段） | 第 7、19 章 |
| 13 | `attestKey` 的 `clientDataHash` = "A SHA256 hash of a unique, single-use data block that embeds a challenge from your server. Should be at least 16 bytes in length."；`attestationObject` = "A statement from Apple about the validity of the key associated with `keyId`."；服务端验不过即丢弃 keyId；`serverUnavailable` 须以同 key 同 hash 重试以保指标；`generateAssertion` 的 hash 定义改为「待签名数据」，断言次数无限制，可在访问付费内容等关键时刻重申 | 已核实 | 1（三处 API 页 + 两篇指南） | 第 6、8、9 章 |
| 14 | attestation 与 assertion 的官方区分存在且有原文：证明密钥来源 vs "Each verified assertion reestablishes the legitimacy of the client."；框架 abstract 另有动词用法 "asserting app integrity" | 已核实 | 1（三处并置） | 第 6 章、附录 F |
| 15 | 证明对象依 WebAuthn 规范由 authenticator data + attestation statement 构成；字段清单：`RP ID` 32B、`counter` 4B（证明中必须为 0）、`aaguid` 16B（`appattestdevelop` 或 `appattest`+7×`0x00`）、`credentialId length` 2B、`credentialId` 32B、`Encoded key` 77B（COSE 公钥）、`extensions` 变长；证书链须验证 `x5c` 自 `credcert` 起含中间与叶证书并用 Apple 证书机构验证；nonce 绑定在叶证书扩展 OID `1.2.840.113635.100.8.2`，macOS 另有 `aclBlob` OID `.8.6` | 已核实 | 1（`validating-apps-that-connect-to-your-server`） | 第 6 章 |
| 16 | `RP ID` = App ID 的哈希，App ID = 10 位 Team ID + "." + `CFBundleIdentifier`（macOS 以 TN3127 的 code signing requirement 取代 bundle ID）；**证明含 App ID 哈希与作为前缀的团队 ID，但无独立 Team ID 字段**；另含 `apple_bundle_version_01` | 已核实 | 1（同上） | 第 6、12 章、附录 E |
| 17 | `validationCategory` 0–10 档逐档有官方描述（4=App Store、5=Enterprise Universal Provisioning 或 ad-hoc、7/8/9 不适合作为类别、10=不匹配任何其他类别），服务端须校验 CBOR 字典中的该值（键名 `apple_validation_category_01`）；Apple **未**给出「重签名必落哪一档」的断言 | 已核实 | 1（同上） | 第 12 章 |
| 18 | 两项与密钥材料相关的未公开/非官方口径：曲线名与算法（`crv`/P-256/`alg`）未出现在任何已取文档页，只有 77 字节 COSE 公钥；`seKey` 在 DeviceCheck 全部页面零命中，检索到的 sekey 均指第三方项目 | 未核实（公开资料未披露） | 6（曲线）+ 5–6（术语） | 第 6 章、附录 D/F |
| 19 | App Attest 不是独立框架，与 `DCDevice` 同属 DeviceCheck；门槛 iOS 11 vs iOS 14（`DCAppAttestService` watchOS 9.0 起）；**无任何「演进/替代/优先选用其一」叙述**；`deprecated`/`deprecatedAt` 在全部已取页面零出现 | 已核实（含两项负面结论） | 1（框架页与 JSON 元数据） | 第 3、6 章、附录 E |
| 20 | Apple 平台安全指南（August 2026）全文 grep DeviceCheck / Device Check / App Attest / DCAppAttest **零命中**，不设相关章节；不可作为这两项机制的一手依据 | 已核实（否定结论） | 1（本地 apsg.txt 全文检索） | 第 4、7 章、附录 E |
| 21 | `isSupported=false` 的后果：Apple 要求 App "gracefully bypass the service"，并要求服务端在设备不支持时改变行为、「can no longer require assertions」，即降级放行且**不提供替代判定**；机型支持枚举与 `simulator`/`emulator` 字样全部未公开 | 已核实（后果）/ 未核实（机型与模拟器） | 1 + 6 | 第 7、18 章、附录 D |
| 22 | 支持面两处官方口径：Mac 上（含 Mac Catalyst 与 Apple silicon 上运行的 iOS/iPadOS App）该值为 `false`；扩展类型支持在 `establishing…`（Action、extensible SSO、watchOS 受支持）与 `isSupported` 页（`generateKey` 在任意 app extension 中失败、仅 watchOS 9+ 扩展支持）之间**自相矛盾** | 已核实（Apple 文档内部冲突） | 1（两处原文并列） | 第 7 章、20.3.1、附录 F |
| 23 | Apple **不提供**「是否越狱」「设备完整性」「风险评分」类判定 API：相关页面 `jailbreak` 零命中，公开成员清单仅 `current`/`shared`、`isSupported`、`generateToken`、`generateKey`、`attestKey`、`generateAssertion`、`DCError` | 已核实（否定结论） | 1 | 第 7、8 章 |
| 24 | 但 Apple 提供一个风险指标：近 30 天该设备上 attested key 的近似计数，"A count that’s higher than expected might be an indication of a compromised device…"；同时明文 "it isn’t possible to detect fraudulent activity with absolute certainty"，且重装/备份恢复/转让会正当推高，要求开发者 "tune your risk assessment logic" | 已核实（修正原预期） | 1（`assessing-fraud-risk`） | 第 8 章、19.1、附录 E |
| 25 | receipt 为 PKCS #7 容器（签名 + 证书链 + ASN.1 编码负载），字段 2 App ID、3 attested 公钥、4 client hash、5 token、6 类型（ATTEST/RECEIPT）、12 创建时间、17 风险指标、19 Not Before、21 过期时间；本地校验要求验签、追溯到 App Attest Apple 根、字段 2 为本 App、字段 12 ≤5 分钟、字段 3 与首次存库一致；仅服务端申请的 RECEIPT 带字段 17；刷新须过字段 19（否则 304）并在字段 21 前完成 | 已核实 | 1（同上表格逐字） | 第 8 章 |
| 26 | 指标端点 data.appattest.apple.com / data-development…，同样使用 APNs 风格 JWT 且申请密钥时须启用 DeviceCheck 服务；环境不可混用："You can’t use a receipt generated in one environment to request a new receipt in the other." | 已核实 | 1（同上） | 第 9 章 |
| 27 | `DeviceInformation` 命令 "Get detailed information about a device."，响应含 `QueryResponses` 字典（80+ 项安全相关字段）；device channel 覆盖 iOS/macOS/Shared iPad/tvOS/visionOS/watchOS，user channel 为 macOS/Shared iPad；Requires supervision = N/A；多数查询项需 Device Information access right，网络类需 Network Information access right | 已核实 | 1（MDM Protocol Reference） | 第 10 章 |
| 28 | `DevicePropertiesAttestation` 原文语义：值为 DER 形式证书数组构成的链，根为 Apple Enterprise Attestation Root CA，第一项为叶证书，内含描述设备的自定义 OID；OS 版本与注册类型决定出现哪些 OID；Apple 证明服务器无法确认某属性时「生成空值、省略该 OID 或拒绝签发」；硬件门槛为 A11 及以后 / 全部 M 系列 / Apple TV A12+ / Watch S4+，A10x 及更早（含 Intel Mac、Vision Pro）忽略 `DevicePropertiesAttestation` 与 `DeviceAttestationNonce` 两键 | 已核实 | 1（同页 + 支持表逐条） | 第 9 章 |
| 29 | MDA 的 OID 清单（iOS 16/macOS 14 世代引入）：`.9.1` 序列号、`.9.2` UDID（Mac 上等于 `ProvisioningUDID`，Apple 明文与 MDM 协议他处的 UDID 不一致）、`.9.4` sepOS 版本（"the version of the operating system running on the Secure Enclave when the attestation is generated"）、`.10.2` freshness（对应 `DeviceAttestationNonce`，缓存可能不匹配）；iOS 17.2/macOS 14.2 起另有 OS 版本、`SoftwareUpdateDeviceID` 与 LLB 版本 | 已核实 | 1（逐表核实） | 第 9 章 |
| 30 | 完整性类断言的平台分裂：SIP 状态（`.13.1`）、Secure Boot 模式（`.13.2`，Full/Reduced/Permissive Security）、第三方 kext 许可（`.13.3`）**仅 macOS 14.2 引入**；`SystemIntegrityProtectionEnabled` 仅 macOS 10.12+；`SecurityInfo` 的完整性字段（ARV、SecureBoot、FileVault、bootstrap token、恢复锁）亦几乎全为 macOS 专属，iOS 侧仅通行码合规类与 `HardwareEncryptionCaps` | 已核实 | 1（`securityinfo` 页与 OID 表） | 第 9、10 章 |
| 31 | `InstalledApplicationList` 的 `AdHocCodeSigned`、`AppStoreVendable`、`IsValidated`、`DistributorIdentifier`、`Source` 等是**来源/签名类别标签**，且仅对受管设备可见，不是「该 App 被改动」的判定 | 已核实 | 1（installedapplicationlistitem 页） | 第 10 章 |
| 32 | MDM 的三条负面结果：`QueryResponses` 全部 95 个键名中不存在任何 jailbreak/越狱/root/Cydia/integrity/trust-level/risk 字段；整个 Device Management 抓取范围无 `jailbreak` 字样；没有任何字段适用于未受管（BYOD/个人）设备 | 已核实（否定结论） | 1（逐字段核对 + grep） | 第 10 章、附录 E |
| 33 | MDA 校验语义："The feature provides a cryptographic declaration of device properties that you can use as part of a trust evaluation. This feature helps protect against various threats, such as a compromised device providing false information."；"There’s no scenario during normal operations in which this validation step fails. You can choose to ignore the attestation entirely, or flag it as an incident that needs investigation."；另有 ACME 工作流（`device-attest-01` challenge，"provides a chain of trust from the CSR back to the hardware-bound private key"） | 已核实 | 1（validating 页） | 第 9 章、20.4.1 |
| 34 | 标识符三项官方口径：IDFA 每设备唯一且仅用于广告、iOS/iPadOS 14.5+ 须先取得追踪授权、多情形返回全零（含「In Simulator, regardless of any settings.」）且不建议存储；IDFV 对 vendor 唯一、删光同 vendor 应用后重装会变、Xcode 测试构建与 ad-hoc 会变、重启未解锁前为 `nil`；公开 SDK 未暴露序列号/UDID 读取（由 `UIDevice` 属性清单支撑），Apple 无「不得读取序列号」原文 | 已核实（前两项）/ 部分核实（第三项为清单型负面结论） | 1（AdSupport、`UIDevice`）+ 6 | 第 11 章、附录 E |
| 35 | 可引政策条目：2.5.1（仅可用公开 API、须运行于当前发布系统）、5.1.1、5.1.2（Data Use and Sharing）、5.1.2(i)（ATT 显式许可）、5.1.2(iii)（禁止秘密构建画像/识别匿名用户/重建画像）、2.3.1(a)（误导性推广如宣传 iOS 病毒与恶意软件扫描类能力）；Guidelines 全文（240,818 字节）`fingerprint` 出现 0 次，亦无 IDFA/IDFV/vendor/serial number 字样 | 已核实（含否定结论） | 1（App Store Review Guidelines，以抓取当日版本为准） | 第 11 章、20.3.3 |
| 36 | 被篡改 App 的威胁模型原文："Someone who modifies your app and distributes it outside the App Store can add unauthorized features like game cheats, ad removal, or access to premium content."；官方场景词只有 game cheats / ad removal / premium content / promotional offer / fraudulent / advertising fraud detection；`bot`、`automation`、`emulator`、`device farm`、`jailbreak` 全部零命中 | 已核实（含否定结论） | 1（框架页 + 全页检索） | 第 12 章、附录 E |
| 37 | 客户端自检不可信的官方立场："You can’t rely on your app’s logic to perform security checks on itself because a compromised app can falsify the results."；同页另述 "A compromised version of your app could falsify the verification result, thus circumventing App Attest." | 已核实 | 1（`establishing…`、`attestKey` 页） | 第 12 章、19.3 |
| 38 | 边界两句 + 一个被点名的滥用形态："A compromised version of your app running on a genuine, unmodified Apple device can’t create valid assertions. However, an attacker that modifies the device’s operating system might bypass restrictions. Although modifying the operating system is difficult and an unlikely source of widespread fraud, you might need to guard against an attack that uses a single compromised device to serve assertions to many subscribers."（"unlikely source" 属 Apple 文档自评，非测量结论） | 已核实（自评须标注） | 1（`assessing-fraud-risk`） | 第 7、12 章、19.3 |
| 39 | 运行期完整性校验的一手范围："At runtime, code signature checks of all executable memory pages are checked as they're loaded to help ensure that an app hasn't been modified since it was installed or last updated."（校验对象是「自安装或上次更新后是否被改动」，非「审核时那一份」） | 已核实 | 1（平台安全指南 August 2026，第 115 页段） | 第 12 章 |
| 40 | 运维与错误码：完整接入需自建 ES256 密钥轮换、per-user-per-device 的 keyId/公钥/receipt 三元组（dev/prod 分份）、challenge 与 counter 状态机、429/503/304 退避与指标刷新调度、Apple 私有 CA 根下载与长期信任、重新证明流；`DCError` 仅 5 个粗粒度 case，**不存在篡改/越狱类错误码**，客户端错误只能作重试依据；速率/SLA/阈值/receipt 保留期/SE 内 key 上限/bit 历史/机型白名单/模拟器行为/重复证明限制/403 触发条件均未公开 | 已核实（前段）/ 未核实（后段清单） | 1 + 6 | 第 9 章、20.2 |
| 41 | ADEP 一手规则：仅供「大型组织向员工分发自有的内部使用应用」，须私密安全地仅分发给员工、须有机制确保只有员工可下载；准入为 100 名及以上员工、法人实体（不接受 DBA/虚拟主体/分店）、D-U-N-S 编号、与组织关联的公开网站域名、299 USD/年；须通过验证访谈与持续评估，续订时再次验证，Apple 保留 sole discretion 拒绝权；页面把 ADEP 定位为其他分发方式都不满足时的兜底通道 | 已核实 | 1（developer.apple.com/programs/enterprise/） | 第 15 章 |
| 42 | 吊销机制一手陈述：首次打开 in-house 应用时联系 Apple OCSP 服务器校验证书，被吊销则应用不启动（设备需能访问 ocsp.apple.com）；OCSP 响应在设备缓存 **3–7 天**，缓存未过期前不再校验；"Revoking a distribution certificate invalidates all of the apps you've signed with it."，故 Apple 称吊销应为最后手段；分发描述文件 12 个月、发行证书 3 年或随会员到期，但**已签名应用不受到期影响**（与吊销是两件事）；iOS/iPadOS 18 起手动安装的 in-house 应用需重启设备完成信任，经设备管理服务安装者被隐含信任 | 已核实 | 1（部署指南 depce7cefc4d，Published Date 2026-09-17 + 平台安全指南第 115 页 + 支持文档 118254） | 第 15 章、附录 E |
| 43 | 两项量化锚点：Apple 公开的唯一数字为「建议每 App 每日渐进放量不超过 1,000 万用户」与「跨全部安装调用不超过 100 req/s」，原文自称 rule of thumb 且称阈值可能动态浮动；明文要求仅三项（challenge ≥16 字节、receipt 创建时间 ≤5 分钟、指标覆盖近 30 天） | 已核实 | 1（`preparing-to-use-the-app-attest-service` 逐字） | 第 9 章、20.2.4 |

### 附录B.2 对抗面与研究口径

| # | 机制陈述 | 判定 | 依据类型与出处 | 正文所在章 |
| --- | --- | --- | --- | --- |
| 44 | 越狱形态术语（tethered / semi-tethered / semi-untethered / untethered、rootful / rootless）来源为社区维护 wiki 与百科，同行评审中无以该术语为分类框架的学术定义；`corebaseless` 在社区文档、百科与论文中均无出处 | 已核实（等级 5 来源）/ 未核实（corebaseless） | 5 + 6（另：最接近的学术用法仅借用 rootful 一层，等级 2） | 第 13 章、附录 A/E |
| 45 | 「A12 及以上长期缺少公开可用越狱」只有三条间接依据（HALY 实验机型 A11 + iOS 16.4.1 + rootful palera1n；USENIX '26 称 TrollStore 仅限较旧 iOS；ESORICS '25 workshop 提及 rootless）；checkm8 影响 A7–A11 的芯片边界仅有社区与媒体口径 | 部分核实 | 2（三条）+ 5/6（边界与时间线） | 第 13 章、21.4 表 |
| 46 | 同行评审口径下的绕过/侧载通道分类表：In-House（企业证书，1 年）、AltStore（免费开发者账号，7 天）、TestFlight（90 天）、Jailbreak（"Root access via iOS vulnerabilities"，Permanent）、TrollStore（"iOS CoreTrust exploit"，Permanent）、WebClip（Permanent）、Ad Hoc（1 年） | 已核实 | 2（USENIX Security '26 Table 2） | 第 13 章 |
| 47 | 银行应用越狱检测实测：34 个中 18 个实现检测（44% 未尝试检测），「试图检测者除 1 个之外全部检测失败」，绕过为现成函数 hook、无需逆向或改包；跨类 3,482 应用中 59%（银行 53%）含某种检测；作者归因于从社区复制粘贴检测代码；论文内部另有 15/34 与 18/34 两处计数不一致 | 已核实（须附 2018–2019 条件） | 2（Kellner et al., EuroS&P 2019；59%/53% 经 HALY 转述核对） | 第 14 章、19.4 |
| 48 | 静态 96.7% vs 动态执行 24.9% / 6.8%；针对 iOS 15（2021-09）及之后发布的越狱未发现任何专用特征检测（"jailbreak detection in apps is not updated very often after new jailbreaking techniques are released"）；主要特征对象为 blackra1n（5.5%）与 unc0ver（1.4%） | 已核实（两口径不可合并） | 2（HALY, EuroS&P 2025） | 第 14、19 章 |
| 49 | 采用率与自防护强度：App Attest 在 iOS 应用静态 4.1% / 动态 1.1%；73.6% 的 iOS 应用采用少于 MASVS 建议一半的自防护技术（Android 24.1%）；iOS 侧仅 1 个应用采用全部七项（Android 26 个）；作者结论为「单个或少数几项加固技术说明不了自防护有效性…目前的采用状况可能给用户和开发者一种虚假的安全感」 | 已核实 | 2（HALY） | 第 14、18 章 |
| 50 | 越狱检测误报率无任何同行评审测量；可核实的只有规范口径「即使以牺牲漏报为代价，也要最小化误报」（等级 2 转述 MASVS）与等级 3 厂商承认、等级 5 社区个案 | 未核实（就「大量」而言） | 2/3/5 混合 + 6 | 第 14 章、20.3.2、附录 E |
| 51 | iOS 上应用无法直接核验自身原始签名者：商店应用一律被 Apple 重签名、设备仅执行 Apple 签名二进制（除非该检查被越狱关闭），因此应用只能退化到「检查 `embedded.mobileprovision` 是否存在」这类弱特征；Apple 把「验证应用未被修改」的能力放在服务端侧的 App Attest | 已核实 | 2（HALY 3.3/3.4 节原文） | 第 12、14 章 |
| 52 | 工具链限制：MobSF 的 iOS 动态分析限定于 Corellium VM（闭源、付费服务提供平台）；HALY 要求任意可越狱设备；其 190 个（5.7%）iOS 应用因需在已越狱设备做内存转储而无法解密；「越狱覆盖到 iOS 16、机型 A11」 | 已核实 | 2（HALY） | 第 17 章、19.4 |
| 53 | 灰产规模观测（**均为研究观测下界**）：3,359 个活跃签名站点（自 8,358 候选域名筛出）、2,118 个唯一工具名、仅 28 个上游证书站点负责签发、单站最多支撑 135 倍下游、最大集群单站 843 个签名站点、8,216 个非官方 IPA 条目（2,660 含直链、实下 2,654 个应用）、1,883 个唯一 baseapp 中 1,023 个存在非官方变体（WeChat 492、小红书 141、抖音 100、X 97、TikTok 91） | 已核实（附作者局限） | 2（USENIX Security '26，地理聚焦中国、仅 Ad Hoc 自助签名通道） | 第 16 章、19.5 |
| 54 | 设备身份与账号配额被资产化：个人开发者账号每年最多注册 100 台设备（前 10 台即时、其后 24–72 小时审核），对 macOS 的执行放松使实践可绑定约 200 台，单台绑定成本约 RMB 3.4、终端支付为 10–30 倍、中间层利润率 900%–3,000%；证书站点维护跨账号 .p8 密钥池并行/随机派发以稀释绑定模式，并提供「保修期内被吊销可免费更换」的预付码 | 已核实 | 2（同上） | 第 15、16 章 |
| 55 | 杀检生态对此类改造包基本失明：8,085 个抽取 dylib 经 VirusTotal 扫描仅 3 个被标记恶意；2,654 个下载应用中 442 个明确且仅以「破解 VIP」为卖点；作者结论为注入组件多为增加功能而非分发显性恶意代码，但「hook 运行时内存、操纵流量、绕过系统控制的行为破坏了应用逻辑并削弱 iOS 安全隔离」 | 已核实（不可读作「无害」） | 2（同上） | 第 16 章 |
| 56 | 条件式有效性结论（本篇唯一可引的正向判断）：「尽管部分应用实现了越狱检测，但这类措施可以通过动态库 hook 轻易绕过，其有效性受限；因此仅依赖客户端机制是不够的。关键逻辑（如 VIP 状态与环境校验）应交由服务端校验。此外，运行时完整性检查（如监控非预期内存改动）对 dylib 类篡改有效」；同文另述运行期插件式加载可规避以重打包为基础的检测机制 | 已核实 | 2（同上） | 第 12、18 章、19.6 |
| 57 | 吊销的两个已公开失效条件并置：OCSP 响应缓存 3–7 天（等级 1）与研究记述的「存在通过阻断设备访问 Apple 证书校验端点使已吊销证书签名的应用继续可用的做法」（等级 2，仅记能力存在性，正文不写实现） | 已核实（前半）/ 已核实为记述（后半） | 1 + 2 | 第 15 章 |
| 58 | 企业证书通道现状与时间纵深：「In-House distribution relies on enterprise certificates and has been widely misused for public app distribution… was once common but is now tightly regulated」（等级 2）；早期研究 Enpublic Apps（ASIACCS 2015）、Telegram 地下分发（POMACS 2024）、WebClip 测量（arXiv:2209.03319，**预印本未见同行评审确认**）、Black Hat Asia 2016 议题（等级 3/4，讲稿未取得） | 已核实（现状判断）/ 部分核实（题录级） | 2 + 3/4 | 第 15、16 章 |
| 59 | 本土定性材料：最高人民检察院官网转载《"超级签名"揭露地下网络犯罪生态》（2022-03），被 USENIX '26 论文引为该现象来源之一；可说明该业态已被司法机关作为犯罪生态立案报道，其涉案金额与人数属个案司法统计 | 部分核实 | 4（检察系统公开材料） | 第 16 章 |
| 60 | 2019-01 Facebook 企业证书被吊销、Google 同期被阻断后恢复（两家媒体口径不同，不合并）；2019 WhatsApp/Pegasus 与 2020 Zoom 企业证书静默跟踪两案仅完成事件级定位，可引用原文页面未取得；Apple 2025-02 对俄罗斯开发者关闭 ADEP 仅可作地域收紧的存在性引用（未见 Apple 一手公告） | 部分核实 | 4（媒体）+ 3 | 第 15 章、附录 C/F |
| 61 | App Attest / DeviceCheck 的独立弱点或绕过研究：同行评审**零篇**；HALY 自陈「device binding 的实现高度依赖具体应用，使得自动检测困难，这些技术的流行度需要更多研究」；其余讨论均为等级 3–5 的观点性内容，不可据以撰写绕过成本结论 | 已核实（负面结论） | 2（HALY）+ 6 | 第 14、17 章、19.6 |
| 62 | 信号强弱的可引定性判据（非排序表）：为每个发现打置信度分，依据是「该信号存在的其他合理用途」——搜索具体越狱名称为强指标，调用 `fork` 可能另有用途；实测 95.7% 的 iOS 应用调用 `_dyld_image_count`、74% 同时调用二者，相关结果被标为高不确定性；检测 Cydia Substrate 同属 hooking 与 root 检测，故类别计数加总不等于 100% | 已核实 | 2（HALY） | 第 14 章、18.7 |
| 63 | 「检测库普遍可被绕过」须修正口径：论文测的是应用内实现与其运行时行为，不是商业检测库产品；「普遍可绕」限于 Kellner 2019 的 hook 能力条件，HALY 2025 给出静态≠动态的口径修正；不得合并为「研究表明检测普遍无效」一句 | 已核实（口径修正） | 2 | 第 14 章、附录 E |
| 64 | 重签名检测的口径不对称：体系化研究集中在 Android（AYPE NDSS 2011、AppInk ASIACCS 2013、DroidMarking ASE 2014、ACSAC 2014、Repackage-proofing DSN 2016、VAhunt CCS 2020、ModZoo APWG eCrime 2024）；**未取得**把 iOS 重签名包作为独立研究对象并给出检测方法的同行评审论文；厂商产品普遍列有 repackaging/re-signing detection，但无公开方法学与可复核样本量的第三方评测 | 已核实（不对称）/ 未核实（iOS 专门研究、检出率） | 2（题录级）+ 3 + 6 | 第 14、17 章 |
| 65 | 仅完成题录核对、禁止引用数字的研究：ESORICS 2025 International Workshops（Irfan & Uto，rootless 条件下重估 iOS 银行应用检测，LNCS pp. 558–577）、iOSModZoo（ACM WiSec 2026）、Taking a Bite Out of the Forbidden Fruit（ACM IMC 2026, pp. 175–187）、Evans 2015（经两篇引用核对标题与作者）、Sun 等 182 个 Android 应用（HALY 转述）、Ruggia 2024（61%）、AttestLens（WiSec 2026，Android Play Integrity 采用率测量） | 部分核实 | 2（题录级/转述级） | 第 14、17 章、附录 C |
| 66 | iOS 模拟器/云手机被滥用的任何一手或权威二级证据：**未取得**；中文「iOS 云手机/ARM 云手机支持原生 iOS 应用」表述全部来自营销页与技术博客（等级 5，判定不可用于事实陈述），且存在相反的营销口径，二者均不构成依据 | 已核实（负面结论） | 6 + 5 | 第 17 章、18.4 |
| 67 | iOS 虚拟化存在且被司法与产业记录确认：Apple 2019-08 起诉 Corellium、2020 年地方法院驳回主要主张、2021-08 撤诉并达成未披露条款的和解、2023-05 第十一巡回上诉法院拒绝恢复版权主张、2023-12 余部和解；合法性建立在安全研究用途之上，服务闭源付费 | 部分核实 | 4（多家报道相互一致） | 第 17 章 |
| 68 | 测试用云真机为公开商业产品（BrowserStack 自述 2 万+ 真实 Android/iOS 设备、Firebase Test Lab 提供 iOS 真机机型清单）：可支撑「真机机群可按需租用并以设备池形式商业化」的能力存在性；规模数字属厂商自述，不得用作滥用规模 | 部分核实 | 3（上限） | 第 17 章 |
| 69 | 以 iOS 为对象的设备农场/群控大规模测量：**未取得**（可核实者为 Android 设备农场或跨平台账号黑产：Mirian WWW 2019、DeKoven IMC 2018）；厂商风控材料（AppsFlyer、Fingerprint、Branch、myTracker、Scalarr）只可支撑「行业把设备农场/机器人/模拟器视为共同滥用载体」与「厂商普遍把 Android 列为主要载体」两条存在性与趋势表述，其百分比（如「2018 年占全部欺诈 5.8%」）为互相转引、无一手测量 | 已核实（负面）+ 部分核实（存在性） | 2 + 3 + 6 | 第 16、17 章 |
| 70 | 应用层自动化滥用的可追溯学术测量对象是 Android：USENIX Security '24 抢票生态（758 应用、3,121 版本）在局限一节明文「我们只关注 Android 应用，未纳入 iOS 应用，主要原因是 iOS 应用商店的更严格要求导致此类应用数量本身有限」，动态分析使用 LDPlayer 模拟器并排除表现不佳的应用；该文另给出「平台持续改进检测、抢票应用相应演化规避手段」的竞赛表述 | 已核实（作者的因果解释，非测量结论） | 2（原文局限一节） | 第 17 章、19.5 |
| 71 | 越狱群体「小众」论断不成立：Kellner 2019 摘要原文为 "jailbreaks gain tremendous popularity among regular users"，并转引 Cydia 方称「超过 3,000 万设备使用其系统」（2018 口径，服务方自述、无方法学，不可用于事实陈述）；Apple 平台安全指南全文出现 "jailbreak" **0 次**，其口径为 untrusted code / unsigned apps / compromised，DeviceCheck 文档口径为 "a device with a compromised operating system" | 已核实（含两项否定结论） | 2（转引）+ 5 + 1（零命中） | 第 13、16 章、附录 E |
| 72 | 「很难找到把检测信号按易伪造程度排序的严肃来源」：量化排序表**缺失**（未发现任何同行评审研究给出 iOS 检测信号的误报率或可伪造成本排序），但存在可引的定性判据与条件式结论；MASVS/MASTG 只给应实现的韧性类别与测试方法，**不给信号可靠性排序** | 部分核实（禁止自制量化表） | 2 + 6 | 18.7、20.1、21.4 表 |

## 附录C 研究证据对照表

本表把对抗面简报引用的每项研究并列，末列给出**使用边界**：允许如何引用该结论、禁止如何外推。等级列沿用六级体系；凡「全文未取得」者，其任何数字均不得进入正文。

| 研究（会议/年份） | 样本与口径 | 结论方向 | 作者自陈局限 | 等级 | 本报告允许如何使用 / 禁止如何外推 |
| --- | --- | --- | --- | --- | --- |
| Kellner, Horlboge, Rieck, Wressnegger, *False Sense of Security: A Study on the Effectivity of Jailbreak Detection in Banking Apps*, EuroS&P 2019 | 34 个 App Store 银行应用（18 个实现检测）+ 跨类别 3,482 个最流行应用 | 试图检测越狱的应用「除 1 个之外全部检测失败」；绕过为现成函数 hook，无需逆向或改包；开发者常从社区复制粘贴检测代码 | 结论限于作者 2018–2019 年所用检测点位与其 hook 能力；写作时点锚 iOS 12.1 | 2 | 允许：作为「端侧检测可被绕过」的主证，并附年份与条件。禁止：写成「所有检测在任何条件下均无效」；把 2018 年口径描述为当前状态；把内部 15/34 与 18/34 两处计数合并 |
| Steinböck, Troost, van Beijnum, Seredynski, Bos, Lindorfer, Continella, *SoK: Hardening Techniques in the Mobile Ecosystem — Are We There Yet?*（HALY），EuroS&P 2025 | 3,322 池 → 每 OS 各 2,646（合计 5,292）；静态 + 动态分析；结果视为下界 | 静态 96.7% vs 动态 24.9%/6.8%；检测清单陈旧（无 iOS 15+ 专用特征）；App Attest 静态 4.1%/动态 1.1%；73.6% iOS 应用低于 MASVS 建议一半；单项防护可绕、有效性取决于实现与多层叠加 | 作者自陈结果为下界；其越狱覆盖仅到 iOS 16、实验机型 A11（iPhone 8, iOS 16.4.1, rootful palera1n）；190 个（5.7%）应用无法解密；device binding 类「需要更多研究」 | 2 | 允许：作为 iOS 检测有效性与采用率的主证，并逐条标注静态/动态判据。禁止：把 96.7% 与 24.9% 合并成单一「检测率」；把类别计数相加；把下界当总体；把采用率外推到未测量的机制 |
| Evans, Benameur, Shen, *All your root checks are belong to us: The sad state of root detection*, ACM Mobility '15, 2015 | 16 个安全类应用 + 19 个 MDM 应用（Android 与 iOS）；**样本口径依据 HALY 转述** | 最早把「设备真实性检测自身的可靠性」当被测对象；13 个安全应用、15 个 MDM 应用使用 root 检测 | 本次未取到原文 PDF；标题与作者经两处引用双重核对 | 2（部分核实） | 允许：作为「该问题自 2015 年即被当作研究对象」的时间锚。禁止：引用其任何比率作为 iOS 结论；在未取得原文前用于任何量化叙述 |
| Irfan & Uto, *Revisiting the Effectiveness of Jailbreak Detection*, ESORICS 2025 International Workshops, LNCS pp. 558–577 | iOS 银行应用；以新型 rootless 越狱为条件重估 | 「rootless 使传统基于特征文件的检测失效」这一命题已进入同行评审议程 | 具体样本量、绕过成功率与结论原文未取得（ACM 403、Springer 303） | 2（workshop 后论文集，强度低于主会） | 允许：只引其研究问题与存在性。禁止：引用其任何百分比；用于支撑「rootless 使检测全面失效」的断言 |
| Liu Yijing, Zhang Yiming, Liu Baojun, Duan Haixin, *Cracks in the Walled Garden: … Ad Hoc Sideloading*, USENIX Security '26, 2026-08 | 用户视角采集 + passive DNS（2025 年 3–5 月日志）；14 个仓库提取 IPA；12 个签名工具分析（采购支出 RMB 500）；定性理论饱和抽样 | 3,359 活跃站点/28 证书站点；UDID 注册配额与账号额度被资产化（100 台/年、实践约 200 台、单台约 RMB 3.4、900%–3,000% 利润率）；8,085 dylib 仅 3 个被标记恶意；「仅绕过客户端校验，只在缺少服务端校验的应用中有效」 | 自陈地理聚焦中国；仅覆盖 Ad Hoc 自助签名这一条通道；独立灰产应用核心逻辑「重度混淆、设计异构，系统化分析不可行」留作未来工作；敏感清单不公开、已上报 Apple，第三方无法完整复核 | 2 | 允许：作为 iOS 灰产基础设施结构的主证，全部数字标注「研究观测下界」。禁止：外推为灰产总量、全球规模或其他通道；把「仅 3 个被标记恶意」读作「改造包无害」 |
| Liu Yijing et al., *Tickets or Privacy? …Chinese Ticket Grabbing Apps*, USENIX Security '24 | 758 个抢票应用、3,121 个版本；动态分析使用 Android 模拟器 LDPlayer | 平台检测与自动化规避构成持续竞赛（RQ3） | **明确仅 Android**，理由为 iOS 应用商店更严格要求使此类应用数量本身有限；排除在模拟器中表现不佳的应用；部分需 root 环境 | 2 | 允许：作为「同类测量在 Android 侧更充分」的口径不对称论证。禁止：写成「iOS 无抢票/自动化滥用」；把作者的因果解释当测量结论 |
| Saavedra et al., *ModZoo*, APWG eCrime 2024（Android） | 修改版应用与其市场的大规模测量 | Android 侧改造应用生态的系统测量基准 | 本次经 USENIX '26 参考文献核对存在性，未逐一取全文 | 2（题录级） | 允许：作为「改造应用研究正从 Android 迁移到 iOS」趋势的题录支撑。禁止：把其数字与 iOS 研究的数字并列比较或换算 |
| Saavedra et al., *iOSModZoo: A Large-Scale Study of Third-Party iOS App Markets*, ACM WiSec 2026（主会 Session 4） | 第三方 iOS 应用市场的大规模研究（样本量未取得） | 证明「以 iOS 第三方市场/改造应用为对象的同行评审研究已存在」 | ACM 页面 403，全文与数字未取得 | 2（仅题录级） | 允许：只写「此类研究已存在、对象是 iOS 第三方市场」。禁止：引用任何具体数字或结论方向；据以更新本篇的缺口判定 |
| Khanari & Rahmati, *Taking a Bite Out of the Forbidden Fruit: …Iranian iOS App Stores*, ACM IMC 2026, pp. 175–187 | 伊朗第三方 iOS 应用商店（样本量未取得） | 同上：证明此类分发与市场在特定地域被同行评审测量过 | 全文未取得 | 2（仅题录级） | 允许：作为「第三方 iOS 市场可被地区性研究测量」的存在性引用。禁止：引用任何数字；据以推断中文语境的灰产结构 |
| Zheng Min et al., *Enpublic Apps: Security threats using iOS enterprise and developer certificates*, ACM ASIACCS 2015, pp. 463–474 | 企业/开发者证书面向公众分发的早期研究 | 企业证书滥用并非新现象，至少 2015 年已被系统研究 | 本次经 USENIX '26 引用核对结论方向，未取原文 | 2 | 允许：作为时间纵深锚点。禁止：据 2015 年结构推断当前监管强度（现状由 USENIX '26「now tightly regulated」另行支撑） |
| Guo Yanhui et al., *Beyond App Markets: … via Telegram*, POMACS 8(3), 2024 | Telegram 地下分发，含 TestFlight、WebClip 等通道 | 地下分发以社交通道承载，通道多元 | 经引用核对，未取原文 | 2 | 允许：作为「分发通道谱系不止侧载」的支撑。禁止：把其规模数字当 iOS 总体滥用规模 |
| Hu Qinyu et al., *Measurement of the Usage of Web Clips in Underground Economy*, arXiv:2209.03319, 2022 | WebClip 在赌博、诈骗、色情传播中的使用 | WebClip 被用于地下经济内容分发 | **预印本，未见同行评审确认** | 2/5（混合） | 允许：作为「WebClip 通道进入过测量议程」的次级线索，并标注预印本性质。禁止：作为事实来源；引用其任何数字 |
| MacDonald & Herwig, *AttestLens: A Large-Scale Measurement of Play Integrity Adoption in Android Apps*, WiSec 2026（节目单级） | Android 平台级设备证明采用率的大规模测量 | 说明 Android 侧已有专门测量「平台证明采用率」的论文，而 iOS 侧仅有 HALY 顺带给出 | 本次仅会议节目单级核对 | 2/4 | 允许：作为「证据密度不对称」的方向性对照。禁止：把其 Android 采用率与 iOS 的 4.1%/1.1% 直接比较（判据不同） |
| Mirian et al., *Hack for Hire: Exploring the Emerging Market for Account Hijacking*, WWW 2019 | 账号劫持市场 | 账号自动化滥用的基线文献 | 由 USENIX '26 论文引用核对存在性；**非以 iOS 为对象** | 2 | 允许：作为账号黑产生态的存在性引用，须标注非 iOS 对象。禁止：用于 iOS 侧设备真实性结论 |
| DeKoven et al., *Following Their Footprints: Characterizing Account Automation Abuse and Defenses*, IMC 2018 | 账号自动化滥用与防御 | 同上 | 同上，非以 iOS 为对象 | 2 | 同上；与上一条合并使用时仍须逐条标注对象平台 |
| Egele et al.（AYPE, NDSS 2011）、Zhou et al.（AppInk, ASIACCS 2013）、Jiang et al.（DroidMarking, ASE 2014）、Shao et al.（ACSAC 2014）、Luo et al.（*Repackage-proofing Android Apps*, DSN 2016） | Android 重打包检测的体系化文献群 | 「重打包检测」研究主战场在 Android | 由 USENIX '26 参考文献列出并用作对照；单篇原文本次未逐一取全文，引用前须回原文再核 | 2（引用关系已核） | 允许：支撑「iOS 侧对应研究密度显著低于 Android」这一口径不对称陈述。禁止：把这些方法的检出率当作 iOS 可用能力；禁止逐篇未经回原文即引用数字 |
| Shi et al., *VAhunt*, ACM CCS 2020 | 针对应用虚拟化外壳伪装的重打包恶意软件 | Android 侧针对「外壳伪装」类改造的检测 | 同上，题录级核对 | 2 | 允许：作为 Android 检测谱系一环。禁止：把「虚拟化外壳」类比移植到 iOS 场景并给出结论 |
| Sun et al.（182 个 Android 应用的多技术 root 检测检验，经 HALY 转述） | 182 个 Android 应用 | 「这些方法无效」 | 本次未取原文，为转述口径 | 2（转述） | 允许：仅用于「同类研究在 Android 侧更充分」的论证。禁止：写成 iOS 结论；写成「root 检测已被证明普遍无效」的通用命题 |
| Ruggia 2024（Android root 检测流行度 61%，经 HALY 相关叙述纳入） | Android，判据与样本独立 | 与 Evans 2015、Kellner 2019、HALY 2025 共同构成四个不可合并的计数点 | 本次未取原文 | 2（题录级） | 允许：作为「同一指标存在四个合法数字」的示例。禁止：把这四个数字排成趋势线 |
| OWASP MASVS / MASTG（经 HALY 转述） | 规范文档，非测量研究 | 定义 Resilience R1–R4 与测试方法；把「最小化误报，即使牺牲漏报」列为设计约束 | 属标准文档而非实证来源 | 2（转述标准文档） | 允许：作为「误报是设计约束」的规范依据；作为分层框架的描述。禁止：据其声称「检测有效性有公认分级」——MASVS **不给信号可靠性排序** |
| *Enterprise Apps: Bypassing the iOS Gatekeeper*, Black Hat Asia 2016（议题页） | 会议议题，未取讲稿正文 | 企业证书议题至少在 2016 年已进入安全会议议程 | 讲稿正文未取得 | 3/4 | 允许：作为时间点证据（该议题的公开讨论史）。禁止：引用其中任何技术结论或有效性表述 |
| 厂商产品文档：MTD/RASP 的 repackaging / re-signing detection（多家）；风控厂商的设备农场/机器人表述（AppsFlyer、Fingerprint、Branch、myTracker、Scalarr）；MTD 厂商 root/越狱检测知识库（Promon、OneSpan） | 产品文档与博客，无公开方法学、无可复核样本 | 「该检测项在商业产品中普遍存在」「误报为工程事实」「行业把设备农场视为共同滥用载体」 | 无第三方评测验证检出率；百分比多为互相转引 | 3（上限） | 允许：只可写「该厂商声称在其自设测试口径下观察到 X」，且须写明其商业立场。禁止：升格为「业界研究表明」「实测证明」；引用其中任何百分比作为事实陈述 |

## 附录D 未公开与未决事项汇总

**声明：以下条目仅表示本次核查（截至 2026-10-19，检索范围为两份简报所列通道）未取到一手或可复核依据，**不等于该事实不存在**；其中相当一部分属「Apple 未公开」而非「Apple 否认」。** 条目由机制面简报 D 节与对抗面简报 D 节去重合并而成，「据此降级」列指明正文哪一节因该缺口采用了封顶表述。

| # | 缺口内容 | 为何重要 | 正文在哪节据此降级表述 |
| --- | --- | --- | --- |
| D1 | Apple 关于 DeviceCheck bit「设备端无法清除」的原文未取得（文档只把重置责任交给开发者） | 决定 bit 能否被当作不可篡改状态存储 | 第 5 章、18.1、附录 E |
| D2 | Apple 文档中「不得用 DeviceCheck/App Attest 做设备指纹或追踪」的限制句未取得 | 决定能否写「Apple 禁止设备指纹」 | 第 11 章、20.3.3、附录 E |
| D3 | 「App Attest 取代/演进自 DeviceCheck bits」的官方叙述未取得 | 决定两项能力的关系表述 | 第 3 章、18.1 |
| D4 | App Attest 支持的机型枚举、`isSupported` 机型白名单 | 决定「无信号」是能力缺失还是配置问题 | 第 7 章、18.4、21.4 表 #2 |
| D5 | iOS 模拟器上的 `isSupported` 取值与两机制在虚拟化环境的行为（文档无 `simulator`/`emulator` 字样） | R3 行整行的判定基础 | 第 7、17 章、18.4、21.4 表 #3 |
| D6 | App Attest 私钥曲线与算法（只有 77 字节 COSE 公钥与 X9.62 非压缩点格式） | 决定验证库的算法假设 | 第 6 章、21.4 表 #1 |
| D7 | `seKey` 无 Apple 口径（零命中），仅第三方项目与学术方案使用 | 决定术语能否作为官方概念引用 | 第 6 章、附录 A、附录 F |
| D8 | Apple 官方「是否越狱」判定服务不存在（已核实为否定结论，但其**为何**不设该判定的说明未取得） | 决定该判定责任层的解释口径 | 第 7、8 章、18.3 |
| D9 | MDM 中越狱/完整性布尔字段不存在；iOS 侧可 attestate 属性不含 SIP/Secure Boot/kext | 企业策略文档能否写成统一断言 | 第 10 章、20.4.3、18.4 |
| D10 | 各 OID 在不同 OS 版本与注册类型下的完整出现矩阵（文档零散） | 决定字段可得性的可预测程度 | 第 9 章、20.4.2 |
| D11 | Apple 证明服务器何时生成空值/省略 OID/拒签的具体条件 | 决定「空白」的告警语义 | 第 9 章、20.4.1 |
| D12 | DeviceCheck / App Attest 服务端速率上限、日配额、SLA、延迟、故障公告 | 容量规划与故障判定 | 第 9 章、20.2.4、21.4 表 #14 |
| D13 | risk metric 的计算定义、精度、是否可被 Apple 修正（只有 "approximate count"） | 服务端阈值的可解释性与合规论证 | 第 8 章、20.2.3、21.4 表 #5 |
| D14 | attestation receipt 在 Apple 侧的存储与保留期 | 数据留存与隐私合规判断 | 第 8 章、附录 E |
| D15 | `403 Forbidden`（"The specified action isn’t allowed"）的触发条件 | 故障归类（配置错误 vs 权限错误） | 第 9 章、20.2.4 |
| D16 | `last_update_time` 更新粒度、两个 bit 的历史与审计可见性 | 能否用 bit 做审计链 | 第 5 章、20.2.4 |
| D17 | Apple 服务端对同一 key 重复证明的次数限制 | 滥用检测的可用输入 | 第 9 章、附录 E |
| D18 | SE 内 App Attest key 的数量上限 | 多用户设备与配额假设 | 第 7 章、20.2.4 |
| D19 | Apple 允许的时钟偏移量（timestamp 强制但偏移容限未公开） | 400/401/304 的归因 | 第 9 章、20.2.4 |
| D20 | App Attest 对 App Clip、各类扩展、watchOS 之外平台的统一支持矩阵（两处口径冲突未消解） | 集成范围决策 | 第 7 章、20.3.1、21.4 表 #4 |
| D21 | Apple 对具体绕过手段（越狱工具、重签名方案、企业证书分发）的公开结论与胜率数据 | 决定 R1/R2 行的现实强度表述 | 第 12 章、18.3、20.2.4 |
| D22 | 「A12 及以上长期无公开可用越狱」的时间边界与一手/学术依据；checkm8 类 bootrom 影响的芯片代际边界 | 决定对抗面能否具体化到机型 | 第 13 章、18.3、21.4 表 #12 |
| D23 | iOS 越狱检测误报率的任何测量（含正常设备被判越狱的比例） | 端侧降级策略的用户代价量化 | 第 14 章、20.3.2、21.4 表 #7 |
| D24 | App Attest / DeviceCheck 的独立弱点或绕过研究（同行评审零篇） | 本篇不能假装知道绕过成本 | 第 14、17 章、18.3、21.4 表 #8 |
| D25 | ESORICS 2025 workshop 的样本量与结论数字 | rootless 时代检测有效性无量化依据 | 第 14 章、附录 C |
| D26 | iOSModZoo（WiSec 2026）与 Iranian iOS App Stores（IMC 2026）的全文与数字 | 第三方市场结构只能作存在性引用 | 第 16 章、附录 C |
| D27 | 把不同检测信号按「易伪造程度」量化排序的公开研究 | 本篇只能给条件式排序 | 18.7、20.1、21.4 表 #10 |
| D28 | 重签名包/改造包检出率与误报率的第三方独立评测 | 商业产品能力不可验证 | 第 14、17 章、20.5、21.4 表 #11 |
| D29 | Apple 公布的企业证书吊销数量/年度统计（Apple 不发此类公告） | 生态侧控制强度不可见 | 第 15、16 章、18.4、21.4 表 #6 |
| D30 | 2019 WhatsApp/Pegasus 与 2020 Zoom 两案的可直接引用原文页面与精确日期段落 | 案例只能作事件级引用 | 第 15 章、附录 C |
| D31 | 以 iOS 为对象的设备农场/群控大规模测量研究 | R3 行的生态侧判定无依据 | 第 16、17 章、18.4 |
| D32 | iOS 模拟器/iOS 云手机被滥用的任何一手或权威二级证据；中文「iOS 云手机」表述全为营销/博客 | 该风险不可定量，也不可否认 | 第 17 章、18.4、21.4 表 #3 |
| D33 | USENIX Security '26 之外、针对 2024–2026 年 Apple 处置力度的更新口径（白皮书数据止于 2020 年） | 处置趋势不可判断 | 第 16 章、18.4、21.2 |
| D34 | Apple 侧对「一台被攻陷设备为大量订阅者供给断言」的公开案例或测量 | 该形态的实际普遍性未知 | 第 8、12 章、18.4 |
| D35 | Managed Device Attestation 在真实企业环境中的失效率/误报率数据 | 企业侧无法据公开材料设定基线 | 第 9 章、20.4 |
| D36 | 平台安全指南未来是否会新增 DeviceCheck/App Attest 章节（本版零命中） | 影响「Apple 未把它当平台安全机制论述」这一判读能否延续 | 第 4、7 章、20.5.2 |
| D37 | App Store Review Guidelines 各条目的修订历史与现行版本的条款稳定性 | 政策引用的时效性（本篇要求注明「以抓取当日版本为准」） | 第 11 章、20.3.3 |

**结尾声明：若读者取到官方原文或新研究，应以其为准修订本表，而不是沿用本报告的封顶判定。** 本表的设计用途是接入内部风险登记册，把每一项标注为「待外部证据」而非「已闭合」；本篇任何「证据不足」格子一旦被外部证据填充，第 18 章的判定值与第 21.4 节的清单都应同步重算。

## 附录E 误解校正汇总

本表合并两份简报的「被推翻/需修正的预期说法」。依据层级列同时给出结论性质：**推翻**（原说法与一手材料冲突）、**修正**（方向成立但口径需改）、**封顶**（本次未取证，只能停在未知）。

| # | 常见说法 | 正确口径 | 依据层级 | 正文所在章 |
| --- | --- | --- | --- | --- |
| 1 | DeviceCheck 的两个 bit 不可重置/设备无法清除 | bit 由开发者服务端改写，重置时机明确归开发者（"You’re also responsible for determining when to reset the bits…"）；Apple 无「设备无法清除」表述 | 推翻（等级 1 + 等级 6 未取得） | 第 5 章、18.1 |
| 2 | 「不可重置」是 bit 的属性 | 唯一带 "can’t reset" 措辞的对象是**指标**："The App Attest service records device metrics that you can’t reset." 两个对象不得混写 | 修正（等级 1） | 第 5、8 章 |
| 3 | DeviceCheck 文档规定不得用于设备指纹/追踪 | 该限制不在 DeviceCheck 文档（四页全文无 fingerprint/tracking 字样）；政策侧硬依据只有 5.1.2(iii) 与 5.1.2(i) | 推翻（等级 1 负面结论） | 第 5、11 章 |
| 4 | Apple 禁止设备指纹 | App Store Review Guidelines 全文（240,818 字节）`fingerprint` 出现 **0 次**；只能写 5.1.2(iii) 的「不得识别匿名用户或重建画像」口径 | 推翻（等级 1） | 第 11 章、20.3.3 |
| 5 | Apple 明令禁止 App 读取序列号 | 正确口径是「公开 SDK 未暴露该能力」；序列号获取只出现在 MDM（需 access right）与用户自助查询页 | 修正（等级 1 清单 + 等级 6 未取得禁令原文） | 第 11 章 |
| 6 | App Attest 是 DeviceCheck 的演进/替代品 | 两者同属 DeviceCheck 框架、并存互补（门槛 iOS 11 vs iOS 14），Apple 无「取代/优先选用其一」叙述；两者均未被标记 deprecated | 推翻（等级 1，含两项负面结论） | 第 3、6 章、18.1 |
| 7 | Apple 不提供任何设备风险信号 | Apple 提供**一个**指标：近 30 天该设备 attested key 的近似计数；同时明文不提供阈值与处置 | 修正（等级 1） | 第 8 章、19.1 |
| 8 | risk metric 可识别被攻陷设备 | Apple 措辞为 approximate / "might be an indication"；重装、备份恢复、转让均会正当推高，需开发者自调阈值 | 修正（等级 1） | 第 8 章、20.2.3 |
| 9 | Apple 提供越狱检测/风险评分 API | 不存在该类 API；公开成员清单仅 `current`/`shared`、`isSupported`、`generateToken`、`generateKey`、`attestKey`、`generateAssertion`、`DCError` | 推翻（等级 1 否定结论） | 第 7、8 章 |
| 10 | MDM 协议里应有越狱/完整性字段 | `QueryResponses` 95 个键名无任何 jailbreak/root/Cydia/integrity/trust-level/risk 字段；真实完整性信号是 `DevicePropertiesAttestation` | 推翻（等级 1 负面结论） | 第 10 章、20.4.2 |
| 11 | 受管 iOS 设备可报告 SIP/Secure Boot/kext 状态 | 这三项 OID 仅 macOS 14.2 引入；`SystemIntegrityProtectionEnabled` 亦仅 macOS；iOS 可 attestate 的是序列号/UDID/sepOS/OS 版本/LLB/freshness | 推翻（等级 1） | 第 9、10 章、20.4.3 |
| 12 | 官方场景词包含 bot 与模拟器 | Apple 场景词为 game cheats / ad removal / access to premium content / promotional offer / fraudulent；`bot`/`automation`/`emulator`/`device farm`/`jailbreak`/`simulator` 全部零命中 | 修正（等级 1 负面结论） | 第 12 章、18.4 |
| 13 | Apple 平台安全指南论述了 App Attest | 该指南（August 2026）全文 grep DeviceCheck/App Attest **零命中**；可引的只有 SE 与代码签名的通用论述 | 推翻（等级 1 负面结论） | 第 4、7 章、20.5.2 |
| 14 | 越狱形态有一套标准分类，含 corebaseless | `corebaseless` 在社区文档、百科与论文中均无出处；可核实的社区术语只有 tethered 系列与 rootful/rootless（等级 5），学术口径改用「通道 + 机制 + 有效期」表（等级 2） | 推翻（等级 6） | 第 13 章、附录 A |
| 15 | A12 以上绝无可用越狱，灰产已无 iOS 真机改造面 | 未取得一手或学术对该芯片/版本边界的表述；可写的只有「公开可用绕过通道存在明确的版本/机型上界」（等级 2 三条间接依据合并） | 封顶（禁止绝对断言） | 第 13 章、18.3 |
| 16 | 越狱检测误报很多（正常设备被判越狱） | 误报被公开承认为工程事实（等级 2 规范转述 + 等级 3 厂商），但 iOS 侧发生率**无任何可核实测量**；不得引用任何误报率数字 | 推翻（就「大量」而言，等级 6） | 第 14 章、20.3.2 |
| 17 | 研究表明检测库普遍可被绕过 | 论文测的是「应用内实现与其运行时行为」，不是商业检测库产品；「普遍可绕」是 Kellner 2019 在其 hook 能力条件下的结论，HALY 2025 给出静态≠动态的口径修正 | 修正（等级 2，结论方向成立） | 第 14 章、18.3 |
| 18 | 59% 与 96.7% 可以取平均或作趋势 | 两个数字来自不同样本池与不同判据（静态字符串 vs 运行时执行），不可合并、不可相加；HALY 类别计数加总本就 ≠ 100% | 修正（等级 2） | 第 14 章、19.4 |
| 19 | Apple 会发布企业证书吊销公告与吊销统计 | 「机制」有一手文档（OCSP 首启动校验、缓存 3–7 天、吊销连坐全部应用）；「公告与量化统计」不存在——Apple 以规则与准入审查表述 | 一半成立、一半推翻（等级 1 + 等级 6） | 第 15 章、20.4 |
| 20 | 证书到期与被吊销是同一回事 | 已签名应用不受发行证书**到期**影响；被**吊销**才导致无法启动。两者不得混写 | 修正（等级 1） | 第 15 章、附录 F |
| 21 | Apple 吊销是即时生效的 | 缓存窗口 3–7 天（Apple 明文），加上「网络侧无法回连 Apple」这一研究记述的失效条件，吊销存在两个已公开的非即时条件 | 修正（等级 1 + 等级 2） | 第 15 章 |
| 22 | 灰产规模数字可直接引用（3,359 站点、900%–3,000% 等） | 全部为**研究观测下界**：地理聚焦中国、仅覆盖 Ad Hoc 自助签名通道、敏感清单不公开第三方不可复核 | 修正（等级 2 口径三件套） | 第 16 章、19.5 |
| 23 | Apple 白皮书数字说明当前滥用规模 | 数字为 Apple 单方自述、口径 2020 年、代表**处置行为计数**而非滥用发生量，且未取得同口径后续版本 | 修正（等级 1 + 口径限定） | 第 16 章、19.5 |
| 24 | 越狱设备数以千万计，属大众现象 | 「3,000 万+」是 Kellner 转引服务方自述（2018 口径、无方法学），不可用于事实陈述；但「小众」论断同样被该论文摘要口径（"tremendous popularity among regular users"）推翻 | 封顶（两侧均不可断言） | 第 13、16 章 |
| 25 | iOS 侧灰产证据少，说明 iOS 更安全 | 证据密度不对称源于可观测性与工具链限制（闭源虚拟化、越狱覆盖、商店审核压制样本），**不得据此推定风险更低** | 推翻（等级 2 口径声明） | 第 17 章、18.4、19.5 |
| 26 | iOS 重签名威胁不严重 | 正确口径是「iOS 侧重签名检测的公开研究密度显著低于 Android」；Android 侧体系化文献充分，iOS 侧未获得以重签名包为独立对象的同行评审论文 | 修正（等级 2 + 等级 6） | 第 14、17 章 |
| 27 | 移动黑产市场规模 XX 亿（白皮书数字） | 凡仅有营销报告支撑的规模数字，一律「不可用于事实陈述」，只可作趋势参照并写明发布方商业立场 | 封顶（等级 3 上限） | 第 16 章、20.5.3 |
| 28 | 「SE 密钥不可读写」=「越狱也无法伪造断言」 | 两句必须并置：Apple 一手同时写 "no process can ever directly read or modify it" 与 "an attacker that modifies the device’s operating system might bypass restrictions"；证明对象是密钥与 App 实例，不是宿主 | 修正（等级 1 并置） | 第 7、12 章、19.3 |
| 29 | Apple 已把模拟器/虚拟环境排除在 App Attest 之外 | Apple 未公开模拟器/虚拟化下的机制行为（文档无相关字样）；「未公开」既不是「已阻断」也不是「可绕过」 | 封顶（等级 6） | 第 7、17 章、18.4 |
| 30 | 检测信号可按易伪造程度排成等级表 | 本次未发现任何同行评审研究给出误报率或可伪造成本的量化排序；只允许「定性分层 + 逐条挂来源」，量化列一律标未公开；**不得自制排序表冒充事实** | 封顶（等级 6 + 等级 2 定性判据） | 18.7、20.1、21.4 |

## 附录F 术语口径冲突与系列内分工对照

### 附录F.1 两份简报记录的术语与口径冲突

下表合并机制面简报 F 节 11 条与对抗面简报 F 节 5 组冲突，并补入本次核查发现的**标度冲突**。**处置方式**一列说明本篇采取的是「并列保留」还是「择一采纳」——凡 Apple 自身口径不一致者，本篇一律并列，不代其消解。

| 冲突项 | 两种（或多种）口径 | 处置方式 | 正文所在 |
| --- | --- | --- | --- |
| attestation / assertion / claim | Apple 三分：attestation（证明密钥来源）、assertion（一次使用的合法性）、asserting app integrity（框架 abstract 的动词用法）；中文社区混译为「设备证明/应用证明/断言/声明/凭据」；`claim` 多来自 WebAuthn，Apple 文档本身不用该词 | 择一采纳并固定：attestation=证明、assertion=断言，首次出现附英文 | 第 6 章、附录 A |
| 「bit」的叫法 | 框架页 "two binary digits of data per device"；`DCDevice` "two per-device binary digits (bits)"；服务端 "persistent bits" + JSON `bit0`/`bit1`；响应说明写作 "the first bit"/"the second bit"；第三方常写 `bit1/bit2`、`deviceCheckBit1/2` | 择一：统一用 `bit0`/`bit1` 官方字段名；第三方写法标注为不符官方字段名 | 第 5 章、附录 A |
| `seKey` | Apple 文档零命中；社区用它指 SE 私钥句柄，另有无关的 `sekey` SSH 项目与学术 SEKEY 密钥管理方案 | 并列 + 强制标注：使用即须标「非 Apple 文档术语」 | 第 6 章、附录 A |
| `isSupported`（属性 vs 方法） | 文档标题区为属性（`var isSupported: Bool` / ObjC `supported` getter），但 Discussion 与多篇指南出现 "reading the property" 与 "the `generateKey` method" 混排，Apple 原文甚至写作 "the `isSupported` method property"；二手文章常误写为 `isSupported()` | 择一：一律写「只读属性」；Apple 自身措辞瑕疵并列注明 | 第 5、7 章、附录 A |
| `isSupported`（扩展支持范围，**Apple 文档内部矛盾**） | `establishing-your-app-s-integrity`：Action、extensible SSO、watchOS 扩展受支持，其余类型即使该值为真也不支持；`DCAppAttestService.isSupported` 页：`generateKey` 在任意 app extension 中一律失败，仅 watchOS 9+ 扩展支持 | **并列保留，不择一**；工程上按最保守口径设计 | 第 7 章、20.3.1、21.2 |
| `RP ID` / `rpIdHash` | Apple 写作 `RP ID`（含空格）并释为 "A hash of your app’s App ID"；WebAuthn 标准术语为 `rpIdHash`，第三方校验库多用后者 | 并列：同一字段两名同时给出 | 第 6 章、附录 A |
| 环境/新鲜度标记 | App Attest 用 `aaguid`（`appattestdevelop` / `appattest`+7×`0x00`）；entitlement 与沙箱叙述用 `development`/`production`；MDA 完全没有环境概念，用 `DeviceAttestationNonce` freshness code 表新鲜度 | 择一并强制分立：三套术语不可互换、不可互引证据 | 第 6、9 章、附录 A |
| 「设备真实性」这一中文标题 | Apple 原文只用 authenticity / validity / legitimacy / integrity 四词，**无 "device authenticity" 这一固定术语** | 并列映射：中文标题须映射到四词并加「以 App/密钥真实性为代理指标」限定 | 第 2、18 章、附录 A |
| 风险指标的三个名字 | `risk metric`（receipt 字段 17 名）、`fraud metric`、`fraud assessment metric`（`validating-apps…`），以及 "an approximate count of unique attestations…" 的描述语 | 择一：首次出现统一为 risk metric（风险指标）并附其余两名 | 第 8 章、附录 A |
| Managed Device Attestation vs App Attest | 前者属 MDM/ACME 生态（`device-attest-01`、Apple Enterprise Attestation Root CA），后者属 DeviceCheck 框架（App Attest root）；社区常统称「苹果设备证明」 | 择一并强制分立：两个机制，不得合并叙述或互引证据 | 第 6、9 章、附录 A |
| 「越狱」一词的来源层 | 社区/百科（等级 5）用 tethered 系列与 rootful/rootless；学术（等级 2）按通道+机制+有效期分表，HALY 以 root/jailbreak detection 统称并称两者界限模糊；**Apple 平台安全指南与 DeviceCheck 文档 `jailbreak` 均零命中**，只用 untrusted code / unsigned apps / compromised | 并列 + 首次出现处声明：本篇使用「越狱」指称社区与论文用法，Apple 文档不使用该词 | 第 13 章、18.1、附录 A |
| 越狱 ≠ 解锁 ≠ 魔改 | unlocked 在中文报道里既指运营商锁也指 Android bootloader 解锁；modified 被论文用于「改造版应用」；三者常被混用 | 择一并定义：三者分列，不得互相替代 | 第 13 章 |
| 设备农场 / 群控 / 云手机 / 云真机 | 厂商风控（等级 3）：device farm = 广告欺诈基础设施；中文灰产（等级 5）：群控 = 成批真机的集中控制；测试产业（等级 3）：real device cloud / 云真机 = 合法测试服务；学术（等级 2）：设备农场研究以 Android 为主 | 择一分三层：以牟意为目标的机群 / 以测试为卖点的设备云 / 以虚拟化为核心的研究平台，**互不指代**；iOS 侧仅后两类有可核依据 | 第 16、17 章、附录 A |
| 重签名 / 二次打包 / repackaging / 改造版 | Android 文献主用 repackaging（AppInk、DroidMarking、ACSAC、DSN、VAhunt 的「应用虚拟化外壳」）；iOS 文献用 modified apps + dynamic library injection；HALY 用 re-signed；厂商把上述统一叫 repackaging/re-signing detection | 择一并限定：定义「重签名=以另一有效证书重签并分发」「二次打包=包内容改动后重打包」，本篇沿用「改造版应用」口径，**不与 Android 指标互相换算** | 第 14 章、附录 A |
| 检出率的同指标多值 | Kellner 59%（3,482 应用）vs HALY 静态 96.7%/动态 24.9%（2,646 应用池内 iOS 侧）；Android 侧 Evans 2015 → Kellner 2019 → Ruggia 2024（61%）→ HALY 2025（92.3%）为四个不同样本与四种判据 | 并列保留：跨研究比较必须逐条标注样本池/判据/年份，**不可排成趋势线** | 第 14 章、19.4 |
| 吊销 vs 到期 | 部署指南：吊销发行证书 → 以其签名的全部应用失效、首启动 OCSP 校验不通过则不启动；同页另述：已签名应用**不受发行证书到期影响**（到期与吊销是两件事） | 择一分列：两机制分开表述，不得混写 | 第 15 章、附录 E |
| Apple 白皮书发布日期 | 本地 PDF 封面 June 2021；BRUEGEL 工作论文与 ACCC 提交材料引为 October 2021；系列内 `iOS供应链安全分析报告` 4.4 误作 2024 年 | 并列：取「2021 年（存在 June 封面版与 October 引用版两种口径）」并附来源，同时更正该处日期 | 第 15、16 章 |
| 2019 年企业证书事件 | Ars Technica（2019-01-30，吊销 Facebook）与 Forbes（2019-02-01，阻断 Google 后**已恢复**）口径不同；中文二手常写成「Google 2019/2020 年被吊销」 | 并列双述，不合并 | 第 15 章、附录 B.2 |
| Kellner 论文内部计数 | 导言「15 out of 34 完全不使用该措施」vs 结论「only 18 out of 34 使用越狱检测」，相加为 33 而非 34 | 并列：同时给出两处原文，或只引摘要口径「44% 未尝试检测」 | 第 14 章、19.4 |
| **两份简报对「L2」的定义不一致**（本次核查补充发现） | 机制面简报：L1 = developer.apple.com 文档接口与平台安全指南本地副本，**L2 = App Store Review Guidelines、MDM Protocol Reference 等「一手但间接」材料**，L3 = 第三方；对抗面简报：**等级 2 = 同行评审学术/会议论文与学位论文**，等级 1 才对应 Apple 一手 | 并列 + 折算规则：本篇统一采用六级体系，机制面 L1/L2 内容折算为等级 1，其「未核实（公开资料未披露）」折算为等级 6 | 18.2、第 18 章口径边界 |

### 附录F.2 与系列已有篇目的分工声明

本篇在系列中的独占范围是：**Apple 官方提供的服务端可验证真实性信号（DeviceCheck、App Attest、Managed Device Attestation）的机制语义、能力边界、失效前提与证据分级**。以下逐一点名可能撞车的既有篇目，并声明本篇**不重讲**什么。

| 既有篇目（其相关章） | 该篇已覆盖 | 本篇显式让出的部分 | 本篇独占的部分 |
| --- | --- | --- | --- |
| 《iOS 系统防御与机制绕过》（第十一章 越狱防御与绕过，含原理/主要越狱工具/越狱检测技术/反越狱检测；3.5 代码签名绕过） | 宿主侧防线的机制描述与绕过现象、检测点位清单 | **不重述任何检测点位清单、不重列越狱工具、不写任何绕过手法** | 这些机制**有效性**的公开实测结论（Kellner 34 银行应用仅 1 例幸免、HALY 静态 96.7% vs 动态 24.9%）与 Apple 一手承认的上限句，据此给出「哪些判定必须上移服务端」的依据 |
| 《iOS 典型漏洞攻击链安全分析报告》（第 5 章 越狱攻击链分析：checkm8/2019–2021、unc0ver/2018–2020、Taurine/2021） | 逐条越狱工具的攻击链与 CVE 组合 | **不重述任何越狱链的技术构成与版本细节** | 「时代口径 + 通道分类表」（USENIX '26 Table 2，等级 2）与其对检测信号可靠性的影响 |
| 《iOS 开发与技术栈》（12.6 越狱检测；第十四章 14.3 企业分发 / 14.4 Ad Hoc / 14.6 应用签名 / 14.7 证书管理） | 工程实现视角的 SDK/API 写法与清单 | **不写 SDK/API 实现步骤与工程清单** | 规则与责任层口径：ADEP 准入、OCSP 吊销生效窗口、attestation 校验项的责任归属 |
| 《著名 iOS 漏洞案例》（第二章 越狱时代的里程碑） | 案例叙事史与编年 | **不重复案例编年，不重述案情** | 越狱时代口径的**证据学**解释：为何 iOS 侧对抗面证据稀疏（工具链、越狱覆盖、商店审核三重限制） |
| 《iOS 供应链安全分析报告》（4.1 企业证书滥用、4.3 第三方应用商店与灰色市场、4.4 侧载风险） | 证书滥用谱系表、DMA 与第三方商店、侧载风险 | **不重述企业证书滥用的通用表**；本篇 4.4 相关的白皮书日期按其口径更正为 2021 年 | Apple 一手处置机制（OCSP/缓存/连坐）+ USENIX '26 同行评审测量数据（3,359 站点、8,216 IPA、28 个证书站点、900%–3,000%、UDID 配额资产化） |
| 《iOS 身份认证与通道加密》（第 2 章「设备级身份认证」、第 4 章应用层认证机制）——**最高风险撞车，此处显式切开** | 设备级身份认证的既有论述、TLS/证书链与 Apple ID 认证 | 本篇**不讲** TLS 与证书通道加密、不讲 Apple ID 认证流程、不重讲该章的认证协议层 | 本篇的「设备真实性」**仅指** DeviceCheck / App Attest / Managed Device Attestation 三条 Apple 官方服务端可验证通道；凡协议层与账号认证，一律归该篇第 2、4、5、8 章。两篇的「设备级身份认证」一词在本系列中按此分工理解 |
| 《iOS 钥匙串窃取》（第 6 章 Secure Enclave 与密钥管理，另含第 2–9 章相关） | SE 内部密钥层级、Keychain 数据模型与窃取路径 | **不展开 SE 结构与内部密钥层级，不写任何窃取路径** | 引用 SE 仅为说明「App Attest 私钥驻留位置与不可读改」这一手陈述，以及它与「宿主未越狱」之间的判定空隙 |
| 《iOS 核心保护作用》（第 1.6 节十三类威胁总览、2.1 七要素复用模式、类别一至八、第 16 章最终矩阵） | 「威胁 → 保护层」映射与 Apple 机制定位；最终矩阵的汇总与打分 | **不另建一套威胁-保护映射、不重复输出打分矩阵**（第 18 章只给判定值计数，不给分数） | 该框架下「设备真实性判定」这一横切层的证据与责任分配；「不可信设备」作为跨类别前提的展开 |
| 《iOS 内核安全机制深度解析》（代码签名强制、第 15 章研究设备与 kext/DriverKit 字段可观察性） | 代码签名在内核里如何被强制、研究设备与字段可观察性口径 | **不讲内核层签名强制机制**；kext/DriverKit 与 macOS 完整性叙述仅在与 MDA 字段对比时一句带过 | `validationCategory` 作为 App Attest 证明里的一个可校验字段来引用；并复用该篇已确立的「源码可观察 ≠ Apple 承诺」口径 |
| 《iOS 数据采集能力深度分析》（设备数据提取、8.1 检测方法） | 数据类别清单、采集能力矩阵、隐私标签、字段清单 | **不写采集能力清单与字段矩阵** | 标识符的官方语义与失效条件（IDFA 全零清单、IDFV 的 `nil` 与重装变化）、政策条款口径，以及「真实性信号被伪造后对采集合规判断的影响」这一句结论 |
| 《iOS 智能识别》（端侧模型与识别栈：Core ML、Vision、Apple Intelligence） | 端侧识别能力 | **不涉及任何端侧 ML 识别** | 本篇的「识别」专指平台侧真实性判定信号；此语义撞车已在范围声明与本章一并排除 |
| 《iOS 访问控制与应用层交互限制》（第 6 章 TCC、第 10 章攻击面与绕过） | TCC 授权模型、Profile 限制与攻击面 | **不讲 TCC 授权模型**；配置文件对 advertising identifier 的限制只在 IDFA 语义里提一句 | 标识符被 ATT 与全零条件约束后，能否作为真实性判据的结论 |
| 《iOS 核心底层技术支撑》（第 1 章架构分层与运行时、第 5 章起安全机制） | SE、签名与运行时的底层原理 | **不重述底层硬件与运行时原理**，需要时以「详见该篇」一句引用 | 面向服务端的能力接口与其语义边界 |
| 《iOS 失窃设备保护》 | Activation Lock、丢失模式、EACS 等功能讲解 | **不重复功能讲解** | 相关字段（`IsActivationLockEnabled`、`IsMDMLostModeEnabled`、`EACSPreflight`）仅作为「MDM 可见字段清单」的证据使用 |
| 《iOS 商业间谍软件与监控技术》（第七/九章反检测、第十章间谍软件检测含 10.9 局限） | 国家级监控软件与取证式检测困境 | **不混入国家级监控叙事的重复论证** | 非国家级、规模化、以牟利为目标的对抗面，以及极客侧检测失效的证据分级 |

**附录口径边界**：本附录各表为**汇总层产物**，不引入任何简报外事实；表中「章号回指」指向本篇第 1–21 章已有陈述，若某格在前文找不到对应依据，则该格属本报告自拟的整理动作，已按术语表「层」列标注为「本报告自拟」。术语表的译法固定、冲突的并列保留、缺口的封顶表述三项处置规则在附录 A、D、F 之间保持一致；三处 Apple 文档内部冲突（扩展支持范围、`isSupported` 的 method/property 措辞、"can’t reset" 的主语）均按「不代 Apple 消解」原则处理。若后续版本取得官方澄清或新的同行评审研究，应以其为准修订本附录各表，而非沿用本篇的封顶判定。

---
**联系方式**：Telegram: https://t.me/ios_one001
