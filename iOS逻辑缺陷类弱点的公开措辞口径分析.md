@@TITLE@@iOS 逻辑缺陷类弱点的公开措辞口径分析
@@SUBTITLE@@选题是"逻辑缺陷（logic）"这一类弱点。逐词测量的结果是：**这一类在公开文本里的落位与竞态类（#122）同为 Description 主导，但它还额外占住 Impact 字段的 10 段，而且两个字段互斥**——`logic` 词界写在 **Impact 侧 10 段**、写在 **Description 侧 540 段**、写在**致谢侧 0 段**，Impact 侧与 Description 侧的交集是 **0 段**，所以三字段并集 **550 ＝ 10 ＋ 540** 无重复计入，占全树 9,136 段的 **6.02%**、占含 Impact 的 7,434 段的 **7.40%**，覆盖 **162／257 页（63.0%）**与 **81／472 名（17.2%）**。第二个发现是**词形的绝对单一**：550 段里 `logic` **同段恰好出现 1 回**，词界匹配与子串匹配**双向差 0**（词条分布只有 `logic(550)` 一种），而 `logical`／`logically`／`logic flaw`／`logic bug`／`logic problem`／`logic in` **全部为 0 段**——这一类在英语里只有"logic"一个裸名词写法。第三个发现是**句位分裂**：14 条含 `logic` 的 Description 句分两种句位，`A logic issue was addressed with …`（类型名在主语位）**441 段**、`… was addressed with improved logic`（类型名在修复位）**99 段**、其他句位 **0 段**。第四个发现是**后果层依旧没有身份**：550 段的后果字段共 **106 条 distinct 全文**，段级 **0 类 330／1 类 215／2 类 2／3 类 3**，八类类次 **228**，其中 **`MEM`（内存破坏）类在 Impact 侧命中 0 段**（另有 3 段把 memory corruption 写在 Description）；与在野集交 **9 段且全部是同一个组件 `Messages` 的同一条 iCloud Link 句子**；与竞态集**零交**；中文手册层 `逻辑` 只有 **4 次／2 页**，实测上下文全是硬件语汇（`硬件逻辑`、`逻辑板`），`逻辑缺陷`／`逻辑错误`／`逻辑问题`／`逻辑漏洞` **0 次**。
@@INFO@@报告日期|2026 年 10 月 29 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（258 份文件排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 9,132 段含致谢**。逻辑缺陷集 L ＝ `logic` 以词界出现在 Impact、Description、致谢任一字段 **＝ 550 段 / 162 页 / 81 名**，其中 Impact 侧 10 段、Description 侧 540 段、致谢侧 0 段。对照集：**R** ＝ #118 的远程触达集（Impact 侧 11 个通道词族）**843 段 / 178 页 / 72 名**；**WO** ＝ #119 的无前提集（Impact 含 `without` 词界）**178 段 / 97 页 / 37 名**；**E** ＝ #114 的在野集（Impact 含 `exploit` 词族）**108 段 / 76 页 / 12 名**；**G／HI** ＝ #116 与 #120 的动名词集 **2,179 段** 与交互触发集 **273 段 / 102 页 / 35 名**；**M2** ＝ #121 的同段 ≥2 后果类集 **185 段 / 82 页 / 35 名**；**U** ＝ Impact 含 `user`（词界）**1,627 段 / 175 页 / 200 名**；**RC** ＝ #122 的竞态集（Description 含 `race condition`）**232 段 / 95 页 / 40 名**。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或利用链构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**"逻辑缺陷"这一类型词在公开英文文本的哪个字段、哪一句位、以哪一种词形出现，与后果字段的八类强度词、与在野标注、与别的类型词之间的集合关系**，属于措辞与文本计量分析；文中任何"逻辑缺陷"表述均指**公开条目把缺陷自述为 logic（或把修复手段自述为 improved logic）这一文本事实**，不是对该缺陷是否属于状态机错误、检查—使用顺序错误、业务规则错误或其他工程分类的判断，也不构成任何组件存在设计缺陷的结论
@@INFO@@分类|判据**电池 68 项 / 三字段全零 9 项 / 非零 59 项 / Impact 列合计 7,601**（附表 A，六组：逻辑本体词 12、校验与检查词 12、状态与处理词 10、内存与边界词 14、修复套话与来源词 12、后果强度八类 8）/ **十四条含 logic 的 Description 句 × 段页名 × 句位 × ∩R ∩WO ∩E ∩M2 ∩RC × 八类**（附表 B，15 行含合计，段合计 **540**、句位 **主语位 441 ∶ 修复位 99 ∶ 其他 0**、类次 **228**）/ **八个 Description 修复短语 × 全树段 ∶ L 内段 ∶ 非 L 段 × 五个外部集**（附表 C，9 行含合计，全树 **2,215** ∶ L 内 **517** ∶ 非 L **1,698** ＝ **23.3%**）/ **页面、编号与致谢四组分箱**（附表 D，41 行，页分箱闭合 162、编号分箱闭合 550、致谢分箱闭合 549）/ **中文层 63 词**（附表 E，七组：逻辑本体 7、缺陷与问题词 8、检查与校验 8、状态与处理 8、限制与许可 9、路径与容器 11、类型与后果 12；**全零 24 词、非零 39 词**）/ **组件层 81 名**（附表 F，82 行含合计，段合计 550、这 81 名的 Impact 全集 **4,092** 段、L 占其 **13.4%**、占全树含 Impact 段的 **55.0%**）/ **八把刀**：字段刀（Impact 10 ∶ Description 540 ∶ 致谢 0，两字段互斥）、词形刀（词界 ≡ 子串，`logical`／`logically`／`logic flaw` 全零）、句位刀（主语位 441 ∶ 修复位 99 ∶ 其他 0）、句式动词刀（`was addressed with` 516 ＋ `addressed by` 33 ＋ `resolved with` 1 ＝ 550）、修复短语反向刀（`improved logic` 61/61、`improved restrictions` 47/47、`improved error handling` 18/18、`improved permissions logic` 5/5 四条 100% 独占；`improved checks` 236/1,018 与 `improved state management` 74/662 两条被严重稀释）、后果刀（228 类次 ∶ 550 段次、0 类 330、`MEM` 类 Impact 侧 0）、在野刀（∩E ＝ 9，全部 `Messages`）、类型词零交刀（十套类型词与 L 零交、五套有交）
@@INFO@@难度|进阶（第 3 章的 550 段有**三套分母**：占全树 9,136 段是 **6.02%**、占含 Impact 的 7,434 段是 **7.40%**、占其所在 162 页全部 8,172 段是 **6.7%**，三个数都真，混用会互相矛盾；第 5 章的附表 B **页列合计 368 大于 162 页、名列合计 123 大于 81 名**，因为同页可有多条不同 Description 句、同名组件可拆到多句，页列与名列**不可当作并集去重值**；第 8 章的后果类列合计 **228 是"类次"不是"段次"**（0 类 330＋1 类 215＋2 类 2＋3 类 3 ＝ 550 段，类次 ＝ 215×1＋2×2＋3×3 ＝ 228），把 228 读成 228 段会虚增 72 段；第 11 章专设一节处理**同一量在不同字段口径下的两套得数**——附表 B 的 226 段与附表 C 的 236 段相差 10 段、∩WO 的 23 与 24 相差 1 段、∩E 的 0 与 9 相差 9 段，三处差值都来自**同一批 10 段**；第 7 章的 517 与 550 相差 33，是 `The issue was addressed by adding additional logic.` 一句不属于附表 C 的八个短语，不是遗漏；第 10 章的十套零交与五套有交**必须逐套表述**，把"零交"推广成"逻辑缺陷与内存类缺陷无关"是越界；第 18 章中文层 `逻辑` 的 4 次全部要连同实测上下文一起引用，否则会被读成"手册提到逻辑缺陷"）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

**"逻辑缺陷"是一类经常被当作兜底命名的弱点，但它在公开文本里的落位是可以直接测的。** 本篇把这句选题拆成四个互相独立的断言，逐个测量：

**断言一（存在这一类）**：公开条目里确实有把缺陷自述为 `logic` 的文本。**测法**：`logic` 词界分别在三个字段匹配。**结果**：成立，且**规模是竞态类的 2.4 倍**——Impact 侧 **10**、Description 侧 **540**、致谢侧 **0**，三字段并集 **550**；两个非零字段**互斥（交 0 段）**，所以并集就是相加（第 3 章）。

**断言二（词形会被写宽）**：文本会用 `logical`、`logic flaw`、`logic bug`、`logic problem` 等多种词形。**测法**：附表 A 组 1 的 12 项逻辑本体词。**结果**：**不成立**。`logical`／`logically`／`logic flaw`／`logic bug`／`logic problem`／`logic in` 六个判据**三字段全零**；`logic` 词界 550 段与 `logic` 子串 550 段**双向差 0**，词条分布只有一种：`logic(550)`（第 4 章）。

**断言三（这一类有可识别的后果）**：后果字段会为逻辑缺陷写出可分类的影响。**测法**：Impact 侧 distinct 全文 ＋ 八类共现。**结果**：550 段的后果文本共 **106 条 distinct 全文**，段级 **0 类 330／1 类 215／2 类 2／3 类 3**，类次 **228**，`MEM` 类 Impact 侧命中 **0**（第 8 章）。

**断言四（这一类独立成类）**：`logic` 在措辞层与其他类型词可分。**测法**：与十五套类型词集合做真交集。**结果**：`race condition`（232）、`out-of-bounds`（816）、`use after free`（127）、`type confusion`（145）、`integer`（158）、`buffer`（200）、`overflow`（374）、`uninitialized`（16）、`double free`（43）、`dereference`（31）**十套零交**；`memory corruption`（388→3）、`corrupt*`（840→3）、`memory`（1,967→8）、`bounds*`（796→8）、`leak*`（192→23）**五套有交但极小**（第 10 章）。

**所以本篇的产出是一条"命名宽、机制零、后果借"的结论**：`logic` 是这套语料里**覆盖面最广的类型名**（162／257 页、81／472 名），但它**不带任何机制词**（`logical`、`flaw`、`bug`、`state machine` 一类全部为零或不成立）、**不与任何内存类词共段**，而且**同一套类型名在两个字段里的位置互斥**——10 段只写在 Impact，540 段只写在 Description。第 20 章给复算，第 22 章纠正会把 550 读成"550 条已确认设计缺陷"的九种误读。

# 1 操作性定义与分母

**1.1 语料与切块。** 与 #114–#122 同一镜像、同一切块口径：块起于 `<h3 class="gb-header">`（域 A）或粗体段（域 B），块止于**四锚点**（下一块起点、页脚声明、版权行、脚本行），缺一锚点就会把页面免责声明切进字段文本。全树 **9,136 段 / 257 页 / 472 名**；含 Impact **7,434 段**；含致谢 **9,132 段**。域 A **9,045 段 / 246 页 / 470 名**，域 B **91 段 / 11 页 / 26 名**。

**1.2 L 集（本篇主集）。** 判据：`logic` 以词界（前后不是字母、数字或连字符）出现在 Impact、Description 或致谢任一字段；大小写不敏感；空格类字符（U+00A0／U+202F）按 `$S` 归一。**L ＝ 550 段 / 162 页 / 81 名**。字段落位 **Impact 10 ∶ Description 540 ∶ 致谢 0**，且 Impact 侧集与 Description 侧集**交集 0 段**（第 3 章）。

**1.3 后果强度八类（与 #121／#122 同判据，只在 Impact 字段匹配）。**

| 代号 | 判据 | 全树 Impact 段 | L 命中 | L 占该类 |
|---|---|---:|---:|---:|
| `ACE` | `arbitrary code execution`／`execute arbitrary code` | 336 | 10 | 3.0% |
| `BYP` | `bypass`（词界） | 291 | 45 | 15.5% |
| `DIS` | `disclos*`／`leak*`／`expos*` | 530 | 36 | 6.8% |
| `DOS` | `denial-of-service`／`unexpected system／app termination`／`terminat*` | 2,003 | 42 | 2.1% |
| `FULL` | `arbitrary file(s)`／`any action`／`full control` | 63 | 12 | 19.0% |
| `MEM` | `memory corruption`／`corrupt memory` | 220 | **0** | 0.0% |
| `PRI` | `elevat* privileges`／`privileges escalat*` | 124 | 34 | 27.4% |
| `SBX` | `sandbox`（词界） | 418 | 49 | 11.7% |

类次合计 **10＋45＋36＋42＋12＋0＋34＋49 ＝ 228**，与第 8 章的段级分布（0 类 330＋1 类 215＋2 类 2＋3 类 3 ＝ 550 段，类次 ＝ 215＋4＋9 ＝ **228**）互为校验。

**1.4 三套分母。** 同一句"逻辑缺陷条目占多少"有三个答案：**6.02%**（÷9,136 全树段）、**7.40%**（÷7,434 含 Impact 段）、**6.7%**（÷550 段所在 162 页的全部 8,172 段）。本篇每次出现百分号都写清用的是哪一套。第四套用于组件层（第 13 章）：**该组件含 Impact 的段数**，L 全集占该口径 **13.4%**。

**1.5 与 #122 的规模对照。** `logic` 550 段 ∶ `race condition` 232 段 ＝ **2.37∶1**；页 162 ∶ 95；名 81 ∶ 40。两集**交集 0 段**（第 16 章），所以并集 782 段，占全树 **8.56%**。

# 2 结论速览（每条都可按第 20 章复算）

