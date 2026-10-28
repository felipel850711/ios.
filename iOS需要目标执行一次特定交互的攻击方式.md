@@TITLE@@iOS 需要目标执行一次特定交互的攻击方式
@@SUBTITLE@@选题把**触发前提（目标先做一次交互）**与**交互本身（一次、特定）**捆在一起。逐词测量的结果是：措辞层里确实存在一类"把人的动作写在主语位"的句子——本篇定义为 **HI 集**（对照集 G 去掉 `processing`／`parsing`）＝ **273 段 / 102 页 / 35 名 / 19 个句首 `-ing` 形 / 48 条 distinct 段全文**，占含 Impact 的 7,434 段的 **3.67%**。但这 273 段里**「谁做的」与「做几次」两个信息都不存在**：**施事刀**（`the user` **0**、`a user` **0**、`they`／`them`／`their` **0**、`you`／`your` **0**、`he`／`she`／`him`／`her` **0**、`victim` **0**、`people`／`person` **0**；含 `user` 的只有 **25 段 = 9.2%**，其右邻词只有 `interface` 19 与 `contact` 6，**0 段**把 `user` 接在情态动词之前）与**一次量化刀**（`one`／`once`／`each`／`every`／`first`／`twice`／`any`／`single`／`a single`／`subsequent`／`initial` **全 0**，唯一量化手段是不定冠词 `a` **261 段 = 95.6%**、`an` **34 段**，而 **57 段 = 20.9% 的宾语是复数**）。第三把是**情态刀**：HI 内 `may` **265** ＋ `could` **5** ＋ `can` **3** = **273**（闭合），`will` **0**，而 **`may be able` 0 段**——#119 的 WO 集里 `may be able to` 占 **123／178 = 69.1%**，两个集在同一个语料里用**两套不同的情态结构**：写"能力"时用人作主语，写"后果"时用动作作主语。第四把是**从句刀**：HI 内 `if`／`when`／`before`／`upon`／`then`／`unless` **全 0**，`after` **1**、`while` **2**、`during` **1**——触发条件**只用动名词主语表达，不用条件从句表达**。第五把是**接缝刀**：HI ∩ WO = **0**、HI ∪ WO = **451 段 = 6.1%**，"要交互"与"无交互"在措辞层是**两套互斥句式**而不是同一句话的两个取值。
@@INFO@@报告日期|2026 年 10 月 28 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 7,434 段含 Description / 9,132 段含致谢**。交互触发句集 HI = 对照集 G 去掉 `processing` 与 `parsing` ＝ **273 段 / 102 页 / 35 名 / 19 个 `-ing` 形 / 48 条 distinct 段全文**（真动名词 **18 词 / 270 段**，`incoming` 的 3 段是形容词）。对照集 G = #116 的动名词集（Impact **首句**以 `\w+ing` 开头）**2,179 段 / 224 页 / 98 名 / 21 词 / 136 条 distinct 首句**；WO = #119 的无前提集（Impact 含 `without` 词界）**178 段 / 97 页 / 37 名**；R = #118 的远程触达集（Impact 侧 11 个通道词族）**843 段 / 178 页 / 72 名**；U = Impact 含 `user`（词界）**1,627 段 / 175 页 / 200 名 = 21.9%**；Impact 不含 `user` **5,807 段**。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**公开英文文本把哪些动作放在主语位、这些动作句里写不写施事者与次数、`user` 这个词在句中承担哪五种角色、以及"需要交互"与"无需交互"两套句式在词汇与句法上如何互斥**，属于措辞与文本计量分析；文中任何"需要交互"表述均指**措辞层的句式分布**，不是对某组件实际触发条件的判断
@@INFO@@分类|判据**电池 84 项 / 三字段并集全零 59 项 / 非零 25 项**（附表 A，五组：需求与前提动词 16、条件从句连词 14、交互名词与触屏词 18、交互动词限定式 16、一次量化与副词 20；其中 **Impact 侧为零但致谢侧非零 2 项**：`tap` 词族致谢 4、`drag` 词族致谢 11）/ 对照集 G 的 **21 个句首动名词 × 段页名 × ∩WO × ∩R × ∩user**（附表 B，22 行含合计，段合计 **2,179**）/ **第一个 `user` 的右邻词 25 分区互斥闭合 = 1,627**（附表 C，26 行含合计，八类角色小计 1,065＋158＋128＋116＋71＋48＋21＋20＝1,627）/ **组件名 × HI 矩阵 35 行**（附表 D，36 行含合计，段合计 **273**、该 35 名的 Impact 全集 **2,386**）/ **中文层 53 项**（附表 E，六组：触屏动作 9、交互名词 8、确认动作 9、一次量化 6、前提否定 8、动作动词 13；**全零 10 项、非零 43 项**）/ **五把刀**：施事刀（HI 内六种人称主语全 0）、一次量化刀（11 个量化词全 0）、情态刀（265＋5＋3＝273、`may be able` 0）、从句刀（6 个从句词全 0）、接缝刀（HI ∩ WO＝0、HI ∪ WO＝451）
@@INFO@@难度|进阶（第 4 章的 17 类宾语首词是把 273 段的首个实词按**剥离冠词之后**归档的结果，只剥一层冠词——`Visiting a website` 计入 `website`、`Opening a maliciously crafted file` 计入 `maliciously`，写成"取宾语中心词"会掉到 12 类且不闭合；第 5 章的 95.6% 是 `a` 的**段级覆盖率**而不是出现次数，且必须与 20.9% 的复数宾语同时报，否则会被读成"全部是单数一次"；第 11 章的 25 个右邻词要求只取**第一个** `user`，且 `user's` 必须拆成 `user`＋`'s` 两形——把 `user's` 整体当 `user` 会让所有格那一行从 128 掉到 0；第 16 章的 `HI ∩ R = 108` 与 `G ∩ R = 108` 是**已验证的集合恒等**（`processing`／`parsing` 的 1,906 段与 R 零交），不是巧合，也不意味着 HI＝G；第 12 章的九串并集 275 段与直接相加 287 段差 12，来自两处重合——`if the user` 与 `user has visited` 是**同 6 段同一句**、`another user` 与 `user with X` 重合 6 段）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

选题字面是"需要目标执行一次特定交互的攻击方式"。这句话里可以被测量的成分有三个：**有没有把"人的动作"写成句子的前提**、**写不写这个动作由谁执行**、**写不写执行几次**。逐词测量的答案分别是：**有，但只有 3.67%；不写；也不写。**

**一、有一类句子把人的动作放在主语位。** 把 #116 的动名词集 G（Impact 首句以 `\w+ing` 开头，2,179 段）去掉 `processing`（1,760 段）与 `parsing`（146 段）这两个**机器动词**，剩下的就是本篇的主集合 **HI**：**273 段 / 102 页 / 35 名 / 19 个 `-ing` 形 / 48 条 distinct 段全文**。它们的形如 `Visiting a malicious website may lead to user interface spoofing`（19 段）、`Mounting a maliciously crafted disk image may cause unexpected system termination…`（10 段）、`Connecting to a malicious NFS server may lead to kernel memory corruption`（12 段）。（第 3、4 章）

**二、但这 273 段一个都没写"谁"。** `the user` **0 段**、`a user` **0**、`they`／`them`／`their` **0**、`you`／`your` **0**、`he`／`she`／`him`／`her`／`his` **0**、`victim` **0**、`people`／`person`／`individual` **0**。含 `user` 的只有 **25 段（9.2%）**，而这 25 段的 `user` 右邻词只有两个——`interface`（19）与 `contact`（6）——**全部是定语，不是施事**；把 `user` 后面接情态或完成动词（`user may`／`user has`）的段数在 HI 内是 **0**。（第 10 章）

**三、"一次"没有词。** 附表 A 的"一次量化与副词"组 20 项里，`one` **9 段但与 HI 只交 1 段**、`a single`／`single`／`once`／`first`／`initial`／`twice`／`each`／`every`／`manual`／`manually`／`unattended`／`knowingly`／`unknowingly`／`deliberately`／`intentionally`／`voluntarily` **Impact 全 0**。HI 内部单独复测：`one` **0**、`each` **0**、`twice` **0**、`any` **0**、`every` **0**、`first` **0**、`subsequent` **0**——只剩不定冠词 `a` **261 段（95.6%）** 与 `an` **34 段（12.5%）**。更关键的是 **57 段（20.9%）的宾语是复数**（`malicious websites`、`protected system files`），这类句子在字面上连"一个"都不成立，更别说"一次"。（第 5 章）

**四、触发条件不用从句写。** 附表 A 的"条件从句连词"组 14 项里 13 项为零，唯一非零是 `if the user`（**6 段**，且这 6 段与 HI **零交**）。HI 内部复测：`if`／`when`／`before`／`upon`／`then`／`unless` **各 0 段**，`after` **1**、`while` **2**、`during` **1**。措辞层表达"做完某事之后"的**唯一句法手段是动名词主语**，不是 `when/if you …`。（第 6 章）

**五、"需要"这个动词几乎不存在，存在的 13 段全在说"绕过需要"。** `require`／`requires`／`requiring`／`need`／`needs`／`needed`／`necessary`／`must`／`shall`／`has to`／`have to`／`prerequisite`／`precondition`／`prior to`／`in order to` 在 Impact 侧**全部 0 段**。`requir` 词族共 **13 段**，其中 **12 段是同一句** `A shortcut may be able to bypass Internet permission requirements`（Shortcuts，页 120908–120916），第 13 段是 `Restoring from a backup may prevent passcode from being required immediately after Face ID enrollment`（BiometricKit）。也就是说：**措辞层里"要求"这个词只出现在"要求被绕过"的句子里，从不出现在"要求你做什么"的句子里。**（第 8 章）

**六、"交互"这一维与"无需交互"那一维互斥。** HI ∩ WO（#119 的 178 段无前提集）= **0 段**；HI ∪ WO = **451 段 = 7,434 的 6.1%**。这不是"同一维度的两个取值"，而是**两套句式**：WO 的主语是程序或数据、谓语是 `may be able to`、句尾挂 `without` 宾语；HI 的主语是一个动作、谓语是 `may lead to`、**句尾不挂任何东西**。（第 15 章）

于是本篇的问题依次是：

**问题一（第 3、4 章）** 把人的动作写成主语的句子有多少、都是些什么动作。答案：**273 段**，19 个 `-ing` 形（真动名词 18 词 270 段，`incoming` 3 段为形容词），宾语首词 **17 类互斥闭合到 273**。

**问题二（第 10、11、13 章）** 这些句子写不写施事者；`user` 这个词在 1,627 段里承担什么角色。答案：HI 内**零**；`user` 的五个角色按右邻词分区为**数据属主 1,065 / 施事·情态 158 / 所有格 128 / 句末落点 116 / 授权名词 71 / 介词短语 48 / 界面 21 / 未解码所有格 20**。

**问题三（第 12 章）** 正面写"用户先做过某事"的句式一共有哪些。答案：**九串**（`user may` 122、`fingerprint the user` 97、`user has installed` 28、`another user` 9、`user with X` 9、`trick a user` 8、`if the user` 6、`user has visited` 6、`user is logged` 2），并集 **275 段 / 136 页 / 54 名**，与 HI **零交**、与 WO **零交**。

