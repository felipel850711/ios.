import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * iOS DarkSword 利用套件防御性分析。
 *
 * 数据来源：Google TAG 2026-03-18 披露；与本报告系列《iOS 漏洞利用套件
 * Coruna 与 DarkSword 安全分析报告》及《iOS 漏洞利用套件与链条组合安全
 * 分析报告》保持一致。本类仅用于教学与防御参考，不包含任何漏洞利用代码、
 * PoC 或扫描逻辑。
 *
 * 用法：直接运行 main 方法，输出 DarkSword 套件的 CVE 目录、攻击链阶段、
 * 受影响版本、目标地区、防御建议等防御性摘要。
 */
public final class DarkSwordAnalysis {

    /** DarkSword 利用链的三个阶段（Google TAG 命名）。 */
    public enum Stage {
        GHOSTBLADE("初始访问 / 解析器 RCE",
                "通过 WebKit 或 ImageIO 解析器漏洞实现零点击代码执行。"),
        KNIFE("沙箱逃逸 / 内核提权",
                "利用 XNU 内核漏洞突破 WebContent 沙箱并获取内核权限。"),
        SABER("植入驻留 / 数据外传",
                "部署持久化载荷并建立与 C2 的通信通道。");

        private final String role;
        private final String description;

        Stage(String role, String description) {
            this.role = role;
            this.description = description;
        }

        public String role() { return role; }
        public String description() { return description; }
    }

    /** 单条 CVE 记录（不可变）。 */
    public static final class CveEntry {
        public final String id;
        public final String component;
        public final String defectClass;
        public final String affectedVersions;
        public final String fixedVersion;
        public final LocalDate disclosureDate;
        public final Stage stage;
        public final String notes;

        public CveEntry(String id, String component, String defectClass,
                        String affectedVersions, String fixedVersion,
                        LocalDate disclosureDate, Stage stage, String notes) {
            this.id = id;
            this.component = component;
            this.defectClass = defectClass;
            this.affectedVersions = affectedVersions;
            this.fixedVersion = fixedVersion;
            this.disclosureDate = disclosureDate;
            this.stage = stage;
            this.notes = notes;
        }
    }

    /** DarkSword 套件的 6 个零日漏洞（Google TAG 2026-03-18 披露）。 */
    public static final List<CveEntry> DARKSWORD_CVES;

    static {
        List<CveEntry> list = new ArrayList<>();
        list.add(new CveEntry(
                "CVE-2025-14174", "WebKit",
                "类型混淆 / UAF",
                "iOS 18.4 之前", "iOS 18.4",
                LocalDate.of(2025, 3, 5), Stage.GHOSTBLADE,
                "在野利用；解析器阶段初始访问入口之一。"));
        list.add(new CveEntry(
                "CVE-2025-31277", "ImageIO",
                "堆缓冲区溢出",
                "iOS 18.4 之前", "iOS 18.4",
                LocalDate.of(2025, 4, 2), Stage.GHOSTBLADE,
                "在野利用；与 CVE-2025-14174 协同承担初始 RCE。"));
        list.add(new CveEntry(
                "CVE-2025-43510", "XNU (IOKit)",
                "越界读写 (OOB)",
                "iOS 18.5 之前", "iOS 18.5",
                LocalDate.of(2025, 5, 12), Stage.KNIFE,
                "在野利用；沙箱逃逸阶段关键原语。"));
        list.add(new CveEntry(
                "CVE-2025-43520", "XNU (内核)",
                "整数溢出导致越界写",
                "iOS 18.5 之前", "iOS 18.5",
                LocalDate.of(2025, 5, 12), Stage.KNIFE,
                "在野利用；与 CVE-2025-43510 配合完成内核提权。"));
        list.add(new CveEntry(
                "CVE-2025-43529", "XNU (内核)",
                "UAF / 任意读写",
                "iOS 18.5 之前", "iOS 18.5",
                LocalDate.of(2025, 5, 12), Stage.KNIFE,
                "在野利用；提供内核态任意读写能力。"));
        list.add(new CveEntry(
                "CVE-2026-20700", "AGXAccelerator (GPU)",
                "MMIO 绕过 PPL",
                "iOS 18.7 之前", "iOS 18.7",
                LocalDate.of(2026, 3, 18), Stage.SABER,
                "在野利用；绕过 PPL 实现持久化载荷注入。"));
        DARKSWORD_CVES = Collections.unmodifiableList(list);
    }

    /** 套件元信息。 */
    public static final String KIT_NAME = "DarkSword";
    public static final LocalDate FIRST_SEEN = LocalDate.of(2025, 11, 1);
    public static final LocalDate PUBLIC_DISCLOSURE = LocalDate.of(2026, 3, 18);
    public static final String DISCLOSER = "Google TAG";
    public static final List<String> TARGET_REGIONS =
            Collections.unmodifiableList(Arrays.asList(
                    "沙特阿拉伯", "土耳其", "马来西亚", "乌克兰"));
    public static final List<String> TARGET_PROFILES =
            Collections.unmodifiableList(Arrays.asList(
                    "记者", "人权活动家", "政府官员", "企业高管"));

