@@TITLE@@iOS 可由网络或远程输入触达的组件与接口
@@SUBTITLE@@选题把**通道（网络/远程）＋动作（触达）＋对象（组件与接口）**三件事捆在一句里。逐词测量之后的结果是：**这三件事在 Apple 安全响应顾问页的英文措辞层里只有第一件存在，而且只以主语形式存在**。含 Impact 的 7,434 段里只有 **843 段（11.3%）**写了任何一个通道词，**6,591 段（88.7%）一个字都不写**；"触达"**没有动词**——`reach` 词族（不含 `reachable`）**0 段**、`trigger-` **0**、`invoke-` **0**、`entry point` **0**、`attack surface` **0**、`attack vector` **0**；"接口"**没有承载词**——`API` 13 段但与通道集合**零交**、`socket` 8 段**零交**、`response` 19 段**零交**、`daemon` 2 段**零交**、`port`／`endpoint`／`framework`／`argument`／`XPC`／`callback` **各 0 段**。所以本篇提出**四把刀**：**主语刀**（含 `may` 的 834 段 R 句中，通道词位于 `may` **之前 834 段、之后 0 段**——通道只当主语，从不写成路径；`over the network` 的 Impact **0 段**、Description **10 段**且 distinct = 1）、**互斥刀**（843 段里恰好 1 个通道族 **800 段**、≥2 族 **43 段**，15 种组合；`remote`∩`website` = **0**、`network`∩`website` = **0**、`USB`∩任何其它通道 = **0**）、**宾语刀**（`remote` 的 257 段宾语闭合为 4 项：attacker 194＋user 37＋content/images 24＋code execution 2 = **257**；`maliciously crafted` 的 1,769 段与 R 只交 **33 段**，其中 `crafted web content` 的 793 段交 **0**）、**接缝刀**（同一批段落改按"三字段拼接"统计，通道集从 **870 掉到 845（丢 25）**，八词去重从 **1,976 掉到 1,566（丢 410）**，`sandbox` 一词就丢 **273**）。
@@INFO@@报告日期|2026 年 10 月 28 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 7,434 段含 Description / 9,132 段含致谢**。远程触达集 R = Impact 侧含 `network ∪ remote ∪ website ∪ Bluetooth ∪ proximity ∪ internet ∪ Wi-Fi/wireless ∪ AirPlay ∪ AirDrop ∪ USB ∪ packets` 任一词形 = **843 段 / 178 页 / 72 名 / 113 条 distinct Impact 句**；全字段并集 **870**、三字段拼接口径 **845**。对照集 A = `attacker` 词族 **890 段**（#113 主语全集）、E = `exploit` 词族 **108 段**（#114 手段全集）、F = `maliciously crafted` **1,769 段**（#116 输入全集）、M = 维持集 **57 段**（#117）、D_I = `data` 的 Impact **1,240 段**、C_I = `code` 的 Impact **363 段**。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**公开文本用哪些词形表示"通道"、这些词出现在句子的哪个位置、哪些组件名与哪些通道同段、以及在哪些维度上它根本不写通道**，属于措辞与文本计量分析
@@INFO@@分类|判据**电池 120 项 / 全零 36 项 / 非零 84 项**（附表 A，六组：远程指称 18、通道与协议 38、输入形态 23、触达与处理动词 17、交互与授权前提 12、接口实体名词 12）/ R 的**十一分区互斥闭合 = 843**（附表 B，12 行）/ 组件名 × 分区**矩阵 22 行**（附表 C，20 个具名＋其余 52 名＝72 名）/ `crafted` 之后的**首词闭合 35 项 = 1,769**（附表 D，36 行）/ **四把刀**：主语刀（834∶0）、互斥刀（800∶43）、宾语刀（257 四项闭合、793∩R = 0）、接缝刀（丢 25／丢 410）
@@INFO@@难度|进阶（第 5 章的 800∶43 是把 11 个词族按**成员集合**去重后逐段归档的结果，不能由 843 与 11 两个数推出"一段一族"；第 6 章的 257 闭合要求把 `remote` 之后两个词取出并按表序去重，`remotely` 的 5 段与 `remote` 的 257 段**零交**，只能用并集 262；第 9 章的接口词 ∩R 必须逐词求交，`API`＝0 与 `socket`＝0 是两个独立的零，不能由"接口词不写通道"这一句话代劳；第 15 章的接缝刀只咬**字段末尾正好把关键词夹在边界上**的段落，`sandbox` 丢 273 而 `network` 只丢 11，这是脚本口径现象而非语料现象；第 12 章 457 与 `denial-of-service` 的 457 数值相同、口径无关，是刻意设置的读法陷阱）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

选题字面是"可由网络或远程输入触达的组件与接口"。这句话里有三个可以分别测量的成分：**通道**（网络、远程）、**动作**（触达）、**对象**（组件、接口）。逐词测量之后的结论是：**只有第一个成分在措辞层有词，而且它只占据一个句法位置**。

**通道有词，但极稀疏。** 含 Impact 的 **7,434** 段里，写了 11 个通道族中任一词形的只有 **843 段 = 11.3%**；**6,591 段 = 88.7% 完全不写通道**。也就是说：绝大多数安全响应条目根本不声明自己是远程的。

**"触达"没有词。** `reach` 词族（把 `reachable` 排除后）**0 段**；`trigger-` **0**、`invoke-` **0**、`entry point` **0**、`attack surface` **0**、`attack vector` **0**、`over the air` **0**、`in the clear` **0**。唯一沾边的是 `reachable` **5 段**，且这 5 段**全部在 Description 侧**、Impact 侧 **0 段**，而且是**同一句话** `A reachable assertion was addressed with improved input validation.`（AirDrop，页 127110/127115/127119/127120/148287）——这是一个**代码断言术语**，不是可达性陈述。（第 14 章）

**"接口"没有承载词。** 附表 A 的"接口实体名词"组 12 项里 **4 项全零**（`endpoint`、`framework`、`argument`、`XPC`），另有 `callback` **0**。非零的 8 项里，与通道集 R **零交**的有 5 项：`API`（13 段）、`socket`（8 段）、`response`（19 段）、`daemon`（2 段）、`header`（7 段）。剩下的交集最大的是 `input` **150**、`kernel` **41**、`sandbox` **40**、`interface` **27**。（第 9 章）

四组反差。

**反差一（通道只当主语，绝不当路径）**：在 R 的 843 段里含 `may` 的有 **834** 段，通道词出现在 `may` **之前 834 段、之后 0 段**。`over the network` 的 Impact **0 段**、Description **10 段**（distinct = 1：`This issue was addressed by using HTTPS when sending information over the network.`）；`by means of`、`attack from`、`network attack`、`an attacker with network access` **各 0 段**；`via` 的 Impact 只有 **23** 段（5 条 distinct 句），其中唯一带通道义的是 `via email` **1 段**。措辞层写的是"**谁**能怎样"，不写"**从哪条路径**到达哪个接口"。（第 10 章）

**反差二（一段基本只写一种通道）**：11 个通道族按成员集合去重后，843 段中恰好命中 **1 族的有 800 段**、命中 **≥2 族的只有 43 段**，组合种类共 **15 种**（9 种单族＋6 种多族）。两个最"混"的组合也只有 8 段：`remote`＋`network` 8、`network`＋`packets` 8、`Wi-Fi/wireless`＋`proximity` 8。**800 ＋ 43 = 843 恰好闭合**。更硬的是**词形两两互斥**：`remote`∩`website` = **0**、`network`∩`website` = **0**、`internet`∩`website` = **0**、`proximity`∩`website` = **0**、`internet`∩`network` = **0**、`USB`∩上述全部 = **0**、`Bluetooth`∩`USB` = **0**、`Bluetooth`∩`remote` = **0**。（第 4、5 章）

**反差三（`attacker` 有一半不写通道）**：`attacker` 词族 **890 段 / 160 页 / 112 名**，其中 **457 段 = 51.3% 不含任何通道词**。六种带位置的限定语合计 **574** 段：`a remote attacker` 194、`an attacker on the local network` 103、`an attacker with physical access` 129、`an attacker in a privileged network position` 80、`a local attacker` 64、`attacker with access to` 4；其余 316 段里只有 52 段含通道词。**574 ＋ 316 = 890 闭合**。（第 7 章）

**反差四（通道与"输入形态"几乎不共段）**：`maliciously crafted` 的 **1,769** 段与 R 只交 **33 段**——`website` 19、`SMB` 9、`AFP` 3、`NTLM` 2（19＋9＋3＋2 = 33 闭合）。而其中最大的一块 `maliciously crafted web content`（**793** 段）与 R 交 **0**；`malicious app`（**337** 段）与 R 只交 **1** 段；`Visiting a malicious website`（37 段）**全部 ∈ R**。措辞层把"远程通道"和"恶意输入"写成**两拨段落**。（第 8 章）

于是本篇的问题依次是：

**问题一（第 3 章）** 通道词到底有几个、各占多少。答案：**R 的 11 个词族展开成非零词形 13 个**：`network` 290、`remote` 257、`website` 222、`Bluetooth` 32、`proximity` 30、`packets` 20、`internet` 14、`Wi-Fi` 10、`wireless` 8、`AirPlay` 7、`USB` 5、`AirDrop` 3、`remotely` 5；`cellular` **0**、`packet` 单数 **0**。附表 A 里另有 `stream` **29**、`relay` **20**、`server` **73**、`IP` **21**、`frame` **10**、`TCP/UDP` **8** 等非零词形，但它们**不属于 R 的判据**（`relay` 的 20 段里有 18 段因同段含其它通道词而进入 R，是巧合不是定义）。

**问题二（第 4、5 章）** 这 843 段怎么分组。答案：**十一分区互斥闭合**（附表 B）＋ **15 种组合**（800 段单族 / 43 段多族）。

**问题三（第 6、7 章）** "远程"在句子里修饰谁。答案：`remote` 只有 **4 种宾语**（257 闭合），`attacker` 有 **6 种位置限定**（574 闭合），但 **457 段不写位置**。

**问题四（第 9 章）** "接口"以什么形式出现。答案：**只有 `sandbox`／`kernel`／`interface`／`connection`／`service`／`input` 六词与通道同段**；`API`／`socket`／`response`／`daemon`／`header`／`port`／`endpoint`／`framework`／`argument`／`XPC`／`callback` 与通道**零交或全零**。

**问题五（第 12 章）** 哪些组件被写成"可远程触达"。答案：**472 个组件名里只有 72 个 = 15.3%** 出现在 R 内；其中 **55 个只出现在 1 种通道组合、17 个出现在 ≥2 种**——一通道一组件，Top 是 WebKit 134、AirPlay 77、Kernel 77、Safari 56、Wi-Fi 41、mDNSResponder 35。

**问题六（第 18 章）** 中文手册层写不写"可达"。答案：**不写**——`可达` **0**、`触达` **0**、`近场` **0**、`有线` **0**、`套接字` **0**、`万维网` **0**、`网络流量` **0**；但 `访问` **1,888 次 / 178 页**、`无线` **1,245 / 178**、`网络` **1,108 / 178**、`远程` **243 / 178**（其中 `远程擦除` 203、`远程锁定` 4、`远程登录` 3、`远程管理` 2）。

# 1 操作性定义与分母

**T2（措辞层）**：`_tmp/adv2/<id>.html` 共 258 份，排除页面 `122085` 得 **257 页**。按 #111 第 1 章的两版式域切块，得 **9,136 段**（域 A **9,045** ＋ 域 B **91**）、**257 页**、**472 个组件名**；含 Impact **7,434**、含 Description **7,434**、含致谢 **9,132**。

**段（block）**＝一个组件条目，`{imp, des, ack, nm, id}` 五字段；**判据命中**＝该段任一字段出现该词形；**并集**＝Impact∪Description∪致谢 三字段**分别匹配后取并集**（这是本篇唯一允许的口径，理由见第 15 章）。

**词界**用 `(?<![\w-])kw(?![\w-])`；**词族**用前缀 `(?<![\w-])kw`。词组内空格统一用 `$S = [ \x{a0}\x{202f}]`（普通空格、不换行空格、窄不换行空格三形），写作 `foo${S}bar`，`${S}` 前后不得再留字面空格；连字符类写作 `$H = [ \x{a0}\x{202f}-]`，**连字符必须放在字符类末尾**。