**问题四（第 7 章）** 这 273 段用什么情态动词和什么后果动词。答案：`may` 265 ＋ `could` 5 ＋ `can` 3 = **273**；`will` **0**、`may be able` **0**；后果动词 `lead to` **153（56.0%）**、`cause` **39**、`allow` **12**（12 段与 `attacker` **完全同段**）、`disclose` 10、`expose` 6、`result in` 7。

**问题五（第 14 章）** 哪些组件名被写成"动作触发"。答案：**472 名里 35 名 = 7.4%**；其中 **6 名的 Impact 段 100% 落在 HI**（zlib 8、WebKit History 7、exFAT 7、file_cmds 6、Disk Utility 3、Git 2，合计 **33 段 = HI 的 12.1%**）；Top 是 Safari 37（占该组件 40.2%）、WebKit 30、SMB 27、Kernel 26。

**问题六（第 19 章）** 中文手册层写不写"一次交互"。答案：**写得比英文多，但仍是手册不是通告**——`轻点` **13 次 / 10 页**、`点按` **1**、`点击` **1**、`长按`／`拖动`／`滑动`／`缩放`／`触控板`／`触摸` **各 0**；`一次` **46 次 / 24 页**、`首次` **62 / 35**、`每个` **106 / 58**、`交互` **19 / 16**、`手动` **27 / 19**；`挂载`、`静音`、`应答`、`先后` **0**。

# 1 操作性定义与分母

**T2（措辞层）**：`_tmp/adv2/<id>.html` 共 258 份，排除页面 `122085` 得 **257 页**。按 #111 第 1 章的两版式域切块，得 **9,136 段**（域 A **9,045** ＋ 域 B **91**）、**257 页**、**472 个组件名**；含 Impact **7,434**、含 Description **7,434**、含致谢 **9,132**。

**段（block）**＝一个组件条目，`{imp, des, ack, nm, id}` 五字段；**判据命中**＝该段任一字段出现该词形；**并集**＝Impact∪Description∪致谢 三字段**分别匹配后取并集**；**拼接**＝三字段首尾相接后匹配（本篇用两者之差做第 18 章的解码缺陷检查）。

**词界**用 `(?<![\w-])kw(?![\w-])`；**词族**用前缀 `(?<![\w-])kw`。词组内空格统一用 `$S = [ \x{a0}\x{202f}]`；连字符类 `$H = [ \x{a0}\x{202f}-]`，**连字符必须放在字符类末尾**。所有字段匹配一律带 `/i`。

**核心集合（本篇定义）**：

- **G**（触发式动名词集，#116 定义原样复用）= Impact **首句**（按 `(?<=[.!?])\s+(?=["A-Z])` 切句后的第一句）以 `\w+ing` 开头 ＝ **2,179 段 / 224 页 / 98 名 / 21 词 / 136 条 distinct 首句**。注意定义里的三个约束：**只看首句**、**必须整词以 `-ing` 收尾**、**大小写不敏感**。
- **HI**（交互触发句集，**本篇新增**）= G 去掉首句动名词为 `processing` 或 `parsing` 的段 ＝ **273 段 / 102 页 / 35 名 / 19 个 `-ing` 形 / 48 条 distinct 段全文**；真动名词 **18 词 / 270 段**（`incoming` 的 3 段是形容词，见第 4 章）。剥离量：`processing` **1,760** ＋ `parsing` **146** = **1,906**，`1,906 ＋ 273 = 2,179` ✓。
- **WO**（#119 无前提集）= Impact 含 `without` 词界 ＝ **178 段 / 97 页 / 37 名**。
- **R**（#118 远程触达集）= Impact 侧 11 个通道词族（`remote`／`network`／`internet`／`webs(ite|ite)`／`Wi-Fi`／`wireless`／`Bluetooth`／`AirPlay`／`AirDrop`／`USB`／`proximity`／`cellular`／`packet`）＝ **843 段 / 178 页 / 72 名**。
- **U**（用户词集）= Impact 含 `user` 词界 ＝ **1,627 段 / 175 页 / 200 名 = 21.9%**；Impact 不含 `user` **5,807 段**。
- **AS**（施事·情态集，第 11 章的角色分区结果）= U 中第一个 `user` 的右邻词为 `may`／`has`／`is` ＝ **158 段**（122＋34＋2）。

**为什么 HI 要剥掉 `processing` 与 `parsing`**：这两个词的主语位是**软件自身**（`Processing maliciously crafted web content…`、`Parsing a file…`），不构成"目标要做某事"；它们合计 1,906 段，占 G 的 **87.5%**，若不剥掉，任何"交互触发"的比例都会被机器动词淹掉。它们的宾语也证明这一点：`processing` 的宾语首词 9 项（`maliciously` 1,502、`web` 73、`image` 69、`file` 58、`audio` 29…）、`parsing` 的 3 项（`file` 91、`maliciously` 47、`image` 8）**全是数据对象**，而 HI 的 17 类宾语里有 `website`、`disk image`、`conversation`、`call` 这类需要人去打开的东西。

# 2 结论速览（每条都可按第 20 章复算）

**一、规模。** HI = **273 段 / 102 页 / 35 名**，占含 Impact 的 7,434 段的 **3.67%**、占全树 257 页的 **39.7%**；**域 A 273 段、域 B 0 段**（HI 完全不在粗体版式域里）。

**二、动作只有 19 个形。** `visiting` 82、`connecting` 58、`mounting` 40、`opening` 21、`restoring` 18、`playing` 8、`deleting` 6、`extracting` 6、`running` 6、`unpacking` 6、`replying` 4、`turning` 4、`copying` 3、`incoming` 3、`cloning` 2、`loading` 2、`muting` 2、`enabling` 1、`forwarding` 1 ＝ **273**。

**三、宾语 17 类闭合。** `maliciously` 97、`to` 58、`malicious` 57、`website` 26、`conversation` 6、`disk` 5、`off` 4、`facetime` 3、`file` 3、`hdiutil` 3、`mount` 3、`url` 3，其余 5 类各 1 段，合计 **273**。

**四、施事零存在。** `the user`／`a user`／`they`／`you`／`he`／`she`／`victim` 在 HI 内**全部 0 段**；`device`／`iPhone`／`iPad` 3 段（1.1%）、`app`／`application` 17 段（6.2%），且这 17 段里的 `app` 都是**后果宾语**而非主语。

**五、"一次"零存在。** HI 内 `one`／`each`／`twice`／`any`／`every`／`first`／`subsequent` **各 0 段**；`a` 261（95.6%）、`an` 34（12.5%）、`the` 4（1.5%）；**复数宾语 57 段（20.9%）**。

**六、情态闭合到 273。** `may` 265 ＋ `could` 5 ＋ `can` 3；`will` **0**、`may be able` **0**、`must` **0**。不含 `may` 的 8 段恰好是 `replying` 4 ＋ `forwarding` 1（`could`，Mail／Lockdown Mode）与 `incoming` 3（`can`，FaceTime）。

**七、`require` 只与"绕过"同现。** `requir` 词族 Impact **13 段**，12 段同一句"bypass Internet permission requirements"、1 段"prevent passcode from being required"；∩WO **0**、∩R **12**、∩HI **1**（唯一在 HI 内的那段是 BiometricKit 的 `restoring`，见第 8 章）。

**八、交互名词全零。** `interaction`／`user interaction`／`interact` 词族／`user action`／`user input`／`user gesture`／`gesture`／`selection`／`verification`／`approval`／`user approval`／`opt in`／`opt out` 在 Impact 侧**全 0**；`click`／`tap`／`swipe`／`press`／`drag`／`scroll`／`toggle` 在 Impact 侧**全 0**（`tap`、`drag` 只出现在致谢侧，4 段与 11 段）。

**九、`user` 的五种角色。** 附表 C 的 25 个右邻词闭合到 1,627；`data` 独占 **880 段（54.1%）**，`may` 122、`consent` 47、`has` 34、`interface` 21；`user` 落在 Impact **末尾**的有 **116 段**，尾部只有三种：`fingerprint the user` 97、`prompting the user` 16、`to another user` 3。

**十、正面句式与 HI 互斥。** 九串并集 **275 段 / 136 页 / 54 名**，与 HI **零交**、与 WO **零交**、与 R 交 **49**。

**十一、组件高度集中。** 35 名里 Top4（Safari 37、WebKit 30、SMB 27、Kernel 26）占 **120 段 = 44.0%**；**8 名的 HI 占比 ≥ 50%**：zlib 100.0%（8／8）、WebKit History 100.0%（7／7）、exFAT 100.0%（7／7）、file_cmds 100.0%（6／6）、Disk Utility 100.0%（3／3）、Git 100.0%（2／2）、afpfs 66.7%（8／12）、Compression 66.7%（6／9）。

**十二、与 #118 的接口是一个恒等式。** `HI ∩ R = 108` 且 `G ∩ R = 108`（已验证两集合相等）；HI 内 **165 段（60.4%）不声明任何通道**，其中 `connecting` 独占 **58 段**——`Connecting to a malicious NFS/SMB server` 里的 `server`、`NFS`、`disk image` 都不在 R 的 11 个词族里（`server` 51 段、`NFS` 25 段、`disk image` 21 段在 HI 内**全部为 R 的新增**）。

**十三、镜像解码缺陷。** `&rsquo;` 残留在 Impact **24 段 / 24 页 / 9 名**（三字段并 32 段）、`&rdquo;` **4 段 / 4 页 / 1 名**；这直接导致附表 C 里 `&rsquo;s` 必须与 `'s` 分成两行（20 段与 128 段），合并后所有格读数为 **148 段**。（第 18 章）

# 3 HI 集的定义：从 G 里剥掉两个机器动词

**G 的构成先摆出来。** 附表 B 逐词给出 21 个 `-ing` 形的段／页／名／∩WO／∩R／∩user／distinct 段全文／宾语首词 Top3。合计行钉住 **2,179 段 / 224 页 / 98 名 / ∩WO 0 / ∩R 108 / ∩user 100 / distinct 136**。

**两个机器动词占 87.5%。** `processing` **1,760 段 / 219 页 / 67 名**，distinct 首句 86 条；`parsing` **146 段 / 65 页 / 19 名**，distinct 8 条。两者相加 **1,906**，且各自的 ∩WO 都是 **0**、∩R 也都是 **0**——这是第 16 章恒等式成立的前提。

**剥离之后剩下什么。** HI 的 19 个形按语义分五组、段数相加即 273：

- **网络与主机侧 140 段**：`visiting` 82、`connecting` 58。这两个词的动作对象是网站与服务器，读起来"必须有人打开一个链接"，但 `visiting` 的 82 段**全部**命中 R（∩R = 82），`connecting` 的 58 段**一段都不命中** R（∩R = 0）。
- **文件与磁盘侧 82 段**：`mounting` 40、`opening` 21、`extracting` 6、`unpacking` 6、`running` 6、`copying` 3。其中 `extracting` 与 `unpacking` 的 12 段是**同一对句的两半**（`… may allow an attacker to write arbitrary files`）。
- **备份与媒体侧 35 段**：`restoring` 18、`playing` 8、`incoming` 3、`loading` 2、`muting` 2、`enabling` 1、`forwarding` 1。
- **邮件与消息侧 14 段**：`deleting` 6、`replying` 4、`turning` 4。
- **版本控制 2 段**：`cloning`（Git）。

