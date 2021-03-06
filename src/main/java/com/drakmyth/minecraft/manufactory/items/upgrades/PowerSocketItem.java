/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items.upgrades;

import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;

import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class PowerSocketItem extends Item implements IPowerUpgrade {

    public PowerSocketItem(Properties properties) {
        super(properties);
    }

    @Override
    public float consumePower(float requestedPower, ServerWorld world, BlockPos pos) {
        PowerNetworkManager pnm = PowerNetworkManager.get(world);
        return pnm.consumePower(requestedPower, pos);
    }

    @Override
    public boolean rendersConnection() {
        return true;
    }
}
