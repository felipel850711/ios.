@@TITLE@@iOS 无需目标主动点击特定内容即可触发的攻击方式
@@SUBTITLE@@选题把**触发前提（无需主动点击）**与**结果（攻击方式可被触发）**捆在一句里。逐词测量之后的结果是：**英文措辞层里根本不存在表示"点击"或"交互"的词**。含 Impact 的 7,434 段里，`click` 词界 **0 段**、`tap`／`taps` **0**、`swipe` **0**、`press` **0**、`hold` **0**、`double-click` **0**、`one-click` **0**、`zero-click` **0**、`zero click` **0**、`user interaction` **0**、`user action` **0**、`user input` **0**、`user gesture` **0**、`user-initiated` **0**、`requires user` **0**、`by the user` **0**、`the user may` **0**、`when the user` **0**、`after the user` **0**、`manual`／`manually` **0**、`unattended` **0**、`knowingly` **0**、`unknowingly` **0**；`zero` 词界在 Impact **0**、Description **0**，只出现在致谢侧（653 段，全是机构名）。所以选题里"是否点击"这一维**无法正面测量**，只能测它的**反面写法**：把 **WO 集**定义为「Impact 含 `without` 词界」＝ **178 段 / 97 页 / 37 名 / 37 条 distinct Impact 句**，占含 Impact 段的 **2.39%**；Description 侧 `without` **0 段**。**五把刀**：**句尾刀**（178 段里 `without` 短语**全部延伸到 Impact 末尾**，句尾形 **178∶0**，且每段恰好 1 个 `without`：**178∶0**）、**宾语刀**（15 类互斥分区闭合到 **178**，剥离冠词后宾语短语只有 **18 项**；`without user consent` 47 ＋ `without authentication` 43 = **90 段 = 50.6%**；`without` 之后紧跟 `user` 的 **70 段**、不跟的 **108 段**）、**主语刀**（19 个 distinct 主语串闭合 178，**"人"作主语头只有 27 段**，而 `a remote attacker` 的 **194 段与 WO 零交**、句首 `a user` 的 61 段与 WO **零交**）、**语态刀**（WO 内 `may be`＋非 `able` 被动 **34 段 = 19.1%**，全集只有 **88 段 = 1.18%**，**约 16 倍**，且 34 段分属三句、主语全是数据）、**接缝刀**（`without` 本身改按三字段拼接统计**零丢失**：并 189 ＝ 拼接 189；它的 15 个宾语词却大面积丢段：`consent` 76→25 **丢 51**、`authentication` 144→93 **丢 51**、`permission` 55→21 **丢 34**、`user` 1,669→1,553 **丢 116**，机制是 **7,334／7,434 段 Impact 没有末尾标点**，宾语词正好压在字段接缝上）。
@@INFO@@报告日期|2026 年 10 月 28 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 7,434 段含 Description / 9,132 段含致谢**。无交互措辞集 WO = Impact 侧含 `without`（词界）＝ **178 段 / 97 页 / 37 名 / 37 条 distinct Impact 句**，全字段并集 **189**、三字段拼接口径 **189**、Description 侧 **0**、致谢侧 **11**。对照集 R = #118 的远程触达集（Impact 侧 11 个通道词族）**843 段 / 178 页 / 72 名**；G = #116 的动名词集（Impact **首句**以 `\w+ing` 开头）**2,179 段 / 224 页 / 98 名 / 21 个动名词 / 136 条 distinct 首句**；A = `attacker` 词族 **890 段**；F = `maliciously crafted` **1,769 段**；S = `remote attacker` **194 段**；`shortcut` 词族 **101 段**。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**公开英文文本用哪些否定短语写"未经某前提"、这些短语落在句子的哪个位置、哪些组件名与之同段、以及在哪些维度上它根本不写"点击"或"交互"**，属于措辞与文本计量分析；文中任何"无需交互"表述均指**措辞层的词形分布**，不是对某组件实际触发条件的判断
@@INFO@@分类|判据**电池 90 项 / 并集全零 45 项 / 非零 45 项**（附表 A，五组：零点击词汇 16、交互动作名词 18、授权缺失宾语 18、无人值守副词 20、可见性与提示面 18；其中 **Impact 侧为零但致谢侧非零 11 项**）/ WO 的**十五分区互斥闭合 = 178**（附表 B，16 行含合计）/ 对照集 G 的 **21 个句首动名词 × ∩WO 全零**（附表 C，22 行含合计）/ **组件名 × WO 矩阵 37 行**（附表 D，逐名报 WO 段、∩R、∩G、该组件 Impact 全集与 WO 占比）/ **中文层 46 项**（附表 E，六组：触屏动作 8、授权动作 10、交互名词 6、无交互侧 8、凭据侧 8、可见性侧 6）/ **五把刀**：句尾刀（178∶0）、宾语刀（18 项闭合、70∶108）、主语刀（19 串闭合、人 27 段、`remote attacker` ∩WO = 0）、语态刀（19.1% ∶ 1.18%）、接缝刀（`without` 丢 0、宾语词丢 3～116）
@@INFO@@难度|进阶（第 4 章的 178 是把 15 个宾语判据按**成员集合**逐段互斥归档的结果，不能由 178 与 15 两个数推出"一类一短语"；W14 的判据必须写成 `without (an 或 user) indicator`，只写 `without indicator` 会少 3 段、闭合掉到 175；第 8 章的 19 个主语串要求把"句首到第一个情态动词之前"整串取出，`A shortcut` 与 `A Shortcut` 是**两个**串（25＋3），合并大小写才得 18；第 15 章的接缝刀只咬**宾语词压在 Impact 字段末尾**的段落，`without` 自身在句中所以丢 0，这是脚本口径现象而非语料现象；第 9 章的 27 与第 16 章的 27 数值相同、口径无关——前者是"主语头含 user 或 attacker 的 WO 段"，后者是"WO∩R"，是刻意设置的读法陷阱）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

选题字面是"无需目标主动点击特定内容即可触发的攻击方式"。这句话里可以被测量的成分是**触发前提**：要不要目标先做一次点击。逐词测量之后的结论是：**这个维度在英文措辞层没有正面词，也没有反面词**。

**"点击"没有词。** `click` 词界在 Impact **0 段**、Description **0**，致谢侧 **1 段**（人名机构名）；`tap`／`taps` Impact **0**（致谢 2）、`swipe` **0**、`press` **0**、`hold` **0**、`drag` Impact **0**（致谢 11）、`double-click` **0**、`one-click` **0**。唯一带 `click` 的词形是 `clickjacking`：Impact **1 段**、Description 2 段、三字段并 3 段——它是一个**界面欺骗术语**，不是"点击"这一动作。（第 6 章）

**"零点击"这个词组也不存在。** `zero` 词界 Impact **0**、Description **0**、致谢 **653 段 / 163 页 / 37 名**；`zero-click` **0**、`zero click` **0**。Impact 侧含 `zero`（不要求词界）**0 段**，Description 侧 **0 段**——致谢侧的 653 段全部是 `Trend Micro Zero Day Initiative`（530 段）、`Google Project Zero`（113 段）、`ZeroClicks`（16 段）等**机构名**。（第 6 章）

**"交互"也没有词。** 附表 A 的"交互动作名词"组 18 项里 **15 项并集为零**：`interact`、`interaction`、`user interaction`、`user action`、`user input`、`user gesture`、`user initiation`、`user approval`、`user-initiated`、`requires user`、`the user may`、`when the user`、`after the user`、`by the user`、`manual`／`manually`。非零的 3 项是 `a user may`（**39 段**）、`if the user`（**6**）、`the user has`（**6**），而这 3 项与 WO 的交集**都是 0**。（第 7 章）

**于是本篇只能测它的否定写法。** 措辞层唯一稳定存在的"某前提没有被满足"的表达是 `without` ＋ 宾语。把 **WO 集**定义为 **Impact 侧含 `without`（词界）**，得 **178 段 / 97 页 / 37 名 / 37 条 distinct 句**，占含 Impact 的 7,434 段的 **2.39%**。这个定义的好处是它**可闭合**：178 段被 15 类宾语判据**互斥且完整地**归档（第 4 章），宾语短语去重后只有 **18 项**（第 5 章），37 条 distinct 句的出现次数**恰好等于 178**（第 18 章）。

四组反差。

**反差一（否定式只写在句尾）**：178 段里，`without` 短语**全部延伸到 Impact 的末尾**——"句中还有后续内容"的段数 **0**。同时每段**恰好出现 1 次** `without`（≥2 次的 **0 段**）。措辞层的句子骨架是固定的：`[主语] [may / may be able to / could] [动作] without [宾语].`（第 5 章）

**反差二（"人"几乎不出现在主语位）**：178 段的主语串去重得 **19 个**，其中主语头含 `user` 或 `attacker` 的只有 **27 段**；**程序与数据作主语 147 段**，其余 **4 段**是 `A maliciously crafted email`。**`a remote attacker` 的 194 段与 WO 零交**，**句首 `a user` 的 61 段与 WO 零交**，`the user may` **0 段**。措辞层不写"用户没点"，只写"某程序能做某事而无某前提"。（第 8 章）

**反差三（否定式偏爱被动，而被动偏爱数据主语）**：WO 内 `may be` ＋ 非 `able` 的被动 **34 段 = 19.1%**，全库 Impact 只有 **88 段 = 1.18%**，比值 **≈16 倍**（若把被动收紧为 `may be` ＋ 过去分词，全集 **61 段 / 48 页**，比值 **≈23 倍**）。这 34 段只来自三句：`Photos in the Hidden Photos Album may be viewed without authentication`（16）、`Private Browsing tabs may be accessed without authentication`（15）、`An encrypted volume may be accessed by a different user without prompting for the password`（3）——**主语全是被访问的数据，不是任何人**。（第 9 章）

**反差四（"触发"这一维与远程通道几乎无关）**：WO ∩ R = **27 段**，占 WO 的 **15.2%**、占 R 的 **3.2%**；WO 与 #116 的动名词集 G **零交**（**0 段**，且 G 集 2,179 段的 Impact **非首句**里也不含 `without`——两个零各自独立）。27 段的通道归因闭合为 `website` 10 ＋ `network`（不含 AirPlay）9 ＋ `network`＋`AirPlay` 7 ＋ `Wi-Fi` 1 = **27**。（第 16 章）

于是本篇的问题依次是：

**问题一（第 3 章）** 措辞层到底有多少段写了否定前提。答案：**178 段**，且 Description 侧 **0**、致谢侧 11、三字段并 189；`without` 的出现总次数 **178**（每段一次）。

**问题二（第 4、5 章）** 这 178 段"无"的是什么。答案：**十五类互斥分区**（附表 B）；`user consent` 47、`authentication` 43（42＋1 个 `authentication in assistive access`）、`prompting` 19、`user authorization` 9、`user permission` 9、`consent` 7、`having …` 11、`pairing` 7、`permission` 5、`valid …` 5、`user confirmation` 5、`entitlement check` 4、`indicator` 3、`restrictions` 3、`authorization` 1，合计 **178**。

**问题三（第 6、7 章）** "点击／交互"有没有替代词形。答案：**没有**——附表 A 的五组 90 项里 **45 项并集全零**，其中零点击词汇组 16 项有 7 项全零、交互动作名词组 18 项有 15 项全零。

**问题四（第 10 章）** 178 段用什么情态动词。答案：`may be able to` **123** ＋ 裸 `may` **48** ＋ `could` **7** ＋ 其余 **0** = **178**；`could` 的 7 段**全部**是同一句 AirPlay（页 122371–122378）。

**问题五（第 13 章）** 哪些组件被写成"无某前提"。答案：**472 个组件名里只有 37 个 = 7.8%** 出现在 WO 内，其中 **8 名的 Impact 段 100% 落在 WO**（App Support 7、CoreCrypto 7、CoreMotion 7、iCloud Document Sharing 6、CoreBluetooth - LE 4、Parental Controls 4、Screen Sharing 3、Storage Management 3，合计 41 段 = WO 的 23.0%）；Top 是 Shortcuts 36（占 20.2%）、WebKit 12、Safari Private Browsing 11、Photos 9。

**问题六（第 19 章）** 中文手册层写不写"点击"。答案：**几乎没有**——`轻点` **13 次 / 10 页**、`点按` **1**、`点击` **1**、`长按`／`拖动`／`滑动`／`缩放`／`触控板`／`触摸` **各 0**；但 `自动` **898 次 / 178 页**、`后台` **234 / 178**、`无需` **64 / 48**、`未经` **21 / 19**、`静默` **1**、`不知情` **1**、`弹窗` **0**、`红点` **0**。

# 1 操作性定义与分母

**T2（措辞层）**：`_tmp/adv2/<id>.html` 共 258 份，排除页面 `122085` 得 **257 页**。按 #111 第 1 章的两版式域切块，得 **9,136 段**（域 A **9,045** ＋ 域 B **91**）、**257 页**、**472 个组件名**；含 Impact **7,434**、含 Description **7,434**、含致谢 **9,132**。

