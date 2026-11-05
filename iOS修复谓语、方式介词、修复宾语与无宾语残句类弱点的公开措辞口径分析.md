@@TITLE@@iOS 修复谓语、方式介词、修复宾语与无宾语残句类弱点的公开措辞口径分析
@@SUBTITLE@@选题是"苹果怎么写'修好了'这三个字"这一层：主刀是**修复谓语动词词族** `address*｜resolv*｜fix*｜correct*｜mitigat*｜patch*`，四根对照刀是**方式介词**（`with`／`by`／`through·via`／零介词）、**主语指称**（`This issue`／`The issue`／`A／An X`／其余）、**程度形容词**（`improved`／`additional`／`more`／`new`）与**修复宾语**（程度形容词之后那段名词）。这一层前面十九篇都没量过——#122–#126 量的是**动作与后果**，#131–#140 量的是**载体、通道、宾语与施事者**；至于修复句里那个谓语动词到底是哪一个、它后面挂的是哪个介词、介词后面是什么名词，只在 #132／#139 里当过交叉对照，从没单独切过。逐形测量给出八条反直觉结论。**第一，本篇主集是全系列最大的一根刀，但它的"大"是结构性的**：RFIX ＝ **7,247 段／256 页／394 名**，占全树 9,136 段的 **79.32%**、占含 Description 或含 Impact 那 **7,434** 段的 **97.48%**——剩下没被抓住的只有 **187** 段，其中 **185** 段是同一句开源组件模板。**第二，六个谓语形里只有一个活着**：`addressed` 在 Description 侧 **7,214** 段，`resolv*` **19**、`correct*` **24**、`fix*` **0**、`mitigat*` **0**、`patch*` **0**；而 Impact 侧正好反过来，`address*` 只有 **64** 段、`fix*` 反而有 **13** 段（13 段全部与 #114 在野集 E 相交）。**第三，方式介词四档在 7,214 段里互斥闭合 ＝ `with` 6,588 ＋ `by` 371 ＋ `through／via` 247 ＋ 零介词 8**；`addressed via` 全树 **0** 段（那 247 段全写 `through`），而把 `with` 这一档整个摘掉，主集从 7,247 掉到 **659** 段——一根介词承了 **90.91%** 的地盘。**第四，`improved` 与 `additional` 在同一整段里从不相遇**：`improved` **6,037** 段、`additional` **850** 段、**两词同段 0 段**；而 `more` 的 203 段里有 **185** 段根本不是程度形容词，是开源模板那句 `Learn more about the issue`，真作程度词用的只有 **18** 段。**第五，修复宾语只有 75 种写法**：`checks` 1,036／`memory handling` 862／`bounds checking` 699／`state management` 662／`input validation` 641 五个形态吃掉 3,900 段，前 10 形态吃 5,309 段、前 25 形态吃 6,407 段，只出现 **1** 次的形态仅 **4** 个；另有 **345** 段抽不出宾语，且 345 ＝ `by` 档 297 ＋ `with` 档 22 ＋ 零介词 8 ＋ 不写 `addressed` 的 18 段，四块恰好闭合。**第六，致谢字段唯一的谓语命中来自一个句柄**：致谢侧主刀 **416** 段 **100%** 是 `patch*`，而 416 ＝ **405** 段 `@patch1t` ＋ **11** 段标准免责句，两批**交集为 0**；把 `patch` 从词表里整个摘掉，主集仍是 **7,247** 段（差 **0**）。**第七，这套修复表述里没有任何"将来时"**：`will be addressed`／`has been addressed`／`scheduled to be fixed`／`in a future software update` 在三字段全树口径下**全为 0** 段；复数系词 `were addressed` 只有 **15** 段，且**全部**长在 Description，Impact 侧 **0** 段。**第八，句子层比段落层更空**：7,321 个判据句次只由 **233** 个不同句子写成，第一句 `The issue was addressed with improved memory handling.` 独占 **644** 次（8.80%），出现只有一次的句子仅 **18** 条——整棵树的"修好了"一共只有 233 种说法。
@@INFO@@报告日期|2026 年 11 月 5 日
@@INFO@@分析对象|Apple 安全响应顾问页 257 份英文页面镜像（258 份文件排除页面 `122085`），按 #111 第 1 章的两版式域读取：域 A（`<h3 class="gb-header">` 起块、四锚点收块）**9,045 段 / 470 名 / 246 页**，域 B（粗体段起块）**91 段 / 26 名 / 11 页**，全树 **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 9,132 段含致谢**。本篇主集 RFIX ＝ 修复谓语词族出现在 **Impact 或 Description** ＝ **7,247 段 / 256 页 / 394 名**，其中 Impact 侧 **83** 段、Description 侧 **7,233** 段；字段组合 **仅I 14／仅D 7,164／I∩D 69**（相加 7,247）；致谢字段命中 **416** 段（其中 402 段已在主集内），三字段并集 ＝ **7,261** 段。四个字段分母：含 Description 的 **7,434** 段、含 Impact 的 **7,434** 段（两者是**同一批段**，成员交 7,434）、含致谢的 **9,132** 段、全树 **9,136** 段。四把对照刀：**方式介词四档**（`with` 6,588／`by` 371／`through·via` 247／零介词 8，在含 `addressed` 的 7,214 段内互斥闭合）、**主语指称四档**（`This issue` 1,049／`The issue` 1,561／`A／An…` 4,608／其余 15，在 Description 侧 7,233 段内闭合）、**程度形容词五档**（`improved` 6,037／`additional` 850／`more` 203／`new` 5／六形并集 7,095）、**修复宾语抽取**（75 个形态、6,888 段、抽不到的 345 段）。对照集：**R** ＝ #118 远程触达词族 **843 段**；**E** ＝ #114 在野集 **108 段**；**WO** ＝ #119 无前提集 **178 段**；**G／HI** ＝ 动名词开头集 **2,179 段** 与交互触发集 **273 段**；**M2** ＝ 同段 ≥2 后果类集 **185 段**；**UU** ＝ Impact 含 `user` **1,627 段**；**RC** ＝ #122 竞态集 **232 段**；**L** ＝ #123 逻辑集 **550 段**；**Ds** ＝ #124 拒绝服务集 **470 段**；**X** ＝ #125 泄露集 **427 段**；**BP** ＝ #126 绕过集 **301 段**；另取《Apple 平台安全保护》zh-CN 全量转储 179 页（窗口切分后 **178 页可用**）做中文层对照
@@INFO@@视角定位|防御与分析视角，不含绕过方法、不含利用代码、不含恶意样本或利用链构造方式、不含针对他人设备的操作步骤与时序；本篇只清点**"修复谓语、方式介词、程度形容词、修复宾语与残句"这五个文本部件在公开英文文本的哪个字段、哪一句位、与哪一个词搭配，以及它与姊妹篇集合之间的包含关系**，属于措辞与文本计量分析；文中任何"内存处理得到改进""移除了易受攻击的代码""输入校验问题已被处理"表述均指**公开条目使用了该词或该短语这一文本事实**，不是对该条目真实缺陷类别、CWE 归属、修复是否有效、修复代码实际改了什么、涉及数据种类或任何 iOS／macOS 版本与机制安全性的判断或暗示，也不构成对 iOS 代码签名、Gatekeeper、沙盒或锁定模式机制安全性的结论。本篇尤其不涉及补丁内容逆向、利用程序构造方式、伪造输入的制作方法，也不提供任何针对他人设备的操作步骤或时序
@@INFO@@分类|判据**六组 109 项 / 并集全零 3 项 / 非零 106 项**（附表 A，六组：本体词 41、句级刀 17、机制名与检测词对照 16、修复套话句 15、外部集对照 12、后果强度八类 8；三个全零项是 `mitigat*` 三字段、`patch*` 两字段、`Kernel` 组件名在 `MK()` 不扫名列下的口径零）**/ 二百三十三个含谓语整句 × 归桶 × 字段 × 外部集**（附表 B，234 行含合计，句次合计 **7,321 ＝ Impact 88 ＋ Description 7,233**，页列合计 4,367）**/ 十七个宾语类别桶 × 句次 × 段页名 × I∶D，续表七档句式形态**（附表 C，18 行含合计，桶句次闭合 **7,321**、未归类 **0**、distinct 句多重命中 **209** 条；续表 **The／This 打头 2,619／A／An X issue 1,985／A／An X 非 issue 2,611／Multiple 15／含 was addressed with 9／含 may 53／其余 29** 闭合 7,321、形态多重 **223** 条）/ **页面、编号、致谢、版式与字段落位五类二十九行分箱**（附表 D，30 行，页九档闭合 256、编号五档闭合 7,247、致谢七档闭合 7,243）**/ 中文层 63 词**（附表 E，八组：修复谓语类 10、程度形容词类 9、方式介词类 4、校验与检查类 8、处理与管理类 7、限制与控制类 8、动作宾语类 9、声明与来源类 8，合计 63；**全零 15 词、非零 48 词**）/ **组件名层 394 名**（附表 F，395 行含合计，段合计 7,247、这 394 名的 Impact 全集 **7,382** 段、占全树含 Impact 段的 **99.3%**）/ **十九把刀**：字段刀（Impact **83** ∶ Description **7,233** ∶ 致谢 **416**，两字段拆分 仅I 14／仅D 7,164／I∩D 69）、谓语六形刀（`address*` 7,214∶64、`resolv*` 19∶1、`fix*` **0**∶13、`correct*` 24∶5、`mitigat*` **0**∶**0**、`patch*` **0**∶**0**）、介词刀（`with` 6,588／`by` 371／`through·via` 247／零 8，闭合 7,214）、主语刀（`This` 1,049／`The` 1,561／`A／An` 4,608／其余 15，闭合 7,233）、矩阵刀（主语四档 × 介词四档 16 格合计 **7,214**，`through` 档 247 段全落在 `This` 与 `A／An` 两行）、程度刀（`improved` 6,037／`additional` 850／同段 **0**／`more` 203（主集内仅 **18**）／`new` 5／六形并集 7,095）、宾语抽取刀（**75** 形态／**6,888** 段／一形态一段／抽不到 **345** 段）、动名词刀（`by` 后 **16** 形态 **371** 次，`removing` 215、`not` **7**）、十七桶刀（优先序闭合 **7,321**、未归类 0、多重 209）、打头形态刀（七档闭合 7,321、多重 223）、残句刀（Impact 以 `addressed` 裸收尾 **21** 段／Description 以 `addressed` 加标点收尾 **7** 段／`addressed to` 目的状语 **1** 段／Description 不写 `addressed` **19** 段）、残差刀（**187** 段 ＝ 开源模板 **185** ＋ `Libnotify` **2**）、摘刀（摘 `with` **659**、摘 `by` **6,876**、摘 `through` **7,000**、摘零介词 **7,239**、摘 `addressed`(des) **33**、摘 `resolv*` **7,228**、摘 `correct*` **7,223**、摘 `fix` 原形 **7,234**、摘 `address` 原形 **7,208**、摘纯致谢 `patch` **7,247**）、词界刀（去左右词界 **7,257 ＝ ＋10**，10 段全被 `incorrect`(8)／`incorrectly`(2) 命中）、致谢刀（**416 ＝ 405 ＋ 11** 两批不交、致谢独有 **14** 段）、句库刀（7,321 句次只由 **233** 句写成，top1 **8.80%**、出现 ≥2 次 **215** 条、只 1 次 **18** 条）、标点刀（判据句 **7,321 ＝ 句号 7,218 ＋ 无末尾标点 103**；`This issue was addressed with improved checks` 142 句次 ＝ 带句号 **131** ＋ 不带 **11**）、外部集刀（`WO`／`E`／`RC`／`L` 四集 **100.00%** 被主集包含，`R` **97.27%**、`BP` **99.67%**、`UU` **99.82%**、`HI` **99.27%**、`X` **97.89%**、`Ds` **92.77%**、`G` **95.00%**、`M2` **89.73%**；十二集并集 5,277 段吃掉主集 **5,137** 段，主集未被覆盖 **2,110** 段＝**29.12%**）、组件名与中文刀（**394** 名扛 7,247 段，Top5 **2,122** 段＝29.28%、Top10 **2,680** 段＝36.98%、**376** 名 100.0% 命中；中文层 63 词里「修复」仅 **14** 次／**8** 页、「补丁」**4** 次／**1** 页、15 词全零）
@@INFO@@难度|进阶（本篇的 7,247 段有**四套分母**：占全树 9,136 段是 **79.32%**、占含 Impact 的 7,434 段是 **97.48%**、占其所在 256 页全部 9,135 段是 **79.33%**（这 256 页每页平均 35.68 段，本篇每页平均 **28.31** 段），三个数都真、不可混用；第四套是**版式域**分母——域 A **7,159／9,045 ＝ 79.15%**、域 B **88／91 ＝ 96.70%**，**域 B 占比明显高于域 A**，而域 B 只有 11 页、每页 8.3 段，这个偏差首先是页面模板造成的，**只能当分母告警读**。更要紧的是本篇与姊妹篇的方向相反：#140 那根刀"只长在 Impact"，本篇这根刀**几乎只长在 Description**（83∶7,233），所以"占含 Impact 段 97.48%"这个数**不是**"苹果对每个漏洞都写了修复句"，而是**"凡有 Description 的段，97.48% 都含一个修复谓语"**——它的补集只有 187 段，见第 12 章。**7,247 是两字段主集、7,261 是三字段并集、7,321 是句次、6,888 是宾语轴段次、7,214 是介词轴段次**（六者互不相等，各自闭合于各自的分区）。7,247 段里 **7,178 段只含 1 个判据句、64 段含 2 句、5 段含 3 句**，所以句次比段数多 74；**附表 B、C、F 的页列与名列是逐行去重后的再求和，不得当作并集去重值**；附表 C 的段列在跨桶处可重叠（17 桶段列相加 ＝ 7,321 而主集只有 7,247），页列合计 1,682、名列合计 918 都不能与 256／394 互换；附表 A「后果强度八类」∩主集列是**整段扫描（两字段＋致谢三字段并集口径 `MK()`）**（八类相加 **4,175**），第 15 章另给后果词只落在 **Description 字段**的逐类数（相加 **462**、Description 侧同段 ≥2 类 **0** 段，而 Impact 侧 ≥2 类有 **185** 段、主集内 **166**，即外部集 `M2`）；两套口径都不是主集段数 7,247；附表 A 的句级刀 ∩主集列是**段级命中**、附表 C 的桶句次相加 **7,321** 是**句次轴**，两者不可互换（例：界面与绘制桶 A 表 411 段 ∶ C 表 4 句次））
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@

# 0 本篇要回答什么

前面十九篇措辞层报告量的是**缺陷、载体与施事者**：#122 竞态、#123 逻辑、#124 拒绝服务、#125 信息泄露、#126 绕过、#132 图像格式、#133 资源边界、#135 音视频、#136 认证凭据、#137 无线通道、#138 用户数据、#139 限制与特权、#140 威胁主体称谓。这些刀切的全是**"哪一类弱点"**或**"谁在做什么"**。

**但没有一篇切过"怎么收句"。**

苹果在每一条安全条目里都要写一句"这个问题是怎么修的"。这一句话是整篇条目里唯一**结构固定**的部分——它有一个谓语动词、一个可选的介词、一个名词宾语，其余全是模板槽位。于是四个此前没人量过的问题：

1. **谓语动词到底是哪一个？** `addressed`、`resolved`、`fixed`、`corrected`、`mitigated`、`patched` 这六个形在 9,136 段里各出现多少？它们是不是长在同一个字段里？
2. **谓语后面的介词怎么选？** `addressed with`／`addressed by`／`addressed through`／`addressed via`／不写介词，这五档各多少段，能不能闭合？
3. **介词后面的宾语有多少种？** `improved checks`／`improved memory handling`／`additional restrictions` 这个名词段一共几个形态、头部集中度多高、有没有抽不出宾语的残句？
4. **主语指称与程度形容词怎么选？** `This issue`／`The issue`／`An X issue`／`Multiple issues were…`；`improved`／`additional`／`more`／`new` 这四组词是不是互斥？

主集 RFIX ＝ 修复谓语词族出现在 **Impact 或 Description** ＝ **7,247 段 / 256 页 / 394 名**，占全树 9,136 段的 **79.32%**、占含 Impact 的 7,434 段的 **97.48%**。这根刀的性质和前面任何一篇都不同：**它切的不是"内容"，是"句式"**，所以它必然极大——真正的问题不是"为什么这么大"，而是**"这么大的刀，剩下的 187 段是什么"**（第 12 章），以及**"它大的那部分到底长在哪个字段"**（第 3 章）。

# 1 口径、判据与四套分母

## 1.1 语料与段切分

沿用 #111 第 1 章、#112 第 1 章建立的两版式域读取口径，不做任何改动：

- 本地镜像 `_tmp/adv2` 共 **258 份 HTML**，排除聚合页 `122085` 后 **257 页**可读。
- **域 A**：以 `<h3 class="gb-header">` 起块、以四个结束锚点（`Impact`／`Description`／`Resolution` 段界与页级免责声明）收块，得到 **9,045 段 / 470 个组件名 / 246 页**。四个结束锚点少任一个，页级免责声明就会被卷进字段文本——这条纪律在本篇是**硬前提**，因为本篇要数"句子里的谓语"，一旦免责声明混入，`doesn't disclose` 一类词就会污染致谢侧（第 13 章实测那 11 段免责句确实长在致谢字段）。
- **域 B**：11 页粗体段版式（不含 `120306`，理由见 #133 记忆条目），**91 段 / 26 名 / 11 页**。
- **全树** ＝ 域 A ＋ 域 B ＝ **9,136 段 / 257 页 / 472 名 / 7,434 段含 Impact / 9,132 段含致谢**。

## 1.2 主刀：修复谓语动词词族

本篇的主刀是一根**词族刀**，不是单词刀。它的构造分两步，第一步是把六个候选谓语动词收成一条并列式 `$ALT_V = address\w*｜resolv\w*｜fix\w*｜correct\w*｜mitigat\w*｜patch\w*`（这里的 `\w*` 让 `addressed`、`correcting`、`mitigation` 一类变形都落在同一个词干里）；第二步是给这条并列式套上左右两侧的词界，得到主刀 `$MAIN = (?<![\w-])(?:$ALT_V)(?![\w-])`。

词界之所以把连字符也算进"不属于词界"的字符，是为了不让 `address` 从 `self-addressed`、`unaddressed` 这类带连字符的嵌套里被单独抠出来——代价是连字符右侧的真词也会被一起挡掉（#132 就实测过这个方向的损失：同一形制的左侧 lookbehind 让 `out-of-bounds` 少计 193 段）。本篇接受这个代价，因为谓语动词后面接连字符复合词的情况在本语料里几乎不存在；第 12.3 节会拿一根去掉词界的宽松刀复核，实测差额只有 10 段且全部来自 `incorrect` 这一个形。

`%CI` ＝ Impact 侧命中 **83** 段、`%CD` ＝ Description 侧命中 **7,233** 段、`%CU` ＝ 两者并集 **7,247** 段、`%CA` ＝ 致谢侧命中 **416** 段。四个集合都从同一个 `$MAIN` 生成，禁止在正文里重抄正则——本篇所有闭合断言都复用这些集合本身。

**为什么把这六个形并成一把刀**：因为在文本上它们是**同一个槽位的竞争者**。第 2 章逐形拆开会看到 `addressed` 一家占了 Description 侧的 **99.74%**，其余五形加起来 44 段——并成一把刀的唯一理由，是为了让"补集只有 187 段"这个结论成立（第 12 章），也是为了让"摘刀反事实"能量化每一形的承重（第 19 章）。

## 1.3 四把对照刀

| 刀 | 定义 | 域 | 闭合 |
|---|---|---|---|
| 方式介词刀 | `addressed` 之后第一个介词，优先序 `with`→`by`→`through／via`→零介词 | Description 侧含 `addressed` 的 **7,214** 段 | 6,588＋371＋247＋8 ＝ **7,214** |
| 主语指称刀 | Description **首段**打头，优先序 `This issue`→`The issue`→`A／An…`→其余 | `%CD` **7,233** 段 | 1,049＋1,561＋4,608＋15 ＝ **7,233** |
| 程度形容词刀 | Description 侧词界匹配 `improved`／`additional`／`more`／`new`／六形并集 | 全树 Description | 不声称闭合（第 6 章给出交并） |
| 修复宾语刀 | Description 侧抽取 `(improved\|additional\|better\|more\|stricter)` 之后到下一个标点前的名词段 | `%CD` 7,233 段 | 6,888 段抽到 ＋ 345 段抽不到 ＝ **7,233** |

第三行必须说清楚：程度形容词**不构成**分区。同一段可以既写 `improved` 又写 `more`（实测 `improved` 与 `additional` 同段 **0** 段，但 `improved` 与 `more` 同段 18 段），所以第 6 章只报交并，不报闭合。

## 1.4 四套分母与"97.48%"的正确读法

7,247 段这个数，除以四个分母得到四个都真的数：

| 分母 | 值 | 占比 | 这套分母在说什么 |
|---|---:|---:|---|
| 全树段 | 9,136 | **79.32%** | 每 5 段里约 4 段有一句"修好了" |
| 含 Impact 的段 | 7,434 | **97.48%** | 见下方警告 |
| 本篇 256 页的全部段 | 9,135 | **79.33%** | 第三套：同页共存密度 |
| 域 A 段 | 9,045 | **79.15%** | 版式域分母（域 B 另算 **88/91 ＝ 96.70%**） |

**警告**：含 Impact 的段数与含 Description 的段数**都是 7,434**，而且实测**成员交也是 7,434**——这两个字段是同进同出的（一条条目要么两个字段都有，要么都没有）。所以"97.48%"这句话**不能**写成"97.48% 的有 Impact 条目的 Description 里写了修复句"以外的任何因果解释；它就是一个纯文本覆盖率，且它的分母与 Description 分母是同一个集合。每页平均：本篇 7,247／256 页 ＝ **28.31** 段／页，所在 256 页全部 9,135／256 ＝ **35.68** 段／页。

## 1.5 句级判据

`SE()` 按 `(?<=[.!?])\s+(?=["A-Z])` 分句、`D0()` 把 NBSP／窄空格归一为普通空格并压叠空白。判据句 ＝ 分句后含 `$MAIN` 的句子，全树共 **7,321** 句次，其中 **88** 句在 Impact、**7,233** 句在 Description。为什么 Description 侧句次正好等于 Description 侧段数（7,233 ＝ 7,233）：实测 Description 侧 7,233 段里，`addressed` 一词**每段最多出现 1 次**（0 次 19 段、1 次 7,214 段、2 次及以上 **0** 段），见第 11 章。

# 2 谓语六形：一枝独秀与两个字段反向

把 `$MAIN` 的六个候选形逐词界拆开，分别扫 Description 与 Impact：

