@@TITLE@@WhatsApp 回调事件与错误码：消息状态机、签名校验与重投的官方口径解读
@@SUBTITLE@@本篇是"WhatsApp 群发／群控系统性口径"系列第四轮的第三篇（轴③，编号 n=14），讲的是：Meta 把回调写成什么、端点要满足哪些前提、握手与签名两道校验各管什么、一条外发消息最多留下几条状态、已读与已送达谁先谁后、批量投递有没有保证、失败之后重投多久、未确认的响应为什么七天后丢弃、错误从哪两条通道回来、交替回调地址按什么顺序兜底、安全与回声两类非消息回调写什么、帮助中心的勾与等待提示怎么对上状态、时间戳为什么会对不上、以及哪些问题官方在这批页面里没有写。全篇只读官方文本，共 **12** 个页面登记位（**12** 页正文全部取回，逐页归属与引用位置见附表 A 的 **13** 行含合计）：Meta 开发者文档 **9** 页（回调总纲 **1**、端点 **1**、五类回调参考 **5**、交替地址覆盖 **1**、错误码总表 **1**）、WhatsApp 帮助中心 **3** 页。
@@INFO@@报告日期|2026 年 11 月 19 日
@@INFO@@分析对象|按站点清点本篇登记的页面构成：Meta 开发者文档 **9** 页（WhatsApp webhooks、Create a webhook endpoint、messages webhook reference、Status messages webhook reference、Errors messages webhooks reference、Security webhook reference、smb_message_echoes webhook reference、Webhook overrides、WhatsApp error codes）、WhatsApp 帮助中心 **3** 页（已读回执、等待消息提示、时间戳修正）。合计 **12** 个登记位，正文全部取回；归组依据是官方页面标题，逐页归属见附表 A。
@@INFO@@视角定位|官方规则文本、帮助中心说明与开发者文档的**口径解读**，不是对接教程，也不是投放方案。本篇**不提供**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法；本篇同样**不写**任何可用于向非自愿收件人大量投递消息的方法——"怎样伪造签名头骗过回调校验""怎样把回调地址换成自己的接收端""怎样用重投机制压垮对方服务"这三类问法在本篇一律不答；官方把回调写成事件、字段、状态与计时器，本篇只登记这些句子出现在哪一页、哪一句（界线见 1.3）。
@@INFO@@分类|**官方页面登记 12 个登记位 × 2 类站点 × 引用位置 × 取回状态**（附表 A，**13** 行含合计；Meta 开发者文档 **9**、WhatsApp 帮助中心 **3**；可引用 **12** 页、不可引用 **0** 页；原句池 **204** 条整句、覆盖 **12** 页）× **数字归档 10 条，覆盖 5 页，去重"数字＋单位" 9 组、单位词 6 种、性质 6 种（上限 2／下限或触发门槛 1／时长 1／月份与号数写明的日期节点 1／申诉与复核时长 4／自动解除或自动生效的时限 1），截断 7 行，池内不含数字的整句 183 句、定不出档位而丢弃的数字实例 5 个**（附表 B，**11** 行含合计）× **官方用词 × 站点类别矩阵 57 个词（组）分属 8 个主题组 × 2 类站点、56 行、句次 318、去重命中句 135（占全池 66.2%）、零命中词 9 个**（附表 C，**57** 行含合计）× **官方写明的取值域 5 组、取值 27 个、去重取值名 21 个、覆盖 2 页，配置的 10 串标记本轮扫到 1 串、9 串零命中，另有 1 处写了标记却取不出两个以上取值**（附表 D，**6** 行含合计）× **同一组维度在各主体上的逐列对照 28 个维度 × 7 列 = 196 格、有句 34 格／空格 162 格、去重引用页 11、同一句跨列复用 1 次、截断 16 格**（附表 E，**29** 行含合计）× **口径冲突与官方未写明 11 个议题、两侧有句 9、单侧有句 1、两侧皆空 1、差异性质 11 种、去重引用页 7、复用 2 次、正文落点已核 7 行 14 句、截断 7 处**（附表 F，**12** 行含合计）× **正文与附表的官方原句逐字回核，失败 0 条**。
@@INFO@@难度|进阶（本篇有六处容易读错。**第一是状态回调有上限**：一条外发消息最多留下三条状态回调，已送达与已读同时发生时官方只送已读（4.1、4.5，附表 F 的 F4 与 F5）。**第二是已读以已送达为前提**：状态没有送达就谈不上已读，而群聊里的已读判定还牵进"所有人"这个主体（4.4、8.1，F5 与 F8）。**第三是两个七天不是同一件事**：一个说重投在七天内递减进行，一个说未确认的响应七天后丢弃（5.2、5.4，F1 与 F3）。**第四是批量上限与批量保证方向相反**：官方给了一个上限，同时明说分批不保证（5.1，F2）。**第五是覆盖顺序只对点名的字段类型生效**：模板与账号级回调不接受覆盖，官方直接投向默认地址（7.1、7.2，F6）。**第六是错误的三处位置与两条通道**：同步响应与异步回调都可能带错，而 title 已被 code 取代（6.2、6.3，F7）。）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

本篇是"WhatsApp 群发／群控系统性口径"系列第四轮的第三篇（轴③，编号 n=14），主题是**回调事件与错误码**：Meta 的服务器在什么时候、向哪个地址发一条什么样的请求；一条请求要过哪两道校验；一条外发消息在回调里最多留下几条状态、每条状态用哪一个词；已读与已送达谁先谁后；投递失败之后重投多久、什么情况下放弃；回调里的错误与接口同步返回的错误是不是同一套东西；状态回调里的计费字段为什么带着两个不同的截止日期。全篇只读官方文本，共 **12** 个页面登记位：Meta 开发者文档 **9** 页、WhatsApp 帮助中心 **3** 页；**12** 页正文全部取回、全部进入原句池，共 **204** 条整句。

前几篇问的是"谁能发、发给谁、名字归谁、对方愿不愿意、到了之后怎么办、一条消息被算成几笔、商品与订单怎么摆、媒体上限写在哪一层"；本篇问的是"**发出去之后，平台用什么句子把结果告诉你**"。这条线在官方文本里被拆在四类页面上：两页总纲写回调是什么、端点要满足什么条件、失败怎么重投；五页参考写 messages、status、errors、security 与回声这五类回调的载荷；一页覆盖页写交替回调地址的兜底顺序；一页错误码总表写同步与异步两条通道、code 与 title 两种标识；三页帮助中心写用户端看到的勾、提示与时间。四类页面之间没有一张统一的"回调字段表"，只有互相指向的链接；本篇把它们并排登记在附表 E 的 **28** 个对照维度 × **7** 列 = **196** 格（**34** 格取到官方原句、**162** 格标作"官方未按这一列写该项"），并把成对出现的口径差异登记在附表 F 的 **11** 个议题里。

本篇的读法是**沿一条回调走一遍**：先看回调是什么、端点要满足什么条件、交替地址怎么兜底（第 2 章）；再看两次请求各自的校验——握手用验证令牌，投递用签名头（第 3 章）；然后是消息状态机，sent、delivered、read、failed、played 各自的定义与前提（第 4 章）；投递失败之后，批量、重投、去重与丢弃各按什么期限走（第 5 章）；错误码从哪里回来、按什么字段处理（第 6 章）；覆盖、安全与回声三类非消息回调（第 7 章）；用户端的三个勾、等待提示与时间戳（第 8 章）；最后是横向对照（第 9 章）、容易读错的几处（第 10 章）与边界（第 11 章）。

下面每个问题都标出它落在哪一节，一个问题可以跨相邻两小节，一节也可以由两个问题分别追问；跨小节的关联只做中文指认，不把两页原句拼成一句。

1. 这批页面在站点上怎么分布，"12 页登记、12 页有句"意味着什么？（1.1）
2. 本篇固定了哪些中文口径词，每个词对应哪个英文原词？（1.2）
3. 回调、事件、状态、投递、确认、错误码在本篇里各指什么？（1.2）
4. 本篇不写什么，界面页写成"步骤"的内容登记到什么颗粒度？（1.3）
5. 官方用什么句子定义回调，它把回调写成几个用途？（2.1）
6. 接收回调之前，端点本身要满足哪些前提？（2.2）
7. 自签名证书为什么不能用于回调端点？（2.2）
8. 交替回调地址是什么，系统按什么顺序决定往哪里投？（2.3）
9. 交替地址只对哪些字段类型生效，哪两类回调被点名排除？（2.3、7.1、7.2）
10. 消息回调为什么有两副骨架，messages 数组与 statuses 数组各由谁产生？（2.4）
11. GET 请求在回调体系里做什么，什么时候会被触发？（3.1）
12. 握手校验比对的是哪两个值，成功了回什么，失败了回什么？（3.1）
13. 端点被判为已验证或未验证，分别意味着什么？（3.2）
14. POST 投递带着哪个签名头，比对的是哪一段字符串？（3.3）
15. 两次校验是不是同一套东西，为什么不能互相替代？（3.3、10.5）
16. 校验通过之后官方让先做什么，为什么先存再处理？（3.4）
17. 官方有没有提供回补历史回调的接口？（3.4、5.5）
18. 一条外发消息最多留下几条状态回调，这三条各是什么？（4.1）
19. 三条是固定条数还是上限，什么情况下会少一条？（4.1、10.1）
20. "已读"以什么为前提，未送达的消息能不能算已读？（4.2）
21. 什么情况下送达与已读会同时发生，此时回调侧少发哪一条？（4.2）
22. 四种状态在用户端各显示成什么符号？（4.3）
23. 用户端说"两个勾"与回调说"delivered"，是不是同一层的东西？（4.3、9.1）
24. 失败状态回调里哪些字段会缺席，缺席条件是否相同？（4.4）
25. 用状态标识对不上原消息时，官方让拿哪个字段做关联？（4.4）
26. 群聊里的送达与已读，回调与界面各按什么主体判定？（4.5）
27. 状态回调里的计费字段为什么要带两个截止日期？（4.6）
28. 批量投递的上限是多少，官方为什么不保证每次都会聚合？（5.1）
29. 投递失败之后的第一次重投是立即开始还是等一段时间？（5.2）
30. "最多七天"在这批页面里出现了几次，两次说的是不是同一件事？（5.2、5.3、10.2）
31. 去重由谁负责，官方为什么把这件事写在服务端？（5.4）
32. 未被确认的响应会怎样，七天后去哪里？（5.3）
33. 错误可能从哪两条通道回来，官方建议同时监控什么？（6.1）
34. 回调载荷里的错误可能出现在哪三处？（6.2）
35. 错误处理应当围绕哪个字段写，为什么不按标题写？（6.3）
36. 错误总表里的时限与状态码各有哪些可整句引用的例子？（6.4）
37. MM API 与 Cloud API 的错误码是什么关系？（6.5）
38. 覆盖回调地址时，号码、账户与应用三级各在哪一步被检查？（7.1）
39. 哪些字段类型不支持覆盖，订阅时去掉参数会发生什么？（7.2）
40. 回声回调写的是谁发的消息，撤回与编辑各携带什么标识？（7.3）
41. 安全回调通知的是什么，它算不算消息状态？（7.4）
42. 用户端的三个勾、一个时钟与"等待这条消息"分别对应什么？（8.1 至 8.4）
43. 时间戳异常时官方把原因指向哪一层？（8.5）
44. 把这批页面横向排开，哪几组词是"同词不同层"，附表 E 的空格该怎么读？（9.1、9.2）

# 1 口径与范围

## 1.1 登记、取数与五张表

本篇登记 **12** 页，来源只有两个站点：Meta 开发者文档 **9** 页（回调总纲《WhatsApp webhooks》、端点创建《Create a webhook endpoint》、messages 参考《messages webhook reference》、status 参考、errors 参考、security 参考、覆盖页、回声参考、错误码总表），WhatsApp 帮助中心 **3** 页（回执、等待提示、时间戳）。这个分布不是按"哪边写得更细"选的，而是按"回调在这条链路上被拆到哪一层"选的：请求怎么发、校验怎么做、字段怎么回传写在开发者文档，用户端看到什么符号、什么提示写在帮助中心，两边对同一件事的措辞并不一致，第 9 章与第 10 章专门处理这种不一致。

取数分三层，三层共用同一批抓取件。**第一层是原句池**：脚本把每页正文切成整句，剔除含竖线的表格混合句、以列表符号起手的片段、长度越界的句子，得到 **204** 条整句，覆盖全部 **12** 页——这一轮没有整页零句的页面。**第二层是词矩阵**：附表 C 用 **57** 个词（组）分 **8** 个主题组逐句扫过这 **204** 句，全表 **56** 行、句次相加 **318** 次、去重后真正被命中的句子 **135** 句（占全池 66.2%），另有 **9** 个词在整池一句都没出现。**第三层是对照表**：附表 E 把 **28** 个维度并排放在 **7** 列主体上，每一格用"页面标识＋一条正则"在原句池里取句，取不到就留空并写明"官方未按这一列写该项"。

三层之外还有三张登记表：附表 A 逐页登记标识、站点、URL、官方标题、取回状态与引用位置；B 表只收"数字＋它数的东西"，本篇这批页面里带阿拉伯数字且能定出单位的整句集中在五个页面，该表有 **11** 行（含合计）、条目 **10** 条，分布在 **5** 个页面；D 表只登记官方逐字写出的取值域，本篇有 **5** 组、**27** 个取值、覆盖 **2** 页。**数字变少不等于规则变少**：状态参考页有大量带字段名的表格行，按建池口径整行剔除，所以计费类别、费率与状态的取值大多不在 B 表里；本篇正文因此只写官方能整句引用的那几句概括，读者要逐值对照请回原页看表。

## 1.2 口径词：回调、事件、状态、投递、确认、错误码

这条链路上最容易混的是六个词，本篇先固定中文译法与所指，后文不再改口。

