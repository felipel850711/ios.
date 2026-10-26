@@TITLE@@iOS 漏洞影响与攻击技术词汇手册
@@SUBTITLE@@从 Apple 官方术语表 60 条到顾问页 88 条「影响：」句式：中英对照、口径冲突、以及哪些攻击技术词在官方文本里一次都没有出现（三层文本实测版）
@@INFO@@报告日期|2026 年 10 月 26 日
@@INFO@@分析对象|《Apple 平台安全保护》zh-CN 指南 179 页正文区（345,840 字符）、Apple 安全响应顾问页 149041（iOS 26.7 / iPadOS 26.7，英文页与中文页各一份）、149226（iOS 26.7.1 / iPadOS 26.7.1，英文页与中文页各一份）
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含针对他人设备的操作步骤
@@INFO@@分类|术语与措辞 / 漏洞影响词汇 + 攻击技术词汇（中英对照手册）
@@INFO@@难度|入门到进阶（可作为整套报告的用词索引与写作规范）
@@INFO@@联系方式|Telegram: https://t.me/ios_one001
@@TOC@@

# 0 这份手册要回答什么

"iOS 漏洞影响"和"攻击技术"这两组词，在行业文本里通常被混在一起用：一条顾问条目里的 `Impact:` 句写的是**后果**（谁、在什么条件下、能造成什么），研究者写的却是**手法**（释放后使用、面向返回编程、沙盒逃逸），厂商手册写的又只是**防线名**（PAC、PPL、SPTM）。三种写法各自正确，但把它们当成同一个东西的三种说法，就会写出"Apple 文档说明了 0-click 利用链"这类根本不存在于任何官方文本里的句子。

本手册把这三套词汇分开重建，并且**所有计数都在 2026-10-26 重新测过**，不引用二手统计。它可以直接当整套报告的用词索引使用：写"影响"时查第 2 章，写"缺陷类型"时查第 3 章，写"攻击手法"时查第 4 章，做中英互译时查第 6 章与附表 D，检查自己有没有把研究层词汇冒充成官方口径时查附表 F。

## 0.1 本期实测到的四个数字，与它们各自的分母

| 数字 | 是什么 | 分母（口径） |
|---:|---|---|
| 60 | 《Apple 平台安全保护》官方术语表的条目数 | 页面 `sec93292bfa6` 的 `<dl>` 结构，按 `<dt id>` 计数 |
| 88 / 60 / 59 | 顾问页 149041 的「影响：」条目数 / 英文不重复句式数 / 中文不重复句式数 | 英文页与中文页各 88 条，按条目块解析后逐行对齐 |
| 25 | 措辞页里被**命名**出来的缺陷类型数 | 中文页 88 条描述句全部命中 `解决…问题` 句式，其中 77 次写出了缺陷名（去重 25 种），另 11 次只写「解决这个问题」 |
| 0 | 手册层 179 页正文里"零日""利用链""越狱""恶意制作的""远程攻击者"的命中数 | 中文词形精确检索，见附表 F |

四个数字来自四个不同的分母。把它们混成一句话（"Apple 官方文档有 60 个攻击术语"）就是错的——这是本手册最想说的一件事。

## 0.2 全篇的三条硬性约束

1. **只给定义，不给操作。** 第 4 章逐条解释攻击技术词汇时，写到"这类手法操纵的是什么对象、被哪道防线拦、在厂商措辞里落成哪种后果"为止，不写步骤、不写代码、不写针对他人设备的用法。
2. **每个计数标分母。** 措辞层的统计样本是**两个**顾问页文档（88 条 + 1 条），不是一万条 CVE。凡"Apple 从不使用 X 词"的断言，都限定为"在本次实测的手册层 179 页正文区与措辞层 2 页里为 0 次"。
3. **不把研究层词汇塞进引号冒充官方。** 手册层没有"利用链"这个词，就不能写"Apple 文档所述的利用链"。第 8 章的缺口清单把这类词逐条列出，并标注该去哪里取词、取到的词属于谁的口径。

## 0.3 结论摘要

1. 词汇分三层居住：**手册层给防线名，措辞层给后果名，研究层给手法名**。三层之间没有官方映射表，映射必须自己建（本手册第 5 章给了一份）。
2. 官方术语表 60 条里，**没有一条的定义对象是一种攻击手法**；8 条（13%）的定义文字里出现了"攻击/恶意/滥用/篡改/窃取"，且都是作为"这道防线为什么存在"的理由出现的。
3. 手册层 179 页正文里，**唯一被点名定义的攻击技术是面向返回编程（ROP）**，1 次 1 页，出现在"操作系统完整性"页的 PAC 小节里；紧随其后的一句是 PPL 的作用范围。
4. 措辞层的「影响：」句是一套**封闭句式**：`主体 + may + 后果`，或 `Processing a maliciously crafted X + may lead to + 后果`。88 条只用了 5 种情态框架（另有 2 条直接用裸 `may` + 动词）、18 种主体（合计 59 条）、15 种触发式主语（合计 29 条）、44 种后果尾串（`denial-of-service` 的连字符写法归一后为 43 种）。
5. 同一份顾问页的中英两版，**按条目对齐后 0 句英文对应多种中文译法**：中文措辞页的翻译是逐句一致的。真正的术语漂移分处两层——措辞层在英文自身（`sensitive user data` 8 次 vs `user-sensitive data` 1 次），手册层在中文全名（「设备固件升级 (DFU)」5 次 vs 「设备固件更新 (DFU)」2 次，见 6.2）。
6. 「拒绝服务」在手册层出现 1 次（macOS 防火墙页），在措辞层中文页出现 **0 次**——措辞页一律写「服务遭拒」5 次。同一个概念，手册层与措辞层用不同中文，跨层引用时必须注明是哪一层。
7. 措辞页的中文描述句有一个英文没有的排版惯例：**把缺陷类型名加弯引号**（`解决“释放后使用”问题`）。英文侧 `use after free` 3 次 + `use-after-free` 4 次 = 7 次，中文侧「释放后使用」7 次，两边条数严格相等，可与英文逐条对齐。
8. 有 11 条描述的中文句只写「解决这个问题」，不命名缺陷类型。**"Apple 说明了缺陷类型"这个预期，在同一页里有 11 条例外。**

---

# 1 词汇住在哪一层

## 1.1 手册层：只命名防线，不命名手法

《Apple 平台安全保护》的 179 页正文区（剔除页面导航与目录块后共 345,840 字符）里，安全机制名以「中文全名 (英文缩写)」的形式出现，中英对照抽取共命中 **264 对不重复组合、362 次、覆盖 109 页、涉及 175 个英文词**（附表 E 是未清洗的原始抽取，前置词粘连与截断伪影的说明见 6.3，正文只引用拆读后的规范名）。

其中与本题相关的机制名，出现次数与出现页都很低：

| 缩写 | 官方中文全名 | 正文区命中 | 命中页 |
|---|---|---:|---:|
| PAC | 指针认证代码 | 1 | 1（`sec8b776536b`） |
| ROP | 面向返回编程 | 1 | 1（`sec8b776536b`） |
| TXM | 可信执行监控器 | 1 | 1 |
| MIE | 内存完整性强制 | 1 | 1 |
| EMTE | 增强型内存标记扩展 | 1 | 1 |
| MTE | 内存标记扩展 | 1 | 1 |
| KIP | 内核完整性保护 | 2 | 2 |
| PPL | 页面保护层 | 3 | 2 |
| SPTM | 安全页表监控器 | 5 | 2 |
| SIP | 系统完整性保护 | 6 | 6 |
| ASLR | 地址空间布局随机化 | 4 | 2 |
| JIT | 即时（编译器） | 2 | 2 |
| AOT | 提前（转译） | 1 | 1 |

这张表要读作两件事。第一，**这些词在整份手册里都是稀有词**：PAC 全文只出现 1 次，比很多人预期的"Apple 大谈 PAC"少得多。第二，**缩写命中页远少于命中数**，说明同一页内反复使用，而跨页传播很弱——把 PAC 当成全手册的组织概念来写，会与文本实际结构不符。

## 1.2 手册层唯一一次直说攻击技术

原文（`sec8b776536b`，"操作系统完整性"页的"指针认证代码"小节）：

> 指针认证代码 (PAC) 用来防止对内存损坏错误的利用。系统软件和内建 App 使用……签名值储存在 64 位指针顶部未使用的填充位中。在使用签名前会进行验证，且填充会恢复以帮助确保指针地址正常工作。验证失败会导致中止使用签名。这种验证提高了许多攻击的难度，如试图通过操纵储存在堆栈中的函数返回地址来欺骗设备恶意执行现有代码的面向返回编程 (ROP) 攻击。

这一段是本手册引用最多的样本，因为它在一处里同时给出了：**防线名**（指针认证代码 / PAC）、**被防的对象**（内存损坏错误的利用）、**手法名**（面向返回编程 / ROP）、**手法的操作对象**（储存在堆栈中的函数返回地址）、以及**厂商对效果的措辞强度**（"提高了许多攻击的难度"，不是"阻止"）。研究层词汇表里"ROP"条目应该指向的就是这句话（见 4.3 与 5.1）。

同一页还有一句关于防线动机的表述：

> Apple 操作系统软件可协助减少攻击和对技术的恶意利用，无论攻击和恶意利用是来自恶意 App、网页还是通过任何其他渠道。

注意它把攻击渠道写成了"恶意 App、网页、任何其他渠道"——手册层的渠道词汇是**应用与网页**两分法，而不是研究层的"0-click / 钓鱼 / 水坑"三分法。这个差异会在 1.4 与第 8 章再出现。

## 1.3 措辞层：后果用固定句式给出

顾问页不描述防线，只描述某一条目的后果与修复。它的「影响：」字段是一套封闭句式（全量拆解见第 2 章），骨架只有两种：英文为 `<主体> + may / may be able to + <后果短语>`，以及 `Processing a maliciously crafted <输入> + may lead to / may result in + <后果短语>`；全量拆解见第 2 章。

也就是说：**措辞层的"漏洞影响词汇"= 主体表 × 情态表 × 后果表**。88 条条目用 60 种英文句式、59 种中文句式写完，这是本手册测到的核心结构。

## 1.4 研究层词汇在官方文本里的命中率

把 49 个常见的攻击技术词/后果词分别在三份语料里检索（手册层用中文词形，措辞层用中英两种词形），结果在附表 F。三行值得单独提出：

- **只在研究层存在**：零日 0/0/0、利用链 0/0/0、越狱 0/0/0、恶意制作（手册层 0，措辞层中英各 25）、TOCTOU 未命中。
- **只在措辞层存在**：释放后使用（手册层 0，措辞层中英各 7）、竞态条件（0/5/5）、整数溢出（0/3/3）、类型混淆（2/3/3）、空指针（0/1/1）、路径遍历（0/2/2）。
- **只在手册层存在**：代码签名 38、门禁 40、公证 26、配置描述文件 14、直接内存访问 13、指针认证 5、恶意软件 70、间谍软件 1；措辞层这些词的英文对应形为 0（除了 `bypass Gatekeeper` 1 次）。

这三行合起来是一句可执行的话：**同一个技术事实，选哪一层措辞，就决定了你能说多重。** 用研究层词（利用链、0-click）写作时不能加"据 Apple 文档"；用手册层词（代码签名、门禁）写作时不能加"这种攻击手法"，因为手册层没有手法名可引。

## 1.5 本篇不写的内容

- 不写任何手法的实现路径、参数选择、地址布局推断过程；不写 PoC 与代码。
- 不判定"某 CVE 属于某攻击链的一环"，除非该关系在厂商文本里有明文。
- 不给 CVE 与手法类型的强制映射（措辞页只有 25 种缺陷命名 + 11 条"解决这个问题"，覆盖率与归因都不足）。
- 不覆盖 macOS 专有条目（本期措辞层样本是 iOS/iPadOS 顾问页）。

---

# 2 漏洞影响词汇（措辞层实测）

## 2.1 样本说明

样本是顾问页 149041（iOS 26.7 / iPadOS 26.7）英文页与中文页各一份，以及 149226（iOS 26.7.1）各一份。前者 88 条「影响：」，后者 1 条。英文页正文区 `CVE-` 字串 92 次（条目数 88，差额来自一条目多编号与锚文本重复，本期未逐条核对）。88 条里，英文页与中文页**逐条对齐成功**：`影响：` 与 `Impact:` 各 88 条，顺序一致。

## 2.2 情态短语：may 的五种落法

| 英文情态短语 | 条数 | 中文对应写法 |
|---|---:|---|
| `may be able to` | 59 | 「或许能够」 |
| `may lead to` | 21 | 「可能会导致」 |
| `may result in` | 3 | 「可能会导致」（与 lead to 共用中文措辞） |
| `may cause` | 2 | 「可能会导致」 |
| `may allow an attacker to` | 1 | 「可能会允许攻击者」 |

另有 2 条用及物动词直接接后果，不含上述短语：`Processing a maliciously crafted image may corrupt process memory`、`Connecting to a malicious NFS server may disclose kernel memory`。这 2 条是"may + 动词"的第三种形态，中文分别写作「可能会损坏进程内存」「可能会泄露内核内存」。

**写作用法**：中文页把 `may lead to`/`may result in`/`may cause` 三种英文合并成「可能会导致」一类，把 `may be able to` 单独保留为「或许能够」。引用中文页时不要把「或许能够」升级成「能够」，也不要把「可能会导致」写成「导致」。

