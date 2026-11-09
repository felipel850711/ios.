@@TITLE@@WhatsApp 群组／社区／频道／广播列表的治理开关与规模上限：谁能进、谁能说、谁能改、改不动时官方写了什么——官方口径解读
@@SUBTITLE@@本篇是"WhatsApp 群发／群控五篇"中的第四篇（轴④），接在《自动化接入面》之后，讲的是**平台把四类载体的"谁能进、谁能说、谁能改、改不动时官方写了什么"落到哪些页面与哪些控件上**。全篇只读官方文本，共引用 **126** 个页面登记位（**122** 页正文已取回可引用、**4** 页不可引用且全篇不引用，详见附表 A 的 **127** 行含合计）：WhatsApp 帮助中心 **104** 页、Meta 开发者文档 **18** 页（Groups API 8 页、Groups API reference 5 页、Webhook reference 1 页、opt-in 规则 1 页、商业资料 1 页、政策执法 1 页、数据与隐私 1 页）、法律与政策页 **4** 页。
@@INFO@@报告日期|2026 年 11 月 9 日
@@INFO@@分析对象|按载体清点本篇引用的页面构成：WhatsApp 帮助中心 **104** 页；Meta 开发者文档 Groups API **8** 页 ＋ Groups API reference **5** 页 ＋ Webhook reference **1** 页 ＋ opt-in 规则 **1** 页 ＋ 商业资料 **1** 页 ＋ 政策执法 **1** 页 ＋ 数据与隐私 **1** 页；法律与政策页 **4** 页。合计 **126** 个登记位，其中可引用 **122** 页、不可引用 **4** 页（3 页正文未取回、1 页只有地域限定提示，全篇不引用，只在附表 F 登记这一条事实）。
@@INFO@@视角定位|官方规则文本与帮助中心控件说明的**口径解读**，不是操作手册。本篇**不提供**批量发送脚本、账号池与多号轮换、限速与频控规避、反垃圾／反检测绕过、封禁申诉话术、非官方客户端；凡官方写成"步骤"的内容，本篇只登记步骤的名称与数量，不重述每一步怎么做（界线见 1.4）。
@@INFO@@分类|**官方页面登记 126 个登记位 × 站点 × 引用位置 × 取回状态**（附表 A，**127** 行含合计；可引用 **122** 页／不可引用 **4** 页；站点分布 帮助中心 **104**、Groups API **8**、Groups API reference **5**、法律与政策页 **4**、Webhook reference **1**、opt-in 规则 **1**、商业资料 **1**、政策执法 **1**、数据与隐私 **1**）× **数字归档 99 行，五档 53／7／26／4／9，去重 57 个数字、41 种计数单位、43 种性质**（附表 B）× **治理开关矩阵 58 行、7 类载体、5 类写明度，默认值或可改方留白 16 行；写明默认值与可改方 18／只写方向（默认值未写明）15／无开关（行为固定）19／以规模或身份为条件 3／写明但互相冲突 3**（附表 C）× **枚举取值 97 个、27 组、覆盖 14 页，「Values can be:」脚本解析 5 组 11 个取值，错误码 15、回调字段 4、回调子场景 20、HTTP 状态 4，说明截断 6 行**（附表 D）× **四类载体逐项对照 30 个维度、逐字原句 76 格、官方未按载体区分 44 格、覆盖 48 页；载体分布 群组 24／社区 19／频道 19／广播列表 14**（附表 E）× **口径冲突与官方未写明 30 项、留白 7 格、未取回页 4 行、差异性质 19 类、原句最长 219 字符、截断 0 格、整格照抄 1 格**（附表 F）× **附表原句逐字回核 402 条，失败 0 条**。
@@INFO@@难度|进阶（本篇有四处容易读错。**第一是 1,024 与 2,000 分属不同容器**：前者是单个群组与社区总群的上限，后者是社区连同公告组的上限，两者不能相加也不能互推。**第二是"默认值"与"随规模自动降权"是两回事**：官方在一页写 `Edit group settings` 的通用默认，另一页写群人数越过 256 时该控件落到"仅管理员"一档，后者是条件触发的档位切换，不是新的默认值。**第三是同一数值在不同页面带不带千分位**：1,024 与 1024、2,000 与 2000 是同一件事的两种写法，本篇按写法分别登记（附表 B、附表 F）。**第四是消费者端开关与 Groups API 字段不是一根轴**：界面上的 `Approve new members` 与接口里的 `join_approval_mode` 各有一套默认值与取值，本篇并列不合并，也不据接口页推断界面行为。）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

本篇只读官方文本：帮助中心的载体说明页与控件页、开发者文档的 Groups API 与其 reference 页、4 页法律与政策文本。官方没写的地方本篇标注"官方未写明"，缺口登记进附表 F，不用第三方口径补齐、不做推测。下面 15 个问题覆盖 1—5 节的 24 个二级小节，一个问题可对应相邻两小节；跨小节关联只做中文指认，不跨页拼接原句。

1. 三类来源各是什么语体，为什么不能混读？（1.1）
2. 中文口径与官方控件名怎么对齐，官方自己有没有换词？（1.2）
3. 群组、社区、频道、广播列表各被定成什么，治理身份分几种？（1.3）
4. 本篇不做什么，官方写成"步骤"的内容登记到什么颗粒度？（1.4）
5. 一个群组能装多少人，各页写法是否同一档？（2.1）
6. 社区的上限挂在哪几个容器上，能不能互推？（2.2）
7. 频道有没有写明的受众上限，唯一硬数字挂在哪？（2.3、4.4）
8. 广播列表的联系人上限与条数上限是不是同一件事？（2.4）
9. 接口侧的数字与界面侧的数字是什么关系？（2.5）
10. 进群有哪几种官方入口，链接与二维码是一套还是两套权限？（3.1、3.2）
11. 加入请求的开关名、默认档与可撤销性各写在哪页？（3.3）
12. 手动加减成员时界面会给哪些规模与权限提示？（3.4）
13. 退群、被移出与再加入之间有哪些等待期与可见性？（3.5）
14. 新成员能否看到历史消息，谁能关掉这一能力？（3.6）
15. 三类管理员的上限与不可动身份写在哪页，五组开关又有几处没写默认值？（4.1—4.4、5.1—5.5）

一句话读法：每条结论都跟着一个页名走——先列页名，再整句引出该页英文原句，最后用中文说这条口径覆盖哪一类载体、有没有写明默认值与可改方；要复核就按页名回到附表 A 找登记位。

# 1 来源层、术语与四件载体

第 1 节先把三件事立起来：用什么文本说话（1.1）、用什么词说话（1.2）、说的是哪四件载体（1.3），最后划出本篇不越过的线（1.4）。

## 1.1 三类来源与各自的语体

本篇把来源分三层，语体差别直接决定能引到什么。**帮助中心**面向使用者，写法是"控件名＋开关方向＋默认值"，一个数字常只在一页出现。**开发者文档**面向集成方，写法是"前提条件＋接口能力＋表格里的数字"，说的是程序化创建的那一套容器。**法律与政策页**只写义务与定性，不写控件名，也不写档位。

最容易被混读的是第二层。《Get started with Groups API》通篇是接入语气：`When you create a group, a unique invite link that is generated which you can share to potential group participants.`，接入之后才有发送资格：`Once a user joins the group, a webhook is triggered, signaling that you are now eligible to send messages to the group.`。同一页把资格并列成前提，其一是 `Your business number is in use with Cloud API (not the WhatsApp Business app).`，其二是 `Your app is subscribed to the Messaging account of your business phone number.`。这些句子讲的是接入条件而不是治理开关，本篇不据它们推断界面上的任何档位；接口侧的数字单独登记在 2.5。

## 1.2 中英术语对照：中文口径与官方英文

中文口径全篇固定：群组＝group、社区＝community、公告组＝announcement group、总群＝general chat、频道＝channel、广播列表＝broadcast list；控件名与开关名保留官方英文、不译。下表登记本篇最易混用的几组词，第二列记的是官方自己换词或不分层的地方。

| 中文口径 | 官方写法 | 出处层 |
|---|---|---|
| 群组 | group、group chat | 帮助中心与法律页同词 |
| 公告组／总群 | announcement group、general chat | 帮助中心，两词不互用 |
| 成员 | member（界面）、participant（接口） | 帮助中心、开发者文档 |
| 管理员与所有者 | group admin、community admin、channel admin、channel owner | 限定词不同即不同身份，owner 只在频道出现 |
| 发言与准入开关 | Send new messages、Edit group settings、Approve new members | 一律保留英文原名 |

法律层的用词比帮助中心宽。《WhatsApp Business Messaging Guidelines》一次列全适用范围：`These Messaging Guidelines (these “Guidelines”) apply to chats, calls, group chats, status updates, and communities.`——两份清单里都没有 broadcast list，频道另有自己的两页法律文本（1.3）。层级关系在同页写死：`Use of the WhatsApp Messenger Application is governed by our Terms of Service and these Guidelines.`。《WhatsApp Terms of Service》则是安全与完整性语体，不逐条给数字，例如 `We work to protect the safety, security, and integrity of our Services.`。本篇据此定下取数规则：数字只从帮助中心与开发者文档取，义务只从法律页取，两类页面不互为证据。

## 1.3 四件载体的官方命名与定性

四件载体各有自己的"定性句"，本篇以定性句为分类依据，不把四件载体塞进同一张定义。《About communities》写 `Communities on WhatsApp bring members together in topic-based groups.`，它的组成单位是 group，社区本身不是大群，创建资格在该页另句给出。《About WhatsApp Channels》的定性是 `While some features might feel familiar to WhatsApp chats, channel updates are a one-way broadcast rather than a conversation.`。《How to use broadcast lists》把它定成清单而不是容器：`Broadcast lists are saved lists of contacts that you can repeatedly message without having to select the contacts each time.`。《Groups API》另有一套接口口径：`Groups are an invite-only experience where participants join using a group invite link you send them.`，本篇把它与 3.2 的界面入口并列登记、不互相替换。

频道的两页法律文本给出治理侧定性。《WhatsApp Channels Guidelines》把义务落在管理员身上：`Channel admins should be mindful of the following guidelines (these “Channels Guidelines”) so their updates are appropriate for a general audience.`。《WhatsApp Channels Terms of Service》给出服务定义：`Channels is a one-to-many broadcast service that allows you to view and interact with relevant and timely updates shared by other WhatsApp users.`。合起来，官方写明的治理身份有三种：群组的 admin、社区与公告组的 community admin、频道的 channel admin 与 channel owner；广播列表在官方文本里没有管理员这一层，本篇按"官方未写明"登记。

## 1.4 本篇不做的事与范围红线

《How to use WhatsApp responsibly》是本篇唯一据此划界的官方页。它先给适用性：`All users of WhatsApp should review the following guidelines to ensure responsible use of WhatsApp.`，再给群组场景的两条义务：`You should get permission from contacts before you add them to a group.` 与 `If you add someone to a group and they remove themselves, honor their decision.`。同页承认平台侧存在一个管理员专用开关：`We've created an admin only message setting for WhatsApp groups.`，并在"应避免的做法"一节里写明后果：`Using WhatsApp in any of the ways listed below may cause your account to be banned.`。

本篇据此做三条自我约束。**一，只写口径不写做法**：批量发送、群控、账号池与多号轮换、频率与限速规避、反垃圾／反检测绕过、封禁申诉话术、非官方客户端一律不进正文；官方页里涉及这些的内容，本篇只登记为"该页把某种用法列为应避免的做法"。**二，官方写成步骤的内容只登记步骤的名称与数量**，不重述每一步怎么点、在哪个标签页里，控件名与档位名保留英文原文当作口径证据。**三，引文不跨页**：一个结论需要两页才成立时，本篇各引各的原句，再用中文说明落差；落差的逐条登记见附表 F，正文不重复列举。

# 2 规模上限：四类载体与接口侧五档

本节只按一种写法陈述：**这个数字挂在哪个容器上、出自哪一页、同一页或别页有没有第二种写法**。逐条数字与计数单位的完整登记见附表 B（99 行），本节只讲四类载体各自的那几档。

## 2.1 群组

群组是数字最密的一类。《How to create and invite into a group》给上限：`Web Windows Mac You can create a WhatsApp group with up to 1024 members.`。《How to see group members》用带千分位的写法：`Note: A WhatsApp group can have up to 1,024 members.`。《How to add and remove group members》再写一次短形式：`Groups can have up to 1024 members.`。三处数字相同、写法不同，本篇按写法分别登记（附表 B、附表 F），不据此认为存在两档。《How to join a group in a community》给出第四种写法，把创建者单列：`The maximum group size is 1,024 members (1,025 including the group creator).`——这是全篇唯一一处把 1,025 写进官方文本的句子，本篇只登记它出现在社区入群这一页，不外推到普通群组。

与权限相关的两档也挂在规模上。《How to change group admin settings》写的是条件触发的降档：`If a group has more than 256 members, Edit group settings will default to Only admins .`，它是越过某个刻度之后的档位切换，不是通用默认值（通用默认见 5.2）。同一页另有一条按人数收缩邀请权的写法，本篇与 5.2 那条并列登记。群组侧官方未写明的是"每个管理员可管多少群"——四页各说一个容器，没有任何一页给出这一档。

## 2.2 社区

社区的上限写成三个不同容器。《How to create a community》给容器数量：`Web Windows Mac You can create a WhatsApp community with up to 100 groups.`，同页给成员总盘：`Note : You can have up to 2,000 members in new and existing communities.`，另给一条独立的命名刻度：`The community name limit is 100 characters.`。《About community announcements》把 2,000 那句说得更具体：`You can add up to 2,000 members to a community and its announcement group .`——也就是说 2,000 挂的是"社区连同公告组"，不是单个群。

总群是第三个容器，被拉回群组那一档。《About general chat for communities》写 `Like other groups, the community general chat limit is 1,024 members.`，并写出触顶之后的行为：`When the community general chat reaches 1,024 members, new community members can’t join or be added to the group.`。这一句是社区侧少见的"到顶之后官方写了什么"，本篇登记为行为而不是档位。社区侧官方未写明的是各子群如何共用 1,024 这一档：三页各说一个容器，没有一页给出合计规则。

## 2.3 频道

频道在本篇只有一个可引的规模句，而且它不是数字。《About creating a WhatsApp Channel》写的是单向触达的范围：`With a channel, you can share one-way updates to an unlimited audience.`。官方在这里写的是 audience 无上限，同页没有给出 followers 的计数单位、分档或统计口径，也没有写频道是否有成员上限的另一档；频道侧唯一的硬数字挂在管理员人数上，登记在 4.4。

同一页把互动的天花板写死：`Followers can’t reply directly to updates or send messages to channel admins.`，互动形式则限定为 `Instead they can show their interest in a channel’s content by adding emoji reactions to channel updates or voting on polls.`。另有两句属于位置与可见性口径，本篇不把它们当作规模证据：`Updates are sent from your channel, which is in a separate part of the app away from chats and calls.`。同页一句治理侧的固定事实值得单列：`Channel admins are visible to each other.`——它是官方对频道管理员可见性的唯一写法。

## 2.4 广播列表

限额这一档只写在《About broadcast limits》。官方先给定性再给数字：`Broadcasts can help people receive important messages like holiday notes, daily greetings or event invites, but they can also be a source of overload.`，随后是 `To help manage your chats, you can only send a limited number of broadcasts per month.`，硬数字是 `You can send up to 35 free broadcast messages per month.`。同页还写明到限之前有提醒：`Note: WhatsApp will notify you when you are about to reach the limit.`。

这一页之外，官方没有写出 35 之上的下一档、也没有写出免费额度与付费额度的换算，本篇登记为留白（附表 C、附表 F）；地域范围官方自己留了口子：`People in some countries might see a monthly broadcast limit.` 与 `This feature is rolling out gradually and might not be available everywhere.`。广播列表的第二个维度是每条清单可含多少联系人，那一页不在本节引用范围，本篇只在附表 B 登记其数值与单位。同一页给出的替代出口是 `If you want to send more messages to more people, you can use status updates and channels to message many people at once.`，它属于官方对载体分工的口径，不属于限额。

## 2.5 Groups API 侧

接口侧的数字与界面侧不在同一套词汇里，而且《Groups Participants API》这一页几乎没有整句。参与者上限写在参数说明的括号里，原文是 `max 8 participants`，字段类型那一格是 `array of participants`；该行的说明文字在官方页里没有句末标点，POST 与 DELETE 两行又共用同一句措辞，本篇因此不把它整句引作口径，只在附表 D 登记措辞与所在方法。该页能整句引用的只有请求头两格的描述：`The user agent string identifying the client software making the request.`、`Bearer token for API authentication.` 与 `This should be a valid access token obtained through the appropriate OAuth flow or system user token.`。这三句讲的是鉴权与客户端标识，与"谁能进哪个群"无关，本篇不据它们推断任何档位。

常被当成接口规模出处的三个数字（每群参与者上限、每个业务号可创建的群数、每群可接入的业务数）写在《Groups API》的 Quick facts 表格里，是表格单元而不是句子，本篇只在附表 B 登记数值与计数单位，正文不改写、不换算。两处的关系官方没有写出：接口侧的 8 与界面侧的 1,024 之间没有任何桥接句，官方也没写接口群组的成员是否适用帮助中心那套开关；本篇把这一条登记为"官方未写明"，页面标识为 whatsapp_reference_groups_groups_participants_api_md 与 whatsapp_groups_md。


# 3 成员的进与出

## 3.1 建群

建群这一格官方只用一句话写了容量：`You can create a WhatsApp group with up to 1024 members.`，其余内容全部落在**字段**上，而不是流程上。本篇把官方写在建群这一步的字段登记为四项：成员选择、群名、群图标、以及两个入口——消失消息时长与群权限面板。官方对建群后的默认命名写法是 `If you don’t add a group name, the name will be a list of the group members.`；对群名的硬上限写法是 `The name limit is 100 characters.`；对时长档的写法是 `Tap Disappearing messages to turn on messages that disappear after a selected duration.` 与 `Select 24 hours, 7 days, or 90 days.`（时长档位与默认档的关系见 5.4）。

群说明是建群之后才加的一格，官方两句：`Once you’ve created a group, you can add a group description to inform group members and encourage engagement.` 与 `Group descriptions are visible to group members and people invited to the group.`——第二句把说明的可见范围扩到"被邀请但还没进群的人"，这是本篇在讲"谁能看到什么"时读到的一句范围扩展。同一页还登记了两种派生建群法：`You can create a new group from an existing group.` 以及从单个会话发起；前者给出的可改项是 `Deselect any members you don’t want to include in the new group.`。

官方在这一页把创建与邀请写成了编号流程。**本篇按 1.4 的划线只登记流程里的动作名与它的对象**：选联系人、命名、加图标、开关消失消息、开关群权限、发出邀请。这里有一条容易被读成操作指引的句子，本篇停在名称层：`Search for or select contacts to add to the group. If a contact doesn’t have WhatsApp, they’ll be sent an SMS invite.`——官方写明未注册联系人会被发短信邀请，这是"建群动作会带来一次短信触达"这一事实，不是本篇推荐的加人路径。

## 3.2 邀请链接与二维码

入口模型在官方页「How to join a group」被写成两句：`There are different types of groups on WhatsApp. Some you’ll need to request to join, and some you can join right away.`，随后是资格句 `To join a group, you’ll need to ask a group admin to add you to the group, or send you an invite link or QR code.`。被邀请者会看到什么，官方给了两句：`Once you receive the group invite, you’ll need to tap Join group.`，以及上下文句 `When you're first added to a group by one of your contacts, you can see more information about the group such as who added you to it, who created it, when it was created, and a group description if it has one.`。陌生号码邀请另有一套写法：`If you receive a group invite from someone who isn’t in your contacts, we’ll give you more context about the group and ways to stay safe.`。同页还有一句把"是否公开"这件事交回管理员：`You’ll only see this option if group admins choose to keep the group open to anyone on WhatsApp.`。

链接这条入口的失效面由一页帮助文档承载，标题是「Can’t join group with invite link」，全文只两句。第一句是错误形态：`If you see "You can't join the group or community because the invite link was reset" after tapping Join group, it means that the previous link is invalid.`，第二句给出官方唯一的补救动作：`You can ask a group admin to manually add you to the group if you’d still like to join the group.`。**这两句合起来是本篇在进这条轴上读到的完整闭环**：链接被重置即失效、失效后不自动回落、补救路径是回到 3.4 的手动添加。至于谁能让链接失效，见 2.1 引过的那页——`If you’re a group admin, you can deactivate a group invite link and create a new one.` 与 `Once an invite link has been reset, the previous link can’t be restored.`，两页共同写明"重置不可逆"这一条固定行为，官方没有给它开关。

二维码在官方文本里始终与链接同现，没有单独一页写它的生命周期；界面控件名「Invite via group link or QR code」同时管着两条入口（见 5.2），本篇据此把二维码登记为"与链接共用开关的那条入口"，不推断它有独立的有效期。

## 3.3 加入请求与审批

审批这一格的默认档在两页上各写一次，写法一致，本篇把它登记为"写明默认值与可改方"。官方页「About group join requests」的定性句是 `The Approve new members setting empowers admins to help grow, moderate, and protect their groups.`，动作句是 `Turning on the setting in Group permissions requires the admin to review every request to join the group before a member is allowed to join.`，默认句是 `The Approve new members setting is turned off by default.`，紧接着给出默认档的后果：`In this case, every member that requests to join the group will be granted admission.`。另一页「How to approve new members as a group admin」用同一句话起头：`If Approve new members is turned on in Group permissions, admins will need to approve anyone who wants to join the group.`，并在注意块里重复默认档 `The Approve new members setting is turned off by default.`。

请求的发起侧，官方先列进出入口（链接、被成员添加、社区群目录、二维码四类，该页以项目符号列出，本篇只登记名称），再写成因：`To request to join a group, you must have been invited by a current member or have found the group through a Group invite link, Group directory in a community, or QR code.`，界面提示句是 `When admin approval is required to join the group, members will see the message “An admin must approve your request” above the Request to join button.`，撤销权被写成一句：`Members will be able to cancel their request to join at any time by tapping Cancel request.`。结果的可追溯性官方也写了：`When a member’s request has been approved, the chat will reflect which group admin added the member or if they joined through the group’s invite link.`。

审批侧的两句在两页上几乎重合，本篇按"两处同值互证"登记：`Group admins will be notified when they have pending requests to review.`、`When reviewing a request, group admins can see the source of the request to better help them make decisions.`、`The admin can choose to approve or reject the request.`；批准与拒绝的后果写在另一页：`If you approve a request, the person will be added to the group.` 与 `If you dismiss a request, they won’t be added.`。**这两页都没有写审批有超时或自动放行**，本篇把这一格标为官方未写明。

## 3.4 手动加减与选择界面

手动加减是"添加"这条轴上唯一由人操作的入口，官方给它的资格句是 `Group admins can add or remove members from their group.`，扩权句是 `Admins can also allow other members to add people into the group.`，容量句是 `Groups can have up to 1024 members.`。被加者的体验官方也写了：`When you add a new member to a group, you may have the option to send message history to them.`（历史消息本身见 3.6），以及通知句 `When message history is sent, the new member will receive a notification.`。

选择界面的可用性信号是这一节的核心事实：`Select the groups you want to add the contact to. You can search for groups by name or scroll through the groups where you’re a member. If the group is full, or if only admins can add members, the group will be grayed out.`——官方把"群满"与"仅管理员可加人"两种情形合并成同一个视觉信号（变灰、不可选），本篇在附表 C 里把这一行标为"无开关（行为固定）"，因为它不是任何人能改的规则。同一页给出的批量入口条件是 `You must be a member of at least one group to see the Add to groups option.` 与 `The contact must be a WhatsApp user to be added to groups.`；对社区侧的排除写法是 `Communities and community announcement groups won't appear in the group list.`——**这一句把广播式载体从群组选择器里排除掉**，是四类载体在选择界面层唯一的互斥写法。非联系人的添加也被写明：`You can add people or phone numbers that aren’t saved in your contacts.`。

移除侧官方只写了动作名与控件名（在成员列表里选中、确认），本篇不重述步骤。值得注意的是官方对"加人加多了"的处理写法：`If you’ve added too many members to groups, you might see an error message.`，`You might need to wait 24 hours to add more members to groups and create additional groups.`——24 小时这一档写在帮助中心，而接口侧同类现象由错误码定性、不给阈值（见附表 B 的"官方未写明"两行与附表 F 第 24 项），两侧不能互相补全。另有一句跨层的限定本篇登记在此：`Groups may work differently if you’re invited to a group by a business using authorized third-party providers to help them manage WhatsApp.`。

## 3.5 退群、被移出与再加入

这一格里官方写得最完整的是"退出的连带效果"。群组侧：`You can exit or exit and delete a group if you no longer want to be in it.`，`When you exit a group, group admins will see “{contacts name or phone number} left.” in the group. It’s not possible to disable this notification.`——退出通知被官方明确写成关不掉。社区侧的连带是两句：`When you leave a community, you'll also leave all the groups you're in that are part of that community.` 与反向的 `When you exit a group that’s part of a community, you’ll also exit the community.`；公告组的绑定另有写法：`You can’t leave an announcement group unless you leave the community.`。创建者的处境单独一句：`If you're the admin who created the community, you'll need to deactivate the community to leave.`，而注销的连带是 `If you deactivate your WhatsApp account, communities and groups you created will also be deactivated.` 与 `There’s no way to reactivate a deactivated community.`。

被移出与"曾经成员"名单：`You can delete a group for all members if you’re the group admin.` 与 `Before you can delete the group, you’ll need to remove all group members.` 把删除群归到管理员，`Only admins can delete groups for all members.` 再写一次；移除的可见性被写成 `The removed member's profile name and phone number will be shown in the Past members list in Group info.` 加时限句 `Group members can see this information in that list for up to 60 days after a member leaves the group.`，同页还有一句常被忽略的后果：`Removing a member or deleting a group won't delete the group on other members’ devices.` 之后紧跟 `However, no one will be able to send messages.`。

再加入这一格官方写成一段递增的等待，三个数都在附表 B：`To rejoin a group chat you’ve left or deleted, you’ll need to ask the group admin to re-invite you.`，`In some cases, if the group admin has allowed them to in settings, other group members can re-invite you.`，`If you want to rejoin a group you’ve left twice, you’ll need to wait 24 hours before the group admin or other members can re-invite you.`，机制句 `Every time you leave a group, the amount of time you must wait to be re-added by the group admin increases.` 与上限句 `The maximum amount of time you’d have to wait would be 81 days.`。社区侧是同值两句、起点写法不同：`If you want to rejoin a community you’ve left twice, you’ll need to wait 24 hours to rejoin.`、`Every time you leave a community, this wait time increases.`、`The most you’d need to wait is 81 days.`——**两页一个给递增机制、一个只称"该等待时长"，本篇按两处同值互证登记，不合并成一条通则**（附表 F 第 19 项）。绕过等待的那句官方也写了：`To avoid the wait time to re-invite a member, the admin or another member can share a group invite link with you.`。历史不可恢复同样写明：`It’s not possible to see or recover groups you’ve exited and deleted or your group history.`、`You can't restore a group once it's been deleted.`。被社区移出侧的补救句是 `If you were removed from a community by an admin, a community or group admin will need to re-invite you.`，重加入的公告组连带是 `Once you rejoin a community, you’ll be automatically added to the community Announcements group.`。

## 3.6 历史消息

官方对历史消息给了两句方向相同、条件不同的写法，本篇并列不取舍。可见性默认的一句在官方页「How to join a group」里：`New members who joined the group through a group invite link or QR code won’t be able to view message history.`；另一句在标题为「About sending message history in groups」的页里：`You can send message history to new group members after they join including members who joined via an invite link.`。**两句各答各的问题**——前者答"新成员自动看得到吗"（看不到），后者答"管理员事后能不能发给他"（能）——本篇把它们登记为两页结论相反（附表 F 第 3 项），不写成任一方为通则。

机制与取值在两页从两个视角各写一次：`When you’re added to a group, the member who added you may choose to send message history to you.` 加 `They can send up to 100 messages from the last 14 days.`，以及 `When you add a new member to a group, you may have the option to send message history to them.` 加 `You can send up to 100 messages from the last 14 days.`——100 条与 14 天两个数完全一致，附表 B 按一个数字登记一次、出处页记两页。可选范围官方另写一句：`You can select the maximum number of messages and all pinned messages to send.`（置顶条数见 5.3）。通知面两句：`You’ll receive a notification if message history is sent.` 与 `All members will see a system message notifying them that message history was sent to the new member.`。

开关面在官方页「How to send message history in a group」里：`Group admins have the option to turn off the ability for members to send message history.`，`If Send message history is turned off, group members won’t see the option to send message history when they add a new member to the group.`，资格前提是 `You can add members to a group if you’re an admin or if admins have chosen to allow any member to add people to the group.`，联动句是 `Group admins can choose to turn Send message history off or on if they’ve allowed members to add others to the group.`——**"给不给看历史"以"能不能拉人"为前提**，这是本篇在 5.2 之后各节反复看到的那种控件联动。加密与例外各一句：`Messages in message history are end-to-end encrypted, meaning no one outside of the group, not even WhatsApp, can read them.`，不随历史一起发的内容是 `Messages that disappeared when disappearing messages was turned on.` 与 `Live location, view once messages, and business transactions.`。举报连带被写成责任归属句：`If someone you sent message history to reports content that was sent as part of the message history you sent, you’ll be reported.` 加 `The original sender of the message won't be reported.`。

# 4 管理员与身份

## 4.1 群管理员

群管理员是四类载体里唯一被官方写明"人数无限"的一格：`A group can have an unlimited number of admins, and any admin can make a member an admin.`，创建者的地位随后被单列：`The creator of a group can't be removed and will remain an admin unless they exit the group.`，任命资格句是 `If you’re a group admin, you can make another group member an admin.`。批量任命与批量解除官方都写成同一面板里的两个方向，本篇只登记控件名 `Edit group admins`（见 5.2）与两句选择动作 `Select the members you want to make admins.`、`Uncheck the admins you want to dismiss.`。

平台不介入这一条在本页以两句出现，是"改不动时官方写了什么"最直白的一处：`WhatsApp can’t interfere with group administration functions.` 与 `For example, we can't make someone an admin, even if they were previously an admin and removed by another admin.`。**这两句是本篇读到的一句直接排除救济的表述**：管理员身份丢了，官方既不代为恢复，也不承认自己有这个能力。同页对唯一管理员离开的情形给出自动机制：`If you're the only group admin and leave the group, another member is chosen at random to become the new admin.`（该句在另一页同写，见 3.5 的加入链），随后是 `If you rejoin the group and would like to be an admin again, you need to ask the group admin to make you one.`。

还有一句限定本篇登记在身份层的末尾：`Groups may work differently if you’re invited to a group by a business using authorized third-party providers to help them manage WhatsApp.`——官方自己在帮助中心三次写下这句（另两处见 3.2、3.4 的页），但**没有给出"不同"具体指什么**，本篇把这一格标为官方未写明，不据第三方材料补全。

## 4.2 社区管理员与总群管理员

总群在 2.2 里已被定性为一个群组（`General chat is a group that new community members can choose to join.`），其治理身份由官方在 2.2 引过的那页写成两句：社区管理员默认自动成为总群管理员、且可以提拔与解除总群管理员。也就是说，社区层的身份最终是**以群管理员这一身份在总群里行使的**。本节因此只处理两件在官方文本里被单独成页的事：任命／解除失败时官方写了什么，以及管理员屏蔽不了什么。

失败原因的官方写法是一张只按群组写的清单，起头句是列表引导句「If you’re unable to make another member an admin or remove them as an admin, it might be because:」 之下的五个原因，本篇逐条引其中三条：`You’re not a group admin.`、`You and the member aren’t on the latest version of WhatsApp.`、`The member is no longer in the group.`，另有 `There’s a network issue.`。清单之后是那句与 4.1 同形的排除句：`WhatsApp can’t interfere with group administration functions.`、`For example, we can't make someone an admin.`。**这一页整页没有按社区与总群分列失败原因**，本篇把它在附表 E 的意义上登记为"官方未按载体区分"——不代表社区没有对应规则，只代表官方没写。

屏蔽侧的官方写法是一条硬边界加一条绕行面：`At this time, it's not possible to block a group.`，`However, you can exit the group and block the group admin.`，随后给出隐私层的替代：`You can decide who can add you to groups by changing your group privacy settings.`。这一页对"群本身不可屏蔽"没有给原因也没有给开关，本篇登记为无开关（行为固定），并把"能屏蔽的是管理员这个身份、不是容器"作为本节结论；它与 5.1 里"谁能把我加进群"的隐私档位是同一条链的两端。

