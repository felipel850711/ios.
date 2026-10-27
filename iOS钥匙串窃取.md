@@TITLE@@iOS 钥匙串窃取深度分析
@@SUBTITLE@@Keychain 架构、访问控制、提取攻击与防御体系全解构
@@INFO@@分类|漏洞分析
@@INFO@@难度|深度分析
@@INFO@@工具|iOS Keychain 安全研究
@@INFO@@目标|安全研究人员 / iOS 开发者 / 取证从业者
@@INFO@@联系方式|Telegram: https://t.me/one00190
@@TOC@@目录

# 本报告导览

本报告共 19 章 + 3 个附录，面向安全研究人员、iOS 开发者与数字取证从业者，系统性解构 iOS Keychain（钥匙串）的安全模型，并逐层剖析"钥匙串窃取"这一主题下所有已知的攻击面、现实案例与防御手段。

| 章节 | 标题 | 核心问题 |
| --- | --- | --- |
| 第 1 章 | 执行摘要与核心发现 | Keychain 到底能不能被偷？在什么前提下被偷？ |
| 第 2 章 | iOS Keychain 概述 | Keychain 在 iOS 安全体系中扮演什么角色？ |
| 第 3 章 | Keychain 数据模型 | SecItem / kSecClass 五大类如何组织？ |
| 第 4 章 | 架构与实现 | securityd、keychainaccessd、AppleKeychain 框架如何协作？ |
| 第 5 章 | 访问控制与 ACL | kSecAttrAccessible、access group、entitlement 如何鉴权？ |
| 第 6 章 | Secure Enclave 与密钥管理 | 密钥如何与硬件绑定？为什么无法软件导出？ |
| 第 7 章 | Keychain 同步机制 | iCloud Keychain / Keychain Syncing / Passkeys 的信任模型 |
| 第 8 章 | 存储格式与序列化 | keychain-2.db、AK 记录、pdat 密文块的结构 |
| 第 9 章 | 越狱环境攻击 | 越狱后 Keychain 为何形同虚设？ |
| 第 10 章 | 物理攻击与取证提取 | Cellebrite / GrayKey / checkm8 / 备份口令爆破 |
| 第 11 章 | 恶意软件窃取 | Pegasus 等间谍软件如何完成最终采集 |
| 第 12 章 | App 层攻击 | access group 滥用、剪贴板、截图、日志泄露 |
| 第 13 章 | 备份与同步路径 | 加密备份中的 Keychain 究竟能不能解？ |
| 第 14 章 | CVE 与漏洞汇总 | 历史上真实影响 Keychain 的缺陷盘点 |
| 第 15 章 | 典型案例复盘 | San Bernardino、DarkSkiiMmer、iMessage 解密等 |
| 第 16 章 | 安全 API 最佳实践 | 开发者如何正确写 Keychain |
| 第 17 章 | 防御体系 | Apple 的纵深防御工程 |
| 第 18 章 | 未来趋势 | Passkey 时代与后量子迁移 |
| 第 19 章 | 总结与分层建议 | 面向不同角色的可执行清单 |

> **法律与伦理声明**：本报告仅用于授权范围内的安全研究、产品安全设计、数字取证与风险评估。所有攻击技术的描述均停留在原理、机制与公开文献层面，不包含针对非授权目标的实战操作指引。未经授权访问他人设备、窃取他人凭证属于违法行为。

---

## 第 1 章　执行摘要与核心发现

### 1.1 一句话结论

**iOS Keychain 不是一个"可以被远程破解的密码锁"，而是一个"密钥生命周期由硬件与操作系统共同托管的访问控制系统"。** 绝大多数真实世界的钥匙串窃取事件，破解的都不是密码学本身，而是**授权边界**：越狱后的 root 权限、取证厂商的 bootrom 漏洞、间谍软件获得的特权进程、备份口令的社会工程、以及第三方 App 对 access group 的误配。

截至本报告撰写时（2026 年 10 月），全球范围内**尚无公开可信的案例**表明攻击者在保持 iOS 完整更新、未越狱、物理不可接近的前提下，仅凭远程 0-click 漏洞链在数学层面直接分解 Data Protection 的 AES-GCM 密钥层次。所有已知的"钥匙串沦陷"都必须先取得以下四个前置条件之一：

| 前置条件 | 代表技术 | 破坏的安全属性 | 典型成本 |
| --- | --- | --- | --- |
| 设备已获得内核/根权限 | 越狱、exploit chain | OS 级隔离与 ACL 强制 | 中—高（0day 售价百万美元级） |
| 设备物理可控且启动链可劫持 | checkm8、DFU、GrayKey | 硬件绑定密钥的不可导出性之外层 | 高（硬件工具 + 固件回滚） |
| 用户主动或被动交出备份 | iTunes/Finder 加密备份、iCloud 备份 | 备份口令的熵 | 低—中（口令弱则可爆破） |
| 合法/半合法凭证获取 | Apple Support 社工、法院命令、MDM | 密钥托管（escrow）模型 | 极低 |

### 1.2 十大核心发现

**发现 1：Keychain 的安全强度取决于"哪一类保护等级"，而非 Keychain 本身。**
`kSecAttrAccessible` 决定了条目在设备锁定后是否仍可解密。开发者大量使用 `AfterFirstUnlock`（为了后台静默刷新 token），这导致设备重启后只要被解锁一次，密钥材料就在内核中常驻可达。这是 iOS 钥匙串泄露事件中**排名第一的设计错误**，而非 Apple 的 bug。

**发现 2：真正的"Keychain 破解"从来都是"keybag 破解"。**
iOS 的数据保护以 keybag（密钥袋）为枢纽：口令派生密钥（scKey）解锁 default keybag，default keybag 内含各保护类密钥（class key），class key 再包装条目级对称密钥。攻击者的目标始终是拿到 class key 或 default keybag，而不是逐条爆破密文。

**发现 3：`ThisDeviceOnly` 系列是目前最有效的一层"低成本防御"。**
该类条目使用与 SEP 内 UID 绑定的硬件类密钥包装，密钥不出芯片、不进入 escrow、不随备份跨设备迁移。取证工具一旦无法访问 SEP，这类条目在数学上不可解。**但绝大多数第三方 App 从未使用过它。**

**发现 4：越狱等于 Keychain 归零。**
ACL 由 `keychainaccessd` 在用户态强制执行；一旦攻击者获得 root 并可注入/替换进程、读取 entitlement 或直接向守护进程发起带特权属性的 XPC 请求，"哪个 App 拥有哪个 access group"的判断就失去了信任根。所有主流越狱方案（unc0ver、checkra1n、palinps4 等）都自带 `keychain_dump` 类工具。

**发现 5：备份路径是"绕过设备防御"的最短路线。**
iCloud 备份（未开启高级数据保护时）历史上曾包含可用 Apple 服务端密钥解密的数据；iTunes 本地加密备份则把"设备侧的硬件绑定"转化为"一个用户口令的熵"。Elcomsoft / Passware 的核心产品逻辑就是**口令猜测 + 并行加速**，而不是密码分析。

**发现 6：Keychain 同步协议出过严重设计缺陷，Apple 用十年时间修复。**
2014 年公开的 DarkSkiiMmer / iCloud Keychain Sync 分析显示，早期同步协议存在硬编码密钥、弱 IV、可被服务端中间化的问题；后续 Apple 引入基于 Apple ID + 设备密码 + 安全码的 E2EE 信任链，iOS 26 时代进一步把密码与 Passkey 的同步迁移到端到端加密的新架构。

**发现 7：间谍软件"偷 Keychain"是攻击链的最后一步，不是第一步。**
Pegasus 类产品的采集清单里，Keychain 读取通常出现在持久化与提权完成之后，用于收割浏览器密码、iMessage 私钥、WhatsApp 账户密钥、App token。这意味着：**Keychain 的安全性等于整条攻击链的最薄弱环节**。

**发现 8：App 层的"隐性钥匙串泄露"比 Keychain 本身更常见。**
把 token 同时写进 `UserDefaults`、URL Scheme 回调、剪贴板、崩溃日志、APNs payload、网络请求头，是移动端凭证泄露的主流原因。安全团队做移动渗透测试时，Keychain 本身往往"没被抓到"，被抓到的是 Keychain 内容的旁路复制。

**发现 9：Passkey 是对"钥匙串窃取"经济模型的根本性打击。**
凭证不可复制、私钥不出设备（或不出安全飞地）、认证依赖用户在场（bio/passcode），使得"偷到凭证即可复用"的攻击收益归零。这也是 Apple 在 iOS 17/18/26 中持续推动密码迁移的底层逻辑。

**发现 10：生物识别不是"第二因素"，而是"密钥解锁的门闩"。**
`SecAccessControl` 把条目的解密条件绑定到 Touch ID / Face ID / 设备口令的验证状态上，验证发生在 SEP 内部，软件层拿不到"已验证"这个布尔值以外的任何信息。正确配置可显著抬高攻击成本；错误配置（例如只用 `kSecAttrAccessControl` 但未设 `PrivateKeyUsage`）则形同虚设。

### 1.3 风险分级矩阵（攻击面 × 现实可能性）

| 攻击面 | 依赖前置 | 影响范围 | 现实发生频率 | 缓解难度 | 综合风险 |
| --- | --- | --- | --- | --- | --- |
| `AfterFirstUnlock` 滥用 | 一次解锁后的本地访问 | 单 App 全量凭证 | 高 | 低 | ★★★★★ |
| 弱备份口令 + 离线爆破 | 物理接触 + 备份副本 | 全设备（含 Keychain） | 中 | 低 | ★★★★★ |
| 越狱设备上的恶意 profile/插件 | 用户安装越狱 | 全 Keychain | 中（特定人群高） | 中 | ★★★★☆ |
| Access group 配置错误 | 同 Team 内恶意/被控 App | 共享条目组 | 中 | 低 | ★★★★☆ |
| 间谍软件 0-click 链 | 国家级资源 | 全 Keychain | 低（目标性极强） | 高 | ★★★★☆ |
| checkm8 / GrayKey 取证提取 | 物理控制数小时 | 未设 ThisDeviceOnly 的条目 | 中（执法场景） | 高 | ★★★☆☆ |
| iCloud 备份/同步信任链滥用 | Apple 侧配合或账户接管 | 同步类条目 | 低—中 | 中 | ★★★☆☆ |
| Apple Support 社工恢复 | 人工流程缺陷 | 备份与账户 | 中 | 中 | ★★★☆☆ |
| 剪贴板/日志/通知旁路 | 任一读权限的 App 或备份 | 明文凭证 | 高 | 低 | ★★★★☆ |
| 密码学层面直接破解 | 无 | — | 极低（无公开案例） | — | ★☆☆☆☆ |

### 1.4 给三类读者的核心建议（速览）

- **iOS 开发者**：默认 `kSecAttrAccessibleWhenUnlocked`；凭证类必须用 `ThisDeviceOnly`；敏感签名私钥用 `SecAccessControlCreateWithFlags(.biometryCurrentSet, kSecAttrTokenIDSecureEnclave)`；禁止把 Keychain 值二次落地到 `UserDefaults`/文件/剪贴板；关闭备份的条目使用 `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly`。
- **安全研究/红队**：把 Keychain 当作"权限边界"测试而非"算法"测试。优先验证 access group 重叠、卸载残留、`AfterFirstUnlock` 窗口、备份包含性、越狱态提取、生物识别绕过（`kSecUseAuthenticationUI` 误用）。
- **取证从业者**：优先级顺序为——现场解锁内存 → 逻辑/文件系统备份 → 加密备份口令 → checkm8 类 bootrom 路径 → 硬件级（不可行）。全程关注保护类归属，这决定了"能不能拿到"。

### 1.5 本报告术语约定

| 术语 | 含义 |
| --- | --- |
| Keychain / 钥匙串 | iOS 的凭证与密钥存储子系统，含 SecItem API 与后端守护进程 |
| SecItem | Keychain 的数据条目抽象，公开 API 的操作单元 |
| 保护类 / Accessible Class | `kSecAttrAccessible` 指定的解密条件（WhenUnlocked 等） |
| keybag | 密钥袋，存放 class key 的加密容器（system bag / data bag） |
| class key | 某一保护类的对称主密钥 |
| SEP | Secure Enclave Processor，独立安全协处理器 |
| UID key | SEP 内一次性熔丝烧录、永不离开芯片的设备唯一密钥 |
| access group | 允许多 App 共享条目的隔离域，形如 `<TeamID>.<identifier>` |
| AK / AppleKeychain | 私有框架 `AppleKeychain.framework`，iOS 内部条目实现层 |
| keychainaccessd | 钥匙串守护进程，ACL 与保护类强制点 |
| escrow / BAGA | 托管密钥备份与恢复机制（FileVault 语境借用，iOS 侧指 OTA/备份可用的密钥托管） |
| DFU | Device Firmware Update 模式，restore 级固件通道 |

---

## 第 2 章　iOS Keychain 概述

### 2.1 定义：Keychain 是什么

iOS Keychain 是操作系统提供的**受硬件与访问控制双重保护的凭证保管服务**。它承担四类职责：

1. **机密存储**：密码、OAuth token、API key、会话 cookie、设备绑定 secret。
2. **密钥管理**：非对称密钥对（含 SEP 内私钥）、对称密钥、TLS 身份材料。
3. **信任材料**：X.509 证书、身份（identity = 私钥 + 证书 + 元数据）。
4. **跨应用/跨设备共享**：access group 内共享，iCloud Keychain 跨设备同步。

技术上它由三层构成：

```text
┌───────────────────────────────────────────────────────────┐
│ 应用层：Security.framework（SecItem / SecKey / SecAccessControl）│
├───────────────────────────────────────────────────────────┤
│ 服务层：keychainaccessd（ACL、access group、保护类判定、持久化）  │
│        securityd（密码学原语、CSR/PKCS12、部分密钥操作）        │
├───────────────────────────────────────────────────────────┤
│ 内核/硬件层：Data Protection（keybag、class key）＋ SEP（UID/GID）  │
└───────────────────────────────────────────────────────────
```

### 2.2 Keychain 在 iOS 安全体系中的角色

iOS 的安全架构可概括为"五个同心圆"，Keychain 位于最内核：

| 层级 | 名称 | 关键机制 | 与 Keychain 的关系 |
| --- | --- | --- | --- |
| L1 | 硬件信任根 | SEP、UID key、熔丝、量产私钥 | 提供不可导出的根密钥 |
| L2 | 启动链 | Secure Bootchain、AOP、.img4 | 决定能否加载被篡改的内核（越狱入口） |
| L3 | 内核与沙箱 | KTRR/CTRR、PPL/SPTM、APNs 沙箱、MAC 策略 | 阻止未授权进程读 Keychain 文件 |
| L4 | 服务与访问控制 | keychainaccessd ACL、entitlement、access group、生物识别 | 直接实施 Keychain 授权 |
| L5 | 数据保护 | Data Protection class、keybag、per-file key | 决定条目"在什么状态下可解密" |

Keychain 是 L4 与 L5 的交汇点：**它既受访问控制约束（谁能访问），也受数据保护约束（什么时候能访问）**。理解钥匙串窃取，本质就是理解"如何分别绕过这两道约束"。

### 2.3 数据保护体系全景

iOS 数据保护（Data Protection）为文件系统与 Keychain 提供统一的机密性框架：

- **每文件密钥（per-file key）**：每个文件/条目有独立对称密钥，密钥存在文件的元数据扩展属性（`AKFakeSuperDisk`/`xattr`，即 `com.appleprotect.*`）中，被 class key 包装。
- **keybag**：包含若干个 class key 的容器，位于 `/private/var/keybags/`（如 `systembag`、`backupbag`），本身用口令派生密钥（scKey）+ 硬件密钥（UID/GID）包装。
- **口令派生链**：用户 4/6 位数字口令 →（PBKDF2/迭代 + salt）→ class 派生密钥 → 解开 keybag → 得到 class key。SEP 的速率限制与单调计数器使离线爆破只能在设备上进行，除非攻击者掌握 bootrom 级缺陷。
- **抹除（effaceable）**：`effaceable storage` 中的密钥一旦被"抹机"即销毁，实现 1 秒密码学擦除——这也是为什么"远程抹掉"能真正保护数据。

Keychain 条目落盘时的密文块（社区逆向常称为 `pdat`）就是这一框架下的产物：先用条目级 AES 密钥加密，再用保护类密钥包装，因此**"能不能解"取决于保护类，而不是"数据库有没有加密"**。

### 2.4 与 Android Keystore 的对比

| 维度 | iOS Keychain | Android Keystore + EncryptedSharedPreferences |
| --- | --- | --- |
| 定位 | 凭证/密钥统一存储，系统级服务 | Keystore 只管密钥；凭证靠应用自建 |
| 存储引擎 | 系统数据库 + Data Protection | SharedPreferences/文件 + keystore key 加密 |
| 隔离单位 | access group（Team ID + 标识符） | UID/GID + app-specific 沙箱，无原生共享凭证库 |
| 硬件绑定 | SEP + UID key，私钥可完全驻留 SEP | StrongBox（独立 SE）/ 软件 Keystore，机型差异大 |
| 生物绑定 | SecAccessControl（在 SEP 内判定） | `setUserAuthenticationRequired` + BiometricPrompt |
| 锁定后不可读 | 支持（WhenUnlocked 类） | 需自研（密钥在 `until-unlocked` 上无原生等价物） |
| 云同步 | iCloud Keychain，端到端加密 | Google Smart Lock / Passwords，模型不同 |
| Passkey | 系统内建，跨设备同步 | 依赖 GMS Password Manager / Credential Manager |
| 卸载后残留 | **默认残留**（Keychain 不随卸载清除） | 随应用数据删除（Keystore 密钥别名可失效） |
| 越狱/Root 影响 | ACL 完全失效 | Root 后同样失效，但 Magisk 检测生态不同 |
| 取证支持 | Cellebrite/GrayKey/Elcomsoft 全家桶 | 厂商碎片化，提取难度分布广 |

