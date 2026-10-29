@@TITLE@@iOS 信息泄露型弱点的公开措辞口径分析
@@SUBTITLE@@选题是"信息泄露（information disclosure）"这一类后果。逐词测量的结果是：**这一类是措辞层四篇里唯一三个字段都有落位、且第三字段完全由页面模板句构成的形态**——`disclos*` 词族写在 **Impact 侧 343 段**、写在 **Description 侧 81 段**、写在**致谢侧 11 段**，三字段并集 **427 ＝ 仅 Impact 335 ＋ 仅 Description 74 ＋ 仅致谢 10 ＋ Impact∩Description 7 ＋ Impact∩致谢 1**（Description∩致谢 0、三重 0），占全树 9,136 段的 **4.67%**、占含 Impact 的 7,434 段的 **5.74%**，覆盖 **141／257 页（54.9%）**与 **54／472 名（11.4%）**。第二个发现是**词形与句式互相绑定**：名词形 `disclosure` 的 Impact 侧 **172** 段里 **170** 段用 `result in`／`lead to`、**0** 段用 `be able to`；动词形 `disclose` 的 **171** 段里 **131** 段用 `be able to`／`may disclose`、只有 30 段用 `result in`／`lead to`；两形在 Impact 侧**交集 0 段**，172＋171 恰好 343；而 `disclosed`、`disclosing`、`discloses`、`disclosures`、`undisclosed` **五种派生全零**。第三个发现是**致谢侧的 11 段一段都不是条目文本**：它们全是同一句页面模板 `For our customers' protection, Apple doesn't disclose, discuss, or confirm security issues…`，全树含该句的段恰好也是 **11**，而这 11 段**全部落在域 B 的 11 个页面上**（每页一段）。第四个发现是**这一类几乎不带可达性与在野标注**：427 段与无前提集（`without` 178 段）**交 0 段**、与 `prompting`（46 段）、`consent`（76 段）**全部交 0 段**，与在野集（`exploit*` 108 段）**只交 2 段**，与远程集（R 843 段）**只交 11 段**，而这 11 段由**三条** Impact 句子承载。
@@INFO@@报告日期|2026 年 10 月 29 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（258 份文件排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 9,132 段含致谢**。泄露集 X ＝ `disclos*`（词界后起的任意后缀形）以子串出现在 Impact、Description、致谢任一字段 **＝ 427 段 / 141 页 / 54 名**，其中 Impact 侧 343 段、Description 侧 81 段、致谢侧 11 段；字段组合 **仅 I 335／仅 D 74／仅 A 10／I∩D 7／I∩A 1／D∩A 0／三重 0**。对照集：**R** ＝ #118 的远程触达集（Impact 侧 11 个通道词族）**843 段 / 178 页 / 72 名**；**WO** ＝ #119 的无前提集（Impact 含 `without` 词界）**178 段 / 97 页 / 37 名**；**E** ＝ #114 的在野集（Impact 含 `exploit` 词族）**108 段 / 76 页 / 12 名**；**G／HI** ＝ #116 与 #120 的动名词集 **2,179 段** 与交互触发集 **273 段 / 102 页 / 35 名**；**M2** ＝ #121 的同段 ≥2 后果类集 **185 段 / 82 页 / 35 名**；**U** ＝ Impact 含 `user`（词界）**1,627 段 / 175 页 / 200 名**；**RC** ＝ #122 的竞态集（Description 含 `race condition`）**232 段 / 95 页 / 40 名**；**L** ＝ #123 的逻辑集（`logic` 词界三字段）**550 段 / 162 页 / 81 名**；**Ds** ＝ #124 的拒绝服务集（`denial of service` 三字段）**470 段 / 155 页 / 62 名**。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或利用链构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**"泄露"这一后果词在公开英文文本的哪个字段、哪一句位、以哪一种词形出现，与被泄露宾语、与 `leak*` 与 `expos*` 两个邻居词族、与页面模板句、与可达性与在野标注之间的集合关系**，属于措辞与文本计量分析；文中任何"信息泄露"表述均指**公开条目把影响自述为 disclosure（或在修复句里自述 information disclosure issue）这一文本事实**，不是对该缺陷是否真的读出敏感数据、泄露范围多大、是否可远程触发的判断，也不构成任何组件机密性风险的结论
@@INFO@@分类|判据**六组 68 项 / 三字段全零 3 项 / 非零 65 项 / Impact 列合计 21,739**（附表 A，六组：泄露本体词 7、被泄露对象词 14、隐私与保护词 14、修复套话词 15、外部集对照 10、后果强度八类 8）/ **四十三条 distinct Impact 全文 × 段页名 × 含本体词与否 × ∩R ∩WO ∩E ∩M2 ∩Ds × 八类**（附表 B，44 行含合计，段合计 **427**、含本体词 **是 343／否 84**、类次 **423**）/ **五条含本体词的 Description 全文 × 修复手段，续表十五把反向刀 × 全树段 ∶ X 内段 ∶ 非 X 段**（附表 C，6 行含合计；续表 15 项 **全树 11,899 ∶ X 内 1,016 ∶ 非 X 10,883 ＝ 8.5%**，X 去重 **424 段**）/ **页面、编号、致谢与版式四组分箱**（附表 D，39 行，页分箱闭合 141、编号分箱闭合 427、致谢分箱闭合 427）/ **中文层 65 词**（附表 E，八组：泄露本体 7、被泄露对象 10、隐私与数据 8、保护机制 8、检查与修复 8、类型与后果 8、读与取 8、范围与程度 8；**全零 10 词、非零 55 词**）/ **组件层 54 名**（附表 F，55 行含合计，段合计 427、这 54 名的 Impact 全集 **3,652** 段、X 占其 **11.7%**、占全树含 Impact 段的 **49.1%**）/ **八把刀**：字段刀（Impact 343 ∶ Description 81 ∶ 致谢 11，六组互斥拆分 335／74／10／7／1／0）、词形刀（名词形 172 ∶ 动词形 171 ∶ 交 **0**，五种派生全零）、句式刀（五句式 **294／343**，残 **49** 段全是"终止或拒绝服务＋泄露"复合句）、宾语刀（八把宾语刀闭合 **343／343**：进程内存 137、内核内存 83、用户信息 73、内存内容 35、sensitive 类 31、内部状态 13、协处理器内存 1、sensitive information 1）、修复套话反向刀（`improved state management` 662 段里 30 段属 X，`improved privacy controls` **14 段全部属 X**）、后果刀（类次 **423** ∶ 427 段，0 类 **53** 段，`DIS` 类 Impact 口径 361 ∶ 并集口径 427）、`leak*` 分工刀（`leak*` 并集 192 段里 **170 段（88.5%）不属 X**，只交 22 段）、模板句刀（致谢侧 11 段**全部**是页面模板句，且全部在域 B）
@@INFO@@难度|进阶（第 3 章的 427 段有**三套分母**：占全树 9,136 段是 **4.67%**、占含 Impact 的 7,434 段是 **5.74%**、占其所在 141 页全部 7,776 段是 **5.49%**，三个数都真，混用会互相矛盾；另有第四套**版式域**分母——域 A **413／9,045 ＝ 4.57%**、域 B **14／91 ＝ 15.38%**，域 B 的三倍密度全部由模板句造成，去掉 11 段模板句后域 B 只剩 **3／91 ＝ 3.3%**；第 7 章的 **1,016 是"短语×段"的行和、不是段数**，同一批 427 段被 15 把刀覆盖后的**去重段数是 424**，两者相差 592，不可互换；`DIS` 类在附表 A 是 **427**（并集口径，每段总在三字段之一写了泄露词）、在附表 B 合计是 **361**（Impact 口径），与 #124 的 `DOS` 类 470∶459 是同一类口径差；附表 B、C、F 的页列与名列是逐行去重后的再求和，**不得当作并集去重值**；凡"两个集得数相同"处必须同时给出 |A|、|B|、|A∩B|（第 3、9、13 章））
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

**"信息泄露"是后果层里最常被读成"机密性问题"的类型词，但它在公开文本里落到哪个字段、用名词还是动词、宾语是什么、致谢里的 `disclose` 是不是条目自述——这些都是可以逐词测的。** 本篇把这句选题拆成四个互相独立的断言，逐个测量：

**断言一（存在这一类，且三个字段都有）**：公开条目里确实有把影响自述为 `disclosure`／`disclose`、把修复自述为 `information disclosure issue` 的文本。**测法**：`disclos*` 词族分三个字段匹配。**结果**：成立，且**是四篇措辞层里唯一三字段全有落位的形态**——Impact 侧 **343**、Description 侧 **81**、致谢侧 **11**，三字段并集 **427**；字段组合拆成六组互斥类：**仅 Impact 335、仅 Description 74、仅致谢 10、I∩D 7、I∩A 1、D∩A 0、三重 0**（第 3 章）。

**断言二（`leak*` 与 `disclos*` 是同一套话）**：如果泄露类措辞是同义互换，两个词族应当高度共段。**测法**：`leak*` 词族 192 段、`expos*` 词族 6 段，分别与 X 求交。**结果**：**不成立**。`leak*` ∩ X ＝ **22 段**（占 leak 集的 **11.5%**），**170 段（88.5%）写了 `leak` 却完全不属 X**；`expos*` 全树只有 **6 段**，与 X **交 0 段**，与 `leak*` 也**交 0 段**。**三个泄露近义词族是三条互不相交的轨道**：`disclos*` 427、`leak*` 170（leak 独有）、`expos*` 6（第 9 章）。

**断言三（致谢里的 `disclose` 是条目自述）**：致谢字段命中 11 段，直觉上像是 11 位研究者被感谢时涉及泄露。**测法**：把这 11 段的致谢原文取出来做 distinct。**结果**：**不成立**。**11 段是同一条页面模板句**——`For our customers' protection, Apple doesn't disclose, discuss, or confirm security issues until an investigation has occurred and patches or releases are available.`；全树含该句的段数恰好也是 **11**（即该句一次不出现在别的段里），且这 11 段**全部落在域 B 的 11 个页面上、每页一段**，该模板句**在 Impact 与 Description 两个字段一次都没出现（0 段）**（第 13 章）。

**断言四（这一类带可达性与在野标注）**：信息泄露常被读成"远程一条消息就能读到数据"。**测法**：与 #118 的远程集、#114 的在野集、#119 的无前提集求交。**结果**：**几乎全不成立**。远程侧 ∩R ＝ **11 段**（占 X 的 **2.6%**，且只由 **3 条** Impact 句子承载）；在野侧 ∩E ＝ **2 段**（都是 WebKit）；无前提侧 ∩WO ＝ **0**；`prompting`（46 段）、`consent`（76 段）与 X **一段不共**（第 11、12 章）。

**所以本篇的产出是一条"落位三字段但第三字段是模板句、词形与句式绑定、宾语只四类内存与信息、零无前提标注"的结论**：`disclos*` 在这套语料里是一套**后果套话 ＋ 修复套话的组合**，它以 43 条固定句子出现在 343 段的 Impact、以 5 条固定句子出现在 81 段的 Description，**与被泄露对象的具体种类（凭据、口令、令牌、证书、位置）一段都不共段**——第 22 章纠正会把 427 段读成"427 起数据泄露事故"的九种误读。

# 1 操作性定义与分母

**1.1 语料与切块。** 与 #114–#124 同一镜像、同一切块口径：块起于 `<h3 class="gb-header">`（域 A）或粗体段（域 B），块止于**四锚点**（下一块起点、页脚声明、版权行、脚本行），缺一锚点就会把页面免责声明切进字段文本。全树 **9,136 段 / 257 页 / 472 名**；含 Impact **7,434 段**；含致谢 **9,132 段**。域 A **9,045 段 / 246 页 / 470 名**，域 B **91 段 / 11 页 / 26 名**（页 `118479`、`120303`、`120304`、`120305`、`120307`–`120312`、`120339`）。本篇的 427 段里有 **14 段落在域 B**，其中 **11 段是模板句**（第 13、16.6 节）。

**1.2 X 集（本篇主集）。** 判据：`disclos` 起头、词界后接任意后缀（`disclosure`／`disclose`／`disclosed`／`disclosing`／`discloses`／`disclosures`），出现在 Impact、Description 或致谢任一字段；大小写不敏感。**X ＝ 427 段 / 141 页 / 54 名**。字段落位 **Impact 343 ∶ Description 81 ∶ 致谢 11**；两两交集 **I∩D ＝ 7、I∩A ＝ 1、D∩A ＝ 0、三重 ＝ 0**，故六组互斥拆分 335 ＋ 74 ＋ 10 ＋ 7 ＋ 1 ＋ 0 ＝ **427**（第 3 章）。427 段的**三个字段全部非空**（Impact 空 0 段、Description 空 0 段、致谢空 0 段）——这是本篇与 #124 的一个差别：#124 的 470 段也是 Impact 全非空，但致谢侧 0 段命中；本篇的致谢侧命中的 11 段全部落在同一句模板上。

**1.3 后果强度八类（与 #121／#122／#123／#124 同判据，只在 Impact 字段匹配）。**

| 代号 | 判据 | 全树 Impact 段 | X 命中 | X 占该类 |
|---|---|---:|---:|---:|
| `ACE` | `arbitrary code execution`／`execute arbitrary code` | 336 | 6 | 1.8% |
| `BYP` | `bypass`（词界） | 291 | **0** | 0.0% |
| `DIS` | `disclos*`／`leak*`／`expos*` | 530 | 361 | 68.1% |
| `DOS` | `denial-of-service`／`unexpected system／app termination`／`terminat*` | 2,003 | 49 | 2.4% |
| `FULL` | `arbitrary file(s)`／`any action`／`full control` | 63 | **0** | 0.0% |
| `MEM` | `memory corruption`／`corrupt memory` | 220 | **0** | 0.0% |
| `PRI` | `elevat* privileges`／`privileges escalat*` | 124 | **0** | 0.0% |
| `SBX` | `sandbox`（词界） | 418 | 7 | 1.7% |

类次合计 **6＋0＋361＋49＋0＋0＋0＋7 ＝ 423**，与第 8 章的段级分布（**0 类 53／1 类 325／2 类 49／3 类 0**，合计 427 段；类次 ＝ 325 ＋ 49×2 ＝ **423**）互为校验。**这张表里四个 0 是核心结果之一**：泄露条目在公开文本中**不写绕过、不写完全控制、不写内存破坏、不写提权**；它与"拒绝服务"共段 **49** 次、与"任意代码执行"共段 **6** 次、与"沙盒逃逸"共段 **7** 次。

**1.4 四套分母。** 同一句"泄露条目占多少"有四个答案：**4.67%**（÷9,136 全树段）、**5.74%**（÷7,434 含 Impact 段）、**5.49%**（÷427 段所在 141 页的全部 7,776 段）、**4.57%∶15.38%**（域 A÷域 A 段、域 B÷域 B 段）。本篇每次出现百分号都写清用的是哪一套。第五套用于组件层（第 15 章）：**该组件含 Impact 的段数**，X 全集占该口径 **11.7%**（427／3,652）。

**1.5 与 #122／#123／#124 的规模对照。** `race condition` 232 段 ∶ `logic` 550 段 ∶ `denial of service` 470 段 ∶ `disclos*` **427 段**。页 **95 ∶ 162 ∶ 155 ∶ 141**；名 **40 ∶ 81 ∶ 62 ∶ 54**。**四篇的字段形态四样**：竞态是 **Impact 0 ∶ Description 232**（单字段），逻辑是 **Impact 10 ∶ Description 540 ∶ 交 0**（两字段互斥），拒绝服务是 **Impact 457 ∶ Description 50 ∶ 致谢 0 ∶ 交 37**（两字段部分重叠），本篇是 **Impact 343 ∶ Description 81 ∶ 致谢 11 ∶ I∩D 7 ∶ I∩A 1 ∶ D∩A 0**（三字段全有、但第三字段是模板句）。四个形态放在一起才构成"措辞层类型词的字段落位不是一个模式"的证据（第 17 章）。

