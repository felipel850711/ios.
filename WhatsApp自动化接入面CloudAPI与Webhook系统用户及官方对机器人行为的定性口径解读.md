@@TITLE@@WhatsApp 商业平台的自动化接入面：Cloud API、Webhook、系统用户与官方对机器人行为的定性——官方口径解读
@@SUBTITLE@@本篇是"WhatsApp 群发／群控五篇"中的第三篇（轴③），接在《消息类型、模板、质量评级与限额档位》之后，讲的是**平台把"程序化地发消息、被平台程序化地通知、把这套能力交给第三方"写成哪些字段、哪些状态与哪些条款**。全篇只读官方文本，共引用 **111** 个页面登记位（**106** 页正文已取回、**5** 页未取回且全篇不引用，详见附表 A 的 **112** 行）：Meta 开发者文档 **104** 页、Meta 商业帮助中心 **3** 页、法律与政策页 **4** 页。
@@INFO@@报告日期|2026 年 11 月 9 日
@@INFO@@分析对象|接入面层：Access Tokens Guide、WhatsApp permissions、In-App Signup 共 **3** 页，账号模型演进 **3** 页（Updates to WhatsApp Business accounts／Managing messaging accounts／Onboarding changes），solution-providers 全组 **30** 页（伙伴对照、应用复核、伙伴主导商业核验、多伙伴方案、嵌入式注册、号码与 WABA 在伙伴之间迁移的 7 页），webhooks 全组 **43** 页（overview／create webhook endpoint／override／message_echoes 共 **4** 页，reference 下 **39** 页＝`messages` 主页与 **19** 个子类型页共 **20** 页、其余 **19** 个事件参考页），Direct Send 侧 **13** 页，吞吐与健康与载入测试与实验位与输入指示器与工单支持 **7** 页，限额与总则与账号类 **11** 页。以上 Meta 开发者文档 **104** 页，另加帮助中心 **3** 页与法律政策 **4** 页，合计 **111** 页。
@@INFO@@视角定位|官方规则文本与官方接口文档的口径解读，属于**平台自动化能力的公开文档梳理**，不是操作指南。本篇**不提供**任何发送脚本、发送队列、并发与重试参数调优、账号池与多号轮换、注册与验证流程的操作步骤、限速或频控规避、风控与反垃圾检测绕过、封禁申诉话术；凡官方写成"步骤"的内容，本篇只登记**步骤的数量与名称**，不重述每一步怎么做。
@@INFO@@分类|**官方页面登记 111 个登记位 × 站点 × 引用位置 × 取回状态**（附表 A，**112** 行含合计；其中正文已取回 **106** 页、正文未取回 **5** 页且全篇不引用）× **数字归档 155 行五档 37／15／36／17／50，去重后 89 个数字、64 种单位、20 种性质**（附表 B）× **处置与生效档位 55 档、16 套体系、42 处官方未写明时长**（附表 C）× **枚举取值域 349 个取值、73 个取值组，其中脚本解析出 55 组 253 个取值、手定 18 组 96 个取值，覆盖 36 页、12 处说明被截断**（附表 D）× **三类伙伴逐项对照 12 个维度，其中官方对照表直接解析 7 个、手定 5 个，官方"未按类别区分"3 格**（附表 E）× **口径冲突与官方未写明 19 项，其中一侧为未写明 6 项**（附表 F）× **附表引句逐字回核 238 条，失败 0 条**× **正文英文引句逐字回核 151 条，未命中 0 条；另有 158 段标识符与短串不计入引句核验**。
@@INFO@@难度|进阶（本篇的难点有四处。**第一是"同一句话挂在三种主语上"**：`access token` 在令牌页的主语是**开发者身份**（direct developer／Tech Provider／solution partner 三种），`Messaging account` 在账号模型页的主语是**计费与回调的容器**，`system user` 在系统用户页的主语是**被授权的人员位**，三者不能互换。**第二是官方把同一个枚举写成三种样式**：换行列表式、行内反引号式与散文式，本篇按样式分列统计，不合并成"一个统一取值表"。**第三是"迁移"这个词在官方文档里指向 7 个不同的动作**：号码在伙伴之间、WABA 在伙伴之间、WABA 在方案之间、客户从方案迁出、经嵌入式注册、经商务套件、程序化，各页主语与前置条件不同。**第四是"自动化"一词的分布**：平台条款页（Terms for WhatsApp Business Platform）本篇取回件里 `automat*` 出现 **0** 次，而服务条款 **5** 次、消息指南 **8** 次、商业消息政策 **2** 次——定性句不在这份"专门管平台"的条款里。
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

前两篇讲的是"能发什么、发到什么规模、发得好不好"。这一篇讲**发之外的那一层**：一个非人工的程序要替企业发消息、要知道消息怎么样了、要知道自己的账号出了什么事，平台给了哪些接口面、这些面各自由谁授权、授权怎么被记录、又怎么被撤销。全篇按官方公开文本的表述回答下面十六个问题，每个答案都落回具体页面与具体句子：

一，平台把"程序化接入"写成哪几条入口，官方自己怎么命名它们。二，同一家企业接进平台时，官方要求先建立哪几种对象，对象之间是什么归属关系。三，`access token` 有三种，官方按什么把三者分开，各自代表谁。四，系统用户分两种角色，角色差别到底落在哪个词上。五，"业务资产访问"是一个开关还是两个开关，接口侧与管理面板侧的行为是否一致。六，一个号码可以同时挂在几个计费容器上，官方在什么条件下要求显式指定容器。七，伙伴分三类，官方对照表逐格写的是什么，哪些格子官方直接写"不区分"。八，把一个客户接进来、把一个号码从一个伙伴挪到另一个伙伴，官方登记了几个动作、几个入口。九，回调（Webhook）侧官方写出几个可订阅字段、几个可覆盖字段，两者的清单是否同一份。十，一条回调载荷的上限、重试、延迟与状态码，官方各在哪一页写、写成什么形式。十一，官方对"机器人／自动化行为"在条款里怎么定性，定性句落在哪几份文件里。十二，`Values can be:` 这一种句式在官方文档里一共枚举了多少个取值，本篇如何做到全部回核。十三，健康、状态页与载入测试三页各给出什么可观测面。十四，Direct Send 这一侧还剩哪些公开限额与公开留白。十五，哪些官方字段在页面里存在但没有任何说明，或说明与字段名互相矛盾。十六，本篇与另外两篇在哪些接缝上必须同时读才不会读错。

一句话概括本篇的读法：**这一层官方写得比消息层细，但"细"体现在字段与状态枚举上，不体现在跨字段的关系上**。本篇把前者逐格抄下来，把后者原样登记为留白。

# 1 来源层、术语与本篇不做的事

## 1.1 三类来源与各自的语言

本篇用到的官方文本分三类，三类的语体不同，能支持的结论也不同。

第一类是**开发者文档**，也就是 `developers.facebook.com` 下 business-messaging／whatsapp 路径里的页面。这一类是本篇的主体，语体是"字段说明＋取值枚举＋请求响应样例"，它能回答"接口暴露了什么、官方给这个字段写了什么值"。本篇附表 D 的 349 个取值全部来自这一类，且绝大多数来自表格行的"Values can be:／Value can be:"结构。

第二类是**法律与政策页**，本篇登记四条：`WhatsApp Terms of Service`、`WhatsApp Business Messaging Policy`、`WhatsApp Terms for WhatsApp Business Platform` 与 `WhatsApp Messaging Guidelines`。这一类的语体是义务性表述，它能回答"平台把哪类行为定性为违规"，不能回答"接口怎么调"。

第三类是**帮助中心页**，本篇登记三条，其中与自动化直接相关的是 `Unauthorized use of automated or bulk messaging on WhatsApp`。这一类的语体是面向企业的解释性表述，它常与第二类的同一件事换一套词，本篇在附表 F 里把这些换词逐处登记。

三类的分工在本篇很关键：**动作面在第一类，定性面在第二、三类**。所以本篇在讲完字段之后一定要回到条款页，否则会出现"官方文档里没写违规，所以不违规"这种误读——本篇不给出这种推论。

## 1.2 术语对照：中文口径与官方英文

| 本篇中文口径 | 官方英文 | 出现的主页面 |
|---|---|---|
| 号码容器／身份账号 | WhatsApp account（WAAC） | Updates to WhatsApp Business accounts |
| 消息账号／计费与回调容器 | Messaging account | Updates to WhatsApp Business accounts、Managing messaging accounts |
| 旧的合一体 | WhatsApp Business account（WABA） | Updates to WhatsApp Business accounts |
| 系统用户 | system user | Access Tokens Guide、Manage System Users |
| 业务资产访问 | business asset access | Access Tokens Guide、Manage System Users |
| 高级访问 | Advanced access | WhatsApp permissions |
| 嵌入式注册 | Embedded Signup | Onboarding changes、Embedded Signup 相关页 |
| 应用复核 | App Review | WhatsApp permissions、App Review |
| 伙伴主导的业务核验 | partner-led business verification | Partner-Led Business Verification |
| 回调／事件 | webhook／event | Webhooks overview 与 reference 下 39 页（19 个事件参考页、`messages` 主页与其 19 个子类型页） |
| 可覆盖字段 | override | Override Webhook |
| 载入测试 | load testing | Load Testing |
| 直接发送 | Direct Send | direct-send 全组页 |

这里有两组必须咬住的差别。第一组是 `WABA` 与 `Messaging account`：官方在演进页里写 `The legacy WhatsApp Business account (WABA) is becoming two distinct accounts.`，也就是说 **WABA 不是一个被替换掉的账号，而是被拆成两个容器**——号码归一个，模板、账单与回调订阅归另一个。第二组是 `system user` 与 `system user access token`：前者是"人员位"，后者是"这个人员位持有的凭证"，官方在令牌页里分别用 `System tokens rely on system users.` 划开这两层，并在下一句给出人员位的两种角色（管理员与员工），本篇不把它们混作一件事。

## 1.3 主语层级：谁的令牌、谁的回调、谁的账号

这一层是轴③最容易读错的地方，本篇把它写成一张主语表：

- **令牌**的主语是**开发者身份**。官方在 `The platform supports the following access token types. The type you use depends on who uses your application, and whether you are a partner.` 之后用三条项目符号按身份分流，不是按用途分流。
- **权限**的主语是**应用**。`Platform endpoints are gated by permissions.` 一句里被门控的是端点，被授予的是应用，而授予动作由"app users"完成。
- **业务资产访问**的主语是**系统用户 × 账号对**。官方要求 `you must grant **Partial** or **Full** business asset access for **both** accounts`，两个账号是 WhatsApp account 与 Messaging account，各自都要授。
- **回调订阅**的主语是**消息账号（容器）**。官方在演进页把 webhook subscriptions 明确放进 Messaging account 这一侧，而不是放进号码这一侧。
- **限额与账单**的主语也是**消息账号**。`Billing, spend limits, and analytics belong to the Messaging account rather than to the phone number` 是本篇读到的一句直接排他的表述。
- **伙伴方案（solution）**的主语是**两个业务实体之间的关系**，不是某个号码，所以它的状态机独立于账号状态。
- **条款义务**的主语是 `you`，而这个 `you` 在服务条款里指使用服务的主体，在平台条款里被定义为 `the company you are authorized to represent`。

## 1.4 本篇不做的事

本篇不写：任何发送脚本或发送服务、消息队列与并发参数、重试退避调优、账号池与多号轮换、注册与验证码获取的操作步骤、把号码批量挂到同一主体的做法、限速与频控规避、风控触发条件与反垃圾检测绕过、非官方客户端的实现或风险面、封禁申诉话术与模板内容代写。

凡官方文档以"步骤"形式给出的内容——号码注册的四个动作、伙伴入驻的四个阶段、系统用户令牌的生成路径——本篇只登记**数量与名称**，并把它们作为"官方把一个流程切成几段"这一口径事实来处理，不重述每段怎么做、需要点哪些面板。这条界线在 5.2、6.1 与 7 节各出现一次，本篇每次都停在名称层。

同时本篇不做跨体系推断：账号模型页的 `Phase 1／Phase 2`、应用复核页的时间、载入测试页的熔断、健康页的节点状态，这四套官方时间／状态刻度之间没有任何一份官方对齐表，本篇不构造对齐表。

# 2 自动化接入面：官方把"程序化"写成哪几条入口

## 2.1 三条入口与它们的官方命名

把本篇登记的 106 个已取回页面按"程序能做什么"归类，官方文本给出的是三条入口，而且这三条入口在文档树里就是三个平级目录：调用的入口（各类 API 端点）、被通知的入口（webhooks）、以及被托管的入口（solution-providers 下的伙伴与入驻体系）。

第三条容易被忽略，但它是本篇存在的一个理由：**平台把"第三方替企业做自动化"写成一等公民**，官方给这类主体的名字是 partner——`Partners are business entities that deploy value-added solutions as WhatsApp authorized service providers on behalf of their clients.` 这句是定义句，它把"替别人做消息自动化"直接放进授权服务体系，而不是放在灰色地带。

## 2.2 三种开发者身份，两条入驻路径

令牌页用三条项目符号把身份与令牌类型绑起来（见 3.1），伙伴总览页则把同一件事写成三种伙伴类别：`Solution Partners`、`Tech Providers` 与 `Tech Partners`，另加一类不需要 API 接入的主体。四条描述的差别落在**信用额度、账单方式与能否参加激励计划**上，不落在能力清单上——这一点在 6.2 的官方对照表里逐格可见。

入驻路径有两条，官方各用一页：成为 Tech Provider 的一页，与 Solution Partner 的四阶段一页。前者写着 `Your business must be verified before you can start the app review process.`，后者把流程写成四个阶段名（1.4 里说明过，本篇只登记阶段名）。两页共同点是把**业务核验**排在**应用复核**之前，这是接入面的第一个次序事实。

## 2.3 账号模型演进：一次拆分，三页说明

平台正在把旧的合一体拆开，官方用一个"警告块"给出时间口径：`The new account model becomes generally available on September 23, 2026`，并在同一块里写明到 2026 年十月中旬覆盖全部企业；另一句 `This documentation describes the new account model for all readers from September 23.` 说明文档自身的生效口径。

拆分的官方定义句是 `The legacy WhatsApp Business account (WABA) is becoming two distinct accounts.`，两个新容器各有一句：`WhatsApp account (WAAC) — a container to hold a single business phone number` 与 `Messaging account — a container for your templates, billing, and webhook subscriptions.`。对已经接入的集成，官方写 `Your existing IDs and API calls continue to work.`，并在演进页解释拆分的动机是 `Today, the WABA bundles phone numbers, templates, billing, webhooks, and partner access into a single entity.`。

这里有一个对"群控"话题最关键的官方表述：`A WAAC is always owned by the client or direct developer and can be shared across direct integrations and multiple partners.` 也就是说，**同一个号码容器被多个集成共享是官方设计目标**，而不是违规形态。本篇把这句留在事实层，不推导任何"因此可以怎样"的结论——因为共享是否合法合规取决于消息内容与用户同意，那是前两篇的范围。

第三页（入驻变化页）说明拆分如何体现在入驻流程里：`standard onboarding flows automatically create a separate Messaging account per partner and return the ID in the existing WABA ID field`，并强调 `Onboarding updates roll out in phases. Each phase is additive — your existing integration continues to work without code changes.`。

## 2.4 条款层怎么称呼"自动化"

本篇把 `automat*`、`bulk`、`auto-messaging`、`auto-dialing`、`non-personal`、`unofficial client`、`escalation paths` 这七组词在四份法律页与三份帮助中心页的取回件里各数了一遍（计数见 13 节），得到的第一件事实是：**这些词分布极不均匀，而且不在同一份"专门管这个平台"的条款里**。

- 服务条款把批量与自动发送列为被禁止的通信类型之一：`involve sending illegal or impermissible communications such as bulk messaging, auto-messaging, auto-dialing, and the like`，并在同一条里加上 `involve any non-personal use of our Services unless otherwise authorized`。
- 消息指南把自动化与"损害"绑定：`or use unofficial clients, bulk messaging, auto-messaging, auto-dialing, or automation to harm WhatsApp or our users.`——注意这条的动词是 harm，它不是"禁止一切自动化"。
- 商业消息政策给出本篇读到的一句许可式表述：`You may use automation when responding during the 24-hour window, but must also have available prompt, clear, and direct escalation paths.` 这是"允许＋附带条件"的写法，条件是人手可达的升级路径。
- 平台条款（Terms for WhatsApp Business Platform）本篇取回件里上述七组词全部 **0** 次。

于是本篇得到一个必须小心的口径结论：**"自动化是否允许"不写在平台专门条款里，写在服务条款、消息指南与商业消息政策里**，而这三份的句式不同——禁止句、限定句、许可句各一。本篇在 13 节把三句并列，不合并。

# 3 访问令牌与权限层

## 3.1 三种令牌，官方按身份分流

令牌页的开头一句决定了后面所有分流：`The platform supports the following access token types. The type you use depends on who uses your application, and whether you are a partner.` 三条项目符号分别是：direct developer 用 System User access token；Tech Provider 用 Business Integration System User access token；solution partner 则同时用两种——共享授信额度用前者、访问客户数据用后者。

这一句在本篇重要，因为它是**同一种凭证承担两种主体角色**的官方自认：同一个伙伴会既有自己的资产又有客户的资产，于是它需要两种令牌并存。本篇在附表 D 里把这三类令牌作为一组取值登记（组名"平台支持的访问令牌类型"），并在 11.2 说明这组是手定组。

## 3.2 系统用户令牌代表谁

官方给这类令牌的定性句是 `System user access tokens ("system tokens") represent you, your business, organization, or people within your business or organization.` 它的授权逻辑写在下一段：`System tokens rely on system users. Most endpoints check if the user identified by the token has access to the queried resource.`——也就是说**端点侧做的是一次成员资格检查**，检查对象是令牌背后那个系统用户。

这条逻辑解释了为什么第 4 节的"业务资产访问"是本层的真正闸门：令牌只是凭证，能不能读到某个资源取决于那个人员位被授予了什么。

## 3.3 权限、高级访问与错误码 200

权限页开头把端点与权限的关系写成一句：`Platform endpoints are gated by permissions.`，随后给出一般需要的两项——用于账号元数据与模板管理的 `whatsapp_business_management`，用于发消息与收回调的 `whatsapp_business_messaging`；另加三项按需要申请。

真正的接入面闸门在"App Review"一节。官方写：伙伴若让客户通过自己的应用访问客户数据，应用必须经过复核，`and you must be approved for **Advanced access** for any permissions your app needs`；缺该权限批准时，`If you lack **Advanced access** for a given permission, your clients cannot grant your app that permission.`。对具体端点，官方另写一句 `If your app uses the whatsapp_business_management permission to access WhatsApp accounts or Messaging accounts not owned by your business, you must have **Advanced access** for that permission.`，并把缺访问时的接口行为写成 `Without **Advanced access**, API calls return error code `200``。

反过来，直接开发者的豁免也是官方明文：`If you are a direct developer and only access your own business data, you do not need to undergo App Review or obtain **Advanced access** for any permissions.`

本篇在这里给出一个口径提醒：错误码 `200` 在同一页里既出现在"缺少高级访问"的表述中，也在其他页作为通用错误码出现。**官方没有把 200 定义成"权限不足专用码"**，所以本篇不把它当作诊断结论，只当作该句写明的返回值。

## 3.4 授权屏与站内订阅

权限授予的界面面在两份页里。Embedded Signup 侧，官方写授权屏 `allows your client to grant your app permissions for which you have **Advanced access** approval:`——这是一句"能授什么由高级访问批准范围决定"的限定。In-App Signup 侧则是另一条自动化入口：官方定义为 `In-App Signup lets you create opt-in deep links that WhatsApp users can click to subscribe to your messages.`，并写明该接口在若干市场不可用（欧洲经济区、英国、巴西、日本、韩国、尼日利亚、南非与土耳其八处），且该注册实体不绑定特定号码——官方给的可复用句是 `You can construct deep link URLs with the same signup ID and any phone number in the WhatsApp account hierarchy associated with your Messaging account.`。

这两句合起来是本篇读到的一对张力：一边是"授权屏把权限范围交给批准结果决定"，另一边是"订阅实体不绑定单个号码"。本篇把张力登记在附表 F，不做优劣判断。

# 4 系统用户与业务资产访问

## 4.1 两种角色，差别在"默认"

官方用一句给出角色枚举（`admins` 与 `employees` 两个词各自挂一个页内锚点），然后分别定义。管理员的差别在默认范围：`By default, admin system users have full access to all WhatsApp accounts (WAACs), Messaging accounts, and their assets owned by or shared with` 业务组合；员工则相反，`Employee system users must be granted access to the individual WhatsApp accounts and Messaging accounts that are owned by, or shared with, your business portfolio`。

本篇注意一个容易被读过的限定：管理员的默认访问**可以被收窄**——`You can override an admin system user's default business asset access by granting partial access per account.`，且官方补一句要求逐个账号授予。所以"管理员＝全量"是默认态描述，不是不可变属性。

## 4.2 两个账号都要授

无论角色，官方的要求句是一对二的：`Once created, you must grant **Partial** or **Full** business asset access for **both** accounts the system user needs to work with: the **WhatsApp account** and the **Messaging account**.` 这一句在本篇的账号模型章节里也成立，因为 2.3 说明一个 WABA 被拆成这两个容器；换句话说，**接入面在授权层要求你对"拆开的两半"分别授权**，即使这两半来自同一次注册。

## 4.3 级联只存在于接口侧

这是本篇认为最实用、也最容易被误传的一条官方差异。同一页用两条项目符号写出两个界面的不同行为：接口侧 `**API.** Granting business asset access on a Messaging account also applies that grant to the WhatsApp account it is linked to.` 并给出原因句 `This cascade exists because the WhatsApp account has no Graph API endpoints of its own yet`；面板侧则是一句短的否定：`**Meta Business Suite.** The panel does not cascade.`，并要求显式指派两个账号。

本篇把这段登记为附表 C 的一套体系（"系统用户访问阶梯"三档），并在正文强调一句方法结论：**级联是接口侧的临时行为，官方给出的理由是 WAAC 尚无自有端点**。它不是"两个账号等价"的声明，也不构成"授一次即可"的跨面结论。

## 4.4 管理入口与一个停用日期

伙伴侧的系统用户管理页把这件事写成程序化管理的前提：`Adding your System User to shared WhatsApp accounts and Messaging accounts allows you to programmatically manage the accounts.` 同页顶部有一个警告：`Embedded Signup v2 and v3 will be deprecated on October 15, 2026`，要求在此之前迁到 v4。这是本篇读到的第二个明确日期（第一个是 2.3 的账号模型通用可用日）。

这一页还登记了一个可缓存字段的事实：`You can cache the System User IDs for future use.` 本篇只把它当作"官方认可的一个运维事实"记录，不展开用法。

# 5 账号容器、号码与授信额度

## 5.1 容器与归属

官方在账号结构页里给出三个容器的分工，本节只引 2.3 与 4.2 未覆盖的一句：号码容器持有的是企业身份——官方给的身份定义括注是 `what WhatsApp users see and interact with in the WhatsApp app`，其内容清单里除号码外还包括业务资料与商品目录，并把业务用户名标注为未来能力。

管理侧页面（manage accounts）描述伙伴如何在共享资产里操作这些账号。本篇在此只做一件事：**把"账号"这个词在本篇的三种所指固定下来**——WAAC 是身份容器，Messaging account 是计费与回调容器，WABA 是被拆分的旧合一体。三篇前作里的"账号"含义各自不同，读者跨篇时必须回到这一节。

## 5.2 号码注册：官方登记为四个连续动作

注册页把这件事写成一句数量声明：`Registering business phone numbers is a four-step process:`，随后是四个动作名（本篇按 1.4 的原则只记名称，见附表 D 的"号码注册的四个连续动作"组）：在号码容器层级上创建号码、为该号码取得验证码、用验证码核验该号码、把已核验号码注册供接口使用。

同一页还有一句 `You can also perform all 4 steps repeatedly to register business phone numbers in bulk`。**本篇按范围红线不展开这一句**，只登记它存在：它是官方文本把重复执行该四步写成合法批量注册的位置，属于本文档梳理必须如实记录的一条；任何"如何批量"的做法不在本篇范围内。

## 5.3 号码管理面

伙伴侧另有一页专门管号码，本篇把它作为"号码这个对象在伙伴场景下有哪些可管理动作"的登记位。它与 5.2 的关系是：5.2 是接入的一次性动作，5.3 是接入之后的持续管理。官方在两页之间没有给出对齐说明，本篇不做推断。

## 5.4 授信额度与账单方向

Solution Partner 的定义里带有资金安排：官方写明这类伙伴 `… which can be extended to clients who they bring on board, thus removing the need for those clients to enter their own payment method during the onboarding process`，而 Tech Provider 恰相反——`Unlike Solution Partners, however, Tech Providers do not have credit lines.`。

额度可给也可撤。授信额度共享页给出的建议句是本篇登记的处置性表述之一：`we recommend that you revoke your credit line from the client`（其适用条件见附表 C"额度撤销"体系）。多方案会话页则说明额度与客户组合之间的归属关系。本篇把这两页合起来读到的事实是：**伙伴与客户之间存在一条由伙伴单方面控制的资金关系**，而这条关系的触发条件、撤销后果时长与申诉路径在官方文本里没有完整说明（附表 C 该体系多档时长为"未写明"）。

## 5.5 由伙伴发起的账号创建，以及加入管理伙伴

除了客户自己走嵌入式注册，官方还有两条"由伙伴起手"的路径：伙伴发起账号创建，以及把一个已有账号加入多伙伴方案。前者的官方机制句是 `If accepted, … will be created, and ownership of the WhatsApp account will be assigned to the client`（前半句的主语是"客户名下的 WhatsApp 账号"），官方另在这一页给出邀请的有效期：`Invitations that have not been accepted within 90 days will be canceled automatically`；后者解决的是"客户已经存在"的情形。

本篇在这一节只补一条与账号模型有关的事实：官方在演进页的受益清单里为伙伴写了一句 `Clients currently sharing a phone number with an agency or a direct API integration can now add you as a partner on the same number.`——**同号码加新伙伴**是被官方写成"新模型的好处"的场景。这两条路径的操作细节本篇一律不重述。

