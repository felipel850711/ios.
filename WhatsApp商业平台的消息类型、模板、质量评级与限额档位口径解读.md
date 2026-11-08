@@TITLE@@WhatsApp 商业平台的消息类型、模板、质量评级与限额档位：官方口径解读
@@SUBTITLE@@本篇是"WhatsApp 群发／群控五篇"中的第二篇（轴②），接在《WhatsApp 群发与群控的违规判定与处置阶梯》之后。轴①数的是**处罚**，本篇数的是**能力**：把 Meta 开发者文档、帮助中心与法律页里关于"一条消息属于哪一类、能长成什么样、要被几套状态与评级描述、能发给多少人、在什么条件下会被暂停或节流"的全部公开表述收在一处，逐条对上原句与出处。全篇只读官方文本，共引用 **88** 个页面登记位（**86** 页正文已取回、**2** 页未取回且全篇不引用，详见附表 A 的 **89** 行）：Meta 开发者文档 **78** 页、Meta 商业帮助中心 **6** 页、法律与政策页 **4** 页。核心发现五条：**第一，"模板"这一个对象同时被四套枚举描述**——API 侧的 `status`、WhatsApp Manager 的 **9** 个状态、帮助中心的 **5** 个状态、API 侧的 **4** 个 `quality_score` 与 Manager 的 **4** 个质量标签，四套之间官方从未给出对齐表，本篇逐套归档（附表 D 共 **23** 组 **134** 个取值）。**第二，限额的主语已经不是号码**：**2025 年 10 月 7 日**起 messaging limit 按 business portfolio 计算并共享，官方自己写了 `it's possible for one number to consume all of the portfolio's messaging capability within a given period`。**第三，官方文本里有三种互不相同的"停"**：模板暂停（**3 小时／6 小时／Disabled**）、模板节流（阈值官方写作 `unspecified`，实用模板另有 **7 天**条件窗）、组合级节流（**500K** 条与 **365** 天回看，超额回 `135000`），三者的触发主语、时长、错误码与后果封锁范围全不一样。**第四，"质量"在两个层级上是两个数**：号码层是过去 **7** 天且 `weighted by recency`，模板层是滚动 **24** 小时，两处都现行、官方没给对照句。**第五，群发的规模上限不只长在发送方**：逐用户营销限额由收件人的阅读率与收件箱负载决定，且美国号码的营销模板消息`WhatsApp does not currently deliver`——也就是说"能发多少"这件事在官方文本里同时受**限额档位、吞吐、速率、逐用户限额、质量与节流**六道闸约束，本篇逐道拆开（第 11 章）。全篇不涉及任何批量发送脚本、账号池、多号轮换、限速规避、检测与反垃圾绕过，也不提供模板内容的代写或规避审核的建议。
@@INFO@@报告日期|2026 年 11 月 8 日
@@INFO@@分析对象|消息平面层：Send messages（客服窗口与 **16** 类窗口内消息）、About the WhatsApp Business Platform（三类限额与速率总则）、Get opt-in、Text／Interactive／Address／Location／Reaction 等 **14** 个消息类型页。**模板层**：templates 下的 overview、components、supported-languages、template-review、template-management、template-archival、template-groups、template-migration、template-comparison、template-media、template-library、time-to-live、template-quality、template-pausing、template-pacing、business-portfolio-pacing、template-categorization 与 messaging-limits、upcoming-messaging-limits-changes、throughput；marketing-templates 及其 5 个子页、utility-templates 及其 4 个子页（其中 **2** 页正文未取回）、authentication-templates 及其 7 个子页。**限额与治理层**：per-user marketing limits、block-users、analytics、pricing 与 conversation-based-pricing、non-template-messages。**接入面与留存层**：marketing-messages 的 overview／features／get-started／send／pricing／onboarding／view-metrics／measure-conversion／deep-links 共 **9** 页，local-storage、no-storage、data-privacy-and-security、business-phone-numbers/media、identity-change、permissions、access-tokens。以上 Meta 层合计 **78** 页。**帮助中心层 6 页**：号码质量与 messaging limit（896873687365001）、模板质量状态（766346674749731）、商业资产验证（2058515294227817）、身份验证（587323819101032）、显示名（486932075688253）、模板有效期（1305007343713790）。**法律与政策层 4 页**：business.whatsapp.com/policy、Messaging Guidelines、WhatsApp Terms for WhatsApp Business Platform、Terms of Service。四类相加 **88** 个登记位（对照：本篇与轴①不共用页面口径，附表 A 只登记轴②取到的页面）。
@@INFO@@视角定位|官方规则文本的口径解读，属于**平台能力的公开文档梳理**，不是操作指南。本篇**不提供**批量发送脚本、发送自动化搭建、账号池、多号轮换、设备农场、限速或频控规避、垃圾信息检测绕过、模板规避审核的写法建议，也不提供任何针对他人设备或他人账号的操作步骤与时序；**不提供**任何可用于向非自愿收件人大量投递消息的方法。文中所有档位、时长、错误码、枚举与措辞均取自官方公开页面，引用目的只有两个：**（1）说明一类消息在什么窗口里才允许发、一个模板最多能长成什么样；（2）说明投递规模的上限由哪些官方已写明的机制共同决定、这些机制在什么条件下会暂停或丢弃消息**。凡引号内的英文句子均为**官方页面原文**，出处紧跟其后；官方未写明处一律标注"官方未写明"，不推测补齐，也不以其他平台惯例代替——本篇共登记 **3** 项官方留白与 **14** 项口径差异（附表 F 共 **17** 项）。本篇不构成对 WhatsApp、Meta 计费或质量评级一致性的评价。
@@INFO@@分类|**官方页面登记清单 88 个登记位 × 站点 × 引用位置 × 取回状态**（附表 A，**89** 行含合计；站点为 Meta 开发者文档 78、帮助中心 6、法律与政策 4，**未取回 2 页**）**/ 数字归档五档 × 137 个条目**（附表 B，**138** 行含合计，档位：**限额与规模档位 24／模板构成上限 21／时长与滚动窗口 41／生效日期与版本节点 28／速率、比率与容量口径 23**，按字面值去重 **90** 个、计数单位 **56** 种）**/ 档位逐档表**（附表 C，**59** 行含合计，**16** 个体系：限额档位 8、模板状态 5、消息有效期 5、号码质量评级 4、模板暂停实例 4、类别误用阶梯 4、逐用户营销限额 4、吞吐档位 3、客服窗口 3、模板归档 3、模板数量档位 3、模板编辑窗口 3、模板节流 3、应用请求速率 2、模板删除 2、模板质量评级 2；时长列 **22** 档官方未写明）**/ 官方文档枚举取值域全表**（附表 D，**135** 行含合计，**23** 组 **134** 个取值，来自 **16** 个页面：模板分析指标 30、窗口内消息类型 16、顶部屏蔽原因 9、Manager 模板状态 9、投递失败错误码 7、误用 webhook 事件 5、帮助中心模板状态 5，四个取值的组 **8** 个、三个取值的组 **5** 个、两个取值的组 **3** 个）**/ 认证类、实用类、营销类模板逐项对照**（附表 E，**11** 行含合计，**10** 个维度：前 **3** 行由解析脚本从 TTL 对照表直接取出、其余 **7** 行为手定，**6** 格官方未按类别区分）**/ 页面之间的口径冲突与官方未写明清单**（附表 F，**18** 行含合计，**17** 项，其中 **3** 项一侧为官方主动留白、**14** 项为两处或两页措辞不一致）
@@INFO@@难度|进阶（本篇的难点有四处。**第一是"同一个词挂在不同的主语上"**：`limit` 在限额页的主语是 business portfolio、在帮助中心的旧写法里是 business phone number、在速率总则里是 app 与 Messaging account 之间的请求数、在逐用户页里主语是收件人，四种 limit 的计量单位互不相通。**第二是"状态"与"评级"是两套并行的词表**：Manager 的 `Active - Low Quality` 与 API 的 `RED` 说的可能是同一件事，但 `Paused`、`Disabled`、`PENDING_DELETION`、`In-Review` 分属状态而非评级，本篇把四套枚举分列不合并。**第三是官方主动留白**：模板节流的触发阈值写作 `an unspecified threshold`、组合级节流写明仍在灰度发布、限速一级的具体量在类别误用阶梯里没有数字，三处只记机制不推数值。**第四是同一判据有两种写法**：自动扩档的利用率门槛在开发者文档里是 `at least half`、在帮助中心里是 `More than 50%`，本篇按两处独立出处并列（附表 F），不合并成第三句。）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

"能不能给十万人发同一条消息"这类问题，在公开渠道里通常只得到两个答案：条款要求先取得 opt-in，以及"有数量限制"。但官方文本实际给出的是远为细化的东西——一个消息平面模型、一套模板构成上限、四套并行的状态与评级枚举、一串限额档位和三套彼此独立的"停下来"机制。本篇把这些**能力层**内容按官方原句逐条登记，并回答十二个问题。

1. 什么消息能在客服窗口外发？——**只有已批准的模板消息**。原文为 `Template messages are the only type of message that can be sent to WhatsApp users outside of a` 客服窗口（`whatsapp_messages_send_messages_md` 与 `whatsapp_templates_overview_md` 两处都有该句），而同页另一句直接点出它与群发的关系：`Templates are commonly used when messaging users in bulk`（第 2.1、2.5 节）。
2. 窗口内有几类消息？——官方在 Send messages 页列了 **16** 类服务消息，本篇逐类登记在附表 D（第 2.4 节）。
3. 一个模板最多能长成什么样？——头部 **60** 字符、正文 **1024** 字符、页脚 **60** 字符、名称 **512** 字符、按钮 **10** 个、网址按钮 **2** 个、按钮标签 **25** 字符、轮播 **10** 张卡片、列表 **10** 行、GIF **3.5** MB（第 3 章，附表 B 第二档 **21** 行）。
4. 模板有几个状态、几套状态？——API 一套、WhatsApp Manager **9** 个、帮助中心 **5** 个、质量标签 **4** 个、质量分 **4** 个值；四套之间**没有**官方对齐表（第 4 章，附表 D）。
5. 营销、实用、认证是怎么判的？——三条定义句加两张目标表与一张 **14** 个用例的"必需或关键"表；营销是兜底类：`Templates with mixed content` 与 `Templates where contents are unclear` 都被归为营销（第 5 章）。
6. 类别会被改判吗？——会。**2025 年 4 月 9 日**起批准的实用模板默认按营销计费，**2025 年 4 月 16 日**起被警告过的业务不再有 **24** 小时预告；误用处置是一条四档阶梯，其中限速一档的容量数字官方未写明（第 6 章，附表 C 共 **4** 档）。
7. 质量有几个观察窗？——**两个**：号码层 `over the past seven days, weighted by recency`，模板层 `in a rolling window of the past 24 hours`，两处措辞都现行且分属两个主语（第 7 章、13.3 节）。
8. 一条消息能被"停"几次？——三套机制：**暂停**（第 1／2／3 次分别是 **3 小时／6 小时／Disabled**）、**模板节流**（阈值 `unspecified`，实用模板另有 **7 天**窗口，被扣消息丢弃并回 `132015`）、**组合级节流**（**500K** 条／**365** 天，超额回 `135000`，并可能`prevented from sending or creating templates`）（第 8 章）。
9. 收件人一侧有没有闸？——有。**逐用户营销限额**由阅读率与收件箱负载自适应决定，且营销模板消息当前**不投递到美国号码**；同一账号反复重发会让该消息对该用户`unavailable for up to 24 hours`并回 `131049`（第 9 章）。
10. 限额档位有几档、怎么升？——起点 **250**，晋级目标 **2,000／10,000／100,000／Unlimited** 共 **4** 档；升到 2,000 走三条路径之一，之后自动扩档要求"高质量消息"加**过去 7 天用到当前限额的一半以上**，满足后 **6 小时**内上调一级（第 10 章，附表 C 共 **8** 档）。
11. 一次发送要过几道速率闸？——本篇按官方文本拆出**六道**：messaging limit、吞吐（默认 **80** mps／自动升级 **1,000** mps／双用途号码固定 **20** mps）、同一收件人速率（**6** 秒 **1** 条）、应用请求速率（**200**／**5,000** 次每小时）、逐用户限额、模板与组合级节流（第 11 章）。
12. 官方没写什么？——第 13.2 节列 **3** 项主动留白，附表 F 共 **17** 项差异，包括节流的触发阈值、组合级节流的适用范围、限速一级的容量数字、`at least half` 与 `More than 50%` 的两种写法、起点 250 是否算档位等。

一句话结论先给：**在官方文本里，"能发多少"从来不是一个数，而是六道彼此独立、主语各不相通的闸；而"发出去的东西长什么样、算哪一类、被哪套状态描述"则由四套并行枚举共同决定——四套之间没有对齐表，三套"停"之间也没有。**

# 1 来源层、术语与本篇不做的事

## 1.1 三类来源与各自的语言

本篇只用三类官方文本，它们的措辞风格不同，不能混引。全篇的页面登记、站点分布与取回状态逐行记在**附表 A**（**88** 个登记位）。

**第一类是 Meta 商业平台开发者文档**（`developers.facebook.com`，本篇 **78** 页）。这是**规格语言**：有枚举、有表、有错误码、有 payload 示例。限额档位、模板构成、四套状态枚举、三种节流与暂停、吞吐与速率、计费类别、分析字段全在这里。本篇绝大多数原句出自这一类。

**第二类是 Meta 商业帮助中心**（`facebook.com/business/help`，本篇 **6** 页，逐篇以文章 ID 引用）。这是**界面语言**：讲"你在 WhatsApp Manager 里看到什么标签、鼠标悬停能看到什么"。它给出的质量标签与模板状态与开发者文档**用词不同**，本篇分列不合并（第 4.3、7.3—7.5 节）。帮助中心页**一律不显示日期字段**，因此附表 A 不设日期列。

**第三类是法律与政策页**（本篇 **4** 页：商业消息政策、消息指南、Business Platform 服务条款、通用服务条款）。这是**合同语言**：opt-in 的义务、`bulk`／`automated` 的定性、类别与内容限制的原则都写在这里。它不给出档位、时长与错误码，本篇也不从它推任何数字。

**未取回的 2 页**：`whatsapp_templates_utility_templates_booking_appointment_templates_md` 与 `whatsapp_templates_utility_templates_location_request_templates_md`。这两页正文本次未能取回，**其内容在本篇任何位置都没有被引用**，只在附表 A 逐行标注其状态。第 3.7 节在列举实用模板子页时会点到它们的名字，但只作为"该页存在且未取回"的登记，不转述其内容。

## 1.2 术语对照：中文口径与官方英文

本篇中文一律对应下表左列，英文原词一律放进引号或反引号，不做二次意译。

**客服窗口**对应 `customer service window`；**窗口外**对应 `outside of a` 客服窗口的写法。**消息账号**对应 `Messaging account`；**业务号码**对应 `business phone number`；**商业资产组合**对应 `business portfolio`；**商务套件**对应 `Meta Business Suite`。**模板**对应 `template`／`message template`；**已批准**对应 `APPROVED`。**营销／实用／认证**分别对应 `Marketing`／`Utility`／`Authentication` 三个类别词，**服务**对应 `Service`，但服务**不是**模板类别（第 5.5 节）。

**限额档位**对应 `messaging limit`，其四个晋级目标在附表 D 里作为一组取值登记。**吞吐**对应 `throughput`（单位 mps）；**速率限制**对应 `rate limit`；**暂停**对应 `pausing`；**节流**对应 `pacing`；**组合级节流**对应 `business portfolio pacing`。**质量评级**在 Manager 里是 `quality rating`、在 API 里是 `quality_score`；**号码质量**在帮助中心里是 `quality rating` 的 Green／Yellow／Red 三档。

三个中文词要特别交代：**"停"**在本篇是三套机制的合称，不是官方词；**"档位"**对应 `limit` 的具体取值与 `TIER_` 前缀字段值；**"闸门"**是本篇为叙述方便造的比喻，官方文本没有把六道上限写成"闸门"，第 11.1 节会说明这六道各自的官方名字。

## 1.3 主语层级：谁的限额、谁的状态、谁的评级

官方文本没有画过一张层级图，但每一句规则都挂在某一层主语上。本篇按原句把四层钉死：

**第一层是业务号码（`business phone number`）**。吞吐挂在它上面（`For each registered business phone number, Cloud API supports up to 80 messages per second (mps) by default`）；本地存储与 No Storage 的设置项挂在它上面；旧写法里的消息限额也挂在它上面——限额页顶部就有一条弃用警告说明 `messaging_limit_tier` 字段已被弃用，改取组合级字段。

**第二层是消息账号（`Messaging account`）**。模板数量上限挂在它上面：`If a parent business portfolio is unverified, each of its Messaging accounts is limited to 250 message templates`；应用请求速率挂在 app 与 Messaging account 的组合上；类别误用阶梯的限速与实用模板禁令也挂在它上面。

**第三层是商业资产组合（`business portfolio`）**。这是本篇最关键的一层，因为限额**现在**挂在它上面：`Messaging limits are calculated and set at the business portfolio level and are shared by all business phone numbers within a portfolio`，同页紧接着写明后果——`it's possible for one number to consume all of the portfolio's messaging capability within a given period`。自动扩档的判据、组合级节流的适用对象、吞吐升级的资格条件（`The business portfolio associated with the phone number must have an unlimited` 限额）也都在这一层。

**第四层是 Meta Business Suite**。组合级误用处置的最高一档挂在它上面：同一套件下多个消息账号持续误用会触发跨全部账号的处置（第 6.5 节）。

**层级之间没有官方映射句**。本篇因此遵守三条硬规矩：**不把跨层数字连用**（例如不把组合级限额与号码级吞吐相乘）、**不把跨层时长相互推断**（例如不因组合级 **6 小时**上调推出号码级 **6 小时**生效）、**不把跨层枚举对齐**（Manager 状态与 API 状态各自登记）。这条层级表同时是附表 C 的"体系"列的分组依据。

## 1.4 本篇不做的事

本篇**不**提供批量发送脚本、发送自动化搭建、账号池与多号轮换、设备农场、限速或频控规避、检测与反垃圾绕过、模板规避审核的写法建议，**不**提供申诉话术模板，**不**评价平台裁量的公正性。凡官方写成区间、多值或主动标注 unspecified 之处，本篇原样保留并登记为留白：**不给单值、不推测阈值、不补齐缺失**。

# 2 客服窗口与两类消息平面

## 2.1 一个平面模型：窗口内自由、窗口外模板

官方在两个不同页面写了同一句边界，这是本篇全部叙述的地基。Send messages 页写窗口内的服务消息 `Service messages can only be sent via the Messages API`，紧接着给出窗口外的替代方案；templates overview 页则写 `Template messages are the only type of message that can be sent to WhatsApp users outside of a` 客服窗口。两句合起来构成一个**两平面模型**：

- **窗口内平面**：内容不需要预先批准，`you can compose and send them as needed in response to a WhatsApp user's message or call`。
- **窗口外平面**：内容必须是已批准的模板消息，且其状态必须是 `APPROVED`。

这个模型对"群发"的直接含义是：**向没有开启窗口的用户发消息，唯一通道是模板**，而模板要走审核、要被归类、要有质量评级。官方在同页把这一件事和规模并列写出：`Templates are commonly used when messaging users in bulk or when no customer service window is open between you and the user`。

## 2.2 窗口的开启与 24 小时

窗口的定义句是：`When a WhatsApp user messages you or` 呼叫你，`a 24-hour timer called a customer service window starts`。两个触发事件在官方文本里是并列的：**入站消息**与**来电**。

窗口时长是一个**定值**而不是区间：**24 小时**，本篇在附表 B 第三档按"硬时长（定值）"登记，并在附表 C"客服窗口"体系里按"开启／重置／服务会话免费"三档分别登记（该体系共 **3** 档）。

## 2.3 重置、关闭与一个官方已知问题

重置规则：`If the user messages or calls you again before the timer expires, the timer resets to 24 hours`。也就是说计时器不是滑动窗口而是**被入站事件拉回起点**；官方没写"最多能重置几次"，本篇记为未写明（附表 F 之外的一处留白，本篇不外推）。

窗口关闭后的边界：官方写法是你**只能**发已批准模板消息。**这里有一个官方自己承认的例外**：Send messages 页有一段 `**Known issue:**` 提示，原文是 `In rare cases, you may receive a message from a WhatsApp user but be unable to respond within the customer service window`。本篇把这条按原样登记，不解释成因，也不推断此时能否用模板消息代替。

## 2.4 窗口内可发的 16 类消息

官方用一个小节列出窗口内可发的服务消息类型，引言句是 `You can send the following types of service messages during an open customer service window`。本篇用解析脚本把这一节的**项目符号逐条取出**，得到 **16** 类，登记在附表 D"窗口内消息类型"组（**16** 项，每组一行，说明列是官方该条的引言原句），分别是地址、音频、通讯录、文档、图片、CTA 网址按钮、语音通话、Flow、列表、位置请求、快捷回复按钮、位置、贴纸、文本、视频与回应（reaction）。

这 **16** 类里有三类要单独标注，因为它们各自带自己的构成上限：**列表消息**的行数上限是 **10** 行（第 3.3 节）、**快捷回复按钮**是 **3** 个预定义回复（该句写作 `send up to three predefined replies for users to choose from`）、**轮播类互动消息**在互动消息页的限制不是单值而是区间，官方句为 `Messages must include between 2 and 10 cards`，即 **2—10** 张卡片（第 3.3 节）。这三处上限的出处、口径与模板侧的对应上限**分属两个层级**（模板层与消息层），本篇在附表 B 第二档为此开 **4** 行、不相加：模板层的轮播模板 **10** 张卡片、消息层的互动媒体轮播 **2—10** 张卡片、消息层的列表 **10** 行、消息层的 **3** 个预定义回复。

## 2.5 窗口与计费类别的交叉

