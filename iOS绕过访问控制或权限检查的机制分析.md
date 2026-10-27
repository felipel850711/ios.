@@TITLE@@iOS 绕过访问控制或权限检查的机制分析
@@SUBTITLE@@选题字面的两个词，在 Apple 的两套公开文本里都不是术语：安全响应顾问页本期 257 份英文镜像（容错解析后全语料 **9,045 段 / 470 个段落名 / 246 页至少含一段**）里，字面短语 `access control` 只有 **21 段 / 8 页 / 5 个段落名**，而且这 21 段**一段都不在 Impact 字段里**、与 `bypass` 的同段共现是 **0**；`permission check` 这个构词 **0 段**，连 1 段都没有。真正承担"访问控制被越过"这件事的是 `bypass` 动词的 **288 段 Impact**、**29 种宾语**、闭合归为 **8 类**；中文手册层 178 页可用正文域里"访问控制"只有 **16 次 / 9 页**，其中 **7 次是"强制访问控制 (MAC)"这个专有名词**，剩下 9 次分布在 7 页里各说各的事；而同一层写通用语义用的是"授权" **172 次 / 63 页**、"权限" **102 次 / 45 页**、"沙盒" **27 次 / 11 页**——换句话说，选题名字和实现词汇之间隔着一层换名，`checks` 一词在措辞层出现 **1,267 段**，但其中 **994 段落在 `improved checks` 这个修复语里、Impact 侧 0 段**，"检查"在 Apple 的公开文本里主要是**修好之后做的事**，不是**失效的那件事**
@@INFO@@报告日期|2026 年 10 月 27 日
@@INFO@@分析对象|《Apple 平台安全保护》zh-CN 指南全量转储 179 页（正文域切分后 178 页可用，逐词普查）；Apple 安全响应顾问页 257 份英文页面镜像（排除页面 `122085`，容错解析后全语料 9,045 段 / 470 个不同段落名）。本篇绕过词族 298 段 / 111 页 / 57 名，其中 Impact 侧 288 段 / 111 页 / 55 名 / 88 个 CVE 编号 / 42 种完整 Impact 句式 / 42 种配套 Description 句式 / 28 种施事主语；`access control` 字面族 21 段 / 8 页 / 5 名；`restrictions` 族 672 段 / 128 页 / 97 名；`checks` 裸词族 1,267 段 / 195 页 / 165 名；全语料在野声明 `may have been exploited` 75 段 / 53 页 / 10 名、`actively exploited` 18 段 / 13 页 / 3 名
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含针对他人设备的操作步骤、不含任何一类访问控制的越过时序、不含授权属性构造与签名校验规避细节、不含提示语规避的操作指引
@@INFO@@分类|访问控制面 / 选题字面词与两层实际用词的**三向错位**实测 / `bypass` 宾语的**闭合分区**（8 类之和恰等于 288）/ "后果语"与"修复语"在字段位置上的分裂 / 两个必须带限定才能读的 0
@@INFO@@难度|进阶（第 1 章的字段位置统计与第 4 章的闭合分区是全篇前提；跳过它们会把 `access control` 这 21 段当成"访问控制类漏洞只有 21 个"，会把 994 段 `improved checks` 当成"检查失效了 994 次"，也会把 Gatekeeper 的 53 段当成 iOS 侧的签名失效）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

选题字面是"绕过访问控制或权限检查"。这九个字里有两个名词（访问控制、权限检查）和一个动词（绕过）。**逐字核对之后，两个名词在 Apple 的两套公开文本里都不是可检索的术语，只有动词是。** 本篇的全部工作从这个核对开始。

三组反差。

**反差一（名词不存在）**：措辞层（Apple 安全响应顾问页，下称 T2）里字面短语 `access control(s)` 只有 **21 段 / 8 页 / 5 个段落名**，且这 21 段的命中位置**全部在 Description 字段**，Impact 字段 **0 段**；把这 21 段与 `bypass` 词族做**段落成员集合**求交，结果是 **0 段**——不是"很少"，是零。`permission check` 这个构词在 9,045 段里 **0 段**：Apple 不说"权限检查"，它说 `Privacy preferences`、`Internet permission requirements`、`sandbox restrictions`。手册层（《Apple 平台安全保护》中文，下称 T1）里"访问控制" **16 次 / 9 页**，但其中 **7 次是"强制访问控制"**这一个专有名词（`系统完整性保护` 一页就占 5 次，`macOS 中的 App 代码签名进程` 1 次，`App 安全性概览` 1 次），余下 9 次分散在 7 页，各自说的是钥匙串、安全隔区、加密概览里的具体句，**不构成一个主题**。

**反差二（动词才是主语）**：T2 里"越过"这个动作由 `bypass` 承担，**298 段 / 111 页 / 57 个段落名**；其中 Impact 字段侧 **288 段**。本篇把这 288 段的**宾语**逐条取出（每个 Impact 句子恰好产出一个宾语窗口，窗口总数 288，与 Impact 侧段数一一对应），得到 **29 种不同宾语短语**，归成 **8 类**，各类段数 95 / 58 / 52 / 48 / 16 / 14 / 4 / 1，**之和恰为 288，没有未归类残留**。这就是"绕过访问控制"在公开文本里的真实形状——它不是一个类，而是八类，第一名（用户同意与隐私设置）占 33%，最后两名合计只占 1.7%。

**反差三（"检查"是修复语，不是失效语）**：T2 里 `check(s)` 裸词族大到 **1,267 段 / 195 页 / 165 个段落名**，看似"检查"是核心概念。但按字段拆开：**Impact 侧 51 段，Description 侧 1,240 段**。其中固定短语 `improved checks` **994 段**，Impact 侧 **0 段**——它百分之百出现在 Description，也就是"我们改了什么"那一栏。同一栏里 `additional checks` **34 段**、Impact 侧同样 **0 段**。结论是：**"检查"在 Apple 公开文本里是修复动作的名字，不是失效对象的名字**；失效对象的名字在第 4 章那 29 种宾语里。把 994 段 `improved checks` 读成"994 次检查被绕过"是本选题最容易犯的一错，第 9 章单列。

于是本篇的问题依次是：

**问题一（第 1–3 章）** 选题的两个名词在两层各自是什么词、什么位置、什么量。答案：T2 用 `bypass` + 具体宾语，不用抽象名词；T1 用"授权/权限/沙盒"三个具体词，不用"访问控制"这个抽象词，且"访问控制"7/16 次属于 MAC 专名。

**问题二（第 4–6 章）** `bypass` 的 288 段宾语能否**闭合分区**。能，且本篇给出一张 8 类表和一张 29 行宾语表，附类别到页/名的映射，同时登记 `Gatekeeper` 这一类**名词与动词一一绑定**（53 段全部同时含 `bypass`）的异常。

**问题三（第 7–9 章）** 同一后果的**句式**有几种、修复语有几种。答案：Impact 侧 **42 种完整句式**，头部一句 `An app may be able to bypass Privacy preferences` 吃掉 **44 段**；配套 Description **42 种**，头部 `The issue was addressed with improved checks.` 21 段。**句式复用率极高**，说明这些后果是被模板化登记的，不是逐案描述。

**问题四（第 10–13 章）** 施事前提与批次分布。答案：**28 种施事主语**，本地前提类（`An app` / `A shortcut` / `A malicious application`）合计 **152 段**，远程类（`A remote attacker` / `An attacker on the local network` / `A website` / 网页内容 / 网络位置）**30 段**，已获读写能力类 **40 段**，物理访问类 **6 段**；8 个类别在页面编号轴上的跨度不同，**内核内存与指针防护类止于编号 122718**，而用户同意类一直延伸到最新批次。

**问题五（第 14–19 章）** 本篇能否支撑"访问控制方向不重要"或"检查机制形同虚设"。都不能。第 18 章给出不可外推清单，其中最要紧一条：`access control` 字面 0 共现 `bypass` **不能**写成"访问控制没有被绕过过"，只能写成"顾问页在描述绕过后果时不使用 access control 这个短语"。

**写作边界（贯穿全篇）**：T2 的段落名（`Kernel`、`Shortcuts`、`WebKit`、`AppleMobileFileIntegrity`、`Sandbox`……）是 Apple 的**归类动作**，不是实现层证据。本篇凡涉及某段属于哪个组件，一律写"顾问页把这条后果归在该段落名之下"。T1 的机制句（例如"内核会检查 Apple 专有的动态代码签名授权是否存在"）是**手册层的公开承诺**，其实现位置本轮不可核对：XNU 与用户态开源部件源码镜像本期未纳入本篇取数，凡涉及判定发生在哪个进程、哪条内核路径的表述一律记为"未核对"并在第 17 章登记为缺口。所引 CVE 与编号只用于说明公开修复事实与批次分布，不含越过路径、不含时序、不含属性构造细节。`Gatekeeper` 与 `Sidecar`、`Login Window` 的条目属于 macOS 批次，本篇逐处标注平台归属，不并入 iOS 结论。

