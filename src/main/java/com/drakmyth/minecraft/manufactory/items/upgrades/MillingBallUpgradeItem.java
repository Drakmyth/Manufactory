/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class MillingBallUpgradeItem extends Item implements IMillingBallUpgrade {
    private Tier tier;
    private float efficiency;

    public MillingBallUpgradeItem(Properties properties, Tier tier, float efficiency) {
        super(properties);
        this.tier = tier;
        this.efficiency = efficiency;
    }

    public Tier getTier() {
        return tier;
    }

    public float getProcessChance(ItemStack stack) {
        return 1f;
    }

    public float getEfficiency(ItemStack stack) {
        return efficiency * (stack.getCount() / 16.0f);
    }
}
