@@TITLE@@WhatsApp 商业目录、商品消息与购物车订单的官方口径解读
@@SUBTITLE@@本篇是"WhatsApp 群发／群控系统性口径"系列第四轮的第一篇（轴①，编号 n=12），讲的是：目录在官方文本里被写成什么对象、它挂在账号的哪一层、一个账号层级能有几个目录、目录里的条目哪些字段是必备、四种商品消息形态各要不要模板、目录链接为什么只是文本消息的一种渲染、单商品与多商品消息的设备分层、商品详情页为什么只显示图片、四种模板各自的类别与卡片上限、购物车属于线程还是账号、购物车有没有过期日、商家为什么不能主动发购物车、商家关掉购物车之后的一天里发生什么、订单消息把状态推到哪一步、支付按钮过期后由谁禁用、巴西与印度的订单详情模板差在类别还是按钮、订单取消与付款构成什么闭环、订单 webhook 为什么可能缺号码字段、以及收单之后的追问、回执与用户侧出口。全篇只读官方文本，共 **32** 个页面登记位（**32** 页正文全部取回，逐页归属与引用位置见附表 A 的 **33** 行含合计）：Meta 开发者文档 **20** 页（商业目录 **14**、巴西与印度支付 **5**、订单 webhook **1**）、WhatsApp 帮助中心 **12** 页。
@@INFO@@报告日期|2026 年 11 月 18 日
@@INFO@@分析对象|按站点清点本篇登记的页面构成：Meta 开发者文档 **20** 页（目录总纲、目录消息、目录链接消息、单商品消息、多商品消息、目录模板、单商品模板、多商品模板、商品卡片轮播模板、交互式商品轮播、上传库存、商务设置、分享商品、接收回应；巴西订单消息、巴西订单详情模板、巴西收款请求 CTA、印度订单详情模板、订单状态模板；订单消息 webhook 参考）、WhatsApp 帮助中心 **12** 页（商业产品对照、目录、购物车、订单详情、订单详情与订单摘要、集合、目录链接、分享商品、举报目录或商家、Shops 与商业 App、商业 App 购物车、创建订单详情）。合计 **32** 个登记位，正文全部取回；归组依据是官方页面标题，逐页归属见附表 A。
@@INFO@@视角定位|官方规则文本、帮助中心说明与开发者文档的**口径解读**，不是操作手册，也不是投放方案。本篇**不提供**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法；本篇同样**不写**任何可用于向非自愿收件人大量投递商品或订单消息的方法——"怎样把商品消息当广告群发""怎样绕过模板审批把营销内容塞进会话""怎样用购物车消息批量触达没同意过的号码"这三类问法在本篇一律不答；官方把商品、购物车、订单写成对象、归属、状态与上限，本篇只登记这些句子出现在哪一页、哪一句（界线见 1.3）。
@@INFO@@分类|**官方页面登记 32 个登记位 × 2 类站点 × 引用位置 × 取回状态**（附表 A，**33** 行含合计；Meta 开发者文档 **20**、WhatsApp 帮助中心 **12**；可引用 **32** 页、不可引用 **0** 页；原句池 **291** 条整句、覆盖 **31** 页）× **数字归档 6 行，五档 1／1／3／0／1（一／二／三／四／五），去重 5 组数字＋单位、5 种计数单位、4 种性质，覆盖 5 页，整句截断 2 行，池内不含数字的整句 250 句、按单位词表定不出档位而被丢弃的数字实例 13 个、在池子里一句都没有的可引用页面 1 个**（附表 B，**7** 行含合计）× **官方用词 × 站点类别矩阵 61 个词（组）分属 8 个主题组 × 2 类站点、非空格 79 个、句次 617、去重命中句 250（占全池 85.9%）、一句最多同时落进 10 格、落进两格以上的句子 171 句、零命中词 0 个、逐字回表失败 0 句**（附表 C，**80** 行含合计）× **官方写明的取值域 1 组、取值 2 个、去重取值名 2 个、覆盖 1 页，配置的 10 串标记本轮扫到 2 串、8 串零命中，另有 2 处写了标记却取不出取值域、分布在 1 页**（附表 D，**3** 行含合计）× **同一组维度在各主体上的逐列对照 27 个维度 × 5 列 = 135 格、有句 37 格／空格 98 格、去重引用页 21、同一句跨列复用 2 次、截断 20 格**（附表 E，**28** 行含合计）× **口径冲突与官方未写明 12 个议题、两侧有句 9／单侧 1／两侧皆无 2、差异性质 11 种、去重引用页 12、复用 0 次、正文落点已核 8 行、截断 7 处**（附表 F，**13** 行含合计）× **正文与附表的官方原句逐字回核，失败 0 条**。
@@INFO@@难度|进阶（本篇有六处容易读错。**第一是"购物车不设过期日"不等于"购物车永远在"**：一句说它没有过期日，另一句说它只存在于具体设备、换设备就不在；两句说的是不同性质（5.1，附表 F 的 F2）。**第二是"没过期"与"开关延迟一天"是两个对象**：前者是购物车对象，后者是商家设置对象，把两句叠成"购物车有一天的有效期"会读反（5.2，F1）。**第三是"最多十张卡"在两页各有一个前提**：模板型是一条已批准模板的消息可扩展到十张，交互式是这条消息本身最多十张；数字相同不等于同一条规则（4.4 与 4.5，F9）。**第四是支付按钮过期后的责任方向相反**：订单页说用户端按钮会被禁用，收款请求页说 WhatsApp 不会自动禁用；两页并列登记，不合并成一句（7.5 与 8.2，F5）。**第五是模板类别要按地区读**：巴西版订单详情模板可选实用或营销，印度版固定实用，订单状态模板两地都是实用（8.1 与 9.1，F8）。**第六是订单状态不是系统定的**：起点与完成态由消息条件决定，何时算完成由商家判定，取消与付款构成一对闭环限制（7.2 与 9.2，F6 与 F7）。）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

本篇是"WhatsApp 群发／群控系统性口径"系列第四轮的第一篇（编号 n=12），主题是**商业目录、商品消息、购物车与订单模板**：一个商家在 WhatsApp 里展示商品、让客户把商品放进购物车、把购物车发回来、再用订单模板催付款，这条链路在官方文本里被拆成哪些对象、每个对象归谁所有、哪些动作商家做不了、哪些上限按什么单位计、哪些模板属于哪一类、哪些状态由谁推进、以及"目录""商品消息""购物车""订单"这四个词在页面之间是不是同一个意思。全篇只读官方文本，共 **32** 个页面登记位：Meta 开发者文档 **20** 页、WhatsApp 帮助中心 **12** 页；**32** 页正文全部取回，其中 **31** 页进入原句池、共 **291** 条整句，另有一页整页入池零句（第 1.3 节点名）。

前几篇问的是"谁能发、发给谁、名字归谁、对方愿不愿意、到了之后怎么办、一条消息被算成几笔"；本篇问的是"商家能不能把一个目录挂到账号上、客户点了商品之后发生什么、购物车这个对象活在哪一层、订单消息把状态推到哪一步、模板的类别与按钮由谁规定"。这条线在官方文本里被拆在五类页面上：目录总纲与库存页写对象与接入，商品消息页写四种形态的动作与平台支持，模板页写类别与卡片上限，帮助中心页写商家端能看到与做不到什么，订单与支付页写状态机与支付按钮。五类页面之间没有统一的对象图，只有互相指向的链接；本篇把它们并排登记在附表 E 的 **27** 个对照维度 × **5** 列 = **135** 格（**37** 格取到官方原句、**98** 格标作"官方未按这一列写该项"），并把成对出现的口径差异与官方留白登记在附表 F 的 **12** 个议题里。

本篇的读法是**沿一条订单往回走**：先看目录这个对象怎么接入账号（第 2 章），再看商家能把目录以哪几种消息形态发出去（第 3 章），然后是四种带模板的形态与它们的卡片上限（第 4 章）；接下来是购物车——它属于谁、能放多少、什么时候消失（第 5 章），以及商家端帮助中心把同一件事写成了什么（第 6 章）；再往后是订单本身：巴西的订单会话与状态机（第 7 章）、巴西的订单详情模板与支付请求 CTA（第 8 章）、印度的订单模板（第 9 章）、订单 webhook（第 10 章）；最后是客户侧的响应与举报（第 11 章）、横向对照（第 12 章）与容易读错的几处（第 13 章）。

下面每个问题都标出它落在哪一节，一个问题可以跨相邻两小节，一节也可以由两个问题分别追问；跨小节的关联只做中文指认，不把两页原句拼成一句。

1. 这批页面在站点上怎么分布，为什么"32 页登记、31 页有句"？（1.1）
2. 本篇固定了哪些中文口径词，每个词对应哪个英文原词？（1.2）
3. 一页整页入池零句，它在附表里落在哪一行，正文为什么不引它？（1.3）
4. 本篇不写什么，官方写成"步骤"的界面内容登记到什么颗粒度？（1.3）
5. 目录在官方文本里被定义成什么对象，它和账号是什么关系？（2.1）
6. 目录条目里官方列了哪些信息，哪些是"包含"、哪些是"必备"？（2.1、6.1）
7. 库存上传页为什么要先问"你有没有现成的 Meta 目录"？（2.2）
8. "一个 WhatsApp 账号层级只能有一个目录"限制的是哪一层？（2.2）
9. 商务设置里的开关管的是购物车还是目录可见性？（2.3）
10. 四种商品消息形态各是什么，它们都需要模板吗？（3.1）
11. 目录消息的头部图片来自哪里，正文能自定义到什么程度？（3.1）
12. 目录链接消息是把目录塞进文本消息，还是另起一种消息类型？（3.2）
13. 单商品消息和多商品消息在动作上有什么相同与不同？（3.3、3.4）
14. 商品详情页为什么不显示视频与 GIF？（3.3、3.4）
15. 客户端的版本不够时，官方的降级行为写在哪一句？（3.3、3.4）
16. 多商品消息里如果有一个商品对不上目录，接口会怎么处理？（3.4）
17. 目录模板、单商品模板、多商品模板各属于哪一类模板？（4.1、4.2、4.3）
18. 多商品模板的商品总量与分区是怎么写的？（4.3）
19. 商品卡片轮播模板的卡片上限和"已批准两张卡"是什么关系？（4.4）
20. 交互式商品轮播最少要几张卡、最多几张卡，卡片索引怎么写？（4.5）
21. 模板型轮播与交互式轮播的数字相同，为什么不能合并成一条规则？（4.4、4.5）
22. 购物车属于聊天线程还是属于账号，官方在哪一句里把它钉在设备上？（5.1）
23. 购物车里一种商品最多放多少件，不同商品种类有没有上限？（5.1）
24. 购物车发出之后还能不能改，客户要加东西怎么办？（5.1）
25. 商家能不能主动给客户发一个购物车？（5.1）
26. 商品下架或售罄时，已经在购物车里的那一项会怎样？（5.1）
27. 商家端关掉购物车开关，为什么还会在一天内收到购物车？（5.2）
28. 商家资料里的目录入口是什么形态，官方把它写成按钮还是菜单？（6.1）
29. 目录里的价格是不是必备字段，哪些国家才显示价格？（6.1）
30. 目录链接能不能自定义，官方为什么说它"可能失效"？（6.2）
31. 商家主动分享商品有哪几种去向，本篇为什么只点名不重述步骤？（6.3）
32. 集合在目录里解决什么问题，上架前要经过谁审核、要多久？（6.4）
33. Shops 店面停用之后，商家原来的商品去哪了？（6.5）
34. 商业 App 与商业平台在这批页面里怎么分工，谁能挡住不想被联系的客户？（6.6）
35. 订单详情消息与订单状态消息各推进什么，`order` 对象省掉会怎样？（7.1）
36. 订单的起点与终点是哪两个状态，巴西页为什么同时写两种终态条件？（7.1、7.2）
37. 支付按钮过期后到底会不会被禁用，两页为什么写法相反？（7.5、8.2）
38. 巴西与印度的订单详情模板在类别与按钮文案上差在哪？（8.1、9.1）
39. 订单取消与付款的先后限制，违反时官方写成什么后果？（9.2）
40. 订单 webhook 为什么提醒"只靠电话号码的集成会收不到消息"？（10.1）
41. 客户侧能对商品与商家做哪些事，官方把举报写成几个选项？（11.3）
42. 把这批页面横向排开，哪几组词是"同词不同层"，哪几处是官方留白？（12.1、12.2）

# 1 口径与范围

## 1.1 登记、取数与三张矩阵

本篇登记 **32** 页，来源只有两个站点：Meta 开发者文档 **20** 页（WhatsApp 商业目录 14 页、巴西与印度支付 5 页、订单 webhook 1 页），WhatsApp 帮助中心 **12** 页（目录、购物车、分享、集合、Shops、订单详情与举报）。这个分布不是按"哪边写得更细"选的，而是按"这条链路上官方把每一段写在哪"选的：对象与接口写在开发者文档，商家端能看到什么、做不到什么写在帮助中心，两边对同一件事的措辞并不一致，第 12 章专门处理这种不一致。

取数分三层，三层共用同一批抓取件。**第一层是原句池**：脚本把每页正文切成整句，剔除含竖线的表格混合句、以列表符号起手的片段、长度越界的句子，得到 **291** 条整句，覆盖 **31** 页；剩下那一页没有产出任何可入池的整句，第 1.3 节单独交代。**第二层是词矩阵**：附表 C 用 **61** 个词（组）分 **8** 个主题组逐句扫过这 **291** 句，命中落在"词 × 站点类别"的格子里，全表 **79** 行、句次相加 **617** 次、去重后真正被命中的句子 **250** 句。**第三层是对照表**：附表 E 把 **27** 个维度并排放在 **5** 列主体上，每一格用"页面标识＋一条正则"在原句池里取句，取不到就留空并写明"官方未按这一列写该项"。

三层之外还有两张登记表：附表 A 逐页登记标识、站点、URL、官方标题、取回状态与引用位置；附表 B 只收"数字＋它数的东西"，本篇这批页面里带阿拉伯数字且能定出单位的整句不多，B 表只有 **6** 行；附表 D 只登记官方逐字写出的取值域，本篇只有一组。**数字少不是这批页面没有数量概念**，而是这条链路把大部分数量写成了英文词（"two cards""three media types"一类），或者写在接口参数表的示例值里；前者不进 B 表的取材范围，后者连同表格 markup 一起被建池口径剔除，B 表如实登记自己漏掉了什么。

## 1.2 口径词：目录、条目、商品消息、购物车、订单

这条链路上最容易混的是五个词，本篇先固定中文译法与所指，后文不再改口。

**目录（catalog）**：官方在总纲页把它写成"上传到 Meta 并连接到 WhatsApp 账号的结构化库存"。它不是一个聊天里的对象，而是一个**账号级的库存容器**；商家可以在目录里建集合，可以把目录挂在商家资料上，也可以把它以消息形态发给客户。**商品条目（catalog item）**：目录里的一条商品或服务，官方在总纲页用一句话列举它包含的信息，在帮助中心页用另一句话列举它的必备字段——两句话列举的东西不一样，第 6.1 节把这件事单独写清楚。

**商品消息（product message）**：本篇把官方这批页面里的四种形态统一叫"商品消息"，它们是**目录消息**（catalog message）、**目录链接消息**（catalog link message）、**单商品消息**（single-product message）与**多商品消息**（multi-product message）。前两种是把整个目录端出去，后两种是把目录里的一个或一组商品端出去；四种形态的头部、按钮、模板要求各不一样，不能因为都叫"商品消息"就合成一种。**购物车（cart）**：客户在会话里把若干商品条目攒成的一个待发送对象，官方把它写成"属于某个客户与某个商家之间的具体聊天线程、存在于具体设备上"的对象，第 5 章整章都在拆这句话。**订单（order）**：客户把购物车发回商家之后形成的一次交易意图；在巴西与印度两套支付页里，"订单"还特指两种消息（订单详情与订单状态）以及它们可选的 `order` 对象。

另外两个词本篇按官方原词照写、不译成别的：**模板（template）** 与 **CTA（call-to-action）**。前者指预审批过的消息模板，后者指模板或消息上的按钮；这两个词在目录页与支付页里都出现，但指的不是同一层对象，第 12.1 节会点名。

## 1.3 零句页与"不写什么"

《Share products with WhatsApp users》是本篇唯一一页整页入池零句的页面。它取回成功、标题清楚、正文也在，但正文的组织方式是一组指向其它页面的链接与一句话说明，切句之后没有任何一条落在建池口径的窗口里。这一页在附表 A 留一行登记，引用位置写明正文点名它的位置；在其余各表里不会出现——**"这一页不在某张表里"不等于"这一页没有内容"**，它承载的是"把商品分享给用户"这个入口的导航，具体规则写在它指向的那几页上，本篇按那些页面取句。

