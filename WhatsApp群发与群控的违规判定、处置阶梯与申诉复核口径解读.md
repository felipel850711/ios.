@@TITLE@@WhatsApp 群发与群控的违规判定与处置阶梯：官方文本口径解读
@@SUBTITLE@@本篇是 WhatsApp 官方口径系列的第三篇，也是"群发／群控五篇"中的第一篇，主题是**规则层**：把 WhatsApp 与 Meta 官方页面上关于"什么行为被判为违规、判了之后按什么顺序处置、每一步持续多久、哪些能申诉哪些不能"的全部公开表述收在一处，逐条对上原句与出处。全篇只读官方文本，共引用 **45** 个页面位置：Meta 开发者文档 **26** 页、帮助中心 **13** 页、法律页 **5** 页、Business 政策页 **1** 页，其中 **5** 页本次正文未取回、全篇不引用其内容（详见附表 A 的 **46** 行）。核心发现四条：**第一，官方至少写了三套互不相通的处置体系**——Business Platform 政策执行页给的是"警告 → **1 或 3 天**封锁 → **5、7 或 30 天**封锁 → 无限期账号锁 → 永久停用"的四级阶梯，Direct Send 完整性页给的是"警告 → 限速 → **7 天**限制 → **30 天**延长限制 → 永久吊销"的五级阶梯，帮助中心消费者端另给"临时封禁／永久封禁／受限账号"一套，三套体系的**主语、时长、档位数量、申诉规则全都不一样**，本篇逐套归档（附表 C 共 **37** 档）。**第二，政策执行页列出的 **19** 个违规类型码全部是商业交易品类**（成人、酒类、烟草、赌博、药品、武器、货币、body parts 等），**没有一个码描述"发送行为"**——也就是说"群发"在 API 层根本不是一个可判定的违规类型，它只在服务条款的 **bulk messaging／auto-messaging／auto-dialing** 三项和消息指南的 adversarial behavior 一句里被命名。**第三，申诉窗口是所有体系里最不一致的一根轴**：Business Platform 申诉决策"typically takes **24 to 48 hours**"、模板暂停申诉"**within 24 hours**"、Direct Send 模板复核"Most reviews take **a few days**"且须在被标记后 **60 days** 内提出、消费者端复核"Most reviews are completed **within 24 hours**"，四个数分属四套流程，且官方从未给出对照表。**第四，限额档位与处罚阶梯是两根独立的轴**：**250／2,000／10,000／100,000／不限**是投递规模档位，达到或跌到某一档不产生任何处罚；反之，处罚阶梯的**限速**一级里那个数字**官方没有写**。全篇不涉及任何批量发送脚本、账号池、多号轮换、限速规避、检测绕过或申诉话术模板；引用这些规则文本的目的只有一个：**让读者知道平台在公开文本里已经把哪些行为定义成违规、以及被处置之后官方承诺给出什么**。
@@INFO@@报告日期|2026 年 11 月 8 日
@@INFO@@分析对象|WhatsApp Business Platform 政策执行层（`developers.facebook.com/documentation/business-messaging/whatsapp/` 下的 policy-enforcement、policy-enforcement/violations 与 Direct Send 的 integrity-and-content-guidelines、supported-features-and-limits、api-reference，共 **5** 页）；模板治理层（templates 下的 template-quality、template-pausing、template-pacing 与 marketing-templates-per-user-limits，共 **4** 页）；限额层（messaging-limits 与 upcoming-messaging-limits-changes，共 **2** 页）；Webhook 与错误码层（webhooks/overview 与 reference 下的 account_update、account_alerts、account_review_update、business_capability_update、message_template_status_update、message_template_quality_update、phone_number_quality_update、template_category_update、security，共 **10** 页，另加 support/error-codes **1** 页）；接入与授权层（getting-started/getting-opt-in，**1** 页）；以上 Meta 层合计 **26** 页（其中 **3** 页本次正文未取回）。帮助中心层 **13** 页：About account bans、About account bans on the WhatsApp Business app、About ban appeals on the WhatsApp Business Platform、About temporarily banned accounts、About account bans for unofficial apps、About unofficial WhatsApp apps、About restricted accounts、After your account is unbanned、About detecting violations、How to use WhatsApp responsibly、How to register your phone number、About the official WhatsApp Business account、How to contact the Grievance Officer and WhatsApp in India（最后 **1** 页本次正文未取回）。法律与政策层 **6** 页：WhatsApp Terms of Service、WhatsApp Messaging Guidelines、WhatsApp Business Messaging Policy、Meta Commerce Policy（两个域名的同一页，计 **2** 个登记位）、WhatsApp Terms for WhatsApp Business Platform、WhatsApp Business Terms of Service（最后 **1** 页本次正文未取回）。四类相加 **45** 个登记位；对照口径：个人号（WhatsApp Messenger）、WhatsApp Business 应用、WhatsApp Business Platform 三套身份各有一套处置体系，本篇逐章标注属于哪一套。"处置"分五档口径——**阶梯档位时长／滚动窗口与观察期／申诉与复核时限／生效日期与版本节点／数量与档位**，本篇共点名 **65** 个数字与时长词条目（按字面值去重后 **39** 个），逐一归档在附表 B（**66** 行含合计）。
@@INFO@@视角定位|官方规则文本的口径解读，属于**平台治理规则的公开文档梳理**，不是操作指南。本篇**不提供**批量发送脚本、发送自动化、账号池、多号轮换、设备农场、限速或频控规避、垃圾信息检测绕过、申诉话术模板、诱导点击，也不提供任何针对他人设备或他人账号的操作步骤与时序；**不提供**任何可用于向非自愿收件人大量投递消息的方法，也不提供"如何不被检测到"的内容。文中所有阶段、时长、错误码、枚举与措辞均取自官方公开页面，引用它们的目的只有两个：**（1）说明平台在规则层已经把哪些行为定义为违规、把哪些处置写成阶梯；（2）说明官方在每一级上承诺给出什么通知、留下什么复核入口**。凡引号内的英文句子均为**官方页面原文**，出处紧跟其后；官方页面未写明的内容一律标注"官方未写明"，不做推测补齐，也不以其他平台的惯例代替。本篇不构成对 WhatsApp、Meta 政策执行一致性或公正性的评价。
@@INFO@@分类|**官方页面登记清单 45 个位置 × 站点 × 引用位置 × 取回状态**（附表 A，**46** 行含合计；分组为 Meta 开发者 26、帮助中心 13、法律与政策 6（含同一页的两个域名），**未取回 5 页**）**/ 数字归档五档 × 全部 65 个条目**（附表 B，**66** 行含合计，档位：**阶梯档位时长 23／滚动窗口与观察期 10／申诉与复核时限 8／生效日期与版本节点 8／数量与档位 16**，按字面值去重 **39** 个、计数单位 **28** 种）**/ 三套体系的处置阶梯逐档**（附表 C，**38** 行含合计，七组：商业平台政策执行阶梯 9、Direct Send 阶梯 10、模板质量与暂停 8、限额档位 3、消费者端 5、Business 应用端 1、Business Platform 端 1）**/ 枚举值全量归档**（附表 D，**137** 行含合计，二十三组：account_update 事件 16、模板状态事件 14、能力封锁类型 9、模板拒绝原因 9、Direct Send 违规类型 8、告警描述 8、限额档位 7、消息断连原因 6、告警类型 6、Direct Send 阶梯 5、模板状态 TITLE 5、模板状态触发 5、bcu 旧字段数值 5、bcu 新字段档位 5、复核状态 4、BM 质量标签 4、号码质量评级 4、模板暂停实例 3、模板类别 3、告警实体类型 3、告警严重度 3、bcu 触发 2、告警状态 2）**/ 受监管品类、真人升级路径与同步错误码**（附表 E，**52** 行含合计，四组：违规类型码 19、Commerce Policy 受监管品类 15、真人客服升级路径 6、同步错误码 11）**/ 页面之间的口径冲突与官方未写明清单**（附表 F，**25** 行含合计，八组冲突 + 十六项留白，其中十一项在第 13.2 节正文列出、五项为本表补登）
@@INFO@@难度|进阶（本篇的难点有三处。**第一是"同一句话在不同体系里指不同的东西"**：`restriction` 在政策执行页指"block on sending … template messages"、在 Direct Send 页指"can't use Direct Send for utility or authentication messaging"、在帮助中心指"Your account is restricted right now"且**自动到期无需申诉**，三者时长、可发内容、是否可申诉全部不同；`ban` 在帮助中心有两种（`Temporarily banned` 与"the ban is final"），在 Business Platform 文档里则写成 `offboard`／`permanently disabled`／`Permanent revocation` 三种。**第二是主语层级**：政策执行阶梯的主语是 Business Account，Direct Send 阶梯的主语是 WABA／Messaging account，限额档位的主语是 business portfolio（**2025 年 10 月 7 日**起），消费者封禁的主语是 phone number——四层主语之间官方没有给出任何映射句，本篇不把跨层数字连用。**第三是"档位数量"与"档位时长"都不闭合**：政策执行页的封锁时长写成"1 or 3 day"与"5, 7, or 30 day"两组区间而不是确定值，Direct Send 的限速一级只写"temporary rate-limit cap"而不给数字，模板暂停写 **3 小时／6 小时／Disabled** 三档但明确"initially not impact the business phone number"，三处都是官方主动留白（见第 13 章）。）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

"WhatsApp 群发／群控会不会被封号"这类问题在公开渠道里通常只有一个答案：**会，因为条款禁止**。但官方文本其实给出的是远为细化的东西——一套分级处置、一组枚举、一串时长与几个申诉入口。本篇的工作是把这些规则层内容按官方原句逐条登记，并回答十二个问题。

1. 官方写了**几套**处置体系？——**三套互不相通**：Business Platform 政策执行、Direct Send 完整性、帮助中心消费者端；另加一条**独立于阶梯之外**的模板质量平面。本篇另在 Business 应用端与 Business Platform 端各登记 **1** 条官方页面互指关系（第 2 章，附表 C 共 **37** 档）。
2. 阶梯各有几级？——政策执行页 **4** 级（警告 → **1 或 3 天** → **5、7 或 30 天** → 无限期锁）加**永久停用**共 **5** 档，另有 **2** 条阶梯外条款；Direct Send 页 **5** 级；帮助中心 **5** 项（第 3、5、11 章）。
3. "群发"本身是什么违规类型？——**不是违规类型**。政策执行页的 **19** 个 `violation_type` 码**全部**是交易品类，零个描述发送行为；"bulk messaging／auto-messaging／auto-dialing"只出现在服务条款与消息指南（第 4 章）。
4. 每级持续多久？——官方给出的是两组区间与两个确定值：**1 or 3 day**、**5, 7, or 30 day**、`indefinite`、Direct Send 的 **7 days** 与 **30 days**（第 3.3、5.2 节，附表 B）。
5. 哪一级能申诉？——政策执行页写"Not all spam violations might be appealed"；Direct Send 写每个被标记模板**只能复核一次**、须在 **60 days** 内提出；帮助中心写"If you don't see an option to request a review, it means the ban is final"（第 10 章）。
6. 申诉多久出结果？——**四个不同的数**：**24 to 48 hours**、**24 hours**（模板暂停）、"a few days"（Direct Send）、**24 hours**（消费者端）（第 10.4 节）。
7. 平台在处置时通过什么渠道通知？——官方写明 **3** 个渠道（Business Manager 通知中心＋WhatsApp Manager 横幅、发给全部 admin 的邮件、webhook），入口 **2** 个（Business Support Home、WhatsApp Manager）（第 9 章）。
8. Webhook 层能观察到什么？——`account_update` 的 **16** 个事件、**9** 个 `restriction_type`、**8** 个 Direct Send `violation_type`、**6** 个 `alert_type`、**4** 个复核决策（第 8 章，附表 D）。
9. 同步错误码与阶梯怎么对应？——**11** 个码里，**4** 个直接落在处置链上（`131064`／`132015`／`139200`／`131049`），其余属于窗口、吞吐与注册（第 12 章，附表 E）。
10. 限额档位算不算处罚？——**不算**。`250／2,000／10,000／100,000／UNLIMITED` 是投递规模档位；但 `phone_number_quality_update` 页的 **7** 个 TIER 里多出一个 `TIER_50`，是任何现行阶梯页都没有的档位（第 7 章、13.2 节）。
11. 消费者端与商业端是同一条阶梯吗？——**不是**。帮助中心写"Any user engaging in spam, automated, or bulk messaging could have their account `banned`"，但帮助中心的封禁没有"天"的概念：只有**临时**、**永久**、**受限**三种，且受限"automatically ends"（第 11 章）。
12. 官方没写什么？——第 13.2 节正文列 **11** 项、附表 F 共 **16** 项，包括限速一级的具体数字、`the enforcement window` 的长度、两级封锁之间是否可跳过、`indefinite` 账号锁的申诉时限、负面反馈到处置的量化阈值、体系 A 与体系 B 之间的对应表等。

一句话结论先给：**官方文本里的"处罚"不是一条阶梯，而是三条并行、主语不同、时长口径不同、申诉规则不同的阶梯，外加一条独立于阶梯的模板质量平面；而"群发"这个词本身在这四条线里都不是一个可判定的违规类型。**

# 1 来源层、术语与本篇不做的事

## 1.1 四类来源与各自的语言

本篇只用四类官方文本，它们的措辞风格不同，不能混引。

**第一类是 Meta 商业平台开发者文档**（`developers.facebook.com/documentation/business-messaging/whatsapp/`，本篇 **26** 页）。这一类的语言是**规格语言**：有枚举、有表、有错误码、有 payload 示例。政策执行阶梯、Direct Send 阶梯、模板暂停、限额档位、九个 webhook 参考页都在这里。

**第二类是帮助中心**（`faq.whatsapp.com`，本篇 **13** 页，逐篇以文章 ID 引用）。这一类的语言是**用户语言**：讲"你会看到什么提示、你能点什么按钮"。它不给出枚举，也不给出档位编号。

**第三类是法律页**（`whatsapp.com/legal/`，本篇 **5** 页）。这一类的语言是**合同语言**：`bulk messaging`、`auto-messaging`、`auto-dialing`、`unauthorized or automated means` 这些定义出现在这里。

**第四类是 Business 政策页**（`whatsappbusiness.com/policy/`，**1** 个登记位）。它与法律页下的 Commerce Policy 登记位是**同一个页面的两个域名**（本篇做了字节比对，两者 SHA-1 前缀相同），所以本篇在附表 A 里把两者都登记、在正文里只当作一个来源引用。

四类合计 **45** 个登记位，其中 **5** 页本次正文未取回。未取回的处理与系列前两篇一致：**全篇不引用其内容**，只在附表 A 里保留登记位与原因。

## 1.2 "群发／群控"在官方文本里不是术语

这是本篇最重要的一条口径，必须先说清楚。

把本篇涉及的官方文本里所有相关词列出来，会得到一张不对称的表：

| 中文说法 | 官方英文原词 | 出现的文本层 | 是否是一个"可判定的违规类型" |
|---|---|---|---|
| 群发 | `bulk messaging` | 服务条款、消息指南、帮助中心（负责任使用） | 否——只在行为禁令句里，不在任何枚举里 |
| 自动消息 | `auto-messaging` | 服务条款、消息指南、帮助中心 | 否——同上 |
| 自动拨号 | `auto-dialing` | 服务条款、消息指南、帮助中心 | 否——同上 |
| 自动化 | `automated`／`automation` | 服务条款、消息指南、消息指南检测节 | 否——`Automated Processing` 一词在指南里指的是**平台自己的**审核方式 |
| 滥用举报 | `abuse reporting tools` | 消息指南 adversarial behavior 一句 | 否 |
| 抓取 | `scraping`／`harvesting personal information` | 帮助中心两页、消息指南 | 否 |
| 非官方客户端 | `unofficial apps` | 帮助中心 **4** 页 | 否——但它是唯一有专门页面的行为 |
| 品类违规 | `ADULT`／`ALCOHOL`／`WEAPONS` 等 **19** 码 | 政策执行 violations 页 | **是**——这是 API 层唯一可判定的违规类型集合 |
| 分类错误 | `DIRECT_SEND_UTILITY_CATEGORY_ABUSE*` 等 **8** 码 | Direct Send 页 | **是**——但只在 Direct Send 一条线上 |
| 低质量 | `RED`／`Low quality` | 模板质量与暂停页 | **是**——但触发的是暂停而非阶梯 |

表里最后一列是关键：**开发者文档层的"可判定违规类型"只有三组**——`violation_type` 的 **19** 个品类码、Direct Send 的 **8** 个分类滥用码、模板平面的质量档位。**没有一组叫"群发"**。换言之，在 API 与 webhook 这一层，平台不是按"你发了多少条"来给出一个违规码的，而是按"你发的是不是营销内容冒充了工具类"、"你卖的是不是受限品类"、"这个模板有没有被用户投诉到低质量"来给码。

"群发"作为一个**行为**被禁止，是在**条款与指南**层完成的（第 4.2 节、第 8 章）；作为一个**可观察信号**被处理，是在**质量**层完成的（第 6 章）。中间那根"发送量"的轴，官方给了它一个完全不同的名字：**messaging limit**（第 7 章），而且它不是处罚。

## 1.3 三套身份，三套阶梯

同一批文本里，"账号"这个词有三种所指，本篇每次引用都标注是哪一种：

| 身份 | 官方主语 | 处置体系 | 本篇章节 |
|---|---|---|---|
| 个人号／Messenger 用户 | phone number／account | 帮助中心：`Temporarily banned`／final ban／`Restricted accounts` | 11 |
| Business 应用用户 | WhatsApp Business account | 帮助中心专门页，**明确把 Platform 的处置另指** | 11.5 |
| Business Platform 企业 | Business Account／WABA／Messaging account | 政策执行阶梯 **＋** Direct Send 阶梯 **＋** 模板平面 | 3、5、6 |

第三行的三个主语本身在官方文本里也不是同一个对象，而官方**没有**给出它们之间的映射句。这件事在附表 F 里同时登记两处：冲突族第 **4** 条（`restriction` 的三个主语）与未写明第 **3** 项（两页之间没有对应句）。

## 1.4 本篇不做的事

- **不做**任何"怎样发更多而不被发现"的分析。本篇列出检测机制的官方描述，是为了说明"哪些信号已经被平台声明为可用"，不是为了给出规避方法；任何规避性推断本篇一律不写。
- **不做**申诉话术。本篇只抄官方写明的申诉入口、时限与结果枚举，不给任何"怎么写更容易通过"的建议。
- **不推测**官方未给出的数字。凡官方写成区间（`1 or 3 day`）、写成"unspecified threshold"、或根本没写数字（Direct Send 的限速上限），本篇一律原样引用并进附表 F 的未写明清单。
- **不跨体系搬运时长**。政策执行页的 **30 day** 和 Direct Send 页的 **30 days** 数字相同，但它们是两个不同体系的两个不同东西，本篇不合并。

# 2 三套并行的处置体系

## 2.1 总览

官方文本里能读出的处置体系有三套，加一条不在"阶梯"里的模板平面：

| 体系 | 出处页 | 阶梯级数 | 最长时长 | 阶梯外条款 | 申诉 | 本篇附表 C 档数 |
|---|---|---|---|---|---|---|
| A. Business Platform 政策执行 | `policy-enforcement` | **5**（警告＋三级封锁＋无限期锁＋永久停用） | `indefinite` | **2**（严重伤害立即下线、负面反馈独立触发） | 部分可申诉 | **9** |
| B. Direct Send 完整性 | `direct-send/integrity-and-content-guidelines` | **5**（警告→限速→7 天→30 天→永久吊销） | Permanent | **1**（复核不解除账号级限制） | 每模板一次、**60 days** 内 | **10** |
| C. 帮助中心消费者端 | 4 篇封禁文章 | **3**（临时／永久／受限） | 永久 | **2**（非官方应用、抓取） | 有入口但"final"即不可申诉 | **5** |
| D. 模板质量平面 | `template-pausing`／`template-quality`／`template-pacing` | **3** 实例档 | `Disabled` | **2**（pacing、限速窗口） | **24 hours** | **8** |
| — | Business 应用端专页 | 互指 | — | — | — | **1** |
| — | Business Platform 端专页 | 互指 | — | — | — | **1** |

三套体系之间**没有任何一处官方文本做过对齐**。附表 C 之所以把七组分列，就是为了避免做出"A 的第 3 级等于 B 的第 3 级"这类比较——官方没这么说。

## 2.2 体系 A：主语是 Business Account

政策执行页的第一句就定了主语：

> "To maintain high-quality experiences at scale on the WhatsApp Business Platform, WhatsApp will enforce on WhatsApp accounts that repeatedly violate the WhatsApp Business Messaging Policy, the WhatsApp Commerce Policy, or the WhatsApp Business Terms of Service."

