@@TITLE@@iOS 身份认证、凭据与授权载体类弱点的公开措辞口径分析
@@SUBTITLE@@选题是"身份认证与验证动作（认证、校验、证书与密码学）""凭据与认证材料本体（密码、口令、凭据、令牌、钥匙串、生物特征）""会话、账户与授权载体（账户、会话、Cookie、授权与权限属性）"这三类措辞。三刀逐词测量给出五条反直觉结论。**第一，三刀不是三把大小相当的刀，而是同一条"字段梯度"**：刀一（凭据与认证材料）100 段**全部只在 Impact 字段（Description 侧 0 段）**，刀三（会话与授权载体）382 段里 **340 段在 Description、Impact 只有 59 段**，刀二（认证与校验动作）184 段两侧几乎对半（**Impact 115∶Description 111，同段 42**）。也就是说：**公开文本只在"后果句"里点名密码、凭据、钥匙串，只在"修复句"里点名授权与权限属性，"认证"这一个词两边都写。第二，主集 600 段的字段方向与本系列惯常形态相反**：Description 侧 443 段比 Impact 侧 243 段多 200 段，**357 段（59.50%）的 Impact 字段里一个三刀词都没有**（这 357 段并非缺 Impact 字段——主集 600 段 Impact 字段全部非空，只是后果句写的是 read／access／view 这类动作词）。**第三，最大的一句是修复句而不是后果句**：`An authorization issue was addressed with improved state management.` 单句 **159 句次＝全部 694 句次的 22.91%**；694 句次只由 **85 个不同句子**写成，其中 **63 句只在 Impact、22 句只在 Description、两侧共用 0 句**；Description 侧 451 句次里 top3 句就占 **315 句次＝69.84%**。**第四，句首形态与本系列前一篇完全反转**：#135 的 Impact 句 74.90% 以动名词打头（`Processing …`），**本篇 694 句次里动名词打头只有 1 句次＝0.14%**（`Restoring from a backup may prevent passcode from being required immediately after Face ID enrollment`——主集内 `Face ID` 一词的 1 段就是这句）；主语＋动词形 667 句次＝96.11%。**第五，词界断言第五次发作、也是五篇里最轻的一次**：去掉前置 `(?<![\w-])` 主集从 600 涨到 **632 段（＋32）**，而这 32 段**全部是 `unauthorized` 一词内部藏着的 `authorized`**（32 段一段都没有独立的 `authoriz*` 词形，全树 `unauthoriz*` 词形 37 段）；去掉尾断言 `(?![\w-])` 再多 **2 段（602）**，两段的真凶都是 `Account-driven` 这个连字符复合词。本篇主集 **AUAT ＝ 600 段／159 页／119 名**，占全树 **6.57%**、占含 Impact 段的 **8.07%**，是措辞层系列迄今最小的主集；**三字段并集＝两字段主集＝600（致谢字段命中 0，继 #135 之后系列第二次）**；后果强度八类在主集内**相加 115＝并集 115（差 0）**，**485 段＝80.83% 一个后果类都不命中**，`MEM`（内存损坏）与 `FULL`（完全控制）两类**各 0 段**，与"同段 ≥2 后果类"集 **M2（全树 185 段）交 0 段**。
@@INFO@@报告日期|2026 年 11 月 2 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（258 份文件排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 9,132 段含致谢**。本篇主集 AUAT ＝ 刀一 `password(s)／passcode(s)／passphrase(s)／credential(s)／token(s)／secret(s)／keychain*／biometric*／Touch ID／Face ID／Apple Pay／Secure Enclave／user(name)／username` 或刀二 `authenticat*／unauthenticated／verif*／certificat*／signature(s)／cryptograph*／hash*／HMAC` 或刀三 `account(s)／session(s)／cookie(s)／identity／identities／authoriz*／entitlement(s)／signed in／sign in／login／log in／logout／log out` 出现在 **Impact 或 Description** ＝ **600 段 / 159 页 / 119 名**，其中 Impact 侧 **243** 段、Description 侧 **443** 段；字段组合 **仅I 157／仅D 357／I∩D 86**（相加 600）；致谢字段命中 **0** 段，三字段并集 ＝ **600 段 / 159 页 / 119 名（与两字段逐值相同）**。三刀定义（两字段口径）：刀一 **100** 段（Impact 100∶Description 0，I∩D 0，页 65、名 25）、刀二 **184** 段（115∶111，I∩D 42，页 95、名 45）、刀三 **382** 段（59∶340，I∩D 17，页 117、名 87）；三刀并集与合成词表 `$re3` 逐段相等，**差 0 段**。闭合分区（优先序 一→二→三，两两档已剔三刀同段）：仅一 **57**／仅二 **138**／仅三 **343**／一二共现 23／一三共现 16／二三共现 19／三刀同段 **4**，七档相加 ＝ 600。对照集：**R** ＝ #118 远程触达词族 **843 段 / 178 页 / 72 名**；**E** ＝ #114 在野集 **108 段 / 76 页 / 12 名**；**WO** ＝ #119 无前提集 **178 段**；**G／HI** ＝ 动名词开头集 **2,179 段** 与交互触发集 **273 段**；**M2** ＝ 同段 ≥2 后果类集 **185 段**；**UU** ＝ Impact 含 `user` **1,627 段**；**RC** ＝ #122 竞态集 **232 段**；**L** ＝ #123 逻辑集 **550 段**；**Ds** ＝ #124 拒绝服务集 **470 段**；**X** ＝ #125 泄露集 **427 段**；**BP** ＝ #126 绕过集 **301 段**；**PMS** ＝ #129 三族并集 **2,213 段**；**TLO** ＝ #130 三刀并集 **343 段**；**WEB** ＝ #131 三刀并集 **1,253 段**；**IMGX** ＝ #132 主集（本篇内重建 **1,543 段**）；**RESX** ＝ #133 主集 **2,791 段**；**RESM** ＝ #134 主集 **1,541 段**；**AVMP** ＝ #135 主集 **2,715 段**；**REM128** ＝ #128 远程通道主集 **849 段**；**CK** ＝ #127 修复动词词族 **4,146 段**；**MEM** ＝ `memor*` 词族 **1,967 段**；**BND** ＝ `out of bounds` 两形 **816 段**；**UAFH／UAFS** ＝ `use-after-free` 连字符形 **193 段**／空格形 **127 段**；**PRVI** ＝ `Privacy Indicator(s)` **4 段**；**EXFL** ＝ `exfiltrat*` **51 段**；**PERM2** ＝ `permission／consent` **639 段**。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或利用链构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**"认证""验证""凭据""密码""口令""钥匙串""生物特征""账户""会话""Cookie""授权""权限属性""证书""签名"这些说法在公开英文文本的哪个字段、哪一句位、与哪一个修复短语搭配，以及它与远程、在野、后果强度、组件名之间的集合关系**，属于措辞与文本计量分析；文中任何"认证绕过""凭据泄露""钥匙串读写""授权缺陷"表述均指**公开条目使用了该词或该短语这一文本事实**，不是对该条目真实缺陷类别、CWE 归属、是否可被远程触达、是否被利用的判断，也不构成对 iOS 认证、凭据存储或授权模型安全性的结论。本篇尤其不涉及任何登录、解锁、配对、证书校验的操作时序或尝试次数策略的实现细节
@@INFO@@分类|判据**六组 290 项 / 并集全零 16 项 / 非零 274 项**（附表 A，六组：本体词 165、句级刀 36、机制名与检测词对照 54、修复套话句 15、外部集对照 12、后果强度八类 8）/ **八十五个含主刀整句 × 归桶 × 句次 × I∶D × 页 × ∩R ∩E ∩M2 ∩L ∩RC**（附表 B，86 行含合计，句次合计 **694 ＝ Impact 243 ＋ Description 451**、页列合计 514）/ **二十八个句形桶 × 句次 × 段页名 × I∶D × ∩R ∩E，续表六档句式形态**（附表 C，29 行含合计，桶句次闭合 **694**、未归类 **0**、多重命中句 **73** 条；续表 **A／An X issue was addressed 255／The／This issue was addressed 147／A／An X（非 issue 名词）was addressed 39／X existed 8／含 may 233／其余 12** 闭合 694）/ **页面、编号、致谢、版式与字段落位五类二十七行分箱**（附表 D，27 行，页八档闭合 159、编号三档闭合 600、致谢七档闭合 599）/ **中文层 63 词**（附表 E，八组：认证与身份类 8、凭据与材料类 8、生物识别与设备解锁类 7、会话与账户类 8、授权与权限类 8、证书与密码学类 8、后果强度类 8、修复套话类 8；**全零 14 词、非零 49 词**）/ **组件层 119 名**（附表 F，120 行含合计，段合计 600、页列合计 560、这 119 名的 Impact 全集 **4,220** 段、占全树含 Impact 段的 **56.8%**）/ **十一把刀**：字段刀（Impact 243 ∶ Description 443 ∶ 致谢 0，两字段拆分 仅I 157／仅D 357／I∩D 86）、逐刀字段刀（刀一 100∶0、刀二 115∶111、刀三 59∶340，**三刀各自 I∩D ＝ 0／42／17**）、词族级落位刀（`password` 23∶0、`credential(s)` 29∶0、`keychain*` 37∶0、`account(s)` 33∶0、`authenticat*` 98∶80、`authoriz*` 11∶194、`entitlement` 7∶128、`verif*` 0∶9、`cryptograph*` 0∶11、`session(s)` 0∶4、`manag*` 7∶1038）、概念刀（`out of bounds` 两形 816∩主集 **0**；`use-after-free` 连字符形 193∩**0**、空格形 127∩**0**；`memor*` 1,967∩**0**）、组件名刀（认证类组件名 **88** 段里 **32** 段进主集、`Security` 类名 **202** 段里 **66** 段进主集；主集 600 段落在 **119** 个组件名下）、句库刀（694 句次只由 **85** 个不同句子写成，top1 占 **22.91%**、top3 **45.39%**、top5 **50.58%**、top10 **61.10%**、top50 **93.66%**）、标点刀（本篇**句号异形可合并组 ＝ 2**，两组都是带点 **1**∶不带点 **1**，分别是 `An authorization issue was addressed with improved state management` 与 `An attacker with arbitrary read and write capability may be able to bypass Pointer Authentication`；正文实体 `&rsquo;` **0** 处、`&amp;` **0** 处、不换行空格 **0** 处）、致谢刀（三字段并集比两字段多 **0** 段，致谢哨兵命中 **0** 段——**系列第二次**）、`manag*` 摘除刀（姊妹集 #134 ∩主集 274 段**全部含 `manag*`**、其中 270 段带 `improved state management`；把 #134 的 `manag*` 摘掉后它自己剩 675 段、与本主集交 **0**）、词界断言刀（去掉 `(?<![\w-])` 主集 **632 段（＋32）**，32 段全来自 `unauthorized`；去掉 `(?![\w-])` **602 段（＋2）**，2 段来自 `Account-driven`）、缺席刀（`passphrase／secret(s)／Touch ID／Apple Pay／Secure Enclave／signature(s)／hash*／HMAC／identity／identities／PAC 专名（大小写敏感）／authenticator／design*` **十三项两字段或三字段口径全 0**，合并对照共 16 项全零）
@@INFO@@难度|进阶（本篇的 600 段有**四套分母**：占全树 9,136 段是 **6.57%**、占含 Impact 的 7,434 段是 **8.07%**、占其所在 159 页全部 8,040 段是 **7.46%**（这 159 页每页平均 50.6 段，本篇每页平均 **3.77** 段），三个数都真、不可混用；第四套是**版式域**分母——域 A **594／9,045 ＝ 6.57%**、域 B **6／91 ＝ 6.59%**，**本篇在两个版式域的占比几乎相同（差 0.02 个百分点）**，与 #134（域 B 只有域 A 的五分之一）和 #135（域 B 高于域 A）都不同；**600 是两字段主集，也是三字段并集（二者相等）**、**694 是句次合计**、**243 有两个含义**（Impact 侧段数与 Impact 侧判据句句次恰好都是 243，因为 94 个两句段里没有一段出现两句 Impact）、**443 与 451 相差 8**（8 个仅 Description 的段各写两句修复句）。600 段里 **506 段只含 1 个本篇整句、94 段含 2 句、0 段一句都没有**，所以句次比段数多 94；这 94 个两句段**拆成 86 段"一句 Impact ＋一句 Description"（与字段刀的 I∩D ＝ 86 是同一批段）＋ 8 段"两句都在 Description"**，两处 86 相等的成因见 1.3。**凡"A 与 B 得数相同"处必须同时报 \|A\|、\|B\|、\|A∩B\|**——第 15 章的成员级核验结论是：**`刀二I 摘 PA` 98 与 `authenticat*I` 98 交 81（既非同集也非子集，双向差集各 17）**、**`RESM∩主集` 274 与 `manag*∩主集` 274 交 274（真同集）**、**`#134 去 manag*` 675 与主集交 0（完全不相交）**、**`memor*` 1,967 与主集交 0**、**`PRVI` 4 与主集交 0**、**`key(s)` 16 与主集交 16（100% 已在主集内，所以收它进刀主集不涨）**；附表 B、C、F 的页列与名列是逐行去重后的再求和，**不得当作并集去重值**（B 页列 514、C 页列 458／名列 195、F 页列 560，并集真值是 159 页／119 名）；附表 B 与 F 的外部集列同理是**逐行和**：B 的 ∩R 行和 **79** 对并集真值 **64**、∩E／∩M2／∩L／∩RC 四列行和 **13／0／19／5** 与真值逐列相同；F 的 ∩R／∩E／∩M2／∩L／∩BP 行和 **64／13／0／19／40** 与真并集**逐值相同**（组件名是段的单值属性）。附表 A「后果强度八类」∩主集列是**三字段口径**（八类相加 **120**、并集 **120**、零类段 **480**），第 9 章正文另给 **Impact 单字段口径**（相加 **115**、并集 **115**、零类段 **485**），两套口径都不是主集段数 600；附表 A 的句级刀 ∩主集列是**段级命中**、附表 C 的桶句次相加 **694** 是**句次轴**，两者不可互换（28 桶的段列相加会超过 600，因为一段两句可以分属两桶））
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

**选题给的是三个并列说法："身份认证与验证动作""凭据与认证材料本体""会话、账户与授权载体。"这三个说法在 Apple 安全响应顾问页里是不是三把大小相当的刀？** 本篇把它们拆成十二个可逐词回答的断言：

1. 三类说法各有多少段？（第 2–4 章：刀一 **100**、刀二 **184**、刀三 **382**，三刀相加 666 对并集 600、重叠 **66**。**最大的一根是"授权载体"，最小的一根是"认证材料"，两者相差近四倍。**）
2. 三类说法分别写在哪个字段？（第 6 章：**刀一 Impact 100∶Description 0；刀三 Impact 59∶Description 340；刀二 Impact 115∶Description 111、同段 42**。这是本系列第一次出现"一根刀零跨界、另一根刀 89% 在 Description"的**梯度形态**。）
3. "凭据本体"用什么词写？（第 2 章：`keychain*` **37**、`credential(s)` **29**、`password(s)` **23**、`passcode(s)` **10**、`token(s)` **4**、`user name(s)` **4**、`biometric*` **1**、`Face ID` **1**，八个非零词形相加 **109** 对刀一并集 100、净重叠 9；`passphrase`、`secret(s)`、`Touch ID`、`Apple Pay`、`Secure Enclave` **五项两字段全 0**。）
4. "认证与校验动作"用什么词写？（第 3 章：`authenticat*` **144** 段一个词族就是刀二的 78.3%；`certificat*` 与 `cryptograph*` 各 **11**、`unauthenticated` 与 `verif*` 各 **9**；`signature(s)`、`hash*／HMAC` **两项全 0**。**五个非零词形相加 184＝刀二并集 184，说明这五族在同一段里不共现。**）
5. "会话与授权载体"用什么词写？（第 4 章：`authoriz*` **198**、`entitlement(s)` **131**、`account(s)` **33**、`cookie(s)` **17**、`sign(in)／signed in／login 四形` 共 **10**、`session(s)` **4**、`identity／identities` **0**；八个非零词形相加 **393** 对并集 **382**、净重叠 11。**`authoriz*` 的 198 段里 194 段写在 Description。**）
6. 三刀落在哪一字段方向？（第 6 章：主集 **Description 443 ＞ Impact 243**。357 段（59.50%）Impact 字段不含三刀词、301 段靠刀三进主集；157 段仅 Impact，其中刀一 73、刀二 67、刀三 33。**主集 600 段的 Impact 字段全部非空**，所以"只在 Description 命中"不等于"没有 Impact 句"。）
7. 这批条目的句子有多模板化？（第 7 章：**85 个不同句子写完 694 句次**；top1 是 `An authorization issue was addressed with improved state management.` **159 句次＝22.91%**，top3 **45.39%**、top10 **61.10%**、top50 **93.66%**；**63 句只在 Impact、22 句只在 Description、0 句两侧共用**；句号异形只有 **2 组**。）
8. 后果与情态怎么写？（第 8、9 章：Impact 判据句 **`may` 233、`could` 9、`can` 1、`might／should／will／must` 各 0**（相加 243＝句次 243，无情态 0 句）；Description 判据句 **451 句次情态 0**。八类后果 Impact 口径命中并集 **115 段＝主集的 19.17%**、**485 段（80.83%）一个后果类都不命中**；**`MEM`、`FULL` 两类各 0 段**；同段 ≥2 类 **0 段**。）
9. 三刀与 #132／#133／#134／#135 的真实重合有多大？（第 15.4 章：**∩RESM 274 段＝主集的 45.67%** 是唯一超过一成半的姊妹交，而它**100% 由 `manag*` 构成**；其余姊妹全部 ≤10.83%（∩REM128 65、∩WEB 47、∩AVMP 41、∩BP 40、∩IMGX 25、∩RESX 23、∩L 19、∩PMS 5、∩Ds 5、∩X 5、∩RC 5、**∩TLO 0**）。五篇（#132＋#133＋#134＋#135＋本篇）并集 **5,069 段＝全树的 55.48%**。）
10. 词界断言在本篇咬掉了什么？（第 5 章：**最轻的一次**。去前置 → **632 段（＋32）**，32 段**全部是 `unauthorized` 内部的 `authorized`**，这 32 段一段都没有独立 `authoriz*` 词形；去尾 → **602 段（＋2）**，2 段都是 `Account-driven`（页 `124149／124150`）。**#132 尾漏 193、#133 前置挡 162、#134 前置挡 434、#135 前置挡 488 且尾挡 9、本篇前置 32 尾 2——本篇是五篇中断言影响最小的一次，因为三刀词形几乎没有内部嵌套。**）
11. 致谢字段能不能进判据？（第 12.1 节：**不能，本篇也不需要警告**。三字段并集 ＝ 两字段 ＝ **600**，致谢字段主刀命中 **0** 段（#135 是 9 段零增量，本篇是 0 段零增量）。但致谢侧仍有 **599** 段写了人名、人次 **1,073**，只是三刀词一个都不出现在人名里。）
12. "认证"在组件名层能解释多少？（第 14 章：**解释不到四成**。认证类组件名 **88** 段里 **32** 段进主集（占该代理 **36.36%**）、`Security` 类名 **202** 段里 **66** 段（**32.67%**）、`Account` 类名 **101** 段里 **30** 段（**29.70%**）；网络类名 **340** 段里 45 段（13.24%）、Kernel 名 **745** 段里 19 段（**2.55%**）、WebKit/Safari 名 **1,312** 段里 47 段（**3.58%**）。**头部两名 `Security` 31 ＋ `WebKit` 28 ＝ 59 段＝主集的 9.83%**，119 名里 **19** 个名只有 1 段。）

**本篇不给任何利用方法、不给任何针对他人设备的操作时序，也不推断任何条目真实的缺陷类别、CWE 归属或可利用性。** 所有结论只是"哪个词在哪个字段、哪一句位出现过多少次、与哪一组段落相交"。尤其要强调：本篇统计"认证""凭据""授权"这些**词**的分布，不评价任何认证机制、口令策略、钥匙串实现或授权模型的安全性，也不给出任何提高尝试次数、规避校验或恢复凭据的方法。

# 1 口径、判据与四套分母

## 1.1 三刀的定义

本篇主集写作 **AUAT**，定义为：

- **刀一（凭据与认证材料本体）** `password(s)／passcode(s)／passphrase(s)／credential(s)／token(s)／secret(s)／keychain*／biometric*／Touch ID／Face ID／Apple Pay／Secure Enclave／user(name)／username`
- **刀二（认证、校验与密码学动作）** `authenticat*／unauthenticated／verif*／certificat*／signature(s)／cryptograph*／hash*／HMAC`
- **刀三（会话、账户与授权载体）** `account(s)／session(s)／cookie(s)／identity／identities／authoriz*／entitlement(s)／signed in／sign in／login／log in／logout／log out`

三支刀一律带 `(?<![\w-])` 前置词界断言与 `(?![\w-])` 尾断言；命中 **Impact 或 Description** 任一字段的段进入主集（**两字段口径**），致谢字段只做哨兵不参与判据。这与 #127–#135 同构。

**四处刻意的取舍**（理由分别在第 1.5、2.5、3.4、4.4 节）：刀一**不收**裸词 `key(s)`；刀二**不摘** `Pointer Authentication`；刀三**不收** `permission(s)／consent`；三刀**都不收** `access` 裸词与 `validat*` 词族。

五条必须区分的口径：

| 口径 | 记号 | 段数 | 这是什么 |
|---|---|---:|---|
| 两字段主集 | AUAT ＝ CU | **600** | 三刀命中 Impact 或 Description |
| 三字段并集 | ALL3 | **600** | 再并入致谢字段，**多 0 段**（见 12.1） |
| Impact 段数 | CI | **243** | Impact 字段含三刀词的**段**数 |
| Description 段数 | CD | **443** | Description 字段含三刀词的**段**数 |
| Description 句次 | — | **451** | Description 侧判据**整句**句次，比 CD 多 8（见 1.3） |

三刀并集与合成词表 `$re3`（把三刀并成一张词表）**逐段相等，差 0 段**——本篇不存在 #132 那种"子刀有、合并词表没有"的漏项。

## 1.2 四套分母

600 段可以被四组分母除，四个商都真：

- 全树段 **9,136** → **6.57%**（本系列最小主集；#135 是 29.72%、#134 是 16.87%、#130 是 3.75%）
- 含 Impact 字段段 **7,434** → **8.07%**
- 所在 **159** 页的全部 **8,040** 段 → **7.46%**（这 159 页每页均 **50.6** 段，本篇每页均 **3.77** 段——**本篇在自己出现的页里也只是零头**）
- 版式域：域 A **594／9,045 ＝ 6.57%**、域 B **6／91 ＝ 6.59%**（**两个商几乎相同**，见 12.3）

