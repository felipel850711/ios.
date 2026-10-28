@@TITLE@@iOS 被传递、处理或执行的数据与代码
@@SUBTITLE@@选题给出**三个动词（传递、处理、执行）× 两个名词（数据、代码）**，并声明"具体含义依上下文而定"。逐词测量之后的结果是：**"传递"在 Apple 安全响应顾问页的英文措辞层里一个词都没有**——附表 B 的 61 个候选词与词组里 **33 项三字段全零**，其中表示传递的整组动词（`pass` 词族、`transfer`、`marshal`、`serialize`、`encode`、`decode`、`ingest`、`consume`、`fetch`、`transport`、`pipe`、`channel`、`handoff`）**各 0 段**。**"处理"由 21 个句首动名词独立承担，覆盖 2,179 段 / 7,434 段含 Impact 的 29.3%**，而 Description 侧以动名词开头的段数是 **0**。**"执行"只有三个词形**（`execution` 240 + `execute` 121 + `executable` 3 = **364**），被动形 `executed`、`executing` **各 0**，`execute` 之后能出现的名词**只有 `arbitrary`（118 次）与 `malicious`（3 次）**。**"数据"与"代码"在 Impact 侧是两套永不相交的段落**：`data` 1,240 段 ∩ `code` 363 段 = **0**（并 1,603），`execute` 361 段 ∩ `data` = **0**。所以"依上下文而定"不是一句免责声明，而是**三把必须先落下的刀**：进程刀（`process` 的 766 次命中里 **683 次说的是"进程"不是"处理"**）、同义刀（`web content` 895 段与 `contents of` 18 段交集 **0**，前者与 `data` 交集也是 **0**）、接缝刀（同一批段落用"三字段拼接"统计 `data` 只得 **440 段**，用"逐字段并集"得 **1,403 段**，十个词合计**静默丢 1,400 段**）。
@@INFO@@报告日期|2026 年 10 月 28 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 7,434 段含 Description / 9,132 段含致谢**。传递动词集 T = 0 段；句首动名词集 G = **21 词 / 2,179 段 / 224 页 / 98 名**；execut 三形集 X = **364 段 / 128 页 / 50 名**（Impact 侧 361）；data 集 D = 全字段 **1,403 段**、Impact 侧 **1,240 段 / 158 页 / 160 名**；code 集 C（Impact 侧）**363 段 / 128 页**；maliciously crafted 集 F = **1,769 段 / 218 页**；对照集 E = `exploit` 词族 **108 段**（#114 手段全集）、P = `attacker` 词族 **890 段**（#113 主语全集）。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**公开文本用哪些动词、哪些词形、哪些句法位置来表达"某样东西被交给系统、被系统消化、被系统运行"这三件事**，属于措辞与文本计量分析
@@INFO@@分类|传递动词**电池 61 项 / 全零 33 项 / 非零 28 项**（附表 B）/ **句首动名词 21 词矩阵**（附表 A，2,179 段、含 crafted 1,680 段 = 77.1%）/ crafted 宾语**头名词 23 项闭合 = 1,760 次**（附表 C）/ data 的 Impact **句式九分区闭合 = 1,240**（附表 D）/ **三把刀**：进程刀（683 ∶ 766）、同义刀（895 ∩ 18 = 0）、**接缝刀**（十词丢 1,400 段）/ **词形闭合**：process 四形 2,545 次命中、1,897 段并集**差 0**；execut 五形 **240 + 121 + 3 + 0 + 0 = 364**；crafted 与 packet **交集 0**
@@INFO@@难度|进阶（第 6 章的 `D ∩ C = 0` 是**成员集合求交**的结果，不能由 1,240 与 363 两个数推出；第 7 章的接缝刀只在**字段末尾没有标点**时发作，是脚本层面的静默低估而不是语料现象；第 8 章的"进程刀"要求把 `process` 的宾语窗口展开到后 1–2 词才能判定词性；第 11 章 17 段"少一个 -ly"要靠**词界 `crafted` 与词组 `maliciously crafted` 两个计数相减**才会现形；第 13 章的九分区是**按序首次命中**的划分，改判定顺序就会改变归档）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

选题字面是"被传递、处理或执行的数据与代码，具体含义依上下文而定"。这是一个**被动语态的三选一**：它假设公开文本里存在"某个东西被交给系统 / 被系统消化 / 被系统运行"这三类陈述，并且承认同一批名词（data、code）在三类陈述里的角色不同。逐词测量之后，本篇的结论是：**第一类陈述不存在，第二类陈述承担了全部表达任务，第三类陈述只允许一种宾语**，而"依上下文而定"必须由测量者自己用三把刀落实。

五组反差。

**反差一（"传递"整组为零）**：附表 B 列出 61 个候选词与词组，**33 项在 Impact、Description、致谢三个字段里一段都没有**。其中表示"把一样东西交出去"的动词组**全部为零**：`pass` 词族（`pass / passes / passed / passing`）**0 段**、`transfer` **0**、`marshal` **0**、`serialize / deserialize / serialization` **0**、`encode / decode` **0**、`ingest` **0**、`consume / fetch` **0**、`transport` **0**、`pipe` **0**、`channel` **0**、`handoff` **0**。剩下的近义动词只有 **6 个非零项**：`send` **34 段**（Impact 24、Description 10）、`relay` **20**、`receive` **6**、`deliver` **4**（其中 3 段在致谢）、`transmit` **1**、`forward` **1**。**没有一段在描述"缺陷把数据传给另一个组件"这件事**（第 3 章）。

**反差二（"处理"完全由句首动名词承担，Description 侧一个都没有）**：Impact 首句以 `…ing` 开头的段共 **2,179 段 / 224 页 / 98 个块名**，占 7,434 条含 Impact 段的 **29.3%**，词表**恰好 21 个**（附表 A）。其中 `Processing` 一个词就占 **1,760 段 / 219 页 / 67 名**，`Parsing` 146、`Visiting` 82、`Connecting` 58、`Mounting` 40，其余 16 词合计 93 段。**而 Description 字段里以动名词开头的段数是 0**——Description 的 7,434 段首词只有 `a`(3,058)、`the`(1,561)、`an`(1,552)、`this`(1,248)、`multiple`(15) 五个，闭合到 7,434。**"处理"是 Impact 独有的句法**：它总写在段首、总带一个 `maliciously crafted` 的宾语（附表 A 的 2,179 段里 **1,680 段首句含 crafted = 77.1%**）（第 4 章）。

**反差三（"执行"只有三形、一种宾语）**：`execut-` 词族在全树的词形分布是 `execution` **240 段**、`execute` **121 段**、`executable` **3 段**、`executed` **0**、`executing` **0**，三形并集 **364 段 / 128 页 / 50 名**。**被动形与进行形为 0**意味着措辞层**从不写"代码被执行"**。`execute` 之后能出现的名词只有两个：`arbitrary`（**118 次**）与 `malicious`（**3 次**），合计 121 次＝`execute` 的全部命中；`execute code`、`execute data`、`data execution`、`run code`、`inject code`、`instructions` **各 0 段**。反过来 `code` 在 Impact 的 363 段里，**336 段含 `arbitrary code`、237 段含 `code execution`，两者都不含的只有 8 段**（第 5 章）。

**反差四（data 与 code 在 Impact 侧互斥）**：含 `data` 的 Impact 段 **1,240**、含 `code` 的 Impact 段 **363**，**交集 0 段、并集 1,603 段**。同一条段不会既写"数据被读到"又写"代码被执行"。再加三条：`execute` 侧 **361 段 ∩ data = 0**、`data ∩ E(108) = 0`、`code ∩ E = 36`。**"被处理的东西"在措辞层不是 data 也不是 code，而是 `maliciously crafted` 修饰的那 23 类头名词**（`content` 793、`file` 521、`image` 184、`font` 46……闭合到 1,760，附表 C）（第 6、10 章）。

**反差五（"依上下文而定"是三把刀，不是免责声明）**：**进程刀**——`process` 词形 766 次命中里，后接 `memory / crash / termination` 的 **683 次说的是进程**，`process memory` 326 段**全部落在 Impact**，"process＋进程名"句式共 **673 段 / 154 页**，与 `processing`（1,769 段）交集 610、差 1,159；`processes` 的 10 段**全部是名词复数**（Kernel `shared between processes` ×8、MetricKit `a root processes` ×2），**`processed` 为 0**。**同义刀**——`data` 1,403 段、`information` 575、`content(s)` 1,004，但 `web content` **895 段 / 167 页** 与 `contents of` **18 段 / 17 页** 交集 **0**，且 `web content ∩ data = 0`、`contents of ∩ data = 7`（这 7 段就是 `data ∩ content(s)` 的全部）。**接缝刀**——同一批段落，把 Impact∥Description∥致谢拼成一个文本块再用词边界正则匹配，`data` 只报 **440 段**（并集 1,403，**丢 963**）、`information` 303（575，丢 272）、`file` 1,055（1,170，丢 115）、`packet` **0（20，全丢）**，十个词合计**静默丢 1,400 段**（第 7–9 章）。

于是本篇的问题依次是：

**问题一（第 3 章）** "传递"到底有没有被写过。答案：**整组动词 0 段**，只有 6 个近义动词非零且语义各属别处（`relay` 20 段全部是 APNs 中继与蓝牙中继语境、`deliver` 的 4 段里 3 段在致谢里指"投递补丁说明"）。

**问题二（第 4–5 章）** "处理"与"执行"分别占据哪个句法位置、允许哪些宾语。答案：处理**固定在 Impact 段首**（2,179 段、21 词，Description 侧 0）；执行**固定在 Impact 段尾的后果位置**（`may be able to execute arbitrary code` 112 段），宾语唯一。

**问题三（第 6–13 章）** data / code / crafted 三套名词各自的真实角色与彼此关系。答案：**data 是"被读走的东西"**（1,240 段里 1,093 段是 `access…data` 句式，占 88.1%）；**code 是"被运行的东西"**（363 段里 336 段带 arbitrary）；**crafted 的宾语才是"被处理的东西"**（1,760 次命中、23 个头名词闭合）；三者两两交集分别是 **0 / 22 / 0**。