# 2 结论速览（每条都可按第 20 章复算）

**一、这一类活在 Impact 与 Description 两个字段，但致谢侧的 11 段不属于条目文本。** Impact **343**、Description **81**、致谢 **11**；六组互斥拆分 **仅 I 335／仅 D 74／仅 A 10／I∩D 7／I∩A 1／D∩A 0**。**I∩D 那 7 段是同一条句子、同一组件、同一条修复套话**：Impact 全为 `An app may be able to disclose kernel memory`、Description 全为 `An information disclosure issue was addressed with improved memory management.`，分布在页 `149034`–`149038`、`149041`、`149042` 七个页面的 `Kernel` 组件。**I∩A 那 1 段**是页 `120308` 的 `WebKit` 条目——它同时是本篇唯一的两条在野段之一（第 3.2、11.2、13.1 节）。

**二、词形只有两形且互斥，五形全零。** Impact 侧 **172** 段用名词形 `disclosure`、**171** 段用动词形 `disclose`，172 ＋ 171 ＝ **343**、**交集 0 段**。`disclosed`、`disclosing`、`discloses`、`disclosures`、`undisclosed` **三字段全部 0 段**。两形与句式绑定：名词形 172 段里 **170** 段用 `result in`／`lead to`、**0** 段用 `be able to`；动词形 171 段里 **131** 段用 `be able to`／`may disclose`、**30** 段用 `result in`／`lead to`（第 4 章）。

**三、句式只有五档且覆盖 294／343。** `may be able to disclose` **72**、`may disclose` **59**、`may result in disclosure of` **71**、`may result in the disclosure of` **49**、`may lead to disclosure of` **43**，五档全为 X 的 Impact 子集（∩X 与自身相等），合计覆盖 **294 段**；残 **49 段**全部是"终止或拒绝服务 ＋ 泄露"复合句（`…denial-of-service or potentially disclose…` 22＋8 ＝ **30**，`…termination or disclose/disclosure…` 7＋4＋3＋3＋2 ＝ **19**）。`may` 覆盖 **343／343**，`will` **0**、`could` **0**（`could` 全树 37 段但一段不属 X）（第 5 章）。

**四、被泄露对象只有四类内存与信息，凭据口令令牌证书一类都不共段。** 八把宾语刀闭合 **343／343**：`process memory` **137**、`kernel memory` **83**、`user information` **73**、`memory contents` **35**、`sensitive ＋ information／data／state／identifier` **31**、`internal states` **13**、`coprocessor memory` **1**、`sensitive information` **1**。而 `credential*` 29 段、`password*` 23 段、`token*` 4 段、`certificat*` 8 段、`location` 69 段、`personal information` **0 段**与 X 的交集**全部为 0**；`personal` 词界、`secret` 词界、`PII`（区分大小写）是附表 A 里**仅有的三个三字段全零判据**（第 6 章）。

**五、修复套话与这一类高度不重合，但有一句完全重合。** 附表 C 续表十五把反向刀合计覆盖全树 **11,899** 段（行和），其中 **1,016** 段次属 X（**8.5%**）；按段去重后是 **424 段／139 页／53 名**，**427 − 424 ＝ 3 段**不被这十五把刀覆盖。最大的一句是 `was addressed with`（全树 6,580 段、X 内 392 段、**6.0%**）；`improved input validation` **641 ∶ 71（11.1%）**；`improved state management` **662 ∶ 30（4.5%）**。反向的特例是 `improved privacy controls`：全树 **14 段，14 段全部属 X（100%）**——这句修复套话只用于泄露条目（第 7 章）。

**六、这一类与"拒绝服务"共段 30 次，与"逻辑"共段 13 次，与"竞态"零共段。** ∩Ds ＝ **30**、∩L ＝ **13**、∩RC ＝ **0**；X ∪ Ds ∪ L ＝ **1,371 段**，三集全交 ＝ **0 段**。30 段 X∩Ds 全部写 `denial-of-service or potentially disclose memory contents`（22＋8 两句），落在 8 个组件（`CoreMedia` 8、`CoreGraphics` 8、`file` 4、`Model I/O` 3、`Ruby` 3、`AppleVA` 2、`ColorSync` 1、`Spotlight` 1）；13 段 X∩L 由两句承载（`Parsing an image may lead to disclosure of user information` **8**、`Processing maliciously crafted web content may result in the disclosure of process memory` **5**）（第 8.5、17 章）。

**七、`leak*` 是另一条轨道。** `leak*` 词族三字段并集 **192 段**（Impact 181、Description 29、致谢 0），与 X **只交 22 段**，**170 段（88.5%）写了 leak 却不属 X**。`leak` 侧最大三句是 `An app may be able to leak sensitive kernel state` **54**、`An app may be able to leak sensitive user information` **28**、`Visiting a maliciously crafted website may leak sensitive data` **15**；leak 独有段的组件集中在 `Kernel` 36、`WebKit` 13、`Security` 8、`zlib` 8。**X 与 `leak` 的交集只有两个组件**：`Kernel` 20、`Apple Account` 2（第 9 章）。

**八、427 段每段都带 CVE 编号，且 96 个编号里 90 个独占。** 编号分箱 **1 个 415 段／2 个 12 段／0 个 0 段**（闭合 427）；distinct **96 个**（全树 2,108 个的 4.6%），**90 个只出现在 X 段**，跨出 X 的只有 **6 个**（`CVE-2023-42937`、`CVE-2024-1580`、`CVE-2024-23211`、`CVE-2024-23212`、`CVE-2024-23222`、`CVE-2024-27877`），其中 **5 个落在域 B**、只有 `CVE-2024-27877` 在域 A。致谢合计 **719 人次**、均值 **1.68**、最大 **15**（全树均值 1.80、最大 37）（第 16 章）。

# 3 字段刀：Impact 343 ∶ Description 81 ∶ 致谢 11，六组互斥拆分

**3.1 三个字段各自的规模。** `disclos*` 词族在 Impact 侧 **343 段／123 页／38 名**、Description 侧 **81 段／52 页／14 名**、致谢侧 **11 段／11 页／6 名**；三字段并集 **427 段／141 页／54 名**（附表 A 首行给出 Impact 与并集两列，其余按本段探针复算）。三字段全非空的性质意味着**并集不是"字段并集里挑一个"，而是同一段可以在两个字段各写一次**——但 427 段里**没有任何一段在同一句 Impact 内写 `disclos*` 两次以上**（Impact 内命中次数分布：恰好 1 次 **343 段**、0 次 **84 段**、≥2 次 **0 段**）。

**3.2 六组互斥拆分。** 335 ＋ 74 ＋ 10 ＋ 7 ＋ 1 ＋ 0 ＝ **427**，逐项：仅 Impact **335**（78.5%）、仅 Description **74**（17.3%）、仅致谢 **10**（2.3%）、Impact∩Description **7**（1.6%）、Impact∩致谢 **1**（0.2%）、Description∩致谢 **0**、三重 **0**。两个交非零类都是**极小集**：I∩D 的 7 段是**同一条 Impact 句 ＋ 同一条 Description 句**（第 2 章第一条）；I∩A 的 1 段是页 `120308` 的 `WebKit` 条目，其 Impact 为 `Processing web content may disclose sensitive information. Apple is aware of a report that this issue may have been exploited against versions of iOS before iOS 16.7.1.`，其致谢含模板句。**这 1 段同时是 ∩E ＝ 2 的其中一段**，即"Impact 与致谢双写 disclose"的那一段恰好也是"在野标注"那两段之一。

**3.3 D∩A ＝ 0 的含义。** Description 侧 81 段与致谢侧 11 段**没有一段重合**，且这 11 个致谢段所在的页面里，Description 字段一次都不写 `disclos*`（模板句本身不出现在 Description，**0 段**）。**因此"三字段都有落位"不等于"三字段互相打通"**：致谢侧那一支（11 段）与条目文本两支（Impact／Description）在段级是**几乎完全分离**的，只通过 1 段（页 `120308`）挂上 Impact 侧。

**3.4 仅 Description 的 74 段写了什么 Impact。** 这 74 段是"修复句写了 information disclosure issue、但影响句换了别的说法"的条目。其 Impact 原文 top：**19** `An app may be able to access sensitive user data`、**16** `An app may be able to leak sensitive kernel state`、**8** `An app may be able to identify what other apps a user has installed`、**7** `A website may be able to determine a user's IP address with Private Relay turned on`、**7** `A sandboxed app may be able to access sensitive user data in system logs`、**5** `A local attacker may be able to determine kernel memory layout`、**4** `A local app may be able to read a persistent account identifier`、**4** `An app may be able to execute arbitrary code with kernel privileges`、**3** `An app may be able to access sensitive network connection information`、**3** `An app may be able to determine kernel memory layout`、**2** `A malicious application may be able to leak sensitive user information`、**1** `A user's private browsing activity may be visible in Settings`（合计 83 段，其余落在附表 B 的单段行）。**注意这批 Impact 里有 `leak`、有 `access`、有 `identify`、有 `determine`、有 `read`、有 `visible`、甚至有一条 `execute arbitrary code`**——公开文本用**七个不同动词**表达同一个被自述为"信息泄露"的后果，而本篇的判据只抓 `disclos*`。这正是第 22 章第一条误读的出处。

# 4 词形刀：名词形 172 ∶ 动词形 171 ∶ 交 0，五种派生全零

**4.1 两形闭合 Impact。** 附表 A 首行的 `disclos* 词族` 拆成两行可见：`disclosure 词界` 三字段并集 **253**（Impact 172、Description 81、致谢 **0**）、`disclose 词界` 并集 **181**（Impact 171、Description **0**、致谢 11）。**名词形独占 Description、动词形独占致谢，Impact 侧两形各占一半且交集 0**（172 ＋ 171 ＝ 343）。

**4.2 派生形五连零。** `disclosed`、`disclosing`、`discloses`、`disclosures`、`undisclosed` **各自的三字段并集都是 0 段**。这套语料里 `disclose` 只以**原形**出现（跟在 `may`／`to` 之后），`disclosure` 只以**单数**出现。因此"词形刀"实际只有两刀，附表 A 那一行 `disclose／disclosed／disclosing／discloses 词界` 的 **181 段全部由 `disclose` 一个形贡献**（第 4.1 节两行相加 253 ＋ 181 ＝ 434，比并集 427 多 7，那 7 段就是 I∩D 段：Impact 写动词形、Description 写名词形）。

**4.3 两形的句式绑定。** 名词形 172 段里 **170** 段的谓语是 `result in`／`lead to`（即 `may result in (the) disclosure of X`／`may lead to disclosure of X`），**0** 段是 `be able to`；动词形 171 段里 **131** 段是 `be able to`／`may disclose`，**30** 段是 `result in`／`lead to`。**换句话说：公开文本用"名词形 ＋ 结果式"表达"处理某物导致泄露"，用"动词形 ＋ 能力式"表达"某施事者能够泄露"**——两条轨道在 Impact 侧一段都不重叠。那 30 段动词形 ＋ 结果式是复合句（第 5.3 节的 49 段残段与之部分重合）。

**4.4 `information disclosure` 短语只在 Description。** 该短语三字段并集 **81 段**，其中 **Impact 侧 0 段、Description 侧 81 段、致谢 0 段**；且 Description 侧命中的 81 段**全部**写了这个短语（81／81）。**即"信息泄露"作为一个类型名，在这套语料里只出现在修复句 `An information disclosure issue was addressed with…`，从不出现在影响句**——影响句写的是被泄露的具体对象（第 6 章），不写类型名。

# 5 句式刀：五档谓语覆盖 294／343，残 49 段全是复合句

**5.1 五档谓语。** Impact 侧 343 段按谓语切：`may be able to disclose` **72 段／40 页／10 名**、`may disclose` **59／38／8**、`may result in disclosure of` **71／53／5**、`may result in the disclosure of` **49／35／5**、`may lead to disclosure of` **43／40／5**。五档**全部是 X 的子集**（每档的 ∩X 与自身相等，无一段在 X 之外）。

**5.2 两档"结果式"的冠词差别。** `may result in disclosure of`（71 段）与 `may result in the disclosure of`（49 段）只差一个 `the`，两档**页集合不同**（53 页对 35 名 5；49 段的 `the` 形只出现在 **5 个组件名**下）。`results in the disclosure` 是 `the` 形的 49 段（同一段集合）。**`may cause … to be disclosed` 的被动式 0 段**——这套语料从不把泄露写成被动。

**5.3 残 49 段的身份。** 343 − 294 ＝ **49 段**，全部是"另一种后果 ＋ 泄露"的并列句：`Processing a maliciously crafted file may lead to a denial-of-service or potentially disclose memory contents` **22**、`Processing a file may lead to a denial-of-service or potentially disclose memory contents` **8**（两句 **30** 段，写字面 `denial-of-service`）；`Opening a maliciously crafted file may cause unexpected process termination or disclose process memory` **7**、`Processing a maliciously crafted Pages document may result in unexpected termination or disclosure of process memory` **4**、`An app may be able to cause unexpected process termination or disclose process memory` **3**、`Processing a maliciously crafted file may result in unexpected app termination or disclosure of process memory` **3**、`Mounting a maliciously crafted exFAT volume may cause unexpected system termination or kernel memory disclosure` **2**（五句 **19** 段，写 `termination` 形）。30 ＋ 19 ＝ **49**，与第 8.5 节的"双类 49 段全部是 DIS＋DOS"是**同一批段**（|A|＝|B|＝49、|A∩B|＝49）。

**5.4 情态动词一刀。** `may` **343／343**（全部命中段的影响句都含 `may`）、`will` **0**、`could` **0**。对照：`could` 在全树 Impact 有 **37 段**，**一段不属 X**。**这一类的每一段都是"可能性"句式，没有一段写成确定性断言。**

# 6 宾语刀：八把宾语刀闭合 343／343

**6.1 闭合表。** Impact 侧 343 段的被泄露宾语按八把刀切，**覆盖率 343／343、无残段**：`process memory` **137**、`kernel memory` **83**、`user information` **73**、`memory contents` **35**、`sensitive` ＋ （`information`／`data`／`state`／`identifier`）**31**、`internal states` **13**、`coprocessor memory` **1**、`sensitive information`（不带其他名词）**1**。合计 137＋83＋73＋35＋31＋13＋1＋1 ＝ **343**。

**6.2 八把刀与全集的关系（"刀宽"与"刀利"）。** `kernel memory` 在全树 Impact 有 **520** 段，X 内 **91** 段、其中 Impact 侧命中 **83** 段（**17.5%** 的 kernel memory 段属 X，第 8 段附表 C 续表）；`process memory` 全树 **326**、X 内 **137**（**42.0%**）——**本篇宾语刀里最"纯"的一把**，即写了 `process memory` 的段有接近一半就是泄露条目；`user information` 全树 **184**、X 内 **75**、Impact 侧 **73**（**40.8%**）；`memory contents` 全树 **35**、X 内 **35**（**100%**，这把刀完全落在本篇内）；`internal states` 全树 **13**、X 内 **13**（**100%**）。