`140 ＋ 82 ＋ 35 ＋ 14 ＋ 2 ＝ 273`。

**"动作"不等于"人做的动作"——这是本篇最大的读法陷阱。** 这 19 个形里有 `incoming`（3 段），它在 `Incoming FaceTime calls can appear or be accepted on a locked macOS device` 里是**定语形容词**，不是动名词；所以"真动名词 18 词 / 270 段"才是本篇可以主张的动作数。剩下 18 词里也仍有归属含糊的：`mounting`、`connecting` 在 iOS 的自动挂载语境下可以由系统发起，但**措辞层不区分**——它只把动词的名词化形式放在主语位。所以本篇的主张严格限定为：**这 273 段把"某个动作"写成了触发条件，且没有把这个动作的施事者写出来。**

# 4 宾语刀：273 段的 17 类动作对象闭合

**取法。** 对 HI 每条首句去掉句首动名词、去掉紧随的一层冠词（`a`／`an`／`the`），取剩下的第一个实词作为"宾语首词"。17 类互斥、闭合到 **273**：

| 读法 | 段 | 说明 |
|---|---:|---|
| `maliciously` | 97 | `Mounting a maliciously crafted disk image…`，副词抢在中心词之前 |
| `to` | 58 | `Connecting to a malicious NFS server…`，动名词后先接介词 |
| `malicious` | 57 | `Visiting a malicious website…` |
| `website` | 26 | `Visiting a website may leak sensitive data`（10 段）等 |
| `conversation` | 6 | `Deleting a conversation in Messages…` |
| `disk` | 5 | `Mounting a … disk image`，中心词在 `image` 之前已被计入 |
| `off` | 4 | `Turning off "Load remote content in messages"…` |
| `facetime` | 3 | `Incoming FaceTime calls…` |
| `file` | 3 | `Opening a maliciously crafted file`（部分形） |
| `hdiutil` | 3 | `Running an hdiutil command…` |
| `mount` | 3 | `Running a mount command…` |
| `url` | 3 | `Copying a URL from Web Inspector…` |
| `call`／`email`／`from`／`lockdown`／`microphone` | 各 1 | 合计 5 |

**97 ＋ 58 ＋ 57 ＋ 26 ＋ 6 ＋ 5 ＋ 4 ＋ 3×5 ＋ 1×5 = 273。**

**为什么前四类（238 段 = 87.2%）不是"交互对象"。** `maliciously`、`to`、`malicious` 是**修饰语或介词**，只有 `website` 一类是真正的对象。所以宾语刀切出来的不是"用户点了什么"，而是"**句子里紧跟动作的那个词是什么词性**"：273 段里只有 **26 段**在动作之后直接给出一个可辨认的对象名（`website`），其余靠修饰语占位。这条决定了后续所有"特定交互"的可读性——**措辞层不提供对象身份**。

# 5 一次量化刀：one／once／each／every／first／twice 全零

**HI 内部单独复测（第 20 章可复算）**：

| 词 | HI 内段 | 占 HI |
|---|---:|---:|
| `one` | 0 | 0.0% |
| `each` | 0 | 0.0% |
| `every` | 0 | 0.0% |
| `twice` | 0 | 0.0% |
| `any` | 0 | 0.0% |
| `first` | 0 | 0.0% |
| `subsequent` | 0 | 0.0% |
| `a` | 261 | 95.6% |
| `an` | 34 | 12.5% |
| `the` | 4 | 1.5% |
| **宾语为复数（`-s`）** | **57** | **20.9%** |

**唯一可用的量化手段是不定冠词。** `a`／`an` 在英文里同时承担"一个"与"任一"两种读法，所以 261／273 段这个数**不能**被读成"261 段要求做一次"。反过来，**57 段（20.9%）的宾语是复数**，字面上直接排除"一个对象"，更谈不上"一次"。

**全集层面的交叉。** 附表 A 显示 `one` 词界在 Impact 有 **9 段**（页 9、名 4），但与 HI 只交 **1 段**、与 R 交 1 段；`a single`、`single`、`once`、`first`、`initial`、`twice`、`each`、`every` 在 Impact 侧**全部 0 段**；`manual`／`manually`／`unattended`／`knowingly`／`unknowingly`／`deliberately`／`intentionally`／`voluntarily` **也全部 0 段**。

**所以"一次特定交互"这个短语的两个限定词——"一次"与"特定"——在措辞层都是空的。** 能说出来的只有：HI 的 273 段里，动作**被写成不定指的单数或复数事件**，没有次数、没有序号、没有"首次／再次"的区分。

# 6 从句刀：if／when／before／upon 在 HI 内全零

**HI 内 11 个从句／时间词复测**：`after` **1 段**、`while` **2 段**、`during` **1 段**、`before` **0**、`when` **0**、`if` **0**、`upon` **0**、`then` **0**、`because`／`so that` **0**、`which`／`who` 关系从句 **0**、`that` 从句 **10 段**（全部是 `that frames malicious content` 这类限定，不是条件）。

**附表 A 的"条件从句连词"组 14 项里 13 项为零。** 唯一非零是 `if the user`：**Impact 6 段 / 6 页 / 1 名**，distinct 全文 1 条：`Websites may know if the user has visited a given link`。而这 6 段与 `user has visited` 的 6 段是**同一批段**（第 12 章），与 HI **零交**、与 WO **零交**。其余 13 项：`if a user`、`when the user`、`once the user`、`before the user`、`after the user`、`unless the user`、`should the user`、`where the user`、`the user must`、`the user needs to`、`requires the user to`、`for the user to`、`the user to` **Impact／Description／致谢三侧全 0**。

**结论：措辞层表达触发条件的句法手段只有一种——把动作名词化后放到主语位。** 不用条件从句、不用时间从句、不用目的从句。这与第 5 章合起来构成一句可以引用的话：**"先做一次交互"在英文通告里没有任何从句或量化词可以承载，它只由一个 `-ing` 承担。**

# 7 情态与后果动词刀：265 ＋ 5 ＋ 3 ＝ 273，`may be able` 为 0

**情态闭合分区**（三段互斥、相加即全集）：

| 情态 | HI 内段 | 占 HI | 说明 |
|---|---:|---:|---|
| `may` | 265 | 97.1% | 唯一可能形 |
| `could` | 5 | 1.8% | `Replying to an email could display remote images in Mail in Lockdown Mode`（4）＋ `Forwarding an email could…`（1） |
| `can` | 3 | 1.1% | `Incoming FaceTime calls can appear or be accepted on a locked macOS device…`（3） |
| `will` | **0** | 0.0% | — |
| `must` | **0** | 0.0% | — |
| `may be able` | **0** | 0.0% | 与 WO 集形成最强对照 |

**265 ＋ 5 ＋ 3 = 273**，且不含 `may` 的 **8 段**恰好是上面 `could` 的 5 段与 `can` 的 3 段，两两不重。

**后果动词（HI 内，段级）**：`lead to` **153（56.0%）**、`cause` **39（14.3%）**、`allow` **12（4.4%）**、`disclose` **10（3.7%）**、`expose` **6（2.2%）**、`result in` **7（2.6%）**、`remain` **0**。`lead to`／`cause`／`result in` 的段**全部与 `may` 同段**（153∕153、39∕39、7∕7）。

**`allow` 的 12 段与 `attacker` 的 12 段完全同段**（allow ⊂ attacker，且两侧都是 12）——全文只有两句：`Unpacking a maliciously crafted archive may allow an attacker to write arbitrary files`（6）、`Extracting a maliciously crafted archive may allow an attacker to write arbitrary files`（6）。这是 HI 里**唯一**把"人"写进句子的位置，而这个人是 **attacker**，不是目标用户。

**形容词层**：`malicious` **223 段（81.7%）**、`crafted` **102 段（37.4%）**、`attacker` **12 段（4.4%）**。

**与 #119 的情态对照（本篇最有价值的一组数）**：

| | HI（273 段） | WO（178 段） |
|---|---:|---:|
| `may be able to` | **0** | **123（69.1%）** |
| 裸 `may` | 265（97.1%） | 48（27.0%） |
| `could` | 5 | 7 |
| `will` | 0 | 0 |
| `may be` ＋ 过去分词（被动） | **0** | **34（19.1%）** |

同一批语料、同一个字段，**写"能力"的时候用人或程序作主语并配 `may be able to`，写"后果"的时候用动作作主语并配 `may lead to`**。这两套结构在段级别互斥（HI ∩ WO = 0），所以任何"这段写了需要交互、也写了无需交互"的读法都不成立。

# 8 需求动词电池：`requir` 13 段全在讲"绕过要求"

**`requir` 词族在 Impact 侧 13 段 / 8 页 / 2 名；∩WO = 0、∩R = 12、∩HI = 1。** 逐段全文只有两种：

- **12 段**（Shortcuts，页 120908–120916）：`A shortcut may be able to bypass Internet permission requirements`
- **1 段**（BiometricKit）：`Restoring from a backup may prevent passcode from being required immediately after Face ID enrollment`

**附表 A 的"需求与前提动词"组 16 项里的逐项计数**：`require` **0**、`requires` **0**、`required` **1**、`requiring` **0**、`requirement(s)` **12**、`requir 词族` **13**（Desc 侧另有 2 段，三字段并 15）、`need／needs／needed` **0**、`necessary` **0**、`must` **0**、`shall` **0**、`should` **2**、`has to／have to` **0**、`prerequisite` **0**、`precondition` **0**、`prior to` **0**、`in order to` **0**。

**三个读法要点。** 其一，**单数 `requirement` 是 0 段、复数 `requirements` 是 12 段**——只用单数词界会得出"需求词族为零"的错误结论（这正是本轮脚本改造的一个坑，第 20 章有断言钉住）。其二，`requir` 的 13 段里 **12 段的主语是 `A shortcut`、谓语是 `may be able to bypass`**：措辞层用"要求"这个词的时候，一定是**要求被绕过**，从来不是**要求你做某事**。其三，第 13 段（BiometricKit）是全语料里最接近"交互前提"的一句话——它说的是"首次录入 Face ID 之后本应立即要求输入密码"这一**预期**没有发生；它落在 `restoring` 这一动名词句里，所以也在 HI 内（HI 的 `restoring` 18 段中有 1 段是它，其余 17 段是 `Restoring a maliciously crafted backup file…`）。

# 9 交互名词与触屏词刀：附表 A 的 34 项里 26 项为零

**"交互名词与触屏词"组 18 项 ＋ "交互动词限定式"组 16 项 = 34 项，Impact 侧为零的 26 项。**

**全零的触屏与交互名词**：`user interaction`、`interact` 词族、`user action(s)`、`user input(s)`、`user gesture(s)`、`swipe` 词族、`press` 词族、`scroll` 词族、`mouse`、`trackpad`、`user approval`、`opt in／opt out`（以上 Impact＝Desc＝Ack 三侧全 0）。

