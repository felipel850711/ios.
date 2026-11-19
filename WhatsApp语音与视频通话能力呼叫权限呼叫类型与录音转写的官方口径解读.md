@@TITLE@@WhatsApp 语音与视频通话能力：呼叫权限、呼叫类型与录音转写的官方口径解读
@@SUBTITLE@@本篇是"WhatsApp 群发／群控系统性口径"系列第四轮的第四篇（轴④，编号 n=15），讲的是：业务号码上的通话默认开不开、能不能只开给对方特定用户、呼叫前必须先拿到什么、一次通话在官方口径里留下哪几个状态、每类回调各带什么字段、为什么同一条回调可能到两次、接听窗口与预接听各指什么、录音与转写是不是默认开启、开启前要对双方播什么、SIP 这条信令路径与 Graph API 是什么关系、它默认发不发回调、通话怎么计费、并发上限与二十四小时窗口是不是一件事、用户端的按钮、链接、访客、等待室、静音与 IP 保护各写在哪一页，以及哪些问题官方在这批页面里没有写。全篇只读官方文本，共 **25** 个页面登记位（**25** 页正文全部取回，逐页归属与引用位置见附表 A 的 **26** 行含合计）：Meta 开发者文档 **15** 页、WhatsApp 帮助中心 **10** 页。
@@INFO@@报告日期|2026 年 11 月 19 日
@@INFO@@分析对象|按站点清点本篇登记的页面构成：Meta 开发者文档 **15** 页（Cloud API Calling、Business-Initiated Calls、Configure Call Settings、API and Webhook Reference、Call recording、Call transcription、SIP Configuration Guide、Integration Patterns、Integration Examples、Calling API Pricing、Set Up a Sandbox Account for Calling、Calling API App Review Guidelines、WhatsApp FAQ、Troubleshoot WhatsApp Calling Errors、Send WhatsApp Call Button Messages and Deep Links）、WhatsApp 帮助中心 **10** 页（一对一语音通话、一对一视频通话、群组语音通话、通话链接、来电等待、通话记录、静默未知来电、通话 IP 保护、把通话转到第二台设备、听不到语音消息或通话声音）。合计 **25** 个登记位，正文全部取回；归组依据是官方页面标题，逐页归属见附表 A。
@@INFO@@视角定位|官方开发者文档、定价页、常见问题页与帮助中心说明的**口径解读**，不是接入教程，也不是投放或外呼方案。本篇**不提供**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法；本篇同样**不写**任何可用于向非自愿收件人大量投递消息或反复呼叫的方法——"怎样绕过权限直接外呼""怎样伪造通话链接或呼叫按钮""怎样用 SIP 或回调机制压垮对方服务"这三类问法在本篇一律不答；令牌获取、SDP 构造、媒体服务器配置、SIP 服务器搭建、回调验签实现与重试队列参数也不写（界线见 1.3）。
@@INFO@@分类|**官方页面登记 25 个登记位 × 2 类站点 × 引用位置 × 取回状态**（附表 A，**26** 行含合计；Meta 开发者文档 **15**、WhatsApp 帮助中心 **10**；可引用 **25** 页、不可引用 **0** 页；原句池 **990** 条整句、覆盖 **25** 页）× **数字归档 39 条，覆盖 11 页，去重"数字＋单位" 34 组、单位词 23 种，五档分布 7／17／2／8／5，截断 19 行，同页同数字同单位去重 12 行**（附表 B，**40** 行含合计）× **官方用词 × 站点类别矩阵 56 个词（组）分属 8 个主题组 × 2 类站点、70 行、句次 1458、去重命中句 715（占全池 72.2%）、零命中词 1 个、落在两个以上格子的句子 419 句**（附表 C，**71** 行含合计）× **官方写明的取值域 5 组、取值 12 个、去重取值名 7 个、覆盖 2 页，配置的 10 串标记本轮扫到 3 串共 15 行、7 串零命中，另有 10 处写了标记却取不出两个以上取值**（附表 D，**6** 行含合计）× **同一组维度在各主体上的逐列对照 33 个维度 × 7 列 = 231 格、有句 66 格／空格 165 格、去重引用页 18、同一句跨列复用 1 次、显示层截断 28 格**（附表 E，**34** 行含合计）× **口径冲突与官方未写明 12 个议题、两侧有句 10、单侧有句 1、两侧皆空 1、差异性质 12 种、去重引用页 6、复用 0 次、正文落点已核 9 行 18 句、截断 7 处**（附表 F，**13** 行含合计）× **正文与附表的官方原句逐字回核，失败 0 条**。
@@INFO@@难度|进阶（本篇有六处容易读错。**第一是"唯一限制"不等于"没有别的限制"**：一千路并发数的是同时，二十四小时窗口数的是滚动累计，两个数不能合并成一句"通话只有一个限制"（7.3、10.1，附表 F 的 F3）。**第二是四个七天不是同一件事**：配置生效、客户端刷新、媒体保留、负反馈自动限制各有一个七天（2.3、5.4、7.5、10.2，F1）。**第三是"用户不能主动呼入"与"企业不能给陌生人打电话"方向相反**：前者只在关闭呼叫图标的选项下成立，后者以用户先给权限为前提（2.2、3.1、10.3）。**第四是"未接听也算完成"不等于"用户接了"**：业务侧只有结束这一层信息，没有开始时间、结束时间与时长（4.2、10.4，F4）。**第五是 SIP 上的 mTLS 是"范围受限"**：只在 Meta 主动发起的连接上支持，且不覆盖媒体，不能拿它替代 IP 白名单（6.4、10.5，F7）。**第六是两个 rate limit 同名不同物**：一个是消息侧配额，一个是通话侧自己的二十四小时窗口；另有一组"五分钟下载链接"与"七天媒体保留"分属地址有效期与文件保留期（7.3、7.4、10.6，F10）。）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

本篇是"WhatsApp 群发／群控系统性口径"系列第四轮的第四篇（轴④，编号 n=15），主题是**语音与视频通话能力**：业务号码上的通话默认开不开；企业能不能主动给一个陌生用户打电话；打通之前要先拿到什么；一次通话在官方口径里留下哪几个状态、每一句回调是什么意思；录音与转写是逐通选择还是默认开启，开启前要对双方播什么；SIP 这条接入面和 Graph API 是什么关系、它默认发不发回调；通话怎么计费、并发上限与二十四小时窗口是不是同一件事；以及用户端看到的呼叫按钮、通话链接、访客与等待室各写在哪些页面。全篇只读官方文本，共 **25** 个页面登记位：Meta 开发者文档 **15** 页、WhatsApp 帮助中心 **10** 页；**25** 页正文全部取回、全部进入原句池，共 **990** 条整句。

前几篇问的是"谁能发、发给谁、名字归谁、对方愿不愿意、到了之后怎么办、一条消息被算成几笔、商品与订单怎么摆、媒体上限写在哪一层、回调怎么回来"；本篇问的是"**从拨出到挂断，平台把这件事拆成了哪些可引用的句子**"。这条线在官方文本里被拆成五类页面：总纲与发起页写通话是什么、两条发起路径怎么分、一次呼叫要经过哪几步；设置页写开关、图标、时窗、语音信箱与编解码；参考页写权限、状态、回调、录音与转写五组接口与载荷；SIP 页与两个集成页写另一条信令路径；定价页与 FAQ 写钱和限额；帮助中心十页写用户端看到的按钮、链接、访客、转移、静音、历史与听不见声音。五类页面之间没有一张统一的"通话能力表"，本篇把它们并排登记在附表 E 的 **33** 个对照维度 × **7** 列 = **231** 格（**66** 格取到官方原句、**165** 格标作"官方未按这一列写该项"），并把成对出现的口径差异与留白登记在附表 F 的 **12** 个议题里。

本篇的读法是**沿一次通话走一遍**：先看能力与两条发起路径（第 2 章）；再看通话之前必须先有的权限，以及权限请求的配额与重置（第 3 章）；然后是一次通话的状态与回调，包括接听窗口、乱序、重复与超时（第 4 章）；接着是录音与转写这两项可选处理（第 5 章）；再换到 SIP 这条信令路径（第 6 章）；最后是计费与限额（第 7 章）、用户端入口（第 8 章），以及横向对照（第 9 章）、容易读错的几处（第 10 章）与边界（第 11 章）。

下面每个问题都标出它落在哪一节，一个问题可以跨相邻两小节，一节也可以由两个问题分别追问；跨小节的关联只做中文指认，不把两页原句拼成一句。

1. 这批页面在站点上怎么分布，"25 页登记、25 页有句"意味着什么？（1.1）
2. 本篇固定了哪些中文口径词，每个词对应哪个英文原词？（1.2）
3. 通话、企业发起、用户发起、呼叫权限、接听、终止、接入面在本篇里各指什么？（1.2）
4. 本篇不写什么，界面页里点按钮的内容登记到什么颗粒度？（1.3）
5. 官方用什么句子定义这套通话能力，它覆盖哪些形态？（2.1）
6. 两条发起路径分别是什么，谁先谁后？（2.2）
7. 企业为什么不能给一个陌生用户直接打电话？（3.1）
8. 通话默认开启吗，开关、图标与配置是几层？（2.3）
9. 配置变更之后，用户多久能看到？（2.3）
10. 呼叫时窗与语音信箱各管什么，两者为什么会冲突？（2.4）
11. 默认编解码是哪个，G.711 在什么场景下才用？（2.4）
12. 呼叫权限是什么，为什么它是发起通话的前提？（3.1）
13. 取得权限有哪两种方式，分别写在哪个位置？（3.2）
14. 权限可以永久授予吗，用户可以改主意吗？（3.2、3.4）
15. 权限请求有配额吗，什么动作会让配额重置？（3.3）
16. 用户端的"静默未知来电"会不会挡住企业来电？（3.4）
17. 通话状态有哪几个词，分成哪两组？（4.1）
18. 接听窗口有多长，不接听会怎样？（4.2）
19. 未接听在业务侧记成什么，为什么没有时长？（4.2）
20. 谁可以终止通话，主动终止为什么被说成"更准"？（4.3）
21. 回调的到达顺序能乱到什么程度？（4.4）
22. 为什么同一条回调可能到两次？（4.5）
23. 超时大约多久，超时之后两侧的认知一致吗？（4.5）
24. 错误长什么样，日志在哪里看？（4.6）
25. 录音与转写是不是同一件事，能不能只开一个？（5.1）
26. 录音是默认开还是逐通选择？（5.1）
27. 开启之前要播什么，谁可以拒绝？（5.2）
28. 目的字段为什么必填，缺了会怎样？（5.2）
29. 两个都开启时播几遍，听哪一侧的字段？（5.3）
30. 产物是什么形态，结束后多久能拿到？（5.4）
31. 下载链接与媒体保留各是多长？（5.4）
32. 转写语言由谁决定，和播报语言是什么关系？（5.5）
33. 录音与转写现在收费吗，未来怎么定价？（5.6）
34. SIP 是什么，用户能感知到企业走的是哪条路径吗？（6.1）
35. 一个号码能配几个 SIP 服务器、几个应用？（6.2）
36. 证书与域名有什么要求，谁验证谁？（6.3）
37. SIP 的媒体加密有几种，mTLS 覆盖到哪？（6.4）
38. SIP 回调默认开吗，它携带什么、不携带什么？（6.5）
39. SIP 常见的三类故障分别是什么？（6.6）
40. 通话怎么计费，脉冲怎么进位？（7.1）
41. 跨档怎么算，费率什么时候生效、市场怎么拆？（7.2）
42. 并发上限是多少，二十四小时窗口又是什么？（7.3）
43. 通话占不占消息限额，客服窗口怎么算？（7.4）
44. 负反馈积累到一定程度会发生什么？（7.5）
45. 用户端从哪里发起通话，企业怎么把入口摆出来？（8.1、8.3）
46. 沙盒能测什么，接收名单有几条？（8.2）
47. 通话链接有哪些限制，访客与等待室各是什么？（8.3）
48. 通话中能做哪些操作，转移有没有次数限制？（8.4）
49. 视频通话与数据节省的口径是什么？（8.5）
50. 听不到声音、打不了紧急号码，官方把原因指向哪里？（8.6）
51. 把这批页面横向排开，哪几组词最容易混？（9.1）
52. 附表 E 的 165 个空格该怎么读？（9.2）
53. 附表 F 的 12 个议题里，哪几处只能按一侧写？（9.3）

# 1 口径与范围

## 1.1 登记、取数与六张表

本篇登记 **25** 页，来源只有两个站点：Meta 开发者文档 **15** 页（总纲《Cloud API Calling》、企业发起《Business-Initiated Calls》、设置《Configure Call Settings》、录音《Call recording》、转写《Call transcription》、参考《API and Webhook Reference》、SIP《SIP Configuration Guide - WhatsApp Business Calling》、常见问题《WhatsApp FAQ》、排错《Troubleshoot WhatsApp Calling Errors - Reference Guide》、呼叫按钮与深度链接、定价、应用审核指南、集成模式、集成示例、沙盒），WhatsApp 帮助中心 **10** 页（一对一语音、一对一视频、群组语音、通话链接、呼叫等待、通话历史、静默未知来电、通话中的 IP 保护、跨设备转移、听不到声音）。这个分布不是按"哪边写得更细"选的，而是按"通话在这条链路上被拆到哪一层"选的：开关、权限、状态、接口与信令写在开发者文档，用户端的按钮、链接、访客、转移与静音写在帮助中心，两边对同一件事的措辞并不一致，第 9 章与第 10 章专门处理这种不一致。

六张表各管一段事实，读法如下。附表 A 是页面登记表：**25** 个登记位逐页写站点类别、官方标题、正文取回状态与它在本篇被点名的小节；**25** 页全部可引用，没有一页因未取回而整页缺席。附表 B 是数字归档：脚本从 **990** 条整句里扫出所有独立数字，按数字后面第一个能对上单位表的词分档，最终 **39** 行、覆盖 **11** 个页面、去重"数字＋单位" **34** 组、单位词 **23** 种；池子里 **875** 句整句不含数字，另有 **54** 个数字实例因后面四个词都对不上任何一档而被丢弃，逐条写在 `_tmp/w6_a10_bdrop.tsv`。附表 C 是词表实测：**56** 个官方词（组）分属 **8** 个主题组，逐句扫过全池，命中句次 **1458**、去重命中句 **715**（占全池 72.2%），其中 **1** 个词在整池零命中。附表 D 是取值域：脚本在原文行里认标记，取到 **5** 组、**12** 个取值、去重 **7** 个取值名，来自 **2** 个页面；另有 **10** 处扫到标记却取不出两个以上取值，逐条写在 `_tmp/w6_a10_dmiss.tsv`。附表 E 是逐列对照：**33** 个维度 × **7** 列，**66** 格有句、**165** 格是空。附表 F 收口径冲突与官方留白：**12** 个议题，两侧都取到原句的 **10** 个、单侧的 **1** 个、两侧皆空的 **1** 个。

取数口径有三条硬规矩。第一，**原句池只收整句**：脚本按抓取件的段落与句读切句，切出来不足 40 字符或超过 400 字符的丢弃，含竖线的"正文黏表格"混合句也丢弃，所以池子 **990** 句与页面总句数不是一一对应。第二，**引句必须整句**：正文与附表引用的每一句都先回表到 `_tmp/w6_a10s/` 的对应页面，按空白折叠做子串比对；附表 B、C、D、E、F 的生成脚本各自独立回表，任何一句核不上就中止出表。第三，**数字必须落地**：正文里每个官方数字要么在附表 B 有一行，要么整句照引官方原文；本篇自己数出来的结构数（登记页数、池子句数、各表行数、引句数）由 `_tmp/w6_a10_locked.json` 实测锁定，不手抄。

## 1.2 口径词：通话、发起、接听、终止、接入面

这一篇的麻烦不在术语多，而在同一个词在两套文本里指的不是同一层。先把本篇要用的中文词钉住，后面各章一律按这张表用词。

**通话（call）** 指一次从发起到终止的实时语音或视频会话。官方在总纲页把它写成基于 VoIP 的呼叫能力：The WhatsApp Business Calling API enables you to initiate and receive calls with WhatsApp users using Voice over Internet Protocol (VoIP).。这一句同时给出三个限定：呼叫对象是 WhatsApp 用户、走的是 VoIP、能力挂在 Business Calling API 之下。**企业发起（business-initiated calling，简称 BIC）** 与 **用户发起（user-initiated calling，简称 UIC）** 是官方自己用的两个缩写，前者指企业主动呼叫用户，后者指用户从客户端呼叫企业；两者在权限、回调、录音可用性上各有一组规则，本篇不把它们合成"外呼"一个词。**呼叫权限（call permission）** 指用户就是否允许该企业呼叫自己作出的选择；官方把"企业呼叫用户需要用户明确许可"写成一句前提句：Calling a WhatsApp user requires explicit permission from the user.。**接听（accept）** 指把已经振铃的通话接起来，官方把它与"建立"区分开：Establishing a new call means that the call was successfully picked up by the WhatsApp user.。**终止（terminate）** 指通话结束这一事件，双方都可以触发；官方写得很直白：Either you or the WhatsApp user can terminate the call at any time.。**接入面（signaling surface）** 是篇里为了方便对照而用的中文词，指企业这一侧用什么接通话信令：一条是 Graph API 加回调，一条是 SIP。官方在 SIP 页把这层写成"用 SIP 替代 Graph API 端点作为信令协议"：WhatsApp Business Calling API supports use of SIP as the signaling protocol instead of our Graph API endpoints.。

另外三个词只在特定章节出现。**告知（announcement）** 指录音或转写开始前对双方播放的法定播报，官方规定它必须先于采集：The recording starts only after the announcement has finished playing.。**目的（purpose）** 指企业在请求里必须填写、并会被念进播报的那段用途说明；官方把它写成开关打开时的必填项。**留白（blank）** 在本篇专指"官方在这批登记页面里没有按这一项写过一句话"，不是"官方没有这项规则"，附表 E 的 **165** 个空格与附表 F 的两侧皆空行都按这个意思读。

## 1.3 不写什么

本篇只读官方规则文本，不写任何可以照着执行的操作。具体地说，本篇**不提供**：批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法；也**不写**任何可用于向非自愿收件人大量投递消息的方法，包括"怎样在没有权限的情况下反复呼叫同一个用户""怎样让被暂停的号码绕过限制继续呼出""怎样把通话能力接成自动外呼系统"这三类问法。通话能力本身不是群发工具，本篇把它当**一套有前提、有状态、有计量的通信能力**来读，而不是当投放手段来写。

界面页的处理再单独说一次。帮助中心与沙盒页里有大量"点哪里、选什么"的句子，本篇只**点名与计数**：哪一页写了按钮叫什么、一个页面里出现几次设置项、沙盒的接收名单最多几条，都照实登记；但不把这些句子改写成一步一步的操作指南，也不把界面路径整理成可复制的步骤清单。SIP 密码那一条同理：官方把"重新生成密码"写成停用并重建 SIP 服务器这条路（附表 F 的 F11），本篇只登记"官方没有单独的重生成接口"，不复述步骤。引句也有一条规矩：官方原句里带字段名、带内联代码或带链接标记的，正文只作中文陈述、不整句照引；能整句照引的句子，一定是池子里不带这些排版标记的原句（附表 C 的统计与第 11 章的边界段都按这个口径写）。

# 2 通话能力与两条发起路径

## 2.1 官方把这件事叫什么

《Cloud API Calling》的开篇先给一句带范围的定位：

> `WhatsApp Business Calling: voice and video calling with global reach.`

紧接着的一句把"能力"落到具体做法上：它说的是把语音与视频通话接进企业的客户沟通链路，而不是在客户端里多一个按钮：

> `The WhatsApp Business Calling API allows businesses to integrate voice and video* calling directly into their customer engagement strategy, offering a trusted, unified, and feature-rich communication channel.`

这句话里有三个限定值得拆开：第一，呼叫对象是 WhatsApp 用户，不是任意电话号码；第二，能力是"接入"而不是"替代"，企业原有的话务系统与这套 API 是叠加关系；第三，它同时覆盖语音与视频两种形态，而本篇后面写到的状态、权限、录音规则对两种形态共用一套对象。官方在常见问题页还补了一句来源说明：

