/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.item.Item;

public class GrinderWheelUpgradeItem extends Item implements IGrinderWheelUpgrade {
    private int tier;

    public GrinderWheelUpgradeItem(Properties properties, int tier) {
        super(properties);
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }
}
