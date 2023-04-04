package com.drakmyth.minecraft.manufactory.util;

import java.util.Arrays;
import java.util.List;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public final class LogHelper {
    public static Object blockPos(BlockPos pos) {
        return LogUtils.defer(() -> String.format("(%s, %s, %s)", pos.getX(), pos.getY(), pos.getZ()));
    }

    public static Object items(List<ItemStack> items) {
        return LogUtils.defer(() -> itemArrayToString(items.toArray(new ItemStack[] {})));
    }

    public static Object items(ItemStack[] items) {
        return LogUtils.defer(() -> itemArrayToString(items));
    }

    private static String itemArrayToString(ItemStack[] items) {
        return String.format("[ %s ]", String.join(" , ", Arrays.stream(items).map(u -> u.toString()).toList()));
    }
}
