@@TITLE@@iOS 绕过缓解机制类弱点的公开措辞口径分析
@@SUBTITLE@@选题是"规避某种漏洞缓解机制（bypass a mitigation）"这一类后果。逐词测量的结果是：**`bypass` 是措辞层五篇里唯一一个"词性由字段决定"的类型词**——写在 **Impact 侧 291 段**、写在 **Description 侧 31 段**、写在**致谢侧 0 段**，三字段并集 **301 ＝ 仅 Impact 270 ＋ 仅 Description 10 ＋ Impact∩Description 21**（三个含致谢的组合全为 0），占全树 9,136 段的 **3.29%**、占含 Impact 的 7,434 段的 **4.05%**，覆盖 **114／257 页（44.36%）**与 **58／472 名（12.29%）**。而两处的语法职能完全相反：Impact 侧 291 段 **291 段全是动词用法、且 291 段全带情态动词**，名词形「bypass of／to／around」在 Impact **0 段**；Description 侧 31 段 **31 段全是名词化修复句**（`A file quarantine bypass was addressed with additional checks.` 27 段＋`A Content Security Policy bypass was addressed with improved enforcement in AudioWorklet contexts.` 4 段），动词式在 Description **0 段**。第二个发现是**派生形全零**：`bypasses`、`bypassed`、`bypassing`、`by-pass` 四形在三个字段**一次都没出现**，所以「词族 `bypass*`」「单词界 `bypass`」「无右界子串 `bypass`」三种刀给出的并集**都是同一个 301**。第三个发现是**被绕过对象全部是具名机制**：27 把对象刀在 Impact 侧**段级无重叠加闭合 291 段**，最大三桶是 Privacy preferences **91**、Gatekeeper **53**、kernel memory protections **23**；`Gatekeeper` 在全树被提到 **53** 段、`Same Origin Policy` **10** 段、`Pointer Authentication` 全称 **17** 段，**三段配 bypass 的比例都是 100%**。第四个发现是**这一类与"在野"两个口径的关系截然相反**：`Apple is aware of a report`（全树 103 段）与本篇交 **20 段**，而 `actively exploited`（全树 18 段）与本篇交 **0 段**——这 20 段只由**两条句子**承载、只落在 **Kernel 8／RTKit 8／RPAC 4** 三个组件名上。
@@INFO@@报告日期|2026 年 10 月 30 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（258 份文件排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 9,132 段含致谢**。绕过集 BP ＝ `bypass`（词界后接任意后缀的 `bypass*` 词族，实测与单词界、与无右界子串三者同集）以子串出现在 Impact、Description、致谢任一字段 **＝ 301 段 / 114 页 / 58 名**，其中 Impact 侧 291 段、Description 侧 31 段、致谢侧 0 段；字段组合 **仅 I 270／仅 D 10／I∩D 21／仅 A 0／I∩A 0／D∩A 0／三重 0**。对照集：**R** ＝ #118 的远程触达集（Impact 侧 11 个通道词族）**843 段 / 178 页 / 72 名**；**WO** ＝ #119 的无前提集（Impact 含 `without` 词界）**178 段 / 97 页 / 37 名**；**E** ＝ #114 的在野集（Impact 含 `exploit` 词族）**108 段 / 76 页 / 12 名**；**G／HI** ＝ #116 与 #120 的动名词开头集 **2,179 段** 与交互触发集 **273 段 / 102 页 / 35 名**；**M2** ＝ #121 的同段 ≥2 后果类集 **185 段 / 82 页 / 35 名**；**UU** ＝ Impact 含 `user`（词界）**1,627 段 / 175 页 / 200 名**；**RC** ＝ #122 的竞态集（Description 含 `race condition`）**232 段 / 95 页 / 40 名**；**L** ＝ #123 的逻辑集（`logic` 词界三字段）**550 段 / 162 页 / 81 名**；**Ds** ＝ #124 的拒绝服务集（`denial of service` 三字段）**470 段 / 155 页 / 62 名**；**X** ＝ #125 的泄露集（`disclos*` 三字段）**427 段 / 141 页 / 54 名**。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或利用链构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**"bypass 这一后果词在公开英文文本的哪个字段、哪一句位、以哪一种词性出现，与被绕过宾语、与具名缓解机制、与页面修复套话、与远程／在野标注之间的集合关系**，属于措辞与文本计量分析；文中任何"绕过缓解机制"表述均指**公开条目把影响自述为 bypass 某对象这一文本事实**，不是对该缺陷是否真的推翻了某项保护、绕过难度多大、需要什么前提的判断，也不构成任何机制是否可被绕过的结论
@@INFO@@分类|判据**六组 87 项 / 三字段全零 8 项 / 非零 79 项 / Impact 列合计 14,242**（附表 A，六组：绕过本体词 6、被绕过对象 27、机制名对照 19、修复套话词 15、外部集对照 12、后果强度八类 8）/ **四十四条含 bypass 整句 × 对象桶 × 句次 × I∶D × 页 × ∩R ∩E ∩M2 ∩L ∩RC**（附表 B，45 行含合计，句次合计 **322 ＝ Impact 291 ＋ Description 31**、页列合计 282）/ **二十六条 Impact 句主语（施事短语）× 句次 × 段页名 × ∩R ∩E，续表五把形态刀**（附表 C，27 行含合计，施事句次闭合 **291**；续表 **名词化 31／主干 280／能力式 241／直陈式 50**）/ **页面、编号、致谢、版式与字段落位五组分箱**（附表 D，23 行，页分箱闭合 114、编号分箱闭合 301、致谢分箱闭合 301）/ **中文层 88 词**（附表 E，八组：绕过本体 7、被绕过对象（机制名）20、认证与凭据 8、保护机制 12、检查与修复 9、施事与载体 12、类型与后果 10、范围与程度 10；**全零 26 词、非零 62 词**）/ **组件层 58 名**（附表 F，59 行含合计，段合计 301、这 58 名的 Impact 全集 **3,226** 段、占全树含 Impact 段的 **43.4%**）/ **八把刀**：字段刀（Impact 291 ∶ Description 31 ∶ 致谢 0，六组互斥拆分 270／10／21／0／0／0／0）、词形刀（`bypass*` ≡ 单词界 ≡ 无右界子串，四个派生形全零）、词性刀（Impact 291／291 动词＋情态 ∶ Description 31／31 名词化，两侧交叉各 **0**）、对象刀（27 把刀段级闭合 **291**、句级闭合 **322**）、机制名配比刀（Gatekeeper 53／53、SOP 10／10、PAC 17／17 全 100%，CSP 46 段里只 4 段）、缺席刀（SIP／CFI／PAC 缩写／Team ID／secure boot／XPC／TCC 文本层全零；Lockdown 10、USB 受限 5、Data Protection 43、AirPlay 7 零共段）、修复套话反向刀（`additional checks` 34 段里 27 段属本篇、`improved enforcement` **4 段全部属本篇**）、在野刀（`Apple is aware of a report` 交 **20**、`actively exploited` 交 **0**）
@@INFO@@难度|进阶（第 3 章的 301 段有**三套分母**：占全树 9,136 段是 **3.29%**、占含 Impact 的 7,434 段是 **4.05%**、占其所在 114 页全部 7,020 段是 **4.29%**，三个数都真、不可混用；第四套是**版式域**分母——域 A **298／9,045 ＝ 3.29%**、域 B **3／91 ＝ 3.30%**，两域密度**相同**，这与 #125 域 B 三倍于域 A 的情形正好相反，因为那三倍的来源（页面模板句）在本篇不存在（致谢侧 0 段）；第 6 章的 **291 是段级口径、322 是句级口径**，同一批 301 段被 27 把对象刀在字段级命中 **318** 次、在句级命中 **322** 次，三个数不可互换；`BYP` 类在附表 A 的 Impact 列是 **291**、在 ∩BP 列是 **301**（并集口径），与 #124 的 `DOS` 470∶459、#125 的 `DIS` 427∶423 是同一类口径差；**附表 A「外部集对照」行的 ∩BP 列一律按三字段并集口径**，所以 `远程可达 R` 行写 **43**、而正文第 14 章的 Impact 口径是 **42**，差的 1 段是页 `149034` 致谢栏里的研究者单位名 `Mobile & Wireless`；附表 B、C、F 的页列与名列是逐行去重后的再求和，**不得当作并集去重值**；凡"两个集得数相同"处必须同时给出 |A|、|B|、|A∩B|（第 6、11、15 章））
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

**"规避缓解机制"是后果层里最容易被读成"某项保护被推翻了"的类型词，但公开文本里 bypass 落在哪个字段、是动词还是名词、宾语是不是一个具名机制、有没有标注在野——这些都是可以逐词测的。** 本篇把这句选题拆成四个互相独立的断言，逐个测量：

**断言一（存在这一类，且两个字段都有）**：公开条目里确实有把影响自述为 `bypass X` 的文本。**测法**：`bypass*` 词族分三个字段匹配。**结果**：成立，共 **301** 段；但**致谢侧 0 段**，所以它是措辞层五篇里唯一"两字段有、第三字段空"的形态——Impact 侧 **291**、Description 侧 **31**、致谢侧 **0**；六组互斥拆分 **仅 Impact 270、仅 Description 10、I∩D 21，其余三组与三重全 0**（第 3 章）。

**断言二（`bypass` 与 `规避／绕过` 是同义互换，词形会铺开）**：直觉上一个常用动词会有第三人称单数、过去式、进行时等形。**测法**：逐形计数 `bypasses`／`bypassed`／`bypassing`／`by-pass`，并把词族刀、单词界刀、无右界子串刀三口对齐。**结果**：**不成立**。**四个派生形在三字段一次都没出现（全 0）**，三口径给出的并集**同为 301**（第 4 章）。也就是说 `bypass` 在这套语料里**只有一个裸词形**，任何"我按同义词表搜到的 0"都必须先声明搜的是哪个确切词形。

**断言三（两个字段说的是同一件事、同一种说法）**：31 段 Description 侧命中看起来像 31 个条目的补充说明。**测法**：把两侧的语法职能分别测——名词化形（`bypass of／to／around`、`X bypass was addressed`）与动词形（情态动词＋`bypass`）。**结果**：**不成立，且方向相反**。**Impact 侧 291 段 100% 是"施事＋情态动词＋bypass＋对象"的动词结构，名词化形 0 段；Description 侧 31 段 100% 是名词化修复句，动词式 0 段**（第 5、10、11 章）。同一个词在两个字段是**两种词性**，这是本篇对措辞层方法学的核心补充：按"两列词"处理 `bypass` 会造出一条并不存在的 Description 侧动词曲线。

**断言四（绕过类条目会标注在野利用）**：绕过缓解机制常被读成"这条一定有真实利用"。**测法**：与两个在野口径、与远程集、与无前提集求交。**结果**：**一半成立一半不成立**。`Apple is aware of a report`（全树 **103** 段）∩ 本篇 ＝ **20** 段，占该集的 **19.4%**，而本篇只占全树 3.29% —— 绕过条目带在野声明的概率是全树平均的 **5.9 倍**（20／301 ＝ 6.65% 对 103／9,136 ＝ 1.13%，比值 5.89）。但更强的口径 `actively exploited`（全树 **18** 段）与本篇**交 0 段**，那 18 段全部落在 `CoreMedia`（8）、`JavaScriptCore`（5）、`WebKit`（5）三个名上，一段不是 bypass（第 13 章）。

**所以本篇的产出是一条"两字段有落位但词性互相绑定、对象全是具名机制、四个派生形全零、在野声明高发而最强在野口径为零"的结论**：`bypass` 在这套语料里是一套**动词后果句 ＋ 名词化修复句**的组合，它以 44 条固定句子中的 42 条出现在 291 段的 Impact、以另外 2 条出现在 31 段的 Description，**与拒绝服务集交 0 段、与泄露集交 0 段、与 `without` 集交 0 段**——第 22 章纠正会把 301 段读成"301 次保护被绕过"的十种误读。

# 1 操作性定义与分母

**1.1 语料与切块。** 与 #114–#125 同一镜像、同一切块口径：块起于 `<h3 class="gb-header">`（域 A）或粗体段（域 B），块止于**四锚点**（下一块起点、页脚免责声明、版权行、脚本行），缺一锚点就会把页面免责声明切进字段文本。全树 **9,136 段 / 257 页 / 472 名**；含 Impact **7,434** 段；含致谢 **9,132** 段。域 A **9,045 段 / 246 页 / 470 名**；域 B **91 段 / 11 页 / 26 名**，页为 `118479`、`120303`、`120304`、`120305`、`120307`–`120312`、`120339`（**不含 `120306`**，该页仍是 gb-header 版式）。本篇的 301 段里有 **3** 段落在域 B（第 17 章）。

**1.2 BP 集（本篇主集）。** 判据：`bypass` 起头、词界后接任意后缀，出现在 Impact、Description 或致谢任一字段；大小写不敏感。**BP ＝ 301 段 / 114 页 / 58 名**。字段落位 **Impact 291 ∶ Description 31 ∶ 致谢 0**；两两交集 **I∩D ＝ 21、I∩A ＝ 0、D∩A ＝ 0、三重 ＝ 0**，故六组互斥拆分 270 ＋ 10 ＋ 21 ＋ 0 ＋ 0 ＋ 0 ＝ **301**（第 3 章）。这 301 段**全部含 Impact 字段**（Impact 为空的 0 段）——所以本篇的分母与"含 Impact 的 7,434 段"可直接相除得 **4.05%**。

**1.3 三套分母与第四套版式分母。** 301／9,136 ＝ **3.29%**；301／7,434 ＝ **4.05%**；301／7,020 ＝ **4.29%**（7,020 是这 114 页的全部段数，平均每页 61.58 段、本篇每页 2.64 段）。版式域：域 A **298／9,045 ＝ 3.29%**、域 B **3／91 ＝ 3.30%**，**两域同密度**。这一条与 #125 正相反：#125 的域 B 密度是域 A 的三倍，而三倍完全由该页面上的模板句造成；本篇致谢侧 0 段，没有可造成该效应的句子，所以版式域对本篇**不是一个需要讨论的变量**。