三个被违反的对象都是**政策文件**，不是行为规则——这一句里没有任何"发送量"的概念。而 `repeatedly` 这个词是整条阶梯的钥匙：**体系 A 的阶梯不是按一次违规的严重度分级，而是按重复次数分级**（"repeatedly violate … they might start seeing messaging restrictions that gradually increase in duration"）。

## 2.3 体系 B：主语是 WABA 与 Messaging account

Direct Send 页的主语换成两种技术对象：

> "Meta applies a temporary rate-limit cap to the WABA. It can still send utility messages up to a capped volume; once sends reach the cap, Meta rejects further utility sends with error `131064`."

同一页的下一级又写 "The **Messaging account** can't use Direct Send for utility or authentication messaging."——**同一个阶梯里，两级的主语不同**（WABA 与 Messaging account）。本篇按原样登记，不做统一。

## 2.4 体系 C：主语是 phone number

帮助中心的表述完全没有阶梯语言。`About account bans` 一句给出的是封禁与复核：

> "We ban accounts if we believe the account activity violates our Terms of Service, for example if it involves spam, scams or if it puts WhatsApp users' safety at risk."

`About restricted accounts` 给出的是另一种东西——受限而不是封禁，且**自动到期**：

> "The restriction automatically ends once the allocated time passes and you'll receive a notification in WhatsApp when it ends."

这里没有任何天数。官方只写"the allocated time"，并且给出一个查看剩余时间的方法（`New Chat` 里选一个从未聊过的联系人，弹窗显示剩余时间）。**体系 C 的受限时长没有任何数字**——本篇把它登记在附表 F 的补登留白里（第 13.2 节末段四项之首），不在正文十一项里，因为它属于消费者端而不是处置阶梯。

## 2.5 平面 D：模板质量不是一条阶梯，而是三条正交的线

模板层的三个概念经常被混为一谈，官方文本把它们分得很开：

- **质量评级**（`GREEN`／`YELLOW`／`RED`／`UNKNOWN` 四档）是对**模板**的评价，输入是"usage, customer feedback, and engagement"三个信号。
- **暂停**（pausing）是质量落到 `RED` 之后的**动作**，按实例计数 **3 小时／6 小时／Disabled** 三档。
- **投放节奏**（pacing）是**另一个动作**，官方明说"valid for marketing and utility templates"，且对 utility 模板只在"曾经有 utility 模板被暂停"之后才可能适用，窗口是 **7 days**。

三者与体系 A、B 的关系，官方只写了**一句**：

> "Pausing will initially not impact the business phone number … if a business consistently sends message templates that reach a Low quality status, the phone number may eventually be impacted."

`initially not impact` 与 `may eventually be impacted` 是这篇文档里唯一把模板平面与号码层级连起来的话，而它**不是**一条规则句，是一句带两个限定词的倾向描述。附表 F 只收两类——两份文本写法不同、与官方主动留白——这句两可的描述两类都不属于，本篇因此**不把它放进附表 F**，也不据此推出任何"发多少次低质量模板会动号码"的结论。

## 2.6 三套体系为什么必须分列

把三套体系合成一张"WhatsApp 封号阶梯表"是公开讨论里最常见的做法，而它在官方文本里没有依据。三处硬差异：

1. **时长轴不同**：体系 A 用 `day` 且给区间；体系 B 给确定值（**7**、**30**）；体系 C 不给数字。
2. **可发内容不同**：体系 A 的第一级只封锁"marketing, utility, and authentication template messages"（即模板消息全类），第二级封锁"any messages"；体系 B 封锁的是"Direct Send for utility or authentication messaging"；体系 C 的受限描述封锁的是"start chats or calls with new contacts, create new groups, add new members to groups, or use linked devices"，同时明确"你可以继续和聊过的联系人通信"。
3. **申诉语义不同**：体系 A 是"appeal"，结果是 `Unchanged`／`Reversed`；体系 B 是"request a review"，结果是四态（`Available for review`／`In review`／`Reversed`／`Unchanged`）；体系 C 是 `Request review` 按钮，且"如果没有这个按钮，就是终审"。

# 3 体系 A：Business Platform 政策执行阶梯

## 3.1 原文与逐档拆分

政策执行页把阶梯写成四个 bullet，本篇逐字登记：

> "- 1 or 3 day block on sending marketing, utility, and authentication template messages and adding additional phone numbers to the account
> - 5, 7, or 30 day block on sending any messages and adding additional phone numbers to the account
> - An account lock, which is an indefinite block on sending any messages; can only be removed via an appeal
> - Eventually be permanently disabled from the WhatsApp Business Platform, if the business does not make changes after multiple warnings and feature limits or blocks"

加上前一句"Initially, WhatsApp accounts will get a warning with information on the policy they violated."，完整阶梯是 **5** 档：**警告、1 或 3 天封锁、5/7/30 天封锁、无限期账号锁、永久停用**。附表 C 的体系 A 共 **9** 行 = 这 **5** 档 + **2** 条阶梯外条款（严重伤害、负面反馈）+ **1** 条回滚（复核结果）+ **1** 条不可申诉声明。

## 3.2 第一档：警告

> "Initially, WhatsApp accounts will get a warning with information on the policy they violated."

这一句里唯一的信息量是**警告必须带"违反了哪条政策"的指向**。它没有时长——警告不是一个"持续 N 天"的状态，是一条通知。第 9 章给出警告的通知渠道与内容结构。

## 3.3 第二、三档：两组区间而不是两个值

这是体系 A 最容易被误读的地方。官方写的是 **1 or 3 day** 与 **5, 7, or 30 day**——**两组各三个候选值中的两个与一个**，不是"1 天、3 天、5 天、7 天、30 天"五个连续档位。本篇在附表 C 里按两行登记，每行的"时长"列原样写 `1 or 3 day`／`5, 7, or 30 day`。

两档封锁的**范围差别**是明确的：第二档封锁"marketing, utility, and authentication template messages"，第三档封锁"any messages"。第二档的措辞值得注意——它把三类模板消息**全部**列出，等于封锁了平台侧的主动发起能力；但**没有**提到客服窗口内的会话消息。本篇不据此推断客服窗口是否仍可发，官方在这里没写。

两档共同封锁的第二件事，在两个 bullet 里逐字重复：**adding additional phone numbers to the account**。也就是说封锁不只作用于发送，还作用于账号扩张——这是官方文本里唯一直接触及"多号"这个动作的处置句，而它出现在阶梯内部，不在违规类型码里。

## 3.4 第四档：无限期账号锁与"can only be removed via an appeal"

> "An account lock, which is an indefinite block on sending any messages; can only be removed via an appeal"

两个信息：**无限期**，以及**唯一的解除路径是申诉**。这一句同时给出了本篇附表 F 的第二类问题：官方在这一档**没有写申诉的处理时限**，也**没有写申诉次数上限**——**24 to 48 hours** 那一句出现在同页的 Appeals 小节，写的是"the appeal review decision"的一般流程，并没有说它专门适用于账号锁。

## 3.5 第五档：永久停用，以及它的触发条件是"不作为"

> "Eventually be permanently disabled from the WhatsApp Business Platform, if the business does not make changes after multiple warnings and feature limits or blocks"

注意这一档的条件句：**触发它的是"多次警告与限制之后没有做出改变"**，而不是第 N 次违规。也就是说官方在这里给永久停用写的判据是**行为改善的缺失**，与前面几档"重复违反"的判据不同型。

## 3.6 阶梯之外的第一条：严重伤害立即下线

> "In some cases, where there is evidence of a policy violation that causes severe harm to our users, such as child exploitation, scams, terrorism, or the sale of illegal drugs, WhatsApp will immediately offboard these Business accounts."

**4** 个场景、一条动作（`immediately offboard`），**不经过任何阶梯**。这一句在体系 A 里的位置很重要：它说明阶梯不是唯一的处置路径，且"立即下线"的四个场景里，`scams`（诈骗）与 `illegal drugs`（非法药物）同时也是体系 A 的品类码（`SCAM`、`DRUGS`）——同一个词既可以是走阶梯的品类违规，也可以是立即下线的情形，官方没有写出区分两次的判据。

## 3.7 阶梯之外的第二条：负面反馈独立触发

> "We might also limit or offboard your business from WhatsApp if your account receives excessive negative feedback from users."

`excessive negative feedback` 是与"违反政策"并列的另一种触发原因，官方**没有**给它任何阈值。它与模板质量的输入信号（"usage, customer feedback, and engagement"）用的是同一族词，但一个作用于 business、一个作用于 template。本篇把这条登记为体系 A 的第 **7** 行（附表 C 编号 **7**），并在附表 F 未写明第 **8** 行记下"官方未写明负面反馈到处置的量化阈值"。

## 3.8 回滚与不可申诉

同页 Enforcement actions 小节给出回滚句：

> "Restricted or disabled accounts can still appeal or acknowledge issues. If issues are reversed following the appeal or acknowledgment, the account returns to its previous status."

`returns to its previous status` 意味着官方承诺的是**状态回滚**而不是补偿；同时这一句把 **acknowledge（承认）** 与 appeal 并列为两种可用的处置响应——这是本篇在体系 A 里登记的第 **9** 行。

同页 Appeals 小节则给出限制句：

> "Note: Not all spam violations might be appealed. Businesses might have to wait until the restriction period ends to begin messaging again."

`Not all … might be appealed` 是一句主动的留白（哪些可申诉、哪些不可，官方没给清单），而"wait until the restriction period ends"反过来确认了阶梯的时间属性是**必须等满**的。

## 3.9 违规更新说明的五项内容

政策执行页"Understanding violations"小节列出违规更新应包含 **5** 项：

> "- Summary of policy violated and link to the policy itself.
> - Examples of which content is allowed or disallowed based on that policy.
> - Whether there are any active restrictions on the account and what happens if the violation happens again.
> - How to avoid future policy violations and links to helpful resources.
> - How to appeal."

第 **3** 项值得单独指出：官方要求通知里写明"如果再次发生会怎样"（`what happens if the violation happens again`）。这是整篇阶梯文档里唯一一句把"预告下一级"写成义务的话。

# 4 违规类型码 19 项：全是品类，零个发送行为

## 4.1 清单与性质

`policy-enforcement/violations` 页给出 **19** 个 `violation_type` 码，官方在页首说明这是"specific policy violations a business could receive"，并加了一句" We may adjust this list over time."。逐码登记见附表 E 的第一组（**19** 行）。

按性质分组：

| 组 | 码 | 数 |
|---|---|---|
| 受禁品类交易 | `ADULT`、`ALCOHOL`、`TOBACCO`、`DRUGS`、`SUPPLEMENTS`、`WEAPONS`、`ANIMALS`、`BODY_PARTS_FLUIDS`、`HEALTHCARE`、`REAL_FAKE_CURRENCY`、`DIGITAL_SERVICES_PRODUCTS`、`ILLEGAL_PRODUCTS`、`UNAUTHORIZED_MEDIA`、`THIRD_PARTY_INFRINGEMENTS` | **14** |
| 受禁服务形态 | `DATING`、`GAMBLING`、`OVERTLY_SEXUALIZED_POSITIONING` | **3** |
| 欺诈与误导 | `SCAM`、`MISLEADING` | **2** |
| **合计** | — | **19** |

三组相加 **19**，无第 **20** 项。**这 19 个码里没有任何一个描述"发送"这件事本身**——既没有 `SPAM`，也没有 `BULK_SENDING`，也没有 `HIGH_VOLUME`。最接近的是 `SCAM` 与 `MISLEADING`，但两者描述的是内容性质，不是投递方式。

这是本篇的中心留白：**"群发"在 API 层不是一个可判定的违规类型**。政策执行页在描述阶梯时确实写了 `sending spam`（"repeatedly violate the WhatsApp Business Terms of Service, such as sending spam, template misclassifications, or high-risk policy categories"），把 spam 与模板误分类、高风险品类并列成三类被阶梯覆盖的行为——但这句话是散文，**它列举的三种行为在 `violation_type` 枚举里只有一种（品类）有对应码**。

## 4.2 同一页内的两处不对称

逐码读这 **19** 行的描述，会发现两组内部不对称：

**第一组：`ADULT` 与 `OVERTLY_SEXUALIZED_POSITIONING`。** 前者的禁令句是"Businesses may not transact in the sale or use of adult products or services"，例子却包含一条**豁免**："Products promoting family planning and contraception, which focus on the contraceptive features of the product, and not on sexual pleasure or sexual enhancement"。也就是说在 `ADULT` 下面，同一个例子列表里既有被禁的（sex toys）又有被允许表述的（以避孕功能为主的计生产品）。这是官方在这一页里唯一一处"例子列表内部包含允许项"的地方。

**第二组：`DRUGS` 与 `HEALTHCARE` 各带一句生产者豁免。** `DRUGS` 写"Manufacturers and healthcare services who do not engage in direct sale are allowed"，`HEALTHCARE` 逐字重复了同一句。而 `ALCOHOL`、`TOBACCO`、`WEAPONS` **没有**这句豁免。同一条豁免句只出现在两个码下面，官方未说明为什么。

## 4.3 与服务条款的关系

`violation_type` 页顶部的链接指向 Business Messaging Policy 与 Commerce Policy，而不是服务条款。但 `SCAM` 与 `MISLEADING` 的禁令句在 Messaging Guidelines 里有对应条目（"Do not engage in fraud or spam"，见第 8.2 节），而服务条款的 `bulk messaging` 禁令**没有**对应的 `violation_type` 码。三者的层级关系是：

| 文本 | 写了什么 | 有没有对应的机器可读码 |
|---|---|---|
| Terms of Service | `bulk messaging`、`auto-messaging`、`auto-dialing`、`create accounts … through unauthorized or automated means` | **无** |
| Messaging Guidelines | adversarial behavior（含 unofficial clients、bulk/auto-messaging/auto-dialing、automation）、fraud or spam | **无** |
| Business/Commerce Policy | 受禁品类与受限制品类 | **有**，即 `violation_type` 的 **14**＋**3** 项 |
| Direct Send 页 | utility 分类里夹带营销 | **有**，即 **8** 个 `DIRECT_SEND_*` 码 |

结论是：**机器可读的违规码只覆盖"你卖什么"和"你把什么类别冒充成什么类别"，不覆盖"你怎么发"**。"你怎么发"只在合同与指南层被禁止，它的处置入口是 `account_update` 的 `ACCOUNT_VIOLATION`／`ACCOUNT_RESTRICTION` 事件与 `POLICY_ENFORCEMENT_ACTION`，而不是某个品类码。


# 5 体系 B：Direct Send 的五级升级

## 5.1 前提：Direct Send 只能发 utility 与 authentication

Direct Send 完整性页第一句划定了这条线适用的消息范围：

> "Direct Send is limited to utility and authentication messages, and Meta actively monitors how messages are categorized against Meta's template guidelines. The same integrity rules that apply to user-created templates also apply to Direct Send generated templates."

`The same integrity rules … also apply to Direct Send generated templates` 这句是关键：Direct Send 的模板是系统自动生成的（`source: AUTO_GENERATED`），但平台把**人工模板的完整性规则同样套上去**。

## 5.2 五级阶梯原文

> "| Warning | Meta notifies you of the misuse. You can request a review of the templates to resolve the issue. | Ongoing |
> | Rate limiting | Meta applies a temporary rate-limit cap to the WABA. It can still send utility messages up to a capped volume; once sends reach the cap, Meta rejects further utility sends with error `131064`. The limit lifts automatically — see the expiration in the `ACCOUNT_RESTRICTION` webhook. | Until utility volume falls back under the cap or the enforcement window ends |
> | Messaging restriction | The Messaging account can't use Direct Send for utility or authentication messaging. | 7 days |
> | Extended restriction | Continued misuse extends the restriction. This is the final warning before permanent revocation. | 30 days |
> | Permanent revocation | Direct Send access is permanently revoked. | Permanent |"

五级逐条读，有四处与体系 A 不同：

1. **限速一级的数字缺失**。官方写"a capped volume"、"up to a capped volume"，**没有给出任何具体上限值**，也没有给出这个 cap 怎么算。这是本篇附表 F 未写明清单的第 **4** 行（正文第 13.2 节第 4 项），也是整个 Direct Send 阶梯里唯一的空档。
2. **限速一级的持续时间是条件句**而不是天数："Until utility volume falls back under the cap or the enforcement window ends"——两个终止条件，且第二个（`the enforcement window`）在本文档里没有定义长度。
3. **后两级是确定值**：**7 days** 与 **30 days**，这与体系 A 的"5, 7, or 30 day"区间写法完全不同。
4. **第三、四级的动作对象是能力**而不是内容：`can't use Direct Send for utility or authentication messaging`。也就是这一级封锁的是"用 Direct Send 发这两类"，而不是"发任何消息"。

## 5.3 八个 `violation_type` 与三态 payload

Direct Send 页给出 **8** 个 `violation_type`，且明确"Every notification has an `event` of `ACCOUNT_RESTRICTION` and uses the same payload shape; the `violation_type` value — and whether a `restriction_info` object is present — identifies the restriction stage."

按 payload 形态分三组：

| 形态 | `restriction_info` | 对应 `violation_type` | 数 |
|---|---|---|---|
| 警告 | **无** | `DIRECT_SEND_UTILITY_CATEGORY_ABUSE_WARN`、`DIRECT_SEND_UTILITY_TEMPLATE_ABUSE` | **2** |
| 生效中的限制 | **有**（数组，含 `restriction_type` 与 `expiration`） | `…_RATE_LIMIT`、`…_STRIKE_1`、`…_STRIKE_2`、`…_OFFBOARD` | **4** |
| 限制解除 | **无** | `…_UNBAN`（restriction lifted）、`…_RATE_LIMIT_RECOVERY`（rate limit recovered） | **2** |
| **合计** | — | — | **8** |

三个结构性的观察：

- 有 `restriction_info` 的 **4** 个码要覆盖阶梯里限速／**7 天**／**30 天**／永久吊销**四**级——`STRIKE_1`／`STRIKE_2`／`OFFBOARD`／`RATE_LIMIT` 恰好一一对应，但**官方从未写出这张对应表**。本篇在附表 C 里把两页并排登记，并把"两页之间缺少对应句"列为官方留白（第 13.2 节第 **3** 项、附表 F 未写明第 **3** 行）。
- 两个警告码里，`DIRECT_SEND_UTILITY_TEMPLATE_ABUSE` 的后缀是 `TEMPLATE_ABUSE` 而不是 `CATEGORY_ABUSE`，它是**唯一**一个不带 `CATEGORY` 的码；官方没有说明模板滥用与分类滥用在这条线上的分工。
- 解除态用 `UNBAN` 这个词——它是 Direct Send 文档里唯一出现 `ban` 词族的地方，而"ban"在帮助中心是另一套含义更强的术语（第 11 章）。同一个词在两层里指不同事情，本篇不合并。

## 5.4 `restriction_type` 9 项：这条线只用到一项

体系 B 的 payload 里出现的是 **9** 个 `restriction_type` 枚举值中的一项：`RESTRICTED_DIRECT_SEND_UTILITY_TEMPLATES`。把 **9** 项全列出来（附表 D 的 restriction_type 组），可以看到这套枚举实际上是跨体系共用的能力清单：

| 被限制的能力 | 官方描述 |
|---|---|
| `RESTRICTED_ADD_PHONE_NUMBER_ACTION` | Business cannot add new phone numbers to the account. |
| `RESTRICTED_BIZ_INITIATED_MESSAGING` | Business cannot initiate conversations with customers. |
| `RESTRICTED_CUSTOMER_INITIATED_MESSAGING` | Business cannot respond to customer-initiated messages. |
| `RESTRICTED_UTILITY_TEMPLATES` | Business cannot create utility templates. |
| `RESTRICTED_DIRECT_SEND_UTILITY_TEMPLATES` | Business cannot send utility templates via Direct Send. |
| `RESTRICTED_BIZ_INITIATED_CALLING` | Business cannot initiate outbound calls. |
| `RESTRICTED_BIZ_INITIATED_AND_USER_INITIATED_CALLING` | Business cannot make or receive calls. |
| `RESTRICTED_USER_INITIATED_CALLING` | Business cannot receive inbound calls from users. |
| `RESTRICTED_USER_INITIATED_CALLING_CALL_BUTTON_HIDDEN` | Call button is hidden from users due to low pickup rates. |

三点值得注意：**发送与应答被拆成两个不同的 restriction_type**（`BIZ_INITIATED` 与 `CUSTOMER_INITIATED`）；**创建模板与发送模板也被拆开**（`RESTRICTED_UTILITY_TEMPLATES` 说的是 cannot create，`RESTRICTED_DIRECT_SEND_UTILITY_TEMPLATES` 说的是 cannot send）；而 **9 项里有 4 项与通话有关**，最后一项甚至不是封锁而是**改界面**（把呼叫按钮对用户隐藏），其判据官方写成"due to low pickup rates"——这是全篇唯一一处把"接听率"写成处置依据的话。