**第四个商是措辞层系列第一次"域 A 与域 B 完全同比例"**：#134 的域 B 占比只有域 A 的五分之一、#135 的域 B 高于域 A 六个百分点，本篇两者差 **0.02 个百分点**。这不说明版式无关，只说明本篇的选题（认证与授权）在 11 页粗体版式里没有系统性偏斜。

## 1.3 段、句、字段三个计数轴

| 轴 | 值 | 与谁闭合 |
|---|---:|---|
| 段 | **600** | 仅I 157＋仅D 357＋I∩D 86 |
| 句次 | **694** | Impact 243＋Description 451 |
| 每段句数 | 1 句 **506**／2 句 **94** | 相加 600；句次 506＋94×2＝694 |

94 个两句段的构成必须先说清，否则容易误抄 #135 的写法：**86 段是"一句 Impact ＋一句 Description"（这 86 段就是字段刀的 I∩D 86 段），另外 8 段是"两句都落在 Description"**。所以：

- Impact 侧句次 243 ＝ Impact 侧段数 243，**每个 Impact 命中段恰好贡献 1 句判据句**；
- Description 侧句次 451 ＝ 段数 443 ＋ 8（8 段各写两句修复句）；
- 两句段总数 94 ＝ 86（I＋D）＋ 8（D＋D）。

**#135 的"两句段数＝I∩D"是巧合的相等（30＝30），本篇把它拆开了：94 ≠ 86。** 抄数的人如果按 #135 的经验把 94 写成 I∩D，就会把字段刀写错。

另一个必须区分的轴：**Impact 字段的"全部句子"**（主集 600 段的 Impact 文本共切成 **613** 句，平均每段 **1.02** 句；只看 Impact 命中三刀的 243 段，这 243 段的 Impact 文本共 **247** 句）与 **Impact 判据句**（**243** 句次）。前者是"这些段一共写了多少句"，后者是"其中多少句里出现了三刀词"。Description 侧同理：**全句 624 句、判据句 451 句次**。**613 与 694 不可互换**，本篇正文除特别说明外一律用判据句轴。

## 1.4 优先序闭合分区

按 一→二→三 的优先序把 600 段切成互斥七档：

| 档 | 段数 | 占主集 | 这一档是什么 |
|---|---:|---:|---|
| 仅刀一 | **57** | 9.50% | 只点名材料（`leak Mail account credentials` 一类里去掉 account 后） |
| 仅刀二 | **138** | 23.00% | 只说"认证／校验"这个动作 |
| 仅刀三 | **343** | 57.17% | 只说"授权／账户／权限属性"这个载体 |
| 一二共现 | **23** | 3.83% | 材料＋认证动作（`Passwords`、`Passkeys`、`Authentication Services`、`curl`） |
| 一三共现 | **16** | 2.67% | 材料＋载体（`NetAuth`、`DiskArbitration`、`MediaRemote`、`Screen Sharing Server`） |
| 二三共现 | **19** | 3.17% | 认证动作＋载体（`iCloud Document Sharing` 6、`Apple Account` 5、`AppleMobileFileIntegrity` 3） |
| 三刀同段 | **4** | 0.67% | 全部是 Mail 的同一句（见 1.6） |
| 合计 | **600** | 100% | — |

三刀相加 666 ＝ 600 ＋ 66，重叠 66 段；两两档已剔除三刀同段，所以七档互斥、相加闭合。

## 1.5 三个反向对照：本篇不取的三个词形

这三个词形都在候选清单里、都被测过、都**故意不收**，不收的理由就是本篇的三个测量结论：

| 候选词形 | 两字段段数 | ∩主集 | 收进来会怎样 | 为什么不收 |
|---|---:|---:|---|---|
| `key(s)` | **16** | **16** | 主集 **+0 段** | 已被 `keychain*` 与专名覆盖，收它只会让词表看起来更"全"，零信息增量 |
| `permission(s)／consent` | **639** | 37 | 主集涨到 **1,202（＋602）** | 这一族属于 #110／#111 的访问控制选题；收进来本篇就变成"授权与权限"合辑 |
| `access` 裸词 | **1,696** | 145 | 主集涨到 **2,057（＋1,457）** | `access` 是本系列最大的一般动词，收它等于把三篇的选题并成一篇 |

`key(s)` 那一条是本篇唯一一次"收了也不涨"的实测：**16 段全部已在主集内**，所以词族清单里它是"可安全省略"的样本。第 15.2 节给成员级核验。

## 1.6 三刀同段只有 4 段，而且是同一句话

优先序分区里最后那 4 段（页 `120898／120902／120903／120905`，组件名全是 `Mail`）内容是**逐字相同的两句**：

> Impact：An attacker with physical access may be able to leak Mail account credentials
> Description：An authentication issue was addressed with improved state management.

一句里同时出现 `credential`（刀一）、`authentication`（刀二）、`account`（刀三）——三刀同段的成因是**同一句话里三个概念被同时点名**，不是新的缺陷类别。#135 的三刀同段有 12 段、#134 有 0 段、#133 有 0 段；**本篇 4 段全部来自 4 个页面的同一句模板**，这说明"三刀同段"在本系列里始终是模板巧合而不是主题重合。

# 2 刀一：认证材料 100 段，全部写在 Impact 字段

刀一（凭据与认证材料本体）**100 段／Impact 100／Description 0**。这是本篇最干净的测量结果：**公开文本只在"某方能拿到什么"这句里点名密码、凭据、钥匙串；在"我们修了什么"那句里从不点名这些材料，只点名授权。**

## 2.1 刀一内部构成

| 词形 | Impact | Description | 并集 | 备注 |
|---|---:|---:|---:|---|
| `keychain*` | 37 | **0** | 37 | 专名 `Keychain`（大小写敏感）34 段、`System Keychain` 7 段、`iCloud Keychain` 4 段 |
| `credential(s)` | 29 | **0** | 29 | 最大的一句：`…leak Mail account credentials` |
| `password(s)` | 23 | **0** | 23 | `password autofill` 短语 9 段、`saved passwords` 2 段 |
| `passcode(s)` | 10 | **0** | 10 | `passcode entries`（解锁尝试）7 段 |
| `token(s)` | 4 | **0** | 4 | 只出现在 1 页 |
| `user name(s)` | 4 | **0** | 4 | `username` 连写与 `user name` 两形 |
| `biometric*` | 1 | **0** | 1 | 短语 `biometrics-gated` |
| `Face ID` | 1 | **0** | 1 | 就是 1.6 之外唯一的动名词打头句（见 8.3） |
| 八项相加 | **109** | **0** | 100 | 净重叠 **9** 段 |

**八个非零词形里没有一个在 Description 侧出现过一次。** 这个"材料词零修复句"的现象与 #126 的"词性随字段绑定"（bypass 在 Impact 是动词、在 Description 是名词化短语）不同：**本篇不是词性绑定，而是话题绑定——修复句的话题是"授权与状态"，后果句的话题是"谁的什么东西被拿到"。**

## 2.2 五件日常说法在公开文本里不存在

`passphrase`、`secret(s)`、`Touch ID`、`Apple Pay`、`Secure Enclave` 五个词形**两字段口径全为 0 段**（附表 A 里连"三字段"口径也是 0）。合起来的对照更清楚：

| 说法 | 两字段 | 三字段 | 中文手册层 |
|---|---:|---:|---:|
| `passphrase` | 0 | 0 | `口令` **1** 次／1 页 |
| `secret(s)` | 0 | 0 | —（顾问页无对应词） |
| `Touch ID` | 0 | 0 | `触控` **874** 次／178 页 |
| `Apple Pay` | 0 | 0 | — |
| `Secure Enclave` | 0 | 0 | `安全隔区` **682** 次／178 页 |
| `Face ID` | **1** | 1 | `面容` **674** 次／178 页 |

**同一批物件在产品手册里是满页词（触控 874、安全隔区 682、面容 674），在安全响应页里是 0–1 段。** 这不是"苹果不谈这些机制"——它谈，但只在**修复句里谈机制的效果**（`improved state management`、`additional entitlement checks`），不在**材料层面**谈机制的名字。第 13 章给完整中文层对照。

## 2.3 `passcode` 的 10 段全在两句里

`passcode(s)` 10 段、`passcode entries` 7 段。两句写完这 10 段：

| Impact 句 | 句次 | 页 | 名 |
|---|---:|---:|---:|
| A malicious app may be able to attempt passcode entries on a locked device and thereby cause escalating time delays after 4 failures | 7 | 7 | 1 |
| Passcode entries may be accepted after the device is locked | 3 | 3 | 1 |

**`passcode` 的 10 段里 7 段是同一页同一个组件名的一句**（附表 B 里该句 ∩R 0、∩E 0）。这句话**只描述公开条目写了什么**，本篇不复述、不评价任何尝试次数或延时策略，也不把它当成任何锁定策略的实现结论。

## 2.4 `Keychain` 是本篇唯一的"材料专名"

`keychain*` 词族 37 段全在 Impact；专名 `Keychain`（大小写敏感）34 段；`keychain` 小写词形 3 段只在另两形之外。`Keychain 专名` 的组件名是 `Security`（部分段）与 Mail／Passwords 等。**中文层 `钥匙串` 1,280 次／178 页满页**（最集中在"钥匙串数据保护"50 次）——**英文安全响应页用 37 段说"钥匙串能被读写"，中文手册用 1,280 次说"钥匙串如何被保护"**，这是第 13 章落差表里最直白的一行。

## 2.5 为什么 `key(s)` 不收，`authenticator` 为什么是 0

`key(s)` 两字段 **16 段**、**∩主集 16（100%）**——收进来主集一段不涨（见 1.5）。`authenticator`（认证器／验证器）**两字段与三字段都是 0 段**，`WebAuthn` 专名 5 段（全在 Description 的 `Sign in with Apple` 语境）。这两个零说明：**公开措辞不使用"认证器""口令短语"这类行业术语，只用 `password／passcode／credential／keychain` 四个日常词。**

# 3 刀二：认证与校验动作 184 段，唯一两侧都写的刀

刀二 **184 段／Impact 115／Description 111／I∩D 42**。这是三刀里唯一"跨界"的一根，也是本篇里**多义性最重**的一根（`Pointer Authentication` 混在 `authenticat*` 里）。

## 3.1 刀二内部构成

| 词形 | Impact | Description | 并集 |
|---|---:|---:|---:|
| `authenticat*` | 98 | 80 | **144**（I∩D 34） |
| `authentication` 裸词 | 93 | 80 | 144（I∩D 29） |
| `unauthenticated` | 9 | 0 | **9** |
| `verif*` | **0** | 9 | **9** |
| `certificat*` | 8 | 11 | 11（I∩D 8） |
| `cryptograph*` | **0** | 11 | **11** |
| `signature(s)` | 0 | 0 | **0** |
| `hash*／HMAC` | 0 | 0 | **0** |

**五个非零词形（`authenticat*` 144＋`unauthenticated` 9＋`verif*` 9＋`certificat*` 11＋`cryptograph*` 11）相加 184＝刀二并集 184。** 由容斥关系，相加等于并集当且仅当两两不共现——所以**这五族在同一段里从不相遇**：写"证书"的一段不写"认证"、写"密码学"的一段不写"校验"。这是本篇最反直觉的闭合之一，也说明刀二是五片互不相干的碎片、不是一个连贯主题。

## 3.2 `verif*` 与 `cryptograph*` 是纯 Description 词

`verif*` 9 段、`cryptograph*` 11 段，**Impact 侧都是 0**。它们的具体句形都在修复套话里：`A validation issue existed in the entitlement verification.`（8 段）、`A cryptographic issue was addressed…`（4 段）。

**结论：`verif*／cryptograph*` 在公开文本里不是后果词、是修复词。** 与之相对，`unauthenticated` 9 段**全是 Impact 侧**（`An unauthenticated user on the same network as a signed-in Mac could send it AirPlay commands without pairing` 7 段）。**同一个英文词根家族里，`authenticate` 两侧都写、`unauthenticated` 只写后果、`verification` 只写修复。**

## 3.3 `Pointer Authentication` 污染：17 段，摘掉之后得到"同数不同集"

`Pointer Authentication`（指针认证，硬件控制流完整性机制）**两字段 17 段、Impact 17、Description 0**，全部经 `authenticat*` 落进刀二。这 17 段的组件名是 `WebKit` 8／`dyld` 5／`RPAC` 4；页 `120896／120898／120901／120902／120903／120905／120909／120911／120912／120914` 等。

**这是一个纯多义污染**：选题里的"身份认证"和人机无关的"指针认证"共用 `authentication` 一词。本篇**保留**它并测量它的边界，理由与 #135 保留 `media*` 相同——**摘掉它不是修正，是换题**。摘除后的数字必须写成这样：

| 口径 | 段数 | 与 `authenticat*I`（98）的成员交 |
|---|---:|---:|
| 刀二 Impact（含 PA） | **115** | — |
| 刀二 Impact **摘掉 PA** | **98** | **81** |
| `authenticat*` 的 Impact 段 | **98** | **81** |

**两个 98 得数相同，但只重合 81 段**：摘掉 PA 后的刀二 Impact 里有 17 段不含 `authenticat*`（靠 `certificat*` 等进来），而 `authenticat*I` 里有 17 段不在刀二 Impact（因为它们在刀一／刀三的段里已计入）。**这是本系列"同数不同集"清单里最容易被误抄成"两者相同"的一条。**

PA 与在野／利用的关系也是反直觉的：**PA ∩在野句（`Apple is aware of a report…`，全树 103 段）= 4 段；PA ∩`actively exploited`（全树 18 段）= 0 段。** 第 15 章按成员集合给数，不看百分比。

## 3.4 `sign…in` 的无界匹配：本篇最大的一次自我污染

刀三里 `sign(?![\w-])` + 空格 + `in` 这个写法（`sign in`）如果**去掉中间的尾断言限制**、写成松匹配 `sign.{0,1}in`，会命中 `designing／design in` 一类。实测：

| 词形 | 两字段段数 |
|---|---:|
| `sign…in` **无界**写法 | **61** |
| `sign in`（带 `(?![\w-])` 正写） | **5** |
| `signed in` | 1 |
| `design*` 词族（三字段） | **0** |
| `signing` | 5 |
| `sign*` 全部 | 18 |

**61 段里只有 5 段是真正的 `sign in`，其余 56 段是 `design` 内部。** 附表 A 里 `design* 词族（sign…in 无界的真凶）` 一行显式记为 **0**（三字段口径），就是为了把这条坑留在表里。这条与 #132 的 `WebP` 松匹配 38 段全是 `webpage`、#131 的同名坑同类。

# 4 刀三：会话与授权载体 382 段，本篇的主体、也是修复句的主体

刀三 **382 段／Impact 59／Description 340／I∩D 17**。**主集 600 段里 57.17% 只由刀三决定**（仅刀三档 343 段），摘掉整根刀三只剩 **218 段**——刀三是本篇的承重墙，但它几乎全压在 Description 一侧。

## 4.1 `authoriz*` 一个词族 198 段，其中 194 段写在 Description

`authoriz*`（`authorization／authorize／authorized／unauthorized`）**198 段：Impact 11、Description 194、I∩D 7**。**裸词 `authorization` 与词族 `authoriz*` 并集相同（都是 198）**——差集是 `authorize／authorized／unauthorized` 三形共 37 段，但**这 37 段全部同时含 `authorization`**，所以并集不涨。

对应的最大句形是 `An authorization issue was addressed with improved state management.`（**159 句次**）与 `An authorization issue was addressed with improved access control.`（18）、`An authorization issue was addressed with improved validation.`（12）——**"授权"在公开文本里几乎只作为缺陷类别名出现在修复句的主语位置。**

## 4.2 `entitlement` 的 131 段与"additional entitlement checks"

`entitlement(s)` **131 段：Impact 7、Description 128、I∩D 4**。这 128 段里 **99 段是同一句** `This issue was addressed with additional entitlement checks.`——**本篇第二大树**（附表 C 里独立成桶、99 句次／60 页／29 名）。

`improved entitlements` 9 段、`…now require restricted entitlements` 2 段、`…improved validation of the process entitlement` 8 段、`addressed by removing／adding … entitlements／a delay` 合并族两字段 **274 段但∩主集只有 13 段**（其余 261 段属于 #133／#135 的内存与处理选题）。

**"权限属性（entitlement）"是苹果修复套话里最具体的机制名词：131 段里 128 段写在 Description，且 99 段共用一句话。**

## 4.3 `account(s)` 33 段全在 Impact，`session(s)` 4 段全在 Description

刀三内部同样有字段梯度：

| 词形 | Impact | Description | 并集 | 典型句 |
|---|---:|---:|---:|---|
| `account(s)` | **33** | **0** | 33 | `A local app may be able to read a persistent account identifier`（18 句次，本篇 Impact 侧 top1） |
| `authoriz*` | 11 | **194** | 198 | `An authorization issue was addressed…`（194） |
| `entitlement` | 7 | **128** | 131 | `…additional entitlement checks.`（99） |
| `session(s)` | **0** | 4 | 4 | `A session management issue was addressed…`（4） |
| `cookie(s)` | 6 | 17 | 17 | `A cookie management issue was addressed with improved state management.`（17） |
| `signed in／sign in` | 6 | 0 | 6 | `…a signed-in Mac could send it AirPlay commands…` |
| `login／log in／logout／log out` | 4 | 0 | 4 | — |
| `identity／identities` | **0** | **0** | **0** | 全零对照 |

**`account(s)` 的 33 段全部在 Impact，而 `session(s)` 的 4 段全部在 Description**——"账户"是后果句里的宾语、"会话"是修复句里的类别名。`identity／identities` 两字段与三字段**都是 0 段**：公开措辞不用"身份"这个词，只用"账户"。

## 4.4 `permission(s)／consent` 为什么不收，收了会怎样

`permission(s)` 两字段 **580** 段（Impact 57∶Description 538，I∩D 15）、`consent` **76** 段（54∶25，I∩D 3）、合并 **639** 段。∩主集 **37** 段。

并入主刀的反事实：600 → **1,202（＋602）**。**不收的理由不是量小，而是选题边界**：`permission／consent` 属于 #110／#111 的"访问控制与授权管理"选题，收进本篇会让"授权载体（authorization／entitlement）"与"权限（permission）"两族混谈。**本篇必须强调这两族在英文里是不同的词，而且是零共现**：`authoriz*` 198 段与 `permission(s)` 580 段的成员交全树 **0** 段——写了 `authorize` 的段落里一段都没有再写 `permission`；`authoriz*` 只与 `consent` 共现 **7** 段（且这 7 段全部落在主集内）。**所以那 639 段里进主集的 37 段，是 `permission`／`consent` 自己在判据字段里的出现，与 `authoriz*` 无关**（两条独立直扫都得 0 与 7，词族规模 198／580／76 互相吻合）。

# 5 词界断言第五次发作：这次是最轻的一次

#132／#133／#134／#135 四篇各有一次词界断言事故（尾断言漏 193、前置挡 162、前置挡 434 而尾 0、前置挡 488 且尾挡 9）。本篇两头都测，**都是五篇最小**。

## 5.1 去掉前置 `(?<![\w-])` → ＋32 段，全部是 `unauthorized` 内部

632 段（600→632）。把新增的 32 段里被松匹配实际命中的词形计数，结果只有一种：

> 新增段匹配词形：**`authorized` 32 次**（其他词形 0 次）

逐段核验：这 **32 段全部含 `unauthorized`**（全树 `unauthoriz*` 词形 37 段）、**一段都没有独立的 `authoriz*` 词形**。样例页 `121238／121239／121240／121246／121248／121249／121250／122066／122281`。

**成因**：`authoriz\w*` 带前置断言时，`unauthorized` 里的 `authorized` 因左边紧邻字母 `n` 而被挡；去掉断言后它作为独立子串被收。**这与 #135 的 `information` 内嵌 `format`（451 段）同构，但量小一个数量级**——因为本篇的刀里没有像 `format` 那样"藏在常见词内部"的短词。

## 5.2 去掉尾断言 `(?![\w-])` → ＋2 段，两段的真凶是 `Account-driven`

602 段（600→2）。新增 2 段（页 **`124149／124150`**）里松匹配命中的词形只有一种：**`account` 2 次**——形态是 `Account-driven`（`account` 后紧跟连字符）。**尾断言的职责就是"后面不能是字母或连字符"，所以 `Account-driven` 这类连字符复合词被挡在主集外。** 两篇对照：#132 是 `out-of-bounds` 被前置断言挡住（漏 193 段），本篇是 `Account-driven` 被尾断言挡住（挡 2 段）。

## 5.3 五篇断言账对照

| 篇 | 去前置新增 | 去尾新增 | 主凶 |
|---|---:|---:|---|
| #132 图像格式 | 0 | **193**（漏） | `out-of-bounds`／`use-after-free` 连字符形 |
| #133 资源边界 | **162** | 0 | 双错 162／166 |
| #134 媒体资源 | **434** | **0** | `management` 内部 |
| #135 音视频处理 | **488** | **9** | `information` 内嵌 `format`（451 段） |
| **本篇 #136** | **32** | **2** | `unauthorized` 内嵌 `authorized`；`Account-driven` |

**五篇五种形态，本篇是唯一"两头都轻"的一篇。** 这不是因为本篇写得更好，而是因为**认证／授权词族几乎没有内部嵌套**：`authentication`、`authorization`、`credential` 都是长词，不容易被别的词包含；唯一的例外恰好是加了 `un-` 前缀的 `unauthorized`。

## 5.4 `authenticat*` 与 `unauthenticated` 为什么分开计

刀二同时列了 `authenticat\w*` 与 `unauthenticated` 两个词形，看似重复。**在不带前置断言的口径下两者会合并（`unauthenticated` 内部的 `authenticated` 会被 `authenticat\w*` 抓到），带断言时它们是两个互斥词形。** 本篇的带断言口径实测：`authenticat*` 144 段、`unauthenticated` 9 段、二者并集 153 段——**9 段与 144 段零交**。这是 5.1 那条坑的正面用法：**词界断言把"未认证"从"认证"里干净地分离出来，本篇才能单独测出"unauthenticated 9 段全在 Impact"这一条结论。**
# 6 字段刀：600 段里 Description 443 ＞ Impact 243

本篇是本系列里少见的"修复侧多于后果侧"的一篇。**主集 600 段中，Impact 字段含三刀词的 243 段、Description 字段含三刀词的 443 段，同段两侧都含的 86 段。**

## 6.1 单侧性测量：三刀构成一条梯度

