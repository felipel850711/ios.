@@TITLE@@WhatsApp 端到端加密、密钥校验、备份加密与留存披露口径解读
@@SUBTITLE@@本篇是前两篇 WhatsApp 口径篇的**同规矩、第三个轴**续篇：那两篇分别数了"一条消息能同时到达多少人"与"一个账号能同时被几个终端持有"，这一篇回答的是"哪些环节在官方文本里被称作端到端加密、谁能看到什么、东西留多久、谁能依法取走"。全篇只读官方页面原文，共登记 **57** 页：**帮助中心 39 页＋法律与政策文本 12 页＋Apple 支持文档 2 页＋Android 官方文档 2 页＋WhatsApp 官方博客 1 页＋加密白皮书 1 页**（白皮书按一页计入，实际取回 40 个内页），其中 **2** 页本次正文未取回、全篇不引用其内容，其余页中本篇实际引用 **48** 页、另 **7** 页在附表 A 标为**登记未引用**。各层的公开数字是：单聊安全码 **60** 位、指纹派生 **5,200** 次迭代与每段 **100,000** 取模、消息密钥 **80** 字节（**32** 加密＋**32** 校验＋**16** 初始向量）、根密钥／链密钥／发送方密钥／SRTP 主密钥／链路密钥共用同一个 **32** 字节、备份手写密钥 **64** 位、两步验证口令由 **6** 位升级中、配对码 **8** 个 Base32 字符、应用状态 Epoch 随机上界 **65,536**；服务端侧未送达消息 **30** 天、执法保全 **90** 天、删除常规上限 **90** 天且 **90 天之后**仍可能留有灾难恢复副本、停用转删除 **30** 天、不活跃 **120 天（美国 365 天）**、单次查看媒体 **14** 天未打开即过期、消失消息 **24 小时／7 天／90 天**三档、群通话旧会话保活 **5 秒**、已签名设备列表最长 **35 天**而校验异常时压缩到 **48 小时**、免费账号云备份 **2 GB**、可同时连接第三方代理 **5** 个、举报时随附上交最近 **5** 条消息。而"签名预密钥具体轮换几天""加密媒体'数周'是几周""执法请求成本补偿的金额""一次性预密钥队列的初始长度"这四件事**官方文本没有写**，连同另外六项一起列为第 13 章的未写明清单（共 **10** 项）。三条最容易读错的结论先说：**第一，"端到端加密"在这套文档里同时是协议描述和定性标签，白皮书用"regardless of their end-to-end encryption status"把两层切开，所以"用同一套协议"与"被称作端到端加密"是两句话**；**第二，同一个"30"在附表 B 占 7 行、横跨时限与密码学参数两档、分属四个主语，其中 1 行根本不是时长而是"取前 30 字节"**；**第三，官方对留存的默认答复是否定式的（送达即删、不保留内容），所有肯定式表述都自带例外主语与"may／up to／approximately"，而执法场景下"可从不保留切换为开始保留"是命令触发的，不是默认状态**。本篇不提供批量发送自动化、账号池与多号轮换、限速或反垃圾检测规避、破解与提取加密数据、构造利用链的任何操作性内容。
@@INFO@@报告日期|2026 年 11 月 8 日
@@INFO@@分析对象|WhatsApp 官方帮助中心（`faq.whatsapp.com`，逐篇以文章 ID 作为 URL 引用，共 **39** 页，覆盖端到端加密总述与安全码校验、密钥与凭据、备份与恢复、账号删除阶梯、消失消息与单次查看、举报与屏蔽、群组与通话、位置与动态、Code Verify 与网页版校验、隐私体检与主动通知十一个主题，主题之间有交叉故不再分档计数，其中 **1** 页正文本次未取回）、WhatsApp 法律与政策文本（**12** 页：现行隐私政策与服务条款、商业应用与商业平台两份协议、数据安全条款、执法披露指引与透明度报告页、第三方智能体条款、消息政策与使用准则、Cookie 说明；执法披露矩阵按五级法律程序逐字段列出可披露对象，其中 **1** 页本次只取回导航链接列表）、WhatsApp 加密白皮书 **1** 页（Signal 协议的密钥层级、双棘轮、群组发送方密钥与 5 秒窗口、通话 SRTP、应用状态 Base Keys、设备绑定链路密钥、安全码算法，按一页计入但实际取回 40 个内页）、Apple 支持文档 **2** 页（iCloud 备份的端到端加密类别清单与高级数据保护）、Android 官方文档 **2** 页（文件级加密与直接启动的存储加密要求）、WhatsApp 官方博客 **1** 页（高级聊天隐私的推出说明）。六组合计 **57** 页，未取回 **2** 页，附表 A 逐行标注取回状态与本篇引用位置。对照口径：本篇把官方"加密"用词拆成**四个互不等价的用法**（协议描述／定性标签／传输层义务／存储层描述），把可见性拆成附表 C 的**六个档位**，把留存拆成第 10 章的**四根时间轴**；凡官方没有统一术语的，本篇不发明术语。
@@INFO@@视角定位|加密与留存口径的解读，属于**协议文档、政策文本与法律披露页的公开表述梳理**，不是操作指南。本篇**不提供**破解、提取或绕过任何一层加密的方法，**不提供**密钥恢复、取证提取、会话还原或中间人校验攻击的实现步骤，**不提供**批量发送自动化、账号池、多号轮换、限速规避或反垃圾检测绕过的操作性内容，**不提供**任何针对他人设备的操作步骤与时序。文中出现的密钥层级、棘轮、SRTP、Base Keys、链路密钥等名词全部**照录白皮书的定义与原文锚点**，只用于说明"官方声称这一层做了什么、没说什么"；第 11 章关于执法披露的全部内容是对官方自述文本的逐字段对齐，第 12 章关于本机防线的全部内容只是把 Android 与 Apple 已公开发布的文档表述对齐，均不含可执行步骤序列。引用目的只有两个：**（1）让读者知道哪一层加密是协议保证、哪一层只是定性承诺，以及被明文排除在端到端称谓之外的六个场景；（2）让读者知道服务端与本机各自留多久、哪些字段可以在什么法律程序下被交出、删除承诺覆盖与不覆盖什么。** 凡引号内英文均为**官方页面原文**，出处紧跟其后，并已逐条比对原文字节；官方未写明的内容一律列入第 13 章未写明清单与附表 E，不做推测补齐。本篇亦不构成对 WhatsApp、Meta、Apple 或 Google 任何产品加密强度、隐私水平或政策执行一致性的评价。
@@INFO@@分类|**官方页面登记清单 57 页 × 站点类型 × 本页口径 × 本篇引用位置**（附表 A，**58** 行含合计；站点类型为帮助中心 39、法律与政策 12、Apple 2、Android 2、官方博客 1、白皮书 1；**未取回 2 页**、**引用 48 页**、**登记未引用 9 行**含那 2 页）**/ 数字归档五档 × 68 个条目 × 45 个去重数字**（附表 B，**69** 行含合计；档位：**时限 20／密码学参数 18／版本与日期 13／上限 10／法律条款 7**，20＋18＋13＋10＋7＝68 与条目数闭合；**14** 个数字各占 2–7 行，多出的 **23** 行全部来自重复值；**4** 组跨档碰撞：30、5、100、14）**/ 可见性与定性六档 × 50 个条目**（附表 C，**51** 行含合计；端到端加密 13／平台可见但不是内容 11／本机侧防线 10／不视为端到端加密 6／取决于你的选择 6／官方未定性 4，分布在 **17** 个页面上）**/ 密钥与凭据十二类 × 55 个条目**（附表 D，**56** 行含合计，按会话建立 7、会话内 7、校验凭据 6、设备绑定 6、账号凭据 6、应用状态 5、备份凭据 4、平台侧 3、本机存储 3、群组 3、通话 3、实时位置 2 排列，出处页 **10** 个）**/ 留存与披露 37 项 × 五类**（附表 E，**38** 行含合计；服务端消息留存 11／账号删除阶梯 10／法定保全与披露 9／备份与本机侧 4／依个案或未公开 3，出处页 **9** 个）**/ 术语与口径 49 条 × 七类**（附表 F，**50** 行含合计；协议与密钥 14、界面与功能 10、留存与披露 7、设备与绑定 5、本机与平台 5、校验与信任 4、群组与通话 4，分布在 **21** 个页面上）
@@INFO@@难度|入门—进阶。本篇的难点有三层。**第一层是"加密"这个词的四层用法不共轴**：白皮书说"All chats use the same Signal protocol outlined in this whitepaper, regardless of their end-to-end encryption status"，这一句把**协议描述**与**定性标签**明确切开；而商业侧数据安全条款里的"protect the confidentiality of data in transit over public networks"是**传输层义务**、支付一节里的"Card and bank numbers are stored encrypted"是**存储层描述**，四者用了同一个词根却回答四个不同问题。把后两种读成端到端承诺，或把第一种读成定性承诺，是这类阅读最常见的错误。**第二层是同一个数字绑不同主语、甚至跨量纲**：`30` 在附表 B 占 **7** 行（未送达保留上限在隐私政策里写了两遍、执法披露指引复述一次、撤销保留窗口、停用转删除等待期、仲裁退出邮寄期限，以及白皮书的"取前 30 字节"）；`90` 占 **4** 行且第四个是"90 天之后"；`5` 占 **4** 行横跨时限与上限（5 秒与三种"5 个／5 条"）；`100`、`14`、`60`、`64`、`6`、`9`、`13`、`120`、`2016`、`2703`、`94025` 各占 2 行。凡引用数字必须同时报它数的是天、秒、字节、位数、条数还是款号。**第三层是"协议保证"与"政策承诺"的分离，以及默认句与例外句的分离**：能不能看到内容由协议与定性两层决定，留多久由四根互不替换的时间轴决定，而依法能交出什么由五级法律程序各自的字段集决定；官方文本的默认答复是否定式的（送达即删、不保留内容、无法应政府请求产出消息正文），所有肯定式表述都带"may／up to／approximately／case-by-case"和一个例外主语。把这两组句式任意一组当成总体政策，都会失真。
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

前两篇 WhatsApp 口径篇分别数了两件事：一篇数"一条消息能同时到达多少人"，一篇数"一个账号能同时被几个终端持有"。这一篇换一个轴，回答四个更基础的问题：

**第一，哪些环节在官方文本里被称作端到端加密，哪些环节被官方明文排除在这个称谓之外。**这不是修辞问题：官方帮助页在同一篇文章里既写了"All of this happens automatically: no need to turn on any special settings"，也写了某些会话"are not considered end-to-end encrypted"。两句话都成立，差别在宾语。

**第二，密钥到底有几把、各自活多久。**白皮书给出了完整的层级：三件套初始密钥、四段 ECDH、根密钥、链密钥、消息密钥、群组的发送方密钥、通话的 SRTP 主密钥、应用状态的 Base Keys、设备绑定的链路密钥、校验用的 60 位安全码。本篇把**十二个类别下的 55 个条目**逐条列进附表 D，每一条都带出处页与原文锚串，同时把官方**没写**的生命周期（例如签名预密钥的具体轮换天数）明确留在原样。

**第三，服务端到底看得到什么。**官方在隐私政策与法律页里逐字段列了自己收集与可披露的内容，同时在帮助中心写明"content"不可见。本篇把这两侧拆成附表 C 的**六个档位、50 个条目**：端到端加密 13 项、平台可见但不是内容 11 项、本机侧防线 10 项、不视为端到端加密 6 项、取决于你的选择 6 项、官方未定性 4 项。

**第四，东西留多久、谁能取走。**本篇把官方写出的留存与删除时点整理成附表 E 的**五个类别、37 个条目**（服务端消息留存 11、账号删除阶梯 10、法定保全与披露 9、备份与本机侧 4、依个案或未公开 3），并把五类法律程序各自能调取的字段集单独列成一节。

三条最容易读错的结论先说：

**第一，"端到端加密"在官方文本里同时是协议描述和定性标签，两个用法不共轴。**白皮书说所有聊天都用同一套 Signal 协议，无论是否被定性为端到端加密；帮助页则把支付、第三方智能体、以及选择让 Meta 托管消息的商家会话明文排除在"端到端加密"之外。所以"用的是同一套协议"与"被称作端到端加密"是两句话，前者是技术描述，后者是定性口径，官方文本自己承认二者可以分离。

**第二，同一个"30"在官方文本里横跨两个档位、四个主语，其中一档根本不是时长。**附表 B 里值是 30 的行共 **7** 行：**6** 行是时长、**1** 行是字节数。那 6 个时长行又分属四个不同的主语——未送达消息的服务端保留上限（这一个主语占 3 行：隐私政策里"保留上限"与"到期删除"各写了一句，执法披露指引又复述了一次）、被保留消息可以撤销保留的窗口、停用账号转为删除的等待期、仲裁条款退出通知的邮寄期限；第 7 行是白皮书安全码算法里的"取前 30 字节"，一个截断长度而不是时间。凡引用"30 天"必须同时报它绑的是哪个主语。

**第三，官方对"留存"给出的默认答复是否定式的，而所有肯定式表述都带主语。**"送达即删""常规业务中不保留消息"两句都没有宾语扩展空间；但只要出现"may""up to""a few weeks""case-by-case"，官方就同时给出了一个例外主语：未送达、媒体、备份、执法保全。本篇的判断是：**读这些页面时，把否定句当默认值、把带"may/up to"的句子当例外条款，比把任何一句话当"总体政策"更接近文本原意。**

本篇不提供任何规避平台检测、批量发送自动化、破解或提取加密数据、构造利用链的内容；第 12 章关于本机防线的全部内容只是把 Android 与 Apple 已经公开发布的文档表述对齐，不含操作步骤序列。

# 1 来源层、四个"加密"用词，以及 57 页怎么分的

本篇读了 **57** 页官方原文，分五个站点类型：**帮助中心 39 页、法律与政策文本 12 页、Apple 支持文档 2 页、Android 官方文档 2 页、WhatsApp 官方博客 1 页、加密白皮书 1 页**（白皮书按一页计入，实际取回 40 个内页）。其中 **2** 页本次未取回正文：一页提示"仅对特定地区可用"，另一页只取回导航链接列表；这两页在附表 A 逐行标注，全篇不引用其内容。剩下 55 页里本篇实际引用 **48** 页，另有 **7** 页取回后没有在本篇任何章节或附表落成口径——它们的主题分别落在受限账号、使用准则、商业应用隐私政策、Cookie、安全总页、高级聊天隐私公告与苹果侧备份加密层级，前两篇已经处理过受限账号与聊天隐私，其余几页与"哪些环节加密、谁能看到、留多久"这三问没有可对齐的独立表述。附表 A 因此多出第五列「本篇引用位置」，把这 7 页与 2 页未取回的一律标为**登记未引用**，不做沉默列出。

"加密"这个词在官方文本里有四个互不等价的用法，读之前必须先分开：

| 用法 | 典型原文 | 它在说什么 |
| --- | --- | --- |
| 协议描述 | All chats use the same Signal protocol outlined in this whitepaper, regardless of their end-to-end encryption status | 说**用了什么算法**，与定性无关 |
| 定性标签 | these conversations are not considered end-to-end encrypted | 说**平台如何称呼这段会话**，影响的是政策承诺范围 |
| 传输层义务 | protect the confidentiality of data in transit over public networks | 商业侧数据安全条款里的**传输中保密义务**，不是端到端 |
| 存储层描述 | Card and bank numbers are stored encrypted and in a highly-secured network | 支付场景的**静态加密**，与消息加密无关 |

第三行来自商业数据安全条款，它承诺的是"公共网络传输中的数据"，这在整个文本体系里是最容易和端到端混淆的一句——因为它也用了"confidentiality"和"in transit"。第四行来自端到端加密总述页的支付一节：官方在这一句之后立刻补了"However, because financial institutions can't process transactions without receiving information related to these payments, these payments aren't end-to-end encrypted"，也就是说静态加密的说明紧接着就被用来否定端到端定性。

按这四个用法回看附表 C 的六档，可以看清官方文本的分布：13 项落在"端到端加密"，11 项是"平台可见但不是内容"，10 项是本机侧防线，6 项被明文排除，6 项取决于用户选择，剩下 **4 项官方根本没有定性**。那 4 项不是"官方说不清"，而是官方在加密语境下没有把它们当作讨论对象——本篇不替它们发明定性。

**"取决于你的选择"这一档 6 项值得单独说明，因为它是全篇唯一一档会随用户操作在两档之间移动的。**典型例子是备份：WhatsApp 云端存储的备份被描述为"always end-to-end encrypted"，而 Google 账号备份则是"cloud storage provided and managed by Google"，端到端加密是"an option"。同一篇备份文档里两种去处给出两种默认，这一档的存在说明"WhatsApp 的备份是否端到端"这个问题无法脱离存储位置回答。

# 2 白皮书的密钥三件套与四段 ECDH

会话建立阶段官方定义了三种密钥，全部是 Curve25519：

