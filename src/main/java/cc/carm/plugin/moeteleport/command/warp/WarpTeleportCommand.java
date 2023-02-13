package cc.carm.plugin.moeteleport.command.warp;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.command.parent.WarpCommands;
import cc.carm.plugin.moeteleport.command.sub.WarpSubCommand;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.manager.TeleportManager;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class WarpTeleportCommand extends WarpSubCommand {

    public WarpTeleportCommand(@NotNull WarpCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            PluginMessages.NOT_PLAYER.send(sender);
            return null;
        }

        if (args.length < 1) return getParent().noArgs(sender);

        Player player = (Player) sender;

        WarpInfo info = getWarp(args[0]);
        if (info == null) {
            PluginMessages.WARP.NOT_FOUND.send(player, args[0]);
            return null;
        }

        MoeTeleport.getTeleportManager().queueTeleport(player, info.getLocation());
        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return listWarpNames(sender, args[args.length - 1], false);
        } else return Collections.emptyList();
    }

}