**一、类型名主要活在 Description，但独占 Impact 的 10 段。** Impact 侧 **10** 段、Description 侧 **540** 段、致谢侧 **0** 段；**10 与 540 互斥**（交 0 段），所以 550 ＝ 10 ＋ 540。这 10 段的 Description 全部**不含** `logic`：9 段是 `This issue was addressed with improved checks.`（`Messages`），1 段是 `The issue was addressed with improved checks.`（`WindowServer`）。

**二、词形只有一种。** 550 段里 `logic` **同段恰好出现 1 回**（0 段出现 2 回）；词界 550 与子串 550 **双向差 0**；`logical`、`logically`、`logic flaw`、`logic bug`、`logic problem`、`logic in` 六个判据**三字段全零**。附表 A 的 68 项判据里**全零 9 项**，其中 6 项是这一族。

**三、句位分两种且闭合。** 14 条含 `logic` 的 Description 句中，`A logic issue was addressed with …`（类型名在**主语位**）**441 段**、`… was addressed with improved logic`／`adding additional logic`（类型名在**修复位**）**99 段**、其他句位 **0 段**，441＋99 ＝ 540（附表 B）。

**四、句式动词三选一闭合到 550。** `was addressed with`∩L **516** ＋ `addressed by`∩L **33** ＋ `resolved with`∩L **1** ＝ **550**；`were addressed with` 全树 8 段而 **∩L 0**；`addressed` 词界∩L **549**（差的 1 段正是唯一用 `resolved` 的那段）。

**五、Description 全文只有 16 种。** 550 段的 Description 段全文 distinct ＝ **16**，其中 **14 条含 `logic`**（覆盖 540 段）、另 2 条不含 `logic`（覆盖那 10 段）；16 条里有 **2 条是双句文本**。最大一句 `A logic issue was addressed with improved checks.` **226 段**，占 L 的 **41.1%**。

**六、修复短语四条独占、两条稀释。** `improved logic` **61/61**、`improved restrictions` **47/47**、`improved error handling` **18/18**、`improved permissions logic` **5/5** 是 L 独占；而 `improved checks` **236/1,018（23.2%）**、`improved state management` **74/662（11.2%）**、`improved validation` **44/366（12.0%）**被大量非 L 条目共用；`improved file handling` **32/38（84.2%）**半独占（附表 C）。

**七、后果层没有身份，且与内存类词互斥。** 106 条 distinct 后果全文里 **68 条不命中任何后果强度类（覆盖 330 段）**，含最大的三句 `An app may be able to access user-sensitive data`（25 段）、`An app may be able to access sensitive user data`（24 段）、`Processing maliciously crafted web content may prevent Content Security Policy from being enforced`（21 段）；`MEM` 类 Impact 侧命中 **0**；十套类型词与 L **零交**。同时 `race condition` 232 段与 L 零交（第 10、16 章）。

**八、在野只有 9 段，全在一个组件。** `exploit` 词族 108 段 ∩ L ＝ **9**，全部是 `Messages` 在同一条 iCloud Link 后果句上的 9 个页面（`122173`、`122174`、`122345`、`122346`、`122900`–`122904`）；反过来 108 段里的 **99 段非 L**（99 段／71 页／11 名）。`extremely sophisticated` 并集 59 段里也只有这 9 段属 L（第 9 章）。

**九、中文手册层几乎不存在这一类。** `逻辑` 在 178 页可用转储里只有 **4 次／2 页**，实测上下文是"通过硬件逻辑按照如下电路图实施"、"不可重新编程的硬件逻辑。此逻辑会在关闭麦克风前提供防抖、滞后"、"自行承担移除密码带来的**逻辑板**更换费用"——全是硬件语汇；`逻辑缺陷`／`逻辑错误`／`逻辑问题`／`逻辑漏洞`／`状态机`／`推理`／`因果` **0 次**（附表 E、第 18 章）。

# 3 字段刀：Impact 10 ∶ Description 540 ∶ 致谢 0，且两字段互斥

**3.1 测量。** 对同一判据 `logic` 词界分别在三个字段匹配：Impact **10** 段、Description **540** 段、致谢 **0** 段；三字段并集 **550** 段／162 页／81 名。关键附加量：**|Impact 侧 ∩ Description 侧| ＝ 0**——那 10 段的 Description 一句都不含 `logic`，而这 540 段的 Impact 一句都不含 `logic`。

**3.2 10 段的原文。** 这 10 段只有两条 distinct 后果文本：（a）**9 段** `A logic issue existed when processing a maliciously crafted photo or video shared via an iCloud Link. Apple is aware of a report that this issue may have been exploited in an extremely sophisticated attack against specific targeted individuals.`，组件 `Messages`，页面 `122173`／`122174`／`122345`／`122346`／`122900`–`122904`；（b）**1 段** `A logic issue existed where a process may be able to capture screen contents without user consent`，组件 `WindowServer`，页面 `121238`。**这一族的 2 个组件名与 10 个页面就是附表 A 首行的"名 2／页 10"。**

**3.3 为什么这条不是废话。** Apple 顾问页的字段分工是固定的：**Description 写"问题是什么、怎么修的"，Impact 写"能做到什么"，致谢写"谁报告的"**。逻辑缺陷的落位与竞态类有一处实质差别：**竞态的 Impact 侧是 0，逻辑缺陷的 Impact 侧有 10 段**——也就是说，语料里存在"类型名被写进后果字段"的条目，但这 10 段同时也是**在野标注段**（9 段）与 `without user consent` 段（1 段）。第 11 章把这条差别逐段核算成两处口径的差值表。

**3.4 与 #122 的对照。** #122 的 RC 集**只能在 Description 侧定义**（Impact 侧 0 段）。两个集天然不同构：本篇的 L 需要三字段并集才能取全，而 RC 取单字段即可。**这就是"同一棵树上两套类型词的字段口径不同"**，第 1.5 节的 2.37∶1 规模比必须在此口径下理解——不是"逻辑缺陷条目是竞态的 2.37 倍"，而是"三字段并集是单字段并集的 2.37 倍"。

**3.5 致谢侧 0 段。** 致谢字段只放人名与报告来源标注，从不放类型词；`internally reported`、`externally reported` 两串在本语料的 Description 与 Impact 侧都是 **0 段**（附表 A 组 5），所以这 550 段的来源标注形式只有一种：**直接列报告人**（第 15 章）。

# 4 词形刀：词界 ≡ 子串，`logical`／`logic flaw` 全零

**4.1 两种匹配方式给出同一个集。** `logic` **词界**（`(?<![\w-])logic(?![\w-])`）＝ **550 段**；`logic` **子串**（无边界）＝ **550 段**；双向差 **0**。把 550 段的全部含 `logic` 词条拆出来统计，分布只有一种：`logic(550)`。**没有 `logics`、没有 `logic-based`、没有 `illogic`。**

**4.2 六个邻近词形为零。** `logical` 词界 **0**、`logically` 词界 **0**、`logic flaw` **0**、`logic bug` **0**、`logic problem` **0**、`logic in` **0**。对照：`logic issue` **433**、`logic error` **18**、`improved logic` **61**、`additional logic` **33**、`permissions logic` **5**（附表 A 组 1）。

**4.3 这条词形结论的可执行含义。** 任何人用自己的缺陷分类词表去匹配这套公开文本，只要写了 `logic flaw|logic bug|logical` 这类常见变体，**匹配到的段数为 0**；只有裸 `logic` 命中。**"逻辑缺陷"在公开措辞层是一个单一 token，而不是一个词族**——这与 #122 的 `condition` 词族 ≡ `race condition`（双向差 0）是同一种现象：**类型词在顾问页语料里以固定套句存在，不产生派生词形**。

**4.4 `logic issue` ∶ `logic error` ＝ 433 ∶ 18 的两层含义。** 组 1 内部只有两种"类型名＋名词"搭配。拆开字段看：**Description 侧**含 `logic issue` 的 ＝ **423** 段、含 `logic error` 的 ＝ **18** 段（`logic error` 的 18 段全部来自同一句 `A logic error was addressed with improved error handling.`，附表 B 第 9 行）；而 433 － 423 ＝ **10**，正是第 3.2 节那 10 段——它们的 Impact 句也以 `A logic issue existed …` 开头。**423 ＋ 18 ＝ 441 恰好等于第 5 章的主语位段数，540 − 441 ＝ 99 恰好等于修复位段数**：即"类型名＋名词"式命名只出现在主语位，修复位的 `logic` 一律不带 `issue`／`error`。

# 5 句位刀：主语位 441 ∶ 修复位 99 ∶ 其他 0

**5.1 分类规则。** 对每条含 `logic` 的 Description 句判句位：以 `A logic`／`An logic` 开头者为**主语位**（类型名是句子主语的一部分）；含 `improved logic`／`additional logic`／`permissions logic` 者为**修复位**（类型名是修复手段的一部分）；两者皆非者为**其他**。

**5.2 测量。** 主语位 **441 段**、修复位 **99 段**、其他 **0 段**，合计 540（附表 B 合计行的句位列，生成器对此处三条分列有 `die` 断言）。441 来自 7 条句：226（improved checks）＋ 71（state management）＋ 47（restrictions）＋ 44（validation）＋ 32（file handling）＋ 18（error handling）＋ 3（memory corruption）＝ 441；99 来自 7 条句：33（adding additional logic）＋ 30（path handling…improved logic）＋ 22（validation issue…improved logic）＋ 6（URL protocols）＋ 4（improved permissions logic）＋ 3（camera metadata）＋ 1（resolved…permissions logic）＝ 99。**441 ＋ 99 ＝ 540 ＝ |Ld|**，且 540 − 441 ＝ 99 与第 4.4 节的 `logic issue`／`logic error` 拆分互为校验。

**5.3 句位与主语的耦合。** 主语位句式是"**类型名 → 修复手段**"（`A logic issue was addressed with X`），修复位句式是"**别的类型名 → 修复手段是 logic**"（`A path handling issue was addressed with improved logic`）。逐段核对 99 段修复位的主语：**主语写出其他类型名的 61 段**（`A path handling issue` 30 ＋ `A validation issue` 22 ＋ `An issue in the handling of URL protocols` 6 ＋ `An issue with app access to camera metadata` 3），**主语只写 `The issue` 的 38 段**（`addressed by adding additional logic` 33 ＋ `improved permissions logic` 4 ＋ `resolved with improved permissions logic` 1），**主语是 `A logic …` 的 0 段**。61 ＋ 38 ＝ 99 闭合。

**5.4 这条为什么重要。** 修复位把 `logic` 从"缺陷类型"改写成"修复手段"——**同一段文本里，`logic` 出现在主语位表示自述类型，出现在修复位表示自述手段**。任何按词频统计"逻辑缺陷条目数"的做法如果不分句位，就会把 99 段中 **61 段的类型名（path handling、validation issue、URL protocols、camera metadata）丢掉**，同时把 33 段"类型名缺失（`The issue`）"的条目算成逻辑缺陷。本篇第 7 章的反向刀正是按短语（而非按类型名）重切一遍。

# 6 句式动词刀：516 ＋ 33 ＋ 1 ＝ 550

**6.1 三个动词短语闭合。** Description 侧对 L 匹配：`was addressed with` **516** 段、`addressed by` **33** 段、`resolved with` **1** 段，合计 **550**。**没有第四种句式**。

**6.2 单复数与措辞约束。** `were addressed with` 全树 **8** 段，与 L 交 **0**——即 550 段的句式主语全部是单数（`A logic issue was…`、`The issue was…`）。`addressed` 词界∩L **549** ＝ 516＋33，差的 1 段是 `The issue was resolved with improved permissions logic.`（唯一用 `resolved` 的一段，也是附表 B 里唯一"resolved"句位的段）。

**6.3 其余套话落位。** `improved` 词界全树 6,038 段、∩L **517**（517 ＝ 516 ＋ 1，即除 `adding additional logic` 那 33 段外全部含 `improved`）；`additional` 词界全树 861 段、∩L **34**（其中 **33 段**是 `The issue was addressed by adding additional logic.` 一句，第 34 段的 `additional` 不在 Description 字段）；`This issue` 全树并集 1,166 段、∩L **9**（正是第 3.2 节那 9 段 `Messages`）；`fixed` 词界 **0**。

**6.4 与 #122 的句式对照。** #122 的 232 段句式**只有一种**（`A race condition was addressed with X`，句首词 distinct ＝ 1）；本篇的 550 段有**三种动词短语、两类句位、四种句首限定词**（`A logic issue`／`The issue`／`This issue`／`An issue in…`／`A path handling issue`／`A validation issue`／`A logic error`）。**逻辑缺陷在句式上比竞态更杂**，这与第 4 章"词形绝对单一"并不矛盾：**词形单一、句式多样**。

# 7 修复短语反向刀：四条 100% 独占、两条被稀释

**7.1 反向切法。** 不按类型名切，而按 Description 里的修复短语切（附表 C，八个短语）。每行给出**全树 Description 段数 ∶ L 内段数 ∶ 非 L 段数**与 L 占该短语的比例。

**7.2 四条 100% 独占。** `improved logic` **61/61**、`improved restrictions` **47/47**、`improved error handling` **18/18**、`improved permissions logic` **5/5**——这四个短语在全树只出现在 L 段里。**`improved restrictions` 尤其值得注意**：它的 47 段全部是 `A logic issue was addressed with improved restrictions.` 一句，且 47 段里有 **11 段命中 `SBX` 类、3 段 `FULL`、3 段 `PRI`**（附表 B 第 3 行），即"限制"是这一族的收束点。

