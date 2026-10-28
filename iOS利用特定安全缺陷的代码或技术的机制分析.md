@@TITLE@@iOS 利用特定安全缺陷的代码或技术的机制分析
@@SUBTITLE@@选题里的"代码或技术"这两个名词，在 Apple 安全响应顾问页的英文措辞层里**一个都不存在**。全树 257 份镜像 / **9,136 段**里，`exploit` 词族只命中 **108 段 / 76 页 / 12 个块名**（占含 Impact 段 7,434 的 **1.45%**），而且这 108 段里 **103 段是同一个过去分词 `exploited`**、**5 段是专有名词短语里的 `exploit`**：**`exploitation` 0 段、`exploiter` 0 段、`an / the / this exploit` 这类名词位 0 段**。与"利用物"相关的 **28 个候选词里 27 个全字段 0 段**：`exploit code` **0**、`technique` **0**、`method` **0**、`proof of concept / PoC` **0**、`weaponized` **0**、`toolkit` **0**、`malware` **0**、`chain` **0**、`in the wild` **0**、`shellcode` **0**、`payload` **0**、`rootkit` **0**、`spyware` **0**、`surveillance` **0**、`Pegasus` **0**、`DarkSword` **0**、`kit` **0**、`framework` **0**、`flaw` **0**、`bug` **0**；唯一非零的 `tool` 只有 **1 段**，原文是 `unexpected tool termination`——那是 `otool` 页面里被崩溃的**工具进程**，不是"工具"这个概念。**同一片语料的另一侧却极厚**：`code` **756 段**、`arbitrary code` **336 段**、`code execution` **237 段**、`maliciously crafted` **1,769 段 / 218 页 / 81 名**。**T2 把"被利用了"和"利用会造成什么"写全了，唯独不写"用什么利用"。**
@@INFO@@报告日期|2026 年 10 月 28 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 7,434 段含 Description**。手段全集 E = **Impact 含 `exploit` 词族的 108 段**（域 A 98 段 / 域 B 10 段）。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含利用样本或恶意输入的构造方式、不含针对他人设备的操作步骤；本篇只清点**公开文本用哪些词形承认"被利用"、又用哪些 omission 回避"利用物"**，属于措辞与文本计量分析
@@INFO@@分类|`exploit` 词族的**形态收敛**（exploited 103 ∶ exploit 5 ∶ exploitation 0 ∶ exploiter 0）/ 字段**单边性**（Impact 108 ∶ Description 0 ∶ 致谢 0，且这 108 段 108 / 108 都有致谢栏）/ 限定语**闭合分区 5 + 59 + 10 + 8 + 26 = 108**、未归类 0 / **14 种句模板**纵向 108 / 手段名词 **28 项词电池 27 项全零** / 后果侧 336、237、1,769 段与手段侧 0 段的**规模反差** / 具名只有 Coruna **5 段 / 2 页**
@@INFO@@难度|进阶（第 4 章的字段单边性是全篇前提：把 `exploit` 当两栏通用词会凭空造出一条不存在的 Description 侧曲线；第 6 章的分区**判序敏感**，未固定 R1→R5 时 `actively` 与 `裸式` 会重复吸收同类段、合计虚增到 177；第 11 章与第 13 章的"交集为 0 / 交集为 25"必须用成员集合求交来断言，不能由段数相减推断）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

选题字面是"利用特定安全缺陷的代码或技术"。这句话在威胁建模里通常要求四件东西同时在场：**利用物**（一段代码、一个工具、一套方法）、**它针对的缺陷**（哪个漏洞）、**它成立的条件**（需要什么前置）、**它的效果**（做到之后怎样）。本篇逐词核对之后发现：Apple 公开安全响应顾问页（下称 T2）**只写了第四件，和一件半的"是否已被人用过"**，前三件在措辞层**没有对应的词**。

四组反差。

**反差一（这个词只有过去分词）**：`exploit` 词族（`exploit(?:s|ed|ing|ation|er|ers)?`）在 Impact 侧命中 **108 段 / 76 页 / 12 个块名**，占含 Impact 段（7,434）的 **1.45%**、占全树段的 **1.18%**。把这 108 段按词形拆开：**`exploited` 103 段、`exploit` 原形 5 段、`exploits` 0、`exploiting` 0、`exploitation` 0、`exploiter(s)` 0**。更关键的是**名词位为空**：`an / the / this / that / their / his / her + exploit` **0 段**，`exploit + the / this / a / an / vulnerability / flaw / issue / bug` **0 段**——也就是说 T2 里**没有任何一处把 exploit 当"某个利用程序"来指称**、也没有任何一处写"利用某个缺陷"这个动宾结构。那 5 处 `exploit` 原形全部出现在同一句型 `This fix associated with the Coruna exploit was shipped in …` 里，是**带专名的定语**，不是"代码 / 技术"的泛指。**结果：T2 承认"被利用"这个状态，却不提供"利用物"这个名词。**

**反差二（手段名词 28 项里 27 项全零）**：附表 D 列出 28 个与"利用特定缺陷的代码或技术"直接相关的候选词，**27 项在三个字段里一段都没有**：`exploit code`、`technique`、`method`、`proof of concept / PoC`、`weaponized`、`toolkit`、`malware`、`chain`、`in the wild`、`shellcode`、`payload`、`rootkit`、`spyware`、`surveillance`、`Pegasus`、`DarkSword`、`kit`、`framework`、`flaw`、`bug`、`exploitation`、`exploiter`、`has been exploited`（确定语气完成式）、`not been exploited`（否定式）、`reported to Apple` **各 0 段**。唯一的 1 段是 `tool`，原文 `Parsing a maliciously crafted binary may lead to unexpected tool termination`（块名 `otool`）——**这是"工具程序自己崩溃"，不是"攻击工具"**。**选题的两个名词"代码"与"技术"在英文措辞层的直接对应物都是 0。**

**反差三（后果侧比手段侧厚三个数量级）**：同一批语料里 `code` **756 段**（Impact 363 / Description 386 / 致谢 43，全字段 179 页）、`arbitrary code` **336 段 / 120 页**、`code execution` **237 段 / 106 页**、`arbitrary code execution` **218 段 / 100 页**、`execution` **240 段 / 106 页**、`execute` 词族 **121 段 / 70 页**、`maliciously crafted` **1,769 段 / 218 页 / 81 名**、`malicious` 词 **2,448 段**。**"造成什么"写了 1,769 段，"用什么造成"写了 0 段。**`arbitrary code` 与手段全集 E 的交集只有 **31 段**，`code`（Impact 侧 363 段）与 E 的交集 **36 段**——**后果句与在野标注句大体是两套段**（第 11 章给全表）。

**反差四（唯一具名的"利用物"只有一个，且只有 5 段）**：全树唯一被写出来的利用物专名是 **Coruna**，命中 **5 段 / 2 页 / 2 个块名**（页 `126632` 4 段、页 `126646` 1 段；块名 `Kernel` 1 段、`WebKit` 4 段）。而近年公开报道中同样著名的 `DarkSword`、`Pegasus`、`spyware`、`surveillance` **各 0 段**。与 Coruna 相邻但**交集为 0** 的另一套话术是 `supplementary fix` **8 段 / 8 页 / 1 名** 与 `blocked in iOS` **8 段 / 8 页 / 1 名**（这两组是同一批段，块名全部 `WebKit`）——**T2 的"利用物"词汇表总容量就是这 5 段专名 + 8 段"补充修复"话术。**

于是本篇的问题依次是：

**问题一（第 3–5 章）** 108 段的形态学与字段位置。答案：**只以 `exploited` 存在**（103 / 108 = 95.4%）；Impact 侧 108 ∶ Description 侧 **0** ∶ 致谢侧 **0**，同时这 108 段 **108 / 108 都有致谢栏内容**（CVE 编号就在致谢栏）。用法三分闭合：**在野标注句式 103 + 施动者 + `exploit` 及物动词 0 + 其余含 `exploit` 5 + 未归类 0 = 108**。

**问题二（第 6–8 章）** 这 108 段能不能按限定语闭合分区。能：**Coruna 具名 5 + 极复杂攻击且定向个人 59 + `actively` 且 Intel Mac 10 + `actively` 且其它限定 8 + 裸式标注 26 = 108**，未归类 **0**；但**判序敏感**（第 6 章给出未固定判序时的虚增合计 **177** 作反例）。句式收敛到 **14 种模板**（附表 A，纵向 108），最大一类 **24 句**。

**问题三（第 9–12 章）** 手段侧的空由谁填。答案：**没人填，但有两处间接痕迹**。其一是 `This fix associated with the Coruna exploit`（第 9 章），其二是 `This is a supplementary fix for an attack that was blocked in iOS 17.2`（第 10 章）——后者承认了"有一次被拦住的攻击"，仍不写工具，且这两套话术**逐段互斥**。第 11 章把动词式与名词式两套后果句式做集合对照：**`execute [arbitrary] code` 118 段 ∩ `arbitrary code execution` 237 段 = 0**；第 12 章证明 `arbitrary` 与 `code` **336 次命中全部直接相邻、中间词 distinct = 1（空串）**——**这个短语在 T2 里是一个不可拆的固定词组，不是一个可加限定的描述。**

**问题四（第 13–16 章）** 与 #113 的路径集、组件层与中文手册层怎么接。第 13 章：E ∩ P = **25**，且 `{exploited}` = `{this issue}` = `{Apple is aware of a report} = 103 段`（两两交集 **103**，即**同一组段**），108 = 103 + 5。**"在野利用"这三个检索名指向同一个集合，不是三份互相印证的独立证据。**第 15 章：108 段里 `buffer` **0**、`overflow` **0**、`type` **0**，`memory` 38、`kernel` 17、`privilege` 9、`sandbox` 8——**标注句本身从不指称缺陷类型，那 38 段来自同一 Impact 字段里的前一句后果描述**。第 16 章：中文手册层 `利用` **43 次 / 29 页**，而这 43 次落在 **43 个互不相同的上下文窗口**里——**中文层连"利用"这个术语都没有固定搭配**；`利用代码` / `漏洞利用` / `利用程序` / `利用工具` / `概念验证` / `武器化` / `在野` **各 0 次**。

