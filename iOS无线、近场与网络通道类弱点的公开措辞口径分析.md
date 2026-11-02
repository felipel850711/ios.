@@TITLE@@iOS 无线、近场与网络通道类弱点的公开措辞口径分析
@@SUBTITLE@@选题是"无线与配对通道专名（Wi-Fi、蓝牙、隔空投送、投屏、随航、VPN、DNS、IP 地址、USB 配件、配对）""近场与距离限定（proximity、nearby、射程、同一网络、附近设备）""网络与报文机制（network、packet、HTTP、信标、帧、SSID、广播、中继、隧道）"这三类措辞。三刀逐词测量给出六条反直觉结论。**第一，本篇有系列迄今最陡的字段梯度**：主集 428 段里 Impact 侧 **422** 段、Description 侧只有 **14** 段（**30∶1**），而刀一 124 段与刀二 41 段的 Description 侧**都是 0 段**——Wi-Fi、蓝牙、配对、配件、近场这些"通道名"在公开文本里**只出现在后果句，一次都没有出现在修复句**；14 个 Description 命中**全部落在刀三**。**第二，76 个判据句里两侧共用 0 句**：**73 句只在 Impact、3 句只在 Description**（这 3 句是 `An integrity issue was addressed with Beacon Protection.`、`This issue was addressed by using HTTPS when sending information over the network.`、`An authorization issue was addressed with improved network restrictions.`），句次轴 **422∶14∶0**；436 句次由 **35 个优先序桶**闭合分区、未归类 **0**，且 76 句**全部同时命中 ≥2 把句级刀**。**第三，两个最大的位置状语严格互斥**：`local network` **112** 段、`privileged network position` **93** 段，**同段 0**；在刀三 322 段里两者都不含的正好是 **117** 段（112＋93＋117＝322，加法自己合上了）。而 `privileged network position` 这 93 段**全部**写在两个句式桶里（attacker 版 80 ＋ user 版 13 ＝ 93，一句不漏）；`same network` 8 段同样 8∶8 全落进「An unauthenticated user on the same network…」一句。**第四，连字符口径在本篇零影响、词界断言仍在发作**：`Wi-Fi` 词界版、只认 ASCII hyphen 版、无词界版三者都是 **10** 段，全三刀重算"只认 ASCII hyphen"仍是 **428**（本篇没有任何 U+2010／U+2013 形连字符）；但去掉左词界主集涨到 **468（＋40）**、去掉右词界 **444（＋16）**；`frame` 加右界松匹配抓到 **33** 段却与主集**交 0**（33 段全是 `framework`）、`IP` 松匹配 33 段里 12 段是 `iPad／IPSec`。**第五，组件名与正文词双向背离，且方向相反**：组件名恰为 `AirPlay` 的 **84** 段里 **77** 段进主集，而正文真的写出 AirPlay 一词的只有 **7** 段（成员交 7）；组件名含 `802.1X` 的 8 段全部进主集、含 `Bonjour／mDNS` 的 74 段里 27 段进主集，可这两个词在正文三字段里**出现次数都是 0**；反向是 `USB／VPN／Relay`：作为组件名 **0** 段，作为正文词 **5／6／20** 段。**第六，协议专名在措辞层几乎整片缺席**：`802.11／WPA／WEP／EAP／Ethernet／DHCP／ARP／TCP／TLS／SSL／SSID／handshake／broadcast／multicast／unicast／advertis*／roaming／access point／quic／NFC／cellular／baseband／modem／CarPlay／Handoff／Continuity／Universal Clipboard／Instant Hotspot／mDNS／Bonjour／IPv4／IPv6 专形` 在正文三字段里全 0——附表 A 的 299 项判据里**并集全零 54 项**。本篇主集 **WNCH ＝ 428 段／135 页／50 名**，占全树 **4.68%**、占含 Impact 段的 **5.76%**；**三字段并集 441 段／138 页／53 名，比两字段多 13 段**，而这 13 段全部来自致谢字段里的机构名与域名（10 段 `Palo Alto Networks`、2 段 `Wireless`、1 段 `httpstorm.com`），**没有一段是真的网络通道条目**。
@@INFO@@报告日期|2026 年 11 月 2 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（258 份文件排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 7,434 段含 Impact / 9,132 段含致谢**。本篇主集 WNCH ＝ 刀一 `Wi-Fi／wifi 连字符族／Bluetooth／Bluetooth*／AirDrop／AirPlay／Sidecar／iPhone Mirroring／VPN／DNS／IP／USB／accessory／accessories／pair／pairs／paired／pairing／wireless／tunnel*` 或刀二 `proximity／nearby／in range／within…range／short-range／long-range／adjacent／same network／other devices／another device／second device／close proximity` 或刀三 `network*／packet*／HTTP*／beacon／frame／SSID／handshake／broadcast／multicast／advertis*／roaming／access point／mDNS*／Bonjour／relay*` 出现在 **Impact 或 Description** ＝ **428 段 / 135 页 / 50 名**，其中 Impact 侧 **422** 段、Description 侧 **14** 段；字段组合 **仅I 414／仅D 6／I∩D 8**（相加 428）；致谢字段命中 **17** 段（其中 4 段已在主集内），三字段并集 ＝ **441 段 / 138 页 / 53 名**。三刀定义（两字段口径）：刀一 **124** 段（Impact 124∶Description 0，I∩D 0，页 82、名 20）、刀二 **41** 段（41∶0，I∩D 0，页 33、名 5）、刀三 **322** 段（316∶14，I∩D 8，页 116、名 40）；三刀并集与合成词表 `$re3` 逐段相等，**差 0 段**。闭合分区（优先序 一→二→三，两两档已剔三刀同段）：仅一 **73**／仅二 **25**／仅三 **278**／一二共现 8／一三共现 36／二三共现 1／三刀同段 **7**，七档相加 ＝ 428。对照集：**R** ＝ #118 远程触达词族 **843 段 / 178 页 / 72 名**；**E** ＝ #114 在野集 **108 段 / 76 页 / 12 名**；**WO** ＝ #119 无前提集 **178 段**；**G／HI** ＝ 动名词开头集 **2,179 段** 与交互触发集 **273 段**；**M2** ＝ 同段 ≥2 后果类集 **185 段**；**UU** ＝ Impact 含 `user` **1,627 段**；**RC** ＝ #122 竞态集 **232 段**；**L** ＝ #123 逻辑集 **550 段**；**Ds** ＝ #124 拒绝服务集 **470 段**（复刻口径 **1,896 段**）；**X** ＝ #125 泄露集 **427 段**（复刻口径 **603 段**）；**BP** ＝ #126 绕过集 **301 段**；**WEB** ＝ #131 三刀并集 **1,253 段**；**AUTH** ＝ #136 三刀并集 **600 段**；**IMGX** ＝ #132 复刻口径 **367 段**；**MEDIA** ＝ #135 复刻口径 **251 段**；**ACE** ＝ 任意代码执行集 **343 段**；**remote** ＝ 远程词界集（仅 Impact）**257 段**。另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或利用链构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**"无线""局域网""蓝牙""配对""配件""隔空投送""投屏""随航""近场""射程""同一网络""网络位置""报文""信标""帧""中继""隧道""域名解析"这些说法在公开英文文本的哪个字段、哪一句位、与哪一个位置状语搭配，以及它与远程、在野、后果强度、组件名之间的集合关系**，属于措辞与文本计量分析；文中任何"蓝牙近场攻击""同网攻击者""特权网络位置""中继未生效"表述均指**公开条目使用了该词或该短语这一文本事实**，不是对该条目真实缺陷类别、CWE 归属、是否可被远程触达、是否被利用的判断，也不构成对 iOS 无线协议栈、蓝牙子系统或网络隔离模型安全性的结论。本篇尤其不涉及任何 Wi-Fi 关联、蓝牙配对、隔空投送可见性、AirPlay 认证的实际操作时序或参数构造
@@INFO@@分类|判据**六组 299 项 / 并集全零 54 项 / 非零 245 项**（附表 A，六组：本体词 173、句级刀 38、机制名与检测词对照 53、修复套话句 15、外部集对照 12、后果强度八类 8）/ **七十六个含主刀整句 × 归桶 × 句次 × I∶D × 页 × ∩R ∩E ∩M2 ∩L ∩RC**（附表 B，77 行含合计，句次合计 **436 ＝ Impact 422 ＋ Description 14**、页列合计 396）/ **三十五个句形桶 × 句次 × 段页名 × I∶D × ∩R ∩E，续表四档句式形态**（附表 C，36 行含合计，桶句次闭合 **436**、未归类 **0**、多重命中句 **76** 条＝全部判据句；续表 **The／This issue was addressed 10／A／An X issue was addressed 4／含 may 408／其余句式 14** 闭合 436）/ **页面、编号、致谢、版式与字段落位五类二十六行分箱**（附表 D，26 行，页八档闭合 135、编号两档闭合 428、致谢七档闭合 428）/ **中文层 78 词**（附表 E，八组：无线与局域网类 8、蓝牙与配件类 9、投送与投屏类 9、近场与距离类 9、网络与报文类 12、协议与隧道类 10、后果强度类 12、修复套话类 9；**全零 25 词、非零 53 词**）/ **组件层 50 名**（附表 F，51 行含合计，段合计 428、页列合计 332、这 50 名的 Impact 全集 **2,860** 段、占全树含 Impact 段的 **38.5%**）/ **十三把刀**：字段刀（Impact 422 ∶ Description 14 ∶ 致谢 17，两字段拆分 仅I 414／仅D 6／I∩D 8）、逐刀字段刀（刀一 124∶0、刀二 41∶0、刀三 316∶14，**三刀各自 I∩D ＝ 0／0／8**）、词族级落位刀（`network*` 290∶0 另有 6 段只在 Description、`HTTP*` 6∶10、`beacon` 0∶3、`local network` 112∶0、`privileged network position` 93∶0、`relay*` 20∶0、`pair*` 22∶0、`accessor*` 14∶0）、位置状语刀（`local network` 112 ∩ `privileged network position` 93 ＝ **0**，在刀三 322 段内两者都不含 117 段，加法闭合）、句库刀（436 句次只由 **76** 个不同句子写成，top1 占 **11.24%**、top3 **21.56%**、top5 **27.52%**、top10 **38.76%**、top20 **56.65%**、top50 **90.14%**）、标点刀（本篇**句号异形可合并组 ＝ 0**；正文实体 `&rsquo;` **0** 处、`&amp;` **0** 处、不换行空格 **0** 处、U+2019 **0** 处、直双引号 **16** 处）、连字符刀（`Wi-Fi` 三种口径 **10／10／10**，全三刀"只认 ASCII hyphen"重算 **428** 与主集逐段相同）、致谢刀（三字段并集比两字段多 **13** 段，13 段全是致谢字段里的机构名／域名：`Palo Alto Networks` 10 段、`Wireless` 2 段、`httpstorm.com` 1 段）、`network*` 摘除刀（整根撤掉刀三的 `network*` 后主集只剩 **179** 段，**−249**，即本篇 58.2% 的段是靠 `network` 这一个词进来的）、组件名刀（通道类组件名 **50** 个扛 428 段，Top5 名 **201** 段＝46.96%、Top10 名 **261** 段＝60.98%、单段名 **8** 个）、词界断言刀（去掉左词界 **468 段（＋40）**、去掉右词界 **444 段（＋16）**）、缺席刀（`SSID／handshake／broadcast／multicast／unicast／advertis*／roaming／access point／quic／frame 裸形／IPv4／IPv6 专形／802.11／802.1X／WPA／WEP／EAP／Ethernet／DHCP／ARP／TCP／TLS／SSL／Lightning／Thunderbolt／NFC／cellular／baseband／modem／CarPlay／Handoff／Continuity／Universal Clipboard／Instant Hotspot／hotspot／mDNS／Bonjour／close proximity／peer／adjacent／near／in range 裸形／Nearby Interactions／other-another-second device／Core Bluetooth 专名` **正文三字段口径全 0**，合并对照后共 54 项全零）
@@INFO@@难度|进阶（本篇的 428 段有**四套分母**：占全树 9,136 段是 **4.68%**、占含 Impact 的 7,434 段是 **5.76%**、占其所在 135 页全部 7,767 段是 **5.51%**（这 135 页每页平均 57.5 段，本篇每页平均 **3.17** 段），三个数都真、不可混用；第四套是**版式域**分母——域 A **427／9,045 ＝ 4.72%**、域 B **1／91 ＝ 1.10%**，**本篇在粗体版式域只有 1 段**（页 `120303`、组件名 Bluetooth），域 B 占比是域 A 的 **23%**，形态接近 #134（域 B 显著偏低）而与 #136（两域几乎相同）相反；**428 是两字段主集、441 是三字段并集、436 是判据句次、422 与 414 是两个数**（422 是 Impact 侧段数，414 是"仅 Impact"段数，二者差 8＝I∩D）。428 段里 **420 段只含 1 个本篇判据句、8 段含 2 句、0 段一句判据都没有**，所以判据句次比段数多 8；这 8 个两句段**恰好就是 I∩D 那 8 段**（一句 Impact ＋一句 Description），而**仅 Description 的 6 段各写 1 句**，所以 Description 侧判据句次 14 ＝ I∩D 8 ＋ 仅D 6。**凡"A 与 B 得数相同"处必须同时报 \|A\|、\|B\|、\|A∩B\|**——第 15 章的成员级核验结论是：**`nearby` 3、`AirDrop` 3、`beacon` 3 三个"得数 3"两两成员交全 0**、**`刀一∩刀二` 8 段与 `I∩D` 8 段成员交 0**、**`same network∩主集` 8 段与 `I∩D` 8 段成员交 0**、**`bypass∩主集` 28 与 `#126 BP∩主集` 28 成员交 28（真同集）**、**`组件名 AirDrop` 20 与 `正文 AirDrop 词` 3 成员交 3**、**`正文 AirDrop 词` 3 与 `relay 词族` 20 成员交 0**、**`组件名 Wi-Fi` 88（∩主集 41）与 `正文 Wi-Fi 词` 10（∩主集 10）成员交 10**；附表 B、C、F 的页列与名列是逐行去重后的再求和，**不得当作并集去重值**（B 页列 396、C 页列 320／名列 83、F 页列 332，并集真值是 135 页／50 名）；附表 B 与 F 的外部集列同理是**逐行和**：B 的 ∩R 行和 **388** 对并集真值 **380**、∩E／∩M2 两列行和 **4／0** 与真值相同、∩L／∩RC 行和 **39／11** 与真值逐列相同；F 的 ∩R／∩E／∩M2／∩L／∩BP 行和 **380／4／0／39／28** 与真并集**逐值相同**（组件名是段的单值属性）。附表 A「后果强度八类」∩主集列是**三字段口径**（八类相加 **255**、并集 **248**、零类段 **180**、≥2 类段 7），第 9 章正文另给 **Impact 单字段口径**（相加 **220**、并集 **220**、零类段 **208**、≥2 类段 0），两套口径都不是主集段数 428；附表 A 的句级刀 ∩主集列是**段级命中**、附表 C 的桶句次相加 **436** 是**句次轴**，两者不可互换（35 桶的段列相加 436、页列相加 320 都会超过 428／135，因为一句可以跨段、一段可以跨页））
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

前三篇措辞层分析挑的是"数据本体"：#132 挑图像与字节的格式解析、#133 挑资源边界、#135 挑音视频与媒体处理。这一篇挑的是**通道**——不是"处理了什么东西"，而是"通过哪条路进来的"。Apple 在公开条目里描述攻击者的**位置**时，可用的说法并不多：`on the local network`、`in a privileged network position`、`in physical proximity`、`in wireless proximity`、`with a paired Bluetooth device`、`on the same network`、`remote`。本篇把这三组说法做成三把刀，然后回答四个问题。

**问题一：这些通道词写在哪个字段。** 这是本篇最干净的一个发现。刀一（Wi-Fi／蓝牙／AirDrop／AirPlay／USB 配件／配对／VPN／DNS／IP／隧道）124 段，Description 侧 **0** 段；刀二（近场与距离限定）41 段，Description 侧 **0** 段；只有刀三（network／packet／HTTP／beacon／relay）在 Description 侧有 **14** 段，而这 14 段里只有 3 个不同的句子。换句话说：**"通过哪条通道"这件事在公开文本里只被写在后果句里，几乎从不被写进修复句**——修复句写的是"改了状态管理""加了校验""改用 HTTPS"。第 6 章给完整拆分。

**问题二：位置状语之间是不是同义词。** `local network` 与 `privileged network position` 在中文里都可以写成"局域网内"或"网络位置优越的攻击者"，但它们在主集里**一段都不共现**：112 段与 93 段，交 0，而且这两个数加起来再加上"两者都不含"的 117 段，正好等于刀三的 322 段。第 8 章量这一把刀。

**问题三：写词和挂名是不是一回事。** 组件名叫 `AirPlay` 的段有 84 段（其中 77 段进主集），正文里真写出 AirPlay 这个词的段只有 7 段；反过来，组件名含 `Bonjour／mDNS` 的 74 段里 27 段进了主集，但 `mDNS` 与 `Bonjour` 这两个词在全部三字段里的出现次数是 **0**。第 14 章给一个双向背离表。

**问题四：协议的专名到底写不写。** 这是本篇附表 A 里"全零"最多的一篇（54 项）。`802.11`、`WPA`、`EAP`、`SSID`、`handshake`、`broadcast`、`multicast`、`roaming`、`access point`、`TCP`、`TLS`、`NFC`、`baseband`、`CarPlay`、`Handoff`、`Continuity`、`mDNS`、`Bonjour` 在正文里**一次都没出现过**。公开措辞谈无线的方式是"蓝牙／配件／配对／near／range／local network"这一层，而不是协议规范那一层。

必须先把话说清楚：**本篇统计的是文本，不是漏洞**。一个条目写 `An attacker on the local network…` 只能说明 Apple 在这一段用了这句话，不能证明该缺陷只能在局域网内触发，也不能证明它不能从远程触达；反过来，一段没写网络限定，也不代表它真的不需要网络。Apple 自己在页面顶部的声明里就写了"为了保护用户，我们不披露、不讨论、不确认安全缺陷"。第 15 章逐条做成员级核验，就是为了防止把"得数相同"读成"同一批段"。

---

# 1 口径、判据与四套分母

## 1.1 语料与读取

沿用 #111 第 1 章确立的**两版式域**读取：镜像里 258 份 HTML 排除页面 `122085`（该页是聚合页，段会被重复计入）后得 **257 份页面文件**；其中 246 页是标准版式（域 A），每段以 `<h3 class="gb-header">` 起、由四个结束锚点之一收块，得 **9,045 段**；另 11 页是粗体段版式（域 B），以粗体段起块，得 **91 段**。两域相加 **9,136 段**，字段完整率：含 Impact **7,434** 段、含 Description **9,136** 段、含致谢 **9,132** 段、含组件名 **9,136** 段。组件名按整条 `nm` 字符串计数（不按逗号拆原子），得 **472** 个名字。这套分母与 #122 到 #136 完全一致，锚点漂移检查在装载脚本里硬编码，任一数字不符就 `die`。

## 1.2 三刀的词表

三把刀都是**段级**判据，词表内所有项都带 `(?<![\w-])…(?![\w-])` 双侧词界，连字符类用宽字符集 `$D = [-\x{2010}\x{2011}\x{2012}\x{2013} \x{a0}\x{202f}]`，空格类用 `$S = [ \x{a0}\x{202f}]`。

| 刀 | 覆盖 | 词表（正则交替项） | 两字段并集 |
|---|---|---|---|
| 刀一 | 无线与配对通道专名 | `Wi-Fi`／`wifi` 连字符族、`Bluetooth`、`AirDrop`、`AirPlay`、`Sidecar`、`iPhone Mirroring`、`VPN`、`DNS`、`IP(?:v[46])?`、`USB`、`accessor(?:y\|ies)`、`pair(?:ing\|ed\|s)?`、`wireless`、`tunnel\w*` | **124 段 / 82 页 / 20 名** |
| 刀二 | 近场与距离限定 | `proximity`、`nearby`、`in range`、`within…range`、`short-range`、`long-range`、`adjacent`、`same network`、`other devices`、`another device`、`second device`、`close proximity` | **41 段 / 33 页 / 5 名** |
| 刀三 | 网络与报文机制 | `network\w*`、`packet\w*`、`HTTP\w*`、`beacon`、`frame`、`SSID`、`handshake`、`broadcast`、`multicast`、`advertis\w+`、`roaming`、`access point`、`mDNS\w*`、`Bonjour`、`relay\w*` | **322 段 / 116 页 / 40 名** |

合成词表 `$re3`（三刀并集）＝ **428 段 / 135 页 / 50 名**，与"逐刀并集"做成员级比对**差 0 段**。主集记作 **WNCH**。

## 1.3 七档闭合分区

优先序固定为 一→二→三，两两档已剔除三刀同段，所以七档互斥且相加即主集：

| 档 | 段 | 占 428 |
|---|---:|---:|
| 仅刀一 | 73 | 17.06% |
| 仅刀二 | 25 | 5.84% |
| 仅刀三 | 278 | 64.95% |
| 刀一∩刀二（不含刀三） | 8 | 1.87% |
| 刀一∩刀三（不含刀二） | 36 | 8.41% |
| 刀二∩刀三（不含刀一） | 1 | 0.23% |
| 三刀同段 | 7 | 1.64% |
| **相加** | **428** | 100% |

七档两两成员交恒为 0（这是构造性质），因此**本篇的加法关系可以当恒等式用**：`|刀一| ＝ 73 ＋ 8 ＋ 36 ＋ 7 ＝ 124`、`|刀二| ＝ 25 ＋ 8 ＋ 1 ＋ 7 ＝ 41`、`|刀三| ＝ 278 ＋ 36 ＋ 1 ＋ 7 ＝ 322`。第 2、3、4 章的"摘刀差值"就是拿这三条恒等式反过来验的。