`RESTRICTED_ADD_PHONE_NUMBER_ACTION` 这一项在体系 A 的两级封锁句里被逐字重复（"adding additional phone numbers to the account"），这是两套体系之间**唯一**一处可以逐字对齐的点。本篇把它写在附表 F 未写明第 **3** 行的括号里（作为"两页之间没有对应句"的例外注记），而不是登记为一条冲突。

## 5.5 限制期间的同步错误码

> "If you attempt to send during a restriction period, the Cloud API synchronously returns error `139200`"

错误文本是 `(#139200) Direct Send Utility access is blocked`，`error_data.details` 写"Direct send messaging capability is not available for this WABA right now due to misclassification based enforcements"。这一句里 `due to misclassification based enforcements` 是官方对**整条体系 B 的定性**：它是一条**因分类错误而执行**的处置线，不是因内容、不是因数量。

## 5.6 通知的两条渠道与三步序列

体系 B 的通知渠道只有两条（比体系 A 的三条少一条）：

> "- **Email**: Sent to all Messaging account admins (people with **Full control** of the account) when a warning, restriction, or lift occurs.
> - **Webhook**: An `account_update` webhook is triggered with the `restriction_info` object reflecting the current enforcement state."

括号里那句把"admin"定义成"people with **Full control** of the account"——这是 Direct Send 页唯一一处定义收件人范围的话。

同一页给出 **3** 步通知序列：模板通知 → 警告 → 限制。序列的意义在于**它把"模板级信号"写在"账号级处置"之前**：官方明确"The first signal of misuse comes through template notifications"，并且建议"request a review as soon as you receive a template notification"。

## 5.7 复核的五条须知

`Things to know about flagged-template reviews` 小节 **5** 条，逐条都是规则而不是操作：

1. **窗口**："Request a review within 60 days of the misclassification notification (the `template_correct_category_detection` webhook). After that, the template is no longer eligible for review."
2. **一次**："Each flagged template can be reviewed once. To have different content evaluated, change the messages you send through the API — a new template is auto-generated and classified fresh."
3. **复核期间不断流**："Requesting a review doesn't pause your access. You can keep sending Direct Send messages while Meta evaluates the template."
4. **自动生成模板不可编辑**："You can't edit auto-generated templates in WhatsApp Manager. To change what's classified, change the messages you send through your API calls."
5. **复核只恢复分类，不解除账号限制**："A review restores a template's classification, but doesn't lift account-level restrictions. Any account restrictions lift on their own schedule as your recent misclassification rate falls back under the threshold."

第 **5** 条是整篇 Direct Send 文档里最重要的一句：**模板复核与账号阶梯是两条互不相干的线**。而它同时留下了一个新的未写明项——账号级限制的自行解除取决于"your recent misclassification rate falls back under the threshold"，而**这个 threshold 官方没有给数字**。

第 **2** 条里的"a new template is auto-generated and classified fresh"也值得单独指出：它意味着**复核机会绑定在模板对象上而不是绑在内容上**，改了发送内容会生成新模板并重新分类，因此不存在"同一个模板复核两次"，也不存在"复核旧模板的新内容"。

## 5.8 复核状态 4 态与三个入口

复核生命周期 **4** 态（`Available for review`／`In review`／`Reversed`／`Unchanged`）里，只有 `In review` 带时间："Most reviews take a few days."——**a few days** 是官方在这一层给出的唯一时长表述，且它是模糊量。

官方给出 **3** 个发起复核的位置：Business Support Home 的 `WhatsApp account > Direct Send template updates`（可批量选择提交）、WhatsApp Manager 的 `Manage templates`（在模板上悬停分类误用指示器）、以及账号已被警告或限制时 WhatsApp Manager 顶部的横幅（横幅文案是"We detected marketing content in your Direct Send API messages"，带 `See flagged templates` 快捷入口）。三个入口最终都落到同一个 `Direct Send template updates` 标签页——这是本篇在附表 F 里登记的一处"多入口单落点"，不算冲突。

# 6 平面 D：模板质量、暂停与投放节奏

## 6.1 质量评级：四档、三个输入、一句后果

模板质量页给出 **4** 个评级值：`GREEN`／`YELLOW`／`RED`／`UNKNOWN`（定义全文见附表 D 的 quality 组）。三档有质量分，`UNKNOWN` 是**尚未有反馈**的初始态——官方写"Indicates a quality score is still pending, because it has yet to receive WhatsApp user feedback or read-rate data."

评级的输入信号官方只写了一句话、**三个**并列项：

> "Template quality ratings are based on usage, customer feedback, and engagement."

这是模板平面里唯一一处说明"评级从哪来"的话，也是本篇把它列入未写明清单的原因：**三个信号各自权重、更新频率、是否按国家分层，官方均未写**。

唯一的硬性后果句是：

> "If the status changes to anything other than `APPROVED`, the template cannot be sent in template messages unless its `APPROVED` status is restored"

注意这句的主语是 `status` 而不是 `quality_score`——质量本身不禁止发送，**状态**才禁止。这两个字段在同一个 API 响应里并存，混淆它们是公开讨论里最常见的错误之一。

Business Manager 侧另有 **4** 个标签：`Active - Quality pending`／`Active - High quality`／`Active - Medium quality`／`Active - Low quality`，官方逐条给出与 API 值的对应（`UNKNOWN`／`GREEN`／`YELLOW`／`RED`）。这是本篇在整套语料里少见的**一一对应句**，所以附表 D 把 **4** 行都登记为 `qualitylabel` 组并保留对应关系。

## 6.2 暂停：三档实例、一句动机、两个日期

暂停页给出 **3** 个实例档：

> "- 1st Instance: Paused for 3 hours
> - 2nd Instance: Paused for 6 hours
> - 3rd Instance: Disabled"

动机句值得逐字读：

> "Meta pauses templates … to protect the quality rating of phone numbers that have used the template"

也就是说暂停的**声明目的**是保护用过该模板的**号码**的质量评级——这与第 2.5 节那句"initially not impact the business phone number"是一体两面：暂停是为了**不让号码被拖下去**，而不是为了处置号码。

其余在规则层有意义的六句：

- **暂停期发送会被拒**："you won't be charged … won't count against your messaging limit, the API will reject these attempts anyway"。三件事并列：不计费、不占限额、但仍被拒。
- **编辑并重新提交即进入复核**：模板改为 `In Review`。
- **申诉须带样本**："appeals must include a sample"。
- **申诉时限**："The appeal will be reviewed and a decision made within 24 hours."
- **自动解暂停**："A template will unpause on its own after satisfying the pause duration."，而 pacing 期间被暂停的模板**须手动解除**："templates paused during Template Pacing must be manually unpaused"。
- **解除后的质量重置**："reset to a value based on the most recent customer feedback"——不是回到暂停前的值。

生效日期有两组：**巴西、哥伦比亚、新加坡 2023 年 9 月 12 日**，**全部企业 2023 年 10 月 12 日**。通知面在这一层是三条并列：Manager 通知、邮件、`message_template_status_update` webhook。

## 6.3 投放节奏：适用于哪类、阈值不写、7 天窗口

节奏页第一句限定适用集合："valid for marketing and utility templates"。被观察的对象是三种模板：**新建的、被解除暂停的、非 `GREEN` 的**。核心那句是官方主动的留白：

> "These templates are sent normally until an unspecified threshold is reached."

`unspecified threshold` 是明确写在文档里的"不告诉你数字"。对 utility 模板，节奏控制是**条件触发**的：只有当某个 utility 模板曾被暂停，utility 模板才"potentially subject to pacing for the next 7 days"。

节奏期的技术表现有三个可观察点：

- `message_status` 取 `accepted` 或 `held_for_quality_assessment`；
- 若反馈为负，模板 `status` 变 `PAUSED`、webhook 事件为 `paused`，被挂起的每条消息以 `"status":"failed"`、`"code":"132015"` 落地；
- 另有 `message_template_quality_update` webhook，且管理员通过 Business Suite 通知、Manager 横幅与邮件被告知。

交付承诺只有一句：**"campaign messages with highest throughput still get delivered within an hour (99 percentile)"**。而这一句还有后半条件：如果内部护栏先被触及，被挂起的消息会**正常释放**。也就是说"一小时内 99 百分位"这个承诺与"护栏是否先触发"绑在一起，官方没有给出护栏的定义。

## 6.4 单用户营销上限：一条与国家相关的硬开关

`Per-user marketing template message limits` 页给出另一条与账号阶梯完全无关的限制线：**按收件人**计的营销模板上限。规则层有四点：

1. 判据是动态的：官方写"based on several factors, including a dynamic view of an individual's recent marketing message read rate and how many messages they currently have in their inbox from friends, family, and businesses."
2. **美国号码是硬开关**："WhatsApp does not currently deliver marketing template messages to WhatsApp users with United States phone numbers (numbers composed of a +1 dialing code and a US area code)."
3. 计数规则："Each marketing template message delivered counts towards the per-user marketing limit. If a WhatsApp user responds to a marketing message, it starts a 24-hour customer service window. Marketing messages sent within this window do not count towards the limit."
4. 重试惩罚："If your Messaging account attempts to resend marketing messages multiple times within a 24-hour period to users who have already reached their messaging limit, further delivery attempts to these users may be unavailable for up to 24 hours and error code 131049 will be returned. This does not affect your ability to send marketing messages to other users."

第 **4** 点是全套语料里唯一一处**因重试行为而施加的、按收件人隔离的**限制——它不进任何阶梯、不改任何 `restriction_type`，只把该收件人上的投递关闭"最多 **24 小时**"。本篇把它单独登记在附表 C 之外（第 12 章的错误码映射里），并把"`multiple times` 未给出次数"登记为附表 F 未写明清单的第 **16** 行（第 13.2 节末段的五项补登之一）。

## 6.5 模板状态事件的 14 个值

`message_template_status_update` webhook 参考页给出 **14** 个 `event` 值：`APPROVED`、`ARCHIVED`、`UNARCHIVED`、`DELETED`、`DISABLED`、`FLAGGED`、`IN_APPEAL`、`LIMIT_EXCEEDED`、`LOCKED`、`PAUSED`、`PENDING`、`PENDING_DELETION`、`REJECTED`、`REINSTATED`（全量见附表 D 的 tpl_event 组）。

其中与本篇直接相关的 **6** 个是 `PAUSED`、`FLAGGED`、`DISABLED`、`IN_APPEAL`、`REINSTATED`、`LIMIT_EXCEEDED`。两点值得指出：

- `FLAGGED` 的描述写"has received negative feedback and is at risk of being disabled"——**风险态**，不是处置态。
- `REINSTATED` 描述为"no longer flagged or disabled"，即一次恢复动作可以同时清掉两种状态。
- `LIMIT_EXCEEDED` 指向的是 template limit（模板数量上限），与第 7 章的 messaging limit 是两个不同的限额对象，官方用了两个不同的词，本篇不合并。

配套的 `REASON` 枚举 **8** 个具名值（`ABUSIVE_CONTENT`、`CATEGORY_NOT_AVAILABLE`（已标 Deprecated）、`INCORRECT_CATEGORY`、`INVALID_FORMAT`、`NONE`、`PROMOTIONAL`、`SCAM`、`TAG_CONTENT_MISMATCH`），另有一句"the value is `null` instead"对应模板被计划删除的情形，所以这一组在本篇是 **9** 行。四个细节：

- `ABUSIVE_CONTENT`、`PROMOTIONAL`、`SCAM` 三个码的官方描述**逐字相同**（都是"Indicates template contains content that violates our policies."），也就是说这一层的原因码**不可区分**——想从原因码判断"是营销内容、诈骗还是辱骂内容"是做不到的。
- `NONE` 的描述是"Indicates template was paused."，即暂停这个动作的原因字段写的是"无"。
- `INCORRECT_CATEGORY` 与 `TAG_CONTENT_MISMATCH` 的描述都是"content doesn't match the category designated at the time of creation"，也是同文不同码。
- `SCAM` 在这一层是模板拒绝原因，在 `violation_type` 那一层是品类码，两处不是同一个枚举体系的成员。

# 7 限额档位：250 起点、四档晋级与 2026 年 2 月

## 7.1 定义句：数的是"24 小时内去重号码"

限额页的定义句是整套语料里被引用得最多、也最常被引用错的一句：

> "Messaging limits are the maximum number of unique WhatsApp user phone numbers your business can deliver messages to, outside of a customer service window, within a moving 24-hour period."

四个限定词都在轴上：**unique**（去重号码，不是条数）、**deliver**（送达而非提交）、**outside of a customer service window**（窗口内不计）、**moving 24-hour period**（滚动窗口，不是自然日）。

## 7.2 层级句：portfolio 共享

> "Messaging limits are calculated and set at the business portfolio level and are shared by all business phone numbers within a portfolio. This means that if a business portfolio has multiple business phone numbers, it's possible for one number to consume all of the portfolio's messaging capability within a given period."

`one number to consume all of the portfolio's messaging capability` 是官方对"一个 portfolio 里多个号码"的关系描述。本篇把它登记为体系外的**容量**事实而非处置事实：限额页通篇没有任何处罚语义。

## 7.3 起点与四档

> "Newly created business portfolios have a messaging limit of 250, but this messaging limit can be increased to: 2,000 (by completing a scaling path) / 10,000 (via automatic scaling) / 100,000 (via automatic scaling) / Unlimited (via automatic scaling)"

**250** 是起点，**2,000** 是唯一可用"完成一条 scaling path"换取的一档，其后三档只能靠自动升档。升 **2,000** 的 **3** 条 scaling path：

1. Verify your business
2. Have your partner verify your business（若由伙伴接入）
3. "Send 2,000 delivered messages outside of customer service windows to unique WhatsApp user phone numbers within a 30-day moving period, using templates with a high quality rating."

第 **3** 条是唯一带量化的路径，而它的量化对象是**历史投递量与质量**，不是账号数量。

自动升档的判据 **2** 条：

> "- You are sending high-quality messages across all of your business phone numbers and templates
> - In the last 7 days, your business has utilized at least half of your current messaging limit"

判据二是**用量比例**（近 **7 天**用掉当前限额的一半以上），判据一是**质量**且覆盖范围写的是"all of your business phone numbers and templates"。满足后"your portfolio's limit increases by one level within 6 hours"。

**这里没有出现任何处罚概念。** 限额升降既不是阶梯的一档，也不是任何 `restriction_type` 的一项——这是本篇要反驳的第二类混读（第一类见第 4 章）。

## 7.4 拒绝的 3 个 alert_type

升 **2,000** 被拒时，官方用 `account_alerts` 的 **3** 个 `INCREASED_CAPABILITIES_ELIGIBILITY_*` 值表示，并在限额页给出一张"能做什么"对照表：

| `alert_type` | 官方给的下一步 |
|---|---|
| `…_DEFERRED` | 近 30 天滚动期内用高质量模板发出 **2,000** 条窗口外去重号码送达 |
| `…_FAILED` | 同上 |
| `…_NEED_MORE_INFO` | 验证身份，**或**同上 |

三行的差别只在第三行多一个身份验证选项。`alert_type` 参考页对这三个码各有一句更完整的说明，其中 `…_DEFERRED` 写"Meta doesn't have enough message signal to make a determination"、`…_FAILED` 写"messaging limits cannot be increased due to past messaging activity"。也就是说 **DEFERRED 是信号不足，FAILED 是历史行为不合格**——这两个词的差别是全篇里最接近"平台因为你过去的发送行为扣分"的官方表述，但它仍然不是一条处置阶梯，而是一个**资格判定**。

## 7.5 两个 2026 年 2 月要移除的字段

限额页与 webhook 参考页共同记录了字段换代：

- `messaging_limit_tier` **已弃用**，改为请求 `whatsapp_business_manager_messaging_limit` 字段（限额页页首整段 Warning）。
- `business_capability_update` 的旧字段 `max_daily_conversation_per_phone` 服务于 **v23.0 及更早**，新字段 `max_daily_conversations_per_business` 服务于 **v24.0 及更新**；旧字段"until February 2026"移除。
- 旧字段的取值是 **5** 个数字：`250`、`2000`、`10000`、`100000`、`-1`（`-1` 表示不限）。
- 新字段的取值是 **5** 个档位串：`TIER_250`、`TIER_2K`、`TIER_10K`、`TIER_100K`、`TIER_UNLIMITED`。

**同一件事有两套取值域**：数字与 `-1`，档位串与 `TIER_UNLIMITED`。而 `phone_number_quality_update` 页的 `current_limit`／`max_daily_conversations_per_business` 又是**第三套**：**7** 个 `TIER_*`，其中包含任何阶梯页与限额页都没有的 `TIER_50` 与 `TIER_NOT_SET`。本篇在附表 D 里把三套分别登记为 `bcu_legacy_limit`（**5**）、`bcu_tier`（**5**）、`tier`（**7**）三组，不做合并。

## 7.6 2025 年 10 月 7 日的换代

`Upcoming changes to messaging limits` 页现在通篇是过去式说明（页首写"The changes described in this document are now live, and are only here for reference purposes."），它记录了 **2025 年 10 月 7 日**起发生的六项变化，其中四项与本篇直接相关：

| 项目 | 变更前 | 变更后 |
|---|---|---|
| 限额设置对象 | 按单个 business phone number | 按 business portfolio |
| 新注册号码的起点 | **250** | 共享 portfolio 的限额 |
| 达到自动升档资格时的档位 | **1,000** | **2,000** |
| 自动升档耗时 | 满足条件后 **24 hours** | 满足条件后 **6 hours** |
| `Flagged` 质量态 | 可能；连续 **7 天** `Flagged` 立即降一档 | 不再存在；质量下降**不再降限额** |
| 吞吐 **1,000 messages per second** 资格 | 号码可发不限＋质量 `Medium` 以上 | portfolio 限额不限＋该号码在滚动 24 小时内向 **100K** 以上去重号码投递＋质量 `Medium` 以上，满足后 **12 hours** 内自动升 |

**`Flagged` 这一行是全篇最重要的一条历史口径变化**：在 2025 年 10 月 7 日之前，"质量连续 7 天为 Flagged"是一个**会立即降低限额**的动作；此后该状态被取消，质量下降不再降限额。这解释了为什么现行阶梯页里找不到"因质量降限额"的条款——它被删除了，而不是从未存在。本篇把这一条同时登记在附表 B（时长）与附表 F（版本冲突）。

同一页还有一句关于新旧对照的说明：**"if a portfolio has a phone number with a limit of 100K and other phone numbers with lower limits, the portfolio's limit will be set to 100K"**——迁移时取**最高**档。

# 8 account_update 与其他 webhook 枚举

## 8.1 account_update 的 16 个事件与 13 个触发句

`account_update` 是整套处置语料的中枢：体系 A 与体系 B 的账号级动作都落在这一个 webhook 上。参考页的 `EVENT` 枚举 **16** 项，本篇全部登记在附表 D 的 `event` 组。按与处置的相关度分四层：

| 层 | 事件 | 数 |
|---|---|---|
| **直接处置** | `ACCOUNT_VIOLATION`、`ACCOUNT_RESTRICTION`、`DISABLED_UPDATE`、`ACCOUNT_OFFBOARDED` | **4** |
| 生命周期 | `ACCOUNT_DELETED`、`ACCOUNT_RECONNECTED` | **2** |
| 能力与商务 | `AUTH_INTL_PRICE_ELIGIBILITY_UPDATE`、`VOLUME_BASED_PRICING_TIER_UPDATE`、`BUSINESS_PRIMARY_LOCATION_COUNTRY_UPDATE`、`MM_LITE_TERMS_SIGNED`、`AD_ACCOUNT_LINKED` | **5** |
| 伙伴与授权 | `PARTNER_ADDED`、`PARTNER_REMOVED`、`PARTNER_APP_INSTALLED`、`PARTNER_APP_UNINSTALLED`、`PARTNER_CLIENT_CERTIFICATION_STATUS_UPDATE` | **5** |
| **合计** | — | **16** |

同页的 Triggers 小节写的是 **13** 条自然语言触发句，与 16 个枚举值**不是一一对应**（例如"A WhatsApp account or Messaging account is shared ("installed") or unshared ("uninstalled") with a partner"一句话覆盖两个事件值）。本篇以枚举值为准，触发句只用作说明。

四个直接处置事件的描述里有一处容易漏读：`ACCOUNT_VIOLATION` 与 `DISABLED_UPDATE` 的描述**都以**"Indicates your WhatsApp account violated Meta policies or terms"开头——前者是"违反"，后者是"因此被停用"。而 `ACCOUNT_OFFBOARDED` 的描述与第 3.6 节的"immediately offboard"**完全无关**：它写的是"offboarded due to a device change or phone number reregistration"。

**这是本篇登记的最重要的一处术语冲突**：`offboard` 一词在政策执行页表示"因严重伤害立即下线"，在 `account_update` 参考页表示"因换设备或重新注册号码而脱机"。两个意思相差极远，共用一个词根，而官方在两处都没有加任何消歧注。

