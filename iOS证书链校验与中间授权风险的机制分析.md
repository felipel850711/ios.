@@TITLE@@iOS 证书链校验与中间授权风险的机制分析
@@SUBTITLE@@一层有词、一层有段，两层从不互相指认：《Apple 平台安全保护》中文全量 179 页正文域里"证书"150 次 / 29 页、"信任"182 次 / 53 页，并且真的写出了 RFC 5280、RFC 6962（证书透明度）、CRL 聚合、OCSP、"无效的证书始终导致硬故障和无连接"这些机制级原句；而 Apple 安全响应顾问页本期 257 份镜像（容错解析后 9,045 段 / 470 个段落名）里，全语料只有 **11 段**提到 certificate，全部挂在 **同一个段落名** `Security` 下，全部来自 **同两句** 字段文本、**两个** CVE 编号，而且这 11 段**在严格解析器下是 0 段**。更关键的方向差：措辞层表达"中间授权"的真实词不是 certificate 而是 entitlement（131 段 / 38 个段落名）与 authorization（198 段 / 52 个名）；`trust` 一词全语料 9 段，**一段都不在** `Security` 名下；而"证书链校验"这件事在 470 个段落名里找不到任何一个名字（Certificates / Secure Transport / TLS / SSL / Trust / Provisioning 四个候选名全部 0 段）
@@INFO@@报告日期|2026 年 10 月 26 日
@@INFO@@分析对象|《Apple 平台安全保护》zh-CN 指南 179 页全量转储（正文域切分后逐词普查）+ 英文侧 20 页取样正文；Apple 安全响应顾问页 257 份英文页面镜像（容错解析后全语料 9,045 段 / 470 个不同段落名），本篇证书与授权家族 304 段 / 19 个段落名 / 65 个 CVE 编号 / 104 页，其中 certificate 词族 11 段 / 8 页 / 1 名，trust 词族 9 段 / 9 页 / 3 名，`Security` 名下 114 段（91 段带字段，归并为 18 种 Impact 句与 19 种 Description 句）
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含针对他人设备的操作步骤、不含证书链构造细节、不含中间证书签发或伪造服务器的实施方法
@@INFO@@分类|信任判定面 / 官方手册层的机制词汇与顾问页的后果词汇的**互不点名**对照 / 一次"按名字取族"与"按词取族"的差集实测 / 解析器标记换代对计数的直接影响
@@INFO@@难度|进阶（第 1 章的两层词表对照与第 2.3 节的"严格解析器 0 段"是全篇前提；跳过它们会把顾问页的段落名读成实现层，把手册层的 RFC 编号读成 Apple 的实现承诺）
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

本篇处理一个具体的口径冲突：**"证书链校验"在 Apple 的公开文本里存在于两个互不相通的层**。

手册层（《Apple 平台安全保护》，下称 T1）有完整的机制词汇：中文正文域"证书"150 次分布在 29 页，"信任"182 次 / 53 页，"证书颁发机构"11 次 / 8 页，"根证书"9 次 / 7 页，"证书链"8 次 / 2 页；而且它给出了可核对的机制级句子——TLS 证书信任状态的评估依照 RFC 5280，并纳入 RFC 6962（证书透明度）；撤销与受限证书清单由 Apple 信任的**每个内建根证书颁发机构及其从属 CA 签发者**发布的 CRL 聚合而成；某个 CA 撤销证书多到无法逐个列出时，信任评估改为要求 OCSP 响应，取不到响应就**无法通过信任评估**；"无效的证书始终导致硬故障和无连接"。

措辞层（Apple 安全响应顾问页，下称 T2）只有 **一句**关于证书校验的修复句式：`A certificate validation issue was addressed with improved certificate validation.` 这句话在本期 257 份镜像里出现 **11 次**，落在 **8 个页面**上，段落名只有一个 `Security`，背后是 **2 个** CVE 编号（CVE-2026-86881、CVE-2026-86889）。T2 全语料没有 X.509、没有 CRL、没有 OCSP、没有 revocation、没有 pinning、没有 man-in-the-middle、没有 eavesdrop，也**没有任何一个叫 Certificates、Secure Transport、TLS、SSL 或 Trust 的段落名**。

于是本篇的四个问题依次是：

**问题一（第 1–2 章）** 两层的证书词表各自长什么样，差额有多大，这个差额是不是采集缺陷造成的假差额？答案的戏剧性在于：T2 那 11 段所在的 8 页恰好是**严格解析器完全看不见的 8 页**——用初稿那把严格切块刀，"全语料 certificate 段落"测出来是 **0 段**，而 0 是错的。

**问题二（第 3–5 章）** 那 11 段究竟登记了什么缺陷？"中间 CA 被攻破"在 Apple 的公开措辞里是被写成一句后果（`issue certificates with arbitrary extended key usages`）、还是写成一条机制？答案是前者，且这句话只出现在 `Security` 段落名下，全语料 8 段 8 页。

**问题三（第 6–9 章）** 把"证书链校验"换成 T2 自己用来表达"授权"的词——`entitlement`、`authorization`、`authentication`、`signed`——画面会变成什么？这一组是 304 段，比证书词族大 27 倍；但 `trust` 这个词在 T2 只有 9 段，并且**全部不在** `Security` 名下。本篇把这条称为"信任词与证书词在措辞层分居两处"。

**问题四（第 10–14 章）** 本篇能否支持任何一句"证书校验方向有 N 个漏洞、因此风险高/低"？不能。第 13 章给出一个方向明确的负结果：在野声明句式 `Apple is aware of a report` 全语料 93 段 / 65 页 / 12 个段落名，与本家族 304 段的交集是 **0**——这个 0 附了覆盖性证明，但它只能说明"该方向本期无在野登记"，不能推出"该方向不易被利用"。

**写作边界（贯穿全篇）**：T2 的段落名是 Apple 的**归类动作**，不是实现层证据。`Security` 名下有 8 段"modify the state of the Keychain"，本篇只写"顾问页把这 8 段归在 `Security` 名下"，不写"钥匙串状态由 Security 框架的某函数修改"。源码层（XNU 与用户态开源部件）本期镜像为空，凡涉及实现的表述一律降级为"未核对"，并在第 19 章登记为缺口。

# 1 反常的起点：手册层有一整套证书词汇，措辞层只有一句

## 1.1 T1 中文正文域词频表

普查对象是《Apple 平台安全保护》zh-CN 指南的 **179 页全量转储**。每份转储除了正文，还带一份约 235 行的全站侧栏目录；目录里反复出现"证书""信任"等页面标题，**不切正文域就会把目录条目当成正文用词**。本篇的切法：起点 = 最后一个逐字等于 `版权和商标` 的行之后，终点 = 首个含 `Apple Footer` 的行之前。179 页**全部**切出成功（`NO_MARKER=0`），正文长度中位在 20–30 行。

下表全部为**正文域**计数（`hits` 为出现次数，`pages` 为命中页数，分母 179）：

| 词 | hits | pages | 说明 |
|---|---:|---:|---|
| 证书 | 150 | 29 | 本篇主轴词 |
| 信任 | 182 | 53 | 分布最广，多数与证书无关 |
| 验证 | 252 | 74 | T1 的"校验"主词 |
| 校验 | 19 | 3 | 只在 3 页，T1 主要用"验证" |
| 授权 | 172 | 63 | 与 T2 的 authorization / entitlement 对应 |
| 认证 | 372 | 92 | 含"身份认证""认证服务器"等多种用法 |
| 签名 | 386 | 70 | — |
| 密钥 | 915 | 95 | — |
| 公钥 | 77 | 26 | — |
| 描述文件 | 46 | 17 | 峰值集中在"配置执行"页（15 次） |
| 撤销 | 24 | 12 | 见 1.2 的多义问题 |
| 吊销 | **0** | **0** | Apple 中文不用这个词 |
| 中间人 | 2 | 1 | **只在"蓝牙安全性"一页** |
| 证书链 | 8 | 2 | — |
| 根证书 | 9 | 7 | — |
| 证书颁发机构 | 11 | 8 | 峰值在"针对 Apple 设备的管理式设备证明"（3 次） |
| 根证书颁发机构 | 6 | 5 | — |
| 证书透明度 | 2 | 1 | 只在"TLS 安全性" |
| 已撤销 | 1 | 1 | 只在"TLS 安全性" |
| 内建根 | 1 | 1 | 只在"TLS 安全性" |
| 透明度 | 2 | 1 | 只在"TLS 安全性" |
| 有效性 | 1 | 1 | 只在"TLS 安全性"（"证书有效性验证"小节名） |
| 硬故障 | 1 | 1 | 只在"TLS 安全性" |
| 信任评估 | 7 | 3 | 启动过程（3）＋管理式设备证明（2）＋TLS 安全性（2） |
| 硬件信任根 | 6 | 4 | — |
| 单点登录 | 17 | 5 | — |
| 通行密钥 | 2 | 2 | — |
| TLS | 45 | 19 | — |
| SSL | 4 | 3 | — |
| 信任存储 | **0** | **0** | — |
| 扩展密钥 | **0** | **0** | 与第 3.3 节的 EKU 缺口直接相关 |
| 密钥用法 | **0** | **0** | 同上 |
| 撤销列表 | **0** | **0** | T1 写"证书撤销清单 (CRL)"，不写"撤销列表" |
| 自签 | **0** | **0** | — |
| 服务器证书 | **0** | **0** | — |
| 数字证书 | **0** | **0** | — |
| 校验链 | **0** | **0** | — |

三个数最值得记住：**"证书"只覆盖 179 页里的 29 页**（150 页正文域不含"证书"二字）；**"信任评估"只有 3 页**；**"扩展密钥""密钥用法"两词为 0**，而 T2 恰好用 `arbitrary extended key usages` 描述本族最要紧的一条后果（第 3 章）。

## 1.2 一个必须登记的中文多义词：撤销

T1 用同一个词"撤销"表达两件不同的事：证书被颁发机构作废（revocation），以及旧版本二进制被阻止在新系统上运行（代码签名的定向撤销）。全量正文域 `撤销` 24 次 / 12 页，其中真正与证书相关的只有"TLS 安全性"3 次与"信任缓存"3 次；其余分布在"在 macOS 中防范恶意软件"（5 次）、"Apple 钱包中的证件"（4 次）、"使用 HomeKit 保护路由器"（2 次）以及健康数据访问、Apple Pay 停用、钥匙类型、安全隔区、外围处理器等各 1 次的页面——**这些页面里的"撤销"跟证书链校验无关**。

因此本篇定一条纪律：凡引用"撤销"必须同时给出页号与该句宾语。T1 全文**不用"吊销"**（0 次 / 0 页），所以中文读者不能靠换词来区分这两个概念；英文侧的 `revocation` 只在 TLS 页出现 1 次，`revoked` 只在 TLS 页出现 1 次、在"保护 App 对用户数据的访问"页出现 1 次（不同语境）。

## 1.3 T1 英文取样 20 页

为排除"中文用词习惯造成的错觉"，本篇另取英文侧 20 页取样（含 TLS security、蓝牙安全性、安全隔区、钥匙串相关页面等），同样按正文域切分（起点 = 最后一个 `Copyright and trademarks` 行，终点 = `Apple Footer` 行前）。20 页正文域词频：

`certificate` 21 次 / 3 页（TLS 页 17 次，隔空投送安全性 3 次，接力安全性 1 次）；`chain` 5 次 / 4 页；`trust` 16 次 / 6 页（TLS 页 9 次）；`root` 10 次 / 3 页；`CRL` 1 次 / 1 页；`OCSP` 1 次 / 1 页；`transparency` 2 次 / 1 页；`authorit` 2 次 / 1 页；`constrain` 2 次 / 1 页；`evaluat` 3 次 / 1 页；`TLS` 29 次 / 8 页；`SSL` 3 次 / 2 页；`revocation` 2 次 / 2 页；`revok` 3 次 / 2 页；`verify` 7 次 / 5 页。

**四组零结果**：`X.509` 0、`entitlement` 0、`provisioning` 0、`validation` 0。最后一条要特别小心——它不是"英文侧不谈校验"，而是英文 TLS 页用的是 `Evaluating the trusted status of a TLS certificate`（动名词），中文侧同一句被译成小节名"证书有效性验证"；`validation` 这个名词形式在取样 20 页正文域里一次也没有。**取样 20 页不等于全量 179 页**，此结论仅在这 20 页内成立（第 19 章 G3）。

顺带一条易错点：`authorit` 只有 2 次且都在 TLS 页，因为原文用的是复数 `certificate authorities`；单数 `authority` 计数为 0。**词干计数与词形计数必须分别说明用哪一种**，否则同一份语料会得到 2 与 0 两个"事实"。

# 2 措辞层的证书词：11 段、8 页、1 个段落名

## 2.1 全量普查

普查语料：**257 份** Apple 安全响应顾问页英文镜像（转储 258 份，剔除 1 份非安全响应内容页 122085）。切块用容错版：块首 `<h3 class="gb-header[^"]*"[^>]*>`，块尾 `(?=<h3 class="gb-header|<div id="disclaimer"|<footer|$)`，段内 `<p class="(?:gb-paragraph|paragraph)[^"]*"[^>]*>`（两处类名都必须前缀匹配）。全语料得到 **9,045 段 / 470 个段落名**，其中带 Impact 或 Description 字段的段 **7,346** 段、块内含 CVE 编号的段 **7,342** 段（两者相差 4 段：有字段但块内没有 CVE 编号）、纯致谢段 **1,699** 段（7,346 + 1,699 = 9,045 ✓；分母口径与本篇的切刀刀法与 #104、#105、#106 完全一致，可直接横向比较）。

关键字普查（**块级**：一个 h3 块算一段；字段级另列）：