**1.4 五篇措辞层的类型词对照。** #122 `race condition` 是**定义位单字段词**（Impact 0／Desc 232）；#123 `logic` 是**三字段并集词**（Impact 10 ∶ Desc 540 ∶ 致谢 0，两非零字段互斥）；#124 `denial of service` 是**并集词、470 段**；#125 `disclos*` 是**三字段全有、且第三字段全是模板句、427 段**；本篇 `bypass` 是**两字段有、第三字段空、且两侧词性相反、301 段**。五个类型词给出**五种不同的字段形态**，说明"类型词都可以按同一套三字段并集处理"这个默认做法在每一篇都要重新验证。

# 2 结论速览（每条都可按第 21 章复算）

**2.1 规模。** BP **301 段 / 114 页 / 58 名**；Impact **291**／Desc **31**／致谢 **0**；仅 I **270**、仅 D **10**、I∩D **21**。页覆盖 **114／257 ＝ 44.36%**，名覆盖 **58／472 ＝ 12.29%**。

**2.2 词形。** `bypasses`／`bypassed`／`bypassing`／`by-pass` **四个派生形在三个字段全 0**；`bypass*` 词族、`bypass` 单词界、`bypass` 无右界子串三种刀**同为 301**。附表 A 前四行（绕过本体词组）因此给出**同一个 291／301**。

**2.3 词性随字段绑定。** Impact 侧名词化（`bypass of／to／around`）**0 段**、"to／may／can／could／would／will／able／designed／attempt" 前置的动词式 **291／291 段**；附表 C 用「句中第一个情态动词之前」切主语，得 **26 条**主语短语、句次**闭合 291**，**没有「(无情态动词)」这一行**。Description 侧 **31／31** 段是 `A X bypass was addressed …` 名词化打头，动词式 **0**（第 5、9、10 章）。

**2.4 对象。** 27 把对象刀在 Impact 侧**段级相加恰为 291、无一段落进两桶**；在句级相加为 **322**（＝ I 291 ＋ D 31）；字段级三口径下的并集列相加为 **318**。最大五桶：Privacy preferences **91**、Gatekeeper **53**、file quarantine（名词化，只在 Desc）**27**、kernel memory protections **23**、Pointer Authentication **17**（第 6 章）。

**2.5 具名机制的 100% 与低配比。** 全树提到 `Gatekeeper` 的 **53** 段**全部**属本篇；`Same Origin Policy` **10** 段全部；`Pointer Authentication` 全称 **17** 段全部；`enforcement` 词族 **9** 段全部；`improved enforcement` **4** 段全部。反例同样明确：`Content Security Policy` 全树 **46** 段里只有 **4** 段配 bypass（**8.7%**）；`privac*` 词族 **348** 段里 **91** 段（**26.1%**）；`check(s)` 泛称 **2,017** 段里 **108** 段（**5.4%**）（第 7 章）。

**2.6 缺席。** 文本层**全零**：`SIP`、`control flow integrity／CFI`、`PAC` 缩写、`address space layout randomization` 全称、`secure boot`、`Team ID`、`XPC`、`TCC` 八项在三个字段的出现段数都是 **0**。**有词但零共段**：`Lockdown` 锁定模式 **10** 段、`USB Restricted` **5** 段、`Data Protection` **43** 段、`AirPlay` **7** 段，各与本篇交 **0** 段（第 8 章）。

**2.7 在野与可达。** ∩`Apple is aware of a report` ＝ **20 段**，由**两条**句子承载（16＋4），只落在 **Kernel／RTKit／RPAC** 三个组件名、**15** 页；∩`actively exploited` ＝ **0**；∩`exploit*` 词界 ＝ **20**（与前者同一批）。∩R 远程 ＝ **42**（附表 A 并集列 **43**，差 1 段来自致谢单位名）；∩WO ＝ **0**；∩UU（Impact 含 `user`）＝ **8**；∩G 动名词开头 ＝ **10**；∩HI 交互触发 ＝ **0**（第 13、14、15 章）。

**2.8 后果强度八类。** `BYP` 类 Impact **291**／并集 **301**；与本篇相交**非零**的只有 `MEM` **16**、`PRI` **3**、`SBX` **29** 三类，`ACE`、`DIS`、`DOS`、`FULL` 四类**各交 0 段**。同段双类 **16** 段，类对只有两种：**BYP＋SBX 13** 与 **BYP＋PRI 3**（全树共 7 种类对，本篇占 2 种）（第 16 章）。

**2.9 编号与致谢。** 301 段**全部**带 CVE 编号（301／301），distinct 编号 **93** 个、**93 个全部为本篇独占**、与他集共享 **0** 个；致谢人次 **441**、每段均值 **1.47**、最大 **5**（全树 1.80／37）（第 17 章）。

**2.10 组件层。** 58 个组件名覆盖 301 段；这 58 名的 Impact 全集 **3,226** 段，占全树含 Impact 段的 **43.4%**，本篇在其内占 **9.33%**。附表 F 的占比列有 **9 行是 100.0%**（第 18 章）。

**2.11 中文层。** zh-CN 手册 **178** 页可用：`绕过` **23** 次／**9** 页（最集中"激活锁安全性"12 次）、`规避` **1** 次／1 页、`跳过` **2** 次／1 页；`逃逸`、`越过`、`绕开`、`突破`、`Gatekeeper`、`沙箱`、`隐私偏好`、`启动约束`、`同源策略`、`内容安全策略`、`点击劫持`、`检疫`、`网络过滤器`、`USB 受限模式`、`凭据`、`提权`、`越权`、`竞态`、`内存破坏`、`完全控制`、`远程攻击者`、`ZIP`、`JAR`、`检查项`、`额外检查`、`加固` 在本组的 26 个词位上**全零**；对照的机制词反而密集：`门禁` **218** 次、`沙盒` **27** 次、`代码签名` **394** 次、`激活锁` **245** 次、`锁定模式` **189** 次、`配对` **699** 次（第 19 章）。

# 3 字段刀：Impact 291 ∶ Description 31 ∶ 致谢 0，六组互斥拆分 270／10／21

**3.1 三字段落位。** `bypass*` 命中 Impact **291** 段、Description **31** 段、致谢 **0** 段。**致谢侧 0 段是本篇与 #125 的分界点**：#125 的 `disclos*` 在致谢侧命中 11 段，而那 11 段一段都不是条目文本、全是页面免责声明模板；本篇在致谢字段一次都没有命中，所以不存在需要剔除的模板污染，域 B 密度也因此与域 A 相同（第 1.3 节）。

**3.2 六组互斥拆分。** 仅 Impact **270**、仅 Description **10**、Impact∩Description **21**、仅致谢 **0**、Impact∩致谢 **0**、Description∩致谢 **0**、三重 **0**。270 ＋ 10 ＋ 21 ＝ **301**，与并集闭合。

**3.3 I∩D 的 21 段是什么。** 这 21 段是"影响句写动词、修复句写名词"的同一条目，两句都用 `bypass` 但**宾语换了词面**：按对象桶归组，**17 段是 Impact 侧 `Gatekeeper` → Description 侧 `quarantine`**，**4 段是两侧都是 `Content Security Policy`**。这 17 段的 Description 文本正是那句 27 段里的 `A file quarantine bypass was addressed with additional checks.`（其余 10 段的 quarantine 句只在 Description 侧命中）。**结论：苹果对同一项 macOS 文件隔离属性在影响句用产品名 `Gatekeeper`、在修复句用底层属性名 `quarantine`**，两侧不构成同词复述，统计时必须按字段而不是按条目去重。

**3.4 得数相同不等于同一组。** 附表 A 的 `BYP 类` 行给出 Impact **291**、三字段并集 **301**、∩BP **301**，与本集的 291／301 完全相同——因为 `BYP` 类的判据就是 `(?<![\w-])bypass` 出现在 Impact，**判据相同所以集合相同**，这是一个恒等式而非发现。真正需要 |A|、|B|、|A∩B| 三个数的是第 13、14、16 章那种跨集合场景。

# 4 词形刀：四个派生形全零，三种刀同一个 301

**4.1 逐形计数。** 在三个字段分别测：`bypass` **I 291／D 31／A 0／并 301**；`bypasses` **0**；`bypassed` **0**；`bypassing` **0**；`by-pass`（连字符形）**0**。

**4.2 三种刀的对照。** `bypass*`（词族，左界＋右界）**301**；`bypass` 单词界（左界＋右界）**301**；`bypass` 仅左界的子串形（可吞掉 `bypassed`）**301**。三者**两两相等**，原因即 4.1：该词没有任何后缀形，所以右锚与无右锚给同一个集合。这与 #123 的 `logic`（词界 ≡ 子串，差 0）同形，与 #112 的 `process*`（`processing` 是准入动词、不是名词复数）相反。

**4.3 方法学后果。** 任何"同义词表遍历"在这套语料上必然得 0。要研究绕过类措辞，只能声明**确切词形**是裸词 `bypass`；`circumvent`、`evade`、`work around`、`disable` 四个英文近义词在本集**一次都没与它们共现**，因为本篇主集就是按 `bypass` 一个词定义的（这是定义选择，不是语料结论）。

# 5 词性刀：Impact 全是动词＋情态，Description 全是名词化，交叉各 0

**5.1 Impact 侧。** 名词化检测（`bypass` 后接 `of／to／around`）**0 段**；动词检测（`bypass` 前有 `to／may／can／could／would／will／able／designed／attempt／tries／trying`）**291／291 段**。两种检测的"两者都不"残差 **0 段**。

**5.2 情态动词的存在性。** 附表 C 的主语切法是「句中第一个情态动词（may／can／will／is／was／could／might）之前的全部内容」。若某条含 bypass 的 Impact 句没有情态动词，切法会产出哨兵行「(无情态动词)」。**附表 C 的 26 行里没有这一行**，且 26 行句次闭合 **291**。所以**每一条 Impact 绕过句都带情态动词**，这是一个可证伪的断言，实测为真。

**5.3 Description 侧。** 31 段全落在两条句子上（第 11 章），两条都是 `A X bypass was addressed …`：**`bypass` 在这里是名词中心语，被 `A … bypass` 限定，后接 `was addressed`**。动词式检测在 Description 侧 **0／31**。

**5.4 两列词的陷阱。** 如果把 `bypass` 当作"两列都写的词"去做 Impact∶Description 对比（例如"绕过在影响句里的频率 vs 在定义句里的频率"），会把 **291 段动词** 与 **31 段名词**放进同一个分布。这两批段落的语法职能不同、位置不同、句式不同，任何比值都不是同一类量的比值。按 #123 的同类教训，正确写法是**分别报告两个字段各自的形态闭合**：Impact 291 ＝ 26 条主语短语 ＋ 280 主干 ＋ 10 条 `Processing` 打头 ＋ 1 条 `On MDM managed devices` 打头；Description 31 ＝ 27 ＋ 4。

# 6 对象刀：27 把刀段级闭合 291、句级闭合 322

**6.1 三种口径的三个数。** 同一批 301 段：段级字段匹配（Impact 字段里出现该对象词）27 桶相加 ＝ **291**；句级归桶（把每条含 bypass 的整句归入唯一一个桶）27 桶相加 ＝ **322**；字段级三字段并集列相加 ＝ **318**。**三个数都对，分别是"段"和"句"和"字段并集"三种口径**，本篇正文与附表按口径分别标注，不得互相替换。段级闭合 291 意味着**每一条 Impact 段的 bypass 宾语只落进 27 桶之一**（无重叠、无遗漏），这是本篇刀工的主要自检。

**6.2 27 桶的句级分布（附表 B 桶列再汇总，合计 322）。** 91／53／27／23／17／13／12／12／10／8／8／7／7／6／6／5／3／2／2／2／2／1／1／1／1／1／1。其中 **`file quarantine（名词化）27 段全部在 Description 侧`**，`Content Security Policy` 一桶是**混装**：句次 8 ＝ Impact 4 ＋ Description 4；其余 25 桶**只在 Impact 侧**。

**6.3 头部集中度。** 前四桶 91 ＋ 53 ＋ 27 ＋ 23 ＝ **194**，占句次 322 的 **60.2%**；前九桶 **269**，占 **83.5%**；尾部 **9 桶各 1–2 段**（Activation Lock、IPSec authentication、browser extension authentication、the Lock Screen、Apple Intelligence security prompts、Digital ID authentication、Login Window、clickjacking protections、pairing、sender policy checks —— 其中六桶为 1 段）。

**6.4 宾语是"具名机制"而不是"泛称"。** 27 桶里**没有一桶**是 `security checks`、`protections`、`restrictions` 之外的抽象泛称，而 `Privacy preferences`、`Gatekeeper`、`kernel memory protections`、`Pointer Authentication`、`Internet permission requirements`、`Same Origin Policy`、`code signing enforcement`、`launch constraint protections`、`Content Security Policy` 都是可直接对应到一项具名保护的名称。这条观察在附表 A 的"机制名对照"组里被反向量化（第 7 章）。

# 7 机制名配比刀：五个 100% 与三个反例

**7.1 五个 100%。** 附表 A 给出「全树出现段数 ∶ 其中属本篇」：**`Gatekeeper` 53／53**、**`Same Origin Policy` 10／10**、**`Pointer Authentication`（全称）17／17**、**`enforcement` 词族 9／9**、**`improved enforcement` 4／4**。这五项在全树被点名时，**每一次都是在讲绕过**。

**7.2 三个反例。** `Content Security Policy` **46** 段里只 **4** 段配 bypass（8.7%）；`privac*` 词族 **348** 段里 **91** 段（26.1%）；`sandbox` 词族 **475** 段里 **29** 段（6.1%）；`check(s)` 泛称 **2,017** 段里 **108** 段（5.4%）；`protection(s)` 泛称 **442** 段里 **27** 段（6.1%）。**泛称词的绝对量大、配比低；具名产品名的量小、配比为 100%**——所以"绕过类条目很多"这个印象来自泛称词，而实际自述为绕过的是具名机制。

**7.3 `quarantine` 与 `Gatekeeper` 是同一对象的两套词面。** 附表 A：`quarantine` Impact **4** 段／三字段并集 **31** 段／∩BP **27**；`Gatekeeper` **53／53／53**。结合第 3.3 节的 17 段跨字段映射，可以确认：**影响句用 `Gatekeeper`，修复句用 `quarantine`**，两个词面在描述同一个 macOS 文件隔离属性时被分别使用。把它们当作两个对象统计会把同一批条目拆成两桶，把 53 与 27 相加则会把 17 段重计一次。