# 1 操作性定义与分母

**定义一（手段全集 E）**：一条 **Impact** 文本里出现 `exploit` 词族（`(?<![\w-])exploit(?:s|ed|ing|ation|er|ers)?(?![\w-])`，不区分大小写），即算 E 的一段。**这是代理定义**：T2 没有"利用代码""利用工具""攻击技术"任何一个字段或标签，本篇用**唯一在 Impact 里成规模出现的、语义指向"利用"这个词根的形态**作为锚。E = **108 段 / 76 页 / 12 个块名**（域 A 98 段、域 B 10 段）。

**为什么不扩集**：把 E 扩成"手段词族"（加 `technique`、`method`、`tool`、`code`、`payload`、`malware`）在数字上是**空操作或污染**——六个词里 `technique` / `method` / `payload` / `malware` 是 **0 段**、`tool` 只贡献 1 段撞车文本、`code` 会把集合从 108 涨到 756 段但那不再是"利用的代码"而是"Impact 里提到 code 的一切"。**本篇把线画在 `exploit` 这个词根上**，第 11 章单独清点 `code` 侧并给出它与 E 的交集，不做合并。**这是本选题与"代码或技术"字面之间唯一的、也是必须声明的落差：词根之外的手段词在 T2 不存在，所以集合只能由词根定义。**

**定义二（句子）**：Impact 字段内部多句以 `" || "` 连接（#111 第 1 章的字段拼接规则），本篇**先拆句再统计**，切句规则 `(?<=[.!?])\s+(?=["A-Z])`。必须拆的原因：`exploited` 标注句与后果句**粘在同一个 Impact 字段里**（第 8 章"0 段只由标注句组成"这条结论完全依赖拆句），不拆句就会把"被利用了"错记成"某行为可能被害"。

**定义三（限定语分区 R1→R5）**：对 E 的段按**固定判序**、第一次命中即归类：R1 `coruna`；R2 `extremely sophisticated`；R3 `actively exploited on Intel`；R4 `actively exploited`（扣除 R3 已用）；R5 `may have been exploited`（裸式或仅带版本范围）。**判序必须先专名、再长限定、再短限定**，否则 R4 与 R5 会把 R2 的段重复吸收——第 6 章用同一脚本给出未固定判序时的错误合计 **177**，作为反例留档。

**分母声明**：沿用 #111 第 1 章的两版式域。全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 7,434 段含 Description**；域 A **9,045 段 / 246 页 / 470 名 / 7,346**；域 B **91 段 / 11 页 / 26 名 / 88**。E 的分域：**域 A 98 段 + 域 B 10 段 = 108**；域 B 那 10 段分布在 **9 页**（`118479`、`120304`、`120305`、`120307`、`120308`（2 段）、`120309`、`120310`、`120311`、`120339`），块名全部 `WebKit`。**只认 `<h3 class="gb-header">` 的解析器会让这 10 段消失，占 E 的 9.3%。**

**词形纪律**：所有计数带词边界；复数写进交替（`techniques?`、`chains?`）；词族写 `exploit(?:s|ed|ing|ation|er|ers)?` 而**不写 `exploit\w*`**（后者读不出形态分布，而形态分布正是第 3 章的结论）；`execute` 侧写 `execut\w*` 并在正文声明它同时吞 `execution`——这是**有意**的词族写法，只用于第 11 章的规模对照，不用于 E 的定义。

# 2 结论速览（每条都可按第 18 章复算）

1. E = **108 段 / 76 页 / 12 名**，占含 Impact 段 **1.45%**、占全树段 **1.18%**；**Description 侧 0、致谢侧 0**，同时 **108 / 108 段都有致谢栏内容**。
2. **形态只有两种**：`exploited` **103**、`exploit` 原形 **5**；`exploits` / `exploiting` / `exploitation` / `exploiter` **各 0**；名词位 `an|the|this + exploit` **0**；动宾 `exploit + the flaw / vulnerability` **0**。
3. **限定语闭合分区五类、未归类 0**：Coruna **5**（2 页 / 2 名）、极复杂 + 定向 **59**（42 页 / 8 名）、`actively`+Intel **10**（5 页 / 2 名）、`actively`+其它 **8**（8 页 / 1 名）、裸式 **26**（20 页 / 3 名），之和 **108**。
4. **句模板 14 种**（附表 A，纵向 **108**）；最大一种 **24 句**（`Apple is aware of a report that this issue may have been exploited.`），前三名 24 + 20 + 19 = **63 句 = 58.3%**。
5. **标注句从不单独成段**：拆句后"整段只有一句且那句含 `exploit`"的段 = **0**；每一段的标注句之前必有一句后果描述（第 8 章）。
6. **手段名词 28 项电池 27 项全零**（附表 D）：`exploit code` / `technique` / `method` / `PoC` / `weaponized` / `toolkit` / `malware` / `chain` / `in the wild` / `shellcode` / `payload` / `rootkit` / `spyware` / `surveillance` / `Pegasus` / `DarkSword` / `kit` / `framework` / `flaw` / `bug` / `exploitation` / `exploiter` / `has been exploited` / `not been exploited` / `reported to Apple` 等；唯一非零 `tool` **1 段**且是 `otool` 的工具进程语义。
7. **后果侧规模**：`code` **756**（Impact 363 / Desc 386 / 致谢 43）、`arbitrary code` **336 / 120 页**、`execution` **240 / 106 页**、`code execution` **237 / 106 页**、`arbitrary code execution` **218 / 100 页**、`execute` 词族 **121 / 70 页**、`maliciously crafted` **1,769 / 218 页 / 81 名**。
8. **两套后果句式互斥**：动词式 `execute [arbitrary] code` **118 段** ∩ 名词式 `arbitrary code execution` **237 段** = **0**；`execute` 之后窗口只有 **4 种**（`arbitrary` 118、`may` 7、`malicious` 3、`in` 3）。
9. **`arbitrary code` 是不可拆固定词组**：贪心枚举 `arbitrary` 与 `code` 之间的 0–3 个词，**336 次命中的中间词 distinct = 1（空串）**，没有任何一段写作 `arbitrary <某修饰> code`。
10. **`code` 的前置修饰只有 4 个词有值**：`arbitrary` **336**（∩P 42）、`kernel` **7**（∩P 7）、`malicious` **3**、`remote` **2**；电池里其余 **15 个候选修饰词全 0**（`native` / `JavaScript` / `WebKit` / `application` / `user-space` / `sandboxed` / `compiled` / `executable` / `bytecode` / `machine` / `object` / `p-code` / `privileged` / `untrusted` / `local`）。
11. **集合关系**：|E| 108 ∩ |P| 890（#113 的路径集）= **25**；`arbitrary code` ∩ P = **42**；`code`（全字段口径）∩ P = **76**；`malicious` ∩ P = **26**；`execute` 词族 ∩ P = **14**；`exploit` ∩ `arbitrary code` = **31**、∩ `code`（Impact）= **36**；**E ⊄ P 且 P ⊄ E**。
12. **三集合同一**：`{exploited}` = `{this issue}` = `{Apple is aware of a report}` = **103 段**，两两交集 **103**；`|E| = 103 + 5`，差额恰为 Coruna 那 5 段。
13. **具名只有 Coruna**：`Coruna` **5 段 / 2 页 / 2 名**；`supplementary fix` 与 `blocked in iOS` 各 **8 段 / 8 页 / 1 名**（同一批段，交 8 并 8）；**Coruna 那 5 段与这 8 段交集 0**。
14. **标注句不指称缺陷类型**：108 段内 `buffer` **0**、`overflow` **0**、`type` **0**、`WebKit`（词，非块名）**0**；`memory` 38、`kernel` 17、`privilege` 9、`sandbox` 8 均来自同段的**前一句**。
15. **中文手册层**：`利用` **43 次 / 29 页**，对应 **43 个互不相同的窗口**；`恶意利用` 5 / 3、`被利用` 1 / 1、`攻击代码` 1 / 1、`恶意代码` 3 / 3、`代码执行` 3 / 2、`点击` **1 / 1**、`缺陷` **1 / 1**；**中文零命中词 16 项**；`技术` 438 次里 **182 次"安全加密技术" + 178 次页脚"[技术支持]" = 360**（**82.2%**）。

# 3 E 的规模与词形分布

E = **108 段 / 76 页 / 12 个块名**。108 / 7,434 = **1.45%**（含 Impact 段口径）、108 / 9,136 = **1.18%**（全树段口径）、域 A 的 98 / 7,346 = **1.33%**。

**词形只有两个活的**：

| 词形 | Impact 段 | Description 段 | 致谢段 | 页 |
|---|---:|---:|---:|---:|
| `exploit` 词族合计 | **108** | **0** | **0** | 76 |
| `exploited` | **103** | 0 | 0 | 74 |
| `exploit`（原形） | **5** | 0 | 0 | 2 |
| `exploits` / `exploiting` | **0** | 0 | 0 | 0 |
| `exploitation` | **0** | 0 | 0 | 0 |
| `exploiter(s)` | **0** | 0 | 0 | 0 |

**这张表是本篇最重要的一张。**`exploit` 在英文里同时是名词和动词，而 T2 的用法**只保留了被动语态的过去分词**：**103 段里 `exploited` 前面永远是 `may have been`**，即"可能曾经被（某人）利用过"。**施动者被 by-短语省略掉了，工具被分词形态吸收掉了。**剩下 5 段原形全部长在 `the Coruna exploit` 这个专名短语里（第 9 章），**是定语而不是指称**。