| 谓语形 | Description 段 | Impact 段 | 备注 |
|---|---:|---:|---|
| `address*`（词族） | **7,214** | **64** | Description 侧唯一主力 |
| `addressed`（单词） | 7,214 | **25** | Impact 侧 64 段里只有 25 段用过去分词 |
| `address`（原形） | 0 | **39** | 39 段里 **33** 段与远程集 R 相交 |
| `resolv*` | **19** | 1 | 19 段全都不写 `addressed`（第 11 章） |
| `correct*` | **24** | 5 | 与 `addressed` 同段共存，非替换关系 |
| `fix*` | **0** | **13** | 13 段全部与在野集 E 相交（13∶13） |
| `mitigat*` | **0** | **0** | 三字段全树零命中，附表 A 全零项 |
| `patch*` | **0** | **0** | 两字段全零；但致谢字段 **416** 段（第 13 章） |

四条结论：

**2.1 `addressed` 不是首选词，它是唯一词。** Description 侧 7,233 段里 7,214 段含 `addressed`，占 **99.74%**；剩下 **19** 段是 `resolved` 异形（`The issue was resolved by sanitizing logging.`），1 段都没换成 `fixed`／`mitigated`／`patched`。所以"苹果用不用别的说法"这个问句，答案是**结构性的不用**，而不是"偶尔用"。

**2.2 两个字段的方向完全相反。** `fix*` 在 Description 侧 **0** 段、在 Impact 侧 **13** 段；`address` 原形在 Description 侧 **0** 段、在 Impact 侧 **39** 段；反过来 `resolv*` 与 `correct*` 偏 Description（19∶1、24∶5）。这与 #136 的"三刀字段不对称"、#140 的"零 Description"是同一类现象，但方向更干净：**过去分词归 Description，动词原形归 Impact**。原因是句法位置：Impact 写的是"这个缺陷会造成什么"，偶尔用一般现在时的 `This address... ` 形；Description 写的是"做了什么"，用被动过去分词。

**2.3 `mitigat*` 与 `patch*` 在两字段都是零。** 这两根词族在本语料的英文条目字段文本里**不存在**（不是"少"，是 0 段）。这条与致谢侧的 416 段并读才有效：`patch*` 只在致谢字段的**人名句柄**里活着（`@patch1t` 405 段），见第 13 章。

**2.4 `fix*` 的 13 段不是在野声明本身，而是补发说明。** 这 13 段与 #114 在野集 E 的成员交是 **13**（E 全集 108 段），也就是说：全树 108 段在野条目里，有 13 段的 Impact 写了 `fix`——即 #140 第 2 章已经量过的那批 `This fix associated with the Coruna exploit was shipped in iOS …` 补发行文。修复表述层的"将来时／过去时"问题在文本上是空的，第 10 章给出四个零时态句的实测。

# 3 字段落位：83∶7,233、14 段错位与三字段并集

## 3.1 三段拆分

`%CI` **83**、`%CD` **7,233**、`%CU` **7,247**，拆成互斥三档：

| 档 | 段 | 占主集 |
|---|---:|---:|
| 仅 Impact 侧（仅I） | **14** | 0.19% |
| 仅 Description 侧（仅D） | **7,164** | 98.83% |
| 两侧都含（I∩D） | **69** | 0.95% |

14 ＋ 7,164 ＋ 69 ＝ **7,247** ✓（与 `83 ＋ 7,233 − 69` 一致）。

## 3.2 那 14 段"仅 Impact"的段是什么

这 14 段落在 **9 个页面**：`122371`(2)、`122372`(1)、`122373`(2)、`122374`(1)、`122375`(1)、`122376`(2)、`122377`(2)、`122378`(2)、`125634`(1)。它们的 Impact 是一句裸的修复句（如 `An input validation issue was addressed`），而 Description **不是修复句**——实测这 14 段的 Description 字段长度全是 **187** 字符，内容是同一句开源组件模板：

> `This is a vulnerability in open source code and Apple Software is among the affected projects. The CVE-ID was assigned by a third party. Learn more about the issue and CVE-ID at cve.org .`

把这 14 段与"含 `open source` 且在主集内"的段做成员交：**双向都是 14**——也就是说，**"仅 Impact 侧"这一档与"开源模板段里那 14 个进了主集的段"是同一批段**，不是巧合。这是一次字段错位：修复表述被写进了 Impact 槽，Description 槽被模板占用。

## 3.3 三字段并集只多 14 段

`%CU`（两字段）**7,247** → `MK()` 三字段并集 **7,261**，增量 **14** 段。这 14 段的定义是"致谢字段含谓语、但两字段都不含"，样本是页 `121238`、`124149`、`125110` 的 `We would like to acknowledge Mickey Jin (@patch1t) for their assistance.`——谓语命中来自句柄 `@patch1t` 里的 `patch`，与第 13 章同源。**撤掉致谢字段，本篇结论一个字都不用改**（这是 #140 第 6 章"致谢字段零增量"现象在本篇的弱版本：不是 0，是 14，且 14 段仍与开源模板错位段毫无关系）。

## 3.4 与"含 Impact"这个分母的实测关系

含 Impact 的段 **7,434**、含 Description 的段 **7,434**、两者成员交 **7,434**；主集 7,247 段里，含 Impact 的 **83** 段、含 Description 的 **7,233** 段——`83/7,434 ＝ 1.12%`、`7,233/7,434 ＝ 97.30%`。所以"修复表述"这一层在字段选择上的答案是**压倒性的 Description**（比例 **1∶87.1**）。第 11 章会给出这个 1.12% 里到底长什么样（21 段以 `addressed` 裸结尾）。

# 4 方式介词四档：一根介词承 90.91%

## 4.1 闭合表

在 Description 侧含 `addressed` 的 **7,214** 段内，按优先序 `with` → `by` → `through／via` → 零介词互斥分档：

| 档 | 段 | 占 7,214 | 典型句 |
|---|---:|---:|---|
| `addressed with` | **6,588** | 91.32% | `The issue was addressed with improved checks.` |
| `addressed by` | **371** | 5.14% | `This issue was addressed by removing the vulnerable code.` |
| `addressed through／via` | **247** | 3.42% | `This issue was addressed through improved state management.` |
| 零介词 | **8** | 0.11% | `An input validation issue was addressed.` |
| 合计 | **7,214** | 100.00% | 与 `des 含 addressed` 段数逐格相等 ✓ |

**4.2 `via` 是零。** 第三档定义写的是 `through|via`，实测该档 247 段里 `addressed via` **0** 段——也就是**整个 247 段全用 `through`**。这一档不是"through 与 via 的并集"，而是"through 独占档"，正文不得写成"through／via 两形并用"。

**4.3 这 247 段全带 `improved`。** 关键套语 `addressed through improved` 实测 **247** 段，与该档段数**完全相同**；程度刀 `improved` ∩ `through` 档也是 **247**。所以 `through` 是 `improved + 名词` 结构的第三个可选介词（`with improved X`／`through improved X`），不承担任何别的宾语类型。

**4.4 摘刀反事实。** 把整档从主集里删掉，看主集还剩多少段：摘 `with` 档剩 **659**、摘 `by` 档剩 **6,876**、摘 `through` 档剩 **7,000**、摘零介词档剩 **7,239**、四档全摘剩 **33**（33 段构成见第 11 章）。**一根 `with` 承 90.91% 的主集**——本篇这根刀的名义承重其实全在这一格上，这也是"79.32% 大而无当"这个质疑的正确回答方式：刀的大小不是由谓语决定的，是由 `with` 这个介词决定的。

**4.5 介词档与"宾语抽取"是反相关的。** 修复宾语刀只在"程度形容词之后"抽取，所以 `by` 档天然抽不到宾语：345 段抽不到宾语的构成是 **by 297 ＋ with 22 ＋ 零介词 8 ＋ 不写 addressed 18 ＝ 345**，逐格相加恰好闭合（`through` 档 **0** 段抽不到，因为该档 247 段全带 `improved`）。

# 5 主语指称四档与 4×4 矩阵

## 5.1 四档闭合

在 `%CD` **7,233** 段内，按 Description 首段打头互斥分档：

| 档 | 段 | 占 7,233 | 说明 |
|---|---:|---:|---|
| `This issue was addressed…` | **1,049** | 14.50% | 指代前一句的后果 |
| `The issue was addressed…` | **1,561** | 21.58% | 与上一档成员交 **0**（互斥验证） |
| `A／An X issue… was addressed…` | **4,608** | 63.71% | 主语自带缺陷名 |
| 其余 | **15** | 0.21% | 全部是 `Multiple … issues were addressed…` |

1,049 ＋ 1,561 ＋ 4,608 ＋ 15 ＝ **7,233** ✓。四档是**优先序**分档，`SB1 ∩ SB2` 实测 **0**，所以这一刀同时通过了互斥与闭合两道检查。

## 5.2 复数系词只有 15 段，且全在 Description

`were addressed`（Description 侧）**15** 段、`Multiple` 打头（Description 侧）**15** 段、"其余"档 **15** 段——**三个 15 是同一批段**（两两成员交都是 15）。这 15 段的样本是 `Multiple memory corruption issues were addressed with improved memory handling.`、`Multiple issues were addressed by updating to curl version 8.4.0.`。

再加一刀：**`were addressed` 在 Impact 侧 0 段**。所以"复数主语 ＋ 复数系词"这个句式是 Description 专属的，Impact 侧从不把多个缺陷合成一句修复表述。

## 5.3 主语四档 × 介词四档

| 主语档 | with | by | through | 零介词 | 档合计 |
|---|---:|---:|---:|---:|---:|
| `This issue` | 649 | 163 | **237** | 0 | 1,049 |
| `The issue` | **1,502** | 41 | **0** | 0 | 1,561 |
| `A／An…` | 4,429 | 160 | 10 | **8** | 4,608 |
| 其余 | 8 | 7 | 0 | 0 | 15 |
| 列合计 | **6,588** | **371** | **247** | **8** | **7,214** ✓ |

三条可读的规律：**① `The issue` 从不用 `through`**（0 段），`through` 全部长在 `This issue`（237）与 `A／An`（10）两行；**② 零介词 8 段全部长在 `A／An` 一行**，也就是"主语自带缺陷名 ＋ 谓语裸收尾"是唯一存在的省略形；**③ `by` 档在四行里都有**（163／41／160／7），是唯一不与主语指称绑定的介词档。

## 5.4 `A／An` 档里的 15 段"Multiple"

第四档 15 段的介词档分布是 **with 8 ＋ by 7 ＝ 15** ✓（`through` 与零介词都是 0）。所以"复数句式"里 `with`（`…were addressed with improved memory handling`）与 `by`（`…were addressed by updating to curl 8.4.0`）几乎对半分，这是本篇唯一一处**句式与修复手段类型绑定**的地方：`by updating to X` 这类"整包升版本"的写法只能出现在复数段里。

# 6 程度形容词五档：improved 与 additional 从不同段

## 6.1 五档数与主集内数

| 词（Description 侧词界匹配） | 全树段 | ∩主集 | 页 | 名 | ∩`with` | ∩`by` | ∩`through` |
|---|---:|---:|---:|---:|---:|---:|---:|
| `improved` | **6,037** | **6,037** | 255 | 344 | 5,786 | 3 | **247** |
| `additional` | **850** | 850 | 150 | 125 | 783 | 67 | **0** |
| `more` | **203** | **18** | 110 | 26 | 0 | 4 | 0 |
| `new` | **5** | 5 | 5 | 1 | 5 | 0 | 0 |
| 六形并集（含 `better`／`stricter`） | **7,095** | 6,910 | 256 | 382 | 6,574 | 74 | 247 |

## 6.2 `improved` 与 `additional` 在同一整段里从不相遇

`improved` 段集 ∩ `additional` 段集 ＝ **0 段**。两词各 6,037／850 段、相加 6,887，正好等于六形并集 7,095 减去 `more` 的重复部分——但**交集为 0 是独立实测的，不是推出来的**。这意味着"改进型修复"与"追加型修复"在文本上是两套互不相交的模板，作者写完 `improved` 就绝不会再在同一段里写 `additional`。`improved` ∩ `new` 也是 **0**。

## 6.3 `more` 不是程度形容词，是导航用语

`more` 在 Description 侧 203 段，但只有 **18** 段在主集内。剩下 **185** 段实测 **185 段全部**含 `open source`、**185 段全部**含 `Learn more`，且与第 12 章的残差集（187 段里的开源模板 185 段）**双向成员交都是 185**——**同一批段**。`more` 后面接的形态 TOP1 是 `about the issue and c…`（185 次），即 `Learn more about the issue and CVE-ID at cve.org`。所以 `more` 这个词的 88% 出现次数**与修复表述无关**，任何把 `more` 当程度形容词并进的刀都会虚增 185 段。主集内那 18 段则 4 段落在 `by` 档（`improved … more …` 形），其余 14 段就是第 3.2 节那批"仅 Impact"错位段。

## 6.4 `new` 的 5 段只有一种宾语

5 段全部是 `This issue was addressed with a new entitlement.`，全部落在 `with` 档、全部只有 **1** 个组件名。`new` 作为程度形容词在本语料里等价于一个单一句式的别名，不具统计意义。

## 6.5 六形并集 ∩主集 6,910 与主集 7,247 的差

差 **337** 段。这 337 段是"有谓语但没有程度形容词"的段——主要就是 `by` 档（`addressed by removing the vulnerable code` 一类，动名词不是程度形容词）与第 7 章的 345 段无宾语群的一部分。这说明**程度形容词刀不能当主刀用**：它有 4.65% 的系统性漏检。

# 7 修复宾语抽取：75 种形态吃掉 6,888 段，剩 345 段抽不出宾语

## 7.1 抽取口径

宾语抽取只在 Description 侧、只在含 `addressed` 的段里做，规则是"取程度形容词之后到句末标点之前的连续名词短语"。结果是 **75 种形态、覆盖 6,888 段**。三条自检：

| 判据 | 数 |
|---|---:|
| 宾语形态种数 | **75** |
| 被至少一种形态覆盖的段 | **6,888** |
| Description 主集段 | **7,233** |
| 抽不到宾语的段 | **345**（7,233 − 6,888）✓ |
| 同段命中 2 种形态的段 | **0** |
| 同段命中 3 种及以上的段 | **0** |

最后一行是关键：**每段最多只有一种宾语形态**，所以 75 个形态的段数相加恰好等于 6,888，不存在"一段被两个形态重复计数"的问题。这也是本篇把宾语层当作**互斥分箱**而不是可重叠标签的依据。

## 7.2 345 段抽不出宾语的四块闭合

| 块 | 段 | 为什么抽不出 |
|---|---:|---|
| `addressed by ＋ 动名词` | **297** | 抽取刀只认"形容词＋名词"，`by removing …` 里是动词 |
| `addressed with ＋ 非二元形` | **22** | `with improvements to X`／`with a new entitlement`／`with Beacon Protection`／`with improved truncation when …` |
| 零介词裸收尾 | **8** | `An input validation issue was addressed.` 根本没有宾语位置 |
| 段内无 `addressed` | **18** | 第 2.3 节的 18 段"有谓语无 addressed"（含 2 段 Libnotify） |
| `addressed through …` | **0** | 247 段全部能抽出宾语 |
| 合计 | **345** ✓ | |

第 2 块的 22 段值得单独说一句：`improvements`（名词）和 `improved`（形容词）是同一个词的两种词性，但**程度形容词刀用的是 `improved` 词界形，抓不到 `improvements`**。所以 `A timing side-channel issue was addressed with improvements to constant-time computation in cryptographic functions.` 这类句子在宾语层是漏的。这 22 段全都还在主集里（靠谓语动词 `addressed` 进来），不影响主集，但影响宾语形态表的完整性。

## 7.3 形态表头部

| 排名 | 宾语形态 | 段 |
|---:|---|---:|
| 1 | `checks` | **1,036** |
| 2 | `memory handling` | **862** |
| 3 | `bounds checking` | **699** |
| 4 | `state management` | **662** |
| 5 | `input validation` | **641** |
| 6 | `restrictions` | 428 |
| 7 | `validation` | 389 |
| 8 | `memory management` | 369 |
| 9 | `private data redaction for log entries` | 114 |
| 10 | `state handling` | 109 |
| — | 前 10 合计 | **5,309**（占 6,888 的 77.08%） |
| 11–25 | `data redaction` 104／`entitlement checks` 102／`logic` 94／`path validation` 94／`redaction of sensitive information` 92／`bounds checks` 89／`sandbox restrictions` 85／`input sanitization` 72／`validation of symlinks` 66／`locking` 60／`access restrictions` 58／`code-signing restrictions` 51／`handling of symlinks` 50／`data protection` 43／`file handling` 38 | 1,098 |
| — | 前 25 合计 | **6,407**（占 6,888 的 93.02%） |

**25 种形态覆盖 93%，剩下 50 种形态只分 481 段。** 这就是"修复表述高度模板化"的量化形式。

## 7.4 长尾只有 4 个单例

| 判据 | 形态种数 | 段数 |
|---|---:|---:|
| 只出现 1 次 | **4** | 4 |
| 出现 ≤5 次 | **23** | 70 |
| 出现 ≥2 次 | **71** | 6,884 |
| 出现 ≥50 次 | 23 | 6,326 |
| 出现 ≥100 次 | **12** | **5,515** |
| 全部 | 75 | 6,888 |

（≥50 与 ≤5 都恰好是 23 种，是**两个不同的 23**：前者 6,326 段、后者 70 段，成员交 **0**。）

4 个单例形态及原文（逐条实测）：

- `app bundle integrity checks` — `The issue was addressed with additional app bundle integrity checks.`
- `message validation` — `This issue was addressed with improved message validation.`
- `network restrictions` — `An authorization issue was addressed with improved network restrictions.`
- `validation of file attributes` — `This issue was addressed with improved validation of file attributes.`

**只有 4 种形态在整棵语料里出现过一次**，其余 71 种至少 2 次。换句话说，Apple 的修复宾语表不是"长尾极长"，而是**头部极陡**：23 种低频形态合计才 70 段（占 6,888 的 **1.02%**），而 12 种高频形态独占 5,515 段（**80.07%**）。

## 7.5 两个"同数不同集"案例

本篇在宾语层撞上两处必须用成员交集区分的读数：

| 刀 A | 段 | 刀 B | 段 | 成员交 | 判定 |
|---|---:|---|---:|---:|---|
| `bounds checking` | 699 | `improved bounds checking` | **699** | **699** | **同一批段**（所有 bounds checking 前面都带 improved） |
| `checks`（宾语形态） | 1,036 | `improved checks`（短语） | **1,018** | **1,002** | **不同集**：18 段 `improved checks` 不是宾语位，34 段宾语 `checks` 前面不是 improved |

第一行是"完全重合"，写 `A＝B` 合法；第二行是"1,036 与 1,018 差 18，但重合只有 1,002"，如果直接写 `improved checks ⊂ checks` 就错了——有 **16** 段 `improved checks` 落在宾语 `checks` 形态之外。这是系列反复强调的口径纪律：**得数相近不代表包含关系**。

## 7.6 宾语词族（宽松词根，Description 侧）

| 词根 | 段 | ∩主集 | 差 |
|---|---:|---:|---:|
| `check*` | **1,990** | 1,990 | 0 |
| `handl*` | **1,454** | 1,454 | 0 |
| `validat*` | **1,319** | 1,319 | 0 |
| `manag*` | **1,038** | 1,038 | 0 |
| `restrict*` | **686** | **684** | **2** |
| `logic` | 540 | 540 | 0 |
| `redact*` | 310 | 310 | 0 |
| `lock*` | 109 | 109 | 0 |
| `sanitiz*` | 93 | 93 | 0 |
| `protect*` | 50 | 50 | 0 |
| `enforc*` | 6 | 6 | 0 |

`restrict*` 是唯一有漏的：**686 ∩ 主集 684，差 2 段**，就是第 12.4 节那两段 Libnotify（页 `121838`、`122066`）——它们含 `restricted entitlements`，但谓语是 `now require`，不含 `addressed`，所以不在主集里。十个词族里九个完全嵌套进主集，说明**修复宾语词族与修复谓语几乎是共生关系**。

# 8 `by` 后面接什么：16 种形态、371 次，且 100% 在 Description

## 8.1 形态表

| `by` 后首词 | 次数 | | `by` 后首词 | 次数 |
|---|---:|---|---|---:|
| `removing` | **215** | | `preventing` | 6 |
| `adding` | **59** | | `disabling` | 4 |
| `restricting` | **33** | | `improved` | **3** |
| `moving` | 16 | | `updating` | 3 |
| `using` | 10 | | `enabling` | 2 |
| `adopting` | 8 | | `enforcing` | 2 |
| **`not`** | **7** | | `blocking` | 1 |
| | | | `refusing` | 1 |
| | | | `suspending` | 1 |
| **合计** | **371** ✓ | | | |

371 与第 4 章 `by` 介词档的 371 段**同数**（每段一个 `by`，无重复），这是一个真正的恒等。

## 8.2 三种不像动名词的形态

**① `by not …` 7 次。** 原文是 `…was addressed by not logging contents of text fields.`（7 段全是这一句）。这是"通过不做某事来修复"——英语里唯一把否定式放进方式状语的写法，且在本语料里只用于日志脱敏。

**② `by improved …` 3 次。** 原文是 `…was addressed by improved management of object lifetimes.`。`improved` 是形容词不是动名词，说明 `by` 槽位并非严格"by ＋ V-ing"，作者把它当成了"by ＋ 名词短语"的介词用。这也解释了第 6.1 节 `improved ∩ by ＝ 3` 这个反常小的数——**`improved` 有 6,037 段，却只有 3 段长在 `by` 后面**。

**③ 单例三种：`refusing`／`blocking`／`suspending` 各 1 次。** 原文依次是 `…addressed by refusing external connections by default.`（注意一句里两个 `by`）、`…addressed by blocking the vulnerable package.`、`…addressed by suspending Persona when the virtual keyboard is active.`。第三句正是第 5 章"其余档 15 段"与未覆盖组件名单里 `Persona` 相关写法的来源之一。

## 8.3 `by` 档的两句套话

| 套语（三字段） | 段 | ∩主集 | 页 |
|---|---:|---:|---:|
| `addressed by removing the vulnerable code` | **173** | **173** | 97 |
| `addressed by restricting options offered on a locked device` | **33** | **33** | 24 |

第一句 173 段占 `removing` 的 215 次的 **80.5%**，也就是"删掉 vulnerable code"是 `by` 档的绝对主力；第二句 33 段与 `restricting` 形态的 33 次**同数同集**（成员交 33）——`restricting` 在本语料里只有这一种完整写法。

# 9 句级十七桶：7,321 句次闭合，未归类 0

## 9.1 桶表

| 桶 | 句次 | | 桶 | 句次 |
|---|---:|---|---|---:|
| 内存与生命周期 | **1,363** | | 锁与配对 | 82 |
| 校验与检查 | **1,202** | | 零方式残句 | **28** |
| 边界与长度 | **1,031** | | 交互与同意 | 25 |
| 限制与访问控制 | **932** | | 认证与凭据 | 23 |
| 状态 | **747** | | 界面与绘制 | **4** |
| 输入与解析 | 583 | | 目的状语 | **1** |
| 脱敏与隐私 | 481 | | 逻辑与代码 | 417 |
| 兜底（带程度形容词但词族不匹配） | **195** | | 泛化处理与管理 | 124 |
| 其他带程度形容词 | 83 | | **合计** | **7,321** ✓ |