| 词 | 段数 | 页数 | 段落名数 | 该词的段落名分布 |
|---|---:|---:|---:|---|
| certificate | 11 | 8 | 1 | `Security` 11 |
| certificates | 8 | 8 | 1 | `Security` 8 |
| intermediate | 8 | 8 | 1 | `Security` 8 |
| authority | 8 | 8 | 1 | `Security` 8 |
| extended key usages | 8 | 8 | 1 | `Security` 8 |
| improved certificate validation | 11 | 8 | 1 | `Security` 11 |
| trust | 9 | 9 | 3 | Safari Downloads 5、AppleMobileFileIntegrity 2、MobileBackup 2 |
| keychain | 37 | 27 | 8 | Security 20、MediaRemote 5、Accounts 3、Authentication Services 3、configd 2、AppleMobileFileIntegrity 2、IDE Documentation 1、User Management 1 |
| **X.509** | **0** | 0 | 0 | — |
| **TLS** | **0** | 0 | 0 | — |
| **SSL** | 6 | 6 | 1 | `WebKit` 6（致谢噪声，见 2.5） |
| **OCSP** | **0** | 0 | 0 | — |
| **CRL** | **0** | 0 | 0 | — |
| **revocation / revoked** | **0** | 0 | 0 | — |
| **pinning / pinned** | **0** | 0 | 0 | — |
| **man-in-the-middle** | **0** | 0 | 0 | — |
| **eavesdrop** | **0** | 0 | 0 | — |
| **signature** | **0** | 0 | 0 | — |
| **trust store / trusted root / root certificate / certificate authority（整词）** | **0** | 0 | 0 | — |

注：`certificate authority` 作为**整词** 0 段，而 `certificate` 与 `authority` 分别 11 段与 8 段——原因是镜像里原文写作 `intermediate certificate authority`，本篇的整词匹配对连续空格与换行敏感（第 16.2 节记录这个匹配细节如何影响过一次结果）。

**一句话结论**：T2 谈论证书的方式，是把 11 段全部塞进一个段落名 `Security`、重复两句字段文本；它不用任何 PKI 术语（X.509、CRL、OCSP、revocation、pinning 全 0）。

## 2.2 段落名层级的空白

本篇先按"证书/信任/TLS 相关的候选组件名"逐个去 470 个段落名里找，结果为：

`Certificates` 0 段、`Secure Transport` 0 段、`TLS` 0 段、`SSL` 0 段、`Trust` 0 段、`Provisioning` 0 段、`Token` 0 段、`Profiles` 0 段、`Credential Manager` 0 段、`Network Extension` 0 段（注意存在的是 `NetworkExtension`，无空格）、`Media Remote` 0 段（存在的是 `MediaRemote`）、`libcrackspot` 0 段。

其中 `libcrackspot` 值得一提：它确实是 Security.framework 的公开测试靶件名，也曾是顾问页段落名，但**本期 257 份镜像里 0 段**。本篇不把它写成"消失"，只写成"本期未出现"（G2）。

## 2.3 决定性的一条：这 11 段在严格解析器下是 0 段

本篇复算时同时跑了两种块首匹配：

- 严格 `class="gb-header"`（属性完全相等）→ 全语料 7,615 段 / 439 个段落名；
- 容错 `class="gb-header[^"]*"`（前缀匹配）→ 9,045 段 / 470 个段落名。

差额 1,430 段逐页闭合在 149034–149043 这 10 页（各页块数 209+314+118+128+165+13+9+103+181+190）。**而 certificate 词族的 8 页 11 段，全部落在这 10 页之内**（页号 149034、149035、149036、149037、149038、149041、149042、149043）。于是：

**用严格解析器普查本主题，得到的答案是"全语料 certificate 0 段、improved certificate validation 0 段、extended key usages 0 段"，而且脚本不报错、不告警。** 三个"0"全错，且错的方向正好是"证书链校验这一层不存在"这种最容易写进结论的形态。本篇把这条登记为全篇最重要的一条方法学事实（第 16.1 节），并沿用 #104 确立的纪律：**任何"0 结论"必须附一次覆盖性证明**——探针要选"已知含带后缀类名的那一页"（149034 容错口径实测 209 段），**不能按页号大小选"最新页"**。

## 2.4 探针纪律的又一条证据

本篇在同一次复算里顺手验证了三页：149226（iOS/iPadOS 26.7.1）、149228（macOS Tahoe 26.7.1）、149229（macOS Sequoia 15.8.1），三页 `Released September 28, 2026`，页号大于上表 8 页，但用的是**旧代裸类名** `<h3 class="gb-header">`（每页 1 个块首、9 个 `gb-paragraph`），两种解析器都能看见。反过来 149034–149043 的 `Released` 字段是 September 14, 2026、条目日期是 October 19, 2026。**类名换代与页面新旧、页号大小、`Released` 日期、条目日期四者都不同步。** 这条与 #104 第 13.5 节同源，本篇只作为探针选页依据引用，不再重复展开。

## 2.5 那 6 段 SSL 是致谢噪声

`SSL` 6 段全部在 `WebKit` 名下，逐段核对后确认来自致谢字段中的人名与机构名（`…sslab at Georgia Tech` 一类字符串被大小写不敏感匹配命中）。本篇把它们登记为**假阳性**，并因此把 SSL 从"措辞层的传输安全词"结论中剔除。这是本篇对"块级计数"与"字段级计数"差异的第一处强制说明：**块级 6 段 ≠ 6 次技术提及**（第 16.3 节给出完整字段级复核表）。

# 3 中间 CA 条目深读：CVE-2026-86881

## 3.1 字段逐字

该条在本期出现在 8 页：149034（iOS 27 与 iPadOS 27）、149035（macOS Golden Gate 27）、149036（tvOS 27）、149037（watchOS 27）、149038（visionOS 27）、149041（iOS 26.7 与 iPadOS 26.7）、149042（macOS Tahoe 26.7）、149043（macOS Sequoia 15.8）。八页字段文本完全一致：

- 段落名：`Security`
- `Available for`：以 149034 为例——"iPhone 11 and later, iPad Pro 12.9-inch 4th generation and later, iPad Pro 11-inch 2nd generation and later, iPad Air 4th generation and later, iPad 9th generation and later, and iPad mini 6th generation and later"
- `Impact`：**An attacker with a compromised intermediate certificate authority may be able to issue certificates with arbitrary extended key usages**
- `Description`：**A certificate validation issue was addressed with improved certificate validation.**
- 致谢：Surya Narayan Kushwaha、Roman Zabicki、John Lussier、Filip Olszak

**这段没有条目备注字段。** 本篇对 11 段逐段核查：8 页的 certificate-validation 块**全部不含** `Entry added` / `Entry updated` 段落。作为分母对照，149034 页容错口径 209 段中有 30 段带条目备注（`Entry added October 19, 2026` 20 段、`Entry updated October 19, 2026` 10 段）。所以"这条目没有更新日期"是**这段**的属性，不是整页的属性，引用时不得写成"149034 页没有条目备注"。

## 3.2 `arbitrary extended key usages` 的分布

该短语全语料 **8 段 / 8 页 / 1 个段落名**，即它只跟随 CVE-2026-86881 出现，没有任何其它条目复用这句后果。换言之，T2 里"扩展密钥用法被任意签发"这一件事**只有一句、只挂一个组件名、只出现在 8 个产品线页**。

## 3.3 手册层对这句话没有对应词

第 1.1 节的两条零结果在此收口：T1 中文正文域 `扩展密钥` 0 次 / 0 页、`密钥用法` 0 次 / 0 页；英文取样 20 页里 `X.509` 0 次。T1 讲证书校验时讲的是"依照 RFC 5280""清单由内建根 CA 及其从属签发者的 CRL 聚合""必要时要求 OCSP"，**从不讲 EKU（Extended Key Usage）这一层约束**。

所以这条缺陷处在一个公开的盲区里：措辞层给出了后果但不给机制，手册层给出了机制但不涉及该机制位。**本篇不据此推断缺陷位于哪一层实现**（G1）。可以核对的只有两句：T2 说"certificate validation issue … improved certificate validation"；T1 说信任状态评估依照 RFC 5280 并纳入 RFC 6962。两句之间没有 Apple 提供的连接文本。

## 3.4 同句式对照：`improved certificate validation` 的全部 11 段

| 页 | 段落名 | CVE | Impact（逐字） |
|---:|---|---|---|
| 149034 | Security | CVE-2026-86881 | An attacker with a compromised intermediate certificate authority may be able to issue certificates with arbitrary extended key usages |
| 149035 | Security | CVE-2026-86889 | An attacker in a privileged network position may be able to intercept network traffic |
| 149035 | Security | CVE-2026-86881 | （同上，逐字一致） |
| 149036 | Security | CVE-2026-86881 | （同上） |
| 149037 | Security | CVE-2026-86881 | （同上） |
| 149038 | Security | CVE-2026-86881 | （同上） |
| 149041 | Security | CVE-2026-86881 | （同上） |
| 149042 | Security | CVE-2026-86889 | An attacker in a privileged network position may be able to intercept network traffic |
| 149042 | Security | CVE-2026-86881 | （同上） |
| 149043 | Security | CVE-2026-86889 | An attacker in a privileged network position may be able to intercept network traffic |
| 149043 | Security | CVE-2026-86881 | （同上） |

按页相加 1+2+1+1+1+1+2+2 = **11** ✓；按 CVE 相加 8 + 3 = **11** ✓；`Description` 字段 11 段逐字相同 ✓。**全语料该句式也只有这 11 段**（不是家族内 11、语料更多），段落名唯一 `Security`。这是本篇能给出的最强"措辞收敛"证据：一个整类缺陷（certificate validation issue）在 T2 里被压成一句、一名、两条 CVE。

## 3.5 备注字段的落点不固定：组级、且跨页不一致

第 3.1 只说了"certificate 块不含备注段"。本篇把这 8 页的 `Security` 块逐个列出其同组邻居，得到一条更弱的结论——**备注段既不是条目级，也不是稳定的组级**。

| 页 | 本页块总数 | 本页备注段总数 | `Security` 块数（按页内顺序列出其 CVE） | certificate 块自带备注 | 组内备注落点 |
|---:|---:|---:|---|---|---|
| 149034 | 209 | 30 | 86881 / 84531 / 84508 / （无 CVE） | 0 | `Entry added October 19, 2026` 落在 **84508** |
| 149035 | 314 | 51 | 86889 / 86881 / 84531 / 84508 / （无 CVE） | 0 | 同上，落在 **84508** |
| 149036 | 118 | 13 | 86881 / （无 CVE） | 0 | `Security` 组内 **0 条备注** |
| 149037 | 128 | 16 | 86881 / （无 CVE） | 0 | 同上，**0 条** |
| 149038 | 165 | 22 | 86881 / 84508 / （无 CVE） | 0 | 落在 **84508** |
| 149041 | 103 | 13 | 86881（本页仅 1 个 `Security` 块） | 0 | **0 条** |
| 149042 | 181 | 28 | 86889 / 86881 / 84508 | 0 | 落在 **84508** |
| 149043 | 190 | 28 | 86889 / 86881 / （无 CVE） | 0 | **0 条** |

两点必须据此修正读法：

1. **一条备注可以覆盖同组多条条目。** 149034 上 `Security` 组有 3 条带 CVE 的条目，但整组只有 1 句备注，且写在最后一条（84508）之后。所以"某条目块内没有备注"不能读成"该条目未单独标注加入日期"——它可能只是被同组前一条的备注覆盖了。第 3.1 的"这段没有条目备注字段"到此只能成立为**块级观察**。
2. **落点跨页不一致。** 8 页里 4 页有该组备注（都落在 84508）、4 页整组一句都没有（149036、149037、149041、149043）。同一批条目在 tvOS/watchOS 页无备注、在 macOS 页也无备注，而在 iOS/macOS 26.7 页有——**本篇无法据此判定 Apple 的备注规则**，只能判定"以块内备注判断条目新增时间"这条路在本族不可用（G13 据此改写）。

对照参考：这 8 页的页级 `Released` 字段全部逐字为 `Released September 14, 2026`（第 14 章），与组备注的 October 19, 2026 不同源、不可互换。

# 4 另一条：CVE-2026-86889，同一句 Description、完全不同方向的 Impact

## 4.1 字段逐字

该条 3 段 / 3 页（149035 macOS Golden Gate 27、149042 macOS Tahoe 26.7、149043 macOS Sequoia 15.8），全部 `Security` 名下：

- `Impact`：**An attacker in a privileged network position may be able to intercept network traffic**
- `Description`：A certificate validation issue was addressed with improved certificate validation.（与 CVE-2026-86881 **逐字相同**）
- 致谢：Jaeho Nam、Jungbum Lee、Sangwi Kang、Hyeonguk Ko and Taekyoung Kwon from SNU CSE MMLAB

**注意它只出现在 macOS 三页，不在 iOS/iPadOS、tvOS、watchOS、visionOS 任何一页。** 同页同时存在 CVE-2026-86881 的段（149035、149042、149043 都有），所以这不是"这页没登记证书条目"，而是"这页登记了两条、另三页只登记一条"。本篇据此把第 14 章写成"同一类缺陷在产品线页面间的分布不对齐"，而不是写成"某平台受影响范围"（G4）。

## 4.2 `intercept network traffic` 一词与手册层的"中间人"

T1 里表达"截获通信"的词是"中间人"，全量正文域 **2 次 / 1 页**，而那一页是**蓝牙安全性**——不是 TLS 安全性、不是网络页面。T1 的 TLS 页讲的是"无效的证书始终导致硬故障和无连接"，通篇不用"中间人"三字。

T2 侧，`man-in-the-middle` **0 段**、`eavesdrop` **0 段**（第 2.1 表），`privileged network position` 属于本篇家族的只有 3 段（即 86889）。这与 #103、#104 得出的"近场族不使用 man-in-the-middle / eavesdrop 句式"一致；本篇补充的是：即使在**证书校验**这个最该出现该词的方向上，T2 同样不使用它。

**本篇不写**："CVE-2026-86889 是中间人攻击漏洞"。可以写的只有：顾问页对该条 Impact 的原文是 `An attacker in a privileged network position may be able to intercept network traffic`，Description 原文是那句 certificate validation 句式；"中间人"是读者的概括，不是 Apple 的措辞。

# 5 `Security` 名下 114 段：18 种 Impact 句与 19 种 Description 句

## 5.1 为什么先做这一章

第 2 章证明了"证书"这个词只占 `Security` 名下 114 段中的 11 段。要判断"中间授权风险"在 T2 里到底怎么被表述，必须先把这个段落名的**全部字段文本穷尽**，否则很容易把"我只搜了 certificate"当成"这个组件名下只讲了证书"。本章取全 114 段，按 Impact 原文与 Description 原文分别归并，逐行给出段数，并保证**逐行相加闭合**。

