# 简报 #97（XNU 源码侧）：代码页内容校验与权限边界的归属、适用性实况（事实清单，非报告正文）

**证据等级：全部 A-（Apple 官方开源发行代码）。本简报不产出结论句，只产出可核对的事实、逐字文本与检索数值。**

---

## 目录

| 节 | 主题 | 状态 |
|---|---|---|
| §0 | 口径、射程、无版本锚点 | 已填充 |
| §1 | 「一页算不算代码页」的判据链 | 已填充（含 §1.6 补） |
| §2 | 问责层实现面（`vm_apple_protect` / `pmap_cs` / pmap 受保护页类型） | 已填充 |
| §3 | 配置门控与适用性 | 已填充（`board_config.h` 部分见 §9.1 的 §3.6） |
| §4 | 可被用户/开发者改变的状态 | 已填充 |
| §5 | 失败出口与日志格式串 | 已填充（含 §5.6 补） |
| §6 | 零命中与反例登记 | 已填充 |
| §7 | 与前篇简报的冲突或需校正之处 | 已填充 |
| §8 | 缺口清单 | 已填充（N1 作废，见 §9.3） |
| §9 | 末次实测的补充与自我勘误 | 已填充 |

**引用格式**：一律 `xnu/<文件名>:行号` 扁平写法（如 `xnu/vm_fault.c:2815`）。上游完整路径以 `osfmk/vm/vm_fault.c` 形式在括号内注明。本地镜像同时存在一份 22 文件的扁平缓存 `_tmp/pe/xnu/`，两处行号已核对一致。

---

## §0 口径、射程、无版本锚点

状态：已填充

### 0.1 证据等级与「按本树配置」限定语

本简报全部取自本地 XNU 镜像，等级统一 **A-（Apple 官方开源分发中的源码与构建脚本）**。三条限制沿用 `_facts_kaslr_xnu.md` §0.2 并已在本节实测中复核：

1. **源码注释不是 Apple 的公开承诺**。它是工程师写给工程师的说明，允许过时、允许与零售构建不一致。凡引注释，必须带「据源码注释」限定语。
2. **`#if 0` / 宏值为 0 的代码不是行为**。§1.4 与 §2 各登记了一处以上被门控关死的校验路径，只能作为「Apple 打算如何做」的证据。
3. **条件编译禁止合并陈述**。本简报所有条目均带 arch / config 限定（arm64-SPTM / arm64-PPL / x86_64 / RELEASE / DEV / DEBUG）。

**最重要的限定语**：本简报只描述「**按本树配置**，哪些代码路径存在、由哪个宏门控」。任何「零售 iPhone 上一定如此」的表述都超出本树可证范围——`board_config.h` 一类的 Apple 内部目标文件不在开源 config 中全量可得（该限制已由 `_facts_page_exec.md` / 手册侧简报记录，此处不重述其结论，只复用其射程边界）。

### 0.2 射程

**只管**：一页内存「是否被本内核当作受代码签名约束的页」的判定链、该判定的问责者（VM 自身 / apple_protect pager / 代码签名监视器 / pmap 层）、决定上述路径是否编入内核的配置开关、以及这些路径的失败出口字符串。

**不管**：任何形式的绕过、内核基址获取、内核读原语构造或符号定位——本篇为防御与研究教育视角，实现细节仅用于配置审计与取证判定链。用户态 `csops`/`codesign` 工具行为、公开文档口径均由配套简报承担。

### 0.3 镜像身份与无版本锚点声明

沿用 `_facts_kaslr_xnu.md` §0.3 的写法，原文照抄其射程边界：

> 本地镜像路径：本机 Temp 目录下的 `xnu/xnu-main`。
> 来源：apple-oss-distributions/xnu **main 分支**快照。
> 语料规模：`find . \( -name '*.c' -o -name '*.h' -o -name '*.cpp' -o -name '*.m' \) | wc -l` = **4160**（本次实测复核，与既有简报一致）。
> 无 `.git`、无 `version.h`、无 `config/ABMS`、无 `Config*.mk`。

由此得出本简报的**无版本锚点声明**：本树**不能**被锚定到任何 XNU 发布标签或 iOS/macOS 版本号；下文所有行号仅对本快照有效，历史版本行号不同。**不得**把本节任何开关取值读作「某零售版本的默认值」。

---

## §1 「一页算不算代码页」的判据链

状态：已填充

判定发生在缺页处理里，函数是 `vm_fault_cs_check_violation()`（`xnu/vm_fault.c:2730-2731`，上游 `osfmk/vm/vm_fault.c`；`static kern_return_t`）。它之前的两个辅助函数与它内部的判定顺序如下。

### 1.1 对象级前置：`object->code_signed`

`xnu/vm_fault.c:2668`（位于 `vm_fault_cs_need_validation` 一侧的准备逻辑；该位是「这页有没有签名可查」的第一道闸）：

> ```c
> 	if (!page_obj->code_signed) {
> ```

另一处同族判据在页校验复用路径上写得更完整——`xnu/vm_fault.c:8874-8877`（逐字）：

> ```c
> 	if (!object->code_signed ||     /* no code signature to check */
> 	    object->internal ||         /* internal objects aren't signed */
> 	    object->terminating ||      /* the object and its pages are already going away */
> 	    !object->pager_ready) {     /* this should happen, but check shouldn't hurt */
> 		goto done;
> 	}
> ```

口径：`code_signed` 是 **vm_object 级**的属性，不是页级；本树里判「算不算代码页」的第一条件是对象是否 `code_signed`，且必须是 vnode-pager 后备对象（`xnu/vm_fault.c:8866-8869` 的 `vnode_pager_lookup_vnode(object->pager)` 返回 NULL 即 `goto done`）。

### 1.2 页级三态位：`vmp_cs_validated / vmp_cs_tainted / vmp_cs_nx`

`xnu/vm_page.h:322-324`（上游 `osfmk/vm/vm_page.h`，逐字）：

> ```c
> 	    vmp_cs_validated:VMP_CS_BITS,     /* code-signing: page was checked */
> 	    vmp_cs_tainted:VMP_CS_BITS,       /* code-signing: page is tainted */
> 	    vmp_cs_nx:VMP_CS_BITS,            /* code-signing: page is nx */
> ```

这三个位是**按超大页内每个 16K 粒度分档**存储的（`VMP_CS_BITS`），因此访问必须经 `VMP_CS_VALIDATED(m, size, offset)` / `VMP_CS_TAINTED(...)` / `VMP_CS_NX(...)` 三个宏；判定链里出现的都是宏而非裸位。

### 1.3 判定顺序（`vm_fault_cs_check_violation` 内部，逐字行号登记）

| 行 | 逐字关键内容 | 作用 |
|---|---|---|
| `xnu/vm_fault.c:2745` | `#if !CODE_SIGNING_MONITOR` | 整个函数的门控起点（见 §1.4） |
| `xnu/vm_fault.c:2751-2753` | `if (!cs_bypass && vm_fault_cs_need_validation(pmap, m, object, fault_page_size, fault_phys_offset)) {` | 只有不 bypass 且需要校验才继续 |
| `xnu/vm_fault.c:2756-2757` | `if (VMP_CS_VALIDATED(...)) { vm_cs_revalidates++; }` | 已校验过又回来 = 重校验计数 |
| `xnu/vm_fault.c:2763-2770` | `#if CODE_SIGNING_MONITOR` / `if (fault_info->csm_associated && csm_enabled() && !VMP_CS_VALIDATED(...) && !VMP_CS_TAINTED(...) && !VMP_CS_NX(...) && (prot & VM_PROT_EXECUTE) && (caller_prot & VM_PROT_EXECUTE)) {` | 有条件把校验**让给代码签名监视器** |
| `xnu/vm_fault.c:2771-2775` | 注释 `In the code signing monitor we trust :)`（据源码注释） | 「VM 不再重复校验」的自述理由 |
| `xnu/vm_fault.c:2776` | `vm_cs_defer_to_csm++;` | 让路分支计数 |
| `xnu/vm_fault.c:2778-2779` | `vm_cs_defer_to_csm_not++;` / `vm_page_validate_cs(m, fault_page_size, fault_phys_offset);` | 不让路则 VM 自己校验 |
| `xnu/vm_fault.c:2782` | `vm_page_validate_cs(m, fault_page_size, fault_phys_offset);` | 非监视器构建的唯一校验调用 |
| `xnu/vm_fault.c:2796-2801` | `if (pmap == kernel_pmap) { /* kernel fault: cs_enforcement does not apply */ cs_enforcement_enabled = 0; } else { cs_enforcement_enabled = pmap_get_vm_map_cs_enforced(pmap); }` | **强制与否向 pmap 询问**，内核缺页一律不强制 |
| `xnu/vm_fault.c:2811-2813` | `if (cs_enforcement_enabled && vm_fault_cs_page_nx(...) && (prot & VM_PROT_EXECUTE)) {` | NX 页拒绝映射为 EXEC |
| `xnu/vm_fault.c:2831-2838` | `if (cs_bypass) { *cs_violation = FALSE; } else if (VMP_CS_TAINTED(...)) { *cs_violation = TRUE; } else if (!cs_enforcement_enabled) { /* no further code-signing enforcement */` | 违规判定的三级瀑布（下游 `2838-2871 → KERN_CODESIGN_ERROR` 见 `_facts_page_exec.md:556`，不重述） |

口径：本树里「一页算不算受签名约束的代码页」= `!cs_bypass` ∧ 对象 `code_signed` ∧ `vm_fault_cs_need_validation()` ∧ 该 pmap 的 `cs_enforced`；四者缺一即不进入逐页校验。**逐页哈希本身**由 `vm_page_validate_cs()` 触发，或在 `CODE_SIGNING_MONITOR` 构建里交给监视器。

### 1.4 `vmp_canonical`：与「代码页」正交的另一条不可变判据

`xnu/vm_page.h:274`（逐字）：

> ```c
> 		uint8_t                 vmp_canonical:1;    /* this page is a canonical kernel page (immutable) */
> ```

它处在一个**独立 struct 位域**里，理由写在 `xnu/vm_page.h:266-268`（据源码注释，逐字）：

> ```c
> 	 * This bitfield is kept in its own struct to prevent coalescing
> 	 * with the next one (which C allows the compiler to do) as they
> 	 * are under different locking domains
> ```

置位点全部在物理页分配侧，与代码签名无关：`xnu/vm_resident.c:937`、`:949`、`:957`、`:2894`、`:3853`（`.vmp_canonical = vm_page_in_array(mem),`）、`:5148`（`.vmp_canonical = true,`）；读回点是 `xnu/vm_resident.c:3903`（`return m->vmp_canonical;`）。约束示例 `xnu/vm_page.h:456`：`assert(!vm_page_in_array(m) && !m->vmp_canonical);`

