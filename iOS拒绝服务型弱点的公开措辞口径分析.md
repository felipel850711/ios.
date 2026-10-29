@@TITLE@@iOS 拒绝服务型弱点的公开措辞口径分析
@@SUBTITLE@@选题是"拒绝服务（denial of service）"这一类后果。逐词测量的结果是：**这一类是措辞层四篇里唯一"Impact 主导但两字段部分重叠"的形态**——`denial of service`（含连写变体）写在 **Impact 侧 457 段**、写在 **Description 侧 50 段**、写在**致谢侧 0 段**，两侧**交集 37 段**，所以三字段并集 **470 ＝ 420 ＋ 13 ＋ 37**，占全树 9,136 段的 **5.14%**、占含 Impact 的 7,434 段的 **6.32%**，覆盖 **155／257 页（60.3%）**与 **62／472 名（13.1%）**。第二个发现是**与终止语汇几乎完全分离**：`terminat*` 词族在 Impact 侧有 **1,546 段**，与本篇 470 段**只交 2 段**；而全树最大的两句终止套话 `unexpected system termination`（802 段）与 `unexpected app termination`（612 段）与本篇交集是 **0 段**。第三个发现是**可用性语汇整族缺席**：`hang`、`unavailable`、`unresponsive`、`freez*`、`stall`、`watchdog`、`reboot`、`restart`、`loop`、`rate limit`、`quit`、`kill`、`forced restart`、`DoS`、`DOS` 共 **15 个判据三字段全零**；连同 `infinite loop`、`reproduc*`、`quit unexpectedly`，附表 A 的 67 项判据里全零共 **18 项**（可用性与资源词组 9、终止与中断词组 7、拒绝服务本体词组 2）。第四个发现是**这一类不带任何"在野"与"无前提"标注**：470 段与在野集（`exploit*` 108 段）**交 0 段**、与无前提集（`without` 178 段）**交 0 段**、与 `extremely sophisticated`（59 段）、`actively exploited`（18 段）、`consent`（54 段）、`prompting`（19 段）**全部交 0 段**。
@@INFO@@报告日期|2026 年 10 月 29 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（258 份文件排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 9,132 段含致谢**。拒绝服务集 Ds ＝ `denial of service`（连字符、各类短横与空格皆可）以子串出现在 Impact、Description、致谢任一字段 **＝ 470 段 / 155 页 / 62 名**，其中 Impact 侧 457 段、Description 侧 50 段、致谢侧 0 段、两侧交集 37 段。对照集：**R** ＝ #118 的远程触达集（Impact 侧 11 个通道词族）**843 段 / 178 页 / 72 名**；**WO** ＝ #119 的无前提集（Impact 含 `without` 词界）**178 段 / 97 页 / 37 名**；**E** ＝ #114 的在野集（Impact 含 `exploit` 词族）**108 段 / 76 页 / 12 名**；**G／HI** ＝ #116 与 #120 的动名词集 **2,179 段** 与交互触发集 **273 段 / 102 页 / 35 名**；**M2** ＝ #121 的同段 ≥2 后果类集 **185 段 / 82 页 / 35 名**；**U** ＝ Impact 含 `user`（词界）**1,627 段 / 175 页 / 200 名**；**RC** ＝ #122 的竞态集（Description 含 `race condition`）**232 段 / 95 页 / 40 名**；**L** ＝ #123 的逻辑集（`logic` 词界三字段）**550 段 / 162 页 / 81 名**；**term** ＝ 本篇自建的终止词族集（Impact 含 `terminat*`）**1,546 段 / 165 页 / 102 名**。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或利用链构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**"拒绝服务"这一后果词在公开英文文本的哪个字段、哪一句位、以哪一种拼写出现，与终止类语汇、与可用性语汇、与在野标注、与别的后果词之间的集合关系**，属于措辞与文本计量分析；文中任何"拒绝服务"表述均指**公开条目把影响自述为 denial of service（或在修复句里自述 denial-of-service issue）这一文本事实**，不是对该缺陷是否真的造成服务中断、是否属于资源耗尽、是否可远程触发的判断，也不构成任何组件可用性风险的结论
@@INFO@@分类|判据**六组 67 项 / 三字段全零 18 项 / 非零 49 项 / Impact 列合计 19,599**（附表 A，六组：拒绝服务本体词 7、终止与中断词 14、可用性与资源词 14、修复套话词 15、外部集对照 9、后果强度八类 8）/ **二十七条 distinct Impact 全文 × 段页名 × 含本体词与否 × ∩R ∩WO ∩E ∩M2 ∩term × 八类**（附表 B，28 行含合计，段合计 **470**、含本体词 **是 457／否 13**、类次 **489**）/ **三条含本体词的 Description 全文 × 修复手段，续表十三把反向刀 × 全树段 ∶ Ds 内段 ∶ 非 Ds 段**（附表 C，4 行含合计；续表 13 项 **全树 4,862 ∶ Ds 内 233 ∶ 非 Ds 4,629 ＝ 4.8%**）/ **页面、编号与致谢四组分箱**（附表 D，34 行，页分箱闭合 155、编号分箱闭合 470、致谢分箱闭合 470）/ **中文层 61 词**（附表 E，八组：拒绝服务本体 7、终止与关闭 7、崩溃与异常 7、挂起与无响应 7、重启与恢复 7、资源与配额 9、检查与修复 8、类型与后果 9；**全零 21 词、非零 40 词**）/ **组件层 62 名**（附表 F，63 行含合计，段合计 470、这 62 名的 Impact 全集 **3,643** 段、Ds 占其 **12.9%**、占全树含 Impact 段的 **49.0%**）/ **八把刀**：字段刀（Impact 457 ∶ Description 50 ∶ 致谢 0，两字段**重叠 37**）、拼写刀（连写 428 ∶ 空格 42 ∶ 混合 0，`DoS`／`DOS`／`denial of access` 全零）、终止刀（`terminat*` 1,546 段 ∩ 470 段 ＝ **2**，两句终止套话 ∩ **0**）、可用性词刀（六组 67 项判据里 **18 项三字段全零**：可用性与资源词组 9、终止与中断词组 7、拒绝服务本体词组 2）、修复套话反向刀（`improved input validation` 641 段里只有 113 段属 Ds，`improved error handling` 18 段里 8 段属 Ds）、后果刀（类次 **489** ∶ 470 段，0 类 11 段，`DOS` 类 Impact 口径 459 ∶ 并集口径 470）、在野刀（∩E ＝ **0**、∩WO ＝ **0**）、施事者刀（`may be able to cause` 260 ＋ `may lead to` 182 ＋ `may be able to perform` 15 ＝ 457）
@@INFO@@难度|进阶（第 3 章的 470 段有**三套分母**：占全树 9,136 段是 **5.14%**、占含 Impact 的 7,434 段是 **6.32%**、占其所在 155 页全部 8,679 段是 **5.4%**，三个数都真，混用会互相矛盾；第 2 章与第 5 章的"∩Ds"在不同表里口径不同——附表 A 的 ∩Ds 列按**该行判据的三字段并集**求交，附表 B 的各列按**该行 Impact 全文的段集合**求交，附表 C 续表按**该短语所在字段**求交，所以 `DOS 类` 一行的 ∩Ds 是 **470**（每段总在三字段之一写了本体词），而 Impact 单字段口径只有 **459**；第 8 章的类次 **489 是"类次"不是"段次"**（1 类 429＋2 类 30×2＝489），把 489 读成 489 段会虚增 19 段；第 4 章的 428 与 42 是**同一批 470 段的两种拼写**，相加恰好 470，不是两个集合；第 7 章的 113 与 50 相差 63，是 `improved input validation` 里有 63 段不写 denial of service；附表 B、C、F 的页列与名列是逐行去重后的再求和，**不得当作并集去重值**；凡"两个集得数相同"处必须同时给出 |A|、|B|、|A∩B|（第 3、9、10 章））
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

**"拒绝服务"是后果层里最容易被读成"可用性风险"的一个类型词，但它在公开文本里的落位、拼写和邻居词都是可以逐词测的。** 本篇把这句选题拆成四个互相独立的断言，逐个测量：

**断言一（存在这一类）**：公开条目里确实有把影响自述为 `denial of service` 的文本。**测法**：本体词（连字符与各类短横、空格皆算）分三个字段匹配。**结果**：成立，且**是四篇措辞层里唯一"Impact 主导但两字段部分重叠"的形态**——Impact 侧 **457**、Description 侧 **50**、致谢侧 **0**，三字段并集 **470**，两侧**交集 37**（第 3 章）。

**断言二（拒绝服务会与终止语汇同段出现）**：如果这一类真的写中断，就应当与 `termination`、`crash`、`hang` 一类语汇高度共段。**测法**：`terminat*` 词族 1,546 段、`crash*` 499 段、十四个可用性判据分别与 Ds 求交。**结果**：**不成立**。`terminat*` ∩ Ds ＝ **2 段**（占终止词族的 **0.1%**）；`unexpected system termination` 802 段与 `unexpected app termination` 612 段各自 ∩ Ds ＝ **0**；`crash*` 499 段 ∩ Ds ＝ **0**；`hang`、`unavailable`、`unresponsive`、`freez*`、`stall`、`watchdog`、`reboot`、`restart`、`loop`、`rate limit`、`quit`、`kill`、`forced restart` **全部三字段 0 段**（第 5、6 章）。

**断言三（这一类有可识别的后果强度）**：后果字段会为拒绝服务写出可分类的影响。**测法**：Impact 侧 distinct 全文 ＋ 八类共现。**结果**：470 段的后果文本共 **27 条 distinct 全文**，段级 **0 类 11／1 类 429／2 类 30／3 类 0**，类次 **489**，其中 **`DOS` 类 459、`DIS` 类 30，其余六类 Impact 侧全部为 0**（第 8 章）。这一类**只与"信息泄露"发生共段**，与提权、沙盒、代码执行、完全控制四类**一段都不共**。

**断言四（这一类会带可达性与在野标注）**：拒绝服务常被读成"远程打一下就能让设备不可用"。**测法**：与 #118 的远程集、#114 的在野集、#119 的无前提集求交。**结果**：**一半成立一半不成立**。远程侧 ∩R ＝ **164 段**（占 Ds 的 34.9%、占 R 集的 19.5%），确实有三分之一的条目写了通道词；但在野侧 ∩E ＝ **0**、无前提侧 ∩WO ＝ **0**，`extremely sophisticated`（59 段）、`actively exploited`（18 段）与 Ds **一段不共**（第 11、12 章）。

**所以本篇的产出是一条"落位偏 Impact、拼写只两种、与终止语汇零重合、零在野标注"的结论**：`denial of service` 在这套语料里是一个**后果套话**，它以 27 条固定句子出现在 457 段的 Impact 字段，**不与任何描述"进程结束"或"设备卡住"的语汇同段**——这两件事在公开措辞层是**分开的两套话**。第 20 章给复算，第 22 章纠正会把 470 段读成"470 起可用性事故"的九种误读。

# 1 操作性定义与分母

**1.1 语料与切块。** 与 #114–#123 同一镜像、同一切块口径：块起于 `<h3 class="gb-header">`（域 A）或粗体段（域 B），块止于**四锚点**（下一块起点、页脚声明、版权行、脚本行），缺一锚点就会把页面免责声明切进字段文本。全树 **9,136 段 / 257 页 / 472 名**；含 Impact **7,434 段**；含致谢 **9,132 段**。域 A **9,045 段 / 246 页 / 470 名**，域 B **91 段 / 11 页 / 26 名**。本篇的 470 段里有 **3 段落在域 B**（第 16.5 节）。

**1.2 Ds 集（本篇主集）。** 判据：`denial` ＋ `of` ＋ `service` 三者按顺序出现，其间分隔符允许连字符、U+2010–U+2011 短横、普通空格与 U+00A0／U+202F，出现在 Impact、Description 或致谢任一字段；大小写不敏感。**Ds ＝ 470 段 / 155 页 / 62 名**。字段落位 **Impact 457 ∶ Description 50 ∶ 致谢 0**；**|Impact 侧 ∩ Description 侧| ＝ 37**，故 470 ＝ 457 ＋ 50 − 37 ＝ **420（仅 Impact）＋ 13（仅 Description）＋ 37（两侧都写）**（第 3 章）。470 段的 Impact 字段**全部非空**（Impact 为空的段数 ＝ 0）。

**1.3 后果强度八类（与 #121／#122／#123 同判据，只在 Impact 字段匹配）。**

| 代号 | 判据 | 全树 Impact 段 | Ds 命中 | Ds 占该类 |
|---|---|---:|---:|---:|
| `ACE` | `arbitrary code execution`／`execute arbitrary code` | 336 | **0** | 0.0% |
| `BYP` | `bypass`（词界） | 291 | **0** | 0.0% |
| `DIS` | `disclos*`／`leak*`／`expos*` | 530 | 30 | 5.7% |
| `DOS` | `denial-of-service`／`unexpected system／app termination`／`terminat*` | 2,003 | 459 | 22.9% |
| `FULL` | `arbitrary file(s)`／`any action`／`full control` | 63 | **0** | 0.0% |
| `MEM` | `memory corruption`／`corrupt memory` | 220 | **0** | 0.0% |
| `PRI` | `elevat* privileges`／`privileges escalat*` | 124 | **0** | 0.0% |
| `SBX` | `sandbox`（词界） | 418 | **0** | 0.0% |

类次合计 **0＋0＋30＋459＋0＋0＋0＋0 ＝ 489**，与第 8 章的段级分布（0 类 11＋1 类 429＋2 类 30＝**470 段**，类次 ＝ 429＋30×2 ＝ **489**）互为校验。**这张表里六个 0 就是本篇的核心结果**：拒绝服务条目在公开文本中**不写提权、不写沙盒逃逸、不写任意代码执行、不写完全控制、不写内存破坏、不写绕过**，只与"信息泄露"共段 30 次。

**1.4 三套分母。** 同一句"拒绝服务条目占多少"有三个答案：**5.14%**（÷9,136 全树段）、**6.32%**（÷7,434 含 Impact 段）、**5.4%**（÷470 段所在 155 页的全部 8,679 段）。本篇每次出现百分号都写清用的是哪一套。第四套用于组件层（第 15 章）：**该组件含 Impact 的段数**，Ds 全集占该口径 **12.9%**（470／3,643）。

**1.5 与 #122／#123 的规模对照。** `denial of service` 470 段 ∶ `logic` 550 段 ∶ `race condition` 232 段。页 **155 ∶ 162 ∶ 95**；名 **62 ∶ 81 ∶ 40**。**三篇的字段形态互不相同**：竞态是 **Impact 0 ∶ Description 232**（单字段），逻辑是 **Impact 10 ∶ Description 540 ∶ 交 0**（两字段互斥），本篇是 **Impact 457 ∶ Description 50 ∶ 交 37**（两字段部分重叠）。这三个形态放在一起才构成"措辞层类型词的字段落位不是一个模式"的证据（第 17 章）。

# 2 结论速览（每条都可按第 20 章复算）