## 4.3 社区管理员的任命与上限

社区管理员是四类身份里唯一被帮助中心单独写人数上限的一格，全页三句核心：`Only a community admin can make another member a community admin.`、`A community can have up to 20 admins.`、`The original creator of a community can’t be removed and will remain an admin unless they disable the community.`。**这三句把权限、上限与不可解除性写在一页里**，其中 20 名这条与 4.1 的"群组管理员无限"分属两个载体，本篇不做跨载体类推（附表 F 第 13 项）。

解除侧的两句都关于创建者：`A community admin can't dismiss the admin who created the community.` 与 `You can remove yourself as a community admin as long as you're not the person who created the community.`——后者把"自我解除"写成允许动作但排除创建者，这一格是本篇读到的"谁能改"里少见的双向写明（默认值与可改方都到位，见附表 C）。任命动作与解除动作的官方控件名本篇只登记名称：在社区信息页里选中成员后确认，不重述步骤。

这一页对"社区管理员能做什么"没有任何直接枚举，本篇因此不写"社区管理员可以改公告组发言权"之类的话；2.2 与 5 节里能看到的社区侧能力（发公告、治理总群、把分组解除链接）都各自来自别的页面，逐格出处见附表 C。**社区管理员能否任命总群管理员，与"社区管理员由谁任命"是两条不同的轴**：后者本页写明只有社区管理员可以任命社区管理员，前者在 2.2 那页写明由社区管理员提拔与解除，两页之间官方没有给出统一术语表，本篇不合并。

## 4.4 频道管理员与所有者

频道的身份层比群组多出一格"所有者"。任命权被写成一句排他句：`Channel admins who aren’t the owner can’t invite or dismiss additional admins.`，上限被写成 `As a channel owner, you can invite up to 16 admins at a time to help you manage your channel and channel updates.`，另一页把同一数字写成总数口径：`You can invite up to 16 additional channel admins to help you manage your channel.`。**两处写法本篇都保留**：一处强调"一次 16 名"，一处强调"额外 16 名"，两数同值、对象描述不同，附表 B 各占一行（见附表 F 的相关登记）。退出与解除不对称也写明：`Any admin can choose to dismiss themselves.` 与 `Only you, as the channel owner, can delete the channel.`。

被任命者会看到什么，官方给了一句隐私后果：`When you invite someone to be a channel admin, the invitation is sent as a chat message from your personal WhatsApp account, not your channel.`，同页的标题式引导句是「Invited admins can see your phone number:」；本篇把这两句当作"任命动作会改变可见面"这一事实登记，不写任何规避办法。权限内容是三项能力加一句范围：`Once an invitation is accepted, an admin can manage your channel info and create, edit, and delete any channel updates.`；另一页把同样的能力写成项目符号（改频道信息与设置、创建编辑删除更新、查看全部指标），本篇只登记名称。

三条边界值得单列。第一是安全建议而非强制，官方写成列表引导句「All admins should enable two-step verification:」——官方用 should 给出，没有把它写成开关（附表 C 记为"只写方向（默认值未写明）"）。第二是有效期：`You can invite, dismiss, and revoke invitations for channel admins from your channel info page.`，邀请在七种情形之外只列了三种失效条件（未被接受、被接受、被所有者撤回），本篇按名称登记，数字见附表 B 的 7 天一行。第三是被解除后的状态：`The dismissed admin will remain a follower of the channel, unless they opt to unfollow the channel when dismissing themselves.`，以及通知面 `The channel owner or admin will be notified whenever someone is dismissed by the owner or dismisses themselves.`。所有者与被任命者互相可见这一格在创建页写成 `Channel admins are visible to each other.`，与 2.3 同一句，本篇不再重复计入。

# 5 治理开关矩阵

本节把附表 C 的六行群组面板与各类载体行落到正文。矩阵全表 **58** 行、**7** 类载体、**5** 类写明度，默认值或可改方留白 **16** 行；本节只写群组与频道里"写明"的部分，凡矩阵标为留白的，本节一律写"官方未写明"，并列页面标识。

## 5.1 发言权

发言权这一格由 `Send new messages` 控件承载，官方把开关方向写全、把默认档留白：`Group admins can change settings to allow either only admins or all members to edit group info, send messages, send invite links, or add people.` 是这一页的总起句，下面两格是 `When turned on, all members can send messages in the group.` 与 `When turned off, only admins can send messages in the group.`。**这一页没有写 `Send new messages` 默认在哪一档**，本篇在附表 C 把它标为"只写方向（默认值未写明）"；与之相对，另一页对"默认全员可发言"给了通用写法：`By default, any group member can send messages or change the group name, icon, description.`（出处与对照见 5.2），两处一个不说默认、一个说默认但没绑规模，本篇不把它们拼成一条。

社区与频道侧的发言权不靠开关：公告组是规则固定 `Only community admins can send announcements.`（同页另一句 `Admins can use this group to send announcements to all community members.`）；频道是形态固定 `Followers can’t reply directly to updates or send messages to channel admins.`。**这两条都是"无开关（行为固定）"**，与群组的可拨控件不在同一类里，读矩阵时最容易把它们混成一格。广播列表的对应格也属固定行为：收件人看不到列表内其他人，回复也不广播给他人。

接收方自己这一侧的发言权替代项是"谁能把我加进群"，官方写法是默认值加三个档位：`By default, your group privacy settings are set to Everyone, so anyone can add or invite you to join a group.`，同段给出选择项 `Everyone`／`My contacts` 两类，另一页把默认档写成一句 `Any users can add you to groups or mention you in a status.`。对已经进入的群，官方的救济是离开与举报而不是静音式降级：`At WhatsApp, we believe that you should have control over who you communicate with and what groups you join.`；`It’s not possible to block a group, however you can block and report group admins and members.`（另见 4.2）；以及管理员身份的独占：`Only other admins can remove an admin from the group.`。本篇在这一格只登记"默认 Everyone 且可自行收窄"这一事实，不写任何把发言权当触达手段的用法。

## 5.2 改群信息与谁能拉人

"改群信息"由 `Edit group settings` 承载，官方在面板页给三句：`Choose to turn on Edit group settings.`、`When turned on, members can edit group settings, including name, icon, and description.`、`When turned off, only admins can edit group settings.`，另加一条以规模为条件的档位切换：`If a group has more than 256 members, Edit group settings will default to Only admins.`。而"默认谁可改"写在另一页：`By default, any group member can send messages or change the group name, icon, description.`，其下紧接 `However, a group admin can change group settings to allow only admins to edit group info or add new members.`。**这是本篇处理"默认值"最典型的一处**：通用默认是全员可改，群规模越过 256 时改群信息落到仅管理员一档——两者不是同一层的写法，前者是默认档、后者是条件触发（附表 F 第 6 项），本篇两个档都保留。

"谁能拉人"由 `Add other members` 承载，是三格里唯一写明默认值的：`By default, all group members can add others to a group.`，`Group admins can change group settings to allow members to add others to a group.`，`When turned on, all members can add other members to the group.`，`When turned off, only admins can add other members to the group.`。"谁能发链接"由界面控件名「Invite via group link or QR code」承载，默认被写成人数条件：`By default, members in groups with fewer than 33 members can send a group link to invite others to the group.`，`Group admins can change group settings to allow only admins to invite others.`，两档方向句分别是 `When turned on, any member of the group can share a link to invite people to join the group.` 与 `When turned off, only admins can create a link to invite people to join the group.`。**同一条 33 线在另一页被写成"建群时的人数"**：`Admins and members in groups with fewer than 33 members can create group links to invite new members to a group.`——起点口径与当前口径并存，两种基准都登记并标明出处页，本篇不推断哪一个生效（附表 F 第 5 项）。联动句也写明：`If Approve new members is turned on, admins will still need to approve new members.`。

改群名的可改项上限在"改群"页重复出现：`The name limit is 100 characters.`；头像的效果句是 `Once added, the group icon will be displayed next to the group in your chat.`；成员号码的不可隐藏也在此页：`You can't hide group members' phone numbers in a group.`。**群状态是"改不动"这一格里唯一被写成成员也可动的容器**：`Group status is a new way to share everyday moments only with your group.`，`All members of the group can view, react and reply to the group status.`，存续时长被写成固定行为 `Like regular statuses, group statuses disappear after 24 hours.`，删除权是 `Only admins or the member who added the status can delete group statuses.`，且管理员删除时不通知作者：`When an admin deletes a status, the member who shared it isn’t notified.`。可见性的分配是两句：`Status views are only visible to the member who adds the status.` 与 `Status likes are visible to all group members.`；个人状态隐私设置对它无效：`Your personal status privacy settings don't apply to your group statuses.`；不可扩散的三条里本篇引一句 `You can't prevent members from sharing a group status.`（另两条为不能在群状态里 @ 人与不能转发他人群状态）。

## 5.3 置顶、@all 提及与群状态

置顶的界面档位三句齐全：`You can pin up to four messages to the top of a chat.`，`Once pinned, the message stays at the top of the chat as a banner for 24 hours, 7 days, or 30 days.`，`A pinned message is displayed as a banner at the top of the chat until it's unpinned.`。多条并存时的排序句是 `The most recent pinned message appears first in the banner.`，计数句是 `If more than one message is pinned, the banner shows how many messages are pinned.`，撤销句是 `You can unpin a message at any time.` 与 `If a pin isn't unpinned, it will automatically unpin when the set duration expires.`。群内置顶是**以开关为条件的能力**：`Group admins can choose to allow members to pin messages in a group chat.`，面板上的控件却复用改群信息那一个（`Turn Edit group settings on or off.`），官方在这一页没有另给"置顶"专属控件，也没有写它的默认档——本篇把这一格标为"只写方向（默认值未写明）"。可见性缺口被官方自己写明：`Users can't see a pinned message if they join a group after a pinned message is sent, if they lost or cleared their chat history, or if they deleted the message before it was pinned.`。**置顶条数在界面端是 4 条、在接口端另有档位**，两个数各带自己的载体，不合并成区间（附表 F 第 1 项）。

@all 是"以规模为条件、无人可改"的那一类，官方原句一句写尽：`All members in groups with 32 or fewer members and admins of groups with more than 32 members can use @all.`。同页对提醒的显示写成 `You'll see a notification with @ in your chats next to an unread message if you’ve been mentioned or someone has replied to your message.`，对穿透静音的效果写成 `Mentioning or tagging someone in a group will override any mute notifications that member has set for the group, unless they've also muted @all mentions.`，随后补一句例外 `They also may have muted your individual chat.`——**这一组句子在 5.5 的静音格上开了一个官方认可的例外通道**，本篇两句都引，不把它读成"提醒必然送达"。该功能自身的灰度限定也写明：`This is a new feature we’re building and testing out and may not be available to you yet.`。名字显示的一句登记在附带项：`The name of the member you’re mentioning might be displayed differently, depending on the way their name is saved in their address book.`。

群状态与提及的关系只有两句被写明：`You can’t mention someone in a group status.` 与置顶无关但决定状态的可见范围 `When you share a status to a group, all members of that group can see it.`。本节因此把"置顶＝管理员可授予、@all＝规模自动决定、群状态＝成员可发但作者与管理员才可删"三格并排登记，这是治理开关矩阵里三种不同写明度的最小样本。

## 5.4 消失消息

消失消息这一格官方同时写了默认档、可选档与"谁能开关"，是矩阵里少见的三格齐全行。默认档在隐私设置页：`The default message timer for disappearing messages is turned off.`；可选档在群版页面：`When you enable disappearing messages, you can choose to have messages disappear after 24 hours, 7 days, or 90 days, unless the messages are kept.`；概览页用另一种措辞写同一件事：`You can set messages to disappear 24 hours, 7 days, or 90 days after they’re sent unless that message is kept.`，并加一句作用范围限定 `The duration you choose only affects new messages in the chat, not messages you’ve already sent or received.`。**默认是关闭、可选是三个时长**，本篇不把 24 小时当默认值，也不省略 unless 从句里的"保留消息"例外（附表 F 第 17 项）。

开关权的群版写法是两句：`In a group chat, any group member can enable disappearing messages.` 与 `However, a group admin can change group settings to allow only admins to enable disappearing messages.`；关闭权另有一句条件式：`Anyone in a group chat can disable disappearing messages at any time, unless a group admin has limited that action to admins only.`。单聊侧的两页各写一次：`Anyone can turn on disappearing messages in an individual chat.`、`Anyone in the chat can disable disappearing messages at any time.`；把默认档设为全量新建会话的入口也写明为一句 `Anyone can turn on disappearing messages by default.`——**注意这一页的默认档讨论的是"新建单聊"，与群组的开关权不是同一格**，概览页对此的说法是 `You can set disappearing messages as default for new individual chats by tapping Settings > Privacy > Default message timer and selecting a duration.`。

"保留消息"（kept）一组的三句本篇完整引：`Disappearing messages can be kept in the chat so that they won’t disappear.`、`A kept message will be visible to everyone in the conversation and won’t disappear when its duration expires.`、`Anyone in a group chat can keep disappearing messages to stop them from disappearing as long as the set duration time hasn’t passed.`。控制权与撤销窗口被写成两句：`The message sender has ultimate control over who can keep it.` 与 `You’ll have approximately 30 days to unkeep a message from the time it was kept.`——**官方在这里的措辞是"约"**，本篇在附表 B 也按"约 30 天"登记，不把它读成硬上限。若会话本身关闭了消失消息则不能再保留：`If you decide to disable disappearing messages, you won’t have the option to keep them.`。边缘行为两句：`If a disappearing message is forwarded to a chat with disappearing messages off, the message won’t disappear in the forwarded chat.` 与 `Disappearing messages disappear for both senders and recipients, including messages you send to yourself.`。

官方在两页对"哪些消息不受所选时长影响"给的例外从句并不相同（一说 kept 消息、一说已发送与已接收的消息），本篇按各页原句分开引用，不改写成一条统一规则（附表 F 第 18 项）。接口侧的另一事实本篇在此登记一次即可：群组场景里消失消息被列在不支持的消息类型之内，因此界面这一整组开关在接口侧没有对应字段，两根轴不互换（见 2.5 的读法约定）。

## 5.5 静音、归档、受限聊天与通知

这一组是四类载体里最"只影响接收方"的一格，官方给的档位齐全但可改方常留白。静音的档位句是 `You can mute individual or group chat calls and messages for a specified period of time.`，`You can choose 8 hours, 1 week, or Always.`，效果句是 `You'll still receive messages, but your device won't vibrate or make noise to notify you.`；三件不失效的事被逐条写明：`Contacts aren't notified that you’ve muted the chat.`、`If you’ve enabled read receipts, contacts will still receive read receipts when you read their messages.`、`You’ll still have the unread message count next to the chat.`。社区侧的静音是分层的：`To mute the announcement group separately, open the community, tap the announcement group, then tap Mute notifications.`——公告组要单独静音，这一格官方写成动作名而非开关，本篇不重述步骤。

归档的默认档被官方写成两句几乎重复的话：`By default, archived chats will stay archived when you receive a new message.` 与 `When you receive a new message in the archived chat, the chat remains archived by default.`，可改句是 `Turn off Keep chats archived.` 与其后果 `When Keep chats archived is off, archived chats move to the bottom of the Chats tab.`。边界三句：`Archiving a chat doesn’t delete the chat or back it up to your SD card.`、`You won't receive notifications for archived chats unless you're mentioned or replied to.`、`You can’t adjust notifications for archived chats.`——**第三条与 5.3 里"提及可穿透静音"是同一方向的两个官方认可例外**。社区侧的归档有一条硬边界：`You can archive a group that’s part of a community. But, you can’t archive a whole community.`，公告组被归档后的表现单独写：`If you archive the Announcement group, it’ll stay at the top of the community with an Archived label.`，以及 `If you archive all groups in a community, the community will disappear from your Chats tab.`。

受限聊天（`Restricted chat`）是这一组里唯一被写成"限制使用方式"的控件，官方给了开关方与生效范围：`In group chats, group admins can enable restricted chat. In individual chats, either participant can enable restricted chat.`，`Once it’s turned on, Restricted chat applies to all messages in the chat.`，`Restricted chat can only be turned off or on on your primary device.`，以及版本条件 `Restricted chat is currently only available on the latest version of WhatsApp.`；它的目的句与本篇红线相关，只引不作操作：`This update helps prevent the use of unofficial clients or AI agents in chats.`；旧设置的升级不被自动执行：`Chats with advanced chat privacy turned on will not update to restricted chat automatically.`；效果面被写成四条"不能"，本篇引其一：`Can’t export the chat.`。通知层的最后两格是：设备优先 `Your device settings override WhatsApp settings.`，与安卓侧的一档版本分界 `Popup notifications aren't available on Android versions 10 and above.`；列表侧的档位句与群组同形：`You can choose to mute them for 8 hours, 1 week, or always.`，且同样保留例外 `You'll still get notifications for mentions and replies.`。

**矩阵里这一组共 4 类写明度都有实例**：写明默认值与可改方（归档的 `Keep chats archived`）、只写方向（静音的当前默认档官方未写明）、无开关（受限聊天在旧设置下不自动升级、公告组无法单独退出）、以规模或身份为条件（5.1 与 5.3 的两格）。凡本节写"官方未写明"的格子，对应页面标识都已在附表 A 登记、并在附表 C 的默认值或可改方留白行里落位；本篇不给这些格子补任何推断值。

# 6 互动功能：回应、投票、活动、语音与媒体

前五节讲容器的边界，这一节讲容器内部：一条消息在四类载体里能被怎么用。官方在这组页面的写法明显不同：绝大多数互动功能**没有可拨的开关**，能不能用只由两件事决定——载体是哪一类，这个载体有多大。附表 C 据此分「无开关（行为固定）」与「以规模或身份为条件（非开关）」两档登记。

## 6.1 表情回应与投票

公告组互动的定性句在官方页面「How to reply and react to announcements」(faq_1859295147860069)，三个动作并列、都没有配套开关：

> `As a member, you can react, report and reply to announcements.`

该页把回应与投票绑到群规模上的门槛是：

> `When an Announcements group passes 1,024 members, no one in the group can react to announcements or vote in polls anymore.`

越过门槛后官方写的是**全体失效**，另补一句这条线与社区人数上限无关。同一个 1,024 在附表 B 里按对象开两行，差异见附表 F 第 9 项。

投票的另一半在频道侧，官方页面「How to share channel polls and quizzes」(faq_223836640775084)：

> `Like all channel updates, polls are public and anyone using channels can vote on them.`

数值上限官方逐项给句——问题 255 个字符、选项 100 个字符、问题与选项各以 12 为上限，附表 B 各占一行且计数单位不同。单票与多票写成一句反向操作，只给关掉后的效果、不给默认档：

> `To limit users to one vote, turn off Allow multiple answers.`

可改性上官方只写"投票不可编辑、只能删了重发"，结果可见面只给到名称层。

## 6.2 活动

活动在两页各写一遍、档位并不相同：社区分组侧在官方页面「How to create and customize events in community groups」(faq_2127363544304650)，会话侧在官方页面「How to create and edit events in groups」(faq_3313983622238973)。两页共用的硬条件是通话链接的提前量，以及活动由创建者主持、没有共同主持：

> `Call links can't be added to events more than 12 months in advance.`
> `It’s not currently possible to have a co-host.`

编辑、取消与置顶三项权限被三句同一排他句式覆盖，本节引其中两句：

> `Only event creators can edit events.`
> `Only event creators can cancel events.`

社区分组页另把"活动发进公告组"归为管理员能力；两页都出现 `Allow guests` 而官方都没写默认档。新成员看不到旧活动是两页共有的硬规则，置顶不能例外：

> `New members can’t see events created before they join the group, even if they’re pinned.`

官方页面「How to find and join events in a community group」(faq_3666520913590793) 从参与者视角再写一次，唯一的补救路径是创建者删掉旧活动、重新建一个。该页也写明不能邀请不在群里的人、活动不能转发到别的聊天——这两句是本节对"活动不是投递通道"最直接的官方表述。

## 6.3 语音会话与群通话

官方在这组页面里写了两个不同的东西：**语音聊天**（群内常驻的语音房间）与**群语音通话**（逐个振铃的通话），本篇按页分别绑定。

语音聊天在官方页面「How to use voice chat in a group」(faq_3966323053691518)，该页限定平台仅限 Android 与 iOS。入口按群规模分两档——3 至 32 人的群与 33 人及以上的群走不同入口，这两个数在附表 B 里作为「入口分界」各占一行。发起后的默认行为是该页最有治理意义的一句：

> `The voice chat starts and you join silently. The group will be able to see you’re connected but will not be notified.`

也就是说**连接本身不产生提醒**。自动结束规则之一在附表 C 里登记为「无开关（行为固定）」：无人能改，也没有延长项。

> `If no one joins the voice chat within 60 minutes, it ends automatically.`

群语音通话在官方页面「How to make a group voice call」(faq_829612741557179)，规模上限一句与上面的分界数字相同但对象不同：

> `Group calling allows up to 32 people to join a WhatsApp group voice call.`

只有群成员能参加群通话；强制静音有次数封顶，官方只给次数、不给重置条件。访客侧是唯一带身份降级的部分：无账号者可用通话链接从浏览器加入，显示为 Guest，能发言与共享屏幕，但：

> `Guests can't add or remove participants, or send messages to others.`

同页另写明通话中不能移除联系人、群语音通话不提供语音转写。

官方页面「How to protect your IP address on WhatsApp calls」(faq_2635108359972899) 把 1:1 通话的中继写成用户可开关的设置，对群通话则另写一条**不需要开关的默认**：

> `Group calls are always relayed through WhatsApp’s servers by default.`

同页写明中继的代价是质量可能下降，中继后的通话仍端到端加密——1:1 的中继是可选项、群通话的中继是固定默认，两者在附表 C 里分属不同写法。通话记录一侧，官方页面「About call history on WhatsApp」(faq_743219147158705) 写的是 WhatsApp 不接管本机通话记录：

> `You can’t stop this from happening, but you can delete WhatsApp calls from your call history.`

同页另写明恢复只能靠备份、记录不能导出、也不会出现在运营商账单上。

## 6.4 媒体、文档与贴纸

媒体与文档的数值上限集中在官方页面「How to send media, contacts, or location」(faq_453914586839706)，该页在附表 B 里贡献 6 处数字。其中两条官方原句是：

> `For documents, the maximum file size allowed is 2 GB.`
> `Any forwarded messages that weren't originally sent by you will display a "Forwarded" label.`

同页对头像只给建议不给硬限（建议宽高至少 192 像素）——官方对头像只给建议、对文档只给上限，两者不是同一类约束。一次最多可选 100 张图片，状态视频有 90 秒的独立时长上限；视频档写成随网络条件分岔的两组**默认值**而不是可选档位。媒体入库的开关 `Media visibility` 官方写明默认为打开。

媒体在四类载体上的公开程度差别，由官方页面「Privacy at a Glance: Additional Information about using Status and Channels」(faq_1170535238421230) 给出官方定性：

> `Status and Channels are a separate space from your personal chats and calls.`

该页分别给出加密与公开属性——状态更新端到端加密、频道状态公开；状态侧以"相互联系人"为限、以 24 小时为期。真正需要登记为边界的是这一句：

> `Anyone can take screenshots or make recordings of channel updates and send them to other people or post them in other places online.`

同页另有一句反向口径：观众彼此不可见。两句合起来就是附表 E 的 E10 与 E18 两行的读法：频道**内容公开、身份私密**；群组与广播列表不在这两页的表述范围内。

# 7 社区专项：容器、总群、公告组

社区是四类载体里唯一一层**容器**：它不是一种消息形态，而是把若干群组装进去、并自动生成一条公告通道的外壳。官方对社区的治理写法比群组更"少"——许多在群组页里存在的开关在社区页里根本没出现，本篇在附表 C 里按"官方是否写明"分档登记，不替官方补齐。

## 7.1 社区结构与创建

官方页面「About communities」(faq_495856382464992) 把社区定性为主题化的分组集合，创建资格是一句不加条件的句子：

> `Anyone can create a WhatsApp community.`

加密属性是一句不附条件的声明：

> `Your personal messages and calls in communities are always end-to-end encrypted.`

这一句与 8.4 里频道那句"channels aren't end-to-end encrypted"构成附表 E 的 E18 行上的两个相反格，本篇不用任何一句去改写另一句。

官方页面「How to create a community」(faq_438859978317289) 是这一节数字最密集的一页：一个社区最多挂 100 个分组、名称上限 100 个字符、满员后不能再加新成员。最要紧的是第二条那句：

> `Community Announcements will automatically be created for your community.`

公告组不是管理员可选配的组件，而是**创建社区时自动生成的**。成员总规模两句：

> `You can have up to 2,000 members in new and existing communities.`
> `This limit includes the total number of members in the sub-groups and announcement groups within the community.`

后一句把 2,000 的计数成分枚举为"分组＋公告组"两类；另有页面写成"社区及其公告组"，两页枚举成分不同，本篇在附表 F 第 8 项登记该差异。

商业应用侧是例外面。官方页面「About communities on the WhatsApp Business app」(faq_1062663711575428) 与创建页各自重复了同一条限制：

> `it’s not currently possible to create a community on the WhatsApp Business app.`

官方紧接着给出例外——从消费端转入所有权、或先在消费端建好再把该号码注册为商业号，都可以**拥有**而不能**新建**。判据是这条社区的来源，而不是账号是不是商业号。

## 7.2 总群（general chat）与加入、退出动作

总群自身的定义与开关集中在 7.5；本节先登记两页，因为**总群在加入与退出这条路径上被官方当成一个普通群组处理**。

官方页面「How to join a group in a community」(faq_967457667545238) 的第一句就是可发现性规则，而且没有开关：

> `Any community member or admin can discover groups that are also part of that community.`

这一句在附表 C 里登记为「无开关（行为固定）」：谁能改这一格官方没有给。同一页给出群规模上限的第二种写法，本篇在附表 F 第 10 项与另两种写法并置，以"成员 1,024、含创建者 1,025"为准：

> `The maximum group size is 1,024 members (1,025 including the group creator).`

满员时的路径官方写成一条等待而不是拒绝；加入的结果被写成两种，且与 `Approve New Members` 那条开关（见 7.5）挂钩。同页还有一句把列表次序写死——最新在前、再按成员数降序、最后是有待审核请求的群。

官方页面「How to exit a community」(faq_1312647189536807) 一侧，退出被写成一次连带动作而不是离开单个会话：

> `When you leave a community, you'll also leave all the groups you're in that are part of that community.`

创建者例外紧跟其后，且官方把出口交给"停用"而不是"退出"，并写明停用无法复活——两句合起来是一条不可逆规则。附表 F 第 21 项把"一个人能建几个社区"登记为留白，依据正是这一页只写机制不给数值。该页也把"隐藏"与"退出"分开，并给出举报的连带数据面：

> `If you select Report to WhatsApp , the last five messages in the chat are sent to WhatsApp.`

## 7.3 子群与解除群组

官方页面「How to remove a group from a community」(faq_970281987938470) 是这一节唯一写明权限层级的一页，第一句就是双重资格：

> `Only community or group admins can remove a group from a community.`

第二句把"顺带清人"的权力只给社区管理员，并给出一条前置例外：

> `To remove a community member who is the owner of a community group, you must unlink the group from the community first.`

这一句在治理上很硬：**群主身份在社区层面受保护**，唯一的解锁方式是先把群与社区解除。解除后群本身仍可继续使用，其他社区成员则不再能在社区里看到该群；且解除**不是静默的**：

> `When a group is removed from a community, a message will appear in the group and Announcements group.`

该页没有写一个社区可以挂几个分组——附表 F 第 22 项登记为留白（分组数量在 7.1 那页另有写法，两处不在同一页，本篇不互相换算）。

归档一侧在官方页面「How to archive a group in a community」(faq_450394724284262)，官方第一句就把两件事切开，这句在附表 E 的 E24 行作为社区一格的逐字原句使用：

> `You can archive a group that’s part of a community. But, you can’t archive a whole community.`

该页另写明：归档公告组时它仍带着 `Archived` 标签留在社区顶部，归档全部分组时社区从 `Chats` 页签消失。前者说明公告组**不能被移出视野**，后者说明社区的消失只是让位给社区页签、不是被删除。

## 7.4 公告组（announcement group）

公告组的三页口径各有分工：成员视角在官方页面「About community announcements」(faq_582420703681043)，管理员视角在官方页面「About community announcements for admins」(faq_742237357891080)，动作与门槛在官方页面「How to reply and react to announcements」(faq_1859295147860069)——后者在本节只承担用途句，互动门槛那句已在 6.1 引用。

成员页把进入社区写成身份的自动分配，动作页则把这条通道的作用面写成一句：

> `Admins can use this group to send announcements to all community members.`

管理员页与成员页各写了一句排他句，话面不同但方向一致：

> `Only admins can send announcements and start polls, but members can reply and react to them.`

这一句是公告组治理开关矩阵里最关键的一格（附表 C 第 22 行登记为「无开关（行为固定）」）：发言权与发起投票权都在管理员一侧。**回复无法被关闭**由管理员页一句独立的否定句写死，这句在附表 E 的 E15 行是社区那一格的逐字原句，也是公告组与频道（关注者根本不能回复）在互动模型上的分水岭：

> `It's not possible to turn off or disable replies in a community.`

**退出**这一项被两页各写一次、话面相同，意味着公告组不是"可退订的子群"：

> `You can’t leave an announcement group unless you leave the community.`

规模在两页写成两种句子，附表 B 里 2,000 与 2000 两种写法各占一行。成员列表可见性是本节唯一的两页结论相反之处，两句都必须逐字保留：

> `Everyone can see the total number of members in the announcement group in the member list.`
> `Only the admin can see the total number of members in the Announcement group.`

两句分属两页，一页写明管理员能看到成员是谁，另一页把总数只交给管理员。本篇在附表 F 第 7 项登记为"两页结论相反"，并陈两句、不判定孰新孰旧。管理员页另有一句关于公告组**用途**的官方定性，本篇不把它写成投递建议：

> `Using community announcements instead of sending repeated messages to groups or members may help reduce the risk of being flagged as spam.`

这句话的主语是"使用公告组这一既有通道"，而不是"提高发送量"；该页没有给出任何发送频次、批量或阈值口径。

## 7.5 社区的规模与互动门槛

官方页面「About general chat for communities」(faq_1418342145436771) 是社区专项里唯一整页只讲一种会话的页面：

> `General chat is a group that new community members can choose to join.`

也就是说总群**不是公告组的另一档**，它是社区里可选加入的普通讨论群。官方接着把它写成"跟其他群一样"，并给出规模：

> `Like other groups, the community general chat limit is 1,024 members.`

满员时新成员**既不能自行加入也不能被添加**，补位条件被官方写成两种（被移出或自行退出）。互动门槛的默认值这一页写了两条，第二条在附表 C 里是社区侧少数「写明默认值与可改方」的行之一：

> `Approve New Members is turned off by default so that members can easily participate in the community general chat.`

社区管理员被默认自动成为总群管理员。成员侧的第二句在本篇的分层里很重要——**普通成员被官方写成可以向总群添加成员**，这与群组侧"谁能拉人"的默认档是同一条口径在社区容器里的再现：

> `You can add members to the general chat. You can also mute, exit, and report the general chat.`

该页同时把总群放在社区的分组发现列表里，也就是它与普通子群共用同一个入口。退出与解除这一段是全页治理味道最重的部分，官方写了三种身份的不同结局，第三条是"解除即停用"的连带效果：

> `If an admin unlinks general chat, it gets deactivated and all members will be removed from the group.`

这与 7.3 那句"群被解除后成员仍可继续使用群"**并不指向同一动作**：前者处理总群、后者处理普通子群，本篇按各页原句分别登记，不合并成通则。

## 7.6 发现、通知、安全与退出

官方对社区的"治理"更多写在这类页面而不是设置页。发现性是一条否定式的产品承诺，写在官方页面「About building private, safe, and secure communities on WhatsApp」(faq_458610306367976)：

> `Unlike social media and other messaging services, WhatsApp will not support the ability to search for or discover new communities on our service.`

该页同时把公告通道的唯一性写成一句因果句，这一句也是 7.4 那句"用途"定性的上位句：

> `To limit noise and overload, only community admins can send announcements to all community members.`