**关键差异（对防御方的启示）**：Android 的隔离默认更"粗"（一个 App 一个沙箱，无共享库），iOS 的隔离更"细"但依赖开发者正确配置 access group 与保护类。而 **iOS 卸载不清 Keychain** 这一特性是独有的坑：企业内部工具、公共设备、二手设备场景下，前一位用户的凭证可被同一 App 的后续安装直接读出。

### 2.5 演进历史

| 版本 | 年代 | 与 Keychain 相关的变化 |
| --- | --- | --- |
| iPhone OS 1.x/2.x | 2007–2008 | 引入 keychain，早期数据库形态简单，越狱社区迅速出现 keychain 提取工具 |
| iPhone OS 3.0 | 2009 | 正式开放第三方 Keychain API（`SecItemAdd` 等），引入 `kSecClass` 体系 |
| iOS 4.0 | 2010 | 引入 `kSecAttrAccessible` 保护类；iMessage/邮件密钥成为研究热点 |
| iOS 5 | 2011 | Data Protection 全量启用；Elcomsoft 等公布"重启后未解锁即可读部分密钥"问题 |
| iOS 7 | 2013 | 64 位 + SEP m7 世代；`ThisDeviceOnly` 类强化 |
| iOS 8 | 2014 | **默认全量文件加密**；Touch ID + `SecAccessControl` 引入，Keychain 条目可要求生物识别 |
| iOS 9 | 2015 | iCloud Keychain 支持应用密码自动填充；HTTPS 相关安全改进 |
| iOS 10 | 2016 | 废弃 `kSecAttrAccessibleAlways*`；引入 `NSFileProtectionCompleteUntilFirstUserAuthentication` 默认策略调整 |
| iOS 11 | 2017 | Keychain 与 AutoFill 深度整合； SEP 与 `kSecAttrTokenIDSecureEnclave` 面向普通密钥 |
| iOS 12 | 2018 | AutoFill 第三方密码管理器接入；Screen Time 数据密钥等新的密钥层次 |
| iOS 13 | 2019 | 引入 **`kSecUseDataProtectionKeychain`**（与 macOS 传统 Keychain 行为解耦），Sign in with Apple 凭证入 Keychain |
| iOS 14 | 2020 | 更严格的数据保护类默认；`SecKey` 支持更多算法 |
| iOS 15 | 2021 | **Passkeys（WebAuthn platform authenticator）** 亮相，凭证模型从"可复制的秘密"转向"不可复制的密钥对" |
| iOS 16 | 2022 | Wi-Fi/E2EE 相关；Secure Enclave 密钥层次随 A16 演进 |
| iOS 17 | 2023 | Passkey 跨设备生成（Nearby）、Apple 内部 Keychain 服务重构；"Stolen Device Protection" 预告 |
| iOS 17.4/18 | 2024 | **Stolen Device Protection（被盗设备保护）**；`Passwords` 独立 App 拆分；`kSecUseDataProtectionKeychain` 行为成为默认讨论焦点 |
| iOS 18.x | 2024–2025 | 端到端加密 iCloud（Advanced Data Protection）覆盖更多类别；Passkey 同步与第三方密码管理器导入 |
| iOS 26 | 2025–2026 | 品牌与架构重组：**Keychain Syncing** 端到端加密范围扩大、密码/Passkey 与 Safari 表单数据分离存储、同步管道迁移至更严格的 E2EE 通道（据公开资料与社区分析） |

### 2.6 "钥匙串窃取"的定义域

为了避免讨论失焦，本报告将"iOS Keychain 窃取"定义为：**攻击者获得了非授权的 Keychain 条目明文（凭证/密钥）或解密能力（class key / keybag）**。据此可分五类：

1. **进程内窃取**：恶意/被劫持 App 通过合法或被错误授权的 access group 读取。
2. **特权窃取**：越狱/内核漏洞后由 root 进程读取全量数据。
3. **离线窃取**：从设备镜像/备份中解出条目（依赖口令或 bootrom）。
4. **同步窃取**：利用云端同步的信任链缺陷、账户接管或 Apple 侧托管密钥。
5. **旁路窃取**：不碰 Keychain，而从其内容的外部复制（内存、日志、网络、剪贴板）中获取等价物。

后文第 9–13 章将按此分类展开。

### 2.7 Keychain 不是什么

一个常见的过度信任：Keychain **不是**：

- 不是 HSM：条目密钥由 OS 管理，OS 被控则保护失效。
- 不是完整性保护：Data Protection 的密文块含认证标签，但访问控制层不防"篡改条目内容"以外的语义攻击。
- 不是 DRM：无法阻止已获 root 的攻击者读取。
- 不是反重放：token 一旦被复制即可被复用。
- 不是隐私沙箱：iCloud Keychain 同步的元数据（域名、时间）在特定配置下可被服务端观测。

明确这些边界，是设计"Keychain 之上业务风控"（设备绑定、DPoP、短期 token、异常检测）的前提。

### 2.8 本章要点

- Keychain 是 iOS 安全同心圆的内核，其强度来自 **SEP 硬件根 + Data Protection 类 + ACL** 三者叠加。
- 真实泄露事件绝大多数源于**保护类误配、备份口令、越狱/0day、同步信任链**，而非密码学被破。
- `ThisDeviceOnly` 与 `WhenPasscodeSetThisDeviceOnly` 是开发者最容易被忽视的两道"硬件级护栏"。
- 卸载不清 Keychain、跨 App 的 access group 共享，是 iOS 独有的两个"隐性持久化"来源。

---

## 第 3 章　Keychain 数据模型

### 3.1 SecItem：一切操作的单元

公开 API 以"类（class）+ 属性（attributes）+ 数据（data）"三元组描述一个条目：

```swift
// 典型写入：generic password
let query: [String: Any] = [
    kSecClass as String: kSecClassGenericPassword,
    kSecAttrService as String: "com.acme.api",
    kSecAttrAccount as String: userID,
    kSecUseDataProtectionKeychain as String: kCFBooleanTrue!,   // iOS 13+ 推荐
    kSecAttrAccessible as String: kSecAttrAccessibleWhenUnlocked,
    kSecValueData as String: tokenData,
    kSecUseAuthenticationContext as String: laContext           // 可选：绑定生物识别
]
let status = SecItemAdd(query as CFDictionary, nil)
```

三个设计要点直接影响安全性：

1. **`kSecUseDataProtectionKeychain`**：告诉系统"用 Data Protection 后端的 Keychain"，与 macOS 遗留的文件型 keychain 行为分离。iOS 上一般由系统自行选择，但该 flag 在跨平台代码里决定了 ACL 语义差异（尤其是 `kSecUseAuthenticationUI` 与生物识别提示行为）。
2. **`kSecAttrAccessible` 是条目属性而非调用参数**：写入时确定，读取时无法临时"降级"，避免运行时被诱导放宽。
3. **主键（primary key）唯一性**：同一 class 下由 `service + account + accessGroup(+ SVP)` 唯一确定，重复 `SecItemAdd` 返回 `errSecDuplicateItem`。攻击者可通过枚举主键探测存在性（见第 12 章）。

### 3.2 五大 kSecClass

| Class | 用途 | 典型内容 | 常见属性 | 泄露影响 |
| --- | --- | --- | --- | --- |
| `kSecClassGenericPassword` | 第三方 App 通用秘密 | OAuth token、API key、口令 | service、account、generic(password) | 账户接管 |
| `kSecClassInternetPassword` | 网络/表单凭证 | HTTP Basic、网站密码、Wi-Fi/EAP 凭据 | server、protocol、path、port、domain | 账户接管、流量解密（若为代理证书口令） |
| `kSecClassKey` | 密钥对象 | 对称密钥、SEP 私钥、Passkey 私钥、CryptoKit key | keyClass、keySizeBits、applicationTag、tokenID | 签名伪造、通信解密、设备绑定失效 |
| `kSecClassCertificate` | X.509 证书 | TLS 客户端证书、MDM 证书、根证书 | issuer、serialNumber、subjectKeyIdentifier | 一般为公开信息，但可辅助身份伪造链 |
| `kSecClassIdentity` | 私钥 + 证书组合 | APNs/MDM 客户端身份、VPN 身份 | 派生自 cert + key | 设备身份冒用、VPN/MDM 越权 |

补充说明：

- **Passkey** 在内部并非新 class，而是以 `kSecClassKey` + 认证器元数据的组合形态存在（不同 iOS 版本细节不同），公开 API 由 AuthenticationServices 封装。
- `kSecClassKey` 的 `kSecAttrTokenID = kSecAttrTokenIDSecureEnclave` 决定了私钥"永不导出"，这是唯一在密码学层面真正抗离线提取的类别。
- iOS 还有若干不公开或半公开的 class 值（如系统内部的 passcode/网络配置条目），取证工具在镜像中常能见到第三方 App 看不到的内部条目。

### 3.3 属性体系分层

属性可划分为四组，理解分组有助于分析"哪些属性是索引（明文可见）、哪些是秘密（加密存储）"：

| 分组 | 属性示例 | 是否作为明文字段落盘 | 攻击意义 |
| --- | --- | --- | --- |
| 主键类 | `kSecAttrService`、`kSecAttrAccount`、`kSecAttrServer`、`kSecAttrPath` | 多为可检索字段（明文/弱混淆） | 枚举探测、用户行为画像（"存了哪些站点"） |
| 隔离类 | `kSecAttrAccessGroup`、`kSecAttrSynchronizable` | 明文 | ACL 绕过测试、同步泄露面 |
| 保护类 | `kSecAttrAccessible`、`kSecAttrAccessControl` | 元数据（含类标识） | 决定取证可行性 |
| 载荷类 | `kSecValueData`、`kSecValueKeychainItemData`、`kSecAttrApplicationData` | **加密**（pdat 块） | 真正的窃取目标 |

**核心洞察**：Keychain 的"索引字段"是弱保护的。这意味着攻击者即使拿不到明文载荷，也可能从元数据中恢复出高价值情报（用户安装了哪些银行 App、登录过哪些站点、拥有哪些证书）。取证场景中，元数据本身就常构成证据。

### 3.4 Generic Password 深度剖析

Generic password 是第三方 App 用得最多、也最容易被误用的一类。

```swift
// 推荐的 generic password 结构
[
  kSecClass: kSecClassGenericPassword,
  kSecAttrService: "com.acme.app/session",      // 稳定的逻辑名，不要用随机值
  kSecAttrAccount: "user:\(userId)",             // 便于多账号隔离
  kSecAttrGeneric: appSpecificTag,               // 自定义元数据（会随条目持久化）
  kSecAttrAccessGroup: "\(teamID).shared",       // 明确写出，不要依赖默认
  kSecAttrAccessible: kSecAttrAccessibleWhenUnlocked,
  kSecValueData: data
]
```

安全要点：

1. **`service` 与 `account` 不加密**：不要在 account 中放邮箱/手机号等 PII，除非接受其在镜像中明文可见（企业设备取证场景）。
2. **`kSecAttrGeneric` 常被忽略**：它也是元数据字段，攻击者/取证方能读到；只放不敏感标签。
3. **不要用它存"可跨设备复用的长期 secret"**：一旦同步（`kSecAttrSynchronizable`）就扩大了泄露面。

### 3.5 Internet Password 深度剖析

Internet password 携带 URL 语义，是 AutoFill/Safari 表单填充的基础，也是"钓鱼型窃取"的载体。

| 属性 | 语义 | 风险面 |
| --- | --- | --- |
| `kSecAttrServer` | 主机名 | 通配符/宽松匹配可能导致跨子域误用 |
| `kSecAttrProtocol` | `http`/`https`/`ftp`/`smtp`/`socks` 等 | 混合协议条目可能被降级使用 |
| `kSecAttrPort`、`kSecAttrPath` | 端口/路径 | 精细匹配可减少误用 |
| `kSecAttrDomain`、`kSecAttrAccount` | 域/账号 | 与自动填充隐私相关 |
| `kSecAttrSecurityDomain` | 安全域 | 企业 SSO 场景常见 |

真实问题示例：某些 App 把自建 API 的 Basic 凭证以 `kSecClassInternetPassword` + `server=api.acme.com` 保存，同时该条目标记为 `synchronizable`，导致同一 Apple ID 下的 Mac/iPad 都持有该 secret；若其中一台设备失陷，凭证泄露面即扩大至整个账户生态。

### 3.6 Key 类与 CryptoKit / SecKey 协作

`kSecClassKey` 有两种本质不同的存在形式：

| 形式 | `kSecAttrTokenID` | 私钥位置 | 抗离线提取 | 典型用途 |
| --- | --- | --- | --- | --- |
| 软件密钥 | 无 | Keychain 密文块中（受 class key 保护） | 弱（越狱/取证可取） | 数据加密 key、JWT 签名 key |
| SEP 密钥 | `kSecAttrTokenIDSecureEnclave` | Secure Enclave 内部 | **强**（密钥不出芯片） | Passkey、Apple Pay、安全 enclave attestation、MDM 身份 |

SEP 密钥的"引用"（一段 opaque token/blob）存在于 Keychain 中，真正的私钥材料保存在 SEP 的持久化密钥存储里。即使完整镜像设备闪存，攻击者得到的也只是"引用"，无法离线签名——除非同时攻破 SEP（目前公开研究集中在故障注入/SFI 缺陷，成本极高）。

```swift
// 生成 SEP 内的 EC 私钥，并要求每次签名都要生物识别
let access = try SecAccessControlCreateWithFlags(
    kCFAllocatorDefault,
    kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly,
    [.userPresence, .biometryCurrentSet],   // 生物特征集合变更即失效
    &err
)!
let attrs: [String: Any] = [
    kSecClass: kSecClassKey,
    kSecAttrKeyType: kSecAttrKeyTypeECSECPrimeRandom,
    kSecAttrKeySizeInBits: 256,
    kSecAttrTokenID: kSecAttrTokenIDSecureEnclave,
    kSecAttrApplicationTag: tag,
    kSecPrivateKeyAttrs: [
        kSecAttrIsPermanent: true,
        kSecAttrAccessControl: access
    ]
]
let key = SecKeyCreateRandomKey(attrs as CFDictionary, &err)
```

注意 `.biometryCurrentSet` 与 `.biometryAny` 的语义差异：前者在用户新增/删除指纹（或面容）时使条目失效，后者仍可用——**金融/密钥恢复类场景必须用 `.biometryCurrentSet`**。

### 3.7 Certificate 与 Identity

- `kSecClassCertificate` 存 DER 证书。证书公开本身风险低，但它是**攻击面的"地图"**：从证书链可推断企业 MDM、内部 CA、VPN 配置。
- `kSecClassIdentity` 把私钥与证书绑定，用于 TLS 客户端认证（APNs 设备身份、MDM Enrollment、企业 VPN）。Identity 泄露 = 设备身份可被冒用，风险等级等同于私钥泄露；若私钥部分实际驻留 SEP，则 Identity 只是引用，风险显著降低。

**iOS 独有的坑**：`SecIdentityCopyCertificate` 等操作无需生物识别授权即可读取证书部分；但 `SecKeyCreateSignature` 需要满足 access control。攻击者拿到镜像后能"看清身份结构"，却未必能"用它签名"——这类差异要在威胁建模中明确区分。

### 3.8 查询与检索语义

| 操作 | 函数 | 关键 flag | 安全注意点 |
| --- | --- | --- | --- |
| 增 | `SecItemAdd` | `returnRepresentation` | 冲突/覆盖策略 |
| 查 | `SecItemCopyMatching` | `matchLimit`、`returnAttributes` | 只取属性可避免解密（元数据探测） |
| 改 | `SecItemUpdate` | — | 更新不改变 accessible 类需显式指定 |
| 删 | `SecItemDelete` | `matchLimitAll` | 卸载 App 不会调用它（残留风险） |
| 存在性 | `SecItemCopyMatching` + 不取数据 | — | 构成侧信道（见 3.9） |

`errSecInteractionNotAllowed`（-25308）与 `errSecMissingEntitlement`（-34018）是两个极具取证价值的返回码：前者说明"ACL 通过但设备锁定/保护类不允许"，后者说明"entitlement/access group 不匹配"。在红队测试中，这两个错误码能直接暴露条目的保护类归属。

### 3.9 元数据侧信道

由于索引字段明文/弱保护，可以构造以下侧信道：

1. **安装画像探测**：恶意 App 若与目标 App 同 Team（企业签名或同一开发者的马甲包），可尝试 `SecItemCopyMatching` 只取 `kSecAttrService` 列表，直接枚举用户账号。
2. **存在性预言机**：即使 ACL 拒绝，不同错误码（duplicate vs missingEntitlement）也可能泄漏条目是否存在（同一 access group 内）。
3. **时间侧信道**：需要解密（SEP 操作 / 生物识别等待）与只读元数据的响应时间差异显著，可被远端页面（通过已授权的扩展）间接观测。

**缓解**：不要在 `account`/`generic` 放可识别 PII；对高价值 App 使用唯一随机 `service` 名并在服务端映射；定期清理未使用条目。

### 3.10 条目生命周期与残留

```text
安装 → SecItemAdd → 使用（可能同步） → 用户卸载 App
                                        ↓
                          Bundle 容器删除，但 Keychain 条目仍在！
                                        ↓
                   重装同一 Team/App → 直接读到旧 token（无需任何认证）
```

这一"卸载不清理"的设计源自 macOS Keychain 的传统（凭证与 App 生命周期解耦），在以下场景直接构成漏洞：

- 公共/共享设备（零售 POS、医院 iPad、租赁设备）。
- 员工离职交接：重装 App 即继承前用户会话。
- 越狱市场/二手设备：App 的会话 token 完整可用。
- 多账号 App：删除账号 A 未删除其 Keychain 条目。

