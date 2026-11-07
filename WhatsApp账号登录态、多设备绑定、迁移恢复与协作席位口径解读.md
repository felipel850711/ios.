@@TITLE@@WhatsApp 账号登录态、多设备绑定、迁移恢复与协作席位口径解读
@@SUBTITLE@@本篇是上一篇《WhatsApp 群发功能的载体、规模上限、可见性与退订机制口径解读》的**同规矩、不同轴**续篇：那篇数的是"一条消息能同时到达多少人"，这篇数的是"一个账号能同时被几个终端持有、这些持有关系各自多久失效、谁能撤销它、撤销之后什么东西跟着消失"。全篇只读官方页面原文，共引用 **81** 页：**40** 篇帮助中心文章＋ **6** 份法律与政策文本＋ **18** 页 Meta 开发者与商务管理文档＋ **7** 页 Apple 开发者与支持文档＋ **10** 页 Google 与 Play 文档（其中 **5** 页本次正文未取回，全篇不引用其内容，已取回 **76** 页）。各层的公开数字是：**已链接设备 4 台**（Business 应用页面写作"4 台设备与 1 部手机"）、multi-agent 开通后 **10 台**、Meta Verified／One 订阅 **4／6／8／10** 四档、主设备上可同时登出 **2** 个账号、桌面端群通话 **32** 人、已链接设备闲置 **30 天**自动断开、主手机 **unused 14 天**后已链接设备被登出、忘记两步验证口令等 **7 天**、端到端备份密钥现行写法 **64 位**、新口令至少 **8** 个字符、免费账号云备份 **2 GB**、Google 侧未更新备份约 **5 个月**后删除、Apple 侧关闭设备备份后旧备份保留 **180 天**、商业平台消息限额 **250／2,000／10,000／100,000／不限** 五档、号码配额 **2→20**、模板 **250→6,000**、吞吐 **80→1,000 mps**、企业侧处罚 **1 或 3 天→5／7／30 天→无限期→永久**。而"临时封禁多久""是否存在一键登出全部已链接设备的入口""单个账号能并发多少路 API 会话""system user 的数量上限"这四件事**官方页面没有写**，连同另外二十四项一起列在附表 E（共 **28** 项）。三条最容易读错的结论先说：**第一，"最多四台"这句被引用时通常丢了主语——官方绑的是"链接到你的主手机"，且同页写明注册与新增链接都需要主手机在场**；**第二，14 天与 30 天不是同一条规则的两半，两条机制各绑一个主语，而"14 天"这个数字在四个页面上其实指三件不同的事**；**第三，官方文本里的批量登出只覆盖 WhatsApp Web 会话，其它设备要逐台操作；真正"收回全部访问"的官方主路径是用同一号码重新注册，而官方明确否认了另一件事——改口令不会把别人移出去**。本篇不提供多账号轮换、设备农场、账号池、封号规避、限速规避或反垃圾检测绕过的任何操作性内容。
@@INFO@@报告日期|2026 年 11 月 7 日
@@INFO@@分析对象|WhatsApp 官方帮助中心（`faq.whatsapp.com`，逐篇以文章 ID 作为 URL 引用，共 **40** 篇，覆盖已链接设备、登录与注册、两步验证、更换号码与迁移、备份与恢复、账号安全与被盗、封禁与非官方应用七个主题，主题之间有交叉故不再分档计数，其中 **3** 篇正文本次未取回）、WhatsApp 法律与政策文本（**6** 页：服务条款 Effective January 4, 2021，与 2026 年 9 月 23 日同日生效的三份新协议——WhatsApp Terms for WhatsApp Business Platform、Meta Terms for WhatsApp Business Platform、WhatsApp Terms for WhatsApp Business App——外加商业消息政策现行页与消息指南）、Meta 开发者与商务管理文档（`developers.facebook.com/docs/whatsapp/` 与 business.whatsapp.com 下 **18** 页：whatsapp-business-accounts、phone-numbers、messaging-limits、throughput、about-the-platform、access-tokens、embedded-signup/overview、onboarding-business-app-users、send-messages、getting-opt-in、policy-enforcement、error codes 等，其中 **2** 页未取回）、Apple 开发者与支持文档 **7** 页（iCloud 备份内容与高级数据保护、账户恢复、通行密钥、设备登录会话管理）、Google 开发者文档与 Play／支持文档 **10** 页（Auto Backup 规格、设备到设备迁移、Android 兼容性定义文档 CDD §9.16、CE 存储、Google 账户按设备登出会话、远程抹除后重新使用设备）。五组相加 **81** 页，其中 **5** 页本次未取回正文，全篇不引用其内容。对照口径：本篇把"登录态"拆成**八种**官方用词各异的对象（附表 B），把"上限"拆成**五档**含义（附表 C），并把四个来源层各自能撤销什么逐章标注；凡官方页面没有统一术语的，本篇不发明术语。
@@INFO@@视角定位|功能与政策口径的解读，属于**账号对象模型与平台规则的公开文本梳理**，不是操作指南。本篇**不提供**多账号轮换、设备农场、账号池、会话维持或转移的搭建方法与操作时序；**不提供**封号规避、限速规避、反垃圾检测绕过或申诉话术模板；**不提供**任何用于维持、恢复、接管他人设备上会话的步骤——第 5 章与第 12 章的全部内容都是**账号持有者对自己账号**可核对的官方路径，第 11 章的全部内容都是系统厂商文档里已经公开的规定。文中所有数字、时限、错误码与措辞均取自官方公开页面，引用目的只有两个：**（1）让读者知道平台在规则层已经把哪些行为定义为违规、哪些能力可以被单独收回；（2）让读者知道用户手上有哪些官方给出的撤销访问的路径，以及这些路径各自覆盖到什么范围。** 凡引号内英文均为**官方页面原文**，出处紧跟其后；官方页面未写明的内容一律标注"官方未文档化"并列入附表 E，不做推测补齐。本篇亦不构成对 WhatsApp、Meta、Apple 或 Google 任何产品安全性、加密强度或政策执行一致性的评价。
@@INFO@@分类|**官方页面引用清单 81 页 × 分组 × 支撑章节 × 取回状态**（附表 A，**82** 行含合计；分组为帮助中心 40、法律与政策 6、Meta 开发者与商务 18、Apple 7、Google 10，**未取回 5 页**，另有一条取回方法口径说明）**/ 八种"会话／登录态"对象 × 官方用词 × 授予方 × 撤销方 × 时长**（附表 B，**9** 行含合计；八种对象分属**四套**撤销体系：设备清单、订阅权益、身份口令、平台授权）**/ 数字归档五档 × 86 个条目 × 64 个去重数字**（附表 C，**87** 行含合计；档位：**硬数量上限 25／时间窗口与时长 31／默认可改 7／条件触发 11／分档晋级 12**；25＋31＋7＋11＋12＝86 与条目数闭合，**13** 个数字因跨轴各占 2–5 行，其中 30 天占 5 行、14 天占 4 行、单独的 2 与 4 各占 4 行）**/ 称谓、字段、错误码、文案与处置枚举**（附表 D，**26** 行含合计，六组 5／5／4／4／4／3：称谓 5、字段 5、错误码 4、文案 4、处置 4、标记 3；其中称谓组的五项差异是本次最容易读错的地方，另见附表 F 第一组）**/ 官方未文档化清单 28 项**（附表 E，**29** 行含合计，四类 **6／11／5／6**：规模与可见性 6、规则与判据 11、读法与措辞 5、本次读取失败 6）**/ 页面之间口径冲突 10 组**（附表 F，**11** 行含合计，性质依次为：主语不同、枚举不闭合、用词不同、版本并存、版本变更、量纲不同、层级不同、精度不同、作用轴不同、范围不同——**十组里没有一组是"数字算错"**）
@@INFO@@难度|入门—进阶。本篇的难点有三层。**第一层是同一个数字绑不同主语**：`4` 在附表 C 占 **4** 行（个人账号可链接台数、未开通 multi-agent 时的台数、超过该台数后只有主设备接到来电、取消订阅后仅保留最近活跃的四台）；`14 天` 占 **4** 行却只指**三件**事；`30 天` 占 **5** 行（已链接设备闲置断开、未送达消息服务端存储、忘记口令时按距上次登录是否超过 30 天分档、进入 2,000 档的投递量统计窗口、沙箱账号有效期）；单独的 `2` 与 `4` 各占 4 行，`2,000`、`250`、`100,000`、`8`、`20`、`6`、`7 天`、`24 小时`、`90 天` 各占 2 行。凡引用数字必须同时报"数的是台、天、小时、次、档位还是角色数"。**第二层是"额度"与"权利"的差别**：4 台是并发口径（at a time）而不是累计口径，multi-agent 与 Meta Verified 把这条线挪到了**订阅档位**上，而 `1217634902127718` 一句 "including the ability to link devices" 说明这个能力可以被单独收回——三者不在同一根轴上。**第三层是产品线文本与合同文本、系统文档的三层分离**：强制开启两步验证写在帮助中心而不在条款里；备份删除时点、迁移留痕要求与"按设备登出全部会话"由 Apple／Google 规定而 WhatsApp 页面没有；而条款 (h) 只允许"通过 WhatsApp 明确提供的工具"做多设备访问。把这三层任意一层当成另一层，是这类阅读最常见的错误来源。
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

"我的 WhatsApp 账号在几个地方登录着"——这个问题看起来只需要一个数字，实际要拆成四件事才能回答：**有几台设备被允许同时在线、这些在线状态各自多久失效一次、谁能把它们撤销、以及撤销之后什么东西会跟着消失**。

官方文本对这四件事的回答分散在**四个不同层级**：帮助中心回答设备数量，服务条款与三份新协议回答"多设备访问"是否被允许，商业平台开发者文档回答席位、配额与令牌，Apple 与 Google 的文档回答备份、迁移与会话撤销——而**这四层之间没有任何一页做过对齐**。所以本篇的工作不是给出一个"最多几台"的答案，而是把四层文本各自说了什么、以什么单位说、以及**在哪里根本没说话**逐条摆开。

本篇是功能与政策口径的解读，**不属于 iOS 措辞计量系列，也不给编号**。它与前一篇（群发功能的载体与上限）共用一套引用规矩，但主题完全不同：那篇数的是**一条消息能到达多少人**，这篇数的是**一个账号能同时被几个终端持有**。

十个问题，以及各自的答案落在哪一章：

1. 一个账号同一时刻能在几台设备上在线？——帮助中心写 **4 台已链接设备**，Business 应用那页写成"4 台设备**与** 1 部手机"；multi-agent 与 Meta Verified／One 把这条线挪到 **4／6／8／10 四档**（第 2 章，附表 B、C）。
2. "14 天"和"30 天"是同一条规则吗？——不是：**14 天绑主手机"未被使用"，30 天绑那台已链接设备自身的闲置**；而"14 天"这个数在四个页面上其实指**三件**不同的事（第 3 章）。
3. 已链接设备上什么事情做不了？——官方逐页列了一份不可用清单，并在群通话上给了一个具体数字 **32 人**；历史同步的三句原话给出**三个不同范围**（第 4 章）。
4. 怀疑账号被别人访问时，官方给的第一动作是什么？——审查设备清单并登出，但**批量登出只覆盖 WhatsApp Web 会话**；真正收回全部访问的主路径是**用同一号码重新注册**，而官方明确写了**改口令不会把别人移出去**（第 5 章）。
5. 两步验证到底是几位？——**这个问题目前没有唯一正确答案**：官方一页写"把 6 位 PIN 升级为 password"，另一页给"至少 **8** 个字符＋1 字母＋1 数字"，同一页还保留 6 位邮箱码（第 6 章）。
6. 换号与换机时，什么东西跟号走、什么跟设备走、什么跟云账号走？——第 7.5 节把散在七页的表述压成一张三分表；跨端迁移**两个方向不对称，唯一的不对称项是通话记录**。
7. 备份由谁保管、什么时候消失？——**三种存储、两个管理方**；端到端保护物的现行写法是 **64 位**密钥；Google 侧约 **5 个月**未更新即删，Apple 侧关闭设备备份后旧备份保留 **180 天**（第 8、11 章）。
8. 政策层怎么写"多设备访问"与"自动化"？——服务条款只允许**通过 WhatsApp 明确提供的工具**做多设备；2026 年 **9 月 23 日**三份新协议同日生效；处罚阶梯第一级只有 **1 或 3 天**（第 9、12 章）。
9. 企业协作层把"人"建模成了什么？——两种角色的 system user、两类访问令牌、四条互不相干的配额轴（**2→20 号码、250→6,000 模板、5 与 2 个方案商、10→200→合作伙伴**）（第 10 章）。
10. iOS 与 Android 有区别吗？——**帮助中心的上限数字不分端**；差异全部在系统能力层，共五处（第 11 章）。

三条最容易读错的结论先说：

**第一，"四台"这句被引用时通常丢掉了它的主语。** 官方原句绑的是"链接到你的**主手机**"，同页另有一句"你仍然需要主手机来注册账号和链接新设备"——所以这四台是**主手机之外**的四台，且额度能否使用取决于主手机是否在场。

**第二，同一个"14 天"在本篇出现的四处里只有一处是"断开"规则。** 另外三处分别指：需要多久在主手机上登录一次、主设备登出后已链接设备还能连多久、以及副手机在主手机未被使用后的登出期。**四个 14 天指三件事，不要把其中任何两处理解成同一条**（第 3.2 节的表就是为这一条准备的）。

**第三，备份与迁移是两条通道，而这条通道不由 WhatsApp 规定。** Android 侧 `android:allowBackup="false"` 关掉的是**云备份**，机到机的数据迁移走的是系统另一套规则（原句见第 11.3 节）；Google 的兼容性定义文档甚至**要求**迁移过程留下审计线索，而 WhatsApp 官方页面明确写它**不提供**"谁访问过这个账号、何时、在哪里"的记录（第 4.4 节原句，第 5.7 节归纳）。**系统层给了留痕，应用层拒绝回答**——这是本篇在第 11 章与第 5 章之间反复出现的一处对照。

本篇不涉及任何多账号轮换、设备农场、账号池、封号规避或反垃圾检测绕过内容；不提供任何可用于维持他人设备上会话的操作步骤。凡引号内英文均为**官方页面原文**，出处紧跟其后；官方页面没有写的，一律进附表 E 留空。

# 1 口径、来源层，与"上限"这个词的五种含义

## 1.1 四类来源层，与四个身份锚点

本篇只用官方来源，分四层。

**第一层是消费者帮助中心**（`faq.whatsapp.com`），共引用 **40** 篇。这一层的数字面向所有用户，且**不按 iOS 与 Android 分别给数**——但"不分端"不等于"不分身份"：Business 应用的功能在这一层另有一批专门文章，本篇逐章标注属于哪一套身份。引用方式是文章 ID：URL 形如 `https://faq.whatsapp.com/<文章 ID>`，同一主题常有多个 ID，本篇在引用时把用到的 ID 全部列出。

**第二层是法律与政策文本**，共 **6** 页：服务条款、三份 2026 年 9 月 23 日生效的新协议、商业消息政策与消息指南。这一层不写"几台设备"，它写**允不允许**——所以它的可数对象是条款项数与生效日期，不是数量上限。

**第三层是 Meta 的开发者与商务管理文档**，共 **18** 页。这一类的读者是企业与其服务商，措辞接近规格书：有定义句、有档位表、有错误码、有生效日期。本篇用到的席位、令牌、号码与模板配额、吞吐与请求速率全部来自这一层。

**第四层是 Apple 与 Google 的开发者与帮助文档**，共 **17** 页（Apple **7**、Google 与 Play **10**）。这一层的规定**不是 WhatsApp 的规定**，但决定了 WhatsApp 在两端各自能做到什么——备份里含什么、迁移要求什么、会话撤销按什么粒度做，都在这一层。

四层相加 **81** 页，其中 **5** 页本次未取回正文，全篇不引用其内容（附表 A 逐行标注）。

在四层来源之上，本篇区分**四个身份锚点**——同一个人可以同时持有它们，而每一个锚点决定一类登录态能活多久：

| 身份锚点 | 谁持有它 | 它决定的登录态 | 主要章节 |
|---|---|---|---|
| 手机号码（SIM 短信验证码） | 电信运营商＋机主 | 主设备注册态；重新注册即可顶替所有终端 | 2、5、7 |
| 设备（主手机／已链接设备） | 用户 | 已链接设备会话、Web 会话、副手机会话 | 2、3、4、11 |
| Meta／商务管理平台账号 | 企业与它的 system users | 访问令牌、portfolio 级限额与配额 | 6.6、6.7、10 |
| 系统账号（Apple Account／Google Account） | 用户与系统厂商 | 备份可得性、机到机迁移、按设备撤销全部会话 | 8、11 |

这张表是本篇最重要的一张，因为**四个锚点各自有自己的撤销路径，跨锚点不通用**：换一个 Meta 密码不会影响已链接设备，登出某台设备也不会让备份消失，而"用同一号码重新注册"这一步同时会登出所有终端——最后一句在 `506595211487528` 的原话是 "anyone who might be using your account is automatically logged out"。第 6.7 节还有一条同方向的观察：商业平台那层的登录态锚在 Facebook 个人身份上，与 WhatsApp 账号锚在 SIM 卡上，**两套身份体系在同一条业务流程里串联**，任何一处失效都会表现为"业务侧无法操作"，但登出路径完全不同。

## 1.2 引用规范

四条规矩，全篇一致执行。

**第一，数字必带出处。** 每一个上限、窗口、档位、日期后面都跟着它的页面 ID 或文档路径。没有出处的数字本篇不写。

**第二，官方没写的就说没写。** 本篇有 **28** 项"官方未文档化"结论（附表 E，四类 6／11／5／6），写法一律是"某某页面只写了 A 与 B，没有写 C"，不用第三方统计、媒体报道或逆向结论补缺口。

**第三，页面自带日期时，日期一并抄下来。** 服务条款页标 Effective January 4, 2021；三份新协议标 2026 年 9 月 23 日；开发者文档页多数带 Updated 日期。**需要说明的是：帮助中心页面在本次逐页核验中全部不显示"Last updated"字段**，因此附表 A 该组的日期列留空是口径，不是疏漏（附表 E 第 19 项记录了这件事）。

**第四，取回方法要交代。** 帮助中心与 `whatsapp.com`、`developers.facebook.com`、`facebook.com/business` 的正文由前端渲染，直接对 URL 发原始 HTTP 请求只会得到 400 或不含正文的外壳；本篇全部引文取自**渲染后的页面文本**。所以本篇说"未取回"的含义是"本次渲染未得到正文"，**不等于"该页面不存在"**——这两个结论的差别很大，附表 A 与附表 E 都按这条口径书写。

## 1.3 "登录态"不是一个对象，以及"上限"这个词的五种含义

**先立第一篇要读的表：附表 B。** 官方文本里没有一个叫"登录态"的统一对象，它至少被写成**八种**东西——主设备注册态、已链接设备会话、副手机会话、网页浏览器会话、multi-agent 设备席位、两步验证口令态、端到端备份保护态、API 访问令牌态。八种各有授予方、撤销方与时长，**其中只有前两种在帮助中心有"台数"写法**。本篇后面每一章都在处理这八种里的某一种；把它们合并成"我的账号登录状态"，是本主题最常见的读错方式。

**再说"limit"这个词。** 帮助中心与开发者文档共用它，但它至少指五件不同的事，混成一件是把官方数字读错的最主要方式。本篇按这五档给每一个数字归档（附表 C 共 **86** 个条目）：

**第一档：硬数量上限（25 条）。** 数的是可数对象的最大值，超过就加不进去或做不成。例如未开通 multi-agent 时的 **4** 台已链接设备、Business 应用的"4 加 1"、主设备上同时登出的 **2** 个账号、桌面端群通话 **32** 人、端到端备份密钥的 **64** 位、每套账号层级初始的 **2** 个号码。

**第二档：时间窗口与时长（31 条）。** 数的是"多久之内"或"某个状态持续／等待多久"。这一档在本篇最容易读错，因为同一个"14 天"或"30 天"在不同页上绑不同主语：主手机 unused 满 14 天、已链接设备闲置 30 天后自动断开、不知道 PIN 时等 7 天、账号超过 90 天账龄可能被强制开启两步验证。

**第三档：默认可改项（7 条）。** 数字描述的是出厂状态，用户自己能改：谁能把自己拉进群组默认 **Everyone**、已读回执默认**开启**、两步验证默认**关闭**（官方原话 optional）、浏览器会话默认**勾选**保持登录、系统用户为 admin 时默认 **Full access**、吞吐默认 **80 mps**。这一档最容易被误读成限制。

