@@TITLE@@iOS 组合多个漏洞或安全弱点实现更大影响
@@SUBTITLE@@选题把**组合（多个漏洞／弱点一起用）**与**更大影响（后果被放大）**捆在一起。逐词测量的结果是：这两个词在公开英文后果字段里都**没有对应的表达**。措辞层唯一能测的是本篇定义的 **M2 集**：同一段 Impact 文本里**同时命中两个以上后果强度类**（八类判据：`ACE` 任意代码执行、`BYP` 绕过、`DIS` 泄露、`DOS` 拒绝服务／终止、`FULL` 任意文件／任意操作／完全控制、`MEM` 内存破坏、`PRI` 提权、`SBX` 沙盒）＝ **185 段 / 82 页 / 35 名 / 23 条 distinct 段全文**，占含 Impact 的 7,434 段的 **2.49%**、占全树 9,136 段的 **2.02%**。而**"组合"这个词族本身为 0**：`in combination with`／`combined with`／`combining`／`combination 词族`／`together with`／`along with`／`as well as`／`in conjunction with`／`chain 词族`／`attack chain`／`exploit chain`／`sequence`／`series`／`several`／`two or more`／`both … and`／`in addition`／`further`／`moreover`／`furthermore`／`plus`／`not only`／`and then`／`subsequently`／`subsequent` **25 串 Impact 与三字段并集全零**（`then` 本身 Impact 也是 **0**，只在致谢侧留 3 段）；**"更大影响"侧同样为空**：`greater`／`more severe`／`worse`／`larger`／`bigger`／`severe`／`critical`／`impact`（词本身）／`amplif*`／`intensif*`／`widen*`／`increase*`／`expand*`／`complete`／`entire`／`total`／`complete control`／`full access`／`any action`／`compromise`／`breach`／`take control`／`vector`／`primitive`／`sandbox escape`／`jailbreak`**26 项 Impact 与三字段全零**（`system-wide` 另计：Impact **7** 段但 ∩M2 **0**；`persist*` 46 段、`potentially` 30 段亦非零，见第 10、15 章）。185 段的连接形态只有一套：**`or` 159 ∶ `and` 5 ∶ 无连接词 21**，且这 21 段仅由 **3 条 distinct 句**构成。**"多个问题"有句式但没有后果**：`Multiple issues in` **39 段 / 36 页 / 6 名 / 7 条 distinct 句**，其中 **31 段（79.5%）连一个后果类都不写**。编号侧同样不在后果字段：**2,108 个 distinct CVE 里 2,106 个只出现在致谢字段**，Impact 侧出现 ≥2 个编号的段**只有 5 段，而且是同一句话**。
@@INFO@@报告日期|2026 年 10 月 28 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 9,132 段含致谢**。组合后果集 M2 = Impact 同段命中 ≥2 个后果强度类 ＝ **185 段 / 82 页 / 35 名 / 23 条 distinct 段全文**。对照集：**WO** = #119 的无前提集（Impact 含 `without` 词界）**178 段 / 97 页 / 37 名**；**R** = #118 的远程触达集（Impact 侧 11 个通道词族）**843 段 / 178 页 / 72 名**；**E** = #114 的在野集（Impact 含 `exploit` 词族）**108 段 / 76 页 / 12 名**；**G／HI** = #116 与 #120 的动名词集 **2,179 段** 与交互触发集 **273 段 / 102 页 / 35 名**；**U** = Impact 含 `user`（词界）**1,627 段 / 175 页 / 200 名**。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或利用链构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**公开英文文本在同一段后果里并写几类后果、用什么连接词并写、"组合"与"放大"这两个语义在措辞层有没有词形承载、CVE 编号落在哪个字段**，属于措辞与文本计量分析；文中任何"组合"表述均指**同一句子内多个后果短语的共现**，不是对多个漏洞被一起利用的判断，也不构成任何利用链存在的结论
@@INFO@@分类|判据**电池 130 项 / 三字段全零 70 项 / 至少在一条字段出现 60 项 / Impact 侧非零 56 项**（附表 A，六组：组合连接词 31（Impact 非零 4、全零 25）、放大与比较词 35（9、26）、「多个问题」句式 12（6、5）、能力与前提叠加 25（18、7）、补充与后补修复 19（11、7）、后果强度八类 8（8、0））/ **23 条 distinct 组合句 × 后果类签名 × 连接词 × ∩WO ∩R ∩G ∩HI**（附表 B，24 行含合计，段合计 **185**）/ **组件名 × M2 矩阵 35 行**（附表 C，36 行含合计，段合计 **185**、这 35 名的 Impact 全集 **2,883**、总占比 **6.4%**）/ **CVE 编号普查 36 行**（附表 D，三段分箱各自闭合：段内 9,136、页内 257、单编号覆盖页 2,108）/ **中文层 91 词**（附表 E，七组：组合动词 12、链与路径 12、多个量词 15、并列连词 9、放大比较 15、弱点与防护 13、后果动词 15；**全零 20 词、非零 71 词**）/ **六把刀**：连接词刀（26 串全零）、放大刀（26 项全零）、共现刀（0＝3,652 ∶ 1＝3,597 ∶ 2＝167 ∶ 3＝18，合计 7,434）、配对刀（8 对非零，合计 221 ＝ 167＋18×3）、情态刀（`may` 177 ∶ 其他 8 ＝ 185）、编号刀（2,106／2,108 只在致谢）
@@INFO@@难度|进阶（第 3 章的**类命中分布**与**配对计数**是两个不同分母：167 段命中恰好 2 类、18 段命中 3 类，所以配对总数 221 ＝ 167×1＋18×3，把 221 当"组合条数"会高估 36；第 5 章的 `or` 判据是**词界**而不是子串，`oracle`、`coord` 之类不该计入，而 `, or` 在 Impact 侧为 **0**、在并集侧却有 **22**，说明这一串只存在于 Description／致谢；第 6 章必须同时报 `Multiple issues in` 的 **39 段**与"其中 31 段无任何后果类"，否则会读成"39 段都是组合后果"；第 12 章的 `potentially` **30 段 100% 落在 M2 内**是本篇唯一一条正向包含关系，但它是**缓和词而不是放大词**，与"更大影响"方向相反；第 14 章的**栏键**（页＋组件名＋致谢前 40 字）才是"一条修复条目"，按段数得到的 459 与按栏数得到的 459 相等属**已按成员验证的结果**，不是恒等式；第 18 章的弱点名词刀在合并 `memory`＋`corruption` 前会虚高到 **789 段**，合并后是 **184 段**，两数都真，差别全在重叠 628 段这一事实上）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

**选题原文是"组合多个漏洞或安全弱点实现更大的影响"。** 这句话在分析上被拆成三个互相独立的断言，本篇分别测量：

**断言一（组合）**：存在把多个漏洞或多个弱点**一起**使用的表述。**测法**：31 项组合连接词电池（附表 A 组 1）。**结果**：Impact 侧非零只有 4 项，其中 3 项是通用并列（` and ` 85、` or` 660、`分号` 28）、1 项是词义无关的 `link*`（15 段全是 `iCloud Link`／`visited a given link`）；**表达"组合"这一语义的 26 串全零**。

**断言二（多个弱点）**：被组合的对象是**多个**弱点。**测法**：12 项「多个问题」句式电池（组 3）＋ CVE 编号普查（附表 D）。**结果**：`Multiple issues in` 有句式（39 段），但它是一个**引用格式**（列出外部库名），并且 **31 段不写后果**；编号侧 **2,108 个 distinct CVE 中 2,106 个只在致谢字段**，Impact 侧 ≥2 编号的段 **5 段且同句**。

**断言三（更大影响）**：组合的后果被放大。**测法**：35 项放大与比较词电池（组 2）＋ 后果强度八类共现（组 6）。**结果**：放大词 **26 项三字段全零**；唯一可用的代理指标是**同段并写几类后果**，即 M2 ＝ **185 段 = 2.49%**，且它的谓语是**选择关系**（`or` 159／185 = 85.9%）而不是**叠加关系**。

**所以本篇的产出主要是一条否定结论**：在公开英文后果字段里，**"组合实现更大影响"没有词汇层承载**；能承载的只有一件事——**同一段文本把两类或三类后果写在同一个谓语下**，这是**措辞层的并写**，不是**利用层的组合**。第 17 章给对账，第 20 章给复算，第 22 章纠正七种会把 185 读成"组合利用条数"的误读。

# 1 操作性定义与分母

**1.1 语料与分母。** 与 #114–#120 同一镜像、同一切块口径（四锚点收块，避免页脚声明串入字段）。全树 **9,136 段 / 257 页 / 472 名**；含 Impact **7,434 段**；含致谢 **9,132 段**。域 A **9,045 段 / 246 页 / 470 名**，域 B **91 段 / 11 页 / 26 名**。

**1.2 后果强度八类（本篇的"影响"刻度）。** 全部只在 **Impact** 字段匹配，词界优先：

| 代号 | 判据 | 段 | 页 | 名 |
|---|---|---:|---:|---:|
| `ACE` | `arbitrary code execution` 或 `execute arbitrary code` | 336 | 120 | 43 |
| `BYP` | `bypass`（词界） | 291 | 114 | 56 |
| `DIS` | `disclos*`／`leak*`／`expos*` | 530 | 144 | 63 |
| `DOS` | `denial-of-service`／`unexpected system／app termination`／`terminat*` | 2,003 | 176 | 130 |
| `FULL` | `arbitrary file(s)`／`any action`／`full control` | 63 | 43 | 18 |
| `MEM` | `memory corruption`／`corrupt memory` | 220 | 85 | 17 |
| `PRI` | `elevat* privileges`／`privileges escalat*` | 124 | 62 | 30 |
| `SBX` | `sandbox`（词界） | 418 | 119 | 67 |

