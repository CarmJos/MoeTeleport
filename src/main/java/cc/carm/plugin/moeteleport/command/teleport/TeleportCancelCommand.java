package cc.carm.plugin.moeteleport.command.teleport;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.command.parent.TeleportCommands;
import cc.carm.plugin.moeteleport.command.sub.TeleportSubCommand;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.teleport.TeleportRequest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class TeleportCancelCommand extends TeleportSubCommand {

    public TeleportCancelCommand(@NotNull TeleportCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (!(sender instanceof Player)) {
            PluginMessages.NOT_PLAYER.send(sender);
            return null;
        }

        Player player = (Player) sender;

        TeleportRequest request = MoeTeleport.getRequestManager().getRequest(player.getUniqueId());
        if (request == null) {
            PluginMessages.REQUESTS.EMPTY_REQUESTS.send(player);
            return null;
        }

        MoeTeleport.getRequestManager().cancelRequest(request);
        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return listRequests(sender, args[args.length - 1]);
        } else return Collections.emptyList();
    }

}