**一、这一类主要活在 Impact，但 Description 也独占 13 段。** Impact 侧 **457** 段、Description 侧 **50** 段、致谢侧 **0** 段，两侧**交 37 段**，并集 **470** 段／155 页（60.3%）／62 名（13.1%）。仅 Description 命中的 **13 段**与"Impact 文本不含本体词"的 **13 段**是**同一批**（|A|＝|B|＝13、|A∩B|＝13、差 0 段），这 13 段的 Impact 是附表 B 的三条"否"文本：**8** 段 `A non-privileged user may be able to modify restricted network settings`、**3** 段 `An app may be able to modify protected parts of the file system`、**2** 段 `An unprivileged process may be able to terminate a root processes`（8＋3＋2 ＝ 13；第 3、10 章）。

**二、拼写只有两种且互斥。** 470 段里 **428 段用连写 `denial-of-service`（91.1%）**、**42 段用空格 `denial of service`（8.9%）**，428＋42 ＝ 470（**没有一段两种拼写同时出现**）。`DoS` 缩写 **0 段**、`DOS` 全大写 **0 段**、`denial of access` **0 段**、`denial of experience` **0 段**。`denial` 词界在这套语料里**只出现在本体词中**：词界并集 **42 段**，全部属于 Ds（第 4 章）。

**三、与终止语汇几乎零重合。** `terminat*` 词族 Impact 侧 **1,546 段／165 页／102 名**，与 Ds **只交 2 段**（0.1%）；`termination` 词界 1,520 段 ∩ Ds ＝ **0**；`unexpected system termination` **802 段 ∩ 0**、`unexpected app termination` **612 段 ∩ 0**、两句合起来 1,414 段 ∩ Ds ＝ **0**、`terminate the process` 24 段 ∩ Ds ＝ **0**。那唯一共段的 2 段是 `MetricKit` 组件在页面 `125108`／`125633` 上的同一条目（第 5 章）。

**四、可用性语汇整族缺席。** 附表 A 的"可用性与资源词"组 14 个判据里 **9 个三字段全零**（`unavailable`、`hang`、`unresponsive`、`reboot`、`restart`、`loop`、`infinite loop`、`rate limit`、`reproduc*`）；非零的五个是 `shutdown` **16 段 ∩ Ds 0**、`memory` **并集 1,967 段 ∩ Ds 135**、`persist*` **46 段 ∩ Ds 7**、`exhaust*` 与 `resource` **各并集 5 段 ∩ Ds 5**。相邻的"终止与中断词"组 14 个判据里另有 **7 个全零**（`quit`、`quit unexpectedly`、`kill`、`freez*`、`stall`、`watchdog`、`forced restart`）。`crash*` **499 段 ∩ Ds 0**。**全树没有一段用"卡住／无响应／重启循环"来表达拒绝服务后果**（第 6 章）。

**五、修复套话与这一类高度不重合。** 附表 C 续表十三把反向刀合计覆盖全树 **4,862** 段，其中只有 **233 段（4.8%）**属 Ds。最大的 `improved input validation` 全树 **641** 段、Ds 内 **113** 段（**17.6%**）；`improved checks` **1,018 ∶ 73（7.2%）**；`improved state management` **662 ∶ 8（1.2%）**；`improved restrictions` **47 ∶ 0**（一条都不属本篇）。反向：**470 段里有 233 段被某个反向刀短语覆盖，237 段没有**（第 7 章）。

**六、这一类的后果层只与"泄露"共段。** 27 条 distinct Impact 全文，**0 类 11 段／1 类 429 段／2 类 30 段／3 类 0 段**；30 段 2 类全部来自两句 `… may lead to a denial-of-service or potentially disclose memory contents`（22 段）与 `Processing a file may lead to a denial-of-service or potentially disclose memory contents`（8 段）——**"拒绝服务 ＋ 内存内容泄露"是唯一的双类句式**，其 Impact 文本不含本体词的 0 段、`MEM` 类 Impact 命中 **0**（第 8 章）。

**七、在野与无前提标注一段都没有。** ∩E ＝ **0**（在野集 108 段全树）、∩WO ＝ **0**（`without` 集 178 段全树）、∩RC ＝ **0**（竞态集 232 段）。`extremely sophisticated` 59 段 ∩ Ds **0**、`actively exploited` 18 段 ∩ **0**、`report that this issue` 103 段 ∩ **0**、`consent` 54 段 ∩ **0**、`prompting` 19 段 ∩ **0**、`user interaction` **0 段**。同时 `will` 词界 Impact **0 段**、`could` 37 段 ∩ Ds **0**——470 段的 Impact **每一段都用 `may`**（第 11 章）。

**八、施事者句式三选一闭合到 457。** `may be able to cause` **260** ＋ `may lead to` **182** ＋ `may be able to perform` **15** ＝ **457**，恰好等于 Impact 侧含本体词的段数；`may lead to` 的 182 段与 #116 的动名词开头集 G（2,179 段）相交 **182 段（|G∩Ds| ＝ 182，与 |"may lead to"∩Ds| 同数**，第 9 章给出 |A|、|B|、|A∩B| 三值核对）。施事者首位共 **20 类**，最大三类 `An app` **117**、`A remote attacker` **54**、`An attacker on the local network` **49**（第 9 章）。

**九、中文手册层几乎不存在这一类。** 附表 E 的 61 个中文词里 **21 个全零**；`拒绝服务` 只有 **1 次／1 页**（《macOS 中的防火墙安全性》），`服务拒绝`／`瘫痪`／`停摆`／`挂起`／`无响应`／`卡住`／`冻结`／`停滞`／`死锁`／`重新启动`／`重新引导`／`复位`／`节流`／`过载`／`配额`／`占用`／`错误处理`／`注销`／`沙箱`／`提权`／`竞态` **全部 0 次**。而 `恢复` **559 次／178 页**、`内存` **511 次／178 页**、`重启` **41 次／20 页**——**中文层的"重启／恢复"与英文层的 termination 语汇是两套不相干的机器运维话**（第 18 章）。

# 3 字段刀：Impact 457 ∶ Description 50 ∶ 致谢 0，两字段重叠 37

**3.1 测量。** 对同一判据分别在三个字段匹配：Impact **457** 段、Description **50** 段、致谢 **0** 段；三字段并集 **470** 段／155 页／62 名。附加量：**|Impact 侧 ∩ Description 侧| ＝ 457 ＋ 50 − 470 ＝ 37**；仅 Impact **420** 段、仅 Description **13** 段。与 #123 的关键差别就在这里——逻辑缺陷的两字段**交 0**，所以 550 ＝ 10 ＋ 540 可以直接相加；本篇的两字段**交 37**，所以 470 **不等于** 457 ＋ 50。

**3.2 三种字段形态的并排。** 把措辞层四篇放在一起：**#122 竞态** Impact 0 ∶ Description 232 ∶ 致谢 0（并集 232，单字段即可定义）；**#123 逻辑** Impact 10 ∶ Description 540 ∶ 致谢 0（并集 550，两字段互斥）；**#124 拒绝服务** Impact 457 ∶ Description 50 ∶ 致谢 0（并集 470，两字段重叠 37）；**#121 M2** 属后果类组合、不落在此轴。**结论：同一棵顾问页树上，"类型／后果词落在哪个字段"没有统一规律，必须逐词测。**

**3.3 那 13 段"仅 Description"的原文。** 逐段核对：13 段的 Description 只有两条 distinct 文本——**10 段** `A denial-of-service issue was addressed with improved input validation.`、**3 段** `A denial of service issue was addressed by removing the vulnerable code.`；它们的 Impact 分别是 `A non-privileged user may be able to modify restricted network settings`（8 段）、`An app may be able to modify protected parts of the file system`（3 段）、`An unprivileged process may be able to terminate a root processes`（2 段）。**这 13 段与"Impact 全文不含本体词"的 13 段是同一批**：|A| ＝ 13、|B| ＝ 13、|A∩B| ＝ 13、|A|−|A∩B| ＝ 0。

**3.4 那 37 段"两侧都写"的原文。** 37 段的 Description 分布为 **23 段** `…improved input validation.` ＋ **13 段** `…improved validation.` ＋ **1 段** `…removing the vulnerable code.`；与附表 C 首表三条文本的合计 **33 ＝ 23 ＋ 10**、**13 ＝ 13 ＋ 0**、**4 ＝ 1 ＋ 3** 完全对账（**33 ＋ 13 ＋ 4 ＝ 50**）。**即：附表 C 首表的三条句子，每一条都同时出现在"两侧都写"与"仅 Description"两档里，只有中间那条 13∶0。**

**3.5 致谢侧 0 段的含义。** 致谢字段只放报告人名与 CVE 编号，从不放后果词；Ds 的 470 段全部有致谢（第 16.4 节），**致谢侧 0 段是字段分工的结果，不是这一类条目缺少报告来源**。同一位置在 #122（Description 主导）与 #123（Description 主导）里也是 0，三篇一致。

**3.6 规模比要在此口径下读。** "拒绝服务 470 段 ∶ 逻辑 550 段 ＝ 0.85∶1"**比较的是三字段并集**，其中本篇以 Impact 为主、逻辑以 Description 为主。**不是"逻辑缺陷条目比拒绝服务多 17%"**，而是"两套词在各自主要字段里的段数之比"。

# 4 拼写刀：连写 428 ∶ 空格 42，`DoS`／`DOS`／`denial of access` 全零

**4.1 两种拼写互斥且闭合。** 在 Impact ＋ Description 合并文本上统计：**含 `denial-of-service`（连字符）428 段**、**含 `denial of service`（空格）42 段**，两数相加 **470 ＝ |Ds|**，说明**没有任何一段同时使用两种拼写，也没有第三种拼写**。分隔符宽度：判据允许连字符、U+2010／U+2011／U+2012／U+2013、普通空格、U+00A0、U+202F——**放宽到这个程度仍然只有两种形态**。

**4.2 三个常见替代写法全零。** `DoS`（区分大小写、词界）**0 段**、`DOS`（全大写、词界）**0 段**、`denial of access` **0 段**、`denial of experience` **0 段**。这四条判据计入附表 A 的"18 项全零"。**这套语料不使用任何缩写，也不把 denial 用在服务之外的宾语上。**

**4.3 `denial` 与 `service` 两个裸词各自的范围。** `denial` 词界（三字段并集）**42 段**，且**这 42 段全部属于 Ds**（|denial 词界并集 ∩ Ds| ＝ 42 ／ |denial 词界并集| ＝ 42）——**`denial` 在这套语料里是本体词专用成分，不独立使用**。`service`／`services` 词界并集 **46 段**、其中 **42 段属 Ds**、另 **4 段**是运维语汇（`services` 词界自身只 **3 段**、∩ Ds **0**）。**"service"一词在本语料里几乎不与"拒绝"无关地使用，但仍有 4 段例外，所以不能用 `service` 词界代替本体词判据。**

**4.4 词形刀与 #122／#123 的同型结论。** #122 得到 `condition` 词界 ≡ `race condition`（双向差 0）；#123 得到 `logic` 词界 ≡ `logic` 子串（差 0）；本篇得到 `denial` 词界 ⊂ 本体词（差 0）而 `service` 词界 ⊄（差 4）。**三篇共同的结论是：类型词在顾问页语料里以固定套句存在，不产生派生词形；差异在于介词后那半截词是否被别的用法共用。**

**4.5 连写比例的意义。** 91.1% 用连写、8.9% 用空格，且**空格形态集中在较新的条目与 Description 侧**（附表 C 首表里唯一用空格的句子 `A denial of service issue was addressed by removing the vulnerable code.` 覆盖 4 段，其 Impact 侧的 19 段 `An app may be able to cause a denial of service` 也用空格）。**做任何按字符串匹配的公开信息抽取，两种拼写必须都列；只写一种会漏 8.9% 或 91.1%。**

# 5 终止刀：`terminat*` 1,546 段 ∩ 470 段 ＝ 2

**5.1 总量。** Impact 含 `terminat*`（词首边界，`(?<![\w-])terminat`）**1,546 段／165 页／102 名**，是全树第二大的后果词族（仅次于 `memory` 并集 1,967）。它与 Ds 的交集是 **2 段**。占终止词族 **0.13%**、占 Ds **0.43%**。**这是本篇最反直觉的一条：一个被普遍当作"拒绝服务定义"的语汇族，与写"denial of service"的条目几乎完全不共段。**

**5.2 词族内部拆分。** `termination` 词界 **1,520 段／165 页／101 名** ∩ Ds ＝ **0**；`terminate` 词界 **26 段／26 页／4 名** ∩ Ds ＝ **2**；`terminate the process` **24 段** ∩ Ds ＝ **0**。**即与 Ds 共段的那 2 段来自 `terminate` 的动词用法，不是来自 `termination` 的名词套话。**

**5.3 两句终止套话 ∩ Ds ＝ 0。** `unexpected system termination` **802 段／129 页／53 名**；`unexpected app termination` **612 段／148 页／56 名**；两句并集 **1,414 段**（两句互斥，无同段共现）∩ Ds ＝ **0**。两句占终止词族的 **802＋612 ＝ 1,414／1,546 ＝ 91.5%**，其余 **132 段**是其他 `terminat*` 形态。**附表 C 续表前两行给出的就是这个 0。**

**5.4 那 2 段的原文。** 两条都是 `MetricKit` 组件、同一条目在两个页面重复呈现：页面 `125108` 与 `125633`；Impact ＝ `An unprivileged process may be able to terminate a root processes`，Description ＝ `A denial-of-service issue was addressed with improved input validation.`。**这是全语料唯一"后果字段写终止、修复字段写拒绝服务"的样本，只有 2 段，且属同一 CVE 组的两页。**（该文本也是附表 B 中"否"档三条之一，∩term 列的 2 就来自它。）

**5.5 `crash*` 同样是零。** `crash` 词族 Impact 侧 **499 段／128 页／17 名**，与 Ds **交 0 段**。**注意这 499 段的 `crash` 全部写在 Impact 之外的语境还是写在不属 Ds 的 Impact 里？是后者**——`crash` 出现于 Impact 但从未与本体词同段。中文层对应：`崩溃` 手册侧 6 次／4 页（第 18.3 节）。

**5.6 这条刀的可执行含义。** 任何人若用"Impact 含 terminat*／crash*"作为拒绝服务的抽取判据，会得到 1,546 ＋ 499 的**巨大集合**，其中只有 **2 段**是公开文本自己声明为 denial of service 的。**两套判据不是松紧关系，是不同概念**：终止套话描述的是"进程／系统结束运行"这一现象，本体词描述的是"服务被拒绝"这一后果归类；语料把它们分写在不同的条目上。

# 6 可用性与资源词刀：十四把判据里九把全零

**6.1 组内全零清单。** 本组（可用性与资源词）全零的九把：`unavailable` 词界、`hang` 词界、`unresponsive` 词界、`reboot` 词界、`restart` 词界、`loop` 词界、`infinite loop`、`rate limit` 词界、`reproduc*` 词界 —— **三字段全部 0 段**。相邻的终止与中断词组另有七把全零：`quit` 词界、`quit unexpectedly`、`kill` 词界、`freez*` 词界、`stall` 词界、`watchdog` 词界、`forced restart`；加上本体词组的 `DoS` 与 `DOS` 两把，附表 A 的**全零项合计 18**（2 ＋ 7 ＋ 9）。

**6.2 组内非零的五项。** `shutdown` 词界 Impact **16 段／12 页／2 名**，∩ Ds **0**；`memory` 词界 Impact **1,136 段**、三字段并集 **1,967 段**，∩ Ds **135**；`persist*` 词界 Impact **46 段／26 页／10 名**，∩ Ds **7**；`exhaust*` 与 `resource` 词界 Impact 各 **0 段**、三字段并集各 **5 段**、∩ Ds 各 **5**（全部来自同一句 `A resource exhaustion issue was addressed with improved input validation.`，5 段）。