# 6 伙伴类型、对照表与复核：官方把"谁能替企业接入"写成几档

第 2 节讲的是技术入口，第 5 节讲的是资产容器。这一节讲**主体**：平台允许哪几类第三方替企业做接入，官方用什么维度把它们区分开，以及"复核"这件事在官方文本里其实有四套彼此独立的流程。

## 6.1 官方对"伙伴"的第一句定义

`solution-providers/overview` 页给出的定义句是全系列里最重要的一句，因为它把"代理企业接入"这件事写成了**受权身份**而不是技术角色：

> `Partners are business entities that deploy value-added solutions as WhatsApp authorized service providers on behalf of their clients.`

这句话里有三个可以单独取值的成分：`business entities`（主体必须是企业实体）、`authorized service providers`（身份是"被授权的"）、`on behalf of their clients`（动作是"代表客户"）。本篇只登记这三个成分在官方文本里各自被哪些字段约束，不推断"什么样的企业算合格"。

同一页把伙伴分成三类，`get-started-for-tech-providers` 页再补一句前置，把"商业核验"钉在"应用复核"之前：

> `Your business must be verified before you can start the app review process.`

三类名称在官方文档里并不总是同一组字面。`solution-providers/overview` 页用 `Solution Partner`、`Tech Provider`、`Tech Partner`；`partner-led business verification` 页把最高一档写成 `Select Solution` 与 `Premier`；`upgrade-to-tech-partner` 页则把 `Tech Partner` 写成从 `Tech Provider` **升级**而来的结果，而不是并列的第三类。本篇在附表 E 里按"哪个页面用哪套名称"分列，不做名称归一。

## 6.2 官方对照表：12 个维度、3 格"未按类别区分"

`solution-providers/overview` 页里有一张真正的三列对照表，表头就是三类伙伴，取值只有 `Yes` 与 `No` 两种。本篇把这张表逐格抄下并在附表 E 登记：**解析自官方对照表的维度 7 个，本篇手定的维度 5 个，合计 12 个维度**；12 个维度里有 **3 格**官方写成"不区分类别"——即官方在该格对三类给出同一个说法，这 3 格本篇不人为拆成三格。

这张表的读法约束有两条，比表本身更重要：

一，`Yes`／`No` 是**能力面**的取值，不是"授权强度"的取值。官方没有一列写"哪一类能发多少量"，量能只在限额与吞吐页查（第 12 节）。

二，对照表的三类与 `partner-led business verification` 页的 `Select Solution`／`Premier` 是**两套档位**。前者按业务模型分，后者按审核资格分。官方没有写这两套档位的换算表，本篇不换算。

## 6.3 伙伴对客户端资产能做什么：四份页面的四种主语

第 5 节登记了账号容器与授信额度，这里把"伙伴侧对这些容器有什么动作"按页面主语列一遍，因为四个页面的主语并不相同：

| 页面 | 官方写明的动作 | 动作的主语 |
|---|---|---|
| `manage-accounts` | 创建与查看消息账号 | 伙伴 |
| `share-and-revoke-credit-lines` | 共享与撤销授信额度 | 伙伴对客户 |
| `partner-initiated-WABA-creation` | 由伙伴发起账号创建 | 伙伴 |
| `adding-waba-to-mps` | 把账号加入多伙伴方案 | 伙伴与伙伴 |

其中 `share-and-revoke-credit-lines` 页把"额度"写成一个可以**给出去也可以收回**的对象，而不是客户自有的东西；`partner-initiated-WABA-creation` 页则把创建动作的主语从客户换成伙伴。这两句合起来决定了本篇第 15 节的一条结论：**在伙伴接入这条路径上，计费与身份的控制点不在发消息的那个号码上**。

## 6.4 权限、高级访问与应用复核：三件事不要混成一件事

`permissions` 页与 `access-tokens` 页讲的是"令牌能做什么"，`app-review` 页讲的是"这个令牌什么时候才能对真实客户用"。官方把两者的差别写成一句条件句：

> `If you are building an app that other businesses will be using, you must request Advanced access for any permissions your app needs.`

`app-review` 页给出的复核定义句：

> `App Review is part of … that enables us to verify that your app uses our Products and APIs in an approved manner.`

这一页同时写了复核的时长与提交物形态，两个数本篇进附表 B／C：**平均处理时长约 24 小时**，官方原句：

> `The average turnaround time for App Review is about 24 hours.`

需要说明这条 24 小时的性质：它是**周转时间的描述**，不是承诺时限，官方在同页把它写成 `average`。附表的 C 表按"可预期时长／官方未写明"分档时，这一条归入"官方写明但写明的是均值"。

提交物的两条硬规则在同页，本篇照抄原句，不解释怎么做：

> `Both written descriptions and screen recordings are required for each permission.`
> `you cannot submit a screenshot`, you must submit a screen recording

另外一条常被忽略的口径在同页末尾：

> `Submissions in draft mode will not be reviewed`

这里出现本篇附表 F 登记的一个同名不同义词：`Draft` 在应用复核语境里指**提交单本身的草稿态**（不提交就不会被审），在 `multi-partner-solutions` 页的 `Draft` 指**方案已保存但尚未发给伙伴**。两者不是同一个状态机，详见第 11.2 节。

## 6.5 伙伴主导的业务核验：三档伙伴资格、三次提交上限

`partner-led-business-verification` 页开头是一条资格警告，官方写法是"只对已批准的某两档开放"：

> `This feature is currently only available to approved` `Select Solution` `and` `Premier` `Solution Partners`

这一页的"前置条件"写成清单，其中两条涉及第 4 节的系统用户：使用令牌的那个系统用户必须是客户所在商业资产组合的管理员，并且必须已授予应用 `business_management` 权限。本篇只登记这两条条件存在，不登记达成它们的界面步骤。

这一页真正有分量的是一句**上限与失败后的归属**：

> `You are limited to three submissions for a given client.`

紧接着官方写明三次都用完之后的结果：客户必须自行完成核验。也就是说，伙伴代客核验的次数上限耗尽后，责任主体从伙伴回到企业自身。这一条在第 15 节和附表 C 一起构成"处置阶梯不只作用于消息，也作用于资格类提交"的证据。

## 6.6 多伙伴方案：一句定义、三条技术前置、一个 200 的上限

`multi-partner-solutions` 页的定义句：

> `Multi-Partner Solutions allow Solution Partners and Tech Providers to jointly manage client WhatsApp assets`

`jointly manage` 是这一节的关键：方案不是"把客户转授给另一个伙伴"，而是两个伙伴**同时**持有对同一批客户资产的访问。官方在这一页给出的技术前置有三条，本篇按原文逐条登记（这三条都是"必须已经如何"的状态要求，不是操作步骤）：

一，发起与接受所用的应用必须就是将来调用 API 时携带令牌的那个应用。
二，该应用必须已通过 App Review 并拿到 `whatsapp_business_management` 与 `whatsapp_business_messaging` 两个权限的高级访问。
三，`The app must be subscribed to the` `account_updates` `webhooks field`——即方案成立之前，回调订阅这件事本身就已经是前置条件。

这一页还给出本篇里少见的**伙伴侧数量上限**：

> `Tech Providers who are part of a solution can onboard up to 200 total new clients in a rolling one week period.`

官方在同句之后立刻补了计数口径：只有对该平台而言是新的客户才计入这 200。这条 200 与第 12 节的号码级限额不是同一根轴（那根轴限制的是发消息的对象数），本篇在附表 B 里把它归入"接入侧计数"。

方案的计费口径同页写明：经该方案的嵌入式注册入驻的客户，使用的是方案中 Solution Partner 那一侧的授信额度——`share the credit line of the Solution Partner associated with the solution`。这句与第 5 节"计费属消息账号而非号码"并不冲突，但它说明**在伙伴路径上，额度来源可以是伙伴的授信线**。

`multi-partner-solution-embedded-creation` 页是这一节的第二个页面：它把创建方案的动作从 App Dashboard 面板换成"在你的网页上放一个按钮"，前置条件写成两条，其中一条是权限名：

> `Your app must undergo App Review and be approved for advanced access for the` `manage_app_solution` `permission`

这一页正文是官方少见的**把界面步骤逐条写出来的页面**（九条连续动作）。按本篇的范围红线，这里只登记"该页把创建过程写成九个连续动作、且第一步是系统用户授权"这一事实，不重述任何一步的操作内容。

## 6.7 度量伙伴与埋点：一条只读例外

`measurement-partners` 页写的是一类完全不同的第三方：它不发消息，只读数据。官方给它的定义与权限面各一句：

> `A Measurement Partner is a third-party company that helps businesses measure the effectiveness of their marketing campaigns on our platform.`
> `Measurement Partners gain read-only access to Messaging account analytics data and webhooks`

同一页紧跟着两条**不能做什么**：企业必须已经拥有消息账号才能把分析数据共享给它；`Measurement Partners cannot create Messaging accounts`。把这三句并排读，本篇得到的口径是：官方在这一类伙伴上把"读"与"建"明确切开——这一刀在其他类伙伴页里并没有以同样的清晰度出现。

`pixel-tracking` 页讲的是另一件小事但很能说明官方的写法：它把"有多少访客点了嵌入式注册按钮"这件事写成嵌入一段脚本，并给出参数名。本篇只登记两件事：该页把脚本参数写成 `appId` 与 `feature` 两个键，`feature` 的取值在这一页固定为一个字符串常量；官方对这段代码的定位是**统计点击**，不是发送消息。按红线，本篇不抄录任何脚本代码。

# 7 迁移面：官方用"迁移"这个词指了七件不同的事

第 5 节末已提过：账号模型演进之后，"迁移"在文档里出现了七个互相不能替换的含义。本节按页面逐条登记，并且**把"这页已经不适用"这件事当作事实本身来登记**——因为这一组的九个页面里有四个页面自己声明已失效。

## 7.1 号码进入 Cloud API：先删账号，三分钟可用

`migrate-existing-whatsapp-number-to-a-business-account` 页的第一句就是本篇范围里的一条硬前置：

> `To use an existing WhatsApp Business app phone number with Cloud API, you must either delete your account`

该页同时写明删除之后会发生什么：三条要点——消息在队列中暂存、断开后**最多三分钟**该号码才可用、以及删除业务应用号码后再注册则**原有消息历史丢失**。本篇把"三分钟"与"历史丢失"分别入附表 B 与附表 C；该页列出的四个删除动作属于"官方写成步骤的内容"，按红线本篇只登记步骤数量，不重述操作。

同页最后一句把号码可用之后的动作交给另一个页面（`Once the number is available, follow the instructions to Add a Phone Number`），这是官方文档里常见的**动作移交**写法，本篇在附表 A 把它记为跨页依赖。

## 7.2 号码在伙伴之间迁移：两页同时声明"不支持"

`migrating-phone-numbers-among-solution-partners-programmatically` 与 `...-via-embedded-signup` 两个页面内容几乎相同，都以同一句警告开头：

> `Phone number migration isn't supported for accounts on the new WhatsApp account model.`

两页接下来的三条实质口径一致，本篇合并登记：

一，`If the destination WhatsApp account … already own the phone number, the request is rejected.`——目标账号必须本来就拥有该号码，否则请求被拒。
二，号码**不能**在账号之间搬移，官方在同句里给出的替代是"先断开再说"，而断开后的重新注册会走到第 7.1 节的流程。
三，`Sharing still works`——共享被明确留在支持面内。

这两页还各自留下一句自我作废声明，本篇认为这是整个自动化接入面里最重要的一句元信息之一：

> `The rest of this page describes the previous model`

也就是说：**官方没有删除这两页，而是把它们标为描述旧模型**。本篇因此把"页面存在"与"规则现行"当成两件事处理，附表 D 与附表 A 里都按此标注。

## 7.3 消息账号在伙伴之间、在方案之间迁移

`migrating-wabas-among-solution-partners-via-embedded-signup` 页同样以警告开头，但措辞换成 `Migrating a WhatsApp Business account between Solution Partners … part of the new WhatsApp account model.`——本篇在附表 F 登记这类"措辞强度差"：前者说功能"不支持"，后者说这件事"不属于新账号模型"，官方没有解释两者是否等价。

该页给出的两条实质口径：

一，客户**不需要**先从不合作的伙伴那里撤销共享即可开始与新伙伴合作；新关系的建立走"共享"而不是"转移"。
二，若要终止与某伙伴的合作，客户要在商务套件里撤销**两个**账号：先消息账号，后与其关联的 WhatsApp 账号。

`migrating-wabas-among-solutions-via-embedded-signup`（方案之间，经嵌入式注册）页则给出四条本篇真正用得上的规则：

一，迁移过程会为客户**新建**一个消息账号，源账号里的模板被复制到目标账号。
二，前置条件写成两句：两个方案所用的应用必须在同一个商业资产组合内，且 `The destination solution must also be in an Active state.`——目标方案必须处于 Active 状态，这句直接接上第 11.2 节的状态词表问题。
三，复制不是自动的：`Templates are not duplicated automatically in this flow.`，需要调用方自己用迁移接口做。
四，计费的分段句，本篇原文登记：

> `Messages delivered before migration is complete are charged to the old Solution Partner.`

## 7.4 方案之间经商务套件迁移：同一件事的第二套写法

`migrating-wabas-among-solutions-via-meta-business-suite` 页与上一页讲同一件事，但在模板这条上给出**相反**的取值：

> `Templates are automatically duplicated in the destination Messaging account and initially granted the same status as their source counterparts.`

这一对差异（一边"不会自动复制"，一边"自动复制并保留状态"）本篇不在正文里裁决，而是并列登记为附表 F 的一项，并给出各自的触发条件（嵌入式注册路径 vs 商务套件路径）。两页共同的部分则只有一份，即下面三条：

一，只有 `status` 为 `APPROVED` 且 `quality_score` 为 `GREEN` 的模板才有复制资格。
二，复制后模板会被重新检查类别归属，可能导致部分复制件被拒。
三，`template quality ratings are not duplicated`，所有复制件从 `UNKNOWN` 起步，且该评级**在前 24 小时内保持 UNKNOWN**。

第三条里的"24 小时"与第 6.4 节应用复核的"约 24 小时"是**两个不同对象的 24 小时**，本篇在附表 B 分两行登记，不合并为一个数。

## 7.5 客户从方案迁出：两条路径、同一个"不重新核验所以不停机"

`migrating-customers-off-solutions-via-embedded-signup` 与 `...-via-meta-business-suite` 两页讲的是"离开方案"而不是"换方案"。两页各给一句关于停机的口径，措辞不同但结论相同，本篇都抄下来：

> `Migrating a customer off of a solution via Embedded Signup does not require business phone number reverification.`
> `Migrating a customer off a solution via Meta Business Suite does not require business phone number reverification, so it eliminates any downtime caused by reverifica`

两页共同的前置条件只有一句，且是**权限级**而非步骤级：应用必须已经拿到 `whatsapp_business_management` 的高级访问。

两页也共同保留一条官方少见的**给客户的建议写法**：在迁出过程中客户可以选择新建一个消息账号；若如此，旧账号的模板需要按第 7.3／7.4 节的规则处理。

## 7.6 代持模型弃用、伙伴升级、迁移文档的目录本身

`obo-model-deprecation` 页给出的是一条三日期时间线，本篇逐字入附表 C：

> `March 24, 2025`：`is made available to all Solution Partners.`
> `September 29, 2025`：`last day to onboard clients to the OBO model.`
> `October 1, 2025`：`last day to transfer ownership of OBO model WhatsApp accounts to clients.`

该页另有三条后果：代持模型的所有权整体转为伙伴发起的创建方式；伙伴发起创建**不支持自动设置付款方式**，改为通过共享授信额度；以及客户**不能**在伙伴发起创建流程中被直接入驻进多伙伴方案，只能在之后被加入。

`upgrade-to-tech-partner` 页讲的是"从技术提供方升级为技术伙伴"。这一页值得注意的是官方在这里换了定义域：它先定义了五个界面／平台名称（开发者侧、`Quickstart panel`、`Enterprise Center`、`Supplier Connect`、`Partner Portal`），再给"升级后能得到什么"的四条好处清单。按红线，本篇只登记**名称与数量**，不登记升级流程本身。

`support` 页与 `business-customer-support` 页给出的是**目录级**事实，对本篇比流程更有用：`support` 页把"客户资产迁移"分成两组链接清单，`Tech Providers` 一组 4 条、`Solution Partners` 一组 4 条——**同一件事在两类伙伴那里各有 4 条文档路径**。`business-customer-support` 页则写明由伙伴代入驻的客户可以自行开 Direct Support 工单，并把账务类问题的主题名与请求类型名写成两个具体取值。这两页合起来是第 12.2 节支持渠道讨论的入口。

# 8 Webhook 基础设施层：定义、端点、字段、覆盖与订阅

第 9、10 两节讲事件内容，本节先讲承载事件的那一层。官方在这里的写法是"一个概览页把规则写完，再让每个事件页只写自己的载荷"，所以概览页 `webhooks/overview` 的十个二级小节就是本节的骨架。

## 8.1 定义句、载荷上限与重投窗口

`webhooks/overview` 页的定义句只有半行，但它同时钉住了三件事——发起方、协议形态与目的地：

> `Webhooks are HTTP requests containing JSON payloads`

该页的小节结构本篇按实测登记：`Create a webhook endpoint`、`Permissions`、`Fields`、`Override webhooks`、`Payload size`、`Webhook delivery failure`、`Mutual TLS`、`IP addresses`、`Troubleshooting`、`Learn more`——**十个**。

两条与容量相关的数值，各自原句：

> `Webhook payloads can be up to 3 MB.`

重投窗口与频率：

> `Meta retries delivery with decreasing frequency until the request succeeds`

这一句官方在原页把窗口写成"最长 7 天"，并同时给出触发的判定条件：**收到 200 以外的任何 HTTP 状态码**都算投递失败。同页还补一句本篇很在意的副作用：重投会发给**所有**已订阅该消息账号相应字段的应用，因此 `These retries can result in duplicate webhook notifications.`——重复通知是官方明文承认的正常现象，不是异常。

`webhooks/create-webhook-endpoint` 页给出验证握手与证书要求。握手部分的三行是官方原文的参数名，本篇只登记名字与响应规则：请求带 `hub.mode`、`hub.challenge`、`hub.verify_token` 三个参数；验证通过要**回 200 并把 challenge 原样返回**，不通过则回 400 级状态码。证书要求一句很干脆：

> `Self-signed certificates are not supported.`

该页另有一句关于 `mTLS` 的范围限定：启用与关闭不能在消息账号或业务号码层面做。这句直接接上第 8.2 节的"层级"问题。

## 8.2 覆盖式回调地址：14 个字段能覆盖，其余一律走默认

`webhooks/override` 页讲的是"同一应用可以给不同号码／不同账号指定不同的回调地址"。官方先写判定顺序（先看有没有为该业务号码指定的备用地址，再看有没有为该消息账号指定的），再把范围钉在一个清单上。本篇实测该页 `Supported webhook fields` 清单**恰好 14 行**：`messages`、`message_echoes`、`calls`、`consumer_profile`、`messaging_handovers`、`standby`、`group_lifecycle_update`、`group_participants_update`、`group_settings_update`、`group_status_update`、`smb_message_echoes`、`smb_app_state_sync`、`history`、`account_settings_update`。

清单之后的官方限定句：

> `For field types not listed here, … default callback URL.`

该页还专门把**模板类与账号类**字段排除在外——即 `message_template_status_update`、`message_template_quality_update`、`message_template_components_update`、`template_category_update` 这四类模板事件不受覆盖地址影响。这一句与第 9.3 节的模板事件分组是配套的。

这里出现本篇附表 F 登记的一组清单不一致：概览页 `Fields` 表登记 **20 个可订阅字段**，覆盖页列出 **14 个可覆盖字段**，而 14 个里有 **9 个**根本不在概览页那 20 行的表里（`message_echoes`、`consumer_profile`、`messaging_handovers`、`standby` 与四个 `group_` 字段、`account_settings_update`）。本篇因此把"可订阅"与"可覆盖"写成两根轴，分别计数，不取并集当"字段总数"。

## 8.3 概览页的 20 个可订阅字段：官方在字段表里就写好了事件定义

`Fields` 那一节的表格是两列（字段名、说明），但第二列的说明句其实是**各事件参考页开头句的复用**——例如 `account_alerts` 行以 `The` `account_alerts` `webhook notifies you of changes to` 起句，与它自己的参考页首句一致。这个现象在本篇第 10、11 节很重要：**同一句定义在文档里出现两次，一次在索引表里，一次在专页里**。官方没有写"两处若不一致以哪处为准"。

该表的 20 行按官方自己的命名可以粗分四组（本篇不在正文做归类结论，只登记分组事实）：账号层 4 个（`account_alerts`、`account_review_update`、`account_update`、`business_capability_update`）、模板层 4 个（`message_template_components_update`、`message_template_quality_update`、`message_template_status_update`、`template_category_update`）、号码层 2 个（`phone_number_name_update`、`phone_number_quality_update`）、消息与其他 10 个（`automatic_events`、`history`、`messages`、`partner_solutions`、`payment_configuration_update`、`security`、`smb_app_state_sync`、`smb_message_echoes`、`user_preferences`，加上概览页与覆盖页共享的口径差异）。四组计数 4／4／2／10 之和恰为 20，本篇以此说明 20 这个数字的构成，而不是把它当成"20 种事件"。

## 8.4 令牌、验证串与回调服务器地址：三处安全口径的落点

`webhooks/reference/security` 页讲的是"号码的安全设置被人改了"这件事，它的三个触发事件全部以人为主语，官方写法是**界面动作**而不是 API 调用：

> `A Meta Business Suite user clicks the` `Turn off two-step verification` `button in` `WhatsApp Manager`
> `A Meta Business Suite user changes or enables the business phone number PIN using` `WhatsApp Manager`

这一页对本篇的价值有两点。**其一**，载荷里带 `event` 与 `requester` 两个字段，`requester` 的取值类型是商务套件用户 ID——也就是说平台把"谁改的"作为一个回调字段提供给企业。**其二**，这页的三个触发事件都是**关闭或变更口令**方向，官方在这一页没有写"启用成功"的对应事件。本篇把这一条登记为官方未写明，不做任何推测。

同一层的其余安全口径分散在别处：验证串由端点创建页写（第 8.1 节）、mTLS 由概览页与端点创建页两处写、Meta 回调服务器 IP 的获取方式概览页给了一条查询命令与一个 CSV 下载入口，并紧跟一句官方自己的警告：Meta 会周期性更换 IP。按红线，本篇不抄录该查询命令，只登记"官方建议改用 mTLS 以避免维护允许地址列表"这一句结论。

## 8.5 逐个账号订阅：伙伴侧最容易漏的一条

`solution-providers/manage-webhooks` 页讲的是伙伴代客户订阅回调，这一页的定义句把资产模型又重复了一次：

> `Messaging accounts and their assets are objects in the Facebook Social Graph`

而真正属于"口径"的是这句加粗的规则，它解释了为什么很多伙伴只收到自己账号的事件（原句在中间挂了一个跳转，本篇按片段引用）：

> `You must … individually subscribe to every Messaging account … for which you want to receive webhooks.`

同页还写明：如果你有多个消息账号，就要重复这个过程多次；订阅状态本身可以用查询接口读回来核对。这一页开头另挂了一条**日期型弃用警告**，本篇入附表 C：

> `Embedded Signup v2 and v3 will be deprecated on October 15, 2026`

该句还补一句"包括其公开预览版本"，并给出动作方向（迁移到 v4）。这一条是第 12.2 节"官方把时限写在警告块里"这一写法模式的例子。

# 9 回调事件分组：24 个事件页、触发条数与字段名是官方唯一给的粒度

官方给每个事件页的骨架是三件：**触发清单（Triggers）、载荷语法（Syntax）、字段取值表**。本节按账号层、模板层、号码层、方案层、其他五组登记，每组只报三件事：页面里触发事件的条数、`event`／`status` 类字段的取值个数、以及官方在这一组里没写什么。

## 9.1 事件页的三段骨架与 messages 的例外

事件页的第一句几乎全是同一个模板：

> `This reference describes trigger events and payload contents for the Messaging account`

`webhooks/overview` 页把订阅动作与字段名钉住，事件页只负责载荷；`automatic_events` 页是这一组里少见的**由平台侧检测驱动**的事件——它的两条触发都是"在会话里检测到一次留资或一次购买"，官方原句：

> `A lead event is detected in a chat thread between a business and user who has messaged the business via a Click to WhatsApp Ad.`

这一页的边界由来源限定：触发只覆盖经点击转 WhatsApp 广告进来的会话。第 12.4 节的实验页与第 10.1 节的载荷字段里都会再遇到"来源标识"这件事——平台在回调里给来源，但不给来源之外的推断。

## 9.2 账号层：4 页、3＋3＋13 条触发与一份告警文案枚举

`account_alerts` 页 3 条触发，分别对应：整个商业资产组合的号码限额提升被拒或决定被延期；某号码的官方商业账号状态被批准或拒绝；某号码的业务头像被删除。第一条是这一组里唯一的**组合级**告警，官方原句：

> `An increase to the messaging limit of all of a business portfolio's phone numbers is denied`

真正重要的是这一页的 `ALERT_DESCRIPTION` 字段：它是一个**枚举的文案**，不是代码。本篇在第 6.5 节和第 11.3 节都要回到这里，因为"不向个人授予官方商业账号"这句结论性表述，全篇语料里**只出现在这个字段的示例文案中**，而不在 `official-business-accounts` 专页里。该处原句：

> `We do not grant official business accounts to individuals`

`official-business-accounts` 专页本身用五项资格条件描述申请主体，五项都以 `the business` 起句，全页没有 individuals 一词——这是附表 F 第 18 项的内容。

`account_review_update` 页 3 条触发：批准、拒绝、决定被延期或在等待更多信息。这一页是本篇唯一一处"审核结果"与"审核中"并列成三个取值的页面。

`account_update` 页的 13 条触发是本系列目前见过最长的单页触发清单，本篇按官方原文分四类登记（不合并、不重排）：

| 类别 | 触发条数 | 官方写法里的对象 |
|---|---|---|
| 核验与账号存亡 | 3 | 伙伴主导核验的批准／拒绝／废弃；消息账号被删除 |
| 共享关系 | 2 | 账号被共享或撤销共享（官方称 install／uninstall） |
| 政策与资格 | 4 | 违反政策或条款；受限；具备国际验证类费率资格；接受某接口条款 |
| 属性与档位 | 4 | 主要经营地设定；授予广告账号访问；量级定价档更新；设备更换后的下线与重连（两条带新标记） |