**核心集合（本篇定义）**：

- **R**（远程触达集，**Impact 侧口径**）= `network ∪ remote ∪ website/web site ∪ Bluetooth ∪ proximity ∪ internet ∪ (Wi-Fi|wireless) ∪ AirPlay ∪ AirDrop ∪ USB ∪ packets` ＝ **843 段 / 178 页 / 72 名**。这是本篇的主集合，因为"某组件可由远程触达"这一陈述在措辞层只可能写在 Impact 字段。
- **R∪**（R 的全字段并集口径）＝ **870 段**；**R△**（三字段拼接后匹配口径）＝ **845 段**。`870 → 845` 的 **25 段差**就是接缝刀（第 15 章）。
- **A**（主语集）= `attacker` 词族 ＝ **890 段 / 160 页 / 112 名**（#113 全集）。
- **F**（输入集）= `maliciously crafted` ＝ **1,769 段 / 218 页 / 81 名**（#116 全集）。
- **E**（手段集）= `exploit` 词族 ＝ **108 段**（#114 全集）；**M**（维持集）＝ **57 段**（#117 全集）。

**十一分区（附表 B 的归档规则）**：对 R 的每一段，按 Impact 句内**首次命中**的通道判据归档，判据顺序为 C1 `remote attacker` → C2 `website` → C3 `network` → C4 `internet` → C5 `Bluetooth` → C6 `proximity` → C7 `Wi-Fi/wireless` → C8 `AirPlay/AirDrop` → C9 `USB` → C10 `packet/packets` → C11 `remote`（不与 attacker 同段）。顺序是**定义的一部分**，因为 `An attacker on the local network` 同时含 C1 之外的 network 词形；换顺序会改变归档而不改变 843 这个总数。十一分区**互斥且并集恰为 R**。

**十一族（第 5 章的组合分析用）**：与分区不同，组合分析**不去重顺序**，而是把 11 个词族当作**集合**，对每段求"命中了哪几族"，得到 15 种组合。两套口径的段数分别是 843（分区）与 800＋43（组合），**必须分别声明**。

**T1（手册层）**：`_tmp/sec*.txt` 179 份，排除 `sec2727afbbc` 得 **178 页可用正文域**；中文词表按字面出现计数，同时报**次数**与**页数**。

# 2 结论速览（每条都可按第 20 章复算）

1. **通道覆盖率**：R = **843 / 7,434 = 11.3%**；无通道标记 **6,591 = 88.7%**；R 覆盖 **178 / 257 页**、**72 / 472 个组件名 = 15.3%**；distinct Impact 句 **113 条**（出现总次数 847）。
2. **通道词形只有 13 个非零**：`network` 290、`remote` 257、`website` 222、`Bluetooth` 32、`proximity` 30、`packets` 20、`internet` 14、`Wi-Fi` 10、`wireless` 8、`AirPlay` 7、`USB` 5、`remotely` 5、`AirDrop` 3；`cellular` **0**、`packet` 单数 **0**。
3. **十一分区闭合**：C1 194 ＋ C2 222 ＋ C3 282 ＋ C4 14 ＋ C5 27 ＋ C6 30 ＋ C7 3 ＋ C8 3 ＋ C9 5 ＋ C10 **0** ＋ C11 63 = **843**。
4. **组合闭合**：恰好 1 族 **800** 段、≥2 族 **43** 段，组合种类 **15**（9 单族＋6 多族）；单族 Top `network` 255、`remote` 249、`website` 222；多族最大 8 段（`remote+network`、`network+packets`、`Wi-Fi/wireless+proximity`）。
5. **词形两两零交**：`remote`∩`website` = 0、`network`∩`website` = 0、`internet`∩`website` = 0、`proximity`∩`website` = 0、`network`∩`proximity` = 0、`internet`∩`network` = 0、`Bluetooth`∩`USB` = 0、`Bluetooth`∩`remote` = 0、`Bluetooth`∩`proximity` = 0、`USB`∩任何族 = 0；非零只有 `remote`∩`network` = **8**、`Bluetooth`∩`network` = **5**。
6. **`remote` 宾语四项闭合**：257 = `remote attacker` 194 ＋ `remote user` 37 ＋ `remote content/images` 24 ＋ `remote code execution` 2 ＋ 其它 **0**；`Remote Desktop`／`remote login`／`remote wipe` **各 0**。
7. **`remotely` 与 `remote` 零交**：`remotely` 5 段全部是同一句 `Password fields may be unintentionally revealed when remotely controlling a device over FaceTime`（FaceTime，页 125884–87、125891）；交 0、并 **262**。
8. **`attacker` 侧**：890 段里 **457 段（51.3%）不含任何通道词**；六种位置限定 574 ＋ 其它 316 = 890；`attackers` **0**、`attack` 90。
9. **通道与输入互斥**：F(1,769) ∩ R = **33**（`website` 19＋`SMB` 9＋`AFP` 3＋`NTLM` 2）；`crafted web content` 793 ∩ R = **0**；`malicious app` 337 ∩ R = **1**；`Visiting a malicious website` 37 **全 ∈ R**。
10. **接口实体层**：`input` ∩R **150**、`kernel` **41**、`sandbox`（词界）**40**、`interface` **27**、`connection` **16**、`service` **11**、`server` **7**、`protocol` **6**、`request` **6**、`extension` **5**、`certificate` **3**、`session` **1**；**与 R 零交 5 项**：`API` 13、`socket` 8、`response` 19、`daemon` 2、`header` 7；**全词零交或全零 6 项**：`port`、`endpoint`、`framework`、`argument`、`XPC`、`callback`。
11. **主语刀**：R 中含 `may` 的 834 段，通道词位于 `may` **之前 834、之后 0**；113 条 distinct 句里主语侧 **109 条**；句首词 distinct **18**（A 418、An 289、Visiting 82、Mounting 12、Remote 8、Network 6…）；通道词出现在句首（无前置名词）**246 段**。
12. **情态闭合分区**：`may be able to` **553** ＋ 裸 `may` **277** ＋ `could` **12** ＋ 其余 **1**（唯一一段是 `might`，页 120907 Bluetooth）= **843**；`can` 的 Impact 16 段 **∩R = 0**；`will`／`must`／`shall`／`would` **各 0**。
13. **组件分布**：72 名中 **55 名只属 1 种通道组合、17 名属 ≥2 种**；Top WebKit 134（C1 12＋C2 122）、AirPlay 77（全 C3）、Kernel 77（C1 37＋C3 22＋C5 7＋C11 11）、Safari 56（全 C2）、Wi-Fi 41、mDNSResponder 35、Security 34、Model I/O 33（全 C1）。
14. **零形电池**：附表 A **120 项 / 全零 36 / 非零 84**；通道与协议组 38 项里 **17 项全零**；`vulnerab` 词族并集 **459 段但 Impact 0 段**——Impact 从不写"漏洞"这个词。
15. **接缝刀**：R 并集 **870 → 拼接 845，丢 25**（逐段归因 `bluetooth|this` 8＋`network|an` 6＋`bluetooth|an` 5＋`network|this` 5＋`internet|a` 1 = 25）；八词去重 **1,976 → 1,566，丢 410**（`sandbox` 464→191 **−273**、`files` 1,170→1,055 −115、`service` 46→8 −38、`connection` 25→9 −16、`Bluetooth` 32→19 −13、`network` 296→285 −11、`notification` 34→30 −4、`extension` 17→13 −4）；四词去重 1,636→1,254 **丢 382**；附表 A 未去重合计 **557**。
16. **零交与前篇接口**：R ∩ E = **4**、R ∩ M = **3**、R ∩ `without prompting` = **0**、R ∩ `without authentication` = **1**；R ∩ A = **433**、R ∩ F = **33**、R ∩ `data`(Impact) = **83**、R ∩ `code`(Impact) = **38**、R ∩ `local` = **112**、R ∩ `physical access` = **4**、R ∩ `Lockdown Mode` = **5**、R ∩ `without` = **27**。
17. **页分布**：178 页，最大页 `149035` = **27 段 = R 的 3.2%**（该页全树共 236 段）；R 侧 Top 149035:27、122373:21、128067:20、149042:20、148287:19、149043:19、128070:18、128071:18。
18. **域 B 自检**：域 B 的 91 段里 R 只有 **5 段**（4 段 WebKit `A malicious website may cause unexpected cross-origin behavior`，页 120304/120309/120311/120339 ＋ 1 段 Bluetooth 页 120303）；域 A **838** 段，838＋5 = 843。
19. **中文层**：`访问` 1,888/178、`无线` 1,245/178、`网络` 1,108/178、`域` 892/178、`服务` 884/178、`无线局域网` 829/178、`配对` 701/178、`信任` 363/178、`蓝牙` 244/178、`远程` 243/178、`服务器` 215/56、`网站` 93/36、`协议` 83/37、`网页` 36/17、`数据包` 18/12；**零**：`可达`、`触达`、`近场`、`有线`、`套接字`、`万维网`、`网络流量`、`越狱`。

# 3 通道词逐词：843 段是怎么来的

R 的 11 个词族按 Impact 侧逐词测量（段／页／名）：

| 词族 | 段 | 页 | 名 | 备注 |
|---|---:|---:|---:|---|
| `network` | 290 | 107 | 35 | 含 `local network` 112、`privileged network position` 93 |
| `remote` | 257 | 101 | 34 | 宾语四项闭合见第 6 章 |
| `website`（含 `web site`） | 222 | 105 | 10 | 致谢侧另有 27 段，不计入 R |
| `Bluetooth` | 32 | 24 | 4 | 与 `remote`/`USB`/`proximity`/`website` 全零交 |
| `proximity` | 30 | 22 | 2 | `physical proximity` 22 ＋ `wireless proximity` 8 = 30 |
| `packets` | 20 | 20 | 3 | 单数 `packet` **0**；三句全是 `privileged network position` |
| `internet` | 14 | 9 | 3 | 12 段是 `bypass Internet permission requirements` |
| `Wi-Fi` | 10 | 10 | 1 | 与 `wireless` 合并为族 C7 |
| `wireless` | 8 | 8 | 1 | 与 `Wi-Fi` 同段的有 8 段属 C6 |
| `AirPlay` | 7 | 7 | 1 | `AirPlay` 词界形；组件侧 AirPlay 名 77 段 |
| `USB` | 5 | 5 | 2 | 5 段全是 `USB Restricted Mode` |
| `AirDrop` | 3 | 3 | 1 | 3 段全是 `quarantine flag` 未施加 |
| `remotely` | 5 | 5 | 1 | 与 `remote` **零交**，同一句 FaceTime |

三个"看起来该有词、实际是零"的词形值得单独点名：`cellular` **0 段**（蜂窝网络在措辞层不存在，中文手册层却有 `蜂窝网络` **203 次 / 178 页**）、`packet` 单数 **0 段**（只有复数 `packets` 20）、`peer` **0 段**（`对等` 中文层 3 次）。

`packets` 的 20 段是一个纯粹的**三类闭合**：8 段 `spoof network packets`（Kernel）、7 段 `crafted Wi-Fi packets`（Wi-Fi）、5 段 `crafted Bluetooth packets`（Bluetooth），合计 20；同段共现 `network` 20（100%）、`Wi-Fi/wireless` 7、`Bluetooth` 5。这三句的句式**完全一致**，都是 `An attacker in a privileged network position may be able to …`——`packets` 从不独立开口，它总是挂在 `privileged network position` 上。（第 11 章）

# 4 十一分区：843 的互斥归档（附表 B）

按第 1 章定义的**首次命中顺序**归档，十一分区互斥、并集恰为 R：

