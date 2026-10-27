@@TITLE@@iOS 外部输入、接口与可触达组件的集合
@@SUBTITLE@@选题里的三个名词在 Apple 安全响应顾问页里的待遇完全不同。**外部输入**有据可查但**没有总名**：全树 257 份英文镜像 / **9,136 段**里，`input` 词族在 Impact 侧只有 **38 段 / 26 个块名**，其中 **20 段**是同一句套语 `An input validation issue was addressed`；`untrusted input`、`user input`、`attack surface`、`entry point`、`vector` **各 0 段**。真正被反复点名的是**具体受事名词**：本篇用 75 个输入名词构成词电池，圈出 **3,245 段 / 255 页 / 201 个块名**（占含 Impact 段 **43.7%**），闭合归成 **8 类**，各类之和 **1,153 + 1,107 + 392 + 323 + 49 + 147 + 68 + 6 = 3,245**，未归类 **0 段**。**接口**几乎空白：`protocol` / `endpoint` / `XPC` / `Mach` / `IPC` / `HTML` / `PDF` / `database` / `NFC` / `system call` / `callback` 全为 **0 段**，只有 `interface` **29 段**、`socket` **8 段**、`URL` **7 段**、`API` **5 段**。**可触达组件**没有字段可代理，只能用**块名**：472 个块名里 **201 个**至少有一段含输入名词，**271 个（57.4%）一次都没有**，这 271 名覆盖 **1,942 段 / 173 页**
@@INFO@@报告日期|2026 年 10 月 27 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact**。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做 54 词逐词普查。输入并集全树 3,245 段 / 255 页 / 201 名（域 A 3,210 段 / 244 页 / 199 名，域 B 35 段 / 11 页 / 7 名）
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含针对他人设备的操作步骤、不含任何"如何构造输入以命中某组件"的说明；本篇只做**名词层面的清点**，不判定可达性
@@INFO@@分类|输入面 / **施动者名词与受事名词的口径分离**（把 `an app` 算作输入会让并集从 3,245 涨到 6,613）/ 接口词在措辞层的**大面积缺席** / 组件=块名的**代理定义**及其 57.4% 零命中 / `crafted` 宾语的**截断规则敏感性**
@@INFO@@难度|进阶（第 3 章的施动者排除是全篇前提，不读它会把 2,778 段 `an app may` 当成"App 是一种外部输入"；第 6 章的窗口截断规则决定宾语表长什么样，第 10 章的命中率分母是**块名内段数**而不是全语料）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

选题字面是"外部输入、接口和可触达组件的集合"。这是**攻击面盘点**的三个层次：什么东西能进来、从哪个界面上进来、进来之后碰到的是哪个部件。本篇逐词核对之后发现，这三层在 Apple 公开文本里**只有第一层成规模存在**，而且它不用"输入"这个名字。

四组反差。

**反差一（输入有段、没有类目名）**：措辞层（Apple 安全响应顾问页，下称 T2）全树 9,136 段里，`input` 词族在 Impact 侧只命中 **38 段 / 26 页 / 11 个块名**（Description 侧 730 段）。把这 38 段逐条读完，成分非常单一：**20 段**是 `An input validation issue was addressed`（这句出现在 Impact 栏，是页面 122371–122378 与 125108–125116 的模板异常）；**15 段**是 HID 类输入（`secure input mode`、`keyboard input`、`can input Game Controller events`）；2 段是 `NTLM input`；1 段是 `Processing malicious input`（`QuartzCore`，页 120895）——**全语料唯一一句把"恶意"和"输入"直接连起来的 Impact**。同时 `untrusted` **0 段**、`user input` **0 段**、`untrusted input` **0 段**、`attack surface` **0 段**、`surface` **0 段**、`vector` **0 段**、`entry point` **0 段**、`improper` **0 段**、`unvalidated` **0 段**。也就是说：**T2 不用"输入"这个词分类，它用具体名词。**

**反差二（"集合"能建，但必须自己建）**：既然文本不给类目，本篇自建**输入名词电池**（75 个带词边界的具体名词，第 3 章全列），命中即入并集，得到 **3,245 段 / 255 页 / 201 个块名**——覆盖 257 页里的 **255 页**，占含 Impact 段 **43.7%**、占全树段 **35.5%**。再按固定判序闭合归成 **8 类**，未归类 **0 段**，各类之和恰为 3,245。第 4 章给全表。

**反差三（接口层几乎是零）**：`protocol` **0 段**、`endpoint` **0 段**、`XPC` **0 段**、`Mach` **0 段**、`IPC` **0 段**、`HTML` **0 段**、`CSS` **0 段**、`JavaScript` **0 段**、`PDF` **0 段**、`database` **0 段**、`NFC` **0 段**、`URI` **0 段**、`system call` **0 段**、`callback` **0 段**、`header` **0 段**；非零的只有 `interface` **29 段**、`socket` **8 段**、`URL` **7 段**、`API` **5 段**。对照中文手册层（下称 T1）178 页可用正文："接口" **32 次 / 17 页**、"协议" **83 次 / 37 页**、"组件" **91 次 / 28 页**、"输入" **106 次 / 55 页**、"攻击面" **12 次 / 9 页**、"可达" **0 次**。**接口这个词在 T2 是零，在 T1 也只是一个局部词。**

**反差四（施动者不是输入）**：`app` 是 T2 里最常见的名词，但它是**谁在做事**、不是**什么进来了**。把 `app / application / shortcut / script / extension / daemon / framework / library` 这 8 类施动者名词也算作输入，并集从 **3,245** 涨到 **6,613 段 / 256 页 / 366 名**；多出的 **3,368 段**里 **2,778 段**的开头是同一个 `an app may`。**这是本篇最重要的口径修正**，也是 #111 第 7 章"起点主语"与本篇"输入宾语"必须分表登记的原因。

于是本篇的问题依次是：

**问题一（第 1–2 章）** 三个选题词各能操作成什么。答案：输入 → Impact 里的受事名词；接口 → 只能按词表清点，且清点结果是零；组件 → 块名代理。三者都是**替代定义**，本篇全程标注。

**问题二（第 3–5 章）** 输入并集能不能闭合。能：8 类、未归类 0，但**判序敏感**——325 段（10.0%）同时命中多类，靠第 4 章的固定顺序归属。

**问题三（第 6–8 章）** "外部输入"在句法上长什么样。答案：`maliciously crafted` **1,769 段**（含 `crafted` 者 1,786 段），其后宾语去重后只有 **39 种**，头部 `web content` **793**、`file` **295**、`image` **167** 三者占 **70.6%**；准入动词只有 `processing`（1,769 段）与 `parsing`（146）成规模，而 `viewing` / `rendering` / `accessing` / `clicking` / `importing` / `uploading` / `triggering` / `reaching` / `interacting` **动名词形式各 0 段**。

**问题四（第 9–13 章）** 可触达组件的集合能不能给。能给**代理集合**：61 个段数 ≥30 的组件按输入命中率排序，`AirPlay` / `CoreText` **91.7%**、`ImageIO` **89.1%**、`WebKit` **85.3%** 在顶端，`Kernel` **8.3%**、`Spotlight` **3.5%** 与 **9 个 0.0% 组件**在底端。

# 1 三个选题词的操作性定义（本篇的口径前提）

**定义一（输入）**：一条 Impact 文本里出现**具体受事名词**（可被投递、可被打开、可被解析的对象），即算"该段有一个外部输入"。词表见第 3 章，75 个词项，全部带词边界 `(?<![\w-])…(?![\w-])`。**故意不含施动者名词**（反差四）；**故意不含"输入"这个词本身**——`input` 只作为对照清点（38 段），不参与并集，否则 `An input validation issue was addressed` 这 20 段模板异常会把"校验"当成"输入"。

**定义二（接口）**：T2 没有"接口"字段，也没有 `API`/`endpoint` 级别的叙述。本篇因此**不构造接口并集**，只给逐词清点表（第 9 章）。凡需要"接口"概念处，改引 T1 的"接口 / 协议"页并明确那是**另一层**。

**定义三（组件）**：T2 每条条目以块名分组（`WebKit`、`Kernel`、`ImageIO`……），全树 **472 个**distinct 块名。本篇把块名当作组件的**代理标识**，并明确其局限：块名说明"这条条目归在哪个部件下"，**不说明该部件是否被暴露给外部输入**。第 10 章的命中率就是用来量这个差距的。

**分母声明**：沿用 #111 第 1 章的两版式域。全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact**；域 A **9,045 段 / 246 页 / 470 名 / 7,346**；域 B **91 段 / 11 页 / 26 名 / 88**。本篇默认全树口径，凡与前几期可比处同时给域 A 值。**两域必须先分流、后统计**：只认 `<h3 class="gb-header">` 的解析器会漏掉 11 页，本篇的输入并集里有 **35 段**在域 B（若沿用旧刀，这 35 段连同 `WebKit` 27 段一起消失）。

**字段口径**：所有"段"计数只作用于 **Impact 字段**；"Desc"列为 Description 字段。两栏同一段的措辞是两套语言——Description 讲的是修了什么（`improved validation` 366 段、`sanitized` 93 段），Impact 讲的是输入造成了什么。**本篇的输入并集只从 Impact 构造。**

# 2 结论速览（每条都可按第 16 章脚本复算）

