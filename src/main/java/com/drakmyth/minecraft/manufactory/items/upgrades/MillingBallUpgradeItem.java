/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;

public class MillingBallUpgradeItem extends Item implements IMillingBallUpgrade {
    private ItemTier tier;
    private float efficiency;

    public MillingBallUpgradeItem(Properties properties, ItemTier tier, float efficiency) {
        super(properties);
        this.tier = tier;
        this.efficiency = efficiency;
    }

    public ItemTier getTier() {
        return tier;
    }

    public float getProcessChance(ItemStack stack) {
        return 1f;
    }

    public float getEfficiency(ItemStack stack) {
        return efficiency * (stack.getCount() / 16.0f);
    }
}
