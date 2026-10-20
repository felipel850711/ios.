@@TITLE@@iOS「Coruna」与「DarkSword」漏洞利用套件安全分析报告

@@SUBTITLE@@从国家级军火库泄漏到网络犯罪扩散——2026 年 iOS 威胁格局深度剖析

@@INFO@@报告主题 | Coruna 与 DarkSword iOS 漏洞利用套件技术分析
@@INFO@@披露来源 | Google 威胁情报（GTIG）、iVerify、Lookout、Kaspersky 等
@@INFO@@涉及平台 | Apple iOS / iPadOS 13.0 – 18.6.x
@@INFO@@编制日期 | 2026 年 10 月 3 日
@@INFO@@适用对象 | 安全工程师、企业 IT 管理员、移动安全运营团队

@@TOC@@目录

# 一、报告概述

## 1.1 背景

2026 年 3 月，iOS 安全研究社区连续披露了两套影响深远的漏洞利用套件——**Coruna**（2026 年 3 月 3 日，Google 威胁情报与 iVerify）与 **DarkSword**（2026 年 3 月 18 日，Google 威胁情报、Lookout 与 iVerify）。二者并非孤立事件，而是一条清晰的演进链路：Coruna 被判定为「三角测量行动」（Operation Triangulation）技术框架的升级版本，而 DarkSword 则是在 Coruna 基础上针对新版 iOS 重构的继任者。

更具警示意义的是其扩散路径：这两套原本具备**国家级水准**的攻击工具，疑似因泄漏而落入网络犯罪团伙之手，用于大规模感染设备、窃取加密货币钱包与个人凭据。仅中国境内就有**至少 4.2 万台设备**被 Coruna 感染（Kaspersky 估计）；DarkSword 则在沙特阿拉伯、土耳其、马来西亚、乌克兰等地被观测到实际攻击活动。这标志着 iOS 威胁进入「多行为者、多动机、军民两用工具扩散」的新阶段。

## 1.2 报告范围与方法

本报告聚焦以下三个层面：

1. **漏洞机理**：Coruna 的 5 条利用链、23 个漏洞组件及 DarkSword 的 6 漏洞全链（含 CVE 编号、影响版本、利用阶段）；
2. **威胁行为者**：国家级行为者（UNC6353、UNC6748、PARS Defense 客户）与犯罪团伙的分工与地理分布；
3. **防御实践**：针对企业 MDM 管理环境与个人用户的检测、加固与应急响应建议。

报告中的 CVE 编号、影响与修复版本均以 Google 威胁情报、Apple 官方安全公告及 NVD 公开记录为准；技术机理描述综合各厂商公开研究文献，并在参考文献中逐条列明出处。

## 1.3 两套漏洞速览

| 维度 | Coruna | DarkSword |
| --- | --- | --- |
| 披露时间 | 2026-03-03 | 2026-03-18/19 |
| 首次使用痕迹 | 约 2025 年 2 月被发现 | 2025 年 11 月起在野使用 |
| 影响范围 | iOS 13.0 – 17.2.1 | iOS 13 – 18.6.2（18.7.5/26.3 之前版本） |
| 漏洞规模 | 23 个漏洞组件、5 条利用链 | 6 个漏洞串联的单条全链 |
| 关键 CVE | CVE-2024-23222、CVE-2023-32434 等 | CVE-2026-20700、CVE-2025-31277 等 |
| 触发方式 | 零点击（被注入恶意代码的合法网站） | 零点击（Safari 恶意 iframe） |
| 驻留方式 | 无文件驻留内存，重启即清除 | 无文件、纯 JavaScript 执行，重启即清除 |
| 主要行为者 | UNC6353（俄语系）、UNC6691（中文系） | UNC6353、UNC6748、PARS Defense 客户 |
| 主要受害地区 | 中国（≥4.2 万台）、乌克兰 | 沙特、土耳其、马来西亚、乌克兰 |
| 主要动机 | 凭据窃取、加密货币盗取 | 凭据与密钥窃取、国家驱动情报收集 |
| 修复版本 | 2026-03-11 起各旧版本分支修复 | iOS 18.7.6 / 26.3.1 及以后 |

