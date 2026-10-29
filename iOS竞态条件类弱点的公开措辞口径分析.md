@@TITLE@@iOS 竞态条件类弱点的公开措辞口径分析
@@SUBTITLE@@选题是"竞态条件（race condition）"这一类弱点。逐词测量的结果是：**这一类在公开文本里只存在于 Description 字段，一处也不进后果字段**——`race condition` 连写在 **Impact 侧 0 段**、在 **Description 侧 232 段**、在**致谢侧 0 段**，三字段并集就是这 232 段，占全树 9,136 段的 **2.54%**、占含 Impact 的 7,434 段的 **3.12%**，覆盖 **95／257 页（37.0%）**与 **40／472 名（8.5%）**。更关键的是**机制词全部为零**：附表 A 的 15 项并发与线程词 `concurrent`／`concurrently`／`concurrency`／`thread 词族`／`thread 词界`／`multithread*`／`task`／`parallel`／`deadlock`／`mutex`／`atomic`／`serializ*`／`async`／`queue`／`simultaneous` **三字段一次都没出现**；`race window`、`race to race`、`time-of-check`／`TOCTOU`、`racing`、`raced`、`racy` 亦为零。**232 段的 Description 只有 6 种文本**，即 `A race condition was addressed with improved state handling.` **94** ∶ `… with additional validation.` **83** ∶ `… with improved locking.` **33** ∶ `… with improved state management.` **11** ∶ `… with improved handling of symbolic links.` **9** ∶ `… with improved checks.` **2** ＝ **232**（六句互斥、覆盖 232 段、重复计入 0）。同一段后果文本侧只有 **30 条 distinct 句**，其中 **11 条一个后果类都不命中、2 条命中 2 类**（`ACE`＋`SBX` 8 段、`BYP`＋`SBX` 6 段），**`MEM`（内存破坏）类命中 0 段**。中文手册层同样为空：**竞态、竞争、竞态条件、竞争条件、race、赛跑、抢占、争用 8 词在 178 页可用转储里 0 次**，`互斥`／`死锁`／`加锁`／`状态机`／`符号链接`／`硬链接`／`挂载`／`解包` 亦为 0，而 `锁` 的 944 次里有 **689 次属于"解锁／锁定／上锁"**——是设备锁屏语义，不是并发锁。
@@INFO@@报告日期|2026 年 10 月 29 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（258 份文件排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 9,132 段含致谢**。竞态条件集 RC ＝ Description 字段含 `race condition`（`race` 与 `condition` 之间只允许空格类字符）**＝ 232 段 / 95 页 / 40 名**，其中 Impact 侧 0 段、致谢侧 0 段。对照集：**R** ＝ #118 的远程触达集（Impact 侧 11 个通道词族）**843 段 / 178 页 / 72 名**；**WO** ＝ #119 的无前提集（Impact 含 `without` 词界）**178 段 / 97 页 / 37 名**；**E** ＝ #114 的在野集（Impact 含 `exploit` 词族）**108 段 / 76 页 / 12 名**；**G／HI** ＝ #116 与 #120 的动名词集 **2,179 段** 与交互触发集 **273 段 / 102 页 / 35 名**；**M2** ＝ #121 的同段 ≥2 后果类集 **185 段 / 82 页 / 35 名**；**U** ＝ Impact 含 `user`（词界）**1,627 段 / 175 页 / 200 名**；**L** ＝ #123 的逻辑缺陷集（`logic` 词界三字段）**550 段 / Impact 10 ∶ Description 540 ∶ 致谢 0**。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或利用链构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**"竞态条件"这一类型词在公开英文文本的哪个字段、有哪几种句式、与后果字段的八类强度词、与在野标注、与并发机制词汇之间的集合关系**，属于措辞与文本计量分析；文中任何"竞态"表述均指**公开条目把缺陷自述为 race condition 这一文本事实**，不是对该缺陷真实触发机制、可利用窗口宽窄或并发实现细节的判断，也不构成任何组件存在可竞态代码路径的结论
@@INFO@@分类|判据**电池 72 项 / 三字段全零 28 项 / 三字段并集非零 44 项 / Impact 侧非零 25 项**（附表 A，六组：竞态本体词 15、并发与线程词 15、锁与状态词 11、验证与句柄词 12、修复套话与来源词 11、后果强度八类 8）/ **六条修复句 × 段页名 × ∩R ∩WO ∩M2 ∩HI × 八类**（附表 B，7 行含合计，段合计 **232**、覆盖 **232**、重复 **0**）/ **30 条 distinct Impact 段全文 × 后果类签名 × 主修复句**（附表 C，31 行含合计，段合计 **232**、0 类 11 ∶ 1 类 17 ∶ 2 类 2）/ **页面、编号与致谢四组分箱**（附表 D，37 行，页分箱闭合 95、编号分箱闭合 232、致谢分箱闭合 232）/ **中文层 68 词**（附表 E，七组：竞态本体 8、时序与窗口 9、并发与线程 10、锁与原子 10、状态与处理 9、验证与链接 11、缺陷与后果 11；**全零 24 词、非零 44 词**）/ **组件层 40 名**（第 12 章正文表，段合计 232、这 40 名的 Impact 全集 **2,954** 段、总占比 **7.9%**）/ **七把刀**：字段刀（Impact 0 ∶ Description 232 ∶ 致谢 0）、词族刀（`condition` 词族 ≡ `race condition`，双向差 0）、机制刀（15 项并发词全零）、六句刀（94＋83＋33＋11＋9＋2 ＝ 232）、类型词刀（与 `memory corruption`／`out-of-bounds`／`use after free`／`type confusion`／`bounds` 五集**全部零交**）、在野刀（∩E ＝ 0，60 个编号 60 个独占）、邻词刀（`lock`／`locked`／`unlock` 与 RC 零交，`locking` 60 段里 33 段是竞态）
@@INFO@@难度|进阶（第 3 章的 **232 段有三套分母**：占全树 9,136 段是 **2.54%**、占含 Impact 的 7,434 段是 **3.12%**、占其所在 95 页全部 6,548 段是 **3.5%**，三个数都真，混用会互相矛盾；第 6 章的附表 B **页列合计 158 大于 95 页、名列合计 44 大于 40 名**，因为同页可有多条不同修复句、同名组件可拆到多句，页列与名列**不可当作并集去重值**；第 8 章的后果类列合计 **149 是"类次"不是"段次"**（0 类 97＋1 类 121＋2 类 14 ＝ 232 段，类次 ＝ 121×1＋14×2 ＝ 149），把 149 读成 149 段会虚增 83 段；第 7 章的 `improved state management` **662 段里只有 11 段是竞态**，说明修复短语与缺陷类型之间是**多对多**而不是同名；第 10 章的五类内存词与 RC 零交是**字段方向不同的结果**：内存词写在 Impact，`race condition` 写在 Description，零交不能读成"竞态不会造成内存破坏"；第 16 章的 `logic` 词界三字段 **550 段**与 RC **零交**，是"同一字段层两套类型词互斥"的实测，不是推定；第 18 章中文层 `锁` 的 944 次必须先扣除 `解锁`＋`锁定`＋`上锁` 的 **689 次**才能谈并发，否则会把锁屏文档读成并发文档）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

**"竞态条件"是一类被反复命名的弱点，但它在公开文本里出现在哪一层，是可以直接测的。** 本篇把这句选题拆成四个互相独立的断言，逐个测量：

**断言一（存在这一类）**：公开条目里确实有将缺陷自述为 `race condition` 的文本。**测法**：三字段分别匹配。**结果**：成立，但**只在 Description**——Impact 侧 **0**、Description 侧 **232**、致谢侧 **0**，三字段并集 **232**（第 3 章）。

**断言二（这一类被机制化描述）**：文本会说明并发、线程、锁、时序窗口等机制语境。**测法**：附表 A 组 2 的 15 项并发与线程词 ＋ 组 1 的窗口与时序词。**结果**：**15 项三字段全零**，`race window`／`time-of-check`／`TOCTOU` 亦全零；唯一非零的时间词是 `timing`（**7 段**）与 `side channel`（**7 段**），而这 7 段是同一句 CoreCrypto 侧信道条目，**与 RC 零交**（第 5、17 章）。

**断言三（这一类有可识别的后果）**：后果字段会为竞态写出可分类的影响。**测法**：Impact 侧 distinct 句 ＋ 八类共现。**结果**：232 段的后果文本共 **30 条 distinct 句**，**0 类 11 条／1 类 17 条／2 类 2 条**，`MEM` 类命中 **0**（第 8 章、附表 C）。

**断言四（这一类独立成类）**：竞态在措辞层与内存破坏类缺陷可分。**测法**：与五套类型词集合做真交集。**结果**：`memory corruption`（390 段）、`out-of-bounds`（816）、`use after free`（127）、`type confusion`（145）、`bounds`（788）与 RC 的交集**全部为 0**；`logic` 词界（550）与 RC 交集也为 **0**（第 10、16 章）。

**所以本篇的产出是一条"命名与机制分离"的结论**：公开文本**给竞态一个类型名（race condition），但不给它机制词汇、也不给它专用后果**；能承载机制语义的位置被六个修复短语接管，而这六个短语有四个同时服务别的类型（第 7 章）。第 20 章给复算，第 22 章纠正会把 232 读成"232 条已确认并发缺陷"的七种误读。

# 1 操作性定义与分母

**1.1 语料与切块。** 与 #114–#121 同一镜像、同一切块口径：块起于 `<h3 class="gb-header">`（域 A）或粗体段（域 B），块止于**四锚点**（下一块起点、页脚声明、版权行、脚本行），缺一锚点就会把页面免责声明切进字段文本。全树 **9,136 段 / 257 页 / 472 名**；含 Impact **7,434 段**；含致谢 **9,132 段**。域 A **9,045 段 / 246 页 / 470 名**，域 B **91 段 / 11 页 / 26 名**。

**1.2 RC 集（本篇主集）。** 判据：Description 字段匹配 `race condition`，两词之间允许普通空格、不换行空格（U+00A0）、窄不换行空格（U+202F）。**RC ＝ 232 段 / 95 页 / 40 名**。同一判据在 Impact 侧匹配 **0 段**、致谢侧 **0 段**，所以本篇所有"RC 段的后果"都是**借来**的：同一段的 Impact 字段是独立文本，字段里没有 `race`。

**1.3 后果强度八类（与 #121 同判据，只在 Impact 字段匹配）。**

| 代号 | 判据 | 全树段 | RC 命中 | RC 占该类 |
|---|---|---:|---:|---:|
| `ACE` | `arbitrary code execution`／`execute arbitrary code` | 336 | 8 | 2.4% |
| `BYP` | `bypass`（词界） | 291 | 18 | 6.2% |
| `DIS` | `disclos*`／`leak*`／`expos*` | 530 | 11 | 2.1% |
| `DOS` | `denial-of-service`／`unexpected system／app termination`／`terminat*` | 2,003 | 74 | 3.7% |
| `FULL` | `arbitrary file(s)`／`any action`／`full control` | 63 | 6 | 9.5% |
| `MEM` | `memory corruption`／`corrupt memory` | 220 | **0** | 0.0% |
| `PRI` | `elevat* privileges`／`privileges escalat*` | 124 | 3 | 2.4% |
| `SBX` | `sandbox`（词界） | 418 | 29 | 6.9% |