**7.3 两条被稀释。** `improved checks` 全树 **1,018** 段，L 内只有 **236** 段（**23.2%**）；`improved state management` 全树 **662** 段，L 内 **74** 段（**11.2%**）；`improved validation` 全树 **366** 段，L 内 **44** 段（**12.0%**）。**"checks"是全树最大的修复套话，逻辑缺陷只占其四分之一**——反向刀的价值就在这里：**不能因为一条条目写了 improved checks 就把它算作逻辑缺陷**。#122 的 `improved state management` 662 段里竞态只有 11 段，是同一现象的另一侧。

**7.4 半独占。** `improved file handling` 全树 **38** 段、L 内 **32** 段（**84.2%**，非 L 的 6 段写在别的类型名上）。这一档说明"文件处理"类修复手段与逻辑缺陷高度重合但不完全。

**7.5 517 与 550 的差是 33。** 附表 C 八短语合计覆盖 L 的 **517** 段；`550 − 517 = 33` 恰是 `The issue was addressed by adding additional logic.` 一句的段数——它用的是 `addressed **by** adding`，不属八个 `improved X` 短语。**这不是遗漏，是短语族的边界**，附表 C 合计行因此标注"L 内段 517"而非 550。

**7.6 外部集列的行和与合计。** 附表 C 的 ∩R 行和 ＝ 54＋12＋15＋3＋4＋0＋0＋0 ＝ **88**，加 `adding additional logic` 那 33 段的 **11** 段才等于合计 **99**；∩M2 行和 ＝ 3，加同组的 **2** 段才等于合计 **5**；∩WO（12＋7＋5＝24）与 ∩E（9）两列行和即等于合计。读该表时**不要把行和当作集合基数**。

# 8 后果刀：228 类次 ∶ 550 段次，0 类 330，`MEM` 类 Impact 侧 0

**8.1 段级分布。** 对 550 段的 Impact 字段按第 1.3 节八类判据计类命中：0 类 **330** 段、1 类 **215** 段、2 类 **2** 段、3 类 **3** 段，合计 **550**；类次 **215＋2×2＋3×3 ＝ 228**。

**8.2 distinct 后果文本。** 550 段的 Impact 段全文 distinct ＝ **106** 条（含 `logic` 写在 Impact 的那 10 段）。最大的八条：`An app may be able to access user-sensitive data` **25**、`An app may be able to access sensitive user data` **24**、`Processing maliciously crafted web content may prevent Content Security Policy from being enforced` **21**、`An app may be able to break out of its sandbox` **19**、`An app may be able to access protected user data` **13**、`An app may be able to gain elevated privileges` **13**、`An app may bypass Gatekeeper checks` **13**、`An app may be able to elevate privileges` **12**。

**8.3 0 类是主体。** **330 段（占 L 的 60.0%）落在八类之外**，由 **68 条 distinct 全文**承载，最大的六条是 `An app may be able to access user-sensitive data`（**25** 段）、`An app may be able to access sensitive user data`（**24**）、`Processing maliciously crafted web content may prevent Content Security Policy from being enforced`（**21**）、`An app may be able to access protected user data`（**13**）、`An app may be able to modify protected parts of the file system`（**12**）、`An attacker in a privileged network position may be able to intercept network traffic`（**11**）。**"访问敏感数据"与"绕过内容安全策略"这两类最常见的逻辑缺陷后果，在本篇判据下不计入任何强度类**——这是判据选择的结果，第 22 章误读四号针对此。注意第 8.2 节列表里的第 4、6、7、8 条（`break out of its sandbox` 19、`gain elevated privileges` 13、`bypass Gatekeeper checks` 13、`elevate privileges` 12）**都命中了类**，所以不在 0 类清单中；而本节的 `modify protected parts of the file system`（12 段）与第 8.2 节的 `elevate privileges`（12 段）是**两句不同的、段数相同的文本**。

**8.4 `MEM` 类为 0，但 Description 里有 3 段写内存破坏。** Impact 侧 `MEM` 类 ∩ L ＝ **0**；同时附表 A 的 `MEM 类` 行在"三字段并集 ∩L"列给出 **3**——这 3 段是 `A logic issue existed resulting in memory corruption. This was addressed with improved state management.`（3 段、3 页、1 名），**`memory corruption` 写在 Description 而非 Impact**。两个数都对，差别只在字段口径；附表 A 的八类行用的是并集口径，附表 B 的八类列用的是 Impact 口径。

**8.5 ≥2 类只有两句。** 5 段的逐条实测：3 段 `An app may be able to execute arbitrary code out of its sandbox or with certain elevated privileges`（`ACE`＋`PRI`＋`SBX`，组件 `Audio`，页 `121839`／`121840`／`121842`）＋ 2 段 `An app may be able to bypass launch constraint protections and execute malicious code with elevated privileges`（`BYP`＋`PRI`，组件 `AppleMobileFileIntegrity`，页 `125110`／`125887`）。这与 #121 的 M2 集（185 段）相交为 **5**，即 **L 只占多类并写集的 2.7%**。

**8.6 与竞态的后果层对照。** #122 的 232 段类次 **149**（0 类 97 段／1 类 121／2 类 14）；本篇 550 段类次 **228**（0 类 330／1 类 215／2 类 2／3 类 3）。换算成"每段类次"：竞态 **0.64**、逻辑 **0.41**；"0 类占比"：竞态 **41.8%**、逻辑 **60.0%**。**逻辑缺陷条目在后果字段写得比竞态更弱**——这与它在页面覆盖（63.0%）远大于竞态（37.0%）形成反向关系。

# 9 在野刀：∩E ＝ 9，全部是 `Messages` 的同一条 iCloud Link 句

**9.1 测量。** 在野集 E（Impact 含 `exploit` 词族）**108 段** ∩ L ＝ **9 段**／9 页／1 名（`Messages`）。9 个页面是 `122173`、`122174`、`122345`、`122346`、`122900`、`122901`、`122902`、`122903`、`122904`，后果文本是同一句（见第 3.2 节）。**这 9 段正是 Impact 侧含 `logic` 的那 9 段。**

**9.2 反向核对。** E 的非 L 部分 ＝ **99 段／71 页／11 名**。作为对照，下表给出各类型词集（三字段并集口径）与在野集 E 的交集——**`race condition`（#122）、`integer overflow`、`buffer overflow` 三套为 0**，`logic` 与 `type confusion`、`privileg*` 同为 9：

| 类型词判据 | 并集段 | ∩E | ∩L |
|---|---:|---:|---:|
| `memory corruption` | 388 | 44 | 3 |
| `bypass` | 301 | 20 | 45 |
| `out-of-bounds` | 816 | 19 | 0 |
| `logic` | 550 | **9** | — |
| `type confusion` | 145 | 9 | 0 |
| `privileg*` | 558 | 9 | — |
| `use after free` | 127 | 8 | 0 |
| `sandbox` | 475 | 8 | 49 |
| `race condition` | 232 | **0** | 0 |
| `integer overflow` | 152 | **0** | 0 |
| `buffer overflow` | 200 | **0** | 0 |

**读法：`logic` 是与在野集有非零交的类型词之一，不是唯一**；但它是**两篇措辞层类型词研究（#122 竞态／本篇逻辑）里唯一"非零且在同一组件、同一句子"的样本**——竞态类是 0 段，本篇的 9 段是同一句话在 9 个页面上的重复呈现。本表所有"并集段"列一律用**词界判据**（`privileg*` ＝ `(?<![\w-])privileg\w*`，558 段；若改用裸子串匹配则是 575 段，两者 ∩E 都是 9）。

**9.3 `extremely sophisticated` 的口径。** 该串三字段并集 ＝ **59 段／42 页／8 名**，∩L ＝ **9**。所以"高级攻击"标注有 50 段不写在逻辑缺陷段上；**不能用 `extremely sophisticated` 反推逻辑缺陷**。

**9.4 这条负结论的读法边界。** ∩E ＝ 9 说明**公开文本只在 9 段里同时写出自述 logic 与自述被利用**。它不说明"逻辑缺陷被在野利用的比例是 8.3%（9/108）"或"逻辑缺陷最少被利用"——各类型词的字段落位与判据宽窄不同（第 22 章误读二号）。

# 10 类型词零交刀：十套零交、五套有交

**10.1 十套零交。** 与 L 的三字段并集**交集为 0** 的类型词集（括号为该集并集段数）：`race condition`（232）、`out-of-bounds`（816）、`use after free`（127）、`type confusion`（145）、`integer`（158）、`buffer`（200）、`overflow`（374）、`uninitialized`（16）、`double free`（43）、`dereference`（31）。**没有一段公开条目同时把缺陷自述为 logic 又自述为这十类之一。**

**10.2 五套有交。** `memory corruption`（388）→ **3**、`corrupt*` 词族（840）→ **3**、`memory` 词界（1,967）→ **8**、`bounds*` 词族（796）→ **8**、`leak*` 词族（192）→ **23**。**必须逐套表述**：`memory` 的 8 段是宽词界命中（含 `memory pressure`、`memory leak` 一类后果句），`memory corruption` 的 3 段是第 8.4 节那 3 句 Description。

**10.3 为什么会零交——字段方向表。** 同一判据在三个字段的落位（Impact／Description／致谢／并集）：

| 判据 | Impact | Description | 致谢 | 并集 | ∩L |
|---|---:|---:|---:|---:|---:|
| `memory corruption` | 218 | 184 | 0 | 388 | 3 |
| `out-of-bounds` | 14 | 813 | 5 | 816 | 0 |
| `type confusion` | 7 | 138 | 0 | 145 | 0 |
| `use after free` | 0 | 127 | 0 | 127 | 0 |
| `integer overflow` | 0 | 152 | 0 | 152 | 0 |
| `buffer overflow` | 2 | 199 | 0 | 200 | 0 |
| `race condition` | 0 | 232 | 0 | 232 | 0 |
| `logic` | 10 | 540 | 0 | 550 | — |
| `uninitialized` | 8 | 8 | 0 | 16 | 0 |
| `double free` | 0 | 43 | 0 | 43 | 0 |
| `dereference` | 0 | 31 | 0 | 31 | 0 |

**10.4 读法。** 零交是**"同段不并写两个类型名"的文本事实**，不是"逻辑缺陷不会造成越界读写"的因果事实。绝大多数内存类词**只写在 Description 的类型名位置**（`out-of-bounds` 813/816、`use after free` 127/127、`integer overflow` 152/152、`double free` 43/43、`dereference` 31/31 几乎全在 Description），而 `logic` 也主要在 Description——**同一位置只能有一个类型名，所以互斥是句式使然**。第 22 章误读五号针对把这条读成"两类缺陷无关"。

# 11 段级与句级对账：226 ∶ 236 的 10 段差

**11.1 问题的来源。** 附表 B（按 Description 含 `logic` 的句切，540 段）与附表 C／F（按三字段并集切，550 段）在多个同名量上给出不同得数。三处差值**全部来自第 3.2 节那 10 段**（它们不含 `logic` 的 Description）。

**11.2 差值表。**

| 量 | 附表 B 口径（Desc 侧 540） | 附表 C／F 口径（L-wide 550） | 差 | 差的来源 |
|---|---:|---:|---:|---|
| `improved checks` 归属段 | 226 | 236 | 10 | 9 段 `This issue…` ＋ 1 段 `The issue…` |
| ∩R（远程触达） | 99 | 99 | 0 | 这 10 段的 Impact 无通道词 |
| ∩WO（`without`） | 23 | 24 | 1 | `WindowServer` 的 `without user consent` |
| ∩E（在野） | 0 | 9 | 9 | 9 段 iCloud Link |
| ∩M2（≥2 类） | 5 | 5 | 0 | 这 10 段八类全不命中 |
| 八类类次 | 228 | 228 | 0 | 同上 |
| 0 类段数 | 320 | 330 | 10 | 同上 |
| 0 类 distinct 全文 | 66 | 68 | 2 | 上述两条 distinct 句子 |
| ∩RC（竞态） | 0 | 0 | 0 | — |

**11.3 结论。** **类次与 ∩R、∩M2 在两口径下相同，纯属这 10 段恰好八类全不命中、也不含通道词**。这是"得数相同 ≠ 同一组段落"的反向教材：**此处两个集合差 10 段却给出相同得数**，所以必须同时报 |A|、|B|、|A∩B| 并给出差段清单（第 11.2 节），不能只报得数。**若把这 10 段读成"Description 也含 logic"，附表 B 的 226 会莫名变成 236**，而两套 判据其实是句位与字段的不同切法。

**11.4 Description distinct 全文 16 条的两条双句文本。** `A logic issue existed resulting in memory corruption. This was addressed with improved state management.`（**3** 段）与 `An issue existed in the handling of snapshots. The issue was resolved with improved permissions logic.`（**1** 段）。其余 14 条为单句；14 条含 `logic` 的句子覆盖 540 段（附表 B），另 2 条不含 `logic` 的句子覆盖 10 段，**16 条闭合 550**。

# 12 镜像与切块自检

**12.1 字段完整性。** 550 段中**含非空 Impact 的 ＝ 550 段（100%）**；**含非空致谢的 ＝ 549 段**。唯一一段致谢为空、且同时没有任何 CVE 编号的，是页面 `127111`、组件 `Siri`、Description `A logic issue was addressed with improved checks.`、Impact `An app may be able to elevate privileges`（第 15.4 节）。**"Impact 全满、致谢缺一"本身就是这套语料的字段填充形状**：后果字段是必填项，报告人署名不是。

