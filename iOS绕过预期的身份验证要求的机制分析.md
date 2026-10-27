@@TITLE@@iOS 绕过预期的身份验证要求的机制分析
@@SUBTITLE@@同一个话题在两层的名字不一样，词频差 190 倍：Apple 安全响应顾问页本期 257 份镜像（容错解析后全语料 9,045 段 / 470 个段落名）里，字段文本含 `authentication` 的有 **142 段 / 82 页 / 36 个段落名**、含 `bypass` 的有 **298 段 / 111 页**，而 `authentic` 作为独立单词 **0 段**、`two-factor` **0 段**、`secure entry` **0 段**、`setup assistant` **0 段**、`single sign-on` **0 段**、`kerberos` **0 段**、`802.1x` **0 段**、`physically proximate` **0 段**；《Apple 平台安全保护》中文全量 178 页正文域里，"身份验证"只有 **5 次 / 3 页**，而"认证"**372 次 / 92 页**、"验证"**252 次 / 74 页**、"要求"**107 次 / 57 页**、"预期"**9 次 / 9 页**；最要紧的一条是：中文"绕过"全文只有 **23 次 / 9 页**，其中单页最高 **12 次**全部落在「激活锁安全性」，而那 12 次说的不是攻击者的动作，是设备管理服务**被设计出来**的"绕过代码"
@@INFO@@报告日期|2026 年 10 月 27 日
@@INFO@@分析对象|《Apple 平台安全保护》zh-CN 指南 179 页全量转储（正文域切分后 178 页可用，逐词普查）；Apple 安全响应顾问页 257 份英文页面镜像（容错解析后全语料 9,045 段 / 470 个不同段落名）。本篇认证词族 107 段 / 25 个段落名 / 34 个 CVE 编号 / 69 页；绕过词族 288 段 / 55 个段落名 / 88 个 CVE 编号 / 111 页；两族同段交集 29 段 / 7 种句式；"要求未满足"句式联盟 78 段 / 20 个段落名 / 46 页；全语料在野声明 93 段 / 12 个段落名 / 15 个 CVE 编号
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含针对他人设备的操作步骤、不含身份验证绕过的操作路径或时序、不含锁屏与配对绕过的任何可执行细节、不含凭证构造与会话重放细节
@@INFO@@分类|身份验证面 / 顾问页后果词汇与官方手册机制词汇的**双向错名**对照 / 一次"按词取族"与"按句式联盟取族"的闭合分区实测 / 同一个中文词"绕过"在攻击语义与管理语义之间的分裂
@@INFO@@难度|进阶（第 1 章的两层词表对照与第 5 章的"联盟 78 段全在 authentic 族内、却只有 7 段含 bypass"是全篇前提；跳过它们会把顾问页的段落名读成实现层，把手册层的"始终需要输入密码"清单读成实现承诺，把 `authentication` 的双义读成同一个对象）
@@INFO@@联系方式|Telegram: https://t.me/ios_one001
@@TOC@@

# 0 本篇要回答什么

选题字面是"绕过预期的身份验证要求"。这句话在 Apple 的两套公开文本里**没有一个共同的名字**，所以本篇的第一件事不是数漏洞，而是把这个名字拆开，看看它在每一层各自对应什么词、什么句式、什么对象。

拆开之后有三组反差，构成本篇的全部骨架。

**反差一（名字）**：措辞层（Apple 安全响应顾问页，下称 T2）不用"身份验证"这个中文说法，它用英文 `authentication`——字段文本（Impact + Description + 致谢）里 **142 段 / 82 页 / 36 个段落名**。但同一个词族里，`authentic` 作为**独立单词**是 **0 段**：151 段命中 `authentic` 这个子串，全部是 `authentication` / `unauthenticated` / `Authenticate` 的内部片段。也就是说"认证"在 T2 里从来不是一个可以被单独检索的抽象概念，它只出现在具体名词短语中。手册层（《Apple 平台安全保护》中文，下称 T1）反过来：正文域"认证"**372 次 / 92 页**、"验证"**252 次 / 74 页**，而完整词形"身份验证"只有 **5 次 / 3 页**（三页分别是 Apple 钱包中的证件 3 次、单点登录安全性 1 次、数字签名和加密 1 次）。选题字面"预期的身份验证要求"这七个字，在本报告自建的两层语料里，**一次都没有作为固定术语出现过**。

**反差二（宾语）**：T2 里 `bypass` 是 298 段 / 111 页的大词，但把它的**宾语**逐句归类后，宾语真的是"身份验证本身"的只有 **7 段**，而且这 7 段**全部挂在同一个段落名 `AirPlay` 下、分布在 7 个不同页面**。剩下 281 段的宾语是隐私授权（100 段）、完整性保护与签名（58 段）、沙盒（13 段）、锁屏与设置助理（4 段）、协议鉴权（2 段）、配对（1 段），以及 103 段不指向任何访问控制的其它句式；`quarantine`（隔离属性）和 `profile`（描述文件）作为 bypass 宾语**各 0 段**——这两个 0 是切分规则的产物而不是"没人绕过它"，第 6 章逐条说明。

**反差三（词义分裂）**：T1 全文只有 **23 次"绕过"**，分布在 9 页。单页最高是「激活锁安全性」**12 次**，占全手册该词的 52%。把那 12 次逐行读出来，它讲的是：`这使该服务可托管设备上的绕过代码，随后该绕过代码便可用于稍后关闭激活锁`、`设备管理服务需要创建一个 31 字节的绕过代码，然后在要启用设备激活锁时将其发送至 Apple 服务器`。这里的"绕过代码"是**企业设备管理服务为了合法关闭激活锁而持有的一段随机值**，是设计出来的管理凭据，不是攻击动作。同一篇里另一处「绕过」也是这个用法。所以"绕过"这个词在手册层的第一名，词义与选题相反。

于是本篇的五个问题依次是：

**问题一（第 1–3 章）** 两层的认证词表差额到底有多大，这个差额是不是采集或切分缺陷造成的假差额？答案：不是。T2 的 142 段可以用同一把切块刀在最新 10 页照常复现，且 `authentication` 的 171 次出现**没有任何其它屈折变体**（第 2.3 节的词形穷尽）；T1 的 5 次 / 3 页也不是漏采，而是该层根本不用这个构词——它的正式构词是"认证"与"验证"，两者重叠关系在第 2.1 节登记。

**问题二（第 4–6 章）** T2 的 `bypass` 288 段，宾语究竟是什么？本篇给出一个**闭合分区**（各分区之和恰等于 288，无未归类残留），并登记两个必须带限定才能读的 0。

**问题三（第 7–9 章）** T2 里"某个要求的检查没有生效"这句话，一共有几种写法，覆盖多少段？答案是一条**句式联盟**：9 种写法并集 **78 段 / 46 页 / 20 个段落名 / 14 条 Impact 原句**，这 78 段的 Impact 侧**全部**含 `authentic`（78 ⊂ 107），但其中只有 **7 段**同时含 `bypass` 这个动词——也就是说 T2 主要靠 `without` / `unauthenticated` / `after failing` 而非 `bypass` 来表达"要求没生效"。这个"包含于认证词族、却几乎不包含于绕过词族"的不对称，是本篇最重要的结构性发现，第 5 与第 8 章解释它为什么成立、以及它为什么不能当作因果证据。

**问题四（第 10–13 章）** T1 有没有写出"预期的身份验证要求"本身？有，而且是一份封闭清单：「视控 ID、面容 ID、触控 ID 和密码」一页给出 **8 项"始终需要输入密码"的操作 + 9 种"也需要使用密码"的设备状态**，逐字引用在第 10 章；配套的是"只允许五次不成功的匹配尝试"这类阈值句。本篇把这份清单与 T2 的 78 段并置，找出**两层各自说到而对方完全不说的部分**。

**问题五（第 14–18 章）** 本篇能否支持"身份验证方向没有在野利用"或"认证缺陷不重要"？都不能。第 17 章给出方向明确的不可外推清单，其中最容易写错的一条是：全语料 93 段在野声明与 `authentic` 族的交集是 **4 段**，且这 4 段是 `RPAC` 名下的 `bypass Pointer Authentication`（CVE-2025-31201）——那是**指针认证**，与用户身份验证无关（`authentication` 一词在 T2 里的双义，第 12 章单列）。把 4 段写成"在野利用只涉及指针认证"是可以的；由此推出"身份验证方向没有在野利用"是不可以的。

**写作边界（贯穿全篇）**：T2 的段落名是 Apple 的**归类动作**，不是实现层证据。`Authentication Services` 名下 4 段、`Passkeys` 名下 4 段、`Apple Account` 名下 5 段，本篇一律写成"顾问页把这条后果归在该段落名之下"，不写成"某个认证服务或某个子系统存在该缺陷"。T1 的机制句（例如"五次不成功的匹配尝试之后便会要求输入密码"）是**手册层的公开承诺**，其实现位置本轮不可核对：XNU 与用户态开源部件源码镜像本期为空，凡涉及判定发生在哪个进程、哪条链路的表述一律降级为"未核对"，并在第 16 章登记为缺口。所引 CVE 只用于说明公开修复事实与配置核查，不含绕过操作路径、不含时序细节、不含凭证构造。

---