## 5.2 Impact 侧：91 段带 Impact，归并为 18 种句子

| # | Impact 句子（逐字） | 段数 |
|---:|---|---:|
| 1 | A remote attacker may be able to cause a denial-of-service | 15 |
| 2 | A remote attacker may be able to leak memory | 8 |
| 3 | A remote user may be able to cause a denial-of-service | 8 |
| 4 | A local attacker may be able to modify the state of the Keychain | 8 |
| 5 | An attacker with a compromised intermediate certificate authority may be able to issue certificates with arbitrary extended key usages | 8 |
| 6 | An app may be able to access protected user data | 7 |
| 7 | A local attacker may gain access to user's Keychain items | 7 |
| 8 | A malicious app acting as a HTTPS proxy could get access to sensitive user data | 6 |
| 9 | An app may be able to read Safari's browsing history | 4 |
| 10 | An app may be able to access associated usernames and websites in a user's iCloud Keychain | 4 |
| 11 | An app may be able to access user-sensitive data | 3 |
| 12 | Third party app extensions may not receive the correct sandbox restrictions | 3 |
| 13 | An attacker in a privileged network position may be able to intercept network traffic | 3 |
| 14 | Processing maliciously crafted NTLM input may lead to unexpected app termination | 2 |
| 15 | A malicious app with root privileges may be able to access keyboard input and location information without user consent | 2 |
| 16 | An app may be able to gain root privileges | 1 |
| 17 | An app may be able to access sensitive user data | 1 |
| 18 | An attacker may be able to modify the state of the Keychain | 1 |

逐行相加 = **91** ✓。另有 **23 段为纯致谢**（无 CVE、无 Impact），114 = 91 + 23 ✓。

三点必须写下来的分布特征：

**一、Keychain 词汇 20 段，占带 Impact 段的 22%。** 由第 4、7、10、18 四句构成（8+7+4+1=20）。也就是说这个段落名下最成体系的后果不是"证书被伪造"，而是"钥匙串状态被改、钥匙串条目被读"。第 5 句那 8 段（中间 CA）在段数上与第 2、3、4 句完全相同，**不能靠段数给它们排序**。

**二、"HTTPS proxy" 6 段是本族唯一把恶意 App 放在传输路径中间的说法**（第 8 句）。它的可达性主语是 `A malicious app`，前置条件是本地安装，不是网络位置。这句话与第 13 句（`An attacker in a privileged network position`）在 T2 里是**两套不同的写法**，Apple 没有把它们归到同一模板；把两者合并称为"中间人风险"是本报告读者最容易犯的错，本篇在第 16.4 节把这条列为"禁止合并的两个句子"。

**三、同一句 Impact 可跨多个段落名。** 第 11 句 `An app may be able to access user-sensitive data` 与第 17 句 `An app may be able to access sensitive user data` 在本篇家族里各自还出现在别的段落名下（第 6 章的每名句子表给出完整分布）。**两句只差一个连字符，语义上不构成"同一种后果"**，本篇全程按原文分列、不做归并。

## 5.3 Description 侧：91 段归并为 19 种句子

| # | Description 句子（逐字） | 段数 |
|---:|---|---:|
| 1 | A certificate validation issue was addressed with improved certificate validation. | 11 |
| 2 | The issue was addressed with improved input validation. | 8 |
| 3 | An integer overflow was addressed with improved input validation. | 8 |
| 4 | A validation issue was addressed with improved logic. | 8 |
| 5 | A denial-of-service issue was addressed with improved input validation. | 8 |
| 6 | This issue was addressed with improved permissions checking. | 7 |
| 7 | A logic issue was addressed with improved checks. | 7 |
| 8 | This issue was addressed with improved access restrictions. | 6 |
| 9 | This issue was addressed with improved validation of the caller's access. | 4 |
| 10 | This issue was addressed with improved redaction of sensitive information. | 4 |
| 11 | A logging issue was addressed with improved data redaction. | 4 |
| 12 | This issue was addressed with improved validation of symlinks. | 3 |
| 13 | The issue was addressed by adding additional logic. | 3 |
| 14 | An access issue was addressed with additional sandbox restrictions. | 3 |
| 15 | An out-of-bounds write issue was addressed with improved bounds checking. | 2 |
| 16 | A permissions issue was addressed with additional restrictions. | 2 |
| 17 | This issue was addressed through improved state management. | 1 |
| 18 | A race condition was addressed with additional validation. | 1 |
| 19 | A package validation issue was addressed by blocking the vulnerable package. | 1 |

逐行相加 = **91** ✓。

三处值得单独登记：

- 第 19 句 `blocking the vulnerable package` **全语料只有 1 段 / 1 页**，就是 149034 那页之外的 126348（macOS Tahoe 26.3），段落名 `Security`，Impact 是 `An app may be able to gain root privileges`（CVE-2026-20658）。全语料这一句式没有第二例，所以它既不能归入"input validation"族，也不能归入"checks"族——**T2 的修复句式表比通用印象更细**。
- 第 9 句 `improved validation of the caller's access` 全语料 **4 段 / 4 页 / 1 个段落名**（`Security`），即它也是唯一归属；对应 CVE-2026-84508。
- 第 1 句与第 6 句、第 8 句之间的分野（certificate validation / permissions checking / access restrictions）说明：**"授权被判错"这件事在 T2 至少用三种不同句式表达，而"证书被判错"只用一种**。这个不对称是本篇第 12 章"双向沉默"的措辞层证据。

# 6 家族 304 段：三组划分、65 个 CVE、104 页

## 6.1 组定义（先声明这是本报告的自建分组）

T2 不给"主题"分组，只给段落名。为了回答"证书链校验与中间授权"这一主题在 T2 的总量，本篇按段落名自建三组。**分组是本报告的解释动作，不是 Apple 的分类**，第 19 章 G5 登记其风险。

**A 组 · 信任判定与密码学（3 名 / 125 段）**

| 段落名 | 段 | 页 | 含 CVE 段 | 纯致谢段 |
|---|---:|---:|---:|---:|
| Security | 114 | 71 | 91 | 23 |
| Security Initialization | 6 | 6 | 6 | 0 |
| CoreCrypto | 5 | 5 | 4 | 1 |
| **小计** | **125** | — | **101** | **24** |

**B 组 · 认证与凭证（11 名 / 90 段）**

| 段落名 | 段 | 页 | 含 CVE 段 | 纯致谢段 |
|---|---:|---:|---:|---:|
| AuthKit | 22 | 22 | 13 | 9 |
| Authentication Services | 18 | 13 | 18 | 0 |
| AppleKeyStore | 14 | 13 | 9 | 5 |
| NetAuth | 15 | 9 | 15 | 0 |
| Keychain Access | 6 | 6 | 6 | 0 |
| Single Sign-On | 6 | 6 | 6 | 0 |
| Passkeys | 5 | 5 | 5 | 0 |
| AppleFDEKeyStore | 1 | 1 | 1 | 0 |
| Local Authentication | 1 | 1 | 0 | 1 |
| Trusted Device | 1 | 1 | 1 | 0 |
| AppleCredentialManager | 1 | 1 | 0 | 1 |
| **小计** | **90** | — | **74** | **16** |

**C 组 · 委托与配置（5 名 / 89 段）**

| 段落名 | 段 | 页 | 含 CVE 段 | 纯致谢段 |
|---|---:|---:|---:|---:|
| Sandbox Profiles | 33 | 27 | 23 | 10 |
| Managed Configuration | 24 | 24 | 17 | 7 |
| Transparency | 22 | 22 | 12 | 10 |
| DeviceCheck | 8 | 8 | 8 | 0 |
| Endpoint Security | 2 | 2 | 0 | 2 |
| **小计** | **89** | — | **60** | **29** |

三组相加 125 + 90 + 89 = **304** ✓；含 CVE 段 101 + 74 + 60 = **235** ✓；纯致谢段 24 + 16 + 29 = **69** ✓。

## 6.2 CVE 与页

- 家族**去重后 CVE 编号 65 个**（A 25、B 23、C 17）。三组相加恰为 65，说明**没有任一 CVE 同时出现在两组**——这不是"证书类与授权类互不相干"的证据，而是本篇分组按段落名切、同一 CVE 在同组内多个段落名下才会被重复归组；它至少证明本篇的分组没有把同一 CVE 拆到两组导致重复计数。
- 至少含一个家族 CVE 的页面 **104 页**（占语料 257 份的 40.5%）。
- 家族 235 个含 CVE 段 / 65 个 CVE ≈ 每编号 3.6 段，即多数条目按产品线复制成多段。**段数不等于缺陷个数**（这条与 #104、#106 一致）。
- 出现在 ≥5 页的家族 CVE 共 15 个：CVE-2024-27884（5）、CVE-2024-44184（5）、CVE-2024-54538（8）、CVE-2025-24180（5）、CVE-2025-30471（8）、CVE-2025-31221（8）、CVE-2025-46290（7）、CVE-2026-20637（8）、CVE-2026-28860（8）、CVE-2026-28864（7）、CVE-2026-43730（5）、CVE-2026-64743（6）、CVE-2026-84583（8）、CVE-2026-84612（8）、CVE-2026-86881（8）。**页多不等于严重**，只等于该产品线页面各自登记了一遍。

## 6.3 严格口径下的家族缺口

同一把严格切块刀（`class="gb-header"` 完全相等）在本家族上得到 **239 段**，容错口径 304 段，**漏 65 段 / 21.4%**。更有后果的是段落名层级：`AppleFDEKeyStore`、`DeviceCheck`、`Local Authentication` 三个名在严格口径下**整个消失**（它们只在带后缀标记的页上出现过）。也就是说，严格刀不只减少总数，还会**删掉组件名**，让"某组件本期 0 段"这种说法变得不可靠。第 7.4 节按名核对时逐名标注了这一点。

# 7 家族内部的四个邻接落点

本章处理"证书校验在 T2 最接近的实现层候选"，并全部降级为归类表述。

## 7.1 `CFNetwork` 25 段：措辞层最接近传输安全的落点

`CFNetwork` 名下 25 段（20 段含 CVE、5 段纯致谢），其中 `validat` 词 8 段。T1 明确说"上层 API（如 CFNetwork）使开发者可以轻松在其 App 中采用 TLS"、"CFNetwork 不接受 SSL 3"，而 T2 在 `CFNetwork` 名下**一次也没有出现 TLS、SSL、certificate 三个词**（第 2.1 表：TLS 0 段、SSL 6 段全在 WebKit 名下）。**本篇不写"CFNetwork 负责证书校验所以它的 25 段是证书链风险"**；只写：手册层把 CFNetwork 与 TLS 关联，措辞层把 25 段归入 CFNetwork 名下但不使用任何传输安全术语。这个断口是 G6。

## 7.2 `Security Initialization` 6 段

2 种 Impact 句各 3 段：`An app may be able to break out of its sandbox`（3）与 `An app may be able to access protected user data`（3）。名带 "Initialization"，但 T2 不给任何初始化语义文本；T1 也没有对应小节。**不推断**它对应信任库装载、锚点初始化或框架启动路径（G7）。

## 7.3 `CoreCrypto` 5 段：本族唯一一句密码学机制描述

4 段共用同一句 Impact：`An attacker may be able to decrypt legacy RSA PKCS#1 v1.5 ciphertexts without having the private key`，Description 为 `A timing side-channel issue was addressed with improvements to constant-time computation in cryptographic functions.`（CVE-2024-23218，4 页：120306 watchOS 10.3、120880、120884、120886）。另 1 段纯致谢。

这条在本篇的位置：**T2 唯一一次把修复方式写到算法族级别**（PKCS#1 v1.5）并给出缺陷类型（timing side-channel）与修复类型（constant-time computation）。相比之下第 3 章的证书条目只写到 "certificate validation issue"。`constant-time computation` 全语料在本族 **4 段 / 4 页**（与 CVE-2024-23218 的 4 页一致，无第二例）。T1 侧对等物是"数字签名和加密"页（证书 9 次），但该页不谈计时侧信道（G8）。

## 7.4 `AppleKeyStore` 14 段与 `AuthKit` 22 段

`AppleKeyStore` 14 段里 9 段共用一句 `An app may be able to cause unexpected system termination`，5 段纯致谢；**14 段中没有一句提到 key、keychain、certificate 或 entitlement**（第 9 章词表可查）。`AuthKit` 22 段（13 含 CVE、9 纯致谢）两句主导：`A local app may be able to read a persistent account identifier`（8）与 `An app may be able to fingerprint the user`（5）。

这两组用于说明一件事：**在 T2 里，"凭证存储组件"的措辞与"凭证/密钥"这两个词经常不同段出现**。段落名承担了归类功能，字段文本不承担。这正是本篇拒绝把段落名当实现层的核心理由（贯穿纪律）。

# 8 `trust` 一词的 9 段：三种说法、三个段落名、零段落在 `Security`

## 8.1 全量分布

`trust` 全语料块级 **9 段 / 9 页 / 3 个段落名**，逐段核对（字段级 Impact 命中 9/9，Description 命中 0/9——即 trust 只出现在 Impact 侧）：

| 页 | 段落名 | CVE | Impact（逐字） |
|---:|---|---|---|
| 121563 | Safari Downloads | CVE-2024-44259 | An attacker may be able to misuse a trust relationship to download malicious content |
| 121564 | Safari Downloads | CVE-2024-44259 | （同上） |
| 121566 | Safari Downloads | CVE-2024-44259 | （同上） |
| 121567 | Safari Downloads | CVE-2024-44259 | （同上） |
| 121571 | Safari Downloads | CVE-2024-44259 | （同上） |
| 124149 | AppleMobileFileIntegrity | CVE-2025-43253 | A malicious app may be able to launch arbitrary binaries on a trusted device |
| 124150 | AppleMobileFileIntegrity | CVE-2025-43253 | （同上） |
| 149034 | MobileBackup | CVE-2026-84598 | An attacker with physical access to a trust-paired device may be able to read and write arbitrary files |
| 149041 | MobileBackup | CVE-2026-84598 | （同上） |

**三种不同构词**：`trust relationship`（5 段 / 5 页 / 1 名）、`trusted device`（2 段 / 2 页 / 1 名）、`trust-paired device`（2 段 / 2 页 / 1 名）。三者在 T2 里**互不复用**，也不共享段落名。