**问题四（第 15–17 章）** 与前几篇的集合接口、组件与页面分布、中文层对照。第 15 章：**D(1,240) ∩ E(108) = 0**，而 C(363) ∩ E = 36；**P(890) 与 D 交 35、与 X 交 49、与 F 交 12**——"攻击者"作为主语出现在执行句里远多于出现在数据句里。第 16 章：data 的 **160 个块名** Top 是 Siri 76、AppleMobileFileIntegrity 69、Shortcuts 64、WebKit 57、Spotlight 49；页内密度最高 **42 段/页**。第 17 章：中文手册层 `传递` **10 次 / 10 页**，逐窗口核对**全部是系统内部移交**（XPC 选项、launchd 参数向量、签名数据、证书、活动状态、视频数据），`有效载荷 / 反序列化 / 任意代码 / 网页内容 / 进程内存 / 泄漏 / 分享 / 非可信 / 被构造 / 特制` **各 0 次**。

# 1 操作性定义与分母

**定义一（段落、句子、字段）**：沿用 #111 第 1 章的两版式域与四锚点收块；一条"段"= 一个组件条目（块名 + Impact + Description + 致谢）。Impact 内部多句以 `" || "` 连接，本篇**先拆句再统计**，切句规则 `(?<=[.!?])\s+(?=["A-Z])`。**本篇的主统计量是"句首词"**，所以拆句规则直接决定附表 A：规则改了附表 A 就要重算。

**定义二（三动词的操作化）**：把选题的三个动词各自翻译成**可判定的表层形式**，不引入语料外推定——**传递** = 附表 B 中 `pass 词族` 至 `handoff` 的 **11 行判据、覆盖 14 个动词词位**（`pass / transfer / marshal / serialize / deserialize / encode / decode / ingest / consume / fetch / transport / pipe / channel / handoff`）；**处理** = **Impact 首句以 `^\s*\w+ing\b` 开头**（不维护词表，让词表从数据里长出来，得 21 词，附表 A）；**执行** = `(?<![\w-])(?:execution|execute|executable|executed|executing)(?![\w-])` 五形全测（得 3 形非零）。**定义三（两名词）**：`data` 与 `code` 均用**词边界**匹配，不用子串（避免 `data` 命中 `metadata`、`code` 命中 `encode`——后者在本语料里本来就是 0）。

**定义四（集合）**：**T** = 传递动词并集（**0 段**）；**G** = 句首动名词段集（**2,179**）；**X** = execut 三形段集（**364**）；**X_I** = execut 三形出现在 Impact 的段集（**361**）；**D** = 含 `data` 段集（全字段 **1,403**）；**D_I** = Impact 含 `data`（**1,240**）；**C_I** = Impact 含 `code`（**363**）；**F** = 含 `maliciously crafted` 段集（**1,769**）；**F'** = 含词界 `crafted` 段集（**1,786**）；**PK** = Impact 含 `packet(s)`（**20**）；**E** = #114 手段全集（**108**）；**P** = #113 `attacker` 主语全集（**890**）。

**分母声明**：全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 7,434 段含 Description / 9,132 段含致谢**；域 A **9,045 / 246 / 470 / 7,346**，域 B **91 / 11 / 26 / 88**。致谢有 **4 段为空**，凡读致谢必须写 `// ""`。本篇分域数字：**G 域 A 2,155 / 域 B 24**；**D_I 域 A 1,223 / 域 B 17**；**X 域 A 335 / 域 B 29**；**F 域 A 1,750 / 域 B 19**；`processing` 词形 **域 A 1,745 / 域 B 24**；**E 域 A 98 / 域 B 10**。域 B 的 24 段句首动名词**全部是 `Processing`**，没有一个其它词。

**算法口径（本篇唯一口径）**：任何"某词有 N 段"**一律逐字段分别匹配、命中即计入该段、再取并集**；**不做三字段拼接**。第 7 章给出拼接会丢多少段的量化对照，附表 B 的"全字段并集 / Impact / Description / 致谢"四列就是这套口径的产物。

# 2 结论速览（每条都可按第 19 章复算）

1. **附表 B 的 61 项里 33 项全零、28 项非零**；"传递"整组 13 词 **0 段**，非零近义动词只有 `send` 34、`relay` 20、`receive` 6、`deliver` 4、`transmit` 1、`forward` 1。
2. **句首动名词恰好 21 词**（附表 A），覆盖 **2,179 段 / 224 页 / 98 名 = 含 Impact 段的 29.3%**；`Processing` 1,760、`Parsing` 146、`Visiting` 82、`Connecting` 58、`Mounting` 40、`Opening` 21，其余 15 词合计 72 段；**21 词里只有 20 个是真动名词**，`incoming`（3 段）是形容词，代表句是 FaceTime 来电那句。
3. **Description 侧以动名词开头的段 = 0**；Description 的 7,434 段首词分布 `a` 3,058 + `the` 1,561 + `an` 1,552 + `this` 1,248 + `multiple` 15 = **7,434**，无剩余。
4. **附表 A 的 2,179 段里 1,680 段首句含 `maliciously crafted`（77.1%）**；`Processing` 一词的 1,760 段里 1,531 段含（87.0%）。
5. **execut 五形闭合**：`execution` 240 ＋ `execute` 121 ＋ `executable` 3 ＋ `executed` 0 ＋ `executing` 0 = **364**（三形并集段 364，**词形和＝并集，差 0**）；Impact 侧 361 段 / 128 页 / 50 名；`arbitrary code execution｜remote code execution` 220 段且**全部 ⊂ X_I**（361 − 220 = 141）。
6. **`execute` 的宾语唯一**：其后紧邻词只有 `arbitrary`（118 次）与 `malicious`（3 次），合计 121 = `execute` 全部命中；`execute arbitrary code` **118 段 / 68 页**；`may be able to execute` **112 段 / 67 页**；`execute code`、`execute data`、`data execution`、`run code`、`inject code`、`instructions` **各 0**。
7. **`code` 在 Impact 的 363 段构成**：`arbitrary code` 336、`code execution` 237、`remote code execution` 2，**两个都不含的只有 8 段**；`code` 前 26 字窗口最高频是 `le to execute arbitrary co`（112）与 `ermination or arbitrary co`（92）。
8. **`data` 与 `code` 在 Impact 侧互斥**：D_I 1,240 ∩ C_I 363 = **0**，并 **1,603**；另有 **X_I ∩ D_I = 0**、**D_I ∩ E = 0**、**C_I ∩ E = 36**。
9. **进程刀**：`process` 词形四形命中 **2,545**（`processing` 1,769 ＋ `process` 766 ＋ `processes` 10 ＋ `processed` 0），四形**段并集 = 1,897 = 词族并集，差 0**；裸 `process` 后接 `memory`(326) / `crash`(294) / `termination`(63) = **683 次是"进程"**；"process＋进程名"**673 段 / 154 页**，∩`processing` = 610、差 **1,159**；`process memory` 326 段**全在 Impact**；`processes` 的 10 段全是名词复数。
10. **同义刀**：`data` 1,403 / `information` 575 / `content(s)` 1,004；D∩info = **88**、D∩content(s) = **7**、info∩content(s) = 42；`web content` **895 段 / 167 页** 与 `contents of` **18 段 / 17 页 / 5 名** **交集 0**，且 `web content ∩ data = 0`、`contents of ∩ data = 7`（＝D∩content(s) 的全部）。
11. **接缝刀（十词）**：`data` 并集 1,403 → 拼接 440（**丢 963**）、`information` 575 → 303（丢 272）、`file` 1,170 → 1,055（丢 115）、`packet` 20 → **0（全丢）**、`credential` 29 → 16（丢 13）、`code` 756 → 746（丢 10）、`metadata` 9 → 3（丢 6）、`message` 41 → 40（丢 1）、`input` 与 `stream` 丢 **0**；**十词合计丢 1,400 段**。
12. **crafted 宾语头名词 23 项闭合 = 1,760 次**（附表 C）：`content` 793 ＋ `file` 521 ＋ `image` 184 ＋ `font` 46 = **1,544 = 87.7%**；其余 19 项 216 次；对应 **1,769 段 / 218 页**。
13. **`crafted` 少一个 -ly 的 17 段**：词界 `crafted` 1,786 段 − `maliciously crafted` 1,769 段 = **17 段**（libarchive 5 段 `malicious crafted file` ＋ Bluetooth 5 段 ＋ Wi‑Fi 7 段 `crafted Bluetooth/Wi-Fi packets`）；**`packet(s)` 20 段 / 20 页 / 3 名与 F 交集 0**。
14. **data 的 Impact 句式九分区闭合 = 1,240**（附表 D）：**A access 1,093（88.1%）**、B exfiltrate 51、C leak 32、D use 16、E output/display 14、F protect 5、**G alter 0**、H log/store 4、未归类 25；模板 distinct **39**，最高三句 `An app may be able to access sensitive user data` **644** ＋ `…access user-sensitive data` **212** ＋ `…access protected user data` **137** = **993 = 80.1%**。
15. **唯一把 data 当"被处理的输入"的句式恰好 8 段**：`Processing malicious data may lead to unexpected app termination`，页 **125884–125891**、块名 **全部 Foundation**、CVE **全部 CVE-2025-43532**、致谢同一批人；**1 名 / 8 页**。另外 `data that`、`data which`、`data to be`、`data being`、`the data`、`this data` **六个写法各 0 段**——措辞层从不给 data 加关系从句或被动后置。
16. **集合接口**：|P| = **890 段 / 160 页 / 112 名**；P ∩ D_I = **35**、P ∩ process 词族 = **27**、P ∩ X = **49**、P ∩ F = **12**；`attacker` 与执行句的交集是与数据句交集的 **1.4 倍**，与 crafted 句几乎不相交。
17. **框架动词**：`may be able to` **4,722 段**、`may lead to` **1,731 段 / 207 页**、`could lead to` 7、`can lead to` **0**、`may allow an attacker` 12；`unexpected app termination` **612 段 / 148 页**，`corrupt process memory` 189 段 / 82 页、`disclose process memory` 10 段。
18. **中文手册层（T1，178 页可用）**：`数据` **3,803 次 / 178 页 / 918 窗口**、`代码` 534 / 178 / 172、`执行` 427 / 178 / 248、`处理` 338 / 178 / 155、`传递` **10 次 / 10 页 / 10 窗口**（逐窗口核对全为系统内部移交）、`构造` **1**（密钥递归派生）、`精心` **1**（"精心设计的范围和时间限制"，非 crafted 义）、`序列化` **1**、`不可信` **1**、`缓冲区` **1**、`代码执行` **3 / 2 页**；**`有效载荷`、`载荷`、`反序列化`、`任意代码`、`网页内容`、`进程内存`、`泄漏`、`分享`、`被构造`、`特制`、`非可信`、`利用链`、`攻击链`、`零点击`、`沙箱`、`字节码`、`机器码`、`中间表示` 各 0 次**。

