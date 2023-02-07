package cc.carm.plugin.moeteleport.command;

import cc.carm.lib.easyplugin.command.SimpleCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class MoeCompleter extends SimpleCompleter {

    public static @NotNull List<String> objects(@NotNull String input, Collection<?> objects) {
        return objects(input, objects.size(), objects);
    }

}