**6.3 `memory` 的 135 段是唯一的大面积重合。** 470 段里有 **135 段（28.7%）**在三字段某处写了 `memory`，其中 **30 段**来自第 8.5 节那两句"或泄露内存内容"的双类句，另 **105 段**是内存相关的其他表述。**但 `MEM` 类（`memory corruption`／`corrupt memory`）在 Impact 侧对 Ds 的命中是 0**，三字段并集口径也只有 **7 段**（写在 Description）——**"内存"一词大量出现，"内存破坏"这一后果归类一段都不落在 Ds 的 Impact 上**（第 8.4 节口径对账）。

**6.4 资源耗尽语汇几乎不存在。** `resource`＋`exhaust*` 组合在本体词语料里只支撑 **5 段**；`配额`／`限额`／`节流`／`过载`／`占用` 五个中文对照词在手册层 **4 个全零、1 个 6 次单页**（附表 E 组 6）。**公开文本没有把拒绝服务解释为"资源耗尽"的成规模写法**——这与第 5 章"不解释为终止"合起来，构成"**这一类只写后果名词、不写机制**"的两条独立证据。

**6.5 `persist*` 的 7 段。** 全树 `persistent denial-of-service` 恰好也是 **7 段**（附表 A 组 1 与本组交叉核对），即 `persist*` ∩ Ds 的 7 段**全部**是"持续性拒绝服务"这一修饰语，组件为 `Kernel`（第 15.4 节）。**这是本体词唯一带的严重性修饰语。**

# 7 修复套话反向刀：`improved input validation` 的 641 段里只有 113 段属本篇

**7.1 反向切法。** 不按本体词切，而按 13 个高频短语切（附表 C 续表）：六把 Impact 侧的终止／崩溃刀 ＋ 七把 Description 侧的修复套话刀。每行给出**全树命中段 ∶ Ds 内段 ∶ 非 Ds 段**与该短语中 Ds 的占比。

**7.2 十三把刀的合计。** 全树 **4,862** 段 ∶ Ds 内 **233** 段 ∶ 非 Ds **4,629** 段，Ds 只占 **4.8%**。**反向刀覆盖 470 段中的 233 段，剩下 237 段的 Description 用的不在这 13 个短语里**（例如 `improved memory handling`、`improved bounds checking`、`improved state management` 之外的表述）。

**7.3 六把终止／崩溃刀全部给 0。** `unexpected system termination` 802、`unexpected app termination` 612、`terminate the process` 24、`crash*` 499 的 **Ds 内段 ＝ 0**；`hang` 与 `unresponsive` 两把**全树本身 0 段**。这与第 5 章的 2 段不矛盾：那 2 段用的是 `terminate a root processes`，不在上面五个短语之内。

**7.4 七把修复套话刀的稀释度。** `improved input validation` **641 ∶ 113（17.6%）**；`improved validation` **366 ∶ 16（4.4%）**；`removing the vulnerable code` **173 ∶ 15（8.7%）**；`improved checks` **1,018 ∶ 73（7.2%）**；`improved error handling` **18 ∶ 8（44.4%）**；`improved restrictions` **47 ∶ 0（0.0%）**；`improved state management` **662 ∶ 8（1.2%）**。**最高的是 `improved error handling`（44.4%），最低的是 `improved restrictions`（一条不属本篇）。**

**7.5 113 与 50 的差是 63。** `improved input validation` 的 Ds 内段 ＝ **113**，而 Description 侧含本体词的段只有 **50**。差 **63** 来自：**Ds 的 470 段里有 113 段的 Description 写了 `improved input validation`，但其中 63 段的 Description 并不写 denial of service**（它们靠 Impact 侧的本体词进集）。**这不是矛盾，是"并集判据"与"短语判据"的口径差**——附表 A 的 `improved input validation` 行给的 ∩Ds 113 就是这个数。

**7.6 附表 A 与附表 C 续表的三处口径不同。** 同一个短语在两表里可能不同数：`improved validation` 在附表 A 是 **Impact 4 段／并集 367 段／∩Ds 16**，在附表 C 续表是 **Description 侧全树 366 段／Ds 内 16**（差 **1** 段来自该短语只在 Impact 出现的段）；`improved input validation` 在附表 A 是 **Impact 0／并集 641／∩Ds 113**，在附表 C 续表是 **Description 全树 641／Ds 内 113**（两数相同）。**引用时必须写明是"并集"还是"单字段"**（第 19.3 节）。

# 8 后果刀：489 类次 ∶ 470 段次，0 类 11 段，六类 Impact 侧为 0

**8.1 段级分布。** 对 470 段的 Impact 字段按第 1.3 节八类判据计命中：**0 类 11 段、1 类 429 段、2 类 30 段、3 类 0 段**，合计 **470**；类次 **429 ＋ 30×2 ＝ 489**。**每段类次 1.04**，高于 #123 逻辑的 0.41 与 #122 竞态的 0.64——**这一类的后果字段写得比另两篇"更实"**，因为它本身就是一个后果词。

**8.2 六类为 0 是本篇最干净的表格。** `ACE` 336 段 ∩ Ds（Impact 口径）**0**、`BYP` 291 ∩ **0**、`FULL` 63 ∩ **0**、`MEM` 220 ∩ **0**、`PRI` 124 ∩ **0**、`SBX` 418 ∩ **0**；三字段并集口径同样为 **0／0／0／7／0／0**（`MEM` 的 7 段写在 Description）。**拒绝服务条目不与提权、沙盒、代码执行、完全控制、绕过同段并写**（附表 B 的八类列全表除 DIS／DOS 两列外逐行为 0）。

**8.3 0 类的 11 段。** 两条 distinct 文本：`A non-privileged user may be able to modify restricted network settings`（**8 段**）与 `An app may be able to modify protected parts of the file system`（**3 段**）。**它们的 Impact 写的其实是"修改受限设置／修改受保护文件系统"，被算进 Ds 只因 Description 写了 denial-of-service**（第 3.3 节）。**这 11 段是"后果归类与后果文本不一致"的直接样本。**

**8.4 `DOS` 类的两个口径。** Impact 单字段口径：`DOS` 类 ∩ Ds ＝ **459**；三字段并集口径（附表 A `拒绝服务后果八类 DOS` 行的 ∩Ds 列）＝ **470**。**两数都真**：459 ＝ 470 − 11，那 11 段的 Impact 不命中 DOS 判据，但它们的 Description 命中本体词，所以按并集口径仍属 DOS 类。**这是本篇最容易读错的一处，附表 A 与第 1.3 节的表分别采用两个口径。**

**8.5 双类句只有两句。** 30 段 2 类全部是 `DOS`＋`DIS` 组合：**22 段** `Processing a maliciously crafted file may lead to a denial-of-service or potentially disclose memory contents` ＋ **8 段** `Processing a file may lead to a denial-of-service or potentially disclose memory contents`。**这与 #121 的 M2 集（185 段）相交恰为 30**（30／185 ＝ 16.2%），即**"同段并写多类后果"的条目里每 6 段就有 1 段是"拒绝服务＋泄露内存内容"**。`or potentially disclose memory contents` 短语本身三字段并集 **30 段、∩Ds 30**（**完全重合**）。

**8.6 与 #123 的对照。** 逻辑缺陷 550 段类次 228、0 类占 60.0%；本篇 470 段类次 489、0 类占 **2.3%**。**两种类型词在后果层的"充实度"相差 26 倍**——`logic` 的后果文本借用其他类别，`denial of service` 自己就是一个后果类别。

# 9 施事者刀：260 ＋ 182 ＋ 15 ＝ 457

**9.1 谓语形态闭合。** 在 Impact 侧含本体词的 457 段上按谓语归类：`may be able to cause` **260 段**、`may lead to` **182 段**、`may be able to perform` **15 段**，三数相加 **＝ 457**。**没有第四种谓语形态**（其余非零计数只有 `may` 词界本身 **470／470**——470 段全部含 `may`；`will` 词界 Impact **0 段**；`could` 词界 37 段 ∩ Ds **0**）。

**9.2 施事者首位共 20 类。** 按 Impact 首句的施事者归并：**`An app` 117**、**`A remote attacker` 54**、**`An attacker on the local network` 49**、**`Processing web content` 46**、**`Processing a maliciously crafted file` 45**、**`Processing an image` 29**、**`Processing a maliciously crafted image` 27**、**其他／空 25**、**`Processing a maliciously crafted message` 16**、**`A user in a privileged network position` 9**、**`Processing a file` 8**、**`A non-privileged user` 8**、**`A remote user` 8**、**`An attacker` 7**、**`Visiting a website` 6**、**`Processing a malicious crafted file` 5**、**`An attacker in physical proximity` 5**、**`An attacker in a privileged position` 3**、**`An unprivileged process` 2**、**`A local attacker` 1**。合计 470（含第 3.3 节那 13 段）。

**9.3 `may lead to` 与动名词集的同数核对。** A ＝ `may lead to` ∩ Ds，|A| ＝ **182**；B ＝ #116 的动名词开头集 G（全树 2,179 段），|G ∩ Ds| ＝ **182**；|A ∩ B| ＝ **182**。**三个数说明"G 落在 Ds 内的 182 段"与"Impact 用 may lead to 的 182 段"是同一段集合**——本体词的被动句式全部以 `Processing…`／`Visiting…` 开头（第 9.2 节的 `Processing*` 七类 ＋ `Visiting a website` 六类 ＝ 176 ＋ 6 ＝ **182**）。**但同数不等于同名：这是逐段核对出来的相等，不是推断。**

**9.4 交互触发集只交 6 段。** #120 的 HI 集（动名词非 `Processing`／`Parsing` 开头，273 段）∩ Ds ＝ **6**，恰好是 **`Visiting a website may lead to an app denial-of-service`** 一句的 6 段。**"处理内容"类不算交互触发、"访问网站"类算**，两篇的边界在本篇里以 6 段体现。

**9.5 修饰语只有三种。** 全树 `maliciously crafted` 词族 Impact **1,769 段**、∩ Ds **88**；`persistent` 40 段 ∩ Ds **7**；`denial-of-service attack` 短语 **12 段**（其中 `crafted Wi-Fi packets` 7、`crafted Bluetooth packets` 5）。**88／457 ＝ 19.3%**——**五分之一的拒绝服务条目把输入写成"恶意构造"**，其余四分之三不写恶意性。**"maliciously crafted" 的有无不是这一类的定义特征。**

# 10 文本层刀：Impact 27 条 ∶ Description 3 条，470 段的 Description 全文有 42 种

**10.1 Impact 侧只有 27 条句子。** 470 段的 Impact 段全文 distinct ＝ **27** 条，**逐条互斥覆盖 470 段**（附表 B 合计行 470、重复段计数 0）。最大一条 `An app may be able to cause a denial-of-service` **95 段（20.2%）**，其后 **49／46／43／29／27／22／19／16／15**。**前 10 条合计 361 段（76.8%）**。

**10.2 Description 侧含本体词的只有 3 条。** 附表 C 首表：**33 段** `A denial-of-service issue was addressed with improved input validation.`、**13 段** `A denial-of-service issue was addressed with improved validation.`、**4 段** `A denial of service issue was addressed by removing the vulnerable code.`，合计 **50**。修复手段归类：input validation **33**、validation **13**、删除脆弱代码 **4**。**注意"input validation"与"validation"是两条不同句子**（附表 A 两行给 641 与 367 的不同全树分母），合并统计会得到 653 段的错误分母。

**10.3 470 段的 Description 全文有 42 种，只有 3 种含本体词。** 420 段靠 Impact 进集的条目，其 Description 写的是各种其他类型名的修复套话——最大的一条 `The issue was addressed with improved memory handling.`（该 470 段的 Description 里 **82 段**）以及 `A null pointer dereference was addressed with improved input validation.`（**29 段**）、`An out-of-bounds write issue was addressed with improved bounds checking.`（**28 段**）。**这就是"Impact 写拒绝服务、Description 写别的类型名"的具体形态：470 段的 Description distinct（42）远大于含本体词的 3 条。**

**10.4 类型名槽位规律。** 与 #123 第 10.4 节一致：**同一段的 Description 主语位置只有一个类型名槽位**。所以当 Impact 已写 `denial-of-service` 而 Description 写 `null pointer dereference` 时，本体词在 Description 侧不再出现。**这解释第 3.1 节"|Impact 侧| 457 远大于 |Description 侧| 50"的结构原因——不是 Description 不写类型，而是它写了别的类型。**

**10.5 句式动词。** Description 侧（三字段并集口径）对 Ds 的交集：`was addressed with` **416** ＋ `addressed by` **15** ＝ **431**；`addressed` 词界 **436**（差 **5** 段属被动外的其他 `addressed` 用法）；`improved` 词界 **412**；`validation` 词界 **137**；`resolved with` 全树仅 **1 段**、∩ Ds **0**；`were addressed with` 全树 **8 段**、∩ Ds **0**；`fixed` 词界 **0**。**与 #123 相同：这套语料的修复句式主语全部单数，且只用 `was addressed with`／`addressed by` 两种。**

# 11 在野与前提刀：∩E ＝ 0、∩WO ＝ 0、∩consent ＝ 0

**11.1 三个零交。** 与 #114 的在野集 E（Impact 含 `exploit*`，**108 段／76 页／12 名**）交 **0 段**；与 #119 的无前提集 WO（Impact 含 `without`，**178 段／97 页／37 名**）交 **0 段**；与 #122 的竞态集 RC（Description 含 `race condition`，**232 段／95 页／40 名**）交 **0 段**。**470 段里没有任何一段带在野标注、没有任何一段写"无需用户操作"、没有任何一段与竞态同段。**

**11.2 在野相关短语逐一核对。** `extremely sophisticated` 三字段并集 **59 段** ∩ Ds **0**；`actively exploited` **18 段** ∩ **0**；`report that this issue` **103 段** ∩ **0**；`in an attack against` **0 段**。**四条判据覆盖的全部在野标注段与 Ds 无一段重合。**

**11.3 前提类语汇。** `without` 词界 Impact **178 段** ∩ Ds **0**；`consent` 词界 Impact **54 段** ∩ **0**；`prompting` 词界 Impact **19 段** ∩ **0**；`user interaction` **0 段**。**注意 ∩UU（`user` 词界 Impact）＝ 25 段**——**"user"作为后果主体出现 25 段（`A user in a privileged network position` 9、`A non-privileged user` 8、`A remote user` 8 三段句合计 25），但没有一段用它表达"需要用户操作"**。这 25 段与 1,627 段的 U 集相比只占 **1.5%**。

**11.4 这条为什么重要。** 公开文本**不主张**拒绝服务类条目"已被在野利用"或"无需交互即可触发"。**这是措辞层的观测**：它既不证明这类缺陷不可被在野利用，也不证明都需要交互，只说明**在 Apple 的公开条目文本里这两件事从未同时出现**（第 22 章误读二号）。

**11.5 与 #123 的对照。** 逻辑缺陷 ∩E ＝ **9**（全在 `Messages`）；本篇 ∩E ＝ **0**。**四篇措辞层的在野交集：竞态 0、逻辑 9、拒绝服务 0**——**在野标注在这套语料里与"类型名"的选择几乎无关，而与具体条目（那一批 Messages 条目）绑定**。