## 2.3 主体词汇：18 种"谁能做到"

英文页 88 条里，主语（不含触发式主语）共 18 种：

| 英文主体 | 条数 | 中文原样 |
|---|---:|---|
| An app | 40 | App 或许能够 |
| A sandboxed app | 2 | 沙盒化 App 或许能够 |
| A local attacker | 2 | 本地攻击者或许能够 |
| A local app | 1 | 本地 App 或许能够 |
| An attacker in a privileged network position | 1 | 拥有特权网络地位的攻击者 |
| A remote attacker | 1 | 远程攻击者 |
| An attacker in radio range | 1 | 无线电通信范围内的攻击者 |
| An archive | 1 | 归档文件或许能够 |
| An attacker with a paired Bluetooth device | 1 | 拥有已配对蓝牙设备的攻击者 |
| A sandboxed process | 1 | 沙盒化进程或许能够 |
| A malicious app | 1 | 恶意 App 或许能够 |
| An app with root privileges | 1 | 拥有根权限的 App |
| A local user | 1 | 本地用户或许能够 |
| An attacker with physical access to a trust-paired device | 1 | 能够实际操作受信任配对设备的攻击者 |
| An attacker with physical access to a locked device | 1 | 能够实际操作已锁定设备的攻击者 |
| An attacker with a compromised intermediate certificate authority | 1 | 入侵了中间证书颁发机构的攻击者 |
| A malicious application | 1 | 恶意应用程序或许能够 |
| An attacker in wireless proximity | 1 | （无线电/无线范围内的攻击者，与 radio range 并列的第二种写法） |

三张读法：

1. **主体分布极度偏斜。** `An app` 一种就占 40/88 ≈ 45%。把"iOS 漏洞的利用主体是攻击者"当默认叙述，与厂商措辞的实际分布相反。
2. **"攻击者"是有前缀限定词的。** 中英两版的影响句里各出现"攻击者/attacker" 11 次，除 `A remote attacker` 1 次以外，每一次都带位置或前置条件限定（网络地位、无线电范围、蓝牙配对、物理操作、已锁定设备、中间 CA 被入侵）。裸用"攻击者"不是官方措辞。
3. **两种近义写法共存**：`A malicious app` 与 `A malicious application`、`attacker in radio range` 与 `attacker in wireless proximity`。这是英文侧的写法漂移，不是翻译问题。

## 2.4 触发式主语：15 种"恶意制作的 X"

`恶意制作（maliciously crafted）` 在中英两版各出现 25 次，落在 15 种"输入 × 动作"组合上；这 15 种触发式主语合计 29 条（其余 2 条用 `malicious` 而非 `maliciously crafted`，1 条不带任何修饰词）。

| 英文触发式主语 | 条数 | 中文对应 |
|---|---:|---|
| Processing a maliciously crafted image | 6 | 处理恶意制作的图像 |
| Processing a maliciously crafted file | 4 | 处理恶意制作的文件 |
| Processing a maliciously crafted 3D model | 3 | 处理恶意制作的 3D 模型 |
| Processing a maliciously crafted video file | 2 | 处理恶意制作的视频文件 |
| Opening a maliciously crafted file | 2 | 打开恶意制作的文件 |
| Processing maliciously crafted web content | 2 | 处理恶意制作的网页内容 |
| Mounting a disk image with maliciously crafted files | 1 | 装载包含恶意制作文件的磁盘映像 |
| Processing a maliciously crafted media file | 1 | 处理恶意制作的媒体文件 |
| Processing web content | 1 | 处理网页内容（此条无"恶意制作"） |
| Mounting a maliciously crafted volume | 1 | 装载恶意制作的宗卷 |
| Extracting a maliciously crafted archive | 1 | 提取恶意制作的归档文件 |
| Processing a maliciously crafted font file | 1 | 处理恶意制作的字体文件 |
| Connecting to a malicious NFS server | 2 | 连接到恶意 NFS 服务器（中文无"制作"字样） |
| Connecting a malicious accessory | 1 | 连接恶意配件 |
| Processing a maliciously crafted 3D scene | 1 | 处理恶意制作的 3D 场景 |

词汇要点：**动作只有五个**——处理 / 打开 / 装载 / 提取 / 连接（英文 Processing / Opening / Mounting / Extracting / Connecting），**输入类型集中在媒体与文件容器**——图像、文件、3D 模型、视频、媒体、字体、归档、宗卷、磁盘映像、网页内容。写作时把"解析器攻击面"说清楚，靠的就是这张表的两个维度，而不是形容词。

## 2.5 后果词汇：44 种尾串写法，归为 7 类

**口径**：把每条 Impact 去掉「主体 + 情态框架动词」之后剩下的部分叫**后果尾串**。88 条共得到 44 种不重复尾串；其中 `a denial-of-service` 与 `a denial of service` 只差一个连字符，归一后即 43 种。下面按尾串归类，每条尾串只进一类，**七类合计 44 种尾串 / 88 条**，无遗漏、无重复计数（组合句 `… or …` 按整条尾串归类，不拆成两个后果）。

**A 代码执行与任意写入类**（6 种尾串 / 6 条）：`execute arbitrary code with kernel privileges`（1）、`execute arbitrary code`（1）、`cause unexpected app termination or arbitrary code execution`（1）、`an attacker to write arbitrary files`（1，来自 `may allow an attacker to write arbitrary files`）、`read and write arbitrary files`（1）、`issue certificates with arbitrary extended key usages`（1）。特征词是 **arbitrary（任意）**，88 条 Impact 行里共 6 次。

**B 内存损坏类**（6 种 / 15 条）：`cause unexpected system termination or corrupt kernel memory`（5）、`memory corruption`（5）、`unexpected app termination or corrupt process memory`（2）、`corrupt process memory`（1）、`kernel memory corruption`（1）、`cause unexpected system termination or write kernel memory`（1）。中文对应：内存损坏 / 损坏进程内存 / 损坏内核内存 / 内核内存损坏 / 向内核内存写入数据。

**C 信息泄露类**（14 种 / 23 条）：`access sensitive user data`（8）、`disclosure of process memory`（2）、`disclose kernel memory`（2）、`unexpected process termination or disclose process memory`（1）、`read uninitialized kernel memory`（1）、`cause unexpected system termination or read kernel memory`（1）、`determine kernel memory layout`（1）、`access user-sensitive data`（1）、`read a persistent account identifier`（1）、`read persistent device identifiers`（1）、`access motion data from headphones without user consent`（1）、`identify what other apps a user has installed`（1）、`determine a user's current location`（1）、`view sensitive user information`（1）。中文对应动词：泄露 / 读取 / 访问 / 确定 / 发现 / 查看。

**D 终止与拒绝服务类**（8 种 / 33 条）：`cause unexpected system termination`（15）、`unexpected app termination`（6）、`unexpected process termination`（4）、`unexpected system termination`（3）、`cause a denial-of-service`（2）、`cause a denial of service`（1）、`a denial-of-service`（1）、`an unexpected Safari crash`（1）。中文对应：导致系统意外终止 / App 意外终止 / 进程意外终止 / 服务遭拒 / Safari 意外崩溃。

**E 权限与绕过类**（4 种 / 4 条）：`gain root privileges`（1）、`circumvent sandbox restrictions`（1）、`bypass Gatekeeper`（1）、`bypass certain Privacy preferences`（1）。中文对应：获得根权限 / 绕过沙盒限制 / 绕过"门禁" / 绕过某些"隐私"偏好设置。

**F 文件与系统修改类**（4 种 / 5 条）：`modify protected parts of the file system`（2）、`modify protected system files`（1）、`overwrite restricted files`（1）、`access restricted files`（1）。中文对应：修改文件系统的受保护部分 / 修改受保护的系统文件 / 覆盖受限文件 / 访问受限制的文件。

**G 追踪与标识类**（2 种 / 2 条）：`fingerprint the device`（1）、`track users across apps and websites without permission`（1）。中文对应：对设备创建独一无二的描述信息 / 在未经授权的条件下跨 App 与网站跟踪用户。

**注意 B 与 D 的边界**：`cause unexpected system termination`（15 条纯终止句）归 D；一旦同一尾串里带上 `or corrupt kernel memory` / `or read kernel memory` / `or write kernel memory`（5+1+1 条），后果就从"可用性"升级到"内核内存被读或被写"，归 B。写报告时这条边界必须明说：**"导致系统意外终止"和"导致系统意外终止或损坏内核内存"不是同一种影响，后者才是内核读写原语。**

## 2.6 后果词汇中英对照主表

| 英文后果短语 | 中文原样 | 类别 |
|---|---|---|
| cause unexpected system termination | 导致系统意外终止 | D |
| unexpected app termination | App 意外终止 | D |
| unexpected process termination | 进程意外终止 | D |
| corrupt kernel memory | 损坏内核内存 | B |
| write kernel memory | 向内核内存写入数据 | B |
| corrupt process memory | 损坏进程内存 | B |
| lead to memory corruption | 导致内存损坏 | B |
| result in disclosure of process memory | 导致进程内存泄露 | C |
| disclose kernel memory | 泄露内核内存 | C |
| read uninitialized kernel memory | 读取未初始化的内核内存 | C |
| determine kernel memory layout | 确定内核内存布局 | C |
| access sensitive user data | 访问敏感的用户数据 | C |
| access user-sensitive data | 访问用户敏感数据 | C |
| view sensitive user information | 查看敏感用户信息 | C |
| read persistent device identifiers | 读取永久性设备标识符 | C |
| read a persistent account identifier | 读取永久性账户标识符 | C |
| execute arbitrary code | 执行任意代码 | A |
| execute arbitrary code with kernel privileges | 以内核权限执行任意代码 | A |
| lead to arbitrary code execution | 导致任意代码执行 | A |
| write arbitrary files | 写入任意文件 | A |
| gain root privileges | 获得根权限 | E |
| circumvent sandbox restrictions | 绕过沙盒限制 | E |
| bypass Gatekeeper | 绕过"门禁" | E |
| cause a denial-of-service | 导致服务遭拒 | D |
| modify protected parts of the file system | 修改文件系统的受保护部分 | F |
| overwrite restricted files | 覆盖受限文件 | F |
| fingerprint the device | 对设备创建独一无二的描述信息 | G |
| track users across apps and websites | 跨 App 与网站跟踪用户 | G |

## 2.7 后果的"三级对象"：系统 / App / 进程

同一句"意外终止"在本页落在三个不同对象上，次数与中文分别是：系统 13（导致系统意外终止）、App 6（App 意外终止）、进程 4（进程意外终止），另有组合句把终止与内存损坏并列（4 条 + 1 条 + 1 条）。中文页对三级对象**没有合并**：「系统/App/进程 + 意外终止」严格对应英文三级。

这条是本手册里最实用的一条：**写"崩溃"时必须写出层级。**「导致崩溃」不区分内核 panic、App 退出与进程被终止，而厂商措辞区分，且这三者在影响面上是不同量级（内核终止 → 整机重启；App 终止 → 单应用；进程终止 → 可能是沙盒内辅助进程）。附表 F 里"崩溃"手册层 6 次、措辞页中英各 1 次，也印证"崩溃"不是措辞层的标准词，标准词是"意外终止"。

## 2.8 「服务遭拒」与「拒绝服务」：一个跨层分裂

- 手册层：「拒绝服务」1 次，出现在 macOS 防火墙页 `seca0e83763f`——"以保护 Mac 免受网络访问攻击和拒绝服务攻击"。
- 措辞层中文页：「拒绝服务」0 次，「服务遭拒」5 次；对应英文 `denial-of-service` 4 次 + `denial of service` 1 次 = 5 次，条数严格相等。

结论：**同一个英文术语，在 Apple 自己的两份中文文本里是两种写法。** 跨层引用时必须写"顾问页中文措辞用「服务遭拒」"或"指南原文用「拒绝服务」"，否则读者会以为有两个不同概念。

---

# 3 缺陷类型词汇与修复手段词汇

## 3.1 Description 句的骨架

英文页 88 条 `Description:` 的骨架是 `A/This <缺陷类型> issue was addressed with/by <修复手段>.`

其中缺陷类型可省略，省略后写作 `This issue was addressed with improved …`。中文页对应骨架是 `通过改进<手段>解决<缺陷类型>问题。`

一个可核对的结构差异：**中文页把缺陷类型写进句子，且给它加弯引号**（当该词是外来术语时）。英文页 7 次 `use after free`/`use-after-free`，中文页 7 次「释放后使用」全部带引号（`解决“释放后使用”问题`）。这一条常被误当成"中文页信息更多"，其实是同一份信息的两种排版。

## 3.2 25 种被命名的缺陷类型

从中文页 88 条描述句里抽出 `解决…问题` 的宾语：该句式命中 88 次，其中 11 次只写"解决这个问题"（不命名缺陷类型），其余 77 次命名了缺陷，去重得 25 种。完整频次与同位置英文句配对见附表 C。前几名：