口径：`vmp_canonical` 是**内核页的不可变性**标记，**不是**「代码页」标记；报告若把二者混为一谈即属越界。本树内没有任何一行把 `vmp_canonical` 与 `vmp_cs_*` 组合判定。

### 1.5 真实存在的逐页校验函数族（及其门控）

`xnu/vm_page_internal.h:848-861` 是本族对外的声明（四个 `extern`）。实现全部在 `xnu/vm_fault.c`：

| 函数 | 定义行 | 备注 |
|---|---|---|
| `vm_page_validate_cs_fast()` | `xnu/vm_fault.c:8094`（注释块自 `:8084` `vm_page_validate_cs_fast():`） | 静态内联的快速分档判定 |
| `vm_page_validate_cs_mapped_slow()` | `xnu/vm_fault.c:8161` | 已映射情形的慢路径 |
| `vm_page_validate_cs_mapped()` | `xnu/vm_fault.c:8219` | 内部先 `:8225` 试 fast，失败落 `:8226` slow |
| `vm_page_validate_cs()` | `xnu/vm_fault.c:8299` | `:8309` 先查 fast 命中即返回 |
| `vm_page_validate_cs_mapped_chunk()` | `xnu/vm_fault.c:8316` | 分块校验 |

调用者（即「谁会把一页送去算哈希」），实测 4 处外部调用：
`xnu/vm_apple_protect.c:511`（`vm_page_validate_cs_mapped(`）、`xnu/vm_dyld_pager.c:1134`、`xnu/vm_shared_region_pager.c:678`、`xnu/vm_resident.c:6383`（`vm_page_validate_cs(src_m, PAGE_SIZE, 0);`）。

**反例登记（重要）**：本树**不存在**名为 `vm_page_validate_cs_disposed` 的函数（见 §6）。`xnu/vm_fault.c:8860-8905` 这段「已处置页重新校验」语境的代码属于 `revalidate_text_page(task_t task, vm_map_offset_t code_addr)`（定义在 `xnu/vm_fault.c:8825-8826`，非 `vm_page_validate_*` 家族），其自述注释逐字（`xnu/vm_fault.c:8815-8823`）：

> ```c
> /*
>  * A fatal process error has occurred in the given task.
>  * Recheck the code signing of the text page at the given
>  * address to check for a text page corruption.
>  *
>  * Returns KERN_FAILURE if a page was found to be corrupt
>  * by failing to match its code signature. KERN_SUCCESS
>  * means the page is either valid or we don't have the
>  * information to say it's corrupt.
>  */
> ```

`vm_fault_cs_need_validation()`（定义 `xnu/vm_fault.c:2656-2657`）之前的四条件注释块是本树对「什么情况下才算需要逐页校验」的最直接自述，逐字（`xnu/vm_fault.c:2648-2655`）：

> ```c
> /*
>  * CODE SIGNING:
>  * When soft faulting a page, we have to validate the page if:
>  * 1. the page is being mapped in user space
>  * 2. the page hasn't already been found to be "tainted"
>  * 3. the page belongs to a code-signed object
>  * 4. the page has not been validated yet or has been mapped for write.
>  */
> ```

其函数体逐条对应：`xnu/vm_fault.c:2664-2666`（`pmap == kernel_pmap` → `return false`，即条件 1）、`:2668-2670`（`!page_obj->code_signed` → 条件 3）、`:2678-2681`（`vmp_cs_tainted == VMP_CS_ALL_TRUE` → 条件 2）、`:2682-2686`（`vmp_cs_validated == VMP_CS_ALL_TRUE && !page->vmp_wpmapped` → 条件 4）；超大页分支为 `:2689-2697`。`vmp_wpmapped` 出现在条件 4 里，口径：曾被写过映射的页即便「全档已校验」也会重走校验。

---

## §1.6（补）真正算哈希的那一行：`cs_validate_page()`

§1.5 的函数族只是**调度层**；本树里逐页哈希比对的实现体是另一个符号：

| 环节 | `文件:行号` | 逐字 |
|---|---|---|
| 声明 | `xnu/vm_protos.h:280`（上游 `osfmk/vm/vm_protos.h`） | `extern void cs_validate_page(` |
| 唯一 VM 侧调用点 | `xnu/vm_fault.c:8196`（位于 `vm_page_validate_cs_mapped_slow()`，定义行 `:8161`） | `cs_validate_page(vnode,` |
| 定义 | `xnu/ubc_subr.c:6010-6018`（上游 `bsd/kern/ubc_subr.c`） | `void` / `cs_validate_page(` / 参数 `struct vnode *vp, memory_object_t pager, memory_object_offset_t page_offset, const void *data, int *validated_p, int *tainted_p, int *nx_p` |

三个输出参数 `validated_p / tainted_p / nx_p` 正是 §1.2 三个页级位桶的写入源。逐字关键判定：

> ```c
> 5868:		size = (1U << blob->csb_hash_pageshift);
> 5906:		if (bcmp(expected_hash, actual_hash, hashtype->cs_size) != 0) {
> 5919:			*tainted |= CS_VALIDATE_TAINTED;
> 5928:		validated = TRUE;
> 5863:		validated = FALSE;
> ```

补充两个计数器与一个条件编译（逐字）：`xnu/ubc_subr.c:5770` `unsigned long cs_validate_page_no_hash = 0;`、`:5771` `unsigned long cs_validate_page_bad_hash = 0;`；`xnu/ubc_subr.c:6025-6026`

> ```c
> #if CONFIG_SUPPLEMENTAL_SIGNATURES
> 	if (blobs == NULL && proc_is_translated(current_proc())) {
> ```

口径：`cs_validate_page()` 的三个输出参数在 `cs_debug` 分级下各自有打印（§5.6）。`size = (1U << blob->csb_hash_pageshift)`（`:5868`）说明**哈希粒度取自 Code Directory blob 的 `csb_hash_pageshift`**，不是固定 4K；这是「逐页校验」的真实粒度来源，报告若要写「按页」应带此限定。

---

## §2 问责层实现面

状态：已填充

### 2.1 `vm_apple_protect.c`：解密/压缩代码页的 EMM，自带一次逐页校验

角色证据（上游 `osfmk/vm/vm_apple_protect.c`）。文件头注释逐字（`xnu/vm_apple_protect.c:63-75`，据源码注释）：

> ```c
> /*
>  * APPLE PROTECT MEMORY PAGER
>  *
>  * This external memory manager (EMM) handles memory from the encrypted
>  * sections of some executables protected by the DSMOS kernel extension.
>  *
>  * It mostly handles page-in requests (from memory_object_data_request()) by
>  * getting the encrypted data from its backing VM object, itself backed by
>  * the encrypted file, decrypting it and providing it to VM.
>  *
>  * The decrypted pages will never be dirtied, so the memory manager doesn't
>  * need to handle page-out requests (from memory_object_data_return()).  The
>  * pages need to be mapped copy-on-write, so that the originals stay clean.
> ```

关键事实：解密后**仍然逐页送校验**——`xnu/vm_apple_protect.c:510-511`（逐字）：

> ```c
> 		if (src_page_object->code_signed) {
> 			vm_page_validate_cs_mapped(
> ```

其失败诊断格式串见 §5。pager 回调族在 `xnu/vm_apple_protect.c:85-107` 声明、`:227`（`apple_protect_pager_init`）、`:318`（`apple_protect_pager_data_request`，即页入主入口）、`:1016`（`apple_protect_pager_create`）、`:1190`（`apple_protect_pager_setup`）中定义。本文件内**没有** `object_lockment` / `PMAP_COMPACT` / `pmapprov` 任何一处（实测 0 命中，见 §6）。

相关开关：挂载/文件系统侧的 `config_protect` 出现在 `config/MASTER.arm64.MacOSX:30` 的 `FILESYS_BASE` 行（`... fs_compression config_protect config_mnt_rootsnap ...`）。

### 2.2 `pmap_cs.c` 的实况：两个同名文件都是**占位符**

实测：`pmap_cs` 相关共有三个文件——

| 文件 | 行数 | 实况 |
|---|---|---|
| `osfmk/arm64/sptm/pmap/pmap_cs.c` | **33** | 仅 license 头 + `#include <arm64/sptm/pmap/pmap_internal.h>` + 一段注释；**零函数、零符号** |
| `osfmk/arm/pmap/pmap_cs.c` | **33** | 同样只有 license 头 + `#include <arm/pmap/pmap_internal.h>` + 同一段注释；**零函数、零符号** |
| `osfmk/vm/pmap_cs.h` | **850** | 真正实现面全在这里（受保护类型枚举、代码目录结构体等） |

两个 `.c` 的注释逐字完全一致（`osfmk/arm64/sptm/pmap/pmap_cs.c:30-33` 与 `osfmk/arm/pmap/pmap_cs.c:30-33`）：

> ```c
> /**
>  * Placeholder for pmap CS functionality. Eventually all PPL code relating to
>  * code signing will be placed into this file.
>  */
```

口径：报告若引用「`pmap_cs.c` 的实现」即为**事实错误**——本树里它是**计划占位**（据源码注释，"Eventually … will be placed into this file"）。`pmap_cs` 符号的 263 处命中（分母 4160，见 `_notes97_probe.md` N5）全部落在头文件与其它 pmap 实现里，不在 `pmap_cs.c`。

### 2.3 pmap 侧「受保护页」的真实类型名：`pmap_cs_trust_t`

本树确实存在的受信任等级枚举是 `osfmk/vm/pmap_cs.h:253-323` 的 `typedef enum { … } pmap_cs_trust_t;`。成员逐字（含各自行号，省略中间注释行）：

> ```c
> 	PMAP_CS_UNTRUSTED = 0,                    /* :254 */
> 	PMAP_CS_RETIRED,                          /* :261 */
> 	PMAP_CS_PROFILE_PREFLIGHT,                /* :269 */
> 	PMAP_CS_COMPILATION_SERVICE,              /* :275 */
> 	PMAP_CS_OOP_JIT,                          /* :282 */
> 	PMAP_CS_LOCAL_SIGNING,                    /* :288 */
> 	PMAP_CS_PROFILE_VALIDATED,                /* :294 */
> 	PMAP_CS_APP_STORE,                        /* :299 */
> 	PMAP_CS_ENGINEERING_SIGNED_WITH_CMS,      /* :307，在 #if PMAP_CS_INCLUDE_INTERNAL_CODE 内 */
> 	PMAP_CS_IN_LOADED_TRUST_CACHE,            /* :313 */
> 	PMAP_CS_IN_STATIC_TRUST_CACHE,            /* :314 */
> 	PMAP_CS_ENGINEERING_SIGNED,               /* :321，在 #if PMAP_CS_INCLUDE_INTERNAL_CODE 内 */
> ```

两条阈值宏逐字（`xnu/pmap_cs.h:326` 与 `:329`）：

