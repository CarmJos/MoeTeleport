package cc.carm.plugin.moeteleport.manager;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.conf.PluginConfig;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.teleport.TeleportRequest;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RequestManager {

    protected final Map<UUID, TeleportRequest> requests = new ConcurrentHashMap<>();
    protected BukkitRunnable runnable;

    public RequestManager(Main main) {
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                tickRequests();
            }
        };
        this.runnable.runTaskTimerAsynchronously(main, 100L, 20L);
    }

    public void shutdown() {
        if (!this.runnable.isCancelled()) {
            this.runnable.cancel();
        }
    }

    @Unmodifiable
    public Map<UUID, TeleportRequest> getRequests() {
        return Collections.unmodifiableMap(requests);
    }

    public @Nullable TeleportRequest getRequest(UUID senderUUID) {
        return requests.get(senderUUID);
    }

    @Unmodifiable
    public @NotNull Map<UUID, TeleportRequest> getUserReceivedRequests(@NotNull UUID receiverUUID) {
        return requests.values().stream()
                .filter(request -> request.getReceiver().getUniqueId().equals(receiverUUID))
                .collect(Collectors.toMap(request -> request.getSender().getUniqueId(), request -> request));
    }

    public void tickRequests() {
        Iterator<Map.Entry<UUID, TeleportRequest>> requestIterator = requests.entrySet().iterator();
        while (requestIterator.hasNext()) {
            Map.Entry<UUID, TeleportRequest> entry = requestIterator.next();
            TeleportRequest request = entry.getValue();
            if (!request.isExpired()) continue;

            requestIterator.remove(); // 移除过期的请求

            // 发送提示
            PluginMessages.REQUESTS.SENT_TIMEOUT.send(request.getSender(), request.getReceiver().getName());
            PluginMessages.REQUESTS.RECEIVED_TIMEOUT.send(request.getReceiver(), request.getSender().getName());
        }
    }

    public void sendRequest(Player sender, Player receiver, TeleportRequest.Type type) {
        int expireTime = PluginConfig.REQUEST.EXPIRE_TIME.getNotNull();

        PluginConfig.REQUEST.SOUND.SENT.playTo(sender);
        PluginConfig.REQUEST.SOUND.RECEIVED.playTo(receiver);

        PluginMessages.REQUESTS.SENT.send(sender, receiver.getName(), expireTime);
        switch (type) {
            case TPA_TO: {
                PluginMessages.REQUESTS.RECEIVED_TP_HERE.send(receiver, sender.getName(), expireTime);
                break;
            }
            case TPA_HERE: {
                PluginMessages.REQUESTS.RECEIVED_TP_TO.send(receiver, sender.getName(), expireTime);
                break;
            }
        }

        requests.put(sender.getUniqueId(), new TeleportRequest(type, sender, receiver));
    }

    public void acceptRequest(TeleportRequest request) {
        PluginMessages.REQUESTS.WAS_ACCEPTED.send(request.getSender(), request.getReceiver().getName());
        PluginMessages.REQUESTS.ACCEPTED.send(request.getReceiver(), request.getSender().getName());

        removeRequests(request);
        MoeTeleport.getTeleportManager().queueTeleport(request.createQueue(
                Duration.of(PluginConfig.TELEPORTATION.WAIT_TIME.getNotNull(), ChronoUnit.SECONDS)
        ));
    }

    public void cancelRequest(TeleportRequest request) {
        PluginMessages.REQUESTS.WAS_ACCEPTED.send(request.getSender(), request.getReceiver().getName());
        PluginMessages.REQUESTS.ACCEPTED.send(request.getReceiver(), request.getSender().getName());
        PluginConfig.REQUEST.SOUND.CANCELLED.playTo(request.getSender());
        PluginConfig.REQUEST.SOUND.CANCELLED.playTo(request.getReceiver());
        removeRequests(request);
        MoeTeleport.getTeleportManager().queueTeleport(request);
    }

    public void denyRequest(TeleportRequest request) {
        PluginMessages.REQUESTS.WAS_DENIED.send(request.getSender(), request.getReceiver().getName());
        PluginMessages.REQUESTS.DENIED.send(request.getReceiver(), request.getSender().getName());
        removeRequests(request);
    }

    public void removeRequests(TeleportRequest request) {
        this.requests.remove(request.getSender().getUniqueId());
    }

    public void cancelAllRequests(Player player) {
        UUID playerUUID = player.getUniqueId();

        TeleportRequest sent = requests.remove(playerUUID);
        if (sent != null) {
            PluginMessages.REQUESTS.OFFLINE.send(sent.getReceiver(), player.getName());
        }

        for (TeleportRequest received : getUserReceivedRequests(playerUUID).values()) {
            PluginMessages.REQUESTS.OFFLINE.send(received.getSender(), player.getName());
            removeRequests(received);
        }
    }

}
