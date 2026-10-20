@@TITLE@@iOS网页零日漏洞分析报告
@@SUBTITLE@@——WebKit/Safari 在野零日漏洞的产业现状、技术类别与防御策略
@@INFO@@报告主题 | iOS 平台 WebKit/Safari 在野零日漏洞综合分析
@@INFO@@编制日期 | 2026年10月5日
@@INFO@@资料来源 | Apple 安全通告、NVD、Google Project Zero/TAG、Citizen Lab、Kaspersky
@@INFO@@适用对象 | 企业安全团队、移动安全工程师、风险管理与研究人员
@@INFO@@文件性质 | 基于公开资料的防御性安全分析，不包含任何攻击工具或利用代码
@@TOC@@目录

# 一、概述

iOS 平台的"网页零日漏洞"（Web Zero-day）指 Safari 浏览器及其底层 WebKit 渲染引擎中、被攻击者在厂商修复前已投入在野利用的漏洞。它们之所以成为高价值攻击武器，根源在于两点：**攻击面大**——任何 iOS 用户点开一个网页就可能触发；**位置关键**——WebKit 既是 Safari 的核心，也被 iOS 系统内嵌于 WKWebView、SFSafariViewController 等组件，第三方浏览器（Chrome、Firefox 等）在 iOS 上同样强制使用 WebKit，因此一个 WebKit 零日漏洞往往覆盖整个 iOS 生态。

从 2019 年针对维吾尔语网站的水坑攻击、2021 年 Pegasus 对香港活动人士的投递，到 2023 年针对卡巴斯基员工的"三角测量行动"、2024 年针对 Mac 用户的 TAG 披露，再到 2025 年 iOS 18.3.2 紧急修复的 CVE-2025-24201——WebKit 零日已成为高端定向攻击与商业间谍软件（Pegasus/Predator）投递链条的标准入口。

本报告基于 Apple 官方安全通告、NVD、Google Project Zero/TAG、Citizen Lab、Kaspersky 等公开权威来源，系统梳理 2019 年至 2026 年初 iOS 平台在野利用的 WebKit/Safari 零日漏洞：技术类别分布、典型 CVE 案例、在攻击链中的位置、修复节奏，并给出面向个人、企业与开发者的防御建议。全部内容仅用于防御与研究，不含任何攻击工具、利用代码或操作指引。

## 1.1 本报告要点

- **数量持续走高**：2021 年起 Apple 平台在野利用的 WebKit 零日每年均达两位数级别披露，2023 年尤为密集（覆盖 CVE-2023-23529/28205/32409/32435/37450 等）。
- **类型高度集中**：WebKit 零日集中在 **类型混淆（Type Confusion）**、**释放后重用（UAF）**、**越界读写（OOB）** 三类内存安全缺陷，与 JIT 优化、DOM/JSC 对象生命周期管理的复杂度高度相关。
- **攻击链位置**：几乎全部 WebKit 零日作为"入口漏洞"存在——通过访问恶意网页或点开 iMessage 链接触发沙箱内代码执行，随后与内核/字体/dyld 漏洞串联完成提权与植入。
- **发现主力**：Google TAG + Project Zero、Citizen Lab 以及 Kaspersky GReAT 是过去三年披露 iOS WebKit 零日最多的三支团队，发现背景多数指向针对记者、异见者、企业高管的商业/国家级间谍软件。

# 二、WebKit 在 iOS 攻击面中的位置

## 2.1 WebKit 的作用

WebKit 是 Safari 的渲染引擎（含 HTML 解析器、CSS 引擎、JavaScriptCore JIT 等组件），同时也通过 WKWebView / SFSafariViewController 向 App 开放，供第三方应用内嵌浏览能力。按 Apple 政策，iOS 上任何浏览器引擎都必须基于 WebKit，这意味着：

- 一个 WebKit 零日可以让所有 iOS 浏览器（Safari、Chrome、Firefox、Edge、Brave 等）、所有内置 WebView 的 App（邮件、聊天、社交、新闻等）同时沦陷。
- 用户访问任意网页、打开 iMessage 预览、点开邮件中的链接，都可能触发同一个 WebKit 漏洞。
- 攻击者只需绕过沙箱即可获得浏览器进程权限，加上内核/字体/MMIO 等提权链，就能完成整机沦陷。