# 1 两层的认证词表：一层 5 次，一层 142 段

本章把"差额有多大"钉成两张可复算的表，并先说明两张表的**分母**不同：T1 数的是**出现次数**（同一页多次计入），T2 数的是**段落数**（一个段落命中记 1）。

## 1.1 手册层（T1）正文域逐词普查

语料 = 《Apple 平台安全保护》zh-CN 指南全量转储 179 页。正文域切分规则与 #104–#108 相同：取最后一个逐字 `版权和商标` 行之后、首个含 `Apple Footer` 行之前。179 页中 **178 页可用**，1 页（页号 `sec2727afbbc`，页面标题即"版权和商标"）没有正文起始锚点，本篇把它排除在分母外并如实登记；可用页的正文窗口行数 **最少 9 行、最多 176 行**，所以"命中 0 次"必须结合窗口大小来读。

| 词形 | 次数 | 命中页数 | 备注 |
|---|---|---|---|
| 认证 | 372 | 92 | 本页层"authenticate"的正式对译，单页最高 29 次（安全隔区） |
| 验证 | 252 | 74 | 单页最高 18 次（Apple 钱包中的证件） |
| 身份验证 | 5 | 3 | **完整词形，全手册只有 3 页** |
| 双重认证 | 38 | 19 | 子串包含于"认证"，单页最高 5 次 |
| 单点登录 | 17 | 5 | 子串包含于"登录"；单页最高 12 次 |
| 密码 | 622 | 100 | 本页层最重的因子词，单页最高 53 次 |
| 口令 | 1 | 1 | 仅出现在「密码」页 1 次 |
| 触控 ID | 144 | 29 | 单页最高 36 次（配备触控 ID 的妙控键盘） |
| 面容 ID | 126 | 27 | 单页最高 25 次 |
| 视控 ID | 78 | 16 | 单页最高 22 次 |
| 生物识别 | 41 | 16 | 单页最高 7 次（生物识别安全性） |
| 生物特征 | 0 | 0 | 常见译法，**手册层不用** |
| 双因子 | 0 | 0 | 同上；"双因子认证"亦 0 |
| 多因子 | 0 | 0 | 同上 |
| 无密码 | 0 | 0 | 同上 |
| 通行密钥 | 2 | 2 | Passkeys 的中文词形，全手册 2 次 |
| 安全密钥 | 1 | 1 | 全手册 1 次 |
| 凭证 | 165 | 39 | 注意与"凭据"区分 |
| 凭据 | 0 | 0 | 手册层不用这个词形 |
| 令牌 | 70 | 15 | |
| 授权 | 172 | 63 | |
| 访问控制 | 16 | 9 | |
| 配对 | 161 | 35 | 含"未配对 / 配对模型"等 |
| 激活锁 | 61 | 7 | 单页最高 44 次 |
| 要求 | 107 | 57 | "预期要求"里的"要求"在本层是分散动词 |
| 预期 | 9 | 9 | 每页至多 1 次，无固定术语 |
| 绕过 | 23 | 9 | 单页最高 12 次，见第 3 章 |
| Kerberos | 13 | 2 | 拉丁词形保留，见第 11 章 |
| 门禁 | 37 | 10 | Gatekeeper 的中文词形 |

**重叠关系必须先登记**，否则上表不能相加：`认证` 包含 `双重认证`(38) / `身份验证` 包含 `验证`(5) / `单点登录`(17) 包含 `登录` / `访问控制` 与 `控制` 重叠 / `配对` 包含 `未配对` / `密码` 包含 `自动强密码` / `固件密码` / `无线局域网密码`。本篇所有表格里的数字都是**子串计数**，凡是需要"独立单词"语义的场合，一律用第 2.2 节的词边界重测值。

## 1.2 措辞层（T2）字段文本逐词普查

语料 = 顾问页 257 份镜像（`122085` 一页在切块后返回 0 段，属页面本身只有免责声明，已从语料分母排除），容错解析后 **9,045 段 / 470 个段落名**；带 `Impact:` 的 7,346 段、纯致谢 1,699 段、含 CVE 编号 7,342 段。下表第一列是**子串命中段数**，第二列是**加词边界后的段数**（`(?<![\w-])kw(?![\w-])`），差值就是短词伪命中。

| 词族 | 子串段数 | 词边界段数 | 页数 | 段落名数 |
|---|---|---|---|---|
| authentication | 142 | 142 | 82 | 36 |
| authenticate | 14 | 5 | 5 | 2 |
| authentic（独立单词） | 151 | **0** | 0 | 0 |
| unauthenticated | 计入 authentication 族 | — | — | — |
| bypass | 298 | 298 | 111 | 57 |
| credential | 29 | **0** | 0 | 0 |
| password | 23 | 20 | 19 | 6 |
| passcode | 10 | 10 | 10 | 4 |
| verification | 9 | 9 | 7 | 4 |
| verify | 14 | **0** | 0 | 0 |
| authorization | 198 | 198 | 64 | 52 |
| biometric | 1 | **0** | 0 | 0 |
| touch id | 0 | 0 | 0 | 0 |
| face id | 1 | 1 | 1 | 1 |
| two-factor | 0 | 0 | 0 | 0 |
| secure entry | 0 | 0 | 0 | 0 |
| setup assistant | 0 | 0 | 0 | 0 |
| activation lock | 2 | 2 | 2 | 1 |
| single sign-on | 0 | 0 | 0 | 0 |
| kerberos | 0 | 0 | 0 | 0 |
| 802.1x | 0 | 0 | 0 | 0 |
| eap | 75 | **0** | 0 | 0 |
| ipsec | 2 | 2 | 2 | 1 |
| pairing | 8 | 8 | 8 | 2 |
| paired | 15 | 13 | 13 | 3 |
| quarantine | 31 | 31 | 28 | 10 |
| gatekeeper | 53 | 53 | 30 | 16 |
| sandbox | 474 | 411 | 108 | 67 |
| physically proximate | 0 | 0 | 0 | 0 |
| man-in-the-middle | 0 | 0 | 0 | 0 |
| replay | 0 | 0 | 0 | 0 |
| spoof | 72 | 21 | 21 | 6 |
| mdm | 2 | 1 | 1 | 1 |
| profile | 3 | **0** | 0 | 0 |
| pin | 40 | **0** | 0 | 0 |

三个必须点名的伪命中：`eap` 的 75 段全部是 `heap` / `HeapSmasher` 的内部片段；`pin` 的 40 段全部是 `mapping` / `pinned` 一类；`credential` 的 29 段全部是 `credentials`。这三条正是"短词做子串检索"的标准陷阱，**本篇不使用左列的任何短词值**。

## 1.3 差额是不是采集缺陷

不是。三条证据：其一，同一把切块刀在最新 10 页（149034–149043，全部 `Released September 14, 2026`）切出 1,430 段，其中 `authentic` 族 **10 段**、`bypass` 族 **39 段**，两个词族在最新鲜批次都非零（第 15 章逐名）。其二，`authentication` 在 T2 的 171 次出现**词形穷尽后只有一种写法**，不存在被屈折变体漏掉的可能。其三，T1 侧 178 页可用、窗口最长 176 行，`身份验证` 的 3 页都是长窗口页面（Apple 钱包中的证件、单点登录安全性、数字签名和加密），不是被截断的短页。

---

# 2 `authentication` 在 T2 里不是一个概念，是两个

## 2.1 142 段的内部结构

字段文本含 `authentication` 的 142 段 / 82 页 / 36 个段落名，按 Impact 侧是否同时含 `bypass` 可以分开：Impact 含 `authentic` 的族共 107 段，其中 29 段的 Impact 同时含 `bypass`。剩下 35 段（142 − 107）的 `authentication` 出现在 Description 或致谢里而 Impact 不提——这类段的典型形态是 Impact 讲"某个 App 能读到数据"、Description 写 `An authentication issue was addressed with improved state management.`。**"认证"在 T2 里同时是后果名词和修复名词**，这是本篇所有按词取族计数都必须说明取的是 Impact 侧还是字段并集侧的原因。

## 2.2 `authentic` 独立单词 0 段

`authentic` 子串命中 151 段，但加词边界后 **0 段**。这个 0 不是"没有认证"，而是英语里 `authentic`（真正的 / 可信的）作为形容词在本语料从未独立使用，所有命中都嵌在 `authentication` / `authentications` / `unauthenticated` 里。也就是说：**"认证"这个词族在 T2 的书面形态只有名词，没有形容词，也没有 `authenticity`**（本篇未检索到，计入 0 处理需谨慎，已在第 16 章登记为待复算项）。

## 2.3 词形穷尽

`authentication` 的全部 171 次出现，去重后**只有 `authentication` 一种词形**，没有 `authentications`、没有连字符变体、没有大小写混排进入 Impact 字段。这条测量排除了一个常见质疑：142 段不是"匹配规则太宽"造成的虚高。

---

# 3 中文"绕过"23 次的词义分裂

T1 全手册"绕过"只有 23 次 / 9 页，本篇逐行读出，按**宾语**归类。

