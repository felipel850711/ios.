@@TITLE@@iOS 媒体数据解析与资源管理类弱点的公开措辞口径分析
@@SUBTITLE@@选题是"媒体数据（音视频文件与流）的解析、格式与容器处理，以及资源与内存对象的管理、分配与释放"这一类措辞。三刀逐词测量给出四条反直觉结论。**第一，本篇的真实主题既不是"媒体"也不是"解析"，而是"管理"这一个词**：刀一（媒体本体）219 段、刀二（解析与格式）247 段、刀三（资源管理）1,105 段，而刀三里的 `manag*` 一个词族就带进 **1,045 段＝主集 1,541 段的 67.8%**；把 `manag*` 摘掉，主集只剩 **496 段**。**第二，三刀各自"字段单侧"，合起来才跨界**：`use-after-free` 一出现就写在 Description、`Processing a maliciously crafted media file` 一出现就写在 Impact——**刀一 I∩D ＝ 0、刀二 I∩D ＝ 0、刀三 I∩D ＝ 0，三刀并集的 I∩D ＝ 17 段**，这 17 段全是"Impact 讲媒体／解析、Description 讲释放与管理"的跨刀配对（一I·三D 9 段＋二I·三D 8 段）。**第三，词界断言第三次咬人，但咬的是尾词不是连字符**：本篇去掉前置 `(?<![\w-])` 会让主集从 1,541 涨到 **1,975 段（＋434）**，而新增的 439 次命中里绝大多数是 **`information` 一词内部藏着的 `format`**（`format\w*` 贪婪吞下 "in-FORMAT-ion"）；反过来去掉尾断言 `(?![\w-])` **一段都不多（＋0）**——这是 #132（`out-of-bounds` 漏 193 段）与 #133（同断言挡 162 段）的镜像：**同一根断言，在图像处理篇挡的是连字符，在本篇挡的是复合词。** **第四，`container` 是本系列迄今最干净的假阳性样本**：刀二的 15 段 `container(s)` 命中**没有一段在讲媒体容器**，12 段是 `improved restriction of data container access.`（沙盒数据容器）、3 段是 `within an App Sandbox container`（应用沙盒容器），**媒体容器格式这个词在苹果的安全措辞里一次都没有出现**。本篇主集 **RESM ＝ 1,541 段／208 页／166 名**，占全树 **16.87%**、占含 Impact 段的 **20.73%**；1,541 段由**三刀两字段、15 个句形桶与 59 个不同句子**写成 1,559 句次，**27 句只在 Impact、32 句只在 Description、0 句两侧共用**；Impact 判据句 373 句里 **`may` 359、`could` 5、无情态 9**，`can／will／would／might／must／should` 全 0，而 Description 侧 1,186 句**一个情态动词都没有**。
@@INFO@@报告日期|2026 年 11 月 1 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（258 份文件排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 9,132 段含致谢**。本篇主集 RESM ＝ 刀一 `media*｜video*｜audio*｜sound*｜movie(s)｜film(s)｜stream(s)｜playback｜player｜playing` 或刀二 `pars*｜decod*｜encod*｜format*｜container(s)` 或刀三 `manag*｜allocat*｜resource(s)｜exhaust*｜cache*｜pool(s)｜free｜freed｜freeing｜garbage｜dangling｜uninitialized｜retain*｜releas*` 出现在 **Impact 或 Description** ＝ **1,541 段 / 208 页 / 166 名**，其中 Impact 侧 **372** 段、Description 侧 **1,186** 段；字段组合 **仅 I 355／仅 D 1,169／I∩D 17**（相加 1,541）；致谢字段命中 **55** 段、其中 **36** 段在主集之外，三字段并集 ＝ **1,577 段 / 217 页 / 166 名**。三刀定义（两字段口径）：刀一 **219** 段（Impact 215∶Description 4）、刀二 **247** 段（149∶98）、刀三 **1,105** 段（21∶1,084）；三刀并集与合成词表 `$re3` 逐段相等，**差 0 段**。闭合分区（优先序 一→二→三）：仅一 197／仅二 227／仅三 1,087／一二共现 12／一三共现 10／二三共现 8／三刀同段 **0**，六项相加 ＝ 1,541。对照集：**R** ＝ #118 远程触达词族 **843 段 / 178 页 / 72 名**；**E** ＝ #114 在野集 **108 段 / 76 页 / 12 名**；**WO** ＝ #119 无前提集 **178 段**；**G／HI** ＝ 动名词开头集 **2,179 段** 与交互触发集 **273 段**；**M2** ＝ 同段 ≥2 后果类集 **185 段**；**UU** ＝ Impact 含 `user` **1,627 段**；**RC** ＝ #122 竞态集 **232 段**；**L** ＝ #123 逻辑集 **550 段**；**Ds** ＝ #124 拒绝服务集 **470 段**；**X** ＝ #125 泄露集 **427 段**；**BP** ＝ #126 绕过集 **301 段**；**PMS** ＝ #129 三族并集 **2,213 段 / 209 页 / 130 名**；**TLO** ＝ #130 三刀并集 **343 段 / 120 页 / 27 名**；**WEB** ＝ #131 三刀并集 **1,253 段 / 184 页 / 45 名**；**IMGX** ＝ #132 主集 **1,547 段**（本篇附表 A 与 15.5 用重建的两字段合成词表口径 **1,543 段 / 211 页 / 133 名**）；**RESX** ＝ #133 主集 **2,791 段 / 206 页 / 154 名**；**CK** ＝ #127 修复动词词族 **4,146 段 / 244 页 / 304 名**；**MEM** ＝ `memor*` 词族 **1,967 段**；**BND** ＝ `out of bounds` 两形 **816 段**。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或利用链构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**"媒体""音视频""流""播放""解析""格式""容器""管理""分配""释放""缓存""资源耗尽""未初始化""悬挂"这些说法在公开英文文本的哪个字段、哪一句位、与哪一个后果短语搭配，以及它与远程、在野、后果强度、机制名之间的集合关系**，属于措辞与文本计量分析；文中任何"恶意媒体文件""解析缺陷""资源耗尽""释放后使用""未初始化内存"表述均指**公开条目使用了该词或该短语这一文本事实**，不是对该条目真实缺陷类别、CWE 归属、是否可被远程触达、是否被利用的判断，也不构成任何媒体栈、解析器或内存管理器安全性的结论
@@INFO@@分类|判据**六组 192 项 / 并集全零 16 项 / 非零 176 项**（附表 A，六组：本体词 69、句级刀 32、机制名与检测词对照 45、修复套话句 15、外部集对照 12、后果强度八类 8）/ **五十九个含主刀整句 × 归桶 × 句次 × I∶D × 页 × ∩R ∩E ∩M2 ∩L ∩RC**（附表 B，60 行含合计，句次合计 **1,559 ＝ Impact 373 ＋ Description 1,186**、页列合计 1,012）/ **十五个句形桶 × 句次 × 段页名 × I∶D × ∩R ∩E，续表八类句式形态**（附表 C，16 行含合计，桶句次闭合 **1,559**；续表 **A／An X issue was addressed 487／A／An X（非 issue 名词）was addressed 398／The／This issue was addressed 291／含 may 359／X existed 12／was addressed with 3／Multiple 4／其余 5** 闭合 1,559）/ **页面、编号、致谢、版式与字段落位五类十块分箱**（附表 D，30 行，页分箱闭合 208、编号分箱闭合 1,541、致谢分箱闭合 1,538）/ **中文层 64 词**（附表 E，八组：媒体与音视频类 8、解析与格式类 8、资源与管理类 8、生命周期与越界类 8、后果强度类 8、修复套话类 8、组件与内核类 8、通道与触发类 8；**全零 12 词、非零 52 词**）/ **组件层 166 名**（附表 F，167 行含合计，段合计 1,541、这 166 名的 Impact 全集 **5,770** 段、占全树含 Impact 段的 **77.6%**）/ **十一把刀**：字段刀（Impact 372 ∶ Description 1,186 ∶ 致谢 55，两字段拆分 仅I 355／仅D 1,169／I∩D 17）、逐刀字段刀（刀一 215∶4、刀二 149∶98、刀三 21∶1,084，**三刀各自 I∩D ＝ 0**）、词族级落位刀（`media*` 102∶0、`video*` 83∶0、`pars*` 146∶83、`manag*` 7∶1,038、`free` 0∶170、`cache*` 0∶29、`format*` 0∶3、`allocat*` 0∶4、`resource*` 0∶5、`exhaust*` 0∶5 等**十六个词族的 I∩D 全部为 0**）、概念刀（`out of bounds` 两形 816 段∩主集 **143**；`use-after-free` 连字符形 193∩主集 **189**、`use after free` 空格形 127∩主集 **127**、两形**交 0 并 316**）、组件名刀（`nm` 含媒体／音视频关键字的 592 段里 **240** 段进主集，主集 1,541 段里 **1,301 段（84.4%）**的组件名不含这类关键字）、句库刀（1,559 句次只由 **59** 个不同句子写成，top1 占 **15.20%**、top3 占 **37.52%**、top5 占 **50.35%**、top10 占 **70.04%**）、标点刀（本篇**句号异形可合并组 ＝ 1**，stem `An authorization issue was addressed with improved state management` 带句号 **159** 次、不带句号 **1** 次；正文实体 `&rsquo;` **8** 处、`&amp;` **0** 处）、致谢刀（三字段并集比两字段多 **36** 段，这 36 段**全部是污染**：10 段是页面免责声明模板、26 段是职称 `Manager of Cyber Security at Suma Soft Pvt. Ltd`）、`manag*` 摘除刀（主集 **1,045 段（67.8%）**含 `manag*`，摘除后剩 **496 段**）、词界断言刀（去掉 `(?<![\w-])` 主集 **1,975 段（＋434）**，其中 `information` 内的 `format` 贡献 439 次命中、`use-after-free` 的 4 段 CFNetwork 条目回补；去掉 `(?![\w-])` **＋0 段**）、缺席刀（`sound*／movie(s)／film(s)／speaker／play／parse／decod*／encod*／pool(s)／freed／freeing／garbage／dangling／improved initialization` **十四项三字段口径全 0**）
@@INFO@@难度|进阶（本篇的 1,541 段有**三套分母**：占全树 9,136 段是 **16.87%**、占含 Impact 的 7,434 段是 **20.73%**、占其所在 208 页全部 8,981 段是 **17.16%**（该 208 页每页平均 7.41 段），三个数都真、不可混用；第四套是**版式域**分母——域 A **1,538／9,045 ＝ 17.00%**、域 B **3／91 ＝ 3.30%**，本篇在域 B 的占比只有域 A 的 **五分之一**，是 #131（域 B 是域 A 的 2.4 倍）、#132（略低）、#133（几乎相同）之后第四种形态；**1,541 是两字段主集、1,577 是三字段并集、1,559 是句次合计、372 与 373 是两个不同的 Impact 口径**——372 是"Impact 字段含三刀词的段数"、373 是"Impact 字段含三刀整句的句数"，差 1 是因为有一段写了两句判据句。1,541 段里 **1,523 段只含 1 个本篇整句、18 段含 2 句、0 段一句都没有**，所以句次比段数多 18；**"零句段 0 段"延续 #133**，与 #132 的"4 段零句"不同——但本篇同样存在 **4 段"连字符无句"段**（页 `124148–124151`，CFNetwork，Description 只写 `A use-after-free issue was addressed by removing the vulnerable code.`，前置断言把 `after`、`free` 两处都挡在门外，这 4 段整体落在主集之外，且在第 5 章的反事实里正好被"去断言"回补）；**凡"A 与 B 得数相同"处必须同时给出 \|A\|、\|B\|、\|A∩B\|**——第 15 章的成员级核验结论是：**R∩主集 199 与 TLO∩主集 199 交 28（既非同集也非子集）**、**UU∩主集 342 与 WEB∩主集 342 交 46（完全不同集）**、**E∩主集 45 与 HI∩主集 45 交 0（完全不相交）**、**`cache*`∩主集 29 与 `stream(s)`∩主集 29 交 0（完全不相交）**、**`media*` ＝ `media file` ＝ `crafted media file` ＝ 102（三处真同集，两两交 102）**、**`resource(s)` ＝ `resource exhaustion` ＝ `exhaust*` ＝ 5（三处真同集）**；附表 B、C、F 的页列与名列是逐行去重后的再求和，**不得当作并集去重值**（B 页列 1,012、C 页列 686／名列 229、F 页列 1,231，并集真值是 208 页／166 名）；附表 B 与 F 的外部集列同理是**逐行和**：B 的 ∩R 行和 199 恰等于并集真值 199、∩E 行和 46 对并集真值 45、∩L 95 对 95、∩RC 11 对 11；F 的 ∩E 行和 45 对并集真值 45；附表 A「后果强度八类」∩主集列是**三字段口径**（八类相加 **834**、并集 **783**、差 **51**），第 9 章正文另给 **Impact 单字段口径**（相加 **762**、并集 **742**、差 **20**），两套口径都不是主集段数 1,541；附表 A 的句级刀 ∩主集列是**段级命中**、附表 C 的段列是**每桶去重段数**（15 桶相加 **1,559** 句次而段列 **1,541**，本篇一段两句的只有 18 段且两句分属不同桶的情况为 0，所以桶层没有出现 #133 那种"相加 3,463 ＞ 2,791"的溢出））
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

**选题给的是三个并列说法："媒体数据（音视频文件、流）的解析与格式处理、容器与元数据处理、以及资源与内存对象的管理分配与释放。"这三个说法在 Apple 安全响应顾问页里是不是三个可分的词族？** 本篇把它们拆成十二个可逐词回答的断言：

1. "媒体"用什么词写？（第 2 章：`media*` **102** 段、`video*` **83** 段、`audio*` **63** 段三词撑起刀一 219 段；`sound*`、`movie(s)`、`film(s)`、`speaker`、`play` 五个日常说法**全为 0 段**；`stream(s)` 的 29 段与 `playback／player／playing` 的 8 段**全部被 media 吸收**，独立贡献 0。）
2. "解析与格式"用什么词写？（第 3 章：`pars*` **229** 段一个词族就是刀二的 92.7%，且 `pars*` 的全部命中形态都是动名词 `parsing`——`parse` 的动词原形与完成时在正文里 **0 段**；`decod*／encod*` 编解码词族 **0 段**；`format*` 只有 **3 段**且全部是 `uncontrolled format string` 这个 CWE 套话。）
3. `container` 有没有真的在讲媒体容器？（第 3.4 节：**一段都没有**。刀二的 15 段 `container` 是 12 段 `data container`（沙盒数据容器访问限制）＋ 3 段 `App Sandbox container`（应用沙盒目录），**媒体容器格式的语义在本篇为零，这是一个整族假阳性。**）
4. "资源管理"用什么词写？（第 4 章：不是 `pool`（**0 段**）、不是 `garbage／dangling`（各 **0 段**）、不是 `freed／freeing`（**0 段**）；是 `manag*` 一个词族 **1,045 段＝主集的 67.8%**，其中 `state management` **662** 与 `memory management` **369** 两句话式（交 0）就写完 1,031 段。）
5. 三刀落在哪一字段？（第 6 章：**刀一 215∶4、刀二 149∶98、刀三 21∶1,084，且每一刀的 I∩D 都是 0**；三刀并集的 I∩D ＝ 17 段，全部是跨刀配对。**"媒体词写在影响句、释放词写在修复句"是三刀之间的分工，不是任一刀的两侧命中。**）
6. `free` 与 `manag*` 是什么关系？（第 4.3 节：`free／freed／freeing` 词界命中 **170 段**，这 170 段**100% 同时含 `manag*`**——全部是 `improved memory management` 句里的主语补足；`free` 作为独立资源词**一段都不在 manag 之外**。）
7. 这批条目的句子有多模板化？（第 7 章：**59 个不同句子写完 1,559 句次**；top1 是 `This issue was addressed through improved state management.` **237 句次＝15.20%**，top10 占 **70.04%**；另有 **1 组句号异形**（同一题干带点 159 次、不带点 1 次）。）
8. 后果与情态怎么写？（第 8、9 章：Impact 判据句 373 句里 `may` **359**、`could` **5**、无情态 **9**，`can／will／would／might／must／should` **各 0**；Description 判据句 1,186 句**情态 0**。八类后果 Impact 口径命中 **742 段＝主集的 48.1%**、**799 段（51.9%）一个后果类都不命中**；同段命中 ≥2 类的主集段只有 **15 段**。）
9. 三刀与 #132／#133 的真实重合有多大？（第 15.5 章：**∩RESX 683 段＝主集的 44.32%、∩IMGX 351 段＝22.78%**；把 `memor*` 并进主刀做反事实，主集涨到 **2,948 段**、与 #133 的重合涨到 **2,090 段＝70.90%**。**本篇与"资源边界篇"的四成五重合完全靠 `memory management` 一句套话撑着。**）
10. 词界断言在本篇咬掉了什么？（第 5 章：与 #132／#133 相反——**本篇前置断言是"功臣"不是"债主"**：它挡掉 434 段纯由 `information` 内嵌 `format` 造成的假命中，代价是把 4 段 `use-after-free` 的 CFNetwork 条目一并挡在门外；尾断言 `(?![\w-])` 对本篇主刀的增删是 **0 段**。）
11. 致谢字段能不能进判据？（第 12.4 节：**不能**。三字段并集比两字段多 **36** 段，其中 **10 段**是页面免责声明模板（被 `releas*` 命中）、**26 段**是研究者职称 `Manager of Cyber Security`（被 `manag*` 命中）；**36 段没有一段是媒体或资源类条目。**）
12. `analysis` 算不算"解析"？（第 3.5 节：**不算**，而且这个候选词族的命中 100% 来自致谢机构名 `Google Threat Analysis Group`——全树 94 段，**Impact 0 段、Description 0 段**；若并入，两字段主集**一段不多（仍 1,541）**，只把三字段并集从 1,577 抬到 1,630。**这是本系列第一次出现"某词族的两字段命中为零"。**）

**本篇不给任何利用方法、不给任何针对他人设备的操作时序，也不推断任何条目真实的缺陷类别、CWE 归属或可利用性。** 所有结论只是"哪个词在哪个字段、哪一句位出现过多少次、与哪一组段落相交"。

# 1 口径、判据与四套分母

## 1.1 三刀的定义

本篇主集写作 **RESM**，定义为：

- **刀一（媒体本体）** `media*｜video*｜audio*｜sound*｜movie(s)｜film(s)｜stream(s)｜playback｜player｜playing`
- **刀二（解析与格式）** `pars*｜decod*｜encod*｜format*｜container(s)`
- **刀三（资源与对象管理）** `manag*｜allocat*｜resource(s)｜exhaust*｜cache*｜pool(s)｜free｜freed｜freeing｜garbage｜dangling｜uninitialized｜retain*｜releas*`

三支刀一律带 `(?<![\w-])` 前置词界断言与 `(?![\w-])` 尾断言；命中 **Impact 或 Description** 任一字段的段进入主集（**两字段口径**），致谢字段只做哨兵不参与判据。这与 #127–#133 同构。

四条必须区分的口径：

| 口径 | 记号 | 段数 | 这是什么 |
|---|---|---:|---|
| 两字段主集 | RESM ＝ CU | **1,541** | 三刀命中 Impact 或 Description |
| 三字段并集 | ALL3 | **1,577** | 再并入致谢字段，多 **36** 段（全部污染，见 12.4） |
| Impact 段数 | CI | **372** | Impact 字段含三刀词的**段**数 |
| Impact 句数 | — | **373** | Impact 字段含三刀**整句**的句次数（见 1.3） |

三刀并集与合成词表 `$re3`（把三刀并成一张词表）**逐段相等，差 0 段**——本篇不存在 #132 那种"子刀有、合并词表没有"的漏项。

## 1.2 四套分母

1,541 段可以被四组分母除，四个商都真：

- 全树段 **9,136** → **16.87%**
- 含 Impact 的段 **7,434** → **20.73%**
- 本篇所在 208 页的全部段 **8,981** → **17.16%**（该 208 页共 208 页 × 平均 7.41 段；这是"同页共存"分母，衡量的是"在这些页面上本篇占了多大版面"）
- 版式域：**域 A 1,538／9,045 ＝ 17.00%**、**域 B 3／91 ＝ 3.30%**

第四个分母是本篇的特殊之处：**域 B（粗体版式的 11 页）在本篇几乎没有存在感**。域 B 的 3 段全部由 `manag*` 一个词族带入，逐段是：

- 页 `120303`（Bluetooth）：`A session management issue was addressed with improved checks.`
- 页 `120304`（Notes）：`This issue was addressed through improved state management.`
- 页 `120307`（LoginWindow）：`An authentication issue was addressed with improved state management.`

**域 B 全段 91、域 A 全段 9,045**，本篇在域 A 的密度（17.00%）是域 B（3.30%）的 **5.2 倍**。这与 #133 几乎持平（30.55% 对 30.77%）、#132 明显偏低、#131 反向富集都不同——**同一篇报告在两个版式域上的密度关系，是判据词族的函数，不是页面模板的函数**：域 B 是"每页条目少、句子短"的新式模板，而本篇的判据词集中在旧式长条目里。

## 1.3 段、句、字段三个计数轴

- 段轴：主集 1,541 段。
- 句轴：附表 B 合计 **1,559 句次** ＝ Impact **373** ＋ Description **1,186**；distinct 整句 **59** 个。
- 字段轴：CI 372 段、CD 1,186 段，拆分为 **仅I 355／仅D 1,169／I∩D 17**，相加 1,541；**CD 的段数 1,186 与 Description 侧句次 1,186 相等不是巧合**——本篇没有任何一段在 Description 字段写出两句判据句（附表 D 的"1 句 1,523／2 句 18"里，那 18 段的多出的一句全部落在 Impact 侧）。