## 2.2 典型攻击链形态

iOS WebKit 零日通常作为"远程代码执行入口"存在于更复杂的攻击链中：

| 链路阶段 | 常见漏洞类别 | 代表性 CVE |
| --- | --- | --- |
| 1. 投递 | iMessage/邮件/网页链接；水坑注入 | 配合 FontParser (CVE-2023-41990)、PDF/.watchface 容器 |
| 2. 入口 RCE | WebKit 类型混淆/UAF/OOB | CVE-2019-8605、CVE-2021-30858、CVE-2023-23529、CVE-2024-23222 |
| 3. 沙箱逃逸 | WebKit WebContent Sandbox 越权 | CVE-2023-32409、CVE-2025-24201 |
| 4. 内核提权 | XNU UAF/OOB、MMIO 保护绕过 | CVE-2019-7287、CVE-2021-30869、CVE-2023-32434、CVE-2023-38606 |
| 5. 持久化/回传 | 植入内存/磁盘监控组件 | 不属于单一 CVE |

## 2.3 为什么 WebKit 是零日高发区

- **代码体量巨大**：WebKit 代码行数以百万级计，涵盖 HTML/CSS/JS/WebAssembly/WebRTC 等多个子引擎，攻击面广。
- **JIT 等高性能特性**：JavaScriptCore 的 FTL/DFG JIT 在类型推断上的激进优化历来是类型混淆的温床（如 CVE-2024-23222、CVE-2023-23529）。
- **多进程 + 大量 IPC**：WebContent / Networking / GPU 等分进程架构带来频繁 IPC，释放后重用与状态不一致漏洞不断出现。
- **零成本投放**：相比 iMessage 零点击，WebKit 利用的构造与投放成本更低——只需控制一个 URL 或往目标发一个链接即可触发。

# 三、典型 CVE 案例回顾（2019–2026）

> 以下 CVE 信息均来自 Apple 官方安全通告与 NVD/CISA KEV，所有"actively exploited"措辞均经公开资料核实。

## 3.1 CVE-2019-8605：水坑投递中的 WebKit 内存破坏（紧急修复）

- **类型**：WebKit 内存破坏，可致任意代码执行
- **修复版本**：iOS 12.4.1（2019 年 8 月紧急更新，同批修复另一枚 WebKit 缺陷 CVE-2019-8675）
- **背景**：Apple 在通告中确认该问题可能已用于针对特定个人的定向攻击；公开报道（Lookout、Citizen Lab）将其与水坑式 Pegasus 投递相关联。2020 年 Trend Micro"毒新闻行动"再次复用该漏洞作为 N-day，针对香港新闻网站访客投放 lightSpy 植入体。
- **意义**：Apple 首次在 WebKit 内存破坏问题上以非计划紧急补丁响应，说明网页端零日对 iOS 生态的现实威胁已形成制度化处置。

## 3.2 CVE-2021-30858：Pegasus 的 Safari 入口

- **类型**：WebKit UAF
- **修复版本**：iOS 14.8（2021 年 9 月 13 日）
- **背景**：Citizen Lab 在追踪 Pegasus 商业间谍软件时发现，NSO Group 使用该漏洞对活动人士发动攻击；Apple 同日修复另一枚 ImageIO 侧的 CVE-2021-30860（FORCEDENTRY 零点击 iMessage 入口）。
- **意义**：代表商业间谍软件对 WebKit 零日的标准化消费——"浏览器漏洞 + iMessage 投递"成为 Pegasus 系列的典型组合。

## 3.3 CVE-2022-22620：Safari 15 的 UAF

- **类型**：WebKit UAF
- **修复版本**：iOS 15.3.1、iPadOS 15.3.1、macOS 12.2.1、Safari 15.3（2022 年 2 月 10 日）
- **背景**：Apple 通告称"may have been actively exploited"，由匿名研究者报告。该漏洞被认为曾被用于定向攻击。

