package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;

public class MillingBallUpgradeItem extends Item implements IMillingBallUpgrade {
    private ToolMaterial tier;
    private float efficiency;

    public MillingBallUpgradeItem(Properties properties, ToolMaterial tier, float efficiency) {
        super(properties);
        this.tier = tier;
        this.efficiency = efficiency;
    }

    public ToolMaterial getTier() {
        return tier;
    }

    public float getProcessChance(ItemStack stack) {
        return 1f;
    }

    public float getEfficiency(ItemStack stack) {
        return efficiency * (stack.getCount() / 16.0f);
    }
}