| 类 | 判据 | 段 | 页 | 名 | distinct 句 | Top 组件 | 最高频句 |
|---|---|---:|---:|---:|---:|---|---|
| C1 | `remote attacker` | 194 | 89 | 26 | 16 | Kernel 37、Model I/O 33、Security 23 | 43×`A remote attacker may be able to cause a denial-of-service` |
| C2 | `website` | 222 | 105 | 10 | 26 | WebKit 122、Safari 56 | 38×`A malicious website may exfiltrate data cross-origin` |
| C3 | `network` | 282 | 107 | 35 | 40 | AirPlay 77、mDNSResponder 27、Kernel 22 | 49×`An attacker on the local network may be able to cause a denial-of-service` |
| C4 | `internet` | 14 | 9 | 3 | 3 | Shortcuts 12 | 12×`A shortcut may be able to bypass Internet permission requirements` |
| C5 | `Bluetooth` | 27 | 19 | 4 | 7 | Core Bluetooth 11、Bluetooth 8、Kernel 7 | 13×`An app may gain unauthorized access to Bluetooth` |
| C6 | `proximity` | 30 | 22 | 2 | 4 | Wi-Fi 27、Bluetooth 3 | 9×`An attacker in physical proximity may be able to corrupt process memory` |
| C7 | `Wi-Fi/wireless` | 3 | 3 | 1 | 2 | Wi-Fi 3 | 2×`…may be able to override managed Wi-Fi profiles` |
| C8 | `AirPlay/AirDrop` | 3 | 3 | 1 | 1 | AirDrop 3 | 3×`A file received from AirDrop may not have the quarantine flag applied` |
| C9 | `USB` | 5 | 5 | 2 | 3 | Accessibility 4、Trusted Device 1 | 4×`A physical attack may disable USB Restricted Mode on a locked device.` |
| C10 | `packet/packets` | **0** | 0 | 0 | 0 | — | — |
| C11 | `remote`（不与 attacker 同段） | 63 | 44 | 10 | 11 | Mail 13、Kernel 11、Mail Drafts 11 | 19×`A remote user may be able to cause unexpected system termination or corrupt kernel memory` |
| | **合计** | **843** | **178** | **72** | **113** | | |

三点必须交代：

**其一，C10 = 0 不是遗漏而是顺序的必然。** `packets` 的 20 段全部含 `network`（100%，见第 3 章），而 C3 排在 C10 之前，所以 20 段全被 C3 收走。**" packets 自成一区"这个读法是错的**：附表 B 的 C10 = 0 与第 3 章的 `packets` = 20 同时成立，二者不矛盾，是同一批段落的两种口径。

**其二，页/名两列不能相加。** 十一行的"页"列合计 **406** 远大于 R 的 **178 页**，"名"列合计 **94** 大于 **72 名**——一页、一个组件名可以同时进多个分区。只有"段"列可加且闭合到 843。

**其三，distinct 句列合计恰为 113。** 16＋26＋40＋3＋7＋4＋2＋1＋3＋0＋11 = **113**，与 R 的 distinct Impact 句总数相等——因为分区是按**句内首次命中**判定的，一条 distinct 句只会落进一个区。但 113 条句子的**出现总次数 = 847**，比段落数 843 多 **4**，这 4 段是**一个段落里含两句通道句**。

# 5 互斥刀：一段只写一种通道

附表 B 是按顺序归档的"一区"，本节则把 11 个词族当**集合**，对每段求它命中了哪几族，得到组合分布。15 种组合：

**单族 9 种，合计 800 段**：`network` 255、`remote` 249、`website` 222、`Bluetooth` 27、`proximity` 22、`internet` 14、`USB` 5、`Wi-Fi/wireless` 3、`AirDrop` 3。

**多族 6 种，合计 43 段**：`remote`＋`network` 8、`network`＋`packets` 8、`Wi-Fi/wireless`＋`proximity` 8、`network`＋`AirPlay` 7、`network`＋`Wi-Fi/wireless`＋`packets` 7、`network`＋`Bluetooth`＋`packets` 5。

**800 ＋ 43 = 843 = |R| 闭合**，且"0 族"= 0 段。

注意单族数字（`network` 255）与逐词数字（`network` 290）的差 **35** 恰好是多族中含 network 的段数（8＋8＋7＋5＋7 = 35，其中 `remote+network` 8、`network+packets` 8、`network+AirPlay` 7、`network+Wi-Fi+packets` 7、`network+Bluetooth+packets` 5）。**这不是巧合而是同一集合的两种切法**：290 = 255 ＋ 35。

再看**词形两两零交**（下表为段级交集）：

| | `remote` | `website` | `network` | `internet` | `proximity` | `Bluetooth` | `USB` |
|---|---:|---:|---:|---:|---:|---:|---:|
| `remote` | 257 | **0** | 8 | **0** | **0** | **0** | **0** |
| `website` | **0** | 222 | **0** | **0** | **0** | **0** | **0** |
| `network` | 8 | **0** | 290 | **0** | **0** | 5 | **0** |
| `internet` | **0** | **0** | **0** | 14 | **0** | **0** | **0** |
| `proximity` | **0** | **0** | **0** | **0** | 30 | **0** | **0** |
| `Bluetooth` | **0** | **0** | 5 | **0** | **0** | 32 | **0** |
| `USB` | **0** | **0** | **0** | **0** | **0** | **0** | 5 |

`website` 与其余六族**全部零交**，`USB` 与其余六族**全部零交**，`internet`、`proximity` 也各自与其它族零交（`proximity` 只与 `Wi-Fi/wireless` 有 8 段重合，故在组合表里体现为 `Wi-Fi/wireless+proximity`）。这意味着**"远程/网站/网络"这三组说法在措辞层是三套互不相交的词汇系统**，而不是同一条通道的粗细两种写法。

21 条 distinct 句含 ≥2 族，全部列出可见其结构：`A remote attacker may be able to …`（17 条，remote＋attacker 同段但句中另含 network/sandbox 词）、`An attacker in a privileged network position may be able to spoof network packets`、`…perform denial-of-service attack using crafted Wi-Fi packets`、`…using crafted Bluetooth packets`、`An attacker in wireless proximity may be able to cause unexpected system termination`、`An unauthenticated user on the same network as a signed-in Mac could send it AirPlay commands without pairing`。**最后一条是全树唯一一句同时写"未配对＋同网段＋AirPlay"的段**，用的是 `could`（不是 `may`）——详见第 11 章。

# 6 宾语刀：`remote` 只有四个可修饰的对象

`remote` 的 **257 段**按其后紧跟的两个词归类：

| 宾语型 | 段 | 说明 |
|---|---:|---|
| `remote attacker` | **194** | Impact 句式主语，占 C1 全部 |
| `remote user` | **37** | 全部落在 C11；12 段是 Mail/Mail Drafts |
| `remote content` / `remote images` | **24** | 邮件与消息里的"远程内容" |
| `remote code execution` | **2** | 唯一一条是 `Cloning a maliciously crafted repository may result in remote code execution` |
| 其它 | **0** | 闭合 |

**194 ＋ 37 ＋ 24 ＋ 2 ＋ 0 = 257 恰好闭合。** 也就是说：措辞层的"远程"要么修饰**人**（attacker/user，231 段 = 89.9%），要么修饰**内容**（content/images，24 段），要么修饰**结果**（code execution，2 段）。它**从不修饰接口或组件**——`remote endpoint`、`remote interface`、`remote service`、`remote API` 这些组合在本语料里一段都没有。

三个应当存在却为零的行政语：**`Remote Desktop` 0、`remote login` 0、`remote wipe` 0**（中文手册层 `远程擦除` 却有 **203 次 / 178 页**——见第 19 章的层间反向）。

`remotely` 的 **5 段**与 `remote` 的 **257 段**交集为 **0**，并集 **262**。这 5 段是 FaceTime 组件下同一句话 `Password fields may be unintentionally revealed when remotely controlling a device over FaceTime`（页 125884、125885、125886、125887、125891）。这是全树**唯一**一次用副词形式写"远程"，而且它描述的通道其实是**屏幕共享**，FaceTime 的这 20 段 Impact **全部 ∉ R**（第 8 章）。

# 7 主语集 `attacker`：890 段里一半不写通道

`attacker` 词族 = **890 段 / 160 页 / 112 名**（#113 全集，本篇复用）。`attackers` 复数 **0**、`attack` 词界 **90**。

按 `attacker` 之前的限定语归档：

| 限定语 | 段 | 是否含通道词 |
|---|---:|---|
| `a remote attacker` | **194** | 是 |
| `an attacker with physical access` | **129** | 否（物理） |
| `an attacker on the local network` | **103** | 是 |
| `an attacker in a privileged network position` | **80** | 是 |
| `a local attacker` | **64** | 否 |
| `attacker with access to …` | **4** | 视语境 |
| **六项小计** | **574** | |
| 其它（无限定语或别的形状） | **316** | 其中含通道词 52 |
| **合计** | **890** | |

**核心数字是 457。** 890 段里 **457 段（51.3%）不含 11 个通道族中的任何一个词**。也就是说：**"攻击者"这个词本身在措辞层不携带通道信息**，通道要靠额外的限定语才有；而**恰好一半的段落选择不写**。这 457 段与 R 的交集为 **0**（按定义：R 就是含通道词的段，所以"不含通道词的 attacker 段"必然 ∉ R）——这个 0 是**定义性的**，不能当独立证据引用。

反过来看 R：R ∩ `attacker` = **433**。433 ＋ 457 = 890 是**恒等式**（R 之外的 attacker 段就是"不含通道词的 attacker 段"），它不构成"R 与 attacker 的覆盖关系"这一类新结论。真正有信息量的是 **433 / 843 = 51.4% 的通道段以 `attacker` 为主语**，其余 410 段的主语是 `a malicious website`、`an app`、`a shortcut`、`a file`、`visiting …` 等**非人主语**。

注意：`denial-of-service`（混排连字符与空格的口径）在 Impact 侧是 **457 段 / 155 页**。**这个 457 与上一节的 457 数值相同、口径完全无关**，是本报告刻意留下的一个读法陷阱（第 22 章误读六）。

# 8 通道与输入形态：793 段"恶意网页内容"与 R 零交

F = `maliciously crafted` 的 Impact **1,769 段 / 218 页 / 81 名**。它与 R 的交集只有 **33 段**，且这 33 段的首词闭合：`website` 19 ＋ `SMB` 9 ＋ `AFP` 3 ＋ `NTLM` 2 = **33**。与 `remote` 交 **2**、与 `website` 交 **19**、与 `network` 交 **12**。

附表 D 给出 1,769 段按 `crafted` 之后第一个名词的**完整 35 项闭合**：`web` 793、`file` 290、`image` 167、`media` 102、`video` 74、`font` 54、`webpage` 38、`3d` 35、`website` 19、`texture` 18、`backup` 17、`disk` 17、`message` 16、`audio` 14、`archive` 12、`asset` 12、`zip` 10、`photo` 9、`smb` 9、`contact` 7、`string` 6、`app` 5、`files` 5、`usd` 5、`volume` 5、`email` 4、`keynote` 4、`pages` 4、`webarchive` 4、`afp` 3、`document` 3、`binary` 2、`exfat` 2、`ntlm` 2、`repository` 2，合计 **1,769**。

三个对照数（附表 A 的"输入形态"组）：

- `maliciously crafted web content`：**793 段**，∩R = **0**。
- `malicious app`：**337 段 / 105 页 / 67 名**，∩R = **1**。
- `Visiting a malicious website`：**37 段 / 24 页 / 2 名**，**全部 ∈ R**。
- `malicious website`（不带 Visiting）：**131 段 / 64 页 / 4 名**，**全部 ∈ R**。

这就是本节的论点：**措辞层把"输入"与"通道"分成两拨段落写。** 说"处理恶意构造的网页内容"时，**不写**它从哪来（793 段零交）；说"访问恶意网站"时，**不写**内容形态（37＋131 段全在 R 内）。`web` 一词在两边都出现，但**组合不同**：`crafted web content` 是输入侧、`malicious website` 是通道侧，所以 `web` 词族 908 段、`web content` 895 段与 R 的关系必须按短语判定，**不能按词根判定**。

`vulnerab` 词族在这里提供了一个补充证据：并集 **459 段 / 159 页 / 80 名**，但 **Impact 侧 0 段**。措辞层在陈述"某人可以做某事"时**不使用"漏洞"这个词**，只在 Description/致谢侧（"This issue was addressed…"）使用。因此"某漏洞可被远程触达"这种句式在 Impact 里**不存在**，能写出的只有"某通道上的人/内容可以做某事"。

