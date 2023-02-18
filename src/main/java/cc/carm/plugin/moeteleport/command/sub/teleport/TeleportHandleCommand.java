package cc.carm.plugin.moeteleport.command.sub.teleport;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.command.sub.TeleportCommands;
import cc.carm.plugin.moeteleport.command.base.TeleportSubCommand;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.storage.UserData;
import cc.carm.plugin.moeteleport.teleport.TeleportRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TeleportHandleCommand extends TeleportSubCommand {

    protected final boolean accept;

    public TeleportHandleCommand(@NotNull TeleportCommands parent, boolean accept, String name, String... aliases) {
        super(parent, name, aliases);
        this.accept = accept;
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (!(sender instanceof Player)) {
            PluginMessages.NOT_PLAYER.send(sender);
            return null;
        }

        Player player = (Player) sender;
        UserData data = MoeTeleport.getUserManager().getData(player);

        Map<UUID, TeleportRequest> receivedRequests = getReceivedRequests(player);
        if (receivedRequests.isEmpty()) {
            PluginMessages.REQUESTS.EMPTY_REQUESTS.send(player);
            return null;
        }

        String targetName = args.length > 0 ? args[0] : null;
        data.setEnableAutoSelect(false);

        if (targetName != null) {
            Player target = Bukkit.getPlayer(targetName);
            TeleportRequest request = target == null ? null : receivedRequests.get(target.getUniqueId());

            if (request == null) {
                PluginMessages.REQUESTS.NO_REQUEST_FROM.send(player, target == null ? targetName : target.getName());
            } else {
                handle(request); // 交给Manager处理
            }
        } else {
            if (receivedRequests.size() == 1 || data.isEnableAutoSelect()) {
                receivedRequests.values().stream()
                        .min(Comparator.comparingLong(TeleportRequest::getActiveMillis))
                        .ifPresent(this::handle);
            } else {
                PluginMessages.REQUESTS.MULTI.send(player,
                        receivedRequests.size(),
                        "moeteleport teleport " + (accept ? "tpAccept" : "tpDeny").toLowerCase()
                );
                data.setEnableAutoSelect(true);
            }
        }
        return null;
    }

    private void handle(TeleportRequest request) {
        if (accept) {
            MoeTeleport.getRequestManager().acceptRequest(request);
        } else {
            MoeTeleport.getRequestManager().denyRequest(request);
        }
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return listRequests(sender, args[args.length - 1]);
        } else return Collections.emptyList();
    }

}