## 8.2 为什么这条是反转而不是补充

直觉上"信任"是证书链校验的主题词，`Security` 名下 114 段应该大量使用它。实测：**`Security` 名下 trust 一词 0 次**（第 5.2 的 18 句穷尽表可直接复核）。反过来，全语料那 9 段 trust 没有一段属于本篇 A 组的信任判定语义。

于是本篇得到一条明确的方向差：**T2 用 certificate 谈证书而不谈信任，用 trust 谈"已配对/已信任的设备或下载来源"而不谈证书。** 两个词在同一段落文本里从未共现。任何把"iOS 信任模型缺陷 N 条"当作 T2 统计结果的写法，在这里都必须作废——T2 没有"信任模型"这个统计口径。

## 8.3 顺带一个重要的"家族 0 / 语料非 0"

CVE-2026-84598（`MobileBackup`）的 Description 是 `A path traversal issue was addressed with improved path validation.`。`improved path validation` 全语料 **94 段 / 50 页 / 26 个段落名**，但**在本篇 304 段家族内为 0 段**（第 10.3 节）。这条是"家族级 0 不等于全语料 0"的标准示范，也是本篇坚持给出双口径的原因。

# 9 措辞层表达"中间授权"的真实词：entitlement 与 authorization

## 9.1 三个词的规模

| 词 | 段 | 页 | 段落名数 | Impact 字段命中段 | Description 字段命中段 |
|---|---:|---:|---:|---:|---:|
| authorization | 198 | 64 | 52 | 11 | 194 |
| signed | 210 | 108 | 27 | 8 | 202 |
| authentication | 142 | 82 | 36 | 93 | 78 |
| entitlement | 131 | 74 | 38 | 7 | 128 |
| keychain | 37 | 27 | 8 | 37 | 0 |
| certificate | 11 | 8 | 1 | 8 | 11 |

三点读法：

**一、`entitlement` 与 `authorization` 是 Description 侧的词，不是 Impact 侧的词。** entitlement 的 131 段里只有 7 段的 Impact 含该词，128 段在 Description 里；authorization 的 198 段里 Impact 侧只有 11 段。也就是说 **Apple 在"后果"里几乎不说"授权"，在"修法"里才说**。这是本篇标题用"中间授权风险"时必须加限定语的原因：T2 没有"N 段授权风险后果"这种口径。

**二、`keychain` 完全相反**：37 段全部在 Impact 侧、Description 侧 0 段。**后果里说钥匙串，修法里不说钥匙串。** certificate 是唯一两侧都出现的词（Impact 8、Description 11）。

**三、`entitlement` 分布在 38 个段落名**，但本篇家族 19 名里只有 **3 名**命中：`Transparency` 5、`NetAuth` 3、`Single Sign-On` 3；族外高频名是 `Power Services` 8、`Assets` 7、`CoreServices` 6、`NetworkExtension` 6、`iCloud Document Sharing` 6。**"授权属性"在 T2 是一个横跨几十个组件名的词，不是一个组件**。

## 9.2 T1 的对侧

T1 中文正文域"授权"172 次 / 63 页、"描述文件"46 次 / 17 页（峰值在"配置执行"页 15 次），"信任缓存"页（`sec7d38fbf97`）给出全系列最直白的一句委托描述：**"非平台二进制文件（例如已公证的第三方代码）必须拥有有效的证书链才能执行，它们可拥有的授权受 Apple 开发者计划向开发者签发的签名描述文件限制。"**（发布日期 2021-02-18）

这句话在本篇的地位很特殊：**它是 T1 里唯一把"有效证书链"与"授权范围受描述文件限制"写在同一句里的手册文本**。而 T2 的 certificate 11 段与 entitlement 131 段**从未同段共现**（本篇按段核查，共现 0 段）。**两层各说一次、彼此不指认**——这就是本篇主轴。

## 9.3 一条容易读反的话

T1 同页还有："在搭载 Apple 芯片的 Mac 上，此标识符（平台标识符）用于指示即使二进制文件由 Apple 签名，但仅当其代码目录哈希值在信任缓存中存在时才能执行"；"若要执行信任缓存，SIP 必须保持活跃"。**这些都是 macOS 表述**。本篇不把它们移用到 iOS 上（该页正文域 28 行中多数为 macOS 语境；第 15.2 节完整引用并把适用范围写进括注）。

# 10 修复句式矩阵：家族 304 段的 21 个候选句式

## 10.1 表（块级，家族内）

| 句式 | 段 | 页 |
|---|---:|---:|
| additional restrictions | 38 | 27 |
| improved input validation | 29 | 29 |
| improved checks | 20 | 20 |
| improved state management | 17 | 15 |
| improved access control | 11 | 8 |
| **improved certificate validation** | **11** | **8** |
| additional entitlement checks | 6 | 6 |
| improved access restrictions | 6 | 6 |
| improved redaction | 4 | 4 |
| constant-time computation | 4 | 4 |
| improved validation of the caller's access | 4 | 4 |
| removing the vulnerable code | 3 | 3 |
| improved bounds checking | 2 | 2 |
| improved path validation | **0** | 0 |
| improved sandbox restrictions | **0** | 0 |
| improved authorization checks | **0** | 0 |
| improved locking | **0** | 0 |
| removed | **0** | 0 |
| privacy preferences | **0** | 0 |

（另：`improved trust evaluation`、`improved validation of the certificate chain`、`improved trust` 三种"想当然"的写法本篇专门查过，**家族内 0 段、全语料 0 段**；作为对照，`improved memory handling` 全语料 844 段但在本家族 **0 段**，属于第 10.3 那类"家族 0 / 语料非 0"。此外 `trust evaluation` 这个名词短语在全语料块内正文出现 **0 次**——它是 T1 手册层的说法，不是 T2 的说法。）

## 10.2 `improved certificate validation` 的双重唯一性

这一句在家族内 11 段 / 8 页，**在全语料同样只有 11 段 / 8 页 / 1 个段落名**。它不像 `improved checks`（家族 20 段，全语料 994 段 / 183 页）那样是通用句式。第 3.4 节已给出 11 段逐页清单，此处补一条排序结论：

**在 21 个句式里，"专用于单一主题"的只有三个**：`improved certificate validation`（11 段，全语料=家族）、`constant-time computation`（4 段，全语料=家族）、`improved validation of the caller's access`（4 段，全语料=家族）。这三个句式的语料级分布与家族级分布完全重合，说明它们各自绑定一个具体条目族，不构成通用模板。其余句式都能在全语料找到家族外的大量复用。

## 10.3 三个"家族 0 / 全语料非 0"

| 句式 | 家族段 | 全语料段 | 全语料页 | 全语料段落名数 |
|---|---:|---:|---:|---:|
| improved path validation | 0 | 94 | 50 | 26 |
| additional sandbox restrictions | 3（仅 `Security`） | 86 | 44 | 25 |
| improved locking | 0 | 家族外大量存在（另见 #103/#104） | — | — |

`improved path validation` 的家族 0 与语料 94 是本篇最容易造成的错读：读者若只看第 10.1 表会以为"Apple 在本主题从不修路径校验"，而实际上 `MobileBackup` 名下 2 段（第 8.1 表末两行）用的正是它——只是 `MobileBackup` 不在本篇分组内。**分组边界会造出 0，这是自建的 0，不是语料的 0**（第 16.5 节）。

`additional sandbox restrictions` 的家族 3 段全部在 `Security` 名下，Impact 都是 `Third party app extensions may not receive the correct sandbox restrictions`（CVE-2024-40821，3 页 120910/120911/120912）。它证明：**"授权边界配错"在 T2 是通过沙箱句式表达的，而不是通过 certificate 句式**——这与第 9 章的 entitlement/authorization 分布相互印证。

## 10.4 一个必须给出的负结果：`improved authorization checks`

直觉上"授权检查被改进"应该是很常见的说法。实测：家族 **0 段**、全语料 **0 段**。Apple 在授权方向用的句式是 `additional entitlement checks`（6 段 / 6 页）、`improved permissions checking`（家族 7 段，全语料 17 段 / 15 页 / 6 名）、`improved access control`（11 段 / 8 页）、`improved access restrictions`（6 段 / 6 页）、`improved validation of the caller's access`（4 段 / 4 页）。

**五句不同写法、零句是"authorization checks"。** 本篇据此把"授权类修复"登记为一个**至少五式**的开放集合，不允许在下游引用时把它缩写成一句。

# 11 八条具名条目深读

本章逐条给出字段原文、页号与页面 `Released` 字段。所有实现层归属一律降级为"顾问页将其归入某段落名"。

## 11.1 CVE-2026-86881（中间 CA 签发任意 EKU）

- 段落名 `Security`，8 段 / 8 页（149034、149035、149036、149037、149038、149041、149042、149043），`Released September 14, 2026`。
- Impact：`An attacker with a compromised intermediate certificate authority may be able to issue certificates with arbitrary extended key usages`（8 段逐字一致）。
- Description：`A certificate validation issue was addressed with improved certificate validation.`
- 无条目备注段（第 3.1）。
- 各页 `Available for` 取值差异（举例逐字）：149034 "iPhone 11 and later, iPad Pro 12.9-inch 4th generation and later, … and iPad mini 6th generation and later"；149036 "Apple TV 4K 2nd generation and later"；149037 "Apple Watch Series 9 and later"；149038 "Apple Vision Pro (all models)"。
- 致谢：Surya Narayan Kushwaha、Roman Zabicki、John Lussier、Filip Olszak。

**本篇对这条只写三件事**：措辞、页分布、Available for 差异。不写它影响哪个 TLS 库、不写 EKU 校验位于哪个函数、不写任何"如何利用被攻破的中间 CA"的过程（后者是公开 PKI 常识，但本报告不给出可操作构造）。

## 11.2 CVE-2026-86889（同一修法、网络位置后果）

见第 4 章。补一条：149035 的 `Available for` 逐字为 "MacBook Neo (2026), MacBook Air with Apple silicon (2020 and later), MacBook Pro with Apple silicon (2020 and later), iMac with Apple silicon (2021 and later), Mac mini with Apple silicon (2020 and later), Mac Studio (2022 and later), and Mac Pro with Apple silicon (2023)"。**这是 macOS 专属机型清单，没有对应 iOS 页面**——所以"这条只影响 Mac"是**页分布层面的观察**，不是 Apple 的适用范围声明（G4）。

## 11.3 CVE-2026-84508（调用者访问校验）

- `Security`，4 段 / 4 页（149034、149035、149038、149042）。
- Impact：`An app may be able to access protected user data`；Description：`This issue was addressed with improved validation of the caller's access.`
- 意义：`improved validation of the caller's access` 全语料仅此 4 段、1 个段落名（第 10.2）。**"调用方身份被判错"这一类修复只在 `Security` 名下出现过。**
- 附带一条：这一条是 4 页（149034、149035、149038、149042）上 `Security` 组最后一条带 CVE 的条目，因此**整组唯一的备注段 `Entry added October 19, 2026` 写在它后面**——第 3.5 用它作为"备注落点是组级"的直接证据。引用这条日期时不能写成"84508 单独于 10 月 19 日加入"。

## 11.4 CVE-2026-84531（NTLM 越界写）

- `Security`，2 段 / 2 页（149034、149035）。
- Impact：`Processing maliciously crafted NTLM input may lead to unexpected app termination`；Description：`An out-of-bounds write issue was addressed with improved bounds checking.`
- 意义：`NTLM` 一词在本篇家族里仅此 2 段。它挂在 `Security` 名下说明 T2 允许把**认证协议输入解析**归到该段落名——这是"段落名≠主题"的又一例。

## 11.5 CVE-2025-24250 与 CVE-2025-43233（同句 HTTPS proxy）

- 均 `Security`，各 3 段：前者页 122373、122374、122375；后者页 124149、124150、124151。
- Impact 逐字相同：`A malicious app acting as a HTTPS proxy could get access to sensitive user data`；Description 逐字相同：`This issue was addressed with improved access restrictions.`
- 合计构成第 5.2 表第 8 句的 6 段。**两个 CVE、一份句子文本**——引用"6 段 HTTPS 代理风险"时必须说清它只对应 2 个编号，否则读者会当成 6 起独立缺陷。

## 11.6 CVE-2025-24180（WebAuthn 后缀抢占）

- `Authentication Services`，5 段 / 5 页（122371、122373、122376、122378、122379）。
- Impact：`A malicious website may be able to claim WebAuthn credentials from another website that shares a registrable suffix`；Description：`The issue was addressed with improved input validation.`
- 该句在 `Authentication Services` 名下占 5/18 段（第 6.1 组 B 表；该名 6 种句子中最高频的是这一句）。
- 意义：这是本篇家族里**唯一把"域名归属判定"写进 Impact 的句式**。T1 侧对"可注册后缀"没有任何对应词（英文取样 20 页 `registrable` 0 次；中文"单点登录"17 次 / 5 页，但那是另一页的另一话题）。

## 11.7 CVE-2024-23218（PKCS#1 v1.5 计时侧信道）

见第 7.3。四页 120306（watchOS 10.3）、120880、120884、120886，`CoreCrypto` 名下 4 段共用一句 Impact。全语料 `constant-time computation` 仅此 4 段。

## 11.8 CVE-2026-20658（唯一的 blocking the vulnerable package）

- `Security`，1 段 / 1 页（126348，macOS Tahoe 26.3，`Released February 11, 2026`）。
- Impact：`An app may be able to gain root privileges`；Description：`A package validation issue was addressed by blocking the vulnerable package.`
- 意义：**全语料该句式 1 段 / 1 页 / 1 名**。它是"包级校验缺陷"唯一的公开表述形态（第 5.3）。同时它也提醒：`Security` 名下**唯一一句 root privileges 后果**（第 5.2 表第 16 句，1 段）与证书毫无关系。

## 11.9 八条合计与不重复性

八条共占段落：8 + 3 + 4 + 2 + 3 + 3 + 5 + 1 = **29 段**，分布在 6 个不同页组、3 个不同段落名（Security、Authentication Services、CoreCrypto）。**29 段仅占家族 304 段的 9.5%**，占 A 组 125 段的 23.2%。本篇给出的具名条目已经覆盖了全部含 certificate / trust / constant-time / caller's access / package validation 五种句式的段，其余 275 段属于通用后果句式，不逐一具名（G9）。