| 页 | 次 | 宾语 / 语义 | 与选题关系 |
|---|---|---|---|
| 激活锁安全性 | 12 | 绕过代码（设备管理服务托管） | **管理语义**，非攻击 |
| 在 macOS 中防范恶意软件 | 2 | 门禁绕过事件；用户绕过门禁运行程序 | 访问控制，但非身份验证 |
| 安全隔区 | 2 | 绕过操作系统密钥绑定；安全性控制可能被绕过（电压/频率包络） | 硬件防线语义 |
| 操作系统完整性 | 2 | 可绕过控制流程保护；TXM 遭入侵不会自动转化为绕过 SPTM | 内核防线语义 |
| 搭载 Apple 芯片的 Mac 的启动磁盘安全性策略控制 | 1 | 绕过最新软件提供的保护 | 版本降级语义 |
| 术语表 | 1 | 直接内存访问 (DMA) 允许硬件子系统绕过 CPU 直接访问主内存 | 定义句，总线语义 |
| 关于 App Store 安全性 | 1 | 恶意 App 尝试绕过设备端保护 | 泛化承诺句 |
| 在 macOS 中控制 App 对文件的访问 | 1 | 帮助确保它们不会绕过其他保护措施 | 隐私授权语义 |
| Apple Configurator 安全性 | 1 | 不允许管理员在非网络共享激活期间绕过任何正常所需的现有激活锁要求 | **唯一一句"绕过 + 激活锁要求"** |

三条结论。**其一**，23 次里有 12 次（52%）与"绕过预期的身份验证要求"语义相反——那是 Apple 为企业客户设计的合法能力，手册还规定了它的字节长度与随机性要求：`该服务为每台设备创建的绕过代码应为随机且唯一`。**其二**，唯一一句把"绕过"和"要求"接在一起的是 Apple Configurator 页，而且它是**否定式**的：`此功能不允许管理员在非网络共享激活期间绕过任何正常所需的现有激活锁要求`——手册用"绕过"这个词的时候，多数是在声明某条路径**不能**绕过。**其三**，中文"绕过"的宾语清单里没有一项是"身份验证"；最接近的是"激活锁要求"，而它在手册层出现的语境是管理约束，不是缺陷后果。

与之对照，T2 的 `bypass` 298 段的宾语分区在第 6 章；两层的"绕过 / bypass"宾语**交集只有协议鉴权一类**（T2 的 `bypass IPSec authentication` 2 段 vs T1 无对应句）。

---

# 4 认证词族 107 段全分解

Impact 侧含 `authentic` 的 107 段：**69 页 / 25 个段落名 / 34 个 CVE 编号 / 21 种 Impact 句式 / 16 种 Description 句式**。21 种句式合计恰为 107，无残差；16 种 Description 句式的头部 11 种合计 102 段，其余 5 种各 1 段（11 + 5 = 16 种，102 + 5 = 107 段，两向闭合）。

| 段落名 | 段数 |
|---|---|
| AirPlay | 14 |
| Safari Private Browsing | 11 |
| WebKit | 11 |
| Photos | 8 |
| curl | 7 |
| iCloud Document Sharing | 6 |
| Accounts | 5 |
| Apple Account | 5 |
| dyld | 5 |
| Authentication Services | 4 |
| Passkeys | 4 |
| RPAC | 4 |
| Passwords | 3 |
| Photos Storage | 3 |
| Screen Sharing | 3 |
| Shortcuts | 3 |
| Screen Sharing Server | 2 |
| Telephony | 2 |
| Accessibility / Compressor / Persona / Safari / Siri / Wallet / Wi-Fi | 各 1 |

**头部两句吃掉 31 段（占该族 29%）**，而且两句都是"某样东西在没有认证的情况下可以被看到 / 被访问"：

- `Photos in the Hidden Photos Album may be viewed without authentication` —— 16 段
- `Private Browsing tabs may be accessed without authentication` —— 15 段

这两句在 T2 里是**跨批次复用的模板**：同一句在多个批次的不同页面逐字出现，因此"16 段"不等于"16 个不同缺陷"，只等于"16 次同样的表述"。第 9 章给出模板复用与 CVE 的关系。

---

# 5 "要求未满足"句式联盟：78 段，全部落在 authentic 族内、但只有 7 段含 bypass

本篇定义一条**句式联盟**：字段文本命中下列 9 种写法之一的段落。写法是从 T2 原文归纳的封闭集，不是先验设定。

| 写法 | 段数 | 页数 | 段落名数 | 该写法命中的 Impact 句式数 |
|---|---|---|---|---|
| without authentication | 43 | 33 | 11 | 7 |
| after failing authentication | 9 | 9 | 3 | 1 |
| unauthenticated | 9 | 9 | 3 | 3 |
| bypass(es\|ing\|ed)? authentication | 7 | 7 | 1 | 1 |
| without pairing | 7 | 7 | 1 | 1 |
| authentication polic\* | 7 | 7 | 1 | 1 |
| without valid credentials | 5 | 5 | 2 | 1 |
| authentication flow | 5 | 5 | 1 | 1 |
| authenticate to | 5 | 5 | 2 | 1 |
| **相加** | **97** | — | — | — |

九种写法逐段相加 97 段，并集 **78 段 / 46 页 / 20 个段落名 / 14 条 Impact 原句**，重叠 19 段——例如 `An unauthenticated user on the same network as a signed-in Mac could send it AirPlay commands without pairing` 这一句的 7 段同时计入 `unauthenticated` 与 `without pairing` 两种写法，所以各列相加会超过并集。每一行的"段数"都等于该写法在 **Impact 侧**的命中段数（实测两列全等），所以本联盟不存在"靠 Description 或致谢凑数"的段。

有一条写法本篇**试过但没有纳入**：`without prompting` 在全语料有 17 段 / 17 页 / 2 个段落名，但这 17 段的 Impact 侧**没有一段含 `authentic`**，且与联盟**重合 0 段**。它们由两条句子承载：`A shortcut may be able to use sensitive data with certain actions without prompting the user`（14 段，`Shortcuts`）与 `An encrypted volume may be accessed by a different user without prompting for the password`（3 段，`DiskArbitration`）——前者是快捷指令的用户确认，后者是加密卷口令，两种都是"提示没弹出"而不是"身份核验被绕过"。把它们并进联盟会让 78 段变成 95 段并破坏下一段的包含关系，所以本篇把它登记为"形近而不义近"的排除项。

**结构性发现（限定为本期语料的观测）**：这 78 段的 Impact 侧**全部**含 `authentic`（78 ⊂ 107），且 9 种写法本身有 7 种以 `authentic` 为构词片段，剩下 2 种（`without valid credentials`、`without pairing`）命中的段也都因为同段另有 `authentication` 而进入 `authentic` 族——实测这 2 种写法没有一段是**单独**把段落送进联盟的。反过来不成立：107 − 78 = 29 段留在联盟外。

这里要登记一个**本篇初稿写错过的等式**。`authentic` 族与 `bypass` 词族的交集也是 29 段（`(?<![\w-])bypass(?![\w-])` 口径），与 107 − 78 得数相同，但**它们是两组不同的段落**：29 段里只有 22 段同时属于两侧。联盟与 `bypass` 词族的交集更窄，只有 **7 段**，而且这 7 段是同一条句子 `An attacker on the local network may be able to bypass authentication policy`。所以"要求未满足"在 T2 里**主要不是靠 bypass 这个动词写出来的**——14 条原句里 13 条靠 `without` / `unauthenticated` / `after failing` 写出，还有 1 条（5 段）`An app may be able to use the Sign In With Apple authentication flow to access the user's Apple Account` 连这四个词都没有，它是仅凭 `authentication flow` 进入联盟的，属于本联盟的**边缘噪声**，本篇如实登记而不清理：清理它联盟就变 73 段，后文所有 78 都要重算。

| 段落名 | 段数 | 段落名 | 段数 |
|---|---|---|---|
| AirPlay | 14 | Photos Storage | 3 |
| Safari Private Browsing | 11 | Screen Sharing | 3 |
| Photos | 8 | Shortcuts | 3 |
| iCloud Document Sharing | 6 | WebKit | 3 |
| Accounts | 5 | Screen Sharing Server | 2 |
| Apple Account | 5 | Accessibility / Compressor / Passwords / Persona / Safari / Siri / Wi-Fi | 各 1 |

联盟内 14 条 Impact 原句的头部（各句段数之和恰为 78）：

- 16 段 `Photos in the Hidden Photos Album may be viewed without authentication`
- 15 段 `Private Browsing tabs may be accessed without authentication`
- 9 段 `Password autofill may fill in passwords after failing authentication`
- 7 段 `An attacker on the local network may be able to bypass authentication policy`
- 7 段 `An unauthenticated user on the same network as a signed-in Mac could send it AirPlay commands without pairing`
- 6 段 `An attacker may be able to turn on sharing of an iCloud folder without authentication`
- 5 段 `An app may be able to use the Sign In With Apple authentication flow to access the user's Apple Account`
- 5 段 `An attacker on the network may be able to authenticate to Screen Sharing without valid credentials`
- 3 段 `A Shortcut may run with admin privileges without authentication`
- 1 段 ×5：Assistive Access 最近照片 / 已解锁设备的无线局域网密码 / unprotected Persona / Compressor 服务器任意代码执行 / Shake-to-undo 复原已删照片

