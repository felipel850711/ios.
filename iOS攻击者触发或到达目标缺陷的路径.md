@@TITLE@@iOS 攻击者触发或到达目标缺陷的路径
@@SUBTITLE@@选题里的"路径"在 Apple 安全响应顾问页里**只有一个端点被写出来**。全树 257 份英文镜像 / **9,136 段**里，`attacker` 在 Impact 侧命中 **890 段 / 160 页 / 112 个块名**（占含 Impact 段 **12.0%**），而 **Description 侧一段都没有**——同这 890 段全部带有 Description 栏。把这 890 段拆成句，能稳定读出的只有一段式骨架：**起点身份（43 种写法）→ 情态（4 个词，`may` 占 868）→ 动作（44 个首词、106 条尾巴）**。中间那段"怎么进来的"没有词：**通道介词 `via` 只有 23 段、`by + 动名词` 只有 2 段、`over the` / `by sending` / `by visiting` / `by mounting` / `by parsing` 各 0 段**；先后顺序也没有词：**`then` / `first` / `chain` / `foothold` / `initial access` / `once` / `escape` / `sequence` / `steps` / `prerequisite` / `trigger` / `reach` / `vector` / `entry point` / `attack surface` 全为 0 段**。连"路径"这个字面词都不站在这件事上：`path` 在 Impact 侧只有 **9 段**，且这 9 段讲的全是**文件系统路径**，与 890 段的交集是 **0**
@@INFO@@报告日期|2026 年 10 月 27 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 7,434 段含 Description**。路径全集 P = Impact 含 `attacker` 的 **890 段**（域 A 885 段 / 155 页 / 112 名，域 B 5 段 / 5 页 / 3 名）。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做 62 词逐词普查
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含针对他人设备的操作步骤、不含任何"按什么顺序做什么才能命中某组件"的说明；本篇只清点**公开文本把哪些成分写成了句子、把哪些成分留在文本之外**，不重建任何真实攻击路径
@@INFO@@分类|路径的**三段骨架**（起点 / 情态 / 动作）与中间层的**词汇空缺** / `attacker` 的**字段单边性**（Impact 890 ∶ Description 0）/ 起点**闭合分区 230 + 129 + 87 + 194 + 64 + 186 = 890**、未归类 0 / 后果句与路径句**互不相交**（`lead to` 1,738 段 ∩ P = 0）/ 在野标记**只落在一种起点**（25 / 25 全为"已具备读写能力"）
@@INFO@@难度|进阶（第 3 章的字段单边性是全篇前提：把 `attacker` 当两栏通用词会凭空造出一条不存在的 Description 侧曲线；第 4 章的判序决定分区数字，换判序会得到 194 / 194 / 175 / 170 / 114 / 43 这组同样闭合但完全不同的数；第 12 章的"两个集合交集为 0"必须用成员求交来断言，不能靠两个段数相减推断）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

选题字面是"攻击者触发或到达目标缺陷的路径"。这句话在威胁建模里通常拆成四件事：**谁**（起点身份与前置条件）、**从哪里进来**（通道）、**按什么顺序做**（步骤与链）、**做到什么算到达**（后果）。本篇逐词核对之后发现，Apple 公开安全响应顾问页（下称 T2）只把**第一件和第四件**写成了句子，而且两者**分处两套句式、互不相交**；第二件和第三件在措辞层**没有对应的词表**。

四组反差。

**反差一（有主语、没有路）**：`attacker` 在 Impact 侧命中 **890 段 / 160 页 / 112 个块名**，占含 Impact 段（7,434）的 **12.0%**、占全树段的 **9.7%**。这 890 段拆成句以后，句法骨架高度收敛：**878 句**是"起点 + 情态 + 动词"，**12 句**是"某事 + 使役 + 攻击者 + 动词"。但表示"通过什么进来"的词几乎不存在：`via` **23 段**、`by + 动名词` **2 段**（且只有 `spoofing` 一个动名词）、`over the` **0 段**、`by sending` / `by visiting` / `by mounting` / `by opening` / `by parsing` / `by playing` / `by connecting` / `by restoring` **各 0 段**、`crafted … by` **0 段**。表示先后次序的词全部为零：`then` / `first` / `chain` / `kill chain` / `foothold` / `initial access` / `once` / `escape` / `pivot` / `staged` / `subsequent` / `prior to` / `sequence` / `steps` / `prerequisite` **Impact 与 Description 双侧 0 段**。**T2 写的是"谁能做到什么"，不是"谁先做什么再做什么"。**

**反差二（attacker 只活在 Impact 栏）**：`attacker` 在 **Description 侧 0 段**——不是"很少"，是 0；而这 890 段里 **890 / 890 全部同时有 Description 栏**。同时全语料的在野利用标记句（`Apple is aware of a report …`）**103 段**也只写在 Impact 侧、Description 侧 0 段。也就是说：**T2 里"谁"属于影响描述，"改了什么"属于修复描述，两栏之间没有共享的施动者叙述**。任何"Apple 在 Description 里说明了触发方式"的读法都是把两栏当一栏。

**反差三（起点写法很多，但一半句子不写起点）**：起点身份去重后有 **43 种写法**（附表 A 全列，纵向合计 890），从 `a remote attacker`（194）一路到 `an attacker with physical access to the accessory`（1）。而 `attacker` 之后、情态之前的**状语窗口**去重后只有 **38 种**，其中最大一项是**空**：432 句（**49.2%**）不写任何起点限定。把 890 段按起点做**闭合分区**（固定判序 Q1→Q6，第 4 章），得到 **230 + 129 + 87 + 194 + 64 + 186 = 890**，未归类 **0 段**。三分之一的段（Q6 + Q4 + Q5 = 444 段）只给"远程 / 本地 / 什么都不给"这三个字。

**反差四（"路径"这个词不在讨论路径）**：`path` 在 Impact 侧只 **9 段**，分布在 4 个块名（`Installer` 3、`zip` 3、`Dev Tools` 2、`Vim` 1），三种句式全部是文件系统语义——`check the existence of an arbitrary path on the file system`、`A path handling issue was addressed with improved validation`、`Processing an overly large path value may crash a process`。**这 9 段与 P 的交集是 0。** `reachable` 在 Impact 侧 **0 段**，在 Description 侧 5 段，5 段全是同一句 AirDrop 的 `A reachable assertion was addressed with improved input validation.`——这里的 reachable 是**断言语义**（可到达的断言），不是"可达性"。**选题用词的中文"路径"在英文措辞层没有对应概念，只有一个语义撞车的同名词。**

于是本篇的问题依次是：

**问题一（第 3、6、7 章）** 890 段到底写了什么。答案：三段骨架 `起点 → 情态 → 动作`。情态只有 4 个词，`may` 占 **868 / 878 = 98.9%**；动作尾巴 106 种，但首词只有 44 个，`cause` 一个词占 **392 / 878 = 44.6%**。

**问题二（第 4、5、8 章）** 起点能不能闭合分区。能：六类、未归类 0；但**判序敏感**，且分区结果与"写法 distinct 数"是两个不同层面（43 种写法归成 6 类）。使役句式只有 **12 段**，且 12 段的宾语动作完全相同（`write arbitrary files`）。

**问题三（第 9–12 章）** 通道与顺序能不能建表。不能。`via` 的 23 段只有 **5 种写法**；顺序词全 0；`when` 34 段、`if` 6 段不构成通道表。唯一成规模的前置条件表达是**否定式**的 `without`：**178 段 / 18 种写法**（`user consent` 47、`authentication` 42、`prompting the user` 16…）。后果句与路径句**交集为 0**（`lead to` 1,738 段 ∩ P = 0，`result in` 163 段 ∩ P = 0）。

**问题四（第 13–16 章）** 不写 `attacker` 的时候，路径感由谁承担。答案：由**施动者替代词**承担——但必须先分清两个口径。**"冠词 + 名词"式的完整施动者措辞**：`an app` 起头 **3,156 段**、`a process` **3 段**、`a shortcut` **75 段**、`a local user` **35 段**、`a person` **11 段**、`third party` **6 段**——它们与 P 的交集**全为 0**。**宽词族**：`app` 词族 **4,172 段**（∩P = **118**）、`user` 词族 **1,651 段**（∩P = **127**）、`shortcut` 词族 **101 段**、`process` 词族 **756 段**（∩P = **27**）——共现高是假象，命中的是 `sensitive user information`、`an arbitrary path` 这类**受事**用法，不是施动者（第 13 章给全表）。**所以"T2 用 app 还是 attacker 承担路径"只能按完整措辞问，按词族问会得出虚假结论。**中文手册层（T1）侧，`攻击者` **60 次 / 29 页**，但 `触发` **5 次**、`路径` **4 次**、`链条` / `利用链` / `漏洞利用` / `网络位置` / `近场` / `可达` **各 0 次**。

# 1 操作性定义与分母

**定义一（路径全集 P）**：一条 **Impact** 文本里出现 `attacker`（带词边界 `(?<![\w-])attacker(?![\w-])`，不区分大小写），即算 P 的一段。**这是代理定义**：T2 没有"攻击路径"字段，也没有任何"步骤"栏，本篇用**唯一在 Impact 里成规模出现的施动者名词**作为路径叙述的锚。P = **890 段 / 160 页 / 112 个块名**。

**故意不扩集**：把 `attacker` 扩成"施动者词族"（加 `app`、`process`、`user`、`shortcut`、`extension`、`third party`）会让集合从 890 涨到数千段，但那就不再是"攻击者路径"而是"Impact 全文"。**本篇把这条线画在 `attacker` 这个词上**，第 13 章单独清点替代词，并给出它们与 P 的交集，不做合并。`attacker` 的**复数形式 0 段**、`the attacker` **0 段**、`another attacker` **0 段**、`adversary` / `hacker` / `criminal` / `someone` / `victim` 各 **0 段**——所以 890 段就是这个词的全部，扩词没有意义。

**定义二（句子）**：Impact 字段内部多句以 `" || "` 连接（#111 第 1 章的字段拼接规则），本篇**先拆句再统计**，切句规则为 `(?<=[.!?])\s+(?=["A-Z])`。必须拆的原因：在野利用标记句 `Apple is aware of a report …` 与主句**粘在同一个 Impact 字段里**，不拆句就会把"后果"和"路径"混在同一段文本上（第 12、14 章的结论依赖这一点）。890 段拆出 **920 句**，其中 **890 句含 `attacker`、30 句不含**；不含的那 30 句 distinct 只有 **4 种**，全部是在野标记与 CVE 补记（第 14 章）。

**定义三（三段骨架）**：对含 `attacker` 的句子，**起点身份** = 句首到情态动词之前的窗口（≤12 词）；**情态** = `may | might | could | can | will | would`；**动作尾巴** = 情态（及其后的 `be able to`）之后到句末。窗口上限 **12 词**、尾巴不截断，两条规则必须与引用一起写清（附表 A、B 的行长由此决定）。

**分母声明**：沿用 #111 第 1 章的两版式域。全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 7,434 段含 Description**；域 A **9,045 段 / 246 页 / 470 名 / 7,346**；域 B **91 段 / 11 页 / 26 名 / 88**。P 的分域：**域 A 885 段（155 页 / 112 名）+ 域 B 5 段（5 页 / 3 名）= 890**。域 B 那 5 段的块名是 `Bluetooth`(1)、`CoreCrypto`(3)、`LoginWindow`(1)。**只认 `<h3 class="gb-header">` 的解析器会让这 5 段消失，且 `CoreCrypto` 与 `LoginWindow` 两个块名从路径统计中消失。**

**词形纪律**：所有词计数带词边界；复数写进交替（`paths?`、`chains?`）；词族写 `exploit(?:s|ed|ing|ation)?` 而不是 `exploit\w*`；`bypass(?:es|ed|ing)?` 全树 **291 段**（与 #110 的域 A 值 288 是同一指标的两域，#112 验证十三已对账）。

# 2 结论速览（每条都可按第 17 章复算）