**1.3 M2 集。** 对**含 Impact 的 7,434 段**逐段统计命中的类数：**0 类 3,652 段、1 类 3,597 段、2 类 167 段、3 类 18 段**，合计 **7,434**（闭合，无第 4 类命中）。**M2 ＝ 命中 ≥2 类 ＝ 167＋18 ＝ 185 段**。

**1.4 三个必须分开的分母。** （a）**类命中分布**：0／1／2／3 的 3,652／3,597／167／18；（b）**配对计数**：**221**（一个 3 类段贡献 3 对）；（c）**组合签名计数**：**185**（一个 3 类段只算 1 个签名）。三者在第 3、4 章分别使用，混用是本选题最容易出的错。

# 2 结论速览（每条都可按第 20 章复算）

**一、"组合"没有词。** 附表 A 组 1 的 31 项里 **25 项三字段全零**；表达组合语义的 `combin*`（含 `in combination with`、`combined with`、`combining`）、`chain*`、`attack chain`、`exploit chain`、`together with`、`along with`、`as well as`、`in conjunction with`、`two or more`、`both … and`、`not only`、`and then`、`sequence`、`series`、`several`、`in addition`、`further`、`moreover`、`furthermore`、`plus`、`subsequent(ly)` **在 Impact、Description、致谢三条字段里一次都没有**。

**二、"更大影响"也没有词。** 组 2 的 35 项里 **26 项三字段全零**，包括 `greater`、`more severe`、`worse`、`larger`、`bigger`、`severe`、`critical`、`impact` 这个词本身、`amplif*`、`intensif*`、`widen*`、`increase*`、`expand*`、`complete control`、`full access`、`compromise`、`breach`、`take control`、`vector`、`primitive`、`sandbox escape`、`jailbreak`。

**三、后果被并写的只有 185 段（2.49%），且用 `or` 并写。** `or` **159** ∶ `and` **5** ∶ 无连接词 **21**（21 段仅 **3 条** distinct 句）。**"或"是选择，不是叠加**：一句里写"要么终止要么执行代码"，公开文本并没有说两者一起发生。

**四、只有 7 种后果类签名。** 92（ACE＋DOS）∶ 49（DIS＋DOS）∶ 18（ACE＋PRI＋SBX）∶ 13（BYP＋SBX）∶ 8（ACE＋SBX）∶ 3（BYP＋PRI）∶ 2（DOS＋MEM）＝ **185**（闭合）。签名只有 7 种、distinct 句只有 **23 条**。

**五、`potentially` 是唯一正向包含关系。** 全树 `potentially` **30 段**，**30 段全部在 M2 内**（30／185 = 16.2%）。它修饰的是"可能泄露"，方向是**缓和**，不是放大。

**六、编号不在后果字段。** **2,108** 个 distinct CVE 里 **2,106 只在致谢**、**2 个**（`CVE-2025-14174`、`CVE-2025-43529`）同时出现在 Impact；Impact 侧 ≥2 编号的段 **5 段**，且这 5 段是同一句 `… were also issued in response to this report.`。**M2 的 185 段里含数字的 0 段。**

**七、与在野集零交。** `M2 ∩ E`（#114 的 `exploit` 词族 108 段）＝ **0**；`M2 ∩ WO`（#119 的 178 段）＝ **0**；`M2 ∩` #117 的 `persist*` 维持集（46 段）＝ **0**。**"被并写多类后果"的句子，没有一句是已确认在野、无前提或可维持的表述。**

**八、组件面只覆盖 7.4% 的名。** 35／472 名，合计 185 段占这 35 名 Impact 全集（2,883 段）的 **6.4%**；最高的是 `libexpat` **66.7%**（16／24）、`ColorSync`／`CoreDrag`／`Wi‑Fi`／`IOGraphics`／`PrintCenter`／`Pages` **100%**（但分母只有 2–4 段）；`WebKit` 7／910 ＝ **0.8%**、`Kernel` 3／681 ＝ **0.4%**。

# 3 共现刀：0 类 3,652 ∶ 1 类 3,597 ∶ 2 类 167 ∶ 3 类 18

**3.1 这是本篇的主判据。** 把 7,434 段按命中类数分箱：**3,652 ∶ 3,597 ∶ 167 ∶ 18**。含义是：**含 Impact 的段里，49.1% 一个后果类都不命中**（多为 `may be used to …`、`may lead to a denial of service` 之外的杂句），**48.4% 恰好命中一类**，只有 **2.49% 命中多类**。

**3.2 "多类"不是"影响更大"。** 命中的两类经常是**同一件事的两种写法**。例：`ACE`＋`DOS` 92 段里，绝大多数是 `unexpected app termination or arbitrary code execution`——苹果在**一句话里给两个候选后果**，因为解析类缺陷的实际表现取决于破坏落点。**这是披露上的不确定性表达，不是叠加。**

**3.3 唯一的三类签名只有一句。** `ACE＋PRI＋SBX` 18 段全部是同一句 `An app may be able to execute arbitrary code out of its sandbox or with certain elevated privileges`。所以**"三类共现"的文本多样性是 1，不是 18**。

**3.4 分箱闭合自检。** 3,652＋3,597＋167＋18 ＝ **7,434** ＝ 含 Impact 段数；无命中 4 类以上的段。

# 4 配对刀：8 对非零，合计 221 ＝ 167＋18×3

**4.1 非零配对只有 8 对**（段数按配对计，同一 3 类段计入 3 对）：

| 配对 | 段 | 说明 |
|---|---:|---|
| `ACE＋DOS` | 92 | `… termination or arbitrary code execution` |
| `DIS＋DOS` | 49 | `… denial-of-service or potentially disclose memory contents` |
| `ACE＋SBX` | 26 | 含 18 段的三类句 ＋ 8 段两句 |
| `ACE＋PRI` | 18 | 全部来自同一条三类句 |
| `PRI＋SBX` | 18 | 全部来自同一条三类句 |
| `BYP＋SBX` | 13 | `bypass sandbox restrictions` 11＋2 |
| `BYP＋PRI` | 3 | `bypass launch constraint protections and execute … with elevated privileges` |
| `DOS＋MEM` | 2 | `A buffer overflow may result in memory corruption and unexpected app termination` |

**4.2 221 的三个分母。** 配对合计 **221**；按签名分组 **185**；按 distinct 句 **23**。三者关系：**221 ＝ 167×1 ＋ 18×3**、**185 ＝ 167 ＋ 18**、**23 条 distinct 句**（附表 B 逐条列出）。**引用"组合条数"时应写 185 段＝23 句，不写 221。**

**4.3 24 对为零。** 八类两两组合共 28 对，非零 8、**零 20**——包括直觉上最"该组合"的 `PRIV＋SBX`（有，18）、`ACE＋DIS`（**0**）、`ACE＋MEM`（**0**）、`DIS＋MEM`（**0**）、`FULL＋任意`（**0**）。**`FULL` 63 段与其余七类零交**：`arbitrary file(s)`／`any action`／`full control` 这三串从不与别的后果写在同一段里。

# 5 连接词刀：`or` 159 ∶ `and` 5 ∶ 无连接词 21

**5.1 分布。** M2 的 185 段按连接形态：**`or` 159 段（85.9%）、`and` 5 段（2.7%）、无连接词 21 段（11.4%）**，合计 185（按句文本判定，附表 B 第 3 列）。

**5.2 无连接词的 21 段只有 3 条 distinct 句。** `A shortcut may be able to bypass sandbox restrictions` **11**、`A sandboxed app may be able to execute arbitrary code with kernel privileges` **8**、`An app may be able to bypass sandbox restrictions` **2**。前两句是**复合词本身携带两类**（`sandbox` 与 `kernel privileges`／`bypass` 与 `sandbox`），**不是用连接词并写两件事**。

**5.3 `and` 只有 5 段。** 3 段 `bypass launch constraint protections **and** execute malicious code with elevated privileges`、2 段 `memory corruption **and** unexpected app termination`。**全树 `and`（宽判据）85 段与 M2 只交 5 段。**

**5.4 `or` 侧的对账。** 全树 Impact 含 `or`（词界）**660 段**，其类命中分布 **0＝22 ∶ 1＝479 ∶ 2＝141 ∶ 3＝18**，∩M2 ＝ 141＋18 ＝ **159**（与 5.1 一致）。**`, or`（逗号＋or）在 Impact 侧为 0 段**，但在三字段并集里有 22 段——那 22 段全在 Description／致谢，属"修复说明"文体，不属后果文体。

**5.5 通用连接词之外的组合词。** 见 1.1 与附表 A 组 1：**没有任何一串专门表达"两个漏洞一起用"的连接词在 Impact 出现过**。

# 6 「多个问题」句式刀：39 段里 31 段不写后果

**6.1 句式存在。** `Multiple issues in` **39 段 / 36 页 / 6 名 / 7 条 distinct 句**：`curl` **16**、`libexpat` **8**、`OpenSSH` **4**、`Perl` **4**、`Apache` **3**、`ruby` **3**、`apache` **1**。**全部是外部开源库名**——这一句式的功能是**把多个上游编号收进一条条目**，不是描述组合利用。