---

# 1 选题字面的两个名词，在 T2 里各值多少段

本章先把"字面词有多少"钉死，因为全篇的反差都建立在这两个数上。

## 1.1 语料与切块口径

语料 = Apple 安全响应顾问页英文页面镜像 258 份，排除页面 `122085` 后 **257 份**。切块刀与 #104–#109 相同：以 `<h3 class="gb-header…">` 为段落名起点，段落终点由**四个锚点**共同决定——下一个 `gb-header`、`<div id="disclaimer"`、`<footer`、文本末尾。缺任一锚点会把页脚免责声明卷进字段文本，这条修正最早在 #106 登记，本篇沿用。字段文本 = Impact + Description + 致谢三段拼接；`Available for…` 开头的段落不计入。

按此口径本期复现结果：**9,045 段 / 470 个不同段落名 / 246 页至少含一段**。三个分母都与 #106–#109 完全一致，说明镜像未变、刀口未变。需要说明：257 份页面里只有 246 份能切出段落，余下 11 份是**不含任何 CVE 段落**的页面，所以"页"这一列在涉及分母时必须说是 246 还是 257——本篇凡给出页数，均为**命中页的绝对数**，不除以页分母。

## 1.2 词族基线（字段文本命中段数，Impact 侧单列）

| 词族（正则带词边界） | 字段文本段数 | Impact 侧段数 | Description 侧 | 致谢侧 | 页数 | 段落名数 |
|---|---|---|---|---|---|---|
| `access control(s)` | 21 | 0 | 21 | 0 | 8 | 5 |
| `permission check/validation/enforcement` | 0 | 0 | 0 | 0 | 0 | 0 |
| `permission(s)` | 575 | 57 | 533 | 0 | 138 | 99 |
| `entitlement(s)` | 131 | 7 | 128 | 0 | 74 | 38 |
| `sandbox` | 411 | 348 | 86 | 0 | 108 | 67 |
| `authorization / authorize*` | 198 | 11 | 194 | 0 | 64 | 52 |
| `privilege(s)` | 451 | 448 | 6 | 0 | 104 | 72 |
| `user consent` | 69 | 47 | 25 | 0 | 54 | 18 |
| `consent` | 76 | 54 | 25 | 0 | 58 | 18 |
| `gatekeeper` | 53 | 53 | 0 | 0 | 30 | 16 |
| `check(s)` 裸词 | 1,267 | 51 | 1,240 | 2 | 195 | 165 |
| `restriction(s)` | 672 | 48 | 642 | 0 | 128 | 97 |
| `bypass*` | 298 | 288 | 31 | 0 | 111 | 57 |

这张表里三件事要先说清。

**第一，`access control` 的 Impact 侧是 0。** 不是四舍五入后的 0，是逐段判定的 0：21 段里没有任何一段的 Impact 文本含这个短语。这 21 段的 Description 原句只有两种：`An authorization issue was addressed with improved access control.`（18 段）和 `An access issue was addressed with improved access controls.`（3 段，段落名都是 `Siri`）。它们落在 8 个页面（编号 149034–149043 之间的 8 页）、5 个段落名（`DeviceCheck`、`Keychain Access`、`Sandbox`、`Siri`、`Spotlight`）之下。**这是一句修复语，不是一句后果语。**

**第二，`permission check` 是 0 段。** 我按 `permission` 后接 `check/checks/validation/enforcement` 构词去搜，9,045 段里一段都没有。Apple 不说"权限检查"。`permissions` 本身有 575 段，但它的用法是 `Internet permission requirements`（12 段做 `bypass` 的宾语）、`A permissions issue was addressed with additional restrictions.`（13 段做修复语），而不是"某个权限检查被跳过"。

**第三，字段位置决定词义。** `sandbox` 的 411 段里 348 段在 Impact——因为 Apple 确实用"sandbox 被越过"表达后果；`restriction(s)` 的 672 段里只有 48 段在 Impact，因为其余是 `additional restrictions`（385 段）与 `additional sandbox restrictions`（86 段）这类**修复语**。同一张表按 Impact 侧排序，会得到与按总段数排序完全不同的名次，这就是第 2 章的主题。

## 1.3 差额是不是采集缺陷

不是。三条证据：(1) 基线分母与上一轮四篇完全复现；(2) `access control` 的 21 段可用同一把刀在 8 个具体页面编号上逐段复核，第 3 章把原句全量列出；(3) `permission check` 的 0 段同时用宽松构词（`permission` 与 `check` 在同一段共现）复核过——`permissions` 与 `bypass` 同段共现 **37 段**，说明"共现"本身是存在的，只是从不构成 `permission check` 这个短语。也就是说：**这两个名词性短语的缺席是文本事实，不是取数缺陷。**

---

# 2 字段位置：同一个词在 Impact 与在 Description 是两件事

T2 每个 CVE 段落至少两栏：Impact（后果）与 Description（修复）。把词族按字段拆开后，本选题的词汇结构立刻分成两组。

**后果组（Impact 侧占比高）**：`privilege` 451 段中 448 段在 Impact（99.3%）、`sandbox` 411 中 348（84.7%）、`gatekeeper` 53 中 53（100%）、`consent` 76 中 54（71%）、`user consent` 69 中 47（68%）、`bypass` 298 中 288（96.6%）。

**修复组（Impact 侧近乎为 0）**：`check(s)` 1,267 中 51（4.0%）、`restriction(s)` 672 中 48（7.1%）、`permissions` 575 中 57（9.9%）、`entitlement` 131 中 7（5.3%）、`authorization` 198 中 11（5.6%）、`access control` 21 中 0（0%）。

这两组的区分不是文风差异，而是**登记规则**：顾问页的 Impact 栏写"谁能做到什么"，所以那里必须出现动词（`bypass`、`escalate`、`access`）；Description 栏写"我们改了什么"，所以那里必须出现名词化的修复动作（`improved checks`、`additional restrictions`、`improved access control`）。因此：

- 一个词在 Impact 侧的出现数，可以当作"该概念作为被越过的对象被公开承认过多少次"的下限；
- 一个词在 Description 侧的出现数，**不能**当作"该概念失效了多少次"，它是修复措辞的模板命中数。

本选题把 `access control`（0/21）与 `checks`（51/1,267）放错组，就会得出"访问控制类缺陷只有 21 个"或"检查机制失效 1,240 次"这两句都错的话。

`gatekeeper` 是唯一一个**两个字段完全重合**的族：53 段，Impact 53、Description 0（`0` 指该词不出现在 Description 栏）。它同时也是与 `bypass` 共现最紧的族——见第 5 章。

---

# 3 `access control` 这 21 段是什么

把它们全量列出，是因为第 1 章的结论"这不是一个主题"必须有可核对的原文。

21 段分布在 8 个页面（编号 149034、149035、149036、149037、149038、149041、149042、149043），段落名 5 个。按页面看：149035 与 149043 各 5 段，149042 与 149034 与 149041 各若干，其余单段。原句只有两种：

- `An authorization issue was addressed with improved access control.` —— 18 段，段落名为 `DeviceCheck`（8 页全部出现）、`Keychain Access`、`Sandbox`、`Spotlight`；
- `An access issue was addressed with improved access controls.` —— 3 段，段落名均为 `Siri`。

两点必须同时说：

**（一）这 21 段的 Impact 栏是别的话。** 它们的 Impact 不是"某物绕过访问控制"，而是与 `authorization issue` / `access issue` 配对的后果句。也就是说 Apple 把"访问控制"这个词**只用于描述修复内容**，描述后果时改用 `authorization` / `access` + 具体动词。

**（二）这 21 段与 `bypass` 同段共现 0 次。** 用段落成员集合求交：`access control` 集合 21 个成员，`bypass` 集合 298 个成员，交集 **0**。这是一个"强 0"，它比"数量少"信息量大得多——它意味着在整个镜像里，Apple **从未**在同一个 CVE 段落里同时写出 `bypass` 和 `access control`。

但要注意它的反面**不成立**：不能由此说"访问控制没被绕过"。只能说"这两个词从不共现"。第 4 章那 8 类宾语就是 Apple 真正用来表达"访问控制被越过"的词。

---

# 4 `bypass` 288 段宾语的闭合分区

这是本篇的主表。取法：对每个 Impact 侧含 `bypass` 的段落，把 Impact 按 `||`（同段多条 Impact 合并符）切成子句，在含 `bypass` 的子句里按词序取 `bypass` 之后**最多 6 个词、截到句末或标点为止**，作为宾语窗口。

先给两条自检：Impact 侧含 `bypass` 的段落 **288 段**，产出的宾语窗口**总数也是 288**，说明每段恰好一个窗口（没有一段出现两次 `bypass`）；不同宾语短语 **29 种**，29 种全部落入下面 8 类，**未归类 0 段**，各类之和 95+58+52+48+16+14+4+1 = **288**，分区闭合。