**第四档：条件触发项（11 条）。** 数字描述的是"达到某条件后会发生什么"，不是"不能超过什么"：超过 **4** 台后只有主设备接到来电、取消订阅后只保留最近活跃的 **4** 台、号码上限自动提升的前提之一是达到 **2,000** 档、Android **12** 及以下迁移时被要求精确定位权限、低于 **6.0** 的手机不受支持。

**第五档：分档晋级项（12 条）。** 数字是档位本身，随订阅、质量与用量变化：Meta Verified／One 的 **4→6→8→10** 台、Embedded Signup 的 **10→200→合作伙伴**、消息限额的 **250／2,000／10,000／100,000／Unlimited** 五档、号码配额 **2→20**、模板 **250→6,000**、请求速率 **200→5,000 次／小时**、吞吐 **80→1,000 mps**，以及企业侧处罚的**四级**阶梯。

**五档相加 25＋31＋7＋11＋12 ＝ 86，与附表 C 的条目数闭合；但这 86 个条目只对应 64 个去重后的数字**——因为 13 个数字在不同轴上各占一行。第 14.2 节给出哪些数字刻意不进这张表，以及为什么。

## 1.4 本篇不写什么

一份负面清单，写在正文而不是脚注里，因为它决定了这些页面能被怎么用：

- **不提供**多账号轮换、设备农场、账号池的搭建方法或任何操作时序。
- **不提供**封号规避、限速规避、垃圾信息检测绕过，也不提供申诉话术模板。
- **不提供**任何用于维持、恢复或转移他人设备上会话的步骤；第 5 章的全部内容都是**账号持有者对自己账号**可核对的官方路径。
- **不评价** WhatsApp、Meta、Apple 或 Google 任何产品的安全性、加密强度或政策执行一致性——本篇只报告页面写了什么。
- **不引用**未取回的那 **5** 页的内容，也不引用第三方统计、媒体报道或逆向结论来填补官方文本的空缺。

写这些的目的只有一个：本篇的所有数字与限制都已经被官方写在公开页面上，引用它们**不增加任何实施能力**，但能说明两件事——平台在规则层把哪些行为已经定义为违规，以及用户手上有哪些官方给出的收回访问的路径。

# 2 主设备、已链接设备与"一台手机"的三种身份

## 2.1 唯一的一句总量定义，和它的确切范围

帮助中心关于设备数量的主页面是 `378279804439436`（About linked devices），它给了本篇最常被引用的一句：

> "You can stay connected by linking up to four devices at a time to your primary phone."

这句话有三个限定，逐字读才能读对：

**第一，它数的是"链接到主手机的设备"，不包括主手机本身。** 所以"四台"不等于"四台设备上能用 WhatsApp"——实际同时在场的终端最多是四台已链接设备**加**那台主手机。官方在 Business 应用那一页把这一点写得比个人号页更直白：`647349420360876` 的原文是 "You can use up to four linked devices and one phone at a time."——**四台链接设备与一台手机是并列的两项，不是包含关系**。

**第二，"at a time"是并发口径，不是累计口径。** 它限制的是同一时刻挂着的数量，不限制历史上一共链接过多少台。因此"我一年里换过十几台设备"与"当前最多四台"这两件事不冲突。

**第三，这句话绑的是"your primary phone"，也就是它预设了主设备的存在。** 同一页紧接着写了这句：

> "You'll still need your primary phone to register your WhatsApp account and link new devices."

这句是本篇第 2 章的支点：**注册与"新增链接"这两个动作都以主设备为前提**。少了主设备，四台的额度根本无从使用。它同时也是"登录态"这个词的第一层含义——**账号必须有一个手机形态的锚点**（但 Android 平板存在例外，见 2.2 末尾）。

## 2.2 官方列出的已链接设备清单，与两类例外

`378279804439436` 把可链接设备列成一组：Windows、Mac、Web、Android 平板、iPad、WearOS、Companion Phones（副手机）、Meta AI 眼镜、VR 设备。这份清单在口径上有两个不显眼但重要的性质。

**性质一：清单里的绝大多数条目不能反过来当主设备。** 原句是：

> "Linked devices (Windows, Mac, Web, iPad, and wearables) can't be used as the primary device on an account."

值得注意的是这句的**枚举不完整**：它列了 Windows、Mac、Web、iPad、wearables，**没有列 Android 平板**。这不是疏漏，因为同一页另有专句处理 Android 平板：

> "To use an Android Tablet as a primary device, you must register your account from that device."

也就是说，**Android 平板是唯一一个官方写明"可以当主设备"的非手机终端**——条件是账号本来就注册在它上面。这一句把"主设备必须是手机"这个常见理解变成了不准确表述：准确的表述是"主设备默认是手机，但 Android 平板可以被注册成主设备"，而 iPad 不可以。本篇把这条差异记在附表 F。

**性质二：清单同时给出了能力口径。** 官方写道，已链接设备"work without your phone online"——主手机**不在线**时已链接设备仍能收发。这一句与 2.1 的"注册与新增链接必须要主手机"合起来，才是完整的模型：**建立绑定要主设备在场，日常使用不需要**。这两件事被许多二手讨论混为一谈，而官方是分两句写的。

## 2.3 第二台手机是被当作"已链接设备"处理的

`1046791737425017`（About linking WhatsApp to a second phone）说明把 WhatsApp 链接到另一台手机时，那台手机在语义上属于已链接设备，界面上的证据是一句固定文案：

> 副手机上会显示 "This is a linked device."

这一句是本篇认为最容易被忽略、但对"账号被接管"判断最关键的一条：**你自己添的第二台手机和别人偷偷链接的设备，在设备列表里是同一类对象，区分只能靠"我是否记得链接过它"**。同一页还写明副手机不支持实时位置（"Live location isn't supported on companion phones."）。

## 2.4 Business 应用侧的"四加一"，与它额外的闲置断开规则

`647349420360876` 是 WhatsApp Business 应用的已链接设备页。除了 2.1 引用的那句数量定义，它还给出两条与个人号页表述不完全一致的规则：

- 断开时长写成了**两个动作**："14 天"与"30 天后重新配对"两件事在该页合并表述（本篇按各自主页面归档，见 3.1）。
- 不支持的能力清单里，Business 侧额外列了**状态的添加与删除**——也就是"发布/删除 Status"这件事在 Business 应用的已链接设备上不可用。

登出路径在该页也写得比个人号页更细，iPhone 侧原文步骤是 "Tap the device > Log out > Log out"（**两段 Log out，不是一段**）。本篇把这条差异放进 5.1，因为它直接影响"我是否真的把那个设备踢掉了"。

## 2.5 multi-agent：把"设备数量"改写成"订阅档位"的那一页

`395911122612120`（About multi-agent）是本篇唯一一页把设备数量与**付费订阅**直接绑定的官方页面。它的主张是一张三列对比表，表头是 "Without multi-agent" 与 "With multi-agent"，对应单元格分别是：

> "Connect up to four devices to your account"
>
> "Connect up to 10 devices to your account"

**注意这两句的主语是 "your account"，而 2.1 那句的主语是 "your primary phone"。** 这是本篇认为第 2 章最要紧的一处口径差别：同一个"四台"，在个人号页上是"挂在主手机下面四台"，在这一页上变成"账号可连接四台"。是否为同一件事，官方没有任何一句话把它们对齐（附表 F 记录）。

紧接着是那句把数量交给订阅的定义句：

> "The maximum number of devices you can assign to your business account is determined by the benefits included in your Meta Verified or Meta One subscription."

这句里有三个新信息：**上限不再由产品形态决定而由权益等级决定**；**动作词从 link 变成 assign（分配）**；适用对象是 **business account**。

第四个信息在页尾的 Note：

> "Note: If you cancel your subscription or your subscription benefits change, only your four most recently active devices will remain linked."

这一句给出了全篇唯一一处**"权益收缩时保留谁"的判据**：不是保留最早绑定的，也不是让用户自选，而是按**最近活跃度**排序保留四台。对"多设备席位"的运营含义很直接——一台长期没人用的设备在降级时是第一批掉出去的。

### 2.5.1 这一页没有出现"seat"这个词

值得单独记录：`395911122612120` 全文 **0 次**出现 "seat"。它对"人"的表述是设备侧的：

> "With multi-agent, you and your team can handle large numbers of customer inquiries and orders more efficiently. You can do this by connecting multiple devices to your account and assigning chats to your sales agents."
>
> "Enter a device name. You can use the name of the agent who is using the device. You can change the name anytime."

也就是说，**官方把"坐席"实现为"一台被命名的设备"，人只是设备名的备注**。这是本篇用"协作席位"作章节名时必须打的引号：文本层没有席位对象，只有设备对象。第 10 章在 API 侧会遇到真正的席位类对象（system user），两边的计量单位不同，不可互相换算。

同页还有一句关于同一批设备的通话路由规则，是本篇见过的一条罕见的、把设备数量写成**功能开关**的表述：

> "Accounts with no more than 4 linked devices will receive calls on all devices"
>
> "Accounts with more than 4 linked devices will only receive calls on the primary device"

即：**超过四台之后，响铃范围收敛到主设备**。这条把 2.1 的"四台"从容量限制变成了行为分界点，属于附表 C 的条件触发档。

另有两句限定：功能本身可能尚未开放（"Note: The premium version of this feature may not be available to you yet."），以及入口按订阅类型分成 "Tap Meta Verified or Meta One, depending on your subscripition."（该句 "subscripition" 为官方页面原样拼写，本篇照抄不改）。该页与帮助中心其余页面一样，**没有任何"Last updated"字段**。

### 2.5.2 唯一给出档位↔台数对应表的页面

`3872729742954601`（About Meta Verified for Business on WhatsApp）把 2.5 里"由订阅权益决定"这句落成了具体四档，措辞是同一句式换数字：

> "Link up to four devices"／"Link up to six devices"／"Link up to eight devices"／"Link up to ten devices"

四档依次对应 Business Standard、Plus、Premium、Max。**这是本篇找到的唯一一处"每档多少台"的官方对照**，也是"十台"这个数在帮助中心出现的第二种写法：`395911122612120` 写的是"开通 multi-agent 得 10 台"，`3872729742954601` 写的是"买到最高档得 10 台"，`975910528178219`（About Meta One plans）则只写了 "More linked devices" 这个描述性短语并链回前者，**没有写任何台数**。三页对同一件事给了三种精度，本篇在附表 C 里按页面分别归档。


## 2.6 主设备上一次可以登出几个账号，以及 14 天的尾巴

`1421067466005147`（主设备上的登出页）给了两个数字。第一个是账号数：

> "Up to two accounts are supported."

第二个是登出后的尾巴长度：该页写明，在主设备上登出后，"Any linked devices … will remain connected for 14 days after logging out."

第二个数字值得单独强调：**从主设备登出并不等于立刻收回全部会话**，官方给的是 14 天的延续窗口。这与直觉（"我登出了就安全了"）相反，是本篇第 5 章反复回到的一条。

## 2.7 支持设备的系统版本下限

`2366980643469578`（About supported devices）给出手机侧下限："Android phones running Android 6.0 and newer."，并把 iPad、Android 平板、WearOS 列为可链接对象。

这条下限在本篇里出现在两个位置：一是**能不能装**（本页），二是**能不能迁**（第 7 章的迁移页各有一套版本要求，数字不同）。两者不是一回事，混用会得到错误的结论——迁移页的 Android 下限里还包含更低的 "SDK 21"（第 7.6 节），因为它描述的是"从旧手机搬出来"而不是"在新手机上运行"。


# 3 两套时长与三个默认值

## 3.1 14 天与 30 天是两条独立机制，不是同一条的两半

已链接设备的失效时长在 `378279804439436` 上是相邻的两句：

> "Linked devices work without your phone online, but will log out if your phone is unused for over 14 days."
>
> "We'll also automatically disconnect linked devices after 30 days of inactivity."

这两句必须分开读，因为它们的**主语不同**：

- 第一句的条件落在**主手机**上——判据是"your phone is unused"，也就是主手机有多久没被使用。
- 第二句的条件落在**已链接设备自身**上——"linked devices … after 30 days of inactivity"，也就是那台设备自己多久没活动。

因此这不是"14 天之后 30 天"的两阶段过程，而是**两条并行、由不同对象的活跃度触发的规则**。实际后果是：一台长期不用的 iPad 会因第二句被断开，即使主手机每天在用；反过来，主手机长期不开机，则四台已链接设备会一起因第一句被登出，即使它们个个活跃。

还有一条措辞细节值得记录：官方用的是 **unused**（未被使用），不是常见的转述"offline（离线）"。**"主手机离线 14 天"这个流传很广的说法在官方页面上找不到原句**——一个只是关机、没有"未使用"的手机，与官方写的条件并不严格对应。本篇把它列入第 13 章的口径冲突（附表 F）。

## 3.2 同一个"14 天"在四个页面上指三件事

本篇归档出的 4 个"14 天"（附表 C）分别来自 4 个页面，含义如下：

| 页面 | 原句要点 | 这 14 天在数什么 |
|---|---|---|
| `378279804439436` | "log out if your phone is unused for over 14 days" | 主手机的**闲置期**，到期则已链接设备被登出 |
| `1317564962315842` | "You'll need to log in to WhatsApp on your primary phone every 14 days to keep linked devices connected" | 一次**主动登录动作的周期**，是上一条的操作性表述 |
| `1046791737425017` | 副手机在主手机未使用后 14 天登出 | 把主页面规则**具体到"第二台手机"**这一类设备 |
| `1421067466005147` | "remain connected for 14 days after logging out" | **登出之后**已链接设备的**剩余连接时长**，触发事件是登出而非闲置 |

前三行说的是同一件事的三种写法（闲置触发、周期表述、设备类型特化），第四行**方向相反**：它的前提是用户主动登出，结论是"不会立刻断开"。把第四行误当成第一行，会得出"我在主设备上登出就立刻收回了所有会话"的错误结论；反过来把第一行误当成第四行，会以为"只要不动手机，已链接设备能一直用"。

## 3.3 "自动断开"与"被登出"是两个词，官方没有合并它们

同一页（`378279804439436`）在两句里分别用了 "log out" 和 "disconnect"，而帮助中心另有独立的两篇文章处理这两件事：`834124628020911`（Unlink a device，主动解除链接）与 `1428782138011916`（检查并解除未识别设备）。

**官方没有给出任何把这两个词定义成同一状态的句子。** 因此在文本层，本篇把它们当作两个动作处理，并在 5.2 说明"要收回访问，走哪一条页面写的路径才是可核对的"。与之相关的一条重要留白见 5.4：**官方页面没有写"一键登出所有已链接设备"的按钮**——它只在 WhatsApp Web 的语境里写了 "log out of all your active WhatsApp Web sessions"，范围是浏览器会话而不是全部设备（附表 E）。

## 3.4 群邀请权限：一个默认值，和一个"只管以后"的限定

`1131457590844955` 是"谁能把你拉进群组"的设置页，官方给出的默认值是 **Everyone**，三个可选项是 Everyone／My contacts／My contacts except…。该页与本主题相关的三条限定，逐条都是原文表述：

- **改不了的地方**：该设置无法在 Web 与桌面端更改，只能在手机上改。
- **只管未来的群**：设置只作用于此后加入的群组，不追溯已有群组。
- **邀请链接不受该设置约束**：群组邀请链接不会因为这项设置而被阻断；私人群组的邀请链接本身在 **3 天**后失效（这个数字属于群组生命周期，与"登录态"无关，故本篇不列入附表 C，只在第 12.4 节作为对照出现）。

第三条对"接管"场景有意义：**即使把群邀请收紧到"仅联系人"，通过邀请链接拉你进群的通路仍然不受这项设置管辖**。

## 3.5 可见性的默认：三句互相咬合的原话

`3307102709559968`（隐私设置默认页）给了三句必须放在一起读的话：

> "Any user can see your profile photo, read receipts, and when you're online."
>
> "Read receipts are always sent for group chats."
>
> "If you don't share your last seen or online, you can't see other users'…"

第一句立的是**默认对所有人可见**，第二句立的是**群聊场景下已读回执不可关**，第三句立的是**对等规则**：关掉自己的可见性会同时失去看别人状态的资格。三句合起来的结果是：一个把隐私全部收紧的账号，在群里仍然会发出已读回执，且看不到任何人的在线状态。

同一页还有一句与本篇第 5 章直接相关的否定式表述：

> "It's not possible to see who viewed your profile."

以及一句在"账号被谁看过"这类追问中最常被引用的边界：

> "There is no way to hide your phone number when using WhatsApp."

**手机号在 WhatsApp 里不是可隐藏项**，这一条与"号码即账号标识"（第 7 章）是同一套设计的两个面：正因为号码就是标识，换号才会牵动备份、群组与已链接设备三条线。


# 4 已链接设备上的能力差异与历史同步

## 4.1 官方写明的"在已链接设备上不可用"清单

`378279804439436` 用一段话列出了已链接设备上不支持的能力，本篇把它们逐条抄下来，因为它们决定了"同一个账号在不同终端上看到的事实不一样"：

- **实时位置**（live location）不可用。
- **创建／查看广播列表**（creating/viewing broadcast lists）不可用。
- **WhatsApp Flows** 不可用。

`1046791737425017` 对副手机单独重复了第一条；`647349420360876` 在 Business 应用侧另外补了一条：**状态的添加与删除**在已链接设备上不可用；`1131457590844955` 则写明**群邀请权限这项设置无法在 Web 与桌面端更改**。

把这几页合起来，可以得到一条本篇认为最有实践价值的读法：**能力缺失清单是按页面分散写的，没有任何一页给出完整清单**。也就是说，"我在电脑上做不了某件事"要么是产品限制、要么是那一页恰好没写；判断依据只能是逐页比对，而不是某个终端的功能直觉。附表 E 把"是否存在一份统一的已链接设备能力矩阵页"列为官方未文档化项。

## 4.2 通话：桌面端给出的 32 人，与 Web 页上的"unlimited"

`378279804439436` 里与设备能力有关的唯一硬数字是通话人数：

> "Use the WhatsApp Mac or Windows app to make voice and video calls with up to 32 people."

而 `668538004658079`（About WhatsApp Web）在描述桌面侧能力时用了 "Unlimited calling" 这样的表述。**两句并不必然矛盾**——前者绑的是"群通话人数"，后者绑的是"通话功能是否收费／是否受运营商式限制"，但**官方没有任何一句把这两个词放在同一句话里界定各自范围**。本篇因此只把 32 归入附表 C 的硬数量上限，把 "unlimited" 记入第 13 章的措辞冲突，不据它推任何上限（附表 E）。

需要一并记录的是：`451924530376167`（桌面端专页）本次正文未取回，`/web/download-and-installation/about-whatsapp-web-and-desktop` 会重定向到 `668538004658079`。所以"桌面端另有一张独立的上限表"这件事，本篇无法证实也无法证伪。

## 4.3 历史同步：三句原话，三个不同的范围

关于已链接设备能看到多少历史，官方写了三句，范围各不相同：

第一句是**机制**，来自 `653480766448040`：在链接的那一刻，主手机会把近期的聊天记录以端到端加密的副本形式发给新设备，且这份副本存放在该设备本地。这里的关键词是**"链接时"**——它是一个一次性的快照动作，不是持续同步全部历史。

第二句是**端间差异**，同一页："WhatsApp Desktop syncs more message history than WhatsApp Web."——官方明确写了桌面端比网页端拿到的多，但**没有写多多少、按什么单位多**（附表 E）。

第三句是**已知问题**，来自 `378279804439436`：

> "There is a known issue for some linked devices not displaying up to one year of chat history."

这句的读法要小心。它**不是**"已链接设备可显示一年历史"的能力声明，而是一个缺陷承认：官方预期的行为是某些设备应能显示长达一年的历史，但部分设备做不到。所以"一年"是**问题描述里的期望上界**，不是产品规格。本篇把它归入时间窗口与时长档，同时在第 13 章标注它的性质与其他行不同。

删除的同步也值得一并记录：`653480766448040` 写明删除会在**在线的**设备之间同步。反过来说，一台离线的已链接设备不会即时收到删除指令——这是"多设备一致性"的一个官方承认的窗口。

## 4.4 会话保持与"登出所有会话"的确切范围

`1428782138011916` 是本篇与"账号是否正在被别人用"关系最直接的一页，它给出两件事。

第一件是一个默认勾选：登录网页端时有 "Stay logged in on this browser" 选项。**取消勾选是缩短该浏览器会话存活期的用户侧手段**，官方没有写明取消后具体多久失效（附表 E）。

第二件是一条范围明确的登出能力：

> "If you believe someone has access to your account through WhatsApp Web, you can log out of all your active WhatsApp Web sessions on your phone."

这句的主语是 **WhatsApp Web sessions**，不是 linked devices。同一页给出的清除未识别设备的步骤是逐台进行的（"Tap a device then tap Log Out." 之后 "Repeat these steps for all linked devices."）。**两句合起来才是官方支持的完整动作：批量登出只覆盖浏览器会话，其它设备要一台一台来。** 本篇在第 5 章按这条口径写自查路径，并且不写"一键踢掉所有设备"——因为官方没有这样一句话。