- **身份密钥对（Identity Key Pair）**：安装时生成的长期密钥对，"A long-term Curve25519 key pair, generated at install time"。重装或换机才会更换。
- **签名预密钥（Signed Pre Key）**：中期密钥对，"medium-term Curve25519 key pair, generated at install time, signed by the Identity Key, and rotated on a periodic timed basis"。
- **一次性预密钥（One-Time Pre Key）**：一用即弃队列，"A queue of Curve25519 key pairs for one time use"，被请求后即从服务端存储中移除——"A One-Time Pre Key is only used once, so it is removed from server storage after being requested"。

**这三者的服务端可见性只有一侧。**注册时上传的是公钥材料："The WhatsApp server stores these public keys associated with the user's identifier"；同一节另有一句更一般的规定："No client authentication secrets are stored on the server."——注意这句的宾语是**认证密钥**，不是全部私钥材料；预密钥体系里服务端持有的是公钥加签名预密钥的签名，一次性预密钥的私钥在服务端存到哪一步，白皮书没有逐字段展开，本篇列入第 13 章未写明清单。

会话主密钥由**四段 ECDH**拼出，官方对退化情形只写了一句："If there is no One Time Pre Key, the final ECDH is omitted"。这句话的工程含义是：当服务端的一次性预密钥队列被耗尽，发起方**不会失败，而是静默降级**——四段变三段，仍然能建会话。这是本篇在附表 D 里标注为"生命周期"而非"强度"的一条：降级不报错，因此从协议文本上无法推断它在实际部署中发生的频率。

派生只有一步："The initiator uses HKDF to create a Root Key and Chain Keys"。HKDF 之前是 master secret，之后是根密钥与链密钥。

**签名预密钥的轮换天数官方未公布**，只给了"periodic timed basis"这个限定。这与很多人以为的"有固定天数"不一致，本篇不猜数字，直接留白（附表 D 该行写明"天数未公布"）。

一次性预密钥的**批次大小与补充阈值**同样未公布。白皮书写了它是"按需补充"的队列，没有给出队列长度、补充触发点，也没有给出"耗尽"的概率口径。这两项都在第 13 章清单里。

# 3 会话内的三层派生，与"每条都换"的确切含义

会话内是三层：根密钥 → 链密钥 → 消息密钥。三句定义式原文：

- "Root Key - A 32-byte value that is used to create Chain Keys"
- "Chain Key - A 32-byte value that is used to create Message Keys"
- "Message Key - An 80-byte value"

消息密钥的 80 字节被官方拆成三段：32 字节 AES-256 密钥、32 字节 HMAC-SHA256 密钥、16 字节 IV。这一拆分是理解"每条都换"的关键：**每条消息换的是这 80 字节，不是根密钥，也不是链密钥本身。**

链密钥的推进规则是唯一一处给出具体算式的："HMAC-SHA256(Chain Key, 0x02) 产生下一条链密钥"，官方同时给了一条结构性约束——"A chain is only ever used to send messages from one user, so message keys are not reused"。这句话的主语是**一条链**，结论是"同一发送方的消息密钥不会重复"，它没有说、也不打算说"全网不重复"。把它读成后者是本篇见到的最常见误读。

帮助页对同一件事的说法更口语："the keys change with every single message that's sent"，白皮书的措辞则是"The Message Key changes for each message transmitted, and is ephemeral"，并补了一句更强的性质：事后无法从会话状态重建。两者不冲突，但**粒度不同**：帮助页那句是"keys"（复数、无主语限定），白皮书那句精确到 Message Key。如果按帮助页的字面读成"根密钥与链密钥也每条换"，就和白皮书自己的派生层级矛盾。本篇在附表 F 里把这两句分别落在 Message Key 与 forward secrecy 两个条目下。

会话什么时候需要重建，官方给的是丢失条件："unless the session state is lost, which can be caused by an event such as an app reinstall or device change"。这句同时是第 6 章安全码变更通知的技术解释来源——两个页面在不同层说同一件事，写法完全不同。

# 4 前向保密的两层棘轮，与"5 秒"这个不属于任何一层的数字

白皮书把前向保密归给两层机制的组合："provides forward secrecy through the combination of both an immediate" 哈希棘轮与每次往返的 DH 棘轮。两处用词分别带引号出现："hash ratchet" 与 "DH ratchet"，其中前者作用于发送方的链密钥，后者在往返时才做一次 ECDH。

**两层的更新频率不同，这是第 3 章"粒度"问题的另一半。**哈希棘轮每条消息推进；DH 棘轮只在往返时更新。所以"每条都换"与"每次都做 ECDH"不是同一句话的两种写法——前者描述消息密钥，后者描述根密钥的输入。官方文本没有把这两者统一成一个词，本篇也不合并。

实时位置一节里，白皮书给了一条与前向保密直接相关的代价式："all chain keys on the path that led to its creation must be destroyed to preserve forward secrecy"，同时给出该链的推进步长约束："Legal values for D are positive powers of two"——D 只能取 2 的正幂。这是**全篇唯一一处官方直接写出"销毁密钥以换取前向保密"的代价**，也是本篇把它单列进附表 D"实时位置"类的原因：它说明前向保密不是免费的，实现上需要主动删除中间状态。

群组一节给出另一条完全不同的生命周期："Whenever a group member leaves, all group participants clear their Sender Key and start over"。这句的主语是**全部参与方**，触发条件是**任何一名成员离开**，动作是清空并重新开始。它没有给时间窗口，也没有区分主动退出与被移出。

通话一节则给出一个时间数字，而它不属于上面任何一层："the old SRTP crypto session is kept alive for up to 5 seconds after group update"。这是本篇认为全文档里最容易被误读成"5 秒安全窗口"的一句——它描述的是**旧会话在群更新后仍然可用**的长度，方向与"密钥轮换保护"相反：它是旧密钥的余命，不是新密钥的保护期。同一个"5"在官方文本里还以另一种身份出现两次（附表 B 里 `5` 占 4 行、跨上限与时限两档），所以引用它必须同时报主语。

# 5 校验层：60 位数字是怎么算出来的，以及它为什么"可选"

校验是全篇唯一一处官方给出**完整可复现算式**的机制，所以本篇把它单列一章。

白皮书 "Verifying Keys" 一节的定义式句子是："WhatsApp users additionally have the option to verify the keys of their devices and the devices of the users with which they are communicating in end-to-end encrypted chats, so that they are able to confirm that an unauthorized third party (or WhatsApp) has not initiated a man-in-the-middle attack."——注意括号里把 WhatsApp 自己列进了"未授权第三方"的候选。

**60 位不是 60 位，是两个 30 位。**原文："The 60-digit number is computed by concatenating the two 30-digit numeric fingerprints for each user's device Identity Keys."单个 30 位指纹的算法有六步，全部在白皮书里逐条写出：

1. 把该用户**全部设备**的公开身份密钥按字典序排序后拼接——"Lexicographically sort public Identity Keys for all of the user's devices, except any device linked to Cloud API and concatenate them"；
2. 对排序结果与用户标识符做迭代 SHA-512，共 **5200** 次——"Iteratively SHA-512 hash the sorted Identity Keys and user identifier 5200 times"；
3. 取最终哈希的**前 30 字节**；
4. 把 30 字节切成 **6 段、每段 5 字节**；
5. 每段按大端无符号整数解释后对 **100000** 取模，得到 5 位数字；
6. 六组五位数字拼成三十位——"Concatenate the six groups of five digits into thirty digits"。

这六步里有三处值得停下来。**第一，排序。**排序意味着这个码不绑定"哪台设备"，只绑定"这一组设备的集合与其顺序"；集合不变则码不变。**第二，5200 次迭代。**官方给了具体次数，这是全篇唯一一处给出迭代计数的口令派生参数——与之对照，设备绑定一节的 PBKDF2 也给次数（2 的 17 次方），但那是另一把口令（配对码），两者不要混。**第三，"取前 30 字节"。**这就是本篇在附表 B 里把 `30` 记为**同时属于时限与密码学参数两个档位、独占 7 行**的原因：同一个数字在帮助页里是"约 30 天"、在法律页里是"30 天后删除"、在这里是**字节数**。把第三种读成第一种，是把哈希长度读成天数。

**二维码与 60 位数字的关系不是"两种表示同一件事"。**白皮书列了二维码的四项内容：版本、双方用户标识符、双方全部设备的完整 32 字节公开身份密钥或 SHA-512 哈希（**排除已链接的 Cloud API 副设备**）、以及一个"该方是否链接了 Cloud API"的标志位。扫描时，"the keys and the Cloud API companion flag are compared to ensure that what is in the QR code matches the Identity Key and the Cloud API companion flag as retrieved from the server"。也就是说**二维码携带可比对的原始材料与标志位，60 位数字只是指纹**；帮助页那句"both as a QR code and a 60-digit number"把两者并列呈现，容易被读成等价。二者不等价的地方正是 Cloud API：白皮书明文"The 60-digit code can be used to verify all devices except a Cloud API companion"。这是本篇认为的**校验层最重要的一个限制**：人工比对的那串数字，覆盖不到 Cloud API 这一类副设备。

**"可选"这个词出现的次数与它的位置。**帮助页在同一个注记里写"The verification process is optional for end-to-end encrypted chats, and is only used to confirm that the messages and calls you send are end-to-end encrypted"，另一篇讲校验失败提示的页面又写了一遍"You can still continue to exchange messages with your contact. The verification process is optional."；而在同一篇端到端加密总述页更远处，官方写了一句方向相反的话："WhatsApp security issues can arise if you don't verify encryption codes."这两句都在官方文本里，本篇把它们记为附表 C 中"官方未定性"与"端到端加密"两档的交叉点：**校验被定义为可选，但"不校验"被定义为有安全风险——官方没有把这两句话调和成一个统一建议。**

**多设备必须逐台校验。**官方写："if you or your contact uses WhatsApp on multiple devices, you'll need to verify the security code on all of your and your contact's devices."——注意这句话在主语上是**双方的全部设备**，不是"你的设备"。

**自动校验与密钥透明。**人工比对之外还有自动路径："WhatsApp also provides automatic verification of these codes through a process called key transparency"。密钥透明在帮助页只有一句话加一个指向白皮书的链接，白皮书正文里则没有给出可核对的透明度协议细节，所以本篇把"密钥透明的具体验证协议"列入第 13 章未写明清单。

**码为什么会变。**帮助页给的三类原因是："likely because you or your contact reinstalled WhatsApp, changed phones, or added or removed a paired device"。这三类原因与第 2 章的身份密钥生命周期、以及白皮书 "Companion Device Removal" 一节里那句更硬的规定是同一件事的不同层——后者写的是：如果被移除的设备是 Cloud API，"the primary generates a new random Identity Key thereby invalidating all existing companions and existing end-to-end encrypted Signal sessions"。也就是说**移除 Cloud API 副设备这一动作会重新生成身份密钥，从而让全部既有副设备与既有会话失效**。这是全篇唯一一处官方写明"一个管理动作会强制更换身份密钥"的路径，它同时也是安全码整串变化的技术上游。

## 5.7 一处与校验同名但不同轴的功能：一次性口令的自动填充

`659113242716268` 与 `1807923459559900` 两页讲的是"用 WhatsApp 账号帮其它应用登录"，机制句分别是"WhatsApp will automatically and securely deliver the OTP directly to the app, making it easier for you to quickly and safely access the apps you need"与"If you tap or click 'autofill,' WhatsApp will seamlessly and securely share the code directly with the right app"。这两句里出现的对象是**同一台设备上的另一个应用**，与本章前面的"设备间校验"完全不同轴。

关键在于同页紧跟的那句定性：`659113242716268` 在描述自动交付之后立即写"Your message content is always protected. No one outside of the chat, not even WhatsApp, can read it."——**这句与"应用可以自动提交它收到的码"并列出现在同一页面**。本篇不推断这两句在实现层如何共存，只把它们作为口径并列记出，并给出该页同时提供的退出口：`1807923459559900` 写"If you choose not to allow WhatsApp to autofill the code, you can still manually enter it into the app yourself"，`659113242716268` 另写"Apps should always ask for permission before sending you a code via your WhatsApp account or any other kinds of messages"。也就是说自动填充是**可关的**，而官方对它的定性用词是"automatically and securely"与"seamlessly and securely"——**"securely"在这里没有配套的机制说明**，本篇把它列入第 13 章未写明清单（自动交付走的是哪条本机通道、由谁授权，页面未写）。

这项功能与"60 位安全码"没有任何关系：后者的英文是 security code，前者的英文是 one-time passcode/OTP，两者在附表 F 里分属"校验与信任"与"界面与功能"两类。把它混作一谈会把一个本机应用间接口读成端到端加密的一部分。

# 6 定义的边界：白皮书怎么定义端到端，六个被排除的子系统

白皮书 "Defining End-to-End Encryption" 一节给的是**设备端点定义**："WhatsApp defines end-to-end encryption as communications that remain encrypted from a device controlled by the sender to one controlled by the recipient, where no third parties, not even WhatsApp or our parent company Meta, can access the content in between."紧接着它把"第三方"也定义了："A third party in this context means any organization that is not the sender or recipient user directly participating in the conversation."

这两句合起来产生本篇最重要的一条判据：**判据不在"用了什么算法"，而在"接收端点是不是对方控制的设备"。**白皮书自己就用了这个判据来排除："WhatsApp does not consider communications with Meta services, or communications with businesses using Cloud API, to be end-to-end encrypted."

然后是那句必须与上面并列阅读的声明（白皮书 "Encryption Has No Off Switch" 一节）："All chats use the same Signal protocol outlined in this whitepaper, regardless of their end-to-end encryption status."

**本篇的处理方式是：把"是否同一套协议"记为技术事实，把"是否被称作端到端加密"记为平台定性，两者分列附表 C 的两个不同档位，不互相推导。**这会得出一个看起来别扭但文本上成立的结论：一段会话可以在协议层与端到端会话完全相同，而在定性层被排除。附表 C 的 6 条"不视为端到端加密"就是按定性层列的，它们的共同点不是算法不同，而是**端点归属或存储形态不同**。

同节还有一句关于私钥的总规定，值得逐字记下："The WhatsApp server has no access to the client's private keys, though when a business user uses the WhatsApp Business Platform Cloud API (as indicated via a system message), Meta will have access to their private keys."这句的结构是"总规则 + 一个例外"，而例外的宾语是 **Meta 可及商家的私钥**，不是用户的私钥。本篇在附表 D 把它归入"平台侧"类，原因就在这儿：它是全篇唯一一处官方直接写"某一方可接触私钥"的表述。

**"状态变更必须可见"是官方给出的少数几条承诺式规则之一，而且它有两个版本。**帮助页版："The encryption status of an end-to-end encrypted chat can't change without being visible to the user."白皮书版："The encryption status of an end-to-end encrypted chat cannot change without the change being visible to the user via a system message."两句差异只在一处：白皮书**指定了可见性的载体是系统消息**，帮助页没有。这是本篇在附表 F 里标出的精度差异之一，也是第 13 章口径冲突清单里"同一规则、两个精度"的典型例子。

最后，白皮书的结论段把整章压缩成四句，本篇照其结构复述：所有消息都用同一套 Signal 协议；发送方全部设备与接收方全部设备之间的消息、语音与视频通话被视为端到端加密；使用 Cloud API 的商业通信不被视为端到端加密，且会通过系统消息告知；副设备之间的消息历史同步与应用状态同步受端到端加密保护，**除非该副设备是 Cloud API**。

# 7 Cloud API：一个把定性挪走的开关，六个受影响的子系统

白皮书 "Implementation with Cloud API" 一节把"链接 Cloud API"当成一个会**逐子系统改变定性**的开关，并明确列出了六个子系统。这个列表本身比任何一句结论都重要，因为它是官方唯一一处成体系写出"哪些环节会因为一个设置而不再是端到端"：

| 子系统 | 官方口径 |
| --- | --- |
| 应用状态同步 | If a user links Cloud API, the App State is also shared with Cloud API and is not considered end-to-end encrypted |
| 通话建立 | Calls involving Cloud API (including when Cloud API is linked as a companion) are not considered end-to-end encrypted |
| 消息历史同步 | a copy of messages from recent chats is also transmitted to Cloud API... therefore the process of history syncing is not considered end-to-end encrypted |
| 副设备移除 | the primary generates a new random Identity Key thereby invalidating all existing companions |
| 群组 | Groups involving Cloud API are not considered end-to-end encrypted |
| 一对一（商家链接 Cloud API 时） | WhatsApp does not consider 1-1 chats with businesses that link Cloud API to be end-to-end encrypted |

四件事值得单独指出。

**第一，"共享的历史是受限的"这句被官方自己写了限定。**消息历史同步一条后面紧跟"The amount of history shared is also limited in this case"——**限制了，但没给数**。这是本篇列入未写明清单的一项，因为"受限的历史量"直接决定了这段会话在服务端侧的可读范围。