类次合计 **8＋18＋11＋74＋6＋0＋3＋29 ＝ 149**，与第 8 章的段级分布（0 类 97＋1 类 121＋2 类 14 ＝ 232，类次 121＋28 ＝ **149**）互为校验。

**1.4 三套分母。** 同一句"竞态条目占多少"有三个答案：**2.54%**（÷9,136 全树段）、**3.12%**（÷7,434 含 Impact 段）、**3.5%**（÷232 段所在 95 页的全部 6,548 段）。本篇每次出现百分号都写清用的是哪一套；第 3 章另有第四套（÷9,136 段的 Description 全集 7,434，也是 3.12%，因为全树每一段的 Description 都非空——见第 11 章自检）。

# 2 结论速览（每条都可按第 20 章复算）

**一、类型名只活在 Description。** `race condition` 在 Impact 侧 **0** 段、Description 侧 **232** 段、致谢侧 **0** 段。**没有一段公开后果文本写到"竞态"这件事**；后果字段读到的是 `An app may be able to cause unexpected system termination` 这类句子，看不出缺陷类型。

**二、`condition` 词族恰好等于竞态。** 三字段并集里 `condition` 词族（含 `conditions`／`conditioned`）＝ **232 段**，与 `race condition` 集**双向差 0**（附表 A 第 3、4 行与 RC 列）。也就是说全树没有别的 `condition` 用法（`conditional`、`air conditioning` 之类一次都没有），"条件"这个词在顾问页语料里被竞态独占。

**三、并发机制词汇整层为零。** 附表 A 组 2 的 **15 项全零**：`concurrent`、`concurrently`、`concurrency`、`thread` 词族、`thread` 词界、`multithread*`、`task`、`parallel`、`deadlock`、`mutex`、`atomic`、`serializ*`、`async`／`asynchronous`、`queue`、`simultaneous`。竞态条目的**机制解释在公开文本里不存在**，只留一个类型名。

**四、Description 只有 6 种文本。** 232 段的 Description 段全文 distinct ＝ **6**，即六句 `A race condition was addressed with …`（94／83／33／11／9／2）。**每段恰好 1 句**、句首词 distinct ＝ **1**（全部是 `A`），六句互斥且闭合到 232，重复计入 **0**（附表 B）。

**五、修复短语不是竞态专用。** `improved state handling` 全树 **109** 段，其中竞态 **94** 段、类型混淆 **12** 段、一致性问题 **3** 段；`improved locking` 全树 **60** 段 = 竞态 **33** ＋ 内存破坏 **27**；`additional validation` 全树 **112** 段 = 竞态 **83** ＋ 信息泄露 **29**；而 `improved state management` 全树 **662** 段里竞态只有 **11** 段、`improved checks` 全树 **1,018** 段里竞态只有 **2** 段。只有 `improved handling of symbolic links`（9 段）是竞态独占（第 7 章）。

**六、与五套内存类词零交。** `memory corruption` 390、`out-of-bounds` 816、`use after free` 127、`type confusion` 145、`bounds` 788 段——与 RC 的交集**分别为 0、0、0、0、0**（第 10 章）。同时 `MEM` 后果类在 RC 的 232 段里命中 **0** 段。

**七、与在野集零交，编号 60 个独占。** `exploit` 词族 **108** 段与 RC 交集 **0**；232 段共出现编号 **249** 次、distinct **60** 个，而这 60 个编号**全部只出现在竞态段**（也出现在非竞态段的编号数 ＝ **0**）；全树 distinct 编号 **2,108** 个，竞态独占 **2.8%**（第 9、14 章）。

**八、中文手册层没有这一类。** 附表 E 竞态本体组 8 词（竞态、竞争、竞态条件、竞争条件、race、赛跑、抢占、争用）**在 178 页可用转储里 0 次**；`互斥`、`死锁`、`加锁`、`状态机`、`符号链接`、`软链接`、`硬链接`、`挂载`、`解包`、`提权`、`弱点`、`沙箱` 亦 0 次。`锁` 944 次里 **689 次**是 `解锁`／`锁定`／`上锁`（第 18 章）。

# 3 字段刀：Impact 0 ∶ Description 232 ∶ 致谢 0

**3.1 测量。** 对同一判据 `race${S}condition` 分别在三个字段匹配：Impact **0** 段、Description **232** 段、致谢 **0** 段；三字段并集 **232** 段。附表 A 首行给出该判据的页/名列同为 **0/0**，因为它在 Impact 侧无命中。

**3.2 为什么这条不是废话。** Apple 顾问页的字段分工是固定的：**Description 写"问题是什么、怎么修的"，Impact 写"能做到什么"，Acknowledgements 写"谁报告的"**。类型词只落在 Description，意味着：（a）**任何人只看后果字段，无法把 232 段中的任何一段识别为竞态**；（b）反过来，本篇第 8 章列的 30 条 distinct 后果句与竞态**没有语义绑定**——同一句 `An app may be able to access sensitive user data` 在全树被大量非竞态条目共用。

**3.3 与 #121 的对照。** #121 的 M2 集**只能在 Impact 侧定义**（它判的是后果类共现），所以两个集天然不在同一字段上；本篇 232 段中有 **232** 段带非空 Impact（第 11 章自检），因此 M2∩RC ＝ **14** 段是**跨字段**交集：这 14 段的 Description 说竞态、Impact 同段并写两类后果。这与 #121 里"并写只发生在后果字段内部"是同一件事的另一侧观测。

**3.4 致谢侧 0 段的含义。** 致谢字段只放人名与"（externally reported）"类标注，从不放类型词；`internally reported`、`or internally`、`entries in Apple` 三串在 Description 与 Impact 侧都是 **0 段**（第 14 章），所以竞态条目的来源标注形式在这套语料里**只有一种**：直接列报告人。

# 4 词族刀：`condition` 词族 ≡ `race condition`