**6.2 后果缺失。** 39 段中 **31 段（79.5%）八类一个都不命中**；剩下 8 段全部是 `Multiple issues in libexpat, including unexpected app termination or arbitrary code execution`，正是 M2 里 8 段同一句（`∩M2 = 8`）。`including` **14 段**（3 名）。

**6.3 `Multiple` 与 `issues`。** `Multiple` 词界 **40 段 / 37 页 / 7 名**（比 39 多 1 段，那句不是 `issues in` 形）；`issues`（复数）**40 段**；`issue`（单数）**152 段 / 107 页 / 20 名**、`this issue` **103 段 / 74 页 / 12 名**，且**两者与 M2 零交**——**单数 `issue` 用来指"这一条漏洞"，从不参与并写**。

**6.4 数量修饰语全零。** `several issues`／`two issues`／`these issues`／`such issues` **全 0**；`vulnerab*` 与 `flaw` 在 Impact 侧 **0 段**（`vulnerab*` 在三字段并集有 459 段，全部在 Description／致谢）。**"多个漏洞"这四个字在后果字段里没有英文写法。**

# 7 能力与前提叠加刀：38 段 `capability`，与 M2 零交

**7.1 唯一像"叠加"的结构在前提侧。** Impact 含 `capability`／`capabilities`（词界）**38 段 / 30 页 / 5 名**，且**只有 5 条 distinct 句**。分区闭合：`read and write capability` **33** ＋ `memory write capability` **5** ＝ **38**（交 **0**）；其中 `arbitrary read and write capability` **17** 是 33 的子集。**`arbitrary read or write capability` 0 段、`memory read capability` 0 段**——**读写永远成对出现，不是两个可分选项**。

**7.2 但它与后果并写零交。** `capability` 38 段 ∩ M2 ＝ **0**。**"先有读写能力、再导致 X"这种两阶段句式在措辞层不存在**：`capability` 出现在主语短语里（`a process with the arbitrary read and write capability`），后面接的是**单一**后果。

**7.3 前提侧其他判据。** `an attacker with` **208**、`with physical access` **142**、`malicious app` **337**、`remote attacker` **194**、`local attacker` **64**、`root privileges` **233**、`malicious attacker` **8**、`unprivileged` **8**、`compromised` **8**、`given` **6**、`with access to` **4**——**其中与 M2 非零的只有 `remote attacker`（20）与 `given`（6）**；`root privileges` **233 段与 M2 零交**，`malicious app` 337 段也零交。**`entitled`、`assuming`、`after an attacker`、`once the attacker`、`already／previously installed`、`may also` 全零。**

**7.4 后果叠加连接词同样零。** `and thereby` **7 段**（1 条 distinct 句，passcode 计时）、`could also` **4 段**（1 条日历／提醒句）、`even` **11 段**（2 条）——**三者的 ∩M2 都是 0**。**公开文本不用"并且／因此／还能"把两个后果串成因果链。**

# 8 补充与后补修复刀：`This is a supplementary fix` 8 段

**8.1 "同一弱点补第二次"有句式。** `supplement*` 词族 **8 段 / 8 页 / 1 名**，8 段全是同一句 `This is a supplementary fix`；它与 `blocked in`（**8 段**）**完全同段**——那 8 段同时含 `… may break out of Web Content sandbox … blocked in iOS 17.2`。**这是本篇唯一一处"修复被分两次披露"的文本证据，但它说的是同一次修复补发，不是多个弱点组合。**

**8.2 版本号回指。** `in iOS <版本号>` **13 段 / 10 页 / 2 名**。**`follow-up`／`again`／`earlier`／`similar`／`as well`／`reappear`／`regress` 在 Impact 侧全零**；`previously`、`previous` 各 **1 段**；`since` **0 段**（其 45 段是 `before`）。**"之前那次修复没做完"这种表述不存在。**

**8.3 `Entry added`。** Impact 侧 **0 段**，三字段并集 **1,358 段 / 172 页 / 225 名**——它写在致谢尾部，属条目元数据，不属后果。**用 Impact 判据测"后补修复"会得到 0，这是口径而不是事实。**

**8.4 `also` 的定位。** Impact 含 `also` **23 段 / 16 页 / 3 名**，**∩M2 = 0**；`may also` **0 段**。`also` 在 Impact 的作用是编号交叉引用（见第 13 章），**不是后果叠加**。

# 9 情态刀：`may` 177 ∶ 其他 8 ＝ 185

**9.1 M2 内情态闭合。** 含 `may` 的 **177 段**；不含 `may` 的 **8 段**，且这 8 段是**同一条句** `Multiple issues in libexpat, including unexpected app termination or arbitrary code execution`——**它没有情态动词，因为整个句子是名词短语。**

**9.2 与其他篇对照。** #119 的 WO 集里 `may be able to` 占 **123／178 = 69.1%**；M2 内 `may be able` 只有 **159 段中的一部分**，且 M2 的主干句式（`Processing a … may lead to … or …`）**不需要** `be able`。**同一语料两套情态结构**（延续 #120 第 7 章的结论）。

**9.3 `will` 为 0。** M2 内**没有一段用 `will`**；后果一律是或然式。**"组合会造成更大影响"这句在措辞层没有断言形态**——没有 `will`、没有 `and thereby`、没有 `could also`。

# 10 缓和词刀：`potentially` 30 段 100% 落在 M2 内

**10.1 正向包含关系。** 全树 Impact 含 `potentially`（词界）**30 段 / 29 页 / 8 名**，**这 30 段全部属于 M2**（30／185 = 16.2%）。两句承担：`Processing a maliciously crafted file may lead to a denial-of-service or potentially disclose memory contents` **22**、`Processing a file may lead to a denial-of-service or potentially disclose memory contents` **8**。

**10.2 方向解释。** `potentially` 修饰的是**第二个候选后果**（泄露），作用是**降低断言强度**，不是提高。**所以 M2 里有 16.2% 的句子在语法上把"更大影响"往回收，而不是往外推。** 这是本篇对"实现更大影响"这一维最直接的负面证据。

**10.3 其他缓和词。** `may not` **26 段**、`intended` **9 段**，两者 ∩M2 ＝ **0**。M2 内的限定词覆盖率：`unexpected` **113 段（61.1%）**、`arbitrary` **118 段（63.8%）**、`malicious*` **77 段（41.6%）**、`certain` **18 段（9.7%，全部来自同一条三类句的 `certain elevated privileges`）**。

# 11 镜像与切块自检

**11.1 M2 的 185 段里没有解码缺陷。** 含引号 **0 段**、含 `&rsquo;`／`&rdquo;`／`&ldquo;`／`&amp;`／`&nbsp;` 实体 **0 段**、含数字 **0 段**。对照 #120 的 HI 集（`&rsquo;` 24 段、`&rdquo;` 4 段），**M2 是干净子集**，因为后果句是模板化短句。

**11.2 字段齐备。** 185 段全部同时有 Description 与致谢（**185／185**），所以第 14 章的编号统计可以安全地在致谢侧做。

**11.3 版式域。** M2 在域 A **185 段**、域 B（11 页粗体版式）**0 段**。`118479`、`120303`、`120304`、`120305`、`120307`、`120308`、`120309`、`120310`、`120311`、`120312`、`120339` 这 11 页没有任何一段命中 ≥2 类——**版式域与后果并写互不相干**。

# 12 组件层：35 名 / 472 名，6 名的占比是 100%

**12.1 覆盖面。** M2 涉及 **35 个组件名 = 全树 472 名的 7.4%**；这 35 名的 Impact 全集 **2,883 段**，**185／2,883 = 6.4%**（附表 C）。

**12.2 高占比是低分母造成的。** 100% 的 6 名分母只有 1–4 段：`ColorSync` 4／4、`CoreDrag` 3／3、`Wi‑Fi` 3／3、`IOGraphics` 2／2、`PrintCenter` 2／2、`Pages` 1／1。**有意义的相对高值**：`libexpat` **16／24 = 66.7%**、`RealityKit` **7／14 = 50.0%**、`Metal` **11／25 = 44.0%**。

**12.3 大组件几乎不并写。** `WebKit` **7／910 = 0.8%**、`Kernel` **3／681 = 0.4%**、`AMFI` 3／146 = 2.1%、`Shortcuts` 5／129 = 3.9%。**披露量最大的两个组件，恰恰是"一句一类"的写法。**

**12.4 `∩R` 只有 4 名非零。** `libexpat` 8、`Bluetooth` 8、`Metal` 4、`CUPS` 3，合计 **23**，等于 `M2 ∩ R`（第 15 章）。**其余 31 名的组合句完全不声明通道**。**`∩WO` 整列为 0。**

# 13 CVE 编号普查：2,106／2,108 只在致谢字段

**13.1 distinct 编号。** 全树共 **2,108 个** distinct `CVE-YYYY-NNNN`。字段落位：**仅致谢 2,106**、**致谢＋Impact 2**、**Impact 单侧 0**。那 2 个是 `CVE-2025-14174` 与 `CVE-2025-43529`，来自 `… were also issued in response to this report.`。

**13.2 段内分箱（三字段并集）。** 0 个 **1,706** ∶ 1 个 **6,952** ∶ 2 个 **339** ∶ 3 个 **84** ∶ 4 个 **20** ∶ 5 个 **16** ∶ ≥6 个 **19**，合计 **9,136**（闭合）。**≥2 个编号的段 478**；其中**致谢侧 ≥2 的 459 段**，差的 **19 段**由 3 条 Impact 句造成（`… was／were also issued in response to this report.`，7＋7＋5）。

