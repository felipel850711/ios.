@@TITLE@@iOS 推送与中继通道的安全机制分析
@@SUBTITLE@@一个词在一层是 70 次、在另一层是 4 段，而且两层的"中继"根本不是同一个词：《Apple 平台安全保护》中文全量 179 页正文域里"推送"15 次 / 13 页、"APNs"24 次 / 8 页、"通知"50 次 / 31 页、"中继"22 次 / 13 页、"令牌"70 次 / 15 页，并且写出了"APNs 最多只能转发大小为 4KB 或 16 KB 的信息""时间戳和 APNs 路由信息等元数据则不加密""信息在发出之后就会从 APNs 中删除"这类可核对的机制级原句；而 Apple 安全响应顾问页本期 257 份镜像（容错解析后全语料 9,045 段 / 470 个段落名）的字段文本里，`push` **0 段**、`device token` **0 段**、`voip` **0 段**、`badge` **0 段**、`alert` **0 段**、`sound` **0 段**、`APNs` 作为词 **0 段**；`token` 全语料只有 **4 段**，全部是同一句 `An app may be able to access sensitive payment tokens`、全部挂在同一个段落名 `App Store` 下、全部对应同一个 CVE 编号，而且讲的是**支付令牌不是设备令牌**
@@INFO@@报告日期|2026 年 10 月 26 日
@@INFO@@分析对象|《Apple 平台安全保护》zh-CN 指南 179 页全量转储（正文域切分后逐词普查）；Apple 安全响应顾问页 257 份英文页面镜像（容错解析后全语料 9,045 段 / 470 个不同段落名），本篇推送—通知—中继—同步家族 350 段 / 19 个段落名 / 70 个 CVE 编号 / 135 页，其中通知族 53 段、中继与共享族 191 段、消息与通话族 106 段；另单列 `relay` 词族 20 段 / 5 个段落名 / 17 页 / 6 个 CVE 编号，与本家族交集为 **0**
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含针对他人设备的操作步骤、不含推送凭据抓取或重放细节、不含中继链路的流量分析或脱敏规避方法
@@INFO@@分类|投递通道面 / 官方手册层的机制词汇与顾问页的后果词汇的**双向错名**对照 / 一次"按名字取族"与"按词取族"的交集实测 / 同一中文词形"中继"在两页里指两个不同对象
@@INFO@@难度|进阶（第 1 章的两层词表对照与第 7 章的"名字族 × 词族交集为 0"是全篇前提；跳过它们会把顾问页的段落名读成实现层，把手册层的 4KB 上限读成 APNs 的当前实现承诺）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

本篇处理一个比前几篇更极端的口径冲突：**"推送"这件事在 Apple 的两套公开文本里，一层写得非常具体、另一层几乎不用这个词**。

手册层（《Apple 平台安全保护》，下称 T1）有完整的推送词汇，而且给出了可核对的规格句子。中文全量 179 页正文域："推送"15 次分布 13 页，"推送通知"9 次 / 8 页，"通知服务"6 次 / 6 页，`APNs` 24 次 / 8 页，"通知"50 次 / 31 页。原句里出现了容量、生命周期与元数据三类具体断言：`APNs 最多只能转发大小为 4KB 或 16 KB 的信息，具体取决于 iOS 或 iPadOS 的版本`；`与所有推送通知一样，信息在发出之后就会从 APNs 中删除。然而与其他 APNs 通知不同的是，如果设备不在线，iMessage 信息会列入队列等待发送。信息在 Apple 服务器上最长储存 30 天`；`时间戳和 APNs 路由信息等元数据则不加密。与 APNs 的通信使用前向保密 TLS 频道加密`。术语表还给出定义式条目：`Apple 推送通知服务 (APNs) 一项由 Apple 提供的全球服务，用于向 Apple 设备传送推送通知`，以及 `Apple 身份服务 (IDS) Apple 的 iMessage 信息公钥、APNs 地址和电话号码及电子邮件地址目录`。

措辞层（Apple 安全响应顾问页，下称 T2）反过来：全语料 9,045 段的 Impact / Description 字段文本里 **`push` 出现 0 段、0 页、0 个段落名**；`device token`、`voip`、`badge`、`alert`、`sound` 同样各 0 段。`APNs` 不是词，只是一个**段落名**，且只承载 **1 段**（页 121238，Released September 16, 2024，CVE-2024-44130），那句 Impact 是 `An app with root privileges may be able to access private information`——与推送通道本身无关，是一条特权应用访问私有信息的条目。

"中继"更麻烦，因为它在中文一层里同时指**两个不同对象**：一页叫「iPhone 蜂窝网络通话中继安全性」，写的是来电经 APNs 通知所有已配置设备；另一页写 iCloud 网络路径，用的却是"代理"而不是"中继"——`它使用了不同的代理：由 Apple 管理的入口代理，以及由内容提供商管理的出口代理`。本报告实测：`私密中继` 与 `私密代理` 两种常见译法在 179 页正文域**各 0 次**，正式词形是 `iCloud 专用代理`（8 次 / 3 页）；拉丁词形 `Private Relay`、`AirPlay`、`Push`、`Screen Sharing`、`notification` 在中文正文域**各 0 次**。

于是本篇的五个问题依次是：

**问题一（第 1–3 章）** 两层的推送词表差额到底有多大，这个差额是不是采集缺陷造成的假差额？答案：不是采集缺陷。T2 全语料确有 34 段 / 29 页 / 10 个段落名使用 `notification` 一词，本篇逐名核对后确认这 34 段讲的是"通知内容被看到 / 通知标记被保留"这类**界面与数据可见性**后果，不是投递通道。同时给出覆盖性证明：`push` 的 0 不是解析器没看见最新页面造成的——同一把刀在最新 10 页照常切出 45 段本家族段落。

**问题二（第 4–5 章）** 手册层对推送通道与中继通道分别写到什么粒度，哪些句子可以直接引用、哪些只是页面级承诺？本篇把 APNs 的六条机制原句与专用代理的三条原句并置，并登记一处中文词形冲突。

**问题三（第 6–7 章）** T2 里唯一真正与"中继"同名的词族 `relay` 有 20 段，这 20 段挂在哪些段落名下？它们与本篇按名字取出的 350 段家族有没有交集？答案是全篇最干净的一条负结果：**交集为 0**。`relay` 的 20 段分布在 `WebKit` 10、`Safari` 4、`Authentication Services` 4、`Networking` 1、`Kernel` 1 五个名下，17 页、6 个 CVE 编号、4 种 Impact 句式、4 种 Description 句式；而本篇家族 19 个段落名（含 `APNs`、`Notifications`、`Notification Services`、`Messages`、`FaceTime`、`CloudKit`、`iCloud`、`AirPlay`）一段都不含 `relay`。

**问题四（第 8–12 章）** 把家族 350 段按字段穷尽、按修复句式分区、按在野声明求交，能得到什么、不能得到什么？可得到的三条：350 段完全分区为 251 段带字段 + 99 段纯致谢；三组 CVE 编号之和恰等于去重并集（13 + 38 + 19 = 70，跨组共享 **0**）；`Apple is aware of a report` 全语料 93 段与本家族的交集只有 **9 段**，且 9 段全部是同一个 CVE 编号、同一个句式、9 个不同页面。

**问题五（第 13–17 章）** 本篇能否支持"推送通道方向没有漏洞"或"中继方向风险低"？不能。第 13 章给出方向明确的不可外推清单：`push` 0 段只说明**措辞层不使用这个词**；家族 53 段通知族 0 段在野，不能外推成"投递通道从未被在野利用"，因为同族措辞的 `relay` 20 段里就有 `A remote attacker may be able to view leaked DNS queries with Private Relay turned on` 这种远程可达句式，而它落在 `WebKit` 名下、被第 7 章的交集排除在本家族之外。

**写作边界（贯穿全篇）**：T2 的段落名是 Apple 的**归类动作**，不是实现层证据。`Notification Services` 名下 5 段的 Impact 是"标记为删除的通知可能被意外保留在设备上"，本篇只写"顾问页把这条后果归在 `Notification Services` 名下"，不写"APNs 或某个通知服务进程保留了该记录"。`APNs` 名下 1 段更不能读成"APNs 服务端存在 CVE-2024-44130"——该页是设备侧系统更新条目。源码层（XNU 与用户态开源部件）本期镜像为空，凡涉及实现的表述一律降级为"未核对"，并在第 18 章登记为缺口。

---

# 1 两层的推送词表：一层 15 次，一层 0 段

本章把"差额有多大"钉成可复算的两张表。

## 1.1 手册层（T1）：正文域逐词普查

语料 = 《Apple 平台安全保护》zh-CN 指南全量转储 179 页。正文域切分规则与 #104–#107 相同：取最后一个逐字 `版权和商标` 行之后、首个含 `Apple Footer` 行之前；179 页全部命中该切分（NO_MARKER = 0）。计数对象是正文域，不含页头导航与页脚免责声明。

| 中文词形 / 拉丁词形 | 次数 | 命中页数 | 说明 |
|---|---|---|---|
| 推送 | 15 | 13 | 全部以"推送通知"或"推送"作定语出现 |
| 推送通知 | 9 | 8 | 术语表出现 2 次，其余 7 页各 1 次 |
| 通知服务 | 6 | 6 | 与 APNs 连用，如"Apple 推送通知服务 (APNs)" |
| APNs | 24 | 8 | 单页最高 9 次（iMessage 信息如何安全发送和接收信息） |
| 通知 | 50 | 31 | 最广，但多数是"通知界面 / 锁屏通知"语境 |
| 中继 | 22 | 13 | 其中"通话中继"4 次 / 4 页 |
| 短信转发 | 7 | 4 | 「iPhone 短信转发安全性」单页 4 次 |
| iCloud 专用代理 | 8 | 3 | 单页最高 5 次（专用代理专页） |
| 私密中继 | 0 | 0 | 常见译法，**手册层不用** |
| 私密代理 | 0 | 0 | 同上 |
| 隔空播放 | 5 | 3 | AirPlay 的中文正式词形 |
| AirPlay | 0 | 0 | 拉丁词形在中文正文域 0 次 |
| 屏幕共享 | 1 | 1 | 仅出现在「单点登录安全性」一处 |
| Screen Sharing | 0 | 0 | 拉丁词形 0 次 |
| Private Relay | 0 | 0 | 拉丁词形 0 次 |
| Push | 0 | 0 | 拉丁词形 0 次 |
| notification | 0 | 0 | 拉丁词形 0 次 |
| 令牌 | 70 | 15 | 单页最高 18 次（在 macOS 中管理文件保险箱） |
| iMessage | 73 | 24 | 通道语境主要载体 |
| FaceTime | 24 | 10 | 同上 |

三条要点：