旧的按对话计价页留下了一句仍然被广泛引用的边界：`Marketing, utility, and authentication conversations can only be opened with template messages`，紧接着 `Service conversations can be opened with any type of message other than a template message`。

这句话在 2025 年 7 月 1 日之后**不再是计费规则**——同一页顶部写明该文档已被按条计价取代；但它对**消息平面**仍然有效，因为它把"谁能开窗口外的会话"这件事写成了类别属性。本篇因此把它当作**平面描述**引用，而不是当作计费依据；计费部分见第 12.6—12.7 节。

## 2.6 opt-in 前置：窗口不等于许可

法律与政策层给的是另一件事：`Businesses are required to obtain opt-in before messaging people on WhatsApp`。该页同时说明 opt-in 可以是"general"的、不必专门针对 WhatsApp，但须符合当地法律。

这一句与第 2.1 节的平面模型是**两根独立的轴**：平面模型说的是"技术上允许发什么"，opt-in 说的是"允不允许向这个人发"。官方文本没有把两者写成同一个条件，本篇也不合并。窗口内可发**不等于**可以不取得 opt-in 就发；反之，取得 opt-in 也不会让窗口外出现模板之外的通道。

## 2.7 窗口内的质量信号

窗口关闭与消息质量是同一页上的两节，但质量一节的主语是"你的消息"而不是"窗口"。官方定义句：`WhatsApp determines message quality from how WhatsApp users have received your messages over the past seven days, weighted by recency`，依据的信号是 `user feedback signals such as blocks, reports, mutes, archives` 以及用户屏蔽时给出的理由。

这段里有三个本篇严格照抄的点：观察期是 **7** 天、有**按近期加权**这一限定、信号清单是四项加"屏蔽理由"。第四点与第 7.5 节帮助中心给出的 **5** 个屏蔽原因构成对照——两处清单**不完全一致**，本篇分别登记（附表 F）。

## 2.8 窗口与限额的口径接缝

限额的定义句里有一句常被略过的排除条件：messaging limit 数的是 `outside of a` 客服窗口投递的**唯一号码数**，时间口径是 `within a moving 24-hour period`。逐用户营销限额页同样写明：用户回复开启窗口后，`Marketing messages sent within this window do not count towards the limit`。

两处"不计"的机制不同但方向一致：**窗口内的事件在官方文本里被排除在两套限额之外**。本篇把这一点写成一句可核对的话：窗口既改变**能发什么**（第 2.1 节），也改变**被不计入什么**（本节）；它不改变**吞吐**与**速率**——吞吐页明确 `Throughput is inclusive of inbound and outbound messages and all message types`，即吞吐是**包含**窗口内消息的。六道闸之间的这条差别，是第 11.1 节总表的关键分列依据。

# 3 模板构成与创建上限

## 3.1 四类主组件与唯一的必填项

模板构成的定义句：`Templates are made up of four primary components which you define when you create a template`，四类即 `header`、`body`、`footer`、`buttons`；紧接着 `The only required component is the body component`。四个组件名在附表 D 里作为一组取值登记。

字符上限按组件分列，官方在每个组件的参数表里各写一次，本篇逐条开一行登记在附表 B 第二档：

- 头部文本：`Maximum 60 characters`，且 `Each template may include only one text header`。
- 正文文本：**1024** 字符。
- 页脚文本：**60** 字符。

这三处都是**硬数量上限**，官方写成 `Maximum`，本篇不改写成"建议"。另需注意：文本头部不使用 Markdown 特殊字符，该句是组件页的禁令而非上限，本篇只登记不展开。

## 3.2 按钮形态与四个数字

按钮组件的上限分散在几处，本篇按"谁决定这个数"分类登记：

- 按钮总数 **10** 个（硬数量上限）。
- 快捷回复 **10** 个（硬数量上限），与第 2.4 节消息层的"3 个预定义回复"分属两层。
- 网址按钮 **2** 个（硬数量上限），其示例 URL 上限 **2,000** 字符。
- 按钮标签 **25** 字符（硬数量上限）。
- 复制码按钮的示例码 **20** 字符。

另有一条**条件触发项**：按钮数超过 **4** 个时，官方写桌面端可能不可视——这一项的性质不是"不能超过"，而是"超过后在某端看不到"，本篇在附表 B 里按"条件触发项"登记，与上面的硬上限分列。

认证模板的 OTP 按钮（autofill）与复制码按钮是两种**按钮形态**，各有自己的页面；两者的差异在本篇只登记形态，不比较优劣。

## 3.3 富媒体形态与两个 10

轮播与列表各有两套页面上的 **10**：营销模板侧的轮播模板**卡片数**上限 **10**，互动消息侧的列表消息**行数**上限 **10**。附表 B 里这两行的计数单位不同（"张卡片"与"行"），因此开两行；它们是**同数不同集**的一个典型例子——本篇不因两者都是 10 就把它们写成同一条规则。

轮播在消息侧还有第三种写法：互动媒体轮播消息的卡片数不写单值而写区间，官方句为 `Messages must include between 2 and 10 cards`。本篇按"区间或多值"性质为它单独开第 **3** 行，与模板侧那条单值上限不合并——因此本节标题里的"两个 **10**"只指两个**单值**上限，不包括这条区间。加上第 2.4 节那句 **3** 个预定义回复，本节这四处构成上限——模板侧轮播 **10** 张卡片、消息侧轮播 **2—10** 张卡片、消息侧列表 **10** 行、消息侧回复按钮 **3** 个——在附表 B 第二档各占一行，共 **4** 行。

限时优惠（LTO）与优惠券是营销模板的两个子形态，各自有页；商品消息模板的上限是**商品 30 个、分组 10 个**，本篇同样按硬数量上限登记。GIF 文件大小上限 **3.5** MB 属于媒体侧上限，见第 3.6 节。

## 3.4 命名、参数格式与语言计数

名称规则是一条**格式约束加长度约束**：`Template names are limited to a maximum of 512 characters, consisting of lowercase alphanumeric characters and underscores`。也就是说名称长度上限 **512** 是**字符**、且只允许小写字母数字与下划线；这与头部／正文／页脚的上限（**60**／**1024**／**60**）不在同一层——名称上限在模板对象层，其余在组件层。附表 B 把两处都放在第二档，但"数的是什么"列分别写清。

参数格式两种：`named` 与 `positional`。官方的默认值句是：不指定格式时按 `positional` 匹配；命名参数须是"小写字母与下划线组成、包在双花括号里的唯一字符串"，原文为 `Parameters using the named format must be unique, single strings, composed of lowercase characters and underscores, wrapped in double curly brackets`。两个格式名在附表 D 作为一组取值登记。

语言计数是一条容易被当成"免费"的规则：官方写明每种语言的模板都算一个模板，因此支持语言表的语言数（本篇按该表行数登记为 **111** 种）与模板总数上限是**两个方向的力**——一个决定你能建多少，一个决定每种语言都要占一格。附表 B 里 **111** 一行的计数单位是"种语言"，性质列记"硬数量上限"，出处为语言表页。

## 3.5 创建速率与模板数量档位

三条上限挂在两个不同主语上，这是本节的关键：

- **创建速率**：`You can create a maximum of 100 templates in a Messaging account per hour`——主语是消息账号，单位是"个模板每小时"。
- **未验证组合**：每个消息账号 **250** 个模板（原文见第 1.3 节第二层）。
- **已验证且至少一个号码有已批准显示名**：`each of its Messaging accounts can have up to 6,000 templates`——这一条是**条件触发项**，因为 **6,000** 需要两个条件同时成立（组合已验证 **且** 至少一个号码有已批准显示名）。

本篇把这三行登记在附表 B 第一档与附表 C"模板数量档位"体系（共 **3** 档），并且**不把 250 与 6,000 写成"升级路径"**：官方没有把它们与限额档位放在一起叙述，两套 250（一个是模板数、一个是限额起点）也因此是**同数不同集**——附表 B 里数字字段为 **250** 的正是这两行，单位分别是"个模板"与"个/日"。

## 3.6 媒体头、文件大小与落地页

媒体头的格式占位符只允许四个取值，即 `IMAGE`、`VIDEO`、`GIF`、`DOCUMENT`；这四个取值在附表 D 作为"媒体头格式"组登记。媒体大小上限方面，GIF 为 **3.5** MB（硬数量上限）。

落地页相关有两页：网址按钮的标题覆盖（tap target URL title override）与业务号码媒体上传。**媒体上传侧的一条留存规则要单列**：启用本地存储的号码，其上传媒体`Uploaded media are accessible for 30 days`——这一句同时出现在存储页，本篇把 **30** 天登记在第三档（时长）与第五档（容量口径）中**更贴近的一句**所在的档，并在第 12.5 节复述该句但**只引用一次数字**，避免同一数字在两处被当成两个不同的量。

## 3.7 三类模板各自的形态清单

营销、实用、认证三类在 templates 下各有一组子页。本篇按"页面是否存在、是否取回"登记，不评价其内容：

- **营销类**：概览页加限时优惠、媒体卡片轮播、优惠券、自定义营销、通话权限请求共 **5** 个子页。
- **实用类**：概览页加订位、位置请求、位置、通话权限请求共 **4** 个子页；其中**订位与位置请求两页正文本次未取回**，其内容在本篇未被引用（附表 A 逐行标注）。
- **认证类**：概览页加最佳实践、autofill 按钮、复制码按钮、zero-tap、批量管理、错误信号、键盘建议共 **7** 个子页，另有模板预览页。

这一清单的唯一用途是让读者知道**官方把哪些形态归到哪一类**——归类的规则文本在第 5 章，不在这里。第 5.4 节会用到认证类子页里的多条硬条件。

# 4 审核、状态与四套枚举

## 4.1 机审、时长与"信息不外传"那句

模板审核的机制句：`Cloud API reviews templates and variable parameters using machine learning`，同段还写明 `no information is shared with WhatsApp`。这两句一起构成本篇对"审核是什么"的全部表述——官方说的是**云端接口用机器学习审核**，并且**不与 WhatsApp 共享信息**；本篇不引申成"自动审核一定通过／一定拒绝"，也不猜测模型。

决策时长的官方写法是上限而非区间：`It can take up to 24 hours for an approval decision to be made`。这句话在三个页面以三种形式出现（审核页、模板概览页的 Manager 状态说明、帮助中心的申诉复核），本篇按**三处独立出处**登记，第 13.1 节会说明为什么同值要开多行。审核通过后模板的状态被置为 `Active - Quality pending`，即**评级尚待产生**。

## 4.2 API 侧状态与 Manager 侧九态

API 侧最硬的一句是：`Templates must have a status of` `APPROVED` 才能发送；另一句是质量页的 `If the status changes to anything other than` `APPROVED`，模板就不能发送。两句合起来给出一个可核对的不变式：**发送权限只由 `APPROVED` 这一个值决定**，其余状态一律不能发。

WhatsApp Manager 侧则是**九个**状态标签（附表 D"Manager 模板状态"组，**9** 项），其中四个带 `Active -` 前缀的质量态、一个 `In-Review`、一个 `Rejected`、一个 `Paused`、一个 `Disabled`、一个 `Appeal Requested`。九态里 `In-Review` 的官方说明带出第 4.1 节的时长句（`Review can take up to 24 hours`），而 `Active - Medium Quality` 与 `Active - Low Quality` 的说明都明确**仍可发送**、但前者"might soon become paused or disabled"。

这两套之间的对应关系，官方只在概览页给了一句界面描述：Manager 面板显示状态并**为已批准（active）模板追加质量评级**。也就是说**评级是附加在状态之上的第二层标注**，不是状态本身。本篇按此把"状态枚举"与"评级枚举"分列两组，不做合并。

## 4.3 帮助中心五态与两套标签的错位

帮助中心（`766346674749731`）给的是**第五套**词表：**5** 个模板状态 `Pending review`／`Approved`／`Rejected`／`Flagged`／`Disabled`（附表 D"帮助中心模板状态"组，**5** 项）。它与 Manager 九态、API 的 `APPROVED`／`PAUSED`／`PENDING_DELETION` 等值都不重合，尤其是 `Flagged` 这个状态在 Manager 九态里**不存在**。

`Flagged` 的恢复条件是：`If the quality rating improves to a high or medium for 7 continuous days, the status will return to Approved`；而 `Disabled` 的条件是同页另一句"isn't improved within **7 days**"。两句在同页、都是 **7** 天，但一为"连续 7 天转好"、一为"7 天内未改善"，本篇按同页更完整的写法叙述（附表 F 登记为"同页两种表述"）。

同一页还有一句历史包袱：`Flagged` 是质量评级低时的警告态，而号码层的 Flagged／Restricted 两种状态已在 **2025 年 10 月 7 日**被帮助中心另一页宣布停用。**模板层的 Flagged 与号码层的 Flagged 不是同一个对象**，本篇在第 7.3 节与第 10.10 节分别处理，不互相推导。

## 4.4 拒绝原因与申诉的硬条件

审核页列出的拒绝原因分五组，本篇按其分组登记：变量参数格式问题、Commerce 政策问题、Business 政策问题、重复模板、其他政策问题。其中"重复"一项有一条官方例外：该规则**不适用于** `AUTHENTICATION` 类别。

申诉的硬条件是两句：**须附样本**（`appeals must include a sample`）与**决策时限**（第 8.4 节的 **24** 小时）。这两条分别出现在审核页与暂停页，本篇把"须附样本"当作申诉的**内容条件**、把 **24** 小时当作**时限条件**，分列在附表 C 的两个不同体系里（模板状态与模板暂停实例），不相加。

## 4.5 模板预览与"看到的不是发出去的"

认证模板预览页描述的是**在接口内预览模板长什么样**。本篇只登记其存在与被引用位置，不从中推出任何投递行为。之所以仍要登记，是因为它容易与"已批准模板"混淆：预览不代表可发，可发的条件只有第 4.2 节的 `APPROVED`。

## 4.6 模板组：两个新的字符上限

模板组是挂在模板之上的组织单位，其两个字段上限分别是名称 **256** 字符与描述 **512** 字符（附表 B 第二档）。这里的 **512** 与模板名上限 **512**（第 3.4 节）**同数不同集**：一个是模板名的长度、一个是模板组描述的长度，本篇开两行、不合并。

模板组还有一个分析字段（`template_group_analytics`），其回看窗口与模板分析同为 **90** 天，见第 12.10 节。

## 4.7 认证模板的批量管理

认证类子页里有一页专门讲批量管理，它提供的是**批量创建与批量查询**这一类接口能力。本篇按第 1.4 节的边界，只登记"官方有此页、该页描述批量接口"，不写出步骤、不写出请求结构。它与第 11.9 节的单次请求上限是两件事：那一节记的是**数量上限**，这一节记的是**能力存在**。

# 5 类别指南与判定

## 5.1 三条定义句与四类的不对称

官方类别页给的是**三条**定义句，而不是四条：`Marketing templates` 用于`Enable businesses to achieve a wide range of goals, from generating awareness to driving sales and retargeting customers`；`Utility templates` 用于`Enable businesses to follow up on user actions or requests, since these messages are typically triggered by user actions`；`Authentication templates` 用于`Enable businesses to verify a user's identity, potentially at various steps of the customer journey`。三个类别名在附表 D"模板类别"组作为 **3** 个取值登记。

这三句在主语层级上是**模板**的类别，而计费侧的类别是**消息**的属性，并且多出一个服务类（第 12.6 节的 **5** 类）。两处口径不同但都现行，本篇分列两组（附表 D 的"模板类别"组与"会话计费类别"组，后者 **4** 项），并在第 13.3 节点明它们不是同一张表。

## 5.2 营销是兜底类

营销一节里最关键的不是它的定义句，而是**两条兜底规则**：`Templates with mixed content`（例如订单更新里带促销）与 `Templates where contents are unclear`（例如正文只有参数占位或"Congratulations!"）都被归为营销。这两条使营销成为**判定结果的余项**：凡不满足实用或认证的严格条件、或内容混／不清晰的，落到营销。

官方紧接着加了一条免责说明：示例仅供参考，相似内容可能因具体文本被判为不同类别。本篇按此**不把任何示例当作可复用的判定式**。法律与政策层对营销内容的原则性限制见第 8.9 节与政策页登记，本篇不从中推数字。

## 5.3 实用的双条件与两张表

实用类别的判定句要求**两条同时成立**：一是 `not containing any promotional or persuasive intent`，二是 `specific to or requested by the user` 或 `essential or critical` 二者之一。原文用 `_Must_` 与 `_Must_ ALSO be either` 强调这是**合取**而非择一。

第一条表是**目标表**：官方实用目标表有 **5** 行，即 opt-in 管理、订单管理、账户提醒或更新、反馈调查、在其他渠道开始而在 WhatsApp 继续对话。每行都带一句"不应推销、推荐、上销或交叉销售、不应包含优惠、不应争取续费"的限定。

第二条表是**用例表**：`For a utility template to be deemed essential or critical to the user`，须属于表中列出的用例之一，且同样非促销。该表本篇数得 **14** 个用例，归在 **5** 个类别名下（公共安全、公共服务、公共中断、账户或产品保护、法律与监管合规）。**这里有一个官方文本自身的小瑕疵**：`Warranty alerts` 这一用例在表中出现两次，分别在"账户或产品保护"与"法律与监管合规"两个类别下，两处示例文本相同。本篇把该重复按原样登记在第 13.3 节，不做去重后"纠正"官方。

## 5.4 认证的硬条件与四类附加形态

认证类别是四类里条件最硬的一档，官方写法是一串"必须／不得"：须使用模板库、须含 OTP 按钮、正文不得含网址、媒体或表情符号。其中一条量化限制是：`Parameters are also restricted to 15 characters`。这几条本篇逐句登记（附表 B 第二档的 **15** 字符一行）。

四项附加形态各有一页，其口径要点是：

- **autofill 按钮**：官方页描述其在支持的键盘上的行为，属形态说明。
- **复制码按钮**：示例码上限 **20** 字符（第 3.2 节已登记，出处不同处在此复述）。
- **zero-tap**：该页给出一个 **10** 分钟的握手窗口，本篇按"区间或多值"以外的定值登记。
- **键盘建议**：官方写明其默认开启与 **iOS 26** 这个系统版本节点相关。

这四页的描述都属"能力与形态"，不属"判定条件"。本篇因此把它们放在第 5.4 节末尾，与前面的硬条件分开——**避免让读者以为用了 autofill 就满足认证类别**，官方没有这样写过。

## 5.5 服务：不是模板类别

第 5.1 节说类别页只给三条定义句，原因就在这一节：**服务不是一种模板类别**。它是第 2.4 节窗口内消息在计费侧的名字。官方在计费页写明 **Service** 是"非模板消息且不由 Meta 商业智能体平台驱动"的那一类，与 `Marketing`／`Utility`／`Authentication` 三个模板类别并列但**来源不同**（模板 vs 非模板）。

本篇的处理方式：附表 D 的"会话计费类别"组按 **4** 个取值登记（含 Service），"模板类别"组按 **3** 个取值登记；两组**不相加**、不写成"四类模板"。

## 5.6 对比页与它的两个窗口

模板对比页把不同类别模板的能力排成矩阵。本篇从该页只取两类事实：**顶部屏蔽原因**清单与若干时长对照（**7**／**30**／**60**／**90** 天与 **1,000** 次），逐条登记在附表 B。

营销消息侧的指标页给的是阅读率与点击率的对照口径，与第 7.7 节的分析字段是两套东西：前者是产品页的说明，后者是接口字段。本篇把 **90** 天回看窗口归给分析接口，不把指标页的描述当作接口约束。

# 6 自动重判、复核与误用阶梯

## 6.1 2025 年 4 月 9 日：批准即按营销

该页写明一个日期节点：**Effective April 9, 2025**，被批准的实用模板可能直接以 `MARKETING` 类别生效，并且 `allow_category_change` 成为默认。这条规则的主语是**模板**，效果是**计费类别**，因此它同时属于本章与第 12.6 节；本篇在附表 B 第四档登记日期、在附表 C"类别误用阶梯"体系登记后果。

## 6.2 2025 年 4 月 16 日：预告被撤回

第二个节点：**Effective April 16, 2025**，对已被警告过的业务，官方撤回 **24** 小时预告——改判即时生效。该页的原文口径是"警告之后，实用转营销的改判即时生效且不再预告"（附表 D"类别误用处置档"组 `Warning` 一行的说明列即此句）。

这两个日期合起来构成一个可核对的结论：**2025 年 4 月之后，"实用被判成营销"在两处官方文本里既是默认行为、也可以无预告发生**。本篇只按这两句叙述，不给出"如何避免"的建议。

## 6.3 复核申请与最长 60 天

类别复核（`correct_category`）的时限句给出一个上限：复核周期`up to 60 days`。**60** 天在附表 B 第三档按"硬时长（定值）"登记，性质列写"硬时长"而不是"建议时长"，因为官方写的是周期上限。

该页另有两个复核周期节点（**2025 年 4 月 9 日**与 **2025 年 4 月 16 日**附近的两个日期），本篇按第四档逐日登记，**不合并成"4 月"**——两日在不同句子里各司其职。

## 6.4 三种匹配与"首日拒绝"

复核请求的结果句里有一条与计费周期绑定的规则：从营销或实用改为认证的路径，其拒绝时点在官方文本里写作**次月首日**。这条是四类判定里唯一把**日历边界**写进拒绝条件的规则，本篇按原句登记在附表 C 的时长列，不解释为何是首日。

`correct_category` 的三种匹配情形（成功、失败、需要更多信息）在该页各有一句，本篇登记其存在与措辞，不合并成"三种结果"这一简化说法。

## 6.5 四档误用阶梯

类别页给出的是**四档**处置阶梯，附表 D 的"类别误用处置档"组（**4** 项）与附表 C 的"类别误用阶梯"体系（**4** 档）分别登记其枚举与语义：