# 12 远程刀：∩R ＝ 164，全部来自 12 条 Impact 句子

**12.1 总量与两个方向的比例。** #118 的远程触达集 **R ＝ 843 段／178 页／72 名**（Impact 含 13 个通道词形：`remote`、`network`、`internet`、`webs(?:ite|ite)`、`Wi‑Fi`、`wireless`、`Bluetooth`、`AirPlay`、`AirDrop`、`USB`、`proximity`、`cellular`、`packet`）。|Ds ∩ R| ＝ **164 段**——占 Ds 的 **34.9%**、占 R 的 **19.5%**。**即每三段拒绝服务条目有一段写了远程／网络／近场通道；同时全树的远程条目里约每五段有一段是自述拒绝服务。**

**12.2 承载 164 段的具体句子。** 附表 B 里 ∩R 非零的正好 **11 行**，其行和 ＝ **164**（与合计行相等，因为各行按 distinct 文本互斥切分）：`An attacker on the local network may be able to cause a denial-of-service` **49**、`A remote attacker may be able to cause a denial-of-service` **43**、`An attacker in a privileged network position may be able to cause a denial-of-service` **13**、`A remote attacker may be able to cause a denial of service` **11**、`A user in a privileged network position may be able to cause a denial-of-service` **9**、`A non-privileged user may be able to modify restricted network settings` **8**、`A remote user may be able to cause a denial-of-service` **8**、`An attacker in a privileged network position may be able to perform denial-of-service attack using crafted Wi-Fi packets` **7**、`Visiting a website may lead to an app denial-of-service` **6**、`…perform denial-of-service attack using crafted Bluetooth packets` **5**、`An attacker in physical proximity may be able to cause a denial-of-service` **5**。

**12.3 通道词的直接核对。** 只用 `remote|network|internet` 三个词的 Impact 集是 **553 段**，∩ Ds ＝ **153**；R 集用的是上面的十三词形表，843 段 ∩ Ds ＝ **164**。**164 − 153 ＝ 11 段**，逐段核对这 11 段的 Impact 只有两条 distinct 文本：**6 段** `Visiting a website may lead to an app denial-of-service`（`dyld`，靠 `website` 命中 R）、**5 段** `An attacker in physical proximity may be able to cause a denial-of-service`（`Wi-Fi`，靠 `proximity` 命中 R）；**反向差为 0**（三词命中的 153 段全部也在十三词形表里）。**所以"这一类的远程可达读数"取决于通道词表的宽度**：三词表给 32.6%（153／470）、十三词形表给 34.9%（164／470），附表 A 的 `远程可达 R` 行因此给出 843 的完整分母。

**12.4 远程句子与本地句子的分布。** 164 段远程 ∶ 其余 **306 段**未写通道词（其中 `Processing…`／`Visiting…` 182 段属于**输入形态而非通道形态**，第 9.3 节）。**"处理一个恶意文件"在公开文本里不写通道，因此既不计入 R、也不计入 WO——它是第三类可达性表述**，这一点在 #118 与 #119 两篇里都只能靠组件层推断。

**12.5 组件层的远程集中度。** 附表 F 的 ∩R 列合计 **164**，其中 `mDNSResponder` **24**、`Security` **23**、`AirPlay` **18**、`Wi-Fi` **12**、`LaunchServices` **8**、`CoreUtils` **15**。**`ImageIO` 的 62 段 ∩R ＝ 0、`WebKit` 的 35 段 ∩R ＝ 9**——**"图片解析导致拒绝服务"在措辞层完全不写可达通道，而"发现服务／认证／无线"相关条目几乎全写。**

# 13 类型词共现刀：六套零交、十二套有交

**13.1 判据与口径。** 对 18 套类型／机制词（三字段并集口径）逐一与 Ds 求交，并同时给出该词的 **Impact 单字段**段数。**注意这是三字段并集口径**：Ds 的 470 段在三字段任一字段处出现该词即计。逐套数据（并集 ∶ Impact 侧 ∶ ∩Ds）：`race condition` 232∶0∶0、`double free` 43∶0∶0、`uninitialized` 16∶8∶0、`leak*` 192∶181∶0、`encrypt*` 3∶3∶0、`inject*` 38∶5∶0、`memory` 1,967∶1,136∶135、`bounds*` 796∶8∶71、`out-of-bounds` 816∶14∶60、`type confusion` 145∶7∶33、`overflow` 374∶2∶31、`disclos*` 427∶343∶30、`dereference` 31∶**0**∶29、`integer` 158∶0∶14、`use after free` 127∶0∶10、`corrupt*` 840∶682∶7、`memory corruption` 388∶218∶7、`buffer` 200∶2∶7。

**13.2 六套零交。** `race condition`（232）、`double free`（43）、`uninitialized`（16）、`leak*`（192）、`encrypt*`（3）、`inject*`（38）——**与 Ds 交 0 段**。

**13.3 十二套有交。** `memory`（并集 1,967）→ **135**；`bounds*`（796）→ **71**；`out-of-bounds`（816）→ **60**；`type confusion`（145）→ **33**；`overflow`（374）→ **31**；`disclos*`（427）→ **30**；`dereference`（31）→ **29**；`integer`（158）→ **14**；`use after free`（127）→ **10**；`corrupt*`（840）→ **7**；`memory corruption`（388）→ **7**；`buffer`（200）→ **7**。

**13.4 两处必须点名的同数不同集。** `disclos*` 与 Ds 交 **30**，`DIS` 类 Impact 与 Ds 也交 **30**——**两个 30 是同一批段**（第 8.5 节的两句双类句；|disclos* 并集| ＝ 427、|DIS 类 Impact| ＝ 530、|A∩B| ＝ 30），**但 `leak*`（192）与 Ds 交 0、`expos*`（6 段）与 Ds 也交 0**，说明这 30 段**全部靠 `disclos*` 一个词命中**（`disclos*|expos*` 并集 433 段 ∩ Ds 仍是 30）。另 `dereference` 并集 **31 段**、Impact 侧 **0 段**、∩ Ds **29**（**93.5% 的空指针解除引用条目同时写了拒绝服务**）——**这是本篇最高的一个重合率，也是第 22 章误读五号的靶子。**

**13.5 与 #123 的结构对照。** 逻辑缺陷是**十套零交、五套有交且极小**；拒绝服务是**六套零交、十二套有交且大**。**这一差别来自定义层次而非文本偏好：`logic` 是一个不带机制的类型名，所以与所有机制词互斥；`denial of service` 是一个后果名，而后果常由机制词（越界、类型混淆、解除引用）引发并在同一段 Description 里写出。**

# 14 镜像与切块自检

**14.1 文件与块数。** 镜像 `_tmp/adv2/<id>.html` 共 **258** 份，排除 `122085` 后 **257 页**；读块器产出 **9,136** 段，与 #111 第 1 章、#122／#123 第 12 章同数。**任何一次重跑若得到不同的块数，本篇全部数字作废。**

**14.2 四锚点收块。** 块止于下一块起点、页脚免责声明、版权行、脚本行四个锚点之一。**只用前两锚点时，页面免责声明会进入字段文本**，#122 实测曾因此虚增 4 段。生成器对 `|Ds| ＝ 470`、`|denial of service 并集| ＝ 470`、`|terminat*| ＝ 1,546`、`|R| ＝ 843`、`|WO| ＝ 178`、`|E| ＝ 108`、`|G| ＝ 2,179`、`|HI| ＝ 273`、`|M2| ＝ 185`、`|RC| ＝ 232`、`|L| ＝ 550`、Impact 分母 **7,434** 共 **12 处锚点**设有 `die` 断言，任一漂移即中断。

**14.3 字段非空率。** 470 段的 Impact **全部非空**（空段 0）；致谢 **全部非空**（0）；Description 非空 **50 ＋ 420 中除去该字段缺失者**——本篇不按 Description 非空筛选，只按判据匹配。**致谢均值按含致谢段计**，9,132 段含致谢（差 4 段）。

**14.4 大小写与匹配模式。** 全部判据大小写不敏感，**除两条**：`DoS`（区分大小写的驼峰）与 `DOS`（区分大小写全大写）——它们测的是"是否使用缩写"，若忽略大小写会与 `dos` 无关词形混在一起。**这两条为 0 段的结论严格依赖大小写敏感**，第 4.2 节据此限定表述范围。另有一条实现注记：qr// 编译时不带 `/i` 时，插值进 `/…/i` 不会继承大小写不敏感，**本篇所有判据在 qr// 处直接带 `/i`**。

# 15 组件层：62／472 名，11 名全命中，62 名的 Impact 全集 3,643 段的 12.9%

**15.1 分母。** 附表 F 的列"该组件含 Impact 全集"＝ **该组件名下含 Impact 的段数**（不是该组件全部段数、更不是该组件漏洞总数）。62 名合计 **3,643 段**，Ds 的 470 段占其 **12.9%**，而 3,643 段占全树含 Impact 的 7,434 段的 **49.0%**。**这 62 个组件名承载了全树近一半的 Impact 文本。**

**15.2 头部。** `ImageIO` **62 段**、`WebKit` **35**、`mDNSResponder` **33**、`Security` **23**、`AirPlay` **18**、`CoreUtils` **15**、`Wi-Fi` **13**、`Audio`／`Foundation`／`SceneKit` 各 **11**。**前 2 名 97 段（20.6%）、前 10 名 232 段（49.4%）**。

**15.3 占比极端两端。** 全命中名（Ds 段数 ＝ 该名 Impact 全集）**11 个**：`CoreUtils` 15/15、`MobileStorageMounter` 8/8、`UIFoundation` 7/7、`smbx` 6/6、`Accelerate` 5/5、`Net-SNMP` 3/3、`SpringBoard` 3/3、`xar` 3/3、`MetricKit` 2/2、`Cellular` 1/1、`lldb` 1/1。**另一端**：`Kernel` **6/681（0.9%）**、`WebKit` **35/910（3.8%）**、`Safari` **3/92（3.3%）**、`CoreMedia` **8/150（5.3%）**。**小分母的 100% 与大分母的 0.9% 必须同时报出**（第 22 章误读八号）。

**15.4 高占比的中等分母名。** `mDNSResponder` **33/53（62.3%）**、`QuartzCore` **8/12（66.7%）**、`Calling Framework` **5/9（55.6%）**、`Heimdal` **8/19（42.1%）**、`CFNetwork` **8/20（40.0%）**、`libc` **4/10（40.0%）**。**"发现协议与网络栈"是这一类真正集中的地方**：这些名字的分母本身不大，但 Ds 占比都在四成以上。

**15.5 远程与近场的组件签名。** 附表 F 的 ∩R 列合计 **164**，`mDNSResponder` 24、`Security` 23、`AirPlay` 18、`CoreUtils` 15、`Wi-Fi` 12。**`CoreUtils` 的 15 段全部属 R（100%）**、`AirDrop`／`Bluetooth`／`Heimdal`… 中 `AirDrop` 5/5、`Bluetooth` 5/5、`Calling Framework` 5/5 也全属 R。**近场通道词的 164 段几乎全部挂在这十几个名字上**（第 12.5 节）。

**15.6 与在野／无前提的组件对账。** 附表 F 的 ∩WO 列与 ∩E 列**逐行全为 0**（合计 0／0）——与第 11 章的集合级零交一致，**组件层没有任何一个名字出现例外**。

# 16 页、编号与致谢层

**16.1 页分布。** Ds 覆盖 **155／257 页（60.3%）**，页号范围 `120304`–`149043`。按"Ds 段所在页的总段数"分箱：1 段页 **43**、2 段页 **44**、3 段 **20**、4 段 **20**、5 段 **9**、6–9 段 **14**、10–19 段 **5**，合计 **155**（附表 D 断言闭合）。**没有任何一页整页都是 Ds 段**（全 Ds 页 ＝ 0）。

**16.2 Top10 页。** `128071` **13**、`128072` **13**、`128067` **12**、`127115` **11**、`149035` **11**、`127110` **9**、`127118` **8**、`148287` **8**、`127111` **7**、`127119` **7**，合计 **99 段（占 Ds 21.1%）**。**最大单页 13 段，占该页 151 段的 8.6%**——**这一类不集中在少数页面，而是摊在 60% 的页面上**。

**16.3 同页共存。** 这 155 页共 **8,679 段**，Ds 占 **5.4%**。**"某一页出现拒绝服务条目"意味着该页其余段平均每页 53.0 段不写本体词**（第 1.4 节的第三套分母）。

**16.4 编号层。** 470 段的致谢里 distinct CVE 编号 **105 个**（全树 2,108 个的 5.0%）；**451 段带 1 个编号、19 段带 2 个编号、0 段不带编号**（分箱闭合 470）。**105 个编号中 104 个独占**（只出现在 Ds 段，其余 8,666 段一次都不出现）；**唯一不独占的是 `CVE-2024-27877`**——它在 Ds 里有 **2 段**（`AppleVA`，页 `120910`／`120912`），另有 **1 段非 Ds 段**也带这个编号（该段不写 logic，也不属 #123）。**因此"共用编号"的说法只对这 1 个编号成立，且共用对象不是逻辑篇。** 与 #123 的编号层关系是另一件事：**Ds 编号集与 L 编号集交 **6** 个**（`CVE-2024-27812`、`CVE-2024-44183`、`CVE-2025-30471`、`CVE-2025-31226`、`CVE-2025-46290`、`CVE-2026-28932`），这 6 个编号的**全部**出现段都同时写了 `logic` 与 `denial-of-service`（逐编号核对：Ds 段数＝L 段数＝双段数、其他段 0）；这 6 个编号**同时落在两个独占集里**（|Ds 独占 ∩ L 独占| ＝ 6），因为 Ds∩L ＝ **33 段**（第 17.2 节）。

**16.5 致谢层。** 470 段**全部有致谢**；合计 **729 人次**、**均值 1.55**（全树 9,132 段均值 **1.80**、最大 37）；分箱 1 人 **289**、2 人 **133**、3 人 **18**、4 人 **30**、**最大 4 人**（分箱闭合 470）。**这一类的条目平均比全树少 0.25 个报告人**，且从不出现大规模集体致谢。

**16.6 版式域。** 470 段中 **3 段落在域 B**（粗体版式的 11 页，全树 91 段）。**域 B 的 91 段里 3.30% 写了本体词，与域 A 的 5.16%（467／9,045）同一量级**——**版式不改变这一类的表达**。

# 17 与 #122／#123 的对账：三种字段形态放在一起

**17.1 三篇核心量并排。**

| 篇 | 判据 | Impact | Description | 致谢 | 并集 | 两字段交集 | 页 | 名 |
|---|---|---:|---:|---:|---:|---:|---:|---:|
| #122 竞态 | `race condition`（Desc） | 0 | 232 | 0 | 232 | 0 | 95 | 40 |
| #123 逻辑 | `logic` 词界 | 10 | 540 | 0 | 550 | 0 | 162 | 81 |
| #124 拒绝服务 | `denial of service` | 457 | 50 | 0 | 470 | **37** | 155 | 62 |

