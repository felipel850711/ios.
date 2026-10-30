@@TITLE@@iOS 规避检测与安全控制类行为的公开措辞口径分析
@@SUBTITLE@@选题是"试图绕过检测或安全控制的行为"这一类措辞。逐词测量的结果是：**这套语料里不存在"检测"词**——`detect*`、`evad*`、`audit*`、`alert*`、`security control` 五个直觉判据在 9,136 段的三个字段里**出现段数全为 0**，`attempt*`（试图）只有 **8 段**且与本篇主集**交 0 段**；公开文本真正用来表达"检测与安全控制"的是 **`check*`（检查）与 `validat*`（校验）两个词族**，其并集 **CK ＝ 3,345 段／241 页／273 名**，占全树 **36.61%**，是措辞层七篇里规模最大的一个。第二个发现是**字段落位与直觉完全相反**：Impact 侧只有 **76** 段、Description 侧 **3,309** 段、致谢侧 **3** 段，**98.9% 的检查／校验词出现在"修复句"而不是"影响句"**，三组拆分 **仅 I 36／仅 D 3,269／I∩D 40**。第三个发现是**"校验"只有名词形**：`validat*` 1,341 段全部是 `validation`，`validate`／`validated` 两个动词形**全零**；而 `check*` 有 `checks` **1,288** 段与 `checking` **724** 段两种派生形，裸形 `check` 只 **10** 段；两个词族**在同一字段内共现为 0 段**，所以并集 3,347 是三字段口径下的两族相加减 **11** 段（同一跨字段条目）。第四个发现是 **Impact 侧那 76 段几乎全是"检查被绕过"**：43 段是 `may bypass Gatekeeper checks`、21 段是 `An input validation issue was addressed`，即"检查"这一词族一旦进入影响句，配的几乎总是 bypass（与绕过集 BP 交 **154** 段）。
@@INFO@@报告日期|2026 年 10 月 30 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（258 份文件排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 9,132 段含致谢**。检查集 CK ＝ `check*` 或 `validat*`（双向词界、大小写不敏感）出现在 Impact 或 Description **＝ 3,345 段 / 241 页 / 273 名**，其中 Impact 侧 76 段、Description 侧 3,309 段、致谢侧 3 段；若把致谢也纳入并集则为 **3,347** 段（比二字段主集多 **2** 段，即两段"只在致谢里出现检查词"的条目）。字段组合 **仅 I 36／仅 D 3,269／I∩D 40**。对照集：**R** ＝ #118 的远程触达集 **843 段 / 178 页 / 72 名**；**WO** ＝ #119 的无前提集 **178 段 / 97 页 / 37 名**；**E** ＝ #114 的在野集 **108 段 / 76 页 / 12 名**；**G／HI** ＝ 动名词开头集 **2,179 段** 与交互触发集 **273 段**；**M2** ＝ 同段 ≥2 后果类集 **185 段**；**UU** ＝ Impact 含 `user` **1,627 段**；**RC** ＝ #122 竞态集 **232 段**；**L** ＝ #123 逻辑集 **550 段**；**Ds** ＝ #124 拒绝服务集 **470 段**；**X** ＝ #125 泄露集 **427 段**；**BP** ＝ #126 绕过集 **301 段**。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或利用链构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**"检查／校验这一组安全控制词在公开英文文本的哪个字段、哪一句位、以哪一种词形出现，与被检查宾语、与检测类缺席词、与修复套话、与远程／在野标注之间的集合关系**，属于措辞与文本计量分析；文中任何"规避检测或安全控制"表述均指**公开条目使用了 check／validation 这一控制词或把某项检查自述为被绕过这一文本事实**，不是对某项控制是否真的可被规避、检测能力是否存在、绕过难度多大、前提是否成立的判断，也不构成任何机制安全性的结论
@@INFO@@分类|判据**六组 91 项 / 并集全零 10 项 / 非零 81 项**（附表 A，六组：本体词 9、检查与校验对象 25、机制名与检测词对照 22、修复套话句 15、外部集对照 12、后果强度八类 8）/ **九十五条含检查词整句 × 桶 × 句次 × I∶D × 页 × ∩R ∩E ∩M2 ∩L ∩RC**（附表 B，96 行含合计，句次合计 **3,393 ＝ Impact 76 ＋ Description 3,317**、页列合计 2,009）/ **二十三个检查对象桶 × 句次 × 段页名 × I∶D × ∩R ∩E，续表六把句式形态刀**（附表 C，24 行含合计，桶句次闭合 **3,393**；续表打头形态 **1,074／507／1,748／5／8／51** 闭合 3,393）/ **页面、编号、致谢、版式与字段落位五组分箱**（附表 D，30 行，页分箱闭合 241、编号分箱闭合 3,345、致谢分箱闭合 3,344）/ **中文层 91 词**（附表 E，八组：检查与校验本体 10、修复与加固套话 10、被校验对象 16、规避与控制词 11、检测与告警词 9、施事与载体 12、类型与后果 12、范围与程度 11；**全零 26 词、非零 65 词**）/ **组件层 273 名**（附表 F，273 行，段合计 3,345、这 273 名的 Impact 全集 **6,726** 段、占全树含 Impact 段的 **90.5%**）/ **八把刀**：字段刀（Impact 76 ∶ Description 3,309 ∶ 致谢 3，三组互斥拆分 36／3,269／40）、词形刀（`validate`／`validated` 动词形 **0**，`validation` 1,341 全部为名词；`check` 裸形仅 10 段）、共现刀（`check*` 与 `validat*` 在同一字段共现 **0** 段）、对象刀（25 把句级刀命中 23 桶、句级闭合 **3,393**）、缺席刀（`detect*`／`evad*`／`audit*`／`alert*`／`security control`／`TCC`／`SIP` 七项全零，`attempt*` 8 段零共段）、修复套话刀（`improved checks` 泛称 **1,002** 句居首）、机制名配比刀（`Gatekeeper` 53 段里 **50** 段属本篇、`quarantine` 31 段里 **27** 段、`code signing` 短语 5 段全部）、Impact 侧刀（76 段里 **43** 段是 `Gatekeeper checks` 被绕过句、**21** 段是 `input validation` 名词化句）
@@INFO@@难度|进阶（本篇的 3,345 段有**三套分母**：占全树 9,136 段是 **36.61%**、占含 Impact 的 7,434 段是 **45.00%**、占其所在 241 页全部 9,119 段是 **36.68%**，三个数都真、不可混用；第四套是**版式域**分母——域 A **3,316／9,045 ＝ 36.66%**、域 B **29／91 ＝ 31.87%**，域 B 反而**低于**域 A，与 #125 的三倍效应方向相反；**3,345 是两字段主集、3,347 是三字段并集**，写"∩主集"列时一律取三字段口径，所以附表 A 的 `check* ∪ validat*` 行给出 3,347 而正文用 3,345；**76／3,309／3,393／3,317 是四个不同口径**：段级 Impact、段级 Description、句级合计、Description 句级，凡"段数与句数不同"处必须说明是多句段还是致谢段；附表 B、C、F 的页列与名列是逐行去重后的再求和，**不得当作并集去重值**；`DOS` 类在附表 A 的并集列是 **2,014**、∩CK 是 **1,181**，这是判据口径差而非矛盾；附表 A「后果强度八类」∩主集列是三字段并集口径，正文第 12.1 章另给该八类的 Impact 集合口径，两套数各自闭合、不可混抄；`∩主集` 列按三字段口径，所以附表 A 的 `check* ∪ validat*` 行 ∩R 是 **427**、第 11.1 章正文是 **425**；凡"两个集得数相同"处必须同时给出 |A|、|B|、|A∩B|（第 4、9、11 章））
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

**"试图绕过检测或安全控制"是威胁建模里的常见说法，但它对应的英文公开措辞到底是什么词，是可以逐词测的。** 本篇把这句选题拆成四个互相独立的断言，逐个测量：

**断言一（语料里存在"检测"这一类措辞）**：直觉判据应是 `detect`／`evasion`／`audit`／`alert`／`security control`。**测法**：五个词族各做双向词界、三字段扫描。**结果**：**不成立，且是彻底的不成立**——五个判据在 9,136 段的三个字段里**出现段数全为 0**（附表 A「机制名与检测词对照」组）；连 `TCC`、`SIP` 两个控制缩写也是 **0** 段。最接近"试图"的 `attempt*` 只有 **8** 段，且与本篇主集**交 0 段**，也就是说语料里连"an attacker may attempt to…"这种句式都不写。

**断言二（这类行为由"检查／校验"词族承载）**：**测法**：改用 `check*` ∪ `validat*` 作主刀。**结果**：**成立且规模很大**——CK ＝ **3,345 段／241 页／273 名**，占全树 **36.61%**，页覆盖 **241／257 ＝ 93.77%**，是本系列迄今最大的集（#126 的 BP 是 301 段、#125 的 X 是 427 段）。

**断言三（检查词主要出现在影响句，因为影响就是"检查被绕过"）**：**测法**：分字段计数并做三组互斥拆分。**结果**：**方向相反**：Impact 侧 **76** 段、Description 侧 **3,309** 段，**98.9% 在修复句**。三组拆分 **仅 I 36／仅 D 3,269／I∩D 40**（36＋3,269＋40 ＝ 3,345）。但断言三在 Impact 那 76 段的**内部**是对的：43 段是 `may bypass Gatekeeper checks`、21 段是 `An input validation issue was addressed`（第 7 章）。

**断言四（检查与校验是同义词，可互换计数）**：**测法**：逐词形计数并测同字段共现。**结果**：**不成立**：`validat*` 的 **1,341** 段**全部**是名词 `validation`，动词形 `validate`／`validated` **0** 段；`check*` 则有 `checks` **1,288**、`checking` **724** 与裸形 `check` **10** 三种分布；两族在同一字段**共现 0 段**。因此"检查"与"校验"不是同义互换，而是**分别绑定在不同句子槽位上的两套固定句式**（第 4、8 章）。

**所以本篇的产出是一条"检测词全零、控制词以检查／校验承载、98.9% 落在修复句、校验只有名词形、Impact 侧几乎只写被绕过的检查"的结论**（第 18 章纠正会把 3,345 段读成"3,345 次检测被规避"的八种误读）。

# 1 操作性定义与分母

**1.1 语料与切块。** 与 #114–#126 同一镜像、同一切块口径：块起于 `<h3 class="gb-header">`（域 A）或粗体段（域 B），块止于**四锚点**（下一块起点、页脚免责声明、版权行、脚本行）。全树 **9,136 段 / 257 页 / 472 名**；含 Impact **7,434** 段；含致谢 **9,132** 段。域 A **9,045 段 / 246 页 / 470 名**；域 B **91 段 / 11 页 / 26 名**（页为 `118479`、`120303`–`120305`、`120307`–`120312`、`120339`，**不含 `120306`**）。本篇 3,345 段里有 **29** 段落在域 B。

**1.2 CK 集（本篇主集）。** 判据：`(?<![\w-])(?:check|validat)\w*(?![\w-])`，出现在 Impact 或 Description；大小写不敏感。**CK ＝ 3,345 段 / 241 页 / 273 名**。字段落位 **Impact 76 ∶ Description 3,309 ∶ 致谢 3**；三组互斥 **仅 I 36、仅 D 3,269、I∩D 40**；把致谢计入并集得 **3,347** 段，比主集多 **2** 段——这 2 段是**只在致谢栏出现检查词**的条目，正文一律不计入主集（第 3 章）。

**1.3 三套分母与第四套版式分母。** 3,345／9,136 ＝ **36.61%**；3,345／7,434 ＝ **45.00%**；3,345／9,119 ＝ **36.68%**（9,119 是这 241 页的全部段数，平均每页 37.84 段、本篇每页 13.88 段）。版式域：域 A **3,316／9,045 ＝ 36.66%**、域 B **29／91 ＝ 31.87%**——**域 B 略低**，这与 #125 的"域 B 三倍于域 A"方向相反，原因是本篇主词在模板化的致谢文本里根本不出现（致谢侧只有 3 段）。

**1.4 与措辞层前六篇的关系。** #122 `race condition`、#123 `logic`、#124 `denial of service`、#125 `disclos*`、#126 `bypass` 都是**类型词**（描述缺陷性质或后果），本篇是**控制词**（描述修复手段）。这带来一个方法学差别：类型词的落位以 Impact 为主（#126 的 291／301 在 Impact），控制词的落位以 Description 为主（3,309／3,345），**把两套词放进同一张表比较密度会得到完全相反的结论**（第 15 章）。

# 2 结论速览（每条都可按第 17 章复算）

**2.1 规模。** CK **3,345 段 / 241 页 / 273 名**；Impact **76**／Desc **3,309**／致谢 **3**；仅 I **36**、仅 D **3,269**、I∩D **40**。页覆盖 **241／257 ＝ 93.77%**，名覆盖 **273／472 ＝ 57.84%**。

**2.2 检测类判据全零。** `detect*` **0**、`evad*` **0**、`audit*` **0**、`alert*` **0**、`security control` **0**、`TCC` **0**、`SIP／System Integrity Protection` **0**；`attempt*` **8** 段（与 CK 交 **0**）、`monitor*` **6** 段（交 **5**）、`circumvent*` **20** 段（交 **14**）、`verif*` **9** 段（交 **8**）、`log／logging／logs` **260** 段（交 **9**，差值来自"研究者单位名与日志类修复句"）。

