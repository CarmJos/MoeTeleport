package cc.carm.plugin.moeteleport.command.warp;

import cc.carm.plugin.moeteleport.command.parent.WarpCommands;
import cc.carm.plugin.moeteleport.command.sub.WarpSubCommand;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class WarpDeleteCommand extends WarpSubCommand {

    public WarpDeleteCommand(@NotNull WarpCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (args.length < 1) return getParent().noArgs(sender);

        String warpName = args[0];

        WarpInfo info = getWarp(warpName);
        if (info == null) {
            PluginMessages.WARP.NOT_FOUND.send(sender, warpName);
            return null;
        }

        if (sender instanceof Player && !sender.isOp()
                && !sender.hasPermission("MoeTeleport.admin")) {
            Player player = (Player) sender;
            if (info.getOwner() == null || !info.getOwner().equals(player.getUniqueId())) {
                PluginMessages.WARP.NOT_OWNER.send(sender);
                return null;
            }
        }

        getManager().delWarpAsync(warpName);
        PluginMessages.WARP.REMOVED.send(sender, warpName, info.getLocation().toFlatString());

        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return listWarpNames(sender, args[args.length - 1], true);
        } else return Collections.emptyList();
    }

}