| 集 | 段 | Impact | Description | I∩D | Impact 占比 | Description 占比 |
|---|---:|---:|---:|---:|---:|---:|
| 刀一 认证材料 | **100** | 100 | **0** | **0** | **100.00%** | **0.00%** |
| 刀二 认证动作 | **184** | 115 | 111 | **42** | 62.50% | 60.33% |
| 刀三 授权载体 | **382** | 59 | 340 | 17 | **15.45%** | **89.01%** |
| 主集 AUAT | **600** | 243 | 443 | 86 | 40.50% | 73.83% |

三行构成一条单调梯度：**从"只写在后果句"到"只写在修复句"**。#126 的结论是"词性随字段绑定"（同一词的动词形在 Impact、名词化短语在 Description），#133／#134 的结论是"词族单侧绑定"（bounds／free／cache 全在 Impact，font／texture 全在 Description），**本篇的结论是"概念角色随字段绑定"：材料＝后果、授权＝修复、认证＝两者皆是。**

## 6.2 同词跨界与跨词跨界：本篇全部是同词

单根刀在自己内部同时命中 Impact 与 Description 的段，可以再分两种：**同一段里"同一个词"两边都出现**，还是"Impact 出现词 A、Description 出现词 B"。

| 刀 | I∩D | 其中同词 | 其中跨词 |
|---|---:|---:|---:|
| 刀一 | 0 | 0 | 0 |
| 刀二 | 42 | **42** | **0** |
| 刀三 | 17 | **17** | **0** |
| 主集 | 86 | **86** | **0** |

**本篇 86 个跨界段全部是"同一个词在两个字段各写一次"**，跨词 0 段。**这与 #135 正好相反**——#135 的刀一 I∩D 3 段全是跨词（`capture`↔`camera`）、刀三 19 段里 14 同词 5 跨词，当时记为"单根刀自己跨界"。**两种形态都成立，但成因不同：#135 是同一概念用两个不同名词，本篇是同一术语在后果句和修复句里被重复使用**（典型：Impact 写 `…bypass authentication policy`、Description 写 `An authentication issue was addressed…`，"authentication"两边都在）。

## 6.3 仅 Impact 与仅 Description 两个子集的刀归属

| 子集 | 段 | 刀一 | 刀二 | 刀三 | 三刀相加 | 净重叠 |
|---|---:|---:|---:|---:|---:|---:|
| 仅 Impact（157 段） | **157** | 73 | 67 | 33 | 173 | **16** |
| 仅 Description（357 段） | **357** | **0** | 64 | 301 | 365 | **8** |

两处必须记住：

- **仅 Description 的 357 段里，刀一 0 段。** 这把第 2 章的"材料词零修复句"推到极值：不仅材料词不写在 Description，连"只在 Description 命中"这一子集里都没有一段是材料词造成的。
- 仅 Description 子集里刀二 64＋刀三 301 的净重叠 **8 段**，与 1.3 节里"两句都在 Description"的 **8 段**做了成员级核验：**两者是同一批 8 段（成员交 8，差集 0∶0）**。所以"一段两句修复句"的语法成因就是"这段的 Description 里同时写了认证动作与授权载体两类词"（如 `An authentication issue was addressed with additional entitlement checks.`）。**这是本篇少见的"两处 8 真同集"，别处不得复制。**

## 6.4 词族级落位（本篇最重要的两张对照）

判据词族（三刀之内）：

| 词族 | Impact | Description | I∩D | 并集 |
|---|---:|---:|---:|---:|
| `password(s)` | 23 | 0 | 0 | 23 |
| `passcode(s)` | 10 | 0 | 0 | 10 |
| `credential(s)` | 29 | 0 | 0 | 29 |
| `token(s)` | 4 | 0 | 0 | 4 |
| `keychain*` | 37 | 0 | 0 | 37 |
| `biometric*` | 1 | 0 | 0 | 1 |
| `Face ID` | 1 | 0 | 0 | 1 |
| `user name(s)` | 4 | 0 | 0 | 4 |
| `authenticat*` | 98 | 80 | 34 | **144** |
| `authentication` 裸词 | 93 | 80 | 29 | 144 |
| `unauthenticated` | 9 | 0 | 0 | 9 |
| `verif*` | **0** | 9 | 0 | 9 |
| `certificat*` | 8 | 11 | 8 | 11 |
| `cryptograph*` | **0** | 11 | 0 | 11 |
| `authoriz*` | 11 | **194** | 7 | **198** |
| `entitlement(s)` | 7 | **128** | 4 | 131 |
| `account(s)` | 33 | 0 | 0 | 33 |
| `Apple Account` | 9 | 0 | 0 | 9 |
| `session(s)` | **0** | 4 | 0 | 4 |
| `cookie(s)` | 6 | 17 | 6 | 17 |
| `signed in` | 1 | 0 | 0 | 1 |
| `sign in` | 5 | 0 | 0 | 5 |
| `login／log in／logout／log out` | 4 | 0 | 0 | 4 |
| `identity／identities` | 0 | 0 | 0 | **0** |

判据之外的对照词族（不收进刀，只作落位证明）：

| 词族 | Impact | Description | I∩D | 并集 | ∩主集 |
|---|---:|---:|---:|---:|---:|
| `permission(s)` | 57 | **538** | 15 | 580 | 37（合并口径） |
| `consent` | 54 | 25 | 3 | 76 | （同上） |
| `privileg*` | **555** | 6 | 3 | 558 | 43 |
| `validat*` | 25 | **1,319** | 3 | 1,341 | 51 |
| `access` 裸词 | 1,470 | 310 | 84 | 1,696 | 145 |
| `memor*` | 1,136 | 1,375 | 544 | 1,967 | **0** |
| `file(s)` | 1,058 | 126 | 14 | 1,170 | **14** |
| `manag*` | 7 | **1,038** | 0 | 1,045 | **274** |
| `state management` | **0** | 662 | 0 | 662 | 270 |
| `without authentication` | 43 | 0 | 0 | 43 | **43（100%）** |
| `Pointer Authentication` | 17 | 0 | 0 | 17 | 17 |

**最后一列是本篇的"归属证明"：`manag*` 的 274 段、`state management` 的 270 段全部在主集内，而 `memor*` 的 1,967 段与主集交 0。** 换句话说：**"管理（状态）"这个词在本篇主集里 100% 存在，"内存"这个词在本篇主集里 100% 不存在。**

## 6.5 三字段并集＝两字段：致谢侧零增量的机制

刀一／刀二／刀三在致谢字段的命中 **0 段**（哨兵 `AFre($re3)`）。与 #135 的"致谢 9 段但全在主集内"不同，**本篇是致谢侧根本没有三刀词**：致谢字段是人名与机构名清单，`Authentication Services`、`Passwords` 这类**组件名**出现在 `nm` 字段而不是致谢字段。附表 A 的 `TAG 致谢（三字段）` 一行显示致谢文本里含 `tag` 一词的 94 段与主集交 5——那是"标签页"的 tag，不是致谢哨兵。

# 7 句层：85 个句子写完 694 句次

## 7.1 句库规模

| 量 | 值 |
|---|---:|
| 句次合计 | **694** |
| distinct 整句 | **85** |
| 只在 Impact 的句子 | **63**（句次 243） |
| 只在 Description 的句子 | **22**（句次 451） |
| 两侧共用句形 | **0** |
| top1 句次 | 159＝**22.91%** |
| top3 | 315＝**45.39%** |
| top5 | 351＝50.58% |
| top10 | 424＝**61.10%** |
| top20 | 511＝73.63% |
| top50 | 650＝**93.66%** |

**85 句里有 50 句只出现 1 次**（top35 之后到 top50 只涨 15 个百分点），但前 10 句已经写完六成句次。**两侧共用 0 句是本系列的稳定形态**（#135 是 191 句共用 0、#133 是共用 0），因为 Impact 与 Description 是两种文体：前者写"谁能做什么"、后者写"我们改了什么"，一句不可能同时属于两边。

## 7.2 Description 侧比 Impact 侧集中得多

| 侧 | 句次 | distinct | top1 | top3 | top5 | top10 |
|---|---:|---:|---:|---:|---:|---:|
| Impact | 243 | 63 | 18＝**7.41%** | 49＝20.16% | 66＝27.16% | 104＝**42.80%** |
| Description | 451 | 22 | 159＝**35.25%** | 315＝**69.84%** | 350＝77.61% | 405＝**89.80%** |

**修复侧 22 个句子写完 451 句次、前三句就写完七成；后果侧 63 个句子写完 243 句次、前十句只写完四成。** 这是本篇"句层"的核心不对称：**苹果可以说 63 种后果，但只会用 3 种说法解释它改了什么。**

## 7.3 前十句

Impact 侧（附表 B 摘录，句次／页／名）：

| 句 | 次 | 页 |
|---|---:|---:|
| A local app may be able to read a persistent account identifier | 18 | 8 |
| Photos in the Hidden Photos Album may be viewed without authentication | 16 | 16 |
| Private Browsing tabs may be accessed without authentication | 15 | 14 |
| Password autofill may fill in passwords after failing authentication | 9 | 9 |
| A local attacker may be able to modify the state of the Keychain | 8 | 8 |
| A local attacker may gain access to user's Keychain items | 8 | 8 |
| A malicious attacker with arbitrary read and write capability may be able to bypass Pointer Authentication | 8 | 6 |
| An attacker with a compromised intermediate certificate authority may be able to issue certificates with arbitrary extended key usages | 8 | 4 |
| A malicious app may be able to attempt passcode entries on a locked device and thereby cause escalating time delays after 4 failures | 7 | 7 |
| An attacker on the local network may be able to bypass authentication policy | 7 | 7 |

Description 侧：

| 句 | 次 | 页 | 名 |
|---|---:|---:|---:|
| An authorization issue was addressed with improved state management. | **159** | 61 | 51 |
| This issue was addressed with additional entitlement checks. | **99** | 60 | 29 |
| An authentication issue was addressed with improved state management. | **57** | 44 | 19 |
| An authorization issue was addressed with improved access control. | 18 | 8 | 4 |
| A cookie management issue was addressed with improved state management. | 17 | 17 | 1 |
| The issue was addressed with improved authentication. | 13 | 13 | 9 |
| An authorization issue was addressed with improved validation. | 12 | 12 | 8 |
| A certificate validation issue was addressed with improved certificate validation. | 11 | 8 | 6 |
| This issue was addressed with improved authentication. | 10 | 8 | 6 |
| This issue was addressed with improved entitlements. | 9 | 9 | 7 |

**Impact 侧 top1（18 句次）只占该侧 7.41%，Description 侧 top1（159）占该侧 35.25%——相差 4.75 倍。** 附表 B 里那 3 句修复句（159＋99＋57＝315）就是 45.39% 的全部来源。

## 7.4 归桶与句式形态

附表 C 的 28 个句形桶：桶句次相加 **694＝句次合计**、**未归类 0 条**；**73 条句子同时命中 ≥2 个桶**（这些句子在桶表里被重复计数，所以桶数 28 与 distinct 85 之间不能直接换算）。最大的六个桶：

| 桶 | 句次 | 段 | 页 | 名 | I∶D |
|---|---:|---:|---:|---:|---|
| 「An authorization issue was addressed…」 | **194** | 194 | 61 | 51 | **0∶194** |
| 「This issue was addressed with additional entitlement checks.」 | 99 | 99 | 60 | 29 | 0∶99 |
| 「An authentication issue was addressed…」 | 57 | 57 | 44 | 19 | 0∶57 |
| 「…without authentication」 | 43 | 43 | 33 | 11 | **43∶0** |
| 「…Keychain…」 | 37 | 37 | 27 | 8 | 37∶0 |
| 其余含三刀词句（catch-all） | 28 | 28 | 22 | 11 | 28∶0 |

**catch-all 桶只有 28 句次＝4.03%**，是本系列里最小的一次（#135 的 catch-all 初版吃掉 45% 句次）。这说明本篇的句子几乎全部能被具名桶接住。

附表 C 续表的六档句式形态（优先序闭合 694）：**「A／An X issue was addressed」255**、**「The／This issue was addressed」147**、「A／An X（非 issue 名词）was addressed」39、「X existed」8、「含 may」233、其余 12。**四档修复句式（255＋147＋39＋8＝449）与含 may 的 233 相加＝682，加其余 12＝694 闭合。**

## 7.5 句号异形：本篇只有 2 组

同一题干"带句末点号"与"不带点号"两种写法并存的可合并组，本篇 **2 组**，且两组都是**带点 1 次∶不带点 1 次**：

- `An authorization issue was addressed with improved state management`
- `An attacker with arbitrary read and write capability may be able to bypass Pointer Authentication`

#135 有 5 组、#134 有 1 组。**本篇的 2 组里最大一组只有 1∶1**，说明点号在描述句（无 may）里几乎总是带上、在 Impact 句里从不带上（Impact 句本身不带点），**两组异形都各只有 2 个实例，不构成排版系统性不一致**。

## 7.6 句子长度与打头词

| 量 | 值 |
|---|---:|
| 85 个 distinct 句的总词数 | **1,090** |
| 每句词数均值 | **12.82** |
| 中位 | 12 |
| 最短 | 6 |
| 最长 | 26 |

句首冠词之后的第一个词（按句次加权，前 12 名）：**authorization 194**／issue 147／（其他）68／authentication 57／attacker 49／local 38／app 34／malicious 27／cookie 17／certificate 11／unauthenticated 9／validation 8。

**"authorization"是全部 694 句次里最常用作句子主语的抽象名词，超过"attacker"（49）近四倍。** 后果句的主语分布（按 600 段的 Impact 字段文本）：以 `app` 打头 **286 段**、含 `local` **47 段**、含远程词（remote／network／internet／website）**57 段**、含 `physical access` **34 段**、含 `malicious` **69 段**。**注意这五个数是对 600 段的 Impact 文本测的，不是对 243 个 Impact 命中段测的**——357 段虽无三刀词命中，仍然有 Impact 句。

# 8 情态刀：243 句影响句只有一种状态

## 8.1 八档相加＝句次

| 情态 | 句次 | 占 Impact 判据句 |
|---|---:|---:|
| `may` | **233** | 95.88% |
| `could` | 9 | 3.70% |
| `can` | 1 | 0.41% |
| `might／should／will／must` | 0 | 0% |
| 相加 | **243** | ＝句次 243 |
| 无情态句次 | **0** | — |
| 两句次含两情态 | 0 | — |

**相加 243 恰等于句次 243**，即**每一个 Impact 判据句都含且只含一个情态动词**。#135 的形态是"may 2,497／can 3／might 1／should 2／零情态 9"（相加比句次多 2），**本篇连一个零情态句都没有**。

## 8.2 `could` 的 9 句次只由 2 句写成，`can` 只有 1 句

| 情态 | 句 | 次 |
|---|---|---:|
| could | An unauthenticated user on the same network as a signed-in Mac could send it AirPlay commands without pairing | 7 |
| could | A system binary could be used to fingerprint a user's Apple Account | 2 |
| can | An attacker with knowledge of a standard user's credentials can unlock another standard user's locked screen on the same Mac | 1 |

**`could` 在本篇只出现在 macOS 相关条目**（signed-in Mac、Apple Account 指纹），`can` 唯一的 1 次也来自 Mac 多用户场景。这是"情态选择与平台相关"的一个可核对样本：**iOS 条目用 may，Mac 条目偶尔用 could／can。**

## 8.3 Description 侧：451 句次情态 0

修复句用的是过去时（`was addressed`、`existed`），**451 句次里情态动词 0 个**。**这就是"情态刀不能当主集判据"的原因**：情态把 243 个 Impact 段与 443 个 Description 段完美分开（100%∶0%），它等价于"字段刀"本身，没有区分力。**反过来，"may 是否出现"可以用来验证一个段是否真有 Impact 句。**

句首形态三项（按句次）：

| 形态 | 句次 | 占比 | 与 #135 对照 |
|---|---:|---:|---|
| 动名词打头 | **1** | **0.14%** | #135：2,056 句次＝74.90% |
| 主语＋动词（含 may／is 等） | **667** | **96.11%** | #135 未测 |
| 被动（`may be` ＋ 过去分词） | 37 | 5.33% | — |

唯一的动名词打头句是：`Restoring from a backup may prevent passcode from being required immediately after Face ID enrollment`。**这 1 句同时是主集内 `Face ID`（1 段）与 `passcode` 的一部分**——**本篇 0.14% ∶#135 的 74.90%，是措辞层系列里句首形态差异最大的一对。** 成因清楚：#135 的主体句形是 `Processing a maliciously crafted … file …`（后果句以动名词起头），本篇的主体句形是 `An authorization issue was addressed …`（修复句以冠词起头）。

被动句 37 句次只由 5 句写成，前四名：`Photos in the Hidden Photos Album may be viewed without authentication` 16、`Private Browsing tabs may be accessed without authentication` 15、`An encrypted volume may be accessed by a different user without prompting for the password` 3、`A system binary could be used to fingerprint a user's Apple Account` 2。**"被动＋without authentication"是认证类条目最典型的后果句式**：不写"谁能绕过认证"，写"什么东西能在未认证情况下被读到"。

# 9 后果强度八类：相加 115＝并集 115，485 段零类

## 9.1 两套口径，十六个数字

Impact 单字段口径（第 9 章正文用）与三字段口径（附表 A 用）：

| 类 | Impact 全集 | ∩主集 | 三字段全集 | ∩主集 |
|---|---:|---:|---:|---:|
| ACE 任意代码执行 | 336 | **1** | 336 | 1 |
| BYP 绕过 | 291 | **40** | 301 | 40 |
| DIS 泄露／暴露 | 530 | 23 | 603 | 28 |
| DOS 终止／拒绝服务 | 2,003 | 11 | 2,014 | 11 |
| FULL 完全控制 | 63 | **0** | 63 | 0 |
| MEM 内存损坏 | 220 | **0** | 390 | 0 |
| PRI 权限提升 | 124 | 6 | 124 | 6 |
| SBX 沙盒 | 418 | 34 | 475 | 34 |
| **相加** | — | **115** | — | **120** |
| **并集** | — | **115** | — | **120** |

**两套口径的"相加＝并集"都成立**（Impact 115＝115、三字段 120＝120），成因见 9.2：**没有任何一段命中两类后果**。附表 A 用三字段列（120），正文用 Impact 列（115），两者都不是 600。

**PRI 在这里是"权限提升"（`elevated privileges`），不是隐私**；隐私指示器另有专名集 `Privacy Indicator(s)` **全树 4 段、∩主集 0**。#135 曾把 PRI 写作"隐私指示器"，本篇按 `%CLS` 定义把标签改正为权限提升类，并说明该命名坑。

## 9.2 类数分布：485 段不带任何后果词

| 命中类数 | 段 | 占主集 |
|---:|---:|---:|
| 0 类 | **485** | **80.83%** |
| 1 类 | 115 | 19.17% |
| ≥2 类 | **0** | 0% |

**主集与"同段 ≥2 后果类"集 M2（全树 185 段）交 0 段。** 这是措辞层系列里第一次出现"零多标签"——#135 有 153 段 ≥2 类、#133 也有多标签段。

**485 段（80.83%）不带后果类词**意味着：**认证与授权类条目的 Impact 句只说"能读到／能看到／能访问"，不说"导致代码执行／内存损坏"。** 这从词族侧独立得到印证：`arbitrary code execution` 两字段 218 段、∩主集 **0**；`improved memory handling` 1,231 段、∩主集 **0**；`memor*` 1,967 段、∩主集 **0**；`out of bounds` 两形 816 段、∩主集 **0**；`use-after-free` 连字符形 193、空格形 127，∩主集**都是 0**；`maliciously crafted` 1,769 段、∩主集只有 **14**。

## 9.3 单类段的类别构成与两两共现

115 个单类段的分布：BYP 40、SBX 34、DIS 23、DOS 11、PRI 6、ACE 1、MEM 0、FULL 0。

**BYP 40 段与 #126 绕过集 ∩主集的 40 段做了成员级核验：两者是同一批 40 段（交 40、双向差集 0）。** 所以本篇可以合法写"**主集里的绕过后果段 ＝ #126 绕过集与主集的交**"，这是全系列少见的真同集。

**BYP 40 段与 DIS 23 段共现 0 段**（这是"零多标签"的又一个侧面，不是巧合）。#132 的教训在此必须复述：**`R∩主集` 与 `disclos*∩主集` 得数相同却完全不相交**——本篇凡是"得数相同"处都用成员集合验过：BYP∶BP 同集、`#134∩主集` 与 `manag*∩主集` 同集（274∶274 交 274）、`刀二I摘PA` 与 `authenticat*I` **不同集**（98∶98 只交 81）。

## 9.4 为什么"相加"不能当段数用

八类相加在别的篇里必然大于并集（因为一段可以命中多类），本篇相等是**结果**不是**规则**。写成"八类共 115 段"必须同时写"相加也是 115、多标签段 0"，否则读者会把这条经验复制到下一篇。**判据：只要 ∩M2 ＝ 0，才允许相加＝并集。**

# 10 套话刀：修复短语的两种命运

本篇把修复侧短语分成两类命运：**"全部落在主集内"的专属于认证授权选题的短语**，和"巨大但与主集无关"的通用内存／校验短语。

## 10.1 十个"100% 落在主集内"的短语

| 短语 | 两字段 | ∩主集 | 占比 |
|---|---:|---:|---:|
| `additional entitlement checks` | 99 | **99** | **100%** |
| `improved entitlements` | 9 | 9 | 100% |
| `improved authentication` | 23 | 23 | 100% |
| `constant-time` | 7 | 7 | 100% |
| `delay between verification` | 1 | 1 | 100% |
| `passcode(s)` | 10 | 10 | 100% |
| `Face ID` | 1 | 1 | 100% |
| `keychain*` | 37 | 37 | 100% |
| `without authentication` | 43 | 43 | 100% |
| `without (user) authorization` | 10 | 10 | 100% |

**这十个短语是全库"认证／授权词汇专用表"**：它们出现的每一段都被本篇抓进主集，无一例外。**`additional entitlement checks` 的 99 段全部在主集内**——这是本篇最干净的一条归属证明，也是第 4.2 节"entitlement 是修复句里最具体的机制名词"的量化形式。

## 10.2 三个"很大但与主集几乎无关"的短语

| 短语 | 两字段 | ∩主集 | 占该短语 |
|---|---:|---:|---:|
| `improved memory handling` | **1,231** | **0** | 0.00% |
| `arbitrary code execution` | 218 | **0** | 0.00% |
| `improved bounds checking` | 699 | **0** | 0.00% |
| `improved checks` | 1,018 | 27 | 2.65% |
| `improved input validation` | 641 | 13 | 2.03% |
| `maliciously crafted` | 1,769 | 14 | 0.79% |
| `improved validation` | 367 | 22 | 5.99% |
| `improved state management` | **662** | **270** | **40.79%** |
| `improved access control` | 21 | 18 | 85.71% |
| `addressed by removing` | 215 | 12 | 5.58% |