最后五条各只 1 段，但它们是全联盟里**限定条件写得最全**的：`An attacker with physical access to a locked device may be able to bypass Digital ID authentication when iPhone Mirroring is active`（1 段）一条句子里就带了三重前提——物理访问、已锁定、iPhone 镜像处于活动状态。第 12 章用这条句与手册层"始终需要输入密码"清单做对照。

---

# 6 绕过词族 288 段的宾语闭合分区

`bypass` 在 T2 是 298 段（字段并集）；按 Impact 侧取族是 **288 段 / 111 页 / 55 个段落名 / 88 个 CVE 编号 / 42 种 Impact 句式 / 42 种 Description 句式**。下面按**宾语**做互斥分区，规则是首次命中优先，分区之和闭合。

| 分区 | 段数 | 段落名数 | 页数 | 代表句式 |
|---|---|---|---|---|
| B1 身份验证本身 | 7 | 1 | 7 | bypass authentication policy |
| B2 锁屏与设置助理 | 4 | 2 | 4 | Sidecar / iTunes Store |
| B3 沙盒与隔离 | 13 | 3 | 13 | bypass sandbox restrictions |
| B4 完整性保护与签名 | 58 | 17 | 34 | Gatekeeper / code signing / AMFI |
| B5 文件隔离属性 | 0 | 0 | 0 | 见 6.2 |
| B6 隐私授权 | 100 | 22 | 64 | bypass Privacy preferences |
| B7 网络与协议鉴权 | 2 | 1 | 2 | bypass IPSec authentication |
| B8 配对与近场 | 1 | 1 | 1 | Core Bluetooth |
| B9 配置与描述文件 | 0 | 0 | 0 | 见 6.2 |
| B10 其它 bypass 句 | 103 | 18 | 71 | ASLR / SOP / CSP / 内核内存保护 |
| B11 未归类 | 0 | 0 | 0 | — |
| **合计** | **288** | — | — | — |

## 6.1 B1 只有 7 段，且只有一个段落名

B1 的 7 段全部是 `AirPlay`，句式是 `An attacker on the local network may be able to bypass authentication policy`（7 段）。这就是"绕过预期的身份验证要求"这个选题在 T2 里按字面能取到的**全部**同族段落：7 段、7 页、1 个段落名。剩下 281 段的宾语不是身份验证。

## 6.2 两个 0 必须带限定

B5（隔离属性）与 B9（描述文件）各 0 段，是**分区规则造成的**，不是"该对象从未被绕过"。证据在同一个 288 段族内部：全语料 `quarantine` 有 31 段（28 页 / 10 个段落名）、`profile` 只有 3 段且加词边界后为 0；而 Description 侧存在固定句式 `A file quarantine bypass was addressed with additional checks.` **27 段**（24 页 / 8 个段落名）——这句话里"quarantine"和"bypass"都在，但它落在 **Description 字段**，本篇的宾语分区只看 Impact，所以 B5 得 0。**如果反过来把 Description 也算进宾语分区，B5 就是 27 段、分区表必须重算**。本篇选 Impact 侧，是为了保证分区之和等于 Impact 族的 288；换口径就换数字，不可混用。

## 6.3 B6 与 B4 才是这个族的重心

B6 隐私授权 100 段的头部是 `An app may be able to bypass Privacy preferences` 44 段 + `An app may be able to bypass certain Privacy preferences` 29 段 + `A malicious application may be able to bypass Privacy preferences` 14 段，三段模板吃掉 87 段。B4 完整性保护 58 段里 `Gatekeeper` 相关句式合计 44 段（19 + 10 + 7 + 4 + 4）。选题是"身份验证"，但 T2 里"绕过"这个词的实际负载在**隐私授权**与**签名校验**上——这层错位是本章唯一要留下的结论。

## 6.4 施事主语基线

Impact 侧含 `bypass` 的 288 段按施事粗分：`An app` / `A malicious application` / `A shortcut` 类本地发起占多数，`A remote attacker`（8 段 `bypass network filters`）与 `An attacker on the local network`（7 段 authentication policy）是仅有的两类非本地前提句式，`An attacker that has already achieved kernel code execution`（7 段）则是**显式写出前置条件**的句式——它是本篇唯一一条把"绕过某防线需要先拥有更高权限"写进 Impact 的句子形态，与手册层"即使拥有 root 或内核权限"的表述正好互为镜像（第 10.4 节）。

---

# 7 `authorization` 与 `authentication`：一个 198 段，一个 142 段，别混

`authorization` 加词边界后 198 段 / 64 页 / 52 个段落名，比 `authentication` 的 142 段更大。这两个词在中文里都可能被译成"授权/认证"，但在 T2 是**两个不重叠的检索族**：198 段里的头部句式是访问授权语义（沙盒授权、后台执行授权一类），142 段里是身份核验语义。本篇取后者为选题主族，并在附表 E 登记"以 authorization 计数代替 authentication"为错误写法。

另一组易混对：`paired` 13 段 / `pairing` 8 段 / `without pairing` 7 段。第三个是第一个的子集形态，但只有 7 段的句式把"未配对"写成**后果条件**，全数落在 AirPlay。手册层对应的是隔空投送与配对语义页，"配对"161 次 / 35 页，是本层最热的相关词之一，但它是名词性流程描述，不写后果。

---

# 8 为什么"联盟 78 段 ⊂ authentic 族"不是因果证据

这条包含关系会被误读成"T2 只在讲认证时才用 bypass"或"认证缺陷是绕过类缺陷的子集"。两者都不成立，原因有三。

**其一，方向是取词方向决定的。** 联盟的 9 种写法里有 7 种本身就以 `authentic` 为构词片段（without authentication / after failing authentication / bypass authentication / authentication policy / authentication flow / authenticate to / unauthenticated），命中它们必然命中 `authentic` 子串。剩下 2 种（`without valid credentials` 5 段、`without pairing` 7 段）字面上不含该片段，但实测这 12 段没有一段是**单独**靠这两种写法进联盟的——同一条 Impact 里另有 `authentication` 字样。所以包含关系的强度是**词形共现**，不是"缺陷同类"。作为对照，本篇考察过但没有采用的第 10 种写法 `without prompting`（全语料 17 段）Impact 侧含 `authentic` 者为 **0 段**，是唯一一种"接进来就会打破包含关系"的写法，这也反证 78 这个数不是靠挑选写法挑出来的。

**其二，`authentication` 是双义词。** 107 段里有 17 段的 `Authentication` 指的是 **Pointer Authentication**（PA，CPU 指令级特性），不是用户身份验证。详见第 12 章。

**其三，段落名不携带机制。** `Screen Sharing` 3 段与 `Screen Sharing Server` 2 段的句式几乎同形（`authenticate to Screen Sharing without valid credentials`），但分处两个段落名；这两个名字是 Apple 的归类动作（客户端 / 服务端），本篇不据此推断任何实现位置或信任边界。

本篇因此把这条包含关系的用途**限制**为一条口径纪律：在 T2 里检索"身份验证被绕过"，可用的最大集合就是这 78 段；超出 78 段的任何"认证缺陷"计数都必须先说明加了哪些写法。

---

# 9 模板复用：21 种句式覆盖 107 段，34 个 CVE 编号

authentic 族 107 段只由 21 种 Impact 句式构成，平均每句 5.1 段；Description 侧 16 种句式里头部一句 `An authentication issue was addressed with improved state management.` 覆盖 **34 段**，即该族 31.8% 的段落用了同一个修复句。第二句 `This issue was addressed through improved state management.`（不带 authentication 一词）16 段，第三句 `The issue was addressed with improved checks.` 13 段。

三条测量学后果：

**其一**，107 段 ≠ 107 个缺陷。同一 CVE 编号可以在多页出现（跨产品批次），同一句式也可以在多页出现，所以段数只能用作**措辞覆盖度**，不能用作缺陷数。本篇全篇不给出"缺陷个数"。

**其二**，34 个 CVE 编号对应 107 段，比例 3.1 段/编号，低于全语料 7,342 含 CVE 段落的平均水平；这说明 authentic 族**更依赖模板复用**，与"头部两句吃 31 段"一致。

**其三**，修复句式不能读成机制。`improved state management` 在本族覆盖 50 段（两句相加），但这句话本身不说明状态在哪里、由谁维护；手册层对应的位置写的是判定条件（第 10 章的清单），两边**不构成互证**。

---

# 10 手册层写出的"预期身份验证要求"：一份封闭清单

这一章是全篇唯一由 T1 单独支撑的部分，因为"T2 从不写预期要求，只写要求没生效"。

## 10.1 术语表没有任何认证条目

《Apple 平台安全保护》术语表页（`sec93292bfa6`）实测：`身份验证` 0 次、`认证` 0 次、`双重认证` 0 次、`单点登录` 0 次、`生物识别` 0 次。该页有 `两步` 1 次，但出现在"具有两步启动架构的 Mac"这句里，讲的是启动流程不是账户。该页确实定义的相关条目是另外四个：

- `钥匙串 一种基础架构和一组 API、Apple 操作系统和第三方 App 用来储存和检索密码、密钥及其他敏感凭证。`
- `密码派生密钥 (PDK) 用户密码与长期 SKP 密钥和安全隔区的 UID 配合使用，由此派生加密密钥。`
- `数据保护 支持的 Apple 设备的文件和钥匙串保护机制。它也可以指 App 用来保护文件和钥匙串项的 API。`
- `预置描述文件 Apple 签名的属性列表（.plist 文件），其中列明允许在 iOS 或 iPadOS 设备上安装和测试 App 的实体和授权。`