**段（block）**＝一个组件条目，`{imp, des, ack, nm, id}` 五字段；**判据命中**＝该段任一字段出现该词形；**并集**＝Impact∪Description∪致谢 三字段**分别匹配后取并集**（本篇唯一允许的口径，理由见第 15 章）。

**词界**用 `(?<![\w-])kw(?![\w-])`；**词族**用前缀 `(?<![\w-])kw`。词组内空格统一用 `$S = [ \x{a0}\x{202f}]`（普通空格、不换行空格、窄不换行空格三形），写作 `foo${S}bar`；连字符类写作 `$H = [ \x{a0}\x{202f}-]`，**连字符必须放在字符类末尾**。所有字段匹配一律带 `/i`——这是本篇的一个**口径风险点**：`without` 本身大小写不敏感无影响，但 R 的通道词族若漏掉 `/i` 会从 843 段掉到 814 段（第 20 章的断言就是用来钉住这个的）。

**核心集合（本篇定义）**：

- **WO**（无交互措辞集，**Impact 侧口径**）= `(?<![\w-])without(?![\w-])` 命中 Impact ＝ **178 段 / 97 页 / 37 名**。这是本篇的主集合。之所以取 Impact 而非三字段并集，是因为 Description 侧 `without` **0 段**，并集多出的 11 段全部来自致谢文本（人名与机构名），与"触发前提"无关。
- **WO∪**（三字段并集口径）＝ **189 段 / 105 页 / 39 名**；**WO△**（三字段拼接后匹配口径）＝ **189 段**。二者相等，即 `without` 一词**不受接缝影响**（第 15 章）。
- **R**（远程触达集，#118 定义原样复用）＝ 11 个通道词族命中 Impact ＝ **843 段 / 178 页 / 72 名 / 113 条 distinct 句**。
- **G**（动名词集，#116 定义原样复用）＝ Impact **首句**以 `\w+ing` 开头 ＝ **2,179 段 / 224 页 / 98 名 / 21 词 / 136 条 distinct 首句**；Description 侧动名词开头必须为 **0**（这是该集定义的一部分）。
- **S** = `remote attacker` **194 段**；**A** = `attacker` 词族 **890 段**；**F** = `maliciously crafted` **1,769 段**；**SR** = `shortcut` 词族 **101 段**。

**十五分区（附表 B 的归档规则）**：对 WO 的每一段，按 Impact 句内**首次命中**的宾语判据归档，判据顺序为 W1 `without user consent` → W2 `without user authorization` → W3 `without user permission` → W4 `without user confirmation` → W5 `without prompting` → W6 `without consent` → W7 `without authentication` → W8 `without permission` → W9 `without authorization` → W10 `without pairing` → W11 `without valid …` → W12 `without having …` → W13 `without (an) entitlement check` → W14 `without (an 或 user) indicator` → W15 `without restrictions`。**顺序是定义的一部分**：`without user consent` 同时匹配 W6 `without consent` 的词干，若不先收 W1，W6 会从 7 段涨到 54 段而总数不变。十五分区**互斥且并集恰为 178**。

**T1（手册层）**：`_tmp/sec*.txt` 179 份，排除 `sec2727afbbc` 得 **178 页可用正文域**；中文词表按字面出现计数，同时报**次数**与**页数**。

# 2 结论速览（每条都可按第 20 章复算）

1. **WO 规模**：**178 段 / 97 页 / 37 名 / 37 条 distinct 句**，占含 Impact 的 **2.39%**；Description 侧 `without` **0 段**，致谢侧 **11 段**，三字段并 **189**；`without` 出现总次数 **178**，每段恰好 1 次。
2. **"点击"全零**：`click` 词界 Impact **0**、`tap`/`taps` **0**、`swipe` **0**、`press` **0**、`hold` **0**、`drag` **0**、`double-click` **0**、`one-click` **0**；非零只有 `clickjacking`（Impact **1**）与 `touch`（Impact **1**，且该唯一命中是组件名 **Touch Bar**）。
3. **"零点击"不存在**：`zero` 词界 Impact **0**、Desc **0**、Ack **653 段 / 163 页 / 37 名**；`zero` 子串在致谢侧 **698 段 / 167 页 / 45 名**；`zero-click` **0**、`zero click` **0**；Impact 与 Description 两侧含 `zero`（不要求词界）都是 **0 段**。
4. **"交互"15 项全零**：`interact`、`interaction`、`user interaction`、`user action`、`user input`、`user gesture`、`user initiation`、`user approval`、`user-initiated`、`requires user`、`the user may`、`when the user`、`after the user`、`by the user`、`manual`／`manually` 并集全零；非零三项 `a user may` 39、`if the user` 6、`the user has` 6 与 WO **全部零交**。
5. **十五分区闭合**：47＋9＋9＋5＋19＋7＋43＋5＋1＋7＋5＋11＋4＋3＋3 = **178**；∩R 列合计 **27**；distinct 句列合计 **37**。
6. **宾语短语 18 项闭合**（剥离 the/a/an 后）：`user consent` 47、`authentication` 42、`prompting the user` 16、`user authorization` 9、`user permission` 9、`consent` 7、`having the private key` 7、`pairing` 7、`permission` 5、`user confirmation` 5、`valid credentials` 5、`entitlement check` 4、`having app management permission` 4、`indicator` 3、`prompting for the password` 3、`restrictions` 3、`authentication in assistive access` 1、`authorization` 1 = **178**。
7. **`user` 是否紧跟**：`without`＋`user` 紧邻 **70 段**（W1–W4），不跟 user 的 **108 段**（W5–W15），70＋108 = **178**。
8. **句尾刀**：`without` 短语延伸至 Impact 末尾 **178 段**、句中 **0 段**；WO 的 178 段 Impact **全部**无末尾标点；全库含 Impact 的 7,434 段里无末尾标点 **7,334 段（98.7%）**、以句号结尾仅 **100 段**。
9. **主语刀**：distinct 主语串 **19** 个（合并大小写得 18）；主语头含 `user`/`attacker` **27 段**、程序或数据作主语 **147 段**、其余 **4 段**（`A maliciously crafted email`）；`a remote attacker` 194 段 **∩WO = 0**、句首 `a user` 61 段 **∩WO = 0**、`a user may` 39 段 **∩WO = 0**。
10. **语态刀**：WO 内被动 `may be`（非 `able`）**34 段 = 19.1%**；全集 **88 段 / 63 页 = 1.18%**，**≈16 倍**；收紧为 `may be` ＋ 过去分词则全集 **61 段 / 48 页 = 0.82%**，**≈23 倍**；`may be able to` 全库 4,722 段、∩WO **123**。
11. **情态闭合**：`may be able to` **123** ＋ 裸 `may` **48**（其中被动 34、非 `be` 形 14）＋ `could` **7** ＋ 无情态 **0** = **178**；`could` 的 7 段全是 AirPlay 同一句（页 122371–122378）。
12. **副词层**：`actively` Impact **19**（＝"may have been actively exploited" 18 ＋ Bluetooth 1，与 WO **零交**）；`silently` **7**（∩WO **0**、∩R 6）；`unintentionally` **14**（∩WO **0**）；`notif-` 词族 **32**（∩WO **0**）；`unexpected` **2,053**（∩WO **0**、∩R 175）；`automatic`／`automatically`／`auto`／`silent`／`unattended`／`passively`／`knowingly`／`unknowingly`／`inadvertently`／`deliberately`／`intentionally`／`without knowing`／`merely`／`simply` Impact 全 **0**。
13. **可见性刀**：`indicator` 词界 Impact **8**，其中 `without an indicator` **3 段**（Control Center，全库唯一写"没有指示器"的否定式）；`dialog` **3**、`alert` **0**、`banner` **0**、`sheet` Impact **0**、`visible to the user`／`shown to the user`／`may not be shown` **各 0**。
14. **组件层**：37 名 / 472 名 = **7.8%**；**8 名的 WO 占比 = 100%**，合计 **41 段 = WO 的 23.0%**；Top Shortcuts 36（20.2%）、WebKit 12、Safari Private Browsing 11、Photos 9、AirPlay 7；**37 名中只有 8 名在 R 里有段**，29 名完全不写通道。
15. **接缝刀**：`without` 三字段并 **189** ＝ 拼接 **189**，丢 **0**；宾语词丢段 `consent` **51**、`authentication` **51**、`permission` **34**、`user` **116**、`restrictions` **30**、`pairing` **8**、`indicator` **3**、`authorization` **4**，而 `prompting` **0**、`entitlement` **0**、`credential` **0**；四个交互名词（`interact`／`interaction`／`user interaction`／`zero-click`）并 **0**、拼接 **0**。
16. **集合接口**：WO∩R = **27**（占 WO 15.2%、占 R 3.2%）；**WO∩G = 0**、WO∩G∩R = **0**；G∩R = **108**（占 G 5.0%、占 R 12.8%）；WO∩`shortcut` 词族 = **43**（占 WO 24.2%）；WO∩`attacker` = **20**；WO∩`malicious` = **18**；WO∩`crafted`（词界）= **4**；WO∩`data` = **32**；WO∩`code` = **0**；WO∩`not/never` = **0**；WO∩`unauthorized` = **0**。
17. **通道归因**：WO∩R 的 27 段闭合为 `website` **10** ＋ `network`（不含 AirPlay）**9** ＋ `network`＋`AirPlay` **7** ＋ `Wi-Fi` **1**；含 `network` 的合计 16 段。`SMB`／`AFP`／`NTLM` 与 WO **零交**。
18. **页分布**：distinct **97 页**；单段页 **59**、双段页 **17**、≥3 段页 **21**（合计 **85 段 = 47.8%**）；最大页 122373 = **8 段**；Top10 页合计 **52 段 = 29.2%**。
19. **版式域自检**：域 B 的 91 段里 WO 只有 **5 段**，域 A **173 段**，173＋5 = 178；R 的 838＋5 与 G 的 2,155＋24 同样闭合。
20. **中文层**：`授权` 353/178、`确认` 250/178、`验证` 433/178、`认证` 380/93、`允许` 152/82、`批准` 49/25、`提示` 45/32、`同意` 26/19、`许可` 14/12、`询问` 5/4、`交互` 19/16、`操作` 476/178、`手动` 27/19、`无需` 64/48、`未经` 21/19、`轻点` 13/10、`弹窗` **0**、`红点` **0**、`长按`／`拖动`／`滑动`／`缩放` **0**。

# 3 `without` 一个词：178 段是怎么来的

WO 的判据只有一个词形：`(?<![\w-])without(?![\w-])`，作用在 **Impact** 字段。三字段分别测量：

| 字段 | 段 | 页 | 名 | distinct 句 | 出现总次数 |
|---|---:|---:|---:|---:|---:|
| Impact | **178** | 97 | 37 | 37 | 178 |
| Description | **0** | 0 | 0 | 0 | 0 |
| 致谢 | 11 | 11 | 6 | — | — |
| 三字段并集 | **189** | 105 | 39 | — | — |

四点必须交代。

**其一，Impact 与 Description 的 0∶178 反差是结构性的。** Description 字段写的是"这个问题是如何修复的"，其措辞是 `This issue was addressed with improved …`，本来就不描述前提；所以"无某前提"这一陈述**只可能**出现在 Impact。这不是统计巧合，而是字段语义决定的，因此本篇把 WO 钉在 Impact 侧。

**其二，致谢侧的 11 段不能进集合。** 这 11 段里的 `without` 出现在致谢行的机构/人名描述中，与组件后果无关；把它们并进来会得到 **189**，与 178 相差 11。本篇两个数都报，但**主集合是 178**。

**其三，"每段恰好一个 `without`"是闭合的前提。** 实测：恰好 1 个 **178 段**、≥2 个 **0 段**。所以 **出现总次数 178 ＝ 段数 178**，distinct 句数 37 与段数之比 **4.81∶1**（第 18 章）。若存在一段两句，`without` 的短语归因就会出现歧义，第 4 章的互斥分区就不成立。

**其四，2.39% 这个占比要用对分母。** 分母是**含 Impact 的 7,434 段**，不是全树 9,136 段（后者会让占比变成 1.95%）。同一分母下的对照：**R = 11.3%（843）**、**G = 29.3%（2,179）**、`unexpected` = 27.6%（2,053）。**措辞层写"未经某前提"的段落比写"远程通道"的还少一个数量级的覆盖。**

# 4 十五分区：178 的互斥归档（附表 B）

按第 1 章定义的**首次命中顺序**归档，十五分区互斥、并集恰为 WO：

