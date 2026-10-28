@@TITLE@@iOS 设备重启或会话结束后仍维持的非预期访问能力
@@SUBTITLE@@选题给了**两个时间边界（重启、会话结束）＋一个状态（仍维持）＋一个对象（非预期访问能力）**。逐词测量之后的结果是：**两个时间边界在 Apple 安全响应顾问页的英文措辞层里都不存在**——附表 A 的 64 项判据里 **26 项三字段全零**，其中"重启"整组 12 项里 **9 项全零**（`reboot`、`restar-`、`power`、`booting`、`startup`、`relaunch`、`recovery`、`DFU`、`safe mode` 各 **0 段**），让步连词组 `even after / despite / regardless / following / subsequent / once` **六个全零**；"会话"只有 **4 段**，且 **Impact 侧 0 段、Description 侧 4 段**，四段的 Description 是**同一句话**。**"维持"不以动词或名词存在，只以形容词存在**：`persistent` **40 段**、`persist` 4 段、`persistently` 2 段、`persistence` **0**、`remain / survive / continue / keep / ongoing` **各 0**。而 `persistent` 之后能出现的名词**只有 3 个**：`account`（18）、`device`（15）、`denial`（7），**18＋15＋7 = 40 恰好闭合**；`persistent access / ability / permission / privileges / entitlement / capability / control / session / login / unauthorized / root / administrator / tracking / presence` **十四项全零**——也就是说**选题要找的"维持的访问能力"这个短语，措辞层一个字都没写过**。真正的跨边界陈述只有 **4 段**（`across reinstalls` 3 段 ＋ `on an erased device` 1 段）。所以本篇提出**三把刀**：**后果刀**（`termination` 1,520 段全部是"意外终止"这一**后果**，`lead to … termination` 572 段 = 37.6%，**没有一段把它当时间前提**）、**词形刀**（维持义 46 段里形容词 40 ∶ 动词 4 ∶ 名词 0）、**接缝刀**（同一批段落用"三字段拼接"统计，`identifier` 得 **3 段**（并集 36，**丢 33**）、`shutdown` 得 **0**（16，全丢）、`termination` 得 **590**（1,520，丢 930））。
@@INFO@@报告日期|2026 年 10 月 28 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 7,434 段含 Description / 9,132 段含致谢**。重启词族集 R = 除 `shutdown` **16**、`boot` **1**、`firmware` **6** 外**整组 0 段**；会话集 SS = **4 段 / 4 页 / 2 名**；维持词族集 PF（`persist` 前缀）= **46 段 / 26 页 / 10 名**；维持集 M = `persist ∪ retain ∪ across reinstalls ∪ may still ∪ erased device` = **57 段 / 34 页 / 14 名 / 11 条 distinct Impact 句**；后果集 TM = `termination` **1,520 段 / 165 页 / 101 名**。对照集 E = `exploit` 词族 **108 段**（#114 手段全集）、P = `attacker` 词族 **890 段**（#113 主语全集）、F = `maliciously crafted` **1,769 段**（#116 输入全集）、D_I = `data` 的 Impact **1,240 段**、C_I = `code` 的 Impact **363 段**（同为 #116）。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**公开文本用哪些词形、哪些连词、哪些句法位置来写"某种状态跨过了某个边界还在继续"这件事，以及在哪些维度上它根本不写边界**，属于措辞与文本计量分析
@@INFO@@分类|判据**电池 64 项 / 全零 26 项 / 非零 38 项**（附表 A，五组：重启 12、会话 8、维持 19、边界连词 15、锁态 10）/ 维持集 M **九分区闭合 = 57**（附表 B，10 行）/ 锁态与重启邻近机制**矩阵 22 行**（附表 C）/ `without` 的 Impact **178 段第一实词 13 项闭合**（附表 D）/ **三把刀**：后果刀（1,520 段零时间前提）、词形刀（40 ∶ 4 ∶ 0）、**接缝刀**（四词丢 980 段）/ **零交六项**：M ∩ `termination` = **0**、M ∩ `locked` = **0**、M ∩ `without` = **0**、M ∩ `data`(Impact) = **0**、M ∩ E = **0**、M ∩ F = **0**
@@INFO@@难度|进阶（第 6 章的 M ∩ `identifier` = 36 是**成员集合求交**的结果，不能由 57 与 36 两个数推出"必然包含"；第 8 章要求把 `persistent` 的每一处右邻词逐个取出才能得到 3 项闭合；第 10 章的后果刀必须同时给出 1,520、6 个修饰词与 572 三个数才不会读成"1,520 段里有 572 段是终止"；第 14 章的接缝只在**字段末尾没有标点**时发作，是脚本层面的静默低估而不是语料现象；第 16 章的 `until` 11 段全部落在致谢，恰好等于域 B 的 11 份页面，这是**切块口径的自检信号**而非语料事实）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

选题字面是"设备重启或会话结束后仍维持某种非预期访问能力"。这是一个**带时间前提的状态句**：它假设公开文本里存在"某件事发生在 T0，边界事件（重启、会话结束）发生在 T1，同一件事在 T1 之后仍然成立"这三段式陈述，并且假设"非预期访问能力"是可以被"维持"的那个宾语。

逐词测量之后，本篇的结论是：**这个三段式在措辞层不成立**。第一段（T0 状态）在文本里以**形容词**出现——`persistent`；第二段（边界事件）**没有词**——`reboot / restart / power / booting / startup / relaunch / recovery / DFU / safe mode` 九个词形各 **0 段**，`even after / despite / regardless / following / subsequent / once` 六个让步连词各 **0 段**；第三段（"仍然成立"）**没有句法位置**——`remain / survive / continue / keep / persistence / ongoing` 六个词各 **0 段**。

五组反差。

**反差一（"重启"整组为零，而且零在原文里）**：附表 A 的"重启"组共 12 项判据，**9 项在 Impact、Description、致谢三个字段里一段都没有**。这不是切块切掉了：对 257 份镜像 HTML **全文**（不分字段、不分块）做原始扫描，`reboot / restarting / restart / rebooting / persisted / persistence / survives / remains / powering / shutdowns` 的出现次数**全部是 0**。非零的只有三项：`shutdown` **16 段 / 12 页 / 2 名**，且 16 段是**同一句话** `A local attacker may be able to cause unexpected system shutdown`（"意外系统关闭"是**后果**，不是边界事件）；`firmware` **6 段**（Impact 3、致谢 3）；`boot` **1 段**——`USB Restricted Mode may not be applied to accessories connected during boot`（页 125110），这是全树**唯一一次**把"启动"当时间状语写。（第 3 章）

**反差二（"会话"只有 4 段，且全在 Description 侧）**：`session` 词界命中 **4 段 / 4 页 / 2 名**，`sessions` **0**、`log in / log out / logout` **0**、`end of session` **0**。这 4 段的 **Impact 侧一条都不含 session**（I=0），Description 侧 4 段是**同一句**：`A session management issue was addressed with improved checks.`（distinct = 1）。它们的后果分别是蓝牙配件配对密钥被提取（页 120303）与 Voice Control 转写他人活动（页 125886–125888）。也就是说：**"会话"在措辞层是一个缺陷类型标签，不是一个时间边界**。相邻的两个词形 `terminate` 26 段、`termination` **1,520 段**看起来像"结束"，但下一节会看到它们全部是**后果**。（第 4 章）

**反差三（"维持"只有形容词，宾语只有三个词）**：`persist-` 词族并集 **46 段 / 26 页 / 10 名**，其中 `persistent` **40**、`persist`（动词裸形）**4**、`persistently` **2**、`persistence` **0**——40＋4＋2 = 46 说明**没有一段同时使用两个词形**。把 40 段 `persistent` 的每一处右邻词取出来，**distinct 恰好 3 个**：`account` 18、`device` 15、`denial` 7（18＋15＋7 = 40 闭合，`denial` 全部来自 `persistent denial-of-service`）。而"维持某种访问能力"这个字面组合的 **14 个候选宾语（access、ability、permission、privileges、entitlement、capability、control、session、login、unauthorized、root、administrator、tracking、presence）全部 0 段**。（第 5、8 章）

**反差四（真正的跨边界陈述只有 4 段）**：把五组判据取并集得到维持集 M = **57 段 / 34 页 / 14 名**。按"是否写明边界"切分：P1–P3（persistent＋identifier 33、persistent＋DoS 7、persistently＋fail to lock 2）**没有边界，共 42 段 = 73.7%**；P4–P9（after navigation 3、on an erased device 1、on the device 5、across reinstalls 3、Lockdown turned on 2、from deleted apps 1）**有边界，共 15 段 = 26.3%**。而 15 段里能算"设备级边界"的只有 **4 段**：`identify a user across reinstalls` **3 段**（iCloud，页 149034/149035/149038）与 `silently persist an Apple Account on an erased device` **1 段**（Find My，页 128072）。**重启、关机、会话结束这三种边界，一段都没有**。（第 6、7 章）

**反差五（三把刀）**：**后果刀**——`termination` 的 1,520 段里，修饰词只有 6 个：`system` 814、`app` 612、`process` 63、`application` 26、`unexpected` 4、`tool` 1（合计 1,520 闭合）；`lead to … termination` **572 段 = 37.6%**，全部是"导致意外终止"这一后果句式，**M ∩ termination = 0**，且 57 段 M 里**没有一段与 reboot/restart/shutdown/recovery/boot 任一词同段**。**词形刀**——"维持"义 46 段中形容词 ∶ 动词 ∶ 副词 ∶ 名词 = **40 ∶ 4 ∶ 2 ∶ 0**；名词为 0 意味着措辞层**从不把"维持"当作一件事来谈**。**接缝刀**——同一批段落，把三字段拼接后再匹配，`identifier` 从 **36 段掉到 3 段（丢 33）**、`shutdown` 从 **16 掉到 0（全丢）**、`termination` 从 **1,520 掉到 590（丢 930）**、`locked` 从 100 掉到 99（丢 1），四词合计**静默丢 980 段**；而 `persist / persistent / lock screen / session / without / after` 六项**丢 0**——接缝刀只咬**字段末尾正好把关键词夹在边界上**的那批段落（第 14 章）。

于是本篇的问题依次是：

**问题一（第 3 章）** "重启"这个词到底有没有被写过。答案：**九个词形 0 段，且在原始 HTML 层面 0 次**；非零的 `shutdown` 是后果、`firmware` 是组件、`boot` 只出现 1 次且是介词宾语。

**问题二（第 4 章）** "会话结束"有没有被写过。答案：`session` 只作缺陷类型标签出现 4 次，`end of session` **0**；`termination` 的 1,520 段全是后果；措辞层**没有"结束"这个时间点**。

**问题三（第 5–8 章）** "维持"以什么形式存在。答案：**形容词 `persistent` ＋ 三个宾语**，外加动词 `persist` 4 段、副词 `persistently` 2 段；名词与全部同义词（remain/survive/continue/keep/ongoing）**为 0**。

