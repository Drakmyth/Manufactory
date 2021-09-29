/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class GrinderWheelUpgradeItem extends Item implements IGrinderWheelUpgrade {
    private Tier tier;
    private float efficiency;

    public GrinderWheelUpgradeItem(Properties properties, Tier tier, float efficiency) {
        super(properties);
        this.tier = tier;
        this.efficiency = efficiency;
    }

    public Tier getTier() {
        return tier;
    }

    public float getEfficiency() {
        return efficiency;
    }
}