**7.4 `Data Protection` 只在非 Impact 字段。** 附表 A 该行 Impact 段 **0**、三字段并集 **43**、∩BP **0**：**全树没有任何一条 Impact 句写 `Data Protection`**，这 43 段全部在 Description 或致谢字段。这是一个纯字段分布事实，不表示该机制不在 iOS 上存在。

# 8 缺席刀：八项文本层全零与四项零共段

**8.1 文本层全零八项。** 在三个字段都测 0 出现段数：`System Integrity Protection／SIP`、`control flow integrity／CFI`、`PAC`（作为独立缩写）、`address space layout randomization`（全称）、`secure boot`、`Team ID`、`XPC`、`TCC`。

**8.2 名字层与文本层的区别（`TCC` 是最好的例子）。** `TCC` 作为**组件名**（块标题）出现在附表 F 且该名的 BP 段为 **4**；但 `TCC` 作为**文本词**在 imp／des／ack 三字段 **0 段**（`MK()` 只扫三个文本字段、不扫 `nm`）。所以"F 表里有 TCC、A 表里 TCC 全零"**不矛盾**，是两套位置。**任何"某缩写没被提到"的结论都必须先说明是名字位还是文本位。**

**8.3 四项零共段。** `Lockdown 锁定模式` **10** 段、`USB Restricted Mode` **5** 段、`Data Protection` **43** 段、`AirPlay` **7** 段——各自在全树都有出现，但**与本篇交 0 段**。这意味着苹果在讲锁定模式、USB 受限模式、数据保护、AirPlay 的条目里**从未把影响自述为 bypass**。这四项的条目用的是别的后果词（`AirPlay` 的 7 段与 `Apple is aware of a report` 交 0、与 `logic` 集交 **7**）。

**8.4 布局随机化的两种词面。** ASLR 桶 **12** 段（`An app may be able to bypass ASLR` 11 ＋ `bypass kASLR` 1），但 `address space layout randomization` 全称 **0 段**——缩写是唯一词面，与中文层"地址空间布局随机化"5 次／2 页形成对照（第 19 章）。

# 9 施事刀：26 条主语短语闭合 291

**9.1 闭合。** 附表 C 按"句中第一个情态动词之前"切 Impact 句主语，得 **26** 条不同短语，句次相加 **＝ 291**（与 Impact 侧段数逐段对齐，因每条这类句子一段一句）。

**9.2 头部。** `An app` **124** 段／75 页／31 名；`A shortcut` **29**／21 页；`A malicious application` **17**；`An attacker with arbitrary kernel read and write capability` **16**；`A maliciously crafted ZIP archive` **10**；`Processing maliciously crafted web content` **10**；`A remote attacker` **9**；`An attacker with arbitrary read and write capability` **9**；`A malicious attacker with arbitrary read and write capability` **8**；`An archive` **7**；`An attacker on the local network` **7**；`An attacker that has already achieved kernel code execution` **7**；`A user` **6**；`A maliciously crafted app` **5**；`A maliciously crafted disk image` **4**；`A website` **4**；余下 10 条各 **1–3** 段。

**9.3 七类归类与一个分类副作用。** 按短语词面归类：**attacker 类 8 条**、**恶意物 类 10 条**、**user/person 类 3 条**、**app 类 1 条**（只有裸 `An app`）、**shortcut 类 1 条**、**website 类 1 条**、**其他 2 条**（`On MDM managed devices, an app` 与 `An Automator Quick Action workflow`），合计 26。**副作用要写明**：`A malicious attacker with arbitrary read and write capability`（8 段）因为以 `a malicious` 打头被**先**收进"恶意物类"，所以 attacker 类是 8 条而不是 9 条；`Processing maliciously crafted web content`（10 段）因含 `crafted` 也进了恶意物类，"动名词类"因此为空。**这是分类顺序造成的归属，不是语料事实**，引用类别计数时必须连同优先级规则一起给。

**9.4 施事与外部集的对应。** `An attacker with arbitrary kernel read and write capability` 的 16 段**全部**与 `exploit*` 交（∩E ＝ 16）；`A shortcut` 的 29 段里 **12** 段与 R 交（`Internet permission requirements` 12 段全在它里面）；`A remote attacker` 9 段 ∩R ＝ 9；`A website` 4 段 ∩R ＝ 4。**逐短语的 ∩R／∩E 见附表 C 最后两列**，页列合计 **217**、名列合计 **68** 是逐行去重后再求和，不得当作并集值。

# 10 谓语刀：能力式 241 对 直陈式 50，主干 280

**10.1 两档谓语闭合。** 附表 C 续表：含 `be able to bypass` 的**能力式**句次 **241**；`may bypass`（其后不接 `be able to`）的**直陈式**句次 **50**；241 ＋ 50 ＝ **291**，与 Impact 侧闭合。也就是**每 6 段里约 5 段写"能够绕过"、约 1 段写"可绕过"**，两档在语义强度上不可互换，但本篇只报告其分布。

**10.2 主干与两条支路。** 「施事 ＋ may（＋ be able to）＋ bypass ＋ 对象」主干 **280** 段／109 页／55 名；两条支路是 `Processing maliciously crafted web content may bypass …`（**10** 段，动名词短语作主语，全部 WebKit）与 `On MDM managed devices, an app may be able to bypass …`（**1** 段，状语前置）。280 ＋ 10 ＋ 1 ＝ **291**。

**10.3 名词化只在 Description。** 续表第一行 `X bypass was addressed …` 打头 **31** 句次／31 段／26 页／9 名，与第 3 章的 Description 侧 31 段完全对齐；该行在 Impact 侧的对应物**不存在**（第 5.1 节 0 段）。

# 11 文本层刀：Impact 42 条闭合 291、Description 2 条闭合 31、44 条句子无一跨字段

**11.1 Impact 全文去重。** 291 段的 Impact **完整文本**只有 **42** 条不同句子；只出现 1 次的有 **8** 条。头部五条款覆盖 45 ＋ 31 ＋ 19 ＋ 16 ＋ 14 ＝ **125** 段（43.0%），前十条覆盖 **200** 段（68.7%）。完整清单的头部是：`An app may be able to bypass Privacy preferences`（45）、`An app may be able to bypass certain Privacy preferences`（31）、`An app may bypass Gatekeeper checks`（19）、`An attacker with arbitrary kernel read and write capability may be able to bypass kernel memory protections. Apple is aware of a report that this issue may have been exploited.`（16）、`A malicious application may be able to bypass Privacy preferences`（14）、`A shortcut may be able to bypass Internet permission requirements`（12）、`A shortcut may be able to bypass sandbox restrictions`（11）、`An app may be able to bypass ASLR`（11）、`A maliciously crafted ZIP archive may bypass Gatekeeper checks`（10）。42 条**逐条段数见附表 B 的 Impact 句列**，合计 291。

**11.2 Description 全文去重。** 31 段只有 **2** 条不同句子：`A file quarantine bypass was addressed with additional checks.`（**27** 段／26 页）与 `A Content Security Policy bypass was addressed with improved enforcement in AudioWorklet contexts.`（**4** 段）。**这两条是本篇全部的 Description 侧命中，没有第三种说法。**

**11.3 44 条句子的字段专属性（闭合）。** 附表 B 的 44 条含 bypass 整句里，**Impact 句列非零的 42 条与 Description 句列非零的 2 条互斥**：42 ＋ 2 ＝ 44，**没有一条句子同时出现在两个字段**。这条与第 3.3 节"同段两字段用不同词面"是一致的——同一条目可以两字段都有 bypass，但两句话的文本不相同。

**11.4 一个句号造成的 44／43。** 44 条里有一对只差**结尾句号**：`An attacker with arbitrary read and write capability may be able to bypass Pointer Authentication`（**5** 段）与其带句号的 **4** 段版本；去掉尾句号后 distinct ＝ **43**。所以"44 条句子"是**切句口径**的产物（`SE()` 以标点为界保留句号，独立条目与"影响句＋在野句"粘在一起的条目会被切成不同文本），**不是 44 种不同说法**。引用时必须给 44 与 43 两个数。

# 12 修复套话反向刀：从修复句反查绕过集

**12.1 四个高配比套话。** `improved enforcement` 全树 **4** 段／本篇 **4**（**100%**）；`additional checks` **34**／**27**（**79.4%**）；`enforcement` 词族 **9**／**9**（**100%**）；`addressed by` **371**／**37**（10.0%）。**`additional checks` 与 `improved enforcement` 是本篇独有的修复说法**——它们在 #123（逻辑）、#124（拒绝服务）、#125（泄露）的并集里占比都远低于此。

**12.2 泛化套话的稀释。** `improved checks` 全树 **1,018** 段里本篇 **49**；`improved state management` **662**／**49**；`improved validation` **367**／**32**；`improved memory handling` **862**／**8**；`improved restrictions` **47**／**0**；`was addressed with` **6,581**／**249**。`improved restrictions` 47 段与本篇**交 0**，说明"改进限制"这一说法苹果用在别的后果类上。

**12.3 反向刀的正确用法。** 附表 A「修复套话词」组的 Impact 列对 `additional checks`、`improved enforcement`、`improved restrictions` 等**全为 0**——因为套话只写在 Description 字段，**修复套话是"定义位"词、不是"影响位"词**，与 #122 的 `race condition`（Definition-only）同理。因此用修复套话去圈"绕过条目"会把 21 段 I∩D 之外的段也圈进来，只能用第 3 章的字段落位口径去对齐。

# 13 在野刀：20 段由两句承载，而 `actively exploited` 交 0

**13.1 两个在野口径给相反结果。** `Apple is aware of a report`（三字段）全树 **103** 段，与本篇交 **20** 段，**这 20 段全部在 Impact 字段**（致谢侧 0）。更强口径 `actively exploited` 全树 **18** 段，与本篇**交 0 段**；那 18 段只落在 `CoreMedia`（8）、`JavaScriptCore`（5）、`WebKit`（5）。所以"绕过类条目更多在野"这句直觉**只对弱口径成立**。**另注**：该短语 **103 ＝ 域 A 93 ＋ 域 B 10**，#109 报的 **93 段／12 个段落名**是域 A 单口径；本篇的 12 名全貌（WebKit 37、Messages 9、CoreMedia 8、Kernel 8、RTKit 8、ImageIO 7、CoreAudio 5、JavaScriptCore 5、dyld 5、Accessibility 4、RPAC 4、CoreGraphics 3）里 `WebKit` 一名的 37 段中 **10 段来自域 B 的粗体版式页**。与本篇相交的 20 段**有 0 段在域 B**，所以两个口径的差不会改变第 13 章的任何交集。

**13.2 20 段的构成。** 只有**两条**在野句子：**16** 段 `Apple is aware of a report that this issue may have been exploited.` ＋ **4** 段 `…may have been exploited in an extremely sophisticated attack against specific targeted individuals on iOS.`。两条都粘在同一条影响句之后，因此它们同时是附表 B 中 **`An attacker with arbitrary kernel read and write capability may be able to bypass kernel memory protections.`（16）** 与 **`An attacker with arbitrary read and write capability may be able to bypass Pointer Authentication.`（4）** 这两行。∩E ＝ 20 由 **2** 条句子承载（附表 B）。

**13.3 组件名与页。** 20 段只落在 **3** 个组件名：**Kernel 8、RTKit 8、RPAC 4**，覆盖 **15** 页。**注意这三名与"在野"重合，但与 `Pointer Authentication` 桶只重合 4 段**：PA 桶 **17** 段的组件名分布是 **WebKit 8、dyld 5、RPAC 4**（覆盖 15 页），其中只有 RPAC 那 **4** 段属上面三名。**反向读更清楚：20 段在野标注 ＝ `kernel memory protections` 桶 16 段 ＋ `Pointer Authentication` 桶 4 段，两桶相加闭合。**所以"绕过＋在野"在这套语料里等价于"内核与协处理器的内存／指针保护条目"，不是一个跨组件现象，也**不等于**"PA 类条目都在野"。

**13.4 情态限定。** `may have been exploited` 全树 **85** 段（本篇 **20**），`has been exploited` **0** 段。**在野声明一律带 `may`**，没有一条写成已然。

**13.5 包含关系。** 附表 A：`exploit*` 词界 **108** 段 ∩ 本篇 **20**；`ITW` **103** ⊂ `exploit*` **108**（ITW∩exploit ＝ **103**）；`actively exploited` **18** ⊂ `ITW` **103**（交 **18**）。三个集合是**层层包含**，但**与本篇的关系只由最弱那一层产生**。

# 14 远程刀：∩R ＝ 42（附表 A 并集列 43），差的一段来自致谢单位名

**14.1 两个数都要给。** Impact 口径 ∩R ＝ **42**（R 是 #118 的 Impact 侧 11 个通道词族集）；附表 A「远程可达 R」行的 ∩BP ＝ **43**，因为该行按**三字段并集**口径统计。差的 **1 段**已定位：页 **`149034`** 的 `Wallet` 条目，Impact 是 `An attacker with physical access to a locked device may be able to bypass Digital ID authentication when iPhone Mirroring is active`（不含任何通道词），Description 是 `This issue was addressed through improved state management.`（不含），**致谢栏里研究者单位名含 `Mobile & Wireless`**——`wireless` 命中通道词族。**结论：致谢字段的人名／单位名会污染通道词族，跨字段口径的 R 不能用作"该条目可远程触发"的证据。**

**14.2 42 段由 8 条句子承载。** 附表 B 里 ∩R 非零的句子只有 **8** 条，其中 `A shortcut may be able to bypass Internet permission requirements`（**12**，全部来自 `A shortcut` 施事）、`A remote attacker may be able to bypass network filters`（**8**）、`An app may be able to bypass network restrictions`（**7**）、`An attacker on the local network may be able to bypass authentication policy`（**7**）、`A website may be able to bypass Same Origin Policy`（**4**）、`An attacker in a privileged network position may be able to bypass IPSec authentication and intercept network traffic`（**2**）、`A malicious Bluetooth input device may bypass pairing`（**1**）、`A remote attacker may be able to bypass sender policy checks and deliver malicious content via email`（**1**）；12＋8＋7＋7＋4＋2＋1＋1 ＝ **42**。

**14.3 远程词族与对象桶重合但不重合。** `Internet permission requirements` 桶 12 段**全部**与 R 交（27 桶里唯一 100% 的一个）；`network filters` 8／8、`network restrictions` 7／7、`authentication policy` 7／7 也全交。但**占比最大**的 `Privacy preferences`（91）与 `Gatekeeper`（53）**一段都不交 R**。所以"绕过类多为远程"只适用于网络权限那一族对象。