# 9 接口实体名词层：与通道零交的 5 个词

附表 A 的"接口实体名词"组 12 项 ＋ 输入形态组里的 4 项 ＋ 通道与协议组里的 `socket`，合并测量（并集／Impact／Description／∩R）：

| 词 | 并集 | Impact | Description | ∩R |
|---|---:|---:|---:|---:|
| `input` | 768 | 38 | 730 | **150** |
| `kernel` | 653 | 653 | 0 | **41** |
| `sandbox`（词界） | 412 | 348 | 87 | **40** |
| `sandbox(es\|ed)` 形 | 464 | 407 | — | 28 |
| `sandbox` 前缀 | 475 | 418 | — | — |
| `interface` | 52 | 29 | 29 | **27** |
| `service` | 46 | 39 | 7 | **11** |
| `server` | 73 | 73 | 0 | **7** |
| `extension` | 17 | 17 | 0 | **5** |
| `certificate` | 11 | 8 | 11 | **3** |
| `session` | 4 | 0 | 4 | **1** |
| `protocol` | 12 | 0 | 12 | **6** |
| `request` | 6 | 2 | 4 | **6** |
| `API` | 13 | 7 | 6 | **0** |
| `socket` | 8 | 8 | 0 | **0** |
| `response` | 19 | 19 | 0 | **0** |
| `daemon` | 2 | 2 | 0 | **0** |
| `header` | 7 | 0 | 7 | **0** |
| `port` | **0** | 0 | 0 | 0 |
| `endpoint` | **0** | 0 | 0 | 0 |
| `framework` | **0** | 0 | 0 | 0 |
| `argument` | **0** | 0 | 0 | 0 |
| `XPC` | **0** | 0 | 0 | 0 |
| `callback` | **0** | 0 | 0 | 0 |

四点读法。

**其一：真正与通道同段的接口词只有 6 个**——`input` 150、`kernel` 41、`sandbox` 40、`interface` 27、`connection` 16、`service` 11。其中 `sandbox` 与 `kernel` 都是**隔离体**而非"对外接口"：R ∩ `sandbox` = 40 的句式是 `A malicious website may be able to process restricted web content outside the sandbox`（20 段）与 `A remote attacker may be able to break out of Web Content sandbox`；R ∩ `kernel` = 41 全是 `corrupt kernel memory` 这类**后果**，不是内核接口被远程触达。

**其二：`API` 与 `socket` 的零交是硬结论。** `API` 的 13 段（Impact 7、Description 6、12 页、3 名）里**没有一段**含通道词；`socket` 的 8 段（Impact 8、8 页、1 名）全部来自**同一句** `A maliciously crafted UDP server socket bound to a local interface …` 类描述，且该句不含 R 判据词（`local interface` 不等于 `local network`）。措辞层**不用"API/socket"来陈述远程可达性**。

**其三：`port`／`endpoint`／`framework`／`argument`／`XPC`／`callback` 六个词在 9,136 段里一次都没出现。** 这不是统计口径问题，是原始 HTML 层面的缺失（第 20 章的全文扫描复算里这六项计数为 0）。中文手册层 `端口` 有 **5 次 / 4 页**——层间方向再次相反。

**其四：`sandbox` 三种口径必须分别交代。** 词界 `sandbox` 412（Impact 348）、加 `sandboxes/sandboxed` 464（Impact 407）、前缀口径 475（Impact 418）。∩R 按词界为 **40**、按扩展形为 **28**；同一批段落在句式级口径下是 28。**报一个数而不报口径，就是错的。**

# 10 主语刀：通道词 100% 在情态动词之前

本节是全篇最干净的一刀。R 的 843 段中含 `may` 的有 **834 段**；对这 834 段测量通道词相对于 `may` 的位置：

- 位于 `may` **之前**（主语侧）：**834 段 = 100.0%**
- 位于 `may` **之后**：**0 段**

在 **113** 条 distinct Impact 句里，通道词落在主语侧的有 **109 条**（其余 4 条属不含 `may` 的 `could`/`might` 句式）。句首词分布（R 侧）：

| 句首词 | 段 | 句首词 | 段 |
|---|---:|---|---:|
| `A` | 418 | `Apple` | 4 |
| `An` | 289 | `Replying` | 4 |
| `Visiting` | 82 | `Turning` | 4 |
| `Mounting` | 12 | `On` | 4 |
| `Remote` | 8 | `Hide` | 4 |
| `Network` | 6 | `Opening` | 3 |
| `Block` | 3 | `Cloning` | 2 |
| `USB` | 1 | 其余 3 词 | 各 1 |

**distinct 句首词只有 18 个。** 其中 `Remote` 8 段、`Network` 6 段是通道词直接开句（`Remote attackers …` 类复数写法在本语料不存在，`attackers` = 0，所以这 8 段是 `Remote …` 开头的其它主语），加上 `An attacker …` / `A remote attacker …` 里的通道词在限定语位置——**通道词出现在句首（无前置名词）共 246 段**。

反面对照：**路径式表达在 Impact 侧不存在**。

| 路径式短语 | Impact | Description | distinct |
|---|---:|---:|---:|
| `over the network` | **0** | 10 | 1 |
| `by means of` | **0** | **0** | — |
| `attack from` | **0** | **0** | — |
| `network attack` | **0** | **0** | — |
| `an attacker with network access` | **0** | **0** | — |
| `via` | 23 | — | 5 |

`over the network` 的 10 段 Description 是**同一句** `This issue was addressed by using HTTPS when sending information over the network.`。`via` 的 23 段 Impact（5 条 distinct 句）里唯一带通道义的是 `…deliver malicious content via email` **1 段**；其余 `via` 是"经由某设置/某入口"的工具义（`via the Settings app` 类）。

**结论措辞**：Apple 安全响应文本的 Impact 字段是**"主语＋may＋动词＋后果"**的四段式，通道只能作为主语的定语或主语本身进入句子；一旦要把通道写成**路径**（介词短语、状语），文本就把它挪到 **Description** 字段——那是"已修复说明"的位置，不是"能力声明"的位置。

# 11 情态闭合分区：843 段的四种"能"

R 的 843 段按情态框架归档：

| 框架 | 段 | 典型句式 |
|---|---:|---|
| `may be able to` | **553** | `A remote attacker may be able to cause a denial-of-service` |
| 裸 `may`（非 `able`、非 `allow`） | **277** | `An attacker on the local network may cause an unexpected app termination` |
| `could` | **12** | `An unauthenticated user on the same network as a signed-in Mac could send it AirPlay commands without pairing` |
| 其余 | **1** | `…an attacker in Bluetooth range might be able to spoof the intended source device…`（页 120907，Bluetooth） |
| **合计** | **843** | |

**553 ＋ 277 ＋ 12 ＋ 1 = 843 闭合。**

`can` 的 Impact **16 段**与 R **零交**；`will`、`must`、`shall`、`would` **各 0 段**。也就是说：措辞层**只在"可能"这一档陈述能力**，`can`（现在时能力）从不出现在通道句里——这与 #113 的主语结论一致：文本刻意避免把"攻击者能做某事"写成事实。

那唯一一段 `might` 值得全文抄出，因为它是**全树里唯一一句"距离型通道＋might"的组合**（Bluetooth，页 120907 的致谢段）：`When your headphones are seeking a connection request to one of your previously paired devices, an attacker in Bluetooth range might be able to spoof the intended source device and gain access to your headphones`。同一段落里 `range of` 的出现（2 段，页 120907、127557，均在致谢字段）是全树 `range` 邻近通道的**唯一**写法——`in range` 词组 **0**、`short-range` **0**、`nearby` 仅 **3** 段。

`could` 的 12 段集中在"同一网络上的未认证用户/已签名 Mac"这一组跨设备陈述里，与 #104（连续互通）测得的口径一致。

# 12 组件层：472 个名字里只有 72 个被写成可远程触达

R 覆盖 **72 / 472 个组件名 = 15.3%**。附表 C 给出 Top 20 ＋ 其余 52 名 ＋ 合计的 22 行矩阵，行列双向闭合：

| 组件名 | R 内段 | 分区构成 |
|---|---:|---|
| WebKit | 134 | C1 12 ＋ C2 122 |
| AirPlay | 77 | 全 C3 |
| Kernel | 77 | C1 37 ＋ C3 22 ＋ C5 7 ＋ C11 11 |
| Safari | 56 | 全 C2 |
| Wi-Fi | 41 | C6 27 ＋ C3 11 ＋ C7 3 |
| mDNSResponder | 35 | C3 27 ＋ C1 8 |
| Security | 34 | C1 23 ＋ C11 8 ＋ C3 3 |
| Model I/O | 33 | 全 C1 |
| Bluetooth | 26 | C1 8 ＋ C5 8 ＋ C3 7 ＋ C6 3 |
| Mail | 16 | C11 13 ＋ C3 3 |
| CFNetwork | 15 | C3 8 ＋ C11 7 |
| CoreUtils | 15 | 全 C3 |
| SMB | 14 | C3 12 ＋ C1 2 |
| Shortcuts | 12 | 全 C4 |
| Core Bluetooth | 11 | 全 C5 |
| Mail Drafts | 11 | 全 C11 |
| CUPS | 10 | C3 7 ＋ C11 3 |
| Authentication Services | 9 | 全 C2 |
| 802.1X | 8 | 全 C3 |
| AirDrop | 8 | C3 5 ＋ C8 3 |
| 其余 52 名合计 | 201 | C1 71、C2 35、C3 77、C4 2、C5 1、C9 5、C11 10 |
| **合计 = R** | **843** | 194／222／282／14／27／30／3／3／5／0／63 |

附表 C 的三处闭合由脚本断言：**行合计 20 名 642 ＋ 其余 52 名 201 = 843**；**列合计与附表 B 的十一分区逐列相等**（生成器对每一列做 `die unless` 比较）。

**"一通道一组件"是这一层的结论。** 72 个名字里 **55 个只出现在 1 种通道组合、17 个出现在 ≥2 种**（55 ∶ 17）。也就是说：某个组件在措辞层被写成远程可触达时，**几乎总是固定挂在同一条通道上**——AirPlay 永远在 `local network`（77 段全 C3）、Safari 永远是 `website`（56 段全 C2）、Model I/O 永远是 `remote attacker`（33 段全 C1）、Shortcuts 永远是 `internet`（12 段全 C4）、Core Bluetooth 永远是 `Bluetooth`（11 段全 C5）、Mail Drafts 永远是 `remote user`（11 段全 C11）。跨组合的 17 个名字里最"多面"的是 Kernel（C1/C3/C5/C11 四处）与 WebKit（C1/C2 两处）。

**组件名不等于系统组件。** `mDNSResponder` 35 段里有 27 段属 C3，是全树**唯一以解析器身份进入通道句的名字**；而协议栈层（`Bonjour`、`mDNS` 两个**词形**）**各 0 段**——**名字有、词形没有**。这个不对称是第 14 章零形电池的要点之一：文本用组件名承载协议，不用协议词承载通道。

# 13 网络词的搭配层：`network` 的右邻只有 10 个词

`network` 是 R 里最大的词族（290 段），但它的**右邻词**（`network` 之后第一个实词）distinct 只有 10 个：

| 右邻 | 段 | 右邻 | 段 |
|---|---:|---|---:|
| `traffic` | 33 | `may` | 6 |
| `settings` | 10 | `connection` | 3 |
| `packets` | 8 | `share` | 3 |
| `connections` | 8 | `devices` | 1 |
| `filters` | 8 | | |
| `restrictions` | 7 | | |

这十个右邻里 **6 个是"被管理的东西"**（settings、filters、restrictions、share、connections、devices），**4 个是"流经的东西"**（traffic、packets、connection、may 属句法残留）。**没有一个右邻是"接口"**——`network interface`、`network endpoint`、`network port`、`network stack` **各 0 段**。

配合通道短语层（Impact／页／名／∈R）看，通道词写作**固定短语**而不是自由组合：