**Impact 为零但别的字段非零的 2 项**：`tap` 词族（致谢 **4**）、`drag` 词族（致谢 **11**）；另有 `click` 词族（Impact **1**、Desc 2、Ack 1，三字段并 **4**——Impact 那 1 段是 `clickjacking` 术语）、`types／typed／typing`（Desc **3**）。这两项说明：**触屏动词只出现在致谢行文里**（研究者描述复现方式），不在后果字段里。

**Impact 非零的零星项**：`keyboard` **10**（∩WO 2、∩R 2）、`touch` 词族 **1**（∩WO 1）、`user confirmation` **5**（∩WO 5）、`dialog` 词族 **3**（∩R 3）、`opens／opened／opening` **23**（∩R 3）、`visits／visited` **88**（∩R **82**）、`enters／entered` **3**（三段全是 `app entering or exiting full screen`，与人无关）、`from the user` **3**、`another user` **9**、`trick a user` **8**。

**授权类名词（本组的实质）**：`consent` 词族 Impact **54**、∩WO **54**（即全部落在 #119 的 WO 内）、∩R 14；`prompt` 词族 Impact **21**、∩WO **19**；`action(s)` **22**、∩WO 19；`permission` 各形 **57**、∩WO 18、∩R 15；`authorization` **11**、∩WO 10；`authentication` **57**∕93 词界 **93**、∩WO 43；`confirmation` **5**、∩WO **5**；`input` 各形 **38**、∩WO 2；`pair` 词族 **22**、∩WO 7、∩R 17。

**这一组的结论是：** 措辞层能写的"交互替代品"全部是**授权名词**（consent／permission／authorization／confirmation／authentication／prompt），而它们**几乎全部与 `without` 同段**——`consent` 的 54 段 100% 在 WO 内、`confirmation` 的 5 段 100% 在 WO 内、`prompt` 的 21 段 19 段在 WO 内。**换句话说：这些词在语料里只以"缺失"形态出现，不以"被要求"形态出现。**（第 15 章的接缝刀正是这一句的量化版本。）

# 10 施事刀：HI 的 273 段里没有一个"人"

**八种人称／施事表达在 HI 内的段数（全部为 0）**：

| 表达 | HI 内段 | 占 HI |
|---|---:|---:|
| `the user` | **0** | 0.0% |
| `a user` | **0** | 0.0% |
| `they`／`them`／`their` | **0** | 0.0% |
| `you`／`your` | **0** | 0.0% |
| `he`／`she`／`him`／`her`／`his` | **0** | 0.0% |
| `victim` | **0** | 0.0% |
| `device`／`iPhone`／`iPad` | 3 | 1.1% |
| `app`／`application` | 17 | 6.2% |

**HI 内 `user` 只有 25 段（9.2%），且两个读法都是定语**：`user interface spoofing` **19**、`user contact information` **6**。把这 25 段按"user 之后是否紧跟情态／完成动词"再测一次：**0 段**。也就是说 HI 里没有任何一句写成 `… user may …` 或 `… user has …`。

**HI ∩ AS = 0**（AS ＝ 第一个 `user` 右邻为 `may`／`has`／`is` 的 158 段，第 11 章）——两个集合的成员级零交，不是比例小。

**唯一的"人"是 attacker，只有 12 段。** `Extracting／Unpacking a maliciously crafted archive may allow an attacker to write arbitrary files` 各 6 段（页 12、名 2），这 12 段既是 HI 内唯一的 `allow`，也是唯一的 `attacker`。措辞层允许出现的角色是**发起者**，不允许出现**执行交互的人**。

**这条为什么重要：** 选题的"目标执行一次交互"需要三个语义成分同时在场——**动作**、**施事（目标）**、**次数**。HI 提供了第一个（273 段），后两个都是空集（**0 段 / 0 段**）。所以**"需要目标执行一次特定交互的攻击方式"不是一个可以从公开措辞统计出来的量**；能统计的只有"有多少句把某个动作写成了触发条件"＝ **273**。

# 11 `user` 的角色分区：1,627 段的右邻词二十五分区（附表 C）

**刀法。** 对 U 集（1,627 段）取**第一个**词界 `user`，跳过其后空白，取紧随的第一个 token（剥掉句末标点），得到 **25 个互斥取值、闭合到 1,627**；再按八类角色归并（表序即优先级）：

| 角色 | 段 | 构成（附表 C 行） | ∩WO | ∩R | ∩G | ∩HI |
|---|---:|---|---:|---:|---:|---:|
| 数据属主 | **1,065** | `data` 880、`information` 170、`contact` 6、`privileges` 4、`account` 2、`enrollment` 2、`contacts` 1 | 9 | 14 | 79 | 6 |
| 施事·情态 | **158** | `may` 122、`has` 34、`is` 2 | **0** | 49 | **0** | 0 |
| 所有格 | **128** | `'s` 128 | 0 | 13 | 0 | 0 |
| 句末落点 | **116** | `(句末)` 116 | 16 | 0 | 0 | 0 |
| 授权名词 | **71** | `consent` 47、`authorization` 10、`permission` 9、`confirmation` 5 | **70** | 14 | 0 | 0 |
| 介词短语 | **48** | `in` 13、`with` 9、`into` 8、`on` 8、`by` 4、`across` 3、`without` 3 | 10 | 21 | 0 | 0 |
| 界面 | **21** | `interface` 21 | 0 | 19 | 21 | **19** |
| 所有格（实体未解码） | **20** | `&rsquo;s` 20 | 0 | 0 | 0 | 0 |
| **合计** | **1,627** | 25 行闭合 | **105** | **130** | **100** | **25** |

**三个要点。**

**一、"施事·情态"这一类是独立的一套句式。** 158 段的 distinct 右上下文只有 **19 条**：`may be able to cause unexpected system termination`（40）、`has installed`（28）、`may be able to elevate privileges`（22）、`may be able to modify restricted network settings`（9）、`may be able to cause a denial-of-service`（8）、`may be able to write arbitrary files`（7）、`has visited a given link`（6）、`may be able to bypass some web content restrictions`（6）、`may be able to read kernel memory`（6）、`may be able to view sensitive user information`（6）…（余 9 条各 1–5 段）。这 158 段 **∩WO = 0、∩G = 0**——它们既不属于"无前提句"，也不属于"动名词触发句"，是措辞层的**第三种骨架**：`A user may be able to [do X]`。页 93、名 34、∩R 49。

**二、"授权名词"这一类几乎完全被 WO 吃掉。** 71 段里 **70 段在 WO 内**（∩WO = 70）——`user consent` 47、`user authorization` 10、`user permission` 9、`user confirmation` 5，只有 1 段例外。这解释了 #119 的第 4 章：`user`＋授权名词这个组合在措辞层**只以"被省略"的形态存在**。

**三、"句末落点"的 116 段只有三种尾部。** `fingerprint the user` **97**、`prompting the user` **16**、`to another user` **3**（97＋16＋3 = 116，页 69、名 19、∩WO 16、∩R 0）。`user` 落在字段末尾本身是切块现象（Impact 侧绝大多数段没有末尾标点），但它恰好使这三种尾部成为可枚举集。

**左邻方向（附表 C 未列，正文口径）**：对同 1,627 段取第一个 `user` 之前的最后一个实词，得 **24 个取值、同样闭合到 1,627**：`sensitive` **844**、`a` **231**、`protected` **145**、`the` **130**、`without` **70**、`of` 43、`remote` 37、`local` 35、`to` 29、`access` 10、`privileged` 9、`unauthenticated` 9，其余 12 项合计 39 段。**没有一个段是 `user` 位于段首**——`user` 永远被一个限定词或介词引导，这与第 10 章"0 段把 user 写成主语"互为佐证。

# 12 正面「用户先做过某事」：九串并集 275 段，与 HI 零交

**九串逐字面计（Impact 侧）**：

| 串 | 段 | 页 | 名 | ∩WO | ∩HI | ∩R | distinct 全文 |
|---|---:|---:|---:|---:|---:|---:|---:|
| `user may` | 122 | 82 | 28 | 0 | 0 | **46** | 17 |
| `fingerprint the user` | 97 | 59 | 17 | 0 | 0 | 0 | 2 |
| `user has installed` | 28 | 25 | 5 | 0 | 0 | 3 | 2 |
| `another user` | 9 | 9 | 3 | 0 | 0 | 0 | 3 |
| `user with X` | 9 | 9 | 4 | 0 | 0 | 0 | 4 |
| `trick a user` | 8 | 8 | 2 | 0 | 0 | 0 | 2 |
| `if the user` | 6 | 6 | 1 | 0 | 0 | 0 | 1 |
| `user has visited` | 6 | 6 | 1 | 0 | 0 | 0 | 1 |
| `user is logged` | 2 | 2 | 2 | 0 | 0 | 0 | 1 |

**并集（去重）= 275 段 / 136 页 / 54 名**；∩WO **0**、∩HI **0**、∩R **49**。直接相加是 287，与并集差 **12**，全部来自两处已验证的重合：`if the user` ∩ `user has visited` = **6**（同一句 `Websites may know if the user has visited a given link`）、`another user` ∩ `user with X` = **6**（同一句 `A user with Voice Control enabled may be able to transcribe another user's activity` 等）。

**四类读法。**

**其一，"用户已装了什么"是唯一被正面写出的既往动作。** `An app may be able to identify what other apps a user has installed`（25 段）＋ `A malicious website may be able to determine what apps a user has installed`（3 段）＝ 28 段。这是全语料里最接近"目标过去的某个决定成为当前后果的前提"的句式——但它是**隐私披露后果**，不是触发条件。

**其二，"用户先访问过某个链接"只有 6 段、1 句。** `Websites may know if the user has visited a given link`——注意它用的是 `if` 从句（第 6 章唯一非零的连词），而不是动名词主语。

**其三，`user with X` 的 9 段是"属性前提"而非"动作前提"。** `A user with Voice Control enabled…`（3）、`A user with screen sharing access…`（3）、`A user with physical access to an iOS device may be able to bypass Activation Lock`（2）、`A user with Live Caller ID app extensions turned off could…`（1）。措辞层把"某种配置状态"写成前提，**不写"某个动作"**。

**其四，`trick a user` 的 8 段是唯一的"诱导交互"表达。** `An app may be able to trick a user into copying sensitive data to the pasteboard`（6）、`An app may be able to trick a user into granting access to photos from the user's photo library`（2）。这两句确实写了"要让用户做一次动作"，但它们**在 HI 之外**（∩HI = 0）、**在 WO 之外**（∩WO = 0），主语是 `An app`。**所以"需要目标交互"在措辞层里唯一的正面承载形式是"欺骗"，而不是任何中性的交互描述。**

# 13 句末刀：116 段以 `user` 收尾，只有 3 种尾部

（本章结论并入第 11 章第三条的表格；此处补三条边界检查。）

**一、116 段的字段状态。** 这 116 段的 `user` 之后**没有任何字符**（含空白与标点）。这与 #119 第 15 章的机制同源：**7,334／7,434 段 Impact 没有末尾标点**，所以"最后一个词"就是"字段最后一个 token"。因此句末落点这一类是**切块与字段口径的产物**，不能直接读成语法上的宾语位置。