本篇的边界与系列前几篇一致，并且在这一篇里尤其要重申：全篇只登记官方写出的对象、字段、类别、上限与状态，**不提供**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法；**不含**任何可用于向非自愿收件人大量投递商品或订单消息的方法——"怎样把商品消息当广告群发""怎样绕过模板审批把营销内容塞进会话""怎样用购物车消息批量触达没同意过的号码"这三类问法在本篇一律不答。界面页里官方写成"步骤"的内容——在商家端哪个页签点哪个按钮、集合怎么拖动排序、分享入口选哪一项——本篇只**点名与计数**（例如"四种去向""两种举报选项"），**不重述点击次序**，也不把清单转写成可执行的动作序列；读者要动手请回原页。

还有一类"不写"是对象层面的：本篇不判断某个商品该不该上架、不评价某个模板类别判得对不对、不推断官方没写的退款与资金处理规则、不把巴西与印度的地区口径合并成通用规则。这些空白在附表的 F 类行里逐条登记，正文只在它们真正影响读法的地方点名。

# 2 目录本体

## 2.1 目录是什么，怎么接到账号上

《Catalogs overview》是这批页面里唯一一页试图把整条链路从头讲一遍的文本。它开篇给了一句用途说明，把"目录"与"跳转站外"对立起来：「Instead of redirecting customers to external websites or apps, catalogs let customers browse products within the chat — customers can view products, explore options, and place orders without leaving WhatsApp.」——目录的价值被写成"不把客户带出聊天"，客户在会话里就能看商品、选项、下单。紧接着它给目录下定义：目录是"上传到 Meta 并连接到 WhatsApp 账号（WAAC）的结构化库存"（这一句自带官方小标题，按第 1.3 节的引句规矩只作中文陈述，不整句引用）。

条目信息在这一页被列举成一句完整的话：

> `Each catalog item includes details like name, description, price, images, and availability.`

这句是"包含"式列举，不是"必备"清单；帮助中心页另有一句"必备"式列举，两句话列举的字段不同，第 6.1 节把差异摊开。接入之后发生什么，这一页用一句判断句收住：

> `Once connected, this inventory becomes the foundation for all commerce interactions on WhatsApp.`

也就是"库存接上之后，它成为 WhatsApp 上所有商务交互的基础"。这句里的"所有"是官方原词，本篇照引，但读者要注意它的范围是**这套商务能力内部**，不是"所有消息类型都依赖目录"。

接入之后还有一层配置，官方把它写成"商务配置"：商家要为商业电话号码配置商务设置，其中包含**是否启用购物车功能**与**目录可见性**两项（这一句同样带官方小标题与编号，只作中文陈述）。也就是说，目录接上不等于购物车自动可用；购物车是配置项，目录可见性也是配置项，两者在第 2.3 节各有对应页面。

## 2.2 库存从哪来：现成目录与"一个层级一个目录"

《Upload inventory to Meta》回答的是"库存怎么进 Meta"。它的第一句是一道分流：

> `If a business already has a Meta catalog set up, use that catalog for WhatsApp commerce use cases.`

如果商家已经有 Meta 目录，就直接把这个目录用于 WhatsApp 的商务场景；没有再走上传流程。第二句是一条硬限制：

> `You can only upload one catalog per WhatsApp account hierarchy.`

这句话里的"账号层级"是关键：限制不在商品数量、不在目录条目数量，而在**一个 WhatsApp 账号层级只能挂一个目录**。本篇因此不写"一个号码一个目录"——账号层级与电话号码不是同一层，官方在这一页没有把限制写到号码层。

## 2.3 商务设置：开关与店面图标

《Set commerce settings》只入池一句，讲的是店面图标的显示开关：「Set to `true` to show catalog storefront icon or `false` to hide it.」——把某个字段设成真就显示目录店面图标，设成假就隐藏。这一页的信息量很小，但它确认了两件事：目录在客户端有"店面图标"这种展示形态；这个形态由商务设置控制，不由目录内容控制。

购物车开关与目录可见性在总纲页被写成配置项（2.1），具体字段名与取值落在接口参数表里，按建池口径没有进入原句池。本篇的处理是：**在正文只登记"有这两个配置项"，具体字段与取值以官方接口页为准**，不替它补写参数名，也不把界面上的勾选状态写成接口取值。

# 3 四种商品消息形态

## 3.1 目录消息：把整个目录端出去

《Catalog messages》写的是"目录消息"这种形态本身。它的结构被一句话列全：

> `Catalog messages display a product thumbnail header image of your choice, custom body text, a fixed text header, a fixed text sub-header, and a **View catalog** button.`

拆开看：头部是一张商品缩略图，图上可以自己选；正文是自定义文本；再往上是固定文本头部与固定文本副标题；底部是一个固定的"查看目录"按钮。**只有缩略图与正文是商家可变的**，头部、副标题与按钮文案都是固定文本——这一点决定了目录消息不是"自由排版的商品页"，而是一种结构固定的信封。

头部图片从哪来，这一页在参数说明里给了答案：

> `WhatsApp uses the thumbnail of this item as the message's header image.`

如果商家指定的条目就是头部那张图，那么目录消息的视觉入口其实是**目录里的某一条商品**，不是单独上传的一张图。请求里两个收件人字段同时出现时以哪个为准，这一页也写死了：「If you include both `to` and `recipient`, `to` is used.」——这句在商品消息、模板消息、轮播页里反复出现，说明它是平台级的参数优先级，不是目录消息独有的规则。

## 3.2 目录链接消息：文本消息里的目录预览

《Catalog link messages》整页只入池一句，而这一句恰好说明了这种形态的本质：**发送一条文本消息时**，把可选参数 preview_url 设为真，消息就会为正文里的某个链接渲染一组商品目录缩略图（这一句自带字段名，按引句规矩只作中文陈述）。注意它的主语是**文本消息**，不是目录：也就是说，目录链接消息不是另一种消息类型，而是**文本消息的一种渲染效果**；没有模板、没有固定按钮，链接的预览形态由参数触发。这一页的信息量很小，但它把"目录消息"与"目录链接消息"分在了两层：前者是一种结构化消息，后者是文本消息的富预览。

## 3.3 单商品消息：一个商品、一条会话、一个购物车

《Single-product messages》把客户的动作拆成三步写：点开商品、加入购物车、把购物车发回商家。第二步之后有一句关于多设备的说明：

> `If your customer has multiple devices linked to their account, single-product messages are synced between devices.`

紧接着是限制：

> `However, the shopping cart is local to each specific device.`

两句合起来是本篇最重要的一组分层：**消息本体可以跨设备同步，购物车不行**。客户在手机上把商品加进购物车，换到网页端打开同一条消息，看到的是同一条消息，但购物车停在原来那台设备上。第 5 章会把这句话与总纲页的另一句并排。

商品详情页的媒体限制在这一页写得很直白：商品详情页当前只支持商品图片，加进商品的视频或 GIF 不会在详情页里显示（这一句句尾粘连了列表编号，按引句规矩只作中文陈述）。版本不够时的降级也写了两句：客户端版本不支持单商品消息时，客户收到的是"因版本过旧而无法接收"的说明；商家同时会收到一条 webhook，告投递失败的原因是客户版本过旧（这两句里含平台名与编号列表，只作中文陈述）。这一页还列了支持该形态的平台与最低版本，逐条落在附表 E 的版本维度上，本篇不在正文重复版本号。

## 3.4 多商品消息：一组商品、三种动作、一个本地购物车

《Multi-product messages》的结构与单商品页几乎平行，但动作多了一步"浏览列表"。客户收到多商品消息后能做三件事：看商品列表、把商品加入购物车、把购物车发给商家。看列表时：

> `View products: Customers can see a list of products.`

点进某一项时：

> `Whenever a customer clicks on a specific item, the product's latest info is fetched and the product displays in a Product Detail Page (PDP) format.`

也就是说，商品信息不是随消息一起固化的，而是在客户点击时**重新取一次最新信息**；这条设计与"商品下架后购物车里的那一项会被移除"是同一套思路，第 5.1 节会回到这一点。多设备的分层在这一页写得更完整：

> `If your customer has multiple devices linked to their account, WhatsApp syncs multi-product messages between devices.`

> `However, the shopping cart is local to each specific device.`

同样两句：消息同步、购物车本地。多商品消息在服务端还有一条约束，写在这一页的参数说明里：

> `At least one item from the products list must match an item from your product catalog.`

商品列表里至少要有一项能与商家的目录对上；全都对不上时接口不会整条失败，而是**丢掉对不上的项、发一条要求更新目录的错误提示**（这一句句尾粘连了下一步的参数清单，只作中文陈述）。版本不支持时的降级与单商品页同构：客户收到"版本过旧无法接收"的说明，商家收到一条说明同一原因的 webhook：

> `If the customer's app version does not support multi-product messages, the customer instead receives a message explaining that delivery failed because they are using an outdated version of WhatsApp.`

多商品消息只在**已存在的会话线程**里发送，这一点在这一页被写成一句范围限制（句子粘连了小标题，只作中文陈述）；它也支持被转发、被重新打开，同样不写成"可以主动发起新会话"。

# 4 模板与轮播

## 4.1 目录模板：目录本身被定义成营销模板

《Catalog templates》的第一句把类别写死了：

> `Catalog templates are marketing templates that allow you to showcase your product catalog entirely within WhatsApp.`

目录模板就是营销模板，作用是把整个目录"完全在 WhatsApp 内"展示出来。结构与目录消息同构：

> `Catalog templates display a product thumbnail header image of your choice and custom body text, along with a fixed text header and fixed text sub-header.`

缩略图同样来自目录里的条目：

> `The thumbnail of this item will be used as the message's header image.`

这一页没有写卡片、没有写分区，它描述的是"一个目录、一张头图、一段正文"的形态；带卡片的目录形态在 4.4 与 4.5 两节。**目录模板按官方的归类站在营销模板这一侧**；它与交互式商品消息（3.4 与 4.5）的模板要求并不相同，这组对照由附表 F 的 F3 行并排登记，正文不在这里合并成一句结论。

## 4.2 单商品模板：一个商品、标题、价格都从目录取

《Single-product message templates》对单商品模板的定义是一整句：

> `SPM templates are marketing templates that allow you to present a single product from your ecommerce catalog, accompanied by a product image, product title, and product price (all pulled from your product within your catalog), along with customizable body text, optional footer text, and an interactive **View** button.`

这句里三件东西都来自目录：商品图片、商品标题、商品价格；可自定义的是正文、可选页脚与一个交互式按钮。头图同样不是另传的：

> `The product image is pulled from the catalog and displayed in the message header.`

也就是说，单商品模板的"商品感"不是靠商家排版做出来的，而是靠目录字段填出来的；目录里那一项改价或换图，模板渲染出来的内容随之变化。

## 4.3 多商品模板：分区与商品总量

《Multi-product message templates》写的是"多商品消息模板"，它与 3.4 的多商品消息不是一回事：前者是预审批的模板，后者是交互式消息。这一页入池的句子不多，其中一句关于标识的对应关系值得记：

> `SKU numbers are labeled as **Content ID** in the Commerce Manager.`

也就是接口里的 SKU 在商务管理后台里被标成"内容 ID"，两边叫法不同、指同一件东西。商品总量与分区在这页的示例说明里出现：

> `It also defines two sections ("Popular Bundles" and "Premium Packages") and identifies the products (a total of 3) that should be injected into those sections.`

"这两个分区各注入多少商品"是示例载荷里的数，不是上限；这一页另有一句写商品总量的示例句，句尾粘连了接口示例的残片（方括号与斜杠），按引句规矩不能整句引用，本篇把它登记在附表 E 的 E8 行，**正文不重复那个数字**——读者要核那个上限，请按 E8 行回原页看整句。

## 4.4 商品卡片轮播模板：两张卡的批准，十张卡的消息

《Product card carousel templates》是模板型轮播的代表页。它的卡片规则先是一条一致性要求（句子粘连了小标题，只作中文陈述）：模板上定义的所有卡片必须拥有相同组件。然后是数量的关键句：

> `An approved template with two product cards can be used to send up to 10 cards in a template message.`

这句要读得很小心：**被批准的模板只有两张卡，但这条模板消息最多可以发十张卡**。十不是模板里定义的卡片数，而是发送时这一条消息能扩展到的最多卡片数。同一页另一句把变量与字符串的关系写死：

> `Number of strings must match the number of variable placeholders in the message body text string.`

示例说明里还有两句可以互证"卡片数由发送时的数组决定"：

> `It supplies a single body text variable value (which the template requires) and three product cards.`

> `Each card identifies where it should appear in the carousel (card_index), as well as the product ID and catalog ID where the card's product details (such as title, description, and price) can be found.`

第二句说每张卡自己带位置（卡片索引）、商品 ID 与目录 ID；位置由发送方给，不是模板固定。链接按钮与查看按钮的分工在这一页也写清了：想让用户去商家自己的网站就用链接按钮，链接会在设备默认浏览器里打开，把用户带出 WhatsApp 客户端（这一句是完整句）；而这套流程不触发订单 webhook：

> `With URL button flows, order placement happens outside of the WhatsApp client, so WhatsApp does not trigger order webhooks for URL-button flows. ## Catalogs To use product card carousel templates, you must have an e-commerce product catalog, with inventory, connected to your Messaging account.`

最后一句是使用前提：要用商品卡片轮播模板，必须有一个带库存、已连到消息账号的电商商品目录（这句粘连了小标题，只作中文陈述）。

## 4.5 交互式商品轮播：最少两张、最多十张

《Interactive product carousel messages》写的是交互式轮播，不是模板。它的卡片数量规则是一句完整的话：

> `You must add two card objects to your message, and can add a maximum of 10.`

**至少两张、最多十张**，两张是下限，不是建议。卡片索引也有明确范围：每张卡在数组里必须给出从零到九的索引值（这句含字段名，只作中文陈述）。所有卡片必须是同一类型、且引用**同一个目录标识**：「The type of each card must be set to `"product"`, and each card must reference the same `"catalog_id"`.」——也就是说，交互式轮播不能把两个目录的商品混在一条消息里。

把 4.4 与 4.5 的数字并排：两页都出现"十"，但一个是"一条模板消息最多十张卡"，一个是"一条交互式消息最多十张卡"；前者的模板只批准两张，后者没有模板。**数字相同、限制条件不同**，附表 F 的 F10 行登记这两句，正文不把它们写成同一条规则。

# 5 购物车

## 5.1 购物车活在聊天线程与设备上

购物车是这条链路上最容易被想错的对象。总纲页为它写了三条性质，三条都在同一页：

> `A shopping cart: - Is unique to a customer/business chat thread on a specific device: Only one cart is created per chat thread between you and a customer, and carts do not persist across multiple devices.`

第一条就把归属钉死：购物车**属于某个客户与某个商家之间的具体聊天线程**，而且**只在那台设备上**；同一个客户换设备，购物车不在。第二条讲生命周期：

> `Once a cart is sent, the customer can open another cart with you and start the process again. - Has no expiration date: The cart persists in the chat thread until it is sent to you.`

"没有过期日"与"发出即结束"是同一句话的两面：购物车停在会话里，直到客户把它发出去；发出之后这条购物车就结束了，客户可以再开一个新的。第三条是编辑权：

> `Once a cart has been sent, no edits can be made.`

购物车一旦发出就不能再改；客户要加东西，只能重新攒一个：

> `Customers can send a new cart if they need new items or would like to change their order.`

数量和种类的上限也被拆成两层：

> `Customers can add up to 99 units of each single catalog item to a shopping cart, but there is no limit on the number of distinct items that can be added to a cart.`

**单条商品最多九十九件，不同商品种类没有上限**——两个"上限"不在同一个维度上，前一个是每项数量，后一个是物品种类数。商家这一侧只有一条否定句：「You cannot send carts to customers.」——商家不能主动给客户发购物车，购物车只能由客户攒、由客户发。这条否定句与"商家可以主动分享目录"（第 6.3 节）不是一回事：能分享目录不等于能替客户攒购物车。

购物车还会被目录状态改动。总纲页用一个例子写这件事：客户已把某商品加进购物车，商家随后把该商品标成不可售，这一项会从购物车里被移除：

> `If a customer has already added product B to a cart, the item will be removed from the cart.`

客户侧看到的是一个对话框，提示购物车里有项目被更新（这句是界面文案，只作中文陈述）。同一页还说，被判定违规的商品会被标记，商家被引向商务政策；用户可以对收到的具体商品发起举报，举报会被审核（这几句带官方小标题与列表编号，只作中文陈述）。这三件事——商品下架、违规标记、用户举报——都会改变商品在目录里的状态，而购物车读的是目录的当前状态，不是加车那一刻的快照；附表 E 的 E3 行把总纲页与多商品页关于"购物车属于哪一层"的两句并排，读者可以对照。