| 短语 | Impact | 页 | 名 | ∈R |
|---|---:|---:|---:|---:|
| `malicious website` | 131 | 64 | 4 | 131 |
| `local network` | 112 | 37 | 7 | 112 |
| `attacker on the local network` | 103 | 31 | 4 | 103 |
| `privileged network position` | 93 | 56 | 16 | 93 |
| `cross-origin` | 56 | 37 | 2 | 56 |
| `exfiltrat-` | 51 | 32 | 2 | 51 |
| `spoof` | 72 | 58 | 8 | 60 |
| `maliciously crafted web content` | 793 | 163 | 16 | **0** |
| `malicious app` | 337 | 105 | 67 | **1** |
| `network traffic` | 39 | 34 | 8 | 39 |
| `Visiting a malicious website` | 37 | 24 | 2 | 37 |
| `intercept` | 28 | 25 | 7 | 28 |
| `physical proximity` | 22 | — | — | （C6 的 30 段 = physical 22 ＋ wireless 8） |
| `FaceTime` | 20 | 15 | 4 | **0** |
| `Private Relay` | 20 | 17 | 5 | 18 |
| `Messages` | 19 | 18 | 6 | 4 |
| `same network` | 8 | 8 | 2 | 8 |
| `VPN` | 6 | 6 | 1 | 6 |
| `DNS query` | 7 | 7 | 1 | 7 |
| `Lockdown Mode` | 10 | 10 | 4 | 5 |
| `unauthenticated user` | 9 | 9 | 3 | 8 |
| `pairing` | 9 | 9 | 3 | 9 |
| `signed-in` 词族 | 12 | 12 | 2 | 7 |
| `quarantine` | 4 | 4 | 2 | 4 |
| `nearby` | 3 | 3 | 1 | 3 |
| `Bluetooth range` | 2 | — | — | 属 C5 |
| `adjacent` | **0** | | | |

`Local Network` 的**大小写分列**：大写标签形 **8 段**、小写 **104 段**，合计 112，且 **112 段全部 ∈ R**。大写形是权限名（`An app may gain unauthorized access to Local Network`，页 121250），小写形是通道描述（`An attacker on the local network …`）——**同一个短语的两种大小写承担两种陈述**，做统计时必须分列。

# 14 零形电池：120 项判据里 36 项全零（附表 A）

附表 A 共 **120 项**，分六组：远程指称 18、通道与协议 38、输入形态 23、触达与处理动词 17、交互与授权前提 12、接口实体名词 12。全零 **36 项**、非零 **84 项**。逐组列零项：

**远程指称 3 项**：`attackers`、`online`、`web page`。
**通道与协议 17 项**：`cellular`、`BLE`、`NFC`、`UWB`、`Lightning`、`Thunderbolt`、`Ethernet`、`hotspot`、`proxy`、`Bonjour`、`mDNS`、`port`、`packet`（单数）、`peer`、`advertisement`、`short-range`、`in range`。
**输入形态 5 项**：`SMS`/`MMS`、`iMessage`、`push`、`payload`、`specially crafted`。
**触达与处理动词 3 项**：`reach`（不含 `reachable`）、`trigger-`、`invoke-`。
**交互与授权前提 4 项**：`user interaction`、`without user interaction`、`zero-click`、`approval`。
**接口实体名词 4 项**：`endpoint`、`framework`、`argument`、`XPC`（另 `callback` 在 fin 脚本电池中亦为 0）。

三组读法。

**其一：协议栈整层为零。** `BLE`/`NFC`/`UWB`/`Lightning`/`Thunderbolt`/`Ethernet`/`cellular`/`hotspot`/`proxy`/`Bonjour`/`mDNS`/`port`/`peer` **13 个协议或链路名各 0 段**。但 TCP/IP 层并非全零：`TCP`/`UDP` **8 段**、`IP` 词界 **21 段**、`DNS` **7 段**、`HTTP`/`HTTPS` **16 段**（Impact 6／Description 10）、`socket` **8 段**。所以准确的说法是：**协议词有，但它们不进通道句**——`socket` 的 8 段 ∩R = 0（第 9 章）。

**其二："交互前提"整层为零，这直接影响 #119/#120 两篇的选题。** `user interaction` **0**、`without user interaction` **0**、`zero-click` **0**、`approval` **0**。文本里最接近的是 `without prompting` **19 段**、`without authentication` **43 段**、`consent` **76 段**（Impact 54）、`unauthorized` **37 段**、`unauthenticat-` **9 段**。`click` 仅 **4 段**（Impact 1、Description 2、致谢 1）、`tap` **2 段**且**全在致谢字段**。**"是否需要用户点一下"在措辞层不是被陈述的维度**，而是被 `without prompting`/`without authentication` 这类**授权缺口**替代。

**其三：三个"预期为零却非零"的项必须交代原文，否则读者会当成口径错误。**

- `range of` **2 段**：`…paired with and in Bluetooth range of your iPhone, iPad, or Mac`（页 120907、127557，**致谢字段**）。
- `reach` 词族 **5 段**（即 `reachable`）：`A reachable assertion was addressed with improved input validation.`（AirDrop 页 127110、127115、127119、127120 与 148287，**全在 Description**，Impact 0，∩R 5）。这是**断言（assertion）术语**，与"可达性"无关。
- `hostname` **1 段**：`An app may be able to observe the hostnames of new network connections`（NetworkExtension，页 122716，Impact）。这是全树唯一一次写"主机名"。

**其四：`vulnerab` 词族的 459/0 分层**（并集 459、Impact 0）与 `downgrade` 的 **48/0** 同构——**描述性词汇全部落在 Description/致谢，Impact 只写能力与后果**。这是第 10 章主语刀的词汇层对应物。

# 15 接缝刀：拼接口径丢 25 段与丢 410 段

同一批段落，若把 `imp`、`des`、`ack` 三字段**先拼接成一个字符串再匹配**，会得到系统性的低估：

**通道集 R 本身**：全字段并集 **870** 段 → 拼接 **845** 段，**丢 25**。逐段归因（丢段的接缝正好是"Impact 末词 ｜ Description 首词"）：

| 接缝 | 丢段 |
|---|---:|
| `bluetooth` ｜ `this` | 8 |
| `network` ｜ `an` | 6 |
| `bluetooth` ｜ `an` | 5 |
| `network` ｜ `this` | 5 |
| `internet` ｜ `a` | 1 |
| **合计** | **25** |

**接口与通道八词（去重后）**：并集 **1,976** 段 → 拼接 **1,566** 段，**实际丢 410**：

| 词 | 并集 | 拼接 | 丢 |
|---|---:|---:|---:|
| `sandbox` | 464 | 191 | **273** |
| `files` | 1,170 | 1,055 | 115 |
| `service` | 46 | 8 | 38 |
| `connection` | 25 | 9 | 16 |
| `Bluetooth` | 32 | 19 | 13 |
| `network` | 296 | 285 | 11 |
| `notification` | 34 | 30 | 4 |
| `extension` | 17 | 13 | 4 |

取其中四个词去重后为 **1,636 → 1,254，丢 382**；附表 A 的 120 项判据若不逐组去重、直接按"并集列"相加，得到 **557** 的丢段合计。

**机理**：拼接把 `imp` 的**末尾**与 `des` 的**开头**粘成一个没有标点的接缝。`… Bluetooth` ＋ `This issue was addressed …` 会拼成 `BluetoothThis`，词界 `(?<![\w-])Bluetooth(?![\w-])` 立刻失配。所以接缝刀只咬**字段末尾恰好把关键词夹在边界上**的那批段落——`sandbox` 丢 273 是因为 1,769 段 crafted 句式里 `sandbox` 高频出现在 Impact 句尾（`… outside the sandbox`、`break out of Web Content sandbox`）。

**对照组（丢 0 的词）**：`remote`、`website`、`proximity`、`internet` 在 R 侧不发作，因为它们出现在 Impact **句首/主语位置**（第 10 章主语刀的直接推论）。**接缝刀与主语刀是同一现象的两面**：主语位置的词安全，宾语位置的词有失配风险。

**Impact 末词分布**（distinct **190**）Top：`data` 1,103、`termination` 930、`memory` 812、`crash` 493、`privileges` 398、`denial-of-service` 376、`information` 292、`sandbox` 284、`corruption` 253、`execution` 200、`system` 154、`files` 133、`user` 116、`preferences` 92。**`sandbox` 以 284 段排在末词第 8 位**——这就是它在接缝刀里丢得最多的原因。

# 16 与前几篇的集合接口

全部用**成员集合求交**，并同时报两侧段数：

| 对照集 | |对照集| | ∩ R | 读法 |
|---|---:|---:|---|
| A = `attacker`（#113） | 890 | **433** | 433/843 = 51.4% 的通道段有人作主语；433＋457=890 是恒等式 |
| E = `exploit` 词族（#114） | 108 | **4** | 手段词与通道词几乎不共段 |
| M = 维持集（#117） | 57 | **3** | "持久"与"远程"是两个互不相干的描述维度 |
| F = `maliciously crafted`（#116） | 1,769 | **33** | 见第 8 章的四项闭合 |
| `data` 的 Impact（#116） | 1,240 | **83** | 数据外泄句里 83 段声明了通道 |
| `code` 的 Impact（#116） | 363 | **38** | 代码执行句里 38 段声明了通道 |
| `local` 词界 | — | **112** | 与 `local network` 的 112 段完全同集合（已用成员比对，非数值比较） |
| `physical access` | 142 | **4** | 物理与远程几乎不共段；`physical proximity` 属 C6 不算 |
| `without` 的 Impact | 178 | **27** | 无授权＋通道同段 27 段 |
| `without prompting` | 19 | **0** | 交互缺失与通道**零交**（对 #119/#120 直接相关） |
| `without authentication` | 43 | **1** | 同上，仅 1 段 |
| `Lockdown Mode` | 10 | **5** | 一半的锁定模式句涉及网络 |
| `sandbox`（词界） | 412 | **40** | 句式级为 28 |
| `kernel` | 653 | **41** | 全部是 `corrupt kernel memory` 类后果 |

**六项零交**：`API`、`socket`、`response`、`daemon`、`header`、`can` 与 R 的交集均为 **0**；另有 `without prompting` ∩ R = **0**。

**两条必须写的负结论**：R ∩ `termination` 侧的"意外终止"是**后果**（#117 的后果刀），不是通道；R ∩ `persistent`（#117 的 46 段）= 3 段，说明"跨边界维持"与"远程触达"在措辞层是**两条正交的轴**——一篇讲时间，一篇讲空间，共段只有 3 段。

# 17 页、版式域与切块自检

R 分布在 **178 页**上（全树 257 页）。最大页 **149035** 有 **27 段**，占 R 的 **3.2%**；该页全树共 **236 段**（全树最大页）。R 侧页 Top 20：

`149035:27`、`122373:21`、`128067:20`、`149042:20`、`148287:19`、`149043:19`、`128070:18`、`128071:18`、`122371:17`、`128069:17`、`128072:16`、`122374:15`、`122375:14`、`122378:14`、`122372:12`、`126792:12`、`127115:12`、`128066:12`、`128068:12`、`149034:12`。

全树最大页 Top 5 供对照：`149035:236`、`149043:172`、`149042:169`、`122373:158`、`128067:156`。**R 密度与页面规模并不同步**——128067 有 156 段但只有 20 段进 R，说明这些是**同一份 iOS 更新的页面**（128066–128072 连续 7 页全在 Top 20 内），通道段在页面里的占比约 12–18%，与全库 11.3% 相当。

**版式域自检**：域 B（11 份粗体版式页面 `118479`、`120303–120305`、`120307–120312`、`120339`）共 **91 段**，其中进 R 的只有 **5 段**：4 段 WebKit `A malicious website may cause unexpected cross-origin behavior`（页 120304、120309、120311、120339）＋ 1 段 Bluetooth（页 120303）。域 A 侧 **838** 段，**838 ＋ 5 = 843**。域 B 的 91 段占全树 1.0%，但只贡献 R 的 0.6%，所以本篇的 R 结论**不是粗体版式带来的偏差**。

`until` 一词的致谢侧残留（#117 第 16 章记录：11 段全部落在致谢、且恰好是域 B 的 11 份页面的免责声明）在本篇不构成风险：R 定义在 Impact 侧，致谢字段不参与判定。

