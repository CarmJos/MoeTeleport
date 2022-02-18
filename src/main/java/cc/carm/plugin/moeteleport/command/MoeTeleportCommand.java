package cc.carm.plugin.moeteleport.command;

import cc.carm.plugin.moeteleport.manager.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MoeTeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
                             @NotNull String s, @NotNull String[] args) {
        if (args[0].equalsIgnoreCase("reload")) {
            commandSender.sendMessage("Reloading config...");
            ConfigManager.reload();
            commandSender.sendMessage("Config reloaded.");
            return true;
        }
        return false;
    }

}