| 类 | 宾语 | 段 | 页 | 名 | ∩R | distinct 句 | Top 组件 |
|---|---|---:|---:|---:|---:|---:|---|
| W1 | `without user consent` | 47 | 41 | 15 | 14 | 12 | App Support 7、CoreMotion 7、WebKit 7 |
| W2 | `without user authorization` | 9 | 9 | 2 | 0 | 2 | Contacts 5、Mail 4 |
| W3 | `without user permission` | 9 | 9 | 4 | 0 | 2 | CoreBluetooth - LE 4、LaunchServices 2、WebKit 2 |
| W4 | `without user confirmation` | 5 | 5 | 1 | 0 | 1 | Shortcuts 5 |
| W5 | `without prompting` | 19 | 19 | 2 | 0 | 2 | Shortcuts 16、DiskArbitration 3 |
| W6 | `without consent` | 7 | 7 | 1 | 0 | 1 | Shortcuts 7 |
| W7 | `without authentication` | 43 | 33 | 11 | 1 | 7 | Safari Private Browsing 11、Photos 8、iCloud Document Sharing 6 |
| W8 | `without permission` | 5 | 4 | 2 | 0 | 2 | Watch App 3、Shortcuts 2 |
| W9 | `without authorization` | 1 | 1 | 1 | 0 | 1 | Touch Bar 1 |
| W10 | `without pairing` | 7 | 7 | 1 | 7 | 1 | AirPlay 7 |
| W11 | `without valid …` | 5 | 5 | 2 | 5 | 1 | Screen Sharing 3、Screen Sharing Server 2 |
| W12 | `without having …` | 11 | 11 | 2 | 0 | 2 | CoreCrypto 7、LaunchServices 4 |
| W13 | `without (an) entitlement check` | 4 | 4 | 1 | 0 | 1 | Parental Controls 4 |
| W14 | `without (an 或 user) indicator` | 3 | 3 | 1 | 0 | 1 | Control Center 3 |
| W15 | `without restrictions` | 3 | 3 | 1 | 0 | 1 | dyld 3 |
| | **合计** | **178** | **97** | **37** | **27** | **37** | |

三点必须交代：

**其一，页/名两列不能相加。** 十五行的"页"列合计 **161** 远大于 WO 的 **97 页**，"名"列合计 **47** 大于 **37 名**——一页、一个组件名可同时进多个分区。只有"段"列与"∩R"列可加，分别闭合到 **178** 与 **27**；"distinct 句"列也闭合到 **37**，因为分区按**句内首次命中**判定，一条 distinct 句只落一个区。

**其二，两类"同意"必须分开。** W1（`without user consent` 47）与 W6（`without consent` 7）之和 **54** ＝ 附表 A 里 `consent` 的 Impact 段数 **54**、也＝ 该行的 ∩WO 列 **54**。W2（9）＋ W9（1）= **10** ＝ `authorization` 的 Impact **11** 段减去**不带 `without` 的那 1 段**，附表 A 的 ∩WO 列报的正是 10。W3（9）＋ W8（5）＋ W12 里的 `having app management permission`（4）= **18** ＝ `permission` 行的 ∩WO，其余 **37** 段不带 `without`。**"user"这一限定词的有无是两套语义**，且**"段数"与"∩WO 数"在这一格里不相等**（55 段 vs 18 段），不能合并统计。

**其三，W14 的判据必须写成两个形。** 三段 Control Center 句子是 `An app may be able to record the screen without an indicator`，若判据写成 `without indicator` 会**漏掉全部 3 段**、闭合掉到 **175**；而 `without user indicator` 这个形在语料里出现 0 次，保留它是为了让判据与 `indicator` 词族（Impact 8 段）的语义域对齐。这是本篇最容易被复算错的一格。

W10 的 7 段是 WO 里唯一用 `could` 的段落（第 10 章），也是唯一"通道词＋否定前提"同时满格的段落（∩R = 7 = 7 段 100%）；W11 的 5 段 ∩R 也是 5（`An attacker on the network … without valid credentials`，Screen Sharing／Screen Sharing Server）。**W10＋W11 的 12 段构成 WO∩R 的 27 段里的 12 段**（第 16 章）。

# 5 句尾刀：`without` 短语 100% 压在 Impact 末尾

对 WO 的 178 段逐段检查 `without` 之后的文本是否触到字段末尾：

- `without` 短语延伸至 Impact **末尾**（其后无 `.!?;:`）：**178 段**
- 其后还有其它句子内容：**0 段**

再检查 Impact 字段本身的标点习惯：

| 口径 | 段 | 占比 |
|---|---:|---:|
| 含 Impact 的段 | 7,434 | 100% |
| Impact 以句号结尾 | 100 | 1.3% |
| Impact 以其它标点（`;`∶`!`∶`?`）结尾 | 0 | 0.0% |
| Impact **无末尾标点** | **7,334** | **98.7%** |
| WO 的 178 段中无末尾标点 | **178** | 100% |

结论是两件事。

**第一，措辞层的句式是固定骨架。** 178 段全部服从 `[主语] [may / may be able to / could] [动作短语] without [宾语].`——`without` 短语是**句子的最后一块**。所以"是否点击／是否授权"这一维在措辞层**不是一个可以修饰的副词位，而是一个只能挂在句尾的补足语位**。这解释了第 6、7 章的全部零：副词位上没有任何词可放。

**第二，句尾位置正是第 15 章接缝丢段的机制。** Impact 里 **98.7%** 的段不带末尾标点，所以挂在句尾的宾语词（`consent`、`authentication`、`pairing`、`indicator`…）在与下一字段拼接时会**粘成 `consentThis`、`pairingAn`** 这类串，词界后视 `(?![\w-])` 失败而丢段；而 `without` 本身在句中，两侧都是空格，**永不受害**——这就是同一段落里"宾语丢 51 而 `without` 丢 0"的原因。样本（第 15 章）：`…ve user data without consent` ＋ `A path handlin…`、`…lay commands without pairing` ＋ `An access issu…`、`… screen without an indicator` ＋ `The issue was …`。

顺带一个可用检查：`without` 短语**从不带句末句号**这一事实，意味着任何按"句子"而不是"字段"切句的实现都会把这 178 段当成**未结束句**；本篇的 `SE` 分句函数（按 `(?<=[.!?])\s+(?=["A-Z])` 切）因此**不能**用来判定 `without` 是否处于句尾，只能用字段末尾判定。第 9 章的被动计数正是靠这个区别才没有多算。

# 6 零点击词汇刀：`click`／`tap`／`touch` 在 Impact 全部为零

附表 A 第一组"零点击词汇"16 项，**7 项并集全零、9 项非零但非零全在致谢侧**。逐词：

| 词形 | Impact | Desc | 致谢 | 三字段并 | 备注 |
|---|---:|---:|---:|---:|---|
| `click`（词界） | **0** | 0 | 1 | 1 | 唯一一次在致谢行的人名/机构描述里 |
| `clickjacking` | 1 | 2 | 0 | 3 | 界面欺骗术语，不是"点击"动作 |
| `tap` / `taps` | **0** | 0 | 2 | 2 | 致谢侧 2 段 |
| `touch`（词界） | 1 | 0 | 0 | 1 | **唯一命中是组件名 Touch Bar**，页 149035 |
| `swipe` | **0** | 0 | 0 | 0 | — |
| `press` | **0** | 0 | 0 | 0 | — |
| `hold`（词界） | **0** | 0 | 0 | 0 | — |
| `drag` | **0** | 0 | 11 | 11 | 致谢侧 11 段 |
| `double-click` | **0** | 0 | 0 | 0 | — |
| `one-click` | **0** | 0 | 0 | 0 | — |
| `zero-click` | **0** | 0 | 0 | 0 | — |
| `zero click`（空格形） | **0** | 0 | 0 | 0 | — |
| `zero`（词界） | **0** | **0** | 653 | 653 | 页 163、名 37，全部机构名 |
| `Zero Day Initiative` | **0** | 0 | 530 | 530 | 页 150、名 27 |
| `Google Project Zero` | **0** | 0 | 113 | 113 | 页 80、名 11 |
| `ZeroClicks` | **0** | 0 | 16 | 16 | 页 7、名 4 |

三条硬结论。

**其一，Impact 侧与"点击"有关的词只有两个，且都不是动作。** `clickjacking` 的 1 段是一种界面欺骗后果的名称；`touch` 的 1 段是组件名 **Touch Bar**（`An app may be able to capture Touch Bar content without authorization`，页 149035）——它同时是 W9 `without authorization` 的**全库唯一 1 段**。也就是说：**整个措辞层里唯一一段"无授权"，其主语组件是一个触控条。** 这两个非零都不构成"点击动作"的证据。

**其二，`zero` 的 653 段全部在致谢侧，且是不分大小写的子串效应。** `zero` 词界在致谢侧 **653 段 / 163 页 / 37 名**；把词界去掉改测子串得 **698 段 / 167 页 / 45 名**，差 **45 段**来自 `zero` 后紧跟字母的连写形（`ZeroClicks.ai Lab` 16 段、`ZeroPointer Lab` 10 段等，45 段未逐段归因）。按**出现次数**统计则是 `Trend Micro Zero Day Initiative` **506**、`Google Project Zero` **137**、`ZeroClicks.ai Lab` **16**、`ZeroPointer Lab` **10**——**注意 506／137 是次数、530／113 是段数，两种单位不能混用**。

**其三，Impact 与 Description 两侧连"zero"这个字母串都不出现。** 实测 Impact 含 `zero`（不要求词界、不区分大小写）**0 段**、Description 含 `zero` **0 段**。所以"零点击"这一概念在 Apple 的后果字段与修复字段里**没有任何词形承载**，无论正写反写。

这决定了本篇的方法论：**"无需点击"在措辞层不可直接测，只能测"无某前提"**，也就是第 1 章的 WO。这个替代是**有代价的**——`without user consent` 说的是"没有用户同意"，它与"用户没有点击"在语义上不等价（同意可以通过设置预先给出，点击只是同意的一种表现）。第 22 章把这条列为第一篇误读。

# 7 交互名词刀：18 项里 15 项全零，`a user` 主语零交

附表 A 第二组"交互动作名词"18 项，**15 项并集全零**：

`interact`、`interaction`、`user interaction`、`user action`、`user input`、`user gesture`、`user initiation`、`user approval`、`user-initiated`、`requires user`、`the user may`、`when the user`、`after the user`、`by the user`、`manual`／`manually`——**Impact／Description／致谢三侧全为 0**。

非零的 3 项恰好是"用户"以主语身份出现的三种合法写法，但它们与 WO 的交集**全是 0**：

| 判据 | Impact 段 | 页 | 名 | ∩WO | ∩R |
|---|---:|---:|---:|---:|---:|
| `a user may` | 39 | 33 | 15 | **0** | **0** |
| `if the user` | 6 | 6 | 1 | **0** | — |
| `the user has` | 6 | 6 | 1 | **0** | — |
| 句首 `a user` | 61 | — | — | **0** | — |
| `the user`（任意位置） | 132 | — | — | 16 | 0 |
| `a user`（任意位置） | 231 | — | — | **0** | 29 |
| `users`（复数） | 24 | — | — | 3 | 10 |

注意最后三行与前面的差别：**`the user` 出现在 132 段 Impact 里，其中 16 段属 WO**，但这 16 段中的 `the user` **不是主语**——它们是 `without prompting the user`（16 段）里的**宾语**。同理 `a user` 的 231 段与 WO 零交，而 WO 里唯一含 `a … user` 的是 `An unauthenticated user on the same network …`（7 段，`user` 作主语头）。**"用户"在措辞层可以出现，但一旦它出现在 `without` 之后，它就是被省略的对象而不是施动者。**

反向电池（第 21 章的缺口清单从这里取料）：`opt in` **0**、`toggle` **0**、`enter` **0**、`type` **0**、`select` **0**、`choose` **0**、`scan` **0**、`deny` **0**、`need` **0**、`switch` **2**（∩WO 0）、`until` Impact **0**（致谢 11）、`once` **0**、`first` **0**。条件从句侧：`if-从句` **6**、`when-从句` **34**、`after-从句` **22**、`before-从句` **45**、`once-从句` **0**、`unless` **0**、`provided` **0**，**八项 ∩WO 全为 0**。

于是本篇的负面结果可以写成一句话：**措辞层没有任何一种句式在同一个句子里既写"用户做了什么"又写"没做什么"。** 用户要么被写成省略的对象（70 段 `without user X`），要么完全不在场。

# 8 主语刀：19 个主语串闭合，"人"只有 27 段

对 WO 每段取"句首到第一个情态动词之前"为主语串，得 **19 个 distinct 串**（区分大小写；把 `A shortcut` 25 与 `A Shortcut` 3 合并则 **18 个**），逐串闭合：

| 主语串 | 段 | 类别 |
|---|---:|---|
| `An app` | 49 | 程序 |
| `A shortcut` | 25 | 程序 |
| `Photos in the Hidden Photos Album` | 16 | 数据（被动） |
| `Private Browsing tabs` | 15 | 数据（被动） |
| `An attacker` | 14 | 人 |
| `A malicious app` | 11 | 程序 |
| `A website` | 10 | 程序 |
| `An unauthenticated user on the same network as a signed-in Mac` | 7 | 人 |
| `A malicious shortcut` | 5 | 程序 |
| `An attacker on the network` | 5 | 人 |
| `A maliciously crafted email` | 4 | 其余 |
| `A Shortcut` | 3 | 程序 |
| `An encrypted volume` | 3 | 数据（被动） |
| `Apps that appear to use App Sandbox` | 3 | 程序 |
| `Third-party shortcuts` | 3 | 程序 |
| `A malicious app with root privileges` | 2 | 程序 |
| `A logic issue existed where a process` | 1 | 程序 |
| `An attacker with physical access to an unlocked device` | 1 | 人 |
| `Shake-to-undo` | 1 | 程序 |
| **合计** | **178** | |

