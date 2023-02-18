package cc.carm.plugin.moeteleport.command.sub.warp;

import cc.carm.plugin.moeteleport.Main;
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

public class WarpRenameCommand extends WarpSubCommand {

    public WarpRenameCommand(@NotNull WarpCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (!(sender instanceof Player)) {
            PluginMessages.NOT_PLAYER.send(sender);
            return null;
        }

        if (args.length < 2) return getParent().noArgs(sender);

        String oldName = args[0];
        String newName = args[1];

        if (oldName.equals(newName)) return getParent().noArgs(sender);
        if (newName.length() > 16) {   // 超过地标的名字长度限定
            PluginMessages.WARP.NAME_TOO_LONG.send(sender);
            return null;
        }

        Player player = (Player) sender;


        WarpInfo info = getWarp(oldName);
        if (info == null) {
            PluginMessages.WARP.NOT_FOUND.send(player);
            return null;
        }

        WarpInfo newInfo = getWarp(oldName);
        if (newInfo != null) {
            PluginMessages.WARP.ALREADY_EXITS.send(player);
            return null;
        }

        PluginMessages.WARP.RENAMED.send(player, newName, info.getLocation());
        Main.getInstance().getScheduler().runAsync(() -> {
            getManager().setWarp(newName, player.getUniqueId(), info.getLocation());
            getManager().delWarp(oldName);
        });

        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return listWarpNames(sender, args[args.length - 1], true);
        } else return Collections.emptyList();
    }

}