**要点一：标题级页面只有 2 个。** 在 179 页里，标题含本篇主题的页面只有「iPhone 蜂窝网络通话中继安全性」与「iCloud 专用代理安全性」两页；**没有** APNs 专页，**没有** 推送服务专页，**没有** AirPlay 专页。APNs 的 24 次是分散在 8 页里的引用式出现，主要落在 iMessage 与 FaceTime 的机制页上。

**要点二：中文一层只用中文词形。** `Push`、`notification`、`AirPlay`、`Screen Sharing`、`Private Relay` 五个拉丁词形在正文域各 0 次，但对应中文词形都有出现。这条对复算很关键：按拉丁词形去搜中文手册会得到全 0，进而误判"手册层不谈推送"。

**要点三：手册层的粒度是规格级的。** 前 4 章会逐条列出，这里先给一句判断依据：一层文本愿意写出 `4KB 或 16 KB`、`最长储存 30 天`、`元数据则不加密`，这三项都属于**可核对的实现承诺**，而不是概述句。

## 1.2 措辞层（T2）：字段文本逐词普查

语料 = 257 份顾问页镜像（258 文件剔除 122085），容错解析后全语料 9,045 段 / 470 个段落名。计数对象限定为每个块的 Impact 与 Description 字段文本（附录 D 第 3 项给出字段抽取规则），致谢字段另计。

| 词形（小写匹配） | 段数 | 页数 | 段落名数 | 出现次数 |
|---|---|---|---|---|
| push | 0 | 0 | 0 | 0 |
| device token | 0 | 0 | 0 | 0 |
| voip | 0 | 0 | 0 | 0 |
| badge | 0 | 0 | 0 | 0 |
| alert | 0 | 0 | 0 | 0 |
| sound | 0 | 0 | 0 | 0 |
| cloudkit | 0 | 0 | 0 | 0 |
| sync | 0 | 0 | 0 | 0 |
| privately relay | 0 | 0 | 0 | 0 |
| man-in-the-middle | 0 | 0 | 0 | 0 |
| eavesdrop | 0 | 0 | 0 | 0 |
| notification | 34 | 29 | 10 | 39 |
| relay | 20 | 17 | 5 | 20 |
| token | 4 | 4 | 1 | 4 |
| icloud private relay | 2 | 2 | 2 | 2 |
| airplay | 7 | 7 | 1 | 7 |
| screen sharing | 10 | 10 | 2 | 10 |
| facetim | 20 | 15 | 4 | 20 |
| messages | 19 | 18 | 6 | 20 |
| icloud | 26 | 22 | 8 | 26 |
| pairing | 8 | 8 | 2 | 8 |
| lock screen | 66 | 44 | 17 | 66 |
| remote attacker | 194 | 89 | 26 | 194 |
| Apple is aware | 93 | 65 | 12 | 93 |

四条要点：

**要点一：`push` 是硬 0。** 不是"段落名里没有 push"，而是**字段文本 9,045 段里一次都没有**。这一条比 #105 的"USB Restricted Mode 只有 5 段"更极端，因为那 5 段毕竟存在。

**要点二：`notification` 存在，但不等于投递通道。** 34 段分布在 10 个段落名下，命中最多的两个名是 `Accessibility`（7 段）与 `Share Sheet`（7 段），二者都不在本篇家族 19 名之内；本篇家族内的 `notification` 命中只有 `Notification Services` 5、`Notification Center` 4、`Libnotify` 2、`Notifications` 1、`FaceTime` 3。逐段核对后，这些句子的谓语全是"访问 / 看到 / 保留 / 冒充"，没有一句写"消息未能送达"或"投递被劫持"。

**要点三：`token` 的 4 段是支付令牌。** 四段全部是 `An app may be able to access sensitive payment tokens`，全部在 `App Store` 名下，全部对应 CVE-2025-46288，分布在 125884、125886、125890、125891 四页；修复句式统一为 `A permissions issue was addressed with additional restrictions.`。手册层的"令牌"70 次 / 15 页里，绝大多数指的是安全令牌 / 密钥包 / 单点登录令牌，与设备推送注册令牌也不是一回事。

**要点四：`cloudkit` 与 `sync` 作为词 0 段，但作为段落名各有一族。** `CloudKit` 名下 30 段、`Sync Services` 名下 12 段。这条是第 7 章"名字族 × 词族交集"的直接样本：词查不到、名查得到，说明 T2 的通道识别**只走段落名这一维度**。

## 1.3 差额的方向

T1 有词、有规格、没有条目；T2 有条目、有后果句式，但**不用"push"这个词**。两层的差额不是数量差，而是**维度差**：T1 描述通道，T2 描述缺陷后果，而 T2 用来指称"某条通道"的唯一手段是段落名。这决定了本篇后面所有取数都必须同时给"按词"与"按名"两套结果——第 7 章正是这套方法的对照实验。

---

# 2 措辞层的否定清单：0 是测量结果，不是采集缺陷

否定结果必须附覆盖性证明，这是 #104 起确立的纪律（附表 D 第 10 项）。本章把本节所有 0 逐条给出对照证据。

## 2.1 覆盖性对照

同一次遍历（同一把切块刀、同一份 257 文件清单、同一段字段抽取规则）在这十条最新页面上切出的段落名与段数如下（`latestBatch` = 该段落名在 149034–149043 十页内出现的段数）：

| 段落名 | 家族内段数 | 家族内页数 | 最新 10 页段数 |
|---|---|---|---|
| Notifications | 11 | 11 | 4 |
| Notification Center | 14 | 10 | 0 |
| Notification Services | 5 | 5 | 0 |
| Libnotify | 20 | 20 | 0 |
| Safari Push Notifications | 2 | 2 | 0 |
| APNs | 1 | 1 | 0 |
| Home | 1 | 1 | 0 |
| iCloud Document Sharing | 6 | 6 | 0 |
| CloudAttestation | 9 | 9 | 0 |
| AirPlay | 84 | 14 | 0 |
| FaceTime Foundation | 3 | 3 | 0 |
| Screen Sharing Server | 17 | 8 | 3 |
| Screen Sharing | 4 | 4 | 1 |
| Sync Services | 12 | 12 | 2 |
| CloudKit | 30 | 22 | 13 |
| iCloud | 22 | 16 | 8 |
| iCloud Photo Library | 6 | 6 | 1 |
| Messages | 77 | 67 | 9 |
| FaceTime | 26 | 24 | 4 |

三条结论：

**结论一：解析器看得见最新页面。** 同一趟遍历在 `Kernel` 名下切出 180 段、在 `WebKit` 名下切出 50 段（均在 149034–149043 内），家族本身在最新十页合计 45 段。因此"某族 0 段"只能解释为**语料里确实没有**，不能解释为没看见。

**结论二：10 / 19 个家族名在最新十页为 0 段。** 这是本篇家族的真实新鲜度分布，第 15 章据此限定所有"当前状态"表述。`AirPlay` 的 84 段最新只到 122378，`Notification Center` 只到 126348，`APNs` 只到 121238。

**结论三：段落名的存续方式不同。** `Messages` 77 段分布 67 页（近乎每页一段），`AirPlay` 84 段只分布 14 页（单页平均 6 段）。这两个数字必须一起看：**段数相同不代表覆盖面相同**，把 84 段读成"AirPlay 有 84 个缺陷"会同时踩两条禁令（第 14 章）。

## 2.2 字段文本口径的说明

本章的 0 只对"字段文本"成立。若把致谢字段并入计数，`notification` 一词的出现面会扩大（例如 `CVE-2025-43309: ...` 这类致谢行的文本含 `Notifications` 字样），但 `push`、`device token`、`voip`、`badge` 仍然 0 段——为保守起见，本篇所有词频断言一律限定在 Impact / Description 字段，并在附表 D 第 3 项写明该口径。

---

# 3 `APNs` 作为段落名：1 段、1 页、1 个 CVE，且说的不是推送

T2 全语料 470 个段落名里确实有 `APNs` 这一个名字，但它承载的内容与本主题几乎无关。逐字段抄录：

| 字段 | 原文 |
|---|---|
| 页号 | 121238 |
| 页面 `Released` 字段 | Released September 16, 2024 |
| 段落名 | APNs |
| CVE 编号 | CVE-2024-44130 |
| Impact | An app with root privileges may be able to access private information |
| Description | This issue was addressed with improved data protection. |
| 块内备注 | 无 |

四条观察：

**观察一：Impact 主语是 app，不是 service。** 这句后果描述的是"具有 root 权限的应用"访问私有信息，属于本地提权后果族（#101 全量登记过 `An app with root privileges may be able to access private information` 的分布）。把它读成"APNs 服务可被 root 应用访问"属于越界。

**观察二：段落名可能是"受影响组件"而不是"缺陷所在子系统"。** 顾问页把这条缺陷归在 `APNs` 名下，能支持的表述止于"Apple 把该缺陷归类到 APNs 组件"。第 14 章把这条列为本篇最需要设限的一项。

**观察三：修复句式 `improved data protection` 在家族里是孤例。** 家族 251 段带 Description 的分区表（第 11 章）里，`improved data protection` 全族只出现 1 次，就是这一段。

**观察四：`APNs` 名下 1 段，而手册层 APNs 24 次 / 8 页。** 这个 1 : 24 的对照就是本篇主轴的最小样本：一层把它当机制词汇反复使用，另一层只把它当组件标签用过一次。

---

# 4 手册层的 APNs 机制原句：容量、生命周期、元数据三类承诺

以下六条全部取自 T1 中文正文域，句内用词与语序保持原文；原文中的方括号链接标记（形如 `[文本](URL)`）已统一还原为纯文本，行号为转储文件行序，非页面显示行。

**S1（容量上限）** 「iMessage 信息如何安全发送和接收信息」`_tmp/sec70e68c949.txt` 第 240 行：
`APNs 最多只能转发大小为 4KB 或 16 KB 的信息，具体取决于 iOS 或 iPadOS 的版本。如果信息文本过长，或者附件中有照片等文件，那么附件会使用 AES 在 CTR 模式下通过随机生成的 256 位密钥进行加密并上传至 iCloud。`

**S2（发送即删除 + 例外队列）** 同页第 243 行：
`与所有推送通知一样，信息在发出之后就会从 APNs 中删除。然而与其他 APNs 通知不同的是，如果设备不在线，iMessage 信息会列入队列等待发送。信息在 Apple 服务器上最长储存 30 天。`

**S3（元数据不加密 + 前向保密）** 同页第 239 行：
`针对每部接收设备所生成的每条信息包含加密的信息文本、加密的信息密钥和发送者的数字签名。信息然后会分派至 APNs 以进行发送。时间戳和 APNs 路由信息等元数据则不加密。与 APNs 的通信使用前向保密 TLS 频道加密。`

**S4（地址目录）** 「iMessage 信息安全性概览」`_tmp/secd9764312f.txt` 第 238 行：
`公钥则与设备的 APNs 地址一起发送至 Apple 身份服务 (IDS)，在身份识别服务中，公钥会与用户的电话号码或电子邮件地址关联在一起。`