**6.3 一类都不共段的宾语。** 附表 A 组 2 给出六把**交集为 0** 的宾语刀：`credential*` 全树 **29** 段 ∩ X ＝ **0**、`password*` **23** ∩ **0**、`token*` **4** ∩ **0**、`certificat*` 并集 **11** ∩ **0**、`location` **75** ∩ **0**、`personal information` **0** ∩ **0**。**另有三把判据连一次都不出现**：`personal` 词界、`secret` 词界、`PII`（区分大小写）三字段全零（附表 A 的全部 3 个全零项就是它们）。**"泄露"在公开文本里从不说泄露了密码、令牌、证书或个人信息，只说泄露了内存、状态与"用户信息"这一抽象名词**（第 22 章第四条）。

**6.4 `sensitive` 一词的分工。** `sensitive` 词界在全树 Impact 有 **1,052** 段，但只有 **78** 段属 X（**7.4%**）；X 内 427 段里写 `sensitive` 的 **84** 段（并集口径，附表 A 组 2 行）。与 #119 无前提集的交是 **25 段**——**即 `sensitive` 主要服务于别的类型词**，本篇只取它的一小部分作 `sensitive ＋ …` 宾语刀（31 段）。

# 7 修复套话反向刀：十五把 11,899 段里 1,016 段次属本篇

**7.1 十把 Description 侧短语。** 附表 C 续表按短语切（不按本体词切），十把 Description 侧修复短语 ×（全树命中段∶X 内段∶非 X 段）：`improved state management` **662∶30∶632（4.5%）**、`improved privacy controls` **14∶14∶0（100.0%）**、`private data redaction` **114∶22∶92（19.3%）**、`improved memory management` **369∶10∶359（2.7%）**、`removing the vulnerable code` **173∶20∶153（11.6%）**、`improved checks` **1,018∶50∶968（4.9%）**、`improved input validation` **641∶71∶570（11.1%）**、`improved error handling` **18∶8∶10（44.4%）**、`improved restrictions` **47∶0∶47（0.0%）**、`was addressed with` **6,580∶392∶6,188（6.0%）**。行和 **11,899∶1,016∶10,883 ＝ 8.5%**。

**7.2 行和不是段数。** **1,016 是"短语 × 段"的乘积计数**，一段可以被多把刀同时覆盖（一段的 Description 里 `was addressed with improved checks` 与 `improved checks` 是两把不同的刀）。**按段去重后是 424 段／139 页／53 名**，故 **427 − 424 ＝ 3 段**不被这十五把刀覆盖。这与 #124 的情形相反：#124 的十三把刀行和 233 **恰好等于**去重段数（十三把刀在 Ds 上互不重叠），本篇相差 **592**（1,016 − 424），**两篇的"行和"不可用同一种读法**。

**7.3 五把 Impact 侧短语。** 续表另五把在 Impact 字段测：`kernel memory` **520∶91（17.5%）**、`process memory` **326∶137（42.0%）**、`user information` **184∶75（40.8%）**、`sensitive 词界` **1,052∶78（7.4%）**、`leak* 词族` **181∶18（9.9%）**。**这五把是"宾语侧"判据**，与第 6 章的八把宾语刀口径相同但刀数不同（五把是续表选用的子集）。**注意 `leak*` 一行只有 18 段**——第 9.1 节按三字段并集测得 **22** 段，差 4 段是 `leak` 写在 Description／致谢而在 Impact 不写的段。

**7.4 一句全属本篇的修复套话。** `improved privacy controls` 全树 **14 段、14 段全部属 X**；附表 C 主表第三行给出这 14 段的 Description 全文就是 `An information disclosure issue was addressed with improved privacy controls.`。**这是四篇措辞层里唯一的"某修复短语只用于本类型"的证据**（#124 的 `improved restrictions` 恰好相反：47 段**一段不属 Ds**；本篇同一短语 **0 段属 X**，两篇形成对照）。

**7.5 主表五条 Description 全文。** Description 侧 81 段的 distinct 全文**只有 5 条**：**24** 段 `improved state management`、**20** 段 `private data redaction for log entries`、**14** 段 `improved privacy controls`、**13** 段 `removing the vulnerable code`、**10** 段 `improved memory management`（24＋20＋14＋13＋10 ＝ 81，闭合）。五条**全部以 `An information disclosure issue was addressed…` 起句**，四条用 `with`、一条用 `by removing`（对应附表 A 的 `addressed by` 判据：全树并集 371 段、X 内 **20** 段，正是这一条）。这五条里**只有 state management 那 24 段的 ∩R 非零（10 段）**，其余四条 ∩R 全 0（附表 C 合计行 ∩R ＝ 10，与第 12 章的 ∩R ＝ 11 差 **1**，那 1 段来自 I∩D 之外的 Impact 侧命中段——两种口径见附表 A 与附表 C 的读法）。

# 8 后果刀：423 类次 ∶ 427 段次，0 类 53 段

**8.1 段级分布。** 427 段按其 **Impact 字段**命中的后果类数分：**0 类 53 段／1 类 325 段／2 类 49 段／3 类 0 段**（53＋325＋49 ＝ 427）。**0 类那 53 段是"含泄露词但影响句不属八类任何一类"的段**。Impact 不写 `disclos*` 的 **84** 段（附表 B 的"否"）与这 53 段是**包含关系而非同一组**：**84 ＝ 53（八类全不命中）＋ 18（只靠 `leak*` 命中 DIS）＋ 6（命中 ACE）＋ 7（命中 SBX）**，命中其他类的为 0 段。**类次 325 ＋ 49×2 ＝ 423**。

**8.2 八类命中。** `DIS` **361**、`DOS` **49**、`SBX` **7**、`ACE` **6**，`BYP`／`FULL`／`MEM`／`PRI` **各 0**。361 ＝ Impact 侧写 `disclos*` 的 **343** ＋ 只写 `leak*` 的 **18**（第 7.3 节），**一段都不靠 `expos*`**。

**8.3 双类 49 段全是一种组合。** 49 段里 **DIS＋DOS ＝ 49、其他组合 ＝ 0**。即：**这一类只与"拒绝服务／终止"发生共段，不与提权、绕过、代码执行、内存破坏、完全控制共段**（`ACE` 那 6 段是单类段：`An app may be able to execute arbitrary code with kernel privileges` 4 段（页 120304、120307、120309、120311，组件名 `Apple Neural Engine`）＋ 在野那条 `Processing maliciously crafted web content may lead to arbitrary code execution` 1 段（页 118479，`WebKit`）＋ `Processing an image may lead to arbitrary code execution` 1 段（页 120312，`CoreMedia`）。**这 6 段一段都不属于"两种后果写在同一句"**：它们的 Impact 与 Description 都**不含** `disclos*`，进入 X 只因为**致谢字段命中了域 B 那 11 页的页面模板句**（第 13 章），即 6 段全部落在"仅致谢"那一组（10 段）里。同理 `SBX` 那 7 段也各归一类：**7 段全部是"仅 Description"**，同为 Siri 的句子 `A sandboxed app may be able to access sensitive user data in system logs`（页 121563–121568 与 121570，共 7 页），影响句写的是沙盒访问、泄露词只出现在 Description 的修复句里。）

**8.4 同句还是分句。** 49 段的 `DIS` 与 `DOS` **全部同句共存**（一句里并列两种后果）、**分句 0 段**。这与 #124 的结论一致（#124 的 30 段双类也是同句），两篇合并看：**"泄露 ＋ 拒绝服务"是这套语料里唯一被写进同一个句子的后果组合。**

**8.5 ∩Ds ＝ 30 段的组件分布。** 8 `CoreMedia`、8 `CoreGraphics`、4 `file`、3 `Model I/O`、3 `Ruby`、2 `AppleVA`、1 `ColorSync`、1 `Spotlight`（合计 30）。**这 30 段全部由第 5.3 节的两条 `denial-of-service or potentially disclose` 句子承载**（22＋8）。

# 9 `leak*` 分工刀：192 段里 170 段不属本篇

**9.1 三族零交。** `disclos*` 并集 **427**、`leak*` 并集 **192**、`expos*` 并集 **6**。两两交集：`disclos* ∩ leak*` ＝ **22**、`disclos* ∩ expos*` ＝ **0**、`leak* ∩ expos*` ＝ **0**。**22／192 ＝ 11.5%，即写 `leak` 的段有 88.5%（170 段）一次都不写 `disclos*`。** `expos*` 的 6 段全在 Impact 侧、只属 **1 个组件名**，与本篇和 #124 都不共段。

**9.2 两个词族的句式分工。** `leak` 侧最大三句：`An app may be able to leak sensitive kernel state` **54**、`An app may be able to leak sensitive user information` **28**、`Visiting a maliciously crafted website may leak sensitive data` **15**，另有 `A remote attacker may be able to leak memory` **8**、`Private browsing may leak some browsing history` **8**、`A remote attacker may be able to view leaked DNS queries with Private Relay turned on` **7**。**`leak` 侧出现了 `disclos*` 侧没有的两类施事者**：`A remote attacker`（远程）与 `Visiting a … website`（访问行为），且 `leaked DNS queries` 是唯一用**过去分词**作定语的形——它不属于本篇判据（`leak*` 抓它，`disclos*` 不抓）。

**9.3 组件分工。** `leak ∩ X` 的组件只有 **2** 个：`Kernel` **20**、`Apple Account` **2**；`leak` 独有段的组件是 `Kernel` **36**、`WebKit` **13**、`Security` **8**、`zlib` **8**、`Contacts` **7**、`Mail` **7**、`AirPlay` **7**、`Sandbox` **7**。**同一个 `Kernel` 组件里，两个词族各写各的**：`Kernel` 的 76 段 X 里 45 段写 `disclose kernel memory`、16 段写 `leak sensitive kernel state`，**共段 0**（附表 B 两条行不相交，且第 6.1 节宾语刀把 `kernel memory` 与 `sensitive…state` 分给两把不同刀）。

# 10 文本层刀：Impact 43 条 ∶ Description 5 条，全谱 36 条

**10.1 Impact 侧 43 条 distinct 全文。** 附表 B 的 43 行覆盖 **427 段**（每行一段数），**是／否 ＝ 343／84**。前 10 条 **285 段（占 427 的 66.7%）**：71 `An app may be able to disclose kernel memory`、38 `Processing a maliciously crafted font may result in the disclosure of process memory`、35 `Processing a maliciously crafted image may result in disclosure of process memory`、30 `Processing maliciously crafted web content may disclose sensitive user information`、27 `Parsing a file may lead to disclosure of user information`、22 `…denial-of-service or potentially disclose memory contents`、19 `An app may be able to access sensitive user data`、16 `An app may be able to leak sensitive kernel state`、14 `Processing a maliciously crafted file may result in disclosure of process memory`、13 `Processing an image may result in disclosure of process memory`。**尾部 19 条各只有 1 段**（附表 B 末段），其中 12 条写"否"。

**10.2 Description 侧只有 5 条。** 第 7.5 节：**5 条覆盖 81 段**（∩Ds、∩M2、∩E、∩WO 四列**全行全 0**，只有 state management 行 ∩R ＝ 10）。**即这一类的修复句是所有措辞层篇里最窄的**：#124 的 470 段有 **42 条** distinct Description 全文（最大一条 82 段），本篇的 81 段 Description 命中只有 5 条。

**10.3 427 段全谱的 Description 有 36 条。** 把范围从"Description 命中"扩到"427 段全体"，distinct Description 全文 **36 条**，前 5 条 **206 段**：74 `The issue was addressed with improved memory handling.`、43 `An out-of-bounds read was addressed with improved input validation.`、34 `An out-of-bounds read was addressed with improved bounds checking.`、31 `The issue was addressed with improved checks.`、24 `An information disclosure issue was addressed with improved state management.`。**最大一条（74 段）不是"泄露"专用套话，而是通用的 `improved memory handling`**——这就是第 22 章第二条误读的出处：**修复句里没有类型名，不等于影响不是泄露。**

**10.4 施事者十八＋一。** Impact 侧 343 段按首句施事者切：**动名词开头 268 段（78.1%）＋ `An app` 75 段（21.9%）＝ 343（闭合）**。动名词内部：**54** `Processing maliciously crafted web content`、**43** `Processing a maliciously crafted image`、**39** `…file`、**38** `…font`、**27** `Parsing a file`、**13** `Processing an image`、**8** `Processing a file`、**8** `Parsing an image`、**7** `Opening a maliciously crafted file`、**7** `Connecting to a malicious NFS server`、**5** `…asset catalog`、**4** `…Pages document`、**4** `…3D model`、**4** `…Keynote file`、**3** `Connecting to a malicious SMB share`、**2** `Mounting a maliciously crafted exFAT volume`、**1** `Processing a maliciously crafted USD file`、**1** `Processing web content`。**没有一段以 `A remote attacker` 或 `An attacker` 开头**（那两条在 `leak` 侧，第 9.2 节）；`An app` 一家的 75 段全部是 `may be able to` 能力式。

# 11 在野与前提刀：∩E ＝ 2、∩WO ＝ 0、∩prompting ＝ 0

**11.1 无前提集零交。** X 的 427 段与 #119 的无前提集（Impact 含 `without`）**交 0 段**。附表 A 里唯一与 WO 非零的泄露相关行是 `sensitive`（25 段）与 `restrict*`（33 段），**但它们都不是本篇本体判据**。`prompting`（46 段）、`consent`（76 段）、`interact`（0 段）与 X **全部交 0 段**。

**11.2 在野集只有两段。** ∩E ＝ **2 段**（占 X 的 0.5%、占在野集 108 段的 1.9%），两段都是 `WebKit`。**页 `118479`** 的 Impact 是 `Processing maliciously crafted web content may lead to arbitrary code execution. Apple is aware of a report that this issue may have been exploited.`、Description 是 `A type confusion issue was addressed with improved checks.`，**两个字段都不含 `disclos*`——该段进入 X 只靠致谢侧的那句模板句**，属第 3.2 节"仅致谢 10 段"那一组。**页 `120308`** 的 Impact 是 `Processing web content may disclose sensitive information. Apple is aware of a report that this issue may have been exploited against versions of iOS before iOS 16.7.1.`，是**全篇唯一 Impact 与致谢双写 `disclos*` 的那 1 段**（第 3.2 节）。**即两段里只有一段在影响句自述泄露**；附表 B 的 ∩E 列两行各 1 段、合计 **2**，与之相符。

**11.3 一句提醒。** **∩E ＝ 2 不等于"这一类只有两起在野利用"**。X 与 E 的交集按 Impact 含 `exploit` 词族定义，而"在野"标注还常写在 Description（`Apple is aware of a report…`）与致谢的独立行里；本篇只做**判据级**求交，不做利用事件计数。

# 12 远程刀：∩R ＝ 11，只由三条句子承载

**12.1 十一段的三条句。** X ∩ R（#118 的远程触达集，Impact 侧 11 个通道词族）＝ **11 段**，只由三条 Impact 句子承载：**7** `A website may be able to determine a user's IP address with Private Relay turned on`（通道词 `website`）、**3** `An app may be able to access sensitive network connection information`（`network`）、**1** `An attacker with physical access to the accessory may be able to extract its Bluetooth pairing key and monitor Bluetooth traffic`（`Bluetooth`）。**11 段分布在 8 页／4 名**：`Authentication Services` 4、`WebKit` 3、`mDNSResponder` 3、`Bluetooth` 1。**三条 Impact 句子本身一段都不写 `disclos*`**——第 10.1 节附表 B 的"否"行里出现 ∩R 非零（7／3／1 三行）正是由此而来。但这 11 段进入 X 的入口不是一条：**10 段靠 Description 的修复句**（`A website …` 7 段在页 148281、148282、148287、148353，`An app … network connection information` 3 段在页 149035、149042、149043，两组全属"仅 Description"），**1 段靠致谢字段里的页面模板句**（页 120303 那条 Bluetooth 配对密钥句，属"仅致谢"组）。**11 段分组＝仅D 10 ＋ 仅A 1，仅I 0。**

