package cc.carm.plugin.moeteleport.command.teleport;

import cc.carm.lib.easyplugin.command.SimpleCompleter;
import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.command.parent.TeleportCommands;
import cc.carm.plugin.moeteleport.command.sub.TeleportSubCommand;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.model.TeleportRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class TeleportRequestCommand extends TeleportSubCommand {

    private final @NotNull TeleportRequest.RequestType type;

    public TeleportRequestCommand(@NotNull TeleportCommands parent,
                                  @NotNull TeleportRequest.RequestType type,
                                  String name, String... aliases) {
        super(parent, name, aliases);
        this.type = type;
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (!(sender instanceof Player)) {
            PluginMessages.NOT_PLAYER.send(sender);
            return null;
        }

        if (args.length < 1) return getParent().noArgs(sender);

        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            PluginMessages.NOT_ONLINE.send(player);
            return null;
        }

        if (player == target) {
            // fix #5 - 玩家给自己发送传送请求
            PluginMessages.REQUESTS.SELF.send(player);
            return null;
        }

        TeleportRequest request = MoeTeleport.getUserManager().getData(target).getReceivedRequests().get(player.getUniqueId());
        if (request != null) {
            PluginMessages.REQUESTS.DUPLICATE.send(sender, target.getName(), request.getRemainSeconds());
            return null;
        }

        MoeTeleport.getRequestManager().sendRequest(player, target, type);
        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return SimpleCompleter.objects(
                    args[args.length - 1], 15,
                    Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .filter(s -> !s.equalsIgnoreCase(sender.getName()))
            );
        } else return Collections.emptyList();
    }

}