**12.2 同段词频。** 550 段里 `logic` **恰好出现 1 回**；出现 2 回的 ＝ **0 段**（附表 D 末行）。这条自检排除了"一段里既写 `A logic issue` 又写 `improved logic`"的可能，从而保证第 5 章的 441∶99 是**按段二分类**而不是重叠分类。

**12.3 版式域。** L 落在域 B（粗体版式）的 ＝ **4 段**，页面 `120304`、`120309`、`120311`、`120339`；其余 546 段在域 A。**与 #122 相反**——竞态在域 B 是 0 段。

**12.4 块切锚点。** 四锚点收块（第 1.1 节）在本篇的体现：若缺页脚声明锚点，页面免责声明会进入 Description 文本，附表 B 的 distinct 句数会虚增；本次生成中 14 条句全部是可读整句、无半句截断，且句位二分类闭合到 540（第 5 章），可作锚点未漂移的间接证据。

**12.5 空格归一。** `logic` 词界在 U+00A0／U+202F 存在处仍匹配；第 4.1 节的"词界 ≡ 子串"同时说明**没有连字符后缀**（`logic-based` 一类为 0）。

# 13 组件层：81／472 名，这 81 名的 Impact 全集 4,092 段的 13.4%

**13.1 表头约定。** 附表 F 每行是一个组件名（取段记录里的 `name` 字段，去首尾空格后精确匹配）；"L 段"＝该名的 L 段数；"页"＝该集所在页数；"该组件含 Impact 全集"＝**该组件在全树含 Impact 的段数**（占比分母）；末列 ＝ L 段 ÷ 该全集。**合计行的"该组件含 Impact 全集"列是 81 个组件各自全集的求和 ＝ 4,092 段，等于全树 7,434 段的 55.0%**，"L 占比"合计 ＝ 550／4,092 ＝ **13.4%**。

**13.2 头部组件。** `WebKit` **71** 段（其全集 910 段，7.8%）、`Kernel` **48**（681，7.0%）、`AppleMobileFileIntegrity` **20**（146，13.7%）、`Shortcuts` **20**（129，15.5%）、`libxpc` **19**（80，23.8%）、`CoreServices` **18**（81，22.2%）、`Security` **18**（91，19.8%）、`Sandbox` **15**（65，23.1%）、`ImageIO` **14**（261，5.4%）、`SharedFileList` **14**（39，35.9%）。**前两名 WebKit ＋ Kernel ＝ 119 段，占 L 的 21.6%。**

**13.3 外部集在组件层的分布。** ∩R 的 **99** 段分布在 **12 名**：`WebKit` 21、`Security` 15、`Kernel` 14、`Mail` 9、`Mail Drafts` 8、`CFNetwork` 7、`Shortcuts` 7、`Safari` 5、`Sandbox` 4、`DesktopServices` 3、`Screen Sharing Server` 3、`libnetcore` 3。∩WO 的 **24** 段分布在 **6 名**：`App Support` 7、`Shortcuts` 6、`Accounts` 5、`LaunchServices` 4、`Safari` 1、`WindowServer` 1。∩E 的 **9** 段全部在 `Messages`。**三个外部集在组件层的集中度都远高于 L 本身。**

**13.4 七个"全命中名"。** 若一名在 L 里的段数等于其全树含 Impact 全集，则该名的全部后果文本都是逻辑缺陷：`App Support` **7/7**、`Storage Services` **3/3**、`xar` **3/3**、`Siri Suggestions` **2/2**、`Swift` **2/2**、`Notifications` **1/1**、`Stolen Device Protection` **1/1**。**七个名字的分母最大是 7**，第 22 章误读八号针对把 100% 读成优先级。

**13.5 大分母低占比。** `Siri` 全集 116 段而 L 只 4 段（3.4%）、`Spotlight` 69∶3（4.3%）、`StorageKit` 59∶3（5.1%）、`Notes` 38∶1（2.6%）、`Accessibility` 46∶3（6.5%）、`WindowServer` 32∶1（3.1%）。**同一棵树里"逻辑缺陷条目多的组件"与"逻辑缺陷占比高的组件"不是同一批**，两条必须同报。

**附表 F 逻辑缺陷集 81 个组件名 × L 段 × 页 × ∩R ∩WO ∩E × 该组件含 Impact 全集 × L 占比（82 行，含合计行）：**

| 组件名 | L 段 | 页 | ∩R | ∩WO | ∩E | 该组件含 Impact 全集 | L 占比 |
|---|---:|---:|---:|---:|---:|---:|---:|
| `WebKit` | 71 | 51 | 21 | 0 | 0 | 910 | 7.8% |
| `Kernel` | 48 | 48 | 14 | 0 | 0 | 681 | 7.0% |
| `AppleMobileFileIntegrity` | 20 | 15 | 0 | 0 | 0 | 146 | 13.7% |
| `Shortcuts` | 20 | 14 | 7 | 6 | 0 | 129 | 15.5% |
| `libxpc` | 19 | 19 | 0 | 0 | 0 | 80 | 23.8% |
| `CoreServices` | 18 | 14 | 0 | 0 | 0 | 81 | 22.2% |
| `Security` | 18 | 18 | 15 | 0 | 0 | 91 | 19.8% |
| `Sandbox` | 15 | 13 | 4 | 0 | 0 | 65 | 23.1% |
| `ImageIO` | 14 | 14 | 0 | 0 | 0 | 261 | 5.4% |
| `SharedFileList` | 14 | 9 | 0 | 0 | 0 | 39 | 35.9% |
| `LaunchServices` | 12 | 10 | 0 | 4 | 0 | 58 | 20.7% |
| `Messages` | 12 | 12 | 0 | 0 | 9 | 50 | 24.0% |
| `Safari` | 11 | 10 | 5 | 1 | 0 | 92 | 12.0% |
| `Foundation` | 10 | 10 | 0 | 0 | 0 | 67 | 14.9% |
| `PackageKit` | 10 | 9 | 0 | 0 | 0 | 106 | 9.4% |
| `Audio` | 9 | 9 | 0 | 0 | 0 | 95 | 9.5% |
| `File Bookmark` | 9 | 6 | 0 | 0 | 0 | 15 | 60.0% |
| `Installer` | 9 | 9 | 0 | 0 | 0 | 29 | 31.0% |
| `Mail` | 9 | 9 | 9 | 0 | 0 | 36 | 25.0% |
| `MobileBackup` | 9 | 9 | 0 | 0 | 0 | 14 | 64.3% |
| `App Intents` | 8 | 8 | 0 | 0 | 0 | 16 | 50.0% |
| `Mail Drafts` | 8 | 8 | 8 | 0 | 0 | 11 | 72.7% |
| `libnetcore` | 8 | 8 | 3 | 0 | 0 | 11 | 72.7% |
| `mDNSResponder` | 8 | 8 | 0 | 0 | 0 | 53 | 15.1% |
| `Accounts` | 7 | 7 | 0 | 5 | 0 | 34 | 20.6% |
| `App Support` | 7 | 7 | 0 | 7 | 0 | 7 | 100.0% |
| `BackgroundAssets` | 7 | 7 | 0 | 0 | 0 | 13 | 53.8% |
| `CFNetwork` | 7 | 7 | 7 | 0 | 0 | 20 | 35.0% |
| `Admin Framework` | 6 | 6 | 0 | 0 | 0 | 12 | 50.0% |
| `Maps` | 6 | 6 | 0 | 0 | 0 | 23 | 26.1% |
| `RemoteViewServices` | 5 | 4 | 0 | 0 | 0 | 9 | 55.6% |
| `System Settings` | 5 | 5 | 0 | 0 | 0 | 30 | 16.7% |
| `sudo` | 5 | 5 | 0 | 0 | 0 | 10 | 50.0% |
| `Calendar` | 4 | 4 | 0 | 0 | 0 | 22 | 18.2% |
| `Control Center` | 4 | 4 | 0 | 0 | 0 | 8 | 50.0% |
| `Siri` | 4 | 3 | 0 | 0 | 0 | 116 | 3.4% |
| `Status Bar` | 4 | 4 | 0 | 0 | 0 | 5 | 80.0% |
| `Accessibility` | 3 | 2 | 0 | 0 | 0 | 46 | 6.5% |
| `AppSandbox` | 3 | 3 | 0 | 0 | 0 | 10 | 30.0% |
| `CUPS` | 3 | 3 | 0 | 0 | 0 | 43 | 7.0% |
| `Crash Reporter` | 3 | 3 | 0 | 0 | 0 | 24 | 12.5% |
| `DesktopServices` | 3 | 3 | 3 | 0 | 0 | 11 | 27.3% |
| `Finder` | 3 | 3 | 0 | 0 | 0 | 10 | 30.0% |
| `NSColorPanel` | 3 | 3 | 0 | 0 | 0 | 7 | 42.9% |
| `Screen Sharing Server` | 3 | 3 | 3 | 0 | 0 | 16 | 18.8% |
| `Spotlight` | 3 | 3 | 0 | 0 | 0 | 69 | 4.3% |
| `Storage Services` | 3 | 3 | 0 | 0 | 0 | 3 | 100.0% |
| `StorageKit` | 3 | 3 | 0 | 0 | 0 | 59 | 5.1% |
| `TCC` | 3 | 3 | 0 | 0 | 0 | 29 | 10.3% |
| `TV App` | 3 | 3 | 0 | 0 | 0 | 8 | 37.5% |
| `Time Zone` | 3 | 3 | 0 | 0 | 0 | 12 | 25.0% |
| `VoiceOver` | 3 | 3 | 0 | 0 | 0 | 14 | 21.4% |
| `autofs` | 3 | 3 | 0 | 0 | 0 | 6 | 50.0% |
| `quarantine` | 3 | 3 | 0 | 0 | 0 | 12 | 25.0% |
| `xar` | 3 | 3 | 0 | 0 | 0 | 3 | 100.0% |
| `zip` | 3 | 3 | 0 | 0 | 0 | 7 | 42.9% |
| `AppKit` | 2 | 2 | 0 | 0 | 0 | 9 | 22.2% |
| `Camera` | 2 | 2 | 0 | 0 | 0 | 3 | 66.7% |
| `Dock` | 2 | 2 | 0 | 0 | 0 | 19 | 10.5% |
| `Screenshots` | 2 | 2 | 0 | 0 | 0 | 7 | 28.6% |
| `Siri Suggestions` | 2 | 2 | 0 | 0 | 0 | 2 | 100.0% |
| `Software Update` | 2 | 2 | 0 | 0 | 0 | 18 | 11.1% |
| `Swift` | 2 | 2 | 0 | 0 | 0 | 2 | 100.0% |
| `WebDAV` | 2 | 2 | 0 | 0 | 0 | 20 | 10.0% |
| `BiometricKit` | 1 | 1 | 0 | 0 | 0 | 8 | 12.5% |
| `Call History` | 1 | 1 | 0 | 0 | 0 | 13 | 7.7% |
| `FrontBoard` | 1 | 1 | 0 | 0 | 0 | 6 | 16.7% |
| `Login Window` | 1 | 1 | 0 | 0 | 0 | 6 | 16.7% |
| `Networking` | 1 | 1 | 0 | 0 | 0 | 10 | 10.0% |
| `Notes` | 1 | 1 | 0 | 0 | 0 | 38 | 2.6% |
| `Notifications` | 1 | 1 | 0 | 0 | 0 | 1 | 100.0% |
| `Passwords` | 1 | 1 | 0 | 0 | 0 | 9 | 11.1% |
| `Perl` | 1 | 1 | 0 | 0 | 0 | 8 | 12.5% |
| `Quick Look` | 1 | 1 | 0 | 0 | 0 | 10 | 10.0% |
| `Safari Private Browsing` | 1 | 1 | 0 | 0 | 0 | 16 | 6.2% |
| `Setup Assistant` | 1 | 1 | 0 | 0 | 0 | 3 | 33.3% |
| `Stolen Device Protection` | 1 | 1 | 0 | 0 | 0 | 1 | 100.0% |
| `WindowServer` | 1 | 1 | 0 | 1 | 0 | 32 | 3.1% |
| `Xcode` | 1 | 1 | 0 | 0 | 0 | 3 | 33.3% |
| `copyfile` | 1 | 1 | 0 | 0 | 0 | 12 | 8.3% |
| `iTunes` | 1 | 1 | 0 | 0 | 0 | 2 | 50.0% |
| **合计** | 550 | 页列合计 498 | 99 | 24 | 9 | 4092 | 13.4% |

# 14 页与分布：162／257 页（63.0%），Top10 页承载 19.6%

**14.1 页分箱。** 含 L 段的页面 **162** 个（占 257 页的 **63.0%**）。按每页 L 段数分箱：**1 段 42 页、2 段 36、3 段 30、4 段 13、5 段 14、6–9 段 20、10–19 段 7**，合计 162；其中 **42＋36＋30 ＝ 108 页（66.7%）只贡献 1–3 段**，而最大的箱（10–19 段）只有 7 页。

**14.2 Top10 页。** `149035` **15** 段（该页全部 314 段，4.8%）、`121839` **14**（90，15.6%）、`120895` **11**（98，11.2%）、`125110` **11**（144，7.6%）、`126349` **10**（51，19.6%）、`126350` **10**（50，20.0%）、`149043` **10**（190，5.3%）、`121238` **9**（142，6.3%）、`122373` **9**（194，4.6%）、`122374` **9**（113，8.0%）。**Top10 合计 108 段 ＝ L 的 19.6%**，与"1 段页也有 42 个"并存——**分布同时是集中的和分散的**。