**`improved state management` 是唯一的"既大又属于本篇"的短语：662 段里 270 段在主集内（40.79%）。** 这 270 段里 **90 段的 Impact 字段本身也命中三刀**，另 180 段只在 Description 命中。它与姊妹集 #134 的关系见 15.3。

## 10.3 修复动词与检查词族（全库层）

| 判据 | 两字段／三字段 | ∩主集 |
|---|---:|---:|
| `addressed with`（全形） | 6,589 | 554 |
| `addressed by` | 371 | 17 |
| `addressed through` | 247 | 19 |
| `check(s)／checking` 词族 | 2,017 | 142 |
| `validation` 词族 | 1,341 | 51 |
| `restrict*` 词族 | 793 | 43 |
| `sanitiz*` 词族 | 93 | **0** |
| `enforce*` 词族 | 53 | **0** |
| `The issue was addressed with improved checks.` | 514 | 18 |

**`sanitiz*` 与 `enforce*` 两族在主集内 0 段**——本选题的修复句不用"净化／强制执行"这两个动词。600 段里 554 段用了 `addressed with`（92.33%），是修复套话的骨架。

## 10.4 「无前提句」：`without` 家族三种命运

| 短语 | 全集 | ∩主集 |
|---|---:|---:|
| `without authentication` | 43 | **43（100%）** |
| `without prompting` | 19 | 3 |
| `without user permission` | 9 | **0** |
| `without user interaction` | **0** | 0 |
| `without` 词界集 WO（全树 178） | 178 | **92** |

**"无需用户许可"（`without user permission`）这个说法在主集内一段都没有，`without user interaction` 在全树都是 0 段。** 主集里"无前提"的表达方式是 **`without authentication`**（43 段、100% 落在主集）——**在认证类条目里，"不提示用户"这件事是用"不经认证"来说的。** **∩WO 92 段的正确读法是按 WO 那一侧除**：WO（无前提集）全树只有 **178** 段，本篇一次抓到 **92 段＝占 WO 的 51.69%**——**苹果用来写"不需要用户动作"的那批条目，有一半是认证／授权类条目**；反过来按主集除只有 **15.33%**（92／600）。**同一个 92 换分母就是两个完全不同的结论。**

# 11 页面分布：159 页、八档、Top10 只占三成

## 11.1 八档闭合

主集 600 段落在 **159** 页（全树 257 页）。按"该页含多少条本篇条目"分箱：

| 本篇段数 | 页 | 累计段 |
|---:|---:|---:|
| 1 | **67** | 67 |
| 2 | 27 | 54 |
| 3 | 16 | 48 |
| 4 | 9 | 36 |
| 5 | 13 | 65 |
| 6–9 | 15 | 90–135 |
| 10–19 | 8 | 80–152 |
| 20–49 | 4 | 88–196 |
| **合计** | **159** | **600** |

**42.14% 的页面只有 1 条本篇条目；每页平均 3.77 条**（600／159）。**没有一页超过 49 条**——#135 的最大页有 400 多条，本篇最集中的页是 `149035` 的 34 条。

## 11.2 Top10 页只占 32.17%

| 页 | 本篇段 | 页 | 本篇段 |
|---|---:|---|---:|
| 149035 | **34** | 149037 | 14 |
| 149034 | 28 | 122371 | 13 |
| 149043 | 23 | 128067 | 13 |
| 149042 | 22 | 126794 | 10 |
| 149038 | 17 | 149041 | 10 |
| 149036 | 15 | 122378 | 9 |

前 10 页合计 **193 段＝32.17%**。**前五页（149034–149043 系列）合计 124 段＝20.67%**，这些是 macOS Ventura 的批量发布页。**头部集中度远低于 #135（Top10 页占 40% 以上）**：本篇的分布更平。

## 11.3 全本篇页 8 页：全是单段页

"该页所有段都被本篇抓到"的页面 **8 页**：`120303`[Bluetooth]、`120907`[Bluetooth]、`123356`[App Store Connect]、`124156`[Apple Music Classical]、`125693`[Compressor]、`148170／148171／148172`[Screen Sharing]。**8 页每页都只有 1 段**——所以"全本篇页"这个指标在本篇里等价于"单段页的一个子集"，不能读成"这些页只讲认证"。

## 11.4 同页共存

这 159 页在全树共 **8,040** 段，本篇 600 段占该页集 **7.46%**（第三套分母）。**换句话说：读到一条认证／授权条目时，同一页上还有约 13 条别的选题的条目**（50.6 ∶3.77）。任何"某页讨论认证"的结论都必须按段而不是按页给出。

# 12 致谢、编号、版式与在野

## 12.1 致谢字段：命中 0、并集零增量

- 三刀在致谢字段命中 **0** 段（哨兵 `AFre($re3)`）
- 三字段并集 ＝ 两字段主集 ＝ **600 段／159 页／119 名**（逐值相同）

**这是 #127 设立致谢哨兵以来第二次"零增量"**（第一次是 #135，但 #135 的致谢侧命中 9 段只是全在主集内；本篇是命中 0 段）。**致谢哨兵"没报警"不等于"不用测"**：#135 的教训是宽松词表会在人名里造出假阳性（`CodeColorist`），本篇之所以干净，是因为三刀全是普通名词、不可能出现在人名里。**换一篇如果词表含专名（如 `Libsystem`、`CodeColorist` 一类），零增量就会立刻变成假阳性。**

## 12.2 编号与致谢人数

| 项 | 值 | 全树对照 |
|---|---:|---|
| 带编号段 | **599／600** | — |
| 编号数分箱 | 0 个 **1**／1 个 590／2 个 **9** | 三档闭合 600 |
| distinct 编号 | **188** | 全树约 5,900 |
| 本集独占编号 | **188** | 与他集共享 **0** |
| 有致谢段 | **599／600** | — |
| 致谢人次 | **1,073** | — |
| 每段均值 | **1.79** 人 | 全树 1.80 |
| 单段最大 | **15** 人 | 全树 37 |
| 人数分箱 | 1 人 349／2 人 138／3 人 47／4 人 48／5 人 6／6–8 人 10／9+ 人 1 | 合计 599 |

**188 个编号 100% 由本篇独占（共享 0）**，这与 #135 的形态相同：**认证类条目的 CVE 不与内存／处理类条目共用同一段**。**致谢均值 1.79 与全树 1.80 几乎相同，单段最大 15 远低于全树 37**——本篇没有"几十人致谢"的批量研究页。

## 12.3 版式域：域 A 与域 B 同比例

域 B（11 页粗体版式，共 91 段）里本篇 **6 段**：

| 页 | 组件名 |
|---|---|
| 120303 | Bluetooth |
| 120304 | Reset Services |
| 120304 | CoreCrypto |
| 120307 | LoginWindow |
| 120309 | CoreCrypto |
| 120311 | CoreCrypto |

域 B **6／91＝6.59%**、域 A **594／9,045＝6.57%**。**两域占比差 0.02 个百分点，是本系列第一次"版式无关"。** 6 段里 **3 段的组件名是 `CoreCrypto`**——粗体版式的这批 2021 年初条目里，密码学／认证类占了不成比例的 3 段（域 B 全 91 段）。

## 12.4 在野与外部锚点

| 集 | 自身 | ∩主集 | 占主集 |
|---|---:|---:|---:|
| R 远程可达 | 843 | **64** | 10.67% |
| E `exploit*` | 108 | 13 | 2.17% |
| WO `without` | 178 | **92** | 15.33% |
| UU Impact 含 `user` | 1,627 | **231** | 38.50% |
| G 动名词开头 | 2,179 | **6** | 1.00% |
| HI 非处理类 | 273 | 1 | 0.17% |
| M2 ≥2 后果类 | 185 | **0** | 0.00% |
| RC 竞态 | 232 | 5 | 0.83% |
| L 逻辑 | 550 | 19 | 3.17% |
| BP 绕过 | 301 | **40** | 6.67% |
| Ds 拒绝服务 | 470 | 5 | 0.83% |
| X 泄露 | 427 | 5 | 0.83% |
| CK 修复词族 | 4,146 | **236** | 39.33% |

**∩UU 231 段＝主集的 38.50%**：**每五段认证／授权条目里有两段的 Impact 提到 `user`**——这与"材料词全在 Impact"（第 2 章）合成一句：**这批条目的后果句主语是用户／应用，不是攻击者技术。**

**∩G 只有 6 段（1.00%）**：动名词打头的段几乎不进主集，与 8.3 的句层结论（0.14% 句次）互为段级／句级两种口径。**∩M2 ＝ 0** 已在 9.2 给出成员级解释。**∩CK 236／600＝39.33%**：主集四成段落用了修复动词词族里的词，与 10.3 的 `addressed with` 554 段口径不同（CK 词族更宽）。

在野侧：`Apple is aware of a report…` 全树 **103** 段、∩主集 **13**；`actively exploited` 全树 **18** 段、∩主集 **5**。**13 段在野句里 4 段来自 `Pointer Authentication`**（见 3.3）——**扣除 PA 后，真正的"认证／凭据类在野条目"只有 9 段。** 本篇按成员集合报数，不用百分比推论。
# 13 中文层：63 词、14 个零，与英文判据层第五次错开

把《Apple 平台安全保护》zh-CN 全量转储（窗口切分后 **178 页可用**）按附表 E 的八组 **63 个中文词**（组容量 8＋8＋7＋8＋8＋8＋8＋8）统计，得到 **全零 14 词、非零 49 词**。本篇的中文层结论与英文判据层的关系，是措辞层系列第五次出现"两层密度对不上"，而且这次出现了**全篇最极端的一对**：`signature(s)` 判据字段 **0 段**，中文 `签名` **1,287 次／178 页（满页）**。

## 13.1 14 个全零词分两类

- **认证说法类 9 个零**：`核验`、`双次认证`、`多因素`、`通行码`、`凭据`、`密码自动填充`、`帐号`、`注销`、`Cookie`。
  - 英文侧对应词都不是零：`passcode(s)` 两字段 **10 段**（全部落进主集）、`credential*` **29 段**、autofill 修饰的 `password` **9 段**、`cookie(s)` **17 段**。**中文手册不用这些说法**——它写 `密码`（2,790 次）而不写 `通行码`／`凭据`，`Cookie` 这个词在 178 页手册里出现 **0 次**，而英文判据里 `cookie(s)` 有 **17 段**。更有意思的是英文侧自己：**`persistent account identifier`（跟踪标识符）两字段 18 段、18 段全部进主集，与 `cookie(s)` 的 17 段成员交 ＝ 0**——**同一件事（跨站跟踪）在判据字段里有两套完全不重叠的说法**（第 15.2 节）。
  - `帐号`（异体）0 而 `账户` 1,218／178 页、`账号` 1 次——**同一个词有三种写法，手册统一用 `账户`**。
- **漏洞文体类 5 个零**：`证书校验`、`任意代码`、`附加验证`、`已修复`、`状态管理`。
  - 这五个零证明**中文手册不是缺陷披露文本**。特别是 `状态管理` **0 次**，而它正是本篇英文侧最大的单一锚点：`improved state management` 全树 **662 段**、其中 **270 段**属于本篇主集（第 10.1 节）。**中文层对同一概念一个字节都没有。**

## 13.2 满页词与零头词：同一概念两层差三个数量级

| 概念 | 英文判据（两字段） | 中文手册（次数／覆盖页） |
|---|---:|---:|
| 签名 | `signature(s)` **0** 段 | `签名` **1,287** 次／178 页（最集中：Mac 启动磁盘策略 32 次） |
| 加密 | `cryptograph*` **11** 段（全 Description） | `加密` **1,763** 次／178 页（安全隔区 71 次） |
| 密码 | `password` **23** 段 | `密码` **2,790** 次／178 页（密码监视 66 次） |
| 钥匙串 | `keychain*` **37** 段 | `钥匙串` **1,280** 次／178 页（钥匙串数据保护 50 次） |
| 账户 | `account(s)` **33** 段（全 Impact） | `账户` **1,218** 次／178 页 |
| 授权 | `authoriz*` **198** 段 | `授权` **352** 次／178 页 |
| 认证 | `authenticat*` **144** 段 | `认证` **380** 次／93 页（52.2%，安全隔区 29 次） |
| 验证 | `verif*` **9** 段（全 Description） | `验证` **432** 次／178 页 |
| 生物识别 | `biometric*` **1** 段 | `生物识别` **400** 次／178 页 |
| 面容 | `Face ID` **1** 段 | `面容` **674** 次／178 页 |
| 会话 | `session(s)` **4** 段（全 Description） | `会话` **78** 次／**20** 页 |
| Cookie | `cookie(s)` **17** 段 | `Cookie` **0** 次 |

**读法警告：这张表不能用来"互推"。** 中文 `认证` 只覆盖 **93／178 页（52.2%）**、`会话` 只覆盖 20 页——这两行是**手册文体**的分布；英文 `authenticat*` 144 段、`session(s)` 4 段是**披露文体**的分布。它们计量的是不同文本、不同字段、不同段落定义。

## 13.3 `身份验证` 5 次／3 页：主刀词的中文对译几乎不存在

附表 E 里 `身份验证` 只有 **5 次、3 页**（最集中页是"Apple 钱包中的证件"3 次）。而本篇英文主刀 `authenticat*` 有 **144 段**。**中文手册表达同一概念用的是 `认证`（380 次）与 `验证`（432 次）两个分开的词**，`身份验证` 这个直译在 178 页里几乎是生僻词。这是"术语对译"层面的实测：**别拿中文词频当英文判据词的对应量。**

## 13.4 一词多义把中文层拉偏：`凭证` 526 次不等于 credential

`凭证` **526 次／178 页**（最集中 NFC 与 SE 平台安全性 24 次），而英文 `credential*` 只有 **29 段**。**两者不是对译关系**：中文 `凭证` 同时承担 credential／certificate／receipt 三种含义，`凭据`（credential 的另一种译法）**全零**。所以本篇不给"credential ≈ 凭证"这类换算，只报两个独立测量值。同理 `会话` 78 次里最集中的页面是"使用卫星收发紧急信息"（11 次）——**与认证语境无关**，而英文 `session(s)` 的 4 段全部写在 Description 侧。

## 13.5 后果与修复词只存在于英文判据层

后果强度类八词全低：`任意代码`0、`代码执行`3、`内存损坏`4、`拒绝服务`1、`泄露`30、`崩溃`6、`终止`13、`冒充`1。**对照英文侧八类后果在主集命中并集 115 段（第 9 章）**——中文手册不写后果句，因为它是"这台设备如何保护你"的说明文体。修复套话类同样低：`改进的`3、`附加验证`0、`已修复`0、`状态管理`0、`约束`1、`校验`19、`新增`15、`移除`104。**英文侧 `addressed with` 全树 6,589 段、∩主集 554 段。**

小规模词也不能与英文集互推：`特权` **9 次／3 页** vs PRI 类（`elevated privileges` 后果）**全树 124 段**；`访问控制` **16 次／9 页** vs `improved access control` **全树 21 段／∩主集 18 段**。**数字接近纯属巧合，两侧集合成员无法核对。**

# 14 组件层：119 个名字扛 600 段，头部五名只占 18.67%

## 14.1 头部不集中：这是"广撒"型主题

附表 F 按组件名（`nm`）聚合，主集 **600 段落在 119 个组件名下**：

| 排名 | 组件名 | 本篇段 | 页 | 该组件含 Impact 全集 | AUAT 占比 |
|---:|---|---:|---:|---:|---:|
| 1 | Security | **31** | 21 | 91 | 34.1% |
| 2 | WebKit | **28** | 26 | 910 | **3.1%** |
| 3 | Accounts | **20** | 20 | 34 | 58.8% |
| 4 | Kernel | **19** | 19 | 681 | **2.8%** |
| 5 | AirPlay | **14** | 7 | 77 | 18.2% |
| 6 | Authentication Services | **14** | 9 | 18 | **77.8%** |
| 7 | Shortcuts | 14 | 13 | 129 | 10.9% |
| 8 | Accessibility | 13 | 13 | 46 | 28.3% |
| 9 | NetworkExtension | 13 | 13 | 28 | 46.4% |
| 10 | Siri | 12 | 11 | 116 | 10.3% |
| 11 | Safari Private Browsing | 11 | 10 | 16 | **68.8%** |
| 12 | App Store | 10 | 10 | 24 | 41.7% |
| 13 | Apple Account | 10 | 9 | 25 | 40.0% |
| 14 | Assets | 10 | 10 | 16 | 62.5% |
| 15 | AppleMobileFileIntegrity | 9 | 9 | 146 | 6.2% |

**top5 合计 112 段＝主集的 18.67%、top10 合计 178 段＝29.67%，另有 19 个名只有 1 段。** 这与 #135 的形态完全相反（那篇 top2 就占 36.17%、top10 占 55.69%）。**认证／凭据／授权类条目是横穿所有组件的：没有哪个框架独占它，也没有哪个框架完全缺席。**

**同一个组件名列里，占比两极分化**：`Authentication Services` 的 18 段含 Impact 全集里 **77.8%** 进本篇，`WebKit` 的 910 段里只有 **3.1%** 进本篇。**占比这一列本身就是判据词族的"归属度"，不是组件重要性。**

## 14.2 组件名代理：最高命中率也只有 36.36%

用"组件名含某类关键字"的代理集去逼近主集：

| 代理集（按 `nm` 关键字） | 全树段 | ∩主集 | 占该代理 | 占主集 |
|---|---:|---:|---:|---:|
| 认证类名（`Auth*`） | 88 | **32** | **36.36%** | 5.33% |
| Security 类名 | 202 | 66 | 32.67% | 11.00% |
| Account 类名 | 101 | 30 | 29.70% | 5.00% |
| 网络类名 | 340 | 45 | 13.24% | 7.50% |
| WebKit／Safari 名 | 1,312 | 47 | **3.58%** | 7.83% |
| Kernel 名 | 745 | 19 | **2.55%** | 3.17% |

**六个代理集没有一个命中率过半**，最像的"组件名带 Auth"也只有 **32／88＝36.36%**；把认证类名与 Security 类名并起来（全树 253 段）也只抓到 **87 段＝主集的 14.50%**。**主集 600 段里 513 段（85.50%）的组件名与"认证"字面无关**——`Mail`、`curl`、`Spotlight`、`CoreMotion`、`Power Services` 这些名字下都在写认证／授权措辞。

## 14.3 词族落在哪些组件名上：`keychain` 37 段有 20 段挂在 `Security`

把主刀词族的段按组件名再切一次（实测，附表 F 的口径是"该名的全部本篇段"，这里只统计"该词族命中且属主集"的段）：

| 词族 | 主集内段 | 涉及组件名数 | 头部落位 |
|---|---:|---:|---|
| `keychain*` | 37 | **8** | Security 20／MediaRemote 5／Accounts 3／Authentication Services 3／AppleMobileFileIntegrity 2／configd 2 |
| `credential*` | 29 | 8 | Authentication Services 8／curl 7／Mail 4／NetAuth 3／Screen Sharing 3／Screen Sharing Server 2 |
| `account(s)` | 33 | 9 | AuthKit 8／App Store 6／Apple Account 5／CloudKit 4／Mail 4 |
| `password*` | 23 | 7 | Authentication Services 6／FaceTime 5／Passkeys 4／DiskArbitration 3／Passwords 2 |
| `entitlement` | 131 | **38** | Power Services 8／Assets 7／CoreServices 6／NetworkExtension 6／iCloud Document Sharing 5 |
| `authoriz*` | 198 | **52** | Accounts 12／DeviceCheck 8／Spotlight 8／CoreMotion 7／MediaRemote 7／NetworkExtension 7 |

**两条读数：**

1. **`keychain*` 词族的 37 段分散在 8 个组件名、其中 20 段挂在 `Security` 名下**，而组件名里叫 `Keychain Access` 的只有 **3 段**、叫 `Keychains` 的**全树 0 段**。**"钥匙串措辞"与"钥匙串组件"不是同一个索引**——想用组件名找钥匙串条目会漏掉八成以上。
2. **`authoriz*` 198 段摊到 52 个组件名、`entitlement` 131 段摊到 38 个**——授权类措辞是全树分布的"横切面"；而 `keychain*`／`credential*`／`account`／`password` 四族各自只落在 **7–9 个**名字里，是**纵切面**。**同一根刀内部有两种散布形态，用组件名做聚类只能抓住后者。**

## 14.4 专名（`nm` 大小写敏感）与全树不存在的名字

| 专名 `nm` | 全树段 | ∩主集 | 页 |
|---|---:|---:|---:|
| Accounts | 48 | **20** | 40 |
| Authentication Services | 18 | **14** | 13 |
| AuthKit | 22 | 8 | 22 |
| Passkeys | 5 | 4 | 5 |
| Screen Sharing | 4 | 3 | 4 |
| Touch Bar | 4 | **4** | 4 |
| Passwords | 25 | 4 | 22 |
| Wi-Fi | 79 | **1** | 55 |
| Safari | 142 | 3 | 81 |
| WebKit | 1,007 | 28 | 155 |
| Libsystem | 2 | 0 | 2 |
| **Keychains** | **0** | 0 | 0 |
| **Faces** | **0** | 0 | 0 |
| **Apple Pay** | **0** | 0 | 0 |
| **Kerberos** | **0** | 0 | 0 |
| **CoreAuthentication** | **0** | 0 | 0 |

**口径注（重要）**：本表"全树段"是**含该名的全部块**（不要求 Impact 非空），附表 F 的"该组件含 Impact 全集"只统计 **Impact 非空块**——`WebKit` 两处分别 **1,007** 与 **910**，差 97 段就是 Impact 字段为空的那批条目。**两个口径不能混用，本篇每次引用都点名。**

**五个"最应该存在"的认证专名组件全树 0 段**：`Keychains`、`Faces`、`Apple Pay`、`Kerberos`、`CoreAuthentication`。**苹果安全响应页没有以这些命名的组件条目**；而 `Kerberos`／`Apple Pay` 作为判据词（小写或大小写敏感）在第 2 章同样是 **0 段**。**认证类条目不集中在"认证框架"名下**——这是 14.2 的专名级复核。`Wi-Fi` 名下 79 段只有 **1 段**进主集（1.27%），说明名字里带网络认证味道的组件与本篇几乎无关。

# 15 同数不同集：成员级核验

凡"A 与 B 得数相同／A 就是 B／A ⊂ B"的说法，本篇一律用成员集合做交集并同时报 `|A|、|B|、|A∩B|`。

## 15.1 三组必须并列摆出的成员级事实

