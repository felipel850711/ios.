@@TITLE@@WhatsApp 媒体消息、附件与媒体对象生命周期的官方口径解读
@@SUBTITLE@@本篇是"WhatsApp 群发／群控系统性口径"系列第四轮的第二篇（轴②，编号 n=13），讲的是：媒体对象在官方文本里被写成什么、媒体 ID 与媒体 URL 各活多久、四个时间尺度为什么不能合成一个、五类媒体消息各收什么格式与编码、静态 WebP 为什么会被转码、体积上限分布在哪三层、模板头图与轮播卡片怎么承载媒体、两种轮播的一致性约束差在哪、服务窗口与 TTL 为什么是两个计时器、公网链接的十分钟缓存怎么判定同一个资产、投递顺序由谁决定、回调里的媒体 ID 为什么另有一套时钟、帮助中心把同一批数字换成了哪些前提、以及本地档案与服务端留存为什么不能互相解释。全篇只读官方文本，共 **27** 个页面登记位（**27** 页正文全部取回，逐页归属与引用位置见附表 A 的 **28** 行含合计）：Meta 开发者文档 **16** 页（媒体对象 **1**、五类媒体消息 **5**、交互式媒体轮播 **1**、模板媒体 **2**、发送消息 **1**、Direct Send **1**、回调参考 **5**）、WhatsApp 帮助中心 **11** 页。
@@INFO@@报告日期|2026 年 11 月 19 日
@@INFO@@分析对象|按站点清点本篇登记的页面构成：Meta 开发者文档 **16** 页（Media、Image messages、Video messages、Audio messages、Document messages、Sticker messages、Interactive media carousel messages、Template media、Media card carousel templates、Send messages、Direct Send supported features and limits，以及 messages webhook 的 image／video／audio／document／sticker 五页参考）、WhatsApp 帮助中心 **11** 页（附件与影片、影片过长、媒体打不开、自动下载、媒体可见性、视图一次、贴纸、语音消息、语音预览、转发媒体、媒体档案）。合计 **27** 个登记位，正文全部取回；归组依据是官方页面标题，逐页归属见附表 A。
@@INFO@@视角定位|官方规则文本、帮助中心说明与开发者文档的**口径解读**，不是操作手册，也不是投放方案。本篇**不提供**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法；本篇同样**不写**任何可用于向非自愿收件人大量投递媒体或附件的方法——"怎样把一段影片当广告群发""怎样绕过模板审批把媒体塞进会话""怎样用大体积附件压垮对方的客户端"这三类问法在本篇一律不答；官方把媒体写成对象、消息、编码、上限与计时器，本篇只登记这些句子出现在哪一页、哪一句（界线见 1.3）。
@@INFO@@分类|**官方页面登记 27 个登记位 × 2 类站点 × 引用位置 × 取回状态**（附表 A，**28** 行含合计；Meta 开发者文档 **16**、WhatsApp 帮助中心 **11**；可引用 **27** 页、不可引用 **0** 页；原句池 **399** 条整句、覆盖 **27** 页）× **数字归档 15 条，覆盖 5 页，去重"数字＋单位" 11 组、单位词 8 种、性质 3 种（上限 9／缓存与复用窗口 2／自动解除或自动生效的时限 4），截断 2 行，池内不含数字的整句 355 句、定不出档位而丢弃的数字实例 22 个**（附表 B，**16** 行含合计）× **官方用词 × 站点类别矩阵 52 个词（组）分属 8 个主题组 × 2 类站点、61 行、句次 549、去重命中句 259（占全池 64.9%）、零命中词 7 个**（附表 C，**62** 行含合计）× **官方写明的取值域 6 组、取值 16 个、去重取值名 8 个、覆盖 6 页，配置的 10 串标记本轮扫到 2 串、8 串零命中，另有 3 处写了标记却取不出两个以上取值、分布在 2 页**（附表 D，**7** 行含合计）× **同一组维度在各主体上的逐列对照 39 个维度 × 7 列 = 273 格、有句 53 格／空格 220 格、去重引用页 16、同一句跨列复用 1 次、截断 15 格**（附表 E，**40** 行含合计）× **口径冲突与官方未写明 11 个议题、两侧有句 11、差异性质 11 种、去重引用页 9、复用 1 次、正文落点已核 10 行 20 句、截断 5 处**（附表 F，**12** 行含合计）× **正文与附表的官方原句逐字回核，失败 0 条**。
@@INFO@@难度|进阶（本篇有六处容易读错。**第一是体积上限按层算**：Cloud API 的媒体消息上限、用户端发送或转发的影片上限、用户文档上限分属三个对象，两页之间还有"默认"与"所有平台"两种前提（4.1 至 4.3、8.1，附表 F 的 F1 与 F11）。**第二是四个时间尺度不能合并**：媒体 ID 三十天、回调媒体 ID 七天、媒体 URL 五分钟、发送缓存十分钟，两两都不是同一把尺子（2.2、6.2，F2 与 F3）。**第三是服务窗口与 TTL 的方向相反**：一个决定能不能发自由格式，一个决定已发出消息还重试多久（6.1、6.4，F7）。**第四是"最多十张"与"二到十张"分属两个环节**：建模板时定死数量，发送时不能改（5.3，F6）。**第五是两种轮播的一致性约束不同**：模板型约束整卡组件，交互式只约束按钮类型与数量（5.4、5.5，F9）。**第六是语音的两个子类与状态媒体的两条时长**：语音消息与基础音频消息的下载行为不同，状态影片与状态语音各有上限（3.3、8.4，F4 与 F10）。）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

本篇是"WhatsApp 群发／群控系统性口径"系列第四轮的第二篇（轴②，编号 n=13），主题是**媒体消息与附件**：一张图片、一段影片、一条语音、一份文档、一个贴纸，在官方文本里各被写成哪一种消息对象；它们各自允许什么容器与编码、体积上限按哪一层算；媒体先上传还是直接用公网链接；媒体 ID 与媒体 URL 各活多久；发出去之后在哪一层被缓存、被重试、被丢弃；回调里回传的媒体标识又按哪一套时钟计时。全篇只读官方文本，共 **27** 个页面登记位：Meta 开发者文档 **16** 页、WhatsApp 帮助中心 **11** 页；**27** 页正文全部取回、全部进入原句池，共 **399** 条整句。

前几篇问的是"谁能发、发给谁、名字归谁、对方愿不愿意、到了之后怎么办、一条消息被算成几笔、商品与订单怎么摆"；本篇问的是"媒体在消息里是什么、上限写在哪一层、活多久、怎么取回、失败长什么样"。这条线在官方文本里被拆在五类页面上：一页媒体对象总纲写 ID、URL、加密与下载；五页消息类型页各写一种媒体的本体与规格；两页模板页与一页交互式轮播页写媒体怎么进模板与卡片；五页回调参考页各写一种媒体在回调里怎么回传；十一页帮助中心页写用户端看到的同一件事。五类页面之间没有统一的"媒体规格表"，只有互相指向的链接；本篇把它们并排登记在附表 E 的 **39** 个对照维度 × **7** 列 = **273** 格（**53** 格取到官方原句、**220** 格标作"官方未按这一列写该项"），并把成对出现的口径差异登记在附表 F 的 **11** 个议题里。

本篇的读法是**沿一个媒体文件走一遍**：先看它在官方文本里是什么对象、ID 与 URL 各管多久（第 2 章）；再看五类媒体消息各自长什么样、收什么编码（第 3 章）；然后是数字——体积、时长、数量上限分别写在哪一页、按哪一层算（第 4 章）；接下来是媒体怎么进模板与轮播卡片（第 5 章）；发出去之后，服务窗口、公网链接缓存、投递顺序与 TTL 各管什么（第 6 章）；回调里回传的媒体标识为什么另有一套时钟（第 7 章）；用户端的帮助中心把同一件事写成了什么（第 8 章）；最后是横向对照（第 9 章）、容易读错的几处（第 10 章）与边界（第 11 章）。

下面每个问题都标出它落在哪一节，一个问题可以跨相邻两小节，一节也可以由两个问题分别追问；跨小节的关联只做中文指认，不把两页原句拼成一句。

1. 这批页面在站点上怎么分布，"27 页登记、27 页有句"意味着什么？（1.1）
2. 本篇固定了哪些中文口径词，每个词对应哪个英文原词？（1.2）
3. 媒体 ID、媒体 URL、公网链接、附件在本篇里各指什么？（1.2）
4. 本篇不写什么，界面页写成"步骤"的内容登记到什么颗粒度？（1.3）
5. 为什么有些请求必须先有一个媒体 ID，ID 是怎么得到的？（2.1）
6. 媒体 ID 活多久，回调里拿到的媒体 ID 为什么是另一个数字？（2.2）
7. 媒体 URL 的 5 分钟与发送侧公网链接的 10 分钟，说的是不是同一件事？（2.2、6.2）
8. 媒体文件在服务端是明文还是密文，留存多久，什么情况下提前消失？（2.3）
9. 下载媒体要带什么，失败时官方给的两步是什么顺序？（2.4）
10. 五类媒体消息各是什么，官方各用哪一句话定义它们？（3.1 至 3.5）
11. 图片消息收什么格式，静态 WebP 会被怎么处理，哪些图会被拒？（3.1）
12. 影片消息的封面与编码有什么硬约束，音轨能有几条？（3.2）
13. 语音消息与基础音频消息是不是同一种东西，官方在哪一句把它们分开？（3.3）
14. 语音转写由谁设置，什么情况下转写会失败？（3.3）
15. 文档消息支持哪些类型，"其他类型也能发"这句话的边界在哪？（3.4）
16. 贴纸的动画与静态两种形态，官方把格式与体积写在哪一句里？（3.5）
17. 五类消息共享哪一套请求与响应骨架，失败时返回的是什么？（3.6）
18. Direct Send 的支持清单与这些消息页是什么关系？（3.6）
19. Cloud API 的媒体体积上限写在哪一页、写成多少？（4.1）
20. 用户端的影片上限为什么有两个数字，各自的前提是什么？（4.2）
21. 状态影片与状态语音的时长上限各是多少，它们是同一规则吗？（4.3）
22. 文档的 2 GB 是附件上限还是文档消息上限？（4.2、5.1）
23. 图片的 4,096 像素与帮助中心建议的最小边长分别约束什么？（4.4）
24. 附表 B 的 15 行分三档性质，为什么"上限"和"自动解除"要分开？（4.4）
25. 模板头图能放哪几类媒体，为什么必须先上传？（5.1）
26. 模板媒体的公网链接与媒体 ID，官方的主次怎么写？（5.2）
27. 轮播模板的卡片数为什么同时出现"最多十张"与"二到十张"？（5.3）
28. 同一模板的卡片在组件、按钮、正文上各要满足什么一致性？（5.4）
29. 交互式媒体轮播的按钮约束与模板型轮播是同一条规则吗？（5.5）
30. 服务消息的 24 小时窗口管的是哪一类消息，什么动作会让它重置？（6.1）
31. 公网链接的十分钟缓存怎么判定"同一个资产"，官方给了哪种绕开写法？（6.2）
32. 一连串消息的投递顺序由谁决定？（6.3）
33. TTL 是什么，超过 TTL 之后消息会怎样，凭什么判它被丢弃？（6.4）
34. 五页回调参考各自写什么，它们的骨架为什么几乎一样？（7.1）
35. 回调里号码字段在什么情况下会缺席，缺了以后靠什么对上人？（7.2）
36. 回调里的媒体 ID 为什么只有 7 天，和 API 的 30 天怎么并存？（7.3）
37. 帮助中心把附件入口与体积写成了哪些数字，与开发文档是怎么对上的？（8.1）
38. 用户端"打不开媒体"时官方给的排查顺序包含哪几类原因？（8.2）
39. 自动下载、媒体可见性与 .nomedia 三件事各改什么？（8.3）
40. 视图一次、转发、贴纸与语音消息在用户端各有什么独有说法？（8.4）
41. 用户端的"媒体信息"入口说明了文件被怎么组织？（8.5）
42. 把这批页面横向排开，哪几组词是"同词不同层"，哪几处是官方留白？（9.1、9.2）

# 1 口径与范围

## 1.1 登记、取数与五张表

本篇登记 **27** 页，来源只有两个站点：Meta 开发者文档 **16** 页（媒体对象 1 页《Media》、五类消息 5 页、交互式媒体轮播 1 页、回调参考 5 页、模板媒体 2 页、发送消息 1 页、Direct Send 1 页），WhatsApp 帮助中心 **11** 页（附件与影片、媒体打不开、自动下载、媒体可见性、视图一次、贴纸、语音消息、语音预览、转发、媒体档案）。这个分布不是按"哪边写得更细"选的，而是按"媒体在这条链路上被拆到哪一层"选的：对象与接口写在开发者文档，用户端能看到什么、收到什么写在帮助中心，两边对同一件事的措辞并不一致，第 9 章与第 10 章专门处理这种不一致。

取数分三层，三层共用同一批抓取件。**第一层是原句池**：脚本把每页正文切成整句，剔除含竖线的表格混合句、以列表符号起手的片段、长度越界的句子，得到 **399** 条整句，覆盖全部 **27** 页——这一轮没有整页零句的页面，所以附表 B 里"池内一句都没有的可引用页面"是 **0** 个。**第二层是词矩阵**：附表 C 用 **52** 个词（组）分 **8** 个主题组逐句扫过这 **399** 句，命中落在"词 × 站点类别"的格子里，全表 **61** 行、句次相加 **549** 次、去重后真正被命中的句子 **259** 句（占全池 64.9%），另有 **7** 个词在整池一句都没出现。**第三层是对照表**：附表 E 把 **39** 个维度并排放在 **7** 列主体上，每一格用"页面标识＋一条正则"在原句池里取句，取不到就留空并写明"官方未按这一列写该项"。

三层之外还有三张登记表：附表 A 逐页登记标识、站点、URL、官方标题、取回状态与引用位置；B 表只收"数字＋它数的东西"，本篇这批页面里带阿拉伯数字且能定出单位的整句比上一篇多，该表有 **16** 行（含合计），分布在 **5** 个页面；D 表只登记官方逐字写出的取值域，本篇有 **6** 组、**16** 个取值、覆盖 **6** 页。**数字变多不等于数字变全**：贴纸页与文档页各有一张"类型／扩展名／MIME／最大体积"的表格，表格行按建池口径整行剔除，所以贴纸的单档体积与文档按类型的体积都不在 B 表里；本篇正文因此不写这些数字，只用官方那两句可整句引用的概括句，读者要具体数值请回原页看表。

## 1.2 口径词：媒体对象、媒体 ID、媒体 URL、附件、载体

这条链路上最容易混的是五个词，本篇先固定中文译法与所指，后文不再改口。

**媒体对象（media object）**：官方在媒体页把它写成"通过这套 API 发送的全部媒体文件"，它有加密与留存规则，也有一个标识符。本篇把"媒体对象"当作文件本体，不当成消息；一条消息可以携带一个媒体对象，也可以只携带文字。**媒体 ID（media ID）**：媒体对象在 Meta 服务端的标识；它由上传得到，用来在发送请求里指代某个已经上载的文件。**媒体 URL（media URL）**：官方在下载媒体时返回的地址，带时效，过期后要重新查询 ID 才能拿到新的 URL——它与发送消息时填的公网链接不是同一个东西。**附件（attachment）**：本篇只在用户端语境里用这个词，指聊天里发出去的那一份照片、影片、语音或文档；开发文档把同一件事写成"媒体消息"。**载体（carrier）**：本篇用来指承载媒体的消息形态，共五种——图像、影片、语音或音频、文档、贴纸；模板头图与轮播卡片是另外两类载体，第 5 章单独写。

另外两个词本篇按官方原词照写、不译成别的：**模板（template）** 与 **轮播（carousel）**。前者指预审批的消息模板，媒体要放进模板时得先上传再引用；后者指横向滚动的卡片集合，卡片可以是图像或影片头图。另有一组必须分开的官方词：**语音消息（voice message）** 与 **基础音频消息（basic audio message）**——它们在开发文档里是同一页的两种子类，帮助中心说的"语音消息"只对应前者，第 3.3 节把这件事钉住。