**S5（术语表定义）** 「术语表」`_tmp/sec93292bfa6.txt` 第 285 行：
`Apple 推送通知服务 (APNs) 一项由 Apple 提供的全球服务，用于向 Apple 设备传送推送通知。`
同页第 284 行：
`Apple 身份服务 (IDS) Apple 的 iMessage 信息公钥、APNs 地址和电话号码及电子邮件地址目录，用于查找密钥和设备地址。`

**S6（通话中继经 APNs 通知）** 「iPhone 蜂窝网络通话中继安全性」`_tmp/sec28a79bf17.txt` 第 238 行：
`来电时，会通过 Apple 推送通知服务 (APNs) 来通知所有已配置的设备，每个通知都会使用与 iMessage 信息相同的端对端加密技术。`
同页第 240 行：
`去电也会使用 APNs 中继到 iPhone，并通过安全的点对点链接在设备间采用类似的方式传输音频。用户可以在 FaceTime 通话设置中关闭"iPhone 蜂窝网络通话"来关闭设备的电话中继功能。`

三条读法限制：

**限制一：S1 的 4KB / 16 KB 是版本限定的。** 句子自带"具体取决于 iOS 或 iPadOS 的版本"，引用时必须保留该限定，不能写成"APNs 单条上限 4KB"。手册层没有给出对应的版本映射表（G4）。

**限制二：S2 是 iMessage 例外，不是 APNs 通用策略。** 句子主干是"与所有推送通知一样……然而与其他 APNs 通知不同的是……"。能写的只有：投递后删除是通用行为、iMessage 有 30 天队列例外。**不能**把 30 天推广为"Apple 保存推送内容 30 天"。

**限制三：S3 的"元数据则不加密"是本篇最重要的可引声明，但它限定了字段名。** 手册层点名"时间戳和 APNs 路由信息"，没有列出完整元数据清单，也没有说明接收方设备标识是否包含在内（G5）。T2 侧全语料字段文本没有一句涉及推送元数据机密性——`eavesdrop` 0 段、`man-in-the-middle` 0 段、`encrypt` 在本家族 0 段。

---

# 5 "中继"在中文一层指两个对象：通话中继 / 短信转发 vs 专用代理

本章处理一个术语冲突，因为它直接影响按词取族的结果。

## 5.1 两个对象，两种词形

| 页面 | 用词 | 指称对象 | 机制要点（逐字） |
|---|---|---|---|
| iPhone 蜂窝网络通话中继安全性 | 中继 | 把 Mac / iPad 上的 iPhone 来电接到其他设备 | `来电时，会通过 Apple 推送通知服务 (APNs) 来通知所有已配置的设备` |
| iPhone 短信转发安全性 | 转发 | 把 SMS/OTP 转发到已配对 Mac | 「短信转发」4 次 / 该页；在 179 页共 7 次 / 4 页 |
| iCloud 专用代理安全性 | **代理** | Safari 出网的入口 / 出口两段路径 | `它使用了不同的代理：由 Apple 管理的入口代理，以及由内容提供商管理的出口代理` |
| 接力安全性 / 连续互通安全性概览 | 中继 | 跨设备接续与通话/信息在设备间转递 | `中继` 在 179 页 22 次 / 13 页 |

三条实测：

**实测一：常见译法不存在。** `私密中继` 0 次 / 0 页，`私密代理` 0 次 / 0 页。正式词形是 `iCloud 专用代理`（8 次 / 3 页，其中「iCloud 专用代理安全性」单页 5 次）。

**实测二：同一个英文词在两页里落到不同中文词形。** `relay` 在通话页译作"中继"、在浏览页译作"代理"。因此**任何按"中继"一词做的中文普查都会漏掉 Private Relay**，反之按"专用代理"普查会漏掉通话中继。这是手册层版的"名字族 × 词族"问题，与第 7 章的 T2 对照同构。

**实测三：专用代理页给出的可核对断言只有三条。** 完整引用：
「iCloud 专用代理安全性」`_tmp/secad8ce3233.txt` 第 237 行：
`iCloud 专用代理主要帮助在用户使用 Safari 浏览器浏览网页时进行保护，还会涉及所有 DNS 域名解析请求。这有助于确保没有任何一方（包括 Apple）可以关联用户的 IP 地址及其浏览活动。`
第 238 行：`有关更多信息，请参阅 iCloud 专用代理概览。`（外链指向 PDF 白皮书，本轮未取回，登记 G7）
另有版本门槛句：`若要使用 iCloud 专用代理，用户设备必须运行 iOS 15 、 iPadOS 15 、 macOS 12.0.1 、 visionOS 1 或更高版本`。

## 5.2 手册层没有写的东西

按"逐词查、查不到就登记缺口"的纪律，以下四项在 179 页正文域**没有对应段落**：入口代理与出口代理之间具体交换什么字段；两段路径各自的信任假设与协议；专用代理的 DNS 处理与"所有 DNS 域名解析请求"这一断言的例外清单；设备令牌（push token）在 IDS 目录中的存储位置与生命周期。四项全部并入第 18 章缺口清单（G6–G9）。

---

# 6 措辞层的 `relay` 词族：20 段全分解

这是 T2 唯一成规模表达"中继"的词族。20 段完整分解如下（字段逐字，页号 + 段落名 + CVE + 两句字段）。

## 6.1 按段落名

| 段落名 | 段数 | CVE 编号 | Impact 句 | Description 句 |
|---|---|---|---|---|
| WebKit | 7 | CVE-2025-43376 | A remote attacker may be able to view leaked DNS queries with Private Relay turned on | A logic issue was addressed with improved state management. |
| WebKit | 3 | CVE-2026-65352 | A website may be able to determine a user's IP address with Private Relay turned on | An information disclosure issue was addressed with improved state management. |
| Authentication Services | 4 | CVE-2026-65355 | A website may be able to determine a user's IP address with Private Relay turned on | An information disclosure issue was addressed with improved state management. |
| Safari | 4 | CVE-2024-44246 | On a device with Private Relay enabled, adding a website to the Safari Reading List may reveal the originating IP address to the website | The issue was addressed with improved routing of Safari-originated requests. |
| Kernel | 1 | CVE-2025-43276 | iCloud Private Relay may not activate when more than one user is logged in at the same time | A logic error was addressed with improved error handling. |
| Networking | 1 | CVE-2025-43506 | iCloud Private Relay may not activate when more than one user is logged in at the same time | A logic error was addressed with improved error handling. |

逐行相加 7 + 3 + 4 + 4 + 1 + 1 = 20，与段落名级汇总（WebKit 10、Authentication Services 4、Safari 4、Kernel 1、Networking 1）完全闭合，无剩余。

## 6.2 句式收敛度

- 20 段 → **4 种** Impact 句（7 / 7 / 4 / 2 = 20）；
- 20 段 → **4 种** Description 句（7 / 7 / 4 / 2 = 20，与 Impact 一一对应）；
- 20 段 → **6 个** CVE 编号；
- 20 段 → **17 个** 页面（121837、121838、121839、121846、124149、125108、125110、125113、125114、125115、125116、125634、126793、148281、148282、148287、148353）。

## 6.3 三条结构事实

**事实一（同句跨名）：`A website may be able to determine a user's IP address with Private Relay turned on` 横跨两个段落名、两个 CVE。** 4 段在 `Authentication Services`（CVE-2026-65355）、3 段在 `WebKit`（CVE-2026-65352）。同一句后果被 Apple 在两个组件名下分别登记。这与 #104 记录的 `An app may be able to access sensitive user data` 跨 5 名 22 段是同一类现象，本篇在第 9 章给出该句的最新复测值。

**事实二（同句跨名，且是"不生效"句式）：`iCloud Private Relay may not activate …` 落在 `Kernel` 与 `Networking` 两名、两个 CVE，Impact 句完全相同。** 这是全语料里少数以"某项保护没有启用"为主语的 Impact 句之一：它描述的不是数据泄露，而是**防线未生效**。该句式在 T2 全语料只出现这 2 段（`relay` 词族内），本篇未见其它组件复用（附表 D 第 6 项可复算）。

**事实三：`relay` 20 段的修复句式全部不与"认证"相关。** 4 种 Description 里 2 种是 state management、1 种是 routing、1 种是 error handling。对照 #107 的发现（授权类家族里 `authorization issue` 13 段全部与 state management 同句出现），本节给出的是一条边界事实：`relay` 词族的 state management 有 14 段（7 + 7）来自 `logic issue` 与 `information disclosure` 两种缺陷句式，**没有一段**来自 `An authentication issue was addressed with improved state management.`。

---

# 7 按名字取族 × 按词取族：本篇的交集为 0

第 1.2 节给出 `relay` 在 5 个段落名下、第 6 节给出 20 段的分解。本章做本篇的方法主体：把两个维度直接求交。

## 7.1 交并关系

| 集合 | 定义 | 段数 |
|---|---|---|
| 名字族 A | 19 个段落名的全部块（P 53 + R 191 + M 106） | 350 |
| 词族 B | 字段文本含 `relay` 的全部块 | 20 |
| A ∩ B | 既是家族名、字段又含 relay | **0** |
| B − A | 含 relay 但名字不在家族内 | 20（WebKit 10、Authentication Services 4、Safari 4、Kernel 1、Networking 1） |
| A − B | 家族名、字段不含 relay | 350 |

同一趟普查里另外四个词的交集也是 0：`cloudkit`（0 段词，但 `CloudKit` 名下 30 段）、`sync`（0 段词，但 `Sync Services` 名下 12 段）、`push`（0 段，`APNs` / `Safari Push Notifications` 名下 3 段）、`icloud private relay`（2 段，`Kernel` 与 `Networking` 名下，均不在 A）。

## 7.2 为什么这个 0 值得单独成章

如果只做"按名字取族"，本篇会得到一条**错误结论**："本篇家族 350 段里没有一段谈到中继失效，所以中继方向在 Apple 的缺陷登记里不存在。" 而 20 段确实存在，只是被归到了浏览器与网络栈名下。反过来，如果只做"按词取族"，会漏掉 `APNs`、`Notification Services`、`Messages`、`AirPlay` 等名下的 350 段——因为它们的字段文本从不使用 `push`、`relay`、`cloudkit`、`sync` 这些词。

由此得到本篇的方法句，也是我认为对整组报告最有复用价值的一条：

**在 T2 语料里，"通道"这一维度只存在于段落名，"后果"这一维度只存在于字段文本，两者不共享词汇。** 任何一侧单独取数都会系统性漏掉另一侧——#103 的蓝牙族、#104 的连续互通族、#105 的配件族、#107 的证书族都已实测到同一条规律，本篇的 A ∩ B = 0 是这条规律目前最强的一次证明，因为交集恰好是 0 而不是小数。

---