同一页还有一句边界声明，本篇在 5.7 单独处理：

> "Note: WhatsApp can't provide information about who accessed your account or the time and location it was accessed."

# 5 怀疑账号被别人访问时，官方文本给出的路径与边界

本章全部是**防守方向**的官方页面表述梳理：一个普通用户在自己账号上发现了不认识的设备时，官方页面上写了哪些动作、这些动作各自的生效范围与代价。本章不提供任何针对他人设备的操作内容。

## 5.1 第一步是审查设备清单，而登出动作在两端写法不同

`378279804439436` 给出的审查入口是：

> "To review all devices linked to your account, tap > Linked devices. To log out of a linked device, tap the device > LOG OUT."

同一页还建议开启推送通知，以便在出现未识别设备时能收到提示并移除它——这是官方文本里唯一一条**事前**告警手段。

`1428782138011916` 与 `834124628020911` 补了两点：登出既可以在主手机上做，也可以**在那台已链接设备自身上做**；以及 iPhone 与 Android 的菜单层级不同，Business 应用侧的 iPhone 路径更是写成两段 "Tap the device > Log out > Log out"。**这些差异不影响结论，但影响"我是否真的完成了登出"的可核对性**——所以本篇把它们逐页抄下来。

## 5.2 官方给出的"收回全部访问"主路径是重新注册

`1131652977717250`（恢复被盗账号）与 `506595211487528` 共同构成这条路径。核心机制在服务条款里已经埋好——号码是唯一标识——而这两页把它变成动作：用同一号码重新注册、通过 6 位注册码验证之后，**所有设备上的会话都会被登出**。`506595211487528` 的原句是：

> "WhatsApp can only be used with one phone number at a time. After you successfully register your phone number on WhatsApp, anyone who might be using your account is automatically logged out…"

这一句与 2.1 的"注册需要主手机"合起来，构成一个闭环：**能用这个号码收到验证码的人，就是有权把所有其它终端踢下线的人。** 因此本篇在第 6 章处理两步验证时，把"谁能拿到 SIM"当作整个登录模型的前置条件，而不是并列的第二道锁。

## 5.3 一句反直觉的否定：改密码不会把别人移出去

`1131652977717250` 里有一句很短但极重要的话：

> "Changing your password won't remove someone else from your account."

这里的 password 指两步验证侧的口令。**它的作用是阻止后续注册，而不是撤销已有的访问。** 官方把两件事分在两条句子里写：改口令防止"再次进来"，重新注册处理"已经在里面的人"。把前者当成后者，是自查时最容易停在半路的错误。

## 5.4 不知道两步验证 PIN 时，官方只给了一个等待期

同一页对"PIN 被别人设置过／自己不记得"的情形给的处置是一句等待：

> "If you don't know your PIN … wait 7 days and try again."

配套的还有第 6 章会展开的另外两个时间表述：重置入口"will unlock after a few days. This option can't be undone."，以及号码被重新分配时的 "wait seven days after you've registered"。**官方没有写这三处等待时间的计算起点，也没有写是否可以加速。** 本篇按原样归档 7 天这个数字（附表 C 有两个 7 天行，分别来自这两页），不合并成"一个 7 天规则"。

## 5.5 SIM 已不在原机时，顺序错了就不可逆

`1131652977717250` 写了两条与号码强相关的硬约束，本篇认为这是全篇最需要提前知道的两句话：

> 换 SIM 之后必须走 Change Number "or you could permanently lose access"。
>
> "It's not possible to recover an account without a SIM card with the same phone number as your account."

第二条的口径是绝对的：**没有对应号码的 SIM 卡就无法恢复账号**（第 6.6 节会说明"邮件验证不需要在网的 SIM"是另一件事，它针对的是两步验证的重置，不是号码归属的证明）。这两句合起来给出一个顺序要求：**先改号，再换卡**。第 7 章在迁移语境下会再遇到同一条要求，且措辞更直白——"you'll lose your chat history"。

## 5.6 举报自己被盗的账号，官方写明可能带来反效果

同一页还有一句罕见的、写在防守路径里的风险提示：

> 举报那个你正在恢复的、被盗用中的账号 "might ban it for violating our Terms of Service"。

这句的实际含义是：**被举报的对象可能就是你自己那个号码的账号本体**，处置结果包括封禁。本篇不解释"该不该举报"，只记录官方把这一条写在了恢复流程页里，并在第 12 章把它与封禁后的数据不可访问（"You can't access your chat history or backups while your account is banned."）并排放在一起。

## 5.7 官方写明的三条"我们做不到"

把分散在三页里的否定式表述集中列出，是本篇认为对读者最有用的部分：

1. `1428782138011916`：**无法提供**"谁访问了账号、什么时间、从什么地点"。
2. `1131652977717250`：**无法替他人停用账号**，理由是验证限制（"WhatsApp can't deactivate accounts for others due to verification constraints."）。
3. `1197347060992858`：换设备时若被要求等待验证码，**"We can't speed up this wait time."**

这三条的共同形状是：**平台把"号码与设备的现场控制权"完全交给持有 SIM 的一方，因而也不提供任何平台侧的取证、代客处置与加急服务。** 任何声称能代查访问记录、代踢设备、代解封的第三方服务，与这三句官方表述直接冲突（第 12.3 节还有一句同方向的："WhatsApp Support does not provide support related to account bans…"）。


# 6 两步验证：一句区分、一次措辞换代，和三个等待期

## 6.1 先数清楚"6 位"出现在几个地方

帮助中心与开发者文档里有三种六位码，它们由不同页面定义，不可互相替代：

1. **注册码**：`506595211487528` 写明通过短信或语音电话送达的 "6-digit registration code"，它是 5.2 那条"重新注册即登出所有设备"路径的唯一凭证。同一页还有一句安全提示："Never share your 6-digit registration code or password with anyone."
2. **两步验证口令**：`1920866721452534` 与 `1278661612895630` 语境里的 PIN／password。
3. **邮件验证码**：`1082491466048496` 与 `2183055648554771` 里的六位数邮箱码，且该页写明**只有最新的一枚有效**、过期时间只写成"a short time"（具体分钟数官方未文档化，见附表 E）。

把三者混为一谈会直接导致自查时找错入口：注册码证明的是**号码归属**，两步验证口令证明的是**账号所有者的第二因素**，邮箱码证明的是**邮箱访问权**。本篇在附表 C 只归档"6"这一个数字，但在此处点明它挂三个对象。

## 6.2 官方给两步验证的定位：可选，且防的是"未授权访问"

`1278661612895630` 的第一条口径是**它是可选功能**，不是默认开启。该页对作用范围的表述是：

> "…like the combination of SMS and password, you can protect against unauthorized access."

同一页有本篇认为**全篇最重要的一句区分**：

> "Trusted devices are different from linked devices like WhatsApp Web or Desktop…"

这句话把两个常被合并的概念正式拆开了：

| 概念 | 官方定义位置 | 由谁授予 | 由谁撤销 | 与 2.1 的"四台"关系 |
|---|---|---|---|---|
| **已链接设备**（linked devices） | `378279804439436` | 主设备扫码／配对 | 主设备逐台登出，或 14／30 天规则失效 | 就是那"四台" |
| **受信任设备**（trusted devices） | `1278661612895630` | 两步验证语境下的本机 | 该页未给独立撤销入口 | **不是同一集合** |

**"我在设备上登录过"不等于"它是已链接设备"**；反过来，把一台设备从链接列表里登出，也不自动等于它不再是某种"受信任"状态。官方没有写两者的转换关系，也没有写"受信任设备"的上限数量（附表 E）。这一条对第 5 章的自查有实际影响：**设备清单只能审查 linked devices 这一类对象。**

## 6.3 一次进行中的措辞换代：6 位 PIN 正在变成 password

`1920866721452534` 顶部有一条说明，是本篇少见的、官方直接承认"本页文字与新版本不一致"的句子：

> "Note: WhatsApp is upgrading the 6-digit PIN used in two-step verification to a password."

这条换代有三个连带后果，本篇逐条记录：

- **强度模型变了**：口令侧引入了"至少 8 个字符、至少 1 个字母、至少 1 个数字"的要求（`2183055648554771`），这在 6 位 PIN 模型下不存在。
- **入口从"PIN"改叫"password"**，但同页仍写 "Tap Settings > Account > Two-step verification"，两个词在同一篇文章里并存。
- **API 侧同步弃用**：`two-step-verification` 文档页写明针对部分 Cloud API 号码的 6 位 PIN 正在滚动弃用（第 6.6 节）。

因此本篇在引用任何"6 位 PIN"的句子时都会加一条限定：**这是换代期的文本，不是稳定的产品规格**。附表 F 把这条列为版本性冲突。

## 6.4 忘记口令：官方按"距上次登录是否超过 30 天"分两档给后果

`2183055648554771` 是本篇唯一一页把**时间长度直接换算成数据损失**的页面。它给了两档：

- **距上次登录不足 30 天**：重置会删除该期间收到的消息，但账号资料（头像、名称等）保留。
- **距上次登录超过 30 天**：重置同时删除消息**与**资料，且官方写明不可恢复。

同页还有两句限定，都必须一起引用：

> "…will unlock after a few days. This option can't be undone."

也就是说重置入口本身有一个"几天后才解锁"的等待，而一旦执行**不可撤销**。另一句是口令强度要求："at least 8 characters, at least 1 letter, and at least 1 number"。

验证通路该页写了三条：**邮箱、短信、语音电话**（"Verify another way"）。这是它与 6.5 的差别所在——6.5 处理的是"号码本身不再属于你"，这一页处理的是"口令忘了但号码还在手上"。

## 6.5 号码已被重新分配时，唯一的额外成本是七天

`1082491466048496` 处理一种特殊但常见的情形：拿到的号码是**二手号码**（前主人注册过 WhatsApp）。该页给的等待期是：

> "you'll need to wait seven days after you've registered before you can reset your two-step verification method."

同一页还有两句在本篇很有价值：

> "An active SIM card isn't required for email verification."

这句常被过度解读。它的范围是**邮箱验证这一步**不需要在网 SIM，而不是"没有 SIM 也能恢复账号"——后者被 `1131652977717250` 明确否定（见 5.5）。**两句一起读才成立：注册需要 SIM，注册之后的两步验证重置可以走邮箱。**

另一句是首次注册的通路："first-time registration code by SMS or phone call only"——**首次注册时邮箱不可用**，这与 5.2 的"验证码等于控制权"闭环一致。该页还写明邮箱地址只能在**已登录的活动设备上**修改。

## 6.6 API 侧：两步验证可以设，但官方没给关闭它的端点

`two-step-verification` 文档页给出两句规格：注册号码时必须设置 6 位 PIN；以及一句在文本层很关键的否定：

> "There is no endpoint to disable two-step verification."

`phone-numbers` 页重复了同一条限制（"you cannot disable two-step verification using the API."）。**这两句共同定义了一个单向开关**：接入方可以用 API 设置两步验证，但不能用 API 关掉它。本篇不推断这是设计意图还是当前实现限制——官方没写，故记入附表 E。

## 6.7 商业组合层：账龄超过 90 天可能被强制开启，且关不掉

`865384580786591` 与 `280940009201586` 是 Meta 商业组合（Business Portfolio）的两步验证页，规则与 WhatsApp 账号侧完全不同一层：

- 广告主**可能被要求**完成两步验证。
- 组合账龄 **">90 days"** 时可能被自动要求开启，且开启后**无法关闭**。
- 可选项有三个：Admins only／Everyone／No one；只有拥有**完全控制权**的人能改这个开关。
- 第三方应用访问时会触发一次验证码。
- 执行验证需要使用**个人 Facebook 资料**，而不是商务账号身份。

最后一条与本篇主题的关系值得点明：**这一层的"登录态"锚在 Facebook 个人身份上，而 WhatsApp 账号那层的登录态锚在 SIM 卡上。** 两套身份体系在同一条业务流程里串联，任何一处的会话失效都会表现为"业务侧无法操作"，但登出路径完全不同。需要如实记录的是：Meta／商业平台条款本身**没有**强制要求两步验证，条款层只写了"maintain the security of its account and login credentials"——强制来自帮助中心页，不来自合同文本（附表 F 记录这条层级差异）。


# 7 更换号码与更换手机：同一件事的两套规则

第 5 章的结论是"谁能收到这个号码的验证码，谁就拥有账号"。本章处理这个模型的另一面：**号码是可以换的，但换号码会牵断三条线**——备份、旧账号本体、以及顺序错误时的聊天记录。

## 7.1 换号传输的对象清单，和它同时删除的东西

`1166321223998129`（About changing phone numbers）给了本篇少见的**逐项清单式**表述。它会转移：profile photo、username、display name、privacy settings、contacts、individual and group chats。

同一页紧跟着三条否定式后果，必须与上面的清单一起引用：

1. **旧号码对应的账号被删除**（"deletes the account tied to the old number"），不是保留成闲置状态。
2. **备份随号码作废**：
   > "Cloud backups of your chat history are linked to the phone number used when the backup is created… all backups made using the old number can no longer be used to restore your data."
3. **不可回退**：
   > "If you change your phone number by mistake, there's no way to revert this change."

第 2 条是本章的核心口径：**备份的键是电话号码，不是账号 ID**。它同时解释了第 8 章为什么把"备份绑定"写成两层（号码＋云账号），也解释了 7.5 里"先改号再换机"这条顺序要求为什么存在——顺序反了，旧备份既不能用于新号，也带不走。

同一页还有一句常被忽略的补充：群组虽然"随账号转移"，但**群里的成员显示需要管理员重新邀请**才能回到部分群组。也就是说"group chats 会转移"这句在页内并不是无条件的。

## 7.2 换号时"通知联系人"这件事的三个选项，和一处无视

`498754122134583`（换号操作步骤）给出通知侧的三选项：**All contacts／Contacts I have chats with／Custom**。同一页写明两件事：

- **群组不受该选择影响**，无论选哪一档，所在群组都会被通知。
- **被屏蔽的联系人保持屏蔽，且不会收到通知**。

后者是一条有用的安全性质：换号不会把一个人从屏蔽名单里放出来，也不会向他广播新号码。

该页还列出**换号的前置条件**与**不接受的号码类型**。前置条件是新号码能收到短信或电话、且旧号码当前仍处于注册状态；操作必须在**主设备**上完成（与 2.1 同一条锚点要求）。被拒绝的号码类型是一份清单：

> VoIP／Toll-free numbers／Paid premium numbers／Universal access numbers (UAN)／Personal numbers／Landlines

其中官方加了一条括注："Note: Landlines are only accepted on the WhatsApp Business app"。**同一种号码在个人号和 Business 应用两侧的可用性不同**——这是帮助中心里少数几处显式按身份分叉的规则之一，与上一篇（群发功能口径篇）观察到的"不分端但分身份"一致。

## 7.3 Android 到 Android：三条路径，和一条顺序要求

`1197347060992858`（About changing phones）说明同为 Android 时官方给了三条路：**账号内转移（Chat Transfer）／Google 账号备份恢复／以及换机流程内的迁移**。它的两条硬要求是：

- 旧设备必须处于可用状态，因为**要在新设备上扫旧设备上的二维码**。
- **顺序要求**：
  > "If you're changing phone numbers and want to keep your chat history, you must first change numbers on your old phone. … you'll lose your chat history."

同页还有两句与"登录态"直接相关的表述：

> "When you re-register on a new phone, you will be logged out on your old phone as soon as re-registration is complete."
>
> "Sometimes, we may need to ensure your account is secure while changing devices. In these cases, you'll need to wait to receive your verification code. We can't speed up this wait time."

第一句是**排他性的即时生效**：新机注册完成的瞬间旧机下线，不存在两台手机同时在线的过渡期（这与"副手机"不同——副手机是已链接设备，不是第二台主设备，见 2.3）。第二句与 5.7 的第 3 条同源：**换设备触发的风控等待没有加急通路**。

`209942271778103`（Chat Transfer 专页）给出条件清单与两条细节：Android 6.0 及以上、**必须同号**、新手机必须尚未注册过 WhatsApp、两台手机靠近且接电、开启 Wi-Fi（不要求已连接）。两条细节是：

> "Precise location permissions are required by the device in Android 12 and below, but WhatsApp does not collect your precise location."
>
> "Messaging is paused during the chat history transfer."

第一句是本篇少数几处**官方主动区分"系统要求"与"应用采集"**的文本：精确定位权限是 Android 12 及以下**系统层**的要求，不是 WhatsApp 的采集行为。第二句给出一个可观测的副作用——迁移期间消息暂停收发。失败重试的操作提示是关闭蓝牙等 30 秒再打开（附表 C 的"30 秒"来自这里）。

该页还写明**不能迁移的对等支付消息与频道媒体**，以及一条对商务用户很重要的限制：

> "It's currently not possible to transfer chats on the WhatsApp Business app."

## 7.4 跨端迁移：两个方向不对称，且不对称项正好是通话记录

两个方向各有一页，条件与可迁内容都不相同，本篇把它们并排：

| 项目 | Android → iPhone（`686469079565350`） | iPhone → Android（`1295296267926284`） |
|---|---|---|
| 使用的通道 | Move to iOS | WhatsApp 自身（"WhatsApp handles it"），全程端到端加密 |
| 新机状态要求 | iPhone 必须是**出厂全新**状态 | 无此要求 |
| 系统下限 | Android Lollipop **SDK 21** 及以上／iOS **15.5** 及以上 | Android 6.0 及以上 |
| 号码 | 必须同号 | 必须同号 |
| **通话记录** | **不可迁移** | **可以迁移** |
| 显示名 | 不可迁移 | 不可迁移 |
| 状态更新 | — | 不可迁移 |
| 频道媒体 | 不可迁移 | 不可迁移 |
| 迁移后的云端 | 数据不会进入云端，直到做出 iCloud 备份 | — |
| 合并 | 不支持合并，需抹机重来 | 不支持合并，需抹机重来 |
| 迁移后旧机 | "You'll be signed out from your Android phone once the data is prepared." | — |

**唯一的不对称项是通话记录**：从 iPhone 搬到 Android 时它会过去，反方向则不会。这一点与 `743219147158705` 的口径吻合——该页写明通话记录存在聊天数据库文件里（"Your call logs are stored in your chat database file."），所以它能否过去取决于迁移通道是否搬运整个数据库，而不是取决于"通话记录"这个功能本身。

Android→iPhone 页还给了两句边界，本篇认为是跨端话题里最实用的两条：**其它第三方迁移工具不受支持**（要用官方的 Move to iOS）；以及

> "WhatsApp can't see the data you transfer."

后一句与该页"数据走本地 Wi-Fi 或热点直传"的机制描述一致，属于**能力否认**类表述，官方没有配套的验证方法（附表 E）。

最后是该页最容易被跳过、但和第 5 章闭环的一句：

> "It's not possible to transfer your WhatsApp data to a new phone number."

**跨端迁移只搬设备，不搬号码。** 与 7.1 的"备份跟号走"合起来，本篇可以得到一条完整顺序：**要换号就在旧手机上先换号，再换设备；换设备时不要指望用迁移来改号。**

## 7.5 一张三分表：跟号走、跟设备走、跟云账号走

把第 2、7、8 章的绑定关系合起来，是本篇认为最有解释力的一张表。它不是任何官方页面的原样内容，而是上引各页的**并置结论**，因此每一项都标了出处。

| 对象 | 绑定键 | 出处 | 后果 |
|---|---|---|---|
| 账号本体 | 电话号码 | `506595211487528` | 换号即删除旧账号 |
| 注册与新增链接的能力 | 主设备 | `378279804439436`、`498754122134583` | 没有主设备就无法使用额度 |
| 已链接设备会话 | 主设备活跃度（14／30 天） | `378279804439436` | 主手机闲置会连带全部掉线 |
| 端到端备份 | 电话号码 **＋** 口令／密钥／通行密钥 | `481135090640375`、`490592613091019` | 换号后旧备份作废；丢密钥不可恢复 |
| Google 备份 | 电话号码 **＋** Google 账号 | `407643231403807` | 换云账号即取不到 |
| iCloud 备份 | 电话号码 **＋** 同一 iCloud 账号 | `481135090640375` | 与 WhatsApp 云存储互斥 |
| 商业组合的操作权 | 个人 Facebook 身份（两步验证） | `865384580786591` | 与号码是两套会话体系 |
| API 的调用权 | 访问令牌 **＋** 系统用户 | `access-tokens` | 见第 10.4 节 |


# 8 备份层：三种存储、两个绑定键与一条覆盖规则

## 8.1 三种存储，两个管理方

`481135090640375`（Back up chat history）把备份分成两类提供方，并给了三处容易读漏的限定：