**二、三种尾部的归属。** `fingerprint the user` 97 段（页 59、名 17，两条 distinct：`An app may be able to fingerprint the user` 60、`A maliciously crafted webpage may be able to fingerprint the user` 37）；`prompting the user` 16 段（全部在 WO 内，即 `without prompting the user`）；`to another user` 3 段（`… may be accessed by a different user without prompting for the password` 一类）。**∩WO = 16、∩R = 0、∩G = 0。**

**三、`user` 在 HI 内的 25 段里没有一段是句末**（∩HI 列：`interface` 19 ＋ `contact` 6 ＝ 25，全部有右邻词）。**所以"动作触发句"从不以 `user` 收尾——它在 `user` 出现之前就已经把谓语写完了。**

# 14 组件层：35 名 / 472 名，6 名的 Impact 全部落在 HI（附表 D）

**附表 D 逐名给出 HI 段、∩R、∩WO、该组件 Impact 全集、HI 占比、动名词构成。** 合计行：**35 名 / 273 段 / ∩R 108 / ∩WO 0 / 这 35 名的 Impact 全集 2,386 段**。

**六名 100%**（该组件 Impact 段全部是动作触发句）：

| 组件 | HI ∕ 全集 | 动名词 |
|---|---|---|
| zlib | 8 ∕ 8 | `visiting` 8 |
| WebKit History | 7 ∕ 7 | `visiting` 7 |
| exFAT | 7 ∕ 7 | `mounting` 7 |
| file_cmds | 6 ∕ 6 | `extracting` 6 |
| Disk Utility | 3 ∕ 3 | `running` 3 |
| Git | 2 ∕ 2 | `cloning` 2 |

合计 **33 段 = HI 的 12.1%**。**六名占比 ≥ 50% 但不足 100%**：afpfs 8∕12（66.7%）、Compression 6∕9（66.7%）、MobileBackup 9∕14（64.3%）、RealityKit 7∕14（50.0%）、AppleRAID 3∕6（50.0%）、Books 2∕4（50.0%）。

**Top4 集中**：Safari **37**（占该组件 92 段的 40.2%，动名词全部是 `visiting` 37）、WebKit **30**（∕910 段，仅 3.3%）、SMB **27**（∕58，46.6%：`connecting` 18 ＋ `mounting` 9）、Kernel **26**（∕681，3.8%：`connecting` 25 ＋ `mounting` 1）。

**两个反向读数值得单独记。** 其一，**WebKit 有 910 段 Impact，只有 30 段（3.3%）是动作触发句**——同一个组件名的绝大多数句子是 `Processing…`／`Parsing…`，即机器动词；把 WebKit 读成"需要交互的组件"是把附表 D 的两行（WebKit 30、WebKit History 7）合并的结果。其二，**Kernel 的 26 段全部是 `connecting`／`mounting`，且 ∩R = 0**——内核段写"连接到恶意 NFS 服务器"却不声明任何远程通道词，这正是第 16 章 165 段无通道的主要来源。

# 15 与 #119 的接缝：HI ∩ WO ＝ 0，HI ∪ WO ＝ 451 段

**成员级零交。** HI 273 段 ∩ WO 178 段 = **0 段**。并集 **451 段 = 7,434 的 6.1%**。所以"要交互"与"无交互"在措辞层**不是同一维度的两个取值**（否则应出现两词同段的段），而是**两套互不重叠的句式**。

**两套句式的成分对照（全部由本篇与 #119 实测）**：

| 维度 | HI（273 段） | WO（178 段） |
|---|---|---|
| 主语 | 动名词短语（动作） | 程序或数据名词（19 个主语串） |
| 谓语 | `may lead to`／`may cause` | `may be able to`（123）／裸 `may`（48） |
| 被动 | **0** | 34（19.1%） |
| `without` | **0** | 178（每段恰 1 次） |
| `user` | 25 段（全作定语） | 105 段 |
| `attacker` | 12 段 | **0**（`a remote attacker` 194 段与 WO 零交） |
| 句尾 | 不挂前提 | `without` 短语 100% 延伸到末尾 |

**授权名词只出现在 WO 侧。** 第 9 章：`consent` 54 段 100% 在 WO、`confirmation` 5 段 100% 在 WO、`prompt` 21 段的 19 在 WO。而 HI 的 273 段里 `consent`／`prompt`／`confirmation` **0 段**——**措辞层的"交互／授权"词汇全部站在"缺失"那一侧。**

**`without` 在 HI 内为 0 的口径意义。** 这不是"交互句都不省略前提"，而是"交互句这一句式里没有否定副词的位置"：谓语被 `may lead to` 占用，句尾是后果名词。#119 的句尾刀（`without` 短语 178∕0 全在末尾）与本篇第 13 章（`user` 从不作 HI 句尾）合起来说明：**这个字段的空间只有句尾一处，谁占了句尾，谁就独占该句的信息增量。**

# 16 与 #118 的接口：HI ∩ R ＝ 108 ＝ G ∩ R，165 段不声明通道

**恒等式。** `HI ∩ R = 108` 与 `G ∩ R = 108` 且**两集合相等**（已按成员逐一验证）。成立原因：`processing` 1,760 段与 `parsing` 146 段各自 ∩R 都是 **0**，所以从 G 剥掉它们不改变与 R 的交集。**这不是巧合，而是"机器动词句不声明通道"这一事实的另一种写法。**

**108 段在 HI 的 19 个形里的分布**：`visiting` **82**（全部 82 段都命中 R）、`mounting` 12、`replying` 4、`turning` 4、`opening` 3、`cloning` 2、`forwarding` 1 ＝ 108。

**165 段（60.4%）不命中 R。** 其动名词构成：`connecting` **58**、`mounting` **28**、`restoring` **18**、`opening` **18**、`playing` 8、`running` 6、`deleting` 6、`extracting` 6、`unpacking` 6、`incoming` 3、`copying` 3、`loading` 2、`muting` 2、`enabling` 1 = **165**。distinct 48 句里，**含 R 段者 14 句、完全不含 R 段者 34 句**。

**R 的判据漏网（HI 内逐词复测）**：

| 词 | HI 内段 | 已在 R | 新增 |
|---|---:|---:|---:|
| `server` | 51 | **0** | **51** |
| `NFS` | 25 | **0** | **25** |
| `disk image` | 21 | **0** | **21** |
| `SMB` | 27 | 9 | 18 |
| `webarchive` | 4 | 0 | 4 |
| `share` | 18 | 15 | 3 |
| `URL` | 3 | 0 | 3 |
| `iframe` | 2 | 0 | 2 |
| `AFP` | 5 | 3 | 2 |
| `email` | 5 | 5 | 0 |
| `host`／`domain` | 0 | 0 | 0 |

**结论：R 的 11 个通道词族系统性地不收 `server`、`NFS`、`disk image`。** 于是"连接到恶意 NFS 服务器"这一整类句子在 #118 的口径里**不属于远程触达**——它们既没有 `remote`／`network`／`internet`，也没有 `website`。本篇做了一次**修正实验**：把 `server`、`NFS`、`SMB`、`AFP`、`disk image`、`webarchive`、`iframe`、`URL`、`share` 九个词并入词族得 **R2 = 969 段**（比 R 多 **126**），此时 `HI ∩ R2 = 192`——即 HI 内原为 R 外的 165 段里有 **84 段被收进**、**81 段仍未收**。仍未收的 81 段以 `restoring` 18、`opening` 14、`playing` 8、`mounting` 7 为主（备份、文件、媒体三类，本就不声明通道）。**这是可复核的口径建议，不是本篇采用数**：在任何修正版本发布之前，本篇沿用 #118 的原口径 **108**，并用 192 作为该口径的敏感性下界。

# 17 页、版式域与切块自检

**HI 的页分布分箱（102 页）**：**1 段页 48** ∶ **2 段页 27** ∶ **≥3 段页 27**；≥3 段的那 27 页合计承载 **171 段 = HI 的 62.6%**；**Top10 页合计 115 段 = 42.1%**。Top 页：`149035` 26 段、`149042` 22、`149043` 20、`149034` 9、`149041` 8、`128067` 7、`149038` 7、`122373` 6、`122371` 5、`149036` 5。

**读法**：HI 是**页面级聚集**的（半数页只有 1 段，但六成段挤在 27 页里）。这说明"动作触发句"不是全库均匀分布的措辞习惯，而是**特定几份通告（页 14903x 系列＝同一批文件系统／内核条目）成批使用**的模板。

**版式域**：**HI 域 A 273 段、域 B 0 段**。对照：WO 域 A 173／域 B 5（178）；R 域 A 838／域 B 5（843）；U 域 A 1,600／域 B 27（1,627）。**HI 完全不在粗体版式域里**，所以域 B 的 11 页对 HI 的任何比例主张都不构成稀释或虚增。

**切块自检**（与 #111–#119 同一套断言）：全树 **9,136 = 9,045 ＋ 91**、**257 页**、**472 名**、含 Impact **7,434**、含 Description **7,434**、含致谢 **9,132**；排除页 `122085`。块尾用四锚点收块（缺一锚会把页面免责声明卷进字段文本，#111 第 1 章的刀）。

# 18 镜像解码缺陷：`&rsquo;` 24 段、`&rdquo;` 4 段

**实测（Impact 侧，逐实体）**：`&rsquo;` **24 段 / 24 页 / 9 名**（三字段并 32 段）、`&rdquo;` **4 段 / 4 页 / 1 名**；`&ldquo;`、`&amp;`、`&nbsp;`、`&mdash;`、`&gt;`、`&lt;` **各 0**。

**它对本篇计数的三处直接影响**：

**一、附表 C 的所有格被拆成两行。** `'s` **128 段** 与 `&rsquo;s` **20 段** 是同一个语法角色（所有格），因为镜像未解码实体而分成两行。合并读数为 **148 段**。本篇在附表 C 里保留两行并单独标角色，是为了让复算脚本与原词形一一对应。

**二、HI 的 `turning` 4 段全文里有引号实体。** `Turning off "Load remote content in messages&rdquo; may not apply to all recipients` ——右引号是 `&rdquo;` 残留，左引号已被解码。这 4 段是 `&rdquo;` 的全部 4 段。

**三、词组判据必须容忍实体形。** 任何用 `user's` 做的字符串匹配都会漏掉 `user&rsquo;s` 的 20 段；本篇因此把 `user` 一律按**词界**匹配、把所有格当作右邻词单独计数。

**这不是语料现象，是抓取／解码现象。** 报告口径里应把它标注为**镜像缺陷**，并在引用受影响句子时保留原文实体（本篇正文引文一律按镜像原样给出）。

# 19 中文手册层（T1）的对应词（附表 E）

**样本**：《Apple 平台安全保护》zh-CN 转储 179 页，窗口切分后 **178 页可用**；剥 HTML 标签后按词面直接计数（不区分词性）。附表 E 六组 53 项，**全零 10 项、非零 43 项**。

**一、触屏动作组几乎全空。** `轻点` **13 次／10 页（5.6%）**、`点按` **1／1**、`点击` **1／1**；`长按`、`拖动`、`滑动`、`缩放`、`触控板`、`触摸` **各 0 次**。九个词里六个为零——**中文手册也不写触屏动作的细节，只在"轻点"这一 Apple 官方术语上出现 13 次**。