## 8.2 页首描述句把六类事情并列

`account_update` 页首一句列出了这个 webhook 覆盖的全部情形：

> "…notifies of changes to the associated WhatsApp account's partner-led business verification submission, the Messaging account's authentication-international rate eligibility, or the WhatsApp account's primary business location, when the Messaging account is shared with a Solution Partner, policy or terms violations, offboarding, reconnection, or when the Messaging account is deleted."

六个并列项里，**只有"policy or terms violations"一项与处置有关**。也就是说：接入一个 webhooks 订阅者，会同时收到商务管理事件与处置事件，官方没有提供"只要处置事件"的订阅粒度（至少在这一页没写）。

## 8.3 其余 webhook：触发句的分布

| webhook | 本页触发句 | 主要枚举 |
|---|---|---|
| `account_alerts` | **3** | `alert_type` **6**、`alert_severity` **3**、`alert_description` **8**、`entity_type` **3**、`status` **2** |
| `account_review_update` | **3** | 决策 **4**（approved／rejected／deferred／awaiting more information） |
| `business_capability_update` | **2** | 旧字段 **5** 数值、新字段 **5** 档位 |
| `message_template_status_update` | **5** | 事件 **14**、原因 **8**＋null、标题 **5**、类别 **3** |
| `message_template_quality_update` | — | 质量变化通知 |
| `phone_number_quality_update` | **1** | `TIER_*` **7** |
| `template_category_update` | — | 类别变化 |
| `security` | **3** | 两步验证关闭／邮件重置关闭／PIN 变更或启用 |
| `webhooks`（总览页） | — | 字段清单 |

**`account_alerts` 的 6 个 `alert_type`** 里只有 **3** 个与限额资格有关，另外 **3** 个是 `OBA_APPROVED`／`OBA_REJECTED`（Official Business Account 状态）与 `PROFILE_PICTURE_LOST`（业务头像被删）。`entity_type` 的 **3** 项里有一项是 `CURRENT_STATUS_ID`——它不是对象类型而是字段名，这是本篇登记的一处枚举内部不纯。

**`account_alerts` 的 8 个 `alert_description`** 值得逐条读，因为它们是官方在机器可读层写出的"平台对你说了什么"的原句。其中三句与本篇直接相关：

- "Your messaging quality was too low to unlock more capabilities at this time."（质量太低所以不给解锁）
- "Based on your activity, limits cannot be increased for your business."（基于你的活动，限额不能提升）
- "Limits cannot be increased at this time for your business …"（暂不能提升）

`Based on your activity` 是全套语料里距离"因为你的发送行为而受限"最近的一句官方原话，而它的后果是**不给你升档**，不是**降低你已有的档位**。这两件事的区别正是第 7.6 节记录的换代内容。

**`account_review_update`** 的三种触发（approved／rejected／deferred or awaiting more information）覆盖 **4** 个决策值，是"审核"而不是"处置复核"——它审的是账号本身能不能用这个平台，与第 10 章的违规申诉不是同一件事。它与账号级审核有关，但与第 10 章的四套申诉时限不同源，因此本篇**不把它并入**附表 F 冲突族第 3 条（四个申诉时限），只在附表 D 的 `reviewstate` 组里逐值登记。

**`security`** 的 **3** 条触发句都是"某人关掉了你的两步验证"或"某人改动 PIN"，其中两条明确写出是通过 WhatsApp Manager 按钮或重置邮件完成的。它与处置阶梯无关，但它是本篇登记的一组**账号被他人操作时可观察信号**，与系列第二篇（登录态与多设备绑定）相邻。

## 8.4 `disconnection_reason` 6 项：设备态与处置态混在一张表里

`disconnection_reason` 的 **6** 个值是整套枚举里语义跨度最大的一组：

| 值 | 官方原因 | 属于 |
|---|---|---|
| `ACCOUNT_DISCONNECTED` | "Your client's account was disconnected due to enforcement" | **处置** |
| `BUSINESS_DOWNGRADE` | 客户用消费者版 WhatsApp 重新注册了该 Business 号码 | 身份变更 |
| `CHANGE_NUMBER` | 客户改了号码 | 身份变更 |
| `USER_RE_REGISTERED` | 客户在新设备上重新注册 | 身份变更 |
| `COMPANION_INACTIVITY` | 伴生设备约 **30 天**未活跃 | 不活跃 |
| `PRIMARY_INACTIVITY` | 主设备约 **14 天**未活跃 | 不活跃 |

只有第一项是处置，其余 **5** 项都不是。而两个不活跃值用了"approximately"（约 **30 天**／约 **14 天**）——与系列第二篇里"14 天需重新登录"那条是同族数字，但这一页的主语是"client's device"。本篇把这 **6** 行登记在附表 D，并在正文里强调：**收到这个 webhook 不等于被处置**，它是**六分之一概率**的处置信号。


# 9 通知面：三个渠道、两个入口、五个字段

## 9.1 体系 A 的通知结构

政策执行页的"Notifications about violations"小节给出 **3** 个渠道，逐字如下：

> "- Surfaced in the Business Manager Notifications Center and as a banner in WhatsApp Manager.
> - Sent as an email to all admins set in Business Manager.
>   - To ensure proper delivery of notifications, please ensure admin information displayed in Business Manager is accurate and up-to-date.
> - Sent as a webhook notification to those subscribed."

三个渠道的性质不同：**第一个是界面**（通知中心＋横幅两处，横幅只在 WhatsApp Manager 出现）、**第二个是推送**（发给"all admins set in Business Manager"，收件人范围由 admin 名单决定，官方在这里补了一句责任提示：admin 信息要准确）、**第三个是订阅制**（只有订阅了才收）。这一段的第 **3** 条与第 8 章相连：`account_update` 是官方在"Preventing future violations"小节里**两次**点名要订阅的 webhook（一次在页中 Webhooks 小节，一次在页尾防范清单）。

## 9.2 两个产品入口

官方给"查看违规详情"的入口只有一个页面（Business Support Home），但给 **2** 条进入路径：Meta Business Suite 与 Business Manager，各自的菜单步骤不同：

| 入口 | 步骤 |
|---|---|
| Meta Business Suite | 登录 → 左侧菜单 **All tools** → **Business Support Home** → 必要时点左侧 **Account Overview**（速度表图标） |
| Business Manager | 登录 → 顶部 **All tools** 图标（三条横线）→ **Business Support Home** → 必要时点 **Account Overview** |

两个路径的最后一步相同，中间一步的图标位置不同（左侧菜单 vs 顶部图标）。这一句在本篇的作用是：**它是全篇唯一一处官方明确写出"违规详情在哪个界面可查"的话**，其余界面线索都来自 Direct Send 页与模板暂停页的横幅描述。

## 9.3 五个字段：官方承诺违规更新里必须有什么

第 3.9 节的 **5** 项内容清单（政策摘要与链接、允许／禁止示例、当前限制与再次违反的后果、如何避免、如何申诉）在本篇里的作用是**反面测量**：把这张清单与 `account_update` 的实际 payload 字段并排，可以看到机器可读层能给出的比这张清单少。

| 违规更新应含 | API／webhook 层有没有对应 |
|---|---|
| 违反的政策摘要与链接 | 只有 `violation_type` 一个枚举串，没有政策链接字段 |
| 允许／禁止内容示例 | **无**（官方页面写"Click the arrow in the left column for examples"，示例只在界面上） |
| 当前是否有主动限制 | `restriction_info`（且 Direct Send 说警告态**不带**这个对象） |
| 限制时长 | `restriction_info.expiration` 时间戳 |
| 如何避免未来违反 | **无** |
| 如何申诉 | **无**（申诉入口只在界面） |

也就是说：**通知面的"完整性"是界面层的承诺，不是 API 层的承诺**。这一句本篇不作为批评，只作为口径登记——接入方在 webhook 里拿不到"怎么改"和"怎么申诉"，这两项信息官方只写在 Business Support Home 里。

## 9.4 通知渠道在三套体系里的分布

| 渠道 | 体系 A | 体系 B（Direct Send） | 模板平面 | 消费者端 |
|---|---|---|---|---|
| 界面横幅／通知中心 | ✅（两处） | ✅（WhatsApp Manager 横幅） | ✅（Manager 横幅＋Business Suite 通知） | ✅（应用内系统消息） |
| 邮件 | ✅（all admins） | ✅（Full control 的人） | ✅ | ❌（官方未写） |
| webhook | ✅（`account_update`） | ✅（`account_update`＋`template_correct_category_detection`） | ✅（`message_template_status_update`＋`message_template_quality_update`） | ❌ |

体系 B 的邮件收件人写法与体系 A 不同：A 写"all admins set in Business Manager"，B 写"all Messaging account admins (people with **Full control** of the account)"。后者的范围更窄且用了权限名，前者用的是角色名。**同一件事两份文本用了两套收件人措辞**，本篇在附表 F 里登记。

# 10 申诉与复核：四个时限、一次机会、两个不解除

## 10.1 体系 A 的申诉五步

政策执行页给出 **5** 步：

> "1. From the Business Support Home page, select the relevant Messaging account.
> 2. Choose from the list of violations and click Request Review.
> 3. A new dialog opens in Business Manager. Enter supporting details and click Submit.
> 4. After submission, the request and the issue are moved to the In Review tab.
> 5. The appeal review decision will be sent via Business Manager and typically takes 24 to 48 hours. The appealed violation will either remain Unchanged or be set as Reversed."

两个决定值（`Unchanged`／`Reversed`）与时限（**24 to 48 hours**，带 `typically`）都在这一步里。同页另有一句限制："Not all spam violations might be appealed. Businesses might have to wait until the restriction period ends to begin messaging again."——**哪些可申诉，官方没给清单**。

## 10.2 体系 B 的复核：60 天窗口、一次机会

Direct Send 页把复核规则写成五条（第 5.7 节），其中两条是硬时限：**60 days** 内提出、每个模板**只能复核一次**。第 **4** 态 `In review` 里那句"Most reviews take a few days"是这一层唯一的时长线索，而它是模糊量。

## 10.3 模板平面的申诉：24 小时与"必须附样本"

模板暂停页给出 **6** 步 Manager 流程与两条硬规则：

> "appeals must include a sample"
> "The appeal will be reviewed and a decision made within 24 hours."

注意这一层的时限**不带 typically**，是"within 24 hours"的硬承诺；而体系 A 的是带 typically 的 **24 to 48 hours**。两者相差一倍的上界，且官方没有解释为什么模板层更快。

## 10.4 消费者端：24 小时与"没有入口就是终审"

`About account bans` 给出应用内文案：

> "This phone number can't use WhatsApp: [phone number]. You can request a review of this number. Most reviews are completed within 24 hours."

以及同一页最关键的一句：

> "If you don't see an option to request a review, it means the ban is final and can't be appealed."

同一页另有两句限制复核效果的表述：**"We only review one phone number per appeal"**（一次一个号码）与 **"contacting WhatsApp outside the review process or through another user's account, won't expedite the review process or impact the decision"**（局外渠道不加速也不影响结果）。Business 应用专页给出同一套文案的 Business 版本，并额外写"a banned account automatically loses its Meta Verified subscription"。

## 10.5 四个时限并排

| 流程 | 官方时限 | 出处 | 带不带限定词 |
|---|---|---|---|
| 体系 A 申诉决策 | **24 to 48 hours** | policy-enforcement | `typically` |
| 模板暂停申诉 | **24 hours** | template-pausing | `within`，无限定词 |
| Direct Send 模板复核 | **a few days** | Direct Send 页 | `Most reviews take` |
| 消费者端复核 | **24 hours** | `About account bans` | `Most reviews are completed within` |

**四套流程、四个不同的时限表述，官方从未给出任何对照句**。这是本篇附表 F 里最集中的一处冲突族：把任何一个数当成"WhatsApp 的申诉时长"都是跨体系引用。

## 10.6 申诉不解决什么

官方文本里有三句明确划出申诉边界：

1. "A review restores a template's classification, but doesn't lift account-level restrictions."（Direct Send）
2. "Not all spam violations might be appealed. Businesses might have to wait until the restriction period ends."（体系 A）
3. "We only review one phone number per appeal."（消费者端）

三句合起来：**复核的产出是"判定是否撤销"，不是"缩短已经在跑的限制期"**。第 3.8 节的状态回滚句讲的是同一件事的另一面——撤销之后账号"returns to its previous status"，即回到被处置前那个状态，而不是回到一个更快的未来。

# 11 消费者端：非官方应用、临时封禁、受限账号与检测

本章是体系 C。它与前两章的关系是：**共用同一份服务条款，但没有任何一条处置句可以互转**。

## 11.1 五项消费者端处置

帮助中心与封禁有关的 **4** 页给出五种状态，本篇登记为附表 C 的消费者端 **5** 行：

| 状态 | 官方触发描述 | 官方给出的动作 |
|---|---|---|
| 临时封禁 `Temporarily banned` | 使用非官方版本，或抓取信息（scraping） | 应用内提示；改正后可继续 |
| 永久封禁 | "If you don't, your account might be permanently banned from using WhatsApp." | 无入口即终审 |
| 受限账号 `Restricted accounts` | "recent activity on your account might be showing signs of engaging in spam, automated, or bulk messaging" | 自动到期，无需操作 |
| 非官方应用处置 | 使用未经授权的客户端或不支持的设备 | 卸载并用官方应用重新注册才能继续使用 |
| 解封后 `unbanned` | "If a review of account activity shows a ban was applied in error, it is removed" | 需重新用号码验证登录 |

## 11.2 受限账号：四项禁止与一项保留

`About restricted accounts` 是消费者端里唯一把"限制了什么"写清楚的一页，禁与止各一组：

> "However, you can't start chats or calls with new contacts, create new groups, add new members to groups, or use linked devices. You may also be logged out of some companion devices."

四项禁止：**向新联系人发起会话或通话、创建新群、向群里加新成员、使用已链接设备**。一句保留在前：

> "You can continue to communicate with contacts and groups you've exchanged chats or calls with previously."

也就是说受限状态切的是**向外扩展的能力**，保留的是**既有关系**。这一句与第 8.3 节 `RESTRICTED_BIZ_INITIATED_MESSAGING`／`RESTRICTED_CUSTOMER_INITIATED_MESSAGING` 的拆分在语义上相似，但**官方在两处没有互相引用**，本篇不合并。

同一页还有三点：受限时长官方只写"the allocated time"、查看剩余时间的方法是"tap New Chat, then select a contact that you've never chatted with before. The pop-up shows the time remaining"、以及一句**主动澄清**："If your business's verified badge is removed … This isn't a restriction on your account, and it doesn't limit how you use WhatsApp."——徽章移除**不是**受限，这是官方少数几处"排除某个猜测"的写法之一。

## 11.3 抓取（scraping）是消费者端唯一被量化描述过的行为吗？

`About temporarily banned accounts` 给出 scraping 的定义句：

> "Scraping is the automated extraction of information, both targeted and at scale, for any unpermitted purposes. Scrapers might attempt to search for and save users' information. This can include phone numbers, user profile pictures, and statuses from the WhatsApp platform. Acquiring user information through scraping violates our Terms of Service."

`both targeted and at scale` 是这套语料里少见的对"规模"的直接措辞。`How to use WhatsApp responsibly` 里的对应句是同族的：

> "Harvesting personal information: Avoid any automated extraction of information from WhatsApp for any unpermitted purposes. The acquisition of information from users in this way, including phone numbers, user profile pictures, and statuses from WhatsApp violates our Terms of Service."

两页都只说"violates our Terms of Service"，**都没有给出任何数量阈值**。这是本篇在未写明清单里登记的一项：消费者端对"多少算 harvesting"没有任何量化。

## 11.4 负责任使用页的"四要六不要"

`How to use WhatsApp responsibly` 是全篇把"群发相关行为"写得最集中的一页，结构是 **4** 条 best practices 与 **6** 条 practices to avoid。页首一句先声明后果：

> "Using WhatsApp in any of the ways listed below may cause your account to be banned."

六条禁令中与本篇直接相关的有四条：

- **Unwanted messages**："If a contact asks you to stop messaging them, you should remove the contact from your address book and refrain from contacting them again."
- **Automated or bulk messages**："Don't bulk message, auto-message, or auto-dial using WhatsApp. WhatsApp uses both machine learning technology and reports from users to detect and ban accounts that send unwanted automated messages. Also, don't create accounts or groups in unauthorized or automated ways, or use modified versions of WhatsApp."
- **Using contact lists that aren't yours**："Don't share phone numbers without consent or use data obtained from illicit sources to message users on WhatsApp or add them to groups."
- **Over-using broadcast lists**："Messages sent using a broadcast list will only be received when users have added your phone number to their contact list. Please keep in mind, frequent use of broadcast messages may lead people to report your messages, and we ban accounts that are reported multiple times."

四条里值得单独指出的是第三条：它是全套语料里唯一把**来源合法性**（"contact lists that aren't yours"、"data obtained from illicit sources"）写成禁令的话；第四条则是唯一把**广播列表**写进封禁成因的话，而它的机制是"reported multiple times"——**被多人举报**，不是发了多少。两条 best practices 也与群发相关：**给群组加好友须先征得同意**、以及"if you add someone to a group and they remove themselves, honor their decision"。

这一页同时给出检测手段的一句：**"WhatsApp uses both machine learning technology and reports from users to detect and ban accounts that send unwanted automated messages"**——机器学习与用户举报**两条通道并列**。

## 11.5 非官方应用：四个被点名的名字与三种后果

`About unofficial WhatsApp apps` 是全篇唯一给出具体例名的页：

> "Examples of unofficial apps may include GB WhatsApp, FM WhatsApp, TeleMessage, TM WhatsApp, and others."

**4** 个例名，且句末是"and others"——官方自己声明这不是闭合清单。同一页给出的后果是 **3** 条：

1. "Your privacy and security are at risk. Unofficial apps might carry malware that can steal your data, and damage your phone."
2. "There's no guarantee your messages or your data, like your location or the files you share, will be private and secure."
3. "Your WhatsApp account might also be temporarily or permanently banned, or it could lead to restrictions on your account, including the ability to link devices."

第 **3** 条把三种消费者端状态（临时封禁／永久封禁／受限）**在同一句里并列**，还加上第四个具体后果：链接设备的能力。另有 **2** 条系统消息文案："You need the official WhatsApp to log in" 与 "You have been logged out for using an unofficial app."

同一页还有一句与系列第二篇直接衔接的区分：**通过 WhatsApp 官方 Agents 功能连接的第三方代理，与"使用非官方应用"不是一回事**——"Connecting a third-party agent through WhatsApp's supported agent feature is different from using an unofficial WhatsApp app or website."

`About account bans for unofficial apps` 给出解封条件与一句警告式回滚：

> "We can reinstate an account ban if the account activity continues to violate our Terms of Service."

`reinstate` 说明**解封不是终局**：如果行为继续，同一个封禁可以重新施加。

## 11.6 检测层：官方声明可用来判定的信息

`About detecting violations` 是整套语料里对"平台看得见什么"写得最直白的一页，两个触发小节：

> "Because WhatsApp messages are end-to-end encrypted, we use information about you from WhatsApp users, information from your business account and registration and other information to identify when a business or Business Solution Provider may be in violation of those policies."

> "When a user blocks or reports a business or a message from a business, WhatsApp receives the last five messages sent to the user by the reported business, the reported group or user ID, information on when the message was sent, and the type of message sent (image, video, text, etc.)."

第二句给出**举报随附的 4 类信息**：**最后五条消息、被举报的群组或用户 ID、消息发送时间、消息类型**。这是全篇唯一一个"多少条消息"被官方写死的地方（**5** 条）。页首那句的因果结构值得原样保留：**因为端到端加密，所以用非内容信号判定**——这一句是加密层与处置层之间唯一的官方连接句。

第二个小节"当资料信息被提交或更新"另有一句："WhatsApp may also conduct reviews using user information to determine whether an account is in violation of the Business or Commerce policies."

Messaging Guidelines 侧对应的是审核机制的 **3** 个小节（Automated Processing／Human Review Teams／Local Law Enforcement）与一句范围声明：

> "WhatsApp may use both automated processing techniques and human review teams to detect and review information available to us including account, group, and community profile information, as well as reported messages, for potential violations of these Guidelines."

指南里对自动化角色的原话是"**automates decisions for certain areas** where account behavior or reported message content is highly likely to be in violation"，以及"our automated systems send it to a human review team to make the final decision"——**自动化做某些领域的决定，其余领域路由给人**，而哪些属于前者官方未写。指南还给出政府请求路径与一句"we always assess the legitimacy and completeness of a government request before taking any action"。

本篇在这里停住：以上都是官方对**自身能力**的声明，引用它的目的仅在于说明"哪些信号已被公开列为可用判定依据"。任何关于这些信号如何被计算、阈值在哪、如何不被判定的推断，本篇一律不写。

