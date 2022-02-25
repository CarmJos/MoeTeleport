package cc.carm.plugin.moeteleport.command.warp;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WarpListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        ArrayList<WarpInfo> warps = new ArrayList<>(MoeTeleport.getWarpManager().listWarps().values());
        if (warps.isEmpty()) {
            PluginMessages.Warp.EMPTY.send(player);
            return true;
        }

        String pageString = args.length > 0 ? args[0] : null;
        int page = 1;
        if (pageString != null) {
            try {
                page = Integer.parseInt(pageString);
            } catch (Exception ignored) {
            }
        }

        int maxPage = (int) Math.ceil(warps.size() / 10.0);
        int currentPage = Math.min(page, maxPage);

        int startIndex = Math.max(0, (currentPage - 1) * 10);
        int endIndex = Math.min(warps.size(), startIndex + 9);

        PluginMessages.Warp.List.HEADER.send(player, currentPage, maxPage);
        for (int i = startIndex; i < endIndex; i++) {
            WarpInfo info = warps.get(i);
            String ownerName = info.getOwnerName();
            if (ownerName == null) {
                PluginMessages.Warp.List.OBJECT_NO_OWNER.send(player, info.getName(), info.getLocation().toFlatString());
            } else {
                PluginMessages.Warp.List.OBJECT.send(player, info.getName(), ownerName, info.getLocation().toFlatString());
            }
        }

        return true;
    }


}