其中"违反政策"与"因政策违规或处置动作而被限制"是**两条独立触发**，官方原文分别是 `Your WhatsApp account violates Meta policies or terms` 与 `Your WhatsApp account is restricted due to policy violations or enforcement actions`。这两句的差别正是第 15 节要讲的：**违反**是一件事，**受限**是另一件事，平台用两个回调把它们分开告诉企业。

## 9.3 模板层：4 页、1＋1＋5＋2 条触发与"自动流程"这个措辞

四条模板事件的触发条数分别是：`message_template_components_update` 1 条（`A template is edited.`）、`message_template_quality_update` 1 条（`A template's quality score changes.`）、`message_template_status_update` 5 条（批准、拒绝、停用、归档、取消归档）、`template_category_update` 2 条。

`message_template_status_update` 页的 5 条本篇全列，因为它就是第 12 节"模板生命周期"的官方状态来源；`template_category_update` 页的 2 条值得整句读，因为**这里出现了官方对"自动"的正面用法**：

> `The existing category of a WhatsApp template is changed manually or by an` `automated process`

也就是说：在模板层，官方**主动**用了"automated process"来描述平台自己的类别改判动作。这与第 13 节要讲的"自动化消息"的贬义用法形成对照——同一个形容词在官方文本里既指平台的处理，也指被禁止的行为，本篇在附表 F 登记这一处所指方向不同但用词相同。

`message_template_status_update` 页还挂着与第 12.1 节配套的两条：**限速类事件与暂停类标题**在该页有独立取值（`LIMIT_EXCEEDED`、`RATE_LIMITING_PAUSE`），而账号级限速本身在限额页讲——这就是附表 F 第 11 项"同一措辞分属两层"。

## 9.4 号码层：3 页、2＋1＋2 条触发

`phone_number_name_update` 页 2 条触发，都是"显示名被审核"这一件事的两个来源：新建号码的显示名被审；已批准的显示名被编辑后再审。

`phone_number_quality_update` 页只有 1 条触发，但它在本篇里最有用，因为它把第 12.1 节的吞吐档位挂到了回调上：

> `A business phone number's throughput level changes.`

`business_capability_update` 页 2 条触发（消息账号被创建；账号层级或组合级业务能力变化），并在说明句里用括注点名"例如消息限额"。这一页是**能力变更与企业可见字段之间唯一的桥**，官方没有给"哪些能力算在这里"的封闭清单——括注的写法本身就是未写明，本篇按此登记。

## 9.5 方案层：partner_solutions 的 6 条触发与三个取值域

`partner_solutions` 页给多伙伴方案的状态变化提供了回调，其 6 条触发本篇逐条列（每条官方都带一个 `solution_status` 取值）：保存为草稿、请求已发给伙伴、伙伴接受、伙伴拒绝、伙伴请求停用、方案已停用。其中官方在第 5 条**没有写取值**，其余 5 条各写一个：`DRAFT`、`INITIATED`、`ACTIVE`、`REJECTED`、`DEACTIVATED`。

这一页与 `multi-partner-solutions` 正文页给出的取值域**长度不同**：正文页的 `SOLUTION_STATUS` 字段表给 7 个取值（含 `PENDING`），回调参考页给 6 个（无 `PENDING`）；同一页的**面板状态表**又用第 6 套专有名词标签（`Active`、`Deactivated`、`Draft`、`Inactive`、`Pending`、`Pending deactivation`）。这三套词表是第 11.2 节的主要内容，这里只登记一件事：官方没有写"面板标签 `Inactive` 对应枚举 `REJECTED`"这一映射，映射关系在本篇属于推断，故不给出。

`partner_solutions` 页另有 `event` 字段的 2 个取值（`SOLUTION_CREATED`、`SOLUTION_UPDATED`），与状态取值是**两个正交字段**，本篇在附表 D 分两组登记。

## 9.6 其他三类：付款配置 3 条、用户偏好 2 条、应用状态同步 4 条

`payment_configuration_update` 页 3 条触发（连接到支付网关、断开、配置变为生效），官方把它限定在印度市场的支付接口上——这一页的 3 条本篇全列，因为它是整个事件层里唯一以"第三方金融配置"为对象的组。

`user_preferences` 页只有 2 条，但这两条在"群发／群控"议题上的分量比它字数多：

> `A WhatsApp user stops marketing messages.`
> `A WhatsApp user resumes marketing messages.`

官方在这两条里把主语写成 **WhatsApp user**，把动作写成 `stops`／`resumes`，对象写成 `marketing messages`。也就是说：**用户这一侧有一个可逆的偏好开关，且平台用回调把它告诉企业**。本篇只登记这个开关存在、可逆、且范围是营销类消息；官方在这一页没有写企业能否绕过该开关，也没有写它是否影响其他类别——这两条都登记为未写明。

`smb_app_state_sync` 页 4 条触发（伙伴同步业务应用联系人；已入驻客户在四种情况下由平台侧发起同步），`history` 页 2 条（伙伴同步业务应用聊天记录的两个方向）。这两页的触发句都带很长的链接文本，本篇在附表 D 按"截断说明"标注；它们的共同点是：**同步类事件的触发方被写成 `partner`**，即这类回调记录的是"有人做了同步"，而不是"用户做了什么"。这一层与第 10.3 节的 `smb_message_echoes`（回声类）合起来，构成业务应用与平台接口共存时的三页回调面。

# 10 messages 回调的子类型页：一套骨架、二十二个页面、五十条触发

## 10.1 骨架相同的十页

`webhooks/reference/messages` 主页先把这个字段能描述的两件事钉住：`The messages webhook describes messages sent from a WhatsApp user to a business`，以及出站消息的状态。本篇统计，围绕这一个字段展开的参考页共 **22 页**：主页 1 页、`messages/` 下的子类型页 19 页、再加上第 10.3 节单列的 `smb_message_echoes` 与 `history` 两页。

19 个子类型页里有 16 页共用同一句开场骨架，官方句式是 `This reference describes trigger events and payload contents for the Messaging account`，句末换成该页对应的载荷类别。本篇把这句骨架拆成三个可核对的成分：**触发（Triggers）**、**载荷（payload contents）**、**所属字段（Messaging account messages webhook）**。这一节覆盖主页与 9 个纯内容类子类型页（`text`、`image`、`audio`、`video`、`document`、`sticker`、`location`、`contacts`、`reaction`）。

本篇逐页统计了各页 Triggers 清单的条目数，19 页合计 **50 条**。内容类九页的分布是：`text` 4 条、`image` 4 条，`audio`、`document`、`location`、`contacts`、`reaction`、`sticker` 各 2 条，`video` 3 条。四页与三页的差别不在"媒体种类多"，而在官方把**转发**与**从商品目录里点按钮发来的消息**单独算作触发情形。以 `text` 页为例，四条里有两条分别是转发与目录按钮：`A WhatsApp user forwards a text message to a business phone number`。这条口径对本篇的意义是：**"用户发来的消息"在官方文本里不是一个事件，而是若干个来源不同的事件**，来源不同，回调里可取的上下文就不同。

`reaction` 页的对象被限定得很窄：`messages containing a reaction to a previous message sent by a business`——回应必须挂在这条业务**此前发出的**消息上。本篇认为这是内容类页里唯一把"方向"写进定义的一句。

## 10.2 交互、系统与状态九页

第二组九页（`button`、`interactive`、`order`、`group`、`edit`、`revoke`、`system`、`errors`、`status`）承载的是"带动作的消息"与"消息的命运"。三处实测差别值得单列。

第一，触发条数的极值都在这组里。最多的是 `status` 页，**7 条**；最少的是 `button` 页与 `order` 页，各 **1 条**。`button` 页那唯一一条是 `A WhatsApp user taps a quick-reply button in a template message`——注意它把触发面钉在**模板消息里的**快捷回复按钮上，而不是所有按钮。

第二，`status` 页的取值与触发条数**并不对应**。该页给 `status` 字段列出的取值是 5 个，按页内顺序为 `delivered`、`failed`、`played`、`read`、`sent`，其中 `played` 的定义句把对象限得很死：`Indicates the first time a voice message is played`——只有**第一次**播放才产生这个值。而 Triggers 有 7 条，因为"发不出去"与"投不到设备"被写成两条，群聊里的"已发送"也单独一条。本篇把这类"取值数 ≠ 触发数"记为口径落差：**读回调的人按取值分桶，读文档的人按触发分条，两套数不是一回事**。

第三，出站状态的回调数量被官方写成一个上限而不是等式。主页原句是一条出站消息最多可以收到 **3** 条独立的状态回调：`up to three separate webhooks (one for a status of sent, one for delivered, and one for read)`，同时主页也明确这类回调不携带消息内容，只携带状态。本篇据此把"状态回调"定义为**只有生命周期信息、没有载荷语义的那一类事件**。

`edit` 页与 `revoke` 页偏离了 10.1 的骨架句式：它们写成 `This reference describes edit events and payload contents`、`This reference describes revoke events and payload contents`，把"trigger events"换成了具体事件名。本篇不把这当成文字瑕疵，而是当成一条口径：**这两类事件不由"用户行为"触发，而由"对既有消息的改动"触发**，所以官方没有给它们套上"trigger"这个词。`status` 页是第三个偏离者，但偏离的方式不同：它保留了"trigger events and payload contents"，只把所属字段从"the Messaging account `messages` webhook"写成"Messaging account status `messages` webhook"。本篇因此把 10.1 的"16 页逐字共用同一句开场"与这里的"3 页各以自己的方式偏离"合起来记：**这 19 页的开场句不是模板自动生成的产物，而是 19 次分别维护的结果**。

`errors` 页与 `status` 页共同构成出站错误的两个落位。本篇在附表 A 里登记的三处错误落位口径是：系统／应用／账号级错误出现在 `value` 对象的属性里，入站消息错误出现在 `messages` 数组里，出站消息错误出现在 `statuses` 数组里。三处不是同一条通道的三种写法，而是**三条不同的通道**。

## 10.3 三个边界页：unsupported、smb_message_echoes、history

`unsupported` 页是入站侧的兜底：当一条入站消息无法被描述时，官方仍然发回调，但把类型置成 `unsupported`，并把原因放进这一页的错误结构里。本篇实测该页 Triggers 有 **3** 条。它与另外两页的分工是主页那条"三处落位"清单直接给出的：入站消息错误指向 `unsupported` 参考页，出站消息错误指向 `status` 参考页，`errors` 页只承载系统／应用／账号级错误。也就是说 **`errors` 页与 `unsupported` 页不是一对**，前者管平台侧，后者管看不懂的那条入站消息。

`smb_message_echoes` 与 `history` 两页在结构上最像子类型页——它们同样以 `This reference describes trigger events and payload contents for the Messaging account` 开头——但句末的字段名不再是 `messages`。这两页对本篇的价值在于它们暴露了两个容易被忽略的口径：

一，**回声类回调的触发者是"在客户端里发消息的那个人"，不是接口调用**。`smb_message_echoes` 页把通知对象写成商务客户经 WhatsApp Business app 或关联（linked）设备发出的消息，并且同页第二条触发是"该客户在 Business app 里撤回一条此前已发出的消息"。也就是说，这一页覆盖的是**没有经过接口的那部分出站流量**。本篇认为这是自动化接入面里唯一把"客户端侧发送"也纳入回调范围的页面。

二，**历史同步是一个有窗口的批量事件**。`history` 页把同步范围写成对批准之后触发的一系列回调，覆盖该商业客户上云之日起 **180 天**内收发的消息；但同一页接着排除了两类内容：群组聊天消息不纳入，媒体消息不携带媒体资源 ID——后者由额外的 history 回调单独发送，且只覆盖上云起 **14 天**内发出的媒体消息。这条 180 天与第 12 节的留存口径无关，它是"伙伴可以替客户读回多久的历史"，属于**账号容器**层的能力边界，本篇因此把它登记在附表 B 的时长档而不是处置档。

`history` 页还有一处对本篇很重要的写法：它把同一事件分成两条触发（客户已批准／客户已拒绝），并在拒绝分支上单独给出语法与参数表。本篇在附表 D 里登记这一点，因为**同一个字段的取值域会因为"事件分支"而不同**，只看字段名会漏。

# 11 取值域的三种写法：为什么同一个字段会有两个可读口径

## 11.1 冒号式：官方最规整的一种

本篇把语料里所有"某字段的取值有哪些"的表述分成三种写法，并在附表 D 里逐组登记。**冒号式**是主力：官方在参数表里写 `Values can be:`，后面用分隔符串起若干取值。本篇按这一句式解析出 **55** 组、**253** 个取值；其中绝大多数来自消息参数表与回调参考页。

冒号式的好处是可解析，坏处是**它只在一页内有效**。本篇实测，同一批字段在"参考页"与"功能页"上经常给出不同的清单长度——最典型的是第 8.2 节登记的回调字段：概览页与覆盖页的字段集合相差 **9** 个互不重合的项。本篇因此不给"某字段有几个取值"这种问法一个数，只给"在哪一页有几个"。

## 11.2 行内式与散文式

第二种写法把取值直接写在说明句里，例如第 10.1 节 `reaction` 页那句、以及"类别只能是 utility 或 authentication"这一类限定；第三种写法是散文段落，取值散在句中，脚本无法稳定切分。本篇在附表 D 里把这两种合计为 **18** 组、**96** 个取值，全部人工核定。

需要提醒的是：**行内式常常是更强那条规则**。以 Direct Send 为例，"支持的类型"页里那句要你在请求体加 `category` 为 utility（或在支持处用 authentication）的话，写在正文里而不是参数表里，但它决定了整条链路能不能用（见第 13.1 节）。本篇把这类"写在散文里的硬约束"单独标出，避免读者只在参数表里找约束。

## 11.3 三种跨页不等价

本节只列本篇实测到的、会直接影响读法的三类：

一，**同名不同域**。第 9.5 节已登记：`SOLUTION_STATUS` 在 `multi-partner-solutions` 正文页给 **7** 个取值（含 `PENDING`），在 `partner_solutions` 回调参考页只给 **6** 个（无 `PENDING`）；同页的面板状态表又用第三套标签。二，**同页不同版**。`status` 参考页在载荷示例里用两条注释给 `conversation` 对象写了版本条件：该对象只在 `sent` 状态、且 `delivered` 或 `read` 之一时带上，并且 v24.0 起的回调整段省略它，除非这条回调对应的是一场免费入口窗口会话；同一页另一条注释给 `pricing` 对象写了互斥规则——同一个对象只能出现在两条回调其中之一。本篇把这两条注释作为版本节点的口径登记在附表 C。三，**同字段不同分支**。第 10.3 节 `history` 页的批准／拒绝两支、`status` 页的 5 个取值与 7 条触发，都属于"字段名相同、可枚举集合随分支变化"。

这三类不等价没有一类是"文档写错"。本篇的读法是：**官方把取值域当成页面局部事实来维护，而不是当成全局契约来维护**；因此任何按字段名做全局字典的实现，都会在这一层遇到无法自动弥合的缝隙。附表 D 共登记 **36** 页、**12** 处说明被截断的取值组，就是这些缝隙的清单。

# 12 吞吐、延迟、健康与测试：官方给出的容量口径

## 12.1 吞吐档位与限额档位是两套数

吞吐侧的数本篇已在附表 B 登记：每个已注册业务号码默认 **80** 条每秒，满足资格后自动升到 **1,000** 条每秒，同一号码同时用于商务应用与云端接口时固定为 **20** 条每秒。这里最容易被读错的是资格句：升档看的是 **24 小时移动窗口内、在客服窗口之外触达的唯一用户号码数**达到 100K 一档，而不是"你发了多少条"。吞吐页还补了一句限定：吞吐统计**含入站与出站、含全部消息类型**，也就是说第 8 节那些回调并不是免费的——它们与你的出站消息同享一份容量口径。超出档位时接口持续返回错误码 `130429`，直到回落到允许水平之内。

限额侧是另一套数，本篇在 12.1 单列是因为经常有人把两者混成"每分钟能发多少"。限额页的定义句把它钉在"唯一号码"上：它衡量的是一个业务资产组合在移动窗口内**能触达多少个不同的人**。新组合的起点是 **250** 个唯一号码，官方列出的更高档位有 **4** 档：2,000（走扩展路径）、10,000、100,000、Unlimited（后三档走自动扩档）。其中 2,000 那一档的自主路径之一被写成一条数量条件：**30** 天滚动窗口内向唯一号码送达 **2,000** 条模板消息。

`upcoming` 页另给两条：限额的计算口径自 **2025-10-07** 起由"按号码"改为"按业务资产组合"，且改口径之后升档时长从现行的 **24 小时**缩到 **6 小时**以内。本篇特别把"24 小时"与"6 小时"都登记进附表 B，因为它们是**同一件事在两个时间点的两个数**，任何一个单独引用都会误导。

## 12.2 状态页与健康接口：两个层级，三份清单

状态页与健康接口是两件不同的东西，本篇分别登记。

状态页侧：官方列举的服务共 **5** 项——`Cloud API`、`Cloud API - Localized Storage`、`WhatsApp Business account management`、`Embedded Signup`、`Marketing Messages API for WhatsApp`；功能清单为 **4** 项。本篇实测"该页提供以下功能"这一句连同它那 4 条列表**在同一页里出现了两次**，两处文字相同。本篇不判断这是排版重复还是抓取产物，只在附表 A 标注该页正文含重复块，并把 4 项按去重后计数。上月可用性数字的补齐时限是**新日历月第 10 个工作日**；连续 **10** 分钟无法收发的判定门槛写在同一页。

健康接口侧：官方列出带 `health_status` 字段的节点共 **3** 个（业务账号、业务号码、消息模板），而响应示例里的 `entity_type` 取值有 **5** 个（`PHONE_NUMBER`、`MESSAGE_TEMPLATE`、`WABA`、`BUSINESS`、`APP`）。**3 与 5 的差是本节最有信息量的一处**：能被查询的节点是 3 个，但一次查询返回的实体是 5 类，因为响应会把上层的业务组合与应用一并纳入聚合。官方给聚合规则写得很直白：只要有一个节点被阻断，总体即取被阻断；无阻断但有受限时取受限。这两个规则本篇在附表 C 与 D 各登记一行。

支持侧：企业开发者的工单首次响应口径是工作日 **24** 小时以内，官方同时把"工作日"限定为客户所在国家周一至周五、不含节假日；严重度分 **3** 级（1 Critical、2 Urgent、3 Standard），另有一个解决后仍有未决请求时改判的 `Mitigated` 状态。本篇把 `Mitigated` 记为**第四种值而不是第四级**，因为它在官方文本里出现在"改判"句里，不在严重度表里。

## 12.3 载入测试：官方只给一种，本篇只登记名称

`whatsapp_support_load_testing` 页对本篇而言是一页"边界声明"。官方在该页写明当前云端接口只提供**1** 种载入测试，名称是 `Outbound Load Test`，并说明它适用于 **2** 类场景（伙伴跟踪请求与响应的时间序列、业务监控状态回调的到达情况）。该页同时写明 Meta 提供的测试号码按普通号码处理请求，但**不会把消息投递出去**，且申请需经 Direct Support 渠道提出；向测试号码发入站流量会触发熔断并断开该号码。

按本篇第 0 节设定的红线，**这一页本篇只登记"有这么一种测试、它叫什么、官方说适用于两类场景、测试号码不投递"这四件事**，不重述该页给出的任何准备清单、工具、参数与执行步骤。理由很简单：那些内容的作用是把发送压力做大，而本篇的目标是把官方口径读清楚。

## 12.4 实验位与输入指示器：两处"平台会自己改行为"

实验页官方口径是平台会不定期运行实验以评估消息对用户体验与互动的影响，且**在跑的实验没有固定结束日期**。该页唯一一条对本篇有口径价值的规则是营销类实验：一小部分用户在任何业务发来营销模板时都收不到，除非满足 **2** 个条件之一——存在客服窗口，或存在开放的免费入口窗口。官方还写明这类消息**不发送也不计费**，改为返回错误，且重发得到同样的错误。本篇认为这是整个自动化接入面里少数几条"平台侧原因导致失败但不算你的配额"的明确表述，因此进附表 C 的处置档而不进限额档。

输入指示器页给出的时长是 **25** 秒：不回复则自动消失，官方同时建议只在确实需要时才显示。这条本身是能力口径，但它在本篇的位置是**回调消费者侧**：这一页的前提是你已经收到 `messages` 回调并能取到消息 ID，属于第 10 节的下游。

# 13 Direct Send：官方把"自动化"写进接口名的一次

## 13.1 类别限定与主动监控

Direct Send 的定性句写在指引页开头：`Direct Send is limited to utility and authentication messages`，同一句接着说 Meta 会**主动监控**消息的类别归类，并适用与模板相同的那套完整性规则。这一句是本篇认为全文最该被读准的一句，因为它的结构是"能力 + 类别上限 + 监控承诺"三件事绑在一句里：**去掉类别上限读，就把一个受限能力读成了通用发送能力**。

支持的类型侧，官方在"支持的消息类型"页把可用形态写成文本加若干交互类型，并要求在请求体里带 `category`；交互消息的头部类型是 **4** 种（文本、图片、视频、文档），回复按钮类消息最多 **3** 个预设回复。语言口径则写成全部云端接口语言均支持。这一组数本篇进附表 B 的一档。

## 13.2 会话定价与模板暂停

FAQ 页把 Direct Send 的实用类消息接进既有的会话定价逻辑：若已存在开放的实用类会话，则不开新会话、在既有会话内投递；若不存在，则开一个**自第一条消息起持续 24 小时**的实用类会话。这两支本篇进附表 C 的窗口档。

模板暂停侧的口径链条是：命中因质量低而被暂停的模板时消息失败，并通过错误回调返回错误码 `132015`；官方另写明模板状态转为暂停时经邮件与回调两种渠道通知，回调里同时带模板名与语言两个字段。该页还留了一句对第 13.4 节很重要的定位方法：被标记的模板可以用 `source` 与纠正类别两个条件一起查出来。

官方在这一节同时给了**收到误判警告后的复核入口名称**（在业务支持主页的 Direct Send 模板更新页签里）与一个条件：如果产品内还没有这个入口，就走 Direct Support 工单。本篇按红线只登记**入口的名称与条件分支的存在**，不登记提交内容与话术。

## 13.3 TTL、命名模板与生成模板的两种来源

有效期这一页是系列第二篇里"只有 Direct Send 才能设 TTL"这条口径的落点：官方写成在 `category` 字段之下增加 `ttl_seconds` 字段。可设区间与默认值本篇已全部登记在附表 B 的三档：实用类与认证类的下限 **30** 秒，实用类上限 **43200** 秒、默认 **30** 天，认证类上限 **900** 秒、默认 **600** 秒。

命名模板侧的口径是：默认情况下 Direct Send 自动把每条消息匹配到一个生成模板；如果提供 `template_name`，Direct Send 会以**那个确切名字**创建或复用一个模板，所有使用该名的消息都归到它。相关校验里最硬的一条是模板名长度不得超过 **512** 个字符，以及与已有非 Direct Send 模板同名时返回错误码 `132021`。"模板创建失败会被重试"的上限是 **3** 次，重试耗尽后经错误回调返回 `131000`。

生成模板页官方明确 Direct Send 创建**两**类模板：基于消息内容的（名字以 `auto_generated` 起始）与基于 API 提供的 `template_name` 的（名字就是所给名字）。这一条与系列第二篇"模板要先审核"的口径合起来读才有意义：**在同一账号里，模板可能来自审核流，也可能来自生成流，两者的可查条件不同**。

## 13.4 洞察与分析：ID 与名称的两套入口

这一页值得写进本篇，不是因为它给了指标，而是因为它把**两套标识体系**摆在一起：分析接口按 `template_id` 直接取数，而 WhatsApp Manager 按**名字**列出模板。官方为此专门写了一句：如果你只有从消息状态回调里拿到的 `template_id`，要先用消息模板接口换出名字。本篇把这读成第 10.2 节那条口径的另一处实例——**状态回调既是事实来源，也是标识符的唯一携带者**；凡是不能在回调里取到 ID 的实现，都无法把"发出的那条"与"归到的那个模板"对上。

## 13.5 条款侧的定性：从"不允许"到"会起诉"

把接口面读完，本篇回到条款侧的三句定性表述，这也是整篇的落点。

第一句是产品定位。帮助中心的负责任使用页把这套产品的用途写得很窄：`WhatsApp was built as a simple, secure and reliable way to message other people`，并且明确列出的若干用法**可能导致账号被封**：`Using WhatsApp in any of the ways listed below may cause your account to be banned`。

第二句是对"群发／自动化"本身的定性。官方在专门一页里写明，这些产品**并非为批量或自动化消息而设计**，且这两类用法**一直是**对条款的违反；该页还表明这项挑战需要整体性 Approach，平台承诺用包括法律手段在内的资源去防止违反条款的滥用，例如自动化或批量消息、以及非个人用途。本篇把"both of which have always been a violation"这一时态读作关键：官方不是在描述一项新规则，而是在把既有规则追认到全部历史。

第三句是执法升级的时间点。官方写明自 **2019-12-07** 起，对被认定参与或协助他人实施违反条款滥用（例如自动化或批量消息、非个人用途）的对象采取法律行动，**即使该认定仅基于站外信息**。这一句决定了本篇对所有"接口能力"的读法：**能调用不等于被允许**，而认定所需的信息来源被官方明确放宽到接口之外。

配套的一句在"违规检测"页：当用户拉黑或举报某业务时，WhatsApp 会收到该业务发给该用户的最近 **5** 条消息、被举报的群组或用户 ID、消息发送时间与消息类型。本篇把这一句放在最后，是因为它是整个自动化接入面里**唯一一处官方明确说明"平台会回读内容"**的位置——它与此前各节谈到的回调、限额、健康状态无关，但在条款定性这一层，它就是证据口径。

# 14 三张清单：本篇能回答什么、拒绝回答什么、读者要自己补什么

## 14.1 本篇可以回答的问题

