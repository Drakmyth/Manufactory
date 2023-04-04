package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public interface IMillingBallUpgrade {
    Tier getTier();

    float getProcessChance(ItemStack stack);

    float getEfficiency(ItemStack stack);
}
