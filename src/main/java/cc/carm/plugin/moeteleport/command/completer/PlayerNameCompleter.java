package cc.carm.plugin.moeteleport.command.completer;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerNameCompleter implements TabCompleter {

	List<Integer> indexes;

	public PlayerNameCompleter() {
		this(1);
	}

	public PlayerNameCompleter(Integer index) {
		this(new Integer[]{index});
	}

	public PlayerNameCompleter(Integer[] indexes) {
		this.indexes = Arrays.asList(indexes);
	}

	@Nullable
	@Override
	public java.util.List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		if (args.length >= 1 && indexes.contains(args.length)) {
			return Bukkit.getOnlinePlayers().stream()
					.map(HumanEntity::getName)
					.filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1]))
					.limit(10).collect(Collectors.toList());
		} else {
			return ImmutableList.of();
		}
	}


}