**工程对策**：
- App 内实现显式"清除凭证"逻辑（`SecItemDelete` with `matchLimitAll` + access group 遍历）。
- 服务端配合：token 绑定设备指纹/attestation，短期化（TTL + refresh），使 Keychain 残留失去价值。
- 企业 MDM：使用"抹机"而非"删 App"来清除 Data Protection 密钥。

### 3.11 数据模型层的攻击面清单

| 攻击面 | 说明 | 对应章节 |
| --- | --- | --- |
| 主键唯一性探测 | 枚举 service/account 判断账号存在 | 3.9 |
| 元数据明文 | 索引字段泄露行为画像 | 3.3、10 |
| accessible 误配 | AfterFirstUnlock 窗口 | 5、9 |
| synchronizable 误配 | 秘密进入同步管道 | 7 |
| access group 重叠 | 同 Team 恶意 App 读取 | 5、12 |
| 卸载残留 | 旧 token 被重装继承 | 3.10 |
| 软件密钥导出 | 镜像 + class key 解密 | 8、10 |
| SEP 引用误用 | 把"引用"当"密钥"评估风险 | 3.6、6 |
| Identity 组合泄露 | 客户端证书身份冒用 | 3.7 |

### 3.12 本章要点

- Keychain 的**元数据与载荷保护强度不对等**，元数据是常被忽视的泄露面。
- 五大 class 中，只有 `kSecClassKey` + `kSecAttrTokenIDSecureEnclave` 提供密码学意义上的"不可导出"。
- `kSecAttrAccessible` 与 `kSecAttrAccessControl` 是两条独立但常被混淆的正交轴：前者管"设备状态"，后者管"用户在场"。
- 卸载残留与账号删除不彻底，是 App 层最普遍的"凭证永生"问题。

---

## 第 4 章　Keychain 架构与实现

### 4.1 进程拓扑

```text
 ┌──────────── App (sandboxed) ─────────────┐
 │ Security.framework: SecItem* API          │
 │   └ AppleKeychain / 私有框架封装            │
 └───────────────┬───────────────────────────┘
                 │ XPC (Mach 消息)
   ┌─────────────┴──────────────┐
   │ keychainaccessd            │  ← ACL / access group / accessible 判定
   │  /usr/libexec/             │  ← 条目持久化、同步队列调度
   └──────┬──────────────┬──────┘
          │              │
 ┌────────┴───┐   ┌──────┴────────────┐
 │ securityd  │   │ accountsd / akd    │ ← Apple ID / iCloud Keychain 信任
 │ 密码学原语   │   │ AuthKit(AK*)       │
 │ SecKey/CSR │   └──────┬────────────┘
 └────────┬───┘          │ CloudKit / APNs
          │              ↓
   ┌──────┴───────────────────────────┐
   │ kernel: AppleCredentialManager / │
   │ Data Protection (keybag)          │
   └──────────────┬───────────────────┘
                  │ IPC / SEP 邮箱
          ┌───────┴────────┐
          │ Secure Enclave │  UID key、class key、SEP 密钥、生物特征匹配
          └────────────────┘
```

要点：

1. **App 不直接接触 Keychain 文件**。所有读写都经 XPC 到 `keychainaccessd`，由守护进程依据调用者的 **CSMSBlob（代码签名信息）**、entitlement 与请求参数做判定。这是 iOS 与 macOS（旧版可直接读 keychain 文件）最大的架构差异。
2. **守护进程判定的是"调用者身份"**：一旦攻击者能影响调用者身份（注入进程、替换 entitlement、利用 `task_for_pid`、或 root 直接发 XPC），ACL 即刻失效。
3. **securityd 与 keychainaccessd 的分工**历史上多次调整：前者偏"密码学服务"（含 SEP 交互代理），后者偏"凭证数据库"。理解这一点对定位 bug 组件（CVE 归类）很重要。

### 4.2 keychainaccessd 内部

从公开逆向与崩溃符号可见的核心对象模型（命名以私有框架 `AppleKeychain.framework` 的 `AK*` 前缀为主）：

| 组件 | 职责 |
| --- | --- |
| `AKItem` / `AKItemClass` | 条目抽象与 class 定义（genp/inet/cert/keys/idnt） |
| `AKSearch` / `AKPersistentSearch` | 查询编译、结果集构造 |
| `AKDatabase` / 存储后端 | SQLite 数据库封装、事务、索引 |
| `AKACL` / 权限判定 | entitlement 与 access group 比对、SHA-1 subject 校验 |
| `AKSyncEngine` | iCloud Keychain 同步状态机、变更集生成与合并 |
| `AKChangeSet` / `AKZone`（CloudKit 语境） | 记录变更与分区 |
| `AKBag` / keybag 交互层 | 调用 Data Protection 完成加解密 |

**ACL 判定的实际输入**（简化）：

```text
allow = (requestedAccessGroup ∈ callerEntitlement.accessGroups)
     && (caller.appID/teamID 匹配条目分区的 subject)
     && (系统 ACL 记录中的 partition 列表包含该 subject 或存在 root/特权豁免)
     && (设备状态满足 kSecAttrAccessible 条件)
     && (生物识别/passcode 条件满足 kSecAttrAccessControl)
```

### 4.3 存储位置与文件布局

| 路径 | 内容 | 保护类 | 备注 |
| --- | --- | --- | --- |
| `/private/var/Keychains/keychain-2.db` | 条目数据库（SQLite） | AfterFirstUnlock（受保护文件） | 核心目标；载荷字段为密文 |
| `/private/var/Keychains/keychain-2.db-wal` / `-shm` | WAL 与共享内存 | 同上 | 取证常含未合并的最新变更 |
| `/private/var/keybags/systembag`（命名随版本变化） | class key 容器 | 硬件+口令包装 | 解不开它，条目就是砖 |
| `/private/var/root/Library/Keychains/` | 系统/守护进程自用 | 视条目 | 含较敏感系统条目 |
| `/private/var/mobile/Library/Keychains/` | 用户级私有 Keychain 材料 | 视条目 | iMessage/身份服务等 |
| `/private/var/mobile/Library/Accounts/`、`CloudKit/` | 账号与同步缓存 | Data Protection | 同步攻击面 |
| `/private/var/protected/`（示例） | 受保护子系统 | 强保护 | 与 Data Protection 关联 |

> 说明：不同 iOS 版本目录名存在差异（例如早期 `keychain.sqlite`）。本报告以近年版本为参考，实际取证需按镜像逐一确认。

### 4.4 SQLite 后端与 WAL

`keychain-2.db` 是**明文 SQLite 结构 + 字段级密文**。这意味着：

- 拷贝数据库文件即可得到"全部条目的元数据视图"（在有文件系统读取权限时）。
- 载荷列（社区常称 `pdat`/`mdat` 一类）是密文，需要 Data Protection 上下文才能解。
- WAL 中可能残留已被覆盖/删除的旧值，构成**"删除后仍可恢复"的时间机器效应**（取证与攻击双用途）。

这解释了一个看似矛盾的现象：**"Keychain 数据库没有整体加密，但依然安全"**——因为安全性来自内核级 Data Protection 对单个 BLOB 的密钥包装，而非数据库层加密。第 8 章将展开格式细节。

### 4.5 AK（AppleKeychain）演进

| 阶段 | 特征 |
| --- | --- |
| 早期（iPhone OS 2–4） | 简单表结构，保护类概念刚引入，越狱工具可直接 dump |
| iOS 5–8 | 引入 Data Protection 全量、`ThisDeviceOnly`、access group 收紧 |
| iOS 9–12 | `AppleKeychain.framework` 化，AK 对象体系成熟；iCloud Keychain 同步强化 |
| iOS 13–16 | `kSecUseDataProtectionKeychain` 引入；私有 API 分层，XPC 协议演进；Passkey 预备 |
| iOS 17–18 | 密码/Passkey 分离与重构，Passwords App 独立；Stolen Device Protection |
| iOS 26 | 同步管道与 E2EE 范围调整（据公开资料与社区分析），凭证域进一步细分 |

AK 演进的主线可以概括为一句话：**把"秘密"从可搬运的数据，逐步变成绑定硬件与用户在场的状态**。

### 4.6 Keychain Sharing（共享钥匙串）

`keychain-access-groups` entitlement 允许多个 App 声明同一 access group，从而共享条目：

```
<TeamID>.<group-identifier>
例：AB12CD34EF.com.acme.shared
特殊：<TeamID>.com.apple.icloud（同步组，需额外授权）
```

规则要点：

1. **默认组 = App 自身的 App ID 前缀（Team ID）+ bundle id 派生**，不显式声明时最隔离。
2. **组前缀的 Team ID 必须与调用者的签名 Team ID 一致**，因此共享天然被限制在同一开发者账号（或企业账号）内。
3. 通过 **App Groups / data protection 协同**可在扩展、Widget、主 App 之间传递凭证（注意：App Group 容器本身**不是** Keychain，把 token 复制到共享容器等于放弃了 Keychain 的保护类）。
4. 系统级特殊组（如 `com.apple.*`）只对持有相应 entitlement 的 Apple 组件开放。

**攻击面**：

- 同一 Team 下的任意 App（含被入侵的马甲包、旧版本、企业分发版）都能读同组条目。
- 证书/描述文件泄露导致攻击者能以相同 Team ID 签名恶意 App，继承共享组。
- 越狱/漏洞获得的 entitlement 伪造（历史上多种工具通过注入带 `get-task-allow` 或特殊 entitlement 的进程实现"变成任意 App"）。

第 5 章与第 12 章将分别展开 ACL 判定与滥用实例。

### 4.7 XPC 协议与信任边界

调用链信任传递的关键在于"守护进程如何确认对端身份"：

| 身份来源 | 说明 | 可被破坏的方式 |
| --- | --- | --- |
| `audit_token` / pid | 内核提供的调用者凭证 | pid 复用竞态（历史 TOCTOU 类 bug） |
| CSMSBlob（代码签名） | 从内核取得的签名信息 | 无签名/临时签名越狱环境、绕过 AMFI |
| Entitlements | 签名内嵌的权限声明 | 签名校验被绕过、任务注入 |
| Sandbox | 网络/文件/进程权限 | sandbox escape |
| 用户态 UI 判定 | 生物识别/passcode 通过 | Face ID 镜像攻击、UI 欺骗 |

因此，Keychain 的实战安全性 = **AMFI + 沙箱 + 代码签名链路 + SEP** 的联合强度。任何一环被 0day 击穿，Keychain 就退化为"一个带索引的密文库"，其剩下的保护只有保护类（而保护类在 root + 已解锁设备下同样可解）。

### 4.8 与 macOS Keychain 的差异

| 维度 | iOS | macOS |
| --- | --- | --- |
| 访问方式 | 必须经守护进程 XPC | 传统上可直接读文件（`/Users/x/Library/Keychains/login.keychain-db`） |
| 权限模型 | 代码签名 + access group + 沙箱 | ACL（应用列表）+ 登录密钥 + 文件权限 |
| 主密钥 | 用户口令 + SEP 硬件 | 登录口令 + iCloud 托管密钥（"默认密钥链"） |
| 弹窗授权 | 无（由签名决定） | 有："xxx 想使用您在钥匙串中存储的…" |
| 典型窃取工具 | 越狱 keychain dump、备份解析 | keychaindump、mKeychainExtract、`security dump-keychain` |
| 数据保护类 | 8 种（含系统内部类） | 概念类似但呈现方式不同（"仅本机" / "iCloud"） |

理解 macOS 侧的对照，有助于跨平台产品（浏览器、密码管理器、VPN）统一设计密钥策略；同时说明 iOS 的"无 UI 授权"是把安全性完全押在签名与 entitlement 链上——这也正是间谍软件偏爱 iOS 的原因之一。

### 4.9 架构层攻击面矩阵

| 攻击位置 | 手段类别 | 后果 | 现实案例参考 |
| --- | --- | --- | --- |
| App 进程 | 同组 access group / 被注入 | 读同组条目 | 多起企业签名工具滥用 |
| XPC 调用者身份 | pid/audit_token TOCTOU、任务端口窃取 | 伪装身份 | 历史 sandbox/权限类 CVE |
| keychainaccessd | 内存破坏、逻辑缺陷 | 任意读取或写入 | 私有守护进程研究 |
| SQLite 文件 | root 直接读取 + class key | 全量离线解密 | 越狱取证、Cellebrite |
| keybag | 口令爆破、bootrom 提取 | 所有非 ThisDeviceOnly 条目 | GrayKey/checkm8 路线 |
| SEP | 硬件接口、故障注入 | 长期看是最后防线 | 学术侧信道研究 |
| 同步引擎 | 变更集伪造、账户接管 | 云端扩散 | DarkSkiiMmer（历史） |

### 4.10 本章要点

- iOS Keychain 的**强制点在用户态守护进程 + 内核数据保护**两处，二者都不是密码学防线，而是"身份 + 状态"防线。
- `keychain-2.db` 结构明文、载荷密文，元数据泄露本身就是风险。
- Keychain Sharing 把安全边界收敛到 **Team ID**，一个账号的失陷等于一组 App 的失陷。
- AK/AppleKeychain 的十年演进方向是"去可移植化"：硬件绑定、用户在场、端到端同步加密。

---

## 第 5 章　访问控制与 ACL

### 5.1 两条正交的授权轴

iOS Keychain 的访问控制由两个正交机制组成，任何一条被绕过都构成泄露：

```text
        何时可解密（When） ─── kSecAttrAccessible（保护类 / Data Protection）
             │
  条目 ──────┤
             │
        谁能解密（Who） ──── access group / entitlement / ACL / SecAccessControl
```

实践中大量事故来自把两者当成一件事：以为"用了生物识别就安全"，却把条目存在 `AfterFirstUnlock`；或反之。

### 5.2 kSecAttrAccessible：8 种保护类

| # | 常量 | 设备锁定时可读 | 重启后首次解锁前可读 | 可跨设备恢复/同步 | 是否进备份 | 典型用途 | 风险评级 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | `kSecAttrAccessibleWhenUnlocked` | 否 | 否 | 可 | 可 | 会话 token、需即时使用的秘密 | 低 |
| 2 | `kSecAttrAccessibleWhenUnlockedThisDeviceOnly` | 否 | 否 | **否** | 本机备份可用 | 高价值本地 secret | 最低 |
| 3 | `kSecAttrAccessibleAfterFirstUnlock` | **是** | 否 | 可 | 可 | 后台静默刷新（Push/后台任务） | 中 |
| 4 | `kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly` | **是** | 否 | **否** | 本机 | 后台任务 + 不迁移 | 低—中 |
| 5 | `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly` | 否（且要求已设口令） | 否 | **否** | **不进备份**（口令移除即销毁） | 极敏感（密码、私钥） | 最低 |
| 6 | `kSecAttrAccessibleAlways` | **是（重启后也是）** | **是** | 可 | 可 | 已废弃（iOS 10） | 极高 |
| 7 | `kSecAttrAccessibleAlwaysThisDeviceOnly` | 是 | 是 | 否 | 可 | 已废弃（iOS 10） | 极高 |
| 8 | 内部 Class 0（`NSFileProtectionNone` 语义） | 是 | 是 | — | — | 仅系统组件（启动必需数据） | App 不可用 |

> 说明：公开 API 暴露 7 个 `kSecAttrAccessible` 常量；连同系统内部使用的无保护类（对应文件保护 Class 0）通常被并列为"8 种保护类"。部分第三方资料会把 `Synchronizable` 变体单列，属分类口径差异。

### 5.3 保护类与内核数据保护类的映射

| kSecAttrAccessible | 大致对应的文件保护类 | 密钥依赖 |
| --- | --- | --- |
| WhenUnlocked | Class 1（Complete） | 需解锁（passcode 派生） |
| WhenUnlockedThisDeviceOnly | Class 1 硬件绑定（Complete + HW） | 额外需要 SEP 内 UID 派生 |
| AfterFirstUnlock | Class 2（UntilFirstAuth） | 首次解锁后在内核常驻 |
| AfterFirstUnlockThisDeviceOnly | Class 2 硬件绑定 | UID 派生 |
| WhenPasscodeSetThisDeviceOnly | Class 4 变体（CompleteUntilUserAuthentication + HW） | 口令存在且用户在场 |
| Always（废弃） | Class 0/近无保护 | 无 |

**取证含义**：Class 2 类条目在"设备已开机且至少解锁过一次"时即可解密；Class 1 需要"当前处于解锁状态"；硬件绑定类需要 SEP。这直接决定了 Cellebrite/GrayKey 在不同设备状态下的产出差异，也是执法场景中最关键的技术分水岭。

### 5.4 Keychain ACL 记录

条目在数据库层带有 ACL 性质的元数据，公开逆向资料中常见的要素包括：

- **partition / subject 列表**：允许访问该条目的 App 标识集合，通常由 CD Hash 或 App ID 派生（研究资料中常描述为存储 **SHA-1 摘要**形式的 subject），以及 Team ID 派生的组标识。
- **可同步标志**：标记条目是否属于 iCloud Keychain 变更集范围。
- **创建/修改时间戳与版本**：用于同步冲突合并（`crtr`/`mdat`/`tver` 一类字段，具体命名随版本变化）。
- **保护类标签**：指示解密所需的 class key。
- **access control blob**：绑定生物识别/passcode 的策略对象。

要点：**subject 使用摘要而非明文 bundle id**，是为了避免数据库里直接可读"谁拥有这条记录"，但摘要仍可在给定签名信息时离线计算——这为越狱态/镜像态的工具提供了"构造合法请求"的能力。

### 5.5 entitlement 与 access group 判定流程