**2.3 词形。** `check*` **2,017** 段、`validat*` **1,341** 段；`validate`／`validated` 动词形 **0** 段；裸形 `check` **10** 段、`checks` **1,288** 段、`checking` **724** 段、`validation` **1,341** 段；两族同字段共现 **0** 段。

**2.4 对象。** 25 把句级刀实际命中 **23** 桶（从未成为第一命中的两把是 `authorization 校验`——**0** 句命中，与末位兜底刀 `其他检查措辞句`——被前一把 `其余含 validat 句` 全部截走），句级闭合 **3,393**；最大六桶：`improved checks` 泛称 **1,002**、`bounds checking` **788**、`input validation` **676**、`improved validation` 泛称 **282**、`additional validation` **112**、`entitlement 检查` **110**（第 6 章）。

**2.5 Impact 侧只有 76 段，且高度专一。** 这 76 段由 **11** 个 distinct 句子承载：`Gatekeeper checks` 桶 **43** 段（六类句子，全部在 Impact、Description 侧 **0**）、`An input validation issue was addressed` **21** 段、`A path handling issue was addressed with improved validation` **4** 段、`…without an entitlement check` **4** 段、`An app may be able to check the existence of an arbitrary path…` **3** 段、`…bypass sender policy checks…` **1** 段，相加 **76** 闭合。这 43 段与 #126 绕过集**同源**：`may bypass Gatekeeper checks`（第 7、9 章）。

**2.6 机制名配比。** 全树 `Gatekeeper` **53** 段里 **50** 段属本篇（**94.3%**）；`quarantine` **31** 段里 **27** 段（**87.1%**）；`code signing`／`signature*` 短语口径 **5** 段**全部**属本篇；对照之下 `sandbox` **475** 段里 **225** 段（**47.4%**）、`restrict*` **793** 段里 **82** 段（**10.3%**）、`permission*` **580** 段里 **122** 段（**21.0%**）（第 9 章）。

**2.7 外部集。** ∩R 远程 ＝ **425**；∩E 在野 ＝ **70**；∩WO ＝ **63**；∩UU ＝ **563**；∩G 动名词开头 ＝ **1,143**；∩HI ＝ **140**；∩M2 ＝ **106**；∩RC ＝ **86**；∩L ＝ **310**；∩Ds ＝ **284**；∩X ＝ **184**；∩BP ＝ **154**（第 11 章）。

**2.8 后果八类。** `DOS` **1,181**、`DIS` **267**、`SBX` **225**、`ACE` **174**、`MEM` **160**、`BYP` **154**、`PRI` **82**、`FULL` **26**——八类**全部非零**，这是措辞层七篇里第一次（前六篇至少有一类交 0）。

**2.9 编号与致谢。** 3,345 段里 **3,344** 段带 CVE 编号，distinct 编号 **880** 个、**877** 个为本篇独占、与外集共享 **3** 个；致谢人次 **5,534**、均值 **1.65**、最大 **37**（全树 1.80／37）。

**2.10 组件层。** **273** 个组件名覆盖 3,345 段；这 273 名的 Impact 全集 **6,726** 段，占全树含 Impact 段的 **90.5%**，本篇在其内占 **49.7%**。附表 F 有 **111** 行的本篇占比是 **100.0%**（第 13 章）。

**2.11 中文层。** zh-CN 手册 **178** 页可用：`验证` **432** 次／**178** 页、`检查` **66** 次／**30** 页、`检测` **77** 次／**35** 页、`校验` **19** 次／**3** 页、`核查` **1** 次／1 页、`审查` **13** 次／4 页；`巡检`、`检查项`、`检查机制`、`校对`、`改进的检查`、`额外的检查`、`加固`、`符号链接`、`环境变量`、`调用方`、`逃逸`、`突破`、`越过`、`安全控制`、`审计`、`告警`、`检测能力`、`威胁检测`、`远程攻击者`、`ZIP`、`JAR`、`整数溢出`、`内存破坏`、`竞态`、`完全控制`、`提权` **26** 个词位**全零**（第 14 章）。

# 3 字段刀：Impact 76 ∶ Description 3,309 ∶ 致谢 3

**3.1 三字段落位。** `check*∪validat*` 命中 Impact **76** 段、Description **3,309** 段、致谢 **3** 段。**98.9% 的落位在 Description**——这与 #126 的 `bypass`（96.7% 在 Impact）正好是镜像。原因很简单：检查／校验是**修复动作的宾语**，公开文本只在"这个问题已通过更好的检查来解决"这类句子里使用它；影响句描述的是后果，不描述控制。

**3.2 三组互斥拆分。** 仅 Impact **36**、仅 Description **3,269**、Impact∩Description **40**。36＋3,269＋40 ＝ **3,345**，与主集闭合。I∩D 的 40 段是"影响句写了检查、修复句又写了检查"的同一条目，例如 Impact `An app may be able to retrieve Safari bookmarks without an entitlement check`（**4** 段）配 Description 的 entitlement 校验句。

**3.3 致谢侧的 3 段是什么。** 致谢字段是研究者名单，出现检查词的唯一方式是**单位名或 ID 里含 check**；这 3 段中 **1** 段同时也在 Impact 或 Description 命中，另 **2** 段是**纯致谢命中**。这就是三字段并集 **3,347** 与主集 **3,345** 的差；本篇把它列进口径而不静默丢弃，因为 #125 已经证明"致谢栏的单位名会污染词族计数"（该篇的 `AirPlay` 类污染是 43∶42）。

**3.4 得数相同不等于同一组。** 附表 A 的 `check* ∪ validat*（本篇主刀）` 行给出并集 **3,347**、∩本篇 **3,347**，这是恒等式；而 `check 单词界（无派生形）` 行给出 **10** 段、∩本篇 **8**，说明 10 段里有 **2** 段的 check 只出现在致谢。凡跨集比较（第 9、11 章）都必须同时给 |A|、|B|、|A∩B|。

# 4 词形刀：校验只有名词形，检查以派生形为主

**4.1 `validat*` 的词形分布。** `validat*` 三字段命中 **1,341** 段，其中名词 `validation` **1,341** 段；动词形 `validate`／`validated` 的单词界命中是 **0** 段。也就是说这套语料里**"校验"永远以名词身份出现**（`input validation`、`path validation`、`library validation`），或作为 `validation issue` 这样的名词化主语，从不写成 `the system validates X`。

**4.2 `check*` 的词形分布。** `checks` **1,288** 段、`checking` **724** 段、裸形 `check` **10** 段，词族合计 **2,017** 段。裸形 10 段里 **8** 段属于本篇主集（另 2 段只在致谢）。`checking` 集中于 `bounds checking`（**788** 句）；`checks` 集中于 `improved checks`（**1,002** 句）与 `Gatekeeper checks`（**43** 句）。

**4.3 两族在同一字段共现 0 段。** 用同段双 lookahead 判据测得 **0**：任何一个字段（一段的 Impact 文本或 Description 文本）都不会同时含 `check` 与 `validat`。但两族可以在**同一条目的两个不同字段**各自出现，这样的条目有 **11** 段（2,017＋1,341 ＝ 3,358，而三字段并集 3,347，差 11）。结论：**"检查"与"校验"在语料里是两套互斥句式，不是同义替换**；把两族当成一个词来"归一化"会让 11 个条目的字段结构被抹平。

# 5 缺席刀：检测类判据七项全零

**5.1 七项全零。** `detect*`、`evad*`、`audit*`、`alert*`、`security control`、`TCC`、`SIP／System Integrity Protection` 在 9,136 段三字段的出现段数**都是 0**。这七项覆盖了"检测／审计／告警"三个直觉措辞面，也覆盖了两个常被引用的控制缩写。

**5.2 页面 HTML 里有，字段文本里没有。** 需要防止的误判是"我在页面上看到过 detect"：`_tmp/adv2` 下 258 份 HTML 里 `detect` 作为**子串**出现 **262** 次、分布在 **258** 个文件；其中带词界的 `detect*` 词形只有 **4** 次。这 4 次没有一次进入任何条目的 Impact／Description／致谢文本，那 262 次绝大多数位于模板的类名、脚本与属性里。同一现象在 `color` 上更极端：HTML 子串 **25,558** 次，而词界形 `color*` 在字段文本里命中 **0 段／0 页**。**所以任何"检测"类结论都必须先声明扫描的是字段文本而不是页面源码，并声明用的是子串还是词界**——这是本篇对措辞层方法学的第二点补充。

**5.3 有词但几乎不共段。** `attempt*` 8 段、∩CK **0**；`monitor*` 6 段、∩CK 5；`circumvent*` 20 段、∩CK 14；`verif*` 9 段、∩CK 8。`attempt` 那 8 段是"尝试"义（如 `an attacker may attempt to…`），与检查词同段一次都没出现；`circumvent` 的 20 段是措辞层里最接近"规避"的词，其中 14 段与检查词同条目，另外 6 段规避的对象是策略与限制而非检查。

# 6 对象刀：25 把句级刀命中 23 桶，句级闭合 3,393

**6.1 刀法。** 对 3,393 句逐一按 25 把刀做**优先序唯一命中**归桶；未归类 **0** 句；因末两把是兜底刀（其余含 check 句、其余含 validat 句），多重命中 **95** 句不作为缺陷计数。命中 **23** 桶意味着 2 把刀从未成为第一命中：`authorization 校验`（`authoriz* validation`）**0** 句命中，`其他检查措辞句`（末位全匹配兜底）被前一把 `其余含 validat 句` 全部截走。另外 `integrity checks` 只有 **5** 句、`library validation` 只有 **2** 句，量级最小但确实各自成桶。

**6.2 六个量级。** 千级：**`improved checks` 泛称 1,002**；百级：`bounds checking` **788**、`input validation` **676**、`improved validation` **282**、`additional validation` **112**、`entitlement` **110**、`path validation` **94**；十级：`symlink` **66**、`Gatekeeper checks` **43**、`permissions` **35**、`additional checks` **34**、`validation issue` 名词化 **31**、兜底 validat **29**、`input sanitization` **23**、`prevent unauthorized actions` **16**、`certificate` **11**、环境变量 **10**；个级：`lock state` **8**、兜底 check **8**、`integrity` **5**、`URL` **4**、`caller` **4**、`library` **2**。

**6.3 泛称与具名的比例。** 把四个泛称桶（improved checks 1,002、improved validation 282、additional validation 112、additional checks 34）相加得 **1,430** 句，占 3,393 的 **42.1%**；具名对象桶（bounds **788**／input **676**／path **94**／symlink **66**／entitlement **110**／Gatekeeper **43**／permissions **35**／certificate **11**／URL **4**／lock state **8**／integrity **5**／library **2**／caller **4**／环境变量 **10**／sanitization **23**／prevent unauthorized **16**）相加 **1,895** 句，占 **55.9%**；余 **68** 句（**2.0%**）落在两个兜底桶与 `validation issue` 名词化桶。**结论：公开措辞有约四成只说"改了检查"，不说改了哪项检查。**

**6.4 每桶的影响句占比。** Impact 句次只出现在四个桶：`Gatekeeper checks` **43**、`input validation` **21**、兜底 check **8**、`improved validation` **4**，合计 **76**。其余 **19** 桶的 Impact 句次**全为 0**——即绝大多数被检查对象（bounds、path、symlink、entitlement、permissions、certificate…）**只存在于修复句**，公开文本从不写"某项边界检查被绕过"。

# 7 Impact 侧 76 段：全是"检查被绕过"或"检查缺失"

**7.1 十一句承载 76 段。** Impact 侧命中的 distinct 句子只有 **11** 条，按句次排：**`An input validation issue was addressed` 21**、`An app may bypass Gatekeeper checks` **19**、`A maliciously crafted ZIP archive may bypass Gatekeeper checks` **10**、`A maliciously crafted disk image may bypass Gatekeeper checks` **4**、`An app may be able to retrieve Safari bookmarks without an entitlement check` **4**、`A path handling issue was addressed with improved validation` **4**、`An app may be able to bypass Gatekeeper checks` **4**、`A malicious JAR file may bypass Gatekeeper checks` **3**、`A malicious application may bypass Gatekeeper checks` **3**、`An app may be able to check the existence of an arbitrary path on the file system` **3**、`A remote attacker may be able to bypass sender policy checks and deliver malicious content via email` **1**。合计 **76**，与 Impact 段数闭合（这 11 句每句各段都不重叠，所以句次＝段次）。其中六条 Gatekeeper 句相加 **43**，与附表 C 的 `Gatekeeper checks` 桶一致；`An app may be able to check the existence…` 与 `without an entitlement check`、`sender policy checks` 三条相加 **8**，即附表 C 的兜底 check 桶；名词化的 `An input validation issue was addressed` **21** 段是"检查缺失"表述，不含绕过。