**4.1 双向零差。** 附表 A 竞态本体组四行：`race condition（连写）` 并集 **232**、`race 词界`（`race|races`）**232**、`condition 词界`（`condition|conditions`）**232**、`condition 词族**（`(?<!\w-)condition`，含 `conditions`/`conditioned`）**232**。四行与 RC 的 ∩ 列都是 **232**。补测的差集：RC − `condition` 词族 ＝ **0**，`condition` 词族 − RC ＝ **0**。

**4.2 结论。** 在这套 9,136 段的语料里，**"condition"这个词只为竞态服务**。这给出一条可用的简化判据：任何字段里出现 `condition`，该段必是竞态条目；反之亦然。这是本篇所有交叉表能用**单判据**收口的原因。

**4.3 词界与子串的差别要不要紧。** 要紧但不改变结论：把 `race` 放宽为子串（允许 `raced`、`races`、`racing`、`embrace` 误入）后并集仍是 **232**（附表 A 组 5 末行 `race 子串（三字段并集）`），说明**语料里没有以 race 为子串的其他词**（`grading`、`trace`、`embrace` 一类全部为 0 段）。而词界形式 `racing`、`raced`、`racy` 各自 **0**（组 1 第 5–7 行）——公开文本只用**名词短语**，不用动词或形容词形式。

# 5 机制刀：15 项并发与线程词三字段全零

**5.1 清单与结果。** 附表 A 组 2 的 15 项，Impact／Description／致谢三字段并集全为 **0 段**：`concurrent` 词族、`concurrently`、`concurrency`、`thread` 词族、`thread` 词界、`multithread*`、`task`（词界）、`parallel` 词族、`deadlock`、`mutex`、`atomic`／`atomically`、`serializ*`、`async`／`asynchronous`、`queue`、`simultaneous`。

**5.2 这条负结果的读法边界。** 它说明的是**公开文本不提并发机制**，不是"这些缺陷与并发无关"。九字之差的差别在于：前者可用语料证伪（本篇已证），后者需要代码与复现，公开文本不提供。第 22 章把这条列为第一号误读。

**5.3 邻近机制词也在同一位置为零。** `race window` **0**、`race to race` **0**、`time-of-check`／`time-of-consistency`／`TOCTOU` **0**、`synchroniz*` **0**、`status` **0**、`handle`（词界）**0**、`lockwise` **0**（附表 A 组 1、组 3、组 4、组 5）。竞态条目在措辞层被压缩成**类型名 ＋ 修复短语**两段文本，中间没有机制描述位。

**5.4 唯一非零的近邻是"锁族"，但它不在竞态里。** `lock` 词界 **78** 段（Impact 62）、`locked` **100** 段（Impact 72）、`unlock` **14** 段（Impact 14），三者与 RC 交集均为 **0**；只有 `locking`（**60** 段，Impact 侧 0）与 RC 交集 **33**，而这 33 段恰好是第 7 章 `improved locking` 的 33 段竞态条目（同一集合，逐成员验证）。**"锁"作为名词出现在别处（锁屏、解锁），作为动名词只出现在修复短语里。**

# 6 六句刀：94＋83＋33＋11＋9＋2 ＝ 232，互斥且闭合

**6.1 六句与段数。** 附表 B 按 Description 全文尾句分箱：

| Description 全文 | 段 | 占 232 |
|---|---:|---:|
| `A race condition was addressed with improved state handling.` | 94 | 40.5% |
| `A race condition was addressed with additional validation.` | 83 | 35.8% |
| `A race condition was addressed with improved locking.` | 33 | 14.2% |
| `A race condition was addressed with improved state management.` | 11 | 4.7% |
| `A race condition was addressed with improved handling of symbolic links.` | 9 | 3.9% |
| `A race condition was addressed with improved checks.` | 2 | 0.9% |
| **合计** | **232** | **100%** |

**6.2 三条闭合断言。**（a）**六句合计 232 ＝ RC 段数**；（b）**六句覆盖 232 段、重复计入 0**，即六句两两互斥且并集等于 RC；（c）RC 段的 Description **distinct 段全文 ＝ 6**，且**每段恰好只有 1 句**（232/232），句首词 distinct ＝ **1**（全部 `A`）。这三条一起说明：**竞态条目的 Description 是模板槽位，不是自由文本**。

**6.3 六句的分布偏斜有意义。** 前两句占 **177/232 = 76.3%**，后四句合计只有 **55 段（23.7%）**。`improved checks` 只有 **2 段**——而它在全树是**最大通用套话之一（1,018 段）**，说明"检查"这个修复方式**很少被归因给竞态**（第 7 章给反向比值）。

**6.4 六句页列与名列不能相加去重。** 附表 B 的页列合计 **158**、名列合计 **44**，而 RC 真值是 **95 页 / 40 名**：同一页面可以出现多种修复句、同一组件名可以跨句，所以页/名列是**逐句去重后的再求和**，不是集合基数。第 22 章误读三号针对此。

# 7 修复短语反向刀：同一短语被几种类型共用

**7.1 正向比值（竞态占该短语）。**

| 修复短语（全树 Description 段数） | 全树 | 其中竞态 | 竞态占 |
|---|---:|---:|---:|
| `improved state handling` | 109 | 94 | 86.2% |
| `additional validation` | 112 | 83 | 74.1% |
| `improved locking` | 60 | 33 | 55.0% |
| `improved state management` | 662 | 11 | **1.7%** |
| `improved handling of symbolic links` | 9 | 9 | **100%** |
| `improved checks` | 1,018 | 2 | **0.2%** |

**7.2 非竞态部分是什么（逐段读出）。** `improved state handling` 的 15 个非竞态段：**12 段**是 `A type confusion issue was addressed with improved state handling.`，**3 段**是 `A consistency issue was addressed with improved state handling.`。`improved locking` 的 27 个非竞态段全部是同一句 `A memory corruption vulnerability was addressed with improved locking.`。`additional validation` 的 29 个非竞态段全部是 `An information leakage was addressed with additional validation.`。三张分解都**闭合**（94＋12＋3 ＝ 109；33＋27 ＝ 60；83＋29 ＝ 112）。

**7.3 所以修复短语与类型是多对多。** `improved locking` 同时被竞态（33 段）与内存破坏（27 段）使用；`improved state management` 主要服务别的类型（662 段里 11 段竞态）。**用修复短语反推缺陷类型在本语料里不成立**——唯一例外是 `improved handling of symbolic links`，9 段全是竞态，是全树唯一一条"短语 ⇒ 类型"的正向蕴含。

**7.4 套话总量层。** `was addressed with` 在三字段并集 **6,581** 段（Impact 侧仅 4 段），`addressed` 词界 **7,228** 段（Impact 25），`improved` 词界 **6,038** 段（Impact 4）；RC 的 232 段**全部**含 `was addressed with`（∩RC ＝ 232）。**`fixed`、`resolved` 作为词界在 Description 侧分别是 0 段与 19 段**，`added a missing check` **0 段**——修复动词在这套文本里被 `addressed` 单一化。

# 8 后果刀：30 条 distinct 句，0 类 11 ∶ 1 类 17 ∶ 2 类 2

**8.1 段级分布。** 对 RC 的 232 段逐段统计 Impact 命中的后果类数：**0 类 97 段（41.8%）、1 类 121 段（52.2%）、2 类 14 段（6.0%）**，合计 **232**（附表 C 合计行与第 1 章类次 149 互校）。**没有一段命中 3 类。**

**8.2 句级分布。** 232 段的 Impact 全文 distinct ＝ **30 条**（附表 C）：**0 类 11 条、1 类 17 条、2 类 2 条**；页列合计 **203**、名列合计 **53**（同样不可去重当基数用）。

**8.3 三条最大的句子。** `An app may be able to cause unexpected system termination` **43 段**（`DOS` 类）；`An app may be able to access sensitive user data` **29 段**（**0 类**——八类判据都不命中，因为 `access` 不属于任何一类）；`An app may be able to gain root privileges` **20 段**（同为 **0 类**，`privileges` 出现但没有 `elevat*`／`escalat*` 搭配，故不满足 `PRI` 判据）。**"取得 root 权限"在本篇八类刻度下不计入任何类**，这是判据定义的结果，第 22 章误读四号针对此。

**8.4 两条 2 类句。** `A sandboxed app may be able to execute arbitrary code with kernel privileges` **8 段**（`ACE`＋`SBX`）与 `A shortcut may be able to bypass sandbox restrictions` **6 段**（`BYP`＋`SBX`）——这 14 段就是 M2∩RC ＝ **14** 的全部内容。其中 8 段的修复句是 `improved state management`、6 段是 `improved handling of symbolic links`（附表 C 主修复句列），即**两句小样本修复短语承担了全部多类并写**。

**8.5 `MEM` 为 0 段。** 232 段里没有一段 Impact 写 `memory corruption`／`corrupt memory`。最接近的是 `… unexpected system termination or corrupt kernel memory`（**15 段**）与 `… or read kernel memory`（**5 段**）、`… or write kernel memory`（**5 段**）——它们命中 `DOS` 而**不**命中 `MEM`，因为 `MEM` 判据要求 `memory corruption`／`corrupt memory` 这一搭配。第 10 章说明这与五套内存类词的零交是同一件事的不同口径。

# 9 在野刀：∩E ＝ 0，60 个编号 60 个独占

**9.1 在野集零交。** #114 的 `exploit` 词族集（Impact 含 `exploit*`）**108 段**与 RC 的交集为 **0**。**在这套语料里，没有任何一段把竞态条件写成已被在野利用**；反过来，108 段在野条目里也没有一段的 Description 写 `race condition`。

**9.2 编号独占。** RC 的 232 段全部含 CVE 编号，共出现 **249 次**、distinct **60** 个；同段含 ≥2 个编号的只有 **8** 段（1 段含 2 个、5 段含 3 个、2 段含 ≥4 个，附表 D 编号分箱 **224∶1∶5∶2 ＝ 232** 闭合）。这 60 个编号中，**60 个只出现在竞态段、0 个也出现在其他段**（"也出现在非 RC 段"一栏计数为 **0**）。全树 distinct 编号 **2,108** 个，竞态独占 **60/2,108 ＝ 2.85%**。

**9.3 为什么这条比 #121 的编号结论更强。** #121 的 M2 集里含编号段为 **0**（因为编号根本不在 Impact 侧）；本篇的 232 段**跨字段**含编号（编号写在致谢字段），所以可以做**归属**判断：**竞态条目的编号集合与其他类型完全不相交**。这把"竞态是独立一类"从措辞层（第 10 章零交）推到编号层（60 个独占）。

# 10 类型词零交刀：与五套内存类词的关系

**10.1 五个集合与交集。** 判据一律为三字段并集（Impact／Description／致谢），词界或连写按下列字面：

| 类型词集 | 段数 | ∩RC | 备注 |
|---|---:|---:|---|
| `memory corruption`／`corrupt memory` | 390 | **0** | 其中 Impact 侧 220 段 |
| `out-of-bounds` | 816 | **0** | — |
| `use after free` | 127 | **0** | — |
| `type confusion` | 145 | **0** | — |
| `bounds`（词界） | 788 | **0** | 与 `improved bounds checking` 699 段不同判据 |
| `logic`（词界，#123 判据） | 550 | **0** | Impact 10 ∶ Description 540 ∶ 致谢 0 |

**10.2 这五条零交的口径边界。** 它们说明**同一文本条目不会同时自述两种类型名**——在 Description 侧，一段只写一个类型（第 6 章的模板化结构是原因）。它**不**说明"竞态不会导致内存破坏"：`improved locking` 的 27 个非竞态段正是内存破坏条目（第 7.2 节），二者共用修复短语，说明文本层面已经承认机制相邻。**零交是关于句子的，不是关于缺陷的。**

**10.3 与内存词的距离可以量化。** `improved memory handling`（862 段）、`improved bounds checking`（699）、`improved input validation`（641）三个通用套话与 RC 交集均为 **0**；`out-of-bounds` 816 段亦 **0**。而 `bounds` 与 `out-of-bounds` 的差集（788 − 816，交叉部分）**不影响**竞态结论，因为两者与 RC 都是 0。

**10.4 唯一非零的类型近邻是 `type confusion` 的短语侧。** 12 段 `A type confusion issue was addressed with improved state handling.` 与竞态的 94 段共用短语但不共用条目（并集 106 段，交集 0 段）。这是"命名互斥、修复同源"的最干净一例。

# 11 镜像与切块自检

**11.1 锚点断言。** 本篇生成器 `_tmp/tw122tab.pl` 开头即断言七组外部集：`R=843`、`WO=178`、`user=1,627`、`G=2,179`、`HI=273`、`M2=185`、`E=108`，以及 `RC=232`、全树 `9,136` 段、含 Impact `7,434` 段。**任一条不满足脚本直接 die**，附表不会产出。

**11.2 五处闭合。** 附表 B 六句合计＝232 且覆盖段＝232 且重复＝0；附表 C 30 条 distinct 句段合计＝232；附表 D 页分箱合计＝**95**、编号分箱合计＝**232**、致谢人数分箱合计＝**232**；第 12 章组件表段合计＝**232**；第 1 章类次 **149** ＝ 附表 B 八列之和 ＝ 0 类 97＋1 类 121＋2 类 14 的类次。

**11.3 版式域。** RC 在**域 B 的 91 段里 0 段**（附表 D"版式域"行），即粗体版式的 11 页页面完全没有竞态条目；对照第 17 章：域 B 页面里出现的类型词是 `timing side-channel`（CoreCrypto）。这说明**类型词的字段落位在两版式间不通用**，用统一口径读两域会漏。

**11.4 Description 全集。** 全树 **7,434 段有非空 Description**，与含 Impact 的段数**相等**（第 1.4 节），所以"RC 占 Description 全集 3.12%"与"RC 占含 Impact 段 3.12%"是同一个分母的两个名字，不是巧合，也不能拆成两个百分比来引用。

# 12 组件层：40 名／472 名，Impact 全集 2,954 段的 7.9%

**12.1 表。** 下表按 RC 段数降序（∩R／∩WO／∩M2 为该组件竞态段与三个外部集的交段数；"该组件含 Impact 全集"是**同组件在 Impact 侧的全部段数**，用于算占比）：

| 组件名 | RC 段 | 页 | ∩R | ∩WO | ∩M2 | 该组件含 Impact 全集 | RC 占比 |
|---|---:|---:|---:|---:|---:|---:|---:|
| `Kernel` | 71 | 45 | 11 | 0 | 0 | 681 | 10.4% |
| `StorageKit` | 13 | 10 | 0 | 0 | 0 | 59 | 22.0% |
| `AVEVideoEncoder` | 8 | 8 | 0 | 0 | 8 | 45 | 17.8% |
| `Graphics` | 8 | 8 | 0 | 0 | 0 | 8 | 100.0% |
| `Storage` | 8 | 8 | 0 | 0 | 0 | 20 | 40.0% |
| `CoreServices` | 7 | 7 | 0 | 0 | 0 | 81 | 8.6% |
| `WebKit` | 7 | 7 | 0 | 0 | 0 | 910 | 0.8% |
| `Compression` | 6 | 6 | 0 | 0 | 0 | 9 | 66.7% |
| `Disk Images` | 6 | 6 | 0 | 0 | 0 | 54 | 11.1% |
| `FileProvider` | 6 | 6 | 0 | 0 | 0 | 18 | 33.3% |
| `IOGPUFamily` | 6 | 6 | 0 | 0 | 0 | 17 | 35.3% |
| `Messages` | 6 | 6 | 0 | 0 | 6 | 50 | 12.0% |
| `NetAuth` | 6 | 6 | 0 | 0 | 0 | 15 | 40.0% |
| `SMB` | 6 | 6 | 3 | 0 | 0 | 58 | 10.3% |
| `IOKit` | 5 | 5 | 0 | 0 | 0 | 26 | 19.2% |
| `dyld` | 5 | 5 | 0 | 0 | 0 | 15 | 33.3% |
| `LaunchServices` | 4 | 4 | 0 | 0 | 0 | 58 | 6.9% |
| `Sandbox` | 4 | 4 | 0 | 0 | 0 | 65 | 6.2% |
| `AMD` | 3 | 3 | 0 | 0 | 0 | 8 | 37.5% |
| `Apple Account` | 3 | 3 | 0 | 0 | 0 | 25 | 12.0% |
| `CUPS` | 3 | 3 | 0 | 0 | 0 | 43 | 7.0% |
| `Core Services` | 3 | 3 | 0 | 0 | 0 | 5 | 60.0% |
| `CoreMedia` | 3 | 3 | 0 | 0 | 0 | 150 | 2.0% |
| `Dock` | 3 | 3 | 0 | 0 | 0 | 19 | 15.8% |
| `GPU Drivers` | 3 | 3 | 0 | 0 | 0 | 34 | 8.8% |
| `Multi-Touch` | 3 | 3 | 0 | 0 | 0 | 15 | 20.0% |
| `NetFSFramework` | 3 | 3 | 0 | 0 | 0 | 9 | 33.3% |
| `PackageKit` | 3 | 3 | 0 | 0 | 0 | 106 | 2.8% |
| `Printing` | 3 | 3 | 0 | 0 | 0 | 17 | 17.6% |
| `Sync Services` | 3 | 3 | 0 | 3 | 0 | 12 | 25.0% |
| `Books` | 2 | 2 | 0 | 0 | 0 | 4 | 50.0% |
| `Crash Reporter` | 2 | 2 | 0 | 0 | 0 | 24 | 8.3% |
| `Foundation` | 2 | 2 | 0 | 0 | 0 | 67 | 3.0% |
| `SecurityAgent` | 2 | 2 | 0 | 0 | 0 | 5 | 40.0% |
| `Archive Utility` | 1 | 1 | 0 | 0 | 0 | 8 | 12.5% |
| `DesktopServices` | 1 | 1 | 0 | 0 | 0 | 11 | 9.1% |
| `Heimdal` | 1 | 1 | 0 | 0 | 0 | 19 | 5.3% |
| `Safari` | 1 | 1 | 0 | 0 | 0 | 92 | 1.1% |
| `Security` | 1 | 1 | 0 | 0 | 0 | 91 | 1.1% |
| `Virtualization` | 1 | 1 | 0 | 0 | 0 | 1 | 100.0% |
| **合计** | 232 | 页列合计 203 | 14 | 3 | 14 | 2954 | 7.9% |

**12.2 集中度。** `Kernel` **71 段（占 RC 的 30.6%）**，覆盖 **45 页**；它的 71 段分解为 `improved state handling` **27** ＋ `additional validation` **26** ＋ `improved locking` **18** ＝ **71**（闭合），distinct 修复句只有 **3** 种。前 5 名（Kernel、StorageKit、AVEVideoEncoder、Graphics、Storage）合计 **108 段 = 46.6%**。

**12.3 两个 100% 组件。** `Graphics` **8/8** 与 `Virtualization` **1/1**：这两个组件名在全树**所有含 Impact 的段**都是竞态条目。`Virtualization` 的分母只有 1 段，不构成显著性；`Graphics` 的 8 段全在同 8 页，是真实的全命中。次高的是 `Compression` **6/9 ＝ 66.7%**、`Core Services` **3/5 ＝ 60.0%**、`Books` **2/4 ＝ 50.0%**。

**12.4 与 WebKit 的对照。** `WebKit` 全树含 Impact **910 段**居全组件之首，竞态只 **7 段（0.8%）**；`Safari` 92 段里 1 段（1.1%）、`Security` 91 段里 1 段（1.1%）、`CoreMedia` 150 段里 3 段（2.0%）。**竞态条目集中在内核与存储/压缩/文件系统类组件，不集中在解析类组件**——与 #112 的"外部输入触达面"分布形状相反（该篇 WebKit 占比最高）。

**12.5 交集按组件落地。** ∩R ＝ 14 全部落在 `Kernel` **11** ＋ `SMB` **3**（网络通道语义）；∩WO ＝ 3 全部落在 `Sync Services`；∩M2 ＝ 14 全部落在 `AVEVideoEncoder` **8** ＋ `Messages` **6**（即第 8.4 节的两条 2 类句）。三组都按成员逐一验证，不是比例推定。

# 13 页与分布：95／257 页，Top10 页承载 31.0%

**13.1 页分箱。** 含竞态条目的页面 **95** 张（全树 257 页，**37.0%**）。按每页竞态段数分箱：**1 段 41 页、2 段 26 页、3 段 7 页、4 段 9 页、5 段 4 页、6–9 段 6 页、10–19 段 2 页**，合计 **95**（附表 D 第一块闭合）。**没有一页全是竞态**：95 页全部还有非竞态段，这 95 页共 **6,548** 段，竞态 **232 段 = 3.5%**。

**13.2 Top10。** 页面 ID **149035（11 段）**、**128067（10）**、**149042（8）**、**128071（7）**、**128072（7）**、**149043（7）**、**126794（6）**、**149034（6）**、**149036（5）**、**149037（5）** 合计 **72 段 = 31.0%**。前十页里 **149xxx 系列占 6 张**、**128xxx 系列占 3 张**，页号范围整体为 **120880–149043**。

**13.3 单页最大占比。** Top10 各页竞态占该页全部段的比例在 **2.9%–5.4%** 之间（附表 D 逐页给出）：**竞态在页面上的均匀性明显高于它在组件上的均匀性**（`Kernel` 30.6% 的段集中度 vs 单页最高 5.4%）。

# 14 编号与致谢层：366 人、均值 1.58，低于全树 1.80

**14.1 致谢规模。** 232 段**全部**有致谢字段，合计 **366** 人次，均值 **1.58**；分箱：**1 人 149 段、2 人 56、3 人 10、4 人 14、5 人 2、9 人 1**（合计 232，人次 366）。全树对照：含致谢段 **9,132**、合计 **16,466** 人次、均值 **1.80**、最大 **37**。**竞态条目的报告人 fewer-per-entry**（1.58 < 1.80），单段最多 9 人，全树单段最多 37 人。

**14.2 没有内部来源标注。** `internally reported`、`or internally`、`entries in Apple` 三串在 Description 与 Impact 侧均为 **0 段**；`This issue` 在 Description 侧 **1,076** 段、Impact 侧 **103** 段（三字段并集 1,166），但**与 RC 交集 0 段**——即竞态条目**不使用** `This issue …` 这种追加句式（第 6 章的模板化解释：它的 Description 只允许一句话）。

**14.3 编号写法。** 全树 9,136 段中 **7,430 段**含编号；RC 的 232 段 **全部**含编号（附表 D）。结合第 9.2 节的 60 个独占编号：**竞态条目的编号密度高于全树平均**（100% vs 81.3%）。

# 15 与 #118／#119／#121 的接缝：∩R 14、∩WO 3、∩M2 14

**15.1 远程触达（#118）。** `R` 集 **843 段** ∩ RC ＝ **14 段**（1.7% of RC）。这 14 段是附表 C 的三条句：**8** 段 `A attacker in a privileged network position may be able to spoof network packets`、**3** 段 `A remote user may be able to cause unexpected system termination or corrupt kernel memory`、**3** 段 `Mounting a maliciously crafted SMB network share may lead to system termination`。三条都落在 `improved locking`（附表 C 主修复句列），也与第 12.5 节 `Kernel` 11 ＋ `SMB` 3 ＝ 14 一致。**竞态条目 98.3% 不是远程可达表述。**

**15.2 无前提（#119）。** `WO` 集 **178 段** ∩ RC ＝ **3 段**，全部是 `An app may be able to access Contacts without user consent`（第 12.5 节的 `Sync Services`）。

**15.3 多类并写（#121）。** `M2` 集 **185 段** ∩ RC ＝ **14 段**（7.6% of RC、14/185 ＝ 7.6% of M2），即第 8.4 节两条 2 类句。**#121 报的"∩WO ＝ 0"在本篇复算为 ∩WO ＝ 3**：那是 M2 与 WO 的交集（0）与 RC 与 WO 的交集（3）两个不同事实，不冲突——两处的 M2 段集合与 RC 段集合本身互不相交于 WO 之外。

**15.4 交互触发与动名词（#116／#120）。** `G`（动名词起句集）**2,179** 段 ∩ RC ＝ **16 段**；`HI`（交互触发集）**273** 段 ∩ RC ＝ **9 段**，这 9 段全部在 `improved locking` 的 33 段内（附表 B ∩HI 列：9 全在该行）。`user` 词界集 **1,627** 段 ∩ RC ＝ **51 段**。

# 16 与 #123 的集合对账：两套类型词，零交

**16.1 定义对齐。** #123 的 L 集判据：`logic` 词界（`(?<!\w-)logic(?!\w-)`）在三字段任一出现，**550 段**，字段分解 **Impact 10 ∶ Description 540 ∶ 致谢 0**。本篇在同一判据下重测，得数与字段分解**完全相同**（550／10／540／0）。

**16.2 交集为 0。** `L ∩ RC` ＝ **0 段**（按成员做交集并同时报 |L| ＝ 550、|RC| ＝ 232、|L∩RC| ＝ 0）。**同一文本条目不会既自述 `logic issue` 又自述 `race condition`**；两集在 Description 侧共用 `was addressed with` 句式（`was addressed with` 并集 6,581 段同时包含两集全部 782 段），但类型名互斥。

**16.3 两集的字段落位形状相同、内容不同。** 竞态 **0∶232∶0**，逻辑 **10∶540∶0** ——两个类型词都几乎只在 Description；差别是 `logic` 有 10 段进了 Impact（后果文本里出现"逻辑"字样），而 `race condition` 一次都没有。**这是"竞态在后果层完全隐形"最强的对照**：同类位置的另一类型词能做到 10 次。

**16.4 短语层的交叉共享不为零。** 虽然集合零交，两集共用修复短语：`improved state management` 的 662 段里，竞态 11 段、逻辑 **71** 段（#123 侧计数）；`improved checks` 的 1,018 段里竞态 2 段。**共用短语不构成集合包含**，第 22 章误读五号针对把短语计数读成类型计数的写法。

# 17 时序侧信道 7 段：全树唯一的 `timing`，与竞态零交

**17.1 测得。** `timing` 词界在三字段并集 **7 段**，`side-channel`（含连字符变体）**7 段**，`constant-time` **7 段**，整句 `A timing side-channel issue …` **7 段**；七段的组件名全部是 `CoreCrypto`，Description **distinct ＝ 1**：`A timing side-channel issue was addressed with improvements to constant-time computation in cryptographic functions.`；Impact 亦同句（`An attacker may be able to decrypt legacy RSA PKCS#1 v1.5 ciphertexts without …`）。