# 二、Coruna 漏洞利用套件深度分析

## 2.1 概况与披露时间线

Coruna 是一套针对 Apple iOS 的**漏洞利用套件（exploit kit）**，由 Google 威胁情报团队（GTIG/Mandiant）与 iOS 安全公司 iVerify 于 2026 年 3 月 3 日联合披露。研究显示该套件早在 2025 年 2 月即进入研究视野，历经约一年的追踪与确认后才公开。iVerify 将其评价为「国家级水准的 iOS 漏洞利用工具包」，并指出其开发与政府背景存在关联。

关键时间线如下：

1. **2025 年 2 月**：Coruna 进入安全研究机构的追踪视野；
2. **2026-03-03**：Google 威胁情报正式披露，iVerify 同步发布分析；
3. **2026-03-05**：CISA 将 Coruna 所涉 3 个 CVE 加入「已知被利用漏洞」（KEV）目录；
4. **2026-03-11**：Apple 发布覆盖 iOS 15/16/17 旧版本分支的安全更新，修复 Coruna 所利用漏洞（据 Kaspersky：15.8.7、16.7.15、18.7.7 等）；
5. **2026 年 3-8 月**：多家厂商（Kaspersky、Malwarebytes、DarkReading）报告 Coruna 与 DarkSword 在全球范围扩散，感染规模持续扩大。

## 2.2 漏洞组合与 CVE 清单

据 Google 威胁情报与维基百科汇总条目，Coruna 整合了 **23 个漏洞利用组件、组织为 5 条独立利用链**，覆盖 WebKit、内核（XNU）、图形驱动等多个攻击面，可按目标设备版本自动选择链路。其中已被公开点名的主要 CVE 包括：

| CVE 编号 | 组件/类型 | 备注 |
| --- | --- | --- |
| CVE-2024-23222 | WebKit 类型混淆 | 影响至 iOS 17.2，曾被在野利用 |
| CVE-2023-32434 | 内核内存越界 | 「三角测量行动」同款漏洞 |
| CVE-2023-38606 | 内核/ANE 相关 | 「三角测量行动」同款漏洞 |
| CVE-2023-41974 | Apple 组件漏洞 | 据 Coruna 披露来源 |
| CVE-2023-43000 | Apple 组件漏洞 | 据 Coruna 披露来源 |
| CVE-2022-48503 | Apple 组件漏洞 | 据 Coruna 披露来源 |
| CVE-2021-30952 | Apple 组件漏洞 | 据 Coruna 披露来源 |

> 说明：上表为公开来源明确点名的 CVE；Coruna 全量 23 个漏洞组件中其余 CVE 未在公开文献中逐一列明。CVE-2023-32434 与 CVE-2023-38606 与 2023 年「三角测量行动」所利用漏洞相同，是判定 Coruna 与该行动存在承继关系的核心证据之一。

## 2.3 攻击链与感染方式

Coruna 的典型攻击路径为**零点击网页感染**：

1. **流量注入/网站植入**：攻击者通过 ISP 级劫持、DNS 劫持或网站入侵，向**合法网站**注入恶意脚本或重定向代码；
2. **指纹识别**：受害设备加载页面后，套件首先采集 iOS 版本、设备型号等指纹信息，从 5 条利用链中自动匹配适用链路；
3. **浏览器漏洞触发**：利用 WebKit 类型混淆等漏洞获得 Safari 渲染进程代码执行（如 CVE-2024-23222）；
4. **沙箱逃逸与提权**：串联内核漏洞（如 CVE-2023-32434、CVE-2023-38606）突破 WebContent 沙箱，获得内核级读写能力；
5. **内存态植入**：后续阶段完全在内存中执行（无文件驻留），窃取凭据、会话令牌与加密货币钱包数据。

