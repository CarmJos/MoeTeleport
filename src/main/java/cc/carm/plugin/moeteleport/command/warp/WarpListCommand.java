package cc.carm.plugin.moeteleport.command.warp;

import cc.carm.lib.easyplugin.command.SimpleCompleter;
import cc.carm.plugin.moeteleport.command.parent.WarpCommands;
import cc.carm.plugin.moeteleport.command.sub.WarpSubCommand;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class WarpListCommand extends WarpSubCommand {

    public WarpListCommand(@NotNull WarpCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        ArrayList<WarpInfo> warps = new ArrayList<>(listWarps().values());
        if (warps.isEmpty()) {
            PluginMessages.WARP.EMPTY.send(sender);
            return null;
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

        PluginMessages.WARP.LIST.HEADER.send(sender, currentPage, maxPage);
        for (int i = startIndex; i < endIndex; i++) {
            WarpInfo info = warps.get(i);
            String ownerName = info.getOwnerName();
            if (ownerName == null) {
                PluginMessages.WARP.LIST.OBJECT_NO_OWNER.send(sender, info.getName(), info.getLocation().toFlatString());
            } else {
                PluginMessages.WARP.LIST.OBJECT.send(sender, info.getName(), ownerName, info.getLocation().toFlatString());
            }
        }

        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            int maxPage = (int) Math.ceil(listWarps().size() / 10.0);
            return SimpleCompleter.objects(args[args.length - 1], IntStream.rangeClosed(1, maxPage).boxed());
        } else return Collections.emptyList();
    }

}