**三个"0"各自的含义不同，必须分开引用**：`exploitation` 0 表示**没有把利用当作一个过程或能力来讨论的语域**；`exploiter` 0 表示**没有利用者的称谓**（称谓侧另见 #113：`attacker` **890 段**、`adversary` / `hacker` 各 **0**）；名词位 `an / the / this exploit` 0 表示**没有把一次利用具体化为一个可数对象的写法**。这三项合起来才构成"选题里的'代码或技术'在措辞层不存在"的完整证据——**单说 `technique` 0 段是不够的。**

# 4 字段单边性：Impact 108 ∶ Description 0 ∶ 致谢 0

`exploit` 词族在 **Description 侧 0 段**、在**致谢侧 0 段**；而 108 段里 **108 / 108 同时有 Description 栏、也有致谢栏内容**（致谢栏装的是那句标准免责声明加 CVE 编号，例如 `WebKit Bugzilla: 267134 ~ CVE-2024-23222`、`CVE-2024-23225`、`CVE-2024-23296`）。

三条推论：

**（1）"被利用"这件事在 T2 里只有一个位置。**它写在 Impact 的最后、跟在后果句之后（第 8 章：0 段只由标注句组成）。**Description 栏 0 段不是"漏写"**——Description 栏写的是"怎么修的"，与"被谁用什么利用"是两套叙述。这一点与 #113 第 3 章的 `attacker` **Impact 890 ∶ Description 0** 完全同构，也是本系列反复出现的**栏位分工**规律：`attacker`、`without`、`via`、在野标注、`arbitrary code`（**336 ∶ 0**）全在 Impact 侧；`by + 动名词`、`through` 全在 Description 侧。

**（2）任何"Apple 在修复说明里透露了利用方式"的读法都是把两栏当一栏。**Description 侧 `exploit` 0 段这条断言直接否掉这种读法；同侧 `technique` / `method` / `toolkit` 也各 0 段。

**（3）"CVE 出现在哪里"决定了编号能不能当手段证据。**CVE 编号（`CVE-\d{4}-\d{4,7}`）全树 **7,430 段**，其中**致谢侧 7,430、Impact 侧仅 19、Description 侧 0**。CVE 是**指派标识**，不携带任何"代码 / 技术"信息。**257 页每页都有 CVE，但每页的 `exploit` 段数是 0 到 4。**

# 5 用法三分区：103 + 0 + 5 + 未归类 0 = 108

对 E 的 108 段按"`exploit` 承担什么句法角色"做闭合分区，**规则序固定**：

| 类 | 判据 | 段数 |
|---|---|---:|
| U1 在野标注句式 | Impact 含 `Apple is aware of a report` 型标注 | **103** |
| U2 施动者 + `exploit` 作及物动词 | 出现 `an attacker may exploit …` 型主动结构 | **0** |
| U3 其余含 `exploit` 的段 | 专名定语型（Coruna） | **5** |
| 未归类 | — | **0** |
| 合计 | 103 + 0 + 5 + 0 | **108** |

**U2 为 0 是本选题的直接答案。**"利用特定安全缺陷的代码或技术"这句话如果真被写出来，它在英文里的自然形态就是 `code / an app / an attacker may exploit this vulnerability`——**T2 一段都没有**。T2 谈论缺陷与攻击者的关系时用的是 `may lead to` / `may be able to` / `may allow`（见 #113 第 6–7 章：`cause` 一个动词占 **392 句**），**从不使用 `exploit` 这个及物动词**。`exploit` 只出现在一个被动、模糊、带 `may have been` 的**事实通报**里。

**U1 与 U3 的差是 5 段 Coruna**（第 9 章）。**U1 + U3 = 108 = |E|**，所以 E 里**没有第三种用法**——没有"利用技术"的名词用法，没有"利用某个漏洞"的动宾用法，没有"已被利用"的确定语气（`has been exploited` **0 段**），也没有否定式（`not been exploited` **0 段**）。**最后这两项同样要紧：T2 既不确认也不否认任何一条，108 段全部使用 `may have been` 这个不置可否的形态。**

# 6 限定语的闭合分区（R1→R5，未归类 0）

把 108 段按**标注句里的限定成分**分类，判序 R1→R5、第一次命中即归类：

| 类 | 判据（按此序判） | 段 | 页 | 名 |
|---|---|---:|---:|---:|
| R1 | 含 `Coruna`（专名具名） | **5** | 2 | 2 |
| R2 | 含 `extremely sophisticated`（极复杂攻击 + 定向个人） | **59** | 42 | 8 |
| R3 | `actively exploited on Intel`（基于 Intel 的 Mac） | **10** | 5 | 2 |
| R4 | `actively exploited`（其它限定，主要是版本范围） | **8** | 8 | 1 |
| R5 | `may have been exploited`（裸式或仅带版本范围） | **26** | 20 | 3 |
| — | 未归类 | **0** | — | — |
| | **合计** | **108** | | |

**读法：**

- **R2 是主体（59 / 108 = 54.6%）**。它的固定形式是 `… exploited in an extremely sophisticated attack against specific targeted individuals`，其中 **`against specific targeted individuals`（定向个人）永远与 `extremely sophisticated` 同时出现，一段都不缺**。这是 T2 里唯一成规模的、对"手段水平"的**形容词性评价**——**注意它是评价，不是描述**：全树没有任何一句把这几个词落到具体的技术类别上（第 15 章：`buffer` / `overflow` / `type` 在 108 段内全 0）。
- **R3 + R4 = 18 段带 `actively`**，占 108 的 16.7%。R3 的 10 段全是 `on Intel-based Mac systems`（2 个块名），R4 的 8 段全是 `against versions of iOS before iOS 17.2`（1 个块名、8 页）。**`actively exploited` 是比裸式 `may have been exploited` 更强的一句断言，而它只出现在这两种限定之下**——这是一条值得单独记住的措辞规律：**Apple 用"主动利用"这个词的时候，必定同时给出平台或版本边界，从不裸用。**
- **R5 的 26 段是"裸式"**：只有 `Apple is aware of a report that this issue may have been exploited.`，什么限定都不给。其中 **24 段是纯裸式句子**，另 **2 段带 `against versions of iOS before iOS 16.7.1`**（`WebKit`，页 `120308`，正是域 B 那两页——第 14 章）。

**判序敏感必须同时报告。**若不固定判序、让每条规则独立统计，会得到 R1 5 + R2 59 + R3 10 + R4 **18** + R5 **85** = **177**，虚增 **69** 段——因为 R4 的 `actively exploited` 是 R3 的超集、R5 的 `may have been exploited` 又是 R1–R4 的共同谓语。**本篇的分区数字只有与"R1→R5 先专名后泛化"这句话绑在一起时才成立**（第 18 章第 5 步的脚本内已加 `未归类=0` 与合计 108 两条断言）。

# 7 十四种句式模板（附表 A）

把 108 段拆句、只取含 `exploit` 的那一句，规范化（小写、去标点、折空白）后去重，得 **14 种写法**，纵向合计 **108**（附表 A 全列）。分布是**陡降的**：**24 + 20 + 19 + 10 + 8 + 8 + 8 = 97 句占前 7 种（89.8%）**，剩下 7 种各 1–3 句。

三点必须点出来：

**（1）最大一类是"什么都不说"的那一类。**`apple is aware of a report that this issue may have been exploited` **24 句**是全部 14 种里限定最少的一种。**它在模板层的第一名，正是限定语层 R5 的 24**（26 段里有 24 段是纯裸式）。

**（2）版本尾缀产生了独立模板。**`… on versions of ios before ios 26` **19 句**、`… before ios 17 2`（含 `supplementary fix` 的长模板）**8 句**、`… on ios`（不带版本号）**8 句**、`… on versions of ios before ios 27` **3 句**、`… released before ios 18 4 1` **1 句**、无任何尾缀 **20 句**。也就是说 **59 段的 R2 里只有 20 句不带版本或平台尾缀**。**"极复杂攻击"这句评价，多数情况下附带一个边界。**

**（3）模板数是写法数，不是事件数。**同一句 `This fix associated with the Coruna exploit was shipped in iOS 17.2 on December 11th, 2023.` 出现在 2 段里，规范化后只算一条模板。**distinct = 14 不能读作"Apple 描述了 14 种利用方式"；它只说明 108 段由 14 个句子模板复制而来——措辞的收敛度本身就是"没有技术描述"的证据。**

# 8 标注句从不单独成段

把 108 段整段拆句后统计"整段只有一句、且那句含 `exploit`"：**0 段**。

**这是一条结构结论，比任何词频都硬。**它意味着 T2 的 Impact 字段**从不以"这条被利用了"作为唯一内容**；它必须先写一句后果（`Processing maliciously crafted web content may lead to arbitrary code execution.` / `An app may be able to execute arbitrary code with kernel privileges.` / `An attacker with arbitrary kernel read and write capability may be able to bypass kernel memory protections.`），**再**追加标注句。

于是每一条在野标注在文本上都是**"后果 + 状态"的两联结构**，而**"手段"这个应有的第三联缺失**。三件可核对的事：

- **两联的顺序固定**：后果在前、标注在后。回指词只有 `this issue`（**103 段 / 74 页**），`these issues`、`the issue`、`this improvement` 在 108 段内**各 0**。
- **标注句永远不带 by-短语**：`exploited by …` 的结构在 108 段里 **0**；这与 #113 第 9 章"`by + 动名词` Impact 侧只有 **2 段**（且只有 `spoofing` 一个动名词）"一致——**Impact 侧整体不使用行为手段介词。**
- **回指对象是 `issue` 而不是缺陷类型**：`this issue` 之后从不接名词化说明，第 15 章给这一项的 0 断言。

# 9 Coruna：唯一具名的"利用物"