> ```c
> #define PMAP_CS_LOWEST_PLATFORM_BINARY_TRUST PMAP_CS_IN_LOADED_TRUST_CACHE
> #define PMAP_CS_LOWEST_ACCEPTABLE_TRUST PMAP_CS_COMPILATION_SERVICE
> ```

口径：`PMAP_CS_IN_LOADED_TRUST_CACHE` / `PMAP_CS_IN_STATIC_TRUST_CACHE` 是本树里**唯一**以「信任缓存」为名的受信任等级，二者与 `#if PMAP_CS_INCLUDE_INTERNAL_CODE`（`xnu/pmap_cs.h:301`、`:316`）的嵌套关系应在报告中如实标注：工程签名档只在内部代码开启时存在。

### 2.4 pmap_cs.h 的宏分层：PPL 监视器 vs 一般 CS

`xnu/pmap_cs.h:66-92`（逐字关键行）：

> ```c
> #if PMAP_CS                                            /* :66 */
> #define PMAP_CS_INCLUDE_CODE_SIGNING 1                 /* :67 */
> #define PMAP_CS_PPL_MONITOR 0                          /* :71（某一分支） */
> #define PMAP_CS_PPL_MONITOR 1                          /* :73（某一分支） */
> #if PMAP_CS_PPL_MONITOR                                /* :78 */
> ```

注释自述的依赖关系逐字（`xnu/pmap_cs.h:81-92`，据源码注释）：

> ```c
>  * XNU_MONITOR and PMAP_CS are both defined for the same targets in board_config.h.
>  * As a result, whenever XNU_MONITOR is defined, so is PMAP_CS. In an ideal world, we
>  * can remove the use of PMAP_CS macro and simply use XNU_MONITOR, but that would
>  ...
>  * PMAP_CS_PPL_MONITOR is defined when we have XNU_MONITOR _and_ we explicitly don't
>  * have CONFIG_SPTM. This effectively means that whenever we have PMAP_CS_PPL_MONITOR,
>  * we should also always have PMAP_CS_INCLUDE_CODE_SIGNING. Lets enforce this with a
>  ...
> #if !PMAP_CS_INCLUDE_CODE_SIGNING
> #error "PMAP_CS_INCLUDE_CODE_SIGNING not defined when under PMAP_CS_PPL_MONITOR"
> ```

口径：这是本简报关于「PPL 与 SPTM 谁承担 CS 职责」最硬的一条源码证据——`PMAP_CS_PPL_MONITOR` 的成立条件被注释明写为「有 XNU_MONITOR 且**明确没有** `CONFIG_SPTM`」，且 `board_config.h` 被点名是决定 `PMAP_CS` 是否定义的文件（该文件不在开源树内，见 §8）。同文件 `xnu/pmap_cs.h:28` 声明这些定义「**private**，只应在 XNU 内访问」：

> ```c
>  * All of PMAP_CS definitions are private and should remain accessible only within XNU
> ```

### 2.5 `pmap_get_vm_map_cs_enforced()`：VM 向 pmap 问「这个 map 是否强制」

声明 `xnu/pmap.h:829`（逐字）：

> ```c
> extern bool pmap_get_vm_map_cs_enforced(pmap_t pmap);
> ```

调用点实测：`xnu/vm_fault.c:2800`（判定链，见 §1.3）、`xnu/vm_fault.c:4011`（`assert(pmap_get_vm_map_cs_enforced(pmap));`）、`xnu/vm_shared_region.c:936` 与 `:939`（两处 assert，分别针对 `sub_map` 与 `config_map`）。

另一条与 pmap 交互的执行侧标志：`xnu/vm_fault.c:3524` 逐字

> ```c
> 	    ((fault_info->fi_xnu_user_debug && !VM_PAGE_OBJECT(m)->code_signed) ? PMAP_OPTIONS_XNU_USER_DEBUG : 0),
> ```

口径：`PMAP_OPTIONS_XNU_USER_DEBUG` 是本树里与「非签名代码页进入 pmap」直接相关的一个 pmap_enter 选项，且其成立条件明写 `!code_signed`。

---

## §3 配置门控与适用性

状态：已填充

本节只描述 Apple 自己的配置项与其注释/构建脚本所述后果，不推断零售行为。

### 3.1 三层开关的源码位置（逐字）

**(a) 编译期 option 声明** —— `config/MASTER`：

> ```
> 93:# secure_kernel - secure kernel from user programs
> 94:options     SECURE_KERNEL       # <secure_kernel>
> ```
> ```
> 320:# Compile XNU with SPTM support. This option should be used mutually exclusively
> 321:# with the config_pmap_ppl option
> 322:options   CONFIG_SPTM				# <config_sptm>
> 325:# Enable support for commmunication with exclaves. Requires SPTM support.
> 326:options		CONFIG_EXCLAVES			# <config_exclaves>
> 328:# Compile XNU with PPL support.
> 329:options   CONFIG_PMAP_PPL			# <config_pmap_ppl>
> 334:# support dynamic signing of code
> 336:options		CONFIG_DYNAMIC_CODE_SIGNING	# <dynamic_codesigning>
> 338:# enforce library validation on all processes.
> 340:options		CONFIG_ENFORCE_LIBRARY_VALIDATION  # <config_library_validation>
```

口径：`config/MASTER:320-321` 是「**SPTM 与 PPL 互斥**」这一命题的**唯一一手源码依据**，且原文是注释里的一句建议（"should be used mutually exclusively"，据源码注释），不是 `#error` 级的强制检查。

**(b) 平台清单** —— `config/MASTER.arm64.*`（这些行本身是 `#` 注释形式书写的配置说明）：

> ```
> config/MASTER.arm64.iPhoneOS:19:#  ARM_EXTRAS_BASE = [ config_pmap_ppl ]
> config/MASTER.arm64.iPhoneOS:20:#  KERNEL_BASE =    [ arm64 xsmall msgb_small config_embedded config_enforce_signed_code config_code_signature_reconstitution config_requires_u32_munging config_darkboot ARM_EXTRAS_BASE ]
> ```
> ```
> config/MASTER.arm64.MacOSX:17:#if MASTER_CONFIG_ENABLE_SPTM
> config/MASTER.arm64.MacOSX:18:#  ARM_EXTRAS_BASE = [ config_sptm config_sptm_iommu ]
> config/MASTER.arm64.MacOSX:19:#else
> config/MASTER.arm64.MacOSX:20:#  ARM_EXTRAS_BASE = [ config_pmap_ppl ]
> config/MASTER.arm64.MacOSX:21:#endif
> config/MASTER.arm64.MacOSX:22:#  KERNEL_BASE =    [ arm64 medium msgb_large config_requires_u32_munging config_delay_idle_sleep config_proc_udata_storage config_uexc config_darkboot ARM_EXTRAS_BASE ]
> ```
> ```
> config/MASTER.arm64.WatchOS:19:#  ARM_EXTRAS_BASE = [ config_pmap_ppl ]
> config/MASTER.arm64.BridgeOS:19:#  KERNEL_BASE =    [ arm64 config_pmap_ppl xsmall msgb_small config_embedded config_enforce_signed_code config_code_signature_reconstitution config_requires_u32_munging config_darkboot ARM_EXTRAS_BASE ]
> config/MASTER.arm64:19:#  KERNEL_BASE =    [ arm64 config_pmap_ppl xsmall msgb_small config_embedded config_enforce_signed_code config_code_signature_reconstitution config_requires_u32_munging config_darkboot ARM_EXTRAS_BASE ]
> config/MASTER.arm:19:#  KERNEL_BASE =    [ arm xsmall msgb_small config_embedded config_enforce_signed_code config_code_signature_reconstitution config_darkboot ARM_EXTRAS_BASE ]
> ```

**(c) `secure_kernel` 的清单落点**（逐字，`config/MASTER.arm64.iPhoneOS:28`）：

> ```
> #  BSD_RELEASE =    [ BSD_BASE no_printf_str no_kprintf_str secure_kernel config_imgsrc_access ]
> ```

对照 `config/MASTER` 的 BSD_RELEASE 与 `MASTER.arm64.MacOSX:27`（`#  BSD_RELEASE =    [ BSD_BASE ]`）。口径：`secure_kernel` 在本树里是**写在 BSD_RELEASE 组件清单里的一行**，不是默认全局打开——沿用 `_facts_kaslr_xnu.md` §9.4 的边界：它是「面向用户态隐藏内核信息的编译期总闸」，本篇不重述其结论，只登记其在 iPhoneOS 清单上的落点。

### 3.2 SPTM 的启用判定发生在 **构建脚本**，而非 config 文件

`makedefs/MakeInc.def:32-34`（逐字）：

> ```
> SUPPORTED_ARM64_MACHINE_CONFIGS = T6000 T6020 T6030 T6031 T6041 T8101 T8103 T8112 T8122 T8132 VMAPPLE
> 
> SPTM_ENABLED_SOCS_MacOSX = t8112 t6020 t8122 t6030 t6031 t8132 t6041
> ```

`makedefs/MakeInc.def:1104` / `:1110-1123`（逐字）：

> ```
> SPTM_ENABLED := $(shell $(EMBEDDED_DEVICE_MAP) -db $(EDM_DBPATH) -query SELECT DISTINCT EnableSPTMTXM FROM Targets WHERE KernelPlatform IS \"$(CURRENT_MACHINE_CONFIG_LC)\" AND SDKPlatform IS \"$(PLATFORM_NORMALIZED)\" AND EnableSPTMTXM == 1)
> ```
> ```
> ifeq ($(CURRENT_KERNEL_CONFIG),SPTM)
>   SPTM_ENABLED = 1
> endif # SPTM
> 
> ifneq ($(filter $(CURRENT_MACHINE_CONFIG_LC),$(SPTM_ENABLED_SOCS_$(PLATFORM))),)
>   SPTM_ENABLED = 1
> endif
> 
> ifeq ($(SPTM_ENABLED),1)
>   LD_KERNEL_ARCHIVES += -lTrustedExecutionMonitor_Kernel
>   LD_KERNEL_ARCHIVES += -lsptm_xnu
>   DOCONF_SPTM := -sptm
> endif # !SPTM_ENABLED
> ```

配套：`SETUP/config/doconf:78` `set DEF_SPTM="-UMASTER_CONFIG_ENABLE_SPTM"`（默认**未定义**）与 `:157-158`

> ```
> 	case "-sptm":
> 	    set DEF_SPTM="-DMASTER_CONFIG_ENABLE_SPTM"
> ```

