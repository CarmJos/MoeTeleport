package cc.carm.plugin.moeteleport.util;

import cc.carm.lib.githubreleases4j.GithubReleases4J;
import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.MoeTeleport;

public class UpdateChecker {

    public static void checkUpdate() {
        Main.getInstance().getScheduler().runAsync(() -> {

            Integer behindVersions = GithubReleases4J.getVersionBehind(
                    "CarmJos", "MoeTeleport",
                    Main.getInstance().getDescription().getVersion()
            );

            String downloadURL = GithubReleases4J.getReleasesURL("CarmJos", "MoeTeleport");

            MoeTeleport.outputInfo();
            if (behindVersions == null) {
                Main.serve("检查更新失败，请您定期查看插件是否更新，避免安全问题。");
                Main.serve("下载地址 " + downloadURL);
            } else if (behindVersions == 0) {
                Main.info("检查完成，当前已是最新版本。");
            } else if (behindVersions > 0) {
                Main.info("发现新版本! 目前已落后 " + behindVersions + " 个版本。");
                Main.info("最新版下载地址 " + downloadURL);
            } else {
                Main.serve("检查更新失败! 当前版本未知，请您使用原生版本以避免安全问题。");
                Main.serve("最新版下载地址 " + downloadURL);
            }

        });
    }


}