| 甲集 | 乙集 | \|甲\| | \|乙\| | \|甲∩乙\| | 关系 |
|---|---|---:|---:|---:|---|
| 刀二 Impact 摘掉 PA 后 | `authenticat*` 的 Impact 段 | 98 | 98 | **81** | **同数不同集**（双向差各 17） |
| `BYP` 后果类∩主集 | `bypass` 词族∩主集 | 40 | 40 | **40** | **真同集** |
| `manag*` 词族∩主集 | `RESM`（#134）∩主集 | 274 | 274 | **274** | **真同集** |
| #134 去掉 `manag*` 后 | 本篇主集 | 675 | 600 | **0** | **完全不相交** |

- **98 对 98、交 81**：刀二的 Impact 侧 115 段里含 `Pointer Authentication` 17 段；把这 17 段摘掉得 98 段，而 `authenticat*` 一词的 Impact 段也是 98 段——**但两批段只有 81 段重合，各自独有 17 段**。得数相同是三刀词表与 PA 扣除的算术巧合，不是同一批条目（第 3.3 节）。
- **40 对 40、交 40（同集）**：主集里被 `bypass` 命中的 40 段，与被打上 BYP 后果类的 40 段**逐段相同**。这是本篇在**"后果类口径 vs 词族口径"两种数法之间**唯一的完全同集案例，也是把 #126（BP 篇）与本篇连起来的唯一一根线。
- **274 对 274、交 274，而 #134 自己摘掉 `manag*` 后与本篇交 0**：**本篇与 #134 的 45.67% 重合完全由一句修复套话（`improved state management`）制造**。#134 的媒体资源刀剩 675 段时，与本篇 600 段**一段都不相交**。跨篇"主题相邻"的判断必须能指到具体短语，否则就是同一句话造成的假关联。

## 15.2 真零交：内存／边界／执行类词与本篇无关

| 集 | 全树段 | ∩主集 |
|---|---:|---:|
| `memor*` 词族 | 1,967 | **0** |
| `out of bounds` 两形 | 816 | **0** |
| `use after free`（连字符形／空格形） | 193／127 | **0／0** |
| `arbitrary code execution` | 218 | **0** |
| `improved memory handling` | 1,231 | **0** |
| `Privacy Indicator(s)` 专名 | 4 | **0** |
| `without user permission` | 9 | **0** |
| `design*`（`sign…in` 无界的真凶） | 三字段 **0** | — |

**`arbitrary code execution` 218 段与本篇 600 段交 0**——**认证类条目的后果句不写"任意代码执行"**，它写 `leak … credentials`／`access … Keychain items`／`bypass … authentication`。这是第 2.2 节"五件日常说法全 0"的集合级复核，也是本篇与 #98／#109 那条主线最干净的分离证明。`Privacy Indicator(s)` 4 段全落主集之外（第 4.4 节 `permission／consent` 的 639∩37 也不含它们）。

**本篇另有两个"族对族零交"，它们比"与主集零交"更值得记：同一件事用了两套互不相见的词。**

| 甲族 | 乙族 | \|甲\| | \|乙\| | \|甲∩乙\| | 说明 |
|---|---|---:|---:|---:|---|
| `authoriz*` | `permission(s)` | 198 | 580 | **0** | 写 `authorize` 的段落从不写 `permission`；`authoriz*` 只与 `consent` 交 **7** 段（7 段全在主集内） |
| `cookie(s)` | `persistent account identifier` | 17 | 18 | **0** | 两族各自 100%／近乎 100% 落在主集内，却是两批完全不同的条目 |

**第一条把"授权载体（authorization／entitlement）"与"权限（permission）"这两个中文里常被混译的概念，在英文判据层证明成两套词**；第二条说明跟踪类条目写"持久账户标识符"、Cookie 类条目写"cookie"，**谁也不提对方**。**这两条都是"读中文会读成一个词、读英文发现是两批段"的实例。**

## 15.3 姊妹集与外部集的成员级落位

| 集 | 全集段 | ∩主集 | 占主集 | 占该集 |
|---|---:|---:|---:|---:|
| #134 RESM 媒体资源管理 | 1,541 | **274** | **45.67%** | 17.78% |
| #128 REM 远程通道 | 849 | **65** | 10.83% | 7.66% |
| #126 BP 绕过 | 301 | **40** | 6.67% | **13.29%** |
| #131 WEB 网页浏览器 | 1,253 | 47 | 7.83% | 3.75% |
| #135 AVMP 音视频处理 | 2,715 | 41 | 6.83% | 1.51% |
| #132 IMGX 图像格式 | 1,543 | 25 | 4.17% | 1.62% |
| #123 LOGIC 逻辑缺陷 | 550 | 19 | 3.17% | 3.45% |
| #133 RESX 资源边界 | 2,791 | 23 | 3.83% | **0.82%** |
| #125 DIS 泄露 | 427 | 5 | 0.83% | 1.17% |
| #122 RC 竞态 | 232 | 5 | 0.83% | 2.16% |
| #124 DoS 拒绝服务 | 470 | 5 | 0.83% | 1.06% |
| #129 PMS 解析与脚本 | 2,213 | 5 | 0.83% | 0.23% |
| #130 TLO 类型生命周期 | 343 | **0** | 0.00% | 0.00% |

**另四组本篇专用对照**：`permission／consent` 词族 639 段 ∩主集 **37**；`file(s)` 1,170 段 ∩主集 **14**；`exfiltrat*` 51 段 ∩主集 **6**；致谢侧 `Threat Analysis Group` 机构名 94 段 ∩主集 **5**（这 5 段是判据字段本身就命中主刀、机构名只在致谢出现，不是致谢驱动）。

**五篇（#132–#136）并集 5,069 段＝全树 9,136 的 55.48%；#134／#135／#136 三篇并集 3,790 段＝41.48%。** 本篇与所有姊妹的最大重合是 #134 的 274 段，而 15.1 已证明这 274 段是**一句套话**的产物；除此之外，本篇与 #133（23）、#132（25）、#129（5）、#130（0）的重合都低于本篇的 5%。**措辞层系列的"资源—处理"半区已经过半，但认证／授权半区是一块与它们几乎正交的新地**（`memor*`／`out of bounds`／UAF／ACE 四组全为 0）。

## 15.4 反事实：换词表会得到什么

| 反事实口径 | 主集段数 | 相对 600 |
|---|---:|---:|
| 并入 `permission`／`consent` | 1,202 | ＋602 |
| 并入 `access` | 2,057 | ＋1,457 |
| 并入 `validat*` | 1,890 | ＋1,290 |
| 并入 `file(s)` | 1,756 | ＋1,156 |
| 并入 `privileg*` | 1,115 | ＋515 |
| 并入 `bypass*` | 861 | ＋261 |
| 并入 `iCloud` | 615 | ＋15 |
| 并入 `pair*` | 613 | ＋13 |
| 并入 `encrypt`／`decrypt` | 600 | **＋0** |
| 并入 `memor*` | 2,567 | ＋1,967 |
| 刀二摘 `authenticat*` | 493 | −107 |
| 刀三摘 `authoriz*` | 414 | −186 |
| 摘刀一（剔所有含刀一词的段） | 500 | −100 |
| 摘刀二 | 416 | −184 |
| 摘刀三 | 218 | −382 |
| 去掉前置 `(?<![\w-])` | 632 | ＋32 |
| 去掉尾断言 `(?![\w-])` | 602 | ＋2 |

**三条读数：**

1. **`encrypt`／`decrypt` 并进主刀增量为 0**——这十一个段落（`cryptograph*` 11 段全在 Description）已被刀二完全吃掉，**加密词族不构成独立的一根刀**。
2. **"摘刀"口径必须点名**：本篇的"摘刀一 500"＝**从主集里剔除所有被刀一命中的段**（100 段，含与刀二／刀三共现的 43 段）。若只剔"仅刀一"的 57 段，应剩 **543**。**两数差 43，正是共现段数**；引用时必须写清是哪一种（第 1.4 节七档表用的是后者口径）。
3. **并词膨胀远大于摘刀收缩**：并 `access` 会涨到 2,057 段（涨 3.4 倍）、并 `memor*` 涨到 2,567 段，而摘掉整根刀三只减 382 段。**"认证类"这条边界是靠不并 `access`／`validat*`／`permission` 三个大词守住的**——一旦并入，本篇就退化成"泛访问控制"选题。

## 15.5 词界断言第五次发作：两头都轻，且都是同词根内嵌

去掉前置 `(?<![\w-])` 后主集 **632 段（＋32）**，实测这 32 段**全部含 `unauthorized`**（样例页 121238／121239／121240／121246／121248／121249／121250／122066／122281），且**其中 0 段含独立的 `authoriz*` 词形**——也就是说这 32 段本来就是 `unauthorized` 一个词的内嵌，去掉前置等于把 `unauthorized` 拆成 `un`＋`authorized` 重复计数（第 5.1 节）。去掉尾断言再多 **2 段**，实测是 `Account-driven`（页 124149／124150）。

**五篇词界形态对照（前置增量／尾增量）**：#132 尾挡 193（`out-of-bounds` 连字符形漏计）、#133 前置 162／166、#134 前置 ＋434／尾 0、#135 前置 ＋488／尾 ＋9、**本篇前置 ＋32／尾 ＋2**。**本篇是五篇里词界最"干净"的一篇**，而且新增的两批都是**同词根的形态变体**，不是外部机构名或研究者人名混入——这与 #132（10 段来自页面免责声明）、#135（`CodeColorist`／`Zone Media`）的污染形态不同。

# 16 十二步复算

把全篇主数字串成一条可独立重跑的检查链，每一步只依赖上一步的集合定义：

1. **全树分母**：域 A 9,045 ＋ 域 B 91 ＝ **9,136 段／257 页／472 名**，含 Impact **7,434**、含致谢 **9,132**。✓
2. **三刀命中**：`$re3`（三刀并一张词表）命中 Impact∪Description ＝ **600 段／159 页／119 名**，与"刀一∪刀二∪刀三"逐段相等，差 **0**。✓
3. **逐刀与字段**：刀一 100（I 100∶D 0∶I∩D 0）、刀二 184（115∶111∶42）、刀三 382（59∶340∶17）；主集 I 243∶D 443∶I∩D 86，仅I 157＋仅D 357＋86 ＝ **600**。✓
4. **优先序闭合**：仅一 57＋仅二 138＋仅三 343＋一二 23＋一三 16＋二三 19＋三刀同段 4 ＝ **600**；三刀相加 666 − 600 ＝ **66**（＝23＋16＋19＋3×4，重叠消耗）。✓
5. **单词与反事实**：`authoriz*` 198、`authenticat*` 144、`entitlement` 131、`account(s)` 33、`credential*` 29、`password` 23、`keychain*` 37、`cookie(s)` 17；摘 `authenticat*` 剩 **493**、摘 `authoriz*` 剩 **414**、剔刀一全部剩 **500**。✓
6. **三字段并集**：致谢侧三刀命中 **0** 段 → 三字段并集 ＝ 两字段 ＝ **600**（零增量）。宽松词表复核：`Threat Analysis Group` 94∩5 为判据侧自命中、非致谢驱动；去前置／去尾的 ＋32／＋2 全为同词根形态。✓
7. **句轴**：1 句段 506＋2 句段 94 ＝ 600 段；句次 506＋94×2 ＝ **694** ＝ Impact 243 ＋ Description 451；451 ＝ Description 段 443 ＋ 两句 Description 段 8；94 ＝ I∩D 86 ＋ 8。**94 ≠ 86**（与 #135 的 30＝30 形态不同）。✓
8. **句库**：694 句次由 **85** 个 distinct 句写成；只在 I 63＋只在 D 22＋共用 **0** ＝ 85；top1 159＝22.91%、top10 424＝61.10%、top50 650＝93.66%。✓
9. **情态**：Impact 句 `may` 233＋`could` 9＋`can` 1 ＝ **243**＝Impact 句次（`might／should／will／must` 0、零情态 0、双情态 0）；Description 451 句情态 **0**。✓
10. **后果八类**：Impact 口径 ∩主集 1＋40＋23＋11＋0＋0＋6＋34 ＝ **115**＝并集（差 **0**）；三字段口径 120＝并集 120（差 0）；0 类段 **485＝80.83%**、≥2 类段 **0**、M2 锚点集∩主集 **0**。✓
11. **页面与版式**：159 页八档 67＋27＋16＋9＋13＋15＋8＋4 ＝ **159**；该页集全树 8,040 段 → 本篇占 **7.46%**、每页均 3.77 段；域 A 594／9,045＝**6.57%**∶域 B 6／91＝**6.59%**。✓
12. **编号与致谢**：编号分箱 1＋590＋9 ＝ 600（带编号 **599**）、distinct 编号 **188**、独占 **188**、共享 **0**；致谢 599 段、**1,073** 人次、均值 **1.79**、最大 **15**（全树 1.80／37）。✓

**十二步任一步不等，本篇作废。** 全部闭合；主集 600 与四套除法（6.57% 全树／8.07% 含 Impact 分母／7.46% 所在页集／6.57%∶6.59% 版式两域）不可混用。

# 附表 A 判据词族普查（291 行）