**12.2 343 段 Impact 侧几乎不写通道。** 与 #124 对照：Ds 的 ∩R ＝ **164 段（占 Ds 34.9%）**，X 的 ∩R ＝ **11 段（占 X 2.6%）**。**拒绝服务条目里三分之一写了通道词，泄露条目里这个比例是二十六分之一。** 泄露侧的影响句写的是"处理某物"（第 10.4 节的 268 段动名词）而不写"从哪来"——**通道的缺席是这一类措辞的结构性特征**，与它是否可远程无关（第 22 章第六条）。

**12.3 附表 A 组 5 的原始行。** `远程可达 R` 一行给出：Impact 843 段／178 页／72 名、三字段并集 870 段、∩X **12**、∩R 843、∩WO 27、∩E 9。**∩X 这里是 12、第 12.1 节是 11**：差的那 1 段是 R 集在**三字段并集口径**（870 段）下多覆盖的一段，`without`／`exploit` 两行同理。**引用 R 交 X 时必须写口径**（本篇正文用 Impact 单字段定义的外部集，得 11；附表 A 按判据并集求交，得 12）。

# 13 模板句刀：致谢侧 11 段全部是页面模板，且全部在域 B

**13.1 11 段一句。** 致谢侧 11 段的 distinct 文本**只有 1 条**：`For our customers' protection, Apple doesn't disclose, discuss, or confirm security issues until an investigation has occurred and patches or releases are available. Recent releases are listed on the Apple security releases page.`**全树含该句的段 ＝ 11，其中 disclos* 命中 ＝ 11**（即该句在致谢里必然带来一次命中，无一例外）。这 11 段的页面是 `118479`、`120303`、`120304`、`120305`、`120307`、`120308`、`120309`、`120310`、`120311`、`120312`、`120339`——**恰好是域 B 的全部 11 个页面，每页一段**。

**13.2 模板句不进 Impact／Description。** 该句出现在 Impact 或 Description 字段的段数 ＝ **0**。**因此致谢侧那一支与条目文本无关**：**416 与 417 是两个口径，别混用**——把承载模板句的那 **11** 段整批从 X 里去掉，并集 **427 → 416**；只去掉"仅靠致谢进来"的那 **10** 段（第 11 段是页 `120308`，它的 Impact 本身也命中 `disclos*`，不属于这一组），并集 **427 → 417**。**本报告一律用 427 并集口径并把这一支单独列出**（附表 D 的"字段落位"三行给出致谢 11／模板 11／同段 ≥2 次 0）。

**13.3 域 B 的密度全部来自模板句。** 域 B 共 **91 段**，其中 X ＝ **14 段（15.38%）**：**11 段模板 ＋ 3 段真实条目**。那 3 段真实条目是同一条 Impact `Processing a maliciously crafted image may result in disclosure of process memory`，分别在页 `120305`、`120307`、`120310`。**域 A ＝ 413／9,045 ＝ 4.57%。** 剔除模板句后域 B 只有 **3／91 ＝ 3.3%**，**低于**域 A。**两版式域的差别不是"粗体版式更爱写泄露"，而是"粗体版式把免责声明也切成了一段"**（第 14 章）。

# 14 镜像与切块自检

**14.1 页数与块数。** 258 份镜像文件排除页面 `122085`（该页无条目块）后 **257 页**；全树 **9,136 段**、含 Impact **7,434 段**、含致谢 **9,132 段**、组件名 **472 名**。这些是 #111 以来固定分母，附表生成器对 **Impact 分母 7,434** 与 **全树 9,136** 各有一道 `die` 断言，任一变位即中止。

**14.2 四锚点。** 块尾锚点必须四条齐（下一块起点、页脚声明行、版权行、脚本行）。**少任一条就会把页面免责声明整段并入字段文本**，而本篇的致谢侧命中恰好全在免责声明里（第 13 章）——**这一项自检对本篇不是形式检查：如果锚点少一条，X 的 427 会变成更大的假集合，域 B 的密度结论也会失效。** 生成器另有一条 `die` 断言 **X 集 ＝ 427／Impact 343／Desc 81／致谢 11／I∩D 7／I∩A 1／D∩A 0、Ds ＝ 470、leak ＝ 192**，把本篇与 #124 的分母同时钉住。

**14.3 大小写与词界。** `disclos*` 判据大小写不敏感、以词界 `(?<![\w-])disclos` 起（`disclosed` 之类不会漏，`undisclosed` 因词界前缀被排除——实测 `undisclosed` **0 段**，两种写法结果相同）。附表 A 的 `PII` 行**区分大小写**，实测 0 段。

# 15 组件层：54／472 名，6 名全命中，54 名的 Impact 全集 3,652 段的 11.7%

**15.1 规模与头部。** X 覆盖 **54 个组件名（占 472 名的 11.4%）**。Top15：`Kernel` **76**、`ImageIO` **63**、`WebKit` **53**、`CoreText` **18**、`CoreGraphics` **16**、`SceneKit` **16**、`FontParser` **13**、`Siri` **12**、`CoreMedia` **9**、`Model I/O` **9**、`Foundation` **8**、`IOSkywalkFamily` **8**、`NetworkExtension` **8**、`Audio` **7**、`Messages` **7**；其余 39 名从 `RealityKit` 7 到 `afpfs` 1（附表 F 逐行列出）。**前三名合计 192 段，占 X 的 45.0%。**

**15.2 占比最高与全命中名。** 附表 F 最后一列"X 占比"按**该组件含 Impact 的段数**为分母：`IOSkywalkFamily` **8／8 ＝ 100%**、`QuickLook` **4／4**、`CoreDrag` **3／3**、`File System` **3／3**、`Keynote` **1／1**、`Pages` **1／1** ——**六个名全部命中**（该组件的每一段含 Impact 的条目都被 X 抓到）。其余高占比：`CoreText` **54.5%**（18／33）、`CoreGraphics` **59.3%**（16／27）、`RealityKit` **50.0%**、`file` **57.1%**、`Ruby` **50.0%**、`FontParser` **44.8%**、`libnetcore` **45.5%**。低占比的名里 `Safari` **1／92 ＝ 1.1%**、`Spotlight` **3／69 ＝ 4.3%**、`Accessibility` **2／46 ＝ 4.3%** ——同一组件的其余条目用别的后果词。

**15.3 与 #124 的组件重合。** X 的 54 名 ∩ Ds 的 62 名 ＝ **25 名**（重合率 25／54 ＝ 46.3%）。重合头部：`Kernel`（76 段）、`ImageIO`（63）、`WebKit`（53）、`CoreText`（18）、`CoreGraphics`（16）、`SceneKit`（16）、`CoreMedia`（9）、`Model I/O`（9）。**其中只有 `CoreGraphics` 16、`CoreMedia` 9、`file` 4、`Model I/O` 3、`Ruby` 3、`AppleVA` 2、`ColorSync` 1、`Spotlight` 1 这八个名在段级与 Ds 共段（合计 30 段，第 8.5 节）**——**25 个重合名里有 17 个是"同名不同段"**，即两个类型词挂在同一组件上但从不写进同一条目（第 22 章第七条）。

**15.4 全集口径。** 这 54 名在 Impact 字段共有 **3,652 段**（占全树 7,434 段的 **49.1%**），X 的 427 段占其 **11.7%**。**分母近一半的 Impact 段挂在本篇涉及的 54 个组件名下，但其中只有不到八分之一写了 `disclos*`。**

# 16 页、编号与致谢层

**16.1 页层。** X 覆盖 **141／257 页（54.9%）**，页号范围 **118479 – 149043**。页内条目数分箱：**1 段 51 页／2 段 29 页／3 段 25 页／4 段 8 页／5 段 9 页／6–9 段 13 页／10–19 段 5 页／20–49 段 1 页**（闭合 141）。**分布右偏但不长尾：51 页（36.2%）只有一条。** 这 141 页共 **7,776 段**，X 占 **5.49%**，非 X 段平均**每页 52.1 段**。**只有 6 页全部条目都是 X 段，其余 135 页混排**（附表 D"同页共存"块）。

**16.2 Top10 页。** 按 X 段数排：**20 段（页 149035，该页 314 段，X 占 6.4%）、12（149034，209 段，5.7%）、11（149036，118 段，9.3%）、11（149037，128 段，8.6%）、11（149038，165 段，6.7%）、10（149042，181 段，5.5%）、9（128071，151 段，6.0%）、9（148287，110 段，8.2%）、9（149043，190 段，4.7%）、8（128067，185 段，4.3%）**。Top10 合计 **110 段（占 X 的 25.8%）**，这 10 页共 **1,751 段**，即 X 在 Top10 页内部的密度是 **6.3%**。**Top10 里 6 页是 149034–149043 这一段连续页号**（同一批更新说明的相邻组件页）。第 11、12 名是页 128072（8 段）与 121564（6 段）。

**16.3 编号层。** 427 段的致谢里 distinct CVE 编号 **96 个**（全树 2,108 个的 4.6%）；**415 段带 1 个编号、12 段带 2 个编号、0 段不带编号**（分箱闭合 427，即**本篇每一段都带编号**）。**96 个编号中 90 个独占**（只出现在 X 段，其余 **8,709** 段一次都不出现）；**跨出 X 的 6 个**是 `CVE-2023-42937`（X 内 2 段／全树 3 段）、`CVE-2024-1580`（1／11）、`CVE-2024-23211`（1／5）、`CVE-2024-23212`（4／7）、`CVE-2024-23222`（1／9）、`CVE-2024-27877`（2／3）。**前 5 个的落点全在域 B 页面**（120304–120312、120339、118479），**只有 `CVE-2024-27877` 落在域 A**（页 120910／120912，`AppleVA`）。**与 #124 的编号关系：Ds 有 104 个独占编号，本篇 90 个，两篇互不冲突**（附表 D 的"编号归属"行给出 6 与 90 两个数）；段级 X∩Ds ＝ 30 段（第 8.5 节）**没有一段是编号共用带来的**——共用的 6 个编号里，`CVE-2024-27877` 在 Ds 的 2 段与在 X 的 2 段是**同一批 AppleVA 段还是不同段，本篇不下结论，只报"6 个编号跨集"这一事实**。

**16.4 致谢层。** 427 段的致谢人数合计 **719 人次**、**均值 1.68**、**最大 15**。分箱：**1 人 264 段／2 人 117／3 人 14／4 人 10／5 人 6／6 人 14／9 人以上 2 段**（264＋117＋14＋10＋6＋14＋2 ＝ 427，**7 与 8 人的箱为空**）。对照全树 **9,132 段均值 1.80、最大 37**。**这一类的致谢人数低于全树平均**，与 #124（均值 1.55）同向、与 #123（1.68）同值。**注意"同值不同集"**：#123 的均值 1.68 来自 550 段／921 人次，本篇的 1.68 来自 427 段／719 人次，**两个 1.68 不是同一批段**（|L∩X| ＝ 13 段）。

**16.5 版式域。** 域 A **413／9,045 ＝ 4.57%**、域 B **14／91 ＝ 15.38%**（第 13.3 节）。域 B 的 14 段中 **11 段是模板句**，真实条目 **3 段**；域 B 的 Impact 侧命中 4 段、Description 侧命中 **0 段**——即域 B 没有任何一条"修复句写了 information disclosure issue"的条目。

**16.6 三个"每页均值"不要混。** **每页 X 段数 ＝ 427／141 ＝ 3.03**；**X 所在页的每页总段数 ＝ 7,776／141 ＝ 55.1**；**非 X 段每页 ＝ 52.1**（附表 D 的"同页共存"块）。#124 里被引用过的是第三个口径（52.6→53.0），**不同篇之间不可直接比较这三个数**。

# 17 与 #122–#124 的对账：四种字段形态放在一起

**17.1 四篇的字段落位。**

| 篇 | 判据 | 并集段 | Impact | Description | 致谢 | 字段交 |
|---|---|---:|---:|---:|---:|---|
| #122 竞态 | Description 含 `race condition` | 232 | **0** | 232 | 0 | 无第二字段 |
| #123 逻辑 | 三字段含 `logic` | 550 | 10 | 540 | 0 | **交 0（互斥）** |
| #124 拒绝服务 | 三字段含 `denial of service` | 470 | 457 | 50 | 0 | 交 **37**（I∩D） |
| 本篇 | 三字段含 `disclos*` | 427 | 343 | 81 | **11** | I∩D **7**、I∩A **1**、D∩A **0** |

**四行给出四种形态：单字段、双字段互斥、双字段重叠、三字段含模板句。** 这张表是措辞层四篇的合并产出：**"类型词在公开文本里的字段落位"没有统一规律**，任何"Apple 把类型写在 Description"或"类型名写在 Impact"的一般化说法都会被这四行中的某一行推翻。

**17.2 三篇集合关系的实测。** X ∩ Ds ＝ **30**、X ∩ L ＝ **13**、X ∩ RC ＝ **0**、Ds ∩ L ＝ **33**；**三集全交 ＝ 0**；**X ∪ Ds ∪ L ＝ 1,371 段**（427＋470＋550 ＝ 1,447，减去 30＋13＋33 ＝ 76 的两两交，加回三重 0 ＝ 1,371，容斥闭合）。**∩RC ＝ 0 意味着：232 段竞态条目没有一段写了 `disclos*`**——竞态只在 Description 自述机制、在 Impact 从不落到泄露后果词。

**17.3 与 #124 的口径互补。** #124 的核心结果是"拒绝服务与终止语汇零重合"（`terminat*` 1,546 ∩ Ds ＝ 2）；本篇的对应结果是"泄露与终止语汇**重合 19 段**"：附表 A 给出 `terminat* 词族` ∩X ＝ **19**、`unexpected app termination` 612 ∩X ＝ **3**、`unexpected system termination` 802 ∩X ＝ **2**、`denial of service 连写` 470 ∩X ＝ **30**。**两篇合起来看：终止语汇基本只服务泄露类，拒绝服务本体词与两类的共段都靠同一批复合句**（第 5.3 节的 49 段）。

**17.4 四篇的致谢与编号对照。** 竞态：**232／232** 段含编号、distinct **60**、全部独占；逻辑：**549／550** 段含编号、distinct **167**、全部独占、921 人次、均值 1.68；拒绝服务：**470／470** 段 distinct **105**、**104 独占**、729 人次、均值 **1.55**；本篇：**427／427** 段 distinct **96**、**90 独占**、**719 人次**、均值 **1.68**。**四篇的致谢均值都低于全树 1.80**；**"每段必带编号"在措辞层四篇里是通则、不是本篇特例**：竞态 **232／232**、拒绝服务 **470／470**、本篇 **427／427** 三段全带编号，**只有逻辑那篇是 549／550**——不带编号的 1 段是页 `127111` 的 `Siri` 条目（Impact `An app may be able to elevate privileges`）。所以本篇的 427／427 只说明"本篇集合无例外"，不能当作与前三篇的差异来引用。

# 18 中文手册层：`泄露` 30 次／11 页，10 个对照词全零

**18.1 泄露本体七词。** 《Apple 平台安全保护》zh-CN 转储 178 页可用。**泄露 30 次／11 页**（最集中于"密码监视"15 次）、**透露 17 次／12 页**（硬件安全性概览 3 次）、**披露 1 次／1 页**（App 安全性概览）、**外泄 1 次／1 页**（密码安全建议）；**泄漏 0、透出 0、显露 0**。**即中文手册层有四个不同词对应英文一个 `disclos*`，而四个词里两个只出现 1 次。**

