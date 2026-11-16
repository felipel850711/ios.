@@TITLE@@WhatsApp 举报、屏蔽与骚扰处置在商户侧和用户侧的官方口径解读
@@SUBTITLE@@本篇是"WhatsApp 群发／群控系统性口径"系列第二轮"身份与信任五轴"的第五篇（轴⑤，编号 n=10），讲的是：官方文本把用户侧的屏蔽、举报与退出写成了哪几种对象、屏蔽的后果落在消息、通话、状态更新与号码名单哪几层、一次举报把哪些材料送到平台、平台收到材料后按什么顺序处置、"骚扰"在政策层被定义成什么、模板级与账号级的暂停各以什么单位计时、申诉入口开在谁那一侧、业务侧的屏蔽接口与退订信号各是什么语义、以及法律文本为这些动作写下了哪些动词与时限。全篇只读官方文本，共 **63** 个页面登记位（**61** 页正文取回、**2** 页正文未取回因而全篇不引用，逐页归属与引用位置见附表 A 的 **64** 行含合计）：Meta 开发者文档 **22** 页、WhatsApp 帮助中心 **35** 页、法律与政策页 **6** 页。
@@INFO@@报告日期|2026 年 11 月 16 日
@@INFO@@分析对象|按站点清点本篇登记的页面构成：Meta 开发者文档 **22** 页（屏蔽用户接口与号码级屏蔽参考页、消息错误与账号状态一组 webhook、用户偏好与退订信号、营销模板与按人计的营销上限、模板质量与暂停、账号告警与账号更新、Direct Send 完整性、通话设置、商品目录总览）、WhatsApp 帮助中心 **35** 页（屏蔽与举报、退出群组与社区、可疑消息与可疑链接、屏蔽高量未知消息、群与社区的安全页、家长管理账号、商业 App 侧的广播与营销消息、手机号可见性与用户名、信誉与隐私入口、印度用户安全页）、法律与政策页 **6** 页（社区标准执行报告、商业消息政策、消息指南、用户条款、Business 条款、WhatsApp Business 服务条款）。合计 **63** 个登记位，其中 **2** 页正文未取回、全篇不引用；归组依据是官方页面标题，逐页归属见附表 A。
@@INFO@@视角定位|官方规则文本、帮助中心控件说明与开发者文档的**口径解读**，不是操作手册。本篇**不提供**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法建议；本篇同样**不写**任何可用于向非自愿收件人大量投递消息的方法——包括"怎样让举报不成立""怎样让屏蔽不触发""怎样在被限制后继续发"这一类问法；官方把屏蔽写成后果句、把举报写成材料提交、把处置写成动词与计时，本篇只登记这些句子出现在哪一页、哪一句（界线见 1.4）。
@@INFO@@分类|**官方页面登记 63 个登记位 × 3 类站点 × 引用位置 × 取回状态**（附表 A，**64** 行含合计；Meta 开发者文档 **22**、WhatsApp 帮助中心 **35**、法律与政策页 **6**；可引用 **61** 页、不可引用 **2** 页；原句池 **1,194** 条整句、覆盖 **58** 页）× **数字归档 47 行，五档 7／3／24／4／9，去重 37 组数字＋单位、21 种计数单位、11 种性质，覆盖 19 页，整句截断 33 行，池内不含数字的整句 1,100 句、按单位词表定不出档位而被丢弃的数字实例 33 个、在池子里一句都没有的可引用页面 3 个**（附表 B，**48** 行含合计）× **官方用词 × 站点类别矩阵 82 个词（组）分属 13 个主题组 × 4 类站点、非空格 176 个、句次 2119、去重命中句 925（占全池 77.5%）、一句最多同时落进 10 格、落进两格以上的句子 578 句、零命中词 8 个、代表句截断 107 处、逐字回表失败 0 句**（附表 C，**177** 行含合计）× **官方写明的取值域 18 组、取值 92 个、去重取值名 74 个、覆盖 5 页，配置的 10 串标记本轮扫到 3 串、7 串零命中，另有 4 处写了标记却取不出取值域、分布在 4 页**（附表 D，**19** 行含合计）× **屏蔽、举报与处置三段在各主体上的逐列对照 15 个维度 × 9 列 = 135 格、有句 68 格／空格 67 格、去重引用页 28、同一句跨列复用 4 次、截断 34 格**（附表 E，**16** 行含合计）× **口径冲突与官方未写明 21 个议题、两侧有句 19／单侧 0／两侧皆无 2、差异性质 21 种、去重引用页 17、复用 0 次、正文落点已核 18 行 36 句、截断 11 处**（附表 F，**22** 行含合计）× **正文与附表的官方原句逐字回核，失败 0 条**。
@@INFO@@难度|进阶（本篇有五处容易读错。**第一是屏蔽、举报与退出是三个动作，官方没有一页把它们写成一条原子流程**：屏蔽切的是接收（消息、通话与状态更新），退群结束的是成员关系、不顺带屏蔽任何人，举报只是把材料提交给平台且官方没有承诺处理结果；把三者读成"屏蔽即举报加退出"会得出官方没写过的承诺（2.1 与 2.4，附表 E 的 E1 至 E3）。**第二是"骚扰"在政策层是内容定义，不是频次定义**：消息指南给 harassment 的写法落在"针对个人或群体"那一类内容上，而"频繁发送"这件事在限额与模板质量两层另算；把"发得多"直接读成"骚扰"会把两层规则混成一条（13.1 与第 11 章，相关议题见附表 F）。**第三是模板级暂停与账号级暂停不是同一把尺**：模板暂停按小时与天计并带申诉窗，账号级营销暂停按天计且到期自动恢复；两者恢复要不要申请、以谁为对象都不同（10.2 与 11.2）。**第四是"屏蔽"有两个方向**：用户屏蔽业务会把号码从业务的联系人名单里移除，业务屏蔽用户走的是接口返回错误的语义，两者后果不是镜像关系（2.1、8.2 与 7.1、7.3）。**第五是"两页没取回"与"三页零句"不是同一件事**：前者全篇不引用、凡规则按"官方未在本篇语料里写明"处理；后者正文取回了，只是参数表、错误码表与短问答进不了句子层，这三页在附表 B 至 F 里都不会出现，只在附表 A 各留一行（1.3、7.2、7.4）。）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

本篇是"WhatsApp 群发／群控系统性口径"系列第二轮"身份与信任五轴"的第五篇（轴⑤，编号 n=10），主题是**举报、屏蔽与骚扰处置**：用户侧的三个动作（屏蔽、举报、退出）在官方文本里各被写成了什么、屏蔽的后果落在哪几层、举报会把哪些东西送到平台、平台收到之后按什么顺序处置、业务侧能看到哪些信号、业务侧屏蔽用户与用户屏蔽业务为什么不是同一件事、模板级与账号级的处置各以什么单位计时、申诉入口开在谁那一侧、以及"骚扰"这个词在政策层被定义成什么。全篇只读官方文本，共 **63** 个页面登记位：Meta 开发者文档 **22** 页、WhatsApp 帮助中心 **33** 页（另有两页正文未取回，逐页点名的登记位合计 **63**）、法律与政策页 **6** 页；其中 **2** 页正文未取回，本篇全篇不引用它们，凡可能落在它们身上的规则一律按"官方未在本篇语料里写明"处理。

可引用的 **61** 页里有 **58** 页进入了原句池，池共 **1,194** 句；另有 **3** 页正文取回但整页是参数表或纯前端渲染页，池里一句都没有（第 1.3 节逐页点名）。本篇的每个官方数字都能在附表 B 的 **47** 个数字条目里找到原句与页标识（按五档归档、去重后 **37** 组「数字＋单位」、单位词 **21** 种、性质 **11** 种、覆盖 **19** 页）；用词归属见附表 C 的 **82** 个词（组）× 站点类别矩阵（分属 **13** 个主题组，非空格命中句次 **2119**，去重命中句 **925** 句，占全池 **77.5%**）；取值域见附表 D 的 **18** 组共 **92** 个取值（去重 **74** 个，覆盖 **5** 页）。

本篇的读法是**沿一条处置链往上走**：先是用户按下的那个动作（第 2 章），再是动作送出去的材料与业务侧的接口信号（第 4、7、8 章），然后是平台在模板、账号与政策三层各自的处置动词与计时（第 9—12 章），最后是写这些动词的法律与政策文本（第 13—14 章）。这条链在官方文本里没有任何一页画出来：帮助中心写"按下之后你会看到什么"，开发者文档写"接口会返回什么"，政策页写"业务必须遵守什么"，三者之间没有对照表。本篇把三条线并排登记在附表 E 的 **15** 个对照维度 × **9** 列 = **135** 格（**68** 格取到官方原句、**67** 格标作"官方未按这一列写该项"），并把成对出现的口径差异登记在附表 F 的 **21** 个议题里（两侧有句 **19**、两侧皆无 **2**）。

下面 **52** 个问题覆盖第 1—17 章的各级小节，一个问题可以跨相邻两小节，一节也可以由两个问题分别追问；跨小节的关联只做中文指认，不把两页原句拼成一句。

1. 四层写作者各自把"屏蔽、举报、退出"写成什么，为什么政策层的动词不能当界面承诺读？（1.1）
2. 本篇固定了哪些中文口径词，每个词对应哪个英文原词？（1.2）
3. 两页未取回、三页零句、一页只有一句，这三类登记位在附表里分别怎么落？（1.3）
4. 本篇不写什么，官方写成"步骤"的内容登记到什么颗粒度？（1.4）
5. 举报和屏蔽为什么不是一件事，屏蔽流程里的举报为什么是勾选项？（2.1）
6. 举报个人时平台收到几条消息、哪些元数据，转发历史之后被举报的是谁？（2.1）
7. 举报会不会一定有结果，官方在哪一句里写了"不必然"？（2.1）
8. 举报之后有没有回执，能不能催办，处理完会不会通知？（2.1）
9. 屏蔽的即时效果有几句写法，屏蔽名单与联系人是同一张表吗？（2.1、2.2）
10. 屏蔽之后对方还能在共同群组里看到你吗，状态更新按什么时间切开？（2.1、2.2）
11. 被屏蔽的一方能看到哪些迹象，官方为什么同时说"无法告知"？（2.3）
12. 群管理员能不能被单独屏蔽，退群与屏蔽是不是两个动作？（2.4）
13. 家长管理账号把屏蔽与举报的权限切在哪一层，家长收到的是什么通知？（2.5）
14. 举报用户名、贴纸、频道、状态、广告、商品各走哪一页，页面之间共享哪一句？（第 3 章）
15. 用户名举报的特殊性在哪，相似用户名算不算同一件事？（3.1）
16. 贴纸举报的后果为什么写成"可能不处置"，频道举报为什么有可查状态？（3.2）
17. 广告与状态里的举报入口把哪些对象合在一起，举报网站与举报广告是不是同一件事？（3.3）
18. 用户被业务屏蔽与用户屏蔽业务，方向与后果各是什么？（3.4）
19. 平台到底从一次举报里收到什么，哪一页专门回答这个问题？（4.1）
20. 商业消息的加密状态在什么条件下会变，变之前用户能不能看到？（4.2）
21. 商业消息的留存期与"平台会不会拿它做广告"各写在哪一句？（4.3）
22. 官方把"可疑消息"写成哪些征兆，用户被建议做哪几个动作？（5.1、5.2）
23. 可疑链接的提醒靠什么触发，官方为什么不承诺链接一定安全？（5.3）
24. "屏蔽高量未知消息"屏蔽的是哪一类、什么时候停？（5.4）
25. 群里的三种举报对象各管什么，群本身能不能被屏蔽？（6.1）
26. 群管理员的工具与成员的工具有什么不同，退出社区为什么可以不出声？（6.2）
27. 业务侧屏蔽用户的接口在语义上是什么，屏蔽成功与失败的粒度是多少？（7.1）
28. 业务侧屏蔽接口的参考页为什么在本篇没有句子可引？（7.2）
29. 屏蔽相关的错误码把"限制"与"封禁"写成了什么关系？（7.3）
30. 消息错误 webhook 页为什么不进句子层，它的取值去了哪里？（7.4）
31. 退订信号在接口层只有哪两个取值，谁收得到？（8.1）
32. opt-in 专页为什么把"用户也可以屏蔽或举报"写进取得同意的那一页？（8.2）
33. 用户屏蔽业务之后，业务侧的号码名单发生了什么变化？（8.2）
34. 营销消息的计费与"送达"是什么关系，阅读数据为什么天然不完整？（9.1、9.2）
35. 带 offers 与 announcements 的商业会话，官方用什么理由收集哪些信息？（9.3）
36. 按人计的营销上限由谁决定，重试窗口为什么也写成一条限制？（9.4）
37. 营销消息在发出前被谁审核，审核者能不能看到受众信息？（10.1）
38. 账号级营销暂停以什么单位计时，恢复要不要申请？（10.2）
39. 模板层面有三个状态词，它们的关系与恢复条件各是什么？（10.3、10.4）
40. 模板质量四档与号码质量档位分别在哪两页，二者是不是同一套评分？（11.1—11.4）
41. 模板暂停的三档时长与申诉时限各写在哪一句？（11.2）
42. 账号级执法的动词有哪几个，申诉的一般规则与例外怎么写？（12.1）
43. 违规清单页为什么只留下几句可引用的话？（12.2）
44. 平台检测违规靠的是内容还是行为信号，业务能不能看到细节？（12.3）
45. 账号封禁申诉由谁受理，帮助中心为什么把用户推给商业帮助中心？（12.4）
46. "批量与自动化"在官方文本里被定性成什么，法律行动从哪一天起算？（12.5）
47. 消息指南给"骚扰"下的定义是什么，自动化与人工怎么分工？（13.1）
48. 商业政策把"尊重屏蔽与退订请求"写成谁的义务，兑现到什么程度？（13.2）
49. 用户条款与商业条款在本轴各能引几句，为什么商业条款几乎引不出来？（13.3）
50. 透明度报告的数字覆盖哪些产品，为什么不能当 WhatsApp 举报数据看？（13.4）
51. 安全页、负责任使用页、印度页与隐私页各补了哪一块？（第 14 章）
52. 限额、告警、账号更新与 Direct Send 完整性这四组接口文本，把处置写成了哪些字段？（第 15 章）

一句话读法：每条结论都跟着一个页名走——先给页名，再整句引出该页英文原句，最后用中文说这一条覆盖哪一类主体、官方有没有同时写明条件与时限；要复核就按页名回附表 A 找登记位，再按附表 B／C／E／F 标注的句序回到抓取件。

# 1 来源层、三个动作的名字与范围红线

本篇要处理的对象在官方文本里散落在四个完全不同的写作者手里：面向普通用户的帮助中心、面向业务开发者的接口文档、约束业务行为的政策页，以及发布处置数字的透明度报告。同一个"屏蔽"，在这四层里分别是"你按下之后会发生什么""接口会返回什么""你必须尊重什么""我们统计了什么"。第 1 章先把这四层分开，再把本篇使用的中文口径词固定下来，最后交代三类特殊登记位与本篇的范围红线。

## 1.1 四层来源与"三个动作"的各层写法

帮助中心那一层的写法是**后果句**：它不定义"屏蔽"是什么，只写按下去之后你不再收到什么。这一层的总纲句是 `Blocking and reporting problematic users helps keep WhatsApp safe.`——屏蔽与举报被并列写成为了让平台更安全而做的事，而不是一项权利清单。同一页随后把三个动作拆开：屏蔽是切断接收（2.1），举报是提交材料（2.1），退出是把会话关系本身结束（2.4、6.1）。

开发者文档那一层写的是**接口语义**：《Block users》全篇没有一句解释"为什么屏蔽"，它只写尝试向被屏蔽用户发消息会返回错误（7.1）。政策层写的是**义务**：《Business Policy》把"尊重屏蔽、停止与退订请求"写成业务必须履行的动作，并且把"从联系人名单里移除"写进同一句（13.2）。透明度报告那一层写的是**分母与比率**：《Community Standards Enforcement Report》只发布 Facebook 与 Instagram 的执法精度与违规内容占比（13.4），WhatsApp 的举报数据不在这份报告里。

这四层之间有没有对齐句？本篇逐页找过，没有。唯一一处跨层写法出现在《WhatsApp Messaging Guidelines》，它把"用户可以举报哪些对象"写成一句清单：`Users can report WhatsApp contacts, groups, communities, status updates, or specific messages that potentially violate these Guidelines.`。这一句是本篇能引到的、唯一把"联系人、群组、社区、状态更新、具体消息"五类对象放在同一句里的官方表述；帮助中心侧把这五类拆在五页上写（第 2、3、6 章），两者不是同一套切分。

## 1.2 中文口径词表与各自的原文词

本篇固定使用十个中文口径词，每个都对应官方文本里的一个英文原词，不混用：

**屏蔽**对应 `block`，在本篇只指"接收侧的切断"；**解除屏蔽**对应 `unblock`；**举报**对应 `report`，只指"把材料提交给平台"这个方向；**退出**对应 `exit` 与 `leave`，指结束群组或社区关系；**退订**对应 `stop` 与 `opt out`，只指营销消息这一类；**处置**对应 enforcement 一类的动作集合；**限制**对应 restriction；**暂停**对应 pause；**封禁**对应 ban；**申诉**对应 appeal 与 acknowledge。这张表不是翻译偏好，而是因为官方在这十个词上各写了一套后果，混用会把"屏蔽"读成"退订"、把"暂停"读成"封禁"（附表 C 的主题组 A、D、H 分别收录这三组词的命中格）。

附表 C 的 **82** 个词（组）分属 **13** 个主题组，覆盖三个能取到句子的站点类别（Meta 开发者文档 **488** 句次、WhatsApp 帮助中心 **1210** 句次、法律与政策页 **421** 句次；Meta 商业帮助中心在本篇没有登记页，所以那一列没有命中）。其中 **8** 个词在整池一句都没出现，本篇按零命中登记而不改词表：`automatic review（自动审核）`、`report it to us（要用户上报的说法）`、`block API（接口名）`、`block rate／report rate（屏蔽率与举报率）`、`threshold（阈值）`、`tier（档位）`、`harassment（名词形态）`、`reporter identity（举报人身份是否告知被举报方）`。零命中不等于官方没有这一项内容——例如"举报人身份"这件事在正文里是用否定式写的（2.3、3.2），只是没有用这个名词短语。

## 1.3 未取回页、零句页与只有一句的条款页

三类特殊登记位要在读表之前交代清楚，免得读者把"登记了"读成"引用了"。

**第一类，两页正文未取回**：《Page not found》与《Page not found》两页的抓取件都是空正文，页面标题在抓取件里就写着 Page not found。这两页在本篇的登记位保留（附表 A 写着"全篇不引用"），正文不出现任何来自它们的句子；凡可能落在它们身上的规则，本篇一律按"官方未在本篇语料里写明"处理，而不是按"官方没写"处理——这两句在本篇里不是同一个意思。

**第二类，三页正文取回但池内零句**：《What information does WhatsApp collect when reporting an issue》（4.1）、《Base URL》（7.2）与《Errors messages webhooks reference》（7.4）。前两页是参数表与问答短文，后一页是错误码表；这些页面上的事实要么写在表格 markup 里、要么写在列表行里，按建池口径（含竖线、以列表符号起手、长度越界的段不入池）整页都进不了句子层。这三页在其余五张附表里都不会出现，只在附表 A 各留一行登记。D 表收录的是另外五页的参数取值（账号更新、账号告警、用户偏好与两页质量更新 webhook），这三页的取值不在其中——它们的枚举值只写在参数表里，建池时随表格一起剔除，本篇因此不声称掌握这三页的取值。

**第三类，只有一句可引的条款页**：《WhatsApp Business Terms of Service》整页入池只有一句，而那一句是站点横幅，与另三页政策页池内的横幅重复。本篇在 13.3 只登记"这一页被多处引用为上位条款"与"它的正文没有进入本篇句子层"两件事，绝不代它拟写条款。

## 1.4 本篇不做的事与范围红线

本篇是官方文本的口径梳理，不是操作手册，也不是对平台规则合理性的评价。**不含**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法；**不含**任何可用于向非自愿收件人大量投递消息的方法——"怎样让举报不成立""怎样让屏蔽不触发""怎样在被限制后继续发"这三类问法在本篇一律不答；**不含**代码、恶意样本或利用链构造方式；**不含**任何针对他人设备或他人账号的操作步骤与时序。

界面页与接口页里官方写成"步骤"的内容——屏蔽入口的层级、举报菜单的勾选项、模板申诉的提交顺序——本篇只登记**控件名与条目数**，不重述点击顺序，也不把清单转写成可执行的动作序列。第 2、3、6 章出现的入口名（Blocked contacts、Report group、Channel reports 一类）都按官方原词点名，读者要动手请回原页，本篇不提供操作路径。

# 2 用户侧：屏蔽、举报与退出

第 2 章处理用户按下的那三个动作。它们在同一批帮助中心页面里被写在一起，却各有一套不同的后果：屏蔽切断接收但保留会话、举报把材料单向送走且不承诺结果、退出结束关系但不等于屏蔽。五节按"个人号主页面—屏蔽名单—被屏蔽方—群管理员—家长管理账号"的顺序展开。

## 2.1 屏蔽与举报的关系，以及举报送出去的材料

《About reporting and blocking on WhatsApp》是本篇在用户侧的主页面，它把两个动作的边界写在相邻两句里。第一句是否定式：`Report a contact, business, unknown user, or group Reporting a user without blocking them won’t stop you from receiving their messages, calls, and status updates.`——只举报不屏蔽，你仍然会收到对方的消息、通话与状态更新。也就是说，在本篇语料里"举报"不改变接收关系，改变接收关系的是"屏蔽"。第二组句子把屏蔽的默认连带写了出来：屏蔽流程里带一个举报勾选项，`Uncheck the box next to Report to WhatsApp if you don’t want to report the person you’re blocking.`；而另一页把长按消息的入口写成二选一，`Users simply need to long press a message to either report or block a user.`。两处并不矛盾，它们描述的是两个不同入口：屏蔽流程默认替你把举报勾上（可取消），长按菜单则让你在 report 与 block 之间选一个。本篇照录两种写法，不合并成"举报与屏蔽总是一起发生"。

举报送出去的材料按对象分档。个人会话的上限是五条：`When you report a user in an individual chat, WhatsApp receives up to five of the last messages they’ve sent to you.`；群组同样是五条，但限定为"发给你的"：`When you report a group, WhatsApp receives up to five of the last messages sent to you in the reported group.`；如果发生通话，还可能附带最近五次通话的基本信息：`If a call took place in an individual chat, WhatsApp may also receive basic info about the last five calls with this user, such as who initiated the call and the duration of the call.`。这三句都用了 up to 或 may，也就是说**条数是上限而不是保证**。除消息之外还有元数据：`WhatsApp receives the reported group or user ID, information on when the message was sent, and the type of message sent (image, video, text, etc.).` 与 `WhatsApp may also receive how the first contact was initiated between you and the reported group or user.`——被举报的群或用户标识、发送时间、消息类型，以及双方第一次接触是怎么发起的。这四类信息在 12.3 的违规检测页里被另外描述一次（以业务为对象），本篇在 12.3 并列，不在这里合并。

转发历史的场景有一条容易读错的规则：`If someone you sent message history to reports content that was sent as part of the message history you sent, you’ll be reported.` 与 `The original sender of the message won't be reported.`。前一句说，如果你把消息历史发给别人、对方举报了这段历史里的内容，被举报的是你；后一句说，消息的原始发送者不会被举报。**官方在这里切的是"谁转发了这段历史"，不是"内容最初由谁写出"**，本篇因此不写"举报会追溯到源头"这类结论。

举报的结果被写成三层否定。第一层是不必然处置：`Please be aware that a user report of conduct in violation of our Terms of Service won't necessarily result in us banning the user or otherwise taking action against the user.`；第二层是平台保留封禁权：`We might ban accounts if we believe the account activity is in violation of our Terms of Service.`；第三层是提交之后不需要你再做什么：`After you’ve submitted a report, you don’t need to take any action or share additional details.`。回执与催办各有专门一句：`We’ll acknowledge receipt of your report, but we will not contact you again.` 承诺"收到"，但同时写明不会再联系你；`Contacting WhatsApp outside of the in-app review process won’t expedite the review process or impact the decision.` 则把"从应用外联系平台"与"加快审查"切开——外联系不加快、也不影响决定。频道举报是个例外：那一页提供了可查状态的入口（3.2 引原句），所以"举报都有回执"这句话在本篇不成立，只有"个人号与用户名的举报只承诺收到即止"成立。

屏蔽本身的后果写在同一页的下一组句子里。切断接收：`Block a contact, business, or unknown user When you block someone, you’ll no longer receive their messages, calls, and status updates.`；对方的可见性：`Your last seen, online, status updates, and any changes made to your profile photo won't be visible to users you've blocked.`；联系人名单不变：`Blocking someone won't remove them from your contacts list, nor will it remove you from the contact list on the blocked user's phone.`；会话保留：`The existing chat will remain in your chatlist.` 与 `You can delete the chat if you want to remove it from your chatlist, or archive the chat to hide it from your chatlist.`；界面标记：`After you block someone: You'll see You blocked this contact.`；解除入口：`Tap to unblock. under the blocked user’s name and in your chat with them.` 与 `You can choose to Unblock or Delete Chat after blocking someone.`。屏蔽名单是单独一张表：`Blocked users are visible in Blocked contacts , which can be found in > Settings > > Contacts .`——它在设置的联系人分组下面，与通讯录不是同一件事。

三条限定句必须与上面的后果句一起读。**其一**，共同群组不受影响：`If they contact you from new numbers, you can block and report each new number. : Blocking someone won’t stop that user from seeing your messages in a group you both participate in, and you’ll see their messages in that group as well.`；**其二**，在线状态按隐私设置分档：`Blocking won’t stop you from being counted as online in a group if your privacy settings are set to Everyone .` 与 `However, a blocked user can’t see if you’re online specifically.`——被屏蔽者看不到你"正在线"这个具体状态，但群里的在线计数仍可能把你算进去；**其三**，状态更新按时间切开：`Users can’t see your status after you’ve blocked them.`、`But, they can see your status if you post it before you block them.` 与 `The status will disappear within 24 hours and you won’t see future status updates from that user.`——屏蔽后发布的状态对方看不到，屏蔽前已发布的仍可见并在 24 小时内消失。这三条合起来是本篇在 2.1 的结论：**屏蔽切断的是一对一的接收，不是"所有可见性"**（附表 F 的 F9、F12 两行按这三句登记）。

同一页最后一段处理高量未知消息与执法场景。前者在本篇按 5.4 单列（它有自己的专页），后者只登记两句：`Involve law enforcement If you feel unsafe or believe a crime has been committed, you can contact your local law enforcement or authorities.` 与 `You can: Take screenshots of the relevant messages, calls, or profile details.`——遇到危险找执法机关、需要留证时截图与备份。本篇引用这两句只为说明"官方把留证写在用户侧"，不延伸成任何取证建议。

## 2.2 屏蔽名单、解除与第三方应用

《How to block and report someone》与 2.1 的主页面高度重叠，但它多写了两块内容：屏蔽名单的生命周期，以及一条与欧洲法规相关的第三方应用通道。

重叠部分按它的写法登记：屏蔽保留会话（`When you block someone, the existing chat will remain in your chatlist.`）、可以退群并单独屏蔽管理员（`Alternatively, you can exit a group and block the group admin.`）、解除后恢复收发（`You can also unblock the sender to receive new messages from them.` 与 `You and the sender will now be able to send and receive messages, calls, and status updates.`）、屏蔽期间发来的消息不会补发（`If you unblock someone, you won't receive any messages, calls, or status updates the sender sent you during the time they were blocked.`）。名单的持久性有一条明确写法：换号不丢屏蔽（`If you change your phone number using the change phone number feature under > Settings > Account > Change number , your blocked contacts will stay blocked.`），但新建账号要重新屏蔽一遍（`If you set up a new WhatsApp account, you’ll need to manually block those contacts again.`）。这两句合起来说明**屏蔽挂在账号而不是号码上**，本篇据此不写"换号即重置"。

群组边界在这里写得更直白：`Blocked contacts or phone numbers will no longer be able to call you or send you messages.`（一对一收发被切断）与 `Blocking a contact won’t stop that user from seeing your messages in a group you both participate in, and you’ll see their messages in that group as well.`（共同群组里仍互相可见）并排。这两句正是附表 F 的 F9 行，本篇在正文并列而不取其一。举报群组时还有两个后果：`If you report a group, you’ll be removed from that group.`（举报群会被移出该群）与 `If you’d like to rejoin a group you reported, the group admin will need to invite you again to the group.`（想回去要管理员重新邀请）。举报条数在这里写成定数：`When you report a group, the last five messages in the chat will be sent to WhatsApp.`——“最后五条”而不是“最多五条”，与 2.1 的上限写法构成 F4 行登记的那处差异。

第三方应用那一段是本篇语料里唯一的跨客户端屏蔽说明。它先写条件：`Chats with third-party apps are only available in select regions and may not be available to you.`（只在部分地区可用）；再写一条负面事实：`Users you’ve previously blocked on WhatsApp might be able to message you through third-party apps.`——**你在 WhatsApp 上屏蔽过的人，可能仍能通过第三方应用给你发消息**。这一句直接限定了 2.1 那句"不再收到"的适用范围，本篇把它登记为"屏蔽的效力边界"，而不是"例外条款"（官方没有用例外这个词）。

## 2.3 被屏蔽的一方能看到什么

《Being blocked by someone》只有寥寥几句，但它把"被屏蔽"写成了两个方向：界面迹象与官方声明。

迹象一侧有三句：`You will still be able to see message sent confirmations (single gray check mark).`（只能看到单灰勾）、`Any messages sent to a contact who has blocked you will only show one gray check mark to show the message was sent.`（发给被屏蔽联系人的消息只显示一个灰勾）与 `Any calls you attempt to place will not go through.`（通话无法接通）。结论句却写得很谨慎：`If you see all the indicators above for a contact, this could mean they’re blocking you.`——所有迹象**可能**意味着对方屏蔽了你；紧接着一句退路：`But they may also be due to other reasons.`——也可能是别的原因。官方声明是这一页的最后一句：`We can’t tell you if someone has blocked you, or who has blocked you.`——平台不会告诉你谁屏蔽了你。

这三句放在一起构成本篇对"屏蔽可见性"的完整口径：**官方不通知，但界面会留下可观察的迹象，而迹象不被承认为结论**。附表 F 的 F11 行把这两侧并列；本篇在正文里同时保留"不告知"与"可观察"，不把任何一侧删掉。

## 2.4 群管理员与退群