# 3 "传递"：整组动词为 0（附表 B 的 11 行传递判据）

选题的第一个动词在 T2 里**没有任何表层形式**。附表 B 用 **11 行判据覆盖 14 个动词词位**，**全字段并集列全部为 0**：

| 附表 B 行名 | 覆盖的词位 | 段数 |
|---|---|---:|
| `pass 词族（传递）` | pass / passes / passed / passing | **0** |
| `transfer` | transfer / transfers / transferred / transferring | **0** |
| `marshal` | marshal / marshalling / marshaled | **0** |
| `(de)serialize` | serialize / deserialize / serialization | **0** |
| `(en\|de)code` | encode / decode | **0** |
| `ingest` | ingest / ingestion | **0** |
| `consume/fetch` | consume / fetch | **0** |
| `transport` | transport / transportation | **0** |
| `pipe` | pipe / pipeline | **0** |
| `channel` | channel | **0** |
| `handoff` | handoff / hand-off | **0** |

这是一个**干净的否定**：不是"词被词边界切掉了"（`pass` 词族已展开四形），也不是"只在某个字段里为零"（三字段都查了）。需要留意的是 `pass` 的**子串**会命中 `bypass`、`password`、`compassing`，本篇用词边界排除；反过来 `password` 语境的命中不计入，因为它不属于"把某物交给某处"这个动作。同理 `channel` 与 `pipe` 的零值不含 `channel` 的组件名写法（本语料里也没有）。

六个近义动词非零，但逐个核对后没有一个在写"缺陷间的数据移交"：

- `send` **34 段**（Impact 24、Description 10）——语境是"应用可以把消息发送到 / 请求发送到"，是**功能描述**而非缺陷后果。
- `relay` **20 段**（Impact 20、页 17）——全部是 APNs 中继与蓝牙/网络中继的组件语境，属 #108 的通道话题。
- `receive` **6 段**、`transmit` **1 段**、`forward` **1 段**（Impact，Mail 转发邮件那条）。
- `deliver` **4 段**，其中 **3 段在致谢字段**（指"投递补丁说明/致谢"），Impact 只有 1 段。

**结论**：选题里的"被传递"这一栏，在公开措辞层是**空的**。如果只统计非零项来回答选题，会得到一个 6 项的小表；把它写成"数据传递路径"就是**引入语料外假定**。本篇按第 1 章定义四把它记作 **T = 0 段**。

# 4 "处理"：21 个句首动名词承担全部表达（附表 A）

附表 A 用**不预设词表**的方式测量：对每条段的 Impact **首句**取 `^\s*\w+ing\b`，命中的词自动进表。结果**恰好 21 个不同词、2,179 段**，占 7,434 条含 Impact 段的 **29.3%**。

| 排名 | 词 | 段 | 页 | 名 | 首句含 crafted |
|---:|---|---:|---:|---:|---:|
| 1 | Processing | 1,760 | 219 | 67 | 1,531 |
| 2 | Parsing | 146 | 65 | 19 | 47 |
| 3 | Visiting | 82 | 57 | 6 | 19 |
| 4 | Connecting | 58 | 16 | 5 | 0 |
| 5 | Mounting | 40 | 17 | 8 | 34 |
| 6 | Opening | 21 | 8 | 4 | 18 |
| 7 | Restoring | 18 | 13 | 4 | 17 |
| 8–21 | 其余 14 词 | 54 | — | — | 14 |

**四点观察。**

**（1）分布极度右偏。** 前两个词 1,906 段 = 全部 2,179 段的 **87.5%**；后 14 词每词不超过 8 段（`Playing` 8、`Deleting`/`Extracting`/`Running`/`Unpacking` 各 6、`Replying`/`Turning` 各 4、`Incoming`/`Copying` 各 3、`Cloning`/`Loading`/`Muting` 各 2、`Enabling`/`Forwarding` 各 1）。**这说明"处理"在 T2 里不是一个泛化概念，而是一条被反复套用的模板句**：`Processing <一个 crafted 对象> may lead to <后果>`。

**（2）句法位置唯一：只在 Impact。** **Description 侧以动名词开头的段 = 0**。Description 的 7,434 段首词分布是 `a` 3,058、`the` 1,561、`an` 1,552、`this` 1,248、`multiple` 15，**五者相加 = 7,434，闭合无剩余**。所以"处理方式"这个句式**只出现在影响描述里**，不出现在缺陷描述里——措辞层把"怎么处理"当作**后果的一部分**，而不是当作缺陷的性质。

**（3）一个词混进来了。** 21 词里 `Incoming`（3 段）**不是动名词**而是形容词（`Incoming FaceTime calls can appear or be accepted on a locked macOS device…`）。本篇保留它并在附表 A 注明，因为**判据是表层形式**（首词以 `-ing` 结尾），改成"必须能带宾语"就会掉到 20 词、2,176 段。这是"依上下文而定"的第一处**必须显式表态**的地方。

**（4）crafted 密度 77.1%。** 2,179 段里 **1,680 段**首句含 `maliciously crafted`；分词看：`Processing` 1,531/1,760 = 87.0%、`Mounting` 34/40、`Opening` 18/21、`Restoring` 17/18、`Extracting` 6/6、`Unpacking` 6/6、`Cloning` 2/2；而 `Connecting`、`Playing`、`Deleting`、`Running`、`Visiting` 之外多为 **0**。**"处理"与"精心构造"在措辞层几乎同义**——这是第 10 章附表 C 的入口。

宾语侧的形态（附表 A"宾语头名词 Top"列）也高度集中：`Processing` 的宾语头名词前三是 `content`(847)、`file`(313)、未取到(251)；`Parsing` 是 `file`(124)；`Visiting` 是 `website`(82)（**唯一一个词占满全部**，即 Visiting 后面永远接 website）；`Mounting` 分散在 `image`(6)/`volume`(5)；`Extracting`/`Unpacking` 全是 `archive`(6)。

# 5 "执行"：三形闭合、被动为零、宾语唯一

`execut-` 词族的五个词形全部单独测过：

| 词形 | 段（全字段并集） | 页 | 名 |
|---|---:|---:|---:|
| `execution` | 240 | 106 | 28 |
| `execute` | 121 | 70 | 25 |
| `executable` | 3 | 3 | 1 |
| `executed` | **0** | 0 | 0 |
| `executing` | **0** | 0 | 0 |
| 三形并集 | **364** | **128** | **50** |

**词形和 = 240 + 121 + 3 = 364 = 并集**，说明三个词形**没有一段共现在同一条段里**（脚本断言"三形并集段 = 364 且 executed/executing = 0"，附表生成器里是 `die` 条件）。**被动形与进行形为 0** 是本篇最干净的一条否定：**措辞层从不写"代码被执行"**，也从不写"正在执行"。

`execute` 的**紧邻词只有两个**：把它的全部 121 次命中按"后一个词"分类，得到 `arbitrary` **118 次**、`malicious` **3 次**，**合计 121，无剩余**；按"前一个词"分类得 `to` **112**、`unexpectedly` **6**、`and` **3**，合计 121。**因此 `execute` 在 T2 里是一个半固定搭配**：`may be able to execute arbitrary code`（**112 段 / 67 页**）与 `may unexpectedly execute arbitrary code`。

选题写的是"被执行的代码"。按表层测量，**下列写法一段都没有**：`execute code` **0**、`execute data` **0**、`data execution` **0**、`run code` **0**、`inject code` **0**、`instructions` **0**、`is/are processed` **0**、`be executed` **0**、`during/while/before processing` **0**、`boundary check(s)` **0**。**存在的是 `arbitrary code`（336 段 / 120 页 / 43 名）与 `code execution`（237 段 / 106 页 / 27 名）**——两者都是**名词短语作后果**，不是"某段代码被运行"这个事件陈述。

`code` 在 Impact 的 363 段构成完全被这两个短语吃下：`arbitrary code` 336、`code execution` 237、`remote code execution` 2，**两个都不含的只有 8 段**。取 `code` 前 26 字窗口统计，最高频三段窗口依次是 `…le to execute arbitrary co`（112）、`…ermination or arbitrary co`（92）、`…t may lead to arbitrary co`（65）——**`code` 出现的位置永远是句尾后果短语**。另有一个反直觉的零值：`arbitrary data`、`arbitrary input`、`arbitrary file` 里前两个 **0**，而 `arbitrary file` 有 63 段（43 页）——**"任意"这个限定词只修饰文件与代码，不修饰数据与输入**。

`arbitrary code execution｜remote code execution` 在 Impact 共 **220 段 / 102 页**，且**全部属于 X_I（361）**；反差 **141 段**是"提到 execution/execute 但没写 ACE"的句子（多为 `kernel privileges` 79 段一类的能力表述）。

# 6 data 与 code：Impact 侧交集 0

本篇最重要的集合事实是**互斥**：

| 集合 | 段 | 页 | 名 |
|---|---:|---:|---:|
| D_I（Impact 含 `data`） | 1,240 | 158 | 160 |
| C_I（Impact 含 `code`） | 363 | 128 | 43 |
| **D_I ∩ C_I** | **0** | — | — |
| D_I ∪ C_I | **1,603** | — | — |
| X_I（execut 三形，Impact） | 361 | 128 | 50 |
| **X_I ∩ D_I** | **0** | — | — |

**1,240 ＋ 363 = 1,603，无重叠。**这条结论不能由两个数字推出，必须做成员求交（第 19 章脚本里是 `die "data∩code!=0"` 断言）。它的含义是：**同一条缺陷不会既写"数据被读走"又写"代码被执行"**——措辞层把两者当作**互斥的后果类别**，而不是同一条攻击路径的两个阶段。第 8 条速览还给出：`data ∩ E = 0`（108 条在野标注段里一条都不提 data），`code ∩ E = 36`（有 36 段提 code）。