    /** 按阶段分组 CVE。 */
    public static Map<Stage, List<CveEntry>> groupByStage() {
        Map<Stage, List<CveEntry>> grouped = new LinkedHashMap<>();
        for (Stage s : Stage.values()) {
            grouped.put(s, new ArrayList<>());
        }
        for (CveEntry cve : DARKSWORD_CVES) {
            grouped.get(cve.stage).add(cve);
        }
        return grouped;
    }

    /** 判断给定 iOS 版本是否受 DarkSword 影响（简化判断）。 */
    public static boolean isAffected(String iosVersion) {
        if (iosVersion == null) return false;
        String v = iosVersion.trim();
        if (v.startsWith("18.")) {
            try {
                int minor = Integer.parseInt(v.substring(3).split("[^0-9]")[0]);
                return minor <= 7;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /** 打印 DarkSword 防御性分析摘要。 */
    public static void printReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=============================================================\n");
        sb.append("  iOS DarkSword 利用套件防御性分析\n");
        sb.append("  Defensive Analysis of iOS DarkSword Exploit Kit\n");
        sb.append("=============================================================\n\n");

        sb.append("[套件概况]\n");
        sb.append("  名称：").append(KIT_NAME).append('\n');
        sb.append("  首次观测：").append(FIRST_SEEN).append('\n');
        sb.append("  公开披露：").append(PUBLIC_DISCLOSURE)
          .append("（").append(DISCLOSER).append("）\n");
        sb.append("  零日数量：").append(DARKSWORD_CVES.size()).append('\n');
        sb.append("  受影响版本：iOS 18.4 — iOS 18.7\n");
        sb.append("  目标地区：").append(String.join("、", TARGET_REGIONS)).append('\n');
        sb.append("  目标画像：").append(String.join("、", TARGET_PROFILES)).append('\n');
        sb.append("  商品化特征：链条已被多家供应商采用，作为商业间谍软件载荷投放。\n\n");

        sb.append("[攻击链阶段]\n");
        Map<Stage, List<CveEntry>> grouped = groupByStage();
        for (Map.Entry<Stage, List<CveEntry>> e : grouped.entrySet()) {
            Stage s = e.getKey();
            sb.append("  ").append(s.name()).append(" — ").append(s.role()).append('\n');
            sb.append("    ").append(s.description()).append('\n');
            for (CveEntry cve : e.getValue()) {
                sb.append("      - ").append(cve.id)
                  .append(" (").append(cve.component).append(", ")
                  .append(cve.defectClass).append(")")
                  .append(" → ").append(cve.fixedVersion).append('\n');
            }
        }
        sb.append('\n');

        sb.append("[CVE 完整目录]\n");
        sb.append(String.format("  %-16s %-18s %-22s %-14s %-14s%n",
                "CVE", "组件", "缺陷类型", "受影响", "修复版本"));
        sb.append("  ---------------------------------------------------------------------------\n");
        for (CveEntry cve : DARKSWORD_CVES) {
            sb.append(String.format("  %-16s %-18s %-22s %-14s %-14s%n",
                    cve.id, cve.component, cve.defectClass,
                    cve.affectedVersions, cve.fixedVersion));
        }
        sb.append('\n');

        sb.append("[防御建议]\n");
        sb.append("  1. 立即升级：所有 iOS 设备升级到最新稳定版（≥ iOS 18.7 后续补丁）。\n");
        sb.append("  2. 启用锁定模式（Lockdown Mode）：高风险用户（记者、活动家、\n");
        sb.append("     政府官员）应在 iOS 16+ 启用锁定模式，显著缩小攻击面。\n");
        sb.append("  3. 监控异常行为：关注设备异常重启、未知描述文件、非预期\n");
        sb.append("     网络连接（尤其是 DarkSword C2 已知 IOC）。\n");
        sb.append("  4. 应用威胁情报：将 Google TAG / Citizen Lab / Kaspersky\n");
        sb.append("     发布的 DarkSword IOC 接入 SIEM 与移动设备管理（MDM）。\n");
        sb.append("  5. 供应链加固：仅从 App Store 安装应用；企业部署需验证\n");
        sb.append("     签名证书链，警惕企业证书滥用与 MDM 劫持。\n");
        sb.append("  6. 纵深防御：不要依赖单一缓解机制；PAC/W^X/JOP 防护\n");
        sb.append("     已多次被绕过，需结合沙箱、entitlement、BlastDoor 等\n");
        sb.append("     多层检查。\n\n");

        sb.append("[参考资料]\n");
        sb.append("  - Google TAG, DarkSword advisory, 2026-03-18\n");
        sb.append("  - Apple Security Releases, https://support.apple.com/en-us/HT201222\n");
        sb.append("  - 本系列报告 2：《iOS 漏洞利用套件 Coruna 与 DarkSword 安全分析报告》\n");
        sb.append("  - 本系列报告 10：《iOS 漏洞利用套件与链条组合安全分析报告》\n");
        sb.append("=============================================================\n");

        System.out.print(sb.toString());
    }

    public static void main(String[] args) {
        printReport();

        System.out.println();
        System.out.println("[版本影响自检示例]");
        String[] samples = {"17.7", "18.3", "18.4", "18.6", "18.7", "18.8", "19.0"};
        for (String v : samples) {
            System.out.printf("  iOS %-6s → %s%n", v,
                    isAffected(v) ? "受影响" : "不受影响 / 已修复");
        }
    }

    private DarkSwordAnalysis() {}
}
