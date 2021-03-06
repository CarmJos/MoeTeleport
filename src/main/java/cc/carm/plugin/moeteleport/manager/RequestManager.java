package cc.carm.plugin.moeteleport.manager;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.configuration.PluginConfig;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.model.TeleportRequest;
import cc.carm.plugin.moeteleport.model.UserData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.UUID;

public class RequestManager {

    public BukkitRunnable checkRunnable;

    public RequestManager(Main main) {
        this.checkRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                checkRequests();
            }
        };
        this.checkRunnable.runTaskTimerAsynchronously(main, 100L, 20L);
    }

    public void shutdown() {
        if (!this.checkRunnable.isCancelled()) {
            this.checkRunnable.cancel();
        }
    }

    public void checkRequests() {
        MoeTeleport.getUserManager().getUserDataMap().values()
                .forEach(data -> data.getReceivedRequests().entrySet().stream()
                        .filter(entry -> entry.getValue().isExpired())
                        .peek(entry -> {
                            Player sender = entry.getValue().getSender();
                            Player receiver = entry.getValue().getReceiver();
                            PluginMessages.Requests.SENT_TIMEOUT.send(sender, receiver.getName());
                            PluginMessages.Requests.RECEIVED_TIMEOUT.send(receiver, sender.getName());
                        })
                        .peek(entry -> MoeTeleport.getUserManager()
                                .getData(entry.getValue().getSender()).getSentRequests()
                                .remove(entry.getKey()))
                        .forEach(entry -> data.getReceivedRequests().remove(entry.getKey()))
                );
    }

    public void sendRequest(Player sender, Player receiver, TeleportRequest.RequestType type) {
        int expireTime = PluginConfig.EXPIRE_TIME.get();

        PluginMessages.Requests.SENT.send(sender, receiver.getName(), expireTime);

        switch (type) {
            case TPA: {
                PluginMessages.Requests.RECEIVED_TP_HERE.send(receiver, sender.getName(), expireTime);
                break;
            }
            case TPA_HERE: {
                PluginMessages.Requests.RECEIVED_TP_TO.send(receiver, sender.getName(), expireTime);
                break;
            }
        }

        TeleportRequest request = new TeleportRequest(sender, receiver, type);
        MoeTeleport.getUserManager().getData(receiver).getReceivedRequests().put(sender.getUniqueId(), request);
        MoeTeleport.getUserManager().getData(sender).getSentRequests().add(receiver.getUniqueId());

    }

    public void acceptRequest(TeleportRequest request) {
        PluginMessages.Requests.WAS_ACCEPTED.send(request.getSender(), request.getReceiver().getName());
        PluginMessages.Requests.ACCEPTED.send(request.getReceiver(), request.getSender().getName());
        TeleportManager.teleport(request.getTeleportPlayer(), request.getTeleportLocation(), true);
        removeRequests(request);
    }

    public void denyRequest(TeleportRequest request) {
        PluginMessages.Requests.WAS_DENIED.send(request.getSender(), request.getReceiver().getName());
        PluginMessages.Requests.DENIED.send(request.getReceiver(), request.getSender().getName());
        removeRequests(request);
    }

    public void removeRequests(TeleportRequest request) {
        MoeTeleport.getUserManager().getData(request.getSender())
                .getSentRequests()
                .remove(request.getReceiver().getUniqueId());
        MoeTeleport.getUserManager().getData(request.getReceiver())
                .getReceivedRequests()
                .remove(request.getSender().getUniqueId());
    }

    public void cancelAllRequests(Player player) {
        UUID playerUUID = player.getUniqueId();
        UserData data = MoeTeleport.getUserManager().getData(player);
        data.getReceivedRequests().keySet().stream()
                .peek(senderUUID -> PluginMessages.Requests.OFFLINE.send(Bukkit.getPlayer(senderUUID), player.getName()))
                .map(senderUUID -> MoeTeleport.getUserManager().getData(senderUUID))
                .filter(Objects::nonNull).map(UserData::getSentRequests)
                .forEach(receivers -> receivers.remove(playerUUID));

        data.getSentRequests().stream()
                .peek(receiverUUID -> PluginMessages.Requests.OFFLINE.send(Bukkit.getPlayer(receiverUUID), player.getName()))
                .map(receiverUUID -> MoeTeleport.getUserManager().getData(receiverUUID))
                .filter(Objects::nonNull).map(UserData::getReceivedRequests)
                .forEach(senders -> senders.remove(playerUUID));

        data.getSentRequests().clear();
        data.getReceivedRequests().clear();
    }

}