商家端帮助中心《About Cart》对购物车的写法更"可见"：购物车提供一个让客户开始下单的入口：

> `The cart provides a way for your customers to begin the order process.`

客户从购物车入口改数量、把商品作为一条 WhatsApp 消息发回商家：

> `From there, they can also change the quantity of each item in their cart.`

> `When they’re ready to check out, customers can send the items in their cart to your business account as a WhatsApp message.`

注意最后这一句的主语：客户把购物车里的商品**作为一条 WhatsApp 消息**发给商家账号——购物车的终点不是订单系统里的一个单，而是一条进入会话的消息。这一页还说商家名旁的购物按钮出现在聊天或商家资料里，客户用它浏览目录、从目录加车（这两句是界面描述，只作中文陈述）。商家端与开发者页在这里的口径是一致的：**购物车是客户侧对象，商家只能收，不能发**。

## 5.2 商家端开关：为什么"关掉"之后还有一天

《How to use cart on the WhatsApp Business app》写的是商业 App 里的购物车设置。它的第一句是版本前提：

> `To use this feature, you and your customers must be on the latest version of WhatsApp.`

商家与客户都要用最新版本，这个功能才成立——这是一条**双向**前提，只有商家升级不够。然后是设置生效的延迟：

> `When you change your carts settings, the change may take up to 24 hours to be reflected.`

以及延迟期内会发生什么：

> `If you turn off the carts feature, you may still receive carts from customers in that 24-hour period.`

这两句合起来解释了一个常见的误读：商家把购物车关掉，**不等于立刻收不到购物车**；设置变更本身要等一段时间才反映到客户端，在这段时间里客户仍可能把购物车发过来。附表 F 的 F1 行把这一句与总纲页"购物车没有过期日"并排：一句说购物车本身不设过期日，一句说开关变更有一天的生效延迟——**两者说的不是同一个对象**，前者是购物车这个对象，后者是商家设置这个对象。

# 6 商家端帮助中心

## 6.1 目录挂在商家资料上，必备字段只有三项

《About catalog》是帮助中心对"目录"的定义页。它先说目录显示在哪：商家在商业 App 里建目录，目录显示在商家资料上（这两句是界面描述，只作中文陈述）。然后是必备字段，这一句是本篇与总纲页对照的关键：

> `Each product or service in a business catalog is required to have a unique title, at least one image displaying the product or service, and the country of origin for that product or service.`

**必备三项**：唯一的标题、至少一张展示图、原产国。价格不在必备清单里——这一页把价格放进"也可以包含"的列举，并加了一句限定：「The ability to add prices to a catalog is only available in certain countries.」——给目录加价格的能力只在某些国家可用。这与总纲页的"包含"式列举（2.1）正好互为参照：总纲页列举的是条目**可以**包含的信息，帮助中心页列举的是条目**必须**具备的信息，两句话列举的字段不同、约束强度也不同，不能把"包含"读成"必填"。附表 F 的 F4 行登记这两句。

目录怎么被看见、被分享，这一页连着写了三句：

> `Customers can use WhatsApp to share a business's catalog , including products and services in that catalog, with their friends.`

> `Businesses can share their own catalogs , including products and services in their catalogs, with customers on WhatsApp or social media.`

> `Finally, businesses can connect an existing Meta catalog to the WhatsApp Business app.`

客户可以把商家的目录分享给朋友；商家可以把目录分享给客户或发到社交平台；已经有 Meta 目录的商家可以把现成目录接到商业 App 上。三句分别对应三种主体与三种动作，不是同一件事的三种说法。

## 6.2 目录链接：能分享整本，也能分享一条

《Sharing catalog links》讲目录链接。它的开头先说明目录能做什么：客户可以浏览商品或服务、提问、与商家互动（这句是完整句）。然后是可分享的粒度：

> `To share a product or service from catalog: You can share a link to an individual product or service, or a link to your entire catalog.`

**一个商品一条链接，整本目录一条链接**，两种粒度都支持。链接在应用内打开时直接落到商品或目录：

> `Tap The shared link will directly open the item or catalog if viewed within the WhatsApp Messenger or WhatsApp Business app.`

而在邮件或浏览器里打开时，会先出现一个选择器让用户挑用哪个应用打开。最重要的一句是链接的不可定制性：

> `When shared over email or browser, a selector will allow the user to choose how to open the link. : The auto-generated catalog or item link can't be customized, and may break if you attempt to change it.`

自动生成的目录或商品链接不能自定义，**尝试修改它可能导致链接失效**。这一句解释了为什么"把链接改成自己的短链再发"不是官方支持的用法；本篇因此不写任何链接改写、跳转包装或追踪参数拼接的方法。

## 6.3 主动分享：四种去向，本篇只点名

《How to share products or services with customers》写商家如何主动把商品或服务分享给客户。开篇是一句立场句：

> `Business Tip : Business owners can proactively share products or services with their customers to drive business interactions.`

商家可以主动分享，以此来推动互动。接下来这一页把分享写成一组界面路径：选中商品、在分享菜单里选去向。去向共有四种——用商业 App 转发目录链接、发到 WhatsApp 状态、复制链接、经邮件或第三方应用分享；单个商品另有同样的四种去向（这些句子带界面控件名与占位符，只作中文陈述）。**本篇只点名"四种去向"这个数与去向类别，不重述在哪个页签点哪个按钮、按什么顺序点**；需要动手的读者请回原页。

这里要区分两个"主动"：商家可以主动分享**目录或商品链接**（本节），但商家**不能主动发购物车**（5.1）。两者的差别不在"主动"这个动作，而在对象——目录是商家自己的库存视图，购物车是客户侧的待发送对象。

## 6.4 集合：目录内部的分组，上架前要审核

《How to create and manage a collection in your catalog》讲目录里的"集合"。开篇一句说明它解决什么问题：

> `About ad performance metrics on the WhatsApp Business app Troubleshooting WhatsApp Premium Features WhatsApp Business Platform How to create and manage a collection in your catalog Copy link Android iOS Web Windows More Android iOS Web Windows Creating catalog collections makes it easier for your customers to find the products and services they’re looking for.`

集合让客户更容易找到想要的商品。创建集合有一个前置条件：必须先把商品加进目录（这句是完整句）；集合建好之后，这一页写了一句很多人会忽略的话：

> `This process should take no more than 24 hours.`

上架前要经过审核，审核时长有明确上限。后面还有一句把审核的主体与依据写全：

> `WhatsApp reviews collections to confirm they meet the WhatsApp Commerce Policy before the collections become available to customers.`

**审核依据是商务政策，审核对象是集合，不是单条商品**；通过之后客户才能在目录里看到这个集合（这句是完整句）。这一页剩下的内容（重排集合、增删商品、重排商品、改名、删除）都是界面操作，本篇只登记"这一页覆盖了集合的创建、排序、改名与删除四类界面动作"，不重述步骤。

## 6.5 Shops 店面退场，目录成为默认商务工具

《About Shops integration with the WhatsApp Business app》讲的是商业 App 里 Shops 店面与目录的交替。它先给出一句状态句：

> `Business users are no longer able to display their Shops storefront on the WhatsApp Business app.`

商业用户**不再能在商业 App 里展示 Shops 店面**。取而代之的是目录：

> `Moving forward, the catalog tool on the WhatsApp Business app will be our default commerce tool.`

目录成为商业 App 的默认商务工具。这次变更不影响 Facebook 或 Instagram 上的店面（这句是完整句）。对已经接过店面的商家，这一页写了两条迁移边界：

> `Any items added to your storefront after turning on the integration won’t be migrated to the WhatsApp Business app.`

> `Once you’ve hidden your storefront, none of your storefront items will be available in WhatsApp.`

店面接入之后加进店面的商品**不会被迁移**到商业 App；把店面可见性设为隐藏之后，店面商品在 WhatsApp 里**一项都不会出现**。对原本没有目录的商家，结论更直接：

> `Users who didn't have a catalog before integrating with Shops storefront Your storefront won’t be migrated to the WhatsApp Business app if you didn’t have a catalog before Shops storefront integration.`

没有目录就没有可迁移的东西，官方建议在 WhatsApp 上新建目录（这句是完整句）。这一页因此不是"功能下线公告"，而是**对象替换说明**：店面这个容器退场，目录这个容器接上，两者的内容不会自动互通。

## 6.6 商业 App 与商业平台：两条入口，一个"不想被联系"的出口

《About WhatsApp Business products》是这批帮助中心页里唯一一页把两个产品面并排写的文本。它先说小商家：

> `If you are a small business, you can use the WhatsApp Business app to communicate with customers from a single device through a free-to-download app.`

单设备、免费下载的商业 App。再说它的能力边界：

> `The WhatsApp Business app enables you to reach hundreds of customers, is free to use and offers customization and integration with other tools.`

"数百个客户"是官方原词，属于规模描述，不是上限承诺。然后是中大型商家：

> `If you are a medium to large business, the WhatsApp Business Platform enables you to manage high volumes of conversations on WhatsApp to strengthen purchase consideration and deepen customer relationships.`

平台侧被定义成"管理大量会话"的工具，入口可以直接接，也可以经解决方案提供商接：

> `You can access the WhatsApp Business Platform directly or work with a Business Solution Provider to integrate on your behalf.`

这一页最后一句是给用户的，不是给商家的：「If you'd like to stop a business from contacting you, you can block them.」——不想被某个商家联系，可以拉黑。**这句是用户侧的出口，不是商家侧的操作建议**；本篇把它登记在这里，是为了说明这批页面里"商家能做什么"与"用户能拒绝什么"是分开写的，不能把用户侧的拒绝动作当成商家侧的可配置项。

# 7 订单消息：两类消息与一条状态线

## 7.1 收款入口：订单详情与订单状态

《WhatsApp orders》是这批页面里唯一一页把订单消息的两端写全的文本：一端是商家发给客户的订单详情，另一端是状态变化时的订单状态更新。它开篇定义这两类消息是 WhatsApp 内收款的入口（官方在这一段末尾带了一个列表序号，是版式残句，本篇只作中文陈述，不整句引用）。

订单详情消息带的是收款设置，并**可选**带一个 `order` 对象，里面是逐项商品、费用与折扣（这句带接口字段名，只作中文陈述）。不带 `order` 对象时，官方允许发一个简化版订单详情，只放总额与收款设置；这一页还替它写了适用场景：

> `The simplified order details message is useful when you don't need to send itemized product details and only need to collect the total payment amount.`

反过来，订单状态消息是**状态变了才发**：依据可以是 WhatsApp 的支付状态变更通知，也可以是商家自己的内部流程；同样允许发不带 `order` 对象的简化状态更新（这两句是完整句，只作中文陈述）。两类消息的骨架相同——页头、正文、页脚、动作四个组成部分；不同的是动作里的参数随消息类型变化（这一句带斜体强调与列表，只作中文陈述）。

两类消息的正文都支持表情、Markdown 与链接：

> `Emojis, markdown, and links are supported.`

这是本页对正文排版唯一的正面许可，它没有把"支持 Markdown"写成"支持任意长度的 Markdown"，也没有写任何与批量投递有关的用法；本篇照原文只记这一条。

## 7.2 状态线：待付款是起点，完成态是终点

订单状态从哪开始、什么时候结束，官方在订单页用两句分开写。一处是：

> 「If the order details message includes the `order` object, WhatsApp also moves the order to `completed`, which is a final status.」

另一处是：

> 「When attached to an order details message, orders start in `pending` status.」

第一句把带 `order` 对象的订单详情推进到完成态，并说明完成态是**终态**；第二句把默认起点钉在待付款。两句不矛盾：起点说的是订单刚被这条消息创建时的状态，完成态说的是商家履约完毕之后必须标成的状态。附表 F 的 F6 行把这两句并排，本篇按"默认起点＋条件推进"来写，不把它读成互斥的两个终态。附表 E 的 E13 行登记的是起点那一句——该表"订单状态的起点"一列只有这一格有原句，其余各列都标作官方未按这一列写该项，说明这不是一处遗漏，而是这一批页面里只有订单页写过起点。

完成态由谁判定也写在同一页：商家已完全履约、买方不应再期待任何后续更新时，订单必须标为完成态。

> 「When the merchant has fully fulfilled the order and the buyer should not expect any further updates, the order must be marked as `completed`.」

## 7.3 简化版：没有逐项明细时发什么

简化版订单详情不是"精简排版"，而是一个能力边界：它不支持图片页头。

> `Simplified order details messages do not support an image header.`

> `Instead of the thumbnail image, the total payment amount is displayed prominently at the top of the message.`

省掉缩略图之后，总额被提到消息顶部——两句话合起来说明简化版把"图"换成了"数"。因此简化版适合只有总额、没有逐项商品的收款场景；有逐项商品时该用带 `order` 对象的那一版（7.1）。

## 7.4 字段边界：字符长度、去重与费用结构

订单详情里有一个字段被官方写了明确长度：它不能是空字符串，只能由英文字母、数字、下划线、短横线与点组成，且不得超过 60 字符。

> `This cannot be an empty string and can only contain English letters, numbers, underscores, dashes, or dots, and should not exceed 60 characters.`

这一句同时是附表 B 里来自本页的唯一一行（B 表全表六行，其余五行分别来自多商品页、模板轮播页与两页帮助中心）。同一个字段还有一条去重规则：同商家的每条订单详情消息里它必须唯一（这句是完整句，只作中文陈述）。长度与唯一性合起来说明这个字段的用途是**标识一条订单详情**，不是给客户看的文案。

费用结构这一页写得比消息结构细：金额以整数表示，并按偏移量换算成货币单位；税的对象是必需项，但金额可以为零，金额为零时客户端不渲染税行；折扣的金额应小于原金额，并参与小计的计算；订单带激励时，商家必须自行定义这部分信息（这几句含接口字段名与数值示例，本篇只作中文陈述，不转述任何示例值）。本篇不把这些字段拼成一份"费用清单模板"：官方写的是每个字段的约束，不是一份可以直接套用的结构。

## 7.5 支付按钮的过期：谁负责禁用

订单页对支付按钮的过期写得很干脆：

> `The CTA for payment will be disabled after expiry on the user end.`

用户端的按钮在过期后被禁用。这句话单独看像是"系统自动处理"，但另一页（8.2）在讲支付请求模板时写下了相反方向的一句；两页的差异与收录方式见附表 F 的 F5 行，本篇不在这里合并。

订单进入已付款状态后，订单气泡会多出一个标记：

> `Receiving this payment status will update the order bubble to include the "paid" label (with green checkmark).`

这一句说明"已付款"是**由支付状态回报驱动**的界面变化，不是商家手动改的标签。本篇不引申任何对账、退款或结算流程——这一批页面没有写这些。

# 8 订单详情模板与收款请求模板

## 8.1 巴西版订单详情模板：类别可选，页头三选一

《Send order details template (Brazil) - WhatsApp》讲的是巴西地区的订单详情模板。它的起点是模板必须先建好并获批：

> `Once you create and approve your order details message templates, you can use the approved template to send the template message with order or bill information to prompt customers to make a payment.`

发送前还有一个前置动作：模板里要带一个"订单详情"类型的行动按钮。

> `Before sending an order details template message, create a template with an "order details" call-to-action button.`

页头可选三种媒体类型——文本、图片或文档（这句带界面字段名，只作中文陈述）。文档页头还有一条格式要求：如果用链接方式提供，路径必须以 PDF 后缀结尾（这句带接口字段名，只作中文陈述）。本篇只按官方列出的三种页头类型写，不推断视频可以用于订单详情模板的页头——这一条已登记在附表 F 的 F12 行。

类别是这一页与印度页最大的分歧点：

> `Order details template can be categorized as "Utility" or "Marketing" template and apart from 'name' and 'language' of choice, the template has general template components such as HEADER, BODY, FOOTER, and a fixed BUTTON with "ORDER_DETAILS" type.`

巴西版把订单详情模板归为**可选**实用或营销。印度版（9.1）写的是固定实用；两页说法不同，附表 F 的 F8 行登记这一对，附表 E 的 E12 行也把这一句放进"模板类别"一列。本篇按地区分别写，不把一地说法套到另一地。

同一页还说，上面关于模板创建的各项规则同样适用于 API 通道：

> `All the guidelines outlined above in creating templates apply through API as well.`

这条"界面与接口同一套规则"的表述，是这批页面里少见的把两条入口写成一份规范的句子。