1. P = **890 段 / 160 页 / 112 名**，占含 Impact 段 **12.0%**、占全树段 **9.7%**；**Description 侧 0 段**，而这 890 段 **890 / 890 都有 Description**。
2. **起点闭合分区六类、未归类 0**：网络位置 **230**、物理持有 **129**、已具备能力 **87**、只写 remote **194**、只写 local **64**、裸 attacker **186**；之和 **890**。
3. 起点身份 **43 种写法**（附表 A，纵向 **890**），状语 **38 种**（其中"空"**432 句 = 49.2%**），`attacker` 左侧限定只有 **5 种**（`an` 612 / `a remote` 194 / `a local` 64 / `allow an` 12 / `a malicious` 8，和 **890**）。
4. **句式三分**：`情态 + be able to` **798**、`情态 + 动词` **80**、`allow an attacker to` **12**，未归类 **0**，合计 **890**。情态配对 **878**，四个词：**may 868 / can 5 / could 4 / might 1**。
5. 动作尾巴 **106 种 / 878 句**（附表 B，纵向 878；另 **12 句**属使役句式、无此尾巴），首词只有 **44 个**；`cause` **392**、`view` **80**、`bypass` **61** 三个词占 **533 / 878 = 60.7%**；单条最大尾巴 `cause a denial-of-service` **110 句**。
6. **通道层不成表**：`via` **23 段 / 5 种写法**（`an icloud link` 9、`an incorrect connection` 8、`accessibility features` 3、`a file url` 2、`email` 1）；`using` 21、`through` 16、`when` 34、`if` 6、`after` 22、`while` 4、`from a` 7、`in certain configurations` 1；`by + 动名词` **2 段**（动名词只有 `spoofing`）；`over the`、`crafted … by`、`requires an attacker`、`would need to`、`in order to`、`by sending/visiting/mounting/opening/parsing/playing/connecting/restoring` **全 0 段**。
7. **顺序层全空**：`then` / `first` / `chain` / `kill chain` / `foothold` / `initial access` / `once` / `escape` / `pivot` / `reach` / `vector` / `entry point` / `attack surface` / `trigger` / `steps` / `sequence` / `prerequisite` / `threat model` / `threat actor` / `CVSS` / `score` / `severity` **Impact 与 Description 双侧 0 段**。
8. **"路径"字面词是撞车不是证据**：`path` Impact **9 段**（4 名 / 3 种句式，全是文件系统路径语义），**∩P = 0**；`reachable` Impact **0** / Description **5**（5 段全是 AirDrop 的 `A reachable assertion …`）；`route` Impact **0** / Description **4**。
9. **前置条件只以否定式成规模**：`without` **178 段 / 97 页 / 37 名**，18 种写法（`user consent` 47、`authentication` 42、`prompting the user` 16、`user authorization` 9、`user permission` 9、`having the private key` 7、`pairing` 7…），纵向合计 **178**；P 内 **20 段**。
10. **后果句与路径句互不相交**：`lead to` **1,738 段** ∩P = **0**；`result in` **163 段** ∩P = **0**；P 内的 `may cause` 只有 **46 个窗口 / 2 种宾语**（`an unexpected app termination` 38 + `… or arbitrary code execution` 8）。全树 `lead to` 宾语 **35 种**、`result in` 宾语 **11 种**。
11. **使役句式只有 12 段且完全同形**：`Unpacking/Extracting a maliciously crafted archive may allow an attacker to write arbitrary files`，块名 `Compression` 6 + `file_cmds` 6，`attacker to` 之后的窗口 distinct = **1**。P 内含 `crafted` 的段 **24 / 890**，宾语只有 **3 种**（`archive` 12、`wi-fi packets` 7、`bluetooth packets` 5）。
12. 交集纪律：P ∩ 输入并集（3,245）= **317**；P ∩ 越级并集（602）= **154**；三者同交 = **92**；P ∩ `bypass`（291）= **61**；P ∩ `arbitrary code execution`（218）= **28**；P ∩ `sandbox`（418）= **1**；P ∩ `memory corruption`（218）= **0**；|P ∪ 输入| = **3,818**。
13. **在野标记只落在一种起点**：`Apple is aware of a report …` **103 段 / 74 页 / 12 名**，Description 侧 0；P ∩ 在野 = **25 段**，这 25 段的起点身份**全部**是"已具备读写能力"类（`arbitrary kernel read and write` 16 + `memory write capability` 5 + `arbitrary read and write` 4 = 25），即 **25 / 87 = 28.7% 的 Q3 段带在野标记，其余五类各 0**。
14. 组件分布：P 落在 **112 个块名**上，头部 `Kernel` 111、`AirPlay` 70、`Security` 50、`Siri` 40、`Wi-Fi` 40、`Model I/O` 33、`mDNSResponder` 33、`Bluetooth` 29；**19 个块名只有 1 段**。P 的页 top：`149035` 24 段、`149042` 19、`128071` 19。
15. T1 中文层 **62 词**普查（附表 C）：`攻击者` **60 次 / 29 页**、`攻击` 162 / 50、`攻击面` 12 / 9；但 `触发` **5**、`路径` **4**、`步骤` **16**、`顺序` **1**、`链条` / `利用链` / `漏洞利用` / `网络位置` / `近场` / `可达` / `暴露面` **各 0 次**。

# 3 P 的规模与字段单边性

**规模**。P = **890 段**，来自 **160 页**（257 页里的 62.3%）与 **112 个块名**（472 名里的 23.7%）。占含 Impact 段 **12.0%**、占全树段 **9.7%**。作为对照：#112 的输入并集是 3,245 段（43.7%）、#111 的越级并集是 602 段。**"攻击者"这个词覆盖的是七分之一的段，不是三分之一。**

**字段单边性（本篇最重要的一个 0）**：`attacker` 在 **Description 侧 0 段**。同时，P 的 890 段里 **890 段全部有 Description 栏**——即这 890 个条目都写了"修了什么"，却没有一条把施动者写进"修了什么"。这不是抽样结论，是全量结论：0 段 = 一段都没有。

这条不对称说明 T2 的两栏是**两套语言**：Impact 回答"谁会因此能做什么"，Description 回答"代码改了什么"。第 10 章会看到同一个模式在词汇层重复——`over the`、`after`、`through`、`by + 动名词` 这些通道词在 Description 侧有 **10、127、247、378 段**，在 Impact 侧只有 **0、22、16、2 段**。也就是说：**通道词汇在 T2 里是存在的，但它被用来描述"修复动作"，不用来描述"到达动作"。**

**句子构成**。890 段拆句得 **920 句**：**890 句含 attacker**（每段恰有一句主句）、**30 句不含**。这 30 句 distinct 只有 **4 种**，逐条读后全部是在野标记与 CVE 补记（`Apple is aware of a report that this issue may have been exploited.` 等）。所以"多句段"不是路径叙述的复杂度，而是**披露状态的附加语**（第 14 章）。

**段长**。P 段的词数分布：**≤12 词 321 段**、**13–24 词 540 段**、**>24 词 29 段**。三分之二段落在 13–24 词——即"起点状语 + 主句 + 一个动作尾巴"的长度。这解释了第 7 章的骨架收敛：**段长没有自由度可供放下一步。**

# 4 起点的闭合分区（Q1→Q6，未归类 0）

判序固定，先命中先归类，全部作用在**段级 Impact 文本**上：

| 类 | 判据（`attacker` 之后 50–60 词窗内的关键短语） | 段 | 页 | 名 |
|---|---|---:|---:|---:|
| Q1 网络位置 | `in a privileged` / `on the local` / `on the network` / `in radio` / `in wireless` / `physical proximity` / `network position` | 230 | 85 | 21 |
| Q2 物理持有 | `with physical access` | 129 | 68 | 36 |
| Q3 已具备能力 | `with arbitrary` / `with root` / `with memory` / `that has already` / `with a compromised` / `with control` / `with a paired` / `with access` / `with host-limited` / `with knowledge` / `with user` / `trust-paired` / `within bluetooth range` / `in bluetooth range` | 87 | 60 | 15 |
| Q4 只写 remote | `a remote attacker` | 194 | 89 | 26 |
| Q5 只写 local | `a local attacker` | 64 | 36 | 14 |
| Q6 裸 attacker | `an attacker`（且未命中 Q1–Q3） | 186 | 108 | 43 |
| Q7 未归类 | — | **0** | — | — |
| 合计 | | **890** | | |

**读法**。三分之一的段（Q4 + Q5 + Q6 = **444 段 / 49.9%**）的起点信息只有 `remote` / `local` / 无。另一半（Q1 + Q2 + Q3 = **446 段**）写了位置、持有关系或已具备的能力——**这才是 T2 愿意写成名词的那部分"前置条件"**。

**判序敏感（必须声明）**：把 Q4/Q5 挪到 Q1 前面、并把"已具备能力"改判为不与"网络位置"争抢同一短语，同一批 890 段会得到 **194 / 194 / 175 / 170 / 114 / 43**（remote / 网络位置(狭) / 裸 / 物理与近场 / 已具备 / local）——同样闭合到 890，但每一类都不同。**分区不是文本的事实，是本篇的切法**。引用第 4 章任一数字时必须带上"判序 Q1→Q6"这个限定。

**段级与句级的唯一差额**：附表 A 的"起点判定"列把同一判序作用在**句级窗口**上，因此使役句式的 12 段单列为 **S3**，得 **Q1 230 / Q2 129 / Q3 87 / Q4 194 / Q5 64 / Q6 174 / S3 12 = 890**。**除 Q6 外逐类与上表相等，且上表 Q6 = 186 = 附表 A 的 174 + S3 的 12**——因为使役句式的 Impact 文本同样含 `an attacker`，段级判定时落进 Q6。这条对账是验证二的全部内容。

**Q2 与 Q3 的界线**：`with physical access` 归 Q2，`with root privileges` / `with memory write capability` 归 Q3。这一段有 15 个块名（Q3），**全部是"攻击者已经具备某种能力"的措辞**——本篇把它读作"路径的后半段"：T2 用 Q3 承认了**链式**的存在，但承认的方式是把它写成起点，而不是写成步骤（第 14 章的在野分布正好落在这里）。

# 5 起点写法、状语与左侧限定

三个不同层面的"起点表"，不可混用：

**（a）左侧限定**——`attacker` 前面那几个词。只有 **5 种**，合计 **890**：`an` **612**、`a remote` **194**、`a local` **64**、`allow an` **12**、`a malicious` **8**。这是**冠词层面的全部修饰自由度**，说明 T2 对攻击者的定性只有"远程 / 本地 / 恶意"三档加一个使役位。

**（b）起点身份（句级窗口）**——句首到情态动词之前，≤12 词。**43 种，合计 890**（附表 A 全列）。头部：`a remote attacker` 194、`an attacker` 174、`an attacker on the local network` 103、`an attacker in a privileged network position` 80、`a local attacker` 64、`an attacker with physical access` 45、`an attacker with physical access to a locked device` 44、`an attacker in physical proximity` 22、`an attacker with arbitrary kernel read and write capability` 16。**长尾很薄但确实存在：只出现 1 次的写法 10 种、出现不超过 3 次的写法 16 种**，包括 `an attacker within bluetooth range`、`an attacker with a usb-c connection to an unlocked device`、`an attacker with knowledge of a standard user's credentials`、`in certain configurations, an attacker with host-limited sudo access`。**distinct = 43 与"10 种写法各只出现 1 次"同时成立**，说明多数写法是同一形状的槽位替换（`with physical access to <设备状态>`）。

**（c）起点状语**——`attacker` 之后、情态之前的窗口。**38 种，合计 878**，最大项是 **(空) 432 句（49.2%）**，即**非空状语只有 446 句（50.8%）**。这 446 句里，表示"位置"的 9 种共 **232 句**（`on the local network` 103、`in a privileged network position` 80、`in physical proximity` 22、`in radio range` 9、`in wireless proximity` 8、`on the network` 5、`in a privileged position` 3、`in bluetooth range` 1、`within bluetooth range` 1），其余 **214 句**全部是"持有 / 已具备"类（`with physical access` 45 + `… to a locked device` 44 + `with arbitrary read and write` 17 + `… kernel …` 16 + `with root privileges` 9 + `with a compromised intermediate certificate authority` 8 …，**232 + 214 + 432 = 878** ✓）。**注意 446 与第 4 章 Q1+Q2+Q3 = 446 段数值相同，但一个是句、一个是段，本篇未做成员比对，不断言"同一批条目"。**

**两条窗口纪律**（第 17 章复算要点）：`(b)` 与 `(c)` 都以情态动词为右界，`extracting / unpacking a maliciously crafted archive` 这两条之所以出现在 `(b)` 表里，是因为它们的句子**主语不是攻击者**、`attacker` 出现在宾语位、窗口被截到 `allow` 之前——这两条就是第 8 章的 12 段。而 `when your headphones are seeking a connection request …` 这一条是**从句开头**、`attacker` 被挤到 12 词窗之外，属于窗口上限造成的截断，不是新身份。**这类边缘条目必须逐条读，不能只报 distinct 数。**

# 6 情态层与句式三分

**句式三分（固定判序，段级，未归类 0）**：

| 句式 | 判据 | 段 |
|---|---|---:|
| S1 情态 + `be able to` + 动词 | `attacker … may be able to …` | **798** |
| S2 情态 + 动词（无 `be able to`） | `attacker … may cause / may gain …` | **80** |
| S3 `allow an attacker to` 使役 | 攻击者在宾语位 | **12** |
| S4 未归类 | — | **0** |
| 合计 | | **890** |