**18.2 被泄露对象的十个词。** **内存 511 次／178 页**（安全隔区 49 次）、**密码 2,790 次／178 页**（密码监视 66 次）、**令牌 70 次／15 页**、**证书 150 次／29 页**、**位置信息 7 次／4 页**、**标识符 53 次／26 页**、**内核内存 3 次／3 页**、**口令 1 次／1 页**；**进程内存 0 次、凭据 0 次**。**英文侧第一宾语是 `process memory`（137 段，占 39.9%），中文手册层这个说法一次都没有。**

**18.3 其余六组与全零十词。** 隐私与数据：隐私 851／178、个人信息 189／178、数据 3,790／178、元数据 40／16、敏感 20／13、日志 4／3、**脱敏 0**；保护机制：加密 1,763／178、保护 5,438／178、限制 478／178、权限 283／178、访问控制 16／9、隔离 10／6、沙盒 27／11、**沙箱 0**；检查与修复：验证 432、处理 337、改进 231、检查 66、校验 19、修复 14、补丁 4、**状态管理 0**；类型与后果：绕过 23／9、可用性 7／6、逻辑 4／2、溢出 1、越界 1、拒绝服务 1／1、**提权 0、竞态 0**；读与取：访问 1,880／178、显示 119／53、读取 59／22、获得 50／31、获取 39／28、查看 38／19、暴露 2／2、窃取 4／2；范围与程度：所有 525／178、部分 112／73、可能 128／74、无法 143／72、未 185／83、已 472／178、任意 9／8、全部 4／4。**全表 65 词，全零 10 词、非零 55 词**，十个全零词是：泄漏、透出、显露、进程内存、凭据、脱敏、沙箱、状态管理、提权、竞态。

**18.4 两个"零"的意义。** `状态管理` **0 次**而英文侧 `improved state management` 是**全树 662 段、本篇 30 段**的最大修复套话；`进程内存` **0 次**而英文侧它是**第一大宾语（137 段）**。**中文手册不翻译这两组术语，是因为它不描述条目级修复与影响，而非翻译遗漏**（第 21.2 节）。

# 19 与相邻各篇的口径衔接

**19.1 与 #118 远程篇。** R 集 843 段按 Impact 的 11 个通道词族定义；本篇 ∩R ＝ **11**（Impact 口径）／**12**（判据并集口径，第 12.3 节）。两篇的分母关系：**X 只有 2.6% 的段带通道词，R 只有 1.3% 的段带泄露词——两个集合几乎是正交的**。

**19.2 与 #119 无前提篇、#120 交互篇。** ∩WO ＝ **0**、∩G（动名词集）**＝ 270**（63.2%）、∩HI（交互触发集）**＝ 19**（4.4%）。动名词 270 段内部的动词分布：**processing 216、parsing 35、connecting 10、opening 7、mounting 2**；HI 侧 19 段 ＝ **connecting 10 ＋ opening 7 ＋ mounting 2**。**即"网络型施事（连接／挂载／打开共享）"这一小撮条目同时是交互触发集的成员**，而 `processing`／`parsing` 的 251 段不算交互触发。

**19.3 与 #121 后果类篇。** ∩M2 ＝ **49**（占 M2 集 185 段的 26.5%），且 49 段全部是 `DIS＋DOS`（第 8.3 节）。**#121 报告的"≥2 后果类"条目里，每四段就有一段的第二类是泄露。**

**19.4 与 #124 拒绝服务篇。** ∩Ds ＝ **30**（占 X 的 7.0%、占 Ds 的 6.4%），全部由两条 `denial-of-service or potentially disclose memory contents` 承载；组件重合 **25 名**、段级共段只发生在 **8 名**。**两个类型词在 93% 的段上各写各的。**

**19.5 与 #114 在野篇、#116 输入篇。** ∩E ＝ **2**、∩G ＝ 270（第 19.2 节）、∩UU（`user` 集，**UU 本身按 Impact 单字段含 `user` 定义**，全树 1,627 段）**＝ 118**。按 `disclos*` 的落点拆：**Impact 也命中本体词 73 ＋ 只在 Description 命中 42 ＋ 只在致谢模板命中 3 ＝ 118**，三者互不重叠（73＋42＋3 恰等于并集，即 UU ∩ X 里没有同段双字段命中）。**注意因果方向**：那 45 段进入 UU 靠的是它们 **Impact 里写的 `user`**，进入 X 靠的才是 Description（42 段）或致谢（3 段）里的 `disclos*`——不能说成"靠 Description 里的 user 进入 UU"。另有**一条按宾语的独立切法**：`user information` 宾语 75 ＋ `sensitive user …` 26 ＋ 其他含 user 提法 17 ＝ 118；它与字段切法是两个维度，不得混引（第 22 章第八条：施事者与被泄露对象里都有 `user`，两个 `user` 不是同一个论元位置）。

# 20 复算步骤与断言

**20.1 判据。** X ＝ `(?<![\w-])disclos\w*`，分别在 `$B{k}{imp}`／`{des}`／`{ack}` 上匹配，大小写不敏感，取并集。全部数据来自 `_tmp/adv2/<id>.html` 经 `_tmp/tl111load.pl` 的 `load_blocks()` 得到的 `%B`，字段名 `imp`／`des`／`ack`／`id`（页面号）／`nm`（组件名）。

**20.2 六步。**（1）组装并运行附表生成器：`head -50 _tmp/tw123tab.pl > _tmp/tw125tab.pl`，再 `cat _tmp/tw125tab_body.pl >> _tmp/tw125tab.pl`，然后 `perl _tmp/tw125tab.pl`。它生成 `_tmp/tw125A.md`…`_tmp/tw125F.md` 六个附表片段、打印各组行数与闭合值，任一 `die` 断言失败直接中止。（2）正文探针按同一方式组装：`{ head -50 _tmp/tw123tab.pl; sed -n '2,17p' _tmp/tw125tab_body.pl; cat _tmp/tw125<名>.pl; } > _tmp/tw125run<号>.pl && perl _tmp/tw125run<号>.pl`（前 50 行是公共前段与九套外部集，第 2–17 行是 X 与 Ds 的集合定义与锚点断言）。（3）`tw125probe.pl`、`tw125p2.pl` 复算致谢模板句、七句式、十六把宾语刀、leak 分工、∩R／∩WO／∩E 原文、编号与页；`tw125p3.pl` 复算附表 A 全零三项、域 B 页表、双类 19 段的句子、UU／G／HI／L／RC 五组求交、组件名重合 25 名；`tw125p5.pl` 复算五句式残 49 段、八把刀闭合 343、名词形与动词形的句式绑定、模板句 11／11 在域 B。（4）`tw125p6.pl` 复算六个词形各自落位、`leak*` 与 `expos*` 字段拆分、427 段三字段完备性、Impact 内命中次数分布、427 段的 36 条 distinct Description、X∪Ds∪L 容斥与三交、双类组合检验、名词形∩动词形。（5）`tw125p7.pl` 复算六组互斥字段拆分 335／74／10／7／1／0，并把 I∩A 的 1 段与 I∩D 的 7 段原文全部打印。（6）附表自身的 `die` 断言：A 行数 ＝ 68、组行数 ＝ 7/14/14/15/10/8、八类组 ＝ 8；B 段合计 ＝ 427、重复 ＝ 0、行数 ＝ 43、是 343／否 84，∩R／∩WO／∩E／∩M2／∩term 五列行和 ＝ 合计；C 主表 5 条／81 段、续表 15 项／11,899∶1,016∶10,883；D 页分箱 ＝ 141、编号分箱 ＝ 427、致谢分箱 ＝ 427；E 词数 ＝ 65、可用页数 ＝ 178；F 组件段合计 ＝ 427。

**20.3 关键断言清单。** `|X| ＝ 427`；`|Xi| ＝ 343`、`|Xd| ＝ 81`、`|Xa| ＝ 11`、`|Xi∩Xd| ＝ 7`、`|Xi∩Xa| ＝ 1`、`|Xd∩Xa| ＝ 0`、三重 ＝ **0**、六组互斥 **335＋74＋10＋7＋1＋0 ＝ 427**；名词形 **172** ＋ 动词形 **171** ＝ 343 且**交 0**；`disclosed`／`disclosing`／`discloses`／`disclosures`／`undisclosed` **＝ 0**；`information disclosure` **并集 81＝Desc 81、Impact 0**；句式 **72＋59＋71＋49＋43 ＝ 294**、残 **49**；`may` **343**／`will` **0**／`could` **0**；宾语八刀 **137＋83＋73＋35＋31＋13＋1＋1 ＝ 343**；`credential*`／`password*`／`token*`／`certificat*`／`location` ∩X **＝ 0**；`personal`／`secret`／`PII` **三字段全零**（附表 A 仅 3 项全零）；八类 Impact 口径 `DIS 361`∶并集口径 **427**；段级 **0 类 53／1 类 325／2 类 49／3 类 0**、类次 **423**；双类 **49 段全 DIS＋DOS**；外部集 **∩R 11／∩WO 0／∩E 2／∩G 270／∩HI 19／∩M2 49／∩UU 118／∩L 13／∩RC 0／∩Ds 30**；`leak*` **192** 且 ∩X **22**、`expos*` **6** 且 ∩X **0**；附表 A **68 项（全零 3）**、Impact 列合计 **21,739**；附表 B **43 行**、前 10 条 **285 段**、∩R 非零 **3 行**；附表 C **5 条／81 段**（∩R 行和 10）、续表 **15 项／11,899∶1,016∶10,883**（去重 **424 段／139 页／53 名**，427−424 ＝ **3**）；附表 D 页 **141**／Top10 **110 段**／编号 **96 distinct、90 独占**／致谢 **719 人次**；附表 E **65 词（全零 10）**；附表 F **54 名／427 段／全集 3,652**；全命中名 **6**；`improved privacy controls` **14∶14＝100%**；模板句 **11 段／11 页／全在域 B**；域 A **413／9,045 ＝ 4.57%**、域 B **14／91 ＝ 15.38%**；中文层 `泄露` **30 次／11 页**、`进程内存` **0 次**、`状态管理` **0 次**。

# 21 缺口清单

**21.1 判据侧的缺口。** 本篇只抓 `disclos*`，**没有覆盖**写成 `leak`（170 段）、`expos*`（6 段）、`access`／`identify`／`determine`／`read`／`visible`／`fingerprint`／`extract` 的泄露条目（第 3.4 节的 74 段里就有这七个动词）。**一个"泄露全景"集需要把这十来个动词并起来，其规模本篇不给出**——本篇只测 `disclos*` 一支的口径。

**21.2 手册侧的缺口。** `进程内存`、`内核内存`、`脱敏`、`状态管理`、`凭据`、`沙箱`、`提权`、`竞态` 在中文手册层的零次或近零次，**不能读成"手册漏译"**；手册是机制文档、不是条目表。要填这一格需要另一套对照（英文手册与中文手册的逐页对齐），本篇未做。

**21.3 需要外部知识才能填的。** 90 个独占编号对应的产品版本与修复时间；六个全命中名（`IOSkywalkFamily` 8/8、`QuickLook` 4/4、`CoreDrag` 3/3、`File System` 3/3、`Keynote` 1/1、`Pages` 1/1）为何该组件名下再无别的后果词；`CVE-2024-27877` 这个 `AppleVA` 编号在 X 与 Ds 各 2 段，**是否同两段**本篇只报数不报判定。**本篇一律不做推断。**

**21.4 域 B 的结构性缺口。** 域 B 的 91 段里 11 段是免责声明（第 13 章）。**若把免责声明单列为第三类字段，X 的并集会再降 10 段**；本篇沿用 #111 的两版式域口径，不新增字段类别。

# 22 常见误读纠正

**一、"427 段 ＝ 427 起数据泄露事故。"** 427 是**写了 `disclos*` 的条目段数**，一段是一条组件—版本条目，不是一起事件；415 段只带 1 个编号、12 段带 2 个编号，**distinct 编号 96 个**，三个数互不相等。

**二、"修复句没写 disclosure，所以不是泄露。"** 附表 B 有 **84 段** Impact 不含 `disclos*`（"否"行），第 10.3 节的 427 段里最大一条 distinct Description 是 **74 段的 `The issue was addressed with improved memory handling.`**（不含类型名）。**修复句与影响句都不写类型名的条目照样属于 X**——它们是靠另一个字段进来的。

**三、"Apple 把类型名写在 Description，所以 X 以 Description 为主。"** Description 侧只有 **81 段**，Impact 侧 **343 段**；但**类型名 `information disclosure` 确实只在 Description（81 段）且 Impact 为 0**——"落位以 Impact 为主"与"类型名只出现在 Description"两件事同时成立，不可互相替换。

**四、"泄露类条目说的是密码、令牌和证书被读走。"** 第 6.3 节：**`credential*`／`password*`／`token*`／`certificat*`／`location` 与 X 的交集全部为 0**；第一大宾语是 `process memory`（137 段）。**公开文本说的宾语是内存与状态，不是凭据。**

**五、"`leak` 和 `disclose` 是同义互换。"** 192 段 `leak*` 里 **170 段（88.5%）不属 X**，两个词族只交 **22** 段；`expos*` 只 6 段且与两族都零交（第 9 章）。

**六、"这一类是远程可利用的。"** ∩R ＝ **11 段（2.6%）**、∩WO ＝ **0**、`prompting`／`consent` **0**；343 段 Impact 里 **没有一段以 `A remote attacker` 开头**（该施事者在 `leak` 侧）。**可达性在措辞层不可判定，"文本没写远程"不等于"不能远程"。**

**七、"`Kernel` 组件既写泄露又写拒绝服务。"** 组件重合 25 名里，`Kernel`、`ImageIO`、`WebKit`、`CoreText`、`SceneKit` 等 **17 个名在段级共段为 0**；真正共段的只有 8 名 30 段（第 15.3 节）。**"同组件"与"同条目"是两件事。**

**八、"118 段涉及 user，所以施事者就是用户。"** ∩UU 的 118 段**按定义全部在 Impact 写了 `user`**，但其中 **Impact 同时写了 `disclos*` 的只有 73 段**，另 42 段的泄露词在 Description、3 段在致谢模板（第 19.5 节）；`user` 的两种论元位置是**施事（`An app may be able to … a user has installed`）**与**被泄露对象（`user information`）**，本篇按宾语刀拆为 75＋26＋17。**"user 出现"不指向单一角色。**

**九、"致谢里出现 disclose，说明研究者认领了泄露条目。"** 致谢侧 **11 段全是同一句页面模板**（`Apple doesn't disclose, discuss, or confirm…`），全树含该句的段恰好 11 且**全在域 B**；**这句话的语义是"不披露"，与条目类型无关**（第 13 章）。

# 23 结论

**一、这一类弱点在公开文本里的位置是"Impact 与 Description 双支、致谢一支全是模板句"。** Impact **343**、Description **81**、致谢 **11**，并集 **427 段／141 页（54.9%）／54 名（11.4%）**，占全树 **4.67%**、占含 Impact 段 **5.74%**。**六组互斥拆分 335／74／10／7／1／0**，措辞层四篇里的第四种字段形态（第 3 章）。

**二、词形、句式、宾语三层各自闭合。** 名词形 **172** ∶ 动词形 **171**（交 **0**）；五句式 **294** ∶ 残 **49**（残段全是复合句）；八把宾语刀 **343／343**（`process memory` 137、`kernel memory` 83、`user information` 73、`memory contents` 35、`sensitive…` 31、`internal states` 13、`coprocessor memory` 1、`sensitive information` 1），**凭据、口令、令牌、证书、位置五把刀全部零交**（第 4–6 章）。