Kaspersky 分析指出：**用户无需任何交互，只需访问被植入代码的页面即会被感染**；且 Coruna 与 DarkSword 均为无文件恶意软件，常驻内存、不落盘，**设备重启后即失效**——这既是其隐蔽性来源，也为防御者提供了低成本的检测/清除手段（详见第六章）。

## 2.4 攻击者与受害情况

Coruna 事件最具标志性的特征是**国家级工具向犯罪生态的泄漏扩散**：

- **UNC6353（俄语系团伙）**：以间谍活动为主要目的，在**乌克兰**境内用于定向情报收集；
- **UNC6691（中文系团伙）**：以经济犯罪为目的，实施大规模感染并**盗取加密货币钱包**；
- **受害规模**：仅**中国**境内被感染设备据估计**不少于 4.2 万台**；实际全球数字可能更高。

iVerify 的溯源分析指出，该套件疑似与美国政府背景的承包商生态存在关联（其技术复杂度、0day 储备与持续维护能力均指向国家级行为者）。无论最终归属如何，**「先军用、后泄漏、再犯罪化」的扩散链条**已经完整发生，这也是本报告将其列为高优先级威胁的原因。

## 2.5 与「三角测量行动」的关系

「三角测量行动」（Operation Triangulation，Kaspersky 2023 年披露）是针对 iOS 的著名零点击攻击行动，利用 CVE-2023-32434（内核）、CVE-2023-38606（ANE/内核）、CVE-2023-41990（FontParser）等漏洞实现完全无交互的设备入侵。Coruna 被多方判定为该技术框架的**升级/复用版本**：

- 复用了相同的内核漏洞（CVE-2023-32434、CVE-2023-38606）；
- 将原本定向、人工运营的攻击流程，**产品化为可自动选择链路的漏洞利用套件**；
- 大幅扩展了漏洞储备（23 个组件 vs 三角测量行动的单条链），覆盖 iOS 13–17.2.1 的宽版本区间。

这一演进代表了国家级 iOS 攻击能力的「工业化」趋势：从手工作战到可批量分发、可多租户运营的工具平台。
# 三、DarkSword 漏洞利用链深度分析

## 3.1 概况与披露时间线

DarkSword 是继 Coruna 之后约两周（2026 年 3 月 18-19 日）由 Google 威胁情报（GTIG）、Lookout 与 iVerify 联合披露的第二套 iOS 漏洞利用工具。Help Net Security 与 CSA 的分析显示，其**在野使用最早可追溯至 2025 年 11 月**。与 Coruna 的「多链武器库」定位不同，DarkSword 是一条针对较新版本 iOS 精心构造的**六漏洞单链全链利用（full-chain）**，攻击能力覆盖从 Safari 远程代码执行到内核读写的完整路径。

关键时间线如下：

1. **2025 年 11 月**：DarkSword 开始在野使用（Help Net Security 援引各厂商研究）；
2. **2026-03-18**：Google 威胁情报发布《The Proliferation of DarkSword》披露分析；
3. **2026-03-19**：Lookout、iVerify 同步发布；Help Net Security、CSA 等跟进报道；
4. **2026 年 3-5 月**：Apple 修复链上漏洞（iOS 18.7.6/26.3.1 及以后版本）；DarkReading 8 月报告其全球扩散态势持续。

## 3.2 六漏洞链与 CVE 清单

DarkSword 将以下 **6 个漏洞串联为单条全链**（各厂商披露口径一致）：

| 序号 | CVE 编号 | 公开披露的角色定位 |
| --- | --- | --- |
| 1 | CVE-2025-31277 | Safari/WebKit 远程代码执行入口 |
| 2 | CVE-2025-43510 | WebKit/渲染进程提权环节 |
| 3 | CVE-2025-43520 | 沙箱逃逸环节（WebGPU 路径） |
| 4 | CVE-2025-43529 | 进程注入/提权环节 |
| 5 | CVE-2025-14174 | 内核权限提升环节 |
| 6 | CVE-2026-20700 | dyld 动态链接器内存破坏（PAC 绕过） |