模板发出去之后，渲染出来的样子是一张带逐项商品、支付方式与行动按钮的卡片（这句是完整句）；消息 ID 可以用来跟踪发送状态（这句带接口字段名，只作中文陈述）。

## 8.2 收款请求 CTA 模板：按钮有限，过期不自动禁用

《Payment request CTA templates (Brazil)》讲的是带收款请求按钮的模板。它先列出支持的支付方式：Pix 动态码、Boleto、支付链接与一键支付四种（这句是带列表的段落，只作中文陈述）。创建这类模板需要一个带消息账户的商业资产组合（这句是完整句）。

模板组件与订单详情页相同：页头、正文、可选页脚，页头同样只能在文本、图片、文档里选一种（这句带界面字段名，只作中文陈述）。示例值只是演示用途，真正的支付码或 URI 由每次客户交互生成并放进发送载荷（这两句是完整句）。

有一个结构性上限值得单独写：带收款请求按钮的模板共支持三个按钮，官方要求**替换**请求体示例里的某个按钮，而不是再加第四个（这句带接口字段名，只作中文陈述）。本篇只记"总数是三个、加一个就要换掉一个"这个结构事实，不重述请求体怎么改。

支付方式的跳转写法里，Pix 有两种：切到银行 App 授权后回到 WhatsApp，或复制 Pix 码到银行 App 粘贴（这句带列表，只作中文陈述）。带跳转的 Pix 官方写着"将作为完整公开功能很快发布"：

> `Pix with redirect will be released as a full public feature soon.`

这一句是时态声明，不是可用性承诺；本篇不把它读成"已经可用"。

最后是本节标题里说过的那个分歧点。先看官方对模板按钮存活状态的描述：即使限时优惠已经显示为过期，收款请求按钮在会话里仍然保持可用（这句是完整句）；接着是那句关键原文：

> `WhatsApp does not automatically disable the payment button when the Pix code, Boleto, or Payment Link is no longer valid.`

Pix 码、Boleto 或支付链接失效后，WhatsApp **不会自动**禁用支付按钮。这与订单页"用户端的支付按钮在过期后会被禁用"（7.5）方向相反。附表 F 的 F5 行把两句并列登记，本篇的写法是：**两处原文各按所在页面语境引用，不合并成一句"按钮会／不会被禁用"**；读者在自己的模板与地区页上应以该页原文为准。这一条也解释了为什么本篇不写任何"过期后怎么处理按钮"的操作建议——官方没写谁该做什么，只写了两处不一致的现状。

# 9 印度版订单模板与订单状态模板

## 9.1 印度版订单详情模板：类别固定为实用

《Send order details template (India) - WhatsApp》与第 8 章那一页是同一件东西的另一地区版本。起点相同：模板先建好并获批，才能用获批的模板发出带订单或账单信息的消息，提示用户付款。

> `Once your Order details message templates have been created and approved, you can use the approved template to send the template message with order or bill information to prompt the WhatsApp user to make a payment.`

发送前的动作也相同——模板要带一个呼叫行动按钮。差别在按钮名：

> `Before sending an order details template message, businesses need to create a template with an "Open order details" call-to-action button.`

巴西版叫"订单详情"，印度版叫"打开订单详情"；按钮上的文字也不同，印度版固定为"查看并付款"（这句带界面字段名，只作中文陈述）。创建路径更窄：印度版只能选实用类别来展开"订单详情消息"选项（这句带界面字段名，只作中文陈述）。

类别就是两页真正的分歧：

> `Order details template is categorized as "Utility" template and apart from 'name' and 'language' of choice, it has general template components such as HEADER, BODY, FOOTER, and a fixed BUTTON with "ORDER_DETAILS" type and "Review and Pay" text.`

印度版把订单详情模板**固定归为实用**；巴西版（8.1）写的是可选实用或营销。附表 F 的 F8 行登记这一对。本篇的处理是：按地区分别写，**不把一地说法套到另一地**，也不去猜哪一说"更新"——官方没有写修订关系，只写了两套地区口径。

接口侧与巴西版一样用消息端点加模板对象，模板对象里带预定义的"打开订单详情"按钮（这两句带接口字段名，只作中文陈述）；示例载荷以一个本地支付类型为例（这句带接口字段名，只作中文陈述）。页末同样用消息 ID 跟踪状态（这句带接口字段名，只作中文陈述）。这一页与巴西版共享同一句"界面规则同样适用于 API"：

> `All the guidelines outlined above in creating templates apply through API as well.`

## 9.2 订单状态模板：取消与付款是一对闭环限制

《Send order status template message》补上了订单页欠的那一半：状态变化之后商家该做什么。它的开篇是一句义务句：

> `Upon receiving the payment signals, the businesses must update the order status to keep the user up to date.`

收到支付信号，商家**必须**更新订单状态。这句话的主语是商家，不是系统；它把"状态是商家的责任"从订单页的隐含前提变成了明写。

这一页的类别没有分歧：订单状态模板归为实用模板，组件是正文、页脚，外加一个"订单状态"子类别（这句带界面字段名，只作中文陈述）。接口规则同样适用于 API：

> `All the guidelines outlined above in creating templates apply through API as well.`

模板发出去之后例子讲的是发货通知：一份样例说明如何在已下单的订单上发"已发货"状态（这句带状态名，只作中文陈述）。

本节最重要的是两条关于取消的限制。第一条是客户侧：

> `The customer cannot pay for an order that is canceled.`

第二条是商家侧：

> `An order can be canceled only if the user has not already paid for the order.`

两句合起来是一个闭环：**已取消的订单，客户不能再付款；已付款的订单，商家不能再取消**。附表 F 的 F7 行把这两句并排，本篇按原文把两条并排写成一个闭环，不引申退款、撤销或资金处理规则——这一批页面没有写这些。

取消之后界面上会发生什么，这一页也写了：客户收到订单状态消息，订单详情页显示为已取消，并移除"继续"按钮（这句带控件文案，只作中文陈述）；"订单已取消"下方那段可选说明文字，由订单状态消息的描述字段提供（这句带接口字段名，只作中文陈述）。如果商家在客户已付款之后仍发取消状态，官方写的是会收到一条错误回调（这句带错误码与提示文本，且错误码是一个官方数字；本篇不转录错误码本身，只记"已付款后再取消会失败"这一结果，错误码请回原页核对）。**这一条是"闭环限制"的兜底证据**：不只客户侧付不了，商家侧也取消不了，系统会在回调里拒绝。

# 10 订单消息的回调：手机号不是必然存在的字段

## 10.1 webhook 参考页唯一展开的横向前提：用户名

《Order messages webhook reference》是订单消息 webhook 的字段参考页。这一页可入池的整句只有五句，其中四句都在讲同一件事：用户采用用户名之后，号码字段的存废。这不是订单业务本身的规则，而是订单回调能不能被正确消费的前提。

先看一句正面表述：

> `Included whether or not the user has adopted a username.`

这个字段无论用户是否采用用户名都会被包含。与之相对的是号码类字段：

> `Omitted if the user has adopted a username and their phone number is unavailable.`

用户采用用户名、且号码不可得时，号码字段被省略。这一页对这一条写了两遍（对应两个不同字段），措辞一致。总述句把后果说得很直接：`wa_id` 与 `from` 在同样条件下会被省略，只依赖手机号的集成将无法处理这些消息（这句带接口字段名，只作中文陈述）。

五句里剩下的那一句讲的是另一个字段的取值来源：它等于发送消息时 API 返回的 `input` 值（这句带接口字段名，只作中文陈述）。**这一页没有任何一句讲订单状态的字段结构**——它只写"一个订单消息的回调长什么样"里与身份标识有关的部分，状态推进完全交给第 7 章与第 9 章那两页。这个分布本身就是本篇要提醒的结论：回调的字段完整性有一半取决于用户是否采用用户名，而不是取决于订单本身。

# 11 收单之后：回调分流、帮助中心与用户侧出口

## 11.1 问询与下单走两种 webhook

《Receive responses from customers》是这批页面里唯一一页站在"商家收到什么"这一侧写的。它的开篇把两种客户动作分开：客户收到单商品或多商品消息之后，可以就某个商品追问更多信息，也可以直接下单；这两类动作**走不同的通道**——问询落在消息 webhook 上，订单落在订单 webhook 上（这一段把 url 路径写在行内，只作中文陈述）。

这一页只有一句可入池的整句，就是订单通道要用这些内容做什么：

> `Use the order contents to fulfill the order.`

**用订单内容去履约**——这是全页唯一的义务句，也是订单 webhook 存在的目的。页面对"追问"这一侧写得更细：客户可以回复一条文字消息，也可以在看某个商品时点"联系商家"或"消息"按钮；这类消息的上下文属性会带一个对象，描述用户点按钮时正在看的商品（这几句带按钮文案与接口字段名，只作中文陈述）。页面对发送状态另有一句：状态 webhook 报告已发送、已送达、已读（这句带状态枚举，只作中文陈述）。

把这三段放在一起，得到的结构是：**商品消息发出去之后，回来的东西分三股——状态回执、追问、订单**，每一股有自己的 webhook。本篇不写怎么区分、怎么路由，那是集成侧的事；本篇只记官方把三股写在同一页、并明确订单那一股要拿去做履约。

## 11.2 帮助中心三页同名：一页有正文，两页只有地区提示

帮助中心里有三页标题都落在"订单详情"上：《About using Order Details》《About providing customers with Order Details or Order Summaries》《How to create order details》。三页属于同一组，但能读到的内容差别很大。

《About providing customers with Order Details or Order Summaries》有完整正文。它先说清这个功能的定位：订单详情／订单摘要功能是给商家向客户提供订单摘要用的；商家对其中信息的强制、正确与真实负责，清单包括用户信息、商家信息、所购商品或服务的描述、购买日期、单项价格、总价与支付信息（这一段是带列表的段落，只作中文陈述）。然后是这一页最要紧的一句：

> `Businesses are solely responsible for issuing an invoice or such other statutory document as required under applicable laws.`

订单详情／订单摘要**不是发票的替代品**，法定单据由商家自己出。这一句把"在 WhatsApp 里下单"与"完成一项法定交易文件"分开：前者是会话里的消息，后者是法律义务，两者不能互相顶替。

另外两页没有可读正文：取回的页面上只有站内导航、地区提示与页脚，正文区域没有产出任何可入池的完整正文句。因此本篇对这两页只登记标题与"只对巴西用户可见"这类地区提示，**不引用它们的任何句子，也不据它们推断官方没写**——页面没取到与官方没写是两回事。附表 A 会把这两页标成正文未引用，读者可以据此判断本篇的覆盖率不是百分之百。

## 11.3 用户侧的出口：连接、举报与拉黑

《How to report a catalog or business》是这批页面里唯一一页从**用户**视角写的。它开篇先排除一种误解——商家不回，先怀疑自己的设备：

> `If you haven’t received a response after reaching out to a business, it may be an issue with your device’s connection.`

设备连接正常、商家仍不回复，或者用户认为商家违反了商业消息政策，就可以举报：

> `If your device is connected and the business still hasn’t responded, or you believe a business is violating the WhatsApp Business Messaging Policy , you can report them.`

这一页后面的内容是两类举报的界面路径：举报商品或服务、举报商家账号；两类都要求用户选一个提交理由，一类以提交结束，另一类可以连带拉黑（这些句子带按钮文案与勾选框名称，本篇只点名"两类举报、每类各需选择一个理由"，不重述点击顺序，需要动手的读者请回原页）。

把这一页与 6.6 最后那句并排看，用户侧有两个出口：不想被联系就拉黑，认为违规就举报。**这两个动作都不在商家可控的配置里**，商家能做的只是不给出触发它们的理由。本篇不写任何"如何避免被举报"或"被举报后怎么处理"的内容——这一批页面没有写，写出来就是本篇替官方编规则了。

# 12 横向对照：把散在各页的口径并起来

## 12.1 三个对象的归属：目录、购物车、订单

这条链路上有三个容易混的对象，官方把它们分得很清：目录是商家的库存视图，商家可以主动分享目录与商品链接（6.3）；购物车是客户侧的待发送对象，商家**不能**主动发给客户（5.1）；订单消息是商家在收款环节发出的消息，客户下单走的是订单 webhook（11.1）。

归属决定动作：商家能推的是**自己的库存视图**，不能推的是**客户的待发送对象**；客户能发起的是**购物车与下单**，商家能发起的是**目录分享与订单状态更新**。这些不是本篇的归纳，而是每一页各自写的一句话；本篇把它们并排，是因为单看任何一页都会漏掉另外两个对象。

## 12.2 模板类别的三处写法

模板类别在本轴出现了三次，三次都不一样：

- 巴西版订单详情模板：可选实用或营销（8.1）；
- 印度版订单详情模板：固定实用（9.1）；
- 订单状态模板：固定实用，另有"订单状态"子类别（9.2）。

三处都不是"错"，而是**地区与模板类型共同决定**的口径：同一个"订单详情"按钮，在巴西可以挂在营销模板上，在印度只能挂在实用模板上；而订单状态模板两地都只有实用一档。本篇不从这里推导任何"最佳类别选择"——类别的判定权在官方，不在读者也不在本篇。

## 12.3 数字都落在哪几张表

先说一个容易记反的：**约束不等于数字**。订单页里有一个费用对象被标为必需，但官方同时写明它的金额可以为零：

> `Even though the object is required, the amount can be zero.`

金额为零时客户端不渲染那一行（这句是完整句）。所以"必需"约束的是字段的存在，不是数值的大小；把它读成"必须有金额"就错了。

真正进表的是另外两类：文本规格与时长／数量上限。它们全部进了附表 B，并写清所在页面与句序，没有一处数字只出现在叙述里而不进表。B 表的性质一栏分四种：上限、下限或触发门槛、时长、规模计数——同一个"24 小时"在本轴出现了三种语境（开关延迟、延迟期内仍可能收到、集合审核），性质并不因为它们数值相同就相同。

另有一组数字是**本篇自己数出来的**：32 个登记页面、291 条整句、27 个对照维度 × 5 列共 135 格、附表 F 的 12 个议题。这些不是官方数字，它们的作用是让读者知道本篇读了多少、漏了多少，不能当成官方的规模或上限来引用。

# 13 结论与边界

## 13.1 这条链路的口径边界

把本轴读成一句话：**WhatsApp 的商业目录与订单是两套对象、两种消息、两条回调**。目录与购物车围绕"客户看什么、攒什么"，订单与支付模板围绕"商家收什么、什么时候更新状态"；两者的交集只有一个——客户从购物车发出商品作为一条消息，商家收到后可以回以订单消息。

这条链路里有三处官方写得最细、也最容易被读错的地方：购物车不设过期日但只存在于具体设备上（5.1）；支付按钮在过期后，一处说用户端会禁用、一处说不会自动禁用（7.5 与 8.2，附表 F 的 F5 行）；订单详情模板的类别巴西可选、印度固定（8.1 与 9.1，附表 F 的 F8 行）。

## 13.2 本篇的边界

本篇只写官方文档在这一批页面里写下的内容。不含批量发送的方法、不含发送队列与并发参数、不含账号池与多号轮换、不含注册与验证的操作步骤、不含限速绕行、不含垃圾信息检测与风控绕过、不含签名号与模板内容代写、不含非官方客户端。凡官方页面写成界面路径的，本篇只点名对象与数量，不重述步骤；凡官方没写的空白，本篇登记在附表 F，不替它补结论。

这一批登记页面里，有两类页面的处理方式不同，读者需要知道：一类是 11.2 的两页帮助中心，取回的页面上只有导航与地区提示，本篇在正文里只登记标题、不引用任何正文句；另一类是 1.3 的分享页，它的正文没有产出任何可入池的整句，所以附表 B 里没有它的行。两类页面都在附表 A 里逐页点名——读者要判断本篇的覆盖是否充分，看附表 A 比看本章更可靠。

# 附表 A 官方页面登记清单与本篇引用位置（33 行）

登记口径与系列前篇一致：**凡本篇抓取的页面，一律登记**。全表 **32** 个登记位，按站点分为 Meta 开发者文档 20、WhatsApp 帮助中心 12。取回状态由脚本实测各层正文的非空白字符数得出，分档统计：已取回 32。其中**可引用 32 页、不可引用 0 页**：本轮登记页正文全部取回，没有需要标注「全篇不引用」的页面。

帮助中心页的正文取回长度按裁剪后正文计：抓取原始页含平台页签（Web／Windows／Mac／iOS／Android）与固定尾栏，这些行不计入长度，也不作为引文出处。Meta 商业帮助中心的页面正文从页面内嵌的转义 JSON 载荷里抽取，抽取残骸（单独的 u 记号与 DOM 类名）整行剔除，不做猜测修补，被剔除的行也不作为引文出处。