**14.4 与无前提集。** ∩WO（Impact 含 `without`）**＝ 0**。301 段里没有任何一段写 `without …`，与 #125（∩WO 也是 0）一致，但本篇还额外有 ∩HI ＝ **0**（交互触发集 273 段）、∩UU ＝ **8**、∩G ＝ **10**。**"绕过不需要用户交互"这句读法在文本层没有依据**：0 段写"无需"，8 段的 `user` 全部是 `A user may be able to …` 的施事位（第 15 章）。

# 15 前提、用户与动名词三把外部刀

**15.1 ∩UU ＝ 8，且只有两句话。** Impact 含 `user`（词界）的全树 1,627 段与本段交 **8** 段，全部落在**两条句子**上：`A user may be able to bypass some web content restrictions`（**6** 段，WebKit，页 120909／120911／120913／120914／120915／120916）与 `A user with physical access to an iOS device may be able to bypass Activation Lock`（**2** 段，iTunes Store，页 125884／126793）。**按 #125 的因果方向纪律**：这 8 段进入 UU 靠的是它们 Impact 里写的 `user` **作施事**（`A user may be able to`），不是作宾语；不能反过来说"绕过影响 8 个用户"。

**15.2 ∩G ＝ 10 与 ∩HI ＝ 0。** 动名词开头集 G（2,179 段）∩ 本篇 **10** 段，**这 10 段与附表 C 的 `Processing maliciously crafted web content` 施事行是同一段**（同为 10、同为 WebKit、同为 SOP 6＋CSP 4 两条句子）：|G∩BP| ＝ |C 行| ＝ 10，且成员一致，因此这是一次**真正的同一组**而非得数巧合。交互触发集 HI（273 段）∩ 本篇 **0**——`Processing` 属于 #112 的"解析"准入动词、被 HI 排除，所以本篇的 10 段动名词全在 HI 之外。

**15.3 ∩L ＝ 45、∩RC ＝ 18。** 逻辑集 L（550）∩ 本篇 **45**，由 **9** 条句子承载；竞态集 RC（232）∩ 本篇 **18**，由 **5** 条句子承载，其中 `A shortcut may be able to bypass sandbox restrictions`（**6**）与 `A malicious attacker with arbitrary read and write capability may be able to bypass Pointer Authentication`（**5**）两条就占 11 段。**这三组数分别来自 #123、#122 的口径，与本篇的对象桶无关**，引用时必须同时给两集大小（45／550、18／232）。

**15.4 与 Ds、X 两集交 0。** 拒绝服务集（470）与泄露集（427）与本篇**各交 0 段**。结合第 16 章的八类口径（`DIS` ∩ 0、`DOS` ∩ 0），**绕过与泄露／拒绝服务是两套完全不交叉的后果自述**：一条条目要么写"绕过某机制"，要么写"泄露／造成拒绝服务"，不会在同一字段写两者。

# 16 后果八类刀：双类只有 BYP+SBX 13 与 BYP+PRI 3

**16.1 八类 ∩ 本篇。** Impact 段数／∩BP：**`ACE` 336／0、`BYP` 291／301（并集口径）、`DIS` 530／0、`DOS` 2,003／0、`FULL` 63／0、`MEM` 220／16、`PRI` 124／3、`SBX` 418／29**。

**16.2 16 段双类的类对。** 在 301 段里同段命中 ≥2 个后果类的 **16** 段，类对只有 **2** 种：**BYP＋SBX 13 段**、**BYP＋PRI 3 段**（全树共有 7 种类对，本篇占 2 种）。3 段 BYP＋PRI 全在 `An app may be able to bypass launch constraint protections and execute malicious code with elevated privileges`（**3** 段）这一句上。

**16.3 `SBX` 的 29 与 13 是两个位置口径。** `sandbox` 词族三字段并集 475 段与本篇交 **29**；但**双类判定只在 Impact 字段内做**，Impact 写 `sandbox` 的 23 段里 bypass 也写在 Impact 的只有 **13** 段。这 29 段拆开是：仅 Impact 写 sandbox **16**、Impact 与 Description 都写 **7**、只在非 Impact 字段写 **6**。**"绕过同时破坏沙盒"的段数是 13，不是 29**——这是 #125 记下的"Impact 定义集合有固定因果方向"那条纪律在本篇的复现。

**16.4 没有 `arbitrary code execution`。** `ACE` 类判据是 `arbitrary code execution`／`execute arbitrary code`，与本篇交 **0**。但第 13 章那 16 段写的是 `An attacker that has already achieved kernel code execution …`／`arbitrary kernel read and write capability`——**内核读／写能力**措辞。**绕过条目从不写"任意代码执行"，它写"已具备读写能力"作为前置条件**，这是一个稳定的句式分工，与 #102（内核状态）与 #117（持久化）的观察一致。

# 17 页、编号与致谢层

**17.1 页分箱闭合。** 114 页里每页的 BP 段数：1 段 **42** 页、2 段 **35**、3 段 **13**、4 段 **5**、5 段 **5**、6–9 段 **12**、10–19 段 **2**；42＋35＋13＋5＋5＋12＋2 ＝ **114**，段数合计 **301**。Top10 页合计 **81** 段（**26.9%**）：`149035`（13）、`120911`（10）、`122373`（9）、`128071`（8）、`128072`（8）、`120909`（7）、`120916`（7）、`149042`（7）、`120910`（6）、`120912`（6）。

**17.2 同页共存与"全绕过页"。** 这 114 页共 **7,020** 段，本篇占 **4.29%**，平均每页 2.64 段。只有 **2** 页的**全部**条目都含 bypass：**页 `120887` 与 `126604`，各 1 段**——也就是说"整页都是绕过条目"的情形只出现在只有一条目的页面上，**不存在多条目全绕过页**。

**17.3 编号层。** 301 段**全部**带编号（301／301）：1 个编号 **290** 段、2 个编号 **11** 段；distinct 编号 **93** 个，**93 个全部只出现在本篇**、与其他任何集共享 **0** 个。**注意编号在致谢字段而不在 `id` 字段**（`$B{$k}{id}` 是页面号），这与 #123 的记录一致。93 个编号覆盖 301 段，说明**同一编号常跨页复用**（版本页序列），不能把"93"读成"93 个组件"或"93 起事件"。

**17.4 致谢层。** 人次 **441**，每段均值 **1.47**、最大 **5**（全树分别 1.80／37）；分布 1 人 **200** 段、2 人 **73**、3 人 **20**、4 人 **5**、5 人 **3**，合计 301。**本篇条目的致谢人数比全树平均少、且远没有全树的最大值**——这与"绕过类条目多由单人报告"这一印象一致，但样本仅到 5 人。

**17.5 版式域。** 域 B 的 11 页共 91 段，本篇 **3** 段（**3.30%**）；域 A **298／9,045 ＝ 3.29%**。**两域同密度**，故本篇不存在 #125 的"页面模板把域 B 密度推高三倍"的假象。若某篇在这里出现倍数差，先查致谢字段是不是模板句。

# 18 组件层：58 名、3,226 段全集与 8 个 100% 行

**18.1 规模。** 58 个组件名覆盖 301 段；这 58 名的 Impact 全集 **3,226** 段，占全树含 Impact 的 7,434 段的 **43.4%**，本篇在其内 **301 段 ＝ 9.33%**。头部：`Kernel` **31**、`Shortcuts` **27**、`WebKit` **24**、`AppleMobileFileIntegrity` **14**、`Sandbox` **11**、`Accounts`／`PackageKit`／`Safari` 各 **9**、`Audio`／`LaunchServices`／`RTKit` 各 **8**。页列合计 **288**、∩R 列合计 **42**、∩E **20**、∩M2 **16**、∩L **45**（逐行去重后求和，非并集）。

**18.2 九个 100.0% 行。** 附表 F 的"BP 占比"列有 **9** 行等于 **100.0%**（该名的 BP 段数＝该名的 Impact 全集段数）：`RTKit`（8）、`CloudAttestation`（5）、`iCloud Photo Library`（3）、`RepairKit`（2）、`Sidecar`（2）、`iTunes Store`（2）、`AppleScript`（1）、`Python`（1）、`Wallet`（1）。**这些是"该组件在本语料里只被写成绕过"的名字**，其中 `RTKit` 与 `CloudAttestation` 的绝对量也非零，最具解释力。另有 `copyfile` **58.3%**、`Sync Services` **50.0%**、`Security Initialization` **50.0%**、`RPAC` **80.0%**、`BOM` **75.0%——小样本组件的高占比要按第 21 章的样本量一起读。

**18.3 大组件的低占比。** `WebKit` 的 Impact 全集 **910** 段，本篇 **24**（**2.6%**）；`Kernel` **681** 段，本篇 **31**（**4.6%**）；`Foundation` **67** 段，本篇 **1**（**1.5%**）；`WindowServer` **32** 段，本篇 **1**（**3.1%**）。**占比最高的不是最大的组件**，所以按"绕过条目数"排组件名会得到与按占比排完全不同的顺序，两套排序都要给。

# 19 中文手册层：`绕过` 23 次／9 页，26 个词位全零

**19.1 本体词。** zh-CN《Apple 平台安全保护》178 页可用窗口内：`绕过` **23** 次／**9** 页（最集中"激活锁安全性"12 次）、`跳过` **2** 次／1 页（自动设备注册）、`规避` **1** 次／1 页（操作系统完整性）；`逃逸`、`越过`、`绕开`、`突破` **全零**。**手册层用"绕过"这一译名的密度比手册层其它机制词低两个数量级**：`门禁` **218** 次、`代码签名` **394** 次、`激活锁` **245** 次、`配对` **699** 次、`锁定模式` **189** 次、`系统完整性保护` **207** 次。

**19.2 对象词的译名缺位。** 被本篇点名的机制在中文层的对应：**`Gatekeeper`、`隐私偏好`、`启动约束`、`同源策略`、`内容安全策略`、`点击劫持`、`检疫`、`网络过滤器`、`沙箱` 全部 0 次**；有译名的是 `沙盒` **27** 次／11 页、`指针认证` **5** 次／3 页、`地址空间布局随机化` **5** 次／2 页、`锁定屏幕` **8** 次／6 页、`登录窗口` **10** 次／6 页、`隔离` **10** 次／6 页。**英文顾问页里 100% 配 bypass 的 `Gatekeeper` 与 `Same Origin Policy` 在中文手册里根本没有对应词条**，所以中英两层不能按词对齐（第 21.3 节）。

**19.3 认证与保护词。** `认证` **380** 次／93 页、`验证` **432** 次／178 页、`密码` **2,790** 次、`凭据` **0** 次、`令牌` **70** 次、`证书` **150** 次；`访问控制` **16** 次／9 页（最大页"系统完整性保护"6 次）、`权限` **283** 次、`限制` **478** 次、`约束` **1** 次、`强制` **53** 次、`加密` **1,763** 次、`数据保护` **1,793** 次、`USB 受限模式` **0** 次。

**19.4 后果类型词全零。** `提权`、`越权`、`竞态`、`内存破坏`、`完全控制` **0** 次；`泄露` **30** 次／11 页、`逻辑` **4** 次、`拒绝服务` **1** 次、`代码执行` **3** 次、`可用性` **7** 次。**中文手册不用 CWE 式类型词**，这一条与 #124、#125 的观察相同，本篇再加一个证据：`绕过` 这个英文语料里的核心动词，在中文层只有 23 次且集中在"激活锁"一个主题。

# 20 措辞层五篇对账：五种字段形态放在一起

**20.1 字段形态表（Impact ∶ Description ∶ 致谢，规模；本表五列数字均已按同一装载口径重测，探针 `tw126run19`）。** 竞态 #122：**0 ∶ 232 ∶ 0**，232 段（单字段，只写修复句）；逻辑 #123：**10 ∶ 540 ∶ 0**，550 段（两字段**互斥**，交 0）；拒绝服务 #124：**457 ∶ 50 ∶ 0**，470 段（两字段**部分重叠**，交 **37**）；泄露 #125：**343 ∶ 81 ∶ 11**，427 段（**三字段都有落位**）；绕过 本篇：**291 ∶ 31 ∶ 0**，**301** 段（两字段部分重叠，交 **21**）。**四种形态**——单字段、两字段互斥、两字段重叠、三字段落位；本篇与 #124 同属"两字段重叠"型，只在重叠量（**21 ∶ 37**）与致谢侧是否为零上不同。**五篇里只有 #125 有致谢侧落位，且那 11 段全是页面模板句。**

**20.2 每段必带编号是通则。** 竞态 **232／232**、逻辑 **549／550**、拒绝服务 **470／470**、泄露 **427／427**、绕过 **301／301**。**只有逻辑那篇有 1 段不带编号**（页 `127111` 的 `Siri` 条目，Impact `An app may be able to elevate privileges`）。所以"公开条目必带 CVE"是本语料的通则，不是本篇特例。

**20.3 distinct 编号与独占性（重测值）。** distinct 编号：竞态 **60** 个、逻辑 **167** 个、拒绝服务 **105** 个、泄露 **96** 个、绕过 **93** 个。独占／跨集：竞态 **60 ∶ 0**、逻辑 **167 ∶ 0**、拒绝服务 **104 ∶ 1**、泄露 **90 ∶ 6**、绕过 **93 ∶ 0**。**所以"编号必为本篇独占"不是通则**，本篇的 93 ∶ 0 只是本篇的实测结果，#124 与 #125 各有 1 与 6 个编号同时出现在本集之外的段落里。编号数可以当作各集的**规模指纹**；"独占"的准确含义是**该编号不出现在本集之外的段落里**（编号层闭合），它**既不能证明条目性质互斥**（同一条目完全可以既写 `race condition` 又写 `bypass`），也不能推广到 #124、#125 那种有跨集编号的情形。

