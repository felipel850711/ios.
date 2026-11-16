@@TITLE@@WhatsApp 商业消息的计费单位、类别与账单结构的官方口径解读
@@SUBTITLE@@本篇是"WhatsApp 群发／群控系统性口径"系列第三轮的第一篇（轴①，编号 n=11），讲的是：一条商业消息在官方文本里被算成几笔、计费从"发出"还是"送达"起算、五类消息与费率卡把价格切成什么坐标、按会话计费的旧口径为什么还在线上、服务消息的每月免费额度挂在哪个对象上、免费入口点与客服窗口各自免费到什么程度、客服窗口内的 utility 消息从哪一天起恢复计费、Meta Business Agent 为什么按 token 而不按条计价、量级阶梯对哪些类别生效、币种与开票实体怎么决定、验证消息的国际费率与营销消息的最高价各约束什么、群组与通话为什么会让"一条消息"在账单上变成多笔、以及支付方式、信用额度、订阅与退款把责任切在谁身上。全篇只读官方文本，共 **33** 个页面登记位（**33** 页正文全部取回，逐页归属与引用位置见附表 A 的 **34** 行含合计）：Meta 开发者文档 **15** 页、WhatsApp 帮助中心 **12** 页、Meta 商业帮助中心 **4** 页、法律与政策页 **2** 页。
@@INFO@@报告日期|2026 年 11 月 16 日
@@INFO@@分析对象|按站点清点本篇登记的页面构成：Meta 开发者文档 **15** 页（计费总纲、服务消息与 utility 消息的过渡与恢复计费、会话计费历史页、AI Provider 计价政策、验证消息国际费率、币种迁移、营销消息最高价与报名、模板类别路由、群组计价、通话计价、多方案会话、共享与收回信用额度、支付配置与模板类别两个 webhook）、WhatsApp 帮助中心 **12** 页（WhatsApp Business Account 的账单、加卡、商业广播与营销消息的付费、商业 App 的广告与广播付款、Meta Verified 与 Meta One 的订阅、退款、付款失败与管理员权限、两步验证与支付）、Meta 商业帮助中心 **4** 页（账户账单总览、加信用卡、信用额度申请、Meta Verified 商业订阅的账单与支付）、法律与政策页 **2** 页（WhatsApp Business Platform 条款、WhatsApp Business App 条款）。合计 **33** 个登记位，正文全部取回；归组依据是官方页面标题，逐页归属见附表 A。
@@INFO@@视角定位|官方规则文本、帮助中心账单说明与开发者文档的**口径解读**，不是操作手册，也不是报价单。本篇**不提供**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法建议；本篇同样**不写**任何可用于向非自愿收件人大量投递消息的方法——"怎样把营销消息伪装成服务消息""怎样把一条群发拆成多条免费额度""怎样在额度用尽后继续投递"这三类问法在本篇一律不答；官方把计费写成单位、类别、市场与时点，把免费写成额度与窗口，本篇只登记这些句子出现在哪一页、哪一句（界线见 1.4）。
@@INFO@@分类|**官方页面登记 33 个登记位 × 4 类站点 × 引用位置 × 取回状态**（附表 A，**34** 行含合计；Meta 开发者文档 **15**、WhatsApp 帮助中心 **12**、Meta 商业帮助中心 **4**、法律与政策页 **2**；可引用 **33** 页、不可引用 **0** 页；原句池 **768** 条整句、覆盖 **32** 页）× **数字归档 128 行，五档 44／1／17／28／38，去重 95 组数字＋单位、33 种计数单位、11 种性质，覆盖 19 页，整句截断 95 行，池内不含数字的整句 612 句、按单位词表定不出档位而被丢弃的数字实例 36 个、在池子里一句都没有的可引用页面 1 个**（附表 B，**129** 行含合计）× **官方用词 × 站点类别矩阵 79 个词（组）分属 13 个主题组 × 4 类站点、非空格 144 个、句次 1110、去重命中句 482（占全池 62.8%）、一句最多同时落进 9 格、落进两格以上的句子 273 句、零命中词 3 个、逐字回表失败 0 句**（附表 C，**145** 行含合计）× **官方写明的取值域 6 组、取值 20 个、去重取值名 20 个、覆盖 3 页，配置的 10 串标记本轮扫到 5 串、5 串零命中，另有 3 处写了标记却取不出取值域、分布在 3 页**（附表 D，**7** 行含合计）× **计费对象与计费边界在各主体上的逐列对照 16 个维度 × 8 列 = 128 格、有句 51 格／空格 77 格、去重引用页 22、同一句跨列复用 2 次、截断 38 格**（附表 E，**17** 行含合计）× **口径冲突与官方未写明 21 个议题、两侧有句 17／单侧 3／两侧皆无 1、差异性质 21 种、去重引用页 18、复用 0 次、正文落点已核 10 行 16 句、截断 19 处**（附表 F，**22** 行含合计）× **正文与附表的官方原句逐字回核，失败 0 条**。
@@INFO@@难度|进阶（本篇有五处容易读错。**第一是"发出"与"送达"不是同一个时点**：平台侧按送达计费，商业 App 侧按发出后五天内送达并累计，两个时点不能互换（2.1、15.1，附表 F 的 F10）。**第二是会话计费页还在线上，但它已把自己标注为历史口径**：页面标题就是 Deprecated，主页面已改写为按消息计费；把历史页当现价会得出"每条模板都含一个会话费"的旧结论（3.1 与 2.2，F1）。**第三是"窗口内不额外收费"与"不计入量级阶梯"不是同一句话**：前者是免费额度内的状态，后者是把某一类消息从阶梯体系里摘出去；两句都存在，但回答的不是同一个问题（4.4、6.2，F2 与 F5）。**第四是"一条消息"在账单上未必是一笔**：群组按每个送达收件人计，多方案会话按实际发送所用的消息账户计，而通话跨档时整通按较低档计价、消息却按当条所在档计——三种口径方向不同（12.1、12.2、12.3）。**第五是"免费额度"与"免费窗口"是两种对象**：前者挂在每个电话号码上、按月重置、不结转；后者是时间窗，窗内怎么收费另有一套写法；把两者叠成"只要在窗口里就不用管额度"会漏掉额度用尽后投递停止这一句（4.2、4.3、7.4）。）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

本篇是"WhatsApp 群发／群控系统性口径"系列第三轮的第二篇（编号 n=11），主题是**商业消息的计费单位、类别与账单结构**：一条从商户发往用户的消息，在官方文本里被算成几笔、按哪个维度定价、什么时候开始算、什么时候不算、免费的部分挂在哪一层、费率卡与量级阶梯各以什么周期重置、账单由哪个实体开给谁、支付方式与信用额度把责任切在谁身上、以及"营销消息""广播""订阅"这些相邻的付费能力为什么不能与平台消息计费混读。全篇只读官方文本，共 **33** 个页面登记位：Meta 开发者文档 **15** 页、WhatsApp 帮助中心 **12** 页、Meta 商业帮助中心 **4** 页、法律与政策页 **2** 页；**33** 页正文全部取回，其中 **32** 页进入原句池（另有一页整页入池零句，第 1.3 节点名）。

前五篇问的是"谁能发、发给谁、名字归谁、对方愿不愿意、到了之后怎么办"；本篇问的是"一条消息被算成几笔、由谁在哪一天按什么单位收钱、哪些写法只是历史口径"。这条线在官方文本里被拆在四类页面上：计费总纲页写单位与类别，专项计费页写某一类消息的过渡与例外，帮助中心的账单与订阅页写扣款、额度与退款，条款页写责任。四类页面之间没有对照表，只有互相指向的链接；本篇把它们并排登记在附表 E 的 **16** 个对照维度 × **8** 列 = **128** 格（**51** 格取到官方原句、**77** 格标作"官方未按这一列写该项"），并把成对出现的口径差异与官方留白登记在附表 F 的 **21** 个议题里。

本篇的读法是**沿一条账单往回走**：先看一条消息为什么会被算作一笔（第 2 章），再看按会话计费的旧口径为什么还在页面上（第 3 章），然后是免费的部分——服务消息的免费额度（第 4 章）、按 token 计费的 Meta Business Agent（第 5 章）、免费入口点（第 6 章）与客服窗口（第 7 章）；接下来是价格本身：市场、费率卡与量级阶梯（第 8 章）、币种与开票实体（第 9 章）、验证消息的国际费率（第 10 章）、营销消息的最高价（第 11 章）、群组与通话（第 12 章）；最后是钱怎么付、账单怎么对、以及与平台计费相邻的付费能力怎样分界（第 13—15 章）。

下面 **50** 个问题覆盖第 1—15 章的各级小节与第 16 章的结论，一个问题可以跨相邻两小节，一节也可以由两个问题分别追问；跨小节的关联只做中文指认，不把两页原句拼成一句。

1. 计费总纲、专项计费页、帮助中心账单页与条款页，各自把"计费"写成了什么单位？（1.1）
2. 本篇固定了哪些中文口径词，每个词对应哪个英文原词？（1.2）
3. 一页整页入池零句，它在附表里落在哪一行，正文为什么不引它？（1.3）
4. 本篇不写什么，官方写成"步骤"的计费设置登记到什么颗粒度？（1.4）
5. 付费方向为什么只有"商户发给用户"这一侧，用户发给商户为什么不计费？（2.1）
6. 计费的触发点是"发出"还是"送达"，官方在哪一句里写死了这个条件？（2.1）
7. 五类消息与一条消息只落一笔，这两件事在官方文本里怎么并排写？（2.2）
8. 费率由哪两个坐标定，为什么"按条计价"不等于"全世界一个价"？（2.3）
9. opt-in 与计费是不是同一条规则，为什么不能把"没同意"读成"不计费"？（2.4）
10. 按会话计费的旧口径为什么还在，页面自己怎么标注它的历史身份？（3.1）
11. 会话是怎么开启和计费的，谁决定一条模板落进哪个会话类别？（3.2）
12. 免费入口点会话持续多久，它为什么能把其它会话全部关掉？（3.3）
13. 服务消息与非模板消息是什么关系，官方在哪一句里把二者等同？（4.1）
14. 每月一千条免费额度挂在哪个对象上，未用完的部分能不能结转？（4.2）
15. 没有支付方式时，官方会投递到哪一步为止？（4.3）
16. 服务消息与客服窗口内的 utility 消息各从哪一天起恢复计费？（4.4）
17. Meta 对政府与非营利组织的计价政策写了什么，资格从哪个字段看出来？（4.5）
18. Meta Business Agent 的计费为什么不是"第二条消息费"？（5.1）
19. token 计费只算哪两类 token，缓存 token 为什么不计？（5.2）
20. 一条消息的 token 消耗为什么只能读成估算，官方用什么措辞限定它？（5.3）
21. Meta Business Agent 的支付方式分几步开放，白名单测试为什么可以例外？（5.4）
22. 免费入口点窗口由什么触发，它和客服窗口是不是同一扇门？（6.1）
23. 窗口期内哪些消息不计费，官方为什么另写"不计入阶梯"？（6.2）
24. 窗口打开时，原有的会话会怎样，新会话什么时候才能再开？（6.3）
25. 窗口期内的非模板消息为什么要另有一个客服窗口？（6.4）
26. 客服窗口的二十四小时从哪一刻起算，用户再来一条消息会怎样？（7.1）
27. 窗口没开时商户还能发什么，这条限制和模板类别谁先谁后？（7.2）
28. opt-in 在这两扇窗里各出现一次，它约束的是"能不能发"还是"算不算钱"？（7.3）
29. 免费额度与窗口期内不计费是不是同一件事，两者叠在一起时按哪个走？（7.4）
30. 费率卡覆盖多少市场、怎么把国家码映射到市场？（8.1）
31. 十月的费率卡更新动了什么，"移出区域费率"与"新增国际费率"是不是一件事？（8.2）
32. 量级阶梯按什么周期重置、对哪些消息类别生效？（8.3）
33. 验证消息的国际费率凭什么高于普通费率，生效前留了多长的缓冲？（8.4）
34. 币种在账户创建时怎么选，选完之后还能不能改？（9.1）
35. 迁移币种时哪些东西跟着走、哪些留在原地？（9.2）
36. 克隆迁移的完成条件是什么，为什么卡在支付方式上？（9.3）
37. BRL 迁移的截止与后果各写在哪一句，为什么这不是"改一个设置"？（9.4）
38. 什么叫"基于印度"的商户会被按国际费率计费？（10.1）
39. 主要营业地由谁判定，判断不出来时官方怎么处理？（10.2）
40. 国际费率的生效时间通过什么信号告知，商户要在接口里读什么？（10.3）
41. 营销最高价的"最高"约束的是哪一层，它与预算上限是什么关系？（11.1）
42. 最高价的开放分几个阶段，报名为什么要签一份协议？（11.2）
43. 模板类别变了以后，发送请求应该走哪个端点，走错会返回什么？（11.3）
44. 群发到群组时，"一条消息"为什么会被拆成多笔，免费额度又怎么共享？（12.1）
45. 通话计费与消息计费在"跨档"上的规则为什么相反？（12.2）
46. 多方案会话里，同一个号码为什么可能对应多个计费账户？（12.3）
47. 支付方式、信用额度与账单责任人三层各由谁承担，退款又落在哪一层？（13.1、13.2）
48. 账单与额度的读数来自哪些信号，支付配置的状态变化与扣款事件是不是一回事？（14.1、14.2）
49. 营销消息的送达观察窗、商业 App 的计费与平台侧差在哪里？（15.1、15.2）
50. 订阅与 Meta Verified 的续费、退款各按什么口径写，为什么具体金额不在本篇补出？（15.3、15.4）

# 1 来源层、口径词与范围

第 1 章交代本篇的取数边界。它不是计费规则的一部分，但决定了后面每一句能读到什么：哪些页面进了语料、哪些词被固定成中文口径、哪一页整页没有可引的句子、以及本篇明确不写的东西。

## 1.1 四层写作者与它们各自写的"计费"

同一件"收钱"的事，在这批页面里由四层文本分别写。**第一层是计费总纲**：《Pricing on the WhatsApp Business Platform》把单位、类别、市场、免费额度、阶梯与费率卡放在同一页，它是本篇唯一一页试图覆盖全部类别的文本。**第二层是专项计费页**：服务消息与 Meta Business Agent 的过渡（4.x）、会话计费的旧口径（3.x）、验证消息的国际费率（10.x）、币种迁移（9.3）、通话与群组（12.x）各有专页，专页只回答自己那一类消息的问题，不重述总纲。**第三层是帮助中心的账单与订阅页**：它们不定义单位，只写"钱从哪里扣、什么时候扣、扣错了怎么退"，本篇把它们登记在 13.x 与 15.x。**第四层是条款页**：它不写任何费率，只写责任由谁承担（15.6）。

这四层的写法差异不是文风问题。总纲页用"per message""per category–market"这样的计量短语，专项页用生效日期与"has not been charged since"这样的历史句，帮助中心用"you'll be charged""funds will be deducted"这样的第二人称后果句，条款页则只留下"you are responsible for"这样的责任句。本篇在 1.2 把这些词固定下来，正是为了避免把四层句子拼成一条官方从未写过的规则。

## 1.2 本篇固定的口径词

本篇固定使用十二个中文口径词，每个都对应官方文本里的一个英文原词，不混用：

**计费单位**对应 `per-message`、`per-message basis` 与 `per conversation`，分别指"按条""按条计价的方式"与旧口径的"按会话"；**计费类别**对应 `category`，指 message category 这一层；**送达**对应 `delivered`，与 `sent` 严格区分；**免费额度**对应 `free tier`；**量级阶梯**对应 `volume tier`；**费率卡**对应 `rate card`；**市场**对应 `market`，指费率卡上的一个国家或地区；**免费入口点**对应 `free entry point`；**客服窗口**对应 `customer service window`；**信用额度**对应 `credit line`；**最高价**对应 `max price`；**账单实体**对应 `billing entity`。这张表不是翻译偏好，而是因为官方在这十二个词上各写了一套后果，混用会把"送达"读成"发出"、把"免费额度"读成"免费入口点"（附表 C 的主题组 A、B、C、D 分别收录这四组词的命中格）。

附表 C 的 **79** 个词（组）分属 **13** 个主题组，覆盖四类站点（Meta 开发者文档 **654** 句次、WhatsApp 帮助中心 **307** 句次、Meta 商业帮助中心 **100** 句次、法律与政策页 **49** 句次；全表 **144** 行、句次合计 **1110**、去重命中句 **482** 句，占全池 62.8%）。其中 **3** 个词在整池一句都没出现（分别关于窗口长度的另一种写法、"免费"的另一种说法与"废止"的写法），本篇按零命中登记而不改词表，也不把它们当作任何官方句子的引文。零命中不等于官方没有这一项内容——例如"免费入口点窗口"这件事在正文里是用 `free entry point` 写的，只是没有用 `7 days` 这个短语；而"废止"这件事在本篇语料里是用 `remains available` 与时间戳写的（3.1）。

## 1.3 一页整页零句与它的登记位置

本篇 **33** 个登记位里，**32** 页进入了原句池，池共 **768** 句；另有一页正文取回、整页入池零句：《template_category_update webhook reference》（14.3）。这一页是类别变更 webhook 的参考页，正文由参数表与字段说明组成，按建池口径（含竖线、以列表符号起手、长度越界的段不入池）整页都进不了句子层。它在附表 A 留一行登记，在其余五张附表里不会出现；正文在 14.3 只登记"这一页存在、它描述的信号类别与另一页同一族"两件事，不代它引句，也不声称掌握它的取值。

反过来还有一类位置：一页只有两句可引。《payment_configuration_update webhook reference》整页入池只有两个取值说明（14.3），《Enroll in the max price feature》入池只有六句（11.2）。这两页在本篇照常引用，但读者要知道它们在池里的句子少，不能用"这一页没有某句"推断"官方没写这件事"。

## 1.4 本篇不做的事与范围红线

本篇是官方计费文本的口径梳理，不是操作手册，也不是对平台定价合理性的评价。**不含**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法；**不含**任何可用于向非自愿收件人大量投递消息的方法——"怎样把营销消息伪装成服务消息""怎样把一条群发拆成多条免费额度""怎样在额度用尽后继续投递"这三类问法在本篇一律不答；**不含**代码、恶意样本或利用链构造方式；**不含**任何针对他人设备或他人账号的操作步骤与时序。

界面页与接口页里官方写成"步骤"的内容——添加支付方式的层级、信用额度的申请顺序、币种迁移的接口调用次序——本篇只登记**控件名、接口名与条目数**，不重述点击顺序，也不把清单转写成可执行的动作序列。第 13 章出现的入口名（Billing & payments、Credit lines、Insights 一类）都按官方原词点名，读者要动手请回原页，本篇不提供操作路径。

# 2 计费的基本单位

第 2 章处理最小的一组问题：谁向谁付、在哪个时点算一笔、一条消息最多几笔、价格由哪两个坐标决定。这四件事在官方文本里被写在总纲页的开头几段，但答案分散在四个小节里，且与后文的免费额度、阶梯、费率卡相互限定。

## 2.1 计费的方向与触发点

《Pricing on the WhatsApp Business Platform》把方向写在第一句总纲里：`Meta charges by category–market, and only for messages from businesses to users.`——计费按"类别—市场"组合发生，而且只针对商户发给用户的消息。同一页紧接着写的是反向的否定句：用户发给商户的消息不产生费用（这一句含官方粗体排版，按第 1.4 节的规矩不整句引用，只作中文陈述）。帮助中心页把同一件事写成第二人称：`When your business messages customers through the WhatsApp Business Platform, you'll be charged per message we deliver.`。三处合起来，方向只有一个：**钱从商户流向平台，不从用户流向商户**；本篇因此不写"来一条回一条都算钱"这类对称结论。

触发点在两处文本里被写成同一个词。帮助中心页写：`We charge only when a message is delivered.`；计费总纲页在写计费条件时用了 "delivered (vs. sent)" 这个对照，把"发出"与"送达"放在同一句里区分（该句含官方粗体排版，同样只作中文陈述）。**发出不算、送达才算**，这是本篇后面所有"免费""额度""窗口"讨论的前提：所有不计费的写法，最终都要回答"这条消息有没有被送达"。

计费对象还有一层限定：一条消息只落一个类别。官方用一段五步交互的例子说明这件事——从营销模板开始，中间经过 Meta Business Agent、人工客服与订单确认，五条送达消息对应五笔计费（附表 B 登记了这句例子里的数字与出处）。本篇把这个例子读成"逐条计费、逐条归类"，而不读成"整段交互打包计价"。

## 2.2 五类消息与"一条一笔"

官方把消息分成五类：营销、utility、authentication、服务，以及 2026 年新增的 Meta Business Agent。前四类在本篇各有一节（营销见第 11 章、utility 与 authentication 见 4.3 与第 10 章、服务见第 4 章），第五类单列第 5 章。

"一条一笔"这条规则在官方文本里没有单独一句定义句，它是用例子写出来的：计费总纲页的交互例子把五条送达消息与五笔计费一一对应，并写明每个类别只产生与该类别对应的一笔。本篇因此采用"一条送达消息最多一笔、类别决定这一笔按哪张费率"的读法；官方没有写"一条消息可能同时按两个类别收费"，也没有写"同一类别可以叠加两笔"。

## 2.3 价格的两个坐标

费率不是全球一个价。总纲页把两个坐标写进同一个短语（category–market），并在费率表前给出国家码到国家或地区的对照（映射表的原句在 8.1 引）。也就是说，"按条计价"只说明计量的粒度，不说明每条多少钱；每条的单价要落到"哪个类别 × 哪个市场"这个格子里，再去费率卡上取值（第 8 章）。

官方对"市场"的写法有一处容易读错：费率卡上列的是国家或地区，而判定用的是**收件人号码**的国家码；商户所在地不参与这一层判定（国际费率是另一层，见第 10 章）。本篇因此把"市场"读成"收件人所在市场"，不读成"商户注册地"。

## 2.4 opt-in 与计费不是同一条规则

计费总纲页在客服窗口一节里插了一句 opt-in 提醒（原句在 7.3 引）。这句话约束的是"能不能发"，不是"算不算钱"；反过来，第 4 章的免费额度与第 6、7 章的窗口约束的是"算不算钱"，也不解除 opt-in。两层规则各自独立，官方没有写"未 opt-in 的消息不计费"或"计费的消息一定已 opt-in"。本篇在 7.3 再处理一次这个交叉，结论相同：不要把合规前提读成计费条件。

# 3 按会话计费的历史口径

第 3 章处理一页仍在线上、但已标注为历史的文本。它没有被删除，因为它仍是历史账单的读法；本篇把它单列一章，正是为了不让"按会话"与"按消息"两种单位在同一段里混用。

## 3.1 这一页为什么还在

《Conversation-based pricing (Deprecated)》是会话计费时期的说明页。页面开头先交代自己的身份：`It remains available for developers who have historical billing data from the conversation-based pricing period.`——它保留给需要读历史账单数据的开发者。第二句是范围限定：`This document explains how conversation-based pricing works on the WhatsApp Business Platform.`。两句合起来说明：这一页描述的是一套**过去的**计费方式，不是现价。

旧口径的核心单位写在第三句：`Charges are applied per conversation, not per individual message sent or received.`——按会话计费，不按单条消息。会话的定义紧随其后：`Conversations are 24-hour message threads between you and your customers.`；开启与计费的时点：`They are opened and charged when messages you send to customers are delivered.`。这三句构成本篇在第 3 章的唯一结论：**会话计费的计量对象是"一段二十四小时的线程"，不是"一条消息"**；它与第 2 章的按条计费不是同一把尺，任何把两者相加或换算的说法都不在官方文本里。

## 3.2 会话类别与开启条件