> 说明：第 6 项 CVE-2026-20700（dyld 内存破坏）由 Apple 于 2026 年 2 月在 iOS 26.3 中修复，且披露时已被发现在野利用（本系列前一报告已收录）；DarkSword 将其纳入链路用于绕过指针认证（PAC）等最新缓解机制。1–5 号 CVE 与利用阶段的具体一一对应关系，公开文献未逐项说明，上表角色定位为各来源描述的整合，仅供参考。

**受影响版本**（各来源口径综合）：iOS 13 至 18.6.2 区间内、且低于 iOS 18.7.5/26.3 的版本均受影响；运行 iOS 18.7.6、26.3.1 及以后版本的设备不受此链影响。

## 3.3 攻击链技术分解

综合 Google 威胁情报与 Help Net Security 的披露，DarkSword 的完整攻击链如下：

1. **恶意 iframe 注入**：被入侵的合法网站向页面注入隐藏 iframe，诱导 Safari 加载攻击者控制的 JavaScript 载荷（零点击触发）；
2. **JSC 远程代码执行**：利用 WebKit/JavaScriptCore 漏洞（CVE-2025-31277 等）在渲染进程中获得任意代码执行；
3. **WebGPU 沙箱逃逸**：借助 WebGPU 相关漏洞突破 WebContent 沙箱，将执行流注入系统守护进程 **mediaplaybackd**；
4. **内核读写**：串联内核漏洞（CVE-2025-14174 等）将权限提升至内核级读写（kernel read/write）；
5. **沙箱策略篡改**：以内核权限修改沙箱配置文件（sandbox profiles）与系统安全策略，解除后续行动的所有限制；
6. **凭据与密钥窃取**：提取密码库（iCloud 钥匙串条目）、会话令牌、**加密货币私钥**等高价值数据并外传。

CSA 研究笔记将其归纳为「**Safari JS 入口 → JSC RCE → GPU 沙箱逃逸 → dyld PAC 绕过 → 内核提权**」五阶段模型；这与上述六步分解一致——第 6 号 CVE（dyld）的角色是在更新版本上绕过 PAC 硬化机制，属于链路的「通行证」环节。

## 3.4 无文件驻留特性

DarkSword 与 Coruna 共享一个重要的工程特征：**全程无文件（fileless）、纯内存执行**。

- 攻击载荷以 JavaScript 形式分发、在内存中解密执行，**不向文件系统写入可落盘样本**；
- 传统基于文件签名的杀毒引擎几乎无法检出；
- **设备重启即彻底清除**，不存在持久化——攻击者如需维持访问，必须重新诱导目标访问恶意页面。

对防御方而言，这一特性是双刃剑：隐蔽性极强，但「每日重启」即可确保清除内存态载荷（详见第七章建议）。

## 3.5 攻击者与受害地区

DarkSword 呈现罕见的**国家级行为者与犯罪团伙共用同一套工具**的格局（Google 威胁情报称之为「proliferation/扩散」）：

- **UNC6353**：俄语系团伙，延续其在乌克兰方向的情报收集活动；
- **UNC6748**：定向攻击团伙，具体归属未完全公开；
- **PARS Defense 客户**：Lookout 观测到该商业防御/情报生态的多个客户在**沙特阿拉伯、土耳其、马来西亚**等地使用 DarkSword 能力；
- **犯罪动机滥用**：与 Coruna 类似，部分攻击以窃取加密货币与凭据变现为目的。

受害观测集中在沙特、土耳其、马来西亚、乌克兰，且攻击目标同时包含政府/军事关联人员与高净值个人——多动机并存的态势与 Coruna 一脉相承。

## 3.6 修复情况

Apple 已通过以下版本修复 DarkSword 链路漏洞：