**7.2 与 #126 同源。** 这 43 段 Gatekeeper 句全部含 `bypass`，因此**必然是绕过集 BP 的子集**：|BP| ＝ 301、|CK| ＝ 3,345、|BP∩CK| ＝ **154**（附表 A ∩主集列，三字段口径）。反向测两个方向都要给：全树含 `Gatekeeper` 的段是 **53** 段（本篇三字段口径与 #126 的 Impact 对象桶口径都得 53，且 #126 报告这 53 段**全部**含 `bypass`），本篇的 `Gatekeeper checks` 桶 **43** 段是它的子集（43／53 ＝ **81.1%**，即 Gatekeeper 被绕过的 53 段里有 43 段把宾语写成"检查"）；而本篇 3,345 段里只有 **4.6%** 含 bypass（154／3,345），所以**"检查"绝不等于"被绕过"**，154 这个数字同时由两集各自的规模决定，不可当作关联强度。

**7.3 `without an entitlement check` 是另一种缺失句式。** **4** 段写 `…without an entitlement check`，语法上属于 #119 的 `without` 无前提集（∩WO ＝ **63** 段），语义上是"控制未生效"而不是"控制被绕过"。这 4 段与 43 段 Gatekeeper 句子**互斥**，说明公开措辞区分"缺失检查"与"绕过检查"两件事。

# 8 修复句形态：六把形态刀闭合 3,393

**8.1 闭合分区。** 附表 C 续表把 3,393 句按打头与修复动词切成**六分箱**：`The／This issue was addressed …` **1,074**、`A／An X issue was addressed …` **507**、`A／An X（非 issue 名词）was addressed …` **1,748**、`Multiple X issues were addressed …` **5**、`X existed` 缺陷存在句 **8**、含 `may` 的影响句 **51**。六项相加 **3,393**，闭合。

**8.2 三档打头。** 名词化打头（第 2、3 箱合计 **2,255** 句，**66.5%**）是主流：句子先给缺陷类型（`An out-of-bounds read`、`A race condition`、`A type confusion issue`），再给修复。代词打头（第 1 箱 **1,074** 句，**31.7%**）是"不告诉你类型"的另一档。两档之比 **2.1∶1**，说明**约三分之一条目把缺陷类型省略掉了**。

**8.3 `X existed` 只有 8 句。** 这是最古老的措辞格式（`A validation issue existed in the entitlement verification.`），在 3,393 句里只剩 **8** 句，且这 8 句里 **3** 个组件名——它属于早期页面，与 `was addressed` 系列并存但不再新增。

**8.4 修复动词只有三个介词。** `was addressed with`／`addressed by`／`addressed through` 三形在**三字段并集**口径下分别是 **6,581**／**371**／**247** 段，其中与本篇同条目的分别 **3,291**／**18**／**10** 段——`with` 占绝对多数，`through` 只有 **10** 段与检查词共现，`by` **18** 段。`addressed through` 在本篇句级只出现 **2** 个 distinct 句、**10** 句次，两句都是 `…was addressed through improved input validation.` 型（第 6 章 input validation 桶）。

# 9 机制名配比刀：两个 90% 以上与三个低配比

**9.1 高配比。** `Gatekeeper` 全树 **53** 段中 **50** 段属本篇（**94.3%**），`quarantine` **31** 段中 **27** 段（**87.1%**），`code signing`／`signature*` 短语口径 **5** 段**全部**（**100%**）。这三项都是"启动前检查"族，公开文本谈它们时几乎必然同时谈检查。

**9.2 低配比。** `restrict*` **793** 段中仅 **82** 段（**10.3%**）、`permission*` **580** 段中 **122** 段（**21.0%**）、`sandbox` **475** 段中 **225** 段（**47.4%**）、`prevent*` **66** 段中 **46** 段（**69.7%**）。可见"限制／权限"这两类控制词在语料里主要不靠 check／validation 表达，而靠 `restrict`、`permission`、`access` 自身的句式表达。

**9.3 反向刀：从修复套话反查。** `improved memory handling` 全树 **862** 段中只有 **4** 段与检查词同条目（**0.5%**）；`improved checks` 泛称桶 **1,002** 句则全部落在本篇。这说明修复套话是**成对互斥**的：一条条目要么写"改了检查"、要么写"改了内存处理"，极少两者都写——与第 4.3 节的两词族零共现一致。

# 10 页、编号与致谢层

**10.1 页分箱。** 241 页含本篇条目，其中每页 **1** 段的有 **58** 页、**2** 段 **15** 页、**3** 段 **8** 页、**4** 段 **7** 页、**5** 段 **7** 页、**6–9** 段 **27** 页、**10–19** 段 **66** 页、**20–49** 段 **43** 页、**50+** 段 **10** 页；合计 241，与页集闭合。Top10 页合计 **789** 段，占 **23.6%**。全篇 241 页的全部段数 **9,119**，即本篇页集覆盖了全树 9,136 段中 **99.81%** 的段——这是"检查词遍布语料"的直接度量。

**10.2 全本篇页 43 页。** 有 **43** 页里**每一段**都被本篇抓到；这 43 页大多是早期单一格式的页面（整页都写 `was addressed with improved checks`）。

**10.3 编号。** **3,344**／3,345 段带 CVE 编号（**0 编号段只有 1 段**）；编号数分箱为 **1 个 3,156**、**2 个 145**、**3 个 22**、**4 个及以上 21**。distinct 编号 **880**，其中 **877** 个只属于本篇、**3** 个与外集共享。**"独占 877"不等于"新编号"**：共享的那 3 个编号所在的段同时被别的类型词抓到，说明检查词与这些后果词写进了同一条目。

**10.4 致谢。** 有致谢的段 **3,344**，人次 **5,534**，均值 **1.65**（全树 1.80），最大 **37** 人。致谢人数分箱：1 人 **2,119**、2 人 **837**、3 人 **197**、4 人 **89**、5 人 **48**、**6–9 人 39**、9 人以上 **15**；七箱相加 **3,344** 与"有致谢段数"闭合。本篇每段致谢人数低于全树均值 **0.15**，与"检查词条目多为常规修复条目"一致。

**10.5 版式域。** 域 B **29** 段／91 段 ＝ **31.87%**；域 A **3,316** 段／9,045 ＝ **36.66%**。**域 B 密度低于域 A**，与 #125 的方向相反，可用作"版式密度差不是必然"的对照样本。

# 11 外部集刀：与七个前篇集都相交

**11.1 逐集相交（三字段并集口径）。** ∩R **425**（附表 A 的 `check* ∪ validat*` 行按三字段 3,347 口径给 **427**，差的 2 段即第 3.3 节那两段纯致谢命中；R 全集 843，即 **50.4%** 的远程条目同时写了检查词）；∩E **70**（E 全集 108，**64.8%**）；∩WO **63**；∩UU **563**（1,627）；∩G **1,143**（2,179，**52.5%**）；∩HI **140**（273）；∩M2 **106**（185，**57.3%**）；∩RC **86**（232）；∩L **310**（550）；∩Ds **284**（470）；∩X **184**（427）；∩BP **154**（301）。

**11.2 覆盖率本身是发现。** 本篇与**每一个**前篇集的交集都占对方集的 **18%–65%**，这是七篇里唯一一个"全覆盖"集——因为它规模大（36.61%）且落在修复句。**所以任何"某类型词与检查词强相关"的说法都必须先减去 36.61% 的基线**：∩E 的 64.8% 只比基线高 1.77 倍，而 #126 与 `Apple is aware of a report` 的交是基线的 **5.9 倍**。

**11.3 在野口径。** ∩E ＝ **70** 段是**两字段主集 × Impact 含 `exploit*`** 的相交，但这 70 段的检查词命中**全在 Description 侧**：**Impact 侧 ∩E ＝ 0**（即没有任何一段同时把"检查"和"exploit"写进影响句）。更强口径 `actively exploited`（三字段 **18** 段，见 #126 第 13 章）与本篇三字段并集交 **5** 段、与本篇 Impact 侧交 **0** 段。也就是说**检查词从来不是被标注在野那类条目的影响句措辞**，它只在那些条目的修复句里出现。

# 12 后果八类刀：八类全部非零

**12.1 八类相交（两套口径）。** 附表 A 的「后果强度八类」∩主集列按**三字段并集口径**给出：`DOS` **1,181**、`DIS` **267**、`SBX` **225**、`ACE` **174**、`BYP` **154**、`MEM` **160**、`PRI` **82**、`FULL` **26**；同一批类若改用**该类的 Impact 判据集合**与本篇主集求交，得数是 `DOS` **1,173**、`DIS` **257**、`SBX` **222**、`ACE` **174**、`BYP` **144**、`MEM` **88**、`PRI` **82**、`FULL` **26**——六组不同、两组相同，差值来自这些后果词只写在 Description 或致谢的那部分段。**两套数不可混抄**。前六篇每篇都至少有一类交 **0**（#126 有四类为 0），本篇**八类全非零**。

**12.2 为什么全非零。** 因为这八类是**后果**，而本篇是**修复手段**；每条条目都要写后果、也常常要写修复手段，所以两者大量同段。`DOS` 最高（1,181／2,014 ＝ **58.6%**）与 `improved bounds checking`、`improved input validation` 两桶对应"越界读取／整数溢出导致拒绝服务"这一固定组合有关。

**12.3 类对分布不可与 #126 类比。** #126 的同段双类只有 2 种（BYP+SBX 13、BYP+PRI 3）；本篇因主集不含后果判据，同段双类的分布由**条目自身后果**决定，与检查词无因果关系，故本篇不测类对表。

# 13 组件层：273 名、90.5% 的 Impact 全集

**13.1 规模。** **273** 个组件名覆盖 3,345 段；这 273 名的 Impact 全集是 **6,726** 段，占全树含 Impact 段（7,434）的 **90.5%**，本篇在其中占 **49.7%**（3,345／6,726）。附表 F 列出全部 273 行，其中 **111** 行的本篇占比为 **100.0%**（即该组件所有含 Impact 的条目都被本篇抓到），**81** 名是该组件全部段都被抓到。

**13.2 前八名。** WebKit **310** 段／该名 Impact 全集 910（**34.1%**）、Kernel **236**／681（**34.7%**）、ImageIO **171**／261（**65.5%**）、SceneKit **89**／112（**79.5%**）、CoreMedia **68**／150、Security **68**／91（**74.7%**）、Model I/O **67**／88（**76.1%**）、CoreAudio **65**／79（**82.3%**）。

**13.3 检查密度最高的组件。** 与 #126 的比较很有意思：`Security` 组件 **91** 段 Impact 里 **68** 段写检查（**74.7%**）、`AirPlay` **77** 段里 **42** 段（**54.5%**，且这 42 段 ∩R ＝ **42**，全部远程可达）、`AppleMobileFileIntegrity` **146** 段里 **54** 段（**37.0%**）。**密度最高的不是解析类，而是"策略与授权"类组件**——它们正是"安全控制"在语料里的落点。

# 14 中文手册层：`验证` 432 次、`检测` 77 次，26 词全零

**14.1 本体词。** `验证` **432** 次／**178** 页（几乎每页都有）、`检查` **66** 次／**30** 页、`检测` **77** 次／**35** 页、`校验` **19** 次／**3** 页、`核查` **1** 次、`审查` **13** 次。中文手册与英文顾问页的分工是：**手册用"验证"覆盖 identity／certificate／signature 三类，顾问页用 validation 只覆盖后两类**。

**14.2 全零的 26 个词位。** `巡检`、`检查项`、`检查机制`、`校对`、`改进的检查`、`额外的检查`、`加固`、`符号链接`、`环境变量`、`净化`、`规避`、`逃逸`、`越界`、`整数溢出`、`类型混淆`、`竞态`、`内存破坏`、`完全控制`、`提权`、`威胁检测`、`审计`、`告警`、`门禁系统`、`安全控制` 等在 178 页可用文本里**一次都没出现**。**所以不能用中文手册的反查来证明英文措辞的存在与否**——两边的词表不同源。

**14.3 反向密集词。** `授权` **352** 次／178 页、`权限` **283** 次／178 页、`改进` **231** 次／178 页、`移除` **104** 次／38 页、`证书` **150** 次／29 页、`输入` **106** 次／55 页。对照英文侧 `permission*` **580** 段与 `entitlement` 桶 **110** 句，中文手册把 entitlement／permission 统一译成"权限"与"授权"两词，**没有对应 `validation` 的固定译法**。

# 15 措辞层七篇对账：类型词与控制词

**15.1 七个集的规模与落位。** #122 `race condition` **232** 段（Impact 0／Desc 232）；#123 `logic` **550** 段（I 10∶D 540）；#124 `denial of service` **470** 段（I 457∶D 50∶致谢 0，两字段重叠 37）；#125 `disclos*` **427** 段（I 343∶D 81∶A 11 模板）；#126 `bypass` **301** 段（I 291∶D 31∶A 0）；本篇 `check∪validat` **3,345** 段（I 76∶D 3,309）。**六篇里五篇以 Impact 为主或以 Description 为主，本篇是 Description 压倒性的唯一一例（98.9%）。**

**15.2 编号独占不是通则。** #126 的 93 个编号**全部独占**，#124 是 457∶50∶0 中有 **1** 个共享、#125 有 **6** 个共享，本篇 **880** 个里 **877** 独占、**3** 共享。**"本篇编号都独占"这一说法在系列里不成立**，必须逐篇测。