**回调（webhook）**：官方把它写成"由 Meta 的服务器发往你指定服务器的一次 HTTP 请求，载荷是 JSON"；本篇把回调当作**一次请求**，不当成一条消息，也不当成一个通知中心。**事件（event）**：触发回调的那件事，例如用户发来一条消息、一条外发消息的状态变化、账户状态变化；事件是原因，回调是结果。**状态（status）**：外发消息在回调里的取值，官方在状态参考页把它写成 delivered、failed、played、read 等词；本篇把"状态"限定在这几个词上，不把用户端的勾直接叫状态。**投递（delivery）**：Meta 把回调送到端点这个动作；"消息投递"与"回调投递"是两件事，前者对应用户收到消息，后者对应你的服务器收到请求，本篇在后文一律写全"回调投递"或"消息送达"，不省成"投递"。**确认（acknowledge）**：端点对一次回调请求给出官方认可的响应；未确认的响应有自己的丢弃期限，第 5.3 节写。**错误码（error code）**：接口同步返回或回调异步带回的错误标识；官方把 code 与 details 当作处理依据，把 title 当作会弃用的展示文本，第 6.3 节写。

另外三组官方的专名本篇按原词照写、不译成别的：**messages** 与 **statuses** 是消息回调里两副不同的数组骨架；**X-Hub-Signature-256** 是投递请求携带的签名头名；**override** 是交替回调地址的官方叫法。还有一组必须分开的官方词：**Webhooks** 与 **WhatsApp Business Platform 的 webhook 字段**——前者是机制，后者是可以逐项订阅的字段；订阅哪些字段决定你会不会收到对应事件，本篇只在第 2.1 与 7.2 节点名，不展开订阅清单。

## 1.3 不写什么

本篇的边界与系列前几篇一致：全篇只登记官方写出的对象、字段、类别、期限与状态，**不提供**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法；**不含**任何可用于向非自愿收件人大量投递消息的方法——"怎样用回调日志反推对方是否在线""怎样伪造一条送达回调骗过自己的系统""怎样让重投把对方的端点压垮"这三类问法在本篇一律不答；**不含**代码、恶意样本或利用链构造；**不含**针对他人设备或他人账号的操作步骤。界面页里官方写成"步骤"的内容——在哪个页签看消息信息、回执怎么关——本篇只**点名与计数**，**不重述点击次序**，也不把清单转写成可执行的动作序列；读者要动手请回原页。

还有一类"不写"是对象层面的：本篇不判断某个重投窗口该不该这样定、不评价某种批量策略好不好、不推断官方没写的重投间隔数值与密钥轮换规则、不把帮助中心的用户端口径与开发文档的服务端口径合并成一条"WhatsApp 的回调规则"。这些空白在附表 F 里逐条登记，正文只在它们真正影响读法的地方点名。

# 2 回调是什么：一条从 Meta 服务器到你端点的请求

## 2.1 官方定义与三类通知

《WhatsApp webhooks》开篇先给范围，再给定义：

> `This document describes webhooks and how the WhatsApp Business Platform uses them.`

> `Webhooks are HTTP requests containing JSON payloads that Meta's servers send to a server of your designation.`

两句话把回调钉在三个位置上：发起方是 Meta 的服务器，接收方是你指定的服务器，载荷是 JSON。它不是"消息推送"的同义词，也不是一个可以回补的日志服务；它是一次**由事件触发的请求**。官方在同一页列出它用来通知的事情：用户发来的消息、外发消息的状态、呼叫事件，以及账户状态变化、消息能力升级、模板质量分变化这类账户级信息（这一句带字段名，按第 1.3 节的引句规矩只作中文陈述）。这几类事情的共同点是"发生了、需要让你知道"，而不是"你随时可以来查"。

接收回调不是默认开启的。官方在总纲页用一句判断句把前提摆出来：

> `To receive webhooks, you must create and configure a webhook endpoint.`

也就是说，先有端点，才有回调。端点的创建、证书与校验写在第 2.2 与第 3 章；账户级事件与消息事件共用同一个入口，但它们的字段类型不同，覆盖规则也不同（第 7 章）。

## 2.2 端点必须具备的证书与两次握手之外的前提

《Create a webhook endpoint》是这批页面里唯一一页把"端点"当成对象来写的。它先给出一条总体要求：端点必须是一个公网可访问、能接收并响应 GET 与 POST、能校验并保存回调载荷的服务器（这一句把前提叠在一条长句里，本篇只作中文概括，不拆成动作清单）。证书是硬条件：

> `Your webhook endpoint server must have a valid TLS or SSL digital security certificate, correctly configured and installed.`

> `Self-signed certificates are not supported.`

两句合起来是一个**必要条件加一条排除**：端点必须有一张配置正确、已安装的有效证书；自签名证书不在支持范围内。官方没有在这两页解释"为什么自签名不行"，也没有给出例外清单，本篇不替它补。加密层还有一项可选支持：

> `Webhooks support mutual TLS (mTLS) for added security.`

同一句话在总纲页也出现了一次，两页各写一处，指向同一项能力；本篇只在这里整句引用一次，不把它读成两种不同的 TLS 模式。签发证书的机构、证书轮换周期与 mTLS 的证书来源，这批登记页面没有对应句子，附表 F 把"密钥轮换"登记为官方留白。

## 2.3 交替端点：覆盖的兜底顺序

《Webhook overrides》处理的是"回调发到哪个地址"。官方先给默认值，再给覆盖：

> `Meta sends webhooks to the callback URL set on your app, but you can override the default callback URL by designating an alternate callback URL for the Messaging account or business phone number.`

覆盖的设计不是"再配一个地址就完事"，而是一条**三级兜底链**：事件触发后，系统先看与该事件关联的业务电话号码有没有交替地址；有就发到那里；没有再看与该号码关联的消息账户有没有交替地址；账户也没有，才回落到应用默认地址。官方把这四步写在相邻的四句里，每一步都是一个判断而不是并列选项——顺序是官方的，不是本篇排的。总纲页另有一句说明交替端点的用途：

> `An alternate endpoint can be useful for testing purposes, or if you are a partner and wish to use unique webhook endpoints for each of your onboarded customers.`

这句话解释的是"为什么要有多地址"，不改变上面的兜底顺序。覆盖还带着两条限制：只对官方列出的字段类型生效；不对模板类与账户级回调生效。这两条分别在第 7.1 与 7.2 节写。

## 2.4 消息回调的两副骨架：messages 与 statuses

《messages webhook reference》把消息回调分成两个方向。先看它自己的定义：

> `The **messages** webhook describes messages sent from a WhatsApp user to a business and the status of messages sent by a business to a WhatsApp user.`

> `Messages webhooks describing a message sent by a business to a WhatsApp user have a different structure.`

两句话对应两副骨架：用户发给商家的消息，载荷里有一个 messages 数组；商家发给用户的消息，载荷里有一个 statuses 数组。前者的对象随消息类型变化，所以官方给每种类型各配一页参考；后者的对象只描述状态，不描述消息内容本身——官方在紧接着的句子里把这一点写成"每个外发消息最多三条独立回调"，这一句同时是本篇第 4.1 节的主句。骨架不同带来一个读法上的后果：**同一个字段名在两副骨架里可能指不同对象**，本篇在引用时一律带上它所在的数组名，不写"回调里的 id"这种省掉主体的说法。

# 3 端点验证与签名：两次请求，两套校验

## 3.1 GET 握手：验证令牌比对

回调体系里的 GET 不是"取数据"，而是"验证端点"。官方先写用途：

> `GET requests are used to verify your webhook endpoint.`

触发时机是"每次设置或修改回调地址字段或验证令牌字段时"，Meta 就向端点发一次 GET；端点必须校验并回应（这两句带字段名与链接，只作中文陈述）。校验的比对对象是一个字符串：

> `If the values match, the request is valid.`

比对的两边是请求里的验证令牌与你自己服务器上存的那串验证字符串；相等就判请求有效。有效时官方要求回 200 并带回请求里的 challenge 值；无效时回 400 级状态码或任何非 200 的状态（这两句带字段名，只作中文陈述）。这里有一个容易漏掉的细节：**无效响应的判据是"不是 200"，不是"必须是 400"**——官方把 400 级放在前面，但又补了"或任何非 200"，所以回 302、回 500 同样落在无效一侧。

## 3.2 握手的结果：已验证与未验证

握手不是一次性的仪式，而是每次改配置都会重来一遍。官方的判断句是：

> `If your webhook endpoint responds with anything else, however, Meta will consider your webhook endpoint unverified, and webhooks will not be sent to your endpoint.`

反过来说，端点回了 200 并带回 challenge，官方就认为端点已验证并开始发送回调。这构成了一个**开关关系**：验证不过，后续的投递不会开始；验证通过，投递才开始。界面侧还有一句描述这个开关的结果：

> `If your webhook endpoint is responding to webhook verification GET requests properly, the panel will save your changes, and a list of fields you can subscribe to will appear.`

也就是说，面板是否保存你的修改、是否出现可订阅字段列表，取决于端点有没有正确回应验证 GET。这句话是界面行为的描述，不是一条新的校验规则；它与前面的 200 判据同源。

## 3.3 POST 投递：X-Hub-Signature-256 比对

验证通过之后，真正的事件由 POST 送来。官方先写触发条件：只要你订阅的任一字段被触发，Meta 就向端点发一次带 JSON 载荷的 POST（这一句带字段名，只作中文陈述）。载荷的完整性靠签名头校验：

> `If the hashes match, the payload is valid.`

> `If they do not match, consider the payload invalid.`

比对的对象是"你自己算出的哈希"与"签名头里 sha256= 之后的那一段"；相等则载荷有效，不等则视为无效。官方没有在这两页写哈希用的是什么密钥、密钥从哪里取、密钥能否轮换——这些都属于"官方未写"的空白，附表 F 的对应行只登记事实，不补流程。这里必须把两次校验分开：**GET 比的是验证令牌，POST 比的是签名哈希**；前者决定"这个端点是不是你的"，后者决定"这条载荷有没有被改过"。两者不是同一套东西，也不能互相替代（第 10.5 节再收一次）。

## 3.4 校验通过之后：先存后处理

签名通过之后，官方给的动作顺序很短：

> `Capture the payload and digest the payload contents according to business needs.`

"捕获并消化"这两个动作之间没有规定先后，但官方在另一句里给了理由：回调数据没有历史接口可补取，所以要按需保存。响应侧的要求是：

> `If the request is valid, respond with HTTP status 200.`

> `Otherwise, respond with a 400-level HTTP status, or anything other than status 200.`

有效请求回 200；其余情况回 400 级或任何非 200。这里的"有效"指的是签名校验通过，不是"业务处理成功"——官方把响应要求写在校验段落里，处理失败与响应状态是两件事。至于保存多久、存成什么格式、要不要落盘，官方没有规定，本篇不替它设计。

# 4 消息状态机：sent、delivered、read、failed、played

## 4.1 一条外发消息最多三条状态回调

《Status messages webhook reference》定义的是外发消息在回调里的状态。先看这份参考的任务声明：

> `This reference describes trigger events and payload contents for Messaging account status **messages** webhook.`

状态回调与消息回调不是两套系统，而是同一页 messages 回调的另一个数组。官方在 messages 参考里先写了条数：

> `These webhooks don't describe the contents of the outgoing message itself, only its status, and each outgoing message can have up to three separate webhooks (one for a status of sent, one for delivered, and one for read).`

这句话里有三个成分：状态回调不描述消息内容；一条外发消息最多三条；三条分别对应 sent、delivered、read。**"最多"不是"固定"**：第 4.2 节会看到，官方点明了一种只发两条的情形。三条之外，官方还列出 failed 与 played 两个状态值，它们与那三条的关系写在状态参考的取值说明里（这一句带字段名与列表，只作中文陈述）。本节的读法因此是：**三条是上限，状态值是枚举**，两件事不能互相换算。

## 4.2 已读以已送达为前提，以及同刻送达的例外

状态之间不是并列的四个词，而是一条有前提的链。官方用一句判断句把前提钉住：

> `A status is considered read only if it has been delivered.`

未送达就不算已读——这条前提先排除掉"对方根本没收到，但界面显示已读"这种读法。紧接着官方又给了一个例外：

> `In some cases, like when a user receives a message while in the chat screen, the message is both delivered and read at the same time.`

用户在会话界面里收到消息时，送达与已读可以是同一时刻。例外带来的回调后果是：

> `In these cases, the "delivered" webhook is not sent because it's implied that the message was delivered since it was read.`

此时官方不发送达那条，因为它已被已读蕴含。官方在下一句说明了原因——内部优化，而不是投递失败或配置问题。这三句合起来，构成本篇对"三条"的正确读法：**上限三条、前提先行、例外处减一条**；不能把"三条"当成"无论如何都会收到三条"，也不能把"少一条送达"当成丢包。

## 4.3 四种状态在用户端各显示成什么符号

状态参考页给每个状态值配了一条界面等价说明：delivered 对应两个勾；failed 对应红色错误三角；played 对应蓝色麦克风；read 对应两个蓝色勾。这四条说明都带字段名与界面词，按本篇体例只作中文陈述，不整句引用。把它们与帮助中心的说法并排，可以看到两层对齐：回调侧用界面符号解释状态值；用户侧则按"送到哪台设备"与"谁读到了"给符号——送达指送到收件人的手机或任一已关联设备，没发出去或没送到时显示一个时钟（这两句分别在第 8.1 节整句引用）。已读的界面符号在群聊里还多一层条件：

> `Two blue check marks appear when everyone has read your message.`

**回调里的 read 与界面上的两个蓝勾不是同名关系，而是等价说明关系**：官方在状态参考里用界面符号解释状态值，读者要拿界面现象反推状态时，必须连同"前提是已送达"一起带上，否则会把"对方没收到"读成"没读"。