## 3.4 CVE-2022-32893：Safari 15.6 的 OOB 写

- **类型**：WebKit 越界写
- **修复版本**：iOS 15.6.1、macOS 12.5.1、Safari 15.6.1（2022 年 8 月 17 日）
- **背景**：与同日修复的 Kernel OOB 写 CVE-2022-32894 配对，构成一条浏览器 RCE + 内核提权的完整在野链；通告措辞为 "actively exploited"。

## 3.5 CVE-2023-23529：iOS 16.3.1 的类型混淆

- **类型**：WebKit 类型混淆（JIT）
- **修复版本**：iOS 16.3.1、macOS 13.2.1、Safari 16.3.1（2023 年 2 月 13 日）
- **背景**：Apple 通告确认"actively exploited"；安全社区认为该漏洞主要用于高定向攻击。

## 3.6 CVE-2023-28205：iOS 16.4.1 的 WebKit UAF

- **类型**：WebKit UAF
- **修复版本**：iOS 16.4.1、Safari 16.4.1（2023 年 4 月 7 日）
- **背景**：与同日修复的 IOSurfaceAccelerator UAF（CVE-2023-28206）构成两步在野链；由 Google TAG 与 Amnesty International 安全实验室联合披露。

## 3.7 CVE-2023-32409：WebKit WebContent 沙箱逃逸

- **类型**：WebKit WebContent 沙箱越权
- **修复版本**：iOS 16.5、Safari 16.5（2023 年 5 月 18 日）
- **背景**：由 Clément Lecigne（Google TAG）与 Donncha Ó Cearbhaill（Citizen Lab）联合披露；通告确认"actively exploited"。首次把"WebKit 沙箱逃逸"作为独立组件列为在野零日。

## 3.8 CVE-2023-37450：Rapid Security Response 中的 WebKit 代码执行

- **类型**：WebKit 任意代码执行
- **修复版本**：iOS 16.5.1(a)（2023 年 7 月 10 日的首个 Rapid Security Response）
- **背景**：Apple 为该漏洞启用了 iOS 16 引入的"快速安全响应"（RSR）机制，不等月度更新即单独发布补丁。通告措辞"actively exploited"。标志 Apple 对高危 WebKit 漏洞的响应模式正式进入"小时级"。

## 3.9 CVE-2023-32435（三角测量行动，WebKit 侧）

- **类型**：WebKit 内存破坏（含类型混淆）
- **修复版本**：iOS 16.5.1（2023 年 6 月 21 日）
- **背景**：Kaspersky 披露"Operation Triangulation"——攻击者通过无显示的 iMessage 投递包含 .watchface 容器的 PDF，解析过程中触发 CVE-2023-41990（FontParser），随后 Safari 加载验证脚本并借助 CVE-2023-32435（WebKit）与 CVE-2023-32434/38606（内核）完成植入。目标为卡巴斯基员工 iPhone，是迄今最复杂的 iOS 零点击 + 浏览器组合链案例。

## 3.10 CVE-2024-23222：iOS 17.3 的类型混淆

- **类型**：WebKit 类型混淆
- **修复版本**：iOS 17.3 / 16.7.5、Safari 17.3（2024 年 1 月 22 日）
- **背景**：Apple 承认"may have been actively exploited"。该漏洞影响 iPhone/iPad/Mac/Apple TV/Apple Watch，是 2024 年首个在野利用的 WebKit 零日。

## 3.11 CVE-2024-44308 / 44309：11 月 iOS 18.1.1 的双 WebKit 零日

- **类型**：
  - CVE-2024-44308：JavaScriptCore 任意代码执行
  - CVE-2024-44309：WebKit cookie 处理漏洞，可致跨站脚本（XSS）
- **修复版本**：iOS 18.1.1 / 17.7.2、Safari 18.1.1、macOS 15.1.1、visionOS 2.1.1（2024 年 11 月 20 日）
- **背景**：Google TAG 的 Clément Lecigne 与 Benoît Sevens 发现；Apple 通告确认 Intel 架构 Mac 上存在在野利用。

