/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.world.item.Item;

public class MotorUpgradeItem extends Item implements IMotorUpgrade {
    private float powerCapMultiplier;

    public MotorUpgradeItem(Properties properties, float powerCapMultiplier) {
        super(properties);
        this.powerCapMultiplier = powerCapMultiplier;
    }

    public float getPowerCapMultiplier() {
        return powerCapMultiplier;
    }

    @Override
    public float getMachineSpeedMultiplier() {
        return 1;
    }

    @Override
    public float getItemSpeed() {
        return 8;
    }
}