```text
调用 SecItemAdd(accessGroup: "AB12CD34EF.com.acme.shared")
   ↓
keychainaccessd 取得调用者 audit_token
   ↓
内核返回 CSMSBlob：签名 Team ID、App ID、entitlements
   ↓
检查 keychain-access-groups 是否包含请求的组
   ↓  (不包含 → errSecMissingEntitlement -34018)
检查该组的 Team ID 前缀 == 调用者 Team ID
   ↓  (不等 → 拒绝)
写入条目，subject/partition 记录调用者身份
   ↓
后续读取：调用者身份必须落在 partition 集合内
```

**绕过判定**的现实路径（防御视角列举，非操作指引）：

1. 获得 root 后直接向守护进程发起请求，或以特权身份读取库文件（越狱）。
2. 伪造/借用 entitlement：在签名校验链被破坏的环境下（AMFI 绕过）加载带有目标组声明的 App。
3. 攻陷同 Team 的任意一个 App（供应链、旧版本、马甲包）。
4. 利用守护进程/框架逻辑缺陷（历史 CVE），使身份判定与请求参数出现不一致。

### 5.6 App Groups 与 Keychain 的常见混淆

| 机制 | 用途 | 是否保护秘密 | 误用后果 |
| --- | --- | --- | --- |
| Keychain + access group | 跨 App/扩展共享凭证 | 是（受保护类约束） | 需正确声明组 |
| App Group 容器（`group.*`） | 共享文件/UserDefaults | **否**（仅沙箱边界放宽） | 把 token 放进共享容器 = 明文落地 |
| `NSUserDefaults(suiteName:)` | 偏好共享 | **否** | 常见泄露点 |
| 文件 + Data Protection(`NSFileProtectionComplete`) | 受保护文件 | 是（但需正确设置类） | 默认类为 `UntilFirstUserAuthentication`，风险高 |
| 剪贴板 / 通知 / URL Scheme | 临时传递 | **否** | 高频泄露渠道 |

**最佳实践**：跨扩展共享秘密一律走 Keychain access group，不要用 App Group 容器传 token。若必须传大对象（如导出的证书包），使用带 `NSFileProtectionComplete` 的容器文件并在用后立即删除。

### 5.7 SecAccessControl：用户在场（User Presence）

| flag | 语义 | 变更敏感度 |
| --- | --- | --- |
| `.userPresence` | 生物识别或设备口令任一通过（fallback） | 中 |
| `.biometryCurrentSet` | 当前生物特征集合（指纹增删/面容重注册即失效） | 高 |
| `.biometryAny` | 任意已注册生物特征 | 低 |
| `.optionUserPresence` | 允许 fallback 到 passcode | 中 |
| `.or` / `.and`（组合语义） | 多条件组合 | 视组合 |
| `.privateKeyUsage` | 私钥使用需每次授权（不缓存） | 最高 |
| `.applicationPassword` | 附加应用口令 | 高 |
| `.watchdogTimeout`（超时约束） | 授权时间窗 | 中 |

```swift
// 每次签名都要求生物识别、不接受 passcode fallback 的严格组合（示意）
let flags: SecAccessControlFlags = [.privateKeyUsage, .biometryCurrentSet]
```

**误用点**：

- 只对"读"设置 access control，却允许"无保护副本"存在于 UserDefaults。
- 使用 `.biometryAny`，攻击者用任意指纹即可（现实中的"手指不够唯一"场景：无生命特征/共享设备）。
- 忘记 `.privateKeyUsage`：某些版本上系统可缓存授权状态，削弱"每次确认"的强度。
- `kSecUseAuthenticationUIAllow`（默认）与静默场景冲突导致业务代码自行降级保护。

### 5.8 ACL 相关的经典设计缺陷类型

| 缺陷类别 | 描述 | 后果 |
| --- | --- | --- |
| TOCTOU | 身份判定与实际操作之间调用者变化 | 越权读 |
| 组前缀解析错误 | 对 `<TeamID>.<group>` 解析过宽 | 跨 Team 读写 |
| 通配/宽松匹配 | 以 service 前缀匹配 | 同 App 内不同账号互串 |
| 同步标志继承 | 更新时意外设置 synchronizable | 秘密上云 |
| 元数据回显 | 错误路径中回显条目属性 | 账号枚举 |
| 授权缓存过期 | 生物识别结果被长期缓存 | "一次授权永久可用" |

### 5.9 本章要点

- 保护类（When）与 ACL/组（Who）是两条必须同时成立的轴。
- `WhenUnlocked` + `ThisDeviceOnly` 的组合，能在"零密码学改动"的前提下把离线提取风险降到最低。
- 元数据/subject 使用摘要存储，但不构成对特权攻击者的秘密。
- 生物识别的正确使用依赖 `.biometryCurrentSet` + `.privateKeyUsage`，否则容易被"已验证状态"缓存绕过。

---

## 第 6 章　Secure Enclave 与密钥管理

### 6.1 SEP 是什么

Secure Enclave Processor（SEP）是与主处理器物理隔离的协处理器（ARM 架构，独立的启动链、内存、加密引擎、随机数发生器与熔丝）。它承担：

- **设备唯一密钥（UID key）**：量产时烧录，永不通过任何接口读出。
- **GID / 工厂密钥**：与产品线相关的次级根。
- **AES/SHA 引擎与密钥槽（key slots）**：主 CPU 只能请求"用第 N 个槽加解密一段数据"，无法读出槽内容。
- **生物特征匹配**：Touch ID 的指纹模板 / Face ID 模型存于 SEP，匹配在 SEP 内完成，主侧只得到布尔结果与受保护的令牌。
- **单调计数器与安全时间**：用于防回滚、密钥老化（key aging）。
- **SEP 密钥存储（sepfs / "Secure Enclave File System"）**：保存 SEP 内部生成的私钥与 attestation 材料。

### 6.2 密钥层次（Key Hierarchy）

```text
  ┌── 熔丝：UID key（设备唯一，永不导出）
  │        │
  │        ├──► 派生/包裹：硬件类密钥（HW class key）
  │        │                        │
  └─ 用户口令 ──► PBKDF2/迭代 ──► scKey ──► 解开 keybag ──► class key（Class 1/2/4）
                                                     │
                                                     ├──► 包裹 per-blob key
                                                     │            │
                                                     │            └──► 条目密文（pdat）
                                                     └──► 文件级 KEK ──► 文件 DEK
```

三个层次的攻击成本差异极大：

| 层次 | 攻击方式 | 可行性 |
| --- | --- | --- |
| UID key | 熔丝读出、SEP 固件漏洞、故障注入 | 公开研究极少，成本极高 |
| class key | 口令爆破 + keybag；bootrom 路径；运行时内存提取 | **现实中最受攻击的层** |
| blob key | 拿到 class key 后自动获得 | 依附上一层 |

**结论**：几乎所有"钥匙串窃取"的实战都是针对 class key 层，而不是密钥算法本身。

### 6.3 保护类如何与 SEP 交互

- `*ThisDeviceOnly` 系列：class key 之外还叠加了由 UID 派生的硬件密钥；即使镜像完整、口令已知，**没有该设备的 SEP 也无法解密**，这就是"跨设备恢复失败"的原因。
- 非 `ThisDeviceOnly` 条目：其 class key 在备份/迁移时可通过"托管/包裹"形式随备份带走（配合备份口令），因此跨设备可恢复——也因此在攻击者手中可能被离线解开。
- 生物识别绑定条目：授权令牌（"已验证"证明）由 SEP 生成并被主侧的 keybag 逻辑校验，形成"密钥只在用户在场时可解"的闭环。

### 6.4 SEP 中的密钥生成与"不可导出"

`SecKeyCreateRandomKey` + `kSecAttrTokenIDSecureEnclave` 的流程：

```text
App → securityd → SEP：请求生成密钥（携带 access control 策略）
SEP：内部生成 EC 私钥 → 存入 sepfs（加密绑定策略）→ 返回公钥 + token 引用
Keychain：保存 kSecClassKey 条目，其"私钥数据"实为引用而非密钥
使用：App → securityd → SEP：请求签名（需满足策略，触发生物识别）
SEP：校验策略与在场证明 → 内部签名 → 返回签名值
```

**安全含义**：

1. 完整闪存镜像 + keybag 破解，也拿不到 SEP 私钥。
2. 攻击者若要在 SEP 上完成签名，必须让**真实设备**执行操作（"用设备而非偷密钥"）——因此针对 SEP 密钥的攻击会转为"设备劫持 + 用户在场伪造"，检测面完全不同。
3. 这也是 Apple Pay、Passkey、MDM 客户端身份、ATTestation 选择 SEP 的原因。

### 6.5 密钥老化、恢复与"数据可延展性"

SEP 的单调计数器使某些密钥随时间自动不可用（例如 Health/Screen Time 的部分数据密钥老化），这是"合规数据最小化"的硬件实现。对 Keychain 而言，相关能力体现在：

- **策略与版本绑定**：SEP 固件升级后旧密钥可能被标记为不可用（key versioning）。
- **attestation**：可远程证明"这把 key 确实在 SEP 中生成且带指定策略"，服务端可据此拒绝软件密钥（**强烈建议金融/身份类业务启用**）。
- **恢复路径**：SEP 密钥一旦损坏（SEP 故障 / 刷机）不可恢复，必须设计服务端可重新 enroll 的流程。

### 6.6 侧信道与硬件攻击（公开研究概述）

| 研究方向 | 公开结论摘要 | 对 Keychain 的现实影响 |
| --- | --- | --- |
| SEP 固件逆向 | 大量研究者（如 i0n1c、Pocokuruenik 等）公开过 SEP 接口/协议细节 | 提升攻击知识储备，但未见直接密钥读出 |
| 故障注入（glitching） | 学术与商业取证界对早期 A 系列有过讨论 | 需物理拆解、良率低，不规模化 |
| 功耗/EM 侧信道 | 学术上对 AES 实现有过分析 | Apple 有掩码与硬件防护，实际未成为主流提取手段 |
| Bootrom（checkm8） | A7–A11 不可修复的启动 ROM 漏洞 | 影响**主处理器侧**的口令爆破限速，间接影响 class key |
| SEP 通信中间人 | 主 CPU 与 SEP 的 IPC 若被内核控制可发起请求 | 提权后可"使用"密钥而非"导出"密钥 |

**关键判断**：checkm8 类 bootrom 漏洞之所以威胁巨大，不是因为它读出了 UID key，而是因为它让**离线口令爆破的速率限制被大幅削弱**（可在恢复流程中反复尝试、绕过安全定时与计数器约束），从而解出 keybag → class key → 所有非硬件绑定条目。

### 6.7 生物识别与在场证明

```text
Face ID 扫描 → SEP 内模型比对（模板不出 SEP）
   → 通过：SEP 产出"在场证明"（signed assertion / token）
   → 主侧 keybag 逻辑校验证明 → 释放条目 class key
```

现实攻击分类：

- **呈现攻击（presentation）**：面具、高保真照片（对 Face ID 有效率低，但存在针对特定机型/特定状态的公开演示）。
- **替代机制攻击**：利用 `kSecUseAuthenticationUI` 允许 passcode fallback，转为口令攻击。
- **法律/胁迫层面**：口令与生物识别在"被迫解锁"上的差异（美国判例中，生物特征通常被视为"物理证据"更易被强制，口令受第五修正案更强保护）。这对威胁模型有实际意义。
- **无意识解锁**：睡眠/被夺取设备时的生物特征可用性——iOS 通过"锁定后必须口令"（重启、48 小时未用、5 次失败生物识别等）与 iOS 17+ 的 Stolen Device Protection 缓解。

### 6.8 与第三方 SE/TEE 的对比

| 平台 | 安全元件 | Keychain 等价物 | 备注 |
| --- | --- | --- | --- |
| Apple iOS | SEP（SoC 内，独立核） | Keychain + Data Protection | 一致性最高 |
| Android | StrongBox / 厂商 TEE（QSEE/Trusty） | Android Keystore | 碎片化，部分机型无硬件 |
| Windows | TPM 2.0 / NPU | DPAPI/CNG | 依赖 OEM TPM 质量 |
| Web | 平台认证器（FIDO2） | Passkey 存储 | 由浏览器/OS 决定 |

对"多端一致凭证策略"的团队而言，iOS 侧应把安全假设建立在"SEP 可用"之上（几乎恒成立），Android 侧则需 `isStrongboxAvailable` 动态判断并降级设计。

### 6.9 本章要点

- 密钥层次中真正决定风险的是 **class key**，而不是算法。
- `ThisDeviceOnly` 的强度来自 UID 绑定，是**开发者可自主启用的最强杠杆**。
- SEP 密钥"可用不可取"，把攻击从"偷密钥"转为"劫持设备 + 伪造在场"，检测策略应随之调整。
- bootrom 类漏洞的现实威胁在于削弱口令爆破限速，而非直接读取熔丝。

---

## 第 7 章　Keychain 同步机制

### 7.1 为什么同步是"钥匙串窃取"的高价值目标

单设备存储的秘密，攻击者必须先取得该设备；而一旦秘密进入跨设备同步管道，攻击面就从"物理/设备"扩展到"账户/云端/协议"。这也是历史上 iCloud Keychain 事件影响最深远的原因。

### 7.2 iCloud Keychain / Keychain Syncing 概览

演进名称对照：

| 时期 | 名称 | 说明 |
| --- | --- | --- |
| iOS 7.0 | iCloud Keychain | 首次引入密码/信用卡同步 |
| iOS 10–17 | iCloud Keychain（扩展） | 加入 App 密码、Passkey（iOS 15） |
| iOS 18 | Passwords App + iCloud Keychain | 密码域独立 UI |
| iOS 26 | Keychain Syncing / iCloud Keychain（重组） | 端到端加密范围与同步通道调整（以官方说明为准） |

同步的数据范围：

1. 网站/表单密码（internet password）。
2. App 密码（部分 generic password，需 App 主动标记 `synchronizable`）。
3. 信用卡（支付卡数据）。
4. Passkeys（WebAuthn 凭证）。
5. Wi-Fi 凭证（在部分语境下由独立机制处理）。
6. 系统级 2FA/验证码相关元数据（随版本变化）。

### 7.3 信任建立：配对与批准流程

```text
新设备开启 iCloud Keychain
   ↓
需要既有"受信设备"批准（弹出通知 + 设备口令）
   或
使用恢复联系人 / 安全码（Recovery Contact，iOS 15+ 引入）
   ↓
建立共享的同步密钥环（keyring）：各设备持有公钥，秘密以 ring 加密
   ↓
CloudKit 私有数据库承载密文（Apple 只见密文与元数据）
```

设计要点与残余风险：

| 环节 | 强度 | 残余风险 |
| --- | --- | --- |
| 受信设备批准 | 需要既有设备 + 口令 | 攻击者若已控制一台受信设备即可加入 ring |
| Apple ID 口令 + 2FA | 常规 | 钓鱼/重置流程社工（历史上 Apple Support 类事件） |
| 恢复联系人 | 社会化信任 | 联系人被胁迫/被钓鱼 |
| 云端存储 | E2EE（同步载荷） | 元数据（时间、大小、zone）可见；实现 bug |
| 服务端推送/变更分发 | APNs/CloudKit | 拒绝服务、变更集回放（历史问题） |

### 7.4 端到端加密的实现层次

iCloud Keychain 的秘密以"环形加密（key ring）"保护：

```text
条目明文 ──► 内容密钥（per-record）──► 密文
密文 ──► 用每个 ring 成员的公钥分别封装（或经共享对称 ring key 派生）
ring key 本身 ──► 由设备口令 + Apple ID + 批准流程派生/分发
```

因此**"加入 ring"就是"获得解密能力"**，这也解释了为什么 Apple 把批准流程设计为需要既有设备确认：任何被劫持的受信设备都是 ring 的入口。

### 7.5 历史缺陷：DarkSkiiMmer 与 skiiper（重要案例）

2013–2014 年公开的两项研究（Thoughtcrime 的 `skiiper` 与 0x00sec/GCC 相关的 `DarkSkiiMmer`）指出早期 iCloud Keychain 同步协议的问题：

| 问题 | 描述 | 后果（当时） |
| --- | --- | --- |
| 硬编码密钥 | 加密中使用写死的密钥/公共材料 | 服务端或中间人可解密部分记录 |
| 弱 KDF / 固定 IV | 口令派生强度不足、Nonce 重用倾向 | 离线爆破与模式分析 |
| RC4 与流加密选择 | 使用 RC4 类算法 | 已知偏置攻击 |
| 服务器可解密设计 | Apple 服务端持有可用密钥 | 法律调取/内部滥用风险 |
| 无设备批准绑定 | 新设备加入缺乏强确认 | 账户接管即获得密码 |

Apple 随后重构：改用更强的密钥协商、引入受信设备批准、限制服务端可见性，并把同步管道迁移到 CloudKit。**这是"钥匙串窃取"史上最重要的教训之一：跨设备同步把设备侧的硬件优势转化为账户侧的信任依赖。**

### 7.6 Advanced Data Protection（ADP）与 Keychain

端到端加密 iCloud（ADP，iOS 16.4+ 推广）把包括 Keychain 相关类别在内的更多 iCloud 数据纳入 E2EE 范围：

- 开启后，Apple 无法在不掌握用户设备口令的情况下解密这些内容。
- 执法调取难度显著上升（这是 Apple 回应"5 眼联盟"关切时的核心论点）。
- 对攻击者的影响：路径从"云端索取"转为"必须拿下设备或口令"。

**注意**：ADP 的保护范围与"iCloud 备份"不同——设备备份（device backup）的历史策略与 ADP 覆盖度存在版本差异，做威胁建模时必须逐项确认，不要笼统假设"开了 ADP 一切 E2EE"。

### 7.7 与 iMessage 密钥同步的争议