## 3.12 CVE-2025-24201：WebContent 沙箱逃逸再度现身

- **类型**：WebKit OOB 写（允许从 WebContent 沙箱逃逸）
- **修复版本**：iOS 18.3.2、macOS 15.3.2、visionOS 2.3.2、Safari 18.3.1（2025 年 3 月 11 日）
- **背景**：Apple 通告称"used in attacks on specific targeted individuals on iOS prior to iOS 17.2"——即在旧版 iOS 上曾被用于高度定向攻击。Apple 在 iOS 17.2 中已部分缓解，本次补丁为完整修复。

# 四、技术类别统计与趋势

将上述案例按技术类别分组，可以看到 iOS WebKit 零日的两个显著特征：

| 技术类别 | 代表 CVE | 典型位置 |
| --- | --- | --- |
| 释放后重用（UAF） | 2021-30858、2022-22620、2023-28205 | DOM/JSC 对象生命周期 |
| 类型混淆 | 2019-8554、2023-23529、2024-23222、2023-32435 | JavaScriptCore JIT 与克隆/序列化路径 |
| 越界读写 | 2022-32893、2025-24201 | 字符串/缓冲区边界 |
| 沙箱逃逸 | 2023-32409、2025-24201 | WebContent 进程边界 |
| 任意 JS 执行 | 2024-44308（JSC）、2023-37450 | JSC 优化器/解释器 |
| Cookie/逻辑类 | 2024-44309 | Cookie 域处理 |

> 注：Apple 通告对部分早期案例仅表述为"处理恶意网页内容可能导致任意代码执行"，未逐条公布缺陷类别；本表按 Project Zero 等公开分析归类。CVE-2019-8605 即属"通告未定性、公开分析归入内存破坏"的案例。

**趋势判断：**

- **UAF 比例逐步下降，类型混淆/逻辑漏洞上升**。原因在于 Apple 在 WebKit 中大量部署了结构体隔离堆、PAC 等缓解措施，使传统 UAF 攻击门槛上升；但 JIT 推断依赖的类型系统仍是薄弱点。
- **沙箱逃逸成为"必选项"**。单一 WebKit RCE 已不足以实现真正的攻击，攻击者会同时准备或购买配套的沙箱逃逸漏洞（如 CVE-2023-32409、CVE-2025-24201）。
- **响应节奏加快**。iOS 16 起引入 Rapid Security Response，使 Apple 可在数小时内发布补丁（如 CVE-2023-37450）；锁定模式（Lockdown Mode）亦显著削减浏览器攻击面。
- **发现主体稳定**。过去三年披露 iOS WebKit 零日最多的是 Google TAG + Project Zero、Citizen Lab 与 Kaspersky GReAT——这与它们追踪商业间谍软件（Pegasus/Predator）和国家级 APT 的任务一致。

# 五、WebKit 零日的攻击链串联

## 5.1 投递路径

WebKit 零日最终要落到用户设备上，常见的投递路径有四种：

1. **直接发送链接**：攻击者把目标 URL 通过 iMessage、SMS、WhatsApp、Signal、邮件等渠道发给受害者，诱导点击。这是 Pegasus/Predator 等商业间谍软件的标准路径。
2. **一键跳转**：诱导受害者点击"PDF 链接""视频链接"等伪装内容，网页在跳转过程中静默触发利用。
3. **水坑注入**：攻击者先入侵受害者常访问的网站，向页面注入隐藏 iframe，访客自然访问即触发（见本系列《iOS 水坑攻击安全分析报告》）。
4. **邮件/消息内嵌预览**：某些 iOS 组件会预渲染 URL/HTML，攻击者只需让目标接收消息即可触发（典型如 FORCEDENTRY，虽为 ImageIO 侧）。

## 5.2 利用期的常见步骤

一旦 WebKit 漏洞被触发，攻击者通常按下列步骤推进：

