package cc.carm.plugin.moeteleport.command;

import cc.carm.lib.easyplugin.command.CommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends CommandHandler {

    public MainCommand(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public Void noArgs(CommandSender sender) {
        return null;
    }

    @Override
    public Void noPermission(CommandSender sender) {
        return null;
    }


}