1. 输入并集 **3,245 段 / 255 页 / 201 名**，占含 Impact 段 **43.7%**、占全树段 **35.5%**、覆盖页 **255/257**。
2. **8 类闭合**：网页与浏览器 **1,153**、文件与归档 **1,107**、媒体与字体 **392**、网络与协议面 **323**、消息与通知 **147**、凭据与签名材料 **68**、近场与配件 **49**、代码与可执行体 **6**；之和 **3,245**，未归类 **0**。
3. 施动者名词若算输入，并集变 **6,613 段**（多 **3,368**，其中 **2,778** 段是 `an app may` 起句）。
4. `crafted` 之后宾语：**1,777** 个窗口、**39** 种写法；`web content` 793 + `file` 295 + `image` 167 = **1,255 段（70.6%）**。
5. 准入动词只剩两个：`processing` **1,769 段 / 228 页 / 67 名**，`parsing` **146 段 / 65 页 / 19 名**；`viewing`、`rendering`、`accessing`、`importing`、`uploading`、`clicking`、`triggering`、`reaching`、`interacting`、`syncing` 的**动名词形式全为 0 段**。
6. Impact 首词三分区：冠词 **5,056 段（68.0%）**、动名词 **2,179 段（29.3%）**、其它 **199 段（2.7%）**；第三类去重后 **50 句、每句只出现 1 次**。
7. 接口词：`protocol` / `endpoint` / `XPC` / `Mach` / `IPC` / `HTML` / `PDF` / `database` / `NFC` / `callback` / `system call` 各 **0 段**；`interface` 29、`socket` 8、`URL` 7、`API` 5。
8. 组件代理集合：472 名中 **201 名有输入段**、**271 名零**（覆盖 **1,942 段 / 173 页**）；段数 ≥30 的 61 名里 **5 名 ≥80%**、**29 名 ≤20%**、**9 名恰为 0.0%**。
9. `Kernel` 有 **745 段**，其中只有 **62 段（8.3%）**含输入名词；这 62 段的输入词是 `network` 46、`file` 13、`packets` 8、`bluetooth` 7、`passcode` 7、`vpn` 6、`disk image` 5。
10. 输入并集 ∩ #111 越级并集（602 段）= **124 段**（3.8% / 602 的 20.6%）；∩ `bypass` 词族（全树 291 段，域 A 288 与 #110 一致）= **72 段**。`kernel privileges` 79 段与输入并集**交集为 0**。
11. 通道词 `via` 在全树只有 **23 个窗口 / 5 种写法**（`icloud link` 9、`incorrect connection` 8、`accessibility features` 3、`file url` 2、`email` 1）——**"从哪条路进来"在措辞层几乎不成表**，这是 #113 的入口。
12. T1 层：`输入` 106 次 / 55 页、`接口` 32 / 17、`组件` 91 / 28、`攻击面` 12 / 9、`可达` **0**、`字体` **0**、`图片` **0**；"输入"最集中的两页是《Tap to Pay on iPhone 安全性》(11) 与《用于数据保护的密钥包》(7)，抽查原句后确认**两页的"输入"都不是恶意对象**（分别是 PIN 码输入、输入密码）。

# 3 输入名词电池：75 个词项与 22 个零

电池按八类的字段顺序排列，逐词给出 Impact 侧**段 / 页 / 名**（同一句常含两个以上词项，**本表纵向不可相加**，相加会重复计数；并集按段去重后为 3,245）。

| 词项 | 段 | 页 | 名 | 词项 | 段 | 页 | 名 |
|---|---|---|---|---|---|---|---|
| `web content` | 895 | 167 | 21 | `network(s)` | 290 | 107 | 35 |
| `website` | 222 | 105 | 10 | `packet(s)` | 20 | 20 | 3 |
| `webpage` | 38 | 27 | 4 | `frame(s)` | 10 | 10 | 2 |
| `iframe` | 17 | 17 | 2 | `smb` | 27 | 12 | 1 |
| `browser` | 2 | 2 | 1 | `afp` | 5 | 3 | 1 |
| `safari` | 226 | 79 | 10 | `ntlm` | 2 | 2 | 1 |
| `file(s)` | 1,058 | 172 | 105 | `wi-fi` | 10 | 10 | 1 |
| `archive(s)` | 29 | 17 | 6 | `wireless` | 8 | 8 | 1 |
| `zip` | 10 | 4 | 3 | `proxy` | 6 | 6 | 1 |
| `disk image` | 29 | 13 | 5 | `vpn` | 6 | 6 | 1 |
| `document(s)` | 10 | 9 | 6 | `bluetooth` | 32 | 24 | 4 |
| `keynote` | 4 | 3 | 3 | `usb` | 5 | 5 | 2 |
| `webarchive` | 4 | 4 | 1 | `airplay` | 7 | 7 | 1 |
| `usd` | 5 | 5 | 1 | `airdrop` | 3 | 3 | 1 |
| `backup` | 21 | 16 | 5 | `pairing` / `paired` | 9 / 13 | — | — |
| `image(s)` | 302 | 134 | 19 | `accessory/ies` | 14 | 14 | 3 |
| `photo(s)` | 39 | 31 | 11 | `message(s)` | 40 | 39 | 9 |
| `video(s)` | 83 | 67 | 8 | `notification(s)` | 32 | 28 | 9 |
| `audio` | 59 | 51 | 6 | `email` | 31 | 26 | 6 |
| `media` | 102 | 74 | 7 | `attachment(s)` | 1 | 1 | 1 |
| `font(s)` | 54 | 40 | 3 | `contact(s)` | 75 | 46 | 18 |
| `texture(s)` | 18 | 15 | 3 | `facetime` | 20 | 15 | 4 |
| `model(s)` | 28 | 12 | 2 | `certificate(s)` | 8 | 8 | 1 |
| `binary/ies` | 6 | 5 | 4 | `token(s)` | 4 | 4 | 1 |
| `jar` | 3 | 3 | 1 | `passcode(s)` | 10 | 10 | 4 |
| `password(s)` | 23 | 21 | 7 | `credential(s)` | 29 | 28 | 8 |
| `keystroke(s)` | 13 | 12 | 6 | — | — | — | — |

**22 个词项 0 段**：`tar`、`bundle`、`docx`、`clip(s)`、`bitmap`、`802.1X`、`802.11`、`nfc`、`lightning`、`thunderbolt`、`mfi`、`handoff`、`continuity`、`carplay`、`homekit`、`imessage`、`crl`、`ocsp`、`ticket(s)`、`executable(s)`、`plug-in(s)`、`kext(s)`。**这 22 个"选题想问而语料不答"的词，比并集本身更能说明 T2 的粒度**：Apple 不写"配件类输入"，只写 `MobileAccessoryUpdater` 那 12 段具体条目（第 4 章 I5）。

三点读法。

**（1）头部只有两个词承担了一半。** `file(s)` 1,058 段与 `web content` 895 段之和 **1,953**，占并集 **60.2%**——且这两词各自覆盖 **172 页 / 167 页**，接近全语料页数。**"外部输入"在 T2 基本就是"一个文件"或"网页内容"两句话。**

**（2）`safari` 与 `browser` 是弱词。** 仅靠 `safari` 一词入并集的段只有 **14 段 / 4 个块名 / 4 种句子**，逐条读完是 `access a user's Safari history`、`read Safari's browsing history`、`retrieve Safari bookmarks without an entitlement check`——**都属于浏览器数据面，不是误收**，但它们没有"输入对象"，因此第 4 章 I1 的 1,153 段应读作"网页与浏览器面"，而非"网页输入"。

**（3）`media` 无独立贡献。** 只含 `media` 而不含其它输入词的段为 **0**——所有 `media file` 段同时被 `file` 收走。这说明电池的冗余是良性的：**没有任何一段是靠一个歧义词进来的**。

# 4 八类闭合分区：3,245 段之和

判序固定（I1→I8，取**首个命中类**），正则与第 3 章词项一一对应：

| 类 | 判据词群 | 段 | 页 | 名 | 占并集 |
|---|---|---|---|---|---|
| I1 网页与浏览器 | web content / webpage / website / iframe / browser / safari / same-origin | 1,153 | 177 | 33 | 35.5% |
| I2 文件与归档 | file / archive / zip / tar / disk image / bundle / document / docx / keynote / webarchive / usd / backup | 1,107 | 172 | 112 | 34.1% |
| I3 媒体与字体 | image / photo / video / audio / media / font / clip / texture / model / bitmap | 392 | 145 | 31 | 12.1% |
| I4 网络与协议面 | network / packet / frame / smb / afp / ntlm / 802.1x / 802.11 / wi-fi / wireless / proxy / vpn / directory server | 323 | 108 | 34 | 10.0% |
| I5 近场与配件 | bluetooth / nfc / usb / lightning / thunderbolt / mfi / airplay / airdrop / handoff / continuity / pairing / accessory / carplay / homekit | 49 | 37 | 8 | 1.5% |
| I6 消息与通知 | message / notification / email / attachment / contact / imessage / facetime | 147 | 88 | 31 | 4.5% |
| I7 凭据与签名材料 | certificate / crl / ocsp / token / ticket / passcode / password / credential / keystroke | 68 | 54 | 20 | 2.1% |
| I8 代码与可执行体 | binary / executable / jar / plug-in / kext | 6 | 5 | 4 | 0.2% |
| 未归类 | — | **0** | — | — | — |
| 合计 | — | **3,245** | 255 | 201 | 100% |

**1,153 + 1,107 + 392 + 323 + 49 + 147 + 68 + 6 = 3,245**，与并集逐段相等。

**判序为什么必须写死**：并集里有 **325 段（10.0%）同时命中两类以上**，**2,920 段**只命中一类。重合集中在 **I2∩I3 = 242 段**（`maliciously crafted video file`、`image file` 这类"既是文件又是媒体"），其余组合都很小：I1∩I3 13、I4∩I5 12、I1∩I4 10、I3∩I6 10、I4∩I7 8、I1∩I6 5、I1∩I7 5、I6∩I7 5、I2∩I4 6、I2∩I5 3、I2∩I8 3、I1∩I2 2、I2∩I7 1。**把 I3 提到 I2 前面，I2 会少 242 段**——这就是"同一份语料、两种分区、都自洽"的实例，所以本篇所有引用都以本表顺序为准。

每类一条逐字例句（取自该类实际 Impact，不做改写）：

