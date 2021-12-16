package cc.carm.plugin.moeteleport.manager;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.model.UserData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class RequestManager {

	public void sendTpaRequest(Player sender, Player receiver) {

	}

	public void sendTpaHereRequest(Player sender, Player receiver) {

	}

	public void cancelAllRequests(Player player) {
		UUID playerUUID = player.getUniqueId();
		UserData data = Main.getUserManager().getData(player);
		data.getSentRequests().keySet().stream()
				.peek(receiverUUID -> PluginMessages.Request.OFFLINE.sendWithPlaceholders(
						Bukkit.getPlayer(receiverUUID),
						new String[]{"%(player)"}, new Object[]{player.getName()}
				)).map(receiverUUID -> Main.getUserManager().getData(receiverUUID))
				.filter(Objects::nonNull).map(UserData::getReceivedRequests)
				.forEach(senders -> senders.remove(playerUUID));

		data.getReceivedRequests().stream()
				.peek(senderUUID -> PluginMessages.Request.OFFLINE.sendWithPlaceholders(
						Bukkit.getPlayer(senderUUID),
						new String[]{"%(player)"}, new Object[]{player.getName()}
				)).map(senderUUID -> Main.getUserManager().getData(senderUUID))
				.filter(Objects::nonNull).map(UserData::getSentRequests)
				.forEach(receivers -> receivers.remove(playerUUID));

		data.getSentRequests().clear();
		data.getReceivedRequests().clear();
	}

	public Set<Player> getRequestedPlayers(Player player) {
		UserData data = Main.getUserManager().getData(player);
		HashSet<UUID> relatedUUIDs = new HashSet<>();
		relatedUUIDs.addAll(data.getReceivedRequests());
		relatedUUIDs.addAll(data.getSentRequests().keySet());

		return relatedUUIDs.stream()
				.map(Bukkit::getPlayer)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	/**
	 * 取消某玩家所有相关的传送请求
	 *
	 * @param player 玩家
	 */
	public void clearAllRequests(Player player) {
		UUID playerUUID = player.getUniqueId();
		UserData data = Main.getUserManager().getData(player);
		data.getSentRequests().keySet().stream()
				.map(receiver -> Main.getUserManager().getData(receiver))
				.filter(Objects::nonNull).map(UserData::getReceivedRequests)
				.forEach(senders -> senders.remove(playerUUID));
		data.getReceivedRequests().stream()
				.map(sender -> Main.getUserManager().getData(sender))
				.filter(Objects::nonNull).map(UserData::getSentRequests)
				.forEach(receivers -> receivers.remove(playerUUID));
		data.getSentRequests().clear();
		data.getReceivedRequests().clear();
	}

}