## 1.3 不写什么

本篇的边界与系列前几篇一致：全篇只登记官方写出的对象、字段、类别、上限与状态，**不提供**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法；**不含**任何可用于向非自愿收件人大量投递媒体或附件的方法——"怎样把一段影片当广告群发""怎样绕过模板审批把媒体塞进会话""怎样用大体积附件压垮对方的客户端"这三类问法在本篇一律不答。界面页里官方写成"步骤"的内容——在哪个页签点哪个按钮、录像怎么按住、贴纸包怎么排序——本篇只**点名与计数**（例如"两处入口""六种支持格式"），**不重述点击次序**，也不把清单转写成可执行的动作序列；读者要动手请回原页。

还有一类"不写"是对象层面的：本篇不判断某个体积上限该不该这样定、不评价某种编码选得好不好、不推断官方没写的转码策略与存储位置、不把帮助中心的用户端口径与开发文档的服务端口径合并成一条"WhatsApp 的媒体规则"。这些空白在附表 F 里逐条登记，正文只在它们真正影响读法的地方点名。

# 2 媒体对象：一个 ID、一个 URL、三个时钟

## 2.1 为什么有些请求必须先有一个媒体 ID

《Media》是这批页面里唯一一页把"媒体"当成对象而不是消息来写的。它开篇先划范围：

> `Some of the API requests described in this document require a media ID.`

也就是说，不是每条请求都要媒体 ID，但有一批请求以它为前提。官方在五类消息页的参数说明里给同一件事写了两个入口：用已上载的媒体时填媒体 ID，用托管在自己服务器上的媒体时填媒体 URL——两条入口在参数表里各占一格，本篇不整句引用带字段名的表格格，只作中文陈述。媒体 ID 从哪来？官方在模板页把它写成"创建模板前必须先用可续传上传接口上传媒体文件"，上传完成返回的标识再填进模板头部的字段（第 5.1 节整句引用）。这条上传路径在媒体页上没有被重写成一张独立的流程页，本篇因此只登记"ID 是上传的产物、发送时用来指代已上载的文件"，不替官方补上传的接口参数与分段规则。

## 2.2 三个时钟：ID 三十天、回调 ID 七天、URL 五分钟

媒体页把三个时效写在同一页的不同位置，分别管三个对象。第一个是 API 返回的媒体 ID：

> `Media IDs returned by the API expire after 30 days.`

第二个是回调里的媒体 ID：

> `Media IDs in webhooks expire after 7 days.`

两句只差"returned by the API"与"in webhooks"，天数却不同。它们不是同一对象的两个版本，而是**两个取得渠道各自的计时起点**：一个从 API 返回时起算，一个从回调出现时起算。第三个是下载地址：

> `Media URLs **expire after 5 minutes**, after which you must query the ID again to get a new URL.`

URL 只有五分钟，过期后官方给的动作不是"重试同一个 URL"，而是"重新查询 ID 拿一个新 URL"——这反过来说明 ID 在五分钟这个尺度上仍然是可用的。三个数字摆在一起，读法是：**ID 是长时效的句柄，URL 是短时效的取件单**；句柄先到期还是取件单先到期，取决于你手里的 ID 是从哪条渠道拿到的（附表 F 的 F2 把两个 ID 数字并排登记）。

## 2.3 加密与留存：三十天与"提前删除"

媒体对象在服务端存多久、以什么形态存，官方在媒体页用一句判断句写清：

> `All media files sent through this API are encrypted and persist for 30 days, unless they are deleted earlier.`

这句有三个成分：发出去的全部媒体文件都加密；留存三十天；除非更早被删除。第三项是一个**否定式例外**，官方在同一句里没有写"谁能删、怎么删、删了之后 ID 与 URL 立刻失效还是稍后失效"——这些在这批登记页面里没有对应句子，本篇不补。读者要注意"加密"修饰的是"全部媒体文件"，没有按消息类型分档，也没有说加密密钥归谁管；密钥与解密不在本轴登记范围内。

## 2.4 下载链路：带什么、失败先做什么

下载媒体的动作在媒体页被写成两步：先拿到 URL，再带着凭证去取文件。成功时的返回是：

> `Upon success, the API will respond with the binary data of the media asset.`

响应头里另有一项说明数据类型：

> `Response headers contain a content-type header to indicate the MIME type of returned data.`

凭证这一层官方写得很直接：

> `Include your access token in the request. **If you omit your token, the request will fail.**`

带令牌、不带就失败，没有"可选凭证"这种中间态。失败时官方给的不是一个笼统的"重试"，而是一条两步路径。第一步：

> `In that case, try to [get a new media URL](#get-media-url) and download it again.`

重取之后仍然不行，才走第二步：

> `If doing so doesn't resolve the issue, renew your access token and attempt to download the media asset again.`

**顺序是官方给的，不是本篇排的**：先换 URL，再换令牌；两步都写在相邻的两句里，本篇照录，不把它们写成"两种并列的排查选项"，也不把它扩成操作步骤（附表 F 的 F8 把这两句并排登记）。

# 3 五类媒体消息的本体

## 3.1 图像：单图、转码与两条拒绝线

《Image messages》的第一句就是定义：

> `Image messages are messages that display a single image and an optional caption.`

"单张图像＋可选说明文字"是这类消息的全部结构；没有多图、没有图集。格式上官方给了一条转换规则：

> `You can send a static WebP image in an image message, and it is transcoded to JPEG.`

静态 WebP 不是直接原样投递，而是**转成 JPEG**；这句话同时限定了"静态"这个前提。紧接着是拒绝条件：

> `Animated WebP images, files that cannot be decoded as valid WebP, and images over 4,096 pixels on an edge are rejected.`

三种拒绝写在同一个句子里：动画 WebP、无法按有效 WebP 解码的文件、任一边超过 4,096 像素的图像。第三项是**像素边长的上限**，与体积上限不是同一个量纲，第 4.4 节会把这两个数放在一起对照。请求失败的条件官方写在另一句里：媒体 ID 无效或已过期、图像格式不受支持、图像超过支持格式表里的最大体积——这三种都让请求失败（这一句带字段名，按第 1.3 节的引句规矩只作中文陈述）。失败时返回什么，官方另有一句：

> `When a request fails, the API returns an error response instead of a message ID.`

**失败返回的是错误响应，不是消息 ID**——这一句决定了"拿到 ID 才算受理"这条判据。

## 3.2 影片：封面、编码与音轨

《Video Messages》的定义句把封面写进了消息结构：

> `Video messages display a thumbnail preview of a video image with an optional caption.`

点击封面之后发生什么：

> `When the WhatsApp user taps the preview, it loads the video and displays it to the user.`

编码约束只有两句，都是硬约束：

> `Only H.264 video codec and AAC audio codec supported.`

> `Single audio stream or no audio stream only.`

影片容器只认 H.264 视频编码与 AAC 音频编码；音轨要么一条，要么没有。**"没有音轨"是允许的**，这句话容易被读成"必须有一条音轨"，原文的 "or no audio stream" 是并列选项。影片页没有写体积上限，也没有写时长上限——那两个数字在媒体页与帮助中心页各有一处，第 4 章分开登记，不把别页的数字搬进这一页的语境里。

## 3.3 语音与基础音频：同一页里的两个子类

《Audio messages》开篇就把能力范围写成两类：

> `You can use Cloud API to send voice messages and basic audio messages.`

两者的差别在这几句里：

> `A voice message (sometimes referred to as a voice note, voice memo, or audio) is a recording of one or more persons speaking, and can include background sounds like music.`

> `Voice messages include features like automatic download, profile picture, and voice icon.`

> `These features are not available with basic audio messages.`

也就是说，自动下载、头像与语音图标这些是**语音消息独有**的外观与行为；基础音频消息没有这一套。基础音频消息长什么样、怎么被播放，官方另有两句：

> `Basic audio messages display a download icon and a music icon.`

> `When the WhatsApp user taps the play icon, the user manually downloads the audio message for the WhatsApp client to load and then play the audio file.`

**一个显示下载与音乐图标、需要用户点了播放才下载，另一个自动下载并显示语音图标**——这两句是第 10.6 节要对照的那组"同词不同层"的来源。语音消息的编码是硬要求：

> `Voice messages must be Ogg files encoded with the **OPUS** codec.`

Ogg 容器加 OPUS 编码；换成别的文件类型或别的编码，转写会失败：

> `If you send a different file type or a file encoded with a different codec, voice message transcription will fail.`

转写本身由用户端设置决定：自动、手动、从不三档，自动档直接带出文字，手动档显示一个"转写"入口，从不档不显示文字——这三句都带界面文案，本篇按第 1.3 节只作中文陈述。官方把最常见的一类错误也写在同一页：

> `The most common errors associated with audio files are mismatched MIME types (MIME type doesn't match the file type indicated by the file name) and invalid encoding for Ogg files (OPUS codec only).`

MIME 类型与扩展名对不上、Ogg 文件用了 OPUS 以外的编码，是官方点名的两类常见错误；页面上紧接着的建议是核对 MIME 与扩展名、Ogg 文件改用 OPUS 编码（两句都带字段名，只作中文陈述）。

## 3.4 文档：图标、可下载与"支持边界"

《Document messages》的定义句把"可点开下载"写进了消息本体：

> `Document messages are messages that display a document icon, linked to a document, that a WhatsApp user can tap to download.`

文档这一类最重要的不是它能发什么，而是官方在哪一句划了"支持"的边界：

> `Only the above listed document types are officially supported and guaranteed to display correctly in the WhatsApp client.`

只有页面上列出的那几类文档是官方支持并保证在客户端正确显示的。那"其他类型"呢？官方没有一刀切禁止：

> `Other file types may be sent via the API, but they are not supported and may not be handled as expected.`

**"可以通过 API 发送"与"受支持"是两件事**：前者是能力，后者是官方保证。这句话的边界正好解释了为什么文档页的参数表里有一列"最大体积"、而本篇不引用那些表格行——表格行按建池口径整行剔除，正文只用这一句概括句，不给具体类型的体积数字。

## 3.5 贴纸：动画与静态、格式清单与失败条件

《Sticker messages》的定义句把两种形态并列：

> `Sticker messages display animated or static sticker images in a WhatsApp message.`

格式与体积在页面上是一张表，表格前有一句概括：

> `WhatsApp supports the following sticker file formats and size limits for sticker messages.`

**这一句只说"有这张清单"，清单本身（类型、扩展名、MIME、最大体积）是表格行**，本篇按第 1.3 节的口径不引用表格行，也不把表里的数字搬进正文；读者要具体档位请回原页。请求失败的条件与图像页同构：媒体 ID 无效或过期、贴纸不是受支持的 WebP 类型、文件超过表里的最大体积（这一句带字段名，只作中文陈述）。失败时的返回与图像页完全一致：

> `When a request fails, the API returns an error response instead of a message ID.`

同一句"错误响应而不是消息 ID"在图像页与贴纸页各出现一次，两页给的是同一条判据；附表 C 把这两句分别记在两页的格子里，不因为措辞相同就合并成一处。

## 3.6 共同的骨架：受理、响应与失败

五类消息页各写各的本体，但发送这一段共享同一套骨架，官方在发送消息页把它收成一句：

> `This works for all message types on this page.`

请求被受理时的返回是：

> `The Messages API returns the following JSON response when it successfully accepts your send message request.`

这句后面紧跟一条最容易被读错的限定：

> `This response only indicates that the API successfully **accepted your request** — it does not indicate successful delivery of your message.`

**返回体只证明请求被受理，不证明消息被送达**；送达状态走另一条通道：

> `You receive delivery status via **messages** webhooks instead.`

把这四句与各消息页的失败句并排，五类媒体的共同骨架是：请求带着媒体标识或公网链接发出；受理则返回消息标识，失败则返回错误响应而不是标识；送达与读取另由回调报告。《Supported features and limits》在这一层只做索引，不做定义：

> `This page summarizes what Direct Send supports.`

它的正文并没有另写一套媒体规格，而是把"未列出的格式或功能一律尚未支持"写成一条总则（这一句带官方排版标记，按第 1.3 节只作中文陈述），并对排队行为补了一句：

> `A paced message may be held and delivered later, or dropped.`

**排队中的消息可能被延后投递，也可能被丢弃**——这一句与第 6.4 节的 TTL 是两种不同层面的"没送到"：一个是排队策略，一个是重试期限，本篇不把它们合并成一条规则。

# 4 体积与格式：数字落在哪儿

## 4.1 Cloud API 的媒体上限

媒体页在讲媒体对象时给出一条全局上限：

> `The maximum supported file size for media messages on Cloud API is 100MB.`

同一页在后半段又用两句说明这条上限在回调里怎么被使用：

> `Max file size we currently support: 100MB.`

> `Send customers a warning message that their media file exceeds the maximum file size when this webhook event is triggered.`

三句说的是同一件事的三个位置：上限本身、官方当前支持的数值、以及超过上限时官方建议给客户发一条提醒。**"100MB"在这三句里是同一个数字**，附表 B 按"同页同数字同单位只留一行"的口径只登记一行，但正文把三处都点名，因为它们各自回答不同的问题：上限是多少、官方怎么表述它、超限之后回调能做什么。

## 4.2 用户端的影片与文档：两套数字、两个前提

帮助中心的附件页把影片上限写成**随网速变化**的两档：

> `For users with a faster internet connection, the default video size limit is 100MB and 720p resolution.`

> `For users with a slower internet connection, the default video size limit is 64MB and 480p resolution.`

快网与慢网、高清与标清，两组默认值成对出现；这两句的前提是"默认"，不是"最大"。同一站点的另一页则给出了一个不带前提的上限：

> `The maximum file size for videos sent or forwarded through WhatsApp is 64MB on all platforms.`

**"默认上限"与"所有平台的上限"是两层**：前者描述默认设置随网络与画质浮动，后者描述平台侧的硬上限；把 823886715479103 的六十四 MB 直接套到 453914586839706 的默认值上，会得到官方没有写过的结论。这两句并排登记在附表 F 的 F11 行，正文并列引用，不替官方判断哪一个是"真正的上限"。文档在用户端另有一套数字：

> `For documents, the maximum file size allowed is 2 GB.`

同一站点另一页把同一件事再写一遍：

> `The maximum allowed document size is 2 GB.`

两句措辞不同、数值一致；B 表按页分别登记，不合并成一行。

## 4.3 时长与数量：状态影片、状态语音与一次选图

状态类媒体有独立于消息的时长上限：

> `Status videos have a maximum duration of 90 seconds.`

> `Record and send a voice status update You can record and share up to 30-second voice updates.`

影片九十秒、语音三十秒，两个数字属于两种媒体，不是同一规则的两档；附表 F 的 F10 把这两句并排，正文只按媒体种类分开写。一次能选多少张图，官方也有一句：

> `Tap and hold to select up to 100 images.`

这一句是用户端选图动作的上限，不是一条消息能承载的图片数量——图像消息页写的是"单张图像"，两者说的不是同一个环节（附表 E 的"用户端一次选图数量"一行与"消息本体定义"一行分别登记）。

## 4.4 两个像素数与附表 B 的三档性质

图片页的 4,096 像素是**拒绝线**：任一边超过就拒收（第 3.1 节已整句引用）。帮助中心另外给了一个更小的边长，是**建议值**：

> `To avoid low quality profile pictures, we suggest using an image with height and width of at least 192 pixels.`

前者约束媒体消息能否被受理，后者建议头像的最低清晰度；两个数字都以像素为单位，但对象与后果完全不同。附表 B 把这类数字收成一张按"数字＋单位＋整句＋出处"排列的表：**16** 行（含合计）、**15** 条，分布在 **5** 个页面，去重后的"数字＋单位"组合 **11** 组、单位词 **8** 种、性质 **3** 种——上限 **9**、缓存与复用窗口 **2**、自动解除或自动生效的时限 **4**。第三档单独存在，是因为"三十天后过期"不是对某个动作设的上限，而是对象自身的寿命；把它并进"上限"会让"上限"这个词同时指两种东西。