# 8 家族 350 段：分组、字段穷尽与页数

## 8.1 分组定义（本报告自建）

分组不是 Apple 的分类，是本报告为了对照而把 470 个段落名里与"投递 / 转递 / 同步 / 近端共享"直接相邻的 19 个名字划成三组：

| 组 | 名称 | 段落名 |
|---|---|---|
| P 通知与推送族 | 6 名 | APNs、Safari Push Notifications、Notification Services、Notifications、Notification Center、Libnotify |
| R 中继与共享族 | 10 名 | AirPlay、Screen Sharing Server、Screen Sharing、Sync Services、CloudKit、iCloud、iCloud Document Sharing、iCloud Photo Library、CloudAttestation、Home |
| M 消息与通话族 | 3 名 | Messages、FaceTime、FaceTime Foundation |

三条分组声明：**换分组会改变多个 0**（例如把 `WebKit` 拉进 R，第 7 章的交集立刻从 0 变 10）；`CloudAttestation` 与 `Home` 是按名字相邻性纳入的弱关联成员，二者合计 10 段，剔除后家族为 340 段、其余各章结论不变；`rapportd`、`Identity Services`、`Local Authentication`、`Remote Control`、`CoreDrag`、`DeviceCheck` 等 #104 登记过的相邻名本篇未纳入，以保持三组定义与 #104 的连续互通族互不重叠。

## 8.2 字段穷尽（逐行相加闭合）

| 组 | 段数 | 带 Impact / Description | 纯致谢 | 校验 |
|---|---|---|---|---|
| P | 53 | 29 | 24 | 29 + 24 = 53 |
| R | 191 | 158 | 33 | 158 + 33 = 191 |
| M | 106 | 64 | 42 | 64 + 42 = 106 |
| 家族合计 | 350 | 251 | 99 | 251 + 99 = 350 |

家族 350 段占全语料 9,045 段的 3.87%；99 段纯致谢占家族 28.3%（全语料的纯致谢比例是 1,699 / 9,045 = 18.8%）——**本家族的纯致谢比例明显高于全语料**，主要由 M 组（42 / 106 = 39.6%）与 P 组（24 / 53 = 45.3%）拉高。这条不是"风险低"的证据，只说明这两个名字下的登记条目里，"致谢型条目"占比更高（第 14.4 节解释为什么会出现这种块）。

## 8.3 段数 / 页数比：同一族内的两种分布形态

| 段落名 | 段数 | 页数 | 段/页 | 最新一段所在页 |
|---|---|---|---|---|
| AirPlay | 84 | 14 | 6.00 | 122378 |
| Messages | 77 | 67 | 1.15 | 149043 |
| CloudKit | 30 | 22 | 1.36 | 149038 |
| FaceTime | 26 | 24 | 1.08 | 149038 |
| iCloud | 22 | 16 | 1.38 | 149038 |
| Libnotify | 20 | 20 | 1.00 | 148287 |
| Screen Sharing Server | 17 | 8 | 2.13 | 149042 |
| Notification Center | 14 | 10 | 1.40 | 126348 |
| Sync Services | 12 | 12 | 1.00 | 149035 |
| Notifications | 11 | 11 | 1.00 | 149038 |
| CloudAttestation | 9 | 9 | 1.00 | 128070 |
| Notification Services | 5 | 5 | 1.00 | 127114 |
| iCloud Photo Library | 6 | 6 | 1.00 | 149034 |
| iCloud Document Sharing | 6 | 6 | 1.00 | 122721 |
| Screen Sharing | 4 | 4 | 1.00 | 149035 |
| FaceTime Foundation | 3 | 3 | 1.00 | 121845 |
| Safari Push Notifications | 2 | 2 | 1.00 | 127121 |
| APNs | 1 | 1 | 1.00 | 121238 |
| Home | 1 | 1 | 1.00 | 122371 |

关键对照：**AirPlay 84 段压在 14 页上（6.00 段/页），Messages 77 段摊在 67 页上（1.15 段/页）**。同一段落名在同一页可以出现多次（同一份更新里多个 CVE 都被归到该名下），因此段数既不是缺陷个数的下界，也不是上界。#106 已实测"同一段落名可在同页出现 3 次"，本篇把这条推到 6.00 段/页的极值。

---

# 9 一次罕见的完全分区，与一次不干净的句子分区

## 9.1 CVE 编号：三组完全不相交

| 口径 | 数值 |
|---|---|
| P 组去重后 CVE 编号数 | 13 |
| R 组去重后 CVE 编号数 | 38 |
| M 组去重后 CVE 编号数 | 19 |
| 三组之和 | 70 |
| 并集（去重后） | **70** |
| 出现在不少于 2 组的编号 | **0** |

也就是说：家族 350 段里的 70 个 CVE 编号，每一个只在一个组内出现，没有一条编号同时被归到通知族名与共享族名（或消息族名）下。这是分组质量的一条硬指标——按编号反查时，家族可以被三组无剩余地划分。

## 9.2 Impact 句式：分组不干净

| 口径 | 数值 |
|---|---|
| 家族内去重 Impact 句（并集） | 46 |
| 三组各自去重后相加 | 10 + 26 + 15 = 51 |
| 差额（跨不少于 2 组复用的句子数） | **5** |
| 全语料去重 Impact 句 | 592 |

同一条 0 与 5 的对照值得写清：**编号层面完全分区，句子层面不分区**。原因是 T2 的 Impact 句是一套共享模板池，同一句可以贴到任意组件名下；而 CVE 编号天然只属于一条条目，条目只挂在一个名下。因此"CVE 分区干净"不能被引用为"分组与 Apple 的组件归类一致"——这两件事没有因果关系，只是分组方式恰好落在编号的原子性上。

## 9.3 家族内复用最多的 Impact 句（含全语料对照）

| 家族段数 | 家族内名数 | 全语料段数 | 全语料页数 | 句子 |
|---|---|---|---|---|
| 20 | 1 | 22 | 13 | An attacker on the local network may cause an unexpected app termination |
| 18 | 1 | 49 | 28 | An attacker on the local network may be able to cause a denial-of-service |
| 18 | 5 | 632 | 119 | An app may be able to access sensitive user data |
| 12 | 1 | 12 | 10 | An attacker on the local network may be able to corrupt process memory |
| 11 | 2 | 188 | 69 | An app may be able to break out of its sandbox |
| 11 | 5 | 209 | 91 | An app may be able to access user-sensitive data |
| 9 | 1 | 9 | 9 | A logic issue existed when processing a maliciously crafted photo or video shared via an iCloud Link. Apple is aware of a report … |
| 9 | 2 | 44 | 32 | An app may be able to bypass Privacy preferences |
| 8 | 1 | 41 | 38 | An attacker may be able to cause unexpected app termination |
| 8 | 1 | 16 | 16 | Processing a maliciously crafted message may lead to a denial-of-service |
| 7 | 1 | 7 | 7 | An unauthenticated user on the same network as a signed-in Mac could send it AirPlay commands without pairing |
| 7 | 1 | 7 | 7 | An attacker on the local network may be able to bypass authentication policy |
| 7 | 1 | 7 | 7 | An attacker on the local network may be able to leak sensitive user information |

三条读数：

**读数一（跨报告一致性校验）** `An app may be able to access sensitive user data` 全语料 632 段 / 119 页，与 #104 发布的复测值完全一致（632 / 119）。这条同时校验两件事：本篇的分母口径与 #104 相同、该句在本轮重测中没有漂移。第 17 章把它列为口径衔接证据。

**读数二（四条"族内独占"句）** `…without pairing`（7/7/7）、`…bypass authentication policy`（7/7/7）、`…leak sensitive user information`（7/7/7）与 `iCloud Link` 句（9/9/9）：家族内多少段，全语料就多少段、多少页，即这四句在本期语料中只被本家族使用。其中 `An unauthenticated user on the same network as a signed-in Mac could send it AirPlay commands without pairing` 是家族里唯一以"未配对"为条件、以 Mac 为受体的句子，7 段全部在 `AirPlay` 名下。

**读数三（AirPlay 的位置限定形态）** AirPlay 的高频句全部以 `An attacker on the local network` 或 `An unauthenticated user on the same network as a signed-in Mac` 开头，77 段带 Impact 里除 1 段外全部带网络位置限定。对照 #103 的"近场族无 remote attacker"规律与 #105 的 Baseband 边界，本篇把这条规律再推一层：**`AirPlay` 名下的段不属于"近场（proximity / in radio range）"词汇，而属于 "local network" 词汇**——`in radio range` 与 `privileged network position` 在本家族三段都没有；`remote attacker` 在 R 组只出现 3 段，全部在 `Screen Sharing Server` 名下。

---

# 10 后果词汇：三组各自说什么

## 10.1 P 组：29 段带字段，归并为 10 种 Impact、8 种 Description

逐名（段数 / 带字段 / 纯致谢）：APNs 1/1/0、Safari Push Notifications 2/0/2、Notification Services 5/5/0、Notifications 11/1/10、Notification Center 14/12/2、Libnotify 20/10/10。

Impact 句按来源名分列（句后数字为该句在名下的段数）：

| 段落名 | Impact 句 | 段数 |
|---|---|---|
| APNs | An app with root privileges may be able to access private information | 1 |
| Notification Services | Notifications marked for deletion could be unexpectedly retained on the device | 5 |
| Notifications | An attacker with physical access to an iOS device may be able to view notification contents from the Lock Screen | 1 |
| Notification Center | An app may be able to access sensitive user data | 4 |
| Notification Center | An app may be able to access contact info related to notifications in Notification Center | 3 |
| Notification Center | An app may be able to access user-sensitive data | 2 |
| Notification Center | A malicious app may be able to access notifications from the user's device | 1 |
| Notification Center | A user may be able to view sensitive user information | 1 |
| Notification Center | An app with root privileges may be able to access private information | 1 |
| Libnotify | An attacker may be able to cause unexpected app termination | 8 |
| Libnotify | An app may be able to cause a denial-of-service | 2 |

逐名相加 1 + 5 + 1 + 4 + 3 + 2 + 1 + 1 + 1 + 8 + 2 = 29，与带字段段数闭合，无剩余。

三条要点：

**要点一：P 组只有一句真正的"留存级"后果。** `Notifications marked for deletion could be unexpectedly retained on the device`（5 段 / 5 页 / CVE-2026-28950）讲的是"该删的没删、留在设备上"；其余 24 段的主语都是 app、attacker with physical access 或 user，指向访问与可见性。

**要点二：P 组远程可达句式为 0。** `remote attacker` 在 P 组 0 段（全家族分布见 9.3 读数三）。同组最高频两句的主语都是 app，攻击前提在 #100 的口径里属于"本地访问 + 已安装恶意应用"。