## 1.4 四套分母

428 段可以被四个不同的总体除，四个数都真，混用会得出互相矛盾的百分比：

1. **全树段分母**：428／9,136 ＝ **4.68%**。
2. **含 Impact 段分母**：428／7,434 ＝ **5.76%**。这是本篇最常用的一个，因为主集里 422／428 ＝ 98.6% 的段本来就有 Impact 字段。
3. **所在页段分母**（第三套）：428 段落在 135 页上，这 135 页一共 7,767 段，428／7,767 ＝ **5.51%**，本篇在这 135 页上平均每页 **3.17** 段。这一套回答的是"这些页面是不是被本篇占满了"——答案是否定的，最"满"的一页也只有 21／74 段。
4. **版式域分母**（第四套）：域 A 427／9,045 ＝ **4.72%**，域 B 1／91 ＝ **1.10%**。

另外两个"看着像分母但不是"的数：**875** 是主集 428 段的**全部**句次（不分判据，Impact 432 ＋ Description 443），每段均 2.04 句；**436** 是主集里**含三刀词的判据句**句次（Impact 422 ＋ Description 14）。第 7 章专门处理这两根轴的换算。

## 1.5 编号、致谢与中文层

编号（CVE 与内部编号）只作字段存在性统计，不用于判定缺陷类别：本篇 428 段**全部**带至少一个编号（428／428），distinct 编号 **111** 个，其中 **111 个只出现在主集段内**、与主集外共享 **0** 个。致谢字段按"逗号切分后非空计数"，本篇人次 **855**、均值 **2.00**、最大 **15**（全树均值 1.80、最大 37）。中文层用《Apple 平台安全保护》zh-CN 转储的 179 页，窗口切分后 **178 页可用**，附表 E 的 78 个中文词全部是在这 178 页上做子串计数，与英文判据层是**两套独立的分母**。

---

# 2 刀一：无线与配对通道专名 124 段，Description 侧 0 段

## 2.1 总量与字段

刀一 **124 段 / 82 页 / 20 名**。字段拆分：

| 指标 | 值 |
|---|---:|
| 两字段并集 | 124 |
| Impact 侧 | **124** |
| Description 侧 | **0** |
| I∩D | 0 |
| 仅 I／仅 D | 124／0 |
| 页／名 | 82／20 |

**124∶0 是一个整段侧零**。把 Description 字段整个从判定里去掉，刀一段数一根毛都不会少；把 Impact 字段去掉，刀一直接归零。这不是"Description 里少写"，是**一次都没写**。

## 2.2 词族明细（三字段口径，附表 A）

| 词族 | 全树段 | ∩主集 | 说明 |
|---|---:|---:|---|
| `Bluetooth` 词界 | 32 | 32 | 全树 32 段全部落在主集内 |
| `Bluetooth*` 词族 | 36 | 32 | 多出的 4 段在主集之外（派生形只落在致谢字段） |
| `Bluetooth device` 短语 | 10 | 10 | |
| `Bluetooth packets` 短语 | 5 | 5 | 走刀三共词 |
| `Bluetooth pairing` 短语 | 1 | 1 | |
| `IP address` 短语 | 21 | 21 | `IPv4／IPv6` **专形 0** |
| `pair` 四形 | 22 | 22 | 无左界宽松版 24（`repair／compare` 污染 2 段） |
| `paired／unpaired` | 14 | 14 | |
| `wireless` 词界 | 13 | 11 | 仅 Impact 8、仅致谢 5（**单字段绑定**） |
| `wireless proximity` | 8 | 8 | 刀一∩刀二的全部 8 段 |
| `accessor(y\|ies)` | 14 | 14 | `malicious accessory` 12 段 |
| `Wi-Fi` | 10 | 10 | 三种连字符口径全同，见第 5 章 |
| `AirDrop` | 3 | 3 | |
| `AirPlay` | 7 | 7 | 与组件名 AirPlay 77 段对照，见第 14 章 |
| `USB` | 5 | 5 | `USB Restricted Mode` 5 段 |
| `VPN`／`tunnel*` | 6 | 6 | `VPN tunnel` 6 段 |
| `DNS` | 7 | 7 | 全部是 `DNS queries` |
| `Sidecar` | 2 | 2 | |
| `iPhone Mirroring` | 6 | 6 | |

`Wi-Fi／AirDrop／AirPlay` 三个专名合并 10 段、`pairing key` 1 段。

## 2.3 全零的通道名（本篇最重要的一张负结果表）

刀一的词表在附录里被扩成了一个"通道名词族"对照集，其中**在正文三字段里出现 0 次**的有：

`CarPlay`、`Handoff`、`Continuity`、`Universal Clipboard`、`Instant Hotspot`、`hotspot`、`NFC`、`Near Field`、`cellular`、`baseband`、`modem`、`mDNS`、`Bonjour`、`802.11`、`802.1X`、`WPA／WPA2／WPA3`、`WEP`、`EAP`、`Ethernet`、`DHCP`、`ARP`、`TCP`、`TLS`、`SSL`、`Lightning`、`Thunderbolt`、`Core Bluetooth` 专名、`IPv4／IPv6` 专形。

这份清单值得逐条看。第一，**连续互通的官方产品名一个都不写**：`Handoff`、`Continuity`、`Universal Clipboard`、`Instant Hotspot` 全 0，而中文层"接力"出现 **212** 次、"连续互通"出现 **362** 次（附表 E）——手册层大量谈这些功能名，措辞层一条都不挂。第二，**蜂窝侧完全缺席**：`cellular／baseband／modem／NFC` 全 0，所以"无线"这一刀实际上只覆盖 Wi-Fi／蓝牙／配件这三条路。第三，**协议专名不写**：`802.11／WPA／EAP／SSID` 全 0，但组件名里 `802.1X` 有 8 段（第 14 章量这个错位）。

## 2.4 摘刀差值

从主集 428 段里做三种减法，用来核对七档表：

| 操作 | 结果段 | 期望 |
|---|---:|---|
| 剔掉**所有含刀一**的段 | 304 | 428 − 124 ＝ 304 ✓ |
| 剔掉**仅含刀一**的段 | 355 | 428 − 73 ＝ 355 ✓ |
| 刀一与其他刀**共现**的段 | 51 | 124 − 73 ＝ 51 ✓ |

三条恒等式全部对上，说明刀一定义与主集定义之间没有隐藏的第三态。

---

# 3 刀二：近场与距离限定 41 段，最薄的一根刀

## 3.1 总量与字段

刀二 **41 段 / 33 页 / 5 名**，Impact **41** ∶ Description **0**，I∩D 0，仅I 41／仅D 0。41 段是本篇三刀里最小的一根，也是**第二个 Description 侧整段零**。

`physical proximity` 22 段、`proximity` 词界 30 段（差额 8 段全是 `wireless proximity`）、`same network` 8 段、`nearby` 3 段、`within…range`（`Bluetooth range`）1 段、`in…range` 11 段但**∩主集只有 2**。裸词 `device` 全树 207 段、∩主集 39；`close proximity／peer／adjacent／near／in range 裸形／Nearby Interactions／other-another-second device` 全 0。

## 3.2 为什么 `in…range` 11 段只交进 2 段

`in…range` 这个短语形本身宽松（`in range`、`in the range of`、`in Bluetooth range` 都会命中），11 段里有 9 段的上下文是音频、电池、显示等非通道路径，只有 2 段真的是"射程"义并且带上了刀三或刀一的词。这一条是"短语形不是语义"的典型：**匹配到 11 段不等于有 11 个近场条目**。本篇没有把 `range` 裸词收进刀二（收了会把 11 段全拉进来），只收了 `within…range` 的专形 1 段。

## 3.3 摘刀差值与刀一∩刀二

| 操作 | 结果段 | 期望 |
|---|---:|---|
| 剔掉所有含刀二的段 | 387 | 428 − 41 ✓ |
| 剔掉仅含刀二的段 | 403 | 428 − 25 ✓ |
| 刀二共现段 | 16 | 41 − 25 ✓ |

刀一∩刀二共 **8** 段，这 8 段**写的是同一句 Impact**：`An attacker in wireless proximity may be able to cause unexpected system termination`。所以"两根刀共现"在这里不是"两种通道叠加"，而是**一句话里同时出现了 `wireless`（刀一）与 `proximity`（刀二）**。8 段全部来自蓝牙配件类条目，`unexpected system termination` 是它们唯一的后果写法。

刀二∩刀三只有 **1** 段（页 `125693`、组件名 Compressor）：`An unauthenticated user on the same network as a Compressor server may be able to execute arbitrary code`——`same network` 归刀二、`network` 归刀三，这是同一段里"限定语"与"本体词"重复计数的唯一一处。

---

# 4 刀三：网络与报文机制 322 段，本篇的主体

## 4.1 总量与字段

刀三 **322 段 / 116 页 / 40 名**，Impact **316** ∶ Description **14**，I∩D **8**，仅I 308／仅D 6。

**本篇全部 14 个 Description 侧命中都在刀三**，全部 8 个 I∩D 段也在刀三。刀一、刀二在 Description 字段是彻底的空集。

## 4.2 词族明细

| 词族 | 全树段 | ∩主集 | 字段侧 |
|---|---:|---:|---|
| `network／networks` 词界 | 306 | 296 | ∩主集 296 段中 **290** 段该词写在 Impact、**6** 段只在 Description；另 10 段只在致谢 |
| `network*` 词族（含 networking） | 296 | 296 | 仅致谢 10 段 |
| `local network` | 112 | 112 | 全 Impact |
| `Local Network` 权限专名（大写形） | 8 | 8 | 全 Impact |
| `privileged network position` | 93 | 93 | 全 Impact |
| `network traffic` | 39 | 39 | |
| `network position` | 93 | 93 | 与上行同集 |
| `packet／packets` | 20 | 20 | |
| `network packet` | 8 | 8 | `crafted packets` 5 |
| `network share` | 18 | 18 | SMB 27∩9、AFP 5∩3 |
| `network connection` | 14 | 14 | |
| `network settings` | 10 | 10 | |
| `network restrictions` | 8 | 8 | |
| `network server` | 6 | 6 | |
| `HTTP*` | 16 | 16 | **仅 Impact 6／仅 Description 10** |
| `HTTPS` 专形 | 16 | 16 | |
| `relay*` | 20 | 20 | 全部是 `Private Relay`，无其他用法 |
| `beacon／beacons` | 3 | 3 | **全 Description** |
| `IPSec／IKE` | 2 | 2 | |
| `frame` 裸形 | **0** | 0 | 无右界版 33 段∩主集 **0**（全是 `framework`） |
| `SSID／handshake／broadcast／multicast／unicast／advertis*／roaming／access point／quic` | 0 | 0 | |

## 4.3 `network*` 一根词就占了本篇的 58%

把刀三里的 `network*` 单独摘掉（保留 packet／HTTP／beacon／relay 等），主集从 428 掉到 **179**，**−249 段**。也就是说**本篇 58.2% 的段是靠 `network` 这一个词进来的**。这既是它成为"通道类"主集的原因，也是它的风险：`network` 是一个语法上极自由的词，它同时出现在位置状语（`on the local network`）、机制名（`network settings`）、后果句（`bypass network filters`）和修复句（`improved network restrictions`）里。第 8 章把位置状语单独做成一把刀，就是为了看这 249 段内部到底被哪些句式分掉了。

另外三根摘刀：去 `IP` 词界 → **418**（−10）；去 `relay*` → **426**（−2）；去 `proximity` → **406**（−22）。`relay*` 只减 2 段是因为 20 段 `Private Relay` 里 18 段同时也含 `network` 或 `IP`，这两段的独立贡献被重叠吃掉了。

## 4.4 摘刀差值

| 操作 | 结果段 | 期望 |
|---|---:|---|
| 剔掉所有含刀三的段 | 106 | 428 − 322 ✓ |
| 剔掉仅含刀三的段 | 150 | 428 − 278 ✓ |
| 刀三共现段 | 44 | 322 − 278 ✓ |

106 段是"完全不含网络报文机制词"的通道条目，它们的通道身份全部由刀一／刀二提供（`Bluetooth`、`accessory`、`pair`、`proximity`），这正是 4.3 里"58% 靠一个词"的另一面。

## 4.5 三字段并集与致谢侧的 13 段

致谢字段命中 **17** 段，其中 4 段本来就在主集内（`Bluetooth／paired／wireless` 出现在致谢行的段落），**主集之外新增 13 段**。三字段并集因此是 **441 段／138 页／53 名**。

把这 13 段逐段读下来，结论很干脆：**没有一段是网络通道条目**。

| 命中词 | 段数 | 实际来源 |
|---|---:|---|
| `Networks` | 10 | 机构名 `Palo Alto Networks`（WebKit Bugzilla 致谢行的公司名） |
| `Wireless` | 2 | 机构／项目名里的 `Wireless` |
| `httpstorm` | 1 | 域名 `httpstorm.com`（`HTTP` 松匹配） |

这 13 段全部是**专名内部藏着通道词**造成的假阳性。#135 那篇写过"三字段并集＝两字段主集"是系列第一次，本篇则是另一种情形：**并集比主集大，但多出来的每一段都可以逐段指认为污染**。因此本篇主集坚持用两字段口径 **428**，第 12 章把 13 段的成因逐条列出。
---

# 5 词界与连字符：`Wi-Fi` 的三种口径给出同一个 10

## 5.1 连字符刀：本篇唯一的"零影响"口径

`Wi-Fi` 这个词在英文文本里有五种可写形态：ASCII 连字符 `Wi-Fi`、不换行连字符 U+2011、短横 U+2013、无连字符 `WiFi`／`wifi`。本篇的宽口径用 `$D{0,1}`（`$D` ＝ `[-\x{2010}\x{2011}\x{2012}\x{2013} \x{a0}\x{202f}]`）覆盖全部，附表 A 因此给了三行对照：

| 口径 | 全树段 | ∩主集 |
|---|---:|---:|
| `Wi-Fi` 宽词界（`Wi$D{0,1}Fi`） | 10 | 10 |
| 只认 ASCII hyphen `Wi-Fi` | 10 | 10 |
| 完全无词界 `wi$D{0,1}fi`（大小写不敏感） | 10 | 10 |

三行**逐值相同**。更强的一条：把三刀全部词表重算、只把连字符换成 ASCII hyphen，主集仍是 **428 段／135 页／50 名**——**逐段相同，不是得数相同**。所以本篇是系列里少见的"连字符口径完全无害"的一篇：镜像里没有异形连字符，`WiFi` 这种无连字符写法**一次都没出现**（附表 E 的中文层对照里 `WiFi` 也是 0 次）。

这条负结果本身就是结论：**不要把宽字符集当成"更严谨"**。在本篇它一个字节都没多抓；在 #132（`out-of-bounds`）和 #133 它却决定了 193 段的进出。口径要按词族逐个测，不能全局统一设一次就交付。

## 5.2 词界断言仍然在发作

连字符无害，不等于词界无害。四行对照：

| 反事实 | 段 | 与主集差 |
|---|---:|---:|
| 主集（双侧词界） | 428 | — |
| 去掉左词界 `(?<![\w-])` | **468** | ＋40 |
| 去掉右词界 `(?![\w-])` | **444** | ＋16 |
| `Wi-Fi` 只认 ASCII hyphen | 428 | 0 |

去左界的 ＋40 段主要来自 `unpaired／repair／unauthorized` 这类前挂词，去右界的 ＋16 段来自 `networking／packets` 之外更冷的派生。两个方向都还给了**同一个 ∩主集 428**（因为松口径把已经进来的段重复覆盖），所以只看"∩主集"列会以为没差异——差异全在"并集段"列。

## 5.3 三处松匹配的具体污染

| 松口径行 | 全树 | ∩主集 | 污染来源 |
|---|---:|---:|---|
| `pair` 无左界 | 24 | 22 | `repair／compare` 2 段 |
| `IP` 无右界 | 33 | 32 | `iPad／IPSec` 12 段（紧口径 21） |
| `frame` 无右界 | 33 | **0** | 33 段**全部**是 `framework` |
| `local` 裸词界 | 237 | 112 | `local` 单独作形容词、非 `local network` |
| `device` 裸词界 | 207 | 39 | 泛指设备，非"另一台设备"义 |
| `range` 裸词界 | 11 | 2 | 见 3.2 |

`frame` 这一行最值得抄进方法学：**裸形 `frame` 全树 0 段**（正文从不单独写"帧"），**无右界版 33 段**，**而这 33 段与主集的交集是 0**。这是一个"松匹配的全部命中都是假阳性"的极端样本。同理 `proximity 无词界` 与紧口径同为 30 段（因为 `proximity` 前面只会是空格），所以这一行是零差异对照——两行同数但成因不同，必须分别读。

## 5.4 摘除单根词的差值汇总

| 摘除项 | 剩余主集 | 差 |
|---|---:|---:|
| 去 `network*`（刀三主体） | 179 | **−249** |
| 去 `proximity`（刀二主体） | 406 | −22 |
| 去 `IP` 词界 | 418 | −10 |
| 去 `relay*` | 426 | −2 |

---

# 6 字段刀：Impact 422 ∶ Description 14，系列最陡的一次

## 6.1 拆分

| 指标 | 段 |
|---|---:|
| 主集两字段 | 428 |
| Impact 侧（CI） | **422** |
| Description 侧（CD） | **14** |
| 仅 I | 414 |
| 仅 D | **6** |
| I∩D | **8** |
| 相加校验 414＋6＋8 | 428 ✓ |

**422∶14 ＝ 30.1∶1**。措辞层系列十三篇里，前面的篇目大多是"两侧都写但侧重不同"（#136 是 243∶443，反向；#133 是词族随字段单侧绑定但总量对得上）。本篇是唯一一根"通道名不进修复句"的分布：**刀一 124∶0、刀二 41∶0，只有刀三 316∶14**。

## 6.2 Description 侧的 14 段是什么

逐段读，14 段一共只写了 **3 个不同的句子**：

| Description 句 | 段数 | 字段组合 |
|---|---:|---|
| `This issue was addressed by using HTTPS when sending information over the network.` | 5 | I∩D |
| `An integrity issue was addressed with Beacon Protection.` | 3 | I∩D |
| `This issue was addressed by using HTTPS when sending information over the network.` | 5 | 仅 D |
| `An authorization issue was addressed with improved network restrictions.` | 1 | 仅 D |

即 **I∩D 8 段 ＝ 3×Beacon ＋ 5×HTTPS**、**仅 D 6 段 ＝ 5×HTTPS ＋ 1×network restrictions**，两句 HTTPS 合计 10 段、Beacon 3 段、restrictions 1 段 ＝ 14 ✓。

这 14 段是本篇信息量最高的一段文本：**Apple 在整个网络通道词族里，只在三种修复说法里写了通道词**——"改用 HTTPS 发送"、"用信标保护修完整性"、"收紧网络限制"。前两种是**协议级动作**，第三种是**策略级动作**，没有一种写"修了 Wi-Fi 关联状态机"或"改了配对流程"。

## 6.3 词族级的单字段绑定

附表 A 里五组"仅某字段"对照行，逐条给出绑定方向：

| 词族 | Impact 侧段 | Description 侧段 | 致谢侧段 |
|---|---:|---:|---:|
| `HTTP*` | **6** | **10** | — |
| `beacon／beacons` | 0 | **3** | 0 |
| `network*` | 290 | 6 | 10 |
| `wireless` | 8 | 0 | 5 |
| `Bluetooth`／`pair*`／`accessor*`／`proximity`／`relay*`／`local network`／`privileged network position` | 各自全数 | **0** | 0 |

`HTTP*` 是唯一一个 **Description 侧多于 Impact 侧**的词族（10∶6），而它多出来的那 10 段全部是那两句 HTTPS 修复句。这是"字段梯度"唯一的例外，而且例外本身只有两句话的量。

## 6.4 段数、句次、字段三个轴不能互换

- **段轴**：主集 428 段。
- **句轴（不分判据）**：428 段一共写出 **875** 个句子（Impact 432 ＋ Description 443），每段均 **2.04** 句——这是"一条目一句后果一句修复"的模板形态。
- **句轴（判据句）**：**436** ＝ Impact 422 ＋ Description 14。因为 **420 段只含 1 个判据句、8 段含 2 句、0 段一个都没有**。
- **字段轴**：422 与 414 是两个数（前者含 I∩D 8 段，后者不含）。

875 与 436 相差 439，这 439 句是**不含任何三刀词的句子**（主要是那些不带通道词的修复句，如 `The issue was addressed with improved memory handling.`）。**875 不是本篇的任何"句库规模"，436 才是**（第 7 章）。

---

# 7 句层：76 个句子写完 436 句次，两侧共用 0 句

## 7.1 规模与集中度

| 指标 | 值 |
|---|---:|
| distinct 判据句 | **76** |
| 判据句次 | 436 |
| top1 占比 | **11.24%**（49 句次） |
| top3 | 21.56% |
| top5 | 27.52% |
| top10 | 38.76% |
| top20 | 56.65% |
| top50 | **90.14%** |

top1 是 `An attacker on the local network may be able to cause a denial-of-service`，**49 句次、28 页**。76 句里最后 26 句合起来只占 436 句次的 9.86%，长尾非常陡：**中位句子只出现 1～2 次**。

## 7.2 两侧共用 0 句

| 句归属 | distinct 句 | 句次 |
|---|---:|---:|
| 只在 Impact | **73** | 422 |
| 只在 Description | **3** | 14 |
| 两侧共用 | **0** | **0** |
| 合计 | 76 | 436 |