**17.2 与竞态的关系。** 七段与 RC 交集 **0**（`timing`／`side channel`／`improvements to` 三行在附表 A 的 ∩RC 列均为 **0**）。**这是全树唯一使用"时序/时间"语汇的缺陷条目，而它被标注为侧信道而不是竞态。** `improvements to` 并集 **11 段**，比 7 多出的 4 段与竞态也零交。

**17.3 为什么单列一章。** "竞态"与"时序"在日常语义里相邻，读者容易预期公开文本用 `timing`／`race` 交替命名。实测结果是：**这两套词在语料里各占一块互不重叠的条目集合，且规模差 33 倍（232 ∶ 7）**。加上第 5 章的机制词全零，本篇可以给出一个明确的负结论：**竞态条目在措辞层没有任何时间语汇支持**。

**17.4 版式提示。** 七段分布在 **120304／120306／120309／120311／120880／120884／120886** 七张页面上，其中四张属于域 B（粗体版式）页面族；这与第 11.3 节"RC 在域 B 为 0 段"并不矛盾——同一页面族里可以有粗体条目，只要那些条目不是竞态。

# 18 中文手册层（T1）：竞态本体 8 词全零，`锁` 的 944 次要先扣 689

**18.1 七组 68 词。** 附表 E 给出七组：竞态本体 8、时序与窗口 9、并发与线程 10、锁与原子 10、状态与处理 9、验证与链接 11、缺陷与后果 11；在 178 页可用转储里**全零 24 词、非零 44 词**。