## 4.4 失败状态回调里缺席的字段

失败回调不是"把成功回调改一个状态值"，它会缺字段。官方在 messages 参考里写了两条缺席规则：失败状态回调一律不含 contacts 数组；如果原消息是发给电话号码的，状态里的 recipient_user_id 也会省略（这两句带字段名，只作中文陈述）。两条规则的条件不同：前一条对所有失败回调成立，后一条只在"发送对象是电话号码"时追加。官方还给了关联方法：用状态标识与原发送请求对上。最后这句可以整句引用：

> `These fields apply to sent, delivered, and read status webhooks.`

这句限定的是"这些字段"的适用范围——适用于 sent、delivered、read 三种状态回调，不是所有回调字段的通则。把它与缺席规则放在一起读：**失败回调缺的是联系人数组与部分接收方标识，剩下的关联抓手是状态标识**；缺字段不等于缺信息，缺的是官方在这一层不再回传的那几项。

## 4.5 群聊：逐用户回调与全员已读

群聊在两种口径里各有一套判定主体。回调侧的定义是按用户逐条：

> `The triggers above also apply to a WhatsApp user who is part of a group chat.`

也就是说，群聊不会把"整个群"压成一个状态；触发条件照样逐用户成立。用户端则按"全员"汇总：

> `In a group chat, the second check mark appears when everyone has received your message.`

两句话并不矛盾，但主体不同：**回调按单个用户给状态，界面按全员给符号**。同一页状态参考还写了群聊接收方的字段落点——发给群标识时，用户电话号码改放到群参与者标识里；发给 BSUID 且号码不可用时该字段省略（带字段名，只作中文陈述）。这两条是"同一条状态挂在不同主体上"的字段后果，本篇只登记，不把它扩成群成员管理规则。

## 4.6 状态回调里的计费字段与两个截止日期

状态参考页的后半段是计费字段。官方在这里同时保留了旧值与新值，并用两个截止日期切开：

> `Will only be set to this value if the webhook was sent before July 1, 2025.`

> `As of October 1, 2026, indicates a 1:1 service delivery within the business phone number's free tier.`

第一句是旧计费模型的退场时点：某个取值只在 2025 年 7 月 1 日之前发出的回调里才会出现。第二句是新模型的生效说明：从 2026 年 10 月 1 日起，某一类一对一服务投递落在免费额度内。同一页还有两句把"免费客服"的旧含义与群聊免费客服的旧含义分别截止在 2026 年 9 月 30 日（这两句带字段名，只作中文陈述）。四个日期摆在一起，读法是：**同一字段在不同时间窗里有不同含义，回调时间决定该读哪一套**；不能把 2025、2026 两个年份当成版本号，也不能把"截止"与"起算"读成同一个方向。另有一句说明某属性将在未来的版本化发布里弃用（带字段名，只作中文陈述），本篇只登记这条提醒，不预测弃用后的替代字段。

# 5 批量、重投、去重与丢弃

## 5.1 批量上限与"不保证批量"

回调不是一条事件一次请求。官方在端点页写了聚合规则：

> `POST requests are aggregated and sent in a batch with a maximum of 1000 updates.`

上限是一千条更新。但紧接着的一句把"上限"与"保证"分开了：

> `However, batching cannot be guaranteed so be sure to adjust your servers to handle each POST request individually.`

两句必须一起读：**上限讲的是单次请求最多装多少，不保证每次都会装满，也不保证一定会聚合**。官方因此要求服务端能把每次 POST 当成独立的一批来处理。把"最多一千条"读成"每次都是一千条"会直接读错两件事：批的次数不可预测，批与批之间的边界也不可预测。至于聚合窗口多长、按什么维度聚合，官方在这批页面里没有写，本篇不补。

## 5.2 失败后的第一次重投：立即，还是等一会儿

投递失败之后，官方在两页各写了一次重投，措辞不同。总纲页的写法是：

> `If a webhook request to your endpoint receives an HTTP status code other than 200, or if the webhook cannot be delivered for another reason, Meta retries delivery with decreasing frequency until the request succeeds, for up to 7 days.`

端点页的写法更具体：

> `If any POST request sent to your server fails, delivery is retried immediately, then a few more times with decreasing frequency over the next 7 days.`

两句话说的是同一个窗口，但端点页补出了**第一次重投的时点**：立即。总纲页只说"频率递减、最长七天"，没有写第一次是否立即；端点页把它写明。本篇按更具体的那一页写：失败后先立即重投一次，之后按递减的频率再投几次，总窗口最长七天。这不是两套期限，而是一处概括、一处细化；把它们并排登记在附表 F 的第一个议题里，是为了提醒读者不要拿概括句去否定细化句。

## 5.3 两个七天：重投窗口与丢弃时点

同一个端点页还有第二个七天：

> `Unacknowledged responses will be dropped after 7 days.`

这句管的是"未被确认的响应"。把它与上一节的重投句放在一起，会看到两个七天：一个是**失败重投的窗口**，一个是**未确认响应的丢弃时点**。它们不是同一个截止时间的两种说法：重投讲的是 Meta 还会不会继续尝试，丢弃讲的是已经拿到但没被确认的那份响应还会不会留着。官方在这两句里都没有写"七天从哪一刻起算"，也没有给重投的具体间隔数值；附表 F 把这两项分别登记为"官方只给定性描述"与"官方只写丢弃时点"，正文不推算。

## 5.4 去重责任在服务端

重投带来一个必然后果：同一条事件可能被送到不止一次。官方没有把"保证只送一次"写成承诺，而是把处理责任写明：

> `Your server should handle deduplication in these cases.`

"在这些情况下"指的是上一句的失败重投。官方的措辞是"应当"，不是"Meta 会替你去重"；这与第 5.1 节的"不保证批量"是同一种写法：**平台给的是投递行为，去重与幂等由接收方自己承担**。去重键选什么、保留多久、重复事件要不要重新处理，官方没有规定，本篇不替它设计；第 5.5 节只补一条与之相关的事实——回调数据没有历史接口可补取。

## 5.5 没有历史数据可补取

端点页在讲"捕获并消化"之后，紧接着写了一条否定式事实：

> `There are no APIs for fetching historical webhook data, so capture and store webhook payloads accordingly.`

这句话有两个作用。第一，它解释了为什么校验通过后要"先存"：错过一次请求，事后没有接口把它捞回来。第二，它把"回调"与"日志查询"彻底分开：回调是一次性投递，不是可回放的流水。官方没有写保存期限、没有写重放机制、也没有写补取窗口，这些都属于官方留白；本篇只登记"没有历史接口"这一条，不把它扩成"所以必须保存多久"的建议。

# 6 错误码：同步与异步、code 与 title

## 6.1 错误可能从两条通道回来

《WhatsApp error codes》是这批页面里最长的一页，它先处理"错误从哪里来"。官方的分类句把到达方式写成三类：同步地从接口响应回来、异步地从回调回来，或者两条通道同时出现（这一句带字段名与粗体提示，按引句规矩只作中文陈述）。紧接着建议同时监控接口响应与 messages 回调（同样带字段名，只作中文陈述）。落到回调这一侧，官方点名了可能承载错误的回调对象：

> `Cloud API errors can be surfaced in the following webhook objects:`

这里有一个容易混的点：**同步与异步不是两种错误类型，而是两条到达通道**；同一条业务失败可能只在其中一条出现，也可能两条都出现。把"收到异步回调"读成"接口一定成功"，或者把"接口报错"读成"回调一定不会来"，都不符合官方的写法。

## 6.2 回调载荷里的三处错误位置

错误在回调载荷里不是只有一处。messages 参考的句子是：

> `Errors in messages webhooks can be surfaced in three places:`

三处具体是哪三处，句子后面的列表按建池口径没有进入原句池，本篇不逐项抄写，也不凭经验补名；读者要逐项对照请回原页。与 messages 参考并列的还有《Errors messages webhooks reference》，它自己的任务声明是：

> `This reference describes trigger events and payload contents for the Messaging account **messages** webhook for errors messages.`

这一页的正文在池子里只有这一句可整句引用，其余内容以字段表为主，按口径整行剔除。**"三处"是载荷结构内的分类，"两条通道"是到达方式的分类**，两套分类的维度不同，不能互相换算；本篇只登记这一点，不在两者之间做映射。

## 6.3 错误处理按 code 与 details，不按 title

错误码总表给的第一条建议不是"记住所有错误码"，而是选定处理依据：

> `These properties and their values are more indicative of the underlying error.`

官方把 code 与 details 放在"更能指示底层错误"的位置上，把 title 放在"会最终弃用"的位置上；title 甚至没有独立属性，只作为消息值的一部分出现（这几句带字段名，只作中文陈述）。一个具体的例子可以说明"按码处理"的意义：

> `The API returns the message "Provide valid app ID".`

这句描述的是某个接口在缺少令牌时返回的文本；官方强调要按 code 处理，正是因为这类文本会变、会本地化、会随版本调整。本篇因此不在正文里维护"错误文本对照表"，只登记官方指定的依据字段。

## 6.4 错误总表里的时限与状态码

总表里还有一些可整句引用的具体条目，它们说明"错误处理"不全是即时的。例如某一类限制解除：

> `This restriction is automatically lifted after the enforcement period.`

又例如某一类同步动作的等待上限：

> `Ad synchronization can take up to 10 minutes.`

还有一条把两个错误区分开的说明：

> `This is distinct from error code 190 (expired or invalid token).`

这三句的共同点是：**错误不是只有"成功/失败"两态，还带时间与相似项**。限制会自动解除，同步会有等待窗口，两个看起来相近的错误可能指向不同的原因。官方没有给出一张"每类错误对应多久解除"的总表，本篇也不替它汇总；可整句引用的条目按附表 B 归档，其余以字段表形式留在原页。

## 6.5 同一套错误码在 MM API 里的增补

总表在后半段处理另一套接口的关系：

> `MM API for WhatsApp uses the same error codes as Cloud API, with a few additions listed below.`

MM API 与 Cloud API 共用错误码，只在这份总表里追加少数几条。这句话决定了本篇的读法：**错误码是跨接口共享的词汇，不是某一个接口私有的枚举**；看到同一个 code 时，先确认它来自哪套接口、再去看该接口有没有增补条目。官方没有写两套接口的增补是否会同步、弃用是否会同步，本篇不推断。

# 7 覆盖、安全与回声

## 7.1 覆盖只对列出的字段类型生效

第 2.3 节写了交替地址的兜底顺序，这里补它的适用范围。官方在覆盖页先限定字段类型：

> `The override applies only to the following webhook field types.`

"下列"指的是这一页紧随其后的一张字段类型清单；清单行按建池口径整行剔除，本篇不逐项抄。范围限定之后，官方才写检查顺序：

> `When a [supported field](#supported-webhook-fields) triggers a webhook, the system first checks if your app has designated an alternate callback URL for the business phone number associated with the event.`

> `If the business phone number has an alternate callback URL, the system sends the webhook there.`

也就是说，**覆盖不是"给应用再配一个地址"，而是"给某些字段类型、某些主体配地址"**：只有列出的字段类型才有资格走覆盖链，链上的主体是业务电话号码与消息账户两级。一个事件同时涉及号码与账户时，号码那级优先；号码没有交替地址才看账户。官方没有写"两级地址相同时会不会去重"，也没有写"一个事件挂多个号码时怎么办"，本篇不补。

## 7.2 不支持覆盖的两类，以及订阅侧的一个动作

覆盖页用一段提示点名了两类回调：模板类回调与账户级回调不支持交替地址，官方始终把它们送到应用默认地址。这一段带字段名与列举，按引句规矩只作中文陈述；紧接着的判断句可以整句引用：

> `Meta always delivers these webhooks to your app's default callback URL.`

另一句是范围的反面写法，把"没列出的字段类型"一律送回默认地址：

> `For field types not listed here, Meta always sends webhooks to your app's default callback URL.`

两句合起来，覆盖的边界就闭合了：**列出的类型走三级链，没列出的与点名的两类一律回默认**。订阅侧还有一个与撤销有关的动作：

> `Subscribing without post body parameters removes the alternate callback URL from the Messaging account, and Meta sends the Messaging account webhooks to the callback URL set in the App Dashboard again.`

不带请求体参数重新订阅，会把消息账户上的交替地址移除，账户级回调重新回到应用面板里设置的那个地址。官方没有写移除是即时生效还是延迟生效，也没有写移除后已经在途的回调会去哪里，本篇只登记这条动作与结果。

## 7.3 回声回调：撤回与编辑

《smb_message_echoes webhook reference》处理的不是平台发的消息，而是**使用 WhatsApp Business 应用的小商家自己发的消息**。官方用两个例子把这类回调的用途写出来：

> `This example payload describes a text message sent to a WhatsApp user by a business customer using the WhatsApp Business app.`

> `This example payload describes a business customer revoking (deleting) a previously sent message using the WhatsApp Business app.`

第一个例子是文本消息的回显；第二个例子是撤回。撤回的载荷里带原消息标识，官方在同一页写明撤回对象包含被删消息的标识（这一句带字段名，只作中文陈述）。编辑是第三种情形：

> `This example payload describes a business customer editing the caption of a previously sent image message using the WhatsApp Business app.`

编辑对象同样带原消息标识，另带一个包含更新后内容的消息对象（带字段名，只作中文陈述）。这类回调与第 4 章的状态回调不是一回事：**状态回调讲"平台把你发的消息送到哪一步"，回声回调讲"小商家在应用里改了自己发的那条消息"**；前者关心状态，后者关心内容变更。回声页里号码字段也会在用户名启用且号码不可用时省略，规则与 messages 参考同构，本篇不重复引用。

## 7.4 安全回调通知的是什么

《Security webhook reference》只有两句可整句引用，但这两句足够定界：

> `This reference describes trigger events and payload contents for the Messaging account **security** webhook.`