十七档是**优先序互斥**分档：**未归类 0**（闭合成立）、**多重命中 209**（在互斥之前，有 209 句至少能落两档，被优先序吃掉）。

## 9.2 前四桶吃掉 62%

1,363 ＋ 1,202 ＋ 1,031 ＋ 932 ＝ **4,528**，占 7,321 的 **61.85%**。加上"状态"747 与"输入与解析"583，六桶合计 **5,858＝80.02%**。修复表述的语义分类实际上只有六个大类有统计意义。

## 9.3 "兜底"195 与"泛化处理与管理"124 的分工

这两桶容易被混为一谈，但抽取规则不同：**"泛化处理与管理"只收 `handling`／`management` 这两个万能动词名化的宾语**（对应第 7 章的 `memory handling`／`state management` 之外的泛化形，如 `improved handling`、`improved management`），而**"兜底"收的是"有程度形容词、有谓语，但宾语里一个特征词族词都没有"的句子**。195 ＋ 124 ＝ 319，占 4.36%——也就是说**每 23 句修复表述里就有 1 句是"说了等于没说"**（`The issue was addressed with improvements.`、`This issue was addressed with improved handling.`）。

## 9.4 段轴与句轴不能混用

同一把刀在两个轴上的数差得很远：

| 刀 | 附表 A 段数（全段任一句命中） | 附表 C 句次（互斥优先序） |
|---|---:|---:|
| 界面与绘制宾语类 | **411** | **4** |
| 内存与生命周期宾语类 | **2,187** | **1,363** |
| 边界与长度宾语类 | 1,034 | 1,031 |
| 校验与检查宾语类 | 3,415 | 1,202 |
| 泛化处理与管理宾语类 | 2,500 | 124 |
| 兜底（其余句式） | 7,247 | 195 |

"界面与绘制"差 **100 倍**、"泛化处理与管理"差 **20 倍**，但"边界与长度"只差 **3**——不是错误：附表 A 的段数统计**全段任一句**含该词族，附表 C 只统计**判据句本身**（含 `addressed` 那一句）并按优先序互斥。`WebKit` 类条目常见"后果句里提到 rendering／drawing，修复句里说的是 improved memory handling"，词族命中落在后果句、判据句落进别的桶。兜底行最典型：段级 7,247（任何段都能被兜底刀看到）vs 句级 195（只有前面十六档都没吃到的那句才落进来）。所以**引用附表 C 时不能说"该桶的段数"**，只能说句次；附表 C 的页列合计 **1,682**、名列合计 **918** 也都是**可重叠**计数，不能当分母。

## 9.5 零方式残句 28 句次：Impact 21 句 ＋ Description 7 句

第 28 句次桶是"谓语在、方式状语和宾语都不在"的句子，形如 `An input validation issue was addressed.`。附表 C 给的口径是**句次 28、段 28、页 18、名 6**，其中 **Impact 侧 21 句 ＋ Description 侧 7 句**。

这与第 7.2 节的"零介词 8 段"不是同一批口径，但可以互相咬合：段级零介词档 **8 段** ＝ 附表 A `des 以 addressed＋标点收尾` 的 **7 段**（`An input validation issue was addressed.`，页 122371–122378）＋ 第 9.1 节末档"目的状语句"的 **1 段**（`A permissions issue was addressed to help ensure Personas are always protected`，有 `to` 无方式宾语）。7 ＋ 1 ＝ **8** ✓。

而残句桶里的 Impact 侧 **21 句**，正对应附表 A 的 `imp 以 addressed 裸收尾` **21 段**（13 页／4 名）——即第 3.2 节"仅 Impact 14 段"所在那批开源／截断页，被分句器切成无标点残句。21 ＋ 7 ＝ **28** ✓。

顺带把 Impact 侧 88 句次按桶拆完（附表 C 的"Impact 句"列相加）：

| 桶 | Impact 句次 |
|---|---:|
| 其余句式（兜底） | **46** |
| 零方式残句 | **21** |
| 锁与配对状态宾语类 | 12 |
| 校验与检查宾语类 | 4 |
| 限制与访问控制宾语类 | 3 |
| 脱敏与隐私宾语类 | 2 |
| 其余 11 桶 | **0** |
| 合计 | **88** ✓ |

**88 句次里的 67 句（46＋21）落在"兜底"与"残句"两桶**——Impact 侧的修复表述基本不是完整模板句，而是被版式解析切坏的碎片。

# 10 整句层：7,321 句次只由 233 个不同句子构成

## 10.1 句次与每段句数

| 判据 | 数 |
|---|---:|
| 判据句句次 | **7,321** ＝ Impact 侧 **88** ＋ Description 侧 **7,233** ✓ |
| distinct 判据句 | **233** |
| 其中出现 ≥2 次 | **215** |
| 其中只出现 1 次 | **18** |
| 每段判据句数＝1 的段 | **7,178** |
| ＝2 的段 | **64** |
| ＝3 的段 | **5** |
| 段数合计 | **7,247** ✓ |

句次自洽校验：7,178 ＋ 64×2 ＋ 5×3 ＝ 7,178 ＋ 128 ＋ 15 ＝ **7,321** ✓。所以**"每段最多 3 句修复表述"是本语料的硬上限**，且 2 句以上的段只占 **0.95%**。

## 10.2 头部整句

| 排名 | 判据句 | 句次 | 占 7,321 | 页 | 归桶 |
|---:|---|---:|---:|---:|---|
| 1 | `The issue was addressed with improved memory handling.` | **644** | **8.80%** | 165 | 内存与生命周期 |
| 2 | `The issue was addressed with improved checks.` | **514** | 7.02% | 138 | 校验与检查 |
| 3 | `A permissions issue was addressed with additional restrictions.` | **361** | 4.93% | 116 | 限制与访问控制 |
| 4 | `An out-of-bounds write issue was addressed with improved bounds checking.` | **258** | 3.52% | 66 | 边界与长度 |
| 5 | `This issue was addressed through improved state management.` | **237** | 3.24% | 127 | 状态 |
| 6 | `A logic issue was addressed with improved checks.` | 226 | 3.09% | 114 | 逻辑与代码 |
| 7 | `A use-after-free issue was addressed with improved memory management.` | 189 | 2.58% | 98 | 内存与生命周期 |
| 8 | `An out-of-bounds read was addressed with improved bounds checking.` | 169 | 2.31% | 71 | 边界与长度 |
| 9 | `An authorization issue was addressed with improved state management.` | 159 | 2.17% | 61 | 状态 |
| 10 | `An out-of-bounds access issue was addressed with improved bounds checking.` | 143 | 1.95% | 99 | 边界与长度 |
| — | 前 5 句合计 | **2,014** | **27.51%** | | |
| — | 前 10 句合计 | **2,900** | **39.61%** | | |

**10 句话吃掉近四成的全部修复表述。** 而 233 句里只出现 1 次的只有 18 句——**distinct 句数少、单例句更少，是"模板化"最强的证据**。

第 5 句是 `through` 档的**唯一头部句**：`through` 档 247 段里 237 段用的就是这一句（**95.95%**）。第 6 句 226 次全部落在逻辑类外部集（`∩L` ＝ 226，成员交 226），是第 14 章"外部集包含"里最彻底的一行。

## 10.3 末尾标点异形：103 句没有句号

| 形态 | 句次 | 段数 |
|---|---:|---:|
| 判据句带末尾句号 | **7,218** | **7,208** |
| 判据句无末尾标点 | **103** | **103** |
| 合计 | **7,321** ✓ | |

两个具体例子说明这不是噪声：

- `This issue was addressed with improved checks` 共 **142** 句次 ＝ 带句号 **131** ＋ 不带 **11**。附表 B 里的 131 是**带句号形**；如果按段级宽松统计会得到 142，两个数分属不同口径。
- `An input validation issue was addressed` 共 **28** 句 ＝ 带句号 **7** ＋ 不带 **21**。**75% 没有句号**。

再加两条口径注记：**Description 全文不以句号结尾的段 25 个**；**同一 Description 段里 `addressed` 出现次数分布为 0 次 19 段、1 次 7,214 段、≥2 次 0 段**（19 ＋ 7,214 ＝ 7,233 ✓）——也就是** Apple 从不在一段里写两次 addressed**。这一条把"一段多缺陷多修复"的可能彻底排除，第 5.2 节的 `Multiple … issues were addressed`（15 段）是复数主语共用一个谓语，而不是多个谓语。

## 10.4 头部整句的句次／段次／异形三口径

| 整句 | 句次（带句号） | 段次 | 无句号异形 |
|---|---:|---:|---:|
| `The issue was addressed with improved memory handling` | 644 | 644 | 0 |
| `The issue was addressed with improved checks` | 514 | 514 | 0 |
| `A permissions issue was addressed with additional restrictions` | 361 | 361 | 0 |
| `This issue was addressed with improved checks` | **131** | **142** | **11** |
| `An input validation issue was addressed` | **7** | **28** | **21** |

前三句是"句次＝段次、零异形"的稳定模板。后两句的异形**不在同一侧**：`An input validation issue was addressed` 的 21 个无句号句次全在 **Impact 字段**，`This issue was addressed with improved checks` 的 11 个全在 **Description 字段**。

**标点异形不止这两套。** 233 个 distinct 判据句里，**216 个带末尾句号、17 个不带**（相加 233 ✓）；把句末句号归并后共 **228 个模板**，其中 **5 个模板两种标点形都有**（`A path handling issue was addressed with improved validation`／`An authorization issue was addressed with improved state management`／`An input validation issue was addressed`／`The issue was resolved by sanitizing logging`／`This issue was addressed with improved checks`）、**211 个只有带句号形、12 个只有无句号形**（5＋211＋12＝228 ✓）。

**103 个无句号句次拆成字段是 Impact 侧 78 ＋ Description 侧 25。** Impact 侧那 78 句里，**44 句被谓语刀打中的其实是名词**：`Visiting a malicious website may lead to address bar spoofing`（18 句次）、`A website may be able to determine a user's IP address with Private Relay turned on`（7）、`An attacker may be able to track users through their IP address`（6）、`On a device with Private Relay enabled … may reveal the originating IP address to the website`（4）、`"Hide IP Address" and "Block All Remote Content" may not apply to all mail content`（4）、`Third party app extensions may not receive the correct sandbox restrictions`（3）、`Privacy Indicators for microphone or camera access may not be correctly displayed`（2）——这些句子本来就不是修复句，`address` 是"地址栏／IP 地址"、`correct` 是"正确的／正确地"。**所以"句末没有标点"不等于"句子被截断"，它更多意味着"这一句根本不在修复模板里"。** 另 8 句次是 `This is a supplementary fix for an attack that was blocked in iOS 17.2. (Apple is aware of …)` 那条自带右括号的长句，句末括号本身不算句号。

Description 侧只有 **6 个 distinct 模板、25 句次**不带句号，主力是 `This issue was addressed with improved checks`（11）与 `The issue was resolved by sanitizing logging`（9）。**第 12 章那 185 段开源模板一段都不在主集里**（成员交 0），所以标点异形不可能来自它们。

# 11 版式错位：粗体版式域里，主刀 100% 靠 Description

## 11.1 两个版式域的分层

| 域 | 全部段 | ∩主集 | with | by | through | 零介词 |
|---|---:|---:|---:|---:|---:|---:|
| 域 A（标准字段版式，246 页） | 9,045 | **7,159**（79.15%） | — | — | — | — |
| 域 B（粗体版式，11 页） | 91 | **88**（**96.70%**） | 84 | 3 | 1 | **0** |
| 全树 | 9,136 | **7,247**（79.32%） | 6,588 | 371 | 247 | 8 |

7,159 ＋ 88 ＝ **7,247** ✓；84 ＋ 3 ＋ 1 ＋ 0 ＝ **88** ✓。

**域 B 的命中率 96.70% 比域 A 的 79.15% 高 17.5 个百分点。** 这不是"粗体版式更爱写修复"，而是那 11 页本身是早期／少数组件页，段落构成几乎全是"缺陷一句＋修复一句"的两段式。

## 11.2 域 B 的 Impact 侧主刀命中 0 段

**`address*／resolv*／fix*／correct*／mitigat*／patch*` 六个谓语形在域 B 的 Impact 字段里命中 0 段。** 域 B 含非空 Impact 字段的段有 **88** 段，这 88 段里主刀命中 **0**；同时这 88 段的 Description 字段主刀命中也是 **88**（100%）。也就是说域 B 的 88 段主集**全部**由 Description 带进来，Impact 字段只写后果、从不写修复。

这条与第 3 章的全树读数（Impact 侧 83 段）**方向一致但机制不同**：全树那 83 段是版式解析把修复句放进了 Impact 的**错位**，域 B 则是**根本没有把修复写进 Impact**。引用"Impact 侧有 83 段修复表述"时必须同时说明**这 83 段里没有一段来自域 B**。

# 12 残差 187 段：185 段开源模板 ＋ 2 段 Libnotify

## 12.1 闭合

有 Description、但谓语刀一个都没打到的段 ＝ **187**。拆开：

| 块 | 段 | 内容 |
|---|---:|---|
| 开源免责模板 | **185** | 同一套话术的**三个字符级异体**：187 字符 151 段／186 字符 33 段／184 字符 1 段 |
| Libnotify 两段 | **2** | 页 `121838`、`122066` |
| 其他 | **0** | ✓ 187 闭合 |

开源模板最长异体全文：`This is a vulnerability in open source code and Apple Software is among the affected projects. The CVE-ID was assigned by a third party. Learn more about the issue and CVE-ID at cve.org .`

**185 段不是同一串字符。** 按 Description 字段全文做 distinct，得到 **3 条**文本：最长那条（187 字符，`cve.org` 与句号之间多一个空格）占 **151 段**；把那个空格去掉就是 186 字符，占 **33 段**；第 184 字符那条只出现 **1 段**，它少的是系动词——`Apple Software among the affected projects`（没有 `is`）。151 ＋ 33 ＋ 1 ＝ **185** ✓。**所以"同一句模板"是语义上的同一句，不是字节上的同一串**；任何按整串等值去重开源段的写法，都会把这 185 段读成 3 段。

注意末尾 `cve.org .`——**域名和句点之间有一个空格**，这是这批页的排版特征：187 与 184 两个异体带这个空格（151 ＋ 1 ＝ 152 段），186 字符那 33 段没有。它与"宽松刀会额外抓到 `incorrect`"是两件互不相干的事（见 12.3）。

## 12.2 `open source` 的 199 段

`This is a vulnerability in open source` 三字段命中 **199** 段（108 页），拆成 **185 段不在主集**（残差）＋ **14 段在主集内**（这些段既有开源模板句又有 `addressed` 句）。185 ＋ 14 ＝ 199 ✓，且这 **14 与第 3.2 节的"仅 Impact 14 段"成员交 14**——**开源模板句被解析器挂到了 Impact 字段**，正是那 14 段错位的成因。

## 12.3 词界刀 vs 宽松刀：10 段全是 `incorrect`

| 口径 | 段 |
|---|---:|
| 主刀（带 `(?<![\w-])`／`(?![\w-])` 词界） | **7,247** |
| 去掉左右词界（宽松） | **7,257** |
| 差 | **＋10** |

这 10 段实测**全部**是被 `incorrect`（**8** 段）／`incorrectly`（**2** 段）命中，且**10 段全部落在残差 187 段之内**。也就是说：

1. 宽松口径的"多出来的段"不是新段落，而是**同一批残差段里出现了 `incorrect`**；
2. `incorrect` 是形容词"不正确的"，与谓语"纠正"是两套语义；
3. **词界是本篇主刀的硬约束**，去掉词界会把后果描述句误当修复表述。

## 12.4 两段 Libnotify

`An app could impersonate system notifications. Sensitive notifications now require restricted entitlements.`

这两段用 `now require`（"现在需要"）表达修复，**谓语刀完全抓不到**。三字段 `now require` 命中 **2** 段、∩主集 **0**。所以**本语的修复谓语表（六形）里没有"现在要求"这一类现在时表述**，它们构成主集的**系统性下界**：主集 7,247 段之外，至少有 2 段是在说修复的。这 2 段同时是第 7.6 节 `restrict*` 686∩684 那 2 段差的来源。

# 13 致谢侧 416 段：405 ＋ 11，两批交 0

## 13.1 416 的构成

| 判据 | 段 |
|---|---:|
| 主刀扫致谢字段（`%CA`） | **416** |
| `patch*` 词界命中致谢字段 | **416** |
| 两者成员交 | **416（100%）** |

**致谢侧主刀命中 416 段，全部由 `patch*` 一个词带来。** `addressed`／`resolved`／`corrected`／`mitigated` 四形在致谢字段命中 **0**；`fix` 形命中 **1** 段——页 `118479`（WebKit）致谢字段末尾那条备注 `Note: This security fix is included in the visionOS 1.0.1 update.`。它就在下面拆出的那 **11 段标准免责句**里，同一段的免责文字里本来就有 `patches`，所以这个 1 **不给 416 增加任何成员**：把 `patch*` 从刀里摘掉，致谢侧剩下的就是它（见 13.2 的"去 `patch` 后致谢字段命中 **1**"）。

再拆 416：

| 块 | 段 | 形 |
|---|---:|---|
| 致谢人带 `@patch1t` | **405** | `We would like to acknowledge Mickey Jin (@patch1t) …` |
| 标准免责句含 `patch` | **11** | Apple 关于第三方补丁归属的免责表述 |
| 成员交 | **0** ✓ | 两批段互不相干 |
| 并集 | **416** ✓ | 405 ＋ 11 |

**`@patch1t` 是一个人的 Twitter 手柄，不是修复动词。** 405 段（占致谢侧命中的 97.4%）纯粹因为有人名里带 `patch` 才被抓到。

## 13.2 把 patch 从刀里摘掉会怎样

| 判据 | 段 |
|---|---:|
| 主刀（六形，含 patch）主集 | **7,247** |
| 去 `patch` 后的主刀主集 | **7,247** |
| 差 | **0** |
| 主刀致谢字段命中（含 patch） | **416** |
| 去 `patch` 后致谢字段命中 | **1** |
| 靠 `patch` 才进致谢集合的段 | **415** |

这是一条很干净的结论：**`patch` 这一形在主集侧贡献 0 段，在致谢侧贡献 415 段。** 六形刀里的 `patch*` 是**纯噪声项**——留着它，只是为了在正文里说明"为什么致谢字段不能和 imp/des 一起扫"。本篇生成器 `MK()` 扫三字段得到的主集是 7,247（与两字段 `%CU` 相同），但致谢侧 416 段完全依赖这个词。

## 13.3 三字段并集 7,261 ＝ 7,247 ＋ 致谢独有 14

| 集合 | 段 |
|---|---:|
| 两字段主集（imp ∪ des） | **7,247** |
| 三字段并集（imp ∪ des ∪ ack） | **7,261** |
| 致谢独有（三字段有、两字段无） | **14** |

**这里出现本篇最容易被误读的一组数：两个"14"。**

| 14 段集合 | 成员 |
|---|---|
| 仅 Impact 带进主集（第 3.2 节） | 13 段 `An input validation issue was addressed`（无句号，页 122371–122378）＋ 1 段 `A path handling issue was addressed with improved validation`（页 125634） |
| 仅致谢带进三字段并集 | 14 段全是 `We would like to acknowledge … (@patch1t) …` 致谢句（页 121238、124149、125110 等） |
| **两者成员交** | **0** |

**同为 14 段，交集为 0，是完全不相干的两批段。** 所以"三字段比两字段多 14 段"与"14 段修复表述只在 Impact"这两句话**没有任何因果关系**，只是巧合同数。这正是本系列坚持"同数必须做成员交"的原因。

# 14 姊妹集包含：四集 100% 落在主集内，十二集并集只吃掉 70.89%

## 14.1 十二个外部集对主集的包含率

| 外部集 | 来源层 | 集内段 | ∩主集 | 包含率 |
|---|---|---:|---:|---:|
| **WO** ＝ 无前提触达集 | #119 | 178 | **178** | **100.00%** |
| **E** ＝ 在野利用集 | #114 | 108 | **108** | **100.00%** |
| **RC** ＝ 竞态集 | #122 | 232 | **232** | **100.00%** |
| **L** ＝ 逻辑缺陷集 | #123 | 550 | **550** | **100.00%** |
| UU ＝ Impact 含 `user` 集 | 载体层 | 1,627 | 1,624 | 99.82% |
| BP ＝ 绕过集 | #126 | 301 | 300 | 99.67% |
| HI ＝ 交互触发集 | 触发条件层 | 273 | 271 | 99.27% |
| R ＝ 远程触达词族 | #118 | 843 | 820 | 97.27% |
| X ＝ 泄露集 | #125 | 427 | 418 | 97.89% |
| G ＝ 动名词开头集 | 句式层 | 2,179 | 2,070 | 95.00% |
| Ds ＝ 拒绝服务集 | #124 | 470 | 436 | 92.77% |
| M2 ＝ 同段 ≥2 后果类集 | #115 | 185 | 166 | 89.73% |

**四个集 100% 被主集包含**：凡写了"无前提触达／在野／竞态／逻辑"的段，都同时写了一个修复谓语。这不是巧合——这四类的措辞本身就依赖"某某问题已被处理"这句才成立。

反过来，**M2（89.73%）与 Ds（92.77%）是漏得最多的**：这两类是靠"后果词"定义的集，一段可以只写后果（`…may crash the device and corrupt data`）而完全不写修复。所以**"本篇主集几乎覆盖全树"与"后果类集覆盖不到 100%"这两件事是同一件事的两个方向**：主集的补集 187 段，正好是那些"只说漏洞不说修复"的段。

## 14.2 十二集并集：5,277 段，主集未被覆盖 2,110 段

| 判据 | 段 |
|---|---:|
| 十二集并集（三字段口径） | **5,277** |
| 并集 ∩ 主集 | **5,137** |
| **主集未被十二集覆盖** | **2,110**（占主集 **29.12%**） |
| 未覆盖段散在 | **173** 页 |

5,137 ＋ 2,110 ＝ **7,247** ✓。也就是说：**把十二个姊妹集全部并起来，仍然有接近三成的"修复表述段"没有任何一篇从语义角度抓到过。** 这正是本篇存在的理由——修复表述层是**结构层**，不是语义层，语义刀切不到它。

## 14.3 未覆盖段的组件名分布

| 组件名 | 未覆盖段 | 介词档拆解 |
|---|---:|---|
| **Kernel** | **346** | `with` 338 ＋ `by` 8 ＝ 346 ✓ |
| PackageKit | 84 | — |
| AppleMobileFileIntegrity | 55 | — |
| AVEVideoEncoder | 35 | — |
| Siri | 35 | — |
| Apple Neural Engine | 32 | — |
| APFS | 31 | — |
| Shortcuts／libxpc | 29／29 | — |
| StorageKit | 28 | — |