- **WhatsApp 云存储**：该页写明其中的聊天**始终端到端加密**，并有一句强表述："No one, not even WhatsApp or Meta, can access it"。
- **Google 存储**：明确写为**由 Google 管理**，不是 WhatsApp 管理。
- **iCloud**：在 iOS 标签页下写明**由 Apple 管理**，并且**与 WhatsApp 云存储互斥**——两者不能同时作为备份目标。

三处的管理方不同，意味着"谁能删你的备份""谁在什么条件下保留它"这两个问题的答案也分属三个主体的政策，而不是一个。8.4 会看到这条差异的具体后果。

同一页还有一句命名口径：iOS 侧的设置项叫 "Automatic backups"，Android 侧叫 "Auto backup"。**同一功能两端命名不同**，本篇在附表 F 记录，因为按中文名去搜索设置项会找不到对应项。

## 8.2 端到端备份的三种保护物，和"64 位"这个新写法

`490592613091019`（E2E encrypted backup）写明备份由以下三者之一保护：

> "protected by either a passkey, password, or a 64-digit encryption key"

这里有两个口径点：

**第一，本篇检索到的现行页面用的是 64-digit，没有出现旧口径里的"32 个字符"写法。** 这是一处版本变化，本篇只按现行页归档（附表 C 的"64"来自本页），并把"是否曾有 32 字符口径、何时改的"列为官方未文档化（附表 E）——**官方没有发布过变更说明页**。

**第二，三种保护物不是等价的三种口味，它们各自的失效后果不同。** 该页对三者给了同一句不可逆声明：

> "WhatsApp can't send you a copy, reset it, or restore your backup for you."

也就是**口令／密钥／通行密钥任一丢失，备份即永久不可解**。这一句与 6.4 的"重置不可撤销"、5.5 的"无 SIM 不可恢复"形状相同：**平台把所有恢复能力都放在用户手上，同时明确放弃服务端恢复通道。**

该页还写了三条操作性口径：切换保护方式需要**先关闭再重新开启**（不是改设置）；通行密钥路径要求 Android 9 及以上、Google 移动服务版本 241217000 及以上、且设备**已设置屏幕锁**（这三项属于设备系统门槛，本篇不并入附表 C 的数字归档，理由见 8.2 末尾）；免费账号使用 WhatsApp 云存储时包含 2 GB 容量。

**为什么这三项版本要求不进附表 C**：它们描述的是"通行密钥这个功能能不能用"，属于第 1.3 节里"能力前提"而非"可数对象的上限或窗口"，与 6.0／15.5 那两行（迁移与运行的系统下限）不是同一档。本篇按档不重复计数的原则处理，第 14.2 节给出口径说明。

最后有一句适用范围声明值得原文抄下，因为它限制了本章全部结论的可用人群：

> "WhatsApp cloud storage is a new feature we're building and testing out and may not be available to you yet."

同一页还列出了该功能会收集的用量信息（备份大小、排障数据、存储指标、系统事件）——**"端到端加密"与"不收集元数据"是两件事**，官方在该页同时写了这两点。

## 8.3 频率、内容开关，和"只认最后一次成功"

`481135090640375` 给出三个频率档：**每日／每周／每月**。同一页有一句关于"每日"的具体时点表述：

> "If your Google Account backup frequency is set to Daily, your chats will be saved to your Google Account every night after the local backup."

内容侧有三个可改开关：**Include videos／Back up over mobile data／Include status archive**。范围侧有一句容易被忽略的：**社区与频道更新会被包含，但频道媒体只有频道管理员的备份里有**。恢复侧则是一句限制：

> "can only be restored from the last successful backup"

这一句让"频率"这个参数有了另一层含义：**能恢复到的最远时间点，取决于最后一次成功的备份，而不是你设置的频率。** 连续多次失败会让可恢复点迅速后退，而该页没有写失败次数的告警机制（附表 E）。

## 8.4 谁在删你的备份：覆盖规则与五个月

`407643231403807`（Google Account backups）只有两句与生命周期相关的话，但它们都是**删除方向**的：

> "Each new backup replaces the previous one."
>
> "Google may automatically delete backups that haven't been updated in five months."

第一句意味着**没有历史版本**——备份不是累积的，是覆盖的。第二句意味着**即使不再使用，也存在一个约五个月的静默过期**，且主语是 Google 而不是 WhatsApp，措辞是 "may"。

把 7.1（备份键是号码）、8.2（密钥丢了不可恢复）、8.4（旧备份被覆盖且五个月可能被删）三句放在一起，就得到本篇对"备份能不能当恢复手段"的完整回答：**它是单点、覆盖式、且有三把可能丢失的钥匙（号码、云账号、口令）的。**

至于 Android 本地备份文件夹、`.crypt` 版本号、本地保留几份这些常被讨论的参数，**本次在帮助中心未取回任何写明它们的页面**（附表 E 第 12 项），本篇不做推测。

## 8.5 通话记录：在数据库里，因此随备份走

`743219147158705` 给了机制句 "Your call logs are stored in your chat database file."，并写明两件事：通话记录**只能通过备份恢复**，且**无法通过邮件或导出取回**。这条解释了 7.4 表格里唯一那个不对称项——迁移通道搬不搬数据库，决定了通话记录去不去得了新机。

# 9 政策层怎么写"多设备访问"与"自动化"

本章是**规则文本梳理**：把平台在合同与指南层写下的措辞列出来，让读者知道哪些行为在条款里已经被点名。本章不提供任何"如何不被检测到"的内容。

## 9.1 服务条款：三条直接相关的义务

`whatsapp.com/legal/terms-of-service`（Effective January 4, 2021）里与登录态／多设备／自动化有关的三处：

**（1）注册义务。** 要求提供准确信息、使用**当前移动号码**注册，并在号码变化时于应用内更新。这是 7.1"备份键是号码"在条款层的依据。

**（2）消息与用途限制。** 原文第 (e) 项禁止：

> "involve sending illegal or impermissible communications such as bulk messaging, auto-messaging, auto-dialing, and the like"

第 (f) 项禁止**任何非个人用途**，除非"otherwise authorized by us"。这两项与第 2、3 章的产品能力无关——**它们约束的是用途，不是数量**。

**（3）一项直接关于"多台设备"的条款。** 危害条款第 (h) 项原文：

> "distribute or make our Services available over a network where they could be used by multiple devices at the same time, except as authorized through tools we have expressly provided via our Services"

本篇认为这是**全篇与"群控"这个话题最相关的一句官方文本**：它没有禁止多设备，而是把多设备的合法性**限定在"官方明确提供的工具"之内**。第 2 章列出的那些已链接设备能力，正好落在这句的例外里；任何未被官方工具化的"多台设备同时使用同一账号"的形态，落在这句的禁止侧。同组还有第 (c) 项（未授权访问）、第 (e) 项（通过"unauthorized or automated means"创建账号）、第 (i) 项（克隆软件／接口）、第 (j) 项（滥用举报渠道，含虚假申诉）。

条款另有三句与本篇前几章互相呼应：暂停或终止后"you agree not to create another account without our permission"；"You are responsible for keeping your device and your WhatsApp account safe and secure…"（**安全责任在用户侧**）；以及未送达消息在服务端最长存储 **30 天**（附表 C 的这个 30 天与其他四个 30 天无关）。

## 9.2 2026 年 9 月 23 日：三份新条款同日生效

本篇必须精确标注日期，因为这一天有三份文本同时进入生效／修订状态，它们取代了此前分散的表述：

| 文本 | 页面上标注的日期字段 | 与本篇相关的条款 |
|---|---|---|
| WhatsApp Terms for WhatsApp Business Platform | Effective September 23, 2026 | §3 仅限企业使用＋此前未被暂停；§4 须遵守停止／退订请求；§9 终止权（含"过多负面反馈"）；欧盟／英国需提前 30 天通知并配套投诉系统；**终止后数据最长保留 90 天** |
| Meta Terms for WhatsApp Business Platform | Last Modified September 23, 2026 | §2.2 组合＋WABA＋消息账户，并同意接收验证短信／电话；§4.1(a)–(h) 含逆向工程、抓取、克隆接口；§4.7 当 AI 是主要功能时的提供方限制；§3.1 逾期月费率 1.5% |
| WhatsApp Terms for WhatsApp Business App | Effective September 23, 2026 | 限制项 (b) 多设备网络分发；(g) **"develop or use any applications that interact with our Business App Services without our prior written consent"**；§6(c) 使用他人账号；§6(h) 垃圾信息；终止后未经许可不得再开商业账号 |

第 (g) 项值得单独指出：它是**"任何与应用交互的程序都需要事先书面同意"**级别的条款，覆盖面远大于"自动化群控"本身。它与 9.1 的第 (h) 项一起，构成本篇讨论的所有"第三方工具"在条款层的两条依据。

两份日期字段的名称不同（Effective 与 Last Modified），本篇按页面实际字段名抄录，不合并成"发布日期"。

## 9.3 消息指南里那句把所有关键词串起来的定义

`WhatsApp Messaging Guidelines`（Last updated September 23, 2026）有一段把本篇涉及的行为词全部并列写进同一句，值得完整引用：

> "Do not engage in adversarial behavior. This includes efforts to undermine WhatsApp integrity systems, abuse reporting tools, compromise user accounts, scrape data, or use unofficial clients, bulk messaging, auto-messaging, auto-dialing, or automation to harm WhatsApp or our users."

同一份指南另有一句程序性表述：处置可能由**自动化系统判定并辅以人工复核**（automated processing + human review）。这句话在第 9.5 与第 12.2 两节都会用到，因为它是理解"为什么申诉材料不被逐条阅读"的官方口径。

## 9.4 商业消息政策：opt-in 两条、24 小时窗口、以及自动化必须给人工出口

Business Messaging Policy（`business.whatsapp.com/policy`，301 跳转至 `whatsappbusiness.com/policy`，页面上**无日期字段**）的四条相关表述：

**（1）opt-in 的两项要件**：收件人给出号码或用户名；且明确给出接收许可。政策把 **opt-in 方式的唯一责任**放在企业侧。`getting-opt-in`（Updated June 16, 2026）另有一句时间锚点："As per the November 2024 WhatsApp Business Messaging Policy update…"——**同一政策里，带日期的修订与页面本身的修订是两个不同的时间**。

**（2）24 小时客服窗口**："opens and resets with each user message"；窗口外只能发**已批准的模板**。`send-messages`（Nov 4, 2026）补充窗口的重置也可由**通话**触发。

**（3）自动化被允许，但必须提供转人工的出口。** 这是本篇认为政策层最实质的一条：窗口内允许自动化应答，同时要求提供升级路径，列举项为 **In-Chat Human Agent transfer／Phone number／Email／Web support／In-store visits／Support form** 六种。换句话说，**"纯机器人无人可接"在政策层是不合规的**，与它技术上是否可行无关。

**（4）一条与"账号被限制"直接相关的机制句**：

> "Our systems may limit the amount of messages a business can send or calls a business can initiate, based on this user feedback."

`getting-opt-in` 那句更直白："Our systems will rate limit businesses if the business's quality is low for a sustained period of time." **限流的判据是用户反馈与质量，不是发送量本身**——这与第 10 章的速率规格是两根不同的轴。政策还包含一条"禁止将数据用于未来其他用途"的义务，以及"政策可能不经通知更新"。

## 9.5 企业侧处罚阶梯：四级，且第一级只有 1 或 3 天

`policy-enforcement`（Updated May 21, 2026）是官方唯一一处把处置写成**阶梯**的页面：

1. **1 或 3 天**封锁：限营销／效用／验证类模板与添加号码。
2. **5、7 或 30 天**封锁：限任何消息与添加号码。
3. **无限期账号锁定**：只能申诉。
4. **永久停用**。

同页另有三句与"能不能救回来"有关的话：涉及儿童性剥削、诈骗、恐怖主义、非法毒品的情形**立即下线**；申诉决定通过商务管理平台给出，"typically takes 24 to 48 hours"，结果只有 **Unchanged 与 Reversed** 两种；以及一句限定：

> "Not all spam violations might be appealed."

本篇把"1 或 3 天／5、7 或 30 天"这一行归入附表 C 的分档晋级档（同页给出的 24 至 48 小时归入时间窗口档）。**每一级内部还有多个可选时长，官方没有写选哪个时长的判据**——这条留白见附表 E。


# 10 协作席位层：Business Platform 的账号模型、配额与令牌

第 2 章讲的是"人和设备"的席位，本章讲"程序和主体"的席位。两者容易被混称"多设备接入"，但它们的计量单位完全不同：前者数终端，后者数**号码、方案商、系统用户与请求速率**。

## 10.1 2026 年 9 月 23 日的账号模型拆分

`account-model-evolution`（Nov 2, 2026）记录了本篇需要事先说明的一次结构性改动：**WABA（WhatsApp Business Account）被拆成两层**——

- **WAAC**：持有**单个**企业电话号码。
- **Messaging account（消息账户）**：持有模板与计费。

页面上标注的落地节奏是：GA 于 September 23, 2026（与 9.2 的三份条款同日），所有企业在 2026 年 10 月中旬前完成，采用分阶段迁移。该页给了一个归属对照：**模板、计费、webhooks 归到 Messaging account；号码归到 WAAC；质量与会话历史仍留在每个号码上。**

这个拆分对"席位"话题有直接后果：**过去"一个商务账号里有什么"的一句话，现在必须回答"你说的是 WAAC 还是 Messaging account"。** 该页还写明两条边界：号码在不同合作伙伴之间迁移**不受支持**；一个账号只归属**一个** portfolio。

## 10.2 号码与模板配额：两档，和触发它们的前提

`whatsapp-business-accounts`（Aug 5, 2026）给出一组并列配额：

> "Meta Business Accounts are initially limited to 2 registered business phone numbers per WhatsApp account hierarchy, but this limit can be increased to up to 20."
>
> "initially limited to 20 WhatsApp account hierarchies"

模板侧是 250（未完成验证）到 6,000（已验证且显示名获批）。

`phone-numbers`（May 21, 2026）给出号码上限自动提升的**两个前提**，任满足其一即可：企业验证，**或达到 2,000 的消息限额**。满足后自动升到 20，并通过 `business_capability_update` webhook 携带 `max_phone_numbers_per_business` 通知。该页另外要求号码状态必须是 "connected"，并对 IVR／1-800 号码给了注意事项。

**这里的 2,000 与限额档位表里的 2,000 是同一个数字、两种作用**：在 10.9 的档位表里它是第二档，在这一页它是"号码配额从 2 升到 20"的触发门槛。本篇在附表 C 里给它开了两行（分档晋级、条件触发各一行），这是"行数≠数字个数"的一个典型例子。

## 10.3 共享给多少个合作伙伴：两个数，两个层级

同一页把"共享"拆成两种对象，且配额不同：

- 一个 **WhatsApp 账号**可与 **最多 5 家** Solution Partner 共享。
- 一个 **Messaging account** 可与 **最多 2 家**共享。

这两句是本篇找到的、**在 API 层最接近"席位"原意**的官方数字：它限的是"多少个不同主体能操作同一套资产"，而不是"多少台终端"。它也解释了 5.7 第 2 条（平台不代客停用）在商务侧为什么另有一套授权模型——**商务侧的"谁能动这个账号"是被显式建模的**。

## 10.4 系统用户与访问令牌：唯一一处"人"被建模成对象的地方

`access-tokens`（Sep 19, 2026）是本篇唯一一页把"操作账号的人"写成可枚举对象：**system user 只有两种角色——admin 或 employee**。三条相关口径：

- admin 的默认权限是 **full access**，但可以按账号覆盖为 **Partial**。
- 授权要**分别授予**：WhatsApp 账号与 Messaging account 是两套权限，都要给。
- 缺权限时报的是**错误码 `200`**，不是 HTTP 状态码 200。最后一条是文本层的陷阱：**同一个"200"在两份文档里指完全不同的东西**，本篇把它放进附表 D 而不是附表 C。

令牌寿命侧，该页写系统令牌是 long-lived、用户令牌"expire quickly… every few hours"，且令牌是 opaque（不透明字符串，不应解析内容）。把"每几小时"这个表述归档进时间窗口档之后，需要立刻补一句：**官方没有给精确秒数**，同一体系的 Facebook Login 文档则给了两个近似值——短期令牌 "approximately 1-2 hours"、长期令牌 "approximately 60 days"；而 Standard-access 的 Marketing API 与商务管理平台系统用户令牌**不随时间过期**。三句合起来的正确读法是：**"令牌什么时候失效"取决于令牌类型，不存在一个通用的 WhatsApp 令牌寿命。**

**系统用户数量上限：官方未文档化。** 本篇在 developers.facebook.com 的现行文档里没有找到任何一句写"最多能建多少个 system user"（附表 E）。

## 10.5 Embedded Signup：接入侧的三级数量

`embedded-signup/overview`（Oct 29, 2026）给的是**接入方每 7 天能带多少新客户进来**的数量，它与"账号有多少台设备"无关，但常被当成"能开多少个号"来引用，所以本篇把它列清楚：

> "By default, you can onboard up to 10 new business customers in a rolling 7-day window."

企业验证＋应用审核＋访问验证三者齐全后提升到 200；需要超过 200 时，官方给的下一步是"apply to become a Meta Business Partner"——**第三级不是配额而是一个资质申请**，本篇因此在附表 C 里把它写成 "10 → 200 → 合作伙伴" 一档。

该页另有五条与本篇相关的事实：客户可领取最多两个 **555 测试号码**（+1、555 区号、自动验证、**不能迁移、不能在平台外使用**）；**客户拥有自己的资产，接入方"you cannot restrict this access in any way"**；沙箱账号有效期 30 天；支持 30 种语言；ES v2／v3 已于 October 15, 2026 弃用。

第二条是政策层的一条硬约束：**接入方不能锁住客户对自己账号的访问权**。这与第 5 章"控制权归于持有号码的人"是同一条设计原则在商务侧的投影。

## 10.6 请求速率：三种计数轴，不要互相换算

`about-the-platform`（Aug 4, 2026）在一页里给了三组速率，**它们的计数对象各不相同**：

1. **按应用**：请求数以 **rolling one hour** 计，默认 **200 次／小时**（每个应用对每个 Messaging account），已验证且号码处于活跃状态的企业提升到 **5,000**；Credit Line API 单独给 5,000／小时。
2. **按收件人**：同一收件人**每 6 秒最多一条**，官方同时给出了换算表述——"about 10 messages per minute or 600 per hour"，超出报错误码 **131056**。
3. **突发**：可**在 6 秒内发最多 45 条**，机制是**借用未来的配额**，因此突发之后会紧跟一段被压低的速度。

该页还给了重试策略的表述 "4^X seconds"（指数退避）。第三组数字 **20 mps** 不在这一页而在 `throughput`（10.7），且明确只适用于**共存号码**。

**这三组数字不能互相换算成"每小时能发多少"**：第一组限的是接口调用次数（包括非发送调用），第二组限的是单个收件人的接收节奏，第三组是瞬时速度的上限。第 9.4 节还有一条独立的限流来源——**基于用户反馈与质量的系统限流**，它不在这三组数字里，也不由它们决定。

同一页还有一句与第 12 章呼应的条款式表述："Using unauthorized third-party tools is prohibited."

## 10.7 吞吐：80 到 1,000，以及它升上去要同时满足的三个条件

`throughput`（Jun 17, 2026）给的是**每秒消息数**这一根独立的轴：默认 **80 mps**，自动升级上限 **1,000 mps**，升级"can take up to 1 minute"，未达标时返回错误码 **130429**，档位状态变化通过 webhook 通知（升不上去时另有 **131057**）。

自动升级的三个条件必须同时成立：**无限额档（Unlimited）＋ 滚动 24 小时内触达 10 万及以上去重收件人 ＋ 质量分 YELLOW 或更高**。

同一页的另外三项规格与"会话／状态"有关，本篇一并归档：共存模式号码的吞吐**固定在 20 mps**；webhook 容量按"3 倍出站 ＋ 1 倍入站"配置；投递延迟中位数 ≤250 毫秒、超过 1 秒的比例 <1%；失败的 webhook **最长重投 7 天，采用指数退避**。

**"1,000 mps"是本篇认为最容易被误当成"限额"的一个数字。** 它是速率规格，不是允许发送的总量；总量由 10.9 的档位表决定，质量由 9.4 的反馈机制决定。三根轴混用会同时高估和低估平台容量——这与上一篇（群发功能口径篇）第 9 章给出的结论一致，本篇在新的登录态语境下重申一次。

## 10.8 共存模式：一个官方写明的"只同步 6 个月、且不含群组"

`onboarding-business-app-users`（Oct 29, 2026）描述 Business App 与 API 同时在线的"共存"形态，这是**平台层唯一一处把"会话／历史"写成可数对象**的文档：

> "All chat messages in the most recent 6 months can be synchronized"