旧口径把会话分成四类：营销、utility、authentication 与服务。类别与开启条件被写成两句话：`Marketing, utility, and authentication conversations can only be opened with template messages.` 与 `Service conversations can be opened with any type of message other than a template message.`——前三类只能用模板消息开启，服务会话则可以用模板以外的任意消息开启。会话的持续与复用写在例子里：`If one does not exist, a new **conversation of that category** is opened, lasting 24 hours.`；同一类别已有会话时不另开：`An open utility conversation already exists between you and the customer, so a new utility conversation is not opened.`。

这一页还留了一句责任句：`The criteria that determines when a conversation is opened and how it is categorized is explained below. **Warning:** Businesses are responsible for reviewing the category assigned to their approved templates.`——商户要自己复核模板被分到的类别，使用模板即接受按该类别计费。这句话在现行按条计费下仍然成立，因为类别决定费率（2.3）；本篇把它放在 3.2 而不是第 11 章，是因为它出现在会话计费页，属于旧口径留下的通用责任，而不是营销专属规则。

## 3.3 免费入口点会话

旧口径里的免费入口点会话有三个特征。第一是长度：会话在商户消息送达时打开，持续一段固定时长（原句在 6.1 引）。第二是开启条件：商户在用户来信后的时限内没有回复，免费入口点会话就不会打开，只能改用模板开启一个按类别计费的会话（原句同样在 6.1）。第三是排他性：`When a free entry point conversation is opened, it automatically closes all other open conversations between you and the customer, and no new conversations will be opened until the free entry point conversation expires.`——免费入口点会话一开，其它打开的会话全部关闭，且在它到期前不会新开任何会话。

窗口期内的计费写法是：`Once the free entry point conversation is opened, you can send any type of message to the customer without incurring additional charges.`——任意类型的消息都不额外计费。但"不额外计费"不等于"什么都能发"：非模板消息仍要有一个开着的客服窗口，这一条的原句在 6.4 引。

# 4 服务消息、免费额度与恢复计费

第 4 章处理"免费的那一部分"。官方在同一批页面上写了三层：哪些消息属于服务消息、每月有多少条免费、以及这些免费从哪一天起结束。三层之间有时间差，读错任何一层都会把"现在免费"读成"一直免费"。

## 4.1 服务消息与非模板消息

《Upcoming pricing updates for Meta Business Agent, service and utility messages》定义的是非模板消息的两分：服务消息与 Meta Business Agent 消息。其中服务消息的身份被写成一句极短的话：`This is an existing message category on our platform.`——它是平台上已有的类别。边界在另一句里被写成否定式：任何不是由 Meta Business Agent 驱动的非模板消息都属于服务消息（这一句自带官方小标题与粗体排版，本篇按引句规矩不整句引用，只作中文陈述）。也就是说，非模板消息不都是服务消息，只有"不是由 Meta Business Agent 驱动"的那一部分才是。

计费方式在专项页里与模板消息并排写：服务消息按条计价、费率随市场与类别变化（第 8 章）。这一页还写了一条对第 4 章的走向很关键的规则：`Meta will not offer volume tiers for service messages.`——服务消息不设量级阶梯。这条规则把服务消息从第 8 章的阶梯体系里摘出去，也解释了为什么后文的阶梯只对 utility、authentication 与营销生效。

## 4.2 每月一千条免费额度

免费额度挂在电话号码这一层，不挂在账户或组合层。总纲页写得最直接：`Thus, Meta only charges as of the 1,001st service message for each business phone number.`——每个电话号码每月一千条免费服务消息；未用完的部分不结转：`The free tier resets monthly for each business phone number (for example, 1,000 for October with no roll-over; 1,000 for November with no roll-over).`——额度按月重置、十月与十一月各算各的。重置时点也被写死：以消息账户时区的零点重置；这一句与"没有支付方式会怎样"写在同一句里，4.3 一并引。

额度与群组的关系另有一句：`The 1,000-message free tier is shared with 1:1 service messages for the same business phone number, and each group send consumes one unit per delivered recipient.`——群组发送与一对一的服务消息共用同一个额度，每次群发按每个送达收件人消耗一个单位。这一句是 12.1 的入口，也是附表 F 的 F6 行：单聊页面只写"每个号码有一份额度"，群组页才补出共享关系。

## 4.3 没有支付方式时投递到哪里为止

同一句的后半段回答了"没有支付方式会怎样"：`This reset happens at 12am by Messaging account timezone. - If businesses do not have a payment method for their Messaging account(s), Meta will deliver service messages within the free tier but _not_ deliver them after the free tier has been used.`——免费额度内的服务消息照常投递，额度用完之后就不再投递。这句话是本篇语料里对"欠费"最接近的写法：官方没有写"欠费后补缴再恢复"，只写了"额度内投、额度外不投"。本篇因此不把"没有支付方式"读成"可以继续发只是稍后结算"。

## 4.4 服务消息与窗口内 utility 的恢复计费

恢复计费有两个时点，分属两类消息。服务消息的历史免费期从 2024 年 11 月 1 日开始；这一句与紧随其后的恢复日写在同一段里，段内自带官方粗体排版，本篇按引句规矩只作中文陈述，原句与句序在附表 B 有登记。窗口内 utility 消息的免费期从 2025 年 7 月 1 日开始，原句是：`These messages have not been charged since July 1, 2025.`。两类消息的恢复日相同，都是 2026 年 10 月 1 日；这一日期写在总纲页的变更段落里，附表 B 与附表 F 的 F2、F3 行各登记一次。

这里有一处必须点明的口径差：帮助中心页仍写着"客服窗口内的服务消息不收费"（`Businesses can respond with service messages within the 24-hour customer service window at no charge.`），而计费总纲页写的是"每个号码每月一千条免费、第一千零一条起计费"（4.2 已引）。两句都对，但回答的不是同一个问题：前者是额度内的状态，后者是额度外的规则。本篇按后者写现行口径，把前者读作"免费额度内的服务消息不另外收费"（附表 F 的 F2 行）。

## 4.5 AI Provider 的计价政策与政府豁免

《New pricing policy for AI Providers leveraging the WhatsApp Business Platform》回答的是一个更窄的问题：当 AI Provider 在"法律要求平台允许其使用"的市场里向用户发服务消息时，Meta 如何计费。政策的生效日写在第一段：`This policy took effect February 16, 2026 and applies to markets as outlined below.`；计费对象是这类主体投递到这些市场的服务消息；有的市场免费，原句是：`Meta does not charge for these messages.`；有的市场先按公布费率收费、后来停收，原句是：`Meta does not charge as of May 13, 2026.`。

三条边界必须一起读：这套费率只落在 AI Provider 身上，`These rates do NOT apply to any other businesses using the WhatsApp Business Platform.`；它也不改变 Meta 对其余商户的计费方式，`This does NOT change how Meta charges all other businesses using the WhatsApp Business Platform.`。所以这一页不是"服务消息要涨价"的证据，它只覆盖一类主体；真正改变服务消息计费的是 4.4 的两个日期。

同一段文本里另有一套面向政府与非营利组织的政策：符合条件的组织可以获得常开的服务消息能力，政策有效期写到 2027 年底（`This pricing policy will be in effect through December 31, 2027.` 与 `Meta continues to support governments and non-profit organizations, and this policy allows Meta to enable eligible governments and non-profits to maintain an always-on presence on WhatsApp, to respond to and engage with WhatsApp users.`）。资格不是靠人工告知，而是靠字段读出来：服务消息的 `subtype` 为 `paid_exempt` 时，这条消息在该政策下免费；`service_pricing_policy` 为 `REGULAR` 时不算符合。这两句原文含反引号字段名，本篇按引句规矩不整句引用，改用中文陈述并指明出处页；这两个字段值与命中句逐字登记在附表 C 的主题组 D。

# 5 Meta Business Agent 的 token 计费

第 5 章处理第五类消息。它与前四类的区别不在"发给谁"，而在"按什么计量"：前四类按条，它按 token。官方在专项页与总纲页各写了一遍，两处的措辞不完全相同，本篇把两处并排登记而不是取其一。

## 5.1 一条消息里有两段成本

Meta Business Agent 消息的计费对象不是"一条消息的投递"，而是"驱动这条消息的那次模型调用加上投递"。专项页把它写成"一次 token 计费涵盖 AI 处理与消息投递"，总纲页则把 token 消耗与投递拆成两段来写。本篇的读法是：**它仍然是一条消息一笔账，但这一笔的计价基础是 token，而不是条**；这与第 2 章的"一条一笔"不矛盾，与第 4 章的"服务消息按条"才是两套基础。

这一层还有一句限定：`Simple responses consume fewer tokens and cost less; complex responses consume more tokens and cost more. - _Volume tiers_: Do not apply. - _Free messages_: Do not apply. ### Pricing example {#example-across-all-message-categories} Below is an example of an interaction between a business and a user.`——简单回复消耗更少 token、成本更低，复杂回复相反；同一句的后半段写明"量级阶梯不适用""免费额度不适用"。也就是说，第 4 章的每月一千条额度与第 8 章的量级阶梯都不延伸到这一类消息。

## 5.2 只算输入与输出 token

专项页把计费范围写死：只对输入与输出 token 计费，缓存 token 不计费；计费方式与行业惯例对齐（`Charging for token consumption aligns to industry standards.`）。总纲页另给出"一档全球单价"的写法，并折算成"一条消息大约几美分"的估算；两个金额都只出现在附表 F 的 F16 行，本篇正文不写具体金额——**金额不是本篇的计价结论，单价与折算值的关系才是**：单价按 token 计，折算值按典型消耗估算，后者随消息复杂度变化，不能当报价用。

## 5.3 估算为什么只能读成估算

官方对"一条消息多少 token"用的是"typically"与"approximately"这类限定词，并明确说成本随复杂度变化。本篇因此只把折算值当量级参考：同一句话里既给了 token 区间，也给了金额区间，两个区间都不是承诺。附表 B 把这条句子连同它的限定词整行登记，正文不从中取中值，也不把区间端点写成两个价格档。

## 5.4 支付方式与白名单测试

这一类消息的支付方式分两步开放：先信用额度、后信用卡与借记卡；信用卡开放日还被官方自己往前挪过一次（`We previously shared this would launch on Sep. 15 for businesses in Brazil; that has been pulled forward to Sep. 8, 2026.`）。没有支付方式时，官方不投递这一类消息：`For any business that does not have a payment method for their Meta Business Agent account, Meta will not deliver Meta Business Agent messages.`。唯一例外是白名单测试——官方原文写明，字段 `ai_audience` 被设为 `ALLOWLISTED_ONLY` 的 agent 可以在没有支付方式的情况下先跑起来，让商户在正式计费前测试；这一句含反引号字段名，本篇按引句规矩只用中文陈述字段名与取值，不整句引用。

信用额度的申请要经过 Meta 复核：`This is subject to review and approval by Meta.`。这句话把"能不能用信用额度"从"有没有资格"切成"申请是否被批准"，本篇在 13.2 再处理一次这层责任。

# 6 免费入口点窗口

第 6 章处理第一层"免费"：免费入口点窗口。它与第 4 章的免费额度不是同一件事——额度按月挂在号码上，窗口按一次会话计时；官方在现行页与历史页上对它的写法也不一样，本章把两处并排读。

## 6.1 窗口由什么触发

触发场景写在计费总纲页的交互例子里：用户从"点按进入 WhatsApp"的广告进入会话，这一段交互就发生在免费入口点窗口内。窗口一旦打开，计时方式与长度写在旧口径页：`The free entry point conversation is opened as soon as your message is delivered and lasts 72 hours.`——消息送达即开窗，长度固定。这一页已标注为历史口径（3.1），但它对窗口本身的描述仍是这批页面里最完整的一段；现行总纲页只在"不计入量级阶梯"的注里提到这扇窗（6.2）。

窗口打不开的条件也写在同一页：`If you do not respond within 24 hours, a free entry point conversation is not opened and you must use a template to message the customer, which opens a marketing, utility, or authentication conversation, per the category of the template.`——商户没有在时限内回复，窗口就不打开，只能改用模板开启一个按类别计费的会话。这句把"免费窗口"从"用户来信就自动有"改写成"商户及时回复才有"。

## 6.2 窗口期内哪些消息不计费

窗口内"不额外收费"的写法在旧口径页里（3.3 已引）：任何类型的消息都不额外计费。现行总纲页在同一件事上只写了一句更窄的话：`Thus, any utility or authentication messages sent in a free entry point window do _not_ count toward volume tiers. - Volume tiers will be determined solely by Meta — All insights data is approximate due to small variations in data processing.`。这句话要分成两截读：前一截说窗口内的 utility 与 authentication 消息**不计入量级阶梯**；后一截说阶梯由 Meta 单独判定、洞察数据因数据处理差异只作近似。它没有写"窗口内免费"，也没有写"窗口内消息不占每月免费额度"。

这正是两页的口径差：旧口径页写"不额外计费"，现行页写"不计入阶梯"。本篇各按各页写——窗口内的计费状态读旧口径页那句，阶梯归属读总纲页这句；两者不合并成"窗口内既免费又不占额度"。

## 6.3 窗口打开时，原有会话怎么处理

旧口径页写得很硬：窗口一开，其它已打开的会话全部关闭，且在它到期前不再新开会话（3.3 已引该句）。这意味着窗口不是叠加在现有会话上，而是**替换**它们；要等它到期，按类别计费的会话才能重新开。如果商户在窗口开启前就回复了用户，开出来的是按模板类别计费的会话（6.1 已引），不是这扇免费窗。

## 6.4 窗口内的非模板消息为什么另需客服窗口

窗口内"任何类型都能发"不等于"非模板消息随时能发"。同一页紧接着限定：`However, you can only send non-templates messages if there is an open customer service window between you and the customer.`。于是两扇门必须同时开着：免费入口点窗口决定"这段时间算不算钱"，客服窗口决定"这条非模板消息能不能发"（第 7 章）。把两扇门读成一扇，是本章最容易出的错。

# 7 客服窗口

第 7 章处理第二层"免费"：客服窗口。它与第 6 章的窗口同名不同物——一个由广告进入触发、按会话计时；一个跟着用户的来信走、按时长滚动。

## 7.1 计时怎么走

计费总纲页把窗口的重置写在客服窗口一节：`If the user messages or calls you again before the timer expires, the timer resets to 24 hours.`——用户在计时到期前再来一条消息或来电，计时重置。帮助中心页把同一扇窗写成"二十四小时客服窗口内的服务消息不收费"（4.4 已引）。两句合起来才是完整规则：窗口长度固定，起点随用户每一次来信往后推，而不是从商户发出第一条消息起算。

## 7.2 窗口没开时还能发什么

`Businesses can only send non-template messages to users when this window is open.`——窗口开着才能发非模板消息；`If it is not open, businesses can only send template messages to reach out to users.`——窗口没开，只能发模板消息联系用户。两句是一组：**窗口管的是"非模板消息能不能发"，模板消息不受这扇门限制**。这也是模板消息与自由文本在计费上的分界：前者按类别计费、随时可发；后者要么在窗口内、要么不发。服务消息的免费额度只解决"算不算钱"，不解决"能不能发"（4.2、4.3）。

## 7.3 opt-in 约束的是"能不能发"

计费总纲页在客服窗口一节末尾插了一句提醒：`As a reminder, you can only send messages to WhatsApp users who have opted in to receiving messages from you.`。这句话约束的是"能不能发"，不是"算不算钱"：窗口开着、额度没超，也只对已 opt-in 的用户成立；反过来，opt-in 也不产生免费。第 2.4 节把它与计费规则分开过一次，这里的结论相同——合规前提不能被读成计价条件。

## 7.4 免费额度与窗口期叠在一起按哪个走

两件事各管一段：免费额度回答"这个号码这个月的前多少条服务消息不收钱"（4.2），窗口回答"这条非模板消息现在能不能发、窗口内的消息计不计阶梯"（6.2）。叠在一起时，本篇按三层分别判定：先看窗口是否开着（能不能发），再看消息是否落在额度内（收不收钱），最后看是否落在免费入口点窗口（计不计阶梯）。三层不互相顶替，也不相加成一条新规则。

# 8 市场、费率卡与量级阶梯

第 8 章处理"单价从哪里来"。第 2 章说过单价落在"类别 × 市场"的格子里；本章补三件事：市场怎么由号码判出来、十月费率卡更新动了哪两类东西、以及量级阶梯按什么周期与单位累计。

## 8.1 费率卡与市场映射

总纲页在费率表之前给出国家码到国家或地区的对照：`The table below shows how Meta maps country calling codes and ISO 3166 Alpha-2 country codes to countries or regions.`。这套映射说明"市场"不是商户注册地，而是**收件人号码所属的国家或地区**；费率卡上没有具名的国家落进"其余地区"，按所在区域的费率计。同一页还写明了具名市场与"其余地区"的取价次序，这篇只取结论，不复制映射表本身。

## 8.2 十月费率卡更新动了什么

官方把这次更新拆成两类，并各自给了生效日。第一类是把若干市场从所属的"其余地区"里移出，改为在费率卡上具名、按市场费率计；对 utility 与 authentication，这些市场的量级阶梯也随之改为按市场划分。第二类是给若干市场新增验证消息国际费率，这一类的费率水平高于该市场原有的 utility 与 authentication 费率（8.4）。两类是两件事：前者改变同一类别在市场间的取价方式，后者改变某一类消息在特定市场的费率水平。把两者读成"统一涨价"，不在官方文本里。

## 8.3 量级阶梯的周期与适用范围

阶梯按月重置，重置点是消息账户时区的零点；累计以业务组合为单位，跨多个计费账户时按组合合起来算，而不是每个账户单独起算。适用范围有三处硬边界：服务消息不设阶梯（4.1 已引），免费入口点窗口内的消息不计入阶梯（6.2），Meta Business Agent 的量级阶梯明确不适用（5.1）。官方用几个多账户、多合作伙伴的例子说明"某个月没跨到下一档"会怎样，本篇只取结论，不复述例子里的具体条数。

## 8.4 验证消息的国际费率

这类费率不是另一张独立价目表，而是在原有市场费率上加的一层：`New authentication-international rates in 9 markets {#new-authentication-international-rates} These are higher than the utility and authentication rates in these markets, aligned to industry standards.`——它高于这些市场原有的 utility 与 authentication 费率，官方把它写成与行业惯例对齐。它落在哪些市场、什么时候开始适用、商户会通过什么信号得知，都不在这句里，而在专项页（第 10 章）。

# 9 币种、开票实体与迁移

第 9 章处理账单的"面值"与"抬头"。计费单位解决一条消息值多少钱，币种解决这笔钱记在哪个币种下，开票实体解决这张账单由谁开给谁；三者由不同的字段决定，也由不同的页面写。

## 9.1 币种在创建时选定

`Currency selection is part of the Messaging account creation process; once selected, a currency cannot be changed.`——币种是消息账户创建流程的一部分，选定之后不能改。这句是本篇所有币种讨论的基线：它不是"默认币种"，而是账户的不可改属性。所有后续的"换币种"讨论，都只能是换账户，不能是改这一个账户的币种。

## 9.2 迁移是另开账户，不是改币种

既然原账户的币种不能改，官方的迁移方案是并存一个账户：`Under the new account model, a business can hold a separate Messaging account per billing currency on a single business phone number.`——同一个电话号码上可以同时挂多个币种各自的消息账户。迁移完成时，计费从旧账户切到新账户：`This switches billing for your existing phone numbers from the old Messaging account to the new one.`。旧账户不删除，留在组合里供历史对账：`The original Messaging account is not deleted — it remains visible in all linked Business Managers for historical reference.`。

## 9.3 克隆迁移卡在支付方式上

《Change billing currency via API》把迁移写成"克隆 + 切换"：新账户克隆旧账户的模板与资产，电话号码本身不迁——它属于账户层级，不随计费账户走。完成条件落在支付路径上：新账户既没有附加支付方式、也没有挂上信用额度时，迁移不会完成。迁移时长随模板与资产的数量变化：`Migration duration varies depending on the number of templates and assets.`。开票实体由两件事共同决定——消息账户的币种与法人实体的总部地址；这一句原文带列表尾巴，本篇只作中文陈述，两个决定因素按原词点名，不转写成一句话。

## 9.4 BRL 迁移的截止与后果

总纲页对巴西雷亚尔账户单列了一段：截止日之前必须把组合内所有消息账户迁到 BRL，逾期之后 Meta 不再投递未迁移账户的消息；同一段还写明 BRL 账户的账单由巴西本地实体开具。这两句原文都带粗体日期，本篇按引句规矩只作中文陈述，具体日期、实体名与两处写法的差异在附表 F 的 F13 行登记。要强调的是后果的写法：官方写的不是"补缴后恢复"，而是"不再投递"，这与 4.3 的额度外停投是同一类硬边界。

# 10 验证消息的国际费率

第 10 章处理"同一类消息在特定市场被加价"这件事。第 8.4 节写了它的水平（高于该市场原有费率），本章写它的三件事：谁会被按这一类费率计、主要营业地怎么判定、以及商户通过什么信号得知生效时间。

## 10.1 谁会被按国际费率计

《Authentication-international rates》把这一类费率挂在一组条件上，并单独点出"基于印度"这一种情形：`If your business is based in India, however, you will be billed the authentication-international rate, if you meet all of the criteria above.`——满足前文全部条件时，即使业务基于印度，也会被按验证消息国际费率计费。条件本身不在本次取回的句子层里，本篇不复述它，也不把"基于印度"读成唯一触发条件：原文的写法是一条并列在条件之后的补充，而不是独立规则。

## 10.2 主要营业地由谁判定

判定不是商户自己申报就完事。官方先用公开可得的信息尝试判定，判定结果会写进业务管理器的"主要营业地"字段；判定不出来时，商户可以在设置里指定或补充回答，由 Meta 复核。整个判定有时限：`This process can take up to 3 business days.`。这一段里官方写成"步骤"的内容，本篇只点名控件名与字段名，不重述点击顺序（1.4）。

判定结果决定了哪一国的国际费率落在哪个号码上：同样的模板、同样的收件人，主要营业地不同，适用的费率就不同。本篇因此把它读成**一个账户级属性**，而不是一条消息级属性。

## 10.3 生效时间通过什么信号告知

生效前留了缓冲：`Start times are set when your business is first deemed eligible for authentication-international rates, and are 30 days from your eligibility date, so you will always have 30 days notice before the authentication-international rate applies.`——起始时间在资格判定时设定，距资格日有一段固定时长，因此商户总能提前得知。这一句里的"总能"是官方的写法，不是本篇的推断。

信号有两个：资格邮件与账户更新 webhook。`The webhook will include start times for each country that has an authentication-international rate.`——webhook 会带上每个设有国际费率的国家的起始时间。邮件里还会写出 Meta 判定为主要营业地的那一国（如果判定得出来）。商户要在接口里读的，是这只 webhook 的字段与各市场的起始时间，而不是邮件正文；邮件与 webhook 给的是同一组值，读哪一边都不改变生效时点。

# 11 营销消息的最高价

第 11 章处理一个 2026 年新增的工具：营销消息的最高价。它不是改费率，而是在费率之上加一个"我愿意付到多少"的封顶；它也不改变计费单位，只改变一次投递的成交价。

## 11.1 最高价约束的是哪一层

《Set a max price for marketing messages (BETA)》把这件事写成两条定价特性：一是每条营销消息投递的最高价，二是帮助商户设定最高价的触达估算工具。开放时点写得最直接：`Max price enters open beta on October 1, 2026.`。最高价的作用范围也有原句：`The effective bid is the maximum price Meta uses for that delivery — as with any max price, Meta charges that amount or lower per message delivered.`——实际出价就是这次投递的最高价，Meta 按这个数或更低收费。