`Coruna` 在 Impact 侧命中 **5 段 / 2 页 / 2 个块名**（页 `126632` 4 段、页 `126646` 1 段；`Kernel` 1 段、`WebKit` 4 段），Description 侧 0 段。**这是全树唯一被写出来的利用物专名。**

5 段原文（整段，含前一句后果）句式完全一致——**具名句只交代"这个修复对应哪个 exploit、它在哪个版本发过、现在补发给谁"，不交代它是什么**：

- `An app may be able to execute arbitrary code with kernel privileges. This fix associated with the Coruna exploit was shipped in iOS 17 on September 18, 2023. This update brings that fix to devices that cannot update to the latest iOS version.`（页 126632，Kernel）
- `Processing maliciously crafted web content may lead to arbitrary code execution. … shipped in iOS 17.3 on January 22, 2024. …`（页 126632，WebKit）
- `Processing maliciously crafted web content may lead to memory corruption. … shipped in iOS 16.6 on July 24, 2023. …`（页 126632，WebKit）
- `Processing maliciously crafted web content may lead to memory corruption. … shipped in iOS 17.2 on December 11th, 2023. …`（页 126632 与页 126646 各 1 段，WebKit）

四点判读：

**（1）具名 ≠ 描述。**5 段里没有任何一词说明 Coruna 是代码、工具、套件还是方法。**它作为专名出现，功能只是"把多个 CVE 归到同一次补发"。**这正好划出 T2 允许的"利用物"表达上限：**一个名字 + 三个日期**。

**（2）5 段全部落在"给不能升级的老设备补发修复"语境。**它们的原文都以 `This update brings that fix to devices that cannot update to the latest iOS version.` 收尾，页面是**老版本补发条目**，因此需要解释"为什么现在才修"——**具名是版本管理层的需求，不是威胁管理层的需求**。并且这 5 段与第 10 章那 8 段 `supplementary fix` **交集为 0**（成员求交实测），**是两套互不重叠的"延迟修复"话术：一套点名、一套不点名。**

**（3）`exploit` 原形的 5 段 = Coruna 的 5 段。**这不是巧合：T2 里 `exploit` 这个名词**只有在与专名绑定时才出现**。**一旦要泛指，它就退回到分词 `exploited` 或者干脆消失。**这是第 3 章形态表与本章数字的接缝。

**（4）另一个常被报道的专名 `DarkSword` 在镜像里 0 段**，`Pegasus`、`spyware`、`surveillance` 同样 **0**。**引用"T2 点名了利用套件"时，能且只能点这一个名字、这 5 段。**

# 10 `supplementary fix`：承认"有一次被拦住的攻击"，仍不写工具

与 Coruna 相邻但**逐段互斥**的一类，是 `This is a supplementary fix for an attack that was blocked in iOS 17.2` 这个句型：`supplementary fix` **8 段 / 8 页 / 1 个块名**，`blocked in iOS` **8 段 / 8 页 / 1 个块名**——**两组是同一批 8 段（交集 8、并集 8）**，块名全部 `WebKit`，页 `122281`、`122283`、`122284`、`122285`、`122345`、`122346`、`122372`、`122376`。这 8 段同时含 `extremely sophisticated`，因此限定语分区里落入 R2、模板层是第 7 章那 **8 句**带 `ios 17 2` 的长模板。

**这三句话在措辞上做了三件事，也只做这三件**：**（a）** 承认攻击发生过；**（b）** 承认它**被拦住了**（`was blocked`，不是"被分析""被溯源"）；**（c）** 说明本次是**补充**修复。**没有一句写攻击者用什么。**`attack` 在这里是被拦的对象，不是被描述的对象。

`actively exploited against versions of iOS before iOS 17.2` 那 **8 段 / 8 页 / 1 名**（第 6 章 R4）与这 8 段**不是同一批**（实测交集 **0**）：R4 的 8 段说"曾被主动利用"，本组的 8 段说"曾被拦截"。**同一条版本线 `before iOS 17.2` 上，T2 同时存在"被主动利用"和"被拦住"两种表述，两者合计 16 段、全部在 `WebKit`。**这是本篇能给出的、关于"某个版本区间的攻击活动"的最强措辞证据，也**仅限于措辞**。

# 11 手段侧 0 与后果侧厚的规模对照

把"手段候选词"与"后果候选词"并排，才能看出不对称的幅度：

| 侧 | 词 / 词组 | Impact 段 | 页 | 与 E 的交 | 与 P 的交 |
|---|---|---:|---:|---:|---:|
| 手段 | `exploit code` | **0** | 0 | 0 | 0 |
| 手段 | `technique(s)` | **0** | 0 | 0 | 0 |
| 手段 | `method(s)` | **0** | 0 | 0 | 0 |
| 手段 | `PoC` / `proof of concept` | **0** | 0 | 0 | 0 |
| 手段 | `toolkit` / `malware` / `chain` | **0** | 0 | 0 | 0 |
| 手段 | `tool` | **1** | 1 | 0 | 0 |
| 在野 | `exploit` 词族 E | **108** | 76 | 108 | **25** |
| 后果 | `arbitrary code` | **336** | 120 | **31** | **42** |
| 后果 | `code`（Impact 363 / 全字段 756） | **363** | 179\* | **36** | **76**\* |
| 后果 | `execution` | **240** | 106 | — | — |
| 后果 | `code execution` | **237** | 106 | — | — |
| 后果 | `arbitrary code execution` | **218** | 100 | — | **28**（#113） |
| 后果 | `execute` 词族 | **121** | 70 | — | **14** |
| 触发 | `maliciously crafted` | **1,769** | 218 | — | — |

\* `code` 的 **756** 段是**全字段口径**（Impact 363 / Description 386 / 致谢 43），"179 页"与"∩P **76**"同样是**全字段口径**；本表的 ∩E 两列（31、36）与 `arbitrary code` 的 ∩P 42 是 **Impact 侧口径**。**引用 756、363、76 必须带口径，不得与 336、237 这类 Impact-only 数相加。**

**结论（不对称的幅度）**：`maliciously crafted` **1,769 段** ∶ `exploit` 词族 **108 段** ∶ `exploit code` / `technique` **0 段**。**T2 描述恶意输入的形态极其细致（81 个块名、218 页都用 `maliciously crafted`），描述"利用行为"只有一句被动标注，描述"利用物"是零。**

**动词式与名词式互斥（这条必须用成员求交断言）**：`execute [arbitrary] code` 型 **118 段** 与 `arbitrary code execution` 型 **237 段** 的**交集为 0**——同一段不会既写"能执行任意代码"又写"导致任意代码执行"。`execute` 之后能出现的词**只有 4 种**：`arbitrary` **118**、`may` **7**、`malicious` **3**、`in` **3**。**"执行"与"代码"之间没有第三种修饰，因此"以某种技术执行代码"这类表述在句式上没有位置。**

# 12 `arbitrary code` 是不可拆的固定词组

对 `arbitrary` 与 `code` 之间允许 0–3 个词的**贪心**枚举（`arbitrary\s+((?:[\w-]+\s+){0,3}?)code`），**336 次命中的中间串 distinct = 1，且那唯一一项是空串**——**没有任何一段写作 `arbitrary <某修饰> code`**（`arbitrary malicious code`、`arbitrary native code` 这类都不存在）。

必须补一句方法学：**用惰性等价的 `{0,3}?` 会得到同样的输出，但那是假阳性**——惰性量词总优先匹配空串，任何情况下都会报告"中间 0 词"。**本条结论只有配合贪心写法才成立**（第 18 章第 6 步）。这是本系列里第三次在同一条正则上翻车后留下的记录。

配套的前置修饰电池（`code` 左边的词，词边界齐全）：

| 修饰词 | Impact 段 | 页 | ∩ P（890） |
|---|---:|---:|---:|
| `arbitrary` | **336** | 120 | **42** |
| `kernel` | **7** | 7 | **7** |
| `malicious` | **3** | 3 | 0 |
| `remote` | **2** | 2 | 0 |
| 其余 15 项（`native` / `JavaScript` / `WebKit` / `application` / `user-space` / `sandboxed` / `compiled` / `executable` / `bytecode` / `machine` / `object` / `p-code` / `privileged` / `untrusted` / `local`） | **各 0** | 0 | 0 |

**`kernel code` 那 7 段的 ∩P = 7（7 段全部也在 P 里）是这张表唯一有信息量的非平凡行**：它对应 #113 第 4 章 Q3"已具备能力的攻击者"那批段的下位表达（`with arbitrary kernel read and write capability`），**是"代码"与"攻击者身份"在措辞层唯一真正重叠的地方**。**其余"代码"段与攻击者身份无关。**

# 13 与 #113 路径集的集合关系

| 集合 | 段数 | ∩ E（108） | ∩ P（890） |
|---|---:|---:|---:|
| E（`exploit` 词族，Impact） | **108** | — | **25** |
| P（`attacker`，Impact，#113） | **890** | **25** | — |
| `arbitrary code` | 336 | 31 | **42** |
| `code`（Impact 侧） | 363 | 36 | 76\* |
| `malicious` | 2,448 | — | **26** |
| `execute` 词族 | 121 | — | **14** |

\* 该行的 ∩P 是全字段口径（见第 11 章脚注）。

四件事：

**（1）E ⊄ P 且 P ⊄ E。**交集 **25**，即 **108 段里只有 23.1% 同时写了攻击者**（**83 段根本不提攻击者**），而 890 段里 **865 段不带在野标注**。**"被利用"与"攻击者能做某事"是两段几乎不相交的文本。**并集 **973** = 108 + 890 − 25。

**（2）那 25 段就是 #113 第 14 章的 25 段。**#113 测得"在野标注 ∩ P = 25，且 25 / 25 全落 Q3（已具备 kernel / 读写能力）"，起点分解 **16 + 5 + 4 = 25**。本篇独立重算 `exploit ∩ attacker = 25`——**两个数是同一组段**（因为 #113 的"在野标注"就是本篇 U1 那 103 段，见本章第（4）点）。**跨期引用这 25 段时可以合并叙述，但必须声明它是同一次测量的两个名字，不是两条独立证据。**