| 类别 | 段数 | 页数 | 段落名数 | 成员宾语 |
|---|---|---|---|---|
| A 用户同意与隐私设置 | 95 | 62 | 23 | `Privacy preferences`(58) / `certain Privacy preferences`(30) / `sensitive Shortcuts app settings`(6) / `Apple Intelligence security prompts`(1) |
| B 启动前校验与代码签名强制 | 58 | 34 | 17 | `Gatekeeper checks`(43) / `Gatekeeper`(10) / `code signing enforcement`(5) |
| C 内核内存与指针防护 | 52 | 39 | 7 | `kernel memory protections`(23) / `Pointer Authentication`(17) / `ASLR`(11) / `KASLR`(1) |
| D 网络与来源策略 | 48 | 35 | 7 | `Internet permission requirements`(12) / `Same Origin Policy`(10) / `network filters`(8) / `network restrictions`(7) / `some web content restrictions`(6) / `Content Security Policy`(4) / `sender policy checks…`(1) |
| E 沙盒与启动约束 | 16 | 15 | 4 | `sandbox restrictions`(13) / `launch constraint protections…`(3) |
| F 认证与登录流程 | 14 | 13 | 6 | `authentication policy`(7) / `browser extension authentication`(2) / `IPsec authentication…`(2) / `pairing`(1) / `Login Window…`(1) / `Digital ID authentication…`(1) |
| G 设备锁定与防盗 | 4 | 4 | 2 | `the Lock Screen`(2) / `Activation Lock`(2) |
| H 交互界面防护 | 1 | 1 | 1 | `clickjacking protections for secure prompts`(1) |

四点读法。

**（1）第一名不是"访问控制"，是"隐私设置"。** A 类 95 段占 33%，页面 62 页，是八类里覆盖页面最广的；它说的是 `bypass Privacy preferences`——App 越过的不是内核级访问控制，而是**用户在设置里勾选项与系统提示**。这决定了本篇后面所有"前提条件"统计的走向。

**（2）B 类是 macOS 词。** `Gatekeeper` 53 段占 B 类 91%，段落名多为 `Xcode`、`libarchive`、`Safari`、`Automator`、`Launch Services`。iOS 侧没有 Gatekeeper。把 B 类并入"iOS 访问控制被绕过"是把两个平台的措辞混在一起，第 6 章按页号区间再核一次。

**（3）C 类是"越权之后的第二次越过"。** 52 段里 40 段的施事主语已经是"具备任意读写能力/已取得内核代码执行"的攻击者（`arbitrary kernel read and write` 16、`arbitrary read and write` 9、`malicious attacker with arbitrary read and write` 8、`already achieved kernel code execution` 7，四数之和恰为 40）。这类句描述的是**利用链后半段的防护墙**，不是入口。

**（4）H 类只有 1 段。** 它出现在页面 149035 的 `Foundation` 段落名下，原句 `A malicious app may be able to bypass clickjacking protections for secure prompts`。它证明"点击劫持防护"在 T2 里也被 `bypass` 动词管辖——这是八类里唯一一段，读的时候要按单例处理，不做趋势推断。

---

# 5 共现矩阵：谁与 `bypass` 真的同段

第 3 章说 `access control ∩ bypass = 0`，但"0"和"接近 0"必须放在同一张矩阵里才读得出来。下表的 |A|、|B| 是**段落成员集合**的基数，|A∩B| 是成员交集，不是两个计数的乘积估计。

| 词族 A | \|A\| | 词族 B | \|B\| | \|A∩B\| | A 的全部成员是否都在 B 内 |
|---|---|---|---|---|---|
| `access control` | 21 | `bypass` | 298 | **0** | 否 |
| `access control` | 21 | `permissions` | 575 | 0 | 否 |
| `access control` | 21 | `sandbox` | 411 | 0 | 否 |
| `gatekeeper` | 53 | `bypass` | 298 | **53** | **是（完全重合）** |
| `checks` 裸词 | 1,267 | `bypass` | 298 | 100 | 否 |
| `permissions` | 575 | `bypass` | 298 | 37 | 否 |
| `sandbox` | 411 | `bypass` | 298 | 29 | 否 |
| `consent` | 76 | `bypass` | 298 | 14 | 否 |
| `authorization` | 198 | `bypass` | 298 | 8 | 否 |
| `privilege` | 451 | `bypass` | 298 | 6 | 否 |
| `restrictions` | 672 | `bypass` | 298 | 3 | 否 |
| `entitlement` | 131 | `bypass` | 298 | 2 | 否 |
| `user consent` | 69 | `consent` | 76 | **69** | **是（A ⊂ B）** |
| `sandbox` | 411 | `entitlement` | 131 | 23 | 否 |
| `sandbox` | 411 | `privilege` | 451 | 18 | 否 |

三行值得单独解读。

**`gatekeeper` 行是 53/53/53。** 一个 53 段的族与 298 段的 `bypass` 族交集恰好 53，意味着 **Gatekeeper 在顾问页里只以"被绕过"的形式出现**。T2 没有 `improved Gatekeeper`、没有 `Gatekeeper was addressed` 这样的修复语——它有 `Gatekeeper checks` 43 次做 `bypass` 的宾语、`Gatekeeper` 10 次做宾语，就此为止。这与手册层形成第 11 章要说的对照：**T1 里 `Gatekeeper` 字面 0 次。**

**`entitlement` 行只有 2。** `entitlement(s)` 有 131 段、74 页、38 个段落名，看起来是访问控制的核心词，但它与 `bypass` 同段仅 **2 段**，Impact 侧仅 7 段。Apple 说"授权属性"时几乎都在 Description 栏解释修复内容，从不把它当被越过的对象命名。**"entitlement 被绕过"这句话在 T2 里基本不存在**，即使手册层第 10 章明确写出"授权是签名到 App 的键值对"这一强承诺。

**`checks` 行的 100 段是最高共现。** `check(s)` 与 `bypass` 同段 100 次，但对照第 2 章：`checks` 的 1,267 段里只有 51 段在 Impact。把这 100 段拆开，绝大多数是"Impact 用 `bypass` 说后果 + Description 用 `improved checks` 说修复"的同段配对。**共现是"后果—修复"配对的产物，不是"检查被越过"的证据。** `improved checks` 单独统计是 994 段、Impact 侧 0 段，这条把上面的解释钉死。

---

# 6 平台归属与批次跨度

八类在页面编号轴上的跨度不一样，这直接影响"哪一类还在继续发生"这个问题的可信度。**下表用页面编号，不用日期**：镜像文件名是页面编号，本篇没有把编号映射到发布日期，所以只说"编号更早/更晚"，不说"哪一年"。

| 类别 | 页数 | 编号区间 |
|---|---|---|
| A 用户同意与隐私设置 | 62 | 120306 – 149043 |
| B 启动前校验与代码签名强制 | 34 | 120887 – 149043 |
| C 内核内存与指针防护 | 7 名 / 39 页 | **120880 – 122718** |
| D 网络与来源策略 | 35 | 120908 – 149038 |
| E 沙盒与启动约束 | 15 | 122373 – 149042 |
| F 认证与登录流程 | 13 | 121250 – 149034 |
| G 设备锁定与防盗 | 4 | 121238 – 126793 |
| H 交互界面防护 | 1 | 149035 |

命中最多的 15 个页面（按该页 Impact 侧 `bypass` 段数）：149035（13 段）、120911（10）、122373（8）、128072（8）、128071（8）、120909（7）、120916（7）、149042（7）、120910（6）、128067（6）、149043（6）、127115（6）、120912（6），其余为 5 段及以下。

两点。

**C 类止于 122718。** 内核内存保护 / 指针认证 / ASLR / KASLR 这四型宾语一共 52 段，全部落在编号 ≤ 122718 的页面。这不表示内核防护后来不再被越过，而表示**顾问页后来不再用 `bypass kernel memory protections` 这类句式描述后果**——第 8 章的修复语统计会说明后来改用了什么说法。这是一个**措辞迁移**，不是风险消失，写反就是本篇最大的外推风险。

**B 类的两端都在。** Gatekeeper 系 53 段跨 120887 到 149043，说明这条 macOS 措辞在整个镜像期内都存在；结合第 11 章"T1 层 `Gatekeeper` 0 次"，它的位置只能是 **macOS 的顾问页措辞 + 中文手册根本不讲的组件**。iOS 侧签名强制由 `code signing enforcement` 5 段单独承担。

---

# 7 段落名：55 个名字如何分布

288 段的段落名（顾问页里的组件归类）**55 个**，头部集中度中等：

`Kernel` 31、`Shortcuts` 25、`WebKit` 24、`AppleMobileFileIntegrity` 14、`Sandbox` 11、`Accounts` 9、`PackageKit` 9、`Safari` 9、`Audio` 8、`LaunchServices` 8、`RTKit` 8、`AirPlay` 7、`copyfile` 7、`Messages` 6、`Sync Services` 6、`libxpc` 6、`CloudAttestation` 5、`CoreServices` 5、`System Settings` 5、`UIKit` 5、`dyld` 5、`RPAC` 4、`libarchive` 4、`APFS` 3、`Assets` 3，其后是 30 个 ≤ 2 段的名字。

