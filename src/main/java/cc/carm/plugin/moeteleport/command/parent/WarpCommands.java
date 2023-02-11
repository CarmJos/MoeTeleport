package cc.carm.plugin.moeteleport.command.parent;

import cc.carm.lib.easyplugin.command.CommandHandler;
import cc.carm.plugin.moeteleport.command.MainCommands;
import cc.carm.plugin.moeteleport.command.warp.*;
import cc.carm.plugin.moeteleport.conf.PluginConfig;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class WarpCommands extends CommandHandler {

    protected final @NotNull MainCommands main;

    public WarpCommands(@NotNull JavaPlugin plugin, @NotNull MainCommands main,
                        @NotNull String cmd, @NotNull String... aliases) {
        super(plugin, cmd, aliases);
        this.main = main;

        registerSubCommand(new WarpTeleportCommand(this, "to", "teleport", "tp"));
        registerSubCommand(new WarpListCommand(this, "list", "ls"));
        registerSubCommand(new WarpInfoCommand(this, "info", "i"));
        registerSubCommand(new WarpCreateCommand(this, "set", "create"));
        registerSubCommand(new WarpDeleteCommand(this, "delete", "remove", "del"));

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!PluginConfig.WARPS.ENABLE.getNotNull()) {
            PluginMessages.NOT_ENABLED.send(sender);
            return true;
        }

        return super.onCommand(sender, command, label, args);
    }

    @Override
    public Void noArgs(CommandSender sender) {
        PluginMessages.USAGE.WARPS.send(sender);
        return null;
    }

    @Override
    public Void noPermission(CommandSender sender) {
        return main.noPermission(sender);
    }

}