- I1：`Processing maliciously crafted web content may lead to arbitrary code execution`
- I2：`Restoring a maliciously crafted backup file may lead to modification of protected system files`
- I3：`Parsing a maliciously crafted video file may lead to unexpected system termination`
- I4：`Mounting a maliciously crafted SMB network share may lead to system termination`
- I5：`A malicious Bluetooth input device may bypass pairing`
- I6：`A malicious app may be able to access notifications from other iCloud devices`
- I7：`An attacker with physical access may be able to bypass the passcode`（`passcode` 词群 10 段 / 10 页）
- I8：`A malicious app may be able to launch arbitrary binaries on a trusted device`

# 5 每类里是谁：块名集中度

附表 A 给出八类 × 块名的完整明细（66 行，纵向合计 3,245）。正文只讲集中度，因为它决定"集合"能不能当清单用。

**（1）I1 极度单极。** `WebKit` 一个块名占 **840 段 / 1,153**（**72.9%**），加上 `Safari` 73、`WebRTC` 42、`libxslt` 30、`WebKit Canvas` 28，前五个名占 **1,013 段（87.8%）**；其余 25 个名合计 101 段。**"网页输入"≈"WebKit"，这是 T2 最不成比例的一处。**

**（2）I2 是长尾。** 112 个块名，头部 `CoreMedia` 104、`CoreAudio` 71、`ImageIO` 69、`SceneKit` 66、`PackageKit` 58，前五名 368 段（33.2%），**其余 104 个名合计 657 段（59.3%）**。文件面是**全语料的公共面**，没有承担者。

**（3）I3 反而收敛。** 392 段里 `ImageIO` 独占 **184（46.9%）**，`SceneKit` 27、`Model I/O` 22、`FontParser` 21、`CoreText` 18。**媒体与字体面是有专职部件的**——这与 #106 的结论一致（字体条目集中在 `FontParser` / `CoreText` / `Siri`）。

**（4）I4 的头部是硬件接口类。** `AirPlay` 77、`Kernel` 30、`SMB` 27、`mDNSResponder` 27、`Wi-Fi` 22、`CoreUtils` 15、`Security` 11、`802.1X` 8。**注意 `Kernel` 在网络类里排第二**——内核条目谈的是分组、协议栈与挂载，第 10 章会展开。

**（5）I5 只有 8 个名。** `MobileAccessoryUpdater` 12、`Core Bluetooth` 11、`Bluetooth` 8、`Kernel` 7、`Audio` 5、`Accessibility` 4、`Passkeys` 1、`Trusted Device` 1。**49 段就是"近场与配件"的全部**——与 `carplay` / `homekit` / `mfi` / `thunderbolt` 各 0 段互为印证：**T2 承认的配件面比选题预设的小一个数量级。**

**（6）I7、I8 是词形而非对象。** I7 头部 `Authentication Services` 9、`Security` 8、`Kernel` 7、`curl` 7；I8 只有 6 段，分属 `AppleMobileFileIntegrity` 2、`Face Gallery` 2、`CoreSymbolication` 1、`otool` 1。**I8 的 6 段里没有任何一段描述"恶意二进制文件被投递"**——`binary` 在 T2 说的是"能在别处启动二进制"，是能力不是对象。

# 6 `crafted` 的宾语：1,777 个窗口、39 种写法

`maliciously crafted` 在 Impact 侧 **1,769 段 / 218 页 / 81 名**；含 `crafted` 词者 **1,786 段**。宾语用**按空白切词 + 模态动词截断**提取（第 16 章规则），得 **1,777 个窗口 / 39 种**，全表见附表 B。

头部（附表 B 前 12 项）：`web content` **793**、`file` **295**、`image` **167**、`media file` **102**、`video file` **74**、`font` **46**、`webpage` **38**、`3D model` **24**、`website` **19**、`texture` **18**、`backup file` **17**、`disk image` **17**。

**三项 1,255 段（`web content` + `file` + `image`）占 1,777 的 70.6%。**

**截断规则敏感性（本篇登记的方法论自查）**：同一关键词只换窗口长度上限，表就变——

| 提取规则 | 窗口数 | distinct |
|---|---|---|
| 向后取 ≤4 词、遇句末标点止 | 1,786 | 59 |
| ≤5 词 | 1,786 | 70 |
| ≤6 词 | 1,786 | 81 |
| ≤8 词 | 1,786 | 86 |
| **≤5 词 + 模态动词截断（本篇采用）** | **1,777** | **39** |

窗口总数几乎不变（1,777 / 1,786 差 9），但**distinct 从 39 涨到 86**——多出来的 47 种全是 `file may lead`、`web content may` 这类被标点前的模态词截断规则折叠掉的碎片。**"宾语有多少种写法"取决于截断规则，不取决于语料。** 引用这类数必须同时给规则；本篇正文与附表 B 一律采用最后一行。

另有两处词形细节：`app` 作为 `crafted` 的宾语出现 **5 段**（施动者被写成受事，是反差四的例外，登记为词表边界情形，不改并集）；`ntlm input` **2 段**——全语料把"输入"当受事名词的只有这两段加上 `Processing malicious input` 1 段，共 **3 段**。

# 7 准入动词：两个动词撑起输入句

按第 1 章规则（词干 + `e|es|ed|ing|s`，词边界），Impact 侧词族段数：

| 词干 | 中文 | 词族段 | 页 | 严格动名词段 | Desc 词族段 |
|---|---|---|---|---|---|
| process | 处理 | 1,889 | 228 | `processing` 1,769 | 8 |
| read | 读取 | 293 | 118 | `reading` 0 | 305 |
| view | 查看 | 128 | 73 | `viewing` **0** | 2 |
| access | 访问 | 1,488 | 161 | `accessing` **0** | 310 |
| pars | 解析 | 146 | 65 | `parsing` 146 | 83 |
| visit | 访问(网页) | 88 | 57 | `visiting` 82 | 0 |
| install | 安装 | 67 | 50 | `installing` 0（`installed` 66） | 0 |
| connect | 连接 | 62 | 20 | `connecting` 58 | 0 |
| shar | 共享 | 63 | 48 | `sharing` 25 | 0 |
| mount | 挂载 | 43 | 20 | `mounting` 40 | 0 |
| brows | 浏览 | 39 | 32 | `browsing` 39 | 0 |
| send | 发送 | 24 | 23 | `sending` 8 | 10 |
| open | 打开 | 23 | 10 | `opening` 21 | 199 |
| pair | 配对 | 22 | 22 | `pairing` 9 | 0 |
| download | 下载 | 18 | 18 | `downloading` 0（`download` 17） | 0 |
| restor | 恢复 | 18 | 13 | `restoring` 18 | 0 |
| load | 载入 | 14 | 14 | `loading` 2 | 1 |
| typ | 键入 | 7 | 7 | `typing` 7 | 141 |
| receiv | 接收 | 6 | 5 | `receiving` 3 | 0 |
| play | 播放 | 8 | 8 | `playing` 8 | 0 |
| enter | 输入/进入 | 3 | 3 | `entering` 3 | 0 |
| search | 搜索 | 1 | 1 | `searching` 0 | 0 |
| click | 点击 | **0** | 0 | 0 | 0 |
| render | 渲染 | **0** | 0 | 0 | 0 |
| import | 导入 | **0** | 0 | 0 | 4 |
| trigger | 触发 | **0** | 0 | 0 | 0 |
| reach | 到达 | **0** | 0 | 0 | 5 |
| interact | 交互 | **0** | 0 | 0 | 0 |
| upload | 上传 | **0** | 0 | 0 | 0 |
| sync | 同步 | **0** | 0 | 0 | 0 |
| scan | 扫描 | **0** | 0 | 0 | 0 |
| subscrib | 订阅 | **0** | 0 | 0 | 0 |

四条读法。

**（1）`processing` 一个词等于"输入处理"这件事。** 1,889 词族段里 **1,769 段**的形态是 `processing`，其中 **1,760 段**是句子第一个词。**换句话说：T2 里每 4 条 Impact 就有 1 条以 `Processing` 开头（1,760 / 7,434 = 23.7%）。**

**（2）用户动作词全为零。** `clicking`、`rendering`、`importing`、`triggering`、`reaching`、`interacting`、`uploading`、`syncing`、`scanning` **词族整体 0 段**；`viewing` / `accessing` 的动名词形式 **0 段**——虽然 `access` 出现 **1,470 段**、`view` 出现 **108 段**，但它们一律以 `may be able to access` / `may be viewed` 的形式出现在**后果位置**，**从不作为输入的入口动作**。**T2 的句式里，用户不做任何事；是系统在 processing。**

**（3）`install`、`download` 只有被动态。** `installed` 66 段、`download` 17 段，`installing` / `downloading` 各 0——**安装与下载是被陈述的结果，不是被触发的输入。**

**（4）Description 侧词族更宽，但方向相反。** `open` 在 Desc 侧 199 段、`read` 305 段、`typ` 141 段，而 `processing` 在 Desc 侧只有 8 段。**修复栏说的是"打开/读取/键入时"，影响栏说的是"处理时"**——同一部件在两栏里换了主语。

# 8 Impact 首词的三分区：68.0% / 29.3% / 2.7%

把 7,434 段含 Impact 的句子按第一个词分类：

| 类 | 判据 | 段 | 占比 |
|---|---|---|---|
| A 冠词起句 | 首词为 `a` / `an` / `the` | 5,056 | 68.0% |
| B 动名词起句 | 首词以 `ing` 结尾，或为 `parsing` / `opening` / `reading` | 2,179 | 29.3% |
| C 其它起句 | 其余 | 199 | 2.7% |

**A 类内部**：`an` **3,845**、`a` **1,211**（`the` 0）。**"主语是某个东西"是最常见的句式**，宾语式输入（`A maliciously crafted ___`）与施动者式（`An app ___`）都在这里。

**B 类共 21 个首词**，头部就是第 7 章的动词：`processing` 1,760、`parsing` 146、`visiting` 82、`connecting` 58、`mounting` 40、`opening` 21、`restoring` 18、`playing` 8、`deleting` 6、`extracting` 6、`running` 6、`unpacking` 6、`replying` 4、`turning` 4、`copying` 3、`incoming` 3、`cloning` 2、`loading` 2、`muting` 2、`enabling` 1、`forwarding 1`。**合计 2,179**，前 8 词占 **2,133 段（97.9%）**。