**73 ＋ 3 ＋ 0 ＝ 76 ✓，422 ＋ 14 ＋ 0 ＝ 436 ✓**。这是本篇最干净的一条闭合：**没有任何一个句子同时被写在 Impact 字段和 Description 字段**。#136 是 63∶22∶0（共用也是 0），本篇 73∶3∶0——共用为 0 在字段梯度极陡时是必然的（修复句和后果句的句式模板不同），但"3 句写完 14 段"这个比例是本篇独有的：**Description 侧的通道词全部是三句套话**。

## 7.3 38 把句级刀、35 个桶、多重命中 76

附表 C 的桶由 **38 把句级刀**（首匹配优先序 ＋ 一把覆盖全树的兜底刀）构成。跑出来的三个数：

- **启用桶数 35**（38 把刀里 3 把在本篇没有句落位）。
- **未归类句 0**。
- **多重命中句 76 条 ＝ 全部判据句**。

第三条需要解释，否则容易被读成"分类失败"。因为 38 把刀里有一把是"其余含三刀词句"（覆盖全树 9,136 段，∩主集恰为 428），**任何**含三刀词的句子都至少命中它 ＋ 一把专刀，所以"≥2 把"是构造性的。**这正是本篇必须用优先序桶做闭合分区、而不能直接报"命中某刀句次"的原因**：命中数相加会重复计数，只有优先序首匹配才让 35 桶句次相加严格等于 436。

## 7.4 句首形态与情态（附表 C 续表）

| 形态刀 | 句次 | 段 | 页 | 名 |
|---|---:|---:|---:|---:|
| `The／This issue was addressed …` 打头 | 10 | 10 | 9 | 2 |
| `A／An X issue was addressed …` 打头 | 4 | 4 | 4 | 2 |
| 含 `may`（影响句情态） | **408** | 408 | 134 | 49 |
| 其余句式 | 14 | 14 | 11 | 3 |
| **合计** | **436** | | | |

**408／436 ＝ 93.58% 的判据句带 `may`**。剩下 28 句次里 14 句是修复句（两类 `was addressed`），14 句是"其余句式"。这 14 句次**全部逐句可指认，没有剩余**：`An unauthenticated user on the same network as a signed-in Mac could send it AirPlay commands without pairing` **7 句次**、`A malicious app acting as a HTTPS proxy could get access to sensitive user data` **6 句次**、`When your headphones are seeking a connection request to one of your previously paired devices, an attacker in Bluetooth range might be able to spoof the intended source device and gain access to your headphones` **1 句次**——7 ＋ 6 ＋ 1 ＝ 14。

`may` 只出现在 Impact 侧：408 句次全部来自 422 个 Impact 判据句，422 − 408 ＝ 14 句次 Impact 侧不带 `may`，正好就是上面那三句：**13 句次用 `could`、1 句次用 `might`，零句次用裸陈述式**。也就是说"不带 may 的 Impact 句"不是一个混合桶，而是三个可以逐字列出的句子。**情态分布是"后果句有不确定性标记、修复句没有"的模板差异**，不是风险评级。

## 7.5 动名词打头：4.36%，与 #135 完全相反

| 指标 | 值 |
|---|---:|
| 判据句里动名词打头句次 | **19**（占 436 的 **4.36%**） |
| distinct 句 | 4 |
| 涉及段 | 19 |
| Impact 侧 ∶ Description 侧 | 19 ∶ **0** |

四个句子全部是"挂载／接入／打开"这一类**物理接入动作**：

| 句 | 句次 |
|---|---:|
| `Mounting a maliciously crafted SMB network share may lead to system termination` | 9 |
| `Connecting a malicious accessory may cause unexpected system termination` | 4 |
| `Mounting a maliciously crafted AFP network share may lead to system termination` | 3 |
| `Opening a file referencing a network share may lead to unexpected software execution` | 3 |

对照 #135：那篇 Impact 句 **74.90%** 以动名词打头（`Processing…`／`Parsing…`），本篇只有 4.36%。通道类条目的主语是**攻击者的位置**（`An attacker on…`），不是**动作**（`Doing X…`）；只有 SMB/AFP 挂载与配件接入这一小撮条目沿用了"动作打头"的写法。

## 7.6 标点与实体

| 检查项 | 结果 |
|---|---:|
| 句号异形可合并组 | **0** |
| `&rsquo;` | 0 |
| `&amp;` | 0 |
| 不换行空格 U+00A0 | 0 |
| 弯引号 U+2019 | 0 |
| 直双引号 `"` | **16** |

**句号异形 0 组**意味着本篇 76 句里没有"同一句带点／不带点各写一次"的情形（#131 有、#136 有 2 组、#132 为 0）。所以本篇的句子并集不会因为标点被人为拆开——76 这个数是干净的。

16 处直双引号全部来自 `Hide IP Address`／`Block All Remote Content`（4 段）、`Control Nearby Devices`（3 段）这类**功能名引用**，是设置项字面量，不是措辞异形。

## 7.7 判据句的长度

| 口径 | 值 |
|---|---:|
| `\w+` 口径、句次加权总词数 | 6,134 |
| distinct 76 句的 `\w+` 合计 | 1,141 |
| 每句均词数（`distinct ÷ 76`） | **15.01** |

`split /\s+/` 口径在本篇退化成一个常数（每句都返回 1），是坏指标，已弃用；只报 `\w+` 口径。**每句均 15.01 词**是本系列的中间偏上水平，与"位置状语 ＋ 情态 ＋ 后果"三段式模板一致：主语（`An attacker`）＋ 位置（`in a privileged network position`）＋ 情态（`may be able to`）＋ 后果（`intercept network traffic`）。

---

# 8 位置状语刀：`local network` 112 与 `privileged network position` 93，同段 0

## 8.1 严格互斥

| 短语 | 段 | 桶句次 |
|---|---:|---:|
| `local network`（含大写权限形） | **112** | 103 ＋ 8 |
| `privileged network position` | **93** | 80 ＋ 13 |
| 两者同段 | **0** | — |
| 刀三 322 段里两者都不含 | **117** | — |

**112 ＋ 93 ＋ 117 ＝ 322**，加法自己合上——这就是"互斥"的算术证明，不是靠一次交集计算碰出来的。两个位置状语在刀三内部**瓜分了 205／322 ＝ 63.7% 的段**，剩下 117 段的位置由别处给出（`in physical proximity` 22、`in wireless proximity` 8、`on the network` 5、`same network` 8、`with a paired Bluetooth device` 6、`with control of a network … server` 6，以及**根本没有位置状语**的 `An app may be able to…` 22 段等）。

## 8.2 句式与短语的两句对账

`local network` 112 段拆到句式桶：

| 桶 | 句次 | 与该短语的关系 |
|---|---:|---|
| 「An attacker on the local network…」 | **103** | 后果句，占 112 段的 92.0% |
| 「…unauthorized access to Local Network…」 | **8** | 大写形，权限授权语义 |
| 「An attacker in a privileged network position…」 | 0 | 互斥 |
| 其余（1 段） | 1 | 小写 local 但非上述句式 |

**103 ＋ 8 ＝ 111**，与 112 差 1 段——那 1 段是 `An app may be able to access local network devices without user consent`（1 句次，走「An app may be able to…」桶）。**所以"local network 有 112 段"和"103 句次是那句最大家庭"必须同时报，只报一个都会误导**：前者是词族规模，后者是句式规模，中间差的 9 段走的是权限授权形与设备访问形。

`privileged network position` 93 段的对账**一句不漏**：

| 桶 | 句次 |
|---|---:|
| 「An attacker in a privileged network position…」 | **80** |
| 「A user in a privileged network position…」 | **13** |
| 合计 | **93 ＝ 短语段数** ✓ |

这是全系列少见的一个"短语与句式 100% 双射"样本。**`privileged network position` 这个短语在公开文本里从来只出现在这两种主语的句式里**（attacker 版 80、user 版 13），没有第三种用法。

## 8.3 `same network` 也是 8∶8 双射

`same network` 8 段，桶「An unauthenticated user on the same network…」句次 **8**——8 段全部出自这一根句级刀，但分两个句子：**7 段**是 `An unauthenticated user on the same network as a signed-in Mac could send it AirPlay commands without pairing`（组件名 AirPlay），**1 段**是 `An unauthenticated user on the same network as a Compressor server may be able to execute arbitrary code`。两句的主语、位置状语完全同形，差别只在谓语：7 段用 `could` ＋ 裸动词（`could send`），1 段用 `may be able to`。

这 7 段就是 7.4 里"14 句次不带 `may`"中的 7 句次，也是整个主集里**唯一一处把通道限定写成 `could` ＋ 裸动词**的位置状语句式。同一个 `same network` 短语在两个句子间共享主语与状语、却换了两套情态框架，是"措辞模板并非严格约束"的直接样本：**同一条位置状语刀内部，情态选择并不统一**。

## 8.4 `proximity` 的两个方向

`proximity` 词界 30 段，全部写在 Impact：

| 细分 | 段 |
|---|---:|
| `physical proximity` | **22** |
| `wireless proximity` | **8** |
| `close proximity` | 0 |

22 ＋ 8 ＝ 30 ✓，与两个句式桶（22 句次／8 句次）**逐值相同**。这 8 段 `wireless proximity` 就是第 3.3 节里"刀一∩刀二"的那 8 段——`proximity` 与 `wireless` 在本篇是**绑死的一对**，`physical proximity` 从不与 `wireless` 同段（否则 8 ＋ 22 会大于 30）。

## 8.5 与"物理访问"这条对照线的关系

`physical access` 三字段 142 段、**∩主集 20**；广义"物理近身词族"（`physical access／proximity／nearby／in range／same network` 等合并）348 段、**∩主集 217**。也就是说本篇 428 段里 **217 段（50.7%）**带某种物理／近身限定，但只有 20 段用的是 `physical access` 这一套说法——**通道类条目偏好"位置"框架（proximity／range／same network），而不是"访问权限"框架（physical access）**。`physical access` 的 142 段与主集交 20 这一低比例，是"两个都能形容近身、但写在不同的句子模板里"的直接证据。

## 8.6 攻击者主语的存在性

| 指标 | 值 |
|---|---:|
| `attacker`（三字段）全树 | 890 段 |
| ∩主集 | **276** |
| 主集内**不含** `attacker` | **152** |

**428 段里有 152 段（35.5%）一个"攻击者"都不提**。它们的主语是 `An app`（22 段）、`A sandboxed app`（7）、`A website`（7）、`A malicious accessory`（8）、`A non-privileged user`（9）、`Mounting…`／`Connecting…`／`Opening…`（19）、`Network traffic`（6）、`A file received from AirDrop`（3）等。**"谁在打"和"从哪条通道打"是两套主语系统**，通道词可以完全不需要攻击者主语就成立——这与第 6 章的字段梯度是同一件事的两面：公开文本描述的是"位置 ＋ 后果"，不是"对手 ＋ 手段"。

---

# 9 后果强度八类：三字段并集 248、Impact 口径并集 220

## 9.1 两套口径，两个并集

| 类 | ∩主集（三字段） | ∩主集（仅 Impact） |
|---|---:|---:|
| ACE 任意代码执行 | 10 | 10 |
| BYP 绕过 | 28 | 28 |
| DIS 泄露／披露 | 41 | 30 |
| DOS 拒绝服务／终止 | 153 | 145 |
| FULL 完全控制 | **0** | **0** |
| MEM 内存损坏 | 7 | **0** |
| PRI 提权 | **0** | **0** |
| SBX 沙盒 | 16 | 7 |
| **相加** | **255** | **220** |
| **并集（去重段）** | **248** | **220** |
| **零类段** | **180** | **208** |

两个"相加＝并集"关系不一样：Impact 单字段口径 **相加 220 ＝ 并集 220**（零类段 208，428 − 208 ＝ 220 ✓），说明**在 Impact 字段里一段最多命中一类后果**——`M2`（同段 ≥2 后果类）**∩主集 ＝ 0 段**。三字段口径相加 255 > 并集 248，多出 **7 段**是"跨字段凑成两类"（例如 Impact 写 DOS、Description 写 DIS）。**所以"零类段 180 对 208"的 28 段差额不是有 28 段变了性质，而是口径换了字段**，两个数都不能当主集段数用。

## 9.2 两类彻底为 0

`FULL`（完全控制）与 `PRI`（提权）**两套口径都是 0 段**。这是通道类的一个结构性特征：公开文本给通道条目的后果封顶在"终止／泄露／绕过"，**不写"获得完全控制"也不写"提升权限"**。对照：`PRI` 类全树 124 段（附表 A 行 300），`FULL` 全树 63 段——它们存在，只是不与本主集共段。

`MEM` 三字段 7 段、Impact 0 段。这 7 段的"内存损坏"字样**全部只写在 Description**（`A buffer overflow was addressed with improved bounds checking.` 一类），Impact 侧写的是通道后果。**7∶0 又是一个单字段绑定**，与第 6 章的方向一致。

## 9.3 与词族对照行的关系

| 词族（三字段全树） | ∩主集 |
|---|---:|
| `denial of service` 470 | **96** |
| `unexpected system／app termination` 1,414 | 45 |
| `system termination` 814 | 27 |
| `leak／disclos*` 597 | 41 |
| `bypass` 301 | **28** |
| `memory corruption` 388 | 7 |
| `arbitrary code execution` 218 | **0** |
| `intercept` 28 | **28** |
| `track` 40 | 12 |
| `spoof` 72 | 11 |
| `sandbox` 475 | 16 |
| `restrict*` 793 | 52 |

两条值得单列。第一，**`intercept` 全树 28 段，∩主集也是 28 段——全部 28 段都在本主集内**。"拦截"这个词在公开文本里是**通道专属动词**，离开网络／近场语境它一次都不出现。第二，`arbitrary code execution` 词族 218 段与主集**交 0**，但 ACE 类命中 10 段——因为那 10 段写的是 `execute arbitrary code`（动宾形）而不是 `arbitrary code execution`（名词短语形）。**"词族 0 交集"与"类别非 0"并存**，这是第 15 章反复强调的"得数与成员是两根轴"的又一个样本。

## 9.4 与 #124 拒绝服务、#125 泄露的交叉

附表 A 外部集对照 12 行给出两套口径：

| 集合 | 自身规模 | ∩主集 |
|---|---:|---:|
| `Ds`（#124 拒绝服务集，附录口径） | 470 | **96** |
| `Ds`（#124 复刻口径） | 1,896 | 153 |
| `X`（#125 泄露集，附录口径） | 427 | **11** |
| `X`（#125 复刻口径） | 603 | 41 |
| `ACE`（任意代码执行集） | 343 | 10 |
| `BP`（#126 绕过集） | 301 | **28** |

**同一套语义用不同词表会得到 470 与 1,896 两种规模，对应的 ∩主集是 96 与 153**。本篇引用时必须带口径标签；第 16 章的复算步骤固定用附录口径的那一组（96／11），因为它们与附表 A、B 的行同源。

---

# 10 套话刀：整个通道词族只有三种修复说法

## 10.1 全树最粗的三把套话刀

| 修复套话形 | 全树段 | ∩主集 |
|---|---:|---:|
| `addressed with`（全形） | 6,589 | **368** |
| `addressed by` | 371 | **20** |
| `addressed through` | 247 | 28 |

368／428 ＝ 86.0% 的主集段带 `addressed with`，但**这 368 段里带通道词的只有 3 句**（第 6.2 节）——因为 `addressed with` 后面接的多半是 `improved state management`／`improved memory handling`，与通道无关。**`addressed by` 才是"动作型修复"的槽位**：本篇 14 段 Description 命中里 10 段走 `addressed by using HTTPS`，1 段走 `addressed with improved network restrictions`，3 段走 `addressed with Beacon Protection`。`addressed by` 全树 371 段、∩主集 20，其中 10 句次是本篇判据句。

## 10.2 校验类套话与主集的关系

| 套话 | 全树段 | ∩主集 |
|---|---:|---:|
| `improved input validation` | 641 | **69** |
| `check(s)／checking` 词族 | 2,017 | 104 |
| `validation` 词族 | 1,341 | 90 |
| `improved bounds checking` | 699 | 38 |
| `The issue was addressed with improved checks.` | 514 | 12 |
| `improved validation` | 367 | 9 |
| `additional validation` | 112 | **0** |
| `additional checks` | 34 | **0** |
| `improved checks to prevent unauthorized actions` | 16 | **0** |
| `sanitiz*` 词族 | 93 | **0** |
| `enforce*` 词族 | 53 | **0** |

最后一行是本篇的"套话缺席表"：五个更冷的校验短语**与通道主集零交集**。`additional validation` 与 `sanitiz*` 的存在感在 #132（图像格式解析）与 #133 里很高，在通道类里一次都不配——**通道条目的修复说法不是"加了校验"这一族**。

## 10.3 三种修复短语的语义档位

| 修复短语 | 段 | 档位 |
|---|---:|---|
| `using HTTPS when sending information over the network` | 10 | **协议替换** |
| `improved network restrictions` | 1 | **策略收紧** |
| `Beacon Protection` | 3 | **机制点名** |

`Beacon Protection` 是全树 3 段、且这 3 段**全部**同时被本篇抓到（∩主集 3）。**这是措辞层系列里极罕见的"直接写机制名"的修复句**：绝大多数修复句写"improved X"，这三句写了一个具体防护机制的名字。它落在 Description 侧、`beacon` 一词在 Impact 侧 0 段——**机制名只出现在修复句，不出现在后果句**，与第 6 章的"通道名只出现在后果句"正好反号。

---

# 11 页面分布：135 页、八档、Top10 只占 28.97%

## 11.1 页内段数八档

| 该页本篇段数 | 页数 |
|---|---:|
| 1 | **57** |
| 2 | 25 |
| 3 | 11 |
| 4 | 10 |
| 5 | 14 |
| 6～9 | 11 |
| 10～19 | 6 |
| 20～49 | **1** |
| 合计 | **135** ✓ |

**57 页（42.22%）只有 1 段**。分布形态是"大量单段页 ＋ 一个 21 段的尾巴"，与 #136（159 页、页面更分散）相比本篇**页均密度更高但页数更少**：428／135 ＝ 3.17 段／页。

## 11.2 Top10 页

| 页 | 段 | 该页本篇段涉及的组件名 |
|---|---:|---|
| 149035 | **21** | Bluetooth／CUPS／Core Bluetooth／DesktopServices／Heimdal／Mail／MobileAccessoryUpdater／NetworkExtension／SMB／Sandbox／Screen Sharing Server／Security／Wi-Fi／autofs／mDNSResponder（15 名） |
| 149042 | 15 | 同类多组件页（13 名） |
| 122374 | 13 | AirPlay／Apple Account／CoreUtils／SMB／Security |
| 149043 | 13 | 同类多组件页（11 名） |
| 122375 | 12 | AirPlay／Apple Account／SMB／Security |
| 149034 | 12 | Baseband／Bluetooth／Core Bluetooth／Heimdal／MobileAccessoryUpdater／Sandbox／Telephony／Wallet／Wi-Fi（9 名） |
| 122373 | 11 | AirPlay／CoreUtils／SMB／Security／Web Extensions |
| 122371 | 9 | AirPlay／CoreUtils／Web Extensions |
| 122372 | 9 | AirPlay／CoreUtils |
| 122378 | 9 | AirPlay／CoreUtils／Web Extensions |
| **Top10 合计** | **124** | 占 428 段的 **28.97%** |

Top10 分成两个家族：`14903x／14904x` 四页（21＋15＋13＋12 ＝ 61 段）是**多组件版本汇总页**（单页 9～15 个组件名），`12237x` 六页（13＋12＋11＋9＋9＋9 ＝ 63 段）是 **AirPlay 集中页**（每页组件名都以 AirPlay 打头，配 CoreUtils／Security／SMB）。**所以"页面头部集中"在本篇有两种完全不同的机制**，一种是版本页把所有组件排在一起，一种是同一功能的连续条目页。把它们混成一个"头部页"来谈会得出错误结论。

## 11.3 全本篇页：9 页 11 段

有 9 页**全部段落都被本篇抓到**：

| 页 | 段 | 组件名 |
|---|---:|---|
| 120303 | 1 | Bluetooth（域 B 那 1 段） |
| 120907 | 1 | Bluetooth |
| 122403 | 2 | SDK／SDK |
| 125693 | 1 | Compressor |
| 127557 | 1 | Bluetooth |
| 148170／148171／148172 | 各 1 | Screen Sharing |
| 148353 | 2 | Authentication Services／WebKit |

这 9 页是"整页即通道"的样本，也是第 14 章组件名的锚点之一（`Screen Sharing` 三名各 1 段、页 148353 的两段一个是 Authentication Services 一个是 WebKit）。

## 11.4 同页共存与第三套分母

这 135 页共 **7,767 段**，本篇 428 段 ＝ **5.51%**。也就是说，即便在最"通道密集"的页面上，**每 100 段里只有 5.5 段被本篇抓到**。这个数字用来防一种常见误读：看到某页 21 段就以为"这页讲的是网络漏洞"——页 `149035` 那页总共有 **314 段**，本篇只抓到 21 段，其余 **293 段**是同页其它组件、其它通道的条目。

把 Top10 页单独看更清楚：这 10 页合计 **1,529 段**，本篇在其中的 124 段占 **8.11%**；135 页里单页全段最多就是 `149035` 的 314 段。**"页面标题里有 Wi-Fi"与"页面主体讲的是无线通道"是两件事**，第三套分母（7,767）就是专门用来把这件事量化出来的。

---

# 12 致谢、编号、版式与在野

## 12.1 致谢

| 每段致谢人数 | 段 |
|---|---:|
| 1 人 | 187 |
| 2 人 | 161 |
| 3 人 | 34 |
| 4 人 | 19 |
| 5 人 | 14 |
| 6～9 人 | 12 |
| 9＋ 人 | 1 |
| **合计** | **428** ✓ |

