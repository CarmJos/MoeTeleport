package cc.carm.plugin.moeteleport.command.teleport;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.command.parent.TeleportCommands;
import cc.carm.plugin.moeteleport.command.sub.TeleportSubCommand;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.model.TeleportRequest;
import cc.carm.plugin.moeteleport.storage.UserData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        if (data.getReceivedRequests().isEmpty()) {
            PluginMessages.REQUESTS.EMPTY_REQUESTS.send(player);
            return null;
        }

        String targetName = args.length > 0 ? args[0] : null;
        data.setEnableAutoSelect(false);

        if (targetName != null) {
            Player target = Bukkit.getPlayer(targetName);
            if (target == null || !data.getReceivedRequests().containsKey(target.getUniqueId())) {
                PluginMessages.REQUESTS.NO_REQUEST_FROM.send(player, target == null ? targetName : target.getName());
            } else {
                handle(data.getReceivedRequests().get(target.getUniqueId())); // 交给Manager处理
            }
        } else {
            if (data.getReceivedRequests().size() == 1 || data.isEnableAutoSelect()) {
                data.getReceivedRequests().values().stream()
                        .min(Comparator.comparingLong(TeleportRequest::getActiveTime))
                        .ifPresent(this::handle);
            } else {
                PluginMessages.REQUESTS.MULTI.send(player, data.getReceivedRequests().size(), (accept ? "tpaccept" : "tpdeny"));
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