同页另写明：因社区引入，已被转发的消息一次只能转发给一个群；号码只对社区管理员、存了你号码的人、以及从其他会话拿到你号码的人可见。处置阶梯在该页被写成三种并列结果：

> `WhatsApp will ban individual community members or admins, disband a community, or ban all members of a community.`

同页紧接一句说明处置所依据的信息面——官方依赖所有可用的非加密信息。这一句在附表 E 的 E27 行与频道侧的处置句构成对照：**社区靠非加密元信息做处置，而不是靠读取内容**。

官方页面「How to keep your groups and community safe」(faq_1003616827680090) 是清单页，官方把管理员的控制点写成三句：邀请链接靠分享而不是公开发布、限制可改动社区主题／图标／描述的人、删除消息或移除成员。第一条里那句"不要把链接发到公开论坛或网站"是官方对链接分发场景的定性提醒，本篇不把它写成任何分发做法：

> `Invite members to join your community by sharing a link. Don't post the link on a public forum or website.`

成员侧的三条控制分别是自查群权限、静默退出与静音群通知。第二句是本篇登记"静默退出"的唯一官方依据：

> `If you choose to leave you can do so quietly. Only the admin will know.`

处置结果的解释页是官方页面「What does "This community is no longer available" mean?」(faq_674413780678918)：

> `We may prevent further activity in communities or chat groups to comply with our legal obligations or when a community admin is in violation of our Terms of Service.`

同页另列出被评估的信息类型：社区名称与描述、群主题与头像。该页把复核入口只写成一个动作名。按本篇红线，这里只登记"存在 request a review 这一入口"与"该入口只对社区所有者或管理员开放"两条事实。

# 8 频道专项：一对多广播的公开面

频道是四类载体里唯一被官方写成"广播"（broadcast）而不是"会话"（conversation）的一种，也是唯一一条内容端到端**不加密**、对所有用户公开的通道。这一组根本属性决定了频道治理开关的稀缺：发言权本来就只在管理员一侧，没有"谁能发消息"的开关；公开性是固定规则，没有"谁能看"的开关；关注者身份与内容留存各有官方写明的固定行为，没有可改档位。本篇在 8.1 与 8.4 分别给出"关注者彼此不可见"与"任何人都能查看频道更新"两句原句——这两句合起来构成频道最核心的治理张力：**内容公开，身份私密**。

## 8.1 创建、关注与身份

官方页面「About WhatsApp Channels」(faq_549900560675125) 把频道与群组最根本的区别写成**回复不可用**：

> `Followers can't reply directly to updates or send messages to channel admins.`

同页另两句补全关注机制——通知默认静音、用户不会被自动订阅任何频道。

创建资格与隐私设计原则在官方页面「Building Private, Safe, and Secure WhatsApp Channels」(faq_958928238510970)：

> `Users can't be added to a channel without choosing to follow it and they will only see updates for the channels they choose.`

这一句与同页"任何人创建频道都不必向关注者暴露号码或头像"合起来，是官方对频道创建门槛与加入机制的正面表述——**创建无需审批，加入完全自愿**。

关注者隐私在官方页面「About safety and privacy on channels」(faq_1318001139066835) 分三层写出：非管理员联系人看不到你关注了哪些频道，其他观众完全看不到你的互动；但若管理员恰好是你的联系人，隐私层完全撤去：

> `Your contacts can't see which channels you follow or how you interact with channel content (unless they are an admin of a channel you follow).`
> `Channel admins who're also contacts can see all parts of your profile like your name, full phone number, and profile photo.`

这几层在附表 E 里分属"关注者可见性"与"管理员可见性"两格，不是同一维度的重复。

官方页面「About creating a WhatsApp Channel」(faq_265055289421317) 给出受众规模的定性句：

> `With a channel, you can share one-way updates to an unlimited audience.`

"unlimited" 在附表 B 里登记为定性表述而非数值上限，官方没有给出关注者数量封顶。官方页面「How to create a WhatsApp Channel」(faq_794229125227200) 给出管理员上限：

> `You can invite up to 16 additional channel admins to help you manage your channel.`

同页另写明频道验证尚未公开。

## 8.2 邀请关注者与管理员

邀请关注者侧在官方页面「How to invite channel followers」(faq_627992286976376)，唯一的数值上限是单次可选的联系人数：

> `You can select up to 64 contacts.`

同页另写明只能邀请那些也把你存为联系人的人，且邀请发出后不可撤回：

> `You can't revoke a follower invite after you send it.`

这一句的不可撤回性与 7.2 的群加入待审核状态形成对照：频道的邀请**没有待审核状态**。

发现侧在官方页面「How to find and follow WhatsApp Channels」(faq_630432792316720)。目录的加入规则写成一条官方拒绝：

> `Requests to add channels to the Explore, Recommendations, or Suggestions sections are not accepted.`

这一句在附表 C 里登记为「无开关（行为固定）」——官方明确不接受主动申请进入推荐列表，不是"暂无此选项"而是"拒绝受理"。验证徽标同页另写一句：眼下也不接受频道验证申请。

## 8.3 名称、头像与管理员身份展示

频道名称上限在官方页面「How to manage channel info and settings」(faq_214063281402212)：

> `Channel name is limited to 100 characters.`

该数与社区名称上限相同但对象不同，附表 B 各占一行。该页另写明名称、描述与图标均可随时更改，但没有给出反应表情设置的默认档。

管理员身份的独立展示规则在官方页面「About channel admin controls」(faq_360977646301595)，这一句把管理员发布更新时的身份呈现从个人账号切了出去：

> `Admins can create a custom admin profile with a name and photo that's separate from their personal WhatsApp profile.`

同页的边界补充是管理员的号码与账号信息对关注者不可见；权限层级的排他句则写明除所有者外其他管理员不能增删管理员，也只有所有者能删除频道。该页同时写出频道公开性与关注者不可撤销两条硬规则，两句各在附表 C 登记为「无开关（行为固定）」：

> `All channels are public. You cannot create a code or password for a channel.`
> `It's not possible to remove channel followers.`

官方页面「How to delete a channel」(faq_598838008878228) 把删除写成不可逆，并留下一句常被忽略的本地留存说明：

> `Your past updates will still be available from their phones.`

也就是说删除的是发布端，收件端已下载的更新不在删除范围内——这句与 8.4 的"已保存的媒体不会被删除"是同一条口径。

## 8.4 更新的形式、互动与留存

本节引用 12 页，按形式、可见窗口、编辑、删除、转发与其他专项逐层登记。

**形式与加密属性**

官方页面「How to share a channel update」(faq_290544379966533) 是频道更新的核心页，官方把加密属性写成一条独立短句：`Most importantly, channels aren't end-to-end encrypted.` 这一句与 7.1 社区那句 "always end-to-end encrypted" 构成附表 E E18 行最硬的两格对照。更新形式在该页列为文字、图片、视频、贴纸、链接、音频、投票七类，视频笔记以 60 秒为上限、不能发 PDF，而关注者一侧有一句不留余地的说明：

> `Followers can save any media you share.`

**可见窗口**

同一页把关注者与非关注者的历史可见性各写一句：关注者能看到其开始关注前 30 天内发布的更新，非关注者也能看到过去 30 天的更新。官方页面「How to forward to your channel」(faq_699347952293912) 把关注者的历史可见性写成另一种句式：

> `Followers can see all channel updates shared since they followed the channel.`

两种句式对关注者可见范围的枚举起点存在差异（"30天前"vs"开始关注后"），附表 F 登记该对差异，本篇不折中。

**编辑与删除**

编辑窗口与不可编辑清单在官方页面「How to edit channel updates」(faq_1549633222110625)：`You can edit some channel updates for up to 30 days.` 不可编辑项一句列全：`You can't edit photos, videos, audio updates, polls, other media, or forwarded updates.` 编辑不触发通知。

删除窗口在官方页面「How to delete channel updates」(faq_251090924134584)：`You can delete updates up to 30 days after sending.` 任何管理员都能删，但两条本地留存补充把删除的范围收窄：`Media saved to users' phones won't be deleted.` 与 `Forwarded updates won't be deleted.`

**转发**

官方页面「How to forward from channels to chat or status」(faq_305393838527652) 把上限写成两句——正常上限与高频降级：`If an update has already been forwarded many times, you can only forward it to one chat at a time.` 转发计数公开但身份保密：

> `You can't see the names of people who forward channel updates.`

官方页面「How to share your channel」(faq_2490388581320854) 给出频道链接的单次转发上限：`Select up to five chats or groups.`

**置顶与收藏**

官方页面「How to pin channels and star updates」(faq_1023031902773792)：频道列表顶部只能钉两个频道；要加星标必须先关注该频道。

> `You must follow a channel to star its updates. When you unfollow a channel, your stars are removed from the channel's updates.`

后一句在附表 C 登记为「无开关（行为固定）」——取消关注后星标自动消失。

**语音转写**

官方页面「About voice update transcripts on Whatsapp Channels」(faq_1339924517208346) 的默认档：`Voice transcripts are off by default. Enable transcripts on your device to use it.` 公开性重申：`Voice updates on channels, like all channel updates, are public.`

**音乐与 AI 贴纸**

官方页面「How to share music updates to your channel」(faq_824049403559473) 给出两档时长：图片更新的音乐为 30 秒（歌曲更短则按歌曲长度），视频更新的音乐可长到与视频同长。

> `Video updates can have music that lasts as long as the video, up to a maximum of 90 seconds.`

AI 贴纸生成上限在 faq_290544379966533：一次生成最多四个新贴纸。

**投票与问答（续 6.1）**

频道投票的转发限制在官方页面「How to share channel polls and quizzes」(faq_223836640775084)：`You can't forward photo polls.` 问答结果能看到响应者的资料名。

**付费合作伙伴标签**

官方页面「How to label paid partnerships on channels」(faq_1512467186677937) 把标签写成不可移除、转发不携带、编辑后需重新添加：

> `The paid partnership label can't be removed from an update once it's been added.`

**广告**

官方页面「About ads in WhatsApp Status and Channels」(faq_337473702666585) 把广告与个人消息的隔离写成一句保护承诺：

> `As always your personal messages, calls, and status updates are end-to-end encrypted and can't be used to show you ads.`

这一句与频道"非端到端加密"属性共存——广告定向使用频道活动数据而非加密消息内容；两句不矛盾，在附表 C 里分属不同行。

## 8.5 数据、举报与治理

频道更新的服务器留存时限写在官方页面「How to request a review for a closed channel」(faq_394993313189648)：

> `All channel updates are removed from our servers after 30 days.`

这一句与 8.4 的"过去 30 天可见"共享同一个 30 天数字，但对用户侧是可见历史上限、对服务器侧是留存上限，附表 B 分别登记。同页把频道关闭写成一句条件句——更新违反频道准则时频道可能被关闭；而复核申请有官方写明的次数封顶：

> `Review requests can only be submitted once per alert, and only channel admins can submit requests.`

这一条与 7.6 的"存在 request a review 入口"性质不同：频道的复核入口有次数封顶，社区侧次数官方未写明。

推荐算法在官方页面「About how WhatsApp recommends channels」(faq_962978635456336) 列出依据的信息类型，其中一句隐私保护句独立成段：`As always, your personal chats and calls remain end-to-end encrypted. They aren't used to recommend channels.` 推荐因素包括频道信息与用户活动，但不含加密消息内容。

推广频道与付费订阅各有一页，均以"尚未全面开放"开头。官方页面「About becoming a promoted channel in the directory」(faq_1251246649902787)：`We're rolling out channel subscriptions gradually, so it may not be available to you yet.`官方页面「About adding subscriptions to your channel」(faq_1371180700817474) 写了同一句话的另一变体：`We're rolling out channel subscriptions gradually to a select group of channels, so it may not be available to you yet.` 这两句是官方对功能时间表的说明，不是治理开关——官方没有写明哪个设置可以打开或关闭它们。

## 8.6 通知、桌面端与统计

频道通知默认档在官方页面「How to manage channel notifications」(faq_6074154625973804)：`When you follow a channel, notifications are muted by default.` 开启后的效果同页写明——与个人消息的通知方式一致。两句在附表 C 里构成一条「写明默认值与可改方（用户）」的行。

统计数据两条门槛与一条延迟说明在官方页面「How to see channel metrics and insights」(faq_1446688872845683)：

> `Channel insights aren't available for channels with fewer than 100 followers.`
> `A region only appears as a top region if your channel reaches more than 100 local accounts in the last 30 days.`
> `Reported numbers may be delayed by about a week.`

前两句各有"100"但计数单位不同——关注者总数与某地区触达账号数——附表 B 分别登记，不合并。"约一周"官方用 `about a week` 表述，附表 B 按措辞登记为描述值而非承诺值。

桌面端通知总规则在官方页面「How to manage your notifications」(faq_797069521522888)，该页不只覆盖频道。本篇只引其中一条对频道统计有直接影响的上位句：`Your device settings override WhatsApp settings.` 这一覆盖关系在附表 C 里登记为「无开关（行为固定）」——设备系统关闭通知时，WhatsApp 内的频道通知开关均无效。

## 8.7 认证、订阅与付费

频道认证通过 Meta Verified 或 Meta One 订阅实现。官方页面「How to create a verified channel on the WhatsApp Business app」(faq_2613314448830863) 把资格写成条件句：需要有效订阅且商业账号已验证，两者缺一时界面上根本不出现添加验证徽标的选项。数量上限与名称绑定各一句：

> `Currently, you can verify up to 10 channels per subscription, depending on the subscription plan.`
> `The business name which has been verified will always be the prefix of any verified channel name.`

同页另写明有验证徽标不保证频道出现在搜索结果里。

频道与社区在商业应用侧的可创建性相反。官方页面「About channels on the WhatsApp Business app」(faq_809740894165112)：

> `Channels on the WhatsApp Business app allow you to broadcast an update to an audience of any size.`

这一句与 7.1 里官方页面「About communities on the WhatsApp Business app」(faq_1062663711575428) 的限制句构成功能不对称：`it's not currently possible to create a community on the WhatsApp Business app.` 商业号可建频道但不能建社区——两句各按原页登记，不合并为"商业号功能总图"。

# 9 广播列表专项：发给一批联系人而不是建一个群

广播列表在四类载体里最特殊：它既不是消息的容器（没有会话名册，成员互不可见），也不是订阅关系（没有"关注"动作，接收方无需主动加入）。官方在广播列表页面的写法因此与群组、社区与频道都不同——没有可拨开关，只有可达前提（双方互为联系人）、一个规模数字（256 人）与一条月度上限。

## 9.1 机制、上限与可达前提

官方页面「How to use broadcast lists」(faq_861663048350950) 的定义句把广播列表定性为"可重复使用的联系人名单"而非会话；同页另写明列表本身数量不限，而单个列表的人数上限是：

> `You can include up to 256 contacts in each broadcast list.`

可达前提与效果各有一句：名单里的人都必须先把你的号码存进通讯录，否则收不到。这两句是同一条件的两种主语写法，附表 F 登记为同义异主语的重复而非冲突。

> `Recipients receive a message from your broadcast list as an individual message from you in their Chats tab. Their reply will also appear as an individual message and won't be sent to other contacts in the broadcast list.`

这是广播列表与群组最根本的区别：**广播没有群聊会话，回复不会广播给所有人**。平台限制一句——不支持 Windows、Mac 与网页端，在附表 C 登记为「无开关（行为固定）」。

月度上限在官方页面「About broadcast limits」(faq_1176325363861991)：

> `You can send up to 35 free broadcast messages per month.`

该数在附表 B 登记为"软限额"——同页第一句就写明只有部分国家会看到月度广播上限、该功能正在分批开放。上限临近时官方会提示；超出上限后的出路，官方写成一句导向说明：

> `If you want to send more messages to more people, you can use status updates and channels to message many people at once.`

这一句是官方对广播列表规模上限的直接补充——出路不是扩容广播，而是换载体。

## 9.2 商业应用侧的广播列表

官方页面「How to use broadcast lists on the WhatsApp Business app」(faq_653415899610349) 的定义句与消费端略有差异：

> `Broadcast lists are saved lists of contacts that allow you to message many of your customers at once.`

主语从"联系人"换成"客户"，功能实质未变。可达前提句也同消费端，只是把"你"换成"你的号码"——只有把你的号码存进通讯录的联系人才会收到广播消息。该页对商业用户给出的仍是同一套互为联系人条件，没有另设更宽的口径：商业侧的规模档位、频次与额度都不在这页出现。

与群组的功能分界在该页被写成一句否定式定性：

> `Broadcast lists are a one-to-many communication. If you want your recipients to participate in a group conversation, you'll need to create a group chat instead.`

该句在附表 E 是广播列表一格的逐字原句，也是本篇关于"广播列表不是群"的官方定性句——它把"想要成员彼此参与"明确交给群组。此外该页把商业广播（business broadcast）列为广播列表之外的另一种工具，其适用范围与额度不在本页给出，另见 9.3。

## 9.3 与群组／频道的分界

广播列表与群组在**有无共同会话上下文**上被官方写成两种不同载体。9.1 引过的那句回复去向与群组侧"所有成员看到彼此消息"构成对照，官方在两类页面各写一次，本篇不合并成通则。

当需要触达未存号码的人时，官方给出的出口是**把广播转为营销消息**，而非扩大广播列表本身。faq_1500274774253689 把这条路径与受众逻辑写成三句：

> `Broadcast list messages can only reach customers that have your business's number in their contact list.`
> `To reach customers who don't have your business's number in their contact list, you can turn your broadcast message into a marketing message.`
> `The marketing message audience is automatically set to the customers who haven't received a broadcast message.`

第三句是本节对营销消息受众定义最重要的一句——营销消息的受众是"广播未能触达的那部分人"，不是全体联系人；两句各自限定一个集合，本篇不把它们读成可以叠加的两次投递。

官方页面「About business broadcasts」(faq_1524953031793983) 把商业广播定性为一种按送达计费的消息类型，其适用范围句是：`Business broadcasts is currently available in select countries and may not be available to you yet.` 同页另写明未发送过商业广播者的试用已于 April 30, 2026 终止、未用额度随之失效。两句都不构成"可以发更多"的口径；按本篇红线，这里只登记官方写明的适用范围与终止日期。

月度上限那一页还给出官方对广播的定位句：

> `Broadcasts can help people receive important messages like holiday notes, daily greetings or event invites, but they can also be a source of overload.`
> `To help manage your chats, you can only send a limited number of broadcasts per month.`

两句在附表 C 里都属于「无开关（行为固定）」——过载风险由官方设定条数上限来处置，而不是交给用户调节。与频道的分界方面：频道关注者是**自愿关注**的任意用户，广播列表的接收者是**互为联系人**的特定人群；频道内容公开留存 30 天，广播内容仅出现在个人 1:1 会话里；频道官方写成 `unlimited audience`，广播列表单列表上限 256 人。

## 9.4 opt-in、商户消息与"能不能一次发很多"

本小节只登记官方对 opt-in 的定性表述与"广播列表不等于批量投递能力"这条边界，不给任何提高发送量、绕开月度上限或规避审核的做法。

opt-in 义务在官方页面「Get opt-in for WhatsApp」(whatsapp_getting_opt_in_md)：

> `Businesses are required to obtain opt-in before messaging people on WhatsApp.`

同页把这条义务展开为一句更长的表述，关键在许可的形式：

> `before messaging people on WhatsApp, businesses are required to obtain opt-in permission, which can be general and not specifically for WhatsApp, as long as businesses comply with all local laws.`

也就是说**许可可以不是专门针对 WhatsApp 的**，只要符合当地法律即可。opt-in 须满足的三条要求在官方清单里各有一句——明确告知这是选择接收该企业的通信、明确说明企业名、符合适用法律；本篇只登记"三条要求"的存在与数量。质量处置句：`Our systems will rate limit businesses if the business's quality is low for a sustained period of time.` "sustained period of time" 无具体时长，附表 F 登记为"官方未写明"。

接收方角度的保护在官方页面「About interactions with business chats that include offers and announcements」(faq_263784176043634)：

> `WhatsApp requires businesses to secure your permission to send offers and announcements and WhatsApp takes action to prevent you from receiving too many of these messages.`

同页另把控制权交给收信人本人——用户可以随时停止与该企业的对话或直接屏蔽。这两句与上段商业方义务相互对称，共同构成 opt-in 的两侧；该页没有任何关于发送量或频次的口径。

官方页面「Business profiles - WhatsApp」(whatsapp_business_profiles_md) 把商业资料描述为电话号码注册时可附带的一组信息字段：`Your business phone number's profile displays additional information such as address, website, and description.` 该页涉及 API 字段的更新方式，按红线本篇只登记字段存在性。

至于"能不能一次发很多"，官方口径可归纳为三条边界，全部原句已在 9.1 登记：单列表人数上限 256；月度免费广播 35 条（并注明分批开放）；可达范围被"互为联系人"这一前提锁死。三条同时成立：**广播列表不是批量投递工具，而是发给已知联系人群组的便利快捷方式**。官方在 faq_1176325363861991 把超出广播列表能力后的出路写成频道与状态，而不是广播列表本身的扩容——这是官方对"能不能一次发很多"最直接的回应：**换载体，不是扩广播**。

# 10 Groups API 接口面：官方把群写成了哪些端点、字段与状态

前面九节读的是消费者端与政策端的群，这一节读接口端的群。要先说明本节的**登记范围**：官方在 Groups API 这一组页面上写了哪些文档层次、哪些端点与 HTTP 方法、哪些字段名与取值域、哪些数值上限、哪些失败码与回调状态——只到这一层为止。本节不给请求示例、不写调用步骤、不做参数拼装、不提供脚本，也不讨论并发与重试策略、批量添加参与者的操作路径、限速规避与账号池轮换；官方文档里那些可以照做的部分，本篇一律不搬进正文。这样写的理由与整篇一致：接口面在这里是**治理口径的证据**（官方把哪些能力写成了可编程的对象、把哪些能力明确关掉），不是操作手册。端点与方法的清单同步登记在附表 D 的接口方法组（覆盖 5 页）与错误码组、HTTP 状态组，字段与取值逐条见附表 D 的其余各组，正文只引官方成句的说法。

## 10.1 文档分层与端点清单

官方把群组接口写成了六个层次，每层各管一件事，分层本身就是口径。概览页「Groups API」(whatsapp_groups_md) 给定位与硬数字：

> `The Groups API enables you to programmatically create groups for messaging and collaboration.`

同一页把入口模型写成一句定性，本篇在附表 C 第 52 行与附表 E 第 26 行都以此句为唯一出处：

> `Groups are an invite-only experience where participants join using a group invite link you send them.`
> `This invite link provides context about the group, helping the user decide whether they want to join.`

入门页「Get started with Groups API」(whatsapp_groups_get_started_md) 把同一件事换成资格语言，并给出前置条件的并列陈述，其中与号码形态直接相关的是：

> `Your business number is in use with Cloud API (not the WhatsApp Business app).`
> `Your app is subscribed to the Messaging account of your business phone number.`
> `Your app has the whatsapp_business_messaging permission for the business number.`

同一页另有一句把"谁决定入群"写在用户侧，这句与概览页的 invite-only 是同一模型的两面：

> `Groups on are invite-only, meaning that potential group participants are ultimately in control of whether they want to join the group or not.`

指南页「Group management」(whatsapp_groups_reference_md) 是接口面最集中的一页，官方在页首写明它的组织方式：

> `The Groups API gives you simple functions to control groups through their lifecycle.`

消息页「Group messaging」(whatsapp_groups_groups_messaging_md) 自己划定了页内范围，这两句也是本篇把"发消息"与"管群"分成不同小节的依据：

> `This document describes the APIs and webhooks for sending and receiving messages within groups.`
> `This page now only discusses how to send and receive messages in groups.`

参考页「Groups Management API」(whatsapp_reference_whatsapp_business_phone_number_groups_management_api_md) 在号码维度列群，「Groups API FAQ」(whatsapp_groups_faq_md) 回答删除群与邀请链接的常见问题。把五张 reference 页的 Method 列逐页解析后，官方写出的端点族可以归成五组：群本身（DELETE、GET、POST 三个方法挂在同一个群组标识上）、参与者（DELETE、POST）、邀请链接（DELETE、POST）、加入请求（DELETE、GET、POST）与号码维度的群列表（GET、POST）；这五组的页数与方法集合由脚本校验，逐条见附表 D 的接口方法组。指南页对其中几个端点各给了一句功能定义，本篇只取三句：

> `Use this endpoint to retrieve a list of active groups for a given business phone number.`
> `Use this endpoint to remove participants from the group.`
> `Use these endpoints to get and reset group invite links.`

数值面在本层只有三行，都出自概览页的 Quick facts，在附表 B 分别登记为第 44、45、46 行：单次请求参与者数组的长度上限 **8** 名参与者、单个业务号码可创建的群组数上限 **10,000** 个群组、同一群组允许接入的 Cloud API 业务数 **1** 个业务资产组合。资格面有一句写在参考页，用于说明"能不能用接口管群"的前提：

> `Indicates if WhatsApp users who click the invitation link can join the group with or without being approved first.`

最后是官方明确关掉的门面。概览页把性能指标写成一句否定式陈述，并补了一句建议：

> `Performance metrics are not available for message templates used in Groups.`
> `Create new templates specifically for Groups use instead of repurposing templates used for one-to-one messaging.`

同一页还以清单形式点名了六类不支持的消息形态与三类不支持的动作（成分逐条见附表 D 的 D76—D81 与 D82—D84 两组），本篇按"官方点名不支持"登记，不外推到其他载体：通话、消失消息、一次性查看、验证类、商品类与互动类在群接口里被列在支持面之外，管理员隐藏成员名单、编辑消息、删除消息三类动作同样被列在不支持动作里。清单成分与第 11 节政策层"社区写在准则里、接口层没有社区专章"的覆盖差（附表 F 第 25 项）是两件事，本篇不把它们并成一条。

## 10.2 参与者接口

「Groups Participants API」(whatsapp_reference_groups_groups_participants_api_md) 是群组接口里唯一以"人"为操作对象的页面。这一页的形状可以这样登记：一个路径段 `participants`，两个方法，两个方法共用同一个请求体属性 `participants`，数组元素是一个内联对象，对象里唯一的必填字段是 `user`，其说明把两种身份并列（号码或 WhatsApp 用户 ID）；两个方法各自返回一条 200 响应，官方对两条响应的描述分别写成"移除请求已处理"与"添加请求已处理"。这一页里能成句引用的官方文字只有鉴权那两句：

> `The user agent string identifying the client software making the request.`
> `This should be a valid access token obtained through the appropriate OAuth flow or system user token.`

值得写进正文的是三件事。**第一，数组上限。** 单次请求的参与者数组长度被写成 **8** 名参与者，这个数字在附表 B 第 44 行登记，出处页是概览页；指南页「Group management」里同值另写了一句，本篇按两行分别引用：

> `Maximum 8 participants.`

同一页还有一句对空数组的否定：

> `The array cannot be empty.`

这两句合起来给出参与者请求的最小与最大形状，但**没有**给出"一个群最多能有多少名参与者"——接口页对成员总数只字未提，成员总数只出现在消费者端页面与 10.4 的响应字段里，这一处留白本篇不动。**第二，两个方法共用同一句参数说明。** 这是接口面最要紧的写法异常，本篇把它作为附表 F 第 11 项登记，正文在 13.1 展开它的差异，这里只写明事实：添加与移除两个端点在参考页上各有一张请求体表，两表里 `participants` 那一行的说明文字逐字相同，都沿用了"移除"口径的措辞，而端点标题本身分别写成 Add Group Participants 与 Remove Group Participants。**第三，身份标识的三种写法。** 参与者在接口里可以被三种东西指代：电话号码、WhatsApp 用户 ID，以及官方在回调面反复出现的 `user_id`、`parent_user_id` 与 `username`；这套标识规则属于回调面，本篇在 10.5 集中登记。与参与者操作有关的失败面不在这一页，而在错误码页：附表 D 的错误码组里有四条说明直接落在"人"上——请求里出现重复参与者、参与者总数越限、参与者已不在群里，以及一次请求里各参与者的操作只有部分成功（HTTP 状态为 206）。四条原文逐条见附表 D，本篇不在这里重复，也**不**据此给出任何一种"把大量号码接入群"的路径说明；接口层存在添加端点这一事实，与官方在指南页写的"不能手动加人"（10.3 引）并不由本篇调和。

## 10.3 邀请链接与加入请求接口

邀请链接在接口面出现在两个层次，官方各写一遍。指南页「Group management」(whatsapp_groups_reference_md) 写的是模型：

> `When you create a new group, an invite link is created for inviting participants to the group.`
> `You can send this invite link to WhatsApp users interested in joining the group.`
> `Since you cannot manually add participants to the group, simply send a message with your invite link to WhatsApp users who you would like to join the group.`

第三句是本篇在附表 F 第 11 项甲侧登记的原句，它与 10.2 说的参考页添加端点构成接口面最明显的一处不一致；本篇把两句各归各处，不做取舍。链接的可重置性官方写成一句带后果的话：

> `Once an invite link is reset, all previous invite links will become invalid.`

另有一句把"链接指向谁"写成自动化行为，属于字段口径：

> `When you provide the group ID in the API request, it will be automatically translated into the corresponding group invite link upon message delivery.`

「Groups Invite Link API」(whatsapp_reference_groups_groups_invite_link_api_md) 把这件事收成两个方法：一个删除现有邀请链接、一个新建邀请链接（方法清单见附表 D 的 D12、D13 两行，附表 C 第 57 行把这一对登记为"写明但互相冲突"）；新建成功的响应里官方给了三个字段位 `messaging_product`、`success` 与 `invite_link`，其中 `invite_link` 是链接本体。指南页对模板侧的补充是三条：

> `Use these pre-defined templates to send group invitations as utility messages.`
> `In order to keep the template priced as utility, you cannot modify it when you copy it from template library to your Messaging account.`
> `Template approval may require up to 24 hours.`

第二句是"类别与可改性绑在一起"的写法；第三句的 24 小时在附表 B 第 81 行登记，性质写作处理时长上限；第一句则把邀请动作归到 utility 类别，归口在 10.4 的计费面。

加入请求一侧由两页共同构成。「Groups Join Requests API」(whatsapp_reference_groups_groups_join_requests_api_md) 给出三个方法（DELETE、GET、POST，见附表 D 的 D14—D16 三行），字段面里请求侧是 `join_request_id`、`wa_id` 与 `creation_timestamp`，响应侧按结果分成 `approved_join_requests`、`rejected_join_requests`、`failed_join_requests` 与 `errors` 四个集合；指南页对其中两项各有一句定义式写法：

> `Unix timestamp indicating when the join request was created.`
> `ID of join request to reject.`

模型层的三句写在指南页，本篇按"接口页把审批当成一条已在文档里成立的通路"登记（附表 F 第 12 项乙侧）：

> `This means that if a WhatsApp user wants to join your group, you can approve or reject their request.`
> `You can create groups that require join request approval.`
> `Use the contents of the webhook or API response to approve or reject requests.`

开关本身只有一个字段 `join_approval_mode`，取值域只有 2 个取值（附表 B 第 97 行登记总数；逐条见附表 D 的 D8—D11 四行，两个取值在建群请求体与群设置响应里各出现一次），默认档官方写成条件句：

> `If omitted, join_approval_mode is set to auto_approve by default.`

这一句同时给出"省略字段"与"显式取值"的等价关系，属于本篇在附表 C 第 49 行登记为"写明默认值与可改方"的那一类。审批通过之后官方写的是可访问性，而不是队列清理：

> `Upon success, the API will respond with the following JSON payload, and WhatsApp users whose join requests were approved will be able to access the group when tapping the invite link.`

还有一句把被移除者的状态写成不可逆，这一句与错误码页的 131212 相互对照（附表 C 第 58 行）：

> `If a participant is removed from a group, they can no longer join the group via an invite link.`

## 10.4 查询、分页、计费与号码资格

查询与分页写在两页。「Groups Query API」(whatsapp_reference_groups_groups_query_api_md) 把群本身收成三个方法：删除群、读群元数据、改群的主题／说明／头像（见附表 D 的 D19—D21 三行），三个方法共用同一个群组标识路径；读操作带一个可选的 `fields` 参数，官方对"不点名要什么"的效果写了两句，本篇按同页两处写法各引一次：

> `If no fields are passed in, only the group ID is returned.`
> `Specifying no fields in the query parameters will just return the group ID and messaging product.`

响应侧的成员计数字段与"是否含业务方"绑在一起，官方给的是排除式写法，它和 10.2 的数组上限是两个对象，不互换：