人次 **855**、均值 **2.00**、最大 **15**。全树基线是均值 1.80／最大 37。**本篇比基线略高**（2.00 vs 1.80），最大 15 远低于全树 37。这个"略高"与 12.3 的致谢污染是同一件事：通道类条目更愿意署多人名字，而致谢行里的机构名（`Palo Alto Networks`）正是三字段并集多出的 13 段的来源。

## 12.2 编号

| 指标 | 值 |
|---|---:|
| 带编号的主集段 | **428／428**（100%） |
| 1 个编号的段 | 410 |
| 2 个编号的段 | 18 |
| distinct 编号 | **111** |
| 只出现在主集段内的编号 | **111** |
| 与主集外共享的编号 | **0** |

111 个编号**全部是本篇独占**。这个"共享 0"的成因是取数方式：编号集 `%CVE` 来自主集段的三字段，非主集段贡献 `%nonl`；本篇的 111 个在 `%nonl` 里一个都没出现。含义是**没有一条网络通道条目的 CVE 编号被另一条非通道条目共享**——这与 #124/#125 那类"一个编号横跨两条目"的情形不同。

## 12.3 致谢侧 13 段的逐段成因

三字段并集 441 比两字段主集 428 多的 13 段，逐段读原文后的归类：

| 命中词 | 段 | 实际是 |
|---|---:|---|
| `Networks` | **10** | 致谢行里的机构名 `Palo Alto Networks`（WebKit Bugzilla 与 WebRTC 条目） |
| `Wireless` | 2 | 致谢行里的项目／机构名用词（Hasso Plattner Institute 相关致谢、Wallet 条目致谢） |
| `httpstorm` | 1 | 域名 `httpstorm.com`（`HTTP` 松匹配） |

同时，致谢字段命中 17 段里有 **4 段本来就在主集内**（页 120303／120907／127557／149034，致谢行里同时出现 `Bluetooth／paired／wireless`）。**13 ＋ 4 ＝ 17 ✓**。

这一节的用途是把"三字段并集 441"这个数**钉死为不可用**：多出的 13 段没有一段是网络通道条目，所以本篇所有正文与附表一律以 **428** 为主集，441 只在第 1 章作为口径差披露。

## 12.4 版式域：域 B 只有 1 段

| 域 | 本篇段 | 域内全段 | 占比 |
|---|---:|---:|---:|
| 域 A（标准版式，246 页） | **427** | 9,045 | **4.72%** |
| 域 B（粗体版式，11 页） | **1** | 91 | **1.10%** |
| 全树 | 428 | 9,136 | 4.68% |

域 B 的那一段（页 `120303`、组件名 Bluetooth、刀一）：`An attacker with physical access to the accessory may be able to extract its Bluetooth pairing key and monitor Bluetooth traffic`。**域 B 占比只有域 A 的 23%**，形态接近 #134（域 B 显著偏低），与 #136（两域几乎相同 6.57%∶6.59%）相反。

这不是巧合，也不是"域 B 页面质量差"。域 B 是那 11 页粗体版式（id `118479｜120303-120305｜120307-120312｜120339`），内容偏硬件与激活；**通道词的分布受页面主题影响，版式域分母只能用作第四套对照，不能当作显著性证据**。

## 12.5 在野：两个哨兵重测

| 哨兵 | 全树段 | ∩主集 |
|---|---:|---:|
| `actively exploited` | **18** | **0** |
| `Apple is aware of a report` | **103** | **4** |
| `E`（#114 在野集，108 段） | 108 | **4** |

`actively exploited` 与主集**零交集**——本篇 428 段没有一段带"正在被积极利用"字样。`Apple is aware of a report` 相交 4 段（`E` 集 108 段 ∩主集也是 4，两处 4 是同一批段：#114 的在野集正是以该句为主判据之一）。这与 #126（`actively exploited` ∩BP ＝ 0、`aware of a report` ∩BP ＝ 20）方向一致：**在野声明与通道类措辞几乎不共段**。

## 12.6 与远程集 R 的关系

| 指标 | 值 |
|---|---:|
| `R`（#118 远程触达词族） | 843 段／178 页／72 名 |
| **∩主集** | **380 段 ＝ 主集的 88.79%** |
| `remote` 词界（仅 Impact） | 257 段 |
| ∩主集 | **19 段** |

**88.79% 这个高数是词族重叠，不是可达性结论。** `R` 的词表包含 `network|wireless|Bluetooth|Wi-Fi|proximity|packet` 等，与本篇三刀大面积同词，所以两集高交是**定义必然**。真正的"远程"字样只有 `remote` 一词，它在全树 257 段而与主集只交 **19 段**——**"写在 local network 上的条目"与"写了 remote 的条目"几乎是两批段**（19／428 ＝ 4.4%）。第 15 章把这两根轴分开报，就是为了防止用 88.79% 去谈远程可达性。
# 13 中文层：78 个词、25 个零，与英文三刀根本不在同一份语料上

前三章量的是英文顾问页，这一章量的是**中文手册页**——两份语料、两种量纲，先把这条边界划清楚，否则后面每个数字都会被读错。

## 13.1 语料、清洗与量纲

装载方式沿用 #111 起的口径：`_tmp` 目录下 `sec*.txt` 共 **179** 个文件，排除 `sec2727afbbc.txt`（聚合页）后得 **178 页可用**；每份文件首行取作页面标题（剥掉 `<[^>]+>` 标签、去掉" - 官方 Apple 支持…"尾巴），正文整体剥标签后作为一段纯文本。计数方式是**子串正则** `/\Q词\E/gi`：不分词、不加词界，命中一次算一次，同时记该词覆盖了几页、最集中的是哪一页。

关键差异有两条，必须写死在口径里：

1. **量纲不同**：中文层的最小单位是"词在某页出现的次数"，英文层是"段落"。一份中文页可能把"配对"写 27 次，但它在英文层对应的是 0 条或多条顾问条目。**两边的数字不能相乘、不能相除、不能互相换算**。
2. **语料不同**：中文层是产品支持文档与安全概览（讲"这个功能怎么设防"），英文层是安全响应顾问页（讲"这条缺陷的后果是什么"）。所以中文层的"有词"只说明**这个说法在中文文档里被使用**，不说明任何一条英文条目写了它。

## 13.2 八组 78 词与高频分布

| 组 | 词数 | 组内最高频 | 次数 | 最集中页 |
|---|---:|---|---:|---|
| 无线与局域网类 | 8 | 无线 | 1,240 | Apple 设备的无线局域网隐私（24 次） |
| 网络与报文类 | 12 | 网络 | 1,103 | Apple 设备的无线局域网隐私（23 次） |
| 网络与报文类 | 12 | 局域网 | 826 | Apple 设备的无线局域网隐私（20 次） |
| 网络与报文类 | 12 | 连接 | 733 | 安全激活数据连接（24 次） |
| 蓝牙与配件类 | 9 | 配对 | 699 | watchOS 系统安全性（27 次） |
| 蓝牙与配件类 | 9 | 配件 | 456 | HomeKit 通信安全性（42 次） |
| 无线与局域网类 | 8 | 无线网络／无线网 | 384 | 文稿修订记录（8 次） |
| 投送与投屏类 | 9 | 连续互通 | 362 | 连续互通安全性概览（5 次） |
| 蓝牙与配件类 | 9 | 蓝牙 | 243 | 蓝牙安全性（21 次） |
| 投送与投屏类 | 9 | 接力 | 212 | 接力安全性（26 次） |
| 投送与投屏类 | 9 | 隔空投送／投送 | 211 | 隔空投送安全性（21 次） |
| 网络与报文类 | 12 | 服务器 | 215 | iCloud 高级数据保护（13 次） |
| 协议与隧道类 | 10 | 中继 | 201 | iPhone 蜂窝网络通话中继安全性（5 次） |
| 协议与隧道类 | 10 | 代理 | 195 | iCloud 专用代理安全性（10 次） |

八个组的词数是 8／9／9／9／12／10／12／9，相加 **78**；非零 **53** 词、全零 **25** 词（附表 E 合计行）。

覆盖页这一列在本组数据里几乎没有区分度：**有 16 个词覆盖全部 178 页**——无线、局域网、无线网络、无线网、蓝牙、配件、配对、键盘、隔空投送、投送、接力、连续互通、网络、连接、中继、代理。这 16 个全是泛词或功能名，在任何一篇苹果中文文档里都会出现。真正有信息量的是**次数与最集中页**：服务器 215 次只落在 **56** 页、移除 104 次只落在 **38** 页、泄露 30 次落在 11 页、校验 19 次落在 **3** 页（其中 16 次集中在《Apple 钱包中证件的安全性》一页）。**"覆盖全部页面"的词的计量价值最低**，这句在中文层尤其成立。

## 13.3 子串计数必然产生的三组嵌套

这条不写成坑就会写错，所以单列一节。因为是子串计数，**短词只要完全被长词包含，两个词的计数就必然相等或呈包含关系**，跟内容无关：

| 长词 | 次数 | 短词 | 次数 | 判读 |
|---|---:|---|---:|---|
| 无线网络 | 384 | 无线网 | 384 | 两数相等 ⇒ 本语料里"无线网"从未单独出现，一律以"无线网络"形式出现 |
| 隔空投送 | 211 | 投送 | 211 | 两数相等 ⇒ "投送"只以"隔空投送"出现，没有"邮件投送／投送文件"这类用法 |
| 配对 | 699 | 已配对 | 3 | 包含关系：3 次"已配对"全部计入 699 次"配对"，两者不能相加 |

同理，"网络"1,103 次是"无线局域网／本地网络／同一网络／安全网络／网络设置／网络共享／网络配置"等所有含"网络"字样的说法的母串。**所以中文层这 78 个词不能像英文三刀那样做集合运算与加法闭合**：英文层一段只算一次（集合并集），中文层一次出现可以被多个词同时计到。附表 E 只报次数与页，不报并集，就是这个原因。

## 13.4 25 个全零词，按缺席原因分四类

| 缺席类型 | 词（附表 E 中出现次数为 0 的项） | 项数 |
|---|---|---:|
| 英文专名的中文连写形 | WiFi（"Wi-Fi"只有 1 次，在《Apple Configurator 安全性》） | 1 |
| 机制／部件的规范译名未落地 | 网络配置、耳机、鼠标、转换器、屏幕镜像、无线投屏、侧边栏、随航、近场、临近、射程、在范围内、物理接近、报文、主机名、私密中继、加密隧道、超文本 | 18 |
| 后果与修复术语的定译未落地 | 意外终止、任意代码执行、提权、越权、附加验证、已修复 | 6 |
| 合计 | — | **25** |

**第二类最值得看**：英文层写 `physical proximity` 22 段、`wireless proximity` 8 段、`within…Bluetooth range` 1 段、`nearby` 3 段、`relay` 20 段、`tunnel` 6 段，这些机制限定在英文措辞层是**高频骨干**（第 2、3 章量过），而中文手册层对应的"近场／射程／物理接近／在范围内／私密中继／加密隧道"**一个都没写**。这不矛盾：**两份语料的任务不同**——顾问页要写"攻击者在哪"，支持文档要写"功能怎么开"。

## 13.5 一条必须写出来的限制

附表 E 的词表**没有收"信标"**，而英文层 `Beacon Protection` 修复句有 3 段、`beacon` 词 3 段全在主集内（6.3 节）。所以**本篇不能说"中文层没有信标说法"**，只能说"本篇中文词表未覆盖该项"。同理，"局域网"826 次与英文 `local network` 112 段之间没有任何算术关系——一个是文档用词频率，一个是条目字段命中。**两份语料之间只能做定性对照，本篇所有跨语料的表述都限定在这个范围内。**

---

# 14 组件层：50 个名字扛住 428 段，名与词双向背离

## 14.1 规模与头部集中度

主集 428 段按**整条 `nm` 字符串**归组，得 **50 个组件名**（附表 F 的 50 行 ＋ 合计行）。头部集中度比 #135 更陡：

| 档位 | 段 | 占 428 |
|---|---:|---:|
| Top5（AirPlay／Wi-Fi／Kernel／mDNSResponder／Bluetooth） | 201 | **46.96%** |
| Top10（＋CoreUtils／MobileAccessoryUpdater／SMB／Core Bluetooth／Accessibility） | 261 | **60.98%** |
| 只有 1 段的尾部名（Airport／Compressor／Passkeys／Screenshots／Trusted Device／UIKit／Wallet／lldb） | 8 | 1.87% |

**一个组件名对应一页还是多页差别很大**：AirPlay 的 77 段分布在 **14** 页，Wi-Fi 的 41 段分布在 **32** 页，Kernel 的 38 段分布在 **34** 页——附表 F 的"页"列逐行相加得 **332**，大于主集的 135 页，因为同一个名字会跨页出现。

## 14.2 名维度是划分，所以行和等于真值

这一点和第 7 章附表 B 正好相反，值得单独说：**每段恰好只有一个 `nm` 字符串**，所以按名分组是一个严格划分，附表 F 的每一列纵向相加都等于全树真值——∩R 行和 380 ＝ `INT(主集, R)` 380，∩E 行和 4 ＝ 真值 4，∩M2 行和 0 ＝ 真值 0，∩L 行和 39 ＝ 真值 39，∩BP 行和 28 ＝ 真值 28，WNCH 段列 428 ＝ 主集。而附表 B 按句分组时一段可以含两句，行和必然虚高（∩R 行和 388 对真值 380）。**同一篇报告里两种表的"合计"含义不同，读附表时必须先看分组键是不是划分。**

## 14.3 名内占比：两极分化到 100% 对 1.1%

把每个名字在全树的"含 Impact 段数"当分母，本篇主集在该名内部占多少：

| 组件名 | 主集段 | 该名含 Impact 全集 | 名内占比 |
|---|---:|---:|---:|
| AirPlay | 77 | 77 | **100.0%** |
| 802.1X | 8 | 8 | 100.0% |
| CoreUtils | 15 | 15 | 100.0% |
| Screen Sharing | 3 | 3 | 100.0% |
| Sidecar | 2 | 2 | 100.0% |
| SDK／Compressor／Trusted Device／Wallet／lldb | 各 1～2 | 同名全集 | 100.0% |
| Wi-Fi | 41 | 58 | 70.7% |
| mDNSResponder | 27 | 53 | 50.9% |
| Bluetooth | 18 | 41 | 43.9% |
| SMB | 12 | 58 | 20.7% |
| Kernel | 38 | 681 | **5.6%** |
| WebKit | 10 | 910 | **1.1%** |

名内占比 100% 的有 **10** 个名字。**这 10 名里，AirPlay／802.1X／CoreUtils／Screen Sharing／Sidecar 是"该组件的公开条目几乎全部与通道措辞有关"，而 Kernel／WebKit 相反**：Kernel 有 681 段含 Impact，只有 38 段被三刀抓到（5.6%），WebKit 910 段里只有 10 段（1.1%）。把 50 名的全集相加得 **2,860 段 ＝ 全树含 Impact 7,434 段的 38.5%**，而本篇只从这 2,860 段里取出 428 段，**名内总覆盖率 15.0%**。也就是说：**"这条条目挂在网络相关组件名下"和"这条条目的措辞写了网络通道"是两件不同的事，后者只有前者的十五分之一。**

## 14.4 名与词的双向背离

这是本篇组件层的核心表。左列是"组件名含该词"（`nm` 字段子串匹配，代理口径），右列是"正文三字段写出该词"：

| 关键字 | 组件名含该词（全树段） | 其中进主集 | 正文写出该词（段） | 词∩名 |
|---|---:|---:|---:|---:|
| AirPlay | 84 | **77** | **7** | 7 |
| Wi-Fi | 88 | 41 | 10 | 10 |
| Bluetooth | 105 | 29 | 32 | **24** |
| AirDrop | 20 | 8 | 3 | 3 |
| Bonjour／mDNS | 74 | 27 | **0** | 0 |
| 802.1X | 8 | 8 | **0** | 0 |
| Network | 97 | 18 | **306** | 11 |
| USB | **0** | 0 | 5 | 0 |
| VPN／IPSec | **0** | 0 | 6 | 0 |
| Relay | **0** | 0 | 20 | 0 |

三个方向都读一遍：

- **有名无词**：`802.1X` 挂名 8 段全部进主集，但正文里 `802.1X` 这个词在三个字段出现 **0** 次；`Bonjour／mDNS` 挂名 74 段、27 段进主集，正文同样 **0** 次。这两行是第 2.3 节"协议专名不写"的组件侧证据：**认证机制靠组件名承载，不靠措辞承载**。
- **有词无名**：`USB` 词 5 段、`VPN` 词 6 段、`relay` 词 20 段，这三类词的**组件名代理计数都是 0**——苹果的条目把这些机制写在正文里，但组件归属挂在 `Wi-Fi`／`CoreUtils`／`Mail`／`Safari` 这类上层名字下面。**反过来用组件名去筛"USB／VPN／中继类条目"，会得到空集**。
- **同词不同数量级**：`AirPlay` 名 84 段对词 7 段（12 倍），`Wi-Fi` 名 88 对词 10（8.8 倍），`Bluetooth` 名 105 对词 32（3.3 倍）。而 `Network` 恰好相反：名 97 段对词 306 段——`network` 这个词写得比 `Networking` 这个组件名挂得多。
- **Bluetooth 是唯一"词比名更准"的行**：32 段写词的里面有 **24** 段挂在含 Bluetooth 的组件名下，另 8 段（`Wi-Fi`、`Core Bluetooth` 之外的名字）没挂；同时名∩主集 29 段里有 5 段整句不写 `Bluetooth`（写的是 `accessory`、`pairing key`）。**这一行是本篇唯一"两个维度部分重合但互不包含"的样本**，所以三个数必须一起报。

## 14.5 名维度与外部集：只有 Accessibility 与"可利用"有关

50 名里 ∩E（`exploit*` 词族）非零的**只有 1 个**：`Accessibility` 4 段。∩M2（≥2 个后果类）非零的**一个都没有**（全为 0）。∩R 为 0 的名有 **8** 个、共 **34** 段：`MobileAccessoryUpdater`（12）、`Networking`（7）、`Audio`（5）、`FrontBoard`（5）、`Sidecar`（2）、`Screenshots`／`UIKit`／`Wallet`（各 1）。

**`Networking` 这 7 段最值得注意**：组件名就叫 Networking，7 段全部进主集，但 ∩R ＝ 0——一句都没写"远程／网络可达"这类词，走的是"应用观察系统级连接""沙盒越出"这类**本地视角**措辞。这是第 1 章问题三的最强样本：**名字说这是网络组件，措辞说这不是远程问题**。

---

# 15 同数不同集：成员级核验

这一章只做一件事：把本篇里所有"两个数字看起来一样或看起来是包含关系"的断言，用成员级交集重算一遍。规则是**每次同时报 |A|、|B|、|A∩B| 三个数**，只报两个数就下结论的写法在本篇一律不成立。

## 15.1 三组"8 段"两两不相交

| 集合 A | |A| | 集合 B | |B| | |A∩B| | 判定 |
|---|---:|---|---:|---:|---:|---|
| 刀一∩刀二（无线专名 ∩ 近场限定） | 8 | I∩D（Impact 与 Description 双侧同段） | 8 | **0** | 完全不相干 |
| 刀一∩刀二 | 8 | `same network` 短语段 | 8 | **0** | 完全不相干 |
| I∩D | 8 | `same network` 短语段 | 8 | **0** | 完全不相干 |

三个 8 段集彼此**零重合**，合计 24 段互不相同。"same network 8 段"和"I∩D 8 段"是两条完全不同的结论：**前者说位置状语的句式规模，后者说字段双侧命中的规模**，把它们并成一句"8 段网络条目"就是把三个集合当同一个。

## 15.2 三组"3 段"与一组"2 段"同样互斥

| 集合对 | 各段数 | |A∩B| |
|---|---|---:|
| `nearby`（3）× `AirDrop` 词（3） | 3 对 3 | **0** |
| `nearby`（3）× `beacon`（3） | 3 对 3 | **0** |
| `AirDrop` 词（3）× `beacon`（3） | 3 对 3 | **0** |
| `IPSec／IKE`（2）× `Sidecar`（2） | 2 对 2 | **0** |

这是 #132 那个"138 对 138 交 0"教训在本篇的复现：**同数量级的全零对照桶之间毫无关系**，"三个都是 3 段"纯属规模巧合。

## 15.3 反例：确实是同一组段的三对

同数不同集要报，**同数且同集**同样要报，否则这章就变成"什么都不能信"的虚无结论：

| 集合 A | |A| | 集合 B | |B| | |A∩B| | 判定 |
|---|---:|---|---:|---:|---:|---|
| `VPN 词界` | 6 | `tunnel* 词族` | 6 | **6** | 同一组段 |
| `Bluetooth packets 短语` | 5 | `crafted packets 短语` | 5 | **5** | 同一组段 |
| 后果类 BYP ∩ 主集 | 28 | #126 `bypass` 词族 ∩ 主集 | 28 | **28** | 同一组段 |
| `remote` 词界 ∩ 主集 | 19 | 远程可达集 R ∩ 主集 | 380 | **19** | 真子集（19 段全在 R 里） |

四对的成因各不相同。`VPN`＝`tunnel` 是因为本语料里 VPN 只以"流量越出隧道"这一组句子出现（6 句次全在同一批段里）；`Bluetooth packets`＝`crafted packets` 是因为全树写 `crafted … packets` 的 5 段恰好都是 Wi-Fi／Bluetooth 报文句；`BYP∩主集`＝`bypass 词族∩主集` 是因为两把刀用同一个词根，只是扫描字段与右词界不同（BYP 全树 **291** 段只扫 Impact 且无右界，bypass 词族全树 **301** 段扫三字段且有右界），**在主集内部差异被吞掉、两者落到同 28 段**；`remote` ⊂ R 是因为 R 的词表包含 `remote`。

**这四对说明：只有做过成员级交集，才能区分"同一组段"、"真子集"和"数字撞车"。**

## 15.4 名维度与词维度的包含关系