# 12 双向沉默：手册层讲机制不讲缺陷，措辞层讲缺陷不讲机制

| 主题 | T1 是否讲机制 | T1 是否讲缺陷 | T2 是否讲缺陷 | T2 是否讲机制 |
|---|---|---|---|---|
| TLS 证书信任状态如何评估（RFC 5280 / 6962） | **是**，原句见 15.1 | 否 | 否（无 RFC 字样） | 否 |
| 撤销清单如何聚合、OCSP 何时被要求 | **是**，15.1 | 否 | 否（CRL/OCSP/revocation 全 0 段） | 否 |
| 无效证书的连接结果 | **是**："始终导致硬故障和无连接" | 否 | 否 | 否 |
| System Roots 存放互联网 PKI 根 CA | **是**，15.3 | 否 | 否（`system roots` 0 段） | 否 |
| Boot ROM 内 Apple 根 CA 公钥 = 信任链第一步 | **是**，15.4 | 否 | 否 | 否 |
| 非平台二进制需有效证书链、授权受描述文件限制 | **是**，15.2 | 否 | 部分：`Security Initialization` 6 段含 3 段 break out of its sandbox | 否 |
| 中间 CA 被攻破可签发任意 EKU | **否**（"扩展密钥""密钥用法"0 次） | — | **是**，8 段 / 8 页 | 只到 "certificate validation issue" |
| 网络位置截获流量（证书校验缺陷所致） | **否**（"中间人"仅蓝牙页） | — | **是**，3 段 / 3 页 | 否 |
| 调用方访问被判错 | **否** | — | **是**，4 段 / 4 页 | 一句 caller's access |
| HTTPS 代理式恶意 App | 否 | — | **是**，6 段 / 2 个 CVE | 一句 access restrictions |
| WebAuthn 可注册后缀抢占 | **否** | — | **是**，5 段 / 5 页 | 一句 input validation |

**读法（这是本篇的核心论点，不是修辞）**：

**一、T1 的证书文本是"策略承诺"文本**，它给出可核对的判定标准（RFC 编号、清单来源、失败行为），但**不含任何缺陷、条件子句或后果句**。全量 179 页里证书相关的 29 页，没有一页写"攻击者可以…"。

**二、T2 的证书文本是"结果通知"文本**，它给出缺陷与修法，但**不含任何判定标准**：11 段里没有一句提到 RFC、CRL、OCSP、X.509、System Roots、信任链。

**三、两层之间没有桥句。** 本篇专门在容错解析出的**全部 9,045 段块内正文**里查过指向手册层的两种可能形态：字符串 `documentation` **0 次**、`guide/security` **0 次**。因此：**"Apple 文档说无效证书始终硬故障，所以证书校验缺陷的后果应该是连接失败"这条推理不成立**——T2 对证书缺陷写出的实际后果是"签发任意 EKU"和"截获网络流量"，都不是"连接失败"。**手册层的承诺不能用来预测措辞层的后果**，这是本篇对下游引用最硬的一条限制。

# 13 在野状态：本家族 0 段（附覆盖性证明）

## 13.1 度量

在野声明句式 `Apple is aware of a report`（含其变体前缀 `Apple is aware of …`）全语料块级 **93 段 / 65 页 / 12 个段落名**。与本篇家族 304 段的交集 **0**。

12 个段落名的完整分布（相加 = 93）：

| 段落名 | 段 |
|---|---:|
| WebKit | 27 |
| Messages | 9 |
| CoreMedia | 8 |
| Kernel | 8 |
| RTKit | 8 |
| ImageIO | 7 |
| CoreAudio | 5 |
| JavaScriptCore | 5 |
| dyld | 5 |
| Accessibility | 4 |
| RPAC | 4 |
| CoreGraphics | 3 |

## 13.2 覆盖性证明（本篇对"0"的强制自证）

1. **分母证明**：93 段来自容错解析器全语料普查（9,045 段），非子集抽样。
2. **探针证明**：普查在 149034 上得到该页全部块数 209，若解析器在该页命中 0 段则整轮作废（第 2.3）。这条正是为排除"标记换代造成的假 0"而设。
3. **同页反证**：本篇家族 304 段分布在 **115 个页面**上，其中 **22 页**同时含有至少一段使用 `Apple is aware` 的段落（在其它组件名下）。也就是说，解析器看得见这些页、句式在这些页可被匹配——**0 是"家族 304 段内 0"这个块级结果，不是"页面级 0"**。反过来也成立且更严：承载 certificate 11 段的那 8 页（149034/35/36/37/38/41/42/43）**整页都没有** `Apple is aware` 字样（逐页 HTML 原文计数均为 0），所以本族的 0 在这 8 页上是页级与块级同时成立的结果。
4. **相邻提醒（不可外推）**：`Accessibility` 名下 4 段在野声明讲的是物理攻击可禁用 USB 受限模式（#105 第 14.6 节已登记）。这条的存在说明"本家族 0 段"**不能**写成"证书/授权方向从未被在野利用"或"物理与网络接口方向都不存在在野利用"。0 只覆盖本篇定义的 304 段。
5. **CoreGraphics 3 段的其中 1 段**来自 149226（iOS/iPadOS 26.7.1，`Released September 28, 2026`），Impact 原文含 "Apple is aware of a report … exploited … versions of iOS before iOS 27"，致谢 Meta Product Security。它是本期镜像里 `Released` 最新的页面之一，与本篇家族无关，但它的存在正是第 2.4 节"页号不等于标记代际"的实证对象。

## 13.3 允许写与不许写

**允许**：本期 257 份镜像中，顾问页未对任何被本篇归入 A/B/C 三组的段落使用在野声明句式；`Security` 名下 114 段无一段含该句式。
**不许**：不许写"证书链校验方向无在野攻击"、"PKI 相关缺陷不实用"、"该方向风险低"。这三句都需要非公开信息或攻击成本估计，本篇没有取数路径（G10）。

# 14 页面对齐：同一类缺陷在不同产品线页上的登记不齐

本章从第 3.4 与 4.1 的观察抽出一条通用纪律。

**观察一**：CVE-2026-86881 覆盖 iOS/iPadOS（27 与 26.7 两页）、macOS（Golden Gate 27、Tahoe 26.7、Sequoia 15.8 三页）、tvOS 27、watchOS 27、visionOS 27 共 8 页；CVE-2026-86889 只覆盖 macOS 三页。两者 Description 逐字相同、段落名相同。**同一种修法在两页族上的登记范围不同。**

**观察二**：同页可能存在多条同段落名条目（149035 与 149042、149043 各含 2 段 certificate 句）。**页内段数 ≠ 该页该组件条目数 ≠ 该 CVE 的缺陷数。**

**观察三**：字段不齐不限于本族。同一 CVE 在不同产品线页的条目备注可能不同（#104 第 13.6 节记录了 CVE-2026-86957 在 149037 写 `Entry updated`、其余 7 页写 `Entry added` 的实例）。本篇对 certificate 两编号核查的结果是 **11 段全部无条目备注**（第 3.1），这是一个方向相反的齐整结果，同样需要点名到"哪一页的哪一个字段"才能引用。但第 3.5 把这条限定到块级：同组邻居 CVE-2026-84508 在 4 页上带着整组唯一一句 `Entry added October 19, 2026`，另 4 页的 `Security` 组一句备注都没有。所以本族能引用的只有"**块内无备注**"，不能引用"该条目无新增日期"。

**纪律（写入附表 D）**：凡引用一个 CVE 的"影响范围"，必须先列出实际核查过的页号清单，再说这些页各自的 `Released` 与 `Available for`；**不得由"CVE 已公开"推定所有产品线页面都列了它**，也不得由"某产品线页未列"推定该产品线不受同类缺陷影响。

# 15 手册层的六页可引原句

以下六页是本篇全部 T1 引用的出处。每页给出页面 ID、发布日期、本篇用到的原句。**所有原句均为正文域文本，不改动标点。**

## 15.1 `sec100a75d12`「TLS 安全性」，发布日期 2026 年 1 月 28 日

正文域 30 行，含全量 179 页里**唯一**的"证书透明度""已撤销""内建根""有效性""硬故障"用法。逐句：

- "iOS、iPadOS 和 macOS 支持传输层安全协议（TLS 1.0、TLS 1.1、TLS 1.2、TLS 1.3）和数据包传输层安全协议 (DTLS)。TLS 协议同时支持 AES-128 和 AES-256 并首选提供正向保密的密码套件。"
- "上层 API（如 CFNetwork）使开发者可以轻松在其 App 中采用 TLS，而底层 API（如 Network.framework）则提供精细控制。CFNetwork 不接受 SSL 3，且使用 WebKit 的 App（如 Safari 浏览器）禁止建立 SSL 3 连接。"
- "在运行 iOS 11、iPadOS 13.1、macOS 10.13 或更高版本的设备上，除非用户信任，否则不再允许将 SHA-1 证书用于 TLS 连接。也不允许使用 RSA 密钥短于 2048 位的证书。"
- "在运行 iOS 12.1 的设备上，2018 年 10 月 15 日之后由系统信任根证书签发的证书必须在可信任证书透明度日志中记录才可进行 TLS 连接。"
- "在运行 iOS 12.2 或更高版本的设备上，针对 Network.framework 和 NSURLSession API 默认启用 TLS 1.3。**使用 SecureTransport API 的 TLS 客户端不能使用 TLS 1.3。**"
- "在运行 iOS 26、iPadOS 26 或更高版本的设备上，针对 URLSession 框架和 Network API 默认启用具有量子安全加密的 TLS 1.3（采用 X25519MLKEM768 密钥交换算法）。"
- "服务器必须支持 TLS 1.2 和正向保密，且证书必须有效并使用 SHA-256 或更强的加密算法签名，以及包含至少 2048 位 RSA 密钥或 256 位椭圆曲线密钥。"
- "不满足这些要求的网络连接会失败，除非 App 重写了 App 传输安全性。**无效的证书始终导致硬故障和无连接。**"
- 「证书有效性验证」小节："对 TLS 证书信任状态的评估依照既定的行业标准（如 RFC 5280 所列）执行，且整合了新出现的标准，如 RFC 6962（证书透明度）。在运行 iOS 11 或更高版本以及 macOS 10.13 或更高版本的设备上，Apple 设备会定期更新当前已撤销和受限证书的列表。**该列表由 Apple 信任的每个内建根证书颁发机构及其从属的 CA 签发者所发布的证书撤销清单 (CRL) 聚合而成**，列表还可能根据 Apple 的要求酌情包括其他限制。每当使用网络 API 函数进行安全连接时都会使用此信息。**如果需要单独列出的 CA 撤销证书太多，信任评估可能需要在线证书状态响应 (OCSP)，否则将无法通过信任评估。**"
- 另参见栏给出两条支持文章：103187（与取消对 Symantec 证书颁发机构的信任有关的信息）、103769（iOS 13 和 macOS 10.15 中的可信证书应满足的要求）。

**与本篇的接点**：这一页就是 T1 的全部证书机制叙述。它把"撤销与受限清单由根 CA 及其**从属签发者**发布"写成了聚合结构——这正是第 3 章那条"中间 CA 被攻破"在概念上的对应物，**但该页通篇没有写"中间 CA 被攻破会怎样"**。本篇把这条登记为 T1 的最重要单点缺口（G11）：手册层说明了清单的来源链条包含从属 CA，却没有说明链条中间节点被攻破时的行为；措辞层则相反，只说了后果、没说清单。

## 15.2 `sec7d38fbf97`「信任缓存」，发布日期 2021 年 2 月 18 日

正文域仅 28 行，却集中了全量 179 页最密的"信任"用法（24 次）。逐句（**多数为 macOS 语境，括号内注明**）：

- "静态信任缓存是安全启动链中包括的一个对象，它是所有装入签名系统宗卷的 Mach-O 二进制文件的受信任记录。每个 Mach-O 由一个代码目录哈希值表示。"（macOS）
- "运行二进制文件时（无论是在生成新进程的过程中，还是在将可执行代码映射到现有进程的过程中），会提取其代码目录并计算哈希值。如果生成的哈希值可在信任缓存中找到，则为该二进制文件创建的可执行映射会被授予平台权限……与之相反，在基于 Intel 的 Mac 上，平台权限由为二进制文件签名的 Apple 证书传递给操作系统内容。（此证书不限制二进制文件可拥有的授权。）"
- "**非平台二进制文件（例如已公证的第三方代码）必须拥有有效的证书链才能执行，它们可拥有的授权受 Apple 开发者计划向开发者签发的签名描述文件限制。**"
- "在基于 Intel 的 Mac 上，平台标识符用于执行将二进制文件从较旧的 macOS 版本中**定向撤销**的操作；这种定向撤销有助于防止这些二进制文件在较新的版本上执行。"（此处"撤销"= 二进制撤信，不是证书撤销；第 1.2 节的撞词实例）
- "静态信任缓存将一组二进制文件完全锁定到给定的 macOS 版本中。此行为可帮助防止较旧版本操作系统中由 Apple 合法签名的二进制文件被引入较新版本的操作系统，进而被攻击者利用。"
- "若要执行信任缓存，SIP 必须保持活跃。"（macOS；SIP 不是 iOS 可开关项，此点已在 #102 更正过）

**该页发布日期为 2021-02-18**，而 TLS 安全性页为 2026-01-28。本篇把这条差登记为一条独立事实：**手册层里"证书链决定能否执行"这句话已有近五年未更新**，引用时必须带发布日期（附表 C）。

## 15.3 `secb0694df1a`「钥匙串数据保护」，发布日期 2024 年 12 月 19 日

- "除了用户钥匙串，macOS 还依靠多个系统级钥匙串来维护非用户特定的认证资源，如网络凭证和公用密钥基础设施 (PKI) 身份。其中一个钥匙串 System Roots 不可更改，**它储存了互联网 PKI 根证书颁发机构 (CA) 证书**，以辅助网上银行和电子商务等常见任务。用户可以类似的方式将内部预置的 CA 证书部署到受管理的 Mac 电脑，以帮助验证内部站点和服务。"