- **iOS 26.3（2026 年 2 月）**：修复 CVE-2026-20700（dyld）；
- **iOS 18.7.6 / 26.3.1 及以后版本（2026 年 3 月）**：修复链上其余漏洞；
- 运行 iOS 13–16 的老旧设备：Apple 同步为旧分支发布安全更新（与 Coruna 修复波次相近，据 Kaspersky 为 15.8.7、16.7.15 等）。

**尚未升级的设备仍处于可被零点击攻破的风险状态**；各厂商一致建议在无法立即升级时启用锁定模式（Lockdown Mode）作为临时缓解。

# 四、Coruna 与 DarkSword 对比分析

## 4.1 工程架构对比

| 维度 | Coruna | DarkSword |
| --- | --- | --- |
| 架构定位 | 多链漏洞武器库（5 链 23 漏洞） | 单条六漏洞全链（full-chain） |
| 目标版本策略 | 广覆盖：iOS 13–17.2.1 | 高精度：针对 18.x 新版与 PAC 硬化机制 |
| 漏洞储备特征 | 大量复用旧漏洞（含三角测量行动资产） | 以 2025-2026 年新修复漏洞为主 |
| 分发形态 | 套件自动指纹匹配链路 | JavaScript 载荷 + 单链串联 |
| 执行驻留 | 无文件、内存态、重启清除 | 无文件、纯 JS、内存态、重启清除 |
| 沙箱逃逸路径 | 内核漏洞直接提权 | WebGPU 逃逸 + mediaplaybackd 注入 |
| 对新型缓解的应对 | 面向无 PAC/旧机制版本 | 专门绕过 dyld PAC（CVE-2026-20700） |

## 4.2 威胁演化解读

从 Coruna 到 DarkSword 的半年间隔内，可以观察到三个明确的演化方向：

1. **攻击对象前移**：从「仅打旧版本」转向「攻克最新版本与硬件缓解」（DarkSword 对 iOS 18 + PAC 的成功突破说明攻击者对 Apple 最新防线的适应速度已缩短至数月）；
2. **工具形态平台化**：漏洞利用从「行动定制」走向「套件化、可分发、多行为者复用」，攻击门槛从国家级行为者下探至普通犯罪团伙；
3. **泄漏成为主要扩散渠道**：两套工具的犯罪化使用均被判定源于国家生态的泄漏或转售，**供应链侧的「武器泄漏」已成为移动威胁的最大增量来源**。

对企业防御而言，这意味着：不能再以「iOS 用户不会被大规模攻击」的旧假设制定策略，而应按「高价值目标必然被定向攻击、普通用户可能被批量误伤」的双重情景规划防御。
# 五、攻击面与暴露评估

## 5.1 版本长尾是最主要暴露面

Coruna 与 DarkSword 的受影响区间合并后几乎覆盖 iOS 13.0 至 18.6.x 的**全部主流版本**。iOS 设备的版本长尾问题因此被彻底放大：

1. **老旧设备停更风险**：iPhone 系列中无法升级到 iOS 26 的机型（如 iPhone 8/X 一代及更早）停留在旧安全分支，其分支更新（15.8.x/16.7.x）虽在本次事件中得到维护，但未来窗口不可预期；
2. **升级滞后设备**：Kaspersky 估计 2026 年 3 月时约四分之一设备仍运行 iOS 18 或更旧版本——这部分设备同时暴露于 Coruna（旧版链）与 DarkSword（18.x 链）；
3. **企业 MDM 环境的版本碎片化**：受应用兼容性、测试周期与用户习惯影响，企业 fleets 中版本混杂是常态，攻击者仅需一个入口设备即可立足。

## 5.2 WebKit 与浏览器入口

两套工具的初始入口均指向 **WebKit/Safari**（被注入的合法网站 + 零点击触发），原因在于：