| 中文缺陷类型 | 条数 | 常见英文手段短语 |
|---|---:|---|
| 越界写入 | 14 | improved bounds checking |
| （不命名，只写"这个问题"） | 11 | 各种 |
| 权限 | 7 | additional restrictions / improved path validation |
| 授权 | 6 | improved access control / improved state management |
| 竞态条件 | 5 | improved state handling / improved state management |
| 越界读取 | 4 | improved bounds checking / improved input validation |
| 整数溢出 | 3 | improved input validation |
| 类型混淆 | 3 | improved memory handling / improved checks |
| 隐私 | 3 | improved logging / removing sensitive data |
| 逻辑 | 2 | improved validation / improved checks |
| 堆缓冲区溢出 | 2 | improved bounds checking |
| 缓冲区溢出 | 2 | improved memory handling |
| 路径处理 | 2 | improved path validation / improved validation |
| 内存损坏 | 2 | improved memory handling |
| 内存初始化 | 2 | improved memory initialization |
| 信息泄露 | 2 | improved memory management / improved state management |
| 双重释放 | 2 | improved memory management |
| 未初始化的内存 / 空指针解引用 / 堆栈溢出 / 路径遍历 / 证书验证 / 服务遭拒 / 输入验证 / 文件隔离功能被绕过 | 各 1 | 各 1–2 种手段 |

三点解释：

1. 「越界写入 + 越界读取」= 18 次，正好等于英文 `out-of-bounds` 的 18 次。这是中英对齐质量的独立校验点。
2. 「权限」与「授权」在中文页是两个词（7 次、6 次），英文侧分别是 `entitlement`（授权，2 次命中短语）与 permission/access control。中文页里「访问敏感的用户数据」8 次 vs 「访问用户敏感数据」1 次，也来自英文侧 `sensitive user data` 8 次 vs `user-sensitive data` 1 次的**英文**漂移，不是翻译不一致。
3. 11 次"解决这个问题"是**信息缺口**，不是措辞松。写"该条目的缺陷类型未公开"时应引这类条目为证据。

## 3.3 20 种修复手段

英文侧修复短语共 20 种，去重后前几名：`improved bounds checking` 19、`improved memory handling` 11、`improved input validation` 10、`improved memory management` 10、`improved state management` 6、`additional restrictions` 5、`improved validation` 5、`improved checks` 4、`improved state handling` 4、`improved path validation` 2、`improved access control` 2、`additional entitlement checks` 2，其余 8 种各 1 次（含 `removing sensitive data`、`improved certificate validation`、`improved private data redaction for log entries`）。全表与中文对应见附表 B。

词汇要点：**这些短语都是"手段类"名词，不是"防线名"。** `improved bounds checking` 不是新增一道防线，而是把某个已有校验补严。把它们写成"Apple 启用了 PPL/SPTM 来修这个洞"是常见的层次误用（防线名来自手册层，修复手段来自措辞层，两者在本期样本里从不互换）。

## 3.4 同义词串：手段名与缺陷名容易混

`improved bounds checking`（边界检查）与 `越界写入问题`（缺陷）在中文页里同句出现，容易被当同一个词。区分办法是看句法位置：手段总在"通过改进 ___"里，缺陷总在"解决 ___ 问题"里。这也是附表 B 与附表 C 分列两张表的原因。

---

# 4 攻击技术词汇（研究层，逐条释义）

以下每条给四项：**中文 / English / 一句定义 / 官方文本命中情况**（M = 手册层 179 页正文区，A = 措辞页 149041 中英合计），以及一个易混点。命中数取自附表 F 与第 1 章表。定义一律写到"操纵什么对象、由哪道防线拦、在厂商措辞里落成哪种后果"为止。

## 4.1 内存破坏类

- **越界读取 out-of-bounds read**：读到缓冲区边界之外的内存。A 侧 4 条中文命名「越界读取」、M 侧 0 次。落点后果通常是 `disclosure of process memory` / `read kernel memory`。易混：越界读取是**缺陷类型**，信息泄露是**后果**，两者在措辞页分属两个字段。
- **越界写入 out-of-bounds write**：向缓冲区边界之外写。A 侧 14 条中文命名「越界写入」，是本页最大一类；修复措辞几乎固定为「通过改进边界检查」。易混：「向内核内存写入数据」是后果句，不是缺陷名。
- **缓冲区溢出 buffer overflow / 堆缓冲区溢出 heap buffer overflow / 栈溢出 stack overflow**：越界写入的两个按内存区域细化的子类。A 侧 4 / 2 / 1 条命名。M 侧「缓冲区溢出」1 次。易混：堆/栈是内存区域标签，不是严重性标签。
- **释放后使用 use after free（UAF）**：对象已被释放，指针仍被继续使用。A 侧中英各 7 次（中文全带引号）；**M 侧 0 次**——手册层从不使用这个词。修复措辞常为「通过改进内存管理」。易混：UAF 是生命周期错误，与"悬垂指针"是同一族的不同表述。
- **双重释放 double free**：同一块内存被释放两次，破坏分配器状态。A 侧 2 次命名「双重释放」；M 侧 1 次。
- **类型混淆 type confusion**：把某类型的对象当作另一类型使用。A 侧 3 次；M 侧 2 次（手册层在 ObjC/Swift 运行时语境里出现过）。修复措辞多为「通过改进内存处理 / 改进检查」。
- **整数溢出 integer overflow**：算术结果超出可表示范围，导致长度/偏移量被误用。A 侧 3 次命名；M 侧 0 次。它本身通常不直接读写内存，而是把越界写入的"长度"造出来。
- **未初始化内存 uninitialized memory / 内存初始化 memory initialization**：读取或依赖未被写入过的内存内容。A 侧各 2 次命名；后果落成 `read uninitialized kernel memory`。
- **空指针解引用 null pointer dereference**：以空地址为对象引用。A 侧 1 次命名「空指针解引用」；后果常为终止而非执行。
- **内存损坏 memory corruption**：泛指内存内容被非预期改写。A 侧 8 次（英文）/ 8 次（中文）；M 侧 4 次——这是**两层都用过**的少数词之一，PAC 那句原文即用它。

## 4.2 并发与时序类

- **竞态条件 race condition**：结果依赖多个操作的相对时序。A 侧 5 次命名「竞态条件」；M 侧 0 次。修复措辞集中在「改进状态处理 / 状态管理」。
- **检查与使用之间窗口 time-of-check to time-of-use（TOCTOU）**：竞态条件在"校验后、使用前被改"这一具体形态。**本次三个语料里均未命中**该词形。易混：厂商措辞把它写成「竞态条件」，不写 TOCTOU。
- **状态管理缺陷 state management**：措辞页里的"手段型"表达（`improved state management` 6 次 / `improved state handling` 4 次），常对应研究层的 TOCTOU 与竞态两类。它是修复动作的类别，不是缺陷类型。

## 4.3 代码重用与执行类

- **面向返回编程 return-oriented programming（ROP）**：用已有代码片段（以返回指令结尾）串成新执行序列。M 侧 1 次、且是手册层唯一被定义的手法（见 1.2 原句）；A 侧 0 次。被 PAC 与 PPL 类防线拦。易混：ROP 不注入新代码，所以"代码签名"不是它的主要对手，指针与页表完整性才是。
- **面向跳转 / 面向调用编程 JOP / COP**：与 ROP 同族，用间接跳转或调用串联片段。**本次三个语料均未命中。**
- **代码重用攻击 code-reuse attack**：ROP/JOP 等的上位类名。**本次未命中**；手册层用"欺骗设备恶意执行现有代码"这句描述性语言承担同一概念——写作时可直接引这句，比造一个官方没有的术语更安全。
- **即时编译利用 JIT / just-in-time compilation**：把可写内存页变成可执行（W^X 例外通道）。M 侧「即时 (JIT)」2 次 2 页，其中"运行时进程安全性"页明文写到 Safari 对 JIT 编译器使用随机地址的内存页；A 侧 0 次。
- **提前转译 AOT / ahead-of-time**：M 侧 1 次（Rosetta 2 页的 x86_64 代码哈希与修复策略语境）。与 JIT 的区别：AOT 在生产前完成，产物按普通二进制受签名与页权限约束。
- **执行非预期代码 execution of unexpected code**：整套报告的章节标题用词之一。它对应措辞页的 `execute arbitrary code` / `arbitrary code execution`（本页 3 次）与手册层的"阻止常见类型的恶意利用"。

## 4.4 内核、权限与沙盒类

- **内核内存读 / 写 kernel memory read / write**：越界等缺陷被用于访问内核地址空间。A 侧「内核内存」12 次（中英各 12），是内核类后果的最高频词。M 侧「内核内存」3 次，均在"防线为什么存在"的语境。
- **修改敏感内核状态 modify sensitive kernel state**：措辞层对"内核里的值被改"的固定说法（本套报告 #102 专篇已重建）。本期样本里落成 `write kernel memory` / `corrupt kernel memory`。
- **确定内核内存布局 kernel memory layout**：`determine kernel memory layout` 1 条，中文「确定内核内存布局」。这是**信息泄露而非控制转移**，写作时不要升格为"已获得内核代码执行"。
- **沙盒逃逸 sandbox escape / circumvent sandbox restrictions**：越过文件与网络访问边界。A 侧「沙盒」4 次，中文「绕过沙盒限制」1 次；M 侧「沙盒」27 次（但全部在防线描述里，不含"逃逸"）。易混：越狱（jailbreak）在本期三语料 **0 次**，不要把两者当同义。
- **授权滥用 entitlement abuse**：取得或冒用超出必要的授权。A 侧 `entitlement` 2 次（`additional entitlement checks`）；M 侧「授权」174 次，但都是"声明的授权"这一机制语境，不含滥用。
- **提权 privilege escalation / gain root privileges**：A 侧中文「获得根权限」1 次，英文 `privilege escalation` 0 次、`root privileges` 2 次。**结论：措辞层不说"提权"，说"获得根权限/以内核权限执行任意代码"。** M 侧「提升权限」1 次，出自运行时进程安全性页谈授权降低"遭入侵的系统 App 或监控程序提升权限的可能性"。
- **绕过门禁 bypass Gatekeeper**：A 侧 1 条中文写作「绕过"门禁"」（同样加引号）；M 侧「门禁」40 次，是手册层最高频防线名之一。
- **证书链滥用 compromised intermediate certificate authority**：A 侧 1 条主体即"入侵了中间证书颁发机构的攻击者"，后果为「颁发具有任意扩展密钥用途的证书」。

## 4.5 投递与触发通道类

- **恶意制作的输入 maliciously crafted input**：措辞层的官方上位词，A 侧 25 次；M 侧 0 次。具体子类见 2.4 的 15 种。
- **网页内容 web content**：A 侧 3 次（含 `Processing maliciously crafted web content`）；M 侧「网页内容」0 次，手册层的对应写法是"来自……网页"这类句式。
- **磁盘映像 / 宗卷装载 disk image / volume mounting**：A 侧 2 条动作是 Mounting。这是一个易被忽略的触发面：不执行代码，只装载即可导致终止或写入。
- **归档提取 archive extraction**：A 侧 2 条（`An archive may be able to bypass Gatekeeper`、`Extracting a maliciously crafted archive may allow an attacker to write arbitrary files`）。缺陷词汇对应「路径遍历」1 次与「文件隔离功能被绕过」1 次。
- **NFS 服务器 malicious NFS server**：A 侧 2 条，分别落成「泄露内核内存」与「内核内存损坏」。语言层面属于"连接到不受信任的远端服务"这一类触发面。
- **配件 malicious accessory**：A 侧 1 条「连接恶意配件可能会导致系统意外终止」；M 侧配件相关页面把验证机制写成 iAP/HAP/证书链。
- **蓝牙与近距离无线电**：A 侧主体 3 种（已配对蓝牙设备、无线电通信范围内、无线范围内）。M 侧相关机制词为 BLE、UWB、BR/EDR。
- **物理访问 physical access**：A 侧 2 种主体（可操作受信任配对设备 / 可操作已锁定设备）。后果分别写成「读取和写入任意文件」与「查看敏感用户信息」。
- **零点击 zero-click / 一键 click**：三语料 **0 次**。渠道词汇只能引手册层的"恶意 App、网页、任何其他渠道"（见 1.2）或明确标注为研究层用词。
- **水坑攻击 watering hole**、**钓鱼 phishing**、**社会工程 social engineering**：本套报告已有专篇，但在本期实测的三份语料里**均 0 次**（此前对手册层的词形测试同样为 0）。

## 4.6 硬件、侧信道与物理类

- **直接内存访问 DMA**：外设绕过 CPU 直接读写主存。M 侧 13 次，是本类最高频；防线词为 IOMMU（3 次）、DMA 保护页。A 侧 0 次。
- **输入/输出内存管理单元 IOMMU**：限制外设可访问的地址范围。见 6.2 的写法冲突。
- **联合测试行动小组 JTAG**、**通用异步收发器 UART**：调试接口。M 侧各命中，术语表收录 JTAG 一条；A 侧 0。
- **侧信道 side channel / 时序 timing**：M 侧「侧信道」2 次、「时序」4 次，多出现在安全隔区页对功耗与时序攻击的防范表述里（含 SPA/DPA 两个缩写）。A 侧 0 次。
- **静态/动态功耗分析 SPA / DPA**：M 侧各 1 次，出现在 AES 引擎语境（"防范通过时序和静态功耗分析……还包括动态功耗分析"）。
- **可擦除存储器 / 唯一 ID (UID) / 组 ID (GID)**：术语表收录，属于密钥层级的硬件根。它们出现在"攻击者实际占有设备"这类前提句里（可擦除存储器条目明文写了该前提下的局限）。

## 4.7 利用工程与产业链类

以下 9 个词是研究/产业层词汇，**在本次实测的三份官方文本里全部 0 次**。本手册给出定义与用法边界，不给它们加"官方称"：