| 词集 | |词| | 名集（含该词组件名 ∩ 主集） | |名∩主集| | |词∩名| | 判定 |
|---|---:|---|---:|---:|---:|---|
| `AirPlay` 词 | 7 | 名含 AirPlay | 77 | **7** | 词 ⊂ 名 |
| `Wi-Fi` 词 | 10 | 名含 Wi-Fi | 41 | **10** | 词 ⊂ 名 |
| `AirDrop` 词 | 3 | 名含 AirDrop | 8 | **3** | 词 ⊂ 名 |
| `Bluetooth` 词 | 32 | 名含 Bluetooth | 29 | **24** | 互不包含（词有 8 段不挂名，名有 5 段不写词） |
| `mDNS／Bonjour` 词 | **0** | 名含 Bonjour／mDNS | 27 | 0 | 名有词无 |
| `802.1X` 词 | **0** | 名含 802.1X | 8 | 0 | 名有词无 |

`Bluetooth` 这一行是本篇最诚实的证据：**"写词的 32 段"与"挂名的 29 段"既不互相包含也不相等**，8 段写了 Bluetooth 却挂在别的组件名下（多为 `Wi-Fi`、`CoreUtils`），5 段挂在 Bluetooth 名下却不写这个词（写 `accessory` 或 `pairing key`）。

## 15.5 姊妹篇九套集与主集的关系

九套集全部按 #122–#136 各自的复刻口径重算（两字段：Impact ＋ Description），与本篇主集取成员级交集：

| 对照集（复刻口径） | 全树段 | ∩本篇主集 | 占主集 |
|---|---:|---:|---:|
| #124 拒绝服务与终止集 | 1,896 | **153** | 35.75% |
| #136 认证与凭据词族 | 600 | 55 | 12.85% |
| #125 泄露词族 | 603 | 41 | 9.58% |
| #126 bypass 词族 | 301 | 28 | 6.54% |
| #131 网页与浏览器策略集 | 1,253 | 22 | 5.14% |
| #122 race condition 集 | 232 | 11 | 2.57% |
| 任意代码执行集 | 343 | 10 | 2.34% |
| #135 音视频与媒体词族 | 251 | **1** | 0.23% |
| #132 图像与字体词族 | 367 | **0** | 0.00% |

两个端点最有信息量。**#124 的 153 段是最大重合**（35.75%），因为通道类条目的后果大量写"unexpected system termination／denial-of-service"；**#132 与本主集 0 段重合**、#135 只有 1 段——图像字体篇与无线通道篇在措辞层是**两套完全分开的句子**，尽管两者都涉及"处理一个外来输入"。

同一批集之间在全树层的相交也顺手量了，用来解释"为什么后果类相加会重复计数"：`泄露集 × #124 DOS 集` 交 **35** 段、`任意代码执行集 × #124 DOS 集` 交 **92** 段、`#136 认证集 × #126 bypass 集` 交 **40** 段。那 92 段几乎全是同一个句式在并列两种后果，样本句：`An attacker may be able to cause unexpected system termination or arbitrary code execution in DCP firmware`、`A remote attacker may cause an unexpected app termination or arbitrary code execution`。**这正是第 9 章"八类相加 255 段、并集 248 段、≥2 类 7 段"的来源——并列后果句在同一段里同时命中两把刀。**

## 15.6 本篇已经报过的同数不同集，集中复核一遍

| 断言 | |A| | |B| | |A∩B| | 出处 |
|---|---:|---:|---:|---|
| `local network` × `privileged network position` | 112 | 93 | **0** | 8.1／8.2 |
| `physical proximity` × `wireless proximity` | 22 | 8 | **0** | 8.4 |
| 致谢字段含 `Wireless` 段 × 仅致谢新增段 | 5 | 13 | **2** | 4.5 |
| `wireless` 三字段段 × 仅致谢新增段 | 13 | 13 | **2** | 4.5 |
| `HTTP*` 仅 Impact × `HTTP*` 仅 Description | 6 | 10 | **0** | 6.3 |
| `beacon` 仅 Description × 主集其余刀 | 3 | 428 | 3 | 6.3 |
| `frame` 无右界宽松段 × 主集 | 33 | 428 | **0** | 5.3 |
| `device` 裸词界段 × 主集 | 207 | 428 | 39 | 5.3 |
| 广义物理近身词族 × 主集 | 348 | 428 | 217 | 8.5 |

最后一行是本篇唯一"对照集覆盖主集一半以上"的情形：广义物理近身词族 **348** 段里有 **217** 段落在主集内——占该词族的 **62.4%**、占主集的 **50.7%**。而 `frame` 那行是反过来的极端：**33 段与主集 0 相交**，说明"framework 污染"不是污染、是完全无关。**用宽松词界扩出来的量，不会按比例落在目标集合里。**

上表里最容易被读错的是那对 **13 ∶ 13**：「仅致谢新增段」441−428＝**13** 段，「`wireless` 三字段命中段」也恰好 **13** 段——两个 13 得数相同，但只有 **2** 段重合（`Shortcuts` 与 `Wallet` 的 Hasso Plattner Institute 致谢行）。其余 11 段 `wireless` 命中本来就在主集内，而另外 11 段仅致谢新增段里根本没有 `wireless` 一词（10 段是 `Palo Alto Networks`、1 段是 `httpstorm.com`）。所以第 4.5 节的"13 段致谢污染"和第 5.3 节的"`wireless` 只出现在致谢侧"是**两组不同的段**，共用同一个数字纯属巧合；两者的并集是 22 段。

---

# 16 十二步复算

以下 12 步给出可逐条重跑的路径与预期锚点值。装载与所有集合定义都在 `_tmp/tw123tab.pl`（前 50 行预载体）＋ `_tmp/s137.pl`（本篇词表与判据表）里，**任何复算都必须复用这两个文件里的定义，不能照抄正则**——#135 的教训是重抄一遍正则会少 53 段。

| 步 | 做什么 | 预期锚点 |
|---:|---|---|
| 1 | 装载两块域，检查硬编码锚点 | 全树 **9,136** 段／**257** 页／**472** 名／含 Impact **7,434**／含致谢 **9,132**；域 A 9,045／246 页，域 B 91／11 页；任一不符即 `die` |
| 2 | 用 `$re3`（三刀合并、带左右词界、三字段）取主集 | **428／135 页／50 名**；占 9,136 的 4.68%、占 7,434 的 5.76% |
| 3 | 三刀分别取并集 | 刀一 **124**、刀二 **41**、刀三 **322**；三刀并集仍 428（差 0） |
| 4 | 七档互斥闭合分区 | 仅刀一 **73**／仅刀二 **25**／仅刀三 **278**／一∩二 **8**／一∩三 **36**／二∩三 **1**／三刀同段 **7**，相加 **428**；三条恒等式 124＝73＋8＋36＋7、41＝25＋8＋1＋7、322＝278＋36＋1＋7 全部对上 |
| 5 | 字段侧分别数 | Impact 侧 **422**、Description 侧 **14**、三字段并集 **441**、仅I **414**／仅D **6**／I∩D **8** |
| 6 | 致谢字段单独数 | `%CA` **17** 段＝新增 **13** ＋ 已在主集 **4**；加致谢后 441 段／138 页／53 名 |
| 7 | 判据句层切句并去重 | 句次 **436**（Impact **422** ＋ Description **14**），distinct 句 **76**，两侧共用句 **0** |
| 8 | 38 把句级刀按优先序归桶 | **35** 桶、未归类 **0**；`^An attacker…` 兜底刀之前的桶全部闭合 |
| 9 | 位置状语加法闭合 | **112 ＋ 93 ＋ 117 ＝ 322**；`local network` 内 103＋8＋1＝112；`privileged` 内 80＋13＝93；`proximity` 内 22＋8＝30 |
| 10 | 后果八类三字段与单字段 | 三字段相加 **255**／并集 **248**／零类 **180**／≥2 类 **7**；Impact 单字段 220／220／208／**0**；`FULL`、`PRI` 两口径皆 **0** |
| 11 | 页面与组件名分箱 | 135 页八档 57／25／11／10／14／11／6／1 ＝ 135；这 135 页全段 **7,767**（428 ＝ 5.51%）；名 50 个、Top5 201＝46.96%、Top10 261＝60.98%、50 名含 Impact 全集 **2,860**＝38.5% |
| 12 | 附表行标与合计行 | A **300 行**（299 判据项＋合计，全零 54 项）、B **77**、C **36**（续表另计 5 行）、D **26**、E **79**（78 词＋合计，全零 25）、F **51**（50 名＋合计）；中文语料 **178 页可用** |

第 4 步和第 9 步是本篇的两根"自证"：加法自己合上，不需要外部真值；第 12 步的行标数由 `_hbcheck.pl` 逐表复核，正文与附表不许各说一套。

---

# 附表 A 判据词族普查（300 行）