**18.2 竞态本体全零。** `竞态`、`竞争`、`竞态条件`、`竞争条件`、`race`、`赛跑`、`抢占`、`争用` **0 次／0 页**。**《Apple 平台安全保护》整套手册没有任何一处把缺陷类别命名为竞态**。同时 `互斥`、`死锁`、`加锁`、`闭锁`、`状态机`、`多任务`、`时机`、`先后`、`符号链接`、`软链接`、`硬链接`、`挂载`、`解包`、`提权`、`弱点`、`脆弱`、`沙箱` 也全部 0 次。

**18.3 `锁` 的构成必须拆开。** `锁` **944 次／178 页**（覆盖全部页），其中 `解锁` **383** ＋ `锁定` **305** ＋ `上锁` **1** ＝ **689 次（73.0%）**，最集中页面是《自动解锁 Apple 设备》（82 次）。剩下的 `锁` 主要是"文件锁／数据库锁"以外的日常用语。**并发意义的锁词汇（互斥、死锁、加锁）在手册层为 0**，与第 5 章的英文侧完全一致。

**18.4 并发语汇极薄但不为零。** `并发` **9 次／8 页**（最集中《NFC 与 SE 平台安全性》2 次）、`线程` **3 次／仅 1 页**（《操作系统完整性》）、`队列` **2 次／2 页**、`异步` **1 次**、`并行` **3 次／2 页**、`序列化` **1 次**、`串行` **7 次／6 页**（最集中《术语表》）。作为对照，`同步` **270 次／178 页**、`进程` **230 次／178 页**、`同时` **69 次／48 页**是**日常与数据同步语义**，不能当并发证据。

**18.5 后果侧对照。** `漏洞` **20 次／12 页**（最集中《搭载 Apple 芯片的 Mac 的启动磁盘安全性策略控制》5 次）、`弱点` **0 次**、`缺陷` **1 次**、`拒绝服务` **1 次**、`崩溃` **6 次／4 页**、`绕过` **23 次／9 页**（最集中《激活锁安全性》12 次）、`沙盒` **27 次／11 页**。手册层用"安全性概览"语体，几乎不使用缺陷分类词汇——这与英文顾问页只有类型名而没有机制词（第 5 章）形成两侧一致的结论。

# 19 与相邻各篇的口径衔接

**19.1 与 #114（在野）**：∩ ＝ **0**（第 9.1 节）。**与 #115（多阶段路径）**：#115 的栏键口径统计"一条修复条目"，本篇的 232 段即 232 条栏，两篇在页/组件粒度上可比。**与 #116（被传递处理执行的数据与代码）**：`G` ∩ RC ＝ **16**，均为 `Processing maliciously crafted web content …`（7 段）与 `Unpacking a maliciously crafted archive …`（6 段）等**处理型句式**——竞态条目里只有 16 段把"被处理的东西"写进后果。

**19.2 与 #117（持久维持）**：#117 的 `persist*` 集 46 段与 RC 交集为 **0**（该集判据在 Impact 侧，而竞态在 Impact 侧 0 段，所以这是**结构性零交**，不能读成负结论的证据）。第 22 章误读六号专门区分"结构性零交"与"实测零交"：本篇第 9、10、16 章的零交都是**按成员验证过的实测值**，其中第 10 章同时给出两个集合的基数，读者可复核。

**19.3 与 #110／#111（访问控制与信任边界）**：`sandbox` 词族 475 段（三字段）∩ RC ＝ **21 段**（附表 A `SBX` 行为 Impact 侧 418 段∩29 段，两个口径并列给出，不互换）。竞态条目里"沙盒"是**后果**而不是边界描述。

# 20 复算步骤与断言

**20.1 环境。** 在 `新建文件夹/` 目录下运行 Perl；镜像文件位于 `_tmp/adv2/<id>.html`（258 份，排除 `122085`）；中文转储位于 `_tmp/sec*.txt`（179 份，排除 `sec2727afbbc.txt`）。

**20.2 三步。**（1）`perl _tmp/tw122tab.pl`：生成 `_tmp/tw122A.md`…`_tmp/tw122E.md` 并打印七组锚点与五处闭合值，任何断言失败直接 die。（2）`perl _tmp/tw122c.pl`：生成第 12 章组件表 `_tmp/tw122F.md`，并复算五套类型词与 RC 的零交、`Kernel` 的 71 段三分解、四个外部集交集。（3）`perl _tmp/tw122d.pl`：复算修复短语的反向分解（109 ＝ 94＋12＋3；60 ＝ 33＋27；112 ＝ 83＋29；662 ∩ 11；1,018 ∩ 2）与 `timing` 7 段的同句性。

**20.3 关键断言清单。** `|RC| ＝ 232`；`|Impact 侧 race| ＝ 0`；`|致谢侧 race| ＝ 0`；六句合计 ＝ 覆盖段 ＝ 232、重复 ＝ 0；`distinct Description ＝ 6`；`distinct Impact 句 ＝ 30`；后果类类次 ＝ 149；`MEM ∩ RC ＝ 0`；`E ∩ RC ＝ 0`；`L ∩ RC ＝ 0`；`|∩R| ＝ 14`、`|∩WO| ＝ 3`、`|∩M2| ＝ 14`、`|∩G| ＝ 16`、`|∩HI| ＝ 9`、`|∩user| ＝ 51`；页分箱 ＝ 95、编号分箱 ＝ 232、致谢分箱 ＝ 232、组件分箱 ＝ 232；中文层 `竞态` 与 `竞争` ＝ 0 次、`锁` 944 次中 `解锁`＋`锁定`＋`上锁` ＝ 689 次。

**20.4 本篇不自证的部分。** 复算只能证明**文本分布**，不能证明任何一段竞态条目的机制、窗口宽窄或可达性；所有组件占比的分母是"该组件含 Impact 段数"，不是"该组件全部段数"，两者在本语料里相差约 6–9%（第 12 章表头已写明）。

# 21 缺口清单

**21.1 文本没给的。** 并发对象（哪两个执行流）、竞态窗口位置（读—写还是 check—use）、触发前置条件（`improved locking` 的 33 段里没有任何一条写出触发方）。**这些不是可以补测的量，是公开文本没有的位置。**

**21.2 判据留下的模糊。** `improved state handling` 与 `improved state management` 在语义上极近，但一个 94 段竞态、一个 11 段——**这两个短语的区别无法从文本恢复**（`state` 词界并集 866 段，其中竞态 112 段，是二者之和 105，说明还有 7 段的 `state` 出现在别处），第 22 章误读七号针对把两短语当作同义并读成 105 段。

**21.3 需要外部知识才能填的。** 60 个独占编号对应的产品版本与组件归属；`Graphics` 全命中 8 段的成因；域 B 粗体版式页面为何完全没有竞态条目。**这些都需要顾问页之外的来源，本篇一律不做推断。**

**21.4 可以补测但本篇未测的。** 同一编号在多个页面重复出现的"后补修复"关系（`This is a supplementary fix` 与 RC 的交集）；`symbolic link` 126 段中 9 段竞态的其余 117 段的类型分布；`state` 词界 787 段（Description 侧）与 `status` 0 段的对照为什么如此悬殊。

# 22 常见误读纠正

**误读一：`concurrent`／`thread` 为 0，说明这些缺陷不是并发引起的。** 正：**0 只说明公开 Description 不写并发机制**。第 5.2 节的边界：这是文本事实，不是因果事实。

**误读二：232 段占 3.12%，所以竞态是第三大类型。** 正：3.12% 的分母是**含 Impact 的段数**，不是"全部缺陷数"；且各类型词的字段落位不同（`out-of-bounds` 816 段是**三字段并集**，`race condition` 232 段是**单字段**），横向比大小必须先把判据统一到同一字段集合。

**误读三：`improved state handling` 109 段＝竞态 109 段。** 正：94 段竞态 ＋ 12 段类型混淆 ＋ 3 段一致性问题 ＝ 109（逐段读出，闭合）。同理 `improved locking` 60 ＝ 33＋27。

**误读四：`An app may be able to gain root privileges` 20 段属提权类。** 正：本篇 `PRI` 判据要求 `elevat* privileges`／`privileges escalat*` 搭配，该句不满足，故 **0 类**。换一套判据（凡出现 `privileges` 即算提权）会得到完全不同的类分布——第 1.3 节把判据字面写出来就是为了允许换算。

**误读五：RC 与 L 零交，所以竞态与逻辑缺陷无关。** 正：零交只关于"同段是否并写两个类型名"。两集共用 `was addressed with` 句式、共用 `improved state management` 短语，说明**文本层承认修复手段相同**；第 16.4 节。

**误读六：∩＃117 ＝ 0 与 ∩＃114 ＝ 0 是同一强度的证据。** 正：#117 的判据在 Impact 侧、`persist*`，而 RC 在 Impact 侧 0 段——**任何 Impact 侧集合与 RC 的交集都可能因字段错配而虚低**；本篇的 ∩R、∩WO、∩M2 之所以可信，是因为**对应的 14／3／14 段全部被逐条读到具体句子**（第 15 章）。第 9.1 节的 ∩E ＝ 0 则做了双向核对：既查 E ∩ RC，也查 108 段里有无竞态。

**误读七：`improved state management` 与 `improved state handling` 可以合并统计。** 正：合并得 105 段竞态（94＋11），但两短语在全树规模差 **6 倍**（109 ∶ 662），合并会把 662 段的主体误算进竞态。本篇全程分列，附表 B 第 1 行与第 4 行分开给出。