最高价与预算上限不是一层：前者封顶"每条送达多少钱"，后者封顶"一个活动周期内花多少钱"；官方明确写两者互不排斥，设了最高价的模板可以放进同时也设了预算的活动里。本篇因此把"最高价"读成单条封顶，不读成活动总预算。

## 11.2 开放分几个阶段，报名为什么要签协议

最高价不是一次全量开放。官方把开放写成三个阶段：有限测试、开放测试、以及后续的正式阶段；前两个阶段各带开始日期，且日期在原文里是粗体，本篇按引句规矩只作中文陈述。每个阶段的资格面也不同——早期只对合作伙伴与其直接集成的客户开放，开放测试阶段才对所有客户开放。

《Enroll in the max price feature》写的是报名这一层。资格面先被限死：`Max price access applies only to Messaging accounts that your clients own and that are shared with or managed by your business.`——只有客户自有、并与商户共享或由其管理的消息账户才有最高价访问权。报名按业务组合签一次协议；协议上有指定签署人，Meta 只用其邮箱发接受链接，且不把这些字段用于营销、也不与签署人的其它 Meta 身份关联：`Meta does not retain either field for marketing, associate it with the signer's other Meta identities, or share it with third parties.`。也就是说，签协议不是为了营销触达，而是为了让 Meta 有权限按组合批量放行账户。

## 11.3 类别变了以后走哪个端点

《Route template messages by category》处理的是"类别变了以后怎么发"。模板的类别不是批准后就固定：如果 WhatsApp 判断内容与类别规范不符，类别会在批准后改变。类别一变，发送请求的端点也要跟着变：营销模板走营销端点，utility 与 authentication 走通用消息端点；走错端点会被拒绝，并提示类别不匹配，重试前要先刷新类别。

重试本身有约束：`Retry only after a confirmed failure, and make retries idempotent.`——只在确认失败后重试，且重试要幂等。这一句与第 11.1 节的"最高价是单条封顶"合起来，才是这一类消息的完整发送面：端点按当前类别选，价格按最高价与费率卡取低，重试只对确认失败的那一次做。

# 12 群组、通话与多方案会话

第 12 章处理三个把"一条消息一笔"这条规则撑开的使用面：群组把一条群发拆成按收件人计的若干笔，通话把跨档规则反过来写，多方案会话让同一个号码对应多个计费账户。

## 12.1 群组：一条群发为什么拆成多笔

《Groups API Pricing》的开头就把计费单位改写成"每个送达收件人"：发到群里的计费消息，按实际送达的人数逐条计费，送达几人就算几笔。免费期也有单独的截止：`Through September 30, 2026, this allows you to send utility and non-template messages for free.`——在截止日之前，群组里的 utility 与非模板消息免费；截止日之后恢复计费。额度关系在第 4.2 节已引：群组发送与一对一服务消息共用同一个每月额度，每次群发按每个送达收件人消耗一个单位。

这三件事要一起读：**群组的"一条消息"是发送动作，不是计费单位**；计费单位是"每个送达收件人"。所以一条群发在账单上是多笔，在额度上也是多份；把它读成"一条群发一笔"，会同时算错账单与额度。

## 12.2 通话：跨档为什么取低档价

《Calling API Pricing》的跨档规则与消息相反：`For calls that cross pricing tiers (for example from the 0 - 50,000 tier to the 50,001 - 250,000 tier), the entire call is priced at the lower rate (that is, the rate of the higher volume tier).`——一通电话跨过档位时，整通按较低的那一档费率计，也就是较高量级档的费率。消息是按当条所在的档计，通话是把整通拉平到低档，这是两种不同的"跨档"处理。

市场调整也有一条独立的写法：`Each of the 9 markets keeps the rate of the region it is leaving; no market's per-minute rate or volume tier thresholds increase or decrease.`——被移出区域的市场保留原区域费率，每分钟费率与档位门槛都不升不降。录制与转写的单独计价还只是计划：`Separate pricing for recording and transcription is planned for a future date.`。本篇因此不把通话的"跨档取低"套到消息上，也不把"计划中的单独计价"读成现行收费项。

## 12.3 多方案会话：一个号码为什么对应多个账户

《How to use Multi-Solution Conversations (MSC)》写的是合作伙伴与终端客户共享号码的拓扑：`Your client shares their WhatsApp account, which holds their business phone number, with each partner they work with, and each partner has its own Messaging account.`——客户把持有号码的 WhatsApp 账户分享给每个合作伙伴，每个合作伙伴各有自己的消息账户。同一号码不能被同一伙伴的两个账户同时分享：`If you have the same phone number linked to WABA 2, you cannot also share WABA 2 with Partner 1.`——这条限制把"一个号码多个账户"收在伙伴边界上。

对计费的含义直接：账单落在**实际发送所用的那个消息账户**上，而不是落在号码上；同一号码下并存多个币种账户时（9.2），发送请求要指明记到哪个账户，否则计费归属就不确定。

# 13 支付方式、信用额度与责任

第 13 章处理"钱怎么付、谁负责"。计费规则决定应收多少，支付方式决定从哪里扣，信用额度决定谁先垫，责任条款决定追到谁；四层在官方文本里分属不同页面，不能互相替代。

## 13.1 支付方式：平台侧与商业 App 侧清单不同

《About billing for your WhatsApp Business Account》与《How to add a credit card to your Messaging account》都写平台侧的支付方式。前置条件写在帮助页里：`To send messages, you must first add a payment method to your messaging account.`——要发消息，先给消息账户加支付方式。可用的卡种被写死：`You can pay for messaging with a credit card via Visa or Mastercard.`——信用卡走 Visa 或 Mastercard。排除项也写死：`We currently don't accept American Express or digital payments such as PayPal.`——目前不接受 American Express 与 PayPal 一类的数字支付。

权限是另一层：`To add a credit card, you need permission to manage payment settings for the messaging account.`——加卡需要"管理支付设置"的权限，账户所有者自动拥有。平台侧另有一条给解决方案合作伙伴的路径：可以申请信用额度作为支付方式（原文带列表，本篇只作中文陈述，13.2）。商业 App 侧的清单不同，支持 PayPal 与银行直扣，见 15.2；两面不合并。

## 13.2 信用额度：责任落在合作伙伴身上

《How to Apply for A Credit Line for Your WhatsApp Business Account》写申请与挂载，《Managing credit lines》写共享与责任。资格与流程先被限定：只有服务类合作伙伴可以申请新的信用额度，批准后才能挂到账户上作为支付方式；通过嵌入式注册创建的账户，由合作伙伴用接口把额度共享给终端客户。

责任链写得很直：`This means that businesses pay you, and you receive an aggregated invoice to pay Meta.`——终端客户把钱付给合作伙伴，合作伙伴收到一张聚合计费单再付给 Meta；`You are the "Bill To Party" for all businesses sharing your credit line.`——对所有共享这条额度的商户，合作伙伴是"Bill To Party"；`You are liable for and will pay Meta for all WhatsApp Business Platform spend made by these businesses.`——这些商户产生的全部平台消费，由合作伙伴对 Meta 负责并付款。

终端客户一侧的边界同样明确：`The end clients can't use their own credit line to pay for their WhatsApp usage.`——终端客户不能用自己名下的信用额度来支付其 WhatsApp 用量。两句合起来是一个闭环：**消费在终端客户，账单责任在合作伙伴，支付路径只能走共享额度**。这也是为什么账户与消息账户被解除共享后，该消息账户的全部消息会被阻断——断的不只是支付方式，是这条责任链。

# 14 账单读数与计费信号

第 14 章处理"钱发生之后怎么看见它"。计费规则决定应收多少，账单与接口信号决定商户能不能复核；这两件事在官方文本里分属帮助页、接口页与 webhook 参考页，颗粒度差别很大。

## 14.1 在哪里读账单与用量

平台侧的实时读数写在帮助页里：`You can view the real-time monitoring of messaging and spending for your WhatsApp Business account in the Insights tab of your WhatsApp Manager.`——消息与花费的实时监控在 WhatsApp Manager 的 Insights 标签里。这是本篇语料里唯一一句把"用量"与"花费"放在同一个读数面上的话；它说明监控是同一处，但没有承诺这个数与最终账单逐分对齐——第 8.3 节引过的"洞察数据只作近似"就写在同一批页面上。

## 14.2 接口层的用量读数能给出什么

通话页给了一个具体的接口读数面：`The endpoints can provide useful information like cost, counts of completed calls, and average call duration.`——接口能给出花费、已完成通话数与平均通话时长一类信息。这类字段是"事后读数"，不是"事前报价"：它们回答"已经发生的通话花了多少"，不回答"下一通会花多少"。把接口读数当报价，是这一章最容易出的错。

## 14.3 计费相关的 webhook

计费相关的信号有两类。第一类是支付配置的状态变化：《payment_configuration_update webhook reference》整页只有两个取值说明——配置在 WhatsApp Manager 里测通、可以用 Payments API，以及配置与支付网关断开、需要重新连接。这一页的两句都带字段名，本篇按引句规矩不整句引用，只登记它描述的是"支付配置的可用状态"，不是扣款事件。

第二类是模板类别的变化：《template_category_update webhook reference》。这一页正文由参数表与字段说明组成，整页入池零句（1.3），本篇不为它代引；它描述的信号与路由页写的是同一件事：`A template's category can change after approval if WhatsApp determines that its content does not match the template category guidelines.`——类别可能在批准后被改判。信号本身只说明"类别变了"，不说明"费率变了"；费率怎么变要回到第 2、8 章的规则表里取。

两类信号都不能当账单用：一个说"支付路径通不通"，一个说"类别是什么"，没有一个字段说"这次扣了多少钱"。扣款结果要对账，只能回到 14.1 的读数面与账单本身。

# 15 与平台计费相邻的付费能力

第 15 章处理五个容易被混读成"平台消息计费"的产品面：商业 App 的营销消息与广播、商业 App 的支付设置、两个订阅产品、Meta Business Agent 的套餐入口、两步验证对支付账户的作用，以及条款层留下的责任句。它们的共同点是都出现"扣款""续费""退款"这些词，但没有一个改变第 2—13 章的计费规则。

## 15.1 商业 App 侧的营销消息与广播

《About pricing for marketing messages》与《About pricing for business broadcasts》写的是同一个产品的两个名字：`Marketing messages is now business broadcasts in some countries.`——在某些国家，营销消息已经改叫商业广播。计费口径写得很具体：`With marketing messages, you only pay for each message that is delivered to a contact within five days of being sent.`——只对发出后五天观察窗内送达的每一条消息付费；`You only pay for each message that is delivered to a contact within five days of being sent.`——同一条规则在广播页重写了一遍。余额的处理是累计式的：`After the balance exceeds a set amount or you reach your billing date, you’ll be charged for the outstanding balance.`——余额超过设定值或到了账单日，才对未结余额扣款。

这一面与平台侧的差别在时点：平台侧是"送达即计费"（2.1），商业 App 侧是"发出后五天内送达、余额累计到阈值再扣"。两个时点不能互换，也不能把五天窗套到平台侧（附表 F 的 F9、F10 两行登记的就是这两处）。

## 15.2 商业 App 侧的支付方式与预算控制

《How to pay for ads and business broadcasts on the WhatsApp Business App》与《How to pay for ads and marketing messages on the WhatsApp Business App》写支付设置。与本篇其余页面最大的不同是支付方式清单：`Supported payment methods include credit and debit cards, PayPal, a bank account (direct debit) in supported countries, and local manual payment methods in supported countries and currencies.`——支持信用卡与借记卡、PayPal、支持国家的银行直扣，以及支持国家与币种下的本地手动支付方式；`Add, remove, or edit a payment method You don't need a credit or debit card to pay for ads.`——付广告费甚至不需要信用卡或借记卡。这与平台侧"只收 Visa 或 Mastercard、不收 Amex 与数字支付"（13.1）是两个清单；哪一面支持什么，就在哪一面写，不合并成"WhatsApp 支持 PayPal"这类跨面结论。

预算控制是另一层：`If you set an account spending limit, your ads and business broadcasts will pause once you reach that limit.`——设了账户花费上限，广告与广播到达上限就暂停；`You'll be charged each time you reach your payment threshold.`——另外还有一个付款阈值，决定多久扣一次款。上限管"花到多少停"，阈值管"什么时候扣"，两者都不改变单条消息的计价。

这两个页面里官方写成"点击步骤"的部分（添加支付方式、验证卡、设置上限、退款申请的入口）本篇只点名控件与步骤数，不重述点击顺序（1.4）。

## 15.3 订阅类收费：Meta One 与 Meta Verified

这一组是六个页面，写的是两种订阅产品与它们的账单、退款、权限。

**Meta One**：《About Meta One billing and payments》写续费与支付方式变更，`Payment method changes take effect at the start of your next billing cycle.`——支付方式的更改在下一个账单周期开始时生效；支付失败时：`If you don't have a backup and your payment fails, you may get a short grace period to fix the issue before your subscription is canceled.`——没有备份支付方式又扣款失败，可能得到一段短期宽限期，之后订阅被取消。

**Meta Verified for Business**：续费与退款分写在四页。《About payments for Meta Verified for Business on WhatsApp》给出续费句：`Automatic renewals Meta Verified business subscriptions are charged on a monthly basis and automatically renew every month.`——按月收费、每月自动续订。《About refunds for Meta Verified for Business on WhatsApp》给出退款边界：`If you cancel your subscription during your current billing cycle, you won’t be eligible for a partial refund.`——在当期账单周期内取消，不按比例退。退款到账的时限写在《Billing and payments for Meta Verified business subscriptions》里：`Refunds may take several business days, and in some cases up to 60 days, depending on your bank or payment provider.`——退款要几个工作日，个别情况下更长，取决于银行或支付服务商。这句时限属订阅，不套到平台消息账单（附表 F 的 F18 行）。支付失败与"扣了两次"的处理写在《Can’t make payment for Meta Verified for Business on WhatsApp》：`If your default payment method fails for any reason, your backup payment will be charged to keep your subscription active.`——默认支付方式失败时，备份支付方式会被扣款以保持订阅有效。

**谁能买**：《About admin permissions for subscribing to Meta Verified or Meta One for businesses on WhatsApp》把权限写在前面：`The account subscribing to Meta Verified or Meta One must have admin permissions within the business portfolio.`——订阅 Meta Verified 或 Meta One 的账户必须在业务组合里拥有管理员权限。这条把"能不能订阅"从"有没有钱"切成"有没有权限"，与 13.1 加卡权限是同一类门。

六个页面的共同点：都只写订阅自身的扣款、续费、退款与权限，没有一句改变平台消息的按条计费。两张订阅产品的续费句句面几乎相同，但分属两个产品，本篇分产品写，不合并成一条规则。

## 15.4 Meta Business Agent 与 Meta One 套餐

《About using Meta Business Agent with Meta One》与《About Meta One plans》写的是"套餐里的 AI 能力"。《About using Meta Business Agent with Meta One》先把一件事说死：`Subscribing to a plan that lists Meta Business Agent doesn’t guarantee access to it.`——订阅一个列出 Meta Business Agent 的套餐，并不保证能用上它。App 侧的使用上限也有写法：`If you reach your free limit on the WhatsApp Business app, you can subscribe to a Meta One plan to get more access to 24/7 responses from Meta Business Agent where it’s available, or wait for your free limit to refresh.`——免费额度用尽后，可以订阅套餐获得更多全天候响应，或者等额度刷新。

套餐本身的结构写在《About Meta One plans》：`Meta One offers multiple plan options with different pricing and levels of benefits.`——多个套餐选项、价格与权益层级不同；另有一条资格限制：`Meta Subscription paid benefits are not currently intended for use by government or political entities, including politicians.`——付费权益目前不面向政府或政治实体（包括政治人物）。对计费的含义是：套餐是"能力的打包价"，不是"消息的折扣价"；它不改变第 5 章按 token 计费的规则。

## 15.5 两步验证与支付账户恢复

《About two-step verification and payments》写的是用户侧支付，与商户计费不在同一层，但它解释了一件容易被误读的事：`If you use payments on WhatsApp, enabling two-step verification will enhance the security of your payment information.`——使用 WhatsApp 支付时，启用两步验证会增强支付信息的安全；`Enabling two-step verification is optional, but you must have two-step verification enabled to restore your payments account information.`——两步验证是可选的，但要恢复支付账户信息就必须启用它。恢复能力本身有范围限制：目前只对绑定特定支付伙伴的 UPI ID 用户开放。另有一条容易混的边界：`Your two-step verification PIN is different from your UPI PIN .`——两步验证的 PIN 与 UPI PIN 不是同一个东西。

这一页不写任何商户计费规则；它出现"支付"二字，但讨论的是用户钱包的访问与恢复，不是平台账单的计价。

## 15.6 条款层：计费句在哪里

《WhatsApp Terms for WhatsApp Business Platform》与《WhatsApp Terms for WhatsApp Business App》是这批页面里唯一的法律文本。平台条款整页没有一句费率或计费规则——它写的是使用前提、数据与责任、以及条款变更；商业 App 条款只留了一句与费用有关的责任：`You are responsible for all carrier data plans, Internet fees, and other fees and taxes associated with your use of our Business App Services.`——运营商数据套餐、网络费与相关税费由商户自负。

条款层的空白要登记成空白，不能读成"不计费"：计费规则在第 2—13 章的计费页里，条款页只负责把"谁承担平台之外的费用"说清。这两层的分工，也是本篇不把条款句当计费依据的原因。

# 16 结论与易错点

把前十五章收成一句：**WhatsApp 商业消息的计费是一个"单位 × 类别 × 市场 × 时点"的四维规则，免费与例外挂在账户、号码、窗口与额度四种对象上，账单再叠一层币种、开票实体与责任链。** 任何把这四维压成一维的读法，都会在某一类消息上算错。

本篇认为最容易出错的十处，按章序列在这里：

1. **把"发出"当"送达"**。计费只在送达时发生（2.1）；所有"免费""额度""窗口"的写法，最终都要回答"这条消息有没有被送达"。
2. **把会话计费当现行口径**。会话计费页仍在线上，但它自己标注只服务历史账单（3.1）；现价是按条计（2.2）。
3. **把免费额度当无限免费**。额度挂在每个电话号码上、按月重置、不结转，第一千零一条起计费（4.2）；没有支付方式时额度外不投递（4.3）。
4. **把"窗口内不额外计费"当"不计入阶梯"**。旧口径页写前者，现行页写后者（6.2）；两个命题不等价。
5. **把免费入口点窗口与客服窗口当同一扇门**。一个按会话计时、由广告进入触发，一个跟着用户来信走；窗口内发非模板消息仍要客服窗口开着（6.4）。
6. **把 token 计费当第二条消息费**。Meta Business Agent 仍是一条消息一笔账，但计价基础是 token；阶梯与免费额度都不适用（第 5 章）。
7. **把"计价单位"当"计价对象"**。群组按每个送达收件人计（12.1），多方案会话按实际发送所用的消息账户计（12.3）；"一条消息"在账单上未必是一笔。
8. **把跨档规则套错产品面**。通话整通取低档，消息按当条所在档（12.2）；两者方向相反。
9. **把商业 App 的五天窗套到平台侧**。平台侧送达即计费，商业 App 侧按发出后五天内送达累计（15.1）；两个时点不互换。
10. **把条款空白读成不计费**。平台条款不写费率（15.6）；计费规则只在计费页里。

一句话的收尾：**读这套文本，先问"这条消息属于哪一类、发往哪个市场、在哪个时点被送达、挂在哪个账户上"，再问"它落在哪一种免费或例外里"；两问都有官方原句可指，才算读对。**

# 附表 A 官方页面登记清单与本篇引用位置（34 行）

登记口径与系列前篇一致：**凡本篇抓取的页面，一律登记**。全表 **33** 个登记位，按站点分为 Meta 开发者文档 15、WhatsApp 帮助中心 12、Meta 商业帮助中心 4、法律与政策页 2。取回状态由脚本实测各层正文的非空白字符数得出：本轮登记页正文全部取回（最短一页 1,216 字符、最长一页 47,671 字符），没有需要标注「全篇不引用」的页面，也没有登记两个地址指向同一份正文的情形。

帮助中心页的正文取回长度按裁剪后正文计：抓取原始页含平台页签（Web／Windows／Mac／iOS／Android）与固定尾栏，这些行不计入长度，也不作为引文出处。Meta 商业帮助中心的页面正文从页面内嵌的转义 JSON 载荷里抽取，抽取残骸（单独的 u 记号与 DOM 类名）整行剔除，不做猜测修补，被剔除的行也不作为引文出处。

第 4 列「本篇引用位置」的小节号是实测的，不是事先规划的：由 _tmp/wa6_postitle.pl 扫成稿分片里的页名标记（正文每点名一次某页的官方标题就留下一枚这种内部标记，展开后成为标题）得出，写进 _tmp/w6_a6_postitle.tsv，再由 _tmp/wa6_pos.pl 拼进这一列；判据与 _tmp/wa6_cover.pl 同口径——**这一列只写标题点名的位置，所以按小节号翻过去一定能撞见这一页的官方标题**。同一页在多个小节被点名时，各小节全部列出；某页只在别处被整句引用而标题未点名的节不出现在这一列，那种句子按附表 C—F 的句序回查。


