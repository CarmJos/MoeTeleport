package cc.carm.plugin.moeteleport.command.completer;

import cc.carm.plugin.moeteleport.Main;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

public class HomeNameCompleter implements TabCompleter {

	@Nullable
	@Override
	public java.util.List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		if (!(sender instanceof Player)) return ImmutableList.of();
		if (args.length == 1) {
			return Main.getUserManager().getData((Player) sender).getHomeLocations().keySet().stream()
					.filter(s -> StringUtil.startsWithIgnoreCase(s, args[0]))
					.limit(10).collect(Collectors.toList());
		} else {
			return ImmutableList.of();
		}
	}

}