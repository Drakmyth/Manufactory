/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items;

import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class PowerUpgradeItem extends Item implements IPowerUpgrade {
    private IPowerUpgrade consumePowerFunc;

    public PowerUpgradeItem(Properties properties, IPowerUpgrade consumePowerFunc) {
        super(properties);
        this.consumePowerFunc = consumePowerFunc;
    }

    public float consumePower(float requestedPower, ServerWorld world, BlockPos pos) {
        return consumePowerFunc.consumePower(requestedPower, world, pos);
    }
}