**情态清点**。S1 + S2 = **878 句**有"施动者 + 情态"配对，情态只有 **4 个词**：**may 868 / can 5 / could 4 / might 1**。`will`、`would`、`shall` **各 0**。**868 / 878 = 98.9% 的句子用同一个情态词。** 这不是文风巧合：`may` 让句子读作"可能性陈述"而非"能力陈述"，同时避免承诺任何发生条件。

**S2 的 80 段只用 6 个动词**：`cause` 46、`gain` 19、`corrupt` 6、`also` 4、`input` 4、`unlock` 1（合计 80）。S2 是 S1 的例外形式，例外的方式是把 `be able to` 省掉、直接接 `cause` / `gain`。逐条读 S2 例句可见一个副产物：`An attacker with access to calendar data could also read reminders`（`could` 4 段之一）是全语料唯一把两个动作并列在同一主语下的句子——**这是 T2 最接近"两步"的措辞，仍然是一步。**

**起点身份 × 情态交叉表**（闭合到 890）：

| 左侧限定 | may | could | can | might | 使役/无 | 合计 |
|---|---:|---:|---:|---:|---:|---:|
| `an attacker` | 602 | 4 | 5 | 1 | 0 | 612 |
| `a remote attacker` | 194 | 0 | 0 | 0 | 0 | 194 |
| `a local attacker` | 64 | 0 | 0 | 0 | 0 | 64 |
| `allow an attacker` | 0 | 0 | 0 | 0 | 12 | 12 |
| `a malicious attacker` | 8 | 0 | 0 | 0 | 0 | 8 |
| | **868** | **4** | **5** | **1** | **12** | **890** |

这张表的意义在于**零元的分布**：`remote` 与 `local` 两行**只有 `may`**，`could` / `can` / `might` 全部集中在裸 `an attacker` 行。也就是说：**当 T2 写明起点时，句式最僵硬；起点越含糊，情态越有变化。**

# 7 动作层：44 个首词、106 条尾巴

**尾巴表**。S1 + S2 的 **878 句**去掉情态与 `be able to`，剩下动作尾巴，去重 **106 种**（附表 B 全列，纵向 **878**）；S3 的 **12 句**不在此表（它们没有"情态之后"的位置，见第 8 章）。头部：`cause a denial-of-service` **110**、`cause unexpected app termination` **49**、`cause unexpected system termination` **45**、`view sensitive user information` **44**、`cause unexpected system termination or corrupt kernel memory` **40**、`cause an unexpected app termination` **38**、`corrupt process memory` **27**、`cause unexpected application termination or heap corruption` **26**、`intercept network traffic` **23**、`use siri to access sensitive user data` **17**。**同一件事的多种写法被 distinct 分开计**（`cause unexpected app termination` 49 与 `cause an unexpected app termination` 38 是两条），所以 **106 不是 106 种动作**，是 106 种**写法**——第 19 章误读三处理这件事。

**首词表**。尾巴首词只有 **44 个**，合计 **878**：`cause` **392**、`view` **80**、`bypass` **61**、`access` **30**、`corrupt` **27**、`leak` **25**、`intercept` **23**、`use` **20**、`gain` **19**、`spoof` **16**、`elevate` **15**、`perform` **15**、`read` **15**、`execute` **14**、`modify` **13**、`track` **12**，往下 `delete` 9、`create` 8、`determine` 8、`issue` 8、`decrypt` 7、`turn` 6、`authenticate` 5、`misuse` 5、`also` 4、`alter` 4、`input` 4、`overwrite` 4、`share` 4、`control` 3、`force` 3、`impersonate` 3、`disable` 2、`discover` 2、`inject` 2、`override` 2，再往下 `break` / `extract` / `listen` / `programmatically` / `see` / `silently` / `take` / `unlock` **各 1**。

**三个结构性观察。**（一）`cause` 独占 **44.6%**，且它的宾语只有 13 种、全部是崩溃 / 终止 / 拒绝服务 / 内存破坏类——**T2 的"动作"有一半其实是"不动作"，即让东西坏掉。**（二）真正的读写类动词（`view` 80、`access` 30、`read` 15、`leak` 25、`corrupt` 27）合计 177，全部指向**数据**而不是**流程**。（三）**`bypass` 61 段是 P 与 #110 的唯一大交集**（P ∩ `bypass` = 61，占 `bypass` 全树 291 的 21.0%）：**"绕过"在 T2 里是被写成攻击者动作的，这是措辞层承认路径的唯一位置。**

**P ∩ `memory corruption` = 0（一个必须点名的零）**：`memory corruption` 全树 **218 段**，与 P 的交集 **0 段**。对照第 7 章的 `corrupt process memory` 27 段与 `corrupt kernel memory` 40 段——**内存破坏在 T2 里是后果名词，不是攻击者动词的宾语**。这条与第 12 章的"后果句 ∩ 路径句 = 0"是同构的。

# 8 使役句式：12 段全录

P 里唯一"主语不是攻击者"的句式是 `X may allow an attacker to V`。**全语料 12 段，逐条如下**（块名 + 原文）：

`Compression`（页 121234、121238、121246、121247、121249、121250）与 `file_cmds`（页 149034、149035、149038、149041、149042、149043）各 6 段，两句原文只有动词差别：

1. `Unpacking a maliciously crafted archive may allow an attacker to write arbitrary files`（`Compression`，6 段）
2. `Extracting a maliciously crafted archive may allow an attacker to write arbitrary files`（`file_cmds`，6 段）

**这 12 段是 T2 全文里唯一把"一个动作"和"攻击者得到能力"写成因果的句子。**三个限定必须同时给出：（一）**主语是操作**（unpacking / extracting），不是人；（二）`attacker to` 之后的动作窗口 distinct = **1**，只有 `write arbitrary files`；（三）12 段全部命中 #112 的输入并集（P ∩ 输入 ∩ 使役 = 12），宾语是 `archive`。**同一句话在 6 个版本页上重复**，所以"12 段"不等于 12 个缺陷（第 19 章缺口四）。

**没有的东西**：`allow the attacker to` **0**、`allow attackers to` **0**、`allow an app to` **0**、`could allow` **0**、`will allow` **0**、`allows`（现在时）**0**、`let …` **0**、`make it possible` **0**、`enable … to` **20 段**（其中 P 内 0——`enable` 从不接攻击者）。**使役句式在 T2 里不是一个可用模板，是 12 次偶发。**

# 9 通道层：`via` 23 段与一批 0

"通过什么进来"在 T2 里的全部词汇量：

| 词 / 构式 | Impact 段 | 页 | 名 | Description 段 | P 内段 |
|---|---:|---:|---:|---:|---:|
| `via` | 23 | 23 | 5 | 0 | 4 |
| `using` | 21 | 21 | 6 | 10 | 12 |
| `through` | 16 | 16 | 4 | 247 | 7 |
| `by + 动名词` | 2 | 2 | 1 | 378 | 2 |
| `over the` | 0 | 0 | 0 | 10 | 0 |
| `when` | 34 | 31 | 13 | 13 | 2 |
| `if` | 6 | 6 | 1 | 0 | 0 |
| `after` | 22 | 19 | 7 | 127 | 0 |
| `while` | 4 | 4 | 4 | 0 | 1 |
| `from a` | 7 | 7 | 4 | 0 | 3 |
| `in certain configurations` | 1 | 1 | 1 | 0 | 1 |
| `crafted … by` | 0 | 0 | 0 | 0 | 0 |
| `requires an attacker` | 0 | 0 | 0 | 0 | 0 |
| `would need to` | 0 | 0 | 0 | 0 | 0 |
| `in order to` | 0 | 0 | 0 | 0 | 0 |
| `by sending` / `by visiting` / `by mounting` / `by opening` / `by parsing` / `by playing` / `by connecting` / `by restoring` | 各 0 | 0 | 0 | — | 0 |

**`via` 的 23 段只有 5 种宾语**：`an icloud link` **9**、`an incorrect connection` **8**、`accessibility features` **3**、`a file url` **2**、`email` **1**（纵向 **23**）。注意其中两种（`an incorrect connection`、`accessibility features`）**不是外部投递通道**：前者是"连错了网络"的缺陷表现，后者是系统功能的正当入口。**`via` 一词在 T2 里连"通道"这一个语义都不纯。**

**`by + 动名词` 只有 2 段**，动名词只有 `spoofing` 一个；同时 Description 侧 `by + 动名词` 有 **378 段**。**这是通道词的使用位置被系统性挪开的直接量化**：修复说明里"通过改进 X"（by improving）可以写 378 次，影响说明里"攻击者通过 Y 进来"只写 2 次。

**`when` 34 段的窗口只有 10 种**（`processing a maliciously crafted photo` 9、`the 'load remote images' setting is` 8、`remotely controlling a device over facetime` 5、`using print preview` 3、`a shortcut fails to launch another` 3、其余各 1–2）；`if` 6 段只有 1 种（`if the user has visited a given`）。**起头就是 `when` / `if` / `in certain` 的 Impact 段一共只有 2 段。** 条件从句在 T2 里既不表通道也不表触发顺序，它们表的是**配置状态**——`when the 'load remote images' setting is` 是唯一一类接近"触发条件"的措辞，且只有 8 段。

# 10 顺序层：路径词汇的全体缺席

**双侧 0 段的词（Impact 与 Description 都为 0）**：`then`、`first`、`chain`、`kill chain`、`foothold`、`initial access`、`escape`、`pivot`、`reach`（含 `reaches` / `reached` / `reaching`）、`attack vector`、`vector`、`entry point`、`attack surface`、`trigger`（含词族）、`steps`、`sequence`、`prerequisite`、`threat model`、`threat actor`、`TCC`、`CVSS`、`score`、`severity`、`once`、`make it possible`、`combined with`、`staged`、`subsequent`、`prior to`、`requires that`、`must be`、`already have`、`obtain`、`user interaction`、`user input`、`locally`。

**非零但语义不相关的四个**（这一组是误读高发区）：

| 词 | Impact | Description | 实际语义 |
|---|---:|---:|---|
| `path` | **9** | 246 | 文件系统路径（`an arbitrary path on the file system`、`path handling issue`、`an overly large path value`）；**∩P = 0** |
| `reachable` | **0** | **5** | AirDrop 的 `A reachable assertion was addressed`——断言语义；5 段全同句 |
| `route` | **0** | **4** | 网络路由 / 代码路由，不与 attacker 同段 |
| `already` | **7** | 0 | 全 7 段都在 P 内，且全在 Q3 起点状语里（`that has already achieved kernel code execution`） |

`exploit` 词族 Impact **108 段 / 76 页**、Description **0 段**，其中 **25 段在 P 内**——全部是在野标记句（`may have been exploited`）。**"利用"这个词在 T2 里只用于披露状态声明，不用于路径描述。**

**唯一承认"前一步存在"的措辞**是 Q3 的三类状语：`an attacker that has already achieved kernel code execution`（7 句）、`an attacker with arbitrary kernel read and write capability`（16 句）、`an attacker with memory write capability`（5 句）、`an attacker with arbitrary read and write capability`（9 句）、`an attacker with root privileges`（9 句）。**它们把链的前半段压缩成一个身份定语**，而不是写成两个句子。**这就是本篇对"路径"能给出的最强肯定结论：T2 承认链，但只在名词层承认。**

# 11 `without`：以否定式写成的前置条件

`without` 在 Impact 侧 **178 段 / 97 页 / 37 名**，**Description 侧 0 段**——与 `attacker` 一样是单边词。178 段的 `without` 宾语去重后 **18 种**，纵向合计恰 **178**：

`user consent` **47** · `authentication` **42** · `prompting the user` **16** · `user authorization` **9** · `user permission` **9** · `consent` **7** · `having the private key` **7** · `pairing` **7** · `permission` **5** · `user confirmation` **5** · `valid credentials` **5** · `an entitlement check` **4** · `having app management permission` **4** · `an indicator` **3** · `prompting for the password` **3** · `restrictions` **3** · `authentication in assistive access` **1** · `authorization` **1**

**这是全语料里规模最大、结构最清楚的"前置条件表"**，而它完全是**否定式**的：T2 不说"攻击者需要什么"，说"攻击者不需要做什么就能做成"。把 18 种写法按语义归成**六类互斥**（每类内不重复、类间不相交，纵向 **178**）：

| 类别 | 包含的写法 | 段 |
|---|---|---:|
| 用户同意 / 提示类 | `user consent` 47、`prompting the user` 16、`user authorization` 9、`user permission` 9、`consent` 7、`permission` 5、`user confirmation` 5、`prompting for the password` 3、`authorization` 1 | **102** |
| 认证 / 凭据 / 权限检查类 | `authentication` 42、`valid credentials` 5、`an entitlement check` 4、`restrictions` 3、`authentication in assistive access` 1 | **55** |
| 配对类 | `pairing` 7 | **7** |
| 私钥类 | `having the private key` 7 | **7** |
| 应用管理类 | `having app management permission` 4 | **4** |
| 界面指示器类 | `an indicator` 3 | **3** |
| 合计 | | **178** |