按上述 13 节，本篇可以给出答案的问题类型有四类，且每一类都能在附表里指到具体行：**资格与角色**（三类伙伴、三种身份、若干权限与令牌类型的组合关系，见附表 E 与第 3、4、6 章）；**容器与归属**（号码、消息账号、业务组合三者之间谁拥有谁、共享与转移的差别，见第 5、7 章）；**事件与载荷**（哪个字段在哪些页有哪些取值、状态回调带不带内容，见第 8、9、10、11 章与附表 D）；**容量与处置**（档位、窗口、时长、错误码、判定门槛，见第 12、13 章与附表 B、C）。

## 14.2 本篇拒绝回答的问题

第二类清单在本篇开头就设了边界，这里只重申结论、不重复理由：**任何批量发送脚本、发送队列与并发调参；账号池与多号轮换；限速或频控规避；风控与反垃圾检测绕过；签名号的抓取或伪造；非官方客户端；申诉话术与模板代写以规避类别判定。**具体到本篇写作过程中实际遇到、但按红线不展开的官方内容有三处：载入测试页的工具与参数（第 12.3 节）、嵌入式注册的逐步操作（第 5、6 章）、以及复核提交的填写内容（第 6 章）。这三处的**步骤名称与数量**已进正文与附表，**怎么做**一律不写。

## 14.3 需要读者自己补的官方留白

第三张清单等于附表 F 本身：本篇登记 **19** 项口径冲突或官方未写明，其中 **6** 项的一侧是"官方未写明"。这三类留白本篇认为最影响判断：**撤销与失效的时间**（多数角色与令牌只写"可以撤销"，不写生效时延）、**跨页取值域的差异**（第 11.3 节三类）、**旧文档的存在与现行效力之间的关系**（第 7 章两页自我作废）。附表 F 里还有一条必须留在此处而不是埋在表里：本次抓取有 **5** 页只返回外壳、正文未取回，本篇**未引用**它们的内容，这 5 页在第 2、6、12 章的登记位仍保留，用来标明"官方确有此页"。

# 15 结论

## 15.1 接入面是一条"资格—凭证—容器—事件"的四段链

把 15 节读完，本篇对轴③的结论是：自动化接入面不是一组接口，而是一条**必须逐段打通的链**。资格段决定谁能建应用、谁的名字能出现在客户账单上（第 6 章）；凭证段决定令牌由谁签发、撤销谁说了算、撤销是不是连带换令牌（第 3、4 章）；容器段决定号码、消息账号、业务组合三者之间所有权与共享的分界（第 5、7 章）；事件段决定这一切运行时的可见性——哪些字段可订阅、哪些能改地址、哪些必须逐个订阅（第 8、9、10、11 章）。链条的价值在于**任何一段缺失，下游的"能力"都只是文本上的能力**；本篇的附表 A 到 E 就是按这四段分组登记的。

## 15.2 三处系统性口径落差

第一处是**数量类口径的"两套数"**：限额与吞吐是两套数（第 12.1 节）、可订阅字段与可覆盖字段是两套数（第 8.2 节）、能被查询的节点与被返回的实体是两套数（第 12.2 节）、状态取值与状态触发是两套数（第 10.2 节）。第二处是**"存在"与"现行"不等价**：官方选择保留旧页面并加作废声明，而不是删除，这使"文档里写着"不再等于"规则还在"（第 7 章、附表 F）。第三处是**能力与许可分家**：接口面把某类消息做成一次调用，条款面把批量与自动化写成一贯的违规，两面对同一件事的评价相反且互不引用（第 13.1、13.5 节）。

## 15.3 与系列前三篇的关系

本篇是"群发／群控"五轴系列的第三篇，与前两篇共用**同一套官方文档口径与同一套门禁**，但分工不重叠：第①轴谈平台**如何判定与处置**，第②轴谈**消息本身**的类型、模板、质量与限额档位，本篇谈**接入面**——也就是"谁有资格、拿什么凭证、在什么容器里、能看见哪些事件"。三者共同点是都不含任何搭建与规避内容。第④轴（群组、社区、频道与列表的治理开关）与第⑤轴（号码、账号形态、受限账号与封禁申诉）沿用本篇已登记的页面登记与引句回核流程。

## 15.4 一句话收口

本篇能给的最有用的一句话是：**官方文档描述的是"被允许的自动化长什么样"，而不是"怎样把批量做成功"**。第 13.5 节那三句定性——产品不为批量与自动化而设计、这类用法一直是违规、认定可以只依据站外信息——与前面 12 节里所有"能力"的表述处在同一份官方文本中。任何只引用其中一半的读法，都是对这套口径的误读。

# 附表 A 官方页面登记清单与本篇引用位置（112 行）

登记口径与系列前三篇一致：**凡本篇抓取的页面，一律登记**；本表收的是轴③（自动化接入面）取到的官方页面，其余两根轴的页面不在本表内。全表 **111** 个登记位：**正文已取回 106 页、正文未取回 5 页**；站点分布为 Meta 商业帮助中心 3、Meta 开发者文档 104、法律与政策页 4。未取回的页面是本次抓取中只返回单页应用外壳、正文由脚本另行延迟加载的文档位，**其内容全篇未被引用**，本篇只在附表 F 里登记"该页正文未取回"这一条事实。