同一页的三条限制同样重要：**群组聊天不同步**；一对一镜像，并通过 `is_on_biz_app` 与 `platform_type = CLOUD_API` 标识来源；**当号码同时被两侧使用时，Deregister API 被阻止调用**。

最后一条与第 2 章形成一组对照：**在消费者侧，主设备可以登出任何已链接设备；在商务侧，官方明确禁止在共存状态下用 API 注销号码。** 这是"谁有权终止会话"这一问题在两套体系里的不同答案。

## 10.9 限额档位：五档、一条用量判据、一个 6 小时时效

`messaging-limits`（May 21, 2026）的三要点：

**（1）档位是 250／2,000／10,000／100,000／Unlimited 五级**，作用层级是 **business portfolio，由该 portfolio 下多个号码共享**——这一点在本篇很重要，因为第 10.2 节的号码配额与第 10.6 节的请求速率**都不是 portfolio 共享的**。

**（2）进入 2,000 档的路径有两条**：完成企业验证；或"通过合作伙伴验证 ＋ 在 30 天滚动期内投递 2,000 条高质量模板消息"。

**（3）自动升档的判据是半用量：** 过去 7 天内"use at least half of your current messaging limit"且质量良好，满足后"**within 6 hours**"提升一档。

该页还记录了一次字段更名：`messaging_limit_tier` 已弃用，改用 `whatsapp_business_manager_messaging_limit`，取值形如 `"TIER_250"`。

## 10.10 一个关键的空白：单个 WhatsApp 账号能并发多少路 API 会话

本篇检索到的官方页面**没有写明"一个 WhatsApp 账号可以同时维持多少路 API 会话／多少个并发连接"**。相关的三处尝试（sessions／multi-device／on-premises 路径）本次不是 404 就是无正文（附表 E、附表 A 的未取回行）。

能找到的**最近的**官方表述只有四条，且没有一条是"并发会话数"：

1. 一个 WhatsApp 账号最多与 **5 家**方案商共享（10.3）。
2. 每个应用对每个 Messaging account 的请求数按 **rolling one hour** 计（10.6）。
3. 共存模式的号码吞吐**固定 20 mps**（10.8）。
4. 服务条款 (h) 项把多设备同时使用的合法性**限定在官方明确提供的工具之内**（9.1）。

本篇把这四条并置，但**不从它们推算任何并发上限**：份额、速率与并发数是三件不同的事，官方没有给换算式。


# 11 系统层对照：Apple 与 Google 把哪些事情规定在了应用之外

本章的作用只有一个：**区分"WhatsApp 没做"与"WhatsApp 做不到"。** 登录态、备份与迁移这三件事，有相当一部分决定权在操作系统与账户层。本章全部引用 Apple 支持文档、Apple iPhone 用户指南、Google 开发者文档、source.android.com 与 Google 账户支持页；凡本章出现的数字，出处均在附表 A 的 Apple／Google 分组里。

## 11.1 Apple：备份里有什么、谁加密、关掉之后留多久

`support.apple.com/en-us/108770`（Published Date: May 01, 2025）写明 iCloud 备份**包含第三方应用的数据**，其中点名了 messaging apps：

> "iCloud Backup includes app data for the apps that you've downloaded on your device. This includes data for third-party social media apps, games, and messaging apps, as well as apps from Apple that don't sync with iCloud."

同一页还给了两条与本篇直接相关的规则：**使用 iCloud Drive 存数据的应用，其数据不进 iCloud 备份**；以及一条保留期：

> "If you turn off iCloud Backup for a device, any backups stored in iCloud are kept for 180 days before being deleted."

**这条 180 天在 WhatsApp 的任何页面上都不存在**，但它决定了"我在 iPhone 上关掉的备份什么时候真的消失"。与 8.4 那句 Google "五个月" 并置，可以得到一条本篇认为重要的结论：**同一个"备份什么时候被删"的问题，答案由系统厂商分别规定，而且两家给的法子不一样——Apple 给了明确天数，Google 的 WhatsApp 侧页面给了约五个月，而 Android 自己的文档只写"一段不活跃期"（见 11.2）。**

加密归属在 `support.apple.com/en-us/102651`（January 05, 2026）：

> "For additional privacy and security, 15 data categories — including Health and passwords in iCloud Keychain — are end-to-end encrypted. Apple doesn't have the encryption keys for these categories, and we can't help you recover this data if you lose access to your account."

同一页区分了两种数据保护等级：**开启高级数据保护时"iCloud Backup and everything inside it"端到端加密；标准保护下备份密钥"are secured in Apple data centers"。** 这句是理解 8.1 那条"iCloud 备份与 WhatsApp 云存储互斥"的背景：**两份备份的密钥在两套完全不同的信任模型里**，把它们同时打开会得到两个可恢复路径不同的副本，而这正是"端到端加密"表述上最容易被抹平的地方。

账号恢复侧 `support.apple.com/en-us/108756`（April 17, 2026）写明，失去账号访问时需要用设备密码、恢复联系人或恢复密钥三者之一来恢复 iCloud 数据。**应用不能自建第四条恢复通道**——这一句与 8.2 的"WhatsApp can't send you a copy, reset it, or restore your backup for you"是同一件事在两侧的表述。

## 11.2 Google：Auto Backup 的三条硬规格，和它"只留最新一份"

`developer.android.com/identity/data/backup`（Last updated 2026-08-26 UTC）给了三句规格：Auto Backup 在 **Android 6.0 及以上**可用、把数据上传到用户的 Google Drive、**每个应用最多 25 MB**，以及备份"roughly once a day"自动发生。

`developer.android.com/identity/data/autobackup`（2026-10-26 UTC）给了排除项与保留规则：

> "Auto Backup excludes files in directories returned by getCacheDir(), getCodeCacheDir(), and getNoBackupFilesDir()."
>
> "Only the most recent backup is stored. When a backup is made, any previous backup is deleted. The backup data can't be read by the user or other apps on the device."
>
> "Obsolete datasets are automatically deleted after a period of inactivity."

第二句与 8.4 的 WhatsApp 页面原句 "Each new backup replaces the previous one." **是同一条规则的两种写法**——一条写在应用帮助里，一条写在系统开发者文档里。这是本篇找到的少数几处**跨层互相印证**的表述，值得作为"文本一致性"的正例记录。第三句则是本篇唯一能引到的 Android 侧过期规则，而它**没有给天数**（"a period of inactivity"），与 Apple 的 180 天形成精度差。

## 11.3 一句直接决定第 7 章读法的话

`autobackup` 页有一条属性说明，本篇认为是整个系统层对照里最有用的一句：

> "android:allowBackup=\"false\" disables cloud-based backup and restore (such as Google Drive backups) but doesn't disable device-to-device transfers for the app."

**关掉云备份不等于关掉机到机迁移——它们是系统里两条独立的通道。** 这条区分解释了为什么第 7 章会出现"三套规则"（备份恢复、Chat Transfer、跨端迁移）：它们在 OS 层就是不同的机制，各有自己的开关与前提。反过来看 8.1 里"WhatsApp 云存储与 iCloud 备份互斥"，那是**应用自选的一对**，不是系统限制。

## 11.4 机到机迁移的四条 MUST：系统替应用把住了入口

`source.android.com/docs/compatibility/16/android-16-cdd` §9.16（Last updated 2026-08-04 UTC）用兼容性条款的强制语气写了迁移前提，四条原文分别是：

> "[C-1-1] MUST NOT initiate transfers of application data from devices on which the user has not set a primary authentication…"
>
> "[C-1-2] MUST securely confirm the primary authentication on the source device and confirm with the user intent to copy the data on the source device before any data is transferred."
>
> "[C-1-4] MUST only migrate application data to the same application on the target device, with the same package name AND signing certificate."
>
> "[C-1-5] MUST show an indication that the source device has had data migrated by a device-to-device data migration in the settings menu."

这四条各自挡住了什么，值得逐句读：**没有设置主锁屏凭据的设备不允许发起迁移**（C-1-1）；**必须在源设备上确认用户意图**（C-1-2）；**同名同签名证书的应用才能接收**（C-1-4，即仿冒包名的应用拿不到数据）；**迁移过这件事必须在设置菜单里留下可见痕迹**（C-1-5，即事后审计线索）。

对照第 5 章会发现一条有意思的差别：**Android 在系统层要求"迁移过要留痕"，而 WhatsApp 在 4.4 引用的那句里明确说"无法提供谁在何时访问过账号"。** 前者是 OS 给的证据，后者是应用拒绝给的证据——两条不矛盾，但对自查的人来说，可依赖的对象完全不同。

## 11.5 迁移前提的两侧对照：谁要求"旧机在场且已解锁"

第 7 章列的那一串迁移条件（新手机必须未注册、两台手机靠近、必须扫旧机上的二维码……）在系统层各有一条对应规定，本篇把它们并排：

Apple 侧 `support.apple.com/en-us/102659`（September 14, 2026）的"快速开始"前提写得很具体：

> "Make sure that your current device is connected to Wi-Fi and Bluetooth is on. Unlock your current device and navigate to the Home Screen. Turn on your new device and place it near your current device."
>
> "If you transfer directly from your previous device, you'll need to wait for the transfer to complete on both devices before you can use them."

同一组页面里的 `support.apple.com/en-us/119967`（December 05, 2025）另加一句前提："Have your Apple Account password ready."

Google 侧除了 11.4 的四条 MUST，还有一条很容易踩的细节，`support.google.com/android/answer/13769784` 写明：**已经同步到账户的联系人和日历事件不会在配机时再搬一次**（"Only contacts and calendar events that aren't synced to an account are transferred."）。

三条合起来给本篇第 7 章补上了一条读法：**WhatsApp 的迁移条件里，凡是"两台设备靠近""旧机要能扫码""新机要处于未注册状态"这类要求，都是在配合系统层的前提，不是应用自己设的门槛。** 反过来说，7.4 表里那个"通话记录只在 iPhone→Android 方向能过去"的差异，则是应用侧的选择——系统层没有规定这个。

## 11.6 CE 存储：系统层对"登录态决定数据可得性"最直白的一句

如果要在整篇里挑一句最能定义"登录态"这个词的系统级表述，本篇选 `source.android.com/docs/security/features/encryption/file-based`（2026-10-30 UTC）：

> "Credential Encrypted (CE) storage, which is the default storage location and only available after the user has unlocked the device."

`developer.android.com/privacy-and-security/direct-boot`（2026-10-28 UTC）重复并收窄了它："Credential encrypted storage is available after the user has successfully unlocked the device and until the user restarts the device."

这两句给出的模型是：**数据的可得性由"设备是否已被本人解锁"门控，而不是由应用内是否登录门控。** 应用层的一切会话概念都坐在这一层之上。它也是本篇对第 6.2 那句 "Trusted devices are different from linked devices" 的系统侧注解：设备 unlock 是比 app login 更靠前的一个状态。

至于 FRP，`developer.android.com/work/dpc/security`（2026-10-23 UTC）写的是恢复出厂后配机时"requires the user to authenticate against any Google Accounts that were previously on the personal profile of the device"，其目的该页直写为防盗（"designed to deter device theft"）。

## 11.7 关键对照：两家都有"登出该设备名下的所有会话"，WhatsApp 只对 Web 有

这是本章最想留下的一条比较。

Google 账户支持页 `support.google.com/accounts/answer/3067630` 先定义了会话，再给出一个**以设备名为单位的批量登出**：

> "A session is a period of time during which you're signed in to our Google Account from a browser, app, or service on the device."
>
> "To make sure there's no account access from another device, sign out of all the sessions with this device name."

Apple 的 `support.apple.com/en-us/102649`（2026-09-14）同样给了账号级的设备撤销，并写明后果：

> "Devices that you remove from your account won't have access to iCloud, Find My, or other Apple services, and won't display verification codes for two-factor authentication."
>
> "You can sign out of your Apple Account remotely, if needed."

把这两句与 4.4、5.1 并排读，结论是本篇最有分量的一条：

| | 撤销粒度 | 是否支持"按设备批量撤销" | 撤销后附带切断什么 |
|---|---|---|---|
| Google 账户 | 按**设备名**下的全部会话 | **是**（sign out of all the sessions with this device name） | 该设备名下所有浏览器、应用与服务的会话 |
| Apple 账户 | 按**设备** | **是**（remove device／远程登出） | iCloud、Find My、其它 Apple 服务，以及**二步验证码的接收能力** |
| WhatsApp | 按**单台已链接设备**逐台 Log Out；批量登出**仅覆盖 WhatsApp Web 会话** | **否**（帮助中心无此入口，见附表 E） | 仅该设备上的 WhatsApp 会话 |

Apple 那句里的细节值得再指一次：**从账户里移除设备会同时剥夺它接收二步验证码的能力**——这是账户层对"验证码等于控制权"（5.2）这个模型的直接加固。WhatsApp 侧没有等价物：一台被登出 WhatsApp 的 iPhone 仍然可以接收短信验证码，因为验证码走的是电信通道而不是账号通道（6.1）。

同一份 Google 文档还有一条与 5.5 对照的表述：`answer/6160491` 写明远程抹除后**要再次使用设备需要该 Google 账户的密码**。

## 11.8 三条对照结论，与一条必须打的限定

**结论一：** 第 8 章的备份删除时点、第 11.3 的"云备份与迁移是两条通道"、第 11.4 的迁移前提与留痕，**都由系统厂商规定，WhatsApp 页面上找不到**。只读帮助中心会把这些当成应用行为。

**结论二：** 第 5 章"逐台登出"的笨拙，不是 WhatsApp 的实现偷懒——**整个行业的账号体系里，只有把设备纳为一级对象的账户层（Apple／Google）才提供按设备批量撤销**。WhatsApp 的对象是号码，设备只是号码的附属。

**结论三：** 8.2 的通行密钥选项把 WhatsApp 的端到端备份**接到了一个 Apple/Google 管理的体系上**：iPhone 用户指南 `use-passkeys-to-sign-in-to-apps-and-websites` 写明 passkey 存于 iCloud 钥匙串且**必须开启 iCloud Keychain 与双重认证**（"To use passkeys, iCloud Keychain and two-factor authentication must be turned on."），也可保存到硬件安全密钥。**"端到端加密的备份"与"备份密钥由谁能恢复"是两件事**，选择通行密钥就等于把恢复依赖交给了系统账户。

**限定：** 本章只对照**官方文本写了什么**。两家的机制在实现上是否真的如文档所述，不在本篇范围内；`support.google.com/android/answer/13626960` 那句"App data … Available at app developer's discretion"提醒了一件事——**系统给了通道，不等于每个应用都选择使用它**，本篇未在 WhatsApp 页面找到对应承诺。


# 12 封禁层：官方把哪些行为写成成因，以及处置之后还能做什么

## 12.1 临时封禁页给出的两个成因，其中一个是"抓取"的定义句

`1848531392146538`（Temporarily banned accounts）把临时封禁的成因写成两类：使用**非官方版本**，或

> "harvesting information, known as scraping"

同一页给了 scraping 的定义句，这是本篇认为全篇最值得单独抄下来的一句规则文本：

> "Scraping is the automated extraction of information, both targeted and at scale, for any unpermitted purposes."

注意这句的三个限定词：**automated**（自动化）、**targeted and at scale**（定向与批量两种都算）、**for any unpermitted purposes**（未经授权的用途）。三者不是并列的"且"关系，而是**在同一句里把自动化的信息提取整体定义为抓取**。同页还写明：不切换到官方版／不停止该行为可能导致**永久封禁**。

**该页没有任何时长数字。**"临时封禁多久"在官方文本里是空白（附表 E），这与 12.3 的"复核 24 小时"是两个不同的时间对象——一个是封禁本身的长度，一个是申诉处理时长。

## 12.2 非官方应用页：三类具名例子与两条系统消息

`1217634902127718`（About unofficial WhatsApp apps）的口径链条是：第三方未经批准构建的版本 → 使用或链接它违反服务条款 → 且 **"we can't validate their security or privacy practices"**。该页点名了几类非官方客户端（GB WhatsApp、FM WhatsApp、TeleMessage、TM WhatsApp），并列出两条用户会看到的系统消息原文：

> "You need the official WhatsApp to log in"
>
> "You have been logged out for using an unofficial app."

第二句对第 5 章的自查有直接用处：**"被登出"本身可能是一条有明确成因文案的事件**，而不必猜。该页还有一条与本主题最相关的后果表述：违规使用的后果包括**链接设备的能力**（"…including the ability to link devices."）——也就是说，第 2 章那"四台"的额度不是一项无条件权利，它可以被单独收回。

同一页特别写明：**官方的 Agents 功能与这些非官方应用是两回事**（该页原文此处有拼写错误 "unoffical"，本篇照抄记录）。这句是 2.5 与 12.1 之间的官方连接器：**"多人共用一个账号"这件事，官方版走 multi-agent，非官方版走被抓取定义覆盖的第三方客户端**——两条路径都在条款里，但一条被文档化、一条被定义为违规。

相关页面还有 `1064395290901991`（因非官方应用被封禁）、`723378546580115`（Business 应用封禁）与 `1508178557633269`（平台封禁申诉）。

## 12.3 永久封禁的申诉流程：一号一次，且没有加急

`465883178708358` 给出了封禁弹窗的**逐字文案**，本篇认为这是全篇最"可核对"的一句——任何用户都能拿它和自己的屏幕比对：

> "This phone number can't use WhatsApp: [phone number]. You can request a review of this number. Most reviews are completed within 24 hours."

同页的五条限制与流程一起构成完整口径：

- 通过 **Tap Request review** 提出；**弹窗里没有这个选项就是最终决定，不可申诉**。
- **"We only review one phone number per appeal."**——一次申诉只处理一个号码。
- 通过该流程之外的渠道联系**不会加速**处理。
- **"You can't access your chat history or backups while your account is banned."**
- 第三方服务**没有**封禁或解封能力；新号码可能继承前主人的违规记录；越狱／root 设备与不受支持的设备／应用不在支持范围内。

最后两条与 5.7 合成一个闭环：**平台既不提供取证，也不承认第三方能处置，而封禁期间连本地之外的数据通道（聊天历史与备份）都一并关闭。** `1508178557633269` 另有一句同方向的表述："WhatsApp Support does not provide support related to account bans…"。

## 12.4 一句需要防止被读错的对照

第 3.4 节提到私人群组的邀请链接 **3 天**后失效。这个数字属于**群组对象的生命周期**，与账号登录态、设备会话或封禁时长都无关。本篇把它写在这里，是因为它是本章唯一一个"看起来像封禁时长"的数字——**它不是**，而"临时封禁多久"这个真正的问题，官方页面没有答案。


# 13 十组口径冲突：两份官方文本写法不同时，本篇怎么处理

附表 F 把这十组差异逐条列成了表。本章要做的是另一件事：**说明每一组差异属于哪一种"不同"**，因为读者拿到这些页面时最常见的动作是"挑一句看起来更新的来引用"——而本篇的十组里没有一组允许这么做。

处理原则只有一条，与第 1.2 节相同：**两句都抄，不合并、不取其一为准。** 下面按冲突性质分三组说明为什么这条原则在这些地方是唯一不引入新错误的做法。

## 13.1 两组"数的不是同一个东西"：主语差异与枚举不闭合

**第一组是"四台"的主语。** 三页分别写成 linking up to four devices **to your primary phone**（`378279804439436`）、Connect up to four devices **to your account**（`395911122612120`）、four linked devices **and one phone**（`647349420360876`）。三个主语不是同义改写：绑在主手机上，意味着主手机在额度之外；绑在账号上，意味着主手机是否占用额度这句话没有回答；写成"四加一"，则是明确并列。三句都不能被压缩成"账号最多四台设备"——那会把第一句的限定丢掉的量补回来，同时把第三句已经写明的并列关系拆散。

**第二组是同一页内部的枚举不闭合。** `378279804439436` 列出不能作为主设备的终端时，只点了 Windows、Mac、Web、iPad 与可穿戴设备；同一页的另一句又写明 Android 平板**注册之后可以作为主设备**。这不是两句互相否证——第一条是"排除清单"，第二条是"能力陈述"，只是清单没有写完。处理方式是把 Android 平板写成"官方文本里唯一非手机形态的主设备"，并同时说明这个结论来自同页另一句、不来自那份排除清单。

这两组的共同点是：**看起来像数字冲突，实际是宾语冲突。**"四"这个数字在所有页面都一样，不一样的是它修饰的是谁。

## 13.2 三组"措辞与版本"：用词差异与两处版本问题

**第三组是 unused 与 offline。** 官方原句是 if your phone is **unused** for over 14 days（`378279804439436`）。"主手机离线 14 天"这一转述在官方页面上找不到原句（附表 E 记录了这次检索）。二者的差别不是文字游戏：一台只是关机、但仍在被使用的手机，与一台"未被使用"的手机，在 14 天这条线上不一定给出同样的结果。本篇全篇只用 unused，并且不把它翻译成"离线"。