**（3）`exploit` ∩ `arbitrary code` = 31、∩ `code` = 36。**也就是说 **108 段里约三成（31 段）在同一段里也写了"执行任意代码"**，但**没有任何一段把二者连成句法关系**——第 8 章的"标注句永远不带 by-短语、不回指缺陷类型"就是这条缺失句法关系的直接证明。**共现 31 ≠ 因果 31**（这正是 #113 误读六、#112 误读八的同一课）。

**（4）三个集合是同一个集合**：`{Impact 含 this issue}` = **103**、`{Apple is aware of a report}` = **103**、`{exploited}` = **103**；两两交集 **103**。**它们不是三份互相印证的材料，是同一条模板句的三个检索名。**因此"Apple 确认在野利用"的**独立条目数上限是 103 段**，任何把这三个数相加或互为佐证的写法都是重复计数。**E = 103 + 5（Coruna）** 是这两个集合之间唯一的算术关系。

# 14 组件与页面分布：12 个块名、76 页

108 段落在 **12 个块名**上（附表 B，纵向 108）：`WebKit` **41**、`Kernel` **9**、`Messages` **9**、`CoreMedia` **8**、`RTKit` **8**、`ImageIO` **7**、`CoreAudio` **5**、`JavaScriptCore` **5**、`dyld` **5**、`Accessibility` **4**、`RPAC` **4**、`CoreGraphics` **3**。

`WebKit` 一个块名占 **41 / 108 = 38.0%**；加上 `JavaScriptCore` 的 5 段，**Web / JS 引擎族占 46 / 108 = 42.6%**。**内核族（`Kernel` + `RTKit`）17 段 = 15.7%**。

页面侧（附表 C，76 行、纵向 108）：**一页最多 4 段**（`126632`，即 Coruna 那页）、**3 段的 2 页**（`122345`、`122346`）、**2 段的 25 页**、**1 段的 48 页**；4 + 2×3 + 25×2 + 48×1 = **108** ✓、页数 1 + 2 + 25 + 48 = **76** ✓。`125884`–`125892`、`121752`–`121756`、`122372`–`122402` 这几组连号页各 2 段，是**同一条标注被复制到相邻版本页**的结果——**按段计数不等于按缺陷计数**（第 19 章缺口清单第 4 条）。

**分域**：域 A **98 段** + 域 B **10 段** = **108**。域 B 的 10 段分布在 **9 页**、块名全为 `WebKit`。**如果解析器只认 `<h3 class="gb-header">`，`120308` 那 2 段（正是 R5 里唯一带 `against versions of iOS before iOS 16.7.1` 的两段）会一起消失，R5 从 26 变 24、E 从 108 变 98。**这是本篇唯一一处版式域直接改变分区结论的地方。

# 15 标注句不指称缺陷类型

对 E 的 108 段（整段文本）检查缺陷类别名词：

`buffer` **0**、`overflow` **0**、`type` **0**、`WebKit`（作为词，非块名）**0**；`memory` **38**、`kernel` **17**、`privilege` **9**、`sandbox` **8**。

**两组必须分开读。**`memory` / `kernel` / `privilege` / `sandbox` 的命中**全部来自同段的前一句后果描述**（`may lead to memory corruption`、`execute arbitrary code with kernel privileges`、`bypass kernel memory protections`），**而不是标注句**——标注句的完整形式只有第 7 章附表 A 那 14 种，其中没有一种含类别词。**`buffer` / `overflow` / `type` 三个词在整个 108 段里一次都没出现。**

于是本选题的第二个名词"特定安全缺陷"也在措辞层落空：**T2 的在野标注不与缺陷类型同句**。标注句用 `this issue` 回指（103 段、唯一回指词），**把缺陷类型的负担完全交给前一句的后果描述**——而后果描述写的是**效果**（内存破坏、内核权限、沙盒绕过），不是**缺陷类别**。这与 #112 的"内存破坏类词与后果词同段、但与缺陷分类不同段"一致。

**引用时禁止的写法**：把 38 段 `memory` 说成"38 起在野利用是内存破坏漏洞"。正确的写法是：**108 段在野标注里，有 38 段的同段后果句提到 memory；类别判定不在公开文本内。**

# 16 中文手册层（T1）的对应词

《Apple 平台安全保护》zh-CN 转储 **178 页可用**（179 份，排除 `sec2727afbbc`）。

| 中文词 | 次数 | 页 |
|---|---:|---:|
| 技术 | **438** | 178 |
| 攻击 | **344** | 178 |
| 恶意软件 | **249** | 178 |
| **利用** | **43** | **29** |
| 沙盒 | 27 | 11 |
| 漏洞 | 20 | 12 |
| 威胁 | 13 | 10 |
| 病毒 | 8 | 3 |
| 恶意利用 | **5** | 3 |
| 恶意代码 | **3** | 3 |
| 代码执行 | **3** | 2 |
| 驻留 | 3 | 2 |
| 被利用 | **1** | 1 |
| 攻击代码 | **1** | 1 |
| 点击 | **1** | 1 |
| 缺陷 | **1** | 1 |
| 对手 | **0** | 0 |

**十六个"0 次"是本篇中文层的骨架**：`利用代码` **0**、`漏洞利用` **0**、`利用程序` **0**、`利用工具` **0**、`利用套件` **0**、`攻击技术` **0**、`概念验证` **0**、`武器化` **0**、`在野` **0**、`零点击` **0**、`持久化` **0**、`恶意程序` **0**、`蠕虫` **0**、`后门` **0**、`木马` **0**、`对手` **0**。（`漏洞利用` 一项须与 `利用` 的 43 次区分：前者作为**固定四字术语**出现 0 次。）

**最重要的单一数字：`利用` 43 次对应 43 个互不相同的上下文窗口**（前 6 字 + 后 8 字）。例如 `堆利用`、`恶意利用`、`被攻击者利用`、`利用安全隔区监控设备`、`利用 CPU 信号`、`利用该响应来破坏设备`、`增加其利用"信息"和…`、`充分利用平台提供的可用安…`。**"利用"在中文手册里不是一个术语，而是一个随处的动词**，它同时承载"善用某项功能"（`连续互通充分利用了…`、`利用强密码提高安全性`）与"被攻击者滥用"（`进而被攻击者利用`、`成为被攻击者利用来持续感染操作系…`）两种相反语义。**任何"T1 用'利用'对应 exploit"的读法都会把这两类混成一类。**

**同一混淆发生在 `技术` 上**：438 次里 **182 次是"安全加密技术"、178 次是页脚"[技术支持]"**，两者合计 **360 / 438 = 82.2%**；剩下 **78 次**落在另外 **78 个互不相同的窗口**里（去重窗口共 **80** 个）。**"技术"这个词在中文层的绝对多数用法与攻击技术无关**——这与英文层 `technique` **0 段**是同一个事实的两面：**两本公开文本都不提供"攻击技术"这个词汇位置。**

**唯一一次"点击"**出现在《"信息"和 IDS 的 BlastDoor》：`它旨在大幅提高用户防范攻击的能力，特别是 0 点击攻击（无需用户交互的攻击）。`**这是 178 页里"点击"唯一一次出现，且它出现在防护描述中、不伴随任何攻击描述；作为术语的"零点击"写法 0 次。**另有 **3 页**写"无需…交互"，语境分别是 **iCloud 服务密钥的可用性**、**密码泄露通知的本地匹配**、**术语表里"设备管理服务"的定义**——**都不是攻击语境**。这两点将在 #119（无需目标主动点击）中作为核心分母重述，本篇只登记其存在。

`恶意代码` 3 次的三处原文分别是《运行时进程安全性》的 ASLR 段（`防止对内存损坏错误的利用`）、《系统完整性保护》的 `帮助防止恶意代码修改此类组件`、《Mac 电脑中的 Option ROM 安全性》的 `他们也能够执行恶意代码`。**三处都是防护陈述，没有一处描述任何具体恶意代码。**

# 17 验证与断言

**验证一（E 的规模与形态）**：E = **108 段 / 76 页 / 12 名**；`exploited` **103** + `exploit` 原形 **5** = **108**；`exploitation` / `exploiter(s)` / `exploits` / `exploiting` **各 0**；名词位（`an|the|this|that|their|his|her + exploit`）**0**；动宾（`exploit + the|this|a|an|vulnerabilit|flaw|issue|bug`）**0**。**四项必须同时为 0**，缺一项即把形态分布说满。

**验证二（字段单边性）**：Impact **108** ∶ Description **0** ∶ 致谢 **0**；E 内 **108 / 108 段有致谢栏内容**；`CVE-\d{4}-\d{4,7}` 全树 **7,430 段**（致谢 7,430 / Impact **19** / Description **0**）。**Impact 侧那 19 段不构成手段证据，不得引用。**

**验证三（用法三分闭合）**：**103 + 0 + 5 = 108**，未归类 **0**；其中 U2（施动者 + `exploit` 及物动词）**0** 是本选题的直接答案。

**验证四（限定语分区闭合）**：**5 + 59 + 10 + 8 + 26 = 108**，未归类 **0**；页 **2 / 42 / 5 / 8 / 20**（页有重叠，**不得相加**），名 **2 / 8 / 2 / 1 / 3**。反例留档：**不固定判序得 5 + 59 + 10 + 18 + 85 = 177**，虚增 **69**。

**验证五（模板层与限定层的接缝）**：附表 A 纵向 **108**、distinct **14**；裸式模板 **24 句** = R5 的 26 段减去 **2 段带 `against versions of iOS before iOS 16.7.1`**；`actively` 类模板 **10 + 8 = 18 句** = R3 + R4；`extremely sophisticated` 类模板 **20 + 19 + 8 + 8 + 3 + 1 = 59** = R2；Coruna 模板 **2 + 1 + 1 + 1 = 5** = R1。**两表用同一判序，逐类相等。**