**第二，群组一节的规则是双向禁止加一个条件分支。**"Cloud API cannot join an end-to-end encrypted group, nor can a user or a group admin add Cloud API to an end-to-end encrypted group"——这是禁止；紧接着"The Sender Key is distributed to Cloud API for groups created by Cloud API. However, when Cloud API is linked as a companion, the Sender Key is not distributed to Cloud API and such a group is considered end-to-end encrypted"——这是分支。**同一个 Cloud API，作为"群创建方"时分发发送方密钥、群不算端到端；作为"被链接的副设备"时不分发、群算端到端。**这是本篇认为全文档里最容易被读反的一处，因为两句相邻、主语相同、结论相反，差别只在 Cloud API 扮演的角色。

**第三，"when Cloud API is linked as a companion"这个条件在六个子系统里出现两次、后果不同。**通话一条把"作为副设备链接"明确**包括**在非端到端之内（"including when Cloud API is linked as a companion"），群组一条却把同一条件**排除**在端到端之外（即此时算端到端）。这两个"包括/排除"方向相反，官方没有解释为什么；本篇只记录文本，不做调和。

**第四，系统消息是这套定性的唯一用户可见载体。**"When group messages are not end-to-end encrypted, it is clearly indicated by a system message"与"When a 1-1 chat with an organization is not end-to-end encrypted, it is clearly indicated by a system message"两句分别覆盖群与一对一，加上"Displaying End-to-End Encryption Status"一节里"If the user's WhatsApp client sees that it's communicating with a business that uses Cloud API, the client will display this to the user. The user can also double check the encryption status within the chat or in the business info section of their app."——也就是说官方给的核对路径是**两处**：会话内与商家信息页。

第三方智能体走的是另一条文本书。智能体条款里定性句是"The communications you exchange with 3P Agents are not end-to-end encrypted"，而原因句是"the Agent Provider receives, processes, and stores your messages in an unencrypted form"——注意这里的关键词从"not considered end-to-end encrypted"换成了"**unencrypted**"。帮助页那条则把智能体归到"由 Meta 的服务代管"："Because messages with third-party agents are managed by a service from Meta on behalf of the agent, these conversations are not considered end-to-end encrypted."两处措辞的强度不同（一个说"未加密"，一个说"不视为端到端加密"），本篇按各自原文分列，不取强的一侧作为统一口径。

支付是第三条排除路径，且它给的排除理由是**功能性的**而非端点性的：官方先写"Card and bank numbers are stored encrypted and in a highly-secured network"，再写"because financial institutions can't process transactions without receiving information related to these payments, these payments aren't end-to-end encrypted"——排除的原因是金融机构必须收到信息才能处理，这是三方定义里"直接参与对话的发送方或接收方"之外的组织必然介入的场景。

# 8 备份层：三种凭据、两处去处、一条"我们无法帮你"

备份是"取决于你的选择"这一档 6 项的主要来源，因为**同一份聊天历史有两个去处，两个去处的默认加密状态不同。**

去处一（WhatsApp 云端存储）在备份总览页 `481135090640375` 里是无条件的："Back up directly on WhatsApp. Your backup is always end-to-end encrypted. No one, not even WhatsApp or Meta, can access it."去处二（Google 账号）则是有条件的，同页写"Google storage: Back up to your Google Account. The cloud storage in your Google Account is provided and managed by Google"，端到端加密是"an option"——`407643231403807` 的原句是"You have the option to turn on end-to-end encryption for these backups"。同页还给出两处去处可互换："You can switch your backup provider at any time from Settings > Chats > Chat backup"。

但去处一紧接着被自己加了一条灰度限定："WhatsApp cloud storage is a new feature we're building and testing out and may not be available to you yet."本篇把这句记进附表 C 的"取决于选择"档，因为它意味着**"always end-to-end encrypted"这条强承诺，对相当一部分读者还不是可选项**。这是"文档强度"与"可用性"分离的教科书例子：承诺是真的，覆盖面是未文档化的。

**三种凭据。**端到端备份的保护方式官方列为三选一："protected by either a passkey, password, or a 64-digit encryption key"。备份文档里的操作分支同样写这三种："Tap Use passkey, or More options to use a 64-digit encryption key or create a password instead."其中 64 位密钥的来源是生成而非自选："A 64-digit encryption key is a long string of characters generated by WhatsApp"。

**三者皆无的后果是明确的、且不可由平台补救。**官方三句连在一起读："WhatsApp can't send you a copy, reset it, or restore your backup for you"。这句话是全篇最强的一条否定式承诺——它同时否定了代取、重置与恢复三种帮助路径。本篇把它与第 9 章"平台看不到内容"并置，因为二者是同一枚硬币：**平台不能读你的备份，也就不能替你解开它。**

**一个容易混淆的口令边界。**备份文档专门写了一句"This password is separate from the password you can set up for app lock."——备份口令与应用锁口令是两套凭据。这一句在附表 D 里被记进"备份凭据"类，因为它是官方少数几处主动划清两个口令边界的地方。

**系统层会绕过应用层的默认。**设备到设备与系统备份两条路径由系统文档规定：`489880259855635` 写明 WhatsApp 按你设定的频率备份，"To stop automatic chat backups, set the frequency to never"；Google 侧备份文档 `407643231403807` 给了两条硬性时点——"Each new backup replaces the previous one. There's no way to restore an older Google Account backup"与"Google may automatically delete backups that haven't been updated in five months"，另有一句绑定关系"WhatsApp backups are tied to the phone number and Google Account they're created with"。这三句合起来是备份层最容易漏掉的一条：**Google 去处没有历史版本，且更新停滞本身就是一种删除条件**——它与第 10 章服务端"未送达 30 天"那根轴无关，但同样是"多久之后拿不回来"。Apple 侧的支持文档则把端到端加密的层级放在 iCloud 的数据类别上，本篇只引用它的数据类别清单，不引用它对 WhatsApp 的推论。

**通行密钥在这一层与账号层的两次出现。**`1240827368203083` 给的是通行密钥的通用定义与它替代的凭据形态："Unlike traditional passwords or SMS-based verification codes, passkeys use advanced cryptography and your device's built-in security features—like your fingerprint, face, or screen lock—to verify your identity"，以及那句与第 12 章应用锁同向的边界声明"your biometric data (like your fingerprint or face) never leaves your device"。它与备份文档里"Tap Use passkey, or More options to use a 64-digit encryption key or create a password instead"是同一个凭据在两个用途上的两次出现：**账号登录用一次，备份解密用一次，而这两次的丢失后果不同**——前者可用其它登录路径恢复，后者官方明说不能代取。本篇不把它们写成同一个开关。

**这一层的结论只有一句：备份的加密状态是"存储位置 × 是否已开启端到端 × 系统备份是否被关掉"三个自变量的函数，而官方文本里只锁定了第一个自变量的默认值。**

# 9 服务端看得到什么：附表 C 的 11 项"平台可见但不是内容"

官方对"看不到内容"的表述一共有三句，强度递增：帮助页的"No one outside of the chat, not even WhatsApp, can read, listen to, or share them"；同页更远处的"WhatsApp has no ability to see the content of messages or listen to calls that are end-to-end encrypted. That's because the encryption and decryption of messages sent and received on WhatsApp occurs entirely on your device"；政府请求应对页的"WhatsApp cannot and does not produce the content of its user's messages in response to government requests"。第三句是**在执法场景下的能力自述**，比前两句更可核对，因为它给的是行为而非属性。

与这三句并列的，是官方明文列出的**平台可及或可披露的内容**。本篇把它们归成 11 项，逐条给出处与锚串（附表 C），这里只挑五条最容易读错的：

**（1）"送达即删"这句没有宾语扩展空间，但它不等于"什么都没留"。**法律页原句是"In the ordinary course of providing our service, WhatsApp does not store messages once they are delivered or transaction logs of such delivered messages"——宾语是消息**与该消息的交易日志**两项。它不覆盖媒体（见下），也不覆盖元数据。

**（2）媒体的时点与消息不同轴。**"Encrypted media may be stored for a few weeks on WhatsApp's servers after you've sent it."这句用的是"may"与"few weeks"，两者都未量化，本篇把"数周"的具体周数列入未写明清单。同时另一篇文档给了单次查看媒体的另一个时点：接收后需在 **14 天**内打开——"you'll need to open it within 14 days of it being sent"。**同一段媒体的"服务端留存"与"可打开窗口"是两个不同的数字，而后者不是前者。**

**（3）举报会把内容送给平台。**两处路径分别给出范围：举报一页写"WhatsApp receives up to five of the last messages they've sent to you"（以及最近五通通话记录），单次查看一页写被举报的媒体"will be provided to WhatsApp"。这里值得注意的是**"未打开的单次查看媒体在被举报时仍可恢复并交出"**——官方对该情形的表述是保留可恢复能力，而不是删除。也就是说"一次性查看"保护的是对方设备上的留存，不保护**举报路径**。

**（4）通话与消息日志只在收到合法命令后开始采集。**政府请求页写明"WhatsApp may start collecting message logs and call logs for a particular user"，其前提是收到对应的法律程序（美国法下的 Pen-Register 类命令）。这一项的意义在于它把"不保留"与"可以被要求开始保留"明确分成两种状态，而不是把后者算进前者。

**（5）设备与连接信息是被逐字段列举的收集项，而不是概括性描述。**隐私政策给的清单是"This includes information such as hardware model, operating system information, battery level, signal strength, app version, browser information, mobile network, connection information (including phone number, mobile operator or ISP), language and time zone, IP address, device operations information, and identifiers"。位置则是一句独立规定："even if you do not use our location-related features, we use IP addresses and other information like phone number area codes to estimate your general location (e.g., city and country)."——**这条与"是否使用位置功能"无关，因此它不属于"取决于选择"档，而属于"平台可见但不是内容"档**，本篇在附表 C 里这样归档。

还有一类必须单列：截图与录屏。隐私政策那句是"any user can capture screenshots of your chats or messages or make recordings of your calls with them and send them to WhatsApp or anyone else, or post them on another platform"；单次查看页面则更具体，写了收件人可以用相机翻拍，也写了"It's possible for iOS or Android users to record audio of view once messages using the native screen recorder"。这三句官方**都没有给定性**——它们既不在加密承诺的范围内，也不在任何设置项的控制下，所以本篇把它们放进附表 C 的"官方未定性"4 项里。把这 4 项当成"漏洞"或当成"已受保护"都是超出文本的读法；官方在这里只做了一件事：**声明它不在控制范围内**。

## 9.10 三项本篇补进去的"平台可见"面：状态、输入中、以及用户自己也能拿到的那份报告

**（1）在线状态与"最后上线"是四档可选受众，而不是布尔开关。**`419827870318306` 的定义句是"Last seen and online tells you if a user is online and the last time they used WhatsApp. If a user is online, they have WhatsApp open on their device and are connected to the Internet. However, this doesn't mean the user has read your message."——最后一句是**语义澄清**：在线不等于已读。可选受众官方列为四档："Choose Everyone, My contacts, My contacts except…, or Nobody"。同页还有一句互惠规则，它是这一项最容易被忽略的性质："If you don't share your last seen, you can't see other contacts' last seen."——**隐藏是双向的**。而最后一句"People online in a chat with you can see when you're typing"则把"正在输入"划到了另一个控制域：它**不在上面四档里，没有开关**。本篇把这三句合并记为"平台可见但不是内容"，因为它们给出的都是会话参与者的活动状态，而不是消息正文。

**（2）用户自己可以请求一份账号信息报告，而官方明确写了它不含什么。**`526463418847093` 是本篇认为整组来源里最被低估的一页：它给出的对象与第 9 章前十项同源（平台侧持有的账号信息），但路径是**用户自助**。原文三句："You can use the request account information feature to request a report of your WhatsApp account information and settings"、"Account information reports don't include your personal messages or information about channels or ads"、"Your report will typically be available three days after the date you requested it"。注意三处限定：**含"settings"（设置项也是平台侧记录的一部分）、不含消息内容、时点是 typically 而非承诺**。该页还给出两条边界：一是入口限制"You can't request account info on WhatsApp for Windows or Mac. You must switch to your primary device or WhatsApp Web."——与第 10 章"删号只能在主设备执行"是同一类主语限制；二是可周期性化"This will create a new report every month until you choose to turn off automatic reports"。**这一项的意义在于它提供了第 11 章之外的第二条核对路径**：执法披露的清单只能从文档读，而这份报告是官方给每个用户的、可重复生成的自查件。

**（3）应用权限被官方明确划在加密之外。**`1533984453785262` 只有四句话，其中两句是这一章的边界："WhatsApp might require access to information or applications from your phone"与"Turning off permissions might cause WhatsApp to lose certain functionality"，并且处置入口在系统而不是应用内——"you can control the permissions the app uses in your phone's settings"。本篇把它列进"平台可见但不是内容"的理由是：**权限决定应用能采集什么，而采集之后的可见性由第 9 章其它项决定**；两件事在文档里分属两页，读者常常把它们合成一个"隐私设置"。该页没有出现 encryption 一词——这不是疏漏，是官方自己把它划到了加密叙述之外。

# 10 留存阶梯：四个 30 天、四个 90 天，和一句"脱钩而不是删除"

附表 E 把官方所有带时限或期限口径的表述收在 37 行里，分成五类：服务端消息留存 11 行、账号删除阶梯 10 行、法定保全与披露 9 行、备份与本机侧 4 行、依个案或未公开 3 行。这一节只做一件事：把"同一个数字其实指四件不同的事"讲清楚，因为这是留存类文档最容易读错的地方。

## 10.1 六个"30 天"行落在 5 页上，指四个对象

- **未送达的消息**：隐私政策给的是形态加时限——"we keep it in encrypted form on our servers for up to 30 days as we try to deliver it"，并且同一页给出到期动作"If a message is still undelivered after 30 days, we delete it."；执法指引复述同一期限，用的是"undelivered messages are deleted from our servers after 30 days"。这是同一个规则的两种文字，不是两条规则。
- **停用账号转删除的等待期**："Deactivated WhatsApp accounts are deleted 30 days after deactivation."——对象是账号，不是消息。
- **撤销"保留"的窗口**："You'll have approximately 30 days to unkeep a message from the time it was kept."注意这里带 **approximately**，而前两处不带；它也不是删除承诺，而是一个可撤销期。
- **仲裁条款退出的邮寄期限**：服务条款里的 30 天与加密、留存都无关。

四组主语都写作"30 天"（隐私政策一页贡献两行，共 6 行），其中只有第一组同时给出了**保存形态**（encrypted form）。把"30 天后删除"当成对整个服务的留存承诺，是把账号级规则误读成消息级规则。

## 10.2 90 天同样指四件事，且第四件是"90 天之后"

- 执法保全：We will take steps to preserve account records in connection with official criminal investigations for 90 days pending our receipt of formal legal process.
- 消失消息的第三档：24 小时、7 天、90 天。
- 删号的正常上限：It may take up to 90 days from the beginning of the deletion process to delete your WhatsApp information.
- **90 天之后仍可能存在**：Copies of your information may also remain after the 90 days in the backup storage that we use to recover in the event of a disaster, software error, or other data loss event.

第四条是这一节最实的一条信息：官方**主动把 90 天写成下限语义**——超过 90 天的灾难恢复副本留存是被承认的状态，而不是例外。也就是说"最长 90 天"约束的是常规删除路径，不约束备份存储。

## 10.3 不活跃账号的两个数字是地域差，不是版本差

"Inactive accounts (not connected to WhatsApp) are generally deleted after 120 days of inactivity (365 days for US)." 这句有两处限定词值得保留：一是 **generally**，二是括号里把美国单独拉到 365 天。它同时说明"不活跃删除"与"主动停用删除"（30 天）是两条独立路径。附表 B 里 120 天占两行是因为删号页与另一页各写了一次，不是两个不同的 120 天。

## 10.4 删除承诺只覆盖两层，第三、第四层被明文排除

把删号页的句子按宾语排队，可以看到官方对"删除"的覆盖范围是**自我限定**的：

| 层 | 官方口径 | 是否在删除承诺内 |
| --- | --- | --- |
| 本机 | 由用户自行卸载或删除 | 不在服务端承诺内 |
| 服务端信息 | 最长 90 天 | 在 |
| 灾难恢复备份 | 90 天之后仍可能留存 | 明文不在 |
| 他人设备上的副本与群组信息 | 不受你删号影响 | 明文不在 |
| 频道更新 | 不删除 | 明文不在 |

后三条的原文分别是：it does not affect your information related to the groups you created or the information other users have relating to you, such as their copy of the messages you sent them；Updates you've shared in channels aren't deleted。这里最容易被忽略的是**对方设备上的那条消息**：端到端加密保证的是"传输与存储期间没人能读"，不是"我删了就全世界消失"。这两件事在文档里属于不同的句子，官方没有把它们混在一起，读者也不该混。

## 10.5 两处"脱钩"而不是"删除"

日志与标识的处理方式在删号页里是同一套逻辑的两句：

