package cc.carm.plugin.moeteleport.command.sub.warp;

import cc.carm.plugin.moeteleport.command.sub.WarpCommands;
import cc.carm.plugin.moeteleport.command.base.WarpSubCommand;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class WarpInfoCommand extends WarpSubCommand {

    public WarpInfoCommand(@NotNull WarpCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        String warpName = args[0];

        WarpInfo info = getWarp(warpName);
        if (info == null) {
            PluginMessages.WARP.NOT_FOUND.send(sender, warpName);
            return null;
        }

        String ownerName = info.getOwnerName();
        if (ownerName != null) {
            PluginMessages.WARP.INFO_FULL.send(sender, warpName, ownerName, info.getLocation().toFlatString());
        } else {
            PluginMessages.WARP.INFO_LOCATION.send(sender, warpName, info.getLocation().toFlatString());
        }

        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return listWarpNames(sender, args[args.length - 1], false);
        } else return Collections.emptyList();
    }

}
