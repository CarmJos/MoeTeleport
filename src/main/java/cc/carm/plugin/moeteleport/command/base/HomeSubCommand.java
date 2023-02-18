package cc.carm.plugin.moeteleport.command.base;

import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.command.sub.HomeCommands;
import cc.carm.plugin.moeteleport.storage.UserData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class HomeSubCommand extends SubCommand<HomeCommands> {
    public HomeSubCommand(@NotNull HomeCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    public @NotNull UserData getData(Player player) {
        return MoeTeleport.getUserManager().getData(player);
    }

    public @Nullable UserData getData(UUID player) {
        return MoeTeleport.getUserManager().getData(player);
    }

    public @NotNull List<String> listHomes(CommandSender sender, String input) {
        if (!(sender instanceof Player)) return Collections.emptyList();

        return MoeTeleport.getUserManager().getData((Player) sender)
                .getHomeLocations().keySet().stream()
                .filter(s -> StringUtil.startsWithIgnoreCase(s, input))
                .limit(10).collect(Collectors.toList());
    }

}