**验证六（标注句不独立成段）**：拆句后"整段仅一句且含 `exploit`"的段 = **0**；回指词只有 `this issue`（**103 段 / 74 页**），`these issues` / `the issue` / `this improvement` **各 0**；`exploited by` 结构 **0**。

**验证七（具名唯一性与两套延迟修复话术互斥）**：`Coruna` **5 段 / 2 页（126632=4、126646=1）/ 2 名（Kernel 1、WebKit 4）**；`DarkSword` / `Pegasus` / `spyware` / `surveillance` / `kit` / `framework` / `payload` / `shellcode` / `rootkit` **各 0**；`tool` **1 段**（块名 `otool`，原文 `unexpected tool termination`）；`supplementary fix` **8 段 / 8 页 / 1 名** 与 `blocked in iOS` **8 段 / 8 页 / 1 名** **为同一批**（交 8、并 8；页 `122281`、`122283`、`122284`、`122285`、`122345`、`122346`、`122372`、`122376`，块名全 `WebKit`）；**Coruna 5 段 ∩ 这 8 段 = 0**；R4 的 8 段 ∩ 这 8 段 = **0**。

**验证八（零交必须用成员求交）**：`execute [arbitrary] code` **118** ∩ `arbitrary code execution` **237** = **0**；`arbitrary` 与 `code` 之间中间词 distinct = **1（空串）**、命中 **336**；`exploit` ∩ `arbitrary code` = **31**、∩ `code`（Impact）= **36**。**不得由 336 − 31 之类减法推断任何并集或差集。**

**验证九（跨期集合的同一性）**：`{this issue}` = `{Apple is aware of a report}` = `{exploited}` = **103**，两两交集 **103**；E ∩ P = **25** = #113 第 14 章的 25（其起点分解 **16 + 5 + 4**）；并集 **973** = 108 + 890 − 25；**25 ≠ 30**（#113 拆句得到 30 个不含 `attacker` 的标注句，因为一句可拆成两句）。**三个 103 是同一个集合的三个检索名，相加即重复计数。**

**验证十（类别词缺失）**：108 段内 `buffer` **0**、`overflow` **0**、`type` **0**、`WebKit` 词 **0**；`memory` **38**、`kernel` **17**、`privilege` **9**、`sandbox` **8** 全部来自前一句后果描述。**不得把 38 读作"38 起内存破坏类在野漏洞"。**

**验证十一（域分与 T1 层）**：域 A **98** + 域 B **10** = **108**，域 B 的 10 段分布在 **9 页**且全为 `WebKit`；T1 **178 页可用**，`利用` **43 次 / 29 页 / 43 个互不相同窗口**，`技术` **438 次**（`安全加密技术` 182 + `[技术支持]` 178 = **360**，占 **82.2%**；余 **78 次落在 78 个各出现 1 次的窗口**，去重窗口共 **80**），`点击` **1 次 / 1 页**，**中文零命中词 16 项**：`利用代码`、`漏洞利用`、`利用程序`、`利用工具`、`利用套件`、`攻击技术`、`概念验证`、`武器化`、`在野`、`零点击`、`持久化`、`恶意程序`、`蠕虫`、`后门`、`木马`、`对手`。**这 16 项要与"非零但被稀释"的 `利用`（43）、`技术`（438）分开引用。**

# 18 复算步骤

1. 镜像目录 `_tmp/adv2/<id>.html`，257 份（排除 `122085`）。逐页先测 `<h3 class="gb-header` 是否存在；存在走域 A 刀、不存在走域 B 刀（粗体段起块）。块体终止锚必须是**四个**（`<h3 class="gb-header`、`<div id="disclaimer"`、`<footer`、文末），少一个会把页脚免责声明并入字段文本。
2. 段取 `<p class="(?:gb-paragraph|paragraph)[^"]*"[^>]*>(.*?)</p>`，剥标签、解实体（`&rsquo;` 类必须落到 `chr($1)`）、折空白，丢 `^Available for` 行；`Impact:` / `Description:` 多句以 `" || "` 连接，其余以 `" ~ "` 连接。**E 只从 Impact 构造。**
3. **必须拆句**：`split /(?<=[\.!?])\s+(?=["A-Z])/`。不拆句则第 8 章的"0 段"与第 7 章的 14 种模板全部失真（后果句会被并入标注句）。
4. 词计数一律带词边界 `(?<![\w-])kw(?![\w-])`；复数写进交替（`techniques?`、`chains?`、`tools?`）；词族写 `exploit(?:s|ed|ing|ation|er|ers)?`，**不写 `exploit\w*`**；`execut\w*` 只在第 11 章对照表里用，且必须声明它同时吞 `execution`。
5. 限定语分区**判序固定 R1→R5**、第一次命中即归类，脚本内必须有 `$rest == 0` 与 `$tot == 108` 两条 `die`；同时输出未固定判序的 **177** 作反例。
6. **`arbitrary` 与 `code` 之间的夹词枚举必须用贪心 `{0,3}`**（`arbitrary\s+((?:[\w-]+\s+){0,3}?)code`）。用 `{0,3}?` 会永远返回空串、把"从不夹词"变成同义反复。
7. 零交与非平凡交集（118 ∩ 237 = 0、108 ∩ 890 = 25、三个 103 两两 = 103、Coruna 5 ∩ supp 8 = 0）**一律以成员集合求交并同时报 |A|、|B|、|A ∩ B|**，不得由段数相减推断。
8. 跨期对照先声明域：`bypass` 全树 **291** / 域 A **288**（#110）、输入并集 **3,245** / 域 A **3,210**（#112）、P **890** / 域 A **885** / 域 B **5**（#113）、E **108** / 域 A **98** / 域 B **10**（本篇）。
9. `code` 一类词必须写明口径（Impact-only **363** ∶ 全字段 **756**）；致谢栏与 Description 栏的命中不得混入 Impact 侧曲线。
10. T1 侧：逐页取最后一个 `^版权和商标$` 之后至首个含 `Apple Footer` 行之前，排除 `sec2727afbbc`，得 **178 页可用**；中文词按**子串**计数，正文须说明"利用 / 恶意利用 / 被利用"存在包含关系、**不可相加**；页面标题从 `_tmp/titles.txt` 的 `sec<id>|<标题>` 行取。
11. 所有含中文或复杂转义的正则**写成 UTF-8 脚本文件**（`use utf8` + `binmode ":utf8"`），不要塞进命令行——Windows 代码页会静默破坏匹配。子程序名**不要用 `stat`**（与 `CORE::stat` 冲突，会把数组当文件名、静默返回 undef）。
12. 附表 A / B / C / D 由 `_tmp/tl114tab.pl` 直接生成，脚本内含断言：`$ta != 108`、`scalar(@ord) != 14`、`$nb != 108`、`scalar(%NM) != 12`、`$np != 108`、`$bA != 98 || $bB != 10`、`@bat != 28` 任一不成立即 `die`、不产出文件；补测脚本 `_tmp/tl114g.pl`（判序分区 + 集合关系）、`_tmp/tl114h.pl`（supp / blocked / Coruna 三者互斥）同样带输出断言。

# 19 缺口清单

1. **"代码或技术"这个层级在 T2 不存在。** E 的 108 段是**代理集合**：它回答"哪些条目声明了被利用"，不回答"被什么利用"。**28 项手段词里 27 项为 0 是事实，不是本篇没找到。**任何把附表 D 当"利用工具清单"引用的说法都超出证据。
2. **具名只有 1 个、5 段，不能建"利用套件"表。** Coruna 之外全为 0。**"T2 点名了哪些利用套件"这个问题在公开文本里的完整答案是"一个"。**需要套件与链的技术描述时，来源只能是厂商分析与代码，两者都不在本篇语料内。
3. **`actively exploited` 的 18 段不足以支撑"活跃利用"的时间序列。**它只带平台（Intel Mac）或版本（before iOS 17.2）两种限定，**没有任何日期、行为体、样本名**。第 6 章的规律（"必带边界、从不裸用"）是措辞规律，不是运营规律。
4. **按段计数、不按缺陷计数。**同一句 `…may have been exploited…` 在连号版本页上重复（附表 C 里 `121752`–`121756`、`122372`–`122402`、`125884`–`125892` 各 2 段）。**换算缺陷数需要去重键（CVE + 块名），本期未建。**108 段 ≠ 108 起利用事件。
5. **iOS / macOS 未分域。**108 段的块名里 `dyld`、`Accessibility`、`RPAC`、`CoreGraphics` 多为 Mac 条目；R3 那 10 段直接写 `on Intel-based Mac systems`。**引用"iOS 在野利用 108 段"时必须再切一次页面归属。**
6. **12 个块名是措辞层的组件标签，不是受影响组件的判定。**`RPAC` 4 段、`RTKit` 8 段这类标签来自页面自身的 `<h3>` 标题，Apple 用它组织修复条目，**与"某组件被利用了"不是同一命题**。
7. **`exploit` ∩ `arbitrary code` = 31 的句法方向不可判。**31 段里两词分属**两个句子**（后果句 + 标注句，第 8 章），文本不提供"因为被这样利用，所以能执行代码"的方向。**因果链需要厂商分析，本篇不提供。**
8. **中文层 43 个窗口的人工归类未做。**`利用` 的 43 次里"善用功能"与"被滥用"两类各占多少，本篇只给出"43 个互不相同窗口"这一条**无固定术语**的证据，没有做语义二分类。这是可登记的口径缺口，不影响任何 0 断言。
9. **静态站、开发者文档、CVSS 层未纳入本篇。**"利用代码"的权威词汇在 CVSS 的 Exploit Code Maturity（E / P / U / F）与厂商 IOC 里，**T2 不采用这套词汇**——这也是附表 D 一片 0 的另一面：不是这些概念不存在，是 T2 不承担它们。