**13.3 Impact 侧 ≥2 编号只有 5 段。** 这 5 段是同一句。**换句话说：整个语料里"一段后果文本同时点名两个 CVE"只有一种写法。**

**13.4 页内分箱。** 257 页**每页都至少 1 个编号**（0 编号页 **0 张**）；分箱 **1＝56 ∶ 2＝18 ∶ 3＝3 ∶ 4＝1 ∶ 5＝5 ∶ 6–9＝10 ∶ 10–19＝25 ∶ 20–49＝90 ∶ 50–99＝31 ∶ 100+＝18**，合计 **257**（闭合）；**≥2 个编号的页 201／257 = 78.2%**；页内编号合计 **8,136** 次。Top5 页：`149035` **242**、`149042` **173**、`149043` **173**、`128067` **170**、`122373` **165**。

**13.5 单编号覆盖页数。** 只出现在 1 页的 **425 个**、2 页 **194**、3 页 **575**、4 页 **176**、5 页 **175**、6 页 **165**、7 页 **191**、8 页 **196**、9 页 **2**、10 页 **2**、11 页 **2**、12 页 **3**、13 页 **1**、15 页 **1**，合计 **2,108**（闭合）。**"一个编号跨多页"是产品版本枚举造成的，不是组合利用。**

**13.6 `also issued`。** **19 段 / 12 页 / 2 名**，3 条 distinct 子句（`was also issued` 7＋7、`were also issued` 5），**∩M2 = 0**——**编号交叉引用的句子从不与多类后果同段。**

# 14 栏键对账：459 段＝459 栏，与 #115 同口径

**14.1 什么是"一条修复条目"。** 段号不是条目号：一个组件名下一段致谢可能列多个编号，也可能多段属于同一条目。本篇按**栏键**（页 ID ＋ 组件名 ＋ 致谢前 40 字）分组，得到 **7,428 栏**；其中含 ≥2 个 distinct 编号的栏 **459 栏**。

**14.2 与段数一致。** 致谢侧 ≥2 编号的**段数也是 459**，且**按成员验证为同一集合**（差 0）。**这说明"一条条目列两个编号"在文本上不会跨栏合并，段与栏在这一维重合。** #115 报的 M 集就是这 459——**两篇口径可互操作，但没有新增信息。**

**14.3 为什么这不支撑"组合"。** 459 栏的**全部语义是"一条修复覆盖两个上游编号"**，其 Impact 字段往往为空或写单一后果。**同段 ≥2 编号的 478 段里，与 M2（同段 ≥2 后果类）相交的部分为 0**——**"多个编号"与"多类后果"在措辞层是两个不相交的集合**。

# 15 与 #114／#117 的接缝：两个零交

**15.1 `M2 ∩ E = 0`。** E ＝ #114 的在野集（Impact 含 `exploit` 词族）**108 段 / 76 页 / 12 名**。**185 段里没有一段含 `exploit`。** 注意不要用"extremely sophisticated attacker"这一短语口径（那只有 59 段）——两篇必须同一判据；按 #114 正式定义测得的仍是 0。

**15.2 `M2 ∩ persist* = 0`。** #117 的维持集（`persist*` 词族）**46 段 / 26 页 / 10 名**，与 M2 **零交**（附表 A 组 2 的 `persist*` 行 ∩组合段列 = 0）。**"重启后仍能维持"与"一段里并写两类后果"没有任何一句同时出现。**

**15.3 推论。** 在公开措辞层，**"组合"既不接在野证据，也不接维持性证据**。**M2 是一条孤立的句式集**，它与本篇之外的四个对照集只有 `R`（23）与 `G`／`HI`（101／9）三个非零接口。

# 16 与 #118／#119／#120 的接口：∩R 23、∩WO 0、∩HI 9

**16.1 通道侧。** `M2 ∩ R` ＝ **23 段**（distinct 3 条句，全部含 `remote`：`A remote attacker may be able to cause unexpected app termination or arbitrary code execution` 12、`A remote attacker may cause an unexpected app termination or arbitrary code execution` 8、`A remote user may cause …` 3）。**162 段（87.6%）不声明任何通道。**

**16.2 并集侧。** `M2 ∪ WO` ＝ **363 段**（185＋178−0）；`M2 ∪ R` ＝ **1,005 段**（185＋843−23）。M2 与 WO 的**零交**意味着：**"无前提触发"的句子从不并写两类后果**——无前提句式只写一个后果（多为 `sandbox`／`restrictions` 一类）。

**16.3 交互侧。** `M2 ∩ HI` ＝ **9 段**（`opening` 7、`mounting` 2）；`M2 ∩ G` ＝ **101 段**（`processing` 91、`opening` 7、`mounting` 2、`parsing` 1）。**M2 的 54.6% 是"处理某个文件"的机器动词句式**，与 #116 的动名词集主干同源；`∩U`（含 `user`）只有 **4 段**。

# 17 弱点名词刀对账：789 段是假象，184 段是真

**17.1 换一把刀。** 第 1 章用**后果**类；这一章用**弱点名词**类（19 串：`issue`、`memory`、`corrupt*`、`sandbox`、`authenticat*`、`restrict*`、`permission`、`input`、`identifier`、`password／passcode`、`credential`、`validation`、`authorization`、`logic`、`type confusion`、`entitle*`、`overflow`、`encrypt*`、`bounds`）。**同段命中 ≥2 个弱点名词的段 = 789。**

**17.2 为什么 789 不能用。** `memory` **1,136 段** 与 `corrupt*` **682 段** 重叠 **628 段**——`memory corruption` 这**一个**词组被两把判据各算一次，制造出假的"多弱点"。**合并 `memory`＋`corruption` 为同一类后：184 段 / 117 页 / 32 名，distinct 26 句。**

**17.3 与后果刀的关系。** 合并后的 **184** 与 M2 的 **185** 接近但**不是同一集合**：弱点名词侧 **∩HI = 0、∩WO = 13、∩R = 30**。**两把刀都指向"公开文本在同一段里并写不超过两个东西"这一事实，数值巧合不代表成员相同。**

**17.4 弱点侧的全零项。** `flaw`、`improper`、`insufficient`、`untrusted`、`weak`、`misconfigur*`、`default`、`signature`、`integer`（Impact）、`race condition`（Impact）、`use after free`（Impact）、`access control`（Impact）、`vulnerab*`（Impact）**全部 0 段**。其中 `vulnerab*` 并集 459、`race condition` 并集 232、`integer` 并集 158、`use after free` 并集 127——**这些是 Description／致谢侧的词汇，不是后果侧的词汇。**

# 18 页与分布：82 页，Top10 页承载 31.4%

**18.1 页分箱。** M2 的 185 段分布在 **82 页**：**1 段 34 页 ∶ 2 段 25 ∶ 3 段 9 ∶ 4 段 7 ∶ 5 段 3 ∶ 6 段 1 ∶ 7 段 1 ∶ 8 段 1 ∶ 10 段 1**，页合计 **82**、段合计 **185**（双闭合）。

**18.2 聚集度。** Top10 页 **58 段 = 31.4%**；Top1 页 `149035` **10 段**。**注意同一页在附表 B 的"页"列会被多个 distinct 句重复计数，页列合计 152 > 82**，附表 B 的合计行已标明是"页列合计"而不是页数。

**18.3 名列同理。** 附表 B 名列合计 **45 > 35**，同一组件名可跨多条句；**M2 的去重名数是 35**。

# 19 中文手册层（T1）：91 词里 20 词为零

**19.1 组合词有，但不与"漏洞"搭配。** `组合` **14 次 / 10 页**、`一起` 18／15、`同时` 69／48、`配合` 21／17、`相互` 11／8、`捆绑` 8／6、`协同` 3／3、`一并` 1、`串联` 2；**`拼接`／`串接`／`合用` 为 0**。

**19.2 "链"字有，"利用链"没有。** `链` **73 次 / 40 页**（全部是 `iCloud Link`／链接一义）、`链路` 1、`路径` 4／3、`阶段` 12／10、`步骤` 16／11、`序列` 15／10、`逐步` 7／2、`依次` 1；**`利用链`、`漏洞链`、`攻击链`、`链条` 全 0**。

**19.3 放大词几乎不存在。** `更大` **3**、`扩大` 3、`增强` 21、`提升` 7、`进一步` 30／25、`完全` 35、`任意` 9、`完整` 494、`彻底` 1；**`更严重`、`加深`、`危害`、`后果` 全 0**，`严重` 仅 **1** 次。

**19.4 弱点词很弱。** `漏洞` **20 次 / 12 页**、`问题` 17／11、`缺陷` 1、**`弱点` 0**；`沙盒` 27／11、**`沙箱` 0**、`绕过` 23／9、`篡改` 19、`权限` 284、`授权` 353、`认证` 380／93、`验证` 433、`限制` 479。**`越狱`、`提权` 为 0**；`代码执行` 3／2、`拒绝服务` 1、`利用` 43／29，而 **`利用难度`、`利用方式`、`攻击方式`、`组合利用`、`一并利用` 全 0**。

**19.5 量词与连词。** `多个` **25／21**、`两个` 20／18、`多种` 9、`三种` 5、`若干` 3、`各类` 3、`数个` 1、`各项` 1、`全部` 4、`所有` 525／178；连词侧 `和` **6,204／178**、`或` **1,307／178**、`以及` 182／90、`与` 1,103、`并` 507／140、`或者` 79／53、`并且` 55、`而且` 17。**中文手册有充沛的并列连词，但没有一个"组合漏洞"的术语。**