《How to block a group admin》只处理一种对象：群管理员。它先确认"群本身不可屏蔽"这件事的替代方案：`However, you can exit the group and block the group admin.`——你可以退出群并屏蔽该管理员。另有一句把入群权限前移到隐私设置：`You can decide who can add you to groups by changing your group privacy settings.`。这一页其余内容是不同入口的控件名（对未存号码的管理员、对已存联系人各一条路径），按范围红线本篇只点名"该页按管理员是否在通讯录里分成两个入口"，不复述点击顺序。

把这一页与 2.2 的 exit-and-block 写法合起来，本篇的结论是：**退出与屏蔽是两个动作，退群不顺带屏蔽任何人，屏蔽管理员也不改变你在群里的成员身份**。官方没有在任何一句里把两者写成原子操作；6.1 页的"群不可屏蔽、成员可逐个屏蔽"是同一件事的另一种写法。

## 2.5 家长管理账号：权限切在联系人与入群请求层

《About messaging and groups on parent-managed accounts》把家长管理的范围写成一句总结：`With a parent-managed account, parents or guardians can choose who can contact their child, decide which groups they can join, and review message requests from unknown contacts.`——家长可以选择谁能联系孩子、决定孩子能进哪些群、审阅陌生联系人的消息请求。三条默认规则把这句落地：`Individual messaging By default, managed accounts can only message with people saved in their contacts.`（默认只能与已存联系人互发）、`If a user with a managed account wants to message someone new, they must first add the person as a contact, which will send an alert to their parent.`（新增联系人会触发家长提醒）与 `If a managed account receives a message from someone not saved in their contacts, the message will be sent to a Requests folder for parent approval.`（陌生人的消息进 Requests 文件夹等家长批准）。群组侧同理：`Group messaging By default, only parents can add a managed account to groups, and managed accounts are not able to create groups themselves.`（默认只有家长能加群）、`If a managed account receives a group invite, a parent will need to review and accept it using their parent PIN on the child’s device.`（入群邀请要家长用 PIN 审阅）与 `Parents will receive alerts for group invites and when groups their child is already in grows in size.`（家长会收到群邀请与群规模变化的提醒）。

权限的边界有两句关键的：`Only parents are able to change contacts settings for managed accounts, learn more in this article .` 与 `Only parents are able to change groups settings for managed accounts, learn more in this article .`——联系人与群组设置只有家长能改。但消息内容不在家长权限内：`Personal messages are end-to-end encrypted, which means no one, not even the parent or WhatsApp, can read them.` 写明个人消息是端到端加密的，连家长与平台都读不到。这两层在本篇并列而不互相抵消：**监护范围止于关系与请求，不及于内容**（附表 F 的 F14 行）。

《How to block and report someone on parent-managed accounts》补的是动作层：`Parent-managed account users can block or report a contact, group chat, or community.` 与 `They can also report a message, image, or contact.`——被监护账号可以屏蔽或举报联系人、群、社区，也可以举报消息、图片与联系人；`Parents will be notified when this action occurs through an activity alert.` 则写明家长会通过活动提醒获知这一动作。这里的通知方向值得单独指出：**被举报的一方不会收到通知（6.1 引原句），而举报一方的家长会收到提醒**；附表 F 的 F13 行把这两句并列，本篇在正文按"通知落在举报方一侧"写。

# 3 举报对象的专用页：每一类对象都有自己的一页

第 2 章的主页面处理的是"人"与"群"这两种对象。官方另外为六类对象各写了一页：用户名、贴纸、频道、状态更新、广告、商品与业务。这些页面的结构与主页面不同——它们大多先写"这类对象为什么会被举报"，再写一个入口，再写一句后果与一句通知。第 3 章按对象逐页登记，并把共享句与差异句指出来。

## 3.1 用户名：唯一带有"冒名"语义的对象

《How to report a username》的起手句是这一页的定性：`WhatsApp takes impersonation and abuse seriously.`——平台严肃对待冒名与滥用。它把可举报的情形写成两类：冒用（`If you think someone is pretending to be you or someone else on WhatsApp in order to scam other WhatsApp users, or attempting to buy, sell, or trade WhatsApp usernames, you should report the user and any suspicious messages so we can review and take action to protect our users.`）与用用户名发垃圾或诈骗（`Someone is using a username to spam or scam people If you receive unwanted messages from someone contacting you by username, you can block them.`，该句后面接着一个屏蔽动作句，本篇只引前半句的定性）。

用户名有一个别处没有的技术前提：它是唯一的，所以"完全同名"不可能发生——`Someone is pretending to be you with a similar username Usernames are unique, so no one can have the exact same username as you.`；但相似写法可以，官方自己举了例子：`However, someone could create a similar-looking username (for example, @JaneDoe vs @Jane_Doe or @JaneDoe1).`。这带来一条本篇要单独登记的规则：**相似用户名不构成"占用"，只构成"可举报"**——`Variations (with different numbers, underscores, or characters) are considered different usernames.` 说明变体被视为不同用户名，`You can’t reserve multiple usernames, but no one can create the exact same username as yours.` 说明不能预留多个用户名，`Certain usernames are held for businesses, governments, public figures and can't be claimed by others.` 说明一部分用户名被保留给机构。至于处置，官方写得含糊而一致：`After review, we may remove usernames or ban accounts that we believe are in violation of our or Business Terms of Service .`（审查后可能移除用户名或封禁账号）与 `We reserve the right to remove usernames or ban accounts that do.`（保留这样做的权利）。举报条数与主页面一致：`Select Report if you want to share the last 5 messages with WhatsApp.`。

这一页是附表 F 的 F3 行中"可能"那一侧的来源：同一批文本里既有"不必然处置"（2.1），也有"可能移除或封禁"（本节），本篇在 17.1 按"材料提交—平台判断"这条线收口，不写"举报即处置"。

## 3.2 贴纸与频道：两种不同的回执设计

贴纸页《How to report a sticker pack》只有三句可引，但它们把"举报不等于处置"写得最完整：`Please be aware that a user report of conduct in violation of our Terms of Service or Sticker Guidelines won't necessarily result in us taking action against the sticker creator.`（不必然对创作者采取行动）、`After you've submitted a report, you don't need to take any action or share additional details.`（提交后无需补充材料）与 `The sticker creator won't be notified of your report.`（创作者不会收到你的举报通知）。三句的结构与 2.1 主页面完全同构，区别只在对象从"用户"换成了"贴纸创作者"。

频道页《How to report a channel》是六页里唯一提供**可查状态**的：`To track the status of reports, tap > Settings > Help and feedback > Channel reports .` 给出 Channel reports 这个入口，`Reviews in progress are labeled Report in review .` 与 `Completed reviews are labeled Report reviewed .` 给出两个状态标签（in review 与 reviewed）。后果与通知各一句：`If the reported content doesn’t violate our Channels Guidelines, the channel will not be affected.`（不违规则频道不受影响）与 `No one in the channel will know about this report nor is the admin notified of your request.`（频道里没人知道这次举报，管理员也不会被通知）。频道页还多写了一条机构侧的争议通道：`If we take enforcement action against your channel, you can dispute our decision by requesting a review.` 与 `File a dispute If WhatsApp determines that a reported channel follows the Channels Guidelines, users in the European Region can also file a dispute with a Dispute Settlement Body using a reference number.`——若平台对频道采取执法，频道方可请求复核；欧洲地区的用户可以拿参考号向争议解决机构提出争议。**这条通道是给被处置方的，不是给举报人的**，本篇在 12.1 与 13.4 各再交代一次它与账号级申诉的区别。

把两页并起来，附表 F 的 F1 行成立：个人号与用户名的举报只承诺"收到即止"（2.1），频道举报提供状态查询（本节）；两者都是官方写法，不能合并成"举报都有回执"。

## 3.3 状态更新与广告：入口合流，对象不同

《How to report someone’s status update》只有三句：举报入口（`Report a status update While the status update is playing: Tap > Report .`）、连带的屏蔽选项（`Alternatively, tap Report and Block to also block the contact.`）与后果（`If the reported content doesn’t violate our policies, the user won’t be affected.`——不违规则对方不受影响）。这一页的价值在于它把"举报状态更新"与"屏蔽该联系人"放在同一句里，是 E12 行"屏蔽与举报是否连带"的第三个来源。

《How to hide or report an ad or website in WhatsApp Status or Channels》处理的是广告与广告落地页，它比状态页多两层。第一层是广告出现的位置与形态：`Ads will appear in Status and Channels, separate from your personal chats and calls.`（广告出现在状态与频道里，与私人会话分开）与 `Ads in WhatsApp Status and Channels are rolling out gradually and may not be available to you yet.`（分阶段上线）。第二层是三类可举报对象：广告本身（`Report an ad from WhatsApp Status or Channels If you think an ad includes problematic content, you can report it.`）、广告打开的网站（`Report a website accessed from an ad from WhatsApp Status If you think a website accessed from an ad includes problematic content, you can report it.` 与 `To report a website accessed from an ad: Open the website and tap > Report website .`）、以及未登录状态下的广告（`Report a channels ad when logged out You can report ads when logged out of WhatsApp in the Meta Ad Library .`——通过 Meta 广告库提交）。反馈与举报是两件事：`You can give feedback about the ads you see in WhatsApp Status or Channels to make your ad experience more relevant.` 与 `Tell us about an ad you're not interested in If you see an ad you don’t like, there are a few things you can do to remove the ad you see in Status and Channels.` 写的是"让广告更相关"的反馈，`Report an ad from WhatsApp Status or Channels If you think an ad includes problematic content, you can report it.` 才是举报。本篇据此在正文区分"隐藏/反馈"与"举报"，不把减少广告打扰读成处置请求。

这一页还有一句把广告标准外链到两份文本：`Read more about the standards we have to keep ads safe across all Meta Company Products in the Advertising Standards and on WhatsApp in the WhatsApp Business Messaging Policy .`——广告安全标准与商业消息政策。本篇引用它只为说明"广告的判定标准不在本页"，不展开两份文本的内容。

## 3.4 商品与业务：两个方向

《How to report a catalog or business》处理"用户举报业务"，它先排除一种误判：`If you haven’t received a response after reaching out to a business, it may be an issue with your device’s connection.`——没有回复可能是设备连接问题。然后才给举报入口与两类对象：`If your device is connected and the business still hasn’t responded, or you believe a business is violating the WhatsApp Business Messaging Policy , you can report them.`（业务违反商业消息政策时可举报）、`Report a product or service Open your chat with the business.`（举报商品或服务）与 `Report a business Open your chat with the business.`（举报业务）。举报与屏蔽的连带在这里是勾选项：`You have two options: To report and block the business, tap the check box next to Block .`；理由选择被写成一句流程句（`You will need to select a reason for submitting the report > tap Submit .`），本篇只引用它的前半句作为"需要选理由"这一事实，不复述界面顺序。

反方向的那一页是《How to block or unblock a business》：它写"业务屏蔽你"之后会发生什么——`What happens if a business blocks you A business can block your phone number.`（业务可以屏蔽你的号码）、`If a business has blocked you: You can't send messages to that business.`（你发不出去）、`You can't see when that business is online.`（看不到对方在线）、`Only the business can remove the block, and it can do this at any time.`（只有业务方能解除，且随时可以）与 `Being blocked by one business doesn't affect your account or your ability to message anyone else.`（不影响你给别的业务发消息）。F 表的 F7 行把 7.1 的接口句与 8.2 的联系人名单句并排；本节这一页补的是相反方向（业务屏蔽你）的后果：**同一动作的两个方向，后果不同、解除权不同**。

# 4 举报时到底送出去什么

第 4 章回答"材料"这件事。帮助中心有一页专门以这个问题为题（4.1），商业消息侧另有两页从数据流与留存角度写同一件事（4.2、4.3）。三节合起来是本篇关于"举报会暴露什么"的全部官方依据。

## 4.1 一页专门回答"收集什么"，但它在句子层是空的

《What information does WhatsApp collect when reporting an issue》的标题直译就是"举报问题时 WhatsApp 收集哪些信息"。这一页的正文在抓取件里只有几百个字符，池内零句——它的内容以清单或表格形式写出，按建池口径整页进不了句子层。本篇在这里只登记两件事：**这一页存在，且它的标题就是这个问题**；**本篇不转写它的清单**，因为那会把列表行改写成中文条目、冒充官方原句。

读者若要这一页的逐条内容，应按附表 A 的登记位回到抓取件；本篇能提供的是"这个问题的官方答案被单独放在一页上"这一事实。收集内容的可引部分集中在 2.1（举报材料）与 12.3（业务侧检测所用的信息），本篇不在这两处之外补写。

## 4.2 商业消息：加密状态、可选服务与用户侧处置

《Privacy and security for business messages》处理的是"你发给业务的消息"。它的第一句是安全投递：`When you message a WhatsApp business account, your message is delivered securely to the business.`；第二句给出默认状态：`WhatsApp considers chats with businesses that use the WhatsApp Business app to be end-to-end encrypted by default.`——使用商业 App 的业务会话默认端到端加密；第三句是边界：`Once the message is received, it is subject to the business’s own privacy practices.`——消息收到之后受业务自己的隐私惯例约束。第四句把"业务侧可能怎么用"写全：`The business may designate employees or other vendors to process and respond to messages, and a business may also use the chats it receives for its own marketing purposes, including advertising on Meta.`——业务可以指定员工或第三方处理消息，也可以把会话用于自己的营销。

可选服务那一段是本篇语料里唯一写"加密状态会变"的地方：`When businesses use these optional services, we display this clearly in the chat and do not consider messages with these businesses to be end-to-end encrypted.`——使用可选服务时平台会在会话里明确显示，并且**不把这些会话视为端到端加密**；`The encryption status of an end-to-end encrypted chat can’t change without being visible to the user.` 则给出保证：端到端加密会话的加密状态**不可能在用户看不到的情况下改变**。这两句合起来是本篇对"加密状态"的口径：状态变化是可见事件，不可见的变化不在官方写法里。

平台侧的动作与用户侧的手段各有一句：`When businesses use optional services from Meta, Meta uses automated systems to detect violations of relevant terms and policies to help keep people and businesses safe.`（可选服务下平台用自动化系统检测违规）与 `If you don’t want to receive messages from a person or business, you can always: Block them directly from the chat Delete them from your contact list We want to make sure you understand how your messages are being handled.`（不想收到就屏蔽或删除联系人）。此外，"广告互动开始会话"在这页被写成用户的一次选择：`For example, you can choose to start a chat with a business after interacting with their ad on Facebook and Instagram or interact with offers and announcements a business may send you on WhatsApp.`——它与"业务取得营销同意"不是同一件事，本篇在 8.2 并列这两侧。

## 4.3 平台侧的留存与广告边界

《Data Privacy & Security》从接口侧写消息的流向：`Please consult the applicable documentation for additional details on how Meta processes data for these services. ## Message flows When a user sends a message to a business that uses Cloud API, the message travels encrypted via WhatsApp between the user and Cloud API.`（消息在用户与 Cloud API 之间加密传输）、`Once Cloud API receives the message, Cloud API decrypts the message and forwards it to the business.`（Cloud API 解密并转交业务）与 `Upon receiving a message from a business, Cloud API will encrypt the message using the Signal protocol before sending it to the user via WhatsApp.`（业务发消息时反向重新加密）。这三句是技术链路，本篇引用它们只为说明"举报材料进入的是这条链路"，不展开协议细节。

与举报直接相关的有两句：`WhatsApp protects users by detecting unusual messaging patterns (like a business trying to message all users) and collecting spam reports from users.`——平台通过检测异常发送模式与收集用户举报来保护用户；以及 `Cloud API, operated by Meta, acts as the intermediary between WhatsApp and businesses using Cloud API.`——Cloud API 作为中间人。**这两句把"举报"写成了一种输入信号**，而不是一次独立的审核流程；具体的审核分工在 13.1 写（自动化与人工），本篇不在这里合并。

留存与广告边界各有明确一句：`Messages have a maximum retention period of 30 days in order to provide the base features and functionality of the Cloud API service; for example, retransmissions. **Does Meta have access to encryption keys?** In order to send and receive messages through Cloud API, Cloud API manages the encryption/decryption keys on behalf of the business.` 给出最大留存期（30 天，且附带用途说明），`Cloud API will not automatically use WhatsApp messages to inform the ads that a person sees.` 写明平台不会自动用消息决定用户看到的广告，`Cloud API will only use the messages it processes on behalf of and at the instruction of the business, unless otherwise directed.` 写明只按业务指示处理。这三句是 9.3 与 10.1 的前置条件：**平台侧能拿到的信息范围，直接决定了业务侧与平台侧各自能做什么判断**。

# 5 可疑消息与骚扰的用户侧识别层

这一章把"骚扰"从政策词翻译回用户能看到的四个页面：什么算可疑、遇到可疑怎么做、可疑链接靠什么提醒、以及"屏蔽高量未知消息"这个客户端开关管的是什么。四个页面各写一层，互不替代。

## 5.1 可疑消息的征兆清单：官方给的是信号，不是结论

《About suspicious messages and scams》先写平台做了什么，再写平台做不到什么。前一句是投入：`We work diligently to reduce any spam or unwanted messages that might be sent on WhatsApp.`；紧接着一句立刻限定了它的边界：`However, just like regular SMS or phone calls, it's possible for other WhatsApp users who have your phone number to contact you.`——只要对方有你的号码，就可能联系你。这两句并排，构成本篇对"骚扰"的第一个口径：**平台减少的是量，不是可能性**。官方没有在任何一页承诺"你不会收到陌生消息"。

动机被单独写成一句：`Some people might send you messages because they want to trick you into giving them personal or financial information, or they want to spread misinformation.`——骗信息、骗钱、散播不实信息。紧接着是全篇的方法论句：`Scams can happen anywhere and affect anyone, but you can help protect your account by knowing what to look out for.`——诈骗哪里都有、谁都可能遇上，能做的只有识别。注意这句的落点是"识别"，不是"拦截"；拦截在 5.4 才作为另一个开关出现。

识别清单本身写成"可能表明"（might indicate）：`How to identify suspicious messages There are clues that might indicate you’ve received a suspicious message, or that the sender can’t be trusted.`。首次接触时平台补充上下文，也全部用"may"：`In some situations, we may show you additional context when you reach out to someone for the first time.`、`We show these tools when you first message with someone who is not saved to your contacts and may be someone you don’t know.` 与 `When this happens, we’ll share additional details with you to let you know if you have groups in common and what country their phone number is registered in.`——共同群组、号码注册国家。这些信息的作用被限定为"让你自己判断"：`From there, you can make an informed decision about whether to continue or cancel the chat.`。本篇据此不把这类上下文写成"风险评分"，官方没有给分。

动作层写在一句枚举里：`From there, you can decide if you should reply, add them as a contact, block them, or report them.`——回复、加联系人、屏蔽、举报，四选一。这是本篇语料里把"举报"与"屏蔽"并列为平行动作的最直白一句。遇到可疑消息之后的具体建议则写成三条：不看、不转（`Best practices if you receive a suspicious message Use these best practices to help protect yourself: If a message looks suspicious or sounds too good to be true, don't tap, share, or forward it.` 与 `As a best practice, if you aren’t sure whether something is true or don’t know who wrote the message you received, don’t forward it.`）、先看清链接或文件再打开（`Always look closely at a link or file before opening it.` 与 `At a glance, links and files might appear legitimate, but could be malicious.`）、以及"举报＋屏蔽＋删除"三件事可以一起做（`You can also report the message, block the sender, and delete the message.`）。**"删除"在这一页与举报、屏蔽并列，但它不是处置动作**：删除只改你自己的会话，官方没写它会把任何东西送到平台。

已知联系人发来可疑消息的处理另有一句：`If you receive a suspicious message from a known contact, their account could be compromised.`——账号可能已被盗。这句把"可疑"的来源从陌生人扩展到账号失守，是 5.1 与第 2 章、第 3 章之外的一条独立入口。

诈骗分类在本篇只按官方给的名字登记，不补充任何新类别。总起句：`Common examples of scams Understanding common scams could help you recognize a scammer and protect yourself.`；接触方式：`Someone attempting to scam you might message you, call you, or add you to a group.`；用工诈骗两句：`Employment scam: You could also receive fake job opportunities from scammers.` 与 `Someone could pretend to be a recruiter at a well-known company to trick you into sharing personal information, or ask for money as part of a job application.`；投资诈骗：`Investment scam: A scammer could share a fake investment opportunity with the promise of low risk and high returns to encourage you to share personal information or money.`；冒名诈骗：`Impersonation scam: It’s common for scammers to pretend to be a friend or family member to trick you into sharing money or personal information.` 与 `Scammers could also pretend to be a celebrity or a well-known business or company.`。另有一句与"封号"直接相关：`If someone requests payment for using WhatsApp or offers to unban your WhatsApp account in exchange for payment, they are likely after your financial or personal information.`——凡有人收费帮你解封，基本是在骗钱。这句在本篇的作用是切断"付费解封"这条叙事，与 12.4 的官方申诉入口形成对照。

## 5.2 五步动作页：把"停下来"写成第一步

《How to protect yourself from suspicious messages and scams》是本篇语料里唯一把应对动作排成序号的一页。它的第一步是判断信号：`Pause and think Look out for suspicious behavior: Are they using an unknown number?`、`Are they being threatening or asking you to trust them?` 与 `Are they asking you to transfer money or share a password, pin, or personal information? 2.`——陌生号码、威胁或要求信任、索要转账与口令。第二步是中止：`Stop the conversation Hang up a call or stop replying to the user.` 与 `If you can’t verify the contact’s identity, do not share any personal or financial information. 3.`——挂断或停止回复；不能核实身份就不给个人与财务信息。第三步才是本篇的主轴：`Block and report Block the user to stop them from contacting you, and report them to WhatsApp.`——屏蔽切断接触，举报交给平台。第四步回到设置：`Update your privacy and security settings Adjust your privacy settings to control who can see your personal information and contact you.` 与 `You can also enable two-step verification to better secure your account.`——隐私范围与两步验证。

这一页的次序值得单独指出：**官方把"屏蔽"放在"举报"之前，并且用"and"把两者连成同一步**。这与 2.1 主页面"举报不改变接收关系"的写法合起来读，结论是：屏蔽负责即时效果，举报负责把材料交出去，两者不是替代关系。本篇不写"举报即可让对方收不到消息"这类因果。

## 5.3 可疑链接：提醒是启发式的，不构成安全承诺

《About suspicious links》只讲一种骚扰载体——链接。触发条件写得很窄：`This reminder might appear when a link contains an unusual combination of characters.`——字符组合异常。动机：`Spammers might use misleading character combinations to trick you into tapping on links.`；后果：`These links might appear to go to legitimate websites, but actually take you to a harmful site.`。示例句直接给出同形字：`You’ll notice the first character looks like the letter "w" but is actually a different character, such as "ʷ" or "ẁ".`——首字符看起来是字母 w，实际是另一个字符。这句的解释：`This means a spammer is trying to trick you into visiting a website that isn’t actually associated with WhatsApp and that may expose you to harm.`——目的是把你带到与 WhatsApp 无关的站点。

用户侧的动作被写成三层，而且都不承诺结果：先看内容（`How to protect yourself When receiving links, carefully review the content of the message.`）；被标记时点开看高亮（`If a link is marked suspicious, you can tap the link and a pop-up message will appear.` 与 `The message will highlight any unusual characters within the link.`）；没被标记的链接，在打开前先点短链核对（`To check a link that isn’t marked as suspicious, tap the short link by before tapping Open Link to continue.`）。另有一条硬限制：可疑链接不能从消息预览里直接打开（`When you receive a link, you can choose to open the link from the WhatsApp message, or go back to the chat. : When a message contains a suspicious link, you can’t open the link from the message preview on your device.`）。

本篇对 5.3 的读法是：**提醒存在，但它按特征触发，未触发不等于安全**。官方在这一页从头到尾没有写"所有恶意链接都会被标记"，也没有给误报率；本篇不把"没提醒"当作"可信"。

## 5.4 "屏蔽高量未知消息"：一个客户端开关，不是屏蔽名单

《How to block high volumes of unknown messages》描述的机制与第 2 章的屏蔽名单完全不同。它的触发是量：`If Block unknown account messages is turned on, WhatsApp will block messages from unknown accounts when they exceed a high volume.`——超过高量才拦；例外是联系人：`During this period, your contacts can message you as usual.`；解除是自动的：`Message blocking stops after message rates return to normal.`——回落到正常速率就停。它可能被提示开启：`You might be prompted to turn this feature on if you receive a high volume of unknown messages.`。最容易被误读的一句是：`If you turn on Block unknown account messages , you won't block all unknown messages.`——打开它不等于屏蔽所有未知消息。

把这四句合起来，本篇给这个开关的定性是"按速率的临时拦阻"：它不产生名单、不入屏蔽列表、不需要逐个解除，也不覆盖联系人。它与 2.1 的逐个屏蔽是两套机制，正文不把两者写成"加强版屏蔽"。

# 6 群与社区：三种举报对象与两套管理工具

群组页与社区页面对的是同一个问题的两侧：群组页回答"我在群里被骚扰能做什么"，社区页回答"管理员和成员各自有什么工具"。两页的层级不同，本篇分开写。

## 6.1 群里的三种举报对象：消息、成员、整个群

《How to stay safe in groups on WhatsApp》开篇先承认群是被拉进去的：`When you’re added to a group by someone not saved in your contacts, we show you information about the group so you can decide if you want to exit the group.`——被不在联系人里的人拉群时，平台会给出群信息让你决定；`Tap Stay to stay in the group, or Exit group to leave the group.` 是留下或退出的两个选项；`After tapping Exit group , you can also tap Report to WhatsApp if you would like to report the group.` 说明退出之后还可以顺手举报。默认设置也写明了：`Group privacy settings By default, your group privacy settings are set to Everyone , so anyone can add or invite you to join a group.`——默认任何人都能把你拉进群；`For additional privacy, you can control who can add you to a group by adjusting your WhatsApp Settings.` 是改法所在的那一句（本篇只登记"设置里可改"，不重述步骤）。

三种举报对象的分工是这一页的核心，三句连着写：举报单条消息，用于单条内容有问题（`When to report Understanding when to use each reporting option helps keep your group safe: Report a message: Use when a single message contains problematic content (e.g., spam, scams, or illegal content).`）；举报成员，用于同一个人反复发（`Report a member: Use when a person is repeatedly sending problematic content.`）；举报整个群，用于整个群被用于问题或非法活动（`Report the group: Use when the entire group is being used for problematic or illegal activity.`）。这是一张按"对象粒度"排的表，不是按"严重程度"排的表——本篇照它的分法写。

单条消息的举报后果有两句关键限定。一句是回执方向：`The message will be sent to WhatsApp, and the sender or group won’t be notified.`——消息会送到平台，发送者或群不会被告知。另一句是屏蔽的边界：`When you select Block contact , you’ll no longer receive messages or calls from them.` 与 `You’ll still be able to see their messages in groups.`——屏蔽后不再收到对方的消息与通话，但在群里仍能看到对方的消息。**所以"在群里屏蔽某人"只切断一对一，不切断群内可见性**，这与 2.1 的结论一致，本篇在 6.1 再登记一次。送出去的材料：`WhatsApp receives the reported user ID, information on when the message was sent, and the type of message sent (image, video, text, etc.).` 与 `WhatsApp may also receive how the first contact was initiated between you and the reported group.`——被举报用户标识、发送时间、消息类型，以及首次接触如何发起。

最容易读错的一句是"群本身不能屏蔽"：`Block a group It’s not possible to block a group, however you can block and report group admins and members.`——不能屏蔽一个群，但可以屏蔽并举报群管理员与成员。紧接着的动作句：`Block a group admin or member You can stop receiving messages from group admins or members by blocking them.`——通过屏蔽管理员或成员来停止接收他们的消息。这两句合起来说明：**对群的处置只有"退出"与"举报"两条，没有"屏蔽"这条**。本篇因此不写"屏蔽群"。

举报整个群的材料上限：`When you report a group, WhatsApp receives up to five of the last messages sent to you in the reported group.`——最多最近五条发给你的消息；另有群标识与元数据（`WhatsApp also receives the reported group ID, information on when the message was sent, and the type of message sent (image, video, text, etc.).` 与 `WhatsApp may also receive how the first contact was initiated between you and the reported group.`）。转发历史的责任句在这一页也出现一次：`If someone you sent message history to reports content that was sent as part of the message history you sent, you’ll be reported.` 与 `The original sender of the message won't be reported.`——转发者被举报、原始发送者不被举报，与 2.1 的写法一致。

管理员一侧的工具被写成四件事：移除成员（`Manage group content as an admin If you’re a group admin, you can use these tools to manage problematic content and members: Remove problematic members from the group.`）、删除消息（`Delete problematic messages sent by users to the group chat.`）、开启"送管理员审核"（`Enable “Send for admin review” to allow members to share messages that they would like to send for review.`）、撤换其他管理员（`Dismiss other admins who may be causing harm or disruption to the group.`）。退出群的入口句只登记不重述：`Exit a group Tap the group name > Exit group .`。

## 6.2 社区：管理员工具与成员工具是两张清单

《How to keep your groups and community safe》的定位是"给社区成员看的快速参考"（`Share this page with your members as a quick reference guide.`）。它开头就写社区应当是什么：`Your community should be a space where people can connect meaningfully.`；平台提供工具来实现它：`We’ve designed tools and safety features to help you achieve that.`。本篇把这一页的价值定在"两张工具清单"，而不是"社区功能介绍"。

隐私与安全工具按名称登记：两步验证`Two-step verification: when you turn on two-step verification, you can choose to use your phone number, password, or email address for additional authentication.`、端到端加密`End-to-end encryption: WhatsApp protects your personal messages with end-to-end encryption.`、生物识别解锁`Enable Touch ID or Face ID: Turn this feature on so only you can unlock your WhatsApp account.`、限时消息`Control what you send and see Send disappearing messages: You can decide how long messages remain visible once you've sent them - 24 hours, 7 days, or 90 days.`（官方在同一句里给了三档时长）、一次性查看`Use view once for shared content: For added privacy, you can send photos and videos that disappear from your chat after they've been opened once.`。转发识别：`Pay attention to messages labeled “Forwarded many times.”` 与 `Avoid rumors, viral messages, and fake news by checking for accuracy before forwarding content - especially if it’s a chain message and the “Forwarded many times" label is displayed.`——"多次转发"标签是提示，不是判定；官方要求转发前先核实。

"避免 unwanted contact"这一段是本篇与 6.1 的接点：屏蔽与举报的定义句 `Avoid unwanted contact Block and report: You can stop receiving messages, calls, and status updates from a contact by blocking them.` 与 `Report inappropriate behavior: You can report inappropriate content, messages, or members.`——屏蔽切断消息、通话与状态更新，举报针对内容、消息或成员。注意这一页把"举报"的对象写成"content, messages, or members"，比 6.1 的三分法少一个"整个群"，本篇不据此改写 6.1。