- We may retain certain log data in our databases even after you delete your account, but such data will be disassociated from any identifying information such that it can no longer be linked to your account.
- We also replace any instance of your account ID with a replacement identifier which can't be linked back to your account once it is deleted.

两句都用了"保留 + 切断回链"的结构，而不是"删除"。这在做数据流梳理时比"删除"更有用：**它承认数据还在，只承诺不可回链**。同页还有一句常被跳过的操作约束——account deletion can only be performed from your primary device, not from any linked devices，这与本篇姊妹篇讲的多设备体系直接相接：删除动作的入口本身就是主设备独占的。

## 10.6 媒体与消息不在同一根时间轴上

两条规则各说一件事："Encrypted media may be stored for a few weeks on WhatsApp's servers after you've sent it"（服务端，"may"加"few weeks"，两重未量化），与"you'll need to open it within 14 days of it being sent"（可打开窗口，硬时限）。**14 天不是服务端留存期，"数周"也不是可打开窗口**；前者是可用性约束，后者是存储约束。至于"数周"到底是几周，官方没写，本篇列入 13.4 的未写明清单。

## 10.7 消失消息与备份的交互是一句独立规定

"Disappearing messages will be deleted when a user restores from a backup." 这句的方向和直觉相反：不是"备份会把已消失的消息带回来"，而是"恢复备份时消失消息会被删掉"。同页那句"A kept message will be visible to everyone in the conversation and won't disappear when its duration expires"是它的对偶——一旦被 Keep，时长到期就不再删除。两句话合起来才构成消失消息的完整留存规则；只引其中一句都会得到半个结论。

**10.7 的补充：消失消息在群聊里的控制权是可以在"任何成员"与"仅管理员"之间切换的。**`515705483748188` 给出两条规定："In a group chat, any group member can enable disappearing messages. However, a group admin can change group settings to allow only admins to enable disappearing messages."以及一句常被忽略的作用域限定："The most recent duration you select controls new messages in the chat, not previous messages you've sent or received."——**时长改动只作用于新消息**。这两句合起来说明：10.7 那条"消失消息与备份"的规则，其触发权在群聊里本身是一个**可被管理员收紧的默认宽松项**，而收紧之前任何人都能让整组新消息在 24 小时／7 天／90 天后消失。本篇把它记在留存章而不是权限章，因为它改变的是**留存时长**这个量，而不是谁能访问什么。

# 11 执法披露：五级法律程序，一句能力自述，和两处必须并读的差异

## 11.1 官方给出的分层，按可调取对象的宽度排序

执法指引把美国法下的程序分成清晰的几级，本篇按"宾语宽度递增"重排：

1. **传票（subpoena）** → 基础订阅者记录，官方列举为 name, service start date, last seen date, IP address and email address，条文定位在 18 U.S.C. §2703(c)(2)。
2. **§2703(d) 法院命令** → "certain records or other information pertaining to the account, **not including contents of communications**"，官方给的示例是 numbers blocking or blocked by the user。
3. **搜查令（search warrant）** → 存储内容，官方列举为 "about" information, profile photos, group information and address book, if available，前提是 probable cause。
4. **紧急披露请求** → 不需要法律程序，但需要平台的独立判断：where we have a good faith reason to believe that the matter involves imminent risk of serious physical injury or death and that WhatsApp may have information to help avert the threat to life。
5. **国安信函（NSL）** → 官方明确它只对应两类信息：production of only two categories of information: name and length of service。另有一条否答：Does WhatsApp receive and respond to requests under Executive Order 12333? **No.**

这套分层里有两处是**平台自加的约束**而不是法条要求：一是"只检索特定化"，原文为 We will search for and disclose information that is specified with particularity in an appropriate form of legal process；二是同一句在政府请求问答页里的另一种表述——we review all requests individually; require that requests be made in accordance with the laws under which they are issued and our policies; and produce only information that is narrowly tailored to respond to the request。两句用词不同（particularity 与 narrowly tailored），但方向一致，且都出现在"披露范围"这个位置上。

## 11.2 那句最强的可核对表述，是能力句而不是承诺句

三句"看不到内容"里，最可核对的是执法页这句：**WhatsApp cannot and does not produce the content of its user's messages in response to government requests.** 它的强度来自结构：前半句说能力（cannot），后半句说行为（does not）。帮助页的"No one outside of the chat, not even WhatsApp, can read, listen to, or share them"是属性描述，需要读者接受密码学前提；而这一句直接给出**在特定场景下的行为结果**，因此它可以独立于宣传语被检验。

与之配套的是保全规则的否定式表述：We do not retain data for law enforcement purposes unless we receive a valid preservation request before a user has deleted that content from our service。这里有一个硬前提——**保全请求必须先到，删除动作必须后发生**。顺序反过来就不成立。这与 11.1 的第 3 级（搜查令可调取"存储内容"）合起来才完整：能被调取的"存储内容"，指的是本来就在存储的东西（关于信息、头像、群组、通讯录）与保全住的东西，而不是已送达的消息。

## 11.3 通知用户的权利，和被保留的两类例外

两句并列：We reserve the right to notify users about requests for their information before disclosing it unless we are prohibited by law from doing so or in exceptional circumstances, such as child exploitation cases or an emergency threat to life；与 We also reserve the right to provide delayed notice upon expiration of a specific non-disclosure period in a court order。

这里官方**没有承诺通知**，只承诺"保留通知的权利"，并同时保留两类不通知的情形与延迟通知的情形。把它读成"会通知用户"是超出文本的。

## 11.4 三条程序性细节，常被当成透明度但不属于透明度

- **不提供专家证言**：WhatsApp does not provide expert testimony support. 同段给出理由：WhatsApp records are self-authenticating pursuant to law and should not require the testimony of a records custodian.
- **成本补偿按账户计**：These fees apply on a per account basis. 另有"不寻常或繁重的请求可加收"与"涉儿童伤害、平台与用户伤害及紧急请求可豁免"。金额在本篇取回的文档里没有给出，列入 13.4。
- **审查主体与节奏**：专门的法律执法响应团队（LERT）逐条审查，包括紧急请求；对外披露节奏是 We share details about these requests and our responses twice a year。纸质收件地址写在指引开头（Attention: WhatsApp LLC, Law Enforcement Response Team），邮编 94025 与服务条款里仲裁退出、版权通知共用的地址相同——这是同一个物理地址在三个不同流程里出现，不是三条不同地址。

## 11.5 两处必须并读才能发现的差异

**第一处：基础订阅者记录的字段数在两个页面上不同。** 执法指引列 5 项（name, service start date, last seen date, IP address and email address），政府请求问答页列 6 项（多一项 device type）。两页都在讲同一类信息，但清单长度不同。本篇在附表 E 与附表 C 各按原页记录，不做归一化，也不推断哪一份是"完整清单"——问答页用的是 such as，指引页用的是 which may include，两者都是示例式列举，因此差异更可能是列举宽严而非范围变化。

**第二处：可采集与可留存被写成两种状态。**问答页承认在收到有效法律程序（如 Pen Register Trap and Trace Order）之后，WhatsApp may start collecting message logs and call logs for a particular user，并且给出采集字段：who the communication was to or from, the time it was transmitted and from which IP address, and the type of communication。这与"常规业务中不保留消息"并不矛盾——**它说的是"被命令之后开始保留"，而不是"一直在保留"**。把这两句分开读会得出两个相反的结论，放在一起读才得到官方真正写的东西：默认不保留，被合法命令时可以开始保留。

# 12 本机侧防线：十项控制，没有一项改变服务端的可见性

附表 C 的"本机侧防线"档共 10 项，本篇在这一节把它们按"控制对象"重排，因为这一档的十个条目其实分属三种完全不同的威胁模型。

## 12.1 第一类：防本机第二个使用者（3 项）

聊天锁的机制句是 Locked chats are hidden in a folder behind an extra layer of authentication，配套的通知句是 Notifications for locked chats hide content and contact information。应用锁的机制句最有信息量：The face and fingerprint authentication takes place entirely on your device. By design, WhatsApp can't access the biometric information stored by your device's operating system。

这句给出两件事：**生物特征不出设备**，以及因此平台无法访问它。但它同时也限定了强度——应用锁页写明仅适用于带指纹传感器、运行 Android 6.0 及以上的设备，并且同一篇文档承认可以用设备 PIN 解锁。也就是说应用锁的实际强度**等于设备锁屏的强度**，而不是"额外加了一层只有 WhatsApp 知道的秘密"。这是本机侧防线里最容易被高估的一项。

## 12.2 第二类：防未被信任的接入面（4 项）

严格账号设置被官方定义为 optional, lockdown-style security feature，开启后强制打开两步验证。受限聊天的三条则是本篇最关心的：includes an additional control that prevents people from using linked devices in chats；This update helps prevent the use of unofficial clients or AI agents in chats；以及既有高级聊天隐私的会话不会自动升级（that chat will not update to Restricted chat automatically）。

第三条是**存量与增量的区分**：新设置不追溯改变已有会话的定性。这一条在做迁移评估时很关键，因为它意味着"是否受限"是逐会话状态，不是账号级开关。

## 12.3 第三类：防客户端代码被改（2 项，且只覆盖 Web）

Code Verify 的定位句是 Code Verify adds an extra layer of security to end-to-end encryption when people use WhatsApp Web；它校验的是"你运行的代码和所有人运行的是否同一份"——the Code Verify browser extension simply confirms the underlying code on WhatsApp Web is the same as the code that everyone else is using。

这一项有三处限定必须一起引：

- **不读内容也不上报**：The extension doesn't log any data, metadata, or user data, and it doesn't share any information with WhatsApp. 另有同页一句 The extension won't read or access the messages you send or receive, and we won't know if you have downloaded the extension. 安装页 `639766781216714_web` 把边界再扩一句：The Code Verify browser extension won't access any other browser tabs or windows you have open.
- **不覆盖桌面端**：The Code Verify browser extension verifies code for WhatsApp Web and doesn't impact WhatsApp Desktop.
- **依赖其它扩展被停用**：The performance of the Code Verify browser extension might depend on pausing or disabling other browser extensions you're using.

第二条与第三条合起来给出这一项的真实边界：它是**Web 专用**的完整性校验，且在扩展互相干扰的环境里效力不确定。三色状态（绿勾、橙圈问号、红圈感叹号）是它的唯一用户可见输出；橙色有两种成因（网络超时与扩展干扰），**官方没有把这两种成因在界面语言上区分开**，这是本篇认为"校验状态可观察性"上的一个实际缺口。

## 12.3.1 校验失败时官方给的动作，比状态本身更有信息量

`3107025856262937_web` 这一页把"Validation Failure"的成因列成**三条**而不是两条：You have another extension installed that is interfering with our ability to verify the page. / The code running your WhatsApp Web page doesn't match the code everyone else is using. / Something went wrong on our side.

第三条与前两条性质不同——**它是平台自认的可能**。而该页给出的处置动作里有一句本篇认为是整组"本机侧防线"中最强的建议：If it has not been addressed as a known issue, log out of WhatsApp Web and use WhatsApp on your mobile device, as your messages on the web might no longer be private.

这句话有三层值得分开读。**第一**，它把"校验失败且非已知问题"与"消息可能不再私密"直接连起来——**这是官方唯一一处把校验状态与机密性失效并列表述的文字**。**第二**，它给的处置不是修复而是**降级回退**（退出 Web、改用移动端），也就是说这条防线在失败场景下不提供"继续安全使用网页版"的路径。**第三**，它把用户引向外部信源来判定是否已知问题：Check to see if it has been publicly addressed on the Engineering at Meta website or Engineering at Meta Twitter page——**判定"这是平台侧问题还是我这边问题"的责任被交给用户**，而该页同时承认"might have to reload WhatsApp Web to clear the warning"，即告警本身可能是假阳性。该页还给了取证入口：If you want to investigate the issue further, click the red validation failure warning and download the source code.

把三页合起来，本篇对 Code Verify 的口径总结如下（不加评价）：**覆盖范围**只有 WhatsApp Web，三个浏览器（Google Chrome、Mozilla Firefox、Microsoft Edge），隐身标签需单独启用；**承诺内容**是"代码是否与所有人一致"，不承诺消息安全性；**数据面**是不记录、不上报、不读消息、不访问其它标签页；**失败语义**是三成因、两色告警，且官方在未知问题情形下建议停用网页版。

## 12.4 第四类：落盘保护（1 项，且不在 WhatsApp 手里）

文件级加密决定备份与本机数据的实际落盘保护强度，官方 Android 安全文档给出的是算法层描述——Encrypt file contents with AES-256 in XTS mode，并配套密钥不落 TEE 之外的规则。本篇把它列在本机侧防线档，但要注明它的**执行主体是操作系统而不是应用**：WhatsApp 能选择是否使用系统提供的加密能力，不能决定它的强度。

## 12.5 这十项与"官方未定性"4 项的分界

附表 C 的另一档是 4 项"官方未定性"：单次查看可被相机翻拍（recipients can take a photo or video of your view once media with a camera or other device）、系统截图之外的原生录屏（It's possible for iOS or Android users to record audio of view once messages using the native screen recorder）、任何用户都可截图转交（any user can capture screenshots of your chats or messages or make recordings of your calls with them and send them to WhatsApp or anyone else）、商家可把收到的聊天用于自有营销。

这 4 项与本机侧防线 10 项的区别是结构性的：**10 项都有开关，4 项都没有开关**。翻拍是物理世界的行为，截图是操作系统的能力，商家营销是数据送达对方之后的事。官方对这 4 项只做了"声明不在控制范围内"这一件事，既没有把它们算进加密承诺，也没有提供任何关闭方式。本篇认为这是整篇文档里最诚实的一类条款——它不是缺陷披露，是能力边界的说明。把它们读成"加密失效"或读成"已有防护"，都超出文本。

## 12.6 两类不属于"开关"但同在本机侧的官方动作：主动通知与自查向导

**（1）平台会主动告诉你设备可能受威胁。**`641700318302674` 是本组里唯一一处**由平台发起**的本机侧动作，它的机制句是：If we believe that your device has come under threat, we may notify you about it directly via a WhatsApp chat.同页给出的威胁来源与叙述是："our teams are constantly working to stay ahead of threats, including from commercial spyware companies trying to deliver malware to people's phones. These attackers look for vulnerabilities in apps or the mobile phone operating system or try to trick users into clicking on malicious links or downloading malware"，以及它依赖的三条通道："This work includes relying on security reports, automated signaling, and deep collaboration with academics and industry peers."

这一项在附表 C 的归档方式值得说明：**它不是开关**（用户无法开启或关闭），也不是"平台可见内容"（它不新增平台能看到的东西），所以本篇把它作为"本机侧防线"的**第 11 项之外的补充项**记在正文而不进表。它真正的口径价值在于两点：一是主语是 we may——**通知是裁量行为而非义务**；二是通知渠道是"via a WhatsApp chat"，也就是**通过它正在告警的那条通道告警**。本篇不把这读成缺陷（间谍软件若已驻留设备，任何应用内通知都不是可信信道），但也不替官方补一句它没写的保证。

同页还有一句把端到端加密与本机威胁明确分层："That starts with end-to-end encryption, which secures your messages and calls as they are sent from one device to another so they cannot be read by WhatsApp or anyone else in between."——**"as they are sent from one device to another"是传输期间的承诺**；设备本身被攻破不在这句话的范围内。这是本篇认为整组文档里对"端到端加密不覆盖什么"说得最清楚的一句，尽管它没有用否定句式。

**（2）官方把分散的设置收进一个向导，但没有改变任何一项的语义。**`143990998547188` 的定性句是：WhatsApp privacy checkup allows you to choose the right level of protection for you. It allows you to access step-by-step guidance to help strengthen the security of your account and customize your privacy settings all in one place.该页列的是**四项入口**而不是四项新控制："Choose who can contact you. / Control your personal info. / Add more privacy to your chats. / Add more protection to your account."

本篇在附表 C 里把"聊天锁""受限聊天""严格账号设置"各自单列，而没有给检查向导单列一项，理由就在这里：**检查向导是聚合入口，不是机制**。它的口径价值是导航性的（官方承认这些控制分散到需要一份向导才能找齐），而不是安全性的。同页那句"You can choose the best audience for your personal information, like online status and activity"与 9.10 的四档受众是同一件事的两处表述，可以互为佐证。

**（3）还有一处官方自己建议使用文本外手段的地方。**`1142481766359885`（拉黑与举报页）在描述拉黑后果时写："When you block someone, the existing chat will remain in your chatlist. **You may want to take screenshots or back up a copy of your chat history.**"这句与 12.5 的"官方未定性"档形成有意思的呼应：在 12.5 里，截图是官方声明"控制不了"的行为；在这一页里，**截图是官方主动建议用户采取的取证动作**。两处不矛盾——前者讲的是"对方截图你拦不住"，后者讲的是"你自己留证据"——但它们合起来说明一件事：**在这套文本里，截图不是加密体系的一部分，而是加密体系之外的一种人工留存手段**。同一页另外两句也属于这一类：You can't block a group.（拉黑的对象限制）与 If you feel you or anyone else is in immediate danger, please contact your local emergency services.（安全升级路径不经过平台）——后者与第 11 章的紧急披露请求是**相反方向**的两条通道，一条是用户找执法机关，一条是执法机关找平台。