**19.6 口径提醒。** 手册是"如何做／是什么"的说明文体，通告是"会导致什么"的披露文体。**本章的比值只在 178 页手册内成立，不能拿来补强英文 M2 的 2.49%。**

# 20 复算步骤与断言

**20.1 载入。** `require "./_tmp/tl111load.pl"; my %B = load_blocks();` → **9,136 段**。两版式域见 #111 第 1 章；域 B 的 11 个页 ID 在本篇第 11.3 节列全。

**20.2 锚定断言（必须先通过）。** `U(\%R)==843 && U(\%WO)==178 && U(\%UU)==1627 && U(\%G)==2179 && U(\%HI)==273 && U(\%E)==108`；再断言 `类命中分布 0=3652 && 1=3597 && 2=167 && 3=18 && U(\%M2)==185`。**任一不过即停。**

**20.3 判据字符串。** `$S = '[ \x{a0}\x{202f}]'`（NBSP／窄不换行空格都算空格）；八类正则按附表 A 组 6 逐字使用；`or`／`issue`／`capability`／`potentially`／`persist`／`user`／`without` 一律**词界**，`and` 用 `${S}and${S}` 宽判据（85 段）。

**20.4 生成附表。** `perl _tmp/tl121tab.pl` → 五份 `tl121{A..E}.md`，脚本内自断言：**A 行=130（31／35／12／25／19／8，三字段全零 70，Impact 列合计 6,966）**、**B 行=23 且段合计=185 且 ∩WO=0 ∩R=23 ∩G=101 ∩HI=9 且 or／and／无 = 159／5／21**、**C 行=35 且段合计=185 且 ∩WO=0 ∩R=23**、**D 行=36 且段分箱=9,136 且页分箱=257 且 distinct CVE=2,108**、**E 行=91（全零 20）**。

**20.5 关键点（复算时最容易做错的四件）。** **（a）** 配对 221 与签名 185 是两个分母，用**签名分组**去数段落；**（b）** `∩R` 的 23 段必须**逐成员**验证（附表 C 里只有 4 名非零）；**（c）** `potentially` 的 30 段"全在 M2 内"要用**子集判定**而不是相交非空；**（d）** 弱点名词刀必须先合并 `memory`＋`corruption`，否则 789 会替 184。

**20.6 门禁。** 对本篇 md 运行 house 门禁脚本 → 9 条 `^@@`、`@@TOC@@`、联系句柄恰好 2 次、零代码围栏、无本机绝对路径、表尾顺序 RULE｜BOUNDARY｜TELEGRAM、五个附表标题的行数与实际数据行一致。

# 21 缺口清单

**缺口一：没有"多个弱点"的计数对象。** `vulnerab*`、`flaw` 在 Impact 侧 0 段，**语料在后果字段不命名"弱点"这一抽象概念**，只写具体动作与对象。**因此"组合几个弱点"无法在任何公开英文文本里被数出来。**

**缺口二：没有叠加连接词。** 26 串组合连接词全零、`and thereby`／`could also`／`may also` 与 M2 零交。**后果之间只有选择关系（`or`），没有因果关系或并列关系。**

**缺口三：没有放大断言。** 26 项放大词全零、`will` 0 段、`potentially` 反向缓和。**"更大"这一比较级在措辞层没有承载。**

**缺口四：编号与后果字段分离。** 2,106／2,108 个编号只在致谢；Impact 侧 ≥2 编号的 5 段是同句。**"多个编号"与"多类后果"相交为 0**，无法用编号数当组合数。

**缺口五：通道声明不完整。** 162／185 段不声明通道（87.6%）。**不能说这些组合"是否远程可达"——文本没写。**

**缺口六：外部库条目结构性缺后果。** `Multiple issues in` 的 31 段不写八类中任何一类。**上游库类条目在措辞层是"修了"而不是"会怎样"。**

**缺口七：域 B 为 0。** 11 页粗体版式没有一段 M2。**这 11 页不能用来验证本篇比例。**

**缺口八：中文层不补强。** `利用链`、`漏洞链`、`攻击链`、`弱点`、`更严重`、`组合利用` 全 0。**中文手册不提供"组合"这一维的术语。**

# 22 常见误读纠正

**误读一：把 185 段读成"185 条组合利用"。** 185 是**段数**；distinct 句 **23 条**、后果类签名 **7 种**、组件名 **35 个**。**正确说法：7 种后果并写形态、23 个句子、185 个条目。**

**误读二：把配对 221 当组合条数。** 18 段命中 3 类，一段贡献 3 对，**221 = 167 + 18×3**。引用组合条数用 **185**。

**误读三：把 `ACE＋SBX = 26` 与签名里的 `ACE＋SBX = 8` 当矛盾。** 两者都对：26 是**配对**口径（含那 18 段的三类句），8 是**签名**口径（恰好这两类、不含 PRI）。**同一标签两个分母。**

**误读四：把 `or` 读成"两个后果都发生"。** `A or B` 在英文里是**候选**，披露文本用它表达"取决于破坏落点"。**M2 的 85.9% 因此恰恰不是叠加陈述。**

**误读五：把弱点名词刀的 789 当"多弱点段数"。** `memory` 与 `corrupt*` 重叠 **628 段**造成假共现；**合并后 184**。**789 是判据重叠产物，不是语言事实。**

**误读六：把 `potentially` 读成放大词。** 它 30 段全在 M2 内，但修饰第二个候选后果、**降低**断言强度。**方向与"更大影响"相反。**

**误读七：把 201／257 页含 ≥2 个编号读成"多数条目是组合"。** 页内编号来自**版本枚举**（一个编号常跨 3–8 页，见附表 D 覆盖分布：3 页 575 个、8 页 196 个）；**栏级 ≥2 才 459，段级 ≥2 与 M2 交 0**。**页数多不等于条目组合。**

# 23 结论

**一、"组合"在后果字段没有词形。** 31 项组合连接词里 **25 项三字段全零**，`combin*`、`chain*`、`attack chain`、`exploit chain`、`together with`、`as well as`、`two or more`、`not only`、`and then` **一次都没出现**。唯一非零的 4 项是通用并列（` and ` 85、` or` 660、`分号` 28）与语义无关的 `link*`（15）。

**二、"更大影响"也没有词形。** 35 项放大与比较词里 **26 项全零**，含 `impact` 这个词本身、`greater`、`more severe`、`worse`、`compromise`、`full access`、`any action`、`sandbox escape`、`jailbreak`。

**三、能测的只有同段并写：185 段 = 2.49%。** 0 类 3,652 ∶ 1 类 3,597 ∶ 2 类 167 ∶ 3 类 18，闭合 7,434。**7 种签名、23 条 distinct 句、35 个组件名（7.4% 的名）。**

**四、并写形态是选择而不是叠加。** `or` **159** ∶ `and` **5** ∶ 无连接词 **21（3 句）**；情态 `may` **177** ∶ 无 `may` **8（1 句）**；`will` **0**；`and thereby`／`could also`／`may also` 与 M2 **零交**。

**五、唯一正向包含是缓和词。** `potentially` **30 段全部在 M2 内**（16.2%），方向是**降低**断言。**这是"实现更大影响"这一维的负面证据。**

**六、"多个弱点"与"多类后果"不相交。** 致谢侧 ≥2 编号 **459 段 = 459 栏**（栏总数 7,428），但**同段 ≥2 编号的 478 段与 M2 零交**；Impact 侧 ≥2 编号只有 **5 段、同一句**；`Multiple issues in` 的 39 段里 **31 段不写任何后果类**。

**七、接缝全部为负。** `M2 ∩ E = 0`（在野）、`M2 ∩ WO = 0`（无前提）、`M2 ∩ persist* = 0`（维持）、`M2 ∩ R = 23`（通道，87.6% 不声明）、`M2 ∩ HI = 9`、`M2 ∩ G = 101`、`M2 ∩ U = 4`、域 B **0**。

**八、所以本篇的产出是一条可复核的否定结论加三件可核对的事。** **否定结论**：在公开英文后果字段里，**"组合多个漏洞或弱点以实现更大影响"这一陈述不可统计**——组合语义**0 词**、放大语义**0 词**、编号与后果**字段分离**、后果并写只有 `or` 型选择关系。**可核对的三件事**：**（a）** 同段多类后果的覆盖率 **2.49%（185／7,434）**、7 种签名、23 条 distinct 句；**（b）** `potentially` **30 段 100% 落在该集内**且方向为缓和；**（c）** 致谢侧 ≥2 编号的 **459 段与 459 栏按成员相同**，且**与该集零交**。防御侧可据此核对自家披露文案：若要表达"多个弱点一起用会放大"，必须**显式写出叠加连接词与放大比较级**，而这正是当前公开措辞两项都缺的一件事实。**本篇不给任何组件"实际能否被组合利用"的判断，不给任何利用链存在与否的判断，也不给任何一条"这样配置就能阻断组合"的操作建议。**

# 附表 A 组合与放大判据六组电池（131 行）