- **Warning**：`A written warning is sent to Messaging account admins`，并即时启动第 6.2 节的无预告改判。该档时长列官方写作 `Ongoing`。
- **Rate limiting**：`Utility message volume on the Messaging account is capped within a 24-hour rolling window`，`Messages exceeding the cap are rejected`。**这一档的容量数字官方没有写**——只写"封顶"，不写封在多少。本篇登记为官方留白（第 13.2 节第 3 项）。该档时长句为 `Minimum 7 days. Lifted once categorization quality improves`。
- **Utility restriction**：`All approved utility templates on the Messaging account are recategorized to` `MARKETING`，并禁止新建实用模板与提交类别复核。时长句为 **7** 天，重复违规 **30** 天。
- **Business portfolio level restriction**：主语升到套件层，`If continued misuse is detected` 于同一商务套件下多个消息账号时，跨全部账号执行上述处置。时长句为 **30** 天。

阶梯末尾还有一句关于**升级**的话：此前被限制之后再次检测到持续误用，处置可能`for longer periods and at a higher level`。本篇把这句登记为阶梯的**可再启用与加重**条款，不推断加重后的具体档位与天数。

## 6.6 五个 webhook 场景

同一页的 webhook 表把上述处置映射到事件名，本篇按解析脚本取到 **5** 个场景（附表 D"误用 webhook 事件"组，**5** 项）：`UTILITY_TEMPLATE_ABUSE` 在**两个**场景下复用（警告与实用模板停用），另三个是解除、限速与限速解除。

这一处是本篇刻意强调的细节：**同一事件名对应两种含义**，区分靠 `violation_type` 与 `restriction_type` 两个字段；并且官方写明警告与解除两类场景**不带** `restriction_info`。本篇因此不把 webhook 事件数当作处置档数——**事件 **5** 个、处置 **4** 档，两者不等**。

## 6.7 模板库的位置

模板库页描述的是认证模板所需的库。它与第 5.4 节"须使用模板库"这条硬条件是同一件事的两处表述，本篇只登记一次规则、两处出处，不重复计数。

# 7 质量评级

## 7.1 API 侧四个质量分

API 侧的取值是 **4** 个：`GREEN`、`YELLOW`、`RED`、`UNKNOWN`（附表 D"API 模板质量分"组）。四个定义句本篇逐句取用，其中两条的发送含义要并排读：

- `YELLOW`：`The template has received negative feedback from multiple WhatsApp users, or low read-rates`，并且`Message templates with this status can still be sent`。
- `RED`：同样可发，但`The template can be sent, but is in danger of being paused or disabled soon`。

新建模板的初始值是 `UNKNOWN`，官方句为 `Newly created templates have a quality score of` `UNKNOWN`，并由`WhatsApp updates the rating automatically as it collects usage, feedback, and engagement signals over time`。

## 7.2 评级与状态的接缝

质量页给出的接缝句是：评级会影响节流与暂停；`If the status changes to anything other than` `APPROVED`，`the template cannot be sent in template messages unless its` `APPROVED` `status is restored`。

这句话把第 4.2 节的不变式与第 8 章的暂停连起来：**评级本身不直接停发，是评级导致的"状态变化"停发**。本篇按此叙述，不写成"RED 就发不出去"——官方原文明确 RED 仍可发。

## 7.3 号码层：7 天与 30 天

帮助中心（`896873687365001`）给的是**号码**的质量：观察期`over the past 7 days`，三档 Green／Yellow／Red，并且质量历史可回看 **30** 天。该页还给出两条与限额相关的判据句（第 10.3、10.6 节使用）。

号码层在这里出现一个必须标注的**停用节点**：`The phone number statuses of Flagged and Restricted have been discontinued as of Oct 7th, 2025`。这条与第 4.3 节模板层仍在使用 `Flagged` 形成对照——**同名状态在两个层级上一个已停、一个仍在**，本篇把它登记在附表 F 的"页面更新不同步"项，并明确以**停用日期为准**叙述号码层。

## 7.4 模板层：24 小时滚动

模板层的质量标签在帮助中心的写法是`in a rolling window of the past 24 hours`，四档为 Green／Yellow／Red 加待评级。这与第 2.7 节号码层的"七日加权"是**两个不同的观察窗**，且都在现行文本里。

本篇采用第 13.3 节登记的写法：**号码质量按七日加权叙述、模板质量按 24 小时滚动叙述，两个数分别记，不合并成"某个滚动窗口"**。同时，第 7.3 节的 **30** 天质量历史与第 7.4 节的 **24** 小时窗口也**不是**同一根轴：一个是可回看的长度，一个是评级所用的窗口。

## 7.5 五个屏蔽原因与两套清单

帮助中心的号码质量页列出用户屏蔽时的 **5** 个原因（`No longer needed`／`Didn't sign up`／`Spam`／`Offensive messages`／`No reason`），本篇在附表 D 以"顶部屏蔽原因"组登记 **9** 个取值——**这里的 **9** 与 **5** 的差**，是因为附表 D 取的是枚举页面上的英文取值码（含 `NO_REASON_GIVEN`、`UNKNOWN_BLOCK_REASON`、`OTHER` 等接口侧写法），而 **5** 是帮助中心界面上呈现给用户的选项数。第 13.3 节把这一项登记为**层级不同**，不做并集。

而第 2.7 节开发者文档的信号清单（`blocks, reports, mutes, archives`）与这两套都不同：那是**质量计算输入**，不是**屏蔽理由枚举**。三处清单本篇分别登记，不合并成"负反馈清单"。

## 7.6 认证错误信号

认证错误信号页给出一组信号与一个 **2026 年 10 月 15 日**的节点。本篇只登记信号名（附表 D"error_signal 组"）与日期（附表 B 第四档），不解释信号成因，也不给出"如何避免触发"的建议——那属于第 1.4 节排除的内容。

## 7.7 分析与指标层

分析接口侧与质量有关的字段本篇登记三组：**指标类型**（`COST`／`CONVERSATION`）、**对话类别**（**4** 项）、**对话方向**（**3** 项）与**模板分析指标**（**30** 项，附表 D 最大的一组）。指标里 **25** 项带 `MM API for WhatsApp only` 标注，也就是说**模板分析指标的近五分之四只在营销消息接口侧可用**——这一条对"用什么口径衡量质量"影响很大，本篇把它写成可核对的比例而不是笼统的"部分指标"。

营销消息侧的指标页另给阅读率与点击率的对照，与上述接口字段**不是同一层**（第 5.6 节）。

# 8 三种"停"：暂停、节流与组合级节流

## 8.1 暂停的触发句与它的主语

暂停页的定义句把触发条件写成质量评级的最低档：`If a message template reaches the lowest quality rating`，并在同一句里给出两种写法——Manager 里的 `Active - Low quality` 与 API 里的 `RED`。这是本篇少见的、官方**自己给出两套词表对应关系**的句子，本篇因此在此处允许"同一个触发条件的两种写法"这一表述；其余枚举仍按分列处理。

同一句还给出暂停的目的：`to protect the quality rating of phone numbers that have used the template`——**保护的是号码的评级，而不是模板本身**。这一点决定了第 8.3 节的计费与限额叙述。

## 8.2 三档实例与两个时长

三档实例是官方逐条列出的：`1st Instance: Paused for 3 hours`、`2nd Instance: Paused for 6 hours`、`3rd Instance: Disabled`。本篇在附表 C"模板暂停实例"体系登记 **4** 档（三次实例加申诉），在附表 B 第三档登记 **3** 小时与 **6** 小时两个时长行，**不合并成"最长 6 小时"**——第三档不是时长而是状态。

第一次与第二次是**时长**，第三次是**终态**，这个不对称是官方文本本身的结构，本篇按原样保留。

## 8.3 暂停期间：不计费、不占限额、但请求仍被拒

这一节是本篇最容易读错的地方，官方把三件事写在同一句里：`Although you won't be charged for attempting to send a paused message template to a customer, and the attempt won't count against your messaging limit, the API will reject these attempts anyway`。

拆开即：**不计费**、**不占限额**、**但接口照拒**。本篇因此明确否掉一种常见误读：不能因为"不占限额"就认为暂停期间请求可以排队到窗口外执行——官方写的是拒绝。同页还给出配套建议（暂停期间应停掉依赖该模板的自动化活动），本篇只登记其存在，不展开为操作建议。

## 8.4 解除、编辑与 24 小时申诉

自动解除的官方句：`A template will unpause on its own after satisfying the pause duration outlined above`，解除后状态回到 `Active`，并且评级会按最近反馈重置。

手动解除有两条入口，官方写成 API 与 Manager 两种；其中一条与节流绑定的例外要单列：`templates paused during Template Pacing must be manually unpaused`——**被节流机制暂停的模板不会自动恢复**。这是三套"停"之间的一条真实接缝（第 8.6 与 8.8 节）。

编辑的代价句：`once you edit a message template and resubmit it for approval, its status will change to In Review`，且在重新批准前不能发送。申诉句是 `The appeal will be reviewed and a decision made within 24 hours`，与第 4.4 节的"须附样本"配套。

范围与日期：`Applies to businesses in Brazil, Colombia, and Singapore, starting September 12, 2023`，随后`Applies to all businesses starting October 12, 2023`。两行都登记在附表 B 第四档。

## 8.5 节流的定义与那个 unspecified

节流页的定义句：`Template pacing is a mechanism that allows time for customers to provide early feedback on templates`，且`Template pacing is valid for marketing and utility templates`——**认证模板不在此机制内**，这是官方明写的适用范围。

核心行为句里有一个官方主动留白：`When a template is paced, messages will be sent normally until an unspecified threshold is reached`。**触发阈值官方未公开**，因此本篇不写任何数字，只记三种走向：收到好信号则`scaled to the entire target audience`、收到坏信号则`dropped`、否则由内部护栏兜底释放（第 8.7 节）。这一项登记为附表 F 的"官方自陈未公开"。

## 8.6 实用模板的 7 天条件窗

实用侧的节流条件是条件式而非普适式：`Utility templates are subject to pacing only if you have had a utility template paused`。一旦有过一次实用模板被暂停，则新建的、解除暂停的、以及此前创建但没有 `GREEN` 评级的模板`are potentially subject to pacing for the next 7 days`。

这里的 **7** 天与第 7.3 节号码质量的 **7** 天、第 6.5 节限速档的 `Minimum 7 days` **是三个不同的 7 天**，分别数"节流有效窗"、"评级观察期"、"限速最短持续期"，附表 B 为此各开一行。第 13.1 节会给出完整的 **7**：单位是"天"的共 **5** 行，还要再加上帮助中心"连续 7 天回到已批准"与失败回调"7 天内重投"这两行；三行性质是硬时长、两行是滚动窗口。

被扣消息在坏信号下的结果句是：每条被丢弃并触发失败 webhook，`"code":"132015"`（本篇把 **132015** 作为同步侧与 webhook 侧共同指向的错误码登记）。同时管理员经商务套件通知、Manager 横幅与邮件三渠道被告知。

## 8.7 投递护栏：99 百分位与一小时

官方写明内部护栏的目标：`Our goal is that even if paced, campaign messages with highest throughput still get delivered within an hour (99 percentile)`。同段一句是这一条的落点：若护栏先于信号达成而被触发，`the held messages will be released normally`。

本篇对这一句的处理要交代清楚：它是**官方目标句**（`Our goal is`），不是承诺句，且限定在"最高吞吐的活动"。附表 B 把 **99 百分位**登记为"比率"性质、**1 小时**登记在第三档，两处都不写成"保证一小时内投完"。

## 8.8 组合级节流：500K、365 天与 135000

组合级节流的定义句与适用对象各一句：`Business portfolio pacing is a template message delivery batching mechanism`；适用条件是`business portfolios that have sent less than 500K template messages collectively, across all of their business phone numbers, within a moving 365-day lookback period`，以及正在被监测可疑活动的组合。

**这一条的适用对象容易被读反**：它适用于发送量**低于** **500K** 的组合（加被监测者），而不是高于。**500K** 与 **365** 天都登记在附表 B 第一档与第五档，性质列写明是**条件触发项**。

行为与后果：首批正常处理、后续被扣（`held_for_quality_assessment`）、随后`We will then deliver messages in batches, monitoring feedback before releasing each new batch`；若反馈指向可疑活动，剩余被扣消息全部丢弃，每条触发失败 webhook 且 `code` 为 **135000**。此外`the business portfolio will be prevented from sending or creating templates while it undergoes further review`，情节严重时可能被完全停用，同时`Portfolio admins will be notified of any enforcement actions and will have the option to appeal any decisions`。

与模板级节流的区别官方自己点明：`Note that business portfolio pacing is different from` 模板节流，后者只影响营销与实用模板。**主语层级因此不同**：模板节流的主语是模板，组合级节流的主语是商业资产组合。

## 8.9 灰度、可疑活动与政策页的接缝

组合级节流页顶部有一条发布状态警告：`This feature is being released gradually over the coming weeks so may not apply to you immediately`。**这是本篇登记的第二项官方留白**——机制的适用范围官方写明"可能暂不适用于你"，本篇因此不写任何生效日期，也不推断哪些组合已覆盖。

"可疑活动"的判定标准在该页只给出两个指向（商业消息政策与消息指南），本篇按第 1.1 节第三类来源登记这两个链接的存在，**不从中推导阈值**。政策执行侧的处置阶梯属轴①，本篇不重复；第 8.3 节的"不计费不占限额"与政策执行侧的"处罚"是两件事，本篇不合并叙述。

# 9 收件人一侧的闸：逐用户营销限额与美国号码

## 9.1 定义句的主语在收件人，不在发送方

这一页的标题本身就是口径：`Per-user marketing template message limits`。定义句写的是可能性而非确定值——`WhatsApp may limit the number of marketing template messages a WhatsApp user receives from any business in a given period of time`，并且加了一个条件从句 `when they are less likely to be receptive and engage with those messages`。

本篇因此把这一章放在所有限制之后单独讲：**前面几章的闸都长在发送方（组合、号码、模板），这一道闸长在收件人身上**。它不由企业配置，也不由接口参数决定，官方文本里没有任何一处给出可查询、可设置的入口。

## 9.2 官方写明的两个输入因子

原文把判定依据写成 `WhatsApp determines this based on several factors, including`，随后列出两项：一是 `a dynamic view of an individual's recent marketing message read rate`，二是 `how many messages they currently have in their inbox from friends, family, and businesses`。

两处细节要按字面记账。**第一**，`several factors, including` 说明这两项是**举例而非穷举**，本篇不能写成"只看这两个因子"。**第二**，第二个因子的范围包含"朋友与家人"的消息——也就是说这一道闸把个人聊天与商家消息放在同一个收件箱负载里计算，这在商业平台的文本里是少见的一句。

## 9.3 自适应与那句"能力不变"

自适应句：`The per-user marketing limit adapts automatically over time based on a person's recent engagement levels`。

同一句后半是官方对企业的安抚表述：`While this adaptive limit may mean delivering fewer messages to some WhatsApp users during periods of lower marketing read rates or overall inbox activity, your ability to reach people when they are most engaged does not change`。

本篇对这句的处理要说明白：它没有给出任何数值、周期或档位，因此不能用来推断"高参与期"的定义。**该限额的具体数字官方全程未写**，登记为附表 C"逐用户营销限额"体系的未写明时长，与附表 F 的留白项。

## 9.4 怎么计数：只有投递成功才计数，窗口内不计数

计数规则页写得比多数限制条款都清楚：`Each marketing template message delivered counts towards the per-user marketing limit`——计数事件是 **delivered**，不是 sent。

紧接着是与第 2 章客服窗口的接缝句：`If a WhatsApp user responds to a marketing message, it starts a 24-hour` 客服窗口，而`Marketing messages sent within this window do not count towards the limit`。

这一对句子构成一个可核对的结构：**同一条营销模板消息，在窗口内发不占这道闸，在窗口外发占**。本篇把"回复开窗 → 窗口内免计数"登记为附表 C 该体系的一条档位，并且在 13.1 节把这一行与另外**六**行同字面量的 **24** 并列。

## 9.5 重试：官方建议值、强制执行值与 131049

重试章节给的是建议而非机制：`To ensure your campaigns reach your audience, wait at least 24 hours before attempting to resend a marketing template message to a user that has reached their limit`，并说明提前重发的两个后果——`will likely result in additional error responses` 与 `can reduce the accuracy of campaign delivery reporting`。

强制执行句是另一套数字结构：`If your Messaging account attempts to resend marketing messages multiple times within a 24-hour period to users who have already reached their messaging limit, further delivery attempts to these users may be unavailable for up to 24 hours and error code 131049 will be returned`。

三点按原样保留。**其一**，触发主语是 Messaging account，不是号码也不是组合。**其二**，"multiple times"官方没给次数，本篇不写"三次"或"两次"。**其三**，这一句的封锁对象是**该收件人**，官方同一句补了范围限定：`This does not affect your ability to send marketing messages to other users`。

webhook 侧的写法与建议侧一致：`the messages webhook is triggered with status set to failed and error code set to 131049`。**131049** 因此是"限额用尽"与"反复重发"两种情形共用的同一个码——官方文本没有把它们分成两个码，本篇不推断平台内部是否区分。

## 9.6 美国号码：一句现在时，一句未来时

这一小节是全篇最容易读错的一句，因为官方在同一行里混了两个时态。现状句：`WhatsApp does not currently deliver marketing template messages to WhatsApp users with United States phone numbers`，并且当场定义了什么叫美国号码——`numbers composed of a +1 dialing code and a US area code`。

紧随其后的句子却写了时间点：`Attempting to send a template message to a WhatsApp user with a US phone number after this date will result in an error`。而**"this date"在该页没有对应日期**。本篇因此把这一处登记为附表 F 的一处口径缺口：句子指向前文某个日期，但该页正文没有任何日期可供回指。

范围也要照抄：这一条只针对**营销**模板消息，不针对实用与认证模板；并且它是"不投递"，不是"接口报错拒绝请求"——第一句用的词是 does not deliver，第二句才用 result in an error。

---

# 10 限额档位：五档阶梯、三条路径与 2025-10-07 的组合化

## 10.1 定义句：三个限定条件同时成立

限额页的定义句一口气给了三个限定：`Messaging limits are the maximum number of unique WhatsApp user phone numbers your business can deliver messages to, outside of a` 客服窗口、`within a moving 24-hour period`。

拆开即：**计量对象是唯一收件人号码**（不是消息条数）、**只统计窗口外投递**、**滚动 24 小时**。这三点决定了限额与第 11 章吞吐不可互换：一个数"人"，一个数"条"。同一定义在总则页被逐字复述了一次，两处都现行。

## 10.2 主语已经在组合上，而且那句警告是官方自己写的

现行文本的第一句主语说明：`Messaging limits are calculated and set at the business portfolio level and are shared by all business phone numbers within a portfolio`，随后是本篇引用最多的一句警告：`This means that if a business portfolio has multiple business phone numbers, it's possible for one number to consume all of the portfolio's messaging capability within a given period`。

这句话在第 1.3 节的"主语层级"表里是决定性的证据：**限额的池子是组合的，号码只是取水口**。本篇不把它写成建议，只把它写成官方对后果的陈述。

## 10.3 起点 250 与四档目标

阶梯句：`Newly created business portfolios have a messaging limit of 250, but this messaging limit can be increased to`，随后四行——`2,000 (by completing a` 升级路径、`10,000 (via` 自动扩档、`100,000 (via automatic scaling)`、`Unlimited (via automatic scaling)`。

**起点是否算一档，官方没有回答**：250 被写成 have a messaging limit of，而"increased to"只列了后面四个。本篇因此把附表 C 的"限额档位"体系登记为 **8** 档（含换代前后的差异行），并在附表 B 第一档把 250 与 2,000／10,000／100,000／UNLIMITED 各开一行。这五行的「数的是什么」列写的是起始档位与第二／三／四档、最高档，但「性质」列并不同步：只有 UNLIMITED 一行记为**分档晋级项**，其余四行记为**硬数量上限**——官方给前四个数字都写了具体量，本篇不因为它们同属一条阶梯就把性质列拉平，也不做"共五档"或"共四档"的单值结论。

## 10.4 升到 2,000 的三条路径不是一种条件

官方把 2,000 单独处理：`You can increase your messaging limit to 2,000 by completing one of the` 升级路径，`After that, your limit increases automatically to the next higher limit if you meet the` 自动扩档条件。三条路径是：完成商业验证、由合作方代为验证、以及第三条自带数量条件的——`Send 2,000 delivered messages outside of customer service windows to unique WhatsApp user phone numbers within a 30-day moving period, using templates with a high` 质量评级。

三条路径的**性质不同**：前两条是身份动作，第三条是投递动作，且第三条同时要求"高质量模板"。同页还有一句常被略过的收尾：`Once you complete one of these paths, Meta analyzes your` 消息质量，`your business portfolio's eligibility for automatic scaling will either be approved or denied`——**完成路径只是进入被分析状态，不是拿到限额**。

## 10.5 批准与拒绝各自的通知面

批准句：`Meta immediately increases your business phone number's messaging limit to 2,000 and notifies you by email and developer alert`，并额外触发一个 webhook，其中`max_daily_conversations_per_business`（v24.0 及以后）与`max_daily_conversation_per_phone`（v23.0 及更早，直至 2026 年 2 月）被设为新限额。

拒绝句：`Meta maintains your business phone number's messaging limit at its current level and notifies you via email and developer alert`，随后触发另一个告警 webhook，由两个字段的取值给出替代路径。该页用一张三行的表把替代路径写死，三个取值全部登记进附表 D，其中最常见的一格是再次要求`Send 2,000 delivered messages`那套条件。

**这一节值得单独记下的是通知渠道的不对称**：批准走 email 加开发者告警加能力 webhook，拒绝走同样三样但换了一个 webhook 种类。两处都没有给出人工复核入口——这与第 8.4 节暂停申诉的 24 小时承诺是两套东西，本篇不合并。

## 10.6 自动扩档的两个条件与 6 小时