**覆盖面收窄的第一条硬事实**：`SPTM_ENABLED_SOCS_*` 变量**只有 `_MacOSX` 一个后缀**（`grep -rn 'SPTM_ENABLED_SOCS'` 实测仅 2 处命中：`makedefs/MakeInc.def:34` 与 `:1115`）。因此 `$(SPTM_ENABLED_SOCS_$(PLATFORM))` 在非 MacOSX 平台展开为空，`MakeInc.def:1115` 的 filter 分支永不命中。这与 `_facts_kaslr_xnu.md` 的记录一致，本简报只补充其机制解释：**本树里 iPhoneOS 侧的 SPTM 启用只能来自 `MakeInc.def:1104` 的 EMBEDED-DEVICE-MAP 数据库查询（`EnableSPTMTXM` 列）或 `CURRENT_KERNEL_CONFIG == SPTM`，两者都依赖树外输入**（`EDM_DBPATH` 数据库与构建环境）。所以：

- **不得**从 `MASTER.arm64.iPhoneOS:19` 写死 `config_pmap_ppl` 读出「零售 iPhone 用 PPL」；该行只是清单基线，SPTM 分支在 iPhoneOS 文件里根本不提供 `ARM_EXTRAS_BASE` 切换（对比 MacOSX 的 `#if/#else`）。
- **也不得**从 `EnableSPTMTXM == 1` 读出具体机型——该 SQL 依赖的 `.db` 不在开源树。

### 3.3 代码签名监视器的三态派发（本树最核心的适用性开关）

`bsd/sys/code_signing_internal.h:43-58`（逐字，未省略任何一行）：

> ```c
> #if CONFIG_SPTM
> /* TrustedExecutionMonitor */
> #define CODE_SIGNING_MONITOR 1
> #define CODE_SIGNING_MONITOR_PREFIX txm
> 
> #elif PMAP_CS_PPL_MONITOR
> /* Page Protection Layer -- PMAP_CS */
> #define CODE_SIGNING_MONITOR 1
> #define CODE_SIGNING_MONITOR_PREFIX ppl
> 
> #else
> /* No monitor -- XNU */
> #define CODE_SIGNING_MONITOR 0
> #define CODE_SIGNING_MONITOR_PREFIX xnu
> 
> #endif /* CONFIG_SPTM */
> ```

三个后果逐条登记：

1. `CODE_SIGNING_MONITOR == 0` 时，§1.3 的 `xnu/vm_fault.c:2763-2783` 让路分支整体消失，`vm_page_validate_cs()` 由 VM 自己调用（`:2782`）——即**逐页校验回到内核内**，问责层只剩内核自身。
2. `CODE_SIGNING_MONITOR == 1` 时，`:2771-2775` 的注释（"In the code signing monitor we trust :)"，据源码注释）说明 VM 不再重复校验；监视器实现在 `bsd/kern/code_signing/txm.c` 或 `bsd/kern/code_signing/ppl.c`，由 `CSM_PREFIX(name)`（`bsd/sys/code_signing_internal.h:64-66`）在链接期决定符号前缀 `txm_` / `ppl_` / `xnu_`。
3. **`PMAP_CS_PPL_MONITOR` 本身**要求 `XNU_MONITOR` 且明确无 `CONFIG_SPTM`（`xnu/pmap_cs.h:86-88` 注释），而 `PMAP_CS`/`XNU_MONITOR` 的定义被注释点名来自 `board_config.h`（`xnu/pmap_cs.h:81`）——该文件不在开源树，见 §8。

### 3.4 `CONFIG_ENFORCE_SIGNED_CODE` 与 `CONFIG_CODE_SIGNATURE_RECONSTITUTION` 的平台差

实测（`grep -rn` 全树，include glob 见 §6）：

| 出现位置 | 逐字 |
|---|---|
| `config/MASTER.arm64:19`、`config/MASTER.arm64.BridgeOS:19`、`config/MASTER.arm64.iPhoneOS:20`、`config/MASTER.arm64.WatchOS:20`、`config/MASTER.arm:19` | 在 `KERNEL_BASE` 行中同时列出 `config_enforce_signed_code` 与 `config_code_signature_reconstitution` |
| `config/MASTER.arm64.MacOSX:22` | `KERNEL_BASE` 行**不含**这两项 |
| `bsd/kern/kern_cs.c:94` | `#if CONFIG_ENFORCE_SIGNED_CODE` |
| `bsd/kern/kern_cs.c:973`、`:992`、`:1010` | `#if !CONFIG_ENFORCE_SIGNED_CODE` |
| `bsd/kern/mach_loader.c:767` | `#ifndef CONFIG_ENFORCE_SIGNED_CODE` |
| `bsd/kern/mach_loader.c:1791` | `#if CONFIG_ENFORCE_SIGNED_CODE` |
| `bsd/kern/ubc_subr.c:4620` | `#if CONFIG_CODE_SIGNATURE_RECONSTITUTION` |

`bsd/kern/kern_cs.c:203` 的一句注释（据源码注释）逐字片段：

> ```
> 	 * CONFIG_ENFORCE_SIGNED_CODE, we can assume there is a policy
> ```

口径：本树的**清单差**显示 `config_enforce_signed_code` 只进 arm/arm64 嵌入式系（含 iPhoneOS、BridgeOS、WatchOS）与通用 `MASTER.arm64`，不进 MacOSX 的 `KERNEL_BASE`；其代码后果是 `kern_cs.c` / `mach_loader.c` 里成对的 `#if` / `#if !` 分支。报告可据此说明「覆盖面收窄的组合之一」= 「无 monitor（`CODE_SIGNING_MONITOR 0`）∧ 无 `CONFIG_ENFORCE_SIGNED_CODE`」，但**不得**据此断言任何具体设备。

### 3.5 entitlement / 签名侧的常量

`CS_ENFORCEMENT` 在本树命中 23 处 / 9 文件（分母 4160，见 `_notes97_probe.md` N5）。其在 pmap_cs 侧的对应类型是 §2.3 的 `pmap_cs_trust_t`；另有 `bsd/sys/code_signing.h:634`（逐字）

> ```c
> kern_return_t
> csm_register_code_signature(
> ```

的注释自述注册语义（`xnu/code_signing.h:626-632`）：

> ```c
> /**
>  * Register a code signature with the monitor environment. The monitor will either
>  * allocate its own memory for the code signature, or it will lockdown the memory which
>  * is given to it. In either case, the signature will be read-only for the kernel.
>  *
>  * If the monitor doesn't enforce code signing, then this function will return the
>  * KERN_SUCCESS condition.
>  */
```

口径：`"If the monitor doesn't enforce code signing, then this function will return the KERN_SUCCESS condition."`（据源码注释）是本树关于「监视器存在但不强制时该接口退化为空操作」的唯一自述。签名在注册后即对内核只读，是本简报「绑定即固化」链条的一条源码证据。

---

## §4 可被用户/开发者改变的状态

状态：已填充

### 4.1 `bsd/kern/kern_cs.c`：强制开关在 `SECURE_KERNEL` 两侧走**不同类型**

`xnu/kern_cs.c:94-106`（逐字）：

> ```c
> #if CONFIG_ENFORCE_SIGNED_CODE
> #define DEFAULT_CS_SYSTEM_ENFORCEMENT_ENABLE 1
> #define DEFAULT_CS_PROCESS_ENFORCEMENT_ENABLE 1
> #else
> #define DEFAULT_CS_SYSTEM_ENFORCEMENT_ENABLE 1
> #define DEFAULT_CS_PROCESS_ENFORCEMENT_ENABLE 0
> #endif
> 
> #if CONFIG_ENFORCE_LIBRARY_VALIDATION
> #define DEFAULT_CS_LIBRARY_VA_ENABLE 1
> #else
> #define DEFAULT_CS_LIBRARY_VA_ENABLE 0
> #endif
> ```

`xnu/kern_cs.c:108-136`（逐字，含 `#if SECURE_KERNEL` 两侧）：

> ```c
> #if SECURE_KERNEL
> 
> /*
>  *  Here we split cs_enforcement_enable into cs_system_enforcement_enable and cs_process_enforcement_enable
>  *
>  *  cs_system_enforcement_enable governs whether or not system level code signing enforcement mechanisms
>  *  are applied on the system. Today, the only such mechanism is code signing enforcement of the dyld shared
>  *  cache.
>  *
>  *  cs_process_enforcement_enable governs whether code signing enforcement mechanisms are applied to all
>  *  processes or only those that opt into such enforcement.
>  *
>  *  (On iOS and related, both of these are set by default. On macOS, only cs_system_enforcement_enable
>  *  is set by default. Processes can then be opted into code signing enforcement on a case by case basis.)
>  */
> SECURITY_READ_ONLY_EARLY(int) cs_system_enforcement_enable = DEFAULT_CS_SYSTEM_ENFORCEMENT_ENABLE;
> SECURITY_READ_ONLY_EARLY(int) cs_process_enforcement_enable = DEFAULT_CS_PROCESS_ENFORCEMENT_ENABLE;
> SECURITY_READ_ONLY_EARLY(int) cs_library_val_enable = DEFAULT_CS_LIBRARY_VA_ENABLE;
> 
> #else /* !SECURE_KERNEL */
> int cs_enforcement_panic = 0;
> int cs_relax_platform_task_ports = 0;
> 
> SECURITY_READ_ONLY_LATE(int) cs_system_enforcement_enable = DEFAULT_CS_SYSTEM_ENFORCEMENT_ENABLE;
> SECURITY_READ_ONLY_LATE(int) cs_process_enforcement_enable = DEFAULT_CS_PROCESS_ENFORCEMENT_ENABLE;
> 
> SECURITY_READ_ONLY_LATE(int) cs_library_val_enable = DEFAULT_CS_LIBRARY_VA_ENABLE;
> 
> #endif /* !SECURE_KERNEL */
> ```

口径（据源码注释，注意最后一段括号是注释而非可执行代码）：`SECURE_KERNEL` 打开时三个变量是 `SECURITY_READ_ONLY_EARLY`，关闭时是 `SECURITY_READ_ONLY_LATE`。两者都**不是启动后可改**的；差别在固化时刻（早期 vs 晚期）。注释自述的系统级强制「目前唯一机制是 dyld 共享缓存的代码签名强制」（`xnu/kern_cs.c:113-115`），这是本报告写「系统级强制覆盖面」时必须带走的限定语。

### 4.2 sysctl：哪些可读写、哪些只读、哪些在 `SECURE_KERNEL` 下直接消失

`xnu/kern_cs.c:139-158`（逐字，节选全 10 条）：