| 组 | 判据 | 并集段 | 页 | 名 | ∩主集 | ∩R | ∩E | ∩WO |
|---|---|---:|---:|---:|---:|---:|---:|---:|
| 本体词（本篇主刀） | `Wi-Fi／wifi 连字符口径（刀一·无线局域网，三字段）` | 10 | 10 | 1 | 10 | 10 | 0 | 1 |
| 本体词（本篇主刀） | `Wi-Fi 仅 ASCII hyphen（刀一·窄口径，三字段）` | 10 | 10 | 1 | 10 | 10 | 0 | 1 |
| 本体词（本篇主刀） | `Wi-Fi 无词界（刀一·宽松对照，三字段）` | 10 | 10 | 1 | 10 | 10 | 0 | 1 |
| 本体词（本篇主刀） | `Bluetooth 词界（刀一·蓝牙，三字段）` | 32 | 24 | 4 | 32 | 32 | 0 | 0 |
| 本体词（本篇主刀） | `Bluetooth* 词族（刀一·含派生，三字段）` | 36 | 28 | 5 | 32 | 32 | 0 | 4 |
| 本体词（本篇主刀） | `Core Bluetooth 专名（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Bluetooth device 短语（刀一·三字段）` | 10 | 7 | 1 | 10 | 10 | 0 | 0 |
| 本体词（本篇主刀） | `Bluetooth pairing 短语（刀一·三字段）` | 1 | 1 | 1 | 1 | 1 | 0 | 0 |
| 本体词（本篇主刀） | `Bluetooth packets 短语（刀一·三字段）` | 5 | 5 | 1 | 5 | 5 | 0 | 0 |
| 本体词（本篇主刀） | `AirDrop 词界（刀一·隔空投送，三字段）` | 3 | 3 | 1 | 3 | 3 | 0 | 0 |
| 本体词（本篇主刀） | `AirPlay 词界（刀一·投屏，三字段）` | 7 | 7 | 1 | 7 | 7 | 0 | 7 |
| 本体词（本篇主刀） | `AirDrop／AirPlay 合并（刀一·两专名，三字段）` | 10 | 10 | 2 | 10 | 10 | 0 | 7 |
| 本体词（本篇主刀） | `CarPlay 专名（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Sidecar 专名（刀一·随航，三字段）` | 2 | 2 | 1 | 2 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Handoff 专名（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Continuity 专名（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Universal Clipboard 专名（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `iPhone Mirroring 短语（刀一·iPhone 镜像，三字段）` | 6 | 6 | 4 | 6 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Instant Hotspot 专名（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `hotspot 词界（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `NFC 专名（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Near Field 短语（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `cellular 词界（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `baseband 词界（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `modem 词界（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `VPN 词界（刀一·虚拟专用网，三字段）` | 6 | 6 | 1 | 6 | 6 | 0 | 0 |
| 本体词（本篇主刀） | `VPN tunnel 短语（刀一·三字段）` | 6 | 6 | 1 | 6 | 6 | 0 | 0 |
| 本体词（本篇主刀） | `tunnel* 词族（刀一·隧道，三字段）` | 6 | 6 | 1 | 6 | 6 | 0 | 0 |
| 本体词（本篇主刀） | `DNS 词界（刀一·域名解析，三字段）` | 7 | 7 | 1 | 7 | 7 | 0 | 0 |
| 本体词（本篇主刀） | `DNS queries 短语（刀一·三字段）` | 7 | 7 | 1 | 7 | 7 | 0 | 0 |
| 本体词（本篇主刀） | `mDNS 专名（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Bonjour 专名（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `IP 词界（刀一·网际协议地址，三字段）` | 21 | 18 | 5 | 21 | 15 | 0 | 0 |
| 本体词（本篇主刀） | `IP address 短语（刀一·三字段）` | 21 | 18 | 5 | 21 | 15 | 0 | 0 |
| 本体词（本篇主刀） | `IPv4／IPv6 专形（刀一·三字段）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `IP 无右界（刀一·宽松对照，iPad／IPSec 污染）` | 33 | 24 | 12 | 32 | 20 | 0 | 0 |
| 本体词（本篇主刀） | `802.11 专名（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `802.1X 专名（刀一·正文全零／组件名有）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `WPA／WPA2／WPA3（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `WEP 专名（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `EAP 专名（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `WEP／WPA／EAP 合并（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Ethernet 词界（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Ethernet／DHCP／ARP／TCP／TLS／SSL 六项合并（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Lightning 专名（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Thunderbolt 专名（刀一·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `USB 词界（刀一·通用串行总线，三字段）` | 5 | 5 | 2 | 5 | 5 | 4 | 0 |
| 本体词（本篇主刀） | `USB Restricted Mode 短语（刀一·受限模式）` | 5 | 5 | 2 | 5 | 5 | 4 | 0 |
| 本体词（本篇主刀） | `accessory／accessories 词界（刀一·配件，三字段）` | 14 | 14 | 3 | 14 | 2 | 0 | 0 |
| 本体词（本篇主刀） | `malicious accessory 短语（刀一·三字段）` | 12 | 12 | 1 | 12 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `pair／pairs／paired／pairing 四形（刀一·配对，两字段）` | 22 | 22 | 4 | 22 | 17 | 0 | 7 |
| 本体词（本篇主刀） | `pair 无左界（刀一·宽松对照，repair／compare 污染）` | 24 | 22 | 5 | 22 | 17 | 0 | 7 |
| 本体词（本篇主刀） | `pairing key 短语（刀一·三字段）` | 1 | 1 | 1 | 1 | 1 | 0 | 0 |
| 本体词（本篇主刀） | `paired／unpaired 两形（刀一·已配对，三字段）` | 14 | 14 | 3 | 14 | 9 | 0 | 0 |
| 本体词（本篇主刀） | `wireless 词界（刀一·无线，两字段）` | 13 | 12 | 4 | 11 | 10 | 0 | 0 |
| 本体词（本篇主刀） | `wireless 仅 Impact 侧（刀一）` | 8 | 8 | 1 | 8 | 8 | 0 | 0 |
| 本体词（本篇主刀） | `wireless 仅致谢侧（刀一·单字段绑定）` | 5 | 5 | 3 | 3 | 2 | 0 | 0 |
| 本体词（本篇主刀） | `wireless proximity 短语（刀一·三字段）` | 8 | 8 | 1 | 8 | 8 | 0 | 0 |
| 本体词（本篇主刀） | `刀一并集（无线与配对通道专名，两字段）` | 124 | 82 | 20 | 124 | 93 | 4 | 8 |
| 本体词（本篇主刀） | `proximity 词界（刀二·近场，三字段）` | 30 | 22 | 2 | 30 | 30 | 0 | 0 |
| 本体词（本篇主刀） | `physical proximity 短语（刀二·三字段）` | 22 | 21 | 2 | 22 | 22 | 0 | 0 |
| 本体词（本篇主刀） | `close proximity 短语（刀二·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `proximity 无词界（刀二·宽松对照）` | 30 | 22 | 2 | 30 | 30 | 0 | 0 |
| 本体词（本篇主刀） | `nearby 词界（刀二·附近，三字段）` | 3 | 3 | 1 | 3 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Nearby Interactions 专名（刀二·三字段）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `near 词界（刀二·未收对照，三字段）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `in range 短语（刀二·裸形全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `within…range 短语（刀二·Bluetooth range，三字段）` | 1 | 1 | 1 | 1 | 1 | 0 | 0 |
| 本体词（本篇主刀） | `in…range 短语（刀二·三字段）` | 11 | 10 | 2 | 2 | 2 | 0 | 0 |
| 本体词（本篇主刀） | `range 裸词界（刀二·未收对照）` | 11 | 10 | 2 | 2 | 2 | 0 | 0 |
| 本体词（本篇主刀） | `short-range／long-range（刀二·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `adjacent 词界（刀二·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `same network 短语（刀二·同网，三字段）` | 8 | 8 | 2 | 8 | 8 | 0 | 7 |
| 本体词（本篇主刀） | `other devices 短语（刀二·裸形对照，三字段）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `another device 短语（刀二·裸形对照，三字段）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `second device 短语（刀二·裸形对照，三字段）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `device 裸词界（刀二·未收对照，两字段）` | 207 | 100 | 51 | 39 | 28 | 9 | 3 |
| 本体词（本篇主刀） | `peer 词界（刀二·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `刀二并集（近场与距离限定，两字段）` | 41 | 33 | 5 | 41 | 38 | 0 | 7 |
| 本体词（本篇主刀） | `network／networks 词界（刀三·网络，三字段）` | 306 | 112 | 38 | 296 | 290 | 0 | 16 |
| 本体词（本篇主刀） | `network* 词族（刀三·含 networking，两字段）` | 296 | 107 | 36 | 296 | 290 | 0 | 16 |
| 本体词（本篇主刀） | `network* 仅 Impact 侧（刀三）` | 290 | 107 | 35 | 290 | 290 | 0 | 16 |
| 本体词（本篇主刀） | `network* 仅致谢侧（刀三·单字段绑定）` | 10 | 10 | 2 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `local network 短语（刀三·本地网络，三字段）` | 112 | 37 | 7 | 112 | 112 | 0 | 1 |
| 本体词（本篇主刀） | `Local Network 权限专名（刀三·大写形，三字段）` | 8 | 8 | 3 | 8 | 8 | 0 | 0 |
| 本体词（本篇主刀） | `network traffic 短语（刀三·三字段）` | 39 | 34 | 8 | 39 | 39 | 0 | 0 |
| 本体词（本篇主刀） | `network position 短语（刀三·位置限定，三字段）` | 93 | 56 | 16 | 93 | 93 | 0 | 0 |
| 本体词（本篇主刀） | `network settings 短语（刀三·三字段）` | 10 | 9 | 3 | 10 | 10 | 0 | 0 |
| 本体词（本篇主刀） | `network share 短语（刀三·网络共享，三字段）` | 18 | 12 | 4 | 18 | 18 | 0 | 3 |
| 本体词（本篇主刀） | `network connection 短语（刀三·三字段）` | 14 | 14 | 4 | 14 | 14 | 0 | 0 |
| 本体词（本篇主刀） | `network restrictions 短语（刀三·三字段）` | 8 | 7 | 3 | 8 | 7 | 0 | 0 |
| 本体词（本篇主刀） | `network server 短语（刀三·三字段）` | 6 | 3 | 2 | 6 | 6 | 0 | 0 |
| 本体词（本篇主刀） | `network packet 短语（刀三·三字段）` | 8 | 8 | 1 | 8 | 8 | 0 | 0 |
| 本体词（本篇主刀） | `packet／packets 词界（刀三·报文，三字段）` | 20 | 20 | 3 | 20 | 20 | 0 | 0 |
| 本体词（本篇主刀） | `packet* 词族（刀三·含派生，三字段）` | 20 | 20 | 3 | 20 | 20 | 0 | 0 |
| 本体词（本篇主刀） | `crafted packets 短语（刀三·三字段）` | 5 | 5 | 1 | 5 | 5 | 0 | 0 |
| 本体词（本篇主刀） | `HTTP／HTTPS／HTTP2 词族（刀三·超文本协议）` | 16 | 15 | 3 | 16 | 5 | 0 | 0 |
| 本体词（本篇主刀） | `HTTP* 仅 Impact 侧（刀三）` | 6 | 6 | 1 | 6 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `HTTP* 仅 Description 侧（刀三·单字段绑定）` | 10 | 9 | 2 | 10 | 5 | 0 | 0 |
| 本体词（本篇主刀） | `HTTPS 专形（刀三·三字段）` | 16 | 15 | 3 | 16 | 5 | 0 | 0 |
| 本体词（本篇主刀） | `beacon／beacons 词界（刀三·信标）` | 3 | 3 | 1 | 3 | 3 | 0 | 0 |
| 本体词（本篇主刀） | `beacon 仅 Description 侧（刀三·单字段绑定）` | 3 | 3 | 1 | 3 | 3 | 0 | 0 |
| 本体词（本篇主刀） | `Beacon Protection 专名（刀三·修复句）` | 3 | 3 | 1 | 3 | 3 | 0 | 0 |
| 本体词（本篇主刀） | `frame 词界（刀三·帧，裸形全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `frame 无右界（刀三·宽松对照，framework 污染）` | 33 | 23 | 2 | 0 | 20 | 0 | 0 |
| 本体词（本篇主刀） | `quic 词界（刀三·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `SSID 专名（刀三·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `handshake 词界（刀三·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `broadcast 词界（刀三·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `multicast 词界（刀三·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `unicast 词界（刀三·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `broadcast／multicast／unicast 合并（刀三·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `advertis* 词族（刀三·广播探测，全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `roaming 词界（刀三·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `access point 短语（刀三·全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `relay／relays／relaying 词族（刀三·中继，两字段）` | 20 | 17 | 5 | 20 | 18 | 0 | 0 |
| 本体词（本篇主刀） | `relay 无词界（刀三·宽松对照）` | 20 | 17 | 5 | 20 | 18 | 0 | 0 |
| 本体词（本篇主刀） | `Private Relay 专名（刀三·私密中继）` | 20 | 17 | 5 | 20 | 18 | 0 | 0 |
| 本体词（本篇主刀） | `IPSec／IKE 专名（刀三·隧道协议）` | 2 | 2 | 1 | 2 | 2 | 0 | 0 |
| 本体词（本篇主刀） | `刀三并集（网络报文与广播机制，两字段）` | 322 | 116 | 40 | 322 | 308 | 0 | 16 |
| 本体词（本篇主刀） | `三刀并集（两字段主集）` | 428 | 135 | 50 | 428 | 380 | 4 | 17 |
| 本体词（本篇主刀） | `三刀并集（三字段，含致谢）` | 441 | 138 | 53 | 428 | 380 | 4 | 17 |
| 本体词（本篇主刀） | `三刀并集（仅 Impact 侧）` | 422 | 135 | 49 | 422 | 380 | 4 | 17 |
| 本体词（本篇主刀） | `三刀并集（仅 Description 侧）` | 14 | 13 | 4 | 14 | 8 | 0 | 0 |
| 本体词（本篇主刀） | `I∩D 双侧同段` | 8 | 7 | 2 | 8 | 8 | 0 | 0 |
| 本体词（本篇主刀） | `仅 Description 命中（Impact 不含三刀）` | 6 | 6 | 2 | 6 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `刀一∩刀二` | 8 | 8 | 1 | 8 | 8 | 0 | 0 |
| 本体词（本篇主刀） | `刀一∩刀三` | 36 | 33 | 6 | 36 | 36 | 0 | 0 |
| 本体词（本篇主刀） | `刀二∩刀三` | 1 | 1 | 1 | 1 | 1 | 0 | 0 |
| 本体词（本篇主刀） | `刀一∩刀二∩刀三` | 7 | 7 | 1 | 7 | 7 | 0 | 7 |
| 本体词（本篇主刀） | `仅致谢侧新增（主集之外）` | 13 | 13 | 5 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `摘除 network*（刀三整根撤除）` | 179 | 101 | 24 | 179 | 132 | 4 | 8 |
| 本体词（本篇主刀） | `摘除 IP 词界（刀一）` | 418 | 135 | 50 | 418 | 376 | 4 | 17 |
| 本体词（本篇主刀） | `摘除 relay*（刀三）` | 426 | 135 | 50 | 426 | 380 | 4 | 17 |
| 本体词（本篇主刀） | `摘除 proximity（刀二）` | 406 | 131 | 50 | 406 | 358 | 4 | 17 |
| 本体词（本篇主刀） | `去掉左词界（加 (?![\w-]) 单侧版）` | 468 | 142 | 55 | 428 | 390 | 4 | 17 |
| 本体词（本篇主刀） | `去掉右词界（加 (?<![\w-]) 单侧版）` | 444 | 141 | 53 | 428 | 390 | 4 | 21 |
| 本体词（本篇主刀） | `Wi-Fi 只认 ASCII hyphen（全三刀重算）` | 428 | 135 | 50 | 428 | 380 | 4 | 17 |
| 本体词（本篇主刀） | `pair 无左界宽松版（污染量）` | 24 | 22 | 5 | 22 | 17 | 0 | 7 |
| 本体词（本篇主刀） | `IP 无右界宽松版（污染量）` | 33 | 24 | 12 | 32 | 20 | 0 | 0 |
| 本体词（本篇主刀） | `local 裸词界（未收对照，两字段）` | 237 | 80 | 27 | 112 | 112 | 0 | 1 |
| 本体词（本篇主刀） | `privileged network position 三字段` | 93 | 56 | 16 | 93 | 93 | 0 | 0 |
| 本体词（本篇主刀） | `physical access 三字段（对照通道词）` | 142 | 71 | 39 | 20 | 4 | 0 | 1 |
| 本体词（本篇主刀） | `physical proximity 三字段` | 22 | 21 | 2 | 22 | 22 | 0 | 0 |
| 本体词（本篇主刀） | `USB Restricted Mode 三字段` | 5 | 5 | 2 | 5 | 5 | 4 | 0 |
| 本体词（本篇主刀） | `SMB 三字段（协议专名）` | 27 | 12 | 1 | 9 | 9 | 0 | 0 |
| 本体词（本篇主刀） | `AFP 三字段（协议专名）` | 5 | 3 | 1 | 3 | 3 | 0 | 0 |
| 本体词（本篇主刀） | `tunnel* 三字段（刀一共词）` | 6 | 6 | 1 | 6 | 6 | 0 | 0 |
| 本体词（本篇主刀） | `广义物理近身词族（对照集，三字段）` | 348 | 113 | 51 | 217 | 189 | 4 | 2 |
| 本体词（本篇主刀） | `#131 网页与浏览器策略集（复刻口径，两字段）` | 1253 | 184 | 45 | 22 | 243 | 46 | 39 |
| 本体词（本篇主刀） | `#136 认证与凭据词族（复刻口径，两字段）` | 600 | 159 | 119 | 55 | 64 | 13 | 92 |
| 本体词（本篇主刀） | `#126 bypass 词族（复刻口径）` | 301 | 114 | 58 | 28 | 42 | 20 | 0 |
| 本体词（本篇主刀） | `#122 race condition 集（复刻口径）` | 232 | 95 | 40 | 11 | 14 | 0 | 3 |
| 本体词（本篇主刀） | `#124 拒绝服务与终止集（复刻口径）` | 1896 | 173 | 127 | 153 | 317 | 0 | 0 |
| 本体词（本篇主刀） | `#125 泄露词族（复刻口径）` | 603 | 159 | 78 | 41 | 74 | 2 | 0 |
| 本体词（本篇主刀） | `任意代码执行集（复刻口径）` | 343 | 120 | 43 | 10 | 36 | 31 | 0 |
| 本体词（本篇主刀） | `#132 图像与字体词族（复刻口径）` | 367 | 143 | 24 | 0 | 7 | 7 | 0 |
| 本体词（本篇主刀） | `#135 音视频与媒体词族（复刻口径）` | 251 | 124 | 30 | 1 | 8 | 14 | 1 |
| 本体词（本篇主刀） | `远程词界 remote（仅 Impact）` | 257 | 101 | 34 | 19 | 257 | 0 | 0 |
| 本体词（本篇主刀） | `组件名含 Network（nm 字段代理）` | 97 | 76 | 5 | 18 | 18 | 0 | 1 |
| 本体词（本篇主刀） | `组件名含 Wi-Fi（nm 字段代理）` | 88 | 55 | 2 | 41 | 41 | 0 | 1 |
| 本体词（本篇主刀） | `组件名含 Bluetooth（nm 字段代理）` | 105 | 48 | 3 | 29 | 37 | 0 | 4 |
| 本体词（本篇主刀） | `组件名含 USB（nm 字段代理）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `组件名含 Kernel（nm 字段代理）` | 745 | 153 | 1 | 38 | 77 | 9 | 0 |
| 本体词（本篇主刀） | `组件名含 WebKit（nm 字段代理）` | 1108 | 155 | 8 | 10 | 154 | 41 | 12 |
| 本体词（本篇主刀） | `组件名含 AirDrop（nm 字段代理）` | 20 | 20 | 1 | 8 | 8 | 0 | 0 |
| 本体词（本篇主刀） | `组件名含 802.1X（nm 字段代理）` | 8 | 8 | 1 | 8 | 8 | 0 | 0 |
| 本体词（本篇主刀） | `组件名含 Bonjour／mDNS（nm 字段代理）` | 74 | 47 | 1 | 27 | 35 | 0 | 0 |
| 本体词（本篇主刀） | `组件名含 VPN／IPSec（nm 字段代理）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `组件名含 DNS（nm 字段代理）` | 74 | 47 | 1 | 27 | 35 | 0 | 0 |
| 本体词（本篇主刀） | `组件名含 Relay（nm 字段代理）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `组件名含 Screen Sharing（nm 字段代理）` | 21 | 11 | 2 | 8 | 11 | 0 | 5 |
| 本体词（本篇主刀） | `组件名含 Accessibility（nm 字段代理）` | 58 | 43 | 1 | 10 | 4 | 4 | 1 |
| 检查与校验对象（38 把句级刀） | `「An attacker on the local network…」句（本地网攻击者）` | 103 | 31 | 4 | 103 | 103 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「An attacker in a privileged network position…」句（特权网络位置）` | 80 | 55 | 14 | 80 | 80 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「A user in a privileged network position…」句（用户版位置句）` | 13 | 13 | 4 | 13 | 13 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「An attacker in physical proximity…」句（物理近身）` | 22 | 21 | 2 | 22 | 22 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「An attacker in wireless proximity…」句（无线近身）` | 8 | 8 | 1 | 8 | 8 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「…Bluetooth range…」句（蓝牙射程两句）` | 2 | 2 | 1 | 2 | 2 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「An attacker on the network…」句（网络上）` | 5 | 5 | 2 | 5 | 5 | 0 | 5 |
| 检查与校验对象（38 把句级刀） | `「An attacker with a paired Bluetooth device…」句（已配对设备）` | 6 | 6 | 1 | 6 | 6 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「An attacker with control of a network … server…」句（控制服务器）` | 6 | 3 | 2 | 6 | 6 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「An attacker with physical access…」句（物理访问前提）` | 129 | 68 | 36 | 20 | 4 | 0 | 1 |
| 检查与校验对象（38 把句级刀） | `「An attacker may be able to…」句（无位置限定的攻击者）` | 165 | 104 | 38 | 9 | 3 | 0 | 14 |
| 检查与校验对象（38 把句级刀） | `「An attacker in…」其余位置状语句` | 122 | 69 | 15 | 110 | 110 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「An attacker…」其余攻击者主语句` | 610 | 148 | 91 | 260 | 238 | 25 | 20 |
| 检查与校验对象（38 把句级刀） | `「A remote attacker…」句（远程攻击者）` | 194 | 89 | 26 | 15 | 194 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「An unauthenticated user on the same network…」句（同网未认证用户）` | 8 | 8 | 2 | 8 | 8 | 0 | 7 |
| 检查与校验对象（38 把句级刀） | `「A non-privileged user…」句（非特权用户）` | 9 | 9 | 2 | 9 | 9 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「A sandboxed app…」句（沙盒应用观察网络）` | 58 | 40 | 9 | 7 | 7 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「An app may gain unauthorized access to Bluetooth…」句` | 13 | 13 | 3 | 13 | 13 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「…unauthorized access to Local Network…」句（本地网络授权）` | 8 | 8 | 3 | 8 | 8 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「An app may be able to…」句（应用侧网络访问）` | 3095 | 168 | 272 | 28 | 22 | 1 | 49 |
| 检查与校验对象（38 把句级刀） | `「A malicious app…」句（恶意应用作代理）` | 336 | 105 | 67 | 7 | 1 | 8 | 13 |
| 检查与校验对象（38 把句级刀） | `「A malicious accessory…」句（恶意配件）` | 8 | 8 | 1 | 8 | 0 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「Connecting a malicious accessory…」句（接入配件）` | 4 | 4 | 1 | 4 | 0 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「A malicious Bluetooth input device…」句（恶意蓝牙输入设备）` | 1 | 1 | 1 | 1 | 1 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「Mounting a maliciously crafted … network share…」句（挂载共享）` | 29 | 17 | 6 | 12 | 12 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「Opening a file referencing a network share…」句（引用共享的文件）` | 3 | 3 | 1 | 3 | 3 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「A file received from AirDrop…」句（隔空投送文件）` | 3 | 3 | 1 | 3 | 3 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「Network traffic may leak outside a VPN tunnel…」句（流量越出隧道）` | 6 | 6 | 1 | 6 | 6 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「iCloud Private Relay may not activate…」句（中继未生效）` | 2 | 2 | 2 | 2 | 0 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「On a device with Private Relay enabled…」句（中继开启时）` | 4 | 4 | 1 | 4 | 4 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「A website may be able to determine…」句（网站可探测 IP）` | 38 | 32 | 5 | 7 | 38 | 0 | 10 |
| 检查与校验对象（38 把句级刀） | `「Hide IP Address／Block All Remote Content…」句（邮件设置未生效）` | 4 | 4 | 1 | 4 | 4 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「A physical attack may disable USB Restricted Mode…」句` | 4 | 4 | 1 | 4 | 4 | 4 | 0 |
| 检查与校验对象（38 把句级刀） | `「USB Restricted Mode may not be applied…」句（启动期配件）` | 1 | 1 | 1 | 1 | 1 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「An integrity issue was addressed with Beacon Protection.」句（信标保护）` | 3 | 3 | 1 | 3 | 3 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「…addressed with improved network restrictions.」句（收紧网络限制）` | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `「…addressed by using HTTPS when sending information…」句（改用 HTTPS）` | 10 | 9 | 2 | 10 | 5 | 0 | 0 |
| 检查与校验对象（38 把句级刀） | `其余含三刀词句` | 9136 | 257 | 472 | 428 | 843 | 108 | 178 |
| 机制名与检测词对照 | `Wi-Fi（无线局域网专名）` | 10 | 10 | 1 | 10 | 10 | 0 | 1 |
| 机制名与检测词对照 | `Bluetooth（蓝牙专名）` | 32 | 24 | 4 | 32 | 32 | 0 | 0 |
| 机制名与检测词对照 | `AirDrop（隔空投送）` | 3 | 3 | 1 | 3 | 3 | 0 | 0 |
| 机制名与检测词对照 | `AirPlay（投屏协议）` | 7 | 7 | 1 | 7 | 7 | 0 | 7 |
| 机制名与检测词对照 | `Sidecar（随航）` | 2 | 2 | 1 | 2 | 0 | 0 | 0 |
| 机制名与检测词对照 | `iPhone Mirroring（iPhone 镜像）` | 6 | 6 | 4 | 6 | 0 | 0 | 0 |
| 机制名与检测词对照 | `Handoff／Continuity（全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `CarPlay（全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `NFC／cellular／baseband（全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `VPN（虚拟专用网）` | 6 | 6 | 1 | 6 | 6 | 0 | 0 |
| 机制名与检测词对照 | `tunnel*（隧道词族）` | 6 | 6 | 1 | 6 | 6 | 0 | 0 |
| 机制名与检测词对照 | `IPSec／IKE（隧道协议）` | 2 | 2 | 1 | 2 | 2 | 0 | 0 |
| 机制名与检测词对照 | `iCloud Private Relay（私密中继）` | 20 | 17 | 5 | 20 | 18 | 0 | 0 |
| 机制名与检测词对照 | `relay*（中继词族）` | 20 | 17 | 5 | 20 | 18 | 0 | 0 |
| 机制名与检测词对照 | `DNS（域名解析）` | 7 | 7 | 1 | 7 | 7 | 0 | 0 |
| 机制名与检测词对照 | `mDNS／Bonjour（全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `HTTP／HTTPS（超文本协议）` | 16 | 15 | 3 | 16 | 5 | 0 | 0 |
| 机制名与检测词对照 | `USB（通用串行总线）` | 5 | 5 | 2 | 5 | 5 | 4 | 0 |
| 机制名与检测词对照 | `USB Restricted Mode（USB 受限模式）` | 5 | 5 | 2 | 5 | 5 | 4 | 0 |
| 机制名与检测词对照 | `accessor*（配件词族）` | 14 | 14 | 3 | 14 | 2 | 0 | 0 |
| 机制名与检测词对照 | `pair*（配对词族四形）` | 22 | 22 | 4 | 22 | 17 | 0 | 7 |
| 机制名与检测词对照 | `wireless（无线形容词）` | 13 | 12 | 4 | 11 | 10 | 0 | 0 |
| 机制名与检测词对照 | `proximity（近场名词）` | 30 | 22 | 2 | 30 | 30 | 0 | 0 |
| 机制名与检测词对照 | `nearby（附近形容词）` | 3 | 3 | 1 | 3 | 0 | 0 | 0 |
| 机制名与检测词对照 | `…range（射程限定短语）` | 11 | 10 | 2 | 2 | 2 | 0 | 0 |
| 机制名与检测词对照 | `network（网络名词）` | 306 | 112 | 38 | 296 | 290 | 0 | 16 |
| 机制名与检测词对照 | `local network（本地网络限定）` | 112 | 37 | 7 | 112 | 112 | 0 | 1 |
| 机制名与检测词对照 | `privileged network position（特权网络位置）` | 93 | 56 | 16 | 93 | 93 | 0 | 0 |
| 机制名与检测词对照 | `packet*（报文词族）` | 20 | 20 | 3 | 20 | 20 | 0 | 0 |
| 机制名与检测词对照 | `beacon*（信标词族）` | 3 | 3 | 1 | 3 | 3 | 0 | 0 |
| 机制名与检测词对照 | `SMB／AFP（网络共享协议）` | 32 | 15 | 2 | 12 | 12 | 0 | 0 |
| 机制名与检测词对照 | `SSID／handshake／broadcast／multicast（全零对照）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `remote（远程词界）` | 257 | 101 | 34 | 19 | 257 | 0 | 0 |
| 机制名与检测词对照 | `attacker（攻击者）` | 890 | 160 | 112 | 276 | 433 | 25 | 20 |
| 机制名与检测词对照 | `physical access（物理访问前提）` | 142 | 71 | 39 | 20 | 4 | 0 | 1 |
| 机制名与检测词对照 | `unauthorized（未授权）` | 37 | 35 | 9 | 21 | 21 | 8 | 0 |
| 机制名与检测词对照 | `unauthenticated（未认证）` | 9 | 9 | 3 | 8 | 8 | 0 | 7 |
| 机制名与检测词对照 | `bypass（绕过词界）` | 301 | 114 | 58 | 28 | 42 | 20 | 0 |
| 机制名与检测词对照 | `permission／consent（授权对照）` | 639 | 148 | 105 | 14 | 50 | 0 | 78 |
| 机制名与检测词对照 | `sandbox（沙盒）` | 475 | 120 | 74 | 16 | 40 | 8 | 6 |
| 机制名与检测词对照 | `denial of service（拒绝服务）` | 470 | 155 | 62 | 96 | 164 | 0 | 0 |
| 机制名与检测词对照 | `unexpected system／app termination（终止套语）` | 1414 | 162 | 96 | 45 | 141 | 0 | 0 |
| 机制名与检测词对照 | `system termination（终止）` | 814 | 129 | 53 | 27 | 81 | 0 | 0 |
| 机制名与检测词对照 | `memory corruption（内存损坏）` | 388 | 127 | 31 | 7 | 3 | 44 | 0 |
| 机制名与检测词对照 | `leak／disclos*（泄露两词）` | 597 | 159 | 77 | 41 | 74 | 2 | 0 |
| 机制名与检测词对照 | `arbitrary code execution（任意代码执行）` | 218 | 100 | 22 | 0 | 26 | 25 | 0 |
| 机制名与检测词对照 | `track（追踪动词）` | 40 | 40 | 6 | 12 | 22 | 0 | 3 |
| 机制名与检测词对照 | `spoof（冒充）` | 72 | 58 | 8 | 11 | 60 | 0 | 0 |
| 机制名与检测词对照 | `intercept（拦截）` | 28 | 25 | 7 | 28 | 28 | 0 | 0 |
| 机制名与检测词对照 | `improved network restrictions（修复套话）` | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 机制名与检测词对照 | `using HTTPS（修复套话）` | 10 | 9 | 2 | 10 | 5 | 0 | 0 |
| 机制名与检测词对照 | `paired device（已配对设备）` | 3 | 3 | 2 | 1 | 1 | 0 | 0 |
| 机制名与检测词对照 | `restrict*（限制词族）` | 793 | 153 | 109 | 52 | 75 | 4 | 33 |
| 修复套话句（全篇共用 15 项） | `The issue was addressed with improved checks.` | 514 | 138 | 82 | 12 | 68 | 5 | 20 |
| 修复套话句（全篇共用 15 项） | `improved bounds checking` | 699 | 154 | 63 | 38 | 81 | 15 | 0 |
| 修复套话句（全篇共用 15 项） | `improved input validation` | 641 | 151 | 79 | 69 | 140 | 1 | 0 |
| 修复套话句（全篇共用 15 项） | `improved validation` | 367 | 101 | 76 | 9 | 13 | 23 | 15 |
| 修复套话句（全篇共用 15 项） | `additional checks` | 34 | 28 | 12 | 0 | 0 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `additional validation` | 112 | 65 | 20 | 0 | 8 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `improved checks to prevent unauthorized actions` | 16 | 16 | 4 | 0 | 0 | 8 | 0 |
| 修复套话句（全篇共用 15 项） | `addressed with（全形）` | 6589 | 254 | 364 | 368 | 735 | 104 | 163 |
| 修复套话句（全篇共用 15 项） | `addressed by` | 371 | 136 | 80 | 20 | 31 | 4 | 4 |
| 修复套话句（全篇共用 15 项） | `addressed through` | 247 | 129 | 48 | 28 | 47 | 0 | 11 |
| 修复套话句（全篇共用 15 项） | `validation 词族` | 1341 | 179 | 168 | 90 | 184 | 24 | 15 |
| 修复套话句（全篇共用 15 项） | `check(s)／checking 词族` | 2017 | 227 | 201 | 104 | 243 | 46 | 48 |
| 修复套话句（全篇共用 15 项） | `sanitiz* 词族` | 93 | 54 | 21 | 0 | 3 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `enforce* 词族` | 53 | 34 | 4 | 0 | 0 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `restrict* 词族` | 793 | 153 | 109 | 52 | 75 | 4 | 33 |
| 外部集对照（12 套，含 #122–#126） | `远程可达 R` | 843 | 178 | 72 | 380 | 843 | 4 | 27 |
| 外部集对照（12 套，含 #122–#126） | `without 词界集 WO` | 178 | 97 | 37 | 17 | 27 | 0 | 178 |
| 外部集对照（12 套，含 #122–#126） | `exploit* 集 E` | 108 | 76 | 12 | 4 | 4 | 108 | 0 |
| 外部集对照（12 套，含 #122–#126） | `user 词界集 UU` | 1627 | 175 | 200 | 82 | 130 | 0 | 105 |
| 外部集对照（12 套，含 #122–#126） | `动名词开头集 G` | 2179 | 224 | 98 | 19 | 108 | 53 | 0 |
| 外部集对照（12 套，含 #122–#126） | `非处理/解析类 HI` | 273 | 102 | 35 | 19 | 108 | 0 | 0 |
| 外部集对照（12 套，含 #122–#126） | `≥2 后果类 M2` | 185 | 82 | 35 | 0 | 23 | 0 | 0 |
| 外部集对照（12 套，含 #122–#126） | `race condition 集 RC（#122）` | 232 | 95 | 40 | 11 | 14 | 0 | 3 |
| 外部集对照（12 套，含 #122–#126） | `logic 集 L（#123）` | 550 | 162 | 81 | 39 | 99 | 9 | 24 |
| 外部集对照（12 套，含 #122–#126） | `拒绝服务集 Ds（#124）` | 470 | 155 | 62 | 96 | 164 | 0 | 0 |
| 外部集对照（12 套，含 #122–#126） | `泄露集 X（#125）` | 427 | 141 | 54 | 11 | 11 | 2 | 0 |
| 外部集对照（12 套，含 #122–#126） | `绕过集 BP（#126）` | 301 | 114 | 58 | 28 | 42 | 20 | 0 |
| 后果强度八类 | `ACE 类` | 336 | 120 | 43 | 10 | 36 | 31 | 0 |
| 后果强度八类 | `BYP 类` | 301 | 114 | 58 | 28 | 42 | 20 | 0 |
| 后果强度八类 | `DIS 类` | 603 | 159 | 78 | 41 | 74 | 2 | 0 |
| 后果强度八类 | `DOS 类` | 2014 | 176 | 130 | 153 | 343 | 0 | 0 |
| 后果强度八类 | `FULL 类` | 63 | 43 | 18 | 0 | 7 | 0 | 0 |
| 后果强度八类 | `MEM 类` | 390 | 127 | 31 | 7 | 3 | 44 | 0 |
| 后果强度八类 | `PRI 类` | 124 | 62 | 30 | 0 | 0 | 8 | 0 |
| 后果强度八类 | `SBX 类` | 475 | 120 | 74 | 16 | 40 | 8 | 6 |
| **合计** | 299 项（并集全零 54 项、非零 245 项） | 428 | — | — | 428 | 843 | 108 | 178 |