> `WhatsApp Calling API is the first public voice API by Meta.`

以及一句边界说明：

> `Meta may reuse the same API and integration model for other Meta products when and if they offer voice solutions.`

也就是说，这套接口眼下是 Meta 面向语音场景的第一个公开 API，官方保留把它复用到其他产品的可能。本篇不把这句话读成"将来一定合并"，只把它登记为官方对能力来源的自我定位。

两条发起路径是这一章的主干。官方在总纲页用一句话把路径数摆出来：

> `Cloud API Calling offers two call initiation paths:`

一条是企业发起（business-initiated calling，BIC），一条是用户发起（user-initiated calling，UIC）。这两条路径不是"同一个接口的两种调用方式"，而是**两个不同的事件起点**：BIC 的起点在企业这一侧，要先有权限才能拨出；UIC 的起点在用户这一侧，由用户从客户端发起，企业这一侧先收到的是回调。两条路径共用同一套通话对象、同一组状态词与同一批回调字段，但在权限、录音可用性与计费触发上各有一组规则，后面分章写。

## 2.2 两条发起路径各写在哪

企业发起路径主要写在《Business-Initiated Calls》。这一页开宗明义：

> `The Calling API lets your business call WhatsApp users.`

它给这条路径的接口定义是"用电话号码与 WebRTC 呼叫提议发起呼叫"：

> `Use this endpoint to initiate a call to a WhatsApp user by providing a phone number and a WebRTC call offer.`

这句话把三个对象绑在一起：被叫的 WhatsApp 用户、企业的呼叫提议、以及随后由 Meta 返回的应答。官方在相邻三句里把这条链路写成一个顺序——先发起，收到一个带应答的接通回调，把应答交给自己的 WebRTC 栈建立媒体连接，然后才收到振铃、接听或拒绝的状态回调。这三句都带字段名或代码标记，按第 1.3 节的引句规矩，本篇只作中文陈述、不整句照引；它们的字段名与取值写在附表 D 的两组 `status` 里（`RINGING`、`ACCEPTED`、`REJECTED` 一组，`COMPLETED`、`FAILED` 一组）。

用户发起路径的起点不在企业这一侧，官方在《Configure Call Settings》里用一句否定句把边界划出来：

> `WhatsApp users cannot make unsolicited calls to your business.`

这句话容易被读成"用户不能主动打给企业"，其实它说的是"用户不能打**未经邀请的**电话"：能否呼入取决于企业这一侧有没有开启通话、以及用户与这个业务号码之间有没有可用的会话入口。常见问题页从合作方视角补了一句：

> `In typical cases, a Solution Partner uses their own app and receives the call webhook followed by call establishment.`

以及一句时间关系：

> `Caller (the WhatsApp Consumer app) would already be ringing by the time the webhook is received.`

两句合起来说明：UIC 的回调不是"呼叫请求"，而是"已经发生的事"的通知——企业收到回调时，用户那一侧已经在振铃。企业这一侧能做的是接听与建立媒体，而不是"批准这次呼叫"。这与 BIC 的"先请求权限、再拨出"正好相反，也是本篇把两条路径分开写的原因。

两条路径的汇合点在通话对象本身：一旦接通，状态词、终止方式、录音与转写、计费口径都不再区分是谁先发起。第 4 章到第 7 章写的就是这条汇合之后的规则；第 3 章的权限只约束 BIC，不约束 UIC。

## 2.3 默认关闭、分层配置与七天生效

通话不是默认打开的。官方在设置页第一句就写：

> `Calling is not enabled by default on a business phone number.`

总纲页对测试号码另有一句：

> `Calling is disabled by default on test numbers.`

两句并不矛盾：一句说正式业务号码的默认值，一句说测试号码的默认值，方向一致——不配置就不开。开启之后，客户端上的入口是这样呈现的：

> `WhatsApp client apps render the call button icon in both the business chat and business chat profile.`

这句话只说"渲染在哪里"，没有说"长什么样"；按钮的文案、图标尺寸与点击后的界面，官方在这批页面里没有写成可引用的规格句，本篇不补。配置本身是分层的：号码级开关决定这条号码能不能通话；权限请求界面决定用户在呼入时会不会看到授权选项；通话图标决定会话里有没有入口；时窗决定用户能在哪些时段呼入；语音信箱决定未接听之后走哪条兜底；编解码决定媒体协商的默认项。这几层各有自己的配置项，改一层不等于改全部，附表 E 把它们拆成 **33** 个对照维度逐列登记。

配置变更的生效时间有两套说法，本篇按官方两句并排写。设置页给的是上限：

> `After you update call configuration, WhatsApp users may take up to 7 days to reflect those changes.`

《WhatsApp FAQ》把同一件事补上"多数更快"的限定，并给出客户端侧的刷新机制。先看 FAQ 自己复述的那句七天：

> `After call configuration is updated, WhatsApp users may take up to 7 days to reflect that configuration although most users refresh much sooner.`

紧接着的四句说明刷新由什么触发、通知覆盖谁：

> `It can take up to 7 days although most WhatsApp users should reflect the changes much sooner.`

> `All WhatsApp clients refresh the business information (including calling configuration) every 7 days regardless of getting any change notifications.`

> `However notification delivery is best effort so not all users may receive it.`

> `In either case (notification driven or 7d refresh), once the local state in WhatsApp client is refreshed, it is reflected in UI only on next enter of the chat screen or chat info screen.`

这四句把"七天"拆成三层：七天是**上限**，也是客户端无条件刷新一次的周期；多数用户会因为变更通知更早看到；通知本身是尽力而为，没收到通知的用户仍会在下一次刷新时对齐。还有一句限定企业侧不受客户端影响：

> `Regardless of WhatsApp client behavior, the server still honors the configured settings.`

也就是说，客户端界面还没刷新，不代表规则没生效——服务端按配置判定。设置页另写了一句"立即刷新"的客户端路径，本篇只登记它存在、不把它改写成操作步骤（1.3 节的界面口径）。变更通知的覆盖范围与规模由两句话给出：

> `Updates to calling settings sends change notifications to all users that have a chat with this business visible in their WhatsApp Inbox.`

> `One WhatsApp Business can have chats with any number of 3B+ WhatsApp users.`

前者说明通知对象是"会话列表里能看到这个业务的用户"，后者说明单个业务号码面对的用户规模理论上没有上限。两句放在一起，本篇读成"通知覆盖面广、但投递不保证"，而不是"所有用户一定同时看到"。

## 2.4 时窗、语音信箱与编解码

**呼叫时窗**约束的是用户能呼入的时段。官方写：

> `Configuring this setting restricts calls only to available weekly hours you configure.`

> `User-initiated calls are unavailable outside of the weekly hours and holiday schedules you set.`

两句把时窗的对象说清楚了：它管的是用户发起（UIC），不是企业发起；时窗之外，用户那一侧根本发不出呼叫。**语音信箱**是未接听之后的兜底，官方对它与时窗的关系给了一条硬约束：

> `When voicemail is enabled, turn off call hours, because WhatsApp users can't place calls outside business hours.`

这句话自带原因：时窗一关，用户在非营业时间连呼叫都发不出，语音信箱就没有被触发的机会，所以两者不能同时开。另一条配置约束是触发条件：

> `At least one trigger must be specified when voicemail is enabled.`

语音信箱开启后至少要给一个触发条件，官方没有列出"全部触发条件"的穷举表，本篇不补。留言的保留期与消息媒体不同：

> `This skips the standard 30-day TTL applied to messaging media so that announcements remain available for the lifetime of the configuration.`

这句话说的是语音信箱的播报素材跳过消息媒体的标准保留期，跟着配置本身的生命周期走；它不改变录音与转写的保留期（第 5 章另写）。留言的采集质量也有官方限定：

> `Voicemail collection is delivered as best-effort.`

> `If voicemail collection fails, Cloud API does not send a voicemail webhook for that call.`

两句合起来是一个"尽力而为 + 失败无回调"的口径：采集失败时不会补一条留言回调，企业侧不能把"没收到留言回调"读成"用户没留言"。

**编解码**只有两项。默认项：

> `Opus is the default audio codec for all WhatsApp calls.`

以及一句不可移除：

> `Opus is always enabled by default and cannot be removed.`

可选项只有 G.711，而且用途被限定在互操作场景：

> `You can enable G.711 (PCMA/PCMU) codecs for interoperability with legacy telephony systems or PSTN gateways.`

官方还给了一条选择建议：

> `Use Opus unless you have a specific requirement for G.711.`

常见问题页解释了为什么移动端只认 Opus：

> `WhatsApp mobile apps only support opus natively, so Meta media infra transcodes opus to other codecs if needed.`

五句话合起来是一条很窄的口径：Opus 是默认且始终启用的，G.711 是为传统电话网与 PSTN 网关准备的兼容项，移动端不原生支持 G.711、需要媒体层转码。本篇不把 G.711 写成"音质更好的选项"，也不写"应该开哪一个"——官方只给了"除非有特定需求否则用 Opus"这一条建议。

# 3 呼叫权限

## 3.1 为什么"先有权限"是前提

企业发起路径不是"拿到号码就能拨"。官方在那一页开头先写了一句前置条件：

> `Before you get started with business-initiated calling, ensure that:`

后面列的前提里，权限是第一项。常见问题页把权限与用户端设置的关系写得很直白：

> `Business initiated calls bypass 'Silence Unknown Callers' settings since the call can only happen after explicit permission provided by the user.`

这句话解释了一个容易误会的现象：用户的"静默未知来电"设置挡不住企业发起的呼叫。原因不是企业有特权，而是这类呼叫**只可能发生在用户已经明确授权之后**——静默设置针对的是陌生号码的主动呼入，而 BIC 的前提已经把"陌生"排除了。官方另有一句说明首次呼叫的场景：

> `This can happen on the first business initiated call to a WhatsApp user when there is no prior communication history between your business phone number and that WhatsApp user.`

这两句放在一起，本篇读成：权限是 BIC 的准入条件，不是可选的礼貌步骤；没有权限就没有合法呼叫，也就没有"绕过静默"这回事。

权限状态可以单独查询。参考页给出接口定位：

> `Use this endpoint to get the call permission state for a business phone number with a single WhatsApp user.`

这句话只说"能查单个用户对单个业务号码的权限状态"，没有列出状态的取值域；附表 D 在这批页面里没有取到权限状态的枚举，本篇不替官方补一个。查询结果怎么用、缓存多久、失败怎么重试，这批页面没有对应句子，附表 F 的 F10 与 F12 之外不再补行。

## 3.2 三种取得方式与两条取得位置

官方把取得权限写成"以下方式之一"：

> `You can obtain call permissions from the WhatsApp user in one of the following ways:`

第一种是发一条权限请求：

> `You can request call permissions by sending the WhatsApp user a permission request.`

这条请求不是随便发的，官方限定了它的载体：

> `Send it as a free form message during an open customer service window, or use a template message.`

也就是说，请求只能走两条路：客服窗口开着时用自由格式消息，或者用模板消息。前者受客服窗口约束，后者受模板审核与类别约束——两条约束分别属于本系列前几篇写过的消息规则，本篇只登记"官方把载体限定在这两条"，不展开模板怎么写。第二种取得位置在用户呼入时：

> `One way to obtain calling permissions is to request permission when a WhatsApp user calls your business.`

> `You can configure the call permission UI to automatically show in the WhatsApp user's client app when they call your business number.`

这两句说的是一条"顺带授权"的路径：用户打进来的时候，企业可以把权限界面配置成自动出现，用户在通话入口处完成选择。第三种是永久授权：

> `The user can also grant permanent permissions to the business at any time through their business profile.`

永久授权由用户在业务资料页自行给出，不经过企业的请求消息。三种方式对应三个不同的界面位置：请求消息、呼入时的权限界面、业务资料页；附表 E 的 E4 与 E5 两行把"呼叫权限"与"权限请求的次数与重置"按列排开，可以看到官方只在设置页与参考页写了这两项，总纲页、录音页与 SIP 页都没有按这两列写过。

客户端版本是这条能力的硬门槛：

> `WhatsApp Client versions 2.24.14.x and later support the call permission requests and business initiated calling feature.`

> `Both WhatsApp Android and iOS platforms support the feature.`

两句说明权限请求与 BIC 依赖较新的客户端版本，且两个移动平台都支持。更早的客户端会看到什么、请求会不会降级成普通消息，官方在这批页面里没有写，本篇不补。

## 3.3 配额与重置

权限请求有次数上限。参考页给了一个例子：

> `For example, your business can send only 2 permission requests in a 24-hour period.`

这句话的句式是"For example"，说明它是配额的示例值而不是唯一形态；官方在同一段没有给出按号码、按用户还是按应用计的口径，本篇只按句子本意登记"二十四小时内两次"这个量级，不把它扩写成"每个用户每号码两次"的断言。配额不是永久的，常见问题页给了一条重置条件：

> `A connected call will reset the call permission limits.`

两句合起来是附表 F 的 F2：一处写配额，一处写重置，本篇把两者并列，不把"两次"写成永久上限。另有一个容易混淆的数字在参考页：

> `A maximum of 5 requests in a one-second window can be made to the API.`

这一句说的是**接口请求速率**，对象是 API 调用，不是发给用户的权限请求条数；两者同名不同物，本篇分开写。权限请求被拒之后能不能再发、拒一次扣不扣配额，官方在这批页面里没有写，本篇不补。

## 3.4 改主意、首通显示与静默例外

权限不是一次授予就锁死。官方写：

> `The user may change their permission selection at any time.`

用户随时可以改选择，企业这一侧拿到的状态也会随之变化；官方没有写"改主意之后企业会收到什么回调"，这批页面里没有对应句子，本篇不补。首次呼叫还有一个与权限无关、但常被一起问到的显示问题：

> `Because the WhatsApp client has not yet received your business profile details, such as your display name, the incoming call screen may show your business phone number instead.`

> `This happens only on the first call between a given business phone number and WhatsApp user.`

> `Subsequent calls show your business display name.`

> `Meta is aware of this issue and fixes are underway to show the business display name on the first call.`

四句是附表 F 的 F5：首通可能显示号码而不是名称，原因不是权限没拿到，而是客户端还没拿到业务资料；官方把它写成"仅首通"的例外，并说修复在进行中。本篇按"首通可能显示号码、后续显示名称、官方称修复中"三段写，不把首通表现写成永久行为。

权限是用户一侧的动作，企业一侧也有对应义务。官方在设置页写了两句：

> `When calling is enabled on your business phone number, you are expected to pick up calls that WhatsApp users place to you.`

> `If a significant number of calls placed to your calling-enabled business phone number are not picked up, you will be notified and expected to make a change.`

这两句说明"开启通话"不是单向获得呼出能力，也意味着要接用户打进来的电话；大量未接听会触发通知与整改要求。它与第 7.5 节的负反馈限制是两套机制：一套按未接听比例提醒，一套按拉黑与举报累计到阈值后自动限制，本篇不把两者合成"处罚"。静默设置的例外在 3.1 已引，这里不再复述；用户端"静默未知来电"的具体界面写在《How to silence unknown callers》，第 8 章按界面页的口径只点名、不写成步骤。

# 4 一次通话的状态与回调

## 4.1 两组状态词与六类回调

《API and Webhook Reference》把通话回调写成"在以下呼叫事件发生时发出"，随后逐项列出三个状态：

> `This webhook is sent during the following calling events:`

> `Ringing: When the WhatsApp user's client device begins ringing`

> `Accepted: When the WhatsApp user accepts the call`

> `Rejected: When the call is rejected by the WhatsApp user`

这四项给出的是**状态词的第一组**：振铃、接听、拒绝。第二组在通话结束之后出现，官方在状态说明里写成"正常完成"与"失败"两个值，取值写在本篇附表 D 的两行 `status` 里（`COMPLETED`、`FAILED`）。这两组词不是同一个字段的两批取值：前一组描述"呼叫还在进行时用户做了什么"，后一组描述"这次通话最后怎么结束"。企业发起页另有一条容易漏读的说明——接听状态的回调在通话建立之后才到，而不是在用户按下接听的那一刻；这句话带字段标记，本篇只作中文陈述。

回调本身不止状态一种。这批页面里出现的有六类：接通回调（带媒体协商的应答）、状态回调、终止回调、录音可用回调、转写可用回调、权限结果回调。权限这一类容易漏掉，官方给了一句定位：

> `This webhook is sent back after requesting user calling permissions.`

也就是说，权限请求发出去之后，用户的决定会以回调的形式回来，企业不该靠轮询去猜。六类回调共用同一个 `calls` 字段入口，但各自的载荷不同；它们的字段名与取值逐项登记在附表 D 与附表 E 的 D 组、E 组。本篇只把"有哪几类、各自在哪一步出现"讲清楚，不把任何一类回调的载荷抄成可复制的模板。

## 4.2 接听窗口、预接听与"接听"的两个含义

接通回调到达之后，企业这一侧有一个接听时限。参考页把它写在接听接口的说明里：收到接通回调之后，大约有三十到六十秒的时间可以把电话接起来（这个区间连同单位登记在附表 B 的三、时长与滚动窗口档）。超过这个窗口不响应，用户那一侧会看到"未接听"的提示，并且企业会收到一条终止回调。这一段的原始句子带站点内链接标记，按引句规矩只作中文陈述。

"接听"在这批文本里有两个层次。客户端层面的判定是：

> `WhatsApp clients treat a call as accepted by peer if they receive a media packet or an accept signal whichever comes first.`

这句话说的是用户设备怎么判断"对方接了"：收到媒体包或收到接听信号，哪个先到算哪个。接口层面则另有一个"预接听"的动作，用来提前建立媒体连接：

> `The pre-accept is meant to start media connection establishment early but it does require controlling the timing of media transmission.`

两句合起来解释了为什么"接听"与"建立"不能混为一谈：预接听可以让媒体先动起来，但用户是否真的接了，仍要以媒体包或接听信号为准。参考页对"建立"另有一句定义，本篇已在 1.2 引用，不重复。

未接听在两侧的记录并不对称。业务侧收到的终止回调会把这次通话记成一个"已完成"的结束状态，但不会给出开始时间、结束时间与时长——官方的原句带字段标记，本篇只作中文陈述；用户侧看到的是"未接听"提示，并且这次呼叫会以终止回调回到企业。两侧说的是同一件事的两面，本篇按附表 F 的 F4 分开登记，不把业务侧的"已完成"读成"用户接了"。

## 4.3 谁终止、为什么主动终止更准

终止接口的定位很直接：

> `Use this endpoint to terminate an active call.`

官方紧接着给了两条使用规则。第一条是"即使媒体路径上已经有结束包，也要调用终止接口"——这句话带代码标记，本篇只作中文陈述；它防的是"看到对方发了结束包就以为不用管"的误读。第二条是主动终止对计费的影响：

> `Ending the call this way also ensures pricing is more accurate.`

> `When the WhatsApp user terminates the call, you do not have to call this endpoint.`

这两句合起来说明：用户挂断时企业不需要再调终止接口；企业主动结束时调终止接口，计费口径会更准。常见问题页另有一种"没有谁挂断"的结束方式：

> `In any case, the call can terminate after a certain duration of inactivity, after which a terminate webhook is sent.`

也就是长时间无活动之后由平台终止，并补发终止回调。三种结束方式（用户挂断、企业终止、无活动超时）都会落到同一条终止回调上，但触发者不同，计费与后续处理的起点也不同。

## 4.4 乱序、过期与超时

回调的到达顺序不是严格时序。官方给了一个具体场景：

> `For example the terminate webhook can arrive before the connect webhook if the WhatsApp user hangs up the call immediately after initiating the call.`

终止先于接通到达，说明"回调顺序"不能当作"事件顺序"来读。官方给的处理建议是按时间戳判断新旧：

> `The webhook server should determine stale webhooks based on timestamp value and avoid calling Graph APIs to further process them.`

这句话把判定责任放在接收端：用回调里的时间戳判断它是不是过期事件，过期的不要再拿去调接口。超时是另一类乱序来源。官方把超时链路写成三步：

> `The Meta HTTPS request to the webhook server timed out after ~20s.`

> `In this case, the server thinks it successfully handled a webhook request but from Meta's side, it failed due to timeout.`