两条条件是合取：一是`You are sending` 高质量消息、并且覆盖`across all of your business phone numbers and templates`；二是`In the last 7 days, your business has utilized at least half of your current messaging limit`。达成后的时限句：`If these criteria are met, your portfolio's limit increases by one level within 6 hours`。

**一次只升一级**这一句官方写得明确（`by one level`），因此从 2,000 到 Unlimited 至少要经过两轮观察期。本篇不写总时长，因为官方没有写。

## 10.7 同一个条件在帮助中心写作 50%

帮助中心把第二条条件写成：`More than 50% of your current limit was used in the last 7 days.`，而开发者文档写的是`at least half`。**两者不是同一个命题**：一个是严格大于半数，一个是大于或等于半数。两处都现行、分属两个站点，官方没有对照句。

本篇的处理方式是：附表 B 第一档给这两句各开一行，出处列分别写明帮助中心页与开发者文档页；第 13.3 节把它列为口径冲突的第一例。**不取交集、不取并集、不写"约一半"。**

## 10.8 查询面：一个面板、一个字段、一个被弃用的字段

查询有两条面：Manager 里的`Account tools` > `Messaging limits` 面板，以及接口侧请求`whatsapp_business_manager_messaging_limit`字段。旧字段被两次写明弃用——页首的警告句与正文的`the` messaging_limit_tier` which used to return the phone number's messaging limit, has been deprecated`复述句（本篇按原样只取其中的`has been deprecated`作为登记依据）。

示例响应里的取值写法是`TIER_250`，即**字段返回的是档位名而不是数量**。这一条与 10.5 的 webhook 用数值（`2000`、`10000`、`100000`、`UNLIMITED`）形成对照：**同一个限额在三处有两种表示法**，本篇分别登记，不写成"统一为档位字符串"。

## 10.9 换代表的六个可核对差异

说明页把 2025-10-07 前后的差异排成一张表，本篇按行取用，逐格登记：设置位置从`On individual business phone numbers.`改为`On individual business portfolios.`；新组合起点从`Not applicable`改为`250`；新注册号码从`250`改为共享组合限额；`Limit upon becoming eligible for automatic scaling`一行从**1,000**改为**2,000**；扩档时间从`We increase the limit 24 hours after you meet the criteria.`改为`Limit increased 6 hours after meeting criteria.`；号码质量状态一行从`If your business phone number quality rating has been set to` **Flagged** 持续 7 天就降一级，改为`The` **Flagged** `phone number quality state is no longer possible`并且`its messaging limit will not be downgraded`。

**这一行是本篇少见的"官方明文废除一个状态"**，它与帮助中心 896873687365001 页里`Flagged and Restricted have been discontinued`互相印证，两处都登记在附表 B 第四档与附表 D 的档位阶梯组里。

## 10.10 官方给出的三条换代理由，以及它们不是承诺

官方把收益写成三句：`Faster access to higher limits`, with upgrades within 6 hours (compared to the current 24 hours) and immediate access to the portfolio's limits for newly registered numbers；`Greater flexibility`；`Reduced administrative burden and operational costs`。

本篇对这三句只做一件事：**标注它们是换代说明页的收益陈述，且该页顶部已被官方标注为现在生效、仅作留存参考**（`The changes described in this document are now live, and are only here for reference purposes`）。这三条理由不构成任何可核验的服务水平承诺，正文不把它们写进"限额规则"。

---

# 11 六道闸的其余五道：吞吐、速率、请求、屏蔽与批量

## 11.1 总则页只列了三道，本篇补三道

平台总则页在`In addition, the platform applies several message rate limits:`之后列了三条——**messaging limits**（并写明`Set at the business portfolio level`）、**Throughput**（`Business phone numbers can send up to 80 messages per second by default, with capacity upgrades available. Set per business phone number`）、**Quality rating**（`Assigned per business phone number and per template, and used to determine automatic throughput tier upgrades`）。

其余三道不在这一节里：同一收件人的成对速率在另一个小节、应用请求速率在接口限流小节、逐用户限额在模板子站单独成页。**"六道"是本篇的归纳口径而不是官方的分类**，第 13.2 节把这一点登记为结构性留白：官方从未在任何一页给出"限流共几道"的清单。

## 11.2 吞吐的定义与它包含什么

默认与升级句：`For each registered business phone number, Cloud API supports up to 80 messages per second (mps) by default, and up to` **1,000 mps** `by automatic upgrade`。

范围句同样重要：`Throughput is inclusive of inbound and outbound messages and all message types`——**入站也算**，这与"吞吐是发送额度"的常见读法相反。同一页还有一句把两道闸的关系写明：`Note that business phone numbers, regardless of throughput, are still subject to their` 账号级限流与模板限额，也就是说吞吐不是替代限额而是另一层。

超限行为句：`If you attempt to send more messages than your current throughput level allows, the API will return error code` **130429** `until you are within your allowed level again`。该页同时提醒吞吐面向**不同收件人**的活动：`Also, throughput levels are intended for messaging campaigns involving different WhatsApp user phone numbers`，对同一号码过量则会撞上成对限流（11.5）。

## 11.3 1,000 mps 的资格三条件、12 小时与那个 1 分钟

资格页三条：`The business portfolio associated with the phone number must have an unlimited` 限额；`The business phone number must be used to message 100K or more unique WhatsApp user phone numbers, outside of a` 客服窗口、`within a moving 24-hour period`；`The business phone number must have a` `quality_score` `of` `YELLOW` `or higher`。

换代说明页补了时限：`If you meet our eligibility requirements, we will automatically upgrade your business phone number to 1,000 messages per second within 12 hours`，而现行吞吐页只写`your business phone number is automatically upgraded to 1,000 mps at no cost to you`并且`Higher throughput does not incur additional charges or affect pricing`。**两条时限都登记**，性质列写明前者来自仅作留存参考的换代说明页。

升级过程本身有一个不可用窗口：`The upgrade process itself can take up to 1 minute. During this time the number will not be usable on our platform. If used in an API request, the API will return error code` **131057**。同句给出一次性性质：`Once a business phone number has been upgraded, it will automatically be upgraded for any future throughput increases with no downtime`。

升级的可观测结果是回调侧的：一个质量更新 webhook，`event` 取值为`THROUGHPUT_UPGRADE`，且`max_daily_conversations_per_business` 取值为`TIER_UNLIMITED`。**这是本篇见到的唯一一处用档位字符串表达"无限"的写法**，与 10.8 的 `TIER_250` 同一表示法。

## 11.4 20 mps 是给双用途号码的固定值

固定值句：`In order to remain compatible with the WhatsApp Business app`，那些同时用于商业应用与云端接口的号码`have a fixed throughput of 20 mps`。

**"固定"在这里是有实际含义的词**：它不参与 11.3 的自动升级，官方在这一段没有给任何例外句。附表 B 第一档把它登记为硬数量上限，与默认 **80**、升级 **1,000** 并列三行。

## 11.5 同一收件人：6 秒 1 条、45 条突发与 4^X

成对速率句把四种单位一次给全：`Business phone numbers can send 1 message every 6 seconds to the same WhatsApp user (0.17 messages/second), which equals about 10 messages per minute or 600 per hour`，超限行为是`Exceeding this limit triggers` **131056** `until you are back within the allowed rate`。

突发额度是**借用的**：`You may send up to 45 messages in a 6-second burst, but this "borrows" from your future quota`，随后`After a burst, you must wait the equivalent time it would take to send those messages at the normal rate`，官方给的示例是`for example, a burst of 20 requires a ~2-minute wait before sending more to that user`。**这个示例是示例，不是公式**，本篇不据此推算任意突发长度的等待时间。

失败后的重试写法是一个指数式：`To manage post-burst throttling, if a send request fails, retry after 4`^X seconds (starting with X=0 and increasing X by 1 after each failure) until successful。本篇把 **4^X** 登记为附表 B 第五档的"模糊量"，性质列写明它是官方写出的式子而不是算出的秒数。

## 11.6 应用请求速率的两个数与两个前提

默认值句：`For the following endpoints, your app can make 200 requests per hour, per app, per Messaging account, by default`——注意三个限定层层收窄（每小时、每应用、每消息账号）。

提升值句：`For active Messaging accounts whose WhatsApp account hierarchy has at least one registered phone number, your app can make 5000 requests per hour, per app, per active Messaging account`。**这一档的前提是层级里至少有一个已注册号码**，因此它不是"高级档"而是条件触发。同页稍后又有一句把 **5000** 用在信用额度接口上：`For the following` Credit Line API `requests, your app can make 5000 requests per hour`——**同一个数字挂在两类不同的端点集合上**。这两句的主语层级都是"应用—消息账号"、速率数值相同、出处同一页，因此本篇按附表 B 的开行规则（同一数字出现在两个**体系**里才开两行）记 **1** 行，把两类端点集合写进同一行的「数的是什么」列，而不是造出第二行。

## 11.7 回调面的容量要求（发送侧读它，才知道哪一段是自己扛）

吞吐页把回调服务器的容量写成一句设计标准：`Your webhook servers should be able to withstand 3x the capacity of outgoing message traffic and 1x the capacity of expected incoming message traffic`，并给了一个算例——`if sending 1,000 mps with a 30% expected response rate, your servers should be able to process up to 3,000 message status webhooks plus an additional 300 incoming message webhooks`。

延迟标准是两条：`Median latency not to exceed 250ms`、`Less than 1% latency exceeds 1s`。重投窗口是`Meta attempts to re-deliver failed webhooks for up to 7 days, with exponential backoff`。

这一节的口径要点：**这里的 **7** 天与第 9.5 节的 24 小时、第 11.3 的号码不可用都不是一回事**，它数的是回调重投；而在无存储模式下这个窗口会被压缩到 1 小时（12.5）。附表 B 第三档因此把"失败回调的重投窗口"单列一行。

## 11.8 屏蔽名单：三个数，两个是容量，一个是时间

屏蔽接口的三条限制逐字登记：`You can only block users that have messaged your business in the last 24 hours`、`Each request can include a maximum of 1,000 users`、`The blocklist has a 64,000 user limit`。第四条不是数字——`You cannot block another WhatsApp account`，它把这道闸的对象限定为**用户号码**而不是企业账号。

行为句同样要抄下来：`Your business cannot message the user. Attempts to message a blocked user return an error`，并且`The API returns errors per-number, because a block can succeed on some numbers and fail on others`；同页写明该接口是同步的。**"部分成功"是官方明文，本篇不把它写成原子操作。**

## 11.9 单次请求的批量上限分散在三个页面，数字互不相同

本篇把三处批量上限并列，因为它们极易被记成一个：**迁移**请求的上限是`maximum count of 500`，**删除**请求是`You can include up to 100 template IDs per request`，**解档**请求是`You can include up to 100 template IDs per request`（同一措辞、不同页面）。

删除侧还有一句失败语义：`The` `hsm_ids` `parameter cannot be combined with the` `name` `or` `hsm_id` `parameters. If any of the template IDs are invalid, the entire request fails and no templates are deleted`——**与 11.8 的屏蔽接口正好相反**：那边是逐号返回成败，这边是一损俱损。两处并置是本篇认为最容易被读者混用的一对。

## 11.10 六道闸的主语对照

按官方文本逐道还原主语，可以得出一张没有任何一行可以合并的表：**限额**在组合（唯一收件人数／滚动 24 小时）、**吞吐**在号码（每秒消息数、含入站）、**成对速率**在号码与单个收件人之间（6 秒 1 条）、**应用请求速率**在应用与消息账号之间（每小时）、**逐用户营销限额**在收件人身上（未公开数值）、**模板与组合级节流**分别在主语是模板与主语是组合的两套机制上。

六道里只有第一道有公开的档位数字，其余五道的"当前值"分散在四种不同的查询面里。**这就是第 0 章那句结论的全部内容**：官方文本从来不提供一个可相乘得到"总发送量"的公式。

---

# 12 生命周期、留存与计费层

## 12.1 有效期：一张默认表、一个 -1、一次被清空

定义句：`If a message cannot be delivered to a WhatsApp user, WhatsApp retries delivery for a period of time known as` 有效期。可定制范围句把三条通道并列：`You can customize the default TTL for authentication and utility templates sent via Cloud API, and for marketing templates sent via Marketing Messages API for WhatsApp`——**营销模板的有效期在云端接口侧不在该句的许可范围内**，附表 E 的三个解析行就是这一格的直接来源。

默认值两行：`All messages except authentication templates:` **30 days**，`Authentication templates:` **10 minutes**，认证模板另有一个 2024 年 10 月 23 日之前的例外（登记在附表 B 第四档）。定制步长是`TTL can be customized in 1-second increments`，并且`For authentication and utility templates, you can set the` `message_send_ttl_seconds` `property value to` `-1`, which will set a custom TTL of 30 days。

超出后的行为是一句风险提示：`The system drops messages that cannot be delivered within the default or customized TTL`，以及`If you do not receive a` 已投递回调 `before the TTL is exceeded, assume the message was dropped`。

与第 6 章的接缝在这里落地：类别被自动重判时`the system clears the TTL value by setting it to a` `_null value_`，随后企业需要按新类别的区间重新设置。官方给了一个完整算例——一个自定义 **12 小时**有效期的实用模板被改判为营销模板后，可设范围变成 **12 小时**到 **30 天**。**这个例子说明重判的代价不止是计费类别，还包括投递重试策略**。

## 12.2 编辑窗口：两个约束写在同一行，用"or"连接

原文只有一行：`Approved templates can be edited up to 10 times in a 30-day window, or 1 time in a 24-hour window. Rejected or paused templates can be edited an unlimited number of times.`

这一行的读法要交代清楚：连接词是 **or**，也就是说两个窗口同时生效、任一触顶即不可再编辑（该页没有给出更细的解释，本篇不写"取较严者"这种推断性表述，只保留官方原句）。第三句是例外：**被拒与被暂停的模板编辑次数不限**——这与第 8.4 节"编辑即回到 In Review"合起来看，构成一条真实的激励结构，本篇只登记两句同现，不评价。

## 12.3 删除：两种 30 天，语义完全不同

同名冻结句：`If you delete an approved template, you cannot create a new template with the same name for 30 days`。

在途续投句：`If you delete a template that has been sent in a template message but has yet to be delivered`，例如收件人设备关机，`the template's status is set to` `PENDING_DELETION` `and WhatsApp attempts delivery for 30 days`。

两个 **30 天**在附表 B 里各开一行、「数的是什么」列分别写"已批准模板同名重建的冻结期"与"删除后未送达消息的续投尝试期"，并在第 13.1 节作为"同数不同集"的常规例子——那一节会说明：单位是"天"的 **30** 其实有 **五** 行，这两行只是其中两行。**其中 `PENDING_DELETION` 是本篇登记的全部状态枚举里唯一带"删除已在进行"语义的一格**，它不出现在第 4 章的四套枚举任何一套里。

## 12.4 归档：12 个月、28 天与一次重置

自动归档句：`Templates that have been inactive for 12 months or more are automatically archived. Archived templates cannot be sent in template messages and are scheduled for deletion after 28 days. You can unarchive a template within the 28-day window to restore it to its previous status.`

解档语义有两个细节被写在同一页：`Unarchiving a template restores it to its previous status before it was archived`，而`If no previous status is available, the template is restored to` `APPROVED`——**默认回到已批准，而不是回到未知**；以及`Unarchiving a template resets its inactivity clock. If you unarchive a template, it is not eligible for auto-archival again until another 12 months of inactivity have passed`。

批量口径已在 11.9 登记（**100** 个 ID 一次）。"不活跃"如何计量官方没有给定义句，本篇在附表 C 的时长列把该档登记为**未写明**。

## 12.5 留存分三层：30 天、60／90 分钟、1 小时

第一层是默认条款。`Messages have a maximum retention period of 30 days in order to provide the base features and functionality of the Cloud API service; for example, retransmissions`，同页另一句补了静态加密：`Cloud API messages at rest are encrypted`。**30 天在这里是上限而不是固定值**，官方给的理由是重传。

第二层是本地存储。该功能把数据分成"在用"与"在用之后"两段，在用窗口按接口分两个值——`When using local storage for Cloud API, the data-in-use period is up to 60 minutes`与`When using local storage for Marketing Messages API for WhatsApp, the data-in-use period is up to 90 minutes`；过了这个窗口，`message content is deleted from Meta data centers outside of the specified local storage region, and persisted only in data centers within the local storage region selected`。同一页还有一条对活动复用有直接影响的限制：`Uploaded media are accessible for 30 days`。

第三层是无存储。它是本地存储的一种定制配置：`the data is not persisted at rest`，出站入站消息与媒体 blobs 均`stored for a maximum of 1 hour`，并且可以把上传媒体的 TTL 从 1 小时放宽到 30 天（`You can pass a custom time-to-live (TTL) — from 1 hour to 30 days — when uploading media`）。

代价被官方写成三条限制，本篇逐条登记。**其一**，解密失败的重试窗口只有 1 小时：`After this 1-hour window, Cloud API cannot retry the message`。**其二**，回调重投从 7 天压到 1 小时，`If your webhook server is unavailable beyond this window, the webhook (including incoming messages, receipts, etc.) will be permanently lost`——这一条与 11.7 直接冲突式并存：一处写 7 天，一处写 1 小时，**主语是同一个功能开关下的不同配置**，本篇不合并。**其三**，入站媒体 1 小时后不可取回。

配置侧还有一句硬约束：`Currently, only the` `60` `value is allowed for the` `retention_minutes` `parameter`。

## 12.6 计费层：按条、只在投递时计费、两个方向不对称

现行模型句：`Effective July 1, 2025 – Meta charges on a` 按条基础、`Meta charges by category–market, and only for messages from businesses to users. Meta does` **not** `charge for messages from users to businesses`。两条基础规则：`Businesses are only charged when a message is` `_delivered_. Meta does _not_ charge for messages sent`，以及`Meta has 5 different message categories on the WhatsApp Business Platform`——第五类是 2026 年 7 月 1 日随商业智能体引入的。

费率维度官方自己数了一遍：`Meta publishes rates for transparency, across all 5 categories x 47 markets`。**5 × 47** 登记在附表 B 第五档，作为"费率表与量能档位的维度规模"。

免费额度是**唯一一处按月重置的发送额度**：`For each business phone number, Meta provides 1,000 free service messages per month, which do not roll over but reset monthly. Thus, Meta only charges as of the 1,001st service message for each business phone number`。**注意主语是号码而不是组合**，这与第 10 章限额改到组合上形成一处真实的错位，本篇登记进 13.3 而不解释成因。

非模板消息的类别定义与第 2 章一致：`Non-template messages` 只能在开窗的 24 小时客服窗口内发送，`a business can also choose to use template messages to respond to users; however, non-template messages` 不能用来主动触达。

## 12.7 会话计价的遗留文本仍在，且带一个 72 小时

遗留页顶部现在是弃用声明：`Deprecated` — `This document describes conversation-based pricing, which was replaced by per-message pricing on July 1, 2025`，同一句解释了它保留的理由（历史账单数据）。

该页仍现行的一段是免费入口会话：`Free-entry point conversations last 72 hours`，而开窗条件是一个两条合取：客户通过广告或主页行动按钮发来消息，**且**企业在 24 小时内回复——`If you do not respond within 24 hours, a free entry point conversation is not opened and you must use a template to message the customer`。开窗后`it automatically closes all other open conversations between you and the customer`。

**72 小时与 24 小时的嵌套关系**是这一节的关键：入口会话 72 小时，但其中的非模板消息只在客服窗口内可发。官方给了一个完整的时刻算例（10 点开始、72 小时、非模板到次日 10 点为止），并明确`it is independent of the free entry point conversation`。本篇把这两个时长各登记一行，并在附表 E 的"未按类别区分"维度之外单列**遗留计价层**作为第 12 章的时间锚。

## 12.8 分析层的两个窗口与一个停用节点

分析页顶部是一条已经生效的变更：`Starting December 1, 2025, the maximum lookback window for messaging, conversation, and pricing analytics is changing from 10 years to 1 year. The lookback window for template and template group analytics will be unaffected and will continue to be 90 days.`

**两组窗口必须分开读**：**10 年 → 1 年**只作用于消息、对话与计价三类；模板与模板组仍是 **90 天**（`Must be no more than 90 days from the current date`）。此外对话分析字段本身有停用节点：`This field is deprecated and will be unavailable beginning November 1, 2026`，同段把它与`Conversation-based pricing was deprecated on June 30, 2025`并置。

可请求的字段共 **7** 个（`analytics`、`conversation_analytics`、`pricing_analytics`、`template_analytics`、`template_group_analytics`、`call_analytics`、`group_analytics`），本篇在附表 B 第五档登记为**7 个分析字段**，模板分析指标 **30** 项逐条落在附表 D 最大的那一组里。

## 12.9 营销消息接口：两个功能与四个节点

最高单价功能页的定义句给出两个能力：`The first pricing feature allows you to` 设置每次投递的最高单价、`when a max price is set, Meta will charge that max price or lower for delivery`，以及`The second pricing feature is the` 触达预估工具。报价的分母被写成`a` `bid_amount` `value per 1,000 deliveries`，推出节奏是`The max price feature rolls out in three phases`。

**"最高单价可以高于公布费率"是官方明文**——`by setting max prices higher than published rates`，理由是节假日与促销期的投递率提升。这一句与第 9.1 节的"限额由平台判定"合起来读才完整：企业一侧可设的是**支付上限**，不是**投递保证**。

时间线四节点各登记一行（附表 B 第四档）：接口正式可用、**2026-05-15** 有限公测、`Max price enters open beta on October 1, 2026`、以及第二季度的正式可用。与之配套的是云端接口侧营销消息的退役三节点（**2026-10-13** 宣布、**2027-04-13** 起新建账号不能经云端接口发营销消息、**2027-10-13** 云端接口停止支持）——两组日期来自两个页面，本篇不推断任何一组的后续变化。

## 12.10 一句话小结

第 12 章的五层（有效期、编辑、删除、归档、留存）与三层计费（现行、遗留、接口侧）在官方文本里**互不引用**：没有一句官方文本把"TTL 用完"与"限额耗尽"或"逐用户限额"写在同一个因果句里。本篇把它们并置只是为了让读者看见**同一批数字（**24 小时**、**30 天**、**7 天**、**1 小时**）在五层里各是什么意思**。

---

# 13 三张清单

## 13.1 数字归档：137 行、五档，以及"同数不同集"的四组例子

本篇把正文出现的每一个官方数字送进附表 B，按性质分五档：**限额与规模档位 24** 行、**模板构成上限 21** 行、**时长与滚动窗口 41** 行、**生效日期与版本节点 28** 行、**速率、比率与容量口径 23** 行；按字面值去重后是 **90** 个不同写法，计数单位共 **56** 种。

四组"同一个数、不同的一组东西"的例子，本篇认为比任何单个数字都更值得记住。下面四组都按附表 B 的**数字字段**逐行数过，因此"同一数字换出现在单位字段"的行不计入本组的行数，会单独交代。这个区分本身就是结论的一部分：**数字**与**计数单位**是两根独立的轴，换一根轴就换一件事。

**24**：数字字段写 **24** 的共 **7** 行，全在第三档，单位都是小时——客服窗口时长、模板审核决策上限、换代前限额上调所需时间、暂停申诉的复核承诺、逐用户营销限额的重试间隔建议、反复重试后的不可用时长、模板质量标签所用的滚动窗口。前六行性质是"硬时长（定值）"，第七行性质是"滚动窗口"；其中重试间隔建议与不可用时长两行来自同一个页面，却是两条不同的规则。另有 **1** 行的单位字段里带 24（"1 次/24 小时"的模板编辑约束），它的数字字段是 **1**。七个位置七个主语，加上那个 1，没有一行可以合并。

**7**：数字字段写 **7** 的共 **6** 行。**5** 行单位是"天"、全在第三档——实用模板节流的有效窗口、低质量号码状态回到已批准的连续天数、质量评级所用的滚动观察天数、类别误用限速的最短持续期、失败回调的重投窗口；这五行里三行是"硬时长（定值）"、两行是"滚动窗口"。第 **6** 行在第五档，单位是"个分析字段"，数的是分析接口可请求的字段数——同一个 7，跟前五行没有一个字的关联。

**30**：数字字段写 **30** 的共 **6** 行，跨两档、两种单位。第三档 **5** 行单位是"天"：已批准模板同名重建的冻结期、删除后未送达消息的续投尝试期、服务端消息最大留存期、已上传媒体的可访问期、重复违规的类别纠正限制期。第二档 **1** 行单位是"个商品"：商品消息模板的商品数。另有 **1** 行的单位字段是"次/30 天"（编辑窗口），它的数字字段是 **10**。所以 12.3 节那"两种 30 天"是这五行里的两行，说"30 天只有两行"会把余下三行和那个 10 一起丢掉。

**2,000**：数字字段写 **2,000** 的只有 **1** 行（第一档，messaging limit 第二档），本篇原来记的"三行跨两档"不成立。同一个字面量确实出现在别处，但不在 B：附表 C 用它记"完成三条升级路径之一"这一触发行，附表 F 有 **2** 行涉及它（同页 `in` 与 `within` 的介词不一致、以及它与 10,000 起的自动阶梯分写两处）。换代页还把它用作`Limit upon becoming eligible for automatic scaling` 一格的换代后取值，同格换代前取值是 **1,000**；这两个数字都没有作为档位行进 B。另外 B 里有一个不带千分位的 **2000**，单位是字符，数的是网址按钮示例 URL 的上限——两个写法在按字面去重时算两个。与之形成对照，**1,000** 在 B 里挂了 **4** 行跨 **2** 档：条/秒的自动升级吞吐、个用户的单次屏蔽请求上限、条服务消息/月的不计费额度（以上第一档）与次交付的最高单价报价分母（第五档）。

## 13.2 官方未写明：3 项主动留白与一整列空值

本篇把"未写明"分成两类，避免读者把它们混起来。

**第一类是官方主动写出的留白**，共 **3** 项：模板节流的触发阈值`an unspecified threshold is reached`；组合级节流的可观测性——该功能`being released gradually over the coming weeks so may not apply to you immediately`；以及 9.6 节那句`after this date`所指日期在该页缺失。三项逐条登记在附表 F。

**第二类是档位表里空着的时长列**，共 **22** 档：官方给了触发与后果但没有给时长的档位，主要是限额各档位的"停留时长"、逐用户限额的周期、类别误用阶梯中限速一档的容量数字，以及归档体系里"不活跃"的计量方式。这一类的正确读法是**没有数字**，不是"数字未公开"，也不是"数字很大"。

**结构性留白另有一处**：11.1 提到的"限流共几道"。官方从未给出一张涵盖六道闸的总表，第 11 章的分法是本篇按主语归并的结果，这一点在附表 E 的维度归属里同样标注为**手定**。

## 13.3 口径冲突：本篇见到的 14 处两页措辞不一致（与 3 处留白同表登记）

附表 F 共 **17** 行，其中 **3** 行是 13.2 的第一类留白，其余 **14** 行是两处或两页的措辞不一致。本篇认为最需要读者带走的六处：

1. **`at least half` 与 `More than 50%`**（10.7）：≥ 与 > 之差，跨两个站点，两处都现行。
2. **1,000 与 2,000**（10.9）：同一格"有资格自动扩档时的限额"在换代表两侧不同值。
3. **24 小时与 6 小时**（10.9）：同一动作"上调一级"的时限在两页各写一次，其中一句来自仅作留存参考的说明页。
4. **质量观察窗 7 天与 24 小时**（第 7 章）：号码层`over the past seven days, weighted by recency`，模板层`in a rolling window of the past 24 hours`，**两个都现行、主语不同、官方没有对照句**。
5. **7 天回调重投与 1 小时回调重投**（11.7 与 12.5）：默认吞吐页写 7 天，无存储页写 1 小时；后者是对前者的覆盖关系，但官方没写"覆盖"这个词。
6. **`1 message every 6 seconds` 的四种单位换算**（11.5）：官方在同句里给了 6 秒、0.17 每秒、约 10 每分钟、600 每小时四个写法，其中前两个是精确值、后两个是近似值（`about`、`equals`）。

其余八处的分布是这样的：**暂停与 Disabled** 的两套词表（Manager 的 `Active - Low quality` 与 API 的 `RED`）在官方句子内对齐，但两套其余档位不对齐；`Flagged` 状态在换代表左侧仍是可发生的、右侧被明文取消；`TIER_250` 与 `2000` 两种限额表示法；限额免费额度按号码而限额按组合；营销模板有效期在云端接口侧是否可定制的两处不同暗示；`unarchive` 与 `restore` 两个动词在同一页混用；**131049** 同时覆盖"用尽"与"反复重发"两种情形；以及 500／100／100 三个批量上限分属三页而无总表。**这八处的完整列表、逐处原句与出处页都在附表 F**，本篇不在正文重复。

---

# 14 结论

## 14.1 十二问的简短回答

第 0 章十二问按本篇的证据分别落在：**窗口外只能发已批准模板**（2.1）；窗口内 **16** 类（2.4）；模板构成上限十项（第 3 章）；**四套**状态与评级枚举且无对齐表（第 4 章）；营销是**兜底类**（第 5 章）；类别会被改判且复核周期有两个日期（第 6 章）；质量有**两个**观察窗（第 7 章）；三种互不相同的"停"（第 8 章）；收件人一侧有自适应闸且**美国号码当前不投营销**（第 9 章）；限额**五档 + 三条路径 + 两个条件 + 6 小时**（第 10 章）；六道闸主语各异（第 11 章）；官方留白 **3** 项、差异 **14** 处（第 13 章）。

## 14.2 与轴①的接缝在哪

轴①数的是**处罚**：违规如何被定义、处置如何分级、每一级给什么通知与复核入口。本篇数的是**能力**：一条消息被允许长成什么样、算哪一类、受哪几道闸。两者在官方文本里只有三处真实接缝，本篇逐条列出：

**第一处是质量。** 号码质量评级同时出现在第 7 章（决定限额能否自动扩档与吞吐能否升级）和轴①的处置阶梯里，但**没有任何一句官方文本把"评级低"写进违规定义**。**第二处是暂停。** 模板暂停的三档实例（3 小时／6 小时／Disabled）是能力层的机制，而它在轴①的违规阶梯里没有对应行——本篇与轴①都按原样保留这一不对齐。**第三处是类别误用。** 第 6 章的四档阶梯是唯一一处官方把"治理"与"限额"写在同一张表里的地方，因此本篇把它完整登记，同时在边界声明里说明它不构成规避建议。

三处之外，本篇与轴①不共用任何判定口径。

## 14.3 三条本篇认为读者会带走的结论

**第一，限额的主语变了，而多数二手解释没有跟着变。** 2025-10-07 之后限额算在组合上、共享、且官方明文警告一个号码可以耗尽整个组合的能力；同时按月免费服务消息的额度仍然按号码计算。**任何以"号码的限额"为主语的叙述，在这一天之后都需要重新核对主语**。

**第二，"能发多少"不是一个数，而是六道互不相通的闸。** 六道里只有第一道有公开档位（**250／2,000／10,000／100,000／Unlimited**），其余五道的当前值分散在四种查询面、两种表示法（档位字符串与数值）、以及一处**根本不公开的自适应限额**里。把任何一道的数字当作总量都是读错。

**第三，官方文本里的三种"停"与四种状态枚举彼此独立，且都不承诺原因。** 暂停页只给触发评级与时长，节流页把阈值写成`unspecified`，组合级节流页把判定写成`If feedback suggests suspicious activity`——**三处都没有给出可供企业自查的中间量**。本篇把这一事实本身作为结论之一，而不是把它解释成平台的疏漏。

## 14.4 本篇没有做的判断

本篇不判断这些规则是否公正、是否被均匀执行，也不提供任何绕过六道闸中任何一道的做法：不写脚本、不写多号轮换、不写限速与频控的规避方式、不写检测与反垃圾的绕过、不代写以躲开类别判定的模板文案、不给申诉话术模板。美国号码不投递、逐用户限额不公开这两个事实，本篇按原样陈述，不推测其实现细节。

官方写成区间、模糊量或主动标注未公开之处，本篇全部保留原写法并登记；三套"停"之间、四套枚举之间，官方没有给出对齐句之处，本篇一律不做跨体系等价推断。

# 附表 A 官方页面登记清单与本篇引用位置（89 行）

登记口径与系列前两篇一致：**凡本篇抓取的页面，一律登记**；本表收的是轴②（消息类型、模板、质量评级与限额档位）取到的官方页面，其余三根轴的页面不在本表内。全表 **88** 个登记位：**正文已取回 86 页、正文未取回 2 页**；站点分布为 Meta 商业帮助中心 6、Meta 开发者文档 78、法律与政策页 4。帮助中心与法律页**一律不显示日期字段**（本次逐页核验其页面元数据），因此本表不设日期列；未取回的 2 页在本表逐行标注，**其内容全篇未被引用**。