> `The **security** webhook notifies you of changes to a business phone number's security settings.`

安全回调通知的是**业务电话号码安全设置的变更**，不是消息状态，也不是账户计费。它与 messages、statuses、smb_message_echoes 并列，属于另一类字段；订阅了哪一类，才收哪一类的回调。官方没有在这一页列举"安全设置"具体包含哪些项，也没有写变更的回调条数与顺序，本篇只登记它的定义，不扩成安全设置清单。

# 8 用户端帮助中心：勾、等待提示与时间戳

## 8.1 三个勾与一个时钟

《How to check read receipts》把聊天界面上的符号逐个解释。官方先给总起句：

> `Here's what each one means: The message was successfully sent.`

"发送成功"对应一个勾；"送达"对应两个勾，且条件是送到收件人的手机或任一已关联设备：

> `The message was successfully delivered to the recipient's phone or any of their linked devices.`

"还没发出去或还没送到"对应一个时钟：

> `Seeing a clock means the message is not yet sent or delivered.`

已读则是两个蓝色勾（第 4.3 节已引）。把这几个符号排开，可以看出用户端的表达是**四个状态、四个符号**：单勾、双勾、双蓝勾、时钟；语音消息另有一个播放符号（麦克风），第 8.2 节带过。官方还写明勾与时钟的颜色不可更改（这一句带界面词，只作中文陈述）——这是一条界面事实，不是状态规则。

## 8.2 群聊里的第二个勾与回执缺失

群聊的送达判定在帮助中心有一句很具体的写法：

> `If the recipient's phone is off and the message isn’t delivered to any of their linked devices, the second check mark won’t appear.`

也就是说，收件人手机离线且没有任一台已关联设备收到时，第二个勾不出现。这与第 4.5 节的回调侧口径同构：界面按"是否到达某个设备"给符号，回调按"是否到达用户设备"给 delivered。回执不出现时，官方列了一组可能原因：对方或自己关了回执、对方没打开会话、连接问题、首次收到消息可能不发回执、设备时间设置不正确（这一组条目带界面词，只作中文陈述）。其中"首次可能不发回执"单独成句：

> `When a person gets your message for the first time, a read receipt might not be sent to you.`

这解释了一个常见误读：**没有蓝勾不等于对方没读**，回执本身可能没发出来。另一条原因则直接把时间问题指出来：

> `Missing read receipts If you don't see two blue check marks, a blue microphone, or Opened next to your sent message or voice message: You or your recipient might have disabled read receipts in the privacy settings.`

回执缺失与时间戳异常在这句话里交叉了一次，第 8.5 节单独写时间戳。

## 8.3 关闭回执的范围

关闭回执不是"一键全关"。官方写了两条限制：

> `This won't disable the read receipts for group chats or play receipts for voice messages.`

> `There's no way to turn those settings off.`

群聊的已读回执与语音消息的播放回执不受这个开关影响，而且官方明确说"没有办法关掉这两项"。另一句补了关闭的对称后果：

> `If you turn off read receipts, you won’t be able to see read receipts from others.`

也就是说，关闭回执是**双向**的：你看不到别人，别人也看不到你。这三句合起来，说明用户端的回执开关不是一个单点的隐私选项，而是带着不可关闭项与双向后果的一组规则；开发者文档的状态回调不受这个开关控制，两套口径各管一层（第 9.1 节再收）。

## 8.4 "Waiting for this message" 的成因

《About "Waiting for this message. This may take a while."》解释的是聊天里出现的占位提示。官方先写它出现的位置：

> `There are a few issues that might cause an incoming message to be replaced with "Waiting for this message.`

也就是说，它替换的是一条**本该到达却没能解密或没能取回**的消息。官方在同一页先声明端到端加密：

> `WhatsApp protects your personal messages and calls with end-to-end encryption.`

这句是理解占位提示的前提：消息在传输与存储层被加密，接收端拿不到能解开的内容时，界面只能用占位提示代替。官方列出的成因包括最近重装、版本过旧、换到同一号码的新主设备等（这几句只作中文陈述）；其中与设备绑定有关的一句是：

> `You need to log in to your primary phone every 14 days to keep linked devices connected to your WhatsApp account.`

十四天的期限接在"保持关联设备连接"上，是帮助中心自己的时限，不是回调重投的七天，也不是媒体 ID 的三十天；四个数字分属四个对象，不能互相套用。官方随后给了一组排查方向（联网、连接、更新、让发送方重发、切回主设备、重新关联设备），本篇只点名这几类，不重述点击次序。

## 8.5 时间戳异常与设备时间

《Fix incorrect timestamps on messages》处理的是"消息旁边的时间不对"。官方先定义时间戳：

> `A timestamp indicates the time a message was sent or received.`

时间戳标记的是发送或接收的时刻。时间不对时，官方把原因指向设备侧：

> `If the timestamp on messages you’ve sent or received is wrong, please check the time and time zone configuration on your device and adjust them.`

设备的时间与时区配置是排查起点；官方建议把日期与时间设为自动，由移动运营商下发正确时间（这一句带界面词，只作中文陈述）。另有一句说明手动时区的用途：

> `This can help if your network sometimes provides the wrong time zone.`

这三句把时间戳问题限定在**用户设备层**：官方没有把时间戳异常归因于服务器时钟，也没有给"回调里的时间字段与界面时间不一致"的对照规则。回调载荷里的时间字段以哪一侧为准，这批登记页面没有写，本篇不推断。

# 9 横向对照：同一件事的三种写法

## 9.1 同词不同层：回调、状态、送达、覆盖

把这批页面横向排开，最先要分开的是几组"同一个词，在两个层上各说各的"。

**回调（webhook）与回调字段（webhook field）**：前者是机制，后者是可以逐项订阅的字段名。官方在总纲与端点页说机制，在参考页与覆盖页说字段；"收到了回调"不等于"订阅了所有字段"，订阅范围决定事件会不会被送到端点。**事件（event）与状态（status）**：事件是触发原因，状态是外发消息在回调里的取值；同一事件可以产生多条状态回调，一条状态回调只对应一条外发消息的一个阶段。**送达（delivery）与送达（delivery）**：中文里是同一个词，英文也是同一个词，但一处指消息到达用户设备，一处指回调到达你的端点；本篇在前面各章一律写全"消息送达"或"回调投递"，就是为了不在这一层混。

**已读（read）与回执（read receipt）**：回调里的 read 是状态值，前提是已送达；用户端的回执是界面行为，可能因为关闭开关、首次接收、连接问题而不出现。两者不是同一个开关的两端：**关闭回执不会改写回调里的状态定义，收到 read 状态也不等于用户端一定显示了蓝勾**。**覆盖（override）与配置（configuration）**：覆盖是给特定字段类型与特定主体指定交替地址，不是把所有回调都改道；模板类与账户级回调始终走默认地址。**错误（error）与失败（failed）**：failed 是消息状态的一个值，error 是接口或回调带回的错误标识；一条消息可以 failed 而没有可读的错误码，一个错误码也可以出现在接口响应而不产生 failed 状态。

这六组词在不同页面上各有各的上下文，官方没有一页把它们并排定义。本篇的读法是：**先看词落在哪一层，再看那一层的页怎么写**；不要用一个层的定义去解释另一个层的现象（第 10.6 节会把最常用的一组再收一次）。

## 9.2 附表 E 的读法：三十四格有句，一百六十二格是空

附表 E 把同一组 **28** 个维度并排放在 **7** 列主体上，共 **196** 格：端点页、总纲与覆盖页、状态参考页、消息与错误参考页、回声与安全参考页、错误码总表页、用户帮助中心。**34** 格取到了官方原句，**162** 格标作"官方未按这一列写该项"，去重引用页 **11** 个，同一句跨列复用 **1** 次。

空格的含义只有一个：官方在这批登记页面里**没有按这一列写这一项**。它不代表该主体没有这项规则，也不代表这一项不存在；它只说明这一列的页面范围内找不到一句能对上这一项的原文。例如"已读的界面符号"这一维度，状态参考页有对应原句，帮助中心也有对应原句，而端点页与错误码总表页在这一列是空——这不是说端点与错误码不涉及已读，而是说这两页没有按"界面符号"这个角度写。读这张表时，**先看列的定义，再看格里的页与句序**；空格是"这一页没这样写"，不是"这一项不存在"。

# 10 容易读错的六处

## 10.1 三条状态回调是上限，不是固定条数

"每个外发消息最多三条独立回调"里的**最多**是上限。官方在同页点明：当已读同时蕴含送达时，送达那条不发。把三条当成固定条数，会把正常的省略误判成丢包；反过来，把省略当成"官方只发两条"，也会在需要三条的场景里错判。正确读法是：三条是上限，前提与例外决定实际条数。

## 10.2 两个七天不是同一个期限

端点页里有两个七天：失败重投的窗口，与未确认响应的丢弃时点。前者回答"Meta 还会不会再试"，后者回答"已经拿到的响应还会不会留着"。它们相邻、同页、同一个数字，但对象不同；再加上帮助中心"十四天保持关联设备连接"与状态回调里的两个日期节点，这批页面里至少有五个带数字的时间口径，彼此不能换算（第 8.4 节已把十四天单独拎出）。

## 10.3 三处错误位置不是三条通道

回调载荷里的"三处"是载荷结构内的位置分类；"同步与异步"是错误到达方式的分类。两套分类维度不同：一处可以同时是同步与异步都可能出现的，一条通道也可能承载多处的错误。把三处读成三条通道，会得到"三种错误类型"的错觉；官方没有做这种映射，本篇也不做。

## 10.4 覆盖只对列出的字段类型生效

交替地址不是"全量改道"。列出的字段类型才走三级兜底链；没列出的、以及模板类与账户级回调，一律送回应用默认地址。把覆盖当成"给应用换个回调地址"，会让模板回调被错误地期待发到交替地址；把"没收到"当成"覆盖失效"，也会误判。

## 10.5 握手校验与签名校验不是同一套

GET 握手比的是验证令牌：它回答"这个端点是不是你的"，决定投递能不能开始。POST 签名比的是哈希：它回答"这条载荷有没有被改过"，决定单条载荷是否有效。两次校验的比对对象、触发时机与失败后果都不同；一次通过不代表另一次会通过，一次失败也不能拿另一次来补救。官方把它们写在同一页的两节里，正是为了让读者分开读。

## 10.6 用户端的勾与回调的状态不是同一层

用户端的勾是界面符号，回调的状态是字段取值。官方用界面符号解释状态值，这是一种**等价说明**，不是把两层合并：delivered 对应两个勾，但两个勾的出现还取决于"送达某台设备"与"回执开关"等界面条件；read 对应两个蓝勾，但蓝勾的条件是"所有人都已读"，而回调的 read 是逐用户的状态。把界面现象当成回调状态的充分条件，或者把回调状态当成界面一定显示的保证，都会读错。

# 11 结论与边界

## 11.1 这条链路的内部边界

沿一条回调走完，可以得到一条清楚的内部边界：**回调是事件触发的请求，不是可查询的日志；端点的有效性由握手决定，载荷的有效性由签名决定；消息状态有前提、有上限、有例外；投递有批量上限、有重投窗口、有去重责任、有丢弃时点；错误有两条到达通道、两套分类维度、两种标识依据；覆盖有范围、有兜底顺序、有点名排除；用户端的符号是另一层，不能反推所有状态。**

这条边界之外，官方在这批页面里没有写的部分同样清楚：重投的具体间隔数值、签名密钥的轮换方式、去重键的选择与保留期限、未确认响应在七天内的存放形态、错误文本的稳定性承诺、回调时间字段与界面时间的对照规则。这些不是本篇漏掉的内容，而是官方文本在这批登记页面里没有给出句子；附表 F 把它们逐条登记为留白，正文不替官方补结论。

## 11.2 本篇的边界

本篇只读官方文本，写的是**口径**：哪个词在哪一页指什么、哪条规则带什么前提、哪两个数字不能合并。它不是接入指南，不含端点代码、验签实现、重试队列参数、去重表结构；也不是运营方案，不含任何利用回调、重投或错误码向非自愿收件人投递消息的方法。凡涉及"怎么做"的部分，本篇只点名与计数，把动作留给原页；凡涉及"该不该"的评价，本篇不写。读者若要把这些口径落到实现，请以官方页面为准，并自行承担与平台条款、当地法律相关的那部分判断。

# 附表 A 官方页面登记清单与本篇引用位置（13 行）

登记口径与系列前篇一致：**凡本篇抓取的页面，一律登记**。全表 **12** 个登记位，按站点分为 Meta 开发者文档 9、WhatsApp 帮助中心 3。取回状态由脚本实测各层正文的非空白字符数得出，分档统计：已取回 12。其中**可引用 12 页、不可引用 0 页**：本轮登记页正文全部取回，没有需要标注「全篇不引用」的页面。

帮助中心页的正文取回长度按裁剪后正文计：抓取原始页含平台页签（Web／Windows／Mac／iOS／Android）与固定尾栏，这些行不计入长度，也不作为引文出处。Meta 商业帮助中心的页面正文从页面内嵌的转义 JSON 载荷里抽取，抽取残骸（单独的 u 记号与 DOM 类名）整行剔除，不做猜测修补，被剔除的行也不作为引文出处。

第 4 列「本篇引用位置」的小节号是实测的，不是事先规划的：由 _tmp/wa6_postitle.pl 扫成稿分片里的页名标记（正文每点名一次某页的官方标题就留下一枚这种内部标记，展开后成为标题）得出，写进 _tmp/w6_a9_postitle.tsv，再由 _tmp/wa6_pos.pl 拼进这一列；判据与 _tmp/wa6_cover.pl 同口径——**这一列只写标题点名的位置，所以按小节号翻过去一定能撞见这一页的官方标题**。章首（未进入任何小节的章引言）写作「第 N 章章首」；某页只在别处被整句引用而标题未点名的节不出现在这一列，那种句子按附表 C—F 的句序回查。