> `Meta retries sending this webhook, which then appears as a duplicate.`

三步合起来是一个"双方认知不一致"的典型：Meta 侧二十秒左右判失败并重投，接收端却可能已经处理成功；重投到的那一条在接收端看来就是重复。附表 F 的 F9 把这一条单独登记，提醒读者"超时"不是单侧事件。更完整的重试过程在常见问题页还有一句：

> `The connect webhook is attempted which fails with timeout after ~20s.`

> `The retry of connect webhook happens after the 'terminate webhook'.`

> `In case of timeout, the webhook server thinks there is no failure but this is seen as failure that warrants a retry.`

这几句说的是接通回调超时之后，重投可能排在终止回调之后才到；接收端如果只按到达顺序处理，就会看到"终止之后又来一条接通"。本篇不替官方补"应该等多久"的阈值，官方在这批页面里只给了这个现象，没有给接收端的等待建议。

## 4.5 为什么同一条回调可能到两次

官方先把底线写明：

> `Due to the distributed nature of Meta's architecture, exactly-once delivery cannot be guaranteed for any webhooks including even messaging related webhooks.`

这句话的对象是包括消息回调在内的所有回调：精确一次投递不保证。随后官方列了几类已知的重复来源：

> `Calling related webhooks will likely have a shorter retry policy but stale webhooks may still be delivered as that may be useful information to a business to know that some consumers tried to reach them.`

> `Failure recovery on Meta queueing infrastructure may result in duplicate webhook sends.`

前者说重试策略与"过期回调仍可能投递"是两件事，后者说队列基础设施的故障恢复也会造成重复。多应用订阅是第三类来源：

> `If the phone number has more than 1 app subscribed to the calls field and Meta dispatches webhooks to app1 and app2 in that order.`

> `If app2 fails, Meta will retry the whole dispatch so app1 will receive a duplicate webhook.`

一个号码挂了两个应用、两个应用都订阅了通话字段时，Meta 会依次投递；后一个失败会触发整轮重投，前一个就会收到重复。官方还留了一句"可能还有未知原因"：

> `There could be other reasons that are currently unknown.`

所以正确的读法不是"列出全部重复原因"，而是"接收端按可能重复来设计"。附表 F 的 F8 把"重试"与"队列恢复"两类来源并列登记；本篇不把任何一条写成"唯一原因"。幂等键用哪个字段、去重窗口留多久，这批页面没有写，本篇不补。

## 4.6 多订阅、多地址、错误形状与日志

一个号码可以挂多个应用，也可以配多个回调地址。官方写：

> `Similar to messaging, multiple subscriptions with distinct apps associated with distinct callback URLs can be configured.`

> `Meta will dispatch all calling webhooks to all configured callback URLs.`

> `All URLs are treated as equal and there is no notion of primary/secondary.`

三句说明多地址是"全量投递"而不是"主备切换"：所有地址都收到同一批回调，官方明确没有主次之分。地址相同、消息与通话混在一个端点时，官方给了一条区分办法：

> `In this setup, the callback URI is the same for both messages and calls related webhooks but the webhook payload can be used to distinguish between the two categories of webhooks.`

以及一条总体建议：

> `In general, using a single app is recommended.`

这两句不构成硬性要求，官方用的是"建议"与"可以"；本篇按建议登记，不把它写成"必须单应用"。错误这一侧，《Troubleshoot WhatsApp Calling Errors - Reference Guide》先给形状：

> `When you receive an error, in the majority of cases the error shape will look like this:`

> `Use the Calling API error codes list below to identify and resolve calling errors.`

这两句只说明"多数情况下错误长这样、可以对照错误码表定位"，没有给出字段清单。日志的位置由三句描述：

> `The tab displays a table of recent call logs for your business phone numbers.`

> `Each log can have multiple events representing a Graph API request made by the business or a webhook sent by Meta.`

> `Each row represents a call, with different columns highlighted to provide information about each call log.`

三句把日志写成"按号码列出、一行一次通话、一行里可以有多条事件"的表；本篇只登记这个结构，不把它改写成界面操作步骤。回调来源另有一个字段区分自动与用户动作，取值写在附表 D 的 `response_source` 两值里（`user_action`、`automatic`）；本篇引用 D 表，不在这里补它的语义。

# 5 录音与转写

## 5.1 逐通选择、彼此独立

录音与转写是两项可选处理，不是通话默认的一部分。《Call recording》先给范围：

> `The Calling API can record the audio of business-initiated calls (BIC) and user-initiated calls (UIC) you make through the WhatsApp Business Cloud API.`

《Call transcription》给的是同一句式、不同动词：

> `The Calling API can transcribe the audio of business-initiated calls (BIC) and user-initiated calls (UIC) you make through the WhatsApp Business Cloud API.`

两项都覆盖企业发起与用户发起两条路径，这一点比很多读者预期的宽。选择方式是逐通：

> `Recording is opt-in on a per-call basis — you decide at the time you initiate or accept each call whether it should be recorded.`

> `You can enable either one on its own, both together, or neither.`

> `They are configured separately, each has its own request object, and each delivers its result in its own webhook event.`

三句把"逐通"与"独立"分开说：逐通指企业每一通都可以重新决定；独立指两项各有请求对象、各有回调事件，开一个不等于开另一个。官方另用一句把互不替代写死：

> `Enabling recording does not produce a transcript, and enabling transcription does not produce an audio recording.`

录音不产生转写、转写不产生音频，这两句在转写页有对称写法，本篇只引录音页这一句，避免同义重复。两项的开关状态用同一组枚举（`ENABLED` 等）表达，取值与字段位置登记在附表 D；本篇不在这里抄请求体。

## 5.2 告知、目的字段与拒绝权

录音开始之前必须先播告知。官方写：

> `When you opt a call into recording, both participants hear a short legally required announcement before the recording begins.`

告知不是事后补一句，而是混进双方的音频流：

> `Before any audio is recorded, the Calling API mixes a spoken announcement into both your business and the WhatsApp user audio streams.`

采集的起点被明确放在告知播完之后：

> `The recording starts only after the announcement has finished playing.`

这三句合起来是一条顺序约束：混入告知、播完、再采集。参与者有拒绝权：

> `A participant who does not consent can decline by terminating the call before or during the announcement.`

拒绝的方式是终止通话，而不是"在界面里点不同意"；官方把拒绝与挂断绑在一起。目的字段是这条链路上的硬性要求：官方写明开关处于启用状态时该字段必填（原句带字段标记），缺了会被请求错误挡回：

> `Calls submitted with recording enabled but without a purpose are rejected with a request error.`

转写页对告知与目的给出对称规则：

> `When you opt a call into transcription, both participants hear a short legally required announcement before transcription begins.`

> `Calls submitted with transcription enabled but without a purpose are rejected with a request error.`

也就是说，两项各自的告知与目的要求都不能省；本篇不把"只开转写"当成可以少播一次告知的情形。附表 E 的 E9 到 E11 三行把"是否逐通选择""告知""目的字段"按页排开，可以看到这三项只在录音页与参考页有句，其他列都是空。

## 5.3 同时开启时的合并播报

两项都开时，官方不让双方听两遍：

> `When you enable both on the same call, participants hear a single combined announcement instead of two:`

合并播报的字段归属有一条容易忽略的规则：两个对象同时存在时，播报使用录音对象的用途与语言，转写对象里的对应值被忽略。这句话在录音页与转写页各写一遍，且都带字段标记，本篇只作中文陈述、不整句照引。它带来的实践含义是：两项同开时，转写侧的用途文案不会出现在播报里，企业不能指望用转写对象的字段去改播报内容。语言支持有一条例外：

> `These values are valid, but until a localized transcription announcement is available they play the English announcement.`

这句话说的是某些语言值本身合法，但本地化播报还没就绪时，实际播放的是英文播报。官方没有列出"哪些语言已就绪"的清单，本篇不补；读者应以这句话为准——"值合法"不等于"播报已经本地化"。

## 5.4 产物、下载链接与保留期

通话结束之后，录音与转写各自异步产出。官方写录音回调的时机：

> `WhatsApp sends a webhook notification after a call with recording enabled ends and post-processing finishes (typically under one minute).`

转写页有一句同构的说明（转写回调在后处理完成后发出），本篇不重复引用。产物不是直接推给你，而是一个媒体标识加一个下载地址；参考页写下载地址的有效期（五分钟），这一句带字段标记，本篇只作中文陈述，数值登记在附表 B 的两行"五分钟"里。保留期两边各有一句：

> `Recordings remain available for download for 7 days after this webhook is delivered.`

> `Transcripts remain available for download for 7 days after this webhook is delivered.`

两句都说七天，但对象不同：一句说录音，一句说转写。过期之后：

> `After that period, the media ID expires and the underlying file is deleted.`

媒体标识失效、底层文件被删除；转写侧有一句同义说明，本篇不重复。产物可能不完整，官方自己留了一句：

> `In some cases call recording and transcript can be cut short.`

这句话没有给原因，也没有给比例；本篇只登记"官方承认可能被截短"，不补"什么情况下会截短"的清单。产物存放在哪里、能不能转移到企业自己的存储，这批页面没有写——附表 F 的 F12 把这一项登记为官方留白，本篇不替它补。

## 5.5 转写语言与分段

转写不需要企业指定语言：

> `You do not specify a transcription language in the request.`

> `The set of languages that can be automatically detected and transcribed is evolving constantly as the underlying speech models improve, so this list changes over time.`

两句说明语言是自动识别的，而且可识别的语言集合会随模型更新变化。这与 5.3 的播报语言是两件事：播报语言由请求里的语言字段决定，转写语言由系统识别；官方把两者分开写，本篇不把它们合成"设置一次语言"。产物的形态是分段文本：

> `Each segment represents a continuous span of speech from one speaker.`

一句说明每一段对应一个说话人的连续语音；官方没有给分段的时间精度与最大段长，本篇不补。

## 5.6 当前免费与未来定价

两项功能现在都不单独收费，但官方明确说未来会分别定价。录音页写：

> `Separate pricing for recording is planned for a future date, and this page will be updated with the rates and advance notice before any charges begin.`

转写页写的是同构的一句（把 recording 换成 transcription），本篇不重复引用。两项一起看时，官方另有一句总结：

> `Both features are currently free; when pricing is introduced, each will be priced separately (see the note above).`

《Calling API Pricing》把同一件事写成"没有上线日期"：

> `There is no launch date at this time, and this page and the feature docs will be updated with the rates and advance notice before any charges begin.`

四句合起来是附表 F 的 F6：当前免费、未来分别定价、费率和提前通知会写进页面、眼下没有上线日期。本篇按这四层写，不写任何具体日期，也不把"未来会收费"读成"很快收费"。

# 6 SIP：另一条信令路径

## 6.1 SIP 是什么、用户能不能感知

《SIP Configuration Guide - WhatsApp Business Calling》把 SIP 写成"用另一种信令协议替代 Graph API 端点"，这句话本篇已在 1.2 引用，不重复。它带来的第一个结论是：用户侧不区分企业走哪条路径。

> `The WhatsApp user calls business phone number and is unaware of whether the business is using SIP or Graph API.`

> `In other words, the user experience is identical.`

两句说的是"用户不知道、体验一致"：呼叫按钮、振铃、接听、挂断在客户端上是同一套界面，SIP 只是企业这一侧的信令选择。第二条结论是 SIP 仍然跑在 VoIP 上。官方在《Integration Patterns》写了整个呼叫流的合规边界，并以警告形式说明：通话流的任何一段都不允许接 PSTN，即使把 WhatsApp 通话桥接进 SIP 世界，也必须保证全程留在 VoIP。这句话带粗体警告标记，本篇只作中文陈述；同一页对"要不要用 SIP 中继"另有一句澄清：

> `SIP trunk by itself is not disallowed because technically a SIP trunk can be used without any PSTN at all.`

也就是说，被禁止的是 PSTN 落地，不是 SIP 中继本身。这个区分很重要：SIP 中继可以完全不碰 PSTN，而一旦有一段落到传统电话网，就超出官方允许的范围。集成模式页还给了 WebRTC 兼容性的总口径：

> `The WhatsApp Business Calling VoIP stack is designed to be compatible with WebRTC.`

> `However, to ensure smooth integration with the WhatsApp protocol, Meta restricts the supported functionalities.`

两句说明"兼容 WebRTC"不等于"WebRTC 的全部功能都能用"，Meta 为了与自家协议配合会收窄支持面；具体收窄项写在那一页的后续清单里，本篇不逐条抄成实现要求。

## 6.2 一个号码一个 SIP 服务器、一个应用

SIP 配置的第一条硬限制是数量：

> `Each phone number can have only one SIP server configured.`

一个业务号码只能配一个 SIP 服务器。官方还解释了一个常见误配：

> `Meta previously allowed multiple apps each with their own SIP server but this setup will not work because Meta will terminate the call after receiving BYE from any of the SIP servers.`

这句话说的是历史上的"多应用各配一个 SIP 服务器"已经不可行：任一服务器发来结束消息，Meta 就会终止通话，多服务器之间无法保持一致。查询时如果看到多个服务器，官方给了原因：

> `In the GET payload, if you see multiple SIP servers, it means you've used the POST API with different access tokens that belong to different apps.`

> `The associated app is extracted from the access token used to make the API call.`

两句说明"多出来的服务器"不是配置项，而是不同应用用各自令牌写进去的结果；应用身份由调用接口时使用的令牌决定。删除服务器的办法官方也写了（传空数组、必要时把状态置为停用），本篇只登记它存在、不复述为操作步骤。密码这一侧有一条容易误读的规则：

> `Meta generates a unique SIP user password for each business phone number + app combination.`

每个"业务号码＋应用"组合有各自独立的密码，不是号码级共享。密码怎么重新生成，官方只给了"停用 SIP、删除服务器、再加回来"这一条路径（附表 F 的 F11），本篇按 1.3 节的口径只登记"没有单独的重新生成接口"，不把它写成步骤。

## 6.3 证书、域名与双向验证

SIP 的 TLS 不是单向的。官方先写 Meta 这一侧出示什么：

> `Meta will present a valid server cert with subject name that covers our SIP domain wa.meta.vc.`

再写企业这一侧要做什么：

> `Your SIP server should do the same as Meta ensures your cert is valid and subject name covers SIP domain you configured on the business phone number.`

两句是**双向**要求：Meta 的证书要覆盖它自己的 SIP 域名，企业的证书要覆盖企业在号码上配置的域名，双方都要校验对方。证书校验失败是用户来电收不到 INVITE 的常见原因——官方在排错段把这一点写成"证书校验错误"，原句带粗体，本篇只作中文陈述。认证方面，用户发起的呼叫走摘要认证：

> `Meta SIP server supports digest auth for user-initiated calls.`

> `Your SIP server should respond with digest auth challenge, and Meta will resend the SIP INVITE with challenge response.`

两句说明挑战—应答的顺序：企业服务器先给出摘要认证挑战，Meta 再带着应答重发 INVITE。这套认证只管用户发起的呼叫；企业发起方向的认证材料是那组号码级密码，两条不要混。

## 6.4 媒体加密：SDES、DTLS 与 mTLS 的覆盖范围

SIP 的媒体加密由 SRTP 承担。官方先给定义：

> `The Secure Real-time Transport Protocol (SRTP) key exchange is a cryptographic protocol used to securely exchange encryption keys between two parties over an insecure communication channel.`

再给两种密钥交换方式：

> `You can configure SRTP key exchange to one of two options:`

两种方式是 SDES 与 DTLS，后者与 WebRTC 路径上的 ICE+DTLS 是同一族。选择 SDES 有一条明确的收益：

> `When SDES is used, there is no need for STUN, ICE, and DTLS which could help shorten the call setup time.`

省掉 STUN、ICE 与 DTLS 意味着建立更快，代价是密钥在信令里交换，安全边界与 DTLS 不同。官方在这批页面里没有替读者选，只把两种方式并列；本篇不写"应该选哪个"。

mTLS 是最容易读错的一处。Webhooks 文档把双向 TLS 写成 IP 白名单的替代方案，SIP 页先否掉这个替代：

> `The Webhooks documentation suggests mutual TLS (mTLS) as an alternative to an IP allowlist, but that alternative does not apply to SIP.`

紧接着又把"不支持"限定成"只在某个方向不支持"：

> `For SIP, Meta supports mTLS only on connections that Meta initiates to your SIP server, and mTLS does not cover media.`

两句合起来是附表 F 的 F7：SIP 上并非完全不支持 mTLS，而是只覆盖 Meta 主动发起的连接，且不覆盖媒体。把它读成"mTLS 在 SIP 上完全不可用"会多否掉一半；读成"mTLS 可以替代 IP 白名单"又会多肯定一半。防火墙这一侧另有一条要求：

> `If your SIP server or media endpoints are behind a firewall, you must allowlist these IP addresses for both SIP signaling and media.`

信令与媒体都要放行，不能只放行信令。官方还提醒不要按 SIP 头里的地址做白名单：

> `SIP headers in Meta's requests can contain addresses that are not the source of the traffic.`

> `This reference is solely to indicate the IP addresses to allow-list for SIP traffic.`

两句说明头里的地址可能不是流量来源，官方给出的地址表只用于配置放行、不用于识别来源。这一段与上一段的 mTLS 是两条独立的安全措施，本篇不把它们写成"二选一"。

## 6.5 SIP 回调默认关闭、且只是信息性的

SIP 号码的回调不是默认开的。官方写：

> `By default, when SIP is enabled, calling-related webhooks are not sent.`

> `You must explicitly enable [SIP call webhooks](#sip-call-webhooks) to receive call lifecycle events.`

开启之后的覆盖范围是：

> `SIP calls now support webhooks — providing call lifecycle events to your webhook endpoint when you enable webhooks on a SIP-enabled number.`

> `These webhooks apply to both business-initiated and user-initiated SIP calls.`

两句说明两条发起路径的 SIP 通话都会发生命周期回调。回调的性质有官方定性：

> `These webhooks are informational only, to keep your messaging system informed about call lifecycle events.`

> `A webhook notification is sent when a SIP call is attempted.`

也就是说，SIP 回调的用途是"让消息系统知道通话发生了什么"，不是驱动接听的指令；结构上终止回调与标准通话的终止回调一致：

> `The terminate webhook for SIP calls has the same structure as the standard call terminate webhook.`

但这不改变"默认不发、开了才有"的前提。本篇把这三层分开写：默认关闭、开启后覆盖两类呼叫、载荷只作信息同步——三者缺一，都会把 SIP 回调读成"和 Graph API 路径一样的控制通道"。

## 6.6 三类典型故障

SIP 排错页与常见问题页把故障分成三类：媒体被截、建立变慢、以及呼叫在固定时间被终止。

**媒体被截**的典型症状是用户少听到开头一小段：

> `A common symptom of this issue is that the WhatsApp consumer may miss approximately one second of audio from the business.`

> `For instance, if an IVR system is playing a sequence like "1-2-3," the consumer might only hear "2" or "3." The extent of audio loss varies based on the specific integration and the severity of the issue.`

原因在两条媒体腿的启动顺序：

> `When bridging two media legs, it's crucial for the media server to synchronize their readiness to prevent audio clipping.`

> `As a result, the media server drops packets, leading to audio clipping issues.`

两句说明媒体服务器如果不等两腿都就绪就开始桥接，会丢包、造成截断。官方给了两种缓解思路：让 WebRTC 先建立再播音频，或在 IVR 播静音期间完成建立：

> `This method allows the WebRTC connection to establish itself while the IVR is playing silence, reducing the likelihood of audio clipping.`

> `According to SIP RFCs, a User Agent Client (UAC) should be prepared to receive media immediately after sending the INVITE.`

> `Therefore, it's advisable to initiate the WebRTC connection setup process prior to sending the SIP INVITE.`

三句把"先建链、再送 INVITE"写成推荐顺序，并给出 SIP 规范上的依据：客户端在发出 INVITE 之后就要准备好接收媒体。

**建立变慢**的常见原因是 DTLS 握手：

> `A common reason for delayed setup of the media leg between your infrastructure and Meta is DTLS handshake completion.`

> `Meta cannot complete DTLS handshake until it receives your SDP.`

> `Common DTLS implementations have a retry interval of 1s, 2s, 4s, and so on.`