Kernel 一个组件就占了未覆盖量的 **16.40%**，且它的拆解是 `with` 338 ＋ `by` 8（闭合）。看未覆盖段的原文就明白为什么语义刀抓不到：

- `The issue was addressed with improved checks.`（没有缺陷类型词，只有 `checks`）
- `A type confusion issue was addressed with improved memory handling.`（`type confusion` 属 #130 但不属这十二集）
- `The issue was addressed by suspending Persona when the virtual keyboard is active.`
- `This issue was addressed by removing the vulnerable code.`
- `This issue was addressed by restricting options offered on a locked device.`
- `This issue was addressed with improved data protection.`

**共同点：句子只描述"怎么修的"，不描述"什么坏了"。** 十二个姊妹集全部按"坏了什么"切分，所以这一批必然落在所有语义刀的缝里。

## 14.4 一个必须点名的同数陷阱

`This is a vulnerability in open source` 三字段 **199** 段，∩主集只有 **14** 段；而主集内"仅 Impact 带进来的"也是 **14** 段。两个 14 的成员交＝**14**，这一对**确实同一批**。但第 13.3 节里"致谢独有 14 段"与它们是**完全不同的一批**（成员交 **0**）。**三个 14，两个恒等、一个零交**——引用任何"14 段"都必须同时说明是哪一个 14。

# 15 后果八类的两个口径：462 段与 4,175 段

## 15.1 两套口径并列

| 后果类 | 口径甲：`MK()` 三字段 ∩主集 | 口径乙：后果词只算 Description ∩主集 |
|---|---:|---:|
| ACE 任意代码执行 | **317** | **0** |
| BYP 绕过 | 300 | 31 |
| DIS 数据泄露 | 591 | **110** |
| DOS 拒绝服务／崩溃 | **1,946** | 50 |
| FULL 完全控制 | 63 | **0** |
| MEM 内存破坏 | 360 | **184** |
| PRI 隐私绕过 | 123 | **0** |
| SBX 沙盒逃逸 | 475 | 87 |
| **相加** | **4,175** | **462** |

两个数都不是主集段数（7,247），也不能互相换算。口径甲把后果词扫三个字段（含致谢），所以 DOS 的 1,946 里混着大量 `crash`／`memory corruption` 出现在 Impact 段的段；口径乙只承认**修复段自己把后果写在 Description 里**，所以只有 462 段。

## 15.2 乙口径里三个 0

`ACE`、`FULL`、`PRI` 三类在 Description 字段里与主集相交 **0 段**。也就是说：**写"执行任意代码""获取完全控制""绕过隐私保护"这类后果词的时候，苹果从不把它写在含修复谓语的 Description 段内**——它们只出现在 Impact 字段。这是字段分工的直接证据：**Description ＝ 缺陷 ＋ 修复；Impact ＝ 后果。**

## 15.3 同段 ≥2 类后果：Description 侧 0，Impact 侧 185

**Description 侧全树含 ≥2 个后果类的段 ＝ 0；Impact 侧 ＝ 185 段**（主集内分别是 **0** 与 **166**）。这 185／166 就是第 14 章外部集 `M2` 的两个数——**`M2` 不是另一把刀，它就是"Impact 字段命中 ≥2 后果类"这一条判据本身**，所以两处读数必须相等，也确实相等。

185 段里命中 **2 类 167 段、3 类 18 段**（没有 4 类及以上）；主集内 166 段则是 **2 类 148、3 类 18**。七种组合中最常见的是 `ACE+DOS`（92 段）与 `DIS+DOS`（49 段），而那 18 段 3 类全是同一组 `ACE+PRI+SBX`，形如 `An app may be able to execute arbitrary code out of its sandbox or with certain elevated privileges.`——一句话同时把"任意代码执行／提权／沙盒逃逸"三类后果词写在一起。

**所以"#135 PRI 0 段、#137 FULL／PRI 皆 0"那种"两侧都零"的读法在本篇不成立：零只出现在 Description 侧。** 修复表述层确实不承担"一段多类后果"——`The issue was addressed with improved checks.` 里没有后果词；但同一条目的 **Impact 字段**可以一次列三类。这与 15.2 的字段分工是同一件事的两面：**后果词集中在 Impact 字段，多类并现也只发生在 Impact 字段。**

## 15.4 为什么甲口径的 4,175 大于乙口径的 462 达 9 倍

差额 **3,713** 段全部来自"Impact 字段有后果词、Description 字段没有"的那些段。本语料的常规写法是：Description 写 `A memory corruption issue existed in X. The issue was addressed with improved memory handling.`，把"可导致任意代码执行"放在 Impact 字段。所以**"修复段含后果词"是少数写法（462／7,247 ＝ 6.38%）**，任何"Impact 侧 83 段"式的读法都要注意这个方向：**不是修复句里带后果，而是后果句里不带修复。**

# 16 页面层：256 页扛 7,247 段，Top10 页吃掉 20.88%

## 16.1 页九档闭合

| 该页含本篇段数 | 页 |
|---|---:|
| 1 段 | **50** |
| 2 段 | 23 |
| 3 段 | 3 |
| 4 段 | 3 |
| 5 段 | 3 |
| 6–9 段 | 12 |
| 10–19 段 | 37 |
| 20–49 段 | **85** |
| 50 段以上 | **40** |
| 合计 | **256** ✓ |

50 ＋ 23 ＋ 3 ＋ 3 ＋ 3 ＝ **82** 页只有 1–5 段；40 页有 50 段以上。**"全树 257 页里 256 页都含本篇条目"，唯一不含的那一页是 `127557`**——该页只有 **1** 段（组件名 `Bluetooth`），内容正是第 12 章的开源模板 `This is a vulnerability in open source code and Apple Software is among the affected projects. The CVE-ID was assigned by a third party. …`。**全树唯一一根"零修复表述"的页，就是一段开源免责模板。**

## 16.2 Top10 页

| 排名 | 页面 | 本篇段 | 该页特征 |
|---:|---|---:|---|
| 1 | `149035` | **234** | `1490xx` 批 |
| 2 | `149043` | **171** | `1490xx` 批，含第 16.4 节 3 段无编号 |
| 3 | `149042` | **168** | `1490xx` 批 |
| 4 | `122373` | **156** | `1223xx` 开源组件批 |
| 5 | `128067` | 153 | — |
| 6 | `149034` | 141 | `1490xx` 批 |
| 7 | `128071` | 133 | — |
| 8 | `128072` | 125 | — |
| 9 | `149038` | 124 | `1490xx` 批 |
| 10 | `122374` | 108 | `1223xx` 开源组件批 |
| — | **合计** | **1,513** | 占 7,247 的 **20.88%** |

这 10 页的**全部段落**是 1,844 段，本篇抓到 1,513 段＝**82.05%**。前 10 页里 **5** 页属 `1490xx` 号段、**2** 页属 `1223xx` 号段——**而 `1223xx` 两页同时是第 12 章开源残差与第 3.2 节"仅 Impact"错位的来源页**（`122371`–`122378` 八页）。"页面批次"是本篇唯一能把 7,247 段进一步归因的结构变量。

## 16.3 同页共存与"全本篇页"

| 判据 | 数 |
|---|---:|
| 这 256 页的全部段落 | **9,135**（全树 9,136 的 99.99%） |
| 本篇占该页集 | **79.33%** |
| 每页平均本篇段 | **28.31** 段（9,135÷256＝35.68 为每页全段均值） |
| **全本篇页**（该页所有段都被本篇抓到） | **83** 页 |

83 页是"整页都在说修复"的页面。它们绝大多数是 1–5 段的小页（第 16.1 节那 82 页里的一部分）——**小页天然容易 100% 命中**。

## 16.4 CVE 编号分箱：2,040 个编号全部独占

| 该段带的 distinct 编号数 | 段 |
|---|---:|
| 0 个 | **4** |
| 1 个 | **6,821** |
| 2 个 | 295 |
| 3 个 | 79 |
| 4 个及以上 | 48 |
| 合计 | **7,247** ✓ |

带编号段 **7,243**；distinct 编号 **2,040** 个；**本集独占 2,040 个、与其他集合共享 0 个**。"独占 2,040／共享 0"意味着：**凡是含修复谓语的段所挂的 CVE 编号，没有任何一个也出现在不含修复谓语的段上。** 换句话说，**"写了修复的条目"与"没写修复的条目"在编号层完全不重叠**——那 187 段残差不只是措辞不同，它们属于另外一批编号。

4 段无编号（实测逐条）：`149043` 页 `Shortcuts` **3** 段（`A privacy／An authorization／A permissions issue was addressed with improved state management.`）＋ `127111` 页 `Siri` **1** 段（`A logic issue was addressed with improved checks.`）。**4 段全是 `with` 档标准模板句却没有任何 CVE 编号**，是全树少见的"只更新措辞不补编号"的条目。

## 16.5 致谢人数分箱

| 该段致谢人数 | 段 |
|---|---:|
| 1 人 | **4,493** |
| 2 人 | **1,776** |
| 3 人 | 434 |
| 4 人 | 257 |
| 5 人 | 119 |
| 6–8 人 | 92 |
| 9 人及以上 | 72 |
| 合计 | **7,243** ✓ |

人次 **12,519**，均值 **1.73**，最大 **37** 人。全树口径是均值 **1.80**／最大 37——**修复段的致谢密度略低于全树平均**。4,493 ＋ 1,776 ＝ **6,269** 段（占 7,243 的 **86.55%**）的致谢人数不超过 2 人；9 人以上 72 段是单段最大批次致谢。

**致谢轴的"0 人档"与编号轴的"0 个档"是同一批段**：主集里致谢字段为空的段实测 **4** 段，与第 16.4 节无 CVE 编号的 **4** 段**成员交 4**——完全同一批（页 `149043` 的 3 段 `Shortcuts` ＋ 页 `127111` 的 1 段 `Siri`）。这两批段是"追加条目"形态：补了一句修复表述，但既没有独立编号也没有独立致谢。这是本篇少见的**两轴恒等**，与第 14.4 节"三个 14、两同一零"恰好相反，说明分箱表 16.5 从 1 人起排、把 0 人档并进合计差额（7,247 − 7,243 ＝ 4）是**正确的**，不是漏项。

# 17 组件名层：394 名扛 7,247 段，376 名在名内 100% 命中

## 17.1 名层规模

| 判据 | 数 |
|---|---:|
| 有本篇段的组件名 | **394**（全树 472 名，83.47%） |
| 段合计 | **7,247** ✓ |
| 页列合计（逐行去重再相加） | **4,474** |
| 只挂 1 段的名 | **52** |
| 挂 ≤5 段的名 | **171** |
| **名内 100% 命中的名** | **376** |
| 名内非 100% 命中的名 | **18** |
| 这 394 名的 Impact 全集段数 | **7,382**（占全树含 Impact 段 7,434 的 **99.3%**） |

**"名内 100% 命中 376 名"是本系列的强读数**：一个组件名底下的**每一段**都被本篇抓到。因为主集覆盖 79.32%，如果组件名层没有强结构，376／394 ＝ 95.4% 是不可能出现的。剩下 18 名是"部分段不写修复"的组件（开源组件页与 Kernel 的一部分条目）。

## 17.2 头部名

| 排名 | 组件名 | 本篇段 | 页 | 占比 | ∩R | ∩E | ∩L | ∩BP |
|---:|---|---:|---:|---:|---:|---:|---:|---:|
| 1 | WebKit | **900** | 155 | 98.9% | 134 | **41** | 71 | 24 |
| 2 | Kernel | **681** | 153 | **100.0%** | 77 | 9 | 48 | 31 |
| 3 | ImageIO | 245 | 119 | 93.9% | 0 | 7 | 14 | 0 |
| 4 | CoreMedia | 150 | 93 | 100.0% | 0 | 8 | 0 | 0 |
| 5 | AppleMobileFileIntegrity | 146 | 62 | 100.0% | 0 | 0 | 20 | 14 |
| 6–10 | Shortcuts 129／Siri 116／SceneKit 112／PackageKit 106／Audio 95 | **558** | — | — | — | — | — | — |
| — | Top5 | **2,122** | | **29.28%** | | | | |
| — | Top10 | **2,680** | | **36.98%** | | | | |

`AirPlay` 只有 77 段但 **∩R ＝ 77（100%）**——AirPlay 的条目全部同时是远程触达条目，这与 #126 的 bypass 宾语分区读数一致。`Model I/O` 84 段、占比 **95.5%**（名全集 88 段），是 18 个"非 100%"名之一。

## 17.3 那 18 个"名内非 100%"的组件名，几乎全是开源第三方件

附表 F 最后一列（本篇段 ÷ 该名含 Impact 全集）不为 100.0% 的名只有 **18** 个，按占比升序：

| 组件名 | 本篇段 | 名全集 | 占比 |
|---|---:|---:|---:|
| **Vim** | 1 | 9 | **11.1%** |
| **curl** | 10 | 39 | **25.6%** |
| **libxml2** | 5 | 19 | **26.3%** |
| **OpenSSH** | 4 | 10 | **40.0%** |
| **libxslt** | 14 | 30 | **46.7%** |
| **Perl** | 4 | 8 | **50.0%** |
| **libarchive** | 19 | 30 | **63.3%** |
| Safari Downloads | 5 | 7 | 71.4% |
| **Libnotify** | 8 | 10 | **80.0%** |
| Passkeys | 4 | 5 | 80.0% |
| AppleJPEG | 23 | 28 | 82.1% |
| Heimdal | 16 | 19 | 84.2% |
| sudo | 9 | 10 | 90.0% |
| ImageIO | 245 | 261 | 93.9% |
| CUPS | 41 | 43 | 95.3% |
| Model I/O | 84 | 88 | 95.5% |
| Bluetooth | 40 | 41 | 97.6% |
| WebKit | 900 | 910 | 98.9% |
| — | **18 名** | — | 其余 **376** 名全部 100.0% |

**排序前七位全是第三方开源件**（Vim／curl／libxml2／OpenSSH／libxslt／Perl／libarchive）。这些组件的条目常用第 12 章那句开源模板代替修复表述，或只写版本号升级（`updating to curl version 8.4.0`）而不写 `addressed`。**`Libnotify` 8／10 ＝ 80.0% 里缺的正是第 12.4 节那 2 段 `now require`。** 而 `WebKit` 98.9%（900／910）与 `Kernel` 100.0%（681／681）说明**苹果自研件的每一条都写了修复句**。

这张表就是"修复表述层的覆盖缺口分布"：**缺口不是随机散落的，它精确地集中在第三方开源组件名底下。**

# 18 中文层：63 词里 15 个全零，「修复」只有 14 次

## 18.1 五个"中文里不存在"的修复谓语

| 中文词 | 次数 | 页 |
|---|---:|---:|
| 修复 | **14** | 8 |
| 已修复 | **0** | 0 |
| 被修复 | **0** | 0 |
| 解决 | 15 | 12 |
| 已解决 | **0** | **0** |
| 缓解 | **0** | **0** |
| 更正 | **0** | **0** |
| 补丁 | 4 | 1 |
| 改善 | 4 | 3 |
| 处理 | **337** | **178（100.0%）** |

**英文侧 7,214 段的 `addressed`，在 178 份中文文档里对应"修复"只有 14 次／8 页，"已修复／被修复"完全为 0。** 中文文档层不是顾问页的译文，它压根没有"修复表述"这一层——它讲的是机制。

## 18.2 十五个全零词

`已修复`、`被修复`、`已解决`、`缓解`、`更正`、`输入校验`、`边界检查`、`权限检查`、`内存处理`、`状态管理`、`状态处理`、`处理逻辑`、`禁用`、`脱敏`、`CVE`——**英文修复宾语头四个形态里，`memory handling`→`内存处理`、`bounds checking`→`边界检查`、`state management`→`状态管理` 三个多字直译在中文层全为 0 次，`input validation`→`输入校验` 同样为 0。**

**第四个头部形态 `checks`（1,036 段）不能这样对照。** 它的最短直译 `检查` 在中文层有 **66 次／30 页**（最集中于"管理式 Apple 账户安全性"13 次），并不是 0；但 `检查` 是通用动词（"检查更新""检查设置"），与英文 `improved checks` 那个名词化宾语不是同一个词位。真正带修复宾语形制的复合词 `边界检查`、`权限检查` 都是 0，`完整性检查` 只有 5 次／4 页。所以这一条的准确说法是：**中文层有"检查"这个动词，没有"（改进的）检查"这个修复宾语。**

`CVE` 全零尤其说明问题：英文侧那 185 段开源模板反复写 `CVE-ID`，而中文文档层一次都没出现过这个串。

## 18.3 高频词的分布

| 中文词 | 次数 | 页 | 最集中页 |
|---|---:|---:|---|
| 保护 | **5,438** | 178 | — |
| 使用 | **3,291** | 178 | — |
| 加密 | 1,763 | 178 | 安全隔区（71 次） |
| 通过 | 1,119 | — | — |
| 限制 | 478 | 178 | LocalPolicy 签名密钥创建和管理（21 次） |
| 验证 | 432 | 178 | Apple 钱包中的证件（19 次） |
| 更新 | 373 | — | — |
| 授权 | 352 | — | — |
| **处理** | **337** | **178** | 安全隔区（24 次） |
| 锁定 | 305 | — | — |
| 权限 | 283 | — | — |
| 组件 | 270 | — | — |
| 采用 | 262 | — | — |
| 版本 | 254 | — | — |
| 改进 | 231 | — | — |

`校验` 19 次／3 页（16 次集中在《Apple 钱包中证件的安全性》一页），`验证` 432 次／178 页——**同一个英文 `validation` 在中文层有 22 倍的词频差，且分布完全不同。** `漏洞` 20 次／12 页、`缺陷` 1 次／1 页。

**结论：中文文档层与英文顾问页在"修复"这件事上没有对应关系。** 用中文层去校验英文措辞统计是无效的，本篇附表 E 只用来标记"这些英文术语在中文官方文档里找不到落点"这一事实。

# 19 十二步复算清单、结论与本篇可被推翻的地方

## 19.1 复算清单（按序做，每步都有闭合式）

1. 读 `_tmp/adv2` 258 份 HTML，排除 `122085` → **257 页／9,136 段／472 名／7,434 含 Impact／9,132 含致谢**（四锚点收块，否则页免责声明会渗进字段文本）。
2. 主刀 `$MAIN = (?<![\w-])(?:address\w*|resolv\w*|fix\w*|correct\w*|mitigat\w*|patch\w*)(?![\w-])`，**只扫 imp 与 des** → `%CI` **83**／`%CD` **7,233**／`%CU` **7,247**，拆 **仅I 14＋仅D 7,164＋I∩D 69** ✓。
3. 另扫 ack → `%CA` **416**；`%PTCHALL = MK($MAIN)` **7,261**；致谢独有 **14**。
4. 谓语六形逐形（des∶imp）：**7,214∶64／19∶1／0∶13／24∶5／0∶0／0∶0**；`addressed` imp 25、`address` 原形 imp 39（∩主 33）。
5. 介词四档在 `%ADRD`（**7,214** 段）内按 `with>by>through>零` 优先序分 → **6,588＋371＋247＋8 ＝ 7,214** ✓；主语四档在 `%CD`（**7,233**）内 → **1,049＋1,561＋4,608＋15 ＝ 7,233** ✓；16 格矩阵列合计须等于介词四档、行合计等于主语四档，格和 **7,214** ✓。
6. 程度五档：`improved` **6,037**／`additional` **850**／`more` **203**／`new` **5**／并集 **7,095**；断言 **`improved ∩ additional ＝ 0`**、`improved ∩ new ＝ 0`；`more` 的 185 段非主集要与残差开源 185 段做**双向成员交**。
7. 宾语抽取：75 形态／6,888 段／2 形态段 **0**／抽不到 **345** ＝ by 297＋with 22＋零介词 8＋无 `addressed` 18 ✓（through **0**）。
8. 句级 17 桶（优先序）：句次相加 **7,321** ＝ Impact 88 ＋ Description 7,233 ✓，未归类 **0**；续表 7 档同样闭合 **7,321**；Impact 句按桶拆 **46＋21＋12＋4＋3＋2 ＝ 88** ✓。
9. 标点：判据句 **7,218 ＋ 103 ＝ 7,321** ✓；`addressed` 在单段 Description 内出现次数 **0 次 19 段／1 次 7,214 段／≥2 次 0 段** ✓。
10. 残差 **187 ＝ 185 ＋ 2** ✓；`open source` des **199 ＝ 185 ＋ 14** ✓；宽松刀 **7,257 ＝ 7,247 ＋ 10**，10 段全为 `incorrect`(8)／`incorrectly`(2) 且全在 187 内 ✓。
11. 致谢 **416 ＝ 405 ＋ 11**、成员交 **0** ✓；摘 `patch` 后主集 **7,247（差 0）**、致谢 **1**。
12. 版式域：域 A **7,159／9,045**、域 B **88／91**、7,159＋88＝**7,247** ✓；域 B imp 侧主刀 **0**、des 侧 **88**。

## 19.2 一句话

**苹果对"修好了"这件事只有 233 种说法，其中 10 句占四成；而这些说法 97.48% 只长在 Description 字段里、91% 靠一个介词 `with` 撑着、致谢字段唯一的命中来自一个人名句柄 `@patch1t`。**

## 19.3 八条反直觉结论的落点

| 结论 | 章节 |
|---|---|
| 主集 7,247 段的"大"是结构性的，补集只有 187 段 | 第 1、12 章 |
| 六个谓语形只有一个活着，且 Impact 侧方向相反 | 第 2、3 章 |
| 方式介词四档互斥闭合，`with` 一档承 90.91% | 第 4 章 |
| `improved` 与 `additional` 从不同段；`more` 是导航用语 | 第 6 章 |
| 修复宾语只有 75 种、345 段抽不出宾语且四块闭合 | 第 7 章 |
| 致谢 416 ＝ 405＋11，两批交 0；摘 `patch` 主集差 0 | 第 13 章 |
| 没有任何"将来时"修复表述；`were addressed` 15 段全在 des | 第 2、5 章 |
| 7,321 句次只由 233 句写成 | 第 10 章 |

## 19.4 本篇能被推翻的六个地方

1. **镜像时效**：`_tmp/adv2` 若与苹果当前页面不同，全部数字作废（苹果会向同一页追加条目）。
2. **块切分**：域 A 的四锚点收块规则若改动，段数与"段"的边界会变，第 7 章宾语抽取与第 10 章句次都会漂移。
3. **`patch` 留在词表**：这是本篇主动的选择（保留以便展示致谢侧噪声）。若把 `patch*` 从主刀剔除，主集不变、附表 A 的三行致谢项与第 13 章全部要重述。
4. **`more` 算不算程度形容词**：本篇按"不算"处理（185/203 是导航用语）。若按词表并集算，六形并集 ∩主 6,910 这个数会被误读成主集覆盖率。
5. **宾语抽取只认"形容词＋名词"**：因此 `by removing X` 的 297 段与 `with improvements to X` 的 22 段被排除在 75 形态之外。若改成"介词后整段名词短语"，75 会变成更大的数，第 7.4 节的集中度要重算。
6. **中文层窗口**：179 页转储切窗后 178 页可用（排除 `sec2727afbbc.txt`）。若窗口规则变化，附表 E 的 15 个全零要逐个重测——"全零"是最容易被窗口规则影响的读数。