**要点三：P 组修复句式向"脱敏"集中。** 家族级分区里 redaction 类句式共 19 段，其中 **16 段在 P 组**（第 11 章表），主要两句是 `A privacy issue was addressed with improved private data redaction for log entries.`（10 段，全部 `Notification Center`）与 `A logging issue was addressed with improved data redaction.`（5 段，全部 `Notification Services`）。这说明在措辞层"通知"与"日志脱敏"是绑在一起的——但这句话只能写成"顾问页把这两类措辞放在一起用"，不能写成"通知内容被写进日志"（实现层断言，G1）。

## 10.2 R 组：158 段带字段，归并为 26 种 Impact、24 种 Description

`AirPlay` 单名 77 段带字段 / 7 种 Impact（9.3 表列出其中 5 种）。`Screen Sharing Server` 16 段 / 6 种，六种全列：

| Impact 句 | 段数 | 名 |
|---|---|---|
| A remote attacker may be able to cause a denial of service | 3 | Screen Sharing Server |
| A user with screen sharing access may be able to view another user's screen | 3 | Screen Sharing Server |
| An app may be able to intercept network connections intended for another process | 3 | Screen Sharing Server |
| A local attacker may be able to determine the legacy VNC password configured for Screen Sharing | 2 | Screen Sharing Server |
| An attacker on the network may be able to authenticate to Screen Sharing without valid credentials | 2 | Screen Sharing Server |
| An attacker on the network may be able to authenticate to Screen Sharing without valid credentials | 3 | Screen Sharing |

最后一行是 R 组最好的一条方法证据：**同一句 `…authenticate to Screen Sharing without valid credentials` 在两个只差一个词的段落名下各自出现（Server 2 段、Screen Sharing 3 段，合计 5 段 / 5 页）**。第 7.2 节的教训在这里反向成立：按 `Screen Sharing Server` 取族会漏 3 段，按 `Screen Sharing` 取族会漏 2 段。

其余六名：`Sync Services` 12 段 / 4 种（最高 `An app may be able to bypass Privacy preferences` 6 段）；`CloudKit` 25 段 / 5 种（`An app may be able to break out of its sandbox` 8 段居首，另有 `An app may be able to fingerprint the user` 5 段、`An app may be able to read device name` 4 段、`A local app may be able to read a persistent account identifier` 4 段）；`iCloud` 11 段 / 4 种（`An app may be able to enumerate a user's installed apps` 6 段、`An app may be able to identify a user across reinstalls` 3 段）；`iCloud Document Sharing` 6 段 / 1 种（`An attacker may be able to turn on sharing of an iCloud folder without authentication`，6 段同句）；`iCloud Photo Library` 3 段 / 1 种；`CloudAttestation` 5 段 / 1 种；`Screen Sharing` 3 段 / 1 种；`Home` 0 段带字段。

其中 `CloudKit` 与 `iCloud` 两名合计 36 段里出现 4 种"身份 / 指纹 / 持久标识"句式（fingerprint the user、read device name、persistent account identifier、identify a user across reinstalls），是全家族唯一成组的**标识符暴露**后果；手册层对应话题写在「无线局域网隐私」与「iCloud 高级数据保护」两页，两层没有共用任何词形（G10）。

## 10.3 M 组：64 段带字段，归并为 15 种 Impact、15 种 Description

`Messages` 50 段 / 10 种，除第 12 章的 9 段在野句外，最高频两句是 `An app may be able to access sensitive user data`（10）与 `Processing a maliciously crafted message may lead to a denial-of-service`（8），另有 `A shortcut may be able to bypass sandbox restrictions`（6）。

`FaceTime` 14 段 / 7 种，其中两句在整个家族里形态独特：

- `Password fields may be unintentionally revealed when remotely controlling a device over FaceTime`（5 段）——主语是字段、条件是"远程控制"，全句不写攻击者；
- `Incoming FaceTime calls can appear or be accepted on a locked macOS device, even with notifications disabled on the lock screen`（3 段）——本组唯一以"来电"为受体、并显式提到"通知已关闭"仍生效的句子。

后一句与 P 组的 `An attacker with physical access to an iOS device may be able to view notification contents from the Lock Screen`（1 段）构成"锁屏通知可见性"的两处登记（全语料 `lock screen` 66 段 / 44 页 / 17 名）。本篇不合并它们：两句的平台（macOS 与 iOS）、条件（关闭通知后仍接受来电 与 物理访问查看内容）都不同。

`FaceTime Foundation` 3 段全部纯致谢、0 段带字段。连同 `Safari Push Notifications`（2 段纯致谢）与 `Home`（1 段纯致谢），家族里有 **3 个名只在致谢里出现**。

---

# 11 修复句式分区：26 类吃掉 249 段，剩 2 段

分类规则：取每段 Description 全文（块内多句先以 ` || ` 连接视为一段文本），按下表**优先顺序**首个命中的子句归类，因此各行互斥、逐行相加无剩余；子句匹配不区分大小写。

| 修复句式（首个命中） | P | R | M | 合计 |
|---|---|---|---|---|
| improved checks | 1 | 38 | 10 | 49 |
| improved state management | 0 | 22 | 12 | 34 |
| improved input validation | 0 | 14 | 5 | 19 |
| removing the vulnerable code | 0 | 7 | 8 | 15 |
| additional entitlement checks | 0 | 13 | 0 | 13 |
| improved access restrictions | 0 | 10 | 0 | 10 |
| private data redaction | 10 | 0 | 0 | 10 |
| additional restrictions | 0 | 10 | 0 | 10 |
| improved handling of symbolic links | 0 | 3 | 6 | 9 |
| bounds checking | 8 | 0 | 0 | 8 |
| improved validation of symlinks | 0 | 8 | 0 | 8 |
| improved input sanitization | 0 | 5 | 3 | 8 |
| improved privacy controls | 0 | 0 | 8 | 8 |
| improved memory management | 0 | 7 | 0 | 7 |
| improved memory handling | 0 | 7 | 0 | 7 |
| improved handling of temporary files | 0 | 0 | 6 | 6 |
| input validation issue | 0 | 6 | 0 | 6 |
| improved data redaction | 5 | 0 | 0 | 5 |
| redaction of sensitive information | 1 | 1 | 2 | 4 |
| improved restrictions | 0 | 3 | 0 | 3 |
| improved handling of identifiers | 0 | 3 | 0 | 3 |
| restricted entitlements | 2 | 0 | 0 | 2 |
| improved UI | 0 | 0 | 2 | 2 |
| improved data protection | 1 | 0 | 0 | 1 |
| improved permissions checking | 0 | 0 | 1 | 1 |
| improved validation | 0 | 0 | 1 | 1 |
| 分类小计 | 28 | 157 | 64 | 249 |
| 未归类 | 1 | 1 | 0 | 2 |
| 合计 | 29 | 158 | 64 | 251 |

两条未归类的 Description 原文（并列即说明它们不该并进上面任何一类）：

- P 组：`A privacy issue was addressed by moving sensitive data to a protected location.`
- R 组：`A privacy issue was addressed by moving sensitive data.`

四条读数：

**读数一（三组修复取向不同）** P 组 28 段里 16 段是各类 redaction（10 + 5 + 1）、8 段是 bounds checking；R 组以 checks 38 + state management 22 为主；M 组以 state management 12 + checks 10 为主。**脱敏句式几乎全在通知族**：全家族 redaction 类 19 段中 16 段属 P 组。

**读数二（bounds checking 的排他性）** `bounds checking` 8 段全部在 P 组（全部 `Libnotify` 名下，全部对应 CVE-2026-64739），R 与 M 两组 0 段。该句 `An out-of-bounds write issue was addressed with improved bounds checking.` 是家族内唯一描述越界写的句式；家族内 **0 段** 出现 out-of-bounds read 表述。

**读数三（entitlement 句式的两组分裂）** `additional entitlement checks` 13 段全在 R 组（`CloudKit` 5、`iCloud Document Sharing` 6 等），`restricted entitlements` 2 段在 P 组（`Libnotify`，句为 `An app could impersonate system notifications. Sensitive notifications now require restricted entitlements.`）。后者是全家族唯一把"能力 / entitlement"写进 Description 的**通知类**条目，也是唯一把"冒充系统通知"写成修复说明的句子。

**读数四（口径声明）** 上表按"首个命中子句"归类，因此不是各句式在语料里的全部出现次数。按子句存在口径重测，`state management` 在家族内出现 34 段（与互斥表一致，因为没有更早命中的词根），而 `relay` 词族 20 段里 `improved state management` 有 14 段——两套口径必须先声明再比较。第 16.2 节记录本篇本轮就因口径混用重跑过一次。

---

# 12 在野声明：家族 350 段 与 全语料 93 段的交集只有 9 段

全语料 `Apple is aware of a report` 句式：**93 段 / 65 页 / 12 个段落名**，逐名分布 WebKit 27、Messages 9、RTKit 8、Kernel 8、CoreMedia 8、ImageIO 7、CoreAudio 5、JavaScriptCore 5、dyld 5、RPAC 4、Accessibility 4、CoreGraphics 3（相加 = 93，无剩余）。

与本家族的交集：**9 段，全部在 `Messages` 名下，全部对应 CVE-2025-43200，全部同一句**：

`A logic issue existed when processing a maliciously crafted photo or video shared via an iCloud Link. Apple is aware of a report that this issue may have been exploited in an extremely sophisticated attack against specific targeted individuals.`

9 段所在页：122173、122174、122345、122346、122900、122901、122902、122903、122904（9 页，每页 1 段）。

三条要点：

**要点一：这是家族里唯一的在野登记。** P 组 0 段、R 组 0 段；M 组 9 段全部来自同一个名、同一个编号。

**要点二：登记的主语是"经 iCloud 链接分享的照片或视频"，不是消息投递通道。** 句子里的动作对象是附件、载体是 iCloud Link。这与手册层 S1 的机制（超阈值附件加密后上传 iCloud，消息里只带 URI 与密钥）指向同一条路径，但两层用词不同：T1 写"附件会使用 AES 在 CTR 模式下通过随机生成的 256 位密钥进行加密并上传至 iCloud"，T2 写 `shared via an iCloud Link`。

**要点三：家族内 icloud 一词的 15 段里有 9 段来自这段在野句。** 全语料字段文本含 `icloud` 共 26 段，本家族占 15 段（`Messages` 9 + `iCloud Document Sharing` 6），其余 11 段落在 `Security`、`Storage Management`、`TCC`、`Kernel`、`Networking`、`Siri` 六个非家族名下。也就是说，**本家族字段文本里"iCloud"这个词有六成来自一段在野声明的从句**，而 `iCloud` 这个段落名自己在字段里一次都没用到（14.2）。

---

# 13 不可外推清单

本篇的四条 0 与一条 9 都容易被写成越界结论。逐条给出允许表述与禁止表述。