再看三个交集：**process 词族 ∩ D_I = 15**（1,897 段里只有 15 段同时提 data）、**crafted F ∩ D_I = 22**、**process 词族 ∩ F = 1,554**（F 的 1,769 段里 87.8% 落在 processing 句族）。也就是说：**"处理"的对象几乎总是 crafted 之物，几乎从不写 data**——"被处理的数据"这个选题字面组合，在语料里只有第 13 章那 **8 段**。

同一条逻辑也适用于"传递"：**D_I ∩ T = 0**（因为 T 本身 = 0），**C_I ∩ T = 0**。选题三个字里只有"处理"与"执行"能落到段落上。

# 7 接缝刀：拼接正则十词静默丢 1,400 段

凡把一条段的三个字段直接拼成一个文本块、再用**词边界**正则匹配，就会在**字段末尾没有句号**处丢掉一次命中——`…user data` ∥ `This issue…` 拼起来是 `…user dataThis issue…`，`data` 后面紧跟大写字母，`(?![\w-])` 不成立。**丢失量取决于该词的字段分布**，与语料内容无关，属于脚本层的静默低估。

本篇对 10 个词做了双口径对照：

| 词（词界） | 逐字段并集 | 三字段拼接 | 丢 |
|---|---:|---:|---:|
| `data` | 1,403 | 440 | **963** |
| `information` | 575 | 303 | **272** |
| `file` | 1,170 | 1,055 | **115** |
| `packet` | 20 | **0** | **20** |
| `credential` | 29 | 16 | 13 |
| `code` | 756 | 746 | 10 |
| `metadata` | 9 | 3 | 6 |
| `message` | 41 | 40 | 1 |
| `input` | 768 | 768 | **0** |
| `stream` | 29 | 29 | **0** |
| **合计** | — | — | **1,400** |

**四个判读。**

**（1）`packet` 会丢光。** 20 段全部丢失，因为这些段的 `packet` 出现在**字段末尾且后面紧跟下一字段首字母**。如果只用拼接口径，会得到"措辞层没有 packet"的错误结论——而 `packet` 正好是第 11 章用来核对"crafted 但不 maliciously"那 17 段的关键名词。

**（2）`data` 丢 963 段，占并集的 68.6%。** 这是本篇数字最大的一个陷阱：`data` 在 Impact 常写作句尾（`…access sensitive user data`），拼接后立刻与 Description 首词 `This`/`Multiple` 粘连。

**（3）`input` 与 `stream` 丢 0。** `input` 几乎只出现在 Description 的 `improved input handling` 里且不在字段末尾；`stream` 的 29 段全部在 Impact 且后面总跟标点。**丢不丢与词频无关，只与标点习惯有关。**

**（4）因此本篇一律采用并集口径。** 附表 B 的四列（全字段并集 / Impact / Description / 致谢）是**分别匹配**得到的，四列之和可以大于并集列（一段可在两个字段同时命中），这是设计而不是错误。

# 8 进程刀：`process` 的 766 次命中里 683 次是"进程"

`process` 是选题里唯一**词形与词性双向歧义**的词。四形全测：

| 词形 | 命中次数 | 段 | 页 | 名 |
|---|---:|---:|---:|---:|
| `processing` | 1,769 | 1,769 | 228 | 67 |
| `process` | 766 | 756 | 156 | 45 |
| `processes` | 10 | 10 | 9 | 2 |
| `processed` | **0** | 0 | 0 | 0 |
| **四形合计** | **2,545** | **1,897（并集）** | — | — |

**断言：四形段并集 = 1,897 = `process` 词族段并集，差 0**（附表生成器里的 `die` 条件）。`processed` 为 0 是第二条"被动缺失"证据（与 `executed` 为 0 同构）：**措辞层不写"数据被处理过"**。

裸 `process` 的 766 次命中按后接词分区：**`process memory` 326、`process crash` 294、`process termination` 63，三者合计 683 = 89.2%**，其余是 `may`（20）、`restricted`（20）、`entitlement`（8）等**动词用法**。把 `process memory / crash / termination` 三种"进程＋名词"句式合起来，共 **673 段 / 154 页**；它与 `processing` 的 1,769 段**交集 610、差 1,159**。**不做这一步就会把 683 次"进程"当成 683 次"处理"**，把 1,897 段的词族夸大成"处理规模"。

`process memory` 的 326 段**全部落在 Impact**（Description 0），页 110——它是"后果"专用短语，代表写法是 `…may lead to corruption of process memory` 与 `…or disclose process memory`（`corrupt process memory` 189 段 / 82 页，`disclose process memory` 10 段 / 7 页）。**"进程内存"是被破坏的对象，不是被处理的对象。**

`processes` 的 10 段全部是**名词复数**：Kernel 的 `shared between processes` ×8、MetricKit 的 `a root processes` ×2（后者是原文的语法瑕疵，本篇按原样计数）。**没有一个 `processes` 是动词第三人称单数**，所以"某组件处理某物"这个句式在 T2 里**不存在**。

**这条刀给"依上下文而定"的第一 answer：** 见到 `process` 必须先取后 1–2 个词；后接 `memory / crash / termination` 判为"进程"，其余判为"处理"。按此规则，选题的"处理"容量是 **processing 1,769 段**，而不是 `process` 词族的 1,897 段。

# 9 同义刀：data、information、content 是三个不重叠的池子

选题只写"data"，但语料里有四个竞争性名词。全字段并集口径：

| 名词 | 段 | 页 | Impact | Description | 致谢 |
|---|---:|---:|---:|---:|---:|
| `data` | 1,403 | 169 | 1,240 | 323 | **0** |
| `information` | 575 | 164 | 358 | 212 | 31 |
| `content` | 948 | 174 | 948 | 4 | 0 |
| `contents` | 56 | 42 | 49 | 7 | 0 |

**交集**：`data ∩ information` = **88**、`data ∩ content(s)` = **7**、`information ∩ content(s)` = **42**。**三个池子几乎不重叠**——尤其 `data` 与 `content` 只重合 7 段。

这 7 段的来源被两个短语锁定：`web content` **895 段 / 167 页** 与 `contents of` **18 段 / 17 页 / 5 名**。**两者交集 0**（同一个网页内容句不会写 `contents of`），且 **`web content ∩ data = 0`**、**`contents of ∩ data = 7`**——**`data` 与 `content` 的全部重合都来自 `contents of` 这一个短语**（多为附件/目录内容的描述）。

三条配套零值帮助定界：**`网页内容`（T1）0 次、`data execution` 0、`arbitrary data` 0**；而 `user data` **880 段**（Impact 880 / Description 0）、`sensitive data` **312 段**（Impact 271 / Description 47）、`sensitive` 词族 **1,134 段**（Impact 1,052）、`private data` 114（**Impact 侧 0**）、`data redaction` 218（**Impact 0**）、`data protection` 43（**Impact 0**）、`data container` 12（**Impact 0**）。**"data"在 Impact 里永远是"被访问的私人内容"，在 Description 里才是"被保护/被脱敏/被装入容器"的技术对象**——同一个词在两个字段里说的是两套东西，这是"依上下文而定"的第二个 answer。

动词侧的竞争同样可数，但要先说明**紧邻式与带定语的写法完全不同**：紧邻二词组 `access data` **0 段**、`protect data` **0 段**、`disclose data` **0 段**、`modify data` **0 段**、`share data` **0 段**，而允许 0–3 个定语后再遇 `data` 时 `access … data` **1,093 段**、`protect … data` **149 段**；`exfiltrat-` 词族 **51 段**（全部 Impact，32 页）、`read … data` **1 段**、`delete … data` **1 段**。**"data 前面必须挂一个定语"是措辞层的硬约束**（`access sensitive user data` 一种写法就 704 段），而**唯一能紧邻 data 的动词是 exfiltrate**（第 12 章 B 类）。附表 A 的 A 类 1,093 段正是这套"access＋定语＋data"句式。

# 10 crafted 的 23 个宾语头名词（附表 C）

"被处理的东西"在措辞层的正面写法是 `maliciously crafted <对象>`。附表 C 对 **1,769 段 / 218 页**里的 `maliciously crafted` 逐句取宾语头名词，得 **23 个不同名词、合计 1,760 次命中**（生成器断言 `crafted 命中==1760 && 段==1760`）。

| 头名词 | 次数 | 段 | 页 | 名 |
|---|---:|---:|---:|---:|
| content | 793 | 793 | 163 | 16 |
| file | 521 | 521 | 137 | 43 |
| image | 184 | 184 | 65 | 12 |
| font | 46 | 46 | 37 | 3 |
| webpage | 38 | 38 | 27 | 4 |
| model | 24 | 24 | 8 | 1 |
| archive | 22 | 22 | 16 | 5 |
| website | 19 | 19 | 19 | 3 |
| texture | 18 | 18 | 15 | 3 |
| message / catalog / share | 16 / 12 / 12 | 16 / 12 / 12 | 16 / 10 / 12 | 2 / 1 / 2 |
| scene | 8 | 8 | 8 | 1 |
| contact / document / volume | 7 / 7 / 7 | 7 / 7 / 7 | 7 / 6 / 5 | 1 / 4 / 1 |
| string | 6 | 6 | 6 | 2 |
| app / files / email | 5 / 5 / 4 | 5 / 5 / 4 | 5 / 5 / 4 | 1 / 1 / 1 |
| binary / input / repository | 2 / 2 / 2 | 2 / 2 / 2 | 1 / 2 / 2 | 2 / 1 / 1 |
| **合计** | **1,760** | **1,769** | **218** | — |

**前四项 1,544 = 87.7%**：`content`、`file`、`image`、`font`。**这四类正是"被解析的媒体与文档"**，与第 4 章的 `Processing`（content 847、file 313）、`Parsing`（file 124）、`Mounting`（image 6、volume 5）、`Extracting/Unpacking`（archive 6）逐词对应。