**两条归类决定必须写出**：`having the private key` 7 段既是凭据也是密码学前提，本篇**单列不并入认证类**（并入则该类 62、总数不变）；`an indicator` 3 段指"无界面提示"，与同意不同义，**单列**。`authentication in assistive access` 1 段归认证类而非同意类。**本篇只给这一套切法，不给"缺防线索"的权威分类。**

P 内 `without` 只有 **20 段**——即 890 段攻击者句里，只有 20 段同时用了否定式前置条件。**其余 158 段 `without` 句子的主语不是攻击者**（多为 `an app may … without user consent`）。这解释了 #112 的口径决定：**施动者与受事分表**在这里同样成立——`without` 是"缺什么防线"，不是"谁走过哪条路"。

# 12 后果句与路径句互不相交

**两个零交**。`lead to` 词族在 Impact 侧 **1,738 段 / 207 页 / 78 名**，与 P 的交集 **0 段**；`result in` 词族 **163 段 / 92 页 / 20 名**，与 P 的交集 **0 段**。

这不是"比例小"，是**逐段成员求交后为 0**（第 17 章断言九给出复算方式）。**T2 的两类句子在词汇上完全分开**：一类主语是攻击者、谓语是动作；另一类主语是缺陷或操作、谓语是 `leads to` / `results in` + 后果名词。**两栏之间没有交接句**——即没有任何一段既写"攻击者 …"又写"… leads to 后果"。

`lead to` 的宾语 **35 种 / 1,738 窗口**，头部：`an unexpected process crash` **278**、`unexpected app termination` **205**、`an unexpected safari crash` **198**、`memory corruption` **184**、`a denial-of-service` **138**、`arbitrary code execution` **123**、`unexpected app termination or corrupt process memory` **111**、`an unexpected app termination` **104**。`result in` 宾语 **11 种 / 163 窗口**，头部 `disclosure of process memory` **71** + `the disclosure of process memory` **49**（同一短语带不带冠词两种写法合计 120）、`memory corruption` **14**、`code execution` **8**。

**P 内的因果动词只剩 `may cause`：46 个窗口 / 2 种宾语**（`an unexpected app termination` 38、`an unexpected app termination or arbitrary code execution` 8）。也就是说：**当主语是攻击者时，"导致"只能接两种后果；一旦要写 35 种后果，主语就必须换成缺陷。** 第 7 章的 `cause` 392 段与此并不矛盾——那 392 段里攻击者是**动作**主语（`cause a denial-of-service`），而 `lead to` 是**结果**句式，两者是不同的语法位置。

**这条发现直接否掉一种常见写法**："文件进来 → 导致内存破坏 → 攻击者拿到代码执行"。三段里前两段与第三段**不同段**（`lead to` ∩ P = 0），第一段属于 #112 的输入并集、第三段属于 P；两集合交集 317 段里**没有一段同时含 `crafted` 输入名词与 `lead to`**，且 P ∩ `memory corruption` = 0。**措辞层不提供这条因果链的单句证据。**

# 13 施动者的替代措辞

不写 `attacker` 时，Impact 用谁做主语。句级起头前三词普查（**7,566 句 / distinct 153**，比含 Impact 段多 132 句是因为多句段）头部：

`a malicious app` **254** · `a remote attacker` **194** · `a malicious website` **94** · `a malicious application` **82** · `a local attacker` **64** · `a maliciously crafted` **61** · `a sandboxed app` **58** · `a remote user` **37** · `a local user` **35** · `a local app` **18** · `a person with` **11** · `a logic issue` **10** · `a malicious hid` **10** · `a non-privileged user` **9** · `a download's origin` **8** · `a malicious accessory` **8** · `a malicious attacker` **8**

**段级替代词族与 P 的关系**（Impact 段数；"段内"= 该措辞出现在同一段任意位置，"起头"= 段首即该措辞）：

| 施动者措辞 | 段内 | 起头 | ∩P | 说明 |
|---|---:|---:|---:|---|
| `an app` | **3,163** | **3,156** | **0** | 与 P **一段不共现** |
| `a malicious app` | 337 | 336 | **0** | |
| `a sandboxed app` | 58 | 58 | **0** | 已受限制的本机代码，不是外部攻击者 |
| `a process` | **3** | 0 | **0** | `process` **词族** 756 段（∩P **27**），但其中绝大多数是 `processing`（#112 的准入动词），**不是施动者** |
| `a shortcut` | 75 | 72 | **0** | `shortcut` 一词共 **101 段 / 60 页**，∩P **0** |
| `a local user` | **35** | 35 | **0** | 与 `a local attacker` 的 64 段是**两组不同段** |
| `a remote user` | 37 | 37 | **0** | |
| `a person` | 11 | 11 | **0** | 全部形如 `a person with physical access` |
| `third party` | 6 | 6 | **0** | Description 侧另有 **199** 段 |
| `attacker` | **890** | — | 890 | |
| （对照）`app` 词族 | 4,172 | — | **118** | 宽词族与 P **大量共现** |
| （对照）`user` 词族 | 1,651 | — | **127** | 同上 |

**这张表的要点在最后一列的两组零与非零**：**所有"冠词 + 名词"式的施动者措辞与 P 交集为 0**（`an app` 3,163 ∩P=0、`a process` 3、`a shortcut` 75、`a local user` 35、`a person` 11 全为 0），而**宽词族与 P 共现很多**（`app` 词族交 118、`user` 词族交 127）。原因是清楚的：**P 段里满是 `sensitive user information`、`user data`、`a user's activity` 这类**受事**用法**，它们命中 `user` 这个词，但没有任何施动者含义。**所以"T2 用 app 还是 attacker 承担路径"这个问题只能按"冠词 + 名词"式的完整措辞来问，按词族问会得到虚假的高共现。**

**读法一（`user` 不做分区）**：`user` 词族 1,651 段里，`a remote user`（37）、`a local user`（35）、`a non-privileged user`（9）、`a standard user's credentials` 与 `user data`（受事）混在同一词形下，**本篇放弃 `user` 的施动者分区，只报词族总数与 ∩P = 127**（第 19 章缺口七）。

**读法二（三套句子互斥）**：`attacker`（890）、`an app` 起头（3,156）、`a local user` / `a remote user`（72）三者**没有一段共享**。#112 的反差四给出把 `app` 当输入的失败（并集从 3,245 涨到 6,613）；本篇的对偶结论是：**把 `app`、`process`、`user` 当"攻击者"同样错误**——它们各自有句子，但那些句子不写 attacker，也不写起点。

**`a malicious attacker` 8 段是 P 内部的一个子集**（全部落 Q3、∩越级 = 8），**不是**与 `a malicious app`（337 段）平行的另一类施动者。

# 14 在野标记只落在一种起点

**在野披露句**（`Apple is aware of a report …`）：**103 段 / 74 页 / 12 个块名**，**Description 侧 0 段**，distinct 句子 **10 种**。头部写法：`… may have been exploited.` **24**、`… may have been exploited in an extremely sophisticated attack against specific targeted individuals.` **20**、`… against specific targeted individuals on versions of iOS before iOS 26.` **19**、`… actively exploited on Intel-based Mac systems.` **10**。相关细分：`may have been exploited` **85 段**、`targeted individuals` **59 段**、`extremely sophisticated attack` **59 段**。

**P ∩ 在野 = 25 段。**第 3 章说过，890 段拆出的 920 句里那 30 句不含 attacker 的全部就是这些标记句——即 **25 段是"攻击者句 + 在野句"同段**，其余 78 段在野标记**不与 attacker 同段**。

**这 25 段的起点身份全部同类**（逐段读 `attacker` 之后的状语）：`with arbitrary kernel read and write capability` **16**、`with memory write capability` **5**、`with arbitrary read and write capability` **4**——**合计 25，全部属于 Q3"已具备能力"**，其余五类各 **0**。换算：Q3 有 87 段，其中 25 段带在野标记（**28.7%**）；Q1 的 230 段、Q4 的 194 段、Q6 的 186 段带标记的都是 0。

**这条分布是本篇最有信息量的一个结果**，它说明：**在 T2 的措辞里，"已被在野利用"从来不同时声明攻击者从哪里进来；它只声明攻击者已经具备什么。** 换句话说，**在野利用的证据与"远程 / 网络位置 / 物理持有"这些起点从不共现**。这不能反推为"这些起点不会被在野利用"（第 19 章误读一），它只能说明披露文本的写法。

**块名侧的集中**：103 段在野标记只落在 **12 个块名**上（对照 P 的 112 名）。**披露集中在少数部件**，与 P 的分布（`Kernel` 111 / `AirPlay` 70 / `Security` 50）不是同一个集合；两者的交叠需要按成员求，本篇未建 CVE 去重键（第 19 章缺口四条）。

# 15 组件与页面分布（块名代理）

P 落在 **112 个块名**上（全树 472 名的 23.7%），头部：

`Kernel` **111** · `AirPlay` **70** · `Security` **50** · `Siri` **40** · `Wi-Fi` **40** · `Model I/O` **33** · `mDNSResponder` **33** · `Bluetooth` **29** · `WebKit` **20** · `Accessibility` **15** · `PackageKit` **15** · `Mail` **14** · `Audio` **12** · `VoiceOver` **10** · `dyld` **10** · `AppleMobileFileIntegrity` **9** · `Calling Framework` **9** · `LaunchServices` **9** · `Notes` **9** · `WindowServer` **9** · `802.1X` **8** · `CoreAudio` **8** · `IOHIDFamily` **8** · `Libnotify` **8** · `RTKit` **8**

**19 个块名只有 1 段**。P 的段数第一（`Kernel` 111）与该块名的总段数（#112 的 745 段）之比是 **14.9%**——注意这是"含 attacker 的比例"，不是 #112 的"含输入名词的比例"（8.3%）：**Kernel 的条目更常写攻击者，而不更常写输入对象。**

**页分布头部**（P 的段落在 160 页上）：`149035` **24**、`149042` **19**、`128071` **19**、`149043` 18、`149034` 18、`128072` 18、`128067` 18、`122374` 16、`122373` 16。**同一条攻击者句在相邻版本页重复**（`122373`–`122375` 连三页各 16 段），这是 T2 的版式而非路径的多样性：**按段计数会放大版本重复**，本篇不做缺陷级去重。

**域归属**：域 A 885 段 / 155 页 / **112 名**（全部名都在域 A 出现），域 B 5 段 / 5 页 / 3 名。**域 B 那 5 段是唯一在粗体版式里写攻击者的条目**：`Bluetooth` 1、`CoreCrypto` 3、`LoginWindow` 1。若沿用只认 `<h3>` 的旧刀，P 从 890 变 885（**−0.6%**），且 `CoreCrypto`（3 段）与 `LoginWindow`（1 段）从路径统计消失。**两域必须先分流、后统计。**

# 16 中文手册层（T1）的对应词

《Apple 平台安全保护》zh-CN 转储，窗口取最后一个 `^版权和商标$` 之后至首个含 `Apple Footer` 行之前，排除 `sec2727afbbc`，得 **178 页可用**。**62 词**逐词普查见附表 C；要点：

**成规模的**：`密码` **622 次 / 100 页**、`认证` **372 / 92**、`执行` **246 / 79**、`解锁` **202 / 47**、`授权` **172 / 63**、`内核` **166 / 41**、`攻击` **162 / 50**、`配对` **161 / 35**、`恶意` **104 / 28**、`局域网` **104 / 34**、`无需` **64 / 48**、`蓝牙` **63 / 28**、`远程` **61 / 35**、`攻击者` **60 / 29**、`安全启动` **60 / 22**、`利用` **43 / 29**、`物理` **31 / 20**、`通道` **30 / 14**、`沙盒` **27 / 11**、`同意` **26 / 19**、`交互` **19 / 16**、`步骤` **16 / 11**。

**接近零的**：`触发` **5 次 / 4 页**、`向量` **5 / 5**、`前提` **9 / 9**、`入口` **2 / 2**、`顺序` **1 / 1**、`精心` **1 / 1**、`拒绝服务` **1 / 1**、`旁白` **1 / 1**、`崩溃` **6 / 4**、`路径` **4 / 3**、`伪造` **4 / 4**、`前置` **4 / 2**。

**真零**（不是抓取失败）：`链条`、`利用链`、`漏洞利用`、`近场`、`暴露面`、`可达`、`越狱`、`特制`、`任意代码`、`权限提升`、`提权`、`网络位置`、`无障碍`、`VoiceOver`、`通用密码`——**各 0 次 / 0 页**。