| 观测 | 允许写 | 禁止写 | 依据章节 |
|---|---|---|---|
| push 全语料 0 段 | 顾问页字段文本不使用 push 一词 | Apple 没有登记任何推送通道缺陷 | 2.1、3 |
| device token / voip / badge / alert / sound 各 0 段 | 这些投递概念在措辞层无对应词 | 设备令牌机制无风险 | 1.2、G2 |
| P 组 53 段在野 0 | 本期通知族登记条目里没有一条声明在野利用 | 通知或推送方向从未被在野利用 | 10.1、12 |
| R 组 191 段在野 0 | 本期按名字取的中继与共享族无在野声明 | 中继方向无风险 | 7.1、12 |
| relay 词族 20 段在野 0 | 这 20 段没有一条在野声明 | 所以中继不受攻击 | 6.2，且被 7.1 排除在家族外 |
| AirPlay 84 段只在 14 页 | 该名下条目高度集中在少数更新批次，最新批次 0 段 | AirPlay 有 84 个缺陷、风险第一 | 8.3、2.1 |
| 家族纯致谢 99 / 350 | 家族里 28.3% 的块无法从字段提取后果 | 这些块对应的缺陷不存在或很轻 | 8.2、14.4 |
| 9 段在野同名同编号 | 本期该家族唯一在野登记是 CVE-2025-43200 | 攻击链、目标人群与利用方式已由该条说明 | 12 |
| 手册层 4KB / 16 KB | 手册层写过该容量断言且自带版本限定 | APNs 现在仍是 4KB 上限 | 4、限制一 |

另加一条方法论禁令：**不要用"最新批次里某族 0 段"描述任何采集结论。** 2.1 表里 `AirPlay` 最新批次 0 段，只说明 149034–149043 十页没有该名下条目；同批次 `CloudKit` 13 段、`Messages` 9 段，解析器工作正常。#105 第 14.6 章与 #106 第 16 章登记过的"标记换代静默漏采"缺陷，本篇已由覆盖性探针排除（第 16.1 节）。

---

# 14 段落名不是实现层：四条同名反证

## 14.1 `Notification Services` 名下 5 段说的是设备端留存

Impact 句 `Notifications marked for deletion could be unexpectedly retained on the device`，主语是"被标记为删除的通知"，位置是 "on the device"。该段能支持的表述止于"顾问页把这条后果归在 `Notification Services` 名下"。写成"APNs 侧未删除记录"或"通知服务进程保留了数据"都没有依据（G1、G3）。

## 14.2 `iCloud` 名下 11 段里没有一段用到 icloud 这个词

全语料字段文本含 `icloud` 的 26 段分布在 8 个名下：`Messages` 9、`iCloud Document Sharing` 6、`Security` 4、`Storage Management` 3、`TCC` 1、`Kernel` 1、`Networking` 1、`Siri` 1。**`iCloud` 这个段落名自己在字段文本里 0 次。** 同理 `CloudKit` 名下 30 段、`Sync Services` 名下 12 段，而 cloudkit 与 sync 两个词在全语料字段文本各 0 段。

这是 7.2 节方法句的正向证明：**通道词汇只活在名字里，名字不进入句子**。任何"按词取族"的做法都会把这三族整族丢掉。

## 14.3 `Messages` 名下 0 段用到复数 messages

全语料 `messages`（复数）19 段分布在 6 个名下：`Photos Storage` 6、`Shortcuts` 5、`Mail` 4、`Contacts` 2、`Media Session` 1、`Telephony` 1，一个都不在 `Messages` 名下。单数 `message` 才出现在 `Processing a maliciously crafted message may lead to a denial-of-service`（`Messages` 名下 8 段）这类句子里。用词形做普查时，单复数之差足以让整个名族消失。

## 14.4 为什么会有"只有致谢的段"

99 段纯致谢块的可核对形态有三种：块内含 `We would like to acknowledge …` 致谢句；块内含 `CVE-YYYY-NNNNN: 研究者` 形式的编号 + 人名行；块内含 `Entry added / updated / moved …` 备注行。本篇不推断这类块为何没有字段（#107 第 3.5 节实测备注落点规则未知，G13）。可写的只有一条：**在这 99 段里，本报告无法从字段文本提取任何后果或修复表述**，它们既不能计为缺陷、也不能计为"无缺陷"。

## 14.5 名字族里的弱成员

`CloudAttestation` 9 段（5 段带字段，句为 `A maliciously crafted app may be able to bypass code signing enforcement`）与 `Home` 1 段（纯致谢）是 R 组的两名弱关联成员。按名字相邻性它们更像 #107（代码签名与授权）的成员，但本篇复算了 #107 全文：那 19 个段落名里 **没有** `CloudAttestation`，全篇也没有 `bypass code signing enforcement` 这一句。两篇的家族因此互不重叠，这条句式由本篇首次登记；同时说明"分组与相邻报告不一致"是本报告体系的已知属性，不是本篇的临时改动（G14）。

---

# 15 批次新鲜度：家族在最新十页里的真实分布

## 15.1 探针与分母

探针规则沿用 #105 第 14.6 章与 #106 第 16 章确立的那一条：**任何"某族 0 段"或"全语料 0 次"的结论，必须先回答最新那批页面被解析器看见了吗。** 本篇的探针不靠页号、不靠"最新"字样，而靠类名命中数：先确认 149034 页在该刀下切出 209 段（与 #105/#106 已发布值一致），再确认同一趟遍历在 149034–149043 十页共切出 `Kernel` 名下 180 段、`WebKit` 名下 50 段。两项都成立，才允许使用本篇的 0。

十页逐页段数（容错口径，与 #104 第 13.5 节、#105 第 14.6 节同值）：149034 = 209、149035 = 314、149036 = 118、149037 = 128、149038 = 165、149039 = 13、149040 = 9、149041 = 103、149042 = 181、149043 = 190，合计 1,430 段，即严格口径与容错口径的全部差额。

## 15.2 本篇家族在最新十页里

| 段落名 | 最新十页段数 | 该名全期段数 |
|---|---|---|
| CloudKit | 13 | 30 |
| Messages | 9 | 77 |
| iCloud | 8 | 22 |
| Notifications | 4 | 11 |
| FaceTime | 4 | 26 |
| Screen Sharing Server | 3 | 17 |
| Sync Services | 2 | 12 |
| Screen Sharing | 1 | 4 |
| iCloud Photo Library | 1 | 6 |
| 其余 10 名 | 0 | 125 |
| 合计 | 45 | 350 |

逐名相加 13 + 9 + 8 + 4 + 4 + 3 + 2 + 1 + 1 = 45，与"家族最新十页 45 段"闭合；其余 **10 个名**在最新十页 0 段（APNs、Safari Push Notifications、Notification Services、Notification Center、Libnotify、AirPlay、iCloud Document Sharing、CloudAttestation、Home、FaceTime Foundation），合计 125 段全部来自 149034 之前的页面。

三条限制：

**限制一：本篇凡写"当前"，都必须是"本期镜像内"。** 镜像批次为已抓取的 257 份页面，最新批次页面 `Released` 字段为 September 14, 2026（149034–149037 等）。这是页面自带的 `Released` 字段值，不是本报告推断的发布日期，也不等同于该 CVE 的公开时间。

**限制二：`relay` 词族的最新十页段数为 2。** 20 段里有 2 段落在 148281、148282、148287、148353 这批（8 月 17 日）页面，最新十页（9 月 14 日批）内 0 段——这条对第 6 章不影响，但对"专用代理方向当前仍有登记"这类表述是硬约束。

**限制三：10 个名在最新批次为 0，不等于这些名已停用。** `Notifications` 在最新十页仍有 4 段纯致谢；`Notification Center` 最新一段在 126348。本报告没有跨批次追踪段落名的能力（G11），只能陈述"最近出现于哪一页"。

---

# 16 自我纠错登记

按既有纪律，本轮取数过程中被推翻或被修正的判断全部登记，不做静默替换。

## 16.1 一处口径混用：家族内 15 段被初稿写成 9 段

初稿在讨论"家族里 iCloud 一词的来源"时写成"家族内 9 段全部来自在野声明"。复算发现该句混淆了两件事：`icloud` 一词的 15 段里 9 段来自 `Messages` 名下的在野句、6 段来自 `iCloud Document Sharing`。9 是**在野句**的段数，15 才是**该词在家族内**的段数。正文第 12 章要点三已按 15 / 9 双数改写，并在 14.2 补上 `iCloud` 名下 0 次这一条。该错误不影响任何分母，只影响一处占比表述（"四分之一"改为"六成"）。

## 16.2 一处交叉引用未核对：`CloudAttestation` 是否属于 #107

初稿把 `CloudAttestation` 的 5 段写成"#107 已登记的同一句、同一编号"。实测：#107 全文 `CloudAttestation` 出现 **0 次**，`bypass code signing enforcement` 也 **0 次**。第 14.5 节改为"两篇家族互不重叠，该句式由本篇首次登记"，并把这条列为缺口 G14（相邻报告分组不一致属体系属性）。这条与 #106 第 16.5 节记录的转录误差同类：**引用另一篇报告的数字前必须 grep 该篇正文**。

## 16.3 一处切块刀不合规，并给出污染实测

本轮第一版家族普查脚本把块尾写成"下一个 h3 或文件末尾"，少了"免责声明 / 页脚"两个终止锚点。后果：每页最后一个块的区域会吞下页面级免责声明与页脚文本，致谢字段里会出现 `Information about products not manufactured by Apple…`（在页 148353 的 relay 行上肉眼可见）。

本轮据即刻意做了污染量化：改用合规刀（块尾 = 下一个 `gb-header` 级 h3 / `<div id="disclaimer"` / `<footer` / 文末）后重跑全语料，**9,045 段中含该免责声明文本的块数 = 0**，即合规刀下不存在污染；不合规刀的段计数恰好不受影响（它按块计数，不按段计数），但关键词计数与致谢字段样本会受污染。本篇正文引用的全部数字均出自合规刀，验收断言见附表 D 第 1、2、4 项。教训并入第 19 章核验项第 3 条：**同一趟遍历里"块计数正确"不能推出"字段文本正确"。**

## 16.4 一处坏值：族内去重编号数最初打印为 0

第一版脚本用一行 `grep { /CVE/ } keys %{...}` 的错误表达式计算并集，导致"三组去重相加"与"并集"两个数都不可信（并集被打印成 0）。本轮重写为按组分别收集编号集合再求并，得到 13 / 38 / 19 / 并集 70 / 跨组 0。这是本篇最容易被写成结论的一条结构事实（第 9.1 节），因此按附表 D 第 8 项单独给了可复算断言：三组之和 = 并集 = 70，且交集元素数为 0。

## 16.5 一处禁令重申

写作过程中一度出现"推送方向本期无在野利用，可见该面风险较低"的草稿句。该句违反第 13 章第二行禁令，已删除。本篇允许的最强表述只是"本期该家族唯一在野登记是 CVE-2025-43200"。

---

# 17 与已发布诸篇的口径衔接