- **零日 zero-day**：修复尚未发布或尚未安装时被使用的漏洞。官方对应表述是安全响应顾问页的存在本身与「后台安全改进」类机制词。
- **利用链 exploit chain**：多个缺陷串起来完成从入口到目标的全过程。官方三层文本都没有对应名词，最接近的是手册层的"安全启动链"（不同概念）。
- **利用套件 exploit kit / 商业化利用套件**：把利用链工程化交付的产品形态。本套报告 #35–#37 有专篇，来源为研究者披露与司法/新闻文本，不是厂商措辞。
- **商业间谍软件 commercial spyware**：M 侧「间谍软件」1 次；A 侧 0 次。
- **持久化 persistence**：A 侧 2 次均为 `persistent account identifier` / `persistent device identifiers`（标识符语义，不是驻留语义）；「持久化」这一用法三语料 0 次。
- **无文件攻击 fileless**：三语料 0 次。本套报告 #39 专篇用"内存中执行"等描述性语言承担该概念。
- **遥测 / 数据外泄 telemetry / exfiltration**：M 侧「遥测」2 次（诊断与度量的官方语境），不含攻击语义；A 侧 0 次。
- **侧载 sideload**：M 侧 2 次，出现在 App Store 与开发者套件语境；A 侧 0 次。
- **在野利用 actively exploited**：本期两条顾问页里 `Apple is aware`、`already been actively exploited`、`reported to Apple` **均为 0 次**。这类声明存在，但不在本期样本里——引用"在野"结论要另找带该声明的页面，不能引本页。

## 4.8 观测与后果描述类

- **意外终止 unexpected termination**：措辞层标准词，A 侧 39 次；分系统 / App / 进程三级（见 2.7）。
- **崩溃 crash**：A 侧 1 次（`an unexpected Safari crash`），M 侧 6 次。"崩溃"是可观测面用词，不是措辞层影响用词。
- **服务遭拒 / 拒绝服务**：见 2.8 的跨层分裂。
- **信息泄露 disclosure**：A 侧英文 8 次、中文 2 次命名「信息泄露」，另有「泄露」7 次散在后果句中。
- **绕过 bypass / 规避 circumvent**：A 侧两种动词并存（`bypass` 用于门禁与隐私偏好，`circumvent` 用于沙盒限制）。这个动词选择是有信息的：**沙盒用 circumvent，防线开关用 bypass**，中文都是"绕过"，需要在引用时保留英文原形才能区分。

---

# 5 防线名 ↔ 手法名 ↔ 后果词的三层映射

官方从不提供这张表，本节按实测文本自建。读法：某条 CVE 的 `Impact:` 句给出"后果词"，研究层给出"手法名"，手册层给出"被加强/被指名的防线"。**只有前两列在同一页文本里能对上，第三列是分析结论，不能引成厂商口径。**

| 手册层防线名（中英） | 对应手法名（研究层） | 措辞层后果词 |
|---|---|---|
| 地址空间布局随机化 (ASLR) | 硬编码地址、代码重用 | `determine kernel memory layout` |
| 指针认证代码 (PAC) | ROP/JOP、指针篡改 | `corrupt kernel memory`、`execute arbitrary code` |
| 内核完整性保护 (KIP) | 内核代码改写 | `write kernel memory` |
| 页面保护层 (PPL) | 签名后改写用户空间代码 | `bypass Gatekeeper`（相邻，非同一条） |
| 安全页表监控器 (SPTM) / 可信执行监控器 (TXM) | 页表篡改 | `modify sensitive kernel state` 类 |
| 内存完整性强制 (MIE) / 增强型内存标记扩展 (EMTE) | 越界读写、UAF | `corrupt process memory`、`memory corruption` |
| 输入/输出内存管理单元 (IOMMU) / DMA 保护 | DMA 攻击 | （A 侧无对应条目） |
| 代码签名与信任缓存 | 未签名代码执行 | `execute arbitrary code with kernel privileges` |
| 沙盒 (sandbox) | 沙盒逃逸 | `circumvent sandbox restrictions` |
| 声明的授权 (entitlement) | 授权滥用 | `access restricted files` |
| 数据保护与密钥层级 (SKP、UID、媒介密钥) | 离线解密、取证提取 | `view sensitive user information` |
| 门禁 (Gatekeeper) 与公证 (notary) | 绕过来源校验 | `bypass Gatekeeper` |
| 锁定模式 (Lockdown Mode) | 扩大攻击面的一条配置通道 | （A 侧无对应条目） |
| BlastDoor（消息图形扩展，M 侧 0 次） | 投递前沙箱化 | （本期无样本） |
| 强制访问控制 (MAC) | 策略绕过 | `modify protected parts of the file system` |

第 1.2 节那句 PAC 原文，就是这张表第 2 行的官方出处——手册层在同一句里同时给出防线名（PAC）与手法名（ROP），这类"一句跨两层"的原文总共有几处，是判断一段叙述能否引成厂商口径的最快检验。

---

# 6 中英对照与口径冲突

## 6.1 官方术语表 60 条的性质

60 条里，机制/组件/密钥类占绝大多数（安全隔区、sepOS、iBoot、Boot ROM、BPR、SCIP、密钥包、文件独有密钥、数据保险箱、Tangling、xART…），服务类若干（IDS、APNs、设备管理服务、Apple 商务管理、Apple 校园教务管理），标准/算法类若干（AES、AES-XTS、ECDSA、HMAC、URI、IC、JTAG、UEFI、IOMMU、DMA、eSPI），另有 XNU、XProtect、门禁、恢复模式、DFU 模式。

- **8 条（13%）的定义文字里出现"攻击/恶意/滥用/篡改/窃取"**：安全储存组件、地址空间布局随机化 (ASLR)、可擦除存储器、唯一 ID (UID)、硬件安全模块 (HSM)、Boot ROM、Tangling、XProtect。
- **0 条的定义对象是攻击手法。** ASLR 条目里那句"使攻击代码无法对这些值进行硬编码"是全套术语表里最接近手法描述的一句话，而它的主语仍是防线。

全表带条目锚点与定义原文见附表 D。

## 6.2 六组实测口径冲突

每组都给原句与页锚点，都可复查。

1. **SKP 两种中文全名**：「密封密钥保护 (SKP)」9 次（含术语表条目）；「封装密钥保护 (SKP)」1 次（`secc745a0845`，"对所有者身份密钥 (OIK) 的访问权限称为'所有权'……OIK 使用封装密钥保护 (SKP) 中所述的相同密钥层级进行保护"）。同一缩写、两种全名，且指向同一段机制。另外术语表还单列一条「密钥封装」，与正文里"AES 密钥封装""媒介密钥封装"的动词用法相邻，三者易混。
2. **IOMMU 差一个斜杠**：「输入/输出内存管理单元 (IOMMU)」3 次 3 页——术语表 `sec93292bfa6`、`sec8a67fa93d`、`seca4960c2b5`；「输入输出内存管理单元 (IOMMU)」（无斜杠）1 次，在「安全隔区」页 `sec59b0b31ff`。术语表自身的定义还是循环式的（条目名即定义首句）。
3. **DFU 术语表内部就不一致**：「设备固件升级 (DFU) 模式」5 次 4 页（含术语表 `sec93292bfa6`），「设备固件更新 (DFU)」2 次 2 页——其一就在术语表页内部的「启动进程寄存器 (BPR)」定义里（"跟踪设备进入的启动模式，如设备固件更新 (DFU) 模式和恢复模式"），另一在「安全隔区」页 `sec59b0b31ff`。
4. **IdP 提供商 / 供应商**：「身份提供商 (IdP)」2 次——「单点登录安全性」页 `sec0c87ccc6d` 与「iPadOS 中的"共享 iPad"安全性」页 `secd99f373ef`；「身份供应商 (IdP)」1 次——「自动设备注册」页 `secc2cd563ef`。同一缩写，两种中文全名分别落在不同页，引用时必须连页锚点一起写。
5. **STS 两个不同展开**：「相互认证的站对站 (STS)」（`sec6ab47ebfc`，对应 WPA3 的 Simultaneous Authentication of Equals）与「作为端对端 (STS) 协议的一部分」（`sec70a4f377d`，MFi-SAP 配件语境）。同一缩写、两个完全不同的英文全称，是典型"缩写不能单独引用"的例子。
6. **MAC 三个含义**：「强制访问控制 (MAC)」（`sec3ad8e6e53`）、"通过 HMAC-SHA-256 来计算信息认证代码 (MAC)"（`secea5f2e977`）、以及无线局域网语境里的 Media Access Control（本期该词形未命中，故本手册不主张第三种）。同一三字母缩写在同一份手册里承载两个不同术语。

## 6.3 自动抽取不能直接当术语表用

264 对中英对照是正则抽取的结果，抽取里有两个必然伪影，拆读校正后才能当作术语名引用：

- **前置词粘连**：正则的中文头会吞掉连接词，于是出现「则攻击者可能在其中关闭系统完整性保护 (SIP)」「骗设备恶意执行现有代码的面向返回编程 (ROP)」「是该账户由手动创建或已与学生信息系统 (SIS)」。这些不是术语名。
- **斜杠与括号截断**：「输入/输出内存管理单元」被切成「输出内存管理单元」；「即时 (JIT)」被切成「即时」。附表 E 里凡带"(1)"的孤立变体，多数属于这一类。

**口径**：本手册只把去粘连后的规范名写进第 1 章与第 5 章表格，抽取全量放在附表 E 并注明它是自动产物。

## 6.4 措辞层的中英对齐质量：0 处漂移

把英文页与中文页按条目块解析、逐条配对（88 对 88），再统计"同一英文句子对应几种中文句子"：

- 英文 60 种句式、中文 59 种句式；
- **0 种英文句式被翻译成多种中文句式**；
- 唯一一次中文侧多出的写法是「App 或许能够访问敏感的用户数据」与「App 或许能够访问用户敏感数据」，其英文同样是两种写法（`sensitive user data` 8 / `user-sensitive data` 1），因此**不算翻译漂移**。

这个结论的实际用途：可以把中英两份顾问页当同一份语料的双视图引用，不需要担心引到的中文句在英文里没有唯一对应。

## 6.5 一处标签层的小漂移

中文页 88 条描述里，**87 条用「描述：」，1 条用「说明：」**；英文页 88 条统一用 `Description:`。条数差 1（87 vs 88）不是漏条，而是标签换了词。凡按标签计数描述句的复测脚本，都要同时接受「描述」与「说明」。

---

# 7 用这套词汇写东西时的规范

1. **影响句写三段**：主体 + 情态 + 后果。少写主体（把"App 或许能够"写成"可以"）就是把 40/88 条的最大类抹掉。
2. **情态不升级**：「或许能够」不写"能够"，「可能会导致」不写"导致"，`may` 不译"将"。
3. **三级对象要写全**：系统 / App / 进程；内核内存 / 进程内存。见 2.7。
4. **动词选择保留英文**：`circumvent`（沙盒）与 `bypass`（门禁、隐私偏好）在中文里都是"绕过"，引用时并列英文原形。
5. **缺陷名与手段名分列**：前者落"解决___问题"，后者落"通过改进___"。见 3.4。
6. **跨层引用先声明层**：写"Apple 文档指出"只能引手册层与措辞层原句；研究层词（利用链、0-click、持久化）必须标明来源为研究披露、顾问页之外的材料，或本套报告自设的分析框架。
7. **缩写带全名**：MAC / STS / SKP / IOMMU / DFU 至少首次给全名，冲突见 6.2。
8. **计数标分母**：本期任何"Apple 从不使用 X"的断言，其分母是"179 页正文区 + 2 页顾问页"，不是全部官方文本。
9. **不要把稀有当次要**：PAC 全文 1 次不代表 PAC 不重要，只代表这份手册给它的位置只有一句。频次用于定位措辞来源，不用于评估技术权重。
10. **术语加引号沿用官方惯例**：中文页给外来术语加弯引号（"释放后使用"、"门禁"、"隐私"），本手册沿用，便于读者一眼识别这是被定义词而非普通描述。

---

# 8 缺口清单：这份手册查不到什么

| 想要 | 三层文本里没有 | 可取的替代来源 | 取到后要标注的口径 |
|---|---|---|---|
| 官方对"0-click"的定义 | M/A 均 0 次 | 研究披露、CVP 提案说明、厂商博客 | 非 Apple 平台安全指南口径 |
| 官方"利用链"上位术语 | 0 次 | 研究者写作与本套报告自设框架 | 自设分析词汇 |
| CWE 与缺陷类的映射 | 措辞页不写 CWE 编号 | NVD / CWE 官网 | 第三方分类，与厂商措辞不是一一对应 |
| 行为检测矩阵 | 无 | MITRE ATT&CK Mobile | 第三方框架，非 Apple 口径 |
| 缺陷的源码级成因 | 措辞页只给手段类短语 | XNU 开源镜像 | 开源树 ≠ 设备实际运行版本 |
| 严重性与 CVSS | 顾问页不给评分 | CVP 评级表、NVD | 分数口径与厂商措辞不同源 |
| 在野利用声明 | 本期两条顾问页 0 次 | 带 `Apple is aware…` 声明的页面 | 须引到具体页面与发布日期 |

---

# 9 附录