管理员工具清单：邀请链接（`Admin controls Admins have access to the following tools: Invite members to join your community by sharing a link.`）与被明确禁止的做法（`Don't post the link on a public forum or website.`——不要把链接发到公开论坛或网站）、限制谁可以改设置（`Restrict who can make changes to your community.` 与 `You control who can make changes to the group or community's subject, icon, or description.`）、删除消息与移除成员（`Delete unwanted messages or remove members.` 与 `As a community admin, you can easily delete unwanted messages or remove members from the community.`）。成员工具清单：群权限（`You can decide who can add you to a group and if you want to stay in a group you’ve been added to.`）、安静退出（`Exiting a community: Members can leave a community they no longer want to be a part of by using the exit community feature.` 与 `If you choose to leave you can do so quietly.`）、举报内容（`Report inappropriate content: Members should report inappropriate or harmful content to WhatsApp to help us keep it safe.`）、静音（`Mute group notifications: If a community becomes too noisy, members can mute group notifications.`）。

两张清单的差别正是本篇要指出的：**管理员能对"别的成员"动手，成员只能对"自己的接收与在场"动手**；退出可以不出声，这一句把"退出"与"举报"彻底分开——退出不产生任何送交平台的材料，举报才产生。

# 7 业务侧：屏蔽接口与错误语义

用户侧的屏蔽是"我不要再收到"；业务侧的屏蔽接口是"这个号码不要再发进来"。两者方向相反，本篇分节登记，不合并成"双向屏蔽"。

## 7.1 屏蔽接口：按号码逐个成功或失败

《Block users》第一句就是语义：`Attempts to message a blocked user return an error.`——向已屏蔽用户发消息会返回错误。第二句是本篇最看重的一句：`The API returns errors per-number, because a block can succeed on some numbers and fail on others.`——接口按号码逐个返回错误，因为一次调用里可能有的号码成功、有的失败。**这意味着"屏蔽成功"不是一个整体状态，而是一组逐号码的结果**。本篇据此不写"一次调用即可完成名单同步"。

对象字段的优先级也写明了（这两句原文带行内代码，本篇只转述，不整句引）：每个对象可以用电话号码或业务级用户标识指定，两者同时给出时以电话号码为准。返回值一侧：`Set to the value you sent (a BSUID or parent BSUID).`——你发出去的那个标识会原样回显在返回里。这两条合起来构成本篇对"业务侧屏蔽"的口径：它是一个以用户标识为键的名单操作，成败粒度是单个标识。

## 7.2 接口参考页：登记了，但句子里一句都没有

《Base URL》在附表 A 里是一行正常登记位，但它的正文是参数与端点表，按建池口径整页进不了句子层（1.3 已交代）。本篇在这一节只做两件事：一是说明"登记≠引用"，其余五张附表里都不会出现这一页；二是交代它的取值集合为什么在本篇查不到——它的枚举值只写在参数表里，建池时随表格一起剔除，D 表收录的是另外几页 webhook 的参数取值；本篇因此不声称掌握这一页的取值，读者要核对字段只能回原页，正文也不会出现从这一页引出的句子。

## 7.3 错误码页：把"限制"与"封禁"写成两种东西

《WhatsApp error codes》的方法论在前几句：错误处理应当围绕代码与细节字段来建（这一句的原文带行内代码，本篇不整句引用，只转述其结论）；标题会被废弃，不能当判据。同步与异步两条通道并列：`Check the eligibility requirements for the API that you are trying to access.` 是前置条件——先确认自己有没有资格调这个接口；错误也可能同时从两条通道回来（该句原文带加粗小标题，本篇只转述）。本篇引用的是三句行为描述：

其一是解封类：`Unblock the WhatsApp user to resume sending messages to them.`——解除对用户的屏蔽才能继续发。其二是限期类：`This restriction is automatically lifted after the enforcement period.`——这类限制到期自动解除，不需要你做什么。其三是重试类：`Doing so will only result in another error response since the limit may be in effect for differing periods of time.`——在限制期内反复重试只会再拿一个错误，因为不同限制的期限并不相同。三句放在一起，本篇得出的口径是：**官方把"要你去解除的"、"到期自解的"、"重试无用的"分成三类，全部用错误码而不是用"封禁"一个词概括**。正文不使用"封号"去翻译这三类中的任何一类。

另有两句属于"错误处理之外但常被误读"的：`Inspect any media files that are causing errors and confirm that they are in fact supported.` 与 `Ad synchronization can take up to 10 minutes.`——前者要求先确认媒体文件本身受支持，后者说明广告同步可能耗时；前者是自查项，后者是等待项，都不属于处置动作。

## 7.4 消息错误 webhook 页：同样是零句登记位

《Errors messages webhooks reference》与 7.2 同属"正文取回但池内零句"的三页之一，理由也一样：整页是错误码表。本篇在这一节只登记两件事：这一页不进句子层；它只在附表 A 留一行登记，其余五张附表里都不会出现这一页。7.3 的三类划分来自错误码说明页，与本页不是同一页，本篇不把两页的码表混成一张。

# 8 退订信号与 opt-in 的反面

第 9 章讲业务怎么把消息发出去，这一章讲用户怎么把它关掉——以及关掉之后，业务侧收到的是什么。两页的分工是：接口页写信号长什么样，帮助页写"点下同意"意味着什么。

## 8.1 退订信号：一个取值，一条不报错的静默路径

《user_preferences webhook reference》是 user_preferences webhook 的参考页。它的句子层只有三句，其中两句可用。第一句讲字段缺失的条件：业务级用户标识未启用时，用户标识字段会被省略（原文带行内代码，本篇只转述）。另一句：`Omitted if the user has adopted a username and their phone number is unavailable.`——用户改用用户名且号码不可用时，号码字段被省略。两句合起来说明**退订信号本身也不是每次都带全标识**。

退订取值那一句的原文带行内代码，本篇不整句引用；它说的是：该取值表示用户已选择停止接收该业务的营销消息。发送侧的对应写法在另一页：`If you attempt to send a marketing template to a WhatsApp user who has stopped marketing template messages from your business, the API will process the request but not send the message.`——向已停止接收营销模板的用户发送时，接口会处理请求，但不会发送消息。**这句是本章的关键：退订在发送侧表现为"请求被接受、消息不发出"，而不是一个错误**。所以"接口返回成功"不能当作"消息已送达"，本篇不把两者等同。

## 8.2 opt-in 页：同意的另一面就是屏蔽

《Get opt-in for WhatsApp》是给业务看的取得同意专页。它先给合规判据：`As long as the opt-in method meets the above requirements, it will be policy compliant.`——方法满足要求即合规。再给一个"用户应当预期收到"的原则：`For example: - Users should expect the messages they receive.`——用户应当预期自己会收到这些消息。这一页没有给"最优方法"，只给判据；本篇因此不写"某种渠道一定合规"。

这一页最容易被忽略的是最后三句：`People can also block or report a business.`——人们也可以屏蔽或举报业务；`Our systems will rate limit businesses if the business's quality is low for a sustained period of time.`——质量长期偏低会被限速；`We may also reactively evaluate user feedback to flag policy violations and develop additional types of enforcement over time.`——平台也会反过来用用户反馈去标记违规。**把这三句放在"取得同意"的页面末尾，等于官方把"同意的反面"写进了同一页**：opt-in 不是一次性授权，用户可以随时用屏蔽与举报撤回它，而平台会把这些信号计入质量。

用户侧的同一条链在另一页写得更具体：《About opting in to receive messages from a business on WhatsApp》先写点下注册按钮意味着什么：`What happens when you sign up When you tap Sign up or similar buttons to connect with a business on WhatsApp: You agree to share your phone number with that business.` 与 `The business can send you messages, including marketing and promotional content.`——同意共享号码、业务可以发包括营销在内的消息；`Your WhatsApp number becomes visible to that business.`——号码对业务可见。停止入口：`Alternatively, you can report and block by tapping and holding a message, then selecting Report .`——长按消息即可举报并屏蔽。隐私边界：`Your privacy Businesses cannot see your profile photo, status, or last seen unless you've adjusted your privacy settings.`——业务看不到头像、状态与最后上线，除非你改过隐私设置。最后一锤是方向句：`Blocking a business removes your number from their contact list.`——屏蔽业务会把你的号码从对方的联系人名单里移除。

这句与 7.1 的"业务侧屏蔽用户"正好构成一对反向操作：**用户屏蔽业务，结果是号码离开业务的名单；业务屏蔽用户，结果是业务再发就报错**。两句话都用了"名单"或"错误"，但一个是用户侧的结果、一个是接口侧的结果，本篇在 8.2 并列而不合并（附表 F 的对应行按这两句登记）。

# 9 商业会话的三个计数层

营销消息的"量"在官方文本里被至少三种不同的窗口切开：计费按送达、按人上限按动态窗口、重试按 24 小时。三页各写一层，混读就会得出"发够五条就会被封"这类不存在的规则。

## 9.1 营销消息：付费单位是"送达"，阅读数据天然不全

《About marketing messages》先写产品定位：`With marketing messages on the WhatsApp Business app, you can send customized messages to multiple people at once to re-engage your audience efficiently.`——在 Business app 上向多人发定制消息以重新触达；价值句：`Marketing messages can help you improve message response rates and build stronger relationships with your customers while saving time.`。计费句是这一页最硬的一句：`With marketing messages, you pay for each message that is delivered to a receiver you chose within five days of being sent.`——按每条**送达**给收件人的消息计费，窗口是发出后五天。注意这句的两个限定：计费对象是 delivered，不是 sent；窗口是五天，不是"永久"。

数据句紧跟其后，并且自带免责：`You can also see how many of your customers have read and replied to your messages to understand how they perform.` 与 `You can use this information to help improve your future marketing messages. : This information won’t reflect receivers who have turned off read receipts, or any messages delivered while your own device’s read receipts is turned off.`——已读与回复数可以用来看效果，但读数不含关闭已读回执的收件人，也不含你自己设备关闭回执期间送达的消息。**因此"阅读率"在本篇里是一个下界，不是全量**。可用范围另有一句：`Marketing messages is currently available in limited countries and might not be available to you yet.`——只在部分国家可用。

## 9.2 商业广播：同一套计费，换了一个名字

《About business broadcasts》与 9.1 高度同构，差别在名称与可用性。计费句：`With business broadcasts, you pay for each message that is delivered to a recipient you chose within five days of being sent.`——按每条送达给你选定的收件人的消息计费，窗口同样是五天。数据句同样带免责：`You can use this information to help improve your future business broadcasts. : This information won’t reflect recipients who turned off read receipts, or any messages delivered while your own device’s read receipts is turned off.`——不含关闭已读回执的收件人，也不含你设备关闭回执期间送达的消息。可用性：`Business broadcasts is currently available in select countries and may not be available to you yet.`——只在部分国家提供。另有一句时间节点：`If you've never sent a business broadcast, your trial ended on April 30, 2026.`——从未发过商业广播的账号，试用在 2026 年 4 月 30 日结束。

本篇把两页并列处理，是因为它们的计费与数据口径逐句对应；但名字不能互换——9.1 的页面标题用 marketing messages，9.2 的页面标题用 business broadcasts，且 9.2 明说"在某些国家，营销消息现在叫商业广播"（该句为页首说明，本篇只登记名称关系，不据此改写 9.1 的标题）。

## 9.3 offers 与 announcements：官方为"限额"给出的理由是体验

《About interactions with business chats that include offers and announcements》回答的是"为什么平台要限制这类消息"。前提句：`WhatsApp requires businesses to secure your permission to send offers and announcements and WhatsApp takes action to prevent you from receiving too many of these messages. : Businesses may choose to share your activity information with Meta to send you relevant messages and improve Meta ads.`——业务必须取得许可，平台也会采取行动防止你收到过多此类消息。收集信息的理由与范围写在相邻几句里：`How WhatsApp limits bad experiences and business message overload In order to limit bad experiences (like spam) and manage the number of offers and announcements you might receive, WhatsApp collects limited information about your chats.`——为限制不良体验（如垃圾信息）并管理数量，平台收集有限的会话信息；范围包括每日与每周会话数（`This includes: The number of chats you have per day and week.`）、上次收到此类消息的时间与相对频率（`When you last received an offer or announcement from a business, and how often you receive them in comparison to your other chats.`）、与业务的互动（`Interactions with businesses or business threads (like feedback you submit).`）。**这三项都是行为信号，不是消息内容**——同一页紧接着写加密：`As always, your personal messages are end-to-end encrypted.` 与 `No one outside the chat, not even WhatsApp, can read, listen to, or share them.`。

Meta 侧的边界写得比一般页面更细：`Your privacy is our priority This icon means WhatsApp's parent company, Meta, may receive limited info to reduce spam, help businesses send you relevant messages and improve Meta ads.` 说明该图标意味着 Meta 可能收到有限信息；`The content of your messages and calls isn’t shared by WhatsApp.` 明确消息与通话内容不被共享；`Meta may receive limited information like: Feedback you submit about a business.` 与 `Browser interactions from links shared by businesses in WhatsApp, like how long the browser was open.` 列举了有限信息的两类（你对业务的反馈、从业务链接进入浏览器的交互）。用户侧的退出句：`You can stop chatting with the business or block them at any time.`——随时可以停止会话或屏蔽。本篇在这一节只登记"官方为限额给出的理由与收集范围"，不替平台补充任何未写明的用途。

## 9.4 按人上限：动态、按送达计、重试另有窗口

《Per-user marketing template message limits》是三层窗口里最细的一页。上限的决定因素：`WhatsApp determines this based on several factors, including a dynamic view of an individual's recent marketing message read rate and how many messages they currently have in their inbox from friends, family, and businesses.`——包括该用户近期营销消息阅读率的动态视图，以及其收件箱里来自亲友与业务的消息量；`The per-user marketing limit adapts automatically over time based on a person's recent engagement levels.`——上限会随时间自动调整。计数单位：`Attempting to send a template message to a WhatsApp user with a US phone number after this date will result in an error. ### How limits are counted Each marketing template message delivered counts towards the per-user marketing limit.`——每条送达的营销模板消息计入上限。

重试规则有独立两句：`Resending earlier than this will likely result in additional error responses and can reduce the accuracy of campaign delivery reporting.`——过早重发很可能只拿到更多错误，并降低投放报告的准确性；`WhatsApp enforces limits on excessive retry attempts of marketing template messages to users that have reached their per-user marketing template limit.`——平台会对"向已达上限用户反复重发"的行为本身施加限制。后果句：`If your Messaging account attempts to resend marketing messages multiple times within a 24-hour period to users who have already reached their messaging limit, further delivery attempts to these users may be unavailable for up to 24 hours and error code 131049 will be returned.`——24 小时内多次重发，可能导致对这些用户的后续投递在一段时间内不可用，并返回错误码；`If you receive this error code, wait at least 24 hours before resending the template message.`——收到该错误码后至少等 24 小时再重发。

这一页还写了一个范围限定：`This does not affect your ability to send marketing messages to other users. ## Error code If a marketing template message is not delivered due to per-user marketing template limit enforcement or a business has repeatedly attempted to resend a message to a user that has reached their limit, the messages webhook is triggered with status set to failed and error code set to 131049.`——不影响你向其他用户发送营销消息。把这句与 9.1 的五天计费窗口、9.4 的 24 小时重试窗口并排，本篇的结论是：**官方文本里有三个互不替代的时间单位——计费窗口（天）、按人上限的动态窗口、重试冷却（小时）**；把它们压缩成"发多了就封"是对三页的同时误读。

# 10 营销消息的审核与账号级暂停

第 9 章讲的是"发多少"，这一章讲"发之前谁看、发不了的时候是什么状态"。前者是可计量的窗口，后者是账号级的开关；两者在官方文本里分属不同的页面，混读会得出"审核通过就等于不会被暂停"这种不存在的保证。

## 10.1 发出之前的那道审核：只看草稿，不看受众

《Why is Meta reviewing my marketing messages?》整页只回答一个问题：为什么 Meta 要在我发营销消息之前审一遍。前提句：`We strive to keep Whatsapp users safe and prevent abuse on our platform.`；机制句：`To help achieve this, your marketing messages are subject to review by Meta before you send them in chats on WhatsApp.`——营销消息在进入聊天之前要经过 Meta 审核。边界写得同样清楚：`Meta can’t access your messages once they’re sent in a WhatsApp chat.`——消息一旦发出，Meta 就访问不到；`Meta can only review messages before they’re sent in chats, and does so only to make sure they don’t contain anything that might be harmful to Meta, WhatsApp or our users.`——审核只发生在发送前，目的限定在"确认不含对 Meta、WhatsApp 或用户有害的内容"。

受众一侧的边界是这一页最容易被忽略的一句：`Meta cannot access any information or details of the audience you choose for your marketing messages.`——Meta 拿不到你所选受众的任何信息。这句与 9.3 的"收集有限会话信息"必须分开读：9.3 说的是**平台为限额而收集的行为信号**，这一句说的是**审核环节看不到受众名单**；两件事的主体与目的都不同，本篇不把它们合并成"平台知道你在发给谁"。

未通过时的回执：`You’ll be notified if a draft message is not approved to send, with guidance for how to update it and try again.`——草稿不被批准发送会收到通知，并附修改建议。注意它写的是 draft，不是"已发送后被撤回"；审核在发送前，所以不存在"发出去再被删"这一步。

## 10.2 账号级暂停：三天的时钟与"自动恢复"

《Can't send marketing messages》处理的是"账号发不出营销消息"这个状态。它先给两种可能：`If you are unable to send marketing messages from your account, it’s possible that the feature isn’t available to you yet or your account was flagged for violating Meta Terms for WhatsApp Marketing Messages .`——要么功能还没对你开放，要么账号被标记为违反营销消息条款。这两件事在这一页被并列，说明"发不出去"本身不是违规的证据。

警告与暂停的阶梯写得很具体。第一步是警告：`If a significant number of users report or block your account due to unwanted messages, your business account will receive a warning.`——大量用户因不想要的消息而举报或屏蔽时，账号会收到警告。第二步是暂停：`However, if you continue to send spam from your business account, marketing messages will be suspended on the business account for three days.`——继续发送垃圾信息，账号的营销消息功能会被暂停三天。这一步正是账号级与模板级的分界：**暂停的对象是账号的营销功能，不是某一个模板**（模板级的计时在 11.2，两者单位不同）。恢复不需要申请：`Access to the marketing messages will be automatically reinstated on the date and time shown in the app.` 与 `There is nothing else for you to do to get the access back.`——到应用里写明的日期时间自动恢复，没有别的事要做。

再犯的后果：`However, if recipients continue to report or block your messages, your account can be restricted again.` 与 `Repeated restrictions may result in longer suspensions.`——恢复后继续被举报或屏蔽可以再次受限，反复受限可能带来更长的暂停期。**这就把"三天"写成了首次的固定值，而不是上限**；本篇不写"最多三天"。

排期消息的处理也有一条明确后果：`Scheduled messages If you had scheduled marketing messages before your account was blocked, those messages won’t be sent.` 与 `Your account will not be charged for these scheduled messages.`——暂停前已排期的消息不会发送，也不会计费。这一句说明暂停的生效点在发送时，而不是排期时。

## 10.3 模板页：模板是按人限额的载体，不是群发清单

《Marketing templates》在 8.1 已经引过它的"已退订则不发送"那一句，本节只补两块与本轴直接相关的内容。其一是按人限额的决定因素：`In most WhatsApp markets, this is determined based on a number of factors, including a dynamic view of an individual's marketing message read rate.`——在多数市场，上限取决于多个因素，其中包括对单个用户近期营销消息阅读率的动态观察。其二是会话排序：`These messages will still be delivered and visible to business customers, but the thread will only move to the top if the customer responds.`——这些消息仍会送达并对业务客户可见，但只有当客户回复时，会话才会移到列表顶部。

这两句合起来，本篇对"模板"的定性是：**模板是消息的合规外壳，不是绕过按人上限的通道**。模板可以被反复发送，但每一次送达都计入 9.4 的上限；模板也不会因为送达而让会话自动置顶。另有一句只做登记：停止与恢复这两个动作才会触发相应 webhook（该句原文带加粗小标题，本篇只转述）。

## 10.4 模板基础页：命名、语言与参数的三条硬规则

《Template fundamentals》是模板体系的总说明页。定位：`Templates are Messaging account assets that can be sent in template messages via Cloud API or Marketing Messages API for WhatsApp.`——模板是消息账号的资产，可通过 Cloud API 或 Marketing Messages API 发送。创建走统一语法：`Template creation via API uses a common syntax.`。命名与语言：`This flexibility allows you to create multiple templates with the same name, but in different languages.`——同名模板可以用不同语言并存；`Template strings and variables are not translated by Meta, so you are responsible for supplying strings and example parameters in their appropriate language.`——Meta 不翻译模板文本与变量，语言与示例参数由业务自己提供。参数顺序：`These are replaced with values included by you in your send message payload when you send the template.`——发送时用你载荷里的值替换；`Example values in template creation payloads and real values in template send payloads can appear in any order.` 与 `Example values in template creation payloads and real values in template send payloads must appear in the order in which their corresponding placeholders appear in the component text string.`——示例值可以任意顺序出现，但真实值必须按占位符在文本中出现的顺序排列。

状态与上限：`If it is rejected, or if its status changes to any other value, it cannot be sent in template messages.`——被拒或状态变为其他值时，模板不能用于发送；`If a parent business portfolio is unverified, each of its Messaging accounts is limited to 250 message templates.`——父业务组合未验证时，每个消息账号最多 250 个模板；`You can customize the TTL for templates upon template creation.`——创建时可自定义模板的 TTL；`This rating helps maintain a high-quality messaging ecosystem and helps ensure that you are sending relevant and well-received messages.`——质量评级的作用是维持高质量生态并确保消息相关且受欢迎。这几句里"250"是一个**账号级**上限（9.1 的计费、9.4 的按人上限、这里的模板数上限，是三个不同的"量"），本篇分节登记，不合并成"总量限制"。

# 11 模板质量与暂停的计时

模板的质量不是一次判定，而是一条随时间更新的评级；暂停也不是"关掉就完"，它有自己的三档时长、编辑后的重审路径与申诉时限。这一章按"评级—暂停—两个 webhook"的顺序写。

## 11.1 三档评级与一个"待定"：评级由 API 报告

《Template quality rating》把评级写成 API 报告的值，并给每一档配一句判据。高质量：`The template has received little to no negative feedback from WhatsApp users.`——几乎或完全没有收到负面反馈。中质量：`The template has received negative feedback from multiple WhatsApp users, or low read-rates, and may soon become paused or disabled.`——收到多个用户的负面反馈或低阅读率，可能很快被暂停或停用。低质量：`The template has received negative feedback from multiple WhatsApp users, or low read-rates.`——同样收到多个用户的负面反馈或低阅读率。另有一档待定，表示尚无反馈与阅读率数据（该句原文带行内代码，本篇只转述）。

更新机制：`WhatsApp updates the rating automatically as it collects usage, feedback, and engagement signals over time.`——评级随使用、反馈与互动信号自动更新。后果：`If a template continuously receives negative feedback or low engagement, it can eventually affect the template's status.`——持续收到负面反馈或低互动，最终会影响模板状态。**把这三档与"待定"并列，本篇的读法是：评级是过程量，不是准入资格**。新模板一开始没有分，老模板的分也会变。

三档的英文名 GREEN／YELLOW／RED 在本篇只作为取值名出现，不翻译成"通过／警告／禁止"；它们与 11.4 的号码档位不是同一套评分，下面立即分开。

## 11.2 模板暂停：三档时长、编辑重审与申诉时限

《Template pausing》是模板级计时的唯一来源。时长写成三档：`Pausing durations are as follows: - 1st Instance: Paused for 3 hours - 2nd Instance: Paused for 6 hours - 3rd Instance: Disabled When a message template is paused (status of **Paused**) it can't be sent to customers, so you should halt any automated messaging campaigns that rely on that template.`——首次三小时、第二次六小时、第三次停用；同一句还写明暂停期间无法发送，因此依赖该模板的自动化活动应当停下。**这一句把"暂停"与"停用"分成两档后果，且第三次不再给时长**；本篇照录，不写"每次都是几小时"。

暂停期间的接口行为：`Although you won't be charged for attempting to send a paused message template to a customer, and the attempt won't count against your messaging limit, the API will reject these attempts anyway.`——不会计费、不计入消息限额，但接口仍会拒绝这些发送请求。恢复：`You should only resume these campaigns when the template's status has been set to Active again.`——只有在模板状态回到 Active 之后才应恢复活动。编辑路径：`You may wish to edit a paused template if you feel that editing its content will reduce the amount of negative feedback it may receive and increase user engagement.` 与 `Keep in mind, however, that once you edit a message template and resubmit it for approval, its status will change to In Review and it can't be sent to customers again until it has been re-approved and its status set to Active.`——可以编辑内容以减少负面反馈，但编辑并重新提交后状态会变为 In Review，重新批准前不能发送。

影响范围：`Pausing will initially not impact the business phone number from which the message template was sent.` 与 `Other high quality message templates can continue to be sent from the phone number.`——暂停最初不影响发出该模板的号码，其他高质量模板仍可从同一号码发送。这句是模板级与号码级之间的关键界线：**模板被暂停，不等于号码被降级**（号码档位在 11.4）。再犯与恢复：`However, it is recommended that you halt any campaigns that rely on a template that has been paused until it is unpaused, because the APIs will reject your requests anyway.`——建议在模板恢复前停掉依赖它的活动，因为接口反正会拒绝；`Similar to pause notifications, you will be notified by WhatsApp Manager notification, email, and webhook once the template's status has been set to Active.`——恢复为 Active 时会通过管理端通知、邮件与 webhook 告知。

适用时间与申诉：`Applies to all businesses starting October 12, 2023.`——自 2023 年 10 月 12 日起适用于所有业务；`If an approved template has become disabled, you may also edit it and resubmit it for approval.`——已被批准的模板若变为停用，也可以编辑后重新提交；`The appeal will be reviewed and a decision made within 24 hours.`——申诉会在 24 小时内审查并作出决定。这一句与 12.1 的 24 到 48 小时构成附表 F 里"申诉时限"那一行的两侧：**模板级写"24 小时内"，账号级写"通常 24 到 48 小时"**，本篇并列而不取一个折中值。

## 11.3 模板质量 webhook：句子层没有可引句

《message_template_quality_update webhook reference》是模板质量更新的 webhook 参考页。它的正文以取值说明为主，池内可引的几句都带行内代码，本篇不整句引用。它在本篇的作用只有两个：一是确认"评级变化会以 webhook 形式推送"这件事有独立的参考页；二是它的取值集合（高质量、中质量、低质量与待定四档）按另一条通道进了附表 D，读者要核对取值名应当去 D，而不是在本篇正文里找引句。**这一页与 11.1 是两页**：11.1 写评级判据，这一页写推送字段；本篇不把两页混成一张表。

## 11.4 号码质量 webhook：另一套档位，不是模板评级

《phone_number_quality_update webhook reference》处理的是**业务电话号码**的质量档位，与 11.1 的模板评级是两套对象。它的可引句同样都带行内代码，本篇不整句引用，只登记三件事：其一，取值里有"尚未设置"这一档，表示该号码还没有用来发过消息（该句原文带行内代码）；其二，另有"注册中"这一档，表示号码仍在注册流程里（同）；其三，这些取值与模板的 GREEN／YELLOW／RED 不是同一套枚举。本篇据此明确：**模板质量、号码质量、账号营销状态是三个不同层级的开关**，分别由 11.1、本节与 10.2 描述，任何一个层级的变化都不能推断另外两层。

# 12 账号级执法：限制、申诉与"批量"的定性

前三章讲的是消息与模板的可计量状态，这一章讲账号。官方在这一层用的动词明显更多：限制、停用、下架（offboard）、封禁、申诉、复核。本篇把它们按页面分开登记，不合并成"封号"一个词。

## 12.1 执法页：四个动词、一条申诉链与一个时限

《WhatsApp Business Platform policy and spam enforcement》先给执法理由：`We might also limit or offboard your business from WhatsApp if your account receives excessive negative feedback from users.`——账号收到过多用户负面反馈时，平台也可能限制或下架你的业务。注意这句的动词是"限制或下架"，不是"封禁"；封禁（ban）在本篇语料里另有其页（12.4、12.5）。

申诉的适用范围写得很宽：`Restricted or disabled accounts can still appeal or acknowledge issues.`——被限制或停用的账号仍然可以申诉或确认问题。复核机制：`When a business appeals a violation, the WhatsApp team reviews the appeal against the violation policy to make a decision on if the violation should be reconsidered.`——平台按违规政策复核申诉，判断该违规是否应被重新考虑。结果与例外：`This review might result in WhatsApp reversing the violation. **Warning:** **Note:** Not all spam violations might be appealed.`——复核可能撤销违规，但并非所有垃圾信息违规都可申诉。时限：`The appeal review decision will be sent via Business Manager and typically takes 24 to 48 hours.`——决定通过 Business Manager 送达，通常需要 24 到 48 小时。另有一条等待规则：`Businesses might have to wait until the restriction period ends to begin messaging again.`——业务可能必须等到限制期结束才能重新发消息。

把这几句连起来，本篇给出执法页的口径：**申诉是"请求复核"，不是"请求解封"；复核可能维持、可能撤销，且存在不可申诉的类别**。正文不使用"申诉一定成功"或"申诉期间可以继续发"这类表述。

## 12.2 违规清单页：一份列表，四句可引

《WhatsApp Business Platform Policy Violations》是违规类别的清单页，正文以分组列表为主。可引的只有总起句与少数条目：`The list below outlines specific policy violations a business could receive.`——下面列出业务可能收到的具体违规；`This includes, but is not limited to, the sale of counterfeit products, such as goods that copy the trademark (name or logo) and/or distinctive features of another company's products to imitate a genuine product.`——售假条目把"复制商标与显著特征以冒充正品"写进定义。另有一句豁免：`Manufacturers and healthcare services who do not engage in direct sale are allowed.`——不从事直销的制造商与医疗服务提供者是被允许的（同一句在该页出现两次，本篇按一次登记）。

这一页在本篇的作用是"类别标签"，不是"判定方法"：它告诉你违规有哪些名字，不告诉你平台怎么发现。发现机制在下一节。

## 12.3 违规检测页：加密之下，平台用什么识别