**14.3 同页共存。** 162 页里 **156 页同时含非 L 段**；只有 **6 页全部是 L 段**：`120887` 与 `122900`–`122904`（后者各 1 段，即第 9 章的在野页；前者 1 段）。这 162 页的全部段数 ＝ **8,172**，L 占 **6.7%**（第 1.4 节第三套分母）。

**14.4 与 #122 的页层对照。** 竞态 95／257 页（37.0%）、Top10 承载 **31.0%**；逻辑缺陷 162／257 页（63.0%）、Top10 承载 **19.6%**。**逻辑缺陷几乎铺满三分之二页面，但集中度更低**——"覆盖面最广的类型名"与"最集中的类型名"是两个不同的结论。

# 15 编号与致谢层：167 个编号独占、921 人次、均值 1.68

**15.1 编号分箱。** 550 段的 distinct CVE 编号数分箱：**0 个 1 段、1 个 536 段、2 个 9 段、3 个 3 段、4 个及以上 1 段**，闭合 550。**L 内 distinct 编号 ＝ 167**，全树 distinct ＝ **2,108**，L 独占 **7.9%**。

**15.2 编号归属。** 167 个编号中，**仅出现在 L 段的 ＝ 167**，**也出现在非 L 段的 ＝ 0**。即**逻辑缺陷条目的编号不与任何非逻辑缺陷条目共享**（同一编号在多页重复出现的"后补修复"情形，其重复段本身也是 L 段）。#122 的竞态 60 个编号同样 60 独占——**两套类型词的编号集互不相交**。

**15.3 致谢分箱。** 549 段有致谢，人数分箱：**1 人 354 段、2 人 113、3 人 39、4 人 21、5 人 5、6 人 5、7 人 11、8 人 1**，闭合 **549 段**；人次 **921**，均值 **1.68**，最大 **8**。全树 9,132 段的均值是 **1.80**、最大 **37**。**逻辑缺陷条目的致谢人数略低于全树平均，且远未达到全树最大致谢规模。**

**15.4 唯一的双空段。** 页面 `127111`（`Siri`）无编号、无致谢，是第 12.1 节字段完整性检查里唯一的例外；其 Description 是最大套话 `A logic issue was addressed with improved checks.`、Impact 是 `An app may be able to elevate privileges`（`PRI` 类 1 命中）。**说明"编号/致谢缺失"与"措辞存在"可以同时成立**——不能以有无编号反推条目是否自述类型。

# 16 与 #122 竞态的集合对账：零交、六句、15 个同名组件

**16.1 集合关系。** |RC| ＝ 232、|L| ＝ 550、**|RC ∩ L| ＝ 0**。两集并集 782 段（全树 8.56%）。**零交是逐段核对的结果**：把 232 段的 Description 与 Impact、540 段与 10 段的三字段全部匹配，无一段同时含 `race condition` 与 `logic`。

**16.2 六句交叉对账。** #122 的六条竞态修复短语，在本篇三字段口径下的重测（全树 Description 段 ∶ ∩RC ∶ ∩L）：

| Description 短语 | 全树 Desc 段 | ∩RC | ∩L |
|---|---:|---:|---:|
| `improved state handling` | 109 | 94 | **0** |
| `additional validation` | 112 | 83 | **0** |
| `improved locking` | 60 | 33 | **0** |
| `improved state management` | 662 | 11 | **74** |
| `improved handling of symbolic links` | 9 | 9 | **0** |
| `improved checks` | 1,018 | 2 | **236** |

**∩RC 列合计 232（六句互斥）；∩L 列合计 310**（＝ 74 ＋ 236），**不构成 550**——本篇另 240 段落在附表 C 的其余短语与 `adding additional logic` 上。**唯一被两族共用的短语是 `improved state management`（竞态 11 ∶ 逻辑 74）与 `improved checks`（竞态 2 ∶ 逻辑 236）。**

**16.3 组件层对账。** RC 40 名 ∶ L 81 名，**同名 15 名**，仅 L 66 名，仅 RC 25 名（40＋81−15 ＝ 106）。头名镜像：`Kernel` RC **71** ∶ L **48**（竞态偏内核）；`WebKit` RC **7** ∶ L **71**（逻辑缺陷偏 WebKit）；`CoreServices` 7 ∶ 18、`Sandbox` 4 ∶ 15、`Security` 1 ∶ 18、`Messages` 6 ∶ 12、`Shortcuts` 0 ∶ 20；反向：`StorageKit` 13 ∶ 3、`Graphics` 8 ∶ 0、`Compression` 6 ∶ 0、`CUPS` 3 ∶ 3。

**16.4 两族的同与异。** **同**：都只以单一类型名出现、都不写机制词、后果层都以 0 类为主体、编号都完全独占、`∩RC` 与 `∩L` 之间零交。**异**：字段落位（竞态 Impact 0 ∶ 逻辑 Impact 10 且互斥）、句式（竞态 1 种 ∶ 逻辑 3 种动词短语 ＋ 2 类句位）、页覆盖（37.0% ∶ 63.0%）、在野（0 ∶ 9）。**这两族合起来构成"措辞层类型词"的两个对照样本：一个窄而集中、一个宽而分散。**

# 17 与 #118／#119／#121 的接缝：∩R 99、∩WO 24、∩M2 5、∩G 87

**17.1 六个外部集的 L-wide 交集。** ∩R **99**／843、∩WO **24**／178、∩E **9**／108、∩G（动名词开头）**87**／2,179、∩HI（交互触发）**35**／273、∩M2（≥2 类）**5**／185、∩user **151**／1,627、∩RC **0**／232。**L 对多类并写集（M2）的占比只有 2.7%，对交互触发集（HI）的占比是 12.8%。**

**17.2 ∩G 的 87 段按动名词分。** `Processing` **44**、`Restoring` **10**、`Visiting` **9**、`Parsing` **8**、`Opening` **7**、`Turning` **4**、`Replying` **4**、`Enabling` **1**，合计 87。**`Processing` 一项占一半（44/87）**，且这 44 段逐条读组件为：`WebKit` **31**、`ImageIO` **6**、`libnetcore` **5**、`Safari` **2** ＝ 44——**全部四个名字都是输入解析类组件**，与 #116《被传递、处理或执行的数据》的口径直接相接。`Parsing` 的 8 段同样在 `ImageIO` 名下（第 8.2 节最大句之一 `Parsing an image may lead to disclosure of user information`）。

**17.3 ∩G 与 ∩HI 的组件分。** ∩G：**`WebKit` 39、`ImageIO` 14、`MobileBackup` 9、`Safari` 7、`libnetcore` 5、`Mail` 4、`Mail Drafts` 4、`DesktopServices` 3、`Setup Assistant` 1、`BiometricKit` 1**。∩HI：**`MobileBackup` 9、`WebKit` 8、`Safari` 5、`Mail` 4、`Mail Drafts` 4、`DesktopServices` 3、`Setup Assistant` 1、`BiometricKit` 1** ＝ 35。**`MobileBackup` 的 9 段全部是交互触发**，而 `ImageIO` 的 14 段全部不是（`Processing`／`Parsing` 被 #120 判为非交互）——**同一名组件在 G 与 HI 两集里的归属差别由动名词决定，不由组件决定**。

**17.4 ∩user 的 151 段。** distinct 后果句 **31** 条，最大的五条：`An app may be able to access sensitive user data` **24**、`An app may be able to access protected user data` **13**、`A sandboxed app may be able to access sensitive user data` **9**、`An attacker may be able to access user data` **9**、`A remote user may be able to cause a denial-of-service` **8**。**注意最后一条的 `user` 指攻击者而非受害者**——`user` 词界不能当作"影响真实用户"的判据，这是 #118 已记录的双义词问题在本篇的复现。

**17.5 ∩R 的 99 段。** 按附表 B 的 Description 句逐条分（这七项就是 ∩R 非零的七条句，另有七条句为 0）：`improved checks` 54、`improved state management` 12、`adding additional logic` 11、`path handling…improved logic` 7、`validation issue…improved logic` 8、`improved restrictions` 3、`improved validation` 4，**合计 99**。（若改按附表 C 的八个短语行看，行和是 88，差的 11 段属 `adding additional logic` 组——它不在那八个 `improved X` 短语内，见第 7.6 节。）**"逻辑缺陷可由远程输入触达"这一命题在公开文本里有 99 段支撑，占 L 的 18.0%**；其余 451 段的文本**没有写出通道**。

# 18 中文手册层：`逻辑` 4 次／2 页，且实测上下文全是硬件语汇

**18.1 判据与分母。** 《Apple 平台安全保护》zh-CN 转储 179 份中的 **178 页可用**（窗口切分后剔除 1 份无正文）；附表 E 七组 63 词，**全零 24 词、非零 39 词**。

**18.2 `逻辑` 的全部 4 次。** 页面 `secbbd20b00b`（《硬件麦克风断联》）**3 次**：「此部分的保护直接通过**硬件逻辑**按照如下电路图实施」、「该信号会通过一组简单的不可重新编程的**硬件逻辑**」、紧随其后的「**此逻辑**会在关闭麦克风前提供防抖、滞后和/或最高 500…」；页面 `sec28382c9ca`（《基于 Intel 的 Mac 中的固件密码保护》）**1 次**：「自行承担移除密码带来的**逻辑板**更换费用」。**四次全部是硬件语汇（电路逻辑、逻辑板），没有一次指软件缺陷类型。**

**18.3 派生词全零。** `逻辑缺陷`／`逻辑错误`／`逻辑问题`／`逻辑漏洞`／`推理`／`因果`／`状态机`／`失误`／`瑕疵`／`弱点`／`错误处理`／`文件处理`／`准入`／`配额`／`符号链接`／`软链接`／`硬链接`／`挂载`／`解包`／`提权`／`沙箱`／`竞态`／`竞争` 全部 **0 次**。

**18.4 对照非零词。** `验证` **432 次／178 页（100%）**、`限制` **478／178**、`内存` **511／178**、`处理` **337／178**、`确认` **249／178**、`权限` **283／178**、`检测` 77／35 页、`状态` 87／45 页、`检查` 66／30 页、`绕过` 23／9 页、`沙盒` 27／11 页、`错误` 27／16 页、`类型混淆` 2／1 页、`溢出` 1／1 页。**中文手册层有"验证／限制／处理／权限"这类手段语汇，但没有"逻辑"这一缺陷类型语汇**——与第 4 章"英文侧词形单一"、第 10 章"类型词互斥"共同构成三层文本对同一件事的不同表达强度。

**18.5 `检查` 与 `校验` 的分布值得单独记。** `检查` 66 次但只覆盖 **30 页**、最集中在《管理式 Apple 账户安全性》13 次；`校验` 19 次只覆盖 **3 页**、16 次集中在《Apple 钱包中证件的安全性》。**手册层的"检查"是账户管理语汇，不是 `improved checks` 的对应翻译**——把附表 C 的最大短语直接译成"改进检查"并引用手册佐证，属于跨层误用。

# 19 与相邻各篇的口径衔接

**19.1 与 #122。** 见第 16 章：零交、六句交叉表、15 个同名组件、Kernel∶WebKit 镜像。两篇共用同一套八类判据与同一套外部集，可直接对表。

**19.2 与 #121（组合缺陷）。** M2 集 185 段 ∩ L ＝ **5**；L 的多类并写只有两句（第 8.5 节），**逻辑缺陷条目几乎不参与"同段并写多类后果"**，因此"逻辑缺陷 ＋ 另一类"的组合在公开措辞层不可见（不可见 ≠ 不存在）。

**19.3 与 #118（远程触达）／#119（无前提）／#114（在野）。** 三集对 L 的交集分别 **99／24／9**（第 17.1、17.5、9 章）。`Messages` 的 9 段同时在 ∩E 与 Impact 侧 `logic` 集里，是**全套语料中唯一"类型名写进后果字段"的成规模样本**。

**19.4 与 #116／#120（动名词与交互触发）。** ∩G 87／∩HI 35，`Processing` 44 段是主要重合面；`MobileBackup` 9 段全属交互触发（第 17.3 节）。

**19.5 与手册层各篇（#103–#113）。** 英文 `improved restrictions` 47 段独占（第 7.2 节）对应中文手册层的"限制"478 次／178 页；**但两侧语义不相连**：手册的"限制"是功能描述，顾问页的 restrictions 是自述修复手段。第 18.5 节给出同型例子（`检查`）。

# 20 复算步骤与断言

**20.1 环境。** 在 `新建文件夹/` 目录下运行 Perl；镜像文件位于 `_tmp/adv2/<id>.html`（258 份，排除 `122085`）；中文转储位于 `_tmp/sec*.txt`（179 份，窗口切分后 178 页可用）。

**20.2 五步。**（1）`perl _tmp/tw123tab.pl`：生成 `_tmp/tw123A.md`…`_tmp/tw123F.md` 六个附表片段并打印锚点与闭合值；任一断言失败直接 die。（2）`perl _tmp/tw123i.pl`：字段方向表、零交／有交表、#122 六句对账、组件对账、在野与页／编号／致谢复核。（3）`perl _tmp/tw123j.pl`：段级类分布、≥2 类逐条、0 类 distinct 与最大句。（4）`perl _tmp/tw123k.pl`、`_tmp/tw123l.pl`、`_tmp/tw123m.pl`、`_tmp/tw123n.pl`、`_tmp/tw123o.pl`、`_tmp/tw123h.pl`：分别复算无编号／无致谢段、词界≡子串、外部集分解、两口径类分布、distinct 全文计数、Impact 侧 10 段原文。（5）`perl _tmp/tw123zh.pl`：中文层 `逻辑` 的全部 4 次上下文与可用页数。

