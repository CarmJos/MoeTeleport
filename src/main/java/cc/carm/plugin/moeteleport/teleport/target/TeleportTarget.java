package cc.carm.plugin.moeteleport.teleport.target;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.Nullable;

public interface TeleportTarget {

    /**
     * 准备可传送的目的地
     *
     * @return 准备传送目的地
     */
    @Nullable Location prepare();

    /**
     * @return 目标的文字介绍
     */
    String getText();

    /**
     * 检查一个位置是否安全
     *
     * @param location 位置
     * @return 是否安全
     */
    @Deprecated
    static boolean checkLocation(Location location) {
        Block leg = location.getBlock();
        if (!leg.getType().isAir()) {
            return false; // not transparent (will suffocate)
        }
        Block head = leg.getRelative(BlockFace.UP);
        if (!head.getType().isAir()) {
            return false; // not transparent (will suffocate)
        }
        Block ground = leg.getRelative(BlockFace.DOWN);
//        return !PluginConfig.TELEPORTATION.DANGEROUS_TYPES.getNotNull().contains(ground.getType().name());
        return !ground.isPassable();
    }

}
