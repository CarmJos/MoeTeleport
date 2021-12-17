package cc.carm.plugin.moeteleport.manager;

import cc.carm.plugin.moeteleport.Main;
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
		Main.getUserManager().getUserDataMap().values()
				.forEach(data -> data.getReceivedRequests().entrySet().stream()
						.filter(entry -> entry.getValue().isExpired())
						.peek(entry -> PluginMessages.Request.SENT_TIMEOUT.sendWithPlaceholders(
								entry.getValue().getSender(), new String[]{"%(player)"},
								new Object[]{entry.getValue().getReceiver().getName()}))
						.peek(entry -> PluginMessages.Request.RECEIVED_TIMEOUT.sendWithPlaceholders(
								entry.getValue().getReceiver(), new String[]{"%(player)"},
								new Object[]{entry.getValue().getSender().getName()}))
						.forEach(entry -> data.getSentRequests().remove(entry.getKey()))
				);
	}

	public void sendRequest(Player sender, Player receiver, TeleportRequest.RequestType type) {
		int expireTime = PluginConfig.EXPIRE_TIME.get();

		PluginMessages.Request.SENT.sendWithPlaceholders(sender,
				new String[]{"%(player)", "%(expire)"},
				new Object[]{receiver, expireTime}
		);
		switch (type) {
			case TPA: {
				PluginMessages.TPA.sendWithPlaceholders(receiver,
						new String[]{"%(player)", "%(expire)"},
						new Object[]{sender.getName(), expireTime}
				);
				break;
			}
			case TPA_HERE: {
				PluginMessages.TPA_HERE.sendWithPlaceholders(receiver,
						new String[]{"%(player)", "%(expire)"},
						new Object[]{sender.getName(), expireTime}
				);
				break;
			}
		}

		TeleportRequest request = new TeleportRequest(sender, receiver, type);
		Main.getUserManager().getData(receiver).getReceivedRequests().put(sender.getUniqueId(), request);
		Main.getUserManager().getData(sender).getSentRequests().add(receiver.getUniqueId());

	}

	public void acceptRequest(TeleportRequest request) {
		PluginMessages.ACCEPTED.sendWithPlaceholders(request.getSender(),
				new String[]{"%(player)"},
				new Object[]{request.getReceiver().getName()}
		);
		PluginMessages.TP_ACCEPT.sendWithPlaceholders(request.getReceiver(),
				new String[]{"%(player)"},
				new Object[]{request.getSender().getName()}
		);
		TeleportManager.teleport(request.getTeleportPlayer(), request.getTeleportLocation());
		removeRequests(request);
	}

	public void denyRequest(TeleportRequest request) {
		PluginMessages.DENIED.sendWithPlaceholders(request.getSender(),
				new String[]{"%(player)"},
				new Object[]{request.getReceiver().getName()}
		);
		PluginMessages.TP_DENY.sendWithPlaceholders(request.getReceiver(),
				new String[]{"%(player)"},
				new Object[]{request.getSender().getName()}
		);
		removeRequests(request);
	}

	public void removeRequests(TeleportRequest request) {
		Main.getUserManager().getData(request.getSender())
				.getSentRequests()
				.remove(request.getReceiver().getUniqueId());
		Main.getUserManager().getData(request.getReceiver())
				.getReceivedRequests()
				.remove(request.getSender().getUniqueId());
	}

	public void cancelAllRequests(Player player) {
		UUID playerUUID = player.getUniqueId();
		UserData data = Main.getUserManager().getData(player);
		data.getReceivedRequests().keySet().stream()
				.peek(senderUUID -> PluginMessages.Request.OFFLINE.sendWithPlaceholders(
						Bukkit.getPlayer(senderUUID),
						new String[]{"%(player)"}, new Object[]{player.getName()}
				)).map(senderUUID -> Main.getUserManager().getData(senderUUID))
				.filter(Objects::nonNull).map(UserData::getSentRequests)
				.forEach(receivers -> receivers.remove(playerUUID));

		data.getSentRequests().stream()
				.peek(receiverUUID -> PluginMessages.Request.OFFLINE.sendWithPlaceholders(
						Bukkit.getPlayer(receiverUUID),
						new String[]{"%(player)"}, new Object[]{player.getName()}
				)).map(receiverUUID -> Main.getUserManager().getData(receiverUUID))
				.filter(Objects::nonNull).map(UserData::getReceivedRequests)
				.forEach(senders -> senders.remove(playerUUID));

		data.getSentRequests().clear();
		data.getReceivedRequests().clear();
	}

}