三分：**人 27 段 / 26 页 / 7 名**（其中 ∩R 13）、**程序或数据 147 段 / 87 页 / 29 名**（∩R 14）、**其余 4 段**（全是 `A maliciously crafted email`，Mail／FaceTime）。27 ＋ 147 ＋ 4 = **178** ✓。

三个零交是本章的硬结论：

- **`remote attacker` 的 194 段与 WO 零交（∩WO = 0，同时 ∩G = 0）**。措辞层写"远程攻击者"时从不写"无某前提"；写"无某前提"时从不写"远程攻击者"。这两拨段落是**完全分开的两套句子**。
- **句首 `a user` 的 61 段与 WO 零交**；`a user may` 的 39 段、`if the user` 的 6 段、`the user has` 的 6 段同样 **∩WO = 0**。
- **`An attacker` 系列合计 20 段**（14＋5＋1）是 WO 里"人"作主语的全部；其中 7 段的主语是 `An unauthenticated user …`，而这 7 段同时是 W10 `without pairing` 的全部（一句 AirPlay）。**"未认证用户"在措辞层里只与"未配对"同现，不与"未同意"同现。**

对照 #118 的主语刀：R 的 843 段里通道词位于 `may` **之前 834、之后 0**；本篇 WO 的 178 段里 `without` **之后 0 段有后续内容**。两把刀指向同一个句式：**Apple 的后果句是"主语＋情态＋动作＋（末尾补足语）"的单层结构，修饰语一律不上前、状语一律不后延。**

# 9 语态刀：WO 的被动率约是全集的 16 倍

| 口径 | 全集 Impact | 页 | ∩WO | WO 内占比 |
|---|---:|---:|---:|---:|
| `may be`（其后非 `able`） | **88** | 63 | **34** | 19.1% |
| `may be` ＋ 过去分词（收紧） | **61** | 48 | **34** | 19.1% |
| `may be able to` | 4,722 | — | 123 | 69.1% |

全集被动率：88／7,434 = **1.18%**；WO 内：34／178 = **19.1%**；比值 **≈16 倍**。若采用收紧口径 61／7,434 = **0.82%**，比值 **≈23 倍**。两个口径的 ∩WO 都是 **34**，所以这个倍数不依赖过去分词表的选择——这是本篇少数"换口径不改结论"的测量。

34 段只来自三句，且**主语全是被访问的数据**：

- `Photos in the Hidden Photos Album may be viewed without authentication` — **16 段**
- `Private Browsing tabs may be accessed without authentication` — **15 段**
- `An encrypted volume may be accessed by a different user without prompting for the password` — **3 段**

16＋15＋3 = **34** ✓。三句合计 **34 段 = WO 的 19.1%**，且**全部落在 W7 `without authentication`（43 段）与 W5 `without prompting`（19 段）**里：16＋15 = 31 进 W7、3 进 W5，所以 **W7 的 43 段里 31 段是被动（72.1%）**，而 W1 的 47 段里被动 **0 段**。**被动形只与"认证"这个宾语绑定，不与"同意"绑定**——这是本篇最干净的一格交叉。

第三句值得单列：`by a different user` 是**全库唯一**在 `without` 句里让 `user` 以**施动者**身份出现的段落（3 段，DiskArbitration，加密卷被另一个用户访问而不提示密码）。即便如此，句子主语仍是 `An encrypted volume`（数据）。

反面对照：**WO 的 144 段是主动形**（123 段 `may be able to` ＋ 21 段其它主动），其中 `An app` 49、`A shortcut` 28。所以"无某前提"这一陈述的默认发出者是**一个程序**，而不是一个人——这与第 6、7 章的全部零构成同一条证据链。

# 10 情态闭合分区：123 ＋ 48 ＋ 7 = 178

对 WO 的 178 段按"第一个情态结构"归档：

| 情态 | 段 | 说明 |
|---|---:|---|
| `may be able to` | **123** | 全库该形 4,722 段，WO 占其 2.6% |
| 裸 `may` | **48** | 其中被动 `may be`＋非 able **34**、非 `be` 形主动 **14** |
| `could` | **7** | 全部是 AirPlay 同一句 |
| 无情态动词 | **0** | — |
| **合计** | **178** | |

123 ＋ 48 ＋ 7 ＋ 0 = **178** ✓。三点：

**其一，`could` 的 7 段是同一句话的七个副本。** `An unauthenticated user on the same network as a signed-in Mac could send it AirPlay commands without pairing`，页 **122371、122372、122373、122374、122375、122377、122378**，组件名全是 AirPlay。这是 WO 里**唯一**不用 `may` 的段落，也是唯一 `could` 与 `without` 同段的组合。措辞层把"未配对"写成 `could`（可能性更弱的一种），而把"未同意"一律写成 `may`——**情态选择与宾语类型绑定**。

**其二，`can` 与 `must` 不参与。** `can` 的 Impact **16** 段与 WO **零交**（#118 同样报 `can` ∩R = 0），`will`／`must`／`shall`／`would` 的 Impact **各 0 段**。

**其三，裸 `may` 的 48 段内部比例。** 34 段被动 ＋ 14 段主动；这 14 段主动里最大的一块是 `A shortcut may output sensitive user data without consent`（**7 段**），其余包括 `Third-party shortcuts may use a legacy action from Automator to send events to apps without user consent`（3）、`A Shortcut may run with admin privileges without authentication`（3）、`Shake-to-undo may allow a deleted photo to be re-surfaced without authentication`（1）。**`may` ＋ 裸动词（不用 `be able to`）在措辞层是少数派，且集中在 Shortcuts 与自动化相关条目。**

# 11 副词层：`actively`、`silently`、`notif-` 与四个零

附表 A 第四组"无人值守副词"20 项里 **13 项并集全零**，第五组"可见性与提示面"18 项里 **9 项全零**。非零项逐词：

| 词形 | Impact 段 | ∩WO | ∩R | 组件／句子 Top |
|---|---:|---:|---:|---|
| `unexpected` | 2,053 | **0** | 175 | 192 页 118 名，全是"unexpected X"后果短语 |
| `actively` | 19 | **0** | 1 | CoreMedia 8、JavaScriptCore 5、WebKit 5、Bluetooth 1 |
| `notif-` 词族 | 32 | **0** | 0 | Accessibility 7、Share Sheet 7、Notification Services 5 |
| `before` | 45 | **0** | 0 | WebKit 24、CoreMedia 8、dyld 5 |
| `prompt` 词族 | 21 | **19** | 0 | Shortcuts 16、DiskArbitration 3 |
| `unintentionally` | 14 | **0** | — | curl 8、FaceTime 5、Authentication Services 1 |
| `requires` 词族 | 13 | **0** | 12 | Shortcuts 12、BiometricKit 1 |
| `enabl-` 词族 | 20 | **3** | 4 | Safari 6、Storage Management 3、Voice Control 3 |
| `silently` | 7 | **0** | 6 | WebKit Storage 6、Find My 1 |
| `after` | 22 | **0** | 3 | Kernel 7、Authentication Services 4、Passkeys 4 |
| `active`（词界） | 1 | **0** | 0 | 2 段三字段并集、含 Desc 1 段 |
| `silence` 词族 | 1 | **0** | 0 | FaceTime 1（`…audio being silenced`） |

四个值得单独点名的结果。

**`actively` 的 19 段不是"主动操作"。** 逐句核对：18 段是 `Apple is aware of a report that this issue may have been actively exploited …`（Intel-based Mac **10** 段、iOS before iOS 17.2 **8** 段），1 段是 Bluetooth 页 120907 的 `…not yet paired and actively seeking pair requests`。**"actively"在措辞层属于"在野利用披露"，与"用户主动点击"毫无关系**；它与 WO **零交**、与在野句族重合 18。另外，`known to be exploited`／`being actively exploited` 这两个常见的在野套语实测 Impact **0 段**——Apple 用的是 `may have been actively exploited`。

**`silently` 的 7 段与 `without` 零交。** 组件是 WebKit Storage **6**、Find My **1**。也就是说：**措辞层里"静默"和"未经同意"是两拨句子**，尽管语义高度重叠。`silent` 词界 **0**、`silence` 词族 **1**（FaceTime 的 `muting … may not result in audio being silenced`，属 G 集的 `muting`）。

**`notif-` 的 32 段与 `without` 零交。** 这是"用户能不能看见"的最接近代理词，但它从不与否定前提同段——**"无提示地做了某事"这个句式在措辞层不存在**。

**`prompt` 词族是唯一的例外，且几乎是同义反复。** Impact 21 段里 **19 段 ∈ WO**（＝W5 `without prompting` 的 19 段），另 2 段是 `prompt` 的非 `without` 用法。**`prompting` 一词全部出现在否定式里**，这是本篇最紧的一格：措辞层里"提示"只有"没有提示"这一种写法。

`enabl-` 词族的 ∩WO **3** 段是 `An app may be able to enable iCloud storage features without user consent`；`requires` 词族 13 段（Shortcuts 12、BiometricKit 1）与 WO **零交**，与 R 交 12——**"需要某前提"这一正面句式在措辞层几乎只与远程通道同段，从不与 `without` 同段**。

# 12 可见性刀：`indicator` 的 3 段是唯一一句"用户看得见吗"

"无需点击"若要成立，通常还隐含"用户看不出来"。措辞层测这一维的词只有四个，且全是零或近零：

| 判据 | Impact 段 | 页 | ∩WO | 备注 |
|---|---:|---:|---:|---|
| `indicator` | 8 | 6 | **3** | Control Center 4、Accessibility 2、Media Session 1、其余 1 |
| `dialog` | 3 | 3 | **0** | — |
| `alert`（词界） | **0** | 0 | 0 | 三侧全零 |
| `banner` | **0** | 0 | 0 | 三侧全零 |
| `sheet`（词界） | **0** | 0 | 0 | 仅致谢 1 段 |
| `visible to the user` | **0** | 0 | 0 | 全零 |
| `shown to the user` | **0** | 0 | 0 | 全零 |
| `may not be shown` | **0** | 0 | 0 | 全零 |

三条：

**其一，全库唯一写"没有指示器"的否定式是 3 段 Control Center**：`An app may be able to record the screen without an indicator`（页 121238、121250、121839）。这 3 段是 W14 的全部，也是措辞层里**最接近"用户看不见"**的一句——它不说看不见，只说**没有那个指示器图标**。

**其二，`indicator` 的 8 段里 5 段不属 WO**（∩WO = 3）。另 5 段写的是"指示器出现了／未清除"这类状态，不是"没有指示器"。所以**"指示器"在措辞层主要是一个 UI 组件名，不是一个可见性声明**。

**其三，`alert`／`banner`／`sheet`／`dialog` 的四个零是独立的四个零。** 不能把它们并成一格统计——`dialog` 有 3 段而 `alert` 有 0 段，是两件不同的事。中文层同样：**`弹窗` 0 次、`红点` 0 次、`横幅` 1 次、`指示器` 1 次、`对话框` 3 次／2 页、`通知` 50 次／31 页**（附表 E 第六组）。中英两侧在"可见性提示面"上是**同时稀疏**的。

# 13 组件层：37 名里有 8 名的 Impact 全部写 `without`（附表 D）

WO 覆盖 **37 个组件名**，占全树 472 名的 **7.8%**；对照 R 覆盖 72 名（15.3%）、G 覆盖 98 名（20.8%）。附表 D 逐名给出 **WO 内段数、∩R、∩G、该组件 Impact 全集段数、WO 占比、该组件最高频 `without` 短语**。三点：

**其一，8 名的 WO 占比 = 100%（合计 41 段 = WO 的 23.0%）**：App Support 7、CoreCrypto 7、CoreMotion 7、iCloud Document Sharing 6、CoreBluetooth - LE 4、Parental Controls 4、Screen Sharing 3、Storage Management 3。这 8 名在 Impact 侧**只写过"无某前提"这一种后果**——它们的每一条后果句都带 `without`。另有两名接近满格：**Watch App 3／4 段 = 75.0%**、**Safari Private Browsing 11／16 = 68.8%**。

**其二，占比高不等于段数多。** Shortcuts 的 WO 段最多（**36 段 = WO 的 20.2%**），但占它自身 Impact 的 **27.9%**；WebKit 12 段只占它自身 Impact 的 **1.3%**（910 段）。**"这个组件常被写成无前提"与"这个组件的后果句几乎都是无前提"是两个不同的问题**，附表 D 用两个列分别回答。