**17.2 三个集合关系逐条核对。** Ds ∩ RC ＝ **0**（470 ∶ 232）；Ds ∩ L ＝ **33**（470 ∶ 550，33／470 ＝ 7.0%、33／550 ＝ 6.0%）；RC ∩ L ＝ **0**（#122 与 #123 实测零交）。**所以三篇两两之间最多只有一对有 33 段重合，其余两对零交。**

**17.3 三篇并集。** 232 ＋ 550 ＋ 470 ＝ 1,252；两两交集 0 ＋ 0 ＋ 33 ＝ 33；三者同段 0；并集 ＝ 1,252 − 33 ＝ **1,219 段（占全树 13.3%）**。**"类型名或后果名被写进公开文本"的条目约占全树八分之一**（其余条目只写机制词或不写类型）。

**17.4 三篇的致谢与编号对照。** 竞态：232 段含编号、distinct **60** 个、**60 个全部独占**（#122 第 9 章）；逻辑：550 段 distinct **167** 个、**167 个全部独占**、921 人次、均值 1.68；拒绝服务：470 段 distinct **105** 个、**104 个独占**、729 人次、均值 **1.55**。**三篇的致谢均值都低于全树 1.80**，其中本篇最低。**三篇的独占集两两关系：|Ds 独占 ∩ L 独占| ＝ 6、|RC 独占 ∩ Ds 独占| ＝ 0**（竞态的 60 个编号一个都不落在 Ds 段，与第 17.3 节的 ∩RC ＝ 0 段一致）。

**17.5 组件层三篇对照。** 逻辑篇 Top2 为 `WebKit`（71）＋`Kernel`（48）＝ 119／550 ＝ **21.6%**；本篇 Top2 为 `ImageIO`（62）＋`WebKit`（35）＝ 97／470 ＝ **20.6%**。**两篇的组件名全集是 62 名与 81 名，重合 21 个名字；两篇各自的 Top10 只重合 3 个**（`ImageIO`、`WebKit`、`Security`）。`Kernel` 在逻辑篇是第 2 名（48 段）、在本篇只排到第 **8** 名之后——**6／681（0.9%）**；`ImageIO` 在本篇是第 1 名（62 段）、在逻辑篇只 **14** 段。**同一组件在两套类型词下的占比可以相差一个数量级。**

# 18 中文手册层：`拒绝服务` 1 次／1 页，21 个对照词全零

**18.1 可用面。** 《Apple 平台安全保护》zh-CN 转储 179 页，窗口切分后 **178 页可用**（1 页为无正文窗口，与 #122／#123 同一处理）。附表 E 的**覆盖页占比**以 178 为分母。

**18.2 本体词组。** `拒绝服务` **1 次／1 页**（《macOS 中的防火墙安全性》，占可用页 **0.6%**）；`服务拒绝`／`瘫痪`／`停摆` **0 次**；`可用性` **7 次／6 页**（最集中在《钥匙串数据保护》2 次）；`中断` **8 次／6 页**（最集中在《Mac 电脑的直接内存访问保护》2 次）；`干扰` **2 次／2 页**。**即中文手册里"拒绝服务"这一术语出现过一次，且出现在 macOS 防火墙语境，不是 iOS 弱点分类语境。**

**18.3 终止／崩溃／挂起三组。** `终止` **13 次／7 页**（《iOS 中的车钥匙安全性》4 次）、`退出` **22 次／11 页**、`关闭` **76 次／43 页**、`强制` **53 次／31 页**、`崩溃` **6 次／4 页**、`异常` **3 次／3 页**、`故障` **6 次／4 页**；**`挂起`／`无响应`／`卡住`／`冻结`／`停滞`／`死锁`／`注销` 全部 0 次**。**英文侧的 `hang`／`unresponsive`／`freez*` 0 段与中文侧对应词 0 次构成双侧缺席**（第 6.1、18.3 节）——**两套公开文本都不使用"卡住／无响应"语汇描述弱点。**

**18.4 运维语汇与弱点语汇的分野。** `重启` **41 次／20 页**、`恢复` **559 次／178 页**、`开机` **7 次／7 页**、`唤醒` **8 次／6 页**、`内存` **511 次／178 页**、`限制` **478 次／178 页**、`验证` **432 次／178 页**、`处理` **337 次／178 页**——**这些高频词属于功能与安全机制描述**，与英文侧 `termination`／`memory` 的**条目级用法不是同一层次**（英文侧那 1,546 段是弱点后果字段）。`输入验证` **2 次／1 页**、`错误处理` **0 次**、`修复` **14 次／8 页**、`补丁` **4 次／1 页**。

**18.5 类型与后果组。** `泄露` **30 次／11 页**（最集中在《密码监视》15 次）、`绕过` **23 次／9 页**、`沙盒` **27 次／11 页**、`检查` **66 次／30 页**、`溢出` **1 次／1 页**、`越界` **1 次／1 页**、`逻辑` **4 次／2 页**（#123 实测全为硬件语汇）、**`竞态`／`提权`／`沙箱` 0 次**。**中文层的类型词分布与英文侧一样是稀疏的**，`拒绝服务` 的 1 次是所有八类后果词里最低的之一。

**18.6 中英两侧的结论差。** 英文侧：**本体词有 470 段、且带 27 条固定句式**；中文侧：**术语只出现 1 次**。**公开文档体系用英文给出弱点后果归类词，但中文文档体系不承载这一归类**——这与 #122（`竞态` 0 次）、#123（`逻辑` 4 次且全为硬件语汇）构成同一模式的第三个样本。

# 19 与相邻各篇的口径衔接

**19.1 与 #121（组合缺陷 M2）。** M2 集 **185 段** ∩ Ds ＝ **30**（16.2%），全部是第 8.5 节两句"或泄露内存内容"。**这一类是唯一成规模参与"同段并写多类后果"的类型词**——#123 逻辑只交 5 段、#122 竞态只交 14 段。

**19.2 与 #118（远程触达）。** ∩R **164**（第 12 章）；附表 B 的 ∩R 行和恰等于合计 164（各行互斥切分所致）。**这是措辞层四篇里唯一"远程交集大于在野交集"的一篇**（164 ∶ 0）。

**19.3 与 #119（无前提）／#114（在野）。** 两集对 Ds 的交集都是 **0**（第 11 章）。**#119 的 `without` 178 段与 Ds 零交意味着：公开文本从不把"无需用户操作"与"拒绝服务"写在同一条目**（不推断实际可达性）。

**19.4 与 #116／#120（动名词与交互触发）。** ∩G **182**、∩HI **6**（第 9.3、9.4 节）。**本篇的 182 段与 `may lead to` 的 182 段逐段相等**，这是四篇之间最紧的一次跨篇对账。

**19.5 与 #111／#103–#113（手册层各篇）。** 中文侧 `恢复` 559 次／178 页、`内存` 511 次／178 页与英文侧 `memory` 1,967 段并集**不构成对应关系**（前者是功能描述、后者是弱点后果字段）；第 18.4 节给出同型的 `检查` 例子（#123 第 18.5 节的对应现象）。

# 20 复算步骤与断言

**20.1 环境。** 在报告目录运行 Perl；镜像文件位于 `_tmp/adv2/<id>.html`（258 份，排除 `122085`）；中文转储位于 `_tmp/sec*.txt`（179 份，窗口切分后 178 页可用）。读块口径由 `tl111load.pl` 提供，与 #111–#123 完全共用。

**20.2 六步。**（1）组装并运行附表生成器：`head -50 _tmp/tw123tab.pl > _tmp/tw124tab.pl`，再 `cat _tmp/tw124tab_body.pl >> _tmp/tw124tab.pl`，然后 `perl _tmp/tw124tab.pl`。它生成 `_tmp/tw124A.md`…`_tmp/tw124F.md` 六个附表片段、打印各组行数与闭合值，任一 `die` 断言失败直接中止。（2）正文探针按同一方式组装：`{ head -50 _tmp/tw123tab.pl; sed -n '2,11p' _tmp/tw124tab_body.pl; cat _tmp/tw<名>.pl; } > _tmp/tw124run.pl && perl _tmp/tw124run.pl`（前 50 行是公共前段与九套外部集，第 2–11 行是 Ds 集合定义与锚点断言）。（3）`tw124iprobe.pl` 复算字段刀、终止刀、可用性刀、八类、外部集、页／编号／致谢；`tw124jprobe.pl` 复算 Desc‑only 13 段与"Impact 不含本体词"13 段的同一性、两种拼写、短语细节、谓语三档闭合、在野相关短语、0 类与 2 类原文。（4）`tw124kprobe.pl` 复算第 12.3 节的通道词表宽度对照（553∶153 对 843∶164、差 11 段、反向差 0）与第 16.4 节编号归属；`tw124lprobe.pl` 复算第 17.4 节三篇独占编号与第 16.6 节版式域占比；`tw124mprobe.pl` 复算 Ds 编号集与 L 编号集的 6 个交及其逐编号"双段"性质。（5）`tw124pprobe.pl` 复算第 13 章十八套类型词的"并集∶Impact 侧∶∩Ds"三段数据；`tw124qprobe.pl` 复算第 17.5 节的组件重合；`tw124rprobe.pl` 复算第 10.3 节的 42 条 distinct Description 与第 10.5、11.2、11.3 节的短语。（6）附表自身的 `die` 断言：B 段合计 ＝ 470、重复 ＝ 0、行数 ＝ 27、是 457／否 13；C 三条 Description 文本、段合计 ＝ 50、重复 ＝ 0，续表 4,862／233 且 Ds 去重 ＝ 行和；D 页分箱 ＝ 155、编号分箱 ＝ 470、致谢分箱 ＝ 470；E 可用页数 ＝ 178；F 组件段合计 ＝ 470。

**20.3 关键断言清单。** `|Ds| ＝ 470`；`|Di| ＝ 457`、`|Dd| ＝ 50`、`|Da| ＝ 0`、`|Di ∩ Dd| ＝ 37`；连写 **428** ＋ 空格 **42** ＝ 470；`denial` 词界并集 **42 ⊂ Ds**；`DoS`／`DOS` **＝ 0**、`denial of (service|access|experience) 三形并集` Ａ `denial of service` 逐数相同（附表 A 组 1 首两行）；`terminat*` **1,546** 且 ∩ Ds **＝ 2**；`unexpected system termination` **802** ∩ **0**、`unexpected app termination` **612** ∩ **0**、两句并集 **1,414** ∩ **0**；`crash*` **499** ∩ **0**；八类 Impact 口径 `DOS 459`∶并集口径 **470**、八类 ∩Ds 行和 **507**；段级 **0 类 11／1 类 429／2 类 30／3 类 0**、类次 **489**；外部集 **∩R 164／∩WO 0／∩E 0／∩G 182／∩HI 6／∩M2 30／∩L 33／∩RC 0／∩UU 25**；谓语 **260＋182＋15 ＝ 457**；附表 A **67 项（全零 18：组1 2／组2 7／组3 9）**、附表 B **27 行**且 ∩R 非零 **11 行**、Impact distinct 前 10 条 **361 段**、附表 C **3 行／50 段**（∩R 行和 40 ＝ 合计 40）、附表 C 续表 **13 项／4,862 ∶ 233 ∶ 4,629**（233 ＝ 去重值，覆盖 **129 页／40 名**，470−233 ＝ **237**）、附表 D 页 **155**／编号 **105 个 distinct、104 独占**／致谢 **729 人次**、附表 E **61 词（全零 21）**、附表 F **62 名／470 段／全集 3,643**；470 段的 Description distinct **42** 条（最大 **82**）；十八套类型词里**六套零交、十二套有交**，`bounds*` 并集 **796**；版式域 **3／91 ＝ 3.30%** 对 **467／9,045 ＝ 5.16%**；与 #123 的组件重合 **21 名**、Top10 重合 **3 名**；中文层 `拒绝服务` ＝ **1 次／1 页**、`服务拒绝` ＝ **0 次**。

**20.4 本篇不自证的部分。** 复算只能证明**文本分布**，不能证明任何一段拒绝服务条目是否真的造成可用性影响、其触发是否需要交互、是否可远程达到；组件占比的分母是"该组件含 Impact 的段数"，不是"该组件全部段数"，也不是"该组件漏洞总数"（第 15.1 节）。

# 21 缺口清单

**21.1 文本没给的。** 470 段里**没有任何一段写出被拒绝的服务名**（`denial-of-service` 后面不带宾语的除外：`…perform denial-of-service attack using crafted Wi-Fi packets` 12 段带了攻击面，其余 458 段不带）；`persistent` 的 7 段不写"持续多久"；`or potentially disclose memory contents` 的 30 段不写泄露哪一类内存。**这些不是可以补测的量，是公开文本没有的位置。**

**21.2 判据留下的模糊。** `improved validation` 与 `improved input validation` 在语义上重叠（全树 366 ∶ 641，Ds 内 16 ∶ 113），二者与 `input validation` 词界（并集 676、∩Ds 113）之间的关系**无法从文本恢复**；第 22 章误读三号针对把三者合并统计。

**21.3 需要外部知识才能填的。** 104 个独占编号对应的产品版本；`CoreUtils` 15/15 与 `Accelerate` 5/5 这类全命中名的成因；那 1 个与 #123 共用的编号为何同时被自述为逻辑缺陷与拒绝服务。**本篇一律不做推断。**

**21.4 可以补测但本篇未测的。** `dereference` 31 段与 Ds 的 29 段重合的逐页分布；`type confusion`（145）∩Ds 33 段的句子结构是否与第 8.5 节的双类句同形；`shutdown` 16 段的组件归属（∩Ds 0，但同为可用性语汇）；中文侧 `恢复` 559 次与账号恢复语境的进一步拆分。

# 22 常见误读纠正

**误读一：470 段说明"iOS 有大量拒绝服务漏洞"。** 正：470 是**一个短语（`denial of service`）在三字段并集上的段数**，与"漏洞数量"不同：470 段带 **105 个编号**，且 19 段带 2 个编号。**同一条目可以在多个页面重复呈现**（如第 5.4 节的 `MetricKit` 一条占 2 段）。

**误读二：∩E ＝ 0 说明这类缺陷没有在野利用、∩WO ＝ 0 说明这类攻击都需要交互。** 正：0 是**公开文本从未把该标注与该后果词写在同一段**（第 11 章）。它既不证明可达性、也不证明需要交互；**措辞层的零交不是事实层的零存在**。

**误读三：`improved validation`、`improved input validation`、`input validation` 是同义套话，可以合并。** 正：三者全树分母 **366 ∶ 641 ∶ 676**、Ds 内 **16 ∶ 113 ∶ 113**。合并会把 676 段的主体误算进某一句；且第 7.5 节已给出 113 ∶ 50 的口径差。**引用时必须点名短语原文与所在字段。**

**误读四：`terminat*` 有 1,546 段，所以终止语汇是这一类的主体表达。** 正：∩ ＝ **2 段**（0.13%）。两句终止套话（1,414 段）与本篇**一段不共**（第 5 章）。**这是同树不同概念，不是同概念的两种写法。**

**误读五：`dereference` 31 段里 29 段属 Ds，所以空指针缺陷就是拒绝服务缺陷。** 正：29 是**并集口径**的重合，即同一段文本里出现了这两个词；**Impact 侧的 `dereference` 是 0 段**（第 13.1 节该行的三段数据 31∶0∶29），说明 `null pointer dereference` 写在 Description 的**类型名槽位**上、而 denial-of-service 写在 Impact 的**后果槽位**上（第 10.4 节）。**两句是同一缺陷的两处描述，不是两个缺陷。**