> `The total number of participants in the group, excluding your business.`

头像的三个数值也在这条线上：格式仅接受 JPEG、上限 **5 MB**、最小 **192 x 192** 且须为正方形（附表 B 第 52、53 行），官方成句的一句是：

> `Image should be in square, that is, height = width.`

号码维度的「Groups Management API」(whatsapp_reference_whatsapp_business_phone_number_groups_management_api_md) 给出 GET 与 POST 两个方法（附表 D 的 D22、D23 两行），主题与说明的长度分别写成 **128** 个字符与 **2048** 个字符，且官方对主题多补了一句空白处理规则（附表 B 第 49、50 行）：

> `Maximum 128 characters.`
> `Whitespace is trimmed.`
> `Maximum 2048 characters.`

分页参数的取值域 **1～1024**、默认 **25** 记在附表 B 第 51 行，出处页是指南页；游标失效只在错误码页以 131059 一条出现，官方要求从头部重新开始（原文见附表 D 的 D26 行）。

计费面单独一页，「Groups API Pricing」(whatsapp_groups_pricing_md) 的总口径第一句就是把单位换成人：

> `Customer service windows work differently when using Groups API.`
> `However, you are charged each time a billable message is delivered to someone in the group.`
> `Messaging rates for Groups API are the same as per-messaging pricing rates for 1 to 1 messaging.`

附表 B 第 57 行按"计费口径"登记第二句，第 58 行登记官方的举例并把它标注为示例值而非规则：

> `If the message was delivered to only 4 of the 5 users, you would only be charged for the 4 delivered messages.`

免费额度一句把群与 1:1 绑在同一个池子里（附表 B 第 56 行，**1,000** 条）：

> `The 1,000-message free tier is shared with 1:1 service messages for the same business phone number, and each group send consumes one unit per delivered recipient.`

日期分界两句记在附表 B 第 88、89 行（**2026-09-30** 与 **2026-10-01**），官方对分界前的免费面写的是：

> `Through September 30, 2026, this allows you to send utility and non-template messages for free.`

类别面有三个 `pricing.category` 取值（附表 B 第 98 行；逐条见附表 D 的 D3—D5 与 D1、D2 两组），其中一条被明确排除在档位之外：

> `Group utility messages are not eligible for volume tiers.`

分析口径上有三处官方自己写明的限制：国家码过滤器在群消息上不生效、并非所有状态回调都带计费信息、入站类与其他类别不能合并查询：

> `The country code filter is not supported for group sent messages.`
> `Not all Message Status webhooks include pricing information.`
> `Please query individually and try again.`

号码资格与善后写在「Groups API FAQ」(whatsapp_groups_faq_md)。删除群的后果官方写成两句，第二句解释了"删了群还会收到回调"这一现象：

> `No members, including you, will be able to message the group.`
> `Cloud API delivers any messages or statuses it received before you deleted the group, so you may still receive webhooks for those messages or statuses.`

同一页对"参与者为什么进不来"给了三条并列可能，本篇按官方原样登记为三种原因、不排序也不判定主次，三句分别是「The invite link might have been deleted.」「You removed the participant from the group previously.」「The group is already full.」；对"链接怎么发出去"官方只写了一句通道说明（「You can send the invite link over a 1:1 conversation.」），另有一句补充自建模板的可能（「You can also create custom, free-form marketing templates.」）。可用范围官方写成对 Cloud API 国家清单的引用，而"哪些号码形态用不了群功能"在概览页与入门页各写一遍（附表 C 第 55 行）。本节四页之间没有新增数值冲突，唯一跨页同题的是 `join_approval_mode` 的默认档与消费者端"入组须批准"开关的默认档，两处都是"默认关闭／默认自动批准"的同向表述，已在附表 C 第 6 行与第 49 行分别登记。

## 10.5 回调：字段、子场景与载荷

群组回调面由两页构成：「Webhooks for Groups API」(whatsapp_groups_webhooks_md) 写字段与事件，「Group messaging」(whatsapp_groups_groups_messaging_md) 写消息状态。字段层官方订阅要求写成 4 个字段（附表 B 第 95 行），前两个的定义都是被动句，这两句是本节主干：

> `A group_lifecycle_update webhook is triggered when a group is either created or deleted.`
> `A group_participants_update webhook is triggered when a WhatsApp user joins a group with an invite link, requests to join a group, cancels their request, or when one or more join requests are approved.`

四个字段下的事件小节总数为 **16**（附表 B 第 96 行按 4／7／3／2 分布登记），加上消息状态一节下的四个小节，本篇在附表 D 的回调子场景组共登记 **20** 条小节标题原文（D44—D66）。子场景里的两处取值口径由官方写成条件句，本篇按取值登记：

> `For join requests: GROUP_REQUEST_TYPE is set to group_join_request_created.`
> `The initiated_by field is set to participant, and the removed_participants list identifies the participant who left the group.`

成员自行退群这件事的发起方写法与业务方移除不同，官方单独成句；设置变更一条把可改对象穷举成三项，与 10.4 的头像／主题／说明三个字段正好对应：

> `This webhook is sent when a group participant leaves the group.`
> `Use this webhook to update your group's subject, description, and photo.`

消息状态面在消息页与回调页各写一遍，两句的差别只在聚合措辞：

> `When you send messages to a group, you will receive a webhook when the message is delivered or read.`
> `Each webhook you receive is only ever in reference to a single message sent to a single group and a single status type.`

聚合行为官方写成三类条件，本篇逐条登记、不合成一条通则：同时产生的状态可能被合并、不同时间产生的状态可能分开、而合并本身不保证：

> `If multiple participants' statuses are generated at approximately the same time, they may be combined into a single webhook.`
> `If statuses are generated at different times, you may receive separate webhooks for each participant.`
> `However, aggregation is not guaranteed.`

消息页另有一句把聚合后的载荷形状写清，回调页对同一件事的写法是总数加两句分类：

> `This means that if you send a message and are set to receive several read or delivered statuses, you receive a single aggregated webhook containing multiple status objects.`
> `There are two types of aggregated message status webhooks you can receive.`
> `Each webhook you receive will be in reference to a single message sent to a single group and a single status type, that is, single group, single status by multiple participants for a single message.`

群状态字段是治理接口，官方在这里写了平台的判定手段与两种后果，这一段是接口面与第 11 节执法面的直接接口：

> `WhatsApp uses advanced machine learning technology to evaluate group information including group subjects, profile photos, and group descriptions.`
> `We may prevent further activity in chat groups to comply with our legal obligations.`
> `You may receive a webhook if a group you manage is suspended.`
> `You may also receive a webhook if a suspended group you manage becomes clear of suspensions.`

载荷字段层在「Group messages webhook reference」(whatsapp_webhooks_reference_messages_group_md)。触发事件官方写成两句主动句，读／发／收三个动作合在一条字段说明里：

> `A WhatsApp user or a business sends a message to a group.`
> `A WhatsApp user or a business receives a message within a group.`
> `This field appears when a WhatsApp user or business sends, receives, or reads a message in a group.`

群标识本身的定义是一句限定式写法，寻址侧的两个字段变化官方写成 now 句，本篇按"字段位"登记、不按"新功能"登记：

> `The string identifier of a group made using the Groups API.`
> `The recipient_type field now supports group as well as individual.`
> `The message object includes a group_id field to indicate this is a group message.`

身份可见性在这一页被写成条件句，是接口面与隐私面的接缝：

> `Omitted if the user has adopted a username and their phone number is unavailable.`
> `Only included if the user has adopted a username.`
> `Only included if your business portfolio is enrolled in parent BSUIDs.`
> `Note that a WhatsApp user's ID and phone number may not always match.`

同页还有一句对识别字段的选择：

> `Use user_id and from_user_id to identify the WhatsApp user.`

消息类型面官方在该页写成四类（附表 B 第 99 行；逐条见附表 D 的 D72—D75 四行），与概览页 Quick facts 的四类一致。计费信息如何进入回调已在 10.4 写明"并非所有状态回调都带计费"，本篇不重复。

## 10.6 错误码：官方给出的失败面

失败面集中在一页，「Groups API Error Codes and Troubleshooting」(whatsapp_groups_error_codes_md) 共 **15** 条错误码（附表 B 第 94 行登记总数；逐条见附表 D 的 D24—D38 十五行，每条的错误码、错误名与说明三段都回表核验），同页出现的 HTTP 状态共 **4** 个（附表 D 的 D39—D42 四行）。按说明的对象可以分成三层。对象是群本身的写成了这一句：

> `Cannot send messages to single member groups.`

对象是成员与加入请求的写成五句：

> `Duplicate participants in the participant array input.`
> `Group participant size exceeds limit.`
> `Not all participant-level operations in the request succeeded.`
> `Participant is not a part of the group.`
> `Group join request does not exist.`

这一组里只有 131201 一条配的不是 400 而是 206，即"部分成功"在同步响应层面自成一档；本篇按附表 D 的成对登记引用，不在正文重排档位。对象是号码与账号的三条写成资格、限流与临时停用，其中两条官方只给机制、不给阈值，本篇在附表 B 第 59、60 行按"未给出数值／官方未写明"登记：

> `Groups APIs are only available for eligible phone numbers.`
> `Group operation failed because there were too many group operations from this phone number in a short period.`
> `Group creation is temporarily disabled due to excessive marketing messages sent by the Messaging account in customer service window over the past 7 days.`

第三条值得单独说明：它是整组错误码里唯一把**发送行为**与**客服窗口**写进触发条件的一条，也是接口面与政策执法面唯一直接重合的一处；它的条数阈值官方没写，本篇不折算成任何每秒或每小时数字（该句的留白登记在附表 F 第 24 项）。对象是头像与分页的写成四句：

> `Width and height of the image must be equal.`
> `Image width and height must be greater than 192px.`
> `The cursor has either expired or become corrupted.`
> `Start pagination from the beginning again.`

最后一条挂在政策上，与 10.5 的群状态回调成对：

> `The group violates platform policies.`

本节的红线在此重申：错误码是**失败形态的清单**，不是重试策略的输入。官方在这一页给出的唯一处置建议是分页游标那一句的"从头部重新开始"，其余各条官方没有写恢复时间、没有写降级顺序、也没有写可提交复核的通道；这些留白按附表 F 登记，本篇不补。

# 11 政策与责任层：条款怎么写这四类载体

第 10 节读的是"官方把群写成了什么对象"，本节读的是"官方把责任写在谁身上"。四页法律与政策文本、十四页帮助中心文本在这里被按同一组问题重排：适用范围写到哪几类载体、内容责任归谁、平台在什么条件下介入、介入之后用户还能做什么。本节只登记条款的写法与层级差别，**不提供申诉话术、不提供提高发送量或规避审核的做法**；凡官方写成建议句、条件句或灰度句的地方，本篇照其句型引用，不改写成承诺。

## 11.1 服务条款与消息指南

商业侧的适用范围由「WhatsApp Business Messaging Guidelines」(legal_messaging_guidelines) 第一句划定，这句是本篇把"社区"写进治理面的唯一直接依据（附表 F 第 25 项甲侧）：

> `These Messaging Guidelines (these “Guidelines”) apply to chats, calls, group chats, status updates, and communities.`

同一句里没有频道——频道有自己的准则与补充条款，归 11.2。与群发、群控直接相关的是对"对抗性行为"的定性，官方先给一条命令句，再给一条穷举句：

> `Do not engage in adversarial behavior.`
> `This includes efforts to undermine WhatsApp integrity systems, abuse reporting tools, compromise user accounts, scrape data, or use unofficial clients, bulk messaging, auto-messaging, auto-dialing, or automation to harm WhatsApp or our users.`

第二句把 bulk messaging、auto-messaging、auto-dialing 与"使用非官方客户端"并列在同一条里，这是准则层的写法；条款层的同题写法在「WhatsApp Terms of Service」(legal_tos)，官方式地以一条六项枚举句表达，其中 (e) 项与 (f) 项是本系列反复引用的那一行：

> `You will not use (or assist others in using) our Services in ways that: (a) violate, misappropriate, or infringe the rights of WhatsApp, our users, or others, including privacy, publicity, intellectual property, or other proprietary rights; (b) are illegal, obscene, defamatory, threatening, intimidating, harassing, hateful, racially or ethnically offensive, or instigate or encourage conduct that would be illegal or otherwise inappropriate, such as promoting violent crimes, endangering or exploiting children or others, or coordinating harm; (c) involve publishing falsehoods, misrepresentations, or misleading statements; (d) impersonate someone; (e) involve sending illegal or impermissible communications such as bulk messaging, auto-messaging, auto-dialing, and the like; or (f) involve any non-personal use of our Services unless otherwise authorized by us.`

条款层另有三句构成"义务—许可—后果"的最小链，本篇按此顺序登记：

> `You must use our Services according to our Terms and posted policies.`
> `You must access and use our Services only for legal, authorized, and acceptable purposes.`
> `If you violate our Terms or policies, we may take action with respect to your account, including disabling or suspending your account and, if we do, you agree not to create another account without our permission.`

第三句末尾的"未经我方许可不得再建账号"是本篇判定"换号继续"这一做法性质的直接依据，本篇只写出这一句的存在，不推导任何规避方式。处置的输入面官方写得很克制：准则页说举报的入口在用户手里（`Users can report WhatsApp contacts, groups, communities, status updates, or specific messages that potentially violate these Guidelines.`），审查的手段与依据分别是「WhatsApp may use both automated processing techniques and human review teams to detect and review information available to us including account, group, and community profile information, as well as reported messages, for potential violations of these Guidelines.」与「WhatsApp may take action against violations of our terms of service, our policies, or these Guidelines, based on the limited information available to WhatsApp, including basic account, group, and community profile information, as well as messages reported by other users.」两句里的 information 清单只到账号、群与社区的**资料面**加上被举报的消息，这一边界与 10.5 的"端到端加密不因治理而失效"是同一条线。商业侧的执行方式另有一页，「WhatsApp Business Platform Policy Violations」(whatsapp_policy_enforcement_violations_md) 写的是一项结构化、一致化的执法口径：

> `We are announcing a structured, consistent approach to how the Commerce and Business Policies are enforced on the WhatsApp Business Platform.`
> `The list below outlines specific policy violations a business could receive.`
> `We may adjust this list over time.`

该页的清单按类别成行（类别名逐条见附表 A 登记位与 12.1 的解析口径说明），并给出一条与接口面对得上的订阅建议：「Subscribe to the account_update webhooks to get real-time notifications about these policy violations.」——这是商业侧违规的唯一官方实时通知面，与 10.5 的 `group_status_update` 分属两套载荷。

## 11.2 频道条款与频道指南

频道是唯一有独立条款与独立准则的载体，两句定位式写法分别在这两页：

> `WhatsApp Channels is an optional, one-way broadcasting feature within WhatsApp, separate from private messaging, designed to help people follow information from people and organizations that are important to them.`
> `Channels is a one-to-many broadcast service that allows you to view and interact with relevant and timely updates shared by other WhatsApp users.`

前者出自「WhatsApp Channels Guidelines」(legal_channels_guidelines)，后者出自「WhatsApp Channels Terms of Service」(legal_channels_terms)；两句都写"单向"，但一个写在功能层、一个写在服务定义层。准则页对管理员的表述是 should 句，帮助中心同一主题写的是 responsible for 句，这一强度差登记在附表 F 第 26 项：

> `Channel admins should be respectful of their followers and avoid sending too many or low-quality updates, which could lead recipients to unfollow their channel.`

条款页则把内容与体验一并归给管理员（`Channel admins are responsible for the content on their Channels and are required to maintain an age-appropriate and safe experience for their followers.`），并另有一条把群发式行为写进义务面：「You must access and use Channels only for legal, authorized, and acceptable purposes.」介入条件官方写成一条以重复违规为前提的句子，且把量与性质并列为判定因素：

> `WhatsApp will suspend channels if admin(s) repeatedly post content that violates our terms and policies, including illegal content.`
> `The decision to suspend a channel will depend on the amount, nature, and severity of the violating content, and, if identifiable, the intent of the user.`

介入之后用户能做的事，官方在两处各写一遍：准则页写站内申诉（`If you disagree with a decision we have made, you can appeal that decision from within your Channels info page.`）与欧盟用户的庭外解决途径，帮助中心写"每条告警只能提交一次"的次数限制与"仅频道管理员可提交"的身份限制；前两句出自「How to request a review for a closed channel」(faq_394993313189648)：

> `Review requests can only be submitted once per alert, and only channel admins can submit requests.`
> `While only one of the admin can submit the review request, all admins can see the progress.`
> `All channel updates are removed from our servers after 30 days.`

第三句的 30 天在附表 B 第 65 行登记，与服务端留存上限（同页另两行）不同源、不合并。举报侧的时长与不可撤销写在「How to report a channel」(faq_3440358352849077)：

> `Report review typically takes 24 hours.`
> `Once a report is submitted, it can’t be cancelled.`

两句的 24 小时在附表 B 第 80 行按"处理时长（官方措辞为通常）"登记；同页还有一句写明举报不通知被举报方、以及被举报内容若不违规则频道不受影响，本篇只取前两句。报告的数据面在「How to request channels report」(faq_6480469855345352)，官方给的是一份可重复生成的自助导出，两条时限与一条不可撤销：

> `Your channels report will typically be available three days after the date you requested it.`
> `Once you request a channels report, you can’t undo or cancel your pending request.`
> `This will create a new channels report every month until you choose to turn off automatic reports.`

三句对应附表 C 第 39 行的"默认按月生成"与"使用者本人可关"，是频道面上少见的"写明默认值与可改方"的例子。

## 11.3 数据、隐私与举报

数据面只有一页，「Data Privacy & Security」(whatsapp_data_privacy_and_security_md) 给的三条都是留存与用途口径，本篇按三条各引一次（30 天在附表 B 第 83、84 两行分别登记，因为两个对象的留存起点不同）：

> `Messages have a maximum retention period of 30 days in order to provide the base features and functionality of the Cloud API service; for example, retransmissions.`
> `Cloud API uses identifiers as sources or destinations of individual messages; as such, Cloud API deletes them within 30 days of the last status update (sent, delivered, read) of a message, unless otherwise directed.`
> `WhatsApp protects users by detecting unusual messaging patterns (like a business trying to message all users) and collecting spam reports from users.`

第一句是"最长留存"，第二句把删除条件绑在最后一个状态更新上，第三句把"试图给全部用户发消息"这一模式**明确写成检测对象**——这一句是本系列关于群发风险口径的直接文本依据，它只说明检测存在，不说明阈值。群组与社区一侧的隐私写法在「How to keep your groups and community safe」(faq_1003616827680090) 与「How to stay safe in groups on WhatsApp」(faq_424124173736394)，两页各给一句关键口径：

> `Report inappropriate content: Members should report inappropriate or harmful content to WhatsApp to help us keep it safe.`
> `When you report a group, WhatsApp receives up to five of the last messages sent to you in the reported group.`

第二句把"举报能让平台看到多少内容"写成了具体条数（附表 B 第 20 行登记为 5 条），且官方在同页补了一句「At this time, it's not possible to hide group member's phone numbers in a group.」，成员号码的可见性因此不是开关。频道一侧的隐私面在三页：「About safety and privacy on channels」(faq_1318001139066835) 写关注者的隐身边界（`Admins can see your profile name and, depending on your profile settings, your profile photo and how you interact with their channel content.`），「Building Private, Safe, and Secure WhatsApp Channels」(faq_958928238510970) 写"不被加入"这一条（`Users can't be added to a channel without choosing to follow it and they will only see updates for the channels they choose.`），「About safety and privacy on channels for admins」(faq_671443411431514) 写管理员侧的三条硬事实：

> `It's not possible to create a private channel.`
> `All channels are publicly accessible.`
> `As a channel admin, you're responsible for keeping the content on your channel safe, relevant, and age-appropriate for your followers.`

第三句是附表 F 第 26 项乙侧原句，本篇把它与 11.2 的 should 句分两层登记，不合成一条。广播列表没有独立的隐私页，其可见性口径挂在"是否存入通讯录"上，已在 9 节与附表 C 第 41、47 行登记，本节不重复。

## 11.4 内容责任、平台介入与执法

责任归属在条款层是一句全部大写的免责句，官方在同一段里先写"不控制"再写"不 obligated"，两句合起来构成"平台不预筛内容、但事后介入"的写法：

> `WE ARE NOT RESPONSIBLE FOR AND ARE NOT OBLIGATED TO CONTROL THE ACTIONS OR INFORMATION (INCLUDING CONTENT) OF OUR USERS OR OTHER THIRD-PARTIES.`
> `We work to protect the safety, security, and integrity of our Services.`

介入之后的账号后果已在 11.1 引过；这里登记的是行为责任如何在参与者之间回流。「About reporting and blocking on WhatsApp」(faq_414631957536067) 写了两条，第一条关闭了"绕开站内流程"的想象，第二条把历史消息的转发者与责任绑在一起：

> `Contacting WhatsApp outside of the in-app review process won't expedite the review process or impact the decision.`
> `If someone you sent message history to reports content that was sent as part of the message history you sent, you'll be reported.`

第二句与 13.3 的历史消息两条（附表 F 第 3 项）构成同一处的因果闭合：能把历史发给新成员的人，也把被举报的风险发给了自己。拉黑与举报的边界在三页各写一遍：「How to block and report someone」(faq_1142481766359885) 写明群内可见性不因拉黑而消失（`Blocking a contact won't stop that user from seeing your messages in a group you both participate in, and you'll see their messages in that group as well.`）并给出一条连带后果（「If you report a group, you'll be removed from that group.」）；「How to block a group admin」(faq_841324506880320) 写明对象限制：

> `At this time, it's not possible to block a group.`
> `However, you can exit the group and block the group admin.`

第三页是转发面，「About forwarding limits」(faq_1053543185312573) 把限速写成防滥用理由（`You can forward messages to up to five people or groups at a time.` 与 `This also limits and helps prevent spamming and misinformation.`），「How to forward from channels to chat or status」(faq_305393838527652) 与「How to forward to your channel」(faq_699347952293912) 则把同一套计数写到频道：

> `If an update has already been forwarded many times, you can only forward it to one chat at a time.`
> `Everyone can see the total number of forwards in a forward button below the update.`
> `Channel updates are reviewed against the channels guidelines.`

第三句是"审核存在"的表述，不是"审核标准"的表述；本篇据此登记的只有次数、对象与可见面三个维度。**本小节不给任何申诉话术、不给提高发送量的做法、不给规避审核的做法**——官方在这些页面上写的都是边界句，把边界句改写成操作指南不是本篇的工作。

## 11.5 商业侧政策与客服窗口

商业侧的责任文本在帮助中心层有一份"负责任使用"清单，「How to use WhatsApp responsibly」(faq_361005896189245) 先给适用面与后果，再逐条给禁令，其中三条与四类载体直接相关：

> `All users of WhatsApp should review the following guidelines to ensure responsible use of WhatsApp.`
> `Using WhatsApp in any of the ways listed below may cause your account to be banned.`
> `Don't bulk message, auto-message, or auto-dial using WhatsApp.`
> `Also, don't create accounts or groups in unauthorized or automated ways, or use modified versions of WhatsApp.`
> `If you add someone to a group and they remove themselves, honor their decision.`
> `Please keep in mind, frequent use of broadcast messages may lead people to report your messages, and we ban accounts that are reported multiple times.`

第三句与 11.1 的准则句、条款句同题，是三份文档里最外层的写法；第六句是本篇见到的唯一把"广播"与"被举报次数"写成因果的官方句子，而检测手段官方写成一句（`WhatsApp uses both machine learning technology and reports from users to detect and ban accounts that send unwanted automated messages.`）。同一页还把群组治理义务交给管理员：「We've created an admin only message setting for WhatsApp groups.」与「If you're an admin, you can decide if all members or only group admins can send messages within the group.」两句与附表 C 第 2 行的"只写方向"登记一致（默认档官方未写明）。账号级开关在「How to use Strict account settings」(faq_846698564598022)，官方用一句定义与三句效果描述它，其中与"谁能把你加进群"有关的是：

> `Strict account settings is an optional, lockdown-style security feature that, when enabled, reduces your vulnerability to cyber attack by limiting functionality.`
> `Only known contacts or a pre-established,  more-selective list of people can add you to groups.`
> `You can only turn on and off Strict account settings from your primary device.`

第三句的"仅主设备"与 10.1 的资格四句同形：都是把能力绑在身份或设备形态上，而不是给出可调档位。权限面在「About WhatsApp permissions」(faq_1533984453785262)，两句写的是首次使用即索权与关权的代价：「When you first use features in WhatsApp, WhatsApp might require access to information or applications from your phone.」「Turning off permissions might cause WhatsApp to lose certain functionality.」隐私设置的枚举面在「How to change your privacy settings」(faq_3307102709559968)，本节只取与群有关的一句：

> `Contacts from your address book, except those you exclude, can see your last seen and online, profile photo, about, links, status, or add you to groups.`

频道商业化在「Privacy at a Glance: Additional Information about using Status and Channels」(faq_1170535238421230)，官方把公开性与付费推广写在同一页：

> `Channel statuses are public.`
> `Additionally, in some countries, admins will be able to pay to promote their channel to other users.`
> `Channel subscriptions are only available in certain countries and may not be available to you yet.`

第三句与 13.4 的灰度句（附表 F 第 15 项）同题，本篇在 13.4 处理两页的宽窄差。商业侧与接口侧的交点只有一个：客服窗口与计费的口径写在 10.4，政策文本里的"许可目的"与"不得使用"写在 11.1，两者之间官方没有写换算规则，本篇也不写。

# 12 枚举与维度方法层：本篇怎么数出来的

本节只回答一件事：前面十一节里出现的每一个"上限""默认""取值""冲突"，是按什么规则从官方文本里取出来的。规则全部来自四张附表的生成口径，本节不复述官方文本内容，因此**不引入任何新页面**（12.1 唯一引用的错误码页已在 10.6 登记）。

## 12.1 取值域的解析规则

「Groups API Error Codes and Troubleshooting」(whatsapp_groups_error_codes_md) 是本篇唯一按"单元格整体"解析的页面，它的形状决定了规则：一行三格，第一格同时放错误码与错误名，第二格放说明句，第三格放 HTTP 状态码与短语；错误说明写成完整句，单元格里另有换行标记。本篇取的是"每格一句"的粒度，示例取其中一条：

> `The group was not found, either because it doesn't exist or you are not a member.`

解析规则有五条，任何一条不通就中止出表而不是猜测：**取值域只认官方穷举句式**——官方写成 Values can be: 的单元里带反引号的项才算取值，附表 D 按这一句式拆出 5 组共 11 个取值；**接口方法只认 Method 列**——附表 D 的接口方法组由 5 个 reference 页逐页解析，页数不足 5 或方法集合越出 GET、POST、DELETE 即中止；**错误码与状态码只认表格行**——条数不等于 15 即中止，且每条的错误码、错误名、说明三段都要回表核验；**回调字段与子场景只认标题层级**——字段组不等于 4、子场景不等于 20 即中止；**离散选项只认官方选项列表**——消失消息时长、置顶时长、邀请链接有效期、静音时长四组按页内选项逐条登记。取值不等于规则：附表 D 合计 97 个取值、27 组、覆盖 14 页，其中 6 行的说明列超过长度按整词截断，截断后仍是原句前缀。官方把一条规则写在单元格里、无法套出句子形状的，本篇按整格照抄，全篇只有 1 格（10.2 说的那句参数说明）。凡官方写成示例的数字（10.4 的 4 与 5）一律登记为示例值，不升为规则；凡官方只给机制不给数值的地方（10.6 的 131208 与 131214）一律登记"未给出数值"，不折算。

## 12.2 附表 E 的维度选取

对照表的选取标准有三条：这一维度**官方自己成句写过**、四类载体里**至少两类有字**、且这一维度**会影响能否做某件事**（而不是界面外观）。按这三条选出 30 个维度，逐字原句共 76 格回表核验，官方未按载体区分的 44 格按原样留白——这 44 格既不代表该载体没有这条规则，也不代表本篇判定其不存在。逐字原句按载体分布是群组 24 格、社区 19 格、频道 19 格、广播列表 14 格，引用页面去重 48 页。分布不均本身是口径信息：广播列表的官方文本最少，所以它在 13 节里出现的冲突与留白也最少，这不是本篇省略的结果。第 1 行与第 2 行示范了两种最省的写法：E1 的四格全是各页的定位句（`Broadcast lists are a one-to-many communication.` 与 `With a channel, you can share one-way updates to an unlimited audience.` 这类），E2 的四格是四类载体各自的规模口径，其中频道那一格与 E1 复用同一句、出处页与原句不变；同一原句可以在多行复用，复用时不改出处页。超过 112 字符的原句在表内按字符截断并以省略号结尾，核验用的是未截断整句。

## 12.3 计数口径与不可引用页处理

数字的口径在附表 B：五档合计 99 行（一 53／二 7／三 26／四 4／五 9），去重后 57 个条目，计数单位 41 种，性质 43 种。"同一数字开两行"是硬规则——1,024 因为挂在不同对象上就占两行，100 因为分别管群组名称长度、社区名称长度、频道名称长度与历史消息条数就不止占两行；官方写成区间或多档的按整段一行登记（1～1024、24 小时／7 天／90 天），日期与版本只进第四档，本篇自己数出来的组数与个数进第五档（4 类载体、15 个错误码、4 个回调字段、16 个事件小节、2 个取值、3 个类别、4 种消息类型，以及登记位 126、不可引用 4 页）。开关的口径在附表 C：58 行、7 类载体、5 类写明度，写明默认值与可改方 18 行、只写方向 15 行、无开关（行为固定）19 行、以规模或身份为条件 3 行、写明但互相冲突 3 行；默认值或可改方两格里有一格写"未写明"的行共 16 行。冲突的口径在附表 F：30 项、留白 7 格、未取回页自动生成 4 行、差异性质 19 类、原句最长 219 字符、整格照抄 1 格。四张表的留白口径不同，本篇不合并：B 记"数值未给出"、C 记"默认值或可改方未写明"、F 记"两处写法不一致或一侧完全无文本"。不可引用页的处理只有一条：附表 A 判定 126 个登记位里可引用 122 页、不可引用 4 页（3 页正文未取回、1 页只有地域限定提示），这 4 页的标题与实测取回长度在附表 F 末尾 4 行逐行登记，正文全篇不引用它们，不为它们补写任何规则，也不引用第三方对同题页面的转述。全篇官方原句逐字回核 402 条、失败 0 条；本节所有计数都来自上述四张表的生成统计，不是正文临场点数。

# 13 冲突与留白：官方自己不一致的地方

本节唯一事实来源是附表 F 的 30 项：表外不新增"矛盾"，表内两项不合并。分派按差异性质就近落节，两处偏离先写明：**F7** 性质虽是"两页结论相反"，争点却是可见性权限，且出处两页又同为附表 C 的登记位，与 F6 连着读才成立，故落 13.2；**F2 与 F11** 可归 13.3，但争点是数值的写法而不是档位，且 F11 的乙侧是全篇唯一那格整格照抄、与 F1 同页同题，故留 13.1。四节不重不漏：**8＋9＋6＋7＝30**。

## 13.1 同一数值的多种写法

**F10**（计数是否含本人）甲「How to join a group in a community」(faq_967457667545238)：`The maximum group size is 1,024 members (1,025 including the group creator).` 乙「How to see group members」(faq_7179561392143247)：`A WhatsApp group can have up to 1,024 members.` 本篇以甲为准写"成员 1,024、含创建者 1,025"，乙只作旁证，不把两个数写成区间。

**F8**（枚举成分不同）甲「How to create a community」(faq_438859978317289)：`This limit includes the total number of members in the sub-groups and announcement groups within the community.`（同页前一句给出 2,000 这个上限）乙「About community announcements」(faq_582420703681043)：`You can add up to 2,000 members to a community and its announcement group.` 甲把上限拆成"分组＋公告组"两类成分、乙只写"社区及其公告组"，两页都没把一般讨论组列进枚举；本篇按枚举不同登记，不替官方补出缺失那一类。

**F9**（对象不同易混）甲「About general chat for communities」(faq_1418342145436771)：`Like other groups, the community general chat limit is 1,024 members.` 乙「How to reply and react to announcements」(faq_1859295147860069)：`Announcements groups support reactions and poll votes for up to 1,024 members.` 同一个 1,024 在甲是人数上限、在乙是回应与投票的人数门槛，乙页紧接的一句自己注明它与社区总人数上限是两回事；B 第 19、20 行分别绑定对象，不并成一行。