| 站点 | 页面标识 | 官方标题 | 本篇引用位置 | 正文状态 |
| --- | --- | --- | --- | --- |
| Meta 开发者文档 | `support_error_codes_md` | WhatsApp error codes | 正文 6.1、附表 B（1 处）、附表 C（11 处）、附表 E（4 处）、附表 F（1 处） | 已取回（正文 32277 字符） |
| Meta 开发者文档 | `webhooks_create_webhook_endpoint_md` | Create a webhook endpoint | 正文 1.1、2.2、附表 B（1 处）、附表 C（21 处）、附表 E（13 处）、附表 F（6 处） | 已取回（正文 5261 字符） |
| Meta 开发者文档 | `webhooks_override_md` | Webhook overrides | 正文 2.3、附表 C（4 处）、附表 E（2 处）、附表 F（2 处） | 已取回（正文 8610 字符） |
| Meta 开发者文档 | `webhooks_overview_md` | WhatsApp webhooks | 正文 1.1、2.1、附表 B（1 处）、附表 C（2 处）、附表 E（2 处）、附表 F（1 处） | 已取回（正文 13859 字符） |
| Meta 开发者文档 | `webhooks_reference_messages_errors_md` | Errors messages webhooks reference | 正文 6.2 | 已取回（正文 2741 字符） |
| Meta 开发者文档 | `webhooks_reference_messages_md` | messages webhook reference | 正文 1.1、2.4、附表 C（2 处）、附表 E（4 处）、附表 F（4 处） | 已取回（正文 5511 字符） |
| Meta 开发者文档 | `webhooks_reference_messages_status_md` | Status messages webhook reference | 正文 4.1、附表 B（6 处）、附表 C（3 处）、附表 D（4 处）、附表 E（3 处）、附表 F（4 处） | 已取回（正文 20292 字符） |
| Meta 开发者文档 | `webhooks_reference_security_md` | Security webhook reference | 正文 7.4、附表 D（1 处）、附表 E（1 处） | 已取回（正文 2515 字符） |
| Meta 开发者文档 | `webhooks_reference_smb_message_echoes_md` | smb_message_echoes webhook reference | 正文 7.3、附表 C（1 处）、附表 E（1 处） | 已取回（正文 10480 字符） |
| WhatsApp 帮助中心 | `faq_3398056720476987` | About "Waiting for this message. This may take a while." | 正文 8.4、附表 B（1 处）、附表 C（5 处）、附表 E（1 处） | 已取回（正文 2410 字符） |
| WhatsApp 帮助中心 | `faq_529932872293332` | Fix incorrect timestamps on messages | 正文 8.5、附表 C（1 处）、附表 E（1 处） | 已取回（正文 1604 字符） |
| WhatsApp 帮助中心 | `faq_665923838265756` | How to check read receipts | 正文 8.1、附表 C（7 处）、附表 E（2 处）、附表 F（1 处） | 已取回（正文 2561 字符） |
| **合计** | **12** 个登记位 | 可引用 **12** 页／不可引用 0 页 | Meta 开发者文档 9、WhatsApp 帮助中心 3 | 不可引用页全篇不引用 |

#ROWS=12 COLS=5


# 附表 B 数字归档五档（11 行）

归档口径：**本表一行一个「数字＋它数的东西＋出处页与句序」**，数字、单位词与整句都由脚本从语料里实测取出——输入是原句池的 **204** 条整句（覆盖 **12** 个页面，池子已剔除含竖线的「正文黏表格」混合句），脚本在每句里找出所有独立数字，取该数字后面最多四个词，档位由这四个词里第一个能对上单位表的词决定；同页同数字同单位只留一行（共去重 **4** 行）。全表 **10** 行，分布在 **5** 个页面上，去重后的「数字＋单位」组合 **9** 组，单位词 **6** 种。五档分布：一、账号、号码与设备规模档位 2／三、时长与滚动窗口 4／五、速率、次数与比率口径 1／四、生效日期与版本节点 3。

三条不成文规矩在这一表里最要紧：**同一个数字在两个页面各写一处，就各登记一行**，不合并成区间；**官方写成区间、多档或带限定词的照原样登记**（`up to`、`about`、`approximately`、`in some countries` 一类措辞原样保留），不取中值、不去掉约数；**档位与性质由脚本按关键词判定，性质判不出来就中止出表**，所以本表没有「其它」这一档。

本表也写清了自己漏掉了什么：池子里 **183** 句整句不含数字，脚本共扫出 **5** 个「数字后面四个词都对不上任何一档」的实例，全部连同它所在的整句写进 `_tmp/w6_a9_bdrop.tsv` 供复核，不进表——这些实例的共同点是紧跟数字的词不在任何一档的单位表里，本表不为它们硬造档位，**漏掉的判定权在读者手里，不在这张表里**。另有 **0** 个可引用页面在池子里一句都没有，本表因此整页缺席，它们在附表 A 里已逐页点名。

官方原句超过 118 字符按整词截断并以省略号结尾，共 **7** 行截断；截断只发生在表格里，进表的每一句都先用未截断的整句逐字回表到`_tmp/w6_a9s/` 的对应页面，失败 **0** 句（回表与建池共用同一套规范化，所以这里一旦非零就是池子与抓取件不同步，脚本直接中止不出表）。性质判定为 **6** 种：上限（官方写成 up to／maximum 一类） 2／下限或触发门槛 1／时长（官方只写数值与单位） 1／月份与号数写明的日期节点 1／申诉与复核时长 4／自动解除或自动生效的时限 1。

| 档 | 数字 | 计数单位（官方原文） | 官方原句 | 性质 | 出处页 |
|---|---|---|---|---|---|
| 三、时长与滚动窗口 | **14** | days | 「You need to log in to your primary phone every 14 days to keep linked devices connected to your WhatsApp account.」 | 时长（官方只写数值与单位） | `faq_3398056720476987`｜第 27 句 |
| 三、时长与滚动窗口 | **10** | minutes | 「Ad synchronization can take up to 10 minutes.」 | 上限（官方写成 up to／maximum 一类） | `support_error_codes_md`｜第 250 句 |
| 三、时长与滚动窗口 | **7** | days | 「If any POST request sent to your server fails, delivery is retried immediately, then a few more times with decreasing…」 | 下限或触发门槛 | `webhooks_create_webhook_endpoint_md`｜第 61 句 |
| 三、时长与滚动窗口 | **7** | days | 「If a webhook request to your endpoint receives an HTTP status code other than 200, or if the webhook cannot be…」 | 上限（官方写成 up to／maximum 一类） | `webhooks_overview_md`｜第 53 句 |
| 四、生效日期与版本节点 | **1** | date | 「Will only be set to this value if the webhook was sent before July 1, 2025.」 | 月份与号数写明的日期节点 | `webhooks_reference_messages_status_md`｜第 70 句 |
| 四、生效日期与版本节点 | **2026** | year | 「As of October 1, 2026, this includes 1:1 utility messages sent within an open customer service window, group utility…」 | 自动解除或自动生效的时限 | `webhooks_reference_messages_status_md`｜第 77 句 |
| 四、生效日期与版本节点 | **30** | date | 「`free_customer_service` — Through September 30, 2026, indicates a utility template message or non-template message…」 | 申诉与复核时长 | `webhooks_reference_messages_status_md`｜第 78 句 |
| 五、速率、次数与比率口径 | **2026** | template | 「`free_customer_service` — Through September 30, 2026, indicates a utility template message or non-template message…」 | 申诉与复核时长 | `webhooks_reference_messages_status_md`｜第 78 句 |
| 一、账号、号码与设备规模档位 | **30** | group | 「`free_group_customer_service` — Through September 30, 2026, indicates a group utility or non-template message…」 | 申诉与复核时长 | `webhooks_reference_messages_status_md`｜第 81 句 |
| 一、账号、号码与设备规模档位 | **2026** | group | 「`free_group_customer_service` — Through September 30, 2026, indicates a group utility or non-template message…」 | 申诉与复核时长 | `webhooks_reference_messages_status_md`｜第 81 句 |
| **合计** | **10** 行 | 单位词 **6** 种 | 去重后数字＋单位 **9** 组，覆盖 **5** 页 | 性质 **6** 种 | 第四档＝日期与版本节点 |

#ROWS=10 COLS=6


# 附表 C 官方用词与写下它的站点类别（57 行）

取格口径：脚本按第一、二列的 **57** 个词（组）（分属 8 个主题组：A　回调本体 8／B　订阅与字段 6／C　校验与传输 8／D　状态机 8／E　重投与去重 7／F　错误面 9／G　覆盖与回声 6／H　用户端 5）逐句扫过原句池的 **204** 条整句（覆盖 **12** 个页面），每命中一次就落进「词 × 站点类别」的格子；**一行一个非空格**，全表 **56** 行，各格命中句次相加是 **318** 次，而去重后真正被任一词命中的句子是 **135** 句（占全池 66.2%）。同一句可以落进多格：本轮一句落进最多格子的是 `webhooks_create_webhook_endpoint_md` 池内序号 36，同时命中 8 个词（组）——「JSON」、「endpoint」、「event」、「field」、「payload」、「subscribe」、「webhook」、「webhook field」；全池有 91 句同时落进两个以上的格子。所以句次合计 **318** 大于去重句数 **135**，这两个数各自回答「这一格有多少句」与「整池有多少句被扫中」，不矛盾。

词表本身也是这一篇的一部分：它写在 `_tmp/w6_a9_verb.tsv` 里，脚本不内置任何选题词，换一篇只换词表、判定规则不变。**正则由脚本逐条编译，编译不过就中止出表**，不存在「写错的正则悄悄扫出零句然后当成官方没写过」。

代表句一列取该格在池子里最先出现的一句，取句规则写死在脚本的遍历顺序里，**不是按哪句更清楚来挑**。「格内有句写明数值」只回答这一格有没有句子自带天数、小时、位数、百分比或规模数——有则记「有」，没有就记「无」，不在这里把别的页面或别的站的数字补进来；跨站对撞属于附表 F。

词零命中的情况：57 个词（组）里 **9** 个在整池一句都没出现（`app secret`、`HMAC`、`statuses array`、`acknowledge`、`error subcode`、`fbtrace_id`、`rate limit`、`throughput`、`message echoes`）。零命中不等于官方没有这一项内容，只说明本篇登记的这批页面没有用这个词写过。

本表的英文原句全部逐字回表：204 条整句每一条都在 `_tmp/w6_a9s/` 的对应页面正文里按规范化后的子串核到，失败 **0** 句（判定与建池用同一套实体还原与空白折叠，见 `_tmp/wa6_lib.pl` 的 ck）。官方原句超过 130 字符按整词截断并以省略号结尾，共 **28** 行截断；截断只发生在表格里，回表核验用的是未截断的整句。各站点命中句次合计：Meta 开发者文档 270／WhatsApp 帮助中心 48／Meta 商业帮助中心 0／法律与政策页 0。