**问题四（第 6、7 章）** 那 57 段写了什么。答案：**11 条 distinct Impact 句**（附表 B 的九分区就是这 11 句按判据归档），其中 **33 段说"某个标识符是持久的"**、**7 段说"造成持续性拒绝服务"**、**5 段说"标记删除的通知仍留在设备上"**、**4 段是真的跨边界陈述**。

**问题五（第 13 章）** "非预期访问能力"这一层在措辞上如何承载。答案：`be able to`（Impact）**4,723 段 / 80 个动词**；M 内只用到 **4 个**：`read` 33、`cause` 7、`identify` 3、`silently`（persist）1，合计 44 段＝M 内 `may be able to` 框架的段数。

**问题六（第 17 章）** 中文手册层写不写边界。答案：**写，而且只写防御机制**：`重启` 41 次 / 20 页、`重启后` 7 次 / 7 页、`首次解锁` 16 次 / 4 页、`会话结束` 3 次 / 2 页、`仍然` 6 次 / 6 页、`维持` 3 次 / 3 页；但 `持久` **0**、`残留` **0**、`依旧 / 依然` **0**、`后门` **0**、`越狱` **0**。且"重启后"的 7 处**全部**是"重启后需要密码 / 重启后首次解锁才可用 / 重启后可通过 SSH 解锁 FileVault"这类**机制陈述**，无一处指攻击后果。

# 1 操作性定义与分母

**T2（措辞层）**：`_tmp/adv2/<id>.html` 共 258 份，排除页面 `122085` 得 **257 页**。按 #111 第 1 章的两版式域切块：域 A 以 `<h3 class="gb-header">` 起块、四个结束锚点收块；域 B（11 份粗体版式页面：`118479, 120303–120305, 120307–120312, 120339`）以粗体段起块。得 **9,136 段**（域 A **9,045** ＋ 域 B **91**）、**257 页**、**472 个块名**；含 Impact **7,434**、含 Description **7,434**、含致谢 **9,132**。

**段（block）**＝一个组件条目，`{imp, des, ack, nm, id}` 五字段；**判据命中**＝该段任一字段出现该词形；**并集**＝Impact∪Description∪致谢 三个字段**分别匹配后取并集**（这是本篇唯一允许的口径，理由见第 14 章）。

**词界**用 `(?<![\w-])kw(?![\w-])`；**词族**用前缀 `(?<![\w-])kw`（因此 `persist` 词族含 `persistent`、`persistently`）。词组内空格统一用 `$S = [ \x{a0}\x{202f}]`（普通空格、不换行空格、窄不换行空格三形），写作 `foo${S}bar`，**`${S}` 前后不得再留字面空格**——留了就是要求两个空格，会把命中打到 0（第 18 章记过一次）。

**五个判据集合（本篇定义）**：

- **R**（重启词族）= `reboot ∪ restar- ∪ power ∪ booting ∪ startup ∪ relaunch ∪ recovery ∪ DFU ∪ safe mode` ＝ 附表 A 的"重启"组 9 项判据，**9 项全 0 段**。
- **SS**（会话集）= `(?<![\w-])session(?![\w-])` ＝ **4 段 / 4 页 / 2 名**，Impact 侧 **0**。
- **PF**（维持词族）= `(?<![\w-])persist` ＝ **46 段 / 26 页 / 10 名**。
- **M**（维持集，本篇主集合）= `persist ∪ retain ∪ across${S}reinstalls ∪ may${S}still ∪ erased${S}device` ＝ **57 段 / 34 页 / 14 名**。
- **TM**（后果集）= `(?<![\w-])termination(?![\w-])` ＝ **1,520 段 / 165 页 / 101 名**。

**T1（手册层）**：`_tmp/sec*.txt` 179 份，排除 `sec2727afbbc` 得 **178 页可用正文域**；中文词表按字面出现计数，同时报**次数**与**页数**。

**为什么 M 是五个判据的并集而不是 `persist` 词族**：因为选题里的"维持"在文本里由四件事分别承担——`persistent`（形容词，40）、`retain`（过去分词，5，Notification Services）、`across reinstalls`（3，iCloud）、`may still`（3：Call History 1 ＋ Managed Configuration 2），加上 `erased device` 那句里的 `persist`（已含在 4 段内）。5 项并集 57 段，比 `persist` 词族的 46 段多 **11 段**，这 11 段全部来自 `retain` 与 `may still`，而它们**不含任何 persist 词形**——只用 `persist` 词族会漏掉选题字面的"仍"。

# 2 结论速览（每条都可按第 19 章复算）

1. **重启整组为 0，且 0 在原文层面**：257 份 HTML 全文扫描，`reboot`、`restart`、`restarting`、`rebooting`、`persisted`、`persistence`、`survives`、`remains`、`powering`、`shutdowns` **各 0 次**。附表 A 的 12 项重启判据里 9 项 0 段。（第 3 章）
2. **非零的三项语义各属别处**：`shutdown` 16 段是**同一句** `unexpected system shutdown`（后果）；`firmware` 6 段（I3／致谢3）是组件与补丁用语；`boot` 1 段是 `during boot`（全树唯一的启动时间状语）。（第 3、14 章）
3. **会话是标签不是边界**：`session` 4 段全在 Description、句式 distinct = 1（`A session management issue was addressed with improved checks.`）；`sessions / log out / end of session` 各 **0**；`termination` 1,520 段全是后果，`lead to …` 572 段 = 37.6%。（第 4、10 章）
4. **维持词形闭合**：46 段 = `persistent` 40 ＋ `persist` 4 ＋ `persistently` 2；`persistence / remain / survive / continue / keep / ongoing / uninstall / reset` 各 **0**。`retain` 裸形 0、`retained` 5。（第 5 章）
5. **M = 57 段 / 34 页 / 14 名**，Impact 侧 57（差 0），域 A 57 / 域 B **0**，含判据的 distinct Impact 句 **11 条**。（第 6 章、附表 B）
6. **九分区闭合**：P1 33 ＋ P2 7 ＋ P3 2 ＋ P4 3 ＋ P5 1 ＋ P6 5 ＋ P7 3 ＋ P8 2 ＋ P9 1 = **57**，未归类 **0 段**。（附表 B）
7. **边界有无**：无边界 P1–P3 = **42 段 = 73.7%**；有边界 P4–P9 = **15 段 = 26.3%**；其中"设备级边界"仅 **4 段**（`across reinstalls` 3 ＋ `on an erased device` 1）。（第 6 章）
8. **`persistent` 的宾语只有 3 个词**：account 18、device 15、denial 7，合计 40 闭合；14 个候选"能力"宾语**全 0**；`persistent identifier` 相邻形式 **0**（永远经 device/account 中介）。（第 8 章）
9. **identifier 侧**：`identifier(s)` 36 段（I33／D3，15 页／7 名）、`device identifier` 15 段、`permanent identifier` **0**、`unique identifier` **0**、`UUID / UDID` **0**。`M ∩ identifier = 36`——**全部 36 段都在 M 内**，占 M 的 63.2%。（第 9 章）
10. **能力句**：`be able to` 的 Impact **4,723 段 / 80 个动词**（access 1,268、cause 1,138、break 249、bypass 241、gain 215…）；M 内只出现 4 个动词：read 33、cause 7、identify 3、silently 1 = 44。（第 13 章）
11. **情态框架三分**：M 的 57 段里 `may be able to` **44**、`could be` **5**（P6 通知滞留）、裸 `may` **8**（P3/P4/P8/P9），合计 57 闭合。（第 13 章）
12. **零交六项**：M ∩ E(108) = **0**、M ∩ F(1,769) = **0**、M ∩ D_I(1,240) = **0**、M ∩ C_I(363) = **0**、M ∩ TM(1,520) = **0**、M ∩ WO_I(178) = **0**。M ∩ P(890) = **8**。（第 15 章）
13. **锁态零交**：M ∩ `locked`(100) = **0**、M ∩ `lock screen`(66) = **0**、M ∩ `passcode`(10) = **0**、M ∩ `authenticat-`(144) = **0**；只有 M ∩ `Lockdown Mode`(10) = **2**、M ∩ `lock` 词族(228) = **4**。（第 12 章）
14. **接缝刀四词丢 980**：`identifier` 36→3、`shutdown` 16→0、`termination` 1,520→590、`locked` 100→99；六词丢 0。（第 14 章）
15. **`without` 的 Impact 178 段、第一实词 13 项闭合**：consent 54、authentication 43、prompting 19、permission 14、authorization 10、pairing 7、private 7、confirmation 5、valid 5、app 4、entitlement 4、indicator 3、restrictions 3 = **178**。M ∩ `without` = **0**。（附表 D）
16. **版式域自检**：`until` 11 段**全部落在致谢字段**，这 11 段恰好是域 B 的 11 份页面（118479、120303–05、120307–12、120339）里的页面级免责声明——**致谢里的"时间连词"不是语料事实，是切块残留**。（第 16 章）
17. **中文层反向**：手册层 `重启` 41 次/20 页、`重启后` 7/7、`首次解锁` 16/4、`会话` 78/20、`会话结束` 3/2、`仍然` 6/6、`维持` 3/3；**零**：`持久`、`残留`、`依旧`、`依然`、`重新启动`、`退出会话`、`后门`、`越狱`、`常驻`、`上电`、`掉电`、`注销`、`恢复出厂`、`重装`。（第 17 章）

# 3 "重启"：整组为 0，而且 0 在原文层面

附表 A 的"重启"组 12 项判据实测（全字段并集／Impact／Description／致谢／页／名／命中次数）：

| 判据 | 并集 | I | D | 致谢 | 页 | 名 |
|---|---:|---:|---:|---:|---:|---:|
| `reboot` 词族 | **0** | 0 | 0 | 0 | 0 | 0 |
| `restar-`（restart/restarting/restarted） | **0** | 0 | 0 | 0 | 0 | 0 |
| `power`（词界） | **0** | 0 | 0 | 0 | 0 | 0 |
| `boot`（裸形） | 1 | 1 | 0 | 0 | 1 | 1 |
| `booting` / `boot-up` | **0** | 0 | 0 | 0 | 0 | 0 |
| `startup` / `starting up` | **0** | 0 | 0 | 0 | 0 | 0 |
| `shutdown` | 16 | 16 | 0 | 0 | 12 | 2 |
| `relaunch` | **0** | 0 | 0 | 0 | 0 | 0 |
| `recovery` | **0** | 0 | 0 | 0 | 0 | 0 |
| `DFU` | **0** | 0 | 0 | 0 | 0 | 0 |
| `safe mode` | **0** | 0 | 0 | 0 | 0 | 0 |
| `firmware` | 6 | 3 | 0 | 3 | 6 | 2 |