| 组 | 判据 | 并集段 | 页 | 名 | ∩主集 | ∩R | ∩E | ∩WO |
|---|---|---:|---:|---:|---:|---:|---:|---:|
| 本体词（本篇主刀） | `password／passwords 词界（刀一·口令）` | 23 | 21 | 7 | 23 | 1 | 0 | 4 |
| 本体词（本篇主刀） | `passcode／passcodes 词界（刀一·设备口令）` | 10 | 10 | 4 | 10 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `passphrase 词界（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `credential／credentials 词界（刀一·凭据）` | 29 | 28 | 8 | 29 | 10 | 0 | 5 |
| 本体词（本篇主刀） | `credential* 词族（刀一·含单复数与派生）` | 29 | 28 | 8 | 29 | 10 | 0 | 5 |
| 本体词（本篇主刀） | `token／tokens 词界（刀一·令牌）` | 4 | 4 | 1 | 4 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `secret／secrets 词界（刀一·密钥材料，全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `keychain／keychains 词界（刀一·钥匙串）` | 37 | 27 | 8 | 37 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `keychain* 词族（刀一·含 Keychain 专名）` | 37 | 27 | 8 | 37 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Keychain 专名（大小写敏感，刀一）` | 34 | 24 | 7 | 34 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `System Keychain 短语（刀一）` | 7 | 7 | 2 | 7 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `iCloud Keychain 短语（刀一）` | 4 | 4 | 1 | 4 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `biometric* 词族（刀一·生物识别）` | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `biometrics-gated 短语（刀一）` | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Touch ID 短语（刀一）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Face ID 短语（刀一）` | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Touch ID／Face ID 合并（刀一）` | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Apple Pay 短语（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Secure Enclave 短语（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `username／user name 两形（刀一·用户名）` | 4 | 4 | 1 | 4 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `password autofill 短语（刀一）` | 9 | 9 | 3 | 9 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `autofill* 词族（刀一候选，未收）` | 9 | 9 | 3 | 9 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `saved passwords 短语（刀一）` | 2 | 2 | 2 | 2 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Wi-Fi passwords 短语（刀一）` | 1 | 1 | 1 | 1 | 1 | 0 | 1 |
| 本体词（本篇主刀） | `passcode entries 短语（刀一·解锁尝试）` | 7 | 7 | 1 | 7 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `passphrase／PIN／OTP／2FA 四项合并（刀一全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `刀一并集（凭据与认证材料本体，两字段）` | 100 | 65 | 25 | 100 | 11 | 0 | 9 |
| 本体词（本篇主刀） | `authenticat* 词族（刀二·认证）` | 144 | 84 | 38 | 144 | 24 | 4 | 48 |
| 本体词（本篇主刀） | `authentication 裸词界（刀二）` | 144 | 84 | 38 | 144 | 24 | 4 | 48 |
| 本体词（本篇主刀） | `authenticate 动词形（刀二）` | 5 | 5 | 2 | 5 | 5 | 0 | 5 |
| 本体词（本篇主刀） | `authentication flow 短语（刀二）` | 5 | 5 | 1 | 5 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `authentication policy 短语（刀二）` | 7 | 7 | 1 | 7 | 7 | 0 | 0 |
| 本体词（本篇主刀） | `unauthenticated 词界（刀二·未认证）` | 9 | 9 | 3 | 9 | 8 | 0 | 7 |
| 本体词（本篇主刀） | `verif* 词族（刀二·校验）` | 9 | 7 | 4 | 9 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `verify／verified／verification 三形（刀二）` | 9 | 7 | 4 | 9 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `certificate* 词族（刀二·证书）` | 11 | 8 | 1 | 11 | 3 | 0 | 0 |
| 本体词（本篇主刀） | `certificate(s) 词界（刀二）` | 11 | 8 | 1 | 11 | 3 | 0 | 0 |
| 本体词（本篇主刀） | `certificate authority 短语（刀二）` | 8 | 8 | 1 | 8 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `certificate validation 短语（刀二）` | 11 | 8 | 1 | 11 | 3 | 0 | 0 |
| 本体词（本篇主刀） | `extended key usages 短语（刀二）` | 8 | 8 | 1 | 8 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `signature(s) 词界（刀二·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `sign* 全词族（刀二对照，含 signing）` | 18 | 17 | 4 | 13 | 7 | 0 | 7 |
| 本体词（本篇主刀） | `signing 词界（刀二假阳性来源）` | 5 | 5 | 1 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `cryptograph* 词族（刀二·密码学）` | 11 | 11 | 2 | 11 | 4 | 0 | 7 |
| 本体词（本篇主刀） | `cryptographic functions 短语（刀二）` | 7 | 7 | 1 | 7 | 0 | 0 | 7 |
| 本体词（本篇主刀） | `hash*／HMAC 词界（刀二·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Pointer Authentication 短语（刀二·多义词）` | 17 | 15 | 3 | 17 | 0 | 4 | 0 |
| 本体词（本篇主刀） | `PAC 专名（大小写敏感，全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `RSA／PKCS#1 专名（刀二对照）` | 7 | 7 | 1 | 7 | 0 | 0 | 7 |
| 本体词（本篇主刀） | `WebAuthn 专名（刀二）` | 5 | 5 | 1 | 5 | 5 | 0 | 0 |
| 本体词（本篇主刀） | `IPSec 专名（刀二）` | 2 | 2 | 1 | 2 | 2 | 0 | 0 |
| 本体词（本篇主刀） | `constant-time 短语（刀二）` | 7 | 7 | 1 | 7 | 0 | 0 | 7 |
| 本体词（本篇主刀） | `side-channel 短语（刀二）` | 7 | 7 | 1 | 7 | 0 | 0 | 7 |
| 本体词（本篇主刀） | `validat* 词族（刀二候选，未收）` | 1341 | 179 | 168 | 51 | 184 | 24 | 15 |
| 本体词（本篇主刀） | `刀二并集（认证、校验与密码学动作，两字段）` | 184 | 95 | 45 | 184 | 39 | 4 | 62 |
| 本体词（本篇主刀） | `account／accounts 词界（刀三·账户）` | 33 | 18 | 9 | 33 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `account* 词族（刀三·含 accounting）` | 35 | 20 | 10 | 33 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Apple Account 短语（刀三）` | 9 | 9 | 4 | 9 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `account identifier 短语（刀三）` | 18 | 8 | 3 | 18 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `persistent account identifier 短语（刀三）` | 18 | 8 | 3 | 18 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `session／sessions 词界（刀三·会话）` | 4 | 4 | 2 | 4 | 1 | 0 | 0 |
| 本体词（本篇主刀） | `session* 词族（刀三）` | 4 | 4 | 2 | 4 | 1 | 0 | 0 |
| 本体词（本篇主刀） | `session management 短语（刀三）` | 4 | 4 | 2 | 4 | 1 | 0 | 0 |
| 本体词（本篇主刀） | `cookie／cookies 词界（刀三·Cookie）` | 17 | 17 | 1 | 17 | 6 | 5 | 0 |
| 本体词（本篇主刀） | `cookie management 短语（刀三）` | 17 | 17 | 1 | 17 | 6 | 5 | 0 |
| 本体词（本篇主刀） | `identity／identities 词界（刀三·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `authoriz* 词族（刀三·授权）` | 198 | 64 | 52 | 198 | 13 | 4 | 25 |
| 本体词（本篇主刀） | `authorization 裸词界（刀三）` | 198 | 64 | 52 | 198 | 13 | 4 | 25 |
| 本体词（本篇主刀） | `unauthorized 词界（刀三）` | 37 | 35 | 9 | 5 | 21 | 8 | 0 |
| 本体词（本篇主刀） | `user authorization 短语（刀三）` | 10 | 10 | 3 | 10 | 0 | 0 | 9 |
| 本体词（本篇主刀） | `entitlement／entitlements 词界（刀三·权限属性）` | 131 | 74 | 38 | 131 | 0 | 0 | 12 |
| 本体词（本篇主刀） | `entitlement* 词族（刀三）` | 131 | 74 | 38 | 131 | 0 | 0 | 12 |
| 本体词（本篇主刀） | `entitlement checks 短语（刀三）` | 102 | 60 | 30 | 102 | 0 | 0 | 11 |
| 本体词（本篇主刀） | `restricted entitlements 短语（刀三）` | 2 | 2 | 1 | 2 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `sign in 分写（刀三·登录）` | 5 | 5 | 1 | 5 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `signed in 短语（刀三）` | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `login／log in／logout／log out 四形（刀三）` | 4 | 3 | 2 | 4 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Login Window 短语（刀三·macOS 登录窗）` | 2 | 1 | 1 | 2 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `permissions?／consent 词界（刀三候选，未收）` | 639 | 148 | 105 | 37 | 50 | 0 | 78 |
| 本体词（本篇主刀） | `刀三并集（会话、账户与授权载体，两字段）` | 382 | 117 | 87 | 382 | 20 | 9 | 36 |
| 本体词（本篇主刀） | `三刀并集（本篇主刀 $re3，两字段）` | 600 | 159 | 119 | 600 | 64 | 13 | 92 |
| 本体词（本篇主刀） | `三字段并集（含致谢）` | 600 | 159 | 119 | 600 | 64 | 13 | 92 |
| 本体词（本篇主刀） | `仅刀一（闭合分区）` | 57 | 39 | 14 | 57 | 5 | 0 | 0 |
| 本体词（本篇主刀） | `仅刀二（闭合分区）` | 138 | 81 | 33 | 138 | 33 | 4 | 50 |
| 本体词（本篇主刀） | `仅刀三（闭合分区）` | 343 | 113 | 80 | 343 | 20 | 9 | 27 |
| 本体词（本篇主刀） | `刀一∩刀二（仅两刀，已剔三刀同段）` | 23 | 22 | 7 | 23 | 6 | 0 | 6 |
| 本体词（本篇主刀） | `刀一∩刀三（仅两刀，已剔三刀同段）` | 16 | 16 | 6 | 16 | 0 | 0 | 3 |
| 本体词（本篇主刀） | `刀二∩刀三（仅两刀，已剔三刀同段）` | 19 | 14 | 5 | 19 | 0 | 0 | 6 |
| 本体词（本篇主刀） | `三刀同段（本篇 4 段）` | 4 | 4 | 1 | 4 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `反事实：主刀并入 permissions?／consent` | 1202 | 188 | 179 | 600 | 114 | 13 | 162 |
| 本体词（本篇主刀） | `反事实：主刀并入 access 裸词` | 2057 | 195 | 239 | 600 | 117 | 13 | 121 |
| 本体词（本篇主刀） | `反事实：主刀并入 bypass* 词族` | 861 | 174 | 151 | 600 | 97 | 29 | 92 |
| 本体词（本篇主刀） | `反事实：主刀并入 validat* 词族` | 1890 | 210 | 239 | 600 | 240 | 37 | 95 |
| 本体词（本篇主刀） | `反事实：主刀并入 privileg* 词族` | 1115 | 182 | 169 | 600 | 143 | 22 | 94 |
| 本体词（本篇主刀） | `反事实：主刀并入 pair* 词族` | 613 | 160 | 119 | 600 | 72 | 13 | 92 |
| 本体词（本篇主刀） | `反事实：主刀并入 iCloud 专名` | 615 | 164 | 122 | 600 | 64 | 22 | 95 |
| 本体词（本篇主刀） | `反事实：主刀并入 encrypt*／decrypt*` | 600 | 159 | 119 | 600 | 64 | 13 | 92 |
| 本体词（本篇主刀） | `反事实：主刀并入 memor* 词族` | 2567 | 219 | 203 | 600 | 202 | 73 | 92 |
| 本体词（本篇主刀） | `反事实：主刀并入 file(s)` | 1756 | 206 | 208 | 600 | 81 | 28 | 92 |
| 本体词（本篇主刀） | `反事实：刀二摘除 authenticat* 词族` | 493 | 137 | 104 | 493 | 46 | 9 | 56 |
| 本体词（本篇主刀） | `反事实：刀三摘除 authoriz* 词族` | 414 | 143 | 88 | 414 | 51 | 9 | 71 |
| 本体词（本篇主刀） | `反事实：去掉前置 (?<![\w-])` | 632 | 166 | 121 | 600 | 80 | 21 | 92 |
| 本体词（本篇主刀） | `反事实：去掉尾断言 (?![\w-])` | 602 | 159 | 119 | 600 | 64 | 13 | 92 |
| 本体词（本篇主刀） | `反事实：sign…in 写成无界（designing 假阳性）` | 61 | 43 | 5 | 6 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `memor* 词族（#133 主刀对照）` | 1967 | 199 | 102 | 0 | 138 | 60 | 0 |
| 本体词（本篇主刀） | `file(s) 词族（#135 主刀对照）` | 1170 | 177 | 119 | 14 | 17 | 15 | 5 |
| 本体词（本篇主刀） | `key／keys 词界（刀一候选，未收）` | 16 | 16 | 3 | 16 | 1 | 0 | 7 |
| 本体词（本篇主刀） | `Pointer Authentication（刀二多义词集）` | 17 | 15 | 3 | 17 | 0 | 4 | 0 |
| 本体词（本篇主刀） | `Pointer Authentication 三字段口径` | 17 | 15 | 3 | 17 | 0 | 4 | 0 |
| 本体词（本篇主刀） | `PAC 专名三字段（全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `authenticat* 词族两字段` | 144 | 84 | 38 | 144 | 24 | 4 | 48 |
| 本体词（本篇主刀） | `authentication 裸词两字段` | 144 | 84 | 38 | 144 | 24 | 4 | 48 |
| 本体词（本篇主刀） | `sign* 全词族两字段` | 18 | 17 | 4 | 13 | 7 | 0 | 7 |
| 本体词（本篇主刀） | `signing 词界（三字段）` | 5 | 5 | 1 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `design* 词族（sign…in 无界的真凶）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `「…without authentication」句` | 43 | 33 | 11 | 43 | 1 | 0 | 43 |
| 本体词（本篇主刀） | `「…without (user) authorization」句` | 10 | 10 | 3 | 10 | 0 | 0 | 10 |
| 本体词（本篇主刀） | `「without prompting」句（对照）` | 19 | 19 | 2 | 3 | 0 | 0 | 19 |
| 本体词（本篇主刀） | `「without user consent」句（对照）` | 47 | 41 | 15 | 8 | 14 | 0 | 47 |
| 本体词（本篇主刀） | `「after failing authentication」句` | 9 | 9 | 3 | 9 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `「Stolen Device Protection」句` | 4 | 4 | 4 | 2 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `「Private Browsing」句` | 35 | 32 | 5 | 15 | 6 | 0 | 15 |
| 本体词（本篇主刀） | `「Hidden Photos Album」句` | 16 | 16 | 4 | 16 | 0 | 0 | 16 |
| 本体词（本篇主刀） | `「Sign In With Apple」句` | 5 | 5 | 1 | 5 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `「unauthenticated user」句` | 9 | 9 | 3 | 9 | 8 | 0 | 7 |
| 本体词（本篇主刀） | `「signed-in／signed in」句` | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `「compromised intermediate certificate authority」句` | 8 | 8 | 1 | 8 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `「timing side-channel」句` | 7 | 7 | 1 | 7 | 0 | 0 | 7 |
| 本体词（本篇主刀） | `「VoiceOver 朗读」句` | 2 | 2 | 2 | 2 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `「iPhone Mirroring」句` | 6 | 6 | 4 | 1 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `「Digital ID」句` | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `「Assistive Access」句` | 1 | 1 | 1 | 1 | 0 | 0 | 1 |
| 本体词（本篇主刀） | `「Touch Bar」句` | 1 | 1 | 1 | 1 | 0 | 0 | 1 |
| 本体词（本篇主刀） | `「Screen Sharing」句` | 10 | 10 | 2 | 7 | 5 | 0 | 5 |
| 本体词（本篇主刀） | `「AirPlay」句` | 7 | 7 | 1 | 7 | 7 | 0 | 7 |
| 本体词（本篇主刀） | `「FaceTime」句` | 20 | 15 | 4 | 9 | 0 | 0 | 4 |
| 本体词（本篇主刀） | `「Shortcut(s)」句` | 101 | 60 | 4 | 9 | 12 | 0 | 43 |
| 本体词（本篇主刀） | `「VNC」句` | 2 | 2 | 1 | 2 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `组件名代理：nm 含认证／凭据类关键字` | 88 | 57 | 7 | 32 | 17 | 0 | 3 |
| 本体词（本篇主刀） | `组件名代理：nm 含 Account` | 101 | 59 | 6 | 30 | 6 | 0 | 5 |
| 本体词（本篇主刀） | `组件名代理：nm 属安全服务类（Security/CoreCrypto/Heimdal/NetAuth/AuthKit/DeviceCheck/Lock/Reset Services）` | 202 | 92 | 11 | 66 | 44 | 0 | 12 |
| 本体词（本篇主刀） | `组件名代理：nm 属网络类（Wi-Fi/Network/Bluetooth/802.1X/curl/IPSec）` | 340 | 116 | 13 | 45 | 104 | 0 | 6 |
| 本体词（本篇主刀） | `组件名代理：nm 为 Kernel` | 745 | 153 | 1 | 19 | 77 | 9 | 0 |
| 本体词（本篇主刀） | `组件名代理：nm 含 WebKit／Safari` | 1312 | 160 | 14 | 47 | 210 | 41 | 27 |
| 本体词（本篇主刀） | `本篇∩#134 主集 RESM` | 1541 | 208 | 166 | 274 | 199 | 45 | 56 |
| 本体词（本篇主刀） | `本篇∩#134 主集（摘除 manag*）` | 675 | 147 | 75 | 0 | 56 | 22 | 7 |
| 本体词（本篇主刀） | `manag* 词族（∩#134 重合成因）` | 1071 | 185 | 130 | 274 | 181 | 31 | 49 |
| 本体词（本篇主刀） | `本篇∩#133 主集 RESX` | 2791 | 206 | 154 | 23 | 290 | 63 | 7 |
| 本体词（本篇主刀） | `本篇∩#132 主集 IMGX` | 1543 | 211 | 133 | 25 | 138 | 24 | 18 |
| 本体词（本篇主刀） | `本篇∩#135 主集 AVMP` | 2715 | 237 | 171 | 41 | 111 | 62 | 25 |
| 本体词（本篇主刀） | `本篇∩#128 远程通道集 REM` | 849 | 178 | 73 | 65 | 843 | 4 | 27 |
| 本体词（本篇主刀） | `本篇∩#131 三刀 WEB` | 1253 | 184 | 45 | 47 | 243 | 46 | 39 |
| 本体词（本篇主刀） | `本篇∩#129 三族 PMS` | 2213 | 209 | 130 | 5 | 154 | 65 | 0 |
| 本体词（本篇主刀） | `本篇∩#130 三刀 TLO` | 343 | 120 | 27 | 0 | 53 | 18 | 0 |
| 本体词（本篇主刀） | `本篇∩#126 绕过集 BP` | 301 | 114 | 58 | 40 | 42 | 20 | 0 |
| 本体词（本篇主刀） | `本篇∩#125 泄露集` | 427 | 141 | 54 | 5 | 11 | 2 | 0 |
| 本体词（本篇主刀） | `本篇∩修复词族 CK` | 4146 | 244 | 304 | 236 | 476 | 74 | 96 |
| 本体词（本篇主刀） | `use after free 空格形` | 127 | 49 | 12 | 0 | 20 | 8 | 0 |
| 本体词（本篇主刀） | `use-after-free 连字符形` | 193 | 100 | 16 | 0 | 13 | 9 | 0 |
| 本体词（本篇主刀） | `out of bounds 两形` | 816 | 172 | 62 | 0 | 72 | 19 | 0 |
| 本体词（本篇主刀） | `Google Threat Analysis Group 机构名（致谢污染源头）` | 94 | 55 | 8 | 5 | 0 | 36 | 0 |
| 本体词（本篇主刀） | `Privacy Indicator(s) 短语` | 4 | 4 | 3 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `exfiltrat* 词族` | 51 | 32 | 2 | 6 | 51 | 0 | 0 |
| 本体词（本篇主刀） | `permission／consent 短语集（三字段）` | 639 | 148 | 105 | 37 | 50 | 0 | 78 |
| 本体词（本篇主刀） | `permission／consent 两字段` | 639 | 148 | 105 | 37 | 50 | 0 | 78 |
| 句级刀（36 把） | `「An authorization issue was addressed…」句` | 194 | 61 | 51 | 194 | 13 | 4 | 21 |
| 句级刀（36 把） | `「An authentication issue was addressed…」句` | 57 | 44 | 19 | 57 | 24 | 0 | 20 |
| 句级刀（36 把） | `「A certificate validation issue…」句` | 11 | 8 | 1 | 11 | 3 | 0 | 0 |
| 句级刀（36 把） | `「A cookie management issue…」句` | 17 | 17 | 1 | 17 | 6 | 5 | 0 |
| 句级刀（36 把） | `「A session management issue…」句` | 4 | 4 | 2 | 4 | 1 | 0 | 0 |
| 句级刀（36 把） | `「A cryptographic issue…」句` | 4 | 4 | 1 | 4 | 4 | 0 | 0 |
| 句级刀（36 把） | `「A timing side-channel issue…」句` | 7 | 7 | 1 | 7 | 0 | 0 | 7 |
| 句级刀（36 把） | `「A validation issue existed in the entitlement verification…」句` | 8 | 6 | 3 | 8 | 0 | 0 | 0 |
| 句级刀（36 把） | `「This issue was addressed with additional entitlement checks.」句` | 99 | 60 | 29 | 99 | 0 | 0 | 10 |
| 句级刀（36 把） | `「…improved authentication」修复句` | 23 | 18 | 9 | 23 | 0 | 0 | 0 |
| 句级刀（36 把） | `「…improved entitlements／entitlement checks」修复句` | 12 | 12 | 5 | 12 | 0 | 0 | 2 |
| 句级刀（36 把） | `「…improved validation of the process entitlement」句` | 8 | 6 | 3 | 8 | 0 | 0 | 0 |
| 句级刀（36 把） | `「…improved certificate validation」修复句` | 11 | 8 | 1 | 11 | 3 | 0 | 0 |
| 句级刀（36 把） | `「…now require restricted entitlements」句` | 2 | 2 | 1 | 2 | 0 | 0 | 0 |
| 句级刀（36 把） | `「…addressed by removing／adding … entitlements／a delay」句` | 274 | 119 | 62 | 13 | 25 | 4 | 3 |
| 句级刀（36 把） | `「…improved state management」修复句` | 662 | 176 | 111 | 270 | 127 | 14 | 45 |
| 句级刀（36 把） | `「…improved access control／restrictions」修复句` | 84 | 46 | 15 | 26 | 7 | 0 | 11 |
| 句级刀（36 把） | `「…without authentication」句（无认证访问）` | 43 | 33 | 11 | 43 | 1 | 0 | 43 |
| 句级刀（36 把） | `「…without (user) authorization」句（无授权）` | 10 | 10 | 3 | 10 | 0 | 0 | 10 |
| 句级刀（36 把） | `「…after failing authentication」句（认证失败后）` | 9 | 9 | 3 | 9 | 0 | 0 | 0 |
| 句级刀（36 把） | `「…Keychain…」句（钥匙串读写）` | 37 | 27 | 8 | 37 | 0 | 0 | 0 |
| 句级刀（36 把） | `「…password／passcode…」句（口令本体）` | 33 | 27 | 11 | 33 | 1 | 0 | 4 |
| 句级刀（36 把） | `「…credential(s)…」句（凭据泄露）` | 29 | 28 | 8 | 29 | 10 | 0 | 5 |
| 句级刀（36 把） | `「…persistent account identifier」句（账户标识）` | 18 | 8 | 3 | 18 | 0 | 0 | 0 |
| 句级刀（36 把） | `「…Apple Account…」句（苹果账户）` | 9 | 9 | 4 | 9 | 0 | 0 | 0 |
| 句级刀（36 把） | `「…Sign In With Apple…」句（第三方登录）` | 5 | 5 | 1 | 5 | 0 | 0 | 0 |
| 句级刀（36 把） | `「…bypass Pointer Authentication」句（指针认证）` | 17 | 15 | 3 | 17 | 0 | 4 | 0 |
| 句级刀（36 把） | `「…certificate authority／extended key usages」句（证书链）` | 8 | 8 | 1 | 8 | 0 | 0 | 0 |
| 句级刀（36 把） | `「…unauthenticated user…」句（未认证用户）` | 9 | 9 | 3 | 9 | 8 | 0 | 7 |
| 句级刀（36 把） | `「…Cookies belonging to one origin…」句（跨源 Cookie）` | 6 | 6 | 1 | 6 | 0 | 0 | 0 |
| 句级刀（36 把） | `「…exfiltrate data cross-origin」句（网页外泄）` | 51 | 32 | 2 | 6 | 51 | 0 | 0 |
| 句级刀（36 把） | `「…account credentials／saved passwords…」句（邮件与账户凭据）` | 6 | 6 | 3 | 6 | 0 | 0 | 0 |
| 句级刀（36 把） | `「A／An X was addressed…」通用修复句` | 7213 | 256 | 394 | 591 | 819 | 108 | 178 |
| 句级刀（36 把） | `「…issue existed…」缺陷存在句` | 55 | 45 | 12 | 8 | 9 | 9 | 1 |
| 句级刀（36 把） | `「Apple is aware of a report…」在野句` | 103 | 74 | 12 | 13 | 4 | 103 | 0 |
| 句级刀（36 把） | `其余含三刀词句` | 9136 | 257 | 472 | 600 | 843 | 108 | 178 |
| 机制名与检测词对照 | `authentication（认证词界）` | 144 | 84 | 38 | 144 | 24 | 4 | 48 |
| 机制名与检测词对照 | `authenticate／authenticated（动词两形）` | 5 | 5 | 2 | 5 | 5 | 0 | 5 |
| 机制名与检测词对照 | `authenticator（认证器）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `unauthenticated（未认证）` | 9 | 9 | 3 | 9 | 8 | 0 | 7 |
| 机制名与检测词对照 | `authorization（授权词界）` | 198 | 64 | 52 | 198 | 13 | 4 | 25 |
| 机制名与检测词对照 | `authorize／authorized／unauthorized（三形）` | 37 | 35 | 9 | 5 | 21 | 8 | 0 |
| 机制名与检测词对照 | `entitlement（权限属性）` | 131 | 74 | 38 | 131 | 0 | 0 | 12 |
| 机制名与检测词对照 | `password／passcode（口令两词）` | 33 | 27 | 11 | 33 | 1 | 0 | 4 |
| 机制名与检测词对照 | `credential（凭据）` | 29 | 28 | 8 | 29 | 10 | 0 | 5 |
| 机制名与检测词对照 | `token（令牌）` | 4 | 4 | 1 | 4 | 0 | 0 | 0 |
| 机制名与检测词对照 | `keychain（钥匙串）` | 37 | 27 | 8 | 37 | 0 | 0 | 0 |
| 机制名与检测词对照 | `key／keys（钥匙／键，未收）` | 16 | 16 | 3 | 16 | 1 | 0 | 7 |
| 机制名与检测词对照 | `certificate（证书）` | 11 | 8 | 1 | 11 | 3 | 0 | 0 |
| 机制名与检测词对照 | `cryptographic（密码学）` | 11 | 11 | 2 | 11 | 4 | 0 | 7 |
| 机制名与检测词对照 | `encryption／decryption（未收）` | 10 | 10 | 2 | 10 | 0 | 0 | 10 |
| 机制名与检测词对照 | `verification（校验名词）` | 9 | 7 | 4 | 9 | 0 | 0 | 0 |
| 机制名与检测词对照 | `validate／validation（未收）` | 1341 | 179 | 168 | 51 | 184 | 24 | 15 |
| 机制名与检测词对照 | `biometric（生物识别）` | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 机制名与检测词对照 | `Face ID／Touch ID（两专名）` | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 机制名与检测词对照 | `Secure Enclave（安全隔区，全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `Apple Pay（全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `WebAuthn（专名）` | 5 | 5 | 1 | 5 | 5 | 0 | 0 |
| 机制名与检测词对照 | `session（会话）` | 4 | 4 | 2 | 4 | 1 | 0 | 0 |
| 机制名与检测词对照 | `cookie（Cookie）` | 17 | 17 | 1 | 17 | 6 | 5 | 0 |
| 机制名与检测词对照 | `account（账户）` | 33 | 18 | 9 | 33 | 0 | 0 | 0 |
| 机制名与检测词对照 | `identity／identities（全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `login／sign in（登录两形）` | 9 | 8 | 3 | 9 | 0 | 0 | 0 |
| 机制名与检测词对照 | `permission（权限，未收）` | 580 | 141 | 99 | 29 | 36 | 0 | 36 |
| 机制名与检测词对照 | `consent（同意，未收）` | 76 | 58 | 18 | 8 | 20 | 0 | 54 |
| 机制名与检测词对照 | `privacy（隐私词族）` | 348 | 119 | 74 | 13 | 4 | 0 | 4 |
| 机制名与检测词对照 | `Pointer Authentication（多义词）` | 17 | 15 | 3 | 17 | 0 | 4 | 0 |
| 机制名与检测词对照 | `bypass（绕过词界）` | 301 | 114 | 58 | 40 | 42 | 20 | 0 |
| 机制名与检测词对照 | `elevate privileges（提权）` | 124 | 62 | 30 | 6 | 0 | 8 | 0 |
| 机制名与检测词对照 | `privileged network position（特权网络位置）` | 93 | 56 | 16 | 17 | 93 | 0 | 0 |
| 机制名与检测词对照 | `sandbox（沙盒）` | 475 | 120 | 74 | 34 | 40 | 8 | 6 |
| 机制名与检测词对照 | `cross-origin（跨源）` | 69 | 43 | 2 | 6 | 56 | 0 | 0 |
| 机制名与检测词对照 | `arbitrary code execution（任意代码执行）` | 218 | 100 | 22 | 0 | 26 | 25 | 0 |
| 机制名与检测词对照 | `memory corruption（内存损坏）` | 388 | 127 | 31 | 0 | 3 | 44 | 0 |
| 机制名与检测词对照 | `process memory（进程内存）` | 326 | 110 | 24 | 0 | 27 | 0 | 0 |
| 机制名与检测词对照 | `kernel memory（内核内存）` | 520 | 113 | 34 | 0 | 41 | 16 | 0 |
| 机制名与检测词对照 | `denial of service（拒绝服务）` | 470 | 155 | 62 | 5 | 164 | 0 | 0 |
| 机制名与检测词对照 | `crash（崩溃）` | 499 | 128 | 17 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `disclos*（泄露正式词）` | 427 | 141 | 54 | 5 | 11 | 2 | 0 |
| 机制名与检测词对照 | `leak（泄露词界）` | 192 | 101 | 31 | 23 | 63 | 0 | 0 |
| 机制名与检测词对照 | `fingerprint（指纹识别／设备指纹双义）` | 103 | 63 | 19 | 18 | 0 | 0 | 0 |
| 机制名与检测词对照 | `physical access（物理访问前提）` | 142 | 71 | 39 | 34 | 4 | 0 | 1 |
| 机制名与检测词对照 | `locked device（锁定设备前提）` | 100 | 55 | 24 | 35 | 5 | 4 | 2 |
| 机制名与检测词对照 | `user（用户词界）` | 1669 | 175 | 202 | 231 | 147 | 0 | 105 |
| 机制名与检测词对照 | `attacker（攻击者）` | 890 | 160 | 112 | 131 | 433 | 25 | 20 |
| 机制名与检测词对照 | `improved checks（改进检查套话）` | 1018 | 192 | 125 | 27 | 151 | 31 | 31 |
| 机制名与检测词对照 | `input validation（输入校验）` | 676 | 151 | 85 | 13 | 146 | 1 | 0 |
| 机制名与检测词对照 | `state management（状态管理套话）` | 662 | 176 | 111 | 270 | 127 | 14 | 45 |
| 机制名与检测词对照 | `access control（访问控制）` | 21 | 8 | 5 | 18 | 0 | 0 | 0 |
| 机制名与检测词对照 | `restrict*（限制词族）` | 793 | 153 | 109 | 43 | 75 | 4 | 33 |
| 修复套话句（全篇共用 15 项） | `The issue was addressed with improved checks.` | 514 | 138 | 82 | 18 | 68 | 5 | 20 |
| 修复套话句（全篇共用 15 项） | `improved bounds checking` | 699 | 154 | 63 | 0 | 81 | 15 | 0 |
| 修复套话句（全篇共用 15 项） | `improved input validation` | 641 | 151 | 79 | 13 | 140 | 1 | 0 |
| 修复套话句（全篇共用 15 项） | `improved validation` | 367 | 101 | 76 | 22 | 13 | 23 | 15 |
| 修复套话句（全篇共用 15 项） | `additional checks` | 34 | 28 | 12 | 1 | 0 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `additional validation` | 112 | 65 | 20 | 5 | 8 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `improved checks to prevent unauthorized actions` | 16 | 16 | 4 | 0 | 0 | 8 | 0 |
| 修复套话句（全篇共用 15 项） | `addressed with（全形）` | 6589 | 254 | 364 | 554 | 735 | 104 | 163 |
| 修复套话句（全篇共用 15 项） | `addressed by` | 371 | 136 | 80 | 17 | 31 | 4 | 4 |
| 修复套话句（全篇共用 15 项） | `addressed through` | 247 | 129 | 48 | 19 | 47 | 0 | 11 |
| 修复套话句（全篇共用 15 项） | `validation 词族` | 1341 | 179 | 168 | 51 | 184 | 24 | 15 |
| 修复套话句（全篇共用 15 项） | `check(s)／checking 词族` | 2017 | 227 | 201 | 142 | 243 | 46 | 48 |
| 修复套话句（全篇共用 15 项） | `sanitiz* 词族` | 93 | 54 | 21 | 0 | 3 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `enforce* 词族` | 53 | 34 | 4 | 0 | 0 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `restrict* 词族` | 793 | 153 | 109 | 43 | 75 | 4 | 33 |
| 外部集对照（12 套，含 #122–#126） | `远程可达 R` | 843 | 178 | 72 | 64 | 843 | 4 | 27 |
| 外部集对照（12 套，含 #122–#126） | `without 词界集 WO` | 178 | 97 | 37 | 92 | 27 | 0 | 178 |
| 外部集对照（12 套，含 #122–#126） | `exploit* 集 E` | 108 | 76 | 12 | 13 | 4 | 108 | 0 |
| 外部集对照（12 套，含 #122–#126） | `user 词界集 UU` | 1627 | 175 | 200 | 231 | 130 | 0 | 105 |
| 外部集对照（12 套，含 #122–#126） | `动名词开头集 G` | 2179 | 224 | 98 | 6 | 108 | 53 | 0 |
| 外部集对照（12 套，含 #122–#126） | `非处理/解析类 HI` | 273 | 102 | 35 | 1 | 108 | 0 | 0 |
| 外部集对照（12 套，含 #122–#126） | `≥2 后果类 M2` | 185 | 82 | 35 | 0 | 23 | 0 | 0 |
| 外部集对照（12 套，含 #122–#126） | `race condition 集 RC（#122）` | 232 | 95 | 40 | 5 | 14 | 0 | 3 |
| 外部集对照（12 套，含 #122–#126） | `logic 集 L（#123）` | 550 | 162 | 81 | 19 | 99 | 9 | 24 |
| 外部集对照（12 套，含 #122–#126） | `拒绝服务集 Ds（#124）` | 470 | 155 | 62 | 5 | 164 | 0 | 0 |
| 外部集对照（12 套，含 #122–#126） | `泄露集 X（#125）` | 427 | 141 | 54 | 5 | 11 | 2 | 0 |
| 外部集对照（12 套，含 #122–#126） | `绕过集 BP（#126）` | 301 | 114 | 58 | 40 | 42 | 20 | 0 |
| 后果强度八类 | `ACE 类` | 336 | 120 | 43 | 1 | 36 | 31 | 0 |
| 后果强度八类 | `BYP 类` | 301 | 114 | 58 | 40 | 42 | 20 | 0 |
| 后果强度八类 | `DIS 类` | 603 | 159 | 78 | 28 | 74 | 2 | 0 |
| 后果强度八类 | `DOS 类` | 2014 | 176 | 130 | 11 | 343 | 0 | 0 |
| 后果强度八类 | `FULL 类` | 63 | 43 | 18 | 0 | 7 | 0 | 0 |
| 后果强度八类 | `MEM 类` | 390 | 127 | 31 | 0 | 3 | 44 | 0 |
| 后果强度八类 | `PRI 类` | 124 | 62 | 30 | 6 | 0 | 8 | 0 |
| 后果强度八类 | `SBX 类` | 475 | 120 | 74 | 34 | 40 | 8 | 6 |
| **合计** | 290 项（并集全零 16 项、非零 274 项） | 600 | — | — | 600 | 64 | 13 | 92 |