B 表也写清了自己漏掉了什么：池内 **355** 句整句不含阿拉伯数字；脚本扫出 **22** 个"数字后面四个词都对不上任何一档"的实例，全部连同整句写进 `_tmp/w6_a8_bdrop.tsv` 供复核，不进表。**这几个数不是官方数字**，是本篇对语料的计数，读者不要把它们当成平台的规模或上限。贴纸页与文档页的表格行不在 B 表里，原因已在第 1.1 节交代：表格行整行剔除，正文不为它们补数字。

# 5 模板媒体与轮播卡片

## 5.1 模板头图：四类媒体与"先上传"

媒体进模板时有独立的入口。《Template media》的定义句把可承载的类型列全：

> `A media header allows you to add an image, video, GIF, or document at the top of your WhatsApp template message.`

图像、影片、GIF、文档四类，位置是模板消息顶部。能不能直接用公网链接？可以，但官方在这之前还有一条前置动作：

> `Before creating the template, you must upload your media file using the Resumable Upload API.`

上传之后返回的媒体标识填进模板头部的字段（这一句带字段名，只作中文陈述）。**"创建模板之前"是时间条件**：不是发送时上传，而是建模板时就要有媒体标识。这一条把模板媒体与普通消息媒体分开：普通消息可以在发送时填 ID 或 URL，模板头图必须先上传。

## 5.2 链接与 ID：主次而不是互斥

模板媒体用链接还是用 ID，官方在同一页给了两句，方向一致但不是同一条：

> `If you're using link, your asset must be on a publicly accessible server or the message will fail to send.`

> `To reduce the likelihood of errors and avoid unnecessary requests to your public server, Meta recommends that you upload your media assets and use their IDs when sending messages.`

第一句是条件：用链接就必须公网可达，否则消息发不出去。第二句是建议：为了减少错误与不必要的公网请求，官方建议上传并使用媒体标识。**条件与建议两句话并列，不互相取消**：链接路径没有被告知"不能用"，只是被要求满足可达性；ID 路径没有被告知"必须用"，只是被建议优先。附表 F 的 F5 把这一对并排登记，正文不把它写成"官方禁止链接"。

## 5.3 轮播卡片：最多十张与二到十张

《Media card carousel templates》把模板型轮播的数量约束写在同一页的两句里，答案看起来像两个：

> `Media card carousel templates allow you to send a single **marketing template** message accompanied by a set of up to 10 product media cards in a horizontally scrollable view: When a WhatsApp user taps a media card's **URL** button to buy a product, the URL mapped to the button is loaded in the device's default web browser, thus taking the WhatsApp user out of the WhatsApp client experience.`

> `Define the exact number of product cards (minimum 2, maximum 10) when you create the template.`

前一句是**消息侧**的上限：一条模板消息最多带十张商品媒体卡；后一句是**建模板时**的取值区间：卡片数量必须是二到十之间的确定值。两者不是同一条规则的两个版本，而是同一对象在两个环节的约束：建模板时定死数量，发消息时不能超过这个数量。

> `An approved template can only be used to send the same number of cards as defined during its creation.`

**已批准的模板只能按创建时定义的数量发送**——这句话把"二到十"的区间收成一个固定值：区间只存在于创建环节，发送环节没有区间。附表 F 的 F6 把"最多十张"与"二到十张"两处并排，正文按环节分开写。轮播的组成还有一句概括：

> `Carousel templates consist of a message body text and up to 10 product media cards.`

## 5.4 卡片的一致性：组件、按钮与正文

轮播模板对"每张卡长得一样"有硬要求：

> `All cards defined on a template must have the same components.`

卡片的可选部件与按钮数量官方另有一句：

> `Each card in the template has an image or video header asset and can optionally include a body text and up to two buttons.`

正文的一致性是一个条件句：

> `If any card in the carousel includes a card body text, then all cards must include a card body text to ensure consistent card heights.`

**只要有一张卡带正文，所有卡都必须带**，目的是让卡片高度一致。变量层还有两条：模板正文里的字符串数量必须与占位符数量一致；URL 按钮的变量必须追加在 URL 字符串末尾（两句都带字段名或引号，只作中文陈述）。这些约束不是"建议"，违反时模板不会被接受——但官方在这批页面里没有逐一写出拒绝的响应，本篇也不替它补。

## 5.5 交互式媒体轮播：不是模板，是消息

《Interactive media carousel messages》与 5.3、5.4 的两页不是同一类页面：模板页写的是"先建模板、再按模板发"，这一页写的是**直接作为一种消息发出去**的轮播。它的定义句：

> `Interactive media carousel messages display a set of horizontally scrollable media cards.`

每张卡的组成：

> `Each card can display an image or video header, body text, and either quick-reply buttons or a URL button.`

跨卡一致性在这里换了一个约束对象——模板型要求"所有卡组件相同"，交互式要求"按钮类型与数量相同"：

> `Button types and numbers must match across all cards (for example, if you define a card with 2 quick-reply buttons, all cards must define exactly 2 quick-reply buttons).`

这两条不是同一个句子的两个版本：前者的对象是整张卡的组件集合，后者的对象只有按钮（类型与个数），正文不合并（附表 F 的 F9 并排登记）。页面给出的示例请求按卡片数量各写一遍，示例本身只说明消息可以带多张卡，不构成上限——上限写在哪里，这一页没有像模板页那样给出一个数字，本篇也不替它补。

**"不需要模板"不等于"不受消息规则约束"**：它仍然是一条消息，仍然走发送接口、仍然返回消息标识或错误响应（3.6）；区别只在媒体与卡片的组织方式，不在投递通道。附表 E 的 E15 至 E19 五行把两种轮播的约束分列在"模板与轮播页"这一列里，取到的句子分属两页，正文按页分别引用。

# 6 发出去之后：窗口、缓存、顺序与 TTL

## 6.1 服务消息的二十四小时窗口

《Service messages》开篇就把两类消息的边界写清：

> `Service messages are free-form messages that you can send to WhatsApp users during a [customer service window](#customer-service-windows).`

服务消息是自由格式，但它只能在客户服务窗口内发；模板消息则相反：

> `Unlike template messages, service messages do not require pre-approval — you can compose and send them as needed in response to a WhatsApp user's message or call.`

> `Service messages can only be sent via the Messages API.`

> `To message WhatsApp users outside of a customer service window, use template messages instead.`

四句连起来是一条完整的分流：窗口内可以自由写，窗口外必须用模板；服务消息不需要预审批，但只能走消息接口，不能走别的通道。窗口本身怎么计时，官方有一句：

> `If the user messages or calls you again before the timer expires, the timer resets to 24 hours.`

**计时起点是"用户来消息或来电"，重置条件也是同一件事**：在计时到期前用户再来一次，窗口重置为二十四小时。窗口打开期间能发什么，官方又用一句收口：

> `While the window is open, you can send any of the following service message types to the user.`

计费与质量也各有落点：服务消息按服务类计费（这一句带计费类别名，只作中文陈述）；消息质量由过去七天的接收反馈加权计算，而高流量号码的质量可能在几分钟内变化（这两句带平台术语，只作中文陈述）。**窗口只决定"能不能发自由格式"，不决定"质量分高不高"**：两件事在页面上各写各的，本篇不把它们读成一条。

## 6.2 公网链接的十分钟缓存

发送时用公网链接指代媒体，服务端会做一层缓存。官方把复用条件写成：

> `The cached asset is reused in subsequent send message requests if the link in subsequent payloads is the same as the link in the initial payload.`

也就是说，判定"同一个资产"的依据是链接本身；同一链接在十分钟内再次出现，就复用第一次取到的文件。不想要复用时的写法官方也给了一句：

> `If you don't want the cached asset reused in a subsequent message within the 10 minute time period, append a random query string to the asset link in the new send message request payload.`

在链接后追加一个随机查询串，服务端就把它当成新资产，重新从服务器取一次并重新缓存十分钟（这一句的前半段带字段名，只作中文陈述）。**"五分钟的下载 URL"与"十分钟的发送缓存"不是同一个计时器**：前者是官方发给你的取件单，后者是平台替你取件的缓存窗口；两者方向相反（一个对外、一个对内），数值也不同（附表 F 的 F3 并排登记）。这条缓存只对"链接"路径成立；用已上载的媒体标识发送时，页面上没有对应的缓存描述，本篇不替它补。

## 6.3 投递顺序不由请求顺序决定

消息一条条发出去，到达顺序官方写得很直接：

> `When sending a series of messages, the order in which messages are delivered is not guaranteed to match the order of your API requests.`

**请求顺序与投递顺序不保证一致**——这一句是排版的、不是内容的问题；它不影响每条消息的送达回执，只影响读者对"先发先到"的预期。本篇不写任何"怎么保证顺序"的做法，官方在这一页也没有写。

## 6.4 TTL：重试期限与"判为丢弃"

消息投不出去时，平台不是无限重试，而是有一个时间窗：

> `If the Cloud API is unable to deliver a message to a WhatsApp user, it retries delivery for a period of time known as a time-to-live, TTL, or the message validity period.`

这个期限对部分模板可以自定义：

> `You can customize the default TTL for authentication and utility templates, and for marketing templates sent using the Marketing Messages API for WhatsApp.`

期限到了之后发生什么，官方用一句动词很重的句子写：

> `The platform drops messages it cannot deliver within the default or customized TTL.`

判据在下一句里：如果超过 TTL 仍未收到状态为"已送达"的回调，就应当假定这条消息已被丢弃（这一句带状态字段名，只作中文陈述）。失败状态的回调可能延迟，官方因此建议留一点缓冲再下判断（这一句同样带字段名，只作中文陈述）：

> `Build in a small buffer before assuming the message was dropped.`

**TTL 与服务窗口是两个计时器**：窗口管"能不能发自由格式"，TTL 管"已发出的消息还重试多久"；一个在发送前，一个在发送后，数值也不同。附表 F 的 F7 把这两句并排，正文不把它们写成同一个截止时间。

# 7 回调里的媒体

## 7.1 五页参考、五个媒体类型

回调参考页共五页，一页一种媒体：《Image messages webhook reference》《Video messages webhook reference》《Audio messages webhook reference》《Document messages webhook reference》《Sticker messages webhook reference》。五页的开篇句式相同，只换媒体名；以图像页为例：

> `This reference describes trigger events and payload contents for the Messaging account **messages** webhook for messages containing an image.`

文档页的对应句是：

> `This reference describes trigger events and payload contents for the Messaging account **messages** webhook for messages containing a document.`

影片、语音与贴纸三页的同一位置也各有一句，措辞同样只换媒体名（三页见附表 A 的逐页登记）。**五页不是五套规则，而是同一套回调骨架在五种媒体上的展开**：每页各有一个触发说明、一组字段口径与一个示例负载；字段口径五页几乎逐字相同，示例负载按媒体类型各给一份。以图像页为例，示例负载的开头是：

> `The following is an example messages webhook payload for an image message.`

文档页的示例负载同理：

> `The following example shows a document messages webhook payload.`

**读这五页要按"骨架＋媒体差异"读**：把五页的字段口径逐句并排，会发现它们在"号码字段何时缺席""字段是否随用户名出现"这些问题上用的是同一批句子；真正的差异集中在媒体类型与示例负载。这批共有句在本篇的词矩阵里按站点类别归格、不按页拆行——词矩阵回答的是"这个词在哪个站点类别出现过、命中多少句"，哪一页说过几次要回原页看；五页各自的引用位置由附表 A 的逐页登记钉住。

## 7.2 号码字段何时缺席、何时回显

回调里最常被集成方依赖的是号码字段，官方对它的缺席条件写得很明确：

> `Omitted if the user has adopted a username and their phone number is unavailable.`

反过来，有些字段是**无论用户是否采用用户名都会包含**的：

> `Included whether or not the user has adopted a username.`

这两句给出的是"字段存在性"的两类口径：一类随用户名与号码可用性变化，一类不随。页面在字段说明里还写了"发送时用什么标识，回调就回显什么标识"——这一句带字段名，只作中文陈述。另有一类取值域（例如某个来源字段只能取"图像"这一值）写在参数说明格里，按第 1.1 节的口径只点名、不整句引用。**号码字段缺席不等于消息无效**：官方在五页里都没有把"缺号码"写成丢弃条件，本篇也不这样推断。

## 7.3 回调里的媒体 ID 为什么只有七天

第 2.2 节已把两个数字并排：API 返回的媒体 ID 三十天、回调里的媒体 ID 七天。这一节的补充只有一句：**回调里的媒体 ID 是"事件发生时才拿到"的句柄**，它的计时起点晚于 API 返回的 ID，所以两个数字并不矛盾，也不该互相替代。集成方从回调里拿 ID 去下载媒体时，七天的窗口就是可用期；超过之后再拿同一个 ID 去查 URL，官方在这批页面里没有写会得到什么，本篇也不补。回调页本身没有重复写这个天数——它写在媒体页上，回调页只负责回传字段；这一处"规则在 A 页、字段在 B 页"的分布，是这批页面的常态，附表 A 的引用位置列把它逐页登记下来。

# 8 用户端帮助中心：同一件事的另一套写法

## 8.1 附件入口与体积：同一批数字，两种前提

《How to send media, contacts, or location》讲的是用户怎么把照片、影片、文档、音频、位置与联系人附进聊天。它写下的体积数字在第 4.2 与 4.3 节已经逐句引过：影片默认上限随网速与画质分两档、文档 2 GB、状态影片 90 秒、一次可选图片有上限、头像另有一个最小边长建议。这一节不重复引用，只补它没写的那一层：**这一页把"附件"当成用户动作的对象**，每一项都挂在"选什么、选多少"上；它不使用"媒体消息""媒体对象"这些开发文档里的名词，也不提服务端的留存。

同站点的《Can’t send longer videos》从"影片发不出去怎么办"切入，把同一批数字换了一个前提重写。它先给出一条全平台上限（这句在 4.2 已引），紧接着又写：

> `For users with a faster internet connection, the default video size limit is 100MB and 720p resolution.`

这一句与 4.2 引的快网默认值是同一个数字，但**前提不同**：一处写"默认"，一处写"更快的网络连接下"；一处与慢网档成对出现，一处单独成句。本篇按页分别引用，不因为数字相同就合并（附表 F 的 F11 把这两句并排登记）。

页面还给了两条替代路径，都不改变上限、只改变"换哪一种消息发"：录一段分辨率更低的影片，或者把影片当文档发送，文档路径的上限更高（这一句带界面选项名，只作中文陈述）。**"当文档发送"不是提高影片消息的上限，而是换一种消息类型**——官方把两条路径分开写，本篇也分开记。

## 8.2 打不开媒体：排查顺序里的五类原因

《Can’t download, open, or send media files》是用户端"媒体打不开"的总入口。它先给出一条格式清单：

> `Supported video formats include 3GP, AVI, FLV, MKV, MOV, and MP4.`

这一句在页面里是"支持哪些影片格式"的答案，不是"消息里允许哪些编码"的规格；开发文档的编码约束写在影片页（3.2），两处不是同一层。接着它把最常见的原因排在第一位：

> `If you can’t download, open, or send media files, the most common reason why is a weak internet connection.`

然后是格式不支持时的提示：

> `If you see the error message “This file format is not supported” , check to make sure that you’re using the most updated version of WhatsApp.`

再往后是存储权限：

> `Check storage and media permissions WhatsApp needs permission to access your phone’s storage and media files.`

> `Without this permission, you won’t be able to download, save, or send photos, videos, and documents.`

最后一类是"文件本身打不开"——例如设备上没有对应的阅读器：

> `Can’t open PDF If you're unable to open a downloaded PDF file on your phone, try the following: Make sure you have a PDF reader installed on your phone.`

把这些句子排开，页面给的是一张**按可能性从高到低**的排查表：网络、版本、存储卡、权限、外部应用。本篇只点名这五类，不重述每类下面的点击步骤（第 1.3 节的口径）。