四条读法。

**（1）内核与强制层的名字堆：`Kernel` 31 + `RTKit` 8 + `AppleMobileFileIntegrity` 14 + `dyld` 5 + `APFS` 3 = 61 段，占 288 的 21.2%。** 本篇不去推断这 61 段各属第 4 章哪一类（类别是按句子宾语划的，名字是按组件归的，两套坐标不同轴），只登记一个可核对的事实：`RTKit` 的 8 段与 `Kernel` 的 31 段里有整句完全相同（`An attacker with arbitrary kernel read and write capability may be able to bypass kernel memory protections. Apple is aware of a report that this issue may have been exploited.`），说明同一句话会在一份页面的多个段落名下重复登记——第 8 章据此说明"段数不等于漏洞数"。

**（2）`Shortcuts` 25 段 + `Safari`/`WebKit` 33 段 = 58 段，占 20%。** `Shortcuts` 名下有三种宾语：`certain Privacy preferences`、`Internet permission requirements`、`sandbox restrictions`、`sensitive Shortcuts app settings`。同一个组件名下出现四种不同类别的宾语，**说明类别是按句子划分的，不是按组件划分的**——用段落名去推断"哪一类防护失效"会串类。

**（3）`copyfile` 7 段。** 这 7 段的 Description 里有 `A file quarantine bypass was addressed with additional checks.` 这类句子；`quarantine` 全语料 31 段（Impact 4 / Description 27），其中 `file quarantine bypass` 短语 **27 段全部在 Description**。也就是说**隔离属性被绕过这件事，Apple 只在修复栏说，不在后果栏说**——与第 3 章 `access control` 的处置方式一模一样，这不是巧合，是登记规则。

**（4）"沙盒"有三个互不相等的数，写作时不能互换。**（a）`sandbox` **词族**命中：411 段（字段文本），Impact 侧 348 段，页数 108；（b）`sandbox restrictions` 作为 **`bypass` 宾语**：13 段；（c）段落名恰为 `Sandbox` 的 **`bypass` 段**：11 段。第 5 章的共现矩阵另给一个数：`sandbox` 词族与 `bypass` 词族**同段共现 29 段**。四个数各测各的集合，谁也不包含谁，本篇在引用时逐处标明是哪一个。

---

# 8 句式模板：42 种 Impact 句覆盖 288 段

Impact 侧 **288 段**里，完整句式只有 **42 种**，平均每种 6.9 段，头部集中度极高。前 12 种（括号内段数）：

`An app may be able to bypass Privacy preferences`(44)、`An app may be able to bypass certain Privacy preferences`(29)、`An app may bypass Gatekeeper checks`(19)、`An attacker with arbitrary kernel read and write capability may be able to bypass kernel memory protections. Apple is aware of a report that this issue may have been exploited.`(16)、`A malicious application may be able to bypass Privacy preferences`(14)、`A shortcut may be able to bypass Internet permission requirements`(12)、`A shortcut may be able to bypass sandbox restrictions`(11)、`An app may be able to bypass ASLR`(11)、`A maliciously crafted ZIP archive may bypass Gatekeeper checks`(10)、`A malicious attacker with arbitrary read and write capability may be able to bypass Pointer Authentication`(8)、`A remote attacker may be able to bypass network filters`(8)、`An app may be able to bypass network restrictions`(7)。

前 12 种合计 **188 段 = 65.3%**。这有两层含义：

**（一）后果被"模板化"了。** 同一句 Impact 会在一份页面的多个段落名下重复出现（上例 `bypass kernel memory protections` 那句在 `Kernel` 与 `RTKit` 下各出现一次，因此 16 段可能只对应 8 个 CVE）。所以**段数不等于漏洞数**：本篇同时给出 88 个不同 CVE 编号（288 段每一段的字段文本里都能取到 CVE 编号，distinct 88），段/编号比 = 3.27，说明同一编号常被拆成多段或多段落名登记。

**（二）"绕过 X"的 X 是一个小闭集。** 29 种宾语 × 有限施事主语（第 9 章 28 种）的乘积远大于 42，实际只出现 42 种组合——语言上的稀疏度意味着：**顾问页并不试图描述机制，只描述类别。** 这是本篇对"从措辞推断实现"最硬的一条反驳。

配套 Description 也是 **42 种**，头部：`The issue was addressed with improved checks.`(21)、`A logic issue was addressed with improved checks.`(17)、`A file quarantine bypass was addressed with additional checks.`(17)、`A memory corruption issue was addressed with improved validation.`(16)、`This issue was addressed by adding an additional prompt for user consent.`(14)、`This issue was addressed through improved state management.`(14)、`A permissions issue was addressed with additional restrictions.`(13)、`A logic issue was addressed with improved validation.`(12)、`A race condition was addressed with additional validation.`(11)、`This issue was addressed by removing the vulnerable code.`(10)、`An authentication issue was addressed with improved state management.`(9)、`A permissions issue was addressed with additional sandbox restrictions.`(8)。

其中第五句 `adding an additional prompt for user consent` **25 段**（全语料口径），是 A 类后果的标准修复配套：**"越过隐私设置"的公开修复方式是"多弹一次提示"**。这句话在本篇的作用是给出一个边界——它说明 Apple 对该类的处置停留在交互层提示，而不是在措辞层承诺更强的强制点。把它当作"实现只改了提示"是不行的（第 18 章），但把它当作"公开文本对该类的承诺强度"的证据是合法的。

---

# 9 施事前提：谁在绕过

取 Impact 首句中 `bypass` 之前的窗口，得到 **28 种施事主语**（完整清单见附表 B）。头部与分类如下。

**本地已安装代码类**：`An app may be able to`(102)、`A shortcut may be able to`(29)、`An app may`(19)、`A malicious application may be able to`(14)、`A maliciously crafted ZIP archive may`(10)、`An archive may be able to`(7)、`A maliciously crafted app may be able to`(5)、`A maliciously crafted disk image may`(4)、`A malicious app may be able to`(3)、`A malicious application may`(3)、`A malicious JAR file may`(3)、`An Automator Quick Action workflow may be able to`(3)、`A malicious Bluetooth input device may`(1)——**13 种主语、合计 203 段 = 70.5%**。

**已具备读写能力类**：`An attacker with arbitrary kernel read and write capability…`(16)、`An attacker with arbitrary read and write capability…`(9)、`A malicious attacker with arbitrary read and write capability…`(8)、`An attacker that has already achieved kernel code execution…`(7)——4 种主语、**合计 40 段 = 13.9%**。

**远程类**：`Processing maliciously crafted web content may`(10)、`A remote attacker may be able to`(9)、`An attacker on the local network may be able to`(7)、`A website may be able to`(4)、`An attacker in a privileged network position may be able to`(2)——5 种主语、**合计 32 段 = 11.1%**。

**用户与物理访问类**：`A user may be able to`(6)、`A user with physical access to an iOS device…`(2)、`An attacker with physical access to a macOS device with Sidecar enabled…`(2)、`A person with physical access to a Mac…`(1)、`An attacker with physical access to a locked device…`(1)——5 种主语、**合计 12 段 = 4.2%**。

**其余**：`On MDM-managed devices, an app may be able to`(1)，1 种主语、1 段 = 0.3%。

一句口径说明：本章把 ZIP 归档、磁盘镜像、JAR、Automator 工作流和蓝牙输入设备都并进"本地已安装代码类"，只看**是否需要本地已存在的物件**这一个维度，合计 203 段；附表 B 在同一 203 段内部再按物件形态细分为"本地已安装代码 / 本地制品 / 本地外设"三档。**两处分组数不同、段数相同**，引用时按各自标签，不要跨章合并子标签。

校验：13+4+5+5+1 = **28 种主语**，203+40+32+12+1 = **288 段**，两个和都与独立计数（附表 B 的 28 行、总段数 288）吻合。

四分类读出的结论是：**在"访问控制/权限检查"这一面，公开承认的越过者第一名不是远程攻击者，是本地已安装的 App 与本地制品（70.5%）；远程只占 11.1%。** 这与第 4 章 A 类居首（隐私设置 95 段）互为印证——App 越过的是用户为其设定的授权项。而"任意读写能力"这一 13.9% 的类，是**已经进入利用链后半段**的前提，它对应 C 类，与入口无关。

---

# 10 手册层用什么词代替"访问控制"

语料 = 《Apple 平台安全保护》zh-CN 全量转储 **179 页**。正文域规则与 #104–#109 相同：取最后一个逐字 `版权和商标` 行之后、首个含 `Apple Footer` 行之前。本期 **179 页全部能取到起始锚点**，但其中 1 页（`sec2727afbbc`，页面正文本身就是"版权和商标"一节）切出的窗口只有 5 行 244 字符，与 #109 一样把它排除，故**可用 178 页**；可用页的正文窗口 **9 – 176 行**。这个页宽差决定了"命中 0 次"必须结合窗口来读。