# 13 数字归档、未写明清单与口径冲突

## 13.1 68 行、45 个数字、五档

附表 B 把本篇取回的官方文档里所有出现过的关键数字收成 68 行，去重后是 45 个不同的数字值，分五档：时限 20 行、密码学参数 18 行、版本与日期 13 行、上限 10 行、法律条款 7 行。68 行对 45 个值，说明有 **14 个数字各占多行**（其余 31 个值各占 1 行，故 14 + 31 = 45，多出的 23 行来自重复值）。多占的行绝大多数是"同一数字在两个页面各出现一次"，例如 60 位、64 位、6 位各占 2 行；而 90 占 4 行、30 占 7 行——**去重时将"30 天"与"30 字节"归并为同一个值 30**，所以这 7 行里 6 行是时限、1 行是密码学参数。

真正需要警惕的是**跨档碰撞**——同一个数字在不同档里表示完全不同的量纲。附表 B 里有 4 组：30（时限"30 天"与密码学参数"取前 30 字节"）、5（时限"5 秒"与上限"5 条／5 个"）、100（上限"至少 100 人"与法律条款"责任上限 100 美元"）、14（时限"14 天"与版本与日期"Android 14"）。**引用这些数字时必须连量纲一起引**，否则两个毫不相干的口径会在转述中被合并。

## 13.2 三套"位数"，两套"字节"

密码学参数档的 18 行里，位数类有 3 组互不相干：60（单聊安全码十进制位数）、64（备份加密密钥）、6（两步验证原口令位数）。字节类也互不相干：80（消息密钥总字节数，由 32 加密密钥 + 32 校验密钥 + 16 IV 组成）、32（根密钥／链密钥／发送方密钥／SRTP 主密钥／链路密钥，五个不同对象共用同一个 32）、30（指纹码派生中截取的字节数）、40（配对密钥材料的位数——注意这条是位不是字节）。

**"32 字节"在本篇语料里至少对应五类不同的密钥**，这是最容易在二次转述里出错的一个数字：它不表示"WhatsApp 只有 32 字节密钥"，而表示这五种对象各自的长度恰好都是 32。

## 13.3 版本与日期档的 13 行里，6 行是平台版本号

这 13 行的分布是：白皮书版本号 3 行（Version 10 / Version 9 / Version 1）、年份 4 行（2016 端到端加密完成、2016 可追溯性一文复述、2019 视频通话攻击、2021 隐私政策生效）、Android 版本 5 行（6.0 应用锁、7.0 直接启动、9 通行密钥、11 加密策略二成为兼容性要求、14 文件名加密首选模式）、以及 1 行 Google 移动服务版本下限 241217000。**白皮书版本号与 Android 版本号在同一档但不同体系**，读表时不要把它们混成一条时间线。

## 13.4 未写明清单

本篇把取回文档里没有给出、但读者通常会期待给出的口径集中列出：

| 编号 | 期待的值 | 官方给到的程度 |
| --- | --- | --- |
| 未1 | 签名预密钥的具体轮换天数 | 只说 rotated on a periodic timed basis，无天数 |
| 未2 | 加密媒体"数周"的具体周数 | may be stored for a few weeks，两重未量化 |
| 未3 | 执法请求成本补偿的金额 | 只说按账户计与可加收，无金额 |
| 未4 | "特定化"的判断标准 | 给出 specified with particularity 的规则，未给判定方法 |
| 未5 | 第三方智能体侧的数据留存期 | 明文交回 Agent Provider：which determines how that data is used and retained once received |
| 未6 | 存储期的通用表 | The storage periods are determined on a case-by-case basis |
| 未7 | 一次性预密钥队列的初始长度与补充阈值 | 白皮书只说按需补充 |
| 未8 | 群通话旧会话 5 秒到期后的处置动作 | 白皮书给出保活的**目的**（To continue decrypting data encrypted with the old key while all participants transition to the new key）与**上限**（up to 5 seconds），未写 5 秒之后旧会话如何收场 |
| 未9 | 一次性口令自动交付所走的本机通道 | 两页只用 automatically and securely 与 seamlessly and securely 形容，未写实现与授权主体 |
| 未10 | 主动威胁通知的判定标准与覆盖面 | 原文为 If we believe that your device has come under threat, we may notify you，两个词（we believe / may）均未给判据 |

附表 E 的"依个案或未公开"档 3 行是这份清单里在留存维度上的子集。本篇的态度是：**未写明不等于不存在，也不等于可以为它填一个数**。

## 13.5 口径冲突：五处需要并读的地方

1. **校验是"可选"还是"必要"——两句在同一页上。**端到端加密说明页先写"The verification process is optional for end-to-end encrypted chats, and is only used to confirm that the messages and calls you send are end-to-end encrypted"，同页更远处又写"WhatsApp security issues can arise if you don't verify encryption codes. Please note, if you or your contact uses WhatsApp on multiple devices, you'll need to verify the security code on all of your and your contact's devices."另一篇"安全码无法验证"页则把第一句缩成"The verification process is optional. It is only used to confirm the messages and calls you send and receive are end-to-end encrypted."三句之间不逻辑矛盾（可选是规则，安全问题是后果，多设备是操作要求），但**"可选"的代价在文本里没有被量化**——它只说"can arise"，不说什么条件下必然发生。同时注意第二句里出现的是 **you'll need to**：在同一页面内，校验既是 optional 又是 need。
2. **Cloud API 作为副设备时，群组与 1-1 商家的定性方向相反。**群组一节把技术事实直接当作定性的充分条件："However, when Cloud API is linked as a companion, the Sender Key is not distributed to Cloud API and such a group is considered end-to-end encrypted."而通话一节给出的是同一个句式的技术事实——"The SRTP master secret or secret set for the call is not distributed to the Cloud API when it's linked as a companion."——但**没有**由此推出定性；定性在另一处被反向规定："WhatsApp does not consider 1-1 chats with businesses that link Cloud API to be end-to-end encrypted since messages and calls in these 1-1 chats are shared by the business to Meta to process the messages and calls on behalf of the business."于是"密钥未分发"这一个事实在群组里足以支撑"视为端到端加密"，在 1-1 商家场景里不足以支撑。白皮书用"All chats use the same Signal protocol outlined in this whitepaper, regardless of their end-to-end encryption status"把协议层与定性层切开，因此两句不互相否证——但**只读其中一条的转述一定会得出错误结论**。另需记住配套的那句能力限定："The WhatsApp server has no access to the client's private keys, though when a business user uses the WhatsApp Business Platform Cloud API (as indicated via a system message), Meta will have access to their private keys."——主语从 WhatsApp server 换成 Meta，这也是本篇把 Cloud API 列进"不视为端到端加密"而非"加密失效"的原因。
3. **基础订阅者记录字段 5 项对 6 项**（见 11.5 第一处）。
4. **"加密状态变更必须可见"有两种精度。** 帮助页只说 cannot change without being visible to the user；白皮书进一步指定机制（通过系统消息）。前者是效果承诺，后者是机制承诺，引用时应标明是哪一层。
5. **单次查看的直觉与文本相反。** 用户通常把"一次性查看"理解为"不可复制"，而官方在相邻位置明文承认翻拍、原生录屏以及举报路径下的媒体移交。**"阅后即焚"在这套文档里约束的是对方设备的常规留存，不约束截图、录屏与举报**，这是本篇认为整组口径里语义落差最大的一条。

## 13.6 术语层

附表 F 收 49 条术语，按七类分：协议与密钥 14、界面与功能 10、留存与披露 7、设备与绑定 5、本机与平台 5、校验与信任 4、群组与通话 4，分布在 21 个页面上。这一张表的存在理由很具体：官方文档里同一个中文概念对应多个英文写法（例如 end-to-end encryption、encrypt、encryption status、lockdown-style 都出现在不同层级），而同一个英文术语在不同页面又可能被赋予不同范围。附表 F 逐条给出该术语出现的页与英文原形，读者可以按页回查，不必依赖本篇的译法。

# 14 结论

本篇把"哪些环节是加密的、谁能看到、留多久"三个问题分开回答，最终得到三句可核对的结论。

**结论一：端到端加密在这套文档里是"定性标签"，不是单一技术开关。**四段 ECDH、三层密钥派生、两层棘轮、群组的发送方密钥与 5 秒窗口、通话的 SRTP 主密钥、实时位置的链销毁代价——这些是同一套 Signal 协议在不同子系统上的实例化，白皮书用 All chats use the same Signal protocol outlined in this whitepaper, regardless of their end-to-end encryption status 明确把协议层与定性层切开。因此"是否端到端加密"这个问题在这套文档里必须回答成**六条被明文排除的场景**（第三方智能体会话、代理侧未加密存取、Meta 代管的智能体会话、支付环节、Cloud API 场景下的商家私钥可及性、以及经此定性的商家通信），加上"取决于选择"的 6 项（Google 账号备份、商家选用 Meta 托管、智能体服务、云存储灰度、Business App 默认视为端到端、消息送达商家后按商家惯例处理），而不是回答成一句"全都有"或"全没有"。附表 C 的 13 项端到端、6 项不视为端到端、6 项取决于选择、4 项官方未定性——**定性轴上的这四档相加是 29 项**；另外 11 项"平台可见但不是内容"与 10 项"本机侧防线"分列另两根轴，三根轴合起来才是 50 项。

**结论二：平台可见性有三条边界，且三条都写在明面上。**第一条是"内容看不到"，最强的一句是执法页的 WhatsApp cannot and does not produce the content of its user's messages in response to government requests；第二条是"元数据与设备信息看得到"，附表 C 的 11 项逐条给出处；第三条是"被合法命令后可以从不保留切换为开始保留"——message logs 与 call logs 的采集是**命令触发**的，而不是默认状态。这三条之间没有矛盾，但把它们压缩成任一句单独的宣传语都会失真。

**结论三：留存不是一条时间轴，而是四根。**服务端消息（送达即删／未送达最长 30 天／媒体数周）、账号与删除阶梯（30 天停用转删除、90 天常规上限、120 或 365 天不活跃、90 天之后仍可能有灾难备份副本）、法定保全（90 天且必须在删除之前请求）、本机与备份（三种凭据、免费额度、凭据丢失即不可恢复）——四根轴上的数字互相不能替换。而删除动作本身只覆盖服务端信息，**明确不覆盖**他人设备上的副本、你创建的群组相关信息、频道更新，日志与账号标识采取的是"保留并脱钩／替换"而非删除。

本篇的全部结论只到"官方文本写了什么、写在哪一句、这句话的边界在哪"为止。以下三类内容本篇不写，也不因读者追问而补充：**批量发送与自动化操作的实现方法、账号池与多号轮换的操作步骤、平台检测与反垃圾机制的规避方式**。这三类内容不在官方文档口径里，把它们写出来不会增加任何可核对的信息，只会把一篇口径解读变成操作指南。

# 附表 A 官方页面登记清单与本篇引用位置（58 行）

| # | 页面标识 | 站点类型 | 本页取到的口径 | 本篇引用位置 |
| --- | --- | --- | --- | --- |
| 1 | `820124435853543` | 帮助中心 | 端到端加密总述、60 位安全码与二维码、校验可选、密钥透明、非端到端加密场景清单 | 附表 B／附表 C／附表 D／附表 F |
| 2 | `490592613091019` | 帮助中心 | 端到端备份的三种凭据、免费账号 2 GB、凭据丢失后平台无法恢复 | 附表 B／附表 C／附表 D／附表 E／附表 F |
| 3 | `1850567238795036` | 帮助中心 | 通行密钥绑定设备与生物特征、更换须先删旧的 | 附表 B／附表 D／附表 F |
| 4 | `1240827368203083` | 帮助中心 | 通行密钥的定义与它替代的凭据形态 | 正文 |
| 5 | `1524220618005378` | 帮助中心 | 安全码变更通知的三类触发原因 | 附表 B／附表 D |
| 6 | `1210420136490135_web` | 帮助中心 | WhatsApp Web 的 Code Verify 与校验失败提示（仅 web 平台可读） | 附表 B／附表 C／附表 F |
| 7 | `639766781216714_web` | 帮助中心 | Code Verify 浏览器扩展的安装与其日志口径 | 正文 |
| 8 | `613194783824713` | 帮助中心 | "安全码无法验证"提示的含义与校验可选定位 | 附表 F |
| 9 | `1807923459559900` | 帮助中心 | 安全码自动填充功能与它读取的凭据 | 正文 |
| 10 | `659113242716268` | 帮助中心 | 自动填充安全码的来源与设备范围 | 正文 |
| 11 | `372839278914311` | 帮助中心 | 联动设备上接收安全码的条件 | 附表 F |
| 12 | `3107025856262937_web` | 帮助中心 | "Validation Failure"警告的含义（仅 web 平台可读） | 正文 |
| 13 | `764072925284841` | 帮助中心 | 聊天锁的开关与其对通知的影响 | 附表 C／附表 F |
| 14 | `715385484388016` | 帮助中心 | 受限聊天、高级聊天隐私与它对联动设备的阻断 | 附表 C／附表 F |
| 15 | `673193694148537` | 帮助中心 | 消失消息三档时长、保留与取消保留窗口、与备份的关系 | 附表 B／附表 E／附表 F |
| 16 | `5590979194321336` | 帮助中心 | 一对一会话中消失消息的开关位置 | 附表 B |
| 17 | `515705483748188` | 帮助中心 | 群聊中消失消息的开关与谁能改 | 正文 |
| 18 | `1077018839582332` | 帮助中心 | 一次性查看媒体的 14 天打开窗口、加密媒体数周留存、被举报媒体会移交 | 附表 B／附表 C／附表 E／附表 F |
| 19 | `414631957536067` | 帮助中心 | 举报与屏蔽可调取的最近消息与通话记录范围 | 附表 B／附表 C |
| 20 | `1142481766359885` | 帮助中心 | 举报时随附提交的内容项 | 正文 |
| 21 | `444002211197967` | 法律页 | 执法披露矩阵：传票、法院命令、搜查令、NSL、保全 90 天、30 天未送达删除 | 附表 B／附表 C／附表 E／附表 F |
| 22 | `808280033839222` | 法律页 | 政府请求的应对原则、E.O. 12333 答复、每年两次报告、无法提供内容的自述 | 附表 B／附表 C／附表 E |
| 23 | `1206094619954598` | 法律页 | 反对消息可追溯性的立场与专责团队 | 附表 B／附表 F |
| 24 | `770041394526058` | 帮助中心 | 【本次未取回】页面提示仅对特定地区可用，正文未读回，全篇不引用其内容 | 登记未引用 |
| 25 | `526463418847093` | 帮助中心 | 请求账号信息的入口与该报告的范围 | 正文 |
| 26 | `828406668498455` | 帮助中心 | 不活跃账号删除政策的说明页 | 附表 B |
| 27 | `2138577903196467` | 帮助中心 | 删除账号的 30/120/365/90 天阶梯、日志脱钩、替换标识、仅主设备可执行 | 附表 B／附表 E／附表 F |
| 28 | `1726618467788240` | 帮助中心 | 应用锁的机型门槛、本机生物特征与后备 PIN | 附表 B／附表 C／附表 F |
| 29 | `1533984453785262` | 帮助中心 | 系统权限与加密无关的边界说明 | 正文 |
| 30 | `2635108359972899` | 帮助中心 | 通话中隐藏 IP 地址的选项及其保护对象 | 附表 C |
| 31 | `641700318302674` | 帮助中心 | 针对间谍软件攻击的应对与账号级措施 | 正文 |
| 32 | `1831251587214580` | 帮助中心 | 视频通话漏洞事件的披露口径：受影响人数与公民社会账号 | 附表 B |
| 33 | `846698564598022` | 帮助中心 | 严格账号设置清单中与安全相关的条目 | 附表 C／附表 D／附表 F |
| 34 | `143990998547188` | 帮助中心 | 隐私检查向导涵盖的设置项 | 正文 |
| 35 | `407643231403807` | 帮助中心 | Google 账号备份与端到端加密开关的关系 | 正文 |
| 36 | `489880259855635` | 帮助中心 | 关闭设备级备份以免聊天被系统备份带走 | 正文 |
| 37 | `506595211487528` | 帮助中心 | 注册验证码六位一次性、两步验证原为 6 位 PIN | 附表 B／附表 D |
| 38 | `1278661612895630` | 帮助中心 | 两步验证的作用与它不覆盖的范围 | 附表 F |
| 39 | `1920866721452534` | 帮助中心 | 6 位 PIN 正在升级为密码 | 附表 B／附表 D |
| 40 | `481135090640375` | 帮助中心 | 聊天备份的默认加密状态与去处 | 正文 |
| 41 | `419827870318306` | 帮助中心 | 最后上线与在线状态的可见性控制 | 正文 |
| 42 | `717472490411581` | 帮助中心 | 受限账号的触发与后果 | 登记未引用 |
| 43 | `https___www_whatsapp_com_legal_terms_of_service` | 法律页 | 服务许可条款中"未送达消息最长保存 30 天"、责任上限与仲裁条款 | 附表 B |
| 44 | `https___www_whatsapp_com_legal_privacy_policy` | 法律页 | 加密形态留存、送达即删、去标识化、IP 与区号粗定位、存储期依个案 | 附表 B／附表 C／附表 E／附表 F |
| 45 | `https___www_whatsapp_com_legal_messaging_guidelines` | 法律页 | 使用准则对内容与安全的一般约束 | 登记未引用 |
| 46 | `https___www_whatsapp_com_legal_third_party_agents_terms` | 法律页 | 第三方智能体的明文存储、五个上限与反规避条款 | 附表 B／附表 C／附表 E |
| 47 | `https___www_whatsapp_com_legal_business_data_security_terms` | 法律页 | 商家侧对"公共网络传输中的数据"的保护义务 | 附表 C |
| 48 | `https___www_whatsapp_com_legal_business_app_privacy_policy` | 法律页 | 商业应用沿用消费者版的留存与删除实践 | 登记未引用 |
| 49 | `https___www_whatsapp_com_legal_transparencyreports` | 法律页 | 【本次未取回】只取回导航链接列表，报告正文未读回，全篇不引用其内容 | 登记未引用 |
| 50 | `https___www_whatsapp_com_legal_cookies` | 法律页 | Cookie 与同类技术的用途及控制方式 | 登记未引用 |
| 51 | `https___www_whatsapp_com_security_` | 法律页 | 安全页对加密、校验与账号保护的整体叙述 | 登记未引用 |
| 52 | `https___blog_whatsapp_com_introducing_advanced_chat_privacy` | 官方博客 | 高级聊天隐私与受限聊天的推出说明 | 登记未引用 |
| 53 | `https___support_apple_com_en_us_102651` | 苹果文档 | iCloud 高级数据保护中属于端到端加密的数据类别清单 | 附表 B／附表 F |
| 54 | `https___support_apple_com_en_us_108770` | 苹果文档 | 苹果侧备份与同步数据的加密层级 | 登记未引用 |
| 55 | `https___developer_android_com_privacy_and_security_keystore` | 安卓文档 | Keystore 中密钥的不导出属性与安全导入的硬件门槛 | 附表 D／附表 F |
| 56 | `https___source_android_com_docs_security_features_encryption_file_based` | 安卓文档 | 文件级加密的密钥保护、TEE 边界与内联加密硬件限制 | 附表 B／附表 C／附表 D／附表 F |
| 57 | `WP` | 白皮书 | Signal 协议密钥体系、棘轮、群组发送方密钥、通话与实时位置、应用状态与设备绑定 | 正文／附表 B／附表 C／附表 D／附表 E／附表 F |
| **合计** | **57 页** | 安卓文档 2／官方博客 1／帮助中心 39／法律页 12／白皮书 1／苹果文档 2 | — | **引用 48／登记未引用 9** |
#ROWS=57 CITED=48 UNCITED=9