**这是全量 179 页唯一写明根锚点存放位置（System Roots）的句子，且明确是 macOS 表述。** 措辞层对 `system roots` 一词 0 段（本篇查过），T1 英文取样 20 页也 0 次。**"System Roots 里有 PKI 根 CA"这句 iOS 侧的对应文本，本期两层都没有提供**（G12）。

## 15.4 `secb3000f149`「iPad 和 iPhone 设备启动过程」，发布日期 2021 年 2 月 18 日

- "启动过程每个步骤包含的组件都经 Apple 加密签名以启用完整性检查，因此只有在验证信任链后，启动才能继续。"
- "iPad 和 iPhone 设备开机后，其应用程序处理器会立即执行只读内存（称为 Boot ROM）中的代码。这些不可更改的代码（称为硬件信任根）是在制造芯片时设定的隐式受信任代码。**Boot ROM 代码包含 Apple 根证书颁发机构 (CA) 公钥**，该公钥用于验证 iBoot 引导载入程序是否经过 Apple 签名，以决定是否允许其载入。**这是信任链中的第一步**，信任链中的每个步骤都会检查下一步骤是否已经过 Apple 的签名。"

本页是 iOS 侧唯一给出信任链起点的页面（正文域 27 行，"信任"5 次）。它与第 15.3 合起来构成本篇所谓"锚点的两种说法"：一个说 iOS 的信任链起点是 Boot ROM 里的根 CA 公钥，一个说 macOS 的根锚点存放在 System Roots 钥匙串；**两者都不是顾问页词汇，也不与任何 CVE 相联**。

## 15.5 `sec8a37b4cb2`「针对 Apple 设备的管理式设备证明」，发布日期 2026 年 1 月 28 日

- 威胁列举原文包含"**攻击者劫持证书请求以骗取证书颁发机构 (CA) 为其签发证书**"——这是全量 179 页里最接近"签发被欺骗"这一 T2 后果的中文句子。
- "通过管理式设备证明，设备可请求获得来自 Apple 证明服务器的证明，Apple 证明服务器会返回**由叶证书和中间证书（植根于 Apple 企业证明根证书颁发机构）组成的数据数组**。"
- "**一旦收到证明，后端服务需要仔细执行验证检查。此类检查包括确保叶证书由 Apple 企业证明根证书颁发机构签发，将动态刷新码的哈希值与预期值比对，以及检查证明中的其他属性。**"
- "验证设备的身份及其安全态势，并使 ACME 解决方案在签发证书前执行信任评估。"
- "作为使用 ACME 协议签发证书中的一环，设备可能会被要求提供证明，这也会使得关联密钥对受安全隔区保护。"

**本篇对这条的用法很克制**：这是 Apple 唯一一处把手册层的信任评估责任**明确写给第三方后端**（"后端服务需要仔细执行验证检查"）。它与第 3 章的"中间 CA 可签发任意 EKU"在结构上是同一类问题（判定方需要正确核验链条），但**前者是部署建议、后者是缺陷通知，Apple 没有把两者相连**。引用时不得写成"Apple 文档已警告中间 CA 风险"。

## 15.6 `secf64471c16`「iOS 中的车钥匙安全性」，发布日期 2026 年 1 月 28 日

- "设备公钥 (device.PK) 会以 **X.509 证书格式与证书链一起**发回车辆，该证书链可由生产时内嵌于车辆的汽车制造商根证书公钥 (root.PK) 验证。"
- "设备将其 device.PK 证书链发送到密钥追踪服务器 (KTS)，该服务器会发回签名以确认注册。……然后车辆会在 KTS 签名验证成功后启用车主钥匙。KTS 签名密钥为汽车制造商所有。"
- "接收者设备会使用中继服务器将钥匙创建证书链发回发送者的 iPhone。然后发送者的 iPhone 会使用内嵌于安全元件的 root.PK 验证接收者钥匙是否已使用预期的证书链创建。"

**这是全量 179 页唯一出现 X.509 的地方**（正文域），也是唯一描述"第三方根 + 链验证 + 注册回执签名"三段式流程的页面。措辞层对 `X.509` **0 段**。本篇把这一对差额作为"手册层讲格式与流程、措辞层不讲格式"的第二例（第一例是第 3.3 节的 EKU）。

# 16 自我纠错与口径陷阱

本章逐条登记本篇在写作过程中纠正或险些写错的地方。**每条给出错误形态、纠正方式与影响范围。**

## 16.1 严格解析器给出的"0 段"是错的（本篇最重要一条）

初稿普查证书族时把块首写成 `class="gb-header"`（完全相等），得到全语料 7,615 段 / 439 名，`certificate` 命中 **0 段**、`improved certificate validation` 命中 **0 段**。脚本不报错、不告警、不给出可疑迹象。改用容错版（`class="gb-header[^"]*"` 前缀匹配 + 块尾三选一）后得到 9,045 段 / 470 名，`certificate` 命中 **11 段 / 8 页 / 1 名**。

**影响范围**：若未复算，本篇会出现"顾问页本期完全不谈证书"这种结论，并把第 3、4、11.1、11.2 章整章写成负结果。这正是"0 结论"最危险的形态——**它看起来比正结论更像发现**。
**纠正方式**：沿用 #104 附表 D 第 7 项的探针纪律（149034 必须 209 段），并把探针按**类名**而非页号选定（第 2.4）。

## 16.2 整词匹配把 `certificate authority` 测成 0

`certificate` 与 `authority` 分别命中 11 与 8 段，但整词 `certificate authority` 命中 0 段。核查后确认原文写作 `intermediate certificate authority`，本篇的整词正则在字段文本里被换行与实体符打断。**结论：整词 0 不代表概念 0**。凡"某术语 0 段"都必须同时给出词干计数与整词计数（第 1.3 节的 `authorit` / `authority` 是同一教训的另一种表现）。

## 16.3 `SSL` 6 段是致谢噪声，不是传输安全表述

见第 2.5。大小写不敏感匹配把致谢中的机构名片段命中。这是本篇唯一一次**正数形式的假阳性**，其余陷阱都是"漏"。因此本篇对所有关键词表同时给出段落名分布：SSL 的 6 段全部落在 `WebKit` 名下，与本篇家族无任何交集——**看段落名分布就能立刻发现词跑错了地方**，这一步不能省。

## 16.4 两句不许合并

第 5.2 表第 8 句 `A malicious app acting as a HTTPS proxy could get access to sensitive user data` 与第 13 句 `An attacker in a privileged network position may be able to intercept network traffic`，在中文里都能写成"中间人"。T2 用了两套主语（恶意 App / 处于网络优势位置的攻击者）、两套前置条件（本地安装 / 网络位置），Apple 未把它们归入同一模板。**合并 = 造出 Apple 没说过的口径。** 同理，第 11 句 `user-sensitive data` 与第 17 句 `sensitive user data` 相差一个连字符，本篇全程分列，**不做归并统计**。

## 16.5 分组边界会造出 0

第 10.3 的 `improved path validation`（家族 0 / 语料 94）。如果本篇把 `MobileBackup` 纳入家族（它在第 8.1 承载了 2 段 trust），家族就会从 304 段变 306 段，且 `improved path validation` 从 0 变 2。**同一个句式在不同分组下有不同计数，两者都不是错，但引用时必须写清分组。** 本篇给出三组完整名清单（第 6.1），任何复算都应说明与本篇分组的差集。

## 16.6 "带后缀标记的 10 页"不等于"最新 10 页"

本篇初稿曾写"certificate 11 段集中在最新 10 页"。核查后改为"**标记带后缀的 10 页（149034–149043）**"，因为 149226 / 149228 / 149229 三页的 `Released` 更新（September 28, 2026）却用旧代裸类名（第 2.4）。**"最新"这个词在描述解析器可见性时是错的**，本篇已把这条回写进 #104 的相应章节。

## 16.7 段数、CVE 数、页数三个量不可互换

本篇内出现三种量纲，必须各自点名：

- certificate 词族：**11 段**，但只有 **2 个 CVE 编号**、**8 个页面**、**1 个段落名**、**1 份 Description 文本**、**2 种 Impact 文本**。
- 家族：**304 段**、**65 个 CVE**、**104 页（含家族 CVE 的页面）**、**115 页（承载家族任一区块的页面）**、**19 个段落名**。
- `Security`：**114 段**、**91 段带字段**、**18 种 Impact 句**、**19 种 Description 句**、**23 段纯致谢**。

注意 104 与 115 的区别：后者按"页面上出现过家族块"计，前者按"页面上出现过家族 CVE"计；两者相差 11 页 = 只以纯致谢形式出现家族段落名的页面。**引用时必须说是哪一个。**

## 16.8 一处 T1 计数陷阱：正文域 vs 全文件

同一份转储里"信任"全文件 364 次 / 179 页、正文域 182 次 / 53 页；"证书"全文件口径明显更高（侧栏目录含多页标题）。**如果不做正文域切分，本页所有 T1 数字都要作废**。本篇的切法与验证（179/179 成功、`NO_MARKER=0`）见第 1.1 与附表 D 第 5 项。

# 17 合图：证书与授权判定矩阵

| 层次 | 本篇能否核对 | 依据 | 引用时必须带的限定 |
|---|---|---|---|
| TLS 证书信任状态如何评估 | **仅 T1** | 15.1 两句 RFC 5280 / 6962 原句 | 手册层承诺，非缺陷证据；不写实现位置 |
| 撤销清单如何聚合、OCSP 何时要求 | **仅 T1** | 15.1 同节 | "由根 CA 及其从属 CA 签发者的 CRL 聚合"是文档原话 |
| 无效证书的失败行为 | **仅 T1** | 15.1"始终导致硬故障和无连接" | 不能用来预测 T2 后果（第 12 章三） |
| 根锚点存放位置 | **仅 T1，且 macOS** | 15.3 System Roots | 不得移用到 iOS |
| iOS 信任链起点 | **仅 T1** | 15.4 Boot ROM 内根 CA 公钥 | "信任链第一步"是文档措辞 |
| 中间 CA 被攻破可签发任意 EKU | **仅 T2** | 3.1 / 3.2 / 11.1 的 8 段 | 后果句，无机制；段落名不等于实现层 |
| 证书校验缺陷导致流量被截获 | **仅 T2** | 4.1 / 11.2 的 3 段（macOS 三页） | 页分布不等于适用范围（G4） |
| 调用方访问被判错 | **仅 T2** | 11.3 的 4 段、句式全语料唯一 | — |
| HTTPS 代理式读取敏感数据 | **仅 T2** | 11.5 的 6 段 / 2 个 CVE | 6 段是复制，不是 6 起缺陷 |
| WebAuthn 后缀抢占 | **仅 T2** | 11.6 的 5 段 / 5 页 | T1 无对应词 |
| PKCS#1 v1.5 计时侧信道 | **仅 T2**（T1 只讲算法族） | 11.7 的 4 段 | 唯一写到算法族的条目 |
| 已配对/已信任设备被读取 | **仅 T2** | 8.1 的 9 段（3 种构词） | 与证书无关，别并表 |
| 描述文件限制可拥有的授权 | **仅 T1** | 15.2 原句（2021-02-18） | 该页未更新近五年 |
| 某方向的在野利用情况 | **本家族：无声明** | 13.1 / 13.2 | 0 只覆盖 304 段（13.3） |
| 缺陷位于哪个进程 / 哪个函数 | **两层都不能** | 源码层本期为空 | 一律标 G1 |

# 18 八条结构性事实

**一、一个主题在两层里的可核对密度悬殊到只有 1 与 150。** T1 中文正文域"证书"150 次 / 29 页并给出 RFC 编号与失败行为；T2 全语料 `certificate` 11 段 / 8 页 / **1 个段落名**、`Description` 只有 1 份文本。**这两组数字描述的不是同一件事**：一层的密度是"机制解释的密度"，另一层的密度是"缺陷登记的密度"，两者不可换算。

**二、本主题最重要的一次采集风险是"静默 0"。** certificate 词族 11 段所在的 8 页，恰是严格解析器完全忽略的 8 页；用严格刀测得的是 0 段而不是"少 11 段"。**错误不是量差，是有无**，而且不报错（第 16.1）。本篇把探针页 149034 = 209 段作为任何后续复算的第一道闸门。

**三、`Security` 名下 114 段里，trust 一词 0 次；全语料 trust 9 段无一在 `Security`。** 措辞层把"证书校验"与"信任"写在了两处，三种 trust 构词各自绑定不同组件（第 8.1）。因此"iOS 信任模型漏洞统计"没有 T2 口径可支撑。

**四、"授权"在 T2 是 Description 侧的词，"钥匙串"在 T2 是 Impact 侧的词。** `entitlement` 131 段里只有 7 段在 Impact；`keychain` 37 段全部在 Impact、Description 侧 0 段。**后果与修法用的是两套词汇**，这是本篇对"某词有多少段所以风险多大"这类读法的正面否定。

**五、修复句式在本主题上至少五式、且"authorization checks"为 0。** `additional entitlement checks`、`improved permissions checking`、`improved access control`、`improved access restrictions`、`improved validation of the caller's access` 各自独立存在；`improved authorization checks` 家族 0、全语料 0。任何把授权修复缩写成一句的下游引用都是不忠实的。

**六、三句"主题专用"句式与语料级分布完全重合。** `improved certificate validation`（11 段）、`constant-time computation`（4 段）、`improved validation of the caller's access`（4 段）在家族内的计数等于全语料计数——它们各自绑定一个条目族，不是通用模板（第 10.2）。这是本篇能给出的最强"句式收敛"结论。

**七、同一类缺陷在不同产品线页的登记范围不齐。** CVE-2026-86881 覆盖 8 页（含 tvOS/watchOS/visionOS），Description 逐字相同的 CVE-2026-86889 只覆盖 macOS 3 页。两者都不含条目备注段。**页集合是登记事实，不是影响范围**（第 14 章）。

**八、手册层的信任链叙述里，最要紧的一句已经五年未更新。** "非平台二进制文件必须拥有有效的证书链才能执行，它们可拥有的授权受……签名描述文件限制"出自 2021-02-18 的「信任缓存」页；同一手册里「TLS 安全性」页是 2026-01-28。**手册层内页之间存在五年量级的更新不同步**，引用时必须带各页发布日期（附表 C）。