- 附表 A：顾问页 149041 全 88 条 Impact 逐条中英对照
- 附表 B：修复手段 20 种中英对照
- 附表 C：措辞页命名的缺陷类型（25 种）
- 附表 D：《Apple 平台安全保护》官方术语表全 60 条（含条目锚点与定义原文）
- 附表 E：手册层正文区的「中文（English）」对照抽取（264 对 / 175 个英文词，按英文聚合，未清洗的原始抽取）
- 附表 F：攻击技术词汇在三层文本里的命中次数（49 词）

---

# 10 数据来源与复测方法

**抓取时间**：2026-10-26。

**手册层**：《Apple 平台安全保护》zh-CN 指南 179 页，取自本地既有抓取转储（179 个按页锚点命名的 HTML 文件与一份锚点清单）。正文区按 `<div id='article-section-wrapper'>` 之后的片段切出，剥离 `<script>`/`<style>`/注释/全部标签，并还原 `&nbsp; &amp; &lt; &gt; &quot; &#39; &#NNN;` 实体。切出后合计 **345,840 字符**（同一口径下切出前的整页文本 1,389,269 字符）——179 个页面把整份目录嵌在导航里，**不切正文区会使任何中文词频统计虚高数倍**（本期实测："密封密钥保护 (SKP)"未切正文区时报 179 页 189 次，切出后为 7 页 9 次）。本节口径可以一次跑完重放：同一个解析函数分别作用于"整页"与"`article-section-wrapper` 之后"两个输入，即可同时得到 179 页、345,840 / 1,389,269 字符、264 对 / 362 次 / 109 页 / 175 个英文词，以及 SKP 的两组对照数。

**措辞层**：`https://support.apple.com/en-us/149041`、`https://support.apple.com/zh-cn/149041`、`https://support.apple.com/en-us/149226`、`https://support.apple.com/zh-cn/149226`，HTTP 200，原始 HTML 已留存本地。条目解析按"遇 `Impact:`/`影响：` 起新条目，取紧随的 `Description:`/`描述：|说明：` 为首个描述"的块状方式，**不使用两列平行数组对齐**（后者会因 6.5 的标签漂移错开一位）。

**已知局限**：

- 措辞层样本只有 2 个文档，`may` 的分布、主体表与后果表不能当全部顾问页的频率结论，只能当"本期样本内的封闭集"。
- 附表 F 的中文列按整词检索，组合词需拆读：「越界读写」中文列 0，实际措辞页写作「越界写入」14 与「越界读取」4；「信息泄露」中文列 2，另有「泄露」7 次散在其他后果句；「提权」中文列 0，措辞页写「获得根权限」1 次。
- 英文列的泛化正则（`image|video|audio|font`、`termination`、`sensitive user data`）命中数包含非术语用法，只作量级对比，不作精确计数。
- 手册层的中英对照抽取带自动粘连伪影，已在 6.3 说明并人工校正。
- 复现方式：正文区切取与描述句解析各写成一段独立脚本，对同一份本地 HTML 运行即可重放出本节全部计数；附表 E、附表 F 由脚本直接从原始 HTML 生成，不经手工誊写。附表 F 的三列命中数已用同一份检索式对三层文本各重跑一次逐行核对（49 行，0 处不符）。

## 附表 A｜顾问页 149041（iOS 26.7 / iPadOS 26.7）全 88 条逐条中英对照

按条目块解析对齐（非两列平行数组），每行同一条 CVE 条目。

| # | 英文 Impact | 中文影响 |
|---:|---|---|
| 1 | Processing a maliciously crafted image may lead to unexpected process termination | 处理恶意制作的图像可能会导致进程意外终止 |
| 2 | An app may be able to access sensitive user data | App 或许能够访问敏感的用户数据 |
| 3 | An app may be able to cause unexpected system termination or write kernel memory | App 或许能够导致系统意外终止或向内核内存写入数据 |
| 4 | An app may be able to cause unexpected system termination | App 或许能够导致系统意外终止 |
| 5 | An app may be able to cause unexpected system termination | App 或许能够导致系统意外终止 |
| 6 | Mounting a disk image with maliciously crafted files may lead to unexpected system termination | 装载包含恶意制作文件的磁盘映像可能会导致系统意外终止 |
| 7 | A local app may be able to read a persistent account identifier | 本地 App 或许能够读取永久性账户标识符 |
| 8 | An app may be able to cause unexpected system termination | App 或许能够导致系统意外终止 |
| 9 | An app may be able to cause unexpected system termination | App 或许能够导致系统意外终止 |
| 10 | A sandboxed app may be able to execute arbitrary code with kernel privileges | 沙盒化 App 或许能够以内核权限执行任意代码 |
| 11 | An app may be able to access sensitive user data | App 或许能够访问敏感的用户数据 |
| 12 | An attacker in a privileged network position may be able to cause a denial-of-service | 拥有特权网络地位的攻击者或许能够导致服务遭拒 |
| 13 | A remote attacker may be able to cause unexpected app termination or arbitrary code execution | 远程攻击者或许能够导致 App 意外终止或任意代码执行 |
| 14 | An attacker in radio range may be able to cause unexpected system termination | 无线电通信范围内的攻击者或许能够导致系统意外终止 |
| 15 | An archive may be able to bypass Gatekeeper | 归档文件或许能够绕过"门禁" |
| 16 | An attacker with a paired Bluetooth device may be able to execute arbitrary code | 拥有已配对蓝牙设备的攻击者或许能够执行任意代码 |
| 17 | Processing a maliciously crafted video file may lead to unexpected app termination | 处理恶意制作的视频文件可能会导致 App 意外终止 |
| 18 | A sandboxed process may be able to circumvent sandbox restrictions | 沙盒化进程或许能够绕过沙盒限制 |
| 19 | Processing a maliciously crafted media file may lead to unexpected app termination or corrupt process memory | 处理恶意制作的媒体文件可能会导致 App 意外终止或损坏进程内存 |
| 20 | Processing a maliciously crafted video file may lead to unexpected app termination or corrupt process memory | 处理恶意制作的视频文件可能会导致 App 意外终止或损坏进程内存 |
| 21 | A sandboxed app may be able to access restricted files | 沙盒化 App 或许能够访问受限制的文件 |
| 22 | An app may be able to access motion data from headphones without user consent | App 或许能够在未经用户同意的情况下访问耳机中的动作数据 |
| 23 | Processing web content may lead to a denial-of-service | 处理网页内容可能会导致服务遭拒 |
| 24 | An app may be able to read persistent device identifiers | App 或许能够读取永久性设备标识符 |
| 25 | An app may be able to cause unexpected system termination | App 或许能够导致系统意外终止 |
| 26 | Mounting a maliciously crafted volume may lead to unexpected system termination | 装载恶意制作的宗卷可能会导致系统意外终止 |
| 27 | Extracting a maliciously crafted archive may allow an attacker to write arbitrary files | 提取恶意制作的归档文件可能会允许攻击者写入任意文件 |
| 28 | Processing a maliciously crafted font file may lead to unexpected app termination | 处理恶意制作的字体文件可能会导致 App 意外终止 |
| 29 | An app may be able to cause a denial of service | App 或许能够导致服务遭拒 |
| 30 | An app may be able to cause unexpected system termination | App 或许能够导致系统意外终止 |
| 31 | Processing a maliciously crafted image may result in disclosure of process memory | 处理恶意制作的图像可能会导致进程内存泄露 |
| 32 | Processing a maliciously crafted file may lead to unexpected app termination | 处理恶意制作的文件可能会导致 App 意外终止 |
| 33 | Processing a maliciously crafted image may corrupt process memory | 处理恶意制作的图像可能会损坏进程内存 |
| 34 | Processing a maliciously crafted image may lead to unexpected app termination | 处理恶意制作的图像可能会导致 App 意外终止 |
| 35 | Processing a maliciously crafted image may result in memory corruption | 处理恶意制作的图像可能会导致内存损坏 |
| 36 | An app may be able to cause unexpected system termination | App 或许能够导致系统意外终止 |
| 37 | An app may be able to cause unexpected system termination | App 或许能够导致系统意外终止 |
| 38 | A local attacker may be able to cause unexpected system termination or corrupt kernel memory | 本地攻击者或许能够造成系统意外终止或损坏内核内存 |
| 39 | An app may be able to cause unexpected system termination or corrupt kernel memory | App 或许能够造成系统意外终止或损坏内核内存 |
| 40 | A malicious app may be able to gain root privileges | 恶意 App 或许能够获得根权限 |
| 41 | An app may be able to cause unexpected system termination | App 或许能够导致系统意外终止 |
| 42 | Connecting to a malicious NFS server may disclose kernel memory | 连接到恶意 NFS 服务器可能会泄露内核内存 |
| 43 | An app may be able to cause unexpected system termination or corrupt kernel memory | App 或许能够造成系统意外终止或损坏内核内存 |
| 44 | Connecting to a malicious NFS server may lead to kernel memory corruption | 连接到恶意 NFS 服务器可能会导致内核内存损坏 |
| 45 | An app may be able to determine kernel memory layout | App 或许能够确定内核内存布局 |
| 46 | An app may be able to cause unexpected system termination | App 或许能够导致系统意外终止 |
| 47 | An app may be able to disclose kernel memory | App 或许能够泄露内核内存 |
| 48 | An app with root privileges may be able to read uninitialized kernel memory | 拥有根权限的 App 或许能够读取未初始化的内核内存 |
| 49 | An app may be able to cause unexpected system termination | App 或许能够导致系统意外终止 |
| 50 | An app may be able to cause unexpected system termination | App 或许能够导致系统意外终止 |
| 51 | An app may be able to cause unexpected system termination or corrupt kernel memory | App 或许能够造成系统意外终止或损坏内核内存 |
| 52 | An app may be able to cause unexpected system termination or corrupt kernel memory | App 或许能够造成系统意外终止或损坏内核内存 |
| 53 | A local user may be able to cause unexpected system termination or read kernel memory | 本地用户或许能够导致系统意外终止或读取内核内存 |
| 54 | An app may be able to cause unexpected system termination | App 或许能够导致系统意外终止 |
| 55 | Processing a maliciously crafted file may lead to unexpected app termination | 处理恶意制作的文件可能会导致 App 意外终止 |
| 56 | Processing a maliciously crafted image may lead to unexpected process termination | 处理恶意制作的图像可能会导致进程意外终止 |
| 57 | Connecting a malicious accessory may cause unexpected system termination | 连接恶意配件可能会导致系统意外终止 |
| 58 | An app may be able to modify protected parts of the file system | App 或许能够修改文件系统的受保护部分 |
| 59 | An attacker with physical access to a trust-paired device may be able to read and write arbitrary files | 能够实际操作受信任配对设备的攻击者或许能够读取和写入任意文件 |
| 60 | Opening a maliciously crafted file may lead to unexpected process termination | 打开恶意制作的文件可能会导致进程意外终止 |
| 61 | An app may be able to access sensitive user data | App 或许能够访问敏感的用户数据 |
| 62 | An app may be able to identify what other apps a user has installed | App 或许能够识别用户已安装哪些其他 App |
| 63 | An app may be able to access sensitive user data | App 或许能够访问敏感的用户数据 |
| 64 | An app may be able to fingerprint the device | App 或许能够对设备创建独一无二的描述信息 |
| 65 | Opening a maliciously crafted file may cause unexpected process termination or disclose process memory | 打开恶意制作的文件可能会导致进程意外终止或泄露进程内存 |
| 66 | Processing a maliciously crafted file may lead to unexpected app termination | 处理恶意制作的文件可能会导致 App 意外终止 |
| 67 | An app may be able to access sensitive user data | App 或许能够访问敏感的用户数据 |
| 68 | An app may be able to access sensitive user data | App 或许能够访问敏感的用户数据 |
| 69 | Processing a maliciously crafted file may result in disclosure of process memory | 处理恶意制作的文件可能会导致进程内存泄露 |
| 70 | Processing a maliciously crafted 3D model may lead to memory corruption | 处理恶意制作的 3D 模型可能会导致内存损坏 |
| 71 | Processing a maliciously crafted 3D model may lead to memory corruption | 处理恶意制作的 3D 模型可能会导致内存损坏 |
| 72 | Processing a maliciously crafted 3D model may lead to memory corruption | 处理恶意制作的 3D 模型可能会导致内存损坏 |
| 73 | Processing a maliciously crafted 3D scene may lead to unexpected process termination | 处理恶意制作的 3D 场景可能会导致进程意外终止 |
| 74 | An attacker with a compromised intermediate certificate authority may be able to issue certificates with arbitrary extended key usages | 入侵了中间证书颁发机构的攻击者或许能够颁发具有任意扩展密钥用途的证书 |
| 75 | A local attacker may be able to overwrite restricted files | 本地攻击者或许能够覆盖受限文件 |
| 76 | An attacker with physical access to a locked device may be able to view sensitive user information | 能够实际操作已锁定设备的攻击者或许能够查看敏感用户信息 |
| 77 | An app may be able to access sensitive user data | App 或许能够访问敏感的用户数据 |
| 78 | An app may be able to cause a denial-of-service | App 或许能够导致服务遭拒 |
| 79 | An app may be able to access user-sensitive data | App 或许能够访问用户敏感数据 |
| 80 | An app may be able to modify protected parts of the file system | App 或许能够修改文件系统的受保护部分 |
| 81 | A malicious application may be able to determine a user's current location | 恶意应用程序或许能够确定用户的当前位置 |
| 82 | An app may be able to modify protected system files | App 或许能够修改受保护的系统文件 |
| 83 | An app may be able to bypass certain Privacy preferences | App 或许能够绕过某些"隐私"偏好设置 |
| 84 | An app may be able to track users across apps and websites without permission | App 或许能够在未经许可的情况下跨 App 和网站跟踪用户 |
| 85 | Processing maliciously crafted web content may lead to memory corruption | 处理恶意制作的网页内容可能会导致内存损坏 |
| 86 | Processing maliciously crafted web content may lead to an unexpected Safari crash | 处理恶意制作的网页内容可能会导致 Safari 浏览器意外崩溃 |
| 87 | An attacker in wireless proximity may be able to cause unexpected system termination | 无线近距离范围内的攻击者或许能够导致系统意外终止 |
| 88 | An app may be able to access sensitive user data | App 或许能够访问敏感的用户数据 |