**20.4 规模比要按口径。** 五集**并集**规模 232／550／470／427／301，**Impact 侧**分别是 **0／10／457／343／291**。跨篇引用任何一个数都要注明取的哪一侧：本篇对 #124 比"两字段重叠率"是 21／301 ＝ **7.0%** 对 37／470 ＝ **7.9%**；对 #125 比"致谢侧落位段数"是 **0** 对 **11**；把 #123 的 **550**（并集）与本篇 **301**（并集）放进同一张表是合法的，把 #123 的 **10**（Impact）与本篇的 **291**（Impact）放在一起才是可比的。**并集数与 Impact 数之间没有换算系数**，因为各篇的字段落位形态本身不同（第 20.1 节）。第 7 章的配比、第 16 章的类次都要按该篇自己声明的口径解释。

**20.5 与"身份验证绕过"旧篇的分工。** #109（《绕过预期的身份验证要求》）测的是 `authentication` 作**双义词**（身份认证 vs Pointer Authentication）的歧义；它报告的在野声明 **93 段／12 个段落名是域 A 单口径**，与本篇第 13.1 节的 **103 ＝ 93 ＋ 10** 是同一短语的两套装载范围，**那多出的 10 段全在域 B 粗体版式页、且没有一段属本篇**。本篇把 **`Pointer Authentication` 单独列为一个对象桶（17 段，∩E ＝ 4，正是那 4 段在野标注）**，另外四个含 `authentication` 的宾语桶 `authentication policy` **7** 段、`IPSec authentication` **2** 段、`browser extension authentication` **2** 段、`Digital ID authentication` **1** 段——**合计 12 段，且四桶 ∩E 全为 0**（探针 `tw126run21`）。所以"认证"在本篇被拆成**一个 17 段的 PA 桶 ＋ 一个 12 段的认证类宾语组**，两组与在野标注的关系完全相反，与 #109 的口径不同、不可互换。

# 21 复算步骤与断言

**21.1 环境。** `perl`（`use utf8`＋`binmode :utf8`）；语料 `_tmp/adv2/<页号>.html` 258 份；装载器 `require "./_tmp/tl111load.pl"` 的 `load_blocks()`，字段 `imp`／`des`／`ack`／`id`／`nm`。**中文关键词脚本必须同时 `use utf8` 与 `map { decode("utf-8",$_,Encode::FB_DEFAULT) } @ARGV`**，否则每个中文词都返回 0，看起来像负结果而不是 bug。

**21.2 主脚本与四组断言。** 附表 A–F 由 `_tmp/tw126tab.pl`（＝`_tmp/tw123tab.pl` 前 50 行公共前导 ＋ `_tmp/tw126tab_body.pl`）一次生成，内置 `die` 断言，任一漂移即中止：**锚点组** `BPi 291／BPd 31／BPa 0／BP 301／PG 114／NM 58／∩X 0／∩Ds 0／∩R 42／∩E 20／∩WO 0／∩RC 18／∩L 45／∩M2 16／∩G 10／∩HI 0／∩UU 8／X 427／Ds 470`；**六组组** `仅I 270／仅D 10／仅A 0／I∩D 21／I∩A 0／D∩A 0／三重 0`；**句级组** `对象桶句次 322、无未归类、无多重命中`；**施事组** `26 条主语短语句次 291`。附表 B 另有 `句次 322 ＝ Impact 291 ＋ Description 31`、附表 F 有 `段 301`。

**21.3 分层复算路径。** 第 3–5 章：三口径词形与两字段词性（探针 `tw126p8`／`tw126p13`）；第 6–7 章：27 桶三种口径闭合（`tw126p14`，直接汇总附表 A、B 的行）；第 8 章：机制名共现（`tw126p10` H 组）；第 11 章：Impact 42／Description 2／44 对 43（`tw126p8` 第 3 节、`tw126p15`）；第 13 章：两个在野口径（`tw126p12`）；第 14 章：43 对 42 的那一段（`tw126p11`）；第 16 章：双类类对与 29∶13 拆分（`tw126p8` 第 1 节、`tw126p16`）；第 17–18 章：分箱与组件名（附表 D、F ＋ `tw126run17`）。

**21.4 中文层。** `178` 页可用（179 份转储页去掉目录页），正文起点在 `^Copyright and trademarks$` 之后；中文词表按附表 E 的 8 组 88 词逐一计数，最集中页与次数同时给。**注意中文层的译名缺位（第 19.2 节）会让英文词族直译搜索产生假阴性。**

**21.5 一句话断言。** 把本篇任何数字抄进别的文档时，必须带上：**口径（段／句／字段并集）、字段（Impact／Description／致谢）、去重层级（并集去重还是逐行去重再求和）**三件信息。本篇最容易被抄错的是 291／301／318／322 四个数与 42／43 两个数。

# 22 缺口清单与常见误读纠正

**22.1 缺口清单（八项）。** 一、`bypass` 的**中文对应词**在手册层不成体系，中英只能分层测、不能对齐；二、`Gatekeeper` 与 `quarantine` 的双词面在英文语料里分属两个字段，任何按条目去重的对象统计都要重新设计；三、SIP／CFI／PAC 缩写／Team ID／secure boot／XPC／TCC 文本层全零，**无法从措辞层判断这些机制是否被讨论**；四、双类判定只在 Impact 内做，所以"跨字段双类"没有指标；五、93 个 distinct 编号跨页复用，不能当组件数；六、附表 A 的 ∩BP 列是并集口径、与正文 Impact 口径在 R 行差 1，须在两处同时出现；七、44 条句子里有一对只差尾句号，distinct 说法实为 43；八、`bypass` 的近义动词（`circumvent`／`evade`／`work around`／`disable`）不在本篇判据内，本篇的 0 结论只覆盖裸词 `bypass`。

**22.2 误读一：「301 段＝301 次保护被绕过」。** 301 段是**把影响自述为 bypass 的文本条目**，其中 **114 页 × 版本序列** 使同一措辞在多个版本页重复；按句子去重后 Impact 只有 **42** 条说法。

**22.3 误读二：「绕过条目也常写泄露／拒绝服务」。** `DIS`、`DOS`、`FULL`、`ACE` 四类与本篇**各交 0 段**；`X`（泄露集 427）与 `Ds`（拒绝服务集 470）与本篇**各交 0 段**。这些后果自述互斥。

**22.4 误读三：「`bypassed`／`bypassing` 也在这套语料里」。** 四形**全零**，只有一个裸词形。

**22.5 误读四：「Description 侧 31 段是 31 条绕过说明」。** 是**两条修复句**（27 ＋ 4），且 `bypass` 在其中是名词，不是动词。

**22.6 误读五：「`sandbox` 与 `bypass` 共段 29 次，所以 29 条同时破坏沙盒」。** 同段双类（两句都在 Impact）只有 **13** 条；`SBX` 的 29 是三字段并集口径（第 16.3 章）。

**22.7 误读六：「43 段可远程触发」。** 43 是三字段并集口径，且那多出的 1 段命中来自**致谢里的研究者单位名 `Mobile & Wireless`**；Impact 口径是 **42**（第 14 章）。

**22.8 误读七：「绕过类多在野」。只对弱口径成立。** `Apple is aware of a report` 交 **20**、`actively exploited` 交 **0**；且 20 段全在 Kernel／RTKit／RPAC 三名。

**22.9 误读八：「锁定模式、USB 受限模式、数据保护这些机制也有绕过条目」。** 三者的条目在本语料里**与本篇交 0 段**（10／5／43 段各 0）。缺席是文本事实，不是"苹果没说所以不能绕过"。

**22.10 误读九：「`TCC` 在语料里没出现」。** 作为**组件名**出现且该名 4 段属本篇；作为**文本词** 0 段（第 8.2 章）。

**22.11 误读十：「8 段与 `user` 共段，所以绕过影响用户」。** 这 8 段是 `A user may be able to …` 的**施事位**；`user` 不是宾语。因果方向固定，不可反推。

# 23 结论

**23.1 一句话结论。** 在这套 9,136 段、257 页的公开英文安全响应语料里，`bypass` 是一个**只有一个词形、词性由字段决定、宾语全部是具名机制**的后果动词：**301 段／114 页／58 名**，Impact 侧 **291** 段全是"施事＋情态＋bypass＋具名对象"，Description 侧 **31** 段全是 `A X bypass was addressed …` 名词化修复句，致谢侧 **0** 段。

**23.2 对象即机制。** 27 把对象刀在 Impact 侧**段级无重叠加闭合 291**，头部 Privacy preferences **91**、Gatekeeper **53**、kernel memory protections **23**、quarantine **27**（Desc 侧）、Pointer Authentication **17**；`Gatekeeper`、`Same Origin Policy`、`Pointer Authentication` 三个名称在全树的每一次出现（53／10／17 段）**都是绕过条目**，而 `Content Security Policy` 46 段里只 4 段。**"绕过"在公开措辞里不是一种强度，而是一份具体机制的点名清单**。

**23.3 缺席同样成形。** `SIP`、`CFI`、`PAC` 缩写、`secure boot`、`Team ID`、`XPC`、`TCC` 文本层全零；`Lockdown`、`USB Restricted`、`Data Protection`、`AirPlay` 与本篇零共段；`actively exploited` 18 段零共段；`without` 178 段零共段；`ACE`／`DIS`／`DOS`／`FULL` 四类零共段。**这些零不是缺数据，而是这套语料对"绕过"这一自述方式的边界**。

**23.4 方法学产出（可复用）。** 一、**词性可以随字段翻转**，所以"两列词"必须先各测一次词性；二、**双词面同物**（Gatekeeper∶quarantine）使跨字段统计必然重复或漏计；三、**致谢字段会污染通道词族**（43 对 42），跨字段口径不可用作可达性证据；四、**版式域密度差要查模板句**，本篇致谢 0 段因此两域同密度；五、**distinct 句子数依赖切句口径**（44 对 43，尾句号）；六、**情态限定贯穿在野声明**（`may have been exploited` 85 对 `has been exploited` 0）。

**23.5 与其他四篇合读。** 五个类型词给出**四种**字段落位形态（单字段、两字段互斥、两字段重叠、三字段落位，本篇与 #124 同型）、五套闭合与五个 distinct 编号数（60／167／105／96／93）；**编号独占性不是通则**——#124 有 1 个、#125 有 6 个编号同时出现在本集之外，只有 #122、#123 与本篇是全独占（第 20.3 节）。措辞层可以稳定回答"苹果在哪个字段、以哪种词性、点名哪一项机制"，不能回答"这项机制是否真的可被绕过"。

# 附表 A 绕过判据六组电池（88 行）

**读法：** 列名"Impact 段／页／名"三列是该判据在 **Impact 单字段**的命中规模；"三字段并集段"＝ Impact∪Description∪致谢；**∩BP／∩R／∩E／∩WO 四列一律按"该判据的三字段并集"与对应外部集求交**。因此 `BYP 类` 行的 ∩BP 是 **301**（并集）而 Impact 列是 **291**，`远程可达 R` 行的 ∩BP 是 **43**、而正文第 14 章按 Impact 口径报 **42**（差的 1 段是页 `149034` 的 `Wallet` 条目，其**致谢栏研究者单位名含 `Mobile & Wireless`**，Impact 与 Description 都不含任何通道词）。六组是 绕过本体词 **6**、被绕过对象（27 把刀，**字段级**匹配）27、机制名对照 **19**、修复套话词 **15**、外部集对照 **12**、后果强度八类 **8** ＝ **87 条判据 ＋ 1 行合计 ＝ 88 行**。三字段全零 **8** 项、非零 **79** 项；**Impact 侧为零的判据有 18 项**，其组分布是 **机制名对照 9 ＋ 修复套话词 7 ＋ 绕过本体词 1 ＋ 被绕过对象 1**：前两类是"修复位／缺席位"词面（第 12 章），后两项是 `名词形 X bypass（主语位）` 31 段与 `file quarantine（名词化）` 27 段——**这两行是 Description 侧词面的原始证据，Impact 列必须为 0**。**合计行的"Impact 列合计 14,242"是 87 项各自 Impact 命中数的求和，不是任何集合的基数**；∩BP 列合计 **3,270**、∩R 列合计 **3,920**、∩E 列合计 **681**、∩WO 列合计 **714** 同样是逐行求和，**不得与 |BP|＝301、|R|＝843、|E|＝108、|WO|＝178 四个集合基数混读**。第 3 组给出"全树点名 ∶ 其中属本篇"的配比：`Gatekeeper` **53／53**、`Same Origin Policy` **10／10**、`Pointer Authentication` 全称 **17／17**、`enforcement` 词族 **9／9**、`improved enforcement` **4／4**（五个 100%），反例 `CSP` **46→4**、`privac*` **348→91**、`sandbox` **475→29**、`improved restrictions` **47→0**（第 7 章）。`TCC` 一行的三个 **0** 是**文本位**结论，与附表 F 里 `TCC` 作为**组件名**带 **4** 段不矛盾——`MK()` 只扫 imp／des／ack，不扫 `nm`（第 8.2 节）。`Data Protection` 一行 Impact **0**／并集 **43**／∩BP **0** 是纯字段分布事实，不表示该机制在 iOS 上不存在（第 7.4 节）。