**其三，∩G 整列为 0。** 37 个组件名里，**没有任何一段既是句首动名词（G）又含 `without`**（第 16 章的集级零交在组件级同样成立）。G 的 Top 组件 WebKit 677、ImageIO 253、CoreMedia 95 与 WO 的 Top 组件 Shortcuts 36、WebKit 12、Safari Private Browsing 11 **交集很窄**：只有 WebKit（12 段进 WO，677 段进 G，且这 12 段与那 677 段**不重叠**）。

组件层的分布还给出一个反直觉的事实：**37 名中只有 8 名在 R 里有段，29 名完全不写通道**——即"无某前提"的绝大多数组件在措辞层**同时不声明远程可达性**。R∩WO 的 27 段来自这 8 名（WebKit 7、AirPlay 7、Screen Sharing 3、Safari 3、NetAuth 3、Screen Sharing Server 2、Wi-Fi 1、NetworkExtension 1，合计 27）。

# 14 零形电池：90 项判据里 45 项全零（附表 A）

附表 A 把本篇全部判据按五组列出，每项报 **Impact 段／Desc 段／致谢段／三字段并集／页／名／∩WO／∩R**，共 **90 项**：**并集全零 45 项、非零 45 项**。逐组：

| 组 | 项数 | 并集全零 | Impact 零但致谢非零 | 组内最大非零项 |
|---|---:|---:|---:|---|
| 零点击词汇 | 16 | 7 | 7（`zero` 653、`ZDI` 530、`GPZ` 113、`ZeroClicks` 16、`click` 1、`tap` 2、`drag` 11） | `zero` 词界 致谢 653 |
| 交互动作名词 | 18 | **15** | 0 | `a user may` Impact 39 |
| 授权缺失宾语 | 18 | 1 | 0 | `without` 178／`authentication` 93 |
| 无人值守副词 | 20 | 13 | 2（`automatically` 3、`directly` 1） | `actively` 19 |
| 可见性与提示面 | 18 | 9 | 2（`sheet` 1、`until` 11） | `unexpected` 2,053 |
| **合计** | **90** | **45** | **11** | |

**45 项全零**是本篇的负面结果主体，值得逐条点名（并集全零＝三侧都是 0）：

- **点击侧（7）**：`zero-click`、`zero click`、`one-click`、`swipe`、`press`、`hold`、`double-click`
- **交互侧（15）**：`interact`、`interaction`、`user interaction`、`user action`、`user input`、`user gesture`、`user initiation`、`user approval`、`user-initiated`、`requires user`、`the user may`、`when the user`、`after the user`、`by the user`、`manual`／`manually`
- **授权侧（1）**：`not without`
- **无人值守侧（13）**：`automatic`、`auto`、`silent`、`inadvertently`、`deliberately`、`intentionally`、`knowingly`、`unknowingly`、`without knowing`、`unattended`、`passively`、`merely`、`simply`
- **可见性侧（9）**：`alert`、`banner`、`visible to the user`、`shown to the user`、`may not be shown`、`opt in`、`toggle`、`once`、`first`

三个"看起来该非零、实际是零"的词值得再次点名：`automatically` 的 Impact **0**（致谢 3）、`until` 的 Impact **0**（致谢 11）、`unauthorized` 的 Impact **21 段但 ∩WO = 0**。最后一个是全篇最锋利的一格：**"未授权"这个形容词在措辞层有 21 段，但没有一段是写成 `without authorization` 的**——它总与别的动词绑定（`gain unauthorized access to Bluetooth` 之类），而"无某前提"这一维只由 `without` ＋ **名词**承载。

对照 #118 的电池：那边 120 项里 **36 项全零**（30%），这边 90 项里 **45 项全零**（50%）。**选题越靠近"人的动作"，措辞层的零密度越高**——这是两篇合起来才能说的一句话，单独任一篇都不足以支撑。

# 15 接缝刀：`without` 丢 0 段，它的宾语丢 3～116 段

同一批判据改按"三字段拼接后匹配"（`imp . des . ack` 直接串接，再匹配一次）统计，与"三字段分别匹配取并集"对比：

| 判据 | 三字段并集 | 拼接口径 | 丢段 | Impact 侧 |
|---|---:|---:|---:|---:|
| `without` | **189** | **189** | **0** | 178 |
| `user` | 1,669 | 1,553 | **116** | 1,627 |
| `consent` | 76 | 25 | **51** | 54 |
| `authentication` | 144 | 93 | **51** | 93 |
| `permission` | 55 | 21 | **34** | 55 |
| `restrictions` | 666 | 636 | **30** | 48 |
| `pairing` | 9 | 1 | **8** | 9 |
| `authorization` | 198 | 194 | **4** | 11 |
| `indicator` | 4 | 1 | **3** | 4 |
| `prompting` | 19 | 19 | **0** | 19 |
| `entitlement` | 115 | 115 | **0** | 7 |
| `credential` | 0 | 0 | **0** | 29 |
| 四个交互名词（`interact`／`interaction`／`user interaction`／`zero-click`） | 0 | 0 | **0** | 0 |

机制在第 5 章已经测出：**7,334／7,434 段 Impact 没有末尾标点（98.7%）**，而 `without` 短语 100% 压在句尾，所以宾语词的右侧接缝是 `consent` ＋ `A path handling…` 这类粘连串，词界后视 `(?![\w-])` 直接失败。样本（Impact 末 28 字 ∶ 下一字段首 14 字）：

- `…ve user data without consent` ∶ `A path handlin`
- `…essed without authentication` ∶ `This issue was`
- `…ones without user permission` ∶ `An access issu`
- `…lay commands without pairing` ∶ `An access issu`
- `… screen without an indicator` ∶ `The issue was `

四个必须写清的红线。

**其一，这是脚本口径现象，不是语料现象。** 语料里 `consent` 出现了 76 段，拼接口径只认出 25 段；丢掉的 51 段每一段都确实在写"无同意"。任何把拼接口径当"更严格统计"的写法都会**系统性低估授权词**。

**其二，`credential` 一行是反向陷阱。** 并集 **0**、拼接 **0**、Impact **29**——因为该词在 Impact 侧的真实形是复数 `credentials`，本篇测的 `credential` 单数词界在两侧的合法命中都是 0；29 那一列是**把 `credential` 当词族前缀**测出来的（`valid credentials` 5 段在其中）。**同一行的两列不能相加，也不能互相解释**（附表 A 里两处分别登记）。

**其三，`without` 自身的丢 0 不是"运气"，而是句式。** 如果哪天出现 `without user consent, the app …`（即 `without` 短语被后延），第 5 章的 178∶0 会破，接缝刀就会咬 `without`。**这个 0 应该作为回归断言长期钉住**（第 20 章）。

**其四，`restrictions` 的丢 30 与 Impact 48 不构成矛盾。** 并集 666 段里绝大多数是 Description 侧的 `content restrictions`／`web content restrictions`；Impact 侧只有 48 段。拼接口径丢 30 段丢的是**字段末尾的 restrictions**，与 Impact 的 48 是两个维度。

# 16 与前几篇的集合接口

三集成员级交集（全部按**段号集合**求交，不按计数推断）：

| 交 | 段 | 占左集 | 占右集 |
|---|---:|---:|---:|
| **WO ∩ R** | **27** | 15.2%（分母 178） | 3.2%（分母 843） |
| **WO ∩ G** | **0** | 0.0% | 0.0%（分母 2,179） |
| **G ∩ R** | **108** | 5.0% | 12.8% |
| WO ∩ G ∩ R | **0** | — | — |

**WO ∩ R 的 27 段闭合到通道词组合**：`website` **10** ＋ `network`（不含 AirPlay）**9** ＋ `network`＋`AirPlay` **7** ＋ `Wi-Fi` **1** = **27** ✓。含 `network` 的合计 16 段（9＋7）。`SMB`／`AFP`／`NTLM` 与 WO **零交**。

**一个词界陷阱在这格上：** `websites`（复数）在 Impact 有 **13 段**、其中 **3 段属 WO**、**0 段属 R**——因为 R 的判据是 `webs(?:ite|ite)` 加词界，复数形被排除。那 3 段是 `An app may be able to track users across apps and websites without permission`（Watch App，页 149034、149037、149041）。**这三段是"跨网站追踪"，语义上属于网页通道，但按 R 的定义它们不在 R 里。** 若把复数收进 R，WO∩R 会从 27 变 30——**任何一侧的词界改动都必须同步重算交集**，本篇选择保持 #118 的原定义不变并把这个差写在这里。

**WO ∩ G = 0 是本篇最硬的一格。** 两个独立的零：(a) 集级求交 0 段；(b) G 的 2,179 段里，Impact **非首句**含 `without` 的句子数 **0**。也就是说：**"Visiting / Processing / Mounting … 可能导致 X"这一整套触发描述，与"某程序可以做 Y 而无 Z"这一整套否定描述，在措辞层是两个不相交的宇宙**（附表 C 的 21 行 ∩WO 列全为 0，合计行也是 0）。

与前面各篇的接口（逐集实测）：

- `attacker` 词族 **890** ∩WO **20**、∩G 12；`remote attacker` **194** ∩WO **0**、∩G **0**。
- `shortcut` 词族 **101** ∩WO **43**（占 WO 24.2%）——WO 有 **近四分之一**属于 Shortcuts 一个组件。
- `an app` 句首 **3,156** ∩WO **49**；`malicious` **∩WO 18**；`crafted` 词界 **∩WO 4**（F 集 1,769 段只有 4 段带 `without`，就是 `A maliciously crafted email may be able to initiate FaceTime calls without user authorization` 的 4 段副本）。
- `data` 的 Impact **∩WO 32**、`code` 的 Impact **∩WO 0**、`arbitrary` **∩WO 7**、`compromis-` **∩WO 0**、`unlock` **∩WO 0**、`pairing` **∩WO 7**、`not/never` **∩WO 0**。
- **`unauthorized` 的 21 段 ∩WO = 0，而 `unauthenticated` 的 9 段 ∩WO = 7、∩R = 8。** 同为"un-＋认证"形容词，一个完全不进否定式、一个几乎全进——**形容词形的"未认证"只用来描写主语（攻击者身份），名词形的"认证"才用来描写被省略的前提。**

最后一条与 #117（维持集 57 段）、#114（手段集 108 段）没有交叠值得报出：WO ∩ `exploit` 词族、WO ∩ 维持集本篇**未测**（列入第 21 章缺口）。

# 17 页、版式域与切块自检

**页分布。** WO 覆盖 **97 页**（全树 257 页 = 37.7%；R 覆盖 178 页 = 69.3%）。按每页段数分箱：

| 每页 WO 段数 | 页数 | WO 段数 |
|---|---:|---:|
| 1 | **59** | 59 |
| 2 | **17** | 34 |
| ≥3 | **21** | **85** |
| 合计 | **97** | **178** ✓ |

59 ＋ 34 ＋ 85 = 178 ✓；≥3 段的 21 页贡献 **47.8%**。Top10 页合计 **52 段 = 29.2%**：122373（**8**）、121238（6）、121250（6）、121839（5）、122374（5）、122375（5）、149035（5）、120893（4）、120895（4）、149034（4）。

**最大簇是连续五页 149034–149038**（iOS 17.x 的一批条目），实测：

| 页 | 该页全树段 | 其中 WO |
|---|---:|---:|
| 149034 | 209 | 4 |
| 149035 | 314 | 5 |
| 149036 | 118 | 2 |
| 149037 | 128 | 3 |
| 149038 | 165 | 1 |
| **合计** | **934** | **15**（＝WO 的 **8.4%**） |

这五页里的 15 段几乎不重复：`An app may be able to access motion data from headphones without user consent`（CoreMotion，4 页各 1）、`A malicious shortcut may be able to send messages without user confirmation`（Shortcuts，5 页各 1）、`An app may be able to track users across apps and websites without permission`（Watch App，2）、`… access local network devices …`、`… authenticate to Screen Sharing without valid credentials`、`… capture Touch Bar content without authorization`、`… view Wi-Fi passwords without authentication`。**同一次 iOS 更新里，多个组件被并行写成"无某前提"，但用的是六个不同的宾语**——这与"一个句子被复制 16 次"（第 18 章）是两种完全不同的聚集方式，本篇分开计数。

**版式域自检（域 A／域 B）。** 域 B 是那 11 份用粗体段起块的页面（页 id `118479`、`120303–120312` 中的 10 个、`120339`），全树 **91 段**。三集在域 B 的分布：**WO ∩ 域 B = 5**、**R ∩ 域 B = 5**、**G ∩ 域 B = 24**；对应域 A 分别 **173**、**838**、**2,155**。逐条闭合：173＋5 = 178 ✓、838＋5 = 843 ✓、2,155＋24 = 2,179 ✓。**WO 的 5 段域 B 全部来自 WebKit 的 `A malicious website may … without user consent` 形**，与 #118 报告的域 B 5 段（4 段 WebKit cross-origin ＋ 1 段 Bluetooth）**不是同一批段落**——两个 5 数值相同、成员不同，求交前必须逐段核对（这是本项目记忆里"得数相同 ≠ 同一组段落"的一次复现）。