## 19.5 与其它措辞层篇目的位置

| 篇 | 层 | 主集 | 字段方向（Impact∶Description） |
|---|---|---:|---|
| #140 | 威胁主体称谓 | 3,268 | **只长 Impact**（3,268∶**0**） |
| **#141** | **修复表述** | **7,247** | **只长 Description**（**83**∶7,233） |
| #139 | 限制／特权／授权／配置载体 | 2,044 | 均衡（1,128∶1,224） |
| #138 | 用户数据／隐私对象 | 2,102 | 偏 Impact（1,975∶397） |
| #137 | 无线／近场／网络通道 | 428 | 极偏 Impact（422∶**14**） |
| #136 | 认证／凭据／授权载体 | 600 | 刀间不对称（刀一 100∶0、刀三 59∶340） |

**#140 与 #141 是同一枚硬币的两面**：那篇抓到"谁在攻击"的段全在 Impact，这篇抓到"苹果怎么修的"的段全在 Description。两篇的并集与交集见附表 A 的外部集对照组与第 14 章。

# 附表 A 判据词族普查（110 行）

| 组 | 判据 | 并集段 | 页 | 名 | ∩主集 | ∩R | ∩E | ∩WO |
|---|---|---:|---:|---:|---:|---:|---:|---:|
| 本体词（本篇主刀） | `谓语全族（本篇主刀·三字段）` | 7261 | 256 | 394 | 7247 | 820 | 108 | 178 |
| 本体词（本篇主刀） | `谓语全族（本篇主刀·两段并集）` | 7247 | 256 | 394 | 7247 | 820 | 108 | 178 |
| 本体词（本篇主刀） | `谓语全族·Impact 侧` | 83 | 58 | 14 | 83 | 34 | 13 | 0 |
| 本体词（本篇主刀） | `谓语全族·Description 侧` | 7233 | 256 | 393 | 7233 | 820 | 108 | 178 |
| 本体词（本篇主刀） | `addressed（des 侧）` | 7214 | 256 | 393 | 7214 | 819 | 108 | 178 |
| 本体词（本篇主刀） | `addressed（imp 侧）` | 25 | 14 | 6 | 25 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `address 原形（imp 侧·地址义）` | 39 | 36 | 5 | 39 | 33 | 0 | 0 |
| 本体词（本篇主刀） | `resolved（des 侧）` | 19 | 17 | 8 | 19 | 1 | 0 | 0 |
| 本体词（本篇主刀） | `fix 原形（imp 侧）` | 13 | 10 | 2 | 13 | 0 | 13 | 0 |
| 本体词（本篇主刀） | `correct*（imp 侧）` | 5 | 5 | 2 | 5 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `correct*（des 侧）` | 24 | 18 | 5 | 24 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `resolv*（des 侧）` | 19 | 17 | 8 | 19 | 1 | 0 | 0 |
| 本体词（本篇主刀） | `mitigat*（三字段）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `patch*（imp+des 两段）` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `imp 以 addressed 裸收尾` | 21 | 13 | 4 | 21 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `des 以 addressed+标点收尾` | 7 | 7 | 2 | 7 | 6 | 0 | 0 |
| 本体词（本篇主刀） | `des 用 addressed to 目的状语` | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `patch*（致谢侧）` | 416 | 98 | 67 | 402 | 1 | 2 | 0 |
| 本体词（本篇主刀） | `patch1t 句柄（致谢侧）` | 405 | 87 | 62 | 391 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `patch1t 句柄（全树致谢）` | 405 | 87 | 62 | 391 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `致谢侧主刀独有（不在主集）` | 14 | 10 | 5 | 0 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `去 patch 后主集（反事实）` | 7247 | 256 | 394 | 7247 | 820 | 108 | 178 |
| 本体词（本篇主刀） | `去 patch 后致谢（反事实）` | 1 | 1 | 1 | 1 | 0 | 1 | 0 |
| 本体词（本篇主刀） | `程度 improved（des 侧）` | 6037 | 255 | 344 | 6037 | 742 | 104 | 130 |
| 本体词（本篇主刀） | `程度 additional（des 侧）` | 850 | 150 | 125 | 850 | 54 | 0 | 40 |
| 本体词（本篇主刀） | `程度 more（des 侧）` | 203 | 110 | 26 | 18 | 23 | 0 | 0 |
| 本体词（本篇主刀） | `程度 new（des 侧）` | 5 | 5 | 1 | 5 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `程度六形并集（des 侧）` | 7095 | 256 | 382 | 6910 | 819 | 104 | 170 |
| 本体词（本篇主刀） | `介词档 with（addressed 后）` | 6588 | 254 | 363 | 6588 | 735 | 104 | 163 |
| 本体词（本篇主刀） | `介词档 by（addressed 后）` | 371 | 136 | 80 | 371 | 31 | 4 | 4 |
| 本体词（本篇主刀） | `介词档 through／via（addressed 后）` | 247 | 129 | 48 | 247 | 47 | 0 | 11 |
| 本体词（本篇主刀） | `介词档 零介词（addressed 后无方式）` | 8 | 8 | 3 | 8 | 6 | 0 | 0 |
| 本体词（本篇主刀） | `主语 This issue 打头（des）` | 1049 | 188 | 160 | 1049 | 76 | 13 | 30 |
| 本体词（本篇主刀） | `主语 The issue 打头（des）` | 1561 | 190 | 152 | 1561 | 214 | 7 | 33 |
| 本体词（本篇主刀） | `主语 A／An… 打头（des）` | 4608 | 244 | 315 | 4608 | 530 | 88 | 115 |
| 本体词（本篇主刀） | `主语 其余打头（des）` | 15 | 15 | 4 | 15 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `复数系词 were addressed（des）` | 15 | 15 | 4 | 15 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `Multiple 打头（des）` | 15 | 15 | 4 | 15 | 0 | 0 | 0 |
| 本体词（本篇主刀） | `开源声明句 open source（des）` | 199 | 108 | 24 | 14 | 23 | 0 | 0 |
| 本体词（本篇主刀） | `残差集（有 des 无谓语）` | 187 | 108 | 25 | 0 | 23 | 0 | 0 |
| 本体词（本篇主刀） | `标准免责句（致谢侧）` | 11 | 11 | 6 | 11 | 1 | 2 | 0 |
| 句级刀（17 把） | `零方式残句（addressed 后直接收尾）` | 28 | 18 | 6 | 28 | 6 | 0 | 0 |
| 句级刀（17 把） | `目的状语句（addressed to…不写方式）` | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 句级刀（17 把） | `边界与长度宾语类` | 1034 | 188 | 79 | 1034 | 94 | 24 | 0 |
| 句级刀（17 把） | `输入与解析宾语类` | 944 | 160 | 114 | 944 | 149 | 1 | 0 |
| 句级刀（17 把） | `内存与生命周期宾语类` | 2187 | 202 | 115 | 2187 | 180 | 60 | 0 |
| 句级刀（17 把） | `脱敏与隐私宾语类` | 575 | 145 | 97 | 575 | 5 | 1 | 8 |
| 句级刀（17 把） | `状态类宾语` | 862 | 185 | 119 | 862 | 127 | 14 | 45 |
| 句级刀（17 把） | `限制与访问控制宾语类` | 1362 | 175 | 159 | 1362 | 94 | 8 | 64 |
| 句级刀（17 把） | `认证与凭据宾语类` | 154 | 85 | 41 | 154 | 29 | 4 | 48 |
| 句级刀（17 把） | `交互与同意宾语类` | 100 | 68 | 21 | 100 | 23 | 0 | 73 |
| 句级刀（17 把） | `锁与配对状态宾语类` | 265 | 117 | 54 | 265 | 40 | 13 | 9 |
| 句级刀（17 把） | `逻辑与代码宾语类` | 614 | 167 | 85 | 614 | 103 | 9 | 24 |
| 句级刀（17 把） | `校验与检查宾语类` | 3415 | 242 | 274 | 3415 | 427 | 70 | 63 |
| 句级刀（17 把） | `界面与绘制宾语类` | 411 | 149 | 36 | 411 | 51 | 7 | 2 |
| 句级刀（17 把） | `泛化处理与管理宾语类` | 2500 | 200 | 209 | 2500 | 274 | 33 | 74 |
| 句级刀（17 把） | `其他带程度形容词宾语类` | 6905 | 255 | 375 | 6905 | 796 | 104 | 170 |
| 句级刀（17 把） | `其余句式（兜底）` | 7247 | 256 | 394 | 7247 | 820 | 108 | 178 |
| 机制名与检测词对照 | `symlink／symbolic link 宾语` | 126 | 61 | 34 | 126 | 1 | 0 | 3 |
| 机制名与检测词对照 | `environment variables 宾语` | 26 | 26 | 7 | 26 | 0 | 0 | 0 |
| 机制名与检测词对照 | `caches 宾语` | 29 | 24 | 9 | 29 | 14 | 0 | 7 |
| 机制名与检测词对照 | `temporary files 宾语` | 33 | 22 | 10 | 33 | 0 | 0 | 0 |
| 机制名与检测词对照 | `sensitive information 宾语` | 110 | 71 | 29 | 102 | 1 | 1 | 4 |
| 机制名与检测词对照 | `log entries 宾语` | 114 | 68 | 22 | 114 | 0 | 0 | 0 |
| 机制名与检测词对照 | `security origins 宾语` | 13 | 13 | 1 | 13 | 6 | 0 | 0 |
| 机制名与检测词对照 | `pasteboard 宾语` | 8 | 7 | 2 | 8 | 0 | 0 | 0 |
| 机制名与检测词对照 | `handling of protocols 宾语` | 6 | 6 | 1 | 6 | 6 | 0 | 0 |
| 机制名与检测词对照 | `audioworklet 语境` | 4 | 4 | 1 | 4 | 0 | 0 | 0 |
| 机制名与检测词对照 | `updating to curl 版本更新句` | 2 | 2 | 1 | 2 | 0 | 0 | 0 |
| 机制名与检测词对照 | `updating to OpenSSH 版本更新句` | 1 | 1 | 1 | 1 | 0 | 0 | 0 |
| 机制名与检测词对照 | `WebKit 组件名` | 1020 | 155 | 10 | 1010 | 154 | 46 | 12 |
| 机制名与检测词对照 | `内核组件名 Kernel` | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 机制名与检测词对照 | `bypass 词族（#126 对照）` | 301 | 114 | 58 | 300 | 42 | 20 | 0 |
| 机制名与检测词对照 | `without prompting／permission（#140 对照）` | 28 | 28 | 6 | 28 | 0 | 0 | 28 |
| 修复套话句（全篇共用 15 项） | `The issue was addressed with improved checks.` | 514 | 138 | 82 | 514 | 68 | 5 | 20 |
| 修复套话句（全篇共用 15 项） | `improved bounds checking` | 699 | 154 | 63 | 699 | 81 | 15 | 0 |
| 修复套话句（全篇共用 15 项） | `improved input validation` | 641 | 151 | 79 | 641 | 140 | 1 | 0 |
| 修复套话句（全篇共用 15 项） | `improved validation` | 367 | 101 | 76 | 367 | 13 | 23 | 15 |
| 修复套话句（全篇共用 15 项） | `additional checks` | 34 | 28 | 12 | 34 | 0 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `additional validation` | 112 | 65 | 20 | 112 | 8 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `improved checks to prevent unauthorized actions` | 16 | 16 | 4 | 16 | 0 | 8 | 0 |
| 修复套话句（全篇共用 15 项） | `addressed with（全形）` | 6589 | 254 | 364 | 6589 | 735 | 104 | 163 |
| 修复套话句（全篇共用 15 项） | `addressed by` | 371 | 136 | 80 | 371 | 31 | 4 | 4 |
| 修复套话句（全篇共用 15 项） | `addressed through` | 247 | 129 | 48 | 247 | 47 | 0 | 11 |
| 修复套话句（全篇共用 15 项） | `validation 词族` | 1341 | 179 | 168 | 1341 | 184 | 24 | 15 |
| 修复套话句（全篇共用 15 项） | `check(s)／checking 词族` | 2017 | 227 | 201 | 2015 | 243 | 46 | 48 |
| 修复套话句（全篇共用 15 项） | `sanitiz* 词族` | 93 | 54 | 21 | 93 | 3 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `enforce* 词族` | 53 | 34 | 4 | 53 | 0 | 0 | 0 |
| 修复套话句（全篇共用 15 项） | `restrict* 词族` | 793 | 153 | 109 | 791 | 75 | 4 | 33 |
| 外部集对照（12 套，含 #122–#126） | `远程可达 R` | 843 | 178 | 72 | 820 | 843 | 4 | 27 |
| 外部集对照（12 套，含 #122–#126） | `without 词界集 WO` | 178 | 97 | 37 | 178 | 27 | 0 | 178 |
| 外部集对照（12 套，含 #122–#126） | `exploit* 集 E` | 108 | 76 | 12 | 108 | 4 | 108 | 0 |
| 外部集对照（12 套，含 #122–#126） | `user 词界集 UU` | 1627 | 175 | 200 | 1624 | 130 | 0 | 105 |
| 外部集对照（12 套，含 #122–#126） | `动名词开头集 G` | 2179 | 224 | 98 | 2070 | 108 | 53 | 0 |
| 外部集对照（12 套，含 #122–#126） | `非处理/解析类 HI` | 273 | 102 | 35 | 271 | 108 | 0 | 0 |
| 外部集对照（12 套，含 #122–#126） | `≥2 后果类 M2` | 185 | 82 | 35 | 166 | 23 | 0 | 0 |
| 外部集对照（12 套，含 #122–#126） | `race condition 集 RC（#122）` | 232 | 95 | 40 | 232 | 14 | 0 | 3 |
| 外部集对照（12 套，含 #122–#126） | `logic 集 L（#123）` | 550 | 162 | 81 | 550 | 99 | 9 | 24 |
| 外部集对照（12 套，含 #122–#126） | `拒绝服务集 Ds（#124）` | 470 | 155 | 62 | 436 | 164 | 0 | 0 |
| 外部集对照（12 套，含 #122–#126） | `泄露集 X（#125）` | 427 | 141 | 54 | 418 | 11 | 2 | 0 |
| 外部集对照（12 套，含 #122–#126） | `绕过集 BP（#126）` | 301 | 114 | 58 | 300 | 42 | 20 | 0 |
| 后果强度八类 | `ACE 类` | 336 | 120 | 43 | 317 | 36 | 31 | 0 |
| 后果强度八类 | `BYP 类` | 301 | 114 | 58 | 300 | 42 | 20 | 0 |
| 后果强度八类 | `DIS 类` | 603 | 159 | 78 | 591 | 74 | 2 | 0 |
| 后果强度八类 | `DOS 类` | 2014 | 176 | 130 | 1946 | 343 | 0 | 0 |
| 后果强度八类 | `FULL 类` | 63 | 43 | 18 | 63 | 7 | 0 | 0 |
| 后果强度八类 | `MEM 类` | 390 | 127 | 31 | 360 | 3 | 44 | 0 |
| 后果强度八类 | `PRI 类` | 124 | 62 | 30 | 123 | 0 | 8 | 0 |
| 后果强度八类 | `SBX 类` | 475 | 120 | 74 | 475 | 40 | 8 | 6 |
| **合计** | 109 项（并集全零 3 项、非零 106 项） | 7247 | — | — | 7247 | 820 | 108 | 178 |

# 附表 B 含主刀整句 × 归桶 × 字段 × 外部集（234 行）