《About detecting violations》回答的是"端到端加密之后，平台还怎么发现违规"。第一句是前提：`Because WhatsApp messages are end-to-end encrypted, we use information about you from WhatsApp users, information from your business account and registration and other information to identify when a business or Business Solution Provider may be in violation of those policies.`——因为消息加密，平台使用来自用户的信息、业务账号与注册信息以及其他信息来识别业务是否违规。第二句把举报写成检测输入：`When a user blocks or reports a business or a message from a business When a business is blocked or reported by a user, WhatsApp receives the last five messages sent to the user by the reported business, the reported group or user ID, information on when the message was sent, and the type of message sent (image, video, text, etc.).`——业务被用户屏蔽或举报时，平台会收到该业务发给该用户的最近五条消息、被举报的群或用户标识、发送时间与消息类型。

第三句是资料审核：`When profile information is submitted or updated WhatsApp may also conduct reviews using user information to determine whether an account is in violation of the Business or Commerce policies.`——提交或更新资料时，平台也可能用用户信息做审核，判断账号是否违反商业或商务政策。**这三句合起来说明：举报在平台侧不只是"用户投诉"，它是一条带内容的检测输入**；而 2.1 的"举报不必然导致处置"讲的是结果，两者不矛盾——材料进去了，不等于判定成立。

## 12.4 封禁申诉页：帮助中心把业务推给商业帮助中心

《About ban appeals on the WhatsApp Business Platform》是一页很短的转介页。页首就写明 WhatsApp Support 不处理商业平台上的账号封禁与限制（该句是页首横幅，本篇只转述）；可引的三句都是指向：`Businesses using the WhatsApp Business Platform can consult the Meta Business Help Center for most of their support needs.`——使用商业平台的业务可以到 Meta 商业帮助中心寻求大部分支持；`To learn how to view policy violation details for your WhatsApp Business account and how to file an appeal, read this article .`——查看违规详情与提交申诉要读另一篇文章；`To learn more about policy and spam enforcement on the WhatsApp Business Platform, read the Meta Developer Documentation here .`——政策与垃圾信息执法的更多说明在开发者文档。

这一页的价值在于"入口归属"：**账号封禁的申诉不在消费者帮助中心，而在商业帮助中心与开发者文档那一侧**。本篇因此不写"在帮助中心提交申诉"，也不替这一页补任何步骤。

## 12.5 "批量与自动化"：一句定性、一条时间线

《Unauthorized use of automated or bulk messaging on WhatsApp》是本篇语料里对"群发"定性最直接的一页。定性句：`Our products are not intended for bulk or automated messaging, both of which have always been a violation of our .`——产品并非为批量或自动化消息设计，两者一直违反条款（该句末尾的链接词在抓取件里丢失，本篇照原文引用并在此说明）。整体立场：`This is a challenge that requires a holistic approach and WhatsApp is committed to using the resources at its disposal–including legal action–to prevent abuse that violates our Terms of Service, such as automated or bulk messaging, or non-personal use.`——这需要整体性方法，平台会动用包括法律行动在内的资源，阻止自动化或批量消息等滥用。

法律行动的时间线有两句：`This is why in addition to technological enforcement, we also take legal action against individuals or companies that we link to on-platform evidence of such abuse.`——除技术执法外，也会对平台内证据指向的个人或公司采取法律行动；`In addition, beginning on December 7, 2019, WhatsApp will take legal action against those we determine are engaged in or assisting others in abuse that violates our Terms of Service, such as automated or bulk messaging, or non-personal use, even if that determination is based on information solely available to us off our platform.`——自 2019 年 12 月 7 日起，对判定为从事或协助此类滥用者采取法律行动，即使判定只基于平台外信息。平台外信息的例子：`For example, off-platform information includes public claims from companies about their ability to use WhatsApp in ways that violate our Terms.`——公司公开宣称能以违反条款的方式使用 WhatsApp。

这一页在本篇的定位是"定性页"：它不给检测方法、不给阈值、不给操作细节，只给"这违反条款"与"可能被诉"两件事。本篇照此登记，不补充任何执行细节。

# 13 政策层：三条文本与一份透明度报告

法律与政策页是本篇的最后一层。它们不描述界面，也不描述接口，只写"谁必须做什么"。这一章按"消息指南—商业政策—用户条款—透明度报告"的顺序，把与本轴直接相关的句子挑出来；挑不出来的页面也点名，说明为什么。

## 13.1 消息指南：骚扰被定义成"反复的非自愿接触"

《WhatsApp Messaging Guidelines》是面向所有用户的行为准则。执法依据句：`WhatsApp may take action against violations of our terms of service, our policies, or these Guidelines, based on the limited information available to WhatsApp, including basic account, group, and community profile information, as well as messages reported by other users.`——平台可以依据有限信息对违规采取行动，包括账号、群与社区资料，以及被其他用户举报的消息。举报入口：`Users can report WhatsApp contacts, groups, communities, status updates, or specific messages that potentially violate these Guidelines.`——用户可以举报联系人、群、社区、状态更新或具体消息。

"骚扰"的定义句在这一页：`Do not engage in fraud or spam This includes repeated unwanted contact with WhatsApp users, and activity that scams or defrauds WhatsApp or our users through purposeful deception or impersonation.`——欺诈与垃圾信息包括**对 WhatsApp 用户的反复非自愿接触**，以及通过 purposeful deception 或冒名去诈骗的行为。这句话是本篇给"骚扰"的唯一官方定义，它把要件落在"反复"与"非自愿"两个词上；本篇不把它扩展成"任何不受欢迎的消息"，因为官方没有这样写。

自动化与人工的分工也写在这一页：`How We Apply the Messaging Guidelines WhatsApp may use both automated processing techniques and human review teams to detect and review information available to us including account, group, and community profile information, as well as reported messages, for potential violations of these Guidelines.`——平台可能同时使用自动化处理与人工审核团队来检测与复核信息；`Human Review Teams When an account, group or community requires further review, our automated systems send it to a human review team to make the final decision.`——需要进一步复核时，自动系统会把它交给人工团队作最终决定；`Automation also helps us prioritize and expedite reviews by routing potentially violating accounts, groups, or communities that may require further review to human reviewers who have the right subject matter and language expertise.`——自动化还帮助平台把可能需要进一步复核的账号、群或社区**优先并加速**分派给具备相应主题与语言专长的人工审核者。**最后这一句里的 expedite 指的是分派加速，不是对用户催办的承诺**，这正是附表 F 里与 2.1 "外部联系不加快审查"并列的那一处差异。

政府请求与本地法：`Local Law Violations When governments believe accounts, groups, or communities on WhatsApp violate local law, they may request that we review the account information available to us.`——政府认为账号、群或社区违反本地法时，可以请求平台复核其掌握的信息；`We always assess the legitimacy and completeness of a government request before taking any action.`——平台在采取任何行动前都会评估请求的合法性与完整性。

## 13.2 商业政策：把"尊重屏蔽"写成业务义务

《Business Policy》是本篇最长的一页，但与本轴直接相关的只有几组。联系的前提：`You may only contact people on WhatsApp if: (a) they have given you their mobile phone number or username; and (b) you have received opt-in permission from the recipient confirming that they wish to receive subsequent messages or calls from you.`——只有在（a）对方给了你号码或用户名、且（b）你取得了确认其愿意接收后续消息或通话的 opt-in 时，才能联系。拒绝的义务：`You must respect all requests (either on or off WhatsApp) by a person to block, discontinue, or otherwise opt out of communications from you via WhatsApp, including removing that person from your contacts list.`——必须尊重对方在 WhatsApp 内外提出的屏蔽、停止或退出请求，包括把该人从联系人名单中移除。这一句是 8.2 那句"屏蔽会把号码移出名单"的政策依据：**用户侧的一次屏蔽，在政策层被写成业务必须兑现的义务**。禁止行为：`Do not confuse, deceive, defraud, mislead, spam, or surprise people with your communications.`——不得用通信去混淆、欺骗、诈骗、误导、发垃圾信息或制造意外。

模板与窗口：`We have the right to review, approve, pause and reject any Message Template at any time.`——平台有权随时审查、批准、暂停与拒绝任何模板；`You may reply to a user message without use of a Message Template as long as it’s within the 24-hour customer service window , which opens and resets with each user message.`——在 24 小时客户服务窗口内可以不用模板回复，窗口随每条用户消息开启并重置；`Outside the 24-hour customer service window, you may only send messages via approved Message Templates.`——窗口之外只能通过已批准的模板发送。自动化句：`You may use automation when responding during the 24-hour window, but must also have available prompt, clear, and direct escalation paths.`——窗口内可以用自动化回复，但必须同时提供及时、清晰、直接的升级路径。

执法与终止：`Enforcement and Updates We may limit or remove your access to or use of WhatsApp for Business, or limit your ability to send messages, if you receive significant amounts of negative feedback, cause harm to WhatsApp or our users, violate or encourage others to violate our terms or policies, or to maintain a high quality experience on WhatsApp, as determined by us in our sole discretion.`——收到大量负面反馈、造成伤害、违反政策等情形下，平台可以限制或移除你的访问、限制发送能力；`Our systems may limit the amount of messages a business can send or calls a business can initiate, based on this user feedback.`——平台系统可以依据用户反馈限制业务可发送的消息量或可发起的通话量；`If you use or operate a service which utilizes WhatsApp in violation of our terms or policies, such as messaging people at scale in an unauthorized manner, we have the right to limit or remove your access to WhatsApp for Business.`——以未经授权的方式大规模联系他人等违反条款的用法，平台有权限制或移除访问；`If we terminate your account for violations of our terms of service , the Business Messaging Policy, or the WhatsApp Messaging Guidelines , we may prohibit you and your organization from all future use of WhatsApp products and services.`——因违规终止账号后，可以禁止你与你的组织未来使用全部 WhatsApp 产品与服务。**这四句是本篇"处置阶梯"的政策底**：从限流到终止再到未来禁用，动词逐级加重，但都没有给出量化阈值。

## 13.3 用户条款与商业条款：一条横幅与一条年龄线

《Terms of Service》在本轴只贡献两组句子。第一组是安全承诺与行动：`We work to protect the safety, security, and integrity of our Services.`、`This includes appropriately dealing with abusive people and activity violating our Terms.` 与 `If we learn of people or activity like this, we will take appropriate action, including by removing such people or activity or contacting law enforcement.`——保护服务的安全、完整与安保，处理违反条款的滥用者与行为，并在知悉时采取适当行动（包括移除或联系执法）。第二组是年龄线：`You must be at least 13 years old to register for and use our Services on your own (or such greater age required in your country or territory).`——注册与自行使用服务须至少 13 岁，或在所在国家/地区要求更高的年龄。

《WhatsApp Business Terms of Service》在本篇语料里只有一句可引，而且那一句是站点横幅（`Now you can make or answer calls from WhatsApp Web.`——现在可以在 WhatsApp Web 上拨打或接听电话），与本轴无关。本篇在 1.3 已经交代过这一页：它被多处引用为上位条款，但正文没有进入本篇句子层。**这一节因此只登记"它存在、它被引用、它不可引"三件事**，不代它拟写任何条款，也不拿横幅句去支撑任何结论。

## 13.4 透明度报告：数字覆盖的是 Facebook 与 Instagram

《Community Standards Enforcement Report》是 Meta 的社区标准执行报告。它的定位句：`This is why we have the Community Standards , which define what is and is not allowed in our community.`——社区标准定义了社区里允许与不允许的内容；发布节奏与覆盖面：`H1 2026 Report We publish the Community Standards Enforcement Report semi-annually, with data broken down quarterly, to more effectively track our progress and demonstrate our continued commitment to making Facebook and Instagram safe and inclusive.`——每半年发布一次、按季度拆分数据，目标是让 Facebook 与 Instagram 保持安全与包容。这句里的两个产品名就是本节的结论：**这份报告的数字不是 WhatsApp 的举报数据**。

本篇不引用报告里的具体百分比，因为那些数字的对象是 Facebook 与 Instagram 的内容执法，与本篇的 WhatsApp 举报链没有对应关系。与 WhatsApp 相关的公开数字在另一页：印度月度报告（`The reports detail user complaints received and the corresponding action taken by WhatsApp, as well as WhatsApp’s own preventive actions to combat abuse on the platform.`——报告详列收到的用户投诉与相应处置，以及平台自身的预防行动）。两页的覆盖范围不同，本篇在 13.4 并列而不相加。

# 14 安全中心与用户责任页：官方的"自我保护"叙事

这一组页面面向普通用户，语气与前面的政策页不同：它们不写"业务必须"，而写"你可以"。本篇把它们放在最后，是因为它们给前三章的动作提供了一个日常语境——举报、屏蔽、退出不是应急手段，而是产品默认提供的控制项。

## 14.1 安全中心：三层默认防护的自我描述

《Security Features, Safety Tools & Tips》是 Meta 的安全功能总览页。它的基调句：`Built-in layers of security give you the peace of mind to focus on connecting with family and friends.`——内建的多层安全让人可以专注于与亲友联系。第一层是默认隐私与自动垃圾检测：`Default privacy Automatic spam detection We catch and remove most spam and scam accounts before they can even reach you or before anyone reports them.`——平台会在大多数垃圾与诈骗账号接触到用户、或被举报之前就发现并移除它们。这一句是官方对"预防"的最强表述，但它用的是 most，不是 all。

第二层是主动提醒：`Proactive security alerts We prompt you to verify your identity if we detect any suspicious or unauthorized attempts to take over your account.`——检测到可疑或未经授权的账号接管尝试时，平台会提示你验证身份。第三层是用户可用的控制项与举报入口：`Report problematic behavior Reporting messages, people or businesses helps us keep WhatsApp safe for everyone.`——举报消息、个人或业务有助于平台保持安全。另有一句关于本地留存的：`Past conversations are safe on your phone and cannot be read by any one trying to access your account.`——过去的会话安全地留在你的手机上，任何试图访问你账号的人都读不到。

这一页没有给出任何检测阈值或处置时限，本篇因此只把它当"叙事页"：它说明官方如何描述自己的防线，不构成 12.x 那一套执法规则的补充。

## 14.2 负责任使用页：把"只联系愿意被联系的人"写成用户准则

《How to use WhatsApp responsibly》是本篇语料里对"群发为什么会出事"写得最直白的一页，虽然它的对象是普通用户。总则：`All users of WhatsApp should review the following guidelines to ensure responsible use of WhatsApp.`——所有用户都应遵守以下准则以确保负责任地使用。第一条就是联系对象：`Best practices Communicate with known contacts : Only send messages to those who have contacted you first or have requested you contact them on WhatsApp.`——只给先联系过你、或请求你联系他们的人发消息；`It's best to give contacts your phone number so they can message you first.`——最好把号码给对方，让对方先发。

入群与退群：`Ask for permission and respect boundaries : You should get permission from contacts before you add them to a group.`——把联系人加入群之前应先取得许可；`If you add someone to a group and they remove themselves, honor their decision.`——如果对方自行退出，尊重这个决定。转发：`We created a label for all forwarded messages and limit the number of times you can forward messages as a way to encourage users to reconsider before sharing.`——平台给所有转发消息加了标签，并限制转发次数，目的是让人在分享前再想一次；`Think twice before forwarding messages .` 与 `If you aren’t sure whether something’s true or don’t know who wrote the message, we don’t recommend forwarding it.`——转发前先想清楚，不确定就不要转。

"应当避免的做法"这一段与业务侧直接相关。后果句：`Practices to avoid Using WhatsApp in any of the ways listed below may cause your account to be banned.`——以下用法可能导致账号被封。第一条：`Unwanted messages : If a contact asks you to stop messaging them, you should remove the contact from your address book and refrain from contacting them again.`——对方让你停止时，应把该联系人从通讯录移除并不再联系。检测方式：`WhatsApp uses both machine learning technology and reports from users to detect and ban accounts that send unwanted automated messages.`——平台同时用机器学习与用户举报来发现并封禁发送不受欢迎的自动化消息的账号。广播列表：`Over-using broadcast lists : Messages sent using a broadcast list will only be received when users have added your phone number to their contact list.`——广播列表的消息只有在用户把你的号码存进通讯录时才会收到；`Please keep in mind, frequent use of broadcast messages may lead people to report your messages, and we ban accounts that are reported multiple times.`——频繁使用广播消息可能导致收件人举报，而**被多次举报的账号会被封禁**。

最后两句把"通讯录来源"也写进禁止项：`Using contact lists that aren’t yours : Don’t share phone numbers without consent or use data obtained from illicit sources to message users on WhatsApp or add them to groups.`——不要未经同意分享号码，也不要用非法来源的数据去发消息或拉群；`Harvesting personal information : Avoid any automated extraction of information from WhatsApp for any unpermitted purposes.` 与 `The acquisition of information from users in this way, including phone numbers, user profile pictures, and statuses from WhatsApp violates our .`——不要为未经许可的目的自动抓取信息，这类获取号码、头像与状态的行为违反条款。**这三句合起来，本篇认定"名单从哪来"是官方判定的一条独立红线**，它与 9.x 的计费、11.x 的质量是不同维度。

## 14.3 印度专页：举报数据与申诉渠道的具体化

《Ensuring User Safety in India on WhatsApp》是地区专页，但它提供了本篇语料里少见的"举报数据"描述。申诉渠道：`Grievance Officer : We have a Grievance Officer based in India, who can be contacted if a user has a concern about their experience and is unable to report it through other channels.`——印度设有申诉专员，用户在其他渠道无法举报时可以联系。月度报告：`Monthly Reports: In compliance with IT Rules 2021, WhatsApp regularly releases its monthly report in India that highlights steps taken by us to ensure user safety on WhatsApp.`——依 IT Rules 2021 定期发布印度月度报告，说明为保障用户安全所采取的措施；报告内容：`The reports detail user complaints received and the corresponding action taken by WhatsApp, as well as WhatsApp’s own preventive actions to combat abuse on the platform.`——报告详列收到的用户投诉与 WhatsApp 相应的处置，以及平台自身的预防行动。可获取性：`You can access the India Monthly Reports here .`——报告有公开入口。

儿童性剥削内容的处理有一段：`WhatsApp relies on all available unencrypted information, including user reports, to detect and prevent this kind of abuse, and we are constantly improving our detection technology.`——平台依赖所有可用的未加密信息（包括用户举报）来发现与阻止此类滥用，并持续改进检测技术；`We also report violating content and accounts to the National Center for Missing and Exploited Children (NCMEC), which refers these CyberTips to Law Enforcement globally and in India, specifically to the National Crime Records Bureau (“NCRB”).`——违规内容与账号会上报 NCMEC，由其转交执法。这两句与 12.3 的检测页结构相同：举报是输入，未加密信息是可用面。

## 14.4 安全使用页：默认隐私的三档与举报的日常化

《How to stay safe on WhatsApp》篇幅较长，但它对本篇的价值集中在三处。第一处是安全的总起：`We want you to know about the tools and features we've designed to help you stay safe while using WhatsApp.`——平台设计了工具与功能帮助用户保持安全。第二处是条款与封禁的关系：`Our Terms of Service outline prohibited activities, which include sharing content (in the status, profile photos, or messages) that's illegal, obscene, defamatory, threatening, intimidating, harassing, hateful, racially or ethnically offensive, or instigates or encourages conduct that would be illegal, or is otherwise inappropriate or violates our Terms of Service.`——条款列出被禁止的活动，包括分享非法、淫秽、诽谤、威胁、恐吓、骚扰、仇恨等内容；`We may ban a user if we believe that user is violating our Terms of Service.`——平台认为用户违反条款时可以封禁账号。**"骚扰"在这句里是内容属性，不是行为频率**，与 13.1 的"反复非自愿接触"是两处不同定义；本篇并列登记，不合并，这一组差异只写在本节，F 表不设对应行。

第三处是默认隐私：`By default, WhatsApp sets your privacy settings to allow: Any user to see your profile photo, read receipts and when you're online.`——默认允许任何用户看到头像、已读回执与在线状态。这一句解释了 2.1 里"屏蔽前发布的状态仍可见"的前提：默认可见性本来就是宽的，屏蔽是在宽默认上做减法。

举报与屏蔽的日常入口：`Block or report contacts and messages We encourage you to report problematic content and contacts to us.` 与 `You can control who you interact with by blocking specific contacts or reporting messages or contacts on WhatsApp.`——平台鼓励举报问题内容与联系人，用户可以通过屏蔽特定联系人或举报消息来控制与谁互动。一次性媒体：`When you receive a view once photo or video, you can report the account to us directly from the media viewer.`——收到一次性查看的照片或视频时，可以直接从媒体查看器举报该账号。

最后是一句与 5.4 同文的机制句：`Block high volumes of unknown messages As an advanced security measure, you can block high volumes of unknown messages to help protect your account and improve device performance.`——作为高级安全措施，可以屏蔽高量未知消息以保护账号并改善设备性能。同一机制在两页各写一次，本篇按两页分别登记，不合并成"两个功能"。

# 15 限额、告警与账号状态：业务侧的三个信号面

这一章的四个页面都属于"平台告诉业务什么"。它们不描述用户动作，而描述平台推给业务的状态：消息限额、账号告警、账号变更、直发内容的完整性。四页的取值大多写在表格里，句子层可引的很少，本篇如实登记。

## 15.1 消息限额：按业务组合计算，由号码共享

《Messaging Limits》是限额的总说明。定位：`This document describes messaging limits for the WhatsApp Business Platform.`——本文描述商业平台的消息限额。计算层级：`Messaging limits are calculated and set at the business portfolio level and are shared by all business phone numbers within a portfolio.`——限额按业务组合计算与设置，由组合内所有业务电话号码共享；后果：`This means that if a business portfolio has multiple business phone numbers, it's possible for one number to consume all of the portfolio's messaging capability within a given period.`——组合里有多个号码时，一个号码有可能在某段时间内用光整个组合的消息能力。

自动扩容的另一半：`Additional messaging limit increases for your business portfolio can now happen automatically, via [automatic scaling](#automatic-scaling). ### Denials If your request is denied, Meta maintains your business phone number's messaging limit at its current level and notifies you via email and developer alert.`——组合的额外限额提升现在可以通过自动扩容自动发生。**这一页把"限额"的对象写成业务组合，而不是单个号码**，这与 10.4 的模板数上限（按消息账号）、9.4 的按人上限（按收件人）是三个不同层级，本篇不把它们相加。

提升被拒时的处理写在该页的拒绝段：限额维持在当前水平并通知业务（该段原文带小标题，本篇只转述）。本篇不写任何"如何提高限额"的操作步骤。

## 15.2 账号告警 webhook：告警是通知，不是裁定

《account_alerts webhook reference》是 account_alerts 的参考页。它的正文几乎全是取值与告警文案，句子层可引的只有一句指向身份验证入口的说明（该句是操作指引，本篇按红线只登记不重述）。本篇在这一节只做结构说明：这一页把告警分成"仅供参考、无需动作"与"可能需要动作"两类（两个取值名在原文里带行内代码，本篇只转述其含义），并把每条告警的原因与建议动作写在描述字段里；具体取值集合按另一条通道进了附表 D，读者核对取值名应当去 D。

## 15.3 账号变更 webhook：变更事件不是处罚

《account_update webhook reference》是 account_update 的参考页。它描述的是账号生命周期事件，不是处罚。可引的一句是共存迁移：`For WhatsApp Business app coexistence onboarding, Meta converts the client's existing WhatsApp Business account into a backward-compatible Messaging account and shares it with your partner business.`——商业应用共存入驻时，Meta 会把客户现有的商业账号转换为向后兼容的消息账号，并与合作方业务共享。其余取值（删除、广告账号关联、条款签署、应用安装与卸载等）都在表格里，原文带行内代码，本篇不整句引用。

这一页与 12.x 的关系需要说清：**账号被删除或被限制，在本页是一个事件通知，在 12.1 是一个执法结果**；两页描述同一件事的不同侧面，本篇不把"收到变更事件"当作"已经被处罚"。

## 15.4 直发内容完整性：营销内容混入 utility 会被识别

《Integrity and content guidelines》处理的是 Direct Send 场景下的类别误用。可引两句：`If Meta detects marketing content sent through Direct Send, it notifies you.`——Meta 检测到通过 Direct Send 发送的营销内容时会通知你；`Persistent misuse results in restrictions to Direct Send access. ### Notification of non-compliant content Meta notifies you when it detects marketing content in messages you send in the utility category through Direct Send.`——持续误用会导致 Direct Send 访问受限。

这一页的其余内容（审核请求、每模板一次复核、审核期间不暂停访问、达到上限后用错误码拒绝）都以列表与加粗小标题写成，句子层不可整句引用，本篇只登记其结构：**类别误用的处置是"标记—通知—复核—限额"四步，而不是直接封号**。这与 12.1 的账号级动词属于不同层级，本篇分节登记。

# 16 两个相邻表面：商品目录与通话设置

这两页在语料里属于"相邻表面"：它们不是举报链的一部分，但都带有"用户可以对业务做什么"的入口。本篇只取与本轴直接相关的句子，不展开产品介绍。

## 16.1 商品目录：商品级的举报入口

《Catalogs overview》的主体是商品目录的产品说明。可引的定位句：`Each catalog item includes details like name, description, price, images, and availability.`——每个目录项包含名称、描述、价格、图片与可售状态；`Once connected, this inventory becomes the foundation for all commerce interactions on WhatsApp.`——连接后这份库存成为 WhatsApp 上所有商务互动的基础。购物车的两条硬规则：`Customers can add up to 99 units of each single catalog item to a shopping cart, but there is no limit on the number of distinct items that can be added to a cart.`——每个商品最多可加 99 件，不同商品数量不设上限；`Once a cart has been sent, no edits can be made.`——购物车发出后不能再改。

与本轴直接相关的是商品级举报：`If a policy-violating item is added, the product is flagged and the business is directed to the WhatsApp Commerce Policies. - **Reporting options at the product catalog level**: Users can report a specific product they receive through a message.`——用户可以通过消息举报收到的某个具体商品；`If a user reports a product, it is reviewed for violations.`——被举报的商品会被审查是否违规。**这把举报对象从"消息、成员、群、账号"扩展到了"商品条目"**，是 6.1 三分法之外的第五类对象；本篇在此补记，但不改写 6.1 的分类（那一页只讲群内对象）。

## 16.2 通话设置：未经请求的呼叫也是被限制的对象

《Configure Call Settings》是 Calling API 的设置页。定位：`Use these endpoints to view and configure call settings for the Calling API.`——用这些端点查看与配置通话设置。界面侧的一句：`WhatsApp client apps render the call button icon in both the business chat and business chat profile.`——客户端会在业务会话与会话资料页渲染通话按钮图标。

与本轴相关的是"未经请求的呼叫"：`WhatsApp users cannot make unsolicited calls to your business.`——用户可以不再对你的业务发起未经请求的呼叫（该句是隐藏全部入口这一取值的效果说明）。时间限制：`User-initiated calls are unavailable outside of the weekly hours and holiday schedules you set.`——在你设置的每周时段与假日表之外，用户发起的呼叫不可用；回拨：`The user will also be shown the next available calling slot on the option screen.`——选项屏会显示下一个可用的通话时段。另有一句运营侧：`Please identify and address the cause of user-initiated calls not being picked up.`——请查明并处理用户发起的呼叫未被接听的原因。

这一节的意义在于补齐"骚扰"的另一个载体：**消息之外还有呼叫，呼叫也有许可与时段两层控制**。本篇不把它与消息限额混为一谈，也不提供任何呼叫配置步骤。

# 17 结论：五条可以分开读的口径线

把这十七章节回到开头那五十几个问题，本篇的结论不是"WhatsApp 如何反 spam"这种单句，而是五条彼此不能替换的口径线。

**第一条，动作线。** 用户侧只有屏蔽、举报、退出三个动作，三者后果不同：屏蔽切断一对一接收（2.1、6.1），举报把材料送到平台但不改变接收（2.1、4.1），退出只改变在场（6.1、6.2）。官方从未把三者写成同一件事，本篇也不合并。

**第二条，材料线。** 举报送出去的是"最近若干条消息＋标识＋时间＋类型"，条数是上限而非保证（2.1、6.1、12.3）；转发历史之后被举报的是转发者（2.1、6.1）。平台在加密之下依赖举报与账号资料做检测（12.3、14.3），这条线解释了为什么"举报"在官方文本里同时是用户动作与检测输入。

**第三条，计时线。** 官方文本里至少有四个互不替代的时钟：营销计费的五天（9.1、9.2）、按人上限的动态窗口（9.4）、重试冷却的一天（9.4）、模板暂停的三档时长与账号暂停的三天（10.2、11.2）。把它们压成一个"限额"是对至少四页的同时误读。

**第四条，层级线。** 模板质量（11.1）、号码质量（11.4）、账号营销状态（10.2）、账号执法状态（12.1）是四个不同层级；模板被暂停不影响号码（11.2），号码档位不等于模板评级（11.4），账号被限制也不等于模板被暂停（10.2）。本篇在 11.4 与 12.1 各写了一次"不能互相推断"。

**第五条，文本线。** 同一件事在四层文本里各有写法：帮助中心写"你会看到什么"，开发者文档写"接口返回什么"，商业政策写"业务必须做什么"，法律条款写"平台可以做什么"（13.1—13.4）。E 表的十五个维度就是按这条线排的；F 表的二十一则议题记录的是同一议题在两层文本里写法不一致的地方。

最后一句留给范围：本篇只登记官方写下的句子与数字，不提供任何发送方法、名单来源、频率参数或规避路径；凡官方未写明的，本篇一律写"官方未在本篇语料里写明"，而不是替它补一个答案。

# 附表 A 官方页面登记清单与本篇引用位置（64 行）

登记口径与系列前篇一致：**凡本篇抓取的页面，一律登记**。全表 **63** 个登记位，按站点分为 Meta 开发者文档 22、WhatsApp 帮助中心 35、法律与政策页 6。取回状态由脚本实测各层正文的非空白字符数得出，分档统计：已取回 61、正文未取回 2。其中**可引用 61 页、不可引用 2 页**：不可引用的页面正文未取回、只有一句地域限定提示，或取回的是抓取残骸（页面不存在的脚手架），**其内容全篇未被引用**，本篇只在附表 F 里登记这条事实。

帮助中心页的正文取回长度按裁剪后正文计：抓取原始页含平台页签（Web／Windows／Mac／iOS／Android）与固定尾栏，这些行不计入长度，也不作为引文出处。Meta 商业帮助中心的页面正文从页面内嵌的转义 JSON 载荷里抽取，抽取残骸（单独的 u 记号与 DOM 类名）整行剔除，不做猜测修补，被剔除的行也不作为引文出处。

第 4 列「本篇引用位置」的小节号是实测的，不是事先规划的：由 _tmp/wa6_postitle.pl 扫成稿分片里的页名标记（正文每点名一次某页的官方标题就留下一枚这种内部标记，展开后成为标题）得出，写进 _tmp/w6_a5_postitle.tsv，再由 _tmp/wa6_pos.pl 拼进这一列；判据与 _tmp/wa6_cover.pl 同口径——**这一列只写标题点名的位置，所以按小节号翻过去一定能撞见这一页的官方标题**。章首（未进入任何小节的章引言）写作「第 N 章章首」；某页只在别处被整句引用而标题未点名的节不出现在这一列，那种句子按附表 C—F 的句序回查。