**规则瑕疵登记**：`incoming`（3 段，如 `Incoming messages may …`）是形容词、不是动名词，被 `/ing$/` 误收进 B 类。**该瑕疵影响 3 段（0.04%），不改并集、不改任何结论**，但按本篇的规矩必须写出来，而不是悄悄改规则。

**C 类是 50 种句子、每种只出现 1 次**，这 199 段是"没有输入、没有施动者主语"的例外集合，头部形态：`Multiple issues in curl` / `Multiple issues in OpenSSH` / `Multiple issues in libexpat, including …`（开源组件套语，共 40 段以 `Multiple` 开头）；`Photos in the Hidden Photos Album may be viewed without authentication`（16 段）；`Private Browsing tabs may be accessed without authentication`（23 段以 `Private` 开头）；`Password autofill may fill in passwords after failing authentication`（15 段）；`Maliciously crafted web content may violate iframe sandboxing policy`；`Apps that appear to use App Sandbox may be able to launch without restrictions`；`USB Restricted Mode may not be applied to accessories connected during boot`；`When your headphones are seeking a connection request to one of your previously paired devices, an attacker in Bluetooth range might be able to spoof the intended source device…`（全语料唯一一句以从句起头并给出 Bluetooth 近场条件的长句）。

**这一层的价值在于它证明了"句式是有模板的"**：68.0% + 29.3% = **97.3%** 的 Impact 落在两种起句里，C 类 50 种 × 1 次是模板外的手写例外。**要把"输入清单"当成可枚举集合，前提是承认这个模板性。**

# 9 接口词清点：15 个零与 4 个小数字

T2 全树 Impact 侧（括号为 Description 侧）：

| 词 | Impact 段 | Desc 段 | 词 | Impact 段 | Desc 段 |
|---|---|---|---|---|---|
| `protocol` | **0** | 0 | `database` | **0** | 0 |
| `endpoint` | **0** | 0 | `NFC` | **0** | 0 |
| `XPC` | **0** | 0 | `URI` | **0** | 0 |
| `Mach` | **0** | 0 | `system call` | **0** | 0 |
| `IPC` | **0** | 0 | `callback` | **0** | 0 |
| `HTML` | **0** | 0 | `header` | **0** | 7 |
| `CSS` | **0** | 0 | `bundle` | **0** | 1 |
| `JavaScript` | **0** | 0 | `package` | **0** | 1 |
| `PDF` | **0** | 0 | `interface` | **29** | 29 |
| — | — | — | `socket` | **8** | 0 |
| — | — | — | `URL` | **7** | 19 |
| — | — | — | `API` | **5** | 6 |

**（1）15 个接口词在措辞层不存在。** 这不是"少"，是**零**：`XPC` / `Mach` / `IPC` 是 iOS 进程间通信的三大真实机制，`protocol`、`endpoint`、`database`、`NFC` 是评审清单上的常规项，**在 7,434 段 Impact 里一段都没有**。

**（2）4 个非零词也不是接口叙述。** `interface` 29 段的典型用法是 `network interface`（网络设备）与 UI 语境；`API` 5 段是 `Web APIs`（`On a Mac with Lockdown Mode enabled, web content opened via a file URL may be able to use Web APIs that should be restricted`）；`URL` 7 段是 `file URL` / `via a file url`；`socket` 8 段是内核 socket 层条目。**没有一段写"某接口暴露给外部输入"。**

**（3）与 T1 对照。** "接口" 32 次 / 17 页，头部两页《Apple 设备的无线局域网隐私》(4)、《IPv6 安全性》(4)——原句是"每次建立这些网络接口均会生成新的随机地址"，指**网络接口**；"协议" 83 次 / 37 页头部是 VPN 与量子安全加密；"可达" 与 `attack surface` 的中文对应"攻击面"**只有 12 次 / 9 页**（最大页《操作系统完整性》3 次）。**两层的词都在，两层的"接口=暴露面"都不成叙述。**

# 10 组件代理集合：472 名、201 有名、271 名零

把块名当组件，逐名统计"该名的段里有多少含输入名词"。**命中率 = 该块名的输入段 / 该块名总段**，分母是块名内段数、不是全语料。

**顶端（≥80%，5 名）**：`AirPlay` 77/84 = **91.7%**、`CoreText` 33/36 = **91.7%**、`ImageIO` 261/293 = **89.1%**、`WebRTC` 48/55 = **87.3%**、`WebKit` 859/1,007 = **85.3%**。

**底端（≤20%，29 名）**：`Spotlight` **3.5%**（3/85）、`Sandbox` **7.5%**（6/80）、`Kernel` **8.3%**（62/745）、`StorageKit` **8.5%**、`App Store` 11.8%、`Siri` 12.6%、`APFS` 12.5%、`LaunchServices` 13.3%、`CoreServices` 14.8%、`TCC` 15.2%、`Shortcuts` 15.4%、`Accounts`/`Foundation`/`System Settings`/`Apple Account` 16.7%、`libxpc` 16.1%、`curl` 17.9%、`AppleMobileFileIntegrity` 18.5%、`Installer` 19.4% 等。

**恰为 0.0% 且段数 ≥30 的 9 名**：`AVEVideoEncoder`(53)、`WindowServer`(44)、`Apple Neural Engine`(39)、`Calendar`(39)、`GPU Drivers`(39)、`Game Center`(37)、`Find My`(35)、`Sandbox Profiles`(33)、`CloudKit`(30)。

**全体**：472 名里 **201 名有输入段（42.6%）**、**271 名零命中**，这 271 名覆盖 **1,942 段 / 173 页**，占全树段 **21.3%**。

四条读法。

**（1）`Kernel` 是"低命中高段数"的极端。** 745 段里只有 62 段提到输入名词，而这 62 段的输入词是 `network` 46、`file` 13、`packets` 8、`bluetooth` 7、`passcode` 7、`vpn` 6、`disk image` 5——**内核条目谈输入时谈的是网络栈与挂载，不是解析器**。反过来说：**内核的其余 683 段（91.7%）根本不在"外部输入"这条轴上**，它们写的是权限与状态。这与 #111 的结论正好对上：内核是终点，不是入口。

**（2）零命中的 271 名不能读作"不可触达"。** `Calendar`、`Game Center`、`Find My`、`CloudKit` 的条目主语是 App 与用户，落点在数据与授权；它们**没有输入名词，是因为 Apple 没写输入是什么**，不是因为不存在输入。**这是本篇最重要的反向警示**：命中率只能用来识别"哪些部件被措辞层承认有输入面"，不能用来识别"哪些部件没有输入面"。

**（3）命中率与体量无关。** 顶端既有 1,007 段的 `WebKit`，也有 36 段的 `CoreText`；底端既有 745 段的 `Kernel`，也有 30 段的 `CloudKit`。**"大组件=多输入面"不成立。**

**（4）输入面高度集中在少数页。** 输入并集按页排序，头部 12 页为 149035(83)、149038(60)、122373(58)、149042(57)、149043(57)、149034(55)、148287(54)、128070(47)、128068(46)、121238(43)、122371(43)、128069(42)。**全树只有 2 页一段输入都没有**：120887 与 123356。**"255/257 页有输入面"与"2 页没有"合起来说明：输入面在页维度上是全覆盖的，在部件维度上是极不均匀的。**

# 11 沙盒词群：边界被命名了 418 次，输入一次都没有

`(?<![\w-])sandbox\w*(?![\w-])` 在 Impact 侧命中 **418 段**；严格词形 `sandbox` **359 段 / 111 页 / 60 名**。逐形态：

| 形态 | 段 | 其中已在输入并集内 |
|---|---|---|
| `break out of its sandbox` | 240 | **0** |
| `out of its sandbox` 其余形态（`execute arbitrary code out of its sandbox`） | 18 | 0 |
| `sandbox restrictions` | 32 | **0** |
| `break out of Web Content sandbox` | 9（`WebKit`、`iTunes`） | — |
| `sandboxed`（形容词） | 75 | 13 |
| `a sandboxed app` 主语 | 58 | 13 |
| `sandbox` 词族合计 | 418 | **79** |

**（1）沙盒段与输入段几乎互斥。** 418 段里只有 **79 段（18.9%）**同时含输入名词；头部句式 `break out of its sandbox` 的 240 段与输入名词的交集是 **0**。**"逃逸"这一整类条目不写输入**——它写的是"某个 App 能出界"。

**（2）`sandboxed` 是唯一带输入味的形态。** 75 段里 13 段进并集，且 `a sandboxed app` 主语 58 段中 13 段进并集——**同一个 App 既可以被写成施动者、也可以被写成"处在沙盒里的东西"**，本篇把这 13 段留在输入并集内（它们确有受事名词），把另外 45 段留给 #111 的起点主语表。

**（3）"可达性"没有词。** 与沙盒相邻的一组词全为零：`reachable` Impact **0**（Desc 5）、`reach` 词族 **0**、`attack surface` **0**、`surface` **0**、`entry point` **0**、`vector` **0**。T2 表达"能到得了"用的是**主语身份**：`remote attacker` **194 段**、`local attacker` **64 段**、`physical access` **142 段**、`proximity` **30 段**、`Bluetooth range` **2 段**。**可达性被写在"谁"上，不写在"哪里"上。** 这是通往 #113 的桥。

# 12 通道词 `via`：23 个窗口、5 种写法

`via` 之后按空白切词取 ≤4 词窗口，全树得 **23 个窗口 / 5 种**：`icloud link` **9**、`incorrect connection` **8**、`accessibility features` **3**、`file url` **2**、`email` **1**。

对照：`maliciously crafted` 1,769 段、`crafted` 1,786 段、`an app` 起句 3,135 段。**通道词的量级比输入词低两个数量级**（23 对 1,786）。**换句话说，T2 说了"什么东西"上千次，说"经由什么途径"23 次。**

