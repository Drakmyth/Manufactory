/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class DrillHeadUpgradeItem extends Item implements IDrillHeadUpgrade {
    private Tier tier;

    public DrillHeadUpgradeItem(Properties properties, Tier tier) {
        super(properties);
        this.tier = tier;
    }

    public Tier getTier() {
        return tier;
    }
}