| 组 | 判据 | Impact 段 | 页 | 名 | 三字段并集段 | ∩WO | ∩R | ∩user | ∩组合段 |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|
| 组合连接词 | `in combination with` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `combined with` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `combining` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `combination 词族` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `together with` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `along with` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `as well as` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `in conjunction with` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `chain 词族` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `attack chain` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `exploit chain` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `sequence` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `series` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `linked 词族` | 15 | 15 | 2 | 42 | 0 | 0 | 6 | 0 |
| 组合连接词 | `several` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `two or more` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `both … and` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `in addition（to）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `further` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `moreover` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `furthermore` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `plus（词界）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `not only` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `and then` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `then` | 0 | 0 | 0 | 3 | 0 | 0 | 0 | 0 |
| 组合连接词 | `subsequently` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `subsequent` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 组合连接词 | `并列 and（宽）` | 85 | 65 | 21 | 1601 | 5 | 10 | 7 | 5 |
| 组合连接词 | `并列 or（宽）` | 660 | 144 | 67 | 704 | 0 | 85 | 44 | 159 |
| 组合连接词 | ``, or`` | 0 | 0 | 0 | 22 | 0 | 0 | 0 | 0 |
| 组合连接词 | `分号 ;` | 28 | 27 | 10 | 317 | 0 | 8 | 20 | 0 |
| 放大与比较词 | `greater` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `more severe` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `worse` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `larger` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `bigger` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `severe` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `critical` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `impact（词本身）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `amplif*` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `intensif*` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `widen*` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `increase*` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `expand*` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `escalat*` | 9 | 9 | 2 | 9 | 0 | 0 | 2 | 0 |
| 放大与比较词 | `extend*` | 8 | 8 | 1 | 8 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `more` | 2 | 2 | 2 | 217 | 0 | 0 | 2 | 0 |
| 放大与比较词 | `most` | 2 | 2 | 1 | 2 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `full` | 3 | 3 | 1 | 3 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `complete` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `entire` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `total` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `complete control` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `full access` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `any action` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `system-wide` | 7 | 7 | 1 | 7 | 0 | 7 | 0 | 0 |
| 放大与比较词 | `root privileges` | 233 | 74 | 43 | 233 | 2 | 3 | 2 | 0 |
| 放大与比较词 | `compromise` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `breach` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `take control` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `vector` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `primitive` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `sandbox escape` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `jailbreak` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 放大与比较词 | `persist*` | 46 | 26 | 10 | 46 | 0 | 3 | 0 | 0 |
| 放大与比较词 | `potentially` | 30 | 29 | 8 | 30 | 0 | 0 | 0 | 30 |
| 「多个问题」句式 | `Multiple issues in` | 39 | 36 | 6 | 39 | 0 | 0 | 0 | 8 |
| 「多个问题」句式 | `Multiple（词界）` | 40 | 37 | 7 | 52 | 0 | 0 | 0 | 8 |
| 「多个问题」句式 | `issues（复数）` | 40 | 37 | 7 | 63 | 0 | 0 | 0 | 8 |
| 「多个问题」句式 | `issue（单数）` | 152 | 107 | 20 | 6458 | 1 | 4 | 1 | 0 |
| 「多个问题」句式 | `this issue` | 103 | 74 | 12 | 1166 | 0 | 4 | 0 | 0 |
| 「多个问题」句式 | `including` | 14 | 14 | 3 | 14 | 0 | 0 | 0 | 8 |
| 「多个问题」句式 | `several issues` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 「多个问题」句式 | `two issues` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 「多个问题」句式 | `these issues` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 「多个问题」句式 | `such issues` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 「多个问题」句式 | `vulnerab* 词族` | 0 | 0 | 0 | 459 | 0 | 0 | 0 | 0 |
| 「多个问题」句式 | `flaw` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `read and write capability` | 33 | 25 | 5 | 33 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `arbitrary read and write capability` | 17 | 15 | 3 | 17 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `arbitrary read or write capability` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `memory write capability` | 5 | 5 | 1 | 5 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `capability／capabilities` | 38 | 30 | 5 | 38 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `an attacker with` | 208 | 97 | 49 | 208 | 1 | 17 | 73 | 0 |
| 能力与前提叠加 | `with access to` | 4 | 4 | 1 | 4 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `malicious app` | 337 | 105 | 67 | 337 | 13 | 1 | 54 | 0 |
| 能力与前提叠加 | `malicious attacker` | 8 | 8 | 2 | 8 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `local attacker` | 64 | 36 | 14 | 64 | 0 | 0 | 9 | 0 |
| 能力与前提叠加 | `remote attacker` | 194 | 89 | 26 | 194 | 0 | 194 | 0 | 20 |
| 能力与前提叠加 | `with physical access` | 142 | 71 | 39 | 142 | 1 | 4 | 70 | 0 |
| 能力与前提叠加 | `unprivileged` | 8 | 8 | 3 | 8 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `entitled` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `root privileges（前提侧）` | 233 | 74 | 43 | 233 | 2 | 3 | 2 | 0 |
| 能力与前提叠加 | `compromised` | 8 | 8 | 1 | 8 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `given` | 6 | 6 | 1 | 6 | 0 | 0 | 6 | 0 |
| 能力与前提叠加 | `assuming` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `after an attacker` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `once the attacker` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `already／previously installed` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `and thereby` | 7 | 7 | 1 | 7 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `could also` | 4 | 4 | 1 | 4 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `may also` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 能力与前提叠加 | `even` | 11 | 11 | 3 | 11 | 0 | 8 | 0 | 0 |
| 补充与后补修复 | `supplementary 词族` | 8 | 8 | 1 | 8 | 0 | 0 | 0 | 0 |
| 补充与后补修复 | `This is a supplementary fix` | 8 | 8 | 1 | 8 | 0 | 0 | 0 | 0 |
| 补充与后补修复 | `blocked in` | 8 | 8 | 1 | 8 | 0 | 0 | 0 | 0 |
| 补充与后补修复 | `in iOS <版本号>` | 13 | 10 | 2 | 13 | 0 | 0 | 0 | 0 |
| 补充与后补修复 | `Entry added` | 0 | 0 | 0 | 1358 | 0 | 0 | 0 | 0 |
| 补充与后补修复 | `follow-up／followup` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 补充与后补修复 | `again` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 补充与后补修复 | `earlier` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 补充与后补修复 | `previously` | 1 | 1 | 1 | 1 | 0 | 1 | 0 | 0 |
| 补充与后补修复 | `previous` | 1 | 1 | 1 | 1 | 0 | 0 | 1 | 0 |
| 补充与后补修复 | `before` | 45 | 36 | 8 | 45 | 0 | 0 | 1 | 0 |
| 补充与后补修复 | `since` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 补充与后补修复 | `similar` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 补充与后补修复 | `same` | 21 | 18 | 7 | 21 | 7 | 12 | 11 | 0 |
| 补充与后补修复 | `related` | 7 | 7 | 2 | 7 | 0 | 0 | 4 | 0 |
| 补充与后补修复 | `reappear／regress` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 补充与后补修复 | `as well` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 补充与后补修复 | `also（Impact）` | 23 | 16 | 3 | 23 | 0 | 0 | 0 | 0 |
| 补充与后补修复 | `second／third` | 3 | 3 | 1 | 202 | 0 | 0 | 0 | 0 |
| 后果强度八类 | `ACE 类` | 336 | 120 | 43 | 336 | 0 | 36 | 5 | 118 |
| 后果强度八类 | `BYP 类` | 291 | 114 | 56 | 301 | 0 | 42 | 8 | 16 |
| 后果强度八类 | `DIS 类` | 530 | 144 | 63 | 603 | 0 | 63 | 141 | 49 |
| 后果强度八类 | `DOS 类` | 2003 | 176 | 130 | 2014 | 0 | 335 | 61 | 143 |
| 后果强度八类 | `FULL 类` | 63 | 43 | 18 | 63 | 0 | 7 | 7 | 0 |
| 后果强度八类 | `MEM 类` | 220 | 85 | 17 | 390 | 0 | 0 | 0 | 2 |
| 后果强度八类 | `PRI 类` | 124 | 62 | 30 | 124 | 0 | 0 | 22 | 21 |
| 后果强度八类 | `SBX 类` | 418 | 119 | 67 | 475 | 3 | 28 | 32 | 39 |
| **合计** | 130 项（全零 70 项） | — | — | — | — | — | — | — | 185 |

# 附表 B 23 条 distinct 组合句 × 后果类签名 × 连接词（24 行）

| # | 后果类组合 | 连接词 | 段 | 页 | 名 | ∩WO | ∩R | ∩G | ∩HI | distinct 组件数 | 句全文 |
|---|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---|
| 1 | ACE+DOS | or | 36 | 16 | 4 | 0 | 0 | 36 | 0 | 4 | Processing a maliciously crafted file may lead to unexpected app termination or arbitrary code execution |
| 2 | DIS+DOS | or | 22 | 22 | 5 | 0 | 0 | 22 | 0 | 5 | Processing a maliciously crafted file may lead to a denial-of-service or potentially disclose memory contents |
| 3 | ACE+DOS | or | 18 | 12 | 4 | 0 | 0 | 18 | 0 | 4 | Processing a file may lead to unexpected app termination or arbitrary code execution |
| 4 | ACE+PRI+SBX | or | 18 | 15 | 7 | 0 | 0 | 0 | 0 | 7 | An app may be able to execute arbitrary code out of its sandbox or with certain elevated privileges |
| 5 | ACE+DOS | or | 12 | 12 | 2 | 0 | 12 | 0 | 0 | 2 | A remote attacker may be able to cause unexpected app termination or arbitrary code execution |
| 6 | BYP+SBX | 无连接词 | 11 | 11 | 2 | 0 | 0 | 0 | 0 | 2 | A shortcut may be able to bypass sandbox restrictions |
| 7 | ACE+DOS | or | 8 | 8 | 1 | 0 | 8 | 0 | 0 | 1 | A remote attacker may cause an unexpected app termination or arbitrary code execution |
| 8 | ACE+DOS | or | 8 | 8 | 1 | 0 | 0 | 0 | 0 | 1 | Multiple issues in libexpat, including unexpected app termination or arbitrary code execution |
| 9 | ACE+SBX | 无连接词 | 8 | 8 | 1 | 0 | 0 | 0 | 0 | 1 | A sandboxed app may be able to execute arbitrary code with kernel privileges |
| 10 | DIS+DOS | or | 8 | 7 | 3 | 0 | 0 | 8 | 0 | 3 | Processing a file may lead to a denial-of-service or potentially disclose memory contents |
| 11 | DIS+DOS | or | 7 | 7 | 1 | 0 | 0 | 7 | 7 | 1 | Opening a maliciously crafted file may cause unexpected process termination or disclose process memory |
| 12 | DIS+DOS | or | 4 | 3 | 3 | 0 | 0 | 4 | 0 | 3 | Processing a maliciously crafted Pages document may result in unexpected termination or disclosure of process memory |
| 13 | ACE+DOS | or | 3 | 3 | 1 | 0 | 3 | 0 | 0 | 1 | A remote user may cause an unexpected app termination or arbitrary code execution |
| 14 | ACE+DOS | or | 3 | 3 | 1 | 0 | 0 | 0 | 0 | 1 | An attacker may be able to cause unexpected system termination or arbitrary code execution in DCP firmware |
| 15 | BYP+PRI | and | 3 | 3 | 1 | 0 | 0 | 0 | 0 | 1 | An app may be able to bypass launch constraint protections and execute malicious code with elevated privileges |
| 16 | DIS+DOS | or | 3 | 3 | 1 | 0 | 0 | 0 | 0 | 1 | An app may be able to cause unexpected process termination or disclose process memory |
| 17 | DIS+DOS | or | 3 | 3 | 1 | 0 | 0 | 3 | 0 | 1 | Processing a maliciously crafted file may result in unexpected app termination or disclosure of process memory |
| 18 | ACE+DOS | or | 2 | 2 | 1 | 0 | 0 | 0 | 0 | 1 | An attacker may be able to cause unexpected app termination or arbitrary code execution |
| 19 | BYP+SBX | 无连接词 | 2 | 2 | 1 | 0 | 0 | 0 | 0 | 1 | An app may be able to bypass sandbox restrictions |
| 20 | DIS+DOS | or | 2 | 1 | 1 | 0 | 0 | 2 | 2 | 1 | Mounting a maliciously crafted exFAT volume may cause unexpected system termination or kernel memory disclosure |
| 21 | DOS+MEM | and | 2 | 1 | 1 | 0 | 0 | 0 | 0 | 1 | A buffer overflow may result in memory corruption and unexpected app termination |
| 22 | ACE+DOS | or | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 1 | A user may be able to cause unexpected app termination or arbitrary code execution |
| 23 | ACE+DOS | or | 1 | 1 | 1 | 0 | 0 | 1 | 0 | 1 | Parsing a file may lead to an unexpected app termination or arbitrary code execution |
| **合计** | 7 种组合 | or 159∶and 5∶无 21 | 185 | 页列合计 152 | 名列合计 45 | 0 | 23 | 101 | 9 | 35 名 | — |

# 附表 C 组件名 × M2 段数与组合占比（36 行）

| 组件名 | 组合段 | ∩WO | ∩R | 该组件 Impact 全集 | 组合占比 | 命中的后果类（段数） |
|---|---:|---:|---:|---:|---:|---|
| `SceneKit` | 24 | 0 | 0 | 112 | 21.4% | ACE24、DOS24 |
| `CoreMedia` | 16 | 0 | 0 | 150 | 10.7% | ACE8、DIS8、DOS16 |
| `libexpat` | 16 | 0 | 8 | 24 | 66.7% | ACE16、DOS16 |
| `Metal` | 11 | 0 | 4 | 25 | 44.0% | ACE11、DOS11 |
| `AVEVideoEncoder` | 8 | 0 | 0 | 45 | 17.8% | ACE8、SBX8 |
| `Bluetooth` | 8 | 0 | 8 | 41 | 19.5% | ACE8、DOS8 |
| `CoreGraphics` | 8 | 0 | 0 | 27 | 29.6% | DIS8、DOS8 |
| `RealityKit` | 7 | 0 | 0 | 14 | 50.0% | DIS7、DOS7 |
| `WebKit` | 7 | 0 | 0 | 910 | 0.8% | ACE7、DOS7 |
| `CUPS` | 6 | 0 | 3 | 43 | 14.0% | ACE3、DIS3、DOS6 |
| `Messages` | 6 | 0 | 0 | 50 | 12.0% | BYP6、SBX6 |
| `libxpc` | 6 | 0 | 0 | 80 | 7.5% | ACE4、BYP2、PRI4、SBX6 |
| `Shortcuts` | 5 | 0 | 0 | 129 | 3.9% | BYP5、SBX5 |
| `AppleDouble` | 4 | 0 | 0 | 9 | 44.4% | ACE4、DOS4 |
| `ColorSync` | 4 | 0 | 0 | 4 | 100.0% | ACE3、DIS1、DOS4 |
| `file` | 4 | 0 | 0 | 7 | 57.1% | DIS4、DOS4 |
| `AppleMobileFileIntegrity` | 3 | 0 | 0 | 146 | 2.1% | BYP3、PRI3 |
| `AppleVA` | 3 | 0 | 0 | 7 | 42.9% | ACE1、DIS2、DOS3 |
| `Audio` | 3 | 0 | 0 | 95 | 3.2% | ACE3、PRI3、SBX3 |
| `CoreDrag` | 3 | 0 | 0 | 3 | 100.0% | DIS3、DOS3 |
| `IOMobileFrameBuffer` | 3 | 0 | 0 | 22 | 13.6% | ACE3、DOS3 |
| `Kernel` | 3 | 0 | 0 | 681 | 0.4% | ACE3、DOS3 |
| `LaunchServices` | 3 | 0 | 0 | 58 | 5.2% | ACE3、PRI3、SBX3 |
| `Model I/O` | 3 | 0 | 0 | 88 | 3.4% | DIS3、DOS3 |
| `Ruby` | 3 | 0 | 0 | 6 | 50.0% | DIS3、DOS3 |
| `Wi‑Fi` | 3 | 0 | 0 | 3 | 100.0% | ACE3、PRI3、SBX3 |
| `Finder` | 2 | 0 | 0 | 10 | 20.0% | ACE2、PRI2、SBX2 |
| `IOGraphics` | 2 | 0 | 0 | 2 | 100.0% | DOS2、MEM2 |
| `PrintCenter` | 2 | 0 | 0 | 2 | 100.0% | ACE2、PRI2、SBX2 |
| `QuickLook` | 2 | 0 | 0 | 4 | 50.0% | DIS2、DOS2 |
| `Spotlight` | 2 | 0 | 0 | 69 | 2.9% | DIS2、DOS2 |
| `exFAT` | 2 | 0 | 0 | 7 | 28.6% | DIS2、DOS2 |
| `GarageBand` | 1 | 0 | 0 | 2 | 50.0% | ACE1、DOS1 |
| `Pages` | 1 | 0 | 0 | 1 | 100.0% | DIS1、DOS1 |
| `XPC` | 1 | 0 | 0 | 7 | 14.3% | ACE1、PRI1、SBX1 |
| **合计** | 185 | 0 | 23 | 2883 | 6.4% | 35 名 |

# 附表 D CVE 编号普查：段内、页内与单编号覆盖三段分箱（36 行）

| 块 | 分箱 | 计数 | 说明 |
|---|---|---:|---|
| 段内 distinct CVE 数 | 0 | 1706 | 全树 9,136 段 |
| 段内 distinct CVE 数 | 1 | 6952 | 全树 9,136 段 |
| 段内 distinct CVE 数 | 2 | 339 | 全树 9,136 段 |
| 段内 distinct CVE 数 | 3 | 84 | 全树 9,136 段 |
| 段内 distinct CVE 数 | 4 | 20 | 全树 9,136 段 |
| 段内 distinct CVE 数 | 5 | 16 | 全树 9,136 段 |
| 段内 distinct CVE 数 | ≥6 | 19 | 全树 9,136 段 |
| 段内 distinct CVE 数 | 合计 | 9136 | 校验=9,136 |
| 页内 distinct CVE 数 | 1 | 56 | 257 页中含编号页 257 张 |
| 页内 distinct CVE 数 | 2 | 18 | 257 页中含编号页 257 张 |
| 页内 distinct CVE 数 | 3 | 3 | 257 页中含编号页 257 张 |
| 页内 distinct CVE 数 | 4 | 1 | 257 页中含编号页 257 张 |
| 页内 distinct CVE 数 | 5 | 5 | 257 页中含编号页 257 张 |
| 页内 distinct CVE 数 | 6-9 | 10 | 257 页中含编号页 257 张 |
| 页内 distinct CVE 数 | 10-19 | 25 | 257 页中含编号页 257 张 |
| 页内 distinct CVE 数 | 20-49 | 90 | 257 页中含编号页 257 张 |
| 页内 distinct CVE 数 | 50-99 | 31 | 257 页中含编号页 257 张 |
| 页内 distinct CVE 数 | 100+ | 18 | 257 页中含编号页 257 张 |
| 页内 distinct CVE 数 | 0（无编号页） | 0 | 页面上完全没有编号 |
| 页内 distinct CVE 数 | 合计 | 257 | 校验=257 |
| 单个 CVE 覆盖页数 | 1 | 425 | distinct 编号共 2108 个 |
| 单个 CVE 覆盖页数 | 2 | 194 | distinct 编号共 2108 个 |
| 单个 CVE 覆盖页数 | 3 | 575 | distinct 编号共 2108 个 |
| 单个 CVE 覆盖页数 | 4 | 176 | distinct 编号共 2108 个 |
| 单个 CVE 覆盖页数 | 5 | 175 | distinct 编号共 2108 个 |
| 单个 CVE 覆盖页数 | 6 | 165 | distinct 编号共 2108 个 |
| 单个 CVE 覆盖页数 | 7 | 191 | distinct 编号共 2108 个 |
| 单个 CVE 覆盖页数 | 8 | 196 | distinct 编号共 2108 个 |
| 单个 CVE 覆盖页数 | 9 | 2 | distinct 编号共 2108 个 |
| 单个 CVE 覆盖页数 | 10 | 2 | distinct 编号共 2108 个 |
| 单个 CVE 覆盖页数 | 11 | 2 | distinct 编号共 2108 个 |
| 单个 CVE 覆盖页数 | 12 | 3 | distinct 编号共 2108 个 |
| 单个 CVE 覆盖页数 | 13 | 1 | distinct 编号共 2108 个 |
| 单个 CVE 覆盖页数 | 15 | 1 | distinct 编号共 2108 个 |
| 字段落位 | 仅致谢字段 | 2106 | 编号主要写在 Acknowledgements |
| 字段落位 | 致谢＋Impact | 2 | 即 CVE-2025-14174／43529 交叉引用 |

# 附表 E 中文手册层七组对应词（92 行）

| 组 | 中文词 | 出现次数 | 覆盖页 | 覆盖页占比 |
|---|---|---:|---:|---:|
| 组合动词 | 组合 | 14 | 10 | 5.6% |
| 组合动词 | 拼接 | 0 | 0 | 0.0% |
| 组合动词 | 串联 | 2 | 2 | 1.1% |
| 组合动词 | 串接 | 0 | 0 | 0.0% |
| 组合动词 | 捆绑 | 8 | 6 | 3.4% |
| 组合动词 | 一并 | 1 | 1 | 0.6% |
| 组合动词 | 一起 | 18 | 15 | 8.4% |
| 组合动词 | 同时 | 69 | 48 | 27.0% |
| 组合动词 | 配合 | 21 | 17 | 9.6% |
| 组合动词 | 协同 | 3 | 3 | 1.7% |
| 组合动词 | 合用 | 0 | 0 | 0.0% |
| 组合动词 | 相互 | 11 | 8 | 4.5% |
| 链与路径 | 链 | 73 | 40 | 22.5% |
| 链与路径 | 利用链 | 0 | 0 | 0.0% |
| 链与路径 | 漏洞链 | 0 | 0 | 0.0% |
| 链与路径 | 攻击链 | 0 | 0 | 0.0% |
| 链与路径 | 链条 | 0 | 0 | 0.0% |
| 链与路径 | 链路 | 1 | 1 | 0.6% |
| 链与路径 | 路径 | 4 | 3 | 1.7% |
| 链与路径 | 阶段 | 12 | 10 | 5.6% |
| 链与路径 | 步骤 | 16 | 11 | 6.2% |
| 链与路径 | 序列 | 15 | 10 | 5.6% |
| 链与路径 | 依次 | 1 | 1 | 0.6% |
| 链与路径 | 逐步 | 7 | 2 | 1.1% |
| 多个量词 | 多个 | 25 | 21 | 11.8% |
| 多个量词 | 数个 | 1 | 1 | 0.6% |
| 多个量词 | 若干 | 3 | 3 | 1.7% |
| 多个量词 | 两个 | 20 | 18 | 10.1% |
| 多个量词 | 三种 | 5 | 4 | 2.2% |
| 多个量词 | 各类 | 3 | 3 | 1.7% |
| 多个量词 | 多种 | 9 | 9 | 5.1% |
| 多个量词 | 各项 | 1 | 1 | 0.6% |
| 多个量词 | 全部 | 4 | 4 | 2.2% |
| 多个量词 | 所有 | 525 | 178 | 100.0% |
| 多个量词 | 多个漏洞 | 0 | 0 | 0.0% |
| 多个量词 | 缺陷 | 1 | 1 | 0.6% |
| 多个量词 | 弱点 | 0 | 0 | 0.0% |
| 多个量词 | 问题 | 17 | 11 | 6.2% |
| 多个量词 | 漏洞 | 20 | 12 | 6.7% |
| 并列连词 | 以及 | 182 | 90 | 50.6% |
| 并列连词 | 和 | 6204 | 178 | 100.0% |
| 并列连词 | 与 | 1103 | 178 | 100.0% |
| 并列连词 | 及 | 269 | 113 | 63.5% |
| 并列连词 | 并 | 507 | 140 | 78.7% |
| 并列连词 | 并且 | 55 | 43 | 24.2% |
| 并列连词 | 而且 | 17 | 14 | 7.9% |
| 并列连词 | 或 | 1307 | 178 | 100.0% |
| 并列连词 | 或者 | 79 | 53 | 29.8% |
| 放大比较 | 更大 | 3 | 3 | 1.7% |
| 放大比较 | 更严重 | 0 | 0 | 0.0% |
| 放大比较 | 进一步 | 30 | 25 | 14.0% |
| 放大比较 | 加深 | 0 | 0 | 0.0% |
| 放大比较 | 扩大 | 3 | 2 | 1.1% |
| 放大比较 | 增强 | 21 | 19 | 10.7% |
| 放大比较 | 提升 | 7 | 6 | 3.4% |
| 放大比较 | 完整 | 494 | 178 | 100.0% |
| 放大比较 | 完全 | 35 | 28 | 15.7% |
| 放大比较 | 彻底 | 1 | 1 | 0.6% |
| 放大比较 | 任意 | 9 | 8 | 4.5% |
| 放大比较 | 严重 | 1 | 1 | 0.6% |
| 放大比较 | 危害 | 1 | 1 | 0.6% |
| 放大比较 | 后果 | 0 | 0 | 0.0% |
| 放大比较 | 影响 | 23 | 18 | 10.1% |
| 弱点与防护 | 边界 | 5 | 5 | 2.8% |
| 弱点与防护 | 校验 | 19 | 3 | 1.7% |
| 弱点与防护 | 验证 | 433 | 178 | 100.0% |
| 弱点与防护 | 限制 | 479 | 178 | 100.0% |
| 弱点与防护 | 权限 | 284 | 178 | 100.0% |
| 弱点与防护 | 授权 | 353 | 178 | 100.0% |
| 弱点与防护 | 认证 | 380 | 93 | 52.2% |
| 弱点与防护 | 沙盒 | 27 | 11 | 6.2% |
| 弱点与防护 | 沙箱 | 0 | 0 | 0.0% |
| 弱点与防护 | 越狱 | 0 | 0 | 0.0% |
| 弱点与防护 | 提权 | 0 | 0 | 0.0% |
| 弱点与防护 | 绕过 | 23 | 9 | 5.1% |
| 弱点与防护 | 篡改 | 19 | 15 | 8.4% |
| 后果动词 | 代码执行 | 3 | 2 | 1.1% |
| 后果动词 | 内存 | 513 | 178 | 100.0% |
| 后果动词 | 拒绝服务 | 1 | 1 | 0.6% |
| 后果动词 | 终止 | 13 | 7 | 3.9% |
| 后果动词 | 崩溃 | 6 | 4 | 2.2% |
| 后果动词 | 泄露 | 30 | 11 | 6.2% |
| 后果动词 | 读取 | 59 | 22 | 12.4% |
| 后果动词 | 写入 | 46 | 25 | 14.0% |
| 后果动词 | 执行 | 427 | 178 | 100.0% |
| 后果动词 | 利用 | 43 | 29 | 16.3% |
| 后果动词 | 利用难度 | 0 | 0 | 0.0% |
| 后果动词 | 利用方式 | 0 | 0 | 0.0% |
| 后果动词 | 攻击方式 | 0 | 0 | 0.0% |
| 后果动词 | 组合利用 | 0 | 0 | 0.0% |
| 后果动词 | 一并利用 | 0 | 0 | 0.0% |
| **合计** | 91 词（全零 20 词） | — | — | — |

---

**规则：** 本篇全部计数按 #111 第 1 章的两版式域切块口径执行，判据只在 **Impact** 字段匹配（附表 A 的"三字段并集段"列除外，该列同时读 Description 与致谢），词界优先，空格类字符统一按 `$S` 归一；同一段命中多个后果类时**计段不计次**；配对合计 221 与签名合计 185 分列报出，不互换；CVE 编号的"栏"按页＋组件名＋致谢前 40 字归并。**所有比率的分母一律写明是 9,136（全树段）还是 7,434（含 Impact 段）还是 185（M2 段），三套分母不同时出现在同一个百分数上。**

**边界声明：** 本篇是**公开英文措辞的文本计量**，不是脆弱性评估、不是攻击可行性研究、也不提供任何利用方法。文中"组合"指**同一段后果文本里多个后果短语的共现**，"多个弱点"指**同一条目内多个 CVE 编号的共列**，两者都**不推断**真实产品中缺陷是否被一起利用；185 段、23 句、7 种签名、459 栏等数字**只描述文本分布**，不构成任何攻击路径存在、可达或影响放大的结论，也不构成对任何组件安全性的评级。

Telegram: https://t.me/one00190