| 衔接项 | 本篇复测值 | 已发布值 | 结论 |
|---|---|---|---|
| 全语料段数 / 段落名数 | 9,045 / 470 | #104–#107 同值 | 一致 |
| 带 Impact 段 / 含编号段 / 纯致谢段 | 7,346 / 7,342 / 1,699 | #107 第 2.1 行同值 | 一致，且 7,346 + 1,699 = 9,045 |
| 严格口径十页差额 | 1,430 段（逐页 209/314/118/128/165/13/9/103/181/190） | #104 第 13.5 节、#105 第 14.6 节同值 | 一致 |
| `An app may be able to access sensitive user data` | 632 段 / 119 页 | #104 发布 632 / 119 | 一致 |
| `Apple is aware of a report` | 93 段 / 65 页 / 12 名 | #105 发布 93 段 / 65 页 | 一致（本篇补 12 名并逐名相加闭合） |
| `maliciously crafted` 类句式 | 本篇未重测 | #106 发布 1,750 段 / 38 宾语值 | 本篇不引用，避免跨口径 |
| 连续互通族段数 | 本篇不引用 | #104 发布 230 段 | 两篇家族定义不同，不可相加 |

本篇对既有分母没有任何修订，只新增两条：`push` 词族在全语料字段文本为 0 段；`relay` 词族 20 段与 19 名家族交集为 0。

---

# 18 缺口清单（G1–G15）

| 编号 | 缺口 | 影响 |
|---|---|---|
| G1 | 源码层（XNU 与用户态开源部件）本期镜像为空 | 全部实现层表述只能写成"顾问页把该段归在某名下"，第 10、14 章因此受限（结构性） |
| G2 | 设备推送注册令牌的产生、存储、轮换在两层文本里都没有 | 第 13 章禁止任何"设备令牌风险"表述 |
| G3 | 通知留存位置（进程、数据库、日志）无法从字段判定 | 10.1 要点三只能停在措辞绑定层面 |
| G4 | 手册层 4KB / 16 KB 的版本映射表缺失 | 第 4 章限制一，容量数字不可用于当前实现判断 |
| G5 | "APNs 路由信息等元数据则不加密"的字段清单未给 | 第 4 章限制三 |
| G6 | 入口代理与出口代理之间交换的字段 | 第 5 章实测三，只能引到"使用两个不同代理" |
| G7 | 专用代理概览 PDF（手册层外链）本轮未取回 | 第 5.1 节实测三只依据安全指南正文 |
| G8 | 两段路径的信任假设与协议未公开 | 第 5.2 节；无法把 `relay` 20 段的修复句式对应到任一跳 |
| G9 | 设备令牌在 IDS 目录中的存储位置与生命周期 | 第 5.2 节 |
| G10 | 标识符暴露后果组（fingerprint / device name / persistent account identifier / identify across reinstalls）与手册层「无线局域网隐私」「iCloud 高级数据保护」两页没有共用词形 | 第 10.2 节末段，跨层对应无法建立 |
| G11 | 报告体系没有跨批次追踪段落名存续的能力 | 第 15 章限制三 |
| G12 | `Safari Push Notifications`、`FaceTime Foundation`、`Home` 三名 0 段带字段的原因未知 | 第 10.3 节末段；三名不能参与后果分析 |
| G13 | 条目备注（Entry added / updated / moved）的落点规则未知（沿用 #107 第 3.5 节实测结论） | 第 14.4 节；不能据块内备注判断条目新增时间 |
| G14 | 相邻报告家族定义不一致（`CloudAttestation` 在 #107 为 0 次） | 第 16.2 节；跨报告数字不可相加 |
| G15 | 措辞层没有"投递失败 / 队列 / 重试"任一后果句式，手册层却有队列与 30 天例外 | 第 4 章 S2、第 7 章；两层之间不存在可核对的映射 |

---

# 19 核验项清单

复算本篇时按顺序执行以下 20 项，任一项不符即停止引用相关数字。

1. 镜像文件数 = 258，剔除 122085 后 = 257；同一清单跑出全语料 9,045 段、470 个段落名。
2. 带 Impact 的段 = 7,346；含 CVE 编号的段 = 7,342；纯致谢段 = 1,699；断言 7,346 + 1,699 = 9,045。
3. 块尾锚点必须含"下一个 gb-header 级 h3 / disclaimer 容器 / footer / 文末"四项，缺任一项则字段文本会混入页面级免责声明；用合规刀重跑，含 `Information about products not manufactured by Apple` 的块数必须为 0。
4. 覆盖性探针按类名命中数验收：149034 = 209 段；最新十页合计 1,430 段；同趟 `Kernel` 名下最新十页 = 180 段、`WebKit` = 50 段。
5. `push`、`device token`、`voip`、`badge`、`alert`、`sound`、`cloudkit`、`sync`、`man-in-the-middle`、`eavesdrop` 在字段文本口径下必须全部为 0 段。
6. `relay` 词族：20 段 / 17 页 / 5 名 / 6 编号；4 种 Impact 与 4 种 Description；7 + 7 + 4 + 2 = 20；与 19 名家族交集 = 0；`icloud private relay` 精确短语 2 段（`Kernel` 1、`Networking` 1）。
7. 家族：P 53 / R 191 / M 106，合计 350；页数 44 / 70 / 79，并集 135；字段穷尽 29 / 158 / 64，纯致谢 24 / 33 / 42，且 251 + 99 = 350。
8. 编号并集：13 + 38 + 19 = 70 = 去重并集，跨组共享 0；Impact 句式并集 46 而三组相加 51，跨组复用 5。
9. 修复句式互斥分区：26 类覆盖 249 段，未归类 2 段（两条 `moving sensitive data` 句式），249 + 2 = 251；`bounds checking` 8 段全部 P 组、`private data redaction` 10 段全部 `Notification Center`。
10. 在野交集：9 段全部 `Messages` 名下、全部 CVE-2025-43200、9 个不同页；P 组 0、R 组 0。
11. `Apple is aware` 全语料 93 段 / 65 页 / 12 名，逐名相加（27+9+8+8+8+7+5+5+5+4+4+3）= 93。
12. `An app may be able to access sensitive user data` 全语料 632 段 / 119 页，与 #104 已发布值一致；家族内 18 段 / 5 名。
13. 手册层中文全量 179 页正文域：`推送` 15 / 13、`推送通知` 9 / 8、`通知服务` 6 / 6、`APNs` 24 / 8、`通知` 50 / 31、`中继` 22 / 13、`短信转发` 7 / 4、`通话中继` 4 / 4、`iCloud 专用代理` 8 / 3、`隔空播放` 5 / 3、`屏幕共享` 1 / 1、`令牌` 70 / 15、`iMessage` 73 / 24、`FaceTime` 24 / 10。
14. 手册层必须为 0 的词形：`私密中继`、`私密代理`、`AirPlay`、`Screen Sharing`、`Private Relay`、`Push`、`notification` 各 0 次 / 0 页。
15. 手册层标题级页面：179 页中标题命中本篇主题的只有「iPhone 蜂窝网络通话中继安全性」与「iCloud 专用代理安全性」两页。
16. 六条机制原句（S1–S6）必须逐字回读，且引用 S1 时保留"具体取决于 iOS 或 iPadOS 的版本"、引用 S2 时保留"与其他 APNs 通知不同的是"。
17. `APNs` 名下 = 1 段 / 1 页（121238）/ 1 编号（CVE-2024-44130）；`Safari Push Notifications` 名下 2 段全为纯致谢。
18. 段落名存续：19 名中 10 名在最新十页 0 段；家族最新十页合计 45 段。
19. 分布形态：`AirPlay` 84 段 / 14 页（6.00 段每页）、`Messages` 77 段 / 67 页（1.15）；`Screen Sharing Server` 17 段 / 8 页。同句 `…authenticate to Screen Sharing without valid credentials` 跨两名共 5 段 / 5 页。
20. 任何"0"或"1"的断言，引用前先回答：这是在字段文本口径、块计数口径，还是段落名口径下测得的？三者不可互换（第 16.3 节）。

---

# 附表 A 家族 19 个段落名逐名清单（19 行）

| 段落名 | 组 | 段数 | 页数 | 带字段 | 纯致谢 | 去重 Impact 句式 | 最新一段页 | 最新十页段数 |
|---|---|---|---|---|---|---|---|---|
| APNs | P | 1 | 1 | 1 | 0 | 1 | 121238 | 0 |
| Safari Push Notifications | P | 2 | 2 | 0 | 2 | 0 | 127121 | 0 |
| Notification Services | P | 5 | 5 | 5 | 0 | 1 | 127114 | 0 |
| Notifications | P | 11 | 11 | 1 | 10 | 1 | 149038 | 4 |
| Notification Center | P | 14 | 10 | 12 | 2 | 6 | 126348 | 0 |
| Libnotify | P | 20 | 20 | 10 | 10 | 2 | 148287 | 0 |
| AirPlay | R | 84 | 14 | 77 | 7 | 7 | 122378 | 0 |
| Screen Sharing Server | R | 17 | 8 | 16 | 1 | 6 | 149042 | 3 |
| Screen Sharing | R | 4 | 4 | 3 | 1 | 1 | 149035 | 1 |
| Sync Services | R | 12 | 12 | 12 | 0 | 4 | 149035 | 2 |
| CloudKit | R | 30 | 22 | 25 | 5 | 5 | 149038 | 13 |
| iCloud | R | 22 | 16 | 11 | 11 | 4 | 149038 | 8 |
| iCloud Document Sharing | R | 6 | 6 | 6 | 0 | 1 | 122721 | 0 |
| iCloud Photo Library | R | 6 | 6 | 3 | 3 | 1 | 149034 | 1 |
| CloudAttestation | R | 9 | 9 | 5 | 4 | 1 | 128070 | 0 |
| Home | R | 1 | 1 | 0 | 1 | 0 | 122371 | 0 |
| Messages | M | 77 | 67 | 50 | 27 | 10 | 149043 | 9 |
| FaceTime | M | 26 | 24 | 14 | 12 | 7 | 149038 | 4 |
| FaceTime Foundation | M | 3 | 3 | 0 | 3 | 0 | 121845 | 0 |

校验：段数逐列相加 = 350；带字段 = 251；纯致谢 = 99；最新十页 = 45。`去重 Impact 句式` 列相加 = 51，而族内去重并集 = 46，差额 5 即第 9.2 节的跨组复用句数。

---

# 附表 B `relay` 词族 20 段逐段表（20 行）