| 组 | 判据 | Impact 段 | 页 | 名 | 三字段并集段 | ∩BP | ∩R | ∩E | ∩WO |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|
| 绕过本体词 | `bypass* 词族（本篇本体，双向词界）` | 291 | 114 | 56 | 301 | 301 | 42 | 20 | 0 |
| 绕过本体词 | `bypass 单词界（无派生形）` | 291 | 114 | 56 | 301 | 301 | 42 | 20 | 0 |
| 绕过本体词 | `仅左界子串形 bypass` | 291 | 114 | 56 | 301 | 301 | 42 | 20 | 0 |
| 绕过本体词 | `动词形 bypass + 宾语` | 291 | 114 | 56 | 301 | 301 | 42 | 20 | 0 |
| 绕过本体词 | `名词形 X bypass（主语位）` | 0 | 0 | 0 | 31 | 31 | 0 | 0 | 0 |
| 绕过本体词 | `绕过后果八类 BYP` | 291 | 114 | 56 | 301 | 301 | 42 | 20 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `Privacy preferences（句级刀）` | 91 | 65 | 23 | 91 | 91 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `Gatekeeper 检查（句级刀）` | 53 | 30 | 16 | 53 | 53 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `kernel memory protections（句级刀）` | 23 | 16 | 2 | 23 | 23 | 0 | 16 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `file quarantine（名词化）（句级刀）` | 0 | 0 | 0 | 27 | 27 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `Pointer Authentication（句级刀）` | 17 | 15 | 3 | 17 | 17 | 0 | 4 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `ASLR／kASLR（句级刀）` | 12 | 12 | 3 | 12 | 12 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `sandbox restrictions（句级刀）` | 13 | 13 | 3 | 13 | 13 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `Internet permission requirements（句级刀）` | 12 | 7 | 1 | 12 | 12 | 12 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `network filters（句级刀）` | 8 | 8 | 1 | 8 | 8 | 8 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `network restrictions（句级刀）` | 7 | 7 | 2 | 7 | 7 | 7 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `authentication policy（句级刀）` | 7 | 7 | 1 | 7 | 7 | 7 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `Shortcuts app settings（句级刀）` | 6 | 6 | 1 | 6 | 6 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `web content restrictions（句级刀）` | 6 | 6 | 1 | 6 | 6 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `Same Origin Policy（句级刀）` | 10 | 10 | 2 | 10 | 10 | 4 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `code signing enforcement（句级刀）` | 5 | 5 | 1 | 5 | 5 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `Content Security Policy（句级刀）` | 4 | 4 | 1 | 4 | 4 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `launch constraint protections（句级刀）` | 3 | 3 | 1 | 3 | 3 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `browser extension authentication（句级刀）` | 2 | 2 | 1 | 2 | 2 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `Activation Lock（句级刀）` | 2 | 2 | 1 | 2 | 2 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `the Lock Screen（句级刀）` | 2 | 2 | 1 | 2 | 2 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `pairing（句级刀）` | 1 | 1 | 1 | 1 | 1 | 1 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `clickjacking protections（句级刀）` | 1 | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `Login Window（句级刀）` | 1 | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `sender policy checks（句级刀）` | 1 | 1 | 1 | 1 | 1 | 1 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `Apple Intelligence security prompts（句级刀）` | 1 | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `Digital ID authentication（句级刀）` | 1 | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 被绕过对象（27 把刀，字段级匹配） | `IPSec authentication（句级刀）` | 2 | 2 | 1 | 2 | 2 | 2 | 0 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `Lockdown 锁定模式` | 10 | 10 | 4 | 10 | 0 | 5 | 0 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `System Integrity Protection／SIP` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `USB Restricted Mode` | 5 | 5 | 2 | 5 | 0 | 5 | 4 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `control flow integrity／CFI` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `PAC 缩写` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `address space layout randomization 全称` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `Data Protection` | 0 | 0 | 0 | 43 | 0 | 0 | 0 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `encrypt* 词族` | 5 | 5 | 2 | 5 | 0 | 0 | 0 | 3 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `secure boot` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `Team ID` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `XPC` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `TCC` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `entitlement* 词族` | 7 | 7 | 3 | 131 | 2 | 0 | 0 | 12 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `AirPlay` | 7 | 7 | 1 | 7 | 0 | 7 | 0 | 7 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `Pointer Authentication 全称` | 17 | 15 | 3 | 17 | 17 | 0 | 4 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `Gatekeeper` | 53 | 30 | 16 | 53 | 53 | 0 | 0 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `quarantine` | 4 | 4 | 2 | 31 | 27 | 4 | 0 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `Content Security Policy` | 46 | 28 | 1 | 46 | 4 | 0 | 0 | 0 |
| 机制名对照（全树有词但零 bypass 同段／或全树零出现） | `Same Origin Policy` | 10 | 10 | 2 | 10 | 10 | 4 | 0 | 0 |
| 修复套话词 | `additional checks` | 0 | 0 | 0 | 34 | 27 | 0 | 0 | 0 |
| 修复套话词 | `improved checks` | 0 | 0 | 0 | 1018 | 49 | 151 | 31 | 31 |
| 修复套话词 | `improved enforcement` | 0 | 0 | 0 | 4 | 4 | 0 | 0 | 0 |
| 修复套话词 | `improved restrictions` | 0 | 0 | 0 | 47 | 0 | 3 | 0 | 0 |
| 修复套话词 | `improved validation` | 4 | 4 | 2 | 367 | 32 | 13 | 23 | 15 |
| 修复套话词 | `improved state management` | 0 | 0 | 0 | 662 | 49 | 127 | 14 | 45 |
| 修复套话词 | `improved memory handling` | 0 | 0 | 0 | 862 | 8 | 59 | 2 | 0 |
| 修复套话词 | `was addressed with` | 4 | 4 | 2 | 6581 | 249 | 735 | 104 | 163 |
| 修复套话词 | `addressed by` | 0 | 0 | 0 | 371 | 37 | 31 | 4 | 4 |
| 修复套话词 | `enforcement 词族` | 5 | 5 | 1 | 9 | 9 | 0 | 0 | 0 |
| 修复套话词 | `restriction(s) 泛称` | 130 | 79 | 29 | 793 | 53 | 75 | 4 | 33 |
| 修复套话词 | `protection(s) 泛称` | 381 | 103 | 71 | 442 | 27 | 4 | 18 | 0 |
| 修复套话词 | `check(s) 泛称` | 51 | 24 | 17 | 2017 | 108 | 243 | 46 | 48 |
| 修复套话词 | `sandbox 词族` | 418 | 119 | 67 | 475 | 29 | 40 | 8 | 6 |
| 修复套话词 | `privac* 词族` | 101 | 70 | 27 | 348 | 91 | 4 | 0 | 4 |
| 外部集对照 | `远程可达 R` | 843 | 178 | 72 | 870 | 43 | 843 | 9 | 27 |
| 外部集对照 | `without 词界` | 178 | 97 | 37 | 189 | 0 | 29 | 5 | 178 |
| 外部集对照 | `exploit* 词界` | 108 | 76 | 12 | 108 | 20 | 4 | 108 | 0 |
| 外部集对照 | `user 词界` | 1627 | 175 | 200 | 1669 | 30 | 147 | 0 | 105 |
| 外部集对照 | `动名词开头集 G` | 2179 | 224 | 98 | 2179 | 10 | 108 | 53 | 0 |
| 外部集对照 | `非处理/解析类 HI` | 273 | 102 | 35 | 273 | 0 | 108 | 0 | 0 |
| 外部集对照 | `≥2 后果类 M2` | 185 | 82 | 35 | 185 | 16 | 23 | 0 | 0 |
| 外部集对照 | `race condition 集 RC` | 232 | 95 | 40 | 232 | 18 | 14 | 0 | 3 |
| 外部集对照 | `logic 词界集 L` | 550 | 162 | 81 | 550 | 45 | 99 | 9 | 24 |
| 外部集对照 | `拒绝服务集 Ds（#124）` | 470 | 155 | 62 | 470 | 0 | 164 | 0 | 0 |
| 外部集对照 | `泄露集 X（#125）` | 427 | 141 | 54 | 427 | 0 | 11 | 2 | 0 |
| 外部集对照 | `leak* 词族` | 181 | 101 | 29 | 192 | 0 | 63 | 0 | 0 |
| 后果强度八类 | `ACE 类` | 336 | 120 | 43 | 336 | 0 | 36 | 31 | 0 |
| 后果强度八类 | `BYP 类` | 291 | 114 | 56 | 301 | 301 | 42 | 20 | 0 |
| 后果强度八类 | `DIS 类` | 530 | 144 | 63 | 603 | 0 | 74 | 2 | 0 |
| 后果强度八类 | `DOS 类` | 2003 | 176 | 130 | 2014 | 0 | 343 | 0 | 0 |
| 后果强度八类 | `FULL 类` | 63 | 43 | 18 | 63 | 0 | 7 | 0 | 0 |
| 后果强度八类 | `MEM 类` | 220 | 85 | 17 | 390 | 16 | 3 | 44 | 0 |
| 后果强度八类 | `PRI 类` | 124 | 62 | 30 | 124 | 3 | 0 | 8 | 0 |
| 后果强度八类 | `SBX 类` | 418 | 119 | 67 | 475 | 29 | 40 | 8 | 6 |
| **合计** | 87 项（全零 8 项、非零 79 项） | — | — | — | — | 301 | 843 | 108 | — |

# 附表 B 四十四条含 bypass 整句 × 对象桶 × 句次 × I∶D × 页 × ∩R ∩E ∩M2 ∩L ∩RC（45 行）

**读法：** 每行是一条 **distinct 含 `bypass` 整句**（按句末标点切分后去空格归一），"句次"＝该句在 301 段的三个字段里出现的次数，**44 行句次合计 322 ＝ Impact 句 291 ＋ Description 句 31**（生成器断言）。字段归属列给出该句写在哪个字段：**Impact 句列非零的 42 条与 Desc 句列非零的 2 条互斥**（42 ＋ 2 ＝ 44，没有一条句子同时出现在两个字段），Desc 侧那 2 条是 `A file quarantine bypass was addressed with additional checks.`（**27**）与 `A Content Security Policy bypass was addressed with improved enforcement in AudioWorklet contexts.`（**4**）。`归入对象桶` 列按第 6 章 27 把刀的**优先序唯一归类**，桶列再汇总仍闭合 **322**，27 个桶的句次分布是 91／53／27／23／17／13／12／12／10／8／8／7／7／6／6／5／3／2／2／2／2／1×6。**五个外部集列都按"该行段集合与外部集求交"的口径**：∩R 只有 **8** 行非零、合计 **42**（第 14.2 节逐句列出）；∩E 只有 **2** 行非零、合计 **20**（16 ＋ 4，即第 13.2 节两条在野句）；∩M2 **3** 行、合计 **16**；∩L **9** 行、合计 **45**；∩RC **5** 行、合计 **18**。**页列合计 282 是逐行去重后的再求和，不是并集去重值（并集为 114 页）**；`Content Security Policy` 一桶是**混装**（句次 8 ＝ Impact 4 ＋ Description 4），其余 25 桶只在 Impact 侧。

| 含 bypass 整句 | 归入对象桶 | 句次 | Impact 句 | Desc 句 | 页 | ∩R | ∩E | ∩M2 | ∩L | ∩RC |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| An app may be able to bypass Privacy preferences | Privacy preferences | 45 | 45 | 0 | 33 | 0 | 0 | 0 | 0 | 3 |
| An app may be able to bypass certain Privacy preferences | Privacy preferences | 31 | 31 | 0 | 30 | 0 | 0 | 0 | 3 | 0 |
| A file quarantine bypass was addressed with additional checks. | file quarantine（名词化） | 27 | 0 | 27 | 24 | 0 | 0 | 0 | 0 | 0 |
| An app may bypass Gatekeeper checks | Gatekeeper 检查 | 19 | 19 | 0 | 15 | 0 | 0 | 0 | 13 | 1 |
| An attacker with arbitrary kernel read and write capability may be able to bypass kernel memory protections. | kernel memory protections | 16 | 16 | 0 | 11 | 0 | 16 | 0 | 0 | 0 |
| A malicious application may be able to bypass Privacy preferences | Privacy preferences | 14 | 14 | 0 | 11 | 0 | 0 | 0 | 0 | 3 |
| A shortcut may be able to bypass Internet permission requirements | Internet permission requirements | 12 | 12 | 0 | 7 | 12 | 0 | 0 | 7 | 0 |
| A shortcut may be able to bypass sandbox restrictions | sandbox restrictions | 11 | 11 | 0 | 11 | 0 | 0 | 11 | 0 | 6 |
| An app may be able to bypass ASLR | ASLR／kASLR | 11 | 11 | 0 | 11 | 0 | 0 | 0 | 0 | 0 |
| A maliciously crafted ZIP archive may bypass Gatekeeper checks | Gatekeeper 检查 | 10 | 10 | 0 | 4 | 0 | 0 | 0 | 3 | 0 |
| A malicious attacker with arbitrary read and write capability may be able to bypass Pointer Authentication | Pointer Authentication | 8 | 8 | 0 | 8 | 0 | 0 | 0 | 0 | 5 |
| A remote attacker may be able to bypass network filters | network filters | 8 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 |
| An app may be able to bypass network restrictions | network restrictions | 7 | 7 | 0 | 7 | 7 | 0 | 0 | 4 | 0 |
| An archive may be able to bypass Gatekeeper | Gatekeeper 检查 | 7 | 7 | 0 | 7 | 0 | 0 | 0 | 0 | 0 |
| An attacker on the local network may be able to bypass authentication policy | authentication policy | 7 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 |
| An attacker that has already achieved kernel code execution may be able to bypass kernel memory protections | kernel memory protections | 7 | 7 | 0 | 7 | 0 | 0 | 0 | 0 | 0 |
| A shortcut may be able to bypass sensitive Shortcuts app settings | Shortcuts app settings | 6 | 6 | 0 | 6 | 0 | 0 | 0 | 0 | 0 |
| A user may be able to bypass some web content restrictions | web content restrictions | 6 | 6 | 0 | 6 | 0 | 0 | 0 | 6 | 0 |
| Processing maliciously crafted web content may bypass Same Origin Policy | Same Origin Policy | 6 | 6 | 0 | 6 | 0 | 0 | 0 | 0 | 0 |
| A maliciously crafted app may be able to bypass code signing enforcement | code signing enforcement | 5 | 5 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| An attacker with arbitrary read and write capability may be able to bypass Pointer Authentication | Pointer Authentication | 5 | 5 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| A Content Security Policy bypass was addressed with improved enforcement in AudioWorklet contexts. | Content Security Policy | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| A maliciously crafted disk image may bypass Gatekeeper checks | Gatekeeper 检查 | 4 | 4 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| A website may be able to bypass Same Origin Policy | Same Origin Policy | 4 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 |
| An app may be able to bypass Gatekeeper checks | Gatekeeper 检查 | 4 | 4 | 0 | 3 | 0 | 0 | 0 | 4 | 0 |
| An attacker with arbitrary read and write capability may be able to bypass Pointer Authentication. | Pointer Authentication | 4 | 4 | 0 | 4 | 0 | 4 | 0 | 0 | 0 |
| Processing maliciously crafted web content may bypass Content Security Policy | Content Security Policy | 4 | 4 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| A malicious JAR file may bypass Gatekeeper checks | Gatekeeper 检查 | 3 | 3 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| A malicious application may bypass Gatekeeper checks | Gatekeeper 检查 | 3 | 3 | 0 | 3 | 0 | 0 | 0 | 3 | 0 |
| An Automator Quick Action workflow may be able to bypass Gatekeeper | Gatekeeper 检查 | 3 | 3 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to bypass launch constraint protections and execute malicious code with elevated privileges | launch constraint protections | 3 | 3 | 0 | 3 | 0 | 0 | 3 | 2 | 0 |
| A malicious app may be able to bypass browser extension authentication | browser extension authentication | 2 | 2 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| A user with physical access to an iOS device may be able to bypass Activation Lock | Activation Lock | 2 | 2 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to bypass sandbox restrictions | sandbox restrictions | 2 | 2 | 0 | 2 | 0 | 0 | 2 | 0 | 0 |
| An attacker in a privileged network position may be able to bypass IPSec authentication and intercept network traffic | IPSec authentication | 2 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 |
| An attacker with physical access to a macOS device with Sidecar enabled may be able to bypass the Lock Screen | the Lock Screen | 2 | 2 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| A malicious Bluetooth input device may bypass pairing | pairing | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| A malicious app may be able to bypass clickjacking protections for secure prompts | clickjacking protections | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| A person with physical access to a Mac may be able to bypass Login Window during a software update | Login Window | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| A remote attacker may be able to bypass sender policy checks and deliver malicious content via email | sender policy checks | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| An app may be able to bypass Apple Intelligence security prompts | Apple Intelligence security prompts | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to bypass kASLR | ASLR／kASLR | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An attacker with physical access to a locked device may be able to bypass Digital ID authentication when iPhone Mirroring is active | Digital ID authentication | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| On MDM managed devices, an app may be able to bypass certain Privacy preferences | Privacy preferences | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| **合计** | 27 桶 | 322 | 291 | 31 | 页列合计 282 | 42 | 20 | 16 | 45 | 18 |

