package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;

public interface IMillingBallUpgrade {
    ToolMaterial getTier();

    float getProcessChance(ItemStack stack);

    float getEfficiency(ItemStack stack);
}