**对照 T2 的两个语义撞车**：T1 的 `攻击面` **12 次 / 9 页**（最集中在《操作系统完整性》3 次），而 T2 的 `attack surface` **0 段**——**"攻击面"是手册层的词，不是响应文本的词**；T1 的 `通道` **30 次 / 14 页**（《车钥匙交易》6、《配备触控 ID 的妙控键盘》5），语义是**物理 / 逻辑传输通道**，不是 T2 里"攻击者经由何处"的通道。

**"路径"在 T1 里也不是攻击路径**：4 次逐条读原句后分布在《签名系统宗卷安全性》2 次、《安全隔区》1 次、《搭载 Apple 芯片的 Mac 上的 Rosetta 2》1 次，四个都是**数据或执行流经的通路**义——`DMA 路径`（AES 引擎内置于 NAND 闪存与主系统内存之间的直接内存访问路径）、`读取路径`（来自内置存储设备的数据在读取路径中生成哈希）、`读取和执行路径`（所有读取和执行路径上的可执行代码受 SSV 与代码签名保护）、`映像执行路径`（x86_64 Mach 对象在映像执行路径中被识别）。**没有一次指攻击路径。**同样，`步骤` **16 次 / 11 页**读原句全是**受控流程的步骤**——启动链"每个步骤包含的组件都经 Apple 加密签名"、恶意软件检测"相继启动一系列步骤"、设备证明"设备会执行以下步骤"、"设置助理中移除特定的步骤"、活体检测"完成所需步骤"、密钥生成"创建数据的先期步骤"——**没有一次是攻击方的步骤**。T1 与 T2 在这一点上一致：中文层同样没有"攻击路径"这个成文对象。

**中文词按子串计数**，故 `攻击` / `攻击者` / `攻击面`、`利用` / `漏洞利用` / `利用链`、`输入` / `输入输出` 存在包含关系，**附表 C 各行不可相加**，也不可据此构造集合。

# 17 验证与断言

以下 15 条断言按第 18 章脚本可独立复算。

**验证一（P 的规模）**：全树 **9,136 段 / 257 页 / 472 名 / 7,434 含 Impact**；P = **890 段 / 160 页 / 112 名**，890 / 7,434 = **12.0%**、890 / 9,136 = **9.7%**。**Description 侧 attacker = 0 段**，且 P 内 **890 / 890 段有 Description**——三个数必须同时成立，若有人报告 Desc 侧非 0，即为把两栏拼成了一个字段。

**验证二（起点分区闭合）**：段级 **230 + 129 + 87 + 194 + 64 + 186 = 890**，Q7 未归类 **0**；附表 A 纵向 43 行合计 **890**，与本分区用同一判序（Q1→Q6）。**两表唯一的差额是 Q6**：段级 Q6 **186** = 附表 A 句级 Q6 **174** + 使役句式单列的 **S3 12**（那 12 段的 Impact 也含 `an attacker`，段级判定落入 Q6）；Q1–Q5 五类段级与句级**逐类相等**。**换判序得 194 / 194 / 175 / 170 / 114 / 43，同样 890**——两组的差是判序，不是文本。

**验证三（三种起点表互不混用）**：左侧限定 **5 种 / 890**（612 + 194 + 64 + 12 + 8 = 890）；句级起点身份 **43 种 / 890**；起点状语 **38 种 / 878**（(空) **432**）。**878 = 890 − 12**，差额恰为使役句式的 12 句。**若某脚本报 890 个状语窗口，即为把 `allow an attacker` 的 12 句也算进去了。**

**验证四（句式三分闭合）**：**798 + 80 + 12 + 0 = 890**；情态配对 **878 = may 868 + can 5 + could 4 + might 1**；交叉表（第 6 章）行合计 **612 + 194 + 64 + 12 + 8 = 890**、列合计 **868 + 4 + 5 + 1 + 12 = 890**，**行列两次求和相等**。`will` / `would` 在列合计里为 0，不得写入。

**验证五（动作层单调性）**：尾巴窗口 **878** ≥ 首词窗口 **878**（同一集合的两种投影）；尾巴 distinct **106** ≥ 首词 distinct **44**；`cause` 首词 **392** ≥ 其宾语 distinct **13**。**三段链条 44 ≤ 106 ≤ 878 ≤ 890** 必须成立；若 106 < 44 即为拆句规则未生效（把多句并成一句）。

**验证六（使役句式的唯一性）**：`allow* an attacker to` **12 段**，逐条列出后全部形如 `… a maliciously crafted archive may allow an attacker to write arbitrary files`；`attacker to` 之后窗口 distinct = **1**；块名分布 `Compression` 6 + `file_cmds` 6 = **12**；页 `121234/121238/121246/121247/121249/121250` + `149034/149035/149038/149041/149042/149043` = 12 页 12 段（**一页一段**）。

**验证七（通道词汇的两栏反差）**：`by + 动名词` Impact **2** ∶ Desc **378**；`through` **16** ∶ **247**；`over the` **0** ∶ **10**；`after` **22** ∶ **127**；`via` **23** ∶ **0**；`without` **178** ∶ **0**。`via` 的 23 段宾语 **5 种**（9 + 8 + 3 + 2 + 1 = **23**）。**`via` 与 `without` 是 Desc 侧 0 的两个通道/前置词，`by+ing` 是 Impact 侧 0 反被 Desc 大量使用的词——四个方向不可混引。**

**验证八（顺序词零断言）**：`then` / `first` / `chain` / `foothold` / `initial access` / `once` / `escape` / `pivot` / `reach` / `vector` / `entry point` / `attack surface` / `trigger` / `steps` / `sequence` / `prerequisite` / `threat actor` / `CVSS` / `severity` **Impact 与 Description 双侧 0**；`path` Impact **9** 但 **∩P = 0**；`reachable` Impact **0** / Desc **5**，5 段全为同一句 AirDrop。**零段不等于概念不存在**，只等于 T2 不用该词。

**验证九（后果句与路径句的零交必须用成员求交）**：`lead to` Impact **1,738**、∩P = **0**；`result in` **163**、∩P = **0**；`lead to` ∩ 输入并集 = **1,675**（即 lead-to 句几乎全在输入并集内）。**三个数一起才构成断言**：后果句属于"输入 → 后果"那条线，不属于"攻击者 → 动作"这条线。**不可由 |P| + |lead to| 推任何并集。**

**验证十（否定式前置条件的闭合）**：`without` **178 段 / 97 页 / 37 名**，宾语 **18 种**、纵向 **178**（47 + 42 + 16 + 9 + 9 + 7 + 7 + 7 + 5 + 5 + 5 + 4 + 4 + 3 + 3 + 3 + 1 + 1 = 178）；六类互斥切法 **102 + 55 + 7 + 7 + 4 + 3 = 178**（同意提示 / 认证凭据 / 配对 / 私钥 / 应用管理 / 界面指示器），其中私钥类单列以免与凭据类重复；P 内 `without` **20 段**，即 **158 = 178 − 20** 段的 `without` 主语不是攻击者。

**验证十一（在野分布的算术）**：在野标记 **103 段 / 74 页 / 12 名**、Desc **0**；P ∩ 在野 **25**，故 **103 − 25 = 78 段**在野标记不与 attacker 同段；**890 段拆出 920 句**，其中不含 attacker 的 **30 句** distinct **4 种**，全部是在野 / CVE 补记句——**30 ≠ 25**：一句可以拆成两句（`… exploited in an extremely sophisticated attack …` 之后另起 `CVE-… were also issued …`）。

**验证十二（25 段的起点全同类）**：16 + 5 + 4 = **25**，三者分别落 `with arbitrary kernel read and write` / `with memory write` / `with arbitrary read and write`，**全部属 Q3**；Q3 共 87 段 → **25 / 87 = 28.7%**；Q1、Q2、Q4、Q5、Q6 各 **0**。**若复算时看到某个 Q1 段带在野标记，即为拆句前用了字段级正则、把邻句的标记错绑到起点身份上。**

**验证十三（交集纪律与并集规模）**：|P| = **890**、|输入| = **3,245**、|P ∩ 输入| = **317**、|P ∪ 输入| = **3,818** = 890 + 3,245 − 317 ✓；|越级| = **602**、P ∩ 越级 = **154**、三者同交 = **92**；|bypass| = **291**（#110 的域 A 值 288 是同一指标的另一域）、P ∩ bypass = **61**；|ACE| = **218**、P ∩ ACE = **28**；|sandbox| = **418**、P ∩ sandbox = **1**；|memory corruption| = **218**、P ∩ 该集合 = **0**。

**验证十四（施动者替代词必须按"冠词 + 名词"口径断言）**：Impact 段内出现 / 段首起头（两口径都 ∩P = **0**）：`an app` **3,163 / 3,156**、`a malicious app`（含 `application` 写法）**337 / 336**、`a sandboxed app` **58 / 58**、`a process` **3 / 0**、`a shortcut` **75 / 72**、`a local user` **35 / 35**、`a remote user` **37 / 37**、`a person` **11 / 11**、`third party`（空格 + 连字符两种拼法）**6 / 6**、Description 侧另有 `third party` **199** 段。**对照组（宽词族与 P 大量共现，不可当施动者引用）**：`app|apps` **4,172 段 ∩P 118**、再加 `application(s)` 为 **4,298 / 144**、`user|users` **1,651 / 127**、`process|processes` **756 / 27**、`shortcut|shortcuts` **101 / 0**、`a user` **231 / 8**。**`a process` 的 3 段与 `process` 词族的 756 段是两个数**——后者绝大多数是 `Processing …` / `processing`（#112 的准入动词），没有施动者含义。**若某脚本报 `an app` ∩P > 0，即为把 `an application` 也算进来（缺后词边界时该数虚增到 3,174）。**

**验证十五（域分与 T1 层）**：域 A P = **885**（155 页 / 112 名）、域 B P = **5**（5 页 / 3 名：`Bluetooth` 1 / `CoreCrypto` 3 / `LoginWindow` 1），885 + 5 = **890** ✓；T1 **178 页可用 / 62 词**（附表 C 纵向 62 行），其中真零 **16 词**、非零 **46 词**、16 + 46 = 62 ✓；`攻击者` **60 / 29**、`触发` **5 / 4**、`路径` **4 / 3**、`步骤` **16 / 11**、`链条` `利用链` `漏洞利用` `网络位置` `近场` `可达` **各 0**。

# 18 复算步骤

1. 镜像目录 `_tmp/adv2/<id>.html`，257 份（排除 `122085`）。逐页先测 `<h3 class="gb-header` 是否存在；存在走域 A 刀、不存在走域 B 刀（粗体段起块）。块体终止锚必须是四个（`<h3 class="gb-header`、`<div id="disclaimer"`、`<footer`、文末），少一个会把页脚免责声明并入字段文本（#111 第 17 章、#112 第 17 章同一刀）。
2. 段取 `<p class="(?:gb-paragraph|paragraph)[^"]*"[^>]*>(.*?)</p>`，剥标签、解实体（含 `&rsquo;` 之类必须落到 `chr($1)`）、折空白，丢 `^Available for` 行；`Impact:` / `Description:` 多句以 `" || "` 连接，其余以 `" ~ "` 连接。**P 只从 Impact 构造。**
3. **必须拆句**：`split /(?<=[\.!?])\s+(?=["A-Z])/`。不拆句则 `attacker` 句与在野句被视为一句，第 14 章的 25 / 30 / 78 三个数与第 12 章的零交全部失真。
4. 词计数一律带词边界 `(?<![\w-])kw(?![\w-])`；复数写进交替（`paths?`、`chains?`）；词族写 `bypass(?:es|ed|ing)?`、`exploit(?:s|ed|ing|ation)?`，**不要写 `reach\w*`**（会漏 `reach` 本身以外的形，且 `reachable` 是另一词）；`privileg\w*` 是**有意**的词族写法，仅用于越级并集，不得用于起点分区。
5. 起点窗口**以情态动词为右界、上限 12 词**；状语窗口取 `attacker` 之后到情态之前；动作尾巴取情态（吞掉 `be able to`）之后到句末**不截断**。三者的合计分别是 890 / 878 / 878，**必须同时报出"12 句使役无尾巴"**，否则 878 与 890 的差无法解释。
6. **多词关键词不能用按词切窗口的老函数**：`_tmp/tlwin.pl` 的 `win_after` 逐 token 匹配，`privileged network position` 这类多词短语会静默返回 **0 个窗口**；本篇一律用"整段正则命中 + 对命中之后的文本切词"的写法。窗口**只在 `[.!?]` 处截断**，不要用情态动词或停用词表截断——那会恰好丢掉最短的那些句子。
7. 分区判定顺序固定 Q1→Q6 并输出"未归类 0"断言；换判序必须同时给出另一组闭合数（194 / 194 / 175 / 170 / 114 / 43），并说明近场（`proximity` / `radio range`）归 Q1 还是归物理类。
8. 零交断言（`lead to` ∩ P = 0、P ∩ `memory corruption` = 0、path ∩ P = 0）**必须以成员集合求交并同时报 |A|、|B|、|A ∩ B|**，不得由段数相减或比例推断。
9. 跨期对照先声明域：`bypass` 全树 **291** / 域 A **288**（#110）、输入并集全树 **3,245** / 域 A **3,210**（#112）、P 全树 **890** / 域 A **885** / 域 B **5**。
10. T1 侧：逐页取最后一个 `^版权和商标$` 之后至首个含 `Apple Footer` 行之前，排除 `sec2727afbbc`，得 **178 页可用**；中文词按**子串**计数，并在正文说明"攻击 / 攻击者 / 攻击面"有包含关系、**不可相加**；页面标题从 `_tmp/titles.txt` 的 `id|标题` 行取。
11. 所有含中文或含复杂转义的正则**写成 UTF-8 脚本文件**（`use utf8` + `binmode ":utf8"`），不要塞进命令行参数——Windows 代码页会静默破坏匹配。
12. 三张表由 `_tmp/tl113tab.pl` 直接生成（`tl113A.md` 43 行 / 890、`tl113B.md` 106 行 / 878、`tl113C.md` 62 行），脚本内含 `die` 断言：`$sa != 890` 或 `$tb != 878` 时不产出文件。