## 11.7 Business 应用端与 Business Platform 端的互指

帮助中心有 **2** 页专门处理身份边界，本篇各登记为附表 C 的 **1** 行：

`About account bans on the WhatsApp Business app` 在页尾把平台侧推走：

> "To learn about account bans on the WhatsApp Business Platform, including how to appeal an account ban, read the Meta Developer Documentation here."

`About ban appeals on the WhatsApp Business Platform` 则把消费者支持渠道关掉：

> "WhatsApp Support does not provide support related to account bans and restrictions on the WhatsApp Business Platform. Businesses using the WhatsApp Business Platform can consult the Meta Business Help Center for most of their support needs."

两句合起来是官方对三套体系关系**唯一的正面陈述**：**Business Platform 的封禁与申诉不走 WhatsApp 支持渠道**。这是本篇把三套体系分列的直接依据，而不是本篇的推断。

# 12 同步错误码：11 个码分别落在哪一级

错误码页是"阶梯"与"实际调用"之间的接口层。与本篇相关的 **11** 个码登记在附表 E 的最后一组，本章把它们映射回前十章的结构。

## 12.1 四个直接落在处置链上的码

| 码 | 官方标题句 | 落在哪一级 |
|---|---|---|
| `131064` | "Message failed to send because this account has reached its messaging limit due to template classification violations. This applies to both template messages and direct send messages." | 体系 B 第 **2** 级（Rate limiting） |
| `132015` | "Template is temporarily unavailable to use because it was paused due to low quality." | 模板平面（暂停） |
| `139200` | "(#139200) Direct Send Utility access is blocked" | 体系 B 第 **3–5** 级（限制期内） |
| `131031` | "The WhatsApp Business Account associated with the app has been restricted or disabled for violating a platform policy…" | 体系 A 第 **3–4** 级 |

四句里 **3** 句自带下一步指引，而这三个指引的性质完全不同：

- `131064` 的指引是"Review your template classifications … **This restriction is automatically lifted after the enforcement period.**"——**自动到期**，且这一句是全套语料中唯一明确写出"体系 B 的限速会自己解除"的地方。同一句还确认该码"applies to both template messages and direct send messages"，即它**跨两种发送方式**。
- `132015` 的指引是把原因写成状态（因为低质量被暂停），与第 6.2 节的三档实例对应；模板暂停页还说明这一类发送**不收费、不占限额**。
- `131031` 的指引最特别：它把两种成因并列——"restricted or disabled for violating a platform policy"，**或者**"we were unable to verify data included in the request against data set on the WhatsApp Business Account (e.g, the two-step pin included in the request is incorrect)"。也就是说**同一个码既可能是政策处置，也可能是两步验证 PIN 不匹配**。该页另建议用 Health Status API 取得"additional insight into the reason or reasons for the account lock"——**官方在这里明确承认单看这个码不能确定原因**。

## 12.2 四个不在阶梯上、但常被当成阶梯的码

| 码 | 官方含义 | 实际是哪一类 |
|---|---|---|
| `130429` | "Cloud API message throughput has been reached." | **吞吐规格**，不是处置。第 7.6 节记录：**1,000 messages per second** 是可获得的档位 |
| `131047` | "More than 24 hours have passed since the recipient last replied to the sender number." | **客服窗口过期**，指引是改发模板消息 |
| `131049` | "This message was not delivered to maintain healthy ecosystem engagement." | **收件人侧的营销上限**（第 6.4 节），按号码隔离 |
| `131056` | "Too many messages sent from the sender phone number to the same recipient phone number in a short period of time." | **同一收发对的频率护栏**，且明确"You can still send messages to a different phone number without waiting" |

`130429` 与 `131064` 的混淆是最常见的一类读错：前者的标题就是"Rate limit hit"（错误码页的示例句 `(#130429) Rate limit hit` 出现在两处），但它数的是 **messages per second**；后者也自称限制，数的是 **24 小时内去重收件人**。一个速率、一个总量，处置含义完全不同——`131048` 的官方描述更是把两者连起来了：

> "Message failed to send because there are restrictions on how many messages can be sent from this phone number. This may be because too many previous messages were blocked or flagged as spam."

这一句在错误码层写出了**历史被封／被标记为垃圾的消息数量**是发送受限的可能成因，用词是"This **may** be because"——一个官方主动标为可能的表述，而不是判据。

`131049` 与 `131056` 都带"不影响发给别人"这句隔离说明（前者写"This does not affect your ability to send marketing messages to other users"，后者写"You can still send messages to a different phone number without waiting"）。**这两句是全篇唯一明确写出"限制是按收件人隔离的"官方文本**。

## 12.3 两个外围码

| 码 | 官方含义 | 为什么在本篇 |
|---|---|---|
| `130497` | "The WhatsApp Business Account is restricted from messaging to users in certain countries." | **按国家的发送限制**，指引指向 Business Messaging Policy 的"allowed countries for messaging in your business category" |
| `133016` | "Registration or Deregistration failed because there were too many attempts for this phone number in a short period of time" | **注册／注销尝试次数上限**，指引指向 Registration 文档的 Limitations 小节 |

`130497` 值得单独指出：它是全套语料里唯一一个**账号级、按国家**的发送禁令，而它的判据是"your business category"——把第 4 章的品类码与"能发到哪些国家"连起来了。也就是说品类不只决定能不能卖，还决定**能发到哪里**，而官方没有给出品类到国家清单的映射。

`133016` 与第 3.3 节的"adding additional phone numbers to the account"同属**号码接入**层面：一个是在阶梯里封锁加号动作，一个是注册尝试次数超限。本篇把它们并排登记，但不写成因果关系——官方没有连接句。

# 13 数字归档、未写明清单与口径冲突

## 13.1 数字归档的方法

附表 B 把本篇正文与附表里出现的每一个数字按五档归档：**阶梯档位时长／滚动窗口与观察期／申诉与复核时限／生效日期与版本节点／数量与档位**。归档规则与前两篇一致：

- 同一个数字出现在两个体系里，**开两行**（本表重复最多的三个数：**24** 占 **6** 行——限额滚动窗口、客服窗口、`131049` 的重试隔离、最高吞吐承诺、模板暂停申诉、消费者端复核时限；**7** 占 **5** 行——体系 B 的 `7 days` 限制、模板平面 pacing 的限速窗口（同一句 pacing 在"时长档"与"滚动窗口"两档各登一次）、限额自动升档判据的近 7 天、废止前 `Flagged` 的连续 7 天观察期、utility 模板 pacing 适用期；**30** 占 **3** 行——体系 B 的 `30 days`、scaling path 的送达观察期、`COMPANION_INACTIVITY` 的近似值）。
- 官方写成区间或多值的（`1 or 3 day`、`5, 7, or 30 day`、`24 to 48 hours`）按**整段一行**登记，不拆成候选值。
- 枚举**条目数**（16 个事件、19 个品类码等）归入"数量与档位"档，与业务数字同表但标注计数对象。

## 13.2 官方未写明的十一项

| # | 未写明的事 | 本篇能确认的边界 |
|---|---|---|
| 1 | 体系 A 中"警告之后是否必然进入封锁" | 官方用 `might start seeing`，无必然性句 |
| 2 | 两组封锁区间里具体取值由什么决定 | 只写"gradually increase in duration" |
| 3 | 体系 A 与体系 B 之间有没有对应 | 无连接句（唯一逐字重合是"adding additional phone numbers"） |
| 4 | Direct Send 限速一级的**具体上限数值** | 只写"a capped volume" |
| 5 | `the enforcement window` 的长度 | 只有 `Until utility volume falls back under the cap or the enforcement window ends` |
| 6 | 模板误分类率降到"threshold 以下"的那个 threshold | 只写"falls back under the threshold" |
| 7 | pacing 的阈值 | 只写"an unspecified threshold" |
| 8 | `excessive negative feedback` 的量化阈值 | 无任何数字 |
| 9 | 质量评级三个输入信号的权重与更新频率 | 只有一句并列 |
| 10 | 无限期账号锁的申诉时限与次数上限 | 只写"can only be removed via an appeal" |
| 11 | 哪些 spam 类违规不可申诉 | 只写"Not all spam violations might be appealed" |

十一项里没有一项是"官方忘了写"——每一处都带着 `unspecified`、`might`、`a capped`、`threshold` 这类主动限定词。本篇把它们登记为**留白**而不是缺陷，并据此拒绝在本篇里给出任何阈值数字。

正文这十一项之外，附表 F 还补登 **5** 项外围留白：受限账号的具体时长（官方只写 `the allocated time`）、`harvesting`／`scraping` 的数量阈值、`automated processing` 具体自动化了哪些领域的判定、品类码与错误码 `130497` 国家清单之间缺少映射表、`131049` 重试惩罚里 `multiple times` 的次数。因此本篇的留白总数是 **11 ＋ 5 ＝ 16** 项，全部收在附表 F 的后 **16** 行（本表合计 **8 ＋ 16 ＝ 24** 行）；第 14 章第七条结论按正文的十一项口径陈述，不把外围五项算进"影响可预测性的核心留白"。

## 13.3 八组口径冲突

附表 F 的前八行是冲突族，逐组性质：

1. **`offboard` 一词两个意思**：政策执行页（严重伤害立即下线）vs `account_update` 参考页（换设备或重注册导致脱机）。**词义冲突**。
2. **`ban` 一词跨层**：帮助中心（临时／永久封禁）vs Direct Send 的 `…_UNBAN`（限速或限制解除）vs `unbanned`（复核通过）。**层级不同**。
3. **四个申诉时限**：**24 to 48 hours／24 hours／a few days／24 hours**，分属四套流程。**流程不同，不可互推**。
4. **`restriction` 的三个主语**：Business Account／WABA 与 Messaging account／phone number。**主语不同**。
5. **限额取值三套域**：`bcu` 旧字段 **5** 个数字含 `-1`、新字段 **5** 个 `TIER_*`、`phone_number_quality_update` **7** 个 `TIER_*` 含 `TIER_50` 与 `TIER_NOT_SET`。**同一档位三套表示**。
6. **收件人措辞两套**：体系 A "all admins set in Business Manager" vs 体系 B "people with **Full control**"。**权限词不同**。
7. **质量与限额的关系在两个版本里相反**：2025 年 10 月 7 日前 `Flagged` 连续 **7 天**立即降一档 vs 此后 `Flagged` 不存在、质量下降不降限额。**版本不同**。
8. **模板原因码同文不同码**：`ABUSIVE_CONTENT`／`PROMOTIONAL`／`SCAM` 三个码描述逐字相同；`INCORRECT_CATEGORY`／`TAG_CONTENT_MISMATCH` 亦然。**词表不区分**。

## 13.4 一处"两个域名同一页"

Commerce Policy 在本篇语料里以两个域名各存一份（`whatsapp.com/legal/commerce-policy` 与 `whatsappbusiness.com/policy/`），本篇对两者做了字节比对，SHA-1 前缀一致。附表 A 保留两个登记位并注明同一页，正文只当作一个来源。这是**登记层的重复**，不是口径冲突，本篇刻意不把它放进附表 F。

# 14 结论

**一、"群发／群控"在官方文本里不是一个违规类型，而是分散在四个层。**

条款层（`bulk messaging`、`auto-messaging`、`auto-dialing`、`create accounts … through unauthorized or automated means`）负责**禁止**；消息指南层（adversarial behavior 一句把 unofficial clients、bulk/auto-messaging/auto-dialing、automation 并列）负责**归类**；开发者文档层负责**处置**，而它的 **19** 个 `violation_type` 码全是交易品类、Direct Send 的 **8** 个码全是分类误用——**没有一个码的判据是"发了多少"**；质量层（模板 `RED`、pacing、per-user 上限）负责**用收件人反馈反向调节**。把"群发被封"当成一条规则来叙述，等于把这四层压成一层。

**二、官方写的不是"一条阶梯"，而是三条互不相通的阶梯加一个平面。**

体系 A：**警告 → 1 或 3 天 → 5、7 或 30 天 → indefinite → 永久停用**，共 **5** 档，另 **2** 条阶梯外条款（严重伤害立即下线、过度负面反馈）、**1** 条状态回滚、**1** 条不可申诉声明（附表 C 共 **9** 行）。体系 B：**Warning(Ongoing) → Rate limiting → 7 days → 30 days → Permanent**，共 **5** 级，另 **1** 条模板通知与 **4** 条复核规则（共 **10** 行）。体系 C：临时封禁、永久封禁、受限账号、非官方应用、解封后 **5** 种状态。平面 D：**3 hours → 6 hours → Disabled** 三档实例，加 pacing 的 **7 days** 与暂停规则的 **2023 年 9 月 12 日／10 月 12 日**两波生效（共 **8** 行）。**三套体系之间官方没有给过任何一条对齐句**，唯一的逐字重合是"adding additional phone numbers to the account"。

**三、"能发多少"与"因什么被罚"是两根轴，而且前者不含任何处罚语义。**

限额 **250 → 2,000 → 10,000 → 100,000 → Unlimited** 由**资格与用量**决定（**3** 条 scaling path、**2** 条自动升档判据、满足后 **6 hours** 内升一档），不由任何处置决定；反过来，阶梯里的限速一级官方**没给数字**。把"限额到了 100,000"读成"账号很危险"或"账号很安全"都没有文本依据。而 2025 年 10 月 7 日的换代把质量与限额之间原有的一条连接（`Flagged` 连续 **7 天**立即降一档）**删掉了**——现行文本里两者已经脱钩。

**四、通知与观测面比处置面写得细，但只到界面层为止。**

违规更新的 **5** 项内容清单、**3** 个通知渠道、**2** 个产品入口、`account_update` 的 **16** 个事件、**9** 个 `restriction_type`、**8** 个 Direct Send `violation_type`、`account_alerts` 的 **6＋3＋8＋3＋2** 五组枚举、`message_template_status_update` 的 **14** 个事件与 **9** 行原因（含 null）、**11** 个同步错误码——机器可读层的覆盖是完整的。但"允许／禁止的示例"、"如何避免"、"如何申诉"这三项**只写在界面里**，webhook 层拿不到。

**五、申诉层最不一致：四个时限、一次机会、三句"不解决什么"。**

**24 to 48 hours**（体系 A）、**24 hours** 且必须附样本（模板平面）、**a few days** 且须在 **60 days** 内提出、每模板仅一次（体系 B）、**24 hours** 且"没有入口就是终审"、一次只审一个号码（体系 C）。三句边界句共同说明：**复核撤销判定，不缩短限制期，不解除账号级限制。**

**六、消费者端把"被举报"写成了主要机制，把"发了多少"写成了零。**

`we ban accounts that are reported multiple times`、`WhatsApp uses both machine learning technology and reports from users`、以及检测页的**最后五条消息＋ID＋时间＋类型**四类随附信息——这套文本里的触发器是**用户反馈与元数据**，不是发送计数。全篇唯一被写死的"条数"是举报时随附的 **5** 条，与发送量无关。

**七、官方主动留白十一处（第 13.2 节），这是本篇最重要的可预测性结论。**

限速上限、enforcement window 长度、误分类率阈值、pacing 阈值、负面反馈阈值、质量输入权重、无限期锁的申诉时限、不可申诉清单——**全都没有数字**。任何声称"WhatsApp 每小时发 X 条就会封"或"被举报 Y 次才处置"的说法，在本次读取的官方文本里都没有出处。

**八、与本篇相邻的三条边界。**

本篇只解读规则文本：不比较各体系的宽严、不推测未写明的阈值、不提供任何规避检测或加速发送的做法。凡官方写成区间、模糊量或主动标注 `unspecified` 之处，本篇原样保留并登记为留白。

*（本篇为"WhatsApp 群发／群控五篇"的第一篇，主题为违规判定与处置阶梯。第二篇讲商业平台的消息类型、模板、质量评级与限额档位；第三篇讲自动化接入面——API、Webhook、system user 与官方对 bot 的定性；第四篇讲群组、社区、频道与列表的治理开关；第五篇讲号码、账号形态、受限账号与封禁申诉。四篇之间引用同一批页面但取数轴不重叠。）*

# 附表 A 官方页面登记清单与本篇引用位置（46 行）

登记口径与系列前两篇一致：**凡本篇取到正文并引用其内容的页面，一律登记**；本次抓取但属于其余四篇（模板与限额／自动化接入／群组治理／账号形态）的页面不在此表内。全表 **45** 个登记位：**引用 40 页、正文未取回 5 页**；站点分布为 Meta 开发者文档 23、WhatsApp 帮助中心 12、WhatsApp 法律页 4、WhatsApp Business 政策页 1，未取回的 5 页分布在 Meta 开发者文档 3、WhatsApp 帮助中心 1、WhatsApp 法律页 1。帮助中心页面**一律不显示日期字段**（本次逐页核验），因此本表不设日期列。

| 站点 | 页面标识 | 页面主题 | 本篇引用位置 | 取回状态 |
|---|---|---|---|---|