> `After you send Meta the SDP, there is often a ~3 second delay before Meta receives your DTLS client hello packet and this is when your internal endpoint is sending media but it is dropped.`

> `When you switch to SDES you can directly send SRTP after you send Meta your SDP.`

五句说明：Meta 必须收到企业的 SDP 才能完成握手；DTLS 的重传间隔是逐次翻倍的；SDP 发出之后通常还有约三秒的空窗；换用 SDES 可以在发出 SDP 后直接发 SRTP。三秒这个数值登记在附表 B 的三、时长与滚动窗口档；本篇不把它写成"固定延迟"，官方用的是"often"。

**固定时间被终止**有两种。常见问题页给的是用户发起的呼叫：

> `This problem manifests as a user initiated call getting connected, with bidirectional audio flow but user stops hearing business audio around ~33 seconds due to your SIP server timing out the SIP dialog.`

也就是约三十三秒后 SIP 服务器把对话超时、用户听不到企业音频。《Integration Examples》给的另一种是企业发起的呼叫：即便存在所描述的那个缺陷，通话仍会接通、媒体双向流动，但约三十二秒后 SIP 服务器终止通话，而这个终止不会传到 WhatsApp 客户端，原因是 BYE 请求的路由头写错了。这一句原文带内联代码标记，按全篇口径改写为中文陈述，不整句引。

两种情况的原因不同：前者是 SIP 对话超时，后者是 BYE 的路由头写错导致结束没有传到客户端。两种现象都表现为"通话被莫名切断"，但排查方向一个在对话保活、一个在路由头。再看用户发起呼叫收不到 INVITE 的情形：

> `If you are not getting SIP INVITE following a user initiated call or a SIP BYE following a user initiated call termination, possible reasons include`

这句只给"可能原因包括"的开头，后面是一张原因表；本篇不把表逐条抄成排查步骤，只登记官方把这一类问题归到信令入口。TLS 握手失败也是其中一项：

> `Common reason is TLS handshake failure when the SIP server is trying to establish a TLS session with Meta SIP server.`

三句合起来，SIP 排错的入口是：先确认信令有没有到（INVITE/BYE），再确认 TLS 与证书，最后确认媒体腿的就绪顺序。

# 7 计费与限额

## 7.1 计费单位与脉冲进位

计费的起点是通话本身。《Calling API Pricing》先给一句总纲：

> `Businesses are charged for calls based on:`

具体怎么按分钟折成脉冲，官方给了一个例子：

> `For example, a 56-second call (9.33 pulses) would be counted as 10 pulses.`

这句话把"不足一分钟怎么算"写清楚了：五十六秒按九点三三个脉冲计，向上进位到十个脉冲；本篇不把它推广成"所有不足一分钟都进一位"的规则，官方只用这一个例子说明进位方向。费率卡的基准时间与口径是：

> `These rate cards represent the current rates and volume tiers for the WhatsApp Business Calling API, effective April 1, 2026, based on Messaging account timezone.`

这句话给了三个限定：费率卡是"当前"的；生效日是四月一日；按消息账户的时区计算。三个限定缺一个，读出来的生效时间就可能差一天。

## 7.2 档位、跨档与后续调整

通话按量分档计价，跨档时的算法容易误会。官方写：

> `For calls that cross pricing tiers (for example from the 0 - 50,000 tier to the 50,001 - 250,000 tier), the entire call is priced at the lower rate (that is, the rate of the higher volume tier).`

这句话说的是"整通按较低费率计"，而不是"跨档部分按新档计"；括号里给了档位边界（五万与二十五万两级），本篇照引，不做四舍五入。定价页还列了后续调整：

> `The following tables show future updates to the rates.`

> `Rates effective July 1, 2026 across 16 currencies are reflected below.`

> `Rates effective October 1, 2026 across 16 currencies are reflected below.`

三句给出了两组未来费率表的生效日与币种数；本篇只登记"官方把后续调整单独列表"，不把它们读成"现行费率已经变了"。市场拆分有一条专门的说明：

> `Effective October 1, 2026, these 9 markets move out of their respective "Rest of" pricing region and become standalone on calling rate cards: Bangladesh, Iraq, Kazakhstan, Kuwait, Morocco, Nepal, Oman, Sri Lanka and Ukraine.`

> `Calling rates do not change on October 1, 2026.`

> `Each of the 9 markets keeps the rate of the region it is leaving; no market's per-minute rate or volume tier thresholds increase or decrease.`

三句合起来是"市场从区域档里独立出来、但费率不变"：九个市场单独成卡，各自保留原区域的价格与档位阈值，同一时点费率没有涨跌。这一条把"市场拆分"与"涨价"分开，是本篇认为最容易被标题党读错的一处。

## 7.3 并发上限与二十四小时窗口

通话的规模限制写在《WhatsApp FAQ》。先看并发：

> `The only limit enforced for calling right now is the 1000 concurrent calls limit, but this may change in near future.`

这句话有两个限定：一千路是**当前**唯一被执行的并发上限，且官方说近期可能变化。再看时间窗口：

> `Limit on connected calls in 24 hours is a time window based running limit.`

> `Think of this as a rate limit for business initiated calls.`

三句是附表 F 的 F3 与 F10 的一半：一千路并发与二十四小时窗口是两个不同对象，前者是同时进行的通话数，后者是滚动窗口内已接通通话的量；官方把后者比作"企业发起呼叫的速率限制"，但没有给出具体条数。超过限制时用户侧看到什么，官方另有三句：

> `When the rate limit is exceeded, the caller (the WhatsApp user) will get a generic message saying call cannot be placed.`

> `No message is played and there is no webhook.`

> `This limit is expected to increase and hence chances of this happening are low.`

三句说明：超限时用户只收到一句笼统提示，没有播报、也没有回调；官方预期这个限制会提高。企业侧不能靠"有没有回调"判断是否超限——这一条与 4.5 的"没收到回调不等于没发生"是同一种读法。

## 7.4 通话占不占消息限额、客服窗口怎么算

通话用量与消息限额是两套计量。官方写：

> `Calling API usage does not count towards messaging rate limits at the moment.`

这句话的对象是"消息速率限制"，回答的是"打电话会不会吃掉发消息的额度"；答案是不会。但这不等于通话没有自己的限制——7.3 的两句已经说明通话另有并发与窗口。两句放在一起，就是附表 F 的 F10："rate limit"这个词在两处指的不是同一件事。

客服窗口这一侧，通话会参与计时：

> `With the introduction of the Calling API, the customer service window now also starts or refreshes for calls:`

> `When you are within the window, your business can send any type of message to the WhatsApp user, which is otherwise not allowed.`

两句说明：通话可以让客服窗口开始或刷新，窗口内企业可以发任意类型的消息。也就是说，通话本身不计入消息限额，但它会影响"能不能发自由格式消息"的窗口状态；两件事方向不同，不能互相替代。官方在定价页还有一句工具性说明：

> `The endpoints can provide useful information like cost, counts of completed calls, and average call duration.`

这句话只说接口能给出费用、完成通话数与平均时长这类信息，没有列出字段名；本篇不补接口清单。

## 7.5 负反馈与七天自动限制

通话能力有一套独立于消息质量评级的负反馈机制。官方先给触发面：

> `If your calls receive high negative user feedback, such as blocks and reports, business-initiated calling, user-initiated calling, or both functionalities on your phone number can be restricted.`

也就是说，被限制的可能是企业发起、用户发起，或两者同时。预警是分层的：

> `You will be notified when the business phone number is close to being paused as an early warning.`

> `If either business or user initiated calling are close to being paused, you will receive a webhook for the respective violation type.`

两句说明：接近被暂停时先有通知，按违规类型分别发回调。真正触发限制时：

> `Once the negative user feedback reaches a threshold, Cloud API automatically restricts calling functionality on your phone number for a period of 7 days.`

> `Once the negative user feedback reaches a threshold, Cloud API automatically restricts user initiated calling functionality on your phone number for a period of 7 days.`

两句分别对应"两类功能一起被限制"与"只限制用户发起"，期限都是七天。官方没有给出触发阈值的具体数值，本篇不补；也没有写"七天后一定恢复"，只写自动限制七天，本篇按句子本意登记。这一套与 3.4 的"未接听提醒"是两条线：一条按负反馈累计，一条按未接听比例；本篇在第 10 章把两者并排列出，避免读者把它们当成同一套处罚。

# 8 用户端入口

## 8.1 呼叫按钮与深度链接

企业这一侧能把通话摆到用户面前的入口有两类：会话里的呼叫按钮，以及可被点开的通话链接。呼叫按钮的官方定位写在《Send WhatsApp Call Button Messages and Deep Links》：

> `After you adopt Cloud API Calling features, you can raise awareness with your customers in two core ways:`

两种方式下文会分别展开，但这句话先说明一个前提：入口是"采用能力之后"才有的事，不是一个默认出现在每条会话里的按钮。点击之后发生什么：

> `When a WhatsApp user clicks the call button, the click initiates a WhatsApp call to the business number that sent the message.`

这句话把按钮与"这套 API 的通话"绑在一起：点击发起的是对该业务号码的 WhatsApp 通话，而不是跳到外部拨号器。《Calling API App Review Guidelines》另有一条要求，说明按钮的可见状态要能反映能力开关：

> `You should clearly show that your app can enable and disable Calling API features by displaying whether the Call Button icon is visible.`

这条要求属于应用审核指南，不是产品功能；本篇只登记"审核要看按钮可见性与能力开关是否一致"，不展开审核材料怎么准备。按钮与链接的文案、图标与放置位置，官方在这两页里没有写成可引用的规格句，本篇不补。

## 8.2 沙盒能测什么、接收名单有几条

《Set Up a Sandbox Account for Calling》把沙盒定义成一个模拟的消息账户：

> `A WhatsApp sandbox account is a mock Messaging account that you can use to test your Calling API integration.`

它的用途是测试接入，不是生产；沙盒的通话限额与生产号码不同：

> `The following table outlines the calling limits for sandbox accounts.`

这句话只说明"有限额表"，具体数值在那一页的表里，本篇不抄表。同一时间只能有一个沙盒账户：

> `You can only have one sandbox account at a time, so if you already claimed a sandbox account with a different feature, delete it and claim a new one.`

这条限制意味着"已经用别的功能占了一个沙盒"时，要先处理掉旧的才能测通话；本篇只登记这条规则，不写操作步骤。接收范围也被收窄：

> `Your sandbox test phone number can only exchange messages and calls with phone numbers on its recipient list.`

同一页另写名单上限是五个收件号码，并附一条与错误码相关的提示；这一句原文带粗体小标题与内联代码标记，按全篇口径改写为中文陈述，只登记上限与提示存在，不复述错误码的处理。

两句说明沙盒号码只能与名单内的号码互通，名单上限是五个（数值登记在附表 B 的账号、号码与设备规模档）。名单怎么加、报错码怎么处理，官方在那一页有表格与提示；本篇按界面口径只点名与计数，不复述为步骤。

## 8.3 通话链接、访客与等待室

《How to create and share WhatsApp call links》写的是用户侧的通话链接。链接的安全属性先给：

> `Every time you create a call link, the URL is different and secure, so no one can guess your call links.`

> `Personal calls from call link calls are end-to-end encrypted.`

两句说明每次创建的链接都不同、不可猜，并且个人通话走端到端加密。链接不是永久的：

> `Links expire if they’ve been unused for 30 days.`

> `Links can be revoked by WhatsApp for privacy and security reasons.`

两句给了两种失效方式：闲置三十天失效，以及官方出于隐私与安全原因撤销。访客是链接带来的特殊参与者：

> `People without a WhatsApp account can join a call link as a Guest from their web browser (desktop).`

> `Guests can participate with audio, video, screen sharing, and reactions.`

> `Guests can’t add or remove other participants, create new calls, or send messages.`

三句把访客的能力与边界分开：没有账号也能从桌面浏览器加入，能参与音频、视频、共享与回应，但不能增删参与者、不能新建通话、不能发消息。访客在等待室里、而所有 WhatsApp 用户都离开时，通话会结束：

> `If a guest is in the waiting room and all WhatsApp users leave the call, the call will end for everyone including guests.`

> `Otherwise, calls continue until everyone, including guests, leave the call or until 60 minutes have passed.`

两句说明两种结束条件：所有人离开，或达到六十分钟上限（数值登记在附表 B）。等待室的准入由创建者控制：

> `Only the call link creator can see who is in the waiting room and admit or deny people onto the call.`

等待室容量上限为一百二十八人，这一数值同样登记在附表 B；官方把容量写在界面说明里，本篇只登记数字、不写"怎么批准"的步骤。这里有一个容易混淆的点：六十分钟上限与三十天失效是两个不同对象——前者是单次通话的时长，后者是链接的闲置寿命；本篇把两者分开写，不把它们读成"通话最多保存三十天"。

## 8.4 等待、转移、静音与 IP 保护

**呼叫等待**写在《How to use call waiting》。官方先给选择权：

> `You can choose to either answer or decline the call.`

> `This won’t interrupt your existing call.`

两句说明等待中的来电不会打断当前通话。漫游与流量另有一句：

> `Charges may also apply for WhatsApp voice and video calls while roaming, or if you exceed your data limit.`

这句话把"通话不占套餐分钟"与"可能产生流量费"分开：通话走数据，不走进运营商语音套餐，但漫游或超出流量上限时仍可能计费。

**跨设备转移**写在《How to transfer a WhatsApp call to a second device》。官方先给结果：

> `When you use Call transfer , the call connects on the new device and ends on the previous device.`

再给次数：

> `There isn’t a limit on transfers so you can move the call back and forth between devices. : Call transfer is currently only available for group voice calls, group video calls, and call links.`

两句说明转移是"新设备接、旧设备断"，且没有次数限制；但可转移的通话类型有限——官方在同一句的后半段限定为群组语音、群组视频与通话链接，个人一对一的语音通话不在其列。这个限定容易被"没有次数限制"盖过，本篇单独点出。群组语音这一侧，《How to make a group voice call》另有一条参与者边界：

> `Only group members can participate in group calls.`

也就是说，群组通话的参与者范围由群成员决定，访客只能通过通话链接这一条路径进入（8.3）。

**静默未知来电**写在《How to silence unknown callers》。先定义"未知"：

> `An unknown caller might be someone you haven’t had contact with before, someone you haven’t saved as a contact, or a suspicious number .`

再给用户看到的判断材料：

> `When you receive a call from an unknown caller, we’ll show you details that you can use to determine how to respond.`

> `These details will let you know that they aren't a contact, if you have groups in common, and what country their phone number is registered in.`

两句说明静默不是"直接拦截"，而是"不响铃、但把判断材料摆出来"。静默之后仍然可见：

> `You can see silenced calls from unknown callers in the Calls tab and your notifications, but your phone won’t ring.`

以及一条反向规则：

> `If you call or message the silenced caller, you won’t be able to silence their calls in future.`

这句话说明用户一旦主动联系过被静默的号码，静默对该号码就不再生效；本篇按句子本意登记，不把它推广成"所有静默都可以用主动联系解除"。企业发起的通话为什么不受这一设置影响，已在 3.1 引用官方原句，这里不重复。

**IP 保护**写在《How to protect your IP address on WhatsApp calls》。官方先给效果：

> `If you turn on Protect IP address in calls , your calls won’t be connected directly and will avoid revealing your IP address to the person you’re calling.`

再给群组通话的默认值：群组通话默认经 WhatsApp 服务器中继（原句与开关说明混排，本篇只作中文陈述）。中继不改变加密属性：

> `Calls relayed through WhatsApp servers are end-to-end encrypted, like all calls on WhatsApp.`

这句话说明"经服务器中继"与"端到端加密"可以同时成立，读者不应把中继读成"平台能听内容"。

**通话历史**写在《About call history on WhatsApp》。三条口径值得并排：

> `WhatsApp calling uses your phone's internet connection to make calls, not your phone plan.`

> `WhatsApp calls won’t show on your phone bill.`

> `Data rates will apply through your mobile provider.`

三句说明通话走网络、不上手机账单、但可能产生数据费。历史的导出与恢复有两条否定：

> `You can’t email or export your call history.`

> `The only way to restore call history is through a backup.`

两句说明通话历史不能导出、只能靠备份恢复；这与 7.4 的计费口径是两条线——账单看运营商，历史看客户端备份。

## 8.5 视频通话与数据节省

一对一视频通话写在《How to make a 1:1 video call》，一对一语音通话写在《How to make a 1:1 voice call》。视频这一侧最值得登记的是数据节省模式：

> `When Data Saver is on, the video you receive will be lower quality so the call uses less data.`

> `The video your contact sees from you stays at full quality.`

两句说明节省是单向的：你收到的视频降质，你发出的视频保持原画质。适用范围也被限定：

> `The incoming video quality will drop within a few seconds. : Data Saver only applies to 1:1 video calls on cellular data.`

> `It doesn’t apply on Wi-Fi, group calls or voice calls.`

两句说明数据节省只对一对一视频、且只在蜂窝数据下生效，Wi-Fi、群组通话与语音通话都不适用。视频通话还有一条版本门槛，官方写的是某个安卓版本或更新（版本号登记在附表 B 的生效日期与版本节点档）；本篇只登记"有版本门槛"，不把它写成"所有设备都支持"。

## 8.6 听不到声音与打不了紧急号码

“听不到声音”这一页（《Can't hear voice messages, status audio, call audio, or videos》）把原因拆成三类。第一类是设备静音模式：

> `If your device is on vibrate only or silent, you will also have no sound when playing a video status with audio or during a call.`

第二类是距离传感器：

> `Check your phone’s proximity sensor WhatsApp uses your phone's proximity sensor to play audio through the earpiece when you're holding your phone to your ear.`

> `However, some screen protectors and cases can disrupt this.`

两句说明听筒切换依赖距离传感器，而贴膜与保护壳可能干扰它。第三类是误触：

> `If your screen goes black and you're unable to hear the other person on a call or listen to voice messages, it's possible that your hand or fingers are accidentally triggering the proximity sensor.`

> `If your screen turns off immediately, there's either an issue with your phone's proximity sensor or with your phone's software.`

两句把"屏幕变黑、听不到"归到传感器被遮挡或软件问题。这三类都写在用户侧的排错页，本篇只登记官方给的指向，不把它们整理成排查步骤。紧急号码这一侧有一条硬边界：

> `You can’t access emergency service numbers through WhatsApp such as 911 in the United States.`

这句话说的是 WhatsApp 不能拨打紧急服务号码；它适用于所有通话路径，也包括企业发起与链接通话。也就是说，这套能力不是"任何号码都能呼叫"的通用电话，业务号码也不能被用来触达紧急服务。

# 9 横向对照

## 9.1 三种"通话"对象

把这批页面横排开，会发现"通话"至少有三个对象，混用会让后面所有判断都错位。

第一种是**API 通话**：由 Cloud API 发起或接收、有权限、有状态回调、可录音转写、按脉冲计费的通话。它的规则写在开发者文档的十五页里。第二种是**帮助中心的用户通话**：一对一语音、一对一视频、群组语音、通话链接；它描述的是客户端里用户看到的功能，与 API 通话共用"通话"这个词，但多数规则（呼叫按钮在哪、数据节省怎么开、历史怎么恢复）不适用于 API 侧。第三种是**通话链接**：一种把非账号用户带进通话的机制，有等待室、访客与六十分钟上限；它可以和 API 通话同时存在，但不是同一个对象。

三种对象的交集只有一个：都叫"通话"、都走 VoIP、都受端到端加密或传输加密保护。除此之外，权限、限额、计费、回调都不通用。本篇第 2 章到第 7 章只写第一种，第 8 章只写第二、三种；附表 E 的列之所以按站点拆开，就是为了让读者看到"同一维度在两边各写了什么、哪边没写"。

## 9.2 附表 E 的 165 个空格该怎么读

附表 E 是 **33** 个维度 × **7** 列 = **231** 格，其中 **66** 格取到了官方原句、**165** 格标作"官方未按这一列写该项"。空格的含义只有一个：官方在这批登记页面里没有按这一列写这一项，**不代表该主体没有这项规则**。举三处：