| 站点 | 页面标识 | 标题 | 本篇引用位置 | 正文取回状态 |
|---|---|---|---|---|
| Meta 开发者文档 | `whatsapp_access_tokens_md` | Access Tokens Guide | 正文 3.1—3.3、4.1—4.3、11.2、附表 B（4 处）、附表 C（6 处）、附表 D（10 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_account_model_evolution_md` | Updates to WhatsApp Business accounts | 正文 1.2、2.3、附表 B（6 处）、附表 C（4 处）、附表 D（5 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_account_model_evolution_messaging_md` | Managing messaging accounts | 正文 1.2、2.3、5.1 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_account_model_evolution_onboarding_md` | Onboarding changes | 正文 1.2、2.3、7.1 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_direct_send_api_reference_md` | Direct Send API reference | 正文 13.1、11.2、附表 B（9 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_direct_send_business_named_templates_md` | Business-named templates | 正文 13.3 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_direct_send_configure_message_ttl_md` | Configure message time-to-live (TTL) | 正文 13.2、附表 B（5 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_direct_send_faq_md` | Direct Send FAQ | 正文 13.5、附表 B（5 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_direct_send_get_started_md` | （正文未取回） | 全篇不引用（正文未取回） | 正文未取回 |
| Meta 开发者文档 | `whatsapp_direct_send_integrity_and_content_guidelines_md` | Integrity and content guidelines | 正文 13.5、9.3、附表 B（3 处）、附表 C（10 处）、附表 D（17 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_direct_send_media_headers_md` | Media message headers | 正文 13.3 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_direct_send_send_sample_payloads_md` | （正文未取回） | 全篇不引用（正文未取回） | 正文未取回 |
| Meta 开发者文档 | `whatsapp_direct_send_send_utility_and_authentication_messages_md` | Send utility and authentication messages | 正文 13.2 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_direct_send_supported_features_and_limits_md` | Supported features and limits | 正文 13.1 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_direct_send_supported_message_types_md` | Supported message types | 正文 13.2、附表 B（4 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_direct_send_view_generated_templates_md` | View templates generated by Direct Send | 正文 13.3、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_direct_send_view_insights_and_analytics_md` | View insights and analytics | 正文 13.4 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_in_app_signup_md` | In-App Signup | 正文 3.4、7.1、附表 B（13 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_messaging_limits_md` | Messaging Limits | 正文 12.1、附表 B（5 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_official_business_accounts_md` | Official Business Accounts | 正文 5.2、9.2、附表 B（1 处）、附表 C（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_overview_md` | （正文未取回） | 全篇不引用（正文未取回） | 正文未取回 |
| Meta 开发者文档 | `whatsapp_permissions_md` | WhatsApp permissions | 正文 3.3、6.4、11.2、附表 B（1 处）、附表 C（3 处）、附表 D（5 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_app_review_md` | WhatsApp app review | 正文 6.4、11.1、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_get_started_for_solution_partners_md` | Get started as a Solution Partner | 正文 2.2、7.6、附表 B（5 处）、附表 C（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_get_started_for_tech_providers_md` | Become a Tech Provider | 正文 2.2、6.1、7.6 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_manage_accounts_md` | Manage Messaging accounts | 正文 5.1、6.3 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_manage_phone_numbers_md` | Client phone numbers | 正文 5.3 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_manage_system_users_md` | Manage System Users | 正文 4.4 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_manage_webhooks_md` | Managing webhooks | 正文 8.5 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_measurement_partners_md` | Measurement Partners | 正文 6.7 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_migrate_existing_whatsapp_number_to_a_business_account_md` | Migrate an existing WhatsApp number to a business account | 正文 7.1、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_multi_partner_solution_embedded_creation_md` | Multi-Partner Solution — Embedded creation | 正文 6.6 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_multi_partner_solutions_md` | Multi-Partner Solutions | 正文 6.6、9.5、11.1—11.3、附表 B（2 处）、附表 C（4 处）、附表 D（9 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_multi_solution_conversations_md` | How to use Multi-Solution Conversations (MSC) | 正文 5.4 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_obo_model_deprecation_md` | On-Behalf-Of account ownership model deprecation | 正文 7.6、附表 B（3 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_overview_md` | Partners | 正文 2.1、6.1—6.3、11.1、附表 B（3 处）、附表 D（8 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_partner_initiated_waba_creation_md` | Partner-initiated Messaging account creation | 正文 5.5、6.3、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_partner_led_business_verification_md` | Partner-led Business Verification | 正文 6.5、9.2、附表 B（3 处）、附表 D（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_pixel_tracking_md` | Tracking with the Meta Pixel | 正文 6.7 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_registering_phone_numbers_md` | Registering business phone numbers | 正文 5.2、11.2、附表 B（1 处）、附表 D（6 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_share_and_revoke_credit_lines_md` | Managing credit lines | 正文 5.4、6.3、附表 C（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_support_md` | Solution providers support - WhatsApp | 正文 7.6、12.2 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_support_adding_waba_to_mps_md` | Adding an account to a Multi-Partner Solution | 正文 5.5 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_support_business_customer_support_md` | Support for onboarded clients | 正文 7.6 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_support_migrating_customers_off_solutions_via_embedded_signup_md` | Migrating customers off of a Multi-Partner Solution using Embedded Signup | 正文 7.5 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_support_migrating_customers_off_solutions_via_meta_business_suite_md` | Migrating customers off a Multi-Partner Solution using Meta Business Suite | 正文 7.5 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_support_migrating_phone_numbers_among_solution_partners_programmatically_md` | Migrate a phone number programmatically - WhatsApp | 正文 7.1 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_support_migrating_phone_numbers_among_solution_partners_via_embedded_signup_md` | Migrate a phone number via Embedded Signup - WhatsApp | 正文 7.2 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_support_migrating_wabas_among_solution_partners_via_embedded_signup_md` | Migrating a Messaging account from one Solution Partner to another via Embedded Signup | 正文 7.3 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_support_migrating_wabas_among_solutions_via_embedded_signup_md` | Migrate customers via Embedded Signup - WhatsApp | 正文 7.3 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_support_migrating_wabas_among_solutions_via_meta_business_suite_md` | Migrate customers via Meta Business Suite - WhatsApp | 正文 7.4 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_solution_providers_upgrade_to_tech_partner_md` | Upgrading to a Tech Partner | 正文 7.6、附表 B（3 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_support_md` | WhatsApp support | 正文 12.2、7.6、附表 B（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_support_api_status_page_md` | WhatsApp for Business Platform Status | 正文 12.2、附表 B（6 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_support_experiments_md` | Experiments | 正文 12.4、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_support_health_status_md` | Messaging and Calling Health Status | 正文 12.2、附表 B（2 处）、附表 C（3 处）、附表 D（6 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_support_load_testing_md` | Load Testing on Cloud API | 正文 12.3、附表 B（2 处）、附表 C（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_throughput_md` | Throughput | 正文 12.1、11.2、附表 B（16 处）、附表 C（5 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_typing_indicators_md` | Typing indicators | 正文 12.4、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_upcoming_messaging_limits_changes_md` | Upcoming changes to messaging limits | 正文 12.1、附表 B（3 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_create_webhook_endpoint_md` | Create a webhook endpoint | 正文 8.1、附表 B（4 处）、附表 C（6 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_message_echoes_md` | （正文未取回） | 全篇不引用（正文未取回） | 正文未取回 |
| Meta 开发者文档 | `whatsapp_webhooks_override_md` | Webhook overrides | 正文 8.2、附表 B（3 处）、附表 C（5 处）、附表 D（21 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_overview_md` | WhatsApp webhooks | 正文 8.1—8.4、9.1、11.1、附表 B（7 处）、附表 C（2 处）、附表 D（20 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_account_alerts_md` | account_alerts webhook reference | 正文 9.2、11.1—11.3、附表 C（1 处）、附表 D（24 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_account_review_update_md` | account_review_update webhook reference | 正文 9.2、附表 D（4 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_account_update_md` | account_update webhook reference | 正文 9.2、11.2、11.3、附表 B（1 处）、附表 D（48 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_automatic_events_md` | automatic_events webhook reference | 正文 9.1、附表 D（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_business_capability_update_md` | business_capability_update webhook reference | 正文 9.4、附表 B（1 处）、附表 D（10 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_history_md` | history webhook reference | 正文 10.3、附表 B（2 处）、附表 D（9 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_message_template_components_update_md` | message_template_components_update webhook reference | 正文 9.3 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_message_template_quality_update_md` | message_template_quality_update webhook reference | 正文 9.3、附表 D（8 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_message_template_status_update_md` | message_template_status_update webhook reference | 正文 9.3、附表 D（22 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_md` | messages webhook reference | 正文 10.1、附表 B（6 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_audio_md` | Audio messages webhook reference | 正文 10.1、附表 D（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_button_md` | Button messages webhook reference | 正文 10.2、附表 B（1 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_contacts_md` | Contacts messages webhook reference | 正文 10.1、附表 D（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_document_md` | Document messages webhook reference | 正文 10.1、附表 D（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_edit_md` | Edit messages webhook reference | 正文 10.2 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_errors_md` | Errors messages webhooks reference | 正文 10.2 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_group_md` | Group messages webhook reference | 正文 10.2 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_image_md` | Image messages webhook reference | 正文 10.1、附表 D（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_interactive_md` | Interactive messages webhook reference | 正文 10.2 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_location_md` | Location messages webhook reference | 正文 10.1、附表 D（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_order_md` | Order messages webhook reference | 正文 10.2 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_reaction_md` | Reaction messages webhook reference | 正文 10.1 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_revoke_md` | Revoke messages webhook reference | 正文 10.2 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_status_md` | Status messages webhook reference | 正文 10.2、附表 B（11 处）、附表 D（25 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_sticker_md` | Sticker messages webhook reference | 正文 10.1、附表 D（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_system_md` | System messages webhook reference | 正文 10.2 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_text_md` | Text messages webhook reference | 正文 10.1、附表 B（2 处）、附表 D（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_unsupported_md` | Unsupported messages webhook reference | 正文 10.3、附表 B（1 处）、附表 D（19 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_messages_video_md` | Video messages webhook reference | 正文 10.1、附表 D（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_partner_solutions_md` | partner_solutions webhook reference | 正文 9.5、11.2、附表 B（1 处）、附表 D（8 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_payment_configuration_update_md` | payment_configuration_update webhook reference | 正文 9.6、附表 D（7 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_phone_number_name_update_md` | phone_number_name_update webhook reference | 正文 9.4、附表 D（7 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_phone_number_quality_update_md` | phone_number_quality_update webhook reference | 正文 9.4、附表 D（22 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_pricing_md` | （正文未取回） | 全篇不引用（正文未取回） | 正文未取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_security_md` | Security webhook reference | 正文 8.4、11.1、附表 D（3 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_smb_app_state_sync_md` | smb_app_state_sync webhook reference | 正文 9.6、附表 D（2 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_smb_message_echoes_md` | smb_message_echoes webhook reference | 正文 10.3 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_template_category_update_md` | template_category_update webhook reference | 正文 9.3 | 正文已取回 |
| Meta 开发者文档 | `whatsapp_webhooks_reference_user_preferences_md` | user_preferences webhook reference | 正文 9.6、附表 D（4 处） | 正文已取回 |
| Meta 开发者文档 | `whatsapp_whatsapp_business_accounts_md` | WhatsApp accounts and Messaging accounts | 正文 5.1 | 正文已取回 |
| 法律与政策页 | `legal_messaging_guidelines` | WhatsApp Messaging Guidelines | 正文 2.4、13.1 | 正文已取回 |
| 法律与政策页 | `legal_business_policy` | WhatsApp Business Messaging Policy | 正文 2.4、13.1 | 正文已取回 |
| 法律与政策页 | `legal_bp_terms` | WhatsApp Terms for WhatsApp Business Platform | 正文 2.4、13.1 | 正文已取回 |
| 法律与政策页 | `legal_tos` | WhatsApp Terms of Service | 正文 2.4、13.1 | 正文已取回 |
| Meta 商业帮助中心 | `help_5957850900902049` | Unauthorized use of automated or bulk messaging on WhatsApp | 正文 2.4、13.2、附表 B（1 处） | 正文已取回 |
| Meta 商业帮助中心 | `help_1805617343145907` | About detecting violations | 正文 2.4、13.2、附表 B（1 处） | 正文已取回 |
| Meta 商业帮助中心 | `help_361005896189245` | How to use WhatsApp responsibly | 正文 2.4、13.3 | 正文已取回 |
| **合计** | **111** 个登记位 | 正文已取回 **106** 页／正文未取回 **5** 页 | Meta 商业帮助中心 3／Meta 开发者文档 104／法律与政策页 4 | 未取回页全篇不引用 |

#ROWS=111 CITED=106 UNCITED=5

# 附表 B 数字归档五档（156 行）

归档规则见第 13.1 节：**同一数字出现在两个体系里开两行**，官方写成区间或多值的按整段一行登记，版本与日期行只进第四档，官方只给示例值的登记为「示例值」、不当作规则，本篇自己数出来的组数与个数登记为「本篇统计」。五档合计 **155** 行（一 **37**／二 **15**／三 **36**／四 **17**／五 **50**），去重后的数字与档位词条目 **89** 个，计数单位 **64** 种。

| 档 | 数字 | 计数单位 | 数的是什么 | 性质 | 出处页 |
|---|---|---|---|---|---|

| 一、限额与规模档位 | **3** | 个权限 | 生成系统用户令牌时须勾选的 Graph API 权限数 | 硬数量下限 | whatsapp_access_tokens_md |
| 一、限额与规模档位 | **200** | 个字符 | 消息账号与号码级备用回调 URL 的长度上限（两处同值） | 硬数量上限 | whatsapp_webhooks_override_md |
| 一、限额与规模档位 | **3 MB** | 字节 | 单个 Webhook 载荷的大小上限 | 硬数量上限 | whatsapp_webhooks_overview_md |
| 一、限额与规模档位 | **1000** | 条更新 | 一次 POST 请求聚合投递的更新条数上限 | 硬数量上限 | whatsapp_webhooks_create_webhook_endpoint_md |
| 一、限额与规模档位 | **200** | 个客户 | 多伙伴方案中技术提供商一周内可新入驻的客户数 | 滚动窗口内上限 | whatsapp_solution_providers_multi_partner_solutions_md |
| 一、限额与规模档位 | **128** | 个字符 | 消息 ID 的最大长度 | 硬数量上限 | whatsapp_solution_providers_get_started_for_solution_partners_md |
| 一、限额与规模档位 | **512** | 个字符 | Direct Send 模板名的最大长度 | 硬数量上限 | whatsapp_direct_send_faq_md |
| 一、限额与规模档位 | **300** | 个字符 | 应用内注册前置同意文案的长度区间上限 | 硬数量上限 | whatsapp_in_app_signup_md |
| 一、限额与规模档位 | **50** | 个字符 | 应用内注册促销码的长度区间上限 | 硬数量上限 | whatsapp_in_app_signup_md |
| 一、限额与规模档位 | **256** | 个字符 | 应用内注册业务别名的长度区间上限 | 硬数量上限 | whatsapp_in_app_signup_md |
| 一、限额与规模档位 | **10** | 条/页 | 应用内注册列举接口的分页示例值 | 示例值 | whatsapp_in_app_signup_md |
| 一、限额与规模档位 | **20** | 个字段 | 概览页登记的可订阅 Webhook 字段数（本表统计值，非官方数字） | 本篇统计 | whatsapp_webhooks_overview_md |
| 一、限额与规模档位 | **14** | 个字段 | 覆盖页支持改写回调地址的字段数（本表统计值） | 本篇统计 | whatsapp_webhooks_override_md |
| 一、限额与规模档位 | **7** | 个字段 | 覆盖页点名不支持回调覆盖的字段数（本表统计值） | 本篇统计 | whatsapp_webhooks_override_md |
| 一、限额与规模档位 | **5** | 个权限 | 权限页登记的 Graph API 权限数（本表统计值） | 本篇统计 | whatsapp_permissions_md |
| 一、限额与规模档位 | **3** | 种身份 | 令牌页划分的开发者身份数（本表统计值） | 本篇统计 | whatsapp_access_tokens_md |
| 一、限额与规模档位 | **3** | 个节点 | 健康状态页列出带 health_status 字段的节点数（本表统计值） | 本篇统计 | whatsapp_support_health_status_md |
| 一、限额与规模档位 | **5** | 个实体类型 | 健康状态响应示例里 entity_type 的取值数（本表统计值） | 本篇统计 | whatsapp_support_health_status_md |
| 一、限额与规模档位 | **4** | 个步骤 | 号码注册页把一次注册写成四个连续动作；该页另有一句称四步可重复执行，本篇按范围红线不展开 | 流程口径 | whatsapp_solution_providers_registering_phone_numbers_md |
| 一、限额与规模档位 | **5** | 个组件 | 应用请求体的顶层组成件数（本表统计值） | 本篇统计 | whatsapp_solution_providers_overview_md |
| 一、限额与规模档位 | **3** | 次提交 | 伙伴代为商业核验时，同一个客户最多可被提交的次数；三次全拒后客户须自行完成核验 | 硬数量上限 | whatsapp_solution_providers_partner_led_business_verification_md |
| 二、速率与吞吐档位 | **80** | 条/秒 | 每个已注册业务号码的云端接口默认吞吐 | 硬速率上限 | whatsapp_throughput_md |
| 二、速率与吞吐档位 | **1,000** | 条/秒 | 满足资格条件后自动升档到的吞吐 | 硬速率上限 | whatsapp_throughput_md |
| 二、速率与吞吐档位 | **20** | 条/秒 | 号码同时用于商业应用与云端接口时的固定吞吐 | 硬速率上限 | whatsapp_throughput_md |
| 二、速率与吞吐档位 | **100K** | 个唯一号码 | 升档资格：24 小时移动窗口内在客服窗口外触达的唯一用户号码数 | 条件触发项 | whatsapp_throughput_md |
| 二、速率与吞吐档位 | **3** | 倍 | 回调服务器应具备的出站消息处理容量倍率 | 容量口径 | whatsapp_throughput_md |
| 二、速率与吞吐档位 | **1** | 倍 | 回调服务器应具备的入站消息处理容量倍率 | 容量口径 | whatsapp_throughput_md |
| 二、速率与吞吐档位 | **3,000** | 条回调 | 吞吐 1,000 条每秒、预期响应率 30% 示例中的消息状态回调数 | 示例值 | whatsapp_throughput_md |
| 二、速率与吞吐档位 | **300** | 条回调 | 同例中额外需要处理的入站消息回调数 | 示例值 | whatsapp_throughput_md |
| 二、速率与吞吐档位 | **30%** | 响应率 | 上述容量示例假定的预期响应率 | 示例值 | whatsapp_throughput_md |
| 二、速率与吞吐档位 | **250** | 毫秒 | 回调服务器中位延迟标准 | 硬速率上限 | whatsapp_throughput_md |
| 二、速率与吞吐档位 | **1%** | 延迟占比 | 回调服务器超过 1 秒的延迟占比上限 | 硬速率上限 | whatsapp_throughput_md |
| 二、速率与吞吐档位 | **2,500** | 条/日均 | 升级为技术合作伙伴的消息量门槛：过去 7 天与用户往来的日均条数 | 条件触发项 | whatsapp_solution_providers_upgrade_to_tech_partner_md |
| 二、速率与吞吐档位 | **200** | 通/日均 | 同门槛的通话替代口径：过去 7 天日均通话数 | 条件触发项 | whatsapp_solution_providers_upgrade_to_tech_partner_md |
| 二、速率与吞吐档位 | **10** | 个活跃客户 | 升级为技术合作伙伴的客户门槛：近 30 天用本应用至少发过 1 条消息 | 条件触发项 | whatsapp_solution_providers_upgrade_to_tech_partner_md |
| 三、时长与窗口 | **7** | 天 | 失败回调的重投窗口（概览页措辞） | 硬时长（定值） | whatsapp_webhooks_overview_md |
| 三、时长与窗口 | **7** | 天 | 失败 POST 投递的重投窗口（端点创建页措辞） | 硬时长（定值） | whatsapp_webhooks_create_webhook_endpoint_md |
| 三、时长与窗口 | **7** | 天 | 未确认回调被丢弃前的存活时长 | 硬时长（定值） | whatsapp_webhooks_create_webhook_endpoint_md |
| 三、时长与窗口 | **7** | 天 | 失败回调重投窗口（吞吐页措辞，附带指数退避） | 硬时长（定值） | whatsapp_throughput_md |
| 三、时长与窗口 | **1** | 分钟 | 吞吐升档过程本身可能占用的最长时长 | 硬时长（定值） | whatsapp_throughput_md |
| 三、时长与窗口 | **25** | 秒 | 输入指示器在没有回复时的自动消失时长 | 硬时长（定值） | whatsapp_typing_indicators_md |
| 三、时长与窗口 | **14** | 天 | 走完嵌入式注册流程后号码必须完成注册的窗口 | 硬时长（定值） | whatsapp_solution_providers_get_started_for_solution_partners_md |
| 三、时长与窗口 | **24** | 小时 | 客户发来消息后免费收发的客服窗口时长 | 硬时长（定值） | whatsapp_solution_providers_get_started_for_solution_partners_md |
| 三、时长与窗口 | **24** | 小时 | 应用复核的平均处理时长 | 滚动窗口 | whatsapp_solution_providers_app_review_md |
| 三、时长与窗口 | **5** | 分钟 | 伙伴主导商业核验的平均处理时长 | 滚动窗口 | whatsapp_solution_providers_partner_led_business_verification_md |
| 三、时长与窗口 | **24** | 小时 | 核验结果回调超时后应开工单的判定时长 | 硬时长（定值） | whatsapp_solution_providers_partner_led_business_verification_md |
| 三、时长与窗口 | **30** | 天 | 被拒后重新提交官方商业账号申请的等待时长（同页两处同值） | 硬时长（定值） | whatsapp_official_business_accounts_md |
| 三、时长与窗口 | **30** | 天 | 云端接口消息在静置状态下的最长留存期 | 硬时长（定值） | whatsapp_solution_providers_get_started_for_solution_partners_md |
| 三、时长与窗口 | **60** | 天 | 被标记模板申请复核的窗口，逾期不再具备复核资格 | 硬时长（定值） | whatsapp_direct_send_integrity_and_content_guidelines_md |
| 三、时长与窗口 | **7** | 天 | 第一级消息限制（Messaging restriction）的持续时长 | 硬时长（定值） | whatsapp_direct_send_integrity_and_content_guidelines_md |
| 三、时长与窗口 | **30** | 天 | 延长限制（Extended restriction）的持续时长 | 硬时长（定值） | whatsapp_direct_send_integrity_and_content_guidelines_md |
| 三、时长与窗口 | **24** | 小时 | 创建后从未用于发送的 Direct Send 模板被删除前的时长 | 硬时长（定值） | whatsapp_direct_send_faq_md |
| 三、时长与窗口 | **24** | 小时 | 类别误用限速按小时滚动开窗的窗口长度 | 滚动窗口 | whatsapp_direct_send_api_reference_md |
| 三、时长与窗口 | **3** | 次重试 | 模板创建失败时消息被重试的次数上限 | 硬数量上限 | whatsapp_direct_send_faq_md |
| 三、时长与窗口 | **30** | 秒 | Direct Send 实用类与认证类有效期的可设下限（两处同值） | 硬时长（区间或多值） | whatsapp_direct_send_configure_message_ttl_md |
| 三、时长与窗口 | **43200** | 秒 | 实用类消息有效期的可设上限 | 硬时长（定值） | whatsapp_direct_send_configure_message_ttl_md |
| 三、时长与窗口 | **900** | 秒 | 认证类消息有效期的可设上限 | 硬时长（定值） | whatsapp_direct_send_configure_message_ttl_md |
| 三、时长与窗口 | **600** | 秒 | 认证类消息的默认有效期 | 默认值 | whatsapp_direct_send_configure_message_ttl_md |
| 三、时长与窗口 | **30** | 天 | 实用类消息的默认有效期 | 默认值 | whatsapp_direct_send_configure_message_ttl_md |
| 三、时长与窗口 | **2—4** | 周 | 模板迁移到新消息账号后恢复质量分与档位所需的爬升期 | 区间或多值 | whatsapp_account_model_evolution_md |
| 三、时长与窗口 | **10** | 个工作日 | 状态页上月可用性数据补齐的时限 | 硬时长（定值） | whatsapp_support_api_status_page_md |
| 三、时长与窗口 | **10** | 分钟 | 判定云端接口不可用所需的连续无法收发时长 | 硬时长（定值） | whatsapp_support_api_status_page_md |
| 三、时长与窗口 | **90** | 天 | 伙伴发起的消息账号邀请在无人接受时被自动取消前的存活时长 | 硬时长（定值） | whatsapp_solution_providers_partner_initiated_waba_creation_md |
| 三、时长与窗口 | **3** | 分钟 | 号码解绑后重新变为可用状态的最长等待（原文措辞为"可能需要"） | 区间或多值 | whatsapp_solution_providers_migrate_existing_whatsapp_number_to_a_business_account_md |
| 四、版本与日期节点 | **v22.0** | API 版本 | 应用内注册接口可用的起始版本 | 版本节点 | whatsapp_in_app_signup_md |
| 四、版本与日期节点 | **v23.0** | API 版本 | 会话对象取值口径的分界版本之一 | 版本节点 | whatsapp_webhooks_reference_messages_status_md |
| 四、版本与日期节点 | **v24.0** | API 版本 | 会话对象整体省略的分界版本（开放免费入口窗口内发出的除外） | 版本节点 | whatsapp_webhooks_reference_messages_status_md |
| 四、版本与日期节点 | **v27.0** | API 版本 | 本批页面示例请求统一使用的版本号（出现频次见正文） | 版本节点 | whatsapp_access_tokens_md |
| 四、版本与日期节点 | **2019-12-07** | 日期节点 | WhatsApp 对仅凭站外证据判定的滥用也开始采取法律行动的起点 | 生效节点 | help_5957850900902049 |
| 四、版本与日期节点 | **2026-09-23** | 日期节点 | 新账号模型转为通用可用的日期 | 生效节点 | whatsapp_account_model_evolution_md |
| 四、版本与日期节点 | **2026 年 10 月中** | 日期节点 | 新账号模型覆盖到全部企业的目标时点 | 生效节点 | whatsapp_account_model_evolution_md |
| 四、版本与日期节点 | **2027 年上半年** | 半年节点 | 迁移第二阶段：新 Graph API 版本 | 生效节点 | whatsapp_account_model_evolution_md |
| 四、版本与日期节点 | **2028 年上半年** | 半年节点 | 迁移第三阶段：强制切换 | 生效节点 | whatsapp_account_model_evolution_md |
| 四、版本与日期节点 | **2026-10-01** | 日期节点 | 计费子类型口径由窗口内实用改为号码免费额度之内的起点 | 生效节点 | whatsapp_webhooks_reference_messages_status_md |
| 四、版本与日期节点 | **2026-09-30** | 日期节点 | 旧的窗口内免费口径的截止日期 | 生效节点 | whatsapp_webhooks_reference_messages_status_md |
| 四、版本与日期节点 | **每月第一个周二** | 发布节点 | 云端接口例行更新的发布时间 | 滚动窗口 | whatsapp_solution_providers_get_started_for_solution_partners_md |
| 四、版本与日期节点 | **2026-05-01** | 日期节点 | 新账号模型文档首次发布日期 | 版本节点 | whatsapp_account_model_evolution_md |
| 四、版本与日期节点 | **2025-03-24** | 日期节点 | OBO 弃用时间线第一档：伙伴发起的消息账号创建开放给全部解决方案合作伙伴 | 生效节点 | whatsapp_solution_providers_obo_model_deprecation_md |
| 四、版本与日期节点 | **2025-09-29** | 日期节点 | OBO 弃用时间线第二档：以 OBO 模式新客户入驻的最后一天 | 截止节点 | whatsapp_solution_providers_obo_model_deprecation_md |
| 四、版本与日期节点 | **2025-10-01** | 日期节点 | OBO 弃用时间线第三档：把 OBO 模式号码所有权转给客户的最后一天 | 截止节点 | whatsapp_solution_providers_obo_model_deprecation_md |
| 五、编号与计数口径 | **200** | 错误码 | 令牌所标识用户无权访问资源、或缺少高级访问权限时接口返回的错误码；与 HTTP 状态码 200 无关 | 标识符 | whatsapp_access_tokens_md |
| 五、编号与计数口径 | **200** | HTTP 状态 | 回调端点验证与投递确认要求的响应状态 | 标识符 | whatsapp_webhooks_create_webhook_endpoint_md |
| 五、编号与计数口径 | **130429** | 错误码 | 超出当前吞吐档位时接口持续返回的码 | 标识符 | whatsapp_throughput_md |
| 五、编号与计数口径 | **131057** | 错误码 | 号码正处于升档不可用窗口内仍被用于请求时返回的码 | 标识符 | whatsapp_throughput_md |
| 五、编号与计数口径 | **131000** | 错误码 | Direct Send 模板创建重试耗尽后经错误回调返回的码 | 标识符 | whatsapp_direct_send_faq_md |
| 五、编号与计数口径 | **132015** | 错误码 | 发送命中因质量低而被暂停的模板时回调里的码 | 标识符 | whatsapp_direct_send_api_reference_md |
| 五、编号与计数口径 | **132021** | 错误码 | Direct Send 生成的模板名与已有非 Direct Send 模板冲突 | 标识符 | whatsapp_direct_send_api_reference_md |
| 五、编号与计数口径 | **131062** | 错误码 | 认证消息改用商业侧用户标识收件时被拒 | 标识符 | whatsapp_direct_send_api_reference_md |
| 五、编号与计数口径 | **131064** | 错误码 | 类别误用限速窗口内实用类发送被拒 | 标识符 | whatsapp_direct_send_api_reference_md |
| 五、编号与计数口径 | **139200** | 错误码 | 限制期内尝试发送时云端接口同步返回的码 | 标识符 | whatsapp_direct_send_api_reference_md |
| 五、编号与计数口径 | **2388330** | 错误码 | 尝试删除 Direct Send 生成模板时被拒 | 标识符 | whatsapp_direct_send_api_reference_md |
| 五、编号与计数口径 | **2388318** | 错误码 | 尝试更新 Direct Send 生成模板时被拒 | 标识符 | whatsapp_direct_send_api_reference_md |
| 五、编号与计数口径 | **100** | 错误码 | 请求含不支持或拼错的参数，或在不支持 Direct Send 的消息上给了有效期值 | 标识符 | whatsapp_direct_send_api_reference_md |
| 五、编号与计数口径 | **2494164** | 错误码 | 应用内注册：指定的注册实体不存在 | 标识符 | whatsapp_in_app_signup_md |
| 五、编号与计数口径 | **2494165** | 错误码 | 应用内注册：该消息账号未启用本接口 | 标识符 | whatsapp_in_app_signup_md |
| 五、编号与计数口径 | **2494166** | 错误码 | 应用内注册：确认消息里出现未知占位符 | 标识符 | whatsapp_in_app_signup_md |
| 五、编号与计数口径 | **2494167** | 错误码 | 应用内注册：用了促销码占位符却没给值 | 标识符 | whatsapp_in_app_signup_md |
| 五、编号与计数口径 | **2494168** | 错误码 | 应用内注册：服务条款未被接受 | 标识符 | whatsapp_in_app_signup_md |
| 五、编号与计数口径 | **2494176** | 错误码 | 应用内注册：已接受过条款却仍带条款对象 | 标识符 | whatsapp_in_app_signup_md |
| 五、编号与计数口径 | **2494177** | 错误码 | 应用内注册：条款链接不是被批准的链接 | 标识符 | whatsapp_in_app_signup_md |
| 五、编号与计数口径 | **403** | HTTP 状态 | 缺少消息管理权限时应用内注册接口的响应状态 | 标识符 | whatsapp_in_app_signup_md |
| 五、编号与计数口径 | **100** | 百分比 | 状态页可用性公式的分子常数 | 容量口径 | whatsapp_support_api_status_page_md |
| 五、编号与计数口径 | **P90/P99** | 百分位 | 状态页对外披露的延迟分位数口径 | 容量口径 | whatsapp_support_api_status_page_md |
| 五、编号与计数口径 | **250／2000／10000／100000／-1** | 档位取值 | 能力升级回调里每日唯一会话数上限字段的取值集 | 分档晋级项 | whatsapp_webhooks_reference_business_capability_update_md |
| 五、编号与计数口径 | **1—16** | 个事件取值 | 消息账号事件回调字段取值个数（明细见附表 D） | 本篇统计 | whatsapp_webhooks_reference_account_update_md |
| 一、限额与规模档位 | **250** | 个唯一号码 | 新建业务资产组合的起始消息限额；官方同句即列出可升到的档位 | 分档晋级项 | whatsapp_messaging_limits_md |
| 一、限额与规模档位 | **4** | 个更高档位 | 限额页在起始值之上列出的档位条数（本表统计值） | 本篇统计 | whatsapp_messaging_limits_md |
| 一、限额与规模档位 | **2,000／10,000／100,000／Unlimited** | 档位取值 | 限额页起始值之上的四档；前两档分别走扩展路径与自动扩档 | 分档晋级项 | whatsapp_messaging_limits_md |
| 一、限额与规模档位 | **2,000** | 条已送达模板消息 | 升到 2,000 档的自主路径之一：滚动窗口内向唯一号码送达且落在客服窗口之外 | 分档晋级项 | whatsapp_messaging_limits_md |
| 一、限额与规模档位 | **9** | 个字段 | 覆盖页 14 个可覆盖字段里不出现在概览页 20 行表中的个数（本表统计值） | 本篇统计 | whatsapp_webhooks_overview_md |
| 一、限额与规模档位 | **3** | 处错误落位 | 官方原句把 messages 回调的错误分成三处（本表另按三处分别登记归属） | 流程口径 | whatsapp_webhooks_reference_messages_md |
| 一、限额与规模档位 | **5** | 项被监控服务 | 状态页"这些服务是"清单的条目数（本表统计值） | 本篇统计 | whatsapp_support_api_status_page_md |
| 一、限额与规模档位 | **4** | 项状态页功能 | 状态页"提供以下功能"清单去重后的条数；该句连同四条列表在同页逐字出现两次 | 本篇统计 | whatsapp_support_api_status_page_md |
| 一、限额与规模档位 | **3** | 级工单严重度 | 支持页严重度表列出的等级数；Mitigated 写在改判句里、不在表内 | 本篇统计 | whatsapp_support_md |
| 一、限额与规模档位 | **5** | 条回读消息 | 用户拉黑或举报某业务时，平台回读该业务发给该用户的消息数 | 硬数量上限 | help_1805617343145907 |
| 一、限额与规模档位 | **1** | 种载入测试 | 载入测试页声明云端接口当前只提供的一种测试（本篇按红线只登记名称） | 流程口径 | whatsapp_support_load_testing_md |
| 一、限额与规模档位 | **2** | 类适用场景 | 载入测试页写明该测试适用的场景数 | 流程口径 | whatsapp_support_load_testing_md |
| 一、限额与规模档位 | **2** | 个实验放行条件 | 营销类实验中让用户重新能收到营销模板的两个条件，满足其一即可 | 条件触发项 | whatsapp_support_experiments_md |
| 一、限额与规模档位 | **4** | 种交互消息头部类型 | Direct Send 交互消息头部可取的形态：文本、图片、视频、文档 | 取值域基数 | whatsapp_direct_send_supported_message_types_md |
| 一、限额与规模档位 | **3** | 个预设回复 | 回复按钮类消息最多可携带的预设回复数 | 硬数量上限 | whatsapp_direct_send_supported_message_types_md |
| 一、限额与规模档位 | **2** | 类生成模板 | 官方写明 Direct Send 会创建的模板类别数：按内容生成与按所给名生成 | 取值域基数 | whatsapp_direct_send_view_generated_templates_md |
| 二、速率与吞吐档位 | **24** | 小时 | 吞吐升档资格句里的移动窗口长度，与限额页的 30 天窗口分属两套口径 | 滚动窗口 | whatsapp_throughput_md |
| 三、时长与窗口 | **180** | 天 | 伙伴替客户回读历史消息的窗口长度，自该业务接入云端接口之时起算 | 硬时长（定值） | whatsapp_webhooks_reference_history_md |
| 三、时长与窗口 | **14** | 天 | 历史同步里媒体消息资产 ID 的可回读窗口；与嵌入式注册后必须完成注册的那个 14 天分属两件事 | 硬时长（定值） | whatsapp_webhooks_reference_history_md |
| 三、时长与窗口 | **24** | 小时 | 企业开发者工单在个工作日内首次响应的口径 | 硬时长（定值） | whatsapp_support_md |
| 三、时长与窗口 | **24** | 小时 | 不存在开放实用类会话时 Direct Send 新开的实用类会话时长，自第一条消息起算 | 硬时长（定值） | whatsapp_direct_send_faq_md |
| 三、时长与窗口 | **6** | 小时 | 限额计算口径改为按业务资产组合后，升档所需时长的上限 | 区间或多值 | whatsapp_upcoming_messaging_limits_changes_md |
| 三、时长与窗口 | **24** | 小时 | 改口径之前升档时长写作的小时数；与上一行是同一件事在两个时点的两个数 | 硬时长（定值） | whatsapp_upcoming_messaging_limits_changes_md |
| 三、时长与窗口 | **30** | 天 | 限额页 2,000 档自主路径所用的滚动窗口长度 | 滚动窗口 | whatsapp_messaging_limits_md |
| 四、版本与日期节点 | **2025-10-07** | 日期节点 | 限额计算口径由按号码改为按业务资产组合的生效日 | 生效节点 | whatsapp_upcoming_messaging_limits_changes_md |
| 五、编号与计数口径 | **3** | 条状态回调 | 一条出站消息最多可以收到的独立状态回调数（官方写成上限而非等式） | 硬数量上限 | whatsapp_webhooks_reference_messages_md |
| 五、编号与计数口径 | **22** | 页 | 围绕 messages 字段展开的参考页总数：主页 1＋子类型 19＋边界 2（本表统计值） | 本篇统计 | whatsapp_webhooks_reference_messages_md |
| 五、编号与计数口径 | **19** | 页 | `messages/` 目录下的子类型参考页数（本表统计值） | 本篇统计 | whatsapp_webhooks_reference_messages_md |
| 五、编号与计数口径 | **16** | 页 | 逐字共用同一句开场骨架的子类型页数（本表统计值） | 本篇统计 | whatsapp_webhooks_reference_messages_text_md |
| 五、编号与计数口径 | **3** | 页 | 各以自己的方式偏离开场骨架的子类型页数：edit、revoke、status（本表统计值） | 本篇统计 | whatsapp_webhooks_reference_messages_status_md |
| 五、编号与计数口径 | **50** | 条触发 | 19 个子类型页 Triggers 清单条目合计（本表统计值） | 本篇统计 | whatsapp_webhooks_reference_messages_text_md |
| 五、编号与计数口径 | **7** | 条触发 | status 页的 Triggers 条目数，为全部子类型页中的最大值 | 本篇统计 | whatsapp_webhooks_reference_messages_status_md |
| 五、编号与计数口径 | **1** | 条触发 | button 页与 order 页各自的 Triggers 条目数，为全部子类型页中的最小值 | 本篇统计 | whatsapp_webhooks_reference_messages_button_md |
| 五、编号与计数口径 | **3** | 条触发 | unsupported 页的 Triggers 条目数 | 本篇统计 | whatsapp_webhooks_reference_messages_unsupported_md |
| 五、编号与计数口径 | **5** | 个状态取值 | status 页给 status 字段列出的取值数；与同页 7 条触发不对应 | 取值域基数 | whatsapp_webhooks_reference_messages_status_md |
| 五、编号与计数口径 | **7** | 个取值 | SOLUTION_STATUS 在伙伴方案正文页列出的取值数（含 PENDING） | 取值域基数 | whatsapp_solution_providers_multi_partner_solutions_md |
| 五、编号与计数口径 | **6** | 个取值 | SOLUTION_STATUS 在 partner_solutions 回调参考页列出的取值数（无 PENDING） | 取值域基数 | whatsapp_webhooks_reference_partner_solutions_md |
| 五、编号与计数口径 | **349** | 个取值 | 附表 D 登记的枚举取值总数（本表统计值） | 本篇统计 | whatsapp_webhooks_reference_messages_status_md |
| 五、编号与计数口径 | **73** | 个取值组 | 附表 D 登记的取值组数（本表统计值） | 本篇统计 | whatsapp_webhooks_reference_messages_status_md |
| 五、编号与计数口径 | **55** | 组 | 冒号式句式可解析出的取值组数（本表统计值） | 本篇统计 | whatsapp_webhooks_reference_messages_status_md |
| 五、编号与计数口径 | **253** | 个取值 | 冒号式句式可解析出的取值数（本表统计值） | 本篇统计 | whatsapp_webhooks_reference_messages_status_md |
| 五、编号与计数口径 | **18** | 组 | 行内式与散文式合计的取值组数，全部人工核定（本表统计值） | 本篇统计 | whatsapp_direct_send_supported_message_types_md |
| 五、编号与计数口径 | **96** | 个取值 | 行内式与散文式合计的取值数，全部人工核定（本表统计值） | 本篇统计 | whatsapp_direct_send_supported_message_types_md |
| 五、编号与计数口径 | **36** | 页 | 附表 D 的取值组所覆盖的页面数（本表统计值） | 本篇统计 | whatsapp_webhooks_reference_messages_md |
| 五、编号与计数口径 | **12** | 行 | 附表 D 里说明文字被截断、需人工补全的取值组行数（本表统计值） | 本篇统计 | whatsapp_webhooks_reference_messages_md |
| 五、编号与计数口径 | **12** | 个维度 | 附表 E 对三类伙伴逐项对照所用的维度数（本表统计值） | 本篇统计 | whatsapp_solution_providers_overview_md |
| 五、编号与计数口径 | **3** | 格 | 附表 E 中官方文本本身写明"未按类别区分"的格子数（本表统计值） | 本篇统计 | whatsapp_solution_providers_overview_md |
| 五、编号与计数口径 | **19** | 项 | 附表 F 登记的口径冲突或官方未写明条目数（本表统计值） | 本篇统计 | whatsapp_webhooks_overview_md |
| 五、编号与计数口径 | **6** | 项 | 附表 F 中一侧为"官方未写明"的条目数（本表统计值） | 本篇统计 | whatsapp_webhooks_overview_md |
| 五、编号与计数口径 | **5** | 页 | 本批抓取只返回页面外壳、正文未取回且本篇未引用的页数 | 本篇统计 | whatsapp_webhooks_overview_md |
| **合计** | **155** 行／**89** 个去重条目 | **64** 种计数单位 | 五档分别对应规模上限、速率吞吐、时长窗口、版本节点、编号口径 | 性质列共 20 种 | 官方写成区间或多值的行全部保留原式，见第 13.1—13.2 节 |

#ROWS=155 COLS=6

# 附表 C 档位逐档表（56 行）

本表把官方分散在各页里的**档位与阶梯**合并成一张表，列序即官方叙述顺序：**体系＝主语层级**（第 1.3 节），**档位＝该体系内的档位名**，触发列与后果列**逐字照抄或直译官方措辞**，时长列同样照抄；官方没给时长的登记为「未写明」并且不做推断（第 13.2 节）。共 **55** 档，按体系分布：Direct Send 处置阶梯 6／健康状态聚合 3／共享解除后果 2／号码注册窗口 2／吞吐档位 5／回调地址解析 5／回调投递阶梯 4／多伙伴方案状态机 4／官方商业账号申请 3／应用运行模式 2／权限访问级别 3／模板复核生命周期 4／端点验证 2／系统用户访问阶梯 6／账号模型迁移阶段 3／载入测试处置 1；其中时长列为「未写明」的 **42** 档——本表的未写明只指时长这一格，全篇留白清单见附表 F。

| 编号 | 体系 | 档位 | 触发或达成条件 | 后果或效果 | 时长 | 出处页 |
|---|---|---|---|---|---|---|

| 1 | 吞吐档位 | 80 条/秒（默认） | 每个已注册业务号码 | 超出后持续返回错误码 130429，回到允许水平为止 | 未写明 | whatsapp_throughput_md |
| 2 | 吞吐档位 | 1,000 条/秒（自动升档） | 组合限额为不限、24 小时窗口内触达 10 万以上唯一号码、质量分为黄或更高 | 免费升档，不额外计费 | 未写明 | whatsapp_throughput_md |
| 3 | 吞吐档位 | 升档过程 | 满足资格条件后触发 | 期间该号码在平台上不可用，用它发请求返回 131057 | 最多 1 分钟 | whatsapp_throughput_md |
| 4 | 吞吐档位 | 升档后再升档 | 同一号码后续任何吞吐提升 | 自动完成且不再停机 | 未写明 | whatsapp_throughput_md |
| 5 | 吞吐档位 | 20 条/秒（固定） | 号码同时用于商业应用与云端接口 | 吞吐固定，且不因升档机制变化 | 未写明 | whatsapp_throughput_md |
| 6 | 回调投递阶梯 | 首次投递 | 字段被触发 | POST 到当前解析出的回调地址 | 未写明 | whatsapp_webhooks_create_webhook_endpoint_md |
| 7 | 回调投递阶梯 | 立即重试 | 任一次 POST 到本端失败 | 立即重投一次 | 未写明 | whatsapp_webhooks_create_webhook_endpoint_md |
| 8 | 回调投递阶梯 | 递减频率重投 | 失败后继续 | 按递减频率重投，可能产生重复通知，本端须自行去重 | 未来 7 天内 | whatsapp_webhooks_create_webhook_endpoint_md |
| 9 | 回调投递阶梯 | 丢弃 | 7 天内始终未确认 | 未确认的响应被丢弃 | 7 天后 | whatsapp_webhooks_create_webhook_endpoint_md |
| 10 | 回调地址解析 | 号码级备用地址 | 该事件所属业务号码设有备用回调地址 | 优先把回调送往号码级备用地址 | 未写明 | whatsapp_webhooks_override_md |
| 11 | 回调地址解析 | 消息账号级备用地址 | 号码无备用地址、但该号码所属消息账号有备用地址 | 送往消息账号级备用地址 | 未写明 | whatsapp_webhooks_override_md |
| 12 | 回调地址解析 | 应用默认地址 | 两级都无备用地址 | 回落到应用面板里配置的默认回调地址 | 未写明 | whatsapp_webhooks_override_md |
| 13 | 回调地址解析 | 不支持覆盖的字段 | 模板类与账号类字段被触发 | 恒送应用默认回调地址，不走上述阶梯 | 未写明 | whatsapp_webhooks_override_md |
| 14 | 回调地址解析 | 解除备用地址 | 按常规方式重新订阅且不带请求体参数 | 消息账号的备用地址被移除，恢复送默认地址 | 未写明 | whatsapp_webhooks_override_md |
| 15 | 端点验证 | 已验证 | GET 校验返回状态 200 且回送 hub.challenge | Meta 认定端点已验证，开始投递回调 | 未写明 | whatsapp_webhooks_create_webhook_endpoint_md |
| 16 | 端点验证 | 未验证 | 返回任何其他内容 | Meta 认定端点未验证，不向该端点投递任何回调 | 未写明 | whatsapp_webhooks_create_webhook_endpoint_md |
| 17 | Direct Send 处置阶梯 | 模板通知 | Meta 在某个模板上检出非合规内容 | 邮件与回调各发一次通知，此时还没有账号级限制 | 未写明 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| 18 | Direct Send 处置阶梯 | 警告 | 滥用继续 | 邮件与回调发出警告；violation_type 取警告类取值，不附带限制明细 | 持续存在（Ongoing） | whatsapp_direct_send_integrity_and_content_guidelines_md |
| 19 | Direct Send 处置阶梯 | 限速 | 滥用持续 | 对消息账号设临时上限，达到上限后实用类发送被 131064 拒绝 | 直到回落或窗口结束 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| 20 | Direct Send 处置阶梯 | 消息限制 | 滥用持续 | 该消息账号不得用 Direct Send 发实用类与认证类消息 | 7 天 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| 21 | Direct Send 处置阶梯 | 延长限制 | 滥用继续 | 限制被延长；正文称这是永久撤销前的最后一次警告 | 30 天 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| 22 | Direct Send 处置阶梯 | 永久撤销 | 滥用继续 | Direct Send 访问被永久撤销 | 永久 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| 23 | 模板复核生命周期 | Available for review | 模板被判为营销内容 | 处于可申请复核状态 | 60 天内可申请 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| 24 | 模板复核生命周期 | In review | 已提交复核申请 | Meta 正在评估；期间发送与访问不受影响 | 多数需数天 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| 25 | 模板复核生命周期 | Reversed | 复核通过 | 分类恢复为实用类，消息继续正常投递 | 未写明 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| 26 | 模板复核生命周期 | Unchanged | 复核确认内容属营销 | 分类维持不变；每个模板只能复核一次 | 未写明 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| 27 | 账号模型迁移阶段 | Phase 1 通用可用 | 2026-09-23 起按批次灰度 | Meta 自动迁移账号；现有 ID、端点与令牌继续可用；号码级双因素口令对符合条件的号码停用 | 到 2026 年 10 月中覆盖全部企业 | whatsapp_account_model_evolution_md |
| 28 | 账号模型迁移阶段 | Phase 2 新 Graph API 版本 | 2027 年上半年 | 新增账号级端点与 whatsapp_account 回调主题；权限拆分为两套作用域；返回独立 WAAC ID | 未写明 | whatsapp_account_model_evolution_md |
| 29 | 账号模型迁移阶段 | Phase 3 强制切换 | 2028 年上半年 | 凡在路径或参数上指向号码 ID 的接口一律改指向 WAAC ID；多消息账号场景必须带消息账号标识 | 未写明 | whatsapp_account_model_evolution_md |
| 30 | 健康状态聚合 | BLOCKED | 一个或多个节点被阻断 | 总体取被阻断，附带 errors 属性说明原因与可能解法 | 未写明 | whatsapp_support_health_status_md |
| 31 | 健康状态聚合 | LIMITED | 无节点被阻断、但一个或多个节点受限 | 总体取受限，附带 additional info | 未写明 | whatsapp_support_health_status_md |
| 32 | 健康状态聚合 | AVAILABLE | 全部节点均可用 | 总体取可用 | 未写明 | whatsapp_support_health_status_md |
| 33 | 系统用户访问阶梯 | Admin 系统用户 | 创建时选择管理员角色 | 默认对名下与共享给本组合的全部账号及资产拥有完全访问，新建资产无需逐个授权 | 未写明 | whatsapp_access_tokens_md |
| 34 | 系统用户访问阶梯 | Employee 系统用户 | 创建时选择员工角色 | 必须逐个账号显式授予业务资产访问，否则接口返回错误码 200 | 未写明 | whatsapp_access_tokens_md |
| 35 | 系统用户访问阶梯 | Full 业务资产访问 | 在账号的人员页授予完全控制 | 该账号全部资产与动作放行 | 未写明 | whatsapp_access_tokens_md |
| 36 | 系统用户访问阶梯 | Partial 业务资产访问 | 在账号的人员页授予部分访问 | 可再按资产或动作收窄到只读 | 未写明 | whatsapp_access_tokens_md |
| 37 | 系统用户访问阶梯 | 接口侧级联（临时行为） | 通过接口在消息账号上授予访问 | 同一授权自动应用到与之关联的 WhatsApp 账号 | 未写明 | whatsapp_access_tokens_md |
| 38 | 系统用户访问阶梯 | 界面侧不级联 | 在商业套件面板里授予访问 | 两个账号必须分别显式指派 | 未写明 | whatsapp_access_tokens_md |
| 39 | 权限访问级别 | 标准访问 | 未申请或未获高级访问批准 | 无法经由嵌入式注册把该权限让给客户授予；用于访问非本企业账号的接口调用返回错误码 200 | 未写明 | whatsapp_permissions_md |
| 40 | 权限访问级别 | 高级访问 | 伙伴应用通过应用复核并获该权限的高级访问批准 | 客户才能在嵌入式注册授权页把该权限授予本应用 | 未写明 | whatsapp_permissions_md |
| 41 | 权限访问级别 | 直接开发者豁免 | 只访问本企业自己的数据 | 无须应用复核，也无须任何权限的高级访问 | 未写明 | whatsapp_permissions_md |
| 42 | 应用运行模式 | Dev 模式 | 应用尚未切到生产可用 | 部分回调不会被发送 | 未写明 | whatsapp_webhooks_overview_md |
| 43 | 应用运行模式 | Live 模式 | 应用已切换 | 回调按订阅字段正常发送 | 未写明 | whatsapp_webhooks_overview_md |
| 44 | 多伙伴方案状态机 | Draft／Initiated／Pending | 一方发起并保存方案、另一方尚未收到邀请 | 方案处于待接受状态；正文注明该状态下的客户在嵌入式注册里会看到"此时不可用于入驻"的错误 | 未写明 | whatsapp_solution_providers_multi_partner_solutions_md |
| 45 | 多伙伴方案状态机 | Active | 被请求方在接口或应用面板接受 | 双方都能用方案 ID 定制嵌入式注册流程 | 未写明 | whatsapp_solution_providers_multi_partner_solutions_md |
| 46 | 多伙伴方案状态机 | Pending deactivation | 一方请求停用 | 状态改为待停用，另一方收到邮件、界面通知与回调，可接受或拒绝 | 未写明 | whatsapp_solution_providers_multi_partner_solutions_md |
| 47 | 多伙伴方案状态机 | Deactivated／Rejected | 对方接受或拒绝停用请求 | 方案不再用于共同管理客户资产 | 未写明 | whatsapp_solution_providers_multi_partner_solutions_md |
| 48 | 共享解除后果 | 消息全面阻断 | 客户的账号与消息账号不再与本伙伴共享 | 该消息账号的全部消息发送被阻断，以保护本伙伴授信额度 | 未写明 | whatsapp_solution_providers_share_and_revoke_credit_lines_md |
| 49 | 共享解除后果 | 建议动作 | 共享刚被解除 | 正文建议在解除后尽快撤销授信额度 | 未写明 | whatsapp_solution_providers_share_and_revoke_credit_lines_md |
| 50 | 官方商业账号申请 | 已批准 | 审核认为号码符合条件 | 号码获得官方商业账号状态并在名称旁显示绿色标识 | 未写明 | whatsapp_official_business_accounts_md |
| 51 | 官方商业账号申请 | 被拒绝 | 审核认为当前不符合条件 | 须等待后方可重新提交 | 30 天 | whatsapp_official_business_accounts_md |
| 52 | 官方商业账号申请 | 个人主体 | 账号告警回调 ALERT_DESCRIPTION 文案枚举命中该条 | 该条文案直接写明不向个人授予官方商业账号，并要求显示名直接关联企业、编辑显示名后重新提交 | 未写明 | whatsapp_webhooks_reference_account_alerts_md |
| 53 | 号码注册窗口 | 14 天内注册 | 走完嵌入式注册流程 | 超窗则必须重新走一遍嵌入式注册流程才能注册 | 14 天 | whatsapp_solution_providers_get_started_for_solution_partners_md |
| 54 | 号码注册窗口 | 再注册不重置口令 | 对已注册号码重复执行注册 | 注册成功且不重置双因素口令 | 未写明 | whatsapp_account_model_evolution_md |
| 55 | 载入测试处置 | 测试号码入站熔断 | 向官方提供的测试号码发入站流量 | 触发熔断，该号码被断开连接 | 未写明 | whatsapp_support_load_testing_md |
| **合计** | 16 个体系 | **55** 档 | 触发与后果列均为官方原句或直译 | 未写明 **42** 档 | 时长列未逐格标注 | 逐行核验原句，核验不过即中止 |

#ROWS=55 COLS=7

# 附表 D 官方文档枚举取值域全表（350 行）

本表一行一个取值。其中 **253** 个取值（**55** 个取值组）由脚本从各页表格行的「Values can be:／Value can be:」结构里直接取出，这 **55** 组按官方写法分三种：**换行列表式 53 组**（每个取值独占一行、形如「`` `取值` `` — 官方说明」，脚本按行拆）、**行内反引号式 1 组**（官方在一句里写「`` `甲` `` or `` `乙` ``」，脚本逐个反引号取出并回表核验）、**散文式 1 组**（官方用不带反引号的「a business portfolio ID or business phone number ID」这类句子给出，取值逐条手定并回表核验）。换行列表式里属性名取不到、取值列表为空、同组内取值重复、列表项不带反引号，四种情况脚本一律中止；行内与散文两式的每一条取值都单独回表核验，核不过即记入失败清单。其余 **96** 个取值来自手定组——手定组同样由脚本从指定小节里逐条抓出，条目数不符即中止，抓不到小节也中止。全表合计 **349** 个取值、**73** 个取值组，来自 **36** 个页面；说明列超过 150 字符时按整词截断（共 **12** 行截断），截断后仍是原句前缀。

| 编号 | 取值域 | 取值 | 官方说明或含义归纳 | 出处页 |
| --- | --- | --- | --- | --- |
| D1    | 方案变更事件（正文位） | `SOLUTION_CREATED` | Solution created. | whatsapp_solution_providers_multi_partner_solutions_md |
| D2    | 方案变更事件（正文位） | `SOLUTION_UPDATED` | The `solution_status` has changed. | whatsapp_solution_providers_multi_partner_solutions_md |
| D3    | 多伙伴方案状态 | `ACTIVE` | The solution partner has accepted the solution request and it can be used with Embedded Signup. | whatsapp_solution_providers_multi_partner_solutions_md |
| D4    | 多伙伴方案状态 | `DEACTIVATED` | The solution has been deactivated and cannot be used with Embedded Signup to onboard clients. | whatsapp_solution_providers_multi_partner_solutions_md |
| D5    | 多伙伴方案状态 | `DRAFT` | Solution has been drafted but invitation has not been sent to the solution partner. | whatsapp_solution_providers_multi_partner_solutions_md |
| D6    | 多伙伴方案状态 | `INITIATED` | The solution partner has been invited to accept the solution, but has yet to accept or reject the request. | whatsapp_solution_providers_multi_partner_solutions_md |
| D7    | 多伙伴方案状态 | `PENDING` | The solution partner has yet to accept or reject the solution request. | whatsapp_solution_providers_multi_partner_solutions_md |
| D8    | 多伙伴方案状态 | `PENDING_DEACTIVATION` | The solution owner requested for an active solution to be deactivated but the solution partner has yet to accept the deactivation request. | whatsapp_solution_providers_multi_partner_solutions_md |
| D9    | 多伙伴方案状态 | `REJECTED` | The solution partner has rejected the solution request. | whatsapp_solution_providers_multi_partner_solutions_md |
| D10   | 伙伴主导核验结论 | `APPROVED` | Indicates the client&#039;s business has been verified. | whatsapp_solution_providers_partner_led_business_verification_md |
| D11   | 伙伴主导核验结论 | `FAILED` | Indicates Meta is unable to verify the client&#039;s business based on the submitted business information. | whatsapp_solution_providers_partner_led_business_verification_md |
| D12   | 验证码送达方式 | `SMS` | `SMS` or `VOICE`. | whatsapp_solution_providers_registering_phone_numbers_md |
| D13   | 验证码送达方式 | `VOICE` | `SMS` or `VOICE`. | whatsapp_solution_providers_registering_phone_numbers_md |
| D14   | 告警描述全文 | `Additional verification is required for your business {NAME}. Go to Security Center in Meta for Business to complete identity verification. To continue without completing additional verification, your business can use WhatsApp Business platform actively for several days and follow our messaging policies.` | — | whatsapp_webhooks_reference_account_alerts_md |
| D15   | 告警描述全文 | `Based on your activity, limits cannot be increased for your business. Contact support for more information.` | — | whatsapp_webhooks_reference_account_alerts_md |
| D16   | 告警描述全文 | `Limits cannot be increased for your business <BUSINESS_PORTFOLIO_NAME>. Use WhatsApp Business platform actively for several days and follow our messaging policies.` | — | whatsapp_webhooks_reference_account_alerts_md |
| D17   | 告警描述全文 | `Limits cannot be increased at this time for your business <BUSINESS_PORTFOLIO_NAME>. Limits cannot be increased as your identity verification submission was rejected. To continue without additional verification, use WhatsApp Business platform actively for several days and follow our messaging policies.` | — | whatsapp_webhooks_reference_account_alerts_md |
| D18   | 告警描述全文 | `Please reupload your profile picture` | — | whatsapp_webhooks_reference_account_alerts_md |
| D19   | 告警描述全文 | `This phone number now has a green badge next to its name showing that it&#039;s an authentic and notable business account. Add more details to your business profile to increase customer trust.` | — | whatsapp_webhooks_reference_account_alerts_md |
| D20   | 告警描述全文 | `We do not grant official business accounts to individuals. Your display name must be directly associated with your business. Edit the display name and submit a new request.` | — | whatsapp_webhooks_reference_account_alerts_md |
| D21   | 告警描述全文 | `Your messaging quality was too low to unlock more capabilities at this time. Alternatively, your business can unlock more capabilities by submitting business documents to verify your business.` | — | whatsapp_webhooks_reference_account_alerts_md |
| D22   | 告警级别 | `CRITICAL` | Indicates a rejection or denial. The `alert_description` value may describe actions that can be taken to resolve the underlying reason for the … | whatsapp_webhooks_reference_account_alerts_md |
| D23   | 告警级别 | `INFORMATIONAL` | Indicates webhooks contain only informational data and no action is needed. | whatsapp_webhooks_reference_account_alerts_md |
| D24   | 告警级别 | `WARNING` | Indicates action may be needed. The `alert_description` value describes possible actions that can be taken. | whatsapp_webhooks_reference_account_alerts_md |
| D25   | 告警在位状态 | `ACTIVE` | — | whatsapp_webhooks_reference_account_alerts_md |
| D26   | 告警在位状态 | `NONE` | — | whatsapp_webhooks_reference_account_alerts_md |
| D27   | 告警类型 | `INCREASED_CAPABILITIES_ELIGIBILITY_DEFERRED` | Indicates Meta doesn&#039;t have enough message signal to make a determination, identity verification was rejected, or your message quality is too … | whatsapp_webhooks_reference_account_alerts_md |
| D28   | 告警类型 | `INCREASED_CAPABILITIES_ELIGIBILITY_FAILED` | Indicates messaging limits cannot be increased due to past messaging activity. Possible solutions are to request an increase or get your business … | whatsapp_webhooks_reference_account_alerts_md |
| D29   | 告警类型 | `INCREASED_CAPABILITIES_ELIGIBILITY_NEED_MORE_INFO` | Indicates messaging limits cannot be increased due to past messaging activity. Possible solutions are to verify your identity or increase your … | whatsapp_webhooks_reference_account_alerts_md |
| D30   | 告警类型 | `OBA_APPROVED` | Indicates Official Business Account status approved. | whatsapp_webhooks_reference_account_alerts_md |
| D31   | 告警类型 | `OBA_REJECTED` | Indicates Official Business Account ("OBA") status denied. Review OBA criteria and edit your display name after reviewing our display name guidelines. | whatsapp_webhooks_reference_account_alerts_md |
| D32   | 告警类型 | `PROFILE_PICTURE_LOST` | Indicates the business phone number&#039;s business profile photo has been deleted. Upload a new photo using WhatsApp Manager or the API. | whatsapp_webhooks_reference_account_alerts_md |
| D33   | 告警对象标识 | `a business portfolio ID` | a business portfolio ID or business phone number ID. | whatsapp_webhooks_reference_account_alerts_md |
| D34   | 告警对象标识 | `business phone number ID` | a business portfolio ID or business phone number ID. | whatsapp_webhooks_reference_account_alerts_md |
| D35   | 告警对象类型 | `BUSINESS` | Indicates a change associated with a business portfolio. | whatsapp_webhooks_reference_account_alerts_md |
| D36   | 告警对象类型 | `PHONE_NUMBER` | Indicates a change associated with a business phone number. | whatsapp_webhooks_reference_account_alerts_md |
| D37   | 告警对象类型 | `CURRENT_STATUS_ID` | Indicates a change associated with a business phone number&#039;s business profile. | whatsapp_webhooks_reference_account_alerts_md |
| D38   | 账号审核结论 | `APPROVED` | Indicates your WhatsApp account is approved and ready for use. | whatsapp_webhooks_reference_account_review_update_md |
| D39   | 账号审核结论 | `REJECTED` | Indicates your WhatsApp account was rejected because it doesn&#039;t meet our policy requirements and cannot be used with our APIs. | whatsapp_webhooks_reference_account_review_update_md |
| D40   | 账号审核结论 | `PENDING` | Indicates a review decision is still pending and your WhatsApp account currently cannot be used with our APIs. | whatsapp_webhooks_reference_account_review_update_md |
| D41   | 账号审核结论 | `DEFERRED` | Indicates a review decision has been deferred and your WhatsApp account currently cannot be used with our APIs. | whatsapp_webhooks_reference_account_review_update_md |
| D42   | 断开发起方 | `SYSTEM` | The disconnection was system-initiated (for example, due to device inactivity or enforcement). | whatsapp_webhooks_reference_account_update_md |
| D43   | 断开发起方 | `USER` | The disconnection was client-initiated (for example, your client changed their phone number, re-registered on a new device, deleted their WhatsApp … | whatsapp_webhooks_reference_account_update_md |
| D44   | 断开原因 | `ACCOUNT_DISCONNECTED` | Your client&#039;s account was disconnected due to enforcement or because your client explicitly deleted their WhatsApp account. Can be initiated by … | whatsapp_webhooks_reference_account_update_md |
| D45   | 断开原因 | `BUSINESS_DOWNGRADE` | Your client registered their business phone number with the consumer WhatsApp app. | whatsapp_webhooks_reference_account_update_md |
| D46   | 断开原因 | `CHANGE_NUMBER` | Your client changed their phone number. | whatsapp_webhooks_reference_account_update_md |
| D47   | 断开原因 | `COMPANION_INACTIVITY` | A companion device was inactive for approximately 30 days. | whatsapp_webhooks_reference_account_update_md |
| D48   | 断开原因 | `PRIMARY_INACTIVITY` | The primary device was inactive for approximately 14 days. | whatsapp_webhooks_reference_account_update_md |
| D49   | 断开原因 | `USER_RE_REGISTERED` | Your client re-registered on a new device. | whatsapp_webhooks_reference_account_update_md |
| D50   | 消息账号事件 | `ACCOUNT_DELETED` | Indicates the Messaging account was deleted. | whatsapp_webhooks_reference_account_update_md |
| D51   | 消息账号事件 | `ACCOUNT_RESTRICTION` | Indicates your WhatsApp account has been restricted due to policy violations. See `restriction_info` for restriction details. | whatsapp_webhooks_reference_account_update_md |
| D52   | 消息账号事件 | `ACCOUNT_VIOLATION` | Indicates your WhatsApp account violated Meta policies or terms. | whatsapp_webhooks_reference_account_update_md |
| D53   | 消息账号事件 | `AD_ACCOUNT_LINKED` | Indicates the Messaging account has been onboarded onto Marketing Messages API for WhatsApp through Embedded Signup or Intent API and gives the … | whatsapp_webhooks_reference_account_update_md |
| D54   | 消息账号事件 | `AUTH_INTL_PRICE_ELIGIBILITY_UPDATE` | Indicates the Messaging account is eligible for authentication-international rates. | whatsapp_webhooks_reference_account_update_md |
| D55   | 消息账号事件 | `BUSINESS_PRIMARY_LOCATION_COUNTRY_UPDATE` | Indicates the Messaging account&#039;s primary business location has been set. | whatsapp_webhooks_reference_account_update_md |
| D56   | 消息账号事件 | `DISABLED_UPDATE` | Indicates your WhatsApp account violated Meta policies or terms. | whatsapp_webhooks_reference_account_update_md |
| D57   | 消息账号事件 | `MM_LITE_TERMS_SIGNED` | Indicates that the Messaging account has successfully accepted the MM API for WhatsApp terms of service. | whatsapp_webhooks_reference_account_update_md |
| D58   | 消息账号事件 | `PARTNER_ADDED` | Indicates a WhatsApp account or Messaging account has been shared with a Solution Partner. | whatsapp_webhooks_reference_account_update_md |
| D59   | 消息账号事件 | `PARTNER_APP_INSTALLED` | Indicates a business customer granted the app one or more permissions. | whatsapp_webhooks_reference_account_update_md |
| D60   | 消息账号事件 | `PARTNER_APP_UNINSTALLED` | Indicates a business customer deauthenticated or uninstalled the app. | whatsapp_webhooks_reference_account_update_md |
| D61   | 消息账号事件 | `PARTNER_CLIENT_CERTIFICATION_STATUS_UPDATE` | Indicates your WhatsApp account&#039;s partner-led business verification submission is approved, rejected, or discarded. | whatsapp_webhooks_reference_account_update_md |
| D62   | 消息账号事件 | `PARTNER_REMOVED` | Indicates a WhatsApp account or Messaging account has been unshared with a Solution Partner. | whatsapp_webhooks_reference_account_update_md |
| D63   | 消息账号事件 | `VOLUME_BASED_PRICING_TIER_UPDATE` | Indicates the Messaging account&#039;s volume-based pricing tier has been updated. | whatsapp_webhooks_reference_account_update_md |
| D64   | 消息账号事件 | `ACCOUNT_OFFBOARDED` | Indicates the WhatsApp account has been offboarded due to a device change or phone number reregistration. | whatsapp_webhooks_reference_account_update_md |
| D65   | 消息账号事件 | `ACCOUNT_RECONNECTED` | Indicates the WhatsApp account has been reconnected after a device change or phone number reregistration. | whatsapp_webhooks_reference_account_update_md |
| D66   | 伙伴主导核验拒绝原因 | `ADDRESS NOT MATCHING` | The country in the submitted address does not match the country on the client&#039;s business profile. Edit the submission or have your client update … | whatsapp_webhooks_reference_account_update_md |
| D67   | 伙伴主导核验拒绝原因 | `BUSINESS NOT ELIGIBLE` | Your client is not eligible for verification via partner-provided information. The client can still apply for Meta business verification directly. | whatsapp_webhooks_reference_account_update_md |
| D68   | 伙伴主导核验拒绝原因 | `LEGAL NAME NOT MATCHING` | The legal name in the submission does not match the legal name or business name on the client&#039;s business profile. The system checks for exact, … | whatsapp_webhooks_reference_account_update_md |
| D69   | 伙伴主导核验拒绝原因 | `LEGAL NAME NOT FOUND IN DOCUMENTS` | The automated document review could not locate the business legal name in the uploaded documents. Common causes include: The business legal name is … | whatsapp_webhooks_reference_account_update_md |
| D70   | 伙伴主导核验拒绝原因 | `MALFORMED DOCUMENTS` | The uploaded documents could not be processed. The files may be corrupted, password protected, or in an unsupported format. | whatsapp_webhooks_reference_account_update_md |
| D71   | 伙伴主导核验拒绝原因 | `NONE` | Indicates the submission was not rejected. | whatsapp_webhooks_reference_account_update_md |
| D72   | 伙伴主导核验拒绝原因 | `WEBSITE NOT MATCHING` | The website domain in the submission does not match the website domain on the client&#039;s business profile. Edit the submission or have your client … | whatsapp_webhooks_reference_account_update_md |
| D73   | 账号限制类型 | `RESTRICTED_ADD_PHONE_NUMBER_ACTION` | Business cannot add new phone numbers to the account. | whatsapp_webhooks_reference_account_update_md |
| D74   | 账号限制类型 | `RESTRICTED_BIZ_INITIATED_AND_USER_INITIATED_CALLING` | Business cannot make or receive calls. | whatsapp_webhooks_reference_account_update_md |
| D75   | 账号限制类型 | `RESTRICTED_BIZ_INITIATED_MESSAGING` | Business cannot initiate conversations with customers. | whatsapp_webhooks_reference_account_update_md |
| D76   | 账号限制类型 | `RESTRICTED_BUSINESS_INITIATED_CALLING` | Business cannot initiate outbound calls. | whatsapp_webhooks_reference_account_update_md |
| D77   | 账号限制类型 | `RESTRICTED_CUSTOMER_INITIATED_MESSAGING` | Business cannot respond to customer-initiated messages. | whatsapp_webhooks_reference_account_update_md |
| D78   | 账号限制类型 | `RESTRICTED_DIRECT_SEND_UTILITY_TEMPLATES` | Business cannot send utility templates via Direct Send. | whatsapp_webhooks_reference_account_update_md |
| D79   | 账号限制类型 | `RESTRICTED_USER_INITIATED_CALLING` | Business cannot receive inbound calls from users. | whatsapp_webhooks_reference_account_update_md |
| D80   | 账号限制类型 | `RESTRICTED_USER_INITIATED_CALLING_CALL_BUTTON_HIDDEN` | Call button is hidden from users due to low pickup rates. | whatsapp_webhooks_reference_account_update_md |
| D81   | 账号限制类型 | `RESTRICTED_UTILITY_TEMPLATES` | Business cannot create utility templates. | whatsapp_webhooks_reference_account_update_md |
| D82   | 核验提交流水状态 | `APPROVED` | Submission has been reviewed and approved. | whatsapp_webhooks_reference_account_update_md |
| D83   | 核验提交流水状态 | `DISCARDED` | Submission has been discarded due to technical issues or has not made progress for a while. | whatsapp_webhooks_reference_account_update_md |
| D84   | 核验提交流水状态 | `FAILED` | Submission has been reviewed and rejected. See `<REJECTION_REASONS>` for details. | whatsapp_webhooks_reference_account_update_md |
| D85   | 核验提交流水状态 | `PENDING` | Submission is pending review. | whatsapp_webhooks_reference_account_update_md |
| D86   | 核验提交流水状态 | `REVOKED` | Submission has been revoked. | whatsapp_webhooks_reference_account_update_md |
| D87   | 账号封禁状态位 | `DISABLE` | Indicates your WhatsApp account is disabled. | whatsapp_webhooks_reference_account_update_md |
| D88   | 账号封禁状态位 | `REINSTATE` | Indicates your WhatsApp account has been reinstated. | whatsapp_webhooks_reference_account_update_md |
| D89   | 账号封禁状态位 | `SCHEDULE_FOR_DISABLE` | Indicates your WhatsApp account has been scheduled to be disabled. | whatsapp_webhooks_reference_account_update_md |
| D90   | 自动事件名 | `LeadSubmitted` | Indicates a lead event. | whatsapp_webhooks_reference_automatic_events_md |
| D91   | 自动事件名 | `Purchase` | Indicates a purchase event. | whatsapp_webhooks_reference_automatic_events_md |
| D92   | 商业资产组合每日消息上限档位（档名制） | `TIER_250` | — | whatsapp_webhooks_reference_business_capability_update_md |
| D93   | 商业资产组合每日消息上限档位（档名制） | `TIER_2K` | — | whatsapp_webhooks_reference_business_capability_update_md |
| D94   | 商业资产组合每日消息上限档位（档名制） | `TIER_10K` | — | whatsapp_webhooks_reference_business_capability_update_md |
| D95   | 商业资产组合每日消息上限档位（档名制） | `TIER_100K` | — | whatsapp_webhooks_reference_business_capability_update_md |
| D96   | 商业资产组合每日消息上限档位（档名制） | `TIER_UNLIMITED` | — | whatsapp_webhooks_reference_business_capability_update_md |
| D97   | 每日唯一会话数档位（数值制，将下架） | `250` | — | whatsapp_webhooks_reference_business_capability_update_md |
| D98   | 每日唯一会话数档位（数值制，将下架） | `2000` | — | whatsapp_webhooks_reference_business_capability_update_md |
| D99   | 每日唯一会话数档位（数值制，将下架） | `10000` | — | whatsapp_webhooks_reference_business_capability_update_md |
| D100  | 每日唯一会话数档位（数值制，将下架） | `100000` | — | whatsapp_webhooks_reference_business_capability_update_md |
| D101  | 每日唯一会话数档位（数值制，将下架） | `-1` | A value of `-1` indicates unlimited messaging. | whatsapp_webhooks_reference_business_capability_update_md |
| D102  | 历史同步消息状态 | `DELIVERED` | — | whatsapp_webhooks_reference_history_md |
| D103  | 历史同步消息状态 | `ERROR` | — | whatsapp_webhooks_reference_history_md |
| D104  | 历史同步消息状态 | `PENDING` | — | whatsapp_webhooks_reference_history_md |
| D105  | 历史同步消息状态 | `PLAYED` | — | whatsapp_webhooks_reference_history_md |
| D106  | 历史同步消息状态 | `READ` | — | whatsapp_webhooks_reference_history_md |
| D107  | 历史同步消息状态 | `SENT` | — | whatsapp_webhooks_reference_history_md |
| D108  | 历史同步阶段 | `0` | indicates messages are from day 0 (business onboarding time) through day 1 | whatsapp_webhooks_reference_history_md |
| D109  | 历史同步阶段 | `1` | indicates messages are from day 1 through day 90 | whatsapp_webhooks_reference_history_md |
| D110  | 历史同步阶段 | `2` | indicates messages are from day 90 through day 180 | whatsapp_webhooks_reference_history_md |
| D111  | 模板新质量分 | `GREEN` | Indicates high quality. | whatsapp_webhooks_reference_message_template_quality_update_md |
| D112  | 模板新质量分 | `RED` | Indicates low quality. | whatsapp_webhooks_reference_message_template_quality_update_md |
| D113  | 模板新质量分 | `YELLOW` | Indicates medium quality. | whatsapp_webhooks_reference_message_template_quality_update_md |
| D114  | 模板新质量分 | `UNKNOWN` | Indicates quality pending. | whatsapp_webhooks_reference_message_template_quality_update_md |
| D115  | 模板原质量分 | `GREEN` | Indicates high quality. | whatsapp_webhooks_reference_message_template_quality_update_md |
| D116  | 模板原质量分 | `RED` | Indicates low quality. | whatsapp_webhooks_reference_message_template_quality_update_md |
| D117  | 模板原质量分 | `YELLOW` | Indicates medium quality. | whatsapp_webhooks_reference_message_template_quality_update_md |
| D118  | 模板原质量分 | `UNKNOWN` | Indicates quality pending. | whatsapp_webhooks_reference_message_template_quality_update_md |
| D119  | 模板状态事件 | `APPROVED` | Indicates the template has been approved and can now be sent in template messages. | whatsapp_webhooks_reference_message_template_status_update_md |
| D120  | 模板状态事件 | `ARCHIVED` | Indicates the template has been archived due to inactivity. Archived templates are scheduled for deletion after 28 days unless unarchived. | whatsapp_webhooks_reference_message_template_status_update_md |
| D121  | 模板状态事件 | `UNARCHIVED` | Indicates the template has been unarchived and restored to its previous status. | whatsapp_webhooks_reference_message_template_status_update_md |
| D122  | 模板状态事件 | `DELETED` | Indicates the template has been deleted. | whatsapp_webhooks_reference_message_template_status_update_md |
| D123  | 模板状态事件 | `DISABLED` | Indicates the template has been disabled due to user feedback. | whatsapp_webhooks_reference_message_template_status_update_md |
| D124  | 模板状态事件 | `FLAGGED` | Indicates the template has received negative feedback and is at risk of being disabled. | whatsapp_webhooks_reference_message_template_status_update_md |
| D125  | 模板状态事件 | `IN_APPEAL` | Indicates the template is in the appeal process. | whatsapp_webhooks_reference_message_template_status_update_md |
| D126  | 模板状态事件 | `LIMIT_EXCEEDED` | Indicates the Messaging account template is at its template limit. | whatsapp_webhooks_reference_message_template_status_update_md |
| D127  | 模板状态事件 | `LOCKED` | Indicates the template has been locked and cannot be edited. | whatsapp_webhooks_reference_message_template_status_update_md |
| D128  | 模板状态事件 | `PAUSED` | Indicates the template has been paused. | whatsapp_webhooks_reference_message_template_status_update_md |
| D129  | 模板状态事件 | `PENDING` | Indicates the template is undergoing template review. | whatsapp_webhooks_reference_message_template_status_update_md |
| D130  | 模板状态事件 | `REINSTATED` | Indicates the template is no longer flagged or disabled and can be sent in template messages again. | whatsapp_webhooks_reference_message_template_status_update_md |
| D131  | 模板状态事件 | `PENDING_DELETION` | Indicates template has been deleted via WhatsApp Manager. | whatsapp_webhooks_reference_message_template_status_update_md |
| D132  | 模板状态事件 | `REJECTED` | Indicates the template has been rejected. You can edit the template to have it undergo template review again or appeal the rejection. | whatsapp_webhooks_reference_message_template_status_update_md |
| D133  | 模板类别 | `MARKETING` | Indicates template is categorized as MARKETING. | whatsapp_webhooks_reference_message_template_status_update_md |
| D134  | 模板类别 | `UTILITY` | Indicates the template is categorized as UTILITY. | whatsapp_webhooks_reference_message_template_status_update_md |
| D135  | 模板类别 | `AUTHENTICATION` | Indicates template is categorized as AUTHENTICATION. | whatsapp_webhooks_reference_message_template_status_update_md |
| D136  | 模板暂停事件标题 | `FIRST_PAUSE` | Indicates template has been paused for the first time. | whatsapp_webhooks_reference_message_template_status_update_md |
| D137  | 模板暂停事件标题 | `SECOND_PAUSE` | Indicates the template has been paused a second time. | whatsapp_webhooks_reference_message_template_status_update_md |
| D138  | 模板暂停事件标题 | `RATE_LIMITING_PAUSE` | Indicates template has been paused due to rate limiting. | whatsapp_webhooks_reference_message_template_status_update_md |
| D139  | 模板暂停事件标题 | `UNPAUSE` | Indicates template has been unpaused. | whatsapp_webhooks_reference_message_template_status_update_md |
| D140  | 模板暂停事件标题 | `DISABLED` | Indicates template has been disabled. | whatsapp_webhooks_reference_message_template_status_update_md |
| D141  | 引流消息媒体类型 | `image` | Indicates an image ad. | whatsapp_webhooks_reference_messages_audio_md |
| D142  | 引流消息媒体类型 | `video` | Indicates a video ad. | whatsapp_webhooks_reference_messages_audio_md |
| D143  | 引流消息媒体类型 | `image` | Indicates an image ad. | whatsapp_webhooks_reference_messages_contacts_md |
| D144  | 引流消息媒体类型 | `video` | Indicates a video ad. | whatsapp_webhooks_reference_messages_contacts_md |
| D145  | 引流消息媒体类型 | `image` | Indicates an image ad. | whatsapp_webhooks_reference_messages_document_md |
| D146  | 引流消息媒体类型 | `video` | Indicates a video ad. | whatsapp_webhooks_reference_messages_document_md |
| D147  | 引流消息媒体类型 | `image` | Indicates an image ad. | whatsapp_webhooks_reference_messages_image_md |
| D148  | 引流消息媒体类型 | `video` | Indicates a video ad. | whatsapp_webhooks_reference_messages_image_md |
| D149  | 引流消息媒体类型 | `image` | Indicates an image ad. | whatsapp_webhooks_reference_messages_location_md |
| D150  | 引流消息媒体类型 | `video` | Indicates a video ad. | whatsapp_webhooks_reference_messages_location_md |
| D151  | 会话类别 | `authentication` | Indicates an authentication conversation. | whatsapp_webhooks_reference_messages_status_md |
| D152  | 会话类别 | `authentication_international` | Indicates an authentication-international conversation. | whatsapp_webhooks_reference_messages_status_md |
| D153  | 会话类别 | `marketing` | Indicates a marketing conversation. | whatsapp_webhooks_reference_messages_status_md |
| D154  | 会话类别 | `marketing_lite` | Indicates a Marketing Messages API for WhatsApp conversation. | whatsapp_webhooks_reference_messages_status_md |
| D155  | 会话类别 | `referral_conversion` | Indicates a free entry point conversation. | whatsapp_webhooks_reference_messages_status_md |
| D156  | 会话类别 | `service` | Indicates a service conversation. | whatsapp_webhooks_reference_messages_status_md |
| D157  | 会话类别 | `utility` | Indicates a utility conversation. | whatsapp_webhooks_reference_messages_status_md |
| D158  | 计费类别 | `authentication` | Indicates authentication rate applied. | whatsapp_webhooks_reference_messages_status_md |
| D159  | 计费类别 | `authentication-international` | Indicates authentication-international rate applied. | whatsapp_webhooks_reference_messages_status_md |
| D160  | 计费类别 | `group_marketing` | Indicates a group marketing template message. | whatsapp_webhooks_reference_messages_status_md |
| D161  | 计费类别 | `group_service` | Indicates a group non-template message. | whatsapp_webhooks_reference_messages_status_md |
| D162  | 计费类别 | `group_utility` | Indicates a group utility template message. | whatsapp_webhooks_reference_messages_status_md |
| D163  | 计费类别 | `marketing` | Indicates marketing rate applied. | whatsapp_webhooks_reference_messages_status_md |
| D164  | 计费类别 | `marketing_lite` | Indicates a Marketing Messages API for WhatsApp pricing applied. | whatsapp_webhooks_reference_messages_status_md |
| D165  | 计费类别 | `referral_conversion` | Indicates a free entry point conversation. | whatsapp_webhooks_reference_messages_status_md |
| D166  | 计费类别 | `service` | Indicates service rate applied. | whatsapp_webhooks_reference_messages_status_md |
| D167  | 计费类别 | `utility` | Indicates utility rate applied. | whatsapp_webhooks_reference_messages_status_md |
| D168  | 计费模型 | `CBP` | Indicates conversation-based pricing applies. Will only be set to this value if the webhook was sent before July 1, 2025. | whatsapp_webhooks_reference_messages_status_md |
| D169  | 计费模型 | `PMP` | Indicates per-message pricing applies. | whatsapp_webhooks_reference_messages_status_md |
| D170  | 计费子类型 | `paid_exempt` | Indicates the service message is free because the organization is eligible for this policy. | whatsapp_webhooks_reference_messages_status_md |
| D171  | 消息投递状态 | `delivered` | Indicates message was successfully delivered to the WhatsApp user&#039;s device. WhatsApp UI equivalent: Two checkmarks. | whatsapp_webhooks_reference_messages_status_md |
| D172  | 消息投递状态 | `failed` | Indicates failure to send or deliver the message to the WhatsApp user&#039;s device. WhatsApp UI equivalent: Red error triangle. | whatsapp_webhooks_reference_messages_status_md |
| D173  | 消息投递状态 | `played` | Indicates the first time a voice message is played by the WhatsApp user&#039;s device. WhatsApp UI equivalent: Blue microphone. | whatsapp_webhooks_reference_messages_status_md |
| D174  | 消息投递状态 | `read` | Indicates the message was displayed in an open chat thread in the WhatsApp user&#039;s device. WhatsApp UI equivalent: Two blue checkmarks. | whatsapp_webhooks_reference_messages_status_md |
| D175  | 消息投递状态 | `sent` | Indicates the message was successfully sent from our servers. WhatsApp UI equivalent: One checkmark. | whatsapp_webhooks_reference_messages_status_md |
| D176  | 引流消息媒体类型 | `image` | Indicates an image ad. | whatsapp_webhooks_reference_messages_sticker_md |
| D177  | 引流消息媒体类型 | `video` | Indicates a video ad. | whatsapp_webhooks_reference_messages_sticker_md |
| D178  | 引流消息媒体类型 | `image` | Indicates an image ad. | whatsapp_webhooks_reference_messages_text_md |
| D179  | 引流消息媒体类型 | `video` | Indicates a video ad. | whatsapp_webhooks_reference_messages_text_md |
| D180  | 不支持的消息类型 | `button` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D181  | 不支持的消息类型 | `edit` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D182  | 不支持的消息类型 | `errors` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D183  | 不支持的消息类型 | `gif` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D184  | 不支持的消息类型 | `group_invite` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D185  | 不支持的消息类型 | `hsm` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D186  | 不支持的消息类型 | `image` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D187  | 不支持的消息类型 | `interactive` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D188  | 不支持的消息类型 | `keep_in_chat` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D189  | 不支持的消息类型 | `link_preview` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D190  | 不支持的消息类型 | `list` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D191  | 不支持的消息类型 | `location` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D192  | 不支持的消息类型 | `media_placeholder` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D193  | 不支持的消息类型 | `order` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D194  | 不支持的消息类型 | `pin` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D195  | 不支持的消息类型 | `poll_creation` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D196  | 不支持的消息类型 | `poll_update` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D197  | 不支持的消息类型 | `product` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D198  | 不支持的消息类型 | `reaction` | — | whatsapp_webhooks_reference_messages_unsupported_md |
| D199  | 引流消息媒体类型 | `image` | Indicates an image ad. | whatsapp_webhooks_reference_messages_video_md |
| D200  | 引流消息媒体类型 | `video` | Indicates a video ad. | whatsapp_webhooks_reference_messages_video_md |
| D201  | 方案变更事件（回调位） | `SOLUTION_CREATED` | Indicates a new solution was saved as a draft or sent as a request to a partner. | whatsapp_webhooks_reference_partner_solutions_md |
| D202  | 方案变更事件（回调位） | `SOLUTION_UPDATED` | Indicates an existing solution has been updated. | whatsapp_webhooks_reference_partner_solutions_md |
| D203  | 多伙伴方案状态 | `ACTIVE` | The solution partner accepted the solution request and the solution can now be used. | whatsapp_webhooks_reference_partner_solutions_md |
| D204  | 多伙伴方案状态 | `DEACTIVATED` | The solution is deactivated. | whatsapp_webhooks_reference_partner_solutions_md |
| D205  | 多伙伴方案状态 | `DRAFT` | The solution is drafted but an invitation request has not been sent to a partner. | whatsapp_webhooks_reference_partner_solutions_md |
| D206  | 多伙伴方案状态 | `INITIATED` | The solution is created and the invitation request sent, but it has not been accepted or rejected yet. | whatsapp_webhooks_reference_partner_solutions_md |
| D207  | 多伙伴方案状态 | `PENDING_DEACTIVATION` | The solution owner requested deactivation of the solution but the solution partner has yet to accept or decline the deactivation request. | whatsapp_webhooks_reference_partner_solutions_md |
| D208  | 多伙伴方案状态 | `REJECTED` | The solution partner has rejected the solution request. | whatsapp_webhooks_reference_partner_solutions_md |
| D209  | 支付配置状态 | `Active` | Indicates the payment configuration has been tested in WhatsApp Manager and can now be used with Payments API. | whatsapp_webhooks_reference_payment_configuration_update_md |
| D210  | 支付配置状态 | `Needs Connecting` | Indicates the payment configuration has been disconnected from the payment gateway and needs to be connected again. | whatsapp_webhooks_reference_payment_configuration_update_md |
| D211  | 支付配置状态 | `Needs Testing` | Indicates the payment configuration has been connected to the payment gateway but still needs testing in WhatsApp Manager. | whatsapp_webhooks_reference_payment_configuration_update_md |
| D212  | 支付网关提供方 | `billdesk` | — | whatsapp_webhooks_reference_payment_configuration_update_md |
| D213  | 支付网关提供方 | `payu` | — | whatsapp_webhooks_reference_payment_configuration_update_md |
| D214  | 支付网关提供方 | `razorpay` | — | whatsapp_webhooks_reference_payment_configuration_update_md |
| D215  | 支付网关提供方 | `zaakpay` | — | whatsapp_webhooks_reference_payment_configuration_update_md |
| D216  | 显示名审核拒绝原因 | `NAME_EMPLOYEE_ISSUE` | Rejected because the display name included a person&#039;s name or employee identifier. | whatsapp_webhooks_reference_phone_number_name_update_md |
| D217  | 显示名审核拒绝原因 | `NAME_ENDCLIENT_NOTRELATED` | Rejected because the display name included an unrelated business&#039;s name. | whatsapp_webhooks_reference_phone_number_name_update_md |
| D218  | 显示名审核拒绝原因 | `NAME_FORMAT_UNACCEPTABLE` | Rejected because the display name used an unacceptable format. | whatsapp_webhooks_reference_phone_number_name_update_md |
| D219  | 显示名审核拒绝原因 | `NAME_INDIVIDUAL_ISSUE` | Rejected because the display name included a person&#039;s name or employee identifier. | whatsapp_webhooks_reference_phone_number_name_update_md |
| D220  | 显示名审核拒绝原因 | `NAME_NOT_CONSISTENT` | Rejected because the display name was not consistent with the business&#039;s branding. | whatsapp_webhooks_reference_phone_number_name_update_md |
| D221  | 显示名审核拒绝原因 | `null` | Indicates name was accepted. | whatsapp_webhooks_reference_phone_number_name_update_md |
| D222  | 显示名审核拒绝原因 | `UNKNOWN` | Rejected for an unknown reason. Contact support. | whatsapp_webhooks_reference_phone_number_name_update_md |
| D223  | 变更前后的当前限额档位 | `TIER_50` | Indicates a messaging limit of 50. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D224  | 变更前后的当前限额档位 | `TIER_250` | Indicates a messaging limit of 250. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D225  | 变更前后的当前限额档位 | `TIER_2K` | Indicates a messaging limit of 2,000. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D226  | 变更前后的当前限额档位 | `TIER_10K` | Indicates a messaging limit of 10,000. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D227  | 变更前后的当前限额档位 | `TIER_100K` | Indicates a messaging limit of 100,000. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D228  | 变更前后的当前限额档位 | `TIER_NOT_SET` | Indicates the business phone number has not been used to send a message yet. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D229  | 变更前后的当前限额档位 | `TIER_UNLIMITED` | Indicates the business phone number has higher throughput. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D230  | 号码质量事件 | `ONBOARDING` | Indicates the business phone number is still being registered. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D231  | 号码质量事件 | `THROUGHPUT_UPGRADE` | Indicates the business phone number&#039;s throughput level has increased to higher throughput. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D232  | 每日消息数上限档位 | `TIER_50` | Indicates a messaging limit of 50. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D233  | 每日消息数上限档位 | `TIER_250` | Indicates a messaging limit of 250. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D234  | 每日消息数上限档位 | `TIER_2K` | Indicates a messaging limit of 2,000. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D235  | 每日消息数上限档位 | `TIER_10K` | Indicates a messaging limit of 10,000. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D236  | 每日消息数上限档位 | `TIER_100K` | Indicates a messaging limit of 100,000. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D237  | 每日消息数上限档位 | `TIER_NOT_SET` | Indicates the business phone number has not been used to send a message yet. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D238  | 每日消息数上限档位 | `TIER_UNLIMITED` | Indicates the business phone number has higher throughput. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D239  | 变更前的限额档位 | `TIER_50` | Indicates a messaging limit of 50. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D240  | 变更前的限额档位 | `TIER_250` | Indicates a messaging limit of 250. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D241  | 变更前的限额档位 | `TIER_2K` | Indicates a messaging limit of 2,000. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D242  | 变更前的限额档位 | `TIER_10K` | Indicates a messaging limit of 10,000. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D243  | 变更前的限额档位 | `TIER_100K` | Indicates a messaging limit of 100,000. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D244  | 变更前的限额档位 | `TIER_NOT_SET` | Indicates the business phone number has not been used to send a message yet. | whatsapp_webhooks_reference_phone_number_quality_update_md |
| D245  | 安全设置事件 | `PIN_CHANGED` | indicates that a Meta Business Suite user changed or enabled the business phone number&#039;s PIN using WhatsApp Manager. | whatsapp_webhooks_reference_security_md |
| D246  | 安全设置事件 | `PIN_RESET_REQUEST` | indicates that a Meta Business Suite user clicked the Turn off two-step verification button in WhatsApp Manager. | whatsapp_webhooks_reference_security_md |
| D247  | 安全设置事件 | `PIN_REQUEST_SUCCESS` | indicates that a Meta Business Suite user completed the instructions in the WhatsApp Two-Step Verification Reset email to turn off two-step … | whatsapp_webhooks_reference_security_md |
| D248  | 通讯录同步动作 | `add` | Indicates the WhatsApp Business app user added or edited a contact. | whatsapp_webhooks_reference_smb_app_state_sync_md |
| D249  | 通讯录同步动作 | `remove` | Indicates the WhatsApp Business app user removed a contact. | whatsapp_webhooks_reference_smb_app_state_sync_md |
| D250  | 营销偏好开关 | `stop` | Indicates the WhatsApp user has opted to stop receiving marketing messages from you. | whatsapp_webhooks_reference_user_preferences_md |
| D251  | 营销偏好开关 | `resume` | Indicates the WhatsApp user has opted to resume receiving marketing messages from you. | whatsapp_webhooks_reference_user_preferences_md |
| D252  | 营销偏好描述全文 | `User requested to stop marketing messages` | — | whatsapp_webhooks_reference_user_preferences_md |
| D253  | 营销偏好描述全文 | `User requested to resume marketing messages` | — | whatsapp_webhooks_reference_user_preferences_md |
| D254  | Webhook 可订阅字段（概览页） | `account_alerts` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D255  | Webhook 可订阅字段（概览页） | `account_review_update` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D256  | Webhook 可订阅字段（概览页） | `account_update` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D257  | Webhook 可订阅字段（概览页） | `automatic_events` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D258  | Webhook 可订阅字段（概览页） | `business_capability_update` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D259  | Webhook 可订阅字段（概览页） | `calls` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D260  | Webhook 可订阅字段（概览页） | `history` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D261  | Webhook 可订阅字段（概览页） | `message_template_components_update` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D262  | Webhook 可订阅字段（概览页） | `message_template_quality_update` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D263  | Webhook 可订阅字段（概览页） | `message_template_status_update` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D264  | Webhook 可订阅字段（概览页） | `messages` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D265  | Webhook 可订阅字段（概览页） | `partner_solutions` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D266  | Webhook 可订阅字段（概览页） | `payment_configuration_update` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D267  | Webhook 可订阅字段（概览页） | `phone_number_name_update` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D268  | Webhook 可订阅字段（概览页） | `phone_number_quality_update` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D269  | Webhook 可订阅字段（概览页） | `security` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D270  | Webhook 可订阅字段（概览页） | `smb_app_state_sync` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D271  | Webhook 可订阅字段（概览页） | `smb_message_echoes` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D272  | Webhook 可订阅字段（概览页） | `template_category_update` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D273  | Webhook 可订阅字段（概览页） | `user_preferences` | 概览页字段表中登记的一个可订阅字段名。 | whatsapp_webhooks_overview_md |
| D274  | Webhook 覆盖支持字段（覆盖页） | `messages` | 该页「Supported webhook fields」列表内的一项，仅这些字段可按号码或消息账号改写回调地址。 | whatsapp_webhooks_override_md |
| D275  | Webhook 覆盖支持字段（覆盖页） | `message_echoes` | 该页「Supported webhook fields」列表内的一项，仅这些字段可按号码或消息账号改写回调地址。 | whatsapp_webhooks_override_md |
| D276  | Webhook 覆盖支持字段（覆盖页） | `calls` | 该页「Supported webhook fields」列表内的一项，仅这些字段可按号码或消息账号改写回调地址。 | whatsapp_webhooks_override_md |
| D277  | Webhook 覆盖支持字段（覆盖页） | `consumer_profile` | 该页「Supported webhook fields」列表内的一项，仅这些字段可按号码或消息账号改写回调地址。 | whatsapp_webhooks_override_md |
| D278  | Webhook 覆盖支持字段（覆盖页） | `messaging_handovers` | 该页「Supported webhook fields」列表内的一项，仅这些字段可按号码或消息账号改写回调地址。 | whatsapp_webhooks_override_md |
| D279  | Webhook 覆盖支持字段（覆盖页） | `standby` | 该页「Supported webhook fields」列表内的一项，仅这些字段可按号码或消息账号改写回调地址。 | whatsapp_webhooks_override_md |
| D280  | Webhook 覆盖支持字段（覆盖页） | `group_lifecycle_update` | 该页「Supported webhook fields」列表内的一项，仅这些字段可按号码或消息账号改写回调地址。 | whatsapp_webhooks_override_md |
| D281  | Webhook 覆盖支持字段（覆盖页） | `group_participants_update` | 该页「Supported webhook fields」列表内的一项，仅这些字段可按号码或消息账号改写回调地址。 | whatsapp_webhooks_override_md |
| D282  | Webhook 覆盖支持字段（覆盖页） | `group_settings_update` | 该页「Supported webhook fields」列表内的一项，仅这些字段可按号码或消息账号改写回调地址。 | whatsapp_webhooks_override_md |
| D283  | Webhook 覆盖支持字段（覆盖页） | `group_status_update` | 该页「Supported webhook fields」列表内的一项，仅这些字段可按号码或消息账号改写回调地址。 | whatsapp_webhooks_override_md |
| D284  | Webhook 覆盖支持字段（覆盖页） | `smb_message_echoes` | 该页「Supported webhook fields」列表内的一项，仅这些字段可按号码或消息账号改写回调地址。 | whatsapp_webhooks_override_md |
| D285  | Webhook 覆盖支持字段（覆盖页） | `smb_app_state_sync` | 该页「Supported webhook fields」列表内的一项，仅这些字段可按号码或消息账号改写回调地址。 | whatsapp_webhooks_override_md |
| D286  | Webhook 覆盖支持字段（覆盖页） | `history` | 该页「Supported webhook fields」列表内的一项，仅这些字段可按号码或消息账号改写回调地址。 | whatsapp_webhooks_override_md |
| D287  | Webhook 覆盖支持字段（覆盖页） | `account_settings_update` | 该页「Supported webhook fields」列表内的一项，仅这些字段可按号码或消息账号改写回调地址。 | whatsapp_webhooks_override_md |
| D288  | Webhook 覆盖不支持字段（覆盖页注记） | `message_template_status_update` | 该页注记点名「do not support callback overrides」的字段，恒送到应用默认回调地址。 | whatsapp_webhooks_override_md |
| D289  | Webhook 覆盖不支持字段（覆盖页注记） | `message_template_quality_update` | 该页注记点名「do not support callback overrides」的字段，恒送到应用默认回调地址。 | whatsapp_webhooks_override_md |
| D290  | Webhook 覆盖不支持字段（覆盖页注记） | `message_template_components_update` | 该页注记点名「do not support callback overrides」的字段，恒送到应用默认回调地址。 | whatsapp_webhooks_override_md |
| D291  | Webhook 覆盖不支持字段（覆盖页注记） | `template_category_update` | 该页注记点名「do not support callback overrides」的字段，恒送到应用默认回调地址。 | whatsapp_webhooks_override_md |
| D292  | Webhook 覆盖不支持字段（覆盖页注记） | `account_update` | 该页注记点名「do not support callback overrides」的字段，恒送到应用默认回调地址。 | whatsapp_webhooks_override_md |
| D293  | Webhook 覆盖不支持字段（覆盖页注记） | `account_review_update` | 该页注记点名「do not support callback overrides」的字段，恒送到应用默认回调地址。 | whatsapp_webhooks_override_md |
| D294  | Webhook 覆盖不支持字段（覆盖页注记） | `account_alerts` | 该页注记点名「do not support callback overrides」的字段，恒送到应用默认回调地址。 | whatsapp_webhooks_override_md |
| D295  | 平台接口所需权限（权限页） | `whatsapp_business_management` | 读消息账号元数据、模板管理、取关联号码、全部分析与账号变更类回调所需的权限。 | whatsapp_permissions_md |
| D296  | 平台接口所需权限（权限页） | `whatsapp_business_messaging` | 向 WhatsApp 用户发送任意类型消息、收消息与消息状态回调所需的权限。 | whatsapp_permissions_md |
| D297  | 平台接口所需权限（权限页） | `business_management` | 以编程方式访问商业资产组合时所需；正文注明该需求很少出现。 | whatsapp_permissions_md |
| D298  | 平台接口所需权限（权限页） | `whatsapp_business_manage_events` | 与转化 API 搭配发送营销模板做事件跟踪时才需要。 | whatsapp_permissions_md |
| D299  | 平台接口所需权限（权限页） | `ads_read` | 与洞察接口搭配取转化指标时才需要。 | whatsapp_permissions_md |
| D300  | 平台支持的访问令牌类型 | `System user access token` | 代表企业自身与内部自动化服务的长生命周期令牌，正文称 system tokens。 | whatsapp_access_tokens_md |
| D301  | 平台支持的访问令牌类型 | `Business integration system user access token` | 按已入驻客户逐一限定的令牌，正文称 business tokens，供 Tech Provider 与合作伙伴访问客户数据。 | whatsapp_access_tokens_md |
| D302  | 平台支持的访问令牌类型 | `User access token` | 受支持但通常只在首次发测试消息时使用，正文注明其过期很快。 | whatsapp_access_tokens_md |
| D303  | 系统用户角色 | `Admin` | 默认对名下与共享给本组合的全部消息账号、WhatsApp 账号及其资产拥有完全访问。 | whatsapp_access_tokens_md |
| D304  | 系统用户角色 | `Employee` | 必须逐个账号被授予业务资产访问权限后方可使用。 | whatsapp_access_tokens_md |
| D305  | 业务资产访问级别 | `Partial` | 可再按资产或动作收窄；正文举例可只给某部门查看模板与号码数据的权限。 | whatsapp_access_tokens_md |
| D306  | 业务资产访问级别 | `Full` | 对该账号的全部资产放行；缺少该访问时相关接口返回错误码 200。 | whatsapp_access_tokens_md |
| D307  | 令牌页划分的开发者身份 | `direct developer` | 只有本人或本企业访问自己数据者，令牌页指定使用系统用户访问令牌。 | whatsapp_access_tokens_md |
| D308  | 令牌页划分的开发者身份 | `Tech Provider` | 令牌页指定使用业务集成系统用户访问令牌。 | whatsapp_access_tokens_md |
| D309  | 令牌页划分的开发者身份 | `solution partner` | 共享授信额度用系统用户令牌，其余一律用业务集成系统用户令牌。 | whatsapp_access_tokens_md |
| D310  | 伙伴页划分的伙伴类型 | `Solution Partners` | 为其他企业提供全量平台服务并可延伸授信额度、直接开票的 Meta 商业合作伙伴。 | whatsapp_solution_providers_overview_md |
| D311  | 伙伴页划分的伙伴类型 | `Tech Providers` | 同样可提供全量服务但没有授信额度，客户须自备支付方式，由 Meta 直接计费。 | whatsapp_solution_providers_overview_md |
| D312  | 伙伴页划分的伙伴类型 | `Tech Partners` | 已具备或可申请成为 Meta 商业合作伙伴的 Tech Provider，因此可参加加速器计划。 | whatsapp_solution_providers_overview_md |
| D313  | Direct Send 类别滥用的处置阶梯档 | `Warning` | Meta 告知滥用，可申请模板复核以解决问题；正文标注持续存在。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D314  | Direct Send 类别滥用的处置阶梯档 | `Rate limiting` | 对消息账号施加临时限速上限，达上限后实用类发送以 131064 被拒；自动解除。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D315  | Direct Send 类别滥用的处置阶梯档 | `Messaging restriction` | 该消息账号不得用 Direct Send 发送实用类或认证类消息；7 天。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D316  | Direct Send 类别滥用的处置阶梯档 | `Extended restriction` | 持续滥用则延长限制；正文称这是永久撤销前的最后一次警告；30 天。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D317  | Direct Send 类别滥用的处置阶梯档 | `Permanent revocation` | Direct Send 访问被永久撤销；永久。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D318  | 被标记模板的复核生命周期状态 | `Available for review` | 已被判定为营销内容且处于可申请复核的窗口内。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D319  | 被标记模板的复核生命周期状态 | `In review` | 已提交申请、Meta 正在评估；正文称多数复核需要数天。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D320  | 被标记模板的复核生命周期状态 | `Reversed` | 复核通过，分类恢复为实用类，消息照常投递。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D321  | 被标记模板的复核生命周期状态 | `Unchanged` | 复核确认内容属营销，分类维持不变。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D322  | Direct Send 滥用的违规类型 | `DIRECT_SEND_UTILITY_CATEGORY_ABUSE_WARN` | 警告阶段的取值。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D323  | Direct Send 滥用的违规类型 | `DIRECT_SEND_UTILITY_TEMPLATE_ABUSE` | 警告阶段的取值。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D324  | Direct Send 滥用的违规类型 | `DIRECT_SEND_UTILITY_CATEGORY_ABUSE_RATE_LIMIT` | 处置阶段的取值。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D325  | Direct Send 滥用的违规类型 | `DIRECT_SEND_UTILITY_CATEGORY_ABUSE_STRIKE_1` | 处置阶段的取值。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D326  | Direct Send 滥用的违规类型 | `DIRECT_SEND_UTILITY_CATEGORY_ABUSE_STRIKE_2` | 处置阶段的取值。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D327  | Direct Send 滥用的违规类型 | `DIRECT_SEND_UTILITY_CATEGORY_ABUSE_OFFBOARD` | 处置阶段的取值。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D328  | Direct Send 滥用的违规类型 | `DIRECT_SEND_UTILITY_CATEGORY_ABUSE_UNBAN` | 处置阶段的取值。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D329  | Direct Send 滥用的违规类型 | `DIRECT_SEND_UTILITY_CATEGORY_ABUSE_RATE_LIMIT_RECOVERY` | 处置阶段的取值。 | whatsapp_direct_send_integrity_and_content_guidelines_md |
| D330  | 健康状态取值 | `AVAILABLE` | 该节点满足全部消息或通话要求。 | whatsapp_support_health_status_md |
| D331  | 健康状态取值 | `LIMITED` | 满足要求但存在限制，正文注明此时附带 additional info。 | whatsapp_support_health_status_md |
| D332  | 健康状态取值 | `BLOCKED` | 未满足一项或多项要求，正文注明此时附带 errors 属性说明原因与可能解法。 | whatsapp_support_health_status_md |
| D333  | 总体健康状态的聚合规则 | `BLOCKED` | 只要有一个节点被阻断，总体即取该值。 | whatsapp_support_health_status_md |
| D334  | 总体健康状态的聚合规则 | `LIMITED` | 没有节点被阻断、但存在受限节点时，总体取该值。 | whatsapp_support_health_status_md |
| D335  | 总体健康状态的聚合规则 | `AVAILABLE` | 全部节点均可用时，总体取该值。 | whatsapp_support_health_status_md |
| D336  | 新账号模型拆分出的账号类型 | `WhatsApp account (WAAC)` | 承载单一业务号码（未来还有单一商业用户名）的身份容器，始终由客户或直接开发者拥有并可与多方伙伴共享。 | whatsapp_account_model_evolution_md |
| D337  | 新账号模型拆分出的账号类型 | `Messaging account` | 承载模板、计费与回调订阅的容器，每个伙伴各得一个，实现模板与账单互相隔离。 | whatsapp_account_model_evolution_md |
| D338  | 账号模型迁移阶段 | `Phase 1 — General availability` | Meta 自动完成账号迁移，现有 ID 与调用继续可用；正文标注按批次灰度。 | whatsapp_account_model_evolution_md |
| D339  | 账号模型迁移阶段 | `Phase 2 — New Graph API version` | 新增账号级接口与新的 whatsapp_account 回调主题，权限拆分为两套作用域。 | whatsapp_account_model_evolution_md |
| D340  | 账号模型迁移阶段 | `Phase 3 — Mandatory transition` | 所有在路径或参数上指向号码 ID 的接口必须改指向 WAAC ID。 | whatsapp_account_model_evolution_md |
| D341  | 伙伴页列出的客户入驻方式 | `Embedded Signup` | 从自建网站或客户门户启动的认证与授权界面，成功后自动生成所需资产并授权本应用访问。 | whatsapp_solution_providers_overview_md |
| D342  | 伙伴页列出的客户入驻方式 | `Onboard WhatsApp Business app users` | 让已用商业应用的客户把现有账号与号码接入云端接口，规模化发送同时保留一对一会话。 | whatsapp_solution_providers_overview_md |
| D343  | 伙伴页列出的客户入驻方式 | `Hosted Embedded Signup` | 由 Meta 托管的替代实现，正文注明无需自行配置与托管实现代码。 | whatsapp_solution_providers_overview_md |
| D344  | 伙伴页列出的客户入驻方式 | `Partner-initiated account creation` | 仅 Solution Partner 可用：代为发起账号创建，由客户在商业套件里批准或拒绝。 | whatsapp_solution_providers_overview_md |
| D345  | 伙伴页列出的客户入驻方式 | `Meta Business Suite` | 客户自建账号后再与本伙伴共享，正文未把它列为接口化路径。 | whatsapp_solution_providers_overview_md |
| D346  | 号码注册的四个连续动作 | `Create the number on the WhatsApp account hierarchy associated with a Messaging account.` | Create the number on the WhatsApp account hierarchy associated with a Messaging account. | whatsapp_solution_providers_registering_phone_numbers_md |
| D347  | 号码注册的四个连续动作 | `Get a verification code for that number.` | Get a verification code for that number. | whatsapp_solution_providers_registering_phone_numbers_md |
| D348  | 号码注册的四个连续动作 | `Use the code to verify the number.` | Use the code to verify the number. | whatsapp_solution_providers_registering_phone_numbers_md |
| D349  | 号码注册的四个连续动作 | `Register the verified number for API use.` | Register the verified number for API use. | whatsapp_solution_providers_registering_phone_numbers_md |
| **合计** | **73** 组 | **349** 个取值 | 解析 55 组（253 个取值，另 55 处同名重复计组）／手定组 18 组 | **36** 页 |

#ROWS=349 COLS=5

# 附表 E 三类伙伴逐项对照（13 行）

本表按伙伴页的三分类逐维度并排：前 **7** 行由脚本从伙伴页「Comparison」对照表直接解析（维度名取不到或列数不是四即中止），其余行的每个单元格都是所引页面的逐字原句或对该原句的直译，核验不过即记入失败清单；标作「官方未按类别区分」的 **3** 格是该页面没有按类别写差异，不代表该类别没有该项规则。

| 编号 | 对照维度 | Solution Partners | Tech Providers | Tech Partners |
| --- | --- | --- | --- | --- |
| E1   | Can offer full WhatsApp Business Platform services to onboarded clients | 是（官方对照表原格 Yes） | 是（官方对照表原格 Yes） | 是（官方对照表原格 Yes） |
| E2   | Has a credit line | 是（官方对照表原格 Yes） | 否（官方对照表原格 No） | 否（官方对照表原格 No） |
| E3   | Customers bypass payment method collection | 是（官方对照表原格 Yes） | 否（官方对照表原格 No） | 否（官方对照表原格 No） |
| E4   | Bills customers directly for API usage (vs. Meta billing customers for usage) | 是（官方对照表原格 Yes） | 否（官方对照表原格 No） | 否（官方对照表原格 No） |
| E5   | Is a Meta Business Partner | 是（官方对照表原格 Yes） | 否（官方对照表原格 No） | 是（官方对照表原格 Yes） |
| E6   | Eligible for accelerator program | 是（官方对照表原格 Yes） | 否（官方对照表原格 No） | 是（官方对照表原格 Yes） |
| E7   | Access to Direct Support | 是（官方对照表原格 Yes） | 是（官方对照表原格 Yes） | 是（官方对照表原格 Yes） |
| E8   | 应使用的访问令牌类型 | 系统用户访问令牌＋业务集成系统用户访问令牌（令牌页：共享授信额度用前者，其余用后者） | 业务集成系统用户访问令牌 | 官方未按类别区分 |
| E9   | 是否必须走应用复核 | 官方未按类别区分 | 客户使用本应用访问其数据时必须复核 | 官方未按类别区分 |
| E10  | 授信额度与代客开票 | 有额度、可直接向客户开票 | 无额度，客户自备支付方式、由 Meta 计费 | 无额度 |
| E11  | Direct Support 通道 | 可用 | 可用 | 可用（官方对照表三格同为 Yes） |
| E12  | 升级路径与门槛 | 官方未按类别区分 | 达到日均消息量或通话量与客户数门槛后升级为技术合作伙伴 | 可参加加速器计划（官方对照表原格 Yes） |
| **合计** | **12** 个维度 | 解析 7 行／手定 5 行 | 未按类别区分 3 格 | 伙伴页对照表原表 |

#ROWS=12 COLS=5

# 附表 F 口径冲突与官方未写明处（20 行）

本表登记同一议题在不同官方页面（或同页两处）之间的措辞差异：两条口径都逐字核验后再并列，「本篇采用」列说明本文按哪一条叙述、另一条如何使用。共 **19** 项，其中 **6** 项一侧为官方未写明。

| 编号 | 议题 | 口径甲 | 口径乙 | 差异性质 | 本篇采用 |
| --- | --- | --- | --- | --- | --- |
| F1   | 同一属性名 <STATUS> 的取值个数 | 「核验提交流水状态」在账号更新页取 5 个值（APPROVED／DISCARDED／FAILED／PENDING／REVOKED） | 「伙伴主导核验结论」在核验页只取 2 个值（APPROVED／FAILED） | 同名不同域 | 两处按字段所在页分列附表 D，正文不合并也不互为注脚 |
| F2   | 同一属性名 <SOLUTION_STATUS> 的取值个数 | 正文页给 7 个值，含 PENDING | 回调参考页给 6 个值，无 PENDING | 同域不同长度 | 正文按状态机叙述 4 个可停留态，附表 D 两组都保留并标注差异 |
| F3   | 可订阅字段与可覆盖字段的清单 | 概览页字段表登记 20 个字段 | 覆盖页只列 14 个字段支持改写回调地址，其中 9 个未出现在概览页字段表里 | 层级不同 | 正文区分「可订阅」与「可覆盖」两根轴并分别计数，不取并集当字段总数 |
| F4   | 同一枚 200 的两种含义 | 接口错误码 200：令牌所标识用户无权访问该资源 | HTTP 状态码 200：回调端点验证与投递确认的应答 | 同名不同义 | 正文每次出现都写明是哪一个，官方本身也用括注提示不要混淆 |
| F5   | 失败回调重投窗口的措辞 | 概览页：Meta 以递减频率重投直到成功，最长 7 天 | 端点创建页：先立即重投，再以递减频率重投若干次；吞吐页额外写出「指数退避」 | 同页外三处措辞不一 | 正文取三句共同部分（递减频率、7 天窗口）叙述，退避算法只作为吞吐页的单独口径标注 |
| F6   | 限额归属层级 | 吞吐页把号码级限额说成消息账号的 rate limit 与模板消息限额 | 账号模型页写明限额留在商业资产组合层级、号码共享时限额由所有伙伴共用 | 对象层级不同 | 正文严格区分「号码级吞吐」「组合级限额」两个层级，附表 B 分档登记 |
| F7   | 新账号在条款里的名称 | 文档页称 WhatsApp account（WAAC） | 同一文档页注明服务条款里把它写作 WhatsApp Business Account | 术语跨文本不一致 | 正文用文档术语，引用条款原文时保留条款写法并加说明 |
| F8   | 双因素口令的现行状态 | 账号模型页：Phase 1 起对符合条件的号码停用双因素口令 | 安全事件回调页：仍以口令变更与口令重置请求为触发事件 | 迁移期并存 | 正文按「停用中但回调仍在」叙述，两者不互相推翻 |
| F9   | 档位字段的两套命名 | 能力升级回调把上限写成 250／2000／10000／100000／-1 的数值档 | 号码质量回调把同一件事写成 TIER_250／TIER_2K／TIER_10K／TIER_100K／TIER_UNLIMITED／TIER_NOT_SET | 数值制与档名制并存 | 正文两套都保留并标明字段来源，附表 D 分两组登记 |
| F10  | 消息账号标识参数的正式名与别名 | 账号模型页：新参数名为 messaging_account_id | 同页注明 paid_messaging_account_id 作为已弃用但仍受支持的反向兼容别名 | 主名与别名并存 | 正文只叙述主名，别名一次性列出 |
| F11  | 限速档位与暂停标题的对应 | 处置阶梯页：限速是账号级的一档处置 | 模板状态回调页另有独立的 LIMIT_EXCEEDED 事件与 RATE_LIMITING_PAUSE 标题 | 同一措辞分属两层 | 正文把账号级限速与模板级暂停分别叙述，不合并为单一阶梯 |
| F12  | 官方未写明：类别误用的判定阈值 | 处置阶梯页只写「持续滥用」与「回落阈值以下」 | 两页都没有给出被判定为滥用所需的比率或绝对量 | 官方未写明 | 正文不给经验值，只说明解除条件由官方侧比率决定 |
| F13  | 官方未写明：吞吐升档资格的复核频率 | 资格页列出三项条件 | 未写资格每隔多久复核一次、也未写失去资格后是否降档 | 官方未写明 | 正文只叙述升档方向，不推断降档路径 |
| F14  | 官方未写明：覆盖式回调地址的验证失败后果 | 覆盖页写明如何设置、读取、删除备用地址 | 未写备用地址验证不通过时的降级投递行为 | 官方未写明 | 正文按官方三句阶梯叙述，不补默认回落之外的规则 |
| F15  | 官方未写明：机器人身份在平台条款里的定性 | 帮助中心页写明产品不适用于批量或自动化消息，且两者始终违反服务条款 | 同页未给出「自动化」的判定标准，也未区分经官方接口接入的自动化与未授权接入 | 官方未写明 | 正文引用该页原文定性，另外单列官方给出的两个受支持的商业消息产品，不做推断 |
| F16  | 官方未写明：站外证据的判定边界 | 帮助中心页把站外证据举例为公开宣称能用 WhatsApp 做违规用途的能力 | 未写站外证据的清单、举证方式与申诉路径 | 官方未写明 | 正文只转述该页给出的定义与起始日期，不推测执法流程 |
| F17  | 同一词 Draft 在两类提交里的所指 | 应用复核页：Draft 指**应用复核提交**本身处于草稿态，原文要求显式提交才会被审核 | 多伙伴方案页：Draft 指**方案已保存但尚未发给伙伴**，该状态下客户走嵌入式注册会看到不可用于入驻的错误 | 同名不同义 | 附表 C 的多伙伴状态机行只引方案页原句；复核页那句另在正文复核流程处引用，两处不互为注脚 |
| F18  | 「不向个人授予官方商业账号」这句话的出处 | 官方商业账号专页用五项资格条件描述申请主体，五项都以 the business 起句，全页没有 individuals 一词 | 该句只出现在账号告警回调 ALERT_DESCRIPTION 字段的示例文案枚举里 | 结论句不在专页 | 附表 C 该行按回调页登记出处，正文不把它写成专页原句 |
| F19  | 官方未写明：本批正文未取回的文档位 | 本篇登记的 111 个页面位中有 5 页只返回单页应用外壳 | 其正文由脚本延迟加载，本次抓取未取回 | 官方未写明（抓取侧留白） | 全篇不引用未取回页，附表 A 逐页标注状态 |
| **合计** | **19** 项 | 口径甲乙均逐字核验 | 差异性质共 14 类 | 官方未写明 6 项 | 冲突不合并、留白不推断 |

#ROWS=19 COLS=6

---

**边界声明：** 本篇是一份**官方接口文档与官方规则文本的口径梳理**，不是操作手册，也不是对平台规则合理性的评价。全篇内容限于 WhatsApp 与 Meta 公开页面已经写出的表述：Access Tokens Guide 与 WhatsApp permissions、In-App Signup、账号模型演进三页（Updates to WhatsApp Business accounts／Managing messaging accounts／Onboarding changes）、solution-providers 全组 30 页（伙伴对照、应用复核、伙伴主导商业核验、多伙伴方案、嵌入式注册、号码与 WABA 在伙伴之间迁移的 7 页）、webhooks 全组 40 页（overview／create webhook endpoint／override／security／自动事件／24 个事件参考页与 19 个 messages 子类型页）、吞吐与健康与载入测试与实验位与输入指示器 6 页、Direct Send 侧 13 页，加上 4 条法律政策页与 3 条帮助中心页。引用这些文本的目的只有两个：说明平台在公开文档里把**谁能建应用、谁的名字能出现在客户账单上、令牌由谁签发与撤销、号码与消息账号与业务组合谁拥有谁、哪些事件在哪些字段上以哪些取值被通知**写成了哪些字段与哪些状态；说明同一批文档在条款侧把**批量消息与自动化**定性成什么、从什么时候起可以采取什么行动。本篇**不含**批量发送脚本、发送队列、并发与重试参数调优、账号池与多号轮换、设备农场、注册与验证流程的操作步骤、限速或频控规避、垃圾信息检测绕过、风控与反检测绕过、签名号的抓取或伪造、非官方客户端、封号规避与申诉话术模板、模板内容代写与规避类别判定的写法建议；**不含**任何可用于向非自愿收件人大量投递消息的方法；**不含**利用代码、恶意样本或利用链构造方式；**不含**任何针对他人设备或他人账号的操作步骤与时序。凡官方写成区间（`2—4` 周的模板爬升期、事件回调字段 `1—16` 个取值）、模糊量（`may take up to 3 minutes`、`about 24 hours`、`within 6 hours`）、同一判据在两个时点各写一数（升档时长在改口径前后分别为 `24 hours` 与 `6 hours`）、或同一数值在两个体系里各有一处（`24` 小时同时是客服窗口、复核平均时长、工单首次响应与吞吐资格的移动窗口）之处，本篇按体系分列登记，不推测阈值、不给出单值、不以其他平台惯例补齐。三类伙伴之间、三种令牌身份之间、号码与消息账号与业务组合三层容器之间、可订阅字段与可覆盖字段两根轴之间，官方从未给出对齐表，本篇不做出任何跨体系的等价推断。载入测试页、嵌入式注册页与复核提交页在本篇只登记**名称、数量与条件分支的存在**，不重述任何一步怎么做。本次未能取回正文的 5 个页面在附表 A 逐行标注「不引用」，其内容全篇未被引用。

Telegram: https://t.me/one00190