---

# 附表 B 含主刀整句 × 归桶 × 字段 × 外部集（77 行）

| distinct 整句 | 归入桶 | 句次 | Impact 句 | Desc 句 | 页 | ∩R | ∩E | ∩M2 | ∩L | ∩RC |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| An attacker on the local network may be able to cause a denial-of-service | 「An attacker on the local network…」句（本地网攻击者） | 49 | 49 | 0 | 28 | 49 | 0 | 0 | 0 | 0 |
| An attacker in a privileged network position may be able to intercept network traffic | 「An attacker in a privileged network position…」句（特权网络位置） | 23 | 23 | 0 | 20 | 23 | 0 | 0 | 11 | 0 |
| An attacker on the local network may cause an unexpected app termination | 「An attacker on the local network…」句（本地网攻击者） | 22 | 22 | 0 | 13 | 22 | 0 | 0 | 0 | 0 |
| An app may gain unauthorized access to Bluetooth | 「An app may gain unauthorized access to Bluetooth…」句 | 13 | 13 | 0 | 13 | 13 | 0 | 0 | 0 | 0 |
| An attacker in a privileged network position may be able to cause a denial-of-service | 「An attacker in a privileged network position…」句（特权网络位置） | 13 | 13 | 0 | 12 | 13 | 0 | 0 | 0 | 0 |
| An attacker on the local network may be able to corrupt process memory | 「An attacker on the local network…」句（本地网攻击者） | 12 | 12 | 0 | 10 | 12 | 0 | 0 | 0 | 0 |
| This issue was addressed by using HTTPS when sending information over the network. | 「…addressed by using HTTPS when sending information…」句（改用 HTTPS） | 10 | 0 | 10 | 9 | 5 | 0 | 0 | 0 | 0 |
| A non-privileged user may be able to modify restricted network settings | 「A non-privileged user…」句（非特权用户） | 9 | 9 | 0 | 9 | 9 | 0 | 0 | 0 | 0 |
| A user in a privileged network position may be able to cause a denial-of-service | 「A user in a privileged network position…」句（用户版位置句） | 9 | 9 | 0 | 9 | 9 | 0 | 0 | 0 | 0 |
| An attacker in physical proximity may be able to corrupt process memory | 「An attacker in physical proximity…」句（物理近身） | 9 | 9 | 0 | 9 | 9 | 0 | 0 | 0 | 0 |
| Mounting a maliciously crafted SMB network share may lead to system termination | 「Mounting a maliciously crafted … network share…」句（挂载共享） | 9 | 9 | 0 | 9 | 9 | 0 | 0 | 0 | 3 |
| A malicious accessory may be able to cause unexpected app termination | 「A malicious accessory…」句（恶意配件） | 8 | 8 | 0 | 8 | 0 | 0 | 0 | 0 | 0 |
| A remote attacker may be able to bypass network filters | 「A remote attacker…」句（远程攻击者） | 8 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 |
| An app may gain unauthorized access to Local Network | 「…unauthorized access to Local Network…」句（本地网络授权） | 8 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 |
| An attacker in a privileged network position may be able to spoof network packets | 「An attacker in a privileged network position…」句（特权网络位置） | 8 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 8 |
| An attacker in physical proximity may be able to cause an out-of-bounds read in kernel memory | 「An attacker in physical proximity…」句（物理近身） | 8 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 |
| An attacker in wireless proximity may be able to cause unexpected system termination | 「An attacker in wireless proximity…」句（无线近身） | 8 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 |
| A remote attacker may be able to view leaked DNS queries with Private Relay turned on | 「A remote attacker…」句（远程攻击者） | 7 | 7 | 0 | 7 | 7 | 0 | 0 | 7 | 0 |
| A sandboxed app may be able to observe system-wide network connections | 「A sandboxed app…」句（沙盒应用观察网络） | 7 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 |
| A website may be able to determine a user's IP address with Private Relay turned on | 「A website may be able to determine…」句（网站可探测 IP） | 7 | 7 | 0 | 4 | 7 | 0 | 0 | 0 | 0 |
| An app may be able to bypass network restrictions | 「An app may be able to…」句（应用侧网络访问） | 7 | 7 | 0 | 7 | 7 | 0 | 0 | 4 | 0 |
| An attacker in a privileged network position may be able to perform denial-of-service attack using crafted Wi-Fi packets | 「An attacker in a privileged network position…」句（特权网络位置） | 7 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 |
| An attacker on the local network may be able to bypass authentication policy | 「An attacker on the local network…」句（本地网攻击者） | 7 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 |
| An attacker on the local network may be able to leak sensitive user information | 「An attacker on the local network…」句（本地网攻击者） | 7 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 |
| An unauthenticated user on the same network as a signed-in Mac could send it AirPlay commands without pairing | 「An unauthenticated user on the same network…」句（同网未认证用户） | 7 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 |
| A malicious app acting as a HTTPS proxy could get access to sensitive user data | 「A malicious app…」句（恶意应用作代理） | 6 | 6 | 0 | 6 | 0 | 0 | 0 | 0 | 0 |
| An attacker in a privileged network position may be able to leak sensitive user information | 「An attacker in a privileged network position…」句（特权网络位置） | 6 | 6 | 0 | 6 | 6 | 0 | 0 | 3 | 0 |
| An attacker in a privileged network position may be able to track a user's activity | 「An attacker in a privileged network position…」句（特权网络位置） | 6 | 6 | 0 | 6 | 6 | 0 | 0 | 0 | 0 |
| An attacker may be able to track users through their IP address | 「An attacker may be able to…」句（无位置限定的攻击者） | 6 | 6 | 0 | 6 | 0 | 0 | 0 | 0 | 0 |
| An attacker on the local network may corrupt process memory | 「An attacker on the local network…」句（本地网攻击者） | 6 | 6 | 0 | 6 | 6 | 0 | 0 | 0 | 0 |
| An attacker with a paired Bluetooth device may be able to execute arbitrary code | 「An attacker with a paired Bluetooth device…」句（已配对设备） | 6 | 6 | 0 | 6 | 6 | 0 | 0 | 0 | 0 |
| Network traffic may leak outside a VPN tunnel | 「Network traffic may leak outside a VPN tunnel…」句（流量越出隧道） | 6 | 6 | 0 | 6 | 6 | 0 | 0 | 6 | 0 |
| An attacker in a privileged network position may be able to perform denial-of-service attack using crafted Bluetooth packets | 「An attacker in a privileged network position…」句（特权网络位置） | 5 | 5 | 0 | 5 | 5 | 0 | 0 | 0 | 0 |
| An attacker in physical proximity may be able to cause a denial-of-service | 「An attacker in physical proximity…」句（物理近身） | 5 | 5 | 0 | 5 | 5 | 0 | 0 | 0 | 0 |
| An attacker on the network may be able to authenticate to Screen Sharing without valid credentials | 「An attacker on the network…」句（网络上） | 5 | 5 | 0 | 5 | 5 | 0 | 0 | 0 | 0 |
| An attacker with physical access to an unlocked device paired with a Mac may be able to view sensitive user information in system logging | 「An attacker with physical access…」句（物理访问前提） | 5 | 5 | 0 | 5 | 0 | 0 | 0 | 0 | 0 |
| "Hide IP Address" and "Block All Remote Content" may not apply to all mail content | 「Hide IP Address／Block All Remote Content…」句（邮件设置未生效） | 4 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 |
| A physical attack may disable USB Restricted Mode on a locked device. | 「A physical attack may disable USB Restricted Mode…」句 | 4 | 4 | 0 | 4 | 4 | 4 | 0 | 0 | 0 |
| An app may be able to access Bluetooth device information | 「An app may be able to…」句（应用侧网络访问） | 4 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 |
| An attacker in a privileged network position may be able to alter network traffic | 「An attacker in a privileged network position…」句（特权网络位置） | 4 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 |
| An attacker in a privileged network position may be able to modify network traffic | 「An attacker in a privileged network position…」句（特权网络位置） | 4 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 |
| Connecting a malicious accessory may cause unexpected system termination | 「Connecting a malicious accessory…」句（接入配件） | 4 | 4 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| On a device with Private Relay enabled, adding a website to the Safari Reading List may reveal the originating IP address to the website | 「On a device with Private Relay enabled…」句（中继开启时） | 4 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 |
| A file received from AirDrop may not have the quarantine flag applied | 「A file received from AirDrop…」句（隔空投送文件） | 3 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 |
| A user in a privileged network position may be able to leak sensitive user information | 「A user in a privileged network position…」句（用户版位置句） | 3 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 |
| An app may be able to access sensitive network connection information | 「An app may be able to…」句（应用侧网络访问） | 3 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 |
| An app may be able to connect to a network share without user consent | 「An app may be able to…」句（应用侧网络访问） | 3 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 |
| An app may be able to intercept network connections intended for another process | 「An app may be able to…」句（应用侧网络访问） | 3 | 3 | 0 | 3 | 3 | 0 | 0 | 3 | 0 |
| An attacker may be able to force a device to disconnect from a secure network | 「An attacker may be able to…」句（无位置限定的攻击者） | 3 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 |
| An attacker with control of a network directory server may be able to execute arbitrary code with root privileges | 「An attacker with control of a network … server…」句（控制服务器） | 3 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 |
| An attacker with control of a network file server may be able to cause unexpected system termination | 「An attacker with control of a network … server…」句（控制服务器） | 3 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 |
| An attacker with physical access may be able to access sensitive user data during iPhone Mirroring | 「An attacker with physical access…」句（物理访问前提） | 3 | 3 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| An attacker with physical access to a locked device may be able to Control Nearby Devices via accessibility features | 「An attacker with physical access…」句（物理访问前提） | 3 | 3 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| An integrity issue was addressed with Beacon Protection. | 「An integrity issue was addressed with Beacon Protection.」句（信标保护） | 3 | 0 | 3 | 3 | 3 | 0 | 0 | 0 | 0 |
| Mounting a maliciously crafted AFP network share may lead to system termination | 「Mounting a maliciously crafted … network share…」句（挂载共享） | 3 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 |
| Opening a file referencing a network share may lead to unexpected software execution | 「Opening a file referencing a network share…」句（引用共享的文件） | 3 | 3 | 0 | 3 | 3 | 0 | 0 | 3 | 0 |
| An attacker in a privileged network position may be able to bypass IPSec authentication and intercept network traffic | 「An attacker in a privileged network position…」句（特权网络位置） | 2 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 |
| An attacker in a privileged network position may be able to inject keystrokes by spoofing a keyboard | 「An attacker in a privileged network position…」句（特权网络位置） | 2 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 |
| An attacker with physical access to a device may be able to override managed Wi-Fi profiles | 「An attacker with physical access…」句（物理访问前提） | 2 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 |
| An attacker with physical access to a macOS device with Sidecar enabled may be able to bypass the Lock Screen | 「An attacker with physical access…」句（物理访问前提） | 2 | 2 | 0 | 2 | 0 | 0 | 0 | 0 | 0 |
| iCloud Private Relay may not activate when more than one user is logged in at the same time | 「iCloud Private Relay may not activate…」句（中继未生效） | 2 | 2 | 0 | 2 | 0 | 0 | 0 | 2 | 0 |
| A malicious Bluetooth input device may bypass pairing | 「A malicious Bluetooth input device…」句（恶意蓝牙输入设备） | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| A malicious app may be able to change network settings | 「A malicious app…」句（恶意应用作代理） | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| A user in a privileged network position may be able to leak sensitive information | 「A user in a privileged network position…」句（用户版位置句） | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| An app may be able to access local network devices without user consent | 「An app may be able to…」句（应用侧网络访问） | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| An app may be able to observe the hostnames of new network connections | 「An app may be able to…」句（应用侧网络访问） | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| An attacker with physical access may be able to use Visual Intelligence to access sensitive user data during iPhone Mirroring | 「An attacker with physical access…」句（物理访问前提） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An attacker with physical access to a locked device may be able to bypass Digital ID authentication when iPhone Mirroring is active | 「An attacker with physical access…」句（物理访问前提） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An attacker with physical access to an unlocked device may be able to view Wi-Fi passwords without authentication | 「An attacker with physical access…」句（物理访问前提） | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| An attacker with physical access to iPhone may be able to take and view screenshots of sensitive data from the iPhone during iPhone Mirroring with Mac | 「An attacker with physical access…」句（物理访问前提） | 1 | 1 | 0 | 1 | 0 | 0 | 0 | 0 | 0 |
| An attacker with physical access to the accessory may be able to extract its Bluetooth pairing key and monitor Bluetooth traffic | 「An attacker with physical access…」句（物理访问前提） | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| An attacker within Bluetooth range may be able to listen through the microphone of a device which is not yet paired and actively seeking pair requests | 「…Bluetooth range…」句（蓝牙射程两句） | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| An authorization issue was addressed with improved network restrictions. | 「…addressed with improved network restrictions.」句（收紧网络限制） | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| An unauthenticated user on the same network as a Compressor server may be able to execute arbitrary code | 「An unauthenticated user on the same network…」句（同网未认证用户） | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| USB Restricted Mode may not be applied to accessories connected during boot | 「USB Restricted Mode may not be applied…」句（启动期配件） | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| When your headphones are seeking a connection request to one of your previously paired devices, an attacker in Bluetooth range might be able to spoof the intended source device and gain access to your headphones | 「…Bluetooth range…」句（蓝牙射程两句） | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| **合计** | 38 桶 | 436 | 422 | 14 | 页列合计 396 | 388 | 4 | 0 | 39 | 11 |

---

# 附表 C 句形桶与优先序闭合分区（36 行）

| 桶 | 句次 | 段 | 页 | 名 | Impact 句 | Desc 句 | ∩R | ∩E |
|---|---:|---:|---:|---:|---:|---:|---:|---:|
| 「An attacker on the local network…」句（本地网攻击者） | 103 | 103 | 31 | 4 | 103 | 0 | 103 | 0 |
| 「An attacker in a privileged network position…」句（特权网络位置） | 80 | 80 | 55 | 14 | 80 | 0 | 80 | 0 |
| 「An app may be able to…」句（应用侧网络访问） | 22 | 22 | 13 | 7 | 22 | 0 | 22 | 0 |
| 「An attacker in physical proximity…」句（物理近身） | 22 | 22 | 21 | 2 | 22 | 0 | 22 | 0 |
| 「An attacker with physical access…」句（物理访问前提） | 20 | 20 | 18 | 8 | 20 | 0 | 4 | 0 |
| 「A remote attacker…」句（远程攻击者） | 15 | 15 | 15 | 2 | 15 | 0 | 15 | 0 |
| 「A user in a privileged network position…」句（用户版位置句） | 13 | 13 | 13 | 4 | 13 | 0 | 13 | 0 |
| 「An app may gain unauthorized access to Bluetooth…」句 | 13 | 13 | 13 | 3 | 13 | 0 | 13 | 0 |
| 「Mounting a maliciously crafted … network share…」句（挂载共享） | 12 | 12 | 12 | 2 | 12 | 0 | 12 | 0 |
| 「…addressed by using HTTPS when sending information…」句（改用 HTTPS） | 10 | 10 | 9 | 2 | 0 | 10 | 5 | 0 |
| 「A non-privileged user…」句（非特权用户） | 9 | 9 | 9 | 2 | 9 | 0 | 9 | 0 |
| 「An attacker may be able to…」句（无位置限定的攻击者） | 9 | 9 | 9 | 2 | 9 | 0 | 3 | 0 |
| 「A malicious accessory…」句（恶意配件） | 8 | 8 | 8 | 1 | 8 | 0 | 0 | 0 |
| 「An attacker in wireless proximity…」句（无线近身） | 8 | 8 | 8 | 1 | 8 | 0 | 8 | 0 |
| 「An unauthenticated user on the same network…」句（同网未认证用户） | 8 | 8 | 8 | 2 | 8 | 0 | 8 | 0 |
| 「…unauthorized access to Local Network…」句（本地网络授权） | 8 | 8 | 8 | 3 | 8 | 0 | 8 | 0 |
| 「A malicious app…」句（恶意应用作代理） | 7 | 7 | 7 | 2 | 7 | 0 | 1 | 0 |
| 「A sandboxed app…」句（沙盒应用观察网络） | 7 | 7 | 7 | 1 | 7 | 0 | 7 | 0 |
| 「A website may be able to determine…」句（网站可探测 IP） | 7 | 7 | 4 | 2 | 7 | 0 | 7 | 0 |
| 「An attacker with a paired Bluetooth device…」句（已配对设备） | 6 | 6 | 6 | 1 | 6 | 0 | 6 | 0 |
| 「An attacker with control of a network … server…」句（控制服务器） | 6 | 6 | 3 | 2 | 6 | 0 | 6 | 0 |
| 「Network traffic may leak outside a VPN tunnel…」句（流量越出隧道） | 6 | 6 | 6 | 1 | 6 | 0 | 6 | 0 |
| 「An attacker on the network…」句（网络上） | 5 | 5 | 5 | 2 | 5 | 0 | 5 | 0 |
| 「A physical attack may disable USB Restricted Mode…」句 | 4 | 4 | 4 | 1 | 4 | 0 | 4 | 4 |
| 「Connecting a malicious accessory…」句（接入配件） | 4 | 4 | 4 | 1 | 4 | 0 | 0 | 0 |
| 「Hide IP Address／Block All Remote Content…」句（邮件设置未生效） | 4 | 4 | 4 | 1 | 4 | 0 | 4 | 0 |
| 「On a device with Private Relay enabled…」句（中继开启时） | 4 | 4 | 4 | 1 | 4 | 0 | 4 | 0 |
| 「A file received from AirDrop…」句（隔空投送文件） | 3 | 3 | 3 | 1 | 3 | 0 | 3 | 0 |
| 「An integrity issue was addressed with Beacon Protection.」句（信标保护） | 3 | 3 | 3 | 1 | 0 | 3 | 3 | 0 |
| 「Opening a file referencing a network share…」句（引用共享的文件） | 3 | 3 | 3 | 1 | 3 | 0 | 3 | 0 |
| 「iCloud Private Relay may not activate…」句（中继未生效） | 2 | 2 | 2 | 2 | 2 | 0 | 0 | 0 |
| 「…Bluetooth range…」句（蓝牙射程两句） | 2 | 2 | 2 | 1 | 2 | 0 | 2 | 0 |
| 「A malicious Bluetooth input device…」句（恶意蓝牙输入设备） | 1 | 1 | 1 | 1 | 1 | 0 | 1 | 0 |
| 「USB Restricted Mode may not be applied…」句（启动期配件） | 1 | 1 | 1 | 1 | 1 | 0 | 1 | 0 |
| 「…addressed with improved network restrictions.」句（收紧网络限制） | 1 | 1 | 1 | 1 | 0 | 1 | 0 | 0 |
| **合计** | 436 | 段列可重叠 | 页列合计 320 | 名列合计 83 | 422 | 14 | — | — |

## 附表 C 续表：句子打头与修复动词形态（优先序闭合分区）

| 形态刀 | 句次 | 段 | 页 | 名 |
|---|---:|---:|---:|---:|
| 「The／This issue was addressed …」打头 | 10 | 10 | 9 | 2 |
| 「A／An X issue was addressed …」打头 | 4 | 4 | 4 | 2 |
| 含「may」（影响句情态） | 408 | 408 | 134 | 49 |
| 其余句式 | 14 | 14 | 11 | 3 |
| **合计** | 436 | — | — | — |

---

# 附表 D 页面、编号、致谢、版式与字段落位分箱（26 行）

