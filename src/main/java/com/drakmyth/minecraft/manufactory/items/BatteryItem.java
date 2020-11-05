/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items;

import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class BatteryItem extends Item implements IPowerUpgrade {

    public BatteryItem(Properties properties) {
        super(properties);
    }

    @Override
    public float consumePower(float requestedPower, ServerWorld world, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean rendersConnection() {
        return false;
    }
}