# 19 缺口清单

1. **T2 没有"路径"这个层级。** 本篇的 890 段是**代理集合**：它回答"哪些条目把攻击者写成了主语"，不回答"攻击者按什么顺序做"。**顺序词双侧全 0 是事实，不是本篇没找到。**任何把 Q1–Q6 当"攻击路径分类"引用的说法都超出证据。
2. **通道层不能建表，不要补。** `via` 23 段只有 5 种宾语，其中 2 种不是投递通道；`by + 动名词` 2 段。**"不构造通道并集"是本篇的决定。**需要通道叙述时只能引 #112 的输入并集与 T1 的"通道 / 局域网"页，并声明那是另一层。
3. **在野标记与起点不共现，读不出因果。** 25 / 25 落在 Q3 这一结果只描述措辞层；**不能读作"只有已具备能力的攻击者才被在野利用"**，因为 78 段在野标记根本不含 `attacker`（第 14 章）。**要判定真实利用条件需要厂商分析与代码，两者都不在本篇语料内。**
4. **按段计数、不按缺陷计数。** 同一句 `Unpacking … may allow an attacker to write arbitrary files` 在 `Compression` 下连续 6 页；`a local attacker` 类句子在 `122373`–`122375` 各 16 段。**换算缺陷数需要去重键（CVE + 块名），本期未建。**
5. **iOS / macOS 未分域。** P 头部块名里 `PackageKit`、`dyld`、`WindowServer`、`IOHIDFamily`、`LoginWindow`、`Dev Tools` 多为 Mac 条目。**引用"iOS 攻击者路径"时必须再切一次页面归属**（#111、#112 第 17/18 章同样登记过）。
6. **窗口上限造成的伪身份。** 附表 A 里 `an attacker with physical access to an unlocked device paired with a`（5 句）与 `when your headphones are seeking a connection request to one of your`（1 句）都是 **12 词截断**的产物，不是两种独立身份。**distinct = 43 必须与"其中 2 条为截断伪形"同时给出。**
7. **`user` 词族的施动者判定不可机械做**。1,651 段里 `a remote user`（37）、`a local user`（35）、`a non-privileged user`（9）、`standard user's credentials` 与 `user data`（受事）混在同一词形下，**本篇放弃 `user` 分区，只报词族总数**。这是可登记的口径缺口，不改结论。
8. **`a local attacker` 与 `a local user` 未合并**。64 段与 35 段是两组不同段（交集 0），语义上却都表达"本机已有一次立足"。**是否合并取决于威胁模型定义，本篇不代作决定**；合并会使 Q5 从 64 变 99，务必声明。
9. **静态站、开发者文档与 CVE 详情层未纳入本篇。** "路径"的权威定义在 CVSS 的 Attack Vector 字段与各厂商分析里，本期不引——**这也是第 10 章一片 0 的另一面：不是这些概念不存在，是 T2 不承担它们。**

# 20 常见误读纠正

**误读一："在野利用都发生在 Q3，所以只有已具备能力的攻击者会被在野利用。"（错）** 在野标记共 **103 段**，其中 **25 段**与 `attacker` 同段（全落 Q3），另外 **78 段**不含 `attacker`。**25 / 25 只描述"含攻击者的那部分"**，且 Q3 本身也只占 P 的 9.8%。零共现是写法，不是威胁分布。

**误读二："T2 有 35 种攻击后果，所以攻击面有 35 类。"** 35 是 **`lead to` 之后宾语**的 distinct（1,738 窗口），且 `lead to` 与 P **交集为 0**——它们是**后果表**，不是攻击面表。#112 的 8 类 / 3,245 段才是受事表。三张表（起点 43 / 动作 106 / 后果 35）**彼此不可换算**。

**误读三："攻击者的动作有 106 种。"** 106 是**尾巴写法**的 distinct：`cause unexpected app termination`（49）与 `cause an unexpected app termination`（38）是同一件事的两种冠词写法；`cause a denial-of-service`（110）与 `cause a denial of service`（12）同理。**按首词归并后只剩 44 个动词，再按语义归并（崩溃 / 读取 / 绕过 / 提权）会更少。**

**误读四：把 `path` 9 段当"Apple 写了 9 条攻击路径"。** 9 段全是文件系统路径（`an arbitrary path on the file system`、`A path handling issue`、`an overly large path value`），**与 P 交集 0**。同理 `reachable` 5 段是断言语义。**词存在 ≠ 概念存在**——这与 #112 的"29 段 `interface` 多为 `network interface`"是同一条方法论。

**误读五："`via` 有 23 段，加上 `using` 21 段、`through` 16 段，通道词汇共 60 段。"** **错，且错两次**：其一，这三组词**在同一段可共现**，必须成员求并才知道规模，本篇未把三者合并、也不建议合并；其二，`through` 的 16 段与 `over the` 的 0 段说明**这些词在 Impact 里主要不是通道用法**（`through their ip address` 是手段宾语）。**通道并集不在本篇构造。**

**误读六：把 890 段与 3,245 段相加得"4,135 段涉及攻击者与输入"。** 交集 **317**，正确并集 **3,818**。更要紧的是：**P ∩ `memory corruption` = 0、`lead to` ∩ P = 0**——"攻击者进来"与"造成内存破坏"在两处分开写，**任何"攻击者 → 内存破坏 → 提权"的单句因果在措辞层没有证据**（第 12 章；#111 第 7 章与 #112 误读八从另外两侧得出同一结论）。

**误读七："Description 侧没有 attacker，说明 Apple 隐瞒修复信息。"** Description 侧 **0 段 attacker**、同时 **890 / 890 段有 Description**——两栏是**分工**（一栏写影响、一栏写改动），不是缺一栏。通道词在 Desc 侧大量出现（`by + 动名词` 378、`through` 247）恰好说明**修复说明是有细节的，只是不写施动者**。

**误读八："T1 有 60 次'攻击者'，所以中文手册写了攻击者模型。"** 60 次 / 29 页，最集中在《基于 Intel 的 Mac 中的固件密码保护》（5）与《启动磁盘安全性策略控制》（5）——**是物理与启动链语境下的假设对手**；同层 `触发` **5**、`路径` **4**、`链条` / `利用链` / `网络位置` **0**。**手册层有"攻击者"这个名词，没有"路径"这个结构**（第 16 章）。

# 21 结论

**（1）"路径"在公开文本里只以三段骨架存在。** 起点身份 **43 种写法 / 6 类闭合分区 / 未归类 0** → 情态 **4 个词（`may` 868 / 878 = 98.9%）** → 动作 **44 个首词 / 106 条尾巴**。**中间层的两个选题词——"通道"与"顺序"——在措辞层没有词表**：`via` 23 段 5 种、`by + 动名词` 2 段、`over the` 0，`then` / `first` / `chain` / `foothold` / `initial access` / `steps` / `sequence` / `prerequisite` 双侧全 0。

**（2）字段单边性是本篇最干净的一条证据。** `attacker` **Impact 890 ∶ Description 0**、在野标记 **103 ∶ 0**、`without` **178 ∶ 0**、`via` **23 ∶ 0**；反向是 `by + 动名词` **2 ∶ 378**、`through` **16 ∶ 247**、`over the` **0 ∶ 10**。**"谁"与"经由什么"分属两栏，两栏不交接，因此公开文本无法被读成一条完整的攻击叙事。**

**（3）唯一成规模的前置条件表达是否定式的。** `without` **178 段 / 18 种宾语**（六类互斥：同意提示 102 + 认证凭据 55 + 配对 7 + 私钥 7 + 应用管理 4 + 界面指示器 3 = 178）。**T2 不说需要什么，只说不需要什么就能做成**——这是"触发前提"这个选题在措辞层的真实形状。

**（4）链的证据只存在于名词层，且与在野披露同处。** Q3 的 **87 段**用身份定语（`that has already achieved kernel code execution` 等）承认了前一步；**在野标记与 P 的 25 段交集 100% 落在 Q3**（Q3 的 28.7%），其余五类各 0。**"Apple 在措辞层承认利用链"这句话，能且只能说这 25 段。**

**（5）三个选题层次里，"路径"是最弱的一个。** 输入 → **3,245 段 / 8 类闭合**（#112）；越级 → **602 段**（#111）；绕过 → **291 段**（#110）；路径 → **890 段 / 43 种起点 / 106 条尾巴，但通道与顺序两层为词汇级空白**。**"攻击者触发或到达目标缺陷的路径"这个短语，在公开文本里可清点的只有"谁能做什么"这半句。**

---

# 附表 A 起点身份写法全表（43 行）

窗口 = **含 `attacker` 的那一句**从起头到**第一个情态动词之前**，按空白切词、上限 12 词，小写化后去重。"起点判定"列把第 4 章同一判序（Q1→Q6）作用在**该句所属的完整 Impact 段文本**上；使役句式（`allow … an attacker to`）单列 **S3**。纵向合计 **890**，与段级 P 相等。

**两处截断伪形必须连同本表一起引用**：`an attacker with physical access to an unlocked device paired with a`（5 句）与 `when your headphones are seeking a connection request to one of your`（1 句）都是 12 词上限截出来的，不是两种独立身份；后者就是域 B 页 `120907 / Bluetooth` 的唯一一段。**段级 Q6 186 = 句级 Q6 174 + S3 12**，其余五类段级与句级逐类相等（附表 D 第 2 行）。

| 起点身份写法（小写化、截到情态动词之前） | 句数 | 起点判定 | 累计 |
|---|---:|---|---:|
| a remote attacker | 194 | Q4 | 194 |
| an attacker | 174 | Q6 | 368 |
| an attacker on the local network | 103 | Q1 | 471 |
| an attacker in a privileged network position | 80 | Q1 | 551 |
| a local attacker | 64 | Q5 | 615 |
| an attacker with physical access | 45 | Q2 | 660 |
| an attacker with physical access to a locked device | 44 | Q2 | 704 |
| an attacker in physical proximity | 22 | Q1 | 726 |
| an attacker with arbitrary kernel read and write capability | 16 | Q3 | 742 |
| an attacker with physical access to a device | 14 | Q2 | 756 |
| an attacker in radio range | 9 | Q1 | 765 |
| an attacker with arbitrary read and write capability | 9 | Q3 | 774 |
| an attacker with root privileges | 9 | Q3 | 783 |
| a malicious attacker with arbitrary read and write capability | 8 | Q3 | 791 |
| an attacker in wireless proximity | 8 | Q1 | 799 |
| an attacker with a compromised intermediate certificate authority | 8 | Q3 | 807 |
| an attacker that has already achieved kernel code execution | 7 | Q3 | 814 |
| an attacker with a paired bluetooth device | 6 | Q3 | 820 |
| extracting a maliciously crafted archive | 6 | S3 | 826 |
| unpacking a maliciously crafted archive | 6 | S3 | 832 |
| an attacker on the network | 5 | Q1 | 837 |
| an attacker with memory write capability | 5 | Q3 | 842 |
| an attacker with physical access to an ios device | 5 | Q2 | 847 |
| an attacker with physical access to an unlocked device | 5 | Q2 | 852 |
| an attacker with physical access to an unlocked device paired with a | 5 | Q2 | 857 |
| an attacker with access to calendar data | 4 | Q3 | 861 |
| an attacker with user privileges | 4 | Q3 | 865 |
| an attacker in a privileged position | 3 | Q1 | 868 |
| an attacker with control of a network directory server | 3 | Q3 | 871 |
| an attacker with control of a network file server | 3 | Q3 | 874 |
| an attacker with physical access to a locked apple watch | 2 | Q2 | 876 |
| an attacker with physical access to a macos device with sidecar enabled | 2 | Q2 | 878 |
| an attacker with physical access to a trust-paired device | 2 | Q2 | 880 |
| an attacker with a usb-c connection to an unlocked device | 1 | Q3 | 881 |
| an attacker with knowledge of a standard user's credentials | 1 | Q3 | 882 |
| an attacker with physical access to a mac | 1 | Q2 | 883 |
| an attacker with physical access to an ios device with stolen device | 1 | Q2 | 884 |
| an attacker with physical access to an ipados device | 1 | Q2 | 885 |
| an attacker with physical access to iphone | 1 | Q2 | 886 |
| an attacker with physical access to the accessory | 1 | Q2 | 887 |
| an attacker within bluetooth range | 1 | Q3 | 888 |
| in certain configurations, an attacker with host-limited sudo access | 1 | Q3 | 889 |
| when your headphones are seeking a connection request to one of your | 1 | Q3 | 890 |