# 20 常见误读纠正

**误读一："T2 报道了 108 起利用事件。"** 108 是**段数**：同一句标注在连号版本页上复制，去重键（CVE + 块名）本篇未建；而且 **108 段里 103 段只说"有一份报告说可能被利用过"**。**正确口径：Impact 字段中含 `exploit` 词族的段落数 = 108。**

**误读二：把 `exploit`、`exploited`、`exploitation` 当一个词的不同写法。** `exploited` **103**、原形 **5**，后两者 **0**。**词族有 108 段 ≠ 这个词在 T2 里有名词用法。**恰恰相反：T2 只用它的被动分词形式（第 3 章）。

**误读三："`technique` 0 段说明 Apple 否认存在利用技术。"** 0 段是**词汇缺席**，不是**否定陈述**。同一片语料里连否定式 `not been exploited` 也是 **0 段**——**T2 对手段既不肯定也不否定，只是不写。**（对照：`exploitation` 0、`exploiter` 0、名词位 0、动宾 0，四个方向都缺。）

**误读四：把 R2 的 59 段读成"59 起国家级定向攻击"。** 59 段共享同一句形容词模板 `extremely sophisticated attack against specific targeted individuals`，**其中没有任何技术类别词**（第 15 章：`buffer` / `overflow` / `type` 在 108 段内全 0）。**这是一句评价，不是一份归因。**

**误读五："`tool` 有 1 段，所以 T2 提到过利用工具。"** 那段是 `unexpected tool termination`（块名 `otool`），指**被崩溃的命令行工具进程**。**词存在 ≠ 概念存在**——与 #113 的 `path` 9 段（全是文件系统路径）、#112 的 `interface` 29 段（多是 `network interface`）是同一条方法论。

**误读六："108 段与 890 段相加得约 998 段攻击者利用叙事。"** 交集 **25**、正确并集 **973**；更要紧的是 **E ⊄ P 且 P ⊄ E**：**108 段里 83 段根本不提攻击者**，890 段里 865 段不带在野标注。**两件事在文本上是分开写的。**

**误读七："38 段提到 `memory`，所以 38 起在野利用是内存破坏漏洞。"** 那 38 次的命中在**同段的前一句后果描述**里（`may lead to memory corruption`），标注句本身不含类别词（第 15 章）。**共现不是分类。**

**误读八："中文手册讲了'利用'这个概念，43 次呢。"** 43 次落在 **43 个互不相同的窗口**——没有固定搭配、没有术语条目。同一个词既写 `充分利用平台提供的可用安全能力`，也写 `进而被攻击者利用`。**把它当术语会造出一套并不存在的中文攻击词汇。**同理 `技术` 438 次的 **82.2%** 是"安全加密技术"与页脚"[技术支持]"。

# 21 结论

**（1）"代码或技术"这两个名词在公开文本里没有一个词位。** 手段侧 **28 项候选词 27 项全零**（附表 D），`exploit` 词族只以 **`exploited`（103）** 和 **`the Coruna exploit`（5）** 两种形态存在，`exploitation` / `exploiter` / 名词位 / 动宾 **各 0**。**T2 提供的是"是否已被用过"这个状态，不提供"用什么"这个对象。**

**（2）108 段的结构是"后果 + 状态"两联，第三联缺失。** 拆句后**没有任何一段只由标注句组成**（0 段），标注句永远跟在后果句之后、永远用 `this issue` 回指、永远不带 `by` 短语、永远不指称缺陷类型。**"手段"应有的句法位置在结构上是空的，不只是词表上是空的。**

**（3）限定语把话说不完，只能用形容词和版本边界。** 闭合分区 **5 + 59 + 10 + 8 + 26 = 108**、未归类 0；**59 段共享一句 `extremely sophisticated … against specific targeted individuals` 的评价**，**18 段带 `actively` 的必定同时给平台或版本边界**，**26 段什么都不给**。**14 种句式模板复制出全部 108 段**——措辞收敛度本身就是"没有技术描述"的证据。

**（4）不对称的幅度是本篇最有力的一句话：`maliciously crafted` 1,769 段 ∶ `exploit` 词族 108 段 ∶ `exploit code` / `technique` 0 段。** 描述恶意输入写 **81 个块名 / 218 页**，描述利用行为写 **108 段被动标注**，描述利用物**一个字都没有**。**公开文本选择了一条完整的效果叙述，加上一个空洞的原因叙述。**

**（5）本选题与 #113 的接口是 25 段，且那三个 103 是同一个集合。** E ∩ P = **25**（与 #113 第 14 章同一组段）；`{exploited}` = `{this issue}` = `{Apple is aware of a report}` = **103**；`|E| = 103 + 5`。**"Apple 承认在野利用"的独立条目上限是 103 段，跨期引用不得把这三次检索当三份证据。**

---

# 附表 A 含 `exploit` 的句子模板全表（14 行）

取 E 的 108 段，拆句后**只保留含 `exploit` 词族的那一句**，小写化、去标点、折空白后去重。"第 6 章归类"列由同一判序（R1→R5）作用在**规范化后的模板本身**上。纵向合计 **108**，与段级 E 相等；**模板数是写法数，不是事件数**（第 7 章第 3 点）。

| # | 规范化句式（含 `exploit` 的那一句，去标点、小写） | 句数 | 第 6 章归类 |
|---|---|---:|---|
| 1 | `apple is aware of a report that this issue may have been exploited ` | 24 | 裸式标注 |
| 2 | `apple is aware of a report that this issue may have been exploited in an extremely sophisticated attack against specific targeted individuals ` | 20 | 极复杂攻击+定向个人 |
| 3 | `apple is aware of a report that this issue may have been exploited in an extremely sophisticated attack against specific targeted individuals on versions of ios before ios 26 ` | 19 | 极复杂攻击+定向个人 |
| 4 | `apple is aware of a report that this issue may have been actively exploited on intel based mac systems ` | 10 | actively + Intel Mac |
| 5 | `apple is aware of a report that this issue may have been actively exploited against versions of ios before ios 17 2 ` | 8 | actively + 其它限定 |
| 6 | `apple is aware of a report that this issue may have been exploited in an extremely sophisticated attack against specific targeted individuals on ios ` | 8 | 极复杂攻击+定向个人 |
| 7 | `this is a supplementary fix for an attack that was blocked in ios 17 2 apple is aware of a report that this issue may have been exploited in an extremely sophisticated attack against specific targeted individuals on versions of ios before ios 17 2 ` | 8 | 极复杂攻击+定向个人 |
| 8 | `apple is aware of a report that this issue may have been exploited in an extremely sophisticated attack against specific targeted individuals on versions of ios before ios 27 ` | 3 | 极复杂攻击+定向个人 |
| 9 | `apple is aware of a report that this issue may have been exploited against versions of ios before ios 16 7 1 ` | 2 | 裸式标注 |
| 10 | `this fix associated with the coruna exploit was shipped in ios 17 2 on december 11th 2023 ` | 2 | Coruna 具名 |
| 11 | `apple is aware of a report that this issue may have been exploited in an extremely sophisticated attack against specific targeted individuals on versions of ios released before ios 18 4 1 ` | 1 | 极复杂攻击+定向个人 |
| 12 | `this fix associated with the coruna exploit was shipped in ios 16 6 on july 24 2023 ` | 1 | Coruna 具名 |
| 13 | `this fix associated with the coruna exploit was shipped in ios 17 3 on january 22 2024 ` | 1 | Coruna 具名 |
| 14 | `this fix associated with the coruna exploit was shipped in ios 17 on september 18 2023 ` | 1 | Coruna 具名 |

# 附表 B 12 个块名分布（12 行）

块名取自页面自身的 `<h3>` 组件标签（措辞层标签，不是受影响组件判定，第 19 章第 6 条）。纵向合计 **108**。

| # | 块名 | 含 `exploit` 的 Impact 段数 | 占 108 段 |
|---|---|---:|---:|
| 1 | WebKit | 41 | 38.0% |
| 2 | Kernel | 9 | 8.3% |
| 3 | Messages | 9 | 8.3% |
| 4 | CoreMedia | 8 | 7.4% |
| 5 | RTKit | 8 | 7.4% |
| 6 | ImageIO | 7 | 6.5% |
| 7 | CoreAudio | 5 | 4.6% |
| 8 | JavaScriptCore | 5 | 4.6% |
| 9 | dyld | 5 | 4.6% |
| 10 | Accessibility | 4 | 3.7% |
| 11 | RPAC | 4 | 3.7% |
| 12 | CoreGraphics | 3 | 2.8% |

# 附表 C 76 页逐页段数（76 行）

按段数降序、同段数按页号升序。"版式域"列：域 B 为粗体段起块的 11 页名单（`118479`、`120303`–`120305`、`120307`–`120312`、`120339`）内的页，其余为域 A。纵向合计 **108**，域 A **98** + 域 B **10**。

