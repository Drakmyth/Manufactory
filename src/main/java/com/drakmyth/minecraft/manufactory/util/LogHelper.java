/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */
package com.drakmyth.minecraft.manufactory.util;

import java.util.Arrays;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public final class LogHelper {
    public static String blockPos(BlockPos pos) {
        return String.format("(%s, %s, %s)", pos.getX(), pos.getY(), pos.getZ());
    }

    public static String items(List<ItemStack> items) {
        return items(items.toArray(new ItemStack[]{}));
    }

    public static String items(ItemStack[] items) {
        return String.format("[ %s ]", String.join(" , ", Arrays.stream(items).map(u -> u.toString()).toList()));
    }
}
