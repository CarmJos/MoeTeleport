package cc.carm.plugin.moeteleport.command.sub;

import cc.carm.lib.easyplugin.command.CommandHandler;
import cc.carm.plugin.moeteleport.command.MainCommands;
import cc.carm.plugin.moeteleport.command.sub.home.*;
import cc.carm.plugin.moeteleport.conf.PluginConfig;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class HomeCommands extends CommandHandler {

    protected final @NotNull MainCommands main;

    public HomeCommands(@NotNull JavaPlugin plugin, @NotNull MainCommands main,
                        @NotNull String cmd, @NotNull String... aliases) {
        super(plugin, cmd, aliases);
        this.main = main;

        registerSubCommand(new HomeTeleportCommand(this, "to", "teleport", "tp"));
        registerSubCommand(new HomeListCommand(this, "list", "ls"));
        registerSubCommand(new HomeCreateCommand(this, "set", "create"));
        registerSubCommand(new HomeDeleteCommand(this, "delete", "remove", "del"));
        registerSubCommand(new HomeRenameCommand(this, "rename", "rn"));
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!PluginConfig.HOMES.ENABLE.getNotNull()) {
            PluginMessages.NOT_ENABLED.send(sender);
            return true;
        }

        return super.onCommand(sender, command, label, args);
    }

    @Override
    public Void noArgs(CommandSender sender) {
        PluginMessages.USAGE.HOMES.send(sender);
        return null;
    }

    @Override
    public Void noPermission(CommandSender sender) {
        return main.noPermission(sender);
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("MoeTeleport.home");
    }

}
