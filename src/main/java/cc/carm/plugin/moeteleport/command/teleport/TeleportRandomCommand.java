package cc.carm.plugin.moeteleport.command.teleport;

import cc.carm.plugin.moeteleport.command.parent.TeleportCommands;
import cc.carm.plugin.moeteleport.command.sub.TeleportSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class TeleportRandomCommand extends TeleportSubCommand {

    public TeleportRandomCommand(@NotNull TeleportCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        //TODO 随机坐标传送
        return null;
    }

}