**三、这一类只与"拒绝服务／终止"共段。** 类次 **423**（0 类 **53** 段／1 类 325／2 类 49／3 类 0），`DIS` 361 ＋ `DOS` 49 ＋ `SBX` 7 ＋ `ACE` 6，其余四类 **0**；49 段双类**全部 DIS＋DOS、全部同句**；∩Ds ＝ **30**、∩L ＝ **13**、∩RC ＝ **0**、三集全交 **0**（第 8、17 章）。

**四、`leak*` 是另一条轨道，致谢那一支是另一类文本。** `leak*` 192 段只与 X 交 **22**（88.5% 不属本篇）、`expos*` 6 段零交；致谢侧 **11 段全是域 B 的页面模板句**，模板句在 Impact／Description **0 段**（第 9、13 章）。

**五、密度、独占与均值。** 427 段每段都带编号、**96 个 distinct 编号里 90 个独占**；致谢 **719 人次、均值 1.68、最大 15**（全树 1.80／37）；54 个组件名覆盖 **49.1%** 的全树 Impact 段而 X 只占其中 **11.7%**；域 B 密度 **15.38%** 对域 A **4.57%**，差值**全部由模板句造成**（第 15、16 章）。

**六、所以这一类在措辞层的定义性特征是"三件事不说"：不说类型名（Impact 侧 0 段写 `information disclosure`）、不说被读走的是凭据（五把宾语刀零交）、不说从哪来（∩WO 0、∩R 仅 11、无 `remote attacker` 施事）。** 它说的是：**处理某个输入 → 结果式 ＋ 名词形 ＋ 一类内存或状态**，或者 **一个 app ＋ 能力式 ＋ 动词形 ＋ 一类内存**。**这套话术与"机密性风险有多大"之间没有可推导的关系**，第 22 章的九条误读全部来自把这套话术当成风险评估表。

# 附表 A 泄露判据六组电池（69 行）

**读法：** 列名"Impact 段／页／名"三列是该判据在 **Impact 单字段**的命中规模；"三字段并集段"＝ Impact∪Description∪致谢；"∩X"列与"∩R／∩WO／∩E"三列都按**该判据的三字段并集**与对应外部集求交（三个外部集本身按 Impact 单字段定义）。因此对只在 Description 出现的判据（组 4 的修复套话族），"∩R"列仍可非零——那是"该短语的段集合 ∩ R 的 Impact 段集合"；同理组 5 的 `远程可达 R` 行 ∩X ＝ **12**，而正文第 12 章按 Impact 口径报 **11**（第 12.3 节）。组 1 的第 2—4 行给出词形刀原始数据（第 4 章）：`disclosure 词界` 253、`disclose／disclosed／disclosing／discloses 词界` 181（实测 181 段全部由 `disclose` 原形贡献）、两行相加 434 比并集 427 多 **7**，那 7 段就是 I∩D 段；`information disclosure 短语` 一行给出 Impact **0**／并集 **81** 的落位证据；`disclos* of 短语`（`disclosure` ＋ `of`）Impact **170** 段，正是名词形 172 段里那 170 段结果式。组 2 的 `personal`／`secret` 两行与组 3 的 `PII` 行是**全表仅有的三个三字段全零判据**，组 2 其余六行（credential*／password*／token*／certificat*／location／internal）里前五行 ∩X ＝ **0**、只有 `internal` **13∶13** 全属本篇。组 3 里塞了三行相邻后果词（`denial of service 连写`、两句 termination 套话、`terminat* 词族`）用于第 17.3 章的跨篇对照。组 4 的 15 项里，**前十一个具体修复短语只有 `was addressed with` 带 Impact 侧 4 段、其余 Impact 全 0**（这是"修复套话写在 Description"的直接证据），后四个词界行 `addressed`／`improved`／`validation`／`handling` 的 Impact 侧是 **25／4／25／4** 段。组 5 是九套外部集加 `拒绝服务集 Ds` 的原始行；组 6 的八类 ∩X 列**并集口径行和不是段数**，其中 `DIS 类` 一行 **427**（每段总在三字段之一写了泄露词），而 Impact 口径的类次是 **423**（第 8.2 节）。**合计行的"68 项（全零 3 项）"是判据条数；"∩X 427"与"∩R 843"两列给出 |X| 与 |R| 两个集合基数本身，不是列求和；Impact 列合计 21,739 是 68 项各自 Impact 命中数的求和，同样不是任何集合的基数。**

| 组 | 判据 | Impact 段 | 页 | 名 | 三字段并集段 | ∩X | ∩R | ∩WO | ∩E |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|
| 泄露本体词 | `disclos* 词族（本篇本体）` | 343 | 123 | 38 | 427 | 427 | 11 | 0 | 2 |
| 泄露本体词 | `disclosure 词界` | 172 | 85 | 18 | 253 | 253 | 10 | 0 | 0 |
| 泄露本体词 | `disclose／disclosed／disclosing／discloses 词界` | 171 | 82 | 25 | 181 | 181 | 1 | 0 | 2 |
| 泄露本体词 | `information disclosure 短语` | 0 | 0 | 0 | 81 | 81 | 10 | 0 | 0 |
| 泄露本体词 | `disclos* of 短语` | 170 | 85 | 17 | 170 | 170 | 0 | 0 | 0 |
| 泄露本体词 | `leak* 词族` | 181 | 101 | 29 | 192 | 22 | 63 | 0 | 0 |
| 泄露本体词 | `泄露后果八类 DIS` | 530 | 144 | 63 | 603 | 427 | 74 | 0 | 2 |
| 被泄露对象词 | `memory 词界` | 1136 | 174 | 72 | 1967 | 274 | 138 | 0 | 60 |
| 被泄露对象词 | `kernel memory` | 520 | 113 | 34 | 520 | 91 | 41 | 0 | 16 |
| 被泄露对象词 | `process memory` | 326 | 110 | 24 | 326 | 137 | 27 | 0 | 0 |
| 被泄露对象词 | `memory contents` | 35 | 32 | 10 | 35 | 35 | 0 | 0 | 0 |
| 被泄露对象词 | `user information` | 184 | 98 | 36 | 184 | 75 | 16 | 0 | 0 |
| 被泄露对象词 | `sensitive 词界` | 1052 | 158 | 142 | 1134 | 84 | 46 | 25 | 1 |
| 被泄露对象词 | `personal 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 被泄露对象词 | `credential* 词界` | 29 | 28 | 8 | 29 | 0 | 10 | 5 | 0 |
| 被泄露对象词 | `password* 词界` | 23 | 21 | 7 | 23 | 0 | 1 | 4 | 0 |
| 被泄露对象词 | `token* 词界` | 4 | 4 | 1 | 4 | 0 | 0 | 0 | 0 |
| 被泄露对象词 | `certificat* 词界` | 8 | 8 | 1 | 11 | 0 | 3 | 0 | 0 |
| 被泄露对象词 | `location 词界` | 69 | 53 | 18 | 75 | 0 | 0 | 2 | 0 |
| 被泄露对象词 | `internal 词界` | 13 | 13 | 2 | 13 | 13 | 0 | 0 | 0 |
| 被泄露对象词 | `secret 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 隐私与保护词 | `privacy 词界` | 101 | 70 | 27 | 348 | 17 | 4 | 4 | 0 |
| 隐私与保护词 | `private 词界` | 117 | 71 | 25 | 227 | 30 | 24 | 22 | 0 |
| 隐私与保护词 | `redact* 词界` | 0 | 0 | 0 | 310 | 35 | 0 | 4 | 0 |
| 隐私与保护词 | `encrypt* 词界` | 3 | 3 | 1 | 3 | 0 | 0 | 3 | 0 |
| 隐私与保护词 | `protect* 词界` | 381 | 103 | 71 | 442 | 11 | 4 | 0 | 18 |
| 隐私与保护词 | `restrict* 词界` | 130 | 79 | 29 | 793 | 5 | 75 | 33 | 4 |
| 隐私与保护词 | `sandbox 词界` | 418 | 119 | 67 | 475 | 7 | 40 | 6 | 8 |
| 隐私与保护词 | `access 词界` | 1508 | 163 | 192 | 1731 | 36 | 73 | 64 | 0 |
| 隐私与保护词 | `PII 全大写（区分大小写）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 隐私与保护词 | `expos* 词界` | 6 | 6 | 1 | 6 | 0 | 0 | 0 | 0 |
| 隐私与保护词 | `denial of service 连写（#124 本体）` | 457 | 155 | 60 | 470 | 30 | 164 | 0 | 0 |
| 隐私与保护词 | `unexpected app termination` | 612 | 148 | 56 | 612 | 3 | 72 | 0 | 0 |
| 隐私与保护词 | `unexpected system termination` | 802 | 129 | 53 | 802 | 2 | 69 | 0 | 0 |
| 隐私与保护词 | `terminat* 词族` | 1546 | 165 | 102 | 1546 | 19 | 179 | 0 | 0 |
| 修复套话词 | `improved state management` | 0 | 0 | 0 | 662 | 30 | 127 | 45 | 14 |
| 修复套话词 | `improved privacy controls` | 0 | 0 | 0 | 14 | 14 | 0 | 0 | 0 |
| 修复套话词 | `improved memory management` | 0 | 0 | 0 | 369 | 10 | 41 | 0 | 17 |
| 修复套话词 | `private data redaction for log entries` | 0 | 0 | 0 | 114 | 22 | 0 | 0 | 0 |
| 修复套话词 | `removing the vulnerable code` | 0 | 0 | 0 | 173 | 20 | 8 | 0 | 4 |
| 修复套话词 | `improved input validation` | 0 | 0 | 0 | 641 | 71 | 140 | 0 | 1 |
| 修复套话词 | `improved checks` | 0 | 0 | 0 | 1018 | 50 | 151 | 31 | 31 |
| 修复套话词 | `improved restrictions` | 0 | 0 | 0 | 47 | 0 | 3 | 0 | 0 |
| 修复套话词 | `improved error handling` | 0 | 0 | 0 | 18 | 8 | 0 | 0 | 0 |
| 修复套话词 | `was addressed with` | 4 | 4 | 2 | 6581 | 392 | 735 | 163 | 104 |
| 修复套话词 | `addressed by` | 0 | 0 | 0 | 371 | 20 | 31 | 4 | 4 |
| 修复套话词 | `addressed 词界` | 25 | 14 | 6 | 7228 | 418 | 819 | 178 | 108 |
| 修复套话词 | `improved 词界` | 4 | 4 | 2 | 6038 | 394 | 742 | 130 | 104 |
| 修复套话词 | `validation 词界` | 25 | 14 | 6 | 1341 | 81 | 184 | 15 | 24 |
| 修复套话词 | `handling 词界` | 4 | 4 | 2 | 1458 | 110 | 105 | 25 | 2 |
| 外部集对照 | `远程可达 R` | 843 | 178 | 72 | 870 | 12 | 843 | 27 | 9 |
| 外部集对照 | `without 词界` | 178 | 97 | 37 | 189 | 2 | 29 | 178 | 5 |
| 外部集对照 | `exploit* 词界` | 108 | 76 | 12 | 108 | 2 | 4 | 0 | 108 |
| 外部集对照 | `user 词界` | 1627 | 175 | 200 | 1669 | 118 | 147 | 105 | 0 |
| 外部集对照 | `动名词开头集 G` | 2179 | 224 | 98 | 2179 | 270 | 108 | 0 | 53 |
| 外部集对照 | `非处理/解析类 HI` | 273 | 102 | 35 | 273 | 19 | 108 | 0 | 0 |
| 外部集对照 | `≥2 后果类 M2` | 185 | 82 | 35 | 185 | 49 | 23 | 0 | 0 |
| 外部集对照 | `race condition 集 RC` | 232 | 95 | 40 | 232 | 0 | 14 | 3 | 0 |
| 外部集对照 | `logic 词界集 L` | 550 | 162 | 81 | 550 | 13 | 99 | 24 | 9 |
| 外部集对照 | `拒绝服务集 Ds（#124）` | 470 | 155 | 62 | 470 | 30 | 164 | 0 | 0 |
| 后果强度八类 | `ACE 类` | 336 | 120 | 43 | 336 | 6 | 36 | 0 | 31 |
| 后果强度八类 | `BYP 类` | 291 | 114 | 56 | 301 | 0 | 42 | 0 | 20 |
| 后果强度八类 | `DIS 类` | 530 | 144 | 63 | 603 | 427 | 74 | 0 | 2 |
| 后果强度八类 | `DOS 类` | 2003 | 176 | 130 | 2014 | 49 | 343 | 0 | 0 |
| 后果强度八类 | `FULL 类` | 63 | 43 | 18 | 63 | 0 | 7 | 0 | 0 |
| 后果强度八类 | `MEM 类` | 220 | 85 | 17 | 390 | 8 | 3 | 0 | 44 |
| 后果强度八类 | `PRI 类` | 124 | 62 | 30 | 124 | 0 | 0 | 0 | 8 |
| 后果强度八类 | `SBX 类` | 418 | 119 | 67 | 475 | 7 | 40 | 6 | 8 |
| **合计** | 68 项（全零 3 项） | — | — | — | — | 427 | 843 | — | — |

# 附表 B 四十三条 distinct Impact 全文 × 含本体词与否 × 五个外部集 × 后果八类（44 行）

**读法：** 每行是一条 **distinct Impact 全文**，"段"是以该文本为 Impact 字段的 **X 段数**，43 行段数合计 **427**（生成器断言）。**"Impact 含本体词"列＝该文本本身是否含 `disclos*`**，是 **343**／否 **84**——"否"的 84 段是"Impact 换了别的动词、靠 Description 或致谢进入 X"的段，所以这批行仍带 ∩M2、∩Ds、八类的非零值（如 `An app may be able to leak sensitive kernel state` 16 段的 DIS 列 ＝ 16，靠 `leak` 命中八类判据）。**∩R 列只有 3 行非零（7／3／1＝11）、∩WO 列全行 0、∩E 列只有 2 行非零（各 1 段，页 118479 与 120308 两条 WebKit 句），∩Ds 列只有 2 行非零（22＋8＝30）**——附表 B 的列和与正文第 12、11、8.5 章的三个数**必须用同一种口径**（本表按该行段集合与外部集求交）。八类列给出该行段在 Impact 字段命中的类次，合计行 `DIS 361／DOS 49／ACE 6／SBX 7／其余 0`，类次合计 **423**；段级分布（0 类 53／1 类 325／2 类 49）在第 8.1 节，**表内不重复给出**。**页列合计 368、名列合计 76 是逐行去重后的再求和，不是并集去重值（并集是 141 页／54 名）。**