`data` 与 `code` **都不在这 23 项里**。`data` 只出现在 crafted 宾语的边缘（`malicious data` 见第 13 章那 8 段），`code` 从不作 crafted 的宾语（`crafted code` 不在 23 项中）。**这就是"依上下文而定"的第三个 answer**：选题的"数据与代码"在"处理"这一栏的**真实对应词是 content / file / image / font**，不是 data / code。

`data ∩ crafted F = 22`：1,240 条含 data 的 Impact 段里只有 22 段同时写 crafted，即"数据本身是精心构造的"这个表述几乎不成立。

# 11 少一个 -ly 的 17 段，与 packet 的 0 交集

把**词界 `crafted`**（1,786 段 / 218 页）与**词组 `maliciously crafted`**（1,769 段 / 218 页）相减，得 **17 段**——两种形态的页数相同（218），说明差异是段级的：**这 17 段没有一个新的页**。逐段读取原文：

- **libarchive 5 段**（页 121563、121564、121838、121840、121842）：`Processing a malicious crafted file may lead to a denial-of-service…`——**少写 `-ly`**（`malicious crafted`）。
- **Bluetooth 5 段**（页 126346、126348、126351、126352、126353）与 **Wi‑Fi 7 段**（页 127110、127111、127115、127116、127117、127118、127119）：`An attacker in a privileged network position may be able to perform denial-of-service attacks using crafted Bluetooth/Wi-Fi packets`——**只有 `crafted`，没有 `malicious(ly)`**。

两种情形语义不同：前者是**拼写偏差**，后者是**中性用法**（"构造的报文"不带恶意定语）。**如果只用词组匹配，这 17 段会被整体漏掉；如果只用词界 `crafted`，就会把 5 段 libarchive 的拼写偏差当成一个独立句式。**

配套的集合核对：`packet(s)` 在 Impact 共 **20 段 / 20 页 / 3 名**，与 F（`maliciously crafted`）**交集 0**——**报文类句子从不写"恶意构造"**，只写 `crafted packets`。同时 `packet` 是第 7 章里**拼接口径会全丢**的那个词。**这两个事实叠加，意味着"用拼接正则统计 crafted 与 packet 的关系"必然得出错误结论。**

# 12 data 的 Impact 句式九分区（附表 D）

附表 D 对 **D_I 的 1,240 条 Impact 句**按**固定顺序首次命中**分九类，**闭合到 1,240**：

| 类 | 句数 | 占 D_I |
|---|---:|---:|
| A access（读取后果） | 1,093 | **88.1%** |
| B exfiltrate（跨源外带） | 51 | 4.1% |
| C leak（泄露） | 32 | 2.6% |
| D use（用于操作） | 16 | 1.3% |
| E output/display（显示） | 14 | 1.1% |
| F protect（保护） | 5 | 0.4% |
| **G alter（改动）** | **0** | **0%** |
| H log/store（记录） | 4 | 0.3% |
| 未归类 | 25 | 2.0% |
| **合计** | **1,240** | 100% |

**要点一：`data` 的后果类别几乎只有一种。** access 1,093 段，剩余 **147 段**（= 1,240 − 1,093）分布在 B–H 与未归类。**G alter 为 0**：措辞层**从不写"data 被修改/删除/覆盖/篡改"**（配套的三个零值：`modify data` 0、`delete data` 在整体里只 1 段、`share data` 0）。

**要点二：句式高度模板化。** 1,240 段拆句后 distinct 模板只有 **39** 个，最高三个是 `An app may be able to access sensitive user data` **644**、`An app may be able to access user-sensitive data` **212**、`An app may be able to access protected user data` **137**，三句 **993 = 80.1%**。第四到第八是 `A malicious website may exfiltrate data cross-origin` 38、`A sandboxed app may be able to access sensitive user data` 18、`An attacker with physical access may be able to use Siri to access sensitive user data` 17、`A shortcut may be able to use sensitive data with certain actions without prompting the user` 16、`Visiting a maliciously crafted website may leak sensitive data` 15。

**要点三：25 段未归类的内容本身就是"其它动词"。** 样例是 `Processing malicious data may lead to unexpected app termination`（8 段）、`An app may be able to trick a user into copying sensitive data to the pasteboard`（6）、`A malicious website may be able to silently hijack clipboard data`（6）、`Sensitive keychain data may be accessible from an iOS backup`（3）、`An app may be able to read sensitive data from the GPU memory`（1）——**processing / copying / hijack / accessible / read** 五个位置。**"被处理的数据"正落在这 25 段里**，第 13 章单列其中的 8 段。

**要点四：语法位置的限制。** `data that`、`data which`、`data to be`、`data being`、`the data`、`this data` **六个写法各 0 段**。**`data` 在 T2 里永远是动词宾语或复合名词的后半，从不作主语、从不带关系从句或不定式后置定语。** 所以"数据被怎样"这个问题在措辞层**无法被问出来**——只有"谁能访问数据"。

# 13 唯一把 data 当输入的句子：恰好 8 段

`Processing malicious data may lead to unexpected app termination` 在**全树恰好 8 段**：**页 125884–125891**、块名 **全部是 Foundation**、CVE **全部是 CVE-2025-43532**、致谢栏同一条（两位外部研究者）。**1 个组件名 / 8 页 / 8 段**。

这是选题字面"被处理的数据"**唯一的正面实例**，且它的实例方式是**跨 macOS 版本重复公告同一个缺陷**（8 个页各 1 段），不是 8 种不同数据。**换句话说：整份措辞层里，"数据被处理"这一格只填了一个组件、一个编号、一种后果。**

对照"被执行的数据"：**0 段**（`execute data` 0、`data execution` 0）。对照"被传递的数据"：**0 段**（传递整组为 0）。**选题三个动词在 `data` 这个名词上的容量是 8 ∶ 0 ∶ 0。**

`data` 一侧另外两个小计数可用于校验拆句规则：`clipboard` 6 段、`pasteboard` 6 段（两者都是 Impact，页 6）——**剪贴板语境的 data 用 hijack/copy 这两个动词，与 processing 不共存**。

# 14 名词与处理动词总表（附表 B）

附表 B 是本篇的**正面证据主体**：61 个候选词/词组，五个计数列。三处最值得注意：

**（1）"处理"的替身词都在 Description 一侧。** `handl-` 词族 **1,463 段**里 Impact 只 **9 段**、Description **1,454**（其中 `handling` 单独一名词 1,458 段 / 177 页，Impact **4**）；`validat-` 词族 **1,341** 段（Impact 25 / Description 1,319，`input validation` 676 段里 Impact 只 21）；`sanitiz-` 词族 **93** 段（**Impact 0**）；`input` **768** 段（Impact **38** / Description **730**）；紧邻的 `improved handling` **168 段 / 86 页**、`improved input` **713 段 / 155 页**，两者 **Impact 侧都是 0**（允许中间插一个定语后 `improved/better … handling` 扩到 1,209 段，Impact 仍为 **0**）。**"处理与校验"是修复说明的用词，不是影响描述的用词**——这与第 4 章"动名词只在 Impact 开头"形成互补：**同一个"处理"概念，在两个字段里分别由"句首动名词"和"improved handling"两种形式承担，且互不越界。**

**（2）内存类名词也是 Description 侧。** `buffer` 200 段（Impact **2** / Description 199）、`overflow` 374 段（Impact **2** / Description 373）、`buffer overflow` 200 段（Impact 2）。**Impact 里几乎不说"缓冲区溢出"**，只说 `process memory`（326 段全在 Impact，第 8 章）。

**（3）技术名词几乎全零。** `byte(s)` **0**、`payload` **0**、`blob` **0**、`argument` **0**、`parameter` **0**、`secret(s)` **0**、`JavaScript` **0**、`database(s)` **0**、`untrusted` **0**、`attacker-supplied` **0**。**非零的小项**：`credential(s)` 29（Impact 29）、`metadata` 9、`object` 3（**Impact 0**）、`value` 2、`stream` 29、`message` 41（Impact 40）、`file` 1,170、`memory` 1,967（Impact 1,136 / Description 1,375，一段可在两栏同时计）。

# 15 与前几篇的集合接口

| 集合 | 段 | 与 D_I | 与 C_I | 与 X_I | 与 F |
|---|---:|---:|---:|---:|---:|
| E（`exploit` 词族，#114） | 108（域 A 98 / 域 B 10，76 页 / 12 名） | **0** | **36** | 36 | — |
| P（`attacker` 词族，#113） | 890（160 页 / 112 名） | **35** | — | **49** | **12** |
| process 词族 | 1,897 | **15** | — | — | **1,554** |

**三点判读。**

**（1）`data ∩ E = 0`。** 108 条在野标注段**没有一条提到 data**。这与 #114 的结论一致：在野标注集中发生在**代码执行与逃逸**类后果上（`code ∩ E = 36`、`execut ∩ E = 36`）。

**（2）`attacker` 更偏向执行句。** P ∩ X = **49**，P ∩ D_I = **35**，P ∩ F = **12**。**"攻击者"作为主语与"执行任意代码"同现的频率，是与"访问数据"同现的 1.4 倍，是与"crafted 句"同现的 4 倍**——因为 crafted 句的主语是**那个对象**（`Processing a … file may lead to …`），不是攻击者。**选题的被动语态正是这个句式选择的产物**：把对象放句首，攻击者就不必出现。

**（3）`process` 与 `crafted` 高度同现（1,554 ∶ 1,769 = 87.8%）**，而 `process` 与 `data` 只同现 15 段。**"处理—精心构造对象"是 T2 的一条固定搭配链，"处理—数据"不是。**

# 16 组件、页与版式域

**块名（组件）侧**：D_I 覆盖 **160 个块名**，Top 是 `Siri` 76、`AppleMobileFileIntegrity` 69、`Shortcuts` 64、`WebKit` 57、`Spotlight` 49、`Foundation` 33、`Kernel` 32、`CoreServices` 29、`StorageKit` 22、`Sandbox` 21、`Accessibility` 20、`Game Center` 19。**data 类缺陷集中在"能读到用户内容的入口组件"**，而不是内核。X_I 只覆盖 **50 个块名**，F 的 `content` 宾语只出现在 **16 个块名**上（WebKit 为主）。**三套名词的组件盘子大小不同：data 160 ≫ code/execution 50 > content-crafted 16。**