# 附表 B 含主刀整句 × 归桶 × 字段 × 外部集（86 行）

| distinct 整句 | 归入桶 | 句次 | Impact 句 | Desc 句 | 页 | ∩R | ∩E | ∩M2 | ∩L | ∩RC |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| An authorization issue was addressed with improved state management. | 「An authorization issue was addressed…」句 | 159 | 0 | 159 | 61 | 13 | 4 | 0 | 0 | 0 |
| This issue was addressed with additional entitlement checks. | 「This issue was addressed with additional entitlement checks.」句 | 99 | 0 | 99 | 60 | 0 | 0 | 0 | 0 | 0 |
| An authentication issue was addressed with improved state management. | 「An authentication issue was addressed…」句 | 57 | 0 | 57 | 44 | 24 | 0 | 0 | 0 | 0 |
| A local app may be able to read a persistent account identifier | 「…persistent account identifier」句（账户标识） | 18 | 18 | 0 | 8 | 0 | 0 | 0 | 0 | 0 |
| An authorization issue was addressed with improved access control. | 「An authorization issue was addressed…」句 | 18 | 0 | 18 | 8 | 0 | 0 | 0 | 0 | 0 |
| A cookie management issue was addressed with improved state management. | 「A cookie management issue…」句 | 17 | 0 | 17 | 17 | 6 | 5 | 0 | 0 | 0 |
| Photos in the Hidden Photos Album may be viewed without authentication | 「…without authentication」句（无认证访问） | 16 | 16 | 0 | 16 | 0 | 0 | 0 | 6 | 0 |
| Private Browsing tabs may be accessed without authentication | 「…without authentication」句（无认证访问） | 15 | 15 | 0 | 14 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved authentication. | 「…improved authentication」修复句 | 13 | 0 | 13 | 13 | 0 | 0 | 0 | 0 | 0 |
| An authorization issue was addressed with improved validation. | 「An authorization issue was addressed…」句 | 12 | 0 | 12 | 12 | 0 | 0 | 0 | 0 | 0 |
| A certificate validation issue was addressed with improved certificate validation. | 「A certificate validation issue…」句 | 11 | 0 | 11 | 8 | 3 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved authentication. | 「…improved authentication」修复句 | 10 | 0 | 10 | 8 | 0 | 0 | 0 | 0 | 0 |
| Password autofill may fill in passwords after failing authentication | 「…after failing authentication」句（认证失败后） | 9 | 9 | 0 | 9 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved entitlements. | 「…improved entitlements／entitlement checks」修复句 | 9 | 0 | 9 | 9 | 0 | 0 | 0 | 0 | 0 |
| A local attacker may be able to modify the state of the Keychain | 「…Keychain…」句（钥匙串读写） | 8 | 8 | 0 | 8 | 0 | 0 | 0 | 0 | 0 |
| A local attacker may gain access to user's Keychain items | 「…Keychain…」句（钥匙串读写） | 8 | 8 | 0 | 8 | 0 | 0 | 0 | 0 | 0 |
| A malicious attacker with arbitrary read and write capability may be able to bypass Pointer Authentication | 「…bypass Pointer Authentication」句（指针认证） | 8 | 8 | 0 | 8 | 0 | 0 | 0 | 0 | 5 |
| A validation issue existed in the entitlement verification. | 「A validation issue existed in the entitlement verification…」句 | 8 | 0 | 8 | 6 | 0 | 0 | 0 | 0 | 0 |
| An attacker with a compromised intermediate certificate authority may be able to issue certificates with arbitrary extended key usages | 「…certificate authority／extended key usages」句（证书链） | 8 | 8 | 0 | 8 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved validation of the process entitlement. | 「…improved validation of the process entitlement」句 | 8 | 0 | 8 | 6 | 0 | 0 | 0 | 0 | 0 |
| A malicious app may be able to attempt passcode entries on a locked device and thereby cause escalating time delays after 4 failures | 「…password／passcode…」句（口令本体） | 7 | 7 | 0 | 7 | 0 | 0 | 0 | 7 | 0 |
| A timing side-channel issue was addressed with improvements to constant-time computation in cryptographic functions. | 「A timing side-channel issue…」句 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 | 0 |
| An attacker on the local network may be able to bypass authentication policy | 其余含三刀词句 | 7 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 |
| An unauthenticated user on the same network as a signed-in Mac could send it AirPlay commands without pairing | 「…unauthenticated user…」句（未认证用户） | 7 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 |
| Authentication credentials may be sent to a server on another origin | 「…credential(s)…」句（凭据泄露） | 7 | 7 | 0 | 7 | 0 | 0 | 0 | 0 | 0 |
| An attacker may be able to turn on sharing of an iCloud folder without authentication | 「…without authentication」句（无认证访问） | 6 | 6 | 0 | 6 | 0 | 0 | 0 | 0 | 0 |
| Cookies belonging to one origin may be sent to another origin | 「…Cookies belonging to one origin…」句（跨源 Cookie） | 6 | 6 | 0 | 6 | 0 | 0 | 0 | 0 | 0 |
| A malicious website may be able to claim WebAuthn credentials from another website that shares a registrable suffix | 「…credential(s)…」句（凭据泄露） | 5 | 5 | 0 | 5 | 5 | 0 | 0 | 0 | 0 |
| A sandboxed app may be able to access the System Keychain | 「…Keychain…」句（钥匙串读写） | 5 | 5 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to add contacts without user authorization | 「…without (user) authorization」句（无授权） | 5 | 5 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to use the Sign In With Apple authentication flow to access the user's Apple Account | 「…Apple Account…」句（苹果账户） | 5 | 5 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| An attacker on the network may be able to authenticate to Screen Sharing without valid credentials | 「…credential(s)…」句（凭据泄露） | 5 | 5 | 0 | 5 | 5 | 0 | 0 | 0 | 0 |
| An attacker with arbitrary read and write capability may be able to bypass Pointer Authentication | 「…bypass Pointer Authentication」句（指针认证） | 5 | 5 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| Password fields may be unintentionally revealed when remotely controlling a device over FaceTime | 「…password／passcode…」句（口令本体） | 5 | 5 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with a new entitlement. | 「A／An X was addressed…」通用修复句 | 5 | 0 | 5 | 5 | 0 | 0 | 0 | 0 | 0 |
| A cryptographic issue was addressed with improved integrity checks. | 「A cryptographic issue…」句 | 4 | 0 | 4 | 4 | 4 | 0 | 0 | 0 | 0 |
| A maliciously crafted email may be able to initiate FaceTime calls without user authorization | 「…without (user) authorization」句（无授权） | 4 | 4 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| A session management issue was addressed with improved checks. | 「A session management issue…」句 | 4 | 0 | 4 | 4 | 1 | 0 | 0 | 0 | 0 |
| An app may be able to access associated usernames and websites in a user's iCloud Keychain | 「…Keychain…」句（钥匙串读写） | 4 | 4 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to access sensitive payment tokens | 其余含三刀词句 | 4 | 4 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to retrieve Safari bookmarks without an entitlement check | 其余含三刀词句 | 4 | 4 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| An attacker with arbitrary read and write capability may be able to bypass Pointer Authentication. | 「…bypass Pointer Authentication」句（指针认证） | 4 | 4 | 0 | 4 | 0 | 4 | 0 | 0 | 0 |
| An attacker with physical access may be able to leak Mail account credentials | 「…credential(s)…」句（凭据泄露） | 4 | 4 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| A Shortcut may run with admin privileges without authentication | 「…without authentication」句（无认证访问） | 3 | 3 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| A malicious application may be able to leak a user's credentials | 「…credential(s)…」句（凭据泄露） | 3 | 3 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to delete credentials stored in Keychain | 「…Keychain…」句（钥匙串读写） | 3 | 3 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| An authorization issue was addressed with improved entitlement checks. | 「An authorization issue was addressed…」句 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| An encrypted volume may be accessed by a different user without prompting for the password | 「…password／passcode…」句（口令本体） | 3 | 3 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| Sensitive keychain data may be accessible from an iOS backup | 「…Keychain…」句（钥匙串读写） | 3 | 3 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| A local attacker may be able to determine the legacy VNC password configured for Screen Sharing | 「…password／passcode…」句（口令本体） | 2 | 2 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| A local attacker may gain access to Keychain items | 「…Keychain…」句（钥匙串读写） | 2 | 2 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| A malicious app may be able to bypass browser extension authentication | 其余含三刀词句 | 2 | 2 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| A system binary could be used to fingerprint a user's Apple Account | 「…Apple Account…」句（苹果账户） | 2 | 2 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| An app from a standard user account may be able to escalate privilege after admin user login | 其余含三刀词句 | 2 | 2 | 0 | 2 | 0 | 0 | 0 | 2 | 0 |
| An app may be able to hijack entitlements granted to other privileged apps | 其余含三刀词句 | 2 | 2 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| An app with admin privileges may be able to modify the System Keychain | 「…Keychain…」句（钥匙串读写） | 2 | 2 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| An attacker in a privileged network position may be able to bypass IPSec authentication and intercept network traffic | 其余含三刀词句 | 2 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 |
| Sensitive notifications now require restricted entitlements. | 「…now require restricted entitlements」句 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed by removing additional entitlements. | 「…addressed by removing／adding … entitlements／a delay」句 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 | 0 |
| A malicious app may be able to access a user's saved passwords | 「…password／passcode…」句（口令本体） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| A malicious application may gain access to a user's Keychain items | 「…Keychain…」句（钥匙串读写） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| A person with physical access to a Mac may be able to bypass Login Window during a software update | 其余含三刀词句 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| A shortcut may be able to transmit data before user authorization is granted | 其余含三刀词句 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| A user's saved passwords may be read aloud by VoiceOver | 「…password／passcode…」句（口令本体） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 1 | 0 |
| An app may be able to capture Touch Bar content without authorization | 「…without (user) authorization」句（无授权） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to enumerate devices that have signed into the user's Apple Account | 「…Apple Account…」句（苹果账户） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An app may be able to unexpectedly leak a user's credentials | 「…credential(s)…」句（凭据泄露） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An attacker may be able to modify the state of the Keychain | 「…Keychain…」句（钥匙串读写） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An attacker may be able to see recent photos without authentication in Assistive Access | 「…without authentication」句（无认证访问） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An attacker with knowledge of a standard user's credentials can unlock another standard user's locked screen on the same Mac | 「…credential(s)…」句（凭据泄露） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 1 | 0 |
| An attacker with physical access may be able to silently persist an Apple Account on an erased device | 「…Apple Account…」句（苹果账户） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An attacker with physical access to a Mac may be able to view protected content from the Login Window | 其余含三刀词句 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An attacker with physical access to a locked device may be able to bypass Digital ID authentication when iPhone Mirroring is active | 其余含三刀词句 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An attacker with physical access to an iOS device with Stolen Device Protection enabled may be able to access biometrics-gated Protected Apps with the passcode | 「…password／passcode…」句（口令本体） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An attacker with physical access to an unlocked device may be able to view Wi-Fi passwords without authentication | 「…without authentication」句（无认证访问） | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| An authorization issue was addressed with improved network restrictions. | 「An authorization issue was addressed…」句 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| An authorization issue was addressed with improved state management | 「An authorization issue was addressed…」句 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| An unauthenticated user may be able to use an unprotected Persona | 「…unauthenticated user…」句（未认证用户） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An unauthenticated user on the same network as a Compressor server may be able to execute arbitrary code | 「…unauthenticated user…」句（未认证用户） | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| Entitlements and privacy permissions granted to this app may be used by a malicious app | 其余含三刀词句 | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| Passcode may be read aloud by VoiceOver | 「…password／passcode…」句（口令本体） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 1 | 0 |
| Password fields may be unintentionally revealed | 「…password／passcode…」句（口令本体） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| Restoring from a backup may prevent passcode from being required immediately after Face ID enrollment | 「…password／passcode…」句（口令本体） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 1 | 0 |
| Shake-to-undo may allow a deleted photo to be re-surfaced without authentication | 「…without authentication」句（无认证访问） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed by adding a delay between verification code attempts. | 「…addressed by removing／adding … entitlements／a delay」句 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| **合计** | 28 桶 | 694 | 243 | 451 | 页列合计 514 | 79 | 13 | 0 | 19 | 5 |

# 附表 C 句形桶与优先序闭合分区（29 行）

| 桶 | 句次 | 段 | 页 | 名 | Impact 句 | Desc 句 | ∩R | ∩E |
|---|---:|---:|---:|---:|---:|---:|---:|---:|
| 「An authorization issue was addressed…」句 | 194 | 194 | 61 | 51 | 0 | 194 | 13 | 4 |
| 「This issue was addressed with additional entitlement checks.」句 | 99 | 99 | 60 | 29 | 0 | 99 | 0 | 0 |
| 「An authentication issue was addressed…」句 | 57 | 57 | 44 | 19 | 0 | 57 | 24 | 0 |
| 「…without authentication」句（无认证访问） | 43 | 43 | 33 | 11 | 43 | 0 | 1 | 0 |
| 「…Keychain…」句（钥匙串读写） | 37 | 37 | 27 | 8 | 37 | 0 | 0 | 0 |
| 其余含三刀词句 | 28 | 28 | 22 | 11 | 28 | 0 | 9 | 0 |
| 「…credential(s)…」句（凭据泄露） | 26 | 26 | 26 | 8 | 26 | 0 | 10 | 0 |
| 「…improved authentication」修复句 | 23 | 23 | 18 | 9 | 0 | 23 | 0 | 0 |
| 「…password／passcode…」句（口令本体） | 23 | 23 | 22 | 9 | 23 | 0 | 0 | 0 |
| 「…persistent account identifier」句（账户标识） | 18 | 18 | 8 | 3 | 18 | 0 | 0 | 0 |
| 「A cookie management issue…」句 | 17 | 17 | 17 | 1 | 0 | 17 | 6 | 5 |
| 「…bypass Pointer Authentication」句（指针认证） | 17 | 17 | 15 | 3 | 17 | 0 | 0 | 4 |
| 「A certificate validation issue…」句 | 11 | 11 | 8 | 1 | 0 | 11 | 3 | 0 |
| 「…without (user) authorization」句（无授权） | 10 | 10 | 10 | 3 | 10 | 0 | 0 | 0 |
| 「…Apple Account…」句（苹果账户） | 9 | 9 | 9 | 4 | 9 | 0 | 0 | 0 |
| 「…after failing authentication」句（认证失败后） | 9 | 9 | 9 | 3 | 9 | 0 | 0 | 0 |
| 「…improved entitlements／entitlement checks」修复句 | 9 | 9 | 9 | 3 | 0 | 9 | 0 | 0 |
| 「…unauthenticated user…」句（未认证用户） | 9 | 9 | 9 | 3 | 9 | 0 | 8 | 0 |
| 「A validation issue existed in the entitlement verification…」句 | 8 | 8 | 6 | 3 | 0 | 8 | 0 | 0 |
| 「…certificate authority／extended key usages」句（证书链） | 8 | 8 | 8 | 1 | 8 | 0 | 0 | 0 |
| 「…improved validation of the process entitlement」句 | 8 | 8 | 6 | 3 | 0 | 8 | 0 | 0 |
| 「A timing side-channel issue…」句 | 7 | 7 | 7 | 1 | 0 | 7 | 0 | 0 |
| 「…Cookies belonging to one origin…」句（跨源 Cookie） | 6 | 6 | 6 | 1 | 6 | 0 | 0 | 0 |
| 「A／An X was addressed…」通用修复句 | 5 | 5 | 5 | 1 | 0 | 5 | 0 | 0 |
| 「A cryptographic issue…」句 | 4 | 4 | 4 | 1 | 0 | 4 | 4 | 0 |
| 「A session management issue…」句 | 4 | 4 | 4 | 2 | 0 | 4 | 1 | 0 |
| 「…addressed by removing／adding … entitlements／a delay」句 | 3 | 3 | 3 | 2 | 0 | 3 | 0 | 0 |
| 「…now require restricted entitlements」句 | 2 | 2 | 2 | 1 | 0 | 2 | 0 | 0 |
| **合计** | 694 | 段列可重叠 | 页列合计 458 | 名列合计 195 | 243 | 451 | — | — |

## 附表 C 续表：句子打头与修复动词形态（优先序闭合分区）

| 形态刀 | 句次 | 段 | 页 | 名 |
|---|---:|---:|---:|---:|
| 「The／This issue was addressed …」打头 | 147 | 147 | 80 | 45 |
| 「A／An X issue was addressed …」打头 | 255 | 255 | 87 | 64 |
| 「A／An X（非 issue 名词）was addressed …」打头 | 39 | 39 | 36 | 5 |
| 「X existed（缺陷存在句）」 | 8 | 8 | 6 | 3 |
| 含「may」（影响句情态） | 233 | 233 | 101 | 49 |
| 其余句式 | 12 | 12 | 12 | 4 |
| **合计** | 694 | — | — | — |

# 附表 D 页面、编号、致谢、版式与字段落位分箱（27 行）

| 块 | 分箱 | 计数 | 说明 |
|---|---|---:|---|
| AUAT 段所在页的段数 | 1 | 67 | 共 159 页含本篇条目（全树 257 页） |
| AUAT 段所在页的段数 | 2 | 27 | 共 159 页含本篇条目（全树 257 页） |
| AUAT 段所在页的段数 | 3 | 16 | 共 159 页含本篇条目（全树 257 页） |
| AUAT 段所在页的段数 | 4 | 9 | 共 159 页含本篇条目（全树 257 页） |
| AUAT 段所在页的段数 | 5 | 13 | 共 159 页含本篇条目（全树 257 页） |
| AUAT 段所在页的段数 | 6-9 | 15 | 共 159 页含本篇条目（全树 257 页） |
| AUAT 段所在页的段数 | 10-19 | 8 | 共 159 页含本篇条目（全树 257 页） |
| AUAT 段所在页的段数 | 20-49 | 4 | 共 159 页含本篇条目（全树 257 页） |
| AUAT 段所在页的段数 | 合计 | 159 | 校验=159 页、AUAT 段 600 |
| Top10 页 | 前 10 页合计 | 193 | 占 600 段的 32.2% |
| 同页共存 | 159 页全部段 | 8040 | 本篇占该页集 7.46%，每页均 3.77 段（第三套分母） |
| 同页共存 | 全本篇页 | 8 | 该页所有段都被本篇抓到 |
| 编号数分箱 | 0 个 | 1 | 无编号段 |
| 编号数分箱 | 1 个 | 590 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 2 个 | 9 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 合计 | 600 | 带编号段=599／600，distinct 编号=188，本集独占=188，与他集共享=0 |
| 致谢人数分箱 | 1 人 | 349 | — |
| 致谢人数分箱 | 2 人 | 138 | — |
| 致谢人数分箱 | 3 人 | 47 | — |
| 致谢人数分箱 | 4 人 | 48 | — |
| 致谢人数分箱 | 5 人 | 6 | — |
| 致谢人数分箱 | 6-8 人 | 10 | — |
| 致谢人数分箱 | 9+ 人 | 1 | — |
| 致谢人数分箱 | 合计 | 599 | 人次=1073，均值=1.79，最大=15（全树 1.80／37） |
| 版式域 | 域 B（11 页粗体版式） | 6 | 域 B 全段 91，占比 6.59%；域 A 594／9,045 ＝ 6.57% |
| 字段落位 | Impact 侧 | 243 | Description 侧 443；仅I 157／仅D 357／I∩D 86（两组闭合 600） |
| 句级 | 600 段的本篇句数 | 1 句 506／2 句 94 | 句次合计 694（I 243＋D 451），distinct 句 85 |