# 附表 B 动作尾巴全表（106 行）

尾巴 = 情态动词（吞掉 `be able to`）之后到**句末、不截断**，因此尾介词短语（`without authentication`、`from the fast user switching screen`）留在串内。纵向合计 **878 = 890 − 12**：缺的 12 句是附表 B 收不到的使役句式（第 8 章），**不是数据丢失**。"首词"列即第 7 章的 44 个动作动词。

**按首词归并前不要把 106 当"动作种类"**：`cause a denial-of-service`（110）与 `cause a denial of service`（12）、`cause unexpected app termination`（49）与 `cause an unexpected app termination`（38）是同一件事的不同写法。

| 动作尾巴（情态与 be able to 之后、至句末） | 句数 | 首词 | 累计 |
|---|---:|---|---:|
| cause a denial-of-service | 110 | cause | 110 |
| cause unexpected app termination | 49 | cause | 159 |
| cause unexpected system termination | 45 | cause | 204 |
| view sensitive user information | 44 | view | 248 |
| cause unexpected system termination or corrupt kernel memory | 40 | cause | 288 |
| cause an unexpected app termination | 38 | cause | 326 |
| corrupt process memory | 27 | corrupt | 353 |
| cause unexpected application termination or heap corruption | 26 | cause | 379 |
| bypass kernel memory protections | 23 | bypass | 402 |
| intercept network traffic | 23 | intercept | 425 |
| bypass pointer authentication | 17 | bypass | 442 |
| use siri to access sensitive user data | 17 | use | 459 |
| cause unexpected system shutdown | 16 | cause | 475 |
| cause unexpected app termination or arbitrary code execution | 14 | cause | 489 |
| leak sensitive user information | 13 | leak | 502 |
| cause a denial of service | 12 | cause | 514 |
| access contacts from the lock screen | 11 | access | 525 |
| elevate their privileges | 11 | elevate | 536 |
| execute arbitrary code | 11 | execute | 547 |
| cause unexpected system termination or read kernel memory | 10 | cause | 557 |
| access user data | 9 | access | 566 |
| delete protected system files | 9 | delete | 575 |
| gain access to protected parts of the file system | 9 | gain | 584 |
| modify the state of the keychain | 9 | modify | 593 |
| view restricted content from the lock screen | 9 | view | 602 |
| bypass network filters | 8 | bypass | 610 |
| cause an out-of-bounds read in kernel memory | 8 | cause | 618 |
| cause an unexpected app termination or arbitrary code execution | 8 | cause | 626 |
| create a read-only memory mapping that can be written to | 8 | create | 634 |
| gain access to user's keychain items | 8 | gain | 642 |
| issue certificates with arbitrary extended key usages | 8 | issue | 650 |
| leak memory | 8 | leak | 658 |
| spoof network packets | 8 | spoof | 666 |
| bypass authentication policy | 7 | bypass | 673 |
| cause a persistent denial-of-service | 7 | cause | 680 |
| decrypt legacy rsa pkcs#1 v1.5 ciphertexts without having the private key | 7 | decrypt | 687 |
| perform denial-of-service attack using crafted wi-fi packets | 7 | perform | 694 |
| spoof their facetime caller id | 7 | spoof | 701 |
| view leaked dns queries with private relay turned on | 7 | view | 708 |
| track a user's activity | 6 | track | 714 |
| track users through their ip address | 6 | track | 720 |
| turn on sharing of an icloud folder without authentication | 6 | turn | 726 |
| authenticate to screen sharing without valid credentials | 5 | authenticate | 731 |
| determine kernel memory layout | 5 | determine | 736 |
| misuse a trust relationship to download malicious content | 5 | misuse | 741 |
| perform denial-of-service attack using crafted bluetooth packets | 5 | perform | 746 |
| view sensitive user information in system logging | 5 | view | 751 |
| also read reminders | 4 | also | 755 |
| alter network traffic | 4 | alter | 759 |
| elevate privileges | 4 | elevate | 763 |
| leak mail account credentials | 4 | leak | 767 |
| modify network traffic | 4 | modify | 771 |
| overwrite restricted files | 4 | overwrite | 775 |
| read kernel memory | 4 | read | 779 |
| share items from the lock screen | 4 | share | 783 |
| access notes from the lock screen | 3 | access | 786 |
| access sensitive user data during iphone mirroring | 3 | access | 789 |
| cause arbitrary code execution | 3 | cause | 792 |
| cause unexpected app termination or heap corruption | 3 | cause | 795 |
| cause unexpected system termination or arbitrary code execution in dcp firmware | 3 | cause | 798 |
| control nearby devices via accessibility features | 3 | control | 801 |
| execute arbitrary code with root privileges | 3 | execute | 804 |
| force a device to disconnect from a secure network | 3 | force | 807 |
| impersonate other users | 3 | impersonate | 810 |
| input game controller events to apps running on a locked device | 3 | input | 813 |
| perform a denial-of-service | 3 | perform | 816 |
| read contact numbers from the lock screen | 3 | read | 819 |
| read information belonging to another user | 3 | read | 822 |
| read sensitive information | 3 | read | 825 |
| view deleted notes | 3 | view | 828 |
| view notification content from the lock screen | 3 | view | 831 |
| bypass ipsec authentication and intercept network traffic | 2 | bypass | 833 |
| bypass the lock screen | 2 | bypass | 835 |
| determine the legacy vnc password configured for screen sharing | 2 | determine | 837 |
| disable stolen device protection | 2 | disable | 839 |
| discover a user&rsquo;s deleted notes | 2 | discover | 841 |
| gain access to keychain items | 2 | gain | 843 |
| inject keystrokes by spoofing a keyboard | 2 | inject | 845 |
| override managed wi-fi profiles | 2 | override | 847 |
| read and write arbitrary files | 2 | read | 849 |
| use siri to enable auto-answer calls | 2 | use | 851 |
| view an image in the most recently viewed locked note | 2 | view | 853 |
| view notification contents from the lock screen | 2 | view | 855 |
| access a deleted call recording | 1 | access | 856 |
| access biometrics-gated protected apps with the passcode | 1 | access | 857 |
| access contact photos from the lock screen | 1 | access | 858 |
| access photos while the app is locked | 1 | access | 859 |
| break out of web content sandbox | 1 | break | 860 |
| bypass digital id authentication when iphone mirroring is active | 1 | bypass | 861 |
| bypass sender policy checks and deliver malicious content via email | 1 | bypass | 862 |
| determine the apple id of the owner of the computer | 1 | determine | 863 |
| extract its bluetooth pairing key and monitor bluetooth traffic | 1 | extract | 864 |
| input keyboard events to apps running on a locked device | 1 | input | 865 |
| listen through the microphone of a device which is not yet paired and actively seeking pair requests | 1 | listen | 866 |
| programmatically access photos | 1 | programmatically | 867 |
| see recent photos without authentication in assistive access | 1 | see | 868 |
| silently persist an apple account on an erased device | 1 | silently | 869 |
| spoof the intended source device and gain access to your headphones | 1 | spoof | 870 |
| take and view screenshots of sensitive data from the iphone during iphone mirroring with mac | 1 | take | 871 |
| unlock another standard user's locked screen on the same mac | 1 | unlock | 872 |
| use visual intelligence to access sensitive user data during iphone mirroring | 1 | use | 873 |
| view live voicemail | 1 | view | 874 |
| view protected content from the login window | 1 | view | 875 |
| view the previous logged in user&rsquo;s desktop from the fast user switching screen | 1 | view | 876 |
| view user contacts | 1 | view | 877 |
| view wi-fi passwords without authentication | 1 | view | 878 |

# 附表 C 手册层逐词普查全表（62 行）

《Apple 平台安全保护》zh-CN 转储 179 份，逐页取最后一个 `^版权和商标$` 之后、首个含 `Apple Footer` 行之前，排除 `sec2727afbbc`，得 **178 页可用正文域**。中文词按**子串**计数；"次数"是全 178 页出现次数，"页数"是命中页数。

**中文列不可相加**：`攻击` 162 已包含 `攻击者` 60 与 `攻击面` 12，`利用` 43 已包含 `漏洞利用`（0）之外的各种复合形。本表 62 行里**真零 16 行、非零 46 行**，16 + 46 = 62（附表 D 第 15 行）。零行只表示**措辞层未写**，不表示功能上不可达（第 20 章误读四）。