- WebKit 攻击面广、渲染管线复杂，历史漏洞密度高（Coruna 的 23 个组件中「大部分」位于 WebKit）；
- 零点击网页触发无需应用分发，绕过了 App Store 审核、企业分发管控等全部传统防线；
- 合法网站被注入（watering hole）后，受害范围以网站访客为界，难以通过「可疑 App」特征预警。

对企业而言，这意味着**仅管控 App 安装（MDM 禁侧载）对这类威胁无效**；浏览器与网络层的监测（代理日志、DNS 异常、证书透明度监控）成为必要补充。

## 5.3 数据暴露优先级

结合两套工具的窃取目标（密码库、会话令牌、加密货币私钥），建议企业按以下优先级评估自身暴露：

| 优先级 | 暴露项 | 风险说明 |
| --- | --- | --- |
| P0 | 加密货币钱包/私钥 App | 直接资产盗取，Coruna/UNC6691 已大规模实施 |
| P0 | iCloud 钥匙串/密码库 | 凭据级联风险：邮箱、VPN、代码仓库随之失守 |
| P1 | 会话令牌与长期登录态 | 绕过 MFA 的横向移动入口 |
| P1 | 企业邮箱/IM 客户端 | 商业邮件欺诈（BEC）与内部情报收集跳板 |
| P2 | 通讯录/相册等 PII | 定向钓鱼素材与合规风险 |

# 六、检测与排查建议

## 6.1 主机侧排查

由于两套工具均为无文件驻留，主机侧取证存在「事后即消失」的天然困难，建议按以下顺序操作：

1. **版本核查（首要）**：通过 MDM 或设置-通用-关于本机，清点全部设备的 iOS 版本；任何低于 iOS 18.7.6/26.3.1（旧分支低于 15.8.7/16.7.15/18.7.7）的设备均视为高风险，立即升级；
2. **重启清洗**：对暂时无法升级的设备执行**立即重启**，确保清除内存态载荷；并将「每日重启」固化为高危人群的临时策略；
3. **异常行为审计**：检查浏览器历史中的非常见站点跳转记录；核对 Apple ID 的登录与同意记录（Settings → Apple ID → Sign-In & Security）；检查面容/触控 ID 是否出现未知生物特征注册；
4. **钱包与凭据应急**：对安装过加密钱包的设备，将其资产**迁移至新密钥**（旧私钥应视为已泄露）；对钥匙串中存储的高价值凭据批量改密。

## 6.2 流量与网关侧检测

- **TLS/代理日志回溯**：检索对已知注入站点或罕见低信誉域名的短时突发访问，尤其是 HTTP 302/iframe 链式跳转模式；
- **DNS 层监控**：对使用企业 DNS/DoH 的设备，标记与加密货币交易所、未知新注册域名相关的解析峰值；
- **CDN/自有站点防注入自查**：企业官网与内部门户应核查供应链脚本（第三方 JS）完整性，部署 SRI（Subresource Integrity）与 CSP（Content Security Policy）防注入。

## 6.3 情报对齐

建议将以下公开情报源纳入威胁情报订阅与检索关键词：Google 威胁情报博客（Coruna、DarkSword 专题）、CISA KEV 目录（Coruna 相关 3 个 CVE 已列入）、Lookout 与 iVerify 的 DarkSword 分析，以及 Kaspersky 对两个套件关联性的综述。

# 七、加固与防御建议

## 7.1 版本升级矩阵

| 当前设备状态 | 目标版本 | 说明 |
| --- | --- | --- |
| 支持 iOS 26 的机型（iPhone 11 及以后） | iOS 26.3.1 或更高 | 完整修复 DarkSword 全链与 Coruna 旧漏洞 |
| 支持 iOS 18 的机型 | iOS 18.7.6 或更高 | 修复 DarkSword 六漏洞链 |
| 仅支持旧分支的机型 | iOS 15.8.7 / 16.7.15 / 17.7.x 分支最新 | 修复 Coruna 利用链（2026-03-11 批次） |
| 已彻底停更的机型 | 停用于敏感业务，或仅限离线/隔离场景 | 无法获得进一步安全维护 |