# 19 缺口登记：本篇不能回答的十五件事

**G1 源码层为空。** 本期 XNU 与用户态开源部件镜像未取得。凡"证书校验发生在哪个进程/哪个函数/哪次调用"的表述，本篇一律降级为"顾问页将其归入某段落名"。**结构性缺口。**

**G2 `libcrackspot` 本期 0 段。** 它是公开的 Security.framework 测试靶件名，也曾出现在顾问页段落名中；本期 257 份镜像 0 段。本篇不解释原因，不写成"下架"。

**G3 英文手册层只取样 20 页。** 第 1.3 的全部零结果（X.509、entitlement、provisioning、validation 各 0）只在取样内成立，不能外推到 179 页英文全量。

**G4 页集合 ≠ 影响范围。** CVE-2026-86889 只登记在 macOS 三页（第 11.2），本篇不能判定 iOS 是否存在同一缺陷或是否不适用。

**G5 本篇分组是自建口径。** 三组 19 名（第 6.1）由本报告划定；Apple 不提供主题分组。换一种分组会改变多个 0（第 16.5）。

**G6 `CFNetwork` 与 TLS 的关系无法从 T2 得到。** 25 段内不含 TLS/SSL/certificate 任一词（第 7.1）。

**G7 `Security Initialization` 语义未知。** 6 段无任何初始化描述（第 7.2）。

**G8 `CoreCrypto` 的算法层归属未核对。** 4 段写 PKCS#1 v1.5，但 T1 侧"数字签名和加密"页不谈计时侧信道（第 7.3）。

**G9 具名条目只覆盖 29 段。** 其余 275 段属通用后果句式，本篇不逐一具名（第 11.9），因此"本篇列出的条目 = 本主题主要条目"不成立。

**G10 在野与攻击成本不可推。** 第 13.3 的三条"不许写"。

**G11 T1 未写中间 CA 被攻破的行为。** 手册层只写清单来源含从属 CA 签发者（第 15.1 末段）。

**G12 iOS 侧根锚点存放位置无文本。** System Roots 句只出现在 macOS 语境（第 15.3），且 `system roots` 在 T2 全语料 0 段。

**G13 条目备注的落点规则未知。** 第 3.5 逐页列出：certificate 两编号的 11 段全无备注段，但同组邻居（CVE-2026-84508）在 4 页上带着整组唯一的 `Entry added October 19, 2026`，另外 4 页整组一句备注都没有。本篇因此**只能否定**"用块内备注判断条目新增时间"这条路，不能给出 Apple 备注字段的书写规则，也不知道这是缺省、继承还是排版差异。

**G14 同一页面的产品版本分支未逐一核对。** 本篇核查了 149034–149043 八页的 `Released`（September 14, 2026）与条目日期（October 19, 2026），但未对 115 个家族页面逐页核对两字段。

**G15 术语表口径未取。** T1 有独立"术语表"页（`sec93292bfa6`，正文域"信任"2 次），本篇未据其建立官方术语定义，因此"证书链""信任缓存"等在报告中均为**引用页面原词**而非官方定义（该缺口为结构性：Apple 未在术语表给出 PKI 词条的可核对对应，本篇未逐条验证）。

# 20 运维与检测：本篇能支持的十四项动作

每项都必须能回到本篇某一节的原文依据。**不支持任何形式的证书链构造、伪造签发、中间人配置或 PoC 制作。**

1. **核查某 CVE 是否在特定产品线页登记**：查第 3.4 / 4.1 / 11 章的页清单；口径见第 14 章纪律。
2. **区分"证书撤销"与"二进制定向撤销"两类中文句子**：用第 1.2 的页号级"撤销"分布；两词在 T1 同形，只能靠页与宾语区分。
3. **判断某产品版本是否要求证书透明度日志**：以第 15.1"运行 iOS 12.1 的设备上，2018 年 10 月 15 日之后由系统信任根证书签发的证书……"为准，并核对该页发布日期。
4. **判断 SecureTransport 与 URLSession/Network.framework 的 TLS 1.3 差异**：第 15.1 两句（iOS 12.2 起 Network.framework/NSURLSession 默认启用 TLS 1.3；SecureTransport 不能使用 TLS 1.3）；这是**API 层配置差异**，可直接用于兼容性排查。
5. **判断 SHA-1 / 短 RSA 密钥证书是否被允许**：第 15.1（iOS 11 起，除非用户信任；RSA 短于 2048 位不允许）。
6. **判断量子安全密钥交换是否已在默认路径启用**：第 15.1（iOS 26 / iPadOS 26 起，URLSession 与 Network API 默认启用 X25519MLKEM768）。
7. **为 MDM 后端确认验证责任**：第 15.5"后端服务需要仔细执行验证检查……确保叶证书由 Apple 企业证明根证书颁发机构签发，将动态刷新码的哈希值与预期值比对"。这是可执行的配置核查清单来源。
8. **核查车钥匙场景是否使用 X.509 链与 KTS 回执**：第 15.6 三句。
9. **建立"授权类修复"的措辞检索集**：第 10.4 的五句；并明确 `improved authorization checks` 检索应为 0，若非 0 说明语料或解析器变了。
10. **为本主题建立解析器验收测试**：断言 `certificate` = 11 段、`improved certificate validation` = 11 段 / 1 名、`extended key usages` = 8 段、探针页 149034 = 209 段。任一项不符即停止引用该轮任何计数（第 16.1）。
11. **核对某条目是否声明在野利用**：家族 304 段内 0；引用时带第 13.3 的三条禁止外推。
12. **判断"信任"一词能否用于统计信任模型缺陷**：不能。第 8.2 给出 9 段的三种构词与三个组件名，均不指向证书判定。
13. **判断"某组件名下 N 段"是否等于该组件有 N 个缺陷**：不等。第 16.7 的量纲表与 #104、#106 一致地说明同一段落名可在同页出现多次、也存在无字段纯致谢段。
14. **判断本报告能否支持任何风险排名**：不能。第 0 章问题四、第 12 章、第 18 章第一条共同给出：T1 与 T2 的密度不可换算，句式的段数只反映复制次数。

# 附表 A 术语口径（30 行）

| 术语 | 本篇用法 | 来源层与章节 | 不得混用 |
|---|---|---|---|
| 证书链校验 | 指 T1「TLS 安全性」页的整套信任状态判定叙述 | T1（15.1） | 不等同于 T2 的 `A certificate validation issue` |
| 中间授权 | 本篇标题用词，指 T2 的 `compromised intermediate certificate authority` + 任意 EKU 签发 | T2（3.1、3.2） | T1 无该词，也无"中间 CA 被攻破"的任何表述（G11） |
| 段落名 | 顾问页 `<h3 class="gb-header…">` 的标签文本 | T2（2.1） | 不是框架、进程或模块名（16.7） |
| 段 | 一个 `<h3>` 块（自该块首至下一同级块首 / `disclaimer` / `footer`） | 本篇口径（2.3） | 不是块内 `<p>` 计数 |
| 正文域 | 中文：最后一个逐字 `版权和商标` 行之后 → 首个含 `Apple Footer` 行之前 | 本篇 T1 切分（1.1） | 不含约 235 行全站侧栏目录 |
| 手册层 / T1 | 《Apple 平台安全保护》zh-CN 179 页转储 + 英文 20 页取样 | 全篇 | 不用于缺陷计数 |
| 措辞层 / T2 | Apple 安全响应顾问页 257 份英文镜像 | 全篇 | 不用于机制解释 |
| 撤销 | T1 同形多义：证书作废 / 二进制定向撤销 | T1（1.2） | 引用必须同时给页号与该句宾语 |
| 吊销 | T1 不用（0 次 / 0 页） | 1.1 | 不可当作"证书撤销"的区分用词 |
| 信任评估 | T1 词，仅 3 页（启动过程 3、管理式设备证明 2、TLS 2） | T1（1.1、15.1） | 与 T2 的 `trust` 9 段无对应关系（8.2） |
| trusted device / trust relationship / trust-paired | T2 的三种 `trust` 构词，各绑不同组件名 | T2（8.1） | 三者都不指向证书判定 |
| 信任缓存 | T1 专名页 `sec7d38fbf97`（macOS 语境） | T1（15.2） | 不是 T2 的任何 trust 段 |
| entitlement | T2 词，131 段 / 74 页 / 38 名，其中 Impact 侧仅 7 段 | T2（9.1） | T1 英文取样 20 页 0 次（G3） |
| authorization | T2 词，198 段 / 64 页 / 52 名，Impact 侧仅 11 段 | T2（9.1） | 不等于中文"授权"的任一手册用法 |
| EKU / extended key usages | T2 短语，8 段 / 8 页 / 1 名 | T2（3.2） | T1「扩展密钥」「密钥用法」均 0 / 0（3.3） |
| 硬故障 | T1 词，1 次 / 1 页 | T1（15.1） | 不是 T2 用词，不能当作后果模板 |
| 证书透明度 | T1 词，2 次 / 1 页（RFC 6962） | T1（15.1） | T2 全语料 `transparency` 段落文本 0 段 |
| CRL 聚合 / OCSP | T1 短语，仅 TLS 页 | T1（15.1） | T2 全语料 `CRL` / `OCSP` / `revocation` 0 段（2.1） |
| System Roots | T1 词，唯一一处根锚点存放位置，且属 macOS | T1（15.3） | T2 全语料 `system roots`、`trusted root`、`root certificate` 均 0 段 |
| 条目备注 | 顾问页块内 `Entry added` / `Entry updated` / `Entry moved` 段 | T2（3.5） | 落点为组级且不跨页一致，不得按条目归属 |
| `Released` | 页级发布日期字段 | T2（14 章） | 与条目备注日期不同源、不可互换 |
| 家族 | 本报告自建三组共 19 个段落名、304 段 | 第 6.1 | 非 Apple 分类（G5） |
| 含家族 CVE 的页面 | 104 页（按页面出现过家族 CVE 计） | 6.2 | 与"承载家族块的页面 115 页"是两个量（16.7） |
| 纯致谢段 | 无 CVE、无 Impact/Description 的块 | 2.1、6.1 | 不计入任何缺陷数 |
| 在野利用声明 | `Apple is aware …` 句式 | T2（13.1） | 家族 0 段 ≠ 该方向无在野利用（13.3） |
| 描述文件 | T1 词，46 次 / 17 页，峰值「配置执行」15 次 | T1（1.1、15.2） | 与 T2 候选名 `Profiles`（0 段）不构成互证 |
| 单点登录 | T1 词，17 次 / 5 页 | T1（1.1） | 与 T2 名 `Single Sign-On`（6 段）之间无桥句 |
| 通行密钥 | T1 词，2 次 / 2 页 | T1（1.1） | 与 `Passkeys`（5 段）无对应文本 |
| 硬件信任根 | T1 词，6 次 / 4 页 | T1（15.4） | 不等于 T2 的任何 trust 段 |
| 根证书颁发机构 | T1 词，6 次 / 5 页 | T1（15.4、15.5） | T2 整词 `certificate authority` 命中 0 段，但概念存在（16.2） |

# 附表 B 顾问页条目索引（9 行）

本章只列本篇正文点名的条目。**"页面 → 产品版本"是登记位置，不是影响范围**（G4）。

| CVE | 段落名 | 登记页面 → 产品版本（页级 `Released`） | Impact 主语（逐字片段） | Description 关键短语 | 备注核查 |
|---|---|---|---|---|---|
| CVE-2026-86881 | Security | 149034 iOS 27/iPadOS 27、149035 macOS Golden Gate 27、149036 tvOS 27、149037 watchOS 27、149038 visionOS 27、149041 iOS/iPadOS 26.7、149042 macOS Tahoe 26.7、149043 macOS Sequoia 15.8（八页均 September 14, 2026） | An attacker with a compromised intermediate certificate authority | improved certificate validation | 块内无备注；8 段 |
| CVE-2026-86889 | Security | 149035、149042、149043（三页皆 macOS） | An attacker in a privileged network position | improved certificate validation（与上行逐字相同） | 块内无备注；3 段 |
| CVE-2026-84508 | Security | 149034、149035、149038、149042 | An app | improved validation of the caller's access（全语料仅此 4 段） | **承载 `Security` 组唯一备注 `Entry added October 19, 2026`（3.5）** |
| CVE-2026-84531 | Security | 149034、149035 | Processing maliciously crafted NTLM input | improved bounds checking | 块内无备注 |
| CVE-2025-24250 | Security | 122373 macOS Sequoia 15.4、122374 macOS Sonoma 14.7.5、122375 macOS Ventura 13.7.5（均 March 31, 2025） | A malicious app acting as a HTTPS proxy | improved access restrictions | 与下行 Impact/Description 逐字相同（16.4） |
| CVE-2025-43233 | Security | 124149 macOS Sequoia 15.6、124150 macOS Sonoma 14.7.7、124151 macOS Ventura 13.7.7（均 July 29, 2025） | 同上（同句） | improved access restrictions（同句） | 6 段 = 2 个编号，不是 6 起缺陷 |
| CVE-2025-24180 | Authentication Services | 122371 iOS/iPadOS 18.4、122373 macOS Sequoia 15.4、122376 watchOS 11.4、122378 visionOS 2.4、122379 Safari 18.4 | A malicious website | improved input validation | 家族内唯一把域名归属判定写进 Impact 的句式 |
| CVE-2024-23218 | CoreCrypto | 120306 watchOS 10.3（January 22, 2024）、120880 iOS/iPadOS 16.7.6（March 5, 2024）、120884 macOS Monterey 12.7.4、120886 macOS Ventura 13.6.5（后两页 March 7, 2024） | An attacker | constant-time computation（全语料仅此 4 段） | 家族唯一写到密码算法族的条目 |
| CVE-2026-20658 | Security | 126348 macOS Tahoe 26.3（February 11, 2026） | An app（后果为 gain root privileges） | blocking the vulnerable package（全语料唯一） | `Security` 名下唯一 root privileges 后果句 |

**九条合计覆盖 29 段**（8+3+4+2+3+3+5+4+1，其中 24250 与 43233 共 6 段）——与第 11.9 的"八条 29 段"是同一批段落，本表把两句 HTTPS proxy 拆成两行呈现。**其余 275 段不具名**（G9）。