**二、交互名词组比英文丰富。** `交互` **19 次／16 页**、`手动` **27／19**、`手势` **4／3**、`动作` **7／2**、`输入` **106／55**、`选择` **116／70**、`选项` **38／28**，而 `操作` **476 次／178 页（100% 覆盖）**。对照英文侧 `interact`／`user interaction`／`user gesture`／`user action` 全零，可见：**中文手册层写"交互"，英文通告层不写。** 这两者不可互换使用。

**三、确认动作组。** `确认` **250／178（满页）**、`允许` **152／82**、`提示` **45／32**、`批准` **49／25**、`同意` **26／19**、`许可` **14／12**、`回复` **8／5**、`询问` **5／4**；`应答` **0**。英文侧对应的是 `consent`（54 段，全部与 `without` 同段）——中文的"同意"只有 26 次且不满页，**说明手册也不是以"是否取得同意"为组织维度**。

**四、一次量化组（本篇关键）。** `一次` **46 次／24 页**、`单个` **15／14**、`首次` **62／35**、`每个` **106／58**、`顺序` **1／1**、`先后` **0**。`首次` 的 62 次是这一组里最实质的一项（对应英文 `first` 在 Impact 侧 **0 段**）：**中文手册会写"首次设置／首次录入"，英文通告从不写 `first`。** 本篇第 8 章 BiometricKit 那一句（`immediately after Face ID enrollment`）在语义上就是"首次录入之后"，但它用的是 `after`，不是 `first`。

**五、前提否定组。** `自动` **898／178（满页）**、`后台` **234／178（满页）**、`无需` **64／48**、`未经` **21／19**、`不需要` **6／4**、`主动` **3／3**、`静默` **1／1**、`不知情` **1／1**。

**六、动作动词组。** `访问` **1,888／178**、`连接` **735／178**、`恢复` **561／178**、`转发` **204／178**、`打开` **121／53**、`载入` **83／25**、`删除` **50／20**、`播放` **12／7**、`加载` **11／4**、`复制` **2／2**、`粘贴` **2／1**；`挂载` **0**、`静音` **0**。英文 HI 的第三大动作 `mounting`（40 段）在中文手册里**一次都没有对应词**，而 `muting`（2 段）对应词 `静音` 也是 0。

**七、所以中文层不提供"一次交互"的补强证据。** 它提供的是**另一个文本体系**：满页的 `操作`／`访问`／`连接`／`恢复`＋近满页的 `选择`／`允许`／`确认`，加上一批只出现个位数次的触屏动词。**手册写"你可以做什么"，通告写"某动作会导致什么"——本篇的所有英文侧结论都只在通告层成立。**

# 20 复算步骤与断言

**加载**：`require "./_tmp/tl111load.pl"; my %B = load_blocks();` → **9,136 段**。脚本：`_tmp/tl120p1.pl`…`_tmp/tl120p11.pl`（输出 `_tmp/out120p1.txt`…`out120p11.txt`）、`_tmp/tl120tab.pl`（附表生成，自带断言）、`_tmp/tl120splice.pl`、`_tmp/tl120head.pl`。

**必须成立的锚定断言**（每条脚本开头都要 print 并 die 校验）：

- 全树 **9,136 段 / 257 页 / 472 名**；Impact∕Description 各 **7,434**、致谢 **9,132**；域 A **9,045／246／470**、域 B **91／11／26**。
- `U(\%R) == 843`、`U(\%WO) == 178`、`U(\%U) == 1627`、`U(\%G) == 2179`、`U(\%HI) == 273`。**五条同时成立才允许出数。**
- `R` 必须带 `/i`：漏掉会掉到 814。`requirement` 必须容复数（`requirements?`），单数词界是 **0**。

**附表内部闭合断言**（`tl120tab.pl` 自带 die）：附表 A **84 行**；附表 B **21 行、段合计 2,179**；附表 C **25 行、段合计 1,627**；附表 D **35 行、段合计 273**；附表 E **53 行、页数 178**。

**交叉恒等式**：

1. `1,760 ＋ 146 ＝ 1,906`，`1,906 ＋ 273 ＝ 2,179`（G 的剥离完备）。
2. HI 内 `may 265 ＋ could 5 ＋ can 3 ＝ 273`；`不含 may 的段 ＝ 8 ＝ could 段 ＋ can 段`。
3. HI 的 17 类宾语首词合计 **273**；19 个动名词合计 **273**；35 名合计 **273**。
4. `HI ∩ WO ＝ 0`、`HI ∩ AS ＝ 0`、`HI ∩ 九串并集 ＝ 0`、`HI ∪ WO ＝ 451`。
5. `HI ∩ R ＝ G ∩ R ＝ 108`（集合相等，不只同数）。
6. 附表 C 八类角色 `1,065＋158＋128＋116＋71＋48＋21＋20 ＝ 1,627`；左邻 24 词同样闭合 1,627。
7. 句末三类 `97 ＋ 16 ＋ 3 ＝ 116`；`U ∩ HI = 25 = interface 19 ＋ contact 6`。
8. `processing`∩R ＝ `parsing`∩R ＝ 0（恒等式 5 的前提）。
9. 九串直接相加 287 − 并集 275 ＝ 12 ＝ 6（if the user ∩ user has visited）＋ 6（another user ∩ user with X）。
10. 六个 100% 组件 `8＋7＋7＋6＋3＋2 ＝ 33 ＝ 12.1% × 273`。
11. 附表 A 五组 `16＋14＋18＋16＋20 ＝ 84`；并集全零 59 ＋ 非零 25 ＝ 84。
12. 附表 E 六组 `9＋8＋9＋6＋8＋13 ＝ 53`；全零 10 ＋ 非零 43 ＝ 53。
13. 五组语义分类 `140＋82＋35＋14＋2 ＝ 273`；扩展通道口径 `R2 = 843 ＋ 126 = 969`、`HI ∩ R2 = 108 ＋ 84 = 192`、`165 ＝ 84 ＋ 81`。

# 21 缺口清单

**缺口一：HI 的"人的动作"归类是**语义判断**，不是词法事实。** 19 个形里 `visiting`／`connecting`／`mounting`／`opening`／`restoring`／`playing`／`deleting`／`replying`／`turning`／`copying`／`cloning`／`loading`／`muting`／`enabling`／`forwarding`／`extracting`／`unpacking`／`running` 都可能由系统自动发起（自动挂载、后台拉取远端图片）。本篇只用它做"句式存在性"计数，**不主张这 273 段实际都需要人操作**。要判定实际触发条件需要实现层证据，公开措辞不提供。

**缺口二：HI 只覆盖 Impact 首句。** Description 侧的动名词开头必须为 0（这是 G 定义的一部分）；致谢侧完全没查。**如果某条通告把交互要求写在 Description 或致谢里，本篇数为 0 不代表语料里没有。**

**缺口三：`a`／`an` 的 261∕34 段不能读成"一次"。** 不定冠词在英文里同时是不定指。真要主张"次数"，需要 `once`／`a single time`／`each time`，这些在 HI 内**全 0**。

**缺口四：没有"用户界面提示"这一层。** HI 内 `dialog` 0、`prompt` 0、`alert`（未测，附表 A 的可见性组在 #119 已测为 indicator 8 段∩WO 3）。措辞层不写"弹了什么窗"，所以"特定交互"的**界面身份**无从统计。

**缺口五：R 的通道词族有系统性漏词**（第 16 章：`server` 51、`NFS` 25、`disk image` 21 全为新增）。这不影响本篇数，但影响任何把"HI ∩ R"读成"需交互且可远程"的比例的读法。

**缺口六：镜像解码缺陷未清理。** `&rsquo;` 24 段、`&rdquo;` 4 段（第 18 章）。所有格与引号内的短语在跨页比对时可能被判为不同串。

**缺口七：`incoming` 的 3 段写的是 macOS 设备**（`… on a locked macOS device`），出现在 iOS 顾问页镜像内。本篇保留在 HI 内并单独标注，但这 3 段对 iOS 设备不构成"交互触发"证据。**域 A／域 B 的版式差异已排除，跨产品线的正文差异尚未建立排除口径。**

**缺口八：致谢侧含 `tap` 4 段、`drag` 11 段。** 那是研究者的复现描述，不是产品措辞。本篇把它们记为"Impact 0"，**不记为"语料 0"**。

# 22 常见误读纠正

**误读一：把 273 读成"273 种交互"。** 273 是**段数**（条目数），distinct 段全文只有 **48 条**，其中 5 条只出现 1 次、8 条出现 ≥10 次；`-ing` 形 **19 个**、真动名词 **18 个**。正确说法：**18 个动作、48 个句子、273 个条目。**

**误读二：把 `visiting` 的 82 段当成 82 个不同的网站。** 82 段来自 **7 条 distinct 句**（Top：`Visiting a malicious website may lead to user interface spoofing` 19、`… address bar spoofing` 18、`Visiting a maliciously crafted website may leak sensitive data` 15、`Visiting a website may leak sensitive data` 10、`Visiting a website that frames malicious content may lead to UI spoofing` 10）。

**误读三：把 `∩user = 25` 读成"25 段提到用户要做什么"。** 这 25 段的 `user` 全部是 `user interface`（19）与 `user contact information`（6）里的定语，**0 段**写成 `user may`／`user has`。

**误读四：把"HI 内 `requir` 0 段"与"#119 里 `requir` 词族 13 段"当矛盾。** 两者都对：13 段全部**不在 HI 内**（∩HI = 0），其中 12 段主语是 `A shortcut`（属 #119 的 WO∩R 之外的句式），1 段在 HI 内的是 BiometricKit 的 `restoring`——注意它含 `required`，但那是 `passcode from being required`，`requir` 判据按段命中所以该段同时进 `requir` 词族。**段级成员关系而非语义一致性。**

**误读五：把 `HI ∩ R = 108` 与 `G ∩ R = 108` 读成"H ⊂ G 所以交集相同，属显然"。** 它**不显然**：只有当被剥掉的 1,906 段与 R 零交时才成立。同一数值的两个集合（108 与 108）在这里**已按成员验证为同一集合**，但在其他场合（#109 的 29∕29 只重合 22 段）并不成立——**每次都要用成员做交集**。

**误读六：把中文手册的 `操作` 476 次、`访问` 1,888 次当"英文 273 段的中文版证据"。** 两个文本体系：手册写"你可以做什么"（`轻点` 仅 13 次），通告写"某动作会导致什么"。**本篇的比例只在 257 页通告镜像内成立。**

**误读七：把 6.1%（HI∪WO）读成"6.1% 的漏洞需要交互"。** 451 段是**措辞形态**的并集，7,434 是**含 Impact 的段数**，两者都是文本量，不是事件量。`HI ∪ WO` 里没有任何一条被验证为"实际触发路径"。

# 23 结论

**一、措辞层能写"某动作会导致什么"，273 段。** HI ＝ **273 段 / 102 页 / 35 名 / 19 个 `-ing` 形 / 48 条 distinct 句**，占 7,434 段的 **3.67%**；页级高度聚集（27 页承载 62.6%），域 A 全部、域 B 为 0。这是选题"需要一次交互"这一维**唯一可测的正面承载**。