| 页号 | 段落名 | CVE 编号 | Impact 句 | Description 句 |
|---|---|---|---|---|
| 121837 | Safari | CVE-2024-44246 | I3 | D3 |
| 121838 | Safari | CVE-2024-44246 | I3 | D3 |
| 121839 | Safari | CVE-2024-44246 | I3 | D3 |
| 121846 | Safari | CVE-2024-44246 | I3 | D3 |
| 124149 | Kernel | CVE-2025-43276 | I4 | D4 |
| 125108 | WebKit | CVE-2025-43376 | I1 | D1 |
| 125110 | WebKit | CVE-2025-43376 | I1 | D1 |
| 125113 | WebKit | CVE-2025-43376 | I1 | D1 |
| 125114 | WebKit | CVE-2025-43376 | I1 | D1 |
| 125115 | WebKit | CVE-2025-43376 | I1 | D1 |
| 125116 | WebKit | CVE-2025-43376 | I1 | D1 |
| 125634 | Networking | CVE-2025-43506 | I4 | D4 |
| 126793 | WebKit | CVE-2025-43376 | I1 | D1 |
| 148281 | Authentication Services | CVE-2026-65355 | I2 | D2 |
| 148281 | WebKit | CVE-2026-65352 | I2 | D2 |
| 148282 | Authentication Services | CVE-2026-65355 | I2 | D2 |
| 148282 | WebKit | CVE-2026-65352 | I2 | D2 |
| 148287 | Authentication Services | CVE-2026-65355 | I2 | D2 |
| 148353 | Authentication Services | CVE-2026-65355 | I2 | D2 |
| 148353 | WebKit | CVE-2026-65352 | I2 | D2 |

句式代号对照（不计入本表行数）：

## B.1 Impact 句与 Description 句逐字对照

| 代号 | Impact 原文 | Description 原文 | 段数 |
|---|---|---|---|
| I1 / D1 | A remote attacker may be able to view leaked DNS queries with Private Relay turned on | A logic issue was addressed with improved state management. | 7 |
| I2 / D2 | A website may be able to determine a user's IP address with Private Relay turned on | An information disclosure issue was addressed with improved state management. | 7 |
| I3 / D3 | On a device with Private Relay enabled, adding a website to the Safari Reading List may reveal the originating IP address to the website | The issue was addressed with improved routing of Safari-originated requests. | 4 |
| I4 / D4 | iCloud Private Relay may not activate when more than one user is logged in at the same time | A logic error was addressed with improved error handling. | 2 |

同页并列行（148281、148282、148353 各 2 段）是"同一句被归在两个段落名下"的直接样本，两个名分别是 `Authentication Services` 与 `WebKit`，两个编号分别是 CVE-2026-65355 与 CVE-2026-65352。

---

# 附表 C 手册层引用页与命中数（16 行）

| 页面标识 | 标题（去网站后缀） | 本篇引用要点 | 命中词与次数（只列本篇引用到的词形） |
|---|---|---|---|
| sec93292bfa6 | 术语表 | S5 两条定义式条目 | APNs 2、推送通知 2、通知服务 1 |
| sec70e68c949 | iMessage 信息如何安全发送和接收信息 | S1 容量、S2 删除与队列、S3 元数据 | APNs 9、推送 1 |
| secd9764312f | iMessage 信息安全性概览 | S4 地址目录 | APNs 3、推送 1、通知服务 1 |
| sec28a79bf17 | iPhone 蜂窝网络通话中继安全性 | S6 来电经 APNs；去电"中继到 iPhone" | APNs 2、中继 3、推送通知 1、通知服务 1 |
| secad8ce3233 | iCloud 专用代理安全性 | 入口代理 / 出口代理、版本门槛、外链 | iCloud 专用代理 5 |
| sec16bb20def | iPhone 短信转发安全性 | 与通话中继同族中文词形 | 短信转发 4 |
| secce267dc10 | 连续互通安全性概览 | 中继与短信转发的跨设备语境 | 中继 1、短信转发 1、通话中继 1 |
| secf78dbe639 | 接力安全性 | 中继 2 次；APNs 2 次 | 中继 2、APNs 2、通话中继 1 |
| seca331c55cd | FaceTime 通话安全性 | 推送通知与服务条目 | APNs 2、通知 2、推送 1、通知服务 1 |
| sec013b5d35d | 设备管理安全性概览 | 通知服务与 APNs 引用 | APNs 3、通知 1、推送 1、通知服务 1 |
| secb0694df1a | 钥匙串数据保护 | APNs 与令牌并存的一页 | APNs 1、令牌 5、通知 1、推送 1、通知服务 1 |
| secbd5822138 | Apple 账户安全性概览 | 推送与账户条目，该页无 APNs 字样 | 推送 1、推送通知 1、通知 2 |
| sec3a881ccb1 | HomeKit 通信安全性 | 隔空播放中文词形与通知 | 隔空播放 2、通知 2 |
| sec31e483abf | Apple 设备的无线局域网隐私 | 隔空播放 1 次（标识符话题相邻） | 隔空播放 1 |
| sec0c87ccc6d | 单点登录安全性 | 令牌 10 次、屏幕共享 1 次（唯一命中） | 令牌 10、屏幕共享 1 |
| sec8447f5049 | 在 macOS 中管理文件保险箱 | 令牌最高单页（对照 T2 的 token 4 段） | 令牌 18 |

---

# 附表 D 复算验收断言集（12 行）

| 序号 | 断言 | 本篇实测值 |
|---|---|---|
| 1 | 镜像清单：258 文件剔 122085 后 257；全语料块数与段落名数 | 9,045 段 / 470 名 |
| 2 | 字段口径闭合：带 Impact 段 + 纯致谢段 = 全语料段 | 7,346 + 1,699 = 9,045 |
| 3 | 块尾四锚点齐备时，含页面免责声明的块数 | 0 |
| 4 | 探针 149034 段数；最新十页合计；同趟 Kernel 与 WebKit 最新十页段数 | 209；1,430；180 与 50 |
| 5 | 十个词形在字段文本口径下为 0 段 | push、device token、voip、badge、alert、sound、cloudkit、sync、man-in-the-middle、eavesdrop |
| 6 | `relay` 词族四元组 | 20 段 / 17 页 / 5 名 / 6 编号；4 种 Impact、4 种 Description |
| 7 | 名字族 × 词族交集 | 350 × 20 交集 0；`icloud private relay` 精确短语 2 段 |
| 8 | 编号并集与跨组共享 | 13 + 38 + 19 = 70 = 并集；跨组 0 |
| 9 | 修复句式互斥分区闭合 | 26 类 249 段 + 未归类 2 段 = 251 |
| 10 | 在野交集 | 家族 9 段，全为 `Messages` / CVE-2025-43200；P 0、R 0 |
| 11 | 手册层 0 词形 | 私密中继、私密代理、AirPlay、Screen Sharing、Private Relay、Push、notification 各 0 次 / 0 页 |
| 12 | 手册层标题命中页 | 仅 2 页（通话中继、iCloud 专用代理） |

---

# 附表 E 错误写法与纠正对照（15 行）

| 错误写法 | 判定 | 依据 |
|---|---|---|
| "Apple 顾问页没有登记推送类漏洞" | ❌ 不可写 | `push` 0 段只说明措辞层不用该词；`APNs` 名下确有 1 段（第 1、3 章） |
| "推送通道没有风险" | ❌ 不可写 | 第 13 章第一、二行禁令 |
| "通知族本期无在野利用，所以该面安全" | ❌ 不可写 | 第 13 章第三行；0 不可外推 |
| "中继方向没有条目" | ❌ 不可写 | `relay` 词族 20 段确实存在，只是被归在浏览器与网络栈名下（第 6 章） |
| "本篇家族 350 段里 0 段谈中继，所以中继与推送无关" | ❌ 不可写 | 交集 0 是分组口径的结果，不是事实陈述（第 7.2 节） |
| "`AirPlay` 名下 84 段说明 AirPlay 缺陷最多" | ❌ 不可写 | 84 段压 14 页，段数不等于缺陷数（8.3、第 13 章） |
| "`Notification Services` 名下 5 段说明 APNs 服务端留存数据" | ❌ 不可写 | 该句主语是被标记删除的通知、位置在设备上（14.1） |
| "CVE-2024-44130 是 APNs 服务端的漏洞" | ❌ 不可写 | 第 3 章观察一、二：Impact 主语是具备 root 权限的应用 |
| "iCloud 名下条目谈的是 iCloud 服务缺陷" | ❌ 不可写 | 14.2：`iCloud` 名下 11 段字段文本里 icloud 一词 0 次 |
| "家族里 iCloud 一词 9 段都来自在野声明" | ⚠️ 只能写成 15 段中 9 段 | 16.1；其余 6 段来自 `iCloud Document Sharing` |
| "`CloudAttestation` 是 #107 已登记的家族成员" | ❌ 不可写 | 16.2：#107 全文该名为 0 次 |
| "手册层的 4KB 上限就是当前实现" | ❌ 不可写 | 第 4 章限制一（版本限定句自带），G4 |
| "Apple 保存推送内容 30 天" | ❌ 不可写 | 第 4 章限制二：30 天是 iMessage 离线队列例外，不是通用策略 |
| "私密中继即 iCloud 专用代理" | ⚠️ 只能带限定写 | 5.1 实测一：手册层 0 次该译法，正式词形是 iCloud 专用代理 |
| "最新批次页面解析不到，所以某些族 0 段" | ❌ 不可写 | 15.1 探针：同趟在最新十页切出 Kernel 180 段、家族 45 段 |

---

**边界声明：** 本报告全部技术结论来自两层可核对的公开文本：《Apple 平台安全保护》官方指南（中文 179 页全量转储，正文域逐词普查）与 Apple 安全响应顾问页（257 份英文页面镜像，容错解析后全语料 9,045 段 / 470 个段落名；本篇推送—中继家族 350 段 / 19 个段落名 / 70 个 CVE 编号 / 135 页，另单列 `relay` 词族 20 段 / 5 个段落名），逐条标注页号、字段原文与页面自带的 `Released` 字段；手册层未说明的部分在第 18 章登记为缺口，不以推断填充。源码层本轮不可用，凡涉及投递通道、通知留存位置与代理链路实现位置的表述均已降级为"顾问页将其归入某段落名"。第 1、2、6、7、8、9、11、12、15 章的统计为本报告对公开文本的自建语料普查，属本报告的测量结果，不是 Apple 的表述；这些数字依赖本报告解析器对顾问页 HTML 标记的识别方式——本轮已因切块刀锚点缺失重跑全部字段口径计数，并用"含页面免责声明的块数 = 0"作为验收（第 16.3、附表 D 第 3 项），后续复算须先跑附表 D 全 12 项断言。所引 CVE 仅用于说明公开修复事实、检测与配置核查，不含利用代码、概念验证、推送凭据抓取或重放、中继链路流量分析或脱敏规避方法，也不含针对他人设备的任何操作步骤。所有第三方研究者姓名仅作顾问页致谢字段的引用，其研究结论本轮未核对，不构成本报告依据。如需就本报告任一结论索取取数脚本或段落级原始文本，或指出任何事实与措辞问题，请联系。

Telegram: https://t.me/one00190