**九项全零**。为了排除"是被切块切掉的"这一可能，本篇对 **257 份镜像 HTML 全文**（不经任何切块、不分字段）做了原始字符串扫描，大小写不敏感：`reboot = 0`、`restarting = 0`、`restart = 0`、`rebooting = 0`、`persisted = 0`、`persistence = 0`、`survives = 0`、`remains = 0`、`powering = 0`、`shutdowns = 0`。**文件数 257，命中总计 0**。所以这些零是**文本本身的零**，不是脚本的零。

`shutdown` 的 16 段是**一句**：`A local attacker may be able to cause unexpected system shutdown`（distinct 句 = 1，12 页 / 2 名）。注意它的意思不是"设备被关闭后仍……"，而是"攻击者能让系统非预期地终止"——`shutdown` 在这里是**后果宾语**，与第 10 章的 `termination` 属同一族。这是"重启"判据里最容易误读的一项：**词有了，方向反了**。

`boot` 的唯一一段（页 125110，块名 Trusted Device）是 `USB Restricted Mode may not be applied to accessories connected during boot`。它的结构是 `during + boot`，不是 `after + boot`：**全树没有"重启之后"这个介词框架**（`after boot`、`after a restart`、`post-reboot` 均 0；`after` 的 150 段里 22 段在 Impact，五类内容见第 11 章，无一与启动相关）。

`recovery` 与 `DFU` 为零也值得单独说一句：这两个词是**手册层**（T1）常用的恢复流程术语（`恢复模式` 16 次/10 页、`DFU` 16 次/7 页，第 17 章），但**顾问页从不写**——公开缺陷文本不涉及"如何进入某种启动状态"，因此也就无从表达"从那种状态出来之后仍然如何"。

**本组结论**：选题的第一个时间前提（重启）**在措辞层没有词汇承载**。要谈"重启后"，只能来自手册层或源码层，不能来自这 257 页。

# 4 "会话"：4 段、0 个边界、1 个句式

`session` 词界命中 **4 段 / 4 页 / 2 名**，字段分布 **Impact 0 / Description 4 / 致谢 0**——**四段的 Impact 里都不含 session**。逐段实测：

| 页 | 块名 | Impact（实测原文） | Description |
|---|---|---|---|
| 120303 | Bluetooth | An attacker with physical access to the accessory may be able to extract its Bluetooth pairing key and monitor Bluetooth traffic | A session management issue was addressed with improved checks. |
| 125886 | Voice Control | A user with Voice Control enabled may be able to transcribe another user's activity | 同上（同一句） |
| 125887 | Voice Control | 同上 | 同上 |
| 125888 | Voice Control | 同上 | 同上 |

**Description 侧句式 distinct = 1**：四段全部是 `A session management issue was addressed with improved checks.`。这与 #116 第 4 章发现的 Description 模板现象同源——Description 只有 `A … issue was addressed with …` 这一族句式，`session management` 是其中**一个类型槽位**。

同组其余判据：`sessions`（复数）**0**；`log in / log out / logout`（`log${S}(out|in)` ∪ `logout`）**0**；`sign in/out/on/off` **5 段**且全部在 Impact、5 页 / **1 名**，句式为 `An app may be able to use the Sign In With Apple authentication flow to access the user's Apple Account`（组件名 1 个：Apple Account）；`end of session` **0**；`disconnect` 3 段（I3）；`terminate` 26 段（I26、26 页 / 4 名）。

于是"会话结束"这个选题词在措辞层的对应情况是：**"会话"有，"结束"没有；有"会话"的四段里，"会话"是缺陷类型而不是时间参照**。

再看 `termination`（1,520 段）：它在语义上确实包含"结束"义，但它在本语料里的**全部**用法是后果（第 10 章）。**两组不能合并**：成员集合求交实测 `session ∩ termination = 0`、`session ∩ terminate = 0`，甚至 `terminate ∩ termination = 0`（26 段与 1,520 段零交）。也就是说**措辞层没有任何一段同时写"会话"与"终止"**，而 `terminate` 与 `termination` 本身也分属两批完全不同的段落——前者只有 2 条 distinct 句、4 个组件名（Audio 8、CoreAudio 8、CoreMedia 8、MetricKit 2），24 段是 `Processing an audio stream in a maliciously crafted media file may terminate the process`、2 段是 `An unprivileged process may be able to terminate a root processes`，后者是"意外终止"这一后果标签（165 页／101 名）。

**本组结论**：选题的第二个时间前提（会话结束）在措辞层同样没有承载。可用的只有 4 段，且它们是**类型标签**。

# 5 "维持"：词形闭合 40 ∶ 4 ∶ 2 ∶ 0

`persist-` 词族并集 **46 段 / 26 页 / 10 名**。四形分解：

| 词形 | 段 | 命中次数 | 备注 |
|---|---:|---:|---|
| `persistent`（形容词） | 40 | 40 | 全部在 Impact |
| `persist`（动词裸形） | 4 | 4 | 全部在 Impact |
| `persistently`（副词） | 2 | 2 | 全部在 Impact，句式 `A device may persistently fail to lock` |
| `persistence`（名词） | **0** | **0** | — |

40＋4＋2 = 46，与词族并集相等 ⇒ **没有一段同时出现两个 persist 词形**，这是词形层面的**互斥闭合**。同时注意每形"段 = 次"⇒ 每段最多出现一次该词形。

同义词表全灭（附表 A"维持"组 19 项中 8 项全零）：

`remain(s/ing)` **0** ／ `survive` 词族 **0** ／ `continue` 词族 **0** ／ `keep(s)` **0** ／ `ongoing` **0** ／ `uninstall` **0** ／ `reset` **0** ／ `persistence` **0**。

非零的近义词：`retained` **5**（`retain` 裸形 **0**，只有过去分词）、`still` **3**、`yet` **1**、`erased` **1**、`reinstall` **3**、`installed` **67**（`install` 词族 68 段：Impact 67、致谢 1）、`restor-` **18**、`remov-` **219**（但 **Impact 侧只有 4 段，215 段在 Description**——`Removed the … key`／`This update removes …` 一类的补丁说明用语，与"移除访问能力"无关，第 14 章把它单列）。

`yet` 的 1 段是 `An attacker within Bluetooth range may be able to listen through the microphone of a device which is not yet paired and actively seeking pair requests`（页 127557）——`not yet` 是**尚未**，方向与"仍然"相反；措辞层用 `yet` 只写"还没发生"，**不写"还没结束"**。

`still` 的 3 段全部在 Impact、3 页 / 2 名，逐段实测：

| 页 | 块名 | Impact |
|---|---|---|
| 122404 | Call History | Call history from deleted apps **may still appear** in spotlight search results |
| 124149 | Managed Configuration | Account-driven User Enrollment **may still be possible** with Lockdown Mode turned on |
| 124150 | Managed Configuration | 同上 |

这三段是全树**唯一**用副词"仍然"的位置，也正是附表 B 的 P8（2 段）与 P9（1 段）。它们的共同点是：**边界不是重启、不是会话结束，而是"配置项已被打开"或"应用已被删除"**。

**本组结论（词形刀）**："维持"在本语料里以**形容词**为主（40/46 = 87.0%），动词只占 4 段、副词 2 段、**名词 0**。名词为 0 的语言学后果是：措辞层**从不把"维持"主题化**——没有"持久性""驻留""残留"这类名词，因此也没有"某种东西的持久性"这种可讨论的对象。要写"某物仍在那里"，它必须找一个形容词去修饰某个名词，而被修饰的名词只有三个（第 8 章）。

# 6 维持集 M 与它的九分区（附表 B）

**M 的规模**：**57 段 / 34 页 / 14 名**；Impact 侧 **57**（M − M_I = **0**，即所有判据命中都发生在 Impact 字段）；域 A **57** / 域 B **0**；含判据的 distinct Impact 句 **11 条**（第 7 章逐条列出）。

九分区按**表序首次命中**归档（互斥），合计恰为 57，未归类 **0 段**：

| 类 | 判据 | 段 | 页 | 名 | 边界 |
|---|---|---:|---:|---:|---|
| P1 | `persistent` ＋ (device／account) identifier | 33 | 15 | 6 | 无 |
| P2 | `persistent` ＋ denial-of-service | 7 | 7 | 1 | 无 |
| P3 | `persistently` ＋ fail to lock | 2 | 2 | 1 | 无 |
| P4 | `persist`（动词）＋ after navigation | 3 | 3 | 1 | 有（导航离开） |
| P5 | `persist`（动词）＋ on an erased device | 1 | 1 | 1 | **有（抹掉）** |
| P6 | `retained` ＋ on the device | 5 | 5 | 1 | 弱（设备驻留） |
| P7 | `identify` ＋ across reinstalls | 3 | 3 | 1 | **有（重装）** |
| P8 | `may still` ＋ be possible（Lockdown turned on） | 2 | 2 | 1 | 有（配置项已开） |
| P9 | `may still` ＋ appear（from deleted apps） | 1 | 1 | 1 | 有（应用已删） |
| **合计** | | **57** | **34** | **14** | |

**两个口径**：**无边界**（P1＋P2＋P3）= 33＋7＋2 = **42 段 = 73.7%**；**有边界**（P4–P9）= 3＋1＋5＋3＋2＋1 = **15 段 = 26.3%**。

再收一层：15 段里**设备级**边界（发生在设备生命周期的不可逆事件上）只有 **4 段** = P7 3 ＋ P5 1；其余 11 段属于**会话内/配置内**边界（导航离开 3、应用被删 1、Lockdown 已开 2、通知标记删除 5）。**"重启"级与"会话结束"级边界：0 段。**

P1 的 33 段全部落在 6 个块名（AuthKit 8、DeviceCheck 8、App Store 6、Sandbox Profiles 6、CloudKit 4、Safari 3 一类，第 16 章给全表），且**同一条语义有三套措辞**：`A local app may be able to read a persistent account identifier`（18 段）、`An app may be able to read persistent device identifiers`（8 段）、`An app may be able to read a persistent device identifier`（7 段）。**18＋8＋7 = 33** ⇒ P1 的 33 段就是这三句，没有第四种写法。

P2 的 7 段是 `An attacker may be able to cause a persistent denial-of-service`：**7 页、1 个组件名、致谢字段逐段相同（同一 CVE-2025-43494）**——"持续性拒绝服务"是全树唯一把 `persistent` 用于**行为**而非**标识符**的位置。

P3 的 2 段是 `A device may persistently fail to lock`（2 页 / 1 名）：注意这里 `persistently` 修饰的是 **fail**（失败持续），不是 lock（锁不持续）——副词落在动词前时，"维持"义归属于**失效**而非归属某个能力。

**M 的施事分布**（含判据 Impact 句的前三词，distinct = 9，合计 57 闭合）：`A local app` 18、`An app …` 18、`An attacker …` 7、`Notifications marked …` 5、`A website's permission …` 3、`A device may …` 2、`Account-driven User …` 2、`An attacker with …` 1、`Call history from …` 1。**主语是"物"的共 8 段**（Notifications 5 ＋ Call history 1 ＋ A device 2），恰好是 P6＋P9＋P3——这三类里"维持"的主语**不是攻击者，是被管理的对象本身**。