## 附表 B｜修复手段 20 种中英对照

| 英文（was addressed with/by …） | 中文对应（原样，括号内为本页条数） | 条数 |
|---|---|---:|
| improved bounds checking | 已通过改进边界检查解决越界写入问题。（14）<br>已通过改进边界检查解决越界读取问题。（3）<br>已通过改进边界检查解决堆缓冲区溢出问题。（2） | 19 |
| improved memory handling | 已通过改进内存处理解决这个问题。（5）<br>已通过改进内存处理解决类型混淆问题。（2）<br>已通过改进内存处理解决内存初始化问题。（2）<br>已通过改进内存处理解决缓冲区溢出问题。<br>已通过改进内存处理解决内存损坏问题。 | 11 |
| improved input validation | 已通过改进输入验证解决整数溢出问题。（3）<br>已通过改进输入验证解决服务遭拒问题。<br>已通过改进输入验证解决堆栈溢出问题。<br>已通过改进输入验证解决空指针解引用问题。<br>已通过改进输入验证解决输入验证问题。<br>已通过改进输入验证解决内存损坏问题。<br>已通过改进输入验证解决越界读取问题。<br>已通过改进输入验证解决了路径遍历问题。 | 10 |
| improved memory management | 已通过改进内存管理解决"释放后使用"问题。（7）<br>已通过改进内存管理解决双重释放问题。（2）<br>已通过改进内存管理解决信息泄露问题。 | 10 |
| improved state management | 已通过改进状态管理解决授权问题。（3）<br>已通过改进状态管理解决了竞态条件问题。<br>已通过改进状态管理解决信息泄露问题。<br>已通过改进状态管理解决隐私问题。 | 6 |
| additional restrictions | 已通过实施额外的限制解决权限问题。（5） | 5 |
| improved validation | 已通过改进验证解决路径处理问题。（2）<br>已通过改进验证解决逻辑问题。<br>已通过改进验证解决授权问题。<br>已通过改进验证解决权限问题。 | 5 |
| improved checks | 已通过改进检查解决这个问题。（2）<br>已通过改进检查解决类型混淆问题。<br>已通过改进检查解决逻辑问题。 | 4 |
| improved state handling | 已通过改进状态处理解决竞态条件问题。（4） | 4 |
| improved path validation | 已通过改进路径验证解决权限问题。<br>已通过改进路径验证解决路径遍历问题。 | 2 |
| improved access control | 已通过改进访问控制解决授权问题。（2） | 2 |
| additional entitlement checks | 已通过实施额外的授权检查解决这个问题。（2） | 2 |
| improved data protection | 已通过改进数据保护解决这个问题。 | 1 |
| additional checks | 已通过增加检查来解决文件隔离功能被绕过的问题。 | 1 |
| improved memory initialization | 已通过改进内存初始化解决未初始化的内存问题。 | 1 |
| improved bounds checks | 已通过改进边界检查解决这个问题。 | 1 |
| improved size validation | 已通过改进大小验证解决缓冲区溢出问题。 | 1 |
| improved certificate validation | 已通过改进证书验证解决证书验证问题。 | 1 |
| improved private data redaction for log entries | 已通过改进日志条目的隐私数据遮盖策略解决隐私问题。 | 1 |
| removing sensitive data | 已通过移除敏感数据解决隐私问题。 | 1 |

## 附表 C｜措辞页命名的缺陷类型（中文页，去重后 25 种）

| 缺陷类型（中文） | 本页条数 | 同位置英文句常配的手段 |
|---|---:|---|
| 越界写入 | 14 | An out-of-bounds write issue was addressed with improved bounds checking |
| 这个 | 11 | This issue was addressed with improved data protection / The issue was addressed with improved checks / The issue was addressed with improved memory handling |
| 权限 | 7 | A permissions issue was addressed with additional restrictions / A permissions issue was addressed with improved path validation / A permissions issue was addressed with improved validation |
| 授权 | 6 | An authorization issue was addressed with improved validation / An authorization issue was addressed with improved access control / An authorization issue was addressed with improved state management |
| 竞态条件 | 5 | A race condition was addressed with improved state handling |
| 越界读取 | 4 | An out-of-bounds read was addressed with improved bounds checking / An out-of-bounds read issue was addressed with improved input validation |
| 整数溢出 | 3 | An integer overflow was addressed with improved input validation |
| 类型混淆 | 3 | A type confusion issue was addressed with improved memory handling / A type confusion issue was addressed with improved checks |
| 隐私 | 3 | A privacy issue was addressed with improved private data redaction for log entries / A privacy issue was addressed by removing sensitive data / A privacy issue was addressed with improved state management |
| 逻辑 | 2 | A logic issue was addressed with improved validation / A logic issue was addressed with improved checks |
| 堆缓冲区溢出 | 2 | A heap buffer overflow was addressed with improved bounds checking |
| 路径处理 | 2 | A path handling issue was addressed with improved validation |
| 缓冲区溢出 | 2 | A buffer overflow issue was addressed with improved memory handling / A buffer overflow was addressed with improved size validation |
| 内存损坏 | 2 | A memory corruption issue was addressed with improved memory handling / A memory corruption issue was addressed with improved input validation |
| 内存初始化 | 2 | A memory initialization issue was addressed with improved memory handling |
| 信息泄露 | 2 | An information disclosure issue was addressed with improved memory management / An information disclosure issue was addressed with improved state management |
| 双重释放 | 2 | A double free issue was addressed with improved memory management |
| 路径遍历 | 2 | A path traversal issue was addressed with improved path validation |
| 服务遭拒 | 1 | A denial-of-service issue was addressed with improved input validation |
| 文件隔离功能被绕过的 | 1 | A file quarantine bypass was addressed with additional checks |
| 堆栈溢出 | 1 | A stack overflow was addressed with improved input validation |
| 空指针解引用 | 1 | A null pointer dereference was addressed with improved input validation |
| 未初始化的内存 | 1 | An uninitialized memory issue was addressed with improved memory initialization |
| 输入验证 | 1 | An input validation issue was addressed with improved input validation |
| 证书验证 | 1 | A certificate validation issue was addressed with improved certificate validation |

## 附表 D｜《Apple 平台安全保护》官方术语表全 60 条

来源页 sec93292bfa6（本页抓取日期 2026-10-26）。带★的定义原文里出现“攻击/恶意/滥用/篡改/窃取”。