结论：**手册层不设"身份验证"这个术语**，它把相关概念拆成钥匙串、PDK、数据保护、描述文件四个可操作对象。选题字面的"预期的身份验证要求"在 T1 侧只能靠组合定义来逼近，这是本篇所有手册层引用都要带页号的原因。该页正文没有可解析的发布日期行，本篇按"日期不可核对"处理，不推断其新鲜度。

## 10.2 密码是根，生物识别是范围内的便利

「视控 ID、面容 ID、触控 ID 和密码」（`sec9479035f1`，发布日期 2024 年 12 月 19 日）逐字：

> 若要使用 视控 ID 、 面容 ID 或 触控 ID ，用户需要为设备设置需要密码才能解锁。当其中一种认证方法检测到匹配成功后，用户的设备便会解锁，而不会要求输入设备密码。…… 视控 ID 、 面容 ID 和 触控 ID 不会取代用户的密码，而是在**精心设计的范围和时间限制内**方便用户访问设备。这一点十分重要，因为强密码是用户的 iPad、iPhone、Mac、 Apple Watch 或 Apple Vision Pro 通过加密方式保护该用户数据的根基。

"精心设计的范围和时间限制内"这九个字是手册层对"预期"二字的正面表述：生物识别的适用性被明确限定在一个范围里，而这个范围由密码要求来界定。「生物识别安全性」（`sec067eb0c9e`，发布日期 2024 年 12 月 19 日）重复了同一命题：`生物识别认证技术兼具强密码的安全性（甚至强化了密码，因为无需手动输入）…… 视控 ID 、 面容 ID 和 触控 ID 不会取代密码，而是在大多数情况下实现更快速的访问。`

## 10.3 八项操作 + 九种状态：手册层的封闭清单

同一页给出两份枚举。**"在以下注重安全性的操作中，始终需要输入密码"（8 项）**：更新软件 / 抹掉设备 / 查看或更改密码设置 / 安装配置描述文件 / 在"系统设置"中解锁"隐私与安全性"面板 / 在"系统偏好设置"中解锁"安全性与隐私"面板 / 解锁"用户与群组"面板（macOS 13，文件保险箱已打开）/ 解锁同一面板（macOS 12，文件保险箱已打开）。

**"如果设备处于以下任一状态，也需要使用密码"（9 种）**：设备刚刚开机或重启 / 用户已退出登录 Mac 账户或尚未登录 / 超过 48 小时未解锁设备 / 过去 156 小时（六天半）内未用密码解锁且过去 4 小时未用生物识别解锁 / 设备已收到远程锁定命令 / 用户退出关机与 SOS 紧急联络 / 五次生物识别认证尝试后未能成功匹配 / 戴口罩使用面容 ID 后的 6.5 小时窗口内 / 以及"每次唤醒时需要成功匹配，或者选择使用密码"。

这份清单是**手册层对"预期要求"的完整表述**，它有四个可核对特征：枚举封闭（逐项列，不用"等等"）；条件全为**阈值或状态**（48 小时、156 小时、4 小时、五次、6.5 小时）；触发者是用户操作或设备状态，不是攻击动作；不含任何 CVE 编号。第 10.4 节把它与 T2 的 78 段联盟做对照。

同一页的阈值句：`为了提供额外保护， 视控 ID 、 面容 ID 和 触控 ID 都只允许五次不成功的匹配尝试，之后便会要求输入密码以获得用户设备或账户的访问权限。` 与错误接受率句：`人群中随机一个人使用 视控 ID 或 面容 ID …… 解锁某位用户的 iPad、iPhone 或 Apple Vision Pro 的成功概率小于一百万分之一。对于用户配备 触控 ID 的 iPad、iPhone、Mac 机型，以及与配备 触控 ID 的妙控键盘配对的机型，此概率小于五万分之一。如果注册多个指纹或 面容 ID 外貌，此概率会随之增长。注册五个指纹或两个面容时概率分别达到一万分之一或五十万分之一。`

这三句是全篇**唯一由 Apple 写出的量化预期**（次数阈值与错误接受率），且手册同时写出该概率会随注册数量上升。本篇不把它换算成"安全性等级"，只登记原句。

## 10.4 与 T2 的 78 段对照

| 手册层的预期（T1） | 措辞层的后果（T2） | 关系 |
|---|---|---|
| 五次失败后要求密码 | `Password autofill may fill in passwords after failing authentication` 9 段 | 同为"失败之后"，但一句定规则、一句报后果，不可互证 |
| 远程锁定命令后要求密码 | 无对应句式 | T1 独有 |
| 48 / 156 / 4 / 6.5 小时阈值 | 无任何时间阈值句式 | T1 独有；T2 全文 `physically proximate` 0 段、`replay` 0 段 |
| 锁屏状态是前提 | `bypass Digital ID authentication when iPhone Mirroring is active` 1 段（含 locked device） | 唯一一条把设备状态写进 Impact 的认证句 |
| 双重认证保护 Apple 账户登录 | `Sign In With Apple authentication flow` 5 段 | T1 写机制承诺，T2 写流程可被 App 用于取账户 |
| 未配对不可发命令（隔空播放） | `without pairing` 7 段（全 AirPlay） | 两层唯一真正对得上的一对 |
| 激活锁绕过代码（管理语义） | 无对应 | 词面同、词义反，见第 3 章 |

T1 的 6 行"独有"必须写成**措辞层不使用该表述**，不能写成"措辞层没有这类缺陷"。

## 10.5 Apple 账户侧的预期句

「Apple 账户安全性概览」（`secbd5822138`，发布日期 2021 年 2 月 18 日）给出双重认证的完整预期定义：

> 它旨在确保仅账户所有者能访问账户，其他人即使知道密码也无法访问。……在任何新设备上首次登录需要两项信息，即 Apple 账户 密码和 6 位数的验证码…… 双重认证意味着已经不能仅靠密码来访问用户账户。

这是全篇唯一一处手册层**明确写出"仅靠一个因子不够"**的句子，也就是选题里"预期的身份验证要求"最接近的正面表述。同页还给出降级恢复路径：`如果用户没有受信任的设备但知道密码，则可以使用受信任的电话号码通过短信验证来进行认证。`

「密码安全性概览」（`sec3d3610acc`，发布日期 2024 年 12 月 19 日）的另一句同样属于预期表述：`管理密码最好的方式就是不使用密码。` —— 手册层在这里给出的"预期"是**减少因子**而不是增加因子，与 T2 的 `Password autofill` 后果句形成方向相反的对照。

---

# 11 企业侧：单点登录 17 次 / 5 页，措辞层 SSO 0 段

「单点登录安全性」（`sec0c87ccc6d`，发布日期 2026 年 1 月 28 日）是全手册唯一集中写认证协议的页，"认证"25 次、"单点登录"12 次、"令牌"10 次。要点句逐字：

> iOS、iPadOS、macOS 和 visionOS 支持通过单点登录 (SSO) 对企业网络进行认证。SSO 与身份提供商 (IdP) 配合，针对用户有权访问的服务对用户进行认证。…… 这使可扩展单点登录支持 Open ID Connect (OIDC)、OAuth、SAML 2.0 和 Kerberos 认证方案。

同一页还写到两个本篇关心的表述：`用户注册过程中会在安全隔区内创建并储存密钥，用于在无需密码的情况下通过 IdP 进行认证。`（"无需密码"——手册层 2 次之一）以及 `用户注册成功后则是用户注册（除非用户账户使用已认证客人模式）。`（"已认证客人"全手册 2 次，仅此页）。

对照措辞层：`single sign-on` **0 段**、`kerberos` **0 段**、`802.1x` **0 段**、`eap` 独立词 **0 段**。但 `Heimdal` 作为段落名有 7 段属于 authentic 族——这是 Kerberos 实现出现在措辞层的唯一方式（以开源组件名而非协议名）。同族里 `Accounts` 5 段、`Apple Account` 5 段，都是账户侧而非 IdP 侧。

结论：企业认证协议族在 T1 有完整词汇表（Kerberos 13 次 / 2 页、OIDC、OAuth、SAML 2.0、WS-Trust、PKINIT、SPNEGO），在 T2 **只剩开源组件名**。这一层错位的口径纪律是：**不能因为 T2 无 `kerberos` 就写"企业鉴权方向无缺陷"**，因为该方向的缺陷在 T2 是以 `Heimdal` / `Accounts` 这类名字出现的；也不能因为 T1 写了协议清单就写"这些协议均受保护"。

---

# 12 `authentication` 的双义：17 段指针认证，与身份验证无关

authentic 族 107 段中，`Pointer Authentication` 句式占 **17 段**：`A malicious attacker with arbitrary read and write capability may be able to bypass Pointer Authentication` 8 段、`An attacker with arbitrary read and write capability may be able to bypass Pointer Authentication` 5 段、同一句再加在野声明的版本 4 段（合计 8 + 5 + 4 = 17，三句相加闭合）。这 17 段只挂在 **3 个**段落名下：`WebKit`(8)、`dyld`(5)、`RPAC`(4)；`curl` 的 7 段虽然也在联盟之外且句式含 `authentic`，实测**不含** `pointer` 字样，不属于本组，本篇不把它并入 PA。