> 企业环境应通过 MDM 强制最低 OS 版本策略，将上述矩阵转化为合规基线；对无法达标的设备实施条件访问隔离（如禁止访问企业邮件与 VPN）。

## 7.2 系统级缓解机制

1. **锁定模式（Lockdown Mode）**：Apple 官方的高强度攻击面收缩模式，禁用 JIT、复杂文档解析与部分 WebKit 功能。虽然可能导致部分网站功能异常，但对 WebKit 零点击链路是公认有效的降级手段——**无法立即升级的设备应默认开启**；
2. **后台安全改进（Background Security Improvements）**：开启 设置 → 隐私与安全性 → 后台安全改进 的「自动安装」，使 Rapid Security Response（快速安全响应）补丁在无需完整系统更新的情况下自动落地；
3. **每日重启策略**：针对无文件驻留特性，对高管、IT 管理员等高危人群实施每日重启（可结合自动化快捷指令），确保内存态载荷周期性清零。

## 7.3 企业管控补充

- **网络层**：强制企业设备经安全网关/零信任代理出网，启用恶意域名与钓鱼页面拦截；对自有 Web 资产部署 CSP 与 SRI 防止被植入跳板代码；
- **身份层**：为 Apple ID 与核心业务系统启用硬件密钥或高保障 MFA；缩短会话令牌有效期，降低令牌窃取的复用价值；
- **资产层**：将加密货币业务资产与普通办公设备严格隔离；涉密/高管设备的钥匙串策略收紧（禁用明文存储高价值口令）；
- **流程层**：将「iOS 0day 批量披露」纳入应急响应预案（本轮 Coruna/DarkSword 间隔仅两周，传统月度补丁周期明显不足），明确 48 小时内完成 fleet 版本核查与升级的 SLA。

## 7.4 个人用户要点

1. 立即升级至最新版本（详见 7.1 矩阵）；
2. 开启锁定模式与后台安全改进；
3. 不点开来路不明的链接，对「缩短链/重定向」保持警惕（零点击并不等于零成本——注入站点仍需流量入口）；
4. 每日重启设备；
5. 加密货币用户：将大额资产迁移至硬件钱包，App 钱包视为热钱包管理。

# 八、结论与趋势展望

## 8.1 核心结论

1. **Coruna 与 DarkSword 构成 2026 年 iOS 威胁的主线事件**：前者以 23 组件武器库横扫 iOS 13–17.2.1 旧版本长尾，后者以六漏洞全链攻克 iOS 18 + PAC 的最新防线，二者合计覆盖当前几乎全部在网 iOS 版本；
2. **「三角测量行动 → Coruna → DarkSword」的技术谱系已经确立**，国家级 iOS 攻击能力完成了从「行动定制」到「平台化套件」的工业化转型；
3. **泄漏/转售驱动的扩散是本轮威胁最大特征**：同一套工具同时服务情报收集（乌克兰、沙特、土耳其、马来西亚）与金融犯罪（中国 4.2 万台感染、加密货币盗取），防御方必须按「双行为者」模型设计对策；
4. **无文件、零点击、内存态**成为新一代 iOS 攻击的标准工程特征，传统终端查杀基本失效，版本管理、攻击面收缩与周期性重启成为最有效的低成本防线。

## 8.2 趋势展望

1. **武器泄漏常态化**：预计未来将有更多国家储备工具经泄漏/灰色市场流入犯罪生态，「军用级漏洞、民用级攻击」的错位将持续扩大受害面；
2. **补丁竞速窗口收窄**：Coruna（3 月 3 日披露）与 DarkSword（3 月 18 日披露）的密集节奏表明，0day 储备与修复窗口的赛跑已进入「周」级；
3. **防御重心迁移**：随攻击链对 PAC 等硬件缓解的持续绕过，行业防御重心将进一步从「单点漏洞修复」转向「攻击面收缩（锁定模式）+ 快速版本管理 + 假设失陷的监测体系」；
4. **监管与问责强化**：商业间谍软件生态（如 PARS Defense 客户滥用模式）与政府承包商漏洞储备的治理压力将持续上升，企业采购与合规审查需将「间谍软件暴露面」纳入评估项。

