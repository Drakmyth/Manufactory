/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;

public class GrinderWheelUpgradeItem extends Item implements IGrinderWheelUpgrade {
    private ItemTier tier;
    private float efficiency;

    public GrinderWheelUpgradeItem(Properties properties, ItemTier tier, float efficiency) {
        super(properties);
        this.tier = tier;
        this.efficiency = efficiency;
    }

    public ItemTier getTier() {
        return tier;
    }

    public float getEfficiency() {
        return efficiency;
    }
}