**误读八：`Graphics` 组件 100% 是竞态，所以它最危险。** 正：100% 的分母是 **8 段**，且"占比"的分母定义是**该组件含 Impact 的段数**（第 12.1 节表头）。同理 `Virtualization` 是 1/1。**比例与绝对数必须同报，且小分母比例不构成优先级。**

# 23 结论

**一、这一类弱点在公开文本里只有一个位置：Description。** 232 段，Impact 0，致谢 0；`condition` 词族被它独占（双向差 0）。**类型名是有的，机制词是零的**——15 项并发与线程词、`race window`、`time-of-check`／`TOCTOU`、`synchroniz*`、`mutex`、`deadlock`、`atomic`、`queue` 全部三字段无一次出现。

**二、这一类的"说明"完全由六个修复短语模板承担。** 94＋83＋33＋11＋9＋2 ＝ 232，互斥且闭合，每段恰好一句、句首词一种。其中四个短语同时服务别的类型（`improved locking` 的 27 段是内存破坏、`additional validation` 的 29 段是信息泄露、`improved state handling` 的 15 段是类型混淆与一致性问题、`improved state management` 662 段里只有 11 段竞态），一个短语是全树最大套话之一（`improved checks` 1,018 段里 2 段），只有 `improved handling of symbolic links` 是竞态独占（9/9）。

**三、这一类在后果层没有身份。** 30 条 distinct 后果句里 **11 条不命中任何后果强度类**（含 `access sensitive user data` 29 段、`gain root privileges` 20 段两条最大的句子），`MEM` 类命中 **0**，多类并写只有 **14 段**且集中在两个小样本短语（8 段 `improved state management` ＋ 6 段 `improved handling of symbolic links`）。与远程触达集交 **14**、与无前提集交 **3**、与在野集交 **0**。

**四、这一类与内存破坏类词在文本层互斥。** 五套内存类词（390／816／127／145／788 段）与 `logic`（550 段）**全部零交**；232 段全部含编号、distinct **60** 个、**60 个独占**。所以"竞态"在措辞层是一个**自成一格、编号自成一集、后果借通用句**的类别。

**五、中文手册层根本没有这一类。** 178 页可用转储里竞态本体 8 词 **0 次**，`互斥`／`死锁`／`加锁`／`状态机`／`符号链接`／`挂载`／`解包` **0 次**，`并发` 仅 **9 次／8 页**、`线程` 仅 **3 次／1 页**，而 `锁` 的 944 次有 **689 次是设备锁屏语义**。**英文侧"只有类型名没有机制词"，中文侧"连类型名都没有"——两侧都指向同一件事：公开文档体系不承担解释竞态机制的职责。**

**六、可执行的防御含义（不涉及任何利用）。** 对内部缺陷库而言：如果一条缺陷只在文本里以 `race condition` ＋ 一个修复短语呈现，那么**公开侧无法据其判断触发方、窗口或前置条件**，任何"公开条目已说明机制"的假设都不成立；对组件优先级而言，本语料里竞态段的 **30.6% 落在 `Kernel`**、**46.6% 落在前 5 个组件**，而解析类组件（`WebKit` 0.8%、`Safari` 1.1%）占比极低——**这条分布是措辞层的观测，用于校准"公开信息能告诉我们什么"，不能替代资产侧的真实风险评估。**

---

# 附表 A 竞态判据六组电池（73 行）

**读法：** 前五行给出"竞态本体"的四种写法与一个词族，其 ∩RC 列全为 232 且 Impact 列全为 0——这是第 3、4 章的原始数据。组 2（并发与线程词 15 项）与组 1 的后半（窗口／时序／TOCTOU）是第 5 章的负结论清单。组 3、组 4 给出"锁族／状态族／验证族／文件句柄族"的非零近邻及其与 RC 的交集。组 5 给出 `This issue` 与 `was addressed with` 的套话落位。组 6 是八类判据在 RC 上的命中数，与第 1.3 节同表。列名"三字段并集段"＝ Impact∪Description∪致谢；"全零 28 项"指 Impact 与并集两列同时为 0。