**第四组是两步验证凭据形态的换代。** `1920866721452534` 写的是把 6-digit PIN **升级为 password**，`2183055648554771` 给的新口令规则是至少 8 个字符、含 1 个字母与 1 个数字，而同一页里仍保留"6 位邮箱验证码"。三句是同一体系内**新旧文本并存**的状态。所以本篇不回答"两步验证到底是几位"——这个问题目前没有唯一正确答案；每次引用都标注它属于换代期的哪一种表述。

**第五组是端到端备份密钥的写法。** 现行页 `490592613091019` 写 **64-digit** encryption key；更早流传的 32 个字符口径，本篇在任何现行页面都没有取回。这里的关键不是哪个数字对，而是**官方没有一页说明过这次变更**（附表 E 第 13 项）。因此本篇只引现行页数字，且明确写出"旧口径的来源页未找到"，不写成"官方从 32 位改为 64 位"。

这三组的共同点是：**它们都是时间造成的差异，而不是页面之间的矛盾。** 处理它们的唯一办法是把版本状态写进引用，让读者知道自己抄的是哪一版。

## 13.3 五组"不在同一根轴上"：量纲、层级、精度、作用轴、范围

**第六组，量纲不同。** `378279804439436` 写桌面端群通话 up to **32 people**，`668538004658079` 写 **Unlimited calling**。前一句数人，后一句数功能，不构成反驳关系。本篇把 32 归入附表 C 的硬数量上限档，同时明确拒绝把 "Unlimited" 读成"桌面端通话无人数上限"——那是把功能描述当成了数量规格。

**第七组，层级不同。** 帮助中心 `865384580786591` 说商业组合账龄超过 **90 天**可能被强制开启两步验证且无法关闭；而 9.2 那三份条款文本只写了"维护其账号与登录凭据的安全"，**没有出现两步验证义务**。一个是帮助中心说明，一个是合同条款，两者不互相覆盖。本篇的写法是两句并列引用，并明确一句结论：**强制来自帮助页，不来自合同文本。**

**第八组，精度不同。** `access-tokens` 只说用户令牌 "expire quickly… every few hours"，Facebook Login 文档给的是短期约 **1–2 小时**、长期约 **60 天**，并列出部分类型不按时间过期。一份给量级、一份给近似值、一份给例外——三者相加仍不等于"WhatsApp 令牌寿命"。本篇三条同时列出，不给单值。

**第九组，作用轴不同。** **2,000** 这个数在 `messaging-limits` 是五档限额表的**第二档**，在 `phone-numbers` 是触发号码配额从 2 升到 20 的**前提**。同一个数字，一根是总量档位轴，一根是触发门槛轴。附表 C 给它开了两行（这也解释了为什么附表 C 的行数不等于数字数），正文每次引用都注明是哪一根轴。

**第十组，范围不同。** `1428782138011916` 的批量登出对象是 "all your active **WhatsApp Web sessions**"，同一页对其它设备写的是逐台重复步骤（"Repeat these steps for all linked devices"）。**一句批量、一句逐台，覆盖对象也不同。** 本篇不接受"一键登出所有设备"这种概括——按官方文本，批量这个动作只存在于 Web 会话这一类。

这五组的共同点最值得单独说：**它们都不是"某一页写错了"。** 量纲、层级、精度、作用轴、范围这四种不同，都属于两份文本各自正确、拼在一起才产生歧义的情形。这类情形正是引用官方文档时最容易出错的地方，因为它看起来像是有个正确答案可选，实际上两个答案都对、只是回答的不是同一个问题。

## 13.4 十组冲突里没有任何一组是数字算错

把十组排一遍：**三组是主语或范围不一致，三组是版本或措辞状态不一致，四组是轴不一致。** 没有一组的形式是"甲页写 4、乙页写 5"。

这个分布本身就是一条结论：**WhatsApp 的公开文本在数字层面是自洽的，问题全部出现在数字所绑的对象、时间与层级上。** 所以引用这些页面时的风险不是抄错数，而是把 A 页的数搬到 B 页的宾语上——第 1 章立的"数字必带出处"这条规矩，就是为了在这种搬动发生时能被看出来。

# 14 结论

## 14.1 十个问题，与它们各自动落在哪一层

**一、一个账号同一时刻能在几台设备上在线？** 帮助中心的答案是**四台已链接设备**，而这句话绑的主语是"链接到你的主手机"（`378279804439436`）；Business 应用那页把它写成"四台链接设备**与**一台手机"（`647349420360876`）。开通 multi-agent 后这条线挪到 **10 台**（`395911122612120`），Meta Verified 与 Meta One 的订阅页给出 **4／6／8／10** 四档对应表（`3872729742954601`）。**这一层属于产品能力。**

**二、14 天与 30 天是同一条规则的两半吗？** 不是。14 天绑的是**主手机未被使用**，30 天绑的是**那台已链接设备自身的闲置**，两句在同一页里分别成立、主语不同（第 3.1 节）。同一页之外还有两个 14 天：一个是"需要多久在主手机上登录一次"，一个是"主设备登出后已链接设备仍连接 14 天"。**四个 14 天指三件事**，这是本篇最容易读错的一组。

**三、已链接设备上什么事情做不了？** 官方逐项列了清单（第 4.1 节），并在通话上给过一个具体数字：**桌面端群通话最多 32 人**。历史同步的三句原话给了三个不同范围，其中"一年"那句是**缺陷描述里的期望上界**，不是产品规格（第 4.3 节）。**这一层属于能力与实现差异。**

**四、怀疑账号被别人访问时，官方给的第一动作是什么？** 审查已链接设备清单并登出——但**批量登出只覆盖 WhatsApp Web 会话**，其它设备要逐台重复步骤；而真正"收回全部访问"的主路径是**用同一号码重新注册**，配合那句 "anyone who might be using your account is automatically logged out"（`506595211487528`）。同时官方明确否认了一件事：**改口令不会把别人移出去**。**这一层属于账号恢复，且它的支点在第 12 章的封禁层之外——官方不提供取证。**

**五、两步验证到底几位？** 目前没有唯一答案：官方一页写"把 6 位 PIN 升级为 password"，另一页给至少 **8 个字符、含 1 个字母与 1 个数字**，同一页又保留 6 位邮箱验证码。**这是换代期文本并存**，本篇按第 13.2 节的规矩逐条标注，不合并。另有一句区分必须抄下来：**"Trusted devices are different from linked devices."**

**六、换号与换机时，什么东西跟号走、什么跟设备走、什么跟云账号走？** 第 7.5 节把官方散在七页的表述压成一张三分表。跨端迁移两个方向**不对称，而唯一的不对称项是通话记录**（Android→iPhone 与 iPhone→Android 的规则不同，第 7.4 节）。

**七、备份由谁保管、什么时候消失？** 三种存储、两个管理方；端到端备份的现行写法是 **64 位**加密密钥；Google 侧写明未更新的备份约**五个月**后被自动删除，Apple 侧写明关闭设备备份后旧备份保留 **180 天**。**这些删除时点由系统厂商规定，WhatsApp 页面上找不到**（第 11.8 节结论一）。

**八、政策层允许"多设备访问"与"自动化"吗？** 服务条款把这两件事都写了：多设备只在**通过 WhatsApp 明确提供的工具**这一条件下被允许；**bulk messaging、auto-messaging、auto-dialing** 与未经同意的自动化并列在同一条义务里。2026 年 9 月 23 日同日生效的三份新条款把 Business Platform 与 Business App 分别成文。**这一层属于合同，不属于功能。**

**九、企业协作层把"人"建模成了什么？** 两种角色（admin／employee）的 system user、两类访问令牌、以及四条配额轴：号码 **2→20**、模板 **250→6,000**、共享方案商 **5 与 2**、接入方 **10→200→合作伙伴**。**这一层是本篇唯一一处"人"变成可枚举对象的地方**，也与第 2 章那"四台"完全不在同一根轴上。

**十、iOS 与 Android 有区别吗？** 帮助中心的上限数字**不分端**；差异全部在系统层：备份包含什么、机到机迁移的四条 MUST、迁移前提是否要求旧机在场且已解锁、CE 存储与登录态的关系，以及"登出该设备名下全部会话"这一动作——**Apple 与 Google 都按设备写了，WhatsApp 只对 Web 会话写了批量。**

**这十个答案合起来的一条总观察：官方文本里没有"会话管理器"这个统一对象。** 附表 B 把"登录态"拆成**八种**各自管理的东西，分属**四套**撤销体系——设备清单、订阅权益、身份口令、平台授权；其中只有前两种在帮助中心有"台数"写法，其余六种要么按订阅、要么按角色、要么根本没写上限。第 5.1 节那条"逐台登出"的路径之所以显得笨拙，原因在这里：**不是实现偷懒，而是对象模型本来就没有把设备当作一级对象**（另见第 11.8 节结论二）。任何"查看并撤销全部登录会话"的期待，都要先落到这八种对象里的某一种上，才有官方文本可依据。

## 14.2 四根不能互相换算的轴，与本篇刻意不进归档表的数字

本篇出现的数字分属四根轴，**任何跨轴换算都不被官方文本支持**：

**轴一：并发台数。** 数的是"同一时刻几台终端在线"，单位是台。4、1、10、6、8、2 都在这一根上。
**轴二：时长与窗口。** 数的是"多久不使用会断开""多久之内完成复核""等待几天"，单位是天或小时。14 天、30 天、7 天、24 小时、90 天、180 天、5 个月都在这一根上，但它们**各自绑不同的主语**（第 3.2 节的教训就是不要以为同数即同事）。
**轴三：配额与档位。** 数的是 portfolio、号码、模板、方案商、每秒消息数，单位是人、个、条、mps。250／2,000／10,000／100,000／Unlimited、2→20、250→6,000、80→1,000 mps、45 条／6 秒都在这一根上。
**轴四：处置与状态。** 数的是阶梯级数与时长，且这些时长是**结果**不是**限额**：1 或 3 天封锁 → 5／7／30 天 → 无限期 → 永久停用。

附表 C 把这四根轴上的数字归成五档，共 **86 个条目**（硬数量上限 **25**、时间窗口与时长 **31**、默认可改 **7**、条件触发 **11**、分档晋级 **12**）。**86 个条目只对应 64 个去重后的数字**——因为有 **13** 个数字在不同轴上各占一行，其中"30 天"占 **5** 行、"14 天"占 **4** 行、单独一个"2"和单独一个"4"各占 **4** 行。所以**引用本篇任何数字时必须同时报它在哪一根轴上**，"30 天"这个词在本篇出现的五次里，没有两次说的是同一件事。

另有几类数字**刻意不进附表 C**，理由如下：

- **Android 9 与 Google 移动服务版本 241217000**（第 8.2 节）：这两项是**通行密钥路径的设备与系统门槛**，不是 WhatsApp 对账号或设备对象设定的上限，且随 Google 服务版本滚动变化。
- **私人群组邀请链接的 3 天**（第 3.4、12.4 节）：属于群组对象的生命周期，与登录态、设备会话、封禁时长都无关。本篇专门在 12.4 提醒它容易被当成封禁时长。
- **各页面的发布日期、生效日期与最后修改日期**：第 1.2 节规定"页面自带日期时抄下日期"，但这些是**版本信息**，不是限制。2026 年 9 月 23 日、2021 年 1 月 4 日这类日期只用于判定哪一版文本现行。
- **错误码（130429 等）、状态枚举与角色枚举**：不进数字档，归入附表 D。
- **文章 ID**（如 `378279804439436`）：标识符，不是数量。
- **本篇自指计量的数字**：附表行数、章节数、引用页数——它们描述的是本篇文本自身，与 WhatsApp 无关，因此也不进 C 表。

## 14.3 留白比冲突多，说明这个主题的公开文本密度较低

本篇的三个自指计数放在一起看，能读出这一主题与"群发功能"那篇的区别：

- 附表 A 收录 **81** 页，其中 **5** 页正文本次未取回；
- 附表 E 列出 **28** 项官方未文档化的问题（规模与可见性 6、规则与判据 11、读法与措辞 5、本次读取失败 6）；
- 附表 F 列出 **10** 组页面之间的口径冲突。

**留白（28）是冲突（10）的近三倍。** 这个比例意味着：在登录态与设备绑定这个主题上，公开文本的主要缺口不是"两页互相矛盾"，而是**问题本身没有被回答**——临时封禁多久、是否存在一键登出全部已链接设备的入口、单个账号能并发多少路 API 会话、system user 的数量上限，这四件事在官方页面上都找不到答案。

而已经写下的那些表述，其精度分布也很不均匀：**"最多四台"这类是硬规格**，"unused for over 14 days"这类是**条件句**，"can take up to 1 minute""typically takes 24 to 48 hours"这类是**近似值**，"expire quickly… every few hours"这类只是**量级**。本篇把这四种精度原样保留，不做统一化改写——把近似值抄成规格，是把官方文本读错的另一条常见路径。

本篇不做的事，写在文末的边界声明里，也写在第 1.4 节的负面清单里。

# 附表 A 官方页面引用清单（82 行）

分组计数：**帮助中心 40、法律与政策 6、Meta 商业平台与商务管理 18、Apple 7、Google 10**，五组相加 **81** 页；其中 **5** 页本次正文未取回，全篇不引用其内容（帮助中心 3 页、Meta 层 2 页）。帮助中心页面**一律不显示"Last updated"字段**（本次以正则逐页核验），因此该组的"日期"列留空属于口径而非疏漏。

| 分组 | 页面 ID／路径 | 页面主题 | 支撑章节 | 取回状态 |
|---|---|---|---|---|
| 帮助中心 | `378279804439436` | About linked devices（设备数量、14／30 天、能力缺失、32 人通话、已知问题） | 2.1、2.2、3.1、4.1、4.2、4.3、5.1、附表 B | 已取回 |
| 帮助中心 | `1317564962315842` | How to link a device（每 14 天登录、未识别设备确认） | 3.1、3.2 | 已取回 |
| 帮助中心 | `1046791737425017` | About linking WhatsApp to a second phone（副手机文案、实时位置不可用） | 2.3、3.1、3.2、4.1 | 已取回 |
| 帮助中心 | `647349420360876` | Business 应用的已链接设备（四台＋一台手机、状态增删不可用、两段 Log out） | 2.1、2.4、4.1、5.1 | 已取回 |
| 帮助中心 | `1421067466005147` | 在主设备上登出（两个账号、登出后 14 天） | 2.6、3.2 | 已取回 |
| 帮助中心 | `834124628020911` | Unlink a device（两端都可解除链接） | 3.3、5.1 | 已取回 |
| 帮助中心 | `1428782138011916` | 检查并解除未识别设备（Web 会话批量登出、无法提供访问者信息） | 4.4、5.1、5.7 | 已取回 |
| 帮助中心 | `653480766448040` | 已链接设备上的消息历史（链接时加密副本、Desktop 多于 Web、删除同步） | 4.3 | 已取回 |
| 帮助中心 | `668538004658079` | About WhatsApp Web（含 unlimited calling 表述；桌面安装页重定向至此） | 4.2 | 已取回 |
| 帮助中心 | `451924530376167` | About WhatsApp Desktop（专页） | — | **未取回** |
| 帮助中心 | `2366980643469578` | About supported devices（Android 6.0 下限、可链接终端清单） | 2.7 | 已取回 |
| 帮助中心 | `395911122612120` | About multi-agent（4／10 台对比表、订阅决定上限、降级保留四台、来电路由） | 2.5、附表 B | 已取回 |
| 帮助中心 | `3872729742954601` | About Meta Verified for Business on WhatsApp（四档 4／6／8／10 台） | 2.5.2 | 已取回 |
| 帮助中心 | `975910528178219` | About Meta One plans（只写 More linked devices，无台数） | 2.5.2 | 已取回 |
| 帮助中心 | `1131457590844955` | 群组邀请权限设置（默认 Everyone、只管未来、邀请链接、3 天失效） | 3.4、4.1 | 已取回 |
| 帮助中心 | `3307102709559968` | 隐私设置默认值（谁可见、已读回执、对等规则、号码不可隐藏） | 3.5 | 已取回 |
| 帮助中心 | `498754122134583` | 更换号码步骤（通知三选项、被拒号码类型、须在主设备操作） | 7.2 | 已取回 |
| 帮助中心 | `1166321223998129` | About changing phone numbers（传输清单、备份作废、不可回退） | 7.1 | 已取回 |
| 帮助中心 | `1197347060992858` | About changing phones（三条路径、先改号、重注册顶替、等待不可加速） | 5.7、7.3 | 已取回 |
| 帮助中心 | `209942271778103` | Android→Android 转移聊天（条件、精确定位、消息暂停、30 秒、Business 不支持） | 7.3 | 已取回 |
| 帮助中心 | `686469079565350` | Android→iPhone 迁移（Move to iOS、出厂全新、SDK 21／iOS 15.5、通话记录不迁） | 7.4 | 已取回 |
| 帮助中心 | `1295296267926284` | iPhone→Android 迁移（通话记录可迁、显示名不可迁、无合并） | 7.4 | 已取回 |
| 帮助中心 | `481135090640375` | 备份聊天历史（三种存储、频率、三个开关、只认最后一次成功） | 8.1、8.3 | 已取回 |
| 帮助中心 | `407643231403807` | Google 账号备份（覆盖规则、五个月删除） | 8.4 | 已取回 |
| 帮助中心 | `490592613091019` | 端到端加密备份（三种保护物、64 位、2 GB、不可恢复、通行密钥前提、用量信息） | 8.2 | 已取回 |
| 帮助中心 | `743219147158705` | 通话记录（存在聊天数据库、仅随备份恢复、不可导出） | 7.4、8.5 | 已取回 |
| 帮助中心 | `1278661612895630` | 两步验证（可选、防未授权访问、trusted≠linked） | 6.2 | 已取回 |
| 帮助中心 | `1920866721452534` | 管理两步验证（PIN 升级为 password 的说明、设置路径） | 6.1、6.3 | 已取回 |
| 帮助中心 | `2183055648554771` | 忘记密码（30 天两档后果、几天后解锁、不可撤销、8 字符、三种验证） | 6.4 | 已取回 |
| 帮助中心 | `1082491466048496` | 邮箱验证码与重置两步验证（七天等待、无需在网 SIM、仅 SMS／电话首发） | 6.1、6.5 | 已取回 |
| 帮助中心 | `506595211487528` | 注册与两步验证（6 位注册码、重注册顶替他人、开启延迟） | 5.2、6.1 | 已取回 |
| 帮助中心 | `1131652977717250` | 恢复被盗账号（改密码不移除他人、7 天、SIM 顺序、举报风险、三条做不到） | 5.2–5.7 | 已取回 |
| 帮助中心 | `465883178708358` | 账号封禁（弹窗原文、24 小时复核、一号一申诉、封禁期不可访问备份） | 12.3 | 已取回 |
| 帮助中心 | `1848531392146538` | 临时封禁（非官方版本、scraping 定义句、无时长） | 12.1 | 已取回 |
| 帮助中心 | `1217634902127718` | About unofficial WhatsApp apps（具名例子、两条系统消息、与 Agents 的区分） | 12.2 | 已取回 |
| 帮助中心 | `1064395290901991` | 因使用非官方应用导致的封禁 | 12.2 | 已取回 |
| 帮助中心 | `723378546580115` | Business 应用的封禁 | 12.2 | 已取回 |
| 帮助中心 | `1508178557633269` | 平台封禁申诉（客服不处理封禁事务） | 5.7、12.3 | 已取回 |
| 帮助中心 | `1007324800132703` | 手机丢失或被盗 | — | **未取回** |
| 帮助中心 | `717472490411581` | 受限账号（restricted accounts） | — | **未取回** |
| 法律与政策 | `whatsapp.com/legal/terms-of-service` | 服务条款（Effective January 4, 2021）：注册义务、(e)(f)、危害条款 (c)(e)(h)(i)(j)、30 天未送达 | 9.1 | 已取回 |
| 法律与政策 | `WhatsApp Terms for WhatsApp Business Platform` | 平台条款（Effective September 23, 2026）：§3、§4、§9、欧盟／英国 30 天通知、终止后 90 天保留 | 9.2 | 已取回 |
| 法律与政策 | `Meta Terms for WhatsApp Business Platform` | Meta 侧条款（Last Modified September 23, 2026）：§2.2、§4.1(a)–(h)、§4.7、§3.1 | 9.2 | 已取回 |
| 法律与政策 | `WhatsApp Terms for WhatsApp Business App` | Business 应用条款（Effective September 23, 2026）：限制项 (b)(g)、§6(c)(h)、终止后不得新开 | 9.2 | 已取回 |
| 法律与政策 | `WhatsApp Messaging Guidelines` | 消息指南（Last updated September 23, 2026）：adversarial behavior 定义句、自动化＋人工复核 | 9.3 | 已取回 |
| 法律与政策 | `business.whatsapp.com/policy`（301→`whatsappbusiness.com/policy`） | Business Messaging Policy：opt-in 两要件、24 小时窗口、六种人工升级路径、按反馈限流（**页面无日期字段**） | 9.4 | 已取回 |
| Meta 开发者 | `whatsapp-business-accounts`（Aug 5, 2026） | WAAC 与消息账户、2→20 号码、20 层级、250→6,000 模板、5／2 家方案商共享 | 10.2、10.3 | 已取回 |
| Meta 开发者 | `phone-numbers`（May 21, 2026） | 新组合初始两个号码、验证或达 2,000 自动升到 20、connected 状态、API 不能关两步验证 | 6.6、10.2 | 已取回 |
| Meta 开发者 | `account-model-evolution`（Nov 2, 2026） | WABA 拆分、GA 时间、归属对照表、号码不可跨伙伴迁移 | 10.1 | 已取回 |
| Meta 开发者 | `access-tokens`（Sep 19, 2026） | 系统用户 admin／employee、full／partial、双层授权、错误码 200、令牌寿命 | 10.4 | 已取回 |
| Meta 开发者 | `two-step-verification`（Jun 28, 2026） | 注册号码必须设 6 位 PIN、无禁用端点、PIN 滚动弃用 | 6.6 | 已取回 |
| Meta 开发者 | `embedded-signup/overview`（Oct 29, 2026） | 滚动 7 天 10→200→合作伙伴、两个 555 测试号、客户拥有资产、沙箱 30 天、30 语言、ES v2/v3 弃用 | 10.5 | 已取回 |
| Meta 开发者 | `onboarding-business-app-users`（Oct 29, 2026） | 共存模式、最近 6 个月同步、群组不同步、字段标识、Deregister 被阻止 | 10.8 | 已取回 |
| Meta 开发者 | `about-the-platform`（Aug 4, 2026） | 滚动 1 小时计数、200→5,000 次、6 秒同收件人、45 条突发、4^X 重试、禁止未授权第三方工具 | 10.6、12.2 | 已取回 |
| Meta 开发者 | `throughput`（Jun 17, 2026） | 80→1,000 mps、131057、共存 20 mps、webhook 容量与延迟、7 天重投、升级三条件 | 10.7 | 已取回 |
| Meta 开发者 | `messaging-limits`（May 21, 2026） | 五档限额、portfolio 层级共享、2,000 档两条路径、半用量判据、6 小时、字段更名 | 10.9 | 已取回 |
| Meta 开发者 | `policy-enforcement`（May 21, 2026） | 四级处罚阶梯、立即下线情形、24–48 小时、Unchanged／Reversed、并非全部可申诉 | 9.5 | 已取回 |
| Meta 开发者 | `getting-opt-in`（Jun 16, 2026） | opt-in 三要件、November 2024 政策更新锚点、质量持续低则限流 | 9.4 | 已取回 |
| Meta 开发者 | `send-messages`（Nov 4, 2026） | 24 小时窗口由消息或通话重置 | 9.4 | 已取回 |
| Meta 开发者 | Facebook Login 令牌生命周期文档 | 短期约 1–2 小时、长期约 60 天、部分令牌不随时间过期 | 10.4 | 已取回 |
| Meta 商务 | `facebook.com/business/help/865384580786591` | 商业组合两步验证：广告主可能被要求、>90 天强制且不可关、三个选项、需个人身份 | 6.7 | 已取回 |
| Meta 商务 | `facebook.com/business/help/280940009201586` | 商业组合两步验证管理入口与权限 | 6.7 | 已取回 |
| Meta 商务 | `facebook.com/business/help/389328664423846` | 疑似可接受使用政策相关页 | — | **未取回** |
| Meta 商务 | `facebook.com/business/help/search?query=multi-agent` | 搜索结果页（显示"找到 3 个结果"但结果未渲染） | — | **未取回** |
| Apple | `support.apple.com/en-us/108770`（May 01, 2025） | iCloud 备份包含第三方应用数据；关闭后保留 180 天 | 11.1 | 已取回 |
| Apple | `support.apple.com/en-us/102651`（January 05, 2026） | 高级数据保护：15 类端到端加密；标准保护下密钥在 Apple 数据中心 | 11.1 | 已取回 |
| Apple | `support.apple.com/en-us/108756`（April 17, 2026） | 失去账号时的三种恢复方式（设备密码、恢复联系人、恢复密钥） | 11.1 | 已取回 |
| Apple | `support.apple.com/en-us/102659`（September 14, 2026） | 快速开始迁移前提：两台在场、旧机解锁、需等待完成 | 11.5 | 已取回 |
| Apple | `support.apple.com/en-us/119967`（December 05, 2025） | 配机需准备好 Apple 账户密码 | 11.5 | 已取回 |
| Apple | iPhone 用户指南 use-passkeys-to-sign-in-to-apps-and-websites（页面无更新日期） | 通行密钥存于 iCloud 钥匙串、需开启 Keychain 与双重认证、可存硬件安全密钥 | 11.8 | 已取回 |
| Apple | `support.apple.com/en-us/102649`（September 14, 2026） | 从账户移除设备的后果；可远程登出 Apple 账户 | 11.7 | 已取回 |
| Google | `developer.android.com/identity/data/backup`（2026-08-26 UTC） | Auto Backup 需 Android 6.0+、上传至 Google Drive、每应用 25 MB、约每天一次 | 11.2 | 已取回 |
| Google | `developer.android.com/identity/data/autobackup`（2026-10-26 UTC） | 三个排除目录、只留最新一份、数据集不活跃期后删除、allowBackup=false 不关机到机 | 11.2、11.3 | 已取回 |
| Google | `source.android.com/docs/compatibility/16/android-16-cdd` §9.16（2026-08-04 UTC） | 机到机迁移四条 MUST（主锁屏、源机确认、同包名同签名、设置里留痕） | 11.4 | 已取回 |
| Google | `source.android.com/docs/security/features/encryption/file-based`（2026-10-30 UTC） | 凭据加密存储为默认位置，仅在用户解锁设备后可用 | 11.6 | 已取回 |
| Google | `developer.android.com/privacy-and-security/direct-boot`（2026-10-28 UTC） | CE 存储的可用区间：解锁后到重启前 | 11.6 | 已取回 |
| Google | `developer.android.com/work/dpc/security`（2026-10-23 UTC） | 消费级恢复出厂保护：需此前个人配置文件上的 Google 账户凭据 | 11.6 | 已取回 |
| Google | `support.google.com/android/answer/13626960` | 换机数据表中 App data 一栏：由应用开发者自行决定 | 11.8 | 已取回 |
| Google | `support.google.com/android/answer/13769784` | 已同步到账户的联系人／日历不在配机时重复传输 | 11.5 | 已取回 |
| Google | `support.google.com/accounts/answer/3067630` | 会话定义；可按设备名登出该设备全部会话 | 11.7 | 已取回 |
| Google | `support.google.com/accounts/answer/6160491` | 远程抹除后需 Google 账户密码才能重新使用设备 | 11.7 | 已取回 |
| **合计** | **81 页：帮助中心 40、法律与政策 6、Meta 开发者与商务 18、Apple 7、Google 10** | 五组相加 **81**；**未取回 5 页**（帮助中心 3＋Meta 商务 2），全篇不引用其内容 | 覆盖第 2–12 章全部引用 | **已取回 76 页** |