| # | 页号（顾问页 id） | 段数 | 版式域 | 备注 |
|---|---|---:|---|---|
| 1 | 126632 | 4 | 域 A | 具名 Coruna |
| 2 | 122345 | 3 | 域 A | — |
| 3 | 122346 | 3 | 域 A | — |
| 4 | 120308 | 2 | 域 B | — |
| 5 | 120881 | 2 | 域 A | — |
| 6 | 120882 | 2 | 域 A | — |
| 7 | 120883 | 2 | 域 A | — |
| 8 | 120893 | 2 | 域 A | — |
| 9 | 120895 | 2 | 域 A | — |
| 10 | 121752 | 2 | 域 A | — |
| 11 | 121753 | 2 | 域 A | — |
| 12 | 121754 | 2 | 域 A | — |
| 13 | 121755 | 2 | 域 A | — |
| 14 | 121756 | 2 | 域 A | — |
| 15 | 122173 | 2 | 域 A | — |
| 16 | 122174 | 2 | 域 A | — |
| 17 | 122282 | 2 | 域 A | — |
| 18 | 122372 | 2 | 域 A | — |
| 19 | 122400 | 2 | 域 A | — |
| 20 | 122401 | 2 | 域 A | — |
| 21 | 122402 | 2 | 域 A | — |
| 22 | 125884 | 2 | 域 A | — |
| 23 | 125885 | 2 | 域 A | — |
| 24 | 125886 | 2 | 域 A | — |
| 25 | 125889 | 2 | 域 A | — |
| 26 | 125890 | 2 | 域 A | — |
| 27 | 125891 | 2 | 域 A | — |
| 28 | 125892 | 2 | 域 A | — |
| 29 | 118479 | 1 | 域 B | — |
| 30 | 120304 | 1 | 域 B | — |
| 31 | 120305 | 1 | 域 B | — |
| 32 | 120307 | 1 | 域 B | — |
| 33 | 120309 | 1 | 域 B | — |
| 34 | 120310 | 1 | 域 B | — |
| 35 | 120311 | 1 | 域 B | — |
| 36 | 120339 | 1 | 域 B | — |
| 37 | 120880 | 1 | 域 A | — |
| 38 | 120884 | 1 | 域 A | — |
| 39 | 120886 | 1 | 域 A | — |
| 40 | 120898 | 1 | 域 A | — |
| 41 | 120900 | 1 | 域 A | — |
| 42 | 120910 | 1 | 域 A | — |
| 43 | 122066 | 1 | 域 A | — |
| 44 | 122068 | 1 | 域 A | — |
| 45 | 122071 | 1 | 域 A | — |
| 46 | 122072 | 1 | 域 A | — |
| 47 | 122073 | 1 | 域 A | — |
| 48 | 122281 | 1 | 域 A | — |
| 49 | 122283 | 1 | 域 A | — |
| 50 | 122284 | 1 | 域 A | — |
| 51 | 122285 | 1 | 域 A | — |
| 52 | 122374 | 1 | 域 A | — |
| 53 | 122375 | 1 | 域 A | — |
| 54 | 122376 | 1 | 域 A | — |
| 55 | 122722 | 1 | 域 A | — |
| 56 | 122900 | 1 | 域 A | — |
| 57 | 122901 | 1 | 域 A | — |
| 58 | 122902 | 1 | 域 A | — |
| 59 | 122903 | 1 | 域 A | — |
| 60 | 122904 | 1 | 域 A | — |
| 61 | 124925 | 1 | 域 A | — |
| 62 | 124926 | 1 | 域 A | — |
| 63 | 124927 | 1 | 域 A | — |
| 64 | 124928 | 1 | 域 A | — |
| 65 | 124929 | 1 | 域 A | — |
| 66 | 125141 | 1 | 域 A | — |
| 67 | 125142 | 1 | 域 A | — |
| 68 | 126346 | 1 | 域 A | — |
| 69 | 126348 | 1 | 域 A | — |
| 70 | 126351 | 1 | 域 A | — |
| 71 | 126352 | 1 | 域 A | — |
| 72 | 126353 | 1 | 域 A | — |
| 73 | 126646 | 1 | 域 A | 具名 Coruna |
| 74 | 149226 | 1 | 域 A | — |
| 75 | 149228 | 1 | 域 A | — |
| 76 | 149229 | 1 | 域 A | — |

# 附表 D 手段词电池（28 行）

每项都带词边界、大小写不敏感，三个字段分别统计。**28 项中 27 项在三个字段里全为 0**；唯一非零项 `tool` 的 1 段是 `otool` 页里的工具进程语义（`unexpected tool termination`）。计数为 0 时"页"与"块名"两列同为 0。

| 词 / 词组（带词边界） | Impact 段 | Description 段 | 致谢段 | 页 | 块名 |
|---|---:|---:|---:|---:|---:|
| exploit code（字面词组） | 0 | 0 | 0 | 0 | 0 |
| technique(s) | 0 | 0 | 0 | 0 | 0 |
| method(s) | 0 | 0 | 0 | 0 | 0 |
| proof of concept / PoC | 0 | 0 | 0 | 0 | 0 |
| weaponized | 0 | 0 | 0 | 0 | 0 |
| toolkit(s) | 0 | 0 | 0 | 0 | 0 |
| tool | 1 | 0 | 0 | 1 | 1 |
| malware | 0 | 0 | 0 | 0 | 0 |
| chain(s) | 0 | 0 | 0 | 0 | 0 |
| in the wild | 0 | 0 | 0 | 0 | 0 |
| shellcode | 0 | 0 | 0 | 0 | 0 |
| payload(s) | 0 | 0 | 0 | 0 | 0 |
| rootkit(s) | 0 | 0 | 0 | 0 | 0 |
| spyware | 0 | 0 | 0 | 0 | 0 |
| surveillance | 0 | 0 | 0 | 0 | 0 |
| Pegasus | 0 | 0 | 0 | 0 | 0 |
| DarkSword | 0 | 0 | 0 | 0 | 0 |
| kit | 0 | 0 | 0 | 0 | 0 |
| framework | 0 | 0 | 0 | 0 | 0 |
| flaw(s) | 0 | 0 | 0 | 0 | 0 |
| bug(s) | 0 | 0 | 0 | 0 | 0 |
| an/the/this exploit（名词位） | 0 | 0 | 0 | 0 | 0 |
| exploit + 缺陷介词 | 0 | 0 | 0 | 0 | 0 |
| exploitation | 0 | 0 | 0 | 0 | 0 |
| exploiter(s) | 0 | 0 | 0 | 0 | 0 |
| has been exploited | 0 | 0 | 0 | 0 | 0 |
| NOT been exploited | 0 | 0 | 0 | 0 | 0 |
| reported to Apple | 0 | 0 | 0 | 0 | 0 |

# 附表 E 错误写法与纠正对照（8 行）

| # | 错误写法 | 为什么错 | 正确口径 |
|---|---|---|---|
| 1 | "T2 报道了 108 起在野利用" | 108 是**段**数，同句标注在连号版本页复制（`121752`–`121756`、`125884`–`125892` 等），去重键未建；且 103 段的原文只是"有一份报告说可能被利用过" | Impact 含 `exploit` 词族的**段落数 108**；引用缺陷数需按 CVE + 块名去重 |
| 2 | "`exploit` 在 T2 里既作名词又作动词" | 名词位 `an / the / this exploit` **0 段**、`exploitation` **0**、`exploiter` **0**；5 处原形全在 `the Coruna exploit` 定语里 | **只有被动分词 `exploited`（103 段）有独立用法**，原形不构成名词用法 |
| 3 | "`technique` 0 段 = Apple 否认利用技术" | 词汇缺席 ≠ 否定陈述；同语料里 `not been exploited` 亦 **0 段**，T2 对手段既无肯定也无否定 | 只能说"不写"；四个方向（名词位 / 动宾 / `-ation` / `-er`）**全 0** |
| 4 | "59 段是国家级定向攻击" | 59 段共享一句评价模板，**其中没有任何缺陷类别词**（`buffer` / `overflow` / `type` 在 108 段内全 0） | R2 是**评价 + 版本或平台尾缀**（59 段中 20 句不写尾缀），不是归因 |
| 5 | "`tool` 有 1 段 → T2 提到利用工具" | 那段是 `unexpected tool termination`（`otool` 页），指工具进程自己崩溃 | **词存在 ≠ 概念存在**；同 `path` 9 段（#113）、`interface` 29 段（#112） |
| 6 | "108 + 890 段攻击者利用叙事" | 交集 **25**，正确并集 **973**；**E ⊄ P、P ⊄ E**：108 段里 83 段不提攻击者，890 段里 865 段无在野标注 | 并集必须成员求并，并同时报 \|A\|、\|B\|、\|A ∩ B\| |
| 7 | "38 段 `memory` = 38 起内存破坏类在野漏洞" | 38 次命中来自**同段前一句后果描述**，标注句不含类别词；回指词只有 `this issue`（103 段） | 只能说"38 段的同段后果句提到 memory"；类别判定不在公开文本内 |
| 8 | "中文手册有 43 次'利用'，所以中文有利用术语" | 43 次对应 **43 个互不相同的窗口**，无固定搭配；同词兼表"善用功能"与"被滥用"；**中文零命中词 16 项**（含 `利用代码`、`漏洞利用`、`概念验证`、`武器化`、`在野`） | 中文层有"利用"这个**动词**，没有"利用物"这个**术语**；`技术` 438 次的 82.2% 是"安全加密技术"与页脚"[技术支持]" |

---

**边界声明：** 本篇全部内容是对 Apple 公开安全响应顾问页英文镜像（257 份、9,136 段，两个版式域）与《Apple 平台安全保护》中文指南转储（178 页可用正文域）的词形分布、字段位置、限定语分区、句式模板、集合交并与零命中词表的**文本计量分析**，用于说明公开文本如何陈述"被利用"这一状态、又如何在措辞层回避"利用物"。本篇**不提供任何利用代码、利用技术或恶意样本**，不含针对任何缺陷的利用方法、构造步骤、触发时序或调试路径，不含绕过访问控制或权限检查的说明，也不构成对任何具体漏洞是否可被利用的判定；文中出现的组件名、CVE 编号、页号与段落均为对公开文本的引用与计数，"零命中"只表示公开措辞未写该词，不表示相应技术或事件不存在；"限定语分区 R1–R5"与"用法三分区 U1–U3"是对**公开句子措辞**的分类，不是威胁情报分类。所有百分比与段数只描述本篇镜像与切分口径下的样本，若上游文本更新，全部数值需按第 18 章复算。

Telegram: https://t.me/one00190