**误读六：`DOS` 类 ∩Ds ＝ 470 与 `DOS 类 Impact ∩Ds ＝ 459` 矛盾。** 正：两数口径不同——**470 是三字段并集**（附表 A 行）、**459 是 Impact 单字段**（第 1.3 节表、附表 B 列）。第 8.4 节给出差 11 段就是第 8.3 节的 0 类段。

**误读七：428 段连写、42 段空格，所以有 470 ＋ 42 ＝ 512 段写了本体词。** 正：428 与 42 是**同一批 470 段的两种拼写**，相加恰为 470（第 4.1 节），**不存在第三种拼写、也不存在两种拼写同时出现的段**。

**误读八：`CoreUtils` 15/15、`Accelerate` 5/5 说明这些组件"全是拒绝服务缺陷"。** 正：分母是**该组件含 Impact 的段数**，最大只有 15 段（第 15.3 节）；同一列里 `Kernel` 是 6/681（0.9%）、`WebKit` 是 35/910（3.8%）。**比例与绝对数必须同报，小分母的 100% 不构成优先级。**

**误读九：∩G ＝ 182 与 `may lead to` ＝ 182 相等，所以两者是同一个集的两种叫法。** 正：它们**在 Ds 内逐段相等**（第 9.3 节给出 |A|、|B|、|A∩B| 三值），但在全树不等——G ＝ 2,179 段、`may lead to` 的 Impact ＝ 1,731 段。**"在子集内重合"不等于"是同一个集"**；这条限定语必须保留。

# 23 结论

**一、这一类弱点在公开文本里的位置是"Impact 为主、Description 有独占 13 段、两字段重叠 37"。** Impact **457**、Description **50**、致谢 **0**，并集 **470 段／155 页（60.3%）／62 名（13.1%）**，占全树 **5.14%**、占含 Impact 段 **6.32%**。**470 ＝ 420 ＋ 13 ＋ 37**，这是措辞层四篇里唯一的第三种字段形态（第 3 章）。

**二、这一类的词形只有两种拼写，机制语汇一个都不带。** 连写 **428** ∶ 空格 **42**（互斥且闭合）、`DoS`／`DOS`／`denial of access`／`denial of experience` **全零**；`terminat*` 1,546 段 ∩ **2**、两句终止套话 1,414 段 ∩ **0**、`crash*` 499 段 ∩ **0**、附表 A 六组 67 项判据里 **18 项三字段全零**（见第 2 章第四条与第 6 章）。**"拒绝服务"是一个被写在后果字段的名词，不是一套描述中断现象的语汇**（第 4–6 章）。

**三、这一类在后果层是完整的，但只与"泄露"共段。** **27 条 distinct Impact 全文**（最大 **95 段／20.2%**）、**0 类 11／1 类 429／2 类 30／3 类 0**、类次 **489**（每段 1.04）；`ACE`／`BYP`／`FULL`／`MEM`／`PRI`／`SBX` 六类 Impact 侧 **全为 0**；30 段双类全部来自两句 `…denial-of-service or potentially disclose memory contents`（第 8 章）。

**四、这一类不带任何在野与无前提标注，但三分之一的条目写了远程通道。** ∩E **0**、∩WO **0**、∩RC **0**、`extremely sophisticated`／`actively exploited`／`consent`／`prompting` 交集 **0**；∩R **164**（占 Ds 34.9%、占 R 19.5%）且全部由 **12 条** Impact 句子承载；谓语 **260 ＋ 182 ＋ 15 ＝ 457** 闭合；470 段全部用 `may`，`will`／`could` **0 段**（第 9、11、12 章）。

**五、中文手册层几乎不存在这一类。** `拒绝服务` **1 次／1 页**（且是 macOS 防火墙语境）、`服务拒绝`／`挂起`／`无响应`／`冻结`／`停滞`／`死锁`／`节流`／`过载`／`错误处理`／`竞态`／`提权` 等 **21 词全零**，而 `恢复` **559 次／178 页**、`内存` **511 次／178 页**、`重启` **41 次／20 页**全是运维语境。**英文侧"有固定的 27 条后果句式"，中文侧"连术语都只出现一次"**（第 18 章）。

**六、可执行的防御含义（不涉及任何利用）。** 对公开信息抽取而言：**本体词判据必须同时列连写与空格两种拼写（漏一种损失 91.1% 或 8.9%），不能用 `terminat*` 或 `crash` 代理（代理会得到 1,546／499 段而只有 2／0 段是真自述），也不能用 `service` 词界（46 段里 4 段与本体词无关）**；**以修复短语为键时 `improved input validation` 的 641 段只有 113 段属这一类（17.6%），`improved restrictions` 的 47 段一条都不属**——**高精度召回要回查 Impact 侧的 27 条 distinct 文本，而不是 Description 侧的修复套话**。对组件优先级而言，本语料里这一类段的 **49.4% 落在前 10 名组件**、**34.9% 有远程触达标注（∩R 164）**、`CoreUtils` 的 15 段 **100% 属 R**、而 `ImageIO` 的 62 段 **0% 属 R**——**这条分布是措辞层的观测，用于校准"公开信息能告诉我们什么"，不能替代资产侧的真实可用性评估。**

---

# 附表 A 拒绝服务判据六组电池（68 行）

**读法：** 列名"Impact 段／页／名"三列是该判据在 **Impact 单字段**的命中规模；"三字段并集段"＝ Impact∪Description∪致谢；"∩Ds"列与"∩R／∩WO／∩E"三列都按**该判据的三字段并集**与对应外部集求交（三个外部集本身按 Impact 单字段定义）。因此对只在 Description 出现的判据（组 4 的修复套话族），"∩R"列仍可非零——那是"该短语的段集合 ∩ R 的 Impact 段集合"。组 1 首两行给出拼写刀（第 4 章）；组 1 第二行 `denial of service|access|experience 三形并集` 的四个数**与第一行完全相同**，这正是 `denial of access` 与 `denial of experience` 两形 **0 段**的证据（第 4.3 节）；`DoS`／`DOS` 两行**大小写敏感**，且 `DOS 全大写` 行与组 6 的 `DOS 类` 行不是同一判据（后者是八类复合判据、三字段并集 2,014 段）。组 2 给出第 5 章的终止刀原始数据，组 3 给出第 6 章的可用性刀。**全零 18 项**的分布是组 1 **2** 项（`DoS`、`DOS`）、组 2 **7** 项（`quit`、`quit unexpectedly`、`kill`、`freez*`、`stall`、`watchdog`、`forced restart`）、组 3 **9** 项，组 4—组 6 无全零项。组 5 是九套外部集的原始行（R／WO／E／UU／G／HI／M2／RC／L）；组 6 的八类 ∩Ds 列**并集口径行和合计 507**（其中 `DOS 类` 一行独占 470），而 Impact 口径的类次是 **489**（第 8.4 节）。**合计行的"67 项（全零 18 项）"是判据条数；"∩Ds 470"与"∩R 843"两列给出 |Ds| 与 |R| 两个集合基数本身，不是列求和；Impact 列合计 19,599 是 67 项各自 Impact 命中数的求和，同样不是任何集合的基数。**