iMessage 的账户/身份密钥长期与 iCloud Keychain 同步机制耦合，2023 年多国政府联名信要求 Apple 保持"对执法可访问"，Apple 明确拒绝并强调 E2EE 一致性。安全研究角度的含义：

- 同步的密钥若可被"服务端保留副本"，则设备端保护形同虚设——这是"后门即 Keychain 后门"的论点。
- 任何为"合法访问"设计的新机制，都会改变 Keychain 的威胁模型（引入新的秘密与新的攻击面）。

### 7.8 Passkeys：把"可窃取的秘密"变成"不可导出的身份"

| 属性 | 传统密码 | Passkey |
| --- | --- | --- |
| 存储形态 | 可复制的字符串 | 私钥（设备/SEP 或同步的 E2EE 记录） |
| 泄露后果 | 立即被复用 | 无法离线复用（需用户在场 + 设备/环） |
| 钓鱼风险 | 高（可输入到假站） | 极低（RP ID 强绑定） |
| 撞库风险 | 高 | 无（每站独立密钥） |
| 同步 | 用户自行复制 | Keychain Syncing（受 ring 保护） |
| 恢复 | 重置流程 | 依赖环/恢复联系人（需重新设计恢复体验） |

Passkeys 并不消灭攻击：它把攻击目标从"偷字符串"迁移到"劫持同步环"与"社会工程式重新 enroll"。**服务端必须校验 attestation 与 AAGUID 策略，并警惕"用户自助替换 passkey"流程的授权强度**（这是当前实际发生的攻击类型）。

### 7.9 共享钥匙串与企业场景

| 场景 | 机制 | 风险 |
| --- | --- | --- |
| 同一开发者多 App | access group（不涉云） | 同 Team 恶意 App |
| 家庭/企业成员共享 Wi-Fi 密码 | 配置描述文件 / CloudKit sharing | 权限过度授予 |
| 企业 App 内部凭证 | MDM + Keychain + 客户端证书 | 证书私钥若在 SEP 则安全，否则镜像可提 |
| 跨设备凭证迁移（换机） | 端到端加密的设备迁移 | 迁移窗口内临时暴露 |
| 共享设备（POS/医院） | 多用户 + 快速账号切换 | 卸载残留 + 保护类过宽 |

### 7.10 同步相关的攻击面矩阵

| 攻击面 | 前置条件 | 影响 | 缓解 |
| --- | --- | --- | --- |
| Apple ID 接管（钓鱼/撞库） | 无 2FA 或 2FA 疲劳 | ring 加入 → 全量同步密码 | 强 2FA、防钓鱼密钥 |
| 受信设备失陷 | 控制任一 ring 设备 | 直接解密 | 设备级 MDM、及时移除旧设备 |
| 恢复联系人被胁迫 | 社会工程 | 重置 ring | 最小化恢复联系人 |
| CloudKit 变更集滥用 | 协议实现缺陷 | 数据污染/重放 | 版本与幂等设计 |
| 服务端法律调取 | 未开 ADP | 部分类别可读 | 推动 E2EE 覆盖 |
| 同步元数据观察 | 账户访问 | 使用画像 | 元数据最小化 |
| 新设备 enroll 流程弱点 | 账户 + 短信 | Passkey 替换 | 服务端二次授权 |

### 7.11 本章要点

- 同步是 Keychain 从"设备安全"转向"账户 + 协议安全"的分水岭。
- 历史缺陷（硬编码密钥、弱 KDF）说明：**协议实现的密码学质量比"是否有加密"更重要。**
- 环（key ring）成员资格即解密能力，设备生命周期管理因此成为安全运营的一部分。
- Passkey 不消灭攻击，而是把攻击迁移到"身份重 enroll"环节，服务端策略必须跟上。

---

## 第 8 章　Keychain 存储格式与序列化

### 8.1 澄清：iOS Keychain 不是 SQLCipher 库

社区与部分资料常把"Keychain 数据库加密"与"SQLCipher"混为一谈。事实是：

| 说法 | 正确性 | 说明 |
| --- | --- | --- |
| iOS Keychain 使用整库 SQLCipher 加密 | **不准确** | 主库是标准 SQLite，仅载荷字段为密文 |
| 数据库文件受 Data Protection 保护 | 正确 | 文件本身也有保护类（约 Class 2 语义） |
| 条目 BLOB 逐条加密 | 正确 | 由 per-blob key + class key 两级包装 |
| 备份中 Keychain 是明文 | 错误 | 备份内仍是密文，另受备份密钥包裹 |
| 第三方 App 常用 SQLCipher | 正确但无关 | 一些 App 自研加密 DB，与系统 Keychain 无关 |

SQLCipher 在移动安全中的真实位置是：**第三方 App 自建的本地加密数据库**（其密钥常常又被存进 Keychain，形成"密钥套密钥"链条——因此 Keychain 一旦失守，App 的 SQLCipher 库也一同沦陷）。这是渗透测试中非常典型的双层塌方。

### 8.2 逻辑结构

```text
keychain-2.db
├── 表：genp   （generic password）
├── 表：inet   （internet password）
├── 表：cert   （certificate）
├── 表：keys   （key）
├── 表：idnt   （identity）
└── 辅助：元数据/索引/版本表（随版本变化）

genp 行的概念划分：
┌────────────────┬────────────────┬─────────────────┐
│ 明文/弱保护键字段 │ 元数据与策略字段   │ 加密载荷字段(pdat) │
│ acct, svc, agrp │ cdat, mdat, tver │ 密文 BLOB         │
│ ptid, svn …     │ aecr/aeas, akid  │                  │
└────────────────┴────────────────┴─────────────────┘
```

> 字段命名以社区逆向与取证厂商公开资料为准，Apple 未公开 schema，且跨版本存在差异（不同 iOS 版本字段集合有增减）。本章描述的是"结构逻辑"而非逐字节规范。

### 8.3 载荷密文块（AK / keychain record blob）

从镜像中导出的载荷通常表现为一段带内嵌头部的二进制记录，公开研究归纳的要素包括：

- **版本与类型标签**（FourCC 风格标识，常见 `kbag`/`akfl` 一类标记，具体取值随版本变化）。
- **保护类标识**：指示使用哪个 class key。
- **IV/Nonce 与认证标签**：AES-CBC（旧）或 AES-GCM/带认证模式（新）。
- **包裹的密钥材料（KEK wrap）**：per-blob key 由 class key 加密后的结果。
- **ACL/subject 相关块**：SHA-1 形式的应用标识集合。
- **access control blob**：生物识别策略与在场要求。

**解析顺序（取证/分析视角）**：

```text
1. 读取 pdat → 识别头部 version/class 标签
2. 由 class 标签定位需要的 class key
3. 用 class key 解包裹 → 得到 blob key
4. 用 blob key 解密内容 → 得到 AK 序列化的属性字典（含值字段）
5. 若存在 access control → 需要设备在场，离线不可完成
```

### 8.4 keybag 的序列化

keybag 是提取链的关键枢纽，公开研究（Elcomsoft、Aleph Nahté、axi0mX 等）描述的要素：

| 组成 | 含义 |
| --- | --- |
| header | 版本、类型、条目数 |
| class 条目 | 每个保护类的 KEK 包装块（含 UUID/tag 标识） |
| scKey 派生参数 | salt、迭代次数、type（passcode / escrow / token） |
| HW 绑定标记 | 指示该 class 需要 SEP 参与 |
| backup bag | 供备份/迁移使用的托管密钥集合 |
| 校验与签名 | 防止 keybag 被篡改（历史上有绕过研究） |

**攻击者的现实路径**：拿到 keybag 文件 + 镜像 → 针对 scKey 做口令搜索 → 逐个解 class → 解条目。口令搜索的可行性取决于限速机制是否被削弱（第 10 章）。

### 8.5 access-group 加密与"分组隔离"

条目除了 class key 之外，还通过**分组标识（partition / access group）参与密钥派生或访问判定**。研究资料中常见的描述是：同一条目在不同 access group 下呈现不同的可用性，数据库中的组信息（`agrp`）与 ACL 摘要共同决定谁能请求解密。

工程含义：

1. 组信息是**授权依据而非加密材料**（不要把它当密码学边界）。真正的机密性来自 class key；组决定"守护进程是否肯替你调用 class key"。
2. 一旦守护进程被绕过（root/注入），组隔离立即失效——这就是越狱环境下"全量 dump"的技术前提。
3. 反之，若攻击者只有镜像而没有该设备的 keybag/SEP，则 `ThisDeviceOnly` 条目仍安全。

**分层结论**：

| 攻击者持有 | 组隔离 | class key | ThisDeviceOnly | SEP 密钥 |
| --- | --- | --- | --- | --- |
| 仅数据库文件 | 无效（无守护进程可骗） | 缺 | 安全 | 安全 |
| 数据库 + keybag + 口令 | 无效 | 获得 | 安全（缺 UID） | 安全 |
| 越狱设备（已解锁） | **绕过** | 获得 | **可解（设备在场）** | 可"用"不可"取" |
| 取证镜像 + bootrom 爆破 | 绕过 | 获得 | 视 UID 参与情况 | 安全 |
| 已解锁设备 5 分钟无人看守 | 绕过（若越狱） | 获得 | 视状态 | 需在场 |

### 8.6 备份中的 Keychain 序列化

iTunes/Finder 备份把 Keychain 作为一个特殊域保存：

- 数据库与 keybag 都在备份中（keybag 以"备份密钥"重新包裹）。
- **未加密备份**：Keychain 条目**不**被包含（系统只备份可用的部分），这是"从明备份偷不到 Keychain"的原因。
- **加密备份**：包含 Keychain，其包裹密钥由备份口令派生；一旦口令弱，等价于整个 Keychain 沦陷。
- `ThisDeviceOnly` 条目：不能跨设备恢复（密钥含 UID 绑定），在备份语境下要么被排除，要么以不可解形式存在。
- iCloud 备份（未开 ADP 时）：历史上服务端可解的类别更多；开启 E2EE 后收紧。

### 8.7 序列化相关的安全属性小结

| 属性 | 是否提供 | 备注 |
| --- | --- | --- |
| 机密性（载荷） | 是 | 两级密钥 |
| 机密性（元数据） | **部分** | 索引字段可见 |
| 完整性 | 是（认证加密） | 篡改会解密失败 |
| 前向安全 | 否 | 拿到 class key 即解全部历史条目 |
| 密钥轮转 | 有限 | 依赖条目重写 |
| 抗重放 | 否 | 条目可被复制/回滚（WAL 时间机器） |
| 抗删除 | **反向特性** | "卸载不删"导致数据留存过久 |

### 8.8 本章要点

- 结构明文 + 载荷密文 + 文件级保护，是 iOS Keychain 的三层存储模型；**不存在"破解 SQLCipher"这种一步到位的捷径**。
- 分析/取证的关键是 class 标签与 keybag，而不是 SQLite 本身。
- access group 是授权而非加密边界；这是理解越狱态全量提取的核心。
- 备份把设备侧的硬件优势转写为"口令熵"，是格式层最脆弱的接缝。

---

## 第 9 章　越狱环境下的 Keychain 攻击

### 9.1 越狱改变了什么

越狱本质上是**取得 root 与绕过代码签名/沙箱强制**。对 Keychain 的影响链条：

```text
root + AMFI 绕过
   ├─► 可直接读取 keychain-2.db 与其保护文件（配合已解锁状态）
   ├─► 可读取 keybag，并在设备已解锁时获得 class key（内存提取）
   ├─► 可注入进程或伪造调用者身份 → 骗过 keychainaccessd 的 ACL
   ├─► 可加载带自定义 entitlement 的二进制 → 声明任意 access group
   └─► 可 hook Security.framework → 直接从 App 内存中截获明文
```

**结论**：在越狱且已解锁的设备上，Keychain 不提供任何机密性保证。所有"越狱即 Keychain 归零"的说法在技术上是准确的。

### 9.2 典型提取路径分类（防御视角）

| 路径 | 依赖 | 覆盖条目 | 特征与检测点 |
| --- | --- | --- | --- |
| A. 数据库离线解密 | 镜像 + keybag + class key | 非硬件绑定类 | 需解锁；文件访问痕迹 |
| B. 守护进程滥用 | root + XPC 权限 | 全量（含需解密） | keychainaccessd 异常请求量 |
| C. 进程注入 hook | root + 目标 App 可注入 | 目标 App 的条目 | 异常 dylib、越狱检测 |
| D. API 直接调用 | 恶意 App（同组）或 tweak | 同 access group | entitlement/组异常 |
| E. 内存搜索 | root | 正在使用的明文 | 无 Keychain 层痕迹 |
| F. 备份/恢复 | 备份口令 | 非 ThisDeviceOnly | 与越狱无关，见第 13 章 |

### 9.3 越狱 Keychain 工具生态（历史与现状）

公开存在过的工具/模块（多数已在 GitHub 归档，属于研究资料）：

| 工具/模块 | 时期 | 能力概述 |
| --- | --- | --- |
| `keychain_dumper`（Zdziarski/社区系列） | iOS 5–13 | 在越狱设备上遍历各类 class 打印条目 |
| iPhone Backup Extractor / iBackupBot | 长期 | 解析备份中的 Keychain 域 |
| `m9keydump` / 各类 tweak | — | 通过框架 hook 提取 |
| Elcomsoft iOS Forensic Toolkit | 长期 | 镜像 + 口令/爆破（非越狱路径） |
| Cellebrite UFED / Physical Extraction + Advanced Analysis | 长期 | 完整解析与 Keychain 解码 |
| Oxygen / Passware | 长期 | 口令恢复与格式解析 |

对研究者的启示：**这些工具的存在说明 Keychain 在 root 下的保护边界几乎为零；防御设计必须以"设备可能已被越狱"为假设之一。**

### 9.4 iMessage / WhatsApp 密钥解密案例（技术脉络）

**WhatsApp（2012 年）**：研究者发现 WhatsApp iOS 版把消息数据库加密密钥存放在 Keychain（generic password，保护类宽松），越狱设备上的 tweak 直接读取该密钥并解密本地聊天库。影响：WhatsApp 随后把密钥保护与设备绑定加强，并将关键材料迁移到更安全的位置。

**iMessage**：iMessage 的身份/对称密钥位于系统级 Keychain 与 `/var/mobile/Library/…` 受保护区域。历史上多起公开演示（含会议演讲与取证厂商能力说明）表明：在越狱或取得镜像 + class key 的情况下，可重建 iMessage 会话密钥，进而解密本地消息库。Apple 通过收紧系统条目保护类、把关键密钥放入硬件绑定类与账户密钥体系中降低可提取性。

**通用教训**：

1. 应用级"我们的数据是加密的"声明，若密钥同机存放且保护类过宽，等同于未加密。
2. **密钥与数据同盘存放**是移动端最常见的设计反模式；应引入服务端配合（密钥分片、设备绑定、attestation）或至少 `ThisDeviceOnly` + 生物识别。

### 9.5 Keychain 提取对取证 vs 攻击的对称性

| 目标 | 取证（授权） | 攻击（未授权） |
| --- | --- | --- |
| 数据来源 | 现场镜像/备份 | 恶意配置、物理接触、间谍软件 |
| 合法性 | 法院令/同意 | 犯罪 |
| 技术手段 | 基本相同 | 基本相同 |

因此**防御方案必须假设"任何取证能力都可能被攻击者获得"**（Cellebrite 工具泄露事件即为例证：2023–2024 年多起针对取证厂商的入侵事件使能力外流）。这一视角下，把敏感数据放在 `ThisDeviceOnly` 的价值不仅是抗攻击，也是抗"能力泄露"。

### 9.6 越狱检测与响应（Keychain 相关）

| 检测信号 | 说明 | 强度 |
| --- | --- | --- |
| Cydia/Sileo 路径存在 | 易被隐藏 | 弱 |
| 可写系统分区尝试 | 需沙箱允许，效果有限 | 弱 |
| `fork`/`ptrace` 行为 | 沙箱限制 | 中 |
| 代码签名/entitlement 异常 | 通过 `SecCodeCopyGuestWithAttributes` 类校验 | 中 |
| 越狱动态库注入痕迹（`_dyld_image_count`） | 列表可被绕过 | 中 |
| **DeviceCheck / App Attest 硬件密钥证明** | 服务端可信、难伪造 | 强 |
| JIT/调试端口异常 | 反映内核防护降级 | 中 |
| Keychain 条目"被非本 App 读过"的迹象 | 需自建审计 | 弱—中 |

**结论**：客户端检测只能提高成本，真正有效的是**服务端可验证的设备绑定密钥**（App Attest / SEP key + attestation），使攻击者即使 dump 到 token 也无法在非越狱设备上"以同一身份"通过验证——注意这一点对"已经越狱的目标设备"无效，需要结合风险引擎。

### 9.7 攻击矩阵：越狱场景 × 保护类

| 保护类 | 越狱 + 已解锁 | 越狱 + 重启未解锁 | 镜像（无设备） |
| --- | --- | --- | --- |
| WhenUnlocked | 可读 | 不可读 | 不可读（需口令+解锁） |
| WhenUnlockedThisDeviceOnly | 可读（设备在场） | 不可读 | 不可读 |
| AfterFirstUnlock | 可读 | 可读（重启后需一次解锁；解锁后即可读） | 需 class key |
| AfterFirstUnlockThisDeviceOnly | 可读 | 需设备 + 一次解锁 | 不可读 |
| WhenPasscodeSetThisDeviceOnly | 可读 | 不可读 | 不可读 |
| Always（废弃） | 可读 | **可读** | 视 keybag |
| SEP 密钥 | 可"用"不可"取" | 需在场 | 不可读 |

这张表是"钥匙串窃取"分析中最有实用价值的一张：**它把"设备状态"这一模糊变量精确化为攻击成功率的决定因素。**

### 9.8 本章要点