- **Heap grooming**：通过大量 JS 对象分配、释放，控制堆内存布局，使漏洞对象处于可预测位置；
- **信息泄露**：读取内存泄露指针与地址，突破 ASLR；
- **任意读写**：构造 fakeobj/addrof 原语，获得任意读写能力；
- **沙箱内代码执行**：JIT 代码缓冲区覆盖或构造 ROP 链，获得 WebContent 进程中的代码执行；
- **沙箱逃逸**：串联 CVE-2023-32409、CVE-2025-24201 等漏洞或 IOSurface/XPC 侧漏洞，向父进程/内核传递利用负载；
- **内核提权**：触发内核 UAF/OOB，解除沙箱、关闭日志、禁用 PPL/kTRR，完成植入。

## 5.3 典型组合（公开披露）

| 组合 | WebKit 入口 | 配套漏洞 | 用途 |
| --- | --- | --- | --- |
| 2019 年初维吾尔语网站水坑 | CVE-2019-8554（JSC 类型混淆） | CVE-2019-7287/7286（内核与 IOMobileFrameBuffer） | 水坑式监控植入 |
| 2019 年 8 月水坑/Pegasus 投递 | CVE-2019-8605 | 内核提权与植入组件（公开报道） | 商业间谍软件投递 |
| 2021 Pegasus/Safari | CVE-2021-30858 | 配合 FORCEDENTRY (CVE-2021-30860) | 商业间谍软件 |
| 2023 Operation Triangulation | CVE-2023-32435 | CVE-2023-32434、CVE-2023-38606、CVE-2023-41990 | 零点击 + 浏览器组合 |
| 2024 TAG 披露 | CVE-2024-44308/44309 | 面向 Intel Mac 的在野攻击 | 定向攻击 |
| 2025 Mar | CVE-2025-24201 | 可能配套的内核或 MIG 漏洞（未公开） | 高度定向攻击 |

# 六、防御与缓解建议

## 6.1 个人用户

- **第一时间升级**：WebKit 零日被披露即意味着利用方法已被研究人员掌握，补丁延迟每多一天风险翻倍。
- **开启自动更新**：在"设置 → 通用 → 软件更新 → 自动更新"中启用"安全响应与系统文件"自动安装，可无感接收 Rapid Security Response 补丁。
- **启用锁定模式（Lockdown Mode）**：对记者、人权工作者、企业高管等高风险人群，锁定模式会禁用部分 JIT、URL 预加载与消息富预览，显著削减 WebKit 攻击面。
- **慎重对待陌生链接**：WebKit 零日仍需用户访问目标 URL，不点未知来源链接是最有效的免疫手段之一。
- **浏览器选择不影响防护**：iOS 上所有浏览器都基于 WebKit，换 Chrome / Firefox 不会绕过 WebKit 零日——保持系统最新才是关键。

## 6.2 企业与组织

- **把 iOS 升级纳入 MDM 合规基线**：对关键岗位设备强制在补丁发布后 X 小时内升级，拒绝未升级设备访问敏感资源。
- **监控威胁情报**：订阅 Apple 安全通告、CISA KEV、Google TAG/Project Zero 博客、Citizen Lab 等源，比对 CVE 并评估资产暴露。
- **部署移动端 EDR**：选择支持 iOS 的威胁检测方案（如 Lookout、iVerify、Jamf Protect），检测异常网络、描述文件安装、进程崩溃信号。
- **DNS 与出站网关过滤**：封堵已披露的恶意跳板域名、新注册域名、高风险 TLD；对高敏感岗位可部署强制 TLS 解密与 IOC 对照。
- **分级设备策略**：高管/敏感岗位使用"低权限 + 严格更新"的专用设备，日常工作设备不接触核心系统。

## 6.3 开发者（WKWebView 场景）

- **跟随系统版本**：WKWebView 继承 iOS WebKit，意味着系统升级即修复——不要阻止用户升级。
- **URL 白名单**：对应用内嵌 WebView 使用 URL 白名单或至少强制 HTTPS，禁止跳转到未审计域名。
- **最小权限**：WKWebView 配置中关闭不必要的 JavaScript、文件访问、通用链接拦截。
- **用户教育**：在应用内明示打开外部链接的风险，避免为了"沉浸式体验"用 WebView 做任意跳转。