`Authentication credentials may be sent to a server on another origin` 7 段是第三种语义（凭证**发送**而非核验）。加上这 7 段，107 段里真正与"用户身份核验"直接相关的只有 83 段（107 − 17 − 7 = 83）。

**跨篇纪律**：报告 #95《iOS 限制特定指针认证…》与本篇是**同一个英文词的两种对象**，两篇都不得互引数字。本篇不使用那 17 段支撑任何身份验证结论；在野声明的对照（第 13 章）也据此把 PA 的 4 段单独列出。

---

# 13 在野声明求交

全语料 `Apple is aware of a report` 形态的段落 **93 段 / 12 个段落名 / 15 个 CVE 编号**。与各族的交集：

| 集合 | 与在野 93 段的交集 |
|---|---|
| authentic 族（107 段） | **4 段**，全部 RPAC、全部 `bypass Pointer Authentication`、同一 CVE-2025-31201、4 个不同页面 |
| 句式联盟（78 段） | **0 段**（4 段的 Impact 只写 read and write capability，不写"身份验证要求未满足"） |
| bypass 族（288 段） | **20 段 / 2 条句式** |
| B1 身份验证本身（7 段） | **0 段** |

那 20 段的构成经复核：**Kernel 8 段 + RTKit 8 段 + RPAC 4 段 = 20**，对应句式只有两条——`An attacker with arbitrary kernel read and write capability may be able to bypass kernel memory protections. Apple is aware of a report that this issue may have been exploited.` **16 段**（这 16 段同时分布在 Kernel 与 RTKit 两个段落名下，所以"句式 16 段"与"Kernel 名 8 段"不矛盾），以及 PA 那句 4 段。

在野 93 段按段落名的全貌是：WebKit 27、Messages 9、CoreMedia 8、Kernel 8、RTKit 8、ImageIO 7、CoreAudio 5、JavaScriptCore 5、dyld 5、Accessibility 4、RPAC 4、CoreGraphics 3。

**这三条 0 与 4 的正确写法与错误写法**：

- 可写：本期公开措辞里，带在野声明的段落没有一条把"身份验证要求未满足"写进 Impact；与 `authentic` 相交的只有 4 段，且是 PA。
- 不可写：身份验证方向没有在野利用；认证缺陷不会被在野利用；锁屏 / 配对 / 单点登录方向风险低。理由是同一批 93 段的段落名分布里含 `Accessibility`(4 段，旁路辅助功能一类的账户访问控制)，本篇第 5 章的联盟里 `Accessibility` 名下也有 1 段——"身份验证语义"与"在野声明"在本期语料里只是没有**逐字共现**，共现缺失不等于事实缺失。

---

# 14 修复句式：authentic 族 16 种，头部一句吃 34 段

authentic 族的 16 种 Description 句式头部：`An authentication issue was addressed with improved state management.` 34 段；`This issue was addressed through improved state management.` 16 段；`The issue was addressed with improved checks.` 13 段；`An access issue was addressed with improved access restrictions.` 7 段；开源第三方 CVE 说明句 7 段；`This issue was addressed with additional entitlement checks.` 6 段；`A logic issue was addressed with improved file handling.` 5 段；`A race condition was addressed with additional validation.` 5 段；`This issue was addressed by removing the vulnerable code.` 4 段；`A configuration issue was addressed with additional restrictions.` 3 段；`A logging issue was addressed with improved data redaction.` 2 段；`A logic issue was addressed with improved checks.` 1 段。

对照 bypass 族的 42 种句式头部：`The issue was addressed with improved checks.` 21、`A file quarantine bypass was addressed with additional checks.` 17、`A logic issue was addressed with improved checks.` 17、`A memory corruption issue was addressed with improved validation.` 16、`This issue was addressed by adding an additional prompt for user consent.` 14、`This issue was addressed through improved state management.` 14、`A permissions issue was addressed with additional restrictions.` 13、`A logic issue was addressed with improved validation.` 12、`A race condition was addressed with additional validation.` 11、`This issue was addressed by removing the vulnerable code.` 10、`An authentication issue was addressed with improved state management.` 9、`A permissions issue was addressed with additional sandbox restrictions.` 8。

两条观察。**其一**，`An authentication issue …` 这句在 authentic 族覆盖 34 段、在 bypass 族只覆盖 9 段，而 `additional prompt for user consent`（追加一次用户确认）14 段**只在 bypass 族**出现——手册层"始终需要输入密码"清单在措辞层能找到的唯一近邻就是这个 14 段的"追加确认提示"句式，两者都不是机制说明。**其二**，`removing the vulnerable code` 4 + 10 段在两个族里都出现，属于通用句式，不参与本选题的分析。

---

# 15 批次新鲜度：联盟、authentic 族与 bypass 族在最新十页

最新 10 页（149034–149043）全部 `Released September 14, 2026`，切块共 1,430 段。各族在该批次的实际分布：

- authentic 族 **10 段**：`Apple Account` 5、`Screen Sharing Server` 2、`Telephony` 1、`Wi-Fi` 1、`Wallet` 1。
- bypass 族 **39 段**：`copyfile` 7、`CoreServices` 4、`Sandbox` 4、`Time Zone` 3、`Multi-Touch` 3、`Kernel` 3、`autofs` 3、`Accounts` 2、`System Settings` 2、`WebDAV` 2、`libxpc` 2、`Apple Intelligence` 1、`Foundation` 1、`Telephony` 1、`Wallet` 1。

三点读法。**其一**，最新批次里 authentic 族的 10 段中，5 段是 `Apple Account`，2 段是 `Screen Sharing Server`——后者正是第 5.5 章 `authenticate to Screen Sharing without valid credentials` 那一句在新批次的延续，说明该句式**跨三年批次仍在复用**。**其二**，`Wi-Fi` 名下 1 段是 `view Wi-Fi passwords without authentication`（物理访问 + 已解锁设备），联盟内各 1 段的那类"限定条件最全"句式在新批次仍有新增。**其三**，本批次 bypass 第一名是 `copyfile` 7 段，属于 B4 完整性保护，不属于 B1；"绕过"负载重心未变。

同一批 10 页里 `Telephony` 与 `Wallet` 同时出现在两个族，这两名是本篇看到的**唯一跨族同名共现**，说明按段落名取族与按词取族不能混用（第 16.2 节）。

---

# 16 自我纠错登记

## 16.1 切块刀锚点缺失的复查

本轮按 #106–#108 的口径重跑：块终止需四个锚点（下一个 `gb-header`、`<div id="disclaimer"`、`<footer`、串尾），缺一即会把页面免责声明吸入字段。122085 与 149040 两页缺 `disclaimer` 锚点：前者切出 0 段（已从分母排除），后者以 `<footer` 正确终止、9 段字段干净。**验收断言：免责声明文本出现在字段中的块数 = 0**，本轮通过。

## 16.2 由"按词取族"改写成"按句式联盟取族"

初稿曾用 `authorization` 的 198 段作主族，并在结论里写"授权类缺陷规模大于认证类"。复查发现 `authorization` 与 `authentication` 是两个不重叠检索族，且 198 段的头部句式属访问授权语义（第 7 章），据此把主族改为 `authentication`，并新增句式联盟（78 段）作为"要求未满足"的口径。原写法撤销，附表 E 登记。

## 16.3 短词伪命中

初稿把 `eap` 75 段读作"EAP 协议相关"，把 `pin` 40 段读作"PIN 码相关"，把 `credential` 29 段当作独立词形。词边界重测后三者**均为 0**（第 1.2 节表）。本篇对全部 30 余个检索词同时给出子串与词边界两列，正文一律引用词边界值。

## 16.4 手册层窗口分母

初稿把 179 页全部计入分母并在结论里写"全手册"。实测 1 页（版权和商标页）无正文锚点，已从所有手册层比例的分母剔除；手册层任何"占全手册 X%"的表述在本篇都改写成绝对次数 + 页数，避免分母歧义。

## 16.5 一处被实测推翻的自家推断

初稿第 5 章写的是"联盟 78 段 100% 落在 `authentic` 族与 `bypass` 族的交集之内"，并据此推论"T2 只在讲认证时才用 bypass"。复查词边界后该说法**不成立**：78 段里 Impact 含 `bypass` 的只有 **7 段**，且这 7 段是同一条 `authentication policy` 句式；其余 71 段靠 `without` / `unauthenticated` / `after failing` 表达。真正成立的包含关系只有一层——78 ⊂ 107（`authentic` 子串），而这是取词方向造成的，不是缺陷分类造成的，第 8 章据此把它降级为口径纪律。

同一轮复查还推翻了两个"看起来闭合"的数字：`authentic ∩ bypass = 29` 与 `107 − 78 = 29` 得数相同，但两者只有 22 段重合，**不是同一组段落**，初稿把它们当成同一组写；以及 `without prompting` 的 17 段曾被当作联盟成员列出，实测与联盟重合 0 段，已从联盟里移出并登记为排除项。三处均在第 5、8 章改正，本篇按"数字先测后写"的顺序重排了引用。

---

# 17 不可外推清单

