package me.kikugie.tmcutils.util;

import net.minecraft.util.math.BlockPos;

public class WorldEditStorage {
    private static final BlockPos[] region = {null, null};
    public static String mode;

    public static void setPos(int pos, BlockPos blockPos) {
        region[pos] = blockPos;
    }

    public static void reset() {
        region[0] = null;
        region[1] = null;
        mode = null;
    }

    public static BlockPos getPos1() {
        return region[0];
    }

    public static void setPos1(BlockPos blockPos) {
        region[0] = blockPos;
    }

    public static BlockPos getPos2() {
        return region[1];
    }

    public static void setPos2(BlockPos blockPos) {
        region[1] = blockPos;
    }

    public static BlockPos[] getRegion() {
        return region;
    }

    public static boolean isComplete() {
        return region[0] != null && region[1] != null;
    }
}