| Meta 开发者文档 | `business_management_business_verification_md` | （正文未取回，无标题） | 正文未取回，本篇不引用（1.1、附表 A） | 正文未取回 |
| Meta 开发者文档 | `direct_send_api_reference_md` | Direct Send API reference | 正文 11.5（Direct Send 错误码页脚链接）、附表 A | 正文已取回 |
| Meta 开发者文档 | `direct_send_integrity_and_content_guidelines_md` | Integrity and content guidelines | 正文 2.3、5.1—5.8、9.4、10.2、12.1、附表 C／附表 D／附表 F | 正文已取回 |
| Meta 开发者文档 | `direct_send_supported_features_and_limits_md` | Supported features and limits | 正文 1.1（Direct Send 范围）、附表 A | 正文已取回 |
| Meta 开发者文档 | `getting_opt_in_md` | Get opt-in for WhatsApp | 正文 1.2（opt-in 前置）、附表 E（政策大节） | 正文已取回 |
| Meta 开发者文档 | `messages_message_types_md` | （正文未取回，无标题） | 正文未取回，本篇不引用（1.1、附表 A） | 正文未取回 |
| Meta 开发者文档 | `messaging_limits_md` | Messaging Limits | 正文 7.1—7.5、12.3、附表 C／附表 D | 正文已取回 |
| Meta 开发者文档 | `policy_enforcement_md` | WhatsApp Business Platform policy and spam enforcement | 正文 2.2、3.1—3.9、9.1—9.3、10.1、13.2、附表 C／附表 F | 正文已取回 |
| Meta 开发者文档 | `policy_enforcement_violations_md` | WhatsApp Business Platform Policy Violations | 正文 4.1—4.3、13.4、附表 E（19 码全量） | 正文已取回 |
| Meta 开发者文档 | `support_error_codes_md` | WhatsApp error codes | 正文 12.1—12.3、附表 E（同步错误码 11 项） | 正文已取回 |
| Meta 开发者文档 | `templates_marketing_templates_per_user_limits_md` | Per-user marketing template message limits | 正文 6.4、12.2、附表 E（131049） | 正文已取回 |
| Meta 开发者文档 | `templates_template_pacing_md` | Template pacing | 正文 2.5、6.3、13.2、附表 C | 正文已取回 |
| Meta 开发者文档 | `templates_template_pausing_md` | Template pausing | 正文 2.5、6.2、10.3、12.1、附表 C／附表 D | 正文已取回 |
| Meta 开发者文档 | `templates_template_quality_md` | Template quality rating | 正文 6.1、附表 D（quality／qualitylabel 两组） | 正文已取回 |
| Meta 开发者文档 | `upcoming_messaging_limits_changes_md` | Upcoming changes to messaging limits | 正文 7.6、13.3、附表 B（版本节点） | 正文已取回 |
| Meta 开发者文档 | `webhooks_overview_md` | WhatsApp webhooks | 正文 8.3、附表 A | 正文已取回 |
| Meta 开发者文档 | `webhooks_reference_account_alerts_md` | account_alerts webhook reference | 正文 7.4、8.3、14 第四项、附表 D（alert_* 四组） | 正文已取回 |
| Meta 开发者文档 | `webhooks_reference_account_review_update_md` | account_review_update webhook reference | 正文 8.3、附表 D（reviewstate） | 正文已取回 |
| Meta 开发者文档 | `webhooks_reference_account_update_md` | account_update webhook reference | 正文 8.1—8.2、附表 D（event／restriction_type 两组） | 正文已取回 |
| Meta 开发者文档 | `webhooks_reference_business_capability_update_md` | business_capability_update webhook reference | 正文 7.5、8.3、附表 D（bcu_* 三组） | 正文已取回 |
| Meta 开发者文档 | `webhooks_reference_message_template_quality_update_md` | message_template_quality_update webhook reference | 正文 6.3、8.3、附表 A | 正文已取回 |
| Meta 开发者文档 | `webhooks_reference_message_template_status_update_md` | message_template_status_update webhook reference | 正文 6.5、8.3、附表 D（tpl_* 四组） | 正文已取回 |
| Meta 开发者文档 | `webhooks_reference_phone_number_quality_update_md` | phone_number_quality_update webhook reference | 正文 7.5、8.3、附表 D（tier 7 项） | 正文已取回 |
| Meta 开发者文档 | `webhooks_reference_security_md` | Security webhook reference | 正文 8.3、附表 A | 正文已取回 |
| Meta 开发者文档 | `webhooks_reference_template_category_update_md` | template_category_update webhook reference | 正文 8.3、附表 A | 正文已取回 |
| Meta 开发者文档 | `webhooks_reference_template_correct_category_detection_md` | （正文未取回，无标题） | 正文未取回；Direct Send 页 5.7 已引用该 webhook 名（附出处为 Direct Send 页） | 正文未取回 |
| WhatsApp 帮助中心 | `1064395290901991` | About account bans for unofficial apps | 正文 4.1（品类违规）、附表 A | 正文已取回 |
| WhatsApp 帮助中心 | `1217634902127718` | About unofficial WhatsApp apps | 正文 11.5、附表 C（4 个例名与 3 条后果） | 正文已取回 |
| WhatsApp 帮助中心 | `1297640017664693` | After your account is unbanned | 正文 11.1、11.5、附表 C | 正文已取回 |
| WhatsApp 帮助中心 | `1508178557633269` | About ban appeals on the WhatsApp Business Platform | 正文 11.7、附表 C | 正文已取回 |
| WhatsApp 帮助中心 | `1805617343145907` | About detecting violations | 正文 11.6、14 第六项、附表 E | 正文已取回 |
| WhatsApp 帮助中心 | `1848531392146538` | About temporarily banned accounts | 正文 11.1、11.3、附表 C | 正文已取回 |
| WhatsApp 帮助中心 | `3166176253599957` | How to contact the Grievance Officer and WhatsApp in India | 正文未取回，本篇不引用（1.1、附表 A） | 正文未取回 |
| WhatsApp 帮助中心 | `361005896189245` | How to use WhatsApp responsibly | 正文 11.3、11.4、14 第六项 | 正文已取回 |
| WhatsApp 帮助中心 | `465883178708358` | About account bans | 正文 10.4、11.1、附表 C／附表 B | 正文已取回 |
| WhatsApp 帮助中心 | `684051319521343` | How to register your phone number | 正文 11.5（重新注册与受限的关系）、附表 A | 正文已取回 |
| WhatsApp 帮助中心 | `717472490411581` | About restricted accounts | 正文 2.4、11.1、11.2、附表 C | 正文已取回 |
| WhatsApp 帮助中心 | `723378546580115` | About account bans on the WhatsApp Business app | 正文 10.4、11.7、附表 C | 正文已取回 |
| WhatsApp 帮助中心 | `823283246153998` | About the official WhatsApp Business account | 正文 11.7（官方账号识别）、附表 A | 正文已取回 |
| WhatsApp Business 政策页 | `https___whatsappbusiness_com_policy_` | Business Policy | 正文 1.1、12.3、13.4、附表 E（升级路径 6 项） | 正文已取回 |
| WhatsApp 法律页 | `https___www_whatsapp_com_legal_WhatsApp_Terms_for_WhatsApp_Business_Platform` | WhatsApp Terms for WhatsApp Business Platform | 正文 1.1、4.2、附表 A | 正文已取回 |
| WhatsApp 法律页 | `https___www_whatsapp_com_legal_business_terms` | WhatsApp Business Terms of Service | 正文未取回，本篇不引用（1.1、附表 A） | 正文未取回 |
| WhatsApp 法律页 | `https___www_whatsapp_com_legal_commerce_policy` | Business Policy | 正文 1.1、4.3、13.4、附表 E（受监管品类 15 项） | 正文已取回 |
| WhatsApp 法律页 | `https___www_whatsapp_com_legal_messaging_guidelines` | WhatsApp Messaging Guidelines | 正文 4.3、8.3、11.6、附表 E／附表 F | 正文已取回 |
| WhatsApp 法律页 | `https___www_whatsapp_com_legal_terms_of_service` | Terms of Service | 正文 1.2、4.2、4.3、11.3、附表 E | 正文已取回 |
| **合计** | **45** 个登记位 | 站点合计 Meta 开发者文档 26、WhatsApp 帮助中心 13、WhatsApp 法律页 5、WhatsApp Business 政策页 1（同一页面在两个域名下各占一位） | 本篇只用规则层，不取任何操作层内容 | 引用 40／未取回 5 |

#ROWS=45 CITED=40 UNCITED=5

# 附表 B 数字归档五档（66 行）

归档规则见第 13.1 节：**同一数字出现在两个体系里开两行**，官方写成区间或多值的按整段一行登记，日期行只进第四档，枚举条目数与业务数字同表但标注计数对象。五档合计 **65** 行（一 **23**／二 **10**／三 **8**／四 **8**／五 **16**），去重后的数字与时长词条目 **39** 个，计数单位 **28** 种。

| 档 | 数字 | 计数单位 | 数的是什么 | 性质 | 出处页 |
|---|---|---|---|---|---|

| 一、阶梯档位时长 | **1 or 3** | 天 | 商业平台·1 短时封锁 | 硬时长（区间或多值） | policy_enforcement_md |
| 一、阶梯档位时长 | **5, 7, or 30** | 天 | 商业平台·2 长时封锁 | 硬时长（区间或多值） | policy_enforcement_md |
| 一、阶梯档位时长 | **indefinite** | 定性时长词 | 商业平台·3 账号锁 | 条件触发项 | policy_enforcement_md |
| 一、阶梯档位时长 | **永久** | 定性时长词 | 商业平台·4 永久停用 | 条件触发项 | policy_enforcement_md |
| 一、阶梯档位时长 | **立即** | 定性时长词 | 商业平台·旁路 严重伤害 | 条件触发项 | policy_enforcement_md |
| 一、阶梯档位时长 | **24 to 48** | 小时 | 商业平台·回滚 | 硬时长（区间或多值） | policy_enforcement_md |
| 一、阶梯档位时长 | **限制期** | 定性时长词 | 商业平台·不可申诉 | 条件触发项 | policy_enforcement_md |
| 一、阶梯档位时长 | **Ongoing** | 定性时长词 | Direct Send·1 Warning | 条件触发项 | direct_send_integrity_and_content_guidelines_md |
| 一、阶梯档位时长 | **enforcement window** | 定性时长词 | Direct Send·2 Rate limiting | 条件触发项 | direct_send_integrity_and_content_guidelines_md |
| 一、阶梯档位时长 | **7** | 天 | Direct Send·3 Messaging restriction | 硬时长（定值） | direct_send_integrity_and_content_guidelines_md |
| 一、阶梯档位时长 | **30** | 天 | Direct Send·4 Extended restriction | 硬时长（定值） | direct_send_integrity_and_content_guidelines_md |
| 一、阶梯档位时长 | **Permanent** | 定性时长词 | Direct Send·5 Permanent revocation | 条件触发项 | direct_send_integrity_and_content_guidelines_md |
| 一、阶梯档位时长 | **60** | 天 | Direct Send·复核窗口 | 硬时长（定值） | direct_send_integrity_and_content_guidelines_md |
| 一、阶梯档位时长 | **1** | 次数 | Direct Send·复核次数 | 条件触发项 | direct_send_integrity_and_content_guidelines_md |
| 一、阶梯档位时长 | **a few** | 天（模糊量） | Direct Send·复核期间 | 硬时长（模糊量） | direct_send_integrity_and_content_guidelines_md |
| 一、阶梯档位时长 | **3** | 小时 | 模板质量·暂停 1 | 硬时长（定值） | templates_template_pausing_md |
| 一、阶梯档位时长 | **6** | 小时 | 模板质量·暂停 2 | 硬时长（定值） | templates_template_pausing_md |
| 一、阶梯档位时长 | **Disabled** | 定性时长词 | 模板质量·暂停 3 | 条件触发项 | templates_template_pausing_md |
| 一、阶梯档位时长 | **自动** | 定性时长词 | 模板质量·解暂停 | 条件触发项 | templates_template_pausing_md |
| 一、阶梯档位时长 | **7** | 天 | 模板质量·限速窗口 | 硬时长（定值） | templates_template_pacing_md |
| 一、阶梯档位时长 | **250** | 个数（无时间单位） | 限额档位·起点 | 条件触发项 | messaging_limits_md |
| 一、阶梯档位时长 | **不可申诉** | 定性时长词 | 消费者端·永久封禁 | 条件触发项 | 465883178708358 |
| 一、阶梯档位时长 | **自动到期** | 定性时长词 | 消费者端·受限账号 | 条件触发项 | 717472490411581 |
| 二、滚动窗口与观察期 | **24** | 小时（滚动窗口） | 限额的定义窗口：moving 24-hour period，且只数窗口外去重号码 | 硬时长（定值） | messaging_limits_md |
| 二、滚动窗口与观察期 | **24** | 小时（客服窗口） | 用户每次发消息即开合一次；窗口内消息不计入限额 | 条件触发项 | templates_marketing_templates_per_user_limits_md |
| 二、滚动窗口与观察期 | **30** | 天（滚动期） | scaling path 第 3 条的送达观察期 | 硬时长（定值） | messaging_limits_md |
| 二、滚动窗口与观察期 | **7** | 天（滚动期） | 自动升档判据二：近 7 天用掉当前限额一半以上 | 硬时长（定值） | messaging_limits_md |
| 二、滚动窗口与观察期 | **7** | 天（观察期） | 2025-10-07 前 Flagged 连续 7 天即降一档（现已废止） | 条件触发项 | upcoming_messaging_limits_changes_md |
| 二、滚动窗口与观察期 | **7** | 天（pacing 窗口） | utility 模板被 pacing 的适用期 | 硬时长（定值） | templates_template_pacing_md |
| 二、滚动窗口与观察期 | **24** | 小时（重试隔离） | 对已达上限用户重发的封隔时长 | 条件触发项 | templates_marketing_templates_per_user_limits_md |
| 二、滚动窗口与观察期 | **24** | 小时（吞吐承诺） | 最高吞吐营销消息在 1 小时内送达（99 百分位） | 默认可改项 | templates_template_pacing_md |
| 二、滚动窗口与观察期 | **30** | 天（不活跃） | COMPANION_INACTIVITY 的官方近似值 | 条件触发项 | webhooks_reference_account_review_update_md |
| 二、滚动窗口与观察期 | **14** | 天（不活跃） | PRIMARY_INACTIVITY 的官方近似值 | 条件触发项 | webhooks_reference_account_review_update_md |
| 三、申诉与复核时限 | **24 to 48** | 小时 | 体系 A 申诉决策（typically takes） | 硬时长（区间或多值） | policy_enforcement_md |
| 三、申诉与复核时限 | **24** | 小时 | 模板暂停申诉（within 24 hours，无限定词） | 硬时长（定值） | templates_template_pausing_md |
| 三、申诉与复核时限 | **a few** | 天（模糊量） | Direct Send 模板复核（Most reviews take a few days） | 硬时长（模糊量） | direct_send_integrity_and_content_guidelines_md |
| 三、申诉与复核时限 | **24** | 小时 | 消费者端复核（Most reviews are completed within 24 hours） | 硬时长（定值） | 465883178708358 |
| 三、申诉与复核时限 | **60** | 天 | Direct Send 复核资格窗口（自 template_correct_category_detection 通知起） | 硬时长（定值） | direct_send_integrity_and_content_guidelines_md |
| 三、申诉与复核时限 | **1** | 次 | 每个被标记模板的复核机会 | 硬数量上限 | direct_send_integrity_and_content_guidelines_md |
| 三、申诉与复核时限 | **1** | 个号码／次 | We only review one phone number per appeal | 硬数量上限 | 465883178708358 |
| 三、申诉与复核时限 | **5** | 条 | 举报随附的最后消息条数 | 硬数量上限 | 1805617343145907 |
| 四、生效日期与版本节点 | **2023 年 9 月 12 日** | 日期 | 模板暂停规则第一波适用国（巴西、哥伦比亚、新加坡） | 版本节点 | templates_template_pausing_md |
| 四、生效日期与版本节点 | **2023 年 10 月 12 日** | 日期 | 模板暂停规则全部业务适用 | 版本节点 | templates_template_pausing_md |
| 四、生效日期与版本节点 | **2025 年 10 月 7 日** | 日期 | 限额改为按 business portfolio 计算与设置 | 版本节点 | upcoming_messaging_limits_changes_md |
| 四、生效日期与版本节点 | **2026 年 2 月** | 月份 | max_daily_conversation_per_phone 字段移除 | 版本节点 | webhooks_reference_business_capability_update_md |
| 四、生效日期与版本节点 | **2026 年 2 月** | 月份 | current_limit 字段移除 | 版本节点 | upcoming_messaging_limits_changes_md |
| 四、生效日期与版本节点 | **v23.0／v24.0** | 版本线 | 旧字段服务 v23.0 及更早，新字段自 v24.0 起 | 版本节点 | messaging_limits_md |
| 四、生效日期与版本节点 | **2024 年 11 月** | 月份 | Business Messaging Policy 更新：opt-in 成为前置要求 | 版本节点 | getting_opt_in_md |
| 四、生效日期与版本节点 | **January 4, 2021** | 日期 | WhatsApp Terms of Service 现行生效日 | 版本节点 | https___www_whatsapp_com_legal_terms_of_service |
| 五、数量与档位 | **19** | 个码 | 体系 A 的 violation_type 品类码（全部为交易品类，零个发送行为） | 分档晋级项 | policy_enforcement_violations_md |
| 五、数量与档位 | **16** | 个值 | account_update 的 EVENT 枚举 | 分档晋级项 | webhooks_reference_account_update_md |
| 五、数量与档位 | **9** | 个值 | restriction_type 能力清单 | 分档晋级项 | webhooks_reference_account_update_md |
| 五、数量与档位 | **8** | 个码 | Direct Send 的 violation_type | 分档晋级项 | direct_send_integrity_and_content_guidelines_md |
| 五、数量与档位 | **5** | 级 | Direct Send 处置阶梯 | 分档晋级项 | direct_send_integrity_and_content_guidelines_md |
| 五、数量与档位 | **5** | 档 | 体系 A 处置阶梯（含永久停用） | 分档晋级项 | policy_enforcement_md |
| 五、数量与档位 | **4** | 条 | 体系 A 的阶梯外条款（立即下线 2 类＋负面反馈＋回滚） | 条件触发项 | policy_enforcement_md |
| 五、数量与档位 | **3** | 档 | 模板暂停实例档 | 分档晋级项 | templates_template_pausing_md |
| 五、数量与档位 | **4** | 档 | 模板质量评级 | 分档晋级项 | templates_template_quality_md |
| 五、数量与档位 | **4** | 个 | Business Manager 质量标签 | 分档晋级项 | templates_template_quality_md |
| 五、数量与档位 | **5** | 个 | message_template_status_update 的 TITLE 枚举 | 分档晋级项 | webhooks_reference_message_template_status_update_md |
| 五、数量与档位 | **14** | 个值 | message_template_status_update 的 event 枚举 | 分档晋级项 | webhooks_reference_message_template_status_update_md |
| 五、数量与档位 | **9** | 行 | 模板拒绝原因 REASON（8 具名＋null） | 分档晋级项 | webhooks_reference_message_template_status_update_md |
| 五、数量与档位 | **6** | 项 | Commerce Policy 升级路径（真人客服） | 条件触发项 | https___whatsappbusiness_com_policy_ |
| 五、数量与档位 | **15** | 类 | Commerce Policy 受监管品类 | 硬数量上限 | https___www_whatsapp_com_legal_commerce_policy |
| 五、数量与档位 | **11** | 个码 | 与处置或限额相关的同步错误码 | 分档晋级项 | support_error_codes_md |
| **合计** | **65** 行／**39** 个去重条目 | **28** 种计数单位 | 五档分别对应阶梯时长、滚动窗口、申诉时限、版本节点、数量档位 | 八类性质 **65** 行闭合：定值时长 13＋区间或多值时长 4＋模糊量时长 2＋条件触发 20＋分档晋级 13＋数量上限 4＋版本节点 8＋默认可改 1 | 官方写成区间或多值的 **4** 行全部保留原式，见第 13.1—13.2 节 |

#ROWS=65 COLS=6

# 附表 C 处置阶梯逐档表（38 行）

本表把官方分散在四份文本里的处置动作合并成一张阶梯表，列序即官方叙述顺序：**体系＝主语层级**（第 1.3 节），**阶段＝该体系内的档位序号**，**时长列逐字照抄官方措辞**，官方没给时长的登记为「未写明」并且不做推断（第 13.2 节）。共 37 档，按体系分布：Direct Send 10／商业平台 9／模板质量 8／消费者端 5／限额档位 3／商业平台端 1／商业应用端 1；其中时长列为「未写明」的 **11** 档——本表的未写明只指时长这一格，全篇留白清单见附表 F。


| 编号 | 体系 | 阶段 | 触发 | 后果 | 时长 | 出处页 |
|---|---|---|---|---|---|---|
| 1 | 商业平台 | 0 警告 | 账号违反政策（首次） | 附带所违反政策信息的警告 | 未写明 | policy_enforcement_md |
| 2 | 商业平台 | 1 短时封锁 | 反复违反商业条款：发送垃圾信息、模板误分类、高风险政策类别 | 封锁营销／实用／认证模板消息的发送与新增手机号 | 1 or 3 day | policy_enforcement_md |
| 3 | 商业平台 | 2 长时封锁 | 同上且未整改 | 封锁任何消息的发送与新增手机号 | 5, 7, or 30 day | policy_enforcement_md |
| 4 | 商业平台 | 3 账号锁 | 多次警告与功能限制／封锁后仍未改变 | 无限期封锁，只能经申诉解除 | indefinite | policy_enforcement_md |
| 5 | 商业平台 | 4 永久停用 | 多次警告与功能限制／封锁后仍未改变 | 从商业平台永久停用 | 永久 | policy_enforcement_md |
| 6 | 商业平台 | 旁路 严重伤害 | 有证据表明造成严重伤害的政策违反（四类被点名） | 立即下线该商业账号 | 立即 | policy_enforcement_md |
| 7 | 商业平台 | 旁路 负面反馈 | 账号收到过度的用户负面反馈 | 限制或下线该业务 | 未写明 | policy_enforcement_md |
| 8 | 商业平台 | 回滚 | 受限／被停用后申诉或确认问题且被撤销 | 账号回到之前的状态 | 24 to 48 hours | policy_enforcement_md |
| 9 | 商业平台 | 不可申诉 | 部分垃圾信息类违反 | 只能等限制期结束才能重新发消息 | 限制期 | policy_enforcement_md |
| 10 | Direct Send | 0 模板通知 | Meta 在 Direct Send 的实用类消息里检出营销内容 | 邮件 + template_correct_category_detection webhook，correct_category 被写为 MARKETING | 未写明 | direct_send_integrity_and_content_guidelines_md |
| 11 | Direct Send | 1 Warning | 误用被确认 | 通知并可请求复核，访问不受影响 | Ongoing | direct_send_integrity_and_content_guidelines_md |
| 12 | Direct Send | 2 Rate limiting | 误用持续 | 对 WABA 施加临时速率上限，超上限的实用发送被拒 | Until the enforcement window ends | direct_send_integrity_and_content_guidelines_md |
| 13 | Direct Send | 3 Messaging restriction | 误用持续 | 消息账号不能用 Direct Send 发实用或认证消息 | 7 days | direct_send_integrity_and_content_guidelines_md |
| 14 | Direct Send | 4 Extended restriction | 误用继续 | 延长限制；这是永久吊销前的最后一次警告 | 30 days | direct_send_integrity_and_content_guidelines_md |
| 15 | Direct Send | 5 Permanent revocation | 误用继续 | 永久吊销 Direct Send 访问 | Permanent | direct_send_integrity_and_content_guidelines_md |
| 16 | Direct Send | 复核窗口 | 模板被标记为营销 | 逾期后模板不再具备复核资格 | 60 days | direct_send_integrity_and_content_guidelines_md |
| 17 | Direct Send | 复核次数 | 每个被标记模板 | 只能复核一次；改内容会自动生成并重新分类新模板 | 1 次 | direct_send_integrity_and_content_guidelines_md |
| 18 | Direct Send | 复核期间 | 已请求复核 | 不中断发送 | a few days | direct_send_integrity_and_content_guidelines_md |
| 19 | Direct Send | 复核作用域 | 复核通过 | 只恢复模板分类，不解除账号级限制 | 未写明 | direct_send_integrity_and_content_guidelines_md |
| 20 | 模板质量 | 暂停 1 | 模板质量评级为 RED（Manager 里 Active - Low quality） | 自动暂停 | 3 hours | templates_template_pausing_md |
| 21 | 模板质量 | 暂停 2 | 再次落到 RED | 自动暂停 | 6 hours | templates_template_pausing_md |
| 22 | 模板质量 | 暂停 3 | 第三次落到 RED | 模板被禁用 | Disabled | templates_template_pausing_md |
| 23 | 模板质量 | 暂停期发送 | 命中已暂停模板 | API 拒绝、不计入限额、不收费 | 未写明 | templates_template_pausing_md |
| 24 | 模板质量 | 解暂停 | 满足暂停时长 | 自动回到 Active，质量评级按最近反馈重置 | 自动 | templates_template_pausing_md |
| 25 | 模板质量 | 适用范围（第一波） | 巴西、哥伦比亚、新加坡的业务 | 暂停规则开始适用 | September 12, 2023 | templates_template_pausing_md |
| 26 | 模板质量 | 适用范围（第二波） | 全部业务 | 暂停规则开始适用 | October 12, 2023 | templates_template_pausing_md |
| 27 | 模板质量 | 限速窗口 | 此前有实用模板被暂停过 | 新建／解暂停／非 GREEN 的实用模板被 pacing | 7 days | templates_template_pacing_md |
| 28 | 限额档位 | 起点 | 新建业务组合 | 消息限额 250 | 250 | messaging_limits_md |
| 29 | 限额档位 | 判据一 | 限额已达 2,000 | 持续发送高质量消息 | 未写明 | messaging_limits_md |
| 30 | 限额档位 | 判据二 | 限额已达 2,000 | 过去 7 天内达到当前限额的一半以上 | within 6 hours | messaging_limits_md |
| 31 | 消费者端 | 临时封禁 | 使用非官方版本的 WhatsApp，或抓取信息（scraping） | 应用内出现临时封禁提示 | 未写明 | 1848531392146538 |
| 32 | 消费者端 | 永久封禁 | 反复或严重违反使用政策 | 封禁为终局，应用内不出现请求复核入口 | 不可申诉 | 465883178708358 |
| 33 | 消费者端 | 受限账号 | 违反消息政策 | 四项能力被阻断、一项被保留，限制自动到期 | 自动到期 | 717472490411581 |
| 34 | 消费者端 | 非官方应用 | 使用被点名的非官方客户端 | 隐私与安全有风险，账号可能被临时或永久封禁 | 未写明 | 1064395290901991 |
| 35 | 消费者端 | 解封后 | 申诉被通过 | 账号回到可用状态，但部分数据可能已丢失 | 未写明 | 1297640017664693 |
| 36 | 商业应用端 | 账号封禁 | 违反 WhatsApp 商业应用的政策或使用条款 | 商业应用账号被封 | 未写明 | 723378546580115 |
| 37 | 商业平台端 | 封禁申诉 | 通过 BSP 接入的账号被封 | 由商务支持入口发起申诉 | 未写明 | 1508178557633269 |
| **合计** | 7 个体系 | 37 档 | 触发列全部为官方原句或原句直译 | 后果列含封锁范围与恢复条件 | 时长列 26 档有官方措辞、**11** 档未写明 | 见第 2—6 节 |