第 4 列「本篇引用位置」的小节号是实测的，不是事先规划的：由 _tmp/wa6_postitle.pl 扫成稿分片里的页名标记（正文每点名一次某页的官方标题就留下一枚这种内部标记，展开后成为标题）得出，写进 _tmp/w6_a7_postitle.tsv，再由 _tmp/wa6_pos.pl 拼进这一列；判据与 _tmp/wa6_cover.pl 同口径——**这一列只写标题点名的位置，所以按小节号翻过去一定能撞见这一页的官方标题**。章首（未进入任何小节的章引言）写作「第 N 章章首」；某页只在别处被整句引用而标题未点名的节不出现在这一列，那种句子按附表 C—F 的句序回查。

| 站点 | 页面标识 | 官方标题 | 本篇引用位置 | 正文状态 |
| --- | --- | --- | --- | --- |
| Meta 开发者文档 | `catalogs_catalog_link_messages_md` | Catalog link messages | 正文 3.2、附表 C（3 处）、附表 E（1 处） | 已取回（正文 562 字符） |
| Meta 开发者文档 | `catalogs_catalog_messages_md` | Catalog messages | 正文 3.1、附表 C（7 处）、附表 E（2 处） | 已取回（正文 3721 字符） |
| Meta 开发者文档 | `catalogs_catalog_template_messages_md` | Catalog templates | 正文 4.1、附表 C（2 处）、附表 E（1 处）、附表 F（1 处） | 已取回（正文 8290 字符） |
| Meta 开发者文档 | `catalogs_catalogs_overview_md` | Catalogs overview | 正文 2.1、附表 C（13 处）、附表 E（5 处）、附表 F（3 处） | 已取回（正文 5438 字符） |
| Meta 开发者文档 | `catalogs_interactive_product_carousel_messages_md` | Interactive product carousel messages | 正文 4.5、附表 C（4 处）、附表 E（2 处）、附表 F（1 处） | 已取回（正文 4658 字符） |
| Meta 开发者文档 | `catalogs_mpm_template_messages_md` | Multi-product message templates | 正文 4.3、附表 C（2 处）、附表 E（1 处） | 已取回（正文 13552 字符） |
| Meta 开发者文档 | `catalogs_multi_product_messages_md` | Multi-product messages | 正文 3.4、附表 B（1 处）、附表 C（8 处）、附表 E（3 处）、附表 F（2 处） | 已取回（正文 8137 字符） |
| Meta 开发者文档 | `catalogs_product_card_carousel_template_messages_md` | Product card carousel templates | 正文 4.4、附表 B（1 处）、附表 C（4 处）、附表 E（4 处）、附表 F（1 处） | 已取回（正文 11283 字符） |
| Meta 开发者文档 | `catalogs_receive_responses_md` | Receive responses from customers | 正文 11.1 | 已取回（正文 1383 字符） |
| Meta 开发者文档 | `catalogs_set_commerce_settings_md` | Set commerce settings | 正文 2.3、附表 C（1 处） | 已取回（正文 3507 字符） |
| Meta 开发者文档 | `catalogs_share_products_md` | Share products with WhatsApp users | 正文 1.3 | 已取回（正文 2454 字符） |
| Meta 开发者文档 | `catalogs_single_product_messages_md` | Single-product messages | 正文 3.3、附表 C（1 处）、附表 E（1 处） | 已取回（正文 6511 字符） |
| Meta 开发者文档 | `catalogs_spm_template_messages_md` | Single-product message templates | 正文 4.2、附表 E（3 处） | 已取回（正文 8587 字符） |
| Meta 开发者文档 | `catalogs_upload_inventory_md` | Upload inventory to Meta | 正文 2.2 | 已取回（正文 1620 字符） |
| Meta 开发者文档 | `payments_payments_br_orderdetailstemplate_md` | Send order details template (Brazil) - WhatsApp | 正文 8.1、附表 C（3 处）、附表 E（1 处）、附表 F（2 处） | 已取回（正文 7604 字符） |
| Meta 开发者文档 | `payments_payments_br_orders_md` | WhatsApp orders | 正文 7.1、附表 B（1 处）、附表 C（5 处）、附表 D（1 处）、附表 E（2 处）、附表 F（3 处） | 已取回（正文 16584 字符） |
| Meta 开发者文档 | `payments_payments_br_payment_request_cta_md` | Payment request CTA templates (Brazil) | 正文 8.2、附表 C（5 处）、附表 E（1 处）、附表 F（1 处） | 已取回（正文 13475 字符） |
| Meta 开发者文档 | `payments_payments_in_orderdetailstemplate_md` | Send order details template (India) - WhatsApp | 正文 9.1、附表 C（1 处）、附表 F（1 处） | 已取回（正文 8400 字符） |
| Meta 开发者文档 | `payments_payments_in_orderstatustemplate_md` | Send order status template message | 正文 9.2、附表 C（1 处）、附表 E（1 处）、附表 F（2 处） | 已取回（正文 6483 字符） |
| Meta 开发者文档 | `webhooks_reference_messages_order_md` | Order messages webhook reference | 正文 10.1 | 已取回（正文 7884 字符） |
| WhatsApp 帮助中心 | `faq_1060909311260819` | About WhatsApp Business products | 正文 6.6 | 已取回（正文 2160 字符） |
| WhatsApp 帮助中心 | `faq_1184376605821468` | About Cart | 正文 5.1、附表 C（7 处）、附表 E（2 处） | 已取回（正文 1403 字符） |
| WhatsApp 帮助中心 | `faq_1232344090659656` | About using Order Details | 正文 11.2 | 已取回（正文 578 字符） |
| WhatsApp 帮助中心 | `faq_1249073475657215` | About providing customers with Order Details or Order Summaries | 正文 11.2 | 已取回（正文 1362 字符） |
| WhatsApp 帮助中心 | `faq_1314318822729539` | About Shops integration with the WhatsApp Business app | 正文 6.5、附表 C（3 处）、附表 E（1 处） | 已取回（正文 2020 字符） |
| WhatsApp 帮助中心 | `faq_1475761576240237` | How to use cart on the WhatsApp Business app | 正文 5.2、附表 B（2 处）、附表 E（1 处）、附表 F（1 处） | 已取回（正文 1025 字符） |
| WhatsApp 帮助中心 | `faq_1498078847370903` | How to create order details | 正文 11.2、附表 C（1 处） | 已取回（正文 586 字符） |
| WhatsApp 帮助中心 | `faq_2133933800112087` | How to report a catalog or business | 正文 11.3、附表 C（3 处） | 已取回（正文 1406 字符） |
| WhatsApp 帮助中心 | `faq_2929318000711140` | How to create and manage a collection in your catalog | 正文 6.4、附表 B（1 处）、附表 C（2 处）、附表 E（1 处） | 已取回（正文 2605 字符） |
| WhatsApp 帮助中心 | `faq_405903568419894` | About catalog | 正文 6.1、附表 C（4 处）、附表 E（2 处）、附表 F（1 处） | 已取回（正文 1974 字符） |
| WhatsApp 帮助中心 | `faq_487917009931629` | Sharing catalog links | 正文 6.2、附表 E（1 处） | 已取回（正文 2201 字符） |
| WhatsApp 帮助中心 | `faq_501451005213099` | How to share products or services with customers | 正文 6.3、附表 E（1 处） | 已取回（正文 1866 字符） |
| **合计** | **32** 个登记位 | 可引用 **32** 页／不可引用 0 页 | Meta 开发者文档 20、WhatsApp 帮助中心 12 | 不可引用页全篇不引用 |

#ROWS=32 COLS=5


# 附表 B 数字归档五档（7 行）

归档口径：**本表一行一个「数字＋它数的东西＋出处页与句序」**，数字、单位词与整句都由脚本从语料里实测取出——输入是原句池的 **291** 条整句（覆盖 **31** 个页面，池子已剔除含竖线的「正文黏表格」混合句），脚本在每句里找出所有独立数字，取该数字后面最多四个词，档位由这四个词里第一个能对上单位表的词决定；同页同数字同单位只留一行（共去重 **1** 行）。全表 **6** 行，分布在 **5** 个页面上，去重后的「数字＋单位」组合 **5** 组，单位词 **5** 种。五档分布：一、账号、号码与设备规模档位 1／三、时长与滚动窗口 3／二、验证要素与文本长度规格 1／五、速率、次数与比率口径 1。

三条不成文规矩在这一表里最要紧：**同一个数字在两个页面各写一处，就各登记一行**，不合并成区间；**官方写成区间、多档或带限定词的照原样登记**（`up to`、`about`、`approximately`、`in some countries` 一类措辞原样保留），不取中值、不去掉约数；**档位与性质由脚本按关键词判定，性质判不出来就中止出表**，所以本表没有「其它」这一档。

本表也写清了自己漏掉了什么：池子里 **250** 句整句不含数字，脚本共扫出 **13** 个「数字后面四个词都对不上任何一档」的实例，全部连同它所在的整句写进 `_tmp/w6_a7_bdrop.tsv` 供复核，不进表；这一批绝大多数是地址、法条编号与接口示例对象 ID，不是官方对某项业务写下的数字，**漏掉的判定权在读者手里，不在这张表里**。另有 **1** 个可引用页面在池子里一句都没有——这一页的正文没有产出任何可入池的整句，官方数字要进本表只能连同表格 markup 一起取，那类混合句已在建池时剔除，所以这一页在本表里是空的，在附表 A 已逐页点名。

官方原句超过 118 字符按整词截断并以省略号结尾，共 **2** 行截断；截断只发生在表格里，进表的每一句都先用未截断的整句逐字回表到`_tmp/w6_a7s/` 的对应页面，失败 **0** 句（回表与建池共用同一套规范化，所以这里一旦非零就是池子与抓取件不同步，脚本直接中止不出表）。性质判定为 **4** 种：上限（官方写成 up to／maximum 一类） 3／下限或触发门槛 1／时长（官方只写数值与单位） 1／规模计数 1。

| 档 | 数字 | 计数单位（官方原文） | 官方原句 | 性质 | 出处页 |
|---|---|---|---|---|---|
| 一、账号、号码与设备规模档位 | **2** | parameters | 「In this case: - The API sends the message successfully - The API drops items without a match - You receive an error…」 | 规模计数 | `catalogs_multi_product_messages_md`｜第 33 句 |
| 五、速率、次数与比率口径 | **10** | template | 「An approved template with two product cards can be used to send up to 10 cards in a template message.」 | 上限（官方写成 up to／maximum 一类） | `catalogs_product_card_carousel_template_messages_md`｜第 11 句 |
| 三、时长与滚动窗口 | **24** | hours | 「When you change your carts settings, the change may take up to 24 hours to be reflected.」 | 上限（官方写成 up to／maximum 一类） | `faq_1475761576240237`｜第 7 句 |
| 三、时长与滚动窗口 | **24** | hour | 「If you turn off the carts feature, you may still receive carts from customers in that 24-hour period.」 | 时长（官方只写数值与单位） | `faq_1475761576240237`｜第 8 句 |
| 三、时长与滚动窗口 | **24** | hours | 「This process should take no more than 24 hours.」 | 上限（官方写成 up to／maximum 一类） | `faq_2929318000711140`｜第 13 句 |
| 二、验证要素与文本长度规格 | **60** | characters | 「This cannot be an empty string and can only contain English letters, numbers, underscores, dashes, or dots, and…」 | 下限或触发门槛 | `payments_payments_br_orders_md`｜第 31 句 |
| **合计** | **6** 行 | 单位词 **5** 种 | 去重后数字＋单位 **5** 组，覆盖 **5** 页 | 性质 **4** 种 | 第四档＝日期与版本节点 |

#ROWS=6 COLS=6


# 附表 C 官方用词与写下它的站点类别（80 行）

取格口径：脚本按第一、二列的 **61** 个词（组）（分属 8 个主题组：A　目录与库存本体 14／B　消息形态与动作 12／C　购物车与结账 5／D　模板与按钮 5／E　订单与状态 9／F　支付方式与码 6／G　审核、政策与责任 4／H　接口与信号 6）逐句扫过原句池的 **291** 条整句（覆盖 **31** 个页面），每命中一次就落进「词 × 站点类别」的格子；**一行一个非空格**，全表 **79** 行，各格命中句次相加是 **617** 次，而去重后真正被任一词命中的句子是 **250** 句（占全池 85.9%）。同一句可以落进多格：本轮一句落进最多格子的是 `catalogs_product_card_carousel_template_messages_md` 池内序号 9，同时命中 10 个词（组）——「URL button（网址按钮）」、「button（按钮）」、「card（卡片）」、「carousel（轮播）」、「catalog」、「inventory」、「order（订单）」、「product（商品）」、「template（模板）」、「webhook」；全池有 171 句同时落进两个以上的格子。所以句次合计 **617** 大于去重句数 **250**，这两个数各自回答「这一格有多少句」与「整池有多少句被扫中」，不矛盾。

词表本身也是这一篇的一部分：它写在 `_tmp/w6_a7_verb.tsv` 里，脚本不内置任何选题词，换一篇只换词表、判定规则不变。**正则由脚本逐条编译，编译不过就中止出表**，不存在「写错的正则悄悄扫出零句然后当成官方没写过」。

代表句一列取该格在池子里最先出现的一句，取句规则写死在脚本的遍历顺序里，**不是按哪句更清楚来挑**。「格内有句写明数值」只回答这一格有没有句子自带天数、小时、位数、百分比或规模数——有则记「有」，没有就记「无」，不在这里把别的页面或别的站的数字补进来；跨站对撞属于附表 F。

词零命中的情况：61 个词（组）里 **0** 个在整池一句都没出现（无）。零命中不等于官方没有这一项内容，只说明本篇登记的这批页面没有用这个词写过。

本表的英文原句全部逐字回表：291 条整句每一条都在 `_tmp/w6_a7s/` 的对应页面正文里按规范化后的子串核到，失败 **0** 句（判定与建池用同一套实体还原与空白折叠，见 `_tmp/wa6_lib.pl` 的 ck）。官方原句超过 130 字符按整词截断并以省略号结尾，共 **47** 行截断；截断只发生在表格里，回表核验用的是未截断的整句。各站点命中句次合计：Meta 开发者文档 459／WhatsApp 帮助中心 158／Meta 商业帮助中心 0／法律与政策页 0。