# 附表 C 本篇引用的手册页清单（19 行）

**每页必须带发布日期**；第 18 条结构性事实（八）就是靠这张表的日期差成立的。

| 手册页（sec hash） | 页面名 | 中文页发布日期 | 本篇引用章节 |
|---|---|---|---|
| sec100a75d12 | TLS 安全性 | 2026-01-28 | 1.1、1.2、3.3、15.1、20 章第 3–6 项 |
| sec7d38fbf97 | 信任缓存 | 2021-02-18 | 1.2、9.2、9.3、15.2、18 条八 |
| secb0694df1a | 钥匙串数据保护 | 2024-12-19 | 15.3、12 章 |
| secb3000f149 | iPad 和 iPhone 设备启动过程 | 2021-02-18 | 15.4、17 章 |
| sec8a37b4cb2 | 针对 Apple 设备的管理式设备证明 | 2026-01-28 | 1.1（证书颁发机构峰值）、15.5、20 章第 7 项 |
| secf64471c16 | iOS 中的车钥匙安全性 | 2026-01-28 | 15.6（X.509 唯一页） |
| sec82597d97e | 蓝牙安全性 | 2021-02-18 | 1.1（"中间人"2 次唯一页）、4.2 |
| secf6fb9f053 | 配置执行 | 2024-05-07 | 1.1（"描述文件"峰值 15 次）、9.1 |
| sec469d47bd8 | 在 macOS 中防范恶意软件 | 2024-12-19 | 1.2（"撤销"5 次，非证书义） |
| secc50cff810 | Apple 钱包中的证件 | 2026-01-28 | 1.2（"撤销"4 次，非证书义） |
| seccab60e931 | 使用 HomeKit 保护路由器 | 2021-02-18 | 1.2（"撤销"2 次，非证书义） |
| secc01781f46 | 保护 App 对用户数据的访问 | 2022-05-13 | 1.2（英文 `revoked` 1 次，非 TLS 语境） |
| seca500d4f2b | Mac 电脑中的外围处理器安全性 | 2021-02-18 | 1.2（"撤销"1 次） |
| sec88be9900f | 保护对用户健康数据的访问 | 2026-01-28 | 1.2（"撤销"1 次） |
| sec30bdef041 | 访问钥匙类型 | 2024-05-07 | 1.2（"撤销"1 次） |
| seca09c3b780 | 停用 Apple Pay 付款卡 | 2024-12-19 | 1.2（"撤销"1 次） |
| sec59b0b31ff | 安全隔区 | 2024-12-19 | 1.2（"撤销"1 次）、15.6 关联 |
| sec75f6d5040 | 使用 Apple 钱包访问 | 2024-05-07 | 1.2（"撤销"1 次） |
| sec93292bfa6 | 术语表 | **该页转储无发布日期字段** | 19 章 G15 |

上表第 9–18 行合起来正是第 1.2 的"撤销 24 次 / 12 页"分布：5+4+3+3+2+1×7 = 24 ✓（信任缓存与 TLS 安全性两页同时属于第 15 章主引页）。**"撤销"这 12 页里只有 2 页（TLS 安全性、信任缓存）与证书相关**，这是本篇要求逐页点名的原因。

# 附表 D 普查方法与可复算说明

1. **手册层取数**。中文侧为《Apple 平台安全保护》zh-CN 指南 **179 页全量转储**；英文侧为 **20 页取样**（非全量，第 1.3 的所有零结果只在取样内成立，G3）。两份都要先切正文域：中文取最后一个逐字 `版权和商标` 行之后、到首个含 `Apple Footer` 行之前；英文取最后一个 `Copyright and trademarks` 行之后、到 `Apple Footer` 行之前。中文侧 **179/179 切分成功、`NO_MARKER=0`**。不切分时"信任"是全文件 364 次 / 179 页，切分后 182 次 / 53 页——**正文域与全文件两个口径的差可达 2 倍，引用时必须点名是哪一个**（16.8）。
2. **顾问层语料**。Apple 安全响应顾问页英文镜像 **257 份**（取自 258 份，剔除 122085）。容错解析后全语料 **9,045 段 / 470 个不同段落名**；其中带字段段 7,346（Impact 与 Description 各 7,346）、块内含 CVE 编号的段 7,342、纯致谢段 1,699。**这三个数是按不同判据切的，不可相加**：9,045 = 7,346 + 1,699，而 7,346 − 7,342 = 4 段有字段却无 CVE 编号。
3. **切块三式（本篇最终采用）**。块首 `<h3 class="gb-header[^"]*"[^>]*>` 至其 `</h3>` 的文本 = 段落名；块尾 = 下一个同级 `<h3 class="gb-header`、`<div id="disclaimer"` 或 `<footer>`，三者取最先；块内取值 `<p class="(?:gb-paragraph|paragraph)[^"]*"[^>]*>`，实体解码后匹配，并排除以 `Available for` 开头的段。**初稿用属性完全相等（`class="gb-header"` / `class="gb-paragraph"`），该写法在标记带后缀的 10 页上命中 0 块且不报错**，得 7,615 段 / 439 名。
4. **两把刀的差逐页闭合**。9,045 − 7,615 = 1,430 段，全部落在标记带后缀的 10 页：149034=209、149035=314、149036=118、149037=128、149038=165、149039=13、149040=9、149041=103、149042=181、149043=190，相加 = 1,430 ✓。家族层面：容错 304 段 / 严格 239 段，漏 65 段（21.4%），且 `AppleFDEKeyStore`、`DeviceCheck`、`Local Authentication` 三个名在严格刀下**整个消失**（6.3）。
5. **探针纪律**。按**类名**选探针页，不得按页号大小或"最新"选：149034 必须得 209 段，命中 0 段即整轮作废。反例是 149226 / 149228 / 149229（`Released September 28, 2026`，页号更大却用旧代裸类名，各 1 段 `CoreGraphics`）。**类名换代与页面新旧、页号、`Released`、条目日期四者都不同步**（2.4、16.6）。
6. **CVE 归属与纯致谢**。致谢行形如 `CVE-xxxx-nnnnn: 姓名（机构）`；一段内多 CVE 时按页面原始顺序保留共现、不拆分。Impact/Description 先于致谢行出现时按紧邻其上的字段归属。无 Impact 且无 CVE 的块判为纯致谢段。
7. **条目备注检测**。在块内匹配 `Entry (added|updated|moved)`。149034 全页 209 块中 30 块含备注（`Entry added` 20、`Entry updated` 10）。**备注段不是条目级**：149034 的 `Security` 组有 3 条带 CVE 条目却只有 1 句备注，写在最后一条（CVE-2026-84508）之后；而 149036、149037、149041、149043 四页的 `Security` 组一句备注都没有（3.5）。因此任何"某条目未标注日期"的写法都不作数，只能写"该块内无备注段"。
8. **计数口径**。"次数"= 不分大小写的正则匹配次数；"段数"= 命中的块数；两者差值主要来自 Impact 与 Description 各写一遍。检索词一律先 `lc` 再 `\Q…\E`。**整词与词干必须分别报告**：`certificate authority` 整词 0 段而概念存在（16.2）；英文取样里 `authorit` 2 次而单数 `authority` 0 次（1.3）。
9. **分组与自检相加**。家族 = 本报告自建的 A/B/C 三组 19 名（6.1）：A 125 + B 90 + C 89 = **304** ✓；含 CVE 段 101 + 74 + 60 = **235** ✓；纯致谢 24 + 16 + 29 = **69** ✓；去重 CVE 25 + 23 + 17 = **65**（说明无 CVE 跨组，6.2）。
10. **验收断言集（复算必须先跑）**：全语料 = 9,045 段 / 470 名；探针 149034 = 209 段；`certificate` = 11 段 / 8 页 / 1 名；`improved certificate validation` = 11 段且语料级同为 11；`extended key usages` = 8 段 / 8 页 / 1 名；`trust` = 9 段 / 9 页 / 3 名且 `Security` 名下 0；`Apple is aware` = 93 段 / 65 页 / 12 名且家族 0；家族 = 304 段 / 19 名 / 65 CVE。任一项不符即停止引用该轮全部计数（16.1）。
11. **镜像时效**。顾问页会追加条目与备注，本语料为本期快照；第 14 章纪律要求凡引用页集合须同时给页级 `Released` 与页号清单。手册页发布日期取自各页转储的"发布日期："行，`sec93292bfa6`（术语表）无该行，本篇不推断其日期（附表 C、G15）。

# 附表 E 允许写 / 不许写对照（20 行）

| 想写的话 | 判定 | 依据与正确写法 |
|---|---|---|
| "Apple 手册写明无效的证书始终导致硬故障和无连接" | ✅ 可写 | 15.1 逐字；须带页 `sec100a75d12` 与发布日期 2026-01-28 |
| "TLS 证书信任状态的评估依照 RFC 5280，并整合 RFC 6962" | ✅ 可写 | 15.1「证书有效性验证」小节原句；这是文档承诺，不是缺陷证据 |
| "撤销清单由 Apple 信任的每个内建根 CA 及其从属 CA 签发者的 CRL 聚合而成" | ✅ 可写 | 15.1 逐字（12 章把它作为 T1 侧唯一链条描述） |
| "Apple 文档警告过中间 CA 被攻破的风险" | ❌ 不可写 | 15.1 只写清单来源包含从属 CA，未写其被攻破后的行为（G11）；15.5 是写给第三方后端的部署建议，两者不得连线 |
| "CVE-2026-86881 登记的是一条中间 CA 被攻破后可签发任意 EKU 的证书校验缺陷" | ✅ 可写（带限定） | 3.1 两句逐字；但须写成"顾问页将其归入 `Security` 段落名"，不得指定框架或函数（G1） |
| "这条证书校验缺陷位于 Security.framework" | ❌ 不可写 | 段落名 ≠ 实现层（16.7）；本期源码层为空（G1） |
| "CVE-2026-86889 只影响 Mac" | ❌ 不可写 | 只能写"只登记在 macOS 三页"；页分布不是适用范围（G4、11.2） |
| "本期语料里提到 certificate 的只有 11 段" | ✅ 可写 | 2.1、3.4；须带三个前置：容错解析、257 份镜像、9,045 段分母 |
| "顾问页本期不谈证书" | ❌ 不可写 | 这是严格解析器造出的静默 0，实际为 11 段（16.1） |
| "trust 词族说明 Apple 把信任模型缺陷登记在 `Security` 名下" | ❌ 不可写 | 8.1：全语料 `trust` 9 段无一在 `Security`；三种构词各绑 Safari Downloads / AppleMobileFileIntegrity / MobileBackup |
| "本家族 304 段内没有任何在野利用声明" | ✅ 可写 | 13.1、13.2；须同时给出 93 段 / 65 页 / 12 名的分母与"0 只覆盖 304 段" |
| "证书链校验方向不存在在野利用，风险较低" | ❌ 不可写 | 13.3 明令禁止；需要攻击成本估计与非公开信息，本篇无取数路径（G10） |
| "授权类缺陷的修法是 improved authorization checks" | ❌ 不可写 | 10.4：该句式家族 0、全语料 0；实际是 18 条五列出的五式，须分开引用 |
| "证书与授权在措辞层属于同一族" | ❌ 不可写 | 9.1：`certificate` 11 段与 `entitlement` 131 段同段共现 0 段；标题里的"中间授权"须带该限定 |
| "iOS 的授权范围由描述文件限制，这就是 provisioning entitlement 机制" | ⚠️ 只能带语境写 | 15.2 原句出自 macOS 语境的「信任缓存」页（2021-02-18）；T1 英文取样 20 页 `entitlement` 0 次（G3） |
| "iOS 的根证书存放在 System Roots 钥匙串" | ❌ 不可写 | 15.3 明确是 macOS 表述；iOS 侧对应文本两层都没有（G12） |
| "X.509 是 Apple 通用的证书格式口径" | ❌ 不可写 | 15.6「iOS 中的车钥匙安全性」是全量 179 页正文域唯一出现处；T2 全语料 0 段 |
| "顾问页有证书撤销（CRL / OCSP）相关条目" | ❌ 不可写 | 2.1：`CRL` / `OCSP` / `revocation` / `revoked` / `trust store` / `pinned` / `man-in-the-middle` 全 0 段 |
| "CVE-2026-84508 是 2026 年 10 月 19 日新增的条目" | ❌ 不可写 | 3.5：该日期是 `Security` 组唯一备注句的落点，且在另外 4 页整组无备注；只能写"该块内含 `Entry added October 19, 2026`" |
| "本报告 A/B/C 三组是 Apple 对证书与授权主题的官方分类" | ❌ 不可写 | 6.1 开篇即声明分组为本报告自建；换分组会改变多个 0（G5、16.5） |

---

**边界声明：** 本报告全部技术结论来自两层可核对的公开文本：《Apple 平台安全保护》官方指南（中文 179 页全量转储、英文 20 页取样正文）与 Apple 安全响应顾问页（257 份英文页面镜像，容错解析后全语料 9,045 段 / 470 个段落名，其中本篇证书与授权家族 304 段 / 19 个段落名 / 65 个 CVE 编号），逐条标注页号、字段原文与日期；手册层未说明的部分在第 19 章登记为缺口，不以推断填充。源码层本轮不可用，凡涉及证书校验实现位置的表述均已降级为"顾问页将其归入某段落名"。第 1、2、5、6、8、9、10、13 章的统计为本报告对公开文本的自建语料普查，属本报告的测量结果，不是 Apple 的表述；这些数字依赖本报告解析器对顾问页 HTML 标记的识别方式——本轮已因标记换代把证书族从严格口径的"0 段"更正为 11 段，并把家族分母从严格口径 239 段更正为容错口径 304 段（第 16.1、6.3），后续复算须先跑附表 D 第 10 项的验收断言集。所引 CVE 仅用于说明公开修复事实、检测与配置核查，不含利用代码、概念验证、证书链构造、伪造签发、中间人配置或绕过方法，也不含针对他人设备的任何操作步骤。所有第三方研究者姓名仅作顾问页致谢字段的引用，其研究结论本轮未核对，不构成本报告依据。如需就本报告任一结论索取取数脚本或段落级原始文本，或指出任何事实与措辞问题，请联系。

Telegram: https://t.me/one00190