| 中文词 | 次数 | 命中页数 |
|---|---|---|
| 限制 | 297 | 178 |
| 验证 | 252 | 74 |
| 信任 | 182 | 53 |
| 授权 | 172 | 63 |
| 权限 | 102 | 45 |
| 检查 | 66 | 30 |
| 强制 | 53 | 31 |
| 描述文件 | 46 | 17 |
| 公证 | 26 | 6 |
| 沙盒 | 27 | 11 |
| 同意 | 26 | 19 |
| 绕过 | 23 | 9 |
| 校验 | 19 | 3 |
| 访问控制 | 16 | 9 |
| 隔离 | 10 | 6 |
| 特权 | 9 | 3 |
| 锁定屏幕 | 8 | 6 |
| 指针认证 | 5 | 3 |
| 身份验证 | 5 | 3 |
| 保护级别 | 1 | 1 |
| Gatekeeper | 0 | 0 |
| 沙箱 | 0 | 0 |
| 越狱 | 0 | 0 |
| 门控 | 0 | 0 |

三点。

**（1）"访问控制"在手册层也不是主题词。** 16 次 / 9 页（逐页见附表 C）。其中 **7 次是"强制访问控制"**这一个专名——`系统完整性保护` 一页 5 次、`macOS 中的 App 代码签名进程` 1 次、`App 安全性概览` 1 次；剩下 9 次分布在 `钥匙串数据保护`(2)、`安全隔区`、`接入无线网络时的隐私功能`、`watchOS 系统安全性`、`数字签名和加密`(2)、`加密和数据保护概览`，每处说各自的句子，**没有一处给出"访问控制"的通用定义**。所以选题字面"访问控制"在 T1 层是个**半专名词**：当作通用概念检索会得到 16 次，其中近半是 MAC。

**（2）通用语义由"授权 / 权限 / 沙盒"三个词承载。** "授权"172 次 / 63 页是本层最接近"access control"的译名，而且手册明确给出了它的定义句（第 12 章逐字引用）。"权限"102 次 / 45 页、"沙盒"27 次 / 11 页。也就是说，T1 讲的是**具体机制名**，T2 讲的是**具体宾语名**，两层都回避抽象名词——这与第 8 章"句式稀疏度"是同一个现象在不同层的再现。

**（3）四个 0 要分别限定。** `Gatekeeper` 0 次：在 178 页正文域内该英文字面一次都没有，而它在 T2 有 53 段（第 13 章）。`沙箱` 0 次：本层统一写"沙盒"，二者不是两个概念。`越狱` 0 次：本层不含该词，涉及用户自主安装的说法在本层不存在，不能引本层作证。`门控` 0 次：中文没有把 Gatekeeper 或访问控制译成"门控"。四个 0 都是**这一层这些页这个窗口**的 0，不是"Apple 不谈这件事"的 0。

## 10.1 一个反例：`特权` 的次数与页数

`特权` 只有 9 次，却分散在 3 页，是本表"次数 / 页数"比最高的一行之一。逐页拆开看：`macOS 中的 Option ROM 安全性` 1 页就占 **7 次**，另外两页（`基于 Intel 的 Mac 中的固件密码保护`、`在 macOS 中安全扩展内核`）各 **1 次**。同一个词，7 次来自同一页里的连续表述，1 次来自另一机制的一句提及——两者不能同等当作"该机制讲过特权"。同一现象在`校验`一行更极端：**19 次 / 3 页**，其中 `Apple 钱包中证件的安全性` 独占 **16 次**，`iCloud 高级数据保护` 2 次、`签名系统宗卷安全性` 1 次。

反过来，`强制` 是 **53 次 / 31 页**，单页最高只有 8 次（`操作系统完整性`），其余大量页面各 1 次；`限制` 是 **297 次 / 178 页**，几乎每页 1.7 次。**所以次数远大于页数的行，说明该词是某几页里的复述用词，而不是分散在多个主题里的概念；页数接近次数、单页上限低的行，才是真正跨主题的通用词。** 用总次数给 T1 排序会得出"特权与校验是重要主题"的错误结论——按页数看，它们在 178 页里合计只出现 6 页。

---

# 11 两层换名矩阵

把 T2 的 8 类宾语与 T1 的中文词并置，得到本篇真正想说的东西：**同一道防线在两层有不同的名字，且没有一个词是选题字面用的。**

| 第 4 章类别 | T2 宾语原词 | T2 段数 | T1 最接近的中文词与次数 |
|---|---|---|---|
| A 用户同意与隐私设置 | Privacy preferences / certain… / Shortcuts app settings / Apple Intelligence security prompts | 95 | 同意 26、权限 102、授权 172 |
| B 启动前校验与代码签名强制 | Gatekeeper checks / Gatekeeper / code signing enforcement | 58 | 签名 386、代码签名 34、公证 26、Gatekeeper **0** |
| C 内核内存与指针防护 | kernel memory protections / Pointer Authentication / ASLR / KASLR | 52 | 指针认证 5、隔离 10、特权 53 |
| D 网络与来源策略 | Internet permission requirements / Same Origin Policy / network filters / CSP / web content restrictions | 48 | 限制 297、网络（各专页）、验证 252 |
| E 沙盒与启动约束 | sandbox restrictions / launch constraint protections | 16 | 沙盒 27、授权 172 |
| F 认证与登录流程 | authentication policy / browser extension authentication / pairing / Login Window | 14 | 认证 372、身份验证 5、锁定屏幕 8 |
| G 设备锁定与防盗 | the Lock Screen / Activation Lock | 4 | 锁定屏幕 8、激活锁（专页） |
| H 交互界面防护 | clickjacking protections for secure prompts | 1 | 无对应词（本层不含"点击劫持"主题句，本篇未逐页核） |

矩阵的用途不是"翻译"，而是**阻止跨层引用**：写"Apple 手册承诺了 X，所以 X 被绕过 Y 段"这种句子时，X 与 Y 往往不在同一个语言层，也不在同一个对象上。第 18 章把它列进不可外推清单。

---

# 12 手册层写出的四道防线（逐字）

以下四段是 T1 原文逐字引用，用于回答"公开文本对访问控制的承诺写到了多细"。**引用只证明手册如此陈述，不证明实现位置与强度。**

**（一）声明的授权。** `iOS、iPadOS 和 visionOS 使用 沙盒 、声明的授权和地址空间布局随机化 (ASLR) 帮助确保运行时安全性。`（`iOS、iPadOS 和 visionOS 中的运行时进程安全性`）同页另一句把"授权"定义到位：`iOS 使用声明的授权来控制第三方 App 对用户信息及功能（如 iCloud 和扩展功能）的访问。授权是签名到 App 的键值对，允许对运行时因素之外的内容（如 UNIX 用户 ID）进行认证。授权已经过数字签名，因此无法更改。`——**这一句是本篇找到的、手册层对"访问控制凭据"最强的一句公开承诺**，而它对应的 T2 词族 `entitlement` 与 `bypass` 同段只有 **2 段**（第 5 章）。承诺强、失效声明少，这个反差是本篇的核心观察之一。

**（二）执行许可与授权检查。** `iOS 和 iPadOS 使用 ARM 的 Execute Never (XN) 功能提供进一步的保护，该功能会将内存页标记为不可执行。只有处于严格的控制条件下，App 才能使用标记为可写入和可执行的内存页：内核会检查 Apple 专有的动态代码签名授权是否存在。`（同页）——手册在这里把"检查"这个动作明确归给**内核**，而 T2 的 `checks` 994 段全部在 Description（修复语），Impact 侧 0 段。**两处的"检查"不是同一件事**：一个是机制描述，一个是修复措施清单。

**（三）强制访问控制与代码签名互为前提。** `强制访问控制 (MAC) 需要代码签名以启用受系统保护的授权。例如，需要通过防火墙访问的 App 必须使用合适的 MAC 授权进行代码签名。`（`macOS 中的 App 代码签名进程`）——注意这句的主语是 MAC 授权，且页面是 **macOS** 页面。它对应 T2 的 B/E 类，不对应 iOS 侧。

**（四）信任缓存决定"能拿到哪些授权"。** `非平台二进制文件（例如已公证的第三方代码）必须拥有有效的证书链才能执行，它们可拥有的授权受 Apple 开发者计划向开发者签发的签名描述文件限制。`（`信任缓存`）同页前一句：`如果生成的哈希值可在信任缓存中找到，则为该二进制文件创建的可执行映射会被授予平台权限；也就是说，这些映射可能拥有任何授权。`——**"平台权限"与"任何授权"是手册层的原话**，它与 T2 的 `launch constraint protections…execute malicious code with elevated privileges`（3 段）在语义上相邻，但本篇不去断言二者指同一实现。