1,523 ＋ 18 ＝ 1,541，1,541 ＋ 18 ＝ 1,559：段数加"双句段数"等于句次，这是本篇的算术闭合。

## 1.4 优先序闭合分区

三刀共现的可能性有八种，本篇把它们按 一→二→三 的优先序做**闭合分区**（附表 A 的四行"仅刀"与三行"共现"与一行"三刀同段"）：

| 分区 | 段数 | 页 | 名 | 说明 |
|---|---:|---:|---:|---|
| 仅刀一 | 197 | 107 | 13 | 纯媒体本体词 |
| 仅刀二 | 227 | 94 | 43 | 纯解析词 |
| 仅刀三 | 1,087 | 189 | 136 | 纯管理／释放词 |
| 刀一∩刀二 | 12 | 12 | 3 | `Parsing a maliciously crafted video file…` |
| 刀一∩刀三 | 10 | 10 | 3 | `…media file…` ＋ `use after free…memory management` |
| 刀二∩刀三 | 8 | 8 | 1 | `Parsing a file…` ＋ `use-after-free…memory management` |
| 三刀同段 | **0** | 0 | 0 | **一段都没有** |
| **合计** | **1,541** | — | — | 六项相加闭合 |

**三刀同段 0 是本篇的结构性事实**：没有任何一个条目的两句判据句同时把媒体、解析、资源三族写全。三族在文本上是**互斥叙述线**——这个性质在第 6 章的字段刀里会以另一种形式再出现一次（每刀 I∩D ＝ 0）。

## 1.5 三个反向对照：本篇不取的三个词族

选题字面上还包括三个说法，本篇**测过但明确剔除**，并给出剔除量：

- **`analysis／analyse／analyz*`**（"分析"与"解析"的英文歧义词）：全树三字段 **94 段**，Impact **0**、Description **0**；∩主集 40（这 40 段主集命中来自别的词）、主集外 54。剔除前后两字段主集**都是 1,541**（详见 3.5）。
- **`camera／microphone`**（输入侧设备）：三字段 **13 段**，∩主集 4。本篇不取，留给 #135 的采集与传感器通道选题。
- **`song／album`**（曲目）：三字段 **56 段**，∩主集 7。56 段里绝大多数来自音乐类组件的**目录名与文件名**，与缺陷措辞无关。

剔除的三个词族合计 ∩主集 **11 段＝主集的 0.71%**——选题的语义边界即使放宽，主集几乎不动；**这说明本篇的主集边界不是被词表选择决定的，而是被 `manag*` 一个词族决定的**（第 4 章）。

# 2 刀一：媒体本体 219 段，其中 215 段只在 Impact

## 2.1 刀一内部的优先序闭合

刀一 219 段按 `media → video → audio → stream → playback → 其余` 优先序归词族：

| 词族 | 三字段 | 两字段 | ∩主集 | 优先序独占 | 说明 |
|---|---:|---:|---:|---:|---|
| `media*` | 109 | **102** | 102 | **102** | 全部是 `media file` |
| `video*` | 83 | **83** | 83 | **83** | `video file` 74 段 |
| `audio*` | 63 | **63** | 63 | **34** | 被 media 吸收 29 段 |
| `stream(s)` | 29 | 29 | 29 | **0** | 29 段全含 `audio stream` |
| `playback／player／playing` | 8 | 8 | 8 | **0** | 8 段全含 media 或 audio |
| `voice／speech` | 3 | 3 | 3 | — | 语音，刀一边缘 |
| `play`（单词界） | **0** | **0** | 0 | 0 | 全树 0 段 |
| `sound*` | **0** | **0** | 0 | 0 | 全树 0 段 |
| `movie(s)`／`film(s)` | **0** | **0** | 0 | 0 | 全树 0 段 |
| `speaker` | **0** | **0** | 0 | 0 | 全树 0 段 |
| **刀一并集** | — | **219** | 219 | **219** | 闭合 102＋83＋34 ＝ 219 |

**三个日常说法一个都没被用上。** 苹果描述"一段视频"用的是 `video file`（74 段）、描述"一部影片"时**不写 movie 也不写 film**——`movie*` 在正文里出现的唯一形态是组件名 `Video`、`CoreMedia Playback` 这类专名，而不是缺陷描述词。`play` 的单词界（`Playing` 以外的动词形式）**0 段**，而 `playing` 的 8 段全部出现在 `Playing a malicious audio file may lead to an unexpected app termination.` 这一句里（附表 C 的 8 句次桶），也就是说 **`play` 只有作为 `-ing` 动名词起手时才被写出来**。

## 2.2 `media*` 的三字段 109 与两字段 102

`media*` 词族在致谢字段多命中 7 段，这 **7 段全部在主集之内**（它们的 `media` 一词只写在致谢的组件／机构名里，正文两字段本来已由别的词带入）。这是"三字段比两字段多"的**第二种机制**：不是新增条目，而是同一条目内命中词的字段换位。与第 12.4 节致谢新增 36 段（新增条目）机制不同，两处的 36 与 7 不可相加。

**同数同集的三连**（第 15.3 节给出成员级证明）：

- `media*` 词族 102 段
- `media file` 短语 102 段
- `crafted media file` 短语 102 段

三者两两相交 **102**、三件套全交 **102**——**这是本系列第一次出现"三个判据得数相同且成员级完全相同"**。文本含义是：刀一里凡出现 `media` 必然是 `media file`，凡出现 `media file` 必然带 `crafted`。**"媒体文件"在公开措辞里不是一个中性名词，而是一个固定的缺陷载体短语。**

## 2.3 刀一为什么只有 4 段写在 Description

刀一 219 段里，Impact 侧 215、Description 侧 **4**，且 **I∩D ＝ 0**。那 4 段是同一个句子在四个页面上：

> `A Content Security Policy bypass was addressed with improved enforcement in AudioWorklet contexts.`（4 句次、4 页、组件 WebKit；页 `127110／127111／127115／127121`）

`AudioWorklet` 之所以命中刀一，是因为词表写的是 `audio\w*`：`Audio` 是一个 token 的开头，`audio\w*` 从 token 头开始匹配并贪婪吞掉整个 `AudioWorklet`，前置与尾断言因此都自然成立。

这是一次**"`\w*` 贪婪量词在 token 开头放行"**的典型：`audio\w*` 从 token 首字母起匹配并吞掉整个 `AudioWorklet`，前置断言（左边是空格）与尾断言（右边是 token 尾）都成立。它与第 5.2 节 `information` 里的 `format`、`displaying` 里的 `playing` 恰好是**同一根断言的两个相反结果**——词干嵌在 token 内部时被挡（434 段），词干位于 token 开头时被放（这 4 段）。这 4 段的真实语义是**内容安全策略绕过**（组件 WebKit、后果是 policy bypass），与音频媒体毫无关系——**刀一的 Description 侧 4 段是全篇 1,541 段里最纯粹的假阳性**。若把它们摘除，刀一变成 **215 段、Impact 单侧 100%**，"三刀各自 I∩D ＝ 0"仍然成立，但第 6 章的"只有刀二横跨两侧"就升级为"三刀全部严格单侧"。

## 2.4 刀一 Impact 侧的句式构成

刀一 215 段的 Impact 判据句，前五个句子占 158 句次（附表 B）：

| Impact 句 | 句次 | 页 |
|---|---:|---:|
| `Processing a maliciously crafted media file may lead to unexpected app termination or corrupt process memory` | **73** | 53 |
| `Parsing a file may lead to an unexpected app termination` | **63** | 34 |
| `Processing a maliciously crafted video file may lead to unexpected app termination` | **32** | 30 |
| `Processing a maliciously crafted video file may lead to unexpected app termination or corrupt process memory` | **30** | 23 |
| `Parsing a maliciously crafted file may lead to an unexpected app termination` | **33** | 25 |

`Processing an audio stream in a maliciously crafted media file may terminate the process`（24 句次、24 页、3 名）是 `stream` 一族的唯一句式——**`audio stream` 全部 29 段都出自这两句**（另 5 句次是 `…may result in code execution.`）。

**后果尾句只有三种**：`…may lead to unexpected app termination`（或 `…or corrupt process memory`）、`…may terminate the process`、`…may lead to unexpected system termination`。**刀一的 Impact 侧没有任何一句写"任意代码执行"以外的更弱后果**，而写 ACE 的只有 5 句次（`Processing an audio stream … may result in code execution.`，∩E 5，见第 9 章）。

# 3 刀二：解析 247 段，`parsing` 一个形态写完 229 段

## 3.1 刀二内部闭合

| 词族 | 三字段 | ∩主集 | 优先序独占 | Impact | Description |
|---|---:|---:|---:|---:|---:|
| `pars*` | 229 | **229** | **229** | 146 | 83 |
| `container(s)` | 15 | **15** | **15** | 3 | 12 |
| `format*` | 3 | **3** | **3** | 0 | 3 |
| `decod*／encod*` | **0** | 0 | 0 | 0 | 0 |
| `parse`（单词界） | **0** | 0 | 0 | 0 | 0 |
| **刀二并集** | — | **247** | **247** | **149** | **98** |

`pars*` 的 229 段**全部是动名词 `parsing`**——`parsing` 词族行的 ∩主集与 `pars*` 行同为 229，`parse` 单词界 **0 段**。也就是说：**公开措辞里"解析"这个动作永远以一个名词化的动名词出现（`Parsing a … file may…`／`a parsing issue`／`in the parsing of URLs`），从来不以动词（`the parser parses`／`parsed`）出现。** 编解码词族 `decod*／encod*` **0 段**——**编码器与解码器在苹果的安全措辞里不是一个被命名的责任对象**，媒体解码缺陷一律写作 `parsing` 或干脆写作 `processing`。

## 3.2 刀二的两种句位

刀二 247 段按字段分成两种完全不同的句式：

- **Impact 侧 149 段**：以 `Parsing`／`Processing` 起手的**影响句**，共 146 句次来自附表 C 的「Parsing a maliciously crafted … file…」桶（47）＋「…may lead to unexpected app termination」桶（64）＋其余。第 8.3 节的句首词统计给得更为直接：**Impact 判据句 373 句里 `parsing` 起手 146 句、`processing` 起手 178 句，两词合计 324 句＝86.9%。**
- **Description 侧 98 段**：以 `was addressed` 为核心的**修复句**，其中 73 句次是 `A parsing issue in the handling of directory paths was addressed with improved path validation.` 这一句（∩IMGX 73、∩RESX 0——见 15.5）。

**"解析"在影响句里是谓语动词的名词化，在修复句里是缺陷的分类名（`a parsing issue`）。同一个词族在两个字段承担两种语法角色，这是刀二 I∩D ＝ 0 的语言学原因。**

## 3.3 刀二与 #130 三刀相交为 0

刀二 247 段与 #130 的类型／生命周期／JIT 三刀并集（343 段）**相交 0 段**；而与 #129 的 parsing／memory／scripting 三族并集（2,213 段）**相交 229 段**——**这 229 段恰好就是 `pars*` 的全部**，即刀二里除 `container`（15）与 `format`（3）以外的每一段都在 #129 里出现过。

**这是一处真包含关系**：`|刀二| = 247`、`|PMS| = 2,213`、`|刀二∩PMS| = 229`、`229 = 247 − 18`，被排除的 18 段正是 `container` 与 `format` 两族。**#129 的"解析错误"与本报告的"媒体解析"是同一条叙述线的两端：#129 抓 `parsing` 与 `memory` 的搭配，本篇抓 `parsing` 与 `media` 的搭配。**

## 3.4 `container` 的 15 段全是假阳性

附表 A 的三行给出刀二里最有信息量的对照：

| 判据 | 段 | ∩主集 | 句子形态 |
|---|---:|---:|---|
| `container(s)` 词界 | 15 | 15 | — |
| `data container` 短语 | 12 | 12 | `The issue was addressed with improved restriction of data container access.` |
| `App Sandbox container` 短语 | 3 | 3 | `An app may be able to access protected files within an App Sandbox container` |

12 ＋ 3 ＝ 15，**闭合说明：`container` 一词在本篇的全部 15 段命中都被这两个沙盒短语吃掉了，媒体容器（MP4／MOV／CAF／M4A 之类的"容器格式"）一次都没有被写出来。**

这是本系列**第一个"整族假阳性"的完整样本**：一个词族的两字段命中 15 段、∩主集 15 段（无一遗漏）、但**语义相关命中 0 段**。它同时给出一个方法论结论——**词族级判据的"假阳性率"可以是 100%，而附表 A 的段级命中数对此毫无提示**；只有把该词族的所有出现短语都列出来（本篇用两条短语行做闭合）才能暴露。

## 3.5 `analysis` 词族：第一次"两字段命中为零"

把 `analysis／analyse／analyz*` 作为刀二候选词族测得：

| 项 | 值 |
|---|---:|
| 三字段并集（含致谢） | **94 段 / 55 页 / 8 名** |
| Impact 字段命中 | **0 段** |
| Description 字段命中 | **0 段** |
| 并入后两字段主集 | **1,541 段（一段不多）** |
| 并入后三字段并集 | **1,630 段（＋53）** |
| ∩主集 CU | 40 段 |
| 只在致谢、主集外 | **54 段** |

94 段的命中来源是**致谢字段里的机构名 `Google Threat Analysis Group`**（附表 A 单列一行：三字段 94、∩主集 40）。∩E 36 说明这 94 段里有 36 段与 #114 的在野集相交——**在野条目集中恰好密集出现这一家机构名**。

**"某词族两字段命中为 0"是本系列十三次测量里第一次出现**：它意味着这个词族在正文的两个判据字段里根本不被使用，只在致谢署名里被使用。这类词族一旦入刀，主集不会变大，但**"致谢污染"风险会从"边角"变成"主体"**——第 12.4 节的 36 段致谢污染就是这个机制的现行犯。

# 4 刀三：1,105 段里 `manag*` 一族扛 1,045 段

## 4.1 刀三内部闭合分区

| 词族 | 三字段并集段 | ∩主集（三字段口径） | 优先序独占（两字段） | Impact | Description |
|---|---:|---:|---:|---:|---:|
| `manag*` | 1,071 | **1,045** | **1,045** | 7 | 1,038 |
| `free／freed／freeing` | 170 | 170 | **0** | 0 | 170 |
| `cache*` | 29 | 29 | **29** | 0 | 29 |
| `uninitialized` | 16 | 16 | **16** | 8 | 8 |
| `retain*` | 5 | 5 | **5** | 5 | 0 |
| `resource(s)` | 5 | 5 | **5** | 0 | 5 |
| `exhaust*` | 5 | 5 | **0** | 0 | 5 |
| `allocat*` | 4 | 4 | **4** | 0 | 4 |
| `releas*` | 12 | 2 | **1** | 1 | 0 |
| `pool(s)`／`garbage`／`dangling`／`freed／freeing` | **0** | 0 | 0 | 0 | 0 |
| **刀三并集** | — | **1,105** | **1,105** | **21** | **1,084** |

优先序独占相加：1,045＋0＋29＋16＋5＋5＋0＋4＋1 ＝ **1,105**，闭合。两字段的 Impact 与 Description 相加 21＋1,084 ＝ 1,105 也闭合（因为该刀的 I∩D ＝ 0）。`releas*` 一行是唯一"三字段口径 ∩主集 2 段、两字段口径只 1 段"的词族——另 1 段的 `releas*` 只写在致谢（`…patches or releases are available`，见 12.4）。

四个必须点名的关系：

1. **`free` 的 170 段 100% 落在 `manag*` 之内**（`|free| = 170`、`|manag*| = 1,045`、`|free ∩ manag*| = 170`、`free − manag* = 0`）。170 段全是 `A use after free issue was addressed with improved memory management.` 或 `A use-after-free issue …` 两句——**`free` 在这里不是"资源释放"的独立描述，而是缺陷名 `use after free` 的一部分，而同一句的宾语是 `memory management`。**
2. **`exhaust*` 的 5 段 100% 等于 `resource(s)` 的 5 段**（三处真同集之一）：`resource exhaustion` 是唯一句式，`exhaust*` 从不单独出现。
3. **`releas*` 词族三字段 12 段，但只有 2 段进主集**，其中 1 段是"仅在 Impact 有命中"的双字段段（页 122722，`…iOS released before iOS 18.4.1…`）。**这 12 段里 11 段是致谢字段里的页面免责声明**（`…patches or releases are available`，见第 12.4 节）——11 段分布在域 B 的 11 页上、每页 1 段，其中 10 段在主集外，另 1 段（页 120303）因同段的 `manag*` 已在主集内。`releas*` 是致谢污染的入口词族。
4. **`pool`／`garbage`／`dangling`／`freed`／`freeing` 全 0**：**内存池、垃圾回收、悬挂指针这三个资源管理概念在公开措辞里不存在。** 第 4.4 节给句式证据。

## 4.2 `manag*` ＝ 两句话式

`manag*` 的 1,045 段（两字段）由**两个短语**写成，且二者**交 0**：

| 短语 | 段 | 页 | 名 | ∩主集 | ∩RESX | ∩R | ∩E | ∩WO |
|---|---:|---:|---:|---:|---:|---:|---:|---:|
| `state management` | **662** | 176 | 111 | 662 | **47（7.1%）** | 127 | 14 | 45 |
| `memory management` | **369** | 115 | 26 | 369 | **369（100%）** | 41 | 17 | 0 |
| 其余 `manag*` | **14** | — | — | 14 | — | — | — | — |
| 两短语相加 | **1,031** | — | — | 1,031 | 416 | 168 | 31 | 45 |
| `manag*` 全族（附表 A 真值） | **1,071** 三字段／**1,045** 两字段 | 185 | 130 | **1,045** | 未单列 | **181** | **31** | **49** |

662 ＋ 369 ＋ 14 ＝ 1,045 闭合；`improved state management`（662）与 `improved memory management`（369）两行与上表前两块**逐格相同**，说明**这两个短语的出现 100% 发生在 `improved X management` 这一套话框架内**。

14 段的"其余"是 `session management`、`cookie management`、`permission management`、`device management`、`cache management` 这类"X management issue"形——附表 B 给了其中三句的句次：`A cookie management issue was addressed with improved state management.`（17）、`A session management issue was addressed with improved checks.`（4）、`A malicious app may be able to modify other apps without having App Management permission`（4）。**注意 17 段 `cookie management` 的修复句宾语仍是 `improved state management`——"X management" 只是缺陷的分类名，真正的修复词永远是 `state management`。**

## 4.3 同一词族对 #133 的两种相反关系（本篇最重要的对照）

| 词族／短语 | 本篇主集段 | ∩RESX | 占比 |
|---|---:|---:|---:|
| `memory management` | 369 | **369** | **100%** |
| `state management` | 662 | **47** | **7.1%** |
| `memor*` 词族（本篇不取） | 1,967 全树 | 560 ∩CU | 28.4% |

**`memory management` 的每一段都在 #133 的资源边界主集里；`state management` 则 92.9% 不在。** 同一个 `manag*` 词族的两个子形态，对一个既有报告主集呈现"全包含"与"几乎不相交"两种相反关系。这给出一个可操作的判据：

> **`manag*` 是不是资源词，取决于它前面那个名词。`memory management` 是内存问题，`state management` 不是。**

而本篇主集里 `manag*` 的 1,045 段中，`state management` 占 662（**63.3%**）——**近三分之二的"资源管理"命中其实与内存无关，而是状态机问题**（授权状态、隐私状态、删除标记、锁定状态）。附表 B 的 `state management` 桶里，主语名列出的缺陷类型是：authorization（159）、inconsistent user interface（29）、logic（71）、authentication（57）、information disclosure（24）、memory corruption（23）、cookie management（17）、race condition（11）、lock screen（6）、permissions（6）、privacy（4）、type confusion（4）。**"资源管理类弱点"这个选题名，在文本上主要是一个"状态管理"弱点的集合。**

## 4.4 未被写出的三个概念

`pool`／`garbage`／`dangling` 三词的 0 段各自对应一类真实的资源管理缺陷，但公开措辞用别的说法覆盖它们：

| 概念 | 判据词 | 本篇段数 | 实际写法 |
|---|---|---:|---|
| 内存池／对象池 | `pool` | **0** | 归入 `memory management`／`state management` |
| 垃圾回收 | `garbage` | **0** | 归入 `object lifetimes`（`This issue was addressed by improved management of object lifetimes.` 3 句次） |
| 悬挂引用 | `dangling` | **0** | 归入 `use after free` 两形（316 段） |
| 重复释放 | `double free` | **43** | `A double free issue was addressed with improved memory management.`（唯一句式，43 句次、31 页、3 名） |
| 未初始化 | `uninitialized` | **16** | `An uninitialized memory issue was addressed with improved memory initialization.`（8）＋`An app with root privileges may be able to read uninitialized kernel memory`（8） |
| 引用计数 | `retain` | **5** | `Notifications marked for deletion could be unexpectedly retained on the device`（5 句次，唯一句式） |

**"释放两次"有独立词（double free，43 段），"没释放干净"没有独立词**——`retain` 的 5 段全在讲通知未删除，是设备端数据留存而不是引用计数。`improved initialization` 这个 #132／#133 都出现过的修复套话，在本篇**0 段**（附表 A 句级刀行、附表"机制名"行双重确认）。

## 4.5 与 #133 的主套话分工

| 套话 | 全树三字段 | ∩CU | ∩RESX | 关系 |
|---|---:|---:|---:|---|
| `improved memory management` | 369 | 369 | 369 | 本篇与 #133 **共用** |
| `improved memory handling` | 862 | **78** | 862 | #133 主套话，**本篇只蹭到 78 段，且 78 段全在 Impact 侧（Description 侧 0）** |
| `improved state management` | 662 | 662 | 47 | **本篇主套话，#133 基本没有** |