| 站点 | 页面标识 | 官方标题 | 本篇引用位置 | 正文状态 |
| --- | --- | --- | --- | --- |
| Meta 开发者文档 | `calling_pricing_md` | Calling API Pricing | 正文 12.2、附表 B（1 处）、附表 C（8 处） | 已取回（正文 6194 字符） |
| Meta 开发者文档 | `groups_pricing_md` | Groups API Pricing | 正文 12.1、附表 B（11 处）、附表 C（19 处）、附表 D（3 处）、附表 E（2 处）、附表 F（3 处） | 已取回（正文 12517 字符） |
| Meta 开发者文档 | `marketing_messages_pricing_enroll_max_price_md` | Enroll in the max price feature | 正文 1.3、11.2、附表 C（2 处） | 已取回（正文 3662 字符） |
| Meta 开发者文档 | `marketing_messages_pricing_md` | Set a max price for marketing messages (BETA) | 正文 11.1、附表 B（13 处）、附表 C（7 处）、附表 E（4 处） | 已取回（正文 26261 字符） |
| Meta 开发者文档 | `marketing_messages_route_template_messages_md` | Route template messages by category | 正文 11.3、附表 B（2 处）、附表 E（1 处） | 已取回（正文 2445 字符） |
| Meta 开发者文档 | `pricing_ai_providers_md` | New pricing policy for AI Providers leveraging the WhatsApp Business Platform | 正文 4.5、附表 B（16 处）、附表 C（1 处）、附表 E（1 处） | 已取回（正文 5203 字符） |
| Meta 开发者文档 | `pricing_authentication_international_rates_md` | Authentication-international rates | 正文 10.1、附表 B（4 处）、附表 C（2 处）、附表 E（1 处）、附表 F（2 处） | 已取回（正文 14894 字符） |
| Meta 开发者文档 | `pricing_change_billing_currency_md` | Change billing currency via API | 正文 9.3、附表 C（6 处）、附表 E（2 处）、附表 F（2 处） | 已取回（正文 13272 字符） |
| Meta 开发者文档 | `pricing_conversation_based_pricing_md` | Conversation-based pricing (Deprecated) | 正文 3.1、附表 B（9 处）、附表 C（5 处）、附表 E（1 处）、附表 F（1 处） | 已取回（正文 16270 字符） |
| Meta 开发者文档 | `pricing_md` | Pricing on the WhatsApp Business Platform | 正文 1.1、2.1、附表 B（37 处）、附表 C（8 处）、附表 E（10 处）、附表 F（11 处） | 已取回（正文 47671 字符） |
| Meta 开发者文档 | `pricing_non_template_messages_md` | Upcoming pricing updates for Meta Business Agent, service and utility messages | 正文 4.1、附表 B（13 处）、附表 C（1 处）、附表 E（5 处）、附表 F（2 处） | 已取回（正文 16685 字符） |
| Meta 开发者文档 | `solution_providers_multi_solution_conversations_md` | How to use Multi-Solution Conversations (MSC) | 正文 12.3、附表 B（5 处） | 已取回（正文 13878 字符） |
| Meta 开发者文档 | `solution_providers_share_and_revoke_credit_lines_md` | Managing credit lines | 正文 13.2、附表 C（3 处）、附表 D（1 处）、附表 E（1 处）、附表 F（1 处） | 已取回（正文 14868 字符） |
| Meta 开发者文档 | `webhooks_reference_payment_configuration_update_md` | payment_configuration_update webhook reference | 正文 1.3、14.3、附表 D（2 处）、附表 E（1 处） | 已取回（正文 3989 字符） |
| Meta 开发者文档 | `webhooks_reference_template_category_update_md` | template_category_update webhook reference | 正文 1.3、14.3 | 已取回（正文 4198 字符） |
| WhatsApp 帮助中心 | `faq_1022626499062292` | About pricing for marketing messages | 正文 15.1、附表 C（12 处）、附表 E（4 处）、附表 F（1 处） | 已取回（正文 2284 字符） |
| WhatsApp 帮助中心 | `faq_1356785542323967` | About pricing for business broadcasts | 正文 15.1、附表 C（3 处）、附表 E（2 处）、附表 F（1 处） | 已取回（正文 1614 字符） |
| WhatsApp 帮助中心 | `faq_1798313690615821` | About payments for Meta Verified for Business on WhatsApp | 正文 15.3、附表 B（1 处）、附表 C（6 处）、附表 E（1 处）、附表 F（2 处） | 已取回（正文 2412 字符） |
| WhatsApp 帮助中心 | `faq_1800502557189664` | How to pay for ads and business broadcasts on the WhatsApp Business App | 正文 15.2、附表 C（7 处）、附表 E（1 处） | 已取回（正文 3605 字符） |
| WhatsApp 帮助中心 | `faq_2112106596314330` | About using Meta Business Agent with Meta One | 正文 15.4、附表 B（1 处）、附表 C（2 处） | 已取回（正文 4386 字符） |
| WhatsApp 帮助中心 | `faq_26947359861568637` | About Meta One billing and payments | 正文 15.3、附表 B（2 处）、附表 C（5 处）、附表 E（3 处）、附表 F（1 处） | 已取回（正文 3706 字符） |
| WhatsApp 帮助中心 | `faq_352796424225584` | About refunds for Meta Verified for Business on WhatsApp | 正文 15.3、附表 E（3 处）、附表 F（1 处） | 已取回（正文 1762 字符） |
| WhatsApp 帮助中心 | `faq_570949191470325` | How to pay for ads and marketing messages on the WhatsApp Business App | 正文 15.2、附表 E（2 处）、附表 F（1 处） | 已取回（正文 3778 字符） |
| WhatsApp 帮助中心 | `faq_580707210518585` | About two-step verification and payments | 正文 15.5、附表 B（3 处） | 已取回（正文 1788 字符） |
| WhatsApp 帮助中心 | `faq_746389017011161` | Can’t make payment for Meta Verified for Business on WhatsApp | 正文 15.3、附表 E（1 处） | 已取回（正文 2048 字符） |
| WhatsApp 帮助中心 | `faq_7962318943852952` | About admin permissions for subscribing to Meta Verified or Meta One for businesses on WhatsApp | 正文 15.3、附表 C（1 处） | 已取回（正文 1216 字符） |
| WhatsApp 帮助中心 | `faq_975910528178219` | About Meta One plans | 正文 15.4、附表 B（1 处）、附表 C（1 处）、附表 F（1 处） | 已取回（正文 5375 字符） |
| Meta 商业帮助中心 | `bh_1684730811624773` | How to Apply for A Credit Line for Your WhatsApp Business Account | 正文 13.2、附表 C（8 处）、附表 F（1 处） | 已取回（正文 1446 字符） |
| Meta 商业帮助中心 | `bh_1723059664805181` | Billing and payments for Meta Verified business subscriptions | 正文 15.3、附表 B（4 处）、附表 C（9 处）、附表 F（2 处） | 已取回（正文 3305 字符） |
| Meta 商业帮助中心 | `bh_2225184664363779` | About billing for your WhatsApp Business Account | 正文 13.1、附表 B（1 处）、附表 C（15 处）、附表 E（3 处）、附表 F（3 处） | 已取回（正文 3122 字符） |
| Meta 商业帮助中心 | `bh_488291839463771` | How to add a credit card to your Messaging account | 正文 13.1、附表 C（1 处）、附表 E（1 处）、附表 F（1 处） | 已取回（正文 1508 字符） |
| 法律与政策页 | `legal_bapp_terms` | WhatsApp Terms for WhatsApp Business App | 正文 15.6、附表 B（2 处）、附表 C（13 处）、附表 E（1 处） | 已取回（正文 31338 字符） |
| 法律与政策页 | `legal_bplat_terms` | WhatsApp Terms for WhatsApp Business Platform | 正文 15.6、附表 B（2 处） | 已取回（正文 15418 字符） |
| **合计** | **33** 个登记位 | 可引用 **33** 页／不可引用 0 页 | Meta 开发者文档 15、WhatsApp 帮助中心 12、Meta 商业帮助中心 4、法律与政策页 2 | 不可引用页全篇不引用 |

#ROWS=33 COLS=5


# 附表 B 数字归档五档（129 行）

归档口径：**本表一行一个「数字＋它数的东西＋出处页与句序」**，数字、单位词与整句都由脚本从语料里实测取出——输入是原句池的 **768** 条整句（覆盖 **32** 个页面，池子已剔除含竖线的「正文黏表格」混合句），脚本在每句里找出所有独立数字，取该数字后面最多四个词，档位由这四个词里第一个能对上单位表的词决定；同页同数字同单位只留一行（共去重 **74** 行）。全表 **128** 行，分布在 **19** 个页面上，去重后的「数字＋单位」组合 **95** 组，单位词 **33** 种。五档分布：一、账号、号码与设备规模档位 44／三、时长与滚动窗口 17／二、验证要素与文本长度规格 1／五、速率、次数与比率口径 28／四、生效日期与版本节点 38。

三条不成文规矩在这一表里最要紧：**同一个数字在两个页面各写一处，就各登记一行**，不合并成区间；**官方写成区间、多档或带限定词的照原样登记**（`up to`、`about`、`approximately`、`in some countries` 一类措辞原样保留），不取中值、不去掉约数；**档位与性质由脚本按关键词判定，性质判不出来就中止出表**，所以本表没有「其它」这一档。

本表也写清了自己漏掉了什么：池子里 **612** 句整句不含数字，脚本共扫出 **36** 个「数字后面四个词都对不上任何一档」的实例，全部连同它所在的整句写进 `_tmp/w6_a6_bdrop.tsv` 供复核，不进表；这一批绝大多数是地址、法条编号与接口示例对象 ID，不是官方对某项业务写下的数字，**漏掉的判定权在读者手里，不在这张表里**。另有 **1** 个可引用页面在池子里一句都没有——它们的正文是纯参数表，官方数字要进本表只能连同表格 markup 一起取，那类混合句已在建池时剔除，所以这几页在本表里是空的，在附表 A 已逐页点名。

官方原句超过 118 字符按整词截断并以省略号结尾，共 **95** 行截断；截断只发生在表格里，进表的每一句都先用未截断的整句逐字回表到`_tmp/w6_a6s/` 的对应页面，失败 **0** 句（回表与建池共用同一套规范化，所以这里一旦非零就是池子与抓取件不同步，脚本直接中止不出表）。性质判定为 **11** 种：上限（官方写成 up to／maximum 一类） 14／下限或触发门槛 11／位数或口令长度 3／时长（官方只写数值与单位） 8／月份与号数写明的日期节点 8／次数与重试 1／版本号或生效日期 31／申诉与复核时长 6／自动解除或自动生效的时限 5／规模计数 24／速率与吞吐 17。

