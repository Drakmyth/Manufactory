/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;

public class GrinderWheelUpgradeItem extends Item implements IGrinderWheelUpgrade {
    private Tiers tier;
    private float efficiency;

    public GrinderWheelUpgradeItem(Properties properties, Tiers tier, float efficiency) {
        super(properties);
        this.tier = tier;
        this.efficiency = efficiency;
    }

    public Tiers getTier() {
        return tier;
    }

    public float getEfficiency() {
        return efficiency;
    }
}