**`improved memory handling` 的 862 段里只有 78 段进本篇**（∩CU 78，且这 78 段的 Description 侧命中为 0，即它们进入主集靠的是 Impact 里的媒体／解析词）。#133 的主套话（862）与本篇的主套话（662）相交：`state management` 与 `memory handling` 两行在附表 A 的段数分别是 662 与 862、∩主集 662 与 78——**两篇报告在同一批页面上读的是两套不同的修复句式，这是选题不重复的文本证据。**

# 5 词界断言第三次发作：这次咬的是 `information` 里的 `format`

## 5.1 反事实测量

对同一条主刀 `$re3` 做两根断言的单独增删（两字段口径）：

| 判据写法 | 主集段数 | 与基线差 |
|---|---:|---:|
| `(?<![\w-])` ＋ `(?![\w-])`（基线） | **1,541** | — |
| 去掉前置 `(?<![\w-])`、保留尾断言 | **1,975** | **＋434** |
| 去掉尾断言 `(?![\w-])`、保留前置断言 | **1,541** | **＋0** |
| 两根都去掉 | **1,975** | ＋434 |

**本篇的判据风险 100% 集中在前置断言上，尾断言完全惰性。** 惰性的原因是词表里凡是能被"后缀"影响的词族都带 `\w*`（`format\w*`、`pars\w*`、`manag\w*`…），贪婪量词已经把 token 的剩余部分吞掉，尾断言因此总是紧接在 `\w*` 之后自然成立。

## 5.2 ＋434 段的词形构成

把 434 个新增段里所有匹配片段收集起来，只有 **5 种词形**：

| 词形 | 出现次数 | 来源 |
|---|---:|---|
| **`formation`** | **439** | `information` 内部的 `format` ＋ 贪婪 `\w*` |
| `mediate` | 8 | `immediate`／`immediately` 内部的 `media` |
| `free` | **4** | `use-after-free` 连字符后的 `free` |
| `playing` | 2 | `displaying` 内部的 `playing` |
| `mediately` | 1 | 同 `immediate` |

**结论一句：**本篇前置断言的作用几乎完全是**挡掉 `information` 这个词里的 `format`**。439 次命中来自一个在安全文本里出现频率极高、与"格式"毫无关系的名词。新增 434 段的组件名包括 Kernel、Safari、WebKit、Siri、Maps、Shortcuts、Passwords、curl、Heimdal、zlib……——**如果去掉断言，"媒体解析与资源管理"报告的主集会凭空吸收掉一半的泄露类条目，因为它们都写着 `information disclosure`。**

同时，那 **4 段 `free`** 正是 #133 第 5 章点名的 CFNetwork 连字符盲区段（页 `124148–124151`，Description 只写 `A use-after-free issue was addressed by removing the vulnerable code.`）。**同一根断言在同一篇报告里既贡献了 434 段的净收益，也造成了 4 段的净损失**——这是本系列第一次能把一根断言的收益与损失同时量化：

- **收益**：挡掉 430 段纯假阳性（`information`/`immediate`/`displaying` 内嵌词）。
- **损失**：挡掉 4 段真条目（连字符形 `use-after-free`）。
- **净收益比 ＝ 430∶4 ＝ 107∶1。**

## 5.3 与前两篇的方向差异

| 报告 | 盲区形态 | 被挡段数 | 断言在本篇的角色 |
|---|---|---:|---|
| #132（图像格式与长度检查） | `out-of-bounds` 连字符形 | **193**（其中 4 段整条落在主集外） | **债主** |
| #133（资源边界） | `out-of-bounds` 三字段 816 段 ∩主集 654 | **162** 段在外；去断言主集 2,791→2,957（＋166） | **债主偏多** |
| **#134（媒体解析与资源管理）** | `information` 内嵌 `format` | **434 段被挡，但挡掉的是假阳性** | **功臣偏多** |

**方向反转的原因在词表本身**：#132／#133 的词表以 `bounds`、`size`、`length` 这类"独立名词"为主，断言挡的是连字符复合词；本篇词表以 `format`、`media` 这类"可作前缀的短词干"为主，断言挡的是复合词内嵌。**同一条 `(?<![\w-])` 在不同词表上的净效应可以变号——因此"词界断言是否有利"必须由每篇自己的反事实测量决定，不能从上一篇外推。**

## 5.4 连字符两形的分开计量（延续 #133）

| 形态 | 三字段 | ∩主集 | 两字段交 |
|---|---:|---:|---:|
| `use-after-free`（连字符形） | 193 | **189** | 0 |
| `use after free`（空格形） | 127 | **127** | 0 |
| 两形并集 | — | **316** | — |

189 ＋ 127 ＝ 316，两形**交 0**：**同一个条目要么写连字符、要么写空格，从不两形混用。** 空格形的 127 段**全部在主集内**，连字符形有 4 段在主集外（正是 5.2 的 CFNetwork 段）。这与 #133 的 127∶189∶316 逐格相同——**同一个两形分裂现象在两个选题里给出同一组数字，因为它是同一批 CoreMedia／CoreAudio 条目造成的。**

# 6 字段刀：三刀各自 I∩D ＝ 0，并集却有 17 段

## 6.1 单侧性测量

| 判据 | 段 | Impact | Description | **I∩D** |
|---|---:|---:|---:|---:|
| 刀一 K1 | 219 | **215** | **4** | **0** |
| 刀二 K2 | 247 | 149 | 98 | **0** |
| 刀三 K3 | 1,105 | 21 | **1,084** | **0** |
| 三刀并集 CU | 1,541 | 372 | 1,186 | **17** |

**这是本篇最漂亮的一个结构事实**：任何一支刀单独看，**没有任何一段同时在该刀的 Impact 和 Description 里命中该刀的词**；但三刀合起来有 17 段两侧都命中。

原因是**跨刀配对**。17 段逐一列出（成员级核验，非统计推断）：

**一I·三D 组（9 段）**

| 组件 | Impact（含刀一） | Description（含刀三） |
|---|---|---|
| CoreMedia × 8 | `Processing a maliciously crafted media file may lead to unexpected app termination or corrupt process memory` | `A use after free issue was addressed with improved memory management.` |
| FaceTime × 1 | `Muting the microphone during a FaceTime call may not result in audio being silenced` | `This issue was addressed through improved state management.` |

**二I·三D 组（8 段）**：全部是 CoreMedia，Impact ＝ `Parsing a file may lead to an unexpected app termination`，Description ＝ `A use-after-free issue was addressed with improved memory management.`

**没有任何一段是"一I·二D"或"三I·一D"或涉及刀一在 Description 的配对。** 文本解释：**影响句负责命名"输入"（媒体文件、解析动作），修复句负责命名"处理机制"（内存管理、状态管理）——两个字段被句式分工绑定，跨刀共段只能出现在"Impact 讲输入、Description 讲机制"这一个方向上。**

## 6.2 词族级落位：十六个词族的 I∩D 全为 0

| 词族 | Impact | Description | I∩D |
|---|---:|---:|---:|
| `media*` | 102 | 0 | 0 |
| `video*` | 83 | 0 | 0 |
| `audio*` | 59 | 4 | 0 |
| `stream(s)` | 29 | 0 | 0 |
| `pars*` | 146 | 83 | 0 |
| `container(s)` | 3 | 12 | 0 |
| `format*` | 0 | 3 | 0 |
| `manag*` | 7 | 1,038 | 0 |
| `free` | 0 | 170 | 0 |
| `cache*` | 0 | 29 | 0 |
| `uninitialized` | 8 | 8 | 0 |
| `allocat*` | 0 | 4 | 0 |
| `resource(s)` | 0 | 5 | 0 |
| `exhaust*` | 0 | 5 | 0 |
| `retain*` | 5 | 0 | 0 |
| `releas*` | 1 | 0 | 0 |

**没有一个词族是两栖的。** 这与 #133（`memor*` 是唯一两栖词族，仅I 592∶仅D 831∶两侧 544）形成明确对照——**#133 的 I∩D 802 段几乎全部由 `memor*` 一族贡献；本篇没有这样一个词族，所以本篇的 I∩D 只剩 17 段，且这 17 段是跨刀而不是同族。**

`uninitialized` 是唯一一行"Impact 8 ∶ Description 8"看似两栖的词族，但 **I∩D ＝ 0**：8 段在 Impact 侧写 `may be able to read uninitialized kernel memory`（影响句），另 8 段在 Description 侧写 `improved memory initialization`（修复句）——**同一词干、两种句法角色、两批互不相交的条目。**

## 6.3 字段刀的推论

按 #133 第 7 章提出的判据——"一篇报告能不能做字段刀，只需数它有几个两栖词族"——本篇的结论是：**零个两栖词族的报告，其字段刀必须在刀级而不是词族级上呈现**。本篇把这条判据推到了极限：**两栖词族数为 0，字段刀的表达形式就自动从"同族两侧分工"（#133）变成"跨刀配对"（本篇）。**

同时，字段落位的方向与选题直觉一致且**三刀三种方向**：刀一偏 Impact（215∶4 ＝ 98.2% 在 Impact）、刀三偏 Description（1,084∶21 ＝ 98.1% 在 Description）、刀二两侧都有（149∶98）。**只有"解析"这个词族横跨两个字段，因为它是唯一既能当缺陷名又能当动作名的词。**

# 7 句层：59 个句子写完 1,559 句次

## 7.1 句库规模

| 指标 | 值 | 与 #133 对照 | 与 #132 对照 |
|---|---:|---:|---:|
| 主集段数 | 1,541 | 2,791 | 1,547 |
| 句次合计 | 1,559 | 3,593 | 1,551 |
| distinct 整句 | **59** | 133 | 146 |
| 句次／distinct（模板负载） | **26.4** | 27.0 | 10.6 |
| top1 占比 | **15.20%** | 17.9% | 45.2% |
| top3 占比 | 37.52% | 30.4% | — |
| top5 占比 | **50.35%** | 39.0% | — |
| top10 占比 | **70.04%** | 54.3% | 65.7% |
| top20 占比 | 87.24% | — | — |
| 句次 ＝ 1 的行 | **8** | — | — |

**59 个句子是本系列最少的一次**（#133 是 133、#132 是 146、#131 更多）。本篇的模板化程度介于 #132（一句 45.2%）与 #133（17.9%）之间，但**句库规模最小**：**句子更少、但每句更长，所以头部集中度反而低于 #132。**

## 7.2 句库的两个不相交子库

- **只在 Impact 出现的句子：27 个**（合计句次 373）
- **只在 Description 出现的句子：32 个**（合计句次 1,186）
- **两侧共用的句子：0 个**

27 ＋ 32 ＝ 59，闭合。**公开措辞里没有一个句子既出现在影响句字段又出现在修复句字段**——这与 #133（99∶34∶0）与 #126（两侧共用 0）同构，是本系列的稳定性质；但本篇的比例最悬殊：**Impact 侧 27 句平均 13.8 句次，Description 侧 32 句平均 37.1 句次。**修复句库更小、负载更重。

## 7.3 前十句（附表 B 摘录）

| distinct 整句 | 桶 | 句次 | I∶D | 页 | ∩R | ∩E |
|---|---|---:|---:|---:|---:|---:|
| `This issue was addressed through improved state management.` | state mgmt | **237** | 0∶237 | 127 | 47 | 0 |
| `A use-after-free issue was addressed with improved memory management.` | memory mgmt | **189** | 0∶189 | 98 | 13 | 9 |
| `An authorization issue was addressed with improved state management.` | state mgmt | **159** | 0∶159 | 61 | 13 | 4 |
| `A use after free issue was addressed with improved memory management.` | memory mgmt | **127** | 0∶127 | 49 | 20 | 8 |
| `A parsing issue in the handling of directory paths was addressed with improved path validation.` | 其余 | **73** | 0∶73 | 45 | 0 | 0 |
| `Processing a maliciously crafted media file may lead to unexpected app termination or corrupt process memory` | media file | **73** | 73∶0 | 53 | 0 | 0 |
| `A logic issue was addressed with improved state management.` | state mgmt | **71** | 0∶71 | 49 | 12 | 0 |
| `Parsing a file may lead to an unexpected app termination` | app termination | **63** | 63∶0 | 34 | 0 | 0 |
| `An authentication issue was addressed with improved state management.` | state mgmt | **57** | 0∶57 | 44 | 24 | 0 |
| `A double free issue was addressed with improved memory management.` | memory mgmt | **43** | 0∶43 | 31 | 8 | 0 |

**四个观察：**

1. **`through` 与 `with` 是两套句子的分水岭。** `This issue was addressed **through** improved state management.` 独占 237 句次，而 `An authorization issue was addressed **with** improved state management.` 独占 159 句次——**两句同义、从不互换**：`This issue…` 配 `through`、`A／An X issue…` 配 `with`。附表 D 的"字段落位"行与附表 C 续表的打头形态（The／This 打头 291、A／An X issue 打头 487）分别对应这两套。
2. **top2 与 top4 是 `use-after-free` 与 `use after free` 两形**（189 与 127），二者在句库中是**两个不同条目**，交 0（第 5.4 节）。若合并计 316 句次，top1 会变成它（20.3%）——**报告是否合并连字符变体，直接改变"头部套话"的归属结论。**
3. **top5 的 `A parsing issue in the handling of directory paths…`（73 句次）与 #132 的重合是 73／73、与 #133 的重合是 0**（∩IMGX 73、∩RESX 0）——**同一条套话在"图像格式"篇里是主句，在"资源边界"篇里一段都不沾。**
4. **`state management` 桶的六个句子（237＋159＋71＋57＋29＋24＋…）里，`An inconsistent user interface issue`（29）与 `An information disclosure issue`（24）都在讲界面与泄露，没有一句讲资源**——**"状态管理"是一个语义空的修复词。**

## 7.4 归桶与句式形态

附表 C 的 **15 个句形桶**按优先序把 1,559 句次分完，闭合成立；三个最大桶是 state management（**662**）、memory management（**369**）、其余含三刀词句（**147**）。662 ＋ 369 ＝ 1,031 ＝ **66.1% 的句次落在两把套话桶里**。

续表的八类句式形态（按优先序闭合 1,559）：

| 形态 | 句次 | 段 | 页 | 名 |
|---|---:|---:|---:|---:|
| `A／An X issue was addressed …` 打头 | **487** | 487 | 148 | 82 |
| `A／An X（非 issue 名词）was addressed …` 打头 | **398** | 398 | 123 | 60 |
| 含 `may`（影响句情态） | **359** | 358 | 124 | 33 |
| `The／This issue was addressed …` 打头 | 291 | 291 | 143 | 52 |
| `X existed（缺陷存在句）` | 12 | 12 | 12 | 2 |
| `Multiple X issues were addressed …` 打头 | 4 | 4 | 4 | 1 |
| 其余句式 | 5 | 5 | 5 | 1 |
| 含 `was addressed with` | 3 | 3 | 3 | 1 |
| **合计** | **1,559** | — | — | — |

487 ＋ 398 ＋ 359 ＋ 291 ＋ 12 ＋ 4 ＋ 5 ＋ 3 ＝ 1,559。**注意"含 may"那一行是段 358、句次 359**——只有一段的两句里各含一个 `may`，这是本篇"含 may"计数的唯一重复段。

**修复句打头的三种形态（487＋398＋291 ＝ 1,176）占句次的 75.4%**，与第 7.2 节 Description 侧 1,186 句次只差 10 句——**这 10 句是 Description 字段里不以 `was addressed` 打头的判据句**（如 `An issue existed in the parsing of URLs.` 3 句次、`This was addressed with improved state management.` 3 句次等残句）。

## 7.5 句号异形：本篇只有 1 组

对 59 个 distinct 句子按"去掉末尾句号的 stem"归并：

- **可合并组 ＝ 1**：stem `An authorization issue was addressed with improved state management` —— 带句号 **159** 次、不带句号 **1** 次（那 1 次落在 **Impact** 字段，附表 B 倒数第五行）。
- 其余 58 个句子**异形 0 组**：每个 stem 只有一种标点形态。

这与 #132（异形 0 组）与 #133（1 组，10∶105）规模相同——**标点异形不是普遍现象，而是个别页录入时的疏忽**（那 1 次"不带句号"出现在 Impact 字段，本身也是一次字段错位的证据）。

正文实体统计：`&rsquo;` **8 处**（全部在 `user&rsquo;s`／`app&rsquo;s` 这类所有格）、`&amp;` **0 处**、短横长横混用 **0 处**、不间断空格 **0 处**。**本篇不存在需要 HTML 实体归一化才能配对的句子**（与 #131 相反）。

# 8 情态刀：373 句影响句只有三种状态

第 7 章把句层的**长度与内容**量完了，本章量句层的**语气**。判据是这样：在主集 1,541 段的 **Impact 字段**里按句切出命中三刀词的 **373 句**，逐句检查句子是否含情态动词，含哪一个。情态词表取七个：`may`、`could`、`can`、`will`、`would`、`might`、`must`、`should`（八个词形，其中 `may／could` 是苹果影响句的标准形）。

## 8.1 三分闭合：359 ＋ 5 ＋ 9 ＝ 373

| 情态状态 | 句次 | 占 373 句 | 段 |
|---|---:|---:|---:|
| 含 `may` | **359** | 96.2% | 358 |
| 含 `could` | **5** | 1.3% | 5 |
| 无情态动词 | **9** | 2.4% | 9 |
| 含 `can／will／would／might／must／should` | **0** | 0.0% | 0 |
| **合计** | **373** | 100.0% | — |

359 ＋ 5 ＋ 9 ＋ 0 ＝ 373，闭合成立。这张表里最值得记的是第四行的 **0**：**六个情态词在本篇全部 373 句影响句里一次都没有出现**。苹果的影响句语气不是"从一组情态动词里挑"，而是"从两个里挑，而且几乎总挑第一个"。`may` 与 `could` 的比例是 **359 ∶ 5 ＝ 71.8 ∶ 1**。

**"含 may 句次 359、段 358"这两根轴本篇只在同一处出现一次分歧**：全树里恰有一段的 Impact 字段被切出两句、两句各含一个 `may`。附表 C 续表里那一行的"句次 359／段 358"就是这一处；本篇其余 62 个桶行都是句次＝段的对齐形态。

## 8.2 无情态的 9 句是什么

那 9 句不带 `may` 也不带 `could`，它们的存在说明**影响句并非句法上强制带情态**——2.4% 的例外率很低，但非零。**量级上不影响"影响句＝may 句"这条判据的使用**：9 句只占主集 1,541 段的 0.6%。

## 8.3 Description 侧：1,186 句，情态 0

同一把刀落在 Description 字段：**主集 Description 侧命刀句 1,186 句，其中含任一到齐情态动词的 ＝ 0 句**。

这不是疏忽，而是两个字段的分工：**修复句是过去时陈述句（`was addressed`／`existed`），它只报告"已经做了什么"，不做任何可能性承诺；可能性全部由 Impact 句单独承担**。所以本篇的"语气轴"其实是一根**字段轴**：把 1,541 段按字段拆成 373 句（可能）＋ 1,186 句（既成），两边各说各的时态，互不越界。

这条分工有一个可核对的推论：**主集内 Impact 侧段 ＝ 372、仅 Description 侧段 ＝ 1,169、两侧都有 ＝ 17**，三档相加 372＋1,169＋17＝1,558，减去 17 段被前两档各数一次＝**1,541 闭合**。也就是说 1,541 段里有 **75.9%（1,169 段）只有陈述句**、**24.1%（372 段）带有至少一句可能性表述**。

## 8.4 句首形态：89.3% 是动名词开头

按 373 句的第一实词统计：

| 句首词 | 句次 |
|---|---:|
| `processing` | **178** |
| `parsing` | **146** |
| `a` | 19 |
| `an` | 13 |
| `playing` | 8 |
| `notifications` | 5 |
| `muting` | 1 |
| `apple` | 1 |
| `on` | 1 |
| `audio` | 1 |
| **合计** | **373** |

四个动名词 `processing／parsing／playing／muting` 相加 ＝ **333 句 ＝ 89.3%**。换句话说：**本篇十句影响句里有九句用"某件事正在被做"的动名词作主语**（`Processing … may allow an attacker to …`、`Parsing … may …`），只有 32 句（冠词 `a／an` 打头，**8.6%**）走"某个问题存在"的名词主语路线。

这与第 3.2 节的刀二两句位（`parsing` 146 ＋ `processing` 178 ＝ 324 ＝ 86.9%）是**同一批句子**——两句位统计用的是刀二词落在句中的位置，句首统计用的是句子的第一个词，两根轴量的是同一个语言事实：**苹果在写影响句时，习惯把"处理／解析"这件事本身当作主语**。

## 8.5 情态刀为什么不进主集判据

本篇主集**不使用**情态词作为取集条件。理由全在本章的数据里：其一，`may` 已经覆盖主集影响句的 **96.2%**，把它当刀几乎不产生筛选——取到的就是"全部带影响句的段"；其二，把它当限定条件（只取带情态词的段）会砍掉 Description 侧**全部 1,186 句**，因为那 1,186 句一句情态都没有。**无论当刀还是当筛，主集都会变形。**所以情态词在本篇只做**描述性度量**，即本章；这也与第 1 章"三刀全部取自内容词、不取自功能词"的口径声明一致。

# 9 后果强度八类：834 与 783 之间差 51

本章用**同一套八类判据**（与 #122–#133 完全一致的后果词形）量主集。这套判据是：任意代码执行（ACE）、绕过（BYP）、泄露（DIS）、拒绝服务／异常终止（DOS）、完全控制（FULL）、内存损坏（MEM）、提权（PRI）、沙盒逃逸（SBX）。**每类词形固定，本篇不新增、不改写、不放宽。**

## 9.1 两套口径，八个数字