> ```c
> SYSCTL_INT(_vm, OID_AUTO, cs_force_kill, CTLFLAG_RW | CTLFLAG_LOCKED, &cs_force_kill, 0, "");
> SYSCTL_INT(_vm, OID_AUTO, cs_force_hard, CTLFLAG_RW | CTLFLAG_LOCKED, &cs_force_hard, 0, "");
> SYSCTL_INT(_vm, OID_AUTO, cs_debug, CTLFLAG_RW | CTLFLAG_LOCKED, &cs_debug, 0, "");
> SYSCTL_INT(_vm, OID_AUTO, cs_debug_fail_on_unsigned_code, CTLFLAG_RW | CTLFLAG_LOCKED,
>     &cs_debug_fail_on_unsigned_code, 0, "");
> SYSCTL_UINT(_vm, OID_AUTO, cs_debug_unsigned_exec_failures, CTLFLAG_RD | CTLFLAG_LOCKED,
>     &cs_debug_unsigned_exec_failures, 0, "");
> SYSCTL_UINT(_vm, OID_AUTO, cs_debug_unsigned_mmap_failures, CTLFLAG_RD | CTLFLAG_LOCKED,
>     &cs_debug_unsigned_mmap_failures, 0, "");
> 
> SYSCTL_INT(_vm, OID_AUTO, cs_all_vnodes, CTLFLAG_RW | CTLFLAG_LOCKED, &cs_all_vnodes, 0, "");
> 
> #if !SECURE_KERNEL
> SYSCTL_INT(_vm, OID_AUTO, cs_system_enforcement, CTLFLAG_RD | CTLFLAG_LOCKED, &cs_system_enforcement_enable, 0, "");
> SYSCTL_INT(_vm, OID_AUTO, cs_process_enforcement, CTLFLAG_RD | CTLFLAG_LOCKED, &cs_process_enforcement_enable, 0, "");
> SYSCTL_INT(_vm, OID_AUTO, cs_enforcement_panic, CTLFLAG_RW | CTLFLAG_LOCKED, &cs_enforcement_panic, 0, "");
> 
> #if !CONFIG_ENFORCE_LIBRARY_VALIDATION
> SYSCTL_INT(_vm, OID_AUTO, cs_library_validation, CTLFLAG_RD | CTLFLAG_LOCKED, &cs_library_val_enable, 0, "");
> #endif
> #endif /* !SECURE_KERNEL */
> ```

口径（这是本节对报告最有价值的一条）：`vm.cs_system_enforcement` / `vm.cs_process_enforcement` / `vm.cs_enforcement_panic` / `vm.cs_library_validation` 四条 sysctl **整体包在 `#if !SECURE_KERNEL` 里**。也就是说在 `SECURE_KERNEL` 打开的构建里，代码签名强制状态**根本不可经 sysctl 读回**——取证时读不到该 OID 是构建属性，不是运行时异常。另 `cs_library_validation` 还额外要求 `!CONFIG_ENFORCE_LIBRARY_VALIDATION`。

底层变量的可改性登记：`xnu/kern_cs.c:85-92`

> ```c
> int cs_force_kill = 0;
> int cs_force_hard = 0;
> int cs_debug = 0;
> // If set, AMFI will error out early on unsigned code, before evaluation the normal policy.
> int cs_debug_fail_on_unsigned_code = 0;
> // If the previous mode is enabled, we count the resulting failures here.
> unsigned int cs_debug_unsigned_exec_failures = 0;
> unsigned int cs_debug_unsigned_mmap_failures = 0;
> ```

这四个 `CTLFLAG_RW` 变量（`cs_force_kill / cs_force_hard / cs_debug / cs_debug_fail_on_unsigned_code`，加上 `cs_all_vnodes`）是**普通 `int`，无 READ_ONLY 标注**，即运行期可由具备权限者改写；两个 `cs_debug_unsigned_*_failures` 是纯计数器（`CTLFLAG_RD`）。§1.3 的 NX 拒绝打印即受 `cs_debug` 门控（`xnu/vm_fault.c:2814-2816`）。

### 4.3 监视器侧：`security.codesigning.{monitor,config}` 只读

`bsd/kern/kern_codesigning.c:110-116`（逐字）：

> ```c
> static SECURITY_READ_ONLY_LATE(bool) cs_config_set = false;
> static SECURITY_READ_ONLY_LATE(code_signing_monitor_type_t) cs_monitor = CS_MONITOR_TYPE_NONE;
> static SECURITY_READ_ONLY_LATE(code_signing_config_t) cs_config = 0;
> static uint32_t security_boot_mode_complete = 0;
> 
> SYSCTL_UINT(_security_codesigning, OID_AUTO, monitor, CTLFLAG_RD, &cs_monitor, 0, "code signing monitor type");
> SYSCTL_UINT(_security_codesigning, OID_AUTO, config, CTLFLAG_RD, &cs_config, 0, "code signing configuration");
```

节点声明 `bsd/kern/kern_codesigning.c:108`：`SYSCTL_NODE(_security, OID_AUTO, codesigning, CTLFLAG_RD, 0, "XNU Code Signing");`

口径：默认值是 `CS_MONITOR_TYPE_NONE`；「监视器类型」与「配置位」两个量都是 `SECURITY_READ_ONLY_LATE`，**启动后不可改**，且 sysctl 侧只读。这是取证时最接近「本内核到底走哪条问责层」的**可读回**出口（在允许读的系统上）。TXM 侧另有 `debug.txm.metrics.*` 一组 `CTLFLAG_RD` sysctl（`xnu/txm.c:1882`、`:1886-1887`、`:1905`、`:1913`，含 `SYSCTL_NODE(_txm_metrics_memory, OID_AUTO, name, CTLFLAG_RD, ...)`），全部只读。

### 4.4 `read-only-late` 语义的三处自述注释

`bsd/sys/code_signing.h:269-279`（逐字）：

> ```c
> /*
>  * Query the current state of research mode on the system. This call never traps into
>  * the monitor environment as the state is queried at boot and saved in read-only-late
>  * memory.
>  *
>  * This state can only ever be enabled on platforms which support the trusted execution
>  * monitor environment. The state requires research fusing and the use of a security
>  * research device.
>  */
> bool
> research_mode_state(void);
```

`bsd/sys/code_signing.h:281-284`（extended research mode，同一措辞）逐字首两行：

> ```c
> /*
>  * Query the current state of extended research mode on the system. This call never traps
>  * into the monitor environment as the state is queried at boot and saved in read-only-late
>  * memory.
> ```

`bsd/sys/code_signing.h:626-629`（逐字）已在 §3.5 引用，其结论句是：

> ```c
>  * is given to it. In either case, the signature will be read-only for the kernel.
> ```

### 4.5 开发者模式与调试类开关

`bsd/sys/code_signing_internal.h:68-69`（逐字）：

> ```c
> void CSM_PREFIX(toggle_developer_mode)(
> 	bool state);
> ```

存储点：`bsd/kern/code_signing/ppl.c:89` `SECURITY_READ_ONLY_LATE(bool*) developer_mode_enabled = &ppl_developer_mode_storage;`；`bsd/kern/code_signing/xnu.c:73` 同型（`&developer_mode_storage`）；`bsd/kern/code_signing/txm.c:562` `SECURITY_READ_ONLY_LATE(bool*) developer_mode_enabled = NULL;`——即 TXM 构建里初值为 NULL，由 `txm.c` 后续从监视器取回。`bsd/sys/code_signing.h:266-267` 提供 `developer_mode_state(void)` 查询接口。

一个 boot-arg 型开关（仅 DEBUG/DEVELOPMENT 生效）：`xnu/vm_fault.c:155` `static bool vmtc_panic_instead = false;`，`xnu/vm_fault.c:359`

> ```c
> 	(void)PE_parse_boot_argn("text_corruption_panic", &vmtc_panic_instead, sizeof(vmtc_panic_instead));
> ```

其消费点在 `xnu/vm_fault.c:8897-8904`（整块 `#if DEBUG || DEVELOPMENT` 内），见 §5。

## §5 失败出口与日志格式串

状态：已填充

登记原则：只登记**逐字字符串 + 文件行号 + 所在门控**。不得据此推断公开文档所述行为。

### 5.1 VM 缺页侧

| # | `文件:行号` | 逐字串 | 门控（实测） |
|---|---|---|---|
| 1 | `xnu/vm_fault.c:2815`（上游 `osfmk/vm/vm_fault.c`） | `"page marked to be NX, not letting it be mapped EXEC\n"` | `if (cs_debug)`（`:2814`）；`cs_debug` 是 `kern_cs.c:87` 的普通 `int`，经 `vm.cs_debug` 可写 |
| 2 | `xnu/vm_fault.c:2807` | `KDBG_TRIAGE_VM_FAILED_IMMUTABLE_PAGE_WRITE`（`ktriage_record(...)` 事件名，非字符串） | `cs_enforcement_enabled && map_is_switched && map_is_switch_protected && ... (prot & VM_PROT_WRITE)`，返回 `KERN_CODESIGN_ERROR`（`:2808`） |
| 3 | `xnu/vm_fault.c:2817` | `KDBG_TRIAGE_VM_FAILED_NX_PAGE_EXEC_MAPPING` | 同 #1 的判定链，返回 `KERN_CODESIGN_ERROR`（`:2818`） |
| 4 | `xnu/vm_fault.c:8903` | `panic("Text page corruption detected: vm_page_t 0x%llx", ...)` | `#if DEBUG \|\| DEVELOPMENT`（`:8897`）且 `vmtc_panic_instead`（`:8902`），即需 boot-arg `text_corruption_panic` |
| 5 | `xnu/vm_fault.c:2917` | `object->code_signed ? "yes" : "no",` | 位于 cs 违规诊断打印的参数表 |
| 6 | `xnu/vm_fault.c:2674-2677` | `assertf("fault_page_size 0x%llx fault_phys_offset 0x%llx\n", ...)` | `vm_fault_cs_need_validation()` 内页大小/偏移一致性断言 |

### 5.2 AMFI / `kern_cs.c` 侧（`CODE SIGNING:` 前缀族）

| # | `文件:行号` | 逐字串（首行） |
|---|---|---|
| 7 | `xnu/kern_cs.c:208` | `"CODE SIGNING: cs_allow_invalid() "` |
| 8 | `xnu/kern_cs.c:215` | `"CODE SIGNING: cs_allow_invalid() "` |
| 9 | `xnu/kern_cs.c:231` | `"CODE SIGNING: cs_allow_invalid() not allowed by pmap: pid %d\n"` |
| 10 | `xnu/kern_cs.c:258` | `"CODE SIGNING: cs_invalid_page(0x%llx): p=%d[%s]\n"` |
| 11 | `xnu/kern_cs.c:300` | `"CODE SIGNING: cs_invalid_page(0x%llx): "` |
| 12 | `xnu/kern_cs.c:517` | `"Invalidated flags, old %x new %x\n"` |
| 13 | `xnu/kern_cs.c:793` | `panic("CoreEntitlements: missing AMFI bridge\n")` |
| 14 | `xnu/kern_cs.c:802` | `panic("CoreEntitlements: This API is no longer supported\n")` |
| 15 | `xnu/kern_cs.c:956` | `"clearing platform binary on proc/task: pid = %d\n"` |