**F13**（载体间口径不同）甲「How to manage group admins」(faq_633713745095781)：`A group can have an unlimited number of admins, and any admin can make a member an admin.` 乙「How to create a WhatsApp Channel」(faq_794229125227200)：`You can invite up to 16 additional channel admins to help you manage your channel.` 群组写"无限"、频道写"额外 16 名"，16 的第二处出处见 B 第 22、23 行；两数分属两个载体，社区管理员数量落在下面的 13.2，不做跨载体类推。

**F1**（消费者端与接口端不同）甲「How to pin a message」(faq_294619079641794)：`You can pin up to four messages to the top of a chat.` 乙「Group messaging」(whatsapp_groups_groups_messaging_md)：`If three messages are already pinned when a new pin request is made, the oldest pinned message will be automatically unpinned.` 界面端记 4 条上限、接口端记 3 条自动解顶阈值，B 第 12、47 行各带自己的载体，不合并成"3～4 条"、不以一方改写另一方。**F2**（同页两处写法不一）两句同出 Group messaging：`When calling the API, only one message can be pinned at a time.` 与 `The display order of the pinned messages is based on the chronological order of parent messages, newest first.` 本篇按"一次发起一条、同时在顶最多三条"各引一次，不读成"最多只能存在一条"。

**F11**（接口页与指南页不同）甲「Group management」(whatsapp_groups_reference_md)：`Since you cannot manually add participants to the group, simply send a message with your invite link to WhatsApp users who you would like to join the group.` 乙出自「Groups Participants API」(whatsapp_reference_groups_groups_participants_api_md) 请求体表 participants 那一行——全篇唯一按整格照抄登记的条目原文：「Array of phone numbers or WhatsApp IDs to remove (max 8 participants)」——没有句末标点，形状是表格单元而不是句子（见 12.1）。指南页说不能手动加人、只能发链接；参考页的添加端点存在，其说明文字却沿用删除端点的写法。本篇据此写明"接口层存在添加端点、官方自己的描述文字与指南页不一致"，不据此推荐任何加人路径；数值 8 的成句写法另见 B 第 44 行。

**F14**（对象不同易混）甲「How to use broadcast lists」(faq_861663048350950)：`You can create as many broadcast lists as you like.` 乙「About broadcast limits」(faq_1176325363861991)：`You can send up to 35 free broadcast messages per month.` 一句管建几张列表、一句管每月发几条，两数不相乘也不互相封顶；两句分处 2.4 与 9.1，计量对象已各自写明。

本小节覆盖 F10、F8、F9、F13、F1、F2、F11、F14 共 **8** 项，用到七种不同的差异性质说法（对象不同易混出现两次）。

## 13.2 默认值与可改方的留白

先交代与附表 C 的关系：C 的 58 行里默认值或可改方一格写「未写明」的行共 16 行，控件名可在官方页面照抄的那些见 C 第 1、2、5、7、8、11、13、14、16、26、32、34、38、40、48、58 行。共同形状是**官方写了开与关各是什么，没写当前默认落在哪一档**：「How to change group admin settings」(faq_526742385997912) 的 `Group permissions` 面板里，`When turned on, all members can send messages in the group.` 与 `When turned off, only admins can send messages in the group.` 两句都完整、都没有默认档。

**F6**（默认范围与更大范围并存）甲「How to make changes to groups」(faq_493270532852273)：`By default, any group member can send messages or change the group name, icon, description.` 乙仍出自 faq_526742385997912：`If a group has more than 256 members, Edit group settings will default to Only admins.` 甲是通用默认值（全员可）、乙是群规模越过 256 之后的默认值（仅管理员可）；本篇按"默认值随群规模分岔"叙述，两个默认档都保留，不写成"全员可发言"一条通则，C 第 1 行那句"256 人及以下官方未写明"就是这一处理的结果。

**F7**（两页结论相反）甲「About community announcements for admins」(faq_742237357891080)：`Only the admin can see the total number of members in the Announcement group.` 乙「About community announcements」(faq_582420703681043)：`Everyone can see the total number of members in the announcement group in the member list.` 同一对象在两页给了相反的可见范围，而两页都没把它写成可拨的控件；本篇两句逐字并陈、各标出处，不判定孰新孰旧，也不写成两句都没说的折中。C 第 29 行因此标"写明但互相冲突"而不是"未写明"。

**F12**（两页同题各说一半）甲「Groups API」(whatsapp_groups_md)：`Groups are an invite-only experience where participants join using a group invite link you send them.` 乙回到「Group management」(whatsapp_groups_reference_md)：`Use the contents of the webhook or API response to approve or reject requests.` 甲把群组定性为仅邀请，乙所在的接口页把申请加入当成一条已在文档里成立的通路并给出审批动作；两句各引各的——甲说明入口模型、乙说明审批接口，字段与两个取值见 10.3 与 C 第 49 行——不合成一句"群组只支持邀请制"。

**F16**（限定词未展开）甲侧仍是 faq_1176325363861991 那句 `You can send up to 35 free broadcast messages per month.`，同页第一句把范围写成灰度句 `People in some countries might see a monthly broadcast limit.`；乙侧官方未写明——官方在数字前写了 free，却没在同篇给出更高档的条数、价格或获取方式。本篇只记"35 条、原文带 free 限定"，不推断存在付费档，也不给提升路径。

**F20、F21、F22**（三项性质同为"只给机制不给数值"，乙侧都是官方未写明）F20 甲出自「About building private, safe, and secure communities on WhatsApp」(faq_458610306367976)：`To limit noise and overload, only community admins can send announcements to all community members.` F21 甲出自「How to exit a community」(faq_1312647189536807)：`If you deactivate your WhatsApp account, communities and groups you created will also be deactivated.` F22 甲出自「How to remove a group from a community」(faq_970281987938470)：`Only community or group admins can remove a group from a community.` 三句都只写权限层级或连带后果，三页都没有写出社区管理员可设几名、一个账号能建几个社区、一个社区能挂几个分组；本篇只记权限、把这三页没有的数值标为留白，不从群组的 unlimited 或频道的 16 名外推，也不把 2,000 人换算成分组数。

**F24**（只给机制不给数值）甲「Groups API Error Codes and Troubleshooting」(whatsapp_groups_error_codes_md)：`Group operation failed because there were too many group operations from this phone number in a short period.` 乙侧官方未写明：不给条数、窗口长度与恢复时间。本篇只引这一句说明限流的存在与报错形态，不折算成任何每秒或每小时阈值（B 第 59 行按"未给出数值"登记，C 第 58 行的"只写方向"与之对应）。**F25**（一处存在一处缺失）甲「WhatsApp Business Messaging Guidelines」(legal_messaging_guidelines)：`These Messaging Guidelines (these “Guidelines”) apply to chats, calls, group chats, status updates, and communities.` 乙侧官方未写明：准则把 communities 列入适用范围，而本篇登记的开发者文档侧没有社区专章；本篇只登记这一覆盖差，不据此推断社区接口的存在或不存在。

频道侧的留白另有一处出处：「About creating a WhatsApp Channel」(faq_265055289421317) 写明 `Channel admins are visible to each other.` 与 `Followers can’t reply directly to updates or send messages to channel admins.`，即该页写出身份可见性与回复方向，却没写出 admin profiles 与可回应表情类型的默认档，这正是 C 第 32、34 两行落在"只写方向"的原因。

本小节覆盖 F6、F7、F12、F16、F20、F21、F22、F24、F25 共 **9** 项。

## 13.3 数值与档位互相冲突

**F3**（两页结论相反）甲「How to join a group」(faq_1139252413769848)：`New members who joined the group through a group invite link or QR code won’t be able to view message history.` 乙「About sending message history in groups」(faq_1062135416113130)：`You can send message history to new group members after they join including members who joined via an invite link.` 两句各绑各自的问题：前者答"新成员自动看得到吗"（看不到），后者答"管理员事后能不能发给他"（能）。本篇并列不取舍，不把任一句写成通则；C 第 20 行按"写明但互相冲突"登记。

**F4**（两处同值互证）被加者视角出自 faq_1139252413769848：`They can send up to 100 messages from the last 14 days.` 添加者视角出自「How to add and remove group members」(faq_841426356990637)：`You can send up to 100 messages from the last 14 days.` 两个数完全一致，B 按一个数字登记一次、出处页记两页（第 55 行）；两页同段的前半句一处写成 may choose、一处写成 may have the option，本篇不把这一条读成"一定能发"。

**F5**（起点口径与当前口径）甲仍出 faq_526742385997912：`If a group was started with fewer than 33 members, all group members can: Add others to the group Send invite links.` 乙「How to create and invite into a group」(faq_3242937609289432)：`Admins and members in groups with fewer than 33 members can create group links to invite new members to a group.` 甲以建群时的人数为条件、乙只说当前少于 33 人；本篇两种基准都写出并标明出处，不推断哪一个生效，也不把两者平均成一条规则。C 第 4、9 行把 33 人这条线与 @all 的 32 人分界登记成两个不同条件（B 第 7、6 行），本项只指向前者。

**F17**（默认值与可选值并记）甲「How to change your privacy settings」(faq_3307102709559968)：`The default message timer for disappearing messages is turned off.` 乙「How to turn disappearing messages on or off in a group」(faq_515705483748188)：`When you enable disappearing messages, you can choose to have messages disappear after 24 hours, 7 days, or 90 days, unless the messages are kept.` 默认是关闭、可选是三个时长；本篇先写默认再写可选档，不把 24 小时当默认值，也不省略 unless 从句里的保留消息例外；三档在 B 第 61 行按整段一行登记。

**F18**（同页两处写法不一）甲仍出 faq_515705483748188：`In a group chat, any group member can enable disappearing messages.` 与同页紧接的一句 `However, a group admin can change group settings to allow only admins to enable disappearing messages.` 乙「About disappearing messages」(faq_673193694148537)：`You can set messages to disappear 24 hours, 7 days, or 90 days after they’re sent unless that message is kept.` 两页对"谁能开关"与"所选时长影响哪些消息"分别给出写法，例外从句一处说是被保留的消息、另一处说是已发送的消息；本篇按各页原句分开引用，不改写成一条统一规则。

**F19**（两处同值互证）甲「How to rejoin a group chat」(faq_420332980275753)：`The maximum amount of time you’d have to wait would be 81 days.` 乙「How to rejoin a community」(faq_651003086597307)：`The most you’d need to wait is 81 days.` 81 天同值，但甲页前面给出递增机制、乙页只称该等待时长并直接给结论，两页互不引用。两页在 81 天之前各有一条"离开两次后等 24 小时"的起点句（原文见 3.5）：24 小时与 81 天不是两个互相冲突的上限，而是同一条递增曲线的起点档与封顶档，B 第 73—76 四行按两个载体各两行登记，本篇不据 81 天抹掉 24 小时、也不反过来。

本小节覆盖 F3、F4、F5、F17、F18、F19 共 **6** 项。

## 13.4 未取回页与官方自陈灰度

第一类是官方自陈灰度与表述层级不同。**F15**（枚举成分不同）甲「About becoming a promoted channel in the directory」(faq_1251246649902787)：`We’re rolling out channel subscriptions gradually, so it may not be available to you yet.` 乙「About adding subscriptions to your channel」(faq_1371180700817474)：`We’re rolling out channel subscriptions gradually to a select group of channels, so it may not be available to you yet.` 乙多出一段"只开放给一部分频道"的限定、甲只说逐步开放；本篇按更窄的那句写开放范围，并注明另一页没有该限定，不写成全量可用。

**F23**（官方自陈灰度、范围未公开）甲「About channels on the WhatsApp Business app」(faq_809740894165112)：`Where channels is available, we’re rolling out new features gradually.` 同页紧接的一句把"能创建频道"列为可能还不可用的功能；乙侧官方未写明：官方写明按地区与功能灰度，却没有地区名单、也没有每人可建频道数。本篇把可用性写成依原句的条件句，不列国家清单、不写开通步骤。

**F26**（文档层级不同）甲「WhatsApp Channels Guidelines」(legal_channels_guidelines)：`Channel admins should be respectful of their followers and avoid sending too many or low-quality updates, which could lead recipients to unfollow their channel.` 乙「About safety and privacy on channels for admins」(faq_671443411431514)：`As a channel admin, you’re responsible for keeping the content on your channel safe, relevant, and age-appropriate for your followers.` 两句主体同为频道管理员，准则页用 should 给行为期待、帮助中心用 responsible for 给责任归属，约束强度不同；本篇按准则层／帮助层分层引用（回指 11.2、11.3），不把一句写成另一句的结论。

第二类是本篇实测正文未取回的 4 页（F27—F30），四行由附表 F 末尾按登记表自动生成，逐行对应附表 A 判定的 4 个不可引用登记位（126 个登记位中可引用 122 页、不可引用 4 页：3 页正文未取回、1 页只有地域限定提示）。本篇在这里只登记这一条事实与它们的页面标识、页面标题，绝不引用、绝不复述、绝不推测其内容：

> faq_1351864989275592，标题「About channel subscriptions」，正文未取回；
> faq_245599461477281，标题「WhatsApp Channels Guidelines」，正文未取回；
> faq_813798063285846，标题「How to add or suggest a group to a community」，正文未取回；
> faq_894737126459640，标题「What is defamation and how do I report it on Channels?」，正文仅显示地域限定提示。

上面四行标题只作为登记标识出现，不是可引用的官方正文。四页里三页与 F15、F23、F26 同属频道题、一页属社区题；这一重合只登记为"同题页面中有一页正文未取回"，本篇不为它补写任何规则，也不引用第三方对同题页面的转述。

本小节覆盖 F15、F23、F26、F27、F28、F29、F30 共 **7** 项。

# 14 能答、拒答与留白

本节不引入新页面，只回指前面各节用过的页面标识、附表行号与实测计数，把三件事写清：官方文本能答到哪一层、本篇拒答什么、剩下的留白怎么用。

## 14.1 官方文本能回答什么

四类载体按同一组三问回收：**谁性能做什么、上限是多少、出问题官方写在哪**。

**群组**：第一问的答案在 5.1、5.2 与 13.2——控件名与面板成文于「How to change group admin settings」(faq_526742385997912) 与「How to make changes to groups」(faq_493270532852273)，C 第 1—8 行逐控件登记；第二问在 2.1 与 B 第 1—3、7—8 行；第三问在 4.2 与 11.3 引用的「How to manage group admins」(faq_633713745095781)。**社区**：第一问在 7.1、7.2、7.4 与 13.2，发言、任命、解除群组分别写在 faq_582420703681043、faq_742237357891080、faq_1418342145436771 与「How to remove a group from a community」(faq_970281987938470)，C 第 22—30 行逐行对应；第二问在 2.2、7.5 与 B 第 15—20 行；第三问在 7.6 与 F7、F20—F22——社区侧的"改不动"多数只写权限层级、不写数值，这一层缺口本篇如实留白。**频道**：第一问在 8.1—8.4（faq_265055289421317 与 faq_794229125227200）；第二问在 8.2、8.4 与 B 第 22—35 行，其中受众规模那一格官方写的是不限而不是数值（附表 E 第 2 行频道格）；第三问在 8.5 与 11.2，写在「How to request a review for a closed channel」(faq_394993313189648)。**广播列表**：第一问在 9.1、9.2——可达范围由是否把号码存进通讯录决定，写在「How to use broadcast lists」(faq_861663048350950) 与其商业应用侧同题页，C 第 41—48 行把它登记成"无开关（行为固定）"；第二问在 2.4、9.1 与 B 第 36、54 行；第三问的答案本身就是一条留白：广播列表没有专门的失败面页面，官方只写了临近上限的提示句与"看不到上限就按主文章口径"。

三问的共同边界只有一句：这些答案的边界**只到"官方写了什么"**。官方写了控件名就登记控件名、写了数值与单位就登记数值、单位与出处页、写了报错那一句就登记那一句；官方没写的运行时行为——多久生效、按什么顺序、在哪个客户端表现一致、报错后多久恢复——本篇一律不补，也不拿其他平台的惯例补。

## 14.2 本篇拒答什么（范围红线）

本篇是口径解读，不是操作手册。以下各项一律不做，各节内部也已逐处重复过同一界线（1.4、9.4、10 节首段、11.4、12.1）：**不提供批量发送脚本、发送队列、并发与重试参数、账号池与多号轮换、设备农场、注册与验证步骤、限速或频控规避、垃圾信息检测绕过、反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定；不提供任何可用于向非自愿收件人大量投递消息的方法。**

这条红线在 13 节有两个落点。其一是 F11：本篇登记"接口层存在添加端点、官方自己的参数说明沿用移除口径"是文本事实，把它读成"所以可以走接口批量加人"不是本篇的结论，10.2 已写明不据此给出任何把大量号码接入群的路径说明。其二是 F24：错误码页只写"短时间内操作过多"而不给阈值，这一条不能被反读成"低于某个值就安全"——官方没写的阈值本篇不折算，也不把"没写"读成"允许"。推而广之：**官方文档里没写违规，不等于本篇认可其可行。** 准则层把对抗性行为写成一条命令句 `Do not engage in adversarial behavior.`（11.1），数据与隐私层把"试图给全部用户发消息"写成检测对象——`WhatsApp protects users by detecting unusual messaging patterns (like a business trying to message all users) and collecting spam reports from users.` 两句合起来是本系列关于群发风险最直接的文本依据：它们只说明检测与处置存在，不说明阈值、不说明顺序，更不说明怎样把行为挪到检测面之外。

## 14.3 留白清单怎么用

本篇有三套彼此独立的留白加上第四套"未取回"，含义与处置动作都不同，**不能互相替换，也都不能读成"该项不存在"**。

附表 C 的 16 行留白，含义是"这两格里有一格官方没写全"：控件仍在、方向仍成文（C 表头把这类写成"只写方向（默认值未写明）"共 15 行，另有写明默认值与可改方 18 行、无开关 19 行、以规模或身份为条件 3 行、写明但互相冲突 3 行）。处置：按控件名回出处页复查；查不出结果就停在"官方未写明"，不补成默认关闭、也不补成默认开启。

附表 E 的 44 格未按载体区分，含义是"官方文本没按这一列写"：既不代表该载体没有这一维度，也不代表本篇判定其不存在，而且它**没有出处页可回查**——这是它与 C 的留白最实际的分别。处置：只读同一维度上有字的那几格，不据空格推断任何载体的有无；逐字原句 76 格按载体分布 24／19／19／14，本身就是口径信息而不是遗漏统计。

附表 F 的 7 格留白，含义是"这一侧官方没有这句话"，且总与另一侧的逐字原句成对出现。处置：只在原表登记，**不允许用同题的其他页面替代**，也不允许把"另一侧没有"读成"另一侧被官方否定"。

4 行未取回页，含义是"本篇抓取批次没拿到正文"，既不等于官方没有这一页、也不等于官方撤下了这一页。处置：只登记标识、标题与取回状态，不引用、不复述、不推测，不用第三方转述替代（与 12.3 同一处理）。

四套留白的唯一共同点是可核对性：正文每出现一次"未写明"，读者都应能指出它落在哪张表、哪一行——C 的行号、E 的编号、F 的编号，或附表 A 的取回状态列。

# 15 结论

## 15.1 四问四答

四问是**谁能进、谁能说、谁能改、改不动时官方写了什么**；四类载体各一答，每答引一句官方原句。

**群组**：进——靠邀请链接或二维码，不足 33 人的群里成员也能发链接，开了 `Approve new members` 就须管理员批准（2.1、3.1—3.3，C 第 4、6 行）；说——由 `Send new messages` 决定，官方只写方向不写默认档（5.1、13.2）；改——由 `Edit group settings` 决定，群规模越过 256 时自动落到仅管理员一档（5.2、13.2 的 F6）；改不动时官方把话写在管理员页上，说的是平台不接管：`WhatsApp can’t interfere with group administration functions.`

**社区**：进——加入社区与被并进公告组是两件事，退出公告组等于退出社区（7.1、7.2）；说——公告组发言权只归社区管理员：`To limit noise and overload, only community admins can send announcements to all community members.` 改——社区管理员的任命与解除官方只写方向（C 第 26 行），总群与公告组各自的治理在 C 第 22—25、28—30 行；改不动时最硬的一句是"这件事没有开关"：`It’s not possible to turn off or disable replies in a community.`

**频道**：进——没人能被"加进"频道，关注是单向动作（8.1、8.2，附表 E 第 6 行频道格）；说——`Followers can’t reply directly to updates or send messages to channel admins.` 改——管理员侧的控制项写了开与关、没写默认档（C 第 32、34 行）；改不动时官方写的是形态本身不可选：`It’s not possible to create a private channel.`

**广播列表**：进——没有"进入"这个动作，只有可达前提：只有把号码存进通讯录的人才收到（9.1，C 第 41、47 行）；说——它是一对多而不是会话：`Broadcast lists are a one-to-many communication.` 改——C 第 41—46 行的写明度全是"无开关（行为固定）"或"写明默认值与可改方"，这一类载体几乎没有可拨的治理开关；改不动时官方给的是提示句与导向句（临近上限时通知、看不到上限按主文章口径），而不是报错面。

四答合起来是本篇主轴：治理面深浅不一——群组最厚（八个控件）、社区次之（权限成文、数值多处留白）、频道居中（多为只写方向）、广播列表最薄（多为无开关）。官方写得最少的载体在冲突表里出现得也最少，这是文本分布的结果，不是本篇的取舍。

## 15.2 与系列另四篇的接缝

本节只讲哪些结论必须并读才不会读错，不转述另四篇的任何数字。

**与篇①《违规判定与处置阶梯》**：处置阶梯与载体规则分属两套文本。本篇的开关矩阵不预测任何处置结果，也不能被反读成"这样设置就不会被处置"；反过来，篇①的判定档位不能用来解释本篇某处为什么没有开关。**与篇②《消息类型、模板、质量评级与限额档位》**：最关键一条是**四类载体的上限与消息层的限额是两根不同的轴**——前者管一个容器能装多少人，后者管一条消息能不能发、按什么档位发；把本篇 B 表的规模数字与篇②的档位数字相加、相除或互相换算，都会造出官方没写过的位置，本篇所有"越过某人数即如何"的句子只指向治理开关，不指向任何发送配额。**与篇③《自动化接入面：Cloud API、Webhook、系统用户与官方对机器人行为的定性》**：**群组的治理开关与 Groups API 字段不是一根轴**——界面开关与接口字段各有一套默认值与取值，13.1 的 F1、F2 是这两根轴在顶置上的两次落笔，F11、F12 是它们在加人上的两次落笔；篇③对机器人行为的定性不改变本篇按页面登记的行为口径。**与篇⑤（尚未写）号码与账号形态、受限账号与封禁申诉**：本篇只登记官方写在载体页上的号码形态条件（C 第 55、56 行、10.4），不讨论账号形态；13.4 的灰度句只登记到"官方自陈范围未公开"这一层；申诉路径整体归篇⑤，8.5 与 11.2 引到的频道复核句只用来说明该载体的"改不动面"写在哪儿。

并读原则收尾：在任何一页读到一个上限，先问它挂在哪个容器上、再问官方有没有第二页写法——前一问决定它属于本篇哪一节，后一问决定它进不进附表 F；跨篇引用数字以各篇自己的附表为准，换算不在这五篇的正文里进行。

## 15.3 一句话读法

**官方写了什么就是什么，官方没写的就是没写**：四类载体里每一条开关，都能在附表 A 找到页面、在附表 C 找到控件名、在附表 B 找到数值与单位、在附表 E 找到它与其他载体的并排、在附表 F 找到它与另一页相撞或与一片留白相邻的那一行。反过来说也一样成立：本篇能给读者的只有"核对官方口径"，不能给的是"据此设计投递"——官方文本没写到的动作，既不在这份材料的授权范围内，也不在本篇的可信范围内。

## 15.4 复核清单

每条写明要复核哪一个数或哪一条说法、去附表哪一行或正文哪一节。

1. 群成员上限的三种写法与"含创建者"那一档：B 第 1—3 行、F 第 F10 项，正文 2.1、13.1。
2. 不带千分位的 1024 与 2000 不是第二个上限：B 第 2、16 行与 12.1 的分行规则，不得当作新矛盾。
3. 社区 2,000 的两种枚举成分：F 第 F8 项，正文 7.1、13.1。
4. 1,024 是人数上限还是互动门槛：B 第 19、20 行、F 第 F9 项，正文 7.5、13.1。
5. 管理员数量的两条出处：B 第 22、23 行、F 第 F13 项，正文 4.1、4.4、13.1；社区管理员数量按 F20 留白，不外推。
6. 顶置的界面 4 条、接口 3 条与一次一条：B 第 12、47、48 行、F 第 F1、F2 项，正文 5.3、10.5、13.1。
7. 参与者数组的 8 名与那一格整格照抄：B 第 44 行、F 第 F11 项，正文 10.2、12.1、13.1；复核点是添加端点的说明沿用移除口径，不是数值本身。
8. 列表数量不设上限与每月 35 条分属两个对象（B 第 36 行是每列表联系人数、第 54 行是每月条数）：F 第 F14、F16 项，正文 2.4、9.1、13.1、13.2。
9. 33 人这条线的两种基准：F 第 F5 项、C 第 4 行，正文 13.3；并核对 @all 的 32／33 分界（B 第 6 行、C 第 9 行）没被混进来。
10. 历史消息可见性与 100 条／14 天：C 第 5、20 行、F 第 F3、F4 项，正文 3.6、11.4、13.3。
11. 消失消息的默认档与三档时长：B 第 61 行、C 第 10 行、F 第 F17、F18 项，正文 5.4、13.3；复核点是"24 小时不是默认值"。
12. 冷却曲线的起点与封顶：B 第 73—76 四行、F 第 F19 项，正文 3.5、13.3；两个载体各两行，不得并成一行。
13. 公告组成员总数的可见性冲突：C 第 29 行、F 第 F7 项，正文 7.4、13.2；两侧都是写明的一格，处置是并陈不裁决。
14. 频道订阅与频道功能的灰度句、准则层与帮助层的强度差：F 第 F15、F23、F26 项，正文 8.5、8.7、11.2、13.4；三处都必须停在条件句上。
15. 四个不可引用页有没有被正文引用过：附表 A 取回状态列、附表 F 末尾 4 行，正文 12.3、13.4；判据是全篇对这 4 页只登记事实、不出现任何引文。
16. 三套留白有没有被互相顶替：附表 C、E、F 的表头与正文 14.3；C 可回页复查、E 无页可回、F 不得用同题页面替代。
17. 本篇自身的全部统计数：六张附表的表头行与合计行，以及正文 12.1—12.3；登记位 126、可引用 122、不可引用 4、核验 402 条失败 0 条只从表里取，不在正文临场点数。

# 附表 A 官方页面登记清单与本篇引用位置（127 行）

登记口径与系列前三篇一致：**凡本篇抓取的页面，一律登记**。全表 **126** 个登记位，按站点分为 Meta 开发者文档（Groups API reference） 5、Meta 开发者文档（Groups API） 8、Meta 开发者文档（Webhook reference） 1、Meta 开发者文档（opt-in 规则） 1、Meta 开发者文档（商业资料） 1、Meta 开发者文档（政策执法） 1、Meta 开发者文档（数据与隐私） 1、WhatsApp 帮助中心 104、法律与政策页 4。取回状态由脚本实测帮助中心裁剪后正文的非空白字符数得出（阈值 400），分四类：正文仅显示地域限定提示 1、正文已取回 121、正文已取回·短文 1、正文未取回 3。其中**可引用 122 页、不可引用 4 页**：不可引用的页面是正文只返回单页应用外壳、或正文只有一句「该内容由特定地区提供」的地域限定提示，**其内容全篇未被引用**，本篇只在附表 F 里登记「该页正文未取回／只有地域限定提示」这一条事实。

帮助中心页的正文取回长度按裁剪后正文计：抓取原始页含平台页签（Web／Windows／Mac／iOS／Android）与固定尾栏，这些行不计入长度，也不作为引文出处。