## 6.4 检测与响应速查

| 环节 | 可观测信号 | 关键措施 |
| --- | --- | --- |
| 浏览器触发 | WebContent 进程异常崩溃、Report 日志中的 UAF 签名 | 收集崩溃报告交由安全厂商分析 |
| 沙箱逃逸 | GPU/网络进程异常、XPC 调用异常 | iOS 系统日志 + MDM 行为检测 |
| 内核提权 | 系统升级、调试、信任状态异常 | MDM 合规检查、锁定模式 |
| 植入持久化 | 新增描述文件、异常回连、电量异常 | DNS 过滤、流量侧 IOC 对照 |

# 七、趋势展望

- **Rapid Security Response 常态化**：Apple 将继续依赖 RSR 机制，缩短 WebKit 补丁从发现到到达用户设备的时间；企业 MDM 应将 RSR 纳入合规策略。
- **JIT 之痛仍将延续**：类型混淆将继续是 JavaScriptCore 的"结构性顽疾"；长期上，Apple 可能继续加强 JIT 代码缓冲区 PAC / 结构体隔离堆以缓解。
- **沙箱逃逸被单独定价**：WebContent 沙箱逃逸作为必选补齐，其黑市/漏洞赏金价格已显著上升；Apple Security Research Device Program（SRDP）与 bug bounty 的提价是对冲手段。
- **锁定模式会越来越"全能"**：Apple 在 iOS 17/18 中持续扩大 Lockdown Mode 覆盖面，预计未来会覆盖更多 WebKit JIT/Networking 边界，并进一步成为企业高风险岗位的默认推荐。
- **商业间谍软件转向混合链**：面对持续加固的浏览器，Pegasus/Predator 等正在转向"浏览器 + 消息 + 字体 + MMIO"混合链；单一 WebKit 零日的"保质期"将逐步缩短，但高价仍能被国家级客户支撑。

# 八、附录

## 8.1 术语表

| 术语 | 含义 |
| --- | --- |
| WebKit | Apple 开源浏览器引擎，iOS 上所有浏览器与 WebView 的基础 |
| JavaScriptCore (JSC) | WebKit 的 JS 引擎，含解释器与 FTL/DFG JIT |
| 类型混淆 | 对象被误以为另一种类型进行访问，引发越权内存操作 |
| UAF | Use-After-Free，释放后重用，典型内存破坏类漏洞 |
| OOB | Out-Of-Bounds，越界读/写 |
| 沙箱逃逸 | 从受限进程（如 WebContent）跳出到更高权限上下文 |
| Rapid Security Response | iOS 16+ 的小补丁快发机制 |
| Lockdown Mode | iOS 16+ 面向高风险用户的强硬化模式 |

## 8.2 主要参考资料

- Apple Security Advisories: https://support.apple.com/en-us/HT201222 系列（iOS 12.1.4 / 12.4.1 / 14.8 / 15.3.1 / 15.6.1 / 16.3.1 / 16.4.1 / 16.5 / 16.5.1 / 17.3 / 18.1.1 / 18.3.2 等）
- NVD：CVE-2019-8605、CVE-2021-30858、CVE-2022-22620、CVE-2022-32893、CVE-2023-23529、CVE-2023-28205、CVE-2023-32409、CVE-2023-32435、CVE-2023-37450、CVE-2024-23222、CVE-2024-44308、CVE-2024-44309、CVE-2025-24201
- Google Project Zero：《A very deep dive into iOS exploit chains found in the wild》，2019-08-29
- Kaspersky GReAT：《Operation Triangulation》系列，2023
- Citizen Lab：Pegasus/Predator 追踪报告系列
- CISA Known Exploited Vulnerabilities Catalog
- The Hacker News / SecurityWeek / Help Net Security / BleepingComputer / SecurityAffairs：各 CVE 披露当日报道

> 本报告为防御性安全分析，仅基于公开资料整理，用于帮助读者理解威胁并落实防护措施；不包含任何攻击工具、利用代码或操作指引。

---

咨询ios系统请咨询 telegram：https://t.me/one00190