| 类别 | 三字段口径（任一模态字段命中） | Impact 字段口径 | 差 |
|---|---:|---:|---:|
| ACE 任意代码执行 | **37** | **37** | 0 |
| BYP 绕过 | **57** | **57** | 0 |
| DIS 泄露 | **106** | **80** | 26 |
| DOS 拒绝服务／异常终止 | **498** | **498** | 0 |
| FULL 完全控制 | **7** | **7** | 0 |
| MEM 内存损坏 | **69** | **23** | 46 |
| PRI 提权 | **17** | **17** | 0 |
| SBX 沙盒逃逸 | **43** | **43** | 0 |
| **相加** | **834** | **762** | 72 |
| **并集（去重段）** | **783** | **742** | 41 |
| **多标签重复** | **51** | **20** | — |

两列**只在 DIS 与 MEM 上不同**：其余六类在三字段口径下的命中全部落在 Impact 字段里。`disclos*／leak*／expos*` 有 26 段、`memory corruption／corrupt memory` 有 46 段是被 **Description 或致谢字段**抓进来的。也就是说：**后果词的"后果"本身只写在 Impact 字段；它们出现在别的字段时，是在转述或致谢，不是在声明后果**。

`相加 － 并集` 是多标签重复量：三字段口径 **834 － 783 ＝ 51**，Impact 口径 **762 － 742 ＝ 20**。

## 9.2 类数分布：799 段一个后果词都不带

以 Impact 口径给主集 1,541 段逐段数它命中了几类：

| Impact 口径类数 | 段 |
|---|---:|
| 0 类 | **799** |
| 1 类 | **727** |
| 2 类 | **10** |
| 3 类 | **5** |
| ≥4 类 | **0** |
| **合计** | **1,541** |

799 ＋ 727 ＋ 10 ＋ 5 ＝ 1,541 闭合；后三行相加 727 ＋ 10 ＋ 5 ＝ **742 ＝ 上面的并集**；加权 727×1 ＋ 10×2 ＋ 5×3 ＝ 727 ＋ 20 ＋ 15 ＝ **762 ＝ 上面的相加**。三处交叉验证互相咬合，且**"≥4 类 ＝ 0"意味着最高的那档恰好是 3 类**。

**主集里 51.8% 的段（799 段）在 Impact 字段里不带任何后果词**。这个数就是"苹果在多批条目里只写机制、不写后果"的度量。

## 9.3 多标签只有三种组合

15 段（≥2 类）的具体组合全清单：

| 组合 | 段 |
|---|---:|
| ACE ＋ DOS | **2** |
| ACE ＋ SBX | **8** |
| ACE ＋ PRI ＋ SBX | **5** |
| **合计** | **15** |

15 ＝ 上面 ≥2 类（10 ＋ 5）＝ 与全树"≥2 类段"集合（185 段）在主集内的重合数（三者同为 15）。**三种组合全部含 ACE**——本篇的多后果段没有一段是"泄露 ＋ 拒绝服务"这种中性搭配，全部是"能执行代码"再叠一层。**而且 DOS 只以"孤立形态"出现：498 段 DOS 里只有 2 段与其他类同段，其余 496 段是单类段**。

## 9.4 把两类段分开看：多标签几乎全在 Description 侧

主集 Impact 侧 372 段（即 Impact 字段本身有刀词的段）按 Impact 口径类数重切：

| Impact 口径类数 | Impact 侧段 |
|---|---:|
| 0 类 | **49** |
| 1 类 | **322** |
| 2 类 | **1** |
| **合计** | **372** |

对照 9.2：**主集整体有 15 段 ≥2 类，但其中只有 1 段属于 Impact 侧的 372 段**。**其余 14 段的多后果标签是在 Description 侧被读出来的**——它们的 Impact 字段里没有三刀词，所以压根不在这 372 段里。这给出一条很实用的判据：**"一段带几类后果"与"一段被哪把字段刀抓到"是两根独立的轴**；用 9.2 的 799／727／10／5 去推 9.4 会得到完全错的数。

## 9.5 DOS 一类独大，FULL 一类垫底

八类按 Impact 口径排序：DOS **498** ≫ DIS **80** > BYP **57** > SBX **43** > ACE **37** > MEM **23** > PRI **17** > FULL **7**。

- DOS 一类占主集 **498／1,541 ＝ 32.3%**——本篇三分之一的段在 Impact 字段里写了"异常终止／退出／拒绝服务"。这与"媒体处理与资源管理"这个选题高度自洽：**处理畸形媒体数据的失败模式，最常被写成"App／系统意外终止"**。
- FULL 只有 **7 段**（0.5%）：`arbitrary files?／any action／full control` 这三个短语在本篇语料里几乎是稀有词。
- MEM（内存损坏）**69／23**：三字段口径是 Impact 口径的 3 倍——**"memory corruption" 在本篇大量出现在 Description 侧的"An issue in … memory corruption …"这类转述句里，而不是后果句里**。

## 9.6 为什么"相加"不能当段数用

834 与 783、762 与 742 两组差（51 与 20）说明：**八类是标签，不是分区**。同一段可以既是 ACE 又是 SBX。**任何一处引用八类之和当作"主集内被后果词覆盖的段数"都会虚报 51 段（三字段口径）或 20 段（Impact 口径）**。本篇正文凡引用八类，均同时给出相加、并集、差三值；附表 E 与本章表格保持同一口径。

# 10 套话刀：同一句式两个短语，走向完全相反

"套话"在本篇指**修复句里那个改进对象短语**。第 7.4 节的 15 桶里，两个最大桶是 `state management`（**662** 句次）与 `memory management`（**369** 句次），两桶相加 **1,031 ＝ 全部 1,559 句次的 66.1%**。本章量这三件事：这两个短语的互斥性、它们与 `improved` 的绑定关系、以及它们与 #133 主集的关系。

## 10.1 manag* 的内部分解闭合到 1,045

| 短语 | 主集内段 | ∩RESX（#133 主集） |
|---|---:|---:|
| `state management` | **662** | **47**（7.1%） |
| `memory management` | **369** | **369**（**100.0%**） |
| `manag*` 其余形态 | **14** | **2** |
| **`manag*` 合计** | **1,045** | **418** |

662 ＋ 369 ＋ 14 ＝ **1,045** ＝ `manag*` 在两字段口径下与主集的重合，闭合成立；∩RESX 一列同样闭合：47 ＋ 369 ＋ 2 ＝ **418**。且 **`state management` ∩ `memory management` ＝ 0 段**——两个短语在同一段里从不共现，所以这三行是**真正的分区**，不是重叠标签。

**注意 418 与刀三整体的 ∩RESX 473 相差 55 段**：那 55 段是刀三里的**非 `manag*` 词族**（`cache*／free／exhaust*／resource(s)／uninitialized` 等）带进来的。**套话词只解释了刀三与 #133 重合的 88.4%（418／473），其余 11.6% 属于资源动词本身**。

## 10.2 `improved` 是强制修饰语

`improved state management` 在全树三字段命中 **662 段**，全部 662 段都在主集内；`improved memory management` 命中 **369 段**，同样全部在主集内。**两个短语在主集内的段数与它们带 `improved` 前缀的段数一模一样**——换言之：**本篇写 `state management` 的 662 段，每一段用的都是 `improved state management` 这个完整套话；写 `memory management` 的 369 段，每一段用的都是 `improved memory management`。**

这条判据可以直接用来做机器识别：**在主集里，"短语出现"与"套话出现"是同一件事**（区别于刀三的其他词族，`manag*` 出现在 Impact 字段侧的 7 段是内容用法，不是套话）。

## 10.3 两个套话与 #133 的关系是反的

这是本篇最有信息量的一组对照：

- `memory management` 的 **369 段全部落在 #133 主集内（369／369 ＝ 100%）**——#133 那把刀里有 `memor*`，任何写了"内存管理"的段必然被它抓到。
- `state management` 的 662 段里只有 **47 段**落在 #133 主集内（**7.1%**）。

所以**同一个句式框架下的两个高频短语，一个与 #133 完全重合、一个几乎完全不重合**。任何"套话 A 与套话 B 都是管理，所以它们的集合关系也一样"的推断都是错的。**跨报告引用时必须按短语分别取交集**（这也再次落在"得数相同不等于同一组段落"那条通用教训上：这里连得数都不同，更不可能同集）。

## 10.4 `memory handling`：本篇不取，但 78 段确实存在

`memory handling` 是 #133 的主套话。本篇三字段口径下它命中 **862 段**，其中**与主集重合 78 段**，而 **78 段全部落在 Impact 侧、Description 侧重合为 0**。

这个分布很能说明问题：**`memory handling` 在本篇语料里主要是"被改进的对象"（Description 侧的 `improved memory handling`），而 #133 把它整体取走了；本篇主集里剩下的 78 段是它作为"机制描述"出现在影响句里的那部分**。两篇对同一短语的取用侧不同，因此两篇主集重合 **683 段**（见第 15 章）而不是一方包含另一方。

## 10.5 单词界：management 1,038 段，manager 0 段

`management`（前后紧界）在 Description 字段命中主集 **1,038 段**；**`manager` 单词界在主集任何字段命中 0 段**。也就是：**本篇的 `manag*` 词族完全由"management／manage／managed／managing"构成，一个"manager"都没有**——而致谢字段里恰恰有大量 `Manager of Cyber Security` 这种**人名职务**（见第 12.4 节，那 26 段就是因为 `manag*` 撞上职务名而进入致谢侧）。**同一词根在正文与致谢里的两种用法，靠字段刀完全分开，这在本篇是唯一的零污染词根。**

## 10.6 其余资源管理词：三个小而闭合的事实

- **`free`（free／freed／freeing，紧界）主集内 170 段，170 段全部同时含 `manag*`** → `improved memory management` 与 `free` 是同一批段的不同侧面，`free` 不构成独立增量。
- **`resource(s)` 5 段 ＝ `exhaust*` 5 段，两集合成员完全相同** → 本篇"资源耗尽"只有 `resource exhaustion` 一种写法，没有第二种表达。
- **`uninitialized` 是唯一双侧词族：Impact 8 段／Description 8 段**；`cache*` 29 段、`retain*` 5 段、`releas*` 在正文两字段口径下只有 **Impact 1 段／Description 0 段**（详见第 12.4 节对 `releas*` 的追踪）。
- **`pool(s)`／`garbage`／`dangling` 三个概念在本篇语料里由刀三词形覆盖为 0 段**（第 4.4 节）——苹果从不写"内存池""垃圾回收""悬挂指针"这三个概念的名词形态。

# 11 页面分布：208 页、九档闭合、Top10 全是发布页

## 11.1 九档

主集 1,541 段分布在 **208 页**上（全树 257 页，占 **80.9%**）。按"该页属于主集的段数"分九档，并同时给出该档的段数合计：

| 该页主集段数 | 页数 | 该档段数合计 | 占主集 |
|---|---:|---:|---:|
| 1 段 | **46** | 46 | 3.0% |
| 2 段 | **15** | 30 | 1.9% |
| 3 段 | **19** | 57 | 3.7% |
| 4 段 | **18** | 72 | 4.7% |
| 5 段 | **9** | 45 | 2.9% |
| 6–9 段 | **47** | **352** | 22.8% |
| 10–19 段 | **34** | **410** | 26.6% |
| 20–49 段 | **19** | **479** | 31.1% |
| 50＋ 段 | **1** | **50** | 3.2% |
| **合计** | **208** | **1,541** | 100.0% |

页数列相加 46＋15＋19＋18＋9＋47＋34＋19＋1 ＝ **208**、段数列相加 46＋30＋57＋72＋45＋352＋410＋479＋50 ＝ **1,541**，两列同时闭合。**≤5 段的页有 107 页（51.4%），却只贡献 250 段（16.2%）**；**≥20 段的 20 页（9.6%）贡献 529 段（34.3%）**——本篇的分布形态是"**一半以上的页极薄，三分之一以上的段极厚**"。

## 11.2 Top10：全部是「About the security content of …」发布页

| 页面 | 段 | 页标题 |
|---|---:|---|
| 149035 | **50** | About the security content of macOS Golden Gate 27 |
| 149034 | **36** | About the security content of iOS 27 and iPadOS 27 |
| 128067 | **33** | About the security content of macOS Tahoe 26.6 |
| 149043 | **33** | About the security content of macOS Sequoia 15.8 |
| 149038 | **32** | About the security content of visionOS 27 |
| 149042 | **31** | About the security content of macOS Tahoe 26.7 |
| 122373 | **26** | About the security content of macOS Sequoia 15.4 |
| 149037 | **26** | About the security content of watchOS 27 |
| 149036 | **25** | About the security content of tvOS 27 |
| 128068 | **24** | About the security content of watchOS 26.6 |

Top10 合计 **316 段 ＝ 主集的 20.5%**。**十页全部是同一模板的操作系统发布页**——不是某个组件的专页。这决定了本篇的一条使用规则：**按页取样会严重偏向发布页**；任何"页均"指标（本篇页均 7.41 段）都必须同时报中位形态（1–5 段）才知道分布有多偏。

（并列按页号升序排列：33 段的两页、26 段的两页各自相邻。）

## 11.3 全本篇页 23 页与第三套分母

208 页里有 **23 页的全部段落都被本篇抓进主集**（该页段数＝该页主集段数）。这 23 页是"媒体／解析／资源管理"话题浓度最高的页面——它们不是发布页，而是**以多媒体或资源管理为唯一主题的组件页**。

**第三套分母**（第 1.2 节定义：主集所在 208 页上的全树段数）＝ **8,981 段**，主集占 **1,541／8,981 ＝ 17.16%**，每页均 7.41 段。三套分母下的本篇占比：全树 **16.87%**（1,541／9,136）、Impact 全集 **20.73%**（1,541／7,434）、同页 **17.16%**（1,541／8,981）——**三个数相差不到 4 个百分点，说明本篇的高占比不是页面选择造成的假象**。

## 11.4 版式域 B 只有 3 段

域 B（11 页粗体版式，全树 91 段）里，主集只抓到 **3 段 ＝ 域 B 的 3.30%**；域 A 抓到 **1,538 段 ＝ 域 A 9,045 段的 17.00%**。**比值 3.30 ∶ 17.00 ＝ 1 ∶ 5.2**。

三段分别是：120303 Bluetooth 的 `session management`、120304 Notes 的 `state management`、120307 LoginWindow 的 `state management`——**三段全部经 `manag*` 进入，没有一段经媒体词或解析词进入**。这与第 3 章刀二在域 B 命中 0 段一致：**粗体版式页写的是"会话／状态"这类管理词，不写解析动作**。

# 12 编号、致谢与切刀污染

## 12.1 编号：418 个 distinct，416 个只属于本篇

主集内出现的 CVE 编号（统一大写、跨三字段去重）distinct ＝ **418**，其中 **416 个只在本篇主集内出现（独占）**、**2 个与主集外段落共享**。共享的两个是 **CVE-2025-43529** 与 **CVE-2025-14174**——它们各有一条主集内条目和一条主集外条目（同一编号被拆到相邻组件）。

**416／418 ＝ 99.5% 独占率**，意味着本篇的选题边界在编号维度上几乎完全干净：**用编号做样本抽取，与用措辞刀做抽取，得到的集合差异只有 2 个编号。**

## 12.2 每段带几个编号

| 每段 distinct 编号 | 段 |
|---|---:|
| 0 个 | **3** |
| 1 个 | **1,409** |
| 2 个 | **71** |
| 3 个 | **36** |
| 4 个及以上 | **22** |
| **合计** | **1,541** |

3＋1,409＋71＋36＋22 ＝ 1,541 闭合；**带编号段 ＝ 1,538**。

那 **3 段既无编号也无致谢**，全部落在 **149043（macOS Sequoia 15.8）的 Shortcuts 组件**下，原文分别是：

- `A shortcut may be able to transmit data before user authorization is granted`（＋一句 `An authorization issue was addressed …`）
- `A shortcut may be able to access sensitive user data without permission`（两句，分别配 `A privacy issue was addressed …` 与 `A permissions issue was addressed …`）

**这是本篇唯一一段"三字段全无外部锚点"的条目**：没有编号、没有致谢，只有机制句加修复句。**它同时是第 8.2 节"9 句无情态"之外另一类边界样本——它带 `may`，但没有任何可核验标识。** 任何"用编号做主键统计致谢人数"的做法会静默丢掉这 3 段。

## 12.3 致谢：1,538 段、2,737 人次、均值 1.78

| 每段致谢人数 | 段 |
|---|---:|
| 1 人 | **933** |
| 2 人 | **356** |
| 3 人 | **130** |
| 4 人 | **61** |
| 5 人 | **23** |
| 6–8 人 | **17** |
| 9 人及以上 | **18** |
| **合计** | **1,538** |

人次 **2,737**、均值 **2,737／1,538 ＝ 1.78**、单段最大 **15 人**（全树同期均值 1.80、最大 37）。**本篇的致谢密度与全树均值几乎持平（1.78 ∶ 1.80），但单段最高值只有全树最高值的 40%**——媒体／资源管理类条目的致谢是"广而薄"的形态，没有"一条挂 37 个名字"那种研究联盟式条目。

致谢字段命中三刀的段共 **55**，其中 **19 段与主集重合**（这些段的刀词在致谢里再出现一次），另有 **36 段只在致谢里命中**（正文两字段无刀词）。**这 36 段就是本篇的"仅致谢集合"，它与主集合并后构成分母 1,577 段／217 页／166 名。**

## 12.4 36 段仅致谢的构成：26 个职务名 ＋ 10 段免责声明

按词形拆开，36 段仅致谢的命中来源**只有两种**：

| 来源 | 段 | 命中词族 | 说明 |
|---|---:|---|---|
| 致谢里的职务名 | **26** | `manag*` | 全部是 `Manager of Cyber Security`（如 120911「Yadhu Krishna M and Narendra Bhati, Manager of Cyber Security At Suma Soft Pvt. Ltd, Pune (India)」） |
| 页面免责声明漏进致谢字段 | **10** | `releas*` | 原文是「…until an investigation has occurred and **patches or releases** are made available…」 |
| **合计** | **36** | — | 26＋10＝36 闭合 |

**两种污染都可预判**：`manag*` 撞上人名职务、`releas*` 撞上"releases"这种平台公告用语。而那 10 段免责声明**全部位于域 B 的 11 页里的 10 页**（118479、120304–120305、120307–120312、120339；第 11 页 120303 的那段因同段含 `manag*` 已在主集内，不在"仅致谢"之列）——**这是版式切刀在致谢侧的余震，不是词形刀的错误**。

**这 11 段解释了附表 A 里 `releas*` 那一行的口径分裂**：`releas*` 三字段全树 12 段、与主集（三字段口径）重合 2 段，而两字段正文口径下只有 **Impact 1 段／Description 0 段**。多出的 **11** 段全部是域 B 免责声明（每页 1 段、共 11 页），其中 **10** 段在主集外、另 **1** 段（页 120303）已因同段的 `manag*` 进主集。**本篇没有把致谢词形计入刀三**，所以正文口径（1 段）才是可用值。

## 12.5 两字段零命中段 ＝ 0

主集内 Impact 与 Description **两个字段都无刀词命中的段 ＝ 0**。这条是构造性检查：主集由两字段并集定义，所以该值必须为 0；它不为 0 时说明刀的定义与表格取数走了不同的字段。**本篇为 0，第 1.1 节的定义与实际取数一致。**

# 13 中文层：64 个词，12 个零

英文侧量完之后，本章在**中文手册层**（可用页 178 页）上核对同一批概念有没有对应的中文书写。附表 E 给全 64 词，本章只提四组结构性结论。

## 13.1 12 个零词，分成三类原因

64 词里 **12 个在 178 页中一次都没出现**（全零 12／非零 52）：

| 原因类型 | 零词 |
|---|---|
| **中文手册不用这个说法** | `状态管理`、`输入校验`、`附加验证`、`已修复`、`精心构造` |
| **中文手册不写这个概念** | `悬挂`、`未初始化`、`回收`、`任意代码` |
| **中文手册不涉及这个对象** | `电影`、`直播`、`扩展名` |

第一类最重要：**本篇英文侧最大的两个套话词，中文一个都没有对应写法**。`state management` 英文 **662 段**、中文「状态管理」**0 次**；`improved` 侧的「已修复」也是 **0 次**（中文手册用"修复了…问题"这类动词句，不用形容词套话）。**所以任何"把英文措辞口径的结论直接平移到中文手册层"的做法，在这五个词上会立刻失去依据**——不是数值偏小，而是根本没有对应用法。

## 13.2 高频中文词与英文刀不对应

中文侧最高的几个词：`用户` **2,437**、`管理` **2,416**、`访问` **1,880**、`文件` **1,489**、`扩展` **708**、`内存` **511**、`限制` **478**、`内核` **346**、`处理` **337**。

**`管理` 2,416 次跨 178 页（100% 覆盖）——但它与英文 `manag*` 不是同一件事**：最集中的页面是「管理式 Apple 账户安全性」（56 次），那是 **administrative** 语义，不是 memory/state management 语义。**中文的高频"管理"对应英文的 manage／administration 这一大片，而英文套话里的 `improved state management` 在中文手册中没有任何固定译法。**

同样地：`缓存` **234**（最集中页「信任缓存」26 次）是 **cache 作为名词概念**（信任缓存是启动机制），与刀三里的 `cache*` 29 段（作为资源管理动词）不是一回事。**中文层的"缓存"比英文层的 `cache` 更宽，不能反推。**

## 13.3 「容器」23 次全是文件系统，不是媒体容器

附表 A 与第 3.4 节量过：英文 `container` 在主集的 15 段**全部是假阳性**（Web Content Container、Container 后缀等）。中文侧 `容器` **23 次／10 页**，最集中页是「Apple 文件系统作用」（7 次）——**同样是文件系统容器，不是音视频容器**。**两侧在同一天空词上犯同一个方向的错**（都往"文件系统"偏），这是本篇少见的中英一致点。