| 站点 | 页面标识 | 官方标题 | 本篇引用位置 | 正文状态 |
| --- | --- | --- | --- | --- |
| Meta 开发者文档 | `block_users_md` | Block users | 正文 1.1、7.1、附表 C（8 处）、附表 E（1 处）、附表 F（1 处） | 已取回（正文 15552 字符） |
| Meta 开发者文档 | `calling_call_settings_md` | Configure Call Settings | 正文 16.2、附表 B（3 处）、附表 C（15 处） | 已取回（正文 34016 字符） |
| Meta 开发者文档 | `catalogs_catalogs_overview_md` | Catalogs overview | 正文 16.1、附表 C（8 处） | 已取回（正文 5438 字符） |
| Meta 开发者文档 | `data_privacy_and_security_md` | Data Privacy & Security | 正文 4.3、附表 B（1 处）、附表 C（9 处） | 已取回（正文 5938 字符） |
| Meta 开发者文档 | `direct_send_integrity_and_content_guidelines_md` | Integrity and content guidelines | 正文 15.4、附表 C（5 处）、附表 E（2 处） | 已取回（正文 11902 字符） |
| Meta 开发者文档 | `getting_opt_in_md` | Get opt-in for WhatsApp | 正文 8.2 | 已取回（正文 2694 字符） |
| Meta 开发者文档 | `messaging_limits_md` | Messaging Limits | 正文 15.1、附表 C（2 处） | 已取回（正文 5493 字符） |
| Meta 开发者文档 | `policy_enforcement_md` | WhatsApp Business Platform policy and spam enforcement | 正文 12.1、附表 B（2 处）、附表 C（3 处）、附表 E（5 处）、附表 F（3 处） | 已取回（正文 6235 字符） |
| Meta 开发者文档 | `policy_enforcement_violations_md` | WhatsApp Business Platform Policy Violations | 正文 12.2、附表 E（1 处） | 已取回（正文 6533 字符） |
| Meta 开发者文档 | `reference_whatsapp_business_phone_number_block_api_md` | Base URL | 正文 1.3、7.2 | 已取回（正文 6197 字符） |
| Meta 开发者文档 | `support_error_codes_md` | WhatsApp error codes | 正文 7.3、附表 B（1 处）、附表 C（2 处）、附表 E（1 处） | 已取回（正文 32277 字符） |
| Meta 开发者文档 | `templates_marketing_templates_md` | Marketing templates | 正文 10.3、附表 C（1 处）、附表 F（1 处） | 已取回（正文 5873 字符） |
| Meta 开发者文档 | `templates_marketing_templates_per_user_limits_md` | Per-user marketing template message limits | 正文 9.4、附表 B（2 处）、附表 C（1 处）、附表 E（1 处） | 已取回（正文 2360 字符） |
| Meta 开发者文档 | `templates_overview_md` | Template fundamentals | 正文 10.4、附表 B（3 处）、附表 C（2 处） | 已取回（正文 13530 字符） |
| Meta 开发者文档 | `templates_template_pausing_md` | Template pausing | 正文 11.2、附表 B（4 处）、附表 C（1 处）、附表 E（4 处）、附表 F（2 处） | 已取回（正文 3506 字符） |
| Meta 开发者文档 | `templates_template_quality_md` | Template quality rating | 正文 11.1、附表 C（1 处） | 已取回（正文 2759 字符） |
| Meta 开发者文档 | `webhooks_reference_account_alerts_md` | account_alerts webhook reference | 正文 15.2、附表 D（4 处） | 已取回（正文 6980 字符） |
| Meta 开发者文档 | `webhooks_reference_account_update_md` | account_update webhook reference | 正文 15.3、附表 B（3 处）、附表 C（1 处）、附表 D（7 处）、附表 E（1 处） | 已取回（正文 23055 字符） |
| Meta 开发者文档 | `webhooks_reference_message_template_quality_update_md` | message_template_quality_update webhook reference | 正文 11.3、附表 D（2 处） | 已取回（正文 2586 字符） |
| Meta 开发者文档 | `webhooks_reference_messages_errors_md` | Errors messages webhooks reference | 正文 1.3、7.4 | 已取回（正文 2741 字符） |
| Meta 开发者文档 | `webhooks_reference_phone_number_quality_update_md` | phone_number_quality_update webhook reference | 正文 11.4、附表 D（4 处） | 已取回（正文 4236 字符） |
| Meta 开发者文档 | `webhooks_reference_user_preferences_md` | user_preferences webhook reference | 正文 8.1、附表 C（1 处）、附表 D（1 处）、附表 E（1 处） | 已取回（正文 4829 字符） |
| WhatsApp 帮助中心 | `faq_1003616827680090` | How to keep your groups and community safe | 正文 6.2、附表 B（4 处）、附表 C（18 处）、附表 E（2 处） | 已取回（正文 3663 字符） |
| WhatsApp 帮助中心 | `faq_1040401477781770` | What information does WhatsApp collect when reporting an issue | 正文 1.3、4.1 | 已取回（正文 874 字符） |
| WhatsApp 帮助中心 | `faq_1142481766359885` | How to block and report someone | 正文 2.2、附表 C（11 处）、附表 F（3 处） | 已取回（正文 7747 字符） |
| WhatsApp 帮助中心 | `faq_1148840052398648` | Privacy and security for business messages | 正文 4.2、附表 C（11 处） | 已取回（正文 3471 字符） |
| WhatsApp 帮助中心 | `faq_1313491802751163` | How to stay safe on WhatsApp | 正文 14.4、附表 C（7 处）、附表 E（1 处） | 已取回（正文 5126 字符） |
| WhatsApp 帮助中心 | `faq_1374700371009788` | About messaging and groups on parent-managed accounts | 正文 2.5、附表 C（2 处）、附表 E（2 处）、附表 F（2 处） | 已取回（正文 2501 字符） |
| WhatsApp 帮助中心 | `faq_1508178557633269` | About ban appeals on the WhatsApp Business Platform | 正文 12.4、附表 C（2 处） | 已取回（正文 665 字符） |
| WhatsApp 帮助中心 | `faq_1524953031793983` | About business broadcasts | 正文 9.2、附表 B（1 处） | 已取回（正文 1393 字符） |
| WhatsApp 帮助中心 | `faq_1561915401728939` | How to block and report someone on parent-managed accounts | 正文 2.5、附表 B（1 处）、附表 E（3 处）、附表 F（1 处） | 已取回（正文 1416 字符） |
| WhatsApp 帮助中心 | `faq_178447635241069` | About marketing messages | 正文 9.1 | 已取回（正文 1946 字符） |
| WhatsApp 帮助中心 | `faq_1797219184095235` | Why is Meta reviewing my marketing messages? | 正文 10.1、附表 E（1 处） | 已取回（正文 1379 字符） |
| WhatsApp 帮助中心 | `faq_1805617343145907` | About detecting violations | 正文 12.3、附表 C（1 处） | 已取回（正文 1374 字符） |
| WhatsApp 帮助中心 | `faq_2090730848486858` | How to report a username | 正文 3.1、附表 B（1 处）、附表 C（2 处）、附表 E（5 处）、附表 F（1 处） | 已取回（正文 2705 字符） |
| WhatsApp 帮助中心 | `faq_2133933800112087` | How to report a catalog or business | 正文 3.4 | 已取回（正文 1406 字符） |
| WhatsApp 帮助中心 | `faq_2286952358121083` | About suspicious messages and scams | 正文 5.1 | 已取回（正文 5190 字符） |
| WhatsApp 帮助中心 | `faq_2470686849751285` | How to block or unblock a business | 正文 3.4、附表 E（1 处） | 已取回（正文 1426 字符） |
| WhatsApp 帮助中心 | `faq_263784176043634` | About interactions with business chats that include offers and announcements | 正文 9.3、附表 C（1 处）、附表 E（1 处） | 已取回（正文 2290 字符） |
| WhatsApp 帮助中心 | `faq_27563559673256587` | About opting in to receive messages from a business on WhatsApp | 正文 8.2、附表 E（2 处）、附表 F（1 处） | 已取回（正文 1254 字符） |
| WhatsApp 帮助中心 | `faq_3379690015658337` | How to block high volumes of unknown messages | 正文 5.4 | 已取回（正文 1614 字符） |
| WhatsApp 帮助中心 | `faq_3405954479576110` | How to report a sticker pack | 正文 3.2、附表 C（1 处）、附表 E（1 处） | 已取回（正文 1068 字符） |
| WhatsApp 帮助中心 | `faq_3440358352849077` | How to report a channel | 正文 3.2、附表 E（1 处）、附表 F（1 处） | 已取回（正文 1885 字符） |
| WhatsApp 帮助中心 | `faq_361005896189245` | How to use WhatsApp responsibly | 正文 14.2、附表 E（4 处） | 已取回（正文 3633 字符） |
| WhatsApp 帮助中心 | `faq_393169153028916` | About suspicious links | 正文 5.3 | 已取回（正文 2013 字符） |
| WhatsApp 帮助中心 | `faq_414631957536067` | About reporting and blocking on WhatsApp | 正文 2.1、附表 B（1 处）、附表 E（7 处）、附表 F（11 处） | 已取回（正文 6120 字符） |
| WhatsApp 帮助中心 | `faq_424124173736394` | How to stay safe in groups on WhatsApp | 正文 6.1、附表 C（2 处）、附表 E（7 处）、附表 F（3 处） | 已取回（正文 4599 字符） |
| WhatsApp 帮助中心 | `faq_478157451902194` | How to hide or report an ad or website in WhatsApp Status or Channels | 正文 3.3、附表 B（1 处） | 已取回（正文 2687 字符） |
| WhatsApp 帮助中心 | `faq_573786218075805` | How to protect yourself from suspicious messages and scams | 正文 5.2、附表 C（1 处） | 已取回（正文 1963 字符） |
| WhatsApp 帮助中心 | `faq_5957850900902049` | Unauthorized use of automated or bulk messaging on WhatsApp | 正文 12.5、附表 B（2 处）、附表 C（1 处） | 已取回（正文 2455 字符） |
| WhatsApp 帮助中心 | `faq_650453616475879` | Ensuring User Safety in India on WhatsApp | 正文 14.3、附表 B（6 处）、附表 F（3 处） | 已取回（正文 11214 字符） |
| WhatsApp 帮助中心 | `faq_666362298345682` | Being blocked by someone | 正文 2.3、附表 F（2 处） | 已取回（正文 1905 字符） |
| WhatsApp 帮助中心 | `faq_740266744985664` | Can't send marketing messages | 正文 10.2、附表 C（4 处）、附表 E（2 处）、附表 F（1 处） | 已取回（正文 2166 字符） |
| WhatsApp 帮助中心 | `faq_766346674749731` | Page not found | 全篇不引用（正文未取回或只有地域限定提示） | 正文未取回（抓取残骸：空正文，文件 0 字符） |
| WhatsApp 帮助中心 | `faq_771267714464323` | How to report someone’s status update | 正文 3.3、附表 E（1 处） | 已取回（正文 1514 字符） |
| WhatsApp 帮助中心 | `faq_841324506880320` | How to block a group admin | 正文 2.4 | 已取回（正文 1769 字符） |
| WhatsApp 帮助中心 | `faq_896873687365001` | Page not found | 全篇不引用（正文未取回或只有地域限定提示） | 正文未取回（抓取残骸：空正文，文件 0 字符） |
| 法律与政策页 | `legal_business_policy` | Business Policy | 正文 1.1、13.2、附表 B（5 处）、附表 C（40 处）、附表 E（4 处） | 已取回（正文 19466 字符） |
| 法律与政策页 | `legal_business_terms` | WhatsApp Business Terms of Service | 正文 1.3、13.3 | 已取回（正文 1121 字符） |
| 法律与政策页 | `legal_messaging_guidelines` | WhatsApp Messaging Guidelines | 正文 1.1、13.1、附表 C（5 处）、附表 E（4 处）、附表 F（1 处） | 已取回（正文 4925 字符） |
| 法律与政策页 | `legal_security_center` | Security Features, Safety Tools & Tips | 正文 14.1、附表 C（3 处） | 已取回（正文 2893 字符） |
| 法律与政策页 | `legal_tos` | Terms of Service | 正文 13.3、附表 B（3 处）、附表 C（4 处）、附表 E（2 处） | 已取回（正文 28027 字符） |
| 法律与政策页 | `legal_transparency_report` | Community Standards Enforcement Report | 正文 1.1、13.4、附表 B（3 处）、附表 C（1 处）、附表 F（1 处） | 已取回（正文 5063 字符） |
| **合计** | **63** 个登记位 | 可引用 **61** 页／不可引用 2 页 | Meta 开发者文档 22、WhatsApp 帮助中心 35、法律与政策页 6 | 不可引用页全篇不引用 |

#ROWS=63 COLS=5


# 附表 B 数字归档五档（48 行）

归档口径：**本表一行一个「数字＋它数的东西＋出处页与句序」**，数字、单位词与整句都由脚本从语料里实测取出——输入是原句池的 **1194** 条整句（覆盖 **58** 个页面，池子已剔除含竖线的「正文黏表格」混合句），脚本在每句里找出所有独立数字，取该数字后面最多四个词，档位由这四个词里第一个能对上单位表的词决定；同页同数字同单位只留一行（共去重 **13** 行）。全表 **47** 行，分布在 **19** 个页面上，去重后的「数字＋单位」组合 **37** 组，单位词 **21** 种。五档分布：一、账号、号码与设备规模档位 7／三、时长与滚动窗口 24／二、验证要素与文本长度规格 3／五、速率、次数与比率口径 4／四、生效日期与版本节点 9。

三条不成文规矩在这一表里最要紧：**同一个数字在两个页面各写一处，就各登记一行**，不合并成区间；**官方写成区间、多档或带限定词的照原样登记**（`up to`、`about`、`approximately`、`in some countries` 一类措辞原样保留），不取中值、不去掉约数；**档位与性质由脚本按关键词判定，性质判不出来就中止出表**，所以本表没有「其它」这一档。

本表也写清了自己漏掉了什么：池子里 **1100** 句整句不含数字，脚本共扫出 **33** 个「数字后面四个词都对不上任何一档」的实例，全部连同它所在的整句写进 `_tmp/w6_a5_bdrop.tsv` 供复核，不进表；这一批绝大多数是地址、法条编号与接口示例对象 ID，不是官方对某项业务写下的数字，**漏掉的判定权在读者手里，不在这张表里**。另有 **3** 个可引用页面在池子里一句都没有——它们的正文是纯参数表，官方数字要进本表只能连同表格 markup 一起取，那类混合句已在建池时剔除，所以这几页在本表里是空的，在附表 A 已逐页点名。

官方原句超过 118 字符按整词截断并以省略号结尾，共 **33** 行截断；截断只发生在表格里，进表的每一句都先用未截断的整句逐字回表到`_tmp/w6_a5s/` 的对应页面，失败 **0** 句（回表与建池共用同一套规范化，所以这里一旦非零就是池子与抓取件不同步，脚本直接中止不出表）。性质判定为 **11** 种：上限（官方写成 up to／maximum 一类） 7／下限或触发门槛 2／位数或口令长度 1／删除与留存窗口 2／时长（官方只写数值与单位） 11／月份与号数写明的日期节点 1／比率 1／版本号或生效日期 3／申诉与复核时长 9／自动解除或自动生效的时限 2／规模计数 8。

