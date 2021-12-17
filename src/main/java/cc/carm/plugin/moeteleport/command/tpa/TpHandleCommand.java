package cc.carm.plugin.moeteleport.command.tpa;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.model.TeleportRequest;
import cc.carm.plugin.moeteleport.model.UserData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class TpHandleCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
							 @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		UserData data = Main.getUserManager().getData(player);
		if (data.getReceivedRequests().isEmpty()) {
			PluginMessages.Request.NOT_FOUND.sendWithPlaceholders(player);
			return true;
		}
		String targetName = args.length > 0 ? args[0] : null;
		boolean accept = command.getName().equalsIgnoreCase("tpAccept");
		data.setEnableAutoSelect(false);
		if (targetName != null) {
			Player target = Bukkit.getPlayer(targetName);
			if (target == null || !data.getReceivedRequests().containsKey(target.getUniqueId())) {
				PluginMessages.Request.NOT_FOUND_PLAYER.sendWithPlaceholders(player,
						new String[]{"%(player)"},
						new Object[]{target == null ? targetName : target.getName()}
				);
			} else {
				handle(data.getReceivedRequests().get(target.getUniqueId()), accept); // 交给Manager处理
			}
		} else {
			if (data.getReceivedRequests().size() == 1 || data.isEnableAutoSelect()) {
				data.getReceivedRequests().values().stream()
						.min(Comparator.comparingLong(TeleportRequest::getActiveTime))
						.ifPresent(request -> handle(request, accept));
			} else {
				PluginMessages.Request.MULTI.sendWithPlaceholders(player,
						new String[]{"%(num)", "%(command)"},
						new Object[]{data.getReceivedRequests().size(), command.getName()}
				);
				data.setEnableAutoSelect(true);
			}
		}
		return true;
	}

	private void handle(TeleportRequest request, boolean accept) {
		if (accept) {
			Main.getRequestManager().acceptRequest(request);
		} else {
			Main.getRequestManager().denyRequest(request);
		}
	}

}