**页侧**：D_I 分布 **158 页**，页内密度直方图最高 **42 段/页**，其次 41、36、34、30、27 各 1 页；**尾部很长**（1 段/页 24 页、2 段 9 页、3 段 18 页、4 段 21 页）。X_I 与 C_I 同为 **128 页**，F 为 **218 页**，G（句首动名词）为 **224 页**——**processing 句式覆盖的页数（219）已经超过 data 句式（158）的 1.4 倍**。

**版式域侧**：本篇四个主集在两版式域的分布为 **G 2,155 ∶ 24**、**D_I 1,223 ∶ 17**、**X 335 ∶ 29**、**F 1,750 ∶ 19**；`processing` 词形 **1,745 ∶ 24**，且**域 B 的 24 段句首动名词全部是 `Processing`**。**域 B（11 页粗体版式）几乎只用 processing 模板**，而域 A 有 21 个动名词——**版式差异会带来词表差异**，任何"处理类句子共有 N 种写法"的结论都必须分域报告（第 1 章分母声明）。

# 17 中文手册层（T1）的对应词

对《Apple 平台安全保护》zh-CN 全量转储（179 页，窗口切分后 **178 页可用**）按"次 / 页 / ±14 字上下文窗口 distinct"三口径测量：

| 中文词 | 次 | 页 | 窗口 |
|---|---:|---:|---:|
| 数据 | 3,803 | 178 | 918 |
| 信息 | 1,954 | 178 | 521 |
| 访问 | 1,888 | 178 | 454 |
| 文件 | 1,494 | 178 | 411 |
| 代码 | 534 | 178 | 172 |
| 执行 | 427 | 178 | 248 |
| 用户数据 | 419 | 178 | 54 |
| 处理 | 338 | 178 | 155 |
| 内容 | 324 | 178 | 141 |
| 恶意 | 288 | 178 | 109 |
| **传递** | **10** | **10** | **10** |
| 传输 | 83 | 46 | 82 |
| 输入 | 106 | 55 | 106 |
| 读取 / 写入 | 59 / 46 | 22 / 25 | 59 / 46 |
| 代码执行 | 3 | 2 | 3 |
| 构造 / 精心 / 序列化 / 不可信 / 缓冲区 | **各 1** | 1 | 1 |

**"传递"的 10 个窗口逐个列出**（这是中文层唯一与选题第一动词直接对应的证据）：**过滤"传递到内核的选项"**、**"macOS 内核传递 launchd 参数向量中的 -s"**、**"服务器会将完整的一组已签名数据传递给设备"**、**"将 Apple 证书传递给操作系统"**、**"将设备的当前活动传递给其他已配对的 iCloud 设备"**、**"视频和音频数据将序列化并通过直接的 XPC 连接实时传递到第三方上传扩展项"**（该窗同时是 `序列化` 唯一一次出现）等。**全部是系统内部移交与接口约定，没有一处描述缺陷的数据流向。**

四个孤例的语境：`构造` = 密钥派生的递归构造；`精心` = "**精心设计**的范围和时间限制内方便用户访问设备"（**与 crafted 无关**，属翻译巧合）；`不可信` = BlastDoor 一节"IDS 和其他向量中收到的**不可信数据**"（中文层唯一一处 untrusted 对应词，对应 #110/#112 话题）；`缓冲区` = "缓冲区溢出（通过确保所有指针都携带…）"。**`代码执行` 只 3 次 / 2 页**，全部指 SPTM 的执行策略、WatchKit 代码限制与 arm64 签名要求。

**19 项零命中**：`有效载荷`、`载荷`、`反序列化`、`任意代码`、`网页内容`、`进程内存`、`泄漏`、`分享`、`被构造`、`特制`、`非可信`、`利用链`、`攻击链`、`零点击`、`沙箱`、`传输链`、`字节码`、`机器码`、`中间表示` **各 0 次**（另有 `精心构造` 0 次，但 `精心` 本身有 1 次，见上）。

**同一选题在两层同时为空**：英文措辞层"传递"13 词 **0 段**（附表 B）＋ 中文手册层 `有效载荷 / 反序列化 / 任意代码 / 网页内容 / 进程内存` **各 0 次**——**"数据在系统之间被传递、序列化成载荷"这套词汇，两层都不写。** 而"处理"与"执行"两层都写得很多（英文 2,179 段句首动名词、中文 `处理` 338 次 / `执行` 427 次），**差别在宾语**：英文的宾语是 crafted 的 content/file/image，中文的宾语是"数据/文件/内容"这类泛称。

# 18 验证与断言

本篇脚本（`_tmp/tl116tab.pl` 生成附表 A–D，`_tmp/tl116chk.pl` 复算集合与三把刀，`_tmp/tl116p2.pl` 复算紧邻词与句式框架，`_tmp/tl116q.pl` 出明细，`_tmp/tl116t.pl` 与 `_tmp/tl116t2.pl` 复算中文层）在运行时带以下 **`die` 断言**，任一不成立即中止、不产出附表文件：

| 断言 | 期望 | 实测 |
|---|---|---|
| 分母 blocks / pages / names | 9,136 / 257 / 472 | ✓（含 Impact 7,434） |
| `processing` 词形命中 | 1,769 | ✓ |
| 词形和 − 词族并集段 | 0 | 2,545 命中 ∶ 1,897 段 ✓ |
| execut 三形并集 | 364（executed 0） | ✓ |
| 句首动名词 distinct | 21 | ✓ |
| 句首动名词段合计 | 2,179 | ✓（= 7,434 的 29.3%） |
| Description 侧动名词开头段 | **0** | ✓ |
| `crafted` 命中合计 | 1,760 | ✓ |
| `data` ∩ `code`（Impact） | **0** | ✓（并 1,603） |
| `data`(Impact) ∩ E | **0** | ✓ |
| P（`attacker`）段数 | 890 | ✓ |
| 附表 D 合计 = D_I 句数 | 1,240，且未归类 = 25、G alter = 0 | ✓ |
| 附表 A 行 / 附表 B 行 / 附表 C 行 / 附表 D 行 | 21 / 61 / 23 / 10 | ✓（B 全零 33、非零 28） |

**读致谢字段一律 `// ""`**：全树有 **4 段致谢为空**，未初始化值会让"致谢列"虚增。附表 A 的"含 crafted 句数"列只统计**首句**，因此合计 **1,680**，小于附表 C 的 **1,760**（后者统计所有句子的 `maliciously crafted`）——**两列口径不同，不可互相校验**。

# 19 复算步骤

1. `require "./_tmp/tl111load.pl"; my %B = load_blocks();` 得 9,136 段；确认 `{imp,des,ack,nm,id}` 四键，且致谢 4 段为空。
2. **附表 A**：对每段 `SE($B{k}{imp})` 的**首句**匹配 `^\s*(\w+ing)\b`（无词表），词自动入矩阵；页/名分别用 `id`/`nm` 去重；宾语头名词用懒取 `((?:[\w-]+\s+){1,4}?)(?:$STOP)`，`$STOP` 含标点与后果动词，**末位锚用 `\Z` 而不是 `$|`**（后者会读成 `$|` 变量）。
3. **附表 C**：对 `maliciously\s+crafted\s+` 之后的 1–3 词取头名词，累加"次数"与"段集合"，要求次数合计 = 1,760。
4. **附表 D**：对 D_I 的每条含 data 句按 **A→B→C→D→E→F→G→H→Z** 顺序首次命中；A 的判据要允许 `access user-sensitive data` 的连字符写法（`(?:[\w-]+\s+){0,3}?`），否则未归类会虚增到 244。
5. **附表 B**：61 项判据全部带 `(?<![\w-])…(?![\w-])`，**逐字段匹配取并集**；另跑一遍"三字段拼接"版本，产出第 7 章的丢段表。
6. **三把刀**：进程刀用 `process\s+(memory|crash|termination)` 计数 683 与"process＋进程名"段集 673；同义刀用 `web content` / `contents of` / `data` 三方求交；接缝刀用 `MK()` 与 `CAT()` 两个函数对照。
7. **中文层**：`_tmp/sec*.txt` 179 个文件、排除 `sec2727afbbc.txt` 得 178；`Encode::decode("utf-8",$raw,FB_DEFAULT)` 读取；标题取 `_tmp/titles.txt` 的 `/^(sec[0-9a-f]+)\|(.*)$/`。
8. `perl _hbcheck.pl "iOS被传递处理或执行的数据与代码.md"` 要求 `RESULT=PASS`，附表 A–D 行数声明与实测一致。

# 20 缺口清单

1. **"传递"无法在措辞层给出任何正面样本**：T = 0 段。任何关于"数据如何在组件间传递"的结论都必须来自手册层（T1）或系统源码，不能来自顾问页；本篇只报告"顾问页不写"。
2. **`executed` / `executing` / `processed` 三形为 0**，导致"某物被执行/被处理"的事件视角在文本里**不可表达**；本篇只能报告"谁可能对什么获得执行能力"。
3. **附表 A 的宾语头名词有 251 段"未取到"**（`Processing` 一词内），原因是宾语前带多词定语或被 `$STOP` 的后果动词截断；这些段仍计入段数，但不进入头名词分布。
4. **附表 C 的 1,760 次命中与 F 的 1,769 段之间差 9**，来自一段内多次出现 crafted 与个别句读差异；本篇不做逐段一一对齐。
5. **词界 `crafted` 与词组 `maliciously crafted` 相差 17 段**，其中 5 段是原文少写 `-ly`。若要跨报告比 crafted 规模，必须说明用了哪一个口径。
6. **九分区的顺序敏感**：A 判据放在最前会吸走 E/F 里同时含 access 的句子；`G alter = 0` 这一结论**即使换序仍为 0**，其余类会小幅移动。
7. **域 B 只有 91 段**，其"全部是 Processing"这一结论样本量小，不可外推为"粗体版式只用 processing 模板"。
8. **T1 与 T2 不构成引用关系**：中文手册层的 `传递 / 序列化 / 不可信` 命中是英文原文的翻译产物，不能用来验证英文措辞层的结论，只能验证"两层都不写什么"。

# 21 常见误读纠正