| 站点 | 页面标识 | 标题 | 本篇引用位置 | 正文取回状态 |
|---|---|---|---|---|
| Meta 开发者文档 | `whatsapp_about_the_platform_md` | About the WhatsApp Business Platform | 正文 1.2、1.3、2.1、11.1—11.7、13.1、附表 B（9 处）、附表 C（3 处）、附表 D（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_getting_opt_in_md` | Get opt-in for WhatsApp | 正文 1.2、2.6、13.2 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_messaging_limits_md` | Messaging Limits | 正文 10.1—10.8、13.1、附表 B（8 处）、附表 C（5 处）、附表 D（7 处）、附表 F（8 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_upcoming_messaging_limits_changes_md` | Upcoming changes to messaging limits | 正文 10.9—10.10、11.3、13.1、附表 B（4 处）、附表 C（3 处）、附表 F（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_throughput_md` | Throughput | 正文 11.2—11.4、11.7、13.1、附表 B（9 处）、附表 C（2 处）、附表 D（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_policy_enforcement_md` | WhatsApp Business Platform policy and spam enforcement | 正文 1.2、2.1、8.9 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_policy_enforcement_violations_md` | WhatsApp Business Platform Policy Violations | 正文 1.2、8.9 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_messages_send_messages_md` | Service messages | 正文 2.1—2.8、7.5、12.1、13.1、附表 B（3 处）、附表 C（3 处）、附表 D（16 处）、附表 F（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_overview_md` | Template fundamentals | 正文 3.1、3.4—3.6、4.1—4.2、5.1、13.1、附表 B（5 处）、附表 C（3 处）、附表 D（11 处）、附表 F（3 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_components_md` | Template components | 正文 3.1—3.3、3.6、13.1、附表 B（13 处）、附表 D（8 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_template_categorization_md` | Template categorization | 正文 5.1—5.4、6.1—6.7、13.1、附表 B（12 处）、附表 C（4 处）、附表 D（14 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_template_quality_md` | Template quality rating | 正文 4.2、7.1—7.2、附表 C（1 处）、附表 D（8 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_template_review_md` | Template review | 正文 4.1—4.5、5.1、附表 C（1 处）、附表 F（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_template_management_md` | Template management | 正文 11.9、12.2—12.3、附表 B（7 处）、附表 C（5 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_template_pacing_md` | Template pacing | 正文 8.5—8.7、13.1、附表 B（2 处）、附表 C（2 处）、附表 D（1 处）、附表 F（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_portfolio_pacing_md` | Business portfolio pacing | 正文 8.8—8.9、13.1、附表 B（2 处）、附表 C（1 处）、附表 D（1 处）、附表 F（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_template_pausing_md` | Template pausing | 正文 8.1—8.4、13.1、附表 B（5 处）、附表 C（4 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_time_to_live_md` | Configure message time-to-live | 正文 5.4、12.1、13.1、附表 B（5 处）、附表 C（4 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_template_library_md` | Template Library | 正文 5.4、6.7 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_template_migration_md` | Template migration | 正文 11.9、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_template_groups_md` | Template groups | 正文 4.6、附表 B（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_template_archival_md` | Template archival | 正文 11.9、12.4、附表 B（3 处）、附表 C（3 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_template_comparison_md` | Template Comparison | 正文 5.6、7.4、附表 D（9 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_template_media_md` | Template media | 正文 3.6 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_supported_languages_md` | Supported Languages | 正文 3.4、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_marketing_templates_md` | Marketing templates | 正文 3.7、5.2 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_marketing_templates_per_user_limits_md` | Per-user marketing template message limits | 正文 9.1—9.6、13.1、附表 B（2 处）、附表 C（4 处）、附表 D（1 处）、附表 F（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_marketing_templates_limited_time_offer_templates_md` | Limited-time offer templates | 正文 3.7 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_marketing_templates_media_card_carousel_templates_md` | Media card carousel templates | 正文 3.3、3.7、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_marketing_templates_coupon_templates_md` | Coupon code templates | 正文 3.7 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_marketing_templates_custom_marketing_templates_md` | Custom marketing templates | 正文 3.7 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_utility_templates_utility_templates_md` | Utility templates | 正文 3.7、5.3 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_utility_templates_booking_appointment_templates_md` | Booking appointment templates | 全篇不引用（正文未取回） | 正文未取回 |
| Meta 开发者文档 | `whatsapp_templates_utility_templates_location_request_templates_md` | Location request templates | 全篇不引用（正文未取回） | 正文未取回 |
| Meta 开发者文档 | `whatsapp_templates_authentication_templates_authentication_templates_md` | Authentication templates | 正文 3.7、5.4、12.1、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_authentication_templates_authentication_best_practices_md` | Best practices for authenticating users via WhatsApp | 正文 5.4 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_authentication_templates_autofill_button_authentication_templates_md` | One-tap autofill authentication templates | 正文 5.4 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_authentication_templates_copy_code_button_authentication_templates_md` | Copy code authentication templates | 正文 3.2、5.4 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_authentication_templates_zero_tap_authentication_templates_md` | Zero-tap authentication templates | 正文 5.4、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_authentication_templates_bulk_management_md` | Bulk management | 正文 4.7 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_authentication_templates_error_signals_md` | Error Signals | 正文 7.6、13.1、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_authentication_templates_keyboard_suggestions_md` | Keyboard suggestions | 正文 5.4、13.1、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_pricing_md` | Pricing on the WhatsApp Business Platform | 正文 12.6—12.8、13.1、附表 B（6 处）、附表 F（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_pricing_conversation_based_pricing_md` | Conversation-based pricing (Deprecated) | 正文 2.5、12.6—12.7、附表 B（4 处）、附表 C（1 处）、附表 D（4 处）、附表 F（4 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_pricing_non_template_messages_md` | Upcoming pricing updates for Meta Business Agent, service and utility messages | 正文 12.6 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_marketing_messages_overview_md` | Marketing Messages API for WhatsApp | 正文 12.9、附表 B（4 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_marketing_messages_features_md` | Features | 正文 12.9 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_marketing_messages_get_started_md` | Get started with the Marketing Messages API | 正文 12.9 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_marketing_messages_send_marketing_messages_md` | Send Marketing Messages | 正文 12.9 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_marketing_messages_pricing_md` | Set a max price for marketing messages (BETA) | 正文 12.9、附表 B（5 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_marketing_messages_onboarding_md` | Onboard | 正文 12.9 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_marketing_messages_view_metrics_md` | Viewing metrics | 正文 7.7 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_marketing_messages_measure_conversion_md` | Setting up conversion measurement | 正文 7.7 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_marketing_messages_deep_links_md` | Deep links | 正文 12.9 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_analytics_md` | Analytics | 正文 7.7、12.10、附表 B（5 处）、附表 D（45 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_block_users_md` | Block users | 正文 11.8、附表 B（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_local_storage_md` | Local storage | 正文 12.5、附表 B（3 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_no_storage_md` | No Storage | 正文 12.5、附表 B（1 处）、附表 D（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_data_privacy_and_security_md` | Data Privacy & Security | 正文 12.5、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_identity_change_md` | Identity Change | 正文 1.3 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_permissions_md` | WhatsApp permissions | 正文 1.3 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_business_phone_numbers_media_md` | Media | 正文 3.6、12.5 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_messages_interactive_list_messages_md` | Interactive list messages | 正文 2.4、3.3、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_messages_interactive_media_carousel_messages_md` | Interactive media carousel messages | 正文 2.4、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_messages_interactive_reply_buttons_messages_md` | Interactive reply buttons messages | 正文 2.4、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_messages_text_messages_md` | Text messages | 正文 2.4、2.7 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_typing_indicators_md` | Typing indicators | 正文 2.4 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_link_previews_md` | Link Previews | 正文 2.4 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_access_tokens_md` | Access Tokens Guide | 正文 1.3 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_messages_address_messages_md` | Address Messages | 正文 2.4 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_messages_location_request_messages_md` | Location request messages | 正文 2.4 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_messages_contextual_replies_md` | Contextual replies | 正文 2.4 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_messages_mark_message_as_read_md` | Mark messages as read | 正文 2.4 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_tap_target_url_title_override_md` | Tap target title URL override | 正文 3.6 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_authentication_templates_template_preview_md` | Template previews | 正文 4.5 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_utility_templates_location_templates_md` | Location templates (utility templates) - WhatsApp | 正文 3.7 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_utility_templates_utility_call_permission_request_templates_md` | Call permission request templates | 正文 3.7 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_templates_marketing_templates_call_permission_request_message_template_md` | Call permission request message template | 正文 3.7 | 正文已取回 |
| Meta 商业帮助中心 | `896873687365001` | About Your WhatsApp Business Phone Number’s Quality Rating | 正文 7.3、7.5、10.3、10.6、13.1、13.3、附表 B（3 处）、附表 C（4 处）、附表 F（3 处） | 正文已取回 |
| Meta 商业帮助中心 | `766346674749731` | About Your WhatsApp Business Message Template’s Quality Rating | 正文 4.3、7.4、8.1、13.3、附表 B（2 处）、附表 C（5 处）、附表 D（5 处）、附表 F（5 处） | 正文已取回 |
| Meta 商业帮助中心 | `2058515294227817` | How to verify your business in Meta Business Suite | 正文 10.4 | 正文已取回 |
| Meta 商业帮助中心 | `587323819101032` | When to submit your ID for business verification | 正文 10.5 | 正文已取回 |
| Meta 商业帮助中心 | `486932075688253` | About Business Portfolios | 正文 10.4 | 正文已取回 |
| Meta 商业帮助中心 | `1305007343713790` | How to Set a Custom Message Validity Period for WhatsApp | 正文 12.1 | 正文已取回 |
| 法律与政策页 | `https___whatsappbusiness_com_policy_` | Business Policy | 正文 1.1、5.3、8.9 | 正文已取回 |
| 法律与政策页 | `https___www_whatsapp_com_legal_messaging_guidelines` | WhatsApp Messaging Guidelines | 正文 1.1、2.6、5.2 | 正文已取回 |
| 法律与政策页 | `https___www_whatsapp_com_legal_WhatsApp_Terms_for_WhatsApp_Business_Platform` | WhatsApp Terms for WhatsApp Business Platform | 正文 1.1、1.2、2.1 | 正文已取回 |
| 法律与政策页 | `https___www_whatsapp_com_legal_terms_of_service` | Terms of Service | 正文 1.1、2.1 | 正文已取回 |
| **合计** | **86** 页正文已取回 | 正文未取回 **2** 页 | 站点合计 Meta 商业帮助中心 6、Meta 开发者文档 78、法律与政策页 4 | 本篇只用规则层，不取任何操作层内容 |

#ROWS=88 CITED=86 UNCITED=2

# 附表 B 数字归档五档（138 行）

归档规则见第 13.1 节：**同一数字出现在两个体系里开两行**，官方写成区间或多值的按整段一行登记，版本与日期行只进第四档，计数单位逐行标注。五档合计 **137** 行（一 **24**／二 **21**／三 **41**／四 **28**／五 **23**），去重后的数字与档位词条目 **90** 个，计数单位 **56** 种。

| 档 | 数字 | 计数单位 | 数的是什么 | 性质 | 出处页 |
|---|---|---|---|---|---|

| 一、限额与规模档位 | **250** | 个/日 | messaging limit 起始档位（唯一对话数） | 硬数量上限 | whatsapp_messaging_limits_md |
| 一、限额与规模档位 | **2,000** | 个/日 | messaging limit 第二档 | 硬数量上限 | whatsapp_messaging_limits_md |
| 一、限额与规模档位 | **10,000** | 个/日 | messaging limit 第三档 | 硬数量上限 | whatsapp_messaging_limits_md |
| 一、限额与规模档位 | **100,000** | 个/日 | messaging limit 第四档 | 硬数量上限 | whatsapp_messaging_limits_md |
| 一、限额与规模档位 | **UNLIMITED** | 档位名 | messaging limit 最高档 | 分档晋级项 | whatsapp_messaging_limits_md |
| 一、限额与规模档位 | **250** | 个模板 | 未验证商业资产组合下每个消息账号的模板数 | 硬数量上限 | whatsapp_templates_overview_md |
| 一、限额与规模档位 | **6,000** | 个模板 | 已验证且至少一个号码有已批准显示名时的模板数 | 条件触发项 | whatsapp_templates_overview_md |
| 一、限额与规模档位 | **100** | 个模板/小时 | 单小时内可在消息账号内新建的模板数 | 硬数量上限 | whatsapp_templates_overview_md |
| 一、限额与规模档位 | **500K** | 条模板消息 | 组合级节流的观察量（滚动 365 天内） | 条件触发项 | whatsapp_templates_portfolio_pacing_md |
| 一、限额与规模档位 | **80** | 条/秒 | 每个注册业务号码的默认吞吐 | 默认可改项 | whatsapp_about_the_platform_md |
| 一、限额与规模档位 | **1,000** | 条/秒 | 自动升级后的吞吐 | 分档晋级项 | whatsapp_throughput_md |
| 一、限额与规模档位 | **20** | 条/秒 | 同时用于商业应用与云端 API 的号码固定吞吐 | 硬数量上限 | whatsapp_throughput_md |
| 一、限额与规模档位 | **200** | 次/小时 | 每个应用对每个消息账号的请求数默认上限 | 默认可改项 | whatsapp_about_the_platform_md |
| 一、限额与规模档位 | **5,000** | 次/小时 | 已注册号码的活跃账号的请求数上限，同页另一句把同一个速率给 Credit Line API 端点集合 | 分档晋级项 | whatsapp_about_the_platform_md |
| 一、限额与规模档位 | **1,000** | 个用户 | 单次屏蔽接口请求可提交的用户数 | 硬数量上限 | whatsapp_block_users_md |
| 一、限额与规模档位 | **64,000** | 个用户 | 屏蔽名单可容纳的用户总数 | 硬数量上限 | whatsapp_block_users_md |
| 一、限额与规模档位 | **500** | 个模板 | 单次迁移请求的批次上限 | 硬数量上限 | whatsapp_templates_template_migration_md |
| 一、限额与规模档位 | **100** | 个模板 ID | 单次删除请求可提交的 hsm_ids 数 | 硬数量上限 | whatsapp_templates_template_management_md |
| 一、限额与规模档位 | **100** | 个模板 ID | 单次归档请求可提交的模板 ID 数 | 硬数量上限 | whatsapp_templates_template_archival_md |
| 一、限额与规模档位 | **100K** | 个唯一号码 | 吞吐自动升级的触达要求（24 小时内） | 条件触发项 | whatsapp_throughput_md |
| 一、限额与规模档位 | **50%** | 当前限额的比例 | 限额自动升档的利用率门槛（帮助中心写法） | 条件触发项 | 896873687365001 |
| 一、限额与规模档位 | **至少一半** | 当前限额的比例 | 限额自动升档的利用率门槛（开发者文档写法） | 条件触发项 | whatsapp_messaging_limits_md |
| 一、限额与规模档位 | **1,000** | 条服务消息/月 | 每个业务号码每月不计费的服务消息额度 | 硬数量上限 | whatsapp_pricing_md |
| 一、限额与规模档位 | **111** | 种语言 | 模板支持语言表的语言数 | 硬数量上限 | whatsapp_templates_supported_languages_md |
| 二、模板构成上限 | **60** | 字符 | 模板头部文本 | 硬数量上限 | whatsapp_templates_components_md |
| 二、模板构成上限 | **1024** | 字符 | 模板正文文本 | 硬数量上限 | whatsapp_templates_components_md |
| 二、模板构成上限 | **60** | 字符 | 模板页脚文本 | 硬数量上限 | whatsapp_templates_components_md |
| 二、模板构成上限 | **512** | 字符 | 模板名长度 | 硬数量上限 | whatsapp_templates_overview_md |
| 二、模板构成上限 | **10** | 个按钮 | 模板按钮组件总数 | 硬数量上限 | whatsapp_templates_components_md |
| 二、模板构成上限 | **10** | 个快捷回复 | 快捷回复按钮数 | 硬数量上限 | whatsapp_templates_components_md |
| 二、模板构成上限 | **2** | 个网址按钮 | 网址按钮数 | 硬数量上限 | whatsapp_templates_components_md |
| 二、模板构成上限 | **2000** | 字符 | 网址按钮示例 URL | 硬数量上限 | whatsapp_templates_components_md |
| 二、模板构成上限 | **25** | 字符 | 按钮标签文本 | 硬数量上限 | whatsapp_templates_components_md |
| 二、模板构成上限 | **20** | 字符 | 复制码按钮示例码 | 硬数量上限 | whatsapp_templates_components_md |
| 二、模板构成上限 | **15** | 字符 | 认证模板参数 | 硬数量上限 | whatsapp_templates_template_categorization_md |
| 二、模板构成上限 | **30** | 个商品 | 商品消息模板的商品数 | 硬数量上限 | whatsapp_templates_components_md |
| 二、模板构成上限 | **10** | 个分组 | 商品消息模板的分组数 | 硬数量上限 | whatsapp_templates_components_md |
| 二、模板构成上限 | **10** | 张卡片 | 轮播模板的卡片数 | 硬数量上限 | whatsapp_templates_marketing_templates_media_card_carousel_templates_md |
| 二、模板构成上限 | **10** | 行 | 互动列表消息的行数 | 硬数量上限 | whatsapp_messages_interactive_list_messages_md |
| 二、模板构成上限 | **3** | 个预定义回复 | 互动回复按钮消息的回复数（原文写作 three） | 硬数量上限 | whatsapp_messages_interactive_reply_buttons_messages_md |
| 二、模板构成上限 | **2—10** | 张卡片 | 互动媒体轮播消息的卡片数（消息侧区间） | 区间或多值 | whatsapp_messages_interactive_media_carousel_messages_md |
| 二、模板构成上限 | **3.5** | MB | GIF 文件大小 | 硬数量上限 | whatsapp_templates_components_md |
| 二、模板构成上限 | **256** | 字符 | 模板组名称 | 硬数量上限 | whatsapp_templates_template_groups_md |
| 二、模板构成上限 | **512** | 字符 | 模板组描述 | 硬数量上限 | whatsapp_templates_template_groups_md |
| 二、模板构成上限 | **4** | 个按钮 | 超出后桌面端不可视的按钮数 | 条件触发项 | whatsapp_templates_components_md |
| 三、时长与滚动窗口 | **24** | 小时 | 客服窗口时长 | 硬时长（定值） | whatsapp_messages_send_messages_md |
| 三、时长与滚动窗口 | **24** | 小时 | 模板审核决策上限 | 硬时长（定值） | whatsapp_templates_overview_md |
| 三、时长与滚动窗口 | **6** | 小时 | 满足条件后限额上调所需时间 | 硬时长（定值） | whatsapp_messaging_limits_md |
| 三、时长与滚动窗口 | **24** | 小时 | 换代前限额上调所需时间 | 硬时长（定值） | whatsapp_upcoming_messaging_limits_changes_md |
| 三、时长与滚动窗口 | **12** | 小时 | 换代后吞吐升级所需时间 | 硬时长（定值） | whatsapp_upcoming_messaging_limits_changes_md |
| 三、时长与滚动窗口 | **1** | 分钟 | 吞吐升级过程中的不可用时长上限 | 硬时长（定值） | whatsapp_throughput_md |
| 三、时长与滚动窗口 | **3** | 小时 | 模板暂停第一次实例 | 硬时长（定值） | whatsapp_templates_template_pausing_md |
| 三、时长与滚动窗口 | **6** | 小时 | 模板暂停第二次实例 | 硬时长（定值） | whatsapp_templates_template_pausing_md |
| 三、时长与滚动窗口 | **24** | 小时 | 暂停申诉的复核承诺 | 硬时长（定值） | whatsapp_templates_template_pausing_md |
| 三、时长与滚动窗口 | **7** | 天 | 实用模板节流的有效窗口（自首次暂停起） | 硬时长（定值） | whatsapp_templates_template_pacing_md |
| 三、时长与滚动窗口 | **30** | 天 | 已批准模板同名重建的冻结期 | 硬时长（定值） | whatsapp_templates_template_management_md |
| 三、时长与滚动窗口 | **30** | 天 | 删除后未送达消息的续投尝试期 | 硬时长（定值） | whatsapp_templates_template_management_md |
| 三、时长与滚动窗口 | **12** | 个月 | 模板自动归档的不活跃门槛 | 硬时长（定值） | whatsapp_templates_template_archival_md |
| 三、时长与滚动窗口 | **28** | 天 | 归档后计划删除的等待期 | 硬时长（定值） | whatsapp_templates_template_archival_md |
| 三、时长与滚动窗口 | **60** | 天 | 类别重判复核周期（其一） | 硬时长（定值） | whatsapp_templates_template_categorization_md |
| 三、时长与滚动窗口 | **24** | 小时 | 逐用户营销限额的重试间隔建议 | 硬时长（定值） | whatsapp_templates_marketing_templates_per_user_limits_md |
| 三、时长与滚动窗口 | **24** | 小时 | 反复重试后的不可用时长 | 硬时长（定值） | whatsapp_templates_marketing_templates_per_user_limits_md |
| 三、时长与滚动窗口 | **72** | 小时 | 免费入口会话的持续时长 | 硬时长（定值） | whatsapp_pricing_conversation_based_pricing_md |
| 三、时长与滚动窗口 | **60** | 分钟 | 云端 API 本地存储的数据在用时长按 | 硬时长（定值） | whatsapp_local_storage_md |
| 三、时长与滚动窗口 | **90** | 分钟 | 营销消息接口的数据在用时长按 | 硬时长（定值） | whatsapp_local_storage_md |
| 三、时长与滚动窗口 | **1** | 小时 | 无存储模式下的消息与媒体留存 | 硬时长（定值） | whatsapp_no_storage_md |
| 三、时长与滚动窗口 | **30** | 天 | 服务端消息最大留存期 | 硬时长（定值） | whatsapp_data_privacy_and_security_md |
| 三、时长与滚动窗口 | **30** | 天 | 已上传媒体的可访问期 | 硬时长（定值） | whatsapp_local_storage_md |
| 三、时长与滚动窗口 | **10** | 分钟 | 非认证模板消息的默认有效期 | 默认可改项 | whatsapp_messages_send_messages_md |
| 三、时长与滚动窗口 | **10** | 分钟 | zero-tap 握手的发起时长上限（可被 code_expiration_minutes 覆盖） | 硬时长（定值） | whatsapp_templates_authentication_templates_zero_tap_authentication_templates_md |
| 三、时长与滚动窗口 | **1** | 分钟 | 每次发送的最小间隔（同一收件人） | 硬时长（定值） | whatsapp_about_the_platform_md |
| 三、时长与滚动窗口 | **45** | 条 | 6 秒突发可借用的后续额度 | 条件触发项 | whatsapp_about_the_platform_md |
| 三、时长与滚动窗口 | **1** | 秒 | 有效期自定义的最小步长 | 硬数量上限 | whatsapp_templates_time_to_live_md |
| 三、时长与滚动窗口 | **30—900** | 秒 | 认证模板有效期可设区间 | 硬时长（区间或多值） | whatsapp_templates_time_to_live_md |
| 三、时长与滚动窗口 | **30—43200** | 秒 | 实用模板有效期可设区间 | 硬时长（区间或多值） | whatsapp_templates_time_to_live_md |
| 三、时长与滚动窗口 | **43200—2592000** | 秒 | 营销模板有效期可设区间 | 硬时长（区间或多值） | whatsapp_templates_time_to_live_md |
| 三、时长与滚动窗口 | **1—90** | 分钟 | 认证码过期时长可设区间 | 硬时长（区间或多值） | whatsapp_templates_authentication_templates_authentication_templates_md |
| 三、时长与滚动窗口 | **10** | 次/30 天 | 已批准模板可编辑次数 | 硬数量上限 | whatsapp_templates_template_management_md |
| 三、时长与滚动窗口 | **1** | 次/24 小时 | 已批准模板可编辑次数（另一约束） | 硬数量上限 | whatsapp_templates_template_management_md |
| 三、时长与滚动窗口 | **7** | 天 | 低质量号码状态回到已批准的连续天数 | 硬时长（定值） | 766346674749731 |
| 三、时长与滚动窗口 | **7** | 天 | 质量评级所用的滚动观察天数 | 滚动窗口 | 896873687365001 |
| 三、时长与滚动窗口 | **24** | 小时 | 模板质量标签所用的滚动窗口 | 滚动窗口 | 766346674749731 |
| 三、时长与滚动窗口 | **7** | 天 | 类别误用限速的最短持续期 | 硬时长（定值） | whatsapp_templates_template_categorization_md |
| 三、时长与滚动窗口 | **7** | 天 | 失败回调的重投窗口 | 滚动窗口 | whatsapp_throughput_md |
| 三、时长与滚动窗口 | **30** | 天 | 重复违规的类别纠正限制期 | 硬时长（定值） | whatsapp_templates_template_categorization_md |
| 三、时长与滚动窗口 | **90** | 天 | 模板与模板组分析的回看窗口 | 滚动窗口 | whatsapp_analytics_md |
| 四、生效日期与版本节点 | **v23.0** | API 版本 | 发送期参数错误码 132018 的起点 | 版本节点 | whatsapp_templates_template_management_md |
| 四、生效日期与版本节点 | **v24.0** | API 版本 | 组合级限额字段的起点 | 版本节点 | whatsapp_messaging_limits_md |
| 四、生效日期与版本节点 | **2023 年 6 月** | 月份节点 | 按对话计价的起始时间 | 版本节点 | whatsapp_pricing_conversation_based_pricing_md |
| 四、生效日期与版本节点 | **2023-09-12** | 日期节点 | 模板暂停机制在三国生效 | 版本节点 | whatsapp_templates_template_pausing_md |
| 四、生效日期与版本节点 | **2023-10-12** | 日期节点 | 模板暂停机制推广到全部企业 | 版本节点 | whatsapp_templates_template_pausing_md |
| 四、生效日期与版本节点 | **2024-07-01** | 日期节点 | 类别重判机制的一个生效节点 | 版本节点 | whatsapp_templates_template_categorization_md |
| 四、生效日期与版本节点 | **2024-10-01** | 日期节点 | 类别重判机制的另一个生效节点 | 版本节点 | whatsapp_templates_template_categorization_md |
| 四、生效日期与版本节点 | **2024-10-23** | 日期节点 | 此前创建的认证模板默认有效期例外 | 版本节点 | whatsapp_templates_time_to_live_md |
| 四、生效日期与版本节点 | **2024-11-01** | 日期节点 | 服务会话不限次数且免费 | 版本节点 | whatsapp_pricing_conversation_based_pricing_md |
| 四、生效日期与版本节点 | **2025-04-09** | 日期节点 | 类别复核周期的一个节点 | 版本节点 | whatsapp_templates_template_categorization_md |
| 四、生效日期与版本节点 | **2025-04-16** | 日期节点 | 类别复核周期的另一个节点 | 版本节点 | whatsapp_templates_template_categorization_md |
| 四、生效日期与版本节点 | **2025-06-01** | 日期节点 | 国际认证会话费率下调 | 版本节点 | whatsapp_pricing_conversation_based_pricing_md |
| 四、生效日期与版本节点 | **2025-07-01** | 日期节点 | 按对话计价被按条计价取代 | 版本节点 | whatsapp_pricing_md |
| 四、生效日期与版本节点 | **2025-10-07** | 日期节点 | 限额改按商业资产组合计算 | 版本节点 | whatsapp_upcoming_messaging_limits_changes_md |
| 四、生效日期与版本节点 | **2026 年 2 月** | 月份节点 | 旧限额字段移除 | 版本节点 | whatsapp_upcoming_messaging_limits_changes_md |
| 四、生效日期与版本节点 | **2026-10-15** | 日期节点 | 认证错误信号相关节点 | 版本节点 | whatsapp_templates_authentication_templates_error_signals_md |
| 四、生效日期与版本节点 | **2026 年 11 月** | 月份节点 | conversation_analytics 停用 | 版本节点 | whatsapp_analytics_md |
| 四、生效日期与版本节点 | **iOS 26** | 系统版本 | 键盘建议默认开启的起点 | 版本节点 | whatsapp_templates_authentication_templates_keyboard_suggestions_md |
| 四、生效日期与版本节点 | **Flagged 退役** | 状态节点 | 号码质量状态取消 | 条件触发项 | 896873687365001 |
| 四、生效日期与版本节点 | **2025-11-19** | 日期节点 | 营销消息接口正式可用 | 版本节点 | whatsapp_marketing_messages_overview_md |
| 四、生效日期与版本节点 | **2026-05-15** | 日期节点 | 最高单价功能有限公测 | 版本节点 | whatsapp_marketing_messages_pricing_md |
| 四、生效日期与版本节点 | **2026 年 10 月** | 月份节点 | 最高单价功能公开公测 | 版本节点 | whatsapp_marketing_messages_pricing_md |
| 四、生效日期与版本节点 | **2026-10-01** | 日期节点 | 费率页所依据的调价生效 | 版本节点 | whatsapp_pricing_md |
| 四、生效日期与版本节点 | **2026-10-13** | 日期节点 | 全球宣布停止支持云端接口的营销消息 | 版本节点 | whatsapp_marketing_messages_overview_md |
| 四、生效日期与版本节点 | **2027-04-13** | 日期节点 | 此后新建账号不能经云端接口发营销消息 | 版本节点 | whatsapp_marketing_messages_overview_md |
| 四、生效日期与版本节点 | **2027-10-13** | 日期节点 | 云端接口停止支持营销消息 | 版本节点 | whatsapp_marketing_messages_overview_md |
| 四、生效日期与版本节点 | **2027 年第二季** | 季度节点 | 最高单价功能正式可用并转为必需 | 版本节点 | whatsapp_marketing_messages_pricing_md |
| 四、生效日期与版本节点 | **2025-12-01** | 日期节点 | 消息、对话与计价分析的回看窗口上限缩短 | 版本节点 | whatsapp_analytics_md |
| 五、速率、比率与容量口径 | **9%** | 交付比例差 | 使用商业平台入口相对自建通道的营销交付提升 | 比率 | whatsapp_about_the_platform_md |
| 五、速率、比率与容量口径 | **3 倍** | 容量倍数 | 回调服务器应承受的出站处理能力 | 容量口径 | whatsapp_throughput_md |
| 五、速率、比率与容量口径 | **1 倍** | 容量倍数 | 回调服务器应承受的入站处理能力 | 容量口径 | whatsapp_throughput_md |
| 五、速率、比率与容量口径 | **250ms** | 中位延迟 | 回调投递的中位延迟上限 | 容量口径 | whatsapp_throughput_md |
| 五、速率、比率与容量口径 | **1%** | 超时比例 | 回调延迟超过 1 秒的比例上限 | 容量口径 | whatsapp_throughput_md |
| 五、速率、比率与容量口径 | **99 百分位** | 分位数 | 被节流的高吞吐活动在 1 小时内投完 | 比率 | whatsapp_templates_template_pacing_md |
| 五、速率、比率与容量口径 | **365** | 天 | 组合级节流的回看期 | 滚动窗口 | whatsapp_templates_portfolio_pacing_md |
| 五、速率、比率与容量口径 | **4^X** | 秒 | 限频后重试等待的指数式 | 模糊量 | whatsapp_about_the_platform_md |
| 五、速率、比率与容量口径 | **0.17** | 条/秒 | 同一收件人的等效速率 | 比率 | whatsapp_about_the_platform_md |
| 五、速率、比率与容量口径 | **600** | 条/小时 | 同一收件人的等效小时量 | 比率 | whatsapp_about_the_platform_md |
| 五、速率、比率与容量口径 | **5** | 条/页 | 模板列表查询的示例分页大小 | 默认可改项 | whatsapp_templates_template_management_md |
| 五、速率、比率与容量口径 | **5×47** | 类别乘市场 | 费率表与量能档位的维度规模 | 容量口径 | whatsapp_pricing_md |
| 五、速率、比率与容量口径 | **20 000—25 000** | token/条 | Meta 商业智能体消息的单条 token 消耗区间 | 区间或多值 | whatsapp_pricing_md |
| 五、速率、比率与容量口径 | **5** | 个消息类别 | 按条计价的类别数（含 Meta 商业智能体） | 容量口径 | whatsapp_pricing_md |
| 五、速率、比率与容量口径 | **16** | 类消息 | 客服窗口内可发送的服务消息类型数 | 容量口径 | whatsapp_messages_send_messages_md |
| 五、速率、比率与容量口径 | **14** | 个用例 | 实用模板"必需或关键"判定的官方用例数 | 容量口径 | whatsapp_templates_template_categorization_md |
| 五、速率、比率与容量口径 | **5** | 个用例类别 | 上述 14 个用例所归的类别数 | 容量口径 | whatsapp_templates_template_categorization_md |
| 五、速率、比率与容量口径 | **5** | 个营销目标 | 营销模板目标表的行数 | 容量口径 | whatsapp_templates_template_categorization_md |
| 五、速率、比率与容量口径 | **5** | 个实用目标 | 实用模板目标表的行数 | 容量口径 | whatsapp_templates_template_categorization_md |
| 五、速率、比率与容量口径 | **3** | 个阶段 | 最高单价功能分阶段推出 | 容量口径 | whatsapp_marketing_messages_pricing_md |
| 五、速率、比率与容量口径 | **1,000** | 次交付 | 最高单价的报价分母 | 容量口径 | whatsapp_marketing_messages_pricing_md |
| 五、速率、比率与容量口径 | **10 年→1 年** | 回看窗口 | 消息、对话与计价分析回看上限的变化 | 区间或多值 | whatsapp_analytics_md |
| 五、速率、比率与容量口径 | **7** | 个分析字段 | 分析接口可请求的字段数 | 容量口径 | whatsapp_analytics_md |
| **合计** | **137** 行／**90** 个去重条目 | **56** 种计数单位 | 五档分别对应限额档位、模板构成、时长窗口、版本节点、速率比率 | 性质列共 12 种 | 官方写成区间的行全部保留原式，见第 13.1—13.2 节 |

#ROWS=137 COLS=6

# 附表 C 档位逐档表（59 行）

本表把官方分散在各页里的**档位与阶梯**合并成一张表，列序即官方叙述顺序：**体系＝主语层级**（第 1.3 节），**档位＝该体系内的档位名**，触发列与后果列**逐字照抄或直译官方措辞**，时长列同样照抄；官方没给时长的登记为「未写明」并且不做推断（第 13.2 节）。共 58 档，按体系分布：限额档位 8／模板状态 5／消息有效期 5／号码质量评级 4／模板暂停实例 4／类别误用阶梯 4／逐用户营销限额 4／吞吐档位 3／客服窗口 3／模板归档 3／模板数量档位 3／模板编辑窗口 3／模板节流 3／应用请求速率 2／模板删除 2／模板质量评级 2；其中时长列为「未写明」的 **22** 档——本表的未写明只指时长这一格，全篇留白清单见附表 F。

| 编号 | 体系 | 档位 | 触发或达成条件 | 后果或效果 | 时长 | 出处页 |
|---|---|---|---|---|---|---|

| 1 | 限额档位 | 250（起始） | 新创建的商业资产组合 | 组合内所有业务号码共享该限额 | 未写明 | whatsapp_messaging_limits_md |
| 2 | 限额档位 | 2,000 | 完成三条升级路径之一 | 立即上调并通知邮件与开发者告警，触发 business_capability_update | 立即 | whatsapp_messaging_limits_md |
| 3 | 限额档位 | 10,000 | 满足自动扩档判据后升一级 | 组合限额上调一级 | 6 小时内 | whatsapp_messaging_limits_md |
| 4 | 限额档位 | 100,000 | 满足自动扩档判据后升一级 | 组合限额上调一级 | 6 小时内 | whatsapp_messaging_limits_md |
| 5 | 限额档位 | Unlimited | 满足自动扩档判据后升一级 | 组合限额不再设上限 | 未写明 | whatsapp_messaging_limits_md |
| 6 | 限额档位 | 换代结转 | 2025 年 10 月 7 日起改按组合计算 | 组合限额取组内号码历史最高档 | 未写明 | whatsapp_upcoming_messaging_limits_changes_md |
| 7 | 限额档位 | 降级（旧） | 号码质量连续 7 天为 Flagged | 限额下调一级 | 立即 | whatsapp_upcoming_messaging_limits_changes_md |
| 8 | 限额档位 | 降级（新） | 2025 年 10 月 7 日起质量下降 | 限额不再下调 | 未写明 | whatsapp_upcoming_messaging_limits_changes_md |
| 9 | 吞吐档位 | 80 mps（默认） | 每个注册业务号码 | 超出后返回错误码 130429 | 未写明 | whatsapp_about_the_platform_md |
| 10 | 吞吐档位 | 1,000 mps | 满足资格条件后自动升级 | 升级期间该号码在平台上不可用 | 最多 1 分钟 | whatsapp_throughput_md |
| 11 | 吞吐档位 | 20 mps（固定） | 号码同时用于商业应用与云端 API | 吞吐固定，不随升级变化 | 未写明 | whatsapp_throughput_md |
| 12 | 应用请求速率 | 200 次/小时（默认） | 每个应用对每个消息账号 | 超出后被限频 | 未写明 | whatsapp_about_the_platform_md |
| 13 | 应用请求速率 | 5,000 次/小时 | 已注册号码的活跃账号 | 按账号级额度返回 | 未写明 | whatsapp_about_the_platform_md |
| 14 | 模板数量档位 | 250 个模板 | 父商业资产组合未验证 | 每个消息账号的模板数上限 | 未写明 | whatsapp_templates_overview_md |
| 15 | 模板数量档位 | 6,000 个模板 | 组合已验证且至少一个号码有已批准显示名 | 每个消息账号的模板数上限提升 | 未写明 | whatsapp_templates_overview_md |
| 16 | 模板数量档位 | 100 个/小时 | 新建模板的速率 | 超出的创建请求不被接受 | 未写明 | whatsapp_templates_overview_md |
| 17 | 号码质量评级 | Green／High quality | 过去 7 天客户反馈 | 可据此获得更多能力的资格 | 7 天滚动窗口 | 896873687365001 |
| 18 | 号码质量评级 | Yellow／Medium quality | 过去 7 天客户反馈 | 悬停可查看屏蔽原因洞察 | 7 天滚动窗口 | 896873687365001 |
| 19 | 号码质量评级 | Red／Low quality | 过去 7 天客户反馈 | 悬停可查看屏蔽原因洞察 | 7 天滚动窗口 | 896873687365001 |
| 20 | 号码质量评级 | Flagged／Restricted（已停用） | 2025 年 10 月 7 日起 | 两种号码状态不再出现 | 未写明 | 896873687365001 |
| 21 | 模板质量评级 | Green／Yellow／Red | 模板消息在滚动 24 小时窗口内的接收情况 | 在质量评级列显示状态 | 24 小时滚动窗口 | 766346674749731 |
| 22 | 模板质量评级 | GREEN／YELLOW／RED／UNKNOWN | API 侧 quality_score 字段 | 返回分数与日期 | 未写明 | whatsapp_templates_template_quality_md |
| 23 | 模板状态 | Pending review | 创建模板后提交审核 | 审核中且尚未批准；决策最长 24 小时作出 | 最多 24 小时 | whatsapp_templates_template_review_md |
| 24 | 模板状态 | Approved | 审核通过 | 可在商业平台上发送 | 未写明 | 766346674749731 |
| 25 | 模板状态 | Rejected | 审核未通过 | 模板被拒绝 | 未写明 | 766346674749731 |
| 26 | 模板状态 | Flagged | 质量评级为低 | 警告状态；连续 7 天转为高或中则回到 Approved | 7 天连续 | 766346674749731 |
| 27 | 模板状态 | Disabled | 质量评级为低且 7 天内未改善 | 不能用于发送消息，也不能编辑 | 7 天内未改善 | 766346674749731 |
| 28 | 模板暂停实例 | 第 1 次 | 高退回率的第一次实例 | 模板暂停 3 小时 | 3 小时 | whatsapp_templates_template_pausing_md |
| 29 | 模板暂停实例 | 第 2 次 | 高退回率的第二次实例 | 模板暂停 6 小时 | 6 小时 | whatsapp_templates_template_pausing_md |
| 30 | 模板暂停实例 | 第 3 次 | 高退回率的第三次实例 | 模板被停用 | 未写明 | whatsapp_templates_template_pausing_md |
| 31 | 模板暂停实例 | 申诉 | 对被拒或被暂停的模板提出申诉 | 由人工复核并作出决定 | 24 小时内 | whatsapp_templates_template_pausing_md |
| 32 | 模板节流 | 实用模板节流 | 曾有一次实用模板被暂停后的 7 天内新建／恢复／非 GREEN 的模板 | 被暂扣的消息丢弃并回 132015 | 7 天 | whatsapp_templates_template_pacing_md |
| 33 | 模板节流 | 投递护栏 | 被节流的高吞吐活动 | 仍在 1 小时内投完（99 百分位） | 1 小时 | whatsapp_templates_template_pacing_md |
| 34 | 模板节流 | 组合级节流 | 滚动 365 天内合计低于 500K 条模板消息的企业 | 模板消息排队投递，超额返回 135000 | 365 天回看 | whatsapp_templates_portfolio_pacing_md |
| 35 | 类别误用阶梯 | Warning | 首次检测到实用模板误用 | 向消息账号管理员发出书面警告；此后实用转营销即时生效且不再预告 | Ongoing（持续） | whatsapp_templates_template_categorization_md |
| 36 | 类别误用阶梯 | Rate limiting | 误用继续 | 24 小时滚动窗口内对实用消息量封顶，超额消息被拒 | 最少 7 天，质量改善后解除 | whatsapp_templates_template_categorization_md |
| 37 | 类别误用阶梯 | Utility restriction | 误用继续 | 全部已批准实用模板改判为营销；禁止新建实用模板与类别复核申请 | 7 天（重复违规 30 天） | whatsapp_templates_template_categorization_md |
| 38 | 类别误用阶梯 | Business portfolio level restriction | 同一商务套件下多个消息账号持续误用 | 跨全部消息账号的实用模板改判为营销并禁止新建与复核 | 30 天 | whatsapp_templates_template_categorization_md |
| 39 | 逐用户营销限额 | 美国号码 | 收件人为美国电话号码 | 营销模板消息不投递 | 未写明 | whatsapp_templates_marketing_templates_per_user_limits_md |
| 40 | 逐用户营销限额 | 窗口内不计 | 用户回复开启 24 小时客服窗口 | 窗口内的营销消息不计入逐用户限额 | 24 小时 | whatsapp_templates_marketing_templates_per_user_limits_md |
| 41 | 逐用户营销限额 | 达到上限 | 该用户已收到当期营销消息上限 | 等待至少 24 小时后再试 | 至少 24 小时 | whatsapp_templates_marketing_templates_per_user_limits_md |
| 42 | 逐用户营销限额 | 反复重试 | 24 小时内多次重发 | 该营销消息对该用户不可用最多 24 小时并返回 131049 | 最多 24 小时 | whatsapp_templates_marketing_templates_per_user_limits_md |
| 43 | 客服窗口 | 开启 | 企业收到入站消息或来电 | 24 小时内可发送任意消息 | 24 小时 | whatsapp_messages_send_messages_md |
| 44 | 客服窗口 | 重置 | 用户再次发送消息 | 计时器重新开始 | 未写明 | whatsapp_messages_send_messages_md |
| 45 | 客服窗口 | 服务会话免费 | 2024 年 11 月 1 日起 | 可不限次数开启服务会话且不收费 | 未写明 | whatsapp_pricing_conversation_based_pricing_md |
| 46 | 消息有效期 | 默认（认证） | 认证模板消息 | 默认 10 分钟 | 10 分钟 | whatsapp_templates_time_to_live_md |
| 47 | 消息有效期 | 默认（其余） | 除认证模板之外的消息 | 默认 30 天 | 30 天 | whatsapp_messages_send_messages_md |
| 48 | 消息有效期 | 自定义区间 | 按类别三档分别可设 | 以 1 秒为步长设置 | 三档区间 | whatsapp_templates_time_to_live_md |
| 49 | 消息有效期 | 取值为 -1 | 认证与实用模板 | 设为 30 天的自定义有效期 | 30 天 | whatsapp_templates_time_to_live_md |
| 50 | 消息有效期 | 重分类清空 | 实用模板被改判为营销 | 系统清空自定义有效期（置为 null） | 未写明 | whatsapp_templates_time_to_live_md |
| 51 | 模板编辑窗口 | 已批准模板 | 30 天窗口内的编辑次数 | 最多 10 次 | 30 天窗口 | whatsapp_templates_template_management_md |
| 52 | 模板编辑窗口 | 已批准模板 | 24 小时窗口内的编辑次数 | 最多 1 次 | 24 小时窗口 | whatsapp_templates_template_management_md |
| 53 | 模板编辑窗口 | 被拒或被暂停模板 | 编辑次数 | 不限次数 | 未写明 | whatsapp_templates_template_management_md |
| 54 | 模板删除 | 删除已发送未送达的模板 | 模板已被用于发送但尚未送达 | 状态转为 PENDING_DELETION 并继续尝试投递 30 天 | 30 天 | whatsapp_templates_template_management_md |
| 55 | 模板删除 | 删除已批准模板 | 同名重建 | 30 天内不能用同名新建模板 | 30 天 | whatsapp_templates_template_management_md |
| 56 | 模板归档 | 不活跃归档 | 模板不活跃满 12 个月 | 平台自动归档 | 12 个月 | whatsapp_templates_template_archival_md |
| 57 | 模板归档 | 归档后删除 | 归档后未在窗口内取消归档 | 计划删除 | 28 天 | whatsapp_templates_template_archival_md |
| 58 | 模板归档 | 取消归档 | 在 28 天窗口内取消归档 | 恢复到归档前状态并重置不活跃计时 | 未写明 | whatsapp_templates_template_archival_md |
| **合计** | 16 个体系 | 58 档 | 触发列全部为官方原句或原句直译 | 后果列含封锁范围与恢复条件 | 时长列 36 档有官方措辞、**22** 档未写明 | 见第 2—12 节 |

#ROWS=58 COLS=7

# 附表 D 官方文档枚举取值域全表（135 行）

本表的取值域列是分组名，每组由解析脚本从指定页面的表格行、项目符号或段落结构中直接取出，取不到即中止；手定组的取值与依据原句同样逐字核验。说明列超过 150 字符时按整词截断（共 21 行截断），截断后仍是原文前缀。全表 23 组、134 个取值，来自 16 个页面。

| 编号 | 取值域 | 取值 | 官方说明或含义归纳 | 出处页 |
| --- | --- | --- | --- | --- |
| D1 | 升级申请结果 | `INCREASED_CAPABILITIES_ELIGIBILITY_DEFERRED` | Send 2,000 delivered messages outside of customer service windows to unique WhatsApp user phone numbers in a 30-day moving period, using templates ... | whatsapp_messaging_limits_md |
| D2 | 升级申请结果 | `INCREASED_CAPABILITIES_ELIGIBILITY_FAILED` | Send 2,000 delivered messages outside of customer service windows to unique WhatsApp user phone numbers within a 30-day moving period, using ... | whatsapp_messaging_limits_md |
| D3 | 升级申请结果 | `INCREASED_CAPABILITIES_ELIGIBILITY_NEED_MORE_INFO` | Verify your identity, or send 2,000 delivered messages outside of customer service windows ... | whatsapp_messaging_limits_md |
| D4 | 顶部屏蔽原因 | `NO_LONGER_NEEDED` |  | whatsapp_templates_template_comparison_md |
| D5 | 顶部屏蔽原因 | `NO_REASON` |  | whatsapp_templates_template_comparison_md |
| D6 | 顶部屏蔽原因 | `NO_REASON_GIVEN` |  | whatsapp_templates_template_comparison_md |
| D7 | 顶部屏蔽原因 | `NO_SIGN_UP` |  | whatsapp_templates_template_comparison_md |
| D8 | 顶部屏蔽原因 | `OFFENSIVE_MESSAGES` |  | whatsapp_templates_template_comparison_md |
| D9 | 顶部屏蔽原因 | `OTHER` |  | whatsapp_templates_template_comparison_md |
| D10 | 顶部屏蔽原因 | `OTP_DID_NOT_REQUEST` |  | whatsapp_templates_template_comparison_md |
| D11 | 顶部屏蔽原因 | `SPAM` |  | whatsapp_templates_template_comparison_md |
| D12 | 顶部屏蔽原因 | `UNKNOWN_BLOCK_REASON` |  | whatsapp_templates_template_comparison_md |
| D13 | 模板类别 | `Marketing templates` | Enable businesses to achieve a wide range of goals, from generating awareness to driving sales and retargeting customers. | whatsapp_templates_template_categorization_md |
| D14 | 模板类别 | `Utility templates` | Enable businesses to follow up on user actions or requests, since these messages are typically triggered by user actions. | whatsapp_templates_template_categorization_md |
| D15 | 模板类别 | `Authentication templates` | Enable businesses to verify a user's identity, potentially at various steps of the customer journey. | whatsapp_templates_template_categorization_md |
| D16 | 分析对话类别 | `AUTHENTICATION` |  | whatsapp_analytics_md |
| D17 | 分析对话类别 | `MARKETING` |  | whatsapp_analytics_md |
| D18 | 分析对话类别 | `SERVICE` |  | whatsapp_analytics_md |
| D19 | 分析对话类别 | `UTILITY` |  | whatsapp_analytics_md |
| D20 | 分析对话方向 | `BUSINESS_INITIATED` | Conversations initiated by the business. | whatsapp_analytics_md |
| D21 | 分析对话方向 | `USER_INITIATED` | Conversations initiated by an end user/customer. | whatsapp_analytics_md |
| D22 | 分析对话方向 | `UNKNOWN` | System cannot determine direction. | whatsapp_analytics_md |
| D23 | 会话计费类别 | `Marketing` | Enables you to achieve a wide range of goals, from generating awareness to driving sales and retargeting customers. Examples include new product, ... | whatsapp_pricing_conversation_based_pricing_md |
| D24 | 会话计费类别 | `Utility` | Enables you to follow-up on user actions or requests. Examples include opt-in confirmation, order/delivery management (for example, delivery update); ... | whatsapp_pricing_conversation_based_pricing_md |
| D25 | 会话计费类别 | `Authentication` | Enables you authenticate users with one-time pass codes, potentially at multiple steps in the login process (for example, account verification, ... | whatsapp_pricing_conversation_based_pricing_md |
| D26 | 会话计费类别 | `Service` | Enables you to resolve customer inquiries. | whatsapp_pricing_conversation_based_pricing_md |
| D27 | 分析对话类型 | `FREE_ENTRY_POINT` | Conversations originating from a [free entry ... | whatsapp_analytics_md |
| D28 | 分析对话类型 | `FREE_TIER` | Conversations within the monthly [free ... | whatsapp_analytics_md |
| D29 | 分析对话类型 | `REGULAR` | Any conversations that did not originate from a [free entry ... | whatsapp_analytics_md |
| D30 | Manager 模板状态 | `In-Review` | Indicates that the template is still under review. Review can take up to 24 hours. | whatsapp_templates_overview_md |
| D31 | Manager 模板状态 | `Rejected` | The template has been rejected during the review process or violates one or more policies. | whatsapp_templates_overview_md |
| D32 | Manager 模板状态 | `Active - Quality pending` | The message template has yet to receive quality feedback or read-rate information from customers. Message templates with this status can be sent to ... | whatsapp_templates_overview_md |
| D33 | Manager 模板状态 | `Active - High Quality` | The template has received little to no negative customer feedback. Message templates with this status can be sent to customers. | whatsapp_templates_overview_md |
| D34 | Manager 模板状态 | `Active - Medium Quality` | The template has received negative feedback from multiple customers, or low read-rates, but might soon become paused or disabled. Message templates ... | whatsapp_templates_overview_md |
| D35 | Manager 模板状态 | `Active - Low Quality` | The template has received negative feedback from multiple customers, or low read-rates. Message templates with this status can be sent to customers ... | whatsapp_templates_overview_md |
| D36 | Manager 模板状态 | `Paused` | The template has been paused due to recurring negative feedback from customers, or low read-rates. Message templates with this status cannot be sent ... | whatsapp_templates_overview_md |
| D37 | Manager 模板状态 | `Disabled` | The template has been disabled due to recurring negative feedback from customers. Message templates with this status cannot be sent to customers. | whatsapp_templates_overview_md |
| D38 | Manager 模板状态 | `Appeal Requested` | Indicates that an appeal has been requested. | whatsapp_templates_overview_md |
| D39 | 类别误用处置档 | `Warning` | A written warning is sent to Messaging account admins. After a warning, utility-to-marketing category changes become instant with no advance notice. | whatsapp_templates_template_categorization_md |
| D40 | 类别误用处置档 | `Rate limiting` | Utility message volume on the Messaging account is capped within a 24-hour rolling window. Messages exceeding the cap are rejected. Marketing and ... | whatsapp_templates_template_categorization_md |
| D41 | 类别误用处置档 | `Utility restriction` | All approved utility templates on the Messaging account are recategorized to `MARKETING`. New utility template creation and [category ... | whatsapp_templates_template_categorization_md |
| D42 | 类别误用处置档 | `Business portfolio level restriction` | If misuse persists across multiple Messaging accounts under the same Meta Business Suite, all approved utility templates across all Messaging ... | whatsapp_templates_template_categorization_md |
| D43 | 消息限额档位 | `2,000` | by completing a scaling path | whatsapp_messaging_limits_md |
| D44 | 消息限额档位 | `10,000` | via automatic scaling | whatsapp_messaging_limits_md |
| D45 | 消息限额档位 | `100,000` | via automatic scaling | whatsapp_messaging_limits_md |
| D46 | 消息限额档位 | `Unlimited` | via automatic scaling | whatsapp_messaging_limits_md |
| D47 | 窗口内消息类型 | `Address messages` | allow you to request a delivery address from WhatsApp users. | whatsapp_messages_send_messages_md |
| D48 | 窗口内消息类型 | `Audio messages` | display an audio icon and a link to an audio file. When the WhatsApp user taps the icon, the WhatsApp client loads and plays the audio file. | whatsapp_messages_send_messages_md |
| D49 | 窗口内消息类型 | `Contacts messages` | allow you to send rich contact information directly to WhatsApp users, such as names, phone numbers, physical addresses, and email addresses. | whatsapp_messages_send_messages_md |
| D50 | 窗口内消息类型 | `Document messages` | display a document icon, linked to a document that a WhatsApp user can tap to download. | whatsapp_messages_send_messages_md |
| D51 | 窗口内消息类型 | `Image messages` | display a single image and an optional caption. | whatsapp_messages_send_messages_md |
| D52 | 窗口内消息类型 | `Interactive CTA URL button messages` | allow you to map any URL to a button, so you don't have to include lengthy or obscure raw URLs in the message body. | whatsapp_messages_send_messages_md |
| D53 | 窗口内消息类型 | `Interactive voice call messages` | allow you to trigger a WhatsApp call from users. | whatsapp_messages_send_messages_md |
| D54 | 窗口内消息类型 | `Interactive Flow messages` | allow you to send structured messages that are more natural or comfortable for your customers. For example, you can use WhatsApp Flows to book ... | whatsapp_messages_send_messages_md |
| D55 | 窗口内消息类型 | `Interactive list messages` | allow you to present WhatsApp users with a list of options to choose from. | whatsapp_messages_send_messages_md |
| D56 | 窗口内消息类型 | `Interactive location request messages` | display body text and a send location button. When a WhatsApp user taps the button, a location sharing screen appears which the user can use to share ... | whatsapp_messages_send_messages_md |
| D57 | 窗口内消息类型 | `Interactive reply buttons` | messages allow you to send up to three predefined replies for users to choose from. | whatsapp_messages_send_messages_md |
| D58 | 窗口内消息类型 | `Location messages` | allow you to send a location's latitude and longitude coordinates to a WhatsApp user. | whatsapp_messages_send_messages_md |
| D59 | 窗口内消息类型 | `Sticker messages` | display animated or static sticker images in a WhatsApp message. | whatsapp_messages_send_messages_md |
| D60 | 窗口内消息类型 | `Text messages` | are messages containing only a text body and an optional link preview. | whatsapp_messages_send_messages_md |
| D61 | 窗口内消息类型 | `Video messages` | display a thumbnail preview of a video image with an optional caption. When the WhatsApp user taps the preview, it loads the video and displays it to ... | whatsapp_messages_send_messages_md |
| D62 | 窗口内消息类型 | `Reaction messages` | are emoji-reactions that you can apply to a previous WhatsApp user message that you have received. | whatsapp_messages_send_messages_md |
| D63 | 分析指标类型 | `COST` | Includes approximate charges for that time range, in the Messaging account's currency. | whatsapp_analytics_md |
| D64 | 分析指标类型 | `CONVERSATION` | Includes the count of conversations for that time range. | whatsapp_analytics_md |
| D65 | 分析消息对象 | `0` | for template messages sent to WhatsApp users | whatsapp_analytics_md |
| D66 | 分析消息对象 | `2` | for non-templates messages sent to WhatsApp users | whatsapp_analytics_md |
| D67 | 分析消息对象 | `100` | for incoming messages sent from WhatsApp users to you | whatsapp_analytics_md |
| D68 | Manager 质量标签 | `Quality pending` | equates to an `UNKNOWN` quality score | whatsapp_templates_template_quality_md |
| D69 | Manager 质量标签 | `High quality` | equates to a `GREEN` quality score | whatsapp_templates_template_quality_md |
| D70 | Manager 质量标签 | `Medium quality` | equates to a `YELLOW` quality score | whatsapp_templates_template_quality_md |
| D71 | Manager 质量标签 | `Low quality` | equates to a `RED` quality score | whatsapp_templates_template_quality_md |
| D72 | API 模板质量分 | `GREEN` | Indicates high quality. The template has received little to no negative feedback from WhatsApp users. The template can be sent. | whatsapp_templates_template_quality_md |
| D73 | API 模板质量分 | `YELLOW` | Indicates medium quality. The template has received negative feedback from multiple WhatsApp users, or low read-rates, and may soon become paused or ... | whatsapp_templates_template_quality_md |
| D74 | API 模板质量分 | `RED` | Indicates low quality. The template has received negative feedback from multiple WhatsApp users, or low read-rates. The template can be sent, but is ... | whatsapp_templates_template_quality_md |
| D75 | API 模板质量分 | `UNKNOWN` | Indicates a quality score is still pending, because it has yet to receive WhatsApp user feedback or read-rate data. The template can be sent. | whatsapp_templates_template_quality_md |
| D76 | 误用限制类型 | `RESTRICTED_UTILITY_TEMPLATES` | Utility Template Suspension | whatsapp_templates_template_categorization_md |
| D77 | 误用限制类型 | `RATE_LIMITED_UTILITY_TEMPLATE_MESSAGING` | Utility Messages Rate Limited | whatsapp_templates_template_categorization_md |
| D78 | 误用 webhook 事件 | `UTILITY_TEMPLATE_ABUSE` | Warning | whatsapp_templates_template_categorization_md |
| D79 | 误用 webhook 事件 | `UTILITY_TEMPLATE_ABUSE` | Utility Template Suspension | whatsapp_templates_template_categorization_md |
| D80 | 误用 webhook 事件 | `UTILITY_TEMPLATE_ABUSE_UNBAN` | Utility Template Suspension Removed | whatsapp_templates_template_categorization_md |
| D81 | 误用 webhook 事件 | `UTILITY_TEMPLATE_ABUSE_RATE_LIMIT` | Utility Messages Rate Limited | whatsapp_templates_template_categorization_md |
| D82 | 误用 webhook 事件 | `UTILITY_TEMPLATE_ABUSE_RATE_LIMIT_RECOVERY` | Utility Messages Rate Limit Removed | whatsapp_templates_template_categorization_md |
| D83 | 模板分析指标 | `COST` |  | whatsapp_analytics_md |
| D84 | 模板分析指标 | `CLICKED` |  | whatsapp_analytics_md |
| D85 | 模板分析指标 | `DELIVERED` |  | whatsapp_analytics_md |
| D86 | 模板分析指标 | `READ` |  | whatsapp_analytics_md |
| D87 | 模板分析指标 | `SENT` |  | whatsapp_analytics_md |
| D88 | 模板分析指标 | `APP_ACTIVATIONS (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D89 | 模板分析指标 | `APP_ADD_TO_CART (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D90 | 模板分析指标 | `APP_CHECKOUTS_INITIATED (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D91 | 模板分析指标 | `APP_PURCHASES (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D92 | 模板分析指标 | `APP_PURCHASES_CONVERSION_VALUE (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D93 | 模板分析指标 | `WEBSITE_ADD_TO_CART (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D94 | 模板分析指标 | `WEBSITE_CHECKOUTS_INITIATED (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D95 | 模板分析指标 | `WEBSITE_PURCHASES (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D96 | 模板分析指标 | `WEBSITE_PURCHASES_CONVERSION_VALUE (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D97 | 模板分析指标 | `APP_ADD_PAYMENT_INFO (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D98 | 模板分析指标 | `APP_ADD_TO_WISHLIST (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D99 | 模板分析指标 | `APP_COMPLETE_REGISTRATION (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D100 | 模板分析指标 | `APP_LEVEL_ACHIEVED (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D101 | 模板分析指标 | `APP_OTHER (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D102 | 模板分析指标 | `APP_RATE (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D103 | 模板分析指标 | `APP_SEARCH (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D104 | 模板分析指标 | `APP_TUTORIAL_COMPLETION (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D105 | 模板分析指标 | `APP_VIEW_CONTENT (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D106 | 模板分析指标 | `WEBSITE_ADD_PAYMENT_INFO (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D107 | 模板分析指标 | `WEBSITE_ADD_TO_WISHLIST (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D108 | 模板分析指标 | `WEBSITE_COMPLETE_REGISTRATION (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D109 | 模板分析指标 | `WEBSITE_CUSTOM (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D110 | 模板分析指标 | `WEBSITE_LEAD (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D111 | 模板分析指标 | `WEBSITE_SEARCH (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D112 | 模板分析指标 | `WEBSITE_VIEW_CONTENT (MM API for WhatsApp only)` |  | whatsapp_analytics_md |
| D113 | 媒体头格式 | `IMAGE` | 媒体头 `<FORMAT>` 占位符只允许这四个取值。 | whatsapp_templates_components_md |
| D114 | 媒体头格式 | `VIDEO` | 媒体头 `<FORMAT>` 占位符只允许这四个取值。 | whatsapp_templates_components_md |
| D115 | 媒体头格式 | `GIF` | 媒体头 `<FORMAT>` 占位符只允许这四个取值。 | whatsapp_templates_components_md |
| D116 | 媒体头格式 | `DOCUMENT` | 媒体头 `<FORMAT>` 占位符只允许这四个取值。 | whatsapp_templates_components_md |
| D117 | 模板组件 | `header` | 创建模板时定义的四类主组件之一；四类里只有正文是必填。 | whatsapp_templates_components_md |
| D118 | 模板组件 | `body` | 创建模板时定义的四类主组件之一；四类里只有正文是必填。 | whatsapp_templates_components_md |
| D119 | 模板组件 | `footer` | 创建模板时定义的四类主组件之一；四类里只有正文是必填。 | whatsapp_templates_components_md |
| D120 | 模板组件 | `buttons` | 创建模板时定义的四类主组件之一；四类里只有正文是必填。 | whatsapp_templates_components_md |
| D121 | 参数格式 | `named` | 参数格式之一：小写字母与下划线组成、包在双花括号里的唯一字符串。 | whatsapp_templates_overview_md |
| D122 | 参数格式 | `positional` | 参数格式之一：创建时不指定格式，即按位置默认匹配。 | whatsapp_templates_overview_md |
| D123 | 帮助中心模板状态 | `Pending review` | 帮助中心状态之一：审核中，尚未批准。 | 766346674749731 |
| D124 | 帮助中心模板状态 | `Approved` | 帮助中心状态之一：已批准，可在商业平台上发送。 | 766346674749731 |
| D125 | 帮助中心模板状态 | `Rejected` | 帮助中心状态之一：审核未通过。 | 766346674749731 |
| D126 | 帮助中心模板状态 | `Flagged` | 帮助中心状态之一：质量评级低时的警告态，连续 7 天回到高或中即恢复 Approved。 | 766346674749731 |
| D127 | 帮助中心模板状态 | `Disabled` | 帮助中心状态之一：质量低且 7 天内未改善，期间不能发送也不能编辑。 | 766346674749731 |
| D128 | 投递失败错误码 | `130429` | 超出当前吞吐档位时接口返回的错误码，回到允许水平前持续返回。 | whatsapp_throughput_md |
| D129 | 投递失败错误码 | `131035` | 无存储模式下入站消息回调投递失败时，改由错误消息回调返回的码。 | whatsapp_no_storage_md |
| D130 | 投递失败错误码 | `131049` | 逐用户营销限额已用尽，或对该用户反复重发被拒时回调里的码。 | whatsapp_templates_marketing_templates_per_user_limits_md |
| D131 | 投递失败错误码 | `131056` | 对同一收件人超过速率限制时触发的码。 | whatsapp_about_the_platform_md |
| D132 | 投递失败错误码 | `131057` | 号码正处于吞吐升级的不可用窗口内，用它发请求时返回的码。 | whatsapp_throughput_md |
| D133 | 投递失败错误码 | `132015` | 模板节流期间因质量信号为负而被丢弃的每条消息所触发的码（云端接口）。 | whatsapp_templates_template_pacing_md |
| D134 | 投递失败错误码 | `135000` | 组合级节流的批间隔反馈指向可疑活动时，剩余被扣留消息逐条丢弃所触发的码。 | whatsapp_templates_portfolio_pacing_md |
| **合计** | 23 组 | 134 个取值 | — | 16 页 |

#ROWS=134 COLS=5

# 附表 E 认证类、实用类、营销类模板逐项对照（11 行）

本表按官方三分类逐维度并排：前三行由脚本从 TTL 配置页的对照表直接解析（维度名未登记即中止），其余行的每个单元格都是所引页面的逐字原句，核验不过即记入失败清单；标作"官方未按类别区分"的 6 格是该页面没有按类别写差异，不代表该类别没有该项规则。

| 编号 | 对照维度 | 认证类模板 | 实用类模板 | 营销类模板 |
| --- | --- | --- | --- | --- |
| E1 | 默认消息有效期 | 10 minutes ／  ／ 30 days for authentication templates created before October 23, 2024 | 30 days | 30 days |
| E2 | 可自定义的接口 | Cloud API | Cloud API only | Marketing Messages API for WhatsApp |
| E3 | 可自定义的范围 | 30 seconds to 15 minutes | 30 seconds to 12 hours | 12 hours to 30 days |
| E4 | 秒数取值区间 | `30` to `900` seconds | `30` to `43200` seconds | `43200` to `2592000` |
| E5 | 会话开启方式 | conversations can only be opened with template messages | conversations can only be opened with template messages | conversations can only be opened with template messages |
| E6 | 按条计费的单价性质（2025-07-01 起） | Utility, authentication, and service: Fixed, per published rates | Utility, authentication, and service: Fixed, per published rates | Marketing: For Cloud API, fixed, per published rates |
| E7 | 同一用户可收条数 | 官方未按类别区分 | 官方未按类别区分 | WhatsApp may limit the number of marketing template messages a WhatsApp user receives from any business in a ... |
| E8 | 美国号码可达性 | 官方未按类别区分 | 官方未按类别区分 | WhatsApp does not currently deliver marketing template messages to WhatsApp users with United States phone ... |
| E9 | 触达上限后的重发间隔 | 官方未按类别区分 | 官方未按类别区分 | wait at least 24 hours before attempting to resend a marketing template message |
| E10 | 发送前提 | required to obtain opt-in before messaging people on WhatsApp | required to obtain opt-in before messaging people on WhatsApp | required to obtain opt-in before messaging people on WhatsApp |
| **合计** | 10 个维度 | 解析 3 行 | 手定 7 行 | 未按类别区分 6 格 |

#ROWS=10 COLS=5

# 附表 F 口径冲突与官方未写明处（18 行）

本表登记同一议题在不同官方页面（或同页两处）之间的措辞差异：两条口径都逐字核验后再并列，"本篇采用"列说明本文按哪一条叙述、另一条如何使用。共 17 项，其中 3 项一侧为官方未写明。

| 编号 | 议题 | 口径甲 | 口径乙 | 差异性质 | 本篇采用 |
| --- | --- | --- | --- | --- | --- |
| F1 | 质量观察窗口 | 「over the past seven days, weighted by recency」（whatsapp_messages_send_messages_md） | 「rolling window of the past 24 hours」（766346674749731） | 七日加权与 24 小时滚动并存 | 号码质量按七日加权、模板质量按 24 小时滚动，分别记数不合并 |
| F2 | 低质量恢复条件 | 「improves to a high or medium for 7 continuous days」（766346674749731） | 「improved within 7 days」（766346674749731） | 同页两种表述 | 按同页更完整的「连续 7 天」记，另一句只作旁证 |
| F3 | Flagged 状态是否仍现行 | 「Flagged: The status occurs when the quality rating is low」（766346674749731） | 「Flagged and Restricted have been discontinued as of Oct 7th, 2025」（896873687365001） | 页面更新不同步 | 以停用日期为准，Flagged 只作历史状态叙述 |
| F4 | 模板状态取值个数 | 「There is different status for the templates」（766346674749731） | 「The [Manage templates](https://business.facebook.com/latest/whatsapp_manager/message_templates) panel in WhatsApp Manager also displays template statuses」（whatsapp_templates_overview_md） | 层级不同 | 帮助中心 5 项、Manager 面板 9 项分列附表 D，不做并集 |
| F5 | 限额字段口径 | 「The `messaging_limit_tier` field, which used to return a business phone number's messaging limit, has been deprecated」（whatsapp_messaging_limits_md） | 「To get a business phone number's current messaging limit, request the `messaging_limit_tier` field」（whatsapp_upcoming_messaging_limits_changes_md） | 迁移期两套指引 | 以限额页的「已弃用、改取组合级字段」为准 |
| F6 | 计费模型 | 「which was replaced by per-message pricing on July 1, 2025」（whatsapp_pricing_conversation_based_pricing_md） | 「Service conversations are now free」（whatsapp_pricing_conversation_based_pricing_md） | 同页标注弃用 | 按 2025-07-01 起的按条计费叙述，免费只作历史条款 |
| F7 | 限额归属层级 | 「Messaging limits are calculated and set at the business portfolio level」（whatsapp_messaging_limits_md） | 「highest limit of any phone number within their portfolio」（whatsapp_upcoming_messaging_limits_changes_md） | 对象不同易混 | 正文区分「组合级限额」与「号码级可达上限」两个层级 |
| F8 | 模板类别数量 | 「Each template must be categorized as」（whatsapp_templates_overview_md） | 「Conversations are categorized with one of the following categories」（whatsapp_pricing_conversation_based_pricing_md） | 模板三分类、会话四分类 | 附表 D 两组分列，正文不互换 |
| F9 | 服务类会话是否收费 | 「you can open an unlimited number of service conversations at no charge」（whatsapp_pricing_conversation_based_pricing_md） | 「Utility, authentication, and service: Fixed, per published rates」（whatsapp_pricing_md） | 弃用页与现行页相反 | 按现行按条计费页记服务类单价，免费条款只作 2024-11-01 起的历史口径 |
| F10 | 升级达标口径 | 「Send 2,000 delivered messages outside of customer service windows to unique WhatsApp user phone numbers in a 30-day moving period」（whatsapp_messaging_limits_md） | 「Send 2,000 delivered messages outside of customer service windows to unique WhatsApp user phone numbers within a 30-day moving period」（whatsapp_messaging_limits_md） | 同页介词不一致 | 两种介词各记一次，达标口径按「in/within 30 天滚动」合并叙述 |
| F11 | 审核时限 | 「It can take up to 24 hours for an approval decision to be made」（whatsapp_templates_template_review_md） | 「Review can take up to 24 hours」（whatsapp_templates_overview_md） | 两处同值 | 按 24 小时上限记，注明两处独立出处 |
| F12 | 自动缩放首个档位 | 「10,000 (via [automatic scaling](#automatic-scaling))」（whatsapp_messaging_limits_md） | 「You can increase your messaging limit to 2,000 by completing one of the」（whatsapp_messaging_limits_md） | 阶梯与路径分写 | 正文先记 2,000 的手动路径，再记 10,000 起的自动阶梯 |
| F13 | 自动扩档的利用率门槛 | 「More than 50% of your current limit was used in the last 7 days」（896873687365001） | 「your business has utilized at least half of your current messaging limit」（whatsapp_messaging_limits_md） | 同一判据两种写法 | 按开发者文档的「至少一半」叙述，帮助中心的 50% 只作对照，不合并成第三句 |
| F14 | 起点 250 是否算档位 | 「Newly created business portfolios have a messaging limit of 250」（whatsapp_messaging_limits_md） | 「There are 4 business-initiated conversation limits you can reach after scaling」（896873687365001） | 计数口径不同 | 把 250 记为起点、4 档记为晋级目标，两数不相加也不互换 |
| F15 | 模板节流的触发阈值 | 「until an unspecified threshold is reached」（whatsapp_templates_template_pacing_md） | 「官方未写明」（—） | 官方自陈未公开 | 只记节流机制与三种走向，不推断阈值数值 |
| F16 | 组合级节流的适用范围 | 「This feature is being released gradually over the coming weeks so may not apply to you immediately」（whatsapp_templates_portfolio_pacing_md） | 「官方未写明」（—） | 灰度发布、按账号而异 | 写明生效范围不稳定，不写成全量可用 |
| F17 | 逐用户营销限额的具体条数 | 「WhatsApp may limit the number of marketing template messages a WhatsApp user receives from any business in a given period of time」（whatsapp_templates_marketing_templates_per_user_limits_md） | 「官方未写明」（—） | 只给机制不给数值 | 记「某段时间内」这一原始措辞，不折算成条数 |
| **合计** | 17 项 | — | — | 未写明 3 项 | 全部逐字核验 |

#ROWS=17 COLS=6

---

**边界声明：** 本篇是一份**官方规则文本的口径梳理**，不是操作手册，也不是对平台规则合理性的评价。全篇内容限于 WhatsApp 与 Meta 公开页面已经写出的表述：Cloud API 的 messages send-messages 与十四个消息类型页、about-the-platform 的三类限额与成对速率、messaging-limits 与其换代说明、throughput、templates 下的 overview／components／review／management／archival／groups／migration／comparison／media／library／supported-languages／time-to-live／template-quality／pausing／pacing／business-portfolio-pacing／categorization、marketing-templates 与 utility-templates 与 authentication-templates 三组子页、per-user marketing limits、block-users、analytics、pricing 与 conversation-based-pricing、local-storage 与 no-storage 与 data-privacy-and-security、marketing-messages 的九个页面，加上帮助中心六条质量与限额问答页与四条法律政策页。引用这些文本的目的只有两个：说明平台在公开规则里把一条消息**归入哪一类、允许长成什么样、由哪几套状态与评级描述**；说明平台把**能发多少人、多快、多久**写成哪几道闸，以及每一道闸的公开数字与公开留白。本篇**不含**批量发送脚本、发送自动化搭建、账号池、多号轮换、设备农场、限速或频控规避、垃圾信息检测绕过、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法建议；**不含**任何可用于向非自愿收件人大量投递消息的方法，也**不含**检测与规避的实现方式；**不含**利用代码、恶意样本或利用链构造方式；**不含**任何针对他人设备或他人账号的操作步骤与时序。凡官方写成区间（`30` to `900`、`43200` to `2592000`、`1 or 3 day`）、模糊量（`about`、`a few days`）、两种写法并存（`at least half` 与 `More than 50%`）或主动标注 `unspecified` 之处，本篇原样保留并把该处登记为留白，不推测阈值、不给出单值、不以其他平台惯例补齐。三套"停"之间、四套状态与评级枚举之间、六道限额与速率闸之间，官方从未给出对齐表，本篇不做出任何跨体系的等价推断。本次未能取回正文的 2 个页面在附表 A 逐行标注，其内容未被引用。

Telegram: https://t.me/one00190