| 档 | 数字 | 计数单位（官方原文） | 官方原句 | 性质 | 出处页 |
|---|---|---|---|---|---|
| 三、时长与滚动窗口 | **60** | seconds | 「The media file must satisfy the following: - Duration must be less than 60 seconds. - MIME type must be `audio/ogg`…」 | 版本号或生效日期 | `calling_call_settings_md`｜第 54 句 |
| 三、时长与滚动窗口 | **30** | day | 「The media file must satisfy the following: - Duration must be less than 60 seconds. - MIME type must be `audio/ogg`…」 | 版本号或生效日期 | `calling_call_settings_md`｜第 54 句 |
| 一、账号、号码与设备规模档位 | **30** | applied | 「This skips the standard 30-day TTL applied to messaging media so that announcements remain available for the lifetime…」 | 时长（官方只写数值与单位） | `calling_call_settings_md`｜第 59 句 |
| 三、时长与滚动窗口 | **30** | days | 「Messages have a maximum retention period of 30 days in order to provide the base features and functionality of the…」 | 上限（官方写成 up to／maximum 一类） | `data_privacy_and_security_md`｜第 23 句 |
| 二、验证要素与文本长度规格 | **6** | digit | 「Channels Accounts and Account Bans Payments WhatsApp for Business How to keep your groups and community safe Copy…」 | 位数或口令长度 | `faq_1003616827680090`｜第 2 句 |
| 三、时长与滚动窗口 | **24** | hours | 「Control what you send and see Send disappearing messages: You can decide how long messages remain visible once you've…」 | 时长（官方只写数值与单位） | `faq_1003616827680090`｜第 16 句 |
| 三、时长与滚动窗口 | **7** | days | 「Control what you send and see Send disappearing messages: You can decide how long messages remain visible once you've…」 | 时长（官方只写数值与单位） | `faq_1003616827680090`｜第 16 句 |
| 三、时长与滚动窗口 | **90** | days | 「Control what you send and see Send disappearing messages: You can decide how long messages remain visible once you've…」 | 时长（官方只写数值与单位） | `faq_1003616827680090`｜第 16 句 |
| 四、生效日期与版本节点 | **30** | date | 「If you've never sent a business broadcast, your trial ended on April 30, 2026.」 | 月份与号数写明的日期节点 | `faq_1524953031793983`｜第 11 句 |
| 五、速率、次数与比率口径 | **5** | messages | 「Tap Report When you choose to report someone, the last 5 messages sent to the managed account will be sent to…」 | 规模计数 | `faq_1561915401728939`｜第 11 句 |
| 五、速率、次数与比率口径 | **5** | messages | 「Select Report if you want to share the last 5 messages with WhatsApp.」 | 规模计数 | `faq_2090730848486858`｜第 19 句 |
| 三、时长与滚动窗口 | **24** | hours | 「The status will disappear within 24 hours and you won’t see future status updates from that user.」 | 申诉与复核时长 | `faq_414631957536067`｜第 47 句 |
| 一、账号、号码与设备规模档位 | **1** | field | 「Tap and hold the URL 1 entry field > tap Paste .」 | 规模计数 | `faq_478157451902194`｜第 29 句 |
| 四、生效日期与版本节点 | **7** | date | 「In addition, beginning on December 7, 2019, WhatsApp will take legal action against those we determine are engaged in…」 | 申诉与复核时长 | `faq_5957850900902049`｜第 11 句 |
| 四、生效日期与版本节点 | **2019** | year | 「In addition, beginning on December 7, 2019, WhatsApp will take legal action against those we determine are engaged in…」 | 申诉与复核时长 | `faq_5957850900902049`｜第 11 句 |
| 二、验证要素与文本长度规格 | **64** | digit | 「With end-to-end encrypted backup , messages and media are stored in the cloud and secured by a password or a 64-digit…」 | 删除与留存窗口 | `faq_650453616475879`｜第 30 句 |
| 四、生效日期与版本节点 | **2021** | year | 「Monthly Reports: In compliance with IT Rules 2021, WhatsApp regularly releases its monthly report in India that…」 | 时长（官方只写数值与单位） | `faq_650453616475879`｜第 39 句 |
| 四、生效日期与版本节点 | **10** | date | 「Fact checking within WhatsApp: In India, 10 fact checking organizations (as of April, 2023) help to provide users the…」 | 申诉与复核时长 | `faq_650453616475879`｜第 49 句 |
| 四、生效日期与版本节点 | **2023** | year | 「Fact checking within WhatsApp: In India, 10 fact checking organizations (as of April, 2023) help to provide users the…」 | 申诉与复核时长 | `faq_650453616475879`｜第 49 句 |
| 一、账号、号码与设备规模档位 | **2912606** | contact | 「Users can save +1 (727) 2912606 as a contact number and text , ”Hi” to get a message or information validated.」 | 规模计数 | `faq_650453616475879`｜第 52 句 |
| 三、时长与滚动窗口 | **30** | days | 「Undelivered messages are deleted from our servers after 30 days.」 | 删除与留存窗口 | `faq_650453616475879`｜第 73 句 |
| 一、账号、号码与设备规模档位 | **24** | customer | 「You may reply to a user message without use of a Message Template as long as it’s within the 24-hour customer service…」 | 申诉与复核时长 | `legal_business_policy`｜第 23 句 |
| 三、时长与滚动窗口 | **24** | hour | 「You may use automation when responding during the 24-hour window, but must also have available prompt, clear, and…」 | 时长（官方只写数值与单位） | `legal_business_policy`｜第 26 句 |
| 一、账号、号码与设备规模档位 | **5** | countries | 「Further Guidance on Regulated Verticals We allow the use of the WhatsApp Business Platform , for messaging about the…」 | 规模计数 | `legal_business_policy`｜第 46 句 |
| 三、时长与滚动窗口 | **18** | years | 「Messaging about any Regulated Vertical using the WhatsApp Business Platform may not be sent to people under 18 years…」 | 时长（官方只写数值与单位） | `legal_business_policy`｜第 55 句 |
| 一、账号、号码与设备规模档位 | **5** | business | 「Provide evidence that the online gambling and gaming activities are appropriately licensed by a regulator, or…」 | 规模计数 | `legal_business_policy`｜第 68 句 |
| 三、时长与滚动窗口 | **13** | years | 「You must be at least 13 years old to register for and use our Services on your own (or such greater age required in…」 | 下限或触发门槛 | `legal_tos`｜第 44 句 |
| 三、时长与滚动窗口 | **30** | days | 「The rights you grant in this license are for the limited purpose of operating and providing our Services (such as to…」 | 上限（官方写成 up to／maximum 一类） | `legal_tos`｜第 79 句 |
| 三、时长与滚动窗口 | **60** | days | 「If we are unable to resolve a dispute within sixty (60) days after the Notice of Dispute is received, you or we may…」 | 自动解除或自动生效的时限 | `legal_tos`｜第 166 句 |
| 四、生效日期与版本节点 | **2026** | year | 「H1 2026 Report We publish the Community Standards Enforcement Report semi-annually, with data broken down quarterly,…」 | 规模计数 | `legal_transparency_report`｜第 4 句 |
| 三、时长与滚动窗口 | **2026** | year | 「Key Takeaways for H1 2026 Last year we began providing new data on our global enforcement precision as a measure of…」 | 下限或触发门槛 | `legal_transparency_report`｜第 8 句 |
| 四、生效日期与版本节点 | **2025** | year | 「Prevalence of violating content remained consistent across most problem areas, with two exceptions: On Facebook,…」 | 比率 | `legal_transparency_report`｜第 10 句 |
| 三、时长与滚动窗口 | **24** | hours | 「The appeal review decision will be sent via Business Manager and typically takes 24 to 48 hours.」 | 申诉与复核时长 | `policy_enforcement_md`｜第 29 句 |
| 三、时长与滚动窗口 | **48** | hours | 「The appeal review decision will be sent via Business Manager and typically takes 24 to 48 hours.」 | 申诉与复核时长 | `policy_enforcement_md`｜第 29 句 |
| 三、时长与滚动窗口 | **10** | minutes | 「Ad synchronization can take up to 10 minutes.」 | 上限（官方写成 up to／maximum 一类） | `support_error_codes_md`｜第 98 句 |
| 三、时长与滚动窗口 | **24** | hours | 「Marketing messages sent within this window do not count towards the limit. ### Retry limits To ensure your campaigns…」 | 上限（官方写成 up to／maximum 一类） | `templates_marketing_templates_per_user_limits_md`｜第 7 句 |
| 一、账号、号码与设备规模档位 | **24** | users | 「If your Messaging account attempts to resend marketing messages multiple times within a 24-hour period to users who…」 | 上限（官方写成 up to／maximum 一类） | `templates_marketing_templates_per_user_limits_md`｜第 10 句 |
| 二、验证要素与文本长度规格 | **512** | characters | 「Template names are limited to a maximum of 512 characters, consisting of lowercase alphanumeric characters and…」 | 上限（官方写成 up to／maximum 一类） | `templates_overview_md`｜第 10 句 |
| 三、时长与滚动窗口 | **24** | hours | 「Review can take up to 24 hours. - **Rejected**: The template has been rejected during the review process or violates…」 | 上限（官方写成 up to／maximum 一类） | `templates_overview_md`｜第 33 句 |
| 五、速率、次数与比率口径 | **250** | message | 「If a parent business portfolio is unverified, each of its Messaging accounts is limited to 250 message templates.」 | 规模计数 | `templates_overview_md`｜第 41 句 |
| 三、时长与滚动窗口 | **3** | hours | 「Pausing durations are as follows: - 1st Instance: Paused for 3 hours - 2nd Instance: Paused for 6 hours - 3rd…」 | 时长（官方只写数值与单位） | `templates_template_pausing_md`｜第 2 句 |
| 三、时长与滚动窗口 | **6** | hours | 「Pausing durations are as follows: - 1st Instance: Paused for 3 hours - 2nd Instance: Paused for 6 hours - 3rd…」 | 时长（官方只写数值与单位） | `templates_template_pausing_md`｜第 2 句 |
| 四、生效日期与版本节点 | **12** | date | 「Applies to all businesses starting October 12, 2023.」 | 版本号或生效日期 | `templates_template_pausing_md`｜第 17 句 |
| 三、时长与滚动窗口 | **24** | hours | 「The appeal will be reviewed and a decision made within 24 hours.」 | 申诉与复核时长 | `templates_template_pausing_md`｜第 28 句 |
| 三、时长与滚动窗口 | **30** | days | 「`COMPANION_INACTIVITY` — A companion device was inactive for approximately 30 days.」 | 时长（官方只写数值与单位） | `webhooks_reference_account_update_md`｜第 30 句 |
| 三、时长与滚动窗口 | **14** | days | 「`PRIMARY_INACTIVITY` — The primary device was inactive for approximately 14 days.」 | 时长（官方只写数值与单位） | `webhooks_reference_account_update_md`｜第 31 句 |
| 五、速率、次数与比率口径 | **102290129340398** | time | 「```json { "object": "whatsapp_business_account", "entry": [ { "id": "102290129340398", "time": 1743451903, "changes":…」 | 自动解除或自动生效的时限 | `webhooks_reference_account_update_md`｜第 73 句 |
| **合计** | **47** 行 | 单位词 **21** 种 | 去重后数字＋单位 **37** 组，覆盖 **19** 页 | 性质 **11** 种 | 第四档＝日期与版本节点 |

#ROWS=47 COLS=6


# 附表 C 官方用词与写下它的站点类别（177 行）

取格口径：脚本按第一、二列的 **82** 个词（组）（分属 13 个主题组：A　三个动作的写法 6／B　举报的对象与入口 8／C　骚扰与不受欢迎消息 7／D　平台侧的处置动词 7／E　判定与检测 7／F　收件人侧的自助动作 6／G　业务侧屏蔽接口 6／H　质量、暂停与负反馈 6／I　限额与可达性 6／J　法律与准则层 7／K　自我保护口径 6／L　举报信息面 6／M　本篇问而句池无答 4）逐句扫过原句池的 **1194** 条整句（覆盖 **58** 个页面），每命中一次就落进「词 × 站点类别」的格子；**一行一个非空格**，全表 **176** 行，各格命中句次相加是 **2119** 次，而去重后真正被任一词命中的句子是 **925** 句（占全池 77.5%）。同一句可以落进多格：本轮一句落进最多格子的是 `faq_1142481766359885` 池内序号 1，同时命中 10 个词（组）——「account（账号）」、「blocked（过去式与形容词用法）」、「block（动词原形）」、「group／community（群组与社区）」、「reported／reporting（已报与进行中）」、「report（动词原形）」、「safety／safe（安全）」、「scam／scammer（诈骗）」、「status update（状态更新）」、「suspicious（可疑）」；全池有 578 句同时落进两个以上的格子。所以句次合计 **2119** 大于去重句数 **925**，这两个数各自回答「这一格有多少句」与「整池有多少句被扫中」，不矛盾。

词表本身也是这一篇的一部分：它写在 `_tmp/w6_a5_verb.tsv` 里，脚本不内置任何选题词，换一篇只换词表、判定规则不变。**正则由脚本逐条编译，编译不过就中止出表**，不存在「写错的正则悄悄扫出零句然后当成官方没写过」。

代表句一列取该格在池子里最先出现的一句，取句规则写死在脚本的遍历顺序里，**不是按哪句更清楚来挑**。「格内有句写明数值」只回答这一格有没有句子自带天数、小时、位数、百分比或规模数——有则记「有」，没有就记「无」，不在这里把别的页面或别的站的数字补进来；跨站对撞属于附表 F。

词零命中的情况：82 个词（组）里 **8** 个在整池一句都没出现（`automatic review（自动审核）`、`report it to us（要用户上报的说法）`、`block API（接口名）`、`block rate／report rate（屏蔽率与举报率）`、`threshold（阈值）`、`tier（档位）`、`harassment（名词形态）`、`reporter identity（举报人身份是否告知被举报方）`）。零命中不等于官方没有这一项内容，只说明本篇登记的这批页面没有用这个词写过。

本表的英文原句全部逐字回表：1194 条整句每一条都在 `_tmp/w6_a5s/` 的对应页面正文里按规范化后的子串核到，失败 **0** 句（判定与建池用同一套实体还原与空白折叠，见 `_tmp/wa6_lib.pl` 的 ck）。官方原句超过 130 字符按整词截断并以省略号结尾，共 **107** 行截断；截断只发生在表格里，回表核验用的是未截断的整句。各站点命中句次合计：Meta 开发者文档 488／WhatsApp 帮助中心 1210／Meta 商业帮助中心 0／法律与政策页 421。

| 主题组 | 官方词（组） | 写下它的站点类别 | 命中句次 | 命中页数 | 代表句（该格池内第一句） | 格内有句写明数值 | 出处页｜句序 |
|---|---|---|---|---|---|---|---|
| A　三个动作的写法 | block（动词原形） | Meta 开发者文档 | **2** | 2 | 「The API returns errors per-number, because a block can succeed on some numbers and fail on others.」 | 无 | `block_users_md`｜第 3 句 |
| A　三个动作的写法 | block（动词原形） | WhatsApp 帮助中心 | **69** | 20 | 「Avoid unwanted contact Block and report: You can stop receiving messages, calls, and status updates from a contact by blocking…」 | 无 | `faq_1003616827680090`｜第 23 句 |
| A　三个动作的写法 | block（动词原形） | 法律与政策页 | **2** | 1 | 「You must respect all requests (either on or off WhatsApp) by a person to block, discontinue, or otherwise opt out of…」 | 无 | `legal_business_policy`｜第 17 句 |
| A　三个动作的写法 | blocked（过去式与形容词用法） | Meta 开发者文档 | **2** | 1 | 「Attempts to message a blocked user return an error.」 | 无 | `block_users_md`｜第 2 句 |
| A　三个动作的写法 | blocked（过去式与形容词用法） | WhatsApp 帮助中心 | **29** | 7 | 「About reporting and blocking on WhatsApp How to block a group admin Being blocked by someone How to report someone’s status…」 | 无 | `faq_1142481766359885`｜第 1 句 |
| A　三个动作的写法 | unblock（解除屏蔽） | Meta 开发者文档 | **1** | 1 | 「Unblock the WhatsApp user to resume sending messages to them.」 | 无 | `support_error_codes_md`｜第 63 句 |
| A　三个动作的写法 | unblock（解除屏蔽） | WhatsApp 帮助中心 | **9** | 4 | 「You can also unblock the sender to receive new messages from them.」 | 无 | `faq_1142481766359885`｜第 8 句 |
| A　三个动作的写法 | report（动词原形） | Meta 开发者文档 | **3** | 3 | 「If a policy-violating item is added, the product is flagged and the business is directed to the WhatsApp Commerce Policies. -…」 | 无 | `catalogs_catalogs_overview_md`｜第 20 句 |
| A　三个动作的写法 | report（动词原形） | WhatsApp 帮助中心 | **101** | 21 | 「Avoid unwanted contact Block and report: You can stop receiving messages, calls, and status updates from a contact by blocking…」 | 有 | `faq_1003616827680090`｜第 23 句 |
| A　三个动作的写法 | report（动词原形） | 法律与政策页 | **8** | 4 | 「Users can report WhatsApp contacts, groups, communities, status updates, or specific messages that potentially violate these…」 | 无 | `legal_messaging_guidelines`｜第 18 句 |
| A　三个动作的写法 | reported／reporting（已报与进行中） | Meta 开发者文档 | **4** | 4 | 「If a policy-violating item is added, the product is flagged and the business is directed to the WhatsApp Commerce Policies. -…」 | 无 | `catalogs_catalogs_overview_md`｜第 20 句 |
| A　三个动作的写法 | reported／reporting（已报与进行中） | WhatsApp 帮助中心 | **32** | 12 | 「About reporting and blocking on WhatsApp How to block a group admin Being blocked by someone How to report someone’s status…」 | 无 | `faq_1142481766359885`｜第 1 句 |
| A　三个动作的写法 | reported／reporting（已报与进行中） | 法律与政策页 | **9** | 4 | 「WhatsApp may use both automated processing techniques and human review teams to detect and review information available to us…」 | 无 | `legal_business_policy`｜第 89 句 |
| A　三个动作的写法 | reports（复数名词，平台收到的举报量） | Meta 开发者文档 | **3** | 2 | 「If a user reports a product, it is reviewed for violations.」 | 无 | `catalogs_catalogs_overview_md`｜第 21 句 |
| A　三个动作的写法 | reports（复数名词，平台收到的举报量） | WhatsApp 帮助中心 | **15** | 8 | 「If someone you sent message history to reports content that was sent as part of the message history you sent, you’ll be reported.」 | 无 | `faq_1142481766359885`｜第 77 句 |
| A　三个动作的写法 | reports（复数名词，平台收到的举报量） | 法律与政策页 | **1** | 1 | 「Default privacy Automatic spam detection We catch and remove most spam and scam accounts before they can even reach you or before…」 | 无 | `legal_security_center`｜第 6 句 |
| B　举报的对象与入口 | user（被举报的个人账号） | Meta 开发者文档 | **41** | 12 | 「Attempts to message a blocked user return an error.」 | 有 | `block_users_md`｜第 2 句 |
| B　举报的对象与入口 | user（被举报的个人账号） | WhatsApp 帮助中心 | **56** | 15 | 「Uncheck the box next to Report to WhatsApp if you don’t want to report the person you’re blocking.」 | 有 | `faq_1142481766359885`｜第 22 句 |
| B　举报的对象与入口 | user（被举报的个人账号） | 法律与政策页 | **12** | 3 | 「To help ensure a strong user experience, review the best practices suggested below .」 | 无 | `legal_business_policy`｜第 16 句 |
| B　举报的对象与入口 | username（用户名形态的账号） | Meta 开发者文档 | **1** | 1 | 「Omitted if the user has adopted a username and their phone number is unavailable.」 | 无 | `webhooks_reference_user_preferences_md`｜第 13 句 |
| B　举报的对象与入口 | username（用户名形态的账号） | WhatsApp 帮助中心 | **18** | 1 | 「Security Accounts and Account Bans Payments WhatsApp for Business How to report a username Copy link Android iOS Web More Android…」 | 无 | `faq_2090730848486858`｜第 1 句 |
| B　举报的对象与入口 | username（用户名形态的账号） | 法律与政策页 | **2** | 2 | 「You may only contact people on WhatsApp if: (a) they have given you their mobile phone number or username; and (b) you have…」 | 无 | `legal_business_policy`｜第 14 句 |
| B　举报的对象与入口 | group／community（群组与社区） | WhatsApp 帮助中心 | **110** | 13 | 「About building private, safe, and secure communities on WhatsApp What does "This community is no longer available" mean?」 | 无 | `faq_1003616827680090`｜第 1 句 |
| B　举报的对象与入口 | group／community（群组与社区） | 法律与政策页 | **10** | 4 | 「WhatsApp may use both automated processing techniques and human review teams to detect and review information available to us…」 | 无 | `legal_business_policy`｜第 89 句 |
| B　举报的对象与入口 | channel（频道） | Meta 开发者文档 | **4** | 2 | 「The early warning notifications will be communicated via the channels below. #### Email Enforcement emails are sent to the email…」 | 无 | `calling_call_settings_md`｜第 86 句 |
| B　举报的对象与入口 | channel（频道） | WhatsApp 帮助中心 | **23** | 5 | 「Channels Accounts and Account Bans Payments WhatsApp for Business How to keep your groups and community safe Copy link WhatsApp…」 | 无 | `faq_1003616827680090`｜第 2 句 |
| B　举报的对象与入口 | sticker pack（贴纸包） | WhatsApp 帮助中心 | **3** | 1 | 「Please be aware that a user report of conduct in violation of our Terms of Service or Sticker Guidelines won't necessarily result…」 | 无 | `faq_3405954479576110`｜第 4 句 |
| B　举报的对象与入口 | status update（状态更新） | Meta 开发者文档 | **21** | 8 | 「When the `status` parameter is set to `"DISABLED"`, calling features are disabled, and both the business chat and business…」 | 有 | `calling_call_settings_md`｜第 12 句 |
| B　举报的对象与入口 | status update（状态更新） | WhatsApp 帮助中心 | **29** | 11 | 「Avoid unwanted contact Block and report: You can stop receiving messages, calls, and status updates from a contact by blocking…」 | 有 | `faq_1003616827680090`｜第 23 句 |
| B　举报的对象与入口 | status update（状态更新） | 法律与政策页 | **4** | 3 | 「Organizations must not wrongfully discriminate or suggest a preference for or against people because of a personal…」 | 有 | `legal_business_policy`｜第 41 句 |
| B　举报的对象与入口 | ad／advertisement（广告与网站链接） | Meta 开发者文档 | **3** | 3 | 「Cloud API will not automatically use WhatsApp messages to inform the ads that a person sees.」 | 有 | `data_privacy_and_security_md`｜第 16 句 |
| B　举报的对象与入口 | ad／advertisement（广告与网站链接） | WhatsApp 帮助中心 | **24** | 4 | 「The business may designate employees or other vendors to process and respond to messages, and a business may also use the chats…」 | 无 | `faq_1148840052398648`｜第 5 句 |
| B　举报的对象与入口 | catalog／business（目录与业务） | Meta 开发者文档 | **11** | 3 | 「Instead of redirecting customers to external websites or apps, catalogs let customers browse products within the chat — customers…」 | 有 | `catalogs_catalogs_overview_md`｜第 2 句 |
| B　举报的对象与入口 | catalog／business（目录与业务） | WhatsApp 帮助中心 | **5** | 4 | 「When you message a WhatsApp business account, your message is delivered securely to the business.」 | 无 | `faq_1148840052398648`｜第 2 句 |
| B　举报的对象与入口 | catalog／business（目录与业务） | 法律与政策页 | **4** | 1 | 「If you use Catalogs, or provide any other commerce experiences to sell or otherwise facilitate the exchange of goods or services…」 | 无 | `legal_business_policy`｜第 45 句 |
| C　骚扰与不受欢迎消息 | harass（骚扰） | WhatsApp 帮助中心 | **2** | 2 | 「Our Terms of Service outline prohibited activities, which include sharing content (in the status, profile photos, or messages)…」 | 无 | `faq_1313491802751163`｜第 4 句 |
| C　骚扰与不受欢迎消息 | spam（垃圾信息） | Meta 开发者文档 | **2** | 2 | 「WhatsApp protects users by detecting unusual messaging patterns (like a business trying to message all users) and collecting spam…」 | 无 | `data_privacy_and_security_md`｜第 11 句 |
| C　骚扰与不受欢迎消息 | spam（垃圾信息） | WhatsApp 帮助中心 | **13** | 9 | 「You can also report them if you think they are sending problematic content or spam.」 | 无 | `faq_1142481766359885`｜第 3 句 |
| C　骚扰与不受欢迎消息 | spam（垃圾信息） | 法律与政策页 | **3** | 3 | 「Do not confuse, deceive, defraud, mislead, spam, or surprise people with your communications.」 | 无 | `legal_business_policy`｜第 18 句 |
| C　骚扰与不受欢迎消息 | abuse（滥用） | Meta 开发者文档 | **1** | 1 | 「The `violation_type` is `DIRECT_SEND_UTILITY_CATEGORY_ABUSE_UNBAN` (restriction lifted) or…」 | 无 | `direct_send_integrity_and_content_guidelines_md`｜第 36 句 |
| C　骚扰与不受欢迎消息 | abuse（滥用） | WhatsApp 帮助中心 | **15** | 5 | 「If you receive or encounter content indicating that a child is being abused or exploited, please contact the National Center for…」 | 无 | `faq_1313491802751163`｜第 44 句 |
| C　骚扰与不受欢迎消息 | abuse（滥用） | 法律与政策页 | **4** | 3 | 「Do not share illegal content or engage in illegal activity This includes content that sexually exploits or endangers children…」 | 无 | `legal_messaging_guidelines`｜第 12 句 |
| C　骚扰与不受欢迎消息 | unsolicited（未经请求） | Meta 开发者文档 | **2** | 1 | 「The behavior for supported options is as follows: `DEFAULT` WhatsApp displays the call button icon in the chat menu bar and the…」 | 无 | `calling_call_settings_md`｜第 15 句 |
| C　骚扰与不受欢迎消息 | unsolicited（未经请求） | 法律与政策页 | **1** | 1 | 「This mitigates the risk that users will block your business because they receive unsolicited messages.」 | 无 | `legal_business_policy`｜第 95 句 |
| C　骚扰与不受欢迎消息 | unwanted（不想要的） | WhatsApp 帮助中心 | **17** | 10 | 「Avoid unwanted contact Block and report: You can stop receiving messages, calls, and status updates from a contact by blocking…」 | 无 | `faq_1003616827680090`｜第 23 句 |
| C　骚扰与不受欢迎消息 | unwanted（不想要的） | 法律与政策页 | **1** | 1 | 「Do not engage in fraud or spam This includes repeated unwanted contact with WhatsApp users, and activity that scams or defrauds…」 | 无 | `legal_messaging_guidelines`｜第 17 句 |
| C　骚扰与不受欢迎消息 | bulk（批量投递） | Meta 开发者文档 | **1** | 1 | 「The bulk of the variation occurs in the `category` string, which assigns a category to the template, and the `components` array,…」 | 无 | `templates_overview_md`｜第 7 句 |
| C　骚扰与不受欢迎消息 | bulk（批量投递） | WhatsApp 帮助中心 | **3** | 1 | 「Our products are not intended for bulk or automated messaging, both of which have always been a violation of our .」 | 无 | `faq_5957850900902049`｜第 3 句 |
| C　骚扰与不受欢迎消息 | bulk（批量投递） | 法律与政策页 | **1** | 1 | 「This includes efforts to undermine WhatsApp integrity systems, abuse reporting tools, compromise user accounts, scrape data, or…」 | 无 | `legal_messaging_guidelines`｜第 16 句 |
| C　骚扰与不受欢迎消息 | automated／automatic（自动化） | Meta 开发者文档 | **11** | 8 | 「You can configure the call permission UI to automatically show in the WhatsApp user's client app when they call your business…」 | 有 | `calling_call_settings_md`｜第 20 句 |
| C　骚扰与不受欢迎消息 | automated／automatic（自动化） | WhatsApp 帮助中心 | **12** | 5 | 「Meta will not automatically use the messages you send a business to inform the ads you see.」 | 无 | `faq_1148840052398648`｜第 7 句 |
| C　骚扰与不受欢迎消息 | automated／automatic（自动化） | 法律与政策页 | **11** | 4 | 「You may use automation when responding during the 24-hour window, but must also have available prompt, clear, and direct…」 | 无 | `legal_business_policy`｜第 26 句 |
| D　平台侧的处置动词 | ban（封禁） | WhatsApp 帮助中心 | **13** | 7 | 「We may ban a user if we believe that user is violating our Terms of Service.」 | 无 | `faq_1313491802751163`｜第 5 句 |
| D　平台侧的处置动词 | suspend（暂停） | WhatsApp 帮助中心 | **1** | 1 | 「However, if you continue to send spam from your business account, marketing messages will be suspended on the business account…」 | 无 | `faq_740266744985664`｜第 11 句 |
| D　平台侧的处置动词 | suspend（暂停） | 法律与政策页 | **6** | 2 | 「Steps to Take if Your Account is Suspended or Terminated To maintain high quality conversations on WhatsApp, we enforce on…」 | 无 | `legal_business_policy`｜第 101 句 |
| D　平台侧的处置动词 | restrict（限制） | Meta 开发者文档 | **20** | 5 | 「Configuring this setting restricts calls only to available weekly hours you configure.」 | 无 | `calling_call_settings_md`｜第 24 句 |
| D　平台侧的处置动词 | restrict（限制） | WhatsApp 帮助中心 | **8** | 4 | 「Restrict who can make changes to your community.」 | 无 | `faq_1003616827680090`｜第 31 句 |
| D　平台侧的处置动词 | restrict（限制） | 法律与政策页 | **6** | 3 | 「Prohibited Organizations and Restrictions on Use We prohibit organizations and/or individuals who have been deemed to be engaged…」 | 无 | `legal_business_policy`｜第 34 句 |
| D　平台侧的处置动词 | remove（移除内容或功能） | Meta 开发者文档 | **3** | 3 | 「Returned as `user_id` in `removed_users` only if you identified the user with `user_id` alone.」 | 无 | `block_users_md`｜第 39 句 |
| D　平台侧的处置动词 | remove（移除内容或功能） | WhatsApp 帮助中心 | **18** | 10 | 「Delete unwanted messages or remove members.」 | 无 | `faq_1003616827680090`｜第 34 句 |
| D　平台侧的处置动词 | remove（移除内容或功能） | 法律与政策页 | **5** | 3 | 「If content or actions violate our terms or policies, we may limit or remove your access to our services.」 | 无 | `legal_business_policy`｜第 5 句 |
| D　平台侧的处置动词 | enforce／enforcement（执法） | Meta 开发者文档 | **9** | 6 | 「The early warning notifications will be communicated via the channels below. #### Email Enforcement emails are sent to the email…」 | 无 | `calling_call_settings_md`｜第 86 句 |
| D　平台侧的处置动词 | enforce／enforcement（执法） | WhatsApp 帮助中心 | **11** | 5 | 「To learn more about policy and spam enforcement on the WhatsApp Business Platform, read the Meta Developer Documentation here .」 | 无 | `faq_1508178557633269`｜第 4 句 |
| D　平台侧的处置动词 | enforce／enforcement（执法） | 法律与政策页 | **15** | 3 | 「Enforcement and Updates We may limit or remove your access to or use of WhatsApp for Business, or limit your ability to send…」 | 有 | `legal_business_policy`｜第 82 句 |
| D　平台侧的处置动词 | appeal（申诉） | Meta 开发者文档 | **9** | 3 | 「Restricted or disabled accounts can still appeal or acknowledge issues.」 | 有 | `policy_enforcement_md`｜第 16 句 |
| D　平台侧的处置动词 | appeal（申诉） | WhatsApp 帮助中心 | **2** | 1 | 「About temporarily banned accounts After your account is unbanned About account bans for unofficial apps How to contact the…」 | 无 | `faq_1508178557633269`｜第 1 句 |
| D　平台侧的处置动词 | appeal（申诉） | 法律与政策页 | **1** | 1 | 「If you believe your account has not violated our policies or terms, please consult the following resources for more information…」 | 无 | `legal_business_policy`｜第 102 句 |
| D　平台侧的处置动词 | warn／warning（警告） | Meta 开发者文档 | **6** | 5 | 「The early warning notifications will be communicated via the channels below. #### Email Enforcement emails are sent to the email…」 | 无 | `calling_call_settings_md`｜第 86 句 |
| D　平台侧的处置动词 | warn／warning（警告） | WhatsApp 帮助中心 | **1** | 1 | 「If a significant number of users report or block your account due to unwanted messages, your business account will receive a…」 | 无 | `faq_740266744985664`｜第 10 句 |
| E　判定与检测 | detect（检测） | Meta 开发者文档 | **4** | 2 | 「WhatsApp protects users by detecting unusual messaging patterns (like a business trying to message all users) and collecting spam…」 | 无 | `data_privacy_and_security_md`｜第 11 句 |
| E　判定与检测 | detect（检测） | WhatsApp 帮助中心 | **4** | 3 | 「When businesses use optional services from Meta, Meta uses automated systems to detect violations of relevant terms and policies…」 | 无 | `faq_1148840052398648`｜第 19 句 |
| E　判定与检测 | detect（检测） | 法律与政策页 | **5** | 3 | 「WhatsApp may use both automated processing techniques and human review teams to detect and review information available to us…」 | 无 | `legal_business_policy`｜第 89 句 |
| E　判定与检测 | identify（识别） | Meta 开发者文档 | **4** | 3 | 「Returned as `user_id` in `added_users` only if you identified the user with `user_id` alone.」 | 无 | `block_users_md`｜第 19 句 |
| E　判定与检测 | identify（识别） | WhatsApp 帮助中心 | **6** | 4 | 「Because WhatsApp messages are end-to-end encrypted, we use information about you from WhatsApp users, information from your…」 | 无 | `faq_1805617343145907`｜第 2 句 |
| E　判定与检测 | identify（识别） | 法律与政策页 | **1** | 1 | 「Don’t share or ask people to share full length individual payment card numbers, financial account numbers, personal ID card…」 | 无 | `legal_business_policy`｜第 30 句 |
| E　判定与检测 | flag（标记） | Meta 开发者文档 | **6** | 3 | 「If a policy-violating item is added, the product is flagged and the business is directed to the WhatsApp Commerce Policies. -…」 | 无 | `catalogs_catalogs_overview_md`｜第 20 句 |
| E　判定与检测 | flag（标记） | WhatsApp 帮助中心 | **3** | 3 | 「This flags the specific message for review.」 | 无 | `faq_424124173736394`｜第 11 句 |
| E　判定与检测 | flag（标记） | 法律与政策页 | **1** | 1 | 「We may also reactively evaluate a business’ opt-in flows, including review of user feedback, to flag policy violations and…」 | 无 | `legal_business_policy`｜第 99 句 |
| E　判定与检测 | violat（违反与违规） | Meta 开发者文档 | **12** | 6 | 「You cannot send carts to customers. *Shopping cart experience example and expected behavior for item state change.* ## Policies…」 | 有 | `catalogs_catalogs_overview_md`｜第 17 句 |
| E　判定与检测 | violat（违反与违规） | WhatsApp 帮助中心 | **23** | 14 | 「When businesses use optional services from Meta, Meta uses automated systems to detect violations of relevant terms and policies…」 | 无 | `faq_1148840052398648`｜第 19 句 |
| E　判定与检测 | violat（违反与违规） | 法律与政策页 | **24** | 4 | 「If content or actions violate our terms or policies, we may limit or remove your access to our services.」 | 有 | `legal_business_policy`｜第 5 句 |
| E　判定与检测 | review（复核与审核） | Meta 开发者文档 | **21** | 6 | 「If a user reports a product, it is reviewed for violations.」 | 有 | `catalogs_catalogs_overview_md`｜第 21 句 |
| E　判定与检测 | review（复核与审核） | WhatsApp 帮助中心 | **22** | 13 | 「With a parent-managed account, parents or guardians can choose who can contact their child, decide which groups they can join,…」 | 无 | `faq_1374700371009788`｜第 2 句 |
| E　判定与检测 | review（复核与审核） | 法律与政策页 | **13** | 4 | 「To help ensure a strong user experience, review the best practices suggested below .」 | 无 | `legal_business_policy`｜第 16 句 |
| F　收件人侧的自助动作 | stop（停止接收） | Meta 开发者文档 | **2** | 2 | 「Only stop and resume actions trigger the webhook. ### Stop/resume controls WhatsApp users can use the…」 | 无 | `templates_marketing_templates_md`｜第 10 句 |
| F　收件人侧的自助动作 | stop（停止接收） | WhatsApp 帮助中心 | **18** | 11 | 「Avoid unwanted contact Block and report: You can stop receiving messages, calls, and status updates from a contact by blocking…」 | 无 | `faq_1003616827680090`｜第 23 句 |
| F　收件人侧的自助动作 | stop（停止接收） | 法律与政策页 | **3** | 2 | 「You must immediately stop sending messages to users in the allowed country if your required license expires, is modified, or is…」 | 无 | `legal_business_policy`｜第 54 句 |
| F　收件人侧的自助动作 | leave（退出群组） | Meta 开发者文档 | **1** | 1 | 「Click Account Overview (the speedometer icon) in the left-side menu, if necessary.」 | 无 | `policy_enforcement_md`｜第 10 句 |
| F　收件人侧的自助动作 | leave（退出群组） | WhatsApp 帮助中心 | **5** | 3 | 「Exiting a community: Members can leave a community they no longer want to be a part of by using the exit community feature.」 | 无 | `faq_1003616827680090`｜第 40 句 |
| F　收件人侧的自助动作 | opt out（退出订阅） | Meta 开发者文档 | **1** | 1 | 「To avoid template restrictions: - Send messages only to WhatsApp users who are expecting them. - Design content so the message is…」 | 无 | `direct_send_integrity_and_content_guidelines_md`｜第 4 句 |
| F　收件人侧的自助动作 | opt out（退出订阅） | WhatsApp 帮助中心 | **2** | 1 | 「You can set this expectation by: Getting their opt-in for the different categories of messages they should expect from your…」 | 无 | `faq_740266744985664`｜第 6 句 |
| F　收件人侧的自助动作 | opt out（退出订阅） | 法律与政策页 | **7** | 2 | 「You must respect all requests (either on or off WhatsApp) by a person to block, discontinue, or otherwise opt out of…」 | 有 | `legal_business_policy`｜第 17 句 |
| F　收件人侧的自助动作 | ignore／dismiss（忽略） | WhatsApp 帮助中心 | **1** | 1 | 「Dismiss other admins who may be causing harm or disruption to the group.」 | 无 | `faq_424124173736394`｜第 52 句 |
| F　收件人侧的自助动作 | ignore／dismiss（忽略） | 法律与政策页 | **1** | 1 | 「This means that if we or you do not bring a Claim (including commencing an arbitration) within one year after the Dispute first…」 | 无 | `legal_tos`｜第 114 句 |
| F　收件人侧的自助动作 | settings（设置项） | Meta 开发者文档 | **2** | 2 | 「Use these endpoints to view and configure call settings for the Calling API.」 | 无 | `calling_call_settings_md`｜第 3 句 |
| F　收件人侧的自助动作 | settings（设置项） | WhatsApp 帮助中心 | **26** | 14 | 「You can also adjust your privacy settings to prevent unknown contacts from adding you to groups.」 | 无 | `faq_1142481766359885`｜第 12 句 |
| F　收件人侧的自助动作 | privacy（隐私设置） | Meta 开发者文档 | **1** | 1 | 「To the extent any data protection or privacy law applicable to you recognizes the concepts of "data processor" or "service…」 | 无 | `data_privacy_and_security_md`｜第 14 句 |
| F　收件人侧的自助动作 | privacy（隐私设置） | WhatsApp 帮助中心 | **22** | 12 | 「Your privacy is our priority We’ve built several features to ensure the privacy of your account.」 | 无 | `faq_1003616827680090`｜第 8 句 |
| F　收件人侧的自助动作 | privacy（隐私设置） | 法律与政策页 | **12** | 4 | 「Protect Data and Comply with Law You are responsible for and must secure all necessary notices, permissions, and consents to…」 | 无 | `legal_business_policy`｜第 28 句 |
| G　业务侧屏蔽接口 | per-number（按号码粒度） | Meta 开发者文档 | **1** | 1 | 「The API returns errors per-number, because a block can succeed on some numbers and fail on others.」 | 无 | `block_users_md`｜第 3 句 |
| G　业务侧屏蔽接口 | error（错误返回） | Meta 开发者文档 | **16** | 4 | 「Attempts to message a blocked user return an error.」 | 有 | `block_users_md`｜第 2 句 |
| G　业务侧屏蔽接口 | success／fail（调用结果两态） | Meta 开发者文档 | **5** | 4 | 「The API returns errors per-number, because a block can succeed on some numbers and fail on others.」 | 无 | `block_users_md`｜第 3 句 |
| G　业务侧屏蔽接口 | success／fail（调用结果两态） | 法律与政策页 | **2** | 1 | 「Our Services may be interrupted, including for maintenance, repairs, upgrades, or network or equipment failures.」 | 无 | `legal_tos`｜第 118 句 |
| G　业务侧屏蔽接口 | request（请求） | Meta 开发者文档 | **19** | 8 | 「One way to obtain calling permissions is to request permission when a WhatsApp user calls your business.」 | 无 | `calling_call_settings_md`｜第 19 句 |
| G　业务侧屏蔽接口 | request（请求） | WhatsApp 帮助中心 | **19** | 6 | 「With a parent-managed account, parents or guardians can choose who can contact their child, decide which groups they can join,…」 | 无 | `faq_1374700371009788`｜第 2 句 |
| G　业务侧屏蔽接口 | request（请求） | 法律与政策页 | **8** | 3 | 「You must respect all requests (either on or off WhatsApp) by a person to block, discontinue, or otherwise opt out of…」 | 无 | `legal_business_policy`｜第 17 句 |
| G　业务侧屏蔽接口 | response（响应） | Meta 开发者文档 | **7** | 4 | 「The same integrity rules that apply to user-created templates also apply to Direct Send generated templates. ## Template pausing…」 | 无 | `direct_send_integrity_and_content_guidelines_md`｜第 2 句 |
| G　业务侧屏蔽接口 | response（响应） | WhatsApp 帮助中心 | **5** | 5 | 「Meta will receive these chats and may generate message responses.」 | 无 | `faq_1148840052398648`｜第 10 句 |
| H　质量、暂停与负反馈 | quality rating（质量评级） | Meta 开发者文档 | **2** | 1 | 「Address the issues that WhatsApp users report. - `UNKNOWN` — Indicates a quality score is still pending, because it has yet to…」 | 无 | `templates_template_quality_md`｜第 9 句 |
| H　质量、暂停与负反馈 | pause／paused（模板暂停） | Meta 开发者文档 | **17** | 5 | 「While paused, the calling phone number will be unable to: - Make business-initiated calls to users - Send call permissions…」 | 有 | `calling_call_settings_md`｜第 89 句 |
| H　质量、暂停与负反馈 | pause／paused（模板暂停） | WhatsApp 帮助中心 | **1** | 1 | 「Pause and think Look out for suspicious behavior: Are they using an unknown number?」 | 无 | `faq_573786218075805`｜第 5 句 |
| H　质量、暂停与负反馈 | pause／paused（模板暂停） | 法律与政策页 | **1** | 1 | 「We have the right to review, approve, pause and reject any Message Template at any time.」 | 无 | `legal_business_policy`｜第 22 句 |
| H　质量、暂停与负反馈 | negative／feedback（负反馈） | Meta 开发者文档 | **11** | 4 | 「We might also limit or offboard your business from WhatsApp if your account receives excessive negative feedback from users.」 | 无 | `policy_enforcement_md`｜第 5 句 |
| H　质量、暂停与负反馈 | negative／feedback（负反馈） | 法律与政策页 | **1** | 1 | 「Enforcement and Updates We may limit or remove your access to or use of WhatsApp for Business, or limit your ability to send…」 | 无 | `legal_business_policy`｜第 82 句 |
| H　质量、暂停与负反馈 | whatsapp manager（平台侧管理入口） | Meta 开发者文档 | **6** | 4 | 「To access calling controls in WhatsApp Manager: 1.」 | 无 | `calling_call_settings_md`｜第 63 句 |
| I　限额与可达性 | limit（限额） | Meta 开发者文档 | **28** | 12 | 「Otherwise, updates are real-time for a limited number of users in conversation with the business, and are eventual for the rest…」 | 有 | `calling_call_settings_md`｜第 13 句 |
| I　限额与可达性 | limit（限额） | WhatsApp 帮助中心 | **10** | 6 | 「There are optional services that a business or you can choose to use where Meta receives limited information.」 | 无 | `faq_1148840052398648`｜第 12 句 |
| I　限额与可达性 | limit（限额） | 法律与政策页 | **15** | 3 | 「If content or actions violate our terms or policies, we may limit or remove your access to our services.」 | 有 | `legal_business_policy`｜第 5 句 |
| I　限额与可达性 | messaging limit（消息限额） | Meta 开发者文档 | **8** | 4 | 「This document describes messaging limits for the WhatsApp Business Platform.」 | 有 | `messaging_limits_md`｜第 2 句 |
| I　限额与可达性 | per-user limit（按人上限） | Meta 开发者文档 | **4** | 1 | 「The per-user marketing limit adapts automatically over time based on a person's recent engagement levels.」 | 无 | `templates_marketing_templates_per_user_limits_md`｜第 3 句 |
| I　限额与可达性 | delivery（投递） | Meta 开发者文档 | **8** | 4 | 「You can enable G.711 (PCMA/PCMU) codecs for interoperability with legacy telephony systems or PSTN gateways. #### Guidelines and…」 | 有 | `calling_call_settings_md`｜第 39 句 |
| I　限额与可达性 | delivery（投递） | WhatsApp 帮助中心 | **7** | 4 | 「When you message a WhatsApp business account, your message is delivered securely to the business.」 | 有 | `faq_1148840052398648`｜第 2 句 |
| I　限额与可达性 | delivery（投递） | 法律与政策页 | **2** | 1 | 「We provide, and always strive to improve, ways for you and businesses and other organizations, to communicate with each other…」 | 有 | `legal_tos`｜第 18 句 |
| I　限额与可达性 | template（模板） | Meta 开发者文档 | **65** | 7 | 「The same integrity rules that apply to user-created templates also apply to Direct Send generated templates. ## Template pausing…」 | 有 | `direct_send_integrity_and_content_guidelines_md`｜第 2 句 |
| I　限额与可达性 | template（模板） | 法律与政策页 | **5** | 1 | 「WhatsApp Business Platform Specific Terms You may only initiate conversations using an approved Message Template (as defined in…」 | 无 | `legal_business_policy`｜第 20 句 |
| J　法律与准则层 | harassing（形容词形态，准则页的用词） | WhatsApp 帮助中心 | **2** | 2 | 「Our Terms of Service outline prohibited activities, which include sharing content (in the status, profile photos, or messages)…」 | 无 | `faq_1313491802751163`｜第 4 句 |
| J　法律与准则层 | prohibited（禁止事项） | WhatsApp 帮助中心 | **2** | 2 | 「Our Terms of Service outline prohibited activities, which include sharing content (in the status, profile photos, or messages)…」 | 无 | `faq_1313491802751163`｜第 4 句 |
| J　法律与准则层 | prohibited（禁止事项） | 法律与政策页 | **17** | 2 | 「Don’t use WhatsApp for telemedicine or to send or request any health related information, if applicable regulations prohibit…」 | 无 | `legal_business_policy`｜第 31 句 |
| J　法律与准则层 | policy（政策） | Meta 开发者文档 | **13** | 6 | 「You cannot send carts to customers. *Shopping cart experience example and expected behavior for item state change.* ## Policies…」 | 有 | `catalogs_catalogs_overview_md`｜第 17 句 |
| J　法律与准则层 | policy（政策） | WhatsApp 帮助中心 | **11** | 8 | 「When businesses use optional services from Meta, Meta uses automated systems to detect violations of relevant terms and policies…」 | 无 | `faq_1148840052398648`｜第 19 句 |
| J　法律与准则层 | policy（政策） | 法律与政策页 | **35** | 4 | 「Creating valuable experiences for users relies on the compliance of businesses with the WhatsApp Business Messaging Policy below.」 | 无 | `legal_business_policy`｜第 2 句 |
| J　法律与准则层 | terms（条款） | Meta 开发者文档 | **2** | 2 | 「When a business chooses to use these services, different terms could apply.」 | 无 | `data_privacy_and_security_md`｜第 4 句 |
| J　法律与准则层 | terms（条款） | WhatsApp 帮助中心 | **17** | 9 | 「When businesses use optional services from Meta, Meta uses automated systems to detect violations of relevant terms and policies…」 | 无 | `faq_1148840052398648`｜第 19 句 |
| J　法律与准则层 | terms（条款） | 法律与政策页 | **44** | 3 | 「If content or actions violate our terms or policies, we may limit or remove your access to our services.」 | 有 | `legal_business_policy`｜第 5 句 |
| J　法律与准则层 | illegal（违法内容） | WhatsApp 帮助中心 | **5** | 3 | 「Our Terms of Service outline prohibited activities, which include sharing content (in the status, profile photos, or messages)…」 | 无 | `faq_1313491802751163`｜第 4 句 |
| J　法律与准则层 | illegal（违法内容） | 法律与政策页 | **2** | 2 | 「We prohibit the use of WhatsApp for Business for operating, buying, selling, promoting, or otherwise facilitating the exchange of…」 | 无 | `legal_business_policy`｜第 36 句 |
| J　法律与准则层 | community standards（社区准则） | 法律与政策页 | **2** | 1 | 「This is why we have the Community Standards , which define what is and is not allowed in our community.」 | 无 | `legal_transparency_report`｜第 3 句 |
| K　自我保护口径 | scam／scammer（诈骗） | WhatsApp 帮助中心 | **23** | 10 | 「About reporting and blocking on WhatsApp How to block a group admin Being blocked by someone How to report someone’s status…」 | 无 | `faq_1142481766359885`｜第 1 句 |
| K　自我保护口径 | scam／scammer（诈骗） | 法律与政策页 | **3** | 2 | 「Do not engage in fraud or spam This includes repeated unwanted contact with WhatsApp users, and activity that scams or defrauds…」 | 无 | `legal_messaging_guidelines`｜第 17 句 |
| K　自我保护口径 | suspicious（可疑） | WhatsApp 帮助中心 | **16** | 9 | 「About reporting and blocking on WhatsApp How to block a group admin Being blocked by someone How to report someone’s status…」 | 无 | `faq_1142481766359885`｜第 1 句 |
| K　自我保护口径 | suspicious（可疑） | 法律与政策页 | **1** | 1 | 「Proactive security alerts We prompt you to verify your identity if we detect any suspicious or unauthorized attempts to take over…」 | 无 | `legal_security_center`｜第 7 句 |
| K　自我保护口径 | phishing／link（钓鱼与链接） | Meta 开发者文档 | **1** | 1 | 「With the introduction of Template Pacing, you can now unpause any paused template through: - The API by making a POST request to…」 | 无 | `templates_template_pausing_md`｜第 18 句 |
| K　自我保护口径 | phishing／link（钓鱼与链接） | WhatsApp 帮助中心 | **22** | 11 | 「Channels Accounts and Account Bans Payments WhatsApp for Business How to keep your groups and community safe Copy link WhatsApp…」 | 无 | `faq_1003616827680090`｜第 2 句 |
| K　自我保护口径 | verify（核实身份） | Meta 开发者文档 | **8** | 3 | 「If a parent business portfolio is unverified, each of its Messaging accounts is limited to 250 message templates.」 | 有 | `templates_overview_md`｜第 41 句 |
| K　自我保护口径 | verify（核实身份） | WhatsApp 帮助中心 | **13** | 4 | 「Channels Accounts and Account Bans Payments WhatsApp for Business How to keep your groups and community safe Copy link WhatsApp…」 | 无 | `faq_1003616827680090`｜第 2 句 |
| K　自我保护口径 | verify（核实身份） | 法律与政策页 | **2** | 1 | 「Proactive security alerts We prompt you to verify your identity if we detect any suspicious or unauthorized attempts to take over…」 | 无 | `legal_security_center`｜第 7 句 |
| K　自我保护口径 | personal information（个人信息） | WhatsApp 帮助中心 | **8** | 4 | 「Be cautious of messages from unfamiliar usernames, especially those asking for personal information or money.」 | 无 | `faq_2090730848486858`｜第 31 句 |
| K　自我保护口径 | safety／safe（安全） | Meta 开发者文档 | **1** | 1 | 「We will ensure that our businesses and partners can continue to enjoy business solutions while keeping their data safe and secure.」 | 无 | `data_privacy_and_security_md`｜第 31 句 |
| K　自我保护口径 | safety／safe（安全） | WhatsApp 帮助中心 | **39** | 18 | 「About building private, safe, and secure communities on WhatsApp What does "This community is no longer available" mean?」 | 无 | `faq_1003616827680090`｜第 1 句 |
| K　自我保护口径 | safety／safe（安全） | 法律与政策页 | **10** | 4 | 「You must ensure – through technical and organizational measures – that messages are gated per legal age, geographic requirements,…」 | 无 | `legal_business_policy`｜第 32 句 |
| L　举报信息面 | collect（收集） | Meta 开发者文档 | **2** | 2 | 「WhatsApp protects users by detecting unusual messaging patterns (like a business trying to message all users) and collecting spam…」 | 无 | `data_privacy_and_security_md`｜第 11 句 |
| L　举报信息面 | collect（收集） | WhatsApp 帮助中心 | **1** | 1 | 「How WhatsApp limits bad experiences and business message overload In order to limit bad experiences (like spam) and manage the…」 | 无 | `faq_263784176043634`｜第 4 句 |
| L　举报信息面 | collect（收集） | 法律与政策页 | **5** | 2 | 「Protect Data and Comply with Law You are responsible for and must secure all necessary notices, permissions, and consents to…」 | 无 | `legal_business_policy`｜第 28 句 |
| L　举报信息面 | information（信息） | Meta 开发者文档 | **6** | 5 | 「You can set your SIP server information and click the Save button to save your configuration.」 | 有 | `calling_call_settings_md`｜第 68 句 |
| L　举报信息面 | information（信息） | WhatsApp 帮助中心 | **48** | 17 | 「Information about when the message was sent, and the type of message sent such as an image, video, or text.」 | 无 | `faq_1142481766359885`｜第 73 句 |
| L　举报信息面 | information（信息） | 法律与政策页 | **27** | 3 | 「Maintain a WhatsApp Business profile with customer support contact information and one or more of the following: email address,…」 | 无 | `legal_business_policy`｜第 8 句 |
| L　举报信息面 | share／provide（提供给平台或其他方） | Meta 开发者文档 | **2** | 2 | 「Messaging limits are calculated and set at the business portfolio level and are shared by all business phone numbers within a…」 | 无 | `messaging_limits_md`｜第 4 句 |
| L　举报信息面 | share／provide（提供给平台或其他方） | WhatsApp 帮助中心 | **41** | 15 | 「Share this page with your members as a quick reference guide.」 | 有 | `faq_1003616827680090`｜第 7 句 |
| L　举报信息面 | share／provide（提供给平台或其他方） | 法律与政策页 | **10** | 5 | 「Protect Data and Comply with Law You are responsible for and must secure all necessary notices, permissions, and consents to…」 | 无 | `legal_business_policy`｜第 28 句 |
| L　举报信息面 | retain／store（保存） | Meta 开发者文档 | **1** | 1 | 「How long is it stored? Cloud API messages at rest are encrypted.」 | 无 | `data_privacy_and_security_md`｜第 22 句 |
| L　举报信息面 | retain／store（保存） | WhatsApp 帮助中心 | **9** | 4 | 「Some businesses can choose optional services to interact with customers: For example, some businesses can choose to use services…」 | 无 | `faq_1148840052398648`｜第 6 句 |
| L　举报信息面 | retain／store（保存） | 法律与政策页 | **4** | 2 | 「These escalation paths include: In-Chat Human Agent transfer Phone number Email Web support (on the business website) In-store…」 | 有 | `legal_business_policy`｜第 27 句 |
| L　举报信息面 | content（内容） | Meta 开发者文档 | **7** | 2 | 「To avoid template restrictions: - Send messages only to WhatsApp users who are expecting them. - Design content so the message is…」 | 无 | `direct_send_integrity_and_content_guidelines_md`｜第 4 句 |
| L　举报信息面 | content（内容） | WhatsApp 帮助中心 | **31** | 13 | 「Use view once for shared content: For added privacy, you can send photos and videos that disappear from your chat after they've…」 | 无 | `faq_1003616827680090`｜第 18 句 |
| L　举报信息面 | content（内容） | 法律与政策页 | **12** | 4 | 「If content or actions violate our terms or policies, we may limit or remove your access to our services.」 | 有 | `legal_business_policy`｜第 5 句 |
| L　举报信息面 | account（账号） | Meta 开发者文档 | **29** | 10 | 「Next, display the calling buttons by either working with your partner or going to WhatsApp Manager > Account tools > Phone…」 | 有 | `calling_call_settings_md`｜第 95 句 |
| L　举报信息面 | account（账号） | WhatsApp 帮助中心 | **91** | 24 | 「Channels Accounts and Account Bans Payments WhatsApp for Business How to keep your groups and community safe Copy link WhatsApp…」 | 有 | `faq_1003616827680090`｜第 2 句 |
| L　举报信息面 | account（账号） | 法律与政策页 | **37** | 4 | 「We may consider your account to be violating this Business Messaging Policy, the WhatsApp Messaging Guidelines , our terms of…」 | 无 | `legal_business_policy`｜第 13 句 |
| M　本篇问而句池无答 | receipt／confirmation（举报回执） | Meta 开发者文档 | **2** | 1 | 「Inspect any media files that are causing errors and confirm that they are in fact supported.」 | 无 | `support_error_codes_md`｜第 78 句 |
| M　本篇问而句池无答 | receipt／confirmation（举报回执） | WhatsApp 帮助中心 | **12** | 6 | 「By default, WhatsApp sets your privacy settings to allow: Any user to see your profile photo, read receipts and when you're…」 | 无 | `faq_1313491802751163`｜第 24 句 |
| M　本篇问而句池无答 | receipt／confirmation（举报回执） | 法律与政策页 | **2** | 2 | 「You may only contact people on WhatsApp if: (a) they have given you their mobile phone number or username; and (b) you have…」 | 无 | `legal_business_policy`｜第 14 句 |
| M　本篇问而句池无答 | timeline／how long（处理时限） | Meta 开发者文档 | **2** | 2 | 「How long is it stored? Cloud API messages at rest are encrypted.」 | 有 | `data_privacy_and_security_md`｜第 22 句 |
| M　本篇问而句池无答 | timeline／how long（处理时限） | WhatsApp 帮助中心 | **3** | 3 | 「Control what you send and see Send disappearing messages: You can decide how long messages remain visible once you've sent them -…」 | 有 | `faq_1003616827680090`｜第 16 句 |
| M　本篇问而句池无答 | timeline／how long（处理时限） | 法律与政策页 | **1** | 1 | 「To opt out, you must notify us in writing postmarked within 30 days of the later of: (a) the date that you first accepted our…」 | 有 | `legal_tos`｜第 175 句 |
| M　本篇问而句池无答 | restore／reinstate（处置后恢复） | Meta 开发者文档 | **1** | 1 | 「`REINSTATE` — Indicates your WhatsApp account has been reinstated.」 | 无 | `webhooks_reference_account_update_md`｜第 67 句 |
| M　本篇问而句池无答 | restore／reinstate（处置后恢复） | WhatsApp 帮助中心 | **3** | 1 | 「Access to the marketing messages will be automatically reinstated on the date and time shown in the app.」 | 无 | `faq_740266744985664`｜第 12 句 |
| **合计** | **82** 个词（组）× **4** 类站点 | 非空格 **176** 个 | 句次 **2119** | 一句可落多格 | 代表句按池内顺序取第一句 | 零命中词 **8** 个 | 回表失败 **0** 句 |

#ROWS=176 COLS=8


# 附表 D 官方写明的取值域全表（19 行）

取组口径：脚本扫过 **61** 个已登记页面（站点类别：Meta 开发者文档）的每一行原文，配置了 10 个取值域标记，本轮真正在这些页面里出现过的有 **3** 个（`Values can be` 20 行／`Possible values` 1 行／`one of the following` 1 行，合计 **22** 行），另有 **7** 个（Supported values are／Can be one of／must be one of／The following values are supported／Valid values are／Options are／Enum Values）在扫过的每一行原文里零命中——这几串字符只是脚本配置表里的名字，本轮官方页面没有一处用过这种写法，所以本篇不给它们加反引号、也不把它们当作任何官方句子的引文。取到值来的页面 **5** 个（站点类别：Meta 开发者文档）。按标记所在行的形态分档取值：自动·参数表说明格 18。全表 **18** 组、共 **92** 个取值、去重后的取值名 **74** 个。其中 **0** 组的取值是不带反引号的纯文本子项——反引号是接口文档的排版，帮助中心同样用「one of the following」加子项写成正式取值域，这一档整项照收、只丢超过 80 字符的说明句；同页同字段同取值的重复写法（帮助中心按平台页签各写一遍）合并为一行，共合并 **0** 处。取值本身带空格的共 **6** 个（如 `LEGAL NAME NOT FOUND IN DOCUMENTS`、`No longer needed`），这一批只能按「反引号紧跟破折号说明」的位置认出，脚本对这种写法额外兜一遍：同一格里见到两个以上这种位置才收，并且只在这一档放开取值不能带空格的限制——接口文档的枚举里确有带空格的大写取值，卡成单词会漏掉整组。

为什么这一表不走原句池：官方把取值域写在接口参数表的说明格里、缩进的子项列表里、整句内联的清单里，这些行要么带竖线、要么是列表行，本来就被池子的入池条件挡在外面。所以 D 的核验独立于 C：每一组把它自己那一行原文、缩进子项式还把每个取值所在的那一行原文，逐字回表到 `_tmp/w6_a5s/` 的对应页面，失败 **0** 行。

落空登记：扫到标记但取不到两个以上取值的共 **4** 处，原因由脚本按同一行的证据判，不由人挑：官方给的"取值"位上是带变量或带说明的整句，不是标识符 2／取值清单交叉引用到别的页面，本页没有写出取值 1／标记句下面紧跟的不是子项列表（官方把选择写在正文段落里，没写成清单） 1。**扫到标记不等于官方写明了取值域**，这几处不算进表，但算进这一句，让读者知道漏了多少、为什么漏。这 **4** 处分布在 **4** 个页面，每一处的页面标识、原文行号、判定档位、命中的标记、落空判据与该行未截断的原文全部逐条写进 `_tmp/w6_a5_dmiss.tsv`（与上面的次数由脚本断言相等，不是"约"），可逐条回原文核对。

单组取值上限设为 **24** 个：实测最多的一组有 **14** 个取值，没有一组触及上限，所以本表没有因上限而丢过取值。

取值列表的截断按整取值进行（顿号相连的列表没有空格，按词截会把一整组截到只剩一项）：单元格 **400** 字符内装得下的原样列出，装不下的整项去掉并在行内写明「另有 N 个取值未列出」。本轮实测 **0** 组触及这一上限、被去掉的取值 **0** 个，也就是说表里列出的取值就是脚本收到的全部 **92** 个，没有一组需要读者回原文补齐。

| 组别 | 页面标识 | 行号 | 字段／属性 | 官方标记 | 取值个数 | 官方写明的取值 |
|---|---|---|---|---|---|---|
| 自动·参数表说明格 | `webhooks_reference_account_alerts_md` | 39 | `<ALERT_SEVERITY>` | Values can be | **3** | `CRITICAL`、`INFORMATIONAL`、`WARNING` |
| 自动·参数表说明格 | `webhooks_reference_account_alerts_md` | 40 | `<ALERT_STATUS>` | Values can be | **2** | `ACTIVE`、`NONE` |
| 自动·参数表说明格 | `webhooks_reference_account_alerts_md` | 41 | `<ALERT_TYPE>` | Values can be | **6** | `INCREASED_CAPABILITIES_ELIGIBILITY_DEFERRED`、`INCREASED_CAPABILITIES_ELIGIBILITY_FAILED`、`INCREASED_CAPABILITIES_ELIGIBILITY_NEED_MORE_INFO`、`OBA_APPROVED`、`OBA_REJECTED`、`PROFILE_PICTURE_LOST` |
| 自动·参数表说明格 | `webhooks_reference_account_alerts_md` | 43 | `<ENTITY_TYPE>` | Values can be | **3** | `BUSINESS`、`PHONE_NUMBER`、`CURRENT_STATUS_ID` |
| 自动·参数表说明格 | `webhooks_reference_account_update_md` | 126 | `<DISCONNECTION_INITIATED_BY>` | Values can be | **2** | `SYSTEM`、`USER` |
| 自动·参数表说明格 | `webhooks_reference_account_update_md` | 127 | `<DISCONNECTION_REASON>` | Values can be | **6** | `ACCOUNT_DISCONNECTED`、`BUSINESS_DOWNGRADE`、`CHANGE_NUMBER`、`COMPANION_INACTIVITY`、`PRIMARY_INACTIVITY`、`USER_RE_REGISTERED` |
| 自动·参数表说明格 | `webhooks_reference_account_update_md` | 125 | `<EVENT>` | Values can be | **14** | `ACCOUNT_DELETED`、`ACCOUNT_RESTRICTION`、`ACCOUNT_VIOLATION`、`AD_ACCOUNT_LINKED`、`AUTH_INTL_PRICE_ELIGIBILITY_UPDATE`、`BUSINESS_PRIMARY_LOCATION_COUNTRY_UPDATE`、`DISABLED_UPDATE`、`MM_LITE_TERMS_SIGNED`、`PARTNER_ADDED`、`PARTNER_APP_INSTALLED`、`PARTNER_APP_UNINSTALLED`、`PARTNER_CLIENT_CERTIFICATION_STATUS_UPDATE`、`PARTNER_REMOVED`、`VOLUME_BASED_PRICING_TIER_UPDATE` |
| 自动·参数表说明格 | `webhooks_reference_account_update_md` | 135 | `<REJECTION_REASONS>` | Values can be | **7** | `ADDRESS NOT MATCHING`、`BUSINESS NOT ELIGIBLE`、`LEGAL NAME NOT MATCHING`、`LEGAL NAME NOT FOUND IN DOCUMENTS`、`MALFORMED DOCUMENTS`、`NONE`、`WEBSITE NOT MATCHING` |
| 自动·参数表说明格 | `webhooks_reference_account_update_md` | 137 | `<RESTRICTION_TYPE>` | Values can be | **9** | `RESTRICTED_ADD_PHONE_NUMBER_ACTION`、`RESTRICTED_BIZ_INITIATED_AND_USER_INITIATED_CALLING`、`RESTRICTED_BIZ_INITIATED_MESSAGING`、`RESTRICTED_BUSINESS_INITIATED_CALLING`、`RESTRICTED_CUSTOMER_INITIATED_MESSAGING`、`RESTRICTED_DIRECT_SEND_UTILITY_TEMPLATES`、`RESTRICTED_USER_INITIATED_CALLING`、`RESTRICTED_USER_INITIATED_CALLING_CALL_BUTTON_HIDDEN`、`RESTRICTED_UTILITY_TEMPLATES` |
| 自动·参数表说明格 | `webhooks_reference_account_update_md` | 140 | `<STATUS>` | Values can be | **5** | `APPROVED`、`DISCARDED`、`FAILED`、`PENDING`、`REVOKED` |
| 自动·参数表说明格 | `webhooks_reference_account_update_md` | 144 | `<WABA_BAN_STATE>` | Values can be | **3** | `DISABLE`、`REINSTATE`、`SCHEDULE_FOR_DISABLE` |
| 自动·参数表说明格 | `webhooks_reference_message_template_quality_update_md` | 33 | `<NEW_QUALITY_SCORE>` | Values can be | **4** | `GREEN`、`RED`、`YELLOW`、`UNKNOWN` |
| 自动·参数表说明格 | `webhooks_reference_message_template_quality_update_md` | 34 | `<PREVIOUS_QUALITY_SCORE>` | Values can be | **4** | `GREEN`、`RED`、`YELLOW`、`UNKNOWN` |
| 自动·参数表说明格 | `webhooks_reference_phone_number_quality_update_md` | 34 | `<CURRENT_LIMIT>` | Values can be | **7** | `TIER_50`、`TIER_250`、`TIER_2K`、`TIER_10K`、`TIER_100K`、`TIER_NOT_SET`、`TIER_UNLIMITED` |
| 自动·参数表说明格 | `webhooks_reference_phone_number_quality_update_md` | 35 | `<EVENT>` | Values can be | **2** | `ONBOARDING`、`THROUGHPUT_UPGRADE` |
| 自动·参数表说明格 | `webhooks_reference_phone_number_quality_update_md` | 36 | `<MAX_DAILY_MESSAGES_LIMIT>` | Values can be | **7** | `TIER_50`、`TIER_250`、`TIER_2K`、`TIER_10K`、`TIER_100K`、`TIER_NOT_SET`、`TIER_UNLIMITED` |
| 自动·参数表说明格 | `webhooks_reference_phone_number_quality_update_md` | 37 | `<OLD_LIMIT>` | Values can be | **6** | `TIER_50`、`TIER_250`、`TIER_2K`、`TIER_10K`、`TIER_100K`、`TIER_NOT_SET` |
| 自动·参数表说明格 | `webhooks_reference_user_preferences_md` | 61 | `<PREFERENCE>` | Values can be | **2** | `stop`、`resume` |
| **合计** | **5** 页 | 行号为原文行序 | 字段名取自官方写法 | 标记 1 种 | 取值 **92** 个 | 去重取值名 **74** 个 |

#ROWS=18 COLS=7


# 附表 E 同一组维度在本轴各主体上的逐列对照（16 行）

取格口径：本表 **15** 个对照维度 × **9** 列 = **135** 格，其中 **68** 格取到了官方原句、**67** 格标作「官方未按这一列写该项」。每一格的取法都是「页面标识＋一条正则」交给脚本在原句池里取句，在该列的页面范围内命中 0 句或命中多句都会让生成脚本中止，所以表里没有一格是人工挑出来的。原句池共 **1194** 句（已由附表 B 逐字回表），本表用去重后的页面 **28** 个；同一句在多个维度或多列复用的共 **4** 次，复用时页标识与句序不变。

列的定义写在 `_tmp/w6_a5_ecols.tsv` 里，每列要么按站点类别取页（），要么按人工点名的页清单取页（个人账号侧帮助中心＝6 页／举报对象专用页＝7 页／家长管理账号侧＝2 页／群组与社区侧＝3 页／业务接口侧＝3 页／营销与模板治理侧＝8 页／执法与申诉文档＝4 页／法律与政策页＝4 页／用户偏好与退出信号＝3 页）。空格的含义只有一个：官方在这批登记页面里没按这一列写这一项，**不代表该主体没有这项规则**。

官方原句超过 112 字符按整词截断并以省略号结尾，共 **34** 格截断；截断只发生在显示层，取句与逐字回表用的是未截断的整句。各列取到句子的格数：个人账号侧帮助中心 12／举报对象专用页 9／家长管理账号侧 5／群组与社区侧 9／业务接口侧 4／营销与模板治理侧 8／执法与申诉文档 7／法律与政策页 10／用户偏好与退出信号 4。

| 编号 | 对照维度 | 个人账号侧帮助中心 | 举报对象专用页 | 家长管理账号侧 | 群组与社区侧 | 业务接口侧 | 营销与模板治理侧 | 执法与申诉文档 | 法律与政策页 | 用户偏好与退出信号 |
|---|---|---|---|---|---|---|---|---|---|---|
| E1   | 屏蔽在收件侧的即时效果 | 「Block a contact, business, or unknown user When you block someone, you’ll no longer receive their messages,…」（`faq_414631957536067`｜第 24 句） | 「Select Block if you want to make sure they cannot message or call you.」（`faq_2090730848486858`｜第 14 句） | 「Parent-managed account users can block or report someone on WhatsApp to stop receiving their messages and calls.」（`faq_1374700371009788`｜第 26 句） | 「When you select Block contact , you’ll no longer receive messages or calls from them.」（`faq_424124173736394`｜第 17 句） | 「Attempts to message a blocked user return an error.」（`block_users_md`｜第 2 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Blocking a business removes your number from their contact list.」（`faq_27563559673256587`｜第 10 句） |
| E2   | 举报会把哪些东西送到平台 | 「When you report a user in an individual chat, WhatsApp receives up to five of the last messages they’ve sent to…」（`faq_414631957536067`｜第 8 句） | 官方未按这一列写该项 | 「Tap Report When you choose to report someone, the last 5 messages sent to the managed account will be sent to…」（`faq_1561915401728939`｜第 11 句） | 「WhatsApp receives the reported user ID, information on when the message was sent, and the type of message sent…」（`faq_424124173736394`｜第 20 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「WhatsApp may take action against violations of our terms of service, our policies, or these Guidelines, based…」（`legal_messaging_guidelines`｜第 10 句） | 官方未按这一列写该项 |
| E3   | 举报之后的通知与回执 | 「We’ll acknowledge receipt of your report, but we will not contact you again.」（`faq_414631957536067`｜第 23 句） | 「Completed reviews are labeled Report reviewed .」（`faq_3440358352849077`｜第 17 句） | 「Parents will be notified when this action occurs through an activity alert.」（`faq_1561915401728939`｜第 4 句） | 「The message will be sent to WhatsApp, and the sender or group won’t be notified.」（`faq_424124173736394`｜第 19 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E4   | 谁能解除这个状态 | 「Tap to unblock. under the blocked user’s name and in your chat with them.」（`faq_414631957536067`｜第 31 句） | 「Only the business can remove the block, and it can do this at any time.」（`faq_2470686849751285`｜第 10 句） | 官方未按这一列写该项 | 「Exiting a community: Members can leave a community they no longer want to be a part of by using the exit…」（`faq_1003616827680090`｜第 40 句） | 官方未按这一列写该项 | 「With the introduction of Template Pacing, you can now unpause any paused template through: - The API by making…」（`templates_template_pausing_md`｜第 18 句） | 「If issues are reversed following the appeal or acknowledgment, the account returns to its previous status. ##…」（`policy_enforcement_md`｜第 17 句） | 「You must respect all requests (either on or off WhatsApp) by a person to block, discontinue, or otherwise opt…」（`legal_business_policy`｜第 17 句） | 「What you can expect After signing up, the business can send you marketing messages on WhatsApp, which may…」（`faq_27563559673256587`｜第 5 句） |
| E5   | 官方写不受欢迎消息时用的词 | 「Unwanted messages : If a contact asks you to stop messaging them, you should remove the contact from your…」（`faq_361005896189245`｜第 18 句） | 「WhatsApp takes impersonation and abuse seriously.」（`faq_2090730848486858`｜第 5 句） | 官方未按这一列写该项 | 「Report the group: Use when the entire group is being used for problematic or illegal activity.」（`faq_424124173736394`｜第 14 句） | 官方未按这一列写该项 | 「WhatsApp determines this based on several factors, including a dynamic view of an individual's recent marketing…」（`templates_marketing_templates_per_user_limits_md`｜第 2 句） | 「We might also limit or offboard your business from WhatsApp if your account receives excessive negative…」（`policy_enforcement_md`｜第 5 句） | 「Do not engage in fraud or spam This includes repeated unwanted contact with WhatsApp users, and activity that…」（`legal_messaging_guidelines`｜第 17 句） | 「How WhatsApp limits bad experiences and business message overload In order to limit bad experiences (like spam)…」（`faq_263784176043634`｜第 4 句） |
| E6   | 处置动作在各层被写成哪些动词 | 「We may ban a user if we believe that user is violating our Terms of Service.」（`faq_1313491802751163`｜第 5 句） | 「After review, we may remove usernames or ban accounts that we believe are in violation of our or Business Terms…」（`faq_2090730848486858`｜第 7 句） | 官方未按这一列写该项 | 「Delete unwanted messages or remove members.」（`faq_1003616827680090`｜第 34 句） | 「This restriction is automatically lifted after the enforcement period.」（`support_error_codes_md`｜第 56 句） | 「Pausing durations are as follows: - 1st Instance: Paused for 3 hours - 2nd Instance: Paused for 6 hours - 3rd…」（`templates_template_pausing_md`｜第 2 句） | 「`RESTRICTED_BIZ_INITIATED_MESSAGING` — Business cannot initiate conversations with customers.」（`webhooks_reference_account_update_md`｜第 52 句） | 「If content or actions violate our terms or policies, we may limit or remove your access to our services.」（`legal_business_policy`｜第 5 句） | 「Values can be: `stop` — Indicates the WhatsApp user has opted to stop receiving marketing messages from you.」（`webhooks_reference_user_preferences_md`｜第 9 句） |
| E7   | 判定是否违规靠什么信号 | 「WhatsApp uses both machine learning technology and reports from users to detect and ban accounts that send…」（`faq_361005896189245`｜第 20 句） | 「If you think someone is pretending to be you or someone else on WhatsApp in order to scam other WhatsApp users,…」（`faq_2090730848486858`｜第 6 句） | 官方未按这一列写该项 | 「If you believe a group has been compromised, you can report the group or report the admin or member involved.」（`faq_424124173736394`｜第 29 句） | 官方未按这一列写该项 | 「To help achieve this, your marketing messages are subject to review by Meta before you send them in chats on…」（`faq_1797219184095235`｜第 6 句） | 「The list below outlines specific policy violations a business could receive.」（`policy_enforcement_violations_md`｜第 3 句） | 「How We Apply the Messaging Guidelines WhatsApp may use both automated processing techniques and human review…」（`legal_messaging_guidelines`｜第 21 句） | 官方未按这一列写该项 |
| E8   | 官方写明的处理时限 | 「The status will disappear within 24 hours and you won’t see future status updates from that user.」（`faq_414631957536067`｜第 47 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「The appeal will be reviewed and a decision made within 24 hours.」（`templates_template_pausing_md`｜第 28 句） | 「The appeal review decision will be sent via Business Manager and typically takes 24 to 48 hours.」（`policy_enforcement_md`｜第 29 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E9   | 处置之后可以走的申诉入口 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Select one or more templates and choose **Request review**.」（`direct_send_integrity_and_content_guidelines_md`｜第 13 句） | 「With the introduction of Template Pacing, you can now unpause any paused template through: - The API by making…」（`templates_template_pausing_md`｜第 18 句） | 「This ensures businesses can quickly adjust behavior to avoid additional warnings and/or enforcement actions. ##…」（`policy_enforcement_md`｜第 19 句） | 「If you believe your account has not violated our policies or terms, please consult the following resources for…」（`legal_business_policy`｜第 102 句） | 官方未按这一列写该项 |
| E10  | 家长管理账号把权限切在哪 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「If a managed account receives a group invite, a parent will need to review and accept it using their parent PIN…」（`faq_1374700371009788`｜第 9 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「You must be at least 13 years old to register for and use our Services on your own (or such greater age…」（`legal_tos`｜第 44 句） | 官方未按这一列写该项 |
| E11  | 批量与自动化在各层的定性 | 「WhatsApp uses both machine learning technology and reports from users to detect and ban accounts that send…」（`faq_361005896189245`｜第 20 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Persistent misuse results in restrictions to Direct Send access. ### Notification of non-compliant content Meta…」（`direct_send_integrity_and_content_guidelines_md`｜第 8 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 「This includes efforts to undermine WhatsApp integrity systems, abuse reporting tools, compromise user accounts,…」（`legal_messaging_guidelines`｜第 16 句） | 官方未按这一列写该项 |
| E12  | 屏蔽与举报是否连带 | 「Report a contact, business, unknown user, or group Reporting a user without blocking them won’t stop you from…」（`faq_414631957536067`｜第 7 句） | 「Alternatively, tap Report and Block to also block the contact.」（`faq_771267714464323`｜第 5 句） | 「Tap > Report Check the box to also block the sender or exit the group and delete the chat.」（`faq_1561915401728939`｜第 10 句） | 「Report a message You can report a message in a group and block the sender.」（`faq_424124173736394`｜第 16 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E13  | 举报者与被举报方各知道多少 | 「We’ll acknowledge receipt of your report, but we will not contact you again.」（`faq_414631957536067`｜第 23 句） | 「The sticker creator won't be notified of your report.」（`faq_3405954479576110`｜第 6 句） | 官方未按这一列写该项 | 「The message will be sent to WhatsApp, and the sender or group won’t be notified.」（`faq_424124173736394`｜第 19 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E14  | 负反馈在业务侧被写成什么 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「If a significant number of users report or block your account due to unwanted messages, your business account…」（`faq_740266744985664`｜第 10 句） | 「Restricted or disabled accounts can still appeal or acknowledge issues.」（`policy_enforcement_md`｜第 16 句） | 「Enforcement and Updates We may limit or remove your access to or use of WhatsApp for Business, or limit your…」（`legal_business_policy`｜第 82 句） | 官方未按这一列写该项 |
| E15  | 重复发生时的计数口径 | 「Please keep in mind, frequent use of broadcast messages may lead people to report your messages, and we ban…」（`faq_361005896189245`｜第 25 句） | 「We reserve the right to remove usernames or ban accounts that do.」（`faq_2090730848486858`｜第 27 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Repeated restrictions may result in longer suspensions.」（`faq_740266744985664`｜第 16 句） | 官方未按这一列写该项 | 「We may take action with respect to your account, including disabling or suspending your account, if you…」（`legal_tos`｜第 85 句） | 官方未按这一列写该项 |
| **合计** | **15** 个维度 × 9 列 = **135** 格 | 有句 **68** 格 | 空格 **67** 格 | 去重引用页 **28** | 跨格复用句 **4** 次 | 截断 **34** 格 | 个人账号侧帮助中心 12 | 举报对象专用页 9 | 家长管理账号侧 5 | 群组与社区侧 9 |

#ROWS=15 COLS=11


# 附表 F 口径冲突与官方未写明处（22 行）

本表收录两类东西：官方多处写法不一致、需要读者知道本篇按哪一说走的**冲突**，以及官方在这批登记页面里根本没写、本篇不替它补的**空白**。全表 **21** 个议题：**19** 个两侧都取到了原句、**0** 个只有一侧有原句（另一侧官方没写）、**2** 个两侧都没有原句可引（共 **2** 行）。

两侧皆空的行要说清它空在哪：本轴登记 **63** 页，其中正文未取回 **2** 页——这 **2** 页全篇不引用，已引用 **61** 页（Meta 开发者文档 22／WhatsApp 帮助中心 33／法律与政策页 6），所以两侧皆空不是因为抓不到，而是因为**官方在可引用的这批页面里没按这一项写过一句话**；这一类议题一律登记为空白，正文不替官方补结论。

取句方式与附表 E 同一把刀：每个说法登记「页面标识＋一条正则」，脚本在原句池里取句，命中 0 句或多句都会让生成脚本中止；取到的每一句再逐字回表到 `_tmp/w6_a5s/` 的对应页面。本表引用页面去重 **17** 个，同一句在多个议题里复用的共 **0** 次。两侧点同一页、又取到同一句的行由脚本直接中止——同句不构成冲突，不允许拿复制冒充对照。差异性质用了 **21** 种。这 21 条性质各不相同（每种恰好 1 行），不在这里重述，逐条只写在表内「差异性质」列。

「本篇采用」一列写的不是判断谁对谁错，而是本篇正文按哪一说的走、以及为什么。这一列的 **18** 行标为已写进正文、**3** 行只在本附表登记（正文不替官方补结论，也不把两种说法合成一说）。这一列标「已写进正文」的 **18** 行本次已逐句核过成稿：它们要引的 **36** 句官方原句全部原样出现在成稿里（空白折叠后做子串比对），缺任何一句本脚本都会中止，所以这一列不是空头承诺。官方原句超过 130 字符按整词截断并以省略号结尾，共 **11** 处截断；截断只发生在显示层。

| 编号 | 议题 | 说法甲 | 说法乙 | 差异性质 | 本篇采用 | 正文落点 |
|---|---|---|---|---|---|---|
| F1   | 举报之后有没有回执 | 「We’ll acknowledge receipt of your report, but we will not contact you again.」（`faq_414631957536067`｜第 23 句） | 「To track the status of reports, tap > Settings > Help and feedback > Channel reports .」（`faq_3440358352849077`｜第 15 句） | 同一「举报」动作，回执与可跟踪性随被举报对象不同 | 本篇按对象分开写：个人号与用户名的举报只承诺「收到即止」，频道举报提供 Channel reports 入口，正文不把两者合并成「举报都有回执」。 | 已写进正文 |
| F2   | 向平台催办能不能加快 | 「Contacting WhatsApp outside of the in-app review process won’t expedite the review process or impact the decision.」（`faq_414631957536067`｜第 22 句） | 「Automation also helps us prioritize and expedite reviews by routing potentially violating accounts, groups, or communities that…」（`legal_messaging_guidelines`｜第 23 句） | 一处否定外部催办的效果，一处描述自动化如何加速分派，主语与所指的「加速」不是同一件事 | 本篇采用「外部联系不改变应用内审查」，同时写清官方所说的 expedite 指自动化把可疑对象分派给人审，不是对用户催办的承诺。 | 已写进正文 |
| F3   | 举报是不是一定导致处置 | 「Please be aware that a user report of conduct in violation of our Terms of Service won't necessarily result in us banning the…」（`faq_414631957536067`｜第 19 句） | 「After review, we may remove usernames or ban accounts that we believe are in violation of our or Business Terms of Service .」（`faq_2090730848486858`｜第 7 句） | 一处写「不必然」，一处写「可能」，必然性不同 | 本篇按「可能」写：举报只是把材料交给平台，是否处置由平台按条款判断，正文不使用「举报即封号」这类因果。 | 已写进正文 |
| F4   | 群组举报送出的条数 | 「When you report a group, WhatsApp receives up to five of the last messages sent to you in the reported group.」（`faq_414631957536067`｜第 9 句） | 「When you report a group, the last five messages in the chat will be sent to WhatsApp.」（`faq_1142481766359885`｜第 99 句） | 一处写成上限（up to five），一处写成定数（the last five） | 本篇并列两种写法而不取其一：引用「最多五条」时点名页面，遇到另一页的「最后五条」也照录，并说明差异在于是否写成上限。 | 已写进正文 |
| F5   | 被举报的是转发者还是原作者 | 「If someone you sent message history to reports content that was sent as part of the message history you sent, you’ll be reported.」（`faq_414631957536067`｜第 14 句） | 「The original sender of the message won't be reported.」（`faq_414631957536067`｜第 15 句） | 同一动作下两个不同主体：转发历史的人被举报，原始发送者不在被举报之列 | 本篇明确区分「谁被举报」与「内容最初由谁发出」：转发历史后被举报的是转发者，原文作者不在被举报之列，正文不写「举报会追溯到源头」。 | 已写进正文 |
| F6   | 举报与屏蔽是不是一件事 | 「Users simply need to long press a message to either report or block a user.」（`faq_650453616475879`｜第 19 句） | 「Uncheck the box next to Report to WhatsApp if you don’t want to report the person you’re blocking.」（`faq_414631957536067`｜第 35 句） | 一处写成二选一，一处的屏蔽流程默认连带举报且可取消 | 本篇按界面口径写：屏蔽流程里举报是默认可取消的勾选项，而长按消息的入口被写成 report or block 的二选一，两种写法都照录。 | 已写进正文 |
| F7   | 屏蔽方向在两侧各是什么 | 「Attempts to message a blocked user return an error.」（`block_users_md`｜第 2 句） | 「Blocking a business removes your number from their contact list.」（`faq_27563559673256587`｜第 10 句） | 同一「屏蔽」在企业侧与用户侧的动作和后果不同 | 本篇按方向分开写：用户屏蔽企业是把自己的号码移出对方联系人名单，企业屏蔽用户是此后向该用户发消息返回错误，二者不可互换。 | 已写进正文 |
| F8   | 退订与屏蔽的覆盖面 | 「If you attempt to send a marketing template to a WhatsApp user who has stopped marketing template messages from your business,…」（`templates_marketing_templates_md`｜第 11 句） | 「Block a contact, business, or unknown user When you block someone, you’ll no longer receive their messages, calls, and status…」（`faq_414631957536067`｜第 24 句） | 退订只覆盖营销消息，屏蔽切断消息、通话与状态更新 | 本篇把 stop 限定在营销消息：已停止接收的用户仍会走请求处理，但消息不发出；屏蔽则写成对消息、通话与状态更新的全面切断，正文不把退订说成屏蔽的同义词。 | 已写进正文 |
| F9   | 屏蔽在共同群组里管不管用 | 「Blocked contacts or phone numbers will no longer be able to call you or send you messages.」（`faq_1142481766359885`｜第 51 句） | 「Blocking a contact won’t stop that user from seeing your messages in a group you both participate in, and you’ll see their…」（`faq_1142481766359885`｜第 59 句） | 一对一的收发被切断，共同群组里的互相可见不受影响 | 本篇写明屏蔽的边界：一对一的收发被切断，但共同群组里双方仍能看到彼此发言，正文不承诺「屏蔽后彻底看不见」。 | 已写进正文 |
| F10  | 整个群能不能被屏蔽 | 「Block a group It’s not possible to block a group, however you can block and report group admins and members.」（`faq_424124173736394`｜第 30 句） | 「Block a group admin or member You can stop receiving messages from group admins or members by blocking them.」（`faq_424124173736394`｜第 40 句） | 对象级屏蔽不存在，成员级屏蔽存在 | 本篇采用「群本身不可屏蔽、成员可逐个屏蔽」的口径，正文把退出群组与屏蔽成员写成两个动作，不写成「屏蔽群」。 | 已写进正文 |
| F11  | 被屏蔽方能不能看出来 | 「We can’t tell you if someone has blocked you, or who has blocked you.」（`faq_666362298345682`｜第 13 句） | 「If you see all the indicators above for a contact, this could mean they’re blocking you.」（`faq_666362298345682`｜第 11 句） | 官方声明不告知，同一页又列出可观察指标 | 本篇同时写官方声明与界面指标：官方说不会告知谁屏蔽了你，但列出灰勾与通话不通等迹象，并强调迹象不构成结论。 | 已写进正文 |
| F12  | 屏蔽前后状态可见性的时间边界 | 「Users can’t see your status after you’ve blocked them.」（`faq_414631957536067`｜第 44 句） | 「But, they can see your status if you post it before you block them.」（`faq_414631957536067`｜第 45 句） | 屏蔽后的新状态不可见，屏蔽前已发布的状态仍可见并随时间消失 | 本篇按时间切开：屏蔽前已发布的状态仍可见并在 24 小时内消失，屏蔽后的新状态对方看不到，正文不写成「屏蔽即全部隐身」。 | 已写进正文 |
| F13  | 举报之后谁会被通知 | 「The message will be sent to WhatsApp, and the sender or group won’t be notified.」（`faq_424124173736394`｜第 19 句） | 「Parents will be notified when this action occurs through an activity alert.」（`faq_1561915401728939`｜第 4 句） | 被举报方不获通知，举报方的监护人反而收到活动提醒 | 本篇按对象写通知：被举报的发送者或群不会收到通知，而未成年人举报时其监护人会在举报方一侧收到活动提醒。 | 已写进正文 |
| F14  | 家长能管到哪一层 | 「Personal messages are end-to-end encrypted, which means no one, not even the parent or WhatsApp, can read them.」（`faq_1374700371009788`｜第 27 句） | 「With a parent-managed account, parents or guardians can choose who can contact their child, decide which groups they can join,…」（`faq_1374700371009788`｜第 2 句） | 监护范围在联系人与入群请求层，不在消息内容层 | 本篇把家长权限限定在联系人与入群请求层，并照录端到端加密使家长也读不到消息这一点，两层并列而不互相抵消。 | 已写进正文 |
| F15  | 模板暂停与账号暂停的层级 | 「However, if you continue to send spam from your business account, marketing messages will be suspended on the business account…」（`faq_740266744985664`｜第 11 句） | 「Pausing durations are as follows: - 1st Instance: Paused for 3 hours - 2nd Instance: Paused for 6 hours - 3rd Instance: Disabled…」（`templates_template_pausing_md`｜第 2 句） | 一处以天计作用在账号的营销权限，一处以小时计作用在单个模板 | 本篇按层级写：模板级暂停以小时计并可解除，账号级营销暂停以天计且自动恢复，正文不把两者混成同一套时限。 | 已写进正文 |
| F16  | 申诉的审查时限 | 「The appeal review decision will be sent via Business Manager and typically takes 24 to 48 hours.」（`policy_enforcement_md`｜第 29 句） | 「The appeal will be reviewed and a decision made within 24 hours.」（`templates_template_pausing_md`｜第 28 句） | 账号违规申诉写 24 至 48 小时，模板申诉写 24 小时 | 本篇分别照录两个时限，并点明它们分属账号违规申诉与模板申诉，不合并成一个数字。 | 已写进正文 |
| F17  | 是不是所有违规都能申诉 | 「Restricted or disabled accounts can still appeal or acknowledge issues.」（`policy_enforcement_md`｜第 16 句） | 「This review might result in WhatsApp reversing the violation. **Warning:** **Note:** Not all spam violations might be appealed.」（`policy_enforcement_md`｜第 21 句） | 一般规则写都能申诉，同一页又写了例外 | 本篇写「可以申诉」时同时保留例外：官方在一般规则之外明确并非所有垃圾信息违规都可申诉，正文不用绝对句。 | 已写进正文 |
| F18  | 透明度数据覆盖不覆盖 WhatsApp | 「H1 2026 Report We publish the Community Standards Enforcement Report semi-annually, with data broken down quarterly, to more…」（`legal_transparency_report`｜第 4 句） | 「The reports detail user complaints received and the corresponding action taken by WhatsApp, as well as WhatsApp’s own preventive…」（`faq_650453616475879`｜第 40 句） | 全球半年度报告的数据面向 Facebook 与 Instagram，WhatsApp 的举报数据另见印度月度报告 | 本篇不把全球半年度报告当作 WhatsApp 举报数据来源：该报告的数据面向 Facebook 与 Instagram，WhatsApp 的月度举报数据另见印度页。 | 已写进正文 |
| F19  | 举报渠道是不是只有应用内 | 「The only way to report someone is to follow the in-app reporting process.」（`faq_414631957536067`｜第 20 句） | 「Grievance Officer : We have a Grievance Officer based in India, who can be contacted if a user has a concern about their…」（`faq_650453616475879`｜第 36 句） | 一处写应用内是唯一途径，印度页另设无法通过其他渠道反映时的兜底联系人 | 本篇按地域分层：应用内举报是唯一途径，印度另有投诉专员作为无法通过其他渠道反映时的兜底，正文不把兜底写成第二条举报入口。 | 只在附表登记 |
| F20  | 个人号举报的处理时限 | 官方未写明（—） | 官方未写明（—） | 官方未写明：登记页面给了账号申诉与模板申诉的时限，没给个人号举报的处理时限 | 本篇不替官方补时限：凡未写明的处理时长一律不给数字，正文只写「不承诺时限」这一事实。 | 只在附表登记 |
| F21  | 被举报的个人能否申诉 | 官方未写明（—） | 官方未写明（—） | 官方未写明：申诉条款写在企业账号一侧，没有写给被举报的个人用户 | 本篇只说企业侧有申诉与复核入口，对个人被举报后的申诉不作任何推断，也不套用企业侧时限。 | 只在附表登记 |
| **合计** | **21** 个议题 | 两侧有句 **19**／单侧 **0**／两侧皆无 **2** | 引用页 **17** | 性质 **21** 种 | 正文落点：已写 **18**／只在附表 **3** | 截断 **11** 处 |

#ROWS=21 COLS=7


---

**边界声明：** 本篇是**官方规则文本、官方帮助中心界面说明与官方接口文档的口径梳理**，不是操作手册，也不是对平台规则合理性的评价。全篇内容限于 WhatsApp 与 Meta 公开页面已经写出的表述，共 **63** 个页面登记位：Meta 开发者文档 **22** 页（屏蔽用户接口与号码级屏蔽参考页、消息错误与账号状态一组 webhook、用户偏好与退订信号、营销模板与按人计的营销上限、模板质量与暂停、账号告警与账号更新、Direct Send 完整性、通话设置、商品目录总览）、WhatsApp 帮助中心 **35** 页（屏蔽与举报、退出群组与社区、可疑消息与可疑链接、屏蔽高量未知消息、群与社区的安全页、家长管理账号、商业 App 侧的广播与营销消息、手机号可见性与用户名、信誉与隐私入口、印度用户安全页）、法律与政策页 **6** 页（社区标准执行报告、商业消息政策、消息指南、用户条款、Business 条款、WhatsApp Business 服务条款）。引用这些文本的目的只有两个：说明平台把**谁对谁执行屏蔽、举报要提交什么、平台按什么动词与顺序处置、哪些对象各自有独立入口、申诉开在哪一侧**写成了哪些字段名、哪些取值域、哪些后果句与哪些否定式前提；说明同一批文本把**模板级与账号级暂停的时长、申诉与复核窗口、限额档位与计数口径**写成了哪些数字与哪些限定词。本篇**不含**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法建议；**不含**任何可用于向非自愿收件人大量投递消息的方法——"怎样让举报不成立""怎样让屏蔽不触发""怎样在被限制后继续发"这三类问法在本篇一律不答；**不含**代码、恶意样本或利用链构造方式；**不含**任何针对他人设备或他人账号的操作步骤与时序。界面页与接口页里官方写成"步骤""清单""要求项"的内容——第 2.1 节屏蔽入口与举报勾选项、第 3 章各举报对象页的菜单层级、第 12.4 节与第 11.2 节的申诉提交顺序——本篇只登记控件的名字与条目数量，**不重述每一步在哪个标签页里怎么点，也不把清单转写成可执行的操作顺序**。

官方写成区间、多档或带限定词的数字（`up to`、`approximately`、`at least`、`in some countries` 一类）按整段一行登记在附表 B，不合成单值、不取中值、不去掉官方写在数字前面的限定词；同一数字在不同指代上各写一处的按指代分列，不做跨指代合并；同一句官方原句在两个页面各出现一次的，B 表也各登记一行，因为它的去重键是「页面＋数字＋单位」。**本表有两类数字进不了行，本篇如实登记而不绕过**。**其一**，数字停在句末或停在名词前、脚本在数字之后四词窗口里定不出单位词的句子。本轮这样的实例共 **33** 个，连同它们所在的整句写进 `_tmp/w6_a5_bdrop.tsv` 供逐行复核，不进表；这一批绝大多数是接口示例对象 ID、地址门牌、法条编号与步骤序号，不是官方对某项业务写下的数量，**"这个数是不是一个上限"的判定权在读者手里，不在这张表里**。**其二**，官方把数量写成英文词而不是阿拉伯数字的句子，正文保留词形陈述，B 表不给它们造行。同页同数字同单位只留一行，本轮合并掉 **13** 行。B 表最终是 **47** 行、分布在 **19** 个页面、五档 **7／3／24／4／9**、单位词 **21** 种、性质 **11** 种、整句截断 **33** 行；截断只发生在显示层，进表的每一句都先用未截断整句回原页核对，失败 **0** 句。它的输入是原句池的 **1,194** 条整句（覆盖 **58** 页），其中 **1,100** 句整句不含任何阿拉伯数字、本来就不在 B 表的取材范围里。另有 **3** 个可引用页面在池子里一句都没有（第 1.3 节逐页点名），所以这几页在本表里是空的——**"这一页没有 B 表行"不等于"这一页没有数字"**，那种页面上的数字写在参数表里，只能连同表格 markup 一起取，而带竖线的混合句在建池时已剔除。

正文引句全部为整句、逐字回核到抓取件。整句的判据是抓取件的句池切分口径：句池按句子终止符切、不按版面标记切，因此官方 markdown 页里紧跟句子的小标题会被并入同一句；同时，长度不在 40–400 字符窗口内、段内含竖线或内联链接、以 `#` 或 `-` 起手的片段一律不入池。本轮正文整句引用去重后 **421** 句（引用标记 **424** 处，另有 **3** 处是同一句在不同章节重复引用），分布在 **55** 个页面上，其中 **5** 句（分布在 **4** 个页面）尾部带了一段 `## 小标题` 与紧随其后的另一句，**4** 句（分布在 **4** 个页面）自带官方粗体排版标记 `**`。这两类都不修补：本篇按句池原样照引、不删字符、不补标记，中文说明只覆盖小标题之前的句子本体，读者按附表登记的「页面＋句序」可回抓取件逐字复核。粘连发生在官方页面的排版层而不是本篇的转写层。另有一条硬规矩：含反引号字段名与枚举名的句子不整句引用，这些页面上的事实改用中文陈述并标注出处页，取值本身逐字登记在附表 D；**本轮正文实际引用的 421 句里，含反引号的句子是 0 句**，这条规矩没有被绕过。本轮引用的句子里有 **2** 句带英文双引号（分布在 **2** 个页面），其中 **1** 句的引号在整句边界上只出现一次、即官方原文的引号不闭合；这一类按原样保留，不替官方补引号。

各层之间官方从未给出对齐表：本篇在附表 E 按同一组维度并排 **9** 列——个人账号侧帮助中心 **6** 页／举报对象专用页 **7** 页／家长管理账号侧 **2** 页／群组与社区侧 **3** 页／业务接口侧 **3** 页／营销与模板治理侧 **8** 页／执法与申诉文档 **4** 页／法律与政策页 **4** 页／用户偏好与退出信号 **3** 页——**15** 个维度 × 9 列 = **135** 格里 **68** 格取到官方原句、**67** 格标作「官方未按这一列写该项」，该标注**只说明官方文本的写法，不说明该类主体没有该项规则**；在附表 C 把「官方是否写明」限成「词（组）× 站点类别」矩阵，**82** 个词（组）分属 **13** 个主题组 × 4 类站点共 **176** 个非空格、句次 **2119**、去重命中句 **925**（占全池 77.5%）、一句最多同时落进 10 格、**578** 句落进两格以上，**8** 个词按零命中登记而不改词表（这 8 个是「automatic review（自动审核）」「report it to us（要用户上报的说法）」「block API（接口名）」「block rate／report rate（屏蔽率与举报率）」「threshold（阈值）」「tier（档位）」「harassment（名词形态）」「reporter identity（举报人身份是否告知被举报方）」）；在附表 D 只登记官方在页面里逐字写出的取值域（**18** 组、**92** 个取值、去重取值名 **74** 个、覆盖 **5** 页），脚本配置的 **10** 串取值域标记本轮扫到 **3** 串、另 **7** 串（Supported values are／Can be one of／must be one of／The following values are supported／Valid values are／Options are／Enum Values）在扫过的每一行原文里零命中，因此本篇不把它们当作任何官方句子的引文；还有 **4** 处官方写了标记却取不出取值域、分布在 **4** 个页面，逐处连同原文行号写进 `_tmp/w6_a5_dmiss.tsv`；**D 表的扫描范围限于站点类别为 Meta 开发者文档的登记页**，政策页与帮助中心页的取值域不在 D 表范围内，本篇不声称它们在 D 表有行；在附表 F 把 **21** 个议题逐条对撞（两侧有句 **19**／只有一侧 **0**／两侧都无原句 **2**，引用页去重 **17**、差异性质 **21** 种各不相同），凡官方留白处一律登记为留白，不推断、不补齐。

页面层的四项事实单独交代，免得读者把"登记"读成"引用"：**第一**，本篇登记 **63** 页中 **2** 页正文未取回，逐页点名在第 1.3 节，两页都在帮助中心侧；这 2 页全篇不引用，凡可能落在它们身上的规则一律按"官方未在本篇语料里写明"处理，而不是按"官方没写"处理——这两句话在本篇里不是同一个意思。这条事实在附表 A 的引用位置列与取回状态列各写明一次，附表 F 的说明段也写明它们不参与"两侧皆空"的归因。**第二**，**3** 页正文取回但池内零句：第 1.3 节点名的两页接口参考页与一页问答短文，它们的内容要么是参数表、要么是错误码表；这三页在附表 B 至 F 里都不会出现，只在附表 A 各留一行登记，附表 D 收录的是另外五页的参数取值，与这三页无关。**第三**，《WhatsApp Business Terms of Service》整页入池只有 **1** 句，而那一句是站点横幅，与另三页政策页池内的同一条横幅重复；本篇在 13.3 只登记"这一页被多处引用为上位条款"与"它的正文没有进入本篇句子层"两件事，绝不代它拟写条款。**第四**，**63** 页全部在正文被官方标题点名（标题标记共 **71** 处），附表 A 的引用位置列 **61** 页写有小节号或章首、**2** 页写明全篇不引用，没有一格留空；反向的一条同样成立：正文整句引用落在 **55** 个页面上，其余页面被点名而未引句、或只在附表里出现，逐页状态由脚本实测而非人工填写。

本篇对"举报是否必然带来处置"的判定标准、各档暂停之间的换算、处置权重的分配、优先级、文档之间的效力顺序与列举的完备性一律不补；「个人号举报的处理时限」与「被举报的个人能否申诉」这两处官方空白在附表 F 各占一行，第三处「印度投诉专员是否构成第二条举报入口」既没有配对的两句官方说法、也不与另两行同类，因而只在第 14.3 节与附表 F 的 F19 登记——这三处的共同处理都是登记为空白，不替官方补结论，理由与逐处证据写在 1.4、12.4 与 14.3。

最后两处范围断言必须交代扫描口径，因为它们都是"穷举后没有"而不是"抽样没看到"：**其一**，本篇在 1.1 与第 17 章写"官方没有一页把屏蔽、举报与退出画成同一条流程"。这一句的依据是：附表 C 的主题组 A、B、D、F 四组共 **27** 个词（组）——A　三个动作的写法 6／B　举报的对象与入口 8／D　平台侧的处置动词 7／F　收件人侧的自助动作 6——在原句池 **1,194** 句里逐句扫过，三类句子分别落在不同的页面与不同的字段上，本篇按三个动作归纳并逐类给出页码；但这只覆盖本篇登记的 **63** 页，帮助中心与政策站里还有大量本篇未登记的页面，所以"没有一页画成一条流程"是对这批语料的陈述，不是对 WhatsApp 全部公开文本的陈述。**其二**，附表 F 有 **3** 个议题标成"只在附表登记、正文未并列两句"，逐条点名是：F19 举报渠道是不是只有应用内、F20 个人号举报的处理时限、F21 被举报的个人能否申诉。这 3 行里 F20 与 F21 的两侧都是空白、只有 F19 两侧各有一句；它们都能在附表 F 逐字回表，但本篇不在正文把它们并排成一句结论；读者若要把它们当通则使用，需要自己回到那两页看官方是否写过连接句——**本篇实测的结果是这批连接句在本篇抓取的语料里不存在，因此本篇不替官方写。**

Telegram: https://t.me/one00190