| 档 | 数字 | 计数单位（官方原文） | 官方原句 | 性质 | 出处页 |
|---|---|---|---|---|---|
| 四、生效日期与版本节点 | **24** | date | 「You will be charged on the same date as your original purchase, until you cancel. (Example: If you purchased your…」 | 版本号或生效日期 | `bh_1723059664805181`｜第 8 句 |
| 三、时长与滚动窗口 | **60** | days | 「Refunds may take several business days, and in some cases up to 60 days, depending on your bank or payment provider.」 | 上限（官方写成 up to／maximum 一类） | `bh_1723059664805181`｜第 20 句 |
| 三、时长与滚动窗口 | **12** | month | 「Offers and discounts We currently offer the following discounts for Meta Verified business subscriptions: - Welcome…」 | 时长（官方只写数值与单位） | `bh_1723059664805181`｜第 22 句 |
| 三、时长与滚动窗口 | **24** | hours | 「To avoid being charged for another billing cycle, you need to cancel your subscription at least 24 hours before your…」 | 下限或触发门槛 | `bh_1723059664805181`｜第 25 句 |
| 一、账号、号码与设备规模档位 | **24** | customer | 「Businesses can respond with service messages within the 24-hour customer service window at no charge.」 | 申诉与复核时长 | `bh_2225184664363779`｜第 15 句 |
| 五、速率、次数与比率口径 | **250,000** | call | 「For calls that cross pricing tiers (for example from the 0 - 50,000 tier to the 50,001 - 250,000 tier), the entire…」 | 速率与吞吐 | `calling_pricing_md`｜第 3 句 |
| 四、生效日期与版本节点 | **24** | date | 「For example: If you purchased your subscription on January 24th, you will be charged again on February 24th, March…」 | 月份与号数写明的日期节点 | `faq_1798313690615821`｜第 10 句 |
| 一、账号、号码与设备规模档位 | **7** | business | 「If you reach your free limit on the WhatsApp Business app, you can subscribe to a Meta One plan to get more access to…」 | 下限或触发门槛 | `faq_2112106596314330`｜第 7 句 |
| 三、时长与滚动窗口 | **30** | day | 「If your billing date falls on a day that doesn't exist in a given month (for example, the 31st in a 30-day month),…」 | 时长（官方只写数值与单位） | `faq_26947359861568637`｜第 10 句 |
| 三、时长与滚动窗口 | **24** | hours | 「To avoid being charged, cancel at least 24 hours before the promotion ends.」 | 下限或触发门槛 | `faq_26947359861568637`｜第 31 句 |
| 二、验证要素与文本长度规格 | **6** | digit | 「WhatsApp for Business About two-step verification and payments Copy link WhatsApp is upgrading the 6-digit PIN used…」 | 位数或口令长度 | `faq_580707210518585`｜第 1 句 |
| 一、账号、号码与设备规模档位 | **4** | number | 「Your UPI PIN is a 4 or 6 digit number you must enter before making each payment.」 | 位数或口令长度 | `faq_580707210518585`｜第 13 句 |
| 一、账号、号码与设备规模档位 | **6** | number | 「Your UPI PIN is a 4 or 6 digit number you must enter before making each payment.」 | 位数或口令长度 | `faq_580707210518585`｜第 13 句 |
| 一、账号、号码与设备规模档位 | **7** | business | 「More AI usage across eligible Meta technologies Meta Business Agent : Get even more access to 24/7 responses from…」 | 版本号或生效日期 | `faq_975910528178219`｜第 35 句 |
| 一、账号、号码与设备规模档位 | **5** | users | 「However, **you are charged each time a billable message is delivered to someone in the group.** For example, if you…」 | 速率与吞吐 | `groups_pricing_md`｜第 2 句 |
| 五、速率、次数与比率口径 | **5** | messages | 「However, **you are charged each time a billable message is delivered to someone in the group.** For example, if you…」 | 速率与吞吐 | `groups_pricing_md`｜第 2 句 |
| 一、账号、号码与设备规模档位 | **4** | users | 「If the message was delivered to only 4 of the 5 users, you would only be charged for the 4 delivered messages. ## How…」 | 规模计数 | `groups_pricing_md`｜第 3 句 |
| 五、速率、次数与比率口径 | **4** | messages | 「If the message was delivered to only 4 of the 5 users, you would only be charged for the 4 delivered messages. ## How…」 | 规模计数 | `groups_pricing_md`｜第 3 句 |
| 四、生效日期与版本节点 | **30** | date | 「Through September 30, 2026, this allows you to send utility and non-template messages for free.」 | 月份与号数写明的日期节点 | `groups_pricing_md`｜第 5 句 |
| 四、生效日期与版本节点 | **2026** | year | 「Through September 30, 2026, this allows you to send utility and non-template messages for free.」 | 月份与号数写明的日期节点 | `groups_pricing_md`｜第 5 句 |
| 五、速率、次数与比率口径 | **1,000** | message | 「The 1,000-message free tier is shared with 1:1 service messages for the same business phone number, and each group…」 | 规模计数 | `groups_pricing_md`｜第 7 句 |
| 五、速率、次数与比率口径 | **1** | messages | 「The 1,000-message free tier is shared with 1:1 service messages for the same business phone number, and each group…」 | 规模计数 | `groups_pricing_md`｜第 7 句 |
| 一、账号、号码与设备规模档位 | **30** | group | 「Values can be: * `GROUP_MARKETING`: Group messages charged the marketing rate. * `GROUP_SERVICE`: Through September…」 | 申诉与复核时长 | `groups_pricing_md`｜第 28 句 |
| 一、账号、号码与设备规模档位 | **2026** | group | 「Values can be: * `GROUP_MARKETING`: Group messages charged the marketing rate. * `GROUP_SERVICE`: Through September…」 | 申诉与复核时长 | `groups_pricing_md`｜第 28 句 |
| 一、账号、号码与设备规模档位 | **1** | group | 「As of October 1, 2026, group non-template messages. * `GROUP_UTILITY`: Through September 30, 2026, group messages…」 | 速率与吞吐 | `groups_pricing_md`｜第 29 句 |
| 四、生效日期与版本节点 | **2019** | year | 「In the EU or UK, you can submit a complaint regarding any alleged breach by us of these Terms or the EU Regulation…」 | 版本号或生效日期 | `legal_bapp_terms`｜第 85 句 |
| 三、时长与滚动窗口 | **90** | days | 「Upon termination, we will remove your account profile from WhatsApp, and retain data associated with your account for…」 | 上限（官方写成 up to／maximum 一类） | `legal_bapp_terms`｜第 132 句 |
| 四、生效日期与版本节点 | **2019** | year | 「In the EU or UK, you can submit a complaint regarding any alleged breach by us of the EU Regulation 2019/1150 (the…」 | 版本号或生效日期 | `legal_bplat_terms`｜第 27 句 |
| 三、时长与滚动窗口 | **90** | days | 「Upon termination, we will retain data associated with your account for up to 90 days, including data you have…」 | 上限（官方写成 up to／maximum 一类） | `legal_bplat_terms`｜第 49 句 |
| 四、生效日期与版本节点 | **1** | date | 「Max price enters open beta on October 1, 2026.」 | 上限（官方写成 up to／maximum 一类） | `marketing_messages_pricing_md`｜第 2 句 |
| 四、生效日期与版本节点 | **2026** | year | 「In 2026, Meta is introducing new pricing features on the Marketing Messages API for WhatsApp to enable businesses to…」 | 规模计数 | `marketing_messages_pricing_md`｜第 4 句 |
| 五、速率、次数与比率口径 | **1,000** | message | 「The `bid_amount` you set in `optimization_spec` is the most Meta charges per 1,000 message deliveries, and…」 | 规模计数 | `marketing_messages_pricing_md`｜第 10 句 |
| 一、账号、号码与设备规模档位 | **15** | partner | 「Limited Beta starting **May 15, 2026** -- Any partner and any directly integrated business can integrate and use the…」 | 上限（官方写成 up to／maximum 一类） | `marketing_messages_pricing_md`｜第 13 句 |
| 一、账号、号码与设备规模档位 | **2026** | partner | 「Limited Beta starting **May 15, 2026** -- Any partner and any directly integrated business can integrate and use the…」 | 上限（官方写成 up to／maximum 一类） | `marketing_messages_pricing_md`｜第 13 句 |
| 一、账号、号码与设备规模档位 | **100,000** | apply | 「For example, if you set a template's `bid_amount` to 100,000 and apply a multiplier of 0.5 to a message, the…」 | 上限（官方写成 up to／maximum 一类） | `marketing_messages_pricing_md`｜第 19 句 |
| 五、速率、次数与比率口径 | **0.5** | message | 「For example, if you set a template's `bid_amount` to 100,000 and apply a multiplier of 0.5 to a message, the…」 | 上限（官方写成 up to／maximum 一类） | `marketing_messages_pricing_md`｜第 19 句 |
| 一、账号、号码与设备规模档位 | **3166** | country | 「Keys are ISO 3166-1 alpha-2 country codes; each value is the multiplier applied to `bid_amount` for sends to that…」 | 规模计数 | `marketing_messages_pricing_md`｜第 27 句 |
| 一、账号、号码与设备规模档位 | **1** | country | 「Keys are ISO 3166-1 alpha-2 country codes; each value is the multiplier applied to `bid_amount` for sends to that…」 | 规模计数 | `marketing_messages_pricing_md`｜第 27 句 |
| 四、生效日期与版本节点 | **31** | date | 「Updates sent with `bid_spec` continue to be accepted until July 31, 2026, after which only `optimization_spec` is…」 | 自动解除或自动生效的时限 | `marketing_messages_pricing_md`｜第 39 句 |
| 三、时长与滚动窗口 | **1** | day | 「Content edits still follow the existing limit of 1 per day and 10 per 30 days. - **Rejected or paused templates**:…」 | 上限（官方写成 up to／maximum 一类） | `marketing_messages_pricing_md`｜第 41 句 |
| 三、时长与滚动窗口 | **10** | days | 「Content edits still follow the existing limit of 1 per day and 10 per 30 days. - **Rejected or paused templates**:…」 | 上限（官方写成 up to／maximum 一类） | `marketing_messages_pricing_md`｜第 41 句 |
| 三、时长与滚动窗口 | **30** | days | 「Content edits still follow the existing limit of 1 per day and 10 per 30 days. - **Rejected or paused templates**:…」 | 上限（官方写成 up to／maximum 一类） | `marketing_messages_pricing_md`｜第 41 句 |
| 五、速率、次数与比率口径 | **2** | template | 「Cache the mapping for fast access. 2. **Add every new template.** Create or update its entry when the category is…」 | 规模计数 | `marketing_messages_route_template_messages_md`｜第 6 句 |
| 五、速率、次数与比率口径 | **4** | request | 「Treat an impending notification as monitoring information; do not route on `correct_category`. 4.…」 | 次数与重试 | `marketing_messages_route_template_messages_md`｜第 9 句 |
| 一、账号、号码与设备规模档位 | **16** | applies | 「This policy took effect February 16, 2026 and applies to markets as outlined below.」 | 月份与号数写明的日期节点 | `pricing_ai_providers_md`｜第 7 句 |
| 一、账号、号码与设备规模档位 | **2026** | applies | 「This policy took effect February 16, 2026 and applies to markets as outlined below.」 | 月份与号数写明的日期节点 | `pricing_ai_providers_md`｜第 7 句 |
| 四、生效日期与版本节点 | **15** | date | 「Charges are for: * Each service message (`<PRICING_CATEGORY>` : `service`) * Delivered from an "AI Provider" * To a…」 | 版本号或生效日期 | `pricing_ai_providers_md`｜第 8 句 |
| 四、生效日期与版本节点 | **2026** | year | 「Charges are for: * Each service message (`<PRICING_CATEGORY>` : `service`) * Delivered from an "AI Provider" * To a…」 | 版本号或生效日期 | `pricing_ai_providers_md`｜第 8 句 |
| 四、生效日期与版本节点 | **90** | date | 「Charges are for: * Each service message (`<PRICING_CATEGORY>` : `service`) * Delivered from an "AI Provider" * To a…」 | 版本号或生效日期 | `pricing_ai_providers_md`｜第 8 句 |
| 四、生效日期与版本节点 | **11** | date | 「Effective **March 11, 2026** - Brazil (+55) - AI Providers can send service messages to users in this market.」 | 版本号或生效日期 | `pricing_ai_providers_md`｜第 10 句 |
| 四、生效日期与版本节点 | **55** | date | 「Effective **March 11, 2026** - Brazil (+55) - AI Providers can send service messages to users in this market.」 | 版本号或生效日期 | `pricing_ai_providers_md`｜第 10 句 |
| 五、速率、次数与比率口径 | **12** | per | 「From **March 11, 2026 to May 12, 2026** Meta charged per the rates published below.」 | 版本号或生效日期 | `pricing_ai_providers_md`｜第 13 句 |
| 五、速率、次数与比率口径 | **2026** | per | 「From **March 11, 2026 to May 12, 2026** Meta charged per the rates published below.」 | 版本号或生效日期 | `pricing_ai_providers_md`｜第 13 句 |
| 四、生效日期与版本节点 | **16** | date | 「From **February 16, 2026 to May 12, 2026**, Meta charged per the rates published below.」 | 版本号或生效日期 | `pricing_ai_providers_md`｜第 15 句 |
| 四、生效日期与版本节点 | **13** | date | 「Meta does not charge as of May 13, 2026.」 | 版本号或生效日期 | `pricing_ai_providers_md`｜第 16 句 |
| 五、速率、次数与比率口径 | **3** | messages | 「Three examples: If a user in the respective market sends an AI Provider a prompt, and the AI provider delivers three…」 | 下限或触发门槛 | `pricing_ai_providers_md`｜第 17 句 |
| 三、时长与滚动窗口 | **5** | minutes | 「Three examples: If a user in the respective market sends an AI Provider a prompt, and the AI provider delivers three…」 | 下限或触发门槛 | `pricing_ai_providers_md`｜第 17 句 |
| 四、生效日期与版本节点 | **39** | date | 「Three examples: If a user in the respective market sends an AI Provider a prompt, and the AI provider delivers three…」 | 下限或触发门槛 | `pricing_ai_providers_md`｜第 17 句 |
| 四、生效日期与版本节点 | **3** | date | 「Three examples: If a user in the respective market sends an AI Provider a prompt, and the AI provider delivers three…」 | 下限或触发门槛 | `pricing_ai_providers_md`｜第 17 句 |
| 四、生效日期与版本节点 | **12** | date | 「Three examples: If a user in the respective market sends an AI Provider a prompt, and the AI provider delivers three…」 | 下限或触发门槛 | `pricing_ai_providers_md`｜第 17 句 |
| 三、时长与滚动窗口 | **30** | days | 「Once deemed eligible, we will set your [start times](#start-times) 30 days out for each country that has an…」 | 速率与吞吐 | `pricing_authentication_international_rates_md`｜第 6 句 |
| 四、生效日期与版本节点 | **1** | date | 「It will appear in the Business Manager under the **Primary Business Location** field starting May 1, 2024, if we are…」 | 版本号或生效日期 | `pricing_authentication_international_rates_md`｜第 19 句 |
| 四、生效日期与版本节点 | **2024** | year | 「It will appear in the Business Manager under the **Primary Business Location** field starting May 1, 2024, if we are…」 | 版本号或生效日期 | `pricing_authentication_international_rates_md`｜第 19 句 |
| 一、账号、号码与设备规模档位 | **3** | business | 「This process can take up to 3 business days.」 | 上限（官方写成 up to／maximum 一类） | `pricing_authentication_international_rates_md`｜第 21 句 |
| 三、时长与滚动窗口 | **24** | hour | 「Conversations are 24-hour message threads between you and your customers.」 | 时长（官方只写数值与单位） | `pricing_conversation_based_pricing_md`｜第 6 句 |
| 三、时长与滚动窗口 | **24** | hours | 「If one does not exist, a new **conversation of that category** is opened, lasting 24 hours.」 | 时长（官方只写数值与单位） | `pricing_conversation_based_pricing_md`｜第 16 句 |
| 一、账号、号码与设备规模档位 | **4** | customer | 「No open marketing conversation exists between you and the customer, so a marketing conversation lasting 24 hours is…」 | 时长（官方只写数值与单位） | `pricing_conversation_based_pricing_md`｜第 18 句 |
| 一、账号、号码与设备规模档位 | **25** | customer | 「An open conversation already exists between you and the customer (a marketing conversation in this case), so a…」 | 自动解除或自动生效的时限 | `pricing_conversation_based_pricing_md`｜第 28 句 |
| 一、账号、号码与设备规模档位 | **24** | customer | 「An open conversation already exists between you and the customer (a marketing conversation in this case), so a…」 | 自动解除或自动生效的时限 | `pricing_conversation_based_pricing_md`｜第 28 句 |
| 一、账号、号码与设备规模档位 | **26** | customer | 「No open conversation exists between you and the customer anymore, so a service conversation is opened, lasting 24…」 | 时长（官方只写数值与单位） | `pricing_conversation_based_pricing_md`｜第 29 句 |
| 三、时长与滚动窗口 | **72** | hours | 「Free-entry point conversations last 72 hours. ## Multiple conversations It is possible to have multiple open…」 | 时长（官方只写数值与单位） | `pricing_conversation_based_pricing_md`｜第 32 句 |
| 四、生效日期与版本节点 | **1** | date | 「Starting July 1, 2025, Cloud API marketing rates will apply to messages sent via Marketing Messages API for WhatsApp.」 | 版本号或生效日期 | `pricing_conversation_based_pricing_md`｜第 44 句 |
| 四、生效日期与版本节点 | **2025** | year | 「Starting July 1, 2025, Cloud API marketing rates will apply to messages sent via Marketing Messages API for WhatsApp.」 | 版本号或生效日期 | `pricing_conversation_based_pricing_md`｜第 44 句 |
| 四、生效日期与版本节点 | **2026** | year | 「Full detail on these updates is [below](#effective-october-1-2026). ## How Meta charges for messages…」 | 版本号或生效日期 | `pricing_md`｜第 2 句 |
| 四、生效日期与版本节点 | **1** | date | 「Full detail on these updates is [below](#effective-october-1-2026). ## How Meta charges for messages…」 | 版本号或生效日期 | `pricing_md`｜第 2 句 |
| 四、生效日期与版本节点 | **2025** | year | 「Full detail on these updates is [below](#effective-october-1-2026). ## How Meta charges for messages…」 | 版本号或生效日期 | `pricing_md`｜第 2 句 |
| 五、速率、次数与比率口径 | **1** | per | 「A message from a business to a user only has 1 of the 5 categories and thus can only incur up to 1 charge, per the…」 | 上限（官方写成 up to／maximum 一类） | `pricing_md`｜第 6 句 |
| 一、账号、号码与设备规模档位 | **3** | solution | 「These can be powered by a 3rd-party AI solution or people (like customer service representatives).」 | 规模计数 | `pricing_md`｜第 9 句 |
| 五、速率、次数与比率口径 | **4** | per | 「One message typically consumes 20,000–25,000 tokens, which translates to approximately 4–5 cents (USD) per message. -…」 | 规模计数 | `pricing_md`｜第 16 句 |
| 五、速率、次数与比率口径 | **5** | per | 「One message typically consumes 20,000–25,000 tokens, which translates to approximately 4–5 cents (USD) per message. -…」 | 规模计数 | `pricing_md`｜第 16 句 |
| 五、速率、次数与比率口径 | **5** | messages | 「Since there are 5 delivered messages, there are 5 charges. - Every message has only one charge, per the category of…」 | 规模计数 | `pricing_md`｜第 20 句 |
| 五、速率、次数与比率口径 | **5** | message | 「Since there are 5 delivered messages, there are 5 charges. - Every message has only one charge, per the category of…」 | 规模计数 | `pricing_md`｜第 20 句 |
| 五、速率、次数与比率口径 | **3** | messages | 「The “message category” and “charges incurred” columns always align. - If this had all started with a user messaging a…」 | 版本号或生效日期 | `pricing_md`｜第 21 句 |
| 五、速率、次数与比率口径 | **5** | x | 「If a business delivers a message to a user in a country that is… - Named on the rate card → Meta charges that…」 | 申诉与复核时长 | `pricing_md`｜第 24 句 |
| 五、速率、次数与比率口径 | **1,000** | messages | 「Meta Business Agent messages remain charged. - _For the_ [first 1,000 service messages each…」 | 下限或触发门槛 | `pricing_md`｜第 33 句 |
| 五、速率、次数与比率口径 | **1,001** | message | 「Thus, Meta only charges as of the 1,001st service message for each business phone number.」 | 版本号或生效日期 | `pricing_md`｜第 34 句 |
| 三、时长与滚动窗口 | **1,000** | month | 「As Meta resumes charging for service messages, Meta provides every business phone number with a free tier of 1,000…」 | 时长（官方只写数值与单位） | `pricing_md`｜第 41 句 |
| 四、生效日期与版本节点 | **1,000** | date | 「The free tier resets monthly for each business phone number (for example, 1,000 for October with no roll-over; 1,000…」 | 下限或触发门槛 | `pricing_md`｜第 43 句 |
| 一、账号、号码与设备规模档位 | **12** | account | 「This reset happens at 12am by Messaging account timezone. - If businesses do not have a payment method for their…」 | 自动解除或自动生效的时限 | `pricing_md`｜第 44 句 |
| 一、账号、号码与设备规模档位 | **0** | businesses | 「This rate applies across all of their Messaging accounts. - Tiers reset monthly — At the start of the next month…」 | 速率与吞吐 | `pricing_md`｜第 50 句 |
| 一、账号、号码与设备规模档位 | **2** | business | 「For example, if a business has already reached the Tier 2, the business would be charged Tier 2’s…」 | 速率与吞吐 | `pricing_md`｜第 54 句 |
| 一、账号、号码与设备规模档位 | **3** | business | 「For example, if a business has already reached the Tier 2, the business would be charged Tier 2’s…」 | 速率与吞吐 | `pricing_md`｜第 54 句 |
| 一、账号、号码与设备规模档位 | **3** | accounts | 「For example, if a business has already reached the Tier 2, the business would be charged Tier 2’s…」 | 速率与吞吐 | `pricing_md`｜第 54 句 |
| 一、账号、号码与设备规模档位 | **1** | business | 「For July, the business is already being charged Tier Rate 1._ - The business portfolio will be accruing toward tiers…」 | 下限或触发门槛 | `pricing_md`｜第 57 句 |
| 四、生效日期与版本节点 | **2** | date | 「If that happens, messages for the remainder of July for Messaging account A will be charged Tier Rate 2. - _Example…」 | 速率与吞吐 | `pricing_md`｜第 58 句 |
| 一、账号、号码与设备规模档位 | **4** | business | 「If that happens, messages for the remainder of July for Messaging account A will be charged Tier Rate 2. - _Example…」 | 速率与吞吐 | `pricing_md`｜第 58 句 |
| 一、账号、号码与设备规模档位 | **2** | partners | 「If that happens, messages for the remainder of July for Messaging account A will be charged Tier Rate 2. - _Example…」 | 速率与吞吐 | `pricing_md`｜第 58 句 |
| 五、速率、次数与比率口径 | **2** | messages | 「Provider 1 sends the first B messages in a given month, and provider 2 starts sending messages as of when the…」 | 版本号或生效日期 | `pricing_md`｜第 59 句 |
| 三、时长与滚动窗口 | **24** | hours | 「If the user messages or calls you again before the timer expires, the timer resets to 24 hours.」 | 自动解除或自动生效的时限 | `pricing_md`｜第 64 句 |
| 一、账号、号码与设备规模档位 | **3166** | country | 「The table below shows how Meta maps country calling codes and ISO 3166 Alpha-2 country codes to countries or regions.」 | 规模计数 | `pricing_md`｜第 71 句 |
| 四、生效日期与版本节点 | **30** | date | 「Charges from any BRL Messaging account will be invoiced in BRL by Meta's local entity in Brazil, Facebook Brasil. -…」 | 版本号或生效日期 | `pricing_md`｜第 75 句 |
| 四、生效日期与版本节点 | **2027** | year | 「Charges from any BRL Messaging account will be invoiced in BRL by Meta's local entity in Brazil, Facebook Brasil. -…」 | 版本号或生效日期 | `pricing_md`｜第 75 句 |
| 四、生效日期与版本节点 | **26** | date | 「Oct. ‘26 rate card updates {#oct-2026-rate-card-updates} Meta updated 40 rates, as summarized and in the table below:…」 | 速率与吞吐 | `pricing_md`｜第 86 句 |
| 四、生效日期与版本节点 | **40** | date | 「Oct. ‘26 rate card updates {#oct-2026-rate-card-updates} Meta updated 40 rates, as summarized and in the table below:…」 | 速率与吞吐 | `pricing_md`｜第 86 句 |
| 五、速率、次数与比率口径 | **9** | message | 「Oct. ‘26 rate card updates {#oct-2026-rate-card-updates} Meta updated 40 rates, as summarized and in the table below:…」 | 速率与吞吐 | `pricing_md`｜第 86 句 |
| 一、账号、号码与设备规模档位 | **30** | users | 「Oct. ‘26 rate card updates {#oct-2026-rate-card-updates} Meta updated 40 rates, as summarized and in the table below:…」 | 速率与吞吐 | `pricing_md`｜第 86 句 |
| 一、账号、号码与设备规模档位 | **2026** | users | 「Oct. ‘26 rate card updates {#oct-2026-rate-card-updates} Meta updated 40 rates, as summarized and in the table below:…」 | 速率与吞吐 | `pricing_md`｜第 86 句 |
| 四、生效日期与版本节点 | **31** | date | 「This pricing policy will be in effect through December 31, 2027.」 | 月份与号数写明的日期节点 | `pricing_md`｜第 98 句 |
| 一、账号、号码与设备规模档位 | **23** | field | 「Ingest the new `subtype` field to build a view of which Messaging accounts are eligible. - As of October 23, 2026,…」 | 版本号或生效日期 | `pricing_md`｜第 101 句 |
| 一、账号、号码与设备规模档位 | **2026** | field | 「Ingest the new `subtype` field to build a view of which Messaging accounts are eligible. - As of October 23, 2026,…」 | 版本号或生效日期 | `pricing_md`｜第 101 句 |
| 五、速率、次数与比率口径 | **1** | messages | 「With the launch of Meta Business Agent on July 1, 2026: Non-template messages can be one of two categories -- Service…」 | 月份与号数写明的日期节点 | `pricing_non_template_messages_md`｜第 4 句 |
| 五、速率、次数与比率口径 | **2026** | messages | 「With the launch of Meta Business Agent on July 1, 2026: Non-template messages can be one of two categories -- Service…」 | 月份与号数写明的日期节点 | `pricing_non_template_messages_md`｜第 4 句 |
| 一、账号、号码与设备规模档位 | **3** | solution | 「This is a new message category on our platform. - **Service messages** – Can be powered by a person, like a customer…」 | 规模计数 | `pricing_non_template_messages_md`｜第 5 句 |
| 四、生效日期与版本节点 | **2026** | year | 「However, Meta only charges for input and output tokens, and does not charge for cache tokens. ## Upcoming pricing…」 | 版本号或生效日期 | `pricing_non_template_messages_md`｜第 12 句 |
| 四、生效日期与版本节点 | **1** | date | 「However, Meta only charges for input and output tokens, and does not charge for cache tokens. ## Upcoming pricing…」 | 版本号或生效日期 | `pricing_non_template_messages_md`｜第 12 句 |
| 四、生效日期与版本节点 | **2024** | year | 「These messages have not been charged since November 1, 2024. - **Effective October 1, 2026** - Meta will charge on a…」 | 申诉与复核时长 | `pricing_non_template_messages_md`｜第 14 句 |
| 一、账号、号码与设备规模档位 | **24** | customer | 「These messages have not been charged since November 1, 2024. - **Effective October 1, 2026** - Meta will charge on a…」 | 申诉与复核时长 | `pricing_non_template_messages_md`｜第 14 句 |
| 五、速率、次数与比率口径 | **4** | per | 「One message typically consumes 20,000–25,000 tokens, which translates to approximately 4–5 cents (USD) per message. -…」 | 规模计数 | `pricing_non_template_messages_md`｜第 20 句 |
| 五、速率、次数与比率口径 | **5** | per | 「One message typically consumes 20,000–25,000 tokens, which translates to approximately 4–5 cents (USD) per message. -…」 | 规模计数 | `pricing_non_template_messages_md`｜第 20 句 |
| 五、速率、次数与比率口径 | **1,000** | messages | 「The following table compares the expected cost to send 1,000 AI-powered messages in response to users in Brazil.」 | 规模计数 | `pricing_non_template_messages_md`｜第 32 句 |
| 四、生效日期与版本节点 | **8** | date | 「Available to eligible businesses only. - Starting September 8, 2026 – Credit/debit cards (Visa and Mastercard only).」 | 版本号或生效日期 | `pricing_non_template_messages_md`｜第 45 句 |
| 一、账号、号码与设备规模档位 | **15** | businesses | 「We previously shared this would launch on Sep. 15 for businesses in Brazil; that has been pulled forward to Sep. 8,…」 | 规模计数 | `pricing_non_template_messages_md`｜第 51 句 |
| 四、生效日期与版本节点 | **16** | date | 「These businesses will continue to be invoiced monthly for Meta Business Agent. ### Selecting a currency for your…」 | 版本号或生效日期 | `pricing_non_template_messages_md`｜第 54 句 |
| 一、账号、号码与设备规模档位 | **1** | waba | 「For example, your client has a phone number linked to WABA 1 and then shares WABA 1 with Partner 1.」 | 规模计数 | `solution_providers_multi_solution_conversations_md`｜第 12 句 |
| 一、账号、号码与设备规模档位 | **1** | partner | 「For example, your client has a phone number linked to WABA 1 and then shares WABA 1 with Partner 1.」 | 规模计数 | `solution_providers_multi_solution_conversations_md`｜第 12 句 |
| 一、账号、号码与设备规模档位 | **2** | partner | 「If you have the same phone number linked to WABA 2, you cannot also share WABA 2 with Partner 1.」 | 规模计数 | `solution_providers_multi_solution_conversations_md`｜第 13 句 |
| 一、账号、号码与设备规模档位 | **4** | name | 「Step 4: Create a name for the new WhatsApp Business Account being created.」 | 规模计数 | `solution_providers_multi_solution_conversations_md`｜第 34 句 |
| 一、账号、号码与设备规模档位 | **5** | partner | 「Your client has exceeded the 5 partner maximum for the number. 1.」 | 上限（官方写成 up to／maximum 一类） | `solution_providers_multi_solution_conversations_md`｜第 38 句 |
| **合计** | **128** 行 | 单位词 **33** 种 | 去重后数字＋单位 **95** 组，覆盖 **19** 页 | 性质 **11** 种 | 第四档＝日期与版本节点 |

#ROWS=128 COLS=6


# 附表 C 官方用词与写下它的站点类别（145 行）

取格口径：脚本按第一、二列的 **79** 个词（组）（分属 13 个主题组：A　计费动作与计量单位 10／B　消息类别的名字 7／C　时间窗口与周期 8／D　免费、额度与豁免 6／E　阶梯、聚合与计数 5／F　钱、支付方式与信用额度 8／G　费率、出价与预算 6／H　市场与地域 5／I　账单信号与取值名 5／J　变更、迁移与废止 4／K　订阅、续费与退款 4／L　责任、资格与权限 6／M　与计费相邻的钱款词 5）逐句扫过原句池的 **768** 条整句（覆盖 **32** 个页面），每命中一次就落进「词 × 站点类别」的格子；**一行一个非空格**，全表 **144** 行，各格命中句次相加是 **1110** 次，而去重后真正被任一词命中的句子是 **482** 句（占全池 62.8%）。同一句可以落进多格：本轮一句落进最多格子的是 `groups_pricing_md` 池内序号 28，同时命中 9 个词（组）——「charged（过去式与被动）」、「customer service window」、「marketing」、「non-template」、「rate／rates」、「sent（发出，与送达对照）」、「service」、「template／templates」、「utility」；全池有 273 句同时落进两个以上的格子。所以句次合计 **1110** 大于去重句数 **482**，这两个数各自回答「这一格有多少句」与「整池有多少句被扫中」，不矛盾。

词表本身也是这一篇的一部分：它写在 `_tmp/w6_a6_verb.tsv` 里，脚本不内置任何选题词，换一篇只换词表、判定规则不变。**正则由脚本逐条编译，编译不过就中止出表**，不存在「写错的正则悄悄扫出零句然后当成官方没写过」。

代表句一列取该格在池子里最先出现的一句，取句规则写死在脚本的遍历顺序里，**不是按哪句更清楚来挑**。「格内有句写明数值」只回答这一格有没有句子自带天数、小时、位数、百分比或规模数——有则记「有」，没有就记「无」，不在这里把别的页面或别的站的数字补进来；跨站对撞属于附表 F。

词零命中的情况：79 个词（组）里 **3** 个在整池一句都没出现（「7 days」、「free of charge」（免费）、「deprecated／deprecation」）。零命中不等于官方没有这一项内容，只说明本篇登记的这批页面没有用这个词写过。

本表的英文原句全部逐字回表：768 条整句每一条都在 `_tmp/w6_a6s/` 的对应页面正文里按规范化后的子串核到，失败 **0** 句（判定与建池用同一套实体还原与空白折叠，见 `_tmp/wa6_lib.pl` 的 ck）。官方原句超过 130 字符按整词截断并以省略号结尾，共 **76** 行截断；截断只发生在表格里，回表核验用的是未截断的整句。各站点命中句次合计：Meta 开发者文档 654／WhatsApp 帮助中心 307／Meta 商业帮助中心 100／法律与政策页 49。

| 主题组 | 官方词（组） | 写下它的站点类别 | 命中句次 | 命中页数 | 代表句（该格池内第一句） | 格内有句写明数值 | 出处页｜句序 |
|---|---|---|---|---|---|---|---|
| A　计费动作与计量单位 | charge（动词原形与三人称） | Meta 开发者文档 | **37** | 5 | 「The first pricing feature allows you to…」 | 有 | `marketing_messages_pricing_md`｜第 5 句 |
| A　计费动作与计量单位 | charge（动词原形与三人称） | WhatsApp 帮助中心 | **6** | 3 | 「These charges will be removed automatically.」 | 无 | `faq_1800502557189664`｜第 19 句 |
| A　计费动作与计量单位 | charge（动词原形与三人称） | Meta 商业帮助中心 | **4** | 1 | 「How you're charged We charge on a per-message basis for each message a business sends to a customer.」 | 无 | `bh_2225184664363779`｜第 7 句 |
| A　计费动作与计量单位 | charged（过去式与被动） | Meta 开发者文档 | **19** | 6 | 「However, you are charged each time a billable message is delivered to someone in the group. For example, if you send a…」 | 有 | `groups_pricing_md`｜第 2 句 |
| A　计费动作与计量单位 | charged（过去式与被动） | WhatsApp 帮助中心 | **20** | 7 | 「After the balance exceeds a set amount or you reach your billing date, you’ll be charged for the outstanding balance.」 | 有 | `faq_1022626499062292`｜第 7 句 |
| A　计费动作与计量单位 | charged（过去式与被动） | Meta 商业帮助中心 | **4** | 2 | 「You will be charged on the same date as your original purchase, until you cancel. (Example: If you purchased your subscription on…」 | 有 | `bh_1723059664805181`｜第 8 句 |
| A　计费动作与计量单位 | charging（进行时） | Meta 开发者文档 | **4** | 2 | 「As Meta resumes charging for service messages, Meta provides every business phone number with a free tier of 1,000 service…」 | 无 | `pricing_md`｜第 41 句 |
| A　计费动作与计量单位 | charging（进行时） | WhatsApp 帮助中心 | **2** | 2 | 「We'll deduct from prepaid funds before charging other payment methods.」 | 无 | `faq_1800502557189664`｜第 7 句 |
| A　计费动作与计量单位 | bill／billed／billing | Meta 开发者文档 | **12** | 5 | 「If your business is based in India, however, you will be billed the authentication-international rate, if you meet all of the…」 | 无 | `pricing_authentication_international_rates_md`｜第 3 句 |
| A　计费动作与计量单位 | bill／billed／billing | WhatsApp 帮助中心 | **17** | 6 | 「After the balance exceeds a set amount or you reach your billing date, you’ll be charged for the outstanding balance.」 | 有 | `faq_1022626499062292`｜第 7 句 |
| A　计费动作与计量单位 | bill／billed／billing | Meta 商业帮助中心 | **5** | 2 | 「Set up a credit line To set up a credit line for your WhatsApp Business account: - Go to Credit lines in Billing & payments. -…」 | 有 | `bh_1684730811624773`｜第 9 句 |
| A　计费动作与计量单位 | per message（按条） | Meta 开发者文档 | **13** | 3 | 「The second pricing feature is the reach estimation tool, which helps businesses set the right max price by helping them…」 | 无 | `marketing_messages_pricing_md`｜第 7 句 |
| A　计费动作与计量单位 | per message（按条） | Meta 商业帮助中心 | **4** | 2 | 「The WhatsApp Business Platform uses a per-message pricing model.」 | 无 | `bh_2225184664363779`｜第 3 句 |
| A　计费动作与计量单位 | per conversation（按会话） | Meta 开发者文档 | **1** | 1 | 「Charges are applied per conversation, not per individual message sent or received.」 | 无 | `pricing_conversation_based_pricing_md`｜第 5 句 |
| A　计费动作与计量单位 | delivered（送达） | Meta 开发者文档 | **31** | 8 | 「However, you are charged each time a billable message is delivered to someone in the group. For example, if you send a…」 | 有 | `groups_pricing_md`｜第 2 句 |
| A　计费动作与计量单位 | delivered（送达） | WhatsApp 帮助中心 | **7** | 2 | 「With marketing messages, you only pay for each message that is delivered to a contact within five days of being sent.」 | 无 | `faq_1022626499062292`｜第 5 句 |
| A　计费动作与计量单位 | delivered（送达） | Meta 商业帮助中心 | **2** | 1 | 「When your business messages customers through the WhatsApp Business Platform, you'll be charged per message we deliver.」 | 无 | `bh_2225184664363779`｜第 4 句 |
| A　计费动作与计量单位 | delivered（送达） | 法律与政策页 | **1** | 1 | 「As part of our Business App Services, WhatsApp provides you with aggregated metrics relating to your messaging activity within…」 | 无 | `legal_bapp_terms`｜第 94 句 |
| A　计费动作与计量单位 | sent（发出，与送达对照） | Meta 开发者文档 | **16** | 7 | 「In this and other similar scenarios, the `delivered` webhook is not sent back.」 | 无 | `groups_pricing_md`｜第 11 句 |
| A　计费动作与计量单位 | sent（发出，与送达对照） | WhatsApp 帮助中心 | **2** | 2 | 「With marketing messages, you only pay for each message that is delivered to a contact within five days of being sent.」 | 无 | `faq_1022626499062292`｜第 5 句 |
| A　计费动作与计量单位 | sent（发出，与送达对照） | Meta 商业帮助中心 | **1** | 1 | 「We charge based on: - Who the message is sent to, determined by the country calling code of the recipient's phone number. - The…」 | 无 | `bh_2225184664363779`｜第 9 句 |
| A　计费动作与计量单位 | sent（发出，与送达对照） | 法律与政策页 | **1** | 1 | 「As part of our Business App Services, WhatsApp provides you with aggregated metrics relating to your messaging activity within…」 | 无 | `legal_bapp_terms`｜第 94 句 |
| A　计费动作与计量单位 | fee／fees | WhatsApp 帮助中心 | **2** | 2 | 「You can send Business broadcasts for a fee .」 | 无 | `faq_1356785542323967`｜第 5 句 |
| A　计费动作与计量单位 | fee／fees | 法律与政策页 | **1** | 1 | 「You are responsible for all carrier data plans, Internet fees, and other fees and taxes associated with your use of our Business…」 | 无 | `legal_bapp_terms`｜第 27 句 |
| A　计费动作与计量单位 | cost／costs | Meta 开发者文档 | **9** | 4 | 「The endpoints can provide useful information like cost, counts of completed calls, and average call duration.」 | 无 | `calling_pricing_md`｜第 15 句 |
| A　计费动作与计量单位 | cost／costs | WhatsApp 帮助中心 | **3** | 3 | 「You must have enough funds in your account to cover the cost of the marketing messages.」 | 无 | `faq_1022626499062292`｜第 9 句 |
| B　消息类别的名字 | marketing | Meta 开发者文档 | **20** | 7 | 「However, you are charged each time a billable message is delivered to someone in the group. For example, if you send a…」 | 有 | `groups_pricing_md`｜第 2 句 |
| B　消息类别的名字 | marketing | WhatsApp 帮助中心 | **18** | 4 | 「How to create and send a marketing message How to manage your marketing messages Why is Meta reviewing my marketing messages?」 | 无 | `faq_1022626499062292`｜第 1 句 |
| B　消息类别的名字 | marketing | Meta 商业帮助中心 | **1** | 1 | 「We charge based on: - Who the message is sent to, determined by the country calling code of the recipient's phone number. - The…」 | 无 | `bh_2225184664363779`｜第 9 句 |
| B　消息类别的名字 | marketing | 法律与政策页 | **2** | 2 | 「Company must provide all necessary data disclosures and notices (such as maintaining a privacy policy or labelling marketing…」 | 无 | `legal_bapp_terms`｜第 40 句 |
| B　消息类别的名字 | utility | Meta 开发者文档 | **21** | 5 | 「Through September 30, 2026, this allows you to send utility and non-template messages for free.」 | 有 | `groups_pricing_md`｜第 5 句 |
| B　消息类别的名字 | utility | Meta 商业帮助中心 | **1** | 1 | 「We charge based on: - Who the message is sent to, determined by the country calling code of the recipient's phone number. - The…」 | 无 | `bh_2225184664363779`｜第 9 句 |
| B　消息类别的名字 | authentication | Meta 开发者文档 | **19** | 5 | 「Includes all authentication and marketing template messages, and any utility template messages sent outside of a customer service…」 | 有 | `groups_pricing_md`｜第 34 句 |
| B　消息类别的名字 | authentication | Meta 商业帮助中心 | **1** | 1 | 「We charge based on: - Who the message is sent to, determined by the country calling code of the recipient's phone number. - The…」 | 无 | `bh_2225184664363779`｜第 9 句 |
| B　消息类别的名字 | service | Meta 开发者文档 | **45** | 5 | 「If the message was delivered to only 4 of the 5 users, you would only be charged for the 4 delivered messages. ## How customer…」 | 有 | `groups_pricing_md`｜第 3 句 |
| B　消息类别的名字 | service | WhatsApp 帮助中心 | **4** | 3 | 「Meta Verified for Business Terms of Service , WhatsApp Business Terms of Service , your subscription will be canceled.」 | 无 | `faq_1798313690615821`｜第 18 句 |
| B　消息类别的名字 | service | Meta 商业帮助中心 | **3** | 2 | 「Only Service partners are eligible to apply for a new credit line for WhatsApp Business accounts.」 | 无 | `bh_1684730811624773`｜第 3 句 |
| B　消息类别的名字 | service | 法律与政策页 | **10** | 2 | 「Company should ensure that it can contact its relevant emergency services providers through a mobile phone, fixed-line telephone,…」 | 无 | `legal_bapp_terms`｜第 11 句 |
| B　消息类别的名字 | Meta Business Agent | Meta 开发者文档 | **16** | 2 | 「It starts with the business reaching out with a marketing template; using Meta Business Agent to respond to user questions;…」 | 无 | `pricing_md`｜第 18 句 |
| B　消息类别的名字 | Meta Business Agent | WhatsApp 帮助中心 | **17** | 2 | 「Subscribing to a plan that lists Meta Business Agent doesn’t guarantee access to it.」 | 无 | `faq_2112106596314330`｜第 4 句 |
| B　消息类别的名字 | template／templates | Meta 开发者文档 | **46** | 9 | 「However, you are charged each time a billable message is delivered to someone in the group. For example, if you send a…」 | 有 | `groups_pricing_md`｜第 2 句 |
| B　消息类别的名字 | template／templates | Meta 商业帮助中心 | **2** | 2 | 「Free message types We offer multiple free message types: - Service messages: Service messages areany message type that is not a…」 | 无 | `bh_2225184664363779`｜第 14 句 |
| B　消息类别的名字 | non-template | Meta 开发者文档 | **11** | 4 | 「Through September 30, 2026, this allows you to send utility and non-template messages for free.」 | 无 | `groups_pricing_md`｜第 5 句 |
| C　时间窗口与周期 | customer service window | Meta 开发者文档 | **10** | 4 | 「If the message was delivered to only 4 of the 5 users, you would only be charged for the 4 delivered messages. ## How customer…」 | 有 | `groups_pricing_md`｜第 3 句 |
| C　时间窗口与周期 | customer service window | Meta 商业帮助中心 | **1** | 1 | 「Businesses can respond with service messages within the 24-hour customer service window at no charge.」 | 无 | `bh_2225184664363779`｜第 15 句 |
| C　时间窗口与周期 | free entry point | Meta 开发者文档 | **6** | 2 | 「If you do not respond within 24 hours, a free entry point conversation is not opened and you must use a template to message the…」 | 有 | `pricing_conversation_based_pricing_md`｜第 35 句 |
| C　时间窗口与周期 | 24-hour | Meta 开发者文档 | **11** | 3 | 「Conversations are 24-hour message threads between you and your customers.」 | 有 | `pricing_conversation_based_pricing_md`｜第 6 句 |
| C　时间窗口与周期 | 24-hour | WhatsApp 帮助中心 | **2** | 1 | 「To avoid being charged, cancel at least 24 hours before the promotion ends.」 | 有 | `faq_26947359861568637`｜第 31 句 |
| C　时间窗口与周期 | 24-hour | Meta 商业帮助中心 | **2** | 2 | 「To avoid being charged for another billing cycle, you need to cancel your subscription at least 24 hours before your next payment…」 | 有 | `bh_1723059664805181`｜第 25 句 |
| C　时间窗口与周期 | 72 hours | Meta 开发者文档 | **2** | 1 | 「Free-entry point conversations last 72 hours. ## Multiple conversations It is possible to have multiple open conversations…」 | 有 | `pricing_conversation_based_pricing_md`｜第 32 句 |
| C　时间窗口与周期 | monthly | Meta 开发者文档 | **7** | 2 | 「Meta Business Agent messages remain charged. - _For the_ [first 1,000 service messages each…」 | 无 | `pricing_md`｜第 33 句 |
| C　时间窗口与周期 | monthly | WhatsApp 帮助中心 | **3** | 3 | 「Automatic renewals Meta Verified business subscriptions are charged on a monthly basis and automatically renew every month.」 | 无 | `faq_1798313690615821`｜第 7 句 |
| C　时间窗口与周期 | monthly | Meta 商业帮助中心 | **1** | 1 | 「Set up a credit line To set up a credit line for your WhatsApp Business account: - Go to Credit lines in Billing & payments. -…」 | 无 | `bh_1684730811624773`｜第 9 句 |
| C　时间窗口与周期 | reset／resets | Meta 开发者文档 | **5** | 1 | 「Meta Business Agent messages remain charged. - _For the_ [first 1,000 service messages each…」 | 有 | `pricing_md`｜第 33 句 |
| C　时间窗口与周期 | reset／resets | WhatsApp 帮助中心 | **2** | 2 | 「Your ads and business broadcasts won't run again until you increase, reset, or remove the limit.」 | 无 | `faq_1800502557189664`｜第 22 句 |
| C　时间窗口与周期 | 12am（当地零点） | Meta 开发者文档 | **2** | 1 | 「This reset happens at 12am by Messaging account timezone. - If businesses do not have a payment method for their Messaging…」 | 无 | `pricing_md`｜第 44 句 |
| D　免费、额度与豁免 | free（形容词） | Meta 开发者文档 | **21** | 3 | 「Through September 30, 2026, this allows you to send utility and non-template messages for free.」 | 有 | `groups_pricing_md`｜第 5 句 |
| D　免费、额度与豁免 | free（形容词） | WhatsApp 帮助中心 | **3** | 3 | 「Subscribe to Meta Verified to get free messages and other benefits.」 | 无 | `faq_1356785542323967`｜第 6 句 |
| D　免费、额度与豁免 | free（形容词） | Meta 商业帮助中心 | **1** | 1 | 「Free message types We offer multiple free message types: - Service messages: Service messages areany message type that is not a…」 | 无 | `bh_2225184664363779`｜第 14 句 |
| D　免费、额度与豁免 | no charge | Meta 商业帮助中心 | **1** | 1 | 「Businesses can respond with service messages within the 24-hour customer service window at no charge.」 | 无 | `bh_2225184664363779`｜第 15 句 |
| D　免费、额度与豁免 | exempt／exemption | Meta 开发者文档 | **1** | 1 | 「Service messages that are free because the organization is eligible for this policy have `subtype` set to `paid_exempt`.」 | 无 | `pricing_md`｜第 103 句 |
| D　免费、额度与豁免 | free tier | Meta 开发者文档 | **6** | 2 | 「The 1,000-message free tier is shared with 1:1 service messages for the same business phone number, and each group send consumes…」 | 无 | `groups_pricing_md`｜第 7 句 |
| D　免费、额度与豁免 | does not charge（否定收费） | Meta 开发者文档 | **3** | 2 | 「Meta does not charge for these messages.」 | 无 | `pricing_ai_providers_md`｜第 9 句 |
| E　阶梯、聚合与计数 | volume tier | Meta 开发者文档 | **8** | 3 | 「For calls that cross pricing tiers (for example from the 0 - 50,000 tier to the 50,001 - 250,000 tier), the entire call is priced…」 | 无 | `calling_pricing_md`｜第 3 句 |
| E　阶梯、聚合与计数 | tier／tiers | Meta 开发者文档 | **21** | 4 | 「For calls that cross pricing tiers (for example from the 0 - 50,000 tier to the 50,001 - 250,000 tier), the entire call is priced…」 | 无 | `calling_pricing_md`｜第 3 句 |
| E　阶梯、聚合与计数 | aggregate／accrue | Meta 开发者文档 | **3** | 2 | 「This rate applies across all of their Messaging accounts. - Tiers reset monthly — At the start of the next month (12am Messaging…」 | 无 | `pricing_md`｜第 50 句 |
| E　阶梯、聚合与计数 | aggregate／accrue | 法律与政策页 | **2** | 1 | 「As part of our Business App Services, WhatsApp provides you with aggregated metrics relating to your messaging activity within…」 | 无 | `legal_bapp_terms`｜第 94 句 |
| E　阶梯、聚合与计数 | threshold | Meta 开发者文档 | **1** | 1 | 「Each of the 9 markets keeps the rate of the region it is leaving; no market's per-minute rate or volume tier thresholds increase…」 | 无 | `calling_pricing_md`｜第 7 句 |
| E　阶梯、聚合与计数 | threshold | WhatsApp 帮助中心 | **4** | 2 | 「Set a payment threshold A payment threshold affects how often you're billed.」 | 无 | `faq_1800502557189664`｜第 27 句 |
| E　阶梯、聚合与计数 | count／counts | Meta 开发者文档 | **4** | 3 | 「The endpoints can provide useful information like cost, counts of completed calls, and average call duration.」 | 无 | `calling_pricing_md`｜第 15 句 |
| F　钱、支付方式与信用额度 | payment method | Meta 开发者文档 | **9** | 3 | 「The difference is only in *how* the payment method is attached to the cloned Messaging account: - Line of Credit: Pass the…」 | 无 | `pricing_change_billing_currency_md`｜第 11 句 |
| F　钱、支付方式与信用额度 | payment method | WhatsApp 帮助中心 | **48** | 8 | 「Your payment method is charged for the total number of recipients you choose, and the funds are credited back to your account for…」 | 无 | `faq_1022626499062292`｜第 11 句 |
| F　钱、支付方式与信用额度 | payment method | Meta 商业帮助中心 | **11** | 4 | 「To secure a payment method, you must set up a credit line for your WhatsApp Business account.」 | 无 | `bh_1684730811624773`｜第 4 句 |
| F　钱、支付方式与信用额度 | credit line | Meta 开发者文档 | **9** | 2 | 「Businesses can pay for Meta Business Agent via: - As of August 1, 2026 – Credit line with Meta, to be invoiced monthly.」 | 无 | `pricing_non_template_messages_md`｜第 44 句 |
| F　钱、支付方式与信用额度 | credit line | Meta 商业帮助中心 | **9** | 2 | 「Only Service partners are eligible to apply for a new credit line for WhatsApp Business accounts.」 | 无 | `bh_1684730811624773`｜第 3 句 |
| F　钱、支付方式与信用额度 | credit card | Meta 开发者文档 | **2** | 1 | 「The difference is only in *how* the payment method is attached to the cloned Messaging account: - Line of Credit: Pass the…」 | 无 | `pricing_change_billing_currency_md`｜第 11 句 |
| F　钱、支付方式与信用额度 | credit card | Meta 商业帮助中心 | **8** | 2 | 「Available payment methods depend on your account type: - If you're a developer who is developing for yourself or your…」 | 无 | `bh_2225184664363779`｜第 5 句 |
| F　钱、支付方式与信用额度 | invoice | Meta 开发者文档 | **6** | 4 | 「Your Messaging account's currency and your legal entity's headquarters address together determine which Meta billing entity…」 | 无 | `pricing_change_billing_currency_md`｜第 14 句 |
| F　钱、支付方式与信用额度 | invoice | Meta 商业帮助中心 | **2** | 1 | 「Set up a credit line To set up a credit line for your WhatsApp Business account: - Go to Credit lines in Billing & payments. -…」 | 无 | `bh_1684730811624773`｜第 9 句 |
| F　钱、支付方式与信用额度 | currency | Meta 开发者文档 | **10** | 4 | 「Use this API when you need to: - Change currency: Clone an existing Messaging account into a new Messaging account with a…」 | 无 | `pricing_change_billing_currency_md`｜第 4 句 |
| F　钱、支付方式与信用额度 | currency | WhatsApp 帮助中心 | **4** | 3 | 「Marketing messages is available in all of the currencies listed on this page through USD-based exchange rate conversion.」 | 无 | `faq_1022626499062292`｜第 13 句 |
| F　钱、支付方式与信用额度 | prepaid／stored value | WhatsApp 帮助中心 | **5** | 3 | 「You can also pay for marketing messages through prepaid funds.」 | 无 | `faq_1022626499062292`｜第 8 句 |
| F　钱、支付方式与信用额度 | payment threshold | WhatsApp 帮助中心 | **4** | 2 | 「Set a payment threshold A payment threshold affects how often you're billed.」 | 无 | `faq_1800502557189664`｜第 27 句 |
| F　钱、支付方式与信用额度 | spending limit | WhatsApp 帮助中心 | **12** | 2 | 「Manage your payments Set an account spending limit An account spending limit helps you control your ad and business broadcasts…」 | 无 | `faq_1800502557189664`｜第 20 句 |
| G　费率、出价与预算 | rate／rates | Meta 开发者文档 | **42** | 8 | 「For calls that cross pricing tiers (for example from the 0 - 50,000 tier to the 50,001 - 250,000 tier), the entire call is priced…」 | 有 | `calling_pricing_md`｜第 3 句 |
| G　费率、出价与预算 | rate／rates | WhatsApp 帮助中心 | **3** | 3 | 「Marketing messages is available in all of the currencies listed on this page through USD-based exchange rate conversion.」 | 无 | `faq_1022626499062292`｜第 13 句 |
| G　费率、出价与预算 | rate card | Meta 开发者文档 | **10** | 3 | 「Messages sent during this period may experience reduced performance. ### Remove a max price and return to rate card pricing Set…」 | 无 | `marketing_messages_pricing_md`｜第 43 句 |
| G　费率、出价与预算 | rate card | WhatsApp 帮助中心 | **1** | 1 | 「We use rate cards for the following specified currencies listed below.」 | 无 | `faq_1356785542323967`｜第 13 句 |
| G　费率、出价与预算 | bid／bid_amount | Meta 开发者文档 | **9** | 1 | 「The `bid_amount` you set in `optimization_spec` is the most Meta charges per 1,000 message deliveries, and `bid_strategy` must be…」 | 有 | `marketing_messages_pricing_md`｜第 10 句 |
| G　费率、出价与预算 | max price | Meta 开发者文档 | **14** | 2 | 「Max price access applies only to Messaging accounts that your clients own and that are shared with or managed by your business.」 | 有 | `marketing_messages_pricing_enroll_max_price_md`｜第 3 句 |
| G　费率、出价与预算 | budget | Meta 开发者文档 | **1** | 1 | 「A bid cap is independent of a `daily_budget` or `lifetime_budget`, which cap how much a campaign spends over a day or over its…」 | 无 | `marketing_messages_pricing_md`｜第 11 句 |
| G　费率、出价与预算 | multiplier | Meta 开发者文档 | **5** | 1 | 「For example, if you set a template's `bid_amount` to 100,000 and apply a multiplier of 0.5 to a message, the effective max price…」 | 无 | `marketing_messages_pricing_md`｜第 19 句 |
| H　市场与地域 | market／markets | Meta 开发者文档 | **16** | 4 | 「Each of the 9 markets keeps the rate of the region it is leaving; no market's per-minute rate or volume tier thresholds increase…」 | 有 | `calling_pricing_md`｜第 7 句 |
| H　市场与地域 | market／markets | 法律与政策页 | **2** | 2 | 「Subject to these Business App Terms and applicable law, we may share this information with the other Meta Companies , and we and…」 | 无 | `legal_bapp_terms`｜第 100 句 |
| H　市场与地域 | country／countries | Meta 开发者文档 | **18** | 5 | 「However, you are charged each time a billable message is delivered to someone in the group. For example, if you send a…」 | 有 | `groups_pricing_md`｜第 2 句 |
| H　市场与地域 | country／countries | WhatsApp 帮助中心 | **11** | 5 | 「Pricing for messages is based on the country code of each message recipient.」 | 无 | `faq_1022626499062292`｜第 12 句 |
| H　市场与地域 | country／countries | Meta 商业帮助中心 | **3** | 2 | 「Credit card payment method is available if you're located in one of the supported countries. - If you're a WhatsApp Solution…」 | 无 | `bh_2225184664363779`｜第 6 句 |
| H　市场与地域 | country／countries | 法律与政策页 | **10** | 2 | 「WhatsApp LLC is the contracting entity providing you our Business App Services, unless you are in a country in the European…」 | 无 | `legal_bapp_terms`｜第 6 句 |
| H　市场与地域 | calling code | Meta 开发者文档 | **4** | 3 | 「However, you are charged each time a billable message is delivered to someone in the group. For example, if you send a…」 | 无 | `groups_pricing_md`｜第 2 句 |
| H　市场与地域 | calling code | Meta 商业帮助中心 | **1** | 1 | 「We charge based on: - Who the message is sent to, determined by the country calling code of the recipient's phone number. - The…」 | 无 | `bh_2225184664363779`｜第 9 句 |
| H　市场与地域 | region／regions | Meta 开发者文档 | **7** | 3 | 「Each of the 9 markets keeps the rate of the region it is leaving; no market's per-minute rate or volume tier thresholds increase…」 | 无 | `calling_pricing_md`｜第 7 句 |
| H　市场与地域 | region／regions | WhatsApp 帮助中心 | **1** | 1 | 「The free trial may not be available in all regions.」 | 无 | `faq_26947359861568637`｜第 34 句 |
| H　市场与地域 | region／regions | 法律与政策页 | **4** | 2 | 「WhatsApp LLC is the contracting entity providing you our Business App Services, unless you are in a country in the European…」 | 无 | `legal_bapp_terms`｜第 6 句 |
| H　市场与地域 | Rest of（费率卡区域） | Meta 开发者文档 | **2** | 1 | 「If a business delivers a message to a user in a country that is… - Named on the rate card → Meta charges that market’s rate - In…」 | 无 | `pricing_md`｜第 24 句 |
| H　市场与地域 | Rest of（费率卡区域） | WhatsApp 帮助中心 | **1** | 1 | 「If a benefit isn’t available to you, the rest of your plan and its benefits are unaffected.」 | 无 | `faq_975910528178219`｜第 49 句 |
| I　账单信号与取值名 | analytics | Meta 开发者文档 | **4** | 2 | 「You can use the following endpoint to retrieve analytics for messages sent using Groups API: ```https…」 | 无 | `groups_pricing_md`｜第 21 句 |
| I　账单信号与取值名 | webhook | Meta 开发者文档 | **7** | 3 | 「In this and other similar scenarios, the `delivered` webhook is not sent back.」 | 无 | `groups_pricing_md`｜第 11 句 |
| I　账单信号与取值名 | pricing category | Meta 开发者文档 | **1** | 1 | 「If you send an empty array, you receive results for all pricing categories.」 | 无 | `groups_pricing_md`｜第 27 句 |
| I　账单信号与取值名 | pricing model | Meta 商业帮助中心 | **2** | 2 | 「The WhatsApp Business Platform uses a per-message pricing model.」 | 无 | `bh_2225184664363779`｜第 3 句 |
| I　账单信号与取值名 | pricing_type／pricing type | Meta 开发者文档 | **1** | 1 | 「If you send an empty array, you receive results for all pricing types.」 | 无 | `groups_pricing_md`｜第 31 句 |
| J　变更、迁移与废止 | migrate／migration | Meta 开发者文档 | **15** | 3 | 「Your business phone numbers are not migrated — they belong to the WhatsApp account hierarchy, which does not change. >…」 | 无 | `pricing_change_billing_currency_md`｜第 2 句 |
| J　变更、迁移与废止 | migrate／migration | Meta 商业帮助中心 | **1** | 1 | 「You can view the added credit card in the Settings tab. - Updates to account structure on WhatsApp Business Platform - Migrate…」 | 无 | `bh_488291839463771`｜第 14 句 |
| J　变更、迁移与废止 | replace／replaces | Meta 开发者文档 | **2** | 2 | 「If you replace `to` with `recipient`, the request fails with error code `131062`.」 | 无 | `marketing_messages_pricing_md`｜第 58 句 |
| J　变更、迁移与废止 | update／updated | Meta 开发者文档 | **10** | 6 | 「You can pass an updated `signer_email` to redirect the link.」 | 有 | `marketing_messages_pricing_enroll_max_price_md`｜第 7 句 |
| J　变更、迁移与废止 | update／updated | WhatsApp 帮助中心 | **8** | 5 | 「To edit your business information, tap Business info > update your business information > tap Save .」 | 无 | `faq_1800502557189664`｜第 31 句 |
| J　变更、迁移与废止 | update／updated | Meta 商业帮助中心 | **2** | 2 | 「Update payment method Any changes to your payment method take effect at the end of your current billing cycle.」 | 无 | `bh_1723059664805181`｜第 13 句 |
| J　变更、迁移与废止 | update／updated | 法律与政策页 | **5** | 2 | 「Company will keep its business account information updated.」 | 无 | `legal_bapp_terms`｜第 17 句 |
| K　订阅、续费与退款 | refund | WhatsApp 帮助中心 | **14** | 4 | 「Refunds Refunds for Meta Verified business subscriptions are available if you didn't complete the verification process or if we…」 | 无 | `faq_1798313690615821`｜第 13 句 |
| K　订阅、续费与退款 | refund | Meta 商业帮助中心 | **3** | 1 | 「Refunds Refunds for Meta Verified subscriptions are only available if you meet the eligibility requirements.」 | 有 | `bh_1723059664805181`｜第 18 句 |
| K　订阅、续费与退款 | cancel／cancellation | WhatsApp 帮助中心 | **16** | 4 | 「You will automatically be charged every month from your initial payment date, until you cancel your subscription .」 | 有 | `faq_1798313690615821`｜第 9 句 |
| K　订阅、续费与退款 | cancel／cancellation | Meta 商业帮助中心 | **4** | 1 | 「Automatic renewal Meta Verified business subscriptions are billed on a recurring basis and renew automatically, unless canceled.」 | 有 | `bh_1723059664805181`｜第 7 句 |
| K　订阅、续费与退款 | renew／renewal | WhatsApp 帮助中心 | **4** | 3 | 「Automatic renewals Meta Verified business subscriptions are charged on a monthly basis and automatically renew every month.」 | 无 | `faq_1798313690615821`｜第 7 句 |
| K　订阅、续费与退款 | renew／renewal | Meta 商业帮助中心 | **1** | 1 | 「Automatic renewal Meta Verified business subscriptions are billed on a recurring basis and renew automatically, unless canceled.」 | 无 | `bh_1723059664805181`｜第 7 句 |
| K　订阅、续费与退款 | subscription | WhatsApp 帮助中心 | **40** | 6 | 「You can purchase your Meta Verified subscription with any of the following payment methods.」 | 有 | `faq_1798313690615821`｜第 3 句 |
| K　订阅、续费与退款 | subscription | Meta 商业帮助中心 | **12** | 1 | 「Automatic renewal Meta Verified business subscriptions are billed on a recurring basis and renew automatically, unless canceled.」 | 有 | `bh_1723059664805181`｜第 7 句 |
| L　责任、资格与权限 | liable／liability | Meta 开发者文档 | **2** | 1 | 「This document describes how Solution Partners can share and revoke lines of credit with onboarded clients. Note:…」 | 无 | `solution_providers_share_and_revoke_credit_lines_md`｜第 4 句 |
| L　责任、资格与权限 | liable／liability | 法律与政策页 | **4** | 2 | 「WhatsApp will not be responsible or liable for your use of those services, the third-party’s terms, or any actions you take under…」 | 无 | `legal_bapp_terms`｜第 35 句 |
| L　责任、资格与权限 | responsible | Meta 开发者文档 | **1** | 1 | 「The criteria that determines when a conversation is opened and how it is categorized is explained below. Warning: Businesses…」 | 无 | `pricing_conversation_based_pricing_md`｜第 8 句 |
| L　责任、资格与权限 | responsible | 法律与政策页 | **6** | 2 | 「You are responsible for all carrier data plans, Internet fees, and other fees and taxes associated with your use of our Business…」 | 无 | `legal_bapp_terms`｜第 27 句 |
| L　责任、资格与权限 | eligible／eligibility | Meta 开发者文档 | **13** | 3 | 「Once deemed eligible, we will set your [start times](#start-times) 30 days out for each country that has an…」 | 有 | `pricing_authentication_international_rates_md`｜第 6 句 |
| L　责任、资格与权限 | eligible／eligibility | WhatsApp 帮助中心 | **6** | 3 | 「Eligibility and availability may change.」 | 无 | `faq_26947359861568637`｜第 35 句 |
| L　责任、资格与权限 | eligible／eligibility | Meta 商业帮助中心 | **2** | 2 | 「Only Service partners are eligible to apply for a new credit line for WhatsApp Business accounts.」 | 无 | `bh_1684730811624773`｜第 3 句 |
| L　责任、资格与权限 | admin permission | Meta 开发者文档 | **1** | 1 | 「No additional Business Portfolio Admin permissions or business approvals are required. - If your migration requires Line of…」 | 无 | `pricing_change_billing_currency_md`｜第 22 句 |
| L　责任、资格与权限 | admin permission | WhatsApp 帮助中心 | **4** | 1 | 「If you are prompted to log in to a Facebook or Instagram account when trying to subscribe to Meta Verified or Meta One for…」 | 无 | `faq_7962318943852952`｜第 1 句 |
| L　责任、资格与权限 | Financial Editor | Meta 开发者文档 | **1** | 1 | 「In addition, the system user who the token represents must have granted your app the business_management permission, and must…」 | 无 | `solution_providers_share_and_revoke_credit_lines_md`｜第 10 句 |
| L　责任、资格与权限 | Bill To Party | Meta 开发者文档 | **1** | 1 | 「You are the "Bill To Party" for all businesses sharing your credit line.」 | 无 | `solution_providers_share_and_revoke_credit_lines_md`｜第 6 句 |
| M　与计费相邻的钱款词 | discount | WhatsApp 帮助中心 | **1** | 1 | 「Respond to customer questions about discounts, payment, and shipping.」 | 无 | `faq_2112106596314330`｜第 30 句 |
| M　与计费相邻的钱款词 | discount | Meta 商业帮助中心 | **1** | 1 | 「Offers and discounts We currently offer the following discounts for Meta Verified business subscriptions: - Welcome offer: New…」 | 无 | `bh_1723059664805181`｜第 22 句 |
| M　与计费相邻的钱款词 | tax／taxes | Meta 商业帮助中心 | **1** | 1 | 「If prompted, click Select file to upload your license, registration or a recent tax document. - View Terms & conditions. - Click…」 | 无 | `bh_1684730811624773`｜第 10 句 |
| M　与计费相邻的钱款词 | tax／taxes | 法律与政策页 | **1** | 1 | 「You are responsible for all carrier data plans, Internet fees, and other fees and taxes associated with your use of our Business…」 | 无 | `legal_bapp_terms`｜第 27 句 |
| M　与计费相邻的钱款词 | balance | WhatsApp 帮助中心 | **4** | 2 | 「The balance for each delivered message is accumulated.」 | 无 | `faq_1022626499062292`｜第 6 句 |
| M　与计费相邻的钱款词 | billing cycle | WhatsApp 帮助中心 | **7** | 2 | 「Meta One billing cycle and automatic renewal Meta One renews automatically each month unless you cancel your subscription.」 | 有 | `faq_26947359861568637`｜第 7 句 |
| M　与计费相邻的钱款词 | billing cycle | Meta 商业帮助中心 | **3** | 1 | 「Update payment method Any changes to your payment method take effect at the end of your current billing cycle.」 | 有 | `bh_1723059664805181`｜第 13 句 |
| M　与计费相邻的钱款词 | free trial | WhatsApp 帮助中心 | **1** | 1 | 「The free trial may not be available in all regions.」 | 无 | `faq_26947359861568637`｜第 34 句 |
| **合计** | **79** 个词（组）× **4** 类站点 | 非空格 **144** 个 | 句次 **1110** | 一句可落多格 | 代表句按池内顺序取第一句 | 零命中词 **3** 个 | 回表失败 **0** 句 |

#ROWS=144 COLS=8


# 附表 D 官方写明的取值域全表（7 行）

取组口径：脚本扫过 **33** 个已登记页面（站点类别：Meta 开发者文档）的每一行原文，配置了 10 个取值域标记，本轮真正在这些页面里出现过的有 **5** 个（`Values can be` 5 行／`Supported values are` 1 行／`Possible values` 1 行／`Can be one of` 1 行／`one of the following` 1 行，合计 **9** 行），另有 **5** 个（must be one of／The following values are supported／Valid values are／Options are／Enum Values）在扫过的每一行原文里零命中——这几串字符只是脚本配置表里的名字，本轮官方页面没有一处用过这种写法，所以本篇不给它们加反引号、也不把它们当作任何官方句子的引文。取到值来的页面 **3** 个（站点类别：Meta 开发者文档）。按标记所在行的形态分档取值：自动·参数表说明格 6。全表 **6** 组、共 **20** 个取值、去重后的取值名 **20** 个。其中 **0** 组的取值是不带反引号的纯文本子项——反引号是接口文档的排版，帮助中心同样用「one of the following」加子项写成正式取值域，这一档整项照收、只丢超过 80 字符的说明句；同页同字段同取值的重复写法（帮助中心按平台页签各写一遍）合并为一行，共合并 **0** 处。取值本身带空格的共 **2** 个（如 `LEGAL NAME NOT FOUND IN DOCUMENTS`、`No longer needed`），这一批只能按「反引号紧跟破折号说明」的位置认出，脚本对这种写法额外兜一遍：同一格里见到两个以上这种位置才收，并且只在这一档放开取值不能带空格的限制——接口文档的枚举里确有带空格的大写取值，卡成单词会漏掉整组。

为什么这一表不走原句池：官方把取值域写在接口参数表的说明格里、缩进的子项列表里、整句内联的清单里，这些行要么带竖线、要么是列表行，本来就被池子的入池条件挡在外面。所以 D 的核验独立于 C：每一组把它自己那一行原文、缩进子项式还把每个取值所在的那一行原文，逐字回表到 `_tmp/w6_a6s/` 的对应页面，失败 **0** 行。

落空登记：扫到标记但取不到两个以上取值的共 **3** 处，原因由脚本按同一行的证据判，不由人挑：取值清单交叉引用到别的页面，本页没有写出取值 1／子项是整段说明句或超过 80 字符，官方没写成短取值清单 1／标记句之后没有以反引号或短子项写出的取值 1。**扫到标记不等于官方写明了取值域**，这几处不算进表，但算进这一句，让读者知道漏了多少、为什么漏。这 **3** 处分布在 **3** 个页面，每一处的页面标识、原文行号、判定档位、命中的标记、落空判据与该行未截断的原文全部逐条写进 `_tmp/w6_a6_dmiss.tsv`（与上面的次数由脚本断言相等，不是"约"），可逐条回原文核对。

单组取值上限设为 **24** 个：实测最多的一组有 **6** 个取值，没有一组触及上限，所以本表没有因上限而丢过取值。

取值列表的截断按整取值进行（顿号相连的列表没有空格，按词截会把一整组截到只剩一项）：单元格 **400** 字符内装得下的原样列出，装不下的整项去掉并在行内写明「另有 N 个取值未列出」。本轮实测 **0** 组触及这一上限、被去掉的取值 **0** 个，也就是说表里列出的取值就是脚本收到的全部 **20** 个，没有一组需要读者回原文补齐。

| 组别 | 页面标识 | 行号 | 字段／属性 | 官方标记 | 取值个数 | 官方写明的取值 |
|---|---|---|---|---|---|---|
| 自动·参数表说明格 | `groups_pricing_md` | 181 | `<PRICING_CATEGORIES>` | Values can be | **3** | `GROUP_MARKETING`、`GROUP_SERVICE`、`GROUP_UTILITY` |
| 自动·参数表说明格 | `groups_pricing_md` | 59 | `<PRICING_TYPE>` | Values can be | **2** | `regular`、`free_group_customer_service` |
| 自动·参数表说明格 | `groups_pricing_md` | 182 | `<PRICING_TYPES>` | Values can be | **2** | `FREE_GROUP_CUSTOMER_SERVICE`、`REGULAR` |
| 自动·参数表说明格 | `solution_providers_share_and_revoke_credit_lines_md` | 44 | `<CUSTOMER_BUSINESS_CURRENCY>` | Supported values are | **6** | `AUD`、`EUR`、`GBP`、`IDR`、`INR`、`USD` |
| 自动·参数表说明格 | `webhooks_reference_payment_configuration_update_md` | 38 | `<PAYMENT_CONFIGURATION_STATUS>` | Values can be | **3** | `Active`、`Needs Connecting`、`Needs Testing` |
| 自动·参数表说明格 | `webhooks_reference_payment_configuration_update_md` | 41 | `<PAYMENT_GATEWAY_PROVIDER_NAME>` | Values can be | **4** | `billdesk`、`payu`、`razorpay`、`zaakpay` |
| **合计** | **3** 页 | 行号为原文行序 | 字段名取自官方写法 | 标记 2 种 | 取值 **20** 个 | 去重取值名 **20** 个 |

#ROWS=6 COLS=7


# 附表 E 同一组维度在本轴各主体上的逐列对照（17 行）

取格口径：本表 **16** 个对照维度 × **8** 列 = **128** 格，其中 **51** 格取到了官方原句、**77** 格标作「官方未按这一列写该项」。每一格的取法都是「页面标识＋一条正则」交给脚本在原句池里取句，在该列的页面范围内命中 0 句或命中多句都会让生成脚本中止，所以表里没有一格是人工挑出来的。原句池共 **768** 句（已由附表 B 逐字回表），本表用去重后的页面 **22** 个；同一句在多个维度或多列复用的共 **2** 次，复用时页标识与句序不变。

列的定义写在 `_tmp/w6_a6_ecols.tsv` 里，每列要么按站点类别取页（），要么按人工点名的页清单取页（计费总纲＝1 页／专项计费页＝7 页／营销最高价与路由＝3 页／账单与信用额度＝5 页／接口与信号页＝2 页／商业 App 计费页＝4 页／订阅与账号付费页＝9 页／条款页＝2 页）。空格的含义只有一个：官方在这批登记页面里没按这一列写这一项，**不代表该主体没有这项规则**。

官方原句超过 112 字符按整词截断并以省略号结尾，共 **38** 格截断；截断只发生在显示层，取句与逐字回表用的是未截断的整句。各列取到句子的格数：计费总纲 10／专项计费页 12／营销最高价与路由 5／账单与信用额度 5／接口与信号页 1／商业 App 计费页 9／订阅与账号付费页 8／条款页 1。

| 编号 | 对照维度 | 计费总纲 | 专项计费页 | 营销最高价与路由 | 账单与信用额度 | 接口与信号页 | 商业 App 计费页 | 订阅与账号付费页 | 条款页 |
|---|---|---|---|---|---|---|---|---|---|
| E1   | 计费按什么单位算 | 「Full detail on these updates is [below](#effective-october-1-2026). ## How Meta charges for messages…」（`pricing_md`｜第 2 句） | 「Rates by market and message category vary. - **Meta Business Agent** - Meta charges on a per-message basis,…」（`pricing_non_template_messages_md`｜第 10 句） | 「The `bid_amount` you set in `optimization_spec` is the most Meta charges per 1,000 message deliveries, and…」（`marketing_messages_pricing_md`｜第 10 句） | 「When your business messages customers through the WhatsApp Business Platform, you'll be charged per message we…」（`bh_2225184664363779`｜第 4 句） | 官方未按这一列写该项 | 「Once you add a payment method, you’ll be charged for the total number of recipients you choose, and the funds…」（`faq_1356785542323967`｜第 11 句） | 「If you cancel your subscription after successfully completing the verification process, Meta won’t provide a…」（`faq_352796424225584`｜第 4 句） | 官方未按这一列写该项 |
| E2   | 谁付钱、谁不付钱 | 「Meta does **not** charge for messages from users to businesses. - Businesses are only charged when a message is…」（`pricing_md`｜第 4 句） | 「Any non-template message that is _not_ powered by Meta Business Agent is a service message. ### How Meta…」（`pricing_non_template_messages_md`｜第 7 句） | 官方未按这一列写该项 | 「We charge only when a message is delivered.」（`bh_2225184664363779`｜第 8 句） | 官方未按这一列写该项 | 「Your payment method is charged for the total number of recipients you choose, and the funds are credited back…」（`faq_1022626499062292`｜第 11 句） | 「If your default payment method fails for any reason, your backup payment will be charged to keep your…」（`faq_746389017011161`｜第 8 句） | 官方未按这一列写该项 |
| E3   | 费率由哪两个坐标定 | 「Rates by category vary as well, thus rates vary by market–category. - _Cost per message_: - Utility,…」（`pricing_md`｜第 11 句） | 「However, **you are charged each time a billable message is delivered to someone in the group.** For example, if…」（`groups_pricing_md`｜第 2 句） | 「Keys are ISO 3166-1 alpha-2 country codes; each value is the multiplier applied to `bid_amount` for sends to…」（`marketing_messages_pricing_md`｜第 27 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Pricing for business broadcasts is based on the country code of each message recipient.」（`faq_1356785542323967`｜第 12 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E4   | 这一条什么时候不计费 | 「The “message category” and “charges incurred” columns always align. - If this had all started with a user…」（`pricing_md`｜第 21 句） | 「Meta does not charge for these messages.」（`pricing_ai_providers_md`｜第 9 句） | 「The effective bid is the maximum price Meta uses for that delivery — as with any max price, Meta charges that…」（`marketing_messages_pricing_md`｜第 32 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Your payment method is charged for the total number of recipients you choose, and the funds are credited back…」（`faq_1022626499062292`｜第 11 句） | 「If your account violates the policies listed, your subscription will be canceled and a refund will be…」（`faq_352796424225584`｜第 10 句） | 官方未按这一列写该项 |
| E5   | 免费窗口的长度与起算 | 「If the user messages or calls you again before the timer expires, the timer resets to 24 hours.」（`pricing_md`｜第 64 句） | 「The free entry point conversation is opened as soon as your message is delivered and lasts 72 hours.」（`pricing_conversation_based_pricing_md`｜第 36 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「You can also pay for marketing messages through prepaid funds.」（`faq_1022626499062292`｜第 8 句） | 「To avoid being charged, cancel at least 24 hours before the promotion ends.」（`faq_26947359861568637`｜第 31 句） | 官方未按这一列写该项 |
| E6   | 免费额度与它的归属 | 「Thus, Meta only charges as of the 1,001st service message for each business phone number.」（`pricing_md`｜第 34 句） | 「The 1,000-message free tier is shared with 1:1 service messages for the same business phone number, and each…」（`groups_pricing_md`｜第 7 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E7   | 阶梯对谁生效、按什么周期重置 | 「This rate applies across all of their Messaging accounts. - Tiers reset monthly — At the start of the next…」（`pricing_md`｜第 50 句） | 「Once deemed eligible, we will set your [start times](#start-times) 30 days out for each country that has an…」（`pricing_authentication_international_rates_md`｜第 6 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E8   | 可以拿来付款的东西 | 官方未按这一列写该项 | 「Available to eligible businesses only. - Starting September 8, 2026 – Credit/debit cards (Visa and Mastercard…」（`pricing_non_template_messages_md`｜第 45 句） | 官方未按这一列写该项 | 「You can pay for messaging with a credit card via Visa or Mastercard.」（`bh_488291839463771`｜第 5 句） | 官方未按这一列写该项 | 「Supported payment methods include credit and debit cards, PayPal, a bank account (direct debit) in supported…」（`faq_570949191470325`｜第 10 句） | 「Supported payment methods Use any payment methods supported by the Apple App store and Google Play store to pay…」（`faq_1798313690615821`｜第 6 句） | 官方未按这一列写该项 |
| E9   | 币种与开票实体 | 「Currency selection is part of the Messaging account creation process; once selected, a currency cannot be…」（`pricing_md`｜第 27 句） | 「Your Messaging account's currency and your legal entity's headquarters address together determine which Meta…」（`pricing_change_billing_currency_md`｜第 14 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Marketing messages is available in all of the currencies listed on this page through USD-based exchange rate…」（`faq_1022626499062292`｜第 13 句） | 「Accepted payment methods for Meta One The payment methods you can use for Meta One depend on the store where…」（`faq_26947359861568637`｜第 11 句） | 官方未按这一列写该项 |
| E10  | 账单的周期与结算时点 | 「The free tier resets monthly for each business phone number (for example, 1,000 for October with no roll-over;…」（`pricing_md`｜第 43 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 「You can view the real-time monitoring of messaging and spending for your WhatsApp Business account in the…」（`bh_2225184664363779`｜第 17 句） | 官方未按这一列写该项 | 「If you set an account spending limit, your ads and business broadcasts will pause once you reach that limit.」（`faq_1800502557189664`｜第 21 句） | 「If your billing date falls on a day that doesn't exist in a given month (for example, the 31st in a 30-day…」（`faq_26947359861568637`｜第 10 句） | 官方未按这一列写该项 |
| E11  | 分析里的类别名 | 官方未按这一列写该项 | 「The category and charge for service messages are also included in message delivery webhooks. **Analytics**…」（`pricing_non_template_messages_md`｜第 63 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E12  | webhook 里的类别名与状态名 | 官方未按这一列写该项 | 「The category and charge for service messages are also included in message delivery webhooks. **Analytics**…」（`pricing_non_template_messages_md`｜第 63 句） | 「Then `/messages` rejects marketing templates with error `131063`; refresh the category and retry through…」（`marketing_messages_route_template_messages_md`｜第 12 句） | 官方未按这一列写该项 | 「Values can be: `Active` — Indicates the payment configuration has been tested in WhatsApp Manager and can now…」（`webhooks_reference_payment_configuration_update_md`｜第 3 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E13  | 口径变更时怎么迁 | 「Charges from any BRL Messaging account will be invoiced in BRL by Meta's local entity in Brazil, Facebook…」（`pricing_md`｜第 75 句） | 「Your business phone numbers are not migrated — they belong to the WhatsApp account hierarchy, which does not…」（`pricing_change_billing_currency_md`｜第 2 句） | 「Updates sent with `bid_spec` continue to be accepted until July 31, 2026, after which only `optimization_spec`…」（`marketing_messages_pricing_md`｜第 39 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E14  | 谁对账单负责 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「You are the "Bill To Party" for all businesses sharing your credit line.」（`solution_providers_share_and_revoke_credit_lines_md`｜第 6 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E15  | 退款、取消与续费 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Add, remove, or edit a payment method You don't need a credit or debit card to pay for ads.」（`faq_570949191470325`｜第 9 句） | 「If you cancel your subscription during your current billing cycle, you won’t be eligible for a partial refund.」（`faq_352796424225584`｜第 7 句） | 官方未按这一列写该项 |
| E16  | 通道费用与税款由谁承担 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「You are responsible for all carrier data plans, Internet fees, and other fees and taxes associated with your…」（`legal_bapp_terms`｜第 27 句） |
| **合计** | **16** 个维度 × 8 列 = **128** 格 | 有句 **51** 格 | 空格 **77** 格 | 去重引用页 **22** | 跨格复用句 **2** 次 | 截断 **38** 格 | 计费总纲 10 | 专项计费页 12 | 营销最高价与路由 5 |

#ROWS=16 COLS=10


# 附表 F 口径冲突与官方未写明处（22 行）

本表收录两类东西：官方多处写法不一致、需要读者知道本篇按哪一说走的**冲突**，以及官方在这批登记页面里根本没写、本篇不替它补的**空白**。全表 **21** 个议题：**17** 个两侧都取到了原句、**3** 个只有一侧有原句（另一侧官方没写）、**1** 个两侧都没有原句可引（共 **1** 行）。

两侧皆空的行要说清它空在哪：本轴登记 **33** 页，其中正文未取回 **0** 页——这 **0** 页全篇不引用，已引用 **33** 页（Meta 商业帮助中心 4／Meta 开发者文档 15／WhatsApp 帮助中心 12／法律与政策页 2），所以两侧皆空不是因为抓不到，而是因为**官方在可引用的这批页面里没按这一项写过一句话**；这一类议题一律登记为空白，正文不替官方补结论。

取句方式与附表 E 同一把刀：每个说法登记「页面标识＋一条正则」，脚本在原句池里取句，命中 0 句或多句都会让生成脚本中止；取到的每一句再逐字回表到 `_tmp/w6_a6s/` 的对应页面。本表引用页面去重 **18** 个，同一句在多个议题里复用的共 **0** 次。两侧点同一页、又取到同一句的行由脚本直接中止——同句不构成冲突，不允许拿复制冒充对照。差异性质用了 **21** 种。这 21 条性质各不相同（每种恰好 1 行），不在这里重述，逐条只写在表内「差异性质」列。

「本篇采用」一列写的不是判断谁对谁错，而是本篇正文按哪一说的走、以及为什么。这一列的 **10** 行标为已写进正文、**11** 行只在本附表登记（正文不替官方补结论，也不把两种说法合成一说）。这一列标「已写进正文」的 **10** 行本次已逐句核过成稿：它们要引的 **16** 句官方原句全部原样出现在成稿里（空白折叠后做子串比对），缺任何一句本脚本都会中止，所以这一列不是空头承诺。官方原句超过 130 字符按整词截断并以省略号结尾，共 **19** 处截断；截断只发生在显示层。

| 编号 | 议题 | 说法甲 | 说法乙 | 差异性质 | 本篇采用 | 正文落点 |
|---|---|---|---|---|---|---|
| F1   | 计费单位：按会话还是按消息 | 「Charges are applied per conversation, not per individual message sent or received.」（`pricing_conversation_based_pricing_md`｜第 5 句） | 「When your business messages customers through the WhatsApp Business Platform, you'll be charged per message we deliver.」（`bh_2225184664363779`｜第 4 句） | 时代口径差：会话计费页标注只服务历史账单，平台主页面已改写为按消息计费 | 正文按现行按消息口径写，第 3 章把会话计费页单列为历史口径，两者不合并成一条现行规则。 | 已写进正文 |
| F2   | 服务消息在客服窗口内是否免费 | 「Businesses can respond with service messages within the 24-hour customer service window at no charge.」（`bh_2225184664363779`｜第 15 句） | 「Thus, Meta only charges as of the 1,001st service message for each business phone number.」（`pricing_md`｜第 34 句） | 同一帮助页未标注生效日，与计费主页面的免费额度写法并存 | 以每月每个电话号码 1,000 条免费、第 1,001 条起计费为准，帮助页的 no charge 读作额度内口径。 | 已写进正文 |
| F3   | 服务消息恢复计费的时点 | 「These messages have not been charged since November 1, 2024. - **Effective October 1, 2026** - Meta will charge on a per-message…」（`pricing_non_template_messages_md`｜第 14 句） | 「Meta charges as of the 1,001st service message per month, after the free tier has been used. - Unused service messages in the…」（`pricing_md`｜第 42 句） | 变更预告页只写历史免费期的终点，计费主页面写的是含免费额度的现行收法 | 正文把 11 月 1 日读作历史免费期的起点，把 10 月 1 日读作恢复计费日，两个时点各归各页。 | 只在附表登记 |
| F4   | 客服窗口内的 utility 消息 | 「These messages have not been charged since July 1, 2025.」（`pricing_non_template_messages_md`｜第 15 句） | 「Additionally, Meta introduced a [free monthly tier](#free-tier-of-monthly-service-messages) of service messages per business…」（`pricing_md`｜第 83 句） | 两页都写 2025 年 7 月 1 日起免费，只有一页补出 2026 年 10 月 1 日起恢复计费 | 正文按恢复计费写，历史免费只作时点说明，不把「免费」写成现行状态。 | 只在附表登记 |
| F5   | 免费入口点消息是否计入免费额度 | 「Thus, any utility or authentication messages sent in a free entry point window do _not_ count toward volume tiers. - Volume tiers…」（`pricing_md`｜第 48 句） | 官方未写明（—） | 官方只写不计入量级阶梯，未写是否计入每月 1,000 条免费额度 | 正文只写「不计入阶梯」，额度归属登记为官方未写明，不替官方补规则。 | 已写进正文 |
| F6   | 免费额度的归属与群组共享 | 「The free tier resets monthly for each business phone number (for example, 1,000 for October with no roll-over; 1,000 for November…」（`pricing_md`｜第 43 句） | 「The 1,000-message free tier is shared with 1:1 service messages for the same business phone number, and each group send consumes…」（`groups_pricing_md`｜第 7 句） | 单聊页面只写每个号码的额度，群组页补出共享关系与按收件人计单位 | 按群组页写「共享同一额度、每个送达收件人消耗一个单位」，单聊额度不另算一份。 | 已写进正文 |
| F7   | 群组客服窗口免费期的截止 | 「Through September 30, 2026, this allows you to send utility and non-template messages for free.」（`groups_pricing_md`｜第 5 句） | 「As of October 1, 2026, group non-template messages delivered within the business phone number's free tier. * `REGULAR`: Billable…」（`groups_pricing_md`｜第 33 句） | 同一页面两处时间切分：一处写免费期终点，一处写新口径下的归类与额度关系 | 正文按 10 月 1 日切分写，9 月 30 日前的免费只作历史口径，不并入现行表。 | 只在附表登记 |
| F8   | 支付方式在两个产品面上的差异 | 「We currently don't accept American Express or digital payments such as PayPal.」（`bh_488291839463771`｜第 6 句） | 「Supported payment methods include credit and debit cards, PayPal, a bank account (direct debit) in supported countries, and local…」（`faq_570949191470325`｜第 10 句） | 两个产品面的清单不同：平台侧排除 PayPal，商业 App 侧支持 PayPal 与银行直扣 | 分面登记，不合并成「WhatsApp 支持 PayPal」这类跨面结论；哪一面支持就在哪一面写。 | 已写进正文 |
| F9   | 营销消息的送达观察窗 | 「You only pay for each message that is delivered to a contact within five days of being sent.」（`faq_1356785542323967`｜第 7 句） | 官方未写明（—） | 平台计费页面未写同类观察窗，商业 App 的五天窗口不能套到平台 | 正文只把五天窗口写在商业 App 一节，平台侧按送达即计费另写，不互相套用。 | 已写进正文 |
| F10  | 计费时点：送达即计费还是五天内累计 | 「We charge only when a message is delivered.」（`bh_2225184664363779`｜第 8 句） | 「With marketing messages, you only pay for each message that is delivered to a contact within five days of being sent.」（`faq_1022626499062292`｜第 5 句） | 平台按送达即计费，商业 App 按发出后五天内送达并累计余额，两个时点不同 | 分面写，平台侧的 delivered 与商业 App 的 within five days 不互换，也不合成一个时点。 | 已写进正文 |
| F11  | 信用额度的责任人与终端客户 | 「You are the "Bill To Party" for all businesses sharing your credit line.」（`solution_providers_share_and_revoke_credit_lines_md`｜第 6 句） | 「The end clients can't use their own credit line to pay for their WhatsApp usage.」（`bh_1684730811624773`｜第 7 句） | 一页写合作伙伴对全部消费负责，另一页写终端客户不能用自有额度付款，是同一链条的两端 | 13.2 并列写：账单责任在合作伙伴一侧，支付路径由共享额度承担。 | 已写进正文 |
| F12  | 账户币种能否更改 | 「Currency selection is part of the Messaging account creation process; once selected, a currency cannot be changed.」（`pricing_md`｜第 27 句） | 「Under the new account model, a business can hold a separate Messaging account per billing currency on a single business phone…」（`pricing_change_billing_currency_md`｜第 6 句） | 默认规则与迁移例外分处两页，后者要求并存独立计费账户而不是改原账户 | 按「创建后不可改、同一号码可并存多币种账户并克隆迁移」写，不写成「可通过接口改币种」。 | 已写进正文 |
| F13  | 开票实体由什么决定 | 「Charges from any BRL Messaging account will be invoiced in BRL by Meta's local entity in Brazil, Facebook Brasil. - As a…」（`pricing_md`｜第 75 句） | 「Your Messaging account's currency and your legal entity's headquarters address together determine which Meta billing entity…」（`pricing_change_billing_currency_md`｜第 14 句） | 通则写「币种加总部地址决定开票实体」，实例只写 BRL 由巴西实体开票 | 主线写通则，BRL 作实例点出，不把巴西实体推广到其它币种。 | 只在附表登记 |
| F14  | 验证消息国际费率的两个写法 | 「Start times are set when your business is first deemed eligible for authentication-international rates, and are 30 days from your…」（`pricing_authentication_international_rates_md`｜第 14 句） | 「New authentication-international rates in 9 markets {#new-authentication-international-rates} These are higher than the utility…」（`pricing_md`｜第 89 句） | 一页写生效缓冲机制，一页写费率水平比较，两处各答一半 | 8.4 写水平、10.1 写 30 天生效机制，不合成一条规则，也不把 30 天读成费率有效期。 | 只在附表登记 |
| F15  | 阶梯重置与资格生效日 | 「This rate applies across all of their Messaging accounts. - Tiers reset monthly — At the start of the next month (12am Messaging…」（`pricing_md`｜第 50 句） | 「Once deemed eligible, we will set your [start times](#start-times) 30 days out for each country that has an…」（`pricing_authentication_international_rates_md`｜第 6 句） | 两个不同的计时轴：量级阶梯按自然月重置，国际费率按资格日加 30 天生效 | 分别写在 8.3 与 10.1，不把 30 天当作阶梯周期，也不把月度重置读成费率生效。 | 只在附表登记 |
| F16  | Meta Business Agent 的计价与折算 | 「Meta only charges for input and output tokens and does _not_ charge for cache tokens. - _Rate_: One global rate of $2.00 USD per…」（`pricing_md`｜第 15 句） | 「One message typically consumes 20,000–25,000 tokens, which translates to approximately 4–5 cents (USD) per message. - _Cost per…」（`pricing_md`｜第 16 句） | 同一页先写 token 单价，再写折算到条的估算，后者是估算不是定价 | 正文把每百万 token 的单价当定价，把 4 至 5 美分标作典型消耗的折算值，不当作报价。 | 只在附表登记 |
| F17  | 订阅退款的两条口径 | 「If you cancel your subscription during your current billing cycle, you won’t be eligible for a partial refund.」（`faq_352796424225584`｜第 7 句） | 「Refunds Refunds for Meta Verified business subscriptions are available if you didn't complete the verification process or if we…」（`faq_1798313690615821`｜第 13 句） | 同一订阅的两条退款口径：未完成验证可退，已完成后取消当期不退 | 15.3 并列写条件，不写「Meta Verified 一律可退」，也不把可退读成按比例退。 | 只在附表登记 |
| F18  | 退款到账时限的适用范围 | 「Refunds may take several business days, and in some cases up to 60 days, depending on your bank or payment provider.」（`bh_1723059664805181`｜第 20 句） | 官方未写明（—） | 平台消息计费页未写退款时限，这句时限属 Meta Verified 订阅 | 只把时限写在订阅一节，不套到平台消息账单，也不当作平台退款承诺。 | 只在附表登记 |
| F19  | 条款层的计费句 | 官方未写明（—） | 官方未写明（—） | 平台条款页整页未写计费句（未写），商业 App 条款只留一句用户自负运营商与网络费用 | 15.6 只登记这一句责任口径，不代条款拟写计费规则，也不把空白读成「不计费」。 | 已写进正文 |
| F20  | 两个订阅产品的续费句 | 「Meta One billing cycle and automatic renewal Meta One renews automatically each month unless you cancel your subscription.」（`faq_26947359861568637`｜第 7 句） | 「Automatic renewals Meta Verified business subscriptions are charged on a monthly basis and automatically renew every month.」（`faq_1798313690615821`｜第 7 句） | Meta One 与 Meta Verified 是两个订阅产品，句面同义但不属同一规则 | 分产品写，句面相同不合并登记；各自续费与取消都在自己的页面里说明。 | 只在附表登记 |
| F21  | 订阅价格的结构 | 「You will be charged on the same date as your original purchase, until you cancel. (Example: If you purchased your subscription on…」（`bh_1723059664805181`｜第 8 句） | 「Meta One offers multiple plan options with different pricing and levels of benefits.」（`faq_975910528178219`｜第 4 句） | 两个订阅产品都只写价格随套餐变化，均未写具体金额 | 只写结构，不写金额；具体价格不在本篇语料里，任何金额都不由本篇补出。 | 只在附表登记 |
| **合计** | **21** 个议题 | 两侧有句 **17**／单侧 **3**／两侧皆无 **1** | 引用页 **18** | 性质 **21** 种 | 正文落点：已写 **10**／只在附表 **11** | 截断 **19** 处 |

#ROWS=21 COLS=7


---

**边界声明：** 本篇是**官方计费规则文本、帮助中心账单说明与开发者文档计费页的口径梳理**，不是操作手册，也不是对平台定价合理性的评价，更不是报价单。全篇内容限于 WhatsApp 与 Meta 公开页面已经写出的表述，共 **33** 个页面登记位：Meta 开发者文档 **15** 页（计费总纲、服务消息与 utility 消息的过渡与恢复计费、会话计费历史页、AI Provider 计价政策、验证消息国际费率、币种迁移、营销消息最高价与报名、模板类别路由、群组计价、通话计价、多方案会话、共享与收回信用额度、支付配置与模板类别两个 webhook）、WhatsApp 帮助中心 **12** 页（WhatsApp Business Account 的账单、加卡、商业广播与营销消息的付费、商业 App 的广告与广播付款、Meta Verified 与 Meta One 的订阅、退款、付款失败与管理员权限、两步验证与支付）、Meta 商业帮助中心 **4** 页（账户账单总览、加信用卡、信用额度申请、Meta Verified 商业订阅的账单与支付）、法律与政策页 **2** 页（WhatsApp Business Platform 条款、WhatsApp Business App 条款）。引用这些文本的目的只有两个：说明平台把**一条消息算成几笔、按哪个单位计价、在哪个时点算、哪些类别有例外、免费额度与免费窗口各挂在哪个对象上**写成了哪些字段名、哪些取值、哪些后果句与哪些否定式前提；说明同一批文本把**费率卡、量级阶梯、国际费率、最高价、币种、开票实体、支付方式、信用额度与订阅退款**写成了哪些数字与哪些限定词。本篇**不含**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法建议；**不含**任何可用于向非自愿收件人大量投递消息的方法——"怎样把营销消息伪装成服务消息""怎样把一条群发拆成多条免费额度""怎样在额度用尽后继续投递"这三类问法在本篇一律不答；**不含**代码、恶意样本或利用链构造方式；**不含**任何针对他人设备或他人账号的操作步骤与时序。界面页与接口页里官方写成"步骤""清单"的内容——添加支付方式与申请信用额度的界面层级、币种迁移的接口调用次序、白名单测试的开关位置——本篇只登记**控件名、接口名、字段名与条目数**，**不重述每一步在哪个页签里怎么点，也不把清单转写成可执行的动作序列**，读者要动手请回原页。

四个产品面的计费口径在本篇里始终分开写，因为官方没有一页把它们合并：**平台侧**按消息计价、以送达为计费时点，类别、市场、量级阶梯与免费额度都在这一面上展开（第 2 至 12 章）；**商业 App 侧**另有一套按发出后五天内送达累计的写法，支付方式清单也与平台侧不同（15.1 与 15.2，附表 F 的 F8、F9、F10）；**订阅类付费能力**（Meta Verified 与 Meta One）只写套餐结构与续费、退款条件，不写具体金额（15.3 与 15.4，F17、F18、F20、F21）；**条款页**整页不写费率，只留一句用户自负运营商与网络费用的责任口径（15.6，F19）。把这四层合并成"WhatsApp 的收费规则"，会得到官方没有写过的跨面结论，本篇不这么做。

官方写成区间、多档或带限定词的数字（「up to」、「approximately」、「starting」、「in some markets」（某些市场）一类）按整段一行登记在附表 B，不合成单值、不取中值、不去掉官方写在数字前面的限定词；同一数字在不同指代上各写一处的按指代分列，不做跨指代合并；同一句官方原句在两个页面各出现一次的，B 表也各登记一行，因为它的去重键是「页面＋数字＋单位」。**本表有两类数字进不了行，本篇如实登记而不绕过**。**其一**，数字停在句末或停在名词前、脚本在数字之后四词窗口里定不出单位词的句子，本轮这样的实例共 **36** 个，连同它们所在的整句写进 `_tmp/w6_a6_bdrop.tsv` 供逐行复核，不进表；这一批绝大多数是地址、法条编号与接口示例里的对象 ID，不是官方对某项业务写下的数量，**"这个数是不是一个上限"的判定权在读者手里，不在这张表里**。**其二**，官方把数量写成英文词而不是阿拉伯数字的句子，正文保留词形陈述，B 表不给它们造行。同页同数字同单位只留一行，本轮去重 **74** 行。B 表最终是 **128** 行、分布在 **19** 个页面、五档 **44／1／17／28／38**、单位词 **33** 种、性质 **11** 种、整句截断 **95** 行；截断只发生在显示层，进表的每一句都先用未截断整句回原页核对，失败 **0** 句。它的输入是原句池的 **768** 条整句（覆盖 **32** 页），其中 **612** 句整句不含任何阿拉伯数字、本来就不在 B 表的取材范围里。另有 **1** 个可引用页面在池子里一句都没有（第 1.3 节点名），所以这一页在本表里是空的——**"这一页没有 B 表行"不等于"这一页没有数字"**，那一页上的数字写在参数表里，只能连同表格 markup 一起取，而带竖线的混合句在建池时已剔除。

正文引句全部为整句、逐字回核到抓取件。整句的判据是抓取件的句池切分口径：句池按句子终止符切、不按版面标记切，因此官方 markdown 页里紧跟句子的小标题会被并入同一句；同时，长度不在 40–400 字符窗口内、段内含竖线或内联链接、以 `#` 或 `-` 起手的片段一律不入池。本轮正文引用去重后 **99** 句（引用标记 **99** 处，没有同一句在正文重复引用），分布在 **30** 个页面上，其中 **1** 句（分布在 **1** 个页面）尾部带了一段 `## 小标题` 与紧随其后的另一句，**2** 句（分布在 **1** 个页面）自带官方粗体排版标记 `**`。这两类都不修补：本篇按句池原样照引、不删字符、不补标记，中文说明只覆盖小标题之前的句子本体，读者按附表登记的「页面＋句序」可回抓取件逐字复核。粘连发生在官方页面的排版层而不是本篇的转写层。另有一条硬规矩：含反引号字段名与枚举名的句子不整句引用，这些页面上的事实改用中文陈述并逐处标注出处页；**本轮正文实际引用的 99 句里，含反引号的句子是 0 句**，这条规矩没有被绕过。本轮引用的句子里没有带英文双引号且引号不闭合的句子（回核计数为 **0**）。

各层之间官方从未给出对齐表：本篇在附表 E 按同一组维度并排 **8** 列——计费总纲 **1** 页／专项计费页 **7** 页／营销最高价与路由 **3** 页／账单与信用额度 **5** 页／接口与信号页 **2** 页／商业 App 计费页 **4** 页／订阅与账号付费页 **9** 页／条款页 **2** 页——**16** 个维度 × 8 列 = **128** 格里 **51** 格取到官方原句、**77** 格标作「官方未按这一列写该项」，该标注**只说明官方文本的写法，不说明该类主体没有该项规则**；在附表 C 把「官方是否写明」限成「词（组）× 站点类别」矩阵，**79** 个词（组）分属 **13** 个主题组 × 4 类站点共 **144** 个非空格、句次 **1110**、去重命中句 **482**（占全池 62.8%）、一句最多同时落进 **9** 格、**273** 句落进两格以上，**3** 个词（「7 days」、「free of charge」（免费）、「deprecated／deprecation」）按零命中登记而不改词表；在附表 D 只登记官方在页面里逐字写出的取值域（**6** 组、**20** 个取值、去重取值名 **20** 个、覆盖 **3** 页），脚本配置的 **10** 串取值域标记本轮扫到 **5** 串、另 **5** 串在扫过的每一行原文里零命中，因此本篇不把它们当作任何官方句子的引文；另有 **3** 处官方写了标记却取不出取值域、分布在 **3** 个页面，逐处连同原文行号写进 `_tmp/w6_a6_dmiss.tsv`；**D 表的扫描范围限于站点类别为 Meta 开发者文档的登记页**，政策页与帮助中心页的取值域不在 D 表范围内，本篇不声称它们在 D 表有行；在附表 F 把 **21** 个议题逐条对撞（两侧有句 **17**／只有一侧 **3**／两侧都无原句 **1**，引用页去重 **18**、差异性质 **21** 种各不相同、复用 **0** 次），凡官方留白处一律登记为留白，不推断、不补齐。

页面层的四项事实单独交代，免得读者把"登记"读成"引用"：**第一**，本篇登记的 **33** 页正文全部取回，没有一页被标注"全篇不引用"；**第二**，**1** 页正文取回但池内零句——第 1.3 节点名的模板类别信号页，它的内容是一组取值说明与接口字段，进不了句子层；这一页在附表 B 至 F 里都不会出现，只在附表 A 留一行登记，它的事实由第 11.3 与 14.3 节的中文陈述承担；**第三**，**33** 页全部在正文被官方标题点名（标题标记共 **39** 处），附表 A 的引用位置列没有一格留空；**第四**，反向的一条同样成立：正文整句引用落在 **30** 个页面上，其余页面被点名而未引句、或只在附表里出现，逐页状态由脚本实测而非人工填写。

本篇对"某条消息到底该收多少钱"、各市场费率的数值比较、阶梯档位的划算程度、免费额度的最优用法、退款能否成功、开票实体与税率的对应关系一律不补；具体金额只出现在官方写出金额的句子里，正文不折算、不汇总、不换算币种。「Meta Business Agent 每条消息折合多少美分」与「订阅套餐具体价格」这两处，官方在登记页里只写结构与估算，本篇把它们登记在附表 F 的 F16 与 F21，不写成报价；「条款页为什么不写费率」登记为 F19，本篇只写条款页实际写了什么，不代条款拟写计费规则。

最后两处范围断言必须交代扫描口径，因为它们都是"穷举后没有"而不是"抽样没看到"：**其一**，本篇在 1.1 与第 16 章写"官方没有一页把计费单位、免费额度、费率卡与账单责任画成同一张表"。这一句的依据是：附表 C 的 13 个主题组共 **79** 个词（组）——A 计费动作与计量单位 10／B 消息类别的名字 7／C 时间窗口与周期 8／D 免费、额度与豁免 6／E 阶梯、聚合与计数 5／F 钱、支付方式与信用额度 8／G 费率、出价与预算 6／H 市场与地域 5／I 账单信号与取值名 5／J 变更、迁移与废止 4／K 订阅、续费与退款 4／L 责任、资格与权限 6／M 与计费相邻的钱款词 5——在原句池 **768** 句里逐句扫过，命中落在不同的页面与不同的字段上；但这只覆盖本篇登记的 **33** 页，帮助中心与政策站里还有大量本篇未登记的页面，所以"没有一页画成同一张表"是对这批语料的陈述，不是对 WhatsApp 全部公开文本的陈述。**其二**，附表 F 有 **11** 个议题标成"只在附表登记、正文未并列两句"，逐条点名是：F3 服务消息恢复计费的时点、F4 客服窗口内的 utility 消息、F7 群组客服窗口免费期的截止、F13 开票实体由什么决定、F14 验证消息国际费率的两个写法、F15 阶梯重置与资格生效日、F16 Meta Business Agent 的计价与折算、F17 订阅退款的两条口径、F18 退款到账时限的适用范围、F20 两个订阅产品的续费句、F21 订阅价格的结构。这 11 行都能在附表 F 逐字回表，其中两侧都有原句的按两侧登记、只有一侧的按单侧登记；本篇不在正文把它们并排成一句结论——**排不并排是叙述选择，不是"官方没有连接句"的断言**；读者若要把它们当通则使用，应自己回到那两页看官方在上下文中怎么连接这两句。

Telegram: https://t.me/one00190