#ROWS=37 COLS=7

# 附表 D 枚举取值域全表（137 行）

本表收录本篇引用到的全部官方枚举，一个取值一行，**含义列照抄官方原句**，不做同义改写；组列是本文档内的枚举组代号（官方字段名），中文对应关系见下面的图例；同一概念在不同页面取值域不一致的（限额档位三套表示、模板原因码同文不同码）在本表内并列保留，不合并（第 13.3 节）。

共 136 个取值，分 23 组：alert_desc＝account_alerts 的告警描述（8）；alert_severity＝告警严重度（3）；alert_status＝告警状态（2）；alert_type＝告警类型（6）；bcu_legacy_limit＝business_capability_update 旧版限额数值（5）；bcu_tier＝business_capability_update 新版限额档位（5）；bcu_trigger＝business_capability_update 的触发原因（2）；disconnection_reason＝消息断连原因（6）；dsladder＝Direct Send 处置阶梯档（5）；dsviol＝Direct Send 的 violation_type（8）；entity_type＝安全类 webhook 的实体类型（3）；event＝account_update 的事件（16）；pause＝模板暂停实例档（3）；quality＝号码质量评级（4）；qualitylabel＝Business Manager 模板质量标签（4）；restriction_type＝account_update 的能力封锁类型（9）；reviewstate＝account_review_update 的复核状态（4）；tier＝phone_number_quality_update 的限额档位（7）；tpl_category＝模板类别（3）；tpl_event＝message_template_status_update 的事件（14）；tpl_reason＝模板拒绝原因 REASON（9）；tpl_title＝模板状态 TITLE（5）；tpl_trigger＝触发模板状态变化的原因（5）