三条限制条件句也顺便清点：`On MDM managed devices, …`（7 段以 `On` 起头，含 `On a device with Private Relay enabled` 与 `On a Mac with Lockdown Mode enabled`）、`In certain configurations, an attacker with host-limited sudo access may be able to elevate privileges`（1 段，全语料唯一以配置条件起头并以提权收尾的句子）、`When your headphones are seeking a connection request to one of your previously paired devices, …`（1 段）。**全树明确写出触发条件的 Impact 只有约 9 段**，与 `via` 的 23 窗口同量级——**条件与路径是措辞层真正的空白**。

# 13 交集纪律：输入面与越级面几乎是两个集合

凡涉及"某类段是不是另一类段的子集"，本篇一律给三数（|A|、|B|、|A∩B|）。

| 集合 A | A 段数 | 集合 B | B 段数 | A∩B |
|---|---|---|---|---|
| 输入并集 | 3,245 | #111 越级并集 | 602 | **124** |
| 输入并集 | 3,245 | `bypass` 词族（全树 / 域 A） | 291 / 288 | **72** |
| 输入并集 | 3,245 | `arbitrary code execution` | 218 | **178** |
| 输入并集 | 3,245 | `memory corruption` / `corrupt … memory` | 425 | **209** |
| 输入并集 | 3,245 | `kernel privileges` | 79 | **0** |
| 输入并集 | 3,245 | `read and write capability` | 33 | **0** |
| 输入并集 | 3,245 | `private key` | 7 | **0** |
| 输入并集 | 3,245 | `sandbox` 词族 | 418 | **79** |

四点。

**（1）输入面与特权面正交。** 3,245 段与 79 段 `kernel privileges` **交集为 0**；与 33 段 `read and write capability` **交集为 0**。**写了输入的段不写特权终点，写特权终点的段不写输入**——两栏在同一语料里互不交接。这不是巧合，是模板分工（第 7 章的句式分区给出机制）。

**（2）代码执行与内存破坏倒是重合。** 178/218（**81.7%**）与 209/425（**49.2%**）。**"输入 → 执行/破坏"这条链在措辞层是通的**，两条断链的位置在特权与授权。

**（3）`bypass` 只有 72/291 与输入同句。** #110 记的 288 段是**域 A** 值，本篇全树值 **291**，差 3 段来自域 B——**两个数不矛盾，是同一指标的不同域**；`bypasses` / `bypassed` / `bypassing` 三个词形在 Impact 侧**各 0 段**，所以 `bypass` 原形与词族同为 291。

**（4）并集之间不能相加。** 若把输入 3,245 与越级 602 相加得 3,847 就错了；正确值 **|A∪B| = 3,245 + 602 − 124 = 3,723 段**，占含 Impact 段 50.1%。

# 14 T1 手册层：106 次"输入"里没有一句恶意输入

附表 C 是 54 个词在 178 页可用正文里的全表。与本篇选题直接相关的六个：

| 词 | 次 | 页 | 最集中的两页 |
|---|---|---|---|
| 输入 | **106** | 55 | 《Tap to Pay on iPhone 安全性》(11)、《用于数据保护的密钥包》(7) |
| 组件 | **91** | 28 | 《安全隔区》(31)、《WidgetKit 安全性》(9) |
| 接口 | **32** | 17 | 《Apple 设备的无线局域网隐私》(4)、《IPv6 安全性》(4) |
| 攻击面 | **12** | 9 | 《操作系统完整性》(3)、《基于 Intel 的 Mac 的 recoveryOS 和诊断环境》(2) |
| 可达 | **0** | 0 | — |
| 字体 | **0** | 0 | — |

**（1）"输入"在 T1 是三种别的东西。** 抽查头部三页的原句：《安全隔区》——"由 sepOS 内核控制的**输入输出**内存管理单元 (IOMMU)"；《用于数据保护的密钥包》——"例如，**输入**密码后"；《Tap to Pay on iPhone 安全性》——"存储转发交易不支持 PIN 码**输入**"。**106 次里没有一次指"外部恶意数据对象"。** 这与 #111 第 11 章"等级 / 级别 / 层级三词五义"是同一现象的第二例：**中文层的高频抽象词，逐个看都是别的义项。**

**（2）"组件"有词、无清单。** 91 次 / 28 页，头部是《安全隔区》(31)——讲 SEP 与 AES 引擎的组件；《Tap to Pay》的"免接触式支付组件安全性"；《WidgetKit 安全性》讲小组件与复杂功能可能显示敏感信息。**没有任何一页给出"可触达组件的枚举表"。**

**（3）"接口"是网络接口。** 32 次的头部页是无线局域网隐私与 IPv6，原句为"每次建立这些网络接口均会生成新的随机地址"。**T1 的"接口"= network interface，不是 API/暴露面**；真要讲 API 时它写的是"框架"（`WidgetKit 是一个可供开发者提供小组件…的框架`）。

**（4）"可达"与"字体"两个零。** 中文全层 **0 次**"可达"，说明 #113 要处理的"路径"问题在 T1 也没有现成词；"字体" **0 次**则与 #106 的结论一致——**字体面在中文手册层完全不存在，其全部公开文本证据都在措辞层（`font` 54 段 / I3）**。

# 15 两层换名矩阵

| 选题词 | T2 措辞层实际用什么写 | T1 手册层实际用什么写 | 对接状态 |
|---|---|---|---|
| 外部输入 | 具体受事名词 + `maliciously crafted`（1,769 段）、`web content` 793、`file` 295 | "文件"405 / "网页"36 / "浏览器"48 / "图像"32 | **可对接，但 T1 无总名** |
| 输入这个概念本身 | `input` 38 段（其中 20 段套语） | "输入"106 次（无一指恶意对象） | **两层都没这个概念** |
| 接口 | `interface` 29（网络接口义）、`API` 5 | "接口"32（网络接口义）、"框架"若干 | **同词同义，都不是暴露面** |
| 可达性 | 主语身份：`remote attacker` 194、`physical access` 142、`proximity` 30 | "可达" **0**、"攻击面" 12 / 9 | **T2 有代理、T1 无** |
| 组件 | 块名 472（201 有输入段） | "组件"91 / 28 页、"安全隔区"专页 | **T1 讲硬件边界，T2 讲软件部件** |
| 用户动作 | 动名词形式全 0（`clicking`、`viewing`、`accessing`…） | "键盘"49、"摄像头"26、"麦克风"16 | **T1 有设备、T2 有授权，都没"动作"** |

矩阵给出的判断是：**两层的词汇在"输入"与"接口"这两行上并不互补，而是各自缺一半**——T2 有成规模的输入对象却没有输入概念，T1 有"输入 / 接口"这两个词却没有攻击面叙述。任何"把两层拼起来就是完整清单"的说法都超出证据。

# 16 验证与断言

以下 15 条断言按第 17 章脚本可独立复算。

**验证一（并集与分母）**：含 Impact 段 **7,434**，输入并集 **3,245**，比 **43.65% → 43.7%**；全树段 **9,136**，比 **35.52% → 35.5%**；页 **255 / 257**，名 **201 / 472 = 42.58%**。

**验证二（分区闭合）**：八类 **1,153 + 1,107 + 392 + 323 + 49 + 147 + 68 + 6 = 3,245**，未归类 **0**；附表 A 纵向 66 行合计 **3,245**，与并集逐段相等。

**验证三（重叠度可解释）**：单类命中 **2,920 段** + 多类命中 **325 段** = **3,245**；13 组多类组合之和（242 + 13 + 12 + 10 + 10 + 8 + 6 + 5 + 5 + 5 + 3 + 3 + 2 + 1 = 325）与"2 类"计数相等，**说明没有任何段命中三类以上**。

**验证四（施动者反证）**：正确并集 **3,245**；把 8 类施动者名词加入后 **6,613**，差 **3,368** 段，其中 **2,778 段**（**82.5%**）以 `an app may` 起头。**若并集报告值接近 6,600，即为本口径错误。**

**验证五（`input` 与并集不同集合）**：`input` 词族 Impact **38 段**，其中仅 **9 段**在输入并集内；`An input validation issue was addressed` 套语 **20 段**（页 122371–122378、125108–125116），逐条列出后与 38 段吻合。

**验证六（crafted 窗口对账）**：采用规则窗口 **1,777**，附表 B 纵向 39 行合计 **1,777**；头部三词 **793 + 295 + 167 = 1,255**，占 **70.6%**；`maliciously crafted` 段数 **1,769 < crafted 段数 1,786**，差 **17 段**。

**验证七（动词表自洽）**：`processing` 严格 **1,769** ≤ 词族 **1,889**；起句 **1,760** ≤ 严格 **1,769**；起句且属输入并集 **1,723**，即 **37 段** `processing` 起句不含任何输入名词。**三段链条单调不增**，若出现 1,760 > 1,769 即为边界正则漏写。

**验证八（首词三分区）**：5,056 + 2,179 + 199 = **7,434** = 含 Impact 段数；A 类内部 `an` 3,845 + `a` 1,211 = **5,056**；B 类 21 个首词之和 **2,179**；C 类 distinct **50**、每句计数 **1**、之和 **199**。

**验证九（接口零断言的位置限定）**：15 个接口词在 **Impact 侧 0 段**；其中 `header`（7）、`callback`（0）、`database`（0）、`package`/`bundle`（各 1）在 **Description 侧同样接近 0**。**零段不等于概念不存在**，只等于 T2 不用该词。

**验证十（组件命中率分母）**：`Kernel` 命中率 **62 / 745 = 8.32%**，分母为块名内全部段（含无 Impact 者）。62 段的输入词频之和 46 + 13 + 8 + 7 + 7 + 6 + 5 = **92 > 62**，因为一段可含多个输入词——**命中率按段计、词频按次计，不可混用。**

**验证十一（零命中集合的规模）**：零输入块名 **271** + 有输入块名 **201** = **472** = distinct 块名总数；271 名覆盖段 **1,942**、页 **173**。**若某解析器报告 271 ≠ 472 − 201，即为块名归一化不一致。**