- E2"默认是否开启"：总纲页写测试号码默认关闭，设置页写业务号码默认关闭，SIP 页写回调默认不发；录音、转写、参考、定价与帮助中心五列都是空。空不代表录音默认开启，只代表这几页没有按"默认"这一项写过。
- E7"接听时限"：参考页给了三十到六十秒的接听窗口，常见问题页给了约二十秒的回调超时，录音页给了后处理通常不到一分钟；这三处时长对象完全不同，不能合并成一个"时限"。
- E12"产物与下载"：只有录音页与参考页有句，其余五列全空；这不代表 SIP 通话没有录音，只代表 SIP 页没按这一项写。

读 E 表的正确方式是**按行看对象、按列看页面**：一行的空格多，说明这一项只在少数页面被写成规则；一列的空格多，说明这一类页面不负责这一项。本篇在正文里引用 E 表时，只引用取到原句的格子，不用空格去反推"官方没有这项能力"。

## 9.3 附表 F 的 12 个议题里，哪几处只能按一侧写

附表 F 收的是"官方多处写法不一致"与"官方在这批页面里根本没写"两类。**12** 个议题里，两侧都取到原句的 **10** 个、只有一侧有原句的 **1** 个、两侧都没有原句的 **1** 个。本篇采用的方式分三种：

第一种是**两侧并列写进正文**（F1 七天生效、F2 配额与重置、F6 免费与未来定价、F9 超时两侧认知）。这几处正文各引两侧原句，读法在对应章节里给出，不在这里重复。

第二种是**按限定后的口径写进正文**（F3"唯一限制"与二十四小时窗口、F5 首通显示、F7 SIP 的 mTLS、F8 重复的两类来源、F10 两种 rate limit）。这几处的共同点是：一侧的说法看起来更绝对，另一侧把它限定住了；本篇先写限定，再写原说法，不把两者合成一个折中结论。

第三种是**只在附表登记、正文不替官方补结论**（F4 未接听在两侧的记录、F11 SIP 密码重新生成、F12 录音与转写产物存放位置）。F4 两侧都有原句，但它们是"业务记录"与"用户提示"两个视角，正文只在 4.2 作中文陈述、不整句照引；F11 只有一条路径，正文只登记"没有单独的重新生成接口"；F12 两侧都是空白，正文不写产物存放在哪里。

这三种处理方式的共同边界是：**不把两种说法合成一种，也不把官方的空白补成规则**。读者如果要用这篇做判断，应当先看附表 F 的"差异性质"一列，再回正文找对应小节；两处对不上时，以附表 F 的行题与出处为准。

# 10 容易读错的六处

## 10.1 "唯一限制"不等于"没有别的限制"

常见问题页那句"当前唯一被执行的限制是一千路并发"，如果只读前半句，会得到"除了并发没有别的上限"的结论；但同一页后面又写了"二十四小时内已接通通话是一个滚动窗口限制"，并把它比作企业发起呼叫的速率限制。两句话的对象不同：一个数**同时**，一个数**一段时间内累计**；一个给出具体数值，一个只给机制。把它们合成一句"通话只有一个限制"，或者反过来读成"官方对通话没有总量限制"，都不对。本篇的处理是分开写（7.3），并在附表 F 的 F3 登记"措辞不一致"。

## 10.2 四个"七天"不是同一件事

这批页面里至少出现四种七天，彼此不能换算：

- **配置生效**：设置变更后用户最多七天看到，常见问题页说多数更快（2.3、F1）。
- **媒体保留**：录音与转写产物在回调送达后可下载七天（5.4）。
- **负反馈限制**：负反馈累计到阈值后，通话功能被自动限制七天（7.5）。
- **客户端刷新**：所有客户端无论有没有收到变更通知，都会每七天刷新一次业务信息（2.3）。

第二与第三个尤其容易混：一个是"东西还在"，一个是"功能被停"。看到"七天"先问它修饰的是哪个对象，是本篇认为最省事的读法。

## 10.3 "用户不能主动打给企业"与"企业不能打给陌生用户"不是同一句话

设置页在讲"全部关闭"这个选项时有一句"WhatsApp 用户不能对企业发起未经邀请的呼叫"，企业发起页又有一整套权限前提。两句话方向相反、约束的对象也不同：前一句约束的是**用户侧的呼入**，后一句约束的是**企业侧的呼出**。把前一句读成"用户完全不能呼入"，会与用户发起路径的存在矛盾；把后一句读成"企业只要拿到号码就能呼出"，会与权限前提矛盾。正确的读法是：用户呼入需要企业开启通话并保留入口，企业呼出需要用户先给权限；两条路径各有各的门槛，不能互相顶替。

## 10.4 "未接听也算完成"不等于"用户接了"

业务侧的终止回调把未接听记成完成状态，但没有开始时间、结束时间与时长；用户侧看到的是"未接听"提示。两句话描述的是同一次呼叫的两面：业务侧只有"结束了"这一层信息，用户侧才有"没接"这一层信息。把"完成"读成"接通"，会连带把计费、录音与客服窗口都算错——没有接通就没有通话时长，也不会有媒体产物。本篇在 4.2 与附表 F 的 F4 分开登记，正文不把两侧合成一个状态。

## 10.5 SIP 上的 mTLS 是"范围受限"，不是"完全不可用"

Webhooks 文档把双向 TLS 写成 IP 白名单的替代方案；SIP 页先说"这个替代不适用于 SIP"，紧接着又说"Meta 只在它主动发起的连接上支持 mTLS，且不覆盖媒体"。只读第一句会得到"SIP 不支持 mTLS"；只读第二句会得到"mTLS 在 SIP 上可用"。两句合起来才是官方口径：**方向受限、媒体除外、不能拿它替代白名单**。本篇在 6.4 与 F7 按这个限定写，不把任何一半当成全部。

还有一层容易混：mTLS 讲的是对端身份验证，TLS 连接复用讲的是同一条连接能不能继续用。官方对后者单独写了一句：

> `Do not rely on TLS connection reuse for Meta initiated SIP requests (mid-dialog messages such as BYE).`

也就是说，即便 TLS 层的加密成立，Meta 主动发起的 SIP 请求也不保证复用连接；把"连接复用"读成"身份已验证"、或把"不复用"读成"没有加密"，都是把两件事并成一件。

## 10.6 两个 rate limit 同名不同物

常见问题页一处说"通话用量目前不计入消息速率限制"，另一处说"可以把二十四小时窗口想成企业发起呼叫的速率限制"。两处的 rate limit 指的是不同对象：前者是消息侧的配额，后者是通话侧自己的窗口。把两句读成"通话也受消息限额约束"或"通话完全没有速率限制"，都会错。本篇在 7.3 与 7.4 分开写，并在 F10 登记"同名不同物"。同样地，"五分钟下载链接"与"七天媒体保留"也不是同一层：前者是地址的有效期，后者是文件的保留期，地址过期不代表文件立刻删除，文件删除也不代表地址还能用。

# 11 结论与边界

## 11.1 一条通话的内部边界

沿一次通话走完，可以得到一条清楚的内部边界：**通话是两条发起路径共用的实时会话对象；企业发起以用户权限为前提，用户发起以企业开启通话与保留入口为前提；通话一旦建立，状态、终止、录音转写、计费与回调都走同一套对象；配置变更有上限、有通知、有服务端兜底；回调可能乱序、可能重复、可能超时，接收端按"可能重复、可能过期"设计；录音与转写逐通选择、彼此独立、先告知后采集、目的必填、产物有链接有效期与保留期；SIP 是另一条信令路径，用户无感，但配置数量、证书、加密范围与回调默认值都另有规则；计费按脉冲与档位，限额分并发与窗口，负反馈另有七天限制；用户端的按钮、链接、访客、等待室、静音与 IP 保护属于另一层文本，不能反推 API 行为。**

这条边界之外，官方在这批页面里没有写的部分同样清楚：权限状态的完整取值域、权限请求被拒后的冷却规则、去重键与去重窗口、录音与转写产物的存放位置、负反馈阈值的具体数值、二十四小时窗口的具体条数、SIP 密码的单独重生成接口、视频通话在 API 侧的独立规则。这些不是本篇漏掉的内容，而是官方文本在这批登记页面里没有给出句子；附表 F 把其中成对的与留白的议题逐条登记，正文不替官方补结论。

## 11.2 本篇的边界

本篇只读官方文本，写的是**口径**：哪个词在哪一页指什么、哪条规则带什么前提、哪两个数字不能合并、哪两处"通话"不是同一个对象。它不是接入指南，不含令牌获取、SDP 构造、媒体服务器配置、SIP 服务器搭建、回调验签实现、重试队列参数；也不是运营方案，不含任何利用通话能力、权限请求或呼叫链接向非自愿收件人反复投递或骚扰的方法。凡涉及"怎么做"的部分，本篇只点名与计数，把动作留给原页；凡涉及"该不该"的评价，本篇不写。读者若要把这些口径落到实现，请以官方页面为准，并自行承担与平台条款、当地法律相关的那部分判断。

# 附表 A 官方页面登记清单与本篇引用位置（26 行）

登记口径与系列前篇一致：**凡本篇抓取的页面，一律登记**。全表 **25** 个登记位，按站点分为 Meta 开发者文档 15、WhatsApp 帮助中心 10。取回状态由脚本实测各层正文的非空白字符数得出，分档统计：已取回 25。其中**可引用 25 页、不可引用 0 页**：本轮登记页正文全部取回，没有需要标注「全篇不引用」的页面。

帮助中心页的正文取回长度按裁剪后正文计：抓取原始页含平台页签（Web／Windows／Mac／iOS／Android）与固定尾栏，这些行不计入长度，也不作为引文出处。Meta 商业帮助中心的页面正文从页面内嵌的转义 JSON 载荷里抽取，抽取残骸（单独的 u 记号与 DOM 类名）整行剔除，不做猜测修补，被剔除的行也不作为引文出处。

第 4 列「本篇引用位置」的小节号是实测的，不是事先规划的：由 _tmp/wa6_postitle.pl 扫成稿分片里的页名标记（正文每点名一次某页的官方标题就留下一枚这种内部标记，展开后成为标题）得出，写进 _tmp/w6_a10_postitle.tsv，再由 _tmp/wa6_pos.pl 拼进这一列；判据与 _tmp/wa6_cover.pl 同口径——**这一列只写标题点名的位置，所以按小节号翻过去一定能撞见这一页的官方标题**。章首（未进入任何小节的章引言）写作「第 N 章章首」；某页只在别处被整句引用而标题未点名的节不出现在这一列，那种句子按附表 C—F 的句序回查。

| 站点 | 页面标识 | 官方标题 | 本篇引用位置 | 正文状态 |
| --- | --- | --- | --- | --- |
| Meta 开发者文档 | `calling_app_review_guidelines_md` | Calling API App Review Guidelines | 正文 8.1、附表 C（3 处）、附表 E（1 处） | 已取回（正文 1660 字符） |
| Meta 开发者文档 | `calling_business_initiated_calls_md` | Business-Initiated Calls | 正文 1.1、2.2、附表 C（12 处）、附表 D（2 处）、附表 E（4 处） | 已取回（正文 27613 字符） |
| Meta 开发者文档 | `calling_call_button_messages_deep_links_md` | Send WhatsApp Call Button Messages and Deep Links | 正文 8.1、附表 C（1 处）、附表 E（1 处） | 已取回（正文 11896 字符） |
| Meta 开发者文档 | `calling_call_recording_md` | Call recording | 正文 1.1、5.1、附表 B（2 处）、附表 C（10 处）、附表 E（7 处）、附表 F（1 处） | 已取回（正文 11570 字符） |
| Meta 开发者文档 | `calling_call_settings_md` | Configure Call Settings | 正文 1.1、2.2、附表 B（2 处）、附表 C（7 处）、附表 E（5 处）、附表 F（1 处） | 已取回（正文 34016 字符） |
| Meta 开发者文档 | `calling_call_transcription_md` | Call transcription | 正文 1.1、5.1、附表 B（2 处）、附表 C（1 处） | 已取回（正文 15667 字符） |
| Meta 开发者文档 | `calling_faq_md` | WhatsApp FAQ | 正文 1.1、2.3、7.3、附表 B（13 处）、附表 C（12 处）、附表 E（11 处）、附表 F（12 处） | 已取回（正文 32757 字符） |
| Meta 开发者文档 | `calling_integration_examples_md` | Integration Examples | 正文 6.6、附表 B（1 处）、附表 C（2 处） | 已取回（正文 37809 字符） |
| Meta 开发者文档 | `calling_integration_patterns_md` | Integration Patterns | 正文 6.1 | 已取回（正文 7270 字符） |
| Meta 开发者文档 | `calling_md` | Cloud API Calling | 正文 1.1、2.1、附表 E（5 处） | 已取回（正文 12760 字符） |
| Meta 开发者文档 | `calling_pricing_md` | Calling API Pricing | 正文 5.6、7.1、附表 B（7 处）、附表 C（1 处）、附表 E（5 处）、附表 F（1 处） | 已取回（正文 6194 字符） |
| Meta 开发者文档 | `calling_reference_md` | API and Webhook Reference | 正文 1.1、4.1、附表 B（6 处）、附表 C（1 处）、附表 D（3 处）、附表 E（11 处）、附表 F（3 处） | 已取回（正文 72977 字符） |
| Meta 开发者文档 | `calling_sandbox_md` | Set Up a Sandbox Account for Calling | 正文 8.2、附表 B（1 处）、附表 E（2 处） | 已取回（正文 9511 字符） |
| Meta 开发者文档 | `calling_sip_md` | SIP Configuration Guide - WhatsApp Business Calling | 正文 1.1、6.1、附表 E（5 处）、附表 F（3 处） | 已取回（正文 48869 字符） |
| Meta 开发者文档 | `calling_troubleshooting_md` | Troubleshoot WhatsApp Calling Errors - Reference Guide | 正文 1.1、4.6、附表 B（1 处）、附表 E（1 处） | 已取回（正文 20663 字符） |
| WhatsApp 帮助中心 | `faq_1061557453223601` | How to transfer a WhatsApp call to a second device | 正文 8.4、附表 C（7 处）、附表 E（1 处） | 已取回（正文 1810 字符） |
| WhatsApp 帮助中心 | `faq_1153602608602452` | How to make a 1:1 voice call | 正文 8.5、附表 C（4 处）、附表 E（1 处） | 已取回（正文 3506 字符） |
| WhatsApp 帮助中心 | `faq_1238612517047244` | How to silence unknown callers | 正文 3.4、8.4、附表 C（2 处）、附表 E（3 处） | 已取回（正文 1999 字符） |
| WhatsApp 帮助中心 | `faq_1862285217468140` | How to make a 1:1 video call | 正文 8.5、附表 B（1 处） | 已取回（正文 4523 字符） |
| WhatsApp 帮助中心 | `faq_2635108359972899` | How to protect your IP address on WhatsApp calls | 正文 8.4、附表 C（1 处）、附表 E（1 处） | 已取回（正文 1555 字符） |
| WhatsApp 帮助中心 | `faq_359678593009102` | Can't hear voice messages, status audio, call audio, or videos | 正文 8.6、附表 C（1 处） | 已取回（正文 2043 字符） |
| WhatsApp 帮助中心 | `faq_456694046556486` | How to create and share WhatsApp call links | 正文 8.3、附表 B（3 处）、附表 C（3 处）、附表 E（1 处） | 已取回（正文 3120 字符） |
| WhatsApp 帮助中心 | `faq_692484435227181` | How to use call waiting | 正文 8.4、附表 C（1 处） | 已取回（正文 1219 字符） |
| WhatsApp 帮助中心 | `faq_743219147158705` | About call history on WhatsApp | 正文 8.4、附表 C（1 处）、附表 E（1 处） | 已取回（正文 1168 字符） |
| WhatsApp 帮助中心 | `faq_829612741557179` | How to make a group voice call | 正文 8.4、附表 C（1 处） | 已取回（正文 4541 字符） |
| **合计** | **25** 个登记位 | 可引用 **25** 页／不可引用 0 页 | Meta 开发者文档 15、WhatsApp 帮助中心 10 | 不可引用页全篇不引用 |

#ROWS=25 COLS=5


# 附表 B 数字归档五档（40 行）

归档口径：**本表一行一个「数字＋它数的东西＋出处页与句序」**，数字、单位词与整句都由脚本从语料里实测取出——输入是原句池的 **990** 条整句（覆盖 **25** 个页面，池子已剔除含竖线的「正文黏表格」混合句），脚本在每句里找出所有独立数字，取该数字后面最多四个词，档位由这四个词里第一个能对上单位表的词决定；同页同数字同单位只留一行（共去重 **12** 行）。全表 **39** 行，分布在 **11** 个页面上，去重后的「数字＋单位」组合 **34** 组，单位词 **23** 种。五档分布：一、账号、号码与设备规模档位 7／三、时长与滚动窗口 17／二、验证要素、文本与文件长度规格 2／五、速率、次数与比率口径 8／四、生效日期与版本节点 5。

三条不成文规矩在这一表里最要紧：**同一个数字在两个页面各写一处，就各登记一行**，不合并成区间；**官方写成区间、多档或带限定词的照原样登记**（`up to`、`about`、`approximately`、`in some countries` 一类措辞原样保留），不取中值、不去掉约数；**档位与性质由脚本按关键词判定，性质判不出来就中止出表**，所以本表没有「其它」这一档。

本表也写清了自己漏掉了什么：池子里 **875** 句整句不含数字，脚本共扫出 **54** 个「数字后面四个词都对不上任何一档」的实例，全部连同它所在的整句写进 `_tmp/w6_a10_bdrop.tsv` 供复核，不进表——这些实例的共同点是紧跟数字的词不在任何一档的单位表里，本表不为它们硬造档位，**漏掉的判定权在读者手里，不在这张表里**。另有 **0** 个可引用页面在池子里一句都没有，本表因此整页缺席，它们在附表 A 里已逐页点名。

官方原句超过 118 字符按整词截断并以省略号结尾；池句自身带落单粗体或内联代码标记的，截在该标记之前并补省略号，共 **19** 行截断；截断只发生在表格里，进表的每一句都先用未截断的整句逐字回表到`_tmp/w6_a10s/` 的对应页面，失败 **0** 句（回表与建池共用同一套规范化，所以这里一旦非零就是池子与抓取件不同步，脚本直接中止不出表）。性质判定为 **8** 种：上限（官方写成 up to／maximum 一类） 7／下限或触发门槛 1／时长（官方只写数值与单位） 7／版本号或生效日期 3／申诉与复核时长 3／自动解除或自动生效的时限 8／规模计数 5／速率与吞吐 5。