| Impact 全文 | 段 | 页 | 名 | Impact 含本体词 | ∩R | ∩WO | ∩E | ∩M2 | ∩Ds | ACE | BYP | DIS | DOS | FULL | MEM | PRI | SBX |
|---|---:|---:|---:|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| An app may be able to disclose kernel memory | 71 | 39 | 9 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 71 | 0 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted font may result in the disclosure of process memory | 38 | 29 | 3 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 38 | 0 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted image may result in disclosure of process memory | 35 | 34 | 2 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 35 | 0 | 0 | 0 | 0 | 0 |
| Processing maliciously crafted web content may disclose sensitive user information | 30 | 24 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 30 | 0 | 0 | 0 | 0 | 0 |
| Parsing a file may lead to disclosure of user information | 27 | 24 | 4 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 27 | 0 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted file may lead to a denial-of-service or potentially disclose memory contents | 22 | 22 | 5 | 是 | 0 | 0 | 0 | 22 | 22 | 0 | 0 | 22 | 22 | 0 | 0 | 0 | 0 |
| An app may be able to access sensitive user data | 19 | 18 | 6 | 否 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to leak sensitive kernel state | 16 | 16 | 1 | 否 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 16 | 0 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted file may result in disclosure of process memory | 14 | 14 | 2 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 14 | 0 | 0 | 0 | 0 | 0 |
| Processing an image may result in disclosure of process memory | 13 | 13 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 13 | 0 | 0 | 0 | 0 | 0 |
| Processing maliciously crafted web content may disclose internal states of the app | 13 | 13 | 2 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 13 | 0 | 0 | 0 | 0 | 0 |
| Processing maliciously crafted web content may result in the disclosure of process memory | 11 | 11 | 2 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 11 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to identify what other apps a user has installed | 8 | 8 | 1 | 否 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Parsing an image may lead to disclosure of user information | 8 | 8 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 8 | 0 | 0 | 0 | 0 | 0 |
| Processing a file may lead to a denial-of-service or potentially disclose memory contents | 8 | 7 | 3 | 是 | 0 | 0 | 0 | 8 | 8 | 0 | 0 | 8 | 8 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted image may lead to disclosure of user information | 8 | 8 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 8 | 0 | 0 | 0 | 0 | 0 |
| A sandboxed app may be able to access sensitive user data in system logs | 7 | 7 | 1 | 否 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 7 |
| A website may be able to determine a user's IP address with Private Relay turned on | 7 | 4 | 2 | 否 | 7 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Connecting to a malicious NFS server may disclose kernel memory | 7 | 7 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 7 | 0 | 0 | 0 | 0 | 0 |
| Opening a maliciously crafted file may cause unexpected process termination or disclose process memory | 7 | 7 | 1 | 是 | 0 | 0 | 0 | 7 | 0 | 0 | 0 | 7 | 7 | 0 | 0 | 0 | 0 |
| A local attacker may be able to determine kernel memory layout | 5 | 5 | 1 | 否 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted asset catalog may result in disclosure of process memory | 5 | 5 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| A local app may be able to read a persistent account identifier | 4 | 4 | 1 | 否 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to execute arbitrary code with kernel privileges | 4 | 4 | 1 | 否 | 0 | 0 | 0 | 0 | 0 | 4 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Processing a 3D model may result in disclosure of process memory | 4 | 4 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted Keynote file may disclose memory contents | 4 | 3 | 3 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted Pages document may result in unexpected termination or disclosure of process memory | 4 | 3 | 3 | 是 | 0 | 0 | 0 | 4 | 0 | 0 | 0 | 4 | 4 | 0 | 0 | 0 | 0 |
| An app may be able to access sensitive network connection information | 3 | 3 | 1 | 否 | 3 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to cause unexpected process termination or disclose process memory | 3 | 3 | 1 | 是 | 0 | 0 | 0 | 3 | 0 | 0 | 0 | 3 | 3 | 0 | 0 | 0 | 0 |
| An app may be able to determine kernel memory layout | 3 | 3 | 1 | 否 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Connecting to a malicious SMB share may disclose kernel memory | 3 | 3 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted file may result in unexpected app termination or disclosure of process memory | 3 | 3 | 1 | 是 | 0 | 0 | 0 | 3 | 0 | 0 | 0 | 3 | 3 | 0 | 0 | 0 | 0 |
| A malicious application may be able to leak sensitive user information | 2 | 2 | 1 | 否 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| Mounting a maliciously crafted exFAT volume may cause unexpected system termination or kernel memory disclosure | 2 | 1 | 1 | 是 | 0 | 0 | 0 | 2 | 0 | 0 | 0 | 2 | 2 | 0 | 0 | 0 | 0 |
| A user's private browsing activity may be visible in Settings | 1 | 1 | 1 | 否 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to access user-sensitive data | 1 | 1 | 1 | 否 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to disclose coprocessor memory | 1 | 1 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to fingerprint the user | 1 | 1 | 1 | 否 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| An attacker with physical access to the accessory may be able to extract its Bluetooth pairing key and monitor Bluetooth traffic | 1 | 1 | 1 | 否 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted USD file may disclose memory contents | 1 | 1 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| Processing an image may lead to arbitrary code execution | 1 | 1 | 1 | 否 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Processing maliciously crafted web content may lead to arbitrary code execution. Apple is aware of a report that this issue may have been exploited. | 1 | 1 | 1 | 否 | 0 | 0 | 1 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Processing web content may disclose sensitive information. Apple is aware of a report that this issue may have been exploited against versions of iOS before iOS 16.7.1. | 1 | 1 | 1 | 是 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| **合计** | 427 | 页列合计 368 | 名列合计 76 | 是 343／否 84 | 11 | 0 | 2 | 49 | 30 | 6 | 0 | 361 | 49 | 0 | 0 | 0 | 7 |

# 附表 C 五条含本体词的 Description 全文 × 修复手段（6 行）

**读法：** 主表每行是一条 **distinct Description 全文**，段数合计 **81**（＝ Description 侧命中段数，生成器断言）。五条**全部以 `An information disclosure issue was addressed…` 起句**，"修复手段"列抽出该句的手段名。**∩R／∩WO／∩E／∩M2／∩Ds 五列在主表逐行全 0，只有 state management 行 ∩R ＝ 10**，合计行按 **81 段整集**与外部集求交给出（10／0／0／0／0），与附表 A 的 `information disclosure 短语` 行 ∩R ＝ 10 一致。**主表段数、页列合计 61、名列合计 17 同样是逐行去重后的再求和。**

| Description 全文 | 段 | 页 | 名 | ∩R | ∩WO | ∩E | ∩M2 | ∩Ds | 修复手段 |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---|
| An information disclosure issue was addressed with improved state management. | 24 | 12 | 6 | 10 | 0 | 0 | 0 | 0 | state management |
| An information disclosure issue was addressed with improved private data redaction for log entries. | 20 | 13 | 2 | 0 | 0 | 0 | 0 | 0 | 日志脱敏 |
| An information disclosure issue was addressed with improved privacy controls. | 14 | 13 | 6 | 0 | 0 | 0 | 0 | 0 | privacy controls |
| An information disclosure issue was addressed by removing the vulnerable code. | 13 | 13 | 2 | 0 | 0 | 0 | 0 | 0 | 删除脆弱代码 |
| An information disclosure issue was addressed with improved memory management. | 10 | 10 | 1 | 0 | 0 | 0 | 0 | 0 | memory management |
| **合计** | 81 | 页列合计 61 | 名列合计 17 | 10 | 0 | 0 | 0 | 0 | memory management 10／privacy controls 14／state management 24／删除脆弱代码 13／日志脱敏 20 |

## 附表 C 续表：反向刀短语十五项

| 反向刀短语 | 字段 | 全树命中段 | 页 | 名 | X 内段 | 非 X 段 | X 占该短语 |
|---|---|---:|---:|---:|---:|---:|---:|
| `improved state management` | Description | 662 | 176 | 111 | 30 | 632 | 4.5% |
| `improved privacy controls` | Description | 14 | 13 | 6 | 14 | 0 | 100.0% |
| `private data redaction` | Description | 114 | 68 | 22 | 22 | 92 | 19.3% |
| `improved memory management` | Description | 369 | 115 | 26 | 10 | 359 | 2.7% |
| `removing the vulnerable code` | Description | 173 | 97 | 43 | 20 | 153 | 11.6% |
| `improved checks` | Description | 1018 | 192 | 125 | 50 | 968 | 4.9% |
| `improved input validation` | Description | 641 | 151 | 79 | 71 | 570 | 11.1% |
| `improved error handling` | Description | 18 | 18 | 4 | 8 | 10 | 44.4% |
| `improved restrictions` | Description | 47 | 37 | 12 | 0 | 47 | 0.0% |
| `was addressed with` | Description | 6580 | 254 | 363 | 392 | 6188 | 6.0% |
| `kernel memory` | Impact | 520 | 113 | 34 | 91 | 429 | 17.5% |
| `process memory` | Impact | 326 | 110 | 24 | 137 | 189 | 42.0% |
| `user information` | Impact | 184 | 98 | 36 | 75 | 109 | 40.8% |
| `sensitive 词界` | Impact | 1052 | 158 | 142 | 78 | 974 | 7.4% |
| `leak* 词族` | Impact | 181 | 101 | 29 | 18 | 163 | 9.9% |
| **合计** | 15 项 | 11899 | — | — | 1016 | 10883 | 8.5% |

# 附表 D 页面、编号、致谢与版式四组分箱（39 行）

**读法：** 全表分五个块，每块最后一行是**该块的分箱合计并带校验值**（页 **141**、编号 **427**、致谢 **427**），生成器对这三处有 `die` 断言。**"Top10 页逐页"块的合计 110 段是 Top10 页承载的 X 段数（25.8%），不是这 10 页的全部段数**（那 10 页共 1,751 段）；每页的"该页全部段"列给出该页总段数与该页 X 占比。"编号归属"块给出 **90 个独占 ＋ 6 个跨集 ＝ 96**（第 16.3 节），"其余 8,709 段"＝ 9,136 − 427。**编号分箱只有 1 与 2 两箱、0 箱为空，即 427 段全部带编号**；致谢分箱的 7、8 人箱为空、最大值 15 落在"9+"箱。**"同页共存"块给出 141 页共 7,776 段、X 占 5.5%、全 X 页 6 页**（第 16.1 节）。"版式域"行的域 B **14 段**对第 13.3、16.5 节，其中 11 段是模板句。"字段落位"三行是本篇特有的：**致谢侧 11 段命中 ＝ 模板句 11 段（同一批）**，且**同一句 Impact 内 `disclos*` 出现 2 次以上的段 ＝ 0**。

| 块 | 分箱 | 计数 | 说明 |
|---|---|---:|---|
| X 段所在页的段数 | 1 | 51 | 共 141 页含泄露条目 |
| X 段所在页的段数 | 2 | 29 | 共 141 页含泄露条目 |
| X 段所在页的段数 | 3 | 25 | 共 141 页含泄露条目 |
| X 段所在页的段数 | 4 | 8 | 共 141 页含泄露条目 |
| X 段所在页的段数 | 5 | 9 | 共 141 页含泄露条目 |
| X 段所在页的段数 | 6-9 | 13 | 共 141 页含泄露条目 |
| X 段所在页的段数 | 10-19 | 5 | 共 141 页含泄露条目 |
| X 段所在页的段数 | 20-49 | 1 | 共 141 页含泄露条目 |
| X 段所在页的段数 | 合计 | 141 | 校验=141 页、X 段 427 |
| Top10 页逐页 | 149035 | 20 | 该页全部段 314 段，X 占 6.4% |
| Top10 页逐页 | 149034 | 12 | 该页全部段 209 段，X 占 5.7% |
| Top10 页逐页 | 149036 | 11 | 该页全部段 118 段，X 占 9.3% |
| Top10 页逐页 | 149037 | 11 | 该页全部段 128 段，X 占 8.6% |
| Top10 页逐页 | 149038 | 11 | 该页全部段 165 段，X 占 6.7% |
| Top10 页逐页 | 149042 | 10 | 该页全部段 181 段，X 占 5.5% |
| Top10 页逐页 | 128071 | 9 | 该页全部段 151 段，X 占 6.0% |
| Top10 页逐页 | 148287 | 9 | 该页全部段 110 段，X 占 8.2% |
| Top10 页逐页 | 149043 | 9 | 该页全部段 190 段，X 占 4.7% |
| Top10 页逐页 | 128067 | 8 | 该页全部段 185 段，X 占 4.3% |
| Top10 页逐页 | 合计 | 110 | 占 X 段 25.8%（427 段／141 页） |
| X 段内 distinct CVE 数 | 1 | 415 | 427 段中 427 段含编号 |
| X 段内 distinct CVE 数 | 2 | 12 | 427 段中 427 段含编号 |
| X 段内 distinct CVE 数 | 合计 | 427 | 校验=427；全树 distinct 编号 2108 个，X 内 96 个 |
| 编号归属 | 仅出现在 X 段 | 90 | 这 90 个编号不出现在其余 8709 段 |
| 编号归属 | 也出现在非 X 段 | 6 | 与 #124 拒绝服务的 104 个独占编号互不冲突 |
| X 段致谢人数 | 1 | 264 | X 合计 719 人、均值 1.68 |
| X 段致谢人数 | 2 | 117 | X 合计 719 人、均值 1.68 |
| X 段致谢人数 | 3 | 14 | X 合计 719 人、均值 1.68 |
| X 段致谢人数 | 4 | 10 | X 合计 719 人、均值 1.68 |
| X 段致谢人数 | 5 | 6 | X 合计 719 人、均值 1.68 |
| X 段致谢人数 | 6 | 14 | X 合计 719 人、均值 1.68 |
| X 段致谢人数 | 9+ | 2 | X 合计 719 人、均值 1.68 |
| X 段致谢人数 | 合计 | 427 | 校验=427；全树 9132 段均值 1.80、最大 37 |
| 同页共存 | X 页中还有非 X 段的页 | 135 | 141 页如此，仅 6 页全为 X 段 |
| 同页共存 | 这 141 页全部段 | 7776 | X 段 427 占 5.5% |
| 版式域 | 域 B 内 X 段 | 14 | 域 B 共 91 段 |
| 字段落位 | 致谢字段含 disclos* | 11 | Impact 343／Description 81／致谢 11 |
| 字段落位 | 致谢侧命中里等于页面模板句的段 | 11 | 模板句为 Apple doesn't disclose, discuss, or confirm… |
| 字段落位 | 同一 Impact 句 disclos* 出现 2 次以上的段 | 0 | Impact 侧 343 段每段命中次数分布 |

# 附表 E 中文手册层八组对应词（66 行）

**读法：** 本表在《Apple 平台安全保护》zh-CN 全量转储上做**逐词计数**，与英文侧的段计数**不是同一量纲**：一次条目在英文侧算 1 段，在中文侧按出现次数与覆盖页计。**"覆盖页占比"分母固定为 178（可用页数）**，100.0% 表示该词遍布全库、多为一般性用词（内存、密码、数据、隐私、访问、保护），**不可读成"该主题占全部页面"**。八组是 泄露本体 7、被泄露对象 10、隐私与数据 8、保护机制 8、检查与修复 8、类型与后果 8、读与取 8、范围与程度 8 ＝ **65 词**；**全零 10 词**：泄漏、透出、显露、进程内存、凭据、脱敏、沙箱、状态管理、提权、竞态。**其中 `进程内存`（英文第一宾语 137 段）与 `状态管理`（英文最大修复套话 662 段）两个零是第 18.4 节的核心对照**；`泄露` 一词最集中于"密码监视"页（15／30 次），**属密码管理语境而非条目语境**。"最集中页面"列给出该词次数最多的单页与次数，标题为脱敏后的中文页名。