| 组 | 判据 | Impact 段 | 页 | 名 | 三字段并集段 | ∩Ds | ∩R | ∩WO | ∩E |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|
| 拒绝服务本体词 | `denial of service 连写` | 457 | 155 | 60 | 470 | 470 | 164 | 0 | 0 |
| 拒绝服务本体词 | `denial of service|access|experience 三形并集` | 457 | 155 | 60 | 470 | 470 | 164 | 0 | 0 |
| 拒绝服务本体词 | `denial 词界` | 39 | 26 | 7 | 42 | 42 | 11 | 0 | 0 |
| 拒绝服务本体词 | `service 词界` | 39 | 26 | 7 | 46 | 42 | 11 | 0 | 0 |
| 拒绝服务本体词 | `DoS 缩写` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 拒绝服务本体词 | `DOS 全大写` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 拒绝服务本体词 | `拒绝服务后果八类 DOS` | 2003 | 176 | 130 | 2014 | 470 | 343 | 0 | 0 |
| 终止与中断词 | `terminat* 词族` | 1546 | 165 | 102 | 1546 | 2 | 179 | 0 | 0 |
| 终止与中断词 | `terminate 词界` | 26 | 26 | 4 | 26 | 2 | 0 | 0 | 0 |
| 终止与中断词 | `termination 词界` | 1520 | 165 | 101 | 1520 | 0 | 179 | 0 | 0 |
| 终止与中断词 | `unexpected system termination` | 802 | 129 | 53 | 802 | 0 | 69 | 0 | 0 |
| 终止与中断词 | `unexpected app termination` | 612 | 148 | 56 | 612 | 0 | 72 | 0 | 0 |
| 终止与中断词 | `terminate the process` | 24 | 24 | 3 | 24 | 0 | 0 | 0 | 0 |
| 终止与中断词 | `quit 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 终止与中断词 | `quit unexpectedly` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 终止与中断词 | `kill 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 终止与中断词 | `crash* 词族` | 499 | 128 | 17 | 499 | 0 | 0 | 0 | 0 |
| 终止与中断词 | `freez* 词族` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 终止与中断词 | `stall 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 终止与中断词 | `watchdog 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 终止与中断词 | `forced restart` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可用性与资源词 | `unavailable 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可用性与资源词 | `hang 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可用性与资源词 | `unresponsive 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可用性与资源词 | `reboot 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可用性与资源词 | `restart 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可用性与资源词 | `shutdown 词界` | 16 | 12 | 2 | 16 | 0 | 0 | 0 | 0 |
| 可用性与资源词 | `loop 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可用性与资源词 | `infinite loop` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可用性与资源词 | `exhaust* 词界` | 0 | 0 | 0 | 5 | 5 | 5 | 0 | 0 |
| 可用性与资源词 | `resource 词界` | 0 | 0 | 0 | 5 | 5 | 5 | 0 | 0 |
| 可用性与资源词 | `memory 词界` | 1136 | 174 | 72 | 1967 | 135 | 138 | 0 | 60 |
| 可用性与资源词 | `rate limit 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可用性与资源词 | `persist* 词界` | 46 | 26 | 10 | 46 | 7 | 3 | 0 | 0 |
| 可用性与资源词 | `reproduc* 词界` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 修复套话词 | `improved input validation` | 0 | 0 | 0 | 641 | 113 | 140 | 0 | 1 |
| 修复套话词 | `improved validation` | 4 | 4 | 2 | 367 | 16 | 13 | 15 | 23 |
| 修复套话词 | `input validation` | 21 | 13 | 4 | 676 | 113 | 146 | 0 | 1 |
| 修复套话词 | `removing the vulnerable code` | 0 | 0 | 0 | 173 | 15 | 8 | 0 | 4 |
| 修复套话词 | `improved error handling` | 0 | 0 | 0 | 18 | 8 | 0 | 0 | 0 |
| 修复套话词 | `improved restrictions` | 0 | 0 | 0 | 47 | 0 | 3 | 0 | 0 |
| 修复套话词 | `improved state management` | 0 | 0 | 0 | 662 | 8 | 127 | 45 | 14 |
| 修复套话词 | `improved checks` | 0 | 0 | 0 | 1018 | 73 | 151 | 31 | 31 |
| 修复套话词 | `was addressed with` | 4 | 4 | 2 | 6581 | 416 | 735 | 163 | 104 |
| 修复套话词 | `addressed by` | 0 | 0 | 0 | 371 | 15 | 31 | 4 | 4 |
| 修复套话词 | `addressed 词界` | 25 | 14 | 6 | 7228 | 436 | 819 | 178 | 108 |
| 修复套话词 | `improved 词界` | 4 | 4 | 2 | 6038 | 412 | 742 | 130 | 104 |
| 修复套话词 | `validation 词界` | 25 | 14 | 6 | 1341 | 137 | 184 | 15 | 24 |
| 修复套话词 | `handling 词界` | 4 | 4 | 2 | 1458 | 114 | 105 | 25 | 2 |
| 修复套话词 | `restrict* 词界` | 130 | 79 | 29 | 793 | 16 | 75 | 33 | 4 |
| 外部集对照 | `远程可达 R` | 843 | 178 | 72 | 870 | 164 | 843 | 27 | 9 |
| 外部集对照 | `without 词界` | 178 | 97 | 37 | 189 | 0 | 29 | 178 | 5 |
| 外部集对照 | `exploit* 词界` | 108 | 76 | 12 | 108 | 0 | 4 | 0 | 108 |
| 外部集对照 | `user 词界` | 1627 | 175 | 200 | 1669 | 25 | 147 | 105 | 0 |
| 外部集对照 | `动名词开头集 G` | 2179 | 224 | 98 | 2179 | 182 | 108 | 0 | 53 |
| 外部集对照 | `非处理/解析类 HI` | 273 | 102 | 35 | 273 | 6 | 108 | 0 | 0 |
| 外部集对照 | `≥2 后果类 M2` | 185 | 82 | 35 | 185 | 30 | 23 | 0 | 0 |
| 外部集对照 | `race condition 集 RC` | 232 | 95 | 40 | 232 | 0 | 14 | 3 | 0 |
| 外部集对照 | `logic 词界集 L` | 550 | 162 | 81 | 550 | 33 | 99 | 24 | 9 |
| 后果强度八类 | `ACE 类` | 336 | 120 | 43 | 336 | 0 | 36 | 0 | 31 |
| 后果强度八类 | `BYP 类` | 291 | 114 | 56 | 301 | 0 | 42 | 0 | 20 |
| 后果强度八类 | `DIS 类` | 530 | 144 | 63 | 603 | 30 | 74 | 0 | 2 |
| 后果强度八类 | `DOS 类` | 2003 | 176 | 130 | 2014 | 470 | 343 | 0 | 0 |
| 后果强度八类 | `FULL 类` | 63 | 43 | 18 | 63 | 0 | 7 | 0 | 0 |
| 后果强度八类 | `MEM 类` | 220 | 85 | 17 | 390 | 7 | 3 | 0 | 44 |
| 后果强度八类 | `PRI 类` | 124 | 62 | 30 | 124 | 0 | 0 | 0 | 8 |
| 后果强度八类 | `SBX 类` | 418 | 119 | 67 | 475 | 0 | 40 | 6 | 8 |
| **合计** | 67 项（全零 18 项） | — | — | — | — | 470 | 843 | — | — |

# 附表 B 二十七条 distinct Impact 全文 × 含本体词与否 × 五个外部集 × 后果八类（28 行）

**读法：** 每行是一条 Impact 段全文，**各行按 distinct 文本互斥切分并覆盖全部 470 段**（合计行段列 ＝ 470、重复段 ＝ 0），因此**五个外部集列与八类列的行和等于合计行**（∩R 164、∩WO 0、∩E 0、∩M2 30、∩term 2、DIS 30、DOS 459）。**"Impact 含本体词"列的"是 457／否 13"就是第 3 章的字段刀原始数据**：13 段（三条"否"文本 8＋3＋2）的 Ds 身份完全来自 Description。页列合计 **396 大于 155 页**、名列合计 **82 大于 62 名**，因为**同页可有多条不同 Impact 句、同名组件可拆到多句**，页列与名列**不可当作并集去重值引用**。`∩term` 列非零的只有 `An unprivileged process may be able to terminate a root processes`（2 段，第 5.4 节）。`DIS` 列非零的两行（22 ＋ 8 ＝ 30）与 `∩M2` 列的两行完全重合（第 8.5 节）。

| Impact 全文 | 段 | 页 | 名 | Impact 含本体词 | ∩R | ∩WO | ∩E | ∩M2 | ∩term | ACE | BYP | DIS | DOS | FULL | MEM | PRI | SBX |
|---|---:|---:|---:|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| An app may be able to cause a denial-of-service | 95 | 64 | 21 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 95 | 0 | 0 | 0 | 0 |
| An attacker on the local network may be able to cause a denial-of-service | 49 | 28 | 3 | 是 | 49 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 49 | 0 | 0 | 0 | 0 |
| Processing web content may lead to a denial-of-service | 46 | 44 | 5 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 46 | 0 | 0 | 0 | 0 |
| A remote attacker may be able to cause a denial-of-service | 43 | 37 | 11 | 是 | 43 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 43 | 0 | 0 | 0 | 0 |
| Processing an image may lead to a denial-of-service | 29 | 29 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 29 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted image may lead to a denial-of-service | 27 | 24 | 3 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 27 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted file may lead to a denial-of-service or potentially disclose memory contents | 22 | 22 | 5 | 是 | 0 | 0 | 0 | 22 | 0 | 0 | 0 | 22 | 22 | 0 | 0 | 0 | 0 |
| An app may be able to cause a denial of service | 19 | 12 | 4 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 19 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted message may lead to a denial-of-service | 16 | 16 | 2 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 16 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted file may lead to a denial-of-service | 15 | 15 | 2 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 15 | 0 | 0 | 0 | 0 |
| An attacker in a privileged network position may be able to cause a denial-of-service | 13 | 12 | 4 | 是 | 13 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 13 | 0 | 0 | 0 | 0 |
| A remote attacker may be able to cause a denial of service | 11 | 9 | 2 | 是 | 11 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 11 | 0 | 0 | 0 | 0 |
| A user in a privileged network position may be able to cause a denial-of-service | 9 | 9 | 2 | 是 | 9 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 9 | 0 | 0 | 0 | 0 |
| A non-privileged user may be able to modify restricted network settings | 8 | 8 | 1 | 否 | 8 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| A remote user may be able to cause a denial-of-service | 8 | 8 | 1 | 是 | 8 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 8 | 0 | 0 | 0 | 0 |
| Processing a file may lead to a denial-of-service or potentially disclose memory contents | 8 | 7 | 3 | 是 | 0 | 0 | 0 | 8 | 0 | 0 | 0 | 8 | 8 | 0 | 0 | 0 | 0 |
| Processing a maliciously crafted file may lead to a denial of service | 8 | 8 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 8 | 0 | 0 | 0 | 0 |
| An attacker in a privileged network position may be able to perform denial-of-service attack using crafted Wi-Fi packets | 7 | 7 | 1 | 是 | 7 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 7 | 0 | 0 | 0 | 0 |
| An attacker may be able to cause a persistent denial-of-service | 7 | 7 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 7 | 0 | 0 | 0 | 0 |
| Visiting a website may lead to an app denial-of-service | 6 | 6 | 2 | 是 | 6 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 6 | 0 | 0 | 0 | 0 |
| An attacker in a privileged network position may be able to perform denial-of-service attack using crafted Bluetooth packets | 5 | 5 | 1 | 是 | 5 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 |
| An attacker in physical proximity may be able to cause a denial-of-service | 5 | 5 | 1 | 是 | 5 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 |
| Processing a malicious crafted file may lead to a denial-of-service | 5 | 5 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 5 | 0 | 0 | 0 | 0 |
| An app may be able to modify protected parts of the file system | 3 | 3 | 1 | 否 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| An attacker in a privileged position may be able to perform a denial-of-service | 3 | 3 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 3 | 0 | 0 | 0 | 0 |
| An unprivileged process may be able to terminate a root processes | 2 | 2 | 1 | 否 | 0 | 0 | 0 | 0 | 2 | 0 | 0 | 0 | 2 | 0 | 0 | 0 | 0 |
| A local attacker may be able to cause a denial of service | 1 | 1 | 1 | 是 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 1 | 0 | 0 | 0 | 0 |
| **合计** | 470 | 页列合计 396 | 名列合计 82 | 是 457／否 13 | 164 | 0 | 0 | 30 | 2 | 0 | 0 | 30 | 459 | 0 | 0 | 0 | 0 |

# 附表 C 三条含本体词的 Description 全文 × 修复手段（4 行）

**读法：** 本表只收**含本体词的 Description 段全文**，三条合计 **50 段**＝ Description 侧全部命中，与第 10.2 节一致；三条文本按 distinct 全文互斥切分、重复段 0（生成器断言）。"∩R／∩WO／∩E／∩M2／∩term"五列按**该行段集合**与外部集求交，行和与合计行一致（∩R **31＋9＋0 ＝ 40**、∩WO 0、∩E 0、∩M2 0、∩term **2**）。**注意这个 40 与附表 B 合计行的 ∩R ＝ 164 不是同一件事**：本表的 50 段是 Description 侧命中段，附表 B 的 164 是全部 470 段按 Impact 侧与 R 求交；两者都以 Impact 单字段定义的 R 集为外部集，但**行集不同**（第 12.2 节）。**页列合计 48 大于 3 行、名列合计 14 同理，不可当作去重值。** 三条文本的修复手段只有三种：input validation（33）、validation（13）、删除脆弱代码（4）。

| Description 全文 | 段 | 页 | 名 | ∩R | ∩WO | ∩E | ∩M2 | ∩term | 修复手段 |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---|
| A denial-of-service issue was addressed with improved input validation. | 33 | 31 | 8 | 31 | 0 | 0 | 0 | 2 | input validation |
| A denial-of-service issue was addressed with improved validation. | 13 | 13 | 4 | 9 | 0 | 0 | 0 | 0 | validation |
| A denial of service issue was addressed by removing the vulnerable code. | 4 | 4 | 2 | 0 | 0 | 0 | 0 | 0 | 删除脆弱代码 |
| **合计** | 50 | 页列合计 48 | 名列合计 14 | 40 | 0 | 0 | 0 | 2 | input validation 33／validation 13／删除脆弱代码 4 |

## 附表 C 续表：反向刀短语十三项

**读法：** 本续表按**短语**切（不按本体词切），"字段"列标明该短语在哪个字段测（Impact 或 Description）；页列与名列是**该短语全树命中段**所在的去重页数与组件名数，合计行不给出（逐行去重值不可相加）。三列关键数是 **全树命中段 ∶ Ds 内段 ∶ 非 Ds 段**，最后一列 ＝ Ds 内 ÷ 全树。合计行的 **4,862 ∶ 233 ∶ 4,629 ＝ 4.8%** 里，233 既是各行 Ds 内段的算术和、也**恰好等于去重后的段数**（这 13 把刀在 Ds 上互不重叠，生成器实测两者相同），因此 470 − 233 ＝ **237 段**不被这 13 个短语覆盖（第 7.2 节）；这 233 段分布在 **129 页／40 名**。**与主表不同，续表的短语是"修复手段侧"与"终止侧"判据，因此它与附表 A 的同名行在"并集"与"单字段"两种口径下可以相差 1 段**（第 7.6 节）。

| 反向刀短语 | 字段 | 全树命中段 | 页 | 名 | Ds 内段 | 非 Ds 段 | Ds 占该短语 |
|---|---|---:|---:|---:|---:|---:|---:|
| `unexpected system termination` | Impact | 802 | 129 | 53 | 0 | 802 | 0.0% |
| `unexpected app termination` | Impact | 612 | 148 | 56 | 0 | 612 | 0.0% |
| `terminate the process` | Impact | 24 | 24 | 3 | 0 | 24 | 0.0% |
| `crash* 词族` | Impact | 499 | 128 | 17 | 0 | 499 | 0.0% |
| `hang 词界` | Impact | 0 | 0 | 0 | 0 | 0 | 0.0% |
| `unresponsive 词族` | Impact | 0 | 0 | 0 | 0 | 0 | 0.0% |
| `improved input validation` | Description | 641 | 151 | 79 | 113 | 528 | 17.6% |
| `improved validation` | Description | 366 | 101 | 75 | 16 | 350 | 4.4% |
| `removing the vulnerable code` | Description | 173 | 97 | 43 | 15 | 158 | 8.7% |
| `improved checks` | Description | 1018 | 192 | 125 | 73 | 945 | 7.2% |
| `improved error handling` | Description | 18 | 18 | 4 | 8 | 10 | 44.4% |
| `improved restrictions` | Description | 47 | 37 | 12 | 0 | 47 | 0.0% |
| `improved state management` | Description | 662 | 176 | 111 | 8 | 654 | 1.2% |
| **合计** | 13 项 | 4862 | — | — | 233 | 4629 | 4.8% |

# 附表 D 页面、编号、致谢与版式五组分箱（34 行）

**读法：** 全表分五个块，每块最后一行是**该块的分箱合计并带校验值**（页 **155**、编号 **470**、致谢 **470**），生成器对这三处有 `die` 断言。**"Top10 页逐页"块的合计 99 段是 Top10 页承载的 Ds 段数（21.1%），不是这 10 页的全部段数**；每页的"该页全部段"列给出该页总段数与该页 Ds 占比。"编号归属"块给出 **104 个独占 ＋ 1 个共用 ＝ 105**（第 16.4 节）。"同页共存"块给出 **155 页共 8,679 段、Ds 占 5.4%、全 Ds 页 0**（第 16.1、16.3 节）。"版式域"块的域 B **3 段**对第 16.6 节；"字段落位"两块给出**致谢侧 0 段**与**同一段 Impact 内本体词不重复出现**（457 段各恰好命中 1 次，第 4 章）。

| 块 | 分箱 | 计数 | 说明 |
|---|---|---:|---|
| Ds 段所在页的段数 | 1 | 43 | 共 155 页含拒绝服务条目 |
| Ds 段所在页的段数 | 2 | 44 | 共 155 页含拒绝服务条目 |
| Ds 段所在页的段数 | 3 | 20 | 共 155 页含拒绝服务条目 |
| Ds 段所在页的段数 | 4 | 20 | 共 155 页含拒绝服务条目 |
| Ds 段所在页的段数 | 5 | 9 | 共 155 页含拒绝服务条目 |
| Ds 段所在页的段数 | 6-9 | 14 | 共 155 页含拒绝服务条目 |
| Ds 段所在页的段数 | 10-19 | 5 | 共 155 页含拒绝服务条目 |
| Ds 段所在页的段数 | 合计 | 155 | 校验=155 页、Ds 段 470 |
| Top10 页逐页 | 128071 | 13 | 该页全部段 151 段，Ds 占 8.6% |
| Top10 页逐页 | 128072 | 13 | 该页全部段 142 段，Ds 占 9.2% |
| Top10 页逐页 | 128067 | 12 | 该页全部段 185 段，Ds 占 6.5% |
| Top10 页逐页 | 127115 | 11 | 该页全部段 105 段，Ds 占 10.5% |
| Top10 页逐页 | 149035 | 11 | 该页全部段 314 段，Ds 占 3.5% |
| Top10 页逐页 | 127110 | 9 | 该页全部段 81 段，Ds 占 11.1% |
| Top10 页逐页 | 127118 | 8 | 该页全部段 48 段，Ds 占 16.7% |
| Top10 页逐页 | 148287 | 8 | 该页全部段 110 段，Ds 占 7.3% |
| Top10 页逐页 | 127111 | 7 | 该页全部段 53 段，Ds 占 13.2% |
| Top10 页逐页 | 127119 | 7 | 该页全部段 49 段，Ds 占 14.3% |
| Top10 页逐页 | 合计 | 99 | 占 Ds 段 21.1%（470 段／155 页） |
| Ds 段内 distinct CVE 数 | 1 | 451 | 470 段中 470 段含编号 |
| Ds 段内 distinct CVE 数 | 2 | 19 | 470 段中 470 段含编号 |
| Ds 段内 distinct CVE 数 | 合计 | 470 | 校验=470；全树 distinct 编号 2108 个，Ds 内 105 个 |
| 编号归属 | 仅出现在 Ds 段 | 104 | 这 104 个编号不出现在其余 8666 段 |
| 编号归属 | 也出现在非 Ds 段 | 1 | 与 #123 逻辑的 167 个独占编号互不冲突 |
| Ds 段致谢人数 | 1 | 289 | Ds 合计 729 人、均值 1.55 |
| Ds 段致谢人数 | 2 | 133 | Ds 合计 729 人、均值 1.55 |
| Ds 段致谢人数 | 3 | 18 | Ds 合计 729 人、均值 1.55 |
| Ds 段致谢人数 | 4 | 30 | Ds 合计 729 人、均值 1.55 |
| Ds 段致谢人数 | 合计 | 470 | 校验=470；全树 9132 段均值 1.80、最大 37 |
| 同页共存 | Ds 页中还有非 Ds 段的页 | 155 | 155 页如此，仅 0 页全为 Ds 段 |
| 同页共存 | 这 155 页全部段 | 8679 | Ds 段 470 占 5.4% |
| 版式域 | 域 B 内 Ds 段 | 3 | 域 B 共 91 段 |
| 字段落位 | 致谢字段含 denial of service | 0 | Impact 457／Description 50／致谢 0 |
| 字段落位 | 同一 Impact 句 denial of service 出现 2 次以上的段 | 0 | 457 段 Impact 每段命中次数分布 |

# 附表 E 中文手册层八组对应词（62 行）

**读法：** 分母是**窗口切分后的 178 页可用转储**（179 份文件排除 1 页无正文），"覆盖页占比"以 178 计。**"最集中页面"列给出该词最高频的那一页及其次数**，用于判断该词是否只是某一页面的专名；例如 `拒绝服务` 1 次／1 页来自《macOS 中的防火墙安全性》、`可用性` 7 次／6 页最集中在《钥匙串数据保护》2 次，均**不构成 iOS 弱点分类语境的术语使用**。**全零 21 词、非零 40 词**（合计行给出），其中挂起与无响应组 7 个词里 6 个全零（只有 `循环` 3 次／3 页，且来自《密码》页面的锁屏语境）。**"恢复"559 次／178 页与"内存"511 次／178 页是运维与功能语汇，不能与英文侧 `termination`／`memory` 的条目级用法互换引用**（第 18.4 节）。

| 组 | 中文词 | 出现次数 | 覆盖页 | 覆盖页占比 | 最集中页面 |
|---|---|---:|---:|---:|---|
| 拒绝服务本体 | 拒绝服务 | 1 | 1 | 0.6% | macOS 中的防火墙安全性（1 次） |
| 拒绝服务本体 | 服务拒绝 | 0 | 0 | 0.0% | — |
| 拒绝服务本体 | 可用性 | 7 | 6 | 3.4% | 钥匙串数据保护（2 次） |
| 拒绝服务本体 | 中断 | 8 | 6 | 3.4% | Mac 电脑的直接内存访问保护（2 次） |
| 拒绝服务本体 | 干扰 | 2 | 2 | 1.1% | 在 macOS 中安全扩展内核（1 次） |
| 拒绝服务本体 | 瘫痪 | 0 | 0 | 0.0% | — |
| 拒绝服务本体 | 停摆 | 0 | 0 | 0.0% | — |
| 终止与关闭 | 终止 | 13 | 7 | 3.9% | iOS 中的车钥匙安全性（4 次） |
| 终止与关闭 | 结束 | 7 | 6 | 3.4% | iPadOS 中的“共享 iPad”安全性（2 次） |
| 终止与关闭 | 退出 | 22 | 11 | 6.2% | iPadOS 中的“共享 iPad”安全性（7 次） |
| 终止与关闭 | 关闭 | 76 | 43 | 24.2% | 搭载 Apple 芯片的 Mac 的启动磁盘安全性策略控制（6 次） |
| 终止与关闭 | 注销 | 0 | 0 | 0.0% | — |
| 终止与关闭 | 卸载 | 3 | 3 | 1.7% | 凭证提供程序扩展（1 次） |
| 终止与关闭 | 强制 | 53 | 31 | 17.4% | 操作系统完整性（8 次） |
| 崩溃与异常 | 崩溃 | 6 | 4 | 2.2% | Apple 操作系统中的后台安全改进（3 次） |
| 崩溃与异常 | 异常 | 3 | 3 | 1.7% | Apple Cash 安全性（1 次） |
| 崩溃与异常 | 故障 | 6 | 4 | 2.2% | TLS 安全性（2 次） |
| 崩溃与异常 | 错误 | 27 | 16 | 9.0% | 操作系统完整性（4 次） |
| 崩溃与异常 | 失效 | 4 | 3 | 1.7% | 用于数据保护的密钥包（2 次） |
| 崩溃与异常 | 意外 | 4 | 4 | 2.2% | 钥匙串安全同步（1 次） |
| 崩溃与异常 | 非预期 | 1 | 1 | 0.6% | Mac 电脑的直接内存访问保护（1 次） |
| 挂起与无响应 | 挂起 | 0 | 0 | 0.0% | — |
| 挂起与无响应 | 无响应 | 0 | 0 | 0.0% | — |
| 挂起与无响应 | 卡住 | 0 | 0 | 0.0% | — |
| 挂起与无响应 | 冻结 | 0 | 0 | 0.0% | — |
| 挂起与无响应 | 停滞 | 0 | 0 | 0.0% | — |
| 挂起与无响应 | 循环 | 3 | 3 | 1.7% | 密码（1 次） |
| 挂起与无响应 | 死锁 | 0 | 0 | 0.0% | — |
| 重启与恢复 | 重启 | 41 | 20 | 11.2% | 在用户数据受到攻击时提供保护（9 次） |
| 重启与恢复 | 重新启动 | 0 | 0 | 0.0% | — |
| 重启与恢复 | 恢复 | 559 | 178 | 100.0% | 账户恢复联系人安全性（48 次） |
| 重启与恢复 | 复位 | 0 | 0 | 0.0% | — |
| 重启与恢复 | 重新引导 | 0 | 0 | 0.0% | — |
| 重启与恢复 | 开机 | 7 | 7 | 3.9% | 配对 recoveryOS 的访问限制（1 次） |
| 重启与恢复 | 唤醒 | 8 | 6 | 3.4% | 视控 ID、面容 ID、触控 ID 和密码（3 次） |
| 资源与配额 | 资源 | 27 | 15 | 8.4% | iOS、iPadOS、Apple tvOS、visionOS 和 watchOS 中的 App 代码签名过程（5 次） |
| 资源与配额 | 内存 | 511 | 178 | 100.0% | 安全隔区（49 次） |
| 资源与配额 | 配额 | 0 | 0 | 0.0% | — |
| 资源与配额 | 限额 | 6 | 1 | 0.6% | 屏幕时间安全性（6 次） |
| 资源与配额 | 限制 | 478 | 178 | 100.0% | LocalPolicy 签名密钥创建和管理（21 次） |
| 资源与配额 | 节流 | 0 | 0 | 0.0% | — |
| 资源与配额 | 过载 | 0 | 0 | 0.0% | — |
| 资源与配额 | 耗尽 | 1 | 1 | 0.6% | 访问钥匙类型（1 次） |
| 资源与配额 | 占用 | 0 | 0 | 0.0% | — |
| 检查与修复 | 验证 | 432 | 178 | 100.0% | Apple 钱包中的证件（19 次） |
| 检查与修复 | 校验 | 19 | 3 | 1.7% | Apple 钱包中证件的安全性（16 次） |
| 检查与修复 | 检查 | 66 | 30 | 16.9% | 管理式 Apple 账户安全性（13 次） |
| 检查与修复 | 输入验证 | 2 | 1 | 0.6% | Apple 账户安全性概览（2 次） |
| 检查与修复 | 处理 | 337 | 178 | 100.0% | 安全隔区（24 次） |
| 检查与修复 | 错误处理 | 0 | 0 | 0.0% | — |
| 检查与修复 | 修复 | 14 | 8 | 4.5% | 在 macOS 中防范恶意软件（5 次） |
| 检查与修复 | 补丁 | 4 | 1 | 0.6% | Apple 操作系统中的后台安全改进（4 次） |
| 类型与后果 | 提权 | 0 | 0 | 0.0% | — |
| 类型与后果 | 绕过 | 23 | 9 | 5.1% | 激活锁安全性（12 次） |
| 类型与后果 | 沙盒 | 27 | 11 | 6.2% | macOS 中的 Option ROM 安全性（6 次） |
| 类型与后果 | 沙箱 | 0 | 0 | 0.0% | — |
| 类型与后果 | 溢出 | 1 | 1 | 0.6% | 内存安全 iBoot 实施（1 次） |
| 类型与后果 | 泄露 | 30 | 11 | 6.2% | 密码监视（15 次） |
| 类型与后果 | 竞态 | 0 | 0 | 0.0% | — |
| 类型与后果 | 逻辑 | 4 | 2 | 1.1% | 硬件麦克风断联（3 次） |
| 类型与后果 | 越界 | 1 | 1 | 0.6% | 操作系统完整性（1 次） |
| **合计** | 61 词（全零 21 词、非零 40 词） | — | — | 178 页可用 | — |

# 附表 F 组件名层六十二名 × 五个外部集 × 该组件 Impact 全集（63 行）

**读法：** 列"该组件含 Impact 全集"＝ **该组件名下含 Impact 的段数**（第 15.1 节定义），"Ds 占比"＝ 该名的 Ds 段 ÷ 该名的 Impact 全集，**分母不是该组件全部段数、也不是该组件漏洞总数**。合计行 **470 段／Impact 全集 3,643 段／占比 12.9%**（3,643 段占全树含 Impact 的 7,434 段的 **49.0%**）。**"页列合计 442"是 62 行各自去重页数再求和，大于 155 页**，不可当并集值引用。**∩WO 与 ∩E 两列逐行为 0**（第 11、15.6 章）。占比 100% 的 11 个名字（第 15.3 节）分母都在 15 段以下，而 `Kernel` 0.9%、`WebKit` 3.8%、`Safari` 3.3% 的分母分别是 681／910／92 段——**比例与绝对数必须同报**。

| 组件名 | Ds 段 | 页 | ∩R | ∩WO | ∩E | 该组件含 Impact 全集 | Ds 占比 |
|---|---:|---:|---:|---:|---:|---:|---:|
| `ImageIO` | 62 | 52 | 0 | 0 | 0 | 261 | 23.8% |
| `WebKit` | 35 | 35 | 9 | 0 | 0 | 910 | 3.8% |
| `mDNSResponder` | 33 | 24 | 24 | 0 | 0 | 53 | 62.3% |
| `Security` | 23 | 23 | 23 | 0 | 0 | 91 | 25.3% |
| `AirPlay` | 18 | 11 | 18 | 0 | 0 | 77 | 23.4% |
| `CoreUtils` | 15 | 15 | 15 | 0 | 0 | 15 | 100.0% |
| `Wi-Fi` | 13 | 13 | 12 | 0 | 0 | 58 | 22.4% |
| `Audio` | 11 | 11 | 0 | 0 | 0 | 95 | 11.6% |
| `Foundation` | 11 | 11 | 0 | 0 | 0 | 67 | 16.4% |
| `SceneKit` | 11 | 11 | 0 | 0 | 0 | 112 | 9.8% |
| `Model I/O` | 10 | 10 | 0 | 0 | 0 | 88 | 11.4% |
| `Messages` | 9 | 9 | 1 | 0 | 0 | 50 | 18.0% |
| `CFNetwork` | 8 | 8 | 8 | 0 | 0 | 20 | 40.0% |
| `CoreGraphics` | 8 | 8 | 0 | 0 | 0 | 27 | 29.6% |
| `CoreMedia` | 8 | 8 | 0 | 0 | 0 | 150 | 5.3% |
| `Heimdal` | 8 | 8 | 0 | 0 | 0 | 19 | 42.1% |
| `LaunchServices` | 8 | 8 | 8 | 0 | 0 | 58 | 13.8% |
| `MobileStorageMounter` | 8 | 8 | 0 | 0 | 0 | 8 | 100.0% |
| `QuartzCore` | 8 | 8 | 0 | 0 | 0 | 12 | 66.7% |
| `libexpat` | 8 | 8 | 0 | 0 | 0 | 24 | 33.3% |
| `CoreText` | 7 | 7 | 0 | 0 | 0 | 33 | 21.2% |
| `Mail` | 7 | 7 | 0 | 0 | 0 | 36 | 19.4% |
| `Spotlight` | 7 | 7 | 0 | 0 | 0 | 69 | 10.1% |
| `UIFoundation` | 7 | 7 | 0 | 0 | 0 | 7 | 100.0% |
| `Kernel` | 6 | 6 | 3 | 0 | 0 | 681 | 0.9% |
| `libxpc` | 6 | 6 | 0 | 0 | 0 | 80 | 7.5% |
| `smbx` | 6 | 6 | 3 | 0 | 0 | 6 | 100.0% |
| `Accelerate` | 5 | 5 | 0 | 0 | 0 | 5 | 100.0% |
| `AirDrop` | 5 | 5 | 5 | 0 | 0 | 14 | 35.7% |
| `AppleJPEG` | 5 | 5 | 0 | 0 | 0 | 28 | 17.9% |
| `Bluetooth` | 5 | 5 | 5 | 0 | 0 | 41 | 12.2% |
| `Calling Framework` | 5 | 5 | 5 | 0 | 0 | 9 | 55.6% |
| `CoreUI` | 5 | 5 | 0 | 0 | 0 | 32 | 15.6% |
| `libarchive` | 5 | 5 | 0 | 0 | 0 | 30 | 16.7% |
| `Baseband` | 4 | 3 | 4 | 0 | 0 | 7 | 57.1% |
| `CUPS` | 4 | 3 | 4 | 0 | 0 | 43 | 9.3% |
| `file` | 4 | 4 | 0 | 0 | 0 | 7 | 57.1% |
| `libc` | 4 | 4 | 0 | 0 | 0 | 10 | 40.0% |
| `Admin Framework` | 3 | 3 | 0 | 0 | 0 | 12 | 25.0% |
| `CoreAnimation` | 3 | 3 | 3 | 0 | 0 | 5 | 60.0% |
| `Net-SNMP` | 3 | 3 | 0 | 0 | 0 | 3 | 100.0% |
| `Ruby` | 3 | 3 | 0 | 0 | 0 | 6 | 50.0% |
| `Safari` | 3 | 3 | 0 | 0 | 0 | 92 | 3.3% |
| `Screen Sharing Server` | 3 | 3 | 3 | 0 | 0 | 16 | 18.8% |
| `SecurityAgent` | 3 | 3 | 0 | 0 | 0 | 5 | 60.0% |
| `SoftwareUpdate` | 3 | 3 | 0 | 0 | 0 | 15 | 20.0% |
| `SpringBoard` | 3 | 3 | 0 | 0 | 0 | 3 | 100.0% |
| `WebDAV` | 3 | 3 | 0 | 0 | 0 | 20 | 15.0% |
| `WindowServer` | 3 | 3 | 0 | 0 | 0 | 32 | 9.4% |
| `apache` | 3 | 3 | 3 | 0 | 0 | 7 | 42.9% |
| `xar` | 3 | 3 | 0 | 0 | 0 | 3 | 100.0% |
| `AppleVA` | 2 | 2 | 0 | 0 | 0 | 7 | 28.6% |
| `Calendar` | 2 | 2 | 2 | 0 | 0 | 22 | 9.1% |
| `FaceTime` | 2 | 2 | 0 | 0 | 0 | 14 | 14.3% |
| `Libnotify` | 2 | 2 | 0 | 0 | 0 | 10 | 20.0% |
| `MetricKit` | 2 | 2 | 0 | 0 | 0 | 2 | 100.0% |
| `Power Management` | 2 | 2 | 0 | 0 | 0 | 7 | 28.6% |
| `Telephony` | 2 | 2 | 2 | 0 | 0 | 8 | 25.0% |
| `dyld` | 2 | 2 | 2 | 0 | 0 | 15 | 13.3% |
| `Cellular` | 1 | 1 | 1 | 0 | 0 | 1 | 100.0% |
| `ColorSync` | 1 | 1 | 0 | 0 | 0 | 4 | 25.0% |
| `lldb` | 1 | 1 | 1 | 0 | 0 | 1 | 100.0% |
| **合计** | 470 | 页列合计 442 | 164 | 0 | 0 | 3643 | 12.9% |

---

**规则：** 本篇全部计数按 #111 第 1 章两版式域切块口径执行，判据大小写不敏感（**仅 `DoS`／`DOS` 两条缩写判据大小写敏感**）、空格类字符（U+00A0／U+202F）与各类短横统一按 `$S`／`$D` 归一；词界优先，仅在明确标注"词族"时用子串匹配；字段范围每次写明是 **Impact 单字段**、**Description 单字段**、**三字段并集**（附表 A 的"Impact 段／页／名"三列为 Impact 单字段，"三字段并集段"与"∩Ds／∩R／∩WO／∩E"列为三字段口径；附表 B 全部列为按 distinct Impact 全文切分的段集合口径；附表 C 主表为 Description 侧 50 段口径、续表为逐短语所在字段口径；附表 F 全部为 Ds-wide 470 段口径）。**分母一律写明：9,136（全树段）／7,434（含 Impact 段）／8,679（Ds 所在 155 页全部段）／3,643（附表 F 62 名 Impact 全集）；类次 489 与段次 470 分列报出，不互换；`DOS` 类 Impact 口径 459 与并集口径 470 分列报出；附表 B、C、F 的页列与名列是逐行去重后的再求和，不得当作集合基数引用；凡"A 与 B 得数相同"处必须同时给出 |A|、|B|、|A∩B| 与差段清单（第 3.3、9.3 章）。**

**边界声明：** 本篇是**公开英文与中文文本的措辞计量**，不是脆弱性评估、不是攻击可行性研究，也不提供任何绕过方法、利用代码、样本、利用链构造或针对他人设备的操作步骤与时序。文中"拒绝服务"一律指**公开条目把影响自述为 denial of service（或在修复句里自述 denial-of-service issue）这一文本事实**，不推断该缺陷是否真的造成服务中断、其触发是否可远程、是否属于资源耗尽或进程终止、是否可利用；470 段、105 个编号、489 类次、62 个组件名、0 段在野标注等数字**只描述文本分布**，不构成任何组件可用性评级、缺陷优先级排序、可达性或攻击路径存在的结论。

Telegram: https://t.me/one00190