**15.3 后果类的零相交模式。** #126 与 `DIS`、`DOS`、`ACE`、`FULL` 四类交 0；本篇与八类**全部非零**。两者相差 3,044 段规模，说明"零相交"更多是**小集的稀疏效应**而非语义排斥；因此后续任何"两语义类互斥"的断言都必须报告两集规模与期望相交数（本篇基线 36.61%）。

# 16 缺口清单与常见误读纠正

**16.1 八种误读。**（一）把 3,345 段读成"3,345 次检测被规避"——其中 **3,269** 段只在修复句出现，一条也没说被规避。（二）把 `check` 与 `validation` 当同义互换（第 4.3 节：同字段共现 0）。（三）用页面 HTML 源码计数代替字段文本计数（第 5.2 节：HTML 子串 262 次对字段文本 0 段）。（四）把 `improved checks` 泛称桶读成具体机制（它 **1,002** 句不含任何对象名）。（五）把 ∩E 70 段读成"检查失效的条目在野"（E 集判据是 Impact 含 `exploit*`，与检查词无关）。（六）把附表 B／C／F 的页列合计当并集去重值（C 页列合计 1,026、B 页列合计 2,009，真实页集是 **241**）。（七）把"致谢侧 3 段"当成模板污染（本篇只有 **2** 段是纯致谢命中，与 #125 的 11 段模板不同）。（八）用中文手册"验证 432 次"反推英文 validation 密度（第 14.2 节：词表不同源）。

**16.2 未回答的三点。**（一）`check` 与 `validat` 的**字段互斥**是否与页面年代有关：本篇只测了共现为 0，未做年代分层。（二）`circumvent*` 20 段的**对象分布**：那 6 段不与检查词同段，其宾语未逐条归类。（三）Impact 侧 76 段与 #126 的 43 段 Gatekeeper 完全同源，但其余 33 段的**后果类别**未做桶级交叉。

**16.3 明确不能得出的结论。** 不能由本篇得出"iOS 没有检测能力"或"检查机制可被绕过"；语料里 `detect` 为 0 只说明**公开响应文本不使用该词**，不能推断产品层有无该功能。同理，`SIP`、`TCC` 两个缩写零出现是**措辞事实**，不是机制事实。

# 17 复算步骤与断言

**17.1 数据层。** `require "./_tmp/tl111load.pl"; my %B = load_blocks();` 得 **9,136** 段键表；`$B{key}` 有 `imp`／`des`／`ack`／`id`（页号）／`nm`（组件名）五栏。

**17.2 判据。** 主刀 `qr/(?<![\w-])(?:check|validat)\w*(?![\w-])/i` 分别过 `IFre`／`DFre`／`AFre` 得 **76**／**3,309**／**3**；`MK` 得三字段并集 **3,347**；主集取 `IFre∪DFre` ＝ **3,345**。`check*` 与 `validat*` 分别用 `qr/(?<![\w-])check\w*(?![\w-])/i`、`qr/(?<![\w-])validat\w*(?![\w-])/i`（**2,017**／**1,341**）；裸形与派生形用 `W('check')`、`W('checks')`、`W('checking')`、`W('validation')`、`W('validated')`（**10**／**1,288**／**724**／**1,341**／**0**）。

**17.3 句级与桶级。** 用 `SE()` 拆句、`D0()` 归一空白，只扫 `imp` 与 `des` 两栏（致谢不进句级，以免单位名混入），得 **3,393** 句、**95** 个 distinct 句；25 把对象刀按优先序首命中得 **23** 桶；六把形态刀首命中闭合 **3,393**。

**17.4 附表。** `_tmp/tw127A.md`（92 行）、`tw127B.md`（96 行）、`tw127C.md`（24 行＋续表 7 行）、`tw127D.md`（30 行）、`tw127E.md`（92 行）、`tw127F.md`（273 行）由 `_tmp/gb127.pl`（= `head -50 _tmp/tw123tab.pl` ＋ `_tmp/s127.pl` ＋ `_tmp/genbody2.pl`）一次产出，锚点断言在运行时无一触发。

---

# 附表 A 检查与校验判据六组电池（92 行）

| 组 | 判据 | 并集段 | 页 | 名 | ∩主集 | ∩R | ∩E | ∩WO |
|---|---|---:|---:|---:|---:|---:|---:|---:|
| 本体词（本篇主刀） | `check* 词族（检查，双向词界）` | 2017 | 227 | 201 | 2015 | 243 | 46 | 48 |
| 本体词（本篇主刀） | `validat* 词族（校验，双向词界）` | 1341 | 179 | 168 | 1341 | 184 | 24 | 15 |
| 本体词（本篇主刀） | `check* ∪ validat*（本篇主刀）` | 3347 | 243 | 273 | 3345 | 427 | 70 | 63 |
| 本体词（本篇主刀） | `check 单词界（无派生形）` | 10 | 7 | 3 | 8 | 3 | 0 | 4 |
| 本体词（本篇主刀） | `checks 复数形` | 1288 | 202 | 166 | 1288 | 156 | 31 | 48 |
| 本体词（本篇主刀） | `checking 动名词形` | 724 | 156 | 68 | 724 | 85 | 15 | 0 |
| 本体词（本篇主刀） | `validation 名词形` | 1341 | 179 | 168 | 1341 | 184 | 24 | 15 |
| 本体词（本篇主刀） | `validated／validate 动词形` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `check∩validat 同段` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `Gatekeeper checks` | 43 | 23 | 14 | 43 | 0 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `bounds checking／checks` | 788 | 163 | 72 | 788 | 81 | 15 | 0 |
| 检查与校验对象（25 把句级刀） | `input validation` | 676 | 151 | 85 | 676 | 146 | 1 | 0 |
| 检查与校验对象（25 把句级刀） | `path validation` | 94 | 50 | 26 | 94 | 3 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `symlink 校验` | 66 | 36 | 19 | 66 | 0 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `entitlement 检查` | 110 | 63 | 32 | 110 | 0 | 0 | 11 |
| 检查与校验对象（25 把句级刀） | `permissions 检查` | 35 | 32 | 11 | 35 | 4 | 0 | 6 |
| 检查与校验对象（25 把句级刀） | `authorization 校验` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `certificate 校验` | 11 | 8 | 1 | 11 | 3 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `library validation` | 2 | 1 | 2 | 2 | 0 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `lock state checking` | 8 | 8 | 1 | 8 | 0 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `integrity checks` | 5 | 4 | 2 | 5 | 4 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `URL validation` | 4 | 4 | 1 | 4 | 0 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `caller 访问校验` | 4 | 4 | 1 | 4 | 0 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `环境变量校验` | 10 | 10 | 4 | 10 | 0 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `input sanitization` | 72 | 37 | 16 | 23 | 3 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `防止未授权动作的检查` | 16 | 16 | 4 | 16 | 0 | 8 | 0 |
| 检查与校验对象（25 把句级刀） | `improved validation（泛称）` | 367 | 101 | 76 | 367 | 13 | 23 | 15 |
| 检查与校验对象（25 把句级刀） | `additional validation（泛称）` | 112 | 65 | 20 | 112 | 8 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `improved checks（泛称）` | 1002 | 188 | 125 | 1002 | 151 | 23 | 31 |
| 检查与校验对象（25 把句级刀） | `additional checks（泛称）` | 34 | 28 | 12 | 34 | 0 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `validation issue 等名词化主语` | 113 | 66 | 24 | 113 | 20 | 0 | 0 |
| 检查与校验对象（25 把句级刀） | `其余含 check 句` | 2018 | 227 | 201 | 2015 | 243 | 46 | 48 |
| 检查与校验对象（25 把句级刀） | `其余含 validat 句` | 1341 | 179 | 168 | 1341 | 184 | 24 | 15 |
| 检查与校验对象（25 把句级刀） | `其他检查措辞句` | 9136 | 257 | 472 | 3345 | 843 | 108 | 178 |
| 机制名与检测词对照 | `detect* 词族（检测）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `evad* 词族（规避）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `circumvent* 词族` | 20 | 19 | 6 | 14 | 0 | 0 | 0 |
| 机制名与检测词对照 | `audit* 词族（审计）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `alert* 词族（告警）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `monitor* 词族（监控）` | 6 | 6 | 4 | 5 | 1 | 0 | 5 |
| 机制名与检测词对照 | `log／logging／logs` | 260 | 123 | 46 | 9 | 0 | 0 | 0 |
| 机制名与检测词对照 | `security control 短语` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `control 词族泛称` | 66 | 38 | 20 | 16 | 8 | 5 | 0 |
| 机制名与检测词对照 | `attempt* 词族（试图）` | 8 | 8 | 2 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `sanitiz*（净化对照）` | 93 | 54 | 21 | 23 | 3 | 0 | 0 |
| 机制名与检测词对照 | `verif* 词族` | 9 | 7 | 4 | 8 | 0 | 0 | 0 |
| 机制名与检测词对照 | `enforce*（强制对照）` | 53 | 34 | 4 | 35 | 0 | 0 | 0 |
| 机制名与检测词对照 | `restrict*（限制对照）` | 793 | 153 | 109 | 82 | 75 | 4 | 33 |
| 机制名与检测词对照 | `prevent* 词族` | 66 | 43 | 7 | 46 | 0 | 8 | 0 |
| 机制名与检测词对照 | `permission* 词族` | 580 | 141 | 99 | 122 | 36 | 0 | 36 |
| 机制名与检测词对照 | `sandbox 词族` | 475 | 120 | 74 | 225 | 40 | 8 | 6 |
| 机制名与检测词对照 | `TCC 缩写` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `SIP／System Integrity Protection` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `Gatekeeper` | 53 | 30 | 16 | 50 | 0 | 0 | 0 |
| 机制名与检测词对照 | `quarantine` | 31 | 28 | 10 | 27 | 4 | 0 | 0 |
| 机制名与检测词对照 | `code signing` | 5 | 5 | 1 | 5 | 0 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `The issue was addressed with improved checks.` | 514 | 138 | 82 | 514 | 68 | 5 | 20 |
| 修复套话句（全篇共用 15 项） | `improved bounds checking` | 699 | 154 | 63 | 699 | 81 | 15 | 0 |
| 修复套话句（全篇共用 15 项） | `improved input validation` | 641 | 151 | 79 | 641 | 140 | 1 | 0 |
| 修复套话句（全篇共用 15 项） | `improved validation` | 367 | 101 | 76 | 367 | 13 | 23 | 15 |
| 修复套话句（全篇共用 15 项） | `additional checks` | 34 | 28 | 12 | 34 | 0 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `additional validation` | 112 | 65 | 20 | 112 | 8 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `improved checks to prevent unauthorized actions` | 16 | 16 | 4 | 16 | 0 | 8 | 0 |
| 修复套话句（全篇共用 15 项） | `addressed with（全形）` | 6589 | 254 | 364 | 3296 | 735 | 104 | 163 |
| 修复套话句（全篇共用 15 项） | `addressed by` | 371 | 136 | 80 | 18 | 31 | 4 | 4 |
| 修复套话句（全篇共用 15 项） | `addressed through` | 247 | 129 | 48 | 10 | 47 | 0 | 11 |
| 修复套话句（全篇共用 15 项） | `validation 词族` | 1341 | 179 | 168 | 1341 | 184 | 24 | 15 |
| 修复套话句（全篇共用 15 项） | `check(s)／checking 词族` | 2017 | 227 | 201 | 2015 | 243 | 46 | 48 |
| 修复套话句（全篇共用 15 项） | `sanitiz* 词族` | 93 | 54 | 21 | 23 | 3 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `enforce* 词族` | 53 | 34 | 4 | 35 | 0 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `restrict* 词族` | 793 | 153 | 109 | 82 | 75 | 4 | 33 |
| 外部集对照（12 套，含 #122–#126） | `远程可达 R` | 843 | 178 | 72 | 425 | 843 | 4 | 27 |
| 外部集对照（12 套，含 #122–#126） | `without 词界集 WO` | 178 | 97 | 37 | 63 | 27 | 0 | 178 |
| 外部集对照（12 套，含 #122–#126） | `exploit* 集 E` | 108 | 76 | 12 | 70 | 4 | 108 | 0 |
| 外部集对照（12 套，含 #122–#126） | `user 词界集 UU` | 1627 | 175 | 200 | 563 | 130 | 0 | 105 |
| 外部集对照（12 套，含 #122–#126） | `动名词开头集 G` | 2179 | 224 | 98 | 1143 | 108 | 53 | 0 |
| 外部集对照（12 套，含 #122–#126） | `非处理/解析类 HI` | 273 | 102 | 35 | 140 | 108 | 0 | 0 |
| 外部集对照（12 套，含 #122–#126） | `≥2 后果类 M2` | 185 | 82 | 35 | 106 | 23 | 0 | 0 |
| 外部集对照（12 套，含 #122–#126） | `race condition 集 RC（#122）` | 232 | 95 | 40 | 86 | 14 | 0 | 3 |
| 外部集对照（12 套，含 #122–#126） | `logic 集 L（#123）` | 550 | 162 | 81 | 310 | 99 | 9 | 24 |
| 外部集对照（12 套，含 #122–#126） | `拒绝服务集 Ds（#124）` | 470 | 155 | 62 | 284 | 164 | 0 | 0 |
| 外部集对照（12 套，含 #122–#126） | `泄露集 X（#125）` | 427 | 141 | 54 | 184 | 11 | 2 | 0 |
| 外部集对照（12 套，含 #122–#126） | `绕过集 BP（#126）` | 301 | 114 | 58 | 154 | 42 | 20 | 0 |
| 后果强度八类 | `ACE 类` | 336 | 120 | 43 | 174 | 36 | 31 | 0 |
| 后果强度八类 | `BYP 类` | 301 | 114 | 58 | 154 | 42 | 20 | 0 |
| 后果强度八类 | `DIS 类` | 603 | 159 | 78 | 267 | 74 | 2 | 0 |
| 后果强度八类 | `DOS 类` | 2014 | 176 | 130 | 1181 | 343 | 0 | 0 |
| 后果强度八类 | `FULL 类` | 63 | 43 | 18 | 26 | 7 | 0 | 0 |
| 后果强度八类 | `MEM 类` | 390 | 127 | 31 | 160 | 3 | 44 | 0 |
| 后果强度八类 | `PRI 类` | 124 | 62 | 30 | 82 | 0 | 8 | 0 |
| 后果强度八类 | `SBX 类` | 475 | 120 | 74 | 225 | 40 | 8 | 6 |
| **合计** | 91 项（并集全零 10 项、非零 81 项） | 3345 | — | — | 3345 | 843 | 108 | 178 |