## 13.4 媒体类中文词的量级远低于英文侧刀一

`媒体` **5**、`视频` **41**、`音频` **39**、`影片` **9**、`播放` **12**、`流媒体` **1**、`电影` **0**、`直播` **0**——八个词相加 **107 次**。而英文刀一在主集内 **219 段**（优先序独占：media* 102＋video* 83＋其余 34，见第 2.1 节）。

**结论不是"中文手册漏译"，而是两份语料的话题分工不同**：中文手册页讲的是"某某机制如何保护"，英文顾问页讲的是"某某数据被如何解析"。**中文层没有"精心构造的媒体文件"这一类句子（`精心构造` 0 次），因此也就没有对应的攻击入口描述**。**跨语言比较时，两侧的词表必须分别独立建立，不能由一侧的结论推另一侧。**

## 13.5 生命周期类：中文同样极薄

`生命周期` **9**、`引用` **3**、`越界` **1**、`溢出` **1**、`边界` **5**、`悬挂` **0**、`未初始化` **0**、`回收` **0**——八个词合计 **19 次**，覆盖页最多 8 页。对照英文侧：`use after free` 127 段、`use-after-free` 193 段（第 5.4 节）、`null pointer` 31 段（与主集 0）。**"内存生命周期"这整片概念在英文顾问页里由 `free／retain／release／uninitialized` 承担，在中文手册层几乎不留痕迹。** 这是第 13.1 节第一类原因的最强例子：**不是译法差异，是文档层根本不谈**。

# 14 组件层：166 个名字扛 1,541 段，而"媒体类名"只能解释六分之一

## 14.1 附表 F 是一张真分区，页列不是

组件名是每一段的**单值属性**（一段只挂一个名），所以附表 F 的段列相加 **＝ 1,541**、与主集严格相等，且 **六个外部集列的行加和恰好等于它们的真并集**：∩R **199**、∩E **45**、∩M2 **15**、∩L **95**、∩BP **57**——这五个数与第 15 章用整段集合独立测得的值**逐个相同**，不需要"行加和 ≠ 并集"的更正。

**只有页列例外**：附表 F 页列合计 **1,231 ＞ 208**，因为同一个组件名横跨多页、多页的同名段各自去重后再相加。**页列与名全集列只能当"行数"读，不能当去重值读**（去重真值是 208 页／166 名）。

## 14.2 头部集中度：两个名占四分之一，26 个名只有一段

| 组件名 | 段 | 占主集 | 页 | 该名全树含 Impact 段 | 本篇占该名 |
|---|---:|---:|---:|---:|---:|
| WebKit | **242** | 15.7% | 113 | 910 | 26.6% |
| Kernel | **152** | 9.9% | 69 | 681 | 22.3% |
| CoreMedia | **103** | 6.7% | 70 | 150 | **68.7%** |
| CoreAudio | **71** | 4.6% | 56 | 79 | **89.9%** |
| Siri | 28 | 1.8% | 22 | 116 | 24.1% |
| WebKit Canvas | 28 | 1.8% | 28 | 28 | **100.0%** |
| Audio | 27 | 1.8% | 27 | 95 | 28.4% |
| ImageIO | 26 | 1.7% | 25 | 261 | **10.0%** |
| Safari | 24 | 1.6% | 21 | 92 | 26.1% |
| Shortcuts | 22 | 1.4% | 17 | 129 | 17.1% |
| AppleAVD | 21 | 1.4% | 21 | 26 | **80.8%** |
| IOKit | 21 | 1.4% | 21 | 26 | **80.8%** |
| sips | 19 | 1.2% | 10 | 25 | **76.0%** |

- 前两名 WebKit ＋ Kernel ＝ **394 段 ＝ 25.6%**；前五名 ＝ **596 段 ＝ 38.7%**。
- 尾部：**26 个名各只有 1 段**、**20 个名各 2 段**，而 **34 个名 ≥10 段**。名层分布与页层同形（薄尾厚头）。
- 166 个名的"该名全树含 Impact 段"合计 **5,770 段 ＝ 7,434 的 77.6%**——本篇的组件覆盖面其实很宽，只是每名的占比高低不同。

## 14.3 精确名与子串名差 48 段

`WebKit` 精确名 **242 段**；组件名里含 `WebKit` 字样的（`WebKit Canvas`／`WebKit Storage`／`WebKit Web Inspector`／`WebKit Sandboxing`／`WebKit Process Model`）合计 **290 段**。**差 48 段全部来自五个 WebKit 子名**（Canvas 28＋Storage 6＋Web Inspector 6＋Sandboxing 5＋Process Model 3 ＝ 48）。

**"WebKit 类"与"WebKit"是两个口径**：用子串匹配做多选题或做组件归并时会多算 48 段（**19.8% 的增量**）。本篇附表 F 一律使用**精确名**。

## 14.4 代理集失效：媒体类组件名只解释 15.6%

按组件名构造"媒体类名"代理集（名中含 Media／Audio／Video／Camera／Photos／Music／TV／Podcast／QuickTime／AVF／AVKit 之一）：它与主集重合 **240 段 ＝ 1,541 的 15.6%**。反过来看，**84.4% 的"媒体数据解析与资源管理"段挂在不含任何媒体字样的组件名下**——WebKit（242）、Kernel（152）、Siri（28）、Shortcuts（22）、ImageIO（26）都在其中。

同一把代理刀落在三刀上：刀一 219 段里 **147 段（67.1%）**挂在媒体类名，刀二 247 段里 **40 段（16.2%）**，刀三 1,105 段里只有 **72 段（6.5%）**。**组件名与刀一的语义几乎重合、与刀二只剩四分之一、与刀三几乎无关（资源管理段的组件名根本不写"媒体"）**。这解释了为什么"按组件筛选媒体漏洞"这件事在解析类上勉强有效、在资源管理类上完全无效。（147＋40＋72＝**259 ＞ 240**：差的 **19 段**是同时被两把刀抓到、在代理集里被数了两次的段——三刀同段为 0，所以最多重复一次。）

另一组值得记的对照：

- **ImageIO 名只有 26 段（该名全树 261 段的 10.0%）**，而本篇与 #132 图像刀复刻集的重合是 **351 段**。**图像词族的段绝大多数不挂在 ImageIO 名下**——它们挂在 WebKit、Kernel、sips、AppleJPEG、Disk Images 等名字上。
- **CoreAudio 89.9%、AppleAVD 80.8%、IOKit 80.8%、sips 76.0%、WebKit Canvas 100.0%、CoreMedia Video Toolbox 100.0%、Video Playback 100.0%**——**占比最高的全是纯多媒体管线组件**；占比最低的是 **Disk Images 5.6%、ImageIO 10.0%、AVEVideoEncoder 17.8%、Shortcuts 17.1%、AirPlay 18.2%**。**"本篇吃掉了某个组件的几乎全部条目"这件事，只在专职多媒体组件上成立。**

## 14.5 外部集在名层的落位

附表 F 合计行给出五个外部集在名层的分布（∩列行和＝真并集，见 14.1）：

| 外部集 | 与主集重合 | 名层最集中的落位 |
|---|---:|---|
| ∩R 远程可达 | **199** | WebKit 48、Kernel 28、Safari 16、AirPlay 14、WebKit Canvas 7 |
| ∩E 在野利用 | **45** | WebKit 13、CoreMedia 8、CoreAudio 5、Accessibility 4、Kernel 1 |
| ∩M2 多后果 | **15** | CoreMedia 1（＋第 9.3 节的 15 段全部含 ACE） |
| ∩L 逻辑缺陷 | **95** | WebKit 32、Kernel 18、AVEVideoEncoder 8、ImageIO 8 |
| ∩BP 绕过 | **57** | Kernel 11、AirPlay 7、Accounts 5、WebKit 4、Safari 4 |

**"逻辑缺陷"措辞在名层明显集中在渲染与图像解码两名（WebKit 32＋ImageIO 8＋AVEVideoEncoder 8＝48＝∩L 的一半）**；**"绕过"措辞集中在 Kernel 与 AirPlay**（11＋7＝18＝31.6%）。**这两条都属于名层观察，不是任何组件的安全性结论。**

## 14.6 三字段比两字段多的段不带来新名字

两字段主集 **1,541 段／208 页／166 名**；三字段并集 **1,577 段／217 页／166 名**。**多的 36 段（仅致谢，见 12.4）没有引入任何新组件名**，只把页面数从 208 推到 217（＋9 页，全部是域 B 与含职务名致谢的页）。**所以"名 166"是两个口径共同的稳定值，而"段"和"页"会随字段口径变化。**

# 15 同数不同集：本篇九处成员级核验，其中两处完全不相交

## 15.1 四处"得数相同"，交集从 28 到 0

| 甲集 | 乙集 | 与主集重合 | 三元交集 |
|---|---|---:|---:|
| ∩R 远程可达 | ∩TLO 类型／对象／生命周期 | **199** ∶ **199** | **28** |
| ∩UU 用户侧 | ∩WEB 网页／浏览器策略 | **342** ∶ **342** | **46** |
| ∩E 在野利用 | ∩HI 非 processing／parsing 动名词 | **45** ∶ **45** | **0** |
| `cache*` 词族 | `stream` 词族 | **29** ∶ **29** | **0** |

四行的右端跨了整整一个区间：**28、46、0、0**。**∩E 与 ∩HI 各贡献主集 45 段却一段都不相干**——"被在野利用"与"动名词主语不是 processing／parsing"这两件事在主集里完全互斥；**`cache*` 与 `stream` 的 29 段也互不相交**（缓存段从不写流，写流的段从不提缓存）。

**任何"两个集与主集重合数相同，所以可以互相代替"的做法，本篇有四处反例。**

## 15.2 三处真相等（成员级，双向独有 0）

| 甲集 | 乙集 | 重合 | 双向独有 |
|---|---|---:|---:|
| 刀二 ∩ PMS（pars／memory／script） | `pars*` ∩ 主集 | **229** ∶ **229** | **0／0** |
| `resource(s)` ∩ 主集 | `exhaust*` ∩ 主集 | **5** ∶ **5** | **0／0** |
| `media*` ∩ 主集 | `crafted media file` ∩ 主集 | **102** ∶ **102** | 三元交 **102** |

这三处是**真正的同一组段落**，可以互换引用：

- **刀二里落在 PMS 的段，全部且只有 `pars*` 段**（`memory`／`script` 两条支在刀二里没有独立贡献）。
- **本篇"资源"只以 `resource exhaustion` 一种形式出现**，`resource(s)` 与 `exhaust*` 抓到同样 5 段。
- **`media*` 词族 102 段与短语 `crafted media file` 102 段是同一批段**——这是第 2.2 节"102∶102∶102 三同"的第三组证据（`media file` 也是 102）。

## 15.3 一处完全不相交与两处弱重合

- **刀二 ∩ TLO ＝ 0 段**：解析／格式／容器这把刀与"类型、对象、生命周期、retain、dealloc、JIT、编译"这一整族概念**在主集里一次都没有共现**。第 4.4 节"苹果不写内存池／垃圾回收／悬挂指针"在这里得到更强的形式：**它也不把"解析"和"生命周期"写进同一段**。
- **刀一 ∩ WEB ＝ 10 段**（219 段里只有 4.6% 与网页／浏览器策略词同段）；**刀一 ∩ IMGX ＝ 87 段**（39.7%）。
- **刀三 ∩ WEB ＝ 332 段**（1,105 段里的 30.0%）、**刀三 ∩ IMGX ＝ 53 段**（4.8%）、**刀三 ∩ CK（检查／校验／净化／强制／限制）＝ 42 段**（3.8%）。

**刀三与"检查校验"类措辞几乎不相干（3.8%）**——这是"资源管理"类条目在公开文本里**不说"检查没做好"、只说"管理方式改了"**的直接证据，与第 10 章的套话分析闭合到同一结论。

## 15.4 与 #132／#133 的重合：三篇并集 3,940 段

本篇内复刻的姊妹篇刀（两字段口径）：**#132 图像刀 1,543 段／211 页／133 名**、**#133 资源边界刀 2,791 段／206 页／154 名**。

| 关系 | 段 |
|---|---:|
| 本篇 ∩ #132 复刻 | **351**（占本篇 22.78%） |
| 本篇 ∩ #133 复刻 | **683**（占本篇 **44.32%**） |
| #132 复刻 ∩ #133 复刻 | **1,051** |
| 三篇共同重合 | **150** |
| 只在本篇、不在两姊妹篇 | **657**（占本篇 42.6%） |
| 三篇并集 | **3,940** 段 |

**与 #133 的重合（683）是近一半，与 #132 的重合（351）只有四分之一；另有 657 段（42.6%）是两篇姊妹篇都不碰的本篇独占区。** 三篇并集 3,940 段 ＝ 全树 9,136 的 **43.13%**，三篇主集相加 5,875 段 ＝ 并集的 1.49 倍——**三篇之间的重复计量已经超过一半集中在 #133 与 #132 之间（1,051 段），而不是与本篇之间。**

**复刻集不等于正式主集**：#132 正式发表的主集是 **1,547 段／212 页／133 名**，本篇复刻词表得到 **1,543／211／133**，差 **4 段／1 页**；#133 正式主集 **2,791／206／154** 与本篇复刻**逐值相同**。**跨篇引用一律以该篇附表为准，复刻值只做本篇内的相对测量。**

## 15.5 两处反事实：为什么不收 memor* 与 analysis

- **把 `memor*` 并进刀**：两字段并集会从 1,541 涨到 **2,948 段**（＋1,407 段，页 208→234，名 166→209），且与 #133 复刻的重合从 **683（44.32%）涨到 2,090（70.90%）**。**收下 `memor*` 就等于把本篇并入 #133**——这才是第 1.1 节把 `memor*` 明确排除在刀三之外的量化理由（而不是"内存不相关"）。
- **把 `analysis` 并进刀**：三字段命中 94 段、与主集重合 **40 段**，而这 40 段里 **23 段属于 Impact 侧、17 段属于 Description 侧**（`analysis` 一词本身在两字段中对主集的命中数为 **0**，全部出现在 ack 字段）。**并入后两字段口径完全不变（仍 1,541），三字段只多 53 段（1,577→1,630）**。所以 `analysis` 族的 94 段有 54 段在主集外，而它们全是 **Threat Analysis Group** 这类致谢单位名——**第 4.5 节"analysis 94／0／0"的含义在此：它是致谢词表，不是内容词。**

## 15.6 九处核验的清单式结论

本章共做 **九处**成员级核验：四处同数（199∶199／342∶342／45∶45／29∶29）、三处真相等（229＝229、5＝5、102＝102）、一处完全不相交（刀二∩TLO＝0）、两处反事实（memor* 与 analysis）。**其中"同数"的四处没有一处是同集（交集 28／46／0／0）**。**得数相同的集在本篇语料里几乎总是不同的段。**

# 16 十二步复算

1. **载入**：`require "./_tmp/tl111load.pl"` → `load_blocks()` 得 `%B`（字段 `imp／des／ack／id／nm`），排除页 `122085`，`U(\%B)` 必须 **＝9,136**。
2. **验锚点**：`%R` 843／`%WO` 178／`%UU` 1,627／`%G` 2,179／`%HI` 273／`%M2` 185／`%E` 108／`%RC` 232／`%L` 550（`Li` 10＋`Ld` 540＋`La` 0），`$ALLI` 7,434——九项由 `die` 守门，任一项漂移则整条流水线不出数。
3. **验分隔符**：`$S`（空格类）、`$H`（空格或连字符）、`$D`（破折号家族＋空格＋连字符）、`$HY`（只含连字符与四个破折号、不含空格）。`$HY` 用来把 `use-after-free` 与 `use after free` 分成两把刀（第 5.4 节）。
4. **验三把主刀**：`$re_MED` 刀一 **219**、`$re_PRS` 刀二 **247**、`$re_MGM` 刀三 **1,105**；并集 `$re3` 两字段 `%CU` **＝1,541**、三字段 `%ALL3` **＝1,577**（差 36 段是致谢侧，见 12.4）。
5. **验六组闭合**：仅刀一 197＋仅刀二 227＋仅刀三 1,087＋一∩二 12＋一∩三 10＋二∩三 8＋三刀同段 **0** ＝ **1,541**；仅I 355＋仅D 1,169＋I∩D 17 ＝ **1,541**；每刀内部 I∩D **＝0／0／0**，而并集 I∩D＝17（第 6.1 节）。
6. **验词界断言**：同一条 `$re3` 去掉 `(?<![\w-])` → **1,975（＋434）**；去掉 `(?![\w-])` → **＋0**。多出的五个词形是 `formation` 439／`mediate` 8／`free` 4／`playing` 2／`mediately` 1，净收益 **430∶4 ＝ 107∶1**（第 5 章）。
7. **验句层**：`SE` 切句 → `D0` 归一 → 只留含 `$re3` 的句 → distinct **59**、句次 **1,559**（I 373＋D 1,186）；页列 **1,012**；句次＝1 的行 **8**；Top1 237／Top3 585／Top5 785／Top10 1,092／Top20 1,360（第 7 章）。
8. **验桶闭合**：附表 C 十五桶相加 **1,559**、未归类 0；续表八档相加 **1,559**，且 `may` 档**句次 359／段 358**是全表唯一的句段分歧（第 7.4 节、8.1 节）。
9. **验情态**：Impact 373 句 `may` 359／`could` 5／零情态 9，相加 **373**；`can／will／would／might／must／should` **全 0**；Description **1,186 句零情态**；句首动名词 333 句 ＝ **89.3%**（第 8 章）。
10. **验两口径后果**：三字段相加 **834**／并集 **783**／差 **51**；Impact 口径相加 **762**／并集 **742**／差 **20**；类数分布 799＋727＋10＋5 ＝ **1,541**，且 ≥2 类组合只有三种 2＋8＋5 ＝ **15 ＝ ∩M2**；Impact 侧 372 段拆成 49＋322＋1（第 9 章）。
11. **验分箱**：页九档 46＋15＋19＋18＋9＋47＋34＋19＋1 ＝ **208**、段档五档 250＋352＋410＋479＋50 ＝ **1,541**、编号五档 3＋1,409＋71＋36＋22 ＝ **1,541**（distinct 418／独占 416／共享 2）、致谢七档 933＋356＋130＋61＋23＋17＋18 ＝ **1,538**（人次 2,737／均值 1.78／最大 15）、附表 F **166 行**（段和 1,541、页列和 1,231、名全集 Impact 5,770）。
12. **验门禁**：`perl _tmp/splice2.pl "<md>" tw134` 注入六表（每个占位符恰好命中 1 次，残留即报错）→ `perl _hbcheck.pl "<md>"` 必须 `RESULT=PASS` → `perl _tmp/c134.pl` 必须 `CONSISTENCY=PASS`。

# 附表 A 判据六组电池：本体词八十项、机制名对照四十五项、句级刀三十二把、套话十五句、外部集十二套与后果八类（193 行）