另有一句与本选题关系最直接，也最容易被当作"检查已失效"的证据：`作为跨平台的附加控制，沙盒化可阻止 App 未经授权访问用户数据。`（`App 安全性概览`）手册用"可阻止"（helps prevent 一类措辞），T2 用 `A shortcut may be able to bypass sandbox restrictions`(11 段)。**一层的"可阻止"与另一层的"可能越过"并不矛盾**：前者是设计目标句，后者是单条缺陷的后果句。把两者写成"手册撒谎"是本选题的第三种误读。

---

# 13 `Gatekeeper`：53 段对 0 次

这是本篇最锋利的一个对照，数字都已在前面给出：

- T2：`gatekeeper` 词族 **53 段 / 30 页 / 16 个段落名**；Impact 侧 **53 段**；与 `bypass` 同段共现 **53 段**。三个数相同，即**该词族与"绕过"完全绑定**。
- T2 宾语侧：`Gatekeeper checks` 43 段 + `Gatekeeper` 10 段 = 53 段，**没有第三种搭配**。
- T1：`Gatekeeper` 英文字面在 178 页可用正文域内 **0 次**；"门控"造词也 **0 次**。

由此可写的三句：**(1)** 在镜像语料里，Gatekeeper 唯一的公开出场方式就是"被绕过"；**(2)** 中文手册层完全没有这个词；**(3)** 它属于 macOS 面，不能计入 iOS 的访问控制结论。

不可写的两句：**"Gatekeeper 形同虚设"**（53 段是缺陷登记数，不是绕过成功率，且模板高度复用——`An app may bypass Gatekeeper checks` 一句就占 19 段）；**"Apple 不保护 Gatekeeper"**（T1 的 0 次是**中文手册**的取舍，macOS 的启动策略在 T1 另有页面，`系统完整性保护` 一页的"访问控制"就有 5 次，本篇在 10 章已计入 MAC 专名）。

顺带登记一个易混点：`code signing enforcement` 5 段是 B 类里唯一与 iOS 直接相关的成员，其主语是 `A maliciously crafted app may be able to`(5 段) —— 与 `Shortcuts`/`App` 类主语同型，说明 iOS 侧的签名强制被越过后，公开措辞落在"恶意 App"这一前提上，而不是"远程内容"。

---

# 14 在野声明的错位

全语料在野声明两句式：`may have been exploited` **75 段 / 53 页 / 10 个段落名**；`actively exploited` **18 段 / 13 页 / 3 个段落名**。两个数都是 Impact 侧命中、Description 侧 **0**——在野声明只写在后果栏。二者相加 75 + 18 = **93 段**，与 #109 登记的"全语料在野声明 93 段 / 12 个段落名 / 15 个 CVE 编号"在段数上完全一致（段落名口径本篇取 10 + 3 = 13，比 #109 的 12 多 1，说明有一个段落在两句里被分别计数，本篇未合并，如实登记）。

与本篇主集的交集：**288 段 Impact 侧 `bypass` 里，20 段带 `may have been exploited`，0 段带 `actively exploited`。** 这 20 段可完全拆成两个模板：

- `An attacker with arbitrary kernel read and write capability may be able to bypass kernel memory protections. Apple is aware of a report that this issue may have been exploited.`——**16 段**；
- `…may be able to bypass Pointer Authentication. Apple is aware of a report that this issue may have been exploited in an extremely sophisticated attack against specific targeted individuals on iOS.`——**4 段**。

16 + 4 = 20，两句式的宾语都落在第 4 章 **C 类**。因此可以写：**A、B、D、E、F、G、H 七类共 236 段，在野声明 0 段。** 也可以写：C 类 52 段中 20 段带在野声明，比例 38.5%，是八类里唯一非零者。

不能写的两句：**(1)**"其余七类没有在野利用"——只能写"其余七类的顾问页文本没有公开在野声明"；**(2)**"这 20 段涉及 20 个漏洞"——`Kernel` 与 `RTKit` 名下整句相同（第 7 章），同一 CVE 常被拆成多段，20 段对应的 distinct CVE 数本篇未单独切出（第 17 章 G8）。

还有一个必须点明的同名陷阱：那 4 段的 `Pointer Authentication` 是**指针认证**，与第 12 章 F 类的 `authentication policy`（用户认证）不是一个对象。T2 里 `authentication` 一词双义这一事实由 #109 首先量化（17 段指针认证 vs 用户身份核验），本篇的 F 类 14 段全部属于后者，与 C 类的 17+1 段（PA / KASLR）互不重叠——两个数各归各类，第 4 章的分区是按宾语字符串整体归类的，不会把 PA 混进 F。

---

# 15 三个交叉验证

本篇的主数全部来自同一次普查，但下面三组**独立口径**互相对上了，可作为解析器正确性的证据。

**验证一（窗口数 = 段数）**：Impact 侧含 `bypass` 的段落 **288**，逐段取出的宾语窗口总数 **288**。若切块刀多切或少切，或 `||` 拆句失败，这两个数会分开；它们相等，说明每段恰好一个 `bypass` 动词、且窗口提取没有落空（落空计数 0）。这一条不只靠"两数相等"反推：直接按 `||` 拆句统计，288 段里 **`bypass` 子句总数也是 288**，同段出现两个 `bypass` 子句的段 **0** 段，有 `bypass` 却取不到宾语的段 **0** 段——三个数各自独立测得，与窗口总数互证。

**验证二（分区和 = 主集）**：八类段数之和 **288 = 288**，未归类 **0**；八类并集的页数 **111** 与 `bypass` 词族 Impact 侧页数 **111** 相同；八类并集的段落名 **55** 与 Impact 侧段落名数 **55** 相同。

**验证三（与上一轮复现）**：全语料分母 9,045 段 / 470 名与 #106–#109 一致；`bypass` 词族 Impact 侧 288 段 / 111 页 / 88 个 CVE 与 #109 登记的绕过词族三项完全一致；在野声明 93 段与 #109 一致（段落名口径差 1，已在第 14 章登记）。

有一处**没对上**，如实登记：#109 曾写"绕过词族 298 段 / 111 页"，而本篇按字段拆为 298（字段文本）/ 288（Impact）；298 − 288 = **10 段**只出现在 Description 或致谢里（其中 31 段命中 Description 计数的部分是 `bypass` 作为名词出现在 `A file quarantine bypass was addressed…` 这类修复语里，27 段正是 `file quarantine bypass`）。这两个数不冲突，是不同字段口径，但引用时必须写清是哪一个。

---

# 16 正面回答"权限检查"这件事

把全篇压成五句：

**第一句**：Apple 的公开文本里没有"权限检查"这个对象名。`permission check` 构词 0 段；`access control` 21 段全在修复栏、与 `bypass` 零共现。**所以"绕过访问控制或权限检查"这句话不能被直接量化，只能被改写。**

**第二句**：可量化的改写是 `bypass` 动词的 288 段宾语，它们闭合为 8 类，第一名是**用户在设置里给出的同意与隐私选项（95 段）**，第二名是 **macOS 的启动前校验（58 段）**，第三名是**已经越权之后还要再越一次的内存与指针防护（52 段）**。

**第三句**："检查"（`checks`）在公开文本里主要是**修复动作**（994 段 `improved checks`，Impact 侧 0 段），把它当失效对象会得出错误数量级。

**第四句**：手册层对"授权"给了最强的一句定义（签名键值对、无法更改），而"授权"对应的 T2 词族 `entitlement` 与 `bypass` 同段只有 **2 段**。**公开承诺的强度与公开失效的声明数之间，在本选题上是反向的。**

**第五句**：本选题的在野信号完全集中在 C 类（20 段，其余七类 0 段），这既不支持"访问控制面没有在野利用"，也不支持"访问控制面已被大规模利用"，它支持的只是：**在顾问页文本里，被公开承认在野的绕过，是越权之后针对内存与指针防护的绕过。**

---

# 17 缺口清单（G1–G14）

G1 实现层未核对：本篇全部结论来自两层公开文本，XNU 与用户态开源部件源码本轮未纳入取数，凡"检查发生在哪个进程/哪条路径"均无结论。
G2 平台字段未取：镜像文件名只有页面编号，没有平台标签；B 类的 macOS 归属由段落名与句式（Sidecar、Automator、Login Window、Xcode）判定，属**推断而非字段**。
G3 日期未映射：第 6 章只用编号区间，未把编号映射到发布日期，因此"止于 122718"不能写成"某年之后不再发生"。
G4 段落名与类别的对应表未建：第 7 章刻意不推断 61 段落在哪几类，因缺一张名字×类别的二维表。
G5 20 段的 distinct CVE 未切：在野声明 20 段对应多少个不同 CVE，本篇未计算。
G6 `check(s)` 的致谢侧 2 段未读：1,267 段中 2 段仅命中致谢文本，样本过小未列举。
G7 `privilege` 448 段 Impact 侧未拆句式：与 #101 的权限提升口径存在重叠，本篇只在第 2 章用其字段位置，未做分区。
G8 `authorization` 198 段与 `access control` 21 段的关系未逐段核：那 18 段 `An authorization issue…` 的 Impact 原文是什么，本篇只核到"不含 access control、不含 bypass"。
G9 T1"校验"19 次 / 3 页、"保护级别"1 次 / 1 页未逐页展开，只在第 10 章给数。
G10 `Apple Intelligence security prompts` 单段（1 段）的页面与日期未定位。
G11 H 类单段（`clickjacking protections for secure prompts`，页面 149035 `Foundation`）未核对同页其它段是否同类。
G12 致谢段落与 CVE 的对应未建：288 段每段都能取到 CVE 编号，但编号位于哪个字段未拆分统计。
G13 "同一段多 Impact 子句"的规则未触发：本期窗口数 = 段数，说明每段只有一个 `bypass` 子句；若镜像更新出现两个以上，本篇的取"首个窗口"规则会漏计，需在复算时先跑该断言（附表 D 第 6 项）。
G14 178 页正文窗口行数 9–176，"命中 0 次"未做窗口长度归一化；如需跨页比较词密度，本篇数字不支持。