| # | 官方术语名 | 条目锚点 | 定义（原样） |
|---:|---|---|---|
| 1 | ★ 安全储存组件 | `secb508da3cb` | 芯片设计为使用不可更改的 RO 代码、硬件随机数生成器、加密引擎和物理篡改检测。在支持的设备上，安全隔区与用于储存反重放值的安全储存组件配对。为了读取和更新反重放值，安全隔区和储存芯片采用安全协议来帮助确保对反重放值的排他访问。此技术已更迭多代，提供了不同的安全性保证。 |
| 2 | ★ 地址空间布局随机化 (ASLR) | `secbac84d05d` | 操作系统所采用的一项技术，旨在让恶意利用软件错误的成功几率极大地降低。通过确保内存地址和偏移量不可预测，使攻击代码无法对这些值进行硬编码。 |
| 3 |  底层引导载入程序 (LLB) | `sec2a442ad02` | 在具有两步启动架构的 Mac 上，LLB 包含由 Boot ROM 调用的代码，该代码随后会载入 iBoot，作为安全启动链的一环。 |
| 4 |  恢复模式 | `sec2bfd9f16f` | 该模式用于在无法识别用户设备的情况下恢复许多 Apple 设备，使用户可以重新安装操作系统。 |
| 5 |  集成电路 (IC) | `sec5b8c8ec05` | 也称为微芯片。 |
| 6 | ★ 可擦除存储器 | `seca7f9fb6d8` | NAND 存储器中一个用于储存加密密钥的专用区域，可被直接寻址和安全擦除。尽管当攻击者实际占有设备时，可擦除存储器无法提供保护，但其中存储的密钥可用作密钥层级的一部分，用于实现快速擦除和前向安全性。 |
| 7 |  联合测试行动小组 (JTAG) | `sec4a0e59d76` | 一个由程序员和电路开发者采用的标准硬件调试工具。 |
| 8 |  临时椭圆曲线迪菲-赫尔曼交换 (ECDHE) | `secab15fa6bf` | 一种基于椭圆曲线的密钥交换机制。ECDHE 允许双方就私密密钥达成一致，其使用的方式可防止窃听双方信息的窃听者发现密钥。 |
| 9 |  媒介密钥 | `secd3bcda329` | 加密密钥层次的一部分。媒介密钥有助于确保快速、安全的擦除 Apple 设备上的数据。在 iPad、iPhone、Apple TV 和 Apple Watch 上，媒介密钥通过封装数据宗卷上的元数据来执行此操作。如果没有媒介密钥，文件密钥会锁定，导致通过数据保护进行保护的文件无法访问。在 Mac 上，媒介密钥会封装密钥材料、所有元数据以及文件保险箱数据。在这两种情况下，擦除媒介密钥都会阻止访问加密数据。 |
| 10 |  门禁 | `seca704f6188` | 运行 macOS 的设备上的一项技术，其设计旨在帮助确保仅受信任的软件可在用户的 Mac 上运行。 |
| 11 |  密封密钥保护 (SKP) | `sec56eaa7293` | 数据保护中的一种技术，其使用系统软件的测量值和仅在硬件中可用的密钥来保护（或密封）加密密钥。 |
| 12 |  密码派生密钥 (PDK) | `secec35a6917` | 用户密码与长期 SKP 密钥和安全隔区的 UID 配合使用，由此派生加密密钥。 |
| 13 |  密钥包 | `sec144a9d5ae` | 一种用于储存一组类密钥的数据结构。每种类型（用户、设备、系统、备份、托管或 iCloud 云备份）的格式都相同。包含以下内容的标头：版本（在 iOS 12 或更高版本中设为四）；类型（系统、备份、托管或 iCloud 云备份）；密钥包 UUID；HMAC（若密钥包已签名）；用于封装类密钥的方法：配合盐和迭代计数使用 Tangling 及 UID 或 PBKDF2。类密钥列表：密钥 UUID；类（哪个文件或钥匙串数据保护类）；封装类型（仅 UID 派生密钥；UID 派生密钥和密码派生密钥）；封装的类密钥；非对称类的公钥。 |
| 14 |  密钥封装 | `sec7c4746df7` | 使用一个密钥来加密另一个密钥。iOS 和 iPadOS 根据 RFC 3394 使用 NIST AES 密钥封装。 |
| 15 |  内存控制器 | `sec2c2268a3b` | 片上系统中的子系统，控制片上系统与其主内存之间的接口。 |
| 16 |  片上系统 (SoC) | `sec9135cb889` | 一种将多种组件整合到单个芯片上的集成电路 (IC)。应用程序处理器、安全隔区和其他协处理器都是 SoC 的组件。 |
| 17 |  启动进程寄存器 (BPR) | `secf0d9abdc8` | 一组片上系统 (SoC) 硬件旗标，软件可使用其跟踪设备进入的启动模式，如设备固件更新 (DFU) 模式和恢复模式。启动进程寄存器旗标经设定后就不可清除。这样就允许后续软件获得系统状态的可信指示。 |
| 18 |  启动转换 | `secff76073ad` | 支持在受支持的 Mac 电脑上安装 Microsoft Windows 的 Mac 实用工具。 |
| 19 |  软件种子位 | `sec2447d22dd` | 安全隔区 AES 引擎中的专用位，从 UID 生成密钥时追加到 UID 末尾。每个软件种子位都有对应的锁定位。只要对应的锁定位还未设定，安全隔区 Boot ROM 和操作系统就可独立更改每个软件种子位的值。设定锁定位后，其本身或软件种子位都不能修改。安全隔区重启时，软件种子位和其锁定位会还原。 |
| 20 |  设备固件升级 (DFU) 模式 | `sec7d96729bd` | 设备的 Boot ROM 代码在等待通过 USB 进行恢复时所处的模式。在 DFU 模式下，设备出现黑屏，但在连接到装有访达或 iTunes 的电脑时，会出现以下提示：“访达（或 iTunes）检测到一个处于恢复模式的 [iPad][iPhone]。用户需要先恢复此 [iPad][iPhone]，然后才能将它与访达（或 iTunes）配合使用。” |
| 21 |  设备管理服务 | `secf30269228` | 一种可让管理员远程管理已注册设备的服务。设备注册后，管理员可通过网络使用设备管理服务在设备上配置设置以及执行其他任务，无需进行用户交互。 |
| 22 |  输入/输出内存管理单元 (IOMMU) | `sec888161fd1` | 输入/输出内存管理单元。集成芯片中的子系统，用于控制从其他输入/输出设备和外围设备访问地址空间的权限。 |
| 23 |  数据保护 | `secb953fca6b` | 支持的 Apple 设备的文件和钥匙串保护机制。它也可以指 App 用来保护文件和钥匙串项的 API。 |
| 24 |  数据保险箱 | `sec2afe6ed83` | 由内核强制实施的一种机制，用于防止未经授权访问数据，不管请求的 App 本身是否经过沙盒化。 |
| 25 |  统一可扩展固件接口 (UEFI) 固件 | `secc241217ea` | 一种 BIOS 的替代技术，用于将固件连接到电脑的操作系统。 |
| 26 |  统一资源标识符 (URI) | `sec1313cac2d` | 可识别基于网络的资源的字符串。 |
| 27 |  椭圆曲线数字签名算法 (ECDSA) | `sec6d6f4bd40` | 一种基于椭圆曲线加密的数字签名算法。 |
| 28 | ★ 唯一 ID (UID) | `secba464e9db` | 一个 256 位的 AES 密钥，在设备制造过程中刻录在每个处理器上。这种密钥无法由固件或软件读取，只能由处理器的硬件 AES 引擎使用。若要获取实际密钥，攻击者必须对处理器的芯片发起极为复杂且代价高昂的物理攻击。UID 与设备上的任何其他标识符均无关，包括但不限于 UDID。 |
| 29 |  文件独有密钥 | `sec2043a6047` | 数据保护用于在文件系统上加密文件的密钥。文件独有密钥使用类密钥封装，储存在文件的元数据中。 |
| 30 |  文件系统密钥 | `secc4da514a3` | 用于加密每个文件的元数据的密钥，包括其类密钥。存储在可擦除存储器中，用于实现快速擦除，并非用于保密目的。 |
| 31 |  纹路走向角度映射 | `sec9840b0edb` | 一种从部分指纹中提取以用于描述纹路走向和宽度的数学呈现方式。 |
| 32 |  系统软件授权 | `sec45370f853` | 将植入硬件的加密密钥与在线服务相结合的过程，以检查在升级时是否仅提供和安装由 Apple 发布且适用于受支持设备的正版软件。 |
| 33 |  系统协处理器完整性保护 (SCIP) | `sec6697394d4` | Apple 采用的一种机制，其设计旨在阻止修改协处理器固件。 |
| 34 |  钥匙串 | `sec5e205bc1a` | 一种基础架构和一组 API、Apple 操作系统和第三方 App 用来储存和检索密码、密钥及其他敏感凭证。 |
| 35 | ★ 硬件安全模块 (HSM) | `sec73cd68c1c` | 专门防篡改的电脑，可保障数字密钥的安全并对其进行管理。 |
| 36 |  预置描述文件 | `seced8a93f2b` | Apple 签名的属性列表（.plist 文件），其中列明允许在 iOS 或 iPadOS 设备上安装和测试 App 的实体和授权。开发预置描述文件列出开发人员选择要进行 Ad Hoc 分发的设备，分发预置描述文件中包含企业开发的 App 的 App ID。 |
| 37 |  增强的串行外设接口 (eSPI) | `sec1e94b9bfc` | 设计用于同步串行通信的一体化总线。 |
| 38 |  直接内存访问 (DMA) | `secfab17ba09` | 一项允许硬件子系统绕过 CPU 直接访问主内存的功能。 |
| 39 |  专有芯片 ID (ECID) | `sec90446fa04` | 每台 iPad 或 iPhone 中处理器独有的一个 64 位标识符，此标识符作为定制化过程的一部分，不被视为密钥。 |
| 40 |  组 ID (GID) | `sec90c2449f5` | 类似于 UID，但同一类中的每个处理器的 GID 都相同。 |
| 41 |  AES 加密引擎 | `secddbfd12ed` | 执行 AES 的专用硬件组件。 |
| 42 |  AES-XTS | `sec289614c27` | IEEE 1619-2007 中所规定的一种 AES 模式，用于加密存储介质。 |
| 43 |  AES（高级加密标准） | `sece457bbb84` | 一种流行的全局加密标准，用于加密数据以保护其隐私。 |
| 44 |  Apple 安全性奖金 | `sec26fdc2e73` | Apple 向研究人员发放的奖金，奖励其报告影响最新发布的操作系统及相关最新硬件的漏洞。 |
| 45 |  Apple 商务管理 | `sec604f04e41` | 一个针对 IT 管理员且基于网站的简单门户，为组织提供了一种快速简单的方法来部署 Apple 设备，无论设备是组织直接从 Apple 购买的，还是购买自 Apple 合作授权经销商或运营商处。在用户获得设备前，管理员无需实际操作或者准备设备，即可在设备管理服务中自动注册设备。 |
| 46 |  Apple 身份服务 (IDS) | `sec5df345784` | Apple 的 iMessage 信息公钥、APNs 地址和电话号码及电子邮件地址目录，用于查找密钥和设备地址。 |
| 47 |  Apple 推送通知服务 (APNs) | `sec832d3d88d` | 一项由 Apple 提供的全球服务，用于向 Apple 设备传送推送通知。 |
| 48 |  Apple 文件系统 (APFS) | `sec024478b96` | 这是 iOS、iPadOS、Apple tvOS、使用 macOS 10.13 或更高版本的 Mac 电脑、visionOS 和 watchOS 的默认文件系统。APFS 具有强加密、空间共享、快照、快速目录大小统计功能以及改进的文件系统基础。 |
| 49 |  Apple 校园教务管理 | `sec969c3d32e` | 一个针对 IT 管理员且基于网站的简单门户，为组织提供了一种快速简单的方法来部署 Apple 设备，无论设备是组织直接从 Apple 购买的，还是购买自 Apple 合作授权经销商或运营商处。在用户获得设备前，管理员无需实际操作或者准备设备，即可在设备管理服务中自动注册设备。 |
| 50 | ★ Boot ROM | `sec958ae1df2` | 设备的处理器在首次启动时所执行的第一个代码。作为处理器不可分割的一部分，Apple 或攻击者均无法修改。 |
| 51 |  CKRecord | `sec12b0a3711` | 键值对词典，键值对包含通过 CloudKit 保存或获取的数据。 |
| 52 |  HMAC | `sec7b3c3f772` | 一种基于加密哈希函数的信息认证码。 |
| 53 |  iBoot | `secc0431742a` | 适用于所有 Apple 设备的 2 级启动载入程序。载入 XNU 以作为安全启动链一部分的代码。iBoot 可由底层引导载入程序载入或直接由 Boot ROM 载入，具体取决于片上系统 (SoC) 的版本。 |
| 54 |  NAND | `sec1c3203ec9` | 非易失性闪存。 |
| 55 |  sepOS | `secb07304f2b` | 安全隔区固件，基于 Apple 定制版本的 L4 微内核。 |
| 56 |  SSD 控制器 | `sec88978a824` | 管理存储介质（固态硬盘）的硬件子系统。 |
| 57 | ★ Tangling | `sec194ce224c` | 用户密码转换为密钥并使用设备的 UID 加强的过程。此过程帮助确保暴力攻击需要在指定设备上执行，因此可限制攻击的频度且避免多部设备同时遭到攻击。Tangling 算法是 PBKDF2。这种算法为每次迭代使用加入设备 UID 的 AES 密钥作为伪随机函数 (PRF)。 |
| 58 |  xART | `sec3b9594880` | eXtended 反重放技术的缩写。一组为具有反重放功能（基于物理储存架构）的安全隔区提供加密且经认证的永久储存区的服务。请参阅“安全储存组件”。 |
| 59 |  XNU | `sec5951608cd` | Apple 操作系统中央的内核。默认为受信任状态，并强制执行代码签名、沙盒化、授权核对和地址空间布局随机化 (ASLR) 等安全措施。 |
| 60 | ★ XProtect | `seca79c87c78` | 运行 macOS 的设备上的一项防病毒技术，可基于签名检测和移除恶意软件。 |

## 附表 E｜手册层正文区的「中文（English）」对照抽取

抽取口径：179 页 article-section 正文区（剔除导航/目录块后 345,840 字符，抓取与解析时间 2026-10-26），检索式为 `([一-鿿]{1,30})s*(([A-Za-z][A-Za-z0-9 .-/]{1,40}))`，共得到 **264 对不重复组合、362 次命中、覆盖 109 页、175 个英文词**。下表按英文聚合中文写法，**这是原始自动抽取结果，未做人工清洗**：前置词粘连（“设备使用低功耗蓝牙”）、句首截断与斜杠合并都会出现在这里；正文引用时一律用清洗后的规范名，伪影说明见 6.3。