**验证十二（沙盒与输入互斥）**：`sandbox` 词族 Impact **418**，∩输入 **79**；`break out of its sandbox` **240** ∩输入 **0**；`out of its sandbox` **258** = `break out` **240** + 其余 **18**（`arbitrary code out of its sandbox`）。**258 − 240 = 18** 即差额，两个数不可混引。

**验证十三（bypass 的两域对账）**：全树 `bypass` **291**、域 A **288**、复数与过去式 **0**；输入并集内 **72**。**#110 的 288 与本篇的 291 是同一指标的域 A / 全树两值**，不是数据变动。

**验证十四（页维度全覆盖）**：输入并集页 **255**，无输入段的页恰 **2** 页（`120887`、`123356`），二者 + 255 = **257** = 全树页数。**若第三页出现，说明块切分改变了页归属。**

**验证十五（域 B 的可复现性）**：域 B 输入段 **35**，块名分布 `WebKit` 27 / `ImageIO` 3 / `Bluetooth` 1 / `CoreMedia` 1 / `LLVM` 1 / `NSOpenPanel` 1 / `WebRTC` 1，页覆盖 **11 页全部命中**；名 **7**。**只认 `<h3>` 的解析器会让这 35 段整体消失，且 `NSOpenPanel`、`LLVM` 两个名从统计中消失。**

# 17 复算步骤

1. 镜像目录 `_tmp/adv2/<id>.html`，257 份（排除 `122085`）。逐页先测 `<h3 class="gb-header` 是否存在；存在走域 A 刀、不存在走域 B 刀（粗体段起块）。块体终止锚必须是四个（`<h3 class="gb-header`、`<div id="disclaimer"`、`<footer`、文末），少一个会把页脚免责声明并入字段文本。
2. 段取 `<p class="(?:gb-paragraph|paragraph)[^"]*"[^>]*>(.*?)</p>`，剥标签、解实体、折空白，丢 `^Available for` 行；`Impact:` / `Description:` 多句以 `" || "` 连接，其余以 `" ~ "` 连接。**并集只读 Impact。**
3. 词计数一律带词边界 `(?<![\w-])kw(?![\w-])`；**复数写进交替里**（`files?`、`archives?`、`binar(?:y|ies)`——`binaries?` 匹配不到 `binary`）；词族写成 `词干(?:e|es|ed|ing|s)?`，**不要写 `parse\w*`**（`parsing` 不含子串 `parse`），也不要写裸词干 `typ\w*`（会误收 `typical`）。
4. 宾语与通道一律**按空白切词**，向后取 ≤5 词、遇 `[.,;:]` 或模态动词（`may|could|will|might|can|would|lead|leads|result|results|cause|causes|trigger|triggers`）截断。**窗口长度上限必须与截断规则一起写进引用**，第 6 章的敏感性表就是为此而测。
5. 分区判定顺序固定为 I1→I8 并输出"未归类 0"断言；调整后须重新给全表段数，并说明 I2∩I3 那 242 段归到哪个类。
6. 命中率一律写清分母（**块名内段数**），并单独输出 271 个零命中块名及其覆盖段/页。
7. 涉及跨期对照时（602、288、9,045），先声明取全树还是域 A，两个数都写出来；断言"A ⊂ B"必须以成员集合求交并同时报 |A|、|B|、|A∩B|。
8. T1 侧：逐页取最后一个 `^版权和商标$` 之后至首个含 `Apple Footer` 行之前，排除 `sec2727afbbc`，得 **178 页可用**；中文词按**子串**计数，并在正文说明"输入 / 输入输出"等有包含关系、**不可相加**；页面标题从 `_tmp/titles.txt` 的 `id|标题` 行取。
9. 所有含中文或含复杂转义的正则**写成 UTF-8 脚本文件**（`use utf8` + `binmode ":utf8"`），不要塞进命令行参数——Windows 代码页会静默破坏匹配。

# 18 缺口清单

1. **T2 没有"输入面"这个层级。** 本篇的 3,245 段是**代理集合**：它回答"哪些条目写了具体对象"，不回答"外部输入一共有多少种"。任何把八类当穷举引用的说法都超出证据。
2. **接口层是空的，不能补。** `protocol` / `endpoint` / `XPC` / `Mach` 各 0 段是事实，**不构造"接口并集"是本篇的决定**，不是遗漏。需要接口叙述时只能引 T1 与开发者文档，并声明是另一层。
3. **块名不等于暴露面。** 271 个零命中块名（1,942 段）只能读作"措辞层没写输入"，读不出"不可触达"（第 10 章）。**要判定真实可达性需要产品架构文档与代码，两者都不在本篇语料内。**
4. **版本页与部件页混排。** 并集里同一句话常出现在多个版本页（如 `Mounting … SMB network share` 在 122373–122378 连续 6 页）。本篇按**段**计数、不按**缺陷**计数；换算缺陷数需要去重键（CVE + 块名），本期未建。
5. **iOS / macOS 未分域。** `MobileAccessoryUpdater`、`CUPS`、`PackageKit`、`otool`、`CoreServices` 等多为 Mac 条目；I2 的 112 个块名里必然含大量 Mac 部件。**引用"iOS 输入清单"时必须再切一次页面归属**（#111 第 17 章同样登记过）。
6. **模板异常污染。** `An input validation issue was addressed` 出现在 **Impact** 栏（20 段）——是页面模板错误，不是输入分类。本篇把它排除在并集外、单独清点，但这提醒：**任何按字段做的统计都可能被模板错误影响**。
7. **`via` 只有 23 个窗口，不能建通道分区。** 通道与触发条件交给 #113，且预期结论同样是"措辞层不成表"。
8. **中文层"输入 / 接口"的义项判定靠人工抽查。** 第 14 章只抽查了头部三页；**其余 52 页的 106 次"输入"未逐条读**，因此本篇只敢说"头部页无一指恶意对象"，不敢说"全部 106 次都不是"。
9. **B 类首词规则误收 `incoming`（3 段）**，属可登记的规则瑕疵，不改结论。
10. **静态站与文档层未纳入本篇。** 本篇只用 T2 + T1 两层；`接口` 的权威定义在开发者文档，本期不引。

# 19 常见误读纠正

**误读一："App 也是一种外部输入，所以输入面接近 6,600 段。"（错。** 6,613 里有 **3,368** 段只因施动者名词进来，其中 **2,778** 段是 `an app may`。**App 是做事者，不是进来的东西**——把它算作输入会让"输入清单"退化成"Impact 全文"。正确做法见第 3 章：施动者与受事分表。

**误读二："媒体类只有 392 段，所以媒体面比网络面小。"（要看分母。** 392 段里 `ImageIO` 独占 184，而 `ImageIO` 自身 293 段的命中率是 **89.1%**；I4 的 323 段摊在 34 个块名上。**类的大小由词表宽度决定，不由风险大小决定**——八类之间不可比大小，只能各自看集中度（第 5 章）。

**误读三：把 271 个零输入块名当"这些组件不受外部输入影响"。** 第 10 章的反向警示：`Calendar`、`Find My`、`CloudKit` 的条目不写输入是因为不写，不是因为不能。**零命中率证据只支持"措辞层未承认"。**

**误读四：用 `processing` 1,889 段做分母算比例。** 词族 1,889、严格形 1,769、起句 1,760、起句且在并集内 1,723 —— **四个数都真，对应四个断言**。引用时必须点名用的是哪个（第 7 章、验证七）。

**误读五：说"Apple 写了 39 种外部输入对象"。** 39 是**`crafted` 之后宾语**的 distinct，规则敏感（换截断规则变 86）；且它只覆盖 1,777 窗口，不覆盖并集其余 1,468 段的非 crafted 宾语。**外部输入的正确表述是"3,245 段含具体受事名词，其中 1,777 个 crafted 宾语窗口、39 种写法"。**

**误读六：把 `interface` 29 段当成"接口面有 29 个"。** 29 段的 `interface` 多数是 `network interface`（设备义）；真正的接口叙述（`endpoint` / `API` / `protocol`）是 **0 / 5 / 0**。**词存在 ≠ 概念存在。**

**误读七：因为 `input` 只有 38 段，就说"T2 不谈输入"。** 895 段写着 `web content`、1,058 段写着 `file`。**不谈的是总名，不谈的不是对象。** 这与 #111 的"7 段 `trust` ≠ Apple 不谈信任"是同一条方法论。

**误读八：把输入并集与越级并集相加。** 3,245 + 602 = 3,847 错；正确 **3,723**（交集 124）。更要紧的是：**输入 ∩ `kernel privileges` = 0**——两栏不交接，所以任何"文件进来 → 到 root"的因果表述在措辞层**没有单句证据**（#111 第 7 章同一结论，从另一侧得出）。

# 20 结论

**（1）三个选题词只有一个能建成集合。** 输入 → **3,245 段 / 8 类闭合 / 未归类 0**；接口 → **15 个零 + 4 个小数字，无法建集合**；组件 → **472 个块名的代理集合，201 有名 / 271 名零**。**"外部输入、接口和可触达组件的集合"这个短语在公开文本里实际只有前半个能被清点。**

**（2）措辞层对输入的表达方式是一条模板。** `Processing` / `Parsing` / `Visiting` 三个动名词起头覆盖 **1,988 段**（1,760 + 146 + 82），`A / An` 冠词起头覆盖 **5,056 段**，宾语由 `maliciously crafted` 引出的 **39 种**写法构成，其中三词占 **70.6%**。**清单很短，写得很稳**——这正是可以拿去做防御侧对照的原因：把自家产品的输入类型对着这 39 种与 8 类核一遍，缺口是看得见的。

**（3）最有用的一条负结论是"两栏不交接"。** 输入面（3,245）与特权面（602）**只重合 124 段**，与 `kernel privileges`（79）**重合 0 段**。**"输入 → 后果"在代码执行与内存破坏上是通的（178/218、209/425），在权限与授权上是断的。** 任何人要用 Apple 措辞证明"某类输入能导致某类越级"，都得先解释这 0 段。