| 组 | 判据 | Impact 段 | 页 | 名 | 三字段并集段 | ∩RC | ∩R | ∩WO | ∩M2 |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|
| 竞态本体词 | `race condition（连写）` | 0 | 0 | 0 | 232 | 232 | 0 | 0 | 0 |
| 竞态本体词 | `race 词界` | 0 | 0 | 0 | 232 | 232 | 0 | 0 | 0 |
| 竞态本体词 | `condition 词界` | 0 | 0 | 0 | 232 | 232 | 0 | 0 | 0 |
| 竞态本体词 | `condition 词族` | 0 | 0 | 0 | 232 | 232 | 0 | 0 | 0 |
| 竞态本体词 | `racing` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 竞态本体词 | `raced` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 竞态本体词 | `racy` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 竞态本体词 | `race window` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 竞态本体词 | `race to race` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 竞态本体词 | `time-of-check／TOCTOU` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 竞态本体词 | `timing` | 0 | 0 | 0 | 7 | 0 | 0 | 0 | 0 |
| 竞态本体词 | `side channel（含连字符）` | 0 | 0 | 0 | 7 | 0 | 0 | 0 | 0 |
| 竞态本体词 | `timing side-channel 整句` | 0 | 0 | 0 | 7 | 0 | 0 | 0 | 0 |
| 竞态本体词 | `constant-time` | 0 | 0 | 0 | 7 | 0 | 0 | 0 | 0 |
| 竞态本体词 | `improvements to` | 0 | 0 | 0 | 11 | 0 | 0 | 0 | 0 |
| 并发与线程词 | `concurrent 词族` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 并发与线程词 | `concurrently` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 并发与线程词 | `concurrency` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 并发与线程词 | `thread 词族` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 并发与线程词 | `thread 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 并发与线程词 | `multithread*` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 并发与线程词 | `task 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 并发与线程词 | `parallel 词族` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 并发与线程词 | `deadlock` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 并发与线程词 | `mutex` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 并发与线程词 | `atomic 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 并发与线程词 | `serializ*` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 并发与线程词 | `async／asynchronous` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 并发与线程词 | `queue 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 并发与线程词 | `simultaneous` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 锁与状态词 | `lock 词界` | 62 | 41 | 18 | 78 | 0 | 0 | 0 | 0 |
| 锁与状态词 | `locking 词界` | 0 | 0 | 0 | 60 | 33 | 0 | 0 | 0 |
| 锁与状态词 | `unlock 词族` | 14 | 14 | 8 | 14 | 0 | 1 | 1 | 0 |
| 锁与状态词 | `locked 词界` | 72 | 50 | 22 | 100 | 0 | 4 | 0 | 0 |
| 锁与状态词 | `lockwise／deadlock 邻词` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 锁与状态词 | `state 词界` | 76 | 62 | 6 | 866 | 112 | 0 | 0 | 0 |
| 锁与状态词 | `state handling` | 0 | 0 | 0 | 109 | 94 | 0 | 0 | 0 |
| 锁与状态词 | `state management` | 0 | 0 | 0 | 662 | 11 | 0 | 0 | 0 |
| 锁与状态词 | `status 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 锁与状态词 | `synchroniz*` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 锁与状态词 | `kernel object 词界` | 0 | 0 | 0 | 3 | 0 | 0 | 0 | 0 |
| 验证与句柄词 | `validation 词界` | 25 | 14 | 6 | 1341 | 83 | 0 | 0 | 0 |
| 验证与句柄词 | `validat* 词族` | 25 | 14 | 6 | 1341 | 83 | 0 | 0 | 0 |
| 验证与句柄词 | `check 词族` | 51 | 24 | 17 | 2017 | 3 | 1 | 4 | 0 |
| 验证与句柄词 | `improved checks` | 0 | 0 | 0 | 1018 | 2 | 0 | 0 | 0 |
| 验证与句柄词 | `additional validation` | 0 | 0 | 0 | 112 | 83 | 0 | 0 | 0 |
| 验证与句柄词 | `symbolic／hard link` | 11 | 11 | 4 | 126 | 9 | 1 | 0 | 0 |
| 验证与句柄词 | `link 词界` | 15 | 15 | 2 | 24 | 9 | 0 | 0 | 0 |
| 验证与句柄词 | `mount 词族` | 43 | 20 | 9 | 43 | 3 | 12 | 0 | 2 |
| 验证与句柄词 | `archive 词界` | 29 | 17 | 6 | 29 | 6 | 0 | 0 | 0 |
| 验证与句柄词 | `unpack／extract` | 13 | 13 | 3 | 13 | 6 | 1 | 0 | 0 |
| 验证与句柄词 | `file 词界` | 1058 | 172 | 105 | 1170 | 8 | 17 | 0 | 95 |
| 验证与句柄词 | `handle 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 修复套话与来源词 | `was addressed with` | 4 | 4 | 2 | 6581 | 232 | 0 | 0 | 0 |
| 修复套话与来源词 | `were addressed with` | 0 | 0 | 0 | 8 | 0 | 0 | 0 | 0 |
| 修复套话与来源词 | `addressed 词界` | 25 | 14 | 6 | 7228 | 232 | 0 | 0 | 0 |
| 修复套话与来源词 | `improved 词界` | 4 | 4 | 2 | 6038 | 149 | 0 | 0 | 0 |
| 修复套话与来源词 | `additional 词界` | 0 | 0 | 0 | 861 | 83 | 0 | 0 | 0 |
| 修复套话与来源词 | `fixed 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 修复套话与来源词 | `resolved 词界` | 0 | 0 | 0 | 19 | 0 | 0 | 0 | 0 |
| 修复套话与来源词 | `This issue` | 103 | 74 | 12 | 1166 | 0 | 4 | 0 | 0 |
| 修复套话与来源词 | `internally reported` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 修复套话与来源词 | `externally reported` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 修复套话与来源词 | `race 子串（三字段并集）` | 0 | 0 | 0 | 232 | 232 | 0 | 0 | 0 |
| 后果强度八类 | `ACE 类` | 336 | 120 | 43 | 336 | 8 | 36 | 0 | 118 |
| 后果强度八类 | `BYP 类` | 291 | 114 | 56 | 301 | 18 | 42 | 0 | 16 |
| 后果强度八类 | `DIS 类` | 530 | 144 | 63 | 603 | 11 | 63 | 0 | 49 |
| 后果强度八类 | `DOS 类` | 2003 | 176 | 130 | 2014 | 74 | 335 | 0 | 143 |
| 后果强度八类 | `FULL 类` | 63 | 43 | 18 | 63 | 6 | 7 | 0 | 0 |
| 后果强度八类 | `MEM 类` | 220 | 85 | 17 | 390 | 0 | 0 | 0 | 2 |
| 后果强度八类 | `PRI 类` | 124 | 62 | 30 | 124 | 3 | 0 | 0 | 21 |
| 后果强度八类 | `SBX 类` | 418 | 119 | 67 | 475 | 29 | 28 | 3 | 39 |
| **合计** | 72 项（全零 28 项） | — | — | — | — | 232 | — | — | 185 |

# 附表 B 六条修复句 × 段页名 × 四个外部集 × 后果八类（7 行）

**读法：** 六句互斥（重复计入 0）、段合计恰为 232。∩R 一列全部落在第 3 行 `improved locking`（14 段），∩M2 一列全部落在第 4 行（8 段）与第 5 行（6 段），∩HI 的 9 段同样在第 3 行——**"远程触达""多类并写""交互触发"三个外部集在竞态侧各自只挂靠一个修复短语**。页列合计 158、名列合计 44 是逐句去重后再求和的值，大于真实基数 95 页／40 名，不可当集合基数引用。

| Description 尾句 | 段 | 页 | 名 | ∩R | ∩WO | ∩M2 | ∩HI | ACE | BYP | DIS | DOS | FULL | MEM | PRI | SBX |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| `improved state handling` | 94 | 48 | 17 | 0 | 0 | 0 | 0 | 0 | 0 | 4 | 52 | 0 | 0 | 3 | 3 |
| `additional validation` | 83 | 58 | 15 | 0 | 0 | 0 | 0 | 0 | 11 | 0 | 15 | 0 | 0 | 0 | 12 |
| `improved locking` | 33 | 31 | 6 | 14 | 0 | 0 | 9 | 0 | 1 | 7 | 7 | 6 | 0 | 0 | 0 |
| `improved state management` | 11 | 10 | 3 | 0 | 0 | 8 | 0 | 8 | 0 | 0 | 0 | 0 | 0 | 0 | 8 |
| `improved handling of symbolic links` | 9 | 9 | 2 | 0 | 3 | 6 | 0 | 0 | 6 | 0 | 0 | 0 | 0 | 0 | 6 |
| `improved checks` | 2 | 2 | 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| **合计** | 232 | 页列合计 158 | 名列合计 44 | 14 | 3 | 14 | 9 | 8 | 18 | 11 | 74 | 6 | 0 | 3 | 29 |

# 附表 C 30 条 distinct Impact 段全文 × 后果类签名 × 主修复句（31 行）

**读法：** 这是 232 段竞态条目在 Impact 字段的全部文本（distinct ＝ 30）。"后果类签名"列按第 1.3 节判据给出类与段数，"无"表示八类全不命中——共 **11 条句、97 段**。第 2、3 行（`access sensitive user data` 29 段、`gain root privileges` 20 段）是最大但不计类的两句，第 22 章误读四号针对此。"主修复句"列给出该句在六句中的最大归属，用于把第 7 章的短语分布与后果分布对齐。

| # | Impact 段全文 | 段 | 页 | 名 | 后果类签名 | 类数 | ∩R | ∩WO | ∩M2 | 主修复句 |
|---|---|---:|---:|---:|---|---:|---:|---:|---:|---|
| 1 | An app may be able to cause unexpected system termination | 43 | 27 | 7 | DOS43 | 1 | 0 | 0 | 0 | improved state handling 32 |
| 2 | An app may be able to access sensitive user data | 29 | 20 | 8 | 无 | 0 | 0 | 0 | 0 | additional validation 19 |
| 3 | An app may be able to gain root privileges | 20 | 16 | 6 | 无 | 0 | 0 | 0 | 0 | improved state handling 16 |
| 4 | An app may be able to cause unexpected system termination or corrupt kernel memory | 15 | 15 | 1 | DOS15 | 1 | 0 | 0 | 0 | improved state handling 15 |
| 5 | An app may be able to access user-sensitive data | 12 | 12 | 3 | 无 | 0 | 0 | 0 | 0 | additional validation 12 |
| 6 | An app may be able to break out of its sandbox | 10 | 10 | 3 | SBX10 | 1 | 0 | 0 | 0 | additional validation 10 |
| 7 | A sandboxed app may be able to execute arbitrary code with kernel privileges | 8 | 8 | 1 | ACE8、SBX8 | 2 | 0 | 0 | 8 | improved state management 8 |
| 8 | An attacker in a privileged network position may be able to spoof network packets | 8 | 8 | 1 | 无 | 0 | 8 | 0 | 0 | improved locking 8 |
| 9 | An attacker may be able to create a read-only memory mapping that can be written to | 8 | 8 | 1 | 无 | 0 | 0 | 0 | 0 | additional validation 8 |
| 10 | An app may be able to leak sensitive kernel state | 7 | 7 | 1 | DIS7 | 1 | 0 | 0 | 0 | improved locking 7 |
| 11 | Processing maliciously crafted web content may lead to an unexpected process crash | 7 | 7 | 1 | 无 | 0 | 0 | 0 | 0 | improved state handling 7 |
| 12 | A shortcut may be able to bypass sandbox restrictions | 6 | 6 | 1 | BYP6、SBX6 | 2 | 0 | 0 | 6 | improved handling of symbolic links 6 |
| 13 | Unpacking a maliciously crafted archive may allow an attacker to write arbitrary files | 6 | 6 | 1 | FULL6 | 1 | 0 | 0 | 0 | improved locking 6 |
| 14 | A local user may be able to cause unexpected system termination or read kernel memory | 5 | 5 | 1 | DOS5 | 1 | 0 | 0 | 0 | additional validation 5 |
| 15 | A malicious attacker with arbitrary read and write capability may be able to bypass Pointer Authentication | 5 | 5 | 1 | BYP5 | 1 | 0 | 0 | 0 | additional validation 5 |
| 16 | An app may be able to cause unexpected system termination or write kernel memory | 5 | 5 | 1 | DOS5 | 1 | 0 | 0 | 0 | improved state handling 5 |
| 17 | An app may be able to access protected user data | 4 | 4 | 2 | 无 | 0 | 0 | 0 | 0 | additional validation 4 |
| 18 | An app may be able to leak sensitive user information | 4 | 4 | 1 | DIS4 | 1 | 0 | 0 | 0 | improved state handling 4 |
| 19 | A local user may be able to read kernel memory | 3 | 3 | 1 | 无 | 0 | 0 | 0 | 0 | improved locking 3 |
| 20 | A malicious application may be able to bypass Privacy preferences | 3 | 3 | 1 | BYP3 | 1 | 0 | 0 | 0 | additional validation 3 |
| 21 | A remote user may be able to cause unexpected system termination or corrupt kernel memory | 3 | 3 | 1 | DOS3 | 1 | 3 | 0 | 0 | improved locking 3 |
| 22 | A sandboxed process may be able to circumvent sandbox restrictions | 3 | 3 | 1 | SBX3 | 1 | 0 | 0 | 0 | improved state handling 3 |
| 23 | An app may be able to access Contacts without user consent | 3 | 3 | 1 | 无 | 0 | 0 | 3 | 0 | improved handling of symbolic links 3 |
| 24 | An app may be able to bypass Privacy preferences | 3 | 3 | 1 | BYP3 | 1 | 0 | 0 | 0 | additional validation 3 |
| 25 | An app may be able to elevate privileges | 3 | 3 | 1 | PRI3 | 1 | 0 | 0 | 0 | improved state handling 3 |
| 26 | Mounting a maliciously crafted SMB network share may lead to system termination | 3 | 3 | 1 | DOS3 | 1 | 3 | 0 | 0 | improved locking 3 |
| 27 | A malicious app may be able to break out of its sandbox | 2 | 2 | 1 | SBX2 | 1 | 0 | 0 | 0 | additional validation 2 |
| 28 | An app may be able to modify protected parts of the file system | 2 | 2 | 1 | 无 | 0 | 0 | 0 | 0 | improved checks 2 |
| 29 | A malicious app may be able to gain root privileges | 1 | 1 | 1 | 无 | 0 | 0 | 0 | 0 | improved locking 1 |
| 30 | An app may bypass Gatekeeper checks | 1 | 1 | 1 | BYP1 | 1 | 0 | 0 | 0 | improved locking 1 |
| **合计** | 30 条 distinct 段全文 | 232 | 页列合计 203 | 名列合计 53 | 0 类 11／1 类 17／2 类 2／3 类 0 | 232 | 14 | 3 | 14 | — |

# 附表 D 页面、编号与致谢四组分箱（37 行）

**读法：** 第一块页分箱闭合到 95；第二块 Top10 页给出逐页占比（2.9%–5.4%）；第三块编号分箱闭合到 232，配合"编号归属"两行（60 独占 ∶ 0 共享）；第四块致谢人数分箱闭合到 232 段／366 人次。末四行是版式域与字段落位的负结论：域 B 内竞态 **0 段**、致谢字段含 `race` **0 段**。

| 块 | 分箱 | 计数 | 说明 |
|---|---|---:|---|
| RC 段所在页的段数 | 1 | 41 | 共 95 页含 race condition |
| RC 段所在页的段数 | 2 | 26 | 共 95 页含 race condition |
| RC 段所在页的段数 | 3 | 7 | 共 95 页含 race condition |
| RC 段所在页的段数 | 4 | 9 | 共 95 页含 race condition |
| RC 段所在页的段数 | 5 | 4 | 共 95 页含 race condition |
| RC 段所在页的段数 | 6-9 | 6 | 共 95 页含 race condition |
| RC 段所在页的段数 | 10-19 | 2 | 共 95 页含 race condition |
| RC 段所在页的段数 | 合计 | 95 | 校验=95 页、RC 段 232 |
| Top10 页逐页 | 149035 | 11 | 该页全部段 314 段，RC 占 3.5% |
| Top10 页逐页 | 128067 | 10 | 该页全部段 185 段，RC 占 5.4% |
| Top10 页逐页 | 149042 | 8 | 该页全部段 181 段，RC 占 4.4% |
| Top10 页逐页 | 128071 | 7 | 该页全部段 151 段，RC 占 4.6% |
| Top10 页逐页 | 128072 | 7 | 该页全部段 142 段，RC 占 4.9% |
| Top10 页逐页 | 149043 | 7 | 该页全部段 190 段，RC 占 3.7% |
| Top10 页逐页 | 126794 | 6 | 该页全部段 123 段，RC 占 4.9% |
| Top10 页逐页 | 149034 | 6 | 该页全部段 209 段，RC 占 2.9% |
| Top10 页逐页 | 149036 | 5 | 该页全部段 118 段，RC 占 4.2% |
| Top10 页逐页 | 149037 | 5 | 该页全部段 128 段，RC 占 3.9% |
| Top10 页逐页 | 合计 | 72 | 占 RC 段 31.0%（232 段／95 页） |
| RC 段内 distinct CVE 数 | 1 | 224 | RC 232 段全部含编号 |
| RC 段内 distinct CVE 数 | 2 | 1 | RC 232 段全部含编号 |
| RC 段内 distinct CVE 数 | 3 | 5 | RC 232 段全部含编号 |
| RC 段内 distinct CVE 数 | 4+ | 2 | RC 232 段全部含编号 |
| RC 段内 distinct CVE 数 | 合计 | 232 | 校验=232；全树 distinct 编号 2108 个，RC 内 60 个 |
| 编号归属 | 仅出现在 RC 段 | 60 | 这 60 个编号不出现在其余 8,904 段 |
| 编号归属 | 也出现在非 RC 段 | 0 | 与 #114/#115 的编号无共享 |
| RC 段致谢人数 | 1 | 149 | RC 合计 366 人、均值 1.58 |
| RC 段致谢人数 | 2 | 56 | RC 合计 366 人、均值 1.58 |
| RC 段致谢人数 | 3 | 10 | RC 合计 366 人、均值 1.58 |
| RC 段致谢人数 | 4 | 14 | RC 合计 366 人、均值 1.58 |
| RC 段致谢人数 | 5 | 2 | RC 合计 366 人、均值 1.58 |
| RC 段致谢人数 | 9+ | 1 | RC 合计 366 人、均值 1.58 |
| RC 段致谢人数 | 合计 | 232 | 校验=232；全树 9132 段均值 1.80、最大 37 |
| 同页共存 | RC 页中还有非 RC 段的页 | 95 | 95 页全部如此 |
| 同页共存 | 这 95 页全部段 | 6548 | RC 段 232 占 3.5% |
| 版式域 | 域 B 内 RC 段 | 0 | 域 B 共 91 段，粗体版式页无 race condition |
| 字段落位 | 致谢字段含 race | 0 | Description 232／Impact 0／致谢 0 |

# 附表 E 中文手册层七组对应词（69 行）

**读法：** 分母是《Apple 平台安全保护》zh-CN 转储 179 份中的 **178 页可用**（窗口切分后剔除 1 份无正文）。第一组 8 词全零是第 18.2 节的核心负结论；第四组 `锁` 的 944 次要结合"最集中页面"列读——《自动解锁 Apple 设备》82 次说明这是设备锁屏语汇，与"解锁＋锁定＋上锁 ＝ 689 次"的拆解一致；第三组 `同步` 270 次／178 页、`进程` 230 次／178 页覆盖全部页面，是日常语体而非并发语汇。

| 组 | 中文词 | 出现次数 | 覆盖页 | 覆盖页占比 | 最集中页面 |
|---|---|---:|---:|---:|---|
| 竞态本体 | 竞态 | 0 | 0 | 0.0% | — |
| 竞态本体 | 竞争 | 0 | 0 | 0.0% | — |
| 竞态本体 | 竞态条件 | 0 | 0 | 0.0% | — |
| 竞态本体 | 竞争条件 | 0 | 0 | 0.0% | — |
| 竞态本体 | race | 0 | 0 | 0.0% | — |
| 竞态本体 | 赛跑 | 0 | 0 | 0.0% | — |
| 竞态本体 | 抢占 | 0 | 0 | 0.0% | — |
| 竞态本体 | 争用 | 0 | 0 | 0.0% | — |
| 时序与窗口 | 时序 | 4 | 3 | 1.7% | 安全隔区（2 次） |
| 时序与窗口 | 时间窗口 | 1 | 1 | 0.6% | 用于数据保护的密钥包（1 次） |
| 时序与窗口 | 窗口 | 12 | 7 | 3.9% | 单点登录安全性（2 次） |
| 时序与窗口 | 时机 | 0 | 0 | 0.0% | — |
| 时序与窗口 | 时刻 | 1 | 1 | 0.6% | 安全激活数据连接（1 次） |
| 时序与窗口 | 先后 | 0 | 0 | 0.0% | — |
| 时序与窗口 | 顺序 | 1 | 1 | 0.6% | 基于 Intel 的 Mac 的 recoveryOS 和诊断环境（1 次） |
| 时序与窗口 | 期间 | 78 | 45 | 25.3% | Apple 安全性研究设备（6 次） |
| 时序与窗口 | 短暂 | 2 | 2 | 1.1% | iPhone 蜂窝网络通话中继安全性（1 次） |
| 并发与线程 | 并发 | 9 | 8 | 4.5% | NFC 与 SE 平台安全性（2 次） |
| 并发与线程 | 并行 | 3 | 2 | 1.1% | 密码监视（2 次） |
| 并发与线程 | 线程 | 3 | 1 | 0.6% | 操作系统完整性（3 次） |
| 并发与线程 | 进程 | 230 | 178 | 100.0% | iOS 和 iPadOS 中的 ReplayKit 安全性（6 次） |
| 并发与线程 | 任务 | 8 | 8 | 4.5% | 安全隔区（1 次） |
| 并发与线程 | 队列 | 2 | 2 | 1.1% | iMessage 信息如何安全发送和接收信息（1 次） |
| 并发与线程 | 异步 | 1 | 1 | 0.6% | iCloud 高级数据保护（1 次） |
| 并发与线程 | 同步 | 270 | 178 | 100.0% | 钥匙串安全同步（29 次） |
| 并发与线程 | 多任务 | 0 | 0 | 0.0% | — |
| 并发与线程 | 同时 | 69 | 48 | 27.0% | 搭载 Apple 芯片的 Mac 的 LocalPolicy 文件内容（4 次） |
| 锁与原子 | 锁 | 944 | 178 | 100.0% | 自动解锁 Apple 设备（82 次） |
| 锁与原子 | 加锁 | 0 | 0 | 0.0% | — |
| 锁与原子 | 解锁 | 383 | 178 | 100.0% | 自动解锁 Apple 设备（70 次） |
| 锁与原子 | 死锁 | 0 | 0 | 0.0% | — |
| 锁与原子 | 互斥 | 0 | 0 | 0.0% | — |
| 锁与原子 | 原子 | 1 | 1 | 0.6% | Apple 文件系统作用（1 次） |
| 锁与原子 | 序列化 | 1 | 1 | 0.6% | iOS 和 iPadOS 中的 ReplayKit 安全性（1 次） |
| 锁与原子 | 串行 | 7 | 6 | 3.4% | 术语表（2 次） |
| 锁与原子 | 锁定 | 305 | 178 | 100.0% | 安全隔区（18 次） |
| 锁与原子 | 上锁 | 1 | 1 | 0.6% | 数据保护类（1 次） |
| 状态与处理 | 状态 | 87 | 45 | 25.3% | 钥匙串数据保护（7 次） |
| 状态与处理 | 状态机 | 0 | 0 | 0.0% | — |
| 状态与处理 | 处理 | 337 | 178 | 100.0% | 安全隔区（24 次） |
| 状态与处理 | 句柄 | 1 | 1 | 0.6% | 搭载 Apple 芯片的 Mac 上的 Rosetta 2（1 次） |
| 状态与处理 | 对象 | 29 | 17 | 9.6% | 搭载 Apple 芯片的 Mac 的 LocalPolicy 文件内容（4 次） |
| 状态与处理 | 生命周期 | 9 | 8 | 4.5% | 证明过程安全性（2 次） |
| 状态与处理 | 初始化 | 14 | 11 | 6.2% | LocalPolicy 签名密钥创建和管理（2 次） |
| 状态与处理 | 销毁 | 4 | 3 | 1.7% | iCloud 钥匙串的托管安全性（2 次） |
| 状态与处理 | 释放 | 7 | 4 | 2.2% | “平安确认”安全性（3 次） |
| 验证与链接 | 验证 | 432 | 178 | 100.0% | Apple 钱包中的证件（19 次） |
| 验证与链接 | 校验 | 19 | 3 | 1.7% | Apple 钱包中证件的安全性（16 次） |
| 验证与链接 | 检查 | 66 | 30 | 16.9% | 管理式 Apple 账户安全性（13 次） |
| 验证与链接 | 符号链接 | 0 | 0 | 0.0% | — |
| 验证与链接 | 软链接 | 0 | 0 | 0.0% | — |
| 验证与链接 | 硬链接 | 0 | 0 | 0.0% | — |
| 验证与链接 | 链接 | 39 | 25 | 14.0% | iOS、iPadOS、Apple tvOS、visionOS 和 watchOS 中的 App 代码签名过程（4 次） |
| 验证与链接 | 挂载 | 0 | 0 | 0.0% | — |
| 验证与链接 | 归档 | 2 | 2 | 1.1% | 访问钥匙类型（1 次） |
| 验证与链接 | 解包 | 0 | 0 | 0.0% | — |
| 验证与链接 | 提取 | 12 | 10 | 5.6% | iMessage 信息如何安全发送和接收信息（2 次） |
| 缺陷与后果 | 漏洞 | 20 | 12 | 6.7% | 搭载 Apple 芯片的 Mac 的启动磁盘安全性策略控制（5 次） |
| 缺陷与后果 | 弱点 | 0 | 0 | 0.0% | — |
| 缺陷与后果 | 缺陷 | 1 | 1 | 0.6% | 基于 Intel 的 Mac 中的 UEFI 固件安全性（1 次） |
| 缺陷与后果 | 脆弱 | 0 | 0 | 0.0% | — |
| 缺陷与后果 | 拒绝服务 | 1 | 1 | 0.6% | macOS 中的防火墙安全性（1 次） |
| 缺陷与后果 | 崩溃 | 6 | 4 | 2.2% | Apple 操作系统中的后台安全改进（3 次） |
| 缺陷与后果 | 终止 | 13 | 7 | 3.9% | iOS 中的车钥匙安全性（4 次） |
| 缺陷与后果 | 提权 | 0 | 0 | 0.0% | — |
| 缺陷与后果 | 绕过 | 23 | 9 | 5.1% | 激活锁安全性（12 次） |
| 缺陷与后果 | 沙盒 | 27 | 11 | 6.2% | macOS 中的 Option ROM 安全性（6 次） |
| 缺陷与后果 | 沙箱 | 0 | 0 | 0.0% | — |
| **合计** | 68 词（全零 24 词、非零 44 词） | — | — | 178 页可用 | — |

---

**规则：** 本篇全部计数按 #111 第 1 章两版式域切块口径执行，判据大小写不敏感、空格类字符（U+00A0／U+202F）统一按 `$S` 归一；词界优先，仅在明确标注"词族"时用子串匹配；字段范围每次写明是 **Impact 单字段**还是**三字段并集**（附表 A 的"三字段并集段"列与第 10 章五套类型词集为三字段口径，其余外部集为 Impact 口径）。**分母一律三选一写明：9,136（全树段）／7,434（含 Impact 段）／6,548（RC 所在 95 页全部段）；类次 149 与段次 232 分列报出，不互换；附表 B、C 的页列与名列是逐行去重后的再求和，不得当作集合基数引用。**

**边界声明：** 本篇是**公开英文与中文文本的措辞计量**，不是脆弱性评估、不是攻击可行性研究，也不提供任何绕过方法、利用代码、样本、利用链构造或针对他人设备的操作步骤与时序。文中"竞态条件"一律指**公开条目在 Description 字段把缺陷自述为 race condition 这一文本事实**，不推断该缺陷的真实触发机制、并发实现细节、竞态窗口宽窄或可利用性；232 段、60 个编号、149 类次、40 个组件名等数字**只描述文本分布**，不构成任何组件安全性评级、缺陷优先级排序或攻击路径存在的结论。

Telegram: https://t.me/one00190