| 词 | 次数 | 页数 | 最集中的两页（次数） |
|---|---:|---:|---|
| 密码 | 622 | 100 | 密码监视（53）；密码安全建议（41） |
| 认证 | 372 | 92 | 安全隔区（29）；单点登录安全性（25） |
| 执行 | 246 | 79 | 搭载 Apple 芯片的 Mac 上的 Rosetta 2（17）；Apple 钱包中的证件（14） |
| 解锁 | 202 | 47 | 自动解锁 Apple 设备（68）；钥匙串数据保护（13） |
| 授权 | 172 | 63 | NFC 与 SE 平台安全性（12）；通过 Apple Pay 使用付款卡支付（11） |
| 内核 | 166 | 41 | 在 macOS 中安全扩展内核（21）；操作系统完整性（18） |
| 攻击 | 162 | 50 | 操作系统完整性（13）；密码（12） |
| 配对 | 161 | 35 | watchOS 系统安全性（24）；配备触控 ID 的妙控键盘（13） |
| 触控 | 147 | 29 | 配备触控 ID 的妙控键盘（36）；视控 ID、面容 ID 和触控 ID 的用途（22） |
| 面容 | 129 | 27 | 视控 ID、面容 ID 和触控 ID 的用途（25）；脸部匹配安全性（18） |
| 锁定 | 125 | 40 | 安全隔区（17）；自动解锁 Apple 设备（9） |
| 恶意 | 104 | 28 | 在 macOS 中防范恶意软件（37）；关于 App Store 安全性（11） |
| 局域网 | 104 | 34 | 接入无线网络时的隐私功能（16）；Apple 设备的无线局域网隐私（15） |
| 备份 | 89 | 22 | iCloud 云备份安全性（35）；用于数据保护的密钥包（20） |
| 本地 | 64 | 29 | HomeKit 摄像头安全性（7）；iPadOS 中的“共享 iPad”安全性（7） |
| 无需 | 64 | 48 | 自动解锁 Apple 设备（3）；术语表（3） |
| 蓝牙 | 63 | 28 | 蓝牙安全性（19）；watchOS 系统安全性（6） |
| 远程 | 61 | 35 | 受管理的丢失模式和远程擦除（10）；自动解锁 Apple 设备（6） |
| 攻击者 | 60 | 29 | 基于 Intel 的 Mac 中的固件密码保护（5）；搭载 Apple 芯片的 Mac 的启动磁盘安全性策略控制（5） |
| 安全启动 | 60 | 22 | 基于 Intel 的 Mac 的启动过程（12）；搭载 Apple T2 安全芯片的 Mac 上的启动安全性实用工具（10） |
| 条件 | 45 | 25 | 搭载 Apple 芯片的 Mac 的 LocalPolicy 文件内容（6）；将信用卡或借记卡添加到 Apple Pay（6） |
| 利用 | 43 | 29 | 操作系统完整性（5）；Apple 操作系统中的量子安全加密技术（4） |
| 物理 | 31 | 20 | 操作系统完整性（5）；术语表（3） |
| 通道 | 30 | 14 | 车钥匙交易（6）；配备触控 ID 的妙控键盘（5） |
| 沙盒 | 27 | 11 | macOS 中的 Option ROM 安全性（6）；iOS、iPadOS 和 visionOS 中的运行时进程安全性（4） |
| 同意 | 26 | 19 | 在 macOS 中安全扩展内核（3）；App 访问已存密码的权限（2） |
| 交互 | 19 | 16 | 密码监视（3）；Apple 钱包中证件的安全性（2） |
| 步骤 | 16 | 11 | iPad 和 iPhone 设备启动过程（3）；在 macOS 中防范恶意软件（2） |
| 快捷指令 | 15 | 3 | “快捷指令” App 中的安全功能（13）；服务安全性概览（1） |
| 恢复模式 | 14 | 9 | 术语表（3）；基于 Intel 的 Mac 的启动过程（2） |
| 终止 | 13 | 7 | iOS 中的车钥匙安全性（4）；停用 Apple Pay 付款卡（3） |
| 攻击面 | 12 | 9 | 操作系统完整性（3）；基于 Intel 的 Mac 的 recoveryOS 和诊断环境（2） |
| 前提 | 9 | 9 | 管理式 Apple 账户安全性（1）；用于数据保护的密钥包（1） |
| 崩溃 | 6 | 4 | Apple 操作系统中的后台安全改进（3）；操作系统完整性（1） |
| 触发 | 5 | 4 | HomeKit 通信安全性（2）；macOS 中的门禁和运行时保护（1） |
| 向量 | 5 | 5 | 生物识别安全性（1）；“备忘录” App 中的安全功能（1） |
| 隔空播放 | 5 | 3 | HomeKit 通信安全性（2）；为 Apple 设备和服务验证配件（2） |
| 路径 | 4 | 3 | 签名系统宗卷安全性（2）；安全隔区（1） |
| 前置 | 4 | 2 | iOS 和 iPadOS 中的 ReplayKit 安全性（2）；iOS 和 iPadOS 中的 ARKit 安全性（2） |
| 伪造 | 4 | 4 | 蓝牙安全性（1）；Apple 钱包中证件的安全性（1） |
| 入口 | 2 | 2 | iCloud 专用代理安全性（1）；Apple Pay 如何保护用户的购买（1） |
| 中间人 | 2 | 1 | 蓝牙安全性（2） |
| 顺序 | 1 | 1 | 基于 Intel 的 Mac 的 recoveryOS 和诊断环境（1） |
| 精心 | 1 | 1 | 视控 ID、面容 ID、触控 ID 和密码（1） |
| 拒绝服务 | 1 | 1 | macOS 中的防火墙安全性（1） |
| 旁白 | 1 | 1 | 生物识别安全性（1） |
| 链条 | 0 | 0 | — |
| 利用链 | 0 | 0 | — |
| 漏洞利用 | 0 | 0 | — |
| 近场 | 0 | 0 | — |
| 暴露面 | 0 | 0 | — |
| 可达 | 0 | 0 | — |
| 越狱 | 0 | 0 | — |
| 特制 | 0 | 0 | — |
| 任意代码 | 0 | 0 | — |
| 权限提升 | 0 | 0 | — |
| 提权 | 0 | 0 | — |
| 网络位置 | 0 | 0 | — |
| 无障碍 | 0 | 0 | — |
| VoiceOver | 0 | 0 | — |
| 通用密码 | 0 | 0 | — |
| 触控ID | 0 | 0 | — |

# 附表 D 复算验收断言集（15 行）

以下 15 行与第 17 章验证一至验证十五一一对应，但按"不通过时先查什么"重排，供复算者定位。第 2、9、11、14 四项是本篇写作过程中真实翻过车的地方。

| # | 断言 | 本期实测 | 不通过时先查什么 |
|---|---|---|---|
| 1 | P = 890 段 / 160 页 / 112 名，占含 Impact **12.0%**、占全树 **9.7%**；Description 侧 attacker = **0**，且 P 内 890 / 890 有 Description | 890 / 7,434 / 9,136 | 是否把 Impact 与 Description 拼成单字段后再匹配 attacker |
| 2 | 起点分区 **230 + 129 + 87 + 194 + 64 + 186 = 890**、未归类 **0**；附表 A 纵向 43 行合计 890 | 成立 | 判序是否仍是 Q1→Q6；段级 Q6 是否等于句级 174 **+ S3 12**（少 12 即为把使役句漏判） |
| 3 | 三种起点表分母不混用：左侧限定 5 种 / **890**、句级身份 43 种 / **890**、起点状语 38 种 / **878** | 878 = 890 − 12 | 状语窗口是否误收了 `allow an attacker to` 的 12 句 |
| 4 | 句式三分 **798 + 80 + 12 + 0 = 890**；情态 **may 868 + can 5 + could 4 + might 1 = 878**；交叉表行列两次求和都 890 | 成立 | 使役句式是否被同时计进 S1 / S2（重复计数）；`will` / `would` 是否被写成非 0 |
| 5 | 尾巴窗口 **878** ≥ 首词窗口 **878**（同一集合两种投影）；distinct **106 ≥ 44**；`cause` 首词 **392** | 附表 B 106 行合计 878 | 拆句正则是否为 `(?<=[.!?])\s+(?=[A-Z])`；尾巴是否被截到 N 词 |
| 6 | 使役句式 **12 段**：块名 `Compression` 6 + `file_cmds` 6、12 页各 1 段、`attacker to` 之后窗口 distinct **1** | 成立 | 是否用裸 `allow` 匹配（会命中 `allows an app to`，数虚增） |
| 7 | 通道词两栏反差：`via` **23 ∶ 0**、`without` **178 ∶ 0**、`using` **21 ∶ 10**、`by + 动名词` **2 ∶ 378**、`through` **16 ∶ 247**、`over the` **0 ∶ 10** | 成立 | Impact 与 Description 是否分栏统计（合并后 `by + ing` 会被读成约 380） |
| 8 | 顺序词 Impact 与 Description **双侧 0**：`then` / `first` / `chain` / `foothold` / `initial access` / `once` / `escape` / `pivot` / `reach` / `vector` / `entry point` / `attack surface` / `trigger` / `steps` / `sequence` / `prerequisite` / `threat actor` / `CVSS` / `severity` | 全 0 | 是否漏了词边界（`reach` 不加边界会命中 `reachable`，`chain` 会误配它形） |
| 9 | 零交必须由成员求交：`lead to` **1,738** ∩P **0**、`result in` **163** ∩P **0**、`path` **9** ∩P **0**、`memory corruption` **218** ∩P **0** | 四个 0 成立 | 是否由两个段数相减或比例推断交集；P 集合是否用了字段级正则 |
| 10 | `without` **178 段 / 18 种宾语 / 六类互斥 102 + 55 + 7 + 7 + 4 + 3 = 178**；P 内 **20**，故 **158** 段主语不是攻击者 | 成立 | 私钥类 7 段是否被并入认证类（并入则两类变 62 / 48，总数不变） |
| 11 | 在野标记 **103 段 / 74 页 / 12 名**、Desc 侧 **0**；P ∩ 在野 **25**；890 段拆 **920 句**，不含 attacker 的 **30 句** distinct **4** | 25 ≠ 30 | 是否在未拆句的字段级文本上匹配（那样 30 会被读成 25） |
| 12 | 在野 25 段的起点 **16 + 5 + 4 = 25** 全落 **Q3**，其余五类各 **0**；25 / 87 = **28.7%** | 成立 | 是否把邻句的在野标记绑到了起点身份（拆句前用字段级正则的典型后果） |
| 13 | 并集纪律：\|P\| 890 + \|输入\| 3,245 − \|P ∩ 输入\| 317 = \|P ∪ 输入\| **3,818**；\|越级\| 602 ∩P 154；\|bypass\| 291 ∩P 61；\|ACE\| 218 ∩P 28；\|sandbox\| 418 ∩P 1；三者同交 92 | 逐项成立 | 跨期对照是否声明了域（\|bypass\| 全树 291 / 域 A 288 是同一指标两域，#110 已对账） |
| 14 | 施动者两口径：所有"冠词 + 名词"式措辞 ∩P = **0**（`an app` 3,163、`a process` **3**、`a shortcut` **75**、`a person` 11、`third party` 6）；宽词族 ∩P 非 0（`app|apps` 4,172 / **118**、`user|users` 1,651 / **127**、`process|processes` 756 / **27**） | 成立 | `an app` 是否缺后词边界（误收 `an application` 会把段起头数从 3,156 虚增到 3,174） |
| 15 | 域分 **885 + 5 = 890**（域 B 名 `Bluetooth` 1 / `CoreCrypto` 3 / `LoginWindow` 1）；T1 **178 页 / 62 词**，真零 **16** + 非零 **46** = 62 | 成立 | 块刀是否有**四个**终止锚（少一个则页脚免责声明并入字段文本）；`sec2727afbbc` 是否排除 |

# 附表 E 错误写法与纠正对照（8 行）

| # | 错误写法 | 为什么错 | 正确口径 |
|---|---|---|---|
| 1 | "在野利用都发生在 Q3，所以只有已具备能力的攻击者才会被在野利用" | 在野标记共 103 段，其中 **78 段不含 `attacker`**；25 / 25 只描述"含攻击者的那部分"，而 Q3 自身也只占 P 的 9.8% | 引用在野分布必须同时给 **103 / 25 / 78** 三个数，并声明零共现是写法、不是威胁分布 |
| 2 | "T2 有 35 种攻击后果，所以攻击面有 35 类" | 35 是 `lead to` 之后宾语的 distinct（1,738 窗口），而 `lead to` ∩ P = **0** | 后果表 ≠ 攻击面表；受事表在 #112（8 类 / 3,245 段），三张表（起点 43 / 动作 106 / 后果 35）不可换算 |
| 3 | "攻击者的动作有 106 种" | 106 是**尾巴写法**数：同一动作因冠词与连字符差异有多种串 | 按首词归并 = **44 个动词**；再按语义（崩溃 / 读取 / 绕过 / 提权）归并会更少 |
| 4 | "`path` 有 9 段，说明 Apple 写了 9 条攻击路径" | 9 段全是文件系统语义（`an arbitrary path on the file system`、`path handling issue`、`an overly large path value`），**∩P = 0**；`reachable` 5 段是断言语义 | **词存在 ≠ 概念存在**；`path` 与 `reachable` 都不得用作路径证据 |
| 5 | "`via` 23 + `using` 21 + `through` 16 = 60 段通道词汇" | 三组词在同一段可共现，未做成员求并；且 `through` 的 16 段多为手段宾语 | **本篇不构造通道并集**；需要通道叙述时引 #112 并声明那是另一层 |
| 6 | "890 + 3,245 = 4,135 段涉及攻击者与外部输入" | 两集合成员交集 **317**，正确并集 **3,818** | 任何并集必须成员求并，并同时报 \|A\|、\|B\|、\|A ∩ B\| |
| 7 | "Description 侧一段 `attacker` 都没有，说明 Apple 隐瞒修复信息" | Desc 侧 0 段 attacker，但 P 内 **890 / 890 段都有 Description**，且通道词在 Desc 侧大量出现（`by + 动名词` 378、`through` 247） | 两栏是**分工**（一栏写影响、一栏写改动），不是缺一栏；单边性只说明措辞位置 |
| 8 | "T1 有 60 次'攻击者'，所以中文手册写了攻击者模型" | 60 次集中在固件密码保护与启动磁盘策略两页，是物理与启动链语境下的假设对手；同层 `触发` **5**、`路径` **4**（且 4 处分别是 DMA 数据路径、读取路径、读取和执行路径、映像执行路径）、`链条` / `利用链` / `网络位置` **0** | 手册层有"攻击者"这个**名词**，没有"路径"这个**结构**；中文计数还须声明子串包含关系、不可相加 |

---

**边界声明：** 本篇全部内容是对 Apple 公开安全响应顾问页英文镜像（257 份、9,136 段，两个版式域）与《Apple 平台安全保护》中文指南转储（178 页可用正文域）的词频、字段位置、句式模板、起点分区、动作清单与集合交并的**文本计量分析**，用于说明公开文本把攻击者写成什么样的主语、又把哪些环节留在词表之外。本篇不提供任何触发或到达目标缺陷的方法，不含绕过访问控制或权限检查的说明，不含利用代码，不含恶意输入或样本构造，不含针对他人设备的操作步骤与时序，也不给出任何具体缺陷的可复现路径；第 4 章的 Q1–Q6 是对**公开句子主语措辞**的分类，不是攻击路径分类，任何把它当威胁建模路径表的引用都超出证据。文中出现的组件名、页号与段落均为对公开文本的引用与计数，不构成对任何产品暴露面的判定；"零命中"只表示措辞层未写，不表示功能上不可达。所有百分比与段数只描述本篇镜像与切分口径下的样本，若上游文本更新，全部数值需按第 18 章复算。

Telegram: https://t.me/one00190