# 7 M 的 11 条 distinct Impact 句（全列）

按段数降序，全部为实测原文（`\xa0` 归一为普通空格、去首尾空白后去重）：

| 段 | 页 | 句子 |
|---:|---:|---|
| 18 | 8 | A local app may be able to read a persistent account identifier |
| 8 | 8 | An app may be able to read persistent device identifiers |
| 7 | 7 | An app may be able to read a persistent device identifier |
| 7 | 7 | An attacker may be able to cause a persistent denial-of-service |
| 5 | 5 | Notifications marked for deletion could be unexpectedly retained on the device |
| 3 | 3 | A website's permission dialog may persist after navigation away from the site |
| 3 | 3 | An app may be able to identify a user across reinstalls |
| 2 | 2 | A device may persistently fail to lock |
| 2 | 2 | Account-driven User Enrollment may still be possible with Lockdown Mode turned on |
| 1 | 1 | An attacker with physical access may be able to silently persist an Apple Account on an erased device |
| 1 | 1 | Call history from deleted apps may still appear in spotlight search results |

**18＋8＋7＋7＋5＋3＋3＋2＋2＋1＋1 = 57**，与 M 段数相等 ⇒ **M 内一段一句、一句无重复**（不存在一句跨段重复但归到不同页的情形被吞掉）。

这 11 句是本篇的全部证据面。三点观察：

**（1）动词位只有四个。** 11 句的谓语动词是 `read`（18＋8＋7 = 33 段）、`cause`（7）、`be retained / be possible / appear / persist`（5＋2＋1＋3＋1 = 12）、`identify`（3）、`fail to lock`（2）、`silently persist`（1）。选题里的"访问能力"（access／ability）在 M 的谓语里**一次都没有**——**M 里没有一个 `access`**。

**（2）"跨边界"只有三种说法。** `after navigation away from the site`（网站会话内）、`across reinstalls`（应用重装）、`on an erased device`（设备抹掉）。**没有 `across reboots`、`after restart`、`across sessions`**——这三个字面组合在语料里不存在（对应词形本身即为 0）。

**（3）唯一的"静默"措辞。** 第 11 行那句 `silently persist` 是全树唯一把副词 `silently` 与 `persist` 连用的段落，也是 M 内 `be able to` 动词表里那个 `silently`（1 次）的来源——它后面接的其实是 `persist`，即 `may be able to silently persist`。**这一段的块名是 Find My、页 128072**，是全树唯一一句把"账户在抹掉后的设备上继续存在"写成能力的句子。

# 8 `persistent` 的右邻只有三个词

对 40 段 `persistent` 的每一处命中取右邻词（`persistent${S}(\w+)`，Impact 侧逐句扫描），**distinct = 3**：

| 右邻词 | 次数 |
|---|---:|
| account | 18 |
| device | 15 |
| denial | 7 |

**18＋15＋7 = 40**，恰等于 `persistent` 的段数（每段一次），因此这是**完全闭合**：`persistent` 在本语料里**只有三种搭配**，且从不用于其它名词。

反过来，选题字面"非预期访问能力"的 14 个候选宾语，逐一实测（`persistent${S}<n>`，全字段并集）：

`access` **0** ／ `ability` **0** ／ `permission` **0** ／ `privileges` **0** ／ `entitlement` **0** ／ `capability` **0** ／ `control` **0** ／ `session` **0** ／ `login` **0** ／ `unauthorized` **0** ／ `root` **0** ／ `administrator` **0** ／ `tracking` **0** ／ `presence` **0**（14 项全零）。

另外：`persistent identifier` 这一**相邻**形式为 **0**——`persistent` 从不直接修饰 `identifier`，中间必须经 `device` 或 `account` 中介（`persistent device identifier` / `persistent account identifier`）。这条细节重要，因为"持久标识符"是行业惯用语，而**公开文本不使用该惯用语的原样**。

`persistent` 之外再看"维持"义能落到哪些宾语：`retained on the device` 5 段（宾语是 Notifications）、`may persist after navigation` 3 段（主语是 permission dialog）、`may still appear` 1 段（主语是 Call history）。**被维持的东西只有：标识符、拒绝服务状态、锁态失效、权限弹窗、通知、通话记录、账户**。这七样里**没有一样是"访问能力"**。

# 9 identifier 侧：36 段全在 M 内

| 判据 | 段 | Impact | 页 | 名 |
|---|---:|---:|---:|---:|
| `identifier(s)` | 36 | 33 | 15 | 7 |
| `device identifier` | 15 | 15 | 15 | 3 |
| `permanent identifier` | **0** | 0 | 0 | 0 |
| `unique identifier` | **0** | 0 | 0 | 0 |
| `UUID` | **0** | 0 | 0 | 0 |
| `UDID` | **0** | 0 | 0 | 0 |
| `track` 词族 | 40 | 27 | 40 | 6 |
| `fingerprint` 词族 | 103 | 101 | 63 | 19 |
| `enumerate` 词族 | 38 | 38 | 27 | 10 |
| `identify` 词族 | 29 | 29 | 24 | 6 |

**`M ∩ identifier = 36`**，而 `|identifier| = 36` ⇒ **含 identifier 的 36 段全部落在 M 内**（交集 = 全集，这是成员集合求交的结果，不是两个数量能推的）。占 M 的 **36 / 57 = 63.2%**。

`identifier` 侧的四个"永久性"候选定语（`permanent`、`unique`、`UUID`、`UDID`）**全 0**：措辞层**不用标准术语描述标识符的唯一性或永久属性**，只用形容词 `persistent`（18＋15 = 33 段）。反查 `identifier ∩ persistent = 33`，正好是 P1 的段数 ⇒ **36 段 identifier 里有 3 段不带 persistent**（这 3 段落在 Description 侧：`A privacy issue was addressed with improved handling of identifiers.` 一类）。

`be able to` 的 80 个动词里与标识符相关的四个：`fingerprint` 99、`track` 26、`enumerate` 38、`identify` 28。这四个数是"跟踪"义的真正承载量，而 M 里只用到 `identify`（3 段）——**"跨重装识别用户"（3 段）与"跟踪用户"（26 段）不是同一批句子**：`across` 的 6 段里，3 段是 `identify a user across reinstalls`、3 段是 `track users across apps and websites without permission`，**两句互斥、交集 0**（第 11 章）。

# 10 后果刀：`termination` 的 1,520 段全是后果

`termination` 词界命中 **1,520 段 / 165 页 / 101 名**——这是本篇最大的一个集合，也是选题第二个时间前提"会话结束"最容易被误当成的东西（"termination = 结束"）。

把每段 Impact 里 `termination` 的左邻修饰词取出来，**distinct = 6**：

| 修饰词 | 次数 |
|---|---:|
| system | 814 |
| app | 612 |
| process | 63 |
| application | 26 |
| unexpected | 4 |
| tool | 1 |

**814＋612＋63＋26＋4＋1 = 1,520**，与段数相等 ⇒ 每段恰有一次带修饰词的 `termination`。六个修饰词全部指向**被终止的宿主**（系统／应用／进程／工具），没有一个指向"会话"。

句式：`lead to … termination`（`lead${S}to${S}.{0,25}?termination`）**572 段 = 37.6%**；`termination` 出现在 Impact 而非前提位置。所以这 1,520 段说的是"**会意外终止**"这个**后果**，不是"**在终止之后**"这个**时间**。

三条排除性验证：

- **M ∩ TM = 0**（57 段维持集与 1,520 段后果集**零交**）。
- **57 段 M 里没有任何一段含 `reboot / restart / shutdown / recovery / boot`**（同段共现计数 = **0**）。
- `terminate`（动词，26 段）与 `termination`（1,520 段）**成员交集 0**：前者只有 2 条 distinct 句、4 个组件名（Audio 8、CoreAudio 8、CoreMedia 8、MetricKit 2），24 段写"处理恶意媒体文件时进程可能终止"，2 段写"非特权进程可能终止 root 进程"；**没有一段与 `session` 的 4 段重合**。

这把刀的作用：如果有人用"`termination` 出现 1,520 次，说明公开文本大量描述进程／会话终止后的状态"来回答选题，那是**把后果读成了前提**。正确说法是：**1,520 段全部是"导致意外终止"，0 段把终止写成时间前提**。

# 11 边界连词：让步全组为 0

附表 A"边界连词"组 15 项，**6 项全零**：`even after`、`despite`、`regardless`、`following`、`subsequent`、`once`。

非零 9 项实测（并集／Impact／致谢；下表末两行 `still`、`yet` 属附表 A 的"维持"组，为对照一并列出）：

| 判据 | 段 | I | 致谢 | 页 | 名 | 内容 |
|---|---:|---:|---:|---:|---:|---|
| `after` | 150 | 22 | 1 | 66 | 19 | I 侧 5 类句（下） |
| `even`（词界） | 11 | 11 | 0 | 11 | 3 | `even when` 8 ＋ `even with` 3 |
| `even when` | 8 | 8 | 0 | 8 | 2 | 全部是 `Remote content may be loaded even when the 'Load Remote Images' setting is turned off` |
| `even with` | 3 | 3 | 0 | 3 | 1 | 全部是 `Incoming FaceTime calls can appear or be accepted on a locked macOS device, even with notifications disabled on the lock screen` |
| `across` | 6 | 6 | 0 | 5 | 2 | 3 × `identify a user across reinstalls`、3 × `track users across apps and websites without permission` |
| `without` | 189 | 178 | 11 | 105 | 39 | 附表 D 十三项闭合 |
| `until` | 11 | **0** | **11** | 11 | 6 | 全部是页面级免责声明（第 16 章） |
| `during` | 8 | 8 | 0 | 8 | 6 | 含 `during boot` 1 段、`during iPhone Mirroring` 3 段、`during a software update` 1 段 |
| `previous(ly)` | 2 | 2 | 0 | 2 | 2 | — |
| `still` | 3 | 3 | 0 | 3 | 2 | 第 5 章三段 |
| `yet` | 1 | 1 | 0 | 1 | 1 | `not yet paired`，方向相反 |

**`after` 的 22 段 Impact 只有 5 条 distinct 句**（合计 22）：`Password autofill may fill in passwords after failing authentication` **9**；`A malicious app may be able to attempt passcode entries on a locked device and thereby cause escalating time delays…` **7**；`A website's permission dialog may persist after navigation away from the site` **3**（唯一进入 M 的 3 段）；`An app from a standard user account may be able to escalate privilege after admin user login` **2**；Face ID 注册相关 **1**。

于是"**after 的 22 段里只有 3 段（13.6%）是维持句**，其余 19 段写的是认证失败后的自动填充行为与口令尝试——它们描述的是**同一状态内的先后**，不是**跨过边界的继续**"。

`even when` 的 8 段与 `even with` 的 3 段是让步框架的两个仅有的实例，且各自只对应**一句话**（Mail 远程图片 8 段、FaceTime 锁屏 3 段）。**让步框架里出现的边界是"设置项被关闭""通知被禁用"，不是"设备重启""会话结束"**。这三个词（turned off／disabled／turned on）构成了本语料真正的边界词汇表——**配置开关，而非生命周期事件**。