口径：#10/#11 所在函数是 `cs_invalid_page()`，其 `CS_KILL`/`CS_HARD` 处置与 `threadsignal(...)` 行为**已由 `_facts_page_exec.md:556` 登记**（`kern_cs.c:248-312`），本节不重述结论，只补充：`kern_cs.c:2640` 处另有 `PANIC_ON_CS_KILLED_DEFAULT` 常量参与初始化，且 `xnu/vm_fault.c:2645-2646` 有两个计数器 `unsigned long cs_enter_tainted_rejected = 0;` / `cs_enter_tainted_accepted = 0;`。

### 5.3 `apple_protect` 侧（解密后校验的现场打印）

`xnu/vm_apple_protect.c:553-562`（逐字，含格式串全文）：

> ```c
> 				if (apple_protect_pager_data_request_debug) {
> 					printf("apple_protect_data_request"
> 					    "(%p,0x%llx+0x%llx+0x%04llx): "
> 					    "out of crypto range "
> 					    "[0x%llx:0x%llx]: "
> 					    "COPY [0x%016llx 0x%016llx] "
> 					    "code_signed=%d "
> 					    "cs_validated=%d "
> 					    "cs_tainted=%d "
> 					    "cs_nx=%d\n",
> ```

口径：`cs_validated / cs_tainted / cs_nx` 三个词**在本树里唯一成组出现的地方就是这个 debug 格式串**（另有 `:598`-`:620` 的第二份同型串），因此它们是 §1.2 三个页级位桶的命名对应物，不是独立的日志子系统。整块受 `apple_protect_pager_data_request_debug` 门控。

### 5.4 TXM / PPL 侧 panic 与 printf

`bsd/kern/code_signing/txm.c`（逐字，含行号）：

| `文件:行号` | 逐字 |
|---|---|
| `xnu/txm.c:133` | `panic("unable to acquire a thread stack for TXM");` |
| `xnu/txm.c:313` | `panic("invalid number of arguments to TXM: selector: %u \| %u",` |
| `xnu/txm.c:352` | `panic("received excessive return words from TXM: selector: %u \| %llu",` |
| `xnu/txm.c:384` | `panic("received out-of-memory error when adding a free page to TXM");` |
| `xnu/txm.c:420` | `panic("received fatal error for a selector from TXM: selector: %u \| 0x%0llX",` |
| `xnu/txm.c:442` | `panic("attempted to transfer non-page-aligned memory to TXM: %p", (void*)addr);` |
| `xnu/txm.c:466` | `panic("attempted to reclaim non-page-aligned memory from TXM: %p", (void*)addr);` |
| `xnu/txm.c:637` | `printf("libTXM_KernelVersion: %u\n", libTrustedExecutionMonitor_KernelVersion);` |
| `xnu/txm.c:638` | `printf("libTXM_Image4Version: %u\n", libTrustedExecutionMonitor_Image4Version);` |
| `xnu/txm.c:687` | `panic("unexpected failure for TXM secure channel: %d", ret);` |
| `xnu/txm.c:1077` | `printf("TXM [XNU]: unregistration of signature skipped as system is in shutdown\n");` |
| `xnu/txm.c:1641` | `panic("unsupported AppleImage4 interface");` |
| `xnu/txm.c:1714` | `printf("image4 object execution isn't supported by TXM\n");` |
| `xnu/txm.c:547` | `printf("%s\n", slot);`（把 TXM 日志页的槽位原样转印到内核控制台；槽位常量 `kTXMLogSlots` / `kTXMLogSlotSize` 见 `:531`、`:544`） |

`bsd/kern/code_signing/ppl.c`（PPL 侧对应物，逐字）：

| `文件:行号` | 逐字 |
|---|---|
| `xnu/ppl.c:76` | `printf("entered lockdown mode policy for the PPL");` |
| `xnu/ppl.c:148` | `panic("attempted to load a too-large profile: %lu bytes", profile_blob_size);` |
| `xnu/ppl.c:426` | `printf("disallowed non-debugger initiated debug mapping\n");` |
| `xnu/ppl.c:768` | `printf("explicit monitor-exports-get not required for the PPL\n");` |
| `xnu/ppl.c:783` | `printf("explicit release-type-set set not required for the PPL\n");` |
| `xnu/ppl.c:798` | `printf("explicit BNCH-shadow-set not required for the PPL\n");` |

### 5.5 SPTM/pmap 侧与信任缓存相关 panic

| `文件:行号` | 逐字 |
|---|---|
| `xnu/pmap.c:13044`（上游 `osfmk/arm/pmap/pmap.c`） | `panic("trust cache type not loadable from interface: %u", type);` |
| `xnu/pmap.c:13046` | `panic("attempted to load an unsupported trust cache type: %u", type);` |
| `xnu/pmap.c:13059` | `panic("attempted to load trust cache without entitlement: %u", type);` |
| `xnu/pmap.c:13728` | `panic("PMAP_CS: profile does not validate through CoreTrust: %d", ct_result);` |
| `xnu/pmap.c:12115` | `panic("%s: %#lx already locked down/executable (%#llx)",` |
| `xnu/pmap.c:6237` | `panic("pmap_enter_options(): attempt to add executable mapping to kernel_pmap");` |
| `xnu/arm_init_sptm.c:1016`（上游 `osfmk/arm64/sptm/arm_init_sptm.c`） | `panic("arm_init: Development SPTM / Release XNU is not a supported configuration.");` |
| `xnu/arm_init_sptm.c:920` | `panic("Mismatch of ARM_LARGE_MEMORY in SPTM (%#llx)/XNU (%#llx)", sptm_is_large_memory, xnu_is_large_memory);` |
| `xnu/arm_init_sptm.c:1443` | `panic("Unexpected /chosen/memory-map/TrustCache property size %u != %zu", trustCacheRangeSize, sizeof(DTMemoryMapRange));` |
| `xnu/arm_init_sptm.c:1450` | `panic("Unexpected location of TrustCache region: %#lx != %#lx",` |

口径：`xnu/arm_init_sptm.c:1016` 是本树里唯一一句**直接以 panic 表达「某种 SPTM/XNU 组合不受支持」**的串，其原文限定为 `Development SPTM / Release XNU`；引用时只可登记原文，不可推广为其它组合的支持性判断。

### 5.6 `cs_validate_page()` 现场串（`bsd/kern/ubc_subr.c`，全部受 `cs_debug` 分级门控）

| `文件:行号` | 逐字 | 门控 |
|---|---|---|
| `xnu/ubc_subr.c:5857` | `cs_validate_page_no_hash++;` | 「无哈希可校验」计数 |
| `xnu/ubc_subr.c:5859-5861` | `printf("CODE SIGNING: cs_validate_page: " "mobj %p off 0x%llx: no hash to validate !?\n", pager, page_offset);` | `if (cs_debug > 1)`（`:5858`） |
| `xnu/ubc_subr.c:5908-5911` | `printf("CODE SIGNING: cs_validate_page: " "mobj %p off 0x%llx size 0x%lx: " "actual [0x%x 0x%x 0x%x 0x%x 0x%x] != " "expected [0x%x 0x%x 0x%x 0x%x 0x%x]\n",` | `if (cs_debug)`（`:5907`），且前置条件是 `bcmp(...) != 0`（`:5906`） |
| `xnu/ubc_subr.c:5918` | `cs_validate_page_bad_hash++;` | 同上分支内 |
| `xnu/ubc_subr.c:5922-5925` | `printf("CODE SIGNING: cs_validate_page: " "mobj %p off 0x%llx size 0x%lx: " "SHA1 OK\n", pager, page_offset, size);` | `if (cs_debug > 10)`（`:5921`） |

口径：`"SHA1 OK"` 是本树里这一处**唯一**以 SHA1 命名哈希算法的成功日志串（逐字位于 `xnu/ubc_subr.c:5924`）；其上下文变量是 `hashtype->cs_size`（`:5906`），即算法与长度由 blob 的 `hashtype` 决定。**不得**据该串断言本树逐页校验只用 SHA-1。三条串分级不同（`>1`、非 0、`>10`），故「控制台有没有输出」取决于 `vm.cs_debug` 的具体整数值，而非开关布尔值。

---

## §6 零命中与反例登记

状态：已填充

**统一检索命令**（在 `本机 Temp 下的 xnu/xnu-main` 下执行，include glob 四元、分母 **4160**）：

```
INC="--include=*.c --include=*.h --include=*.cpp --include=*.m"
grep -r $INC '<pattern>' . | wc -l        # 命中行数
grep -rl $INC '<pattern>' . | wc -l       # 命中文件数
```

| 检索式 | 命中行数 / 文件数 | 结论 |
|---|---|---|
| `vm_page_validation` | **0 / 0** | 不存在该函数名/符号；不得用于命名 §1.5 的函数族 |
| `PMAP_OPTIONS_NOREGISTER` | **0 / 0** | 不存在该 pmap_enter 选项 |
| `vm_page_validate_cs_disposed` | **0 / 0** | 不存在该函数 |
| `object_lockment` | **0 / 0** | 不存在 |
| `pmapprov` | **0 / 0** | 不存在 |
| `PMAP_COMPACT` | **0 / 0** | 不存在 |
| `PPL_SIGNER_TYPE`（子串） | 3 / 1（全部在 `osfmk/kern/cs_blobs.h`） | **假阳性**：真实符号是 `CS_SUPPL_SIGNER_TYPE_UNKNOWN = 0`（`:153`）、`CS_SUPPL_SIGNER_TYPE_TRUSTCACHE = 7`（`:154`）、`CS_SUPPL_SIGNER_TYPE_LOCAL = 8`（`:155`）——`SUPPL` 尾部含 `PPL` |
| `-w PPL_SIGNER_TYPE`（词边界） | **0 / 0** | 复核后确认：本树无 `PPL_SIGNER_TYPE` 这一独立标识符 |
| `Kernel Integrity Protection` | **0 / 0** | 英文全称不出现在源码；树内对应物是宏 `KERNEL_INTEGRITY_KTRR` / `KERNEL_INTEGRITY_CTRR` / `KERNEL_INTEGRITY_PV_CTRR`（见 `xnu/kern_csr.c:46`、`xnu/sysctl.c:447`、`libkern/c++/OSKext.cpp:1225`、`libkern/c++/OSSymbol.cpp:456`） |
| `Page Protection Level` | **0 / 0** | 英文全称不出现；树内对应物是 `CONFIG_PMAP_PPL` / `PMAP_CS_PPL_MONITOR` 与注释里的 "Page Protection Layer" |
| `Configurable Text Read-only Region` | **0 / 0** | 英文全称不出现；对应宏名 `KERNEL_INTEGRITY_CTRR` |
| `KTRR`（子串，大小写敏感） | 83 / 24 | 全部是 `KERNEL_INTEGRITY_KTRR` 一类复合标识符（抽样见 `bsd/dev/arm64/sysctl.c:447`、`bsd/kern/kern_csr.c:46`、`EXTERNAL_HEADERS/img4/nonce.h:31,33`） |
| `-iw KTRR`（词边界、忽略大小写） | **26** | 只有 26 处是独立词形，其余 57 处是子串；报告若引用「KTRR 出现 83 次」必须改为「子串命中 83，词边界命中 26，且 0 处为英文全称」 |
| `CTRR`（子串）/ `-iw CTRR` | 160 / 61 | 同上口径：子串远多于词形，不可混用 |