# 18 distinct 句：113 条句子撑起 843 段

R 的 843 段只由 **113 条 distinct Impact 句**组成（出现总次数 847）。Top 24：

| 次 | 句 |
|---:|---|
| 49 | `An attacker on the local network may be able to cause a denial-of-service` |
| 43 | `A remote attacker may be able to cause a denial-of-service` |
| 38 | `A malicious website may exfiltrate data cross-origin` |
| 26 | `A remote attacker may be able to cause unexpected application termination or heap corruption` |
| 25 | `A remote attacker may be able to cause unexpected system termination` |
| 23 | `An attacker in a privileged network position may be able to intercept network traffic` |
| 22 | `An attacker on the local network may cause an unexpected app termination` |
| 20 | `A malicious website may be able to process restricted web content outside the sandbox` |
| 19 | `A remote user may be able to cause unexpected system termination or corrupt kernel memory` |
| 19 | `Visiting a malicious website may lead to user interface spoofing` |
| 18 | `Visiting a malicious website may lead to address bar spoofing` |
| 16 | `A remote attacker may cause an unexpected app termination` |
| 15 | `Visiting a maliciously crafted website may leak sensitive data` |
| 14 | `A remote attacker may be able to cause unexpected system termination or corrupt kernel memory` |
| 13 | `An app may gain unauthorized access to Bluetooth` |
| 13 | `An attacker in a privileged network position may be able to cause a denial-of-service` |
| 12 | `A remote attacker may be able to cause unexpected app termination or arbitrary code execution` |
| 12 | `A shortcut may be able to bypass Internet permission requirements` |
| 12 | `An attacker on the local network may be able to corrupt process memory` |
| 11 | `A remote attacker may be able to cause a denial of service`（空格形，与连字符形并存） |
| 10 | `A website may be able to access sensor information without user consent` |
| 10 | `Visiting a website may leak sensitive data` |
| 10 | `Visiting a website that frames malicious content may lead to UI spoofing` |
| 9 | `A non-privileged user may be able to modify restricted network settings` |

**两个必须注意的形态对**：`denial-of-service`（连字符）与 `denial of service`（空格）**并存**且都是高频（混排口径合计 457 段 / 155 页），`app termination` 与 `application termination` 也并存。任何按单字符串统计"同一句话出现几次"的做法都会把一类拆成两类——本篇所有句级数字均按**原句字面**计数，不做规范化合并。

113 条句中，主语含 `attacker` 的、含 `website` 的、含 `app` 的三类覆盖了绝大多数；**没有一条句子的主语是"接口"**：主语是 `API`/`socket`/`endpoint`/`port` 的句子数量为 **0**（第 9 章的直接体现）。

# 19 中文手册层（T1）的对应词

178 页可用正文域的字面计数（次数／页数）：

| 词 | 次 | 页 | 词 | 次 | 页 |
|---|---:|---:|---|---:|---:|
| 访问 | 1,888 | 178 | 服务器 | 215 | 56 |
| 无线 | 1,245 | 178 | 证书 | 150 | 29 |
| 网络 | 1,108 | 178 | 网站 | 93 | 36 |
| 域 | 892 | 178 | 协议 | 83 | 37 |
| 服务 | 884 | 178 | 浏览器 | 50 | 31 |
| 无线局域网 | 829 | 178 | 通知 | 50 | 31 |
| 局域网 | 829 | 178 | 互联网 | 44 | 26 |
| 配对 | 701 | 178 | 邮件 | 62 | 27 |
| 信任 | 363 | 178 | 网页 | 36 | 17 |
| 蓝牙 | 244 | 178 | 数据包 | 18 | 12 |
| 远程 | 243 | 178 | 推送 | 15 | 13 |
| 蜂窝网络 | 203 | 178 | 附件 | 17 | 7 |
| 热点 | 202 | 178 | 降级 | 23 | 10 |
| 消息 | 9 | 4 | 门户 | 7 | 6 |
| 重定向 | 5 | 3 | 端口 | 5 | 4 |
| 伪造 | 4 | 4 | 对等 | 3 | 3 |
| 中间人 | 2 | 1 | 劫持 | 1 | 1 |

**"远程"四词**：`远程擦除` **203 次 / 178 页**、`远程锁定` 4/4、`远程登录` 3/3、`远程管理` 2/2；`远程` 总 243 次里 **203 次是"远程擦除"**（占 83.5%）。

**中文层为零的词**：`可达` **0**、`触达` **0**、`近场` **0**、`有线` **0**、`套接字` **0**、`万维网` **0**、`网络流量` **0**、`越狱` **0**。

**层间反向（本篇最重要的对照结论）**：

1. **英文措辞层写通道、中文手册层写机制**：英文 `network` 290 段里 112 段是 `local network`（通道），中文 `局域网` **829 次 / 178 页**却全部是"无线局域网"这一**功能名**（`无线局域网` 与 `局域网` 计数完全相等 = **829**，说明中文层的"局域网"三字**只在"无线局域网"里出现**，没有任何独立的"局域网"条目）。
2. **中文层的"远程"是管理动作，不是威胁**：`远程擦除` 203、`远程锁定` 4、`远程登录` 3、`远程管理` 2 全是**找回/管控功能**；英文层的 `remote` 257 段里 **231 段（89.9%）修饰人**（attacker/user）。同一个形容词在两层里方向相反。
3. **英文为零、中文非零**：`proxy` **0** ↔ `门户` 7 次；`peer` **0** ↔ `对等` 3 次；`cellular` **0** ↔ `蜂窝网络` 203 次；`hotspot` **0** ↔ `热点` 202 次。
4. **中文为零、英文非零**：`可达` **0** ↔ `reachable` 5 段；`近场` **0** ↔ `proximity` 30 段；`网络流量` **0** ↔ `network traffic` 39 段；`套接字` **0** ↔ `socket` 8 段。
5. **两层的"中间人"都不成词**：英文 `man-in-the-middle` **0**、`MitM` **0**、`in the clear` **0**，只有 `privileged network position` **93 段**承担同一语义；中文 `中间人` **2 次 / 1 页**、`劫持` **1 次 / 1 页**。**两层都拒绝用"中间人"这个术语，都改用位置隐喻。**

# 20 复算步骤与断言

**脚本**：`_tmp/tl118tab.pl`（附表 A/B/C/D 生成器，自断言）→ `_tmp/tl118fin.pl`（正文数字总复算：分母、R、情态、组合、交集、接口、接缝、末词、页分布、distinct 句、T1）→ `_tmp/tl118p9.pl`（三个意外非零项的原文、四/八词接缝去重、`Local Network` 大小写、45 个概念邻近词、中文全表）→ `_tmp/tl118p10.pl`（逐词 Impact、情态闭合分区、每区 Top 组件与代表句）→ `_tmp/tl118p11.pl`（11 族组合、`packets` 三分、`might/can/will` 计数、主语刀、组件↔组合 55∶17）。

**生成器内建断言**（任一不成立即 `die`，附表不会输出半成品）：

- 附表 A：`行=120`、`全零=36`、`非零=84`；`remote` 宾语 194＋37＋24＋2＋0 与 257 相等。
- 附表 B：十一区段数之和 `= |R| = 843`，否则 `附表B 合计 != |R|` 中止。
- 附表 C：`组件名数 = 72`；`Top20 行合计 + 其余52名行合计 = 843`；`其余行分列合计 = 201`；`每一列与附表 B 该区段数逐一相等`（11 次 `die unless`）；`列合计总和 = 843`。
- 附表 D：35 个首词之和 `= 1,769 = |F|`。

**复算口径三条禁令**（本轮 #110–#117 的缺陷全部来自这三处）：

1. **禁止用"三字段拼接"做判据匹配**——它使 `sandbox` 丢 273、`service` 丢 38、`connection` 丢 16、R 整体丢 25（第 15 章）。
2. **禁止用 `my %h = grep {…} keys %B` 建集合**——`grep` 在哈希初始化上下文里会被**静默折半**（实测把域 B 的 91 段读成 46 段、把 R∩域B 的 5 段读成 2 段）。必须写 `map { $_=>1 } grep {…}`。
3. **禁止由"两个集合同数"推"同一段落"**——`denial-of-service` 的 457 与 attacker 无通道的 457 数值相同、成员无关（第 7 章）。凡"A ⊂ B / A 就是 B"的表述，必须给出 |A|、|B|、|A∩B| 三个数。

# 21 缺口清单

1. **"组件—通道"矩阵在公开文本里没有系统表达。** 72 / 472 = 15.3% 的名字被写进通道句，其余 400 个名字**从未声明通道**；没有一份公开表格给出"哪些组件接受远程输入"。想回答选题字面，只能反向从 `无通道 = 88.7%` 这一侧推断。
2. **"可达性"这个概念没有词。** `reach` 0、`trigger-` 0、`invoke-` 0、`entry point` 0、`attack surface` 0、`attack vector` 0；`reachable` 5 段全是断言术语。**CVSS 的 Attack Vector 维度在措辞层没有对应物。**
3. **协议栈层为零（13 词）**，导致"哪一协议可远程触达"无法从文本回答；能回答的只有组件名（`mDNSResponder` 35、`802.1X` 8、`CUPS` 10）。
4. **交互前提维度为零（4 词）**，`without prompting` 19 段是**唯一可用的近似**，但它说的是"未提示"而非"未交互"。
5. **接口词与通道零交（6 项）**：`API`、`socket`、`response`、`daemon`、`header`、`port`。文本不用接口名词承载远程可达性，所以"接口清单"这一层需要外部资料（如系统架构文档）才能补。
6. **路径式表达只在 Description**：`over the network` Impact 0 / Description 10。修复说明里有路径，能力声明里没有路径。
7. **`could`/`might` 共 13 段的语义档未被定义**，与 553＋277 段 `may` 是否同一威胁级别，文本不给依据。
8. **中文层与英文层不可互相翻译**：`局域网` = `无线局域网`（829 = 829）而 `local network` 是通道；`远程` 83.5% 指"远程擦除"而 `remote` 89.9% 修饰攻击者。**任何中英对照都必须逐词重新计数，不能沿译。**

# 22 常见误读纠正

**误读一："843 段就是 843 个可远程触达的漏洞。"** 843 是**段落数**（一个段落＝一个组件条目），覆盖 178 页、72 个组件名、113 条 distinct 句；同一句 Impact 可以重复出现在不同页。而且 R 的口径是**"Impact 里出现了通道词"**，不是"该缺陷确认可被远程触发"。

**误读二：把 11 分区的段数当词族段数。** C3 的 282 ≠ `network` 的 290，C5 的 27 ≠ `Bluetooth` 的 32，C10 的 0 ≠ `packets` 的 20。分区是**按首次命中去重**的结果，词族是**逐词**结果，两者都要报。

**误读三："`remote` 与 `remotely` 合起来 262 段说远程。"** 两者**零交**，`remotely` 的 5 段是 FaceTime 副词用法（同一句），不是新的远程通道；报 262 时必须同时报"交 0"。

**误读四："文本说 API 可被远程调用。"** `API` 13 段 ∩R = **0**；`socket` 8 段 ∩R = **0**；`port`、`endpoint` **各 0 段**。文本从未把接口名词与通道词放在同一段。

**误读五："`network` 290 段说明文本大量描述网络攻击。"** 290 段里 **112 段是 `local network`（同一短语）、93 段是 `privileged network position`（同一短语）**；`network traffic` 只有 39 段，右邻词 distinct 只有 10 个。`network` 不是"网络"这个概念的度量，而是**两个固定攻击者称谓**的组成部分。

**误读六：把两个 457 混为一谈。** `attacker` 无通道段的 **457**（占 890 的 51.3%）与 `denial-of-service` 混排口径的 **457**（155 页）数值相同、口径无关、成员不重叠判定未做。任何一处引用都必须带上口径说明。

**误读七："574 ＋ 316 ＝ 890 证明了限定语分类的正确性。"** 这是**穷举定义**下的必然（每一段要么命中六个限定语之一、要么不命中），不是独立验证。真正需要验证的是**六个限定语之间互斥**（脚本按首次命中顺序归档，并集恰为 890，无重叠段）。