# 附表 B 九十五条含检查词整句 × 桶 × 句次 × I∶D × 页 × ∩R ∩E ∩M2 ∩L ∩RC（96 行）

| distinct 整句 | 归入桶 | 句次 | Impact 句 | Desc 句 | 页 | ∩R | ∩E | ∩M2 | ∩L | ∩RC |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| The issue was addressed with improved checks. | improved checks（泛称） | 514 | 0 | 514 | 138 | 68 | 5 | 14 | 1 | 0 |
| An out-of-bounds write issue was addressed with improved bounds checking. | bounds checking／checks | 258 | 0 | 258 | 66 | 36 | 10 | 17 | 0 | 0 |
| A logic issue was addressed with improved checks. | improved checks（泛称） | 226 | 0 | 226 | 114 | 54 | 0 | 3 | 226 | 0 |
| An out-of-bounds read was addressed with improved bounds checking. | bounds checking／checks | 169 | 0 | 169 | 71 | 19 | 0 | 6 | 0 | 0 |
| An out-of-bounds access issue was addressed with improved bounds checking. | bounds checking／checks | 143 | 0 | 143 | 99 | 4 | 0 | 5 | 0 | 0 |
| An integer overflow was addressed with improved input validation. | input validation | 136 | 0 | 136 | 57 | 25 | 0 | 9 | 0 | 0 |
| This issue was addressed with improved checks. | improved checks（泛称） | 131 | 0 | 131 | 93 | 3 | 9 | 4 | 9 | 0 |
| A path handling issue was addressed with improved validation. | improved validation（泛称） | 110 | 0 | 110 | 62 | 0 | 0 | 0 | 0 | 0 |
| A buffer overflow was addressed with improved bounds checking. | bounds checking／checks | 102 | 0 | 102 | 44 | 22 | 0 | 7 | 0 | 0 |
| This issue was addressed with additional entitlement checks. | entitlement 检查 | 99 | 0 | 99 | 60 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved bounds checks. | bounds checking／checks | 89 | 0 | 89 | 65 | 0 | 0 | 3 | 0 | 0 |
| A race condition was addressed with additional validation. | additional validation（泛称） | 83 | 0 | 83 | 58 | 0 | 0 | 0 | 0 | 83 |
| An out-of-bounds write issue was addressed with improved input validation. | input validation | 83 | 0 | 83 | 64 | 6 | 0 | 7 | 0 | 0 |
| An out-of-bounds read was addressed with improved input validation. | input validation | 77 | 0 | 77 | 56 | 0 | 1 | 19 | 0 | 0 |
| A parsing issue in the handling of directory paths was addressed with improved path validation. | path validation | 73 | 0 | 73 | 45 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved validation of symlinks. | symlink 校验 | 66 | 0 | 66 | 36 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved input validation. | input validation | 65 | 0 | 65 | 49 | 19 | 0 | 0 | 0 | 0 |
| A type confusion issue was addressed with improved checks. | improved checks（泛称） | 63 | 0 | 63 | 50 | 25 | 9 | 0 | 0 | 0 |
| An out-of-bounds read issue was addressed with improved input validation. | input validation | 59 | 0 | 59 | 41 | 7 | 0 | 8 | 0 | 0 |
| A logic issue was addressed with improved validation. | improved validation（泛称） | 44 | 0 | 44 | 35 | 4 | 0 | 0 | 44 | 0 |
| A permissions issue was addressed with improved validation. | improved validation（泛称） | 38 | 0 | 38 | 27 | 0 | 0 | 0 | 0 | 0 |
| A denial-of-service issue was addressed with improved input validation. | input validation | 33 | 0 | 33 | 31 | 31 | 0 | 0 | 0 | 0 |
| A null pointer dereference was addressed with improved input validation. | input validation | 31 | 0 | 31 | 30 | 22 | 0 | 0 | 0 | 0 |
| An information leakage was addressed with additional validation. | additional validation（泛称） | 29 | 0 | 29 | 26 | 8 | 0 | 0 | 0 | 0 |
| A buffer overflow was addressed with improved size validation. | 其余含 validat 句 | 28 | 0 | 28 | 23 | 0 | 0 | 1 | 0 | 0 |
| A file quarantine bypass was addressed with additional checks. | additional checks（泛称） | 27 | 0 | 27 | 24 | 0 | 0 | 0 | 0 | 0 |
| A memory corruption issue was addressed with improved validation. | improved validation（泛称） | 27 | 0 | 27 | 22 | 0 | 23 | 0 | 0 | 0 |
| A correctness issue was addressed with improved checks. | improved checks（泛称） | 24 | 0 | 24 | 18 | 0 | 0 | 0 | 0 | 0 |
| A validation issue was addressed with improved input sanitization. | input sanitization | 23 | 0 | 23 | 18 | 3 | 0 | 3 | 0 | 0 |
| A memory corruption issue was addressed with improved input validation. | input validation | 22 | 0 | 22 | 18 | 3 | 0 | 0 | 0 | 0 |
| A stack overflow was addressed with improved input validation. | input validation | 22 | 0 | 22 | 22 | 6 | 0 | 0 | 0 | 0 |
| A validation issue was addressed with improved logic. | validation issue 等名词化主语 | 22 | 0 | 22 | 22 | 8 | 0 | 0 | 22 | 0 |
| An input validation issue was addressed | input validation | 21 | 21 | 0 | 13 | 0 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed with improved checks. | improved checks（泛称） | 20 | 0 | 20 | 17 | 0 | 0 | 0 | 0 | 0 |
| An app may bypass Gatekeeper checks | Gatekeeper checks | 19 | 19 | 0 | 15 | 0 | 0 | 0 | 13 | 1 |
| The issue was addressed with additional permissions checks. | permissions 检查 | 18 | 0 | 18 | 18 | 0 | 0 | 0 | 0 | 0 |
| A memory corruption issue was addressed with improved bounds checking. | bounds checking／checks | 17 | 0 | 17 | 17 | 0 | 5 | 0 | 0 | 0 |
| An injection issue was addressed with improved validation. | improved validation（泛称） | 17 | 0 | 17 | 17 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved input validation. | input validation | 17 | 0 | 17 | 15 | 6 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved permissions checking. | permissions 检查 | 17 | 0 | 17 | 15 | 4 | 0 | 0 | 0 | 0 |
| A permissions issue was addressed with improved path validation. | path validation | 16 | 0 | 16 | 8 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved validation. | improved validation（泛称） | 16 | 0 | 16 | 16 | 0 | 0 | 0 | 0 | 0 |
| A denial-of-service issue was addressed with improved validation. | improved validation（泛称） | 13 | 0 | 13 | 13 | 9 | 0 | 0 | 0 | 0 |
| An input validation issue was addressed with improved input validation. | input validation | 13 | 0 | 13 | 9 | 0 | 0 | 0 | 0 | 0 |
| A file access issue was addressed with improved input validation. | input validation | 12 | 0 | 12 | 12 | 0 | 0 | 0 | 0 | 0 |
| An authorization issue was addressed with improved validation. | improved validation（泛称） | 12 | 0 | 12 | 12 | 0 | 0 | 0 | 0 | 0 |
| A certificate validation issue was addressed with improved certificate validation. | certificate 校验 | 11 | 0 | 11 | 8 | 3 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved checks | improved checks（泛称） | 11 | 0 | 11 | 11 | 0 | 0 | 0 | 0 | 0 |
| A heap buffer overflow was addressed with improved bounds checking. | bounds checking／checks | 10 | 0 | 10 | 7 | 0 | 0 | 0 | 0 | 0 |
| A maliciously crafted ZIP archive may bypass Gatekeeper checks | Gatekeeper checks | 10 | 10 | 0 | 4 | 0 | 0 | 0 | 3 | 0 |
| A buffer overflow was addressed with improved input validation. | input validation | 9 | 0 | 9 | 9 | 1 | 0 | 0 | 0 | 0 |
| A memory corruption issue was addressed with improved lock state checking. | lock state checking | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 | 0 |
| A validation issue existed in the entitlement verification. | validation issue 等名词化主语 | 8 | 0 | 8 | 6 | 0 | 0 | 0 | 0 | 0 |
| An integer overflow was addressed through improved input validation. | input validation | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 | 0 |
| An issue was addressed with improved validation of environment variables. | 环境变量校验 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 | 0 |
| An out-of-bounds write issue was addressed with improved checks to prevent unauthorized actions. | 防止未授权动作的检查 | 8 | 0 | 8 | 8 | 0 | 8 | 0 | 0 | 0 |
| This issue was addressed with improved checks to prevent unauthorized actions. | 防止未授权动作的检查 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved validation of the process entitlement. | entitlement 检查 | 8 | 0 | 8 | 6 | 0 | 0 | 0 | 0 | 0 |
| A mail header parsing issue was addressed with improved checks. | improved checks（泛称） | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 | 0 |
| A permissions issue was addressed by removing vulnerable code and adding additional checks. | additional checks（泛称） | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 | 0 |
| An input validation issue was addressed. | input validation | 7 | 0 | 7 | 7 | 6 | 0 | 0 | 0 | 0 |
| A cross-origin issue in the Navigation API was addressed with improved input validation. | input validation | 6 | 0 | 6 | 6 | 0 | 0 | 0 | 0 | 0 |
| A custom URL scheme handling issue was addressed with improved input validation. | input validation | 6 | 0 | 6 | 6 | 1 | 0 | 0 | 0 | 0 |
| A path traversal issue was addressed with improved input validation. | input validation | 6 | 0 | 6 | 6 | 0 | 0 | 0 | 0 | 0 |
| An integer underflow was addressed with improved input validation. | input validation | 6 | 0 | 6 | 3 | 3 | 0 | 0 | 0 | 0 |
| A path traversal issue was addressed with improved path validation. | path validation | 5 | 0 | 5 | 5 | 3 | 0 | 0 | 0 | 0 |
| A reachable assertion was addressed with improved input validation. | input validation | 5 | 0 | 5 | 5 | 5 | 0 | 0 | 0 | 0 |
| A resource exhaustion issue was addressed with improved input validation. | input validation | 5 | 0 | 5 | 5 | 5 | 0 | 0 | 0 | 0 |
| An injection issue was addressed with improved input validation. | input validation | 5 | 0 | 5 | 5 | 0 | 0 | 0 | 0 | 0 |
| An out-of-bounds write was addressed with improved input validation. | input validation | 5 | 0 | 5 | 5 | 0 | 0 | 0 | 0 | 0 |
| Multiple memory corruption issues were addressed with improved input validation. | input validation | 5 | 0 | 5 | 5 | 0 | 0 | 0 | 0 | 0 |
| A cryptographic issue was addressed with improved integrity checks. | integrity checks | 4 | 0 | 4 | 4 | 4 | 0 | 0 | 0 | 0 |
| A maliciously crafted disk image may bypass Gatekeeper checks | Gatekeeper checks | 4 | 4 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| A path handling issue was addressed with improved validation | improved validation（泛称） | 4 | 4 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| A session management issue was addressed with improved checks. | improved checks（泛称） | 4 | 0 | 4 | 4 | 1 | 0 | 0 | 0 | 0 |
| An app may be able to bypass Gatekeeper checks | Gatekeeper checks | 4 | 4 | 0 | 3 | 0 | 0 | 0 | 4 | 0 |
| An app may be able to retrieve Safari bookmarks without an entitlement check | 其余含 check 句 | 4 | 4 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| An input validation issue was addressed with improved memory handling. | input validation | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved URL validation. | URL validation | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved validation of the caller's access. | caller 访问校验 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| A malicious JAR file may bypass Gatekeeper checks | Gatekeeper checks | 3 | 3 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| A malicious application may bypass Gatekeeper checks | Gatekeeper checks | 3 | 3 | 0 | 3 | 0 | 0 | 0 | 3 | 0 |
| An app may be able to check the existence of an arbitrary path on the file system | 其余含 check 句 | 3 | 3 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| An authorization issue was addressed with improved entitlement checks. | entitlement 检查 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| An input validation issue was addressed by removing the vulnerable code. | input validation | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| An uncontrolled format string issue was addressed with improved input validation. | input validation | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| A race condition was addressed with improved checks. | improved checks（泛称） | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 | 2 |
| A stack buffer overflow was addressed through improved input validation. | input validation | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved validation of environment variables. | 环境变量校验 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed by enforcing library validation on the affected binaries. | library validation | 2 | 0 | 2 | 1 | 0 | 0 | 0 | 0 | 0 |
| A package validation issue was addressed by blocking the vulnerable package. | validation issue 等名词化主语 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| A remote attacker may be able to bypass sender policy checks and deliver malicious content via email | 其余含 check 句 | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| The issue was addressed with additional app bundle integrity checks. | integrity checks | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved message validation. | 其余含 validat 句 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved validation of file attributes. | improved validation（泛称） | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| **合计** | 25 桶 | 3393 | 76 | 3317 | 页列合计 2009 | 425 | 70 | 106 | 325 | 86 |

