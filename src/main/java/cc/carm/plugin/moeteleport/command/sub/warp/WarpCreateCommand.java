package cc.carm.plugin.moeteleport.command.sub.warp;

import cc.carm.lib.easyplugin.command.SimpleCompleter;
import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.command.sub.WarpCommands;
import cc.carm.plugin.moeteleport.command.base.WarpSubCommand;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class WarpCreateCommand extends WarpSubCommand {

    public WarpCreateCommand(@NotNull WarpCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            PluginMessages.NOT_PLAYER.send(sender);
            return null;
        }

        Player player = (Player) sender;
        String warpName = args.length >= 1 ? args[0] : player.getName();

        if (warpName.length() > 16) {   // 超过地标的名字长度限定
            PluginMessages.WARP.NAME_TOO_LONG.send(sender);
            return null;
        }

        WarpInfo info = getWarp(warpName);

        if (!player.isOp() && !player.hasPermission("MoeTeleport.admin")) {
            if (info != null && (info.getOwner() == null || !info.getOwner().equals(player.getUniqueId()))) {
                PluginMessages.WARP.NOT_OWNER.send(sender, warpName);
                return null;
            }

            int maxWarp = MoeTeleport.getUserManager().getMaxWarps(player);
            long currentUsed = MoeTeleport.getUserManager().countUserWarps(player.getUniqueId());
            if (currentUsed >= maxWarp && info == null) {
                PluginMessages.WARP.OVER_LIMIT.send(sender, maxWarp);
                return null;
            }
        }

        getManager().setWarpAsync(warpName, player.getUniqueId(), player.getLocation());
        if (info != null) {
            PluginMessages.WARP.OVERRIDE.send(sender, warpName, info.getLocation().toFlatString());
        } else {
            PluginMessages.WARP.SET.send(sender, warpName);
        }

        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return SimpleCompleter.text(args[args.length - 1], sender.getName());
        } else return Collections.emptyList();
    }

}