**误读八："433 ＋ 457 ＝ 890 说明一半的通道段有攻击者主语。"** 前半句是恒等式；后半句应当写成 **433 / 843 = 51.4%**（分母是 R 而不是 A）。**同一对数字，换分母就换结论**，这是本篇最容易写错的一处。

**误读九："`sandbox` 40 段说明沙箱可被远程逃逸。"** R ∩ `sandbox` = 40（句式级 28），其中最大一块是 `A malicious website may be able to process restricted web content outside the sandbox`（20 段）——它说的是**已修复的行为**（措辞为"outside the sandbox"的后果陈述）。**同一段落用拼接口径会丢 273 段**，所以这个数字本身对口径极其敏感，必须带口径引用。

# 23 结论

**其一：选题的三个成分里，措辞层只有一个。** 通道有词（13 个非零词形）、触达无词（`reach`/`trigger`/`invoke` 全零）、接口无承载（`API`/`socket`/`port`/`endpoint`/`XPC` 与通道零交或全零）。因此"可由网络或远程输入触达的组件与接口"这句话在 Apple 安全响应文本里的**可读形式只有一种**：`<带通道定语的主语> + may (+ be able to) + <动词> + <后果>`。

**其二：通道是稀疏的。** 843 / 7,434 = **11.3%**；88.7% 的 Impact 段不写通道。组件侧更稀疏：**472 个名字里 72 个 = 15.3%** 被写进通道句，且 **55 ∶ 17** 近乎"一通道一组件"。

**其三：通道是互斥的。** 800 段单族 ∶ 43 段多族（15 种组合）；`remote`、`website`、`internet`、`proximity`、`USB` 五者两两零交，`Bluetooth` 只与 `network` 交 5 段。**"网络/远程/网站"不是同一条通道的三种粗细写法，而是三套互不相交的词汇系统。**

**其四：通道只写在主语位置。** 834 段含 `may` 的 R 句中，通道词在 `may` 之前 **834**、之后 **0**；路径式介词（`over the network`）Impact **0**、Description 10。措辞层声明"谁能做什么"，不声明"从哪里进来"。

**其五：通道与输入分家。** `crafted` 1,769 段只与 R 交 33；`crafted web content` 793 段交 **0**；`malicious app` 337 段交 **1**；而 `Visiting a malicious website` 37 段与 `malicious website` 131 段**全部在 R 内**。文本要么说"通道＋人/站点"，要么说"内容＋处理"，**不两说**。

**其六：交互维度整层缺失。** `user interaction`、`without user interaction`、`zero-click`、`approval` **各 0 段**，`click` 仅 4 段、`tap` 2 段全在致谢。措辞层用 `without prompting`（19）、`without authentication`（43）、`consent`（76）、`unauthorized`（37）这套**授权缺口**语言替代了"用户点没点"这套**交互**语言。这是第 #119、#120 两篇（无需点击／需要一次交互）在选题上必须重新定义判据的原因。

**其七：中英两层方向相反。** 中文手册层把"远程"用在管控功能（`远程擦除` 占 `远程` 的 83.5%），英文措辞层把 `remote` 用在攻击者称谓（89.9%）；中文层没有 `可达`/`触达`/`近场`/`套接字`/`网络流量` 这五个概念词（各 0），而英文层用 `proximity` 30、`local network` 112 承载了对应语义。**任何一侧的词表都不能当作另一侧的口径。**

**其八：口径先于结论。** 本篇的四个关键数字（R 的 843、`sandbox` 的 40/28、`denial-of-service` 的 457、`local network` 的 112）在没有声明字段口径时**都可以被读成另一个数**；接缝刀（丢 25／410／382）与折半 bug（91→46）证明：**同一批语料，脚本写法不同，结论就不同**。所以本篇不给"某某组件可远程触达"这种脱离口径的断言，只给"在 Impact 字段、按并集口径、按词界匹配时，该词形命中多少段"。

# 附表 A 远程触达六组判据电池（120 行）

| 组 | 判据（词界/词组） | 全字段并集 | Impact | Description | 致谢 | 页 | 名 |
|---|---|---:|---:|---:|---:|---:|---:|
| 远程指称 | `remote`（词界） | 257 | 257 | 1 | 0 | 101 | 34 |
| 远程指称 | `remotely` | 5 | 5 | 0 | 0 | 5 | 1 |
| 远程指称 | `remote attacker` | 194 | 194 | 0 | 0 | 89 | 26 |
| 远程指称 | `remote user` | 37 | 37 | 0 | 0 | 27 | 7 |
| 远程指称 | `remote content` / `remote images` | 24 | 24 | 1 | 0 | 23 | 2 |
| 远程指称 | `remote code execution` | 2 | 2 | 0 | 0 | 2 | 1 |
| 远程指称 | `attacker`（词界） | 890 | 890 | 0 | 0 | 160 | 112 |
| 远程指称 | `attackers` | 0 | 0 | 0 | 0 | 0 | 0 |
| 远程指称 | `attack`（词界） | 90 | 90 | 0 | 0 | 67 | 11 |
| 远程指称 | `network`（词界） | 296 | 290 | 11 | 0 | 107 | 36 |
| 远程指称 | `internet` | 14 | 14 | 0 | 0 | 9 | 3 |
| 远程指称 | `online` | 0 | 0 | 0 | 0 | 0 | 0 |
| 远程指称 | `web`（词界） | 908 | 908 | 0 | 0 | 167 | 22 |
| 远程指称 | `website` / `website` | 248 | 222 | 0 | 27 | 119 | 16 |
| 远程指称 | `web page` | 0 | 0 | 0 | 0 | 0 | 0 |
| 远程指称 | `webpage` | 38 | 38 | 0 | 0 | 27 | 4 |
| 远程指称 | `URL` | 22 | 7 | 19 | 0 | 22 | 5 |
| 远程指称 | `link` | 24 | 15 | 9 | 0 | 24 | 3 |
| 通道与协议 | `Wi-Fi` / `wireless` | 23 | 18 | 0 | 5 | 21 | 4 |
| 通道与协议 | `cellular` | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `Bluetooth` | 32 | 32 | 0 | 3 | 24 | 4 |
| 通道与协议 | `BLE` | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `NFC` | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `UWB` | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `USB` | 5 | 5 | 0 | 0 | 5 | 2 |
| 通道与协议 | `Lightning` | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `Thunderbolt` | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `Ethernet` | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `hotspot` | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `VPN` | 6 | 6 | 0 | 0 | 6 | 1 |
| 通道与协议 | `proxy` | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `Bonjour` | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `mDNS` | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `AirPlay` / `AirPlay` | 7 | 7 | 0 | 0 | 7 | 1 |
| 通道与协议 | `AirDrop` | 3 | 3 | 0 | 0 | 3 | 1 |
| 通道与协议 | `TCP` / `UDP` | 8 | 8 | 0 | 0 | 8 | 1 |
| 通道与协议 | `IP`（词界） | 21 | 21 | 0 | 0 | 18 | 5 |
| 通道与协议 | `DNS` | 7 | 7 | 0 | 0 | 7 | 1 |
| 通道与协议 | `HTTP` / `HTTPS` | 16 | 6 | 10 | 0 | 15 | 3 |
| 通道与协议 | `socket` | 8 | 8 | 0 | 0 | 8 | 1 |
| 通道与协议 | `port`（词界） | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `packet`（单数） | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `packets`（复数） | 20 | 20 | 0 | 0 | 20 | 3 |
| 通道与协议 | `frame` | 10 | 10 | 0 | 0 | 10 | 2 |
| 通道与协议 | `stream` | 29 | 29 | 0 | 0 | 29 | 3 |
| 通道与协议 | `relay` | 20 | 20 | 0 | 0 | 17 | 5 |
| 通道与协议 | `server` | 73 | 73 | 0 | 0 | 26 | 7 |
| 通道与协议 | `client` | 1 | 0 | 1 | 0 | 1 | 1 |
| 通道与协议 | `peer` | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `beacon` | 3 | 0 | 3 | 0 | 3 | 1 |
| 通道与协议 | `advertisement` | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `proximity` | 30 | 30 | 0 | 0 | 22 | 2 |
| 通道与协议 | `nearby` | 3 | 3 | 0 | 0 | 3 | 1 |
| 通道与协议 | `physical access` | 142 | 142 | 0 | 0 | 71 | 39 |
| 通道与协议 | `short-range` | 0 | 0 | 0 | 0 | 0 | 0 |
| 通道与协议 | `in range` | 0 | 0 | 0 | 0 | 0 | 0 |
| 输入形态 | `message` | 22 | 21 | 1 | 0 | 22 | 4 |
| 输入形态 | `SMS` / `MMS` | 0 | 0 | 0 | 0 | 0 | 0 |
| 输入形态 | `iMessage` | 0 | 0 | 0 | 0 | 0 | 0 |
| 输入形态 | `attachment` | 1 | 1 | 0 | 0 | 1 | 1 |
| 输入形态 | `mail` / `email` | 31 | 24 | 7 | 0 | 31 | 4 |
| 输入形态 | `notification` | 34 | 32 | 2 | 0 | 29 | 10 |
| 输入形态 | `push` | 0 | 0 | 0 | 0 | 0 | 0 |
| 输入形态 | `payload` | 0 | 0 | 0 | 0 | 0 | 0 |
| 输入形态 | `request` | 6 | 2 | 4 | 0 | 6 | 2 |
| 输入形态 | `response` | 19 | 19 | 0 | 0 | 12 | 2 |
| 输入形态 | `header` | 7 | 0 | 7 | 0 | 7 | 1 |
| 输入形态 | `input` | 768 | 38 | 730 | 0 | 155 | 101 |
| 输入形态 | `archive` | 29 | 29 | 0 | 0 | 17 | 6 |
| 输入形态 | `file` | 1170 | 1058 | 126 | 0 | 177 | 119 |
| 输入形态 | `image` | 302 | 302 | 1 | 0 | 134 | 19 |
| 输入形态 | `video` | 83 | 83 | 0 | 0 | 67 | 8 |
| 输入形态 | `audio` | 59 | 59 | 0 | 0 | 51 | 6 |
| 输入形态 | `font` | 60 | 54 | 0 | 6 | 46 | 5 |
| 输入形态 | `media file` | 102 | 102 | 0 | 0 | 74 | 7 |
| 输入形态 | `web content` | 895 | 895 | 0 | 0 | 167 | 21 |
| 输入形态 | `maliciously crafted` | 1769 | 1769 | 0 | 0 | 218 | 81 |
| 输入形态 | `specially crafted` | 0 | 0 | 0 | 0 | 0 | 0 |
| 输入形态 | `malicious` | 678 | 627 | 0 | 52 | 160 | 95 |
| 触达与处理动词 | `reach`（词族，不含 able） | 0 | 0 | 0 | 0 | 0 | 0 |
| 触达与处理动词 | `reachable` | 5 | 0 | 5 | 0 | 5 | 1 |
| 触达与处理动词 | `accessible` | 3 | 3 | 0 | 0 | 3 | 1 |
| 触达与处理动词 | `deliver-` | 4 | 1 | 0 | 3 | 4 | 2 |
| 触达与处理动词 | `receive-` | 6 | 6 | 0 | 0 | 5 | 2 |
| 触达与处理动词 | `send` | 34 | 24 | 10 | 0 | 32 | 6 |
| 触达与处理动词 | `transmit-` | 1 | 1 | 0 | 0 | 1 | 1 |
| 触达与处理动词 | `connect-` | 87 | 86 | 1 | 0 | 35 | 15 |
| 触达与处理动词 | `trigger-` | 0 | 0 | 0 | 0 | 0 | 0 |
| 触达与处理动词 | `invoke-` | 0 | 0 | 0 | 0 | 0 | 0 |
| 触达与处理动词 | `parse-` | 229 | 146 | 83 | 0 | 89 | 40 |
| 触达与处理动词 | `process-` | 1897 | 1889 | 8 | 0 | 228 | 83 |
| 触达与处理动词 | `mount-` | 43 | 43 | 0 | 0 | 20 | 9 |
| 触达与处理动词 | `extract-` | 7 | 7 | 0 | 0 | 7 | 2 |
| 触达与处理动词 | `visit-` | 88 | 88 | 0 | 0 | 57 | 6 |
| 触达与处理动词 | `open` | 278 | 27 | 200 | 55 | 121 | 28 |
| 触达与处理动词 | `load` | 40 | 14 | 1 | 25 | 32 | 8 |
| 交互与授权前提 | `without prompting` | 19 | 19 | 0 | 0 | 19 | 2 |
| 交互与授权前提 | `without authentication` | 43 | 43 | 0 | 0 | 33 | 11 |
| 交互与授权前提 | `unauthenticat-` | 9 | 9 | 0 | 0 | 9 | 3 |
| 交互与授权前提 | `unauthorized` | 37 | 21 | 16 | 0 | 35 | 9 |
| 交互与授权前提 | `user interaction` | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互与授权前提 | `without user interaction` | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互与授权前提 | `zero-click` | 0 | 0 | 0 | 0 | 0 | 0 |
| 交互与授权前提 | `click` | 4 | 1 | 2 | 1 | 4 | 3 |
| 交互与授权前提 | `tap`（词界） | 2 | 0 | 0 | 2 | 2 | 1 |
| 交互与授权前提 | `consent` | 76 | 54 | 25 | 0 | 58 | 18 |
| 交互与授权前提 | `prompting` | 19 | 19 | 0 | 0 | 19 | 2 |
| 交互与授权前提 | `approval` | 0 | 0 | 0 | 0 | 0 | 0 |
| 接口实体名词 | `API` | 13 | 7 | 6 | 0 | 12 | 3 |
| 接口实体名词 | `interface` | 52 | 29 | 29 | 0 | 49 | 10 |
| 接口实体名词 | `endpoint` | 0 | 0 | 0 | 0 | 0 | 0 |
| 接口实体名词 | `daemon` | 2 | 2 | 0 | 0 | 2 | 1 |
| 接口实体名词 | `service` | 46 | 39 | 7 | 1 | 31 | 10 |
| 接口实体名词 | `framework` | 0 | 0 | 0 | 0 | 0 | 0 |
| 接口实体名词 | `protocol` | 12 | 0 | 12 | 0 | 12 | 2 |
| 接口实体名词 | `argument` | 0 | 0 | 0 | 0 | 0 | 0 |
| 接口实体名词 | `sandbox` | 412 | 348 | 87 | 0 | 109 | 68 |
| 接口实体名词 | `kernel` | 653 | 653 | 0 | 0 | 128 | 37 |
| 接口实体名词 | `extension` | 17 | 17 | 0 | 0 | 16 | 6 |
| 接口实体名词 | `XPC` | 0 | 0 | 0 | 0 | 0 | 0 |