| 组 | 判据 | 并集段 | 页 | 名 | ∩主集 | ∩R | ∩E | ∩WO |
|---|---|---:|---:|---:|---:|---:|---:|---:|
| 本体词（本篇主刀） | `media* 词族（刀一·媒体）` | 109 | 74 | 8 | 109 | 0 | 5 | 0 |
| 本体词（本篇主刀） | `video* 词族（刀一·视频）` | 83 | 67 | 8 | 83 | 0 | 9 | 0 |
| 本体词（本篇主刀） | `audio* 词族（刀一·音频）` | 63 | 52 | 6 | 63 | 6 | 5 | 0 |
| 本体词（本篇主刀） | `stream(s) 词界（刀一·流）` | 29 | 29 | 3 | 29 | 0 | 5 | 0 |
| 本体词（本篇主刀） | `stream* 词族（刀一·含 streaming）` | 29 | 29 | 3 | 29 | 0 | 5 | 0 |
| 本体词（本篇主刀） | `playback／player／playing（刀一·播放）` | 8 | 8 | 1 | 8 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `play 单词界（刀一）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `sound* 词族（刀一·声音）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `movie(s) 词界（刀一·影片）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `film(s) 词界（刀一·影片）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `speaker 词界（刀一·扬声器）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `voice／speech 词界（刀一·语音）` | 3 | 3 | 1 | 3 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `song／album 词界（刀一·曲目）` | 56 | 41 | 9 | 7 | 7 | 0 | 16 |
| 本体词（本篇主刀） | `camera／microphone（输入侧，留给 #135）` | 13 | 12 | 9 | 4 | 2 | 0 | 0 |
| 本体词（本篇主刀） | `media file 短语（刀一）` | 102 | 74 | 7 | 102 | 0 | 5 | 0 |
| 本体词（本篇主刀） | `crafted media file 短语（刀一）` | 102 | 74 | 7 | 102 | 0 | 5 | 0 |
| 本体词（本篇主刀） | `audio stream 短语（刀一）` | 29 | 29 | 3 | 29 | 0 | 5 | 0 |
| 本体词（本篇主刀） | `video file 短语（刀一）` | 74 | 58 | 7 | 74 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `audio file 短语（刀一）` | 22 | 22 | 1 | 22 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `媒体三词合计（media／video／audio）` | 226 | 120 | 17 | 226 | 6 | 14 | 0 |
| 本体词（本篇主刀） | `刀一并集（媒体本体词）` | 219 | 120 | 16 | 219 | 6 | 14 | 0 |
| 本体词（本篇主刀） | `pars* 词族（刀二·解析）` | 229 | 89 | 40 | 229 | 3 | 0 | 0 |
| 本体词（本篇主刀） | `parse 单词界（刀二）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `parsing 动名词（刀二）` | 229 | 89 | 40 | 229 | 3 | 0 | 0 |
| 本体词（本篇主刀） | `decod*／encod* 词族（刀二·编解码）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `format* 词族（刀二·格式）` | 3 | 3 | 1 | 3 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `container(s) 词界（刀二·容器）` | 15 | 13 | 5 | 15 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `App Sandbox container 短语（刀二假阳性）` | 3 | 3 | 1 | 3 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `data container 短语（刀二假阳性）` | 12 | 10 | 4 | 12 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `刀二并集（解析与格式词）` | 247 | 97 | 45 | 247 | 3 | 0 | 0 |
| 本体词（本篇主刀） | `analysis／analyse／analyz* 词族（刀二候选，已剔除）` | 94 | 55 | 8 | 40 | 0 | 36 | 0 |
| 本体词（本篇主刀） | `其中只由致谢字段带入主集之外（刀二候选剔除量）` | 54 | 38 | 7 | 0 | 0 | 14 | 0 |
| 本体词（本篇主刀） | `Google Threat Analysis Group 机构名（致谢污染源头）` | 94 | 55 | 8 | 40 | 0 | 36 | 0 |
| 本体词（本篇主刀） | `Manager of Cyber Security 职称名（致谢污染源头）` | 37 | 21 | 3 | 11 | 16 | 0 | 0 |
| 本体词（本篇主刀） | `manag* 词族（刀三·管理）` | 1071 | 185 | 130 | 1045 | 181 | 31 | 49 |
| 本体词（本篇主刀） | `management 单词界（刀三）` | 1042 | 184 | 129 | 1042 | 169 | 31 | 49 |
| 本体词（本篇主刀） | `manager 单词界（刀三）` | 37 | 21 | 3 | 11 | 16 | 0 | 0 |
| 本体词（本篇主刀） | `state management 短语（刀三最大套话）` | 662 | 176 | 111 | 662 | 127 | 14 | 45 |
| 本体词（本篇主刀） | `memory management 短语（刀三·与 #133 共用）` | 369 | 115 | 26 | 369 | 41 | 17 | 0 |
| 本体词（本篇主刀） | `memory handling 短语（#133 主套话，本篇不取）` | 862 | 172 | 63 | 78 | 59 | 2 | 0 |
| 本体词（本篇主刀） | `improved state management 套话` | 662 | 176 | 111 | 662 | 127 | 14 | 45 |
| 本体词（本篇主刀） | `improved memory management 套话` | 369 | 115 | 26 | 369 | 41 | 17 | 0 |
| 本体词（本篇主刀） | `allocat* 词族（刀三·分配）` | 4 | 4 | 1 | 4 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `resource(s) 词界（刀三·资源）` | 5 | 5 | 2 | 5 | 5 | 0 | 0 |
| 本体词（本篇主刀） | `resource exhaustion 短语（刀三）` | 5 | 5 | 2 | 5 | 5 | 0 | 0 |
| 本体词（本篇主刀） | `exhaust* 词族（刀三·耗尽）` | 5 | 5 | 2 | 5 | 5 | 0 | 0 |
| 本体词（本篇主刀） | `cache* 词族（刀三·缓存）` | 29 | 24 | 9 | 29 | 14 | 0 | 7 |
| 本体词（本篇主刀） | `pool(s) 词界（刀三·池）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `free／freed／freeing 词族（刀三·释放）` | 170 | 67 | 14 | 170 | 28 | 8 | 0 |
| 本体词（本篇主刀） | `free 单词界（刀三）` | 170 | 67 | 14 | 170 | 28 | 8 | 0 |
| 本体词（本篇主刀） | `freed／freeing 词形` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `garbage／dangling 词界（刀三·回收与悬挂）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `uninitialized 词界（刀三·未初始化）` | 16 | 8 | 2 | 16 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `retain* 词族（刀三·保留）` | 5 | 5 | 1 | 5 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `releas* 词族（刀三·释放词）` | 12 | 12 | 7 | 2 | 1 | 3 | 0 |
| 本体词（本篇主刀） | `use after free 空格形（刀三句式）` | 127 | 49 | 12 | 127 | 20 | 8 | 0 |
| 本体词（本篇主刀） | `use-after-free 连字符形（刀三句式）` | 193 | 100 | 16 | 189 | 13 | 9 | 0 |
| 本体词（本篇主刀） | `double free 两形（刀三句式）` | 43 | 31 | 3 | 43 | 8 | 0 | 0 |
| 本体词（本篇主刀） | `null pointer 短语（刀三句式）` | 31 | 30 | 5 | 0 | 22 | 0 | 0 |
| 本体词（本篇主刀） | `刀三并集（资源管理词）` | 1105 | 189 | 137 | 1105 | 190 | 32 | 56 |
| 本体词（本篇主刀） | `三刀并集（本篇主刀 $re3，两字段）` | 1541 | 208 | 166 | 1541 | 199 | 45 | 56 |
| 本体词（本篇主刀） | `三字段并集（含致谢）` | 1577 | 217 | 166 | 1541 | 209 | 47 | 56 |
| 本体词（本篇主刀） | `仅刀一（闭合分区）` | 197 | 107 | 13 | 197 | 6 | 13 | 0 |
| 本体词（本篇主刀） | `仅刀二（闭合分区）` | 227 | 94 | 43 | 227 | 3 | 0 | 0 |
| 本体词（本篇主刀） | `仅刀三（闭合分区）` | 1087 | 189 | 136 | 1087 | 190 | 31 | 56 |
| 本体词（本篇主刀） | `刀一∩刀二（两刀共现）` | 12 | 12 | 3 | 12 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `刀一∩刀三（两刀共现）` | 10 | 10 | 3 | 10 | 0 | 1 | 0 |
| 本体词（本篇主刀） | `刀二∩刀三（两刀共现）` | 8 | 8 | 1 | 8 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `三刀同段（两刀共现）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `反事实：主刀并入 memor*（两字段）` | 2948 | 234 | 209 | 1541 | 296 | 78 | 56 |
| 本体词（本篇主刀） | `组件名代理：nm 含媒体／音视频类关键字` | 592 | 144 | 21 | 240 | 8 | 13 | 12 |
| 本体词（本篇主刀） | `组件名代理：nm 为 Kernel` | 745 | 153 | 1 | 152 | 77 | 9 | 0 |
| 本体词（本篇主刀） | `组件名代理：nm 含 WebKit` | 1108 | 155 | 8 | 290 | 154 | 41 | 12 |
| 本体词（本篇主刀） | `组件名代理：nm 为 ImageIO` | 293 | 134 | 1 | 26 | 0 | 7 | 0 |
| 本体词（本篇主刀） | `本篇∩#132 主集 IMGX` | 1543 | 211 | 133 | 351 | 138 | 24 | 18 |
| 本体词（本篇主刀） | `本篇∩#133 主集 RESX` | 2791 | 206 | 154 | 683 | 290 | 63 | 7 |
| 本体词（本篇主刀） | `本篇∩#129 三族 PMS` | 2213 | 209 | 130 | 787 | 154 | 65 | 0 |
| 本体词（本篇主刀） | `本篇∩#130 三刀 TLO` | 343 | 120 | 27 | 199 | 53 | 18 | 0 |
| 本体词（本篇主刀） | `本篇∩#131 三刀 WEB` | 1253 | 184 | 45 | 342 | 243 | 46 | 39 |
| 本体词（本篇主刀） | `本篇∩修复词族 CK` | 4146 | 244 | 304 | 386 | 476 | 74 | 96 |
| 句级刀（32 把） | `「The／This issue was addressed with improved checks.」句` | 514 | 138 | 82 | 44 | 68 | 5 | 20 |
| 句级刀（32 把） | `「…improved state management.」句` | 662 | 176 | 111 | 662 | 127 | 14 | 45 |
| 句级刀（32 把） | `「…improved memory management.」句` | 369 | 115 | 26 | 369 | 41 | 17 | 0 |
| 句级刀（32 把） | `「…improved memory handling.」句（#133 套话对照）` | 862 | 172 | 63 | 78 | 59 | 2 | 0 |
| 句级刀（32 把） | `「A use after free issue…」（空格形）` | 127 | 49 | 12 | 127 | 20 | 8 | 0 |
| 句级刀（32 把） | `「A use-after-free issue…」（连字符形）` | 193 | 100 | 16 | 189 | 13 | 9 | 0 |
| 句级刀（32 把） | `「A double free／double-free issue…」句` | 43 | 31 | 3 | 43 | 8 | 0 | 0 |
| 句级刀（32 把） | `「…improved input validation」修复句` | 641 | 151 | 79 | 82 | 140 | 1 | 0 |
| 句级刀（32 把） | `「…additional validation」修复句` | 112 | 65 | 20 | 0 | 8 | 0 | 0 |
| 句级刀（32 把） | `「Processing a maliciously crafted media file…」句` | 73 | 53 | 7 | 73 | 0 | 0 | 0 |
| 句级刀（32 把） | `「Processing a maliciously crafted video file…」句` | 62 | 46 | 5 | 62 | 0 | 0 | 0 |
| 句级刀（32 把） | `「Processing a maliciously crafted audio file…」句` | 14 | 14 | 1 | 14 | 0 | 0 | 0 |
| 句级刀（32 把） | `「Processing an audio stream in a maliciously crafted media file…」句` | 29 | 29 | 3 | 29 | 0 | 5 | 0 |
| 句级刀（32 把） | `「Parsing a maliciously crafted … file…」句` | 47 | 34 | 10 | 47 | 0 | 0 | 0 |
| 句级刀（32 把） | `「Playing a malicious audio file…」句` | 8 | 8 | 1 | 8 | 0 | 0 | 0 |
| 句级刀（32 把） | `「…may lead to unexpected app termination」句` | 612 | 148 | 56 | 254 | 72 | 0 | 0 |
| 句级刀（32 把） | `「…cause／lead to unexpected system termination」句` | 802 | 129 | 53 | 164 | 69 | 0 | 0 |
| 句级刀（32 把） | `「…may terminate the process」句` | 24 | 24 | 3 | 24 | 0 | 0 | 0 |
| 句级刀（32 把） | `「…or corrupt process memory」句` | 189 | 82 | 14 | 117 | 27 | 0 | 0 |
| 句级刀（32 把） | `「…may result in the disclosure of process memory」句` | 127 | 69 | 14 | 8 | 0 | 0 | 0 |
| 句级刀（32 把） | `「…may lead to arbitrary code execution」句` | 123 | 75 | 11 | 16 | 0 | 25 | 0 |
| 句级刀（32 把） | `「…memory corruption」句` | 388 | 127 | 31 | 69 | 3 | 44 | 0 |
| 句级刀（32 把） | `「…may allow an attacker to exhaust…」句` | 5 | 5 | 2 | 5 | 5 | 0 | 0 |
| 句级刀（32 把） | `「…resource exhaustion issue…」句` | 5 | 5 | 2 | 5 | 5 | 0 | 0 |
| 句级刀（32 把） | `「…improved handling of caches.」句` | 29 | 24 | 9 | 29 | 14 | 0 | 7 |
| 句级刀（32 把） | `「…improved restrictions limiting…」句` | 47 | 37 | 12 | 0 | 3 | 0 | 0 |
| 句级刀（32 把） | `「…was addressed by removing the vulnerable code.」句` | 215 | 108 | 56 | 1 | 8 | 4 | 0 |
| 句级刀（32 把） | `「…improved initialization…」句` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 句级刀（32 把） | `「A … issue was addressed with improved…」通用修复句` | 6130 | 252 | 373 | 1386 | 688 | 106 | 175 |
| 句级刀（32 把） | `「…issue existed…」缺陷存在句` | 55 | 45 | 12 | 15 | 9 | 9 | 1 |
| 句级刀（32 把） | `「Apple is aware of a report…」在野句` | 103 | 74 | 12 | 43 | 4 | 103 | 0 |
| 句级刀（32 把） | `其余含三刀词句` | 9136 | 257 | 472 | 1541 | 843 | 108 | 178 |
| 机制名与检测词对照 | `memory（内存，#133 主词对照）` | 1967 | 199 | 102 | 560 | 138 | 60 | 0 |
| 机制名与检测词对照 | `memory management（内存管理）` | 369 | 115 | 26 | 369 | 41 | 17 | 0 |
| 机制名与检测词对照 | `memory handling（内存处理）` | 862 | 172 | 63 | 78 | 59 | 2 | 0 |
| 机制名与检测词对照 | `state management（状态管理）` | 662 | 176 | 111 | 662 | 127 | 14 | 45 |
| 机制名与检测词对照 | `management（管理单词）` | 1042 | 184 | 129 | 1042 | 169 | 31 | 49 |
| 机制名与检测词对照 | `allocation／allocate（分配）` | 4 | 4 | 1 | 4 | 0 | 0 | 0 |
| 机制名与检测词对照 | `free（释放词界）` | 170 | 67 | 14 | 170 | 28 | 8 | 0 |
| 机制名与检测词对照 | `freed／freeing（释放词形）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `use after free 空格形（机制名）` | 127 | 49 | 12 | 127 | 20 | 8 | 0 |
| 机制名与检测词对照 | `use-after-free 连字符形（机制名）` | 193 | 100 | 16 | 189 | 13 | 9 | 0 |
| 机制名与检测词对照 | `double free（两形）` | 43 | 31 | 3 | 43 | 8 | 0 | 0 |
| 机制名与检测词对照 | `null pointer（空指针）` | 31 | 30 | 5 | 0 | 22 | 0 | 0 |
| 机制名与检测词对照 | `uninitialized（未初始化）` | 16 | 8 | 2 | 16 | 0 | 0 | 0 |
| 机制名与检测词对照 | `garbage collection（垃圾回收）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `dangling（悬垂）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `retain（保留）` | 5 | 5 | 1 | 5 | 0 | 0 | 0 |
| 机制名与检测词对照 | `release（释放词）` | 12 | 12 | 7 | 2 | 1 | 3 | 0 |
| 机制名与检测词对照 | `resource（资源）` | 5 | 5 | 2 | 5 | 5 | 0 | 0 |
| 机制名与检测词对照 | `resource exhaustion（资源耗尽）` | 5 | 5 | 2 | 5 | 5 | 0 | 0 |
| 机制名与检测词对照 | `exhaustion（耗尽）` | 5 | 5 | 2 | 5 | 5 | 0 | 0 |
| 机制名与检测词对照 | `cache（缓存）` | 29 | 24 | 9 | 29 | 14 | 0 | 7 |
| 机制名与检测词对照 | `pool（池）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `buffer（缓冲区）` | 200 | 79 | 32 | 8 | 23 | 0 | 0 |
| 机制名与检测词对照 | `overflow（溢出）` | 374 | 114 | 46 | 8 | 54 | 0 | 0 |
| 机制名与检测词对照 | `out of bounds（两形）` | 816 | 172 | 62 | 143 | 72 | 19 | 0 |
| 机制名与检测词对照 | `bounds checking` | 699 | 154 | 63 | 93 | 81 | 15 | 0 |
| 机制名与检测词对照 | `leak（泄露词界）` | 192 | 101 | 31 | 24 | 63 | 0 | 0 |
| 机制名与检测词对照 | `disclos*（泄露正式词）` | 427 | 141 | 54 | 84 | 11 | 2 | 0 |
| 机制名与检测词对照 | `kernel memory（内核内存）` | 520 | 113 | 34 | 72 | 41 | 16 | 0 |
| 机制名与检测词对照 | `process memory（进程内存）` | 326 | 110 | 24 | 125 | 27 | 0 | 0 |
| 机制名与检测词对照 | `media file（媒体文件）` | 102 | 74 | 7 | 102 | 0 | 5 | 0 |
| 机制名与检测词对照 | `audio stream（音频流）` | 29 | 29 | 3 | 29 | 0 | 5 | 0 |
| 机制名与检测词对照 | `maliciously crafted（恶意构造）` | 1769 | 218 | 81 | 495 | 33 | 61 | 4 |
| 机制名与检测词对照 | `processing（处理动名词）` | 1769 | 228 | 67 | 427 | 0 | 62 | 0 |
| 机制名与检测词对照 | `parsing（解析动名词）` | 229 | 89 | 40 | 229 | 3 | 0 | 0 |
| 机制名与检测词对照 | `visiting／viewing 动名词` | 82 | 57 | 6 | 13 | 82 | 0 | 0 |
| 机制名与检测词对照 | `memory corruption` | 388 | 127 | 31 | 69 | 3 | 44 | 0 |
| 机制名与检测词对照 | `arbitrary code execution` | 218 | 100 | 22 | 18 | 26 | 25 | 0 |
| 机制名与检测词对照 | `denial-of-service` | 470 | 155 | 62 | 36 | 164 | 0 | 0 |
| 机制名与检测词对照 | `unexpected termination` | 4 | 3 | 3 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `crash（崩溃）` | 499 | 128 | 17 | 159 | 0 | 0 | 0 |
| 机制名与检测词对照 | `sandbox（沙盒）` | 475 | 120 | 74 | 43 | 40 | 8 | 6 |
| 机制名与检测词对照 | `remote（远程词界）` | 257 | 101 | 34 | 44 | 257 | 0 | 0 |
| 机制名与检测词对照 | `input validation` | 676 | 151 | 85 | 82 | 146 | 1 | 0 |
| 机制名与检测词对照 | `improved checks` | 1018 | 192 | 125 | 69 | 151 | 31 | 31 |
| 修复套话句（全篇共用 15 项） | `The issue was addressed with improved checks.` | 514 | 138 | 82 | 44 | 68 | 5 | 20 |
| 修复套话句（全篇共用 15 项） | `improved bounds checking` | 699 | 154 | 63 | 93 | 81 | 15 | 0 |
| 修复套话句（全篇共用 15 项） | `improved input validation` | 641 | 151 | 79 | 82 | 140 | 1 | 0 |
| 修复套话句（全篇共用 15 项） | `improved validation` | 367 | 101 | 76 | 0 | 13 | 23 | 15 |
| 修复套话句（全篇共用 15 项） | `additional checks` | 34 | 28 | 12 | 0 | 0 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `additional validation` | 112 | 65 | 20 | 0 | 8 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `improved checks to prevent unauthorized actions` | 16 | 16 | 4 | 0 | 0 | 8 | 0 |
| 修复套话句（全篇共用 15 项） | `addressed with（全形）` | 6589 | 254 | 364 | 1286 | 735 | 104 | 163 |
| 修复套话句（全篇共用 15 项） | `addressed by` | 371 | 136 | 80 | 8 | 31 | 4 | 4 |
| 修复套话句（全篇共用 15 项） | `addressed through` | 247 | 129 | 48 | 239 | 47 | 0 | 11 |
| 修复套话句（全篇共用 15 项） | `validation 词族` | 1341 | 179 | 168 | 155 | 184 | 24 | 15 |
| 修复套话句（全篇共用 15 项） | `check(s)／checking 词族` | 2017 | 227 | 201 | 176 | 243 | 46 | 48 |
| 修复套话句（全篇共用 15 项） | `sanitiz* 词族` | 93 | 54 | 21 | 16 | 3 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `enforce* 词族` | 53 | 34 | 4 | 18 | 0 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `restrict* 词族` | 793 | 153 | 109 | 21 | 75 | 4 | 33 |
| 外部集对照（12 套，含 #122–#126） | `远程可达 R` | 843 | 178 | 72 | 199 | 843 | 4 | 27 |
| 外部集对照（12 套，含 #122–#126） | `without 词界集 WO` | 178 | 97 | 37 | 56 | 27 | 0 | 178 |
| 外部集对照（12 套，含 #122–#126） | `exploit* 集 E` | 108 | 76 | 12 | 45 | 4 | 108 | 0 |
| 外部集对照（12 套，含 #122–#126） | `user 词界集 UU` | 1627 | 175 | 200 | 342 | 130 | 0 | 105 |
| 外部集对照（12 套，含 #122–#126） | `动名词开头集 G` | 2179 | 224 | 98 | 609 | 108 | 53 | 0 |
| 外部集对照（12 套，含 #122–#126） | `非处理/解析类 HI` | 273 | 102 | 35 | 45 | 108 | 0 | 0 |
| 外部集对照（12 套，含 #122–#126） | `≥2 后果类 M2` | 185 | 82 | 35 | 15 | 23 | 0 | 0 |
| 外部集对照（12 套，含 #122–#126） | `race condition 集 RC（#122）` | 232 | 95 | 40 | 11 | 14 | 0 | 3 |
| 外部集对照（12 套，含 #122–#126） | `logic 集 L（#123）` | 550 | 162 | 81 | 95 | 99 | 9 | 24 |
| 外部集对照（12 套，含 #122–#126） | `拒绝服务集 Ds（#124）` | 470 | 155 | 62 | 36 | 164 | 0 | 0 |
| 外部集对照（12 套，含 #122–#126） | `泄露集 X（#125）` | 427 | 141 | 54 | 84 | 11 | 2 | 0 |
| 外部集对照（12 套，含 #122–#126） | `绕过集 BP（#126）` | 301 | 114 | 58 | 57 | 42 | 20 | 0 |
| 后果强度八类 | `ACE 类` | 336 | 120 | 43 | 37 | 36 | 31 | 0 |
| 后果强度八类 | `BYP 类` | 301 | 114 | 58 | 57 | 42 | 20 | 0 |
| 后果强度八类 | `DIS 类` | 603 | 159 | 78 | 106 | 74 | 2 | 0 |
| 后果强度八类 | `DOS 类` | 2014 | 176 | 130 | 498 | 343 | 0 | 0 |
| 后果强度八类 | `FULL 类` | 63 | 43 | 18 | 7 | 7 | 0 | 0 |
| 后果强度八类 | `MEM 类` | 390 | 127 | 31 | 69 | 3 | 44 | 0 |
| 后果强度八类 | `PRI 类` | 124 | 62 | 30 | 17 | 0 | 8 | 0 |
| 后果强度八类 | `SBX 类` | 475 | 120 | 74 | 43 | 40 | 8 | 6 |
| **合计** | 192 项（并集全零 16 项、非零 176 项） | 1541 | — | — | 1541 | 199 | 45 | 56 |