---

# 18 不可外推清单

1. **不能**由 `access control ∩ bypass = 0` 写"访问控制没有被绕过"。只能写"这两个词在顾问页从不同段共现"。
2. **不能**由 994 段 `improved checks` 写"检查机制失效 994 次"。它是 Description 栏的修复语命中数。
3. **不能**由 288 段写"288 个漏洞"。42 种句式模板高度复用，同一 CVE 常拆多段、多段落名；本篇的独立计数是 88 个 CVE。
4. **不能**由 A 类 95 段居首写"iOS 最大的访问控制问题是隐私设置"。类别按句子宾语划分，与风险量级无关，且 30 段以上的类别都跨几十页。
5. **不能**把 B 类 58 段计入 iOS。`Gatekeeper` 是 macOS 组件，T1 中文手册层 0 次，T2 iOS 侧只有 `code signing enforcement` 5 段。
6. **不能**由 C 类止于编号 122718 写"内核防护后来没被绕过"。这是措辞迁移，不是事件停止。
7. **不能**由 20 段在野声明写"其余七类干净"。缺的是**公开声明**，不是利用本身。
8. **不能**把 `Pointer Authentication` 与 `authentication policy` 当同一个 `authentication`。前者在 C 类 17 段，后者在 F 类 7 段。
9. **不能**把手册"授权已经过数字签名，因此无法更改"当作不可越过的保证；也不能用 T2 的 2 段共现反驳它。两层陈述的对象不同。
10. **不能**由"sandbox 词族 411 / Impact 348 / 与 bypass 共现 29 / 作宾语 13"任选一个当"沙盒逃逸总数"。四个数分属不同集合（第 7 章）。
11. **不能**用本篇支持任何越过方法、时序、属性构造或提示规避细节。所引 CVE 与编号只用于说明公开修复事实与批次分布。
12. **不能**把段落名读成实现层。`Sandbox` 名下 11 段是**归类动作**，不等于"沙盒模块存在这 11 个缺陷"。

---

# 19 自我纠错登记

本篇在成稿前推翻了四次自己的取数，逐条登记。

**（1）`restrictions` 先测 15 段，重测 672 段。** 首版正则写成 `restriction(?![\w-])s?`——把复数后缀放在否定前瞻**之后**，导致 `restrictions` 永远无法匹配（前瞻在 `s` 处失败），只数到单数 15 段。改成 `restrictions?(?![\w-])` 后得 **672 段 / 128 页 / 97 名，Impact 侧 48 段**。若沿用首版，第 2 章"修复组"的比例会全部失真。

**（2）宾语窗口首轮命中 0。** 首版窗口正则要求每个词后必须跟空格、并以 `[.;]` 或句末收尾，而真实句子里最后一个宾语词不带尾随空格，导致**窗口总数 0**。改为**先按空白切词、再按标点截断**的分词法，窗口总数立刻等于 288（第 15 章验证一即由此而来）。

**（3）"段落名数"曾被写成 9,045。** 首版把段落名 keyed 在段落序号上，导致名数与段数同值。改为按 `gb-header` 文本作键后得 **470 名 / 55 名（Impact 侧 bypass）**。

**（4）页数 257 与 246 曾被混用。** 257 是镜像文件数（排除 `122085` 后），**246 是能切出至少一个段落的页数**。首版把两者当同一个数，凡"页"未加限定的句子已全部改写；第 1.1 节把两个分母并列钉死。

**（5）一处被自家推断诱出的过度表述。** 初稿曾写"那 4 段带极端复杂攻击声明的句子在段落名 `RPAC` 名下"。逐段核对未完成，本篇改为只登记两个独立事实：C 类里该句式 **4 段**；段落名 `RPAC` 在 288 段中共 **4 段**。两者数值相同，但**是否同一组本篇未证**（第 14 章按此表述）。

---

# 附表 A 288 段的 29 种 `bypass` 宾语逐条清单（29 行）

| # | 宾语（Impact 原句片段，大小写按原文归一） | 类别 | 段数 |
|---|---|---|---|
| 1 | Privacy preferences | A | 58 |
| 2 | certain Privacy preferences | A | 30 |
| 3 | Gatekeeper checks | B | 43 |
| 4 | kernel memory protections | C | 23 |
| 5 | Pointer Authentication | C | 17 |
| 6 | sandbox restrictions | E | 13 |
| 7 | Internet permission requirements | D | 12 |
| 8 | ASLR | C | 11 |
| 9 | Gatekeeper | B | 10 |
| 10 | Same Origin Policy | D | 10 |
| 11 | network filters | D | 8 |
| 12 | authentication policy | F | 7 |
| 13 | network restrictions | D | 7 |
| 14 | sensitive Shortcuts app settings | A | 6 |
| 15 | some web content restrictions | D | 6 |
| 16 | code signing enforcement | B | 5 |
| 17 | Content Security Policy | D | 4 |
| 18 | launch constraint protections and execute malicious | E | 3 |
| 19 | browser extension authentication | F | 2 |
| 20 | IPsec authentication and intercept network traffic | F | 2 |
| 21 | the Lock Screen | G | 2 |
| 22 | activation lock | G | 2 |
| 23 | Apple Intelligence security prompts | A | 1 |
| 24 | clickjacking protections for secure prompts | H | 1 |
| 25 | Digital ID authentication when iPhone mirroring | F | 1 |
| 26 | KASLR | C | 1 |
| 27 | login window during a software update | F | 1 |
| 28 | pairing | F | 1 |
| 29 | sender policy checks and deliver malicious | D | 1 |

第 18、29 两项带"and execute malicious / and deliver malicious"尾巴，是**六词窗口上限**切到句法边界之外的结果（第 4 章取数规则）；归类时按窗口头名（`launch constraint protections`、`sender policy checks`）判定，尾巴不参与归类。第 27 项窗口首词是小写 `login`，与 F 类里 `Login Window` 主语式句子是同一短语的不同大小写，本篇不做大小写合并（合并会使 F 类段数变化，故保留原样）。

---

# 附表 B 288 段的 28 种施事主语逐条清单（28 行）

| # | 主语窗口（`bypass` 之前，逐字） | 段数 | 前提分类 |
|---|---|---|---|
| 1 | An app may be able to | 102 | 本地已安装代码 |
| 2 | a shortcut may be able to | 29 | 本地已安装代码 |
| 3 | an app may | 19 | 本地已安装代码 |
| 4 | an attacker with arbitrary kernel read and write capability may be able to | 16 | 已具备读写能力 |
| 5 | a malicious application may be able to | 14 | 本地已安装代码 |
| 6 | a maliciously crafted zip archive may | 10 | 本地制品 |
| 7 | processing maliciously crafted web content may | 10 | 远程 |
| 8 | a remote attacker may be able to | 9 | 远程 |
| 9 | an attacker with arbitrary read and write capability may be able to | 9 | 已具备读写能力 |
| 10 | a malicious attacker with arbitrary read and write capability may be able to | 8 | 已具备读写能力 |
| 11 | an archive may be able to | 7 | 本地制品 |
| 12 | an attacker that has already achieved kernel code execution may be able to | 7 | 已具备读写能力 |
| 13 | an attacker on the local network may be able to | 7 | 远程（局域网） |
| 14 | a user may be able to | 6 | 用户 |
| 15 | a maliciously crafted app may be able to | 5 | 本地制品 |
| 16 | a website may be able to | 4 | 远程 |
| 17 | a maliciously crafted disk image may | 4 | 本地制品 |
| 18 | a malicious app may be able to | 3 | 本地已安装代码 |
| 19 | a malicious jar file may | 3 | 本地制品 |
| 20 | a malicious application may | 3 | 本地已安装代码 |
| 21 | an automator quick action workflow may be able to | 3 | 本地制品 |
| 22 | an attacker in a privileged network position may be able to | 2 | 远程（网络位置） |
| 23 | a user with physical access to an ios device may be able to | 2 | 物理访问 |
| 24 | an attacker with physical access to a macos device with sidecar enabled may be able to | 2 | 物理访问 |
| 25 | on mdm managed devices, an app may be able to | 1 | 企业托管限定 |
| 26 | a person with physical access to a mac may be able to | 1 | 物理访问 |
| 27 | an attacker with physical access to a locked device may be able to | 1 | 物理访问 |
| 28 | a malicious bluetooth input device may | 1 | 本地外设 |