- 越狱后 Keychain 的授权层被完全绕过，剩下的只是数据保护层的"状态依赖"。
- 真正的防线是**保护类选择**（`ThisDeviceOnly` / `WhenPasscodeSet`）与**服务端可验证的密钥绑定**。
- 应用把加密密钥与密文同机存放且用宽松保护类，是历史上最普遍的失败模式（WhatsApp/iMessage 案例）。
- 取证能力的存在使 `ThisDeviceOnly` 具备"抗能力外泄"的额外价值。

---

## 第 10 章　物理攻击与取证提取

### 10.1 取证提取的三个层级

| 层级 | 名称 | 获取内容 | 对 Keychain 的意义 |
| --- | --- | --- | --- |
| L1 | 逻辑提取（Logical） | 沙箱内文件、备份级数据 | 通常拿不到 Keychain 密文 |
| L2 | 文件系统提取（Filesystem） | 越狱/漏洞获得的全文件系统 | 拿到 keychain-2.db 与 keybag |
| L3 | 物理/完整镜像（Physical/Image） | 全闪存原始镜像 | 可离线分析，配合口令解 keybag |

Cellebrite、Oxygen、GrayKey、Elcomsoft 的差别主要体现为可稳定达到的层级与可覆盖的 iOS 版本/芯片代际。

### 10.2 主流工具能力概览（基于公开资料）

| 工具 | 厂商 | 主要路径 | 已知的 Keychain 能力 | 局限 |
| --- | --- | --- | --- | --- |
| UFED / 4Dot / Physical Analyzer | Cellebrite | 漏洞 + 备份 + 高级分析 | 文件系统级提取后可解析条目；口令破解服务 | 依赖漏洞库；新款芯片难度高 |
| GrayKey | AZD (Grayshift) | 设备侧提权 + 口令搜索加速 | 支持解锁后可获得文件系统级数据并解析 Keychain | 主要覆盖较老芯片；速率仍受限 |
| Oxygen Forensic Detective / Explorer | Oxygen | 备份解析、漏洞利用、云提取 | 备份 + 文件系统解析 | 同上，依赖版本 |
| iOS Forensic Toolkit | Elcomsoft | 镜像 + 口令爆破（含 bootrom 路径） | **明确的 Keychain 提取与离线解密**能力 | 需要物理接触与长时间 |
| Passware / Device Seizure | Passware | 口令恢复 | 备份口令与设备口令恢复 | 纯口令强度依赖 |
| checkra1n / palerain（Reaver 等） | 社区 | bootrom | 获得 root → 全量 Keychain | 仅限 A7–A11，不可启动后修补 |

### 10.3 checkm8：改变游戏规则的 bootrom 漏洞

- **性质**：A5–A11（覆盖 iPhone 5s 至 iPhone X）iBoot 中的一级引导代码缺陷，位于只读掩膜 ROM，**软件层面永远无法修复**。
- **对 Keychain 的实际威胁**：不是读出 UID，而是**在 DFU/恢复流程中削弱口令尝试的限速与计数器约束**，使 6 位数字口令的穷举在数小时—数天量级内完成（8 位以上或字母数字显著变长）。
- **解锁后果**：口令一旦破解 → keybag 解 → Class 1/2 条目全量可读；`ThisDeviceOnly` 类因 UID 参与仍需真实 SEP，离线仍不可解（社区与取证厂商的经验一致）。
- **防御面**：这是 Apple 无法修复的类别，只能通过**产品层（引导用户升级到 A12+ 设备）**与**数据层（使用硬件绑定类）**缓解。

### 10.4 DFU / 恢复模式与"降级攻击"

历史技术路线（公开文献描述）：

1. 进入 DFU，利用 bootrom 加载未签名代码。
2. 通过恢复流程把设备降级到存在已知漏洞的旧版本（旧版本需要 Apple 已停止签名的窗口或攻击者持有 SHSH）。
3. 在旧版本上越狱，获得文件系统，dump keybag 与 Keychain 数据库。
4. 由于旧版本保护类映射可能更宽松（如早期 iMessage 密钥位于 `Always`/`AfterFirstUnlock`），可读出在新版本上本不可读的条目。

**"降级攻击"是钥匙串取证史上极其重要的一类**：同一份数据在新旧系统下的可提取性不同，攻击者通过降级获得"历史保护等级"。Apple 后来通过 SEP 单调版本计数器与禁止降级（OTA 签名窗口收紧）大幅削弱该路径。

### 10.5 口令强度与解锁窗口

| 口令类型 | 组合空间 | 在 bootrom 加速下的现实可行性 |
| --- | --- | --- |
| 4 位数字 | 10^4 | 极低耗时（分钟级） |
| 6 位数字 | 10^6 | 数小时量级（受其他约束影响） |
| 复杂字母数字（10+） | 巨大 | 通常不可行 |
| "简单扩展"（1111/0000） | — | 秒级（字典命中） |

**取证侧另一条捷径**：设备在现场处于已解锁状态（或"已开机且已解锁过一次"），则 Class 2 类条目在文件系统提取后即可解密——**完全不需要口令**。这解释了为什么执法培训强调"抓取正在解锁的设备"。

### 10.6 提取可行性判定树

```text
设备是否开机并处于解锁状态？
├─ 是 → 文件系统提取 → 直接解 Class 1/2 条目（ThisDeviceOnly 也可，因设备在场）
└─ 否 → 是否 A7–A11？
        ├─ 是 → bootrom 路径 + 口令搜索 → 解 Class 1/2；ThisDeviceOnly 仍难
        └─ 否 → 是否有备份？
                ├─ 加密备份 + 弱口令 → 口令恢复 → 解备份中条目
                ├─ iCloud 备份 → 视 ADP/E2EE 状态与账户控制
                └─ 无 → 依赖 Apple 侧托管（法律途径）或放弃
```

### 10.7 内存与运行时提取

- 越狱或漏洞环境下，可直接从进程/内核内存搜索已解密条目与 class key。
- 冷启动攻击（cold boot）在 iOS 上因 SEP 与内存加密/快速断电特性而基本不实际，但在早期设备与研究语境中被讨论过。
- DMA 攻击（Thunderbolt/PCIe）在 macOS 上曾威胁 TPM/口令相关流程；iOS 无此接口（历史上闪电口相关研究有限）。

### 10.8 本章要点

- 物理攻击的现实瓶颈是"设备状态"与"口令熵"，而不是算法。
- checkm8 证明了"不可修复漏洞"对钥匙串的实际杀伤力在于**削弱口令限速**。
- 已解锁状态下的文件系统提取是钥匙串取证最高产、最低成本的路径。
- `ThisDeviceOnly` 是目前少数能同时抗"镜像 + 口令破解"的配置。

---

## 第 11 章　恶意软件窃取 Keychain

### 11.1 间谍软件中的"最后一步"

现代 iOS 商业间谍软件（Pegasus/Predator/Quasar/Havij 等）的攻击链通常分五阶段，Keychain 读取几乎总位于第四或第五阶段：

```text
① 初始访问（0-click/1-click）：短信/图片/推送/链接
② 逃逸与持久化：沙箱逃逸、内核提权、安装 daemon
③ 权限获取：以特权进程运行采集器
④ 数据收割：消息、照片、通讯录、位置 + **Keychain dump**
⑤ 外传与反取证：分块加密上传、清理痕迹
```

CITIZEN LAB 与 Amnesty 对 Pegasus 的取证报告中反复出现的证据，正是 **Keychain 中出现了"被访问/被复制"的痕迹**：例如在设备 Keychain 中被发现由间谍软件写入的**私有 RSA 密钥对**（攻击者为伪装设备身份而生成），以及被读取的浏览器/邮箱凭证条目。

### 11.2 恶意软件读取 Keychain 的四种实现路径

| 路径 | 前提 | 说明 | 典型场景 |
| --- | --- | --- | --- |
| 同 access group | 与目标同 Team（企业签名/同主体多包） | 合法 API 滥用 | 供应链、马甲包 |
| 特权进程/注入 | 已有 root 或代码注入 | 直接向守护进程请求或 hook | 间谍软件、越狱 tweak |
| 配置文件/MDM 滥用 | 控制 MDM 或诱导安装描述文件 | 强制安装带高权限的 App/证书 | 国家行为体、企业内部攻击 |
| 直接读库 + keybag | root | 离线/半离线解析 | 越狱态恶意软件 |

### 11.3 历史与现实的 iOS 恶意软件样本类别

| 名称 | 类型 | Keychain 相关性 |
| --- | --- | --- |
| Pegasus（NSO） | 国家级 0-click | 全量收割，包括浏览器/邮箱/凭证；Keychain 中出现攻击者生成的密钥对 |
| Predator / GovWare（C-Irama、Intellexa 等） | 0-click 链 | 同上，取证报告有 Keychain 痕迹 |
| xAgent / TinyDHH / LightPythia | 定向 1-click | 以数据外传为主，可枚举敏感本地数据 |
| KANAF / BLASTPASS / FORCEDENTRY 系列 | 利用载体 | 载体本身不偷 Keychain，是提权链的一环 |
| SpamTik / DeepPush / HaE / Casino | 广告/欺诈类 | 主要滥用推送与广告标识，偶有凭证滥用 |
| Evilsaurus / 侧载描述文件类 | 企业签名/AltStore 滥用 | 通过配置与 sideload 扩大权限 |
| KeySteal / StealV2 / AMOS（macOS） | 信息窃取 | 直接 `security dump-keychain` 类调用（macOS 与 iOS 模型不同，可作对照） |
| Agent Smith（Android） | App 劫持 | **非 iOS**：用于对照说明 Android 无系统 Keychain 共享模型，凭证滥用逻辑不同 |

### 11.4 Entitlement 滥用：间谍软件的"钥匙"

真正让 Keychain 沦陷的往往不是新 API，而是**获得不该有的 entitlement**：

- `keychain-access-groups`：声明系统级或跨 App 组。
- `get-task-allow`：允许被调试/注入（历史越狱与 exploit 常用）。
- `com.apple.private.*` 系列：大量私有权限可让进程直接操作其他子系统（安全研究中最被觊觎的一类）。
- `com.apple.developer.kernel.extended-virtual-addressing` 等沙箱相关扩展：配合逃逸。
- MDM/配置描述文件授予的 `com.apple.application-identifier` 组合。

**攻击手法**：利用签名校验绕过或已泄露的开发者证书，使恶意二进制以带特权 entitlement 的形态运行。历史上 Apple 曾多次吊销滥用证书（enterprise program 滥用系列事件）。

### 11.5 syncServices 后门与账号级访问

商业间谍软件常利用系统同步/推送基础设施作为外传通道与"合法流量掩体"：

- 滥用 CloudKit / 同步服务的请求模式，使 C2 流量混入真实 Apple 服务流量。
- 通过被控设备上的账户（Apple ID、Exchange、IMAP）在**协议层**读取本应受 Keychain 保护的会话数据：即使 Keychain 未被 dump，攻击者已可用其中的凭证登录同步服务。
- **关键认知**：窃取 Keychain 里的 token 与"直接使用该 token"效果相同。防御不能只盯"密钥导出"，必须盯"凭证使用上下文"。

### 11.6 键盘记录与剪贴板

iOS 沙箱限制了传统全局键盘记录，但存在变体：

| 手段 | 说明 | 防护 |
| --- | --- | --- |
| 自定义键盘扩展 | 用户安装并授权的第三方键盘可观察输入 | 敏感输入关闭第三方键盘（`UITextField.secureTextEntry` 部分限制） |
| 辅助功能/AX 滥用 | 某些配置或漏洞下可读取 UI 内容 | 最小化权限授予、审查配置描述文件 |
| 剪贴板读取 | iOS 14+ 有剪贴板访问提示；iOS 16+ 需授权 | 避免把口令放剪贴板、超时清空 |
| WebKit/JS 注入 | 在被注入的 WebView 中读取表单 | 校验 WebView 加载源、禁用不必要的 JS 注入 |
| 通知内容 | 一次性验证码出现在锁屏通知 | 通知内容隐藏（`UNNotificationContent` 与锁屏策略） |

### 11.7 恶意软件视角下的"有效目标"排序

实践中，攻击者读取 Keychain 时的优先级（基于取证报告中的采集清单归纳）：

1. 邮箱/IMAP/Exchange token（可读取全部历史通信）。
2. 社交媒体与消息 App 的会话 token。
3. 浏览器保存的站点密码（Safari 走 iCloud Keychain 路径）。
4. VPN/MDM 客户端证书与私钥（伪装设备接入企业内网）。
5. 银行/加密钱包 App 的本地密钥与 token（部分为 SEP 密钥，不可导出）。
6. iMessage/WhatsApp 消息加密密钥（解密本地历史）。

**这条排序直接是防御方的资产分级清单**：第 3、4、5 类恰恰是最能通过"硬件绑定 + SEP + attestation"降低风险的类别。

### 11.8 检测与响应（Keychain 维度）

| 检测信号 | 可行性 | 说明 |
| --- | --- | --- |
| 本地出现"陌生 access group 的条目" | 中 | App 可枚举自身可见组，发现异常条目 |
| SEP 中陌生密钥（App Attest 密钥被新增） | 低（无 API） | 需 MDM/系统层能力 |
| 后台异常解密失败/权限错误码突增 | 中 | 需要 OSLevel 遥测 |
| 越狱/完整设备状态变化 | 中 | DeviceCheck 风险评分 + 自建信号 |
| 凭证在服务端"多设备并行使用" | 高 | **最有效的现实检测**：绑定设备指纹后的异常使用 |
| Token 泄露情报（暗网/日志外泄） | 中 | 需持续轮换 |

**结论**：客户端"检测 Keychain 被偷"能力有限，**服务端"检测凭证被异常使用"才是主战场**。这也是 Apple 推动 Passkey 与 Short-Lived Token 的核心原因。

### 11.9 本章要点

- Keychain 窃取在恶意软件链条中是"收割环节"，其难度取决于是否已取得 root/entitlement。
- 攻击者常选择"使用凭证"而非"导出凭证"，防御必须覆盖使用上下文。
- 商业间谍软件的 Keychain 痕迹（攻击者自建密钥）已成为公开取证的重要 IOC。
- 最有效的长期缓解是短期化、设备绑定、attestation 化与 Passkey 化。

---

## 第 12 章　App 层攻击

> 本章聚焦"不需要越狱、不需要 0day"的现实攻击面。统计意义上，绝大多数移动端凭证泄露发生在这里。

### 12.1 Access Group 滥用

**场景一：同 Team 多 App 的过度共享**

```text
开发者账号 AB12CD34EF 下有：
  - AcmeBank（主 App）
  - AcmeBank Lite（长期不更新、有已知漏洞）
  - AcmePartner（外包团队维护，供应链风险）
三者共用 access group "AB12CD34EF.shared.auth"
→ 任一 App 失陷 = 全部会话 token 失陷
```

工程守则：

1. **最小化共享组**：只在必须传递凭证时声明组，且组名带用途后缀（`.shared.push` 与 `.auth` 分离）。
2. **不要让低价值 App 加入高价值组**：营销类/试验类 App 绝不能进主凭证组。
3. **审计描述文件**：定期核对 App Store Connect 中所有证书/描述文件，撤销不用的。
4. **组内条目加"调用者二次确认"**：在高价值 App 中，读取时校验 `kSecAttrAccessGroup` 与业务上下文。

**场景二：企业证书/开发证书泄露**

企业分发证书泄露后，攻击者可用同一 Team ID 签名恶意 App，声明相同 access group，直接读取生产 App 的条目（前提是目标设备允许安装该 App——在越狱或描述文件诱导下可行）。这属于"凭证与身份基础设施耦合"的典型风险。

**场景三：系统级特殊组**

历史研究中，某些系统组件暴露的组（如与账号、iCloud 相关的组）成为越狱工具与恶意 tweak 的抓取目标。第三方 App 无法声明 `com.apple.*` 组，但这也说明 **Apple 内部同样存在"组即权限"的信任假设**。

### 12.2 共享钥匙串的逻辑陷阱

一个常见误设计：把"共享 Keychain"当作"加密的 IPC 信道"，用来在 App 与扩展、主 App 与 Widget、App 与 Safari 扩展之间传递任意数据。后果：

- 共享组的每个成员都能读全部条目，包括本不该看到的部分。
- 扩展的运行环境（如 Safari 扩展可被网页触发）会放大读取时机与频率。
- 条目累积不清理，形成"跨 App 的数据湖"。

**建议**：共享组只放"最小必要且各方都必须使用"的凭证；其余用带鉴权的服务器交换。

### 12.3 URL Scheme 与回调截获

| 问题 | 说明 | 案例类型 |
| --- | --- | --- |
| 自定义 scheme 无来源校验 | 任意 App 可 `openURL`，回调可被伪造 | OAuth 授权码被窃（经典移动端 OAuth 缺陷） |
| `universal links` 未配 AASA | 回退到 scheme 时失去域名绑定 | 授权码/短 token 截获 |
| 回调携带 access_token | URL 中明文 token 进入系统日志与最近使用 | 授权服务器日志/崩溃报告泄露 |
| WebView 注入 JS 窃取表单 | App 内浏览器组件被篡改 | 银行 App 中间人 |

**正确做法（OAuth for native apps / RFC 8252）**：使用 PKCE + `https` 回调（Universal Links）+ 随机端口 loopback（受限），绝不在 URL fragment 中长期传 refresh token。

### 12.4 剪贴板

- 复制口令/验证码/私钥到剪贴板，会被任何前台 App 在 iOS 14 之前无提示读取；iOS 14+ 有横幅提示，iOS 16+ 需要用户授权粘贴；但**提示本身可被忽视**。
- 键盘云同步（Gboard/SwiftKey 等）会把剪贴板历史上传。
- **建议**：App 内避免复制敏感数据；若必须，写入后短延时清空（`UIPasteboard.general.items = []`），并使用 `UIPasteboard` 的 `localOnly`/`.expiresDate`（iOS 16+）能力。