# 附表 B 远程触达集 R 的十一分区（12 行）

| 类 | 判据（Impact 句内首次命中，按表序） | 段 | 页 | 名 | distinct Impact 句 |
|---|---|---:|---:|---:|---:|
| C1 | remote attacker | 194 | 89 | 26 | 16 |
| C2 | website / website 站点入口 | 222 | 105 | 10 | 26 |
| C3 | network（含 local network） | 282 | 107 | 35 | 40 |
| C4 | internet | 14 | 9 | 3 | 3 |
| C5 | Bluetooth | 27 | 19 | 4 | 7 |
| C6 | proximity（邻近） | 30 | 22 | 2 | 4 |
| C7 | Wi-Fi / wireless | 3 | 3 | 1 | 2 |
| C8 | AirPlay / AirPlaye / AirDrop | 3 | 3 | 1 | 1 |
| C9 | USB | 5 | 5 | 2 | 3 |
| C10 | packet / packets | 0 | 0 | 0 | 0 |
| C11 | remote（不与 attacker 同段） | 63 | 44 | 10 | 11 |
| **合计** | C1–C11 十一类互斥并集 = 远程触达集 R | **843** | **178** | **72** | **113** |

# 附表 C 组件名 × 通道分区矩阵（22 行）

| 组件名 | R 内段 | C1 | C2 | C3 | C4 | C5 | C6 | C7 | C8 | C9 | C10 | C11 |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| WebKit | 134 | 12 | 122 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| AirPlay | 77 | 0 | 0 | 77 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Kernel | 77 | 37 | 0 | 22 | 0 | 7 | 0 | 0 | 0 | 0 | 0 | 11 |
| Safari | 56 | 0 | 56 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Wi-Fi | 41 | 0 | 0 | 11 | 0 | 0 | 27 | 3 | 0 | 0 | 0 | 0 |
| mDNSResponder | 35 | 8 | 0 | 27 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Security | 34 | 23 | 0 | 3 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 8 |
| Model I/O | 33 | 33 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Bluetooth | 26 | 8 | 0 | 7 | 0 | 8 | 3 | 0 | 0 | 0 | 0 | 0 |
| Mail | 16 | 0 | 0 | 3 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 13 |
| CFNetwork | 15 | 0 | 0 | 8 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 7 |
| CoreUtils | 15 | 0 | 0 | 15 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| SMB | 14 | 2 | 0 | 12 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Shortcuts | 12 | 0 | 0 | 0 | 12 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| Core Bluetooth | 11 | 0 | 0 | 0 | 0 | 11 | 0 | 0 | 0 | 0 | 0 | 0 |
| Mail Drafts | 11 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 11 |
| CUPS | 10 | 0 | 0 | 7 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 3 |
| Authentication Services | 9 | 0 | 9 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 802.1X | 8 | 0 | 0 | 8 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| AirDrop | 8 | 0 | 0 | 5 | 0 | 0 | 0 | 0 | 3 | 0 | 0 | 0 |
| **其余 52 个组件名合计** | **201** | **71** | **35** | **77** | **2** | **1** | **0** | **0** | **0** | **5** | **0** | **10** |
| **合计（= 远程触达集 R）** | **843** | **194** | **222** | **282** | **14** | **27** | **30** | **3** | **3** | **5** | **0** | **63** |

# 附表 D `maliciously crafted` 之后的第一个名词闭合（36 行）

| crafted 之后的第一个词 | 段 | 代表句（截取） |
|---|---:|---|
| `web` | 793 | Processing maliciously crafted web content may lead to arbitrary code execution. |
| `file` | 290 | Processing a maliciously crafted file may lead to unexpected app termination |
| `image` | 167 | Processing a maliciously crafted image may lead to a denial-of-service |
| `media` | 102 | Processing an audio stream in a maliciously crafted media file may result in code exec |
| `video` | 74 | Processing a maliciously crafted video file may lead to unexpected app termination |
| `font` | 54 | Processing a maliciously crafted font may result in the disclosure of process memory |
| `webpage` | 38 | A maliciously crafted webpage may be able to fingerprint the user |
| `3d` | 35 | Processing a maliciously crafted 3D model may lead to memory corruption |
| `website` | 19 | Visiting a maliciously crafted website may lead to a cross-site scripting attack |
| `texture` | 18 | Processing a maliciously crafted texture may lead to unexpected app termination |
| `backup` | 17 | Restoring a maliciously crafted backup file may lead to modification of protected syst |
| `disk` | 17 | A maliciously crafted disk image may bypass Gatekeeper checks |
| `message` | 16 | Processing a maliciously crafted message may lead to a denial-of-service |
| `audio` | 14 | Processing a maliciously crafted audio file may lead to memory corruption |
| `archive` | 12 | Unpacking a maliciously crafted archive may allow an attacker to write arbitrary files |
| `asset` | 12 | Processing a maliciously crafted asset catalog may result in disclosure of process mem |
| `zip` | 10 | A maliciously crafted ZIP archive may bypass Gatekeeper checks |
| `photo` | 9 | A logic issue existed when processing a maliciously crafted photo or video shared via  |
| `smb` | 9 | Mounting a maliciously crafted SMB network share may lead to system termination |
| `contact` | 7 | Processing a maliciously crafted contact may leak sensitive data |
| `string` | 6 | Processing a maliciously crafted string may lead to heap corruption |
| `app` | 5 | A maliciously crafted app may be able to bypass code signing enforcement |
| `files` | 5 | Mounting a disk image with maliciously crafted files may lead to unexpected system ter |
| `usd` | 5 | Processing a maliciously crafted USD file may disclose memory contents |
| `volume` | 5 | Mounting a maliciously crafted volume may lead to unexpected system termination |
| `email` | 4 | A maliciously crafted email may be able to initiate FaceTime calls without user author |
| `keynote` | 4 | Processing a maliciously crafted Keynote file may disclose memory contents |
| `pages` | 4 | Processing a maliciously crafted Pages document may result in unexpected termination o |
| `webarchive` | 4 | Opening a maliciously crafted webarchive file may lead to universal cross-site scripti |
| `afp` | 3 | Mounting a maliciously crafted AFP network share may lead to system termination |
| `document` | 3 | Processing a maliciously crafted document may lead to an out-of-bounds read |
| `binary` | 2 | Parsing a maliciously crafted binary may lead to unexpected app termination |
| `exfat` | 2 | Mounting a maliciously crafted exFAT volume may cause unexpected system termination or |
| `ntlm` | 2 | Processing maliciously crafted NTLM input may lead to unexpected app termination |
| `repository` | 2 | Cloning a maliciously crafted repository may result in remote code execution |
| **合计** | **1769** | = crafted 的 Impact 段总数（35 个首词闭合） |

# 附表 E 错误写法与纠正对照（8 行）

| 错误写法 | 错在哪 | 正确写法 |
|---|---|---|
| "顾问页里 843 个远程漏洞" | 843 是**段落**（组件条目）不是漏洞编号；同一句 Impact 在多页重复 | "843 段 Impact 含通道词，覆盖 178 页、72 个组件名、113 条 distinct 句" |
| "`network` 出现 290 次，其中 C3 区 282 段，所以差 8 段" | 290 是**逐词**口径、282 是**首次命中分区**口径，两者不可直接相减 | 分别报 290（词族）与 282（分区），并注明 8 段被 C1 `remote attacker` 先收走 |
| "`packets` 分区为 0，所以 `packets` 不重要" | C10 = 0 是**顺序**造成的；`packets` 实际 20 段且 100% 与 `network` 同段 | "`packets` 词族 20 段，全部含 `network`，故在按序分区中落入 C3" |
| "`remote`＋`remotely` 共 262 段写远程" | 两个集合**零交**，直接相加会把同一段落重复计的风险留白 | "二者零交，并集 262；`remotely` 的 5 段是同一句 FaceTime 副词用法" |
| "`sandbox` 40 段说明沙箱被远程逃逸" | 40 是**词界并集**口径的 ∩R；句式级为 28；拼接口径下 `sandbox` 本身只剩 191 段 | "R ∩ `sandbox` = 40（词界并集）／28（句式级）；其中 20 段为同一句 `outside the sandbox` 陈述" |
| "`denial-of-service` 457 段说明一半攻击者段是拒绝服务" | 457（DoS 混排口径）与 457（attacker 无通道段）**数值巧合、口径无关** | 两处各自标明口径分别引用，禁止互为分母 |
| "433 ＋ 457 = 890 证明了 R 与 attacker 的关系" | 该式是**定义恒等**（R 之外的 attacker 段即无通道段），不含新信息 | "R ∩ attacker = 433，即 R 的 51.4%（分母 843）以 attacker 为主语" |
| "中文手册层 `局域网` 829 次对应英文 `local network` 112 段" | 中文 `局域网` 829 **完全等于** `无线局域网` 829，即只作为功能名出现；与英文的通道义不同源 | 分列两层：中文"无线局域网（功能名）"、英文 `local network`（通道定语 112 段，其中大写标签 `Local Network` 8 段） |

---

**边界声明：** 本篇全部材料来自公开可得的安全响应通告与公开安全文档的字面统计，仅用于防御性分析与文本计量研究。文中不含有任何绕过方法、利用代码、恶意样本、利用链构造方式，亦不含针对他人设备的具体操作步骤与时序；所有数字均为公开英文措辞在指定字段口径下的出现频次，不构成对任何组件可被实际触达或未修复的判断。

Telegram: https://t.me/one00190