按"前提分类"列求和：本地已安装代码（1/2/3/5/18/20 项）+ 本地制品（6/11/15/17/19/21 项）+ 本地外设（28 项）= **203 段**，已具备读写能力（4/9/10/12 项）= **40 段**，远程含局域网与网络位置（7/8/13/16/22 项）= **32 段**，用户与物理访问（14/23/24/26/27 项）= **12 段**，企业托管限定（25 项）= **1 段**；**203 + 40 + 32 + 12 + 1 = 288**，与第 9 章的四分类逐项同值，本表不引入新集合。

---

# 附表 C 手册层"访问控制"逐页清单（9 行）

| # | 页号 | 页面标题 | "访问控制"次数 | 其中"强制访问控制" |
|---|---|---|---|---|
| 1 | secb7ea06b49 | 系统完整性保护 | 6 | 5 |
| 2 | secb0694df1a | 钥匙串数据保护 | 2 | 0 |
| 3 | secd0da50a09 | 数字签名和加密 | 2 | 0 |
| 4 | sec35dd877d0 | App 安全性概览 | 1 | 1 |
| 5 | sec3ad8e6e53 | macOS 中的 App 代码签名进程 | 1 | 1 |
| 6 | sec59b0b31ff | 安全隔区 | 1 | 0 |
| 7 | secb9cb3140c | 接入无线网络时的隐私功能 | 1 | 0 |
| 8 | secc7d85209d | watchOS 系统安全性 | 1 | 0 |
| 9 | sece3bee0835 | 加密和数据保护概览 | 1 | 0 |

次数列合计 **16**，与第 10 章表内"访问控制 16 次 / 9 页"一致；"强制访问控制"列合计 **7**，即第 10 章"16 次里 7 次属 MAC"的出处。页面标题取自各转储文件首个非空行并去掉站点后缀，标题用词以转储为准（`macOS 中的 App 代码签名进程`、`watchOS 系统安全性` 是本层原写法，非笔误）。

---

# 附表 D 复算验收断言集（14 行）

| # | 断言 | 本期实测 | 不通过时先查什么 |
|---|---|---|---|
| 1 | 镜像文件数 258，排除 `122085` 后 257 | 257 | 目录列表与排除项 |
| 2 | 全语料段数 = 9,045 | 9,045 | 切块刀四锚点是否齐全 |
| 3 | 含页免责声明的块数 = 0 | 0 | `disclaimer` / `footer` 锚点 |
| 4 | 段落名数 = 470 | 470 | 名键是否误用段序号 |
| 5 | 能切出段落的页数 = 246 | 246 | 257 与 246 是否被混用 |
| 6 | 每段 Impact 内 `bypass` 出现次数 = 1 | 成立 | 多子句段落的窗口取法 |
| 7 | 宾语窗口总数 = 288 | 288 | 分词截断规则（勿用尾随空格正则） |
| 8 | 八类段数之和 = 288、未归类 = 0 | 288 / 0 | 大小写合并是否改变段数 |
| 9 | 八类并集页数 = 111、并集名数 = 55 | 111 / 55 | 共现矩阵是否用了计数而非集合 |
| 10 | `access control` 21 段、Impact 侧 0、与 bypass 交集 0 | 21 / 0 / 0 | 词边界与字段归属 |
| 11 | `permission check` 构词 = 0 段 | 0 | 宽松共现复核（permissions∩bypass 应为 37） |
| 12 | `restrictions` 族 = 672 段（复数后缀在前瞻之内） | 672 | 正则顺序（第 19 章第 1 条） |
| 13 | 在野声明 75 + 18 = 93 段 | 93 | 两句是否被去重合并 |
| 14 | T1：179 页转储、可用 178 页、窗口 9–176 行；"访问控制"16 次 / 9 页 | 全部成立 | `sec2727afbbc` 是否被计入分母 |

---

# 附表 E 错误写法与纠正对照（13 行）

| # | 会被写出的错误句子 | 纠正后的写法 |
|---|---|---|
| 1 | "顾问页里访问控制类漏洞只有 21 个" | `access control` 字面 21 段，全部在 Description；后果侧的对应集合是 288 段 `bypass` 宾语 |
| 2 | "1,240 次权限检查被绕过" | `check(s)` 1,267 段中 1,240 段在 Description，其中 994 段是 `improved checks` 修复语，Impact 侧 51 段 |
| 3 | "288 个绕过访问控制的漏洞" | 288 段 / 42 种句式 / 88 个不同 CVE，段数不等于漏洞数 |
| 4 | "沙盒逃逸 411 起" | 411 是 `sandbox` 词族字段文本段数；作 `bypass` 宾语 13 段，挂 `Sandbox` 名 11 段，与 bypass 共现 29 段 |
| 5 | "iOS 上 Gatekeeper 被绕过 53 次" | 该 53 段属 macOS 批次措辞，iOS 侧签名强制对应 `code signing enforcement` 5 段 |
| 6 | "Apple 手册从不讲访问控制" | T1 有 16 次 / 9 页，其中 7 次是 MAC 专名；通用语义用"授权 172 / 权限 102 / 沙盒 27"承载 |
| 7 | "授权属性经常被绕过" | `entitlement` 131 段与 `bypass` 同段仅 2 段 |
| 8 | "绕过访问控制的多是远程攻击者" | 本地前提 203 段（70.5%），远程 32 段（11.1%），已具备读写能力 40 段（13.9%） |
| 9 | "身份验证被绕过 7 类里最常见" | F 类 14 段仅占 4.9%；A 类隐私设置 95 段才是第一名 |
| 10 | "内核内存防护后来没人绕了" | C 类止于编号 122718 是**措辞迁移**，镜像无日期字段可支撑"停止发生" |
| 11 | "在野利用集中在访问控制方向" | 288 段中仅 20 段带在野声明，且全部落在 C 类；其余七类公开声明为 0，不等于没有利用 |
| 12 | "`authentication` 被绕过 17 段" | 那 17 段是 `Pointer Authentication`（C 类），与 F 类 `authentication policy` 7 段不同对象 |
| 13 | "手册说沙盒可阻止未授权访问，顾问页却说被绕过，所以手册不实" | 前者是设计目标句、后者是单条缺陷后果句，两层可同真；本篇只登记措辞差异 |

---

**边界声明：** 本报告全部技术结论来自两层可核对的公开文本：《Apple 平台安全保护》官方指南（中文 179 页全量转储，正文域切分后 178 页可用，逐词普查，窗口 9–176 行）与 Apple 安全响应顾问页（257 份英文页面镜像，容错解析后全语料 9,045 段 / 470 个段落名 / 246 页可切出段落；本篇绕过词族字段文本侧 298 段 / 111 页 / 57 名，Impact 侧 288 段 / 111 页 / 55 名 / 88 个 CVE 编号 / 42 种句式 / 28 种施事主语，`access control` 字面族 21 段 / 8 页 / 5 名，`restrictions` 族 672 段 / 128 页 / 97 名，`checks` 裸词族 1,267 段 / 195 页 / 165 名，全语料在野声明 75 + 18 = 93 段），逐处标注页号、字段与原文片段；顾问页镜像在本轮只提供页面编号，不提供发布日期，因此第 6 章的所有"新旧"表述一律限定为编号先后，不写成日期结论；本手册每个被引页面同时存在内容日期与页面发布日期两个字段，凡本篇引用手册页处均只引页号与标题，不涉及日期字段。第 1、2、3、4、5、6、7、8、9、10、13、14、15 章的统计与第 4 章的八类闭合分区，为本报告对公开文本的自建语料测量结果，不是 Apple 的表述；这些数字依赖本报告解析器的切块锚点、字段归属与词边界规则，复算须先跑附表 D 全 14 项断言，其中第 2、3、7、8、12 项为本篇新增。源码层本轮未纳入，凡涉及判定发生在哪个进程、哪条内核路径、阈值常量落点的表述均已降级为"顾问页将其归入某段落名"或"手册层如此陈述"，并在第 17 章 G1 登记。`bypass`、`check`、`authentication`、`sandbox` 在本报告语料中均存在多义，凡涉及这些词处均已写明取的是 Impact 侧、Description 侧还是字段并集侧。所引 CVE 与页面编号仅用于说明公开修复事实与批次分布，不含绕过方法、不含利用代码、不含任何一类访问控制的越过路径或时序、不含授权属性构造与签名校验规避细节、不含提示语规避的操作指引，也不含针对他人设备的任何操作步骤。所有研究者姓名仅出现在顾问页致谢字段中，其研究结论本轮未核对，不构成本报告依据。如需就本报告任一结论索取取数脚本或段落级原始文本，或指出任何事实与措辞问题，请联系。

Telegram: https://t.me/one00190