# 附表 C 二十六条 Impact 句主语（施事短语）× 句次 × 段页名 × ∩R ∩E（27 行）

**读法：** 主表每行是「**句中第一个情态动词（may／can／will／is／was／could／might）之前的全部内容**」抽出的施事短语，**26 行句次合计 291、段合计 291**（Impact 侧每段一句，故句次＝段数），**表内没有「(无情态动词)」这一行**——即 291 段 Impact 绕过句**每一条都带情态动词**，这是可证伪断言、实测为真（第 5.2 节）。最大行 `An app` **124** 段（75 页／31 名），其次 `A shortcut` **29**、`A malicious application` **17**、`An attacker with arbitrary kernel read and write capability` **16**。∩R 列只有 **7** 行非零、合计 **42**（`A shortcut` 12、`A remote attacker` 9、`An attacker on the local network` 7、`An app` 7、`A website` 4、`An attacker in a privileged network position` 2、`A malicious Bluetooth input device` 1）；∩E 列只有 **2** 行非零、合计 **20**（kernel 读写 16 ＋ 任意读写 4）。**页列合计 217、名列合计 68 是逐行去重后的再求和**，合计行按 291 段整集与外部集求交。**续表五行不属于主表的 26 行、单独成表**：名词化打头 **31**（＝ Description 侧全量）、主干句式 **280**、能力式 `be able to bypass` **241** 与直陈式 `may bypass` **50**（**241 ＋ 50 ＝ 291**，第 10.1 节）、对象词长度分箱一行只指向第 6 章。**引用附表 C 时"施事类"归并口径见生成器输出**：`app` 类 1 个词位、`attacker` 类 8 个、`shortcut` 1 个、`user／person` 3 个、`website` 1 个、恶意物（malicious／crafted／archive／JAR／ZIP／workflow）10 个、其他 2 个。

| Impact 句主语（施事短语） | 句次 | 段 | 页 | 名 | ∩R | ∩E |
|---|---:|---:|---:|---:|---:|---:|
| An app | 124 | 124 | 75 | 31 | 7 | 0 |
| A shortcut | 29 | 29 | 21 | 2 | 12 | 0 |
| A malicious application | 17 | 17 | 11 | 5 | 0 | 0 |
| An attacker with arbitrary kernel read and write capability | 16 | 16 | 11 | 2 | 0 | 16 |
| A maliciously crafted ZIP archive | 10 | 10 | 4 | 3 | 0 | 0 |
| Processing maliciously crafted web content | 10 | 10 | 10 | 1 | 0 | 0 |
| A remote attacker | 9 | 9 | 9 | 2 | 9 | 0 |
| An attacker with arbitrary read and write capability | 9 | 9 | 9 | 2 | 0 | 4 |
| A malicious attacker with arbitrary read and write capability | 8 | 8 | 8 | 2 | 0 | 0 |
| An archive | 7 | 7 | 7 | 1 | 0 | 0 |
| An attacker on the local network | 7 | 7 | 7 | 1 | 7 | 0 |
| An attacker that has already achieved kernel code execution | 7 | 7 | 7 | 1 | 0 | 0 |
| A user | 6 | 6 | 6 | 1 | 0 | 0 |
| A maliciously crafted app | 5 | 5 | 5 | 1 | 0 | 0 |
| A maliciously crafted disk image | 4 | 4 | 4 | 1 | 0 | 0 |
| A website | 4 | 4 | 4 | 1 | 4 | 0 |
| A malicious JAR file | 3 | 3 | 3 | 1 | 0 | 0 |
| A malicious app | 3 | 3 | 3 | 2 | 0 | 0 |
| An Automator Quick Action workflow | 3 | 3 | 3 | 1 | 0 | 0 |
| A user with physical access to an iOS device | 2 | 2 | 2 | 1 | 0 | 0 |
| An attacker in a privileged network position | 2 | 2 | 2 | 1 | 2 | 0 |
| An attacker with physical access to a macOS device with Sidecar enabled | 2 | 2 | 2 | 1 | 0 | 0 |
| A malicious Bluetooth input device | 1 | 1 | 1 | 1 | 1 | 0 |
| A person with physical access to a Mac | 1 | 1 | 1 | 1 | 0 | 0 |
| An attacker with physical access to a locked device | 1 | 1 | 1 | 1 | 0 | 0 |
| On MDM managed devices, an app | 1 | 1 | 1 | 1 | 0 | 0 |
| **合计** | 291 | 段列可重叠 | 页列合计 217 | 名列合计 68 | — | — |

## 附表 C 续表：名词化句式与字段落位

| 续表刀 | 句次 | 段 | 页 | 名 | 说明 |
|---|---:|---:|---:|---:|---|
| 「X bypass was addressed …」名词化打头（只在 Description） | 31 | 31 | 26 | 9 | 与 31 段 Description 侧对账 |
| 「施事 + may (+be able to) + bypass + 对象」主干 | 280 | 280 | 109 | 55 | 与 291 段 Impact 侧对账 |
| 含 `be able to bypass`（能力式） | 241 | — | — | — | 与直陈式 `may bypass` 相加＝291 |
| 直陈式 `may bypass`（无 be able to） | 50 | — | — | — | 与上一行相加＝291 |
| 对象词长度分箱：单词对象／双词对象／三词及以上 | — | — | — | — | 见正文第 6 章 |

# 附表 D 页面、编号、致谢、版式与字段落位五组分箱（23 行）

**读法：** 全表分五个块，**每块最后一行是该块的分箱合计并带校验值**（页 **114**、编号 **301**、致谢 **301**），生成器对这三处有 `die` 断言。**"BP 段所在页的段数"分箱的单位是"页"**：42 页只有 1 段带 bypass、35 页 2 段、13 页 3 段、5 页 4 段、5 页 5 段、12 页 6—9 段、2 页 10—19 段，合计 **114 页**。**"Top10 页合计 81 段"是这 10 页承载的 BP 段数（占 301 段的 26.9%），不是这 10 页的全部段数**；Top10 逐页是 149035（13）、120911（10）、122373（9）、128071（8）、128072（8）、120909（7）、120916（7）、149042（7）、120910（6）、120912（6）。"同页共存"给出 **114 页共 7,020 段、BP 占 4.29%、每页均 2.64 段**，**全 BP 页只有 2 页**（`120887`、`126604`，各 1 段）。"编号数分箱"只有 **1 个（290 段）与 2 个（11 段）** 两箱、**0 箱为空＝301 段全部带编号**；该块合计行给出 **distinct 编号 93、本集独占 93、与他集共享 0**（第 20.3 节）。致谢人数分箱为 200／73／20／5／3 ＝ **301 段、441 人次、均值 1.47、最大 5**，与全树 9,132 段的 **1.80／37** 对照——**绕过类条目的致谢人数明显少于全树平均**。"版式域"行域 B **3／91 ＝ 3.30%**、域 A **298／9,045 ＝ 3.29%**，两域密度**相同**（第 3.2 节）；"字段落位"与"句级"两行给出 **291∶31∶0**、六组 **270／10／21／0／0／0／0** 与 **1 句 280／2 句 21、句次 322、distinct 句 44**。

| 块 | 分箱 | 计数 | 说明 |
|---|---|---:|---|
| BP 段所在页的段数 | 1 | 42 | 共 114 页含 bypass 条目 |
| BP 段所在页的段数 | 2 | 35 | 共 114 页含 bypass 条目 |
| BP 段所在页的段数 | 3 | 13 | 共 114 页含 bypass 条目 |
| BP 段所在页的段数 | 4 | 5 | 共 114 页含 bypass 条目 |
| BP 段所在页的段数 | 5 | 5 | 共 114 页含 bypass 条目 |
| BP 段所在页的段数 | 6-9 | 12 | 共 114 页含 bypass 条目 |
| BP 段所在页的段数 | 10-19 | 2 | 共 114 页含 bypass 条目 |
| BP 段所在页的段数 | 合计 | 114 | 校验=114 页、BP 段 301 |
| Top10 页 | 前 10 页合计 | 81 | 占 301 段的 26.9% |
| 同页共存 | 114 页全部段 | 7020 | BP 占其中 4.29%，每页均 2.64 段 |
| 同页共存 | 全 BP 页 | 2 | 该页所有段都含 bypass |
| 编号数分箱 | 1 个 | 290 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 2 个 | 11 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 合计 | 301 | 带编号段=301／301，distinct 编号=93，本集独占=93，与他集共享=0 |
| 致谢人数分箱 | 1 人 | 200 | — |
| 致谢人数分箱 | 2 人 | 73 | — |
| 致谢人数分箱 | 3 人 | 20 | — |
| 致谢人数分箱 | 4 人 | 5 | — |
| 致谢人数分箱 | 5 人 | 3 | — |
| 致谢人数分箱 | 合计 | 301 | 人次=441，均值=1.47，最大=5（全树 1.80／37） |
| 版式域 | 域 B（11 页粗体版式） | 3 | 域 B 全段 91，占比 3.30%；域 A 298／9,045 ＝ 3.29% |
| 字段落位 | Impact 侧 | 291 | Description 侧 31、致谢侧 0；仅I 270／仅D 10／I∩D 21 |
| 句级 | 301 段的 bypass 句数 | 1 句 280／2 句 21 | 句次合计 322，distinct 句 44 |

# 附表 E 中文手册层八组对应词（89 行）

**读法：** 本表在《Apple 平台安全保护》zh-CN 全量转储（179 页，窗口切分后 **178 页可用**）上做**逐词计数**，与英文侧的段计数**不是同一量纲**：英文侧一个条目算 1 段，中文侧按出现次数与覆盖页计。**"覆盖页占比"分母固定为 178**，100.0% 表示该词遍布全库、多为一般性用词，**不可读成"该主题占全部页面"**。八组是 绕过本体 **7**、被绕过对象（机制名）**20**、认证与凭据 **8**、保护机制 **12**、检查与修复 **9**、施事与载体 **12**、类型与后果 **10**、范围与程度 **10** ＝ **88 词 ＋ 1 行合计**；**全零 26 词、非零 62 词**。`绕过` 一词 **23 次／9 页**，最集中于"激活锁安全性"页（**12** 次）——**属激活锁语境而非条目语境**。26 个全零词是英文词族直译搜索的**假阴性来源**（第 19.2 节）：中文手册不用英文机制的对应译名时，"该词在中文层不存在"不等于"该机制在中文文档里没写过"。**合计行的"178 页可用"是分母说明，不是求和。**