## 8.3 自动下载、媒体可见性与 .nomedia

《How to configure auto-download》管的是"媒体要不要自动下载"。它对"手动下载"只给一句：

> `To manually download photos and video, tap the photo or video, tap .`

对"下载之后去哪"给一句：

> `Automatically downloaded media will show up in your gallery.`

对"不想让它进相册"给一条文件级做法：

> `To stop WhatsApp media from appearing in your gallery: Create a .nomedia file or folder .`

> `Remember, you can always delete the .nomedia file and WhatsApp media will show up in your gallery again.`

《How to stop WhatsApp from saving media to your phone》从同一套设置的另一侧写：媒体可见性默认开启（这一句带设置项名，只作中文陈述）；关掉之后可以再打开；针对单个聊天也能单独设置。它对 .nomedia 的说明更直接：

> `Please note this will hide all your WhatsApp photos from your phone's gallery.`

> `If you want to view your photos in your phone's gallery later, simply delete the .nomedia file.`

**这三件事不在同一层**：自动下载管"要不要从服务器取"，媒体可见性管"取回来要不要存进相册"，.nomedia 管"已经存进相册的要不要被系统相册索引"。官方把它们分在两页的不同段落里；本篇不把它们合并成一条"媒体保存规则"，因为合并之后会得到官方没有写过的因果。

## 8.4 视图一次、贴纸、语音与转发：四套独有说法

《How to send and open view once media and voice messages》写"视图一次"。它先说这个模式能保证什么：

> `View once media View once photos and videos won’t be saved to the recipient’s Photos or Gallery.`

> `They won't be able to forward, share, or copy them.`

> `Recipients also won’t be able to take a screenshot or screen recording of your view once media.`

紧接着又给出一条**不保证**的部分：

> `It’s still possible for someone to take a photo or video of the media, for example with a camera or another device, before it disappears.`

前三句是模式内的行为限制，第四句是模式外的现实边界；官方没有把"视图一次"写成"无法被记录"，只写成"应用内不能保存、转发、截屏"。**这四句要连起来读**，单引前三句会读成绝对保证。

《How to use stickers》写贴纸。它对"下载来的贴纸包"有两条不对称的说明：站外来源的贴纸可能搜不到（这一句带第三方来源与标注规范，只作中文陈述）；贴纸显示空白时可能是备份损坏：

> `If your stickers appear blank, you may have a corrupted backup.`

同一页还交代了头像贴纸的边界：应用内不再提供新的头像创建，已有的头像贴纸仍可继续使用（这一句前半段是界面动作，只作中文陈述）。**"贴纸显示异常"与"贴纸包下载异常"是两条不同的排查线**：一条查备份，一条查来源与标注；页面把它们放在不同段落里，本篇不合并。

《How to preview a voice message》只讲"预览一条语音消息"，与消息规格无关；它的正文全是界面路径，按第 1.3 节只点名、不引句。《How to forward media》写转发时的说明文字：

> `By default, the caption is included when you forward media to give additional information when sharing between chats.`

> `You can choose to remove the caption or replace it with your own message.`

> `Captions can't be added when forwarding multiple photos or videos.`

前两句是"可以保留也可以改"，第三句是**多条照片或影片一起转发时的例外**：不能加说明文字。**单条转发与多条转发在这一项上不同**，官方用"multiple"一词划界，本篇照写。

《How to send voice messages》写语音消息的录制与状态语音。它对下载行为有一句总起：

> `All voice messages are downloaded automatically.`

这一句说的是聊天里的语音消息；开发文档在同一页把"语音消息"与"基础音频消息"分成两个子类，后者的播放由用户触发（3.3）。**同一条音频在不同子类下有两个不同的下载口径**，附表 F 的 F4 把两句并排登记；本篇按子类分开写，不把它读成"语音消息有时要手动下载"。

这一页与 8.1 的附件页共用"语音"这个词，但一个讲聊天里的语音消息，一个讲状态更新，本篇按页面分开处理，第 9.1 节再把这一组同词摊开。

## 8.5 媒体档案：文件按什么组织

《Finding media files》回答"发过的媒体去哪找"。它先给位置：

> `If you have Internal storage, the WhatsApp folder is located in your Internal storage.`

语音消息的文件夹是隐藏的，仍可用文件浏览器进入：

> `You can still access this folder with a file explorer under /WhatsApp/Media/WhatsApp Voice Notes.`

组织方式按发送日期：

> `Files are organized by the date they were sent.`

每一份文件还带来源与时间：

> `Here you can find details about who sent the file, as well as the date and time the file was sent to the chat.`

**这一页描述的是设备本地档案，不是服务端留存**：服务端的留存规则写在媒体页（2.3），两处的时间与位置完全不同；把"文件按日期排"读成"服务端按日期保留"就跨层了。这一页也不进入对照表的七个主体列——它讲的既不是消息本体，也不是接口对象，而是用户设备上的目录结构；正文引用它，是为了把"本地"与"服务端"这条界线钉住。

# 9 横向对照：同一批页面，两套词

## 9.1 同词不同层：媒体、预览、上限、语音

把这批页面横向排开，至少四组词在页与页之间指的不是同一层。

**第一组是"媒体"。** 从《Media》这一页看起，开发文档里"媒体"有三种指代：媒体对象是文件本体（有加密、留存与标识，2.1 至 2.4）；媒体消息是承载它的消息类型（五类，3.1 至 3.5）；帮助中心则一律写成"附件"。附表 E 的 E1 行取到的是五类消息各自的定义句，E9 与 E10 两行取到的是体积上限，E20 至 E25 行取到的是对象与 URL 的时钟——同一列里，这三组句子挂在三个不同的对象上。**"媒体多大"问的是消息，"媒体活多久"问的是对象**，两者不能互换主语。

**第二组是"预览"。** 影片页写的是缩略图（E2 的影片列），帮助中心写的是"预览一条语音消息"（E2 的用户帮助列）；前者是消息在聊天里的静态封面，后者是用户打开播放器的入口。同一个词，一个是消息自带的一部分，一个是用户动作的名字。

**第三组是"上限"。** 同一张 E 表里有三个数字分属三层：Cloud API 的媒体消息上限、用户端发送或转发的影片上限、用户端文档上限（E9 与 E10 两行）。前两个在第 4 章已经并排解释过（附表 F 的 F1、F11 都是"已写进正文"的行）；文档的 2 GB 是用户端附件路径的数字，与文档消息的类型清单不在同一页，也不互相替代。

**第四组是"语音"。** 开发文档在同一页里分"语音消息"与"基础音频消息"两种子类（3.3），帮助中心另有"状态语音更新"（8.4）；三者共用一个词根，指的不是同一种消息：前两者是聊天里的消息，后者是状态。**本篇没有把三者的行为写成一条**——官方在哪一页写哪一种，本篇就在哪一节只写哪一种。

## 9.2 附表 E 的读法：五十三格有句，二百二十格是空

附表 E 把 **39** 个维度并排放在 **7** 列主体上，共 **273** 格：**53** 格取到了官方原句，**220** 格标作"官方未按这一列写该项"。七列分别是图像与贴纸页、影片页、语音页、文档页、模板与轮播页、媒体对象与投递页、用户帮助中心；前六列按人工点名的页清单取页，最后一列按站点类别取页。

空格不是"该主体没有这项规则"，而是"官方在这批登记页面里没按这一列写这一项"。三处最典型的空格：

- **用户帮助中心的媒体 ID 与 URL 时钟列**（E20 至 E25）：帮助中心写的是保存与打开，不写标识符有效期；这不是用户端"没有时限"，而是这一层不出现这个对象。
- **媒体对象与投递页的消息本体列**（E1）：这一页讲的是文件与投递，不定义消息类型；五类消息的定义在各自的消息页。
- **模板与轮播页的编码列**（E3）：模板页只写"头图可以是哪几类媒体"，不写每类媒体的编码与码率；编码规格在五类消息页与媒体页。

全表用去重后的页面 **16** 个，同一句在多个维度或多列复用的共 **1** 次，显示层截断 **15** 格。**这 39 个维度、273 个格子都是本篇自己数出来的对照结构，不是官方数字**：它们的作用是让读者知道本篇比了多少项、空了多少项，不能当成官方的规格或上限来引用。官方在这批页面里没有画过这样一张表，E 表是读者视角的并排，不是官方口径的复述。

# 10 容易读错的六处

## 10.1 上限要按层读：一条消息、一段影片、一份文档

最容易混的是三个体积数字。Cloud API 的媒体消息上限写在《Media》，用户端发送或转发的影片上限写在帮助中心，用户文档的 2 GB 写在附件页——**三者分属三个对象**：一条消息、一段影片、一份文档。附表 F 的 F1 与 F11 分别把"开发文档与帮助中心"和"帮助中心两页之间"这两组并排登记，两组都是"已写进正文"的行。把它们压成一个"WhatsApp 的媒体上限"会得到官方没有写过的数：同一个 2 GB 在文档页是附件路径的上限，在文档消息页只是类型清单之外的"其它类型"；同一个影片上限在两页又有"默认"与"所有平台"两种前提。

## 10.2 两个计时器与三个时钟

第 2 章与第 6 章各有一组时间。**同一批页面上至少有四个时间尺度**：媒体 ID 三十天、回调里的媒体 ID 七天、媒体 URL 五分钟、公网链接的发送缓存十分钟。前两个是"标识符的有效期"，第三个是"取件地址的有效期"，第四个是"平台替你取件的缓存窗口"。附表 F 的 F2 把前两个并排（同页两句，取得渠道不同），F7 把服务窗口与 TTL 并排（一个决定能不能发自由格式，一个决定已发出的消息重试多久）；F3 登记的是下载 URL 与发送链接的时间尺度，正文在 2.2 与 6.2 两节分开写，不把它们合成一个计时器。

**"五分钟、七天、三十天"不是同一把尺子上的刻度**：它们量的对象不同，起点也不同。把回调媒体 ID 的七天套到 API 媒体 ID 上，会让集成方在第十天就开始重传；反过来把三十天当成 URL 的有效期，会在第五分钟之后收到下载失败。

## 10.3 卡片数量的两个十

模板型轮播在同一页里出现"最多十张"与"二到十张"。前者是**一条已批准模板消息能扩展到多少张卡**，后者是**建模板时必须定下的数量区间**；两句说的是两个环节（附表 F 的 F6，已写进正文）。再加上同页另一句"已批准模板只能按创建时的数量发送"，三句合起来才是完整约束：创建时定死，发送时不能改。**"最多十张"不是"可以随时发十张"**，也不是"最少十张"；区间与上限各在一句里，不能互相替代。

## 10.4 两步排查与两种轮播的一致性

下载失败后的处理，官方给的是顺序而不是菜单：先重取媒体 URL，仍不解决才换访问令牌（附表 F 的 F8，正文 2.4 已按顺序引用）。**把两步写成"二选一"会误导排查方向**——官方的第一句是"try"，第二句以"if doing so doesn't resolve"开头，条件关系写在句子里。

两种轮播的一致性约束也常被读成一条：模板型要求"所有卡的组件相同"（5.4），交互式要求"按钮类型与数量跨卡一致"（5.5）。前者约束整张卡的组件集合，后者只约束按钮；同一页上并没有说交互式轮播要求"所有卡组件相同"，本篇也不替它加（附表 F 的 F9，已写进正文）。

## 10.5 语音子类与状态时长

"语音消息"在开发文档里与"基础音频消息"并列，是同一页的两个子类；前者自动下载，后者的播放由用户触发（3.3、8.4，附表 F 的 F4）。**把两句合成"语音消息的下载行为不一致"是读错了主语**：官方分的是两个子类，不是同一种消息的两种行为。

状态媒体另有一组时长：状态影片与状态语音各有自己的上限（4.3，附表 F 的 F10）。两个数字相同与否都不构成"同一规则"，因为它们分别写在两种媒体上；本篇按媒体种类分开登记，不做跨媒体的合并。

## 10.6 链接与 ID 的主次

模板媒体的链接路径不是被禁止的，而是被要求"公网可达，否则消息发不出去"；官方同时建议上传并用媒体 ID 以减少错误与公网请求（5.2，附表 F 的 F5）。**条件与建议不是互斥选项**：一句说"如果不满足会失败"，一句说"推荐这样做"，两句方向一致。把它们读成"官方不允许链接"或"链接已弃用"，都会超出原文。

把这六处放在一起看，会发现它们有同一个形状：**官方把同一类词写在不同层上，层与层之间没有一句总纲**。本篇的做法不是替官方写总纲，而是在每一处只写它所在的那一页、那一句。

# 11 结论与边界

## 11.1 这条链路的口径边界

把本篇读成一句话：**媒体在 WhatsApp 里是"一个文件对象、五种消息载体、两套计时器、三层上限"**。文件对象管加密、留存、标识与下载；五种消息载体（图像、影片、语音与基础音频、文档、贴纸）各管自己的编码与预览；两套计时器分别是标识符与取件地址的有效期；三层上限分别落在一条消息、一段影片与一份文档上。模板头图与轮播卡片是另外两层载体，它们把媒体再包进模板或消息，规则并不因为"都带媒体"就与五类消息相同。

这条链路里官方写得最细、也最容易被读错的三处是：媒体 ID 的两个有效期（2.2、7.3，附表 F 的 F2）；服务窗口与 TTL 的两个截止时间（6.1、6.4，F7）；三种轮播/卡片的数量与一致性约束（5.3 至 5.5，F6 与 F9）。三处都不是"官方自相矛盾"，而是同一个词在不同层上各有定义；本篇把每一层分开写，不把层与层之间的空白补成结论。

## 11.2 本篇的边界

本篇只写官方文档在这一批页面里写下的内容。**27** 个登记页面的正文全部取回、全部进入原句池，池内 **399** 条整句；没有一页是"全篇不引用"，也没有一页是"整页零句"。但这不等于每一句都被引用：正文引用的是一部分整句，其余句子留在原句池里，进入附表 C 的词矩阵；被附表 E 的对照格取到的那些，另在 E 表逐格登记。

范围上，本篇**不含**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法；**不含**任何可用于向非自愿收件人大量投递媒体或附件的方法；也不含代码、恶意样本或利用链构造。帮助中心里写成界面路径的内容——在哪个页签选图、怎么按住录音、贴纸包怎么排序、.nomedia 文件放在哪——本篇只点名对象与数量，不重述点击次序，读者要动手请回原页。

最后一类边界是对象层面的：本篇不判断某个编码该不该支持、不评价某个上限该不该这样定、不推断官方没写的转码策略与服务端存储位置、不把帮助中心的用户端口径与开发文档的服务端口径合并成一条"WhatsApp 的媒体规则"。这些空白登记在附表 F，正文只在它们影响读法的地方点名。

# 附表 A 官方页面登记清单与本篇引用位置（28 行）

登记口径与系列前篇一致：**凡本篇抓取的页面，一律登记**。全表 **27** 个登记位，按站点分为 Meta 开发者文档 16、WhatsApp 帮助中心 11。取回状态由脚本实测各层正文的非空白字符数得出，分档统计：已取回 27。其中**可引用 27 页、不可引用 0 页**：本轮登记页正文全部取回，没有需要标注「全篇不引用」的页面。

帮助中心页的正文取回长度按裁剪后正文计：抓取原始页含平台页签（Web／Windows／Mac／iOS／Android）与固定尾栏，这些行不计入长度，也不作为引文出处。Meta 商业帮助中心的页面正文从页面内嵌的转义 JSON 载荷里抽取，抽取残骸（单独的 u 记号与 DOM 类名）整行剔除，不做猜测修补，被剔除的行也不作为引文出处。