| 英文 | 中文写法（次数） |
|---|---|
| ACL | 钥匙串可以使用访问控制列表(1) / 钥匙串数据通过访问控制列表(1) |
| AES | 和高级加密标准(1) |
| AOT | 通过提前(1) |
| AP | 其中包括在接入兼容的无线接入点(1) / 无需使用任何互联网连接或无线接入点(1) |
| APFS | 文件系统(6) |
| APNs | 推送通知服务(6) |
| Apple VAS | 增值服务(1) |
| AR | 该数据绑定至一个授权随机(1) / 使用与添加付款卡时相同的配对密钥和授权随机(1) |
| ASLR | 地址空间布局随机化(2) / 声明的授权和地址空间布局随机化(1) / 授权核对和地址空间布局随机化(1) |
| auxi | 清单哈希值(2) |
| AuxKC | 辅助内核集(6) / 表明应当具有辅助内核集(1) / 的辅助内核集(1) / 会被合并到辅助内核集(1) / 会转而合并到启动过程中载入的辅助内核集(1) |
| auxp | 策略哈希值(2) |
| auxr | 接收项哈希值(2) |
| BAA | 基础证明机构(1) |
| BLE | 但在使用低功耗蓝牙(1) / 的配件和低功耗蓝牙(1) / 该设备使用低功耗蓝牙(1) / 会开始通过低功耗蓝牙(1) / 即经典蓝牙和低功耗蓝牙(1) / 建立频段外的低功耗蓝牙(1) / 例如兼容的低功耗蓝牙(1) / 邻近度通过低功耗蓝牙(1) / 设备发起低功耗蓝牙(1) / 设备可使用低功耗蓝牙(1) / 设备使用低功耗蓝牙(1) / 设备之间会使用低功耗蓝牙(1) / 在两台设备间建立了安全的低功耗蓝牙(1) |
| BPR | 启动进程寄存器(3) |
| BR/EDR | 版已将安全连接功能添加到经典蓝牙(1) |
| CA | 根证书颁发机构(3) / 证书颁发机构(2) / 攻击者劫持证书请求以骗取证书颁发机构(1) / 其关联的属性以及是否有证书颁发机构(1) |
| CBC | 密码块链接(1) |
| CHIP | 芯片(1) |
| CMAC | 并为内存计算基于密码的消息认证码(1) |
| coih | 清单哈希值(2) |
| CPRNG | 密码学伪随机数生成器(1) |
| CRL | 签发者所发布的证书撤销清单(1) |
| CSR | 生成证书签名请求(1) |
| CTR | 并在计数器(1) / 在计数器(1) |
| CTRR | 可配置文本只读区域(1) |
| Curve25519 | 密钥包具有适用于数据保护类的非对称(1) / 密钥交换(1) |
| CVM | 例如持卡人验证方法(1) |
| DFU | 设备固件升级(4) / 可锁定的种子位用于从设备固件更新(1) / 如设备固件更新(1) / 如用于设备固件升级(1) |
| DMA | 电脑必须支持外围设备的直接内存访问(1) / 闪存与主系统内存之间的直接内存访问(1) / 直接内存访问(1) |
| DPA | 引擎还包括动态功耗分析(1) |
| DTLS | 和数据包传输层安全协议(1) |
| EC | 的椭圆曲线(1) |
| ECC | 保护的椭圆曲线加密(1) / 和椭圆曲线加密(1) / 指示设备的安全元件生成椭圆曲线加密(1) |
| ECC-OBKG | 片上密钥生成算法(1) |
| ECDH | 的非对称椭圆曲线加密技术(1) / 赫尔曼(1) |
| ECDHE | 赫尔曼交换(4) |
| ECDSA | 椭圆曲线数字签名算法(4) |
| ECIES | 然后使用椭圆曲线集成加密方案(1) / 设备可能使用椭圆曲线集成加密方案(1) |
| ECU | 读卡器和处理型电子控制单元(1) |
| EDK | 和可扩展开发工具包(1) |
| EEPROM | 使用电可擦除可编程只读存储器(1) |
| EFI | 这些固件基于可扩展固件接口(1) |
| EMTE | 与同步模式下的增强型内存标记扩展(1) |
| eSPI | 此内存通过增强的串行外设接口(1) / 增强的串行外设接口(1) |
| EU | 欧盟(1) |
| FFT | 标准交易还可用于新共享钥匙与车辆的首次接触(1) |
| FIRST | 事件响应与安全组织论坛(1) |
| GAL | 全局地址列表(1) |
| GCM | 计数器模式(1) |
| H2E | 哈希到元素(1) |
| HAP | 配件协议(2) |
| HID | 提供人机界面设计(1) |
| HKDF | 每条信息将使用基于哈希的密钥导出函数(1) |
| HMI | 以及车辆的人机界面(1) |
| HSM | 硬件安全模块(4) / 可在设备安全元件和车辆硬件安全模块(1) / 控制的硬件安全模块(1) |
| iAP | 配件协议(1) |
| IC | 集成电路(2) / 可读取免接触式集成电路(1) / 一种将多种组件整合到单个芯片上的集成电路(1) |
| ICE | 和互联网连接建立(1) |
| IdP | 以便账户可基于组织的身份供应商(1) / 用户会被重定向至身份提供商(1) / 与身份提供商(1) |
| IDS | 身份服务(13) |
| IE | 从而将信息元素(1) |
| IOMMU | 输出内存管理单元(3) / 内核控制的输入输出内存管理单元(1) |
| IPC | 系统服务发送进程间通信(1) |
| IR | 其中包括在参与者知情同意的条件下所开展的研究中收集的红外(1) |
| IRK | 创建和使用机构恢复密钥(1) |
| IV | 位初始化向量(1) |
| IWF | 信息会从网络互通功能(1) |
| JIT | 即时(1) / 在即时(1) |
| JPKI | 支持将移动身份证件和日本公开密钥基础设施(1) |
| JTAG | 联合测试行动小组(2) |
| KEK | 密钥加密密钥(2) / 安全令牌是受用户密码保护的密钥加密密钥(1) / 该密钥使用密钥加密密钥(1) / 相同的密钥加密密钥(1) |
| Kext | 内核扩展(2) / 如果第三方内核扩展(1) / 如引入第三方内核扩展(1) |
| KIP | 会启用内核完整性保护(1) / 上提供的基于硬件的内核完整性保护(1) |
| kSecValueData | 私密值(1) |
| KTS | 证书链发送到密钥追踪服务器(1) |
| LLB | 底层引导载入程序(4) |
| losv | 本地操作系统版本(2) |
| LPN | 随机数(1) |
| lpnh | 随机数哈希值(1) |
| MAC | 来计算信息认证代码(1) / 强制访问控制(1) / 无线局域网(1) |
| macOS | 或自动补全建议(1) / 重复使用和已泄露的密码会在密码列表(1) |
| ME | 管理引擎(2) |
| MIE | 内存完整性强制(1) |
| MITM | 防止中间人(1) |
| MMU | 应用程序处理器的内存管理单元(1) |
| MSO | 发证机构提供的签名等元素会包括在移动安全对象(1) |
| MTE | 年发布了内存标记扩展(1) |
| NFC | 读卡器使用建立的安全通道通过近距离无线通信(1) / 用户只需轻触即可使用近距离无线通信(1) / 针对近距离无线通信(1) / 终端使用专门的硬件总线通过近距离无线通信(1) / 终端通过近距离无线通信(1) |
| NIST P-256 | 所有钥匙都在安全元件内基于椭圆曲线(1) |
| NIST Special Publication 800-108 | 位文件独有密钥通过密钥派生功能(1) |
| nsih | 清单哈希值(1) |
| Numerical Comparison | 数字比较(2) |
| OCSP | 信任评估可能需要在线证书状态响应(1) |
| OIC | 所有者身份证书(1) / 中还包含所有者身份证书(1) |
| OID | 这些限制使用叶证书中的特殊对象标识符(1) |
| OIK | 对所有者身份密钥(1) / 所有者身份密钥(1) |
| Option-Shift-K | 标志(1) |
| OS-bound | 公钥加速器会为操作系统绑定(1) |
| OTA | 但是如果采用无线(1) / 在无线(1) |
| OWE | 机会性无线加密(1) |
| PAC | 指针认证代码(1) |
| PAN | 借记卡卡号(1) |
| Passkey Entry | 和密钥接入(2) |
| PCI SSC | 采取方式与由支付卡产业安全标准委员会(1) |
| PDK | 密码派生密钥(2) |
| pf | 数据包过滤(1) |
| PII | 个人可识别信息(1) / 中的证件和证件读卡器之间交换的所有个人可识别信息(1) |
| PIN | 使用个人身份号码(1) |
| PIV | 在兼容的智能卡所附个人身份验证(1) |
| PKA | 公钥加速器(1) / 键盘配备了公钥加速器(1) |
| PKI | 如网络凭证和公用密钥基础设施(1) |
| PKINIT | 还支持使用密钥对(1) |
| PMF | 中定义的受保护的管理帧(1) |
| PPL | 页面保护层(2) / 中的页面保护层(1) |
| PQC | 密码学界一直致力于研究后量子密码学(1) |
| PRF | 密钥作为伪随机函数(1) |
| PRK | 储存以进行托管的个人恢复密钥(1) / 而应改用个人恢复密钥(1) |
| prot | 受信任启动策略测量值(1) |
| ronh | 随机数哈希值(1) |
| ROP | 堆栈中的函数返回地址来欺骗设备恶意执行现有代码的面向返回编程(1) |
| rpnh | 远程策略随机数哈希值(1) |
| SCEP | 简单证书注册协议(1) |
| SCIP | 系统协处理器完整性保护(4) |
| SDK | 软件开发套件(1) |
| SDXC | 扩展容量(1) |
| sepOS | 安全隔区固件(1) / 安全隔区还会执行安全启动过程来检查其软件(1) |
| Sigma | 每个会话均使用三阶段(1) |
| SIK | 服务器在制造期间基于芯片身份密钥(1) |
| SIP | 保存现有系统完整性保护(1) / 的启动或系统完整性保护(1) / 否则攻击者可能在其中关闭系统完整性保护(1) / 如果系统完整性保护(1) / 使用内核权限并通过叫做系统完整性保护(1) / 系统完整性保护(1) |
| sip0 | 系统完整性保护(1) |
| sip1 | 系统完整性保护(1) |
| sip2 | 系统完整性保护(1) |
| sip3 | 系统完整性保护(1) |
| SIS | 集成了学生信息系统(1) / 前提是该账户由手动创建或已与学生信息系统(1) |
| SKP | 密封密钥保护(9) / 封装密钥保护(1) |
| SLAAC | 使用无状态地址自动配置(1) |
| SMC | 电脑此前一直使用称为系统管理控制器(1) |
| SMDK | 系统测量设备密钥(1) |
| SMM | 系统管理模式(1) / 这种模式称为系统管理模式(1) |
| SMRK | 系统测量根密钥(1) |
| SoC | 片上系统(6) / 具体取决于片上系统(1) / 一组片上系统(1) |
| SPA | 引擎设计用于防范通过时序和静态功耗分析(1) |
| spih | 清单哈希值(3) |
| SPTM | 安全页表监控器(2) / 和安全页表监控器(2) / 中的安全页表监控器(1) |
| SRP | 集群利用安全远程密码(1) |
| SSID | 服务集标识符(1) |
| SSO | 支持通过单点登录(1) |
| SSV | 还负责验证签名系统宗卷(1) / 启动内核集和签名系统宗卷(1) / 签名系统宗卷(1) / 如启动内核集或签名系统宗卷(1) / 未将签名系统宗卷(1) / 系统宗卷通过签名系统宗卷(1) |
| stng | 代号(3) |
| STS | 相互认证的站对站(1) / 作为端对端(1) |
| STUN | 会话穿越实用程序(1) |
| TBPM | 受信任启动策略测量值(1) |
| TDM | 目标磁盘模式(1) |
| Team ID | 此验证过程通过团队标识符(1) |
| TKIP | 暂时密钥集成协议(1) |
| TLS | 发送设备会与接收设备建立一个加密的(1) |
| TRNG | 其硬件真随机数生成器(1) / 真随机数生成器(1) |
| TSF | 定时同步功能(1) |
| TSM | 开发者选择的可信服务管理平台(1) |
| TXM | 和可信执行监控器(1) |
| UAKL | 列表(2) |
| UART | 其访问仅限于模拟音频和一小部分串行(1) |
| ucrt | 用户身份证书(1) |
| UEFI | 统一可扩展固件接口(4) / 的代码符合统一可扩展固件接口(1) |
| UIK | 限制在用户身份密钥(1) / 验证硬件证明的用户身份密钥(1) / 用户身份密钥(1) |
| URI | 统一资源标识符(3) |
| URSK | 测距密钥(1) |
| UWB | 或者结合使用低功耗蓝牙和超宽带(1) / 如果车辆使用支持超宽带(1) |
| VEK | 同时也受到与宗卷加密密钥(1) |
| VIN | 例如车辆识别码(1) |
| VPN | 虚拟专用网络(6) / 像虚拟专用网络(1) |
| VT-d | 虚拟化技术(1) |
| WPA R3 | 个人级更新(1) |
## 附表 F｜攻击技术词汇在三层文本里的命中次数（本次实测）

检索口径：手册层＝179 页正文区的**中文词形**命中；措辞层 EN／ZH＝顾问页 149041 的**英文词形／中文词形**命中。两列不同语料不可直接比大小，只能比“有/无”与量级。

| 中文词 | 英文检索式 | 手册层（179 页正文） | 措辞层 EN（149041） | 措辞层 ZH（149041） |
|---|---|---:|---:|---:|
| 释放后使用 | `use after free|use-after-free` | 0 | 7 | 7 |
| 双重释放 | `double free` | 1 | 2 | 2 |
| 越界读写 | `out-of-bounds` | 0 | 18 | 0 |
| 缓冲区溢出 | `buffer overflow` | 1 | 4 | 4 |
| 堆缓冲区溢出 | `heap buffer overflow` | 0 | 2 | 2 |
| 栈溢出 | `stack overflow` | 0 | 1 | 1 |
| 整数溢出 | `integer overflow` | 0 | 3 | 3 |
| 类型混淆 | `type confusion` | 2 | 3 | 3 |
| 竞态条件 | `race condition` | 0 | 5 | 5 |
| 空指针 | `null pointer` | 0 | 1 | 1 |
| 未初始化 | `uninitialized` | 0 | 2 | 2 |
| 内存损坏 | `memory corruption` | 4 | 8 | 8 |
| 代码执行 | `arbitrary code` | 3 | 3 | 1 |
| 内核内存 | `kernel memory` | 3 | 12 | 12 |
| 提权（提升权限） | `privilege escalation|root privileges` | 0 | 2 | 0 |
| 沙盒 | `sandbox` | 27 | 4 | 4 |
| 授权 | `entitlement` | 174 | 2 | 8 |
| 路径遍历 | `path traversal` | 0 | 2 | 2 |
| 恶意制作 | `maliciously crafted` | 0 | 25 | 25 |
| 远程攻击 | `remote attacker` | 0 | 1 | 1 |
| 本地攻击 | `local attacker` | 0 | 2 | 2 |
| 中间人 | `man-in-the-middle|privileged network position` | 2 | 1 | 0 |
| 拒绝服务 | `denial-of-service|denial of service` | 1 | 5 | 0 |
| 意外终止 | `termination` | 0 | 39 | 39 |
| 崩溃 | `crash` | 6 | 1 | 1 |
| 持久化 | `persistent|persistence` | 0 | 2 | 0 |
| 侧信道 | `side channel` | 2 | 0 | 0 |
| 功耗分析 | `power analysis` | 2 | 0 | 0 |
| 直接内存访问 | `direct memory access|dma` | 13 | 0 | 0 |
| 利用链 | `exploit chain` | 0 | 0 | 0 |
| 零日 | `zero-day|zero day` | 0 | 0 | 0 |
| 恶意软件 | `malware` | 70 | 0 | 0 |
| 间谍软件 | `spyware` | 1 | 0 | 0 |
| 越狱 | `jailbreak` | 0 | 0 | 0 |
| 侧载 | `sideload` | 2 | 0 | 0 |
| 公证 | `notary` | 26 | 0 | 0 |
| 配置描述文件 | `configuration profile` | 14 | 0 | 0 |
| 代码签名 | `code sign` | 38 | 0 | 0 |
| 指针认证 | `pointer authentication|pac` | 5 | 0 | 0 |
| 地址空间布局随机化 | `aslr` | 5 | 0 | 0 |
| 内存标记 | `memory tagging|mte` | 8 | 0 | 0 |
| 即时编译 | `jit` | 1 | 0 | 0 |
| 网页内容 | `web content` | 0 | 3 | 3 |
| 媒体文件 | `media file` | 0 | 1 | 1 |
| 服务遭拒 | `denial-of-service|denial of service` | 0 | 5 | 5 |
| 门禁 | `gatekeeper` | 40 | 1 | 1 |
| 信息泄露 | `disclosure|disclose` | 0 | 8 | 2 |
| 隐私 | `privacy` | 318 | 5 | 6 |
| 访问敏感数据 | `sensitive user data` | 2 | 8 | 0 |

---

边界声明：本手册是一份面向防御与安全研究写作工作的用词对照工具。全文只使用 Apple 公开手册（《Apple 平台安全保护》zh-CN）与 Apple 安全响应顾问页的公开文本作为材料，交付内容为"官方在哪一层、用哪个词、说了什么、没说什么"。本手册不提供任何漏洞利用代码、不提供概念验证程序、不描述任何针对他人设备的操作步骤、不构造攻击链、不给出绕过任何具体防线的实施方法；第 4 章与附表 F 列出的攻击技术词汇只用于说明这些词在官方文本里的命中率（多数为 0 次），不构成对任何机型当前可被攻破的判断。凡本手册出现的"可以""能够""允许"，均为 Apple 原文措辞的直接引用，不代表本手册的可行性结论。本期所有计数只在 179 页指南正文区与 149041 / 149226 两份顾问页这四个文档的样本内成立，抽样范围变化时须按第 10 章口径重测，不作为全量顾问页的频率结论。

Telegram: https://t.me/ios_one001