# 附表 C 二十三个检查对象桶 × 句次 × 段页名 × I∶D × ∩R ∩E（24 行）

| 桶 | 句次 | 段 | 页 | 名 | Impact 句 | Desc 句 | ∩R | ∩E |
|---|---:|---:|---:|---:|---:|---:|---:|---:|
| improved checks（泛称） | 1002 | 1002 | 188 | 125 | 0 | 1002 | 151 | 23 |
| bounds checking／checks | 788 | 788 | 163 | 72 | 0 | 788 | 81 | 15 |
| input validation | 676 | 676 | 151 | 85 | 21 | 655 | 146 | 1 |
| improved validation（泛称） | 282 | 282 | 97 | 58 | 4 | 278 | 13 | 23 |
| additional validation（泛称） | 112 | 112 | 65 | 20 | 0 | 112 | 8 | 0 |
| entitlement 检查 | 110 | 110 | 63 | 32 | 0 | 110 | 0 | 0 |
| path validation | 94 | 94 | 50 | 26 | 0 | 94 | 3 | 0 |
| symlink 校验 | 66 | 66 | 36 | 19 | 0 | 66 | 0 | 0 |
| Gatekeeper checks | 43 | 43 | 23 | 14 | 43 | 0 | 0 | 0 |
| permissions 检查 | 35 | 35 | 32 | 11 | 0 | 35 | 4 | 0 |
| additional checks（泛称） | 34 | 34 | 28 | 12 | 0 | 34 | 0 | 0 |
| validation issue 等名词化主语 | 31 | 31 | 29 | 6 | 0 | 31 | 8 | 0 |
| 其余含 validat 句 | 29 | 29 | 24 | 8 | 0 | 29 | 0 | 0 |
| input sanitization | 23 | 23 | 18 | 6 | 0 | 23 | 3 | 0 |
| 防止未授权动作的检查 | 16 | 16 | 16 | 4 | 0 | 16 | 0 | 8 |
| certificate 校验 | 11 | 11 | 8 | 1 | 0 | 11 | 3 | 0 |
| 环境变量校验 | 10 | 10 | 10 | 4 | 0 | 10 | 0 | 0 |
| lock state checking | 8 | 8 | 8 | 1 | 0 | 8 | 0 | 0 |
| 其余含 check 句 | 8 | 8 | 4 | 3 | 8 | 0 | 1 | 0 |
| integrity checks | 5 | 5 | 4 | 2 | 0 | 5 | 4 | 0 |
| URL validation | 4 | 4 | 4 | 1 | 0 | 4 | 0 | 0 |
| caller 访问校验 | 4 | 4 | 4 | 1 | 0 | 4 | 0 | 0 |
| library validation | 2 | 2 | 1 | 2 | 0 | 2 | 0 | 0 |
| **合计** | 3393 | 段列可重叠 | 页列合计 1026 | 名列合计 513 | 76 | 3317 | — | — |

## 附表 C 续表：句子打头与修复动词形态（优先序闭合分区）

| 形态刀 | 句次 | 段 | 页 | 名 |
|---|---:|---:|---:|---:|
| 「The／This issue was addressed …」打头 | 1074 | 1074 | 194 | 152 |
| 「A／An X issue was addressed …」打头 | 507 | 507 | 152 | 83 |
| 「A／An X（非 issue 名词）was addressed …」打头 | 1748 | 1748 | 211 | 160 |
| 「Multiple X issues were addressed …」打头 | 5 | 5 | 5 | 1 |
| 「X existed（缺陷存在句）」 | 8 | 8 | 6 | 3 |
| 含「may」（影响句情态） | 51 | 51 | 24 | 17 |
| **合计** | 3393 | — | — | — |

# 附表 D 页面、编号、致谢、版式与字段落位五组分箱（30 行）

| 块 | 分箱 | 计数 | 说明 |
|---|---|---:|---|
| CK 段所在页的段数 | 1 | 58 | 共 241 页含本篇条目（全树 257 页） |
| CK 段所在页的段数 | 2 | 15 | 共 241 页含本篇条目（全树 257 页） |
| CK 段所在页的段数 | 3 | 8 | 共 241 页含本篇条目（全树 257 页） |
| CK 段所在页的段数 | 4 | 7 | 共 241 页含本篇条目（全树 257 页） |
| CK 段所在页的段数 | 5 | 7 | 共 241 页含本篇条目（全树 257 页） |
| CK 段所在页的段数 | 6-9 | 27 | 共 241 页含本篇条目（全树 257 页） |
| CK 段所在页的段数 | 10-19 | 66 | 共 241 页含本篇条目（全树 257 页） |
| CK 段所在页的段数 | 20-49 | 43 | 共 241 页含本篇条目（全树 257 页） |
| CK 段所在页的段数 | 50+ | 10 | 共 241 页含本篇条目（全树 257 页） |
| CK 段所在页的段数 | 合计 | 241 | 校验=241 页、CK 段 3345 |
| Top10 页 | 前 10 页合计 | 789 | 占 3345 段的 23.6% |
| 同页共存 | 241 页全部段 | 9119 | 本篇占该页集 36.68%，每页均 13.88 段（第三套分母） |
| 同页共存 | 全本篇页 | 43 | 该页所有段都被本篇抓到 |
| 编号数分箱 | 0 个 | 1 | 无编号段 |
| 编号数分箱 | 1 个 | 3156 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 2 个 | 145 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 3 个 | 22 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 4+ 个 | 21 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 合计 | 3345 | 带编号段=3344／3345，distinct 编号=880，本集独占=877，与他集共享=3 |
| 致谢人数分箱 | 1 人 | 2119 | — |
| 致谢人数分箱 | 2 人 | 837 | — |
| 致谢人数分箱 | 3 人 | 197 | — |
| 致谢人数分箱 | 4 人 | 89 | — |
| 致谢人数分箱 | 5 人 | 48 | — |
| 致谢人数分箱 | 6-9 人 | 39 | — |
| 致谢人数分箱 | 9+ 人 | 15 | — |
| 致谢人数分箱 | 合计 | 3344 | 人次=5534，均值=1.65，最大=37（全树 1.80／37） |
| 版式域 | 域 B（11 页粗体版式） | 29 | 域 B 全段 91，占比 31.87%；域 A 3316／9,045 ＝ 36.66% |
| 字段落位 | Impact 侧 | 76 | Description 侧 3309；仅I 36／仅D 3269／I∩D 40（两组闭合 3345） |
| 句级 | 3345 段的本篇句数 | 1 句 3297／2 句 48 | 句次合计 3393（I 76＋D 3317），distinct 句 95 |

# 附表 E 中文手册层八组对应词（92 行）

| 组 | 中文词 | 出现次数 | 覆盖页 | 覆盖页占比 | 最集中页面 |
|---|---|---:|---:|---:|---|
| 检查与校验本体 | 检查 | 66 | 30 | 16.9% | 管理式 Apple 账户安全性（13 次） |
| 检查与校验本体 | 校验 | 19 | 3 | 1.7% | Apple 钱包中证件的安全性（16 次） |
| 检查与校验本体 | 验证 | 432 | 178 | 100.0% | Apple 钱包中的证件（19 次） |
| 检查与校验本体 | 核查 | 1 | 1 | 0.6% | 搭载 Apple 芯片的 Mac 上的 Rosetta 2（1 次） |
| 检查与校验本体 | 检测 | 77 | 35 | 19.7% | 在 macOS 中防范恶意软件（12 次） |
| 检查与校验本体 | 审查 | 13 | 4 | 2.2% | 关于 App Store 安全性（8 次） |
| 检查与校验本体 | 巡检 | 0 | 0 | 0.0% | — |
| 检查与校验本体 | 检查项 | 0 | 0 | 0.0% | — |
| 检查与校验本体 | 检查机制 | 0 | 0 | 0.0% | — |
| 检查与校验本体 | 校对 | 0 | 0 | 0.0% | — |
| 修复与加固套话 | 改进 | 231 | 178 | 100.0% | Apple 操作系统中的后台安全改进（38 次） |
| 修复与加固套话 | 改进的检查 | 0 | 0 | 0.0% | — |
| 修复与加固套话 | 额外的检查 | 0 | 0 | 0.0% | — |
| 修复与加固套话 | 改善 | 4 | 3 | 1.7% | 脸部匹配安全性（2 次） |
| 修复与加固套话 | 加强 | 3 | 3 | 1.7% | 术语表（1 次） |
| 修复与加固套话 | 加固 | 0 | 0 | 0.0% | — |
| 修复与加固套话 | 修复 | 14 | 8 | 4.5% | 在 macOS 中防范恶意软件（5 次） |
| 修复与加固套话 | 补丁 | 4 | 1 | 0.6% | Apple 操作系统中的后台安全改进（4 次） |
| 修复与加固套话 | 移除 | 104 | 38 | 21.3% | Apple 操作系统中的后台安全改进（17 次） |
| 修复与加固套话 | 删除 | 50 | 20 | 11.2% | iOS 中的车钥匙安全性（8 次） |
| 被校验对象 | 输入 | 106 | 55 | 30.9% | Tap to Pay on iPhone 安全性（11 次） |
| 被校验对象 | 路径 | 4 | 3 | 1.7% | 签名系统宗卷安全性（2 次） |
| 被校验对象 | 符号链接 | 0 | 0 | 0.0% | — |
| 被校验对象 | 权限 | 283 | 178 | 100.0% | 操作系统完整性（14 次） |
| 被校验对象 | 授权 | 352 | 178 | 100.0% | NFC 与 SE 平台安全性（13 次） |
| 被校验对象 | 证书 | 150 | 29 | 16.3% | TLS 安全性（17 次） |
| 被校验对象 | 库 | 35 | 15 | 8.4% | iCloud 加密（6 次） |
| 被校验对象 | URL | 20 | 8 | 4.5% | TLS 安全性（5 次） |
| 被校验对象 | 环境变量 | 0 | 0 | 0.0% | — |
| 被校验对象 | 边界 | 5 | 5 | 2.8% | 内存安全 iBoot 实施（1 次） |
| 被校验对象 | 长度 | 8 | 7 | 3.9% | 设备管理安全性概览（2 次） |
| 被校验对象 | 格式 | 18 | 14 | 7.9% | 基于 Intel 的 Mac 的启动过程（4 次） |
| 被校验对象 | 调用方 | 0 | 0 | 0.0% | — |
| 被校验对象 | 沙盒 | 27 | 11 | 6.2% | macOS 中的 Option ROM 安全性（6 次） |
| 被校验对象 | 隔离 | 10 | 6 | 3.4% | 安全隔区（3 次） |
| 被校验对象 | 门禁 | 218 | 178 | 100.0% | macOS 中的门禁和运行时保护（14 次） |
| 规避与控制词 | 绕过 | 23 | 9 | 5.1% | 激活锁安全性（12 次） |
| 规避与控制词 | 规避 | 1 | 1 | 0.6% | 操作系统完整性（1 次） |
| 规避与控制词 | 逃逸 | 0 | 0 | 0.0% | — |
| 规避与控制词 | 突破 | 0 | 0 | 0.0% | — |
| 规避与控制词 | 跳过 | 2 | 1 | 0.6% | 自动设备注册（2 次） |
| 规避与控制词 | 越过 | 0 | 0 | 0.0% | — |
| 规避与控制词 | 控制 | 509 | 178 | 100.0% | 搭载 Apple 芯片的 Mac 的 LocalPolicy 文件内容（11 次） |
| 规避与控制词 | 约束 | 1 | 1 | 0.6% | 操作系统完整性（1 次） |
| 规避与控制词 | 限制 | 478 | 178 | 100.0% | LocalPolicy 签名密钥创建和管理（21 次） |
| 规避与控制词 | 策略 | 301 | 178 | 100.0% | 搭载 Apple T2 安全芯片的 Mac 上的启动安全性实用工具（21 次） |
| 规避与控制词 | 安全控制 | 0 | 0 | 0.0% | — |
| 检测与告警词 | 监控 | 28 | 15 | 8.4% | 操作系统完整性（8 次） |
| 检测与告警词 | 监测 | 1 | 1 | 0.6% | Apple 设备的无线局域网安全性（1 次） |
| 检测与告警词 | 日志 | 4 | 3 | 1.7% | 保护对用户健康数据的访问（2 次） |
| 检测与告警词 | 记录 | 257 | 178 | 100.0% | 证明过程安全性（12 次） |
| 检测与告警词 | 审计 | 0 | 0 | 0.0% | — |
| 检测与告警词 | 告警 | 0 | 0 | 0.0% | — |
| 检测与告警词 | 警报 | 1 | 1 | 0.6% | 访问钥匙类型（1 次） |
| 检测与告警词 | 检测能力 | 0 | 0 | 0.0% | — |
| 检测与告警词 | 威胁检测 | 0 | 0 | 0.0% | — |
| 施事与载体 | App | 18967 | 178 | 100.0% | 文稿修订记录（457 次） |
| 施事与载体 | 应用 | 82 | 40 | 22.5% | Apple 操作系统中的后台安全改进（10 次） |
| 施事与载体 | 快捷指令 | 196 | 178 | 100.0% | “快捷指令” App 中的安全功能（15 次） |
| 施事与载体 | 网站 | 93 | 36 | 20.2% | Apple 钱包中的证件（14 次） |
| 施事与载体 | 远程攻击者 | 0 | 0 | 0.0% | — |
| 施事与载体 | 恶意 | 287 | 178 | 100.0% | 在 macOS 中防范恶意软件（39 次） |
| 施事与载体 | 存档 | 3 | 1 | 0.6% | 将信用卡或借记卡添加到 Apple Pay（3 次） |
| 施事与载体 | 归档 | 2 | 2 | 1.1% | 访问钥匙类型（1 次） |
| 施事与载体 | 磁盘映像 | 23 | 6 | 3.4% | 数字签名和加密（9 次） |
| 施事与载体 | ZIP | 0 | 0 | 0.0% | — |
| 施事与载体 | JAR | 0 | 0 | 0.0% | — |
| 施事与载体 | 附件 | 17 | 7 | 3.9% | “备忘录” App 中的安全功能（6 次） |
| 类型与后果 | 越界 | 1 | 1 | 0.6% | 操作系统完整性（1 次） |
| 类型与后果 | 整数溢出 | 0 | 0 | 0.0% | — |
| 类型与后果 | 缓冲区 | 1 | 1 | 0.6% | 内存安全 iBoot 实施（1 次） |
| 类型与后果 | 类型混淆 | 2 | 1 | 0.6% | 内存安全 iBoot 实施（2 次） |
| 类型与后果 | 内存破坏 | 0 | 0 | 0.0% | — |
| 类型与后果 | 竞态 | 0 | 0 | 0.0% | — |
| 类型与后果 | 逻辑 | 4 | 2 | 1.1% | 硬件麦克风断联（3 次） |
| 类型与后果 | 拒绝服务 | 1 | 1 | 0.6% | macOS 中的防火墙安全性（1 次） |
| 类型与后果 | 泄露 | 30 | 11 | 6.2% | 密码监视（15 次） |
| 类型与后果 | 代码执行 | 3 | 2 | 1.1% | 搭载 Apple 芯片的 Mac 上的 Rosetta 2（2 次） |
| 类型与后果 | 完全控制 | 0 | 0 | 0.0% | — |
| 类型与后果 | 提权 | 0 | 0 | 0.0% | — |
| 范围与程度 | 任意 | 9 | 8 | 4.5% | 搭载 Apple 芯片的 Mac 的启动磁盘安全性策略控制（2 次） |
| 范围与程度 | 全部 | 4 | 4 | 2.2% | 密码（1 次） |
| 范围与程度 | 所有 | 525 | 178 | 100.0% | 接入无线网络时的安全功能（22 次） |
| 范围与程度 | 部分 | 112 | 73 | 41.0% | 术语表（6 次） |
| 范围与程度 | 可能 | 128 | 74 | 41.6% | Apple 操作系统中的后台安全改进（4 次） |
| 范围与程度 | 无法 | 143 | 72 | 40.4% | iCloud 高级数据保护（9 次） |
| 范围与程度 | 未 | 185 | 83 | 46.6% | 激活锁安全性（9 次） |
| 范围与程度 | 已 | 472 | 178 | 100.0% | 在 macOS 中防范恶意软件（11 次） |
| 范围与程度 | 某些 | 30 | 27 | 15.2% | 搭载 Apple 芯片的 Mac 的 LocalPolicy 文件内容（3 次） |
| 范围与程度 | 特定 | 120 | 58 | 32.6% | Apple 钱包中的证件（8 次） |
| 范围与程度 | 意外 | 4 | 4 | 2.2% | 钥匙串安全同步（1 次） |
| **合计** | 91 词（全零 26 词、非零 65 词） | — | — | 178 页可用 | — |