# 附表 B 五十九句含主刀整句 × 归桶 × 句次 × 五个外部集（60 行）

| distinct 整句 | 归入桶 | 句次 | Impact 句 | Desc 句 | 页 | ∩R | ∩E | ∩M2 | ∩L | ∩RC |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| This issue was addressed through improved state management. | 「…improved state management.」句 | 237 | 0 | 237 | 127 | 47 | 0 | 5 | 0 | 0 |
| A use-after-free issue was addressed with improved memory management. | 「…improved memory management.」句 | 189 | 0 | 189 | 98 | 13 | 9 | 1 | 0 | 0 |
| An authorization issue was addressed with improved state management. | 「…improved state management.」句 | 159 | 0 | 159 | 61 | 13 | 4 | 0 | 0 | 0 |
| A use after free issue was addressed with improved memory management. | 「…improved memory management.」句 | 127 | 0 | 127 | 49 | 20 | 8 | 0 | 0 | 0 |
| A parsing issue in the handling of directory paths was addressed with improved path validation. | 其余含三刀词句 | 73 | 0 | 73 | 45 | 0 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted media file may lead to unexpected app termination or corrupt process memory | 「Processing a maliciously crafted media file…」句 | 73 | 73 | 0 | 53 | 0 | 0 | 0 | 0 | 0 |
| A logic issue was addressed with improved state management. | 「…improved state management.」句 | 71 | 0 | 71 | 49 | 12 | 0 | 0 | 71 | 0 |
| Parsing a file may lead to an unexpected app termination | 「…may lead to unexpected app termination」句 | 63 | 63 | 0 | 34 | 0 | 0 | 0 | 0 | 0 |
| An authentication issue was addressed with improved state management. | 「…improved state management.」句 | 57 | 0 | 57 | 44 | 24 | 0 | 0 | 0 | 0 |
| A double free issue was addressed with improved memory management. | 「…improved memory management.」句 | 43 | 0 | 43 | 31 | 8 | 0 | 0 | 0 | 0 |
| Parsing a maliciously crafted file may lead to an unexpected app termination | 「Parsing a maliciously crafted … file…」句 | 33 | 33 | 0 | 25 | 0 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted video file may lead to unexpected app termination | 「Processing a maliciously crafted video file…」句 | 32 | 32 | 0 | 30 | 0 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted video file may lead to unexpected app termination or corrupt process memory | 「Processing a maliciously crafted video file…」句 | 30 | 30 | 0 | 23 | 0 | 0 | 0 | 0 | 0 |
| An inconsistent user interface issue was addressed with improved state management. | 「…improved state management.」句 | 29 | 0 | 29 | 26 | 14 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved handling of caches. | 「…improved handling of caches.」句 | 29 | 0 | 29 | 24 | 14 | 0 | 0 | 0 | 0 |
| Parsing a file may lead to disclosure of user information | 其余含三刀词句 | 27 | 27 | 0 | 24 | 0 | 0 | 0 | 0 | 0 |
| An information disclosure issue was addressed with improved state management. | 「…improved state management.」句 | 24 | 0 | 24 | 12 | 10 | 0 | 0 | 0 | 0 |
| Processing an audio stream in a maliciously crafted media file may terminate the process | 「Processing an audio stream in a maliciously crafted media file…」句 | 24 | 24 | 0 | 24 | 0 | 0 | 0 | 0 | 0 |
| A memory corruption issue was addressed with improved state management. | 「…improved state management.」句 | 23 | 0 | 23 | 23 | 0 | 5 | 0 | 0 | 0 |
| A cookie management issue was addressed with improved state management. | 「…improved state management.」句 | 17 | 0 | 17 | 17 | 6 | 5 | 0 | 0 | 0 |
| Parsing a maliciously crafted video file may lead to unexpected system termination | 「Parsing a maliciously crafted … file…」句 | 12 | 12 | 0 | 12 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved restriction of data container access. | 「A … issue was addressed with improved…」通用修复句 | 12 | 0 | 12 | 10 | 0 | 0 | 0 | 0 | 0 |
| A race condition was addressed with improved state management. | 「…improved state management.」句 | 11 | 0 | 11 | 10 | 0 | 0 | 8 | 0 | 11 |
| An information disclosure issue was addressed with improved memory management. | 「…improved memory management.」句 | 10 | 0 | 10 | 10 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved state management. | 「…improved state management.」句 | 10 | 0 | 10 | 10 | 0 | 0 | 0 | 0 | 0 |
| A logic issue existed when processing a maliciously crafted photo or video shared via an iCloud Link. | 「…issue existed…」缺陷存在句 | 9 | 9 | 0 | 9 | 0 | 9 | 0 | 9 | 0 |
| An app with root privileges may be able to read uninitialized kernel memory | 其余含三刀词句 | 8 | 8 | 0 | 8 | 0 | 0 | 0 | 0 | 0 |
| An uninitialized memory issue was addressed with improved memory initialization. | 「A … issue was addressed with improved…」通用修复句 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 | 0 |
| Parsing an image may lead to disclosure of user information | 其余含三刀词句 | 8 | 8 | 0 | 8 | 0 | 0 | 0 | 8 | 0 |
| Playing a malicious audio file may lead to an unexpected app termination | 「Playing a malicious audio file…」句 | 8 | 8 | 0 | 8 | 0 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted audio file may corrupt process memory | 「Processing a maliciously crafted audio file…」句 | 8 | 8 | 0 | 8 | 0 | 0 | 0 | 0 | 0 |
| A mail header parsing issue was addressed with improved checks. | 「A … issue was addressed with improved…」通用修复句 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 | 0 |
| A lock screen issue was addressed with improved state management. | 「…improved state management.」句 | 6 | 0 | 6 | 6 | 0 | 0 | 0 | 0 | 0 |
| A malicious website may exfiltrate audio data cross-origin | 其余含三刀词句 | 6 | 6 | 0 | 6 | 6 | 0 | 0 | 0 | 0 |
| A permissions issue was addressed with improved state management. | 「…improved state management.」句 | 6 | 0 | 6 | 3 | 1 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted audio file may lead to memory corruption | 「Processing a maliciously crafted audio file…」句 | 6 | 6 | 0 | 6 | 0 | 0 | 0 | 0 | 0 |
| A resource exhaustion issue was addressed with improved input validation. | 「…improved input validation」修复句 | 5 | 0 | 5 | 5 | 5 | 0 | 0 | 0 | 0 |
| Notifications marked for deletion could be unexpectedly retained on the device | 其余含三刀词句 | 5 | 5 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| Processing an audio stream in a maliciously crafted media file may result in code execution. | 「Processing an audio stream in a maliciously crafted media file…」句 | 5 | 5 | 0 | 5 | 0 | 5 | 0 | 0 | 0 |
| A Content Security Policy bypass was addressed with improved enforcement in AudioWorklet contexts. | 其余含三刀词句 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| A malicious app may be able to modify other apps without having App Management permission | 其余含三刀词句 | 4 | 4 | 0 | 4 | 0 | 0 | 0 | 4 | 0 |
| A privacy issue was addressed with improved state management. | 「…improved state management.」句 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| A session management issue was addressed with improved checks. | 「A … issue was addressed with improved…」通用修复句 | 4 | 0 | 4 | 4 | 1 | 0 | 0 | 0 | 0 |
| A type confusion issue was addressed with improved state management. | 「…improved state management.」句 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| Multiple issues were addressed by disabling array allocation sinking. | 其余含三刀词句 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to access protected files within an App Sandbox container | 其余含三刀词句 | 3 | 3 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| An issue existed in the parsing of URLs. | 「…issue existed…」缺陷存在句 | 3 | 0 | 3 | 3 | 3 | 0 | 0 | 0 | 0 |
| An uncontrolled format string issue was addressed with improved input validation. | 「…improved input validation」修复句 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed by improved management of object lifetimes. | 「A … issue was addressed with improved…」通用修复句 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| This was addressed with improved state management. | 「…improved state management.」句 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 3 | 0 |
| An attacker with physical access to a device may be able to override managed Wi-Fi profiles | 其余含三刀词句 | 2 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 |
| An authorization issue was addressed with improved state management | 「…improved state management.」句 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| Apple is aware of a report that this issue may have been exploited in an extremely sophisticated attack against specific targeted individuals on versions of iOS released before iOS 18.4.1. | 「Apple is aware of a report…」在野句 | 1 | 1 | 0 | 1 | 0 | 1 | 0 | 0 | 0 |
| Audio messages in Messages may be able to capture a few seconds of audio before the microphone indicator is activated | 其余含三刀词句 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| Muting the microphone during a FaceTime call may not result in audio being silenced | 其余含三刀词句 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| On MDM managed devices, an app may be able to bypass certain Privacy preferences | 其余含三刀词句 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| Parsing a file may lead to an unexpected app termination or arbitrary code execution | 「…may lead to unexpected app termination」句 | 1 | 1 | 0 | 1 | 0 | 0 | 1 | 0 | 0 |
| Parsing a maliciously crafted binary may lead to unexpected app termination | 「Parsing a maliciously crafted … file…」句 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| Parsing a maliciously crafted binary may lead to unexpected tool termination | 「Parsing a maliciously crafted … file…」句 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| **合计** | 15 桶 | 1559 | 373 | 1186 | 页列合计 1012 | 199 | 46 | 15 | 95 | 11 |

# 附表 C 十五个句形桶与句次闭合，附八档句式形态（16 行）

| 桶 | 句次 | 段 | 页 | 名 | Impact 句 | Desc 句 | ∩R | ∩E |
|---|---:|---:|---:|---:|---:|---:|---:|---:|
| 「…improved state management.」句 | 662 | 662 | 176 | 111 | 0 | 662 | 127 | 14 |
| 「…improved memory management.」句 | 369 | 369 | 115 | 26 | 0 | 369 | 41 | 17 |
| 其余含三刀词句 | 147 | 147 | 99 | 34 | 66 | 81 | 8 | 0 |
| 「Processing a maliciously crafted media file…」句 | 73 | 73 | 53 | 7 | 73 | 0 | 0 | 0 |
| 「…may lead to unexpected app termination」句 | 64 | 64 | 35 | 7 | 64 | 0 | 0 | 0 |
| 「Processing a maliciously crafted video file…」句 | 62 | 62 | 46 | 5 | 62 | 0 | 0 | 0 |
| 「Parsing a maliciously crafted … file…」句 | 47 | 47 | 34 | 10 | 47 | 0 | 0 | 0 |
| 「A … issue was addressed with improved…」通用修复句 | 34 | 34 | 32 | 9 | 0 | 34 | 1 | 0 |
| 「Processing an audio stream in a maliciously crafted media file…」句 | 29 | 29 | 29 | 3 | 29 | 0 | 0 | 5 |
| 「…improved handling of caches.」句 | 29 | 29 | 24 | 9 | 0 | 29 | 14 | 0 |
| 「Processing a maliciously crafted audio file…」句 | 14 | 14 | 14 | 1 | 14 | 0 | 0 | 0 |
| 「…issue existed…」缺陷存在句 | 12 | 12 | 12 | 2 | 9 | 3 | 3 | 9 |
| 「Playing a malicious audio file…」句 | 8 | 8 | 8 | 1 | 8 | 0 | 0 | 0 |
| 「…improved input validation」修复句 | 8 | 8 | 8 | 3 | 0 | 8 | 5 | 0 |
| 「Apple is aware of a report…」在野句 | 1 | 1 | 1 | 1 | 1 | 0 | 0 | 1 |
| **合计** | 1559 | 段列可重叠 | 页列合计 686 | 名列合计 229 | 373 | 1186 | — | — |

## 附表 C 续表：句子打头与修复动词形态（优先序闭合分区）

| 形态刀 | 句次 | 段 | 页 | 名 |
|---|---:|---:|---:|---:|
| 「The／This issue was addressed …」打头 | 291 | 291 | 143 | 52 |
| 「A／An X issue was addressed …」打头 | 487 | 487 | 148 | 82 |
| 「A／An X（非 issue 名词）was addressed …」打头 | 398 | 398 | 123 | 60 |
| 「Multiple X issues were addressed …」打头 | 4 | 4 | 4 | 1 |
| 「X existed（缺陷存在句）」 | 12 | 12 | 12 | 2 |
| 含「was addressed with」 | 3 | 3 | 3 | 1 |
| 含「may」（影响句情态） | 359 | 358 | 124 | 33 |
| 其余句式 | 5 | 5 | 5 | 1 |
| **合计** | 1559 | — | — | — |

# 附表 D 页面、编号、致谢、版式与字段落位五类八块分箱（30 行）

| 块 | 分箱 | 计数 | 说明 |
|---|---|---:|---|
| RESM 段所在页的段数 | 1 | 46 | 共 208 页含本篇条目（全树 257 页） |
| RESM 段所在页的段数 | 2 | 15 | 共 208 页含本篇条目（全树 257 页） |
| RESM 段所在页的段数 | 3 | 19 | 共 208 页含本篇条目（全树 257 页） |
| RESM 段所在页的段数 | 4 | 18 | 共 208 页含本篇条目（全树 257 页） |
| RESM 段所在页的段数 | 5 | 9 | 共 208 页含本篇条目（全树 257 页） |
| RESM 段所在页的段数 | 6-9 | 47 | 共 208 页含本篇条目（全树 257 页） |
| RESM 段所在页的段数 | 10-19 | 34 | 共 208 页含本篇条目（全树 257 页） |
| RESM 段所在页的段数 | 20-49 | 19 | 共 208 页含本篇条目（全树 257 页） |
| RESM 段所在页的段数 | 50+ | 1 | 共 208 页含本篇条目（全树 257 页） |
| RESM 段所在页的段数 | 合计 | 208 | 校验=208 页、RESM 段 1541 |
| Top10 页 | 前 10 页合计 | 316 | 占 1541 段的 20.5% |
| 同页共存 | 208 页全部段 | 8981 | 本篇占该页集 17.16%，每页均 7.41 段（第三套分母） |
| 同页共存 | 全本篇页 | 23 | 该页所有段都被本篇抓到 |
| 编号数分箱 | 0 个 | 3 | 无编号段 |
| 编号数分箱 | 1 个 | 1409 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 2 个 | 71 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 3 个 | 36 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 4+ 个 | 22 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 合计 | 1541 | 带编号段=1538／1541，distinct 编号=418，本集独占=416，与他集共享=2 |
| 致谢人数分箱 | 1 人 | 933 | — |
| 致谢人数分箱 | 2 人 | 356 | — |
| 致谢人数分箱 | 3 人 | 130 | — |
| 致谢人数分箱 | 4 人 | 61 | — |
| 致谢人数分箱 | 5 人 | 23 | — |
| 致谢人数分箱 | 6-8 人 | 17 | — |
| 致谢人数分箱 | 9+ 人 | 18 | — |
| 致谢人数分箱 | 合计 | 1538 | 人次=2737，均值=1.78，最大=15（全树 1.80／37） |
| 版式域 | 域 B（11 页粗体版式） | 3 | 域 B 全段 91，占比 3.30%；域 A 1538／9,045 ＝ 17.00% |
| 字段落位 | Impact 侧 | 372 | Description 侧 1186；仅I 355／仅D 1169／I∩D 17（两组闭合 1541） |
| 句级 | 1541 段的本篇句数 | 1 句 1523／2 句 18 | 句次合计 1559（I 373＋D 1186），distinct 句 59 |

# 附表 E 中文手册层八组六十四词（65 行）

| 组 | 中文词 | 出现次数 | 覆盖页 | 覆盖页占比 | 最集中页面 |
|---|---|---:|---:|---:|---|
| 媒体与音视频类 | 媒体 | 5 | 4 | 2.2% | 保护 App 对用户数据的访问（2 次） |
| 媒体与音视频类 | 视频 | 41 | 12 | 6.7% | HomeKit 摄像头安全性（16 次） |
| 媒体与音视频类 | 音频 | 39 | 9 | 5.1% | HomeKit 通信安全性（11 次） |
| 媒体与音视频类 | 影片 | 9 | 2 | 1.1% | iOS 和 iPadOS 中的 ReplayKit 安全性（8 次） |
| 媒体与音视频类 | 电影 | 0 | 0 | 0.0% | — |
| 媒体与音视频类 | 直播 | 0 | 0 | 0.0% | — |
| 媒体与音视频类 | 流媒体 | 1 | 1 | 0.6% | HomeKit 摄像头安全性（1 次） |
| 媒体与音视频类 | 播放 | 12 | 7 | 3.9% | HomeKit 通信安全性（3 次） |
| 解析与格式类 | 解析 | 8 | 5 | 2.8% | iOS、iPadOS 和 watchOS 的 SiriKit 安全性（3 次） |
| 解析与格式类 | 解码 | 1 | 1 | 0.6% | iOS 和 iPadOS 中的 ReplayKit 安全性（1 次） |
| 解析与格式类 | 编码 | 10 | 6 | 3.4% | iOS 和 iPadOS 中的 ReplayKit 安全性（3 次） |
| 解析与格式类 | 格式 | 18 | 14 | 7.9% | 基于 Intel 的 Mac 的启动过程（4 次） |
| 解析与格式类 | 容器 | 23 | 10 | 5.6% | Apple 文件系统作用（7 次） |
| 解析与格式类 | 元数据 | 40 | 16 | 9.0% | Apple 设备中的数据保护（6 次） |
| 解析与格式类 | 扩展名 | 0 | 0 | 0.0% | — |
| 解析与格式类 | 文件 | 1489 | 178 | 100.0% | Apple 设备中的数据保护（36 次） |
| 资源与管理类 | 资源 | 27 | 15 | 8.4% | iOS、iPadOS、Apple tvOS、visionOS 和 watchOS 中的 App 代码签名过程（5 次） |
| 资源与管理类 | 内存 | 511 | 178 | 100.0% | 安全隔区（49 次） |
| 资源与管理类 | 管理 | 2416 | 178 | 100.0% | 管理式 Apple 账户安全性（56 次） |
| 资源与管理类 | 分配 | 21 | 12 | 6.7% | 操作系统完整性（5 次） |
| 资源与管理类 | 释放 | 7 | 4 | 2.2% | “平安确认”安全性（3 次） |
| 资源与管理类 | 缓存 | 234 | 178 | 100.0% | 信任缓存（26 次） |
| 资源与管理类 | 回收 | 0 | 0 | 0.0% | — |
| 资源与管理类 | 初始化 | 14 | 11 | 6.2% | LocalPolicy 签名密钥创建和管理（2 次） |
| 生命周期与越界类 | 生命周期 | 9 | 8 | 4.5% | 证明过程安全性（2 次） |
| 生命周期与越界类 | 引用 | 3 | 2 | 1.1% | 钥匙串数据保护（2 次） |
| 生命周期与越界类 | 悬挂 | 0 | 0 | 0.0% | — |
| 生命周期与越界类 | 未初始化 | 0 | 0 | 0.0% | — |
| 生命周期与越界类 | 重复 | 13 | 8 | 4.5% | 密码安全建议（6 次） |
| 生命周期与越界类 | 越界 | 1 | 1 | 0.6% | 操作系统完整性（1 次） |
| 生命周期与越界类 | 溢出 | 1 | 1 | 0.6% | 内存安全 iBoot 实施（1 次） |
| 生命周期与越界类 | 边界 | 5 | 5 | 2.8% | 内存安全 iBoot 实施（1 次） |
| 后果强度类 | 任意代码 | 0 | 0 | 0.0% | — |
| 后果强度类 | 代码执行 | 3 | 2 | 1.1% | 搭载 Apple 芯片的 Mac 上的 Rosetta 2（2 次） |
| 后果强度类 | 内存损坏 | 4 | 2 | 1.1% | 操作系统完整性（3 次） |
| 后果强度类 | 拒绝服务 | 1 | 1 | 0.6% | macOS 中的防火墙安全性（1 次） |
| 后果强度类 | 泄露 | 30 | 11 | 6.2% | 密码监视（15 次） |
| 后果强度类 | 崩溃 | 6 | 4 | 2.2% | Apple 操作系统中的后台安全改进（3 次） |
| 后果强度类 | 终止 | 13 | 7 | 3.9% | iOS 中的车钥匙安全性（4 次） |
| 后果强度类 | 意外 | 4 | 4 | 2.2% | 钥匙串安全同步（1 次） |
| 修复套话类 | 改进的 | 3 | 3 | 1.7% | Apple 操作系统中的后台安全改进（1 次） |
| 修复套话类 | 状态管理 | 0 | 0 | 0.0% | — |
| 修复套话类 | 输入校验 | 0 | 0 | 0.0% | — |
| 修复套话类 | 附加验证 | 0 | 0 | 0.0% | — |
| 修复套话类 | 已修复 | 0 | 0 | 0.0% | — |
| 修复套话类 | 处理器 | 278 | 178 | 100.0% | 安全隔区（21 次） |
| 修复套话类 | 更新 | 373 | 178 | 100.0% | 文稿修订记录（31 次） |
| 修复套话类 | 限制 | 478 | 178 | 100.0% | LocalPolicy 签名密钥创建和管理（21 次） |
| 组件与内核类 | 内核 | 346 | 178 | 100.0% | 在 macOS 中安全扩展内核（23 次） |
| 组件与内核类 | 组件 | 270 | 178 | 100.0% | 安全隔区（32 次） |
| 组件与内核类 | 驱动 | 21 | 12 | 6.7% | macOS 的 DriverKit 安全性（5 次） |
| 组件与内核类 | 框架 | 26 | 18 | 10.1% | Apple 操作系统中的后台安全改进（3 次） |
| 组件与内核类 | 接口 | 32 | 17 | 9.6% | Apple 设备的无线局域网隐私（4 次） |
| 组件与内核类 | 沙盒 | 27 | 11 | 6.2% | macOS 中的 Option ROM 安全性（6 次） |
| 组件与内核类 | 扩展 | 708 | 178 | 100.0% | iOS、iPadOS、macOS 和 visionOS 中支持的扩展项（34 次） |
| 组件与内核类 | 子系统 | 15 | 8 | 4.5% | 术语表（4 次） |
| 通道与触发类 | 恶意 | 287 | 178 | 100.0% | 在 macOS 中防范恶意软件（39 次） |
| 通道与触发类 | 精心构造 | 0 | 0 | 0.0% | — |
| 通道与触发类 | 处理 | 337 | 178 | 100.0% | 安全隔区（24 次） |
| 通道与触发类 | 打开 | 121 | 53 | 29.8% | iCloud 高级数据保护（16 次） |
| 通道与触发类 | 访问 | 1880 | 178 | 100.0% | 保护对用户健康数据的访问（32 次） |
| 通道与触发类 | 远程 | 242 | 178 | 100.0% | 受管理的丢失模式和远程擦除（12 次） |
| 通道与触发类 | 网站 | 93 | 36 | 20.2% | Apple 钱包中的证件（14 次） |
| 通道与触发类 | 用户 | 2437 | 178 | 100.0% | Apple 钱包中的证件（71 次） |
| **合计** | 64 词（全零 12 词、非零 52 词） | — | — | 178 页可用 | — |