`across` 的 6 段是本组最有价值的一项：`across` 在措辞层只有两个宾语——`reinstalls`（3）与 `apps and websites`（3）。**`across reboots` 0、`across sessions` 0、`across restarts` 0**（这三个词组不存在的直接原因是 `reboot`、`session` 的复数搭配本身为 0）。**"跨重装"是全树唯一被写下来的应用级生命周期跨越**。

# 12 锁态层（附表 C）

选题的"非预期访问能力"若要有承载点，最自然的是**锁态**。附表 C 的 22 行矩阵给出全貌，这里取要点（均为全字段并集／Impact／Description）：

`lock` 裸形 **78**（I62／D16）；`locked` **100**（I72／D33，命中次数 **106** ⇒ 至少 6 处段内重复）；`locking` **60**（**I0／D60**，全部在 Description：`… addressed with improved locking`）；`unlock` 词族 **14**（I14）；`lock screen` **66**（I58／D8，44 页 / 17 名）；`locked device` **89**（I61／D33）；`unlocked device` **11**；`unlocked`（词界）**13**；`on a locked` **48**（I18／D33）；`on an unlocked` **0**；`fail to lock` **2**（= P3）；`Lockdown Mode` **10**；`passcode` **10**；`password` 词界 **20**；`authentication` 词族 **144**（I98／D80，命中 178）；`pairing` 词族 **22**（I22／致谢3）；`USB Restricted Mode` **5**；`Login Window` **2**；`FileVault` **1**；`backup` **21**；`restore` 词族 **18**；`erased` **1**（= P5）。

`lock screen` 的 66 段有 **19 条 distinct 句**，Top（段数≥3）：`An attacker may be able to view restricted content from the lock screen` **9**；`A malicious app may be able to dismiss the system notification on the Lock Screen that a recording was started` **7**；`An attacker with physical access may be able to access contacts from the lock screen` **7**；`An attacker with physical access may be able to share items from the lock screen` **4**；`An attacker with physical access to a device may be able to access contacts from the lock screen` **4**；其余 3 段级各一句（读联系人号码、查看受限内容、抓取键盘事件、FaceTime 锁屏来电）。**这些句子全部是"从锁屏访问"，没有一句是"锁屏状态被维持/跨过某边界后仍能访问"**。

`Lockdown Mode` 的 10 段有 **5 条 distinct 句**：`Replying to an email could display remote images in Mail in Lockdown Mode` 4；`Account-driven User Enrollment may still be possible with Lockdown Mode turned on` 2（= P8）；`On a Mac with Lockdown Mode enabled, web content opened via a file URL may be able to use Web APIs that should be restricted` 2；`Enabling Lockdown Mode while setting up a Mac may cause FileVault to become unexpectedly disabled` 1；`Forwarding an email could display remote images in Mail in Lockdown Mode` 1。

**M 与锁态的交集**：`M ∩ locked = 0`、`M ∩ lock screen = 0`、`M ∩ passcode = 0`、`M ∩ authentication = 0`、`M ∩ without = 0`；非零只有 `M ∩ Lockdown Mode = 2`（P8）与 `M ∩ lock 词族 = 4`（P3 的 2 段 `fail to lock` ＋ P8 的 2 段 `turned on`）。

`backup`（21 段）与 `restore`（词族 18 段）在措辞层是**数据迁移语境**，与"恢复出厂"无关；`erased` **只有 1 段**——即 P5 那句 Find My。**"抹掉"在顾问页几乎不存在，但在手册层是 44 次/18 页（`抹掉`）与 229 次/178 页（`擦除`）**（第 17 章）。

# 13 情态框架与能力句

`be${S}able${S}to`（Impact）**4,723 段**，其后动词 **distinct = 80**。Top 15（次数）：`access` **1,268**、`cause` **1,138**、`break` **249**、`bypass` **241**、`gain` **215**、`modify` **197**、`read` **179**、`leak` **126**、`execute` **112**、`view` **105**、`fingerprint` **99**、`elevate` **90**、`disclose` **72**、`determine` **56**、`enumerate` **38**。

M 之内（只统计含判据的 57 句）：

| 情态框架 | 段 | 占比 |
|---|---:|---:|
| `may be able to` ＋ 动词 | 44 | 77.2% |
| 裸 `may` ＋ 动词（含 `could`） | 8 | 14.0% |
| `could be` ＋ 过去分词 | 5 | 8.8% |
| **合计** | **57** | 100% |

框架与分区的对应：P1（33）、P2（7）、P5（1）、P7（3）＝ `may be able to` 44；P6（5）＝ `could be retained`；P3（2）、P4（3）、P8（2）、P9（1）＝ 裸 `may`（`may persistently fail`、`may persist`、`may still be possible`、`may still appear`）。**44＋5＋8 = 57 闭合**。

M 内 `be able to` 后接动词只有 4 个：`read` **33**、`cause` **7**、`identify` **3**、`silently`（→persist）**1** = 44。**`access` 在 M 内为 0**——这是本篇对选题最重要的一条否定：**"维持非预期访问能力"的措辞对应物是"读取一个持久标识符"，不是"维持访问"**。

对照 #114/#113：`exploit` 词族 108 段与 M **零交**，`attacker` 词族 890 段与 M 交 **8 段**（P2 的 7 段 `An attacker may be able to cause a persistent denial-of-service` ＋ P5 的 1 段）。M 的 57 段里**主语是 attacker 的只有 8 段**（第 6 章施事表：`An attacker may` 7 ＋ `An attacker with` 1），其余 49 段的主语是 app／local app／website／device／notifications／call history／enrollment。

# 14 接缝刀：四个词丢 980 段

同一批 9,136 段，两种口径：**并集口径**（三字段分别匹配再取并集）与**拼接口径**（`imp . des . ack` 连成一个串后匹配）。逐词实测：

| 判据 | 并集 | 拼接 | 丢 |
|---|---:|---:|---:|
| `persist`（词族） | 46 | 46 | 0 |
| `persistent` | 40 | 40 | 0 |
| `identifier(s)` | 36 | **3** | **33** |
| `lock screen` | 66 | 66 | 0 |
| `session` | 4 | 4 | 0 |
| `without` | 189 | 189 | 0 |
| `after` | 150 | 150 | 0 |
| `locked` | 100 | 99 | **1** |
| `shutdown` | 16 | **0** | **16** |
| `termination` | 1,520 | **590** | **930** |
| **合计丢失** | | | **980**（其中四词 33＋1＋16＋930 = **980**） |

机制：拼接后 `…identifier` 落在 Impact 字段末尾、紧邻 Description 开头的 `A privacy issue…` 时，词界 `(?![\w-])` 与后续字符的组合、以及字段末尾换行/无标点会导致**该段只被记 1 次或 0 次**；`shutdown` 与 `termination` 的段几乎全是"一字段极短、另一字段承载关键词"的形态（Impact 只有一句 `… unexpected system shutdown`，Description 另有长句），所以被接缝吃掉的比例最高。`shutdown` 丢到 **0** 意味着**拼接口径会得出"全树没有 shutdown"的错误结论**，而这个词恰恰是"重启"组里唯一的非零项。

**同一条结论的镜像**：`persist / persistent / session / lock screen / without / after` 六项丢 **0**——它们都稳定出现在 Impact 字段内部。所以接缝刀不是"普遍低估"，而是**按字段布局偏置**：它专吃"关键词在字段边界"的段。

另外记录一条同类陷阱：`remov-` 词族 219 段里 **Impact 只有 4 段、Description 215 段**。如果按拼接口径统计"移除"，会得出"移除类措辞 219 段"，而真实的**能力句侧只有 4 段**。这与 #116 第 7 章的十字词丢 1,400 段同源，但本篇把它单列出来，因为选题的"维持"的反义词正是"移除"：**措辞层用 Description 写"移除了什么"（215 段），用 Impact 写"什么持续存在"（46 段）——两者不是同一层**。

# 15 与前几篇的集合接口

| 集合 | 定义 | 段 | 与 M 的交集 |
|---|---|---:|---:|
| E | `exploit` 词族（#114 手段全集） | 108 | **0** |
| P | `attacker` 词族（#113 主语全集） | 890 | **8** |
| F | `maliciously crafted`（#116 输入全集） | 1,769 | **0** |
| D_I | `data` 的 Impact（#116） | 1,240 | **0** |
| C_I | `code` 的 Impact（#116） | 363 | **0** |
| TM | `termination`（本篇后果集） | 1,520 | **0** |
| WO_I | `without` 的 Impact（#109 授权缺失集） | 178 | **0** |
| PHY | `physical access` 的 Impact | 142 | **1** |
| LKD | `Lockdown Mode` | 10 | **2** |
| DEL | `delet` 词族 | 53 | **6** |
| AFT | `after` | 150 | **3** |
| IDN | `identifier(s)` | 36 | **36** |

四点解读：

**（1）M 与三条"攻击面"集合全为零交。** 维持集的 57 段里，**没有一段**含 `exploit`、`maliciously crafted`、`data` 或 `code`（Impact 侧）。这与 #116 的结论互为印证：措辞层的"输入侧"与"持续侧"是**两套互不相干的句子**。想从 57 段推出"某种输入能带来持久访问"，在文本层面**没有任何桥梁**。

**（2）M 与授权缺失集也零交。** 178 段 `without` 与 57 段 M 交集 **0**——"未经同意的访问"与"持续的标识符"在措辞层是不同段。（附表 D 给出 178 段的 13 个第一实词闭合：consent 54、authentication 43、prompting 19、permission 14、authorization 10、pairing 7、private 7、confirmation 5、valid 5、app 4、entitlement 4、indicator 3、restrictions 3。）

**（3）与"物理访问"只交 1 段。** `physical access` 的 Impact 有 142 段，M 只交 **1 段**（P5：`An attacker with physical access may be able to silently persist an Apple Account on an erased device`）。这是全树唯一一句把"物理接触"与"抹掉后仍驻留"写在同一句里的段落。

**（4）IDN ⊂ M，不是 M ⊂ IDN。** 交集 36 恰等于 `identifier` 全集，所以**包含方向是"标识符集被 M 包含"**。写"M 的 63.2% 是标识符"是对的，写"标识符的 100% 是维持句"虽然本例成立，但必须用成员集合验证后才能写（`|A∩B| = |B|` 才是包含，`|A|` 与 `|B|` 的关系推不出来）。

# 16 版式域、页与组件分布

**M 完全在域 A**：域 A 57 段、**域 B 0 段**——11 份粗体版式页面（91 段）里没有任何 persist/retain/still/across/erased 判据。