# 附表 B 数字归档五档（69 行）

| # | 档位 | 数字 | 语境 | 出处页 | 原文锚点 |
| --- | --- | --- | --- | --- | --- |
| 1 | 时限 | 30 天 | 离线收件人的消息在服务器上以加密形式保留的上限 | https___www_whatsapp_com_legal_privacy_policy | we keep it in encrypted form on our servers for up to 30 days |
| 2 | 时限 | 30 天 | 超期未送达即删除 | https___www_whatsapp_com_legal_privacy_policy | If a message is still undelivered after 30 days, we delete it |
| 3 | 时限 | 30 天 | 执法指引复述同一删除期限 | 444002211197967 | undelivered messages are deleted from our servers after 30 days |
| 4 | 时限 | 30 天 | 被保留消息可以撤销保留的窗口 | 673193694148537 | You’ll have approximately 30 days to unkeep a message |
| 5 | 时限 | 30 天 | 停用账号转为删除的等待期 | 2138577903196467 | Deactivated WhatsApp accounts are deleted 30 days after deactivation |
| 6 | 时限 | 30 天 | 仲裁条款退出通知的邮寄期限 | https___www_whatsapp_com_legal_terms_of_service | postmarked within 30 days |
| 7 | 时限 | 90 天 | 执法保全请求的有效期 | 444002211197967 | preserve account records in connection with official criminal investigations for 90 days |
| 8 | 时限 | 90 天 | 消失消息可选时长上限档 | 673193694148537 | disappear 24 hours, 7 days, or 90 days |
| 9 | 时限 | 90 天 | 删除账号后信息清空的正常上限 | 2138577903196467 | it may take up to 90 days |
| 10 | 时限 | 90 天 | 灾难备份副本可在 90 天之后继续存在 | 2138577903196467 | Copies of your information may also remain after the 90 days |
| 11 | 时限 | 120 天 | 不活跃账号的一般删除期限 | 828406668498455 | accounts are generally deleted after 120 days of inactivity |
| 12 | 时限 | 120 天 | 删号页对不活跃期限的复述 | 2138577903196467 | Inactive accounts are generally deleted after 120 days |
| 13 | 时限 | 365 天 | 美国地区不活跃删除期限 | 2138577903196467 | (365 days for US) |
| 14 | 时限 | 14 天 | 未打开的单次查看媒体自动过期 | 1077018839582332 | open it within 14 days of it being sent |
| 15 | 时限 | 24 小时 | 消失消息的最短档 | 5590979194321336 | Select 24 hours , 7 days , or 90 days |
| 16 | 时限 | 7 天 | 消失消息的中间档 | 673193694148537 | You can set messages to disappear after 24 hours, 7 days, or 90 days |
| 17 | 时限 | 5 秒 | 群通话更新后旧 SRTP 会话的保活时长 | WP | kept alive for up to 5 seconds after group update |
| 18 | 时限 | 35 天 | 已签名设备列表的最长有效期 | WP | Signed Device Lists are expired with a Time to Live of 35 days |
| 19 | 时限 | 48 小时 | 校验异常时发送方设备列表被压缩到的有效期 | WP | current device list to 48 hours or less |
| 20 | 时限 | 24/7 | 紧急请求团队的支持节奏 | 1206094619954598 | assisting law enforcement 24/7 with emergencies |
| 21 | 上限 | 5 | 可同时连接的第三方代理数量上限 | https___www_whatsapp_com_legal_third_party_agents_terms | You may connect up to five (5) 3P Agents at any time |
| 22 | 上限 | 5 | 举报个人聊天时上交的最近消息条数 | 414631957536067 | WhatsApp receives up to five of the last messages |
| 23 | 上限 | 5 | 举报时可能一并收到的最近通话记录条数 | 414631957536067 | basic info about the last five calls |
| 24 | 上限 | 2 GB | WhatsApp 云备份免费账号的存储额度 | 490592613091019 | Free accounts include 2 GB of storage |
| 25 | 上限 | 1,400 | 2019 年视频通话攻击中收到专项提示的用户数 | 1831251587214580 | We sent a special WhatsApp message to approximately 1,400 users |
| 26 | 上限 | 100 | 研究方判断该攻击至少覆盖的公民社会成员数 | 1831251587214580 | targeted at least 100 members of civil society |
| 27 | 上限 | 15 | Apple 端内端到端加密的数据类别数 | https___support_apple_com_en_us_102651 | 15 data categories &mdash; including Health and passwords in iCloud Keychain &mdash; are end-to-end encrypted |
| 28 | 上限 | 13 | 注册使用服务的最低年龄 | https___www_whatsapp_com_legal_terms_of_service | You must be at least 13 years old |
| 29 | 上限 | 13 | 连接第三方代理的最低年龄 | https___www_whatsapp_com_legal_third_party_agents_terms | under the age of 13 |
| 30 | 上限 | 3 | Code Verify 校验不通过时可出现的提示种类 | 1210420136490135_web | you will see one of these three messages |
| 31 | 密码学参数 | 60 | 单聊安全码的十进制位数 | 820124435853543 | both as a QR code and a 60-digit number |
| 32 | 密码学参数 | 60 | 安全码变更提示页复述的位数 | 1524220618005378 | both as a QR code and a 60-digit number |
| 33 | 密码学参数 | 64 | 备份加密的三种保护方式之一：手写密钥位数 | 490592613091019 | protected by either a passkey, password, or a 64-digit encryption key |
| 34 | 密码学参数 | 64 | 恢复备份时可选用的密钥种类 | 490592613091019 | use a 64-digit encryption key or a password instead |
| 35 | 密码学参数 | 6 | 两步验证原口令的位数 | 506595211487528 | Never share your 6-digit registration code or password |
| 36 | 密码学参数 | 6 | 两步验证正被升级的口令位数 | 1920866721452534 | WhatsApp is upgrading the 6-digit PIN used in two-step verification |
| 37 | 密码学参数 | 80 | 消息密钥的总字节数 | WP | Message Key - An 80-byte value |
| 38 | 密码学参数 | 32 | 根密钥与链密钥的字节数 | WP | Root Key - A 32-byte value |
| 39 | 密码学参数 | 30 | 指纹码派生中截取的字节数 | WP | Take the first 30 bytes |
| 40 | 密码学参数 | 5200 | 指纹码派生的哈希迭代次数 | WP | hash the sorted Identity Keys and user identifier 5200 times |
| 41 | 密码学参数 | 100000 | 指纹码每段取模的基数 | WP | reducing it modulo 100000 |
| 42 | 密码学参数 | 40 | 配对密钥材料的位数 | WP | random 40-bit linkCodePairingSecret |
| 43 | 密码学参数 | 8 | 配对码的 Base32 字符长度 | WP | 8-character, Base32 alphanumeric string |
| 44 | 密码学参数 | 1024 | LtHash 变体的位宽 | WP | A 1024-bit variant called LtHash16 |
| 45 | 密码学参数 | 65536 | 应用状态 Epoch 的随机取值上界 | WP | Epoch is selected randomly between 1 and 65536 |
| 46 | 密码学参数 | 256 | 加密算法名中的密钥长度 | https___source_android_com_docs_security_features_encryption_file_based | Encrypt file contents with AES-256 in XTS mode |
| 47 | 密码学参数 | 512 | 校验算法名中的摘要长度 | WP | or SHA-512 hash for all devices of both parties |
| 48 | 密码学参数 | 4096 | 内联加密硬件可支持的数据单元上限字节数 | https___source_android_com_docs_security_features_encryption_file_based | does not support data units larger than 4096 bytes |
| 49 | 法律条款 | 2701–2712 | 美国存储通信法的条文区间 | 444002211197967 | Sections 2701-2712 |
| 50 | 法律条款 | 2703 | 基础订阅者记录条文的款号 | 444002211197967 | basic subscriber records (defined in 18 U.S.C. Section 2703(c)(2)) |
| 51 | 法律条款 | 2703 | 强制调取记录类信息的命令条文 | 444002211197967 | A court order issued under 18 U.S.C. Section 2703(d) |
| 52 | 法律条款 | 12333 | 被明确答复为不接收请求的行政令编号 | 808280033839222 | Does WhatsApp receive and respond to requests under Executive Order 12333 |
| 53 | 法律条款 | 94025 | 执法请求纸质收件地邮编 | 444002211197967 | Menlo Park, California 94025 |
| 54 | 法律条款 | 94025 | 仲裁退出与版权通知共用地址的邮编 | https___www_whatsapp_com_legal_terms_of_service | 1 Meta Way, Menlo Park, CA 94025 |
| 55 | 法律条款 | 100 | 责任上限取两者中的较大者：美元额 | https___www_whatsapp_com_legal_terms_of_service | ONE HUNDRED DOLLARS ($100) |
| 56 | 版本与日期 | 2016 | 端到端加密在全应用完成的年份 | 820124435853543 | Since completing the implementation of end-to-end encryption in 2016 |
| 57 | 版本与日期 | 2016 | 可追溯性一文复述的部署年份 | 1206094619954598 | WhatsApp deployed end-to-end encryption throughout our app in 2016 |
| 58 | 版本与日期 | 2019 | 视频通话漏洞攻击被拦截的年份 | 1831251587214580 | In May 2019 we stopped a highly sophisticated cyber attack |
| 59 | 版本与日期 | 2021 | 隐私政策文本的生效日期 | https___www_whatsapp_com_legal_privacy_policy | Effective January 4, 2021 |
| 60 | 版本与日期 | 10 | 加密总览白皮书的当前版本号 | WP | Version 10 Updated October 30, 2026 |
| 61 | 版本与日期 | 9 | 白皮书上一版版本号 | WP | Version 9 Updated February 25, 2026 |
| 62 | 版本与日期 | 1 | 白皮书最初发布版本 | WP | Version 1 Originally published April 5, 2016 |
| 63 | 版本与日期 | 6.0 | 应用锁要求的 Android 最低版本 | 1726618467788240 | running Android 6.0+ |
| 64 | 版本与日期 | 7.0 | 直接启动特性引入的 Android 版本 | https___source_android_com_docs_security_features_encryption_file_based | a new feature introduced in Android 7.0 called Direct Boot |
| 65 | 版本与日期 | 9 | 通行密钥要求的 Android 最低版本 | 1850567238795036 | you’ll need an Android 9 version or later model device |
| 66 | 版本与日期 | 11 | 加密策略版本二成为兼容性要求的 Android 版本 | https___source_android_com_docs_security_features_encryption_file_based | CDD requirements for devices launching with Android 11 and higher |
| 67 | 版本与日期 | 14 | 文件名加密首选模式的起始 Android 版本 | https___source_android_com_docs_security_features_encryption_file_based | Since Android 14, AES-HCTR2 is the preferred mode |
| 68 | 版本与日期 | 241217000 | 通行密钥要求的 Google 移动服务版本下限 | 490592613091019 | Google Mobile Services version 241217000 or higher |
| **合计** | 时限 20／密码学参数 18／版本与日期 13／上限 10／法律条款 7 | **68 条** | — | — | — |
#ROWS=68 COLS=6

# 附表 C 加密定性与可见性六档（51 行）