| 编号 | 组 | 取值 | 官方含义 | 出处页 |
|---|---|---|---|---|
| D1 | restriction_type | RESTRICTED_ADD_PHONE_NUMBER_ACTION | Business cannot add new phone numbers to the account. | webhooks_reference_account_update_md |
| D2 | restriction_type | RESTRICTED_BIZ_INITIATED_AND_USER_INITIATED_CALLING | Business cannot make or receive calls. | webhooks_reference_account_update_md |
| D3 | restriction_type | RESTRICTED_BIZ_INITIATED_MESSAGING | Business cannot initiate conversations with customers. | webhooks_reference_account_update_md |
| D4 | restriction_type | RESTRICTED_BUSINESS_INITIATED_CALLING | Business cannot initiate outbound calls. | webhooks_reference_account_update_md |
| D5 | restriction_type | RESTRICTED_CUSTOMER_INITIATED_MESSAGING | Business cannot respond to customer-initiated messages. | webhooks_reference_account_update_md |
| D6 | restriction_type | RESTRICTED_DIRECT_SEND_UTILITY_TEMPLATES | Business cannot send utility templates via Direct Send. | webhooks_reference_account_update_md |
| D7 | restriction_type | RESTRICTED_USER_INITIATED_CALLING | Business cannot receive inbound calls from users. | webhooks_reference_account_update_md |
| D8 | restriction_type | RESTRICTED_USER_INITIATED_CALLING_CALL_BUTTON_HIDDEN | Call button is hidden from users due to low pickup rates. | webhooks_reference_account_update_md |
| D9 | restriction_type | RESTRICTED_UTILITY_TEMPLATES | Business cannot create utility templates. | webhooks_reference_account_update_md |
| D10 | event | ACCOUNT_DELETED | Indicates the Messaging account was deleted. | webhooks_reference_account_update_md |
| D11 | event | ACCOUNT_RESTRICTION | Indicates your WhatsApp account has been restricted due to [policy violations](https://developers.facebook.com | webhooks_reference_account_update_md |
| D12 | event | ACCOUNT_VIOLATION | Indicates your WhatsApp account violated Meta [policies or terms](https://developers.facebook.com/documentatio | webhooks_reference_account_update_md |
| D13 | event | AD_ACCOUNT_LINKED | Indicates the Messaging account has been onboarded onto Marketing Messages API for WhatsApp through Embedded S | webhooks_reference_account_update_md |
| D14 | event | AUTH_INTL_PRICE_ELIGIBILITY_UPDATE | Indicates the Messaging account is eligible for [authentication-international rates](https://developers.facebo | webhooks_reference_account_update_md |
| D15 | event | BUSINESS_PRIMARY_LOCATION_COUNTRY_UPDATE | Indicates the Messaging account's [primary business location](https://developers.facebook.com/documentation/bu | webhooks_reference_account_update_md |
| D16 | event | DISABLED_UPDATE | Indicates your WhatsApp account violated Meta [policies or terms](https://developers.facebook.com/documentatio | webhooks_reference_account_update_md |
| D17 | event | MM_LITE_TERMS_SIGNED | Indicates that the Messaging account has successfully accepted the MM API for WhatsApp terms of service. | webhooks_reference_account_update_md |
| D18 | event | PARTNER_ADDED | Indicates a WhatsApp account or Messaging account has been shared with a [Solution Partner](https://developers | webhooks_reference_account_update_md |
| D19 | event | PARTNER_APP_INSTALLED | Indicates a business customer granted the app one or more permissions. | webhooks_reference_account_update_md |
| D20 | event | PARTNER_APP_UNINSTALLED | Indicates a business customer deauthenticated or uninstalled the app. | webhooks_reference_account_update_md |
| D21 | event | PARTNER_CLIENT_CERTIFICATION_STATUS_UPDATE | Indicates your WhatsApp account's [partner-led business verification](https://developers.facebook.com/document | webhooks_reference_account_update_md |
| D22 | event | PARTNER_REMOVED | Indicates a WhatsApp account or Messaging account has been unshared with a [Solution Partner](https://develope | webhooks_reference_account_update_md |
| D23 | event | VOLUME_BASED_PRICING_TIER_UPDATE | Indicates the Messaging account's volume-based pricing tier has been updated. | webhooks_reference_account_update_md |
| D24 | event | ACCOUNT_OFFBOARDED | Indicates the WhatsApp account has been offboarded due to a device change or phone number reregistration. | webhooks_reference_account_update_md |
| D25 | event | ACCOUNT_RECONNECTED | Indicates the WhatsApp account has been reconnected after a device change or phone number reregistration. | webhooks_reference_account_update_md |
| D26 | disconnection_reason | ACCOUNT_DISCONNECTED | Your client's account was disconnected due to [enforcement](https://developers.facebook.com/documentation/business-messa | webhooks_reference_account_update_md |
| D27 | disconnection_reason | BUSINESS_DOWNGRADE | Your client registered their business phone number with the consumer WhatsApp app. | webhooks_reference_account_update_md |
| D28 | disconnection_reason | CHANGE_NUMBER | Your client changed their phone number. | webhooks_reference_account_update_md |
| D29 | disconnection_reason | COMPANION_INACTIVITY | A companion device was inactive for approximately 30 days. | webhooks_reference_account_update_md |
| D30 | disconnection_reason | PRIMARY_INACTIVITY | The primary device was inactive for approximately 14 days. | webhooks_reference_account_update_md |
| D31 | disconnection_reason | USER_RE_REGISTERED | Your client re-registered on a new device. | webhooks_reference_account_update_md |
| D32 | alert_type | INCREASED_CAPABILITIES_ELIGIBILITY_DEFERRED | Indicates Meta doesn't have enough message signal to make a determination, identity verification was rejected, or your m | webhooks_reference_account_alerts_md |
| D33 | alert_type | INCREASED_CAPABILITIES_ELIGIBILITY_FAILED | Indicates messaging limits cannot be increased due to past messaging activity. Possible solutions are to [request an inc | webhooks_reference_account_alerts_md |
| D34 | alert_type | INCREASED_CAPABILITIES_ELIGIBILITY_NEED_MORE_INFO | Indicates messaging limits cannot be increased due to past messaging activity. Possible solutions are to [verify your id | webhooks_reference_account_alerts_md |
| D35 | alert_type | OBA_APPROVED | Indicates Official Business Account status approved. | webhooks_reference_account_alerts_md |
| D36 | alert_type | OBA_REJECTED | Indicates Official Business Account ("OBA") status denied. Review OBA [criteria](https://developers.facebook.com/documen | webhooks_reference_account_alerts_md |
| D37 | alert_type | PROFILE_PICTURE_LOST | Indicates the business phone number's business profile photo has been deleted. Upload a new photo using [WhatsApp Manage | webhooks_reference_account_alerts_md |
| D38 | alert_severity | CRITICAL | Indicates a rejection or denial. The `alert_description` value may describe actions that c | webhooks_reference_account_alerts_md |
| D39 | alert_severity | INFORMATIONAL | Indicates webhooks contain only informational data and no action is needed. | webhooks_reference_account_alerts_md |
| D40 | alert_severity | WARNING | Indicates action may be needed. The `alert_description` value describes possible actions t | webhooks_reference_account_alerts_md |
| D41 | entity_type | BUSINESS | Indicates a change associated with a business portfolio. | webhooks_reference_account_alerts_md |
| D42 | entity_type | PHONE_NUMBER | Indicates a change associated with a business phone number. | webhooks_reference_account_alerts_md |
| D43 | entity_type | CURRENT_STATUS_ID | Indicates a change associated with a business phone number's [business profile](https://de | webhooks_reference_account_alerts_md |
| D44 | alert_status | ACTIVE |  | webhooks_reference_account_alerts_md |
| D45 | alert_status | NONE |  | webhooks_reference_account_alerts_md |
| D46 | alert_desc | Additional verification is required for your business &#123;NAME&#125;. Go to Security Center in |  | webhooks_reference_account_alerts_md |
| D47 | alert_desc | Based on your activity, limits cannot be increased for your business. Contact support for more i |  | webhooks_reference_account_alerts_md |
| D48 | alert_desc | Limits cannot be increased for your business <BUSINESS_PORTFOLIO_NAME>. Use WhatsApp Business pl |  | webhooks_reference_account_alerts_md |
| D49 | alert_desc | Limits cannot be increased at this time for your business <BUSINESS_PORTFOLIO_NAME>. Limits cann |  | webhooks_reference_account_alerts_md |
| D50 | alert_desc | Please reupload your profile picture |  | webhooks_reference_account_alerts_md |
| D51 | alert_desc | This phone number now has a green badge next to its name showing that it's an authentic and nota |  | webhooks_reference_account_alerts_md |
| D52 | alert_desc | We do not grant official business accounts to individuals. Your display name must be directly as |  | webhooks_reference_account_alerts_md |
| D53 | alert_desc | Your messaging quality was too low to unlock more capabilities at this time. Alternatively, your |  | webhooks_reference_account_alerts_md |
| D54 | quality | GREEN | — Indicates high quality. The template has received little to no negative feedback from WhatsApp users. The template can | templates_template_quality_md |
| D55 | quality | YELLOW | — Indicates medium quality. The template has received negative feedback from multiple WhatsApp users, or low read-rates, | templates_template_quality_md |
| D56 | quality | RED | — Indicates low quality. The template has received negative feedback from multiple WhatsApp users, or low read-rates. Th | templates_template_quality_md |
| D57 | quality | UNKNOWN | — Indicates a quality score is still pending, because it has yet to receive WhatsApp user feedback or read-rate data. Th | templates_template_quality_md |
| D58 | qualitylabel | Active - **Quality pending** (equates to an `UNKNOWN` quality score) |  | templates_template_quality_md |
| D59 | qualitylabel | Active - **High quality** (equates to a `GREEN` quality score) |  | templates_template_quality_md |
| D60 | qualitylabel | Active - **Medium quality** (equates to a `YELLOW` quality score) |  | templates_template_quality_md |
| D61 | qualitylabel | Active - **Low quality** (equates to a `RED` quality score) |  | templates_template_quality_md |
| D62 | pause | 1st Instance: Paused for 3 hours |  | templates_template_pausing_md |
| D63 | pause | 2nd Instance: Paused for 6 hours |  | templates_template_pausing_md |
| D64 | pause | 3rd Instance: Disabled |  | templates_template_pausing_md |
| D65 | dsladder | Warning | Meta notifies you of the misuse. You can [request a review](#request-a-review-of-a-flagged-template) of the te | direct_send_integrity_and_content_guidelines_md |
| D66 | dsladder | Rate limiting | Meta applies a temporary rate-limit cap to the WABA. It can still send utility messages up to a capped volume; | direct_send_integrity_and_content_guidelines_md |
| D67 | dsladder | Messaging restriction | The Messaging account can't use Direct Send for utility or authentication messaging. | direct_send_integrity_and_content_guidelines_md |
| D68 | dsladder | Extended restriction | Continued misuse extends the restriction. This is the final warning before permanent revocation. | direct_send_integrity_and_content_guidelines_md |
| D69 | dsladder | Permanent revocation | Direct Send access is permanently revoked. | direct_send_integrity_and_content_guidelines_md |
| D70 | dsviol | DIRECT_SEND_UTILITY_CATEGORY_ABUSE_WARN |  | direct_send_integrity_and_content_guidelines_md |
| D71 | dsviol | DIRECT_SEND_UTILITY_TEMPLATE_ABUSE |  | direct_send_integrity_and_content_guidelines_md |
| D72 | dsviol | DIRECT_SEND_UTILITY_CATEGORY_ABUSE_RATE_LIMIT |  | direct_send_integrity_and_content_guidelines_md |
| D73 | dsviol | DIRECT_SEND_UTILITY_CATEGORY_ABUSE_STRIKE_1 |  | direct_send_integrity_and_content_guidelines_md |
| D74 | dsviol | DIRECT_SEND_UTILITY_CATEGORY_ABUSE_STRIKE_2 |  | direct_send_integrity_and_content_guidelines_md |
| D75 | dsviol | DIRECT_SEND_UTILITY_CATEGORY_ABUSE_OFFBOARD |  | direct_send_integrity_and_content_guidelines_md |
| D76 | dsviol | DIRECT_SEND_UTILITY_CATEGORY_ABUSE_UNBAN |  | direct_send_integrity_and_content_guidelines_md |
| D77 | dsviol | DIRECT_SEND_UTILITY_CATEGORY_ABUSE_RATE_LIMIT_RECOVERY |  | direct_send_integrity_and_content_guidelines_md |
| D78 | reviewstate | Available for review | The template was flagged as marketing and is eligible for review. | direct_send_integrity_and_content_guidelines_md |
| D79 | reviewstate | In review | You've requested a review, and Meta is evaluating the template. Most reviews take a few days. | direct_send_integrity_and_content_guidelines_md |
| D80 | reviewstate | Reversed | The review was approved. The template's classification is restored to utility, and your messages continue to be delivere | direct_send_integrity_and_content_guidelines_md |
| D81 | reviewstate | Unchanged | The review confirmed the content is marketing, and the classification stands. | direct_send_integrity_and_content_guidelines_md |
| D82 | tier | TIER_50 | Indicates a messaging limit of 50. | webhooks_reference_phone_number_quality_update_md |
| D83 | tier | TIER_250 | Indicates a messaging limit of 250. | webhooks_reference_phone_number_quality_update_md |
| D84 | tier | TIER_2K | Indicates a messaging limit of 2,000. | webhooks_reference_phone_number_quality_update_md |
| D85 | tier | TIER_10K | Indicates a messaging limit of 10,000. | webhooks_reference_phone_number_quality_update_md |
| D86 | tier | TIER_100K | Indicates a messaging limit of 100,000. | webhooks_reference_phone_number_quality_update_md |
| D87 | tier | TIER_NOT_SET | Indicates the business phone number has not been used to send a messag | webhooks_reference_phone_number_quality_update_md |
| D88 | tier | TIER_UNLIMITED | Indicates the business phone number has higher throughput. | webhooks_reference_phone_number_quality_update_md |
| D89 | tpl_event | APPROVED | Indicates the template has been approved and can now be sent in template messages. | webhooks_reference_message_template_status_update_md |
| D90 | tpl_event | ARCHIVED | Indicates the template has been [archived](https://developers.facebook.com/documentation/b | webhooks_reference_message_template_status_update_md |
| D91 | tpl_event | UNARCHIVED | Indicates the template has been [unarchived](https://developers.facebook.com/documentation | webhooks_reference_message_template_status_update_md |
| D92 | tpl_event | DELETED | Indicates the template has been deleted. | webhooks_reference_message_template_status_update_md |
| D93 | tpl_event | DISABLED | Indicates the template has been disabled due to user [feedback](https://developers.faceboo | webhooks_reference_message_template_status_update_md |
| D94 | tpl_event | FLAGGED | Indicates the template has received negative feedback and is at risk of being disabled. | webhooks_reference_message_template_status_update_md |
| D95 | tpl_event | IN_APPEAL | Indicates the template is in the [appeal](https://developers.facebook.com/documentation/bu | webhooks_reference_message_template_status_update_md |
| D96 | tpl_event | LIMIT_EXCEEDED | Indicates the Messaging account template is at its [template limit](https://developers.fac | webhooks_reference_message_template_status_update_md |
| D97 | tpl_event | LOCKED | Indicates the template has been locked and cannot be edited. | webhooks_reference_message_template_status_update_md |
| D98 | tpl_event | PAUSED | Indicates the template has been [paused](https://developers.facebook.com/documentation/bus | webhooks_reference_message_template_status_update_md |
| D99 | tpl_event | PENDING | Indicates the template is undergoing template review. | webhooks_reference_message_template_status_update_md |
| D100 | tpl_event | REINSTATED | Indicates the template is no longer flagged or disabled and can be sent in template messag | webhooks_reference_message_template_status_update_md |
| D101 | tpl_event | PENDING_DELETION | Indicates template has been deleted via WhatsApp Manager. | webhooks_reference_message_template_status_update_md |
| D102 | tpl_event | REJECTED | Indicates the template has been rejected. You can [edit the template](https://developers.f | webhooks_reference_message_template_status_update_md |
| D103 | tpl_reason | ABUSIVE_CONTENT | Indicates template contains content that violates our policies. | webhooks_reference_message_template_status_update_md |
| D104 | tpl_reason | CATEGORY_NOT_AVAILABLE | (Deprecated) Indicates an authentication templates for an unsupported region. | webhooks_reference_message_template_status_update_md |
| D105 | tpl_reason | INCORRECT_CATEGORY | Indicates the template's content doesn't match the category designated at the time of temp | webhooks_reference_message_template_status_update_md |
| D106 | tpl_reason | INVALID_FORMAT | Indicates template has an invalid format. | webhooks_reference_message_template_status_update_md |
| D107 | tpl_reason | NONE | Indicates template was paused. | webhooks_reference_message_template_status_update_md |
| D108 | tpl_reason | PROMOTIONAL | Indicates template contains content that violates our policies. | webhooks_reference_message_template_status_update_md |
| D109 | tpl_reason | SCAM | Indicates template contains content that violates our policies. | webhooks_reference_message_template_status_update_md |
| D110 | tpl_reason | TAG_CONTENT_MISMATCH | Indicates the template's content doesn't match the category designated at the time of temp | webhooks_reference_message_template_status_update_md |
| D111 | tpl_reason | null | 模板被计划删除时该字段取 null 而不是字符串 | webhooks_reference_message_template_status_update_md |
| D112 | tpl_title | FIRST_PAUSE | Indicates template has been paused for the first time. | webhooks_reference_message_template_status_update_md |
| D113 | tpl_title | SECOND_PAUSE | Indicates the template has been paused a second time. | webhooks_reference_message_template_status_update_md |
| D114 | tpl_title | RATE_LIMITING_PAUSE | Indicates template has been paused due to rate limiting. | webhooks_reference_message_template_status_update_md |
| D115 | tpl_title | UNPAUSE | Indicates template has been unpaused. | webhooks_reference_message_template_status_update_md |
| D116 | tpl_title | DISABLED | Indicates template has been disabled. | webhooks_reference_message_template_status_update_md |
| D117 | tpl_category | MARKETING | Indicates template is categorized as MARKETING. | webhooks_reference_message_template_status_update_md |
| D118 | tpl_category | UTILITY | Indicates the template is categorized as UTILITY. | webhooks_reference_message_template_status_update_md |
| D119 | tpl_category | AUTHENTICATION | Indicates template is categorized as AUTHENTICATION. | webhooks_reference_message_template_status_update_md |
| D120 | tpl_trigger | A template is approved. |  | webhooks_reference_message_template_status_update_md |
| D121 | tpl_trigger | A template is rejected. |  | webhooks_reference_message_template_status_update_md |
| D122 | tpl_trigger | A template is disabled. |  | webhooks_reference_message_template_status_update_md |
| D123 | tpl_trigger | A template is archived. |  | webhooks_reference_message_template_status_update_md |
| D124 | tpl_trigger | A template is unarchived. |  | webhooks_reference_message_template_status_update_md |
| D125 | bcu_legacy_limit | 旧字段数值 250 | 将在 2026 年 2 月移除的 max_daily_conversation_per_phone 取值；-1 表示不限 | webhooks_reference_business_capability_update_md |
| D126 | bcu_legacy_limit | 旧字段数值 2000 | 将在 2026 年 2 月移除的 max_daily_conversation_per_phone 取值；-1 表示不限 | webhooks_reference_business_capability_update_md |
| D127 | bcu_legacy_limit | 旧字段数值 10000 | 将在 2026 年 2 月移除的 max_daily_conversation_per_phone 取值；-1 表示不限 | webhooks_reference_business_capability_update_md |
| D128 | bcu_legacy_limit | 旧字段数值 100000 | 将在 2026 年 2 月移除的 max_daily_conversation_per_phone 取值；-1 表示不限 | webhooks_reference_business_capability_update_md |
| D129 | bcu_legacy_limit | 旧字段数值 -1 | 将在 2026 年 2 月移除的 max_daily_conversation_per_phone 取值；-1 表示不限 | webhooks_reference_business_capability_update_md |
| D130 | bcu_tier | TIER_250 | 新字段 max_daily_conversations_per_business 取值 | webhooks_reference_business_capability_update_md |
| D131 | bcu_tier | TIER_2K | 新字段 max_daily_conversations_per_business 取值 | webhooks_reference_business_capability_update_md |
| D132 | bcu_tier | TIER_10K | 新字段 max_daily_conversations_per_business 取值 | webhooks_reference_business_capability_update_md |
| D133 | bcu_tier | TIER_100K | 新字段 max_daily_conversations_per_business 取值 | webhooks_reference_business_capability_update_md |
| D134 | bcu_tier | TIER_UNLIMITED | 新字段 max_daily_conversations_per_business 取值 | webhooks_reference_business_capability_update_md |
| D135 | bcu_trigger | A Messaging account is created. |  | webhooks_reference_business_capability_update_md |
| D136 | bcu_trigger | A WhatsApp account hierarchy or business portfolio business capability (for example, [messaging limits](https://developers.facebook.com/documentation/business-messaging/whatsapp/messaging-limits#increasing-your-limit), [phone number limits](https://developers.facebook.com/documentation/business-messaging/whatsapp/business-phone-numbers/phone-numbers#registered-number-limits)) is increased or decreased. |  | webhooks_reference_business_capability_update_md |
| **合计** | 23 组 | 136 个取值 | 逐字照抄，未做归并 | 见第 8—9 节与附表 F |

#ROWS=136 COLS=5

# 附表 E 品类码与错误码表（52 行）

本表四组：体系 A 的 19 个 `violation_type` 品类码、Commerce Policy 的 15 个受监管品类、6 项升级路径、11 个与处置或限额相关的同步错误码。

共 51 项，分 4 组：升级路径、受监管品类、同步错误码、违规类型码


| 编号 | 组 | 条目 | 出处页 |
|---|---|---|---|
| E1 | 违规类型码 19 | ADULT（示例 5 条） | policy_enforcement_violations_md |
| E2 | 违规类型码 19 | ALCOHOL（示例 4 条） | policy_enforcement_violations_md |
| E3 | 违规类型码 19 | ANIMALS（示例 9 条） | policy_enforcement_violations_md |
| E4 | 违规类型码 19 | BODY_PARTS_FLUIDS（示例 8 条） | policy_enforcement_violations_md |
| E5 | 违规类型码 19 | DATING（示例 0 条） | policy_enforcement_violations_md |
| E6 | 违规类型码 19 | DIGITAL_SERVICES_PRODUCTS（示例 5 条） | policy_enforcement_violations_md |
| E7 | 违规类型码 19 | DRUGS（示例 3 条） | policy_enforcement_violations_md |
| E8 | 违规类型码 19 | GAMBLING（示例 0 条） | policy_enforcement_violations_md |
| E9 | 违规类型码 19 | HEALTHCARE（示例 8 条） | policy_enforcement_violations_md |
| E10 | 违规类型码 19 | ILLEGAL_PRODUCTS（示例 0 条） | policy_enforcement_violations_md |
| E11 | 违规类型码 19 | MISLEADING（示例 5 条） | policy_enforcement_violations_md |
| E12 | 违规类型码 19 | OVERTLY_SEXUALIZED_POSITIONING（示例 3 条） | policy_enforcement_violations_md |
| E13 | 违规类型码 19 | REAL_FAKE_CURRENCY（示例 8 条） | policy_enforcement_violations_md |
| E14 | 违规类型码 19 | SCAM（示例 0 条） | policy_enforcement_violations_md |
| E15 | 违规类型码 19 | SUPPLEMENTS（示例 6 条） | policy_enforcement_violations_md |
| E16 | 违规类型码 19 | THIRD_PARTY_INFRINGEMENTS（示例 2 条） | policy_enforcement_violations_md |
| E17 | 违规类型码 19 | TOBACCO（示例 7 条） | policy_enforcement_violations_md |
| E18 | 违规类型码 19 | UNAUTHORIZED_MEDIA（示例 5 条） | policy_enforcement_violations_md |
| E19 | 违规类型码 19 | WEAPONS（示例 9 条） | policy_enforcement_violations_md |
| E20 | 受监管品类 15 | Firearms | https___whatsappbusiness_com_policy_ |
| E21 | 受监管品类 15 | Alcohol and tobacco | https___whatsappbusiness_com_policy_ |
| E22 | 受监管品类 15 | Drugs, whether prescription, recreational, or otherwise | https___whatsappbusiness_com_policy_ |
| E23 | 受监管品类 15 | Medical and healthcare products | https___whatsappbusiness_com_policy_ |
| E24 | 受监管品类 15 | Endangered species (wildlife and plants) | https___whatsappbusiness_com_policy_ |
| E25 | 受监管品类 15 | Live non-endangered animals excluding livestock | https___whatsappbusiness_com_policy_ |
| E26 | 受监管品类 15 | Hazardous goods and materials | https___whatsappbusiness_com_policy_ |
| E27 | 受监管品类 15 | Real, virtual, or fake currency, including ICOs and binary options | https___whatsappbusiness_com_policy_ |
| E28 | 受监管品类 15 | Body parts or fluids | https___whatsappbusiness_com_policy_ |
| E29 | 受监管品类 15 | Business models, goods, items, or services that we determine may be or are fraudulent, misleading, offensive,  | https___whatsappbusiness_com_policy_ |
| E30 | 受监管品类 15 | Real Money Gambling and Gaming (Online Gambling and Gaming is a subset of Real Money Gambling and Gaming. See  | https___whatsappbusiness_com_policy_ |
| E31 | 受监管品类 15 | Adult products and services | https___whatsappbusiness_com_policy_ |
| E32 | 受监管品类 15 | Dating services | https___whatsappbusiness_com_policy_ |
| E33 | 受监管品类 15 | Multi-level marketing | https___whatsappbusiness_com_policy_ |
| E34 | 受监管品类 15 | Payday loans, paycheck advances, peer-to-peer lending, debt collection, and bail bonds | https___whatsappbusiness_com_policy_ |
| E35 | 升级路径 6 | In-Chat Human Agent transfer | https___whatsappbusiness_com_policy_ |
| E36 | 升级路径 6 | Phone number | https___whatsappbusiness_com_policy_ |
| E37 | 升级路径 6 | Email | https___whatsappbusiness_com_policy_ |
| E38 | 升级路径 6 | Web support (on the business website) | https___whatsappbusiness_com_policy_ |
| E39 | 升级路径 6 | In-store visits (ex: visit retail store, bank branch), or | https___whatsappbusiness_com_policy_ |
| E40 | 升级路径 6 | Support form | https___whatsappbusiness_com_policy_ |
| E41 | 同步错误码 11 | 130429（吞吐上限）：Cloud API message throughput has been reached. | support_error_codes_md |
| E42 | 同步错误码 11 | 130497（按国家受限）：The WhatsApp Business Account is restricted from messaging to users in c | support_error_codes_md |
| E43 | 同步错误码 11 | 131031（账号被限制或停用）：The WhatsApp Business Account associated with the app has been restricte | support_error_codes_md |
| E44 | 同步错误码 11 | 131047（24 小时窗口已过）：More than 24 hours have passed since the recipient last replied to the s | support_error_codes_md |
| E45 | 同步错误码 11 | 131048（号码发送量受限）：Message failed to send because there are restrictions on how many messag | support_error_codes_md |
| E46 | 同步错误码 11 | 131049（单用户营销上限）：This message was not delivered to maintain healthy ecosystem engagement. | support_error_codes_md |
| E47 | 同步错误码 11 | 131056（同收发对过频）：Too many messages sent from the sender phone number to the same recipien | support_error_codes_md |
| E48 | 同步错误码 11 | 131064（分类违规达限额）：Message failed to send because this account has reached its messaging li | support_error_codes_md |
| E49 | 同步错误码 11 | 132015（模板被暂停）：Template is paused due to [low quality](https://developers.facebook.com/ | support_error_codes_md |
| E50 | 同步错误码 11 | 133016（注册尝试过频）：Registration or Deregistration failed because there were too many attemp | support_error_codes_md |
| E51 | 同步错误码 11 | 139200（Direct Send 被封锁）：1 | direct_send_integrity_and_content_guidelines_md |
| **合计** | 4 组 | 51 项 | 见第 4 节与第 12 节 |

#ROWS=51 COLS=4

# 附表 F 页面之间的口径冲突与官方未写明清单（25 行）

本表两截：前 **8** 行是**同一主题在两份官方文本里写法不同**，后 16 行是**官方主动留白**（第 13.2 节把其中十一项列在正文，本表另收5 项外围留白）。处理原则与第 1.4 节一致：**两句都抄，不合并、不取其一为准**；留白一律原样登记，不用其他平台惯例补齐。

| 类 | 主题 | 甲页面／甲处的写法 | 乙页面／乙处的写法 | 差异性质 | 本篇的处理 |
|---|---|---|---|---|---|

| 冲突 | **offboard 的词义** | policy-enforcement：WhatsApp will immediately offboard these Business accounts（因严重伤害下线） | account_update 参考页：ACCOUNT_OFFBOARDED = "offboarded due to a device change or a phone number reregistration"（因换设备或重注册下线） | 词义不同，两个意思共用一个词根 | 两处原句并列引用，第 8.1 节标注这是本篇最需要注意的同词异义 |
| 冲突 | **ban 词族的三层用法** | 帮助中心：Temporarily banned／the ban is final／After your account is unbanned | Direct Send：DIRECT_SEND_UTILITY_CATEGORY_ABUSE_UNBAN（限制解除）；政策执行页：permanently disabled | 层级不同（消费者封禁／平台限制解除／账号停用） | 三套词各归本章，不把 UNBAN 译成"解封" |
| 冲突 | **申诉时限四个数** | 体系 A：typically takes 24 to 48 hours；模板平面：within 24 hours | 体系 B：Most reviews take a few days；消费者端：Most reviews are completed within 24 hours | 流程不同，四个数分属四套流程且无对照句 | 四行并列入附表 B 第三档，不给出"WhatsApp 申诉要多久"的单值 |
| 冲突 | **restriction 的三个主语** | 体系 A：Business Account／WhatsApp accounts；帮助中心：your account（受限自动到期） | 体系 B：WABA（限速）与 Messaging account（能力封锁）在同一条阶梯里换主语；限额：business portfolio | 主语层级不同，官方无映射句 | 每次引用都带主语，第 1.3 节列表说明三套身份 |
| 冲突 | **限额档位的三套取值域** | bcu 旧字段 5 个数值含 -1；bcu 新字段 5 个 TIER_* 串 | phone_number_quality_update 7 个 TIER_*，含任何阶梯页都没有的 TIER_50 与 TIER_NOT_SET | 同一档位概念三套表示，且集合不相等 | 附表 D 分三组登记（bcu_legacy_limit／bcu_tier／tier），正文第 7.5 节明说"不做合并" |
| 冲突 | **通知收件人的两种措辞** | 体系 A："Sent as an email to all admins set in Business Manager"（角色名） | 体系 B："Sent to all Messaging account admins (people with Full control of the account)"（权限名） | 权限词不同，宽窄未对齐 | 两句逐字引用，不推断哪个范围更大 |
| 冲突 | **质量与限额的关系被删除** | 2025 年 10 月 7 日前：Flagged 连续 7 天立即降一档；1,000 是自动升档资格档位 | 此后：Flagged 状态 no longer possible，质量下降不降限额；资格档位改为 2,000 | 版本不同，官方以 reference purposes 保留旧页 | 现行句为主，旧句在第 7.6 节按换代表逐格引用 |
| 冲突 | **模板原因码同文不同码** | ABUSIVE_CONTENT／PROMOTIONAL／SCAM 三个码的官方描述逐字相同（Indicates template contains content that violates our policies.） | INCORRECT_CATEGORY 与 TAG_CONTENT_MISMATCH 的描述也逐字相同 | 枚举词表不区分语义 | 附表 D 保留三行原文并加注"不可区分"，正文第 6.5 节明说无法从原因码判断类别 |
| 未写明 | **体系 A 警告之后是否必然进入封锁** | 官方原句：官方用 might start seeing，无必然性句 | — | 主动留白（非疏漏） | 正文第 3.2、13.2 节已列 |
| 未写明 | **两组封锁区间里具体取哪个值** | 官方原句：只写 gradually increase in duration | — | 主动留白（非疏漏） | 正文第 3.3、13.2 节已列 |
| 未写明 | **体系 A 与体系 B 之间有没有对应关系** | 官方原句：无连接句；唯一逐字重合是 adding additional phone numbers | — | 主动留白（非疏漏） | 正文第 2.1、5.4 节已列 |
| 未写明 | **Direct Send 限速一级的具体上限数值** | 官方原句：只写 a capped volume | — | 主动留白（非疏漏） | 正文第 5.2、13.2 节已列 |
| 未写明 | **the enforcement window 的长度** | 官方原句：只写 Until … or the enforcement window ends | — | 主动留白（非疏漏） | 正文第 5.2、13.2 节已列 |
| 未写明 | **误分类率回落到 threshold 以下的那个阈值** | 官方原句：只写 falls back under the threshold | — | 主动留白（非疏漏） | 正文第 5.7、13.2 节已列 |
| 未写明 | **pacing 的触发阈值** | 官方原句：官方原话 an unspecified threshold | — | 主动留白（非疏漏） | 正文第 6.3、13.2 节已列 |
| 未写明 | **excessive negative feedback 的量化阈值** | 官方原句：无任何数字 | — | 主动留白（非疏漏） | 正文第 3.7、13.2 节已列 |
| 未写明 | **质量评级三个输入信号的权重与更新频率** | 官方原句：只有一句 usage, customer feedback, and engagement 并列 | — | 主动留白（非疏漏） | 正文第 6.1、13.2 节已列 |
| 未写明 | **indefinite 账号锁的申诉时限与次数上限** | 官方原句：只写 can only be removed via an appeal | — | 主动留白（非疏漏） | 正文第 3.4、10.1 节已列 |
| 未写明 | **哪些 spam 类违规不可申诉** | 官方原句：只写 Not all spam violations might be appealed | — | 主动留白（非疏漏） | 正文第 3.8、10.1 节已列 |
| 未写明 | **受限账号的具体时长** | 官方原句：只写 the allocated time，并给出应用内查看剩余时间的方法 | — | 主动留白（非疏漏） | 本篇补登 |
| 未写明 | **harvesting／scraping 的数量阈值** | 官方原句：两页都只写 violates our Terms of Service | — | 主动留白（非疏漏） | 本篇补登 |
| 未写明 | **automated processing 具体自动化了哪些领域的判定** | 官方原句：只写 automates decisions for certain areas | — | 主动留白（非疏漏） | 本篇补登 |
| 未写明 | **品类码与 130497 的国家清单映射** | 官方原句：错误码页指向政策页的 allowed countries，两处无对照表 | — | 主动留白（非疏漏） | 本篇补登 |
| 未写明 | **131049 重试惩罚里 multiple times 的次数** | 官方原句：官方原句：attempts to resend marketing messages multiple times within a 24-hour period，无次数 | — | 主动留白（非疏漏） | 本篇补登 |
| **合计** | **8 组冲突＋16 项留白＝24 行** | 八组冲突里没有一组是数字算错，全部是词义、主语、流程、表示域、措辞、版本或词表不区分 | 16 项留白里没有一项给出过数字 | — | 见第 13.2—13.3 节 |

#ROWS=24 COLS=6

---

**边界声明：** 本篇是一份**官方规则文本的口径梳理**，不是操作手册，也不是对平台治理公正性的评价。全篇内容限于 WhatsApp 与 Meta 公开页面已经写出的表述：Business Platform 的 policy-enforcement 与 violations、Direct Send 的 integrity-and-content-guidelines、supported-features-and-limits 与 api-reference、templates 下的 quality／pausing／pacing／per-user limits、messaging-limits 与其换代说明、九个 webhooks 参考页与 error-codes，加上帮助中心十三条封禁与治理问答、六条法律与政策页。引用这些文本的目的只有两个：说明平台在公开规则里把哪些行为定义为违规、把哪些处置写成阶梯；说明官方在每一级上承诺给出什么通知、留下什么复核入口。本篇**不含**批量发送脚本、发送自动化搭建、账号池、多号轮换、设备农场、限速或频控规避、垃圾信息检测绕过、封号规避与申诉话术模板；**不含**任何可用于向非自愿收件人大量投递消息的方法，也**不含**检测与规避的实现方式；**不含**利用代码、恶意样本或利用链构造方式；**不含**任何针对他人设备或他人账号的操作步骤与时序。凡官方写成区间（`1 or 3 day`、`5, 7, or 30 day`、`24 to 48 hours`）、模糊量（`a few days`）或主动标注 `unspecified` 之处，本篇原样保留并把该处登记为留白，不推测阈值、不给出单值、不以其他平台惯例补齐。三套处置体系与一个模板平面之间官方从未给出对齐句，本篇不做出任何跨体系的等价推断。本次未能取回正文的 5 个页面在附表 A 逐行标注，其内容未被引用。

Telegram: https://t.me/one00190
