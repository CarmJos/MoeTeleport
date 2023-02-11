package cc.carm.plugin.moeteleport.command;

import cc.carm.lib.easyplugin.command.CommandHandler;
import cc.carm.plugin.moeteleport.command.parent.*;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MainCommands extends CommandHandler {

    public MainCommands(@NotNull JavaPlugin plugin) {
        super(plugin);

        registerHandler(new TeleportCommands(plugin, this, "teleport", "tp"));
        registerHandler(new WarpCommands(plugin, this, "warp", "warps"));
        registerHandler(new HomeCommands(plugin, this, "home", "homes"));

        registerSubCommand(new BackCommand(this, "back"));
        registerSubCommand(new ReloadCommand(this, "reload"));
    }

    @Override
    public Void noArgs(CommandSender sender) {
        PluginMessages.USAGE.COMMAND.send(sender);
        return null;
    }

    @Override
    public Void noPermission(CommandSender sender) {
        PluginMessages.NO_PERMISSION.send(sender);
        return null;
    }

}