# 九、参考资料

## 9.1 一手研究与厂商分析

- Google Cloud Threat Intelligence，"Coruna: The Mysterious Journey of a Powerful iOS Exploit Kit"，2026-03-03 - https://cloud.google.com/blog/topics/threat-intelligence/coruna-powerful-ios-exploit-kit
- Google Cloud Threat Intelligence，"The Proliferation of DarkSword: iOS Exploit Chain Adopted by Multiple Actors"，2026-03-18 - https://cloud.google.com/blog/topics/threat-intelligence/darksword-ios-exploit-chain
- iVerify，"Coruna: Inside the Nation-State-Grade iOS Exploit Kit We've Been Tracking"，2026-03-03 - https://www.iverify.com/blog/coruna-inside-the-nation-state-grade-ios-exploit-kit-we-ve-been-tracking
- Lookout Threat Intelligence，"Attackers Wielding DarkSword Threaten iOS Users"，2026-03-18 - https://www.lookout.com/threat-intelligence/article/darksword
- Kaspersky Blog，"Invincible no more: a look at DarkSword and Coruna"，2026-04-17 - https://www.kaspersky.com/blog/ios-exploits-darksword-and-coruna-in-mass-attacks/55622/

## 9.2 权威汇总与行业报道

- Wikipedia，"Coruna (exploit kit)"条目（CVE 与时间线汇总） - https://en.wikipedia.org/wiki/Coruna_(exploit_kit)
- Cloud Security Alliance，"Full-Chain iOS Zero-Day Exploitation by State Actors"（DarkSword 研究笔记），2026-03-19 - https://labs.cloudsecurityalliance.org/research/csa-research-note-darksword-ios-fullchain-zeroday-multiactor/
- Help Net Security，"DarkSword: Researchers uncover another iOS exploit kit"，2026-03-19 - https://www.helpnetsecurity.com/2026/03/19/darksword-ios-exploit-iphone/
- Malwarebytes，"Apple patches Coruna exploit kit flaws for older iOS versions"，2026-03-12 - https://www.malwarebytes.com/blog/news/2026/03/apple-patches-coruna-exploit-kit-flaws-for-older-ios-versions
- Malwarebytes，"A DarkSword hangs over unpatched iPhones"，2026-03-19 - https://www.malwarebytes.com/blog/mobile/2026/03/a-darksword-hangs-over-unpatched-iphones
- DarkReading，"Coruna, DarkSword iOS Exploits Proliferate Globally"，2026-08-10 - https://www.darkreading.com/vulnerabilities-threats/coruna-darksword-ios-exploits-proliferate-globally
- Holland & Knight，"New iOS Exploit DarkSword and a New Era of Mobile Security"，2026-03-23 - https://www.hklaw.com/en/insights/publications/2026/03/new-ios-exploit-darksword-and-a-new-era-of-mobile-security
- Jamf，"DarkSword iOS Exploit Kit: 3 Lessons for Mobile Security"，2026-04-22 - https://www.jamf.com/blog/darksword-ios-exploit-kit-three-lessons-mobile-security/
- FortiGuard Threat Signal，"DarkSword iOS Exploit Chain"，2026-03-26 - https://www.fortiguard.com/threat-signal-report/6389/darksword-ios-exploit-chain

> 说明：本报告中的 CVE 编号、影响版本与修复版本均以 Google 威胁情报披露、Apple 官方安全公告及 NVD 记录为准；技术机理描述综合上述公开研究文献。各来源对个别版本区间口径略有差异之处，文中已分别标注。

---

咨询ios系统请咨询 telegram：https://t.me/one00190