# 附表 F 组件名层二百七十三名 × 六个外部集 × 该组件 Impact 全集（273 行）

| 组件名 | CK 段 | 页 | ∩R | ∩E | ∩M2 | ∩L | ∩BP | 该组件含 Impact 全集 | CK 占比 |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| WebKit | 310 | 127 | 49 | 25 | 7 | 25 | 14 | 910 | 34.1% |
| Kernel | 236 | 109 | 27 | 8 | 0 | 32 | 15 | 681 | 34.7% |
| ImageIO | 171 | 97 | 0 | 7 | 0 | 6 | 0 | 261 | 65.5% |
| SceneKit | 89 | 49 | 8 | 0 | 16 | 0 | 0 | 112 | 79.5% |
| CoreMedia | 68 | 55 | 0 | 0 | 8 | 0 | 0 | 150 | 45.3% |
| Security | 68 | 51 | 34 | 0 | 0 | 15 | 0 | 91 | 74.7% |
| Model I/O | 67 | 31 | 33 | 0 | 3 | 0 | 0 | 88 | 76.1% |
| CoreAudio | 65 | 51 | 8 | 5 | 0 | 0 | 0 | 79 | 82.3% |
| PackageKit | 57 | 34 | 0 | 0 | 0 | 5 | 3 | 106 | 53.8% |
| AppleMobileFileIntegrity | 54 | 40 | 0 | 0 | 1 | 13 | 4 | 146 | 37.0% |
| Shortcuts | 50 | 39 | 7 | 0 | 0 | 20 | 7 | 129 | 38.8% |
| Audio | 44 | 39 | 0 | 0 | 3 | 9 | 8 | 95 | 46.3% |
| CoreServices | 43 | 29 | 0 | 0 | 0 | 11 | 2 | 81 | 53.1% |
| AirPlay | 42 | 14 | 42 | 0 | 0 | 0 | 0 | 77 | 54.5% |
| libxpc | 40 | 34 | 0 | 0 | 0 | 19 | 0 | 80 | 50.0% |
| Disk Images | 38 | 20 | 0 | 0 | 0 | 0 | 3 | 54 | 70.4% |
| WebRTC | 38 | 22 | 0 | 0 | 0 | 0 | 0 | 48 | 79.2% |
| Foundation | 35 | 30 | 0 | 0 | 0 | 9 | 0 | 67 | 52.2% |
| LaunchServices | 34 | 29 | 8 | 0 | 0 | 11 | 5 | 58 | 58.6% |
| Safari | 34 | 31 | 15 | 0 | 0 | 6 | 1 | 92 | 37.0% |
| CoreText | 33 | 29 | 0 | 0 | 0 | 0 | 0 | 33 | 100.0% |
| CUPS | 31 | 11 | 9 | 0 | 6 | 3 | 0 | 43 | 72.1% |
| StorageKit | 31 | 25 | 0 | 0 | 0 | 3 | 3 | 59 | 52.5% |
| Bluetooth | 29 | 17 | 19 | 0 | 8 | 0 | 0 | 41 | 70.7% |
| FontParser | 29 | 24 | 0 | 0 | 0 | 0 | 0 | 29 | 100.0% |
| CoreGraphics | 27 | 24 | 0 | 3 | 8 | 0 | 0 | 27 | 100.0% |
| CoreUI | 27 | 13 | 0 | 0 | 0 | 0 | 0 | 32 | 84.4% |
| Wi-Fi | 27 | 20 | 23 | 0 | 0 | 0 | 0 | 58 | 46.6% |
| SMB | 26 | 8 | 5 | 0 | 0 | 0 | 0 | 58 | 44.8% |
| sips | 25 | 13 | 0 | 0 | 0 | 0 | 0 | 25 | 100.0% |
| APFS | 24 | 18 | 0 | 0 | 0 | 0 | 0 | 38 | 63.2% |
| GPU Drivers | 23 | 18 | 0 | 0 | 0 | 0 | 0 | 34 | 67.6% |
| Metal | 23 | 19 | 4 | 0 | 11 | 0 | 0 | 25 | 92.0% |
| Spotlight | 22 | 20 | 0 | 0 | 2 | 3 | 0 | 69 | 31.9% |
| IOMobileFrameBuffer | 21 | 17 | 0 | 0 | 3 | 0 | 0 | 22 | 95.5% |
| Mail | 21 | 18 | 7 | 0 | 0 | 7 | 0 | 36 | 58.3% |
| Sandbox | 21 | 18 | 4 | 0 | 0 | 8 | 7 | 65 | 32.3% |
| Game Center | 20 | 20 | 0 | 0 | 0 | 0 | 0 | 33 | 60.6% |
| HFS | 20 | 10 | 0 | 0 | 0 | 0 | 0 | 32 | 62.5% |
| AVEVideoEncoder | 19 | 15 | 0 | 0 | 0 | 0 | 0 | 45 | 42.2% |
| System Settings | 19 | 17 | 0 | 0 | 0 | 5 | 5 | 30 | 63.3% |
| Calendar | 18 | 10 | 2 | 0 | 0 | 0 | 0 | 22 | 81.8% |
| mDNSResponder | 18 | 15 | 11 | 0 | 0 | 0 | 0 | 53 | 34.0% |
| CloudKit | 17 | 17 | 0 | 0 | 0 | 0 | 0 | 25 | 68.0% |
| Installer | 17 | 13 | 0 | 0 | 0 | 9 | 0 | 29 | 58.6% |
| Contacts | 16 | 8 | 0 | 0 | 0 | 0 | 0 | 25 | 64.0% |
| Messages | 16 | 16 | 1 | 9 | 0 | 9 | 0 | 50 | 32.0% |
| AppleJPEG | 15 | 15 | 0 | 0 | 0 | 0 | 0 | 28 | 53.6% |
| CoreUtils | 15 | 15 | 15 | 0 | 0 | 0 | 0 | 15 | 100.0% |
| ICU | 15 | 15 | 0 | 0 | 0 | 0 | 0 | 15 | 100.0% |
| Multi-Touch | 15 | 15 | 0 | 0 | 0 | 0 | 3 | 15 | 100.0% |
| RealityKit | 14 | 7 | 0 | 0 | 7 | 0 | 0 | 14 | 100.0% |
| libarchive | 14 | 14 | 0 | 0 | 0 | 0 | 4 | 30 | 46.7% |
| ARKit | 13 | 13 | 0 | 0 | 0 | 0 | 0 | 13 | 100.0% |
| Accelerate Framework | 13 | 8 | 0 | 0 | 0 | 0 | 0 | 13 | 100.0% |
| Apple Neural Engine | 13 | 13 | 0 | 0 | 0 | 0 | 0 | 36 | 36.1% |
| BackgroundAssets | 13 | 13 | 0 | 0 | 0 | 7 | 0 | 13 | 100.0% |
| FileProvider | 13 | 11 | 0 | 0 | 0 | 0 | 0 | 18 | 72.2% |
| MobileAccessoryUpdater | 13 | 13 | 0 | 0 | 0 | 0 | 0 | 13 | 100.0% |
| Admin Framework | 12 | 11 | 0 | 0 | 0 | 6 | 0 | 12 | 100.0% |
| DiskArbitration | 12 | 11 | 0 | 0 | 0 | 0 | 0 | 25 | 48.0% |
| Heimdal | 12 | 12 | 4 | 0 | 0 | 0 | 0 | 19 | 63.2% |
| IOHIDFamily | 12 | 12 | 0 | 0 | 0 | 0 | 0 | 27 | 44.4% |
| NetAuth | 12 | 9 | 0 | 0 | 0 | 0 | 0 | 15 | 80.0% |
| SharedFileList | 12 | 12 | 0 | 0 | 0 | 3 | 0 | 39 | 30.8% |
| Xsan | 12 | 9 | 0 | 0 | 0 | 0 | 0 | 15 | 80.0% |
| CoreMedia Playback | 11 | 11 | 0 | 0 | 0 | 0 | 0 | 23 | 47.8% |
| MediaRemote | 11 | 11 | 0 | 0 | 0 | 0 | 0 | 24 | 45.8% |
| QuartzCore | 11 | 11 | 0 | 0 | 0 | 0 | 0 | 12 | 91.7% |
| Software Update | 11 | 10 | 0 | 0 | 0 | 0 | 0 | 18 | 61.1% |
| copyfile | 11 | 11 | 0 | 0 | 0 | 0 | 7 | 12 | 91.7% |
| IOSurfaceAccelerator | 10 | 10 | 0 | 0 | 0 | 0 | 0 | 18 | 55.6% |
| libc | 10 | 10 | 0 | 0 | 0 | 0 | 0 | 10 | 100.0% |
| App Store | 9 | 9 | 0 | 0 | 0 | 0 | 0 | 24 | 37.5% |
| AppleDouble | 9 | 9 | 0 | 0 | 4 | 0 | 0 | 9 | 100.0% |
| CFNetwork | 9 | 9 | 8 | 0 | 0 | 0 | 0 | 20 | 45.0% |
| DesktopServices | 9 | 9 | 3 | 0 | 0 | 3 | 3 | 11 | 81.8% |
| Libinfo | 9 | 9 | 0 | 0 | 0 | 0 | 3 | 9 | 100.0% |
| NetFSFramework | 9 | 9 | 0 | 0 | 0 | 0 | 0 | 9 | 100.0% |
| Quick Look | 9 | 9 | 0 | 0 | 0 | 1 | 0 | 10 | 90.0% |
| afpfs | 9 | 6 | 6 | 0 | 0 | 0 | 0 | 12 | 75.0% |
| AppleAVD | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 26 | 30.8% |
| BiometricKit | 8 | 8 | 0 | 0 | 0 | 1 | 0 | 8 | 100.0% |
| Libnotify | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 10 | 80.0% |
| Power Services | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| RTKit | 8 | 8 | 0 | 8 | 0 | 0 | 8 | 8 | 100.0% |
| Siri | 8 | 8 | 0 | 0 | 0 | 3 | 0 | 116 | 6.9% |
| Storage | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 20 | 40.0% |
| StoreKit | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 18 | 44.4% |
| Voice Control | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 16 | 50.0% |
| curl | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 39 | 20.5% |
| libnetcore | 8 | 8 | 3 | 0 | 0 | 8 | 0 | 11 | 72.7% |
| quarantine | 8 | 5 | 0 | 0 | 0 | 3 | 1 | 12 | 66.7% |
| zlib | 8 | 8 | 8 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| Core Data | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 9 | 77.8% |
| CoreMotion | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| DriverKit | 7 | 4 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| Printing | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 17 | 41.2% |
| Pro Res | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 21 | 33.3% |
| Reminders | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| SystemMigration | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| UIFoundation | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| VoiceOver | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 14 | 50.0% |
| WebKit History | 7 | 7 | 7 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| dyld | 7 | 7 | 2 | 0 | 0 | 0 | 5 | 15 | 46.7% |
| exFAT | 7 | 5 | 0 | 0 | 2 | 0 | 0 | 7 | 100.0% |
| file | 7 | 7 | 0 | 0 | 4 | 0 | 0 | 7 | 100.0% |
| ATS | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 17 | 35.3% |
| Accounts | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 34 | 17.6% |
| AppleRAID | 6 | 3 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Baseband | 6 | 3 | 5 | 0 | 0 | 0 | 0 | 7 | 85.7% |
| Control Center | 6 | 6 | 0 | 0 | 0 | 3 | 0 | 8 | 75.0% |
| Core Bluetooth | 6 | 6 | 6 | 0 | 0 | 0 | 0 | 16 | 37.5% |
| CoreML | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| CoreSymbolication | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| DeviceLink | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| File Bookmark | 6 | 6 | 0 | 0 | 0 | 6 | 0 | 15 | 40.0% |
| GeoServices | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Intel Graphics Driver | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 9 | 66.7% |
| Maps | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 23 | 26.1% |
| NetworkExtension | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 28 | 21.4% |
| Power Manager | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Single Sign-On | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Spell Check | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Sync Services | 6 | 6 | 0 | 0 | 0 | 0 | 6 | 12 | 50.0% |
| TCC | 6 | 6 | 0 | 0 | 0 | 0 | 3 | 29 | 20.7% |
| WebContentFilter | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 11 | 54.5% |
| autofs | 6 | 3 | 3 | 0 | 0 | 3 | 3 | 6 | 100.0% |
| configd | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 9 | 66.7% |
| file_cmds | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| iCloud Document Sharing | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| sudo | 6 | 6 | 0 | 0 | 0 | 2 | 0 | 10 | 60.0% |
| zip | 6 | 6 | 0 | 0 | 0 | 3 | 3 | 7 | 85.7% |
| AMD | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 8 | 62.5% |
| Accelerate | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| AirDrop | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 14 | 35.7% |
| Authentication Services | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 18 | 27.8% |
| Calling Framework | 5 | 5 | 5 | 0 | 0 | 0 | 0 | 9 | 55.6% |
| Clipboard | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| CloudAttestation | 5 | 5 | 0 | 0 | 0 | 0 | 5 | 5 | 100.0% |
| Crash Reporter | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 24 | 20.8% |
| Dock | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 19 | 26.3% |
| Finder | 5 | 5 | 0 | 0 | 0 | 3 | 3 | 10 | 50.0% |
| JavaScriptCore | 5 | 5 | 0 | 5 | 0 | 0 | 0 | 5 | 100.0% |
| MobileBackup | 5 | 3 | 0 | 0 | 0 | 0 | 0 | 14 | 35.7% |
| RemoteViewServices | 5 | 4 | 0 | 0 | 0 | 5 | 0 | 9 | 55.6% |
| Safe Browsing | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| Screen Sharing Server | 5 | 3 | 3 | 0 | 0 | 0 | 0 | 16 | 31.2% |
| System | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| Telephony | 5 | 4 | 3 | 0 | 0 | 0 | 0 | 8 | 62.5% |
| WebDAV | 5 | 3 | 0 | 0 | 0 | 2 | 2 | 20 | 25.0% |
| libiconv | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| libxml2 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 19 | 26.3% |
| Accessibility | 4 | 4 | 0 | 0 | 0 | 1 | 0 | 46 | 8.7% |
| Apple Account | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 25 | 16.0% |
| Books | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| CoreRoutine | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| CoreVideo | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| Directory Utility | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| Focus | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 14 | 28.6% |
| IOGPUFamily | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 17 | 23.5% |
| Image Capture | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 7 | 57.1% |
| Mail Drafts | 4 | 4 | 4 | 0 | 0 | 4 | 0 | 11 | 36.4% |
| MallocStackLogging | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| NSColorPanel | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 7 | 57.1% |
| Notes | 4 | 4 | 0 | 0 | 0 | 1 | 0 | 38 | 10.5% |
| Parental Controls | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| Passkeys | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 5 | 80.0% |
| Photos Storage | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 18 | 22.2% |
| QuickLook | 4 | 4 | 0 | 0 | 2 | 0 | 0 | 4 | 100.0% |
| Safari Private Browsing | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 16 | 25.0% |
| Screen Capture | 4 | 3 | 0 | 0 | 0 | 0 | 0 | 5 | 80.0% |
| Screenshots | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 7 | 57.1% |
| Security Initialization | 4 | 4 | 0 | 0 | 0 | 0 | 3 | 6 | 66.7% |
| Touch Bar | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| UserAccountUpdater | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| Web Extensions | 4 | 4 | 4 | 0 | 0 | 0 | 0 | 14 | 28.6% |
| WindowServer | 4 | 4 | 0 | 0 | 0 | 1 | 0 | 32 | 12.5% |
| udf | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 10 | 40.0% |
| ASP TCP | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 7 | 42.9% |
| Apple Software Restore | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Archive Utility | 3 | 3 | 0 | 0 | 0 | 0 | 2 | 8 | 37.5% |
| Assets | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 16 | 18.8% |
| Automator | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 9 | 33.3% |
| BOM | 3 | 3 | 0 | 0 | 0 | 0 | 3 | 4 | 75.0% |
| CoreAnimation | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 5 | 60.0% |
| CoreDrag | 3 | 3 | 0 | 0 | 3 | 0 | 0 | 3 | 100.0% |
| CoreServicesUIAgent | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 4 | 75.0% |
| Disk Utility | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| File System | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| IOAcceleratorFamily | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Kerberos v5 PAM module | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Kext Management | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Keychain Access | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 6 | 50.0% |
| Login Window | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 6 | 50.0% |
| Mobile Device Service | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Net-SNMP | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Network Extensions | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| OpenSSH | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 10 | 30.0% |
| Perl | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 8 | 37.5% |
| QuickTime Player | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Remote Management | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 6 | 50.0% |
| Restore Framework | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| SoftwareUpdate | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 15 | 20.0% |
| SpringBoard | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Storage Services | 3 | 3 | 0 | 0 | 0 | 3 | 0 | 3 | 100.0% |
| Touch Bar Controls | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Wi‑Fi | 3 | 3 | 0 | 0 | 3 | 0 | 0 | 3 | 100.0% |
| XProtect | 3 | 2 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Xcode | 3 | 3 | 0 | 0 | 0 | 1 | 1 | 3 | 100.0% |
| iCloud Photo Library | 3 | 3 | 0 | 0 | 0 | 0 | 3 | 3 | 100.0% |
| manpages | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| odproxyd | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| smbx | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 6 | 50.0% |
| zsh | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| AppleGraphicsControl | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 8 | 25.0% |
| AppleVA | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 7 | 28.6% |
| Camera | 2 | 2 | 0 | 0 | 0 | 2 | 0 | 3 | 66.7% |
| CoreTypes | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Dev Tools | 2 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Filters | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| IOGraphics | 2 | 1 | 0 | 0 | 2 | 0 | 0 | 2 | 100.0% |
| Libsystem | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Mail Addressing | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| MetricKit | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| NSDocument | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 6 | 33.3% |
| Passwords | 2 | 2 | 0 | 0 | 0 | 1 | 0 | 9 | 22.2% |
| Photos | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 19 | 10.5% |
| PrintCenter | 2 | 1 | 0 | 0 | 2 | 0 | 0 | 2 | 100.0% |
| Privacy | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| RepairKit | 2 | 2 | 0 | 0 | 0 | 0 | 2 | 2 | 100.0% |
| Siri Suggestions | 2 | 2 | 0 | 0 | 0 | 2 | 0 | 2 | 100.0% |
| Symptom Framework | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 10 | 20.0% |
| UIKit | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 14 | 14.3% |
| Video Playback | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Xcode IDE | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| bash | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| iTunes Store | 2 | 2 | 0 | 0 | 0 | 0 | 2 | 2 | 100.0% |
| otool | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| App Protection | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| AppKit | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 9 | 11.1% |
| Apple Music Classical | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| AppleFDEKeyStore | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| AppleScript | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 1 | 100.0% |
| Call History | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 13 | 7.7% |
| Disk Management | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| FaceTime | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 14 | 7.1% |
| Find My | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 19 | 5.3% |
| FrontBoard | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 6 | 16.7% |
| GNU | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| GPUTools | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| GarageBand | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| IDE Assets | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| IDE CoreML | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Keynote | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Media Session | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Music | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 10 | 10.0% |
| NSSpellChecker | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 11 | 9.1% |
| Notifications | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 1 | 100.0% |
| Pages | 1 | 1 | 0 | 0 | 1 | 0 | 0 | 1 | 100.0% |
| Playgrounds | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| Python | 1 | 1 | 1 | 0 | 0 | 0 | 1 | 1 | 100.0% |
| RPAC | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 5 | 20.0% |
| SDK | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| Settings | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Setup Assistant | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 3 | 33.3% |
| Status Bar | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 5 | 20.0% |
| System Extensions | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Vim | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 9 | 11.1% |
| Virtualization | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| WidgetKit | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| iTunes | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| lldb | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| **合计** | 3345 | 页列合计 2470 | 425 | 70 | 106 | 310 | 154 | 这 273 名 Impact 全集 6726 段（全树 7,434 的 90.5%） | — |