**二、"谁做的"不存在。** `the user`／`a user`／`they`／`you`／`he`／`she`／`victim` 在 HI 内**六式全 0**；含 `user` 的 25 段全部是定语；`HI ∩ AS（施事·情态 158 段）= 0`。唯一被允许出现的人是 **attacker**，只有 **12 段**，且这 12 段与 `allow` 完全同段。

**三、"做几次"不存在。** HI 内 `one`／`each`／`every`／`twice`／`any`／`first`／`subsequent` **七词全 0**；量化只剩不定冠词 `a` 261（95.6%）／`an` 34；**57 段（20.9%）宾语为复数**。全集层面 `a single`／`once`／`single`／`first`／`initial`／`each`／`every` 亦 Impact 全 0。

**四、"要求"只以"被绕过"的形态存在。** `requir` 词族 13 段中 **12 段是 `bypass Internet permission requirements`**；`need`／`must`／`necessary`／`has to`／`prerequisite`／`precondition`／`prior to`／`in order to`／`until`／`once`／`unless`／`provided` **Impact 全 0**。授权名词 71 段里 **70 段在 `without` 句里**。

**五、两套句式互斥。** `HI ∩ WO = 0`、并集 451 段 = 6.1%；`may be able` 在 HI 内 **0**、在 WO 内 **123**；被动在 HI 内 **0**、在 WO 内 **34（19.1%）**；`attacker` 在 HI 内 12、在 WO 内 **0**。**"要交互"与"无交互"不是同一维度，无法用一个比例轴描述。**

**六、通道声明不完整。** `HI ∩ R = 108`（与 `G ∩ R` 集合相等），**165 段（60.4%）不声明任何通道**；R 的词族系统性不收 `server`（51）、`NFS`（25）、`disk image`（21）。

**七、中文层不补强。** `轻点` 13、`点按` 1、`点击` 1，其余触屏动词 0；`一次` 46／24 页、`首次` 62／35、`挂载` 0、`静音` 0、`应答` 0、`先后` 0。**手册写"要满足什么"，不写"要做哪一次"。**

**八、所以本篇的产出是一条可复核的否定结论加三件可核对的事。** **否定结论**：在公开英文后果字段里，**"目标需要执行一次特定交互"这一陈述不可统计**——它有句式（273 段）但无施事（0 段）、无次数（0 段）、无对象身份（只有 26 段给出 `website` 一个对象名）。**可核对的三件事**：**（a）** 动作触发句的覆盖率 **3.67%**、19 个 `-ing` 形、17 类宾语闭合；**（b）** 它与"无前提句"**零交**、与"人作主语句"**零交**；**（c）** 它的谓语结构固定为 `may ＋ lead to／cause／allow／disclose／expose／result in`（153＋39＋12＋10＋6＋7，段级可重叠），**0 段用 `will`、0 段用 `may be able`、0 段用被动**。防御侧可据此核对自家披露文案：如果也要写"需用户交互"，就必须**显式写出施事与次数**，而这正是当前公开措辞没有做的一件事。**本篇不给任何组件"实际需要什么触发条件"的判断，不给任何攻击路径的可行性判断，也不给任何一条"用户这样做就能避免"的操作建议。**

# 附表 A 「需目标做一次交互」正面判据五组电池（84 行）

| 组 | 判据 | Impact 段 | Desc 段 | Ack 段 | 三字段并集段 | 页 | 名 | ∩WO | ∩R |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|
| 需求与前提动词 | `require` | 0 | 2 | 0 | 2 | 2 | 1 | 0 | 0 |
| 需求与前提动词 | `requires` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 需求与前提动词 | `required` | 1 | 0 | 0 | 1 | 1 | 1 | 0 | 0 |
| 需求与前提动词 | `requiring` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 需求与前提动词 | `requirement(s)` | 12 | 0 | 0 | 12 | 7 | 1 | 0 | 12 |
| 需求与前提动词 | `requir 词族` | 13 | 2 | 0 | 15 | 10 | 3 | 0 | 12 |
| 需求与前提动词 | `need／needs／needed` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 需求与前提动词 | `necessary` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 需求与前提动词 | `must` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 需求与前提动词 | `shall` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 需求与前提动词 | `should` | 2 | 0 | 0 | 2 | 2 | 1 | 0 | 0 |
| 需求与前提动词 | `has to／have to` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 需求与前提动词 | `prerequisite` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 需求与前提动词 | `precondition` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 需求与前提动词 | `prior to` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 需求与前提动词 | `in order to` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 条件从句连词 | `if the user` | 6 | 0 | 0 | 6 | 6 | 1 | 0 | 0 |
| 条件从句连词 | `if a user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 条件从句连词 | `when the user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 条件从句连词 | `once the user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 条件从句连词 | `before the user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 条件从句连词 | `after the user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 条件从句连词 | `unless the user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 条件从句连词 | `should the user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 条件从句连词 | `where the user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 条件从句连词 | `the user must` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 条件从句连词 | `the user needs to` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 条件从句连词 | `requires the user to` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 条件从句连词 | `for the user to` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 条件从句连词 | `the user to` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互名词与触屏词 | `user interaction` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互名词与触屏词 | `interact 词族` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互名词与触屏词 | `user action(s)` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互名词与触屏词 | `user input(s)` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互名词与触屏词 | `user gesture(s)` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互名词与触屏词 | `click 词族` | 1 | 2 | 1 | 4 | 4 | 3 | 0 | 0 |
| 交互名词与触屏词 | `tap 词族` | 0 | 0 | 4 | 4 | 4 | 2 | 0 | 0 |
| 交互名词与触屏词 | `swipe 词族` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互名词与触屏词 | `press 词族` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互名词与触屏词 | `drag 词族` | 0 | 0 | 11 | 11 | 11 | 1 | 0 | 0 |
| 交互名词与触屏词 | `scroll 词族` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互名词与触屏词 | `keyboard` | 10 | 1 | 1 | 11 | 11 | 6 | 2 | 2 |
| 交互名词与触屏词 | `mouse` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互名词与触屏词 | `trackpad` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互名词与触屏词 | `touch 词族` | 1 | 0 | 0 | 1 | 1 | 1 | 1 | 0 |
| 交互名词与触屏词 | `user approval` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互名词与触屏词 | `user confirmation` | 5 | 0 | 0 | 5 | 5 | 1 | 5 | 0 |
| 交互名词与触屏词 | `dialog 词族` | 3 | 0 | 0 | 3 | 3 | 1 | 0 | 3 |
| 交互动词限定式 | `clicks／clicked／clicking` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动词限定式 | `taps／tapped／tapping` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动词限定式 | `select／selects／selected` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动词限定式 | `choose／chose／chosen` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动词限定式 | `types／typed／typing` | 0 | 3 | 0 | 3 | 3 | 1 | 0 | 0 |
| 交互动词限定式 | `opens／opened／opening` | 23 | 0 | 0 | 23 | 10 | 5 | 0 | 3 |
| 交互动词限定式 | `visits／visited` | 88 | 0 | 0 | 88 | 57 | 6 | 0 | 82 |
| 交互动词限定式 | `approves／approved` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动词限定式 | `confirms／confirmed` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动词限定式 | `enters／entered` | 3 | 0 | 0 | 3 | 3 | 1 | 0 | 0 |
| 交互动词限定式 | `opt in／opt out` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动词限定式 | `by the user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动词限定式 | `from the user` | 3 | 0 | 0 | 3 | 2 | 2 | 0 | 0 |
| 交互动词限定式 | `with the user` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互动词限定式 | `another user` | 9 | 0 | 0 | 9 | 9 | 3 | 0 | 0 |
| 交互动词限定式 | `trick a user` | 8 | 0 | 0 | 8 | 8 | 2 | 0 | 0 |
| 一次量化与副词 | `one` | 9 | 0 | 0 | 9 | 9 | 4 | 0 | 1 |
| 一次量化与副词 | `a single` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 一次量化与副词 | `single` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 一次量化与副词 | `once` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 一次量化与副词 | `first` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 一次量化与副词 | `initial` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 一次量化与副词 | `twice` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 一次量化与副词 | `each` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 一次量化与副词 | `every` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 一次量化与副词 | `any` | 1 | 0 | 0 | 1 | 1 | 1 | 0 | 0 |
| 一次量化与副词 | `manual／manually` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 一次量化与副词 | `unattended` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 一次量化与副词 | `knowingly` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 一次量化与副词 | `unknowingly` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 一次量化与副词 | `deliberately` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 一次量化与副词 | `intentionally` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 一次量化与副词 | `voluntarily` | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 一次量化与副词 | `actively` | 19 | 0 | 1 | 20 | 15 | 4 | 0 | 1 |
| 一次量化与副词 | `consent 词族` | 54 | 25 | 0 | 76 | 58 | 18 | 54 | 14 |
| 一次量化与副词 | `prompt 词族` | 21 | 25 | 0 | 46 | 34 | 7 | 19 | 0 |

# 附表 B 对照集 G 的二十一个句首动名词与 ∩WO、∩R、∩user（22 行）

| 句首动名词 | 归类 | 段 | 页 | 名 | ∩WO | ∩R | ∩user | distinct 段全文 | 宾语首词 Top3 |
|---|---|---:|---:|---:|---:|---:|---:|---:|---|
| `processing` | 机器动作 | 1760 | 219 | 67 | 0 | 0 | 40 | 86 | maliciously(1502)、web(73)、image(69)… |
| `parsing` | 机器动作 | 146 | 65 | 19 | 0 | 0 | 35 | 8 | file(91)、maliciously(47)、image(8) |
| `visiting` | 人的动作 | 82 | 57 | 6 | 0 | 82 | 19 | 7 | malicious(37)、website(26)、maliciously(19) |
| `connecting` | 人的动作 | 58 | 16 | 5 | 0 | 0 | 0 | 12 | to(54)、malicious(4) |
| `mounting` | 人的动作 | 40 | 17 | 8 | 0 | 12 | 0 | 7 | maliciously(29)、malicious(6)、disk(5) |
| `opening` | 人的动作 | 21 | 8 | 4 | 0 | 3 | 0 | 4 | maliciously(18)、file(3) |
| `restoring` | 人的动作 | 18 | 13 | 4 | 0 | 0 | 0 | 2 | maliciously(17)、from(1) |
| `playing` | 人的动作 | 8 | 8 | 1 | 0 | 0 | 0 | 1 | malicious(8) |
| `deleting` | 人的动作 | 6 | 6 | 1 | 0 | 0 | 6 | 1 | conversation(6) |
| `extracting` | 人的动作 | 6 | 6 | 1 | 0 | 0 | 0 | 1 | maliciously(6) |
| `running` | 人的动作 | 6 | 6 | 2 | 0 | 0 | 0 | 2 | hdiutil(3)、mount(3) |
| `unpacking` | 人的动作 | 6 | 6 | 1 | 0 | 0 | 0 | 1 | maliciously(6) |
| `replying` | 人的动作 | 4 | 4 | 1 | 0 | 4 | 0 | 1 | to(4) |
| `turning` | 人的动作 | 4 | 4 | 1 | 0 | 4 | 0 | 1 | off(4) |
| `copying` | 人的动作 | 3 | 3 | 1 | 0 | 0 | 0 | 1 | url(3) |
| `incoming` | 形容词（非动作） | 3 | 3 | 1 | 0 | 0 | 0 | 1 | facetime(3) |
| `cloning` | 人的动作 | 2 | 2 | 1 | 0 | 2 | 0 | 1 | maliciously(2) |
| `loading` | 人的动作 | 2 | 2 | 1 | 0 | 0 | 0 | 1 | malicious(2) |
| `muting` | 人的动作 | 2 | 2 | 2 | 0 | 0 | 0 | 2 | call(1)、microphone(1) |
| `enabling` | 人的动作 | 1 | 1 | 1 | 0 | 0 | 0 | 1 | lockdown(1) |
| `forwarding` | 人的动作 | 1 | 1 | 1 | 0 | 1 | 0 | 1 | email(1) |
| **合计** | 21 词 | 2179 | 224 | 98 | 0 | 108 | 100 | 136 | — |

# 附表 C 第一个 user 右邻词的二十五分区与角色归类（26 行）

| 第一个 user 的右邻词 | 角色 | 段 | 页 | 名 | ∩WO | ∩R | ∩G | ∩HI |
|---|---|---:|---:|---:|---:|---:|---:|---:|
| `data` | 数据属主 | 880 | 137 | 134 | 9 | 1 | 0 | 0 |
| `information` | 数据属主 | 170 | 97 | 31 | 0 | 13 | 73 | 0 |
| `'s` | 所有格 | 128 | 79 | 36 | 0 | 13 | 0 | 0 |
| `may` | 施事·情态 | 122 | 82 | 28 | 0 | 46 | 0 | 0 |
| `(句末)` | 句末落点 | 116 | 69 | 19 | 16 | 0 | 0 | 0 |
| `consent` | 授权名词 | 47 | 41 | 15 | 47 | 14 | 0 | 0 |
| `has` | 施事·情态 | 34 | 31 | 6 | 0 | 3 | 0 | 0 |
| `interface` | 界面 | 21 | 21 | 2 | 0 | 19 | 21 | 19 |
| `&rsquo;s` | 所有格（实体未解码） | 20 | 20 | 8 | 0 | 0 | 0 | 0 |
| `in` | 介词短语 | 13 | 13 | 4 | 0 | 13 | 0 | 0 |
| `authorization` | 授权名词 | 10 | 10 | 3 | 9 | 0 | 0 | 0 |
| `permission` | 授权名词 | 9 | 9 | 4 | 9 | 0 | 0 | 0 |
| `with` | 介词短语 | 9 | 9 | 4 | 0 | 0 | 0 | 0 |
| `into` | 介词短语 | 8 | 8 | 2 | 0 | 0 | 0 | 0 |
| `on` | 介词短语 | 8 | 8 | 2 | 7 | 8 | 0 | 0 |
| `contact` | 数据属主 | 6 | 6 | 1 | 0 | 0 | 6 | 6 |
| `confirmation` | 授权名词 | 5 | 5 | 1 | 5 | 0 | 0 | 0 |
| `by` | 介词短语 | 4 | 4 | 1 | 0 | 0 | 0 | 0 |
| `privileges` | 数据属主 | 4 | 4 | 1 | 0 | 0 | 0 | 0 |
| `across` | 介词短语 | 3 | 3 | 1 | 0 | 0 | 0 | 0 |
| `without` | 介词短语 | 3 | 3 | 1 | 3 | 0 | 0 | 0 |
| `account` | 数据属主 | 2 | 2 | 1 | 0 | 0 | 0 | 0 |
| `enrollment` | 数据属主 | 2 | 2 | 1 | 0 | 0 | 0 | 0 |
| `is` | 施事·情态 | 2 | 2 | 2 | 0 | 0 | 0 | 0 |
| `contacts` | 数据属主 | 1 | 1 | 1 | 0 | 0 | 0 | 0 |
| **合计（25 项闭合）** | — | 1627 | 175 | 200 | 105 | 130 | 100 | 25 |