**切块自检。** 本篇的 178 全部可复算，但依赖 #111 的四锚点收块规则。**若收块锚少写一个，段末文本会漏进下一段**，后果是：`without` 短语会被切成两段，第 5 章的 178∶0 会变成"部分命中"。所以第 20 章把 `句尾形 178 / 句中形 0` 与 `每段恰好 1 个 without` 同时列为断言——这两条只有切块正确才同时成立。

# 18 distinct 句：37 条句子撑起 178 段

按**含 `without` 的子句**去重（不是整段去重），WO 得 **37 条 distinct 句**，出现总次数 **178**，均值 **4.81**。频次阶梯：

| 频次 | 句数 | 段数 |
|---:|---:|---:|
| 16 | 2 | 32 |
| 15 | 1 | 15 |
| 10 | 1 | 10 |
| 7 | 5 | 35 |
| 6 | 1 | 6 |
| 5 | 4 | 20 |
| 4 | 6 | 24 |
| 3 | 8 | 24 |
| 2 | 3 | 6 |
| 1 | 6 | 6 |
| **合计** | **37** | **178** ✓ |

32＋15＋10＋35＋6＋20＋24＋24＋6＋6 = 178 ✓。频次 ≥7 的 **9 句**（2＋1＋1＋5）合计 **98 段 = WO 的 55.1%**；频次 =1 的 **6 句**只占 3.4%。

Top 9 句（按频次）：

1. 16× `A shortcut may be able to use sensitive data with certain actions without prompting the user`（Shortcuts）
2. 16× `Photos in the Hidden Photos Album may be viewed without authentication`（Photos／Photos Storage）
3. 15× `Private Browsing tabs may be accessed without authentication`（Safari Private Browsing）
4. 10× `A website may be able to access sensor information without user consent`（WebKit）
5. 7× `A malicious app may be able to run arbitrary shortcuts without user consent`（Shortcuts）
6. 7× `A shortcut may output sensitive user data without consent`（Shortcuts）
7. 7× `An app may be able to access motion data from headphones without user consent`（CoreMotion）
8. 7× `An attacker may be able to decrypt legacy RSA PKCS#1 v1.5 ciphertexts without having the private key`（CoreCrypto）
9. 7× `An unauthenticated user on the same network as a signed-in Mac could send it AirPlay commands without pairing`（AirPlay）

四条读法纪律。

**其一，"16 段"不等于"16 个漏洞"。** 同一句话在多份页面里重复出现（同一 CVE 族跨多个 iOS／macOS 版本条目），篇内一律按**段**计数并在页维度另计；第 17 章的 97 页与本章的 37 句是同一批 178 段的两个投影。

**其二，distinct 句列在附表 B 里可加且闭合到 37**，因为一句只归一个区；但"页"列与"名"列不可加（第 4 章）。

**其三，两句 16 次并列是巧合而非同一句。** 一句是 Shortcuts 的 `without prompting the user`，另一句是 Photos 的 `may be viewed without authentication`——前者是**裸 may 之外的 `may be able to` 缺位形**、后者是**被动形**，它们分别贡献了 W5 与 W7 的最大块。

**其四，第 8 句是全篇唯一写密码学前提的否定式**：`without having the private key`（CoreCrypto 7 段，该组件 Impact 全集 7 段 → **WO 占比 100%**）。它与"用户交互"无关，属于**密钥持有前提**，本篇把它计入 WO 是因为判据是词形而非语义；第 22 章第 4 条专门纠正由此产生的误读。

# 19 中文手册层（T1）的对应词（附表 E）

《Apple 平台安全保护》zh-CN 全量转储 **178 页可用**，附表 E 按六组 **46 项**报**次数／覆盖页／页占比**。要点：

**触屏动作组几乎全空。** `轻点` **13 次／10 页（5.6%）**、`点按` **1／1**、`点击` **1／1**；`长按`、`拖动`、`滑动`、`缩放`、`触控板`、`触摸` **全部 0 次**。而单字 `点` **689 次／178 页**、`触控` **877 次／178 页**——**这两个数是 `点`（如"信任点""接入点"）与 `触控 ID` 的词干贡献，不是触屏动作**。中文手册层的"轻点"是操作指引用词（出现在"如何操作"章节），与安全后果无关。

**授权动词组满页。** `验证` **433／178**、`认证` **380／93**、`授权` **353／178**、`确认` **250／178**、`允许` **152／82**、`批准` **49／25**、`提示` **45／32**、`同意` **26／19**、`许可` **14／12**、`询问` **5／4**。这十个词覆盖的页数与英文侧的授权名词**方向一致但口径不同**：中文侧"验证／认证"混用，英文侧 `authentication` 一个词 93 段（Impact）。**不能把中文 433 次与英文 93 段并列成"中英对应"**（第 22 章第 6 条）。

**无交互侧集中在"自动／后台"。** `自动` **898／178**、`后台` **234／178**、`无需` **64／48**、`未经` **21／19**、`不需要` **6／4**、`主动` **3／3**、`静默` **1／1**、`不知情` **1／1**、`手动操作` **1／1**、`口令` **1／1**。**中文侧"无需／未经"合计 85 次、覆盖 48＋19 页，是整篇里唯一真正对应 `without X` 的表达**——但它是手册的说明性用语，不是后果陈述。

**凭据侧压倒一切。** `密码` **2,801／178**、`配对` **701／178**、`凭证` **528／178**、`生物识别` **401／178**、`认证` 380、`解锁` **384／178**、`信任` **363／178**、`锁定` **306／178**、`令牌` **70／15**。**中文手册在"前提"上是厚描写的（近 6,000 次），在"省略前提"上是零描写的**（`静默` 1、`不知情` 1、`弹窗` 0、`红点` 0）。

**可见性侧同样薄。** `通知` **50／31**、`对话框` **3／2**、`横幅` **1／1**、`指示器` **1／1**、`弹窗` **0**、`红点` **0**。英文侧的 `indicator` 3 段否定式在中文侧对应 `指示器` **1 次**——**两侧同时稀疏，不构成互证**。

# 20 复算步骤与断言

**数据面**：`_tmp/adv2/<id>.html`（258 份，排除 `122085`）＋ `_tmp/sec*.txt`（179 份，排除 `sec2727afbbc`）；加载器 `_tmp/tl111load.pl` 的 `load_blocks()` 返回 `{imp, des, ack, nm, id}`。测量脚本：`_tmp/tl119p4.pl`～`p9.pl`、`_tmp/tl119fin.pl`～`fin5.pl`；附表生成 `_tmp/tl119tab.pl`；拼接 `_tmp/tl119splice.pl`；标题 `_tmp/tl119head.pl`。

**复算顺序（每一步都必须先看断言，再看结论）**：

1. **三集基数断言**：`WO = 178`、`R = 843`、`G = 2179`，三个条件**任一不满足即中止**。这三个数在 `p6/p7/p8/fin/fin2/fin4/fin5` 里各自重复钉住。
2. **字段口径断言**：Impact `without` = 178、Description = **0**、致谢 = 11、三字段并 = **189**、拼接 = **189**。
3. **分区闭合断言**：W1…W15 十五类互斥相加 = 178，`未归入 = 0`；宾语短语（剥冠词）distinct = **18** 且相加 = 178；十五类的 ∩R 列相加 = **27**；distinct 句列相加 = **37**。
4. **句式断言**：`without` 短语延伸至 Impact 末尾 = **178**、句中 = **0**；每段恰好 1 个 `without` = **178**、≥2 = **0**。这两条同时失败意味着切块规则被改动。
5. **情态闭合断言**：123 ＋ 48 ＋ 7 ＋ 0 = 178；其中裸 `may` 的 48 再分 34 ＋ 14 = 48。
6. **交集断言**：WO∩R = **27**（组合闭合 10＋9＋7＋1）、WO∩G = **0**、G∩R = **108**、WO∩G∩R = **0**。求交一律用成员集合，不得由 |WO|、|R| 与"占比"反推。
7. **页与域断言**：distinct 页 = 97；1／2／≥3 段页 59／17／21 → 段数 59＋34＋85 = 178；域 A 173 ＋ 域 B 5 = 178。
8. **中文层断言**：T1 可用页 = **178**；`长按`／`滑动`／`缩放`／`触控板`／`触摸`／`弹窗`／`红点` 七项 = **0**。

**三个已知口径陷阱**（复算时最容易撞）：

- **`/i` 缺失。** `IF/DF/AF` 三个匹配器必须带 `/i`。R 的通道词族是一个交替串，`Wi${H}Fi`、`Bluetooth`、`AirPlay`、`USB` 的实际大小写在语料里不稳定；漏掉 `/i` 时 R 会算成 **814** 而不是 843，进而把 WO∩R 从 27 压到更小的数。**这是本项目已经踩过的一次坑。**
- **`keys` 吞参数。** Perl 里 `printf $fmt, $g, keys %h, PG(\%h), …` 的裸 `keys %h` 会**吞掉后面的整个实参表**，产生 "Redundant argument in printf" 且列全部错位。必须写 `(scalar keys %h)`。
- **段落集合的构造方式。** 建集合必须 `map { $_=>1 } grep {…} keys %B`；写成 `my %h = grep {…} keys %B` 会把值也赋进去、集合规模翻倍。

# 21 缺口清单

**测不到的（词形不存在，不是没测）**：`click` 系全部触屏动词（Impact 0）、`interact`／`interaction` 及 8 个 `user X` 名词短语（三侧全零）、`manual`／`manually`、`unattended`、`passively`、`knowingly`／`unknowingly`、`opt in`、`toggle`、`alert`、`banner`、`sheet`（Impact）、`visible/shown to the user`、`unless`、`provided`、`once-从句`。这一批零**本身就是结果**，列在这里是为了防止后续篇目把它们误当"还没测"。

**测到了但没归因的**：

1. `zero` 词界 653 段与子串 698 段相差 **45 段**，只确认其中 `ZeroClicks` 16、`ZeroPointer` 10，其余 19 段未逐段列出。
2. `unauthorized` 的 21 段与 `unauthenticated` 的 9 段为何一前一后进入不同句式，本篇只报了交集数字，没有做句式分类归因。
3. `restrictions` 并集 666 段里 Description 侧的具体分布（`content restrictions`／`web content restrictions` 两类各多少）未拆。
4. `credential`（词族 29 段）与 `valid credentials`（5 段）之外的那 24 段的字段位置未核。

**没做的**：

- **WO ∩ `exploit` 词族（#114 全集 108 段）与 WO ∩ 维持集（#117 的 57 段）** 本篇未求交，只报了"未测"。
- **致谢侧 11 段 `without` 的机构名归因**未做（第 3 章只声明它们被排除）。
- **同一 CVE 跨页句重复的网络结构**未做——第 18 章的频次阶梯只给计数，不给"哪些页共享同一句"的表。
- **中文手册层"无需／未经"85 次所在章节的位置**未定位。

**口径未定论的**：W14 的判据包含 `without user indicator` 这一 0 次形（第 4 章），本篇选择保留；若删去，W14 = 3 段不变、判据字符串变短，**不影响 178 闭合**。`credential` 单数与词族两列并存（第 15 章），本篇不合并。

# 22 常见误读纠正

| 误读 | 为什么错 | 正确写法 |
|---|---|---|
| "178 段证明了 178 个无需点击的攻击" | 178 是**措辞层 `without` 在 Impact 出现的段数**，同一句话被复制到多个版本条目（Top 句 16 次、频次 ≥7 的 9 句占 55.1%） | "Impact 侧含 `without` 的段共 178 段，去重后仅 **37 条 distinct 句**，覆盖 **97 页 / 37 个组件名**" |
| "措辞层支持零点击结论" | `zero-click`、`zero click`、`click` 在 Impact 全部 **0 段**；`zero` 的 653 段全在**致谢侧机构名**里 | "措辞层**没有**点击／交互的词形，'无需点击'这一维不可正面测量，只能用 `without` 否定式做代理" |
| "`without user consent` 47 段说明用户没被提示" | `consent`（同意）与 `prompting`（提示）是两批不同段落：W1 47 段、W5 19 段，二者 ∩R、组件名都不同 | 分列："同意侧 47＋7 = 54 段"与"提示侧 19 段"，并注明 `prompt` 词族 Impact 21 段里 19 段就是 `without prompting` |
| "`without having the private key` 是无需交互的证据" | 这 7 段（CoreCrypto）写的是**密钥持有前提**，与用户动作无关；它进 WO 只是因为判据是词形 | 在宾语分区里单列 W12（11 段，含 `having app management permission` 4），说明其语义属"密钥／权限位"而非"人机交互" |
| "WO∩R = 27 说明多数无前提条目是远程的" | 27 占 WO 只有 **15.2%**、占 R 只有 **3.2%**，两侧都是少数；且 `remote attacker` 的 194 段与 WO **零交** | "无前提措辞与远程通道**基本正交**，重合的 27 段里 16 段靠 `network` 词形（含 AirPlay 7 段）、10 段靠 `website`、1 段靠 `Wi-Fi`" |
| "中文手册 `验证` 433 次对应英文 `authentication` 93 段" | 中文"验证／认证／校验"混用且 433 次含非安全义；433 是**次数**、93 是**段数**，页数分别是 178 与 33 | 只并比页数覆盖："中文 `验证` 覆盖 178 页（100%）、英文 `without authentication` 覆盖 33 页（12.8%）"，不并次数与段数 |
| "`unauthorized` 21 段也是 `without` 家族" | `unauthorized` 的 21 段 **∩WO = 0**，它从不与 `without` 同段；同族的 `unauthenticated` 才是 9 段 ∩WO 7 | "形容词形 `un-` 描写主语，名词形 `without X` 描写被省略的前提，两个集合互斥" |
| "含人主语 27 段 ＝ WO∩R 27 段" | 两个 **27** 口径无关：前者是主语头含 `user`/`attacker` 的 WO 段（∩R 只有 13），后者是通道词交集（其中 4 段主语是 `A website`） | 分别标注分母与判据；若需合并，报 \|人∩WO∩R\| = 13 |
| "`websites` 复数也算 website" | R 的判据带词界，复数被排除；复数 13 段里 **3 段属 WO、0 段属 R** | 报告两处计数并声明"若放宽词界则 WO∩R 由 27 变 30，本篇保持 #118 定义" |
| "域 B 的 WO 5 段与 #118 的 R 域 B 5 段是同一批" | 数值相同、成员不同（前者是 WebKit `without user consent` 形，后者是 cross-origin 形＋1 段 Bluetooth） | 得数相同不等于同一组段落，必须用成员集合求交后再陈述 |