| # | 档位 | 对象 | 出处页 | 原文锚点 |
| --- | --- | --- | --- | --- |
| 1 | 端到端加密 | 一对一文字消息 | 820124435853543 | No one outside of the chat, not even WhatsApp, can read, listen to, or share them |
| 2 | 端到端加密 | 消息、照片、视频、语音、文档、实时位置、动态与通话一并覆盖 | 820124435853543 | your messages, photos, videos, voice messages, documents, |
| 3 | 端到端加密 | 商业聊天同样使用 Signal 协议保护出站消息 | 820124435853543 | Every WhatsApp message is protected by the same Signal encryption protocol |
| 4 | 端到端加密 | 群聊（发送方密钥体系） | WP | random 32-byte Chain Key |
| 5 | 端到端加密 | 群成员离开后发送方密钥全部重建 | WP | Whenever a group member leaves, all group participants clear their Sender Key |
| 6 | 端到端加密 | 一对一通话 | WP | WhatsApp servers do not have access to the SRTP master secrets |
| 7 | 端到端加密 | 群通话 | WP | the old SRTP crypto session is kept alive for up to 5 seconds after group update |
| 8 | 端到端加密 | 实时位置的链式棘轮与前向保密 | WP | all chain keys on the path that led to its creation must be destroyed to preserve forward secrecy |
| 9 | 端到端加密 | 经服务器中转的通话仍是端到端加密 | 2635108359972899 | Calls relayed through WhatsApp servers are end-to-end encrypted, like all calls on WhatsApp |
| 10 | 端到端加密 | 个人对话不受商家侧自动化检测影响 | 820124435853543 | This does not apply to your personal conversations on WhatsApp, which always remain end-to-end encrypted |
| 11 | 端到端加密 | 端到端加密备份 | 490592613091019 | End-to-end encrypted backups are protected by either a passkey, password, or a 64-digit encryption key |
| 12 | 端到端加密 | 执法请求下不提供消息内容 | 808280033839222 | WhatsApp cannot and does not produce the content of its user’s messages in response to government requests |
| 13 | 端到端加密 | 加密状态变更必须可见 | 820124435853543 | The encryption status of an end-to-end encrypted chat can’t change without being visible to the user |
| 14 | 取决于选择 | Google 账号备份可自选是否端到端加密 | 490592613091019 | You have the option to turn on end-to-end encryption for these backups |
| 15 | 取决于选择 | 商家可选 Meta 托管消息与 AI 回复 | 820124435853543 | some can choose WhatsApp’s parent company, Meta, to securely store messages and respond to customers |
| 16 | 取决于选择 | 商家选用可选服务后不再视为端到端加密 | 820124435853543 | do not consider messages with these businesses to be end-to-end encrypted |
| 17 | 取决于选择 | 使用 Business App 的商家聊天默认视为端到端加密 | 820124435853543 | WhatsApp considers chats with businesses that use the WhatsApp Business app to be end-to-end encrypted by default |
| 18 | 取决于选择 | 消息被商家接收后改按商家自身惯例处理 | 820124435853543 | Once a message is received by a business, it will be subject to the business’s own privacy practices |
| 19 | 取决于选择 | WhatsApp 云存储备份仍在灰度 | 490592613091019 | WhatsApp cloud storage is a new feature we’re building and testing out |
| 20 | 不视为端到端加密 | 与第三方 AI 代理的会话 | https___www_whatsapp_com_legal_third_party_agents_terms | The communications you exchange with 3P Agents are not end-to-end encrypted |
| 21 | 不视为端到端加密 | 代理提供方以未加密形式接收与存储 | https___www_whatsapp_com_legal_third_party_agents_terms | the Agent Provider receives, processes, and stores your messages in an unencrypted form |
| 22 | 不视为端到端加密 | 由 Meta 服务代管的第三方代理会话 | 820124435853543 | Because messages with third-party agents are managed by a service from Meta on behalf of the agent, these conversations are not considered end-to-end encrypted |
| 23 | 不视为端到端加密 | 支付环节 | 820124435853543 | these payments aren’t end-to-end encrypted |
| 24 | 不视为端到端加密 | Cloud API 场景下平台可接触私钥 | WP | Meta will have access to their private keys |
| 25 | 不视为端到端加密 | 与 Meta 服务或 Cloud API 商业的通信定性 | WP | WhatsApp does not consider communications with Meta services, or communications with businesses using Cloud API, to be end-to-end encrypted |
| 26 | 平台可见但不是内容 | 送达后消息与交易日志不保存 | 444002211197967 | WhatsApp does not store messages once they are delivered or transaction logs of such delivered messages |
| 27 | 平台可见但不是内容 | 未送达消息以加密形式暂存 | https___www_whatsapp_com_legal_privacy_policy | we keep it in encrypted form on our servers for up to 30 days |
| 28 | 平台可见但不是内容 | 加密媒体可在服务器存分数周 | 1077018839582332 | Encrypted media may be stored for a few weeks on WhatsApp’s servers |
| 29 | 平台可见但不是内容 | 举报上送的最近消息 | 414631957536067 | WhatsApp receives up to five of the last messages they’ve sent to you |
| 30 | 平台可见但不是内容 | 被举报的单次查看媒体会交给平台 | 1077018839582332 | the media, or voice messages will be provided to WhatsApp |
| 31 | 平台可见但不是内容 | 通话日志与消息日志只在收到合法命令后开始采集 | 808280033839222 | WhatsApp may start collecting message logs and call logs for a particular user |
| 32 | 平台可见但不是内容 | 基础订阅者记录可依法提供 | 444002211197967 | name, service start date, last seen date, IP address and email address |
| 33 | 平台可见但不是内容 | 内容与备份存储位置 | 808280033839222 | Message content is only kept on your phone (and, if you choose, in the cloud-based backup storage for your phone) |
| 34 | 平台可见但不是内容 | 设备与连接信息的收集范围 | https___www_whatsapp_com_legal_privacy_policy | We collect device and connection-specific information when you install, access, or use our Services |
| 35 | 平台可见但不是内容 | 服务端不存客户端认证密钥 | WP | No client authentication secrets are stored on the server. |
| 36 | 本机侧防线 | 聊天锁把会话藏在本机再认证之后 | 764072925284841 | Locked chats are hidden in a folder behind an extra layer of authentication |
| 37 | 本机侧防线 | 锁定聊天通知隐藏联系人与内容 | 764072925284841 | Notifications for locked chats hide content and contact information |
| 38 | 本机侧防线 | 应用锁的生物识别不出设备 | 1726618467788240 | The face and fingerprint authentication takes place entirely on your device |
| 39 | 本机侧防线 | 严格账号设置以限功能换风险 | 846698564598022 | Strict account settings is an optional, lockdown-style security feature |
| 40 | 本机侧防线 | 受限聊天新增禁止链路设备参与 | 715385484388016 | includes an additional control that prevents people from using linked devices in chats |
| 41 | 本机侧防线 | 受限聊天阻止非官方客户端与 AI 代理 | 715385484388016 | This update helps prevent the use of unofficial clients or AI agents in chats |
| 42 | 本机侧防线 | 既有高级聊天隐私不会自动升级 | 715385484388016 | that chat will not update to Restricted chat automatically |
| 43 | 本机侧防线 | Code Verify 是给端到端加密加的额外一层 | 1210420136490135_web | Code Verify adds an extra layer of security to end-to-end encryption when people use WhatsApp on the web |
| 44 | 本机侧防线 | Code Verify 不读取消息也不上报 | 1210420136490135_web | The extension doesn’t log any data, metadata, or user data, and it doesn’t share any information with WhatsApp |
| 45 | 本机侧防线 | 文件级加密决定备份与本机数据的落盘保护 | https___source_android_com_docs_security_features_encryption_file_based | Encrypt file contents with AES-256 in XTS mode |
| 46 | 官方未定性 | 单次查看仍可被外部设备翻拍 | 1077018839582332 | recipients can take a photo or video of your view once media with a camera or other device |
| 47 | 官方未定性 | 系统截图之外的录屏能力 | 1077018839582332 | It's possible for iOS or Android users to record audio of view once messages using the native screen recorder |
| 48 | 官方未定性 | 任何用户都可截图并转交 | https___www_whatsapp_com_legal_privacy_policy | any user can capture screenshots of your chats or messages or make recordings of your calls |
| 49 | 官方未定性 | 商家可使用收到的聊天做自有营销 | 820124435853543 | businesses will be able to use chats they receive for their own marketing purposes |
| 50 | 平台可见但不是内容 | 托管方须保证公网传输保密与多因子远程访问 | https___www_whatsapp_com_legal_business_data_security_terms | WhatsApp will enforce use of appropriate protocols designed to protect the confidentiality of data in transit over public networks |
| **合计** | 端到端加密 13／平台可见但不是内容 11／本机侧防线 10／不视为端到端加密 6／取决于选择 6／官方未定性 4 | **50 条** | — | — |
#ROWS=50 COLS=5

# 附表 D 密钥与凭据十二类（56 行）

| # | 类别 | 密钥或凭据 | 生命周期与可见性 | 出处页 | 原文锚点 |
| --- | --- | --- | --- | --- | --- |
| 1 | 会话建立 | 身份密钥对（Identity Key Pair） | 安装时生成的长期 Curve25519 密钥对；重装或换机才会更换；服务端只存公钥 | WP | A long-term Curve25519 key pair, generated at install time |
| 2 | 会话建立 | 签名预密钥（Signed Pre Key） | 安装时生成的中期密钥对，由身份密钥签名并按周期性时间轮换；轮换天数未公布 | WP | medium-term Curve25519 key pair, generated at install time, signed by the Identity Key, and rotated on a periodic timed basis |
| 3 | 会话建立 | 一次性预密钥（One-Time Pre Key） | 安装时生成并按需补充的一用即弃队列 | WP | A queue of Curve25519 key pairs for one time use |
| 4 | 会话建立 | 一次性预密钥的用后即删 | 被请求后即从服务端存储中移除 | WP | A One-Time Pre Key is only used once, so it is removed from server storage after being requested |
| 5 | 会话建立 | 会话主密钥的四段 ECDH | 无一次性预密钥时省略最后一段 | WP | If there is no One Time Pre Key, the final ECDH is omitted |
| 6 | 会话建立 | 根密钥与链密钥的派生 | 发起方用 HKDF 从主密钥派生 | WP | The initiator uses HKDF to create a Root Key and Chain Keys |
| 7 | 会话建立 | 注册时上传的密钥材料 | 身份公钥、签名预密钥与一批一次性预密钥与账号标识关联存放 | WP | The WhatsApp server stores these public keys associated with the user's identifier |
| 8 | 会话内 | 根密钥（Root Key） | 用于创建链密钥的 32 字节值 | WP | Root Key - A 32-byte value that is used to create Chain Keys |
| 9 | 会话内 | 链密钥（Chain Key） | 用于创建消息密钥的 32 字节值 | WP | Chain Key - A 32-byte value that is used to create Message Keys |
| 10 | 会话内 | 消息密钥（Message Key） | 80 字节：32 字节 AES-256 密钥、32 字节 HMAC-SHA256 密钥、16 字节 IV | WP | Message Key - An 80-byte value |
| 11 | 会话内 | 消息密钥的每条更新 | 每条传输都是新密钥，事后无法从会话状态重建 | WP | The Message Key changes for each message transmitted, and is ephemeral |
| 12 | 会话内 | 链密钥的推进规则 | 同一条链只服务于一个发送方，密钥不重复使用 | WP | A chain is only ever used to send messages from one user, so message keys are not reused |
| 13 | 会话内 | 前向保密的两层棘轮 | 即时哈希棘轮与往返 DH 棘轮组合提供 | WP | provides forward secrecy through the combination of both an immediate |
| 14 | 会话内 | 会话重建的触发条件 | 会话状态丢失才需重建，例如重装应用或换机 | WP | unless the session state is lost, which can be caused by an event such as an app reinstall or device change |
| 15 | 群组 | 发送方密钥组件 | 群聊为每位成员建立随机 32 字节链密钥与随机 Curve25519 签名密钥对 | WP | random 32-byte Chain Key |
| 16 | 群组 | 成员退出后的重建 | 任何成员离开即让全部参与方清空发送方密钥重新开始 | WP | Whenever a group member leaves, all group participants clear their Sender Key and start over |
| 17 | 群组 | 附加发送方密钥 | 新成员加入时引入的密钥组件 | WP | Add-on Sender Key |
| 18 | 通话 | SRTP 主密钥 | 随机 32 字节，只驻留内存且仅在通话期间使用 | WP | random 32-byte SRTP master secrets |
| 19 | 通话 | 服务端对通话密钥的可见性 | 服务器无法接触 SRTP 主密钥 | WP | WhatsApp servers do not have access to the SRTP master secrets |
| 20 | 通话 | 群通话旧会话的存活 | 群更新后旧 SRTP 加密会话最多再存活 5 秒 | WP | the old SRTP crypto session is kept alive for up to 5 seconds after group update |
| 21 | 实时位置 | 实时位置链的推进步长 | D 只能取 2 的正幂：1、2、4、8、16、32 | WP | Legal values for D are positive powers of two |
| 22 | 实时位置 | 实时位置的前向保密代价 | 销毁创建路径上的全部链密钥 | WP | all chain keys on the path that led to its creation must be destroyed to preserve forward secrecy |
| 23 | 校验凭据 | 60 位安全码的算法来源 | 双方全部设备身份密钥排序后迭代 SHA-512 共 5200 次，取前 30 字节切六段取模 | WP | Iteratively SHA-512 hash the sorted Identity Keys and user identifier 5200 times |
| 24 | 校验凭据 | 安全码的两种呈现 | QR 码与 60 位数字并存 | 820124435853543 | both as a QR code and a 60-digit number |
| 25 | 校验凭据 | 校验的可选定位 | 帮助页明确校验只是可选确认动作 | 820124435853543 | The verification process is optional for end-to-end encrypted chats, and is only used to confirm that the messages and calls you send are end-to-end encrypted |
| 26 | 校验凭据 | 多设备需逐台校验 | 任一方有多台设备时要在全部设备上校验 | 820124435853543 | you’ll need to verify the security code on all of your and your contact’s devices |
| 27 | 校验凭据 | 自动校验与密钥透明 | 除人工比对外另有密钥透明流程自动完成校验 | 820124435853543 | WhatsApp also provides automatic verification of these codes through a process called key transparency |
| 28 | 校验凭据 | 安全码变更的三类原因 | 重装、换机、增删配对设备 | 1524220618005378 | likely because you or your contact reinstalled WhatsApp, changed phones, or added or removed a paired device |
| 29 | 设备绑定 | 链路密钥（Linking Secret Key） | 32 字节值，由副设备通过扫码传给主设备 | WP | Linking Secret Key - A 32-byte value |
| 30 | 设备绑定 | 配对码的构造 | 40 位随机值、Base32 八字符、PBKDF2 迭代 2 的 17 次方 | WP | random 40-bit linkCodePairingSecret |
| 31 | 设备绑定 | 配对码需重复输入 | 配对过程中要再输入两次 | WP | re-enter linkCodePairingSecret two additional times |
| 32 | 设备绑定 | 已签名设备列表的有效期 | TTL 不超过 35 天；校验异常时压到 48 小时以内 | WP | Signed Device Lists are expired with a Time to Live of 35 days or less |
| 33 | 设备绑定 | 传输通道的加密 | 设备列表与密钥材料通过 Noise 管道传输 | WP | Noise Pipes with Curve25519, AES-GCM, and SHA256 from the Noise Protocol Framework |
| 34 | 设备绑定 | 服务端不存认证密钥 | 客户端认证密钥不落服务端 | WP | No client authentication secrets are stored on the server. |
| 35 | 应用状态 | Base Keys 的轮换 | 定期轮换，设备从账号移除时也轮换 | WP | Base Keys are rotated periodically and when a device is removed from the account to provide eventual future secrecy |
| 36 | 应用状态 | 状态值的加密方式 | Encrypt-then-MAC：AES-256-CBC 与 HMAC-SHA2-512 分别用两个密钥 | WP | Apply Encrypt-then-MAC approach with AES-256-CBC keyed by Value Encryption Key and HMAC-SHA2-512 keyed by Value MAC Key |
| 37 | 应用状态 | 批量校验用的摘要结构 | 对每个变更的 32 字节 MAC 再做一次 HMAC | WP | the HMAC over the 32-byte MACs of each individual Mutation |
| 38 | 应用状态 | 密钥标识结构 | KeyID 由 4 字节 Epoch 与 2 字节 DeviceID 组成，Epoch 在 1 到 65536 间随机 | WP | The KeyID is composite and consists of 4 byte Epoch and 2 byte DeviceID |
| 39 | 应用状态 | 轮换周期的示例 | 白皮书在应用状态一节举例说明轮换频率 | WP | clients rotate the key periodically (for example once a month) |
| 40 | 账号凭据 | 注册验证码 | 六位数字，一次性使用，不得与他人共享 | 506595211487528 | Never share your 6-digit registration code or password |
| 41 | 账号凭据 | 两步验证口令的迁移 | 原为 6 位 PIN，可能仍是 PIN 而非密码 | 506595211487528 | you might have created a PIN instead of a password |
| 42 | 账号凭据 | 两步验证正被升级 | 帮助页称 6 位 PIN 正在升级为密码 | 1920866721452534 | WhatsApp is upgrading the 6-digit PIN used in two-step verification to a password |
| 43 | 账号凭据 | 严格账号设置的强制项 | 开启后两步验证被打开 | 846698564598022 | Two-step verification is turned on. |
| 44 | 账号凭据 | 通行密钥的替代关系 | 替代口令与短信验证码，靠设备与生物特征或锁屏 | 1850567238795036 | Unlike passwords or SMS-based verification codes, passkeys are cryptographically secured |
| 45 | 账号凭据 | 通行密钥的更换路径 | 想换必须先删除旧的再新建 | 1850567238795036 | you'll need to delete your current passkey and create a new one |
| 46 | 备份凭据 | 备份保护的三种凭据 | 通行密钥、自设密码或 64 位加密密钥 | 490592613091019 | protected by either a passkey, password, or a 64-digit encryption key |
| 47 | 备份凭据 | 64 位密钥的来源 | 由 WhatsApp 生成的一长串字符 | 490592613091019 | A 64-digit encryption key is a long string of characters generated by WhatsApp |
| 48 | 备份凭据 | 凭据丢失的后果 | 三者皆无则无法读取；平台无法代取、重置或恢复 | 490592613091019 | WhatsApp can’t send you a copy, reset it, or restore your backup for you |
| 49 | 备份凭据 | 备份口令与应用锁口令相互独立 | 备份密码与为应用锁设置的密码是两回事 | 490592613091019 | This password is separate from the password you can set up for app lock |
| 50 | 平台侧 | Meta 对 Cloud API 商家私钥的可及性 | 商业用户使用 Cloud API 的场景下 Meta 可接触其私钥 | WP | Meta will have access to their private keys |
| 51 | 平台侧 | 协议不因定性而改变 | 所有聊天都用同一套 Signal 协议，无论是否被定性为端到端加密 | WP | All chats use the same Signal protocol outlined in this whitepaper, regardless of their end-to-end encryption status |
| 52 | 平台侧 | 协议库来源 | 基于开源 Signal 协议库 | WP | The Signal Protocol library used by WhatsApp is based on the Open Source library |
| 53 | 本机存储 | Android 文件级加密的密钥保护 | 除 CE 内部存储密钥外，其余密钥用各自 Keystore 密钥以 AES-256-GCM 加密且不离开 TEE | https___source_android_com_docs_security_features_encryption_file_based | Every key besides CE keys for internal storage is encrypted with AES-256-GCM using its own Keystore key that is not exposed outside the TEE |
| 54 | 本机存储 | 内联加密硬件的 IV 宽度限制 | 仅在只支持 32 位 IV 的合规 eMMC 硬件上使用相应标志 | https___source_android_com_docs_security_features_encryption_file_based | supports only 32-bit IVs |
| 55 | 本机存储 | 密钥导入的硬件门槛 | 安全导入加密密钥要求出厂 Keymaster 4 及以上 | https___developer_android_com_privacy_and_security_keystore | This feature is supported only on devices that ship with Keymaster 4 or higher |
| **合计** | 会话内 7／会话建立 7／校验凭据 6／设备绑定 6／账号凭据 6／应用状态 5／备份凭据 4／平台侧 3／本机存储 3／群组 3／通话 3／实时位置 2 | **55 条** | — | — | — |
#ROWS=55 COLS=6