| 不能写 | 原因 |
|---|---|
| "认证缺陷只有 107 个" | 107 是段数；同一句式 16 段可跨 16 页复用，同一 CVE 可跨页出现 |
| "bypass 主要是绕过身份验证" | B1 只有 7 段 / 288；重心在 B6(100) 与 B4(58) |
| "身份验证方向没有在野利用" | 4 段相交是 PA，0 段共现 ≠ 0 事实（第 13 章） |
| "`authentic` 0 段说明 Apple 不谈认证可信性" | 0 是词边界结果，名词形态 142 段仍在 |
| "手册层没写绕过身份验证，所以没有该要求" | 手册层用"始终需要输入密码"清单表达预期，不用"绕过" |
| "激活锁可以绕过" | 手册层的"绕过代码"是设备管理服务的托管凭据，管理语义 |
| "SSO / Kerberos 方向无缺陷" | T2 以 `Heimdal` / `Accounts` 等组件名承载，不以协议名承载 |
| "T2 一句后果对应 T1 一条机制" | 第 10.4 节的 7 行对照里只有 1 行真正对得上 |
| "Apple Account 5 段说明账户服务有缺陷" | 段落名是归类动作；`Screen Sharing` 与 `Screen Sharing Server` 同句式分处两名即为反证 |
| "指针认证的 17 段属于本选题" | 同一个英文词的另一种对象，与 #95 严格分账 |
| "手册层的错误接受率可换算安全等级" | 1/1,000,000 与 1/50,000 是随机单人尝试口径，且随注册数上升 |
| "T1 的 8 项 + 9 项清单是穷尽实现清单" | 它是手册层枚举，本轮无源码核对 |

---

# 18 段落名不是实现层：四条反证

**反证一（同句式分两名）**：`An attacker on the network may be able to authenticate to Screen Sharing without valid credentials` 在 `Screen Sharing` 名下 3 段、`Screen Sharing Server` 名下 2 段。两句逐字相同，名字不同；从这两个名字能读出的只是 Apple 按客户端 / 服务端分栏，读不出校验发生在哪一端。

**反证二（协议名缺失）**：`Heimdal` 名下 7 段属于 authentic 族，但同族的 Kerberos / OIDC / OAuth / SAML 协议名在 470 个段落名里**一个都没有**。以组件名取族与以协议名取族会得到两个完全不同的集合。

**反证三（一名跨语义）**：`Accounts` 名下 5 段与 `Apple Account` 名下 5 段同族但句式不同（前者多为 `bypass Privacy preferences` 类，后者多为 `Sign In With Apple authentication flow` 类），说明同一名下混合了隐私授权语义与账户核验语义，"按名取族"会把两种语义并进一个计数。

**反证四（PA 混名）**：`RPAC` 名下 4 段全是 PA，`dyld` 名下 5 段也有 `bypass Pointer Authentication`——两个名字之间没有语义关系。第 12 章的双义分离只能靠句式，不能靠名字。

---

# 19 缺口清单（G1–G15）

| 编号 | 缺什么 | 为什么缺 |
|---|---|---|
| G1 | XNU / 用户态开源源码 | 本轮源码镜像为空，无法核对认证判定位置 |
| G2 | 5 次失败阈值的实现常量 | 依赖 G1 |
| G3 | 48 / 156 / 4 / 6.5 小时阈值的实现位置 | 依赖 G1 |
| G4 | `without authentication` 类后果对应的具体子系统边界 | 顾问页不写，T1 不写缺陷 |
| G5 | AirPlay authentication policy 的未公开语义 | 手册层未描述策略本体 |
| G6 | "绕过代码"的生成时机与长度之外的细节 | T1 只写 31 字节、随机且唯一 |
| G7 | 术语表页发布日期 | 该页无可解析日期行 |
| G8 | `authenticity` 是否存在于语料 | 本篇未穷尽检索，第 2.2 节留待复算 |
| G9 | 中文层是否存在"已认证客人模式"的机制描述 | 仅 2 次，均为条件句 |
| G10 | 手册层是否写"生物识别不可用于支付授权之外的高风险操作" | 需补读 Apple Pay 与钱包诸页，本轮未做 |
| G11 | 93 段在野声明的 CVE 与批次对应关系 | 与 #99 / #102 口径未合并核对 |
| G12 | 联盟 78 段与 CVE 的去重数 | 本篇只给族级 34，未给联盟级 |
| G13 | `paired` 13 段与 `pairing` 8 段的语义分工 | 未逐段读 |
| G14 | 其它非中文手册页的对应词形 | 本轮只镜像 zh-CN 一份 |
| G15 | 静态站点是否需重编 | 本篇未触发 `ios-web` 重建 |

---

# 20 核验项清单

1. 全语料分母：257 份镜像、9,045 段、470 个段落名、7,346 带 Impact、1,699 纯致谢、7,342 含 CVE。
2. authentic 族：107 段 / 69 页 / 25 名 / 34 CVE / 21 Impact 句式 / 16 Description 句式 / 4 在野。
3. 21 句式段数之和 = 107（无残差）；16 Description 句式头部 11 种之和 = 102，其余 5 种各 1 段。
4. bypass 族：288 段 / 111 页 / 55 名 / 88 CVE / 42 Impact 句式 / 42 Description 句式 / 20 在野。
5. 宾语分区之和 = 288；B1 = 7 且全部 AirPlay；B5 = 0、B9 = 0、B11 = 0。
6. 句式联盟 = 78 段 / 46 页 / 20 名 / 14 句式；且 78 段 Impact 侧 100% 含 `authentic`。
7. authentic ∩ bypass（词边界）= 29 段 / 7 句式；联盟 ∩ bypass = 7 段（同一条 authentication policy 句式）；107 − 78 = 29 与前者**得数相同但不是同一组段落**，两者重合 22 段。
8. `authentication` 142 段 / 82 页 / 36 名；词形穷尽后仅 1 种写法、171 次出现。
9. `authentic` 子串 151 段，词边界 **0 段**；`eap` 75→0、`pin` 40→0、`credential` 29→0、`verify` 14→0、`biometric` 1→0、`profile` 3→0。
10. 在野 93 段 / 12 名 / 15 CVE；∩ authentic = 4（全 PA）；∩ 联盟 = 0；∩ B1 = 0；∩ bypass = 20（Kernel 8 + RTKit 8 + RPAC 4）。
11. 手册层 178 页可用、窗口 9–176 行；"身份验证"5 次 / 3 页、"认证"372 / 92、"验证"252 / 74、"绕过"23 / 9（单页最高 12）。
12. 最新 10 页（Released September 14, 2026）共 1,430 段：authentic 10 段、bypass 39 段。
13. 字段中出现页面免责声明文本的块数 = 0。
14. 手册层清单：8 项"始终需要输入密码"操作 + 9 种"也需要使用密码"状态，逐项可回溯至同一页。
15. 术语表页对 身份验证 / 认证 / 双重认证 / 单点登录 / 生物识别 命中数均为 0。

---

# 附表 A authentic 族 25 个段落名逐名清单（25 行）

| 段落名 | 族内段数 | 入联盟 | 未入联盟 |
|---|---|---|---|
| AirPlay | 14 | 14 | 0 |
| Safari Private Browsing | 11 | 11 | 0 |
| WebKit | 11 | 3 | 8 |
| Photos | 8 | 8 | 0 |
| curl | 7 | 0 | 7 |
| iCloud Document Sharing | 6 | 6 | 0 |
| Accounts | 5 | 5 | 0 |
| Apple Account | 5 | 5 | 0 |
| dyld | 5 | 0 | 5 |
| Authentication Services | 4 | 4 | 0 |
| Passkeys | 4 | 4 | 0 |
| RPAC | 4 | 0 | 4 |
| Passwords | 3 | 1 | 2 |
| Photos Storage | 3 | 3 | 0 |
| Screen Sharing | 3 | 3 | 0 |
| Shortcuts | 3 | 3 | 0 |
| Screen Sharing Server | 2 | 2 | 0 |
| Telephony | 2 | 0 | 2 |
| Accessibility | 1 | 1 | 0 |
| Compressor | 1 | 1 | 0 |
| Persona | 1 | 1 | 0 |
| Safari | 1 | 1 | 0 |
| Siri | 1 | 1 | 0 |
| Wallet | 1 | 0 | 1 |
| Wi-Fi | 1 | 1 | 0 |

三列横向可加：族内 107 = 入联盟 78 + 未入联盟 29，本表 25 行逐名相加与全语料实测的 107 / 78 / 29 完全一致，无残差。入联盟的段落名 20 个、未入联盟 7 个，两者有重合——`WebKit`（3 入 / 8 出）、`Passwords`（1 入 / 2 出）同时出现在两侧，所以 20 + 7 = 27 大于本表的 25 个名字，差额 2 即这两个名字。未入联盟的 29 段由 7 个名字承载：`WebKit` 8、`curl` 7、`dyld` 5、`RPAC` 4、`Telephony` 2、`Passwords` 2、`Wallet` 1。其中 `WebKit` 8 + `dyld` 5 + `RPAC` 4 = 17 段为指针认证句式（第 12 章），`curl` 7 段是同一句 `Authentication credentials may be sent to a server on another origin`（凭证外发，不是指针认证也不是身份核验被绕过），`Telephony` 2 与 `Wallet` 1、`Passwords` 2 属另因；本篇不把这 29 段外项归并为同一机制。

