/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items.upgrades;

import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;

import net.minecraft.world.item.Item;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class PowerSocketItem extends Item implements IPowerUpgrade {

    public PowerSocketItem(Properties properties) {
        super(properties);
    }

    @Override
    public float consumePower(float requestedPower, ServerLevel level, BlockPos pos) {
        PowerNetworkManager pnm = PowerNetworkManager.get(level);
        return pnm.consumePower(requestedPower, pos);
    }

    @Override
    public boolean rendersConnection() {
        return true;
    }
}