### 12.5 截图、录屏与后台快照

- 后台任务切换器会保存 App 界面的快照（`applicationDidBecomeActive` 前的 snapshot），落盘于容器内，取证/攻击可读取（快照文件在越狱或镜像下可见）。
- 截屏通知（`userDidTakeScreenshot`）不能阻止截图。
- 屏幕录制/镜像（AirPlay）可窃取显示的秘密。

**防御**：

1. 敏感界面遮罩：在 `applicationWillResignActive` 时覆盖不透明视图。
2. `UISnapshotBehavior` / 视图层级 `isHidden` 控制（配合 iOS 版本的截屏保护 API，如部分场景可用隐私屏 API）。
3. 显示敏感值时打码 + 短时可见 + 需重新生物识别。

### 12.6 日志、崩溃报告与遥测

移动端最常见的"明文凭证落地"：

| 载体 | 风险 | 治理 |
| --- | --- | --- |
| `print`/`NSLog`/os_log | 进入系统日志（Console/paired device） | 严禁打印 token；构建期 lint |
| 第三方 SDK 日志 | 上传到厂商云 | SDK 审计与网络出口白名单 |
| 崩溃报告（Crashlytics/Bugsnag） | 内存中的 token 被 dump | 敏感字段脱敏（`setData`/`beforeSend`） |
| 网络抓包（App 自带 debug） | Charles/mitmproxy 可见 header | Release 关闭 debug、证书固定 |
| APNs 推送 payload | 通知里带验证码/交易信息 | 载荷最小化，锁屏隐藏 |
| Analytics 事件属性 | 上报了用户 ID 与设备密钥片段 | 事件 schema 审查 |

### 12.7 WebView 与 JS 桥

```text
攻击者控制页面 → 注入 JS → window.webkit.messageHandlers.native.postMessage(token)
```

只要 App 把 Keychain 值通过 JS 桥暴露给任意远端页面，Keychain 的保护就等价于"通过 WebView 明文广播"。

**守则**：`WKWebView` 加载域白名单校验 + 每次 message handler 调用校验 `frameInfo.securityOrigin`；禁用对第三方内容的 JS 注入；敏感操作要求原生界面二次确认。

### 12.8 深度链接/通知驱动的自动行为

一些 App 为便捷设计"点击通知直接执行转账/展示密钥"。攻击者若控制了通知通道（推送凭证泄露、企业推送伪造）或诱导用户点击，可触发未经确认的敏感动作。**任何"通知即可解锁 Keychain 并使用"的流程，都是在把保护类设为 `WhenUnlocked` 的同时把 UI 授权绕过。**

### 12.9 越狱可选依赖库的风险

某些 App 使用第三方"安全键盘/加密库/反越狱库"。这些库若被 hook（fishhook、substrate）或与越狱环境交互，会成为"合法 App 内的攻击代理"。**依赖审计**必须包含"该库是否会读取/写 Keychain"。

### 12.10 App 层攻击矩阵

| 攻击 | 需要越狱 | 需要用户交互 | 影响 | 难度 | 缓解 |
| --- | --- | --- | --- | --- | --- |
| 同组 access group 读取 | 否（需同 Team） | 否 | 全组凭证 | 低 | 组最小化 |
| URL Scheme 授权码截获 | 否 | 低（点击） | 账户接管 | 中 | PKCE + UL |
| 剪贴板窃取 | 否 | 用户复制 | 口令/私钥 | 低 | 不落地剪贴板 |
| 键盘扩展记录 | 否（需安装） | 使用键盘 | 输入内容 | 低 | 敏感字段限制 |
| 快照/截图恢复 | 部分（需文件访问） | 否 | 界面秘密 | 中 | 遮罩 |
| 日志/崩溃泄露 | 否 | 否 | token | 中 | 脱敏 |
| WebView JS 桥 | 否 | 需访问恶意页 | token | 中 | origin 校验 |
| 通知内容读取 | 否 | 锁屏即可 | 验证码 | 低 | 隐藏内容 |
| 备份明文（未加密） | 否 | 曾备份 | 元数据/日志 | 低 | 敏感数据不入备份路径 |
| 卸载残留继承 | 否 | 装机即用 | 旧 token | 极低 | 显式清理 + 服务端失效 |

### 12.11 本章要点

- App 层的弱点几乎不依赖 iOS 本身的安全缺陷，而是**开发者决策**的结果。
- 判定标准很简单：**Keychain 的值在生命周期的哪一刻离开了 Keychain**。一旦离开，保护类就没有意义。
- 卸载残留 + access group 过度共享，是"低技术、高命中"组合，应优先修复。

---

## 第 13 章　备份与同步路径

### 13.1 备份是 Keychain 的"影子副本"

备份把设备侧的安全假设（硬件绑定 + 实时状态）转写为一个静态文件 + 一个口令，这几乎总是**降低**了安全性。理解钥匙串窃取必须完整理解备份路径。

### 13.2 本地备份（Finder/iTunes）

| 备份类型 | Keychain 是否包含 | 解密依赖 | 风险 |
| --- | --- | --- | --- |
| 未加密本地备份 | **不包含** Keychain 条目 | — | 元数据/文件仍泄露 |
| 加密本地备份 | 包含（密文） | 备份口令（可离线验证） | **弱口令 = 全量 Keychain 泄露** |
| 加密备份 + 强口令（25 位随机） | 包含 | 现实不可爆破 | 安全 |

技术细节：

1. 加密备份使用"备份密码 + keybag"体系：manifest 中的密钥材料被口令派生的密钥包裹；备份口令熵决定一切。
2. 工具链（Elcomsoft Advanced Archive Password Recovery / Passware）会：先做**快速可验证性检查**（一个 header 即可判断口令对错，无需逐条解密），再用 GPU/规则字典穷举。
3. **可离线并行验证**是加密备份最大的结构性弱点：Apple 设备侧的限速（尝试次数、SEP 计数器）在备份场景不存在。
4. 历史上还出现过"默认口令/已知明文"类问题（早期备份使用固定 key、口令仅用于本地包装），使"加密备份"一度名存实亡；Apple 后续版本收紧。

**取证/攻击视角的顺序**：拿到设备前，先看是否能拿到备份文件（员工电脑、Time Machine、MDM 存档、云同步的备份目录）。

### 13.3 iCloud 备份

| 维度 | 未开启 ADP | 开启 ADP（端到端加密） |
| --- | --- | --- |
| 备份内容 | 大量类别上云 | 覆盖范围与密钥归属变化 |
| Apple 可解密性 | 历史上部分类别服务端可解 | E2EE，服务端不可解 |
| 攻击路径 | Apple ID + 2FA 接管、法律调取 | 仍需设备/口令 |
| 执法可行性 | 通过账户与法院命令 | 显著下降 |

**要点**：iCloud 备份中的 Keychain 与 iCloud Keychain 同步是两套机制，前者是"设备镜像"，后者是"密钥环"。企业安全评估时不能只关"同步"，还要看"备份"开关。

### 13.4 设备迁移与"临时暴露窗口"

换机流程（快速开始 / iCloud 恢复 / 电脑恢复）中：

- 旧设备的 Keychain 条目需要在有效期内以可解密形式传输（端到端或经口令保护通道）。
- `ThisDeviceOnly` 类条目**不会**迁移（这是特性而非缺陷）。
- 恢复完成后，新设备的类映射可能变化，App 需具备"凭证缺失时的重新登录"逻辑——若产品为了"用户无感"而把原本 `ThisDeviceOnly` 的秘密降级为可迁移，就人为制造了泄露面。

### 13.5 Keychain 在备份中的"可包含性"控制

| 需求 | 手段 |
| --- | --- |
| 条目不进任何同步/备份 | `kSecAttrSynchronizable = false` + `ThisDeviceOnly` 类 |
| 完全不允许出现在备份 | 无公开 API 逐条排除；需依赖保护类（`WhenPasscodeSetThisDeviceOnly` 语义最接近） |
| 数据文件不进备份 | 存于 `Library/Caches` 或标记 `NSURLIsExcludedFromBackupKey` |
| 备份含敏感文件但需保护 | `NSFileProtectionComplete` + 迁移时主动清除 |

**明确警告**：不要假设"Keychain 里放了就不会被备份带走"。必须实测：做一次加密备份，用备份解析工具确认目标条目是否出现。

### 13.6 iCloud 数据与账户接管链

```text
钓鱼获取 Apple ID + 2FA（SIM 交换/推送疲劳/验证码中继）
  → 新设备登录并"批准"（若攻击者已控受信设备通知）
  → 加入 iCloud Keychain 环
  → 全部同步密码/Passkey 抵达攻击者设备
```

这条链完全**不需要**触碰 iOS 密码学，只需要账户控制。对应的防御：

- 强 2FA（防钓鱼安全密钥优先）。
- 受信设备清单审计（用户自助 + 企业 MDM 报表）。
- 关键账户使用**不同 Apple ID**（个人 vs 工作）。
- 恢复联系人最小化并告知风险。

### 13.7 备份路径风险矩阵

| 路径 | 攻击者需要 | 覆盖条目 | 缓解成本 | 建议 |
| --- | --- | --- | --- | --- |
| 加密本地备份 + 弱口令 | 备份文件 + GPU | 非硬件绑定全部 | 极低 | 强制强口令/随机口令管理器 |
| 明本地备份 | 备份文件 | 元数据、文件（无 Keychain） | 低 | 备份加密 |
| iCloud 备份（无 ADP） | Apple ID + 2FA | 视类别 | 中 | 开启 ADP |
| iCloud Keychain 环加入 | Apple ID + 一台受信设备 | 同步类全部 | 中 | 设备治理、防钓鱼 |
| 设备迁移窗口 | 物理 + 引导操作 | 可迁移类 | 低 | 迁移后强制重认证 |
| MDM/企业备份归档 | 企业系统访问 | 视配置 | 低 | 备份域访问控制与加密 |

### 13.8 本章要点

- **备份是钥匙串窃取中最"性价比"的路径**：它把硬件优势换算成口令熵。
- 加密备份的可离线并行验证，使口令强度成为唯一防线。
- 同步（Keychain 环）与备份（设备镜像）是两条不同管道，必须分别评估。
- 任何"跨设备可用"的需求，都在密码学意义上等价于放弃硬件绑定。

---

## 第 14 章　CVE 与漏洞汇总

### 14.1 分类框架

Keychain 相关的真实缺陷可归入六类：

| 类别 | 缺陷本质 | 代表后果 |
| --- | --- | --- |
| C1 授权判定缺陷 | access group / entitlement 校验错误 | 跨 App 读取 |
| C2 守护进程/框架内存破坏 | keychainaccessd / Security.framework | 任意读写、代码执行 |
| C3 沙箱/内核提权 | 使攻击者获得绕过 ACL 的能力 | 全量 dump |
| C4 保护类/数据保护实现缺陷 | keybag、class key、限速 | 离线解密可行性上升 |
| C5 同步协议/密码学缺陷 | iCloud Keychain 协议 | 云端可读、账户扩散 |
| C6 UI/生物识别旁路 | 授权提示、在场证明 | 绕过用户确认 |

### 14.2 汇总矩阵（代表性条目）

> **核对提示**：下表把"与 Keychain/凭证/数据保护直接相关"的公开漏洞与"在真实钥匙串窃取事件中作为链条一环"的漏洞并列。CVE 编号—组件—修复版本的一一对应关系，请以 Apple 官方《Apple Security Releases》数据库（support.apple.com 各版本发布说明）为准；标注 ★ 者为在公开取证/研究文献中被明确讨论过的条目。

| CVE / 标识 | 组件 | 类别 | 影响（与 Keychain 的关系） | 影响版本（大致） | 修复 |
| --- | --- | --- | --- | --- | --- |
| ★ checkm8（无 Apple CVE 编号） | iBoot / Secure Boot ROM | C4 | 削弱口令爆破限速 → 解 keybag → 非硬件绑定条目全可读 | A5–A11 | **无法软件修复** |
| ★ DarkSkiiMmer（无 CVE，协议缺陷） | iCloud Keychain Sync | C5 | 早期同步协议硬编码密钥/弱 KDF，服务端可读 | iOS 7–8 时代 | 协议重构 |
| ★ skiiper（无 CVE） | iCloud Keychain Sync | C5 | 同上方向，公开 PoC 工具 | iOS 7–8 | 同步机制改版 |
| ★ CVE-2014-4410 | iOS Device Access / 锁屏 | C6 | 物理访问下绕过锁屏获得数据访问，间接影响钥匙串窗口 | iOS 8.x 前 | iOS 8.2 附近 |
| CVE-2016-1746 / CVE-2016-1757 系列 | WebKit + 沙箱 | C3 | 提权链组件（Pegasus 早期链条），最终采集 Keychain | iOS 9.x | iOS 9.3.5 |
| CVE-2017-2363 / CVE-2017-2364 系列 | WebKit / Memory | C3 | 0-click 链条组件 | iOS 10.x | iOS 10.2.1 |
| CVE-2017-13872 等 macOS 条目 | macOS Keychain/SecurityAgent | C6 | macOS 侧钥匙串弹窗与授权相关缺陷（对照参考） | macOS 10.12–10.13 | 系统更新 |
| CVE-2019-8605 系列（macOS） | Sandbox/权限 | C1/C3 | 允许非授权访问用户数据（含凭证类） | macOS 10.14/10.15 | 更新 |
| CVE-2020-3835 | Passcode / 数据保护 | C4 | 物理访问下的口令/解锁相关问题 | iOS 13.x | iOS 13.5 附近 |
| CVE-2021-30860 ★ | CoreGraphics/PDF | C3 | FORCEDENTRY：0-click RCE，链末端包含数据收割 | iOS 14.6–14.8 | iOS 14.8 |
| CVE-2021-30869 / 30870 / 30865 / 30858 ★ | WebKit / Sandbox / kernel | C2/C3 | "Candour" 链条：提权后读取 Keychain 类数据 | iOS 13.4–14.6 | iOS 14.7/14.6 后续 |
| CVE-2021-30890 ★ | launchd/XPC | C3 | 与上述链配合的权限组件 | iOS 14.x | 同上 |
| CVE-2022-32818 / 32896 等 | WebKit / Kernel | C3 | 在野链条（Paragon/Repin 等场景讨论） | iOS 15.x | iOS 15.6/15.7 |
| CVE-2023-32434 / 32439 ★ | WebKit / GPU | C3 | "III Invasion"（Paragon）链条组件 | iOS 16.x | iOS 16.6 |
| CVE-2023-41060 / CVE-2023-41990 ★ | Images/Notifications + WebKit | C3 | BLASTPASS（NSO Pegasus）投递链 | iOS 16.x | iOS 16.6.1 |
| CVE-2023-38606 ★（KANAF） | WebKit/iMessage 相关 | C3 | 0-click 链，采集含凭证 | iOS 16.x | iOS 16.6.x/16.7 |
| CVE-2024-23222 / CVE-2024-23225 ★ | ImageIO / Kernel | C3 | Predator/Quasar 在野链，链末含 Keychain 读取 | iOS 17.2 前 | iOS 17.3 |
| CVE-2024-23225 同类内核项 | XNU | C3 | 提权后可 dump | iOS 17.x | 17.4+ |
| CVE-2024-44309 / 44316 ★ | WebKit / Kernel | C3 | ShadowOps/InSpy 类链条（公开披露） | iOS 17.x | iOS 17.7/18 |
| CVE-2025-xxxx 系列（iOS 18.x） | 多个 | C3 | 持续出现的 0-day 链；防御结论不变 | iOS 18.x | 18.x 增量 |
| 各类 `keychainaccessd` 逻辑缺陷（散见公告） | Keychain | C1 | 组件字段常标注为 "Keychain"，影响为"访问敏感用户数据" | 多版本 | 各版本 |

### 14.3 阅读 CVE 时的三条经验

1. **Apple 公告的组件字段（"Keychain"、"Security"、"Kernel"）比标题更有信息量**。搜索官方发布页时按组件过滤，"Keychain" 条目数量虽少，但每条都直接关系到凭证边界。
2. **"An application may be able to access sensitive user data" 这一标准表述，在多数情况下就是钥匙串类泄露**——需要结合受影响组件判断是 Keychain、通讯录还是文件。
3. **链条型漏洞不应按"Keychain CVE"统计**。真实窃取事件里 Keychain 通常不是被"利用"的漏洞，而是被"访问"的资源；把它统计进 CVE 数量会造成风险评估失真。

### 14.4 非 CVE 的"设计即风险"清单（比 CVE 更重要）

| 项 | 类型 | 现状 |
| --- | --- | --- |
| 卸载不删 Keychain 条目 | 设计 | 未改变；需 App 自行清理 |
| `AfterFirstUnlock` 解锁后常驻可读 | 设计 | 未改变；需开发者选择更严类 |
| 元数据明文 | 设计 | 未改变 |
| 备份把硬件绑定转为口令熵 | 设计 | 加密备份 + 强口令可缓解 |
| `Always` 类曾长期可用 | 已修 | iOS 10 废弃（旧条目仍可存在） |
| 早期 iCloud Keychain 协议缺陷 | 已修 | 重构 + E2EE |
| 越狱环境下 ACL 失效 | 设计（必然） | 只能靠设备完整性检测 |
| checkm8 不可修 | 硬件 | 只能靠机型迁移 |

### 14.5 本章要点

- 影响 Keychain 的"漏洞"多数是**提权/逃逸类**，其价值在于绕过 ACL，而不是破解加密。
- 协议层（同步）曾出现过真正的密码学级缺陷，代价是整个账户生态的凭证外泄。
- 做风险评估时，"设计即风险"清单应排在 CVE 清单之前——它解释了大量不需要漏洞的泄露。

---

## 第 15 章　典型案例复盘