**（4）方法论上本篇留下两条规矩。** 一是**施动者名词不进输入电池**（差 3,368 段的量级教训）；二是**窗口截断规则必须与被引用的 distinct 数同栏出现**（39 对 86 的教训）。两者都已写进第 17 章步骤，供 #113 及以后各期沿用。

**（5）不能说的。** 不能说"iOS 的外部输入共有 8 类 39 种"（这是 T2 的写法清单，不是产品穷举）；不能说"271 个组件没有输入面"；不能说"Apple 不谈接口"（`interface` 有 29 段，谈的是网络接口）；不能把中文层 106 次"输入"当恶意输入证据；不能把输入并集与越级并集相加；也不能把 9,045 当全语料（全树 **9,136**）。

---

# 附表 A 输入并集 3,245 段的八类块名明细（66 行）

判序见第 4 章（I1→I8，先命中先归类），因此八类对**归类后的段**互斥、纵向合计等于并集 3,245。每类列段数最多的 8 个块名，不足 8 个的全列，超出的合并为"其余 N 个块名"一行；类标签只在该类首行写出。**数值为该块名在该类内的段数**，不是该块名在全语的段数（`Kernel` 在 I4、I5、I6、I7 四类各有一行）。同一块名可在多类出现，因为一段可含多类名词——归类由判序决定，不代表该段只有一类输入。

| 类 | 块名 | 段 |
|---|---|---|
| **I1 网页与浏览器** | `WebKit` | 840 |
|  | `Safari` | 73 |
|  | `WebRTC` | 42 |
|  | `libxslt` | 30 |
|  | `WebKit Canvas` | 28 |
|  | `ICU` | 15 |
|  | `WebKit Web Inspector` | 13 |
|  | `WebKit Storage` | 11 |
|  | 其余 25 个块名 | 101 |
| **I2 文件与归档** | `CoreMedia` | 104 |
|  | `CoreAudio` | 71 |
|  | `ImageIO` | 69 |
|  | `SceneKit` | 66 |
|  | `PackageKit` | 58 |
|  | `Model I/O` | 30 |
|  | `CoreGraphics` | 27 |
|  | `libarchive` | 25 |
|  | 其余 104 个块名 | 657 |
| **I3 媒体与字体** | `ImageIO` | 184 |
|  | `SceneKit` | 27 |
|  | `Model I/O` | 22 |
|  | `FontParser` | 21 |
|  | `CoreText` | 18 |
|  | `Messages` | 14 |
|  | `Accelerate Framework` | 13 |
|  | `Mail Drafts` | 11 |
|  | 其余 23 个块名 | 82 |
| **I4 网络与协议面** | `AirPlay` | 77 |
|  | `Kernel` | 30 |
|  | `SMB` | 27 |
|  | `mDNSResponder` | 27 |
|  | `Wi-Fi` | 22 |
|  | `CoreUtils` | 15 |
|  | `Security` | 11 |
|  | `802.1X` | 8 |
|  | 其余 26 个块名 | 106 |
| **I5 近场与配件** | `MobileAccessoryUpdater` | 12 |
|  | `Core Bluetooth` | 11 |
|  | `Bluetooth` | 8 |
|  | `Kernel` | 7 |
|  | `Audio` | 5 |
|  | `Accessibility` | 4 |
|  | `Passkeys` | 1 |
|  | `Trusted Device` | 1 |
| **I6 消息与通知** | `Contacts` | 22 |
|  | `Siri` | 12 |
|  | `Share Sheet` | 10 |
|  | `FaceTime` | 9 |
|  | `Messages` | 9 |
|  | `ImageIO` | 8 |
|  | `Mail` | 8 |
|  | `Accessibility` | 7 |
|  | 其余 23 个块名 | 62 |
| **I7 凭据与签名材料** | `Authentication Services` | 9 |
|  | `Security` | 8 |
|  | `Kernel` | 7 |
|  | `curl` | 7 |
|  | `App Store` | 4 |
|  | `IOHIDFamily` | 4 |
|  | `Mail` | 4 |
|  | `Passkeys` | 4 |
|  | 其余 12 个块名 | 21 |
| **I8 代码与可执行体** | `AppleMobileFileIntegrity` | 2 |
|  | `Face Gallery` | 2 |
|  | `CoreSymbolication` | 1 |
|  | `otool` | 1 |

# 附表 B `crafted` 之后的宾语全表（39 行）

窗口取法见第 17 章第 4 步：按空白切词、向后取 ≤5 词、遇 `[.,;:]` 或模态动词截断。本表纵向合计 **1,777**，与第 6 章全量相等；"归属类"列按第 4 章同一判序给出该宾语**多数段落**所属的类，仅供交叉阅读，不用于再分区（同一宾语可跨类）。头部三词 793 + 295 + 167 = **1,255**，占 **70.6%**。

| crafted 宾语 | 窗口 | 归属类 |
|---|---|---|
| `web content` | 793 | I1 |
| `file` | 295 | I2 |
| `image` | 167 | I3 |
| `media file` | 102 | I2 |
| `video file` | 74 | I2 |
| `font` | 46 | I3 |
| `webpage` | 38 | I1 |
| `3d model` | 24 | I3 |
| `website` | 19 | I1 |
| `texture` | 18 | I3 |
| `backup file` | 17 | I2 |
| `disk image` | 17 | I2 |
| `message` | 16 | I6 |
| `audio file` | 14 | I2 |
| `archive` | 12 | I2 |
| `asset catalog` | 12 | — |
| `zip archive` | 10 | I2 |
| `smb network share` | 9 | I4 |
| `3d scene` | 8 | I3 |
| `font file` | 8 | I2 |
| `contact` | 7 | I6 |
| `wi-fi packets` | 7 | I4 |
| `string` | 6 | — |
| `app` | 5 | — |
| `bluetooth packets` | 5 | I4 |
| `files` | 5 | I2 |
| `usd file` | 5 | I2 |
| `volume` | 5 | I4 |
| `email` | 4 | I6 |
| `keynote file` | 4 | I2 |
| `pages document` | 4 | I2 |
| `webarchive file` | 4 | I2 |
| `3d file` | 3 | I2 |
| `afp network share` | 3 | I4 |
| `document` | 3 | I2 |
| `binary` | 2 | I8 |
| `exfat volume` | 2 | I4 |
| `ntlm input` | 2 | I4 |
| `repository` | 2 | I2 |

# 附表 C 手册层逐词普查全表（54 行）

T1 可用页 **178**（179 份转储排除 `sec2727afbbc`），窗口取最后一个 `^版权和商标$` 之后至首个含 `Apple Footer` 行之前。中文词按**子串**计数，故"输入 / 输入输出""接口 / 界面"等存在包含关系，**本表各行不可相加**，也不可据此构造集合。括号内为该词最集中的两页及其次数；次数为 0 的行是**真零**，不是抓取失败。