**20.3 关键断言清单。** `|L| ＝ 550`；`|Li| ＝ 10`、`|Ld| ＝ 540`、`|La| ＝ 0`、`|Li ∩ Ld| ＝ 0`；八锚点集规模 `R 843／WO 178／UU 1,627／G 2,179／HI 273／M2 185／E 108／RC 232`；全树 `9,136 段／257 页／472 名／7,434 含 Impact／2,108 distinct 编号`；附表 B `14 行／540 段／重复 0／句位 441＋99＋0`；附表 C `2,215 ＝ 517 ＋ 1,698`；附表 D 页分箱 ＝ 162、编号分箱 ＝ 550、致谢分箱 ＝ 549；附表 F `81 行／550 段／全集合计 4,092`；类次 ＝ 228 且 `MEM` Impact 侧 ＝ 0；`∩E ＝ 9` 全在 `Messages`；`∩RC ＝ 0`；中文层 `逻辑 ＝ 4 次／2 页`、`逻辑缺陷 ＝ 0 次`。

**20.4 本篇不自证的部分。** 复算只能证明**文本分布**，不能证明任何一段逻辑缺陷条目的工程成因（状态机、检查—使用顺序、业务规则或其他）、可达性或可利用性；组件占比的分母是"该组件含 Impact 段数"，不是"该组件全部段数"，也不是"该组件漏洞总数"（第 13.1 节）。

# 21 缺口清单

**21.1 文本没给的。** `logic` 的具体对象（哪一段判定、哪一个分支、哪一条业务规则）、`improved logic` 的 61 段里**没有任何一段写出新增了什么逻辑**；`A logic issue existed resulting in memory corruption.`（3 段）**写出了后果类别却没写机制**。**这些不是可以补测的量，是公开文本没有的位置。**

**21.2 判据留下的模糊。** `improved checks` 与 `improved validation` 在语义上重叠（1,018 段 ∶ 366 段，L 内 236 ∶ 44），二者与 `improved restrictions`（47 段独占）之间的区别**无法从文本恢复**；第 22 章误读三号针对把三者合并统计。

**21.3 需要外部知识才能填的。** 167 个独占编号对应的产品版本与组件归属；`App Support` 7/7 与 `xar` 3/3 这类全命中名的成因；域 B 粗体版式为何只有 4 段逻辑缺陷条目。**本篇一律不做推断。**

**21.4 可以补测但本篇未测的。** `improved restrictions` 的 47 段与 #111《不同信任等级之间的访问限制》的集合关系；`logic` 与 `consent`（词界并集 76 段、∩L 8）、`permission*`（词界＋后缀并集 580 段、∩L 16；`permission` 单数词形只 55 段、∩L 11）三个授权词族的逐段共现结构；`path handling`（附表 B 第 7 行 30 段）与 #113 路径层判据的重合度。

# 22 常见误读纠正

**误读一：550 段说明"逻辑缺陷是最多的弱点类型"。** 正：550 是**一个词形（`logic`）在三字段并集上的段数**。`out-of-bounds` 816 段、`memory` 1,967 段、`bypass` 301 段各自判据宽窄与字段落位都不同（第 10.3 节），横向比大小必须先把判据统一到同一字段集合，且各判据的派生词覆盖率不同（`logic` 无派生词，`overflow` 是裸词界）。

**误读二：∩E ＝ 9 说明逻辑缺陷被在野利用的比例是 8.3%。** 正：9／108 的分母是"在野标注段全集"，9／550 的分母是"自述 logic 的段全集"，两个比都不估计任何总体比例；且这 9 段是同一组件同一条句子的 9 次重复呈现（9 页）。

**误读三：`improved checks`、`improved validation`、`improved restrictions` 是同义套话，可以合并。** 正：合并 L 内段得 236＋44＋47 ＝ 327，但三者的全树分母差 **1,018 ∶ 366 ∶ 47**，其中只有 `improved restrictions` 是 L 独占（47/47）。合并会把 1,018 段的主体误算进逻辑缺陷。附表 C 全程分列。

**误读四：60.0% 的段"后果类为 0"，所以这些条目没写后果。** 正：0 类 ＝ **本篇八类判据不命中**，而这些段的 Impact 全部非空（第 12.1 节）。最大两句 `access user-sensitive data`（25 段）与 `access sensitive user data`（24 段）是明确的数据访问后果，只因判据要求 `disclos*|leak*|expos*` 而不计类。换判据会得到不同分布，第 1.3 节把判据字面写出来正是为了允许换算。

**误读五：L 与十套类型词零交，所以逻辑缺陷与内存破坏无关。** 正：零交只关于"同段是否并写两个类型名"（第 10.4 节：同一字段位置只有一个类型名槽位）。`memory corruption` 与 L 的交是 **3**、`memory` 词界是 **8**、`leak*` 是 **23**——**有交但极小，且这 3 段的 memory corruption 写在 Description**。

**误读六：`MEM` 类在 L 里是 0，与"有 3 段写 memory corruption"矛盾。** 正：两数口径不同——0 是**Impact 单字段**（附表 B 列），3 是**三字段并集**（附表 A 行）。第 8.4 节。

**误读七：Impact 侧 10 段 ∪ Description 侧 540 段 ＝ 550，所以"有 10 段两头都写"。** 正：**恰恰相反，两个字段互斥（交 0 段）**，那 10 段的 Description 全都不含 `logic`（第 3.1、3.2 节）。550 ＝ 10 ＋ 540 成立**只因为交为 0**。

**误读八：`App Support` 7/7、`xar` 3/3 说明这些组件"全是逻辑缺陷"。** 正：分母是**该组件含 Impact 的段数**，最大只有 7 段（第 13.4 节）；同一列里 `Siri` 是 4/116（3.4%）、`Notes` 是 1/38（2.6%）。**比例与绝对数必须同报，小分母的 100% 不构成优先级。**

**误读九：中文手册提到"逻辑"，所以中文文档体系承认逻辑缺陷这一类。** 正：`逻辑` 的 4 次全部实测为硬件语汇（硬件逻辑／逻辑板），`逻辑缺陷`／`逻辑错误`／`逻辑漏洞` **0 次**（第 18.2 节）。引用次数而不引用上下文，会把 4 次读成分类承认。

# 23 结论

**一、这一类弱点在公开文本里的位置是"Description 为主、Impact 有独占 10 段"。** Impact **10**、Description **540**、致谢 **0**，两字段**互斥**（交 0），并集 **550 段／162 页（63.0%）／81 名（17.2%）**。那 10 段是全语料中唯一把类型名写进后果字段的成规模样本，且 9 段就是 `Messages` 的在野条目。

**二、这一类的词形绝对单一，句位分两种。** `logic` 词界 ≡ 子串（差 0）、同段恰 1 回、`logical`／`logically`／`logic flaw`／`logic bug`／`logic problem`／`logic in` 全零；主语位 **441** ∶ 修复位 **99** ∶ 其他 **0**；句式动词三选一闭合 **516 ＋ 33 ＋ 1 ＝ 550**；Description 全文 distinct 仅 **16** 条，最大一条 **226 段（41.1%）**。**类型名可以被机器一次性抓全，但 99 段的"逻辑"其实说的是修复手段。**

**三、这一类在后果层没有身份。** **106 条 distinct 后果全文**、**68 条不命中任何强度类（覆盖 330 段＝60.0%）**、类次 **228**（每段 0.41，低于竞态的 0.64）、`MEM` 类 Impact 侧 **0**、≥2 类只有 **5 段两句**。与八个外部集的交集为 ∩R 99、∩WO 24、∩E 9、∩G 87、∩HI 35、∩M2 5、∩user 151、∩RC **0**。

**四、这一类与其他类型词在文本层互斥。** **十套零交**（`race condition`、`out-of-bounds`、`use after free`、`type confusion`、`integer`、`buffer`、`overflow`、`uninitialized`、`double free`、`dereference`）、**五套有交但极小**（3／3／8／8／23）。550 段含编号 **167 个且 167 个独占**、致谢 **921 人次／均值 1.68**（低于全树 1.80）。

**五、中文手册层没有这一类。** `逻辑` 仅 **4 次／2 页且全为硬件语汇**，`逻辑缺陷`／`逻辑错误`／`逻辑问题`／`逻辑漏洞`／`状态机` **0 次**，而 `验证`／`限制`／`内存`／`处理`／`权限` 均覆盖 **178／178 页**。**英文侧"类型名单一但存在"，中文侧"连类型名都不存在"——两侧共同说明：公开文档体系不承担把缺陷归入"逻辑"这一类的职责，它只承担给出一个修复短语。**

**六、可执行的防御含义（不涉及任何利用）。** 对内部缺陷库而言：**`logic` 是覆盖面最广的类型名，但它的信息量集中在修复短语而不是机制**——1,018 段的 `improved checks` 只有 236 段是逻辑缺陷，而 47 段的 `improved restrictions` 全部是逻辑缺陷；因此**以修复短语为键做公开信息抽取时，四条独占短语（logic／restrictions／error handling／permissions logic）可用于高精度召回，两条大套话（checks／state management）只能用于宽召回并必须回查 Description 主语位**。对组件优先级而言，本语料里逻辑缺陷段的 **21.6% 落在 WebKit ＋ Kernel 两名**、**18.0% 有远程触达标注（∩R 99）**、**12 名承载全部 ∩R**、**6 名承载全部 ∩WO**——**这条分布是措辞层的观测，用于校准"公开信息能告诉我们什么"，不能替代资产侧的真实风险评估。**

---

# 附表 A 逻辑判据六组电池（69 行）

**读法：** 列名"Impact 段／页／名"三列是该判据在 **Impact 单字段**的命中规模；"三字段并集段"＝ Impact∪Description∪致谢；"∩L"列是**该判据的并集**与 L 的交集。注意"∩R／∩WO／∩M2"三列按**该判据的 Impact 侧段集合**与外部集求交——所以对只在 Description 出现的判据（`race condition`、`improved checks` 一类）这三列必为 0，这是列定义而非结论。组 1 的前 4 行给出字段刀（第 3 章）与词形刀（第 4 章）的原始数据；组 1 末两行 `logical`／`logically` 与组 5 的 `fixed`／`internally reported`／`externally reported` 属"全零 9 项"。组 2、组 3 是校验／状态族与 L 的近邻关系（`check 词族` ∩L 252、`restrict* 词族` ∩L 67、`state 词界` ∩L 74）；组 4 是第 10 章十五套类型词的原始行；组 5 是第 6 章句式动词的原始行；组 6 的八类 ∩L 列合计 **231** 含 `MEM` 的并集口径 3，而 Impact 口径是 **228**（第 8.4 节）。合计行"Impact 列合计 7,601"是 68 项各自 Impact 命中数的求和，不是任何集合的基数。

