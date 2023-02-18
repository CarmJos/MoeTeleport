package cc.carm.plugin.moeteleport.command.sub.teleport;

import cc.carm.plugin.moeteleport.command.sub.TeleportCommands;
import cc.carm.plugin.moeteleport.command.base.TeleportSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class TeleportRandomCommand extends TeleportSubCommand {

    public TeleportRandomCommand(@NotNull TeleportCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        return null;
    }

}