---

# 附表 B 手册层"绕过"9 页逐页表（9 行）

| 页面（页号） | 发布日期 | 次数 | 宾语 |
|---|---|---|---|
| 激活锁安全性 `sec0f8dfd030` | 2024-12-19 | 12 | 绕过代码（管理服务托管） |
| 在 macOS 中防范恶意软件 `sec469d47bd8` | 2024-12-19 | 2 | 门禁绕过事件 |
| 安全隔区 `sec59b0b31ff` | 2024-12-19 | 2 | 操作系统密钥绑定 / 安全性控制 |
| 操作系统完整性 `sec8b776536b` | 2026-01-28 | 2 | 控制流程保护 / SPTM |
| 启动磁盘安全性策略控制 `sec7d92dc49f` | 2021-02-18 | 1 | 最新软件提供的保护 |
| 术语表 `sec93292bfa6` | 未标注 | 1 | DMA 绕过 CPU |
| 关于 App Store 安全性 `secb8f887a15` | 2024-12-19 | 1 | 设备端保护 |
| 在 macOS 中控制 App 对文件的访问 `secddd1d86a6` | 2021-02-18 | 1 | 其他保护措施 |
| Apple Configurator 安全性 `sece6ced019e` | 2022-05-13 | 1 | 现有激活锁要求（否定式） |

合计 23 次 / 9 页，与第 3 章一致。日期一律取页面自带的**发布日期**字段；该手册每个被引页都有两个日期字段（内容日期与页面发布日期），本篇引用时只取发布日期。

---

# 附表 C 措辞层句式联盟 14 条 Impact 原句与段数（14 行）

| 段数 | 宾语 / 条件 | 段落名 |
|---|---|---|
| 16 | 隐藏相册照片可被查看（无认证） | Photos |
| 15 | 私密浏览标签页可被访问（无认证） | Safari Private Browsing |
| 9 | 认证失败后仍自动填充密码 | Passwords / Passkeys 等 |
| 7 | 绕过身份验证策略（本地网络） | AirPlay |
| 7 | 未配对即发送 AirPlay 命令（未认证用户） | AirPlay |
| 6 | 未经认证开启 iCloud 文件夹共享 | iCloud Document Sharing |
| 5 | 借助"通过 Apple 登录"流程访问 Apple 账户 | Apple Account |
| 5 | 无有效凭证即可向屏幕共享认证 | Screen Sharing / Screen Sharing Server |
| 3 | 快捷指令未经认证以管理员权限运行 | Shortcuts |
| 1 | 辅助访问下最近照片可见 | Accessibility |
| 1 | 已解锁设备 + 物理访问查看无线局域网密码 | Wi-Fi |
| 1 | 未受保护的 Persona 可被未认证用户使用 | Persona |
| 1 | Compressor 服务器：未认证用户可执行任意代码 | Compressor |
| 1 | 摇动撤销使已删照片重现 | Photos |

段数合计 78。表内 3 段及以下的行共 6 条（1 条 3 段 + 5 条 1 段），是本篇唯一"限定条件写全"的句子群（第 5 章末）。

---

# 附表 D 复算验收断言集（12 行）

| 编号 | 断言 |
|---|---|
| D1 | 镜像文件数 = 257；切块总数 = 9,045；段落名数 = 470 |
| D2 | 带 Impact 段 = 7,346；纯致谢段 = 1,699；含 CVE 段 = 7,342；三者之和关系自洽 |
| D3 | 免责声明文本出现在字段中的块数 = 0 |
| D4 | authentic 族五元组 = (107, 69, 25, 34, 21)；其 Impact 句式段数和 = 107 |
| D5 | bypass 族六元组 = (288, 111, 55, 88, 42, 42)；宾语分区和 = 288 |
| D6 | authentic ∩ bypass（词边界）= 29 段 / 7 句式；107 − 78 = 29，但两组**只有 22 段重合**，不得写成同一组 |
| D7 | 联盟 = 78 段 / 46 页 / 20 名 / 14 句式；78 段 Impact 侧含 `authentic` 者 = 78；联盟 ∩ bypass（词边界）= 7 段 |
| D8 | `authentication` = 142 段 / 82 页 / 36 名；词形去重后种类 = 1；出现次数 = 171 |
| D9 | `authentic` 词边界段数 = 0；`eap` / `pin` / `credential` / `verify` / `biometric` / `profile` 词边界均 = 0 |
| D10 | 在野 93 段 / 12 名 / 15 CVE；∩ authentic = 4（全 PA）；∩ 联盟 = 0；∩ bypass = 20（8+8+4） |
| D11 | 手册层可用页 = 178；窗口行数 min = 9 / max = 176；"绕过"= 23 次 / 9 页且单页 max = 12 |
| D12 | 最新 10 页切块 = 1,430 段；authentic = 10；bypass = 39 |

---

# 附表 E 错误写法与纠正对照（15 行）

| 错误写法 | 判定 | 纠正依据 |
|---|---|---|
| "认证缺陷共 107 个" | ❌ 不可写 | 107 是段数，头部两句吃 31 段（第 4、9 章） |
| "bypass 主要是绕过身份验证" | ❌ 不可写 | 6.1：B1 = 7 / 288，全部 AirPlay |
| "身份验证方向没有在野利用" | ❌ 不可写 | 第 13 章：4 段相交是 PA，共现缺失 ≠ 事实缺失 |
| "quarantine 从未被绕过" | ❌ 不可写 | 6.2：B5 = 0 是分区口径产物；Description 侧同一句式 27 段 |
| "T2 不谈 SSO / Kerberos 所以该方向无缺陷" | ❌ 不可写 | 第 11 章：该方向由 `Heimdal`(7 段) / `Accounts` 承载 |
| "以 authorization 198 段作主族" | ❌ 已撤销 | 16.2 与第 7 章：两族不重叠 |
| "eap 75 段是 EAP 协议" | ❌ 已撤销 | 1.2：词边界 0，全部 heap / HeapSmasher |
| "credential 29 段是独立词" | ❌ 已撤销 | 16.3：全部 credentials 子串 |
| "手册层没写绕过身份验证，所以不存在该要求" | ❌ 不可写 | 10.3：手册用"始终需要输入密码"清单表达预期 |
| "激活锁存在绕过漏洞" | ❌ 不可写 | 第 3 章：绕过代码是管理服务的设计能力 |
| "指针认证 17 段并入本选题族" | ⚠️ 须分账 | 第 12 章：与 #95 严格分账 |
| "`authentic` 0 段说明不谈可信性" | ❌ 不可写 | 第 2.2 节：0 是词边界结果 |
| "Screen Sharing 与 Server 两句对应不同实现" | ❌ 不可写 | 第 18 章反证一：同句分两名 |
| "错误接受率 1/1,000,000 可换算安全等级" | ⚠️ 只能带限定写 | 10.3：随注册数上升至 1/10,000 与 1/500,000 |
| "8 项 + 9 项清单是实现清单" | ❌ 不可写 | 第 17 章：手册枚举，无源码核对 |

---

**边界声明：** 本报告全部技术结论来自两层可核对的公开文本：《Apple 平台安全保护》官方指南（中文 179 页全量转储，正文域切分后 178 页可用，逐词普查）与 Apple 安全响应顾问页（257 份英文页面镜像，容错解析后全语料 9,045 段 / 470 个段落名；本篇认证词族 107 段 / 25 个段落名 / 34 个 CVE 编号 / 69 页，绕过词族 288 段 / 55 个段落名 / 88 个 CVE 编号 / 111 页，"要求未满足"句式联盟 78 段 / 20 个段落名 / 46 页），逐条标注页号、字段原文与页面自带的 `Released` 字段（中文页标注的是发布日期，本手册每个被引页面同时存在内容日期与页面发布日期两个字段，本报告一律注明所取字段）；手册层未说明的部分在第 19 章登记为缺口，不以推断填充。源码层本轮不可用，凡涉及认证判定位置、阈值实现常量与失败计数落点的表述均已降级为"顾问页将其归入某段落名"或"手册层如此陈述"。第 1、2、3、4、5、6、9、13、14、15 章的统计为本报告对公开文本的自建语料普查，属本报告的测量结果，不是 Apple 的表述；这些数字依赖本报告解析器对顾问页 HTML 标记的识别方式与词边界规则——本轮已因切块刀锚点缺失重跑全部字段口径计数，并用"含页面免责声明的块数 = 0"作为验收（第 16.1、附表 D 第 3 项），后续复算须先跑附表 D 全 12 项断言。`authentication` 在本报告语料中同时指用户身份核验与指针认证两类对象，凡涉及该词族处均已声明取的是 Impact 侧还是字段并集侧。所引 CVE 仅用于说明公开修复事实、检测与配置核查，不含绕过方法、不含利用代码、不含身份验证绕过的操作路径或时序、不含锁屏与配对场景的可执行细节、不含凭证构造与会话重放，也不含针对他人设备的任何操作步骤。所有第三方研究者姓名仅作顾问页致谢字段的引用，其研究结论本轮未核对，不构成本报告依据。如需就本报告任一结论索取取数脚本或段落级原始文本，或指出任何事实与措辞问题，请联系。

Telegram: https://t.me/ios_one001