# 附表 F 组件名层一百六十六名 × 八个外部集 × 该名含 Impact 全集（167 行）

| 组件名 | RESM 段 | 页 | ∩R | ∩E | ∩M2 | ∩L | ∩BP | 该组件含 Impact 全集 | RESM 占比 |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| WebKit | 242 | 113 | 48 | 13 | 0 | 32 | 4 | 910 | 26.6% |
| Kernel | 152 | 69 | 28 | 1 | 0 | 18 | 11 | 681 | 22.3% |
| CoreMedia | 103 | 70 | 0 | 8 | 1 | 0 | 0 | 150 | 68.7% |
| CoreAudio | 71 | 56 | 0 | 5 | 0 | 0 | 0 | 79 | 89.9% |
| Siri | 28 | 22 | 0 | 0 | 0 | 1 | 0 | 116 | 24.1% |
| WebKit Canvas | 28 | 28 | 7 | 0 | 0 | 0 | 0 | 28 | 100.0% |
| Audio | 27 | 27 | 0 | 0 | 0 | 0 | 0 | 95 | 28.4% |
| ImageIO | 26 | 25 | 0 | 0 | 0 | 8 | 0 | 261 | 10.0% |
| Safari | 24 | 21 | 16 | 0 | 0 | 0 | 4 | 92 | 26.1% |
| Shortcuts | 22 | 17 | 0 | 0 | 0 | 0 | 0 | 129 | 17.1% |
| AppleAVD | 21 | 21 | 0 | 0 | 0 | 0 | 0 | 26 | 80.8% |
| IOKit | 21 | 21 | 0 | 0 | 0 | 0 | 0 | 26 | 80.8% |
| sips | 19 | 10 | 0 | 0 | 0 | 0 | 0 | 25 | 76.0% |
| NetworkExtension | 17 | 9 | 2 | 0 | 0 | 0 | 0 | 28 | 60.7% |
| Accounts | 15 | 13 | 0 | 0 | 0 | 0 | 5 | 34 | 44.1% |
| AppleJPEG | 15 | 15 | 0 | 0 | 0 | 0 | 0 | 28 | 53.6% |
| Accessibility | 14 | 13 | 4 | 4 | 0 | 0 | 0 | 46 | 30.4% |
| AirPlay | 14 | 7 | 14 | 0 | 0 | 0 | 7 | 77 | 18.2% |
| LaunchServices | 14 | 13 | 0 | 0 | 3 | 5 | 0 | 58 | 24.1% |
| Mail | 13 | 13 | 0 | 0 | 0 | 0 | 0 | 36 | 36.1% |
| Apple Account | 12 | 10 | 0 | 0 | 0 | 0 | 0 | 25 | 48.0% |
| Foundation | 12 | 12 | 0 | 0 | 0 | 0 | 1 | 67 | 17.9% |
| Messages | 12 | 12 | 0 | 9 | 0 | 12 | 0 | 50 | 24.0% |
| Safari Private Browsing | 12 | 10 | 0 | 0 | 0 | 1 | 0 | 16 | 75.0% |
| CoreServices | 11 | 11 | 0 | 0 | 0 | 0 | 3 | 81 | 13.6% |
| mDNSResponder | 11 | 11 | 11 | 0 | 0 | 0 | 0 | 53 | 20.8% |
| APFS | 10 | 10 | 0 | 0 | 0 | 0 | 3 | 38 | 26.3% |
| Core Bluetooth | 10 | 10 | 5 | 0 | 0 | 0 | 1 | 16 | 62.5% |
| Model I/O | 10 | 10 | 0 | 0 | 0 | 0 | 0 | 88 | 11.4% |
| PackageKit | 10 | 9 | 0 | 0 | 0 | 1 | 0 | 106 | 9.4% |
| Spotlight | 10 | 10 | 0 | 0 | 0 | 0 | 0 | 69 | 14.5% |
| StorageKit | 10 | 10 | 0 | 0 | 0 | 0 | 0 | 59 | 16.9% |
| Wi-Fi | 10 | 10 | 10 | 0 | 0 | 0 | 0 | 58 | 17.2% |
| WindowServer | 10 | 10 | 0 | 0 | 0 | 0 | 1 | 32 | 31.2% |
| AppleKeyStore | 9 | 9 | 0 | 0 | 0 | 0 | 0 | 9 | 100.0% |
| CUPS | 9 | 9 | 3 | 0 | 0 | 0 | 0 | 43 | 20.9% |
| FaceTime | 9 | 9 | 0 | 0 | 0 | 0 | 0 | 14 | 64.3% |
| 802.1X | 8 | 8 | 8 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| AVEVideoEncoder | 8 | 8 | 0 | 0 | 8 | 0 | 0 | 45 | 17.8% |
| AppleMobileFileIntegrity | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 146 | 5.5% |
| Authentication Services | 8 | 8 | 4 | 0 | 0 | 0 | 0 | 18 | 44.4% |
| CoreGraphics | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 27 | 29.6% |
| CoreText | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 33 | 24.2% |
| Display | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| Notes | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 38 | 21.1% |
| SceneKit | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 112 | 7.1% |
| libxml2 | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 19 | 42.1% |
| ARKit | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 13 | 53.8% |
| Bluetooth | 7 | 7 | 7 | 0 | 0 | 0 | 0 | 41 | 17.1% |
| CoreMedia Video Toolbox | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| DiskArbitration | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 25 | 28.0% |
| Game Center | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 33 | 21.2% |
| MediaRemote | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 24 | 29.2% |
| afclip | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| libxpc | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 80 | 8.8% |
| libxslt | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 30 | 23.3% |
| AppleGraphicsControl | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 8 | 75.0% |
| CoreSymbolication | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| DeviceLink | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Managed Configuration | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 17 | 35.3% |
| NSDocument | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Networking | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 10 | 60.0% |
| Pro Res | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 21 | 28.6% |
| SMB | 6 | 6 | 6 | 0 | 0 | 0 | 0 | 58 | 10.3% |
| Spell Check | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Vision | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Web Extensions | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 14 | 42.9% |
| WebKit Storage | 6 | 6 | 6 | 0 | 0 | 0 | 0 | 11 | 54.5% |
| WebKit Web Inspector | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 16 | 37.5% |
| WebRTC | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 48 | 12.5% |
| XPC | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 7 | 85.7% |
| FileProvider | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 18 | 27.8% |
| IOSurface | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 9 | 55.6% |
| Music | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 10 | 50.0% |
| Notification Services | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| Photos | 5 | 4 | 0 | 0 | 0 | 0 | 0 | 19 | 26.3% |
| Quick Look | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 10 | 50.0% |
| Safari Downloads | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 7 | 71.4% |
| Screen Sharing Server | 5 | 5 | 2 | 0 | 0 | 0 | 0 | 16 | 31.2% |
| System Settings | 5 | 5 | 0 | 0 | 0 | 1 | 1 | 30 | 16.7% |
| WebKit Sandboxing | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| dyld | 5 | 5 | 0 | 5 | 0 | 0 | 0 | 15 | 33.3% |
| ATS | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 17 | 23.5% |
| Apple Neural Engine | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 36 | 11.1% |
| Calling Framework | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 9 | 44.4% |
| CloudKit | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 25 | 16.0% |
| Data Detectors UI | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| Finder | 4 | 4 | 0 | 0 | 2 | 0 | 0 | 10 | 40.0% |
| LoginWindow | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| Phone | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 10 | 40.0% |
| Sandbox | 4 | 4 | 0 | 0 | 0 | 0 | 4 | 65 | 6.2% |
| Wi-Fi Connectivity | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| ASP TCP | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 7 | 42.9% |
| AirDrop | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 14 | 21.4% |
| AppSandbox | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 10 | 30.0% |
| AppleEvents | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Assets | 3 | 3 | 0 | 0 | 0 | 0 | 3 | 16 | 18.8% |
| Automator | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 9 | 33.3% |
| Call History | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 13 | 23.1% |
| Compression | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 9 | 33.3% |
| Crash Reporter | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 24 | 12.5% |
| Disk Images | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 54 | 5.6% |
| Heimdal | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 19 | 15.8% |
| Login Window | 3 | 2 | 0 | 0 | 0 | 1 | 1 | 6 | 50.0% |
| NetFSFramework | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 9 | 33.3% |
| Photos Storage | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 18 | 16.7% |
| Printing | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 17 | 17.6% |
| Screen Sharing | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| SharedFileList | 3 | 3 | 0 | 0 | 0 | 3 | 0 | 39 | 7.7% |
| Time Zone | 3 | 3 | 0 | 0 | 0 | 3 | 0 | 12 | 25.0% |
| UserAccountUpdater | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 4 | 75.0% |
| Voice Control | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 16 | 18.8% |
| Watch App | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 4 | 75.0% |
| WebDAV | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 20 | 15.0% |
| WebKit Process Model | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 4 | 75.0% |
| macOS Recovery | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| ppp | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 10 | 30.0% |
| smbx | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 6 | 50.0% |
| udf | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 10 | 30.0% |
| xar | 3 | 3 | 0 | 0 | 0 | 3 | 0 | 3 | 100.0% |
| Apple Intelligence | 2 | 2 | 0 | 0 | 0 | 0 | 1 | 2 | 100.0% |
| AppleVA | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 7 | 28.6% |
| Calendar | 2 | 2 | 2 | 0 | 0 | 0 | 0 | 22 | 9.1% |
| CoreAnimation | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 5 | 40.0% |
| Handoff | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Live Captions | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Mail Drafts | 2 | 2 | 2 | 0 | 0 | 0 | 0 | 11 | 18.2% |
| MigrationKit | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 12 | 16.7% |
| Power Management | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 7 | 28.6% |
| Sandbox Profiles | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 23 | 8.7% |
| Screenshots | 2 | 2 | 0 | 0 | 0 | 2 | 0 | 7 | 28.6% |
| SecurityAgent | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 5 | 40.0% |
| Setup Assistant | 2 | 2 | 0 | 0 | 0 | 1 | 0 | 3 | 66.7% |
| Sidecar | 2 | 2 | 0 | 0 | 0 | 0 | 2 | 2 | 100.0% |
| Sync Services | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 12 | 16.7% |
| Telephony | 2 | 2 | 2 | 0 | 0 | 0 | 2 | 8 | 25.0% |
| Transparency | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 12 | 16.7% |
| Video Playback | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| VoiceOver | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 14 | 14.3% |
| WorkoutKit | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Admin Framework | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 12 | 8.3% |
| App Store Connect | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Apple TV Remote | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Archive Utility | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 8 | 12.5% |
| Baseband | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 7 | 14.3% |
| Cellular | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Control Center | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 8 | 12.5% |
| Disk Management | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| Face ID | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| GarageBand | 1 | 1 | 0 | 0 | 1 | 0 | 0 | 2 | 50.0% |
| Instruments | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Media Session | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| NSOpenPanel | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| Perl | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 8 | 12.5% |
| Presence | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| Security | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 91 | 1.1% |
| Status Bar | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 5 | 20.0% |
| TCC | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 29 | 3.4% |
| Terminal | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| UIKit | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 14 | 7.1% |
| User Management | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Wallet | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 1 | 100.0% |
| WidgetKit | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| Xcode | 1 | 1 | 0 | 0 | 0 | 1 | 1 | 3 | 33.3% |
| iCloud | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 11 | 9.1% |
| otool | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| **合计** | 1541 | 页列合计 1231 | 199 | 45 | 15 | 95 | 57 | 这 166 名 Impact 全集 5770 段（全树 7,434 的 77.6%） | — |

# 17 结论

1. **本篇主集 1,541 段／208 页／166 名**，占全树 9,136 段的 **16.87%**、Impact 全集 7,434 的 **20.73%**、所在 208 页全部 8,981 段的 **17.16%**——三套分母相差不到 4 个百分点，占比不是页面选择造成的。
2. **三把刀的体量差两个数量级**：刀一媒体本体 **219**、刀二解析格式 **247**、刀三资源管理 **1,105**。所谓"媒体漏洞的公开措辞"，实际是**资源管理措辞带出来的段落，外加不到三分之一的媒体与解析段**。
3. **每把刀内部 Impact 与 Description 的交集都是 0**，而三把刀并集之后出现 **17 段双侧**——这 17 段全部是"一刀管后果、另一刀管修复"的跨刀搭配（8＋9 段落在 CoreMedia 与 FaceTime）。**字段侧别是刀的属性，不是词的属性。**
4. **十六个词族在主集里的 I∩D 全部为 0**（第 6.2 节），本篇因此**没有任何"既描述后果又描述修复"的 amphibious 词族**——这是三条口径声明里最硬的一条。
5. **`manag*` 一个词族占主集 67.8%（1,045／1,541）**，其内部分解为 `state management` 662＋`memory management` 369＋其余 14，且 state 与 memory **从不共现**（交 0）。
6. **`improved` 是强制修饰语**：主集里 662 段 `state management` 全部写作 `improved state management`，369 段 `memory management` 全部写作 `improved memory management`。**短语出现＝套话出现。**
7. **同一句式的两个套话与 #133 关系相反**：`memory management` ∩ #133 ＝ **369／369（100%）**，`state management` ∩ #133 ＝ **47／662（7.1%）**。**跨报告引用必须按短语分别取交集。**
8. **词界断言第三次发作，但方向与前两篇相反**：去掉左侧 `(?<![\w-])` 会虚增 **434 段**（`information` 里的 `format` 为主），去掉右侧只增加 **0 段**——**本篇的边界风险全在词首，净收益 107∶1**。
9. **`container` 的 15 段全是假阳性、`format` 的 3 段全在 webpage 语境**，与 #132 的同名词族翻车同型；**中文侧「容器」23 次同样全是文件系统容器**——两侧在同一天空词上朝同一方向偏。
10. **句层极度模板化**：59 个 distinct 句扛 1,559 句次（模板负载 26.4），Top1 237 句次＝15.20%、Top10＝70.04%、Top20＝87.24%；**`state management` 与 `memory management` 两个桶就吃掉 66.1% 的句次**。
11. **情态与字段完全绑定**：Impact 373 句里 `may` 359／`could` 5／零情态 9，六个非标准情态词 **0 次**；Description 1,186 句 **一句情态都没有**。**可能性全部由影响句单独承担。**
12. **后果八类是标签不是分区**：三字段相加 834／并集 783（差 51）、Impact 相加 762／并集 742（差 20）；**799 段（51.8%）Impact 字段不带任何后果词**；≥2 类只有 **15 段、三种组合、全部含 ACE**；DOS 一类独占 **498 段（32.3%）**，FULL 只有 **7 段**。
13. **页面与组件两个维度都不能用来做样本框**：Top10 页全是同一模板的发布页（20.5% 的段），而"媒体类组件名"代理集只解释 **15.6%**；**精确名 `WebKit` 242 与子串名 WebKit* 290 差 48 段**。
14. **致谢字段必须只做哨兵**：全树 36 段"仅致谢命中"（在主集之外）里 **26 段是 `Manager of Cyber Security` 职务名、10 段是域 B 页面免责声明里的 `patches or releases`**；而附表 A 里 `releas*` 三字段 12 段与正文两字段 1 段相差 **11 段**——这 11 段全是域 B 每页 1 段的免责声明，其中 **10 段在主集外、1 段（页 120303）已因同段别的词进主集**；`manag*` 的 1,071 对 1,045 同样差的 **26 段全部在主集外**。
15. **同数不同集在本篇出现九处核验、无一例外**：四处同数（199∶199／342∶342／45∶45／29∶29）的交集是 28／46／0／0，三处真相等（229、5、102）可以互换，一处完全不相交（刀二∩TLO＝0）。**引用任何"某集与主集重合 N 段"的结论时，必须同时报出比较对象的成员集合，不能只报 N。**

---

**规则：** 本篇所有数字由 `_tmp/gb134.pl`（`_tmp/s134.pl` ＋ `_tmp/genbody2.pl`）在同一 `%B` 上产出，判据一律以词界与字段名写出，可用第 16 章十二步复算。分母一律注明为 **9,136**（全树段）、**7,434**（含 Impact 段）、**8,981**（本篇所在 208 页全部段）、**9,045 ∶ 91**（域 A ∶ 域 B）、**5,770**（附表 F 166 名的 Impact 全集）。**1,541 是两字段主集、1,577 是三字段并集（多的 36 段是致谢侧职务名与域 B 免责声明）、373 与 1,186 是句次、1,559 是句次合计、662 与 369 是两把套话刀的段级命中**，六者互不相等且不可互换：Impact 侧 372 段、仅 Description 侧 1,169 段、双侧 17 段（372＋1,169＋17－17 ＝ 1,541），**两字段零命中段 0**。六组闭合 197＋227＋1,087＋12＋10＋8＋0 ＝ 1,541 与 355＋1,169＋17 ＝ 1,541 各自成立；页九档 46＋15＋19＋18＋9＋47＋34＋19＋1 ＝ 208、段五档 250＋352＋410＋479＋50 ＝ 1,541、编号五档 3＋1,409＋71＋36＋22 ＝ 1,541、致谢七档 933＋356＋130＋61＋23＋17＋18 ＝ 1,538、句首十项 178＋146＋19＋13＋8＋5＋1＋1＋1＋1 ＝ 373。附表 A 的 ∩主集列是**三字段口径**、正文各词族表是**两字段口径**（`releas*` 因此是 12 对 1，差 11 段全部来自致谢与域 B 免责声明），附表 B／C／F 的页列与名列是**逐行去重后再相加**（**1,012／686／1,231**），三者均**不得当作并集去重值**（并集真值为 208 页／166 名，段级并集真值为 1,541）；附表 B 的 ∩E 行和 **46** 与真并集 **45** 差 1（一句所属段与另一句重复），附表 F 的 ∩R／∩E／∩M2／∩L／∩BP 行和 **199／45／15／95／57** 与真并集逐值相同（组件名是段的单值属性）。附表 A「后果强度八类」∩主集列是三字段口径（相加 **834**、并集 **783**、差 **51**），第 9.1 节另给 Impact 口径（**762／742／20**），两套数不可混用。第 15 章的九处成员级核验一律同时报甲集、乙集与三元交：**四处同数完全不相交或近不相交（199∶199 交 28、342∶342 交 46、45∶45 交 0、29∶29 交 0）、三处真包含或真相等（`刀二∩PMS`＝`pars*`＝229、`resource`＝`exhaust*`＝5、`media*`＝`crafted media file`＝102）、一处完全不相交（刀二∩TLO＝0）**。`use-after-free`（`$HY` 形）三字段 **193／∩主集 189**，`use after free`（空格形）**127／∩主集 127**，两集合**交 0、并 316**；去掉左侧 `(?<![\w-])` 后同一条 `$re3` 两字段从 1,541 涨到 **1,975（＋434）**，去掉右侧 `(?![\w-])` **＋0**——这是判据选择而不是事实矛盾，但引用 1,541 时必须并列这两个数。**主集一律用两字段口径，致谢只做哨兵**——致谢字段是人名与机构名词表：`manag*` 的 **26** 段仅致谢命中全部来自职务名 `Manager of Cyber Security`，`releas*` 的 **10** 段来自域 B 页面免责声明 `patches or releases are made available`，`analysis` 的 **94** 段里 **54** 段在主集外且**两字段命中 0**。**姊妹篇复刻值不等于正式主集**：本篇内复刻的 #132 刀是 **1,543／211／133**（#132 正式 **1,547／212／133**，差 4 段 1 页），#133 刀是 **2,791／206／154**（与正式逐值相同），跨篇引用一律以该篇附表为准。

**边界声明：** 本篇是**公开英文与中文文本的措辞统计**，不是脆弱性评价、不是可利用性研究，不提供任何规避方法、利用代码、样本、绕过链路或针对他人设备操作步骤与时间序列。文中"媒体数据""音视频流""解析""格式""容器""资源管理""状态管理""内存管理""缓存""释放""资源耗尽""未初始化""释放后使用""拒绝服务""信息泄露"一律指**公开条目使用了该词或该短语这一文本事实**，不推断任何条目真实的缺陷类别、CWE 归属、是否内存破坏、是否可被远程或本地触达、是否被利用、利用难度或修复完整度；**1,541 段、208 页、166 个组件名、59 个句子、418 个编号、1,559 句次、`CoreMedia` 103 段、`WebKit` 242 段** 只描述文本分布，不构成任何多媒体管线、解码器、内核内存管理器或浏览器引擎的安全性结论、缺陷优先级排序或可利用性评估。

Telegram: https://t.me/one00190