| 组 | 中文词 | 出现次数 | 覆盖页 | 覆盖页占比 | 最集中页面 |
|---|---|---:|---:|---:|---|
| 泄露本体 | 泄露 | 30 | 11 | 6.2% | 密码监视（15 次） |
| 泄露本体 | 披露 | 1 | 1 | 0.6% | App 安全性概览（1 次） |
| 泄露本体 | 泄漏 | 0 | 0 | 0.0% | — |
| 泄露本体 | 外泄 | 1 | 1 | 0.6% | 密码安全建议（1 次） |
| 泄露本体 | 透出 | 0 | 0 | 0.0% | — |
| 泄露本体 | 显露 | 0 | 0 | 0.0% | — |
| 泄露本体 | 透露 | 17 | 12 | 6.7% | 硬件安全性概览（3 次） |
| 被泄露对象 | 内存 | 511 | 178 | 100.0% | 安全隔区（49 次） |
| 被泄露对象 | 内核内存 | 3 | 3 | 1.7% | 搭载 Apple 芯片的 Mac 的启动磁盘安全性策略控制（1 次） |
| 被泄露对象 | 进程内存 | 0 | 0 | 0.0% | — |
| 被泄露对象 | 凭据 | 0 | 0 | 0.0% | — |
| 被泄露对象 | 密码 | 2790 | 178 | 100.0% | 密码监视（66 次） |
| 被泄露对象 | 口令 | 1 | 1 | 0.6% | 密码（1 次） |
| 被泄露对象 | 令牌 | 70 | 15 | 8.4% | 在 macOS 中管理文件保险箱（18 次） |
| 被泄露对象 | 证书 | 150 | 29 | 16.3% | TLS 安全性（17 次） |
| 被泄露对象 | 位置信息 | 7 | 4 | 2.2% | “查找”安全性（2 次） |
| 被泄露对象 | 标识符 | 53 | 26 | 14.6% | iOS 中的车钥匙安全性（8 次） |
| 隐私与数据 | 隐私 | 851 | 178 | 100.0% | 文稿修订记录（14 次） |
| 隐私与数据 | 个人 | 436 | 178 | 100.0% | Apple 设备的无线局域网安全性（12 次） |
| 隐私与数据 | 个人信息 | 189 | 178 | 100.0% | 通过 Apple 登录安全性（4 次） |
| 隐私与数据 | 敏感 | 20 | 13 | 7.3% | WidgetKit 安全性（6 次） |
| 隐私与数据 | 数据 | 3790 | 178 | 100.0% | 保护对用户健康数据的访问（84 次） |
| 隐私与数据 | 日志 | 4 | 3 | 1.7% | 保护对用户健康数据的访问（2 次） |
| 隐私与数据 | 元数据 | 40 | 16 | 9.0% | Apple 设备中的数据保护（6 次） |
| 隐私与数据 | 脱敏 | 0 | 0 | 0.0% | — |
| 保护机制 | 加密 | 1763 | 178 | 100.0% | 安全隔区（71 次） |
| 保护机制 | 保护 | 5438 | 178 | 100.0% | 安全隔区（83 次） |
| 保护机制 | 限制 | 478 | 178 | 100.0% | LocalPolicy 签名密钥创建和管理（21 次） |
| 保护机制 | 隔离 | 10 | 6 | 3.4% | 安全隔区（3 次） |
| 保护机制 | 沙盒 | 27 | 11 | 6.2% | macOS 中的 Option ROM 安全性（6 次） |
| 保护机制 | 沙箱 | 0 | 0 | 0.0% | — |
| 保护机制 | 权限 | 283 | 178 | 100.0% | 操作系统完整性（14 次） |
| 保护机制 | 访问控制 | 16 | 9 | 5.1% | 系统完整性保护（6 次） |
| 检查与修复 | 验证 | 432 | 178 | 100.0% | Apple 钱包中的证件（19 次） |
| 检查与修复 | 校验 | 19 | 3 | 1.7% | Apple 钱包中证件的安全性（16 次） |
| 检查与修复 | 检查 | 66 | 30 | 16.9% | 管理式 Apple 账户安全性（13 次） |
| 检查与修复 | 处理 | 337 | 178 | 100.0% | 安全隔区（24 次） |
| 检查与修复 | 修复 | 14 | 8 | 4.5% | 在 macOS 中防范恶意软件（5 次） |
| 检查与修复 | 补丁 | 4 | 1 | 0.6% | Apple 操作系统中的后台安全改进（4 次） |
| 检查与修复 | 改进 | 231 | 178 | 100.0% | Apple 操作系统中的后台安全改进（38 次） |
| 检查与修复 | 状态管理 | 0 | 0 | 0.0% | — |
| 类型与后果 | 提权 | 0 | 0 | 0.0% | — |
| 类型与后果 | 绕过 | 23 | 9 | 5.1% | 激活锁安全性（12 次） |
| 类型与后果 | 溢出 | 1 | 1 | 0.6% | 内存安全 iBoot 实施（1 次） |
| 类型与后果 | 竞态 | 0 | 0 | 0.0% | — |
| 类型与后果 | 逻辑 | 4 | 2 | 1.1% | 硬件麦克风断联（3 次） |
| 类型与后果 | 越界 | 1 | 1 | 0.6% | 操作系统完整性（1 次） |
| 类型与后果 | 拒绝服务 | 1 | 1 | 0.6% | macOS 中的防火墙安全性（1 次） |
| 类型与后果 | 可用性 | 7 | 6 | 3.4% | 钥匙串数据保护（2 次） |
| 读与取 | 读取 | 59 | 22 | 12.4% | Tap to Pay on iPhone 安全性（10 次） |
| 读与取 | 访问 | 1880 | 178 | 100.0% | 保护对用户健康数据的访问（32 次） |
| 读与取 | 获得 | 50 | 31 | 17.4% | 保护对用户健康数据的访问（5 次） |
| 读与取 | 获取 | 39 | 28 | 15.7% | 激活锁安全性（4 次） |
| 读与取 | 暴露 | 2 | 2 | 1.1% | 关于 App Store 安全性（1 次） |
| 读与取 | 显示 | 119 | 53 | 29.8% | WidgetKit 安全性（12 次） |
| 读与取 | 查看 | 38 | 19 | 10.7% | “备忘录” App 中的安全功能（6 次） |
| 读与取 | 窃取 | 4 | 2 | 1.1% | Apple 操作系统中的量子安全加密技术（3 次） |
| 范围与程度 | 任意 | 9 | 8 | 4.5% | 搭载 Apple 芯片的 Mac 的启动磁盘安全性策略控制（2 次） |
| 范围与程度 | 全部 | 4 | 4 | 2.2% | 密码（1 次） |
| 范围与程度 | 所有 | 525 | 178 | 100.0% | 接入无线网络时的安全功能（22 次） |
| 范围与程度 | 部分 | 112 | 73 | 41.0% | 术语表（6 次） |
| 范围与程度 | 可能 | 128 | 74 | 41.6% | Apple 操作系统中的后台安全改进（4 次） |
| 范围与程度 | 无法 | 143 | 72 | 40.4% | iCloud 高级数据保护（9 次） |
| 范围与程度 | 未 | 185 | 83 | 46.6% | 激活锁安全性（9 次） |
| 范围与程度 | 已 | 472 | 178 | 100.0% | 在 macOS 中防范恶意软件（11 次） |
| **合计** | 65 词（全零 10 词、非零 55 词） | — | — | 178 页可用 | — |

# 附表 F 组件名层五十四名 × 五个外部集 × 该组件 Impact 全集（55 行）

**读法：** 每行是一个组件名，"X 段"＝该名下被 X 抓到的段数（合计行 **427**，生成器断言）。**"该组件含 Impact 全集"列是该组件在 Impact 字段的全部段数（不限判据）**，"X 占比"＝ 前者除以后者，故 `IOSkywalkFamily` **100.0%**（8／8）一类是**全命中名**，共 **6 名**（另 5 名是 `QuickLook` 4/4、`CoreDrag` 3/3、`File System` 3/3、`Keynote` 1/1、`Pages` 1/1）。∩R／∩WO／∩E／∩Ds 四列按该行段集合与外部集求交：**∩R 只有 4 名非零（Authentication Services 4、WebKit 3、mDNSResponder 3、Bluetooth 1 ＝ 11）、∩WO 全名 0、∩E 只有 WebKit 2、∩Ds 只有 8 名非零（CoreGraphics 16 名里 8 段、CoreMedia 9 名里 8 段、file 4、Model I/O 3、Ruby 3、AppleVA 2、ColorSync 1、Spotlight 1 ＝ 30）**。**合计行"该组件含 Impact 全集 3,652"是 54 名的全集段数之和（各名互不重叠，可直接相加），X 占其 11.7%、该 3,652 段占全树 7,434 的 49.1%**；页列合计 372 是逐行去重再求和，**不是 141**。**与 #124 的 62 名重合 25 名（第 15.3 节），重合是名级、不是段级。**

| 组件名 | X 段 | 页 | ∩R | ∩WO | ∩E | ∩Ds | 该组件含 Impact 全集 | X 占比 |
|---|---:|---:|---:|---:|---:|---:|---:|---:|
| `Kernel` | 76 | 52 | 0 | 0 | 0 | 0 | 681 | 11.2% |
| `ImageIO` | 63 | 55 | 0 | 0 | 0 | 0 | 261 | 24.1% |
| `WebKit` | 53 | 35 | 3 | 0 | 2 | 0 | 910 | 5.8% |
| `CoreText` | 18 | 18 | 0 | 0 | 0 | 0 | 33 | 54.5% |
| `CoreGraphics` | 16 | 13 | 0 | 0 | 0 | 8 | 27 | 59.3% |
| `SceneKit` | 16 | 16 | 0 | 0 | 0 | 0 | 112 | 14.3% |
| `FontParser` | 13 | 13 | 0 | 0 | 0 | 0 | 29 | 44.8% |
| `Siri` | 12 | 12 | 0 | 0 | 0 | 0 | 116 | 10.3% |
| `CoreMedia` | 9 | 9 | 0 | 0 | 0 | 8 | 150 | 6.0% |
| `Model I/O` | 9 | 8 | 0 | 0 | 0 | 3 | 88 | 10.2% |
| `Foundation` | 8 | 8 | 0 | 0 | 0 | 0 | 67 | 11.9% |
| `IOSkywalkFamily` | 8 | 8 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| `NetworkExtension` | 8 | 8 | 0 | 0 | 0 | 0 | 28 | 28.6% |
| `Audio` | 7 | 7 | 0 | 0 | 0 | 0 | 95 | 7.4% |
| `Messages` | 7 | 7 | 0 | 0 | 0 | 0 | 50 | 14.0% |
| `RealityKit` | 7 | 7 | 0 | 0 | 0 | 0 | 14 | 50.0% |
| `libarchive` | 6 | 6 | 0 | 0 | 0 | 0 | 30 | 20.0% |
| `CoreUI` | 5 | 5 | 0 | 0 | 0 | 0 | 32 | 15.6% |
| `libnetcore` | 5 | 5 | 0 | 0 | 0 | 0 | 11 | 45.5% |
| `Apple Neural Engine` | 4 | 4 | 0 | 0 | 0 | 0 | 36 | 11.1% |
| `Authentication Services` | 4 | 4 | 4 | 0 | 0 | 0 | 18 | 22.2% |
| `CloudKit` | 4 | 4 | 0 | 0 | 0 | 0 | 25 | 16.0% |
| `QuickLook` | 4 | 4 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| `file` | 4 | 4 | 0 | 0 | 0 | 4 | 7 | 57.1% |
| `CUPS` | 3 | 3 | 0 | 0 | 0 | 0 | 43 | 7.0% |
| `CoreDrag` | 3 | 3 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| `Disk Images` | 3 | 3 | 0 | 0 | 0 | 0 | 54 | 5.6% |
| `File System` | 3 | 3 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| `GPU Drivers` | 3 | 3 | 0 | 0 | 0 | 0 | 34 | 8.8% |
| `LaunchServices` | 3 | 3 | 0 | 0 | 0 | 0 | 58 | 5.2% |
| `Ruby` | 3 | 3 | 0 | 0 | 0 | 3 | 6 | 50.0% |
| `SMB` | 3 | 3 | 0 | 0 | 0 | 0 | 58 | 5.2% |
| `Spotlight` | 3 | 3 | 0 | 0 | 0 | 1 | 69 | 4.3% |
| `WebContentFilter` | 3 | 3 | 0 | 0 | 0 | 0 | 11 | 27.3% |
| `WebDAV` | 3 | 3 | 0 | 0 | 0 | 0 | 20 | 15.0% |
| `mDNSResponder` | 3 | 3 | 3 | 0 | 0 | 0 | 53 | 5.7% |
| `sips` | 3 | 3 | 0 | 0 | 0 | 0 | 25 | 12.0% |
| `AVEVideoEncoder` | 2 | 2 | 0 | 0 | 0 | 0 | 45 | 4.4% |
| `Accessibility` | 2 | 2 | 0 | 0 | 0 | 0 | 46 | 4.3% |
| `Apple Account` | 2 | 2 | 0 | 0 | 0 | 0 | 25 | 8.0% |
| `AppleVA` | 2 | 2 | 0 | 0 | 0 | 2 | 7 | 28.6% |
| `exFAT` | 2 | 1 | 0 | 0 | 0 | 0 | 7 | 28.6% |
| `Bluetooth` | 1 | 1 | 1 | 0 | 0 | 0 | 41 | 2.4% |
| `ColorSync` | 1 | 1 | 0 | 0 | 0 | 1 | 4 | 25.0% |
| `FaceTime` | 1 | 1 | 0 | 0 | 0 | 0 | 14 | 7.1% |
| `Finder` | 1 | 1 | 0 | 0 | 0 | 0 | 10 | 10.0% |
| `IOMobileFrameBuffer` | 1 | 1 | 0 | 0 | 0 | 0 | 22 | 4.5% |
| `Keynote` | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| `Managed Configuration` | 1 | 1 | 0 | 0 | 0 | 0 | 17 | 5.9% |
| `Music` | 1 | 1 | 0 | 0 | 0 | 0 | 10 | 10.0% |
| `Pages` | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| `Safari` | 1 | 1 | 0 | 0 | 0 | 0 | 92 | 1.1% |
| `TCC` | 1 | 1 | 0 | 0 | 0 | 0 | 29 | 3.4% |
| `afpfs` | 1 | 1 | 0 | 0 | 0 | 0 | 12 | 8.3% |
| **合计** | 427 | 页列合计 372 | 11 | 0 | 2 | 30 | 3652 | 11.7% |

---

**规则：** 本篇所有数字由 `_tmp/tw125tab.pl`（附表 A–F）与 `tw125probe.pl`／`tw125p2.pl`／`tw125p3.pl`／`tw125p5.pl`／`tw125p6.pl`／`tw125p7.pl`（正文）在同一 `%B` 上产出，判据一律以词界与字段名写出、可用第 20 章逐步复算；分母一律注明为 9,136（全树段）／7,434（含 Impact 段）／7,776（X 所在 141 页全部段）／3,652（附表 F 54 名 Impact 全集）／9,045∶91（域 A∶域 B）；类次 423 与段次 427 分列报出，不互换；`DIS` 类 Impact 口径 361 与并集口径 427 分列报出；附表 C 续表的 **1,016 是行和、424 是去重段数**，两者不互换；附表 B、C、F 的页列与名列是逐行去重后的再求和，不得当作集合基数引用；凡"A 与 B 得数相同"处必须同时给出 |A|、|B|、|A∩B| 与差段清单（第 3、16.4、17 章）。**

**边界声明：** 本篇是**公开英文与中文文本的措辞计量**，不是脆弱性评估、不是攻击可行性研究，也不提供任何绕过方法、利用代码、样本、利用链构造或针对他人设备的操作步骤与时序。文中"信息泄露"一律指**公开条目把影响自述为 disclosure、或把修复自述为 information disclosure issue 这一文本事实**，不推断该缺陷是否真的读出敏感数据、其数据范围与机密性影响如何、其触发是否可远程、是否已被利用；427 段、96 个编号、423 类次、54 个组件名、11 段模板句、2 段在野标注等数字**只描述文本分布**，不构成任何组件机密性评级、缺陷优先级排序、可达性或攻击路径存在的结论。

Telegram: https://t.me/one00190
