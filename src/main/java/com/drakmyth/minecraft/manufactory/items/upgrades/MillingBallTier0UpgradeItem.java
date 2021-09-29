/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

public class MillingBallTier0UpgradeItem extends Item implements IMillingBallUpgrade {

    public MillingBallTier0UpgradeItem(Properties properties) {
        super(properties);
    }

    public Tier getTier() {
        return Tiers.WOOD;
    }

    public float getProcessChance(ItemStack stack) {
        return stack.getCount() / 16.0f;
    }

    public float getEfficiency(ItemStack stack) {
        return 0;
    }
}