# 附表 E 留存与披露条目（38 行）

| # | 类别 | 对象 | 官方口径 | 出处页 | 原文锚点 |
| --- | --- | --- | --- | --- | --- |
| 1 | 服务端消息留存 | 已送达的消息 | 送达后即从服务器删除，连事务日志也不保留 | 444002211197967 | WhatsApp does not store messages once they are delivered or transaction logs of such delivered messages |
| 2 | 服务端消息留存 | 未送达的消息 | 最长 30 天后删除 | 444002211197967 | undelivered messages are deleted from our servers after 30 days |
| 3 | 服务端消息留存 | 未送达消息的保存形态 | 以加密形式保存在服务器上，尽力投递期间最长 30 天 | https___www_whatsapp_com_legal_privacy_policy | we keep it in encrypted form on our servers for up to 30 days as we try to deliver it |
| 4 | 服务端消息留存 | 满 30 天仍未送达 | 删除 | https___www_whatsapp_com_legal_privacy_policy | If a message is still undelivered after 30 days, we delete it. |
| 5 | 服务端消息留存 | 常规业务中的消息 | 普通提供服务过程中不保留消息 | https___www_whatsapp_com_legal_privacy_policy | We do not retain your messages in the ordinary course of providing our Services to you. |
| 6 | 服务端消息留存 | 一次性查看的媒体与语音 | 需在发送后 14 天内打开 | 1077018839582332 | you’ll need to open it within 14 days of it being sent |
| 7 | 服务端消息留存 | 已发送的加密媒体 | 在服务器上可能保存数周 | 1077018839582332 | Encrypted media may be stored for a few weeks on WhatsApp’s servers after you’ve sent it |
| 8 | 服务端消息留存 | 消失消息的时长档 | 24 小时、7 天、90 天三档 | 673193694148537 | You can set messages to disappear 24 hours, 7 days, or 90 days after they’re sent unless that message is kept |
| 9 | 服务端消息留存 | 被保留（Keep）的消息 | 时长到期也不消失，对全对话可见 | 673193694148537 | A kept message will be visible to everyone in the conversation and won’t disappear when its duration expires |
| 10 | 服务端消息留存 | 从保留改回消失的窗口 | 自保留起约 30 天内可取消保留 | 673193694148537 | You’ll have approximately 30 days to unkeep a message from the time it was kept |
| 11 | 服务端消息留存 | 消失消息与备份 | 从备份恢复时被删除 | 673193694148537 | Disappearing messages will be deleted when a user restores from a backup |
| 12 | 账号删除阶梯 | 主动停用后的账号 | 停用后 30 天删除 | 2138577903196467 | Deactivated WhatsApp accounts are deleted 30 days after deactivation. |
| 13 | 账号删除阶梯 | 长期不活跃账号 | 一般 120 天，美国为 365 天 | 2138577903196467 | Inactive accounts (not connected to WhatsApp) are generally deleted after 120 days of inactivity (365 days for US). |
| 14 | 账号删除阶梯 | 删除流程的总时长 | 自删除流程开始最长 90 天 | 2138577903196467 | It may take up to 90 days from the beginning of the deletion process to delete your WhatsApp information. |
| 15 | 账号删除阶梯 | 灾难与故障恢复备份中的副本 | 90 天之后仍可能留存 | 2138577903196467 | Copies of your information may also remain after the 90 days in the backup storage that we use to recover in the event of a disaster, software error, or other data loss event. |
| 16 | 账号删除阶梯 | 日志数据 | 删除账号后仍可能保留，但与身份信息脱钩 | 2138577903196467 | We may retain certain log data in our databases even after you delete your account, but such data will be disassociated from any identifying information such that it can no longer be linked to your account. |
| 17 | 账号删除阶梯 | 账号标识 | 被不可回链的替换标识取代 | 2138577903196467 | We also replace any instance of your account ID with a replacement identifier which can’t be linked back to your account once it is deleted. |
| 18 | 账号删除阶梯 | 删除操作入口 | 只能在主设备执行，不能在联动设备执行 | 2138577903196467 | account deletion can only be performed from your primary device, not from any linked devices |
| 19 | 账号删除阶梯 | 只卸载应用而未用删除账号功能 | 信息留存时间更长 | https___www_whatsapp_com_legal_privacy_policy | if you only delete WhatsApp from your device without using our in-app delete my account feature, your information will be stored with us for a longer period |
| 20 | 账号删除阶梯 | 对方设备上的副本 | 删除自己账号不影响他人持有的那条消息 | https___www_whatsapp_com_legal_privacy_policy | it does not affect your information related to the groups you created or the information other users have relating to you, such as their copy of the messages you sent them |
| 21 | 账号删除阶梯 | 频道更新 | 账号删除后更新不自动删除 | 2138577903196467 | Updates you’ve shared in channels aren’t deleted. |
| 22 | 法定保全与披露 | 执法保全请求 | 收到正式法律程序前保全 90 天 | 444002211197967 | We will take steps to preserve account records in connection with official criminal investigations for 90 days pending our receipt of formal legal process. |
| 23 | 法定保全与披露 | 默认不为执法留存 | 未收到有效保全请求即不为执法目的留存 | 444002211197967 | We do not retain data for law enforcement purposes unless we receive a valid preservation request before a user has deleted that content from our service. |
| 24 | 法定保全与披露 | 披露范围的一般规则 | 只检索法律程序中特定化、且能合理定位的信息 | 444002211197967 | We will search for and disclose information that is specified with particularity in an appropriate form of legal process |
| 25 | 法定保全与披露 | 传票可调取的基础订阅者记录 | 姓名、服务起始日、最后可见时间、IP 地址、邮箱 | 444002211197967 | name, service start date, last seen date, IP address and email address |
| 26 | 法定保全与披露 | §2703(d) 法院命令可调取 | 不含通信内容，可能含屏蔽与被屏蔽号码 | 444002211197967 | which may include numbers blocking or blocked by the user |
| 27 | 法定保全与披露 | 搜查令可调取的存储内容 | 关于信息、头像、群组信息与通讯录 | 444002211197967 | which may include "about" information, profile photos, group information and address book, if available |
| 28 | 法定保全与披露 | 国安信函只对应两类信息 | 姓名与服务时长 | 444002211197967 | production of only two categories of information: name and length of service |
| 29 | 法定保全与披露 | 是否处理 E.O. 12333 请求 | 官方答复为 No | 808280033839222 | Does WhatsApp receive and respond to requests under Executive Order 12333? No. |
| 30 | 法定保全与披露 | 披露频率 | 每年两次公布请求与应对详情 | 808280033839222 | We share details about these requests and our responses twice a year in Meta’s Government Requests for User Data Report |
| 31 | 备份与本机侧 | 端到端备份的凭据形态 | 通行密钥、自设密码或 64 位加密密钥三选一 | 490592613091019 | protected by either a passkey, password, or a 64-digit encryption key |
| 32 | 备份与本机侧 | WhatsApp 云端备份的免费额度 | 免费账号含 2 GB 存储 | 490592613091019 | Free accounts include 2 GB of storage. |
| 33 | 备份与本机侧 | 凭据丢失的后果 | 平台无法代取、重置或恢复 | 490592613091019 | WhatsApp can’t send you a copy, reset it, or restore your backup for you |
| 34 | 备份与本机侧 | 服务器对通话密钥的可见性 | 服务器无法接触 SRTP 主密钥 | WP | WhatsApp servers do not have access to the SRTP master secrets |
| 35 | 依个案或未公开 | 存储期限的决定方式 | 依个案，取决于信息性质与法定留存需要 | https___www_whatsapp_com_legal_privacy_policy | The storage periods are determined on a case-by-case basis that depends on factors like the nature of the information, why it is collected and processed, relevant legal or operational retention needs, and legal obligations. |
| 36 | 依个案或未公开 | 签名预密钥的具体轮换天数 | 只说按周期性时间轮换，天数未公布 | WP | rotated on a periodic timed basis |
| 37 | 依个案或未公开 | 第三方智能体侧的数据留存 | 数据送达后由 Agent Provider 决定如何用与留 | https___www_whatsapp_com_legal_third_party_agents_terms | which determines how that data is used and retained once received |
| **合计** | 服务端消息留存 11／账号删除阶梯 10／法定保全与披露 9／备份与本机侧 4／依个案或未公开 3 | **37 条** | — | — | — |
#ROWS=37 COLS=6

# 附表 F 术语与口径对照（50 行）

| # | 类别 | 英文术语 | 中文口径 | 出处页 | 原文锚点 |
| --- | --- | --- | --- | --- | --- |
| 1 | 协议与密钥 | end-to-end encryption | 端到端加密 | 820124435853543 | end-to-end encryption |
| 2 | 协议与密钥 | Identity Key | 身份密钥（长期，安装时生成） | WP | Identity Key |
| 3 | 协议与密钥 | Signed Pre Key | 签名预密钥（中期，由身份密钥签名） | WP | Signed Pre Key |
| 4 | 协议与密钥 | One Time Pre Key | 一次性预密钥（用后即弃） | WP | One Time Pre Key |
| 5 | 协议与密钥 | Root Key | 根密钥（32 字节） | WP | Root Key |
| 6 | 协议与密钥 | Chain Key | 链密钥（32 字节） | WP | Chain Key |
| 7 | 协议与密钥 | Message Key | 消息密钥（80 字节，每条一换） | WP | Message Key |
| 8 | 协议与密钥 | Hash Ratchet | 哈希棘轮（即时推进那一半） | WP | Hash Ratchet |
| 9 | 协议与密钥 | DH ratchet | DH 棘轮（每次往返再做一次） | WP | DH ratchet |
| 10 | 协议与密钥 | HKDF | 用于从主密钥派生根密钥与链密钥的密钥派生函数 | WP | HKDF |
| 11 | 协议与密钥 | Curve25519 | 白皮书中所有 ECDH 与签名所用的椭圆曲线族 | WP | Curve25519 |
| 12 | 协议与密钥 | AES-256-GCM | 消息与通道加密使用的分组密码模式 | WP | AES-256-GCM |
| 13 | 协议与密钥 | HMAC-SHA256 | 消息密钥中用于完整性的那一段 | WP | HMAC-SHA256 |
| 14 | 协议与密钥 | forward secrecy | 前向保密 | WP | forward secrecy |
| 15 | 校验与信任 | security code | 安全码（60 位数字或二维码） | 820124435853543 | security code |
| 16 | 校验与信任 | key transparency | 密钥透明（自动校验流程） | 820124435853543 | key transparency |
| 17 | 校验与信任 | Code Verify | WhatsApp Web 的客户端代码校验扩展 | 1210420136490135_web | Code Verify |
| 18 | 校验与信任 | Your security code could not be verified | 安全码无法验证的提示语 | 613194783824713 | security code could not be verified |
| 19 | 群组与通话 | Sender Key | 群组发送方密钥 | WP | Sender Key |
| 20 | 群组与通话 | Add-on Sender Key | 新成员加入时引入的附加发送方密钥 | WP | Add-on Sender Key |
| 21 | 群组与通话 | SRTP master secret | 通话的 SRTP 主密钥 | WP | SRTP master secret |
| 22 | 群组与通话 | Live Location | 实时位置（白皮书单列一节） | WP | Live Location |
| 23 | 设备与绑定 | Linking Secret Key | 链路密钥（32 字节，副设备传给主设备） | WP | Linking Secret Key |
| 24 | 设备与绑定 | Signed Device List | 已签名设备列表（TTL 不超过 35 天） | WP | Signed Device List |
| 25 | 设备与绑定 | Noise Pipes | 配对传输所用的噪声管道 | WP | Noise Pipes |
| 26 | 设备与绑定 | linkCodePairingSecret | 配对码口令 | WP | linkCodePairingSecret |
| 27 | 设备与绑定 | linked devices | 已联动设备 | 372839278914311 | linked devices |
| 28 | 界面与功能 | end-to-end encrypted backup | 端到端加密备份 | 490592613091019 | end-to-end encrypted backup |
| 29 | 界面与功能 | passkey | 通行密钥 | 1850567238795036 | passkey |
| 30 | 界面与功能 | two-step verification | 两步验证 | 1278661612895630 | two-step verification |
| 31 | 界面与功能 | app lock | 应用锁 | 1726618467788240 | app lock |
| 32 | 界面与功能 | Chat Lock | 聊天锁 | 764072925284841 | chat lock |
| 33 | 界面与功能 | Restricted chat | 受限聊天 | 715385484388016 | restricted chat |
| 34 | 界面与功能 | Advanced chat privacy | 高级聊天隐私 | 715385484388016 | Advanced chat privacy |
| 35 | 界面与功能 | disappearing messages | 消失消息 | 673193694148537 | disappearing messages |
| 36 | 界面与功能 | view once | 一次性查看 | 1077018839582332 | view once |
| 37 | 界面与功能 | Strict account settings | 严格账号设置 | 846698564598022 | Strict account settings |
| 38 | 留存与披露 | basic subscriber records | 基础订阅者记录 | 444002211197967 | basic subscriber records |
| 39 | 留存与披露 | preservation request | 保全请求 | 444002211197967 | preservation request |
| 40 | 留存与披露 | search warrant | 搜查令（可及存储内容） | 444002211197967 | search warrant |
| 41 | 留存与披露 | traceability | 消息可追溯性 | 1206094619954598 | traceability |
| 42 | 留存与披露 | replacement identifier | 替换标识 | 2138577903196467 | replacement identifier |
| 43 | 留存与披露 | disassociated | 与身份信息脱钩 | 2138577903196467 | disassociated |
| 44 | 留存与披露 | case-by-case | 依个案决定的存储期 | https___www_whatsapp_com_legal_privacy_policy | case-by-case |
| 45 | 本机与平台 | Keystore | 安卓密钥库（密钥不导出） | https___developer_android_com_privacy_and_security_keystore | Keystore |
| 46 | 本机与平台 | TEE | 可信执行环境 | https___source_android_com_docs_security_features_encryption_file_based | TEE |
| 47 | 本机与平台 | File-Based Encryption | 文件级加密 | https___source_android_com_docs_security_features_encryption_file_based | File-Based Encryption |
| 48 | 本机与平台 | Direct Boot | 开机解锁前状态 | https___source_android_com_docs_security_features_encryption_file_based | Direct Boot |
| 49 | 本机与平台 | Advanced Data Protection | iCloud 高级数据保护 | https___support_apple_com_en_us_102651 | Advanced Data Protection |
| **合计** | 协议与密钥 14／界面与功能 10／留存与披露 7／本机与平台 5／设备与绑定 5／校验与信任 4／群组与通话 4 | **49 条** | — | — | — |
#ROWS=49 COLS=6

---

**边界声明：** 本篇是一份**公开文本口径梳理**，不是操作手册，也不是安全评估报告。全篇内容限于 WhatsApp 官方帮助中心与法律政策页面、WhatsApp 加密白皮书、Apple 与 Google 官方文档中已经公开写出的表述，引用目的只有两个：说明哪一层加密是协议保证、哪一层只是定性承诺；说明服务端与本机各自留多久、哪些字段可以在什么法律程序下被交出。本篇**不含**破解、提取、绕过或降级任何一层加密的方法与步骤；**不含**密钥恢复、取证提取、会话还原、中间人或重定向校验攻击的实现方式；**不含**批量发送脚本、账号池、多号轮换、设备农场、限速规避、垃圾信息检测绕过与封号规避；**不含**利用代码、恶意样本或利用链构造方式；**不含**任何针对他人设备的操作步骤与时序。第 11 章关于执法披露的内容是对官方自述文本与其引用的美国法条编号的逐字段对齐，不构成对任何法律程序的解释或建议；第 12 章关于本机防线的内容只对齐 Android 与 Apple 已公开文档的规定，不给出操作时序。本篇对 WhatsApp、Meta、Apple 与 Google 产品的加密强度、隐私水平与政策执行一致性**不作评价**，只报告页面写了什么、写在哪一句、这句话的边界在哪。凡官方页面未文档化的问题，全部列入第 13 章未写明清单与附表 E 留空，不以第三方统计、媒体报道或逆向结论补齐；本次未能取回正文的 2 个页面在附表 A 逐行标注，其内容未被引用。

Telegram: https://t.me/one00190