第 4 列「本篇引用位置」的小节号是实测的，不是事先规划的：由 _tmp/wa6_postitle.pl 扫成稿分片里的页名标记（正文每点名一次某页的官方标题就留下一枚这种内部标记，展开后成为标题）得出，写进 _tmp/w6_a8_postitle.tsv，再由 _tmp/wa6_pos.pl 拼进这一列；判据与 _tmp/wa6_cover.pl 同口径——**这一列只写标题点名的位置，所以按小节号翻过去一定能撞见这一页的官方标题**。章首（未进入任何小节的章引言）写作「第 N 章章首」；某页只在别处被整句引用而标题未点名的节不出现在这一列，那种句子按附表 C—F 的句序回查。

| 站点 | 页面标识 | 官方标题 | 本篇引用位置 | 正文状态 |
| --- | --- | --- | --- | --- |
| Meta 开发者文档 | `business_phone_numbers_media_md` | Media | 正文 1.1、2.1、10.1、附表 B（4 处）、附表 C（25 处）、附表 E（9 处）、附表 F（6 处） | 已取回（正文 8531 字符） |
| Meta 开发者文档 | `direct_send_supported_features_and_limits_md` | Supported features and limits | 正文 3.6、附表 C（2 处） | 已取回（正文 3289 字符） |
| Meta 开发者文档 | `messages_audio_messages_md` | Audio messages | 正文 3.3、附表 C（4 处）、附表 E（5 处）、附表 F（1 处） | 已取回（正文 6589 字符） |
| Meta 开发者文档 | `messages_document_messages_md` | Document messages | 正文 3.4、附表 C（1 处）、附表 E（3 处） | 已取回（正文 4936 字符） |
| Meta 开发者文档 | `messages_image_messages_md` | Image messages | 正文 3.1、附表 C（1 处）、附表 E（5 处） | 已取回（正文 4638 字符） |
| Meta 开发者文档 | `messages_interactive_media_carousel_messages_md` | Interactive media carousel messages | 正文 5.5、附表 C（2 处）、附表 E（2 处）、附表 F（1 处） | 已取回（正文 8252 字符） |
| Meta 开发者文档 | `messages_send_messages_md` | Service messages | 正文 6.1、附表 B（3 处）、附表 C（3 处）、附表 E（6 处）、附表 F（3 处） | 已取回（正文 19129 字符） |
| Meta 开发者文档 | `messages_sticker_messages_md` | Sticker messages | 正文 3.5、附表 C（3 处）、附表 E（2 处） | 已取回（正文 4288 字符） |
| Meta 开发者文档 | `messages_video_messages_md` | Video Messages | 正文 3.2、附表 C（1 处）、附表 E（4 处） | 已取回（正文 3417 字符） |
| Meta 开发者文档 | `templates_marketing_templates_media_card_carousel_templates_md` | Media card carousel templates | 正文 5.3、附表 E（4 处）、附表 F（3 处） | 已取回（正文 14832 字符） |
| Meta 开发者文档 | `templates_template_media_md` | Template media | 正文 5.1、附表 C（2 处）、附表 E（4 处）、附表 F（2 处） | 已取回（正文 3030 字符） |
| Meta 开发者文档 | `webhooks_reference_messages_audio_md` | Audio messages webhook reference | 正文 7.1、附表 D（1 处） | 已取回（正文 10346 字符） |
| Meta 开发者文档 | `webhooks_reference_messages_document_md` | Document messages webhook reference | 正文 7.1、附表 D（1 处） | 已取回（正文 10498 字符） |
| Meta 开发者文档 | `webhooks_reference_messages_image_md` | Image messages webhook reference | 正文 7.1、附表 D（1 处） | 已取回（正文 10748 字符） |
| Meta 开发者文档 | `webhooks_reference_messages_sticker_md` | Sticker messages webhook reference | 正文 7.1、附表 D（1 处） | 已取回（正文 10219 字符） |
| Meta 开发者文档 | `webhooks_reference_messages_video_md` | Video messages webhook reference | 正文 7.1、附表 D（1 处） | 已取回（正文 10514 字符） |
| WhatsApp 帮助中心 | `faq_1044960269529733` | How to preview a voice message | 正文 8.4、附表 C（2 处）、附表 E（1 处） | 已取回（正文 708 字符） |
| WhatsApp 帮助中心 | `faq_1096027074615511` | Can’t download, open, or send media files | 正文 8.2、附表 C（9 处）、附表 E（2 处） | 已取回（正文 2732 字符） |
| WhatsApp 帮助中心 | `faq_1436677150190242` | Finding media files | 正文 8.5、附表 C（1 处） | 已取回（正文 1329 字符） |
| WhatsApp 帮助中心 | `faq_366146522333492` | How to configure auto-download | 正文 8.3、附表 E（1 处） | 已取回（正文 1449 字符） |
| WhatsApp 帮助中心 | `faq_453914586839706` | How to send media, contacts, or location | 正文 8.1、附表 B（4 处）、附表 C（5 处）、附表 D（1 处）、附表 E（3 处）、附表 F（2 处） | 已取回（正文 3134 字符） |
| WhatsApp 帮助中心 | `faq_476272750957554` | How to stop WhatsApp from saving media to your phone | 正文 8.3 | 已取回（正文 1687 字符） |
| WhatsApp 帮助中心 | `faq_578442220724722` | How to send and open view once media and voice messages | 正文 8.4、附表 E（1 处） | 已取回（正文 2165 字符） |
| WhatsApp 帮助中心 | `faq_639351827594474` | How to use stickers | 正文 8.4、附表 C（1 处） | 已取回（正文 3267 字符） |
| WhatsApp 帮助中心 | `faq_657157755756612` | How to send voice messages | 正文 8.4、附表 B（1 处）、附表 F（2 处） | 已取回（正文 2184 字符） |
| WhatsApp 帮助中心 | `faq_823886715479103` | Can’t send longer videos | 正文 8.1、附表 B（3 处）、附表 E（1 处）、附表 F（2 处） | 已取回（正文 1448 字符） |
| WhatsApp 帮助中心 | `faq_982833196228708` | How to forward media | 正文 8.4 | 已取回（正文 1132 字符） |
| **合计** | **27** 个登记位 | 可引用 **27** 页／不可引用 0 页 | Meta 开发者文档 16、WhatsApp 帮助中心 11 | 不可引用页全篇不引用 |

#ROWS=27 COLS=5


# 附表 B 数字归档五档（16 行）

归档口径：**本表一行一个「数字＋它数的东西＋出处页与句序」**，数字、单位词与整句都由脚本从语料里实测取出——输入是原句池的 **399** 条整句（覆盖 **27** 个页面，池子已剔除含竖线的「正文黏表格」混合句），脚本在每句里找出所有独立数字，取该数字后面最多四个词，档位由这四个词里第一个能对上单位表的词决定；同页同数字同单位只留一行（共去重 **5** 行）。全表 **15** 行，分布在 **5** 个页面上，去重后的「数字＋单位」组合 **11** 组，单位词 **8** 种。五档分布：三、时长与滚动窗口 8／二、验证要素、文本与文件长度规格 7。

三条不成文规矩在这一表里最要紧：**同一个数字在两个页面各写一处，就各登记一行**，不合并成区间；**官方写成区间、多档或带限定词的照原样登记**（`up to`、`about`、`approximately`、`in some countries` 一类措辞原样保留），不取中值、不去掉约数；**档位与性质由脚本按关键词判定，性质判不出来就中止出表**，所以本表没有「其它」这一档。

本表也写清了自己漏掉了什么：池子里 **355** 句整句不含数字，脚本共扫出 **22** 个「数字后面四个词都对不上任何一档」的实例，全部连同它所在的整句写进 `_tmp/w6_a8_bdrop.tsv` 供复核，不进表——这些实例的共同点是紧跟数字的词不在任何一档的单位表里，本表不为它们硬造档位，**漏掉的判定权在读者手里，不在这张表里**。另有 **0** 个可引用页面在池子里一句都没有，本表因此整页缺席，它们在附表 A 里已逐页点名。

官方原句超过 118 字符按整词截断并以省略号结尾，共 **2** 行截断；截断只发生在表格里，进表的每一句都先用未截断的整句逐字回表到`_tmp/w6_a8s/` 的对应页面，失败 **0** 句（回表与建池共用同一套规范化，所以这里一旦非零就是池子与抓取件不同步，脚本直接中止不出表）。性质判定为 **3** 种：上限（官方写成 up to／maximum 一类） 9／缓存与复用窗口 2／自动解除或自动生效的时限 4。