- **把 `process` 的 766 次命中读成"处理了 766 次"**：683 次是"进程"，动词用法只有 83 次；且 `processing` 另有 1,769 段。
- **把附表 B 的四列相加当作段数**：一段可在 Impact 与 Description 同时命中，四列之和 ≥ 并集列。
- **用三字段拼接脚本得到的 440 段回答"data 有多少段"**：正确值是并集 1,403 段（Impact 侧 1,240）。
- **把 `web content` 的 895 段当作"数据"的一种**：它与 `data` 交集 **0**，与 `contents of` 交集也 **0**。
- **由"措辞层没有 pass/transfer"推出"iOS 不传递数据"**：零命中只表示**公开措辞未写该词**，不表示相应机制不存在。
- **把 `Processing a maliciously crafted X` 说成"X 是攻击代码"**：X 的 23 个取值全是**被解析的对象**（content/file/image/font/…），措辞层从未称其为代码。
- **把第 13 章那 8 段读成 8 种数据被处理**：8 段是**同一组件、同一 CVE、同一后果**在 8 个版本页上的重复公告。
- **把 `data ∩ code = 0` 说成"数据缺陷与代码缺陷互不相干"**：它是**Impact 句式层**的互斥（后果不混写），不是缺陷层的关系。

# 22 结论

选题的三个动词在 Apple 安全响应顾问页的英文措辞层里得到三种完全不同的答复。**"传递"没有表层形式**——附表 B 的 61 项里 33 项全零，其中 **11 行"移交"判据（覆盖 14 个动词词位）**构成整组零，六个近义动词（send 34、relay 20、receive 6、deliver 4、transmit 1、forward 1）语义各属别处。**"处理"以句法而非词汇存在**——21 个词形以 2,179 段占据含 Impact 段的 **29.3%**，且**只在 Impact 段首出现**（Description 侧为 0），77.1% 首句带 `maliciously crafted`。**"执行"以三形闭合、零被动、单一宾语存在**——`execution 240 ＋ execute 121 ＋ executable 3 = 364`，`executed`/`executing` 各 0，`execute` 之后只有 `arbitrary`（118）与 `malicious`（3）。

两个名词的分工同样清晰：**`data` 是"被访问的私人内容"**（1,240 段里 1,093 段是 access 句式，`G alter = 0`、六个 `data that/which/the data` 写法全 0）；**`code` 是"被运行的东西"**（363 段里 336 段带 `arbitrary code`，两者都不含的只有 8 段）；**两者在 Impact 侧交集为 0，并集恰好 1,603**。而"被处理的东西"的正面名单是 `maliciously crafted` 的 **23 个头名词、1,760 次命中**，前四项（content、file、image、font）占 87.7%，`data` 与 `code` 都不在其中。

"具体含义依上下文而定"于是有了可执行的形式：**进程刀**要求先看 `process` 后 1–2 词（683 次"进程" ∶ 766 次命中）；**同义刀**要求把 `data`、`information`、`content`、`contents` 当四个池子求交（`web content` 895 ∩ `contents of` 18 = 0，前者与 `data` 也不相交）；**接缝刀**要求任何词频都用逐字段并集而不是拼接（十词合计会静默丢 1,400 段，`packet` 会丢光）。三把刀都落下之后，选题在 T2 里的最终容量是：**传递 0 段、处理 2,179 段、执行 364 段；数据 1,240 段、代码 363 段、而"被处理的数据"只有 8 段——且这 8 段属于同一个组件、同一个编号、同一种后果。**

# 附表 A 句首动名词矩阵（21 行）

判据：Impact 首句匹配 `^\s*(\w+ing)\b`（**不预设词表**）。列为：段数 / 页 / 块名 / 首句含 `maliciously crafted` 的句子数 / 宾语头名词 Top3 / 代表句。

| 句首动名词 | 段 | 页 | 名 | 含 crafted 句数 | 宾语头名词（Top） | 代表句 |
|---|---:|---:|---:|---:|---|---|
| `processing` | 1760 | 219 | 67 | 1531 | content(847)、file(313)、(未取到)(251) | Processing a maliciously crafted media file may lead to unexpected app termination or corrupt process memory |
| `parsing` | 146 | 65 | 19 | 47 | file(124)、(未取到)(12)、image(8) | Parsing a file may lead to disclosure of user information |
| `visiting` | 82 | 57 | 6 | 19 | website(82) | Visiting a website may leak sensitive data |
| `connecting` | 58 | 16 | 5 | 0 | (未取到)(54)、accessory(4) | Connecting to a malicious NFS server may disclose kernel memory |
| `mounting` | 40 | 17 | 8 | 34 | (未取到)(29)、image(6)、volume(5) | Mounting a maliciously crafted volume may lead to unexpected system termination |
| `opening` | 21 | 8 | 4 | 18 | file(14)、(未取到)(7) | Opening a file referencing a network share may lead to unexpected software execution |
| `restoring` | 18 | 13 | 4 | 17 | (未取到)(17)、backup(1) | Restoring a maliciously crafted backup file may lead to modification of protected system files |
| `playing` | 8 | 8 | 1 | 0 | file(8) | Playing a malicious audio file may lead to an unexpected app termination |
| `deleting` | 6 | 6 | 1 | 0 | messages(6) | Deleting a conversation in Messages may expose user contact information in system logging |
| `extracting` | 6 | 6 | 1 | 6 | archive(6) | Extracting a maliciously crafted archive may allow an attacker to write arbitrary files |
| `running` | 6 | 6 | 2 | 0 | command(6) | Running an hdiutil command may unexpectedly execute arbitrary code |
| `unpacking` | 6 | 6 | 1 | 6 | archive(6) | Unpacking a maliciously crafted archive may allow an attacker to write arbitrary files |
| `replying` | 4 | 4 | 1 | 0 | email(4) | Replying to an email could display remote images in Mail in Lockdown Mode |
| `turning` | 4 | 4 | 1 | 0 | (未取到)(4) | Turning off "Load remote content in messages&rdquo; may not apply to all mail previews |
| `copying` | 3 | 3 | 1 | 0 | (未取到)(3) | Copying a URL from Web Inspector may lead to command injection |
| `incoming` | 3 | 3 | 1 | 0 | calls(3) | Incoming FaceTime calls can appear or be accepted on a locked macOS device, even with notifications disabled on the lock screen |
| `cloning` | 2 | 2 | 1 | 2 | repository(2) | Cloning a maliciously crafted repository may result in remote code execution |
| `loading` | 2 | 2 | 1 | 0 | iframe(2) | Loading a malicious iframe may lead to a cross-site scripting attack |
| `muting` | 2 | 2 | 2 | 0 | (未取到)(1)、ringing(1) | Muting a call while ringing may not result in mute being enabled |
| `enabling` | 1 | 1 | 1 | 0 | (未取到)(1) | Enabling Lockdown Mode while setting up a Mac may cause FileVault to become unexpectedly disabled |
| `forwarding` | 1 | 1 | 1 | 0 | email(1) | Forwarding an email could display remote images in Mail in Lockdown Mode |

**合计 2,179 段 / 224 页 / 98 个块名 = 全树 7,434 条含 Impact 段的 29.3%；首句含 crafted 的段合计 1,680（77.1%）。**`incoming` 一行的词形以 `-ing` 结尾但语法上是形容词，本篇按表层判据保留。**Description 字段里以动名词开头的段 = 0。**

# 附表 B 传递、处理、执行与名词电池（61 行）

判据：全部带词边界 `(?<![\w-])…(?![\w-])`，**逐字段分别匹配后取并集**（第 7 章）；致谢列读取时带 `// ""`。**61 项里 33 项四列全零、28 项非零。**

| 词 / 词组（带词边界） | 全字段并集 | Impact | Description | 致谢 | 页 |
|---|---:|---:|---:|---:|---:|
| data（词界） | 1403 | 1240 | 323 | 0 | 169 |
| code（词界） | 756 | 363 | 386 | 43 | 179 |
| information | 575 | 358 | 212 | 31 | 164 |
| content（单数） | 948 | 948 | 4 | 0 | 174 |
| contents（复数） | 56 | 49 | 7 | 0 | 42 |
| input | 768 | 38 | 730 | 0 | 155 |
| buffer | 200 | 2 | 199 | 0 | 79 |
| message | 41 | 40 | 1 | 0 | 39 |
| file | 1170 | 1058 | 126 | 0 | 177 |
| stream | 29 | 29 | 0 | 0 | 29 |
| packet | 20 | 20 | 0 | 0 | 20 |
| byte(s) | 0 | 0 | 0 | 0 | 0 |
| payload | 0 | 0 | 0 | 0 | 0 |
| blob | 0 | 0 | 0 | 0 | 0 |
| value | 2 | 2 | 0 | 0 | 1 |
| object | 3 | 0 | 3 | 0 | 3 |
| argument | 0 | 0 | 0 | 0 | 0 |
| parameter | 0 | 0 | 0 | 0 | 0 |
| secret(s) | 0 | 0 | 0 | 0 | 0 |
| credential(s) | 29 | 29 | 0 | 0 | 28 |
| JavaScript | 0 | 0 | 0 | 0 | 0 |
| database(s) | 0 | 0 | 0 | 0 | 0 |
| metadata | 9 | 6 | 3 | 0 | 9 |
| pass 词族（传递） | 0 | 0 | 0 | 0 | 0 |
| transfer | 0 | 0 | 0 | 0 | 0 |
| transmit | 1 | 1 | 0 | 0 | 1 |
| deliver | 4 | 1 | 0 | 3 | 4 |
| receive | 6 | 6 | 0 | 0 | 5 |
| send | 34 | 24 | 10 | 0 | 32 |
| forward | 1 | 1 | 0 | 0 | 1 |
| relay | 20 | 20 | 0 | 0 | 17 |
| marshal | 0 | 0 | 0 | 0 | 0 |
| (de)serialize | 0 | 0 | 0 | 0 | 0 |
| (en|de)code | 0 | 0 | 0 | 0 | 0 |
| ingest | 0 | 0 | 0 | 0 | 0 |
| consume/fetch | 0 | 0 | 0 | 0 | 0 |
| transport | 0 | 0 | 0 | 0 | 0 |
| pipe | 0 | 0 | 0 | 0 | 0 |
| channel | 0 | 0 | 0 | 0 | 0 |
| handoff | 0 | 0 | 0 | 0 | 0 |
| process 词族 | 1897 | 1889 | 8 | 0 | 228 |
| execute 词族 | 364 | 361 | 3 | 0 | 128 |
| executed（被动形） | 0 | 0 | 0 | 0 | 0 |
| parse 词族 | 229 | 146 | 83 | 0 | 89 |
| handl 词族 | 1463 | 9 | 1454 | 0 | 177 |
| validat 词族 | 1341 | 25 | 1319 | 0 | 179 |
| sanitiz 词族 | 93 | 0 | 93 | 0 | 54 |
| untrusted | 0 | 0 | 0 | 0 | 0 |
| attacker-supplied | 0 | 0 | 0 | 0 | 0 |
| arbitrary data | 0 | 0 | 0 | 0 | 0 |
| arbitrary input | 0 | 0 | 0 | 0 | 0 |
| arbitrary code | 336 | 336 | 0 | 0 | 120 |
| data execution | 0 | 0 | 0 | 0 | 0 |
| execute data | 0 | 0 | 0 | 0 | 0 |
| instructions | 0 | 0 | 0 | 0 | 0 |
| inject code | 0 | 0 | 0 | 0 | 0 |
| run code | 0 | 0 | 0 | 0 | 0 |
| is/are processed | 0 | 0 | 0 | 0 | 0 |
| be executed | 0 | 0 | 0 | 0 | 0 |
| during/while/before processing | 0 | 0 | 0 | 0 | 0 |
| boundary check(s) | 0 | 0 | 0 | 0 | 0 |