**致谢字段里的 `until` 是切块自检信号。** `until` 全字段 11 段，**Impact 0、致谢 11**，逐段实测**全部**是同一句页面级免责声明：`For our customers' protection, Apple doesn't disclose, discuss, or confirm security issues until an investigation has occurred and patches or releases are available.`。这 11 段的页号是 **118479、120303、120304、120305、120307、120308、120309、120310、120311、120312、120339**——**恰好是域 B 的 11 份页面，每页 1 段**。域 A 的四锚点收块把这段免责文本切在块外，域 B 的粗体版式没有对应锚点，于是免责声明落进了致谢字段。**所以：措辞层统计里的连词必须做"字段归属"检查；`until` 的 11 段不是语料事实，是切块残留。** 同一现象也解释 `firmware` 的致谢 3 段（"patches or releases"族）与 `pairing` 词族的致谢 3 段。

**M 的 14 个块名分布**（合计 57）：`AuthKit` **8**、`DeviceCheck` **8**、`Mail` **7**、`App Store` **6**、`Sandbox Profiles` **6**、`Notification Services` **5**、`CloudKit` **4**、`Safari` **3**、`iCloud` **3**、`Managed Configuration` **2**、`Siri` **2**、`Call History` **1**、`Find My` **1**、`Game Center` **1**。

**M 的 34 页 Top**：`149034` **5**、`149038` **5**、`149035` **4**、`149036` **4**、`149037` **4**、`149042` **3**、`149041` **2**、`149043` **2**、`125632` **2**、`125633` **2**，其余 24 页各 1 段。**149034–149038 这 5 页合计 22 段 = M 的 38.6%**——五个版本页贡献了近四成的维持句，这是"版本页重复公告"的形态（同一句式、同一组件在多个 OS 版本页各列一段），不能读成"22 个不同的持久问题"。

**读法警告**：M 有 57 段，但 distinct 句只有 **11 条**、页 **34** 个、组件 **14** 个。三个口径任何两个都不相等；引用时必须同时给 `|段|`、`|页|`、`|名|`、`|distinct 句|` 四个数，否则 57 会被读成 57 件事。

# 17 中文手册层（T1）的对应词

178 页可用正文域，字面计数（次数／页数）：

| 中文词 | 次 | 页 | | 中文词 | 次 | 页 |
|---|---:|---:|---|---|---:|---:|
| 重启 | 41 | 20 | | 标识符 | 53 | 26 |
| **重新启动** | **0** | **0** | | 永久标识符 | 2 | 2 |
| 重启后 | 7 | 7 | | 轮替 | 4 | 3 |
| 会话 | 78 | 20 | | 抹掉 | 44 | 18 |
| 会话结束 | 3 | 2 | | 擦除 | 229 | 178 |
| **退出会话** | **0** | **0** | | 清除 | 187 | 178 |
| **持久** | **0** | **0** | | 卸载 | 3 | 3 |
| **残留** | **0** | **0** | | 重新安装 | 3 | 2 |
| **依旧 / 依然** | **0** | **0** | | 恢复模式 | 16 | 10 |
| 仍然 | 6 | 6 | | DFU | 16 | 7 |
| 维持 | 3 | 3 | | 安全模式 | 6 | 4 |
| 保持 | 31 | 31 | | 锁屏 | 6 | 4 |
| 继续 | 29 | 24 | | 解锁 | 384 | 178 |
| **后门 / 越狱 / 常驻** | **0** | **0** | | 退出登录 | 18 | 9 |
| **上电 / 掉电 / 注销 / 恢复出厂 / 重装** | **0** | **0** | | 首次解锁 | 16 | 4 |
| 安全会话 | 8 | 5 | | 无需额外认证 | 1 | 1 |
| 计时器 / 定时器 | 2 / 4 | 2 / 2 | | | | |

**手册层与顾问页的分工是相反的**：顾问页 `reboot = 0`、`session = 4`，手册页 `重启 = 41 次/20 页`、`会话 = 78 次/20 页`；顾问页 `重启后 = 0`，手册页 `重启后 = 7 次/7 页`。

`重启后` 的 7 处**全部是机制陈述**，无一处指攻击后果（±14/±22 字窗口实测摘录）：

- 「在设备**启动、重启或退出登录**（在 Mac 上）后**仍然需要密码**。」（sec067eb0c9e）——这是全树唯一把"重启后仍 X"写成**要求**的句子，且 X = 需要密码（防御方向）。
- 「如果设备在定时延迟期间**重启**，延迟**仍然执行**，且定时器从当期重新计时。」（sec20230a10d）
- 「用户成功登录 Mac 后不会执行，但会在**重启后**重新执行。」（sec20230a10d）
- 「这就是**重启后**，用户首次使用"访达"或 iTunes 进行备份之前必须输入密码的原因。」（sec6483d5760）
- 「**重启后**可通过 SSH 解锁文件保险箱」（前提："远程登录"已打开）（sec8447f5049）
- 「**重启后保留内容**。」（seca7ff718d2，Developer Mode）
- 「设备**重启后**，视控 ID／面容 ID／触控 ID 用来解锁设备……」（sec5227ff3c）

`首次解锁` 16 次/4 页，绝大多数来自一张数据保护等级表（secb0694df1a：「iCloud 令牌 首次解锁后」「邮件账户 首次解锁后」「VPN 密码 首次解锁后」…）——**手册层用"首次解锁后"表达数据类的可用边界**，这是选题"边界"最接近的公开对应物，但它在**手册层**，不在顾问页。

`会话` 侧：「Apple Watch **重启后**用于引导新**会话**」（sec6ab47ebfc）、「**会话结束后**，iPhone 首次解锁时会显示一条横幅」（sec6ab47ebfc）、「临时**会话**结束后便无法继续同步数据」（secd99f373ef）、「每台设备会在每个**会话**中**轮替标识符**以防止关联」（sec18a3be52a）、「无法通过**永久标识符**跟踪用户」（sec6cbc80fd0）——**"标识符轮替以防跟踪"是手册层对第 9 章 36 段 persistent identifier 的正向回答**。

`维持` 的 3 处也全是机制：「可**维持**与 IdP 的受信任连接而不受用户影响」（sec0c87ccc6d）、「付款卡数据会**维持**加密和认证状态，直到其到达付款服务提供商处」（sec72cb155f4）、「**维持**操作系统完整性」（secc7d85209d）。`安全会话`／`无需额外认证`：「安全**会话**打开时，用户**无需额外认证**即可查看或保护其他备忘录。但是，安全会话仅适用于使用密码短语进行保护的备忘录，对于使用其他密码短语保护的备忘录，用户仍需要认证」（sec1782bcab1）——**这是全树唯一一处公开中文文本明确写"某种访问在某个状态期间无需认证即可继续"**，而它是备忘录安全会话的设计说明，不是缺陷公告。

**两个方向**：手册层写边界（重启后 7、首次解锁 16、会话结束 3）但不写"持久"（0）；顾问页写"持久"（40 段 `persistent`）但不写边界（重启 0、会话结束 0）。**"跨边界维持"这句话在中文层与英文层各有一半，任何一层单独都不成立。**

# 18 验证与断言

本篇全部数由三个自测脚本产出，跑完必须打印下述行，否则视为失败：

- `_tmp/tl117tab.pl` → `附表 A 行=64（全零26/非零38） 附表 B 行=10 附表 C 行=22 附表 D 行=14`，内含五条断言：`rows==64`、`zeros==26`、`nonz==38`、九分区**未归类段 = 0** 且 `sum==57`、`附表C rows==22`、`附表D sum==178` 且 `词数==13`。任何一条不满足即 `die`，附表不落地。
- `_tmp/tl117fin.pl` → 分母（9,136／257／472／7,434／7,434／9,132；域 A 9,045、域 B 91）、M（57／34／14，M_I 差 0，域 A 57／域 B 0，distinct 句 11）、`persistent` 右邻 distinct=3 且 18＋15＋7 = 40、`termination` 修饰词 6 项合计 1,520、`be able to` 4,723／80 动词、12 项集合接口、接缝刀十词、原始 HTML 十个词全 0（文件数 257）。
- `_tmp/tl117sent.pl` → 11 句逐条打印、段数合计 57；`_tmp/tl117sub.pl` → 施事 9 类合计 57；`_tmp/tl117x.pl` → M 与 16 个集合的交集与 `until` 11 段逐页清单（页号必须等于域 B 的 11 个页号）；`_tmp/tl117t1.pl`、`_tmp/tl117t1b.pl` → T1 词表（178 页）。

本轮修掉的两类脚本缺陷：

**（1）`${S}` 旁的字面空格。** `"Lockdown$S Mode"`、`"across$S reinstalls"`、`"be$S able$S to"`、`"maliciously$S crafted"`、`"lead$S to$S…"` 这类写法要求**两个空格**，把命中打到 0。初测因此得到 M = 51（应 57）、`be able to` = 0（应 4,723）、`physical access` = 0（应 142）、`lock 词族` 与 `Lockdown` 的 M 交集各 0（应 4 与 2）。修正为 `foo${S}bar`（**紧邻、无字面空格**）后重测。

**（2）附表 B 的两条判据表达式过紧。** `persist.$S.erased.$S.device` 与 `identify.$S.across…` 要求字符紧邻，导致 4 段落为未归类（7405、7760、8011、8511）；放宽为 `persist(?![\w-]).{0,60}?erased${S}device` 与 `identify.{0,30}?across${S}reinstalls` 后九分区闭合到 57、未归类归零。

**（3）附表 D 的第一实词口径。** 旧口径 `without (?:user |the |any |having )*(\w+)` 得到 12 词但把 `without an indicator` 记成 `an`；新口径跳过分词集合 `{user,the,any,having,an,a}` 后得到 13 词、合计仍 **178 闭合**（新增 `entitlement` 4、`indicator` 3，`an` 归零）。**口径变更会让"几个词"这一计数变化，但不会让"几段"变化——引用时必须绑定口径。**

其余一致性检查：五组判据的段数与词形和（40＋4＋2 = 46；九分区 33＋7＋2＋3＋1＋5＋3＋2＋1 = 57；施事 18＋18＋7＋5＋3＋2＋2＋1＋1 = 57；情态 44＋5＋8 = 57；附表 D 13 项 = 178；`termination` 6 项 = 1,520；`persistent` 右邻 3 项 = 40）**全部闭合，差 0**。

# 19 复算步骤