| 档 | 数字 | 计数单位（官方原文） | 官方原句 | 性质 | 出处页 |
|---|---|---|---|---|---|
| 三、时长与滚动窗口 | **30** | days | 「Media IDs returned by the API expire after 30 days.」 | 自动解除或自动生效的时限 | `business_phone_numbers_media_md`｜第 14 句 |
| 三、时长与滚动窗口 | **7** | days | 「Media IDs in webhooks expire after 7 days.」 | 自动解除或自动生效的时限 | `business_phone_numbers_media_md`｜第 15 句 |
| 三、时长与滚动窗口 | **5** | minutes | 「Media URLs **expire after 5 minutes**, after which you must query the ID again to get a new URL.」 | 自动解除或自动生效的时限 | `business_phone_numbers_media_md`｜第 42 句 |
| 二、验证要素、文本与文件长度规格 | **100** | mb | 「The maximum supported file size for media messages on Cloud API is 100MB.」 | 上限（官方写成 up to／maximum 一类） | `business_phone_numbers_media_md`｜第 107 句 |
| 二、验证要素、文本与文件长度规格 | **2** | gb | 「To attach other media, tap , then tap: Document to select a document of up to 2 GB from your phone.」 | 上限（官方写成 up to／maximum 一类） | `faq_453914586839706`｜第 7 句 |
| 二、验证要素、文本与文件长度规格 | **100** | mb | 「For users with a faster internet connection, the default video size limit is 100MB and 720p resolution.」 | 上限（官方写成 up to／maximum 一类） | `faq_453914586839706`｜第 21 句 |
| 二、验证要素、文本与文件长度规格 | **64** | mb | 「For users with a slower internet connection, the default video size limit is 64MB and 480p resolution.」 | 上限（官方写成 up to／maximum 一类） | `faq_453914586839706`｜第 22 句 |
| 三、时长与滚动窗口 | **90** | seconds | 「Status videos have a maximum duration of 90 seconds.」 | 上限（官方写成 up to／maximum 一类） | `faq_453914586839706`｜第 23 句 |
| 三、时长与滚动窗口 | **30** | second | 「Record and send a voice status update You can record and share up to 30-second voice updates.」 | 上限（官方写成 up to／maximum 一类） | `faq_657157755756612`｜第 21 句 |
| 二、验证要素、文本与文件长度规格 | **64** | mb | 「The maximum file size for videos sent or forwarded through WhatsApp is 64MB on all platforms.」 | 上限（官方写成 up to／maximum 一类） | `faq_823886715479103`｜第 4 句 |
| 二、验证要素、文本与文件长度规格 | **100** | mb | 「For users with a faster internet connection, the default video size limit is 100MB and 720p resolution.」 | 上限（官方写成 up to／maximum 一类） | `faq_823886715479103`｜第 5 句 |
| 二、验证要素、文本与文件长度规格 | **2** | gb | 「The maximum allowed document size is 2 GB.」 | 上限（官方写成 up to／maximum 一类） | `faq_823886715479103`｜第 11 句 |
| 三、时长与滚动窗口 | **24** | hours | 「If the user messages or calls you again before the timer expires, the timer resets to 24 hours.」 | 自动解除或自动生效的时限 | `messages_send_messages_md`｜第 11 句 |
| 三、时长与滚动窗口 | **10** | minutes | 「If you are using a link (`link`) to a media asset on your server (as opposed to the ID (`id`) of an asset you have…」 | 缓存与复用窗口 | `messages_send_messages_md`｜第 129 句 |
| 三、时长与滚动窗口 | **10** | minute | 「If you don't want the cached asset reused in a subsequent message within the 10 minute time period, append a random…」 | 缓存与复用窗口 | `messages_send_messages_md`｜第 131 句 |
| **合计** | **15** 行 | 单位词 **8** 种 | 去重后数字＋单位 **11** 组，覆盖 **5** 页 | 性质 **3** 种 | 第四档＝日期与版本节点 |

#ROWS=15 COLS=6


# 附表 C 官方用词与写下它的站点类别（62 行）

取格口径：脚本按第一、二列的 **52** 个词（组）（分属 8 个主题组：A　媒体类型本体 10／B　媒体对象与标识 7／C　上传与下载 5／D　体积与格式规格 10／E　生命周期与留存 6／F　加密与凭证 4／G　模板与卡片 5／H　事件与错误 5）逐句扫过原句池的 **399** 条整句（覆盖 **27** 个页面），每命中一次就落进「词 × 站点类别」的格子；**一行一个非空格**，全表 **61** 行，各格命中句次相加是 **549** 次，而去重后真正被任一词命中的句子是 **259** 句（占全池 64.9%）。同一句可以落进多格：本轮一句落进最多格子的是 `messages_sticker_messages_md` 池内序号 33，同时命中 7 个词（组）——「expire（过期）」、「file（文件）」、「format（格式）」、「max size（最大体积）」、「request（请求）」、「size（体积）」、「sticker（贴纸）」；全池有 148 句同时落进两个以上的格子。所以句次合计 **549** 大于去重句数 **259**，这两个数各自回答「这一格有多少句」与「整池有多少句被扫中」，不矛盾。

词表本身也是这一篇的一部分：它写在 `_tmp/w6_a8_verb.tsv` 里，脚本不内置任何选题词，换一篇只换词表、判定规则不变。**正则由脚本逐条编译，编译不过就中止出表**，不存在「写错的正则悄悄扫出零句然后当成官方没写过」。

代表句一列取该格在池子里最先出现的一句，取句规则写死在脚本的遍历顺序里，**不是按哪句更清楚来挑**。「格内有句写明数值」只回答这一格有没有句子自带天数、小时、位数、百分比或规模数——有则记「有」，没有就记「无」，不在这里把别的页面或别的站的数字补进来；跨站对撞属于附表 F。

词零命中的情况：52 个词（组）里 **7** 个在整池一句都没出现（`PNG`、`attachment（附件）`、`retrieve（取回）`、`MB`、`retention（留存）`、`hash（哈希）`、`Authorization（授权头）`）。零命中不等于官方没有这一项内容，只说明本篇登记的这批页面没有用这个词写过。

本表的英文原句全部逐字回表：399 条整句每一条都在 `_tmp/w6_a8s/` 的对应页面正文里按规范化后的子串核到，失败 **0** 句（判定与建池用同一套实体还原与空白折叠，见 `_tmp/wa6_lib.pl` 的 ck）。官方原句超过 130 字符按整词截断并以省略号结尾，共 **11** 行截断；截断只发生在表格里，回表核验用的是未截断的整句。各站点命中句次合计：Meta 开发者文档 314／WhatsApp 帮助中心 235／Meta 商业帮助中心 0／法律与政策页 0。

| 主题组 | 官方词（组） | 写下它的站点类别 | 命中句次 | 命中页数 | 代表句（该格池内第一句） | 格内有句写明数值 | 出处页｜句序 |
|---|---|---|---|---|---|---|---|
| A　媒体类型本体 | image（图片） | Meta 开发者文档 | **26** | 12 | 「You can send a static WebP image in an image message, and it is transcoded to JPEG.」 | 无 | `business_phone_numbers_media_md`｜第 89 句 |
| A　媒体类型本体 | image（图片） | WhatsApp 帮助中心 | **6** | 4 | 「Scroll to find the image you're looking for.」 | 有 | `faq_1436677150190242`｜第 13 句 |
| A　媒体类型本体 | JPEG | Meta 开发者文档 | **2** | 2 | 「You can send a static WebP image in an image message, and it is transcoded to JPEG.」 | 无 | `business_phone_numbers_media_md`｜第 89 句 |
| A　媒体类型本体 | video（视频） | Meta 开发者文档 | **10** | 6 | 「Only H.264 video codec and AAC audio codec supported.」 | 无 | `business_phone_numbers_media_md`｜第 99 句 |
| A　媒体类型本体 | video（视频） | WhatsApp 帮助中心 | **33** | 7 | 「Can't hear voice messages, status audio, call audio, or videos Can’t record voice messages Seeing blurry photos Can’t send longer…」 | 有 | `faq_1096027074615511`｜第 2 句 |
| A　媒体类型本体 | audio（音频） | Meta 开发者文档 | **14** | 4 | 「Only H.264 video codec and AAC audio codec supported.」 | 无 | `business_phone_numbers_media_md`｜第 99 句 |
| A　媒体类型本体 | audio（音频） | WhatsApp 帮助中心 | **9** | 4 | 「Can't hear voice messages, status audio, call audio, or videos Can’t record voice messages Seeing blurry photos Can’t send longer…」 | 无 | `faq_1096027074615511`｜第 2 句 |
| A　媒体类型本体 | voice message（语音消息） | Meta 开发者文档 | **8** | 1 | 「You can use Cloud API to send voice messages and basic audio messages.」 | 无 | `messages_audio_messages_md`｜第 4 句 |
| A　媒体类型本体 | voice message（语音消息） | WhatsApp 帮助中心 | **17** | 6 | 「Preview a voice message Open an individual or group chat.」 | 无 | `faq_1044960269529733`｜第 3 句 |
| A　媒体类型本体 | document（文档） | Meta 开发者文档 | **7** | 4 | 「Some of the API requests described in this document require a media ID.」 | 无 | `business_phone_numbers_media_md`｜第 12 句 |
| A　媒体类型本体 | document（文档） | WhatsApp 帮助中心 | **9** | 3 | 「Without this permission, you won’t be able to download, save, or send photos, videos, and documents.」 | 有 | `faq_1096027074615511`｜第 18 句 |
| A　媒体类型本体 | PDF | Meta 开发者文档 | **1** | 1 | 「Example request to send a PDF in a document message with a caption to a WhatsApp user.」 | 无 | `messages_document_messages_md`｜第 38 句 |
| A　媒体类型本体 | PDF | WhatsApp 帮助中心 | **2** | 1 | 「Can’t open PDF If you're unable to open a downloaded PDF file on your phone, try the following: Make sure you have a PDF reader…」 | 无 | `faq_1096027074615511`｜第 44 句 |
| A　媒体类型本体 | sticker（贴纸） | Meta 开发者文档 | **5** | 2 | 「Sticker messages display animated or static sticker images in a WhatsApp message.」 | 无 | `messages_sticker_messages_md`｜第 3 句 |
| A　媒体类型本体 | sticker（贴纸） | WhatsApp 帮助中心 | **30** | 1 | 「To view and use stickers, open a chat, then: Tap .」 | 无 | `faq_639351827594474`｜第 3 句 |
| A　媒体类型本体 | GIF | Meta 开发者文档 | **1** | 1 | 「A media header allows you to add an image, video, GIF, or document at the top of your WhatsApp template message.」 | 无 | `templates_template_media_md`｜第 3 句 |
| B　媒体对象与标识 | media（媒体总称） | Meta 开发者文档 | **32** | 6 | 「Some of the API requests described in this document require a media ID.」 | 有 | `business_phone_numbers_media_md`｜第 12 句 |
| B　媒体对象与标识 | media（媒体总称） | WhatsApp 帮助中心 | **47** | 8 | 「If you can’t download, open, or send media files, the most common reason why is a weak internet connection.」 | 有 | `faq_1096027074615511`｜第 5 句 |
| B　媒体对象与标识 | media ID（媒体标识） | Meta 开发者文档 | **5** | 3 | 「Some of the API requests described in this document require a media ID.」 | 有 | `business_phone_numbers_media_md`｜第 12 句 |
| B　媒体对象与标识 | media URL（媒体链接） | Meta 开发者文档 | **3** | 1 | 「Media URLs expire after 5 minutes, after which you must query the ID again to get a new URL.」 | 有 | `business_phone_numbers_media_md`｜第 42 句 |
| B　媒体对象与标识 | media asset（媒体资产） | Meta 开发者文档 | **5** | 3 | 「You can then use the URL with your access token to [download the media asset](#download-media).」 | 有 | `business_phone_numbers_media_md`｜第 40 句 |
| B　媒体对象与标识 | media object（媒体对象） | Meta 开发者文档 | **1** | 1 | 「Set the `type` property to `template` and use the template property to define your template object and its media object.」 | 无 | `templates_template_media_md`｜第 11 句 |
| B　媒体对象与标识 | file（文件） | Meta 开发者文档 | **17** | 6 | 「All media files sent through this API are encrypted and persist for 30 days, unless they are deleted earlier.」 | 有 | `business_phone_numbers_media_md`｜第 19 句 |
| B　媒体对象与标识 | file（文件） | WhatsApp 帮助中心 | **25** | 6 | 「If you can’t download, open, or send media files, the most common reason why is a weak internet connection.」 | 无 | `faq_1096027074615511`｜第 5 句 |
| C　上传与下载 | upload（上传） | Meta 开发者文档 | **5** | 3 | 「Example request to send an audio message using an uploaded media ID.」 | 有 | `messages_audio_messages_md`｜第 54 句 |
| C　上传与下载 | upload（上传） | WhatsApp 帮助中心 | **3** | 2 | 「The quality of media uploaded to WhatsApp might be lower than the original version because media files are usually compressed…」 | 无 | `faq_453914586839706`｜第 19 句 |
| C　上传与下载 | download（下载） | Meta 开发者文档 | **9** | 3 | 「You can then use the URL with your access token to [download the media asset](#download-media).」 | 无 | `business_phone_numbers_media_md`｜第 40 句 |
| C　上传与下载 | download（下载） | WhatsApp 帮助中心 | **25** | 6 | 「If you can’t download, open, or send media files, the most common reason why is a weak internet connection.」 | 无 | `faq_1096027074615511`｜第 5 句 |
| C　上传与下载 | fetch（抓取） | Meta 开发者文档 | **1** | 1 | 「The Cloud API treats this as a new asset, fetches it from your server, and caches it for 10 minutes.」 | 有 | `messages_send_messages_md`｜第 132 句 |
| C　上传与下载 | request（请求） | Meta 开发者文档 | **26** | 10 | 「Some of the API requests described in this document require a media ID.」 | 有 | `business_phone_numbers_media_md`｜第 12 句 |
| D　体积与格式规格 | size（体积） | Meta 开发者文档 | **6** | 3 | 「The maximum supported file size for media messages on Cloud API is 100MB.」 | 无 | `business_phone_numbers_media_md`｜第 107 句 |
| D　体积与格式规格 | size（体积） | WhatsApp 帮助中心 | **7** | 2 | 「For users with a faster internet connection, the default video size limit is 100MB and 720p resolution.」 | 无 | `faq_453914586839706`｜第 21 句 |
| D　体积与格式规格 | size limit（体积上限） | Meta 开发者文档 | **1** | 1 | 「WhatsApp supports the following sticker file formats and size limits for sticker messages.」 | 无 | `messages_sticker_messages_md`｜第 21 句 |
| D　体积与格式规格 | size limit（体积上限） | WhatsApp 帮助中心 | **4** | 2 | 「For users with a faster internet connection, the default video size limit is 100MB and 720p resolution.」 | 无 | `faq_453914586839706`｜第 21 句 |
| D　体积与格式规格 | max size（最大体积） | Meta 开发者文档 | **2** | 2 | 「A request fails if the `<MEDIA_ID>` is invalid or has expired, if the image format isn't supported, or if the image exceeds the…」 | 无 | `messages_image_messages_md`｜第 39 句 |
| D　体积与格式规格 | GB | WhatsApp 帮助中心 | **3** | 2 | 「To attach other media, tap , then tap: Document to select a document of up to 2 GB from your phone.」 | 有 | `faq_453914586839706`｜第 7 句 |
| D　体积与格式规格 | resolution（分辨率） | WhatsApp 帮助中心 | **5** | 2 | 「For users with a faster internet connection, the default video size limit is 100MB and 720p resolution.」 | 无 | `faq_453914586839706`｜第 21 句 |
| D　体积与格式规格 | format（格式） | Meta 开发者文档 | **5** | 4 | 「> Note. Any message format or feature not listed here is not yet supported.」 | 无 | `direct_send_supported_features_and_limits_md`｜第 10 句 |
| D　体积与格式规格 | format（格式） | WhatsApp 帮助中心 | **4** | 3 | 「Supported video formats include 3GP, AVI, FLV, MKV, MOV, and MP4.」 | 无 | `faq_1096027074615511`｜第 4 句 |
| D　体积与格式规格 | extension（扩展名） | Meta 开发者文档 | **1** | 1 | 「If you encounter an error when sending a media file, verify that your audio file's MIME type matches its extension and is a…」 | 无 | `messages_audio_messages_md`｜第 51 句 |
| D　体积与格式规格 | MIME type（媒体类型） | Meta 开发者文档 | **3** | 2 | 「Response headers contain a content-type header to indicate the MIME type of returned data.」 | 无 | `business_phone_numbers_media_md`｜第 57 句 |
| D　体积与格式规格 | codec（编码） | Meta 开发者文档 | **6** | 3 | 「Only H.264 video codec and AAC audio codec supported.」 | 无 | `business_phone_numbers_media_md`｜第 99 句 |
| E　生命周期与留存 | expire（过期） | Meta 开发者文档 | **6** | 4 | 「Media IDs returned by the API expire after 30 days.」 | 有 | `business_phone_numbers_media_md`｜第 14 句 |
| E　生命周期与留存 | 30 days（三十天） | Meta 开发者文档 | **2** | 1 | 「Media IDs returned by the API expire after 30 days.」 | 有 | `business_phone_numbers_media_md`｜第 14 句 |
| E　生命周期与留存 | 7 days（七天） | Meta 开发者文档 | **1** | 1 | 「Media IDs in webhooks expire after 7 days.」 | 有 | `business_phone_numbers_media_md`｜第 15 句 |
| E　生命周期与留存 | delete（删除） | Meta 开发者文档 | **1** | 1 | 「All media files sent through this API are encrypted and persist for 30 days, unless they are deleted earlier.」 | 有 | `business_phone_numbers_media_md`｜第 19 句 |
| E　生命周期与留存 | delete（删除） | WhatsApp 帮助中心 | **5** | 5 | 「You can also tap, hold, and drag any part of the recording to play it from that timestamp Tap to delete the voice message, tap to…」 | 无 | `faq_1044960269529733`｜第 8 句 |
| E　生命周期与留存 | cache（缓存） | Meta 开发者文档 | **4** | 1 | 「If you are using a link (`link`) to a media asset on your server (as opposed to the ID (`id`) of an asset you have uploaded to…」 | 有 | `messages_send_messages_md`｜第 129 句 |
| F　加密与凭证 | encrypt（加密） | Meta 开发者文档 | **1** | 1 | 「All media files sent through this API are encrypted and persist for 30 days, unless they are deleted earlier.」 | 有 | `business_phone_numbers_media_md`｜第 19 句 |
| F　加密与凭证 | access token（访问令牌） | Meta 开发者文档 | **4** | 2 | 「You can then use the URL with your access token to [download the media asset](#download-media).」 | 无 | `business_phone_numbers_media_md`｜第 40 句 |
| G　模板与卡片 | media card（媒体卡片） | Meta 开发者文档 | **7** | 2 | 「Interactive media carousel messages display a set of horizontally scrollable media cards.」 | 有 | `messages_interactive_media_carousel_messages_md`｜第 3 句 |
| G　模板与卡片 | carousel（轮播） | Meta 开发者文档 | **9** | 2 | 「Interactive media carousel messages display a set of horizontally scrollable media cards.」 | 有 | `messages_interactive_media_carousel_messages_md`｜第 3 句 |
| G　模板与卡片 | header（模板头部） | Meta 开发者文档 | **10** | 4 | 「Response headers contain a content-type header to indicate the MIME type of returned data.」 | 无 | `business_phone_numbers_media_md`｜第 57 句 |
| G　模板与卡片 | template（模板） | Meta 开发者文档 | **20** | 4 | 「Direct Send aligns with the message-length limits for business-initiated template messages:」 | 有 | `direct_send_supported_features_and_limits_md`｜第 17 句 |
| G　模板与卡片 | thumbnail（缩略图） | Meta 开发者文档 | **1** | 1 | 「Video messages display a thumbnail preview of a video image with an optional caption.」 | 无 | `messages_video_messages_md`｜第 3 句 |
| H　事件与错误 | webhook | Meta 开发者文档 | **15** | 8 | 「Media IDs in webhooks expire after 7 days.」 | 有 | `business_phone_numbers_media_md`｜第 15 句 |
| H　事件与错误 | 404 | Meta 开发者文档 | **1** | 1 | 「If the download attempt fails, you will receive a `404 Not Found` response code.」 | 无 | `business_phone_numbers_media_md`｜第 59 句 |
| H　事件与错误 | error（错误） | Meta 开发者文档 | **7** | 5 | 「The most common errors associated with audio files are mismatched MIME types (MIME type doesn't match the file type indicated by…」 | 无 | `messages_audio_messages_md`｜第 50 句 |
| H　事件与错误 | error（错误） | WhatsApp 帮助中心 | **1** | 1 | 「If you see the error message “This file format is not supported” , check to make sure that you’re using the most updated version…」 | 无 | `faq_1096027074615511`｜第 11 句 |
| H　事件与错误 | response（响应） | Meta 开发者文档 | **11** | 6 | 「A successful response includes an object with a media URL.」 | 无 | `business_phone_numbers_media_md`｜第 45 句 |
| H　事件与错误 | payload（载荷） | Meta 开发者文档 | **12** | 7 | 「The post body varies depending on the [type of message](#message-types) you want to send, but the payload uses the following…」 | 有 | `messages_send_messages_md`｜第 39 句 |
| **合计** | **52** 个词（组）× **2** 类站点 | 非空格 **61** 个 | 句次 **549** | 一句可落多格 | 代表句按池内顺序取第一句 | 零命中词 **7** 个 | 回表失败 **0** 句 |

#ROWS=61 COLS=8


# 附表 D 官方写明的取值域全表（7 行）

取组口径：脚本扫过 **27** 个已登记页面的每一行原文，配置了 10 个取值域标记，本轮真正在这些页面里出现过的有 **2** 个（`Values can be` 5 行／`one of the following` 4 行，合计 **9** 行），另有 **8** 个（Supported values are／Possible values／Can be one of／must be one of／The following values are supported／Valid values are／Options are／Enum Values）在扫过的每一行原文里零命中——这几串字符只是脚本配置表里的名字，本轮官方页面没有一处用过这种写法，所以本篇不给它们加反引号、也不把它们当作任何官方句子的引文。取到值来的页面 **6** 个（站点类别：Meta 开发者文档／WhatsApp 帮助中心）。按标记所在行的形态分档取值：自动·参数表说明格 5／自动·整句逗号清单 1。全表 **6** 组、共 **16** 个取值、去重后的取值名 **8** 个。其中 **0** 组的取值是不带反引号的纯文本子项——反引号是接口文档的排版，帮助中心同样用「one of the following」加子项写成正式取值域，这一档整项照收、只丢超过 80 字符的说明句；同页同字段同取值的重复写法（帮助中心按平台页签各写一遍）合并为一行，共合并 **0** 处。取值本身带空格的共 **0** 个（如 `LEGAL NAME NOT FOUND IN DOCUMENTS`、`No longer needed`），这一批只能按「反引号紧跟破折号说明」的位置认出，脚本对这种写法额外兜一遍：同一格里见到两个以上这种位置才收，并且只在这一档放开取值不能带空格的限制——接口文档的枚举里确有带空格的大写取值，卡成单词会漏掉整组。

为什么这一表不走原句池：官方把取值域写在接口参数表的说明格里、缩进的子项列表里、整句内联的清单里，这些行要么带竖线、要么是列表行，本来就被池子的入池条件挡在外面。所以 D 的核验独立于 C：每一组把它自己那一行原文、缩进子项式还把每个取值所在的那一行原文，逐字回表到 `_tmp/w6_a8s/` 的对应页面，失败 **0** 行。

落空登记：扫到标记但取不到两个以上取值的共 **3** 处，原因由脚本按同一行的证据判，不由人挑：标记句下面紧跟的不是子项列表（官方把选择写在正文段落里，没写成清单） 3。**扫到标记不等于官方写明了取值域**，这几处不算进表，但算进这一句，让读者知道漏了多少、为什么漏。这 **3** 处分布在 **2** 个页面，每一处的页面标识、原文行号、判定档位、命中的标记、落空判据与该行未截断的原文全部逐条写进 `_tmp/w6_a8_dmiss.tsv`（与上面的次数由脚本断言相等，不是"约"），可逐条回原文核对。

单组取值上限设为 **24** 个：实测最多的一组有 **6** 个取值，没有一组触及上限，所以本表没有因上限而丢过取值。

取值列表的截断按整取值进行（顿号相连的列表没有空格，按词截会把一整组截到只剩一项）：单元格 **400** 字符内装得下的原样列出，装不下的整项去掉并在行内写明「另有 N 个取值未列出」。本轮实测 **0** 组触及这一上限、被去掉的取值 **0** 个，也就是说表里列出的取值就是脚本收到的全部 **16** 个，没有一组需要读者回原文补齐。

| 组别 | 页面标识 | 行号 | 字段／属性 | 官方标记 | 取值个数 | 官方写明的取值 |
|---|---|---|---|---|---|---|
| 自动·整句逗号清单 | `faq_453914586839706` | 81 | `(句首未写字段名)` | one of the following | **6** | `3GP`、`AVI`、`FLV`、`MKV`、`MOV`、`MP4` |
| 自动·参数表说明格 | `webhooks_reference_messages_audio_md` | 83 | `<AD_MEDIA_TYPE>` | Values can be | **2** | `image`、`video` |
| 自动·参数表说明格 | `webhooks_reference_messages_document_md` | 84 | `<AD_MEDIA_TYPE>` | Values can be | **2** | `image`、`video` |
| 自动·参数表说明格 | `webhooks_reference_messages_image_md` | 90 | `<AD_MEDIA_TYPE>` | Values can be | **2** | `image`、`video` |
| 自动·参数表说明格 | `webhooks_reference_messages_sticker_md` | 83 | `<AD_MEDIA_TYPE>` | Values can be | **2** | `image`、`video` |
| 自动·参数表说明格 | `webhooks_reference_messages_video_md` | 89 | `<AD_MEDIA_TYPE>` | Values can be | **2** | `image`、`video` |
| **合计** | **6** 页 | 行号为原文行序 | 字段名取自官方写法 | 标记 2 种 | 取值 **16** 个 | 去重取值名 **8** 个 |

#ROWS=6 COLS=7


# 附表 E 同一组维度在本轴各主体上的逐列对照（40 行）

取格口径：本表 **39** 个对照维度 × **7** 列 = **273** 格，其中 **53** 格取到了官方原句、**220** 格标作「官方未按这一列写该项」。每一格的取法都是「页面标识＋一条正则」交给脚本在原句池里取句，在该列的页面范围内命中 0 句或命中多句都会让生成脚本中止，所以表里没有一格是人工挑出来的。原句池共 **399** 句（已由附表 B 逐字回表），本表用去重后的页面 **16** 个；同一句在多个维度或多列复用的共 **1** 次，复用时页标识与句序不变。

列的定义写在 `_tmp/w6_a8_ecols.tsv` 里，每列要么按站点类别取页（用户帮助中心＝site WhatsApp 帮助中心），要么按人工点名的页清单取页（图像与贴纸页＝2 页／影片页＝1 页／语音页＝1 页／文档页＝1 页／模板与轮播页＝3 页／媒体对象与投递页＝2 页）。空格的含义只有一个：官方在这批登记页面里没按这一列写这一项，**不代表该主体没有这项规则**。

官方原句超过 112 字符按整词截断并以省略号结尾，共 **15** 格截断；截断只发生在显示层，取句与逐字回表用的是未截断的整句。各列取到句子的格数：图像与贴纸页 7／影片页 4／语音页 5／文档页 3／模板与轮播页 10／媒体对象与投递页 15／用户帮助中心 9。

| 编号 | 对照维度 | 图像与贴纸页 | 影片页 | 语音页 | 文档页 | 模板与轮播页 | 媒体对象与投递页 | 用户帮助中心 |
|---|---|---|---|---|---|---|---|---|
| E1   | 消息本体的官方定义 | 「Image messages are messages that display a single image and an optional caption.」（`messages_image_messages_md`｜第 3 句） | 「Video messages display a thumbnail preview of a video image with an optional caption.」（`messages_video_messages_md`｜第 3 句） | 「You can use Cloud API to send voice messages and basic audio messages.」（`messages_audio_messages_md`｜第 4 句） | 「Document messages are messages that display a document icon, linked to a document, that a WhatsApp user can tap…」（`messages_document_messages_md`｜第 3 句） | 「Interactive media carousel messages display a set of horizontally scrollable media cards.」（`messages_interactive_media_carousel_messages_md`｜第 3 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E2   | 手机端的预览与图标 | 官方未按这一列写该项 | 「When the WhatsApp user taps the preview, it loads the video and displays it to the user.」（`messages_video_messages_md`｜第 4 句） | 「Basic audio messages display a download icon and a music icon.」（`messages_audio_messages_md`｜第 18 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Preview a voice message Open an individual or group chat.」（`faq_1044960269529733`｜第 3 句） |
| E3   | 编码与容器格式 | 「WhatsApp supports the following sticker file formats and size limits for sticker messages.」（`messages_sticker_messages_md`｜第 21 句） | 「Only H.264 video codec and AAC audio codec supported.」（`messages_video_messages_md`｜第 23 句） | 「Voice messages must be Ogg files encoded with the **OPUS** codec.」（`messages_audio_messages_md`｜第 31 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Only H.264 video codec and AAC audio codec supported.」（`business_phone_numbers_media_md`｜第 99 句） | 「Supported video formats include 3GP, AVI, FLV, MKV, MOV, and MP4.」（`faq_1096027074615511`｜第 4 句） |
| E4   | 静态 WebP 图的转码 | 「You can send a static WebP image in an image message, and it is transcoded to JPEG.」（`messages_image_messages_md`｜第 29 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E5   | 图像与贴纸的拒绝条件 | 「Animated WebP images, files that cannot be decoded as valid WebP, and images over 4,096 pixels on an edge are…」（`messages_image_messages_md`｜第 30 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E6   | 音频流的条数约束 | 官方未按这一列写该项 | 「Single audio stream or no audio stream only.」（`messages_video_messages_md`｜第 24 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Single audio stream or no audio stream only.」（`business_phone_numbers_media_md`｜第 100 句） | 官方未按这一列写该项 |
| E7   | 文档类型的支持边界 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Only the above listed document types are officially supported and guaranteed to display correctly in the…」（`messages_document_messages_md`｜第 35 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 「If you see the error message “This file format is not supported” , check to make sure that you’re using the…」（`faq_1096027074615511`｜第 11 句） |
| E8   | 未列入文档类型的处理 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Other file types may be sent via the API, but they are not supported and may not be handled as expected.」（`messages_document_messages_md`｜第 36 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E9   | 单条消息的体积上限 | 「A request fails if the `<MEDIA_ID>` is invalid or has expired, if the image format isn't supported, or if the…」（`messages_image_messages_md`｜第 39 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「The maximum supported file size for media messages on Cloud API is 100MB.」（`business_phone_numbers_media_md`｜第 107 句） | 「The maximum file size for videos sent or forwarded through WhatsApp is 64MB on all platforms.」（`faq_823886715479103`｜第 4 句） |
| E10  | 用户端文档体积上限 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「For documents, the maximum file size allowed is 2 GB.」（`faq_453914586839706`｜第 25 句） |
| E11  | 模板头图可承载的媒体类别 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「A media header allows you to add an image, video, GIF, or document at the top of your WhatsApp template message.」（`templates_template_media_md`｜第 3 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E12  | 模板媒体的上载方式 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Before creating the template, you must upload your media file using the Resumable Upload API.」（`templates_template_media_md`｜第 4 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E13  | 模板媒体的链接要求 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「If you're using link, your asset must be on a publicly accessible server or the message will fail to send.」（`templates_template_media_md`｜第 13 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E14  | 模板媒体用 ID 还是链接 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「To reduce the likelihood of errors and avoid unnecessary requests to your public server, Meta recommends that…」（`templates_template_media_md`｜第 14 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E15  | 轮播卡片数量在建模板时固定 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Define the exact number of product cards (minimum 2, maximum 10) when you create the template.」（`templates_marketing_templates_media_card_carousel_templates_md`｜第 15 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E16  | 已批准模板只能按原数量发送 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「An approved template can only be used to send the same number of cards as defined during its creation.」（`templates_marketing_templates_media_card_carousel_templates_md`｜第 16 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E17  | 轮播卡片的组件一致性 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「All cards defined on a template must have the same components.」（`templates_marketing_templates_media_card_carousel_templates_md`｜第 10 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E18  | 按钮类型与数量跨卡一致 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Button types and numbers must match across all cards (for example, if you define a card with 2 quick-reply…」（`messages_interactive_media_carousel_messages_md`｜第 14 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E19  | 卡片正文的一致性 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「If any card in the carousel includes a card body text, then all cards must include a card body text to ensure…」（`templates_marketing_templates_media_card_carousel_templates_md`｜第 17 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E20  | 媒体 ID 的有效期 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Media IDs returned by the API expire after 30 days.」（`business_phone_numbers_media_md`｜第 14 句） | 官方未按这一列写该项 |
| E21  | 回调里的媒体 ID 有效期 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Media IDs in webhooks expire after 7 days.」（`business_phone_numbers_media_md`｜第 15 句） | 官方未按这一列写该项 |
| E22  | 媒体 URL 的有效期 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Media URLs **expire after 5 minutes**, after which you must query the ID again to get a new URL.」（`business_phone_numbers_media_md`｜第 42 句） | 官方未按这一列写该项 |
| E23  | 下载失败后的重取路径 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「In that case, try to [get a new media URL](#get-media-url) and download it again.」（`business_phone_numbers_media_md`｜第 60 句） | 官方未按这一列写该项 |
| E24  | 换访问令牌后重试 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「If doing so doesn't resolve the issue, renew your access token and attempt to download the media asset again.」（`business_phone_numbers_media_md`｜第 61 句） | 官方未按这一列写该项 |
| E25  | 下载媒体的凭证 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Include your access token in the request. **If you omit your token, the request will fail.**」（`business_phone_numbers_media_md`｜第 54 句） | 官方未按这一列写该项 |
| E26  | 图片请求失败的判定 | 「A request fails if the `<MEDIA_ID>` is invalid or has expired, if the image format isn't supported, or if the…」（`messages_image_messages_md`｜第 39 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E27  | 贴纸请求失败的判定 | 「A request fails if the `<MEDIA_ID>` is invalid or has expired, if the sticker isn't a supported WebP type, or…」（`messages_sticker_messages_md`｜第 33 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E28  | 链接媒体的缓存窗口 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「If you are using a link (`link`) to a media asset on your server (as opposed to the ID (`id`) of an asset you…」（`messages_send_messages_md`｜第 129 句） | 官方未按这一列写该项 |
| E29  | 同链接复用缓存 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「The cached asset is reused in subsequent send message requests if the link in subsequent payloads is the same…」（`messages_send_messages_md`｜第 130 句） | 官方未按这一列写该项 |
| E30  | 绕开缓存的随机查询串 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「If you don't want the cached asset reused in a subsequent message within the 10 minute time period, append a…」（`messages_send_messages_md`｜第 131 句） | 官方未按这一列写该项 |
| E31  | 语音转写的触发设置 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「If the user sets voice message transcripts to **Automatic**, the message includes a text transcription.」（`messages_audio_messages_md`｜第 9 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E32  | 转写失败的编码原因 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「If you send a different file type or a file encoded with a different codec, voice message transcription will…」（`messages_audio_messages_md`｜第 11 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E33  | 用户端状态视频时长 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Status videos have a maximum duration of 90 seconds.」（`faq_453914586839706`｜第 23 句） |
| E34  | 用户端一次选图数量 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Tap and hold to select up to 100 images.」（`faq_453914586839706`｜第 10 句） |
| E35  | 用户端的视图一次 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「View once media View once photos and videos won’t be saved to the recipient’s Photos or Gallery.」（`faq_578442220724722`｜第 4 句） |
| E36  | 用户端自动下载的例外 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「YouTube videos are unable to auto-download.」（`faq_366146522333492`｜第 13 句） |
| E37  | 客户服务窗口的计时 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「If the user messages or calls you again before the timer expires, the timer resets to 24 hours.」（`messages_send_messages_md`｜第 11 句） | 官方未按这一列写该项 |
| E38  | 投递顺序 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「When sending a series of messages, the order in which messages are delivered is not guaranteed to match the…」（`messages_send_messages_md`｜第 138 句） | 官方未按这一列写该项 |
| E39  | TTL 到期丢弃 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「The platform drops messages it cannot deliver within the default or customized TTL.」（`messages_send_messages_md`｜第 149 句） | 官方未按这一列写该项 |
| **合计** | **39** 个维度 × 7 列 = **273** 格 | 有句 **53** 格 | 空格 **220** 格 | 去重引用页 **16** | 跨格复用句 **1** 次 | 截断 **15** 格 | 图像与贴纸页 7 | 影片页 4 |

#ROWS=39 COLS=9


# 附表 F 口径冲突与官方未写明处（12 行）

本表收录两类东西：官方多处写法不一致、需要读者知道本篇按哪一说走的**冲突**，以及官方在这批登记页面里根本没写、本篇不替它补的**空白**。全表 **11** 个议题：**11** 个两侧都取到了原句、**0** 个只有一侧有原句（另一侧官方没写）、**0** 个两侧都没有原句可引（共 **0** 行）。

两侧皆空的行要说清它空在哪：本轴登记 **27** 页，其中正文未取回 **0** 页——这 **0** 页全篇不引用，已引用 **27** 页（Meta 开发者文档 16／WhatsApp 帮助中心 11），所以两侧皆空不是因为抓不到，而是因为**官方在可引用的这批页面里没按这一项写过一句话**；这一类议题一律登记为空白，正文不替官方补结论。

取句方式与附表 E 同一把刀：每个说法登记「页面标识＋一条正则」，脚本在原句池里取句，命中 0 句或多句都会让生成脚本中止；取到的每一句再逐字回表到 `_tmp/w6_a8s/` 的对应页面。本表引用页面去重 **9** 个，同一句在多个议题里复用的共 **1** 次。两侧点同一页、又取到同一句的行由脚本直接中止——同句不构成冲突，不允许拿复制冒充对照。差异性质用了 **11** 种。这 11 条性质各不相同（每种恰好 1 行），不在这里重述，逐条只写在表内「差异性质」列。

「本篇采用」一列写的不是判断谁对谁错，而是本篇正文按哪一说的走、以及为什么。这一列的 **10** 行标为已写进正文、**1** 行只在本附表登记（正文不替官方补结论，也不把两种说法合成一说）。这一列标「已写进正文」的 **10** 行本次已逐句核过成稿：它们要引的 **20** 句官方原句全部原样出现在成稿里（空白折叠后做子串比对），缺任何一句本脚本都会中止，所以这一列不是空头承诺。官方原句超过 130 字符按整词截断并以省略号结尾，共 **5** 处截断；截断只发生在显示层。

| 编号 | 议题 | 说法甲 | 说法乙 | 差异性质 | 本篇采用 | 正文落点 |
|---|---|---|---|---|---|---|
| F1   | 视频体积上限的两个口径 | 「The maximum file size for videos sent or forwarded through WhatsApp is 64MB on all platforms.」（`faq_823886715479103`｜第 4 句） | 「The maximum supported file size for media messages on Cloud API is 100MB.」（`business_phone_numbers_media_md`｜第 107 句） | 口径对象不同：帮助中心写手机端发送或转发的视频上限，开发文档写 Cloud API 媒体消息上限 | 本篇分两层写：手机端发送或转发的视频上限按帮助中心的数字，Cloud API 媒体消息上限按开发文档的数字，两者不互相套用。 | 已写进正文 |
| F2   | 媒体 ID 的有效期为什么有两个数字 | 「Media IDs returned by the API expire after 30 days.」（`business_phone_numbers_media_md`｜第 14 句） | 「Media IDs in webhooks expire after 7 days.」（`business_phone_numbers_media_md`｜第 15 句） | 取得渠道不同：API 返回的媒体 ID 与回调里的媒体 ID 分别计时 | 本篇按取得渠道分开写：API 返回的媒体 ID 与回调里的媒体 ID 各按官方数字计时，不合并成一个有效期。 | 已写进正文 |
| F3   | 下载 URL 与发送用链接的时间尺度 | 「Media URLs **expire after 5 minutes**, after which you must query the ID again to get a new URL.」（`business_phone_numbers_media_md`｜第 42 句） | 「If you are using a link (`link`) to a media asset on your server (as opposed to the ID (`id`) of an asset you have uploaded to…」（`messages_send_messages_md`｜第 129 句） | 对象不同：前者是下载用的媒体 URL 有效期，后者是发送时对公网链接的内部缓存窗口 | 本篇把下载 URL 与发送用链接分开写：下载 URL 五分钟失效需重取，发送侧公网链接十分钟内复用缓存，不把两者合成同一个时效。 | 只在附表登记 |
| F4   | 语音消息是否自动下载 | 「All voice messages are downloaded automatically.」（`faq_657157755756612`｜第 8 句） | 「When the WhatsApp user taps the play icon, the user manually downloads the audio message for the WhatsApp client to load and then…」（`messages_audio_messages_md`｜第 19 句） | 子类不同：帮助中心说的是语音消息，开发文档说的是基础音频消息 | 本篇按消息子类分开写：语音消息自动下载，基础音频消息需用户点播放后手动下载，不把两种音频子类混为一谈。 | 已写进正文 |
| F5   | 模板媒体用 ID 还是链接 | 「If you're using link, your asset must be on a publicly accessible server or the message will fail to send.」（`templates_template_media_md`｜第 13 句） | 「To reduce the likelihood of errors and avoid unnecessary requests to your public server, Meta recommends that you upload your…」（`templates_template_media_md`｜第 14 句） | 主次而非互斥：链接路径可用但要求公网可达，官方同时建议优先用已上载的媒体 ID | 本篇按官方的主次写：链接路径要求资产在公网可达的服务器上，官方同时建议优先上载并改用媒体 ID，两条并列不互相取消。 | 已写进正文 |
| F6   | 轮播卡片上限的两个十 | 「Media card carousel templates allow you to send a single **marketing template** message accompanied by a set of up to 10 product…」（`templates_marketing_templates_media_card_carousel_templates_md`｜第 3 句） | 「Define the exact number of product cards (minimum 2, maximum 10) when you create the template.」（`templates_marketing_templates_media_card_carousel_templates_md`｜第 15 句） | 同一页两处写法：消息侧展示上限与建模板时的取值区间说的是不同环节 | 本篇把两处并列写：一张模板消息最多十张卡，建模板时必须把卡片数定在二到十之间，两个数字说的是不同环节。 | 已写进正文 |
| F7   | 服务窗口与 TTL 是两个计时器 | 「If the user messages or calls you again before the timer expires, the timer resets to 24 hours.」（`messages_send_messages_md`｜第 11 句） | 「The platform drops messages it cannot deliver within the default or customized TTL.」（`messages_send_messages_md`｜第 149 句） | 对象不同：服务窗口决定能否发自由格式消息，TTL 决定已发出消息的重试期限 | 本篇把两个计时器分开写：服务窗口按用户来消息或来电重置，TTL 是已发出消息的重试与丢弃期限，不读成同一个截止时间。 | 已写进正文 |
| F8   | 下载失败后的排查顺序 | 「In that case, try to [get a new media URL](#get-media-url) and download it again.」（`business_phone_numbers_media_md`｜第 60 句） | 「If doing so doesn't resolve the issue, renew your access token and attempt to download the media asset again.」（`business_phone_numbers_media_md`｜第 61 句） | 先后顺序：官方先让重取 URL，仍不解决才换访问令牌 | 本篇按官方给的先后写：先重取媒体 URL，仍失败再换访问令牌重试，不把两步写成并列选项。 | 已写进正文 |
| F9   | 两种轮播各自的卡片一致性约束 | 「All cards defined on a template must have the same components.」（`templates_marketing_templates_media_card_carousel_templates_md`｜第 10 句） | 「Button types and numbers must match across all cards (for example, if you define a card with 2 quick-reply buttons, all cards…」（`messages_interactive_media_carousel_messages_md`｜第 14 句） | 载体不同：模板型轮播要求所有卡组件相同，交互式媒体轮播要求按钮类型与数量跨卡一致 | 本篇按载体分开写：模板型轮播要求所有卡组件一致，交互式媒体轮播要求按钮类型与数量跨卡一致，各按各的原文，不互相替代。 | 已写进正文 |
| F10  | 状态媒体的时长两个数字 | 「Status videos have a maximum duration of 90 seconds.」（`faq_453914586839706`｜第 23 句） | 「Record and send a voice status update You can record and share up to 30-second voice updates.」（`faq_657157755756612`｜第 21 句） | 媒体种类不同：状态视频与状态语音各有自己的时长上限，不是同一规则的两个版本 | 本篇按媒体种类分开写：状态视频与状态语音各按官方给的时长上限登记，不把两个数字读成同一规则。 | 已写进正文 |
| F11  | 用户端影片上限的两个前提 | 「For users with a faster internet connection, the default video size limit is 100MB and 720p resolution.」（`faq_453914586839706`｜第 21 句） | 「The maximum file size for videos sent or forwarded through WhatsApp is 64MB on all platforms.」（`faq_823886715479103`｜第 4 句） | 同一站点两页不同层：一页写默认值随网速与画质变化，另一页写所有平台的影片上限 | 本篇并列写：默认影片上限随网速与画质两档变化，另一页把全平台影片上限写成一个值，两处不合并成一条。 | 已写进正文 |
| **合计** | **11** 个议题 | 两侧有句 **11**／单侧 **0**／两侧皆无 **0** | 引用页 **9** | 性质 **11** 种 | 正文落点：已写 **10**／只在附表 **1** | 截断 **5** 处 |

#ROWS=11 COLS=7


---

**边界声明：** 本篇是**官方媒体消息、附件与媒体对象文本的口径梳理**，不是发送指南，也不是对平台编码或画质能力的评价，更不是运营方案。全篇内容限于 WhatsApp 与 Meta 公开页面已经写出的表述，共 **27** 个页面登记位：Meta 开发者文档 **16** 页（媒体对象 1 页、五类媒体消息 5 页、交互式媒体轮播 1 页、模板媒体 2 页、发送消息 1 页、Direct Send 1 页、回调参考 5 页）、WhatsApp 帮助中心 **11** 页（附件与影片、媒体打不开、自动下载、媒体可见性、视图一次、贴纸、语音消息、语音预览、转发媒体、媒体档案）。引用这些文本的目的只有两个：说明平台把**媒体对象、五类媒体消息、编码与容器格式、体积与时长上限、模板头图与轮播卡片**写成了哪些对象名、哪些字段、哪些上限、哪些否定式前提与哪些拒绝条件；说明同一批文本把**媒体 ID 与媒体 URL 的有效期、服务窗口、发送缓存、投递顺序与 TTL**写成了哪些数字与哪些限定词。本篇**不含**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法；**不含**任何可用于向非自愿收件人大量投递媒体或附件的方法——"怎样把一段影片当广告群发""怎样绕过模板审批把媒体塞进会话""怎样用大体积附件压垮对方的客户端"这三类问法在本篇一律不答；**不含**代码、恶意样本或利用链构造方式；**不含**任何针对他人设备或他人账号的操作步骤与时序。界面页里官方写成"步骤""清单"的内容——在聊天里怎么选图、怎么按住录音、贴纸包怎么排序、.nomedia 文件放在哪一层目录——本篇只登记**控件名、字段名、媒体类型与条目数**，**不重述每一步在哪个页签里怎么点，也不把清单转写成可执行的动作序列**，读者要动手请回原页。

五类页面在本篇里始终分开写，因为官方没有一页把它们画成同一张对象图：**对象与时钟面**写在媒体页，回答媒体是什么、ID 与 URL 各活多久、下载要带什么（第 2 章）；**消息本体面**写在五类消息页，回答每类媒体长什么样、收什么编码、哪些条件会让请求失败（第 3 章）；**数字面**写在媒体页、五类消息页与帮助中心，回答体积、时长、数量上限分别落在哪一层（第 4 章）；**模板与轮播面**写在模板媒体页、轮播模板页与交互式轮播页，回答媒体怎么进模板、卡片数量在哪一步固定、两种轮播各约束什么（第 5 章）；**投递与回传面**写在发送消息页与五页回调参考，回答窗口、缓存、顺序、TTL 与回调字段各自管什么（第 6、7 章）。把这五层合并成"一条媒体消息从上传到丢弃的完整规则"，会得到官方没有写过的跨面结论，本篇不这么做。

官方写成区间、多档或带限定词的数字（「up to」「maximum」「on all platforms」「for users with a faster internet connection」一类）按整段一行登记在附表 B，不合成单值、不取中值、不去掉官方写在数字前面的限定词；同一数字在不同指代上各写一处的按指代分列，不做跨指代合并；同一句官方原句在两个页面各出现一次的，B 表也各登记一行，因为它的去重键是「页面＋数字＋单位」。B 表最终是 **15** 条、**16** 行（含合计）、分布在 **5** 个页面、去重后的「数字＋单位」组合 **11** 组、单位词 **8** 种、性质 **3** 种（上限 **9**、缓存与复用窗口 **2**、自动解除或自动生效的时限 **4**）、整句截断 **2** 行；截断只发生在显示层，进表的每一句都先用未截断整句回原页核对，失败 **0** 句。它的输入是原句池的 **399** 条整句（覆盖 **27** 页），其中 **355** 句整句不含任何阿拉伯数字、本来就不在 B 表的取材范围里；另有 **22** 个「数字后面四词都对不上任何一档」的实例写进 `_tmp/w6_a8_bdrop.tsv` 供逐行复核，不进表——这一批多数是接口示例里的对象标识与字段说明，不是官方对某项业务写下的数量，**"这个数是不是一个上限"的判定权在读者手里，不在这张表里**。贴纸页与文档页各有一张类型／扩展名／MIME／最大体积表，表格行按建池口径整行剔除，所以贴纸的单档体积与文档按类型的体积都不在 B 表里；**"表里没有这一行"不等于"官方没有这个数"**，只是它写在表格里，不在句子层。

正文引句全部为整句、逐字回核到抓取件。整句的判据是抓取件的句池切分口径：句池按句子终止符切、不按版面标记切，因此官方 markdown 页里紧跟句子的小标题会被并入同一句；同时，长度不在窗口内、段内含竖线或内联链接、以 `#` 或 `-` 起手的片段一律不入池。本轮正文引用去重后 **110** 句（引用标记 **110** 处，同一句在正文重复引用 **0** 处），分布在 **23** 个页面上；官方标题在正文被点名 **30** 处、覆盖全部 **27** 个登记页。引句里 **8** 句自带官方粗体排版标记 `**`（每句各一对，偶数，照抄原文、不删字符），**0** 句在同一句里带 `## 小标题`。另有一条硬规矩：含反引号字段名与枚举名的句子不整句引用，这些页面上的事实改用中文陈述并逐处标注出处页；**本轮正文实际引用的 110 句里，含反引号的句子是 0 句**，这条规矩没有被绕过。本轮引用的句子里没有带英文双引号且引号不闭合的句子（回核计数为 **0**）。

方法层的三张表各自回答一个问题，且都不越界：附表 E 把同一组 **39** 个维度并排放在 **7** 列主体上（图像与贴纸页／影片页／语音页／文档页／模板与轮播页／媒体对象与投递页／用户帮助中心）＝ **273** 格，**53** 格取到官方原句、**220** 格标作「官方未按这一列写该项」，该标注**只说明官方文本的写法，不说明该类主体没有该项规则**；去重引用页 **16** 个、跨格复用句 **1** 次、显示层截断 **15** 格。附表 C 把「官方是否写明」限成「词（组）× 站点类别」矩阵，**52** 个词（组）分属 **8** 个主题组 × **2** 类站点共 **61** 行、句次 **549**、去重命中句 **259**（占全池 64.9%），**7** 个词按零命中登记。附表 D 只登记官方在页面里逐字写出的取值域：配置的 **10** 串标记本轮 **2** 串在登记页里出现，另 **8** 串在扫过的每一行原文里零命中，最终 **6** 组、**16** 个取值、去重取值名 **8** 个、覆盖 **6** 页；另有 **3** 处官方写了标记却取不出两个以上取值、分布在 **2** 个页面，逐处连同原文行号写进 `_tmp/w6_a8_dmiss.tsv`。附表 F 把 **11** 个议题逐条对撞（两侧有句 **11**／只有一侧 **0**／两侧皆无 **0**，引用页去重 **9**、差异性质 **11** 种、复用 **1** 次、显示层截断 **5** 处），凡官方留白处一律登记为留白，不推断、不补齐。

页面层的四项事实单独交代，免得读者把"登记"读成"引用"：**第一**，本篇登记的 **27** 页正文全部取回，没有一页被标注"全篇不引用"，也没有一页整页零句；**第二**，正文整句引用落在 **23** 个页面上，其余 **4** 页被点名而未引句——回调参考里的 video／audio／sticker 三页与帮助中心的语音预览页；前三页的字段口径与已引的 image／document 两页同构，第四页的正文全是界面路径，逐页状态由脚本实测而非人工填写，见附表 A 的引用位置列；**第三**，**27** 页全部在正文被官方标题点名（标题标记共 **30** 处），附表 A 的引用位置列没有一格留空；**第四**，正文引用的 **110** 句里没有一句含反引号、没有一句引号不闭合，含粗体的 **8** 句全部按原样保留标记。

本篇对"某个编码该不该支持""某个体积上限该不该这样定""转码之后画质损失多少""服务端文件存在哪里"一律不补；媒体 URL 过期后旧地址还能不能被第三方使用、模板链接路径与媒体 ID 路径在失败时返回是否相同、状态媒体的时长上限是否随版本变化，这些官方只写了一半的问题登记在附表 F，正文只写官方实际写了的那一半。两张站点口径只按各自页面分别写：开发文档的对象、字段与回传，帮助中心的设置、排查与本地档案，两套装在两章里，不合并成"WhatsApp 的媒体规则"。

最后两处范围断言必须交代扫描口径，因为它们都是"穷举后没有"而不是"抽样没看到"：**其一**，本篇在 1.2 写"媒体对象、媒体 ID、媒体 URL、附件、载体"五个词的所指，第 9.1 节把它收成四组同词不同层。这一句的依据是：附表 C 的 8 个主题组共 **52** 个词（组）在 **399** 句里逐句扫过，命中落在不同的页面与不同的字段上；但这只覆盖本篇登记的 **27** 页，两个站点还有大量本篇未登记的页面，所以"没有统一的媒体规格表"是对这批语料的陈述，不是对 WhatsApp 全部公开文本的陈述。**其二**，附表 F 有 **1** 个议题标成"只在附表登记、正文未并列两句"，逐条点名是：F3 下载 URL 与发送用链接的时间尺度。这一行能在附表 F 逐字回表；正文在 2.2 与 6.2 各引一处、不把两句并排——**排不并排是叙述选择，不是"官方没有连接句"的断言**；读者若要把它们当通则使用，应自己回到那两页看官方在上下文中怎么连接这两句。

Telegram: https://t.me/one00190