**"全字段并集"列小于 Impact＋Description＋致谢三列之和属正常**（一段可在多个字段命中）。`pass 词族`、`transfer`、`marshal`、`(de)serialize`、`(en|de)code`、`ingest`、`consume/fetch`、`transport`、`pipe`、`channel`、`handoff` **共 11 个传递相关判据全零**；`untrusted`、`attacker-supplied`、`arbitrary data`、`arbitrary input`、`data execution`、`execute data`、`instructions`、`inject code`、`run code`、`is/are processed`、`be executed`、`during/while/before processing`、`boundary check(s)` **共 13 项全零**。

# 附表 C crafted 宾语头名词分布（23 行）

判据：`maliciously crafted` 之后 1–3 词内的头名词；对 **1,769 段 / 218 页**逐句统计，**命中合计 1,760**（断言条件）。"代表短语"列已剥去 `maliciously crafted` 前缀。

| 宾语头名词 | 次数 | 段 | 页 | 名 | 代表短语 |
|---|---:|---:|---:|---:|---|
| `content` | 793 | 793 | 163 | 16 | web content may lead to an unexpected Safari crash |
| `file` | 521 | 521 | 137 | 43 | media file may lead to unexpected app termination or corrupt process memory |
| `image` | 184 | 184 | 65 | 12 | image may result in disclosure of process memory |
| `font` | 46 | 46 | 37 | 3 | font may lead to unexpected app termination or corrupt process memory |
| `webpage` | 38 | 38 | 27 | 4 | webpage may be able to fingerprint the user |
| `model` | 24 | 24 | 8 | 1 | 3D model may lead to memory corruption |
| `archive` | 22 | 22 | 16 | 5 | ZIP archive may bypass Gatekeeper checks |
| `website` | 19 | 19 | 19 | 3 | website may leak sensitive data |
| `texture` | 18 | 18 | 15 | 3 | texture may lead to unexpected app termination |
| `message` | 16 | 16 | 16 | 2 | message may lead to a denial-of-service |
| `catalog` | 12 | 12 | 10 | 1 | asset catalog may lead to unexpected process termination |
| `share` | 12 | 12 | 12 | 2 | SMB network share may lead to system termination |
| `scene` | 8 | 8 | 8 | 1 | 3D scene may lead to unexpected process termination |
| `contact` | 7 | 7 | 7 | 1 | contact may leak sensitive data |
| `document` | 7 | 7 | 6 | 4 | document may lead to an out-of-bounds read |
| `volume` | 7 | 7 | 5 | 1 | volume may lead to unexpected system termination |
| `string` | 6 | 6 | 6 | 2 | string may lead to heap corruption |
| `app` | 5 | 5 | 5 | 1 | app may be able to bypass code signing enforcement |
| `files` | 5 | 5 | 5 | 1 | files may lead to unexpected system termination |
| `email` | 4 | 4 | 4 | 1 | email may be able to initiate FaceTime calls without user authorization |
| `binary` | 2 | 2 | 1 | 2 | binary may lead to unexpected tool termination |
| `input` | 2 | 2 | 2 | 1 | NTLM input may lead to unexpected app termination |
| `repository` | 2 | 2 | 2 | 1 | repository may result in remote code execution |

**前四项 `content` 793 ＋ `file` 521 ＋ `image` 184 ＋ `font` 46 = 1,544 = 87.7%；23 项合计 1,760，对应 1,769 段 / 218 页。**`data` 与 `code` **都不在这 23 项之中**；`packet` 也不在（`crafted packets` 的 17 段属第 11 章的"无 maliciously"分支，不计入本表判据）。

# 附表 D data 的 Impact 句式分区（10 行）

判据：对 Impact 含 `data` 的 **1,240 条句**按 A→H 顺序**首次命中**归档，未命中记 `Z?`；**九类相加 = 1,240**（断言条件：合计 = 句总数、未归类 = 25、G alter = 0）。

| 类 | 判据（对含 data 的 Impact 句，按此序首次命中） | 句数 |
|---|---|---:|
| A access（读取后果） | 句中同时出现 access…data（含 user-sensitive data 连字符写法） | 1093 |
| B exfiltrate（跨源外带） | 句中含 exfiltrat- 词族 | 51 |
| C leak（泄露） | 句含 leak…data / leak sensitive | 32 |
| D use（用于操作） | 句含 use…data | 16 |
| E output/display（显示） | 句含 output / display / shown / visible / view / render | 14 |
| F protect（保护） | 句含 protect / redact / encrypt / prevent | 5 |
| G alter（改动） | 句含 delete / modify / overwrite / corrupt / tamper / change | 0 |
| H log/store（记录） | 句含 track / log / record / stored / storage | 4 |
| Z? | 以上八条都不命中 | 25 |
| **合计** | A–H 八类 + 未归类 = 含 data 的 Impact 句总数 | **1240** |

**A 类 1,093 段 = 88.1%，`G alter（改动）= 0`**——措辞层不写"data 被修改/删除/覆盖"。**未归类 25 段的样例**：`Processing malicious data may lead to unexpected app termination`（8）、`…trick a user into copying sensitive data to the pasteboard`（6）、`…silently hijack clipboard data`（6）、`Sensitive keychain data may be accessible from an iOS backup`（3）、`…read sensitive data from the GPU memory`（1）。

# 附表 E 错误写法与纠正对照（8 行）

| 错误写法 | 问题 | 纠正后的说法 |
|---|---|---|
| "`data` 在 Impact 里出现 440 段" | 440 是**三字段拼接**结果，接缝吃掉 963 段 | "逐字段并集 **1,403 段**，其中 **Impact 侧 1,240 段 / 158 页 / 160 名**" |
| "`process` 词族 1,897 段说明处理类缺陷近两千条" | 裸 `process` 的 766 次命中里 **683 次是"进程"** | "processing 词形 **1,769 段**；'process＋进程名' 句式 673 段，与 processing 交集 610" |
| "`payload`、`byte`、`parameter` 用得少" | 三者**在三个字段里一段都没有**（另 `argument`、`secret`、`JavaScript`、`database` 同） | "附表 B 中 33 项全零，`payload / byte(s) / argument / parameter / secret(s) / JavaScript / database(s)` **各 0 段**" |
| "措辞层写了数据在组件间的传递" | **11 行传递判据（14 个词位）**整组 **0 段**；非零的 `send/relay/receive/deliver/transmit/forward` 语义各属别处 | "`T = 0 段`；传递关系只能来自手册层或源码，顾问页不写" |
| "`data` 与 `code` 同属'被处理的对象'" | 两者 Impact 侧**交集 0**，且都**不在 crafted 的 23 个宾语里** | "`D_I 1,240 ∩ C_I 363 = 0`（并 1,603）；被处理的对象是 content/file/image/font（1,760 次、前四项 87.7%）" |
| "代码被执行的情况文本里有记载" | `executed`、`executing`、`be executed`、`execute code`、`run code` **各 0** | "执行只有主动式能力句：`may be able to execute` **112 段**，三形合计 **364 段**" |
| "895 段 `web content` 也是'数据'的一种写法" | `web content` ∩ `data` = **0**；`contents of`（18 段）∩ `data` = 7，这 7 段就是 `data ∩ content(s)` 的全部 | "三个名词池几乎不重叠：`data ∩ information` 88、`data ∩ content(s)` **7**" |
| "8 段 `Processing malicious data` 说明有 8 类数据被处理" | 8 段是**同一 CVE（CVE-2025-43532）、同一组件 Foundation** 在页 125884–125891 的重复公告 | "**1 名 / 8 页 / 8 段、同一后果**；这是全树唯一把 data 写成处理输入的句式" |

---

**边界声明：** 本篇全部内容是对 Apple 公开安全响应顾问页英文镜像（257 份、9,136 段，两个版式域）与《Apple 平台安全保护》中文指南转储（178 页可用正文域）的词形分布、句首句法位置、宾语头名词归类、集合交并、双口径计数差异与零命中词表的**文本计量分析**，用于说明公开文本如何以措辞与句法选择来表达"某物被交给系统、被系统消化、被系统运行"这三件事，以及在哪些维度上根本不表达。本篇不提供任何利用代码、恶意样本、构造 crafted 对象的方法或参数、数据结构序列化格式细节，不含针对任何缺陷的触发步骤、时序、调试路径或绕过访问控制与权限检查的说明；文中出现的组件名、CVE 编号、页号、例句均为对公开文本的引用与计数，"零命中"仅表示公开措辞未写该词，不表示相应机制、数据流或事件不存在；各"集合"与"分区"是对公开句子表层形式的分类，不是威胁情报分类，也不是数据流向或攻击阶段划分。所有百分比与段数只描述本篇镜像与切分口径下的样本，若上游文本更新，全部数值需按第 19 章复算。

Telegram: https://t.me/one00190