1. 装载：`require "./_tmp/tl111load.pl"; my %B = load_blocks();` 得 9,136 段，逐段 `{imp,des,ack,nm,id}`。
2. 定义 `$S='[ \x{a0}\x{202f}]'`；所有词组判据写 `foo${S}bar`，`${S}` 两侧不留字面空格。
3. 并集口径：只允许 `MK`（Impact∪Description∪致谢 分别匹配后取并集）；**禁止**三字段拼接后匹配。若要用拼接口径，必须与并集口径同表并报出差值（第 14 章）。
4. 附表：`perl _tmp/tl117tab.pl` → 写 `_tmp/tl117{A,B,C,D}.md`；断言失败即中止。
5. 主集合：`$FILT = "persist|retain|across${S}reinstalls|may${S}still|erased${S}device"`；`M = MK("(?<![\w-])(?:$FILT)")`。
6. 句子级：`SE($B{$k}{imp})` 按 `(?<=[.!?])\s+(?=[A-Z])` 切句，只保留含判据的句子，`D()` 归一空白后去重（得 11 句）。
7. 右邻／左邻词表：对命中句做 `while ($s =~ /pat${S}(\w+)/gi)` 累加，并检查合计是否等于段数（闭合判据）。
8. 原始层复核：对 `_tmp/adv2/*.html` 257 份文件做全文 `index`/`qr//i` 计数，确认零命中词的零是文本级。
9. 接口：对每个对照集合做成员级 `I2`（交集计数），并同时报 `|A|`、`|B|`、`|A∩B|`。
10. 中文层：`_tmp/sec*.txt`（排除 `sec2727afbbc`）178 页，`Encode::decode("utf-8",$raw,1)` 后按字面词计数，同时报次数与页数；窗口摘录 ±14/±22 字。
11. 闸门：`perl _hbcheck.pl "iOS设备重启或会话结束后仍维持的非预期访问能力.md"` 必须 `RESULT=PASS`，且附表行数为 **64 / 10 / 22 / 14 / 8**。

# 20 缺口清单

1. **重启后状态的公开文本不存在**：`reboot / restart / power / booting / startup / relaunch / recovery / DFU / safe mode` 在 257 份镜像里 **0 次**（原始层）。要谈重启后状态只能引手册层（`重启后` 7 次/7 页），而手册层的 7 处全为机制陈述。
2. **会话结束不可测**：`session` 只有 4 段且全在 Description 模板里；`end of session`、`sessions`、`log out` **0**；`termination` 全部是后果。**公开措辞层不提供"会话生命周期"这一维度**。
3. **"访问能力"的维持无句式**：`persistent` 右邻闭合到 3 个非能力名词；14 项"能力"宾语全 0；`M ∩ access（be able to access）= 0`。
4. **`persistence` 名词为 0**：无法统计"持久性"作为主题的分布。
5. **组件名不等于实现组件**：M 的 14 个块名是公告条目名（AuthKit、DeviceCheck 等），不指向任何已实现的模块边界；本篇不做组件—机制映射。
6. **版本页重复放大段数**：149034–149038 五页贡献 22 段 = M 的 38.6%，distinct 句只有若干条；段数与"事件数"之间没有换算。
7. **域 B 的字段污染**：11 段致谢含页面免责声明（`until`、部分 `firmware`/`pairing`），致谢侧连词计数需按域 B 逐页扣除；扣除后 `until` 的真实段数为 **0**。
8. **无 CVE 级归并**：本篇只给页/名/段三级口径，未做 CVE→段的一对多归并表；如需"多少个不同缺陷涉及持久化"，须另按 CVE 字段聚类。
9. **中文层词表有限**：T1 只测 37 个词（含零词表 14 个），未覆盖"驻留／留存／遗留／失效期"等近义表达。

# 21 常见误读纠正

**误读一："顾问页大量描述重启后的残留状态。"** 纠正：`reboot / restart / recovery / DFU` **各 0 段**（且原始 HTML 层 0 次）。非零的 `shutdown` 16 段是 `unexpected system shutdown`（后果），`boot` 1 段是 `during boot`（介词宾语）。

**误读二："`termination` 1,520 段说明文本关注终止之后的状态。"** 纠正：1,520 段的修饰词只有 system/app/process/application/unexpected/tool 六个，`lead to … termination` 572 段 = 37.6%，**全部是后果**；`M ∩ termination = 0`；57 段维持句里**没有一段**与重启类词同段。

**误读三："`session` 出现 4 次，说明会话管理是缺陷类型之一。"** 纠正为半对半错：4 段的 Description 是**同一句**模板（distinct = 1），Impact 侧 **0**；它们分属蓝牙配对密钥提取与 Voice Control 转写两个后果，**措辞没有描述会话本身**。

**误读四："57 段维持句＝57 个持久化问题。"** 纠正：57 段对应 **11 条 distinct 句 / 34 页 / 14 个组件名**；149034–149038 五页就占 22 段。四个口径数必须一起给。

**误读五："`persistent identifier` 是公开措辞。"** 纠正：相邻形式 `persistent identifier` = **0**；实际写法是 `persistent device identifier`（15）与 `persistent account identifier`（18），中间必须有 `device`/`account`。同时 `permanent identifier` / `unique identifier` / `UUID` / `UDID` 在英文措辞层**各 0**（`永久标识符` 只在中文手册层出现 2 次/2 页）。

**误读六："`across` 出现 6 次，说明文本讨论跨重启。"** 纠正：6 段的宾语只有 `reinstalls`（3）与 `apps and websites`（3）；`across reboots / across sessions` 为 0。

**误读七："设备被抹掉后的持久访问有 4 段记载。"** 纠正：`erased` 词界 **1 段**（Find My，页 128072），`on an erased device` 与 `across reinstalls`（3 段，iCloud）合计 **4 段**，且这 4 段是全树唯一带设备级边界的维持句。

**误读八："`without` 的 178 段说明攻击者常能在无授权下维持访问。"** 纠正：`M ∩ without = 0`；178 段的 13 个第一实词里 consent（54）与 authentication（43）占 54.5%，且这些句子**没有一句含 persist/retain/still**。

**误读九：致谢里的 `until` 表示"补丁发布前"。** 纠正：11 段 `until` 全部在致谢字段、页号恰为域 B 的 11 页，是**页面级免责声明的切块残留**；域 A 里 `until` 的真实段数为 0。

**误读十：把 `remov-` 的 219 段读成"219 处访问被移除"。** 纠正：其中 **Impact 4 段、Description 215 段**，后者是补丁说明用语（`This update removes …`），与访问能力无关。

# 22 结论

选题问的是"重启或会话结束后仍维持的非预期访问能力"。在 9,136 段公开措辞里，这个问题的**两个时间前提各为 0 与 4**（重启组九形 0 段；会话 4 段且全为模板），**状态词只剩一个形容词**（`persistent` 40 段），**它的宾语只有三个词**（account 18、device 15、denial 7，闭合到 40），**被维持的东西里没有一样是访问能力**（14 项候选宾语全 0，`M ∩ access = 0`）。

真正的跨边界陈述只有 **4 段**：`identify a user across reinstalls`（3 段，iCloud）与 `silently persist an Apple Account on an erased device`（1 段，Find My）。加上弱边界的 11 段（`after navigation` 3、`retained on the device` 5、`may still` 3），有边界陈述共 **15 段 = 26.3%**；其余 **42 段 = 73.7%** 不带任何时间或状态边界——**"persistent" 在本语料里描述的是一种属性，不是一段寿命**。

于是本篇的方法论结论有三条。**其一，边界不可借词**：`shutdown`、`termination`、`remov-` 这类"看起来像边界"的词，字段位置与句法角色都对不上（后果位、补丁说明位），把它们当时间前提会产生 1,520 段的假证据。**其二，零必须在原文层证明**：`reboot` 的 0 是切块后的 0，也是 257 份 HTML 全文扫描的 0——两种口径都给出 0，才可以说"公开文本不写"。**其三，字段并集是唯一安全口径**：`shutdown` 在拼接口径下丢到 0、`identifier` 丢 33、`termination` 丢 930，四个词共丢 980 段——**一个用拼接口径统计的脚本会得出"顾问页从不写 shutdown"的结论，而这句话恰好是错的**。

最后一条留给选题本身：**"仍维持"这件事在 Apple 的公开缺陷文本里不是攻击者的能力，而是标识符的属性**。要读到"跨边界的可用状态"，必须换层——手册层的 `重启后`（7 次/7 页）、`首次解锁后`（16 次/4 页）、`会话结束后`（3 次/2 页）、`轮替标识符`（4 次/3 页）、`安全会话…无需额外认证`（1 次/1 页）——而那一层的每一句都是**设计说明**，不是**缺陷公告**。两层的分工本身，就是这篇文本计量要交给读者的结论。

# 附表 A 重启、会话、维持、边界连词与锁态五组判据电池（64 行）

| 组 | 判据（词界/词组） | 全字段并集 | Impact | Description | 致谢 | 页 | 名 | 命中次数 |
|---|---|---:|---:|---:|---:|---:|---:|---:|
| 重启 | `reboot` 词族 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 重启 | `restar-`（restart/restarting/restarted） | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 重启 | `power`（power cycle/power off） | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 重启 | `boot`（裸形） | 1 | 1 | 0 | 0 | 1 | 1 | 1 |
| 重启 | `booting` / `boot-up` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 重启 | `startup` / `starting up` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 重启 | `shutdown` | 16 | 16 | 0 | 0 | 12 | 2 | 16 |
| 重启 | `relaunch` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 重启 | `recovery`（恢复模式） | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 重启 | `DFU` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 重启 | `safe mode` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 重启 | `firmware` | 6 | 3 | 0 | 3 | 6 | 2 | 14 |
| 会话 | `session` | 4 | 0 | 4 | 0 | 4 | 2 | 4 |
| 会话 | `sessions` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 会话 | `log in` / `log out` / `logout` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 会话 | `sign in/out/on/off` | 5 | 5 | 0 | 0 | 5 | 1 | 5 |
| 会话 | `disconnect` | 3 | 3 | 0 | 0 | 3 | 1 | 3 |
| 会话 | `terminate` | 26 | 26 | 0 | 0 | 26 | 4 | 26 |
| 会话 | `termination` | 1520 | 1520 | 0 | 0 | 165 | 101 | 1520 |
| 会话 | `end of session` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 维持 | `persist`（动词） | 4 | 4 | 0 | 0 | 4 | 2 | 4 |
| 维持 | `persistent`（形容词） | 40 | 40 | 0 | 0 | 22 | 7 | 40 |
| 维持 | `persistently`（副词） | 2 | 2 | 0 | 0 | 2 | 1 | 2 |
| 维持 | `persistence`（名词） | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 维持 | `remain(s/ing)` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 维持 | `survive` 词族 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 维持 | `retain(s/ed)` | 5 | 5 | 0 | 0 | 5 | 1 | 5 |
| 维持 | `continue` 词族 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 维持 | `keep(s)` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 维持 | `still` | 3 | 3 | 0 | 0 | 3 | 2 | 3 |
| 维持 | `yet` | 1 | 1 | 0 | 0 | 1 | 1 | 1 |
| 维持 | `ongoing` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 维持 | `uninstall` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 维持 | `remov-` | 219 | 4 | 215 | 0 | 110 | 58 | 219 |
| 维持 | `reset` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 维持 | `eras-` | 1 | 1 | 0 | 0 | 1 | 1 | 1 |
| 维持 | `restor-` | 18 | 18 | 0 | 0 | 13 | 4 | 18 |
| 维持 | `reinstall` | 3 | 3 | 0 | 0 | 3 | 1 | 3 |
| 维持 | `install-` | 68 | 67 | 0 | 1 | 51 | 14 | 68 |
| 边界连词 | `after` | 150 | 22 | 127 | 1 | 66 | 19 | 150 |
| 边界连词 | `even`（让步） | 11 | 11 | 0 | 0 | 11 | 3 | 11 |
| 边界连词 | `even after` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 边界连词 | `even when` | 8 | 8 | 0 | 0 | 8 | 2 | 8 |
| 边界连词 | `even with` | 3 | 3 | 0 | 0 | 3 | 1 | 3 |
| 边界连词 | `despite` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 边界连词 | `regardless` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 边界连词 | `following` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 边界连词 | `subsequent` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 边界连词 | `across` | 6 | 6 | 0 | 0 | 5 | 2 | 6 |
| 边界连词 | `without` | 189 | 178 | 0 | 11 | 105 | 39 | 189 |
| 边界连词 | `until` | 11 | 0 | 0 | 11 | 11 | 6 | 11 |
| 边界连词 | `once` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 边界连词 | `during` | 8 | 8 | 0 | 0 | 8 | 6 | 8 |
| 边界连词 | `previous(ly)` | 2 | 2 | 0 | 0 | 2 | 2 | 2 |
| 锁态 | `lock`（裸形） | 78 | 62 | 16 | 0 | 50 | 19 | 78 |
| 锁态 | `locked` | 100 | 72 | 33 | 0 | 60 | 24 | 106 |
| 锁态 | `locking` | 60 | 0 | 60 | 0 | 53 | 9 | 60 |
| 锁态 | `unlock(-ed)` | 14 | 14 | 0 | 0 | 14 | 8 | 14 |
| 锁态 | `lock screen` | 66 | 58 | 8 | 0 | 44 | 17 | 66 |
| 锁态 | `Lockdown Mode` | 10 | 10 | 0 | 0 | 10 | 4 | 10 |
| 锁态 | `passcode` | 10 | 10 | 0 | 0 | 10 | 4 | 10 |
| 锁态 | `Face ID` / `Touch ID` | 1 | 1 | 0 | 0 | 1 | 1 | 1 |
| 锁态 | `password` 词界 | 20 | 20 | 0 | 0 | 19 | 6 | 20 |
| 锁态 | `authenticat-` | 144 | 98 | 80 | 0 | 84 | 38 | 178 |