| 站点 | 页面标识 | 标题 | 本篇引用位置 | 正文取回状态 |
|---|---|---|---|---|
| WhatsApp 帮助中心 | `faq_1003616827680090` | How to keep your groups and community safe | 正文 7.6、11.3 | 已取回（正文 3435 字符） |
| WhatsApp 帮助中心 | `faq_1010627070036599` | Can’t join group with invite link | 正文 3.2 | 已取回·短文（正文 204 字符，人工核对为完整答复） |
| WhatsApp 帮助中心 | `faq_1023031902773792` | How to pin channels and star updates | 正文 8.4、附表 B（1 处） | 已取回（正文 929 字符） |
| WhatsApp 帮助中心 | `faq_1053543185312573` | About forwarding limits | 正文 11.4 | 已取回（正文 1639 字符） |
| WhatsApp 帮助中心 | `faq_1062135416113130` | About sending message history in groups | 正文 3.6、13.3、附表 C（1 处）、附表 F（1 处） | 已取回（正文 1181 字符） |
| WhatsApp 帮助中心 | `faq_1062663711575428` | About communities on the WhatsApp Business app | 正文 7.1、8.7 | 已取回（正文 523 字符） |
| WhatsApp 帮助中心 | `faq_1110600769849613` | About group join requests | 正文 3.3 | 已取回（正文 1486 字符） |
| WhatsApp 帮助中心 | `faq_1139252413769848` | How to join a group | 正文 3.2、3.6、13.3、附表 B（1 处）、附表 F（2 处） | 已取回（正文 1636 字符） |
| WhatsApp 帮助中心 | `faq_1142317320080231` | How to invite and dismiss channel admins | 正文 4.4、附表 B（2 处）、附表 C（1 处） | 已取回（正文 2447 字符） |
| WhatsApp 帮助中心 | `faq_1142481766359885` | How to block and report someone | 正文 11.4 | 已取回（正文 6556 字符） |
| WhatsApp 帮助中心 | `faq_1170535238421230` | Privacy at a Glance: Additional Information about using Status and Channels | 正文 6.4、11.5 | 已取回（正文 9810 字符） |
| WhatsApp 帮助中心 | `faq_1176325363861991` | About broadcast limits | 正文 2.4、9.1、9.3、13.1、附表 B（1 处）、附表 C（1 处）、附表 F（2 处） | 已取回（正文 785 字符） |
| WhatsApp 帮助中心 | `faq_1237143835184017` | How to send message history in a group | 正文 3.6、附表 C（1 处） | 已取回（正文 1801 字符） |
| WhatsApp 帮助中心 | `faq_1251246649902787` | About becoming a promoted channel in the directory | 正文 8.5、13.4、附表 F（1 处） | 已取回（正文 582 字符） |
| WhatsApp 帮助中心 | `faq_1310128517201866` | About group status | 正文 5.2、附表 B（1 处） | 已取回（正文 1927 字符） |
| WhatsApp 帮助中心 | `faq_1312647189536807` | How to exit a community | 正文 3.5、7.2、附表 F（1 处） | 已取回（正文 1723 字符） |
| WhatsApp 帮助中心 | `faq_1318001139066835` | About safety and privacy on channels | 正文 8.1、11.3 | 已取回（正文 1941 字符） |
| WhatsApp 帮助中心 | `faq_1339924517208346` | About voice update transcripts on Whatsapp Channels | 正文 8.4、附表 C（1 处） | 已取回（正文 1648 字符） |
| WhatsApp 帮助中心 | `faq_1351864989275592` | About channel subscriptions | 全篇不引用（正文未取回或只有地域限定提示） | 正文未取回（0 字符） |
| WhatsApp 帮助中心 | `faq_1371180700817474` | About adding subscriptions to your channel | 正文 8.5、13.4、附表 F（1 处） | 已取回（正文 458 字符） |
| WhatsApp 帮助中心 | `faq_1418342145436771` | About general chat for communities | 正文 2.2、7.5、13.1、附表 B（1 处）、附表 C（2 处）、附表 F（1 处） | 已取回（正文 1895 字符） |
| WhatsApp 帮助中心 | `faq_1426887324388733` | How to archive or unarchive a chat or group | 正文 5.5 | 已取回（正文 1363 字符） |
| WhatsApp 帮助中心 | `faq_1446688872845683` | How to see channel metrics and insights | 正文 8.6、附表 B（4 处） | 已取回（正文 3404 字符） |
| WhatsApp 帮助中心 | `faq_1500274774253689` | How to turn broadcast lists into marketing messages | 正文 9.3 | 已取回（正文 883 字符） |
| WhatsApp 帮助中心 | `faq_1512467186677937` | How to label paid partnerships on channels | 正文 8.4 | 已取回（正文 941 字符） |
| WhatsApp 帮助中心 | `faq_1524953031793983` | About business broadcasts | 正文 9.3 | 已取回（正文 1313 字符） |
| WhatsApp 帮助中心 | `faq_1533984453785262` | About WhatsApp permissions | 正文 11.5 | 已取回（正文 448 字符） |
| WhatsApp 帮助中心 | `faq_1549633222110625` | How to edit channel updates | 正文 8.4 | 已取回（正文 563 字符） |
| WhatsApp 帮助中心 | `faq_1859295147860069` | How to reply and react to announcements | 正文 6.1、7.4、13.1、附表 B（1 处）、附表 C（2 处）、附表 F（1 处） | 已取回（正文 1352 字符） |
| WhatsApp 帮助中心 | `faq_2127363544304650` | How to create and customize events in community groups | 正文 6.2、附表 B（1 处） | 已取回（正文 2042 字符） |
| WhatsApp 帮助中心 | `faq_214063281402212` | How to manage channel info and settings | 正文 8.3、附表 B（1 处） | 已取回（正文 776 字符） |
| WhatsApp 帮助中心 | `faq_223836640775084` | How to share channel polls and quizzes | 正文 6.1、8.4、附表 B（5 处） | 已取回（正文 1700 字符） |
| WhatsApp 帮助中心 | `faq_245599461477281` | WhatsApp Channels Guidelines | 全篇不引用（正文未取回或只有地域限定提示） | 正文未取回（95 字符） |
| WhatsApp 帮助中心 | `faq_2490388581320854` | How to share your channel | 正文 8.4、附表 C（1 处） | 已取回（正文 1126 字符） |
| WhatsApp 帮助中心 | `faq_251090924134584` | How to delete channel updates | 正文 8.4 | 已取回（正文 942 字符） |
| WhatsApp 帮助中心 | `faq_2613314448830863` | How to create a verified channel on the WhatsApp Business app | 正文 8.7、附表 B（1 处） | 已取回（正文 2926 字符） |
| WhatsApp 帮助中心 | `faq_2635108359972899` | How to protect your IP address on WhatsApp calls | 正文 6.3 | 已取回（正文 556 字符） |
| WhatsApp 帮助中心 | `faq_263784176043634` | About interactions with business chats that include offers and announcements | 正文 9.4 | 已取回（正文 1995 字符） |
| WhatsApp 帮助中心 | `faq_265055289421317` | About creating a WhatsApp Channel | 正文 2.3、8.1、13.2 | 已取回（正文 1970 字符） |
| WhatsApp 帮助中心 | `faq_290544379966533` | How to share a channel update | 正文 8.4、附表 B（1 处） | 已取回（正文 3990 字符） |
| WhatsApp 帮助中心 | `faq_294619079641794` | How to pin a message | 正文 5.3、13.1、附表 B（2 处）、附表 C（1 处）、附表 D（3 处）、附表 F（1 处） | 已取回（正文 1115 字符） |
| WhatsApp 帮助中心 | `faq_305393838527652` | How to forward from channels to chat or status | 正文 8.4、11.4、附表 B（1 处） | 已取回（正文 2158 字符） |
| WhatsApp 帮助中心 | `faq_3242937609289432` | How to create and invite into a group | 正文 2.1、3.1、13.3、附表 B（2 处）、附表 D（3 处）、附表 F（1 处） | 已取回（正文 3473 字符） |
| WhatsApp 帮助中心 | `faq_3307102709559968` | How to change your privacy settings | 正文 5.4、11.5、13.3、附表 F（1 处） | 已取回（正文 3939 字符） |
| WhatsApp 帮助中心 | `faq_3313983622238973` | How to create and edit events in groups | 正文 6.2、附表 B（1 处） | 已取回（正文 2171 字符） |
| WhatsApp 帮助中心 | `faq_337473702666585` | About ads in WhatsApp Status and Channels | 正文 8.4 | 已取回（正文 3523 字符） |
| WhatsApp 帮助中心 | `faq_3440358352849077` | How to report a channel | 正文 11.2、附表 B（1 处） | 已取回（正文 1573 字符） |
| WhatsApp 帮助中心 | `faq_360977646301595` | About channel admin controls | 正文 8.3、附表 B（1 处）、附表 C（4 处） | 已取回（正文 2272 字符） |
| WhatsApp 帮助中心 | `faq_361005896189245` | How to use WhatsApp responsibly | 正文 1.4、11.5 | 已取回（正文 2710 字符） |
| WhatsApp 帮助中心 | `faq_3666520913590793` | How to find and join events in a community group | 正文 6.2 | 已取回（正文 636 字符） |
| WhatsApp 帮助中心 | `faq_3862936950594201` | Can&#039;t change or assign a group admin | 正文 4.2 | 已取回（正文 446 字符） |
| WhatsApp 帮助中心 | `faq_394993313189648` | How to request a review for a closed channel | 正文 8.5、11.2、附表 B（1 处）、附表 C（1 处） | 已取回（正文 1886 字符） |
| WhatsApp 帮助中心 | `faq_3966323053691518` | How to use voice chat in a group | 正文 6.3、附表 B（3 处）、附表 C（2 处） | 已取回（正文 1254 字符） |
| WhatsApp 帮助中心 | `faq_414631957536067` | About reporting and blocking on WhatsApp | 正文 11.4 | 已取回（正文 4965 字符） |
| WhatsApp 帮助中心 | `faq_420332980275753` | How to rejoin a group chat | 正文 3.5、13.3、附表 B（2 处）、附表 F（1 处） | 已取回（正文 1224 字符） |
| WhatsApp 帮助中心 | `faq_424124173736394` | How to stay safe in groups on WhatsApp | 正文 5.1、11.3、附表 C（2 处） | 已取回（正文 3833 字符） |
| WhatsApp 帮助中心 | `faq_438859978317289` | How to create a community | 正文 2.2、7.1、13.1、附表 B（3 处）、附表 F（1 处） | 已取回（正文 1192 字符） |
| WhatsApp 帮助中心 | `faq_441425088132574` | How to manage community admins | 正文 4.3、附表 B（1 处）、附表 C（1 处） | 已取回（正文 644 字符） |
| WhatsApp 帮助中心 | `faq_450394724284262` | How to archive a group in a community | 正文 5.5、7.3 | 已取回（正文 514 字符） |
| WhatsApp 帮助中心 | `faq_453914586839706` | How to send media, contacts, or location | 正文 6.4、附表 B（6 处） | 已取回（正文 2589 字符） |
| WhatsApp 帮助中心 | `faq_458610306367976` | About building private, safe, and secure communities on WhatsApp | 正文 7.6、附表 F（1 处） | 已取回（正文 3718 字符） |
| WhatsApp 帮助中心 | `faq_493270532852273` | How to make changes to groups | 正文 5.2、13.2、附表 F（1 处） | 已取回（正文 1739 字符） |
| WhatsApp 帮助中心 | `faq_495856382464992` | About communities | 正文 1.3、7.1 | 已取回（正文 812 字符） |
| WhatsApp 帮助中心 | `faq_498814665492149` | How to exit and delete groups as an admin | 正文 3.5、附表 B（1 处） | 已取回（正文 2479 字符） |
| WhatsApp 帮助中心 | `faq_515705483748188` | How to turn disappearing messages on or off in a group | 正文 5.4、13.3、附表 B（1 处）、附表 C（1 处）、附表 F（2 处） | 已取回（正文 1506 字符） |
| WhatsApp 帮助中心 | `faq_521835819370205` | How to mention in a group | 正文 5.3、附表 B（1 处）、附表 C（1 处） | 已取回（正文 1282 字符） |
| WhatsApp 帮助中心 | `faq_526742385997912` | How to change group admin settings | 正文 2.1、5.1、5.2、13.2、附表 B（2 处）、附表 C（6 处）、附表 F（2 处） | 已取回（正文 2583 字符） |
| WhatsApp 帮助中心 | `faq_549900560675125` | About WhatsApp Channels | 正文 1.3、8.1、附表 C（1 处） | 已取回（正文 1530 字符） |
| WhatsApp 帮助中心 | `faq_5590979194321336` | How to turn disappearing messages on or off | 正文 5.4、附表 D（4 处） | 已取回（正文 1685 字符） |
| WhatsApp 帮助中心 | `faq_582420703681043` | About community announcements | 正文 2.2、7.4、13.1、13.2、附表 F（2 处） | 已取回（正文 661 字符） |
| WhatsApp 帮助中心 | `faq_598838008878228` | How to delete a channel | 正文 8.3 | 已取回（正文 1152 字符） |
| WhatsApp 帮助中心 | `faq_6074154625973804` | How to manage channel notifications | 正文 8.6 | 已取回（正文 492 字符） |
| WhatsApp 帮助中心 | `faq_627992286976376` | How to invite channel followers | 正文 8.2、附表 B（1 处）、附表 C（1 处） | 已取回（正文 705 字符） |
| WhatsApp 帮助中心 | `faq_630432792316720` | How to find and follow WhatsApp Channels | 正文 8.2 | 已取回（正文 3598 字符） |
| WhatsApp 帮助中心 | `faq_633713745095781` | How to manage group admins | 正文 4.1、13.1、附表 C（2 处）、附表 F（1 处） | 已取回（正文 1155 字符） |
| WhatsApp 帮助中心 | `faq_6480469855345352` | How to request channels report | 正文 11.2、附表 C（1 处） | 已取回（正文 2837 字符） |
| WhatsApp 帮助中心 | `faq_651003086597307` | How to rejoin a community | 正文 3.5、13.3、附表 B（2 处）、附表 F（1 处） | 已取回（正文 924 字符） |
| WhatsApp 帮助中心 | `faq_653415899610349` | How to use broadcast lists on the WhatsApp Business app | 正文 9.2、附表 C（2 处） | 已取回（正文 1731 字符） |
| WhatsApp 帮助中心 | `faq_671443411431514` | About safety and privacy on channels for admins | 正文 11.3、13.4、附表 F（1 处） | 已取回（正文 1686 字符） |
| WhatsApp 帮助中心 | `faq_673193694148537` | About disappearing messages | 正文 5.4、13.3、附表 B（1 处）、附表 F（1 处） | 已取回（正文 2562 字符） |
| WhatsApp 帮助中心 | `faq_674413780678918` | What does &quot;This community is no longer available&quot; mean? | 正文 7.6 | 已取回（正文 466 字符） |
| WhatsApp 帮助中心 | `faq_678712076864311` | How to exit a group as a member | 正文 3.5、附表 B（1 处）、附表 C（1 处） | 已取回（正文 2438 字符） |
| WhatsApp 帮助中心 | `faq_694350718331007` | How to mute or unmute individual or group chat notifications | 正文 5.5、附表 B（1 处）、附表 C（1 处）、附表 D（3 处） | 已取回（正文 1810 字符） |
| WhatsApp 帮助中心 | `faq_699347952293912` | How to forward to your channel | 正文 8.4、11.4、附表 B（1 处） | 已取回（正文 621 字符） |
| WhatsApp 帮助中心 | `faq_715385484388016` | About restricted chat | 正文 5.5、附表 C（1 处） | 已取回（正文 2806 字符） |
| WhatsApp 帮助中心 | `faq_7179561392143247` | How to see group members | 正文 2.1、13.1、附表 B（3 处）、附表 F（1 处） | 已取回（正文 678 字符） |
| WhatsApp 帮助中心 | `faq_742237357891080` | About community announcements for admins | 正文 7.4、13.2、附表 B（1 处）、附表 C（2 处）、附表 F（1 处） | 已取回（正文 958 字符） |
| WhatsApp 帮助中心 | `faq_743219147158705` | About call history on WhatsApp | 正文 6.3 | 已取回（正文 979 字符） |
| WhatsApp 帮助中心 | `faq_794229125227200` | How to create a WhatsApp Channel | 正文 4.4、8.1、13.1、附表 B（1 处）、附表 F（1 处） | 已取回（正文 1267 字符） |
| WhatsApp 帮助中心 | `faq_797069521522888` | How to manage your notifications | 正文 5.5、8.6、附表 B（1 处） | 已取回（正文 6977 字符） |
| WhatsApp 帮助中心 | `faq_809740894165112` | About channels on the WhatsApp Business app | 正文 8.7、附表 F（1 处） | 已取回（正文 2501 字符） |
| WhatsApp 帮助中心 | `faq_813798063285846` | How to add or suggest a group to a community | 全篇不引用（正文未取回或只有地域限定提示） | 正文未取回（113 字符） |
| WhatsApp 帮助中心 | `faq_824049403559473` | How to share music updates to your channel | 正文 8.4 | 已取回（正文 1832 字符） |
| WhatsApp 帮助中心 | `faq_829612741557179` | How to make a group voice call | 正文 6.3、附表 B（2 处） | 已取回（正文 4211 字符） |
| WhatsApp 帮助中心 | `faq_841324506880320` | How to block a group admin | 正文 4.2、11.4 | 已取回（正文 604 字符） |
| WhatsApp 帮助中心 | `faq_841426356990637` | How to add and remove group members | 正文 2.1、3.4、13.3、附表 B（1 处）、附表 C（1 处）、附表 F（1 处） | 已取回（正文 2907 字符） |
| WhatsApp 帮助中心 | `faq_846698564598022` | How to use Strict account settings | 正文 11.5 | 已取回（正文 1659 字符） |
| WhatsApp 帮助中心 | `faq_861663048350950` | How to use broadcast lists | 正文 1.3、9.1、13.1、附表 B（1 处）、附表 C（5 处）、附表 F（1 处） | 已取回（正文 1705 字符） |
| WhatsApp 帮助中心 | `faq_894737126459640` | What is defamation and how do I report it on Channels? | 全篇不引用（正文未取回或只有地域限定提示） | 正文仅显示地域限定提示（70 字符） |
| WhatsApp 帮助中心 | `faq_902091421605313` | How to approve new members as a group admin | 正文 3.3 | 已取回（正文 942 字符） |
| WhatsApp 帮助中心 | `faq_958928238510970` | Building Private, Safe, and Secure WhatsApp Channels | 正文 8.1、11.3 | 已取回（正文 2553 字符） |
| WhatsApp 帮助中心 | `faq_962978635456336` | About how WhatsApp recommends channels | 正文 8.5 | 已取回（正文 1155 字符） |
| WhatsApp 帮助中心 | `faq_967457667545238` | How to join a group in a community | 正文 2.1、7.2、13.1、附表 B（1 处）、附表 C（1 处）、附表 F（1 处） | 已取回（正文 702 字符） |
| WhatsApp 帮助中心 | `faq_970281987938470` | How to remove a group from a community | 正文 7.3、附表 C（1 处）、附表 F（1 处） | 已取回（正文 1017 字符） |
| Meta 开发者文档（Groups API） | `whatsapp_groups_md` | Groups API | 正文 1.3、10.1、附表 B（3 处）、附表 C（5 处）、附表 D（13 处）、附表 F（1 处） | 已取回（正文 4439 字符） |
| Meta 开发者文档（Groups API） | `whatsapp_groups_get_started_md` | Get started with Groups API | 正文 1.1、10.1 | 已取回（正文 4205 字符） |
| Meta 开发者文档（Groups API） | `whatsapp_groups_groups_messaging_md` | Group messaging | 正文 10.1、10.5、13.1、附表 B（3 处）、附表 C（1 处）、附表 F（3 处） | 已取回（正文 11821 字符） |
| Meta 开发者文档（Groups API） | `whatsapp_groups_reference_md` | Group management | 正文 10.1、10.3、13.2、附表 B（6 处）、附表 C（2 处）、附表 D（4 处）、附表 F（2 处） | 已取回（正文 28919 字符） |
| Meta 开发者文档（Groups API） | `whatsapp_groups_webhooks_md` | Webhooks for Groups API | 正文 10.5、附表 B（2 处）、附表 D（29 处） | 已取回（正文 31793 字符） |
| Meta 开发者文档（Groups API） | `whatsapp_groups_error_codes_md` | Groups API Error Codes and Troubleshooting | 正文 10.6、12.1、附表 B（3 处）、附表 C（1 处）、附表 D（19 处）、附表 F（1 处） | 已取回（正文 2543 字符） |
| Meta 开发者文档（Groups API） | `whatsapp_groups_faq_md` | Groups API FAQ | 正文 10.1、10.4 | 已取回（正文 1133 字符） |
| Meta 开发者文档（Groups API） | `whatsapp_groups_pricing_md` | Groups API Pricing | 正文 10.4、附表 B（6 处）、附表 D（7 处） | 已取回（正文 12570 字符） |
| Meta 开发者文档（Groups API reference） | `whatsapp_reference_groups_groups_invite_link_api_md` | Groups Invite Link API | 正文 10.3、附表 C（1 处）、附表 D（2 处） | 已取回（正文 3214 字符） |
| Meta 开发者文档（Groups API reference） | `whatsapp_reference_groups_groups_join_requests_api_md` | Groups Join Requests API | 正文 10.3、附表 D（3 处） | 已取回（正文 6089 字符） |
| Meta 开发者文档（Groups API reference） | `whatsapp_reference_groups_groups_participants_api_md` | Groups Participants API | 正文 2.5、10.2、13.1、附表 D（2 处）、附表 F（1 处） | 已取回（正文 3343 字符） |
| Meta 开发者文档（Groups API reference） | `whatsapp_reference_groups_groups_query_api_md` | Groups Query API | 正文 10.4、附表 D（3 处） | 已取回（正文 5017 字符） |
| Meta 开发者文档（Groups API reference） | `whatsapp_reference_whatsapp_business_phone_number_groups_management_api_md` | Groups Management API | 正文 10.1、10.4、附表 B（1 处）、附表 D（2 处） | 已取回（正文 5623 字符） |
| Meta 开发者文档（Webhook reference） | `whatsapp_webhooks_reference_messages_group_md` | Group messages webhook reference | 正文 10.5、附表 B（1 处） | 已取回（正文 7343 字符） |
| Meta 开发者文档（数据与隐私） | `whatsapp_data_privacy_and_security_md` | Data Privacy & Security | 正文 11.3、附表 B（2 处） | 已取回（正文 6104 字符） |
| Meta 开发者文档（政策执法） | `whatsapp_policy_enforcement_violations_md` | WhatsApp Business Platform Policy Violations | 正文 11.1 | 已取回（正文 7278 字符） |
| Meta 开发者文档（opt-in 规则） | `whatsapp_getting_opt_in_md` | Get opt-in for WhatsApp | 正文 9.4、附表 B（1 处） | 已取回（正文 2826 字符） |
| Meta 开发者文档（商业资料） | `whatsapp_business_profiles_md` | Business profiles - WhatsApp | 正文 9.4 | 已取回（正文 3689 字符） |
| 法律与政策页 | `legal_channels_guidelines` | WhatsApp Channels Guidelines | 正文 1.3、11.2、附表 F（1 处） | 已取回（正文 6208 字符） |
| 法律与政策页 | `legal_channels_terms` | WhatsApp Channels Terms of Service | 正文 1.3、11.2 | 已取回（正文 4761 字符） |
| 法律与政策页 | `legal_messaging_guidelines` | WhatsApp Business Messaging Guidelines | 正文 1.2、11.1、附表 F（1 处） | 已取回（正文 5012 字符） |
| 法律与政策页 | `legal_tos` | WhatsApp Terms of Service | 正文 1.2、11.1 | 已取回（正文 28433 字符） |
| **合计** | **126** 个登记位 | 可引用 **122** 页／不可引用 **4** 页 | Meta 开发者文档（Groups API reference） 5／Meta 开发者文档（Groups API） 8／Meta 开发者文档（Webhook reference） 1／Meta 开发者文档（opt-in 规则） 1／Meta 开发者文档（商业资料） 1／Meta 开发者文档（政策执法） 1／Meta 开发者文档（数据与隐私） 1／WhatsApp 帮助中心 104／法律与政策页 4 | 不可引用页全篇不引用 |

#ROWS=126 CITED=122 UNCITED=4

# 附表 B 数字归档五档（100 行）

归档规则见正文口径节：**同一数字出现在两个体系里开两行**（例如 1,024 既是成员上限、又是公告组互动失效门槛），官方写成区间或多档的按整段一行登记（例如 24 小时／7 天／90 天），版本与日期行只进第四档，官方只给示例值的登记为「示例值」、不当作规则，本篇自己数出来的组数与个数登记为「本篇统计」。五档合计 **99** 行（一 **53**／二 **7**／三 **26**／四 **4**／五 **9**），去重后的数字与档位词条目 **57** 个，计数单位 **41** 种，性质 **43** 种。**同一数字在不同页上写法不一致的行一律分别登记**，差异本身在附表 F 逐条对撞。

| 档 | 数字 | 计数单位 | 数的是什么 | 性质 | 出处页 |
|---|---|---|---|---|---|
| 一、限额与规模档位 | **1,024** | 人 | 单个群组的成员上限（帮助中心三种写法之一，带千分位） | 硬数量上限 | faq_7179561392143247 |
| 一、限额与规模档位 | **1024** | 人 | 同一上限的第二种写法（不带千分位，出现在建群页与治理页） | 硬数量上限 | faq_3242937609289432 |
| 一、限额与规模档位 | **1,025** | 人 | 把创建者一并计入时的同一上限表述 | 硬数量上限（口径含创建者） | faq_967457667545238 |
| 一、限额与规模档位 | **100** | 个字符 | 群组名称的长度上限 | 硬数量上限 | faq_3242937609289432 |
| 一、限额与规模档位 | **10** | 人 | 成员列表默认折叠显示、超出后须点「查看全部」的分界人数 | 显示分界 | faq_7179561392143247 |
| 一、限额与规模档位 | **32 人及以下／33 人及以上** | 人 | @all 提及权限的资格分界：小群全员可用，大群仅管理员可用 | 权限分界 | faq_521835819370205 |
| 一、限额与规模档位 | **33** | 人 | 「成员可否自发邀请链接／可否自发加人」的默认资格分界 | 默认资格分界 | faq_526742385997912 |
| 一、限额与规模档位 | **256** | 人 | 超过该人数后「编辑群信息」自动落到仅管理员一档 | 条件触发降权门槛 | faq_526742385997912 |
| 一、限额与规模档位 | **3 - 32** | 人 | 语音聊天走「上滑按住」手势入口的群规模区间 | 入口分界 | faq_3966323053691518 |
| 一、限额与规模档位 | **33 人及以上** | 人 | 语音聊天改走菜单入口的群规模分界 | 入口分界 | faq_3966323053691518 |
| 一、限额与规模档位 | **32** | 人 | 群组语音通话可同时加入的人数上限 | 硬数量上限 | faq_829612741557179 |
| 一、限额与规模档位 | **4** | 条 | 消费端可置顶消息条数上限 | 硬数量上限 | faq_294619079641794 |
| 一、限额与规模档位 | **3** | 次 | 同一通通话里管理员可静音某成员的次数上限 | 硬次数上限 | faq_829612741557179 |
| 一、限额与规模档位 | **5** | 条 | 举报时可随附的最近消息条数 | 硬数量上限 | faq_678712076864311 |
| 一、限额与规模档位 | **2,000** | 人 | 社区及其公告组的成员上限（带千分位写法） | 硬数量上限 | faq_438859978317289 |
| 一、限额与规模档位 | **2000** | 人 | 同一上限的不带千分位写法 | 硬数量上限 | faq_742237357891080 |
| 一、限额与规模档位 | **100** | 个群组 | 单个社区可容纳的群组数上限 | 硬数量上限 | faq_438859978317289 |
| 一、限额与规模档位 | **20** | 名 | 社区管理员人数上限 | 硬数量上限 | faq_441425088132574 |
| 一、限额与规模档位 | **1,024** | 人 | 社区总群（general chat）沿用最普通群组的成员上限 | 硬数量上限 | faq_1418342145436771 |
| 一、限额与规模档位 | **1,024** | 人 | 公告组表情回应与投票可参与的成员规模上限（超过即全体失效） | 功能失效门槛 | faq_1859295147860069 |
| 一、限额与规模档位 | **100** | 个字符 | 社区名称长度上限 | 硬数量上限 | faq_438859978317289 |
| 一、限额与规模档位 | **16** | 名 | 频道所有者一次可邀请的管理员数 | 硬数量上限 | faq_1142317320080231 |
| 一、限额与规模档位 | **16** | 名 | 频道可额外增设的管理员总数上限（另一页写法） | 硬数量上限 | faq_794229125227200 |
| 一、限额与规模档位 | **100** | 个字符 | 频道名称长度上限 | 硬数量上限 | faq_214063281402212 |
| 一、限额与规模档位 | **64** | 个联系人 | 邀请关注者时可一次勾选的联系人上限 | 硬数量上限 | faq_627992286976376 |
| 一、限额与规模档位 | **10** | 个频道 | 单个订阅可认证的频道数 | 硬数量上限（随订阅方案变动） | faq_2613314448830863 |
| 一、限额与规模档位 | **2** | 个频道 | 可将频道固定到列表顶部的位置数 | 硬数量上限 | faq_1023031902773792 |
| 一、限额与规模档位 | **12** | 个选项 | 频道投票可设的选项数上限 | 硬数量上限 | faq_223836640775084 |
| 一、限额与规模档位 | **12** | 个问题 | 同一投票里可追加的问题数上限 | 硬数量上限 | faq_223836640775084 |
| 一、限额与规模档位 | **255** | 个字符 | 投票问题文本长度上限 | 硬数量上限 | faq_223836640775084 |
| 一、限额与规模档位 | **100** | 个字符 | 投票选项文本长度上限 | 硬数量上限 | faq_223836640775084 |
| 一、限额与规模档位 | **100** | 个关注者 | 关注者少于该数时频道数据看板不可用 | 功能可用门槛 | faq_1446688872845683 |
| 一、限额与规模档位 | **100** | 个本地账号 | 某地区要作为「热门地区」出现所需的触达账号数 | 统计显示门槛 | faq_1446688872845683 |
| 一、限额与规模档位 | **10** | 个地区 | 数据看板最多展示的地区数 | 硬数量上限 | faq_1446688872845683 |
| 一、限额与规模档位 | **60** | 秒 | 频道视频笔记的时长上限 | 硬数量上限 | faq_290544379966533 |
| 一、限额与规模档位 | **256** | 个联系人 | 单个广播列表可容纳的联系人上限 | 硬数量上限 | faq_861663048350950 |
| 一、限额与规模档位 | **5** | 个会话 | 一条消息一次可转发的会话数上限 | 硬数量上限 | faq_305393838527652 |
| 一、限额与规模档位 | **2 GB** | 字节 | 文档附件的大小上限 | 硬数量上限 | faq_453914586839706 |
| 一、限额与规模档位 | **100** | 张 | 一次可选取的图片数上限 | 硬数量上限 | faq_453914586839706 |
| 一、限额与规模档位 | **192** | 像素 | 头像建议的最小高宽，低于此易糊 | 建议下限 | faq_453914586839706 |
| 一、限额与规模档位 | **100MB／720p** | 字节与分辨率 | 较快网络下的默认视频上限 | 默认值（随网络条件分档） | faq_453914586839706 |
| 一、限额与规模档位 | **64MB／480p** | 字节与分辨率 | 较慢网络下的默认视频上限 | 默认值（随网络条件分档） | faq_453914586839706 |
| 一、限额与规模档位 | **90** | 秒 | 状态视频的最大时长 | 硬数量上限 | faq_453914586839706 |
| 一、限额与规模档位 | **8** | 名参与者 | Groups API 单次请求参与者数组的长度上限 | 硬数量上限 | whatsapp_groups_md |
| 一、限额与规模档位 | **10,000** | 个群组 | 单个业务号码可创建的群组数上限 | 硬数量上限 | whatsapp_groups_md |
| 一、限额与规模档位 | **1** | 个业务资产组合 | 同一群组允许接入的 Cloud API 业务数 | 硬数量上限 | whatsapp_groups_md |
| 一、限额与规模档位 | **3** | 条 | Groups API 侧同一群组同时存在的置顶消息上限 | 硬数量上限 | whatsapp_groups_groups_messaging_md |
| 一、限额与规模档位 | **1** | 条 | 单次 API 调用可置顶的消息数 | 硬数量上限 | whatsapp_groups_groups_messaging_md |
| 一、限额与规模档位 | **128** | 个字符 | API 群主题（subject）长度上限，且首尾空白会被裁掉 | 硬数量上限 | whatsapp_groups_reference_md |
| 一、限额与规模档位 | **2048** | 个字符 | API 群描述长度上限 | 硬数量上限 | whatsapp_reference_whatsapp_business_phone_number_groups_management_api_md |
| 一、限额与规模档位 | **1～1024（默认 25）** | 个群组 | 列表接口 limit 参数的取值域与默认值 | 取值域与默认值 | whatsapp_groups_reference_md |
| 一、限额与规模档位 | **5 MB** | 字节 | 群头像文件大小上限 | 硬数量上限 | whatsapp_groups_reference_md |
| 一、限额与规模档位 | **192 x 192** | 像素 | 群头像的最小尺寸（须为正方形） | 硬数量下限 | whatsapp_groups_reference_md |
| 二、速率与吞吐档位 | **35** | 条／月 | 广播列表每月可免费发出的广播条数 | 软限额（官方注明分批开放） | faq_1176325363861991 |
| 二、速率与吞吐档位 | **100 条／14 天** | 条 | 新成员入群时可随之同步的历史消息条数与其时间跨度 | 硬数量上限 | faq_1139252413769848 |
| 二、速率与吞吐档位 | **1,000** | 条 | Groups API 与 1:1 服务消息共用的免费额度条数 | 免费额度 | whatsapp_groups_pricing_md |
| 二、速率与吞吐档位 | **按送达人数逐人计费** | 人／条 | 群发一次消息的计费单位：每个送达的接收者算一个单位 | 计费口径 | whatsapp_groups_pricing_md |
| 二、速率与吞吐档位 | **4／5** | 人 | 官方举例：五人里只有四人送达则仅按四人计费 | 示例值（不当作规则） | whatsapp_groups_pricing_md |
| 二、速率与吞吐档位 | **未给出数值** | 次／时段 | 错误码 131208 说明的「短时间内群组操作过多」——官方只定性、未给阈值 | 官方未写明 | whatsapp_groups_error_codes_md |
| 二、速率与吞吐档位 | **未给出数值** | 条／日 | 131214 的触发口径「过去 7 天内客服窗口里营销消息过多」——只给窗口、未给条数 | 官方未写明 | whatsapp_groups_error_codes_md |
| 三、时长与窗口 | **24 小时／7 天／90 天** | 三档 | 消失消息可选的三档时长（群与单聊同套） | 取值档位 | faq_515705483748188 |
| 三、时长与窗口 | **24 小时／7 天／30 天** | 三档 | 消费端置顶横幅的三档时长 | 取值档位 | faq_294619079641794 |
| 三、时长与窗口 | **1～30** | 天 | Groups API 置顶参数 expiration_days 的取值域 | 取值域 | whatsapp_groups_groups_messaging_md |
| 三、时长与窗口 | **8 小时／1 周／始终** | 三档 | 静音会话的三档时长 | 取值档位 | faq_694350718331007 |
| 三、时长与窗口 | **30** | 天 | 频道更新从服务器上被移除的时限 | 服务端留存上限 | faq_394993313189648 |
| 三、时长与窗口 | **30** | 天 | 频道管理员可编辑或删除已发出更新的时间窗 | 可编辑窗口 | faq_360977646301595 |
| 三、时长与窗口 | **30** | 天 | 未关注者也可见的频道历史跨度 | 公开可见窗口 | faq_699347952293912 |
| 三、时长与窗口 | **30** | 天 | 删除频道投票的时间窗 | 可删除窗口 | faq_223836640775084 |
| 三、时长与窗口 | **约 30** | 天 | 被「保留」的消息可被取消保留的时间窗 | 可撤销窗口（官方措辞为「约」） | faq_673193694148537 |
| 三、时长与窗口 | **30** | 天 | 频道数据看板各项指标统一的统计跨度 | 统计跨度 | faq_1446688872845683 |
| 三、时长与窗口 | **60** | 天 | 已离群成员在「曾经成员」名单里被继续展示的时间窗 | 记录留存窗口 | faq_498814665492149 |
| 三、时长与窗口 | **60** | 天 | 成员变动记录可回溯的跨度 | 记录可查跨度 | faq_7179561392143247 |
| 三、时长与窗口 | **24** | 小时 | 离开群组两次后，管理员或成员可再次邀请你的等待时长 | 冷却窗口 | faq_420332980275753 |
| 三、时长与窗口 | **81** | 天 | 反复离群再入群被封顶的最长等待 | 冷却窗口上限 | faq_420332980275753 |
| 三、时长与窗口 | **24** | 小时 | 离开社区两次后再次加入的等待时长 | 冷却窗口 | faq_651003086597307 |
| 三、时长与窗口 | **81** | 天 | 反复离开社区的最长等待上限 | 冷却窗口上限 | faq_651003086597307 |
| 三、时长与窗口 | **24** | 小时 | 加人群组或新建群组被临时限制后的等待时长 | 冷却窗口 | faq_841426356990637 |
| 三、时长与窗口 | **7** | 天 | 频道管理员邀请未被接受时的失效时长 | 邀请有效期 | faq_1142317320080231 |
| 三、时长与窗口 | **24** | 小时 | 状态（含群状态）的存续时长 | 内容存续窗口 | faq_1310128517201866 |
| 三、时长与窗口 | **24** | 小时 | 举报复核的常规处理时长 | 处理时长（官方措辞为「通常」） | faq_3440358352849077 |
| 三、时长与窗口 | **24** | 小时 | 官方就模板审核给出的最长时长 | 处理时长上限 | whatsapp_groups_reference_md |
| 三、时长与窗口 | **60** | 分钟 | 无人加入时语音聊天自动结束的等待时长 | 自动结束窗口 | faq_3966323053691518 |
| 三、时长与窗口 | **30** | 天 | Cloud API 消息在服务器上的最长留存 | 服务端留存上限 | whatsapp_data_privacy_and_security_md |
| 三、时长与窗口 | **30** | 天 | 标识符在最近一次状态更新之后被删除的时限 | 服务端留存上限 | whatsapp_data_privacy_and_security_md |
| 三、时长与窗口 | **12** | 个月 | 活动可提前添加通话链接的最长时间跨度 | 提前量上限 | faq_2127363544304650 |
| 三、时长与窗口 | **3** | 小时 | 活动未设结束时间时可用的「开始后 3 小时」置顶档 | 取值档位 | faq_3313983622238973 |
| 四、版本与日期节点 | **Android 10 及以上** | 版本 | 该版本起桌面弹窗通知不再可用 | 版本分界 | faq_797069521522888 |
| 四、版本与日期节点 | **2026-09-30** | 日期 | 群计费口径切换的分界日（此前非模板与客服窗口内实用消息免费） | 计费口径切换节点 | whatsapp_groups_pricing_md |
| 四、版本与日期节点 | **2026-10-01** | 日期 | 此后实用消息在客服窗口内改为计费、非模板消息仅在免费额度内免费 | 计费口径切换节点 | whatsapp_groups_pricing_md |
| 四、版本与日期节点 | **2024-11** | 日期 | 商业发消息政策要求先取得 opt-in 的更新节点 | 政策生效节点 | whatsapp_getting_opt_in_md |
| 五、编号与计数口径 | **4** | 类载体 | 本篇治理开关矩阵覆盖的载体：群组／社区／频道／广播列表 | 本篇统计 | —（本篇统计，不落单页） |
| 五、编号与计数口径 | **126** | 个登记位 | 附表 A 登记的官方页面数 | 本篇统计 | —（本篇统计，不落单页） |
| 五、编号与计数口径 | **4** | 页 | 登记了但正文不可引用的页面数（3 页正文未取回＋1 页只有地域限定提示） | 本篇统计 | —（本篇统计，不落单页） |
| 五、编号与计数口径 | **15** | 个错误码 | Groups API 错误码页给出的错误码条数 | 本篇统计 | whatsapp_groups_error_codes_md |
| 五、编号与计数口径 | **4** | 个回调字段 | 接收群组元数据须订阅的 webhook 字段数 | 本篇统计 | whatsapp_groups_webhooks_md |
| 五、编号与计数口径 | **16** | 个事件小节 | 四个回调字段下的事件小节总数（4／7／3／2） | 本篇统计 | whatsapp_groups_webhooks_md |
| 五、编号与计数口径 | **2** | 个取值 | join_approval_mode 的取值数 | 本篇统计 | whatsapp_groups_reference_md |
| 五、编号与计数口径 | **3** | 个类别 | 群计费 pricing.category 的类别数 | 本篇统计 | whatsapp_groups_pricing_md |
| 五、编号与计数口径 | **4** | 种消息类型 | Groups API 群消息回调里 message type 支持的四类 | 本篇统计 | whatsapp_webhooks_reference_messages_group_md |
| **合计** | **99** 行／**57** 个去重条目 | **41** 种计数单位 | 五档分别对应规模上限、速率吞吐、时长窗口、版本节点、编号口径 | 性质列共 **43** 种 | 官方写成区间或多档的行保留原式，写法差异见附表 F |