| distinct 整句 | 归入桶 | 句次 | Impact 句 | Desc 句 | 页 | ∩R | ∩E | ∩M2 | ∩L | ∩RC |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| The issue was addressed with improved memory handling. | 内存与生命周期宾语类 | 644 | 0 | 644 | 165 | 56 | 2 | 22 | 0 | 0 |
| The issue was addressed with improved checks. | 校验与检查宾语类 | 514 | 0 | 514 | 138 | 68 | 5 | 14 | 1 | 0 |
| A permissions issue was addressed with additional restrictions. | 限制与访问控制宾语类 | 361 | 0 | 361 | 116 | 10 | 0 | 1 | 0 | 0 |
| An out-of-bounds write issue was addressed with improved bounds checking. | 边界与长度宾语类 | 258 | 0 | 258 | 66 | 36 | 10 | 17 | 0 | 0 |
| This issue was addressed through improved state management. | 状态类宾语 | 237 | 0 | 237 | 127 | 47 | 0 | 5 | 0 | 0 |
| A logic issue was addressed with improved checks. | 逻辑与代码宾语类 | 226 | 0 | 226 | 114 | 54 | 0 | 3 | 226 | 0 |
| A use-after-free issue was addressed with improved memory management. | 内存与生命周期宾语类 | 189 | 0 | 189 | 98 | 13 | 9 | 1 | 0 | 0 |
| An out-of-bounds read was addressed with improved bounds checking. | 边界与长度宾语类 | 169 | 0 | 169 | 71 | 19 | 0 | 6 | 0 | 0 |
| An authorization issue was addressed with improved state management. | 状态类宾语 | 159 | 0 | 159 | 61 | 13 | 4 | 0 | 0 | 0 |
| An out-of-bounds access issue was addressed with improved bounds checking. | 边界与长度宾语类 | 143 | 0 | 143 | 99 | 4 | 0 | 5 | 0 | 0 |
| An integer overflow was addressed with improved input validation. | 输入与解析宾语类 | 136 | 0 | 136 | 57 | 25 | 0 | 9 | 0 | 0 |
| This issue was addressed with improved checks. | 校验与检查宾语类 | 131 | 0 | 131 | 93 | 3 | 9 | 4 | 9 | 0 |
| A use after free issue was addressed with improved memory management. | 内存与生命周期宾语类 | 127 | 0 | 127 | 49 | 20 | 8 | 0 | 0 | 0 |
| A path handling issue was addressed with improved validation. | 校验与检查宾语类 | 110 | 0 | 110 | 62 | 0 | 0 | 0 | 0 | 0 |
| A logging issue was addressed with improved data redaction. | 脱敏与隐私宾语类 | 104 | 0 | 104 | 67 | 0 | 0 | 0 | 0 | 0 |
| A buffer overflow was addressed with improved bounds checking. | 边界与长度宾语类 | 102 | 0 | 102 | 44 | 22 | 0 | 7 | 0 | 0 |
| This issue was addressed with additional entitlement checks. | 限制与访问控制宾语类 | 99 | 0 | 99 | 60 | 0 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed with improved private data redaction for log entries. | 脱敏与隐私宾语类 | 94 | 0 | 94 | 60 | 0 | 0 | 0 | 0 | 0 |
| A race condition was addressed with improved state handling. | 状态类宾语 | 94 | 0 | 94 | 48 | 0 | 0 | 0 | 0 | 94 |
| This issue was addressed with improved redaction of sensitive information. | 脱敏与隐私宾语类 | 92 | 0 | 92 | 59 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved bounds checks. | 边界与长度宾语类 | 89 | 0 | 89 | 65 | 0 | 0 | 3 | 0 | 0 |
| This issue was addressed by removing the vulnerable code. | 其余句式（兜底） | 89 | 0 | 89 | 59 | 8 | 4 | 0 | 0 | 0 |
| A race condition was addressed with additional validation. | 校验与检查宾语类 | 83 | 0 | 83 | 58 | 0 | 0 | 0 | 0 | 83 |
| An out-of-bounds write issue was addressed with improved input validation. | 边界与长度宾语类 | 83 | 0 | 83 | 64 | 6 | 0 | 7 | 0 | 0 |
| An out-of-bounds read was addressed with improved input validation. | 边界与长度宾语类 | 77 | 0 | 77 | 56 | 0 | 1 | 19 | 0 | 0 |
| A parsing issue in the handling of directory paths was addressed with improved path validation. | 输入与解析宾语类 | 73 | 0 | 73 | 45 | 0 | 0 | 0 | 0 | 0 |
| A logic issue was addressed with improved state management. | 状态类宾语 | 71 | 0 | 71 | 49 | 12 | 0 | 0 | 71 | 0 |
| This issue was addressed with improved validation of symlinks. | 校验与检查宾语类 | 66 | 0 | 66 | 36 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved input validation. | 输入与解析宾语类 | 65 | 0 | 65 | 49 | 19 | 0 | 0 | 0 | 0 |
| A type confusion issue was addressed with improved checks. | 校验与检查宾语类 | 63 | 0 | 63 | 50 | 25 | 9 | 0 | 0 | 0 |
| A type confusion issue was addressed with improved memory handling. | 内存与生命周期宾语类 | 59 | 0 | 59 | 47 | 0 | 0 | 0 | 0 | 0 |
| An out-of-bounds read issue was addressed with improved input validation. | 边界与长度宾语类 | 59 | 0 | 59 | 41 | 7 | 0 | 8 | 0 | 0 |
| An authentication issue was addressed with improved state management. | 状态类宾语 | 57 | 0 | 57 | 44 | 24 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved handling of symlinks. | 泛化处理与管理宾语类 | 50 | 0 | 50 | 34 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved input sanitization. | 输入与解析宾语类 | 49 | 0 | 49 | 24 | 0 | 0 | 0 | 0 | 0 |
| A buffer overflow issue was addressed with improved memory handling. | 内存与生命周期宾语类 | 48 | 0 | 48 | 42 | 0 | 0 | 0 | 0 | 0 |
| An access issue was addressed with additional sandbox restrictions. | 限制与访问控制宾语类 | 48 | 0 | 48 | 36 | 13 | 0 | 2 | 0 | 0 |
| A logic issue was addressed with improved restrictions. | 限制与访问控制宾语类 | 47 | 0 | 47 | 37 | 3 | 0 | 0 | 47 | 0 |
| A memory corruption issue was addressed with improved memory handling. | 内存与生命周期宾语类 | 45 | 0 | 45 | 35 | 0 | 0 | 0 | 0 | 0 |
| A logic issue was addressed with improved validation. | 逻辑与代码宾语类 | 44 | 0 | 44 | 35 | 4 | 0 | 0 | 44 | 0 |
| A double free issue was addressed with improved memory management. | 内存与生命周期宾语类 | 43 | 0 | 43 | 31 | 8 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved data protection. | 其他带程度形容词宾语类 | 43 | 0 | 43 | 36 | 0 | 0 | 0 | 0 | 0 |
| A memory initialization issue was addressed with improved memory handling. | 内存与生命周期宾语类 | 42 | 0 | 42 | 31 | 3 | 0 | 0 | 0 | 0 |
| A permissions issue was addressed with improved validation. | 限制与访问控制宾语类 | 38 | 0 | 38 | 27 | 0 | 0 | 0 | 0 | 0 |
| A permissions issue was addressed with additional sandbox restrictions. | 限制与访问控制宾语类 | 37 | 0 | 37 | 23 | 6 | 0 | 5 | 0 | 0 |
| A denial-of-service issue was addressed with improved input validation. | 输入与解析宾语类 | 33 | 0 | 33 | 31 | 31 | 0 | 0 | 0 | 0 |
| A race condition was addressed with improved locking. | 锁与配对状态宾语类 | 33 | 0 | 33 | 31 | 14 | 0 | 0 | 0 | 33 |
| The issue was addressed by adding additional logic. | 逻辑与代码宾语类 | 33 | 0 | 33 | 28 | 11 | 0 | 2 | 33 | 0 |
| A logic issue was addressed with improved file handling. | 逻辑与代码宾语类 | 32 | 0 | 32 | 23 | 0 | 0 | 0 | 32 | 0 |
| A permissions issue was addressed by removing the vulnerable code. | 限制与访问控制宾语类 | 32 | 0 | 32 | 28 | 0 | 0 | 0 | 0 | 0 |
| A null pointer dereference was addressed with improved input validation. | 输入与解析宾语类 | 31 | 0 | 31 | 30 | 22 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed by removing sensitive data. | 脱敏与隐私宾语类 | 31 | 0 | 31 | 29 | 0 | 0 | 0 | 0 | 0 |
| A path handling issue was addressed with improved logic. | 逻辑与代码宾语类 | 30 | 0 | 30 | 25 | 7 | 0 | 0 | 30 | 0 |
| An inconsistent user interface issue was addressed with improved state management. | 状态类宾语 | 29 | 0 | 29 | 26 | 14 | 0 | 0 | 0 | 0 |
| An information leakage was addressed with additional validation. | 校验与检查宾语类 | 29 | 0 | 29 | 26 | 8 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved handling of caches. | 泛化处理与管理宾语类 | 29 | 0 | 29 | 24 | 14 | 0 | 0 | 0 | 0 |
| This issue was addressed by restricting options offered on a locked device. | 锁与配对状态宾语类 | 29 | 0 | 29 | 20 | 0 | 0 | 0 | 0 | 0 |
| A buffer overflow was addressed with improved size validation. | 内存与生命周期宾语类 | 28 | 0 | 28 | 23 | 0 | 0 | 1 | 0 | 0 |
| An access issue was addressed with improved access restrictions. | 限制与访问控制宾语类 | 28 | 0 | 28 | 26 | 7 | 0 | 0 | 0 | 0 |
| A file quarantine bypass was addressed with additional checks. | 校验与检查宾语类 | 27 | 0 | 27 | 24 | 0 | 0 | 0 | 0 | 0 |
| A memory corruption issue was addressed with improved validation. | 内存与生命周期宾语类 | 27 | 0 | 27 | 22 | 0 | 23 | 0 | 0 | 0 |
| A memory corruption vulnerability was addressed with improved locking. | 内存与生命周期宾语类 | 27 | 0 | 27 | 22 | 0 | 1 | 0 | 0 | 0 |
| A downgrade issue affecting Intel-based Mac computers was addressed with additional code-signing restrictions. | 限制与访问控制宾语类 | 25 | 0 | 25 | 19 | 0 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed with improved handling of temporary files. | 脱敏与隐私宾语类 | 25 | 0 | 25 | 21 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed by adding an additional prompt for user consent. | 交互与同意宾语类 | 25 | 0 | 25 | 21 | 6 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved access restrictions. | 限制与访问控制宾语类 | 25 | 0 | 25 | 16 | 0 | 0 | 0 | 0 | 0 |
| A correctness issue was addressed with improved checks. | 校验与检查宾语类 | 24 | 0 | 24 | 18 | 0 | 0 | 0 | 0 | 0 |
| An information disclosure issue was addressed with improved state management. | 状态类宾语 | 24 | 0 | 24 | 12 | 10 | 0 | 0 | 0 | 0 |
| A downgrade issue was addressed with additional code-signing restrictions. | 限制与访问控制宾语类 | 23 | 0 | 23 | 18 | 0 | 0 | 0 | 0 | 0 |
| A memory corruption issue was addressed with improved state management. | 内存与生命周期宾语类 | 23 | 0 | 23 | 23 | 0 | 5 | 0 | 0 | 0 |
| A validation issue was addressed with improved input sanitization. | 输入与解析宾语类 | 23 | 0 | 23 | 18 | 3 | 0 | 3 | 0 | 0 |
| The issue was addressed with improved UI. | 其他带程度形容词宾语类 | 23 | 0 | 23 | 23 | 20 | 0 | 0 | 0 | 0 |
| A memory corruption issue was addressed with improved input validation. | 输入与解析宾语类 | 22 | 0 | 22 | 18 | 3 | 0 | 0 | 0 | 0 |
| A stack overflow was addressed with improved input validation. | 输入与解析宾语类 | 22 | 0 | 22 | 22 | 6 | 0 | 0 | 0 | 0 |
| A validation issue was addressed with improved logic. | 逻辑与代码宾语类 | 22 | 0 | 22 | 22 | 8 | 0 | 0 | 22 | 0 |
| An input validation issue was addressed | 零方式残句（addressed 后直接收尾） | 21 | 21 | 0 | 13 | 0 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed with improved checks. | 脱敏与隐私宾语类 | 20 | 0 | 20 | 17 | 0 | 0 | 0 | 0 | 0 |
| An information disclosure issue was addressed with improved private data redaction for log entries. | 脱敏与隐私宾语类 | 20 | 0 | 20 | 13 | 0 | 0 | 0 | 0 | 0 |
| A logic error was addressed with improved error handling. | 逻辑与代码宾语类 | 18 | 0 | 18 | 18 | 0 | 0 | 0 | 18 | 0 |
| An authorization issue was addressed with improved access control. | 限制与访问控制宾语类 | 18 | 0 | 18 | 8 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with additional permissions checks. | 限制与访问控制宾语类 | 18 | 0 | 18 | 18 | 0 | 0 | 0 | 0 | 0 |
| Visiting a malicious website may lead to address bar spoofing | 其余句式（兜底） | 18 | 18 | 0 | 18 | 18 | 0 | 0 | 5 | 0 |
| A cookie management issue was addressed with improved state management. | 状态类宾语 | 17 | 0 | 17 | 17 | 6 | 5 | 0 | 0 | 0 |
| A memory corruption issue was addressed with improved bounds checking. | 边界与长度宾语类 | 17 | 0 | 17 | 17 | 0 | 5 | 0 | 0 | 0 |
| An injection issue was addressed with improved validation. | 校验与检查宾语类 | 17 | 0 | 17 | 17 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved input validation. | 输入与解析宾语类 | 17 | 0 | 17 | 15 | 6 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved memory handling. | 内存与生命周期宾语类 | 17 | 0 | 17 | 16 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved permissions checking. | 限制与访问控制宾语类 | 17 | 0 | 17 | 15 | 4 | 0 | 0 | 0 | 0 |
| A permissions issue was addressed with improved path validation. | 限制与访问控制宾语类 | 16 | 0 | 16 | 8 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved validation. | 校验与检查宾语类 | 16 | 0 | 16 | 16 | 0 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed with improved handling of user preferences. | 脱敏与隐私宾语类 | 14 | 0 | 14 | 14 | 4 | 0 | 0 | 0 | 0 |
| An information disclosure issue was addressed with improved privacy controls. | 脱敏与隐私宾语类 | 14 | 0 | 14 | 13 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved UI handling. | 泛化处理与管理宾语类 | 14 | 0 | 14 | 14 | 14 | 0 | 0 | 0 | 0 |
| A configuration issue was addressed with additional restrictions. | 限制与访问控制宾语类 | 13 | 0 | 13 | 13 | 0 | 0 | 0 | 0 | 0 |
| A denial-of-service issue was addressed with improved validation. | 校验与检查宾语类 | 13 | 0 | 13 | 13 | 9 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed by removing the vulnerable code. | 脱敏与隐私宾语类 | 13 | 0 | 13 | 12 | 0 | 0 | 0 | 0 | 0 |
| An information disclosure issue was addressed by removing the vulnerable code. | 其余句式（兜底） | 13 | 0 | 13 | 13 | 0 | 0 | 0 | 0 | 0 |
| An input validation issue was addressed with improved input validation. | 输入与解析宾语类 | 13 | 0 | 13 | 9 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved authentication. | 认证与凭据宾语类 | 13 | 0 | 13 | 13 | 0 | 0 | 0 | 0 | 0 |
| A file access issue was addressed with improved input validation. | 输入与解析宾语类 | 12 | 0 | 12 | 12 | 0 | 0 | 0 | 0 | 0 |
| A type confusion issue was addressed with improved state handling. | 状态类宾语 | 12 | 0 | 12 | 12 | 0 | 0 | 0 | 0 | 0 |
| An authorization issue was addressed with improved validation. | 校验与检查宾语类 | 12 | 0 | 12 | 12 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved restriction of data container access. | 限制与访问控制宾语类 | 12 | 0 | 12 | 10 | 0 | 0 | 0 | 0 | 0 |
| A certificate validation issue was addressed with improved certificate validation. | 校验与检查宾语类 | 11 | 0 | 11 | 8 | 3 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed with improved handling of files. | 脱敏与隐私宾语类 | 11 | 0 | 11 | 11 | 0 | 0 | 0 | 0 | 0 |
| A race condition was addressed with improved state management. | 状态类宾语 | 11 | 0 | 11 | 10 | 0 | 0 | 8 | 0 | 11 |
| An out-of-bounds write issue was addressed by removing the vulnerable code. | 边界与长度宾语类 | 11 | 0 | 11 | 11 | 0 | 0 | 8 | 0 | 0 |
| This issue was addressed with improved checks | 校验与检查宾语类 | 11 | 0 | 11 | 11 | 0 | 0 | 0 | 0 | 0 |
| A heap buffer overflow was addressed with improved bounds checking. | 边界与长度宾语类 | 10 | 0 | 10 | 7 | 0 | 0 | 0 | 0 | 0 |
| An information disclosure issue was addressed with improved memory management. | 内存与生命周期宾语类 | 10 | 0 | 10 | 10 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed by using HTTPS when sending information over the network. | 其余句式（兜底） | 10 | 0 | 10 | 9 | 5 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved authentication. | 认证与凭据宾语类 | 10 | 0 | 10 | 8 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved state management. | 状态类宾语 | 10 | 0 | 10 | 10 | 0 | 0 | 0 | 0 | 0 |
| A buffer overflow was addressed with improved input validation. | 输入与解析宾语类 | 9 | 0 | 9 | 9 | 1 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed by moving sensitive data. | 脱敏与隐私宾语类 | 9 | 0 | 9 | 9 | 0 | 0 | 0 | 0 | 0 |
| A race condition was addressed with improved handling of symbolic links. | 泛化处理与管理宾语类 | 9 | 0 | 9 | 9 | 0 | 0 | 6 | 0 | 9 |
| The issue was resolved by sanitizing logging | 校验与检查宾语类 | 9 | 0 | 9 | 9 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved entitlements. | 限制与访问控制宾语类 | 9 | 0 | 9 | 9 | 0 | 0 | 0 | 0 | 0 |
| A memory corruption issue was addressed with improved lock state checking. | 内存与生命周期宾语类 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 | 0 |
| An integer overflow was addressed by adopting 64-bit timestamps. | 内存与生命周期宾语类 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 | 0 |
| An integer overflow was addressed through improved input validation. | 输入与解析宾语类 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 | 0 |
| An issue was addressed with improved handling of temporary files. | 泛化处理与管理宾语类 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 | 0 |
| An issue was addressed with improved validation of environment variables. | 校验与检查宾语类 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 | 0 |
| An out-of-bounds write issue was addressed with improved checks to prevent unauthorized actions. | 边界与长度宾语类 | 8 | 0 | 8 | 8 | 0 | 8 | 0 | 0 | 0 |
| An uninitialized memory issue was addressed with improved memory initialization. | 内存与生命周期宾语类 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 | 0 |
| This is a supplementary fix for an attack that was blocked in iOS 17.2. (Apple is aware of a report that this issue may have been exploited in an extremely sophisticated attack against specific targeted individuals on versions of iOS before iOS 17.2.) | 锁与配对状态宾语类 | 8 | 8 | 0 | 8 | 0 | 8 | 0 | 0 | 0 |
| This issue was addressed with improved checks to prevent unauthorized actions. | 校验与检查宾语类 | 8 | 0 | 8 | 8 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved validation of the process entitlement. | 限制与访问控制宾语类 | 8 | 0 | 8 | 6 | 0 | 0 | 0 | 0 | 0 |
| A cross-origin issue was addressed with improved tracking of security origins. | 其他带程度形容词宾语类 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 | 0 |
| A library injection issue was addressed with additional restrictions. | 限制与访问控制宾语类 | 7 | 0 | 7 | 6 | 0 | 0 | 0 | 0 | 0 |
| A mail header parsing issue was addressed with improved checks. | 输入与解析宾语类 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 | 0 |
| A permissions issue was addressed by removing vulnerable code and adding additional checks. | 限制与访问控制宾语类 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed by not logging contents of text fields. | 脱敏与隐私宾语类 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 | 0 |
| A timing side-channel issue was addressed with improvements to constant-time computation in cryptographic functions. | 其余句式（兜底） | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 | 0 |
| A website may be able to determine a user's IP address with Private Relay turned on | 其余句式（兜底） | 7 | 7 | 0 | 4 | 7 | 0 | 0 | 0 | 0 |
| An input validation issue was addressed. | 零方式残句（addressed 后直接收尾） | 7 | 0 | 7 | 7 | 6 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved environment sanitization. | 校验与检查宾语类 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved handling of floats. | 输入与解析宾语类 | 7 | 0 | 7 | 7 | 0 | 0 | 0 | 0 | 0 |
| A cross-origin issue in the Navigation API was addressed with improved input validation. | 输入与解析宾语类 | 6 | 0 | 6 | 6 | 0 | 0 | 0 | 0 | 0 |
| A custom URL scheme handling issue was addressed with improved input validation. | 输入与解析宾语类 | 6 | 0 | 6 | 6 | 1 | 0 | 0 | 0 | 0 |
| A lock screen issue was addressed with improved state management. | 状态类宾语 | 6 | 0 | 6 | 6 | 0 | 0 | 0 | 0 | 0 |
| A path deletion vulnerability was addressed by preventing vulnerable code from running with privileges. | 其余句式（兜底） | 6 | 0 | 6 | 3 | 0 | 0 | 0 | 0 | 0 |
| A path traversal issue was addressed with improved input validation. | 输入与解析宾语类 | 6 | 0 | 6 | 6 | 0 | 0 | 0 | 0 | 0 |
| A permissions issue was addressed with improved state management. | 状态类宾语 | 6 | 0 | 6 | 3 | 1 | 0 | 0 | 0 | 0 |
| An attacker may be able to track users through their IP address | 其余句式（兜底） | 6 | 6 | 0 | 6 | 0 | 0 | 0 | 0 | 0 |
| An integer underflow was addressed with improved input validation. | 输入与解析宾语类 | 6 | 0 | 6 | 3 | 3 | 0 | 0 | 0 | 0 |
| An issue in the handling of URL protocols was addressed with improved logic. | 逻辑与代码宾语类 | 6 | 0 | 6 | 6 | 0 | 0 | 0 | 6 | 0 |
| The issue was addressed with improved handling of protocols. | 泛化处理与管理宾语类 | 6 | 0 | 6 | 6 | 6 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved file handling. | 泛化处理与管理宾语类 | 6 | 0 | 6 | 6 | 0 | 0 | 0 | 0 | 0 |
| This was addressed with improved tracking of security origins. | 其他带程度形容词宾语类 | 6 | 0 | 6 | 6 | 6 | 0 | 0 | 0 | 0 |
| A path traversal issue was addressed with improved path validation. | 校验与检查宾语类 | 5 | 0 | 5 | 5 | 3 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed with improved access restrictions. | 脱敏与隐私宾语类 | 5 | 0 | 5 | 5 | 0 | 0 | 0 | 0 | 0 |
| A reachable assertion was addressed with improved input validation. | 输入与解析宾语类 | 5 | 0 | 5 | 5 | 5 | 0 | 0 | 0 | 0 |
| A resource exhaustion issue was addressed with improved input validation. | 输入与解析宾语类 | 5 | 0 | 5 | 5 | 5 | 0 | 0 | 0 | 0 |
| An injection issue was addressed with improved input validation. | 输入与解析宾语类 | 5 | 0 | 5 | 5 | 0 | 0 | 0 | 0 | 0 |
| An out-of-bounds write was addressed with improved input validation. | 边界与长度宾语类 | 5 | 0 | 5 | 5 | 0 | 0 | 0 | 0 | 0 |
| Multiple memory corruption issues were addressed with improved input validation. | 输入与解析宾语类 | 5 | 0 | 5 | 5 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with additional restrictions on the observability of app states. | 脱敏与隐私宾语类 | 5 | 0 | 5 | 5 | 0 | 0 | 0 | 0 | 0 |
| The issue was resolved by sanitizing logging. | 校验与检查宾语类 | 5 | 0 | 5 | 4 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with a new entitlement. | 限制与访问控制宾语类 | 5 | 0 | 5 | 5 | 0 | 0 | 0 | 0 | 0 |
| This update brings that fix to devices that cannot update to the latest iOS version. | 其余句式（兜底） | 5 | 5 | 0 | 2 | 0 | 5 | 0 | 0 | 0 |
| "Hide IP Address" and "Block All Remote Content" may not apply to all mail content | 锁与配对状态宾语类 | 4 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 |
| A Content Security Policy bypass was addressed with improved enforcement in AudioWorklet contexts. | 限制与访问控制宾语类 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| A cryptographic issue was addressed with improved integrity checks. | 校验与检查宾语类 | 4 | 0 | 4 | 4 | 4 | 0 | 0 | 0 | 0 |
| A denial of service issue was addressed by removing the vulnerable code. | 其余句式（兜底） | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| A memory corruption issue was addressed by removing the vulnerable code. | 内存与生命周期宾语类 | 4 | 0 | 4 | 3 | 0 | 0 | 0 | 0 | 0 |
| A path handling issue was addressed with improved validation | 校验与检查宾语类 | 4 | 4 | 0 | 4 | 0 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed by moving sensitive data to a more secure location. | 脱敏与隐私宾语类 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed with improved state management. | 脱敏与隐私宾语类 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| A script imports issue was addressed with improved isolation. | 限制与访问控制宾语类 | 4 | 0 | 4 | 4 | 4 | 0 | 0 | 0 | 0 |
| A session management issue was addressed with improved checks. | 校验与检查宾语类 | 4 | 0 | 4 | 4 | 1 | 0 | 0 | 0 | 0 |
| A type confusion issue was addressed with improved state management. | 状态类宾语 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| A use-after-free issue was addressed by removing the vulnerable code. | 其余句式（兜底） | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| An input validation issue was addressed with improved memory handling. | 输入与解析宾语类 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| Multiple issues were addressed by disabling array allocation sinking. | 其余句式（兜底） | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| On a device with Private Relay enabled, adding a website to the Safari Reading List may reveal the originating IP address to the website | 其余句式（兜底） | 4 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 |
| The issue was addressed by restricting options offered on a locked device. | 锁与配对状态宾语类 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved access restrictions to the file system. | 限制与访问控制宾语类 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved permissions logic. | 限制与访问控制宾语类 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 4 | 0 |
| The issue was addressed with improved routing of Safari-originated requests. | 其他带程度形容词宾语类 | 4 | 0 | 4 | 4 | 4 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved URL validation. | 校验与检查宾语类 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved validation of the caller's access. | 校验与检查宾语类 | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improvements to the noise injection algorithm. | 其余句式（兜底） | 4 | 0 | 4 | 4 | 0 | 0 | 0 | 0 | 0 |
| A consistency issue was addressed with improved state handling. | 状态类宾语 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed by moving sensitive data to a protected location. | 脱敏与隐私宾语类 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed with improved handling of identifiers. | 脱敏与隐私宾语类 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| An access issue was addressed with improved access controls. | 限制与访问控制宾语类 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| An authorization issue was addressed with improved entitlement checks. | 限制与访问控制宾语类 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| An input validation issue was addressed by removing the vulnerable code. | 输入与解析宾语类 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| An integrity issue was addressed with Beacon Protection. | 校验与检查宾语类 | 3 | 0 | 3 | 3 | 3 | 0 | 0 | 0 | 0 |
| An issue with app access to camera metadata was addressed with improved logic. | 逻辑与代码宾语类 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 3 | 0 |
| An uncontrolled format string issue was addressed with improved input validation. | 输入与解析宾语类 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| Multiple memory corruption issues were addressed with improved memory handling. | 内存与生命周期宾语类 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with additional code-signing restrictions. | 限制与访问控制宾语类 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved handling of secure input state. | 状态类宾语 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| The issue was resolved by blocking unsigned services from launching on Intel Macs. | 锁与配对状态宾语类 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| Third party app extensions may not receive the correct sandbox restrictions | 限制与访问控制宾语类 | 3 | 3 | 0 | 3 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed by improved management of object lifetimes. | 内存与生命周期宾语类 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved data access restriction. | 限制与访问控制宾语类 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved handling of executable types. | 逻辑与代码宾语类 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 0 | 0 |
| This was addressed with improved state management. | 状态类宾语 | 3 | 0 | 3 | 3 | 0 | 0 | 0 | 3 | 0 |
| A clickjacking issue was addressed with improved out-of-process view handling. | 泛化处理与管理宾语类 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 | 0 |
| A race condition was addressed with improved checks. | 校验与检查宾语类 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 | 2 |
| A spoofing issue was addressed with improved truncation when displaying the fully qualified domain name | 界面与绘制宾语类 | 2 | 0 | 2 | 2 | 2 | 0 | 0 | 0 | 0 |
| A stack buffer overflow was addressed through improved input validation. | 输入与解析宾语类 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 | 0 |
| An access issue was addressed with additional sandbox restrictions on the system pasteboards. | 限制与访问控制宾语类 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 | 0 |
| Multiple issues were addressed by updating to curl version 8.4.0. | 其余句式（兜底） | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 | 0 |
| Privacy Indicators for microphone or camera access may not be correctly displayed | 脱敏与隐私宾语类 | 2 | 2 | 0 | 2 | 0 | 0 | 0 | 2 | 0 |
| The issue was addressed by removing the relevant flags. | 其余句式（兜底） | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with improved validation of environment variables. | 校验与检查宾语类 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 | 0 |
| This fix associated with the Coruna exploit was shipped in iOS 17.2 on December 11th, 2023. | 其余句式（兜底） | 2 | 2 | 0 | 2 | 0 | 2 | 0 | 0 | 0 |
| This issue was addressed by enabling hardened runtime. | 其余句式（兜底） | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed by enforcing library validation on the affected binaries. | 校验与检查宾语类 | 2 | 0 | 2 | 1 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed by removing additional entitlements. | 限制与访问控制宾语类 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with additional restrictions on the lock screen. | 限制与访问控制宾语类 | 2 | 0 | 2 | 2 | 0 | 0 | 0 | 0 | 0 |
| A package validation issue was addressed by blocking the vulnerable package. | 锁与配对状态宾语类 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| A permissions issue was addressed to help ensure Personas are always protected | 目的状语句（addressed to…不写方式） | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| A privacy issue was addressed with improved client ID handling for alternative app marketplaces. | 脱敏与隐私宾语类 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| A website may be able to access sensitive user data when resolving symlinks | 其余句式（兜底） | 1 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 |
| An authorization issue was addressed with improved network restrictions. | 限制与访问控制宾语类 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| An authorization issue was addressed with improved state management | 状态类宾语 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| Multiple issues were addressed by updating to OpenSSH 9.6. | 其余句式（兜底） | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed by refusing external connections by default. | 其余句式（兜底） | 1 | 0 | 1 | 1 | 1 | 0 | 0 | 0 | 0 |
| The issue was addressed by suspending Persona when the virtual keyboard is active. | 界面与绘制宾语类 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| The issue was addressed with additional app bundle integrity checks. | 校验与检查宾语类 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| The issue was resolved by not loading remote images | 界面与绘制宾语类 | 1 | 0 | 1 | 1 | 1 | 0 | 0 | 0 | 0 |
| The issue was resolved with improved permissions logic. | 限制与访问控制宾语类 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 1 | 0 |
| This fix associated with the Coruna exploit was shipped in iOS 16.6 on July 24, 2023. | 其余句式（兜底） | 1 | 1 | 0 | 1 | 0 | 1 | 0 | 0 | 0 |
| This fix associated with the Coruna exploit was shipped in iOS 17 on September 18, 2023. | 其余句式（兜底） | 1 | 1 | 0 | 1 | 0 | 1 | 0 | 0 | 0 |
| This fix associated with the Coruna exploit was shipped in iOS 17.3 on January 22, 2024. | 其余句式（兜底） | 1 | 1 | 0 | 1 | 0 | 1 | 0 | 0 | 0 |
| This issue was addressed by adding a delay between verification code attempts. | 校验与检查宾语类 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved message validation. | 校验与检查宾语类 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| This issue was addressed with improved validation of file attributes. | 校验与检查宾语类 | 1 | 0 | 1 | 1 | 0 | 0 | 0 | 0 | 0 |
| **合计** | 17 桶 | 7321 | 88 | 7233 | 页列合计 4367 | 854 | 126 | 166 | 557 | 232 |