| 组 | 判据 | Impact 段 | 页 | 名 | 三字段并集段 | ∩L | ∩R | ∩WO | ∩M2 |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|
| 逻辑本体词 | `logic 词界` | 10 | 10 | 2 | 550 | 550 | 0 | 1 | 0 |
| 逻辑本体词 | `logic issue` | 10 | 10 | 2 | 433 | 433 | 0 | 1 | 0 |
| 逻辑本体词 | `logic error` | 0 | 0 | 0 | 18 | 18 | 0 | 0 | 0 |
| 逻辑本体词 | `logic flaw` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 逻辑本体词 | `logic bug` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 逻辑本体词 | `logic problem` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 逻辑本体词 | `logic in` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 逻辑本体词 | `improved logic` | 0 | 0 | 0 | 61 | 61 | 0 | 0 | 0 |
| 逻辑本体词 | `additional logic` | 0 | 0 | 0 | 33 | 33 | 0 | 0 | 0 |
| 逻辑本体词 | `permissions logic` | 0 | 0 | 0 | 5 | 5 | 0 | 0 | 0 |
| 逻辑本体词 | `logical 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 逻辑本体词 | `logically 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 校验与检查词 | `check 词族` | 51 | 24 | 17 | 2017 | 252 | 1 | 4 | 0 |
| 校验与检查词 | `improved checks` | 0 | 0 | 0 | 1018 | 236 | 0 | 0 | 0 |
| 校验与检查词 | `validation 词界` | 25 | 14 | 6 | 1341 | 66 | 0 | 0 | 0 |
| 校验与检查词 | `validat* 词族` | 25 | 14 | 6 | 1341 | 66 | 0 | 0 | 0 |
| 校验与检查词 | `improved validation` | 4 | 4 | 2 | 367 | 44 | 0 | 0 | 0 |
| 校验与检查词 | `verify 词族` | 0 | 0 | 0 | 9 | 0 | 0 | 0 | 0 |
| 校验与检查词 | `restrict* 词族` | 130 | 79 | 29 | 793 | 67 | 41 | 3 | 13 |
| 校验与检查词 | `improved restrictions` | 0 | 0 | 0 | 47 | 47 | 0 | 0 | 0 |
| 校验与检查词 | `permission 词族` | 57 | 48 | 13 | 580 | 16 | 15 | 18 | 0 |
| 校验与检查词 | `entitlement 词族` | 7 | 7 | 3 | 131 | 0 | 0 | 4 | 0 |
| 校验与检查词 | `allow* 词族` | 13 | 13 | 3 | 13 | 0 | 0 | 1 | 0 |
| 校验与检查词 | `consent 词界` | 54 | 47 | 15 | 76 | 8 | 14 | 54 | 0 |
| 状态与处理词 | `state 词界` | 76 | 62 | 6 | 866 | 74 | 0 | 0 | 0 |
| 状态与处理词 | `state handling` | 0 | 0 | 0 | 109 | 0 | 0 | 0 | 0 |
| 状态与处理词 | `state management` | 0 | 0 | 0 | 662 | 74 | 0 | 0 | 0 |
| 状态与处理词 | `improved state management` | 0 | 0 | 0 | 662 | 74 | 0 | 0 | 0 |
| 状态与处理词 | `handling 词族` | 4 | 4 | 2 | 1458 | 87 | 0 | 0 | 0 |
| 状态与处理词 | `file handling` | 0 | 0 | 0 | 38 | 32 | 0 | 0 | 0 |
| 状态与处理词 | `error handling` | 0 | 0 | 0 | 18 | 18 | 0 | 0 | 0 |
| 状态与处理词 | `improved error handling` | 0 | 0 | 0 | 18 | 18 | 0 | 0 | 0 |
| 状态与处理词 | `process 词界` | 756 | 156 | 44 | 764 | 18 | 50 | 1 | 17 |
| 状态与处理词 | `snapshot 词界` | 0 | 0 | 0 | 1 | 1 | 0 | 0 | 0 |
| 内存与边界词 | `memory 词界` | 1136 | 174 | 72 | 1967 | 8 | 76 | 0 | 51 |
| 内存与边界词 | `memory corruption 连写` | 218 | 85 | 16 | 388 | 3 | 0 | 0 | 2 |
| 内存与边界词 | `corrupt* 词族` | 682 | 151 | 48 | 840 | 3 | 89 | 0 | 2 |
| 内存与边界词 | `overflow 词界` | 2 | 1 | 1 | 374 | 0 | 0 | 0 | 2 |
| 内存与边界词 | `buffer 词界` | 2 | 1 | 1 | 200 | 0 | 0 | 0 | 2 |
| 内存与边界词 | `out-of-bounds` | 14 | 11 | 3 | 816 | 0 | 8 | 0 | 0 |
| 内存与边界词 | `use after free` | 0 | 0 | 0 | 127 | 0 | 0 | 0 | 0 |
| 内存与边界词 | `type confusion` | 7 | 7 | 1 | 145 | 0 | 0 | 0 | 0 |
| 内存与边界词 | `integer 词界` | 0 | 0 | 0 | 158 | 0 | 0 | 0 | 0 |
| 内存与边界词 | `bounds* 词族` | 8 | 8 | 1 | 796 | 8 | 0 | 0 | 0 |
| 内存与边界词 | `uninitialized 词族` | 8 | 8 | 1 | 16 | 0 | 0 | 0 | 0 |
| 内存与边界词 | `double free` | 0 | 0 | 0 | 43 | 0 | 0 | 0 | 0 |
| 内存与边界词 | `leak 词族` | 181 | 101 | 29 | 192 | 23 | 63 | 0 | 0 |
| 内存与边界词 | `dereference 词族` | 0 | 0 | 0 | 31 | 0 | 0 | 0 | 0 |
| 修复套话与来源词 | `was addressed with` | 4 | 4 | 2 | 6581 | 516 | 0 | 0 | 0 |
| 修复套话与来源词 | `were addressed with` | 0 | 0 | 0 | 8 | 0 | 0 | 0 | 0 |
| 修复套话与来源词 | `addressed by` | 0 | 0 | 0 | 371 | 33 | 0 | 0 | 0 |
| 修复套话与来源词 | `resolved with` | 0 | 0 | 0 | 1 | 1 | 0 | 0 | 0 |
| 修复套话与来源词 | `addressed 词界` | 25 | 14 | 6 | 7228 | 549 | 0 | 0 | 0 |
| 修复套话与来源词 | `improved 词界` | 4 | 4 | 2 | 6038 | 517 | 0 | 0 | 0 |
| 修复套话与来源词 | `additional 词界` | 0 | 0 | 0 | 861 | 34 | 0 | 0 | 0 |
| 修复套话与来源词 | `fixed 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 修复套话与来源词 | `resolved 词界` | 0 | 0 | 0 | 19 | 1 | 0 | 0 | 0 |
| 修复套话与来源词 | `This issue` | 103 | 74 | 12 | 1166 | 9 | 4 | 0 | 0 |
| 修复套话与来源词 | `internally reported` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 修复套话与来源词 | `externally reported` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 后果强度八类 | `ACE 类` | 336 | 120 | 43 | 336 | 10 | 36 | 0 | 118 |
| 后果强度八类 | `BYP 类` | 291 | 114 | 56 | 301 | 45 | 42 | 0 | 16 |
| 后果强度八类 | `DIS 类` | 530 | 144 | 63 | 603 | 36 | 63 | 0 | 49 |
| 后果强度八类 | `DOS 类` | 2003 | 176 | 130 | 2014 | 42 | 335 | 0 | 143 |
| 后果强度八类 | `FULL 类` | 63 | 43 | 18 | 63 | 12 | 7 | 0 | 0 |
| 后果强度八类 | `MEM 类` | 220 | 85 | 17 | 390 | 3 | 0 | 0 | 2 |
| 后果强度八类 | `PRI 类` | 124 | 62 | 30 | 124 | 34 | 0 | 0 | 21 |
| 后果强度八类 | `SBX 类` | 418 | 119 | 67 | 475 | 49 | 28 | 3 | 39 |
| **合计** | 68 项（全零 9 项） | — | — | — | — | 550 | — | — | 185 |

# 附表 B 十四条含 logic 的 Description 句 × 句位 × 五个外部集 × 后果八类（15 行）

**读法：** 本表按 **Description 侧含 `logic` 的段（540）**切，行与行互斥（重复计入 0），段列合计 **540**。"logic 句位"列给出第 5 章的二分类，合计行写作"主语位 441 段／修复位 99 段／其他 0 段"。**页列合计 368、名列合计 123 是逐行去重后再求和的值，大于真实基数 162 页／81 名，不可当集合基数引用**；本表页列不含 Impact 侧那 10 段所在的页。外部集五列合计 ∩R **99**、∩WO **23**、∩E **0**、∩M2 **5**、∩RC **0**，其中 ∩R 的行和（54＋12＋3＋4＋11＋7＋8＝99）与 ∩WO 的行和（11＋5＋7＝23）经生成器断言与整集求交相等。八类列合计 **228 类次**（每段可命中多类，`MEM` 列 **0**）。第 1 行 `A logic issue was addressed with improved checks.`（226 段、114 页、41 名、类次 116）一条就占 L 的 **41.1%**；第 7 行 `A path handling issue was addressed with improved logic.`（30 段）是 FULL 类 7 段的唯一来源；第 8 行 `A validation issue was addressed with improved logic.`（22 段）贡献 ACE 7 段。

| Description 含 logic 句 | 段 | 页 | 名 | logic 句位 | ∩R | ∩WO | ∩E | ∩M2 | ∩RC | ACE | BYP | DIS | DOS | FULL | MEM | PRI | SBX |
|---|---:|---:|---:|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| A logic issue was addressed with improved checks. | 226 | 114 | 41 | 主语位 | 54 | 11 | 0 | 3 | 0 | 3 | 17 | 21 | 16 | 0 | 0 | 28 | 31 |
| A logic issue was addressed with improved state management. | 71 | 49 | 16 | 主语位 | 12 | 0 | 0 | 0 | 0 | 0 | 5 | 7 | 6 | 0 | 0 | 1 | 0 |
| A logic issue was addressed with improved restrictions. | 47 | 37 | 12 | 主语位 | 3 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 3 | 0 | 3 | 11 |
| A logic issue was addressed with improved validation. | 44 | 35 | 13 | 主语位 | 4 | 0 | 0 | 0 | 0 | 0 | 12 | 0 | 0 | 1 | 0 | 0 | 3 |
| The issue was addressed by adding additional logic. | 33 | 28 | 11 | 修复位 | 11 | 0 | 0 | 2 | 0 | 0 | 2 | 0 | 0 | 0 | 0 | 2 | 3 |
| A logic issue was addressed with improved file handling. | 32 | 23 | 11 | 主语位 | 0 | 5 | 0 | 0 | 0 | 0 | 3 | 0 | 1 | 1 | 0 | 0 | 1 |
| A path handling issue was addressed with improved logic. | 30 | 25 | 6 | 修复位 | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 7 | 0 | 0 | 0 |
| A validation issue was addressed with improved logic. | 22 | 22 | 3 | 修复位 | 8 | 0 | 0 | 0 | 0 | 7 | 0 | 0 | 8 | 0 | 0 | 0 | 0 |
| A logic error was addressed with improved error handling. | 18 | 18 | 4 | 主语位 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 8 | 8 | 0 | 0 | 0 | 0 |
| An issue in the handling of URL protocols was addressed with improved logic. | 6 | 6 | 1 | 修复位 | 0 | 0 | 0 | 0 | 0 | 0 | 6 | 0 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved permissions logic. | 4 | 4 | 2 | 修复位 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| A logic issue existed resulting in memory corruption. | 3 | 3 | 1 | 主语位 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 3 | 0 | 0 | 0 | 0 |
| An issue with app access to camera metadata was addressed with improved logic. | 3 | 3 | 1 | 修复位 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| The issue was resolved with improved permissions logic. | 1 | 1 | 1 | 修复位 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| **合计** | 540 | 页列合计 368 | 名列合计 123 | 主语位 441 段／修复位 99 段／其他 0 段 | 99 | 23 | 0 | 5 | 0 | 10 | 45 | 36 | 42 | 12 | 0 | 34 | 49 |

# 附表 C Description 修复短语反向刀（9 行）

**读法：** 本表按 **Description 短语**切，给出全树该短语的 Description 段数 ∶ 其中 L 内段 ∶ 非 L 段，用于第 7 章"独占／稀释"的判读。八个短语的行段合计 **2,215**、L 内 **517**、非 L **1,698**，L 占 **23.3%**。**517 不等于 550**：差的 33 段是 `The issue was addressed by adding additional logic.`，它用的是 `addressed by`，不属这八个 `improved X` 短语。外部集四列（∩R 99、∩WO 24、∩E 9、∩M2 5）与 ∩RC 0 均按**整段 L（550）口径**给出，因此 ∩R 与 ∩M2 两列的**行和（88 与 3）小于合计**，缺的 11 与 2 段属上述 33 段（第 7.6 节）。页列合计 339、名列合计 113 是逐行去重后求和，不可当集合基数。四行 100.0% 是独占判据，两行 11–12% 是稀释判据。

| Description 修复短语 | 全树 Desc 段 | L 内段 | 非 L 段 | L 占该短语 | 页 | 名 | ∩R | ∩WO | ∩E | ∩M2 | ∩RC |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| `improved checks` | 1018 | 236 | 782 | 23.2% | 123 | 43 | 54 | 12 | 9 | 3 | 0 |
| `improved state management` | 662 | 74 | 588 | 11.2% | 52 | 17 | 12 | 0 | 0 | 0 | 0 |
| `improved logic` | 61 | 61 | 0 | 100.0% | 46 | 10 | 15 | 7 | 0 | 0 | 0 |
| `improved restrictions` | 47 | 47 | 0 | 100.0% | 37 | 12 | 3 | 0 | 0 | 0 | 0 |
| `improved validation` | 366 | 44 | 322 | 12.0% | 35 | 13 | 4 | 0 | 0 | 0 | 0 |
| `improved file handling` | 38 | 32 | 6 | 84.2% | 23 | 11 | 0 | 5 | 0 | 0 | 0 |
| `improved error handling` | 18 | 18 | 0 | 100.0% | 18 | 4 | 0 | 0 | 0 | 0 | 0 |
| `improved permissions logic` | 5 | 5 | 0 | 100.0% | 5 | 3 | 0 | 0 | 0 | 0 | 0 |
| **合计** | 2215 | 517 | 1698 | 23.3% | 页列合计 339 | 名列合计 113 | 99 | 24 | 9 | 5 | 0 |

# 附表 D 页面、编号与致谢四组分箱（41 行）

**读法：** 第一块是每页 L 段数的分箱，闭合到 **162 页**（"合计"行给出校验）；第二块 Top10 页逐页给出该页全部段数与 L 占比（4.6%–20.0%），合计 **108 段 ＝ 19.6%**；第三块是每段 distinct CVE 数分箱，闭合到 **550 段**，配合"编号归属"两行（**167 独占 ∶ 0 共享**）；第四块是致谢人数分箱，闭合到 **549 段／921 人次**，均值 **1.68**、最大 **8**，末行与全树 9,132 段（均值 1.80、最大 37）对照。末三行是版式域与字段落位的负结论：域 B 内 **4 段**、致谢字段含 `logic` **0 段**、同段 `logic` 出现 2 回的 **0 段**。所有"合计"行都参与本附表行数统计，阅读时请勿把它们当作数据条目。

| 块 | 分箱 | 计数 | 说明 |
|---|---|---:|---|
| L 段所在页的段数 | 1 | 42 | 共 162 页含 logic 条目 |
| L 段所在页的段数 | 2 | 36 | 共 162 页含 logic 条目 |
| L 段所在页的段数 | 3 | 30 | 共 162 页含 logic 条目 |
| L 段所在页的段数 | 4 | 13 | 共 162 页含 logic 条目 |
| L 段所在页的段数 | 5 | 14 | 共 162 页含 logic 条目 |
| L 段所在页的段数 | 6-9 | 20 | 共 162 页含 logic 条目 |
| L 段所在页的段数 | 10-19 | 7 | 共 162 页含 logic 条目 |
| L 段所在页的段数 | 合计 | 162 | 校验=162 页、L 段 550 |
| Top10 页逐页 | 149035 | 15 | 该页全部段 314 段，L 占 4.8% |
| Top10 页逐页 | 121839 | 14 | 该页全部段 90 段，L 占 15.6% |
| Top10 页逐页 | 120895 | 11 | 该页全部段 98 段，L 占 11.2% |
| Top10 页逐页 | 125110 | 11 | 该页全部段 144 段，L 占 7.6% |
| Top10 页逐页 | 126349 | 10 | 该页全部段 51 段，L 占 19.6% |
| Top10 页逐页 | 126350 | 10 | 该页全部段 50 段，L 占 20.0% |
| Top10 页逐页 | 149043 | 10 | 该页全部段 190 段，L 占 5.3% |
| Top10 页逐页 | 121238 | 9 | 该页全部段 142 段，L 占 6.3% |
| Top10 页逐页 | 122373 | 9 | 该页全部段 194 段，L 占 4.6% |
| Top10 页逐页 | 122374 | 9 | 该页全部段 113 段，L 占 8.0% |
| Top10 页逐页 | 合计 | 108 | 占 L 段 19.6%（550 段／162 页） |
| L 段内 distinct CVE 数 | 0 | 1 | 550 段中 549 段含编号 |
| L 段内 distinct CVE 数 | 1 | 536 | 550 段中 549 段含编号 |
| L 段内 distinct CVE 数 | 2 | 9 | 550 段中 549 段含编号 |
| L 段内 distinct CVE 数 | 3 | 3 | 550 段中 549 段含编号 |
| L 段内 distinct CVE 数 | 4+ | 1 | 550 段中 549 段含编号 |
| L 段内 distinct CVE 数 | 合计 | 550 | 校验=550；全树 distinct 编号 2108 个，L 内 167 个 |
| 编号归属 | 仅出现在 L 段 | 167 | 这 167 个编号不出现在其余 8,586 段 |
| 编号归属 | 也出现在非 L 段 | 0 | 与 #122 竞态的 60 个编号无共享 |
| L 段致谢人数 | 1 | 354 | L 合计 921 人、均值 1.68 |
| L 段致谢人数 | 2 | 113 | L 合计 921 人、均值 1.68 |
| L 段致谢人数 | 3 | 39 | L 合计 921 人、均值 1.68 |
| L 段致谢人数 | 4 | 21 | L 合计 921 人、均值 1.68 |
| L 段致谢人数 | 5 | 5 | L 合计 921 人、均值 1.68 |
| L 段致谢人数 | 6 | 5 | L 合计 921 人、均值 1.68 |
| L 段致谢人数 | 7 | 11 | L 合计 921 人、均值 1.68 |
| L 段致谢人数 | 8 | 1 | L 合计 921 人、均值 1.68 |
| L 段致谢人数 | 合计 | 549 | 校验=549；全树 9132 段均值 1.80、最大 37 |
| 同页共存 | L 页中还有非 L 段的页 | 156 | 162 页中 156 页如此，仅 6 页全为 L 段 |
| 同页共存 | 这 162 页全部段 | 8172 | L 段 550 占 6.7% |
| 版式域 | 域 B 内 L 段 | 4 | 域 B 共 91 段，logic 条目 4 段落在 120304／120309／120311／120339 |
| 字段落位 | 致谢字段含 logic | 0 | Description 540／Impact 10／致谢 0 |
| 字段落位 | 同一句 logic 出现 2 次的段 | 0 | 540 段每段恰 1 句含 logic |

# 附表 E 中文手册层七组对应词（64 行）

**读法：** 分母是《Apple 平台安全保护》zh-CN 转储 179 份中的 **178 页可用**（窗口切分后剔除 1 份无正文）。第一组"逻辑本体"7 词里只有 `逻辑` 非零（**4 次／2 页**），其余 6 词全零——第 18.2 节给出这 4 次的完整上下文（`硬件逻辑`×2 与 `此逻辑`×1 在《硬件麦克风断联》，`逻辑板`×1 在《基于 Intel 的 Mac 中的固件密码保护》），**全部是硬件语汇**。第五组"限制与许可"与第七组"类型与后果"是最集中页的对照来源：`绕过` 23 次里 12 次在《激活锁安全性》、`沙盒` 27 次里 6 次在《macOS 中的 Option ROM 安全性》、`特权` 9 次里 7 次在同一页。第三组 `检查` 66 次只覆盖 30 页、`校验` 19 次只覆盖 3 页（16 次在《Apple 钱包中证件的安全性》），是第 18.5 节跨层误用的实证。`竞态`／`竞争` 全零是 #122 附表 E 的同一结论。

| 组 | 中文词 | 出现次数 | 覆盖页 | 覆盖页占比 | 最集中页面 |
|---|---|---:|---:|---:|---|
| 逻辑本体 | 逻辑 | 4 | 2 | 1.1% | 硬件麦克风断联（3 次） |
| 逻辑本体 | 逻辑缺陷 | 0 | 0 | 0.0% | — |
| 逻辑本体 | 逻辑错误 | 0 | 0 | 0.0% | — |
| 逻辑本体 | 逻辑问题 | 0 | 0 | 0.0% | — |
| 逻辑本体 | 逻辑漏洞 | 0 | 0 | 0.0% | — |
| 逻辑本体 | 推理 | 0 | 0 | 0.0% | — |
| 逻辑本体 | 因果 | 0 | 0 | 0.0% | — |
| 缺陷与问题词 | 缺陷 | 1 | 1 | 0.6% | 基于 Intel 的 Mac 中的 UEFI 固件安全性（1 次） |
| 缺陷与问题词 | 问题 | 17 | 11 | 6.2% | 关于 App Store 安全性（3 次） |
| 缺陷与问题词 | 错误 | 27 | 16 | 9.0% | 操作系统完整性（4 次） |
| 缺陷与问题词 | 故障 | 6 | 4 | 2.2% | TLS 安全性（2 次） |
| 缺陷与问题词 | 异常 | 3 | 3 | 1.7% | Apple Cash 安全性（1 次） |
| 缺陷与问题词 | 失误 | 0 | 0 | 0.0% | — |
| 缺陷与问题词 | 瑕疵 | 0 | 0 | 0.0% | — |
| 缺陷与问题词 | 弱点 | 0 | 0 | 0.0% | — |
| 检查与校验 | 检查 | 66 | 30 | 16.9% | 管理式 Apple 账户安全性（13 次） |
| 检查与校验 | 校验 | 19 | 3 | 1.7% | Apple 钱包中证件的安全性（16 次） |
| 检查与校验 | 验证 | 432 | 178 | 100.0% | Apple 钱包中的证件（19 次） |
| 检查与校验 | 审查 | 13 | 4 | 2.2% | 关于 App Store 安全性（8 次） |
| 检查与校验 | 确认 | 249 | 178 | 100.0% | “平安确认”安全性（25 次） |
| 检查与校验 | 核对 | 6 | 4 | 2.2% | 将信用卡或借记卡添加到 Apple Pay（3 次） |
| 检查与校验 | 检测 | 77 | 35 | 19.7% | 在 macOS 中防范恶意软件（12 次） |
| 检查与校验 | 核实 | 0 | 0 | 0.0% | — |
| 状态与处理 | 状态 | 87 | 45 | 25.3% | 钥匙串数据保护（7 次） |
| 状态与处理 | 状态机 | 0 | 0 | 0.0% | — |
| 状态与处理 | 处理 | 337 | 178 | 100.0% | 安全隔区（24 次） |
| 状态与处理 | 错误处理 | 0 | 0 | 0.0% | — |
| 状态与处理 | 文件处理 | 0 | 0 | 0.0% | — |
| 状态与处理 | 句柄 | 1 | 1 | 0.6% | 搭载 Apple 芯片的 Mac 上的 Rosetta 2（1 次） |
| 状态与处理 | 生命周期 | 9 | 8 | 4.5% | 证明过程安全性（2 次） |
| 状态与处理 | 快照 | 5 | 3 | 1.7% | Apple 文件系统作用（3 次） |
| 限制与许可 | 限制 | 478 | 178 | 100.0% | LocalPolicy 签名密钥创建和管理（21 次） |
| 限制与许可 | 约束 | 1 | 1 | 0.6% | 操作系统完整性（1 次） |
| 限制与许可 | 许可 | 14 | 12 | 6.7% | 为 Apple 设备和服务验证配件（2 次） |
| 限制与许可 | 允许 | 152 | 82 | 46.1% | 搭载 Apple 芯片的 Mac 的 LocalPolicy 文件内容（8 次） |
| 限制与许可 | 拒绝 | 11 | 9 | 5.1% | 证明过程安全性（2 次） |
| 限制与许可 | 准入 | 0 | 0 | 0.0% | — |
| 限制与许可 | 权限 | 283 | 178 | 100.0% | 操作系统完整性（14 次） |
| 限制与许可 | 特权 | 9 | 3 | 1.7% | macOS 中的 Option ROM 安全性（7 次） |
| 限制与许可 | 配额 | 0 | 0 | 0.0% | — |
| 路径与容器 | 路径 | 4 | 3 | 1.7% | 签名系统宗卷安全性（2 次） |
| 路径与容器 | 符号链接 | 0 | 0 | 0.0% | — |
| 路径与容器 | 软链接 | 0 | 0 | 0.0% | — |
| 路径与容器 | 硬链接 | 0 | 0 | 0.0% | — |
| 路径与容器 | 挂载 | 0 | 0 | 0.0% | — |
| 路径与容器 | 归档 | 2 | 2 | 1.1% | 访问钥匙类型（1 次） |
| 路径与容器 | 解包 | 0 | 0 | 0.0% | — |
| 路径与容器 | 提取 | 12 | 10 | 5.6% | iMessage 信息如何安全发送和接收信息（2 次） |
| 路径与容器 | 容器 | 23 | 10 | 5.6% | Apple 文件系统作用（7 次） |
| 路径与容器 | 协议 | 83 | 37 | 20.8% | 虚拟专用网络 (VPN) 安全性（8 次） |
| 路径与容器 | URL | 20 | 8 | 4.5% | TLS 安全性（5 次） |
| 类型与后果 | 类型混淆 | 2 | 1 | 0.6% | 内存安全 iBoot 实施（2 次） |
| 类型与后果 | 溢出 | 1 | 1 | 0.6% | 内存安全 iBoot 实施（1 次） |
| 类型与后果 | 内存 | 511 | 178 | 100.0% | 安全隔区（49 次） |
| 类型与后果 | 破坏 | 9 | 9 | 5.1% | 在 macOS 中防范恶意软件（1 次） |
| 类型与后果 | 竞态 | 0 | 0 | 0.0% | — |
| 类型与后果 | 竞争 | 0 | 0 | 0.0% | — |
| 类型与后果 | 拒绝服务 | 1 | 1 | 0.6% | macOS 中的防火墙安全性（1 次） |
| 类型与后果 | 崩溃 | 6 | 4 | 2.2% | Apple 操作系统中的后台安全改进（3 次） |
| 类型与后果 | 提权 | 0 | 0 | 0.0% | — |
| 类型与后果 | 绕过 | 23 | 9 | 5.1% | 激活锁安全性（12 次） |
| 类型与后果 | 沙盒 | 27 | 11 | 6.2% | macOS 中的 Option ROM 安全性（6 次） |
| 类型与后果 | 沙箱 | 0 | 0 | 0.0% | — |
| **合计** | 63 词（全零 24 词、非零 39 词） | — | — | 178 页可用 | — |

---

**规则：** 本篇全部计数按 #111 第 1 章两版式域切块口径执行，判据大小写不敏感、空格类字符（U+00A0／U+202F）统一按 `$S` 归一；词界优先，仅在明确标注"词族"时用子串匹配；字段范围每次写明是 **Impact 单字段**、**Description 单字段**还是**三字段并集**（附表 A 的"三字段并集段"与"∩L"列、第 10 章十五套类型词集为三字段口径；附表 A 的 ∩R／∩WO／∩M2 列按各行判据的 Impact 侧集合求交；附表 B 的五个外部集列与八类列按 Description 侧 540 段口径；附表 C 的外部集列与附表 F 全部按 L-wide 550 段口径）。**分母一律写明：9,136（全树段）／7,434（含 Impact 段）／8,172（L 所在 162 页全部段）／4,092（附表 F 81 名 Impact 全集）；类次 228 与段次 550 分列报出，不互换；附表 B、C、F 的页列与名列是逐行去重后的再求和，不得当作集合基数引用；凡"A 与 B 得数相同"处必须同时给出 |A|、|B|、|A∩B| 与差段清单（第 11 章）。**

**边界声明：** 本篇是**公开英文与中文文本的措辞计量**，不是脆弱性评估、不是攻击可行性研究，也不提供任何绕过方法、利用代码、样本、利用链构造或针对他人设备的操作步骤与时序。文中"逻辑缺陷"一律指**公开条目把缺陷自述为 logic（或把修复手段自述为 improved logic／additional logic）这一文本事实**，不推断该缺陷的真实成因、是否属于状态机或业务规则错误、触发路径或可利用性；550 段、167 个编号、228 类次、81 个组件名、9 段在野标注等数字**只描述文本分布**，不构成任何组件安全性评级、缺陷优先级排序或攻击路径存在的结论。

Telegram: https://t.me/one00190