#ROWS=99 COLS=6

# 附表 C 治理开关矩阵（59 行）

本表把官方散落在帮助中心各页与 Groups API 文档里的**治理开关**并成一张表，列序为：载体（群组／社区／频道／广播列表／Groups API）｜开关或规则名（照抄官方控件名）｜默认值｜谁能改｜效果｜官方是否写明｜出处页。「官方是否写明」只允许五类，越界即中止出表：**写明默认值与可改方**（默认值与可改方两格都写到了）、**只写方向（默认值未写明）**（官方只写开启与关闭各是什么，没写当前默认在哪一档）、**无开关（行为固定）**（官方没有给可拨的控件，规则恒定）、**以规模或身份为条件（非开关）**（能否用某项能力由群人数或成员身份自动决定）、**写明但互相冲突**（两处官方页面都写明，但写法不一致，冲突双方在附表 F 逐条对撞）。全表 **58** 行，按载体分布：Groups API 10／广播列表 6／广播列表·商业应用侧 2／社区 9／群组·群权限面板 8／群组·身份与规模 13／频道 10；「官方是否写明」分布：以规模或身份为条件（非开关） 3／写明但互相冲突 3／写明默认值与可改方 18／只写方向（默认值未写明） 15／无开关（行为固定） 19。默认值或可改方一格写「未写明」的行共 **16** 行——本表只登记这两格留白，全篇留白清单见附表 F；留白一律不推断。

每一行的每一条引文都逐字回表核验，核验不过即中止出表；跨页互证的行把页名写在引文里而不是第 8 列。

| 序号 | 载体 | 开关或规则 | 默认值 | 谁能改 | 效果 | 官方是否写明 | 出处页 |
|---|---|---|---|---|---|---|---|
| 1 | 群组·群权限面板 | Edit group settings（改群信息） | 超过 256 人的群默认「仅管理员」；256 人及以下官方未写明 | 群管理员 | 开＝所有成员可改群名、群头像、群说明；关＝仅管理员可改 | 只写方向（默认值未写明） | `faq_526742385997912` |
| 2 | 群组·群权限面板 | Send new messages（谁能发言） | 未写明 | 群管理员 | 开＝所有成员可在群内发言；关＝仅管理员可发言 | 只写方向（默认值未写明） | `faq_526742385997912` |
| 3 | 群组·群权限面板 | Add other members（谁能拉人） | 默认所有成员可加人 | 群管理员 | 开＝所有成员可把其他人拉进群；关＝仅管理员可拉人 | 写明默认值与可改方 | `faq_526742385997912` |
| 4 | 群组·群权限面板 | Invite via group link or QR code（谁能发邀请链接） | 不足 33 人的群里默认成员可发链接 | 群管理员 | 开＝任意成员可分享入群链接；关＝仅管理员可创建入群链接 | 写明默认值与可改方 | `faq_526742385997912` |
| 5 | 群组·群权限面板 | Send message history（给新成员看历史消息） | 未写明 | 群管理员（前提是已允许成员加人） | 关＝成员添加新成员时不再看到发送消息历史的选项 | 只写方向（默认值未写明） | `faq_1237143835184017` |
| 6 | 群组·群权限面板 | Approve new members（入组须批准） | 默认关闭 | 群管理员 | 开＝想加入的人须经管理员批准才能进群；关＝无需批准即可加入 | 写明默认值与可改方 | `faq_526742385997912` |
| 7 | 群组·群权限面板 | Edit group admins（任命群管理员） | 未写明 | 任意管理员可选成员当管理员 | 被选成员成为群管理员；群创建者不可被移除 | 只写方向（默认值未写明） | `faq_633713745095781` |
| 8 | 群组·群权限面板 | 开关之间的联动 | 未写明 | 群管理员 | 开启发邀请链接后若同时开着 Approve new members，新成员仍须管理员批准 | 只写方向（默认值未写明） | `faq_526742385997912` |
| 9 | 群组·身份与规模 | 使用 @all 提及全员 | 以人数划界：32 人及以下全员可用，超过 32 人仅管理员可用 | 无人可改（由人数自动决定） | 达不到门槛的成员输入 @all 不获得全员提醒能力 | 以规模或身份为条件（非开关） | `faq_521835819370205` |
| 10 | 群组·身份与规模 | 群内开关 disappearing messages | 默认消息计时器为关闭 | 默认可由任意成员开启；管理员可收窄为仅管理员 | 开启后新消息按所选时长消失；关闭权限后普通成员不能再关 | 写明默认值与可改方 | `faq_515705483748188` |
| 11 | 群组·身份与规模 | 群内置顶消息权限 | 未写明 | 群管理员可选择允许成员置顶 | 允许后成员可把消息置顶为横幅；置顶条数与时长见附表 B | 只写方向（默认值未写明） | `faq_294619079641794` |
| 12 | 群组·身份与规模 | 谁能把我加进群（群隐私） | 默认 Everyone | 本人（隐私设置） | Everyone＝任何用户都能把我加进或邀请进群 | 写明默认值与可改方 | `faq_424124173736394` |
| 13 | 群组·身份与规模 | 移除群管理员 | 未写明 | 仅其他管理员可移除管理员 | 普通成员不能移除管理员 | 写明默认值与可改方 | `faq_424124173736394` |
| 14 | 群组·身份与规模 | Mute（静音该群通知） | 未写明 | 成员本人 | 可只静音群消息或群来电，档位为 8 小时／1 周／一直 | 只写方向（默认值未写明） | `faq_694350718331007` |
| 15 | 群组·身份与规模 | 为所有人删除群 | 无开关，行为固定 | 仅管理员 | 只有管理员能把群从所有成员会话中删除 | 无开关（行为固定） | `faq_678712076864311` |
| 16 | 群组·身份与规模 | Restricted chat（受限聊天） | 未写明 | 群内管理员可启用；只能在主设备上开关 | 启用后对聊天内全部消息生效；已有高级聊天隐私的聊天不会自动升级 | 只写方向（默认值未写明） | `faq_715385484388016` |
| 17 | 群组·身份与规模 | 群满或仅管理员可加人时的选人界面 | 无开关，行为固定 | 无人可改 | 该群在选择界面变灰、不可选 | 无开关（行为固定） | `faq_841426356990637` |
| 18 | 群组·身份与规模 | 群语音聊天的可用人数下限 | 以人数划界：3 人及以上可用，且按 3—32 与 33 人以上两种入口 | 无人可改（由人数自动决定） | 3—32 人用下滑长按发起；33 人及以上走菜单项 | 以规模或身份为条件（非开关） | `faq_3966323053691518` |
| 19 | 群组·身份与规模 | 语音聊天自动结束 | 60 分钟无人加入即自动结束 | 无人可改 | 语音聊天自行结束 | 无开关（行为固定） | `faq_3966323053691518` |
| 20 | 群组·身份与规模 | 群历史消息对新成员可见性 | 两处写法不一致（见附表 F） | 群管理员（Send message history 开关） | 一处写新成员无法查看历史消息，另一处写经邀请链接加入者可收到历史消息 | 写明但互相冲突 | `faq_1062135416113130` |
| 21 | 群组·身份与规模 | WhatsApp 是否介入群管理 | 无开关，官方明确不介入 | 无人可改 | 官方称不能替任何人任命管理员，即使其此前曾任管理员 | 无开关（行为固定） | `faq_633713745095781` |
| 22 | 社区 | 公告组发言权 | 无开关，规则固定 | 仅社区管理员 | 只有社区管理员能在公告组向全体成员发消息 | 无开关（行为固定） | `faq_1859295147860069` |
| 23 | 社区 | 公告组回复 | 官方明确无法关闭 | 无人可改 | 社区内回复无法关闭或禁用 | 无开关（行为固定） | `faq_742237357891080` |
| 24 | 社区 | 总群管理员任命 | 社区管理员默认自动成为总群管理员 | 社区管理员可提拔或解除总群管理员 | 总群自动由社区管理员治理 | 写明默认值与可改方 | `faq_1418342145436771` |
| 25 | 社区 | 总群 Approve New Members | 默认关闭 | 社区管理员 | 默认状态下成员可直接参与总群讨论 | 写明默认值与可改方 | `faq_1418342145436771` |
| 26 | 社区 | 社区管理员任命权 | 未写明 | 仅社区管理员可把成员升为社区管理员 | 社区管理员不能解除创建该社区的管理员 | 只写方向（默认值未写明） | `faq_441425088132574` |
| 27 | 社区 | 把群从社区解除 | 无开关 | 仅社区或群管理员 | 可解除群组，并可把被解除群的成员移出社区 | 写明默认值与可改方 | `faq_970281987938470` |
| 28 | 社区 | 公告组的表情与投票 | 1,024 人及以下可用 | 无人可改（由规模触发） | 公告组一旦超过 1,024 人，群内任何人都不能再回应公告或投票 | 以规模或身份为条件（非开关） | `faq_1859295147860069` |
| 29 | 社区 | 公告组成员总数可见性 | 两处写法不一致（见附表 F） | 无人可改 | 一处写仅管理员可见公告组总人数，另一处写人人可见 | 写明但互相冲突 | `faq_742237357891080` |
| 30 | 社区 | 社区内群的可发现性 | 无开关 | 无人可改 | 任何社区成员或管理员都能发现同属该社区的群 | 无开关（行为固定） | `faq_967457667545238` |
| 31 | 频道 | 频道通知 | 默认静音 | 关注者本人逐频道开启 | 开启后与普通消息通知行为一致；不开则只在频道内可见 | 写明默认值与可改方 | `faq_549900560675125` |
| 32 | 频道 | admin profiles（管理员身份展示） | 未写明 | 任意频道管理员可在频道设置里开启 | 开启后该身份出现在管理员发布的更新上 | 只写方向（默认值未写明） | `faq_360977646301595` |
| 33 | 频道 | 语音更新转写 | 默认关闭 | 观看者本人（设置 > 聊天，或收到语音更新时就地开启） | 开启后可查看频道语音更新的转写 | 写明默认值与可改方 | `faq_1339924517208346` |
| 34 | 频道 | 允许哪类表情回应 | 未写明 | 频道管理员可改频道设置 | 限定频道更新上可用于回应的表情类型 | 只写方向（默认值未写明） | `faq_360977646301595` |
| 35 | 频道 | 邀请与解除频道管理员 | 仅频道所有者可增删管理员 | 频道所有者 | 其他管理员既不能加也不能删管理员；任意管理员可主动退出 | 写明默认值与可改方 | `faq_360977646301595` |
| 36 | 频道 | 频道更新浏览量可见性 | 无开关，规则固定 | 仅频道管理员 | 管理员可见每条更新被浏览次数；普通关注者不可见 | 无开关（行为固定） | `faq_360977646301595` |
| 37 | 频道 | 关闭频道的复核与申诉 | 每条告警只能提交一次 | 仅频道管理员可提交；一位提交后全体管理员可见进度 | 用于认为频道被误关时申请复核 | 写明默认值与可改方 | `faq_394993313189648` |
| 38 | 频道 | 频道管理员的双步验证 | 官方以建议句给出，未写为强制开关 | 各管理员本人 | 频道治理页用祈使句要求全部管理员开启双步验证 | 只写方向（默认值未写明） | `faq_1142317320080231` |
| 39 | 频道 | 每月自动频道报告 | 默认按月生成 | 使用者本人 | 关闭后不再每月生成新的频道报告 | 写明默认值与可改方 | `faq_6480469855345352` |
| 40 | 频道 | 频道管理员邀请关注者 | 未写明 | 频道管理员 | 可在建频道时或频道信息页邀请关注者 | 只写方向（默认值未写明） | `faq_2490388581320854／faq_627992286976376` |
| 41 | 广播列表 | 送达前提（非开关） | 规则固定 | 收件人（是否把号码存进通讯录） | 只有把号码存入通讯录的人收到广播；对方未存号则收不到 | 无开关（行为固定） | `faq_861663048350950` |
| 42 | 广播列表 | 回复走向（非开关） | 规则固定 | 无人可改 | 收件人的回复以个人消息出现，不再转发给列表内其他联系人 | 无开关（行为固定） | `faq_861663048350950` |
| 43 | 广播列表 | 会话形态（非开关） | 规则固定 | 无人可改 | 广播是一对多；要形成群会话须改建群 | 无开关（行为固定） | `faq_861663048350950` |
| 44 | 广播列表 | 桌面端支持 | 规则固定 | 无人可改 | Windows、Mac 与网页版不支持广播列表 | 无开关（行为固定） | `faq_861663048350950` |
| 45 | 广播列表 | 可建列表数量 | 无上限 | 用户本人 | 可创建任意数量的广播列表；每列表联系人数见附表 B | 写明默认值与可改方 | `faq_861663048350950` |
| 46 | 广播列表 | 每月免费广播条数 | 部分地区可见的月度上限 | 无人可改 | 看不到上限的用户按主文章口径执行 | 写明默认值与可改方 | `faq_1176325363861991` |
| 47 | 广播列表·商业应用侧 | 送达前提 | 规则固定 | 客户（是否存号） | 仅把号码存入通讯录的客户收到广播；客户侧同样给出排查动作 | 无开关（行为固定） | `faq_653415899610349` |
| 48 | 广播列表·商业应用侧 | 与标签的关系 | 未写明 | 商家本人 | 可用标签直接生成广播列表；也可把广播列表转换成其他形态（官方原句在该页被截断） | 只写方向（默认值未写明） | `faq_653415899610349` |
| 49 | Groups API | join_approval_mode（入群是否须批准） | 省略该字段时默认 auto_approve | 业务方在建群或改设置时设定 | approval_required＝须经加入请求批准；auto_approve＝点击链接即可入群 | 写明默认值与可改方 | `whatsapp_groups_reference_md` |
| 50 | Groups API | 群内置顶条数上限 | 固定 3 条 | 业务方调用接口 | 已有 3 条时再置顶会自动取消最早那条；一次调用只能置顶一条 | 无开关（行为固定） | `whatsapp_groups_groups_messaging_md` |
| 51 | Groups API | 创建者身份 | 规则固定 | 不可改 | 建群所用商业号码总以创建者兼管理员身份进入该群 | 无开关（行为固定） | `whatsapp_groups_reference_md` |
| 52 | Groups API | 加人方式 | 官方定性为 invite-only | 业务方（另见附表 F 的接口冲突） | 参与者用业务方发送的邀请链接入群，链接本身承载群的上下文信息 | 无开关（行为固定） | `whatsapp_groups_md` |
| 53 | Groups API | 模板性能指标 | 官方写明不可用 | 无人可改 | 消息模板在 Groups 场景下无性能指标；官方建议为 Groups 另建模板 | 无开关（行为固定） | `whatsapp_groups_md` |
| 54 | Groups API | 群内通话 | 官方写明不支持 | 无人可改 | 通话接口在群内不可用 | 无开关（行为固定） | `whatsapp_groups_md` |
| 55 | Groups API | 哪些号码形态用不了群功能 | 规则固定 | 无人可改 | 商业应用号码与多方案会话入驻的号码不可用群功能 | 无开关（行为固定） | `whatsapp_groups_md` |
| 56 | Groups API | 资格门槛 | 面向全部官方商业账号开放 | 无人可改 | 群功能资格以官方商业账号为条件；不满足即返回对应错误码 | 写明默认值与可改方 | `whatsapp_groups_md` |
| 57 | Groups API | 邀请链接生命周期 | 官方给出删除与新建两个动作 | 业务方调用接口 | 删除现有邀请链接后需新建；新建即产生一条新链接 | 写明但互相冲突 | `whatsapp_reference_groups_groups_invite_link_api_md` |
| 58 | Groups API | 被移出者能否再入群 | 官方写明需重新邀请 | 业务方（群参与者接口） | 被移除的参与者不再属于该群，须再次通过邀请链接或接口加入 | 只写方向（默认值未写明） | `whatsapp_groups_error_codes_md` |
| **合计** | 7 类载体 | **58** 行 | 未写明 **16** 行 | 控件名照抄官方 | 效果列为官方原句直译 | 5 类写明度 | 逐行核验原句，核验不过即中止 |

#ROWS=58 COLS=8

# 附表 D 官方取值域全表（98 行）

本表一行一个取值，收的是**一个字段的全部合法取值或一份官方清单的全部成分**；数字档位本身在附表 B 逐档登记，本表按字段成组登记，两表口径不同、不互为注脚。其中 **11** 个取值（**5** 组）由脚本从 Groups API 文档的「Values can be:」单元里直接拆出——属性名取不到、取值列表为空、同组内取值重复、首项不带反引号四种情况脚本一律中止；**86** 个取值来自脚本从页面结构里抓出的其余组或手定组：接口方法组由 5 个 API reference 页的 Method 列逐页解析（页数的不等于 5、方法集合不等于 GET／POST／DELETE 即中止），错误码组与 HTTP 状态组由错误码页表格逐行解析（条数不等于 15 即中止，且每条的错误码、错误名、说明三段都回表核验），回调字段与子场景组由回调页的二级与三级标题解析（字段组不等于 4、子场景不等于 20 即中止），其余手定组每一条取值都单独回表核验。全表合计 **97** 个取值、**27** 个取值组，覆盖 **14** 个页面；说明列超过 150 字符时按整词截断（共 **6** 行截断），截断后仍是原句前缀。

| 编号 | 取值域 | 取值 | 官方说明或本篇含义归纳 | 出处页 |
| --- | --- | --- | --- | --- |
| D1    | 消息是否计费的类型位（pricing.type） | `regular` | indicates the message is billable. | whatsapp_groups_pricing_md |
| D2    | 消息是否计费的类型位（pricing.type） | `free_group_customer_service` | through September 30, 2026, indicates a utility template message or non-template message sent within a group customer service window. As of October … | whatsapp_groups_pricing_md |
| D3    | 计费请求参数里的类别数组取值（pricing.category 大写形） | `GROUP_MARKETING` | Group messages charged the marketing rate. | whatsapp_groups_pricing_md |
| D4    | 计费请求参数里的类别数组取值（pricing.category 大写形） | `GROUP_SERVICE` | Through September 30, 2026, group messages that were not charged, including non-template messages and utility messages sent within a group customer … | whatsapp_groups_pricing_md |
| D5    | 计费请求参数里的类别数组取值（pricing.category 大写形） | `GROUP_UTILITY` | Through September 30, 2026, group messages charged the utility rate. As of October 1, 2026, group utility messages. | whatsapp_groups_pricing_md |
| D6    | 计费请求参数里的类型数组取值 | `FREE_GROUP_CUSTOMER_SERVICE` | Through September 30, 2026, group non-template and utility messages sent within group customer service windows. As of October 1, 2026, group … | whatsapp_groups_pricing_md |
| D7    | 计费请求参数里的类型数组取值 | `REGULAR` | Billable messages. Includes all authentication and marketing template messages, and any utility template messages sent outside of a customer service … | whatsapp_groups_pricing_md |
| D8    | 建群请求体里的入群审批模式 | `approval_required` | Indicates WhatsApp users must be approved via join request before they can access the group. | whatsapp_groups_reference_md |
| D9    | 建群请求体里的入群审批模式 | `auto_approve` | Indicates WhatsApp users can join the group without approval. If omitted, `join_approval_mode` is set to `auto_approve` by default. ／ `auto_approve` | whatsapp_groups_reference_md |
| D10   | 群设置与查询响应里的入群审批模式 | `approval_required` | Indicates WhatsApp users must be approved via join request before they can access the group. | whatsapp_groups_reference_md |
| D11   | 群设置与查询响应里的入群审批模式 | `auto_approve` | Indicates WhatsApp users can join the group without approval. ／ `auto_approve` | whatsapp_groups_reference_md |
| D12   | Groups API 接口方法（逐页 Method 列） | `DELETE` | 该页 Method 列里出现的 HTTP 方法；本页共 DELETE 1 个端点／POST 1 个端点。 | whatsapp_reference_groups_groups_invite_link_api_md |
| D13   | Groups API 接口方法（逐页 Method 列） | `POST` | 该页 Method 列里出现的 HTTP 方法；本页共 DELETE 1 个端点／POST 1 个端点。 | whatsapp_reference_groups_groups_invite_link_api_md |
| D14   | Groups API 接口方法（逐页 Method 列） | `DELETE` | 该页 Method 列里出现的 HTTP 方法；本页共 DELETE 1 个端点／GET 1 个端点／POST 1 个端点。 | whatsapp_reference_groups_groups_join_requests_api_md |
| D15   | Groups API 接口方法（逐页 Method 列） | `GET` | 该页 Method 列里出现的 HTTP 方法；本页共 DELETE 1 个端点／GET 1 个端点／POST 1 个端点。 | whatsapp_reference_groups_groups_join_requests_api_md |
| D16   | Groups API 接口方法（逐页 Method 列） | `POST` | 该页 Method 列里出现的 HTTP 方法；本页共 DELETE 1 个端点／GET 1 个端点／POST 1 个端点。 | whatsapp_reference_groups_groups_join_requests_api_md |
| D17   | Groups API 接口方法（逐页 Method 列） | `DELETE` | 该页 Method 列里出现的 HTTP 方法；本页共 DELETE 1 个端点／POST 1 个端点。 | whatsapp_reference_groups_groups_participants_api_md |
| D18   | Groups API 接口方法（逐页 Method 列） | `POST` | 该页 Method 列里出现的 HTTP 方法；本页共 DELETE 1 个端点／POST 1 个端点。 | whatsapp_reference_groups_groups_participants_api_md |
| D19   | Groups API 接口方法（逐页 Method 列） | `DELETE` | 该页 Method 列里出现的 HTTP 方法；本页共 DELETE 1 个端点／GET 1 个端点／POST 1 个端点。 | whatsapp_reference_groups_groups_query_api_md |
| D20   | Groups API 接口方法（逐页 Method 列） | `GET` | 该页 Method 列里出现的 HTTP 方法；本页共 DELETE 1 个端点／GET 1 个端点／POST 1 个端点。 | whatsapp_reference_groups_groups_query_api_md |
| D21   | Groups API 接口方法（逐页 Method 列） | `POST` | 该页 Method 列里出现的 HTTP 方法；本页共 DELETE 1 个端点／GET 1 个端点／POST 1 个端点。 | whatsapp_reference_groups_groups_query_api_md |
| D22   | Groups API 接口方法（逐页 Method 列） | `GET` | 该页 Method 列里出现的 HTTP 方法；本页共 GET 1 个端点／POST 1 个端点。 | whatsapp_reference_whatsapp_business_phone_number_groups_management_api_md |
| D23   | Groups API 接口方法（逐页 Method 列） | `POST` | 该页 Method 列里出现的 HTTP 方法；本页共 GET 1 个端点／POST 1 个端点。 | whatsapp_reference_whatsapp_business_phone_number_groups_management_api_md |
| D24   | Groups API 错误码 | `131020` | Bad Group——Cannot send messages to single member groups. | whatsapp_groups_error_codes_md |
| D25   | Groups API 错误码 | `131041` | Group unknown——The group was not found, either because it doesn't exist or you are not a member. | whatsapp_groups_error_codes_md |
| D26   | Groups API 错误码 | `131059` | Invalid cursor——The cursor has either expired or become corrupted. Start pagination from the beginning again. | whatsapp_groups_error_codes_md |
| D27   | Groups API 错误码 | `131201` | Request partially succeeded——Not all participant-level operations in the request succeeded. | whatsapp_groups_error_codes_md |
| D28   | Groups API 错误码 | `131202` | Duplicate participant——Duplicate participants in the participant array input. | whatsapp_groups_error_codes_md |
| D29   | Groups API 错误码 | `131204` | Participant overlimit——Group participant size exceeds limit. | whatsapp_groups_error_codes_md |
| D30   | Groups API 错误码 | `131207` | Group suspended——The group violates platform policies. | whatsapp_groups_error_codes_md |
| D31   | Groups API 错误码 | `131208` | Group Rate Limit Hit——Group operation failed because there were too many group operations from this phone number in a short period. | whatsapp_groups_error_codes_md |
| D32   | Groups API 错误码 | `131209` | Invalid Group Profile Picture Aspect Ratio——Width and height of the image must be equal. | whatsapp_groups_error_codes_md |
| D33   | Groups API 错误码 | `131210` | Image is Too Small to Process——Image width and height must be greater than 192px. | whatsapp_groups_error_codes_md |
| D34   | Groups API 错误码 | `131211` | Group create limit reached——Reached the limit for the maximum number of groups that can be created for this number. | whatsapp_groups_error_codes_md |
| D35   | Groups API 错误码 | `131212` | Participant is not a part of the group.——Participant is not a part of the group. | whatsapp_groups_error_codes_md |
| D36   | Groups API 错误码 | `131213` | Group join request does not exist.——Group join request does not exist. | whatsapp_groups_error_codes_md |
| D37   | Groups API 错误码 | `131214` | Group creation is temporarily disabled——Group creation is temporarily disabled due to excessive marketing messages sent by the Messaging account in … | whatsapp_groups_error_codes_md |
| D38   | Groups API 错误码 | `131215` | This phone number is not eligible to access Groups APIs——Groups APIs are only available for eligible phone numbers. Check eligibility for Groups APIs … | whatsapp_groups_error_codes_md |
| D39   | Groups 错误码页出现的 HTTP 状态 | `206` | 该状态码在本页右侧列里的写法： | whatsapp_groups_error_codes_md |
| D40   | Groups 错误码页出现的 HTTP 状态 | `400` | 该状态码在本页右侧列里的写法： | whatsapp_groups_error_codes_md |
| D41   | Groups 错误码页出现的 HTTP 状态 | `403` | 该状态码在本页右侧列里的写法： | whatsapp_groups_error_codes_md |
| D42   | Groups 错误码页出现的 HTTP 状态 | `429` | 该状态码在本页右侧列里的写法： | whatsapp_groups_error_codes_md |
| D43   | Groups API 可订阅回调字段 | `group_lifecycle_update` | 本页以「## `group_lifecycle_update` webhooks」成节；该节下官方给出 4 个子场景（逐条见下一组）。 | whatsapp_groups_webhooks_md |
| D44   | Groups 回调子场景（小节标题逐字） | `Group create succeed` | 官方在本页为「group_lifecycle_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D45   | Groups 回调子场景（小节标题逐字） | `Group create fail` | 官方在本页为「group_lifecycle_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D46   | Groups 回调子场景（小节标题逐字） | `Delete group succeed` | 官方在本页为「group_lifecycle_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D47   | Groups 回调子场景（小节标题逐字） | `Delete group fails` | 官方在本页为「group_lifecycle_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D48   | Groups API 可订阅回调字段 | `group_participants_update` | 本页以「## `group_participants_update` webhooks」成节；该节下官方给出 7 个子场景（逐条见下一组）。 | whatsapp_groups_webhooks_md |
| D49   | Groups 回调子场景（小节标题逐字） | `User joined group using invite link succeed` | 官方在本页为「group_participants_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D50   | Groups 回调子场景（小节标题逐字） | `User accepts or cancels join request` | 官方在本页为「group_participants_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D51   | Groups 回调子场景（小节标题逐字） | `Join request approved` | 官方在本页为「group_participants_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D52   | Groups 回调子场景（小节标题逐字） | `Group participant remove succeed` | 官方在本页为「group_participants_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D53   | Groups 回调子场景（小节标题逐字） | `Group participant remove with participants partially fails` | 官方在本页为「group_participants_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D54   | Groups 回调子场景（小节标题逐字） | `Group participant remove fails` | 官方在本页为「group_participants_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D55   | Groups 回调子场景（小节标题逐字） | `Group participant leaves webhook` | 官方在本页为「group_participants_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D56   | Groups API 可订阅回调字段 | `group_settings_update` | 本页以「## `group_settings_update` webhooks」成节；该节下官方给出 3 个子场景（逐条见下一组）。 | whatsapp_groups_webhooks_md |
| D57   | Groups 回调子场景（小节标题逐字） | `Group settings update succeed` | 官方在本页为「group_settings_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D58   | Groups 回调子场景（小节标题逐字） | `Group settings update partial fail` | 官方在本页为「group_settings_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D59   | Groups 回调子场景（小节标题逐字） | `Group settings update total fail` | 官方在本页为「group_settings_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D60   | Groups API 可订阅回调字段 | `group_status_update` | 本页以「## `group_status_update` webhooks」成节；该节下官方给出 2 个子场景（逐条见下一组）。 | whatsapp_groups_webhooks_md |
| D61   | Groups 回调子场景（小节标题逐字） | `Group suspended` | 官方在本页为「group_status_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D62   | Groups 回调子场景（小节标题逐字） | `Group suspension cleared` | 官方在本页为「group_status_update」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D63   | Groups 回调子场景（小节标题逐字） | `Multiple participants, single message` | 官方在本页为「Group message status webhooks」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D64   | Groups 回调子场景（小节标题逐字） | `Multiple messages, single participant` | 官方在本页为「Group message status webhooks」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D65   | Groups 回调子场景（小节标题逐字） | `Group message delivered` | 官方在本页为「Group message status webhooks」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D66   | Groups 回调子场景（小节标题逐字） | `Pricing information` | 官方在本页为「Group message status webhooks」单列的子场景标题。 | whatsapp_groups_webhooks_md |
| D67   | 群设置更新回调的设置对象 | `profile_picture` | 群头像变更结果，对象里带 mime_type 与 sha256，并带 update_successful 布尔位。 | whatsapp_groups_webhooks_md |
| D68   | 群设置更新回调的设置对象 | `group_subject` | 群主题变更结果，对象里带新的 text，并带 update_successful 布尔位。 | whatsapp_groups_webhooks_md |
| D69   | 群设置更新回调的设置对象 | `group_description` | 群说明变更结果，对象里带新的 text，并带 update_successful 布尔位。 | whatsapp_groups_webhooks_md |
| D70   | 群参与者变更回调的发起方 | `business` | 官方在示例载荷里给出的 initiated_by 取值。 | whatsapp_groups_webhooks_md |
| D71   | 群参与者变更回调的发起方 | `participant` | 官方在示例载荷里给出的 initiated_by 取值。 | whatsapp_groups_webhooks_md |
| D72   | Groups API 支持的消息类型 | `Text` | 该页「Quick facts」里 Supported message types 一项的列举成分。 | whatsapp_groups_md |
| D73   | Groups API 支持的消息类型 | `media` | 该页「Quick facts」里 Supported message types 一项的列举成分。 | whatsapp_groups_md |
| D74   | Groups API 支持的消息类型 | `text-based templates` | 该页「Quick facts」里 Supported message types 一项的列举成分。 | whatsapp_groups_md |
| D75   | Groups API 支持的消息类型 | `media-based templates` | 该页「Quick facts」里 Supported message types 一项的列举成分。 | whatsapp_groups_md |
| D76   | Groups API 点名不支持的消息类型 | `Calling` | 该页「Non-supported message types」列表内的一项。 | whatsapp_groups_md |
| D77   | Groups API 点名不支持的消息类型 | `Disappearing messages` | 该页「Non-supported message types」列表内的一项。 | whatsapp_groups_md |
| D78   | Groups API 点名不支持的消息类型 | `View-once` | 该页「Non-supported message types」列表内的一项。 | whatsapp_groups_md |
| D79   | Groups API 点名不支持的消息类型 | `Auth` | 该页「Non-supported message types」列表内的一项。 | whatsapp_groups_md |
| D80   | Groups API 点名不支持的消息类型 | `Commerce messages` | 该页「Non-supported message types」列表内的一项。 | whatsapp_groups_md |
| D81   | Groups API 点名不支持的消息类型 | `Interactive messages` | 该页「Non-supported message types」列表内的一项。 | whatsapp_groups_md |
| D82   | Groups API 点名不支持的动作 | `Admin hide group participant list` | 该页「Non-supported actions」列表内的一项。 | whatsapp_groups_md |
| D83   | Groups API 点名不支持的动作 | `Edit message` | 该页「Non-supported actions」列表内的一项。 | whatsapp_groups_md |
| D84   | Groups API 点名不支持的动作 | `Delete message` | 该页「Non-supported actions」列表内的一项。 | whatsapp_groups_md |
| D85   | 帮助中心离散选项：消失消息时长 | `24 hours` | 该页在本节里给出的可选档位之一。 | faq_5590979194321336 |
| D86   | 帮助中心离散选项：消失消息时长 | `7 days` | 该页在本节里给出的可选档位之一。 | faq_5590979194321336 |
| D87   | 帮助中心离散选项：消失消息时长 | `90 days` | 该页在本节里给出的可选档位之一。 | faq_5590979194321336 |
| D88   | 帮助中心离散选项：消失消息时长 | `Off` | 该页在本节里给出的可选档位之一。 | faq_5590979194321336 |
| D89   | 帮助中心离散选项：群内置顶时长 | `24 hours` | 该页在本节里给出的可选档位之一。 | faq_294619079641794 |
| D90   | 帮助中心离散选项：群内置顶时长 | `7 days` | 该页在本节里给出的可选档位之一。 | faq_294619079641794 |
| D91   | 帮助中心离散选项：群内置顶时长 | `30 days` | 该页在本节里给出的可选档位之一。 | faq_294619079641794 |
| D92   | 帮助中心离散选项：邀请链接有效期 | `24 hours` | 该页在本节里给出的可选档位之一。 | faq_3242937609289432 |
| D93   | 帮助中心离散选项：邀请链接有效期 | `7 days` | 该页在本节里给出的可选档位之一。 | faq_3242937609289432 |
| D94   | 帮助中心离散选项：邀请链接有效期 | `90 days` | 该页在本节里给出的可选档位之一。 | faq_3242937609289432 |
| D95   | 帮助中心离散选项：静音时长 | `8 hours` | 该页在本节里给出的可选档位之一。 | faq_694350718331007 |
| D96   | 帮助中心离散选项：静音时长 | `1 week` | 该页在本节里给出的可选档位之一。 | faq_694350718331007 |
| D97   | 帮助中心离散选项：静音时长 | `Always` | 该页在本节里给出的可选档位之一。 | faq_694350718331007 |
| **合计** | **27** 组 | **97** 个取值 | 「Values can be」解析 5 组／11 个取值，其余结构解析与手定 86 个取值 | **14** 页 |