### 15.1 案例一：FBI vs Apple，San Bernardino（2015–2016）

| 维度 | 内容 |
| --- | --- |
| 目标 | 2015 年枪击案嫌疑人的 iPhone 5c |
| 障碍 | 设备口令 + 数据保护；尝试次数上限可配置为"抹除" |
| Apple 立场 | 拒绝构建"专用工具"（所谓 backdoor），理由是削弱所有用户安全 |
| 结果 | FBI 转而借助第三方（后披露为 Cellebrite）完成提取并撤销诉讼 |

**钥匙串视角的复盘**：

1. 争议焦点并非"能否解 AES"，而是"是否要为访问引入一条新通道"。这正是钥匙串安全的核心命题：**任何新增的合法访问路径都会成为攻击路径**。
2. iPhone 5c 使用的是较老的数据保护形态（非 SEP 全链路 + 无 Touch ID 时代），Apple 后来承认该案的特殊性，并强调新设备（5s 起）在口令丢失时**连 Apple 也无法提取**。
3. 该案直接推动了行业对"引导用户设置强口令 + 开启 ADP"的重视——因为**当口令是唯一秘密时，它的熵决定一切**。
4. 对 Keychain 的启示：执法提取的可行性来自"设备老 + 口令短 + 状态可控"，而不是算法破缺。

### 15.2 案例二：Cellebrite / GrayKey 式提取的普遍模式

公开报道与厂商能力矩阵（不依赖机密资料即可归纳）显示，典型钥匙串提取作业遵循如下流程：

```text
① 现场判定：设备是否开机/已解锁/口令长度未知
② 优先尝试：解锁态 → 文件系统提取 → 解析 Keychain（最快、成本最低）
③ 次优：备份（设备历史备份、iCloud）
④ 硬件路径：芯片代际判定 → bootrom/漏洞利用 → 口令搜索
⑤ 离线分析：镜像 + keybag + class key → 条目解码 → 时间线还原
⑥ 报告：按 class 标注"我们拿到了什么/没拿到什么/为什么"
```

**教训**：企业安全评估时应假定攻击者可复用同一流程（取证工具能力外泄事件已多次发生），并据此判断哪些秘密必须放在 `ThisDeviceOnly`。

### 15.3 案例三：Pegasus 与"Keychain 里的攻击者私钥"

CITIZEN LAB 的 *Pegasus Gateways*、*The Berlin Spyware* 以及多份设备取证公开报告中，反复出现同一类 IOC：

- 目标设备 Keychain 中出现**由攻击者生成的 RSA/EC 私钥条目**（用于伪装成该设备建立 TLS/推送身份）。
- 系统级条目（如邮箱账号 token）被复制的痕迹、异常的配置描述文件。
- 注入的 daemon 使用高权限 entitlement 访问钥匙串。

**要点**：这证明了"国家级链"的 Keychain 采集是在**取得内核权限后**通过系统 API/守护进程完成的——没有魔法，只有权限。防御投入应优先放在**拒绝越狱/降级环境**（App Attest + 服务端策略）与**凭证短期化**。

### 15.4 案例四：Apple Support / 账户恢复社工

公开报道过多起因客服流程缺陷导致的账户接管（记者、政客 iCloud 被访问），其后果直接包括 iCloud Keychain 中的密码：

| 攻击步骤 | 利用了 | 与 Keychain 的关系 |
| --- | --- | --- |
| 猜测/获得安全问题答案 | 身份验证的弱因子 | 通过账户验证即靠近环 |
| 客服人工重置 | 流程信任 | 绕过 2FA 的等效路径 |
| 新设备登录并批准 | 受信设备缺失 | 加入 iCloud Keychain 环 |
| 读取同步密码/Passkey | 环密钥 | **完整钥匙串沦陷** |

Apple 后续改进（更严格的恢复流程、恢复联系人、账户恢复密钥、ADP）本质上都是"把人工信任换成密码学信任"。

### 15.5 案例五：12306 与早期移动凭证实践（历史教训）

2014 年，安全研究员公开指出 12306 App 存在把用户口令/敏感信息以不当方式处理的问题（含本地明文存储与网络传输层面的缺陷），引发中国移动互联网行业对"App 不应明文存密码"的广泛讨论。

**对钥匙串研究的启示**（作为对照案例）：

1. 该时代的普遍问题是**没有使用 Keychain 而使用 `NSUserDefaults`/文件明文**，而不是"Keychain 被破解"。
2. 它推动了国内厂商把凭证迁移到 Keychain，也带来了第二代问题：**用了 Keychain，但保护类宽松、密钥与密文同机、卸载不清理**。
3. 评估任何 App 时，"是否用 Keychain"只是第一层；"用了哪一类、是否同步、内容是否被复制到别处"才是实质。

### 15.6 案例六：银行/证券 App 泄露的三种常见形态

| 形态 | 根因 | 表现 |
| --- | --- | --- |
| 会话 token 在越狱设备被读取 | `AfterFirstUnlock` + 无设备绑定 | token 在其他设备重放成功 |
| 本地加密库密钥与数据同盘 | SQLCipher key 存 Keychain（弱类） | 镜像后聊天/交易记录被解 |
| 备份外泄 | 员工加密备份口令弱/明备份 | 历史凭证可恢复 |

**工程改进（已在多家金融机构落地）**：SEP 密钥 + DeviceCheck/App Attest + DPoP（持有者证明）+ 60 分钟内短期 token + 服务端设备指纹与风险引擎。效果：**即使 Keychain 被 dump，凭证在别的设备上不可用**——这是把"钥匙串窃取"从"高危"降为"低危"的唯一可靠路径。

### 15.7 案例七：密码管理器与"密钥套娃"塌方

多个第三方密码管理器的历史问题：主口令派生的解锁密钥被以宽松保护类存入 Keychain，导致在越狱/取证环境下可离线解开封印库。教训：

1. 密钥环（key ring）设计必须让"最外层秘密"来自用户（口令）而非系统（Keychain）。
2. `kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly` + 生物识别 + 不可同步，是本地解封密钥的正确配置。
3. 云端同步必须对条目再套一层"零知识"客户端加密，避免把 Keychain 的弱点外溢。

### 15.8 案例八：Safari 自动填充与钓鱼页

真实事件类型：钓鱼页面触发 AutoFill，用户在生物识别确认后凭据被填入恶意站点。**Keychain 没有失守，失守的是"用户对提示的判断"**。防御：Safari 的域匹配严格化、企业策略禁用不明站点自动填充、Passkey 从根本上消除此类攻击（RP ID 绑定）。

### 15.9 案例分析表（横向对比）

| 案例 | 破坏的层 | 是否需要漏洞 | 是否需要物理 | 时间成本 | 可预防性 |
| --- | --- | --- | --- | --- | --- |
| San Bernardino | 口令 + 数据保护 | 否（老机型） | 是 | 中 | 强口令可显著抵抗 |
| Cellebrite/GrayKey 提取 | class key | bootrom/漏洞 | 是 | 数小时—数天 | 机型迁移 + ThisDeviceOnly |
| Pegasus | 沙箱/内核 → ACL | 是（0day） | 否 | 分钟 | 补丁 + attestation |
| Apple Support 社工 | 账户信任链 | 否 | 否 | 小时 | 防钓鱼 2FA、ADP |
| 12306 类明文存储 | App 层 | 否 | 部分 | 极低 | 使用 Keychain + 正确类 |
| 银行 token 重放 | 凭证设计 | 否 | 部分 | 低 | 设备绑定 + 短期化 |
| 密码管理密钥套娃 | 密钥层次 | 否 | 部分 | 中 | 用户口令为根 |
| AutoFill 钓鱼 | UI 信任 | 否 | 否 | 秒 | Passkey |

### 15.10 案例共性提炼

1. **八个案例中没有一个是通过"破解 AES"完成的。**
2. 成功要素排名：口令熵 > 设备状态 > 权限获取 > 协议/信任链设计 > 算法强度。
3. 最便宜的防御：设置强口令、启用 `ThisDeviceOnly`、开启 ADP、迁移 Passkey。
4. 最贵但最有效的防御：让凭证不可移植（设备绑定 + 短期 + 持有者证明）。

---

## 第 16 章　Keychain 安全 API 最佳实践

> 面向 iOS 开发者的可执行清单。目标：**假设设备可能被越狱、镜像、备份外泄，仍然不泄露秘密。**

### 16.1 保护类选择决策树

```text
这个秘密需要跨设备/换机可用吗？
├─ 需要 → 考虑是否应该根本不放本地（改为服务端短期 token）
│         若必须放：AfterFirstUnlock（可同步）+ 服务端二次授权
└─ 不需要 → 能用 ThisDeviceOnly 吗？
            ├─ 能 → WhenUnlockedThisDeviceOnly
            └─ 很敏感且可接受"抹口令即失效" → WhenPasscodeSetThisDeviceOnly

后台任务需要在锁定时读取吗？
├─ 需要 → AfterFirstUnlockThisDeviceOnly（避免可迁移）
└─ 不需要 → WhenUnlocked（首选）

永远不要用：Always / AlwaysThisDeviceOnly（已废弃，重启即可读）
```

### 16.2 推荐模板（Swift）

```swift
import Security

enum KeychainStore {
    /// 会话凭证：本机、仅解锁时可读、不进同步
    static func saveSession(token: Data, account: String, accessGroup: String) -> OSStatus {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: account,
            kSecAttrService as String: "com.acme.session",
            kSecAttrAccessGroup as String: accessGroup,
            kSecAttrAccessible as String: kSecAttrAccessibleWhenUnlockedThisDeviceOnly,
            kSecUseDataProtectionKeychain as String: kCFBooleanTrue as Any,
            kSecValueData as String: token,
            kSecAttrLabel as String: "Acme Session"   // 注意 label 也是元数据
        ]
        SecItemDelete([kSecClass as String: kSecClassGenericPassword,
                       kSecAttrAccount as String: account,
                       kSecAttrService as String: "com.acme.session"] as CFDictionary)
        return SecItemAdd(query as CFDictionary, nil)
    }

    static func loadSession(account: String, accessGroup: String) -> Data? {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: account,
            kSecAttrService as String: "com.acme.session",
            kSecAttrAccessGroup as String: accessGroup,
            kSecReturnData as String: true,
            kSecMatchLimit as String: kSecMatchLimitOne,
            kSecUseDataProtectionKeychain as String: kCFBooleanTrue as Any
        ]
        var item: CFTypeRef?
        let status = SecItemCopyMatching(query as CFDictionary, &item)
        guard status == errSecSuccess else { return nil }
        return (item as? Data)
    }

    /// 显式清理：登出/删除账号/设备交接必须调用
    static func wipe(account: String, accessGroup: String) {
        _ = loadSession(account: account, accessGroup: accessGroup) // 占位：确保组上下文正确
        SecItemDelete([kSecClass as String: kSecClassGenericPassword,
                       kSecAttrAccount as String: account,
                       kSecAttrService as String: "com.acme.session",
                       kSecAttrAccessGroup as String: accessGroup] as CFDictionary)
    }
}
```

### 16.3 生物识别绑定（strongBox / SEP / userPresence）

```swift
func makeBiometricKey(tag: Data) -> SecKey? {
    var err: Unmanaged<CFError>?
    guard let acl = SecAccessControlCreateWithFlags(
        kCFAllocatorDefault,
        kSecAttrAccessibleWhenPasscodeSetThisDeviceOnly,
        [.biometryCurrentSet, .privateKeyUsage],
        &err
    ) else { return nil }

    let attrs: [String: Any] = [
        kSecClass: kSecClassKey,
        kSecAttrKeyType: kSecAttrKeyTypeECSECPrimeRandom,
        kSecAttrKeySizeInBits: 256,
        kSecAttrTokenID: kSecAttrTokenIDSecureEnclave,     // 关键：私钥不进主内存
        kSecPrivateKeyAttrs: [
            kSecAttrIsPermanent: true,
            kSecAttrApplicationTag: tag,
            kSecAttrAccessControl: acl
        ]
    ]
    return SecKeyCreateRandomKey(attrs as CFDictionary, &err)
}
```

要点：

1. **`.privateKeyUsage` 必须加**：否则授权可被缓存，削弱"每次在场"。
2. **`.biometryCurrentSet` 而非 `.biometryAny`**：新增指纹/面容不应自动继承解密权。
3. **`kSecAttrTokenIDSecureEnclave`**：让"密钥导出"不再是有效攻击（攻击转为"用设备"，可被在场检测与服务端异常发现）。
4. **`LAContext` 复用**：`SecItemCopyMatching` 传入 `kSecUseAuthenticationContext`，并设置 `policy = .deviceOwnerAuthenticationWithBiometrics`、`localizedReason` 明确用途，减少用户"习惯性同意"。
5. **不要在 App 内自行"缓存解密后的秘密"**超过必要时间；用 `Data.resetBytes`/安全清零意识（Swift 无法保证，但可缩小窗口）。

### 16.4 attestation：让服务端知道密钥是真的

```swift
// 生成 App Attest key 并由服务端向 Apple 验证
let provider = DCAppAttestService.shared
provider.generateKey { keyId, error in
    // 1) 用 keyId 生成 attestation（含客户端 challenge）
    // 2) 上传 → 服务端调用 App Attest API 校验 → 绑定业务账号
    // 3) 之后使用 "asserted token" 保护 API（replay 防护）
}
```

配合策略：

- 服务端**拒绝**无 attestation 的设备绑定登录（金融/加密资产场景）。
- attestation 的 `isDebug`/环境字段用于识别非生产环境。
- 结合 `DeviceCheck` 的 "verified" 标志 + 风险评分 API，识别越狱/异常设备。

### 16.5 禁止清单（Anti-patterns）

| 反模式 | 后果 | 修正 |
| --- | --- | --- |
| token 同时写 UserDefaults | 明文落地 | 只写 Keychain |
| token 放入 App Group 容器文件 | 无保护类 | 用 access group 共享 Keychain |
| 复制到剪贴板 | 键盘/历史泄露 | 不复制；必须则限时清空 |
| 打印到日志/崩溃上报 | 服务端日志泄露 | 脱敏、构建期检查 |
| 传给 WebView JS | 远程页可读 | origin 校验、原生确认 |
| `Always` 类 | 重启即读 | 迁移到 WhenUnlocked |
| `synchronizable: true` 存 API key | 上云扩散 | 显式 false |
| 卸载不删条目 | 继承会话 | 登出时 `SecItemDelete` |
| 用 service 名当"秘密" | 元数据画像 | 随机/映射名 |
| 仅靠"越狱检测"保护 | 易绕过 | 服务端 attestation + 短期化 |

### 16.6 分场景配置建议

| 数据类型 | 类 | 同步 | 生物识别 | SEP | 备注 |
| --- | --- | --- | --- | --- | --- |
| 短期 access token | WhenUnlocked | 否 | 可选 | 否 | 60 分钟内过期 |
| refresh token | WhenUnlockedThisDeviceOnly | 否 | 建议 | 否 | 服务端轮换 |
| 账号口令（离线解封用） | WhenPasscodeSetThisDeviceOnly | 否 | 是 | 否 | 允许失效重登 |
| 业务签名私钥 | — | 否 | 是 | **是** | 每次在场 |
| 本地数据库密钥 | WhenUnlockedThisDeviceOnly | 否 | 是 | 可选 | 与数据分离存储不现实，需增强 |
| Push 相关 secret | WhenUnlocked | 否 | 否 | 否 | 服务端可轮换 |
| 客户证书 + 私钥 | — | 否 | 否 | **是** | Identity + SEP |
| Passkey | 系统托管 | 是（环） | 是 | 是 | 交由系统 |

### 16.7 测试用例（把 Keychain 纳入安全测试）

1. **卸载/重装继承测试**：安装 → 登录 → 卸载 → 重装 → 是否直接已登录（应为否）。
2. **备份包含性测试**：加密备份 → 解析备份 → 检查条目/密钥是否出现。
3. **保护类行为测试**：锁定/重启后由后台扩展尝试读取，断言返回 `errSecInteractionNotAllowed`。
4. **access group 重叠测试**：同 Team 的第二个最小 App 尝试读取，确认是否预期共享。
5. **生物识别变更测试**：新增指纹后，`.biometryCurrentSet` 条目应失效。
6. **旁路落地扫描**：对容器、共享组、日志、崩溃包做全量字符串扫描（Grep token 前缀）。
7. **越狱设备真机测试**（在授权范围内）：确认产品是否具备"拒绝在高危设备运行"的能力与提示。

### 16.8 代码评审 Checklist（可直接放进 PR 模板）

```text
[ ] 所有 SecItemAdd 显式指定 kSecAttrAccessible，无隐式默认
[ ] 无 Always / AlwaysThisDeviceOnly
[ ] 秘密类条目使用 *ThisDeviceOnly
[ ] kSecAttrSynchronizable 默认 false，除非经过安全评审
[ ] 高价值私钥使用 SEP + .biometryCurrentSet + .privateKeyUsage
[ ] access group 显式声明，且有文档说明共享范围
[ ] 登出/删除账号路径调用 SecItemDelete（含所有历史组）
[ ] 无 token 进入 UserDefaults / App Group 文件 / 剪贴板 / 日志
[ ] 服务端已实施：短期 token + 设备绑定（attestation）+ 异常检测
[ ] 已加入自动化扫描：容器与备份中的敏感字符串检测
```

### 16.9 本章要点

- 最佳实践的核心不是"加密更强"，而是**让秘密更不可携带**（硬件绑定、用户在场、短期化、服务端可验证）。
- 三个必做动作：`ThisDeviceOnly`、登出清理、attestation。
- 生物识别与 SEP 是"钥匙串之上"最有价值的两件套，用错一个 flag 就会失去大部分收益。

---






---

**联系方式**：Telegram: https://t.me/one00190
