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

public class TpAcceptCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		UserData data = Main.getUserManager().getData(player);
		if (data.getReceivedRequests().isEmpty()) {
			PluginMessages.Request.NOT_FOUND.sendWithPlaceholders(player);
			return true;
		}
		String targetName = args.length > 0 ? args[0] : null;
		if (targetName != null) {
			Player target = Bukkit.getPlayer(targetName);
			if (target == null || !data.getReceivedRequests().containsKey(target.getUniqueId())) {
				PluginMessages.Request.NOT_FOUND_PLAYER.sendWithPlaceholders(player,
						new String[]{"%(player)"},
						new Object[]{target == null ? targetName : target.getName()}
				);
			} else {
				TeleportRequest request = data.getReceivedRequests().get(target.getUniqueId());
				Main.getRequestManager().acceptRequest(request); // 交给Manager处理
			}
		} else {
			data.getReceivedRequests().values().stream()
					.min(Comparator.comparingLong(TeleportRequest::getActiveTime))
					.ifPresent(request -> Main.getRequestManager().acceptRequest(request));
		}
		return true;
	}

}