# 附表 E 中文层 63 词（64 行）

| 组 | 中文词 | 出现次数 | 覆盖页 | 覆盖页占比 | 最集中页面 |
|---|---|---:|---:|---:|---|
| 认证与身份类 | 认证 | 380 | 93 | 52.2% | 安全隔区（29 次） |
| 认证与身份类 | 身份验证 | 5 | 3 | 1.7% | Apple 钱包中的证件（3 次） |
| 认证与身份类 | 身份 | 150 | 49 | 27.5% | Apple 钱包中的证件（22 次） |
| 认证与身份类 | 核验 | 0 | 0 | 0.0% | — |
| 认证与身份类 | 验证 | 432 | 178 | 100.0% | Apple 钱包中的证件（19 次） |
| 认证与身份类 | 双次认证 | 0 | 0 | 0.0% | — |
| 认证与身份类 | 两步 | 1 | 1 | 0.6% | 术语表（1 次） |
| 认证与身份类 | 多因素 | 0 | 0 | 0.0% | — |
| 凭据与材料类 | 密码 | 2790 | 178 | 100.0% | 密码监视（66 次） |
| 凭据与材料类 | 口令 | 1 | 1 | 0.6% | 密码（1 次） |
| 凭据与材料类 | 通行码 | 0 | 0 | 0.0% | — |
| 凭据与材料类 | 凭据 | 0 | 0 | 0.0% | — |
| 凭据与材料类 | 凭证 | 526 | 178 | 100.0% | NFC 与 SE 平台安全性（24 次） |
| 凭据与材料类 | 令牌 | 70 | 15 | 8.4% | 在 macOS 中管理文件保险箱（18 次） |
| 凭据与材料类 | 密钥 | 1641 | 178 | 100.0% | 用于数据保护的密钥包（64 次） |
| 凭据与材料类 | 钥匙串 | 1280 | 178 | 100.0% | 钥匙串数据保护（50 次） |
| 生物识别与设备解锁类 | 面容 | 674 | 178 | 100.0% | 视控 ID、面容 ID 和触控 ID 的用途（29 次） |
| 生物识别与设备解锁类 | 触控 | 874 | 178 | 100.0% | 配备触控 ID 的妙控键盘（41 次） |
| 生物识别与设备解锁类 | 生物识别 | 400 | 178 | 100.0% | 生物识别安全性（10 次） |
| 生物识别与设备解锁类 | 指纹 | 27 | 13 | 7.3% | 生物识别安全性（12 次） |
| 生物识别与设备解锁类 | 解锁 | 383 | 178 | 100.0% | 自动解锁 Apple 设备（70 次） |
| 生物识别与设备解锁类 | 锁定 | 305 | 178 | 100.0% | 安全隔区（18 次） |
| 生物识别与设备解锁类 | 密码自动填充 | 0 | 0 | 0.0% | — |
| 会话与账户类 | 会话 | 78 | 20 | 11.2% | 使用卫星收发紧急信息（11 次） |
| 会话与账户类 | 账户 | 1218 | 178 | 100.0% | Apple 账户安全性概览（32 次） |
| 会话与账户类 | 帐号 | 0 | 0 | 0.0% | — |
| 会话与账户类 | 账号 | 1 | 1 | 0.6% | NFC 与 SE 平台安全性（1 次） |
| 会话与账户类 | 登录 | 541 | 178 | 100.0% | iPadOS 中的“共享 iPad”安全性（33 次） |
| 会话与账户类 | 注销 | 0 | 0 | 0.0% | — |
| 会话与账户类 | Cookie | 0 | 0 | 0.0% | — |
| 会话与账户类 | 标识符 | 53 | 26 | 14.6% | iOS 中的车钥匙安全性（8 次） |
| 授权与权限类 | 授权 | 352 | 178 | 100.0% | NFC 与 SE 平台安全性（13 次） |
| 授权与权限类 | 权限 | 283 | 178 | 100.0% | 操作系统完整性（14 次） |
| 授权与权限类 | 许可 | 14 | 12 | 6.7% | 为 Apple 设备和服务验证配件（2 次） |
| 授权与权限类 | 同意 | 26 | 19 | 10.7% | 在 macOS 中安全扩展内核（3 次） |
| 授权与权限类 | 批准 | 49 | 25 | 14.0% | 在 macOS 中安全扩展内核（6 次） |
| 授权与权限类 | 限制 | 478 | 178 | 100.0% | LocalPolicy 签名密钥创建和管理（21 次） |
| 授权与权限类 | 访问控制 | 16 | 9 | 5.1% | 系统完整性保护（6 次） |
| 授权与权限类 | 特权 | 9 | 3 | 1.7% | macOS 中的 Option ROM 安全性（7 次） |
| 证书与密码学类 | 证书 | 150 | 29 | 16.3% | TLS 安全性（17 次） |
| 证书与密码学类 | 签名 | 1287 | 178 | 100.0% | 搭载 Apple 芯片的 Mac 的启动磁盘安全性策略控制（32 次） |
| 证书与密码学类 | 加密 | 1763 | 178 | 100.0% | 安全隔区（71 次） |
| 证书与密码学类 | 解密 | 76 | 41 | 23.0% | Tap to Pay on iPhone 安全性（9 次） |
| 证书与密码学类 | 哈希 | 124 | 30 | 16.9% | 搭载 Apple 芯片的 Mac 的 LocalPolicy 文件内容（27 次） |
| 证书与密码学类 | 证书校验 | 0 | 0 | 0.0% | — |
| 证书与密码学类 | 密码学 | 4 | 2 | 1.1% | Apple 操作系统中的量子安全加密技术（3 次） |
| 证书与密码学类 | 安全隔区 | 682 | 178 | 100.0% | 安全隔区（95 次） |
| 后果强度类 | 任意代码 | 0 | 0 | 0.0% | — |
| 后果强度类 | 代码执行 | 3 | 2 | 1.1% | 搭载 Apple 芯片的 Mac 上的 Rosetta 2（2 次） |
| 后果强度类 | 内存损坏 | 4 | 2 | 1.1% | 操作系统完整性（3 次） |
| 后果强度类 | 拒绝服务 | 1 | 1 | 0.6% | macOS 中的防火墙安全性（1 次） |
| 后果强度类 | 泄露 | 30 | 11 | 6.2% | 密码监视（15 次） |
| 后果强度类 | 崩溃 | 6 | 4 | 2.2% | Apple 操作系统中的后台安全改进（3 次） |
| 后果强度类 | 终止 | 13 | 7 | 3.9% | iOS 中的车钥匙安全性（4 次） |
| 后果强度类 | 冒充 | 1 | 1 | 0.6% | 车钥匙交易（1 次） |
| 修复套话类 | 改进的 | 3 | 3 | 1.7% | Apple 操作系统中的后台安全改进（1 次） |
| 修复套话类 | 附加验证 | 0 | 0 | 0.0% | — |
| 修复套话类 | 已修复 | 0 | 0 | 0.0% | — |
| 修复套话类 | 状态管理 | 0 | 0 | 0.0% | — |
| 修复套话类 | 新增 | 15 | 9 | 5.1% | 文稿修订记录（6 次） |
| 修复套话类 | 移除 | 104 | 38 | 21.3% | Apple 操作系统中的后台安全改进（17 次） |
| 修复套话类 | 约束 | 1 | 1 | 0.6% | 操作系统完整性（1 次） |
| 修复套话类 | 校验 | 19 | 3 | 1.7% | Apple 钱包中证件的安全性（16 次） |
| **合计** | 63 词（全零 14 词、非零 49 词） | — | — | 178 页可用 | — |

# 附表 F 组件名层 119 名（120 行）

| 组件名 | AUAT 段 | 页 | ∩R | ∩E | ∩M2 | ∩L | ∩BP | 该组件含 Impact 全集 | AUAT 占比 |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| Security | 31 | 21 | 3 | 0 | 0 | 0 | 0 | 91 | 34.1% |
| WebKit | 28 | 26 | 6 | 5 | 0 | 0 | 8 | 910 | 3.1% |
| Accounts | 20 | 20 | 0 | 0 | 0 | 5 | 5 | 34 | 58.8% |
| Kernel | 19 | 19 | 0 | 0 | 0 | 7 | 0 | 681 | 2.8% |
| AirPlay | 14 | 7 | 14 | 0 | 0 | 0 | 7 | 77 | 18.2% |
| Authentication Services | 14 | 9 | 5 | 0 | 0 | 0 | 0 | 18 | 77.8% |
| Shortcuts | 14 | 13 | 0 | 0 | 0 | 0 | 0 | 129 | 10.9% |
| Accessibility | 13 | 13 | 4 | 4 | 0 | 1 | 0 | 46 | 28.3% |
| NetworkExtension | 13 | 13 | 0 | 0 | 0 | 0 | 0 | 28 | 46.4% |
| Siri | 12 | 11 | 0 | 0 | 0 | 0 | 0 | 116 | 10.3% |
| Safari Private Browsing | 11 | 10 | 0 | 0 | 0 | 0 | 0 | 16 | 68.8% |
| App Store | 10 | 10 | 0 | 0 | 0 | 0 | 0 | 24 | 41.7% |
| Apple Account | 10 | 9 | 0 | 0 | 0 | 0 | 0 | 25 | 40.0% |
| Assets | 10 | 10 | 0 | 0 | 0 | 0 | 3 | 16 | 62.5% |
| AppleMobileFileIntegrity | 9 | 9 | 0 | 0 | 0 | 0 | 0 | 146 | 6.2% |
| CloudKit | 9 | 9 | 0 | 0 | 0 | 0 | 0 | 25 | 36.0% |
| 802.1X | 8 | 8 | 8 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| AuthKit | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 13 | 61.5% |
| Bluetooth | 8 | 7 | 7 | 0 | 0 | 0 | 0 | 41 | 19.5% |
| DeviceCheck | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| Mail | 8 | 4 | 0 | 0 | 0 | 0 | 0 | 36 | 22.2% |
| Photos | 8 | 7 | 0 | 0 | 0 | 0 | 0 | 19 | 42.1% |
| Power Services | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| Spotlight | 8 | 5 | 0 | 0 | 0 | 0 | 0 | 69 | 11.6% |
| CoreCrypto | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| CoreMotion | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| Heimdal | 7 | 6 | 4 | 0 | 0 | 0 | 0 | 19 | 36.8% |
| MediaRemote | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 24 | 29.2% |
| Photos Storage | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 18 | 38.9% |
| curl | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 39 | 17.9% |
| CoreServices | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 81 | 7.4% |
| IOKit | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 26 | 23.1% |
| Managed Configuration | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 17 | 35.3% |
| XPC | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 7 | 85.7% |
| iCloud Document Sharing | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Contacts | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 25 | 20.0% |
| FaceTime | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 14 | 35.7% |
| FileProvider | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 18 | 27.8% |
| Music | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 10 | 50.0% |
| PackageKit | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 106 | 4.7% |
| Safe Browsing | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| Telephony | 5 | 4 | 2 | 0 | 0 | 0 | 2 | 8 | 62.5% |
| Transparency | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 12 | 41.7% |
| WebKit Sandboxing | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| dyld | 5 | 5 | 0 | 0 | 0 | 0 | 5 | 15 | 33.3% |
| Core Bluetooth | 4 | 4 | 4 | 0 | 0 | 0 | 0 | 16 | 25.0% |
| Data Detectors UI | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| IOHIDFamily | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 27 | 14.8% |
| LoginWindow | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| NSColorPanel | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 7 | 57.1% |
| Parental Controls | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| Passkeys | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 5 | 80.0% |
| Passwords | 4 | 4 | 0 | 0 | 0 | 1 | 2 | 9 | 44.4% |
| RPAC | 4 | 4 | 0 | 4 | 0 | 0 | 4 | 5 | 80.0% |
| Screen Sharing Server | 4 | 4 | 2 | 0 | 0 | 0 | 0 | 16 | 25.0% |
| Touch Bar | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| Wi-Fi Connectivity | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| AppleEvents | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Audio | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 95 | 3.2% |
| Automator | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 9 | 33.3% |
| CUPS | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 43 | 7.0% |
| Compression | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 9 | 33.3% |
| CoreMedia | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 150 | 2.0% |
| CoreServicesUIAgent | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 4 | 75.0% |
| DiskArbitration | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 25 | 12.0% |
| Kext Management | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Keychain Access | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 6 | 50.0% |
| Login Window | 3 | 2 | 0 | 0 | 0 | 1 | 1 | 6 | 50.0% |
| NetAuth | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 15 | 20.0% |
| Printing | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 17 | 17.6% |
| QuickTime Player | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Safari | 3 | 3 | 0 | 0 | 0 | 1 | 0 | 92 | 3.3% |
| Sandbox | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 65 | 4.6% |
| Screen Sharing | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Single Sign-On | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 6 | 50.0% |
| SpringBoard | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Touch Bar Controls | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Voice Control | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 16 | 18.8% |
| WebDAV | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 20 | 15.0% |
| macOS Recovery | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| sudo | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 10 | 30.0% |
| CoreTypes | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Dock | 2 | 2 | 0 | 0 | 0 | 2 | 0 | 19 | 10.5% |
| Face Gallery | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| LaunchServices | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 58 | 3.4% |
| Libnotify | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 10 | 20.0% |
| Live Captions | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| MigrationKit | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 12 | 16.7% |
| Notes | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 38 | 5.3% |
| Power Management | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 7 | 28.6% |
| Privacy | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| RepairKit | 2 | 2 | 0 | 0 | 0 | 0 | 2 | 2 | 100.0% |
| Sandbox Profiles | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 23 | 8.7% |
| StorageKit | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 59 | 3.4% |
| Sync Services | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 12 | 16.7% |
| TV App | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 8 | 25.0% |
| VoiceOver | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 14 | 14.3% |
| WorkoutKit | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| configd | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 9 | 22.2% |
| mDNSResponder | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 53 | 3.8% |
| App Protection | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| App Store Connect | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Apple Intelligence | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| Apple Music Classical | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Application Firewall | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| BiometricKit | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 8 | 12.5% |
| Compressor | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Disk Management | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| Find My | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 19 | 5.3% |
| IDE Documentation | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Image Capture | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 7 | 14.3% |
| MobileLockdown | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| NSOpenPanel | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 50.0% |
| Persona | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Phone | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 10 | 10.0% |
| Reset Services | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| User Management | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Wallet | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 1 | 100.0% |
| Wi-Fi | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 58 | 1.7% |
| **合计** | 600 | 页列合计 560 | 64 | 13 | 0 | 19 | 40 | 这 119 名 Impact 全集 4220 段（全树 7,434 的 56.8%） | — |

# 17 结论

选题给的是"身份认证材料""认证校验动作""会话与授权载体"三类措辞。逐词、逐句、逐字段测量后，本篇结论与选题字面预期的关系如下：

**一、主集 600 段里 Description 侧 443 段 ＞ Impact 侧 243 段——认证类条目的措辞重心在"修了什么"，不在"能干什么"。** 这与内存破坏类（#132／#135，Impact 侧压倒）正好相反：Impact 243∶Description 443∶I∩D 86；仅 Description 的 357 段（59.50%）里**刀一材料词一段都没有**（0∶64∶301）。**"丢了什么材料"只有 100 段写得出来，其余全靠套话句。**

**二、字段分工是本篇最硬的结构：材料词全挂 Impact，校验动作词全挂 Description。** `keychain*` 37／37 全 Impact、`password` 23、`passcode` 10、`credential` 29、`account(s)` 33、`token` 4 全部 I-only；反向 `verif*` 9、`cryptograph*` 11、`session(s)` 4 **全部 D-only**，`authoriz*` 198 段里 **194 段写在 Description**（Impact 侧只有 11 段，其中 7 段两侧都写、**Impact-only 仅 4 段**）。三刀合计 I∩D 只有 86 段。**同一批 CVE 的"后果句"与"修复句"用的是两套不重叠的词汇表。**

**三、三刀重叠 66 段全部是同词跨字段，跨词共段为 0。** 仅刀一 57∶仅刀二 138∶仅刀三 343，刀二∩刀三 19、刀一∩刀二 23、刀一∩刀三 16、三刀同段 **4**（4 段全是 Mail，Impact 写"泄露 Mail 账户凭据"、Description 写"authentication issue … improved state management"）。**"认证材料—认证动作—授权载体"这三段语义在真实条目里极少同时出现。**

**四、`improved state management` 一句就把本篇与 #134 连起来，也把它们分开。** 该短语全树 662 段、∩主集 **270**；`manag*` 词族∩主集 **274** 与 #134 媒体资源刀∩主集 **274** 是同一批段（成员交 274）；**而 #134 摘掉 `manag*` 后剩 675 段，与本篇 600 段交 0。** 姊妹篇之间 45.67% 的"重合"由一句修复套话单独制造，除此之外两篇正交。

**五、"认证"不等于"绕过"，也不等于"任意代码执行"——两个都是零。** `bypass` 词族 301 段只有 **40** 段进本篇（且这 40 段与 BYP 后果类 40 段**逐段相同**）；主集内 BYP 40 段与 DIS 23 段共现 **0**；`arbitrary code execution` 218 段、`memor*` 1,967 段、`out of bounds` 816 段、UAF 193／127 段与主集交 **全部为 0**。**后果八类里 0 类段占 80.83%、≥2 类段 0 段**：认证类条目的后果句只写一个动作，不写组合后果。

**六、词界断言第五次发作，本篇是五篇里最轻的一次，且两批新增都是同词根。** 去前置 ＋32（32 段全含 `unauthorized`、0 段含独立 `authoriz*`）、去尾 ＋2（`Account-driven`）。`sign…in` 的无界匹配 61 段里正写只有 5 段，真凶是 `design*`（三字段 **0 段**）。**词界问题在本篇不构成风险，但"宽松匹配把派生词当独立词"这条仍要在每次普查时复核。**

**七、组件层：119 个名扛 600 段，头部五名只占 18.67%，六个组件名代理最高命中率 36.36%。** `Authentication Services` 名下 77.8% 进本篇、`WebKit` 名下只有 3.1%；`Keychains／Faces／Apple Pay／Kerberos／CoreAuthentication` 五个专名组件**全树 0 段**；`keychain*` 词族 37 段里 20 段挂 `Security`，而叫 `Keychain Access` 的组件名下只有 3 段。**用组件名圈"认证类条目"会同时漏掉与错抓；`authoriz*`（52 名）与 `keychain*`（8 名）在同一根刀里就有横切与纵切两种散布形态。**

**八、中文层与英文判据层再次不可互换，且本篇出现全系列最极端的一对。** `signature(s)` 判据 **0 段**∶中文 `签名` **1,287 次／178 页**；`improved state management` ∩主集 **270 段**∶中文 `状态管理` **0 次**；`cookie(s)` 17 段∶中文 `Cookie` **0 次**；`passcode` 10 段∶中文 `通行码` **0 次**；`credential` 29 段∶中文 `凭据` **0 次**（而 `凭证` 526 次是一词多义）。**63 词里 14 个全零，其中 5 个属于"漏洞披露文体专有词"——中文手册不是缺陷披露文本，这一句在本篇得到最直接的验证。**

**规则：**
1. **凡"A 与 B 得数相同"必同时报 |A|、|B|、|A∩B|**：98∶98 交 81 是本篇反例，40∶40 交 40、274∶274 交 274 是正例。**得数相同既可能同集、也可能各有一半不同。**
2. **段轴、句轴、字段轴三根数不可混用**：600（段）／694（句次）／243（Impact 段＝Impact 句次）／451（Description 句次）／443（Description 段）各有含义。**本篇特别警告：两句段 94 ≠ I∩D 86**，因为另有 8 段是"两句 Description"，这与 #135 的 30＝30 形态不同，不能沿用姊妹篇的换算。
3. **"摘刀"要点名口径**：剔"含该刀任一词的段"得 500／416／218，剔"仅该刀"段得 543／462／257——**差值就是共现段数**，引用时不说清就是错。
4. **主集一律两字段口径，致谢只做哨兵**；致谢命中 0、三字段并集＝两字段并集时，仍要单独复核宽松词表与机构名（本篇 `Threat Analysis Group` 94∩5 属判据字段自命中，不是致谢驱动；`permission／consent` 639 段里 ∩主集 37，而 `authoriz*` 与 `permission(s)` 的成员交全树 **0**、只与 `consent` 交 **7**——**两族是否同批段只能靠成员交，不能靠"都叫授权"）。**
5. **跨篇重合必须能指到具体短语**：与 #134 的 274 段能指到 `manag*`、与 #126 的 40 段能指到 `bypass`、与 #128 的 65 段指到远程通道词族；**指不到短语的"主题相邻"一律按 0 处理**（与 #130 交 0、与 #133 交 23）。

---
**边界声明：** 本报告是公开英文措辞与中文手册文本的计量分析（防御与研究视角），只做"哪个词在哪个字段、哪一句位、与哪个后果短语搭配、与哪一组段落相交"的清点。全篇不含绕过方法、不含利用代码、不含恶意样本或利用链构造方式、不含针对他人设备的操作步骤与时序。文中"凭据泄露""认证问题""授权载体""绕过认证""账户标识符"等表述，一律指**公开条目使用了该词或该短语这一文本事实**，不是对该条目真实缺陷类别、CWE 归属、是否可被远程触达、是否被利用的判断，也不构成对任何认证机制、密钥链、密码策略、授权模型或账户系统安全性的结论。所有集合规模依赖第 1 章的三刀定义与四套分母，更换词表、字段口径或词界断言都会改变得数，本篇已逐项给出可复算的闭合校验。

Telegram: https://t.me/one00190