| 组 | 中文词 | 出现次数 | 覆盖页 | 覆盖页占比 | 最集中页面 |
|---|---|---:|---:|---:|---|
| 绕过本体 | 绕过 | 23 | 9 | 5.1% | 激活锁安全性（12 次） |
| 绕过本体 | 规避 | 1 | 1 | 0.6% | 操作系统完整性（1 次） |
| 绕过本体 | 逃逸 | 0 | 0 | 0.0% | — |
| 绕过本体 | 跳过 | 2 | 1 | 0.6% | 自动设备注册（2 次） |
| 绕过本体 | 越过 | 0 | 0 | 0.0% | — |
| 绕过本体 | 绕开 | 0 | 0 | 0.0% | — |
| 绕过本体 | 突破 | 0 | 0 | 0.0% | — |
| 被绕过对象（机制名） | Gatekeeper | 0 | 0 | 0.0% | — |
| 被绕过对象（机制名） | 沙盒 | 27 | 11 | 6.2% | macOS 中的 Option ROM 安全性（6 次） |
| 被绕过对象（机制名） | 沙箱 | 0 | 0 | 0.0% | — |
| 被绕过对象（机制名） | 隐私偏好 | 0 | 0 | 0.0% | — |
| 被绕过对象（机制名） | 指针认证 | 5 | 3 | 1.7% | 操作系统完整性（3 次） |
| 被绕过对象（机制名） | 地址空间布局随机化 | 5 | 2 | 1.1% | iOS、iPadOS 和 visionOS 中的运行时进程安全性（3 次） |
| 被绕过对象（机制名） | 代码签名 | 394 | 178 | 100.0% | iOS、iPadOS、Apple tvOS、visionOS 和 watchOS 中的 App 代码签名过程（10 次） |
| 被绕过对象（机制名） | 签名 | 1287 | 178 | 100.0% | 搭载 Apple 芯片的 Mac 的启动磁盘安全性策略控制（32 次） |
| 被绕过对象（机制名） | 启动约束 | 0 | 0 | 0.0% | — |
| 被绕过对象（机制名） | 同源策略 | 0 | 0 | 0.0% | — |
| 被绕过对象（机制名） | 内容安全策略 | 0 | 0 | 0.0% | — |
| 被绕过对象（机制名） | 激活锁 | 245 | 178 | 100.0% | 激活锁安全性（46 次） |
| 被绕过对象（机制名） | 锁定屏幕 | 8 | 6 | 3.4% | 密码（3 次） |
| 被绕过对象（机制名） | 登录窗口 | 10 | 6 | 3.4% | 单点登录安全性（2 次） |
| 被绕过对象（机制名） | 配对 | 699 | 178 | 100.0% | watchOS 系统安全性（27 次） |
| 被绕过对象（机制名） | 点击劫持 | 0 | 0 | 0.0% | — |
| 被绕过对象（机制名） | 隔离 | 10 | 6 | 3.4% | 安全隔区（3 次） |
| 被绕过对象（机制名） | 检疫 | 0 | 0 | 0.0% | — |
| 被绕过对象（机制名） | 网络过滤器 | 0 | 0 | 0.0% | — |
| 被绕过对象（机制名） | 门禁 | 218 | 178 | 100.0% | macOS 中的门禁和运行时保护（14 次） |
| 认证与凭据 | 身份验证 | 5 | 3 | 1.7% | Apple 钱包中的证件（3 次） |
| 认证与凭据 | 认证 | 380 | 93 | 52.2% | 安全隔区（29 次） |
| 认证与凭据 | 验证 | 432 | 178 | 100.0% | Apple 钱包中的证件（19 次） |
| 认证与凭据 | 口令 | 1 | 1 | 0.6% | 密码（1 次） |
| 认证与凭据 | 密码 | 2790 | 178 | 100.0% | 密码监视（66 次） |
| 认证与凭据 | 凭据 | 0 | 0 | 0.0% | — |
| 认证与凭据 | 令牌 | 70 | 15 | 8.4% | 在 macOS 中管理文件保险箱（18 次） |
| 认证与凭据 | 证书 | 150 | 29 | 16.3% | TLS 安全性（17 次） |
| 保护机制 | 保护 | 5438 | 178 | 100.0% | 安全隔区（83 次） |
| 保护机制 | 限制 | 478 | 178 | 100.0% | LocalPolicy 签名密钥创建和管理（21 次） |
| 保护机制 | 约束 | 1 | 1 | 0.6% | 操作系统完整性（1 次） |
| 保护机制 | 强制 | 53 | 31 | 17.4% | 操作系统完整性（8 次） |
| 保护机制 | 执行策略 | 2 | 2 | 1.1% | Apple 安全性研究设备（1 次） |
| 保护机制 | 权限 | 283 | 178 | 100.0% | 操作系统完整性（14 次） |
| 保护机制 | 访问控制 | 16 | 9 | 5.1% | 系统完整性保护（6 次） |
| 保护机制 | 加密 | 1763 | 178 | 100.0% | 安全隔区（71 次） |
| 保护机制 | 数据保护 | 1793 | 178 | 100.0% | iCloud 高级数据保护（39 次） |
| 保护机制 | 锁定模式 | 189 | 178 | 100.0% | Apple 服务的锁定模式安全性（9 次） |
| 保护机制 | 系统完整性保护 | 207 | 178 | 100.0% | 搭载 Apple 芯片的 Mac 的 LocalPolicy 文件内容（6 次） |
| 保护机制 | USB 受限模式 | 0 | 0 | 0.0% | — |
| 检查与修复 | 检查 | 66 | 30 | 16.9% | 管理式 Apple 账户安全性（13 次） |
| 检查与修复 | 校验 | 19 | 3 | 1.7% | Apple 钱包中证件的安全性（16 次） |
| 检查与修复 | 检查项 | 0 | 0 | 0.0% | — |
| 检查与修复 | 额外检查 | 0 | 0 | 0.0% | — |
| 检查与修复 | 改进 | 231 | 178 | 100.0% | Apple 操作系统中的后台安全改进（38 次） |
| 检查与修复 | 修复 | 14 | 8 | 4.5% | 在 macOS 中防范恶意软件（5 次） |
| 检查与修复 | 补丁 | 4 | 1 | 0.6% | Apple 操作系统中的后台安全改进（4 次） |
| 检查与修复 | 加固 | 0 | 0 | 0.0% | — |
| 检查与修复 | 实施 | 242 | 178 | 100.0% | 操作系统完整性（4 次） |
| 施事与载体 | App | 18967 | 178 | 100.0% | 文稿修订记录（457 次） |
| 施事与载体 | 应用 | 82 | 40 | 22.5% | Apple 操作系统中的后台安全改进（10 次） |
| 施事与载体 | 快捷指令 | 196 | 178 | 100.0% | “快捷指令” App 中的安全功能（15 次） |
| 施事与载体 | 网站 | 93 | 36 | 20.2% | Apple 钱包中的证件（14 次） |
| 施事与载体 | 远程攻击者 | 0 | 0 | 0.0% | — |
| 施事与载体 | 本地网络 | 15 | 7 | 3.9% | 使用 HomeKit 保护路由器（5 次） |
| 施事与载体 | 恶意 | 287 | 178 | 100.0% | 在 macOS 中防范恶意软件（39 次） |
| 施事与载体 | 存档 | 3 | 1 | 0.6% | 将信用卡或借记卡添加到 Apple Pay（3 次） |
| 施事与载体 | 归档 | 2 | 2 | 1.1% | 访问钥匙类型（1 次） |
| 施事与载体 | 磁盘映像 | 23 | 6 | 3.4% | 数字签名和加密（9 次） |
| 施事与载体 | ZIP | 0 | 0 | 0.0% | — |
| 施事与载体 | JAR | 0 | 0 | 0.0% | — |
| 类型与后果 | 泄露 | 30 | 11 | 6.2% | 密码监视（15 次） |
| 类型与后果 | 提权 | 0 | 0 | 0.0% | — |
| 类型与后果 | 越权 | 0 | 0 | 0.0% | — |
| 类型与后果 | 代码执行 | 3 | 2 | 1.1% | 搭载 Apple 芯片的 Mac 上的 Rosetta 2（2 次） |
| 类型与后果 | 竞态 | 0 | 0 | 0.0% | — |
| 类型与后果 | 逻辑 | 4 | 2 | 1.1% | 硬件麦克风断联（3 次） |
| 类型与后果 | 拒绝服务 | 1 | 1 | 0.6% | macOS 中的防火墙安全性（1 次） |
| 类型与后果 | 可用性 | 7 | 6 | 3.4% | 钥匙串数据保护（2 次） |
| 类型与后果 | 内存破坏 | 0 | 0 | 0.0% | — |
| 类型与后果 | 完全控制 | 0 | 0 | 0.0% | — |
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
| **合计** | 88 词（全零 26 词、非零 62 词） | — | — | 178 页可用 | — |

# 附表 F 组件名层五十八名 × 六个外部集 × 该组件 Impact 全集（59 行）

**读法：** 每行一个组件名（块的 `nm` 字段），"BP 段"＝该名下被本篇抓到的段数，**58 行合计 301**（生成器断言）。**"该组件含 Impact 全集"列是该组件在 Impact 字段的全部段数（不限判据）**，"BP 占比"＝ 前除以后；**占比列有 9 行等于 100.0%**（`RTKit` 8、`CloudAttestation` 5、`iCloud Photo Library` 3、`RepairKit` 2、`Sidecar` 2、`iTunes Store` 2、`AppleScript` 1、`Python` 1、`Wallet` 1），**这些是"该组件在本语料里只被写成绕过"的名字**；小样本行（1—3 段）要按第 21 章的样本量一起读。六个外部集列按**该行段集合与外部集求交**：**∩R 只有 9 名非零、合计 42**；**∩E 只有 3 名非零（Kernel 8、RTKit 8、RPAC 4 ＝ 20）**；∩M2 **4** 名、合计 16；∩L **13** 名、合计 45；**∩X（泄露集）全名 0**——这与附表 A 的 `BP ∩ X ＝ 0` 是同一事实的两种呈现（|BP|＝301、|X|＝427、|BP∩X|＝**0**）。`TCC` 一行是**名字位**证据（该名的 BP 段 **4**），与附表 A 的 `TCC` 文本位全零不矛盾（第 8.2 节）。**合计行"这 58 名的 Impact 全集 3,226 段"是 58 名各自全集段数之和（各名互不重叠，可直接相加），占全树 7,434 的 43.4%，本篇 301 段占其 9.33%**；页列合计 **288** 是逐行去重再求和，**不是 114**。

| 组件名 | BP 段 | 页 | ∩R | ∩E | ∩M2 | ∩L | ∩X | 该组件含 Impact 全集 | BP 占比 |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| Kernel | 31 | 31 | 8 | 8 | 0 | 3 | 0 | 681 | 4.6% |
| Shortcuts | 27 | 19 | 12 | 0 | 5 | 7 | 0 | 129 | 20.9% |
| WebKit | 24 | 22 | 0 | 0 | 0 | 6 | 0 | 910 | 2.6% |
| AppleMobileFileIntegrity | 14 | 13 | 0 | 0 | 3 | 2 | 0 | 146 | 9.6% |
| Sandbox | 11 | 11 | 4 | 0 | 0 | 7 | 0 | 65 | 16.9% |
| Accounts | 9 | 9 | 0 | 0 | 0 | 0 | 0 | 34 | 26.5% |
| PackageKit | 9 | 9 | 0 | 0 | 0 | 0 | 0 | 106 | 8.5% |
| Safari | 9 | 9 | 4 | 0 | 0 | 0 | 0 | 92 | 9.8% |
| Audio | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 95 | 8.4% |
| LaunchServices | 8 | 8 | 0 | 0 | 0 | 2 | 0 | 58 | 13.8% |
| RTKit | 8 | 8 | 0 | 8 | 0 | 0 | 0 | 8 | 100.0% |
| AirPlay | 7 | 7 | 7 | 0 | 0 | 0 | 0 | 77 | 9.1% |
| copyfile | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 12 | 58.3% |
| Disk Images | 6 | 6 | 3 | 0 | 0 | 0 | 0 | 54 | 11.1% |
| Messages | 6 | 6 | 0 | 0 | 6 | 0 | 0 | 50 | 12.0% |
| Sync Services | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 12 | 50.0% |
| libxpc | 6 | 6 | 0 | 0 | 2 | 0 | 0 | 80 | 7.5% |
| CloudAttestation | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| CoreServices | 5 | 4 | 0 | 0 | 0 | 2 | 0 | 81 | 6.2% |
| System Settings | 5 | 4 | 0 | 0 | 0 | 4 | 0 | 30 | 16.7% |
| UIKit | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 14 | 35.7% |
| dyld | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 15 | 33.3% |
| RPAC | 4 | 4 | 0 | 4 | 0 | 0 | 0 | 5 | 80.0% |
| TCC | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 29 | 13.8% |
| libarchive | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 30 | 13.3% |
| APFS | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 38 | 7.9% |
| Assets | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 16 | 18.8% |
| Automator | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 9 | 33.3% |
| BOM | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 4 | 75.0% |
| DesktopServices | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 11 | 27.3% |
| Finder | 3 | 3 | 0 | 0 | 0 | 3 | 0 | 10 | 30.0% |
| Libinfo | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 9 | 33.3% |
| Multi-Touch | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 15 | 20.0% |
| Security Initialization | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 6 | 50.0% |
| StorageKit | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 59 | 5.1% |
| Time Zone | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 12 | 25.0% |
| autofs | 3 | 3 | 0 | 0 | 0 | 3 | 0 | 6 | 50.0% |
| iCloud Photo Library | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| zip | 3 | 3 | 0 | 0 | 0 | 3 | 0 | 7 | 42.9% |
| Archive Utility | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 8 | 25.0% |
| Passwords | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 9 | 22.2% |
| RepairKit | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Sidecar | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Telephony | 2 | 2 | 2 | 0 | 0 | 0 | 0 | 8 | 25.0% |
| WebDAV | 2 | 2 | 0 | 0 | 0 | 2 | 0 | 20 | 10.0% |
| iTunes Store | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Apple Intelligence | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| AppleScript | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Core Bluetooth | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 16 | 6.2% |
| Core Data | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 9 | 11.1% |
| Foundation | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 67 | 1.5% |
| Login Window | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 6 | 16.7% |
| Playgrounds | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| Python | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Wallet | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| WindowServer | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 32 | 3.1% |
| Xcode | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 3 | 33.3% |
| quarantine | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 12 | 8.3% |
| **合计** | 301 | 页列合计 288 | 42 | 20 | 16 | 45 | 0 | 这 58 名 Impact 全集 3226 段（全树 7,434 的 43.4%） | — |

---

**规则：** 本篇所有数字由 `_tmp/tw126tab.pl`（附表 A–F）与探针 `tw126p8.pl`／`tw126p9.pl`／`tw126p10.pl`／`tw126p11.pl`／`tw126p12.pl`／`tw126p13.pl`／`tw126p14.pl`／`tw126p15.pl`／`tw126p16.pl`／`tw126run17.pl` 在同一 `%B` 上产出，判据一律以词界与字段名写出，可用第 21 章逐步复算；分母一律注明为 9,136（全树段）、7,434（含 Impact 段）、7,020（本篇所在 114 页全部段）、9,045∶91（域 A∶域 B）、3,226（附表 F 58 名 Impact 全集）；**同一批 301 段的 291／318／322 分别是段级、字段并集级、句级三种口径，44 与 43 分别是带尾句号与去尾句号的 distinct 句子数，两组都不可互换**；`SBX` 类 ∩BP 的 29 是三字段并集口径、同段双类实为 13；附表 A「外部集对照」行的 ∩BP 按并集口径，故 `远程可达 R` 行写 43 而正文 Impact 口径写 42；附表 B、C、F 的页列与名列是逐行去重后的再求和，不得当作并集去重值；凡"A 与 B 得数相同"处必须同时给出 |A|、|B|、|A∩B| 与成员清单（第 6、11、15、16 章）。

**边界声明：** 本篇**公开英文与中文文本的措辞统计**，不是脆弱性评价、不是可利用性研究，不提供任何绕过方法、利用代码、样本、绕过链路或针对他人设备操作步骤与时间序列。文中"绕过缓解机制"一律指**公开条目把影响自述为 bypass 某对象、或在修复句里使用 `X bypass was addressed` 这一文本事实**，不推断该缺陷是否真的推翻某项保护、不推断其绕过难度、前提、可达性是否成立、不推断是否被利用；**301 段、93 个编号、27 个对象桶、58 个组件名、20 段带在野标注的条目**只描述文本分布，不构成任何组件安全性结论、缺陷优先级排序或可利用性评估。

Telegram: https://t.me/one00190