| 词 | 次 | 页 | 最集中的两页 |
|---|---|---|---|
| `密码` | 622 | 100 | 密码监视（53）；密码安全建议（41） |
| `服务` | 521 | 117 | iCloud 高级数据保护（37）；激活锁安全性（25） |
| `文件` | 405 | 70 | Apple 设备中的数据保护（30）；在 macOS 中使用文件保险箱加密宗卷（23） |
| `签名` | 386 | 70 | 搭载 Apple 芯片的 Mac 的启动磁盘安全性策略控制（27）；搭载 Apple 芯片的 Mac 的 LocalPolicy 文件内容（27） |
| `请求` | 184 | 67 | Apple 钱包中的证件（23）；Apple 钱包中证件的安全性（10） |
| `授权` | 172 | 63 | NFC 与 SE 平台安全性（12）；通过 Apple Pay 使用付款卡支付（11） |
| `扩展` | 169 | 50 | iOS、iPadOS、macOS 和 visionOS 中支持的扩展项（30）；单点登录安全性（25） |
| `内核` | 166 | 41 | 在 macOS 中安全扩展内核（21）；操作系统完整性（18） |
| `证书` | 150 | 29 | TLS 安全性（17）；针对 Apple 设备的管理式设备证明（15） |
| `配置` | 148 | 58 | 配置执行（21）；自动设备注册（9） |
| `输入` | 106 | 55 | Tap to Pay on iPhone 安全性（11）；用于数据保护的密钥包（7） |
| `权限` | 102 | 45 | 操作系统完整性（13）；保护对用户健康数据的访问（12） |
| `配件` | 94 | 8 | HomeKit 通信安全性（40）；使用 HomeKit 保护路由器（17） |
| `组件` | 91 | 28 | 安全隔区（31）；WidgetKit 安全性（9） |
| `协议` | 83 | 37 | 虚拟专用网络 (VPN) 安全性（8）；Apple 操作系统中的量子安全加密技术（8） |
| `令牌` | 70 | 15 | 在 macOS 中管理文件保险箱（18）；用于数据保护的密钥包（13） |
| `蓝牙` | 63 | 28 | 蓝牙安全性（19）；watchOS 系统安全性（6） |
| `邮件` | 62 | 27 | 数字签名和加密（16）；通过 Apple 登录安全性（7） |
| `通知` | 50 | 31 | 自动解锁 Apple 设备（10）；iPhone 蜂窝网络通话中继安全性（4） |
| `进程` | 49 | 27 | iOS 和 iPadOS 中的 ReplayKit 安全性（5）；Apple 安全性研究设备（4） |
| `键盘` | 49 | 12 | 配备触控 ID 的妙控键盘（26）；iOS、iPadOS、macOS 和 visionOS 中支持的扩展项（9） |
| `浏览器` | 48 | 30 | 接力安全性（6）；Apple 操作系统中的后台安全改进（5） |
| `描述文件` | 46 | 17 | 配置执行（15）；钥匙串数据保护（5） |
| `视频` | 41 | 12 | HomeKit 摄像头安全性（16）；iOS 和 iPadOS 中的 ReplayKit 安全性（7） |
| `链接` | 39 | 25 | iOS、iPadOS、Apple tvOS、visionOS 和 watchOS 中的 App 代码签名过程（4）；将信用卡或借记卡添加到 Apple Pay（4） |
| `音频` | 39 | 9 | HomeKit 通信安全性（11）；iOS 和 iPadOS 中的 ReplayKit 安全性（9） |
| `网页` | 36 | 17 | iCloud 高级数据保护（12）；接力安全性（6） |
| `传感器` | 34 | 9 | 生物识别安全性（12）；配备触控 ID 的妙控键盘（10） |
| `图像` | 32 | 14 | 生物识别安全性（6）；视控 ID 匹配安全性（5） |
| `接口` | 32 | 17 | Apple 设备的无线局域网隐私（4）；IPv6 安全性（4） |
| `沙盒` | 27 | 11 | macOS 中的 Option ROM 安全性（6）；iOS、iPadOS 和 visionOS 中的运行时进程安全性（4） |
| `公证` | 26 | 6 | 在 macOS 中防范恶意软件（14）；macOS 中的 App 代码签名进程（4） |
| `摄像头` | 26 | 7 | HomeKit 摄像头安全性（14）；生物识别安全性（4） |
| `URL` | 20 | 8 | TLS 安全性（5）；单点登录安全性（4） |
| `数据库` | 20 | 8 | iCloud 加密（6）；保护对用户健康数据的访问（4） |
| `界面` | 17 | 10 | iOS 和 iPadOS 中的 ReplayKit 安全性（5）；密码安全建议（3） |
| `麦克风` | 16 | 6 | 硬件麦克风断联（10）；iOS 和 iPadOS 中的 ReplayKit 安全性（2） |
| `USB` | 15 | 13 | macOS 的 DriverKit 安全性（2）；iPad 和 iPhone 的物理配对模型安全性（2） |
| `快捷指令` | 15 | 3 | “快捷指令” App 中的安全功能（13）；服务安全性概览（1） |
| `攻击面` | 12 | 9 | 操作系统完整性（3）；基于 Intel 的 Mac 的 recoveryOS 和诊断环境（2） |
| `用户空间` | 12 | 5 | 操作系统完整性（3）；在 macOS 中安全扩展内核（3） |
| `消息` | 9 | 4 | 蓝牙安全性（6）；安全隔区（1） |
| `剪贴板` | 7 | 1 | 接力安全性（7） |
| `WebKit` | 5 | 4 | 单点登录安全性（2）；TLS 安全性（1） |
| `媒体` | 5 | 4 | 保护 App 对用户数据的访问（2）；HomeKit 摄像头安全性（1） |
| `脚本` | 3 | 2 | “快捷指令” App 中的安全功能（2）；通过 Apple 登录安全性（1） |
| `归档` | 2 | 2 | 访问钥匙类型（1）；Apple 操作系统中的量子安全加密技术（1） |
| `暴露` | 2 | 2 | 关于 App Store 安全性（1）；加密和数据保护概览（1） |
| `压缩` | 0 | 0 | — |
| `可达` | 0 | 0 | — |
| `图片` | 0 | 0 | — |
| `字体` | 0 | 0 | — |
| `档案` | 0 | 0 | — |
| `表面` | 0 | 0 | — |

# 附表 D 复算验收断言集（15 行）

以下 15 行与第 16 章验证一至验证十五一一对应，但本表按"不通过时先查什么"重排，供复算者定位。第 4、15 两项是本篇写作过程中真实翻过车的地方。

| # | 断言 | 本期实测 | 不通过时先查什么 |
|---|---|---|---|
| 1 | 输入并集占含 Impact 段 43.7%、占全树段 35.5%，名 201 / 472 | 3,245 / 7,434 / 9,136 | 分母是否误用域 A 的 7,346 或 9,045 |
| 2 | 八类之和 = 并集、未归类 = 0，附表 A 纵向 66 行合计 = 3,245 | 1,153+1,107+392+323+49+147+68+6 | 判序是否仍是 I1→I8；`其余 N 个块名` 行是否被漏加 |
| 3 | 单类 2,920 + 多类 325 = 3,245，且 13 组组合之和也 = 325 | 成立 | 是否把"命中类数"记成了"命中段数" |
| 4 | 施动者名词并集 **6,613**，与正确值差 **3,368**，其中 **2,778** 段以 `an app may` 起头 | 成立 | 词表里是否混进 `app` / `application` / `shortcut` / `extension`（本篇初稿即错于此） |
| 5 | `input` 词族 38 段，仅 9 段在并集内；套语 `An input validation issue was addressed` 20 段 | 成立 | 是否把 Description 侧的 730 段算进 Impact |
| 6 | 附表 B 纵向 39 行合计 = crafted 窗口 **1,777**；头部三词 793+295+167 = **1,255**（70.6%） | 成立 | 窗口截断是否用了"每词后必须跟空格"的旧写法 |
| 7 | `processing` 词族 1,889 ≥ 严格 1,769 ≥ 起句 1,760 ≥ 起句∩并集 1,723 | 单调不增 | 边界正则是否漏了复数与 `-ing`；裸词干 `process\w*` 会多收 |
| 8 | A 5,056 + B 2,179 + C 199 = **7,434** = 含 Impact 段数 | 成立 | B 类首词表里是否重复列入 `visiting`（曾致 2,182） |
| 9 | 15 个接口词 Impact 侧 **0 段**；`interface` 29 / `socket` 8 / `URL` 7 / `API` 5 | 成立 | 是否把 Description 侧命中并入 Impact 计数 |
| 10 | `Kernel` 命中率 62 / 745 = 8.3%，且 62 段内输入词次之和 92 > 62 | 成立 | 命中率分母是否用了页数（段/词次不可互推） |
| 11 | 零输入名 271 + 有输入名 201 = 472；271 名覆盖 1,942 段 / 173 页 | 成立 | 块名归一化（大小写、尾句点）是否一致 |
| 12 | `sandbox` 词族 418 ∩输入 79；`break out of its sandbox` 240 ∩输入 **0**；258 = 240 + 18 | 成立 | 258 与 240 是否被当作同一个数引用 |
| 13 | `bypass` 全树 291 / 域 A 288 / 并集内 72，#110 引用的是 288 | 成立 | 是否声明了域；两值差 3 段来自 11 页旧版式 |
| 14 | 无输入段的页恰 2 页（120887、123356），255 + 2 = 257 | 成立 | 排除 `122085` 的规则是否被执行 |
| 15 | 域 B 输入 35 段 / 7 名，`WebKit` 独占 27；`NSOpenPanel`、`LLVM` 仅见于域 B | 成立 | 解析器是否只认 `<h3 class="gb-header">` 起块（若是，这 35 段整体消失） |

# 附表 E 错误写法与纠正对照（8 行）

与第 19 章误读一至误读八逐条对应；左列是本篇写作过程中真实出现过的草稿句式。

| # | 会被写出的错误句子 | 纠正后的写法 |
|---|---|---|
| 1 | "App 也是一种外部输入，输入面共约 6,600 段" | 6,613 中 3,368 段只因施动者名词进入，其中 2,778 段是 `an app may` 起头的行为描述；施动者与受事必须分表，正确并集 3,245 |
| 2 | "媒体输入面 392 段，比网络输入面 323 段大，所以媒体更危险" | 两类分别由 31 个与 34 个块名构成，`ImageIO` 一词独占 I3 的 184 段；类大小由词表宽度决定，八类之间不可比大小 |
| 3 | "271 个组件不接收外部输入" | 只能说这 271 个块名在 1,942 段的措辞里没写输入对象；`Calendar`、`Find My`、`CloudKit` 的条目同样不写，但没有任何文本证明它们不可触达 |
| 4 | "`processing` 出现的 1,889 段就是处理外部数据的段数" | 1,889 是词族、1,769 是严格形、1,760 是起句、1,723 是起句且在并集内；四个数各自对应不同断言，引用须点名口径 |
| 5 | "Apple 记录了 39 种外部输入类型" | 39 是 crafted 之后宾语的 distinct，规则敏感（放宽截断变 86），且只覆盖 1,777 个窗口，不覆盖并集内其余 1,468 段 |
| 6 | "接口面有 29 段（`interface`）" | 29 段里的 `interface` 多为 `network interface`（设备义）；`endpoint` / `protocol` / `XPC` / `Mach` 全为 0，接口集合在本语料里建不起来 |
| 7 | "`input` 只有 38 段，说明 Apple 不谈外部输入" | 总名缺席但对象充分：`web content` 793 个窗口、`file` 295、`image` 167；与 #111 的"7 段 `trust` ≠ 不谈信任"同理 |
| 8 | "输入面 3,245 段 + 越级面 602 段 = 3,847 段涉及越权输入" | 两集合成员交集 124，并集应为 3,245 + 602 − 124 = 3,723；且输入 ∩ `kernel privileges` = 0，措辞层没有"文件进来→拿到 root"的单句证据 |

---

**边界声明：** 本篇全部内容是对 Apple 公开安全响应顾问页英文镜像（257 份、9,136 段，两个版式域）与《Apple 平台安全保护》中文指南转储（178 页可用正文域）的词频、字段位置、句式模板、块名归属与集合交并的**文本计量分析**，用于说明公开文本"把什么写成外部输入"与"哪些层面根本没有成文"。本篇不提供任何触发缺陷、构造恶意输入、绕过输入校验或访问控制的方法，不含样本构造、不含利用代码、不含针对他人设备的操作步骤与时序，也不给出任何具体缺陷的可复现路径。文中出现的组件名与段落均为对公开文本的引用与计数，不构成对任何产品暴露面的判定；"零命中"只表示措辞层未写，不表示功能上不可达。所有百分比与段数只描述本篇镜像与切分口径下的样本，若上游文本更新，全部数值需按第 17 章复算。

Telegram: https://t.me/one00190