| 块 | 分箱 | 计数 | 说明 |
|---|---|---:|---|
| WNCH 段所在页的段数 | 1 | 57 | 共 135 页含本篇条目（全树 257 页） |
| WNCH 段所在页的段数 | 2 | 25 | 共 135 页含本篇条目（全树 257 页） |
| WNCH 段所在页的段数 | 3 | 11 | 共 135 页含本篇条目（全树 257 页） |
| WNCH 段所在页的段数 | 4 | 10 | 共 135 页含本篇条目（全树 257 页） |
| WNCH 段所在页的段数 | 5 | 14 | 共 135 页含本篇条目（全树 257 页） |
| WNCH 段所在页的段数 | 6-9 | 11 | 共 135 页含本篇条目（全树 257 页） |
| WNCH 段所在页的段数 | 10-19 | 6 | 共 135 页含本篇条目（全树 257 页） |
| WNCH 段所在页的段数 | 20-49 | 1 | 共 135 页含本篇条目（全树 257 页） |
| WNCH 段所在页的段数 | 合计 | 135 | 校验=135 页、WNCH 段 428 |
| Top10 页 | 前 10 页合计 | 124 | 占 428 段的 29.0% |
| 同页共存 | 135 页全部段 | 7767 | 本篇占该页集 5.51%，每页均 3.17 段（第三套分母） |
| 同页共存 | 全本篇页 | 9 | 该页所有段都被本篇抓到 |
| 编号数分箱 | 1 个 | 410 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 2 个 | 18 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 合计 | 428 | 带编号段=428／428，distinct 编号=111，本集独占=111，与他集共享=0 |
| 致谢人数分箱 | 1 人 | 187 | — |
| 致谢人数分箱 | 2 人 | 161 | — |
| 致谢人数分箱 | 3 人 | 34 | — |
| 致谢人数分箱 | 4 人 | 19 | — |
| 致谢人数分箱 | 5 人 | 14 | — |
| 致谢人数分箱 | 6-9 人 | 12 | — |
| 致谢人数分箱 | 9+ 人 | 1 | — |
| 致谢人数分箱 | 合计 | 428 | 人次=855，均值=2.00，最大=15（全树 1.80／37） |
| 版式域 | 域 B（11 页粗体版式） | 1 | 域 B 全段 91，占比 1.10%；域 A 427／9,045 ＝ 4.72% |
| 字段落位 | Impact 侧 | 422 | Description 侧 14；仅I 414／仅D 6／I∩D 8（两组闭合 428） |
| 句级 | 428 段的本篇句数 | 1 句 420／2 句 8 | 句次合计 436（I 422＋D 14），distinct 句 76 |

---

# 附表 E 中文层 78 词（79 行）

| 组 | 中文词 | 出现次数 | 覆盖页 | 覆盖页占比 | 最集中页面 |
|---|---|---:|---:|---:|---|
| 无线与局域网类 | 无线 | 1240 | 178 | 100.0% | Apple 设备的无线局域网隐私（24 次） |
| 无线与局域网类 | 局域网 | 826 | 178 | 100.0% | Apple 设备的无线局域网隐私（20 次） |
| 无线与局域网类 | Wi-Fi | 1 | 1 | 0.6% | Apple Configurator 安全性（1 次） |
| 无线与局域网类 | WiFi | 0 | 0 | 0.0% | — |
| 无线与局域网类 | 无线网络 | 384 | 178 | 100.0% | 文稿修订记录（8 次） |
| 无线与局域网类 | 无线网 | 384 | 178 | 100.0% | 文稿修订记录（8 次） |
| 无线与局域网类 | 网络设置 | 2 | 2 | 1.1% | iPad 和 iPhone 的物理配对模型安全性（1 次） |
| 无线与局域网类 | 网络配置 | 0 | 0 | 0.0% | — |
| 蓝牙与配件类 | 蓝牙 | 243 | 178 | 100.0% | 蓝牙安全性（21 次） |
| 蓝牙与配件类 | 配件 | 456 | 178 | 100.0% | HomeKit 通信安全性（42 次） |
| 蓝牙与配件类 | 外设 | 3 | 3 | 1.7% | 生物识别安全性（1 次） |
| 蓝牙与配件类 | 配对 | 699 | 178 | 100.0% | watchOS 系统安全性（27 次） |
| 蓝牙与配件类 | 已配对 | 3 | 2 | 1.1% | HomeKit 通信安全性（2 次） |
| 蓝牙与配件类 | 耳机 | 0 | 0 | 0.0% | — |
| 蓝牙与配件类 | 键盘 | 230 | 178 | 100.0% | 配备触控 ID 的妙控键盘（28 次） |
| 蓝牙与配件类 | 鼠标 | 0 | 0 | 0.0% | — |
| 蓝牙与配件类 | 转换器 | 0 | 0 | 0.0% | — |
| 投送与投屏类 | 隔空投送 | 211 | 178 | 100.0% | 隔空投送安全性（21 次） |
| 投送与投屏类 | 投送 | 211 | 178 | 100.0% | 隔空投送安全性（21 次） |
| 投送与投屏类 | 屏幕镜像 | 0 | 0 | 0.0% | — |
| 投送与投屏类 | 镜像 | 10 | 2 | 1.1% | 自动解锁 Apple 设备（9 次） |
| 投送与投屏类 | 无线投屏 | 0 | 0 | 0.0% | — |
| 投送与投屏类 | 接力 | 212 | 178 | 100.0% | 接力安全性（26 次） |
| 投送与投屏类 | 连续互通 | 362 | 178 | 100.0% | 连续互通安全性概览（5 次） |
| 投送与投屏类 | 侧边栏 | 0 | 0 | 0.0% | — |
| 投送与投屏类 | 随航 | 0 | 0 | 0.0% | — |
| 近场与距离类 | 近场 | 0 | 0 | 0.0% | — |
| 近场与距离类 | 近距离 | 10 | 7 | 3.9% | 使用 Apple 钱包访问（2 次） |
| 近场与距离类 | 附近 | 14 | 12 | 6.7% | 隔空投送安全性（2 次） |
| 近场与距离类 | 临近 | 0 | 0 | 0.0% | — |
| 近场与距离类 | 射程 | 0 | 0 | 0.0% | — |
| 近场与距离类 | 范围内 | 10 | 8 | 4.5% | watchOS 系统安全性（3 次） |
| 近场与距离类 | 在范围内 | 0 | 0 | 0.0% | — |
| 近场与距离类 | 物理接近 | 0 | 0 | 0.0% | — |
| 近场与距离类 | 邻近 | 2 | 1 | 0.6% | 通过 Apple Pay 使用付款卡支付（2 次） |
| 网络与报文类 | 网络 | 1103 | 178 | 100.0% | Apple 设备的无线局域网隐私（23 次） |
| 网络与报文类 | 本地网络 | 15 | 7 | 3.9% | 使用 HomeKit 保护路由器（5 次） |
| 网络与报文类 | 同一网络 | 1 | 1 | 0.6% | IPv6 安全性（1 次） |
| 网络与报文类 | 数据包 | 18 | 12 | 6.7% | 账户恢复联系人安全性（5 次） |
| 网络与报文类 | 报文 | 0 | 0 | 0.0% | — |
| 网络与报文类 | 流量 | 10 | 6 | 3.4% | 虚拟专用网络 (VPN) 安全性（4 次） |
| 网络与报文类 | 带宽 | 4 | 1 | 0.6% | 使用卫星收发紧急信息（4 次） |
| 网络与报文类 | 连接 | 733 | 178 | 100.0% | 安全激活数据连接（24 次） |
| 网络与报文类 | 主机名 | 0 | 0 | 0.0% | — |
| 网络与报文类 | 服务器 | 215 | 56 | 31.5% | iCloud 高级数据保护（13 次） |
| 网络与报文类 | 共享文件夹 | 2 | 1 | 0.6% | iCloud 高级数据保护（2 次） |
| 网络与报文类 | 网络共享 | 2 | 2 | 1.1% | 将信用卡或借记卡添加到 Apple Pay（1 次） |
| 协议与隧道类 | 域名 | 7 | 4 | 2.2% | 接力安全性（4 次） |
| 协议与隧道类 | 域名解析 | 1 | 1 | 0.6% | iCloud 专用代理安全性（1 次） |
| 协议与隧道类 | 虚拟专用网 | 7 | 3 | 1.7% | 虚拟专用网络 (VPN) 安全性（3 次） |
| 协议与隧道类 | 隧道 | 4 | 1 | 0.6% | 自动解锁 Apple 设备（4 次） |
| 协议与隧道类 | 中继 | 201 | 178 | 100.0% | iPhone 蜂窝网络通话中继安全性（5 次） |
| 协议与隧道类 | 私密中继 | 0 | 0 | 0.0% | — |
| 协议与隧道类 | 代理 | 195 | 178 | 100.0% | iCloud 专用代理安全性（10 次） |
| 协议与隧道类 | 加密隧道 | 0 | 0 | 0.0% | — |
| 协议与隧道类 | 超文本 | 0 | 0 | 0.0% | — |
| 协议与隧道类 | 安全网络 | 2 | 2 | 1.1% | Apple 设备的无线局域网隐私（1 次） |
| 后果强度类 | 拒绝服务 | 1 | 1 | 0.6% | macOS 中的防火墙安全性（1 次） |
| 后果强度类 | 意外终止 | 0 | 0 | 0.0% | — |
| 后果强度类 | 崩溃 | 6 | 4 | 2.2% | Apple 操作系统中的后台安全改进（3 次） |
| 后果强度类 | 内存损坏 | 4 | 2 | 1.1% | 操作系统完整性（3 次） |
| 后果强度类 | 泄露 | 30 | 11 | 6.2% | 密码监视（15 次） |
| 后果强度类 | 窃取 | 4 | 2 | 1.1% | Apple 操作系统中的量子安全加密技术（3 次） |
| 后果强度类 | 冒充 | 1 | 1 | 0.6% | 车钥匙交易（1 次） |
| 后果强度类 | 拦截 | 3 | 2 | 1.1% | 单点登录安全性（2 次） |
| 后果强度类 | 跟踪 | 19 | 12 | 6.7% | 生物识别安全性（4 次） |
| 后果强度类 | 任意代码执行 | 0 | 0 | 0.0% | — |
| 后果强度类 | 提权 | 0 | 0 | 0.0% | — |
| 后果强度类 | 越权 | 0 | 0 | 0.0% | — |
| 修复套话类 | 改进的 | 3 | 3 | 1.7% | Apple 操作系统中的后台安全改进（1 次） |
| 修复套话类 | 附加验证 | 0 | 0 | 0.0% | — |
| 修复套话类 | 已修复 | 0 | 0 | 0.0% | — |
| 修复套话类 | 新增 | 15 | 9 | 5.1% | 文稿修订记录（6 次） |
| 修复套话类 | 移除 | 104 | 38 | 21.3% | Apple 操作系统中的后台安全改进（17 次） |
| 修复套话类 | 约束 | 1 | 1 | 0.6% | 操作系统完整性（1 次） |
| 修复套话类 | 校验 | 19 | 3 | 1.7% | Apple 钱包中证件的安全性（16 次） |
| 修复套话类 | 默认启用 | 3 | 2 | 1.1% | TLS 安全性（2 次） |
| 修复套话类 | 限制网络 | 1 | 1 | 0.6% | 接入无线网络时的安全功能（1 次） |
| **合计** | 78 词（全零 25 词、非零 53 词） | — | — | 178 页可用 | — |

---

# 附表 F 组件名层 50 名（51 行）

| 组件名 | WNCH 段 | 页 | ∩R | ∩E | ∩M2 | ∩L | ∩BP | 该组件含 Impact 全集 | WNCH 占比 |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| AirPlay | 77 | 14 | 77 | 0 | 0 | 0 | 7 | 77 | 100.0% |
| Wi-Fi | 41 | 32 | 41 | 0 | 0 | 0 | 0 | 58 | 70.7% |
| Kernel | 38 | 34 | 37 | 0 | 0 | 15 | 8 | 681 | 5.6% |
| mDNSResponder | 27 | 19 | 27 | 0 | 0 | 0 | 0 | 53 | 50.9% |
| Bluetooth | 18 | 17 | 18 | 0 | 0 | 0 | 0 | 41 | 43.9% |
| CoreUtils | 15 | 15 | 15 | 0 | 0 | 0 | 0 | 15 | 100.0% |
| MobileAccessoryUpdater | 12 | 12 | 0 | 0 | 0 | 0 | 0 | 13 | 92.3% |
| SMB | 12 | 9 | 12 | 0 | 0 | 0 | 0 | 58 | 20.7% |
| Core Bluetooth | 11 | 8 | 11 | 0 | 0 | 0 | 1 | 16 | 68.8% |
| Accessibility | 10 | 10 | 4 | 4 | 0 | 0 | 0 | 46 | 21.7% |
| WebKit | 10 | 10 | 10 | 0 | 0 | 7 | 0 | 910 | 1.1% |
| Security | 9 | 9 | 3 | 0 | 0 | 0 | 0 | 91 | 9.9% |
| 802.1X | 8 | 8 | 8 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| AirDrop | 8 | 8 | 8 | 0 | 0 | 0 | 0 | 14 | 57.1% |
| CFNetwork | 8 | 8 | 8 | 0 | 0 | 0 | 0 | 20 | 40.0% |
| CUPS | 7 | 6 | 7 | 0 | 0 | 0 | 0 | 43 | 16.3% |
| Heimdal | 7 | 6 | 7 | 0 | 0 | 0 | 0 | 19 | 36.8% |
| Mail | 7 | 7 | 7 | 0 | 0 | 3 | 0 | 36 | 19.4% |
| Networking | 7 | 7 | 0 | 0 | 0 | 1 | 0 | 10 | 70.0% |
| libxpc | 7 | 7 | 7 | 0 | 0 | 0 | 0 | 80 | 8.8% |
| Apple Account | 6 | 6 | 6 | 0 | 0 | 0 | 0 | 25 | 24.0% |
| Audio | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 95 | 5.3% |
| FrontBoard | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 6 | 83.3% |
| Passwords | 5 | 4 | 5 | 0 | 0 | 0 | 0 | 9 | 55.6% |
| Screen Sharing Server | 5 | 5 | 5 | 0 | 0 | 3 | 0 | 16 | 31.2% |
| Telephony | 5 | 4 | 4 | 0 | 0 | 0 | 2 | 8 | 62.5% |
| Authentication Services | 4 | 4 | 4 | 0 | 0 | 0 | 0 | 18 | 22.2% |
| Safari | 4 | 4 | 4 | 0 | 0 | 0 | 0 | 92 | 4.3% |
| Sandbox | 4 | 4 | 4 | 0 | 0 | 4 | 4 | 65 | 6.2% |
| Web Extensions | 4 | 4 | 4 | 0 | 0 | 0 | 0 | 14 | 28.6% |
| Baseband | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 7 | 42.9% |
| DesktopServices | 3 | 3 | 3 | 0 | 0 | 3 | 0 | 11 | 27.3% |
| Disk Images | 3 | 3 | 3 | 0 | 0 | 0 | 3 | 54 | 5.6% |
| NetAuth | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 15 | 20.0% |
| NetworkExtension | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 28 | 10.7% |
| Notes | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 38 | 7.9% |
| Screen Sharing | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| afpfs | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 12 | 25.0% |
| autofs | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 6 | 50.0% |
| libnetcore | 3 | 3 | 3 | 0 | 0 | 3 | 0 | 11 | 27.3% |
| SDK | 2 | 1 | 2 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Sidecar | 2 | 2 | 0 | 0 | 0 | 0 | 2 | 2 | 100.0% |
| Airport | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 4 | 25.0% |
| Compressor | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Passkeys | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 5 | 20.0% |
| Screenshots | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 7 | 14.3% |
| Trusted Device | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| UIKit | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 14 | 7.1% |
| Wallet | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 1 | 100.0% |
| lldb | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| **合计** | 428 | 页列合计 332 | 380 | 4 | 0 | 39 | 28 | 这 50 名 Impact 全集 2860 段（全树 7,434 的 38.5%） | — |

---

# 17 结论

本篇用三刀（无线与配对通道专名、近场与距离限定、网络报文与广播机制）在 9,136 段公开文本上取出主集 **428 段／135 页／50 名**，并把这个集合的措辞结构量到句级和成员级。七条可复核的结论：

**一、通道类是"写后果、不写成因"的模板，字段梯度 30∶1。** Impact 侧命中 **422** 句次、Description 侧 **14** 句次、两侧共用 **0** 句。刀一 124 段、刀二 41 段**一句都不写在 Description 里**，14 个 Description 命中全部落在刀三，且只有三种句子（改用 HTTPS、Beacon Protection、收紧网络限制）。**读者从修复说明里无法还原这条条目属于哪个通道**——通道只在后果句里出现。

**二、"网络位置"不是同义词池，而是三个互斥的排他标签。** `local network` **112** 段、`privileged network position` **93** 段、其余 **117** 段，相加恰为刀三的 **322**（两两同段 **0**）。`nearby`／`AirDrop`／`beacon` 三个 3 段集两两 **0** 相交，`physical proximity` 22 与 `wireless proximity` 8 同段 **0**。**"近场""同网""特权位置"这三类说法在文本层面是彼此独立的标签，不能合并解读为"攻击者离得近"。**

**三、协议专名整片不写，机制靠组件名承载。** 附表 A 的 299 项里全零 **54** 项，包括 `802.11`／`WPA`／`EAP`／`SSID`／`handshake`／`broadcast`／`multicast`／`frame`（裸形）／`mDNS`／`Bonjour`／`Handoff`／`Continuity`／`CarPlay`／`NFC`／`cellular`／`baseband` 全套。`802.1X` 与 `Bonjour／mDNS` 的组件名分别挂 **8** 段与 **74** 段（其中 8 段、27 段进主集），**正文出现次数却是 0**。反过来 `USB`／`VPN`／`relay` 词有 5／6／20 段、组件名代理计数 **0**。**名与词两个维度必须分别测，任何一个都不足以代表另一个。**

**四、词界与连字符口径的污染可以被量化，但不能被假设。** `Wi-Fi` 三种口径同为 **10** 段、"只认 ASCII hyphen"重算全三刀仍是 **428**（连字符异形对本篇零影响）。但去掉左词界得 **468**（＋40）、去掉右词界得 **444**（＋16）；`frame` 无右界扩出 **33** 段却与主集 **0** 相交，`device` 裸词界 207 段只交 **39**，`local` 裸词界 237 段只交 112。**宽松匹配多出来的量不按比例落在目标集里**，所以本篇所有对照桶都单独列值，不并入口径。

**五、句式规模远大于句子多样性。** 76 个 distinct 句子承担 **436** 句次，top1 一句（`An attacker on the local network may be able to cause a denial-of-service`）占 49 句次（11.24%）、top10 占 38.76%、top50 占 90.14%，尾部 **26** 句合占 9.86%。**428 段里有 420 段只含 1 个判据句**；`may` 覆盖 **408** 句次（93.58%），不带 `may` 的 14 句次可以逐句列出（13 句次 `could` ＋ 1 句次 `might`）。**通道类条目没有独立描述，只有模板填充。**

**六、后果层在本篇几乎不分级。** 八类后果三字段相加 **255** 段、并集 **248**、**180** 段（42.1%）一个类都不命中、**7** 段命中 ≥2 类；只看 Impact 则是 **220／220／208／0**——**Impact 侧不存在一段话同时被算作两类后果**。`FULL`（完全控制）与 `PRI`（提权）两个类在主集内**两口径皆为 0**。同时 `arbitrary code execution` 名词形在全树 218 段、与主集 **0** 相交，而 `execute arbitrary code` 动宾形有 10 段进主集。**"这条通道类条目能造成多大影响"在公开文本里基本不表述。**

**七、数字相同不代表集合相同，本篇所有跨集断言都做到成员级。** 三组"8 段"集两两 **0** 交、四组"3∶3／2∶2"集 **0** 交；但也有 `VPN`＝`tunnel*`（6∶6 交 **6**）、`Bluetooth packets`＝`crafted packets`（5∶5 交 **5**）、BYP 后果类∩主集＝#126 bypass 词族∩主集（28∶28 交 **28**）、`remote`∩主集 ⊂ R∩主集（19 段全在内）。与九套姊妹集的关系同样按成员给出：#124 **153** 段（35.75%）、#132 **0** 段、#135 **1** 段。**任何一句"A 就是 B"在本篇都必须带 |A|、|B|、|A∩B| 三个数。**

这些结论只描述**公开文本怎么写**：它说明苹果的安全响应措辞在通道维度上以 Impact 侧位置状语为唯一载体、以模板句为基本单位、以组件名而非协议专名承载机制、且不以措辞区分风险等级。它不说明任何一条被列举条目的真实缺陷类别、可达性、是否被利用，也不构成对 iOS 无线或网络隔离模型安全性的评价。第四套分母已经把版式偏差点出来：**域 A 每页 36.77 段、本篇 427 段＝4.72%，域 B 每页 8.3 段、本篇 1 段＝1.10%**——同一个措辞集合在两种页面模板上的占比相差四倍多，任何按页面均值做的推断都必须先声明用的是哪一套分母。

---

**边界声明：** 本报告统计的是苹果安全响应顾问页面的公开措辞（段落、字段、句子与组件名的出现与搭配），不是漏洞本身；不构成对任何 iOS／macOS 版本安全性的评价，不提供绕过方法、利用代码、恶意样本或针对他人设备的操作步骤与时序。全部计数基于 `_tmp/adv2` 页面镜像（258 份 HTML 排除 1 份聚合页后的 257 页）与 178 份中文支持文档，分母、词界、优先序归桶与八类后果定义见第 1 章与第 16 章。镜像若更新，本报告数字需重测。

Telegram: https://t.me/one00190