# 附表 D 组件名 × HI 段数与 HI 占比（36 行）

| 组件名 | HI 段 | ∩R | ∩WO | 该组件 Impact 全集 | HI 占比 | 动名词构成 |
|---|---:|---:|---:|---:|---:|---|
| Safari | 37 | 37 | 0 | 92 | 40.2% | visiting(37) |
| WebKit | 30 | 24 | 0 | 910 | 3.3% | visiting(24)、opening(4)、loading(2) |
| SMB | 27 | 9 | 0 | 58 | 46.6% | connecting(18)、mounting(9) |
| Kernel | 26 | 0 | 0 | 681 | 3.8% | connecting(25)、mounting(1) |
| HFS | 9 | 0 | 0 | 32 | 28.1% | mounting(9) |
| MobileBackup | 9 | 0 | 0 | 14 | 64.3% | restoring(9) |
| CoreAudio | 8 | 0 | 0 | 79 | 10.1% | playing(8) |
| afpfs | 8 | 3 | 0 | 12 | 66.7% | connecting(5)、mounting(3) |
| zlib | 8 | 8 | 0 | 8 | 100.0% | visiting(8) |
| Model I/O | 7 | 0 | 0 | 88 | 8.0% | opening(7) |
| RealityKit | 7 | 0 | 0 | 14 | 50.0% | opening(7) |
| WebKit History | 7 | 7 | 0 | 7 | 100.0% | visiting(7) |
| exFAT | 7 | 0 | 0 | 7 | 100.0% | mounting(7) |
| Compression | 6 | 0 | 0 | 9 | 66.7% | unpacking(6) |
| Disk Images | 6 | 0 | 0 | 54 | 11.1% | mounting(3)、running(3) |
| Managed Configuration | 6 | 0 | 0 | 17 | 35.3% | restoring(6) |
| Photos Storage | 6 | 0 | 0 | 18 | 33.3% | deleting(6) |
| WebDAV | 6 | 0 | 0 | 20 | 30.0% | connecting(6) |
| file_cmds | 6 | 0 | 0 | 6 | 100.0% | extracting(6) |
| AppleDouble | 5 | 0 | 0 | 9 | 55.6% | mounting(5) |
| Mail Drafts | 5 | 5 | 0 | 11 | 45.5% | replying(4)、forwarding(1) |
| FaceTime | 4 | 0 | 0 | 14 | 28.6% | incoming(3)、muting(1) |
| Mail | 4 | 4 | 0 | 36 | 11.1% | turning(4) |
| MobileAccessoryUpdater | 4 | 0 | 0 | 13 | 30.8% | connecting(4) |
| Web Extensions | 4 | 4 | 0 | 14 | 28.6% | visiting(4) |
| AppleRAID | 3 | 0 | 0 | 6 | 50.0% | mounting(3) |
| DesktopServices | 3 | 3 | 0 | 11 | 27.3% | opening(3) |
| Disk Utility | 3 | 0 | 0 | 3 | 100.0% | running(3) |
| WebKit Web Inspector | 3 | 0 | 0 | 16 | 18.8% | copying(3) |
| Books | 2 | 0 | 0 | 4 | 50.0% | restoring(2) |
| Git | 2 | 2 | 0 | 2 | 100.0% | cloning(2) |
| dyld | 2 | 2 | 0 | 15 | 13.3% | visiting(2) |
| Audio | 1 | 0 | 0 | 95 | 1.1% | muting(1) |
| BiometricKit | 1 | 0 | 0 | 8 | 12.5% | restoring(1) |
| Setup Assistant | 1 | 0 | 0 | 3 | 33.3% | enabling(1) |
| **合计（35 名）** | 273 | 108 | 0 | 2386 | 35∕472＝7.4% | — |

# 附表 E 中文手册层六组对应词（53 行）

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
| 触屏动作 | 触摸 | 0 | 0 | 0.0% |
| 交互名词 | 交互 | 19 | 16 | 9.0% |
| 交互名词 | 操作 | 476 | 178 | 100.0% |
| 交互名词 | 手动 | 27 | 19 | 10.7% |
| 交互名词 | 输入 | 106 | 55 | 30.9% |
| 交互名词 | 动作 | 7 | 2 | 1.1% |
| 交互名词 | 手势 | 4 | 3 | 1.7% |
| 交互名词 | 选择 | 116 | 70 | 39.3% |
| 交互名词 | 选项 | 38 | 28 | 15.7% |
| 确认动作 | 确认 | 250 | 178 | 100.0% |
| 确认动作 | 批准 | 49 | 25 | 14.0% |
| 确认动作 | 允许 | 152 | 82 | 46.1% |
| 确认动作 | 同意 | 26 | 19 | 10.7% |
| 确认动作 | 许可 | 14 | 12 | 6.7% |
| 确认动作 | 询问 | 5 | 4 | 2.2% |
| 确认动作 | 提示 | 45 | 32 | 18.0% |
| 确认动作 | 应答 | 0 | 0 | 0.0% |
| 确认动作 | 回复 | 8 | 5 | 2.8% |
| 一次量化 | 一次 | 46 | 24 | 13.5% |
| 一次量化 | 单个 | 15 | 14 | 7.9% |
| 一次量化 | 首次 | 62 | 35 | 19.7% |
| 一次量化 | 每个 | 106 | 58 | 32.6% |
| 一次量化 | 先后 | 0 | 0 | 0.0% |
| 一次量化 | 顺序 | 1 | 1 | 0.6% |
| 前提否定 | 无需 | 64 | 48 | 27.0% |
| 前提否定 | 不需要 | 6 | 4 | 2.2% |
| 前提否定 | 未经 | 21 | 19 | 10.7% |
| 前提否定 | 自动 | 898 | 178 | 100.0% |
| 前提否定 | 静默 | 1 | 1 | 0.6% |
| 前提否定 | 后台 | 234 | 178 | 100.0% |
| 前提否定 | 主动 | 3 | 3 | 1.7% |
| 前提否定 | 不知情 | 1 | 1 | 0.6% |
| 动作动词 | 打开 | 121 | 53 | 29.8% |
| 动作动词 | 访问 | 1888 | 178 | 100.0% |
| 动作动词 | 连接 | 735 | 178 | 100.0% |
| 动作动词 | 载入 | 83 | 25 | 14.0% |
| 动作动词 | 加载 | 11 | 4 | 2.2% |
| 动作动词 | 挂载 | 0 | 0 | 0.0% |
| 动作动词 | 恢复 | 561 | 178 | 100.0% |
| 动作动词 | 播放 | 12 | 7 | 3.9% |
| 动作动词 | 删除 | 50 | 20 | 11.2% |
| 动作动词 | 复制 | 2 | 2 | 1.1% |
| 动作动词 | 粘贴 | 2 | 1 | 0.6% |
| 动作动词 | 转发 | 204 | 178 | 100.0% |
| 动作动词 | 静音 | 0 | 0 | 0.0% |

---

**边界声明：** 本篇全部材料来自公开可得的安全响应通告与公开安全文档的字面统计，仅用于防御性分析与文本计量研究。文中不含有任何绕过方法、利用代码、恶意样本、利用链构造方式，亦不含针对他人设备的具体操作步骤与时序；所有数字均为公开英文措辞在指定字段口径下的出现频次，只反映**文本如何把某个动作写成触发条件**，不反映该动作在真实产品中是否真的需要人员执行、由谁执行或执行几次，亦不构成任何攻击方式真实存在或需要交互的结论。

Telegram: https://t.me/one00190