# 附表 C 句形桶与优先序闭合分区（18 行）

| 桶 | 句次 | 段 | 页 | 名 | Impact 句 | Desc 句 | ∩R | ∩E |
|---|---:|---:|---:|---:|---:|---:|---:|---:|
| 内存与生命周期宾语类 | 1363 | 1363 | 179 | 76 | 0 | 1363 | 100 | 48 |
| 校验与检查宾语类 | 1202 | 1199 | 186 | 155 | 4 | 1198 | 127 | 23 |
| 边界与长度宾语类 | 1031 | 1031 | 188 | 79 | 0 | 1031 | 94 | 24 |
| 限制与访问控制宾语类 | 932 | 929 | 151 | 135 | 3 | 929 | 47 | 0 |
| 状态类宾语 | 747 | 747 | 179 | 115 | 0 | 747 | 127 | 9 |
| 输入与解析宾语类 | 583 | 583 | 139 | 87 | 0 | 583 | 130 | 0 |
| 脱敏与隐私宾语类 | 481 | 481 | 139 | 79 | 2 | 479 | 4 | 0 |
| 逻辑与代码宾语类 | 417 | 417 | 144 | 64 | 0 | 417 | 84 | 0 |
| 其余句式（兜底） | 195 | 190 | 113 | 40 | 46 | 149 | 44 | 9 |
| 泛化处理与管理宾语类 | 124 | 124 | 79 | 32 | 0 | 124 | 34 | 0 |
| 其他带程度形容词宾语类 | 83 | 83 | 61 | 16 | 0 | 83 | 30 | 0 |
| 锁与配对状态宾语类 | 82 | 82 | 62 | 17 | 12 | 70 | 18 | 8 |
| 零方式残句（addressed 后直接收尾） | 28 | 28 | 18 | 6 | 21 | 7 | 6 | 0 |
| 交互与同意宾语类 | 25 | 25 | 21 | 4 | 0 | 25 | 6 | 0 |
| 认证与凭据宾语类 | 23 | 23 | 18 | 9 | 0 | 23 | 0 | 0 |
| 界面与绘制宾语类 | 4 | 4 | 4 | 3 | 0 | 4 | 3 | 0 |
| 目的状语句（addressed to…不写方式） | 1 | 1 | 1 | 1 | 0 | 1 | 0 | 0 |
| **合计** | 7321 | 段列可重叠 | 页列合计 1682 | 名列合计 918 | 88 | 7233 | — | — |

## 附表 C 续表：句子打头与修复动词形态（优先序闭合分区）

| 形态刀 | 句次 | 段 | 页 | 名 |
|---|---:|---:|---:|---:|
| 「The／This issue was addressed …」打头 | 2619 | 2619 | 208 | 247 |
| 「A／An X issue was addressed …」打头 | 1985 | 1985 | 197 | 214 |
| 「A／An X（非 issue 名词）was addressed …」打头 | 2611 | 2611 | 224 | 203 |
| 「Multiple X issues were addressed …」打头 | 15 | 15 | 15 | 4 |
| 含「was addressed with」 | 9 | 9 | 9 | 2 |
| 含「may」（影响句情态） | 53 | 53 | 48 | 8 |
| 其余句式 | 29 | 24 | 19 | 10 |
| **合计** | 7321 | — | — | — |

# 附表 D 页面、编号、致谢、版式与字段落位分箱（30 行）

| 块 | 分箱 | 计数 | 说明 |
|---|---|---:|---|
| RFIX 段所在页的段数 | 1 | 50 | 共 256 页含本篇条目（全树 257 页） |
| RFIX 段所在页的段数 | 2 | 23 | 共 256 页含本篇条目（全树 257 页） |
| RFIX 段所在页的段数 | 3 | 3 | 共 256 页含本篇条目（全树 257 页） |
| RFIX 段所在页的段数 | 4 | 3 | 共 256 页含本篇条目（全树 257 页） |
| RFIX 段所在页的段数 | 5 | 3 | 共 256 页含本篇条目（全树 257 页） |
| RFIX 段所在页的段数 | 6-9 | 12 | 共 256 页含本篇条目（全树 257 页） |
| RFIX 段所在页的段数 | 10-19 | 37 | 共 256 页含本篇条目（全树 257 页） |
| RFIX 段所在页的段数 | 20-49 | 85 | 共 256 页含本篇条目（全树 257 页） |
| RFIX 段所在页的段数 | 50+ | 40 | 共 256 页含本篇条目（全树 257 页） |
| RFIX 段所在页的段数 | 合计 | 256 | 校验=256 页、RFIX 段 7247 |
| Top10 页 | 前 10 页合计 | 1513 | 占 7247 段的 20.9% |
| 同页共存 | 256 页全部段 | 9135 | 本篇占该页集 79.33%，每页均 28.31 段（第三套分母） |
| 同页共存 | 全本篇页 | 83 | 该页所有段都被本篇抓到 |
| 编号数分箱 | 0 个 | 4 | 无编号段 |
| 编号数分箱 | 1 个 | 6821 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 2 个 | 295 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 3 个 | 79 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 4+ 个 | 48 | 该段带此数量 distinct 编号 |
| 编号数分箱 | 合计 | 7247 | 带编号段=7243／7247，distinct 编号=2040，本集独占=2040，与他集共享=0 |
| 致谢人数分箱 | 1 人 | 4493 | — |
| 致谢人数分箱 | 2 人 | 1776 | — |
| 致谢人数分箱 | 3 人 | 434 | — |
| 致谢人数分箱 | 4 人 | 257 | — |
| 致谢人数分箱 | 5 人 | 119 | — |
| 致谢人数分箱 | 6-8 人 | 92 | — |
| 致谢人数分箱 | 9+ 人 | 72 | — |
| 致谢人数分箱 | 合计 | 7243 | 人次=12519，均值=1.73，最大=37（全树 1.80／37） |
| 版式域 | 域 B（11 页粗体版式） | 88 | 域 B 全段 91，占比 96.70%；域 A 7159／9,045 ＝ 79.15% |
| 字段落位 | Impact 侧 | 83 | Description 侧 7233；仅I 14／仅D 7164／I∩D 69（两组闭合 7247） |
| 句级 | 7247 段的本篇句数 | 1 句 7178／2 句 64／3+ 句 5 | 句次合计 7321（I 88＋D 7233），distinct 句 233 |

# 附表 E 中文层 63 词（64 行）

| 组 | 中文词 | 出现次数 | 覆盖页 | 覆盖页占比 | 最集中页面 |
|---|---|---:|---:|---:|---|
| 修复谓语类 | 修复 | 14 | 8 | 4.5% | 在 macOS 中防范恶意软件（5 次） |
| 修复谓语类 | 已修复 | 0 | 0 | 0.0% | — |
| 修复谓语类 | 被修复 | 0 | 0 | 0.0% | — |
| 修复谓语类 | 解决 | 15 | 12 | 6.7% | 针对 Apple 设备的管理式设备证明（3 次） |
| 修复谓语类 | 已解决 | 0 | 0 | 0.0% | — |
| 修复谓语类 | 处理 | 337 | 178 | 100.0% | 安全隔区（24 次） |
| 修复谓语类 | 缓解 | 0 | 0 | 0.0% | — |
| 修复谓语类 | 补丁 | 4 | 1 | 0.6% | Apple 操作系统中的后台安全改进（4 次） |
| 修复谓语类 | 更正 | 0 | 0 | 0.0% | — |
| 修复谓语类 | 改善 | 4 | 3 | 1.7% | 脸部匹配安全性（2 次） |
| 程度形容词类 | 改进 | 231 | 178 | 100.0% | Apple 操作系统中的后台安全改进（38 次） |
| 程度形容词类 | 增强 | 21 | 19 | 10.7% | 操作系统完整性（3 次） |
| 程度形容词类 | 额外 | 48 | 37 | 20.8% | 将信用卡或借记卡添加到 Apple Pay（5 次） |
| 程度形容词类 | 附加 | 7 | 6 | 3.4% | App 安全性概览（2 次） |
| 程度形容词类 | 更好 | 4 | 4 | 2.2% | 设备管理安全性概览（1 次） |
| 程度形容词类 | 更严格 | 1 | 1 | 0.6% | 搭载 Apple 芯片的 Mac 的 LocalPolicy 文件内容（1 次） |
| 程度形容词类 | 新的 | 88 | 47 | 26.4% | 在 macOS 中防范恶意软件（9 次） |
| 程度形容词类 | 完善 | 1 | 1 | 0.6% | Apple SoC 安全性（1 次） |
| 程度形容词类 | 优化 | 7 | 6 | 3.4% | Apple 操作系统中的后台安全改进（2 次） |
| 方式介词类 | 通过 | 1119 | 178 | 100.0% | 使用卫星收发紧急信息（19 次） |
| 方式介词类 | 借助 | 7 | 7 | 3.9% | 管理式 Apple 账户安全性（1 次） |
| 方式介词类 | 采用 | 262 | 178 | 100.0% | iCloud 高级数据保护（11 次） |
| 方式介词类 | 使用 | 3291 | 178 | 100.0% | 自动解锁 Apple 设备（51 次） |
| 校验与检查类 | 校验 | 19 | 3 | 1.7% | Apple 钱包中证件的安全性（16 次） |
| 校验与检查类 | 验证 | 432 | 178 | 100.0% | Apple 钱包中的证件（19 次） |
| 校验与检查类 | 检查 | 66 | 30 | 16.9% | 管理式 Apple 账户安全性（13 次） |
| 校验与检查类 | 输入校验 | 0 | 0 | 0.0% | — |
| 校验与检查类 | 边界检查 | 0 | 0 | 0.0% | — |
| 校验与检查类 | 完整性检查 | 5 | 4 | 2.2% | 基于 Intel 的 Mac 的 recoveryOS 和诊断环境（2 次） |
| 校验与检查类 | 权限检查 | 0 | 0 | 0.0% | — |
| 校验与检查类 | 身份验证 | 5 | 3 | 1.7% | Apple 钱包中的证件（3 次） |
| 处理与管理类 | 内存处理 | 0 | 0 | 0.0% | — |
| 处理与管理类 | 内存管理 | 6 | 5 | 2.8% | 术语表（2 次） |
| 处理与管理类 | 状态管理 | 0 | 0 | 0.0% | — |
| 处理与管理类 | 状态处理 | 0 | 0 | 0.0% | — |
| 处理与管理类 | 处理逻辑 | 0 | 0 | 0.0% | — |
| 处理与管理类 | 生命周期 | 9 | 8 | 4.5% | 证明过程安全性（2 次） |
| 处理与管理类 | 初始化 | 14 | 11 | 6.2% | LocalPolicy 签名密钥创建和管理（2 次） |
| 限制与控制类 | 限制 | 478 | 178 | 100.0% | LocalPolicy 签名密钥创建和管理（21 次） |
| 限制与控制类 | 约束 | 1 | 1 | 0.6% | 操作系统完整性（1 次） |
| 限制与控制类 | 访问控制 | 16 | 9 | 5.1% | 系统完整性保护（6 次） |
| 限制与控制类 | 权限 | 283 | 178 | 100.0% | 操作系统完整性（14 次） |
| 限制与控制类 | 授权 | 352 | 178 | 100.0% | NFC 与 SE 平台安全性（13 次） |
| 限制与控制类 | 沙盒 | 27 | 11 | 6.2% | macOS 中的 Option ROM 安全性（6 次） |
| 限制与控制类 | 隔离 | 10 | 6 | 3.4% | 安全隔区（3 次） |
| 限制与控制类 | 保护 | 5438 | 178 | 100.0% | 安全隔区（83 次） |
| 动作宾语类 | 移除 | 104 | 38 | 21.3% | Apple 操作系统中的后台安全改进（17 次） |
| 动作宾语类 | 删除 | 50 | 20 | 11.2% | iOS 中的车钥匙安全性（8 次） |
| 动作宾语类 | 禁用 | 0 | 0 | 0.0% | — |
| 动作宾语类 | 启用 | 149 | 63 | 35.4% | 激活锁安全性（20 次） |
| 动作宾语类 | 替换 | 5 | 5 | 2.8% | 单点登录安全性（1 次） |
| 动作宾语类 | 更新 | 373 | 178 | 100.0% | 文稿修订记录（31 次） |
| 动作宾语类 | 加密 | 1763 | 178 | 100.0% | 安全隔区（71 次） |
| 动作宾语类 | 脱敏 | 0 | 0 | 0.0% | — |
| 动作宾语类 | 锁定 | 305 | 178 | 100.0% | 安全隔区（18 次） |
| 声明与来源类 | 开源 | 1 | 1 | 0.6% | 单点登录安全性（1 次） |
| 声明与来源类 | 第三方 | 71 | 45 | 25.3% | iOS 和 iPadOS 中的 ReplayKit 安全性（6 次） |
| 声明与来源类 | 组件 | 270 | 178 | 100.0% | 安全隔区（32 次） |
| 声明与来源类 | 版本 | 254 | 81 | 45.5% | 蓝牙安全性（14 次） |
| 声明与来源类 | 漏洞 | 20 | 12 | 6.7% | 搭载 Apple 芯片的 Mac 的启动磁盘安全性策略控制（5 次） |
| 声明与来源类 | 缺陷 | 1 | 1 | 0.6% | 基于 Intel 的 Mac 中的 UEFI 固件安全性（1 次） |
| 声明与来源类 | 问题 | 17 | 11 | 6.2% | 关于 App Store 安全性（3 次） |
| 声明与来源类 | CVE | 0 | 0 | 0.0% | — |
| **合计** | 63 词（全零 15 词、非零 48 词） | — | — | 178 页可用 | — |

# 附表 F 组件名层 394 名（395 行）