| 主题组 | 官方词（组） | 写下它的站点类别 | 命中句次 | 命中页数 | 代表句（该格池内第一句） | 格内有句写明数值 | 出处页｜句序 |
|---|---|---|---|---|---|---|---|
| A　回调本体 | webhook | Meta 开发者文档 | **58** | 9 | 「However, Meta recommends that you do not rely on titles for your error handling logic as titles will eventually be deprecated.…」 | 有 | `support_error_codes_md`｜第 7 句 |
| A　回调本体 | callback URL | Meta 开发者文档 | **11** | 2 | 「Anytime you [set or edit the Callback URL field or the Verify token field](#configure-webhooks) in the App Dashboard,…」 | 无 | `webhooks_create_webhook_endpoint_md`｜第 13 句 |
| A　回调本体 | endpoint | Meta 开发者文档 | **14** | 3 | 「This error occurs on certain GET endpoints (such as `whatsapp_business_profile`) when no token is included.」 | 有 | `support_error_codes_md`｜第 48 句 |
| A　回调本体 | payload | Meta 开发者文档 | **17** | 7 | 「In general, Meta recommends that you build your app's error handling logic around `code` values and `details` payload properties.」 | 无 | `support_error_codes_md`｜第 4 句 |
| A　回调本体 | JSON | Meta 开发者文档 | **2** | 2 | 「Anytime a webhook event is triggered for any webhook fields you are subscribed to, Meta sends a POST request to your webhook…」 | 无 | `webhooks_create_webhook_endpoint_md`｜第 36 句 |
| A　回调本体 | HTTP request | Meta 开发者文档 | **1** | 1 | 「Webhooks are HTTP requests containing JSON payloads that Meta's servers send to a server of your designation.」 | 无 | `webhooks_overview_md`｜第 4 句 |
| A　回调本体 | event | Meta 开发者文档 | **7** | 7 | 「Anytime a webhook event is triggered for any webhook fields you are subscribed to, Meta sends a POST request to your webhook…」 | 无 | `webhooks_create_webhook_endpoint_md`｜第 36 句 |
| A　回调本体 | server | Meta 开发者文档 | **10** | 3 | 「Before you can use your app in a production capacity, you must create and configure your own webhook endpoint on a public server…」 | 有 | `webhooks_create_webhook_endpoint_md`｜第 4 句 |
| B　订阅与字段 | subscribe | Meta 开发者文档 | **4** | 3 | 「If you are subscribed to the `messages` webhook field, you will receive notification of errors as they occur for supported…」 | 无 | `support_error_codes_md`｜第 9 句 |
| B　订阅与字段 | webhook field | Meta 开发者文档 | **3** | 3 | 「If you are subscribed to the `messages` webhook field, you will receive notification of errors as they occur for supported…」 | 无 | `support_error_codes_md`｜第 9 句 |
| B　订阅与字段 | messages webhook | Meta 开发者文档 | **1** | 1 | 「Notice that in this case, the `conversation` object is omitted because it's a v24.0 webhook, and the `pricing` object is omitted…」 | 无 | `webhooks_reference_messages_status_md`｜第 107 句 |
| B　订阅与字段 | field | Meta 开发者文档 | **11** | 5 | 「If you are subscribed to the `messages` webhook field, you will receive notification of errors as they occur for supported…」 | 无 | `support_error_codes_md`｜第 9 句 |
| B　订阅与字段 | account-level webhook | Meta 开发者文档 | **1** | 1 | 「> Note: Template webhooks (`message_template_status_update`, `message_template_quality_update`,…」 | 无 | `webhooks_override_md`｜第 26 句 |
| B　订阅与字段 | template webhook | Meta 开发者文档 | **1** | 1 | 「> Note: Template webhooks (`message_template_status_update`, `message_template_quality_update`,…」 | 无 | `webhooks_override_md`｜第 26 句 |
| C　校验与传输 | verify token | Meta 开发者文档 | **1** | 1 | 「Anytime you [set or edit the Callback URL field or the Verify token field](#configure-webhooks) in the App Dashboard,…」 | 无 | `webhooks_create_webhook_endpoint_md`｜第 13 句 |
| C　校验与传输 | challenge | Meta 开发者文档 | **2** | 1 | 「If the request is valid, respond with HTTP status `200` and the `hub.challenge` value.」 | 无 | `webhooks_create_webhook_endpoint_md`｜第 30 句 |
| C　校验与传输 | signature | Meta 开发者文档 | **1** | 1 | 「Compare your generated hash to the hash assigned to the `X-Hub-Signature-256` header (everything after `sha256=`).」 | 无 | `webhooks_create_webhook_endpoint_md`｜第 50 句 |
| C　校验与传输 | mTLS | Meta 开发者文档 | **2** | 2 | 「Webhooks support mutual TLS (mTLS) for added security.」 | 无 | `webhooks_create_webhook_endpoint_md`｜第 9 句 |
| C　校验与传输 | TLS | Meta 开发者文档 | **3** | 2 | 「Your webhook endpoint server must have a valid TLS or SSL digital security certificate, correctly configured and installed.」 | 无 | `webhooks_create_webhook_endpoint_md`｜第 6 句 |
| C　校验与传输 | certificate | Meta 开发者文档 | **2** | 1 | 「Your webhook endpoint server must have a valid TLS or SSL digital security certificate, correctly configured and installed.」 | 无 | `webhooks_create_webhook_endpoint_md`｜第 6 句 |
| D　状态机 | status | Meta 开发者文档 | **17** | 4 | 「If the request is valid, respond with HTTP status `200` and the `hub.challenge` value.」 | 有 | `webhooks_create_webhook_endpoint_md`｜第 30 句 |
| D　状态机 | status | WhatsApp 帮助中心 | **2** | 1 | 「If you turn read receipts off, you might not be able to see who views your status.」 | 无 | `faq_665923838265756`｜第 38 句 |
| D　状态机 | sent | Meta 开发者文档 | **25** | 5 | 「If your webhook endpoint responds with anything else, however, Meta will consider your webhook endpoint unverified, and webhooks…」 | 有 | `webhooks_create_webhook_endpoint_md`｜第 34 句 |
| D　状态机 | sent | WhatsApp 帮助中心 | **8** | 3 | 「This means that only you and the person you’re communicating with can read, listen to, or share messages sent between you.」 | 无 | `faq_3398056720476987`｜第 9 句 |
| D　状态机 | delivered | Meta 开发者文档 | **12** | 3 | 「If a webhook request to your endpoint receives an HTTP status code other than 200, or if the webhook cannot be delivered for…」 | 有 | `webhooks_overview_md`｜第 53 句 |
| D　状态机 | delivered | WhatsApp 帮助中心 | **5** | 1 | 「The message was successfully delivered to the recipient's phone or any of their linked devices.」 | 无 | `faq_665923838265756`｜第 4 句 |
| D　状态机 | read | Meta 开发者文档 | **6** | 2 | 「These webhooks don't describe the contents of the outgoing message itself, only its status, and each outgoing message can have up…」 | 无 | `webhooks_reference_messages_md`｜第 15 句 |
| D　状态机 | read | WhatsApp 帮助中心 | **11** | 2 | 「This means that only you and the person you’re communicating with can read, listen to, or share messages sent between you.」 | 无 | `faq_3398056720476987`｜第 9 句 |
| D　状态机 | played | Meta 开发者文档 | **1** | 1 | 「`played` — Indicates the first time a voice message is played by the WhatsApp user's device. - WhatsApp UI equivalent: Blue…」 | 无 | `webhooks_reference_messages_status_md`｜第 86 句 |
| D　状态机 | played | WhatsApp 帮助中心 | **3** | 1 | 「Message info screen The message info screen displays details of when your sent message was delivered, read, or played by the…」 | 无 | `faq_665923838265756`｜第 13 句 |
| D　状态机 | failed | Meta 开发者文档 | **3** | 2 | 「Failed status webhooks omit the `contacts` array.」 | 无 | `webhooks_reference_messages_md`｜第 30 句 |
| D　状态机 | failed | WhatsApp 帮助中心 | **1** | 1 | 「Ask the sender to resend the message If the message failed to deliver, ask the sender to resend the message.」 | 无 | `faq_3398056720476987`｜第 24 句 |
| D　状态机 | checkmark | Meta 开发者文档 | **2** | 1 | 「Values can be: `delivered` — Indicates message was successfully delivered to the WhatsApp user's device. - WhatsApp UI…」 | 无 | `webhooks_reference_messages_status_md`｜第 84 句 |
| E　重投与去重 | retry | Meta 开发者文档 | **2** | 2 | 「If any POST request sent to your server fails, delivery is retried immediately, then a few more times with decreasing frequency…」 | 有 | `webhooks_create_webhook_endpoint_md`｜第 61 句 |
| E　重投与去重 | decreasing frequency | Meta 开发者文档 | **2** | 2 | 「If any POST request sent to your server fails, delivery is retried immediately, then a few more times with decreasing frequency…」 | 有 | `webhooks_create_webhook_endpoint_md`｜第 61 句 |
| E　重投与去重 | batch | Meta 开发者文档 | **2** | 1 | 「POST requests are aggregated and sent in a batch with a maximum of 1000 updates.」 | 无 | `webhooks_create_webhook_endpoint_md`｜第 59 句 |
| E　重投与去重 | deduplication | Meta 开发者文档 | **1** | 1 | 「Your server should handle deduplication in these cases.」 | 无 | `webhooks_create_webhook_endpoint_md`｜第 62 句 |
| E　重投与去重 | drop | Meta 开发者文档 | **1** | 1 | 「Unacknowledged responses will be dropped after 7 days.」 | 有 | `webhooks_create_webhook_endpoint_md`｜第 63 句 |
| E　重投与去重 | 7 days | Meta 开发者文档 | **3** | 2 | 「If any POST request sent to your server fails, delivery is retried immediately, then a few more times with decreasing frequency…」 | 有 | `webhooks_create_webhook_endpoint_md`｜第 61 句 |
| F　错误面 | error | Meta 开发者文档 | **15** | 4 | 「In general, Meta recommends that you build your app's error handling logic around `code` values and `details` payload properties.」 | 无 | `support_error_codes_md`｜第 4 句 |
| F　错误面 | error code | Meta 开发者文档 | **2** | 1 | 「This is distinct from error code 190 (expired or invalid token).」 | 无 | `support_error_codes_md`｜第 50 句 |
| F　错误面 | details | Meta 开发者文档 | **1** | 1 | 「In general, Meta recommends that you build your app's error handling logic around `code` values and `details` payload properties.」 | 无 | `support_error_codes_md`｜第 4 句 |
| F　错误面 | details | WhatsApp 帮助中心 | **1** | 1 | 「Message info screen The message info screen displays details of when your sent message was delivered, read, or played by the…」 | 无 | `faq_665923838265756`｜第 13 句 |
| F　错误面 | title | Meta 开发者文档 | **2** | 1 | 「Code titles, which do not have a dedicated property in API error response payloads, are included as part of the `message` value.」 | 无 | `support_error_codes_md`｜第 6 句 |
| F　错误面 | synchronously | Meta 开发者文档 | **1** | 1 | 「However, Meta recommends that you do not rely on titles for your error handling logic as titles will eventually be deprecated.…」 | 无 | `support_error_codes_md`｜第 7 句 |
| G　覆盖与回声 | override | Meta 开发者文档 | **4** | 1 | 「Meta sends webhooks to the callback URL set on your app, but you can override the default callback URL by designating an…」 | 无 | `webhooks_override_md`｜第 3 句 |
| G　覆盖与回声 | alternate callback | Meta 开发者文档 | **7** | 1 | 「Meta sends webhooks to the callback URL set on your app, but you can override the default callback URL by designating an…」 | 无 | `webhooks_override_md`｜第 3 句 |
| G　覆盖与回声 | revoke | Meta 开发者文档 | **1** | 1 | 「The `revoke` object contains the `original_message_id` of the message being deleted.」 | 无 | `webhooks_reference_smb_message_echoes_md`｜第 44 句 |
| G　覆盖与回声 | edit | Meta 开发者文档 | **3** | 2 | 「Anytime you [set or edit the Callback URL field or the Verify token field](#configure-webhooks) in the App Dashboard,…」 | 无 | `webhooks_create_webhook_endpoint_md`｜第 13 句 |
| G　覆盖与回声 | edit | WhatsApp 帮助中心 | **1** | 1 | 「Read receipts reset when you edit a message, so you can see who viewed your updates.」 | 无 | `faq_665923838265756`｜第 9 句 |
| G　覆盖与回声 | security | Meta 开发者文档 | **5** | 3 | 「Your webhook endpoint server must have a valid TLS or SSL digital security certificate, correctly configured and installed.」 | 无 | `webhooks_create_webhook_endpoint_md`｜第 6 句 |
| H　用户端 | read receipt | WhatsApp 帮助中心 | **7** | 1 | 「Read receipts reset when you edit a message, so you can see who viewed your updates.」 | 无 | `faq_665923838265756`｜第 9 句 |
| H　用户端 | message info | WhatsApp 帮助中心 | **3** | 1 | 「Message info screen The message info screen displays details of when your sent message was delivered, read, or played by the…」 | 无 | `faq_665923838265756`｜第 13 句 |
| H　用户端 | waiting for this message | WhatsApp 帮助中心 | **3** | 1 | 「Can’t join group with invite link How to free up storage on WhatsApp How to change your ringtone for WhatsApp How to use dark…」 | 无 | `faq_3398056720476987`｜第 5 句 |
| H　用户端 | timestamp | WhatsApp 帮助中心 | **2** | 1 | 「A timestamp indicates the time a message was sent or received.」 | 无 | `faq_529932872293332`｜第 4 句 |
| H　用户端 | end-to-end encryption | WhatsApp 帮助中心 | **1** | 1 | 「WhatsApp protects your personal messages and calls with end-to-end encryption.」 | 无 | `faq_3398056720476987`｜第 8 句 |
| **合计** | **57** 个词（组）× **2** 类站点 | 非空格 **56** 个 | 句次 **318** | 一句可落多格 | 代表句按池内顺序取第一句 | 零命中词 **9** 个 | 回表失败 **0** 句 |

#ROWS=56 COLS=8


# 附表 D 官方写明的取值域全表（6 行）

取组口径：脚本扫过 **12** 个已登记页面的每一行原文，配置了 10 个取值域标记，本轮真正在这些页面里出现过的有 **1** 个（`Values can be` 6 行，合计 **6** 行），另有 **9** 个（Supported values are／Possible values／Can be one of／must be one of／The following values are supported／Valid values are／Options are／one of the following／Enum Values）在扫过的每一行原文里零命中——这几串字符只是脚本配置表里的名字，本轮官方页面没有一处用过这种写法，所以本篇不给它们加反引号、也不把它们当作任何官方句子的引文。取到值来的页面 **2** 个（站点类别：Meta 开发者文档）。按标记所在行的形态分档取值：自动·参数表说明格 5。全表 **5** 组、共 **27** 个取值、去重后的取值名 **21** 个。其中 **0** 组的取值是不带反引号的纯文本子项——反引号是接口文档的排版，帮助中心同样用「one of the following」加子项写成正式取值域，这一档整项照收、只丢超过 80 字符的说明句；同页同字段同取值的重复写法（帮助中心按平台页签各写一遍）合并为一行，共合并 **0** 处。取值本身带空格的共 **0** 个（如 `LEGAL NAME NOT FOUND IN DOCUMENTS`、`No longer needed`），这一批只能按「反引号紧跟破折号说明」的位置认出，脚本对这种写法额外兜一遍：同一格里见到两个以上这种位置才收，并且只在这一档放开取值不能带空格的限制——接口文档的枚举里确有带空格的大写取值，卡成单词会漏掉整组。

为什么这一表不走原句池：官方把取值域写在接口参数表的说明格里、缩进的子项列表里、整句内联的清单里，这些行要么带竖线、要么是列表行，本来就被池子的入池条件挡在外面。所以 D 的核验独立于 C：每一组把它自己那一行原文、缩进子项式还把每个取值所在的那一行原文，逐字回表到 `_tmp/w6_a9s/` 的对应页面，失败 **0** 行。

落空登记：扫到标记但取不到两个以上取值的共 **1** 处，原因由脚本按同一行的证据判，不由人挑：官方只写了一个取值 1。**扫到标记不等于官方写明了取值域**，这几处不算进表，但算进这一句，让读者知道漏了多少、为什么漏。这 **1** 处分布在 **1** 个页面，每一处的页面标识、原文行号、判定档位、命中的标记、落空判据与该行未截断的原文全部逐条写进 `_tmp/w6_a9_dmiss.tsv`（与上面的次数由脚本断言相等，不是"约"），可逐条回原文核对。

单组取值上限设为 **24** 个：实测最多的一组有 **10** 个取值，没有一组触及上限，所以本表没有因上限而丢过取值。

取值列表的截断按整取值进行（顿号相连的列表没有空格，按词截会把一整组截到只剩一项）：单元格 **400** 字符内装得下的原样列出，装不下的整项去掉并在行内写明「另有 N 个取值未列出」。本轮实测 **0** 组触及这一上限、被去掉的取值 **0** 个，也就是说表里列出的取值就是脚本收到的全部 **27** 个，没有一组需要读者回原文补齐。

| 组别 | 页面标识 | 行号 | 字段／属性 | 官方标记 | 取值个数 | 官方写明的取值 |
|---|---|---|---|---|---|---|
| 自动·参数表说明格 | `webhooks_reference_messages_status_md` | 99 | `<CONVERSATION_CATEGORY>` | Values can be | **7** | `authentication`、`authentication_international`、`marketing`、`marketing_lite`、`referral_conversion`、`service`、`utility` |
| 自动·参数表说明格 | `webhooks_reference_messages_status_md` | 111 | `<PRICING_CATEGORY>` | Values can be | **10** | `authentication`、`authentication-international`、`group_marketing`、`group_service`、`group_utility`、`marketing`、`marketing_lite`、`referral_conversion`、`service`、`utility` |
| 自动·参数表说明格 | `webhooks_reference_messages_status_md` | 112 | `<PRICING_MODEL>` | Values can be | **2** | `CBP`、`PMP` |
| 自动·参数表说明格 | `webhooks_reference_messages_status_md` | 115 | `<STATUS>` | Values can be | **5** | `delivered`、`failed`、`played`、`read`、`sent` |
| 自动·参数表说明格 | `webhooks_reference_security_md` | 34 | `<EVENT>` | Values can be | **3** | `PIN_CHANGED`、`PIN_RESET_REQUEST`、`PIN_REQUEST_SUCCESS` |
| **合计** | **2** 页 | 行号为原文行序 | 字段名取自官方写法 | 标记 1 种 | 取值 **27** 个 | 去重取值名 **21** 个 |

#ROWS=5 COLS=7


# 附表 E 同一组维度在本轴各主体上的逐列对照（29 行）

取格口径：本表 **28** 个对照维度 × **7** 列 = **196** 格，其中 **34** 格取到了官方原句、**162** 格标作「官方未按这一列写该项」。每一格的取法都是「页面标识＋一条正则」交给脚本在原句池里取句，在该列的页面范围内命中 0 句或命中多句都会让生成脚本中止，所以表里没有一格是人工挑出来的。原句池共 **204** 句（已由附表 B 逐字回表），本表用去重后的页面 **11** 个；同一句在多个维度或多列复用的共 **1** 次，复用时页标识与句序不变。

列的定义写在 `_tmp/w6_a9_ecols.tsv` 里，每列要么按站点类别取页（用户帮助中心＝site WhatsApp 帮助中心），要么按人工点名的页清单取页（端点页＝1 页／总纲与覆盖页＝2 页／状态参考页＝1 页／消息与错误参考页＝2 页／回声与安全参考页＝2 页／错误码总表页＝1 页）。空格的含义只有一个：官方在这批登记页面里没按这一列写这一项，**不代表该主体没有这项规则**。

官方原句超过 112 字符按整词截断并以省略号结尾，共 **16** 格截断；截断只发生在显示层，取句与逐字回表用的是未截断的整句。各列取到句子的格数：端点页 13／总纲与覆盖页 4／状态参考页 3／消息与错误参考页 4／回声与安全参考页 2／错误码总表页 4／用户帮助中心 4。

| 编号 | 对照维度 | 端点页 | 总纲与覆盖页 | 状态参考页 | 消息与错误参考页 | 回声与安全参考页 | 错误码总表页 | 用户帮助中心 |
|---|---|---|---|---|---|---|---|---|
| E1   | 回调的官方定义 | 「Anytime a webhook event is triggered for any webhook fields you are subscribed to, Meta sends a POST request to…」（`webhooks_create_webhook_endpoint_md`｜第 36 句） | 「Webhooks are HTTP requests containing JSON payloads that Meta's servers send to a server of your designation.」（`webhooks_overview_md`｜第 4 句） | 官方未按这一列写该项 | 「The **messages** webhook describes messages sent from a WhatsApp user to a business and the status of messages…」（`webhooks_reference_messages_md`｜第 3 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E2   | 端点必须具备的证书 | 「Your webhook endpoint server must have a valid TLS or SSL digital security certificate, correctly configured…」（`webhooks_create_webhook_endpoint_md`｜第 6 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E3   | 自签名证书 | 「Self-signed certificates are not supported.」（`webhooks_create_webhook_endpoint_md`｜第 7 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E4   | 握手请求的用途 | 「GET requests are used to verify your webhook endpoint.」（`webhooks_create_webhook_endpoint_md`｜第 12 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E5   | 握手请求的比对对象 | 「To validate GET requests, compare the `hub.verify_token` value in the request to the verification string you…」（`webhooks_create_webhook_endpoint_md`｜第 26 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E6   | 投递请求的签名头 | 「Compare your generated hash to the hash assigned to the `X-Hub-Signature-256` header (everything after…」（`webhooks_create_webhook_endpoint_md`｜第 50 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E7   | 校验通过后的处理 | 「Capture the payload and digest the payload contents according to business needs.」（`webhooks_create_webhook_endpoint_md`｜第 52 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E8   | 校验不通过 | 「If they do not match, consider the payload invalid.」（`webhooks_create_webhook_endpoint_md`｜第 53 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E9   | 端点未通过验证 | 「If your webhook endpoint responds with anything else, however, Meta will consider your webhook endpoint…」（`webhooks_create_webhook_endpoint_md`｜第 34 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E10  | 重投的期限 | 「If any POST request sent to your server fails, delivery is retried immediately, then a few more times with…」（`webhooks_create_webhook_endpoint_md`｜第 61 句） | 「If a webhook request to your endpoint receives an HTTP status code other than 200, or if the webhook cannot be…」（`webhooks_overview_md`｜第 53 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E11  | 批量的上限 | 「POST requests are aggregated and sent in a batch with a maximum of 1000 updates.」（`webhooks_create_webhook_endpoint_md`｜第 59 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E12  | 去重的责任 | 「Your server should handle deduplication in these cases.」（`webhooks_create_webhook_endpoint_md`｜第 62 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E13  | 未确认响应的去向 | 「Unacknowledged responses will be dropped after 7 days.」（`webhooks_create_webhook_endpoint_md`｜第 63 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E14  | 已读以已送达为前提 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「A status is considered read only if it has been delivered.」（`webhooks_reference_messages_status_md`｜第 13 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E15  | 已读的界面符号与群聊口径 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「`read` — Indicates the message was displayed in an open chat thread in the WhatsApp user's device. - WhatsApp…」（`webhooks_reference_messages_status_md`｜第 87 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Two blue check marks appear when everyone has read your message.」（`faq_665923838265756`｜第 8 句） |
| E16  | 外发消息的回调条数 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「These webhooks don't describe the contents of the outgoing message itself, only its status, and each outgoing…」（`webhooks_reference_messages_md`｜第 15 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E17  | 失败回调的字段缺席 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Failed status webhooks omit the `contacts` array.」（`webhooks_reference_messages_md`｜第 30 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E18  | 错误出现的三处 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Errors in messages webhooks can be surfaced in three places:」（`webhooks_reference_messages_md`｜第 43 句） | 官方未按这一列写该项 | 「Cloud API errors can be surfaced in the following webhook objects:」（`support_error_codes_md`｜第 11 句） | 官方未按这一列写该项 |
| E19  | 判错的依据 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「In general, Meta recommends that you build your app's error handling logic around `code` values and `details`…」（`support_error_codes_md`｜第 4 句） | 官方未按这一列写该项 |
| E20  | 弃用提醒 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「The `billable` property will be deprecated in a future versioned release.」（`webhooks_reference_messages_status_md`｜第 53 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 「However, Meta recommends that you do not rely on titles for your error handling logic as titles will eventually…」（`support_error_codes_md`｜第 7 句） | 官方未按这一列写该项 |
| E21  | 同步与异步 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「However, Meta recommends that you do not rely on titles for your error handling logic as titles will eventually…」（`support_error_codes_md`｜第 7 句） | 官方未按这一列写该项 |
| E22  | 覆盖的兜底顺序 | 官方未按这一列写该项 | 「If the Messaging account also has no alternate, the webhook falls back to your app's callback URL.」（`webhooks_override_md`｜第 8 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E23  | 不能覆盖的类型 | 官方未按这一列写该项 | 「> **Note:** Template webhooks (`message_template_status_update`, `message_template_quality_update`,…」（`webhooks_override_md`｜第 26 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E24  | 回声的撤回触发 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「This example payload describes a business customer revoking (deleting) a previously sent message using the…」（`webhooks_reference_smb_message_echoes_md`｜第 43 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E25  | 安全事件的官方定义 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「The **security** webhook notifies you of changes to a business phone number's security settings.」（`webhooks_reference_security_md`｜第 4 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E26  | 用户端的送达勾选 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「The message was successfully delivered to the recipient's phone or any of their linked devices.」（`faq_665923838265756`｜第 4 句） |
| E27  | 时间戳异常的处置 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「If the timestamp on messages you’ve sent or received is wrong, please check the time and time zone…」（`faq_529932872293332`｜第 5 句） |
| E28  | 等待提示与端到端加密 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「WhatsApp protects your personal messages and calls with end-to-end encryption.」（`faq_3398056720476987`｜第 8 句） |
| **合计** | **28** 个维度 × 7 列 = **196** 格 | 有句 **34** 格 | 空格 **162** 格 | 去重引用页 **11** | 跨格复用句 **1** 次 | 截断 **16** 格 | 端点页 13 | 总纲与覆盖页 4 |

#ROWS=28 COLS=9


# 附表 F 口径冲突与官方未写明处（12 行）

本表收录两类东西：官方多处写法不一致、需要读者知道本篇按哪一说走的**冲突**，以及官方在这批登记页面里根本没写、本篇不替它补的**空白**。全表 **11** 个议题：**9** 个两侧都取到了原句、**1** 个只有一侧有原句（另一侧官方没写）、**1** 个两侧都没有原句可引（共 **1** 行）。

两侧皆空的行要说清它空在哪：本轴登记 **12** 页，其中正文未取回 **0** 页——这 **0** 页全篇不引用，已引用 **12** 页（Meta 开发者文档 9／WhatsApp 帮助中心 3），所以两侧皆空不是因为抓不到，而是因为**官方在可引用的这批页面里没按这一项写过一句话**；这一类议题一律登记为空白，正文不替官方补结论。

取句方式与附表 E 同一把刀：每个说法登记「页面标识＋一条正则」，脚本在原句池里取句，命中 0 句或多句都会让生成脚本中止；取到的每一句再逐字回表到 `_tmp/w6_a9s/` 的对应页面。本表引用页面去重 **7** 个，同一句在多个议题里复用的共 **2** 次。两侧点同一页、又取到同一句的行由脚本直接中止——同句不构成冲突，不允许拿复制冒充对照。差异性质用了 **11** 种。这 11 条性质各不相同（每种恰好 1 行），不在这里重述，逐条只写在表内「差异性质」列。

「本篇采用」一列写的不是判断谁对谁错，而是本篇正文按哪一说的走、以及为什么。这一列的 **7** 行标为已写进正文、**4** 行只在本附表登记（正文不替官方补结论，也不把两种说法合成一说）。这一列标「已写进正文」的 **7** 行本次已逐句核过成稿：它们要引的 **14** 句官方原句全部原样出现在成稿里（空白折叠后做子串比对），缺任何一句本脚本都会中止，所以这一列不是空头承诺。官方原句超过 130 字符按整词截断并以省略号结尾，共 **7** 处截断；截断只发生在显示层。

| 编号 | 议题 | 说法甲 | 说法乙 | 差异性质 | 本篇采用 | 正文落点 |
|---|---|---|---|---|---|---|
| F1   | 重投措辞的两处差异 | 「If a webhook request to your endpoint receives an HTTP status code other than 200, or if the webhook cannot be delivered for…」（`webhooks_overview_md`｜第 53 句） | 「If any POST request sent to your server fails, delivery is retried immediately, then a few more times with decreasing frequency…」（`webhooks_create_webhook_endpoint_md`｜第 61 句） | 一处只写频率递减与最长七天，另一处补出失败后先立即重投 | 本篇按更具体的端点页写法展开：先立即重投，再频率递减，总窗口最长七天；总纲页的说法作为同一个窗口的概括，不读成两套期限。 | 已写进正文 |
| F2   | 批量上限与批量保证 | 「POST requests are aggregated and sent in a batch with a maximum of 1000 updates.」（`webhooks_create_webhook_endpoint_md`｜第 59 句） | 「However, batching cannot be guaranteed so be sure to adjust your servers to handle each POST request individually.」（`webhooks_create_webhook_endpoint_md`｜第 60 句） | 上限讲的是聚合规模，官方同时明说不保证每次都会聚合 | 本篇把两句并列：单次 POST 的批量上限是一千条更新，但不保证聚合，所以服务端必须能逐条处理每次 POST；不把上限读成承诺。 | 已写进正文 |
| F3   | 两个七天说的不是同一件事 | 「If any POST request sent to your server fails, delivery is retried immediately, then a few more times with decreasing frequency…」（`webhooks_create_webhook_endpoint_md`｜第 61 句） | 「Unacknowledged responses will be dropped after 7 days.」（`webhooks_create_webhook_endpoint_md`｜第 63 句） | 同一页两个七天：前者是失败重投的窗口，后者是未确认响应的丢弃时点 | 本篇把两个七天分开写：重投在最长七天内频率递减，未确认的响应七天后被丢弃；不把两者合成一个截止时间。 | 已写进正文 |
| F4   | 三条状态回调与已读时的省略 | 「These webhooks don't describe the contents of the outgoing message itself, only its status, and each outgoing message can have up…」（`webhooks_reference_messages_md`｜第 15 句） | 「In these cases, the "delivered" webhook is not sent because it's implied that the message was delivered since it was read.」（`webhooks_reference_messages_status_md`｜第 15 句） | 前者是上限，后者是官方点名的省略情形，上限不等于每次都发三条 | 本篇按上限写：一条外发消息最多三条状态回调，但当已读同时蕴含送达时，官方不发送达回调，所以三条是上限而不是固定条数。 | 已写进正文 |
| F5   | 已读的送达前提与同时发生的例外 | 「A status is considered read only if it has been delivered.」（`webhooks_reference_messages_status_md`｜第 13 句） | 「In some cases, like when a user receives a message while in the chat screen, the message is both delivered and read at the same…」（`webhooks_reference_messages_status_md`｜第 14 句） | 前提规则与同页点名的例外情形并列 | 本篇先写前提：未送达不算已读；再写例外：用户正在会话界面时消息可同时视为送达与已读，此时回调侧省略送达那一条。 | 已写进正文 |
| F6   | 覆盖范围的适用范围与点名的例外 | 「The override applies only to the following webhook field types.」（`webhooks_override_md`｜第 10 句） | 「Meta always delivers these webhooks to your app's default callback URL.」（`webhooks_override_md`｜第 27 句） | 一般适用范围与点名的例外类型分处两段 | 本篇按范围写：覆盖只对列出的字段类型生效，模板类与账户级回调明确不支持覆盖，始终投到应用默认地址。 | 已写进正文 |
| F7   | 错误位置的三处与两条通道 | 「Errors in messages webhooks can be surfaced in three places:」（`webhooks_reference_messages_md`｜第 43 句） | 「It is a good practice when working with Cloud API that you monitor both the Graph API response and the `messages` webhook for…」（`support_error_codes_md`｜第 8 句） | 分类维度不同：一处数回调载荷里的位置，一处说同步与异步两条通道 | 本篇不在两套分类之间换算：回调内三处是载荷结构，同步与异步是到达方式，正文分别引用，不把三处读成三条通道。 | 只在附表登记 |
| F8   | 群聊送达的判定主体 | 「In a group chat, the second check mark appears when everyone has received your message.」（`faq_665923838265756`｜第 7 句） | 「The triggers above also apply to a WhatsApp user who is part of a group chat.」（`webhooks_reference_messages_status_md`｜第 12 句） | 客户端按全员收到显示送达勾，回调按单个用户逐条给状态 | 本篇分层写：界面上的第二个勾在群聊里要等全员收到，回调侧则对群内每个用户逐条给状态；两者判定主体不同，不互相套用。 | 已写进正文 |
| F9   | 重投间隔的具体数值 | 「If any POST request sent to your server fails, delivery is retried immediately, then a few more times with decreasing frequency…」（`webhooks_create_webhook_endpoint_md`｜第 61 句） | 官方未写明（—） | 单一说法：官方只给频率递减的定性描述，未给任何间隔数值 | 本篇只按官方定性口径写频率递减与最长七天，不推算也不列出任何具体间隔数值。 | 只在附表登记 |
| F10  | 失败回调里两个字段的缺席条件 | 「Failed status webhooks omit the `contacts` array.」（`webhooks_reference_messages_md`｜第 30 句） | 「If the original message was sent to a phone number, `statuses.recipient_user_id` is also omitted.」（`webhooks_reference_messages_md`｜第 31 句） | 缺席条件不同：一处对所有失败回调成立，一处只在发送对象是电话号码时追加省略 | 本篇把两处并列：失败回调一律不含 contacts，发送对象为电话号码时再省略 statuses.recipient_user_id；两条条件不同，不合并成一条。 | 只在附表登记 |
| F11  | 签名密钥的轮换 | 官方未写明（—） | 官方未写明（—） | 官方未写：本批登记页面没有签名密钥轮换或更换的说明 | 本篇不写轮换或更换流程，只按官方给的签名头比对口径写校验，密钥管理不在本篇展开。 | 只在附表登记 |
| **合计** | **11** 个议题 | 两侧有句 **9**／单侧 **1**／两侧皆无 **1** | 引用页 **7** | 性质 **11** 种 | 正文落点：已写 **7**／只在附表 **4** | 截断 **7** 处 |

#ROWS=11 COLS=7


---

**边界声明：** 本篇是**官方回调事件、消息状态与错误码文本的口径梳理**，不是对接教程，也不是对平台投递能力的评价，更不是运营方案。全篇内容限于 WhatsApp 与 Meta 公开页面已经写出的表述，共 **12** 个页面登记位：Meta 开发者文档 **9** 页（回调总纲 1 页、端点 1 页、五类回调参考 5 页、交替地址覆盖 1 页、错误码总表 1 页）、WhatsApp 帮助中心 **3** 页（已读回执、等待消息提示、时间戳修正）。引用这些文本的目的只有两个：说明平台把**回调、事件、载荷、状态、确认、错误**写成了哪些对象名、哪些字段、哪些状态词与哪些否定式前提；说明同一批文本把**重投、批量、去重、丢弃、覆盖顺序与两条错误通道**写成了哪些数字与哪些限定词。本篇**不含**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法；**不含**任何可用于向非自愿收件人大量投递消息的方法——"怎样伪造签名头骗过回调校验""怎样把回调地址换成自己的接收端""怎样用重投机制压垮对方服务"这三类问法在本篇一律不答；**不含**代码、恶意样本或利用链构造方式；**不含**任何针对他人设备或他人账号的操作步骤与时序。界面页里官方写成"步骤""清单"的内容——在哪一层菜单看已读回执、等待提示出现在哪一屏、时区怎么选——本篇只登记**控件名、状态名与条目数**，**不重述每一步在哪个页签里怎么点，也不把清单转写成可执行的动作序列**，读者要动手请回原页。

四类页面在本篇里始终分开写，因为官方没有一页把它们画成同一张对象图：**本体与端点面**写在总纲与端点页，回答回调是什么、端点要满足什么、交替地址按什么顺序兜底（第 2 章）；**校验面**写在端点页，回答握手的验证令牌与投递的签名头各比对什么（第 3 章）；**状态面**写在状态参考页与 messages 参考页，回答一条外发消息最多留下几条状态、每个状态词的前提是什么（第 4 章）；**投递与丢弃面**写在总纲、端点页与状态页，回答批量、重投、去重与丢弃各按什么期限走（第 5 章）；**错误面**写在错误码总表与 errors 参考页，回答错误从哪两条通道回来、按什么字段处理（第 6 章）；**非消息回传面**写在覆盖页、安全页与回声页，回答覆盖只对谁生效、两类非消息回调各带什么（第 7 章）。把这六层合并成"一条回调从订阅到丢弃的完整规则"，会得到官方没有写过的跨面结论，本篇不这么做。

官方写成区间、多档或带限定词的数字（「up to」「maximum」「at least」「a few more times」一类）按整段一行登记在附表 B，不合成单值、不取中值、不去掉官方写在数字前面的限定词；同一数字在不同指代上各写一处的按指代分列，不做跨指代合并。B 表最终是 **10** 条、**11** 行（含合计）、分布在 **5** 个页面、去重后的「数字＋单位」组合 **9** 组、单位词 **6** 种、性质 **6** 种（上限 **2**、下限或触发门槛 **1**、时长 **1**、月份与号数写明的日期节点 **1**、申诉与复核时长 **4**、自动解除或自动生效的时限 **1**）、整句截断 **7** 行；截断只发生在显示层，进表的每一句都先用未截断整句回原页核对，失败 **0** 句。它的输入是原句池的 **204** 条整句（覆盖 **12** 页），其中 **183** 句整句不含任何阿拉伯数字、本来就不在 B 表的取材范围里；另有 **5** 个「数字后面四词都对不上任何一档」的实例写进 `_tmp/w6_a9_bdrop.tsv` 供逐行复核，不进表——这一批多数是接口示例里的对象标识与字段说明，不是官方对某项业务写下的数量，**"这个数是不是一个上限"的判定权在读者手里，不在这张表里**。错误码总表把大量错误码与说明写在表格里，表格行按建池口径整行剔除，所以错误码的具体编号不在 B 表里；**"表里没有这一行"不等于"官方没有这个数"**，只是它写在表格里，不在句子层。

正文引句全部为整句、逐字回核到抓取件。整句的判据是抓取件的句池切分口径：句池按句子终止符切、不按版面标记切，因此官方 markdown 页里紧跟句子的小标题会被并入同一句；同时，长度不在窗口内、段内含竖线或内联链接、以 `#` 或 `-` 起手的片段一律不入池。本轮正文引用去重后 **72** 句（引用标记 **72** 处，同一句在正文重复引用 **0** 处），分布在 **12** 个页面上；官方标题在正文被点名 **15** 处、覆盖全部 **12** 个登记页。引句里 **5** 句自带官方粗体排版标记 `**`（每句各一对，偶数，照抄原文、不删字符），**0** 句在同一句里带 `## 小标题`。另有一条硬规矩：含反引号字段名与枚举名的句子不整句引用，这些页面上的事实改用中文陈述并逐处标注出处页；**本轮正文实际引用的 72 句里，含反引号的句子是 0 句**，这条规矩没有被绕过。本轮引用的句子里有 **1** 句官方原文本身在抓取件里只带一个英文双引号（官方页面原样如此，句子在引号中间结束），本篇照抄不改字符，也不替它补上闭合引号。

方法层的三张表各自回答一个问题，且都不越界：附表 E 把同一组 **28** 个维度并排放在 **7** 列主体上（端点页／总纲与覆盖页／状态参考页／消息与错误参考页／回声与安全参考页／错误码总表页／用户帮助中心）＝ **196** 格，**34** 格取到官方原句、**162** 格标作「官方未按这一列写该项」，该标注**只说明官方文本的写法，不说明该类主体没有该项规则**；去重引用页 **11** 个、跨格复用句 **1** 次、显示层截断 **16** 格；各列取到句子的格数分别是端点页 **13**、总纲与覆盖页 **4**、状态参考页 **3**、消息与错误参考页 **4**、回声与安全参考页 **2**、错误码总表页 **4**、用户帮助中心 **4**。附表 C 把「官方是否写明」限成「词（组）× 站点类别」矩阵，**57** 个词（组）分属 **8** 个主题组 × **2** 类站点共 **56** 行、句次 **318**、去重命中句 **135**（占全池 **66.2%**），**9** 个词按零命中登记。附表 D 只登记官方在页面里逐字写出的取值域：配置的 **10** 串标记本轮 **1** 串在登记页里出现，另 **9** 串在扫过的每一行原文里零命中，最终 **5** 组、**27** 个取值、去重取值名 **21** 个、覆盖 **2** 页；另有 **1** 处官方写了标记却取不出两个以上取值，逐处连同原文行号写进 `_tmp/w6_a9_dmiss.tsv`。附表 F 把 **11** 个议题逐条对撞（两侧有句 **9**／只有一侧 **1**／两侧皆无 **1**，引用页去重 **7**、差异性质 **11** 种、复用 **2** 次、显示层截断 **7** 处），凡官方留白处一律登记为留白，不推断、不补齐。

页面层的四项事实单独交代，免得读者把"登记"读成"引用"：**第一**，本篇登记的 **12** 页正文全部取回，没有一页被标注"全篇不引用"，也没有一页整页零句；**第二**，正文整句引用落在 **12** 个页面上，**12** 页每一页都被整句引用过；**第三**，**12** 页全部在正文被官方标题点名（标题标记共 **15** 处），附表 A 的引用位置列没有一格留空；**第四**，正文引用的 **72** 句里没有一句含反引号，含粗体的 **5** 句全部按原样保留标记，另有 **1** 句的英文引号在官方原文里本身就不闭合，照抄不改。逐页状态由脚本实测而非人工填写，见附表 A 的引用位置列。

本篇对"回调该不该同步处理""重投间隔具体是几秒""签名密钥多久轮换一次""状态回调的计费字段该怎么入账"一律不补；签名密钥的轮换方式、重投间隔的具体数值、群聊里已读判定的完整前提，这些官方只写了一半或根本没写的问题登记在附表 F，正文只写官方实际写了的那一半。两张站点口径只按各自页面分别写：开发文档的对象、字段、状态与回传，帮助中心的勾、提示与时间，两套装在两章里，不合并成"WhatsApp 的回调规则"。

最后两处范围断言必须交代扫描口径，因为它们都是"穷举后没有"而不是"抽样没看到"：**其一**，本篇在 1.2 写"回调、事件、状态、投递、确认、错误码"六个词的所指，第 9.1 节把它收成几组同词不同层。这一句的依据是：附表 C 的 8 个主题组共 **57** 个词（组）在 **204** 句里逐句扫过，命中落在不同的页面与不同的字段上；但这只覆盖本篇登记的 **12** 页，两个站点还有大量本篇未登记的页面，所以"没有统一的回调字段表"是对这批语料的陈述，不是对 WhatsApp 全部公开文本的陈述。**其二**，附表 F 有 **4** 个议题标成"只在附表登记、正文未并列两句"，逐条点名是：F7 错误位置的三处与两条通道、F9 重投间隔的具体数值、F10 失败回调里两个字段的缺席条件、F11 签名密钥的轮换。这四行都能在附表 F 逐字回表；正文在相关小节只写官方实际给出的那一半，**不并列是叙述选择，不是"官方没有连接句"的断言**；读者若要把它们当通则使用，应自己回到原页看官方在上下文中怎么连接这几句。

Telegram: https://t.me/one00190