**须复核的既有数值**：`_notes97_probe.md` N5 记录的 `apple_protect` 248/9、`pmap_cs` 263/14、`CS_ENFORCEMENT` 23/9、`cs_validate_page` 10/3、`KERN_CODESIGN_ERROR` 9/5、`csm_code_signing` 3/3 均为主代理实测，本简报沿用未复算；本节新增的 12 项 0 命中为本次实测。

**关于 `cs_validate_page`（已在本简报内闭环）**：既有 N5 记录它命中 10 处 / 3 文件；本次实测确认这 3 个文件是 `bsd/kern/ubc_subr.c`、`osfmk/vm/vm_fault.c`、`osfmk/vm/vm_protos.h`，与 §1.6 的登记一致。因此 `cs_validate_page` 与 `vm_page_validate_cs*` **不是同一层**：前者是哈希比对实现体，后者是 VM 侧调度族；报告引用时不可互换。

**本节另一条反例（自我校正）**：本简报 §1.5 初稿曾把 `xnu/vm_fault.c:8860-8905` 称作「`vm_page_validate_*` 家族的 disposed 分支」，实测其归属函数是 `revalidate_text_page()`（定义 `xnu/vm_fault.c:8825-8826`），与 §1.5 的调度族**不同层**。该更正已在 §1.5 正文落实，此处保留勘误记录以免读者按初稿去查。

---

## §7 与前篇简报的冲突或需校正之处

状态：已填充

### 7.1 无实质冲突

对照 `_facts_page_exec.md` §5.2 / §5.4 与 `_facts_kaslr_xnu.md` §0.3，本简报**未发现事实性冲突**。逐条核对：

| 既有记录 | 本次实测 | 结论 |
|---|---|---|
| `vm_fault.c:2838-2871` → `KERN_CODESIGN_ERROR`（`_facts_page_exec.md:556`） | `xnu/vm_fault.c:2808`、`:2818` 两处 `return KERN_CODESIGN_ERROR;`，位于 `2838` 之前 | **补充而非矛盾**：本树在该区间内有 3 处早退出口（2808 不可变页写、2818 NX 页 EXEC、以及 `_facts_page_exec.md` 已登记的 2838-2871 违规分支）。报告引用行号区间时应把 2808/2818 也算作同一函数的出口 |
| `kern_cs.c:248-312` 的 `CS_KILL` / `CS_HARD` 处置（`_facts_page_exec.md:556`） | 本次仅登记该区间内的格式串（§5.2 #10 `:258`、#11 `:300`），未复算处置逻辑 | 无冲突；本简报不重述其结论 |
| `SPTM_ENABLED_SOCS_MacOSX = t8112 t6020 t8122 t6030 t6031 t8132 t6041` | `makedefs/MakeInc.def:34` 逐字一致 | 一致；本简报补充其**唯一性**（`grep SPTM_ENABLED_SOCS` 全树仅 2 处命中：`:34` 与 `:1115`），故无 iPhoneOS 等价物这一说法在源码层面成立 |
| iPhoneOS `ARM_EXTRAS_BASE = config_pmap_ppl`，**不得**读作「零售 iPhone 用 PPL」 | `config/MASTER.arm64.iPhoneOS:19` 逐字为**注释行**（`#  ARM_EXTRAS_BASE = [ config_pmap_ppl ]`），且 iPhoneOS 文件**不提供** MacOSX 那样的 `#if MASTER_CONFIG_ENABLE_SPTM` 切换 | 一致，并加强：该行是清单注释；真正的 SPTM 判定发生在 `MakeInc.def:1104/1110/1115`，依赖树外输入（§3.2） |
| `SECURE_KERNEL` 是信息泄露总闸 | `config/MASTER:93-94` 注释自述 `# secure_kernel - secure kernel from user programs`；iPhoneOS 的 `BSD_RELEASE` 行含 `secure_kernel`（`config/MASTER.arm64.iPhoneOS:28`） | 一致；本简报另在 §4.2 补充一条**新的**后果：`vm.cs_system_enforcement` 等 4 条 sysctl 整体包在 `#if !SECURE_KERNEL` 内 |

### 7.2 需要在报告落笔时校正的三个易错点

1. **`pmap_cs.c` 不可作为「实现文件」引用**。两份同名文件（`osfmk/arm64/sptm/pmap/` 与 `osfmk/arm/pmap/`）各 33 行、零函数，注释自述为占位符；850 行的实现面在 `osfmk/vm/pmap_cs.h`。详见 §2.2。
2. **`PPL_SIGNER_TYPE` 是子串假阳性**。词边界检索 0 命中；真实符号是 `osfmk/kern/cs_blobs.h:153-155` 的 `CS_SUPPL_SIGNER_TYPE_{UNKNOWN,TRUSTCACHE,LOCAL}`。详见 §6。
3. **`KTRR` / `CTRR` 在本树里不是独立标识符**。子串命中 83/160，词边界命中 26/61，差额全部来自复合宏名 `KERNEL_INTEGRITY_KTRR` / `KERNEL_INTEGRITY_CTRR` / `KERNEL_INTEGRITY_PV_CTRR`（抽样：`osfmk/arm/pmap/pmap.h:244-257`、`osfmk/arm64/sptm/pmap/pmap.h:201-214`、`osfmk/arm64/amcc_rorgn.h:38-47`、`osfmk/mach/arm/vm_param.h:253-256`、`osfmk/arm/cpu_internal.h:68-73`、`bsd/kern/kern_csr.c:46-48`）。英文全称 `Kernel Integrity Protection` / `Page Protection Level` / `Configurable Text Read-only Region` 三者均 **0 / 0**。

### 7.3 骨架遗留节的处置说明（避免读者按旧目录找内容）

本简报覆盖了原骨架的 15 节目录，压缩为任务指定的 8 节，映射关系：旧 §1→新 §1（并新增 §1.6）；旧 §2→新 §1.5；旧 §3→新 §2.1；旧 §4→新 §2.2–2.5；旧 §5→新 §4.1–4.2；旧 §6「固化与静态区」→**并入新 §7.2 第 3 点与新 §6 表格**（因本树无独立 KTRR/CTRR 标识符，独立成节会制造假事实）；旧 §7→新 §3；旧 §8→新 §4；旧 §9→新 §5；旧 §10/§11→新 §6；旧 §12「结构体与枚举逐字附录」→新 §2.3（只登记 `pmap_cs_trust_t` 一项，其余枚举本次未取证，见 §8 N8）；旧 §13→新 §7；旧 §14→新 §8。旧 §0.5「无版本锚点声明」并入新 §0.3。

---

## §8 缺口清单

状态：已填充

逐条列出**本树查不到、且不得由二手材料补**的内容：

- **N1｜`board_config.h` 不在开源树。** `xnu/pmap_cs.h:81` 的注释点名「`XNU_MONITOR` 和 `PMAP_CS` 都在 `board_config.h` 里为相同目标定义」，但该文件本身不在本快照内（实测 `find . -name 'board_config.h'` 无结果）。因此「某个具体 SoC 目标上 `PMAP_CS_PPL_MONITOR` 是否成立」不可从本树判定，§3.3 的三态派发只能给出**条件式**结论。
- **N2｜无版本锚点。** 无 `.git`、无 `version.h`、无 `config/ABMS`、无 `Config*.mk`（沿用 `_facts_kaslr_xnu.md` §0.3）。本简报全部行号仅对本快照有效。
- **N3｜SPTM 启用所需的 EDM 数据库不在树内。** `makedefs/MakeInc.def:1104` 的 SQL 依赖 `$(EMBEDDED_DEVICE_MAP)` 与 `$(EDM_DBPATH)`，两者均由构建环境注入；`EnableSPTMTXM` 的按机型取值不可得。同时 `ifneq ($(EMBEDDED_DEVICE_MAP),)`（`:1101`）意味着未设该变量时整个 `SPTM_ENABLED` 查询块**根本不执行**。
- **N4｜TXM 与 SPTM 的实现体是外部链接库。** `MakeInc.def:1120-1121` 只登记了归档名 `-lTrustedExecutionMonitor_Kernel`、`-lsptm_xnu`；监视器内部如何校验一页、`txm_call` 选择子语义、以及 §2.3 信任等级如何被赋予，本树无源码。`bsd/kern/code_signing/txm.c` 只是 XNU 侧的调用壳。
- **N5｜PPL 侧代码签名的真实落点未定位。** §2.2 已确认两份 `pmap_cs.c` 是占位符；`osfmk/arm64/sptm/pmap/pmap_ppl_interface.c` 与 `pmap.c` 内含 PPL 交叉调用，但本简报未逐函数取证「PPL 校验一页」的具体实现体，故不得写「PPL 的校验逻辑在 X 文件」。
- **N6｜`vm_fault.c:2838-2871` 区间的违规细分未复算。** 本次只登记该函数内 3 个早退出口与 §5.1 的串，未逐行采集 2838 之后与 `_facts_page_exec.md` 对应的分支细节；如需扩展须另开一次取证。
- **N7｜`CONFIG_SUPPLEMENTAL_SIGNATURES` / Rosetta 式补充签名的构建侧归属未查。** §1.6 只登记了 `xnu/ubc_subr.c:6025` 的条件编译本身，未登记该 option 在 `config/MASTER*` 中的落点与注释。补充签名与 §6 的 `CS_SUPPL_SIGNER_TYPE_*` 是否同一条链，本简报未证。
- **N8｜§2.3 之外的枚举未取。** `pmap_cs.h` 850 行内的 `struct pmap_cs_code_directory`（`:331`）、`struct pmap_cs_lookup_results`（`:478`）、以及 `PMAP_CS_INCLUDE_CODE_SIGNING` 分支（`:187` 起）内的一系列类型，本简报只登记了 `pmap_cs_trust_t` 与两条阈值宏；「逐字附录」若需完整枚举须另次取证。
- **N9｜KIP / SCIP 的寄存器与锁定代码未检索。** 本次只做了英文全称的 0 命中登记（§6）并抽样了 `KERNEL_INTEGRITY_*` 宏，未系统检索 `amcc_rorgn`（`osfmk/arm64/amcc_rorgn.h:38-47`）的函数体与协处理器保护路径。报告若要写「KIP 在源码里的锁定动作」，本简报**不足以支撑**，须补查。
- **N10｜`csm_*` 接口族的实现分布未登记。** `bsd/sys/code_signing.h:531-841` 整块包在 `#if CODE_SIGNING_MONITOR` 内（实测 `:531` 与 `:841`），`csm_enabled()` 声明于 `:541`；但本简报未逐一登记 `csm_` 前缀函数的实现文件，也未采集 §1.3 用到的计数器 `vm_cs_defer_to_csm` / `vm_cs_defer_to_csm_not` / `vm_cs_revalidates` 的可观测出口（是否存在对应 sysctl 未查）。