| 主题组 | 官方词（组） | 写下它的站点类别 | 命中句次 | 命中页数 | 代表句（该格池内第一句） | 格内有句写明数值 | 出处页｜句序 |
|---|---|---|---|---|---|---|---|
| A　目录与库存本体 | catalog | Meta 开发者文档 | **22** | 10 | 「When sending a text message, you can use the optional `preview_url` set to `true` to have the message render a set of product…」 | 有 | `catalogs_catalog_link_messages_md`｜第 2 句 |
| A　目录与库存本体 | catalog | WhatsApp 帮助中心 | **34** | 7 | 「Using this shopping button, customers can browse your catalog and add items to their cart from your catalog.」 | 无 | `faq_1184376605821468`｜第 5 句 |
| A　目录与库存本体 | inventory | Meta 开发者文档 | **5** | 4 | 「Instead of redirecting customers to external websites or apps, catalogs let customers browse products within the chat — customers…」 | 无 | `catalogs_catalogs_overview_md`｜第 2 句 |
| A　目录与库存本体 | item（商品条目） | Meta 开发者文档 | **26** | 7 | 「WhatsApp uses the thumbnail of this item as the message's header image.」 | 有 | `catalogs_catalog_messages_md`｜第 11 句 |
| A　目录与库存本体 | item（商品条目） | WhatsApp 帮助中心 | **24** | 6 | 「Using this shopping button, customers can browse your catalog and add items to their cart from your catalog.」 | 无 | `faq_1184376605821468`｜第 5 句 |
| A　目录与库存本体 | product（商品） | Meta 开发者文档 | **55** | 12 | 「When sending a text message, you can use the optional `preview_url` set to `true` to have the message render a set of product…」 | 有 | `catalogs_catalog_link_messages_md`｜第 2 句 |
| A　目录与库存本体 | product（商品） | WhatsApp 帮助中心 | **17** | 6 | 「To continue showcasing your products and services on WhatsApp, we recommend creating a catalog on WhatsApp.」 | 无 | `faq_1314318822729539`｜第 14 句 |
| A　目录与库存本体 | service（服务） | Meta 开发者文档 | **1** | 1 | 「Instead of redirecting customers to external websites or apps, catalogs let customers browse products within the chat — customers…」 | 无 | `catalogs_catalogs_overview_md`｜第 2 句 |
| A　目录与库存本体 | service（服务） | WhatsApp 帮助中心 | **18** | 6 | 「Over the years, we’ve learned that businesses need focused solutions to help them showcase their goods and services.」 | 无 | `faq_1314318822729539`｜第 3 句 |
| A　目录与库存本体 | SKU／Content ID | Meta 开发者文档 | **1** | 1 | 「SKU numbers are labeled as Content ID in the Commerce Manager.」 | 无 | `catalogs_mpm_template_messages_md`｜第 23 句 |
| A　目录与库存本体 | storefront（店面） | Meta 开发者文档 | **1** | 1 | 「Set to `true` to show catalog storefront icon or `false` to hide it.」 | 无 | `catalogs_set_commerce_settings_md`｜第 7 句 |
| A　目录与库存本体 | storefront（店面） | WhatsApp 帮助中心 | **6** | 1 | 「Business users are no longer able to display their Shops storefront on the WhatsApp Business app.」 | 无 | `faq_1314318822729539`｜第 4 句 |
| A　目录与库存本体 | collection（集合） | Meta 开发者文档 | **1** | 1 | 「Whether you're a seasoned succulent enthusiast or just starting your plant collection, these rare succulents are sure to impress.」 | 无 | `catalogs_product_card_carousel_template_messages_md`｜第 25 句 |
| A　目录与库存本体 | collection（集合） | WhatsApp 帮助中心 | **17** | 2 | 「About ad performance metrics on the WhatsApp Business app Troubleshooting WhatsApp Premium Features WhatsApp Business Platform…」 | 无 | `faq_2929318000711140`｜第 2 句 |
| A　目录与库存本体 | price（价格） | Meta 开发者文档 | **3** | 3 | 「Each catalog item includes details like name, description, price, images, and availability.」 | 无 | `catalogs_catalogs_overview_md`｜第 3 句 |
| A　目录与库存本体 | price（价格） | WhatsApp 帮助中心 | **2** | 1 | 「Products or services may also contain other details such as: Additional images and videos displaying the product or service The…」 | 无 | `faq_405903568419894`｜第 5 句 |
| A　目录与库存本体 | description（描述） | Meta 开发者文档 | **3** | 3 | 「Each catalog item includes details like name, description, price, images, and availability.」 | 无 | `catalogs_catalogs_overview_md`｜第 3 句 |
| A　目录与库存本体 | description（描述） | WhatsApp 帮助中心 | **1** | 1 | 「The sale price of the product or service A description of the product or service A website link A product code These details can…」 | 无 | `faq_405903568419894`｜第 6 句 |
| A　目录与库存本体 | availability（可售状态） | Meta 开发者文档 | **1** | 1 | 「Each catalog item includes details like name, description, price, images, and availability.」 | 无 | `catalogs_catalogs_overview_md`｜第 3 句 |
| A　目录与库存本体 | thumbnail（缩略图） | Meta 开发者文档 | **6** | 4 | 「When sending a text message, you can use the optional `preview_url` set to `true` to have the message render a set of product…」 | 无 | `catalogs_catalog_link_messages_md`｜第 2 句 |
| A　目录与库存本体 | image（图片） | Meta 开发者文档 | **17** | 9 | 「Catalog messages display a product thumbnail header image of your choice, custom body text, a fixed text header, a fixed text…」 | 无 | `catalogs_catalog_messages_md`｜第 2 句 |
| A　目录与库存本体 | image（图片） | WhatsApp 帮助中心 | **2** | 1 | 「Each product or service in a business catalog is required to have a unique title, at least one image displaying the product or…」 | 无 | `faq_405903568419894`｜第 4 句 |
| A　目录与库存本体 | video／GIF | Meta 开发者文档 | **2** | 2 | 「Currently, PDPs only support product images — any videos or GIFs added to the product won't be displayed in the PDP. 1.」 | 无 | `catalogs_multi_product_messages_md`｜第 5 句 |
| A　目录与库存本体 | video／GIF | WhatsApp 帮助中心 | **1** | 1 | 「Products or services may also contain other details such as: Additional images and videos displaying the product or service The…」 | 无 | `faq_405903568419894`｜第 5 句 |
| B　消息形态与动作 | catalog message（目录消息） | Meta 开发者文档 | **1** | 1 | 「Catalog messages display a product thumbnail header image of your choice, custom body text, a fixed text header, a fixed text…」 | 无 | `catalogs_catalog_messages_md`｜第 2 句 |
| B　消息形态与动作 | catalog template（目录模板） | Meta 开发者文档 | **2** | 1 | 「Catalog templates are marketing templates that allow you to showcase your product catalog entirely within WhatsApp.」 | 无 | `catalogs_catalog_template_messages_md`｜第 3 句 |
| B　消息形态与动作 | single-product message | Meta 开发者文档 | **7** | 1 | 「If your customer has multiple devices linked to their account, single-product messages are synced between devices.」 | 无 | `catalogs_single_product_messages_md`｜第 9 句 |
| B　消息形态与动作 | multi-product message | Meta 开发者文档 | **10** | 1 | 「Within WhatsApp, customers can browse products, view details, add items to a cart, and send an order. *Multi-Product message…」 | 有 | `catalogs_multi_product_messages_md`｜第 2 句 |
| B　消息形态与动作 | carousel（轮播） | Meta 开发者文档 | **3** | 2 | 「The product carousel message integrates with the Product Catalog and supports Single Product Message (SPM) actions on each card,…」 | 无 | `catalogs_interactive_product_carousel_messages_md`｜第 2 句 |
| B　消息形态与动作 | card（卡片） | Meta 开发者文档 | **11** | 3 | 「The product carousel message integrates with the Product Catalog and supports Single Product Message (SPM) actions on each card,…」 | 有 | `catalogs_interactive_product_carousel_messages_md`｜第 2 句 |
| B　消息形态与动作 | card_index（卡片序号） | Meta 开发者文档 | **3** | 2 | 「Each card exists in a `cards[]` array and must be given a `"card_index"` value of `0` through `9`.」 | 无 | `catalogs_interactive_product_carousel_messages_md`｜第 4 句 |
| B　消息形态与动作 | View catalog／View details（查看按钮） | Meta 开发者文档 | **3** | 3 | 「Catalog messages display a product thumbnail header image of your choice, custom body text, a fixed text header, a fixed text…」 | 无 | `catalogs_catalog_messages_md`｜第 2 句 |
| B　消息形态与动作 | product detail page（商品详情页） | Meta 开发者文档 | **4** | 2 | 「Whenever a customer clicks on a specific item, the product's latest info is fetched and the product displays in a Product Detail…」 | 无 | `catalogs_multi_product_messages_md`｜第 4 句 |
| B　消息形态与动作 | forward（转发） | Meta 开发者文档 | **2** | 2 | 「You also receive a webhook notification indicating that WhatsApp could not deliver the message because the customer is using an…」 | 无 | `catalogs_multi_product_messages_md`｜第 17 句 |
| B　消息形态与动作 | notification（通知） | Meta 开发者文档 | **4** | 3 | 「You also receive a webhook notification indicating that WhatsApp could not deliver the message because the customer is using an…」 | 无 | `catalogs_multi_product_messages_md`｜第 17 句 |
| B　消息形态与动作 | chat thread（会话线程） | Meta 开发者文档 | **6** | 3 | 「A shopping cart: - Is unique to a customer/business chat thread on a specific device: Only one cart is created per chat thread…」 | 无 | `catalogs_catalogs_overview_md`｜第 11 句 |
| C　购物车与结账 | cart（购物车） | Meta 开发者文档 | **22** | 4 | 「This creates the catalog that powers all downstream interactions. 2. Commerce configuration — You configure commerce settings…」 | 有 | `catalogs_catalogs_overview_md`｜第 6 句 |
| C　购物车与结账 | cart（购物车） | WhatsApp 帮助中心 | **10** | 2 | 「About ad performance metrics on the WhatsApp Business app Troubleshooting WhatsApp Premium Features WhatsApp Business Platform…」 | 有 | `faq_1184376605821468`｜第 2 句 |
| C　购物车与结账 | checkout（结账） | Meta 开发者文档 | **2** | 2 | 「URL button flows can be useful when you want to load the product in your mobile checkout page where users can add promo codes and…」 | 无 | `catalogs_product_card_carousel_template_messages_md`｜第 8 句 |
| C　购物车与结账 | quantity（数量） | WhatsApp 帮助中心 | **1** | 1 | 「From there, they can also change the quantity of each item in their cart.」 | 无 | `faq_1184376605821468`｜第 7 句 |
| C　购物车与结账 | units of an item（件数） | Meta 开发者文档 | **2** | 1 | 「Customers can add up to 99 units of each single catalog item to a shopping cart, but there is no limit on the number of distinct…」 | 有 | `catalogs_catalogs_overview_md`｜第 14 句 |
| C　购物车与结账 | shopping button（购物按钮） | WhatsApp 帮助中心 | **2** | 1 | 「The shopping button next to your business name appears in your chat or business profile.」 | 无 | `faq_1184376605821468`｜第 4 句 |
| D　模板与按钮 | template（模板） | Meta 开发者文档 | **41** | 8 | 「Catalog templates are marketing templates that allow you to showcase your product catalog entirely within WhatsApp.」 | 有 | `catalogs_catalog_template_messages_md`｜第 3 句 |
| D　模板与按钮 | button（按钮） | Meta 开发者文档 | **20** | 7 | 「Catalog messages display a product thumbnail header image of your choice, custom body text, a fixed text header, a fixed text…」 | 无 | `catalogs_catalog_messages_md`｜第 2 句 |
| D　模板与按钮 | button（按钮） | WhatsApp 帮助中心 | **2** | 1 | 「The shopping button next to your business name appears in your chat or business profile.」 | 无 | `faq_1184376605821468`｜第 4 句 |
| D　模板与按钮 | header／body／footer（模板部件） | Meta 开发者文档 | **18** | 8 | 「Catalog messages display a product thumbnail header image of your choice, custom body text, a fixed text header, a fixed text…」 | 无 | `catalogs_catalog_messages_md`｜第 2 句 |
| D　模板与按钮 | call-to-action（行动按钮） | Meta 开发者文档 | **5** | 2 | 「Before sending an order details template message, create a template with an "order details" call-to-action button.」 | 无 | `payments_payments_br_orderdetailstemplate_md`｜第 3 句 |
| D　模板与按钮 | URL button（网址按钮） | Meta 开发者文档 | **4** | 1 | 「Users who have placed an order can see the contents of the order by tapping the View details button. ## URL buttons To send…」 | 无 | `catalogs_product_card_carousel_template_messages_md`｜第 6 句 |
| E　订单与状态 | order（订单） | Meta 开发者文档 | **46** | 8 | 「Instead of redirecting customers to external websites or apps, catalogs let customers browse products within the chat — customers…」 | 无 | `catalogs_catalogs_overview_md`｜第 2 句 |
| E　订单与状态 | order（订单） | WhatsApp 帮助中心 | **6** | 4 | 「About ad performance metrics on the WhatsApp Business app Troubleshooting WhatsApp Premium Features WhatsApp Business Platform…」 | 无 | `faq_1184376605821468`｜第 2 句 |
| E　订单与状态 | order_details（订单详情消息） | Meta 开发者文档 | **5** | 3 | 「Order details template can be categorized as "Utility" or "Marketing" template and apart from 'name' and 'language' of choice,…」 | 无 | `payments_payments_br_orderdetailstemplate_md`｜第 17 句 |
| E　订单与状态 | order_status（订单状态消息） | Meta 开发者文档 | **6** | 2 | 「Send an `order_status` message when you update the order status, either based on the WhatsApp payment status change notification…」 | 无 | `payments_payments_br_orders_md`｜第 7 句 |
| E　订单与状态 | pending（待处理） | Meta 开发者文档 | **1** | 1 | 「When attached to an order details message, orders start in `pending` status.」 | 无 | `payments_payments_br_orders_md`｜第 11 句 |
| E　订单与状态 | completed（已完成） | Meta 开发者文档 | **2** | 1 | 「If the order details message includes the `order` object, WhatsApp also moves the order to `completed`, which is a final status.」 | 无 | `payments_payments_br_orders_md`｜第 10 句 |
| E　订单与状态 | canceled（已取消） | Meta 开发者文档 | **5** | 1 | 「The customer cannot pay for an order that is canceled.」 | 无 | `payments_payments_in_orderstatustemplate_md`｜第 21 句 |
| E　订单与状态 | reference_id（订单引用号） | Meta 开发者文档 | **1** | 1 | 「The reference_id must be unique for each order_details message for the same business.」 | 无 | `payments_payments_br_orders_md`｜第 32 句 |
| E　订单与状态 | subtotal／total（金额合计） | Meta 开发者文档 | **8** | 3 | 「Add product items, up to 30 products total across all sections ] } // ...」 | 有 | `catalogs_mpm_template_messages_md`｜第 20 句 |
| E　订单与状态 | offset（金额偏移） | Meta 开发者文档 | **2** | 1 | 「The following fields are part of the `subtotal` object: `offset` string - Required. Must be `100` for `BRL`.」 | 无 | `payments_payments_br_orders_md`｜第 40 句 |
| F　支付方式与码 | pix | Meta 开发者文档 | **7** | 1 | 「This enables customers to choose their preferred payment option directly from the conversation. --- ## Supported payment methods…」 | 无 | `payments_payments_br_payment_request_cta_md`｜第 3 句 |
| F　支付方式与码 | boleto | Meta 开发者文档 | **3** | 1 | 「This enables customers to choose their preferred payment option directly from the conversation. --- ## Supported payment methods…」 | 无 | `payments_payments_br_payment_request_cta_md`｜第 3 句 |
| F　支付方式与码 | payment link（支付链接） | Meta 开发者文档 | **3** | 1 | 「This enables customers to choose their preferred payment option directly from the conversation. --- ## Supported payment methods…」 | 无 | `payments_payments_br_payment_request_cta_md`｜第 3 句 |
| F　支付方式与码 | one-click payment（一键支付） | Meta 开发者文档 | **4** | 1 | 「This enables customers to choose their preferred payment option directly from the conversation. --- ## Supported payment methods…」 | 无 | `payments_payments_br_payment_request_cta_md`｜第 3 句 |
| F　支付方式与码 | UPI | Meta 开发者文档 | **1** | 1 | 「The below example shows an example payload with `upi` as the payment type.」 | 无 | `payments_payments_in_orderdetailstemplate_md`｜第 20 句 |
| F　支付方式与码 | payment code（支付码） | Meta 开发者文档 | **1** | 1 | 「The following images show examples of limited-time offer templates combined with payment request CTA buttons rendered in…」 | 无 | `payments_payments_br_payment_request_cta_md`｜第 35 句 |
| G　审核、政策与责任 | review（审核） | Meta 开发者文档 | **2** | 2 | 「If a user reports a product, it is reviewed for violations.」 | 无 | `catalogs_catalogs_overview_md`｜第 21 句 |
| G　审核、政策与责任 | review（审核） | WhatsApp 帮助中心 | **2** | 1 | 「Your collection will be reviewed by our team.」 | 无 | `faq_2929318000711140`｜第 12 句 |
| G　审核、政策与责任 | policy（政策） | Meta 开发者文档 | **2** | 1 | 「You cannot send carts to customers. *Shopping cart experience example and expected behavior for item state change.* ## Policies…」 | 无 | `catalogs_catalogs_overview_md`｜第 17 句 |
| G　审核、政策与责任 | policy（政策） | WhatsApp 帮助中心 | **2** | 2 | 「If your device is connected and the business still hasn’t responded, or you believe a business is violating the WhatsApp Business…」 | 无 | `faq_2133933800112087`｜第 4 句 |
| G　审核、政策与责任 | report（举报） | Meta 开发者文档 | **2** | 1 | 「If a policy-violating item is added, the product is flagged and the business is directed to the WhatsApp Commerce Policies. -…」 | 无 | `catalogs_catalogs_overview_md`｜第 20 句 |
| G　审核、政策与责任 | report（举报） | WhatsApp 帮助中心 | **6** | 1 | 「If your device is connected and the business still hasn’t responded, or you believe a business is violating the WhatsApp Business…」 | 无 | `faq_2133933800112087`｜第 4 句 |
| G　审核、政策与责任 | violation（违规） | Meta 开发者文档 | **3** | 1 | 「You cannot send carts to customers. *Shopping cart experience example and expected behavior for item state change.* ## Policies…」 | 无 | `catalogs_catalogs_overview_md`｜第 17 句 |
| G　审核、政策与责任 | violation（违规） | WhatsApp 帮助中心 | **1** | 1 | 「If your device is connected and the business still hasn’t responded, or you believe a business is violating the WhatsApp Business…」 | 无 | `faq_2133933800112087`｜第 4 句 |
| H　接口与信号 | webhook | Meta 开发者文档 | **6** | 5 | 「You also receive a webhook notification indicating that WhatsApp could not deliver the message because the customer is using an…」 | 无 | `catalogs_multi_product_messages_md`｜第 17 句 |
| H　接口与信号 | status（状态） | Meta 开发者文档 | **12** | 4 | 「Use the ID listed after `wamid` to track your message status.」 | 无 | `payments_payments_br_orderdetailstemplate_md`｜第 27 句 |
| H　接口与信号 | status（状态） | WhatsApp 帮助中心 | **4** | 3 | 「About showing ads in WhatsApp Status How to use labels How to Comply with the Laws for Selling Online in India How to create ads…」 | 无 | `faq_1498078847370903`｜第 1 句 |
| H　接口与信号 | wamid（消息 ID） | Meta 开发者文档 | **4** | 4 | 「If your message is sent successfully, you get the following response: ```json { "messaging_product": "whatsapp", "contacts": [{…」 | 无 | `catalogs_multi_product_messages_md`｜第 38 句 |
| H　接口与信号 | wa_id／BSUID | Meta 开发者文档 | **3** | 3 | 「If your message is sent successfully, you get the following response: ```json { "messaging_product": "whatsapp", "contacts": [{…」 | 无 | `catalogs_multi_product_messages_md`｜第 38 句 |
| H　接口与信号 | recipient（收件人字段） | Meta 开发者文档 | **8** | 8 | 「If you include both `to` and `recipient`, `to` is used.」 | 无 | `catalogs_catalog_messages_md`｜第 14 句 |
| H　接口与信号 | API | Meta 开发者文档 | **8** | 7 | 「The product carousel message integrates with the Product Catalog and supports Single Product Message (SPM) actions on each card,…」 | 无 | `catalogs_interactive_product_carousel_messages_md`｜第 2 句 |
| **合计** | **61** 个词（组）× **2** 类站点 | 非空格 **79** 个 | 句次 **617** | 一句可落多格 | 代表句按池内顺序取第一句 | 零命中词 **0** 个 | 回表失败 **0** 句 |

#ROWS=79 COLS=8


# 附表 D 官方写明的取值域全表（2 行）

取组口径：脚本扫过 **32** 个已登记页面的每一行原文，配置了 10 个取值域标记，本轮真正在这些页面里出现过的有 **2** 个（`must be one of` 1 行／`one of the following` 2 行，合计 **3** 行），另有 **8** 个（Values can be／Supported values are／Possible values／Can be one of／The following values are supported／Valid values are／Options are／Enum Values）在扫过的每一行原文里零命中——这几串字符只是脚本配置表里的名字，本轮官方页面没有一处用过这种写法，所以本篇不给它们加反引号、也不把它们当作任何官方句子的引文。取到值来的页面 **1** 个（站点类别：Meta 开发者文档）。按标记所在行的形态分档取值：自动·参数表说明格 1。全表 **1** 组、共 **2** 个取值、去重后的取值名 **2** 个。其中 **0** 组的取值是不带反引号的纯文本子项——反引号是接口文档的排版，帮助中心同样用「one of the following」加子项写成正式取值域，这一档整项照收、只丢超过 80 字符的说明句；同页同字段同取值的重复写法（帮助中心按平台页签各写一遍）合并为一行，共合并 **0** 处。取值本身带空格的共 **0** 个（如 `LEGAL NAME NOT FOUND IN DOCUMENTS`、`No longer needed`），这一批只能按「反引号紧跟破折号说明」的位置认出，脚本对这种写法额外兜一遍：同一格里见到两个以上这种位置才收，并且只在这一档放开取值不能带空格的限制——接口文档的枚举里确有带空格的大写取值，卡成单词会漏掉整组。

为什么这一表不走原句池：官方把取值域写在接口参数表的说明格里、缩进的子项列表里、整句内联的清单里，这些行要么带竖线、要么是列表行，本来就被池子的入池条件挡在外面。所以 D 的核验独立于 C：每一组把它自己那一行原文、缩进子项式还把每个取值所在的那一行原文，逐字回表到 `_tmp/w6_a7s/` 的对应页面，失败 **0** 行。

落空登记：扫到标记但取不到两个以上取值的共 **2** 处，原因由脚本按同一行的证据判，不由人挑：标记句下面紧跟的不是子项列表（官方把选择写在正文段落里，没写成清单） 2。**扫到标记不等于官方写明了取值域**，这几处不算进表，但算进这一句，让读者知道漏了多少、为什么漏。这 **2** 处分布在 **1** 个页面，每一处的页面标识、原文行号、判定档位、命中的标记、落空判据与该行未截断的原文全部逐条写进 `_tmp/w6_a7_dmiss.tsv`（与上面的次数由脚本断言相等，不是"约"），可逐条回原文核对。

单组取值上限设为 **24** 个：实测最多的一组有 **2** 个取值，没有一组触及上限，所以本表没有因上限而丢过取值。

取值列表的截断按整取值进行（顿号相连的列表没有空格，按词截会把一整组截到只剩一项）：单元格 **400** 字符内装得下的原样列出，装不下的整项去掉并在行内写明「另有 N 个取值未列出」。本轮实测 **0** 组触及这一上限、被去掉的取值 **0** 个，也就是说表里列出的取值就是脚本收到的全部 **2** 个，没有一组需要读者回原文补齐。

| 组别 | 页面标识 | 行号 | 字段／属性 | 官方标记 | 取值个数 | 官方写明的取值 |
|---|---|---|---|---|---|---|
| 自动·参数表说明格 | `payments_payments_br_orders_md` | 226 | `(格首未写字段名)` | Must be one of | **2** | `digital-goods`、`physical-goods` |
| **合计** | **1** 页 | 行号为原文行序 | 字段名取自官方写法 | 标记 1 种 | 取值 **2** 个 | 去重取值名 **2** 个 |

#ROWS=1 COLS=7


# 附表 E 同一组维度在本轴各主体上的逐列对照（28 行）

取格口径：本表 **27** 个对照维度 × **5** 列 = **135** 格，其中 **37** 格取到了官方原句、**98** 格标作「官方未按这一列写该项」。每一格的取法都是「页面标识＋一条正则」交给脚本在原句池里取句，在该列的页面范围内命中 0 句或命中多句都会让生成脚本中止，所以表里没有一格是人工挑出来的。原句池共 **291** 句（已由附表 B 逐字回表），本表用去重后的页面 **21** 个；同一句在多个维度或多列复用的共 **2** 次，复用时页标识与句序不变。

列的定义写在 `_tmp/w6_a7_ecols.tsv` 里，每列要么按站点类别取页（），要么按人工点名的页清单取页（目录总纲与库存页＝3 页／消息形态页＝4 页／模板与轮播页＝5 页／商家端帮助页＝8 页／订单与支付页＝6 页）。空格的含义只有一个：官方在这批登记页面里没按这一列写这一项，**不代表该主体没有这项规则**。

官方原句超过 112 字符按整词截断并以省略号结尾，共 **20** 格截断；截断只发生在显示层，取句与逐字回表用的是未截断的整句。各列取到句子的格数：目录总纲与库存页 5／消息形态页 7／模板与轮播页 11／商家端帮助页 9／订单与支付页 5。

| 编号 | 对照维度 | 目录总纲与库存页 | 消息形态页 | 模板与轮播页 | 商家端帮助页 | 订单与支付页 |
|---|---|---|---|---|---|---|
| E1   | 目录如何接入账号 | 「Instead of redirecting customers to external websites or apps, catalogs let customers browse products within…」（`catalogs_catalogs_overview_md`｜第 2 句） | 官方未按这一列写该项 | 「With URL button flows, order placement happens outside of the WhatsApp client, so WhatsApp does not trigger…」（`catalogs_product_card_carousel_template_messages_md`｜第 9 句） | 「Finally, businesses can connect an existing Meta catalog to the WhatsApp Business app.」（`faq_405903568419894`｜第 9 句） | 官方未按这一列写该项 |
| E2   | 商品信息的来源与必备字段 | 「Each catalog item includes details like name, description, price, images, and availability.」（`catalogs_catalogs_overview_md`｜第 3 句） | 官方未按这一列写该项 | 「SPM templates are marketing templates that allow you to present a single product from your ecommerce catalog,…」（`catalogs_spm_template_messages_md`｜第 3 句） | 「Each product or service in a business catalog is required to have a unique title, at least one image displaying…」（`faq_405903568419894`｜第 4 句） | 官方未按这一列写该项 |
| E3   | 购物车属于哪一层 | 「A shopping cart: - Is unique to a customer/business chat thread on a specific device: Only one cart is created…」（`catalogs_catalogs_overview_md`｜第 11 句） | 「However, the shopping cart is local to each specific device.」（`catalogs_multi_product_messages_md`｜第 13 句） | 官方未按这一列写该项 | 「The cart provides a way for your customers to begin the order process.」（`faq_1184376605821468`｜第 3 句） | 官方未按这一列写该项 |
| E4   | 消息本体能否跨设备 | 官方未按这一列写该项 | 「If your customer has multiple devices linked to their account, WhatsApp syncs multi-product messages between…」（`catalogs_multi_product_messages_md`｜第 12 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E5   | 多商品消息的最低版本 | 官方未按这一列写该项 | 「Currently, the following platforms support multi-product messages: - iOS: 2.21.100 - Android: 2.21.9.15 - Web:…」（`catalogs_multi_product_messages_md`｜第 15 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E6   | 单商品消息的最低版本 | 官方未按这一列写该项 | 「Currently, single-product messages can be received on the following platforms: - iOS: 2.21.210 - Android:…」（`catalogs_single_product_messages_md`｜第 12 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E7   | 购物车里一种商品能放多少 | 「Customers can add up to 99 units of each single catalog item to a shopping cart, but there is no limit on the…」（`catalogs_catalogs_overview_md`｜第 14 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 「From there, they can also change the quantity of each item in their cart.」（`faq_1184376605821468`｜第 7 句） | 官方未按这一列写该项 |
| E8   | 多商品模板的商品总量 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Add product items, up to 30 products total across all sections ] } // ...」（`catalogs_mpm_template_messages_md`｜第 20 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E9   | 交互式轮播的卡片数量 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「You must add two card objects to your message, and can add a maximum of 10.」（`catalogs_interactive_product_carousel_messages_md`｜第 3 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E10  | 模板型轮播的卡片数量 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「An approved template with two product cards can be used to send up to 10 cards in a template message.」（`catalogs_product_card_carousel_template_messages_md`｜第 11 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E11  | 商品下架后的购物车 | 「If a customer has already added product B to a cart, the item will be removed from the cart.」（`catalogs_catalogs_overview_md`｜第 30 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E12  | 模板类别 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「SPM templates are marketing templates that allow you to present a single product from your ecommerce catalog,…」（`catalogs_spm_template_messages_md`｜第 3 句） | 官方未按这一列写该项 | 「Order details template can be categorized as "Utility" or "Marketing" template and apart from 'name' and…」（`payments_payments_br_orderdetailstemplate_md`｜第 17 句） |
| E13  | 订单状态的起点 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「When attached to an order details message, orders start in `pending` status.」（`payments_payments_br_orders_md`｜第 11 句） |
| E14  | 支付按钮过期后的行为 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「WhatsApp does not automatically disable the payment button when the Pix code, Boleto, or Payment Link is no…」（`payments_payments_br_payment_request_cta_md`｜第 36 句） |
| E15  | 订单能否取消 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「An order can be canceled only if the user has not already paid for the order.」（`payments_payments_in_orderstatustemplate_md`｜第 24 句） |
| E16  | 目录链接的分享方式 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「To share a product or service from catalog: You can share a link to an individual product or service, or a link…」（`faq_487917009931629`｜第 4 句） | 官方未按这一列写该项 |
| E17  | 购物车设置生效的延迟 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「If you turn off the carts feature, you may still receive carts from customers in that 24-hour period.」（`faq_1475761576240237`｜第 8 句） | 官方未按这一列写该项 |
| E18  | 集合上架前的审核 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「WhatsApp reviews collections to confirm they meet the WhatsApp Commerce Policy before the collections become…」（`faq_2929318000711140`｜第 41 句） | 官方未按这一列写该项 |
| E19  | Shops 店面与目录 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Business users are no longer able to display their Shops storefront on the WhatsApp Business app.」（`faq_1314318822729539`｜第 4 句） | 官方未按这一列写该项 |
| E20  | 订单消息里的 order 对象 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Without the `order` object, you can send a simplified order details message with just the total amount and…」（`payments_payments_br_orders_md`｜第 5 句） |
| E21  | 商品图片被用作消息头图 | 官方未按这一列写该项 | 「WhatsApp uses the thumbnail of this item as the message's header image.」（`catalogs_catalog_messages_md`｜第 11 句） | 「The thumbnail of this item will be used as the message's header image.」（`catalogs_catalog_template_messages_md`｜第 21 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E22  | 消息上的按钮文案 | 官方未按这一列写该项 | 「Catalog messages display a product thumbnail header image of your choice, custom body text, a fixed text…」（`catalogs_catalog_messages_md`｜第 2 句） | 「SPM templates are marketing templates that allow you to present a single product from your ecommerce catalog,…」（`catalogs_spm_template_messages_md`｜第 3 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E23  | 文本消息的链接预览 | 官方未按这一列写该项 | 「When sending a text message, you can use the optional `preview_url` set to `true` to have the message render a…」（`catalogs_catalog_link_messages_md`｜第 2 句） | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E24  | 轮播卡片是否必须同一目录 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「The type of each card must be set to `"product"`, and each card must reference the same `"catalog_id"`.」（`catalogs_interactive_product_carousel_messages_md`｜第 5 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E25  | 模板卡片组件是否必须一致 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「All cards defined on a template must have the same components. ## View buttons When a WhatsApp user taps the…」（`catalogs_product_card_carousel_template_messages_md`｜第 3 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E26  | 模板变量与字符串数量 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Number of strings must match the number of variable placeholders in the message body text string.」（`catalogs_product_card_carousel_template_messages_md`｜第 15 句） | 官方未按这一列写该项 | 官方未按这一列写该项 |
| E27  | 商家能否主动分享目录 | 官方未按这一列写该项 | 官方未按这一列写该项 | 官方未按这一列写该项 | 「Business Tip : Business owners can proactively share products or services with their customers to drive…」（`faq_501451005213099`｜第 3 句） | 官方未按这一列写该项 |
| **合计** | **27** 个维度 × 5 列 = **135** 格 | 有句 **37** 格 | 空格 **98** 格 | 去重引用页 **21** | 跨格复用句 **2** 次 | 截断 **20** 格 |

#ROWS=27 COLS=7


# 附表 F 口径冲突与官方未写明处（13 行）

本表收录两类东西：官方多处写法不一致、需要读者知道本篇按哪一说走的**冲突**，以及官方在这批登记页面里根本没写、本篇不替它补的**空白**。全表 **12** 个议题：**9** 个两侧都取到了原句、**1** 个只有一侧有原句（另一侧官方没写）、**2** 个两侧都没有原句可引（共 **2** 行）。

两侧皆空的行要说清它空在哪：本轴登记 **32** 页，其中正文未取回 **0** 页——这 **0** 页全篇不引用，已引用 **32** 页（Meta 开发者文档 20／WhatsApp 帮助中心 12），所以两侧皆空不是因为抓不到，而是因为**官方在可引用的这批页面里没按这一项写过一句话**；这一类议题一律登记为空白，正文不替官方补结论。

取句方式与附表 E 同一把刀：每个说法登记「页面标识＋一条正则」，脚本在原句池里取句，命中 0 句或多句都会让生成脚本中止；取到的每一句再逐字回表到 `_tmp/w6_a7s/` 的对应页面。本表引用页面去重 **12** 个，同一句在多个议题里复用的共 **0** 次。两侧点同一页、又取到同一句的行由脚本直接中止——同句不构成冲突，不允许拿复制冒充对照。差异性质用了 **11** 种。其中重复出现的有 1 种：官方未写明 2；其余 10 种各只出现 1 次，逐条写在表内「差异性质」列，不在这里重述。

「本篇采用」一列写的不是判断谁对谁错，而是本篇正文按哪一说的走、以及为什么。这一列的 **8** 行标为已写进正文、**4** 行只在本附表登记（正文不替官方补结论，也不把两种说法合成一说）。这一列标「已写进正文」的 **8** 行本次已逐句核过成稿：它们要引的 **16** 句官方原句全部原样出现在成稿里（空白折叠后做子串比对），缺任何一句本脚本都会中止，所以这一列不是空头承诺。官方原句超过 130 字符按整词截断并以省略号结尾，共 **7** 处截断；截断只发生在显示层。

| 编号 | 议题 | 说法甲 | 说法乙 | 差异性质 | 本篇采用 | 正文落点 |
|---|---|---|---|---|---|---|
| F1   | 购物车有没有过期日 | 「Once a cart is sent, the customer can open another cart with you and start the process again. - Has no expiration date: The cart…」（`catalogs_catalogs_overview_md`｜第 12 句） | 「If you turn off the carts feature, you may still receive carts from customers in that 24-hour period.」（`faq_1475761576240237`｜第 8 句） | 口径对象不同：单条购物车不设过期日，商家关闭购物车开关后的 24 小时是设置生效延迟 | 本篇分两层写：单条购物车不设过期日，商家侧关闭购物车功能后的 24 小时是开关延迟，不是购物车有效期。 | 已写进正文 |
| F2   | 购物车是否随账号跨设备 | 「A shopping cart: - Is unique to a customer/business chat thread on a specific device: Only one cart is created per chat thread…」（`catalogs_catalogs_overview_md`｜第 11 句） | 「If your customer has multiple devices linked to their account, WhatsApp syncs multi-product messages between devices.」（`catalogs_multi_product_messages_md`｜第 12 句） | 对象不同：消息本体可在设备间同步，购物车只存在于具体设备 | 本篇按对象分开写：商品消息本体可在已登录设备间同步，购物车只属于具体设备的会话，不把两者读成同一件事。 | 已写进正文 |
| F3   | 交互式商品消息要不要模板 | 「During testing, businesses had an average 7% conversion of multi-product messages sent to carts received. #### No templates…」（`catalogs_multi_product_messages_md`｜第 26 句） | 「Catalog templates are marketing templates that allow you to showcase your product catalog entirely within WhatsApp.」（`catalogs_catalog_template_messages_md`｜第 3 句） | 载体不同：交互式消息免模板，目录模板本身就是营销模板 | 本篇把两种载体分两套写：交互式商品消息不需要模板或预审批，目录模板本身就是营销模板、受模板规则约束。 | 只在附表登记 |
| F4   | 价格是不是商品必备字段 | 「Each catalog item includes details like name, description, price, images, and availability.」（`catalogs_catalogs_overview_md`｜第 3 句） | 「Each product or service in a business catalog is required to have a unique title, at least one image displaying the product or…」（`faq_405903568419894`｜第 4 句） | 概述式列举与必备字段清单不同：概览把价格列入条目信息，帮助中心的必备清单只有标题、图片、原产国 | 本篇把价格当可选信息写，必备字段按帮助中心的三项清单，不把概览里的列举读成必填。 | 已写进正文 |
| F5   | 支付按钮过期后由谁禁用 | 「The CTA for payment will be disabled after expiry on the user end.」（`payments_payments_br_orders_md`｜第 37 句） | 「WhatsApp does not automatically disable the payment button when the Pix code, Boleto, or Payment Link is no longer valid.」（`payments_payments_br_payment_request_cta_md`｜第 36 句） | 直接冲突：一处说过期后按钮会被禁用，另一处说 WhatsApp 不会自动禁用 | 两处原文并列登记，本篇不合并成一句话；引用时各按所在页面语境标注，并提醒读者以所用模板与地区页为准。 | 已写进正文 |
| F6   | 订单状态从哪开始、何时结束 | 「When attached to an order details message, orders start in `pending` status.」（`payments_payments_br_orders_md`｜第 11 句） | 「If the order details message includes the `order` object, WhatsApp also moves the order to `completed`, which is a final status.」（`payments_payments_br_orders_md`｜第 10 句） | 条件不同：默认起点是待付款，带 order 对象的订单详情会推进到完成态 | 本篇写清默认起点与带 order 对象时的完成态推进，两者是不同条件下的状态，不写成互斥的两个终态。 | 已写进正文 |
| F7   | 订单取消与付款的先后限制 | 「An order can be canceled only if the user has not already paid for the order.」（`payments_payments_in_orderstatustemplate_md`｜第 24 句） | 「The customer cannot pay for an order that is canceled.」（`payments_payments_in_orderstatustemplate_md`｜第 21 句） | 互补限制：已付款不能取消，已取消不能付款 | 本篇把两条并排写成一个闭环限制，只按官方原文陈述，不引申退款、撤销或资金处理规则。 | 已写进正文 |
| F8   | 订单详情模板属于哪一类 | 「Order details template can be categorized as "Utility" or "Marketing" template and apart from 'name' and 'language' of choice,…」（`payments_payments_br_orderdetailstemplate_md`｜第 17 句） | 「Order details template is categorized as "Utility" template and apart from 'name' and 'language' of choice, it has general…」（`payments_payments_in_orderdetailstemplate_md`｜第 14 句） | 地区口径不同：巴西可选实用或营销，印度固定实用 | 本篇按地区分别写：巴西订单详情模板可选实用或营销，印度固定为实用；不把一地说法套到另一地。 | 已写进正文 |
| F9   | 模板型轮播能发几张卡 | 「An approved template with two product cards can be used to send up to 10 cards in a template message.」（`catalogs_product_card_carousel_template_messages_md`｜第 11 句） | 「You must add two card objects to your message, and can add a maximum of 10.」（`catalogs_interactive_product_carousel_messages_md`｜第 3 句） | 数值相同但限制条件不同：前者是已批准模板的消息可扩展到十张卡，后者是交互式消息本身最多十张 | 本篇分别写两种轮播的卡片上限，数值相同但限制条件不同，不因数字一样就当成同一条规则。 | 已写进正文 |
| F10  | 关闭购物车后 24 小时内新建购物车如何处理 | 官方未写明（—） | 官方未写明（—） | 官方未写明 | 本篇只登记开关变更的 24 小时延迟，不推断这 24 小时内新建购物车的处理方式。 | 只在附表登记 |
| F11  | 商家能否主动清空客户的购物车 | 官方未写明（—） | 官方未写明（—） | 官方未写明 | 本篇只写商家不能向客户发送购物车，不补清空、撤回或修改客户购物车的操作。 | 只在附表登记 |
| F12  | 订单详情模板的头图能不能用视频 | 「Fill in template components such as `Header`, `Body`, and optional `footer` text. * For the `Header`, you can choose one of three…」（`payments_payments_br_orderdetailstemplate_md`｜第 10 句） | 官方未写明（—） | 官方未写明：只列文本、图片、文档三种头类型，未提视频 | 本篇只按官方列出的三种头类型写，不推断视频可用于订单详情模板的头图。 | 只在附表登记 |
| **合计** | **12** 个议题 | 两侧有句 **9**／单侧 **1**／两侧皆无 **2** | 引用页 **12** | 性质 **11** 种 | 正文落点：已写 **8**／只在附表 **4** | 截断 **7** 处 |

#ROWS=12 COLS=7


---

**边界声明：** 本篇是**官方商业目录、商品消息、购物车与订单模板文本的口径梳理**，不是开店指南，也不是对平台商品能力的评价，更不是运营方案。全篇内容限于 WhatsApp 与 Meta 公开页面已经写出的表述，共 **32** 个页面登记位：Meta 开发者文档 **20** 页（WhatsApp 商业目录 14 页、巴西与印度支付 5 页、订单 webhook 1 页）、WhatsApp 帮助中心 **12** 页（目录、购物车、分享、集合、Shops、订单详情与举报）。引用这些文本的目的只有两个：说明平台把**目录、条目、四种商品消息、模板与轮播、购物车、订单与支付状态**写成了哪些对象名、哪些字段、哪些上限、哪些否定式前提与哪些降级后果；说明同一批文本把**目录接入层级、卡片数量、购物车数量与生命周期、模板类别、状态起点与终点、按钮过期后的行为**写成了哪些数字与哪些限定词。本篇**不含**批量发送脚本、发送队列与并发重试参数、账号池与多号轮换、注册与验证流程的操作步骤、限速与频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法；**不含**任何可用于向非自愿收件人大量投递商品或订单消息的方法——"怎样把商品消息当广告群发""怎样绕过模板审批把营销内容塞进会话""怎样用购物车消息批量触达没同意过的号码"这三类问法在本篇一律不答；**不含**代码、恶意样本或利用链构造方式；**不含**任何针对他人设备或他人账号的操作步骤与时序。界面页与接口页里官方写成"步骤""清单"的内容——商业 App 里在哪建目录、集合怎么排序、分享入口选哪一项、举报时选哪个理由——本篇只登记**控件名、接口名、字段名与条目数**，**不重述每一步在哪个页签里怎么点，也不把清单转写成可执行的动作序列**，读者要动手请回原页。

五类页面的口径在本篇里始终分开写，因为官方没有一页把它们画成同一张对象图：**目录与库存面**写在总纲与上传页，回答目录是什么、挂在哪一层、条目里有什么（第 2 章）；**商品消息面**写在四种形态各自的页面，回答一个商品、一组商品、整本目录各怎么发、客户端版本不够时怎么降级（第 3 章）；**模板与轮播面**写在四类模板页，回答模板类别、卡片数量与变量绑定（第 4 章）；**商家端帮助面**写在商业 App 的帮助中心页，回答商家能看到什么、能主动做什么、开关变更多久生效（第 5 至 6 章）；**订单与支付面**写在巴西与印度的支付页、订单 webhook 页，回答状态从哪开始、谁能推进、按钮过期后谁负责、取消与付款谁先谁后（第 7 至 10 章）。把这五层合并成"一条商品消息从发出到收款的完整规则"，会得到官方没有写过的跨面结论，本篇不这么做。

官方写成区间、多档或带限定词的数字（「up to」「a maximum of」「only in certain countries」一类）按整段一行登记在附表 B，不合成单值、不取中值、不去掉官方写在数字前面的限定词；同一数字在不同指代上各写一处的按指代分列，不做跨指代合并；同一句官方原句在两个页面各出现一次的，B 表也各登记一行，因为它的去重键是「页面＋数字＋单位」。B 表最终是 **6** 行、分布在 **5** 个页面、去重后的「数字＋单位」组合 **5** 组、单位词 **5** 种、五档 **1／1／3／0／1**、性质 **4** 种、整句截断 **2** 行；截断只发生在显示层，进表的每一句都先用未截断整句回原页核对，失败 **0** 句。它的输入是原句池的 **291** 条整句（覆盖 **31** 页），其中 **250** 句整句不含任何阿拉伯数字、本来就不在 B 表的取材范围里；另有 **13** 个「数字后面四词都对不上任何一档」的实例写进 `_tmp/w6_a7_bdrop.tsv` 供逐行复核，不进表——这一批绝大多数是接口示例里的对象 ID 与接口字段说明，不是官方对某项业务写下的数量，**"这个数是不是一个上限"的判定权在读者手里，不在这张表里**。另有 **1** 个可引用页面在池子里一句都没有（第 1.3 节点名），所以这一页在本表里是空的——**"这一页没有 B 表行"不等于"这一页没有数字"**，那一页上的内容是一组指向其它页面的导航链接，进不了句子层。

正文引句全部为整句、逐字回核到抓取件。整句的判据是抓取件的句池切分口径：句池按句子终止符切、不按版面标记切，因此官方 markdown 页里紧跟句子的小标题会被并入同一句；同时，长度不在 40–400 字符窗口内、段内含竖线或内联链接、以 `#` 或 `-` 起手的片段一律不入池。本轮正文引用去重后 **87** 句（引用标记 **87** 处，没有同一句在正文重复引用），分布在 **27** 个页面上；官方标题在正文被点名 **33** 处、覆盖全部 **32** 个登记页。引句里 **3** 句自带官方粗体排版标记 `**`（每句各一对，偶数，照抄原文、不删字符），**1** 句在同一句里带着 `## 小标题` 与紧随其后的另一句。这两类都不修补：本篇按句池原样照引，中文说明只覆盖小标题与粗体之前的句子本体，读者按附表登记的「页面＋句序」可回抓取件逐字复核。粘连发生在官方页面的排版层而不是本篇的转写层。另有一条硬规矩：含反引号字段名与枚举名的句子不整句引用，这些页面上的事实改用中文陈述并逐处标注出处页；**本轮正文实际引用的 87 句里，含反引号的句子是 0 句**，这条规矩没有被绕过。本轮引用的句子里没有带英文双引号且引号不闭合的句子（回核计数为 **0**）。

方法层的三张表各自回答一个问题，且都不越界：附表 E 把同一组 **27** 个维度并排放在 **5** 列主体上（目录总纲与库存页／消息形态页／模板与轮播页／商家端帮助页／订单与支付页）＝ **135** 格，**37** 格取到官方原句、**98** 格标作「官方未按这一列写该项」，该标注**只说明官方文本的写法，不说明该类主体没有该项规则**；去重引用页 **21** 个、跨格复用句 **2** 次、显示层截断 **20** 格。附表 C 把「官方是否写明」限成「词（组）× 站点类别」矩阵，**61** 个词（组）分属 **8** 个主题组 × **2** 类站点共 **79** 个非空格，句次 **617**、去重命中句 **250**（占全池 85.9%）、一句最多同时落进 **10** 个词格、**171** 句落进两格以上，**0** 个词按零命中登记，显示层截断 **47** 行。附表 D 只登记官方在页面里逐字写出的取值域：配置的 **10** 串取值域标记本轮 **2** 串在登记页里出现（合计 **3** 行）、另 **8** 串在扫过的每一行原文里零命中，最终 **1** 组、**2** 个取值、覆盖 **1** 页；另有 **2** 处官方写了标记却取不出两个以上取值、分布在 **1** 个页面，逐处连同原文行号写进 `_tmp/w6_a7_dmiss.tsv`。附表 F 把 **12** 个议题逐条对撞（两侧有句 **9**／只有一侧 **1**／两侧皆无 **2**，引用页去重 **12**、差异性质 **11** 种、复用 **0** 次、显示层截断 **7** 处），凡官方留白处一律登记为留白，不推断、不补齐。

页面层的四项事实单独交代，免得读者把"登记"读成"引用"：**第一**，本篇登记的 **32** 页正文全部取回，没有一页被标注"全篇不引用"；**第二**，**1** 页正文取回但池内零句——第 1.3 节点名的商品分享入口页，它的正文是一组指向其它页面的链接，进不了句子层；这一页在附表 B 至 F 里都不会出现，只在附表 A 留一行登记，它承载的导航由 1.3 节的中文陈述承担；**第三**，**32** 页全部在正文被官方标题点名（标题标记共 **33** 处），附表 A 的引用位置列没有一格留空；**第四**，反向的一条同样成立：正文整句引用落在 **27** 个页面上，其余 **5** 页被点名而未引句（目录链接页、商务设置页、零句页与两页只有导航的帮助中心页，逐页见附表 A），逐页状态由脚本实测而非人工填写。

本篇对"某个商品该不该上架""模板类别判得对不对""退款与资金怎么处理""巴西与印度哪套口径更新"一律不补；目录价格在哪些国家显示、集合审核具体看什么、支付按钮过期后钱怎么走，这些官方只写了一半的问题登记在附表 F，正文只写官方实际写了的那一半。地区差异只按地区分别写：巴西的订单会话、状态机与订单详情模板，印度的订单模板与订单状态模板，两套装在两章里，不合并成"WhatsApp 的订单规则"。

最后两处范围断言必须交代扫描口径，因为它们都是"穷举后没有"而不是"抽样没看到"：**其一**，本篇在 1.1 写"五类页面之间没有统一的对象图"，第 13.1 节把它收成"两套对象、两种消息、两条回调"。这一句的依据是：附表 C 的 8 个主题组共 **61** 个词（组）——A 目录与库存本体 14／B 消息形态与动作 12／C 购物车与结账 5／D 模板与按钮 5／E 订单与状态 9／F 支付方式与码 6／G 审核、政策与责任 4／H 接口与信号 6——在原句池 **291** 句里逐句扫过，命中落在不同的页面与不同的字段上；但这只覆盖本篇登记的 **32** 页，帮助中心与开发者文档里还有大量本篇未登记的页面，所以"没有统一的对象图"是对这批语料的陈述，不是对 WhatsApp 全部公开文本的陈述。**其二**，附表 F 有 **4** 个议题标成"只在附表登记、正文未并列两句"，逐条点名是：F3 交互式商品消息要不要模板、F10 关闭购物车后 24 小时内新建购物车如何处理、F11 商家能否主动清空客户的购物车、F12 订单详情模板的头图能不能用视频。这 4 行都能在附表 F 逐字回表，其中有一侧原句的按单侧登记、两侧皆空的按空白登记；本篇不在正文把它们并排成一句结论——**排不并排是叙述选择，不是"官方没有连接句"的断言**；读者若要把它们当通则使用，应自己回到那两页看官方在上下文中怎么连接这两句。

Telegram: https://t.me/one00190