# 附表 B 维持集 M 的九分区（10 行）

| 类 | 判据（Impact 句内首次命中，按表序） | 段 | 页 | 名 | 情态框架 |
|---|---|---:|---:|---:|---|
| P1 | persistent＋identifier | 33 | 15 | 6 | may be able to ＋ 动词 |
| P2 | persistent＋denial-of-service | 7 | 7 | 1 | may be able to ＋ 动词 |
| P3 | persistently＋fail to lock | 2 | 2 | 1 | may ＋ 动词（无 able） |
| P4 | persist（动词）＋after navigation | 3 | 3 | 1 | may ＋ 动词（无 able） |
| P5 | persist（动词）＋on an erased device | 1 | 1 | 1 | may be able to ＋ 动词 |
| P6 | retained＋on the device | 5 | 5 | 1 | could be ＋ 过去分词 |
| P7 | identify＋across reinstalls | 3 | 3 | 1 | may be able to ＋ 动词 |
| P8 | may still＋Lockdown Mode turned on | 2 | 2 | 1 | may ＋ 动词（无 able） |
| P9 | may still＋from deleted apps | 1 | 1 | 1 | may ＋ 动词（无 able） |
| **合计** | P1–P9 九类互斥并集 = 维持集 M | **57** | **34** | **14** | 3 种情态框架 |

# 附表 C 锁态与重启邻近机制矩阵（22 行）

| 词 / 词组 | 全字段并集 | Impact | Description | 致谢 | 页 | 名 |
|---|---:|---:|---:|---:|---:|---:|
| `lock`（裸形） | 78 | 62 | 16 | 0 | 50 | 19 |
| `locked` | 100 | 72 | 33 | 0 | 60 | 24 |
| `locking` | 60 | 0 | 60 | 0 | 53 | 9 |
| `unlock` 词族 | 14 | 14 | 0 | 0 | 14 | 8 |
| `lock screen` | 66 | 58 | 8 | 0 | 44 | 17 |
| `locked device` | 89 | 61 | 33 | 0 | 50 | 19 |
| `unlocked device` | 11 | 11 | 0 | 0 | 11 | 6 |
| `unlocked`（词界） | 13 | 13 | 0 | 0 | 13 | 7 |
| `on a locked` | 48 | 18 | 33 | 0 | 34 | 10 |
| `on an unlocked` | 0 | 0 | 0 | 0 | 0 | 0 |
| `fail to lock` | 2 | 2 | 0 | 0 | 2 | 1 |
| `Lockdown Mode` | 10 | 10 | 0 | 0 | 10 | 4 |
| `passcode` | 10 | 10 | 0 | 0 | 10 | 4 |
| `password` | 20 | 20 | 0 | 0 | 19 | 6 |
| `authentication` 词族 | 144 | 98 | 80 | 0 | 84 | 38 |
| `pairing` 词族 | 22 | 22 | 0 | 3 | 22 | 4 |
| `USB Restricted Mode` | 5 | 5 | 0 | 0 | 5 | 2 |
| `Login Window` | 2 | 2 | 0 | 0 | 1 | 1 |
| `FileVault` | 1 | 1 | 0 | 0 | 1 | 1 |
| `backup` | 21 | 21 | 0 | 0 | 16 | 5 |
| `restore` 词族 | 18 | 18 | 0 | 0 | 13 | 4 |
| `erased` | 1 | 1 | 0 | 0 | 1 | 1 |

# 附表 D `without`（Impact 178 段）第一实词分布（14 行）

| without 之后的第一实词 | 段 | 代表句（截取） |
|---|---:|---|
| `consent` | 54 | A shortcut may output sensitive user data without consent |
| `authentication` | 43 | Private Browsing tabs may be accessed without authentication |
| `prompting` | 19 | A shortcut may be able to use sensitive data with certain actions without prompting the us |
| `permission` | 14 | An app may be able to access Bluetooth-connected microphones without user permission |
| `authorization` | 10 | A maliciously crafted email may be able to initiate FaceTime calls without user authorizat |
| `pairing` | 7 | An unauthenticated user on the same network as a signed-in Mac could send it AirPlay comma |
| `private` | 7 | An attacker may be able to decrypt legacy RSA PKCS#1 v1.5 ciphertexts without having the p |
| `confirmation` | 5 | A malicious shortcut may be able to send messages without user confirmation |
| `valid` | 5 | An attacker on the network may be able to authenticate to Screen Sharing without valid cre |
| `app` | 4 | A malicious app may be able to modify other apps without having App Management permission |
| `entitlement` | 4 | An app may be able to retrieve Safari bookmarks without an entitlement check |
| `indicator` | 3 | An app may be able to record the screen without an indicator |
| `restrictions` | 3 | Apps that appear to use App Sandbox may be able to launch without restrictions |
| **合计** | **178** | = without 的 Impact 段总数（13 个第一实词闭合） |

# 附表 E 错误写法与纠正对照（8 行）

| 错误写法 | 问题 | 纠正后的说法 |
|---|---|---|
| "顾问页写了设备重启后的残留能力" | `reboot / restar- / power / booting / startup / relaunch / recovery / DFU / safe mode` **各 0 段**，且 257 份 HTML 全文扫描 **0 次** | "重启组 12 项判据 9 项全零；非零只有 `shutdown` 16 段（后果）、`firmware` 6 段、`boot` 1 段（`during boot`）" |
| "`termination` 1,520 段说明文本关心终止之后" | 六个修饰词（system 814／app 612／process 63／application 26／unexpected 4／tool 1）全部是**被终止的宿主**；`lead to …` 572 段 = 37.6% | "1,520 段全是'导致意外终止'的后果句式，**M ∩ termination = 0**，0 段把终止写成时间前提" |
| "`session` 出现 4 次 ⇒ 会话是缺陷维度" | 4 段 **Impact 侧 0**、Description 4 段是**同一句**模板（distinct = 1） | "`session` 只作类型标签出现（蓝牙配件 1 页、Voice Control 3 页），`end of session / sessions / log out` 各 **0**" |
| "有 57 个跨重启／跨会话的持久问题" | 57 段 = **11 条 distinct 句 / 34 页 / 14 名**，且 149034–149038 五页占 22 段 | "57 段维持句对应 11 条句子；其中 42 段（73.7%）**没有任何边界**，设备级边界只有 4 段" |
| "`persistent identifier` 是公开措辞" | 相邻形式 **0**；`permanent / unique identifier / UUID / UDID` 在英文措辞层 **各 0** | "实际写法必须经中介：`persistent device identifier` 15 段、`persistent account identifier` 18 段；`persistent` 右邻 distinct = 3" |
| "措辞层写了'维持的访问能力'" | `persistent access / ability / permission / privileges / entitlement / capability / control / session / login / unauthorized / root / administrator / tracking / presence` **14 项全 0**；`M ∩ be able to access = 0` | "M 内 `be able to` 动词只有 4 个：read 33、cause 7、identify 3、silently 1（合计 44 = `may be able to` 框架段数）" |
| "`across` 6 次涉及跨重启" | 6 段宾语只有 `reinstalls`（3）与 `apps and websites`（3），两句交集 **0** | "`across reinstalls` 3 段（iCloud）是全树唯一应用级生命周期跨越；`across reboots / across sessions` **0**" |
| "致谢里 `until` 11 段说明公告有发布时间约束" | 11 段**全部在致谢字段**，页号恰为域 B 的 11 份页面，是页面免责声明落进字段的切块残留 | "域 A 的 `until` 真实段数 = **0**；连词统计必须做字段归属检查" |

---

**边界声明：** 本篇全部内容是对 Apple 公开安全响应顾问页英文镜像（257 份、9,136 段，两个版式域）与《Apple 平台安全保护》中文指南转储（178 页可用正文域）的词形分布、字段归属、句法位置、右邻与左邻搭配、集合交并、双口径计数差异与零命中词表的**文本计量分析**，用于说明公开文本如何以词形与连词的选择来表达"某种状态跨过某个边界仍在继续"，以及在哪些维度上它根本不表达边界。本篇不提供任何利用代码、恶意样本、持久化实现方式或参数、清除或恢复痕迹的方法，不含针对任何缺陷的触发步骤、时序、调试路径或绕过访问控制与权限检查的说明；文中出现的组件名、CVE 编号、页号、例句均为对公开文本的引用与计数，"零命中"仅表示公开措辞未写该词，不表示相应机制、事件或状态不存在；各"集合"与"分区"是对公开句子表层形式的分类，不是威胁情报分类，也不是持久化技术或攻击阶段划分。所有百分比与段数只描述本篇镜像与切分口径下的样本，若上游文本更新，全部数值需按第 19 章复算。

Telegram: https://t.me/one00190