---

## 附录 A：本次实测的检索台账

全部命令在 `本机 Temp 下的 xnu/xnu-main` 下执行；include glob 为 `--include=*.c --include=*.h --include=*.cpp --include=*.m`；语料分母 **4160**（本次用 `find . \( -name '*.c' -o -name '*.h' -o -name '*.cpp' -o -name '*.m' \) | wc -l` 复算，与既有简报一致）。

| 实测项 | 命令要点 | 结果 |
|---|---|---|
| `osfmk/arm64/sptm/pmap/pmap_cs.c` 行数 | `wc -l` | 33 |
| `osfmk/arm/pmap/pmap_cs.c` 行数 | `wc -l` | 33 |
| `osfmk/vm/pmap_cs.h` 行数 | `wc -l` | 850 |
| `osfmk/vm/vm_apple_protect.c` 存在性 | `ls -la` | 存在，46035 字节 |
| `SPTM_ENABLED_SOCS` | `grep -rn` | 2 处（`makedefs/MakeInc.def:34`、`:1115`） |
| `MASTER_CONFIG_ENABLE_SPTM` | `grep -rn config/ SETUP/ makedefs/` | 8 处：`SETUP/config/doconf:78,158`；`config/MASTER.arm64.{iPhoneOS:84,90, MacOSX:17,82,88, WatchOS:70,76}`（注：iPhoneOS 的 `:84/:90` 与 KernelTag 相关，不涉及 `ARM_EXTRAS_BASE`） |
| `vm_page_validate` 族 | `grep -rn --include=*.c --include=*.h` | 21 处，全部落在 `vm_apple_protect.c`、`vm_dyld_pager.c`、`vm_fault.c`、`vm_page_internal.h`、`vm_resident.c`、`vm_shared_region_pager.c` |
| `vmp_canonical` | `grep -rn osfmk/vm/` | 9 处：`vm_page.h:274,456`；`vm_resident.c:937,949,957,2894,3853,3903,5148` |
| `code_signed`（`vm_fault.c` 单文件） | `grep -n` | 17 处 |
| `SECURITY_READ_ONLY_LATE` CS 相关 | `grep -rn` kern_cs.c/code_signing/ | 16 处（`kern_cs.c:131,132,134`；`ppl.c:89`；`txm.c:480,481,482,556-564`；`xnu.c:73`）+ `kern_codesigning.c:110-112` + `kern_csr.c:50` |
| `KERNEL_INTEGRITY_*` 抽样 | `grep -rn --include=*.h` | `osfmk/arm/cpu_internal.h:68,73`、`osfmk/arm/pmap/pmap.h:244,250,257`、`osfmk/arm64/amcc_rorgn.h:38,47`、`osfmk/arm64/sptm/pmap/pmap.h:201,207,214`、`osfmk/mach/arm/vm_param.h:253,256` |

**本次未做**（避免虚报覆盖面）：未检索 `lockdown` / `hardware lock` 字面（N9）；未检索 `csm_` 前缀全族实现文件（N10）；未复算 `_notes97_probe.md` N5 的 6 组既有数值（沿用）。

---

## §9 末次实测的补充与自我勘误（必读，优先级高于前文对应段落）

状态：已填充

### 9.1 勘误：`board_config.h` **在开源树内**，前文 N1 的说法作废

实测命令 `find . -name 'board_config.h'` 返回 **2 个文件**：`pexpert/pexpert/arm/board_config.h`（12 行）与 `pexpert/pexpert/arm64/board_config.h`（**348 行**）。因此 §8 N1 中「该文件不在本快照内，`find` 无结果」一句**是本简报的错误**，特此更正；`_notes97_probe.md` N5/N6 之外、以及既有简报里「`board_config.h` 属 Apple 内部目标文件、不在开源 config 中全量可得」的表述，就 **arm64 而言不成立**（该文件确实随开源树发布）。

**§3.6（替代 N1 的正确事实）：`pexpert/pexpert/arm64/board_config.h` 里的逐 SoC `PMAP_CS` / `XNU_MONITOR` 定义**

文件头注释自述组织顺序（`xnu/board_config.h:34-41`，据源码注释，逐字）：

> ```c
> /*
>  * Per-SoC configuration.  General order is:
>  *
>  * CPU type
>  * CPU configuration
>  * CPU feature disables / workarounds
>  * CPU topology
>  * Other platform configuration (e.g. DARTs, PPL)
>  *
>  * This should answer the question: "what's unique about this SoC?"
> ```

实测的两种形态（逐字行号）：

**形态 A —— 无条件定义（无 `#ifndef CONFIG_SPTM` 包裹）**：

> ```c
> 52:#ifdef ARM64_BOARD_CONFIG_T6000
> 62:#define PMAP_CS                        1
> 63:#define PMAP_CS_ENABLE                 1
> 64:#define XNU_MONITOR                    1 /* Secure pmap runtime */
> ```
> 同样形态出现在 `ARM64_BOARD_CONFIG_T8101`（`:110`，定义 `:117`/`:119`）与 `ARM64_BOARD_CONFIG_T8103`（`:124`，定义 `:131`/`:133`）。

**形态 B —— 被 `#ifndef CONFIG_SPTM` 包裹**（即 SPTM 打开时**不**定义 `PMAP_CS`/`XNU_MONITOR`）：

> ```c
> 86:#ifndef CONFIG_SPTM
> 87:#define PMAP_CS                        1
> 89:#define XNU_MONITOR                    1 /* Secure pmap runtime */
> 90:#endif /* CONFIG_SPTM */
> ```

采用形态 B 的 SoC 段起始行实测：`T6020`（`:77`，块 `:86-90`）、`T8112`（`:139`，块 `:146-150`）、`T8122_T8130`（`:158`，块 `:166-170`）、`T8132`（`:180`，块 `:187-191`）、`T6030`（`:210`，块 `:219-223`）、`T6031`（`:232`，块 `:242-246`）、`T6041`（`:254`，块 `:261-265`）。`ARM64_BOARD_CONFIG_VMAPPLE` 段（`:281-295`）**不含** `PMAP_CS`/`XNU_MONITOR` 定义。

口径（三条，均只描述 Apple 自己的配置项与其字面后果）：

1. 形态 B 的 7 个 SoC 段与 `makedefs/MakeInc.def:34` 的 `SPTM_ENABLED_SOCS_MacOSX = t8112 t6020 t8122 t6030 t6031 t8132 t6041` **逐一对应**（`T8122_T8130` 覆盖列表里的 `t8122`）。这是本树里「哪些 SoC 的 `PMAP_CS` 会被 SPTM 关掉」最直接的源码交叉印证。
2. 形态 A 的三个 SoC（`T6000`、`T8101`、`T8103`）**不在** `SPTM_ENABLED_SOCS_MacOSX` 列表里，且其 `PMAP_CS` 定义不受 `CONFIG_SPTM` 约束——据本节字面，这 3 个目标在开启 SPTM 时仍会定义 `PMAP_CS`/`XNU_MONITOR`。报告若要写这一现象，必须标注「仅为本快照的 `board_config.h` 字面，未与任何文档核对」，且**不得**推断其在零售系统中的意义。
3. `PMAP_CS_PPL_MONITOR`（§2.4 / §3.3）的成立条件「有 `XNU_MONITOR` 且明确无 `CONFIG_SPTM`」，与形态 B 的 `#ifndef CONFIG_SPTM` 包裹是同一语义的两次表达；`xnu/pmap_cs.h:81` 的注释点名本文件，本次已实际打开验证该注释所指文件在树内可见。

**同时须注意 §9.1 与 §3.2 的关系不改变原结论**：`board_config.h` 在场只解决了「`PMAP_CS` 按 SoC 怎样定义」，**没有**解决「零售构建是否为某 SoC 打开 `CONFIG_SPTM`」——后者仍取决于 `MakeInc.def:1104` 的树外 EDM 数据库或 `CURRENT_KERNEL_CONFIG`。所以「覆盖面收窄的组合」仍只能以条件式表述。

### 9.2 附录 A 的两处数值勘误（复算后的正确值）

| 行 | 原记 | **正确值（复算）** |
|---|---|---|
| `MASTER_CONFIG_ENABLE_SPTM` 命中数 | 8 处 | **9 处**：`config/MASTER.arm64.iPhoneOS:84,90`、`config/MASTER.arm64.MacOSX:17,82,88`、`config/MASTER.arm64.WatchOS:70,76`、`SETUP/config/doconf:78,158`（`makedefs/` 下 0 处） |
| CS 相关 `SECURITY_READ_ONLY_LATE` 命中数 | 16 处 | **23 处**（检索范围为 `bsd/kern/kern_cs.c bsd/kern/code_signing/ bsd/kern/kern_codesigning.c bsd/kern/kern_csr.c`；含 `kern_csr.c:50` 的 `csr_config_t`） |
| `vm_page_validate` 族分布 | 6 文件 / 21 处 | 一致（`vm_apple_protect.c`、`vm_dyld_pager.c`、`vm_fault.c`、`vm_page_internal.h`、`vm_resident.c`、`vm_shared_region_pager.c`；21 处） |
| `code_signing.h` 的 CSM 块边界 | `:531` 与 `:841` | 一致：`:531` = `#if CODE_SIGNING_MONITOR`、`:541` = `csm_enabled(void);`、`:841` = `#endif /* CODE_SIGNING_MONITOR */` |

### 9.3 §8 缺口清单的增量修订

- **N1 → 作废**，由 §9.1 取代（正确事实：`board_config.h` 在树内，arm64 版 348 行，逐 SoC 定义 `PMAP_CS`/`PMAP_CS_ENABLE`/`XNU_MONITOR`）。
- **N1′（新增，替代原缺口的真实未证项）**：`pexpert/pexpert/arm/board_config.h` 仅 12 行（32 位 arm 路径），本简报未取证其内容；且 `ARM64_BOARD_CONFIG_*` 这些宏**由谁在构建时置位**（对应的构建变量与机型映射）不在本树，故「某具体机型 ⇒ 某具体 `ARM64_BOARD_CONFIG_*`」这一跳仍需树外信息。
- **N8 → 部分完成**：`struct pmap_cs_code_directory`（`xnu/pmap_cs.h:331`，内含 `pmap_cs_code_directory_next` 于 `:474`）与 `struct pmap_cs_lookup_results`（`:478`）已确认存在，但其字段级逐字未采集。
- 其余 N2–N7、N9、N10 维持原样（本次未取证，不得由二手补）。