#ROWS=97 COLS=5

# 附表 E 四类载体逐项对照（31 行）

本表把**群组、社区、频道、广播列表**四类载体按同一组维度并排。列序为：编号｜对照维度｜群组｜社区｜频道｜广播列表。前三行起，每个非空白单元格都是**所引页面的逐字原句**（超过 112 字符按字符截断并以省略号结尾），共 **76** 格逐字回表核验，核验不过即记入失败清单；标作「官方未按载体区分」的 **44** 格是官方文本没有按载体写这一项，**不代表该载体没有该项规则**，也不代表本篇判定其不存在。全表 **30** 个维度，逐字原句按载体分布：群组 24 格／社区 19 格／频道 19 格／广播列表 14 格；引用页面去重 **48** 页。同一原句可在多行复用（例如 1,024 既是社区公告组的互动上限也是它的规模上限），复用时出处页与原句不变。

| 编号 | 对照维度 | 群组 | 社区 | 频道 | 广播列表 |
|---|---|---|---|---|---|
| E1   | 官方定位（一句话原句） | Broadcast lists are a one-to-many communication. If you want your recipients to participate in a group conversat… | This is a space where community admins can send messages to all community members. | With a channel, you can share one-way updates to an unlimited audience. | You can use broadcast lists to send a message to several contacts at once. |
| E2   | 受众规模上限 | A WhatsApp group can have up to 1,024 members. | You can have up to 2,000 members in new and existing communities. | With a channel, you can share one-way updates to an unlimited audience. | You can include up to 256 contacts in each broadcast list. |
| E3   | 上限是否包含创建者 | The maximum group size is 1,024 members (1,025 including the group creator). | This limit includes the total number of members in the sub-groups and announcement groups within the community. | 官方未按载体区分 | You can include up to 256 contacts in each broadcast list. |
| E4   | 可建数量是否写明上限 | 官方未按载体区分 | 官方未按载体区分 | 官方未按载体区分 | You can create as many broadcast lists as you like. |
| E5   | 达到上限后的行为 | When the community general chat reaches 1,024 members, new community members can’t join or be added to the group… | When an Announcements group passes 1,024 members, no one in the group can react to announcements or vote in poll… | 官方未按载体区分 | 官方未按载体区分 |
| E6   | 加入方式 | Anyone with a group link can join your group and forward the link to other people. | When you join a community, you can choose to join general chat, unless the chat is full. | Users can’t be added to a channel without choosing to follow it. | Only people who’ve added you to their phone’s contacts will receive your broadcast message. |
| E7   | 加入是否需要审批 | The Approve new members setting is turned off by default. | Approve New Members is turned off by default so that members can easily participate in the community general cha… | 官方未按载体区分 | 官方未按载体区分 |
| E8   | 谁能添加成员 | By default, members in groups with fewer than 33 members can send a group link to invite others to the group. | You can add members to the general chat. | Channel admins can invite followers when creating a channel or from the channel info page after you create your … | Search for or select the contacts you want to add. |
| E9   | 管理员数量 | A group can have an unlimited number of admins, and any admin can make a member an admin. | 官方未按载体区分 | 官方未按载体区分 | 官方未按载体区分 |
| E10  | 成员（粉丝）名单可见性 | You can't hide group members' phone numbers in a group. | Admins can see who the members are. | People viewing a channel can’t see who is following a channel. | It won't be sent to other customers on your broadcast list. |
| E11  | 新成员能否看历史消息 | Group admins have the option to turn off the ability for members to send message history. | 官方未按载体区分 | 官方未按载体区分 | 官方未按载体区分 |
| E12  | 被移出后能否再加入 | To rejoin a group chat you've left or deleted, you'll need to ask the group admin to re-invite you. | To rejoin a community that you left, use the invite link or rejoin any of the groups that are in that community. | 官方未按载体区分 | 官方未按载体区分 |
| E13  | 谁能发言 | When turned off, only admins can send messages in the group. | This is a space where community admins can send messages to all community members. | Followers can’t reply directly to updates or send messages to channel admins. | Broadcast lists are a one-to-many communication. |
| E14  | 谁能改名称、头像、说明 | When turned off, only admins can edit group settings. | You can edit the general chat name, description, and photo and will see the members list as people join. | Any admin can make updates or manage channel info: Once an invitation is accepted, an admin can manage your chan… | 官方未按载体区分 |
| E15  | 回复能否被关闭 | When turned off, only admins can send messages in the group. | It's not possible to turn off or disable replies in a community. | Followers can’t reply directly to updates or send messages to channel admins. | Their reply will also appear as an individual message and won’t be sent to other contacts in the broadcast list. |
| E16  | 反应与投票 | 官方未按载体区分 | Only admins can send announcements and start polls, but members can reply and react to them. | You can also customize which emojis people can use to react to your channel updates. | 官方未按载体区分 |
| E17  | 语音会话与通话 | Group calling allows up to 32 people to join a WhatsApp group voice call. | If no one joins the voice chat within 60 minutes, it ends automatically. | Voice transcripts are off by default. | 官方未按载体区分 |
| E18  | 端到端加密是否适用 | Messages in message history are end-to-end encrypted, meaning no one outside of the group, not even WhatsApp, ca… | Your personal messages and calls in communities are always end-to-end encrypted. | Most importantly, channels aren't end-to-end encrypted. | 官方未按载体区分 |
| E19  | 消失消息是否可开 | Send disappearing messages: You can decide how long messages remain visible once you've sent them - 24 hours, 7 … | 官方未按载体区分 | 官方未按载体区分 | 官方未按载体区分 |
| E20  | 转发规则 | You can forward messages to up to five people or groups at a time. | If you’re forwarding a message or update that was forwarded to you, it can only be shared with one additional gr… | You can forward from a channel to individual or group chats, or to your WhatsApp Status. | 官方未按载体区分 |
| E21  | 置顶数量与时长 | You can pin up to four messages to the top of a chat. | You can pin an event to the top of a individual or group chat to help remind people about the upcoming event. | You can only pin two channels to the top of your channels list at a time. | 官方未按载体区分 |
| E22  | 静音 | You can choose to mute them for 8 hours, 1 week, or Always. | 官方未按载体区分 | Notifications are muted by default. | 官方未按载体区分 |
| E23  | 举报与拉黑 | Check the box if you would like to also block the sender or exit the group and delete the chat. | You can also mute, exit, and report the general chat. | Review requests can only be submitted once per alert, and only channel admins can submit requests. | 官方未按载体区分 |
| E24  | 归档与删除 | Only admins can delete groups for all members. | You can archive a group that’s part of a community. But, you can’t archive a whole community. | When you delete your channel, it doesn’t affect channel information and content that other users may continue to… | Check the box if you’d also like to delete media received from the device gallery. |
| E25  | 桌面与网页端可用性 | 官方未按载体区分 | 官方未按载体区分 | 官方未按载体区分 | Broadcast lists aren't supported on Windows, Mac, or Web. |
| E26  | 是否有官方编程接口 | Groups are an invite-only experience where participants join using a group invite link you send them. | 官方未按载体区分 | 官方未按载体区分 | 官方未按载体区分 |
| E27  | WhatsApp 能否介入该载体的管理 | WhatsApp can't interfere with group administration functions. For example, we can't make someone an admin. | 官方未按载体区分 | WhatsApp will suspend channels if admin(s) repeatedly post content that violates our terms and policies, includi… | 官方未按载体区分 |
| E28  | 月度广播条数上限 | 官方未按载体区分 | 官方未按载体区分 | 官方未按载体区分 | People in some countries might see a monthly broadcast limit. |
| E29  | 商业化与广告 | 官方未按载体区分 | 官方未按载体区分 | We don’t share personal information (like phone number) with Meta, so Meta cannot identify you and is only able … | Marketing messages is now business broadcasts in some countries. |
| E30  | 数据指标可见性 | 官方未按载体区分 | 官方未按载体区分 | Channel insights aren’t available for channels with fewer than 100 followers. | You can also see how many of your customers have read and replied to your broadcasts to understand how they perf… |
| **合计** | **30** 个维度 | 逐字核验 **76** 格 | 未按载体区分 **44** 格 | 群组 24／社区 19／频道 19／广播列表 14 | 去重 **48** 页 |

#ROWS=30 COLS=6

# 附表 F 口径冲突与官方未写明处（31 行）

本表登记的每一条都成对出现：凡官方写出的一侧都是所引页面的逐字原句，核验不过即记入失败清单；留白的一侧标作"官方未写明"，共 7 格，含义是官方没有这句话，不是该项规则不存在。差异性质限定在 19 种说法之内，出现表外说法或某种说法一次都没用上都会中止。末尾 4 行由登记表自动生成，逐行对应附表 A 判定的不可引用页，条数与附表 A 的不可引用计数不一致即中止。格子里的原句实测最长 219 字符，超过 230 字符按整词截断（共 0 格截断），截断后仍是原句前缀；回表核验用的是未截断的整句，不是格子里显示的那一段。写出的一侧必须是带句末标点的完整原句，唯一例外是官方把这条规则写在一个表格单元里、整格照抄，这样的格子共 1 格，仍逐字回表核验，只是不套句子形状。

| 编号 | 议题 | 口径甲 | 口径乙 | 差异性质 | 本篇处理 |
| --- | --- | --- | --- | --- | --- |
| F1   | 顶置消息的条数上限 | 「You can pin up to four messages to the top of a chat.」（faq_294619079641794） | 「If three messages are already pinned when a new pin request is made, the oldest pinned message will be automatically unpinned.」（whatsapp_groups_groups_messaging_md） | 消费者端与接口端不同 | 附表 B 两个数各占一行、各带自己的载体：界面端记 4 条上限，接口端记 3 条自动解顶阈值，不合并成「3～4 条」，也不以其中一方改写另一方。 |
| F2   | 接口侧顶置是"一次一条"还是"多条并存" | 「When calling the API, only one message can be pinned at a time.」（whatsapp_groups_groups_messaging_md） | 「The display order of the pinned messages is based on the chronological order of parent messages, newest first.」（whatsapp_groups_groups_messaging_md） | 同页两处写法不一 | 同一页面先说一次只发起一条顶置请求，又说已在顶的消息有多条并规定其排序，本篇按"一次发起一条、同时在顶最多三条"两句各引一次，不把它读成"最多只能存在一条"。 |
| F3   | 经邀请链接入群者能否看到历史消息 | 「New members who joined the group through a group invite link or QR code won’t be able to view message history.」（faq_1139252413769848） | 「You can send message history to new group members after they join including members who joined via an invite link.」（faq_1062135416113130） | 两页结论相反 | 两句分别绑定各自问题：前者答"新成员自动看得到吗"（看不到），后者答"管理员事后能不能发给他"（能）。本篇并列不取舍，也不把任一句写成通则。 |
| F4   | 历史消息的条数与时窗 | 「When you’re added to a group, the member who added you may choose to send message history to you. They can send up to 100 messages from the last 14 days.」（faq_1139252413769848） | 「When you add a new member to a group, you may have the option to send message history to them. You can send up to 100 messages from the last 14 days.」（faq_841426356990637） | 两处同值互证 | 两页从被加者视角与添加者视角各写一次，100 条／14 天两个数完全一致；附表 B 仍按一个数字登记一次，出处页记两页。 |
| F5   | 33 人这条线按什么时点判定 | 「If a group was started with fewer than 33 members, all group members can: Add others to the group Send invite links.」（faq_526742385997912） | 「Admins and members in groups with fewer than 33 members can create group links to invite new members to a group.」（faq_3242937609289432） | 起点口径与当前口径 | 甲句以"建群时的人数"为条件，乙句只说"群里少于 33 人"这一当前状态。本篇两种基准都写出并标明各自出处页，不推断哪一个生效，也不把两者平均成一条规则。 |
| F6   | 谁默认可以发言与改群信息 | 「By default, any group member can send messages or change the group name, icon, description. However, a group admin can change group settings to allow only admins to edit group info or add new members.」（faq_493270532852273） | 「If a group has more than 256 members, Edit group settings will default to Only admins.」（faq_526742385997912） | 默认范围与更大范围并存 | 甲句是通用默认值（全员可），乙句是群规模超过 256 人时的默认值（仅管理员可）。本篇按"默认值随群规模分岔"叙述，两个默认档都保留，不写成"全员可发言"一条通则。 |
| F7   | 公告组成员总数对谁可见 | 「Only the admin can see the total number of members in the Announcement group.」（faq_742237357891080） | 「Everyone can see the total number of members in the announcement group in the member list.」（faq_582420703681043） | 两页结论相反 | 同一对象（公告组成员总数）在两页给了相反的可见范围。本篇两句逐字并陈并各标出处页，不判定孰新孰旧，也不写成"管理员可见、成员只见总数"这种两句都没说的折中。 |
| F8   | 社区 2,000 人计入哪些成员 | 「You can have up to 2,000 members in new and existing communities. This limit includes the total number of members in the sub-groups and announcement groups within the community.」（faq_438859978317289） | 「You can add up to 2,000 members to a community and its announcement group.」（faq_582420703681043） | 枚举成分不同 | 甲页把上限拆成"分组＋公告组"两类成分，乙页只写"社区及其公告组"。两页都没有把一般讨论组列进枚举，本篇按"枚举不同"登记，不替官方补出缺失的那一类。 |
| F9   | 1,024 这个数字挂在谁身上 | 「Like other groups, the community general chat limit is 1,024 members.」（faq_1418342145436771） | 「Announcements groups support reactions and poll votes for up to 1,024 members. This is a separate limit from the total number of members a community can have.」（faq_1859295147860069） | 对象不同易混 | 甲句的 1,024 是人数上限，乙句的 1,024 是"表情回应与投票"的人数门槛且官方自己注明与社区人数上限无关。附表 B 同值两行分别绑定对象，不并成一行。 |
| F10  | 群规模上限是否把创建者算在内 | 「The maximum group size is 1,024 members (1,025 including the group creator).」（faq_967457667545238） | 「A WhatsApp group can have up to 1,024 members.」（faq_7179561392143247） | 计数是否含本人 | 甲页单列"含创建者 1,025"这一档，乙页只给 1,024。本篇以甲页为准写"成员 1,024、含创建者 1,025"，乙页只作旁证，不把两个数写成区间。 |
| F11  | 能不能用接口直接把人加进群 | 「Since you cannot manually add participants to the group, simply send a message with your invite link to WhatsApp users who you would like to join the group.」（whatsapp_groups_reference_md） | 「Array of phone numbers or WhatsApp IDs to remove (max 8 participants)」（whatsapp_reference_groups_groups_participants_api_md） | 接口页与指南页不同 | 指南页说不能手动加人、只能发邀请链接；参考页的添加端点存在，但它的 participants 参数说明照抄了删除端点的"to remove"。本篇据此写明"接口层存在添加端点、官方自己的描述文字与指南页不一致"，不据此推荐任何一种加人路径。 |
| F12  | 加入群组这件事的官方定性 | 「Groups are an invite-only experience where participants join using a group invite link you send them.」（whatsapp_groups_md） | 「Use the contents of the webhook or API response to approve or reject requests.」（whatsapp_groups_reference_md） | 两页同题各说一半 | 甲句把群组定性为"仅邀请"，乙句所在的接口页把"申请加入"当成一条已在文档里成立的通路并给出审批动作。本篇两句各引各的，甲用于说明入口模型、乙用于说明审批接口，不合成一句"群组只支持邀请制"。 |
| F13  | 管理员数量在群组与频道之间 | 「A group can have an unlimited number of admins, and any admin can make a member an admin.」（faq_633713745095781） | 「You can invite up to 16 additional channel admins to help you manage your channel.」（faq_794229125227200） | 载体间口径不同 | 群组写明"无限"，频道写明"额外 16 名"。附表 B 两个数分属两个载体，社区管理员数量则记入下面的未写明项，不做跨载体类推。 |
| F14  | 广播列表数量与每月广播条数 | 「You can create as many broadcast lists as you like.」（faq_861663048350950） | 「You can send up to 35 free broadcast messages per month.」（faq_1176325363861991） | 对象不同易混 | 一句管"能建几张列表"、一句管"每月能发几条"，两数不相乘也不互相封顶。本篇把两句放在不同小节，并明确写出各自计量的对象。 |
| F15  | 频道订阅的开放范围 | 「We’re rolling out channel subscriptions gradually, so it may not be available to you yet. We’re working to make it available to more people.」（faq_1251246649902787） | 「We’re rolling out channel subscriptions gradually to a select group of channels, so it may not be available to you yet.」（faq_1371180700817474） | 枚举成分不同 | 乙页多出"to a select group of channels"这一限定，甲页只说逐步开放。本篇按更窄的那句写开放范围，并注明另一页没有该限定，不写成全量可用。 |
| F16  | "free"有没有对应的更高档 | 「You can send up to 35 free broadcast messages per month.」（faq_1176325363861991） | 官方未写明（—） | 限定词未展开 | 官方在数字前写了 free，但没有在同篇给出更高档的条数、价格或获取方式。本篇只记"35 条、且原文带 free 限定"这一事实，不推断存在付费档，也不给出提升路径。 |
| F17  | 消失消息的默认档与可选档 | 「The default message timer for disappearing messages is turned off.」（faq_3307102709559968） | 「When you enable disappearing messages, you can choose to have messages disappear after 24 hours, 7 days, or 90 days, unless the messages are kept.」（faq_515705483748188） | 默认值与可选值并记 | 默认是关闭，可选是三个时长。本篇先写默认再写可选档，不把"24 小时"当默认值，也不省略 unless 从句里的"保留消息"例外。 |
| F18  | 同一页里"谁能开关消失消息"的两种写法 | 「In a group chat, any group member can enable disappearing messages. However, a group admin can change group settings to allow only admins to enable disappearing messages.」（faq_515705483748188） | 「You can set messages to disappear 24 hours, 7 days, or 90 days after they’re sent unless that message is kept. The duration you choose only affects new messages in the chat, not messages you’ve already sent or received.」（faq_673193694148537） | 同页两处写法不一 | 两页对"谁能开关"与"所选时长影响哪些消息"分别给出写法，且例外从句一说是 kept 消息、一说是已发送的消息。本篇按各页原句分开引用，不改写成一条统一规则。 |
| F19  | 离开后重新加入的等待上限 | 「Every time you leave a group, the amount of time you must wait to be re-added by the group admin increases. The maximum amount of time you’d have to wait would be 81 days.」（faq_420332980275753） | 「Every time you leave a community, this wait time increases. The most you’d need to wait is 81 days.」（faq_651003086597307） | 两处同值互证 | 81 天同值，但甲页给出递增机制、乙页只称"该等待时长"并直接给结论。附表 B 按一个数字登记一次、出处页记两页，正文分别说明两个载体的递增起点。 |
| F20  | 社区管理员的人数上限 | 「To limit noise and overload, only community admins can send announcements to all community members.」（faq_458610306367976） | 官方未写明（—） | 只给机制不给数值 | 官方写明"只有社区管理员能发公告"却没写社区管理员可设几名。本篇只记权限、把人数标为未写明，不从群组的"无限"或频道的"16 名"外推。 |
| F21  | 一个账号能建几个社区 | 「If you deactivate your WhatsApp account, communities and groups you created will also be deactivated.」（faq_1312647189536807） | 官方未写明（—） | 只给机制不给数值 | 官方只说"你创建的社区"会随账号注销一并停用，未给每人可创建的社区数量。本篇把创建者概念写实、数量留白。 |
| F22  | 一个社区能装几个分组 | 「Only community or group admins can remove a group from a community.」（faq_970281987938470） | 官方未写明（—） | 只给机制不给数值 | 官方写明谁有权把分组移出社区，却未写一个社区最多可挂几个分组；人数上限 2,000 也不能换算成分组数。本篇只记解除链接的权限层级，分组数量留白。 |
| F23  | 频道功能与创建数量的可用范围 | 「Where channels is available, we’re rolling out new features gradually. Some features, like being able to create a channel, might not be available to you yet.」（faq_809740894165112） | 官方未写明（—） | 官方自陈灰度、范围未公开 | 官方自己写明按地区与功能灰度、且没给出地区名单或每人可建频道数。本篇把可用性写成"依官方原句的条件句"，不列国家清单、不写开通步骤。 |
| F24  | 群组操作限流的触发数值 | 「Group operation failed because there were too many group operations from this phone number in a short period.」（whatsapp_groups_error_codes_md） | 官方未写明（—） | 只给机制不给数值 | 错误码页只描述"短时间内操作过多"，不给条数、窗口长度或恢复时间。本篇只引这一句说明限流的存在与报错形态，不折算成任何每秒／每小时阈值。 |
| F25  | 治理规则覆盖哪几类载体 | 「These Messaging Guidelines (these “Guidelines”) apply to chats, calls, group chats, status updates, and communities.」（legal_messaging_guidelines） | 官方未写明（—） | 一处存在一处缺失 | 准则页把 communities 明确列入适用范围，而本篇登记的开发者文档侧没有社区专章。本篇只登记这一覆盖差，不据此推断社区接口的存在或不存在。 |
| F26  | 频道内容治理义务的表述层级 | 「Channel admins should be respectful of their followers and avoid sending too many or low-quality updates, which could lead recipients to unfollow their channel.」（legal_channels_guidelines） | 「As a channel admin, you’re responsible for keeping the content on your channel safe, relevant, and age-appropriate for your followers.」（faq_671443411431514） | 文档层级不同 | 准则页用 should 给出行为期待、帮助中心用 responsible for 给出责任归属，两句主体同为频道管理员但约束强度不同。本篇按"准则层／帮助层"分层引用，不把其中一句写成另一句的结论。 |
| F27  | 不可引用页 faq_1351864989275592 | 「本篇实测取回状态：正文未取回（0 字符）」（—） | 「页面标题：About channel subscriptions」（—） | 本篇实测：正文未取回 | 该页在本篇抓取批次里只返回单页应用外壳或地域限定提示，按红线全篇不引用；本篇不为它补写任何规则，也不引用第三方对同题页面的转述。 |
| F28  | 不可引用页 faq_245599461477281 | 「本篇实测取回状态：正文未取回（95 字符）」（—） | 「页面标题：WhatsApp Channels Guidelines」（—） | 本篇实测：正文未取回 | 该页在本篇抓取批次里只返回单页应用外壳或地域限定提示，按红线全篇不引用；本篇不为它补写任何规则，也不引用第三方对同题页面的转述。 |
| F29  | 不可引用页 faq_813798063285846 | 「本篇实测取回状态：正文未取回（113 字符）」（—） | 「页面标题：How to add or suggest a group to a community」（—） | 本篇实测：正文未取回 | 该页在本篇抓取批次里只返回单页应用外壳或地域限定提示，按红线全篇不引用；本篇不为它补写任何规则，也不引用第三方对同题页面的转述。 |
| F30  | 不可引用页 faq_894737126459640 | 「本篇实测取回状态：正文仅显示地域限定提示（70 字符）」（—） | 「页面标题：What is defamation and how do I report it on Channels?」（—） | 本篇实测：正文未取回 | 该页在本篇抓取批次里只返回单页应用外壳或地域限定提示，按红线全篇不引用；本篇不为它补写任何规则，也不引用第三方对同题页面的转述。 |
| **合计** | **30** 项 | 逐字核验 45 格 | 留白 7 格／未取回页 4 行 | 性质 19 类 | 原句最长 219 字符／截断 0 格／整格 1 格 |

#ROWS=30 COLS=6

---

**边界声明：** 本篇是**官方规则文本与官方接口文档的口径梳理**，不是操作手册，也不是对平台规则合理性的评价。全篇内容限于 WhatsApp 与 Meta 公开页面已经写出的表述，共 126 个页面登记位：WhatsApp 帮助中心 **104** 页（群组、社区、频道、广播列表四条产品线的安全与治理页）、Meta 开发者文档 **18** 页（Groups API 侧 8 页：主页、get started、messaging、reference、faq、pricing、webhooks、error codes；Groups API reference 下 5 页：groups 管理、invite link、join requests、participants、query；另加 Webhook reference 的 messages/group 页 1 页、opt-in 规则 1 页、商业资料 1 页、政策执法 1 页、数据与隐私 1 页）、法律与政策页 **4** 页（服务条款、消息指南、Channels Guidelines、Channels Terms of Service）。引用这些文本的目的只有两个：说明平台把**谁能建群、谁能进、谁能说、谁能改、谁能移人、谁看得见名单、哪些行为固定不需要开关**写成了哪些控件名、哪些默认值与哪些条件；说明同一批文本把四类载体的**规模上限、互动门槛、留存窗口与失败码**写成了哪些数字与哪些档位。本篇**不含**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、设备农场、注册与验证流程的操作步骤、限速或频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号的抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法建议；**不含**任何可用于向非自愿收件人大量投递消息的方法；**不含**利用代码、恶意样本或利用链构造方式；**不含**任何针对他人设备或他人账号的操作步骤与时序。Groups API 一节只登记端点名称、HTTP 方法、字段名、取值域、限制条数与失败码，不重述任何一次调用怎么做。官方写成区间或多档的数字（`24 hours／7 days／90 days`、`8 hours／1 week／Always`、`1～1024（默认 25）`、`100 条／14 天`）按整段一行登记在附表 B，不合成单值；官方写成 `up to`、`about`、`might`、`in some countries`、`rolling out` 之处照原样保留限定词，不推测阈值；同一数字在两个体系里各写一处（`1,024` 同时是群组成员上限、社区总群上限与公告组互动失效门槛；`100` 同时是名称长度、数据看板门槛与热门地区触达数）按体系分列，不做跨体系合并。四类载体之间、消费者端开关与 Groups API 字段之间、帮助中心页与法律页之间，官方从未给出对齐表：本篇在附表 E 按同一组维度并排四类载体，官方没有按载体写某一项的格子一律标作「官方未按载体区分」，该标注**只说明官方文本的写法，不说明该载体没有该项规则**；在附表 C 把「官方是否写明」限成五类，在附表 F 把两处官方写法不一致的项逐条对撞，凡官方留白处一律登记为留白，不推断、不补齐。本次未能取回正文或只取回地域限定提示的 **4** 个页面在附表 A 逐行标注「全篇不引用」，其内容未被本篇任何一处引用，本篇也不为它们补写规则。

Telegram: https://t.me/one00190