## A.1 关于取回方法的一条口径

帮助中心与 `whatsapp.com`、`developers.facebook.com`、`facebook.com/business` 的页面内容均由前端渲染，直接对 URL 发原始 HTTP 请求会得到 400 状态码或不含正文的外壳；本篇全部引文取自**渲染后的页面文本**。因此"未取回"在本篇的含义是：**该页面在本次渲染尝试中未能得到正文**，不是"该页面不存在"。两者在结论上的差别很大，附表 E 的读取失败分组按此口径书写。


# 附表 B 八种"会话／登录态"对象（9 行）

本表是本篇的骨架：官方文本里并不存在一个叫"登录态"的对象，它至少被写成八种不同的东西，各有自己的授予方、撤销方与时长。第 1.3 节对"登录态"的处理方式、以及第 14 章的四根轴划分，都建立在这张表上。

| 对象 | 官方用什么词 | 由谁授予 | 由谁撤销 | 官方写明的时长／上限 | 出处 |
|---|---|---|---|---|---|
| 主设备注册态 | primary phone／primary device | 用号码收验证码完成注册 | 同号码在新设备重新注册即顶替 | 无时长，只写"每 14 天需登录一次" | `378279804439436`、`1317564962315842`、`1197347060992858` |
| 已链接设备会话 | linked devices | 主设备扫码／配对 | 主设备逐台 Log Out；或 14／30 天规则失效 | **4 台**（Business 应用：4 台＋1 部手机） | `378279804439436`、`647349420360876`、`834124628020911` |
| 副手机会话 | linked device（界面文案）／companion phone | 主手机链接 | 同已链接设备 | 计入上述 4 台 | `1046791737425017` |
| 网页浏览器会话 | WhatsApp Web sessions | 勾选 "Stay logged in on this browser" 登录 | 手机上登出**全部** Web 会话 | 官方未给取消勾选后的时长 | `1428782138011916` |
| multi-agent 设备席位 | devices assigned to your business account | 订阅权益＋设备命名 | 降级后仅保留**最近活跃的四台** | 4／6／8／**10** 台按订阅档 | `395911122612120`、`3872729742954601` |
| 两步验证口令态 | two-step verification（PIN→password 换代中） | 用户自行开启（可选） | 用户重置；重置不可撤销 | 忘记时 **7 天**；入口解锁"几天" | `1278661612895630`、`1920866721452534`、`2183055648554771`、`1082491466048496` |
| 端到端备份保护态 | passkey／password／64-digit encryption key | 用户设置 | 关闭需先关后开，切换方式即重建 | 无过期表述；密钥丢失不可恢复 | `490592613091019` |
| API 访问令牌态 | access token／system user | 商务管理平台授权（admin／employee） | 官方未写自助撤销页 | 用户令牌"每几小时"；短期约 **1–2 小时**、长期约 **60 天** | `access-tokens`、Facebook Login 文档 |
| **合计** | **八种对象，分属四套撤销体系**：设备清单（主手机）、订阅权益（Meta Verified／One）、身份口令（SIM／邮箱）、平台授权（商务管理平台＋令牌） | — | — | **其中只有前两种在帮助中心有"台数"上限；后六种的上限要么按订阅、要么按角色、要么根本没有写** | 见第 14.2 节 |

## B.1 这张表读法的三条提醒

**第一，行与行之间没有官方定义的换算关系。** 例如"把某台设备从已链接设备里登出"与"让某个访问令牌失效"是两套完全独立的动作，官方没有任何一页把它们放在一起讲。

**第二，时长列里的"官方未给"不是本篇的疏漏。** 本表所有时长都能追到附表 C 的具体行；未给的那几处进附表 E，不推测。

**第三，八种对象里有三种"没有主体"**：端到端备份保护态、API 访问令牌态与商务组合操作权都不是"某个终端上的某个会话"，而是**一份可携带的秘密**。这是第 14.1 节末尾那条总观察的依据：WhatsApp 的官方文本里没有"会话管理器"这个统一对象，只有八个各自管理的清单。


# 附表 C 数字归档五档（87 行）

档位口径与第 1.3 节一致：**硬数量上限**＝超过就加不进去／发不出去；**时间窗口与时长**＝一段时间内累计允许多少、或某个状态持续／等待多久；**默认可改**＝官方写明的出厂状态；**条件触发**＝达到某个前提后发生什么；**分档晋级**＝档位本身，档位随质量与用量变化。同一个数字可以在不同档位各占一行（例如 2,000 既是分档晋级里的一个档，又是条件触发里"号码上限自动提升"的前提），**本表的行数不等于去重后的数字个数**，两个口径在第 14 章分别给出。

| 档位 | 数字 | 数的是什么 | 官方表述／定义句 | 出处 | 正文章节 |
|---|---|---|---|---|---|
| 硬数量上限 | 4 | 同时可链接的设备数（个人账号） | link up to four devices at a time | `378279804439436` | 2.1 |
| 硬数量上限 | 1 | 同时可用的手机数（Business 应用） | four linked devices and one phone at a time | `647349420360876` | 2.4 |
| 硬数量上限 | 4 | 未开通 multi-agent 时可连接设备数 | Connect up to four devices to your account | `395911122612120` | 2.5 |
| 硬数量上限 | 10 | 开通 multi-agent 后可连接设备数 | Connect up to 10 devices to your account | `395911122612120` | 2.5 |
| 硬数量上限 | 6 | Meta Verified Business Plus 档可链接设备数 | Link up to six devices | `3872729742954601` | 2.5 |
| 硬数量上限 | 8 | Meta Verified Business Premium 档可链接设备数 | Link up to eight devices | `3872729742954601` | 2.5 |
| 硬数量上限 | 2 | 主设备上可同时登出的账号数 | Up to two accounts are supported | `1421067466005147` | 2.6 |
| 硬数量上限 | 32 | 桌面端通话可加入人数 | calls with up to 32 people | `378279804439436` | 4.2 |
| 硬数量上限 | 64 | 端到端备份加密密钥位数 | a 64-digit encryption key | `490592613091019` | 8.2 |
| 硬数量上限 | 6 | 注册码与验证码位数 | 6-digit registration code | `506595211487528` | 6.1 |
| 硬数量上限 | 8 | 新密码最少字符数 | at least 8 characters | `2183055648554771` | 6.4 |
| 硬数量上限 | 2 GB | 免费账号的云存储备份容量 | 2 GB of storage | `490592613091019` | 8.3 |
| 硬数量上限 | 2 | 每套账号层级初始可注册号码数 | initially limited to 2 registered business phone numbers | `whatsapp-business-accounts` | 10.2 |
| 硬数量上限 | 20 | 该号码上限可提升到的值 | increased to up to 20 | `whatsapp-business-accounts` | 10.2 |
| 硬数量上限 | 20 | 每个商业组合初始账号层级数 | initially limited to 20 WhatsApp account hierarchies | `whatsapp-business-accounts` | 10.2 |
| 硬数量上限 | 250 | 未完成验证时的模板数上限 | 250 | `whatsapp-business-accounts` | 10.2 |
| 硬数量上限 | 6,000 | 验证并批准显示名后的模板数上限 | 6,000 | `whatsapp-business-accounts` | 10.2 |
| 硬数量上限 | 5 | 可共享同一 WhatsApp 账号的方案商数 | up to five Solution Partners | `whatsapp-business-accounts` | 10.3 |
| 硬数量上限 | 2 | 可共享同一消息账户的方案商数 | up to two | `whatsapp-business-accounts` | 10.3 |
| 硬数量上限 | 2 | 每个客户可领取的测试号码数 | up to two 555 numbers | `embedded-signup/overview` | 10.5 |
| 硬数量上限 | 45 | 6 秒突发内最多可发消息条数 | up to 45 messages in a 6-second burst | `about-the-platform` | 10.6 |
| 硬数量上限 | 1,000 mps | 每秒消息数的上限 | 1,000 messages per second | `throughput` | 10.7 |
| 硬数量上限 | 20 mps | 共存模式号码的固定吞吐 | fixed at 20 mps | `throughput` | 10.7 |
| 时间窗口与时长 | 14 天 | 主手机停用后已链接设备被登出 | log out if your phone is unused for over 14 days | `378279804439436` | 3.1 |
| 时间窗口与时长 | 30 天 | 已链接设备被自动断开的闲置期 | disconnect linked devices after 30 days of inactivity | `378279804439436` | 3.1 |
| 时间窗口与时长 | 14 天 | 需多久在主手机上登录一次 | log in to WhatsApp on your primary phone every 14 days | `1317564962315842` | 3.1 |
| 时间窗口与时长 | 14 天 | 主设备登出后已链接设备仍连接多久 | remain connected for 14 days after logging out | `1421067466005147` | 3.2 |
| 时间窗口与时长 | 14 天 | 副手机在主手机未用后的登出期 | logged out after 14 days | `1046791737425017` | 3.2 |
| 时间窗口与时长 | 30 天 | 未送达消息在服务端的存储期 | up to 30 days | 服务条款 | 9.1 |
| 时间窗口与时长 | 1 年 | 已链接设备可显示历史的上界 | up to one year of chat history | `378279804439436` | 4.3 |
| 时间窗口与时长 | 6 个月 | 共存模式可回溯同步的聊天长度 | most recent 6 months | `onboarding-business-app-users` | 10.8 |
| 时间窗口与时长 | 7 天 | 号码被重新分配后重置两步验证的等待 | wait seven days after you've registered | `1082491466048496` | 6.5 |
| 时间窗口与时长 | 7 天 | 不知道 PIN 时的等待期 | wait 7 days and try again | `1131652977717250` | 5.4 |
| 时间窗口与时长 | 30 天 | 重置密码时区分后果的登录间隔分界 | more than 30 days since you logged in | `2183055648554771` | 6.4 |
| 时间窗口与时长 | 5 个月 | Google 自动删除未更新备份的期限 | haven't been updated in five months | `407643231403807` | 8.4 |
| 硬数量上限 | 25 MB | Android 自动备份每应用的数据量上限 | up to 25 MB of file-based data per app | Google 开发者文档 | 11.2 |
| 硬数量上限 | 15 类 | Apple 高级数据保护下端到端加密的数据类别数 | 15 data categories | Apple 支持文档 | 11.1 |
| 时间窗口与时长 | 180 天 | 关闭设备 iCloud 备份后旧备份的保留期 | kept for 180 days before being deleted | Apple 支持文档 | 11.1 |
| 时间窗口与时长 | 约每天一次 | Android 自动备份的触发频率 | roughly once a day | Google 开发者文档 | 11.2 |
| 时间窗口与时长 | 24 小时 | 封禁复核的官方完成时长 | Most reviews are completed within 24 hours | `465883178708358` | 12.3 |
| 时间窗口与时长 | 24 至 48 小时 | 政策申诉出结果的时长 | typically takes 24 to 48 hours | `policy-enforcement` | 9.5 |
| 时间窗口与时长 | 90 天 | 协议终止后平台保留数据的期限 | up to 90 days | `Meta Terms for WhatsApp Business Platform` | 9.2 |
| 时间窗口与时长 | 30 天 | 沙箱账号的有效期 | valid for 30 days | `embedded-signup/overview` | 10.5 |
| 时间窗口与时长 | 最长 7 天 | 失败 webhook 的重投窗口 | up to 7 days with exponential backoff | `throughput` | 10.7 |
| 时间窗口与时长 | 最长 1 分钟 | 吞吐档位升级的生效耗时 | can take up to 1 minute | `throughput` | 10.7 |
| 时间窗口与时长 | 约 1–2 小时 | 短期访问令牌的寿命 | approximately 1-2 hours | Facebook Login 文档 | 10.4 |
| 时间窗口与时长 | 约 60 天 | 长期访问令牌的寿命 | approximately 60 days | Facebook Login 文档 | 10.4 |
| 时间窗口与时长 | 每几小时 | 系统用户令牌的过期节奏 | expire quickly … every few hours | `access-tokens` | 10.4 |
| 时间窗口与时长 | 滚动 1 小时 | API 请求计数的窗口 | per rolling one hour | `about-the-platform` | 10.6 |
| 时间窗口与时长 | 每小时 200 次 | 应用默认请求速率 | 200 requests per hour | `about-the-platform` | 10.6 |
| 时间窗口与时长 | 每小时 5,000 次 | 活跃账号提升后的请求速率 | 5,000 | `about-the-platform` | 10.6 |
| 时间窗口与时长 | 每小时 600 条 | 同一收件人的软上限 | about 10 messages per minute or 600 per hour | `about-the-platform` | 10.6 |
| 时间窗口与时长 | 6 秒 | 同一收件人两条消息的最小间隔 | 1 message every 6 seconds | `about-the-platform` | 10.6 |
| 时间窗口与时长 | 每日／每周／每月 | 云备份的三个频率档 | daily, weekly, or monthly | `481135090640375` | 8.3 |
| 时间窗口与时长 | 30 秒 | 迁移失败后重开蓝牙的等待 | wait 30 seconds | `209942271778103` | 7.3 |
| 时间窗口与时长 | 24 小时 | 客服窗口的长度 | 24-hour customer service window | `send-messages` | 9.4 |
| 默认可改 | Everyone | 谁能把自己拉进群组的默认值 | Everyone | `1131457590844955` | 3.4 |
| 默认可改 | 开启 | 已读回执的默认发送状态 | Read receipts are always sent for group chats | `3307102709559968` | 3.5 |
| 默认可改 | 关闭 | 两步验证是否默认启用 | optional | `1278661612895630` | 6.2 |
| 默认可改 | 勾选 | 浏览器会话是否保持登录 | Stay logged in on this browser | `1428782138011916` | 4.4 |
| 默认可改 | 三个开关 | 备份内容可改项 | Include videos／Back up over mobile data／Include status archive | `481135090640375` | 8.3 |
| 默认可改 | Full access | 系统用户为管理员时的默认权限 | full access | `access-tokens` | 10.4 |
| 默认可改 | 80 mps | 吞吐的默认档位 | 80 messages per second | `throughput` | 10.7 |
| 条件触发 | 4 | 超过该台数后只有主设备接到来电 | more than 4 linked devices will only receive calls on the primary device | `395911122612120` | 2.5 |
| 条件触发 | 2,000 | 号码上限自动提升的前提之一 | reaching a messaging limit of 2,000 | `phone-numbers` | 10.2 |
| 条件触发 | 4 | 取消订阅后保留的最近活跃设备数 | only your four most recently active devices | `395911122612120` | 2.5 |
| 条件触发 | 100,000 | 吞吐自动升级的 24 小时触达门槛 | 100K+ unique recipients | `throughput` | 10.7 |
| 条件触发 | 一半／7 天 | 限额自动升档的用量判据 | utilized at least half of your current messaging limit | `messaging-limits` | 10.9 |
| 条件触发 | 6 小时 | 满足判据后的升档时效 | within 6 hours | `messaging-limits` | 10.9 |
| 条件触发 | 30 天 | 进入 2,000 档的投递量统计窗口 | 30-day moving period | `messaging-limits` | 10.9 |
| 条件触发 | 6.0 | 手机系统最低版本门槛 | Android 6.0 and newer | `2366980643469578` | 2.7 |
| 条件触发 | 15.5／SDK 21 | 跨端迁移的两端最低系统版本 | iOS 15.5+／SDK 21 | `686469079565350` | 7.4 |
| 条件触发 | 12 | 迁移时被要求精确定位权限的系统版本 | Android 12 and below | `209942271778103` | 7.3 |
| 条件触发 | 90 天 | 商业组合被强制两步验证的账龄 | more than 90 days old | `865384580786591` | 6.7 |
| 分档晋级 | 4 → 6 → 8 → 10 | Meta Verified／Meta One 四档订阅对应的可链接设备数 | Link up to four／six／eight／ten devices | `3872729742954601` | 2.5 |
| 分档晋级 | 10 → 200 → 合作伙伴 | Embedded Signup 的三级接入档 | up to 10… up to 200… apply to become a Meta Business Partner | `embedded-signup/overview` | 10.5 |
| 分档晋级 | 250 | 商业平台消息限额第一档 | 250 | `messaging-limits` | 10.9 |
| 分档晋级 | 2,000 | 第二档 | 2,000 | `messaging-limits` | 10.9 |
| 分档晋级 | 10,000 | 第三档 | 10,000 | `messaging-limits` | 10.9 |
| 分档晋级 | 100,000 | 第四档 | 100,000 | `messaging-limits` | 10.9 |
| 分档晋级 | 不限 | 第五档 | Unlimited | `messaging-limits` | 10.9 |
| 分档晋级 | 2 → 20 | 号码配额的两档 | limited to 2… increased to up to 20 | `whatsapp-business-accounts` | 10.2 |
| 分档晋级 | 250 → 6,000 | 模板配额的两档 | 250／6,000 | `whatsapp-business-accounts` | 10.2 |
| 分档晋级 | 200 → 5,000 次／小时 | 请求速率的两档 | 200… 5,000 | `about-the-platform` | 10.6 |
| 分档晋级 | 80 → 1,000 mps | 吞吐的两档 | 80… up to 1,000 mps | `throughput` | 10.7 |
| 分档晋级 | 1 或 3 天 → 5／7／30 天 → 无限期 → 永久 | 企业侧处罚四级阶梯 | 1 or 3 day block… permanent disable | `policy-enforcement` | 9.5 |
| **合计** | **86 个条目／64 个去重后的数字** | 五档：硬数量上限 **25**、时间窗口与时长 **31**、默认可改 **7**、条件触发 **11**、分档晋级 **12** | 25＋31＋7＋11＋12 ＝ 86，与本表条目数闭合；86 个条目里有 **13** 个数字各占 2–5 行，所以条目数不等于数字数 | 与第 1.3 节同口径 | 14.2 |