# 23 结论

**一、这一维在措辞层没有正面词。** 表示"点击"的 10 个词形、表示"交互"的 15 个词形、表示"静默／无人值守"的 13 个词形、表示"可见性提示"的 9 个词形，在 Apple 安全响应顾问页的三字段里**并集全零**（附表 A 的 45 项）。Impact 侧唯一与 `click` 沾边的两个非零是 `clickjacking`（1 段，界面欺骗术语）与 `touch`（1 段，组件名 Touch Bar）。**"目标是否需要点击"不是一个可以被这份语料回答的问题。**

**二、能测到的只有否定式，且它只占 2.39%。** 把 WO 定义为 Impact 含 `without`，得 **178 段 / 97 页 / 37 名 / 37 条 distinct 句**。与 R 的 11.3%、G 的 29.3%、`unexpected` 的 27.6% 相比，**"某前提被省略"是措辞层最少见的陈述之一**。它的分布也极不均衡：Shortcuts 一个组件占 **43 段 = 24.2%**，频次 ≥7 的 9 句话占 **55.1%**，≥3 段的 21 页占 **47.8%**。

**三、句式决定了语义的位置。** 178 段里 `without` 短语**100% 压在 Impact 句尾、每段恰好一次、Impact 100% 无末尾标点**（全库该比例 98.7%）。由此，`without` 自身在拼接口径下**零丢失**，而它的 15 个宾语词丢 **3～116 段**（`consent` 51、`authentication` 51、`permission` 34、`user` 116）。**任何按字段拼接的统计都会系统性低估授权词**，这是本篇给方法论留下的最重要一条。

**四、"人"被写掉了。** 19 个 distinct 主语串中，"人"作主语头只有 **27 段**，程序与数据 **147 段**；`a remote attacker` 的 194 段、句首 `a user` 的 61 段、`a user may` 的 39 段与 WO **全部零交**。WO 内被动率 **19.1%**，是全库 **1.18%** 的约 **16 倍**，而 34 段被动句的主语全是数据（Hidden Photos Album、Private Browsing tabs、encrypted volume）。**措辞层的默认叙述者是一个程序，受事是一份数据，被省略的第三方在句法上只以 `without user consent` 的宾语身份存在 70 次。**

**五、它与"远程"几乎无关。** WO∩R = **27**（15.2%∶3.2%），且 **WO∩G = 0**——两套触发描述（`Visiting / Processing … may lead to X` 与 `X may … without Y`）在 2,179 段对 178 段的规模下**一段都不重合**。R 里最响的 `remote attacker`（194 段）在 WO 里是 0。**"无某前提"与"从远程到达"是措辞层的两条平行线**，把它们串起来需要一个这套语料没有提供的词。

**六、中文层不提供补强。** 《Apple 平台安全保护》178 页里，`轻点` 13 次／10 页、`点按` 与 `点击` 各 1 次，`长按`／`拖动`／`滑动`／`缩放`／`触控板`／`触摸`／`弹窗`／`红点` **0 次**；而"前提"侧的 `密码` 2,801、`配对` 701、`凭证` 528、`生物识别` 401、`验证` 433、`授权` 353 全部满页。**中文手册厚描写"要满足什么"，零描写"少了什么会怎样"。**

**七、所以本篇的实际产出是一条方法论，不是一个攻击结论。** 它可以支撑三件可复核的事：**（a）** 在公开英文后果字段里，"省略授权前提"这一陈述的覆盖率是 **2.39%（178／7,434）**，其宾语只有 **18 种短语**；**（b）** 这一陈述**不与远程通道共段**（27／178）且**不与触发式动名词句共段**（0／178）；**（c）** 它的句法位置**固定在句尾补足语位**（178∶0），因而对字段拼接口径**单向敏感**（宾语词最多丢 116 段）。防御侧可据此核对自家披露文案是否也在同一位置、同一宾语集上写"无某前提"；**本篇不给任何组件"可被实际触发"或"已修复"的判断，也不给任何一条攻击路径的可行性判断。**

# 附表 A 无交互措辞五组判据电池（90 行）

| 组 | 判据 | Impact 段 | Desc 段 | Ack 段 | 三字段并集段 | 页 | 名 | ∩WO | ∩R |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|
| 零点击词汇 | `zero`（词界） | 0 | 0 | 653 | 653 | 163 | 37 | 0 | 0 |
| 零点击词汇 | `zero-click` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 零点击词汇 | `zero click`（空格形） | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 零点击词汇 | `Zero Day Initiative` | 0 | 0 | 530 | 530 | 150 | 27 | 0 | 0 |
| 零点击词汇 | `ZeroClicks` | 0 | 0 | 16 | 16 | 7 | 4 | 0 | 0 |
| 零点击词汇 | `Google Project Zero` | 0 | 0 | 113 | 113 | 80 | 11 | 0 | 0 |
| 零点击词汇 | `one-click` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 零点击词汇 | `click`（词界） | 0 | 0 | 1 | 1 | 1 | 1 | 0 | 0 |
| 零点击词汇 | `clickjacking` | 1 | 2 | 0 | 3 | 3 | 2 | 0 | 0 |
| 零点击词汇 | `tap` / `taps` | 0 | 0 | 2 | 2 | 2 | 1 | 0 | 0 |
| 零点击词汇 | `touch`（词界） | 1 | 0 | 0 | 1 | 1 | 1 | 1 | 0 |
| 零点击词汇 | `swipe` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 零点击词汇 | `press` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 零点击词汇 | `hold`（词界） | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 零点击词汇 | `drag` | 0 | 0 | 11 | 11 | 11 | 1 | 0 | 0 |
| 零点击词汇 | `double-click` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动作名词 | `interact` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动作名词 | `interaction` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动作名词 | `user interaction` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动作名词 | `user action` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动作名词 | `user input` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动作名词 | `user gesture` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动作名词 | `user initiation` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动作名词 | `user approval` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动作名词 | `user-initiated` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动作名词 | `requires user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动作名词 | `the user may` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动作名词 | `a user may` | 39 | 0 | 0 | 39 | 33 | 15 | 0 | 0 |
| 交互动作名词 | `when the user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动作名词 | `after the user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动作名词 | `if the user` | 6 | 0 | 0 | 6 | 6 | 1 | 0 | 0 |
| 交互动作名词 | `the user has` | 6 | 0 | 0 | 6 | 6 | 1 | 0 | 0 |
| 交互动作名词 | `by the user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动作名词 | `manual` / `manually` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 授权缺失宾语 | `consent` | 54 | 25 | 0 | 76 | 58 | 18 | 54 | 14 |
| 授权缺失宾语 | `user consent` | 47 | 25 | 0 | 69 | 54 | 18 | 47 | 14 |
| 授权缺失宾语 | `prompting` | 19 | 0 | 0 | 19 | 19 | 2 | 19 | 0 |
| 授权缺失宾语 | `prompt`（词族） | 21 | 25 | 0 | 46 | 34 | 7 | 19 | 0 |
| 授权缺失宾语 | `confirmation` | 5 | 0 | 0 | 5 | 5 | 1 | 5 | 0 |
| 授权缺失宾语 | `user confirmation` | 5 | 0 | 0 | 5 | 5 | 1 | 5 | 0 |
| 授权缺失宾语 | `approv-` | 2 | 0 | 0 | 2 | 2 | 1 | 2 | 0 |
| 授权缺失宾语 | `permission` | 55 | 0 | 0 | 55 | 47 | 11 | 18 | 15 |
| 授权缺失宾语 | `authorization` | 11 | 194 | 0 | 198 | 64 | 52 | 10 | 0 |
| 授权缺失宾语 | `authentication` | 93 | 80 | 0 | 144 | 84 | 38 | 43 | 10 |
| 授权缺失宾语 | `credential(s)` | 29 | 0 | 0 | 29 | 28 | 8 | 5 | 10 |
| 授权缺失宾语 | `entitlement` | 7 | 128 | 0 | 131 | 74 | 38 | 4 | 0 |
| 授权缺失宾语 | `indicator` | 4 | 0 | 0 | 4 | 4 | 2 | 3 | 0 |
| 授权缺失宾语 | `pairing` | 9 | 0 | 0 | 9 | 9 | 3 | 7 | 9 |
| 授权缺失宾语 | `unauthorized` | 21 | 16 | 0 | 37 | 35 | 9 | 0 | 21 |
| 授权缺失宾语 | `unauthenticated` | 9 | 0 | 0 | 9 | 9 | 3 | 7 | 8 |
| 授权缺失宾语 | `without`（词界） | 178 | 0 | 11 | 189 | 105 | 39 | 178 | 27 |
| 授权缺失宾语 | `not without` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 无人值守副词 | `automatic` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 无人值守副词 | `automatically` | 0 | 0 | 3 | 3 | 3 | 1 | 0 | 0 |
| 无人值守副词 | `auto`（词界） | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 无人值守副词 | `silent` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 无人值守副词 | `silently` | 7 | 0 | 0 | 7 | 7 | 2 | 0 | 6 |
| 无人值守副词 | `silence`（词族） | 1 | 0 | 0 | 1 | 1 | 1 | 0 | 0 |
| 无人值守副词 | `actively` | 19 | 0 | 1 | 20 | 15 | 4 | 0 | 1 |
| 无人值守副词 | `active`（词界） | 1 | 1 | 0 | 2 | 2 | 2 | 0 | 0 |
| 无人值守副词 | `unintentionally` | 14 | 0 | 0 | 14 | 14 | 3 | 0 | 0 |
| 无人值守副词 | `inadvertently` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 无人值守副词 | `deliberately` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 无人值守副词 | `intentionally` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 无人值守副词 | `knowingly` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 无人值守副词 | `unknowingly` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 无人值守副词 | `without knowing` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 无人值守副词 | `unattended` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 无人值守副词 | `directly` | 0 | 0 | 1 | 1 | 1 | 1 | 0 | 0 |
| 无人值守副词 | `passively` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 无人值守副词 | `merely` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 无人值守副词 | `simply` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可见性与提示面 | `notif-`（词族） | 32 | 2 | 0 | 34 | 29 | 10 | 0 | 0 |
| 可见性与提示面 | `alert`（词界） | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可见性与提示面 | `dialog` | 3 | 0 | 0 | 3 | 3 | 1 | 0 | 3 |
| 可见性与提示面 | `sheet`（词界） | 0 | 0 | 1 | 1 | 1 | 1 | 0 | 0 |
| 可见性与提示面 | `banner` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可见性与提示面 | `visible to the user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可见性与提示面 | `shown to the user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可见性与提示面 | `may not be shown` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可见性与提示面 | `unintent-`对照 `unexpected` | 2053 | 0 | 0 | 2053 | 192 | 118 | 0 | 175 |
| 可见性与提示面 | `before` | 45 | 0 | 0 | 45 | 36 | 8 | 0 | 0 |
| 可见性与提示面 | `after` | 22 | 127 | 1 | 150 | 66 | 19 | 0 | 3 |
| 可见性与提示面 | `until` | 0 | 0 | 11 | 11 | 11 | 6 | 0 | 0 |
| 可见性与提示面 | `once`（词界） | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可见性与提示面 | `first`（词界） | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可见性与提示面 | `requires`（词族） | 13 | 2 | 0 | 15 | 10 | 3 | 0 | 12 |
| 可见性与提示面 | `opt in` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可见性与提示面 | `toggle` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 可见性与提示面 | `enabl-`（词族） | 20 | 2 | 0 | 22 | 18 | 11 | 3 | 4 |