| 组件名 | RFIX 段 | 页 | ∩R | ∩E | ∩M2 | ∩L | ∩BP | 该组件含 Impact 全集 | RFIX 占比 |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| WebKit | 900 | 155 | 134 | 41 | 7 | 71 | 24 | 910 | 98.9% |
| Kernel | 681 | 153 | 77 | 9 | 3 | 48 | 31 | 681 | 100.0% |
| ImageIO | 245 | 119 | 0 | 7 | 0 | 14 | 0 | 261 | 93.9% |
| CoreMedia | 150 | 93 | 0 | 8 | 16 | 0 | 0 | 150 | 100.0% |
| AppleMobileFileIntegrity | 146 | 62 | 0 | 0 | 3 | 20 | 14 | 146 | 100.0% |
| Shortcuts | 129 | 68 | 12 | 0 | 5 | 20 | 27 | 129 | 100.0% |
| Siri | 116 | 60 | 0 | 0 | 0 | 4 | 0 | 116 | 100.0% |
| SceneKit | 112 | 53 | 8 | 0 | 24 | 0 | 0 | 112 | 100.0% |
| PackageKit | 106 | 47 | 0 | 0 | 0 | 10 | 9 | 106 | 100.0% |
| Audio | 95 | 64 | 0 | 0 | 3 | 9 | 8 | 95 | 100.0% |
| Safari | 92 | 60 | 56 | 0 | 0 | 11 | 9 | 92 | 100.0% |
| Security | 91 | 64 | 34 | 0 | 0 | 18 | 0 | 91 | 100.0% |
| Model I/O | 84 | 34 | 33 | 0 | 3 | 0 | 0 | 88 | 95.5% |
| CoreServices | 81 | 41 | 0 | 0 | 0 | 18 | 5 | 81 | 100.0% |
| libxpc | 80 | 55 | 7 | 0 | 6 | 19 | 6 | 80 | 100.0% |
| CoreAudio | 79 | 56 | 8 | 5 | 0 | 0 | 0 | 79 | 100.0% |
| AirPlay | 77 | 14 | 77 | 0 | 0 | 0 | 7 | 77 | 100.0% |
| Spotlight | 69 | 49 | 0 | 0 | 2 | 3 | 0 | 69 | 100.0% |
| Foundation | 67 | 52 | 0 | 0 | 0 | 10 | 1 | 67 | 100.0% |
| Sandbox | 65 | 41 | 4 | 0 | 0 | 15 | 11 | 65 | 100.0% |
| StorageKit | 59 | 35 | 0 | 0 | 0 | 3 | 3 | 59 | 100.0% |
| LaunchServices | 58 | 41 | 8 | 0 | 3 | 12 | 8 | 58 | 100.0% |
| SMB | 58 | 17 | 14 | 0 | 0 | 0 | 0 | 58 | 100.0% |
| Wi-Fi | 58 | 41 | 41 | 0 | 0 | 0 | 0 | 58 | 100.0% |
| Disk Images | 54 | 26 | 3 | 0 | 0 | 0 | 6 | 54 | 100.0% |
| mDNSResponder | 53 | 36 | 35 | 0 | 0 | 8 | 0 | 53 | 100.0% |
| Messages | 50 | 45 | 1 | 9 | 6 | 12 | 6 | 50 | 100.0% |
| WebRTC | 48 | 26 | 0 | 0 | 0 | 0 | 0 | 48 | 100.0% |
| Accessibility | 46 | 38 | 4 | 4 | 0 | 3 | 0 | 46 | 100.0% |
| AVEVideoEncoder | 45 | 24 | 0 | 0 | 8 | 0 | 0 | 45 | 100.0% |
| CUPS | 41 | 15 | 9 | 0 | 6 | 3 | 0 | 43 | 95.3% |
| Bluetooth | 40 | 22 | 25 | 0 | 8 | 0 | 0 | 41 | 97.6% |
| SharedFileList | 39 | 22 | 0 | 0 | 0 | 14 | 0 | 39 | 100.0% |
| APFS | 38 | 32 | 4 | 0 | 0 | 0 | 3 | 38 | 100.0% |
| Notes | 38 | 31 | 3 | 0 | 0 | 1 | 0 | 38 | 100.0% |
| Apple Neural Engine | 36 | 34 | 0 | 0 | 0 | 0 | 0 | 36 | 100.0% |
| Mail | 36 | 27 | 16 | 0 | 0 | 9 | 0 | 36 | 100.0% |
| Accounts | 34 | 29 | 0 | 0 | 0 | 7 | 9 | 34 | 100.0% |
| GPU Drivers | 34 | 29 | 0 | 0 | 0 | 0 | 0 | 34 | 100.0% |
| CoreText | 33 | 29 | 0 | 0 | 0 | 0 | 0 | 33 | 100.0% |
| Game Center | 33 | 27 | 0 | 0 | 0 | 0 | 0 | 33 | 100.0% |
| CoreUI | 32 | 13 | 0 | 0 | 0 | 0 | 0 | 32 | 100.0% |
| HFS | 32 | 10 | 3 | 0 | 0 | 0 | 0 | 32 | 100.0% |
| WindowServer | 32 | 26 | 0 | 0 | 0 | 1 | 1 | 32 | 100.0% |
| System Settings | 30 | 20 | 0 | 0 | 0 | 5 | 5 | 30 | 100.0% |
| FontParser | 29 | 24 | 0 | 0 | 0 | 0 | 0 | 29 | 100.0% |
| Installer | 29 | 21 | 0 | 0 | 0 | 9 | 0 | 29 | 100.0% |
| TCC | 29 | 25 | 0 | 0 | 0 | 3 | 4 | 29 | 100.0% |
| NetworkExtension | 28 | 20 | 3 | 0 | 0 | 0 | 0 | 28 | 100.0% |
| WebKit Canvas | 28 | 28 | 7 | 0 | 0 | 0 | 0 | 28 | 100.0% |
| CoreGraphics | 27 | 24 | 0 | 3 | 8 | 0 | 0 | 27 | 100.0% |
| IOHIDFamily | 27 | 20 | 0 | 0 | 0 | 0 | 0 | 27 | 100.0% |
| AppleAVD | 26 | 26 | 0 | 0 | 0 | 0 | 0 | 26 | 100.0% |
| IOKit | 26 | 26 | 0 | 0 | 0 | 0 | 0 | 26 | 100.0% |
| Apple Account | 25 | 20 | 6 | 0 | 0 | 0 | 0 | 25 | 100.0% |
| CloudKit | 25 | 21 | 0 | 0 | 0 | 0 | 0 | 25 | 100.0% |
| Contacts | 25 | 17 | 0 | 0 | 0 | 0 | 0 | 25 | 100.0% |
| DiskArbitration | 25 | 19 | 0 | 0 | 0 | 0 | 0 | 25 | 100.0% |
| Metal | 25 | 21 | 4 | 0 | 11 | 0 | 0 | 25 | 100.0% |
| sips | 25 | 13 | 0 | 0 | 0 | 0 | 0 | 25 | 100.0% |
| App Store | 24 | 24 | 0 | 0 | 0 | 0 | 0 | 24 | 100.0% |
| Crash Reporter | 24 | 23 | 0 | 0 | 0 | 3 | 0 | 24 | 100.0% |
| MediaRemote | 24 | 24 | 0 | 0 | 0 | 0 | 0 | 24 | 100.0% |
| AppleJPEG | 23 | 23 | 0 | 0 | 0 | 0 | 0 | 28 | 82.1% |
| CoreMedia Playback | 23 | 23 | 0 | 0 | 0 | 0 | 0 | 23 | 100.0% |
| Maps | 23 | 22 | 0 | 0 | 0 | 6 | 0 | 23 | 100.0% |
| Sandbox Profiles | 23 | 20 | 0 | 0 | 0 | 0 | 0 | 23 | 100.0% |
| Calendar | 22 | 14 | 2 | 0 | 0 | 4 | 0 | 22 | 100.0% |
| IOMobileFrameBuffer | 22 | 17 | 0 | 0 | 3 | 0 | 0 | 22 | 100.0% |
| Pro Res | 21 | 17 | 0 | 0 | 0 | 0 | 0 | 21 | 100.0% |
| CFNetwork | 20 | 16 | 15 | 0 | 0 | 7 | 0 | 20 | 100.0% |
| Storage | 20 | 15 | 0 | 0 | 0 | 0 | 0 | 20 | 100.0% |
| WebDAV | 20 | 9 | 0 | 0 | 0 | 2 | 2 | 20 | 100.0% |
| Dock | 19 | 17 | 0 | 0 | 0 | 2 | 0 | 19 | 100.0% |
| Find My | 19 | 19 | 0 | 0 | 0 | 0 | 0 | 19 | 100.0% |
| Photos | 19 | 14 | 0 | 0 | 0 | 0 | 0 | 19 | 100.0% |
| libarchive | 19 | 19 | 0 | 0 | 0 | 0 | 4 | 30 | 63.3% |
| Authentication Services | 18 | 13 | 9 | 0 | 0 | 0 | 0 | 18 | 100.0% |
| FileProvider | 18 | 16 | 0 | 0 | 0 | 0 | 0 | 18 | 100.0% |
| IOSurfaceAccelerator | 18 | 18 | 0 | 0 | 0 | 0 | 0 | 18 | 100.0% |
| Photos Storage | 18 | 14 | 0 | 0 | 0 | 0 | 0 | 18 | 100.0% |
| Software Update | 18 | 15 | 0 | 0 | 0 | 2 | 0 | 18 | 100.0% |
| StoreKit | 18 | 18 | 0 | 0 | 0 | 0 | 0 | 18 | 100.0% |
| ATS | 17 | 12 | 0 | 0 | 0 | 0 | 0 | 17 | 100.0% |
| IOGPUFamily | 17 | 17 | 0 | 0 | 0 | 0 | 0 | 17 | 100.0% |
| Managed Configuration | 17 | 17 | 0 | 0 | 0 | 0 | 0 | 17 | 100.0% |
| Printing | 17 | 11 | 0 | 0 | 0 | 0 | 0 | 17 | 100.0% |
| App Intents | 16 | 16 | 0 | 0 | 0 | 8 | 0 | 16 | 100.0% |
| Assets | 16 | 13 | 0 | 0 | 0 | 0 | 3 | 16 | 100.0% |
| Core Bluetooth | 16 | 13 | 11 | 0 | 0 | 0 | 1 | 16 | 100.0% |
| Heimdal | 16 | 14 | 4 | 0 | 0 | 0 | 0 | 19 | 84.2% |
| Safari Private Browsing | 16 | 14 | 0 | 0 | 0 | 1 | 0 | 16 | 100.0% |
| Screen Sharing Server | 16 | 8 | 8 | 0 | 0 | 3 | 0 | 16 | 100.0% |
| Voice Control | 16 | 16 | 0 | 0 | 0 | 0 | 0 | 16 | 100.0% |
| WebKit Web Inspector | 16 | 16 | 0 | 0 | 0 | 0 | 0 | 16 | 100.0% |
| CoreUtils | 15 | 15 | 15 | 0 | 0 | 0 | 0 | 15 | 100.0% |
| File Bookmark | 15 | 12 | 0 | 0 | 0 | 9 | 0 | 15 | 100.0% |
| ICU | 15 | 15 | 0 | 0 | 0 | 0 | 0 | 15 | 100.0% |
| Multi-Touch | 15 | 15 | 0 | 0 | 0 | 0 | 3 | 15 | 100.0% |
| NetAuth | 15 | 9 | 3 | 0 | 0 | 0 | 0 | 15 | 100.0% |
| Share Sheet | 15 | 15 | 0 | 0 | 0 | 0 | 0 | 15 | 100.0% |
| SoftwareUpdate | 15 | 12 | 0 | 0 | 0 | 0 | 0 | 15 | 100.0% |
| Xsan | 15 | 9 | 0 | 0 | 0 | 0 | 0 | 15 | 100.0% |
| dyld | 15 | 15 | 2 | 5 | 0 | 0 | 5 | 15 | 100.0% |
| AirDrop | 14 | 14 | 8 | 0 | 0 | 0 | 0 | 14 | 100.0% |
| FaceTime | 14 | 13 | 0 | 0 | 0 | 0 | 0 | 14 | 100.0% |
| Focus | 14 | 10 | 0 | 0 | 0 | 0 | 0 | 14 | 100.0% |
| MobileBackup | 14 | 12 | 0 | 0 | 0 | 9 | 0 | 14 | 100.0% |
| RealityKit | 14 | 7 | 0 | 0 | 7 | 0 | 0 | 14 | 100.0% |
| UIKit | 14 | 13 | 0 | 0 | 0 | 0 | 5 | 14 | 100.0% |
| VoiceOver | 14 | 14 | 0 | 0 | 0 | 3 | 0 | 14 | 100.0% |
| Web Extensions | 14 | 10 | 8 | 0 | 0 | 0 | 0 | 14 | 100.0% |
| libxslt | 14 | 7 | 0 | 0 | 0 | 0 | 0 | 30 | 46.7% |
| ARKit | 13 | 13 | 0 | 0 | 0 | 0 | 0 | 13 | 100.0% |
| Accelerate Framework | 13 | 8 | 0 | 0 | 0 | 0 | 0 | 13 | 100.0% |
| AuthKit | 13 | 13 | 0 | 0 | 0 | 0 | 0 | 13 | 100.0% |
| BackgroundAssets | 13 | 13 | 0 | 0 | 0 | 7 | 0 | 13 | 100.0% |
| Call History | 13 | 11 | 0 | 0 | 0 | 1 | 0 | 13 | 100.0% |
| Icons | 13 | 12 | 0 | 0 | 0 | 0 | 0 | 13 | 100.0% |
| MobileAccessoryUpdater | 13 | 13 | 0 | 0 | 0 | 0 | 0 | 13 | 100.0% |
| Admin Framework | 12 | 11 | 0 | 0 | 0 | 6 | 0 | 12 | 100.0% |
| MigrationKit | 12 | 10 | 0 | 0 | 0 | 0 | 0 | 12 | 100.0% |
| Notification Center | 12 | 10 | 0 | 0 | 0 | 0 | 0 | 12 | 100.0% |
| QuartzCore | 12 | 12 | 0 | 0 | 0 | 0 | 0 | 12 | 100.0% |
| Sync Services | 12 | 12 | 0 | 0 | 0 | 0 | 6 | 12 | 100.0% |
| Time Zone | 12 | 12 | 0 | 0 | 0 | 3 | 3 | 12 | 100.0% |
| Transparency | 12 | 12 | 0 | 0 | 0 | 0 | 0 | 12 | 100.0% |
| afpfs | 12 | 7 | 6 | 0 | 0 | 0 | 0 | 12 | 100.0% |
| copyfile | 12 | 12 | 0 | 0 | 0 | 1 | 7 | 12 | 100.0% |
| quarantine | 12 | 9 | 0 | 0 | 0 | 3 | 1 | 12 | 100.0% |
| DesktopServices | 11 | 10 | 3 | 0 | 0 | 3 | 3 | 11 | 100.0% |
| Mail Drafts | 11 | 10 | 11 | 0 | 0 | 8 | 0 | 11 | 100.0% |
| NSSpellChecker | 11 | 11 | 0 | 0 | 0 | 0 | 0 | 11 | 100.0% |
| Screen Time | 11 | 8 | 0 | 0 | 0 | 0 | 0 | 11 | 100.0% |
| WebContentFilter | 11 | 9 | 0 | 0 | 0 | 0 | 0 | 11 | 100.0% |
| WebKit Storage | 11 | 11 | 6 | 0 | 0 | 0 | 0 | 11 | 100.0% |
| iCloud | 11 | 10 | 1 | 0 | 0 | 0 | 0 | 11 | 100.0% |
| libnetcore | 11 | 10 | 3 | 0 | 0 | 8 | 0 | 11 | 100.0% |
| AppSandbox | 10 | 8 | 1 | 0 | 0 | 3 | 0 | 10 | 100.0% |
| Finder | 10 | 10 | 0 | 0 | 2 | 3 | 3 | 10 | 100.0% |
| Music | 10 | 9 | 0 | 0 | 0 | 0 | 0 | 10 | 100.0% |
| Networking | 10 | 10 | 0 | 0 | 0 | 1 | 0 | 10 | 100.0% |
| Phone | 10 | 10 | 0 | 0 | 0 | 0 | 0 | 10 | 100.0% |
| Quick Look | 10 | 10 | 0 | 0 | 0 | 1 | 0 | 10 | 100.0% |
| Symptom Framework | 10 | 10 | 0 | 0 | 0 | 0 | 0 | 10 | 100.0% |
| curl | 10 | 10 | 0 | 0 | 0 | 0 | 0 | 39 | 25.6% |
| libc | 10 | 10 | 0 | 0 | 0 | 0 | 0 | 10 | 100.0% |
| ppp | 10 | 8 | 0 | 0 | 0 | 0 | 0 | 10 | 100.0% |
| udf | 10 | 7 | 0 | 0 | 0 | 0 | 0 | 10 | 100.0% |
| AppKit | 9 | 9 | 0 | 0 | 0 | 2 | 0 | 9 | 100.0% |
| AppleDouble | 9 | 9 | 0 | 0 | 4 | 0 | 0 | 9 | 100.0% |
| AppleKeyStore | 9 | 9 | 0 | 0 | 0 | 0 | 0 | 9 | 100.0% |
| Automator | 9 | 9 | 0 | 0 | 0 | 0 | 3 | 9 | 100.0% |
| Calling Framework | 9 | 9 | 5 | 0 | 0 | 0 | 0 | 9 | 100.0% |
| Compression | 9 | 9 | 0 | 0 | 0 | 0 | 0 | 9 | 100.0% |
| Core Data | 9 | 9 | 0 | 0 | 0 | 0 | 1 | 9 | 100.0% |
| IOSurface | 9 | 9 | 0 | 0 | 0 | 0 | 0 | 9 | 100.0% |
| Intel Graphics Driver | 9 | 6 | 0 | 0 | 0 | 0 | 0 | 9 | 100.0% |
| Libinfo | 9 | 9 | 0 | 0 | 0 | 0 | 3 | 9 | 100.0% |
| NetFSFramework | 9 | 9 | 0 | 0 | 0 | 0 | 0 | 9 | 100.0% |
| Passwords | 9 | 8 | 5 | 0 | 0 | 1 | 2 | 9 | 100.0% |
| RemoteViewServices | 9 | 8 | 0 | 0 | 0 | 5 | 0 | 9 | 100.0% |
| configd | 9 | 8 | 0 | 0 | 0 | 0 | 0 | 9 | 100.0% |
| sudo | 9 | 9 | 0 | 0 | 0 | 5 | 0 | 10 | 90.0% |
| 802.1X | 8 | 8 | 8 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| AMD | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| AppleGraphicsControl | 8 | 6 | 0 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| Archive Utility | 8 | 6 | 0 | 0 | 0 | 0 | 2 | 8 | 100.0% |
| BiometricKit | 8 | 8 | 0 | 0 | 0 | 1 | 0 | 8 | 100.0% |
| Control Center | 8 | 7 | 0 | 0 | 0 | 4 | 0 | 8 | 100.0% |
| DeviceCheck | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| Display | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| Graphics | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| IOSkywalkFamily | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| Libnotify | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 10 | 80.0% |
| MobileStorageMounter | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| Power Services | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| RTKit | 8 | 8 | 0 | 8 | 0 | 0 | 8 | 8 | 100.0% |
| TV App | 8 | 8 | 0 | 0 | 0 | 3 | 0 | 8 | 100.0% |
| Telephony | 8 | 6 | 5 | 0 | 0 | 0 | 2 | 8 | 100.0% |
| Weather | 8 | 8 | 0 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| zlib | 8 | 8 | 8 | 0 | 0 | 0 | 0 | 8 | 100.0% |
| ASP TCP | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| App Support | 7 | 7 | 0 | 0 | 0 | 7 | 0 | 7 | 100.0% |
| AppleVA | 7 | 6 | 0 | 0 | 3 | 0 | 0 | 7 | 100.0% |
| Baseband | 7 | 4 | 6 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| CoreCrypto | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| CoreMedia Video Toolbox | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| CoreMotion | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| DriverKit | 7 | 4 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| Image Capture | 7 | 6 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| NSColorPanel | 7 | 7 | 0 | 0 | 0 | 3 | 0 | 7 | 100.0% |
| Power Management | 7 | 6 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| Reminders | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| Screenshots | 7 | 7 | 0 | 0 | 0 | 2 | 0 | 7 | 100.0% |
| SystemMigration | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| UIFoundation | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| WebKit History | 7 | 7 | 7 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| XPC | 7 | 7 | 0 | 0 | 1 | 0 | 0 | 7 | 100.0% |
| afclip | 7 | 7 | 0 | 0 | 0 | 0 | 0 | 7 | 100.0% |
| exFAT | 7 | 5 | 0 | 0 | 2 | 0 | 0 | 7 | 100.0% |
| file | 7 | 7 | 0 | 0 | 4 | 0 | 0 | 7 | 100.0% |
| zip | 7 | 7 | 1 | 0 | 0 | 3 | 3 | 7 | 100.0% |
| AppleRAID | 6 | 3 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| CoreML | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| CoreSymbolication | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| DeviceLink | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| FrontBoard | 6 | 6 | 0 | 0 | 0 | 1 | 0 | 6 | 100.0% |
| GeoServices | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Keychain Access | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Login Window | 6 | 5 | 0 | 0 | 0 | 1 | 1 | 6 | 100.0% |
| NSDocument | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Power Manager | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Remote Management | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Security Initialization | 6 | 6 | 0 | 0 | 0 | 0 | 3 | 6 | 100.0% |
| Single Sign-On | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Spell Check | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Vision | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| autofs | 6 | 3 | 3 | 0 | 0 | 3 | 3 | 6 | 100.0% |
| cd9660 | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| file_cmds | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| iCloud Document Sharing | 6 | 6 | 0 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| smbx | 6 | 6 | 3 | 0 | 0 | 0 | 0 | 6 | 100.0% |
| Accelerate | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| Accounts Framework | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| Clipboard | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| CloudAttestation | 5 | 5 | 0 | 0 | 0 | 0 | 5 | 5 | 100.0% |
| Core Services | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| CoreAnimation | 5 | 5 | 3 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| Image Processing | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| JavaScriptCore | 5 | 5 | 0 | 5 | 0 | 0 | 0 | 5 | 100.0% |
| Mail Accounts | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| Mail Search | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| Notification Services | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| RPAC | 5 | 5 | 0 | 4 | 0 | 0 | 4 | 5 | 100.0% |
| Safari Downloads | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 7 | 71.4% |
| Safe Browsing | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| Screen Capture | 5 | 4 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| SecurityAgent | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| Status Bar | 5 | 5 | 0 | 0 | 0 | 4 | 0 | 5 | 100.0% |
| System | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| WebKit Sandboxing | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| libiconv | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 5 | 100.0% |
| libxml2 | 5 | 5 | 0 | 0 | 0 | 0 | 0 | 19 | 26.3% |
| Airport | 4 | 4 | 1 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| BOM | 4 | 4 | 0 | 0 | 0 | 0 | 3 | 4 | 100.0% |
| Books | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| ColorSync | 4 | 3 | 0 | 0 | 4 | 0 | 0 | 4 | 100.0% |
| CoreBluetooth - LE | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| CoreRoutine | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| CoreServicesUIAgent | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| CoreVideo | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| Data Detectors UI | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| Directory Utility | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| Family Sharing | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| Logging | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| LoginWindow | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| MallocStackLogging | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| OpenSSH | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 10 | 40.0% |
| Parental Controls | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| Passkeys | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 5 | 80.0% |
| Perl | 4 | 4 | 0 | 0 | 0 | 1 | 0 | 8 | 50.0% |
| QuickLook | 4 | 4 | 0 | 0 | 2 | 0 | 0 | 4 | 100.0% |
| Touch Bar | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| UserAccountUpdater | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| Watch App | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| WebKit Process Model | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| Wi-Fi Connectivity | 4 | 4 | 0 | 0 | 0 | 0 | 0 | 4 | 100.0% |
| AccountPolicy | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Apple Online Store Kit | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Apple Software Restore | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| AppleEvents | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Camera | 3 | 3 | 0 | 0 | 0 | 2 | 0 | 3 | 100.0% |
| CoreDrag | 3 | 3 | 0 | 0 | 3 | 0 | 0 | 3 | 100.0% |
| Diagnostics | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Disk Utility | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| File System | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Game Controllers | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| IOAcceleratorFamily | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Kerberos Helper | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Kerberos v5 PAM module | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Kext Management | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| MDM Configuration Tools | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| MediaLibrary | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Mobile Device Service | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Net-SNMP | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Network Extensions | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| On-device Intelligence | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| QuickTime Player | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Restore Framework | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Screen Sharing | 3 | 3 | 3 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Scripting Bridge | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Setup Assistant | 3 | 3 | 0 | 0 | 0 | 1 | 0 | 3 | 100.0% |
| SpringBoard | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Storage Management | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Storage Services | 3 | 3 | 0 | 0 | 0 | 3 | 0 | 3 | 100.0% |
| Touch Bar Controls | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Wi‑Fi | 3 | 3 | 0 | 0 | 3 | 0 | 0 | 3 | 100.0% |
| XProtect | 3 | 2 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Xcode | 3 | 3 | 0 | 0 | 0 | 1 | 1 | 3 | 100.0% |
| bless | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| bootp | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| iCloud Photo Library | 3 | 3 | 0 | 0 | 0 | 0 | 3 | 3 | 100.0% |
| macOS Recovery | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| manpages | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| odproxyd | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| xar | 3 | 3 | 0 | 0 | 0 | 3 | 0 | 3 | 100.0% |
| zsh | 3 | 3 | 0 | 0 | 0 | 0 | 0 | 3 | 100.0% |
| Apple Intelligence | 2 | 2 | 0 | 0 | 0 | 0 | 1 | 2 | 100.0% |
| CoreTypes | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Dev Tools | 2 | 1 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Disk Management | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| ExtensionKit | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Face Gallery | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Filters | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| GarageBand | 2 | 2 | 0 | 0 | 1 | 0 | 0 | 2 | 100.0% |
| Handoff | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| IOGraphics | 2 | 1 | 0 | 0 | 2 | 0 | 0 | 2 | 100.0% |
| Libsystem | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Live Captions | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Mail Addressing | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| MetricKit | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| NSOpenPanel | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Playgrounds | 2 | 2 | 0 | 0 | 0 | 0 | 1 | 2 | 100.0% |
| Presence | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| PrintCenter | 2 | 1 | 0 | 0 | 2 | 0 | 0 | 2 | 100.0% |
| Privacy | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| RepairKit | 2 | 2 | 0 | 0 | 0 | 0 | 2 | 2 | 100.0% |
| SDK | 2 | 1 | 2 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Sidecar | 2 | 2 | 0 | 0 | 0 | 0 | 2 | 2 | 100.0% |
| Siri Suggestions | 2 | 2 | 0 | 0 | 0 | 2 | 0 | 2 | 100.0% |
| Swift | 2 | 2 | 0 | 0 | 0 | 2 | 0 | 2 | 100.0% |
| Synapse | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Video Playback | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| WidgetKit | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| WorkoutKit | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| Xcode IDE | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| bash | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| iTunes | 2 | 2 | 1 | 0 | 0 | 1 | 0 | 2 | 100.0% |
| iTunes Store | 2 | 2 | 0 | 0 | 0 | 0 | 2 | 2 | 100.0% |
| iWork | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| otool | 2 | 2 | 0 | 0 | 0 | 0 | 0 | 2 | 100.0% |
| APNs | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| AWD | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| AirPort | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| App Protection | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| App Store Connect | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Apple Intelligence Reports | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Apple Music Classical | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Apple TV Remote | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| AppleFDEKeyStore | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| AppleScript | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 1 | 100.0% |
| Application Firewall | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| ArchiveService | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| BackBoardServices | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Cellular | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Compressor | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Face ID | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| FeedbackLogger | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Found in Apps | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| GNU | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| GPUTools | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| IDE Assets | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| IDE CoreML | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| IDE Documentation | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| IDE Tools | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Instruments | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Journal | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Keynote | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| LLVM | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Lock Screen | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| MarketplaceKit | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Media Session | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| MediaExperience | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| MobileLockdown | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| NSColor | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Notifications | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 1 | 100.0% |
| Pages | 1 | 1 | 0 | 0 | 1 | 0 | 0 | 1 | 100.0% |
| Persona | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Reset Services | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Settings | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Simulator | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| StickerKit | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Stolen Device Protection | 1 | 1 | 0 | 0 | 0 | 1 | 0 | 1 | 100.0% |
| Summarization Services | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| System Extensions | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Terminal | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Text Input | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Trusted Device | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| User Management | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Vim | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 9 | 11.1% |
| Virtualization | 1 | 1 | 0 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| Wallet | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 1 | 100.0% |
| lldb | 1 | 1 | 1 | 0 | 0 | 0 | 0 | 1 | 100.0% |
| **合计** | 7247 | 页列合计 4474 | 820 | 108 | 166 | 550 | 300 | 这 394 名 Impact 全集 7382 段（全树 7,434 的 99.3%） | — |

---

**边界声明：** 本报告统计的是 Apple 安全响应顾问页公开措辞（段落、字段、句子、词界、修复谓语与方式介词的搭配、宾语形态与组件名的出现与分布），以及 178 份中文支持文档的用词频次，不是漏洞本身；全部计数只陈述"公开文本使用了该词／该句"这一文本事实，不构成对缺陷类别、CWE 归属、修复是否有效、修复代码实际内容、涉及数据种类或任何 iOS／macOS 版本与机制安全性的判断或暗示。本报告不提供绕过方法、利用代码、恶意样本、伪造输入的制作方法或利用链构造方式，也不提供针对他人设备的操作步骤与时序，不涉及权限提升、恶意软件编写或攻击行为的实施指导。分母、主刀与十九把对照刀定义、介词与主语四档优先序、十七个句形桶与八类后果定义见第 1 章、第 4–9 章与第 15 章；数据取自本地 `_tmp/adv2` 页面镜像（258 份 HTML 排除 1 份聚合页后的 257 页），镜像若更新则本报告全部数字需重测。

Telegram: https://t.me/one00190