| 档 | 数字 | 计数单位（官方原文） | 官方原句 | 性质 | 出处页 |
|---|---|---|---|---|---|
| 三、时长与滚动窗口 | **5** | minutes | 「The `url` returned in the webhook is valid for 5 minutes.」 | 时长（官方只写数值与单位） | `calling_call_recording_md`｜第 109 句 |
| 三、时长与滚动窗口 | **7** | days | 「Recordings remain available for download for **7 days** after the `call_recording_available` webhook is delivered.」 | 自动解除或自动生效的时限 | `calling_call_recording_md`｜第 114 句 |
| 三、时长与滚动窗口 | **7** | days | 「After you update call configuration, WhatsApp users may take up to 7 days to reflect those changes.」 | 上限（官方写成 up to／maximum 一类） | `calling_call_settings_md`｜第 10 句 |
| 三、时长与滚动窗口 | **30** | day | 「This skips the standard 30-day TTL applied to messaging media so that announcements remain available for the lifetime…」 | 时长（官方只写数值与单位） | `calling_call_settings_md`｜第 110 句 |
| 三、时长与滚动窗口 | **5** | minutes | 「The `url` returned in the webhook is valid for 5 minutes.」 | 时长（官方只写数值与单位） | `calling_call_transcription_md`｜第 138 句 |
| 三、时长与滚动窗口 | **7** | days | 「Transcripts remain available for download for **7 days** after the `call_transcription_available` webhook is…」 | 自动解除或自动生效的时限 | `calling_call_transcription_md`｜第 143 句 |
| 五、速率、次数与比率口径 | **1000** | calls | 「The only limit enforced for calling right now is the 1000 concurrent calls limit, but this may change in near future.」 | 上限（官方写成 up to／maximum 一类） | `calling_faq_md`｜第 52 句 |
| 一、账号、号码与设备规模档位 | **1** | app | 「If the phone number has more than 1 app subscribed to the calls field and Meta dispatches webhooks to app1 and app2…」 | 下限或触发门槛 | `calling_faq_md`｜第 122 句 |
| 五、速率、次数与比率口径 | **84** | per | 「G711 codec in comparison needs 64 kbps for codec + 20 kbps overhead = 84 kbps per call.」 | 规模计数 | `calling_faq_md`｜第 207 句 |
| 五、速率、次数与比率口径 | **15** | calls | 「Example: A 1mbps bandwidth can roughly handle 15 concurrent calls on opus (1000/64) vs. 12 concurrent calls on G711…」 | 规模计数 | `calling_faq_md`｜第 209 句 |
| 五、速率、次数与比率口径 | **12** | calls | 「Example: A 1mbps bandwidth can roughly handle 15 concurrent calls on opus (1000/64) vs. 12 concurrent calls on G711…」 | 规模计数 | `calling_faq_md`｜第 209 句 |
| 五、速率、次数与比率口径 | **1** | call | 「But roughly, a 1 min call on Opus consumes 3.75MB of data vs. the same on G711 takes 4.9MB of data.」 | 申诉与复核时长 | `calling_faq_md`｜第 212 句 |
| 二、验证要素、文本与文件长度规格 | **3.75** | mb | 「But roughly, a 1 min call on Opus consumes 3.75MB of data vs. the same on G711 takes 4.9MB of data.」 | 申诉与复核时长 | `calling_faq_md`｜第 212 句 |
| 二、验证要素、文本与文件长度规格 | **4.9** | mb | 「But roughly, a 1 min call on Opus consumes 3.75MB of data vs. the same on G711 takes 4.9MB of data.」 | 申诉与复核时长 | `calling_faq_md`｜第 212 句 |
| 三、时长与滚动窗口 | **7** | days | 「After call configuration is updated, WhatsApp users may take up to 7 days to reflect that configuration although most…」 | 上限（官方写成 up to／maximum 一类） | `calling_faq_md`｜第 230 句 |
| 一、账号、号码与设备规模档位 | **3** | users | 「One WhatsApp Business can have chats with any number of 3B+ WhatsApp users.」 | 规模计数 | `calling_faq_md`｜第 241 句 |
| 五、速率、次数与比率口径 | **2.24.14** | x | 「WhatsApp Client versions 2.24.14.x and later support the call permission requests and business initiated calling…」 | 规模计数 | `calling_faq_md`｜第 275 句 |
| 三、时长与滚动窗口 | **24** | hours | 「Limit on connected calls in 24 hours is a time window based running limit.」 | 上限（官方写成 up to／maximum 一类） | `calling_faq_md`｜第 298 句 |
| 三、时长与滚动窗口 | **33** | seconds | 「This problem manifests as a user initiated call getting connected, with bidirectional audio flow but user stops…」 | 时长（官方只写数值与单位） | `calling_faq_md`｜第 321 句 |
| 一、账号、号码与设备规模档位 | **2** | list | 「After this make sure your `200 OK` has following headers as the last 2 in the list of `Record-Route` headers This…」 | 自动解除或自动生效的时限 | `calling_integration_examples_md`｜第 94 句 |
| 三、时长与滚动窗口 | **56** | second | 「For example, a 56-second call (9.33 pulses) would be counted as 10 pulses.」 | 时长（官方只写数值与单位） | `calling_pricing_md`｜第 10 句 |
| 五、速率、次数与比率口径 | **250,000** | call | 「For calls that cross pricing tiers (for example from the 0 - 50,000 tier to the 50,001 - 250,000 tier), the entire…」 | 速率与吞吐 | `calling_pricing_md`｜第 11 句 |
| 四、生效日期与版本节点 | **1** | date | 「These rate cards represent the current rates and volume tiers for the WhatsApp Business Calling API, effective April…」 | 速率与吞吐 | `calling_pricing_md`｜第 14 句 |
| 一、账号、号码与设备规模档位 | **2026** | account | 「These rate cards represent the current rates and volume tiers for the WhatsApp Business Calling API, effective April…」 | 速率与吞吐 | `calling_pricing_md`｜第 14 句 |
| 四、生效日期与版本节点 | **2026** | year | 「Effective October 1, 2026, these 9 markets move out of their respective "Rest of" pricing region and become…」 | 速率与吞吐 | `calling_pricing_md`｜第 36 句 |
| 四、生效日期与版本节点 | **9** | date | 「Effective October 1, 2026, these 9 markets move out of their respective "Rest of" pricing region and become…」 | 速率与吞吐 | `calling_pricing_md`｜第 36 句 |
| 四、生效日期与版本节点 | **16** | date | 「Rates effective October 1, 2026 across 16 currencies are reflected below.」 | 版本号或生效日期 | `calling_pricing_md`｜第 40 句 |
| 三、时长与滚动窗口 | **30** | seconds | 「You have about 30 to 60 seconds after the [Call Connect Webhook](#call-connect-webhook) is sent to accept the phone…」 | 自动解除或自动生效的时限 | `calling_reference_md`｜第 53 句 |
| 三、时长与滚动窗口 | **60** | seconds | 「You have about 30 to 60 seconds after the [Call Connect Webhook](#call-connect-webhook) is sent to accept the phone…」 | 自动解除或自动生效的时限 | `calling_reference_md`｜第 53 句 |
| 一、账号、号码与设备规模档位 | **2** | permission | 「For example, your business can send only 2 permission requests in a 24-hour period.」 | 时长（官方只写数值与单位） | `calling_reference_md`｜第 170 句 |
| 三、时长与滚动窗口 | **24** | hour | 「For example, your business can send only 2 permission requests in a 24-hour period.」 | 时长（官方只写数值与单位） | `calling_reference_md`｜第 170 句 |
| 五、速率、次数与比率口径 | **5** | requests | 「A maximum of 5 requests in a one-second window can be made to the API.」 | 上限（官方写成 up to／maximum 一类） | `calling_reference_md`｜第 182 句 |
| 三、时长与滚动窗口 | **7** | days | 「Recordings remain available for download for 7 days after this webhook is delivered.」 | 自动解除或自动生效的时限 | `calling_reference_md`｜第 309 句 |
| 一、账号、号码与设备规模档位 | **5** | recipient | 「You can add up to 5 recipient phone numbers. **Note:** If you get the error…」 | 上限（官方写成 up to／maximum 一类） | `calling_sandbox_md`｜第 99 句 |
| 三、时长与滚动窗口 | **3** | second | 「After you send Meta the SDP, there is often a ~3 second delay before Meta receives your DTLS client hello packet and…」 | 自动解除或自动生效的时限 | `calling_troubleshooting_md`｜第 154 句 |
| 四、生效日期与版本节点 | **4.1** | version | 「Video calling is only available on Android phones running version 4.1 or newer.」 | 版本号或生效日期 | `faq_1862285217468140`｜第 73 句 |
| 三、时长与滚动窗口 | **30** | days | 「Links expire if they’ve been unused for 30 days.」 | 自动解除或自动生效的时限 | `faq_456694046556486`｜第 15 句 |
| 三、时长与滚动窗口 | **60** | minutes | 「Otherwise, calls continue until everyone, including guests, leave the call or until 60 minutes have passed.」 | 版本号或生效日期 | `faq_456694046556486`｜第 25 句 |
| 一、账号、号码与设备规模档位 | **128** | people | 「Alternatively, the call link creator can allow multiple people or tap Approve all to let everyone in. : The waiting…」 | 上限（官方写成 up to／maximum 一类） | `faq_456694046556486`｜第 34 句 |
| **合计** | **39** 行 | 单位词 **23** 种 | 去重后数字＋单位 **34** 组，覆盖 **11** 页 | 性质 **8** 种 | 第四档＝日期与版本节点 |

#ROWS=39 COLS=6


# 附表 C 官方用词与写下它的站点类别（71 行）

取格口径：脚本按第一、二列的 **56** 个词（组）（分属 8 个主题组：A　通话本体 6／B　发起与终结 7／C　权限与同意 6／D　状态与回调 6／E　媒体与信令 12／F　录音与转写 5／G　计费与限额 6／H　用户端入口 8）逐句扫过原句池的 **990** 条整句（覆盖 **25** 个页面），每命中一次就落进「词 × 站点类别」的格子；**一行一个非空格**，全表 **70** 行，各格命中句次相加是 **1458** 次，而去重后真正被任一词命中的句子是 **715** 句（占全池 72.2%）。同一句可以落进多格：本轮一句落进最多格子的是 `calling_faq_md` 池内序号 307，同时命中 7 个词（组）——「BYE」、「INVITE」、「SIP」、「call」、「initiate」、「terminate」、「user-initiated」；全池有 419 句同时落进两个以上的格子。所以句次合计 **1458** 大于去重句数 **715**，这两个数各自回答「这一格有多少句」与「整池有多少句被扫中」，不矛盾。

词表本身也是这一篇的一部分：它写在 `_tmp/w6_a10_verb.tsv` 里，脚本不内置任何选题词，换一篇只换词表、判定规则不变。**正则由脚本逐条编译，编译不过就中止出表**，不存在「写错的正则悄悄扫出零句然后当成官方没写过」。

代表句一列取该格在池子里最先出现的一句，取句规则写死在脚本的遍历顺序里，**不是按哪句更清楚来挑**。「格内有句写明数值」只回答这一格有没有句子自带天数、小时、位数、百分比或规模数——有则记「有」，没有就记「无」，不在这里把别的页面或别的站的数字补进来；跨站对撞属于附表 F。

词零命中的情况：56 个词（组）里 **1** 个在整池一句都没出现（`disclose`）。零命中不等于官方没有这一项内容，只说明本篇登记的这批页面没有用这个词写过。

本表的英文原句全部逐字回表：990 条整句每一条都在 `_tmp/w6_a10s/` 的对应页面正文里按规范化后的子串核到，失败 **0** 句（判定与建池用同一套实体还原与空白折叠，见 `_tmp/wa6_lib.pl` 的 ck）。官方原句超过 130 字符按整词截断并以省略号结尾，共 **25** 行截断；截断只发生在表格里，回表核验用的是未截断的整句。各站点命中句次合计：Meta 开发者文档 1098／WhatsApp 帮助中心 360／Meta 商业帮助中心 0／法律与政策页 0。

| 主题组 | 官方词（组） | 写下它的站点类别 | 命中句次 | 命中页数 | 代表句（该格池内第一句） | 格内有句写明数值 | 出处页｜句序 |
|---|---|---|---|---|---|---|---|
| A　通话本体 | call | Meta 开发者文档 | **239** | 15 | 「You should clearly show that your app can enable and disable Calling API features by displaying whether the Call Button icon is…」 | 有 | `calling_app_review_guidelines_md`｜第 9 句 |
| A　通话本体 | call | WhatsApp 帮助中心 | **189** | 10 | 「Android iOS Web Windows Mac More Android iOS Web Windows Mac You can move an active WhatsApp call from one device to another…」 | 有 | `faq_1061557453223601`｜第 2 句 |
| A　通话本体 | voice call | WhatsApp 帮助中心 | **25** | 4 | 「There isn’t a limit on transfers so you can move the call back and forth between devices. : Call transfer is currently only…」 | 无 | `faq_1061557453223601`｜第 4 句 |
| A　通话本体 | video call | Meta 开发者文档 | **1** | 1 | 「Session Description Protocol (SDP) is a text-based format that describes multimedia session characteristics, such as voice and…」 | 无 | `calling_reference_md`｜第 369 句 |
| A　通话本体 | video call | WhatsApp 帮助中心 | **29** | 5 | 「There isn’t a limit on transfers so you can move the call back and forth between devices. : Call transfer is currently only…」 | 无 | `faq_1061557453223601`｜第 4 句 |
| A　通话本体 | VoIP | Meta 开发者文档 | **7** | 4 | 「WebRTC relies on SRTP/SRTCP for actual media which is the VoIP media standard.」 | 无 | `calling_faq_md`｜第 66 句 |
| A　通话本体 | PSTN | Meta 开发者文档 | **3** | 2 | 「You can enable G.711 (PCMA/PCMU) codecs for interoperability with legacy telephony systems or PSTN gateways.」 | 无 | `calling_call_settings_md`｜第 75 句 |
| A　通话本体 | telephony | Meta 开发者文档 | **1** | 1 | 「You can enable G.711 (PCMA/PCMU) codecs for interoperability with legacy telephony systems or PSTN gateways.」 | 无 | `calling_call_settings_md`｜第 75 句 |
| B　发起与终结 | business-initiated | Meta 开发者文档 | **15** | 11 | 「Before you get started with business-initiated calling, ensure that:」 | 无 | `calling_business_initiated_calls_md`｜第 10 句 |
| B　发起与终结 | user-initiated | Meta 开发者文档 | **17** | 8 | 「The Calling API can record the audio of business-initiated calls (BIC) and user-initiated calls (UIC) you make through the…」 | 有 | `calling_call_recording_md`｜第 4 句 |
| B　发起与终结 | initiate | Meta 开发者文档 | **49** | 12 | 「Before you get started with business-initiated calling, ensure that:」 | 有 | `calling_business_initiated_calls_md`｜第 10 句 |
| B　发起与终结 | accept | Meta 开发者文档 | **25** | 9 | 「_Note: The `ACCEPTED` call status webhook arrives after the call is established.」 | 有 | `calling_business_initiated_calls_md`｜第 7 句 |
| B　发起与终结 | accept | WhatsApp 帮助中心 | **5** | 4 | 「If the person isn’t saved as a contact and you haven’t interacted with them, they must accept the request to start seeing your…」 | 无 | `faq_1153602608602452`｜第 53 句 |
| B　发起与终结 | reject | Meta 开发者文档 | **7** | 6 | 「You then receive an appropriate status webhook, indicating that the call is `RINGING`, `ACCEPTED`, or `REJECTED`:」 | 无 | `calling_business_initiated_calls_md`｜第 34 句 |
| B　发起与终结 | terminate | Meta 开发者文档 | **23** | 10 | 「Either you or the WhatsApp user can terminate the call at any time.」 | 无 | `calling_business_initiated_calls_md`｜第 36 句 |
| B　发起与终结 | ring | Meta 开发者文档 | **6** | 3 | 「You then receive an appropriate status webhook, indicating that the call is `RINGING`, `ACCEPTED`, or `REJECTED`:」 | 无 | `calling_business_initiated_calls_md`｜第 34 句 |
| B　发起与终结 | ring | WhatsApp 帮助中心 | **3** | 3 | 「You can see silenced calls from unknown callers in the Calls tab and your notifications, but your phone won’t ring.」 | 无 | `faq_1238612517047244`｜第 20 句 |
| C　权限与同意 | permission | Meta 开发者文档 | **23** | 8 | 「Use this guide as complementary to the permissions reference page, but treat the permissions reference page as the official…」 | 有 | `calling_app_review_guidelines_md`｜第 5 句 |
| C　权限与同意 | permission | WhatsApp 帮助中心 | **2** | 2 | 「About last seen and online About WhatsApp permissions How to use app lock About sharing your device's location Exercising your…」 | 无 | `faq_1238612517047244`｜第 2 句 |
| C　权限与同意 | consent | Meta 开发者文档 | **2** | 2 | 「A participant who does not consent can decline by terminating the call before or during the announcement.」 | 无 | `calling_call_recording_md`｜第 35 句 |
| C　权限与同意 | opt in / opt-in | Meta 开发者文档 | **2** | 2 | 「Recording is opt-in on a per-call basis — you decide at the time you initiate or accept each call whether it should be recorded.」 | 无 | `calling_call_recording_md`｜第 11 句 |
| C　权限与同意 | announcement | Meta 开发者文档 | **25** | 4 | 「When you opt a call into recording, both participants hear a short legally required announcement before the recording begins.」 | 无 | `calling_call_recording_md`｜第 5 句 |
| C　权限与同意 | purpose | Meta 开发者文档 | **19** | 4 | 「The announcement is generated from the `purpose` string you provide and the `announcement_language` you select, for example:」 | 无 | `calling_call_recording_md`｜第 32 句 |
| D　状态与回调 | webhook | Meta 开发者文档 | **98** | 13 | 「The calling webhook that the WhatsApp platform delivers to your app.」 | 有 | `calling_app_review_guidelines_md`｜第 28 句 |
| D　状态与回调 | status | Meta 开发者文档 | **26** | 8 | 「_Note: The `ACCEPTED` call status webhook arrives after the call is established.」 | 无 | `calling_business_initiated_calls_md`｜第 7 句 |
| D　状态与回调 | status | WhatsApp 帮助中心 | **1** | 1 | 「If your device is on vibrate only or silent, you will also have no sound when playing a video status with audio or during a call.」 | 无 | `faq_359678593009102`｜第 5 句 |
| D　状态与回调 | connect | Meta 开发者文档 | **42** | 7 | 「After you successfully initiate a new call, you receive a Call Connect webhook response containing an `SDP Answer` from Cloud API.」 | 有 | `calling_business_initiated_calls_md`｜第 32 句 |
| D　状态与回调 | connect | WhatsApp 帮助中心 | **12** | 8 | 「When you use Call transfer , the call connects on the new device and ends on the previous device.」 | 无 | `faq_1061557453223601`｜第 3 句 |
| D　状态与回调 | callback | Meta 开发者文档 | **6** | 3 | 「The WhatsApp client app shows WhatsApp users an option to chat with the business, or request a callback, if…」 | 无 | `calling_call_settings_md`｜第 62 句 |
| D　状态与回调 | event | Meta 开发者文档 | **19** | 8 | 「The Cloud API sends it for call event auditing._」 | 无 | `calling_business_initiated_calls_md`｜第 8 句 |
| D　状态与回调 | payload | Meta 开发者文档 | **8** | 4 | 「Contains three values: `display_text`, `ttl_minutes`, and `payload`.」 | 无 | `calling_call_button_messages_deep_links_md`｜第 30 句 |
| E　媒体与信令 | SDP | Meta 开发者文档 | **35** | 6 | 「After you successfully initiate a new call, you receive a Call Connect webhook response containing an `SDP Answer` from Cloud API.」 | 有 | `calling_business_initiated_calls_md`｜第 32 句 |
| E　媒体与信令 | WebRTC | Meta 开发者文档 | **22** | 6 | 「Your business then applies the `SDP Answer` from this webhook to your WebRTC stack to initiate the media connection.」 | 无 | `calling_business_initiated_calls_md`｜第 33 句 |
| E　媒体与信令 | SRTP | Meta 开发者文档 | **4** | 3 | 「WebRTC relies on SRTP/SRTCP for actual media which is the VoIP media standard.」 | 无 | `calling_faq_md`｜第 66 句 |
| E　媒体与信令 | SDES | Meta 开发者文档 | **10** | 4 | 「The WA to Asterisk leg uses SDES for media encryption key exchange and Opus for audio codec.」 | 无 | `calling_integration_examples_md`｜第 18 句 |
| E　媒体与信令 | ICE | Meta 开发者文档 | **12** | 4 | 「As such it is not mandatory to use STUN/TURN to determine the ICE candidates.」 | 无 | `calling_faq_md`｜第 61 句 |
| E　媒体与信令 | DTLS | Meta 开发者文档 | **9** | 5 | 「This can be done even before calling the accept API but the ICE process will only converge after Meta processes the SDP answer,…」 | 有 | `calling_faq_md`｜第 189 句 |
| E　媒体与信令 | codec | Meta 开发者文档 | **16** | 5 | 「Opus is the default audio codec for all WhatsApp calls.」 | 无 | `calling_call_settings_md`｜第 74 句 |
| E　媒体与信令 | Opus | Meta 开发者文档 | **15** | 4 | 「Opus is the default audio codec for all WhatsApp calls.」 | 无 | `calling_call_settings_md`｜第 74 句 |
| E　媒体与信令 | G.711 | Meta 开发者文档 | **12** | 3 | 「You can enable G.711 (PCMA/PCMU) codecs for interoperability with legacy telephony systems or PSTN gateways.」 | 无 | `calling_call_settings_md`｜第 75 句 |
| E　媒体与信令 | SIP | Meta 开发者文档 | **117** | 7 | 「Optionally, you can include SIP user credentials in your response body by adding the SIP credentials query parameter in the POST…」 | 有 | `calling_call_settings_md`｜第 142 句 |
| E　媒体与信令 | INVITE | Meta 开发者文档 | **20** | 4 | 「If you are not getting SIP INVITE following a user initiated call or a SIP BYE following a user initiated call termination,…」 | 无 | `calling_faq_md`｜第 307 句 |
| E　媒体与信令 | INVITE | WhatsApp 帮助中心 | **2** | 2 | 「You’ll have the option to invite the user to join WhatsApp.」 | 无 | `faq_1153602608602452`｜第 22 句 |
| E　媒体与信令 | BYE | Meta 开发者文档 | **7** | 4 | 「If you are not getting SIP INVITE following a user initiated call or a SIP BYE following a user initiated call termination,…」 | 无 | `calling_faq_md`｜第 307 句 |
| F　录音与转写 | record | Meta 开发者文档 | **40** | 7 | 「The Calling API can record the audio of business-initiated calls (BIC) and user-initiated calls (UIC) you make through the…」 | 无 | `calling_call_recording_md`｜第 4 句 |
| F　录音与转写 | record | WhatsApp 帮助中心 | **4** | 3 | 「Leave a voice message If they don’t pick up, you can leave a voicemail by tapping Record voice message .」 | 无 | `faq_1153602608602452`｜第 34 句 |
| F　录音与转写 | transcript | Meta 开发者文档 | **44** | 4 | 「In some cases call recording and transcript can be cut short.」 | 有 | `calling_call_recording_md`｜第 9 句 |
| F　录音与转写 | transcript | WhatsApp 帮助中心 | **1** | 1 | 「Voice message transcripts aren’t available during group voice calls.」 | 无 | `faq_829612741557179`｜第 77 句 |
| F　录音与转写 | media ID | Meta 开发者文档 | **6** | 3 | 「After the call ends, you receive a webhook with a media ID you can use to download the finished recording.…」 | 无 | `calling_call_recording_md`｜第 6 句 |
| F　录音与转写 | download | Meta 开发者文档 | **12** | 3 | 「After the call ends, you receive a webhook with a media ID you can use to download the finished recording.…」 | 有 | `calling_call_recording_md`｜第 6 句 |
| G　计费与限额 | pulse | Meta 开发者文档 | **1** | 1 | 「For example, a 56-second call (9.33 pulses) would be counted as 10 pulses.」 | 无 | `calling_pricing_md`｜第 10 句 |
| G　计费与限额 | rate | Meta 开发者文档 | **14** | 4 | 「Separate pricing for recording is planned for a future date, and this page will be updated with the rates and advance notice…」 | 无 | `calling_call_recording_md`｜第 7 句 |
| G　计费与限额 | rate | WhatsApp 帮助中心 | **1** | 1 | 「Data rates will apply through your mobile provider.」 | 无 | `faq_743219147158705`｜第 17 句 |
| G　计费与限额 | tier | Meta 开发者文档 | **4** | 2 | 「Example: a lead that's qualified or a customer who is in premium tier Providing this type of feature would require Meta to store…」 | 无 | `calling_faq_md`｜第 248 句 |
| G　计费与限额 | concurrent | Meta 开发者文档 | **3** | 1 | 「The only limit enforced for calling right now is the 1000 concurrent calls limit, but this may change in near future.」 | 无 | `calling_faq_md`｜第 52 句 |
| G　计费与限额 | limit | Meta 开发者文档 | **11** | 4 | 「When the rate limit is exceeded, the caller (the WhatsApp user) will get a generic message saying call cannot be placed.」 | 有 | `calling_faq_md`｜第 12 句 |
| G　计费与限额 | limit | WhatsApp 帮助中心 | **2** | 2 | 「There isn’t a limit on transfers so you can move the call back and forth between devices. : Call transfer is currently only…」 | 无 | `faq_1061557453223601`｜第 4 句 |
| G　计费与限额 | customer service window | Meta 开发者文档 | **2** | 2 | 「Send it as a free form message during an open customer service window, or use a template message.」 | 无 | `calling_business_initiated_calls_md`｜第 19 句 |
| H　用户端入口 | call link | WhatsApp 帮助中心 | **18** | 3 | 「There isn’t a limit on transfers so you can move the call back and forth between devices. : Call transfer is currently only…」 | 有 | `faq_1061557453223601`｜第 4 句 |
| H　用户端入口 | waiting room | WhatsApp 帮助中心 | **9** | 2 | 「To enable a waiting room for people to join the call, toggle Require approval to join .」 | 有 | `faq_456694046556486`｜第 10 句 |
| H　用户端入口 | guest | WhatsApp 帮助中心 | **13** | 2 | 「People without a WhatsApp account can join a call link as a Guest from their web browser (desktop).」 | 有 | `faq_456694046556486`｜第 19 句 |
| H　用户端入口 | call waiting | WhatsApp 帮助中心 | **2** | 2 | 「Manage the call waiting room If the call link creator turned on Require approval to join , anyone joining the call will enter a…」 | 无 | `faq_456694046556486`｜第 26 句 |
| H　用户端入口 | call history | WhatsApp 帮助中心 | **12** | 3 | 「How to edit favorites from the Calls tab About call history on WhatsApp About data roaming charges Troubleshooting Communities…」 | 无 | `faq_692484435227181`｜第 2 句 |
| H　用户端入口 | silence | Meta 开发者文档 | **3** | 2 | 「Business initiated calls bypass 'Silence Unknown Callers' settings since the call can only happen after explicit permission…」 | 无 | `calling_faq_md`｜第 290 句 |
| H　用户端入口 | silence | WhatsApp 帮助中心 | **12** | 4 | 「You can change your settings to silence calls from unknown callers .」 | 无 | `faq_1153602608602452`｜第 49 句 |
| H　用户端入口 | IP address | Meta 开发者文档 | **5** | 2 | 「Run `host {domain-name}` to verify that the IP address points to the Asterisk server.」 | 无 | `calling_integration_examples_md`｜第 85 句 |
| H　用户端入口 | IP address | WhatsApp 帮助中心 | **4** | 1 | 「Safety Security Accounts and Account Bans Payments WhatsApp for Business How to protect your IP address on WhatsApp calls Copy…」 | 无 | `faq_2635108359972899`｜第 8 句 |
| H　用户端入口 | transfer | Meta 开发者文档 | **3** | 1 | 「In essence, a customer is speaking with Agent A and needs to be transferred to Agent B?」 | 无 | `calling_faq_md`｜第 214 句 |
| H　用户端入口 | transfer | WhatsApp 帮助中心 | **14** | 5 | 「When you use Call transfer , the call connects on the new device and ends on the previous device.」 | 无 | `faq_1061557453223601`｜第 3 句 |
| F　录音与转写 | language | Meta 开发者文档 | **8** | 2 | 「You do not specify a transcription language in the request.」 | 无 | `calling_call_transcription_md`｜第 91 句 |
| **合计** | **56** 个词（组）× **2** 类站点 | 非空格 **70** 个 | 句次 **1458** | 一句可落多格 | 代表句按池内顺序取第一句 | 零命中词 **1** 个 | 回表失败 **0** 句 |

#ROWS=70 COLS=8


# 附表 D 官方写明的取值域全表（6 行）

取组口径：脚本扫过 **25** 个已登记页面的每一行原文，配置了 10 个取值域标记，本轮真正在这些页面里出现过的有 **3** 个（`Values can be` 7 行／`Possible values` 7 行／`one of the following` 1 行，合计 **15** 行），另有 **7** 个（Supported values are／Can be one of／must be one of／The following values are supported／Valid values are／Options are／Enum Values）在扫过的每一行原文里零命中——这几串字符只是脚本配置表里的名字，本轮官方页面没有一处用过这种写法，所以本篇不给它们加反引号、也不把它们当作任何官方句子的引文。取到值来的页面 **2** 个（站点类别：Meta 开发者文档）。按标记所在行的形态分档取值：自动·参数表说明格 5。全表 **5** 组、共 **12** 个取值、去重后的取值名 **7** 个。其中 **0** 组的取值是不带反引号的纯文本子项——反引号是接口文档的排版，帮助中心同样用「one of the following」加子项写成正式取值域，这一档整项照收、只丢超过 80 字符的说明句；同页同字段同取值的重复写法（帮助中心按平台页签各写一遍）合并为一行，共合并 **0** 处。取值本身带空格的共 **0** 个（如 `LEGAL NAME NOT FOUND IN DOCUMENTS`、`No longer needed`），这一批只能按「反引号紧跟破折号说明」的位置认出，脚本对这种写法额外兜一遍：同一格里见到两个以上这种位置才收，并且只在这一档放开取值不能带空格的限制——接口文档的枚举里确有带空格的大写取值，卡成单词会漏掉整组。

为什么这一表不走原句池：官方把取值域写在接口参数表的说明格里、缩进的子项列表里、整句内联的清单里，这些行要么带竖线、要么是列表行，本来就被池子的入池条件挡在外面。所以 D 的核验独立于 C：每一组把它自己那一行原文、缩进子项式还把每个取值所在的那一行原文，逐字回表到 `_tmp/w6_a10s/` 的对应页面，失败 **0** 行。

落空登记：扫到标记但取不到两个以上取值的共 **10** 处，原因由脚本按同一行的证据判，不由人挑：官方只写了一个取值 7／官方给的"取值"位上是带变量或带说明的整句，不是标识符 2／标记句下面紧跟的不是子项列表（官方把选择写在正文段落里，没写成清单） 1。**扫到标记不等于官方写明了取值域**，这几处不算进表，但算进这一句，让读者知道漏了多少、为什么漏。这 **10** 处分布在 **3** 个页面，每一处的页面标识、原文行号、判定档位、命中的标记、落空判据与该行未截断的原文全部逐条写进 `_tmp/w6_a10_dmiss.tsv`（与上面的次数由脚本断言相等，不是"约"），可逐条回原文核对。

单组取值上限设为 **24** 个：实测最多的一组有 **3** 个取值，没有一组触及上限，所以本表没有因上限而丢过取值。

取值列表的截断按整取值进行（顿号相连的列表没有空格，按词截会把一整组截到只剩一项）：单元格 **400** 字符内装得下的原样列出，装不下的整项去掉并在行内写明「另有 N 个取值未列出」。本轮实测 **0** 组触及这一上限、被去掉的取值 **0** 个，也就是说表里列出的取值就是脚本收到的全部 **12** 个，没有一组需要读者回原文补齐。

| 组别 | 页面标识 | 行号 | 字段／属性 | 官方标记 | 取值个数 | 官方写明的取值 |
|---|---|---|---|---|---|---|
| 自动·参数表说明格 | `calling_business_initiated_calls_md` | 428 | `status` | Possible values | **3** | `RINGING`、`ACCEPTED`、`REJECTED` |
| 自动·参数表说明格 | `calling_business_initiated_calls_md` | 504 | `status` | Possible values | **2** | `COMPLETED`、`FAILED` |
| 自动·参数表说明格 | `calling_reference_md` | 1031 | `response_source` | Possible values | **2** | `user_action`、`automatic` |
| 自动·参数表说明格 | `calling_reference_md` | 774 | `status` | Possible values | **3** | `RINGING`、`ACCEPTED`、`REJECTED` |
| 自动·参数表说明格 | `calling_reference_md` | 849 | `status` | Possible values | **2** | `COMPLETED`、`FAILED` |
| **合计** | **2** 页 | 行号为原文行序 | 字段名取自官方写法 | 标记 1 种 | 取值 **12** 个 | 去重取值名 **7** 个 |

#ROWS=5 COLS=7


# 附表 E 同一组维度在本轴各主体上的逐列对照（34 行）

取格口径：本表 **33** 个对照维度 × **7** 列 = **231** 格，其中 **66** 格取到了官方原句、**165** 格标作「官方未按这一列写该项」。每一格的取法都是「页面标识＋一条正则」交给脚本在原句池里取句，在该列的页面范围内命中 0 句或命中多句都会让生成脚本中止，所以表里没有一格是人工挑出来的。原句池共 **990** 句（已由附表 B 逐字回表），本表用去重后的页面 **18** 个；同一句在多个维度或多列复用的共 **1** 次，复用时页标识与句序不变。

列的定义写在 `_tmp/w6_a10_ecols.tsv` 里，每列要么按站点类别取页（用户帮助中心＝site WhatsApp 帮助中心），要么按人工点名的页清单取页（总纲页＝1 页／发起、设置与审批页＝5 页／录音与转写页＝2 页／参考与排错页＝2 页／SIP 页＝1 页／定价与问答页＝2 页）。空格的含义只有一个：官方在这批登记页面里没按这一列写这一项，**不代表该主体没有这项规则**。

官方原句超过 112 字符按整词截断并以省略号结尾；池句自身带落单粗体或内联代码标记的，截在该标记之前并补省略号，共 **28** 格截断；截断只发生在显示层，取句与逐字回表用的是未截断的整句。各列取到句子的格数：总纲页 5／发起、设置与审批页 13／录音与转写页 7／参考与排错页 12／SIP 页 5／定价与问答页 16／用户帮助中心 8。

| 编号 | 对照维度 | 总纲页 | 发起、设置与审批页 | 录音与转写页 | 参考与排错页 | SIP 页 | 定价与问答页 | 用户帮助中心 |
|---|---|---|---|---|---|---|---|---|
| E1   | 一句话定位 | 「The WhatsApp Business Calling API enables you to initiate and receive calls with WhatsApp users using Voice…」（`calling_md`｜第 4 句） | 「The Calling API lets your business call WhatsApp users.」（`calling_business_initiated_calls_md`｜第 4 句） | 「The Calling API can record the audio of business-initiated calls (BIC) and user-initiated calls (UIC) you make…」（`calling_call_recording_md`｜第 4 句） | 「Use this endpoint to connect to a call by providing a call agent's SDP.」（`calling_reference_md`｜第 52 句） | 「WhatsApp Business Calling API supports use of SIP as the signaling protocol instead of our Graph API endpoints.」（`calling_sip_md`｜第 7 句） | 「WhatsApp Calling API is the first public voice API by Meta.」（`calling_faq_md`｜第 22 句） | 「WhatsApp calling uses your phone's internet connection to make calls, not your phone plan.」（`faq_743219147158705`｜第 15 句） |
| E2   | 默认是否开启 | 「Calling is disabled by default on test numbers.」（`calling_md`｜第 91 句） | 「Calling is not enabled by default on a business phone number.」（`calling_call_settings_md`｜第 3 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 「By default, **calling-related webhooks are not sent**, but you can enable webhook delivery for SIP calls to…」（`calling_sip_md`｜第 4 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E3   | 谁发起 | 「The WhatsApp Business Calling API enables you to initiate and receive calls with WhatsApp users using Voice…」（`calling_md`｜第 4 句） | 「Use this endpoint to initiate a call to a WhatsApp user by providing a phone number and a WebRTC call offer.」（`calling_business_initiated_calls_md`｜第 42 句） | 官方未按这一列写该项 | 「WhatsApp sends a webhook notification in near real-time when a call initiated by your business is ready to be…」（`calling_reference_md`｜第 187 句） | 「These webhooks apply to both business-initiated and user-initiated SIP calls.」（`calling_sip_md`｜第 170 句） | 官方未按这一列写该项 | 「Safety Security Accounts and Account Bans Payments WhatsApp for Business How to silence unknown callers Copy…」（`faq_1238612517047244`｜第 7 句） |
| E4   | 呼叫权限 | 官方未按这一列写该项 | 「Calling a WhatsApp user requires explicit permission from the user.」（`calling_call_settings_md`｜第 47 句） | 官方未按这一列写该项 | 「Use this endpoint to get the call permission state for a business phone number with a single WhatsApp user.」（`calling_reference_md`｜第 142 句） | 官方未按这一列写该项 | 「Business initiated calls bypass 'Silence Unknown Callers' settings since the call can only happen after…」（`calling_faq_md`｜第 290 句） | 「From there you can decide if you should answer the call, block the phone number or silence calls from unknown…」（`faq_1238612517047244`｜第 11 句） |
| E5   | 权限请求的次数与重置 | 官方未按这一列写该项 | 「The user may change their permission selection at any time.」（`calling_call_settings_md`｜第 50 句） | 官方未按这一列写该项 | 「For example, your business can send only 2 permission requests in a 24-hour period.」（`calling_reference_md`｜第 170 句） | 官方未按这一列写该项 | 「A connected call will reset the call permission limits.」（`calling_faq_md`｜第 288 句） | 官方未按这一列写该项 |
| E6   | 呼叫状态词 | 官方未按这一列写该项 | 「You then receive an appropriate status webhook, indicating that the call is `RINGING`, `ACCEPTED`, or…」（`calling_business_initiated_calls_md`｜第 34 句） | 官方未按这一列写该项 | 「Possible values: `COMPLETED`: The call completed without unexpected errors.」（`calling_reference_md`｜第 266 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E7   | 接听时限 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「After the call ends and post-processing finishes (typically under one minute), the Calling API sends a…」（`calling_call_recording_md`｜第 91 句） | 「You have about 30 to 60 seconds after the [Call Connect Webhook](#call-connect-webhook) is sent to accept the…」（`calling_reference_md`｜第 53 句） | 官方未按这一列写该项 | 「The Meta HTTPS request to the webhook server timed out after ~20s.」（`calling_faq_md`｜第 118 句） | 官方未按这一列写该项 |
| E8   | 怎样结束 | 官方未按这一列写该项 | 「Either you or the WhatsApp user can terminate the call at any time.」（`calling_business_initiated_calls_md`｜第 36 句） | 官方未按这一列写该项 | 「Use this endpoint to terminate an active call.」（`calling_reference_md`｜第 112 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E9   | 录音是否逐通选择 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「You can enable either one on its own, both together, or neither.」（`calling_call_recording_md`｜第 13 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E10  | 录音与转写的告知 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「When you opt a call into recording, both participants hear a short legally required announcement before the…」（`calling_call_recording_md`｜第 5 句） | 「`purpose` — (_String_) **Required when `status` is `ENABLED`.** The purpose of the recording, spoken to both…」（`calling_reference_md`｜第 92 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E11  | 目的字段 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「The `purpose` field is mandatory whenever `status` is `ENABLED`.」（`calling_call_recording_md`｜第 36 句） | 「`purpose` — (_String_) **Required when `status` is `ENABLED`.** The purpose of the transcription, spoken to…」（`calling_reference_md`｜第 74 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E12  | 产物与下载 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「After the call ends, you receive a webhook with a media ID you can use to download the finished recording.…」（`calling_call_recording_md`｜第 6 句） | 「It delivers a `call_transcription_available` event under the `calls` webhook field, with a media ID you can use…」（`calling_reference_md`｜第 313 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E13  | 保留期 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Recordings remain available for download for **7 days** after the `call_recording_available` webhook is…」（`calling_call_recording_md`｜第 114 句） | 「Transcripts remain available for download for 7 days after this webhook is delivered.」（`calling_reference_md`｜第 342 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E14  | 媒体加密 | 「You can use SDES instead of ICE+DTLS with Graph API + Webhook signaling」（`calling_md`｜第 53 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「The Secure Real-time Transport Protocol (SRTP) key exchange is a cryptographic protocol used to securely…」（`calling_sip_md`｜第 228 句） | 「WebRTC relies on SRTP/SRTCP for actual media which is the VoIP media standard.」（`calling_faq_md`｜第 66 句） | 「Calls relayed through WhatsApp servers are end-to-end encrypted, like all calls on WhatsApp.」（`faq_2635108359972899`｜第 18 句） |
| E15  | 编解码 | 「Additional audio codecs supported: `PCMA`, `PCMU`」（`calling_md`｜第 55 句） | 「Opus is the default audio codec for all WhatsApp calls.」（`calling_call_settings_md`｜第 74 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「WhatsApp mobile apps only support opus natively, so Meta media infra transcodes opus to other codecs if needed.」（`calling_faq_md`｜第 173 句） | 官方未按这一列写该项 |
| E16  | 计费单位 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「For example, a 56-second call (9.33 pulses) would be counted as 10 pulses.」（`calling_pricing_md`｜第 10 句） | 官方未按这一列写该项 |
| E17  | 跨境档位 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「For calls that cross pricing tiers (for example from the 0 - 50,000 tier to the 50,001 - 250,000 tier), the…」（`calling_pricing_md`｜第 11 句） | 官方未按这一列写该项 |
| E18  | 费率生效日 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「These rate cards represent the current rates and volume tiers for the WhatsApp Business Calling API, effective…」（`calling_pricing_md`｜第 14 句） | 官方未按这一列写该项 |
| E19  | 市场拆分 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Effective October 1, 2026, these 9 markets move out of their respective "Rest of" pricing region and become…」（`calling_pricing_md`｜第 36 句） | 官方未按这一列写该项 |
| E20  | 并发上限 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「The only limit enforced for calling right now is the 1000 concurrent calls limit, but this may change in near…」（`calling_faq_md`｜第 52 句） | 官方未按这一列写该项 |
| E21  | 二十四小时窗口 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Limit on connected calls in 24 hours is a time window based running limit.」（`calling_faq_md`｜第 298 句） | 官方未按这一列写该项 |
| E22  | 与消息限额的关系 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Calling API usage does not count towards messaging rate limits at the moment.」（`calling_faq_md`｜第 51 句） | 官方未按这一列写该项 |
| E23  | 客服窗口 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「With the introduction of the Calling API, the customer service window now also starts or refreshes for calls:」（`calling_pricing_md`｜第 97 句） | 官方未按这一列写该项 |
| E24  | 客户端入口 | 官方未按这一列写该项 | 「WhatsApp client apps render the call button icon in both the business chat and business chat profile.」（`calling_call_settings_md`｜第 36 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「After call configuration is updated, WhatsApp users may take up to 7 days to reflect that configuration…」（`calling_faq_md`｜第 230 句） | 「You can see silenced calls from unknown callers in the Calls tab and your notifications, but your phone won’t…」（`faq_1238612517047244`｜第 20 句） |
| E25  | 测试面 | 官方未按这一列写该项 | 「A WhatsApp sandbox account is a mock Messaging account that you can use to test your Calling API integration.」（`calling_sandbox_md`｜第 6 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E26  | 沙盒接收名单 | 官方未按这一列写该项 | 「You can add up to 5 recipient phone numbers. **Note:** If you get the error…」（`calling_sandbox_md`｜第 99 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E27  | 呼叫按钮消息 | 官方未按这一列写该项 | 「When a WhatsApp user clicks the call button, the click initiates a WhatsApp call to the business number that…」（`calling_call_button_messages_deep_links_md`｜第 9 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E28  | 审批要展示什么 | 官方未按这一列写该项 | 「You should clearly show that your app can enable and disable Calling API features by displaying whether the…」（`calling_app_review_guidelines_md`｜第 9 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E29  | AP 数量 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Each phone number can have only one SIP server configured.」（`calling_sip_md`｜第 120 句） | 「In general, using a single app is recommended.」（`calling_faq_md`｜第 145 句） | 官方未按这一列写该项 |
| E30  | 访客与等候室 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「People without a WhatsApp account can join a call link as a Guest from their web browser (desktop).」（`faq_456694046556486`｜第 19 句） |
| E31  | 跨设备转移 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「There isn’t a limit on transfers so you can move the call back and forth between devices. : Call transfer is…」（`faq_1061557453223601`｜第 4 句） |
| E32  | 紧急号码 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「You can’t access emergency service numbers through WhatsApp such as 911 in the United States.」（`faq_1153602608602452`｜第 55 句） |
| E33  | 错误形状与日志 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「When you receive an error, in the majority of cases the error shape will look like this:」（`calling_troubleshooting_md`｜第 4 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| **合计** | **33** 个维度 × 7 列 = **231** 格 | 有句 **66** 格 | 空格 **165** 格 | 去重引用页 **18** | 跨格复用句 **1** 次 | 截断 **28** 格 | 总纲页 5 | 发起、设置与审批页 13 |

#ROWS=33 COLS=9


# 附表 F 口径冲突与官方未写明处（13 行）

本表收录两类东西：官方多处写法不一致、需要读者知道本篇按哪一说走的**冲突**，以及官方在这批登记页面里根本没写、本篇不替它补的**空白**。全表 **12** 个议题：**10** 个两侧都取到了原句、**1** 个只有一侧有原句（另一侧官方没写）、**1** 个两侧都没有原句可引（共 **1** 行）。

两侧皆空的行要说清它空在哪：本轴登记 **25** 页，其中正文未取回 **0** 页——这 **0** 页全篇不引用，已引用 **25** 页（Meta 开发者文档 15／WhatsApp 帮助中心 10），所以两侧皆空不是因为抓不到，而是因为**官方在可引用的这批页面里没按这一项写过一句话**；这一类议题一律登记为空白，正文不替官方补结论。

取句方式与附表 E 同一把刀：每个说法登记「页面标识＋一条正则」，脚本在原句池里取句，命中 0 句或多句都会让生成脚本中止；取到的每一句再逐字回表到 `_tmp/w6_a10s/` 的对应页面。本表引用页面去重 **6** 个，同一句在多个议题里复用的共 **0** 次。两侧点同一页、又取到同一句的行由脚本直接中止——同句不构成冲突，不允许拿复制冒充对照。差异性质用了 **12** 种。这 12 条性质各不相同（每种恰好 1 行），不在这里重述，逐条只写在表内「差异性质」列。

「本篇采用」一列写的不是判断谁对谁错，而是本篇正文按哪一说的走、以及为什么。这一列的 **9** 行标为已写进正文、**3** 行只在本附表登记（正文不替官方补结论，也不把两种说法合成一说）。这一列标「已写进正文」的 **9** 行本次已逐句核过成稿：它们要引的 **18** 句官方原句全部原样出现在成稿里（空白折叠后做子串比对），缺任何一句本脚本都会中止，所以这一列不是空头承诺。官方原句超过 130 字符按整词截断并以省略号结尾，共 **7** 处截断；截断只发生在显示层。

| 编号 | 议题 | 说法甲 | 说法乙 | 差异性质 | 本篇采用 | 正文落点 |
|---|---|---|---|---|---|---|
| F1   | 客户看到新配置要等多久 | 「After you update call configuration, WhatsApp users may take up to 7 days to reflect those changes.」（`calling_call_settings_md`｜第 10 句） | 「After call configuration is updated, WhatsApp users may take up to 7 days to reflect that configuration although most users…」（`calling_faq_md`｜第 230 句） | 补充（FAQ 补"多数更快"） | 本篇先给七天上限，再补 FAQ 的"多数用户更快"限定，不把每次配置变更都写成一周后才生效。 | 已写进正文 |
| F2   | 权限请求的配额与重置 | 「For example, your business can send only 2 permission requests in a 24-hour period.」（`calling_reference_md`｜第 170 句） | 「A connected call will reset the call permission limits.」（`calling_faq_md`｜第 288 句） | 补充（一处给配额，一处给重置条件） | 本篇把配额与重置并列写：24 小时内最多两次请求，任意一次接通即重置，不把两次写成永久上限。 | 已写进正文 |
| F3   | "唯一限制"与二十四小时窗口 | 「The only limit enforced for calling right now is the 1000 concurrent calls limit, but this may change in near future.」（`calling_faq_md`｜第 52 句） | 「Limit on connected calls in 24 hours is a time window based running limit.」（`calling_faq_md`｜第 298 句） | 措辞不一致（"唯一限制"与另有时间窗口） | 本篇按两个不同对象写：一千路并发是当前唯一并发上限，连接通话的二十四小时窗口限制另计，不把"唯一"读成没有时间窗口。 | 已写进正文 |
| F4   | 未接听在两侧分别记成什么 | 「Even unanswered calls are considered completed but will not have a `start_time` or `end_time` or `duration`.」（`calling_reference_md`｜第 267 句） | 「If your business does not respond, the call is terminated on the WhatsApp user side with a "Not Answered" notification and a…」（`calling_reference_md`｜第 54 句） | 视角差异（业务记录与用户提示） | 本篇分开写：业务侧把未接听记为完成但没有起止时间与时长，用户侧看到未接听提示并收到终止回调，两者不互相替代。 | 只在附表登记 |
| F5   | 首通显示号码、后续显示名称 | 「Because the WhatsApp client has not yet received your business profile details, such as your display name, the incoming call…」（`calling_faq_md`｜第 269 句） | 「Subsequent calls show your business display name.」（`calling_faq_md`｜第 271 句） | 例外与常规（首通与后续） | 本篇按首通与后续两段写：首通可能显示号码，后续显示名称，官方称修复进行中，不把首通表现写成永久行为。 | 已写进正文 |
| F6   | 免费现状与未来定价 | 「Separate pricing for recording is planned for a future date, and this page will be updated with the rates and advance notice…」（`calling_call_recording_md`｜第 7 句） | 「There is no launch date at this time, and this page and the feature docs will be updated with the rates and advance notice before…」（`calling_pricing_md`｜第 85 句） | 补充（定价页另称暂无上线日期） | 本篇按两页合并写：两项功能当前免费、未来分别定价且事先公告费率，定价页另称暂无上线日期，本篇不写任何具体日期。 | 已写进正文 |
| F7   | SIP 上的 mTLS 是否适用 | 「The Webhooks documentation suggests mutual TLS (mTLS) as an alternative to an IP allowlist, but that alternative does not apply…」（`calling_sip_md`｜第 269 句） | 「For SIP, Meta supports mTLS only on connections that Meta initiates to your SIP server, and mTLS does not cover media.」（`calling_sip_md`｜第 270 句） | 限定补充（看似全否，实为范围受限） | 本篇按限定后的口径写：SIP 上 mTLS 只覆盖 Meta 主动发起的连接且不覆盖媒体，不写成 SIP 完全不支持 mTLS。 | 已写进正文 |
| F8   | 同一条回调为什么可能到两次 | 「Calling related webhooks will likely have a shorter retry policy but stale webhooks may still be delivered as that may be useful…」（`calling_faq_md`｜第 113 句） | 「Failure recovery on Meta queueing infrastructure may result in duplicate webhook sends.」（`calling_faq_md`｜第 126 句） | 来源补充（重试与队列恢复） | 本篇写重复的两类来源：重试策略与队列恢复都可能造成重复，接收端按可能重复来设计，不假设一条事件只到一次。 | 已写进正文 |
| F9   | 超时之后两侧认知是否一致 | 「The connect webhook is attempted which fails with timeout after ~20s.」（`calling_faq_md`｜第 132 句） | 「In case of timeout, the webhook server thinks there is no failure but this is seen as failure that warrants a retry.」（`calling_faq_md`｜第 135 句） | 视角差异（发送端判定与接收端认知） | 本篇把超时写成双侧判定：Meta 侧约二十秒判失败并重投，接收端可能自认为已处理，双方认知不一致会留下重复。 | 已写进正文 |
| F10  | 通话限额与消息限额是不是一回事 | 「Calling API usage does not count towards messaging rate limits at the moment.」（`calling_faq_md`｜第 51 句） | 「Think of this as a rate limit for business initiated calls.」（`calling_faq_md`｜第 300 句） | 同名不同物（消息限额与通话自身限制） | 本篇把两处 rate limit 分开写：通话用量不计入消息限额，而通话自身另有一条按窗口计的限制，同名不同物。 | 已写进正文 |
| F11  | SIP 密码怎样重新生成 | 「To make Meta generate a new SIP password, you would need to disable SIP, delete SIP server and add your SIP server back.」（`calling_sip_md`｜第 159 句） | 官方未写明（—） | 单一说法（官方未写单独重新生成接口） | 本篇只登记官方给出的这条路径：没有单独的密码重新生成接口，本篇不复述为可执行步骤。 | 只在附表登记 |
| F12  | 录音与转写的产物存在哪里 | 官方未写明（—） | 官方未写明（—） | 官方未写（本批页面未写存储位置） | 本篇不写录音与转写产物存放在何处，只写官方给出的下载链接有效期与媒体保留期。 | 只在附表登记 |
| **合计** | **12** 个议题 | 两侧有句 **10**／单侧 **1**／两侧皆无 **1** | 引用页 **6** | 性质 **12** 种 | 正文落点：已写 **9**／只在附表 **3** | 截断 **7** 处 |

#ROWS=12 COLS=7


---

**边界声明：** 本篇是**官方通话能力、权限前提、状态回调与信令接入文本的口径梳理**，不是接入教程，也不是对平台呼叫能力的评价，更不是外呼或投放方案。全篇内容限于 WhatsApp 与 Meta 公开页面已经写出的表述，共 **25** 个页面登记位：Meta 开发者文档 **15** 页（能力总纲与发起 **2** 页、设置配置 **1** 页、接口与回调参考 **1** 页、录音与转写 **2** 页、SIP 与集成 **3** 页、定价 **1** 页、沙盒与审核 **2** 页、常见问题与排错 **2** 页、用户端入口 **1** 页）、WhatsApp 帮助中心 **10** 页（一对一语音、一对一视频、群组语音、通话链接、来电等待、通话记录、静默未知来电、通话 IP 保护、转接第二台设备、听不到声音）。引用这些文本的目的只有两个：说明平台把**通话、发起、权限、接听、终止、录音、转写、信令面**写成了哪些对象名、哪些状态词与哪些否定式前提；说明同一批文本把**接听窗口、超时、重投、七日生效、并发上限、二十四小时窗口、计费脉冲**写成了哪些数字与哪些限定词。本篇**不含**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法；**不含**任何可用于向非自愿收件人大量投递消息或反复呼叫的方法——"怎样绕过权限直接外呼""怎样伪造通话链接或呼叫按钮""怎样用 SIP 或回调机制压垮对方服务"这三类问法在本篇一律不答；**不含**代码、恶意样本或利用链构造方式；**不含**令牌获取、SDP 构造、媒体服务器配置、SIP 服务器搭建、回调验签实现与重试队列参数；**不含**任何针对他人设备或他人账号的操作步骤与时序。界面页里官方写成"步骤""清单"的内容——按钮渲染在哪里、等待室怎么进、静音与转移有哪些控件、IP 保护在哪个界面开关——本篇只登记**控件名、状态名与条目数**，**不重述每一步在哪个页签里怎么点，也不把清单转写成可执行的动作序列**，读者要动手请回原页。

五类页面在本篇里始终分开写，因为官方没有一页把它们画成同一张对象图：**能力与路径面**写在总纲页与发起页，回答通话是什么、两条发起路径怎么分（第 2 章）；**权限面**写在设置页与发起页，回答用户许可、请求配额与重置（第 3 章）；**状态与回调面**写在参考页与常见问题页，回答状态词、接听窗口、乱序、重复与超时（第 4 章）；**媒体处理面**写在录音页与转写页，回答逐通选择、告知、产物与保留（第 5 章）；**信令接入面**写在 SIP 页与两个集成页，回答证书、加密范围、回调默认值与三类典型故障（第 6 章）；**计费与限额面**写在定价页与常见问题页，回答脉冲、档位、并发与二十四小时窗口（第 7 章）；**用户端入口面**写在帮助中心十页与按钮页，回答用户看到什么、什么不进 API（第 8 章）。把这七层合并成"一条通话从拨出到计费的完整规则"，会得到官方没有写过的跨面结论，本篇不这么做。

官方写成区间、多档或带限定词的数字按整段一行登记在附表 B，不合成单值、不取中值、不去掉官方写在数字前面的限定词；同一数字在不同指代上各写一处的按指代分列，不做跨指代合并。B 表最终是 **39** 条、**40** 行（含合计），分布在 **11** 个页面，去重后的「数字＋单位」组合 **34** 组、单位词 **23** 种，五档分布为账号规模 **7**、时长与滚动窗口 **17**、文本与文件规格 **2**、速率与次数 **8**、生效日期与版本节点 **5**，同页同数字同单位去重 **12** 行、整句截断 **19** 行；截断只发生在显示层，进表的每一句都先用未截断整句回原页核对，失败 **0** 句。它的输入是原句池的 **990** 条整句（覆盖 **25** 页）。

正文引句全部为整句、逐字回核到抓取件。正文引用去重后 **237** 句（引用标记 **238** 处，同一句在正文重复引用 **1** 处），分布在 **24** 个页面上；官方标题在正文被点名 **37** 处、覆盖全部 **25** 个登记页。引句里含反引号的 **0** 句、含官方粗体排版标记的 **0** 句、带 `## 小标题` 的 **0** 句——含反引号字段名与枚举名的句子不整句引用，这些页面上的事实改用中文陈述并逐处标注出处页，这条规矩没有在本篇被绕过。正文共 **12** 章、**51** 节，另有一章引言与结论分列首尾。

方法层的三张表各自回答一个问题，且都不越界：附表 E 把同一组 **33** 个维度并排放在 **7** 列主体上＝ **231** 格，**66** 格取到官方原句、**165** 格标作「官方未按这一列写该项」，该标注**只说明官方文本的写法，不说明该类主体没有该项规则**；去重引用页 **18** 个、跨格复用句 **1** 次、显示层截断 **28** 格。附表 C 把「官方是否写明」限成「词（组）× 站点类别」矩阵：**56** 个词（组）分属 **8** 个主题组 × **2** 类站点共 **70** 行、句次 **1458**、去重命中句 **715**（占全池 **72.2%**），**1** 个词（`disclose`）按零命中登记，落在两个以上格子的句子 **419** 句。附表 D 只登记官方在页面里逐字写出的取值域：配置的 **10** 串标记本轮 **3** 串在登记页里出现（合计 **15** 行），另 **7** 串在扫过的每一行原文里零命中；最终 **5** 组、**12** 个取值、去重取值名 **7** 个、覆盖 **2** 页；扫到标记但取不到两个以上取值的 **10** 处连同页面标识、行号与判据写进 `_tmp/w6_a10_dmiss.tsv`，未进表。附表 F 把 **12** 个议题逐条对撞（两侧有句 **10**／只有一侧 **1**／两侧皆无 **1**，引用页去重 **6**、差异性质 **12** 种、复用 **0** 次、显示层截断 **7** 处、正文落点已核 **9** 行 **18** 句），凡官方留白处一律登记为留白，不推断、不补齐。

页面层的三项事实单独交代，免得读者把"登记"读成"引用"：**第一**，本篇登记的 **25** 页正文全部取回，没有一页被标注"全篇不引用"，也没有一页整页零句；**第二**，正文整句引用落在 **24** 个页面上，**25** 页每一页都在正文被官方标题点名（标题标记共 **37** 处），附表 A 的引用位置列没有一格留空；**第三**，正文引用的 **237** 句里没有一句含反引号，也没有一句带粗体或小标题标记。逐页状态由脚本实测而非人工填写，见附表 A 的引用位置列。

本篇对"权限状态的完整取值域""权限请求被拒后的冷却规则""去重键与去重窗口""录音与转写产物的存放位置""负反馈阈值的具体数值""二十四小时窗口的具体条数""SIP 密码的单独重生成接口""视频通话在 API 侧的独立规则"一律不补；这些官方只写了一半或根本没写的问题登记在附表 F 与第 11 章，正文只写官方实际写了的那一半。开发文档与帮助中心两套口径只按各自页面分别写：开发文档的对象、字段、状态与计时器，帮助中心的按钮、链接、等待室与提示，两套装在两章里，不合并成"WhatsApp 的通话规则"。

最后两处范围断言必须交代扫描口径，因为它们都是"穷举后没有"而不是"抽样没看到"：**其一**，本篇在 1.2 写"通话、企业发起、用户发起、呼叫权限、接听、终止、接入面"七个词的所指，第 9.1 节把它收成三种"通话"对象。这一句的依据是：附表 C 的 8 个主题组共 **56** 个词（组）在 **990** 句里逐句扫过，命中落在不同的页面与不同的字段上；但这只覆盖本篇登记的 **25** 页，两个站点还有大量本篇未登记的页面，所以"没有统一的通话能力表"是对这批语料的陈述，不是对 WhatsApp 全部公开文本的陈述。**其二**，附表 E 的 **165** 个空格是"官方未按这一列写该项"，不是"该项不存在"；读者若要把某一格当通则使用，应自己回到原页看官方在上下文中怎么连接这几句。

Telegram: https://t.me/one00190