# 附表 B 无交互措辞集 WO 的十五分区（16 行）

| 类 | 判据（Impact 句内，按表序互斥首计入） | 段 | 页 | 名 | ∩R | distinct Impact 句 | 组件 Top3 |
|---|---|---:|---:|---:|---:|---:|---|
| W1 | `without user consent` | 47 | 41 | 15 | 14 | 12 | App Support 7、CoreMotion 7、WebKit 7 |
| W2 | `without user authorization` | 9 | 9 | 2 | 0 | 2 | Contacts 5、Mail 4 |
| W3 | `without user permission` | 9 | 9 | 4 | 0 | 2 | CoreBluetooth - LE 4、LaunchServices 2、WebKit 2 |
| W4 | `without user confirmation` | 5 | 5 | 1 | 0 | 1 | Shortcuts 5 |
| W5 | `without prompting` | 19 | 19 | 2 | 0 | 2 | Shortcuts 16、DiskArbitration 3 |
| W6 | `without consent`（无 user） | 7 | 7 | 1 | 0 | 1 | Shortcuts 7 |
| W7 | `without authentication` | 43 | 33 | 11 | 1 | 7 | Safari Private Browsing 11、Photos 8、iCloud Document Sharing 6 |
| W8 | `without permission`（无 user） | 5 | 4 | 2 | 0 | 2 | Watch App 3、Shortcuts 2 |
| W9 | `without authorization`（无 user） | 1 | 1 | 1 | 0 | 1 | Touch Bar 1 |
| W10 | `without pairing` | 7 | 7 | 1 | 7 | 1 | AirPlay 7 |
| W11 | `without valid …` | 5 | 5 | 2 | 5 | 1 | Screen Sharing 3、Screen Sharing Server 2 |
| W12 | `without having …` | 11 | 11 | 2 | 0 | 2 | CoreCrypto 7、LaunchServices 4 |
| W13 | `without (an) entitlement …` | 4 | 4 | 1 | 0 | 1 | Parental Controls 4 |
| W14 | `without (an 或 user) indicator` | 3 | 3 | 1 | 0 | 1 | Control Center 3 |
| W15 | `without restrictions` | 3 | 3 | 1 | 0 | 1 | dyld 3 |
| **合计** | **W1–W15 十五类互斥并集 = `without` 触达集 WO** | **178** | **97** | **37** | **27** | **37** | **—** |

# 附表 C 对照集 G 的二十一个句首动名词与 ∩WO（22 行）

| 句首动名词 | 段 | 页 | 名 | ∩WO | ∩R | distinct 首句 | 组件 Top3 |
|---|---:|---:|---:|---:|---:|---:|---|
| `processing` | 1760 | 219 | 67 | 0 | 0 | 80 | WebKit 677、ImageIO 253、CoreMedia 95 |
| `parsing` | 146 | 65 | 19 | 0 | 0 | 8 | CoreAudio 20、sips 19、CoreMedia 17 |
| `visiting` | 82 | 57 | 6 | 0 | 82 | 7 | Safari 37、WebKit 24、zlib 8 |
| `connecting` | 58 | 16 | 5 | 0 | 0 | 12 | Kernel 25、SMB 18、WebDAV 6 |
| `mounting` | 40 | 17 | 8 | 0 | 12 | 7 | HFS 9、SMB 9、exFAT 7 |
| `opening` | 21 | 8 | 4 | 0 | 3 | 4 | Model I/O 7、RealityKit 7、WebKit 4 |
| `restoring` | 18 | 13 | 4 | 0 | 0 | 2 | MobileBackup 9、Managed Configuration 6、Books 2 |
| `playing` | 8 | 8 | 1 | 0 | 0 | 1 | CoreAudio 8 |
| `deleting` | 6 | 6 | 1 | 0 | 0 | 1 | Photos Storage 6 |
| `extracting` | 6 | 6 | 1 | 0 | 0 | 1 | file_cmds 6 |
| `running` | 6 | 6 | 2 | 0 | 0 | 2 | Disk Images 3、Disk Utility 3 |
| `unpacking` | 6 | 6 | 1 | 0 | 0 | 1 | Compression 6 |
| `replying` | 4 | 4 | 1 | 0 | 4 | 1 | Mail Drafts 4 |
| `turning` | 4 | 4 | 1 | 0 | 4 | 1 | Mail 4 |
| `copying` | 3 | 3 | 1 | 0 | 0 | 1 | WebKit Web Inspector 3 |
| `incoming` | 3 | 3 | 1 | 0 | 0 | 1 | FaceTime 3 |
| `cloning` | 2 | 2 | 1 | 0 | 2 | 1 | Git 2 |
| `loading` | 2 | 2 | 1 | 0 | 0 | 1 | WebKit 2 |
| `muting` | 2 | 2 | 2 | 0 | 0 | 2 | Audio 1、FaceTime 1 |
| `enabling` | 1 | 1 | 1 | 0 | 0 | 1 | Setup Assistant 1 |
| `forwarding` | 1 | 1 | 1 | 0 | 1 | 1 | Mail Drafts 1 |
| **合计（21 词）** | **2179** | **224** | **98** | **0** | **108** | **—** | **—** |

# 附表 D 组件名 × WO 段数与 WO 占比（37 行）

| 组件名 | WO 内段 | ∩R | ∩G | 该组件在 Impact 全集的段 | WO 占比 | 该组件最高频 without 短语 |
|---|---:|---:|---:|---:|---:|---|
| Shortcuts | 36 | 0 | 0 | 129 | 27.9% | prompting the user（16） |
| WebKit | 12 | 7 | 0 | 910 | 1.3% | user consent（7） |
| Safari Private Browsing | 11 | 0 | 0 | 16 | 68.8% | authentication（11） |
| Photos | 9 | 0 | 0 | 19 | 47.4% | authentication（8） |
| AirPlay | 7 | 7 | 0 | 77 | 9.1% | pairing（7） |
| App Support | 7 | 0 | 0 | 7 | 100.0% | user consent（7） |
| CoreCrypto | 7 | 0 | 0 | 7 | 100.0% | having the private（7） |
| CoreMotion | 7 | 0 | 0 | 7 | 100.0% | user consent（7） |
| LaunchServices | 6 | 0 | 0 | 58 | 10.3% | having app management（4） |
| iCloud Document Sharing | 6 | 0 | 0 | 6 | 100.0% | authentication（6） |
| Accounts | 5 | 0 | 0 | 34 | 14.7% | authentication（5） |
| Contacts | 5 | 0 | 0 | 25 | 20.0% | user authorization（5） |
| CoreBluetooth - LE | 4 | 0 | 0 | 4 | 100.0% | user permission（4） |
| Mail | 4 | 0 | 0 | 36 | 11.1% | user authorization（4） |
| Parental Controls | 4 | 0 | 0 | 4 | 100.0% | an entitlement check（4） |
| Safari | 4 | 3 | 0 | 92 | 4.3% | user consent（3） |
| Control Center | 3 | 0 | 0 | 8 | 37.5% | an indicator（3） |
| DiskArbitration | 3 | 0 | 0 | 25 | 12.0% | prompting for the（3） |
| NetAuth | 3 | 3 | 0 | 15 | 20.0% | user consent（3） |
| Photos Storage | 3 | 0 | 0 | 18 | 16.7% | authentication（3） |
| Sandbox | 3 | 0 | 0 | 65 | 4.6% | user consent（3） |
| Screen Sharing | 3 | 3 | 0 | 3 | 100.0% | valid credentials（3） |
| Storage Management | 3 | 0 | 0 | 3 | 100.0% | user consent（3） |
| Sync Services | 3 | 0 | 0 | 12 | 25.0% | user consent（3） |
| Watch App | 3 | 0 | 0 | 4 | 75.0% | permission（3） |
| dyld | 3 | 0 | 0 | 15 | 20.0% | restrictions（3） |
| Screen Sharing Server | 2 | 2 | 0 | 16 | 12.5% | valid credentials（2） |
| Security | 2 | 0 | 0 | 91 | 2.2% | user consent（2） |
| SharedFileList | 2 | 0 | 0 | 39 | 5.1% | user consent（2） |
| Accessibility | 1 | 0 | 0 | 46 | 2.2% | authentication in assistive（1） |
| Foundation | 1 | 0 | 0 | 67 | 1.5% | user permission（1） |
| Image Capture | 1 | 0 | 0 | 7 | 14.3% | user consent（1） |
| NetworkExtension | 1 | 1 | 0 | 28 | 3.6% | user consent（1） |
| Siri | 1 | 0 | 0 | 116 | 0.9% | authentication（1） |
| Touch Bar | 1 | 0 | 0 | 4 | 25.0% | authorization（1） |
| Wi-Fi | 1 | 1 | 0 | 58 | 1.7% | authentication（1） |
| WindowServer | 1 | 0 | 0 | 32 | 3.1% | user consent（1） |

# 附表 E 中文手册层六组对应词（46 行）

| 组 | 中文词 | 出现次数 | 覆盖页 | 覆盖页占比 |
|---|---|---:|---:|---:|
| 触屏动作 | 轻点 | 13 | 10 | 5.6% |
| 触屏动作 | 点按 | 1 | 1 | 0.6% |
| 触屏动作 | 点击 | 1 | 1 | 0.6% |
| 触屏动作 | 长按 | 0 | 0 | 0.0% |
| 触屏动作 | 拖动 | 0 | 0 | 0.0% |
| 触屏动作 | 滑动 | 0 | 0 | 0.0% |
| 触屏动作 | 缩放 | 0 | 0 | 0.0% |
| 触屏动作 | 触控板 | 0 | 0 | 0.0% |
| 授权动作 | 授权 | 353 | 178 | 100.0% |
| 授权动作 | 许可 | 14 | 12 | 6.7% |
| 授权动作 | 允许 | 152 | 82 | 46.1% |
| 授权动作 | 批准 | 49 | 25 | 14.0% |
| 授权动作 | 同意 | 26 | 19 | 10.7% |
| 授权动作 | 确认 | 250 | 178 | 100.0% |
| 授权动作 | 验证 | 433 | 178 | 100.0% |
| 授权动作 | 认证 | 380 | 93 | 52.2% |
| 授权动作 | 询问 | 5 | 4 | 2.2% |
| 授权动作 | 提示 | 45 | 32 | 18.0% |
| 交互名词 | 交互 | 19 | 16 | 9.0% |
| 交互名词 | 操作 | 476 | 178 | 100.0% |
| 交互名词 | 手动 | 27 | 19 | 10.7% |
| 交互名词 | 手动操作 | 1 | 1 | 0.6% |
| 交互名词 | 输入 | 106 | 55 | 30.9% |
| 交互名词 | 口令 | 1 | 1 | 0.6% |
| 无交互侧 | 无需 | 64 | 48 | 27.0% |
| 无交互侧 | 不需要 | 6 | 4 | 2.2% |
| 无交互侧 | 未经 | 21 | 19 | 10.7% |
| 无交互侧 | 自动 | 898 | 178 | 100.0% |
| 无交互侧 | 静默 | 1 | 1 | 0.6% |
| 无交互侧 | 后台 | 234 | 178 | 100.0% |
| 无交互侧 | 主动 | 3 | 3 | 1.7% |
| 无交互侧 | 不知情 | 1 | 1 | 0.6% |
| 凭据侧 | 密码 | 2801 | 178 | 100.0% |
| 凭据侧 | 生物识别 | 401 | 178 | 100.0% |
| 凭据侧 | 解锁 | 384 | 178 | 100.0% |
| 凭据侧 | 锁定 | 306 | 178 | 100.0% |
| 凭据侧 | 信任 | 363 | 178 | 100.0% |
| 凭据侧 | 配对 | 701 | 178 | 100.0% |
| 凭据侧 | 凭证 | 528 | 178 | 100.0% |
| 凭据侧 | 令牌 | 70 | 15 | 8.4% |
| 可见性侧 | 通知 | 50 | 31 | 17.4% |
| 可见性侧 | 弹窗 | 0 | 0 | 0.0% |
| 可见性侧 | 对话框 | 3 | 2 | 1.1% |
| 可见性侧 | 横幅 | 1 | 1 | 0.6% |
| 可见性侧 | 指示器 | 1 | 1 | 0.6% |
| 可见性侧 | 红点 | 0 | 0 | 0.0% |

---

**边界声明：** 本篇全部材料来自公开可得的安全响应通告与公开安全文档的字面统计，仅用于防御性分析与文本计量研究。文中不含有任何绕过方法、利用代码、恶意样本、利用链构造方式，亦不含针对他人设备的具体操作步骤与时序；所有数字均为公开英文措辞在指定字段口径下的出现频次，只反映**文本如何描述后果**，不构成对任何组件实际触发条件、交互要求或修复状态的判断，也不构成任何攻击方式真实存在或无需交互的结论。

Telegram: https://t.me/one00190