# 附表 D 称谓、字段、错误码、文案与处置枚举（26 行）

本表收录**不是数字但需要精确引用**的官方文本对象：同一件事在不同页面用了不同称谓、同一字段换了名字、同一个"200"在两处指完全不同的东西。六组依次是：设备与会话称谓 5、状态与字段枚举 5、API 错误码 4、用户可见文案 4、处置与申诉枚举 4、换代与生效标记 3。

| 组 | 对象 | 官方写法 | 出现位置 | 本篇的处理 |
|---|---|---|---|---|
| 称谓 | 主设备 | primary phone／primary device（两种写法并存） | `378279804439436`、`498754122134583`、`1421067466005147` | 视为同一对象，但注意 Android 平板可被注册为 primary device |
| 称谓 | 已链接设备 | linked devices | `378279804439436`、`647349420360876` | 计入"四台"的唯一集合 |
| 称谓 | 第二台手机 | companion phones（清单用词）／"This is a linked device."（界面文案） | `378279804439436`、`1046791737425017` | 归入 linked devices，不是第二台主设备 |
| 称谓 | 受信任设备 | Trusted devices（"different from linked devices"） | `1278661612895630` | **与 linked devices 显式不同**；数量与撤销入口未文档化 |
| 称谓 | 网页会话 | WhatsApp Web sessions | `1428782138011916` | 批量登出**只覆盖这一类** |
| 字段 | 号码状态 | "connected" | `phone-numbers` | 号码配额自动提升的前提之一 |
| 字段 | 质量分档 | quality_score YELLOW or higher | `throughput` | 吞吐自动升级的三个条件之一 |
| 字段 | 限额档位 | `"TIER_250"`；`messaging_limit_tier` 已弃用 → `whatsapp_business_manager_messaging_limit` | `messaging-limits` | 引档位名时必须用新字段 |
| 字段 | 共存标识 | `is_on_biz_app`＋`platform_type = CLOUD_API` | `onboarding-business-app-users` | 判定"这条消息从哪一侧进来"的官方字段 |
| 字段 | 能力变更通知 | `business_capability_update` ＋ `max_phone_numbers_per_business` | `phone-numbers` | 号码上限升到 20 时的回调载荷 |
| 错误码 | 缺少账号级访问 | `200`（**业务错误码，不是 HTTP 状态码**） | `access-tokens` | 与 10.4 的"错误码 200"一起读，勿按 HTTP 理解 |
| 错误码 | 同一收件人发送过快 | `131056` | `about-the-platform` | 对应 6 秒／每分钟 10 条那组节奏 |
| 错误码 | 吞吐未达标 | `130429` | `throughput` | 自动升级未满足条件时的返回 |
| 错误码 | 档位切换未生效 | `131057` | `throughput` | 与"最长 1 分钟生效"配套 |
| 文案 | 副手机界面提示 | "This is a linked device." | `1046791737425017` | 自查时判断"这台是不是被我链接过"的唯一界面证据 |
| 文案 | 封禁弹窗 | "This phone number can't use WhatsApp: [phone number]. You can request a review of this number. Most reviews are completed within 24 hours." | `465883178708358` | 逐字引用；弹窗内**有无 Request review** 决定可否申诉 |
| 文案 | 非官方客户端拦截 | "You need the official WhatsApp to log in" | `1217634902127718` | 与下一条一起构成"有明确成因文案的登出" |
| 文案 | 因非官方应用被登出 | "You have been logged out for using an unofficial app." | `1217634902127718` | 同上 |
| 处置 | 企业侧四级阶梯 | 1 或 3 天封锁 → 5／7／30 天封锁 → 无限期账号锁定 → 永久停用 | `policy-enforcement` | 每级内部选哪个时长**未文档化** |
| 处置 | 立即下线情形 | 儿童性剥削、诈骗、恐怖主义、非法毒品 | `policy-enforcement` | 不走阶梯 |
| 处置 | 申诉结果枚举 | Unchanged／Reversed | `policy-enforcement` | 只有两种取值 |
| 处置 | 消费者侧两级 | 临时封禁（可整改恢复）→ 永久封禁（复核一次） | `1848531392146538`、`465883178708358` | 临时封禁**无时长表述** |
| 标记 | 两步验证换代 | "WhatsApp is upgrading the 6-digit PIN … to a password" | `1920866721452534` | 同页两种措辞并存，属过渡期文本 |
| 标记 | 接入组件弃用 | Embedded Signup v2／v3 于 October 15, 2026 弃用 | `embedded-signup/overview` | 与 9.2 同日生效的条款是两件事 |
| 标记 | 账号模型生效 | WABA 拆分 GA September 23, 2026；10 月中旬前全量 | `account-model-evolution` | 引用"一个 WABA 里有什么"前必须确认是否已迁移 |
| **合计** | **25 项，六组：5／5／4／4／4／3** | 其中**称谓组 5 项是本次最容易读错的地方**——五个词指四种对象，且"trusted devices"与"linked devices"被官方明确区分 | — | 见第 13.1 节 |


# 附表 E 官方未文档化清单（29 行）

四类计数：**规模与可见性类 6、规则与判据类 11、读法与措辞类 5、本次读取失败 6**，合计 **28** 项。本表是全篇的边界：**这 28 个问题在官方文本里没有答案，本篇一律留空，不用第三方统计、媒体报道或逆向结论补齐。** 最后一列给出正文回指的章节，以便核对"留白确实发生在被引用的地方"。

| 类别 | 留白的问题 | 本篇检索过的页面 | 为什么不能推 | 正文回指 |
|---|---|---|---|---|
| 规模与可见性 | 是否存在一份统一的"已链接设备能力矩阵"页 | `378279804439436`、`647349420360876`、`1046791737425017`、`1131457590844955` | 缺失项散落在四页，拼出来的是**并集**而非官方清单 | 4.1 |
| 规模与可见性 | Desktop 比 Web 多同步多少历史（按条、按天还是按容量） | `653480766448040` | 官方只写 "more"，没有任何量词 | 4.3 |
| 规模与可见性 | 取消勾选 "Stay logged in on this browser" 后该会话多久失效 | `1428782138011916` | 该页只给勾选与不勾选两条路径，未给时长 | 4.4 |
| 规模与可见性 | 受信任设备（trusted devices）的数量上限与撤销入口 | `1278661612895630`、`1920866721452534` | 该词只在"与 linked devices 不同"这一句里出现，没有独立管理页 | 6.2 |
| 规模与可见性 | 单个 WhatsApp 账号可维持多少路并发 API 会话 | sessions／multi-device／on-premises 三处路径（见本表第 26 项） | 共享家数、请求速率、并发数是三件不同的事，官方未给换算 | 10.10 |
| 规模与可见性 | system user 的数量上限 | `access-tokens`、`whatsapp-business-accounts` | 两页写了角色与配额，没写席位数量 | 10.4 |
| 规则与判据 | 临时封禁的持续时长 | `1848531392146538` | 该页只写"可能导致永久封禁"，无天数；与"复核 24 小时"不是同一对象 | 12.1 |
| 规则与判据 | 阶梯每级内部选哪一档时长的判据（1 还是 3 天；5、7 还是 30 天） | `policy-enforcement` | 官方给的是集合，不是映射 | 9.5 |
| 规则与判据 | 邮箱验证码 "a short time" 究竟多久 | `1082491466048496` | 原文即模糊量词，且同页只补了"只有最新一枚有效" | 6.1 |
| 规则与判据 | 三处等待期（7 天／"a few days"／7 天）各自的起算点 | `2183055648554771`、`1082491466048496`、`1131652977717250`、`1197347060992858` | 官方明确"can't speed up"，但没写从哪一刻开始计时 | 5.4 |
| 规则与判据 | 是否存在"一键登出全部已链接设备"的入口 | `1428782138011916`、`834124628020911`、`378279804439436` | 三页给出的批量能力**只覆盖 WhatsApp Web 会话** | 3.3、4.4 |
| 规则与判据 | 端到端备份密钥从旧口径改为 64 位的变更说明 | `490592613091019` | 现行页只写现值，官方未发布变更页 | 8.2 |
| 规则与判据 | "API 无法关闭两步验证"是设计意图还是当前实现限制 | `two-step-verification`、`phone-numbers` | 两句都只陈述"没有端点"，未给理由 | 6.6 |
| 规则与判据 | Android 本地备份的目录、加密版本文件与本地保留份数 | 帮助中心本次未取回任何写明这些参数的页面 | 该主题公开渠道只有逆向结论，本篇不引用 | 8.4 |
| 规则与判据 | 两步验证的重新提示节奏 | `1278661612895630`、`1920866721452534` | 两页均未写提醒频率，只写开关与设置路径 | 6.3 |
| 规则与判据 | 频道媒体在三条迁移通道上的一致处理方式 | `209942271778103`、`686469079565350`、`1295296267926284` | 三页都写"不可迁移"，但都没写**原因与范围**（是媒体本身还是订阅关系） | 7.4 |
| 规则与判据 | 共存模式下"群组聊天不同步"能否通过其它官方工具补齐 | `onboarding-business-app-users` | 该页只陈述不同步，未给替代路径 | 10.8 |
| 读法与措辞 | 帮助中心页面**全部没有** "Last updated" 字段 | 本表所涉全部帮助中心页面 | 这不是疏漏而是口径：该组引文无法配日期 | 1.2 |
| 读法与措辞 | 条款页的 Effective 与 Last Modified 两种字段能否合并为"发布日期" | 9.2 的三份条款 | 字段名本身是原文的一部分，含义不同 | 9.2 |
| 读法与措辞 | "unused"与常被转述的"offline"是否同义 | `378279804439436` | 官方原文只用 unused；转述用词不是文本依据 | 3.1 |
| 读法与措辞 | "log out"与"disconnect"是否指同一状态 | `378279804439436`、`834124628020911` | 两句主语不同（主手机闲置 vs 设备闲置），官方未合并定义 | 3.3 |
| 读法与措辞 | 是否存在独立的"可接受使用政策"页 | `whatsapp.com/legal/` 下 4 个候选路径全部 404 | 只能用服务条款＋消息指南＋商业消息政策三份文本 | 9.1 |
| 本次读取失败 | `451924530376167` 桌面端专页正文 | 见附表 A | 无法判断桌面端是否另有上限表 | 4.2 |
| 本次读取失败 | `1007324800132703` 手机丢失或被盗页 | 见附表 A | 该页可能含设备撤销与远程处置路径，缺失使第 5 章的自查清单不完整 | 5.1 |
| 本次读取失败 | `717472490411581` 受限账号页 | 见附表 A | "restricted"与"banned"的关系无法从已取回页面确定 | 12.1 |
| 本次读取失败 | `facebook.com/business/help/389328664423846` | 见附表 A | 无法确认是否存在商务侧独立使用政策 | 9.1 |
| 本次读取失败 | 商务帮助中心的 multi-agent 搜索结果页（显示 3 条结果但未渲染） | `facebook.com/business/help/search` | 3 条结果可能含席位计费文本，未能核对 | 2.5 |
| 本次读取失败 | `developers.facebook.com` 的 sessions／multi-device／on-premises 三处路径 | 见附表 A | 全部 404 或无正文，是第 10.10 节留白的直接原因 | 10.10 |
| **合计** | **28 项：规模与可见性 6、规则与判据 11、读法与措辞 5、本次读取失败 6** | 与附表 A 的"未取回 5 页"部分重合但不等价——**本表第 23–28 行的 6 项里，有 3 项对应的页面根本没进附表 A（它们是被检索过但不存在的 URL）** | 留白数量（28）大于冲突数量（10），说明这一主题的公开文本**密度远低于群发功能那一主题** | 见第 14.3 节 |


# 附表 F 页面之间的口径冲突（11 行）

下列 **10** 组差异都是**同一主题在两份官方文本里写法不同**，不是本篇的解读分歧。处理原则与第 1.2 节一致：**两句都抄，不合并、不取其一为准**；确需选择时（如新旧版本），本篇注明以哪一页为现行，同时保留另一页的记录。

| 主题 | 甲页面的写法 | 乙页面的写法 | 冲突性质 | 本篇的处理 |
|---|---|---|---|---|
| "四台"的主语 | `378279804439436`：linking up to four devices at a time **to your primary phone** | `395911122612120`：Connect up to four devices **to your account**；`647349420360876`：four linked devices **and one phone** | 三种主语（主手机／账号／手机+设备），无对齐句 | 三种写法逐条引用，不写成"账号最多四台设备" |
| 主设备可以是哪些终端 | `378279804439436` 的"不能当主设备"枚举只列 Windows、Mac、Web、iPad、wearables | 同页另有一句：Android 平板**注册后即可作为主设备** | 同一页内的枚举不闭合 | 按"Android 平板是唯一非手机的主设备形态"书写，并注明来自同页另一句 |
| 14 天的触发条件 | `378279804439436`：if your phone is **unused** for over 14 days | 公开转述普遍写作"主手机**离线** 14 天" | 用词不同（未使用≠离线） | 只用 unused 的原文；把 offline 版列入附表 E 说明找不到原句 |
| 两步验证的凭据形态 | `1920866721452534`： upgrading the **6-digit PIN** … to a **password** | `2183055648554771`：口令规则是 **8 字符＋1 字母＋1 数字**，同页仍出现"6 位邮箱码" | 换代期文本，同一体系内新旧并存 | 每次引用都标注"换代期表述"，不给出统一的"两步验证是几位"的答案 |
| 端到端备份的密钥写法 | `490592613091019`（现行）：**64-digit** encryption key | 更早流传的口径：32 个字符（本次未在任一现行页面取回） | 版本不同，且官方无变更说明页 | 只引现行页数字，把旧口径的处理列入附表 E |
| 桌面端通话能力 | `378279804439436`：calls with up to **32 people** | `668538004658079`：**Unlimited** calling | 一句数人、一句数功能，不同轴 | 32 进附表 C；"unlimited"不解读为无人数上限 |
| 两步验证是否强制 | `865384580786591`：组合账龄 **>90 天**可能被强制开启**且无法关闭** | 条款文本（9.2 的三份）只写"维护账号与凭据安全"，**未出现两步验证义务** | 层级不同：帮助中心 vs 合同 | 两句并列引用，明确"强制来自帮助页，不来自条款" |
| 令牌的有效期 | `access-tokens`：用户令牌 "expire quickly… **every few hours**" | Facebook Login 文档：短期约 **1–2 小时**、长期约 **60 天**、部分类型不按时间过期 | 一份给量级、一份给近似值、一份给例外 | 三条同时列出，不给"WhatsApp 令牌寿命"单值 |
| 2,000 这个数字的作用 | `messaging-limits`：五档限额表中的**第二档** | `phone-numbers`：**触发号码配额从 2 升到 20 的前提** | 同数不同轴（档位 vs 触发门槛） | 附表 C 给它开两行，正文每次注明是哪一轴 |
| 批量登出的范围 | `1428782138011916`：可 log out of all your active **WhatsApp Web sessions** | 同页对其它设备写 "Repeat these steps for **all linked devices**"（逐台） | 一句是批量、一句是逐台，且覆盖对象不同 | 明确"批量只覆盖 Web 会话"，不写"一键登出所有设备" |
| **合计** | **10 组冲突，性质依次为：主语不同、枚举不闭合、用词不同、版本并存、版本变更、量纲不同、层级不同、精度不同、作用轴不同、范围不同** | 十组里没有一组是"数字算错"，全部是**表述轴或层级不一致** | — | 见第 13.3 节 |


---

**边界声明：** 本篇是一份**公开文本口径梳理**，不是操作手册。全篇内容限于 WhatsApp 官方帮助中心与法律政策页面、Meta 商业平台开发者与商务管理文档、Apple 与 Google 开发者与支持文档中已经公开写出的表述，引用目的仅限于说明两件事：平台在规则层已经把哪些行为定义为违规、哪些能力可以被单独收回；以及用户在自己账号上有哪些官方给出的查看与撤销访问的路径。本篇**不含**批量发送脚本、账号池、多号轮换、设备农场、会话维持或转移的操作方法；**不含**限速规避、垃圾信息检测绕过、封号规避与申诉话术模板；**不含**任何针对他人设备的操作步骤与时序；**不含**利用代码、恶意样本或利用链构造方式。本篇对 WhatsApp、Meta、Apple 与 Google 产品的安全性、加密强度与政策执行一致性**不作评价**，只报告页面写了什么、没写什么。凡官方页面未文档化的问题，全部列入附表 E 留空，不以第三方统计、媒体报道或逆向结论补齐；本次未能取回正文的 5 个页面在附表 A 逐行标注，其内容未被引用。

Telegram: https://t.me/one00190