---

**规则：** 本篇所有数字由 `_tmp/gb127.pl`（`_tmp/s127.pl` ＋ `_tmp/genbody2.pl`）在同一 `%B` 上产出，判据一律以词界与字段名写出，可用第 17 章逐步复算；分母一律注明为 9,136（全树段）、7,434（含 Impact 段）、9,119（本篇所在 241 页全部段）、9,045∶91（域 A∶域 B）、6,726（附表 F 273 名 Impact 全集）；**3,345 是两字段主集、3,347 是三字段并集、3,393 是句次、3,317 是 Description 句次，四个数不可互换**；附表 A「∩主集」列一律按三字段口径，故 `check* ∪ validat*` 行写 3,347 而正文写 3,345；附表 B、C、F 的页列与名列是逐行去重后的再求和，不得当作并集去重值；凡"A 与 B 得数相同"处必须同时给出 |A|、|B|、|A∩B|（第 3.4、7.2、11.2 章）。

**边界声明：** 本篇是**公开英文与中文文本的措辞统计**，不是脆弱性评价、不是可利用性研究，不提供任何规避方法、利用代码、样本、绕过链路或针对他人设备操作步骤与时间序列。文中"规避检测或安全控制"一律指**公开条目使用了 `check*`／`validat*` 这组控制词、或把某项检查自述为被绕过这一文本事实**，不推断某项控制是否真的可被规避、不推断产品是否具备检测能力、不推断其前提与可达性是否成立、不推断是否被利用；**3,345 段、880 个编号、23 个检查对象桶、273 个组件名、76 段 Impact 侧条目**只描述文本分布，不构成任何组件安全性结论、缺陷优先级排序或可利用性评估。

Telegram: https://t.me/one00190
