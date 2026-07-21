package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class GrinderWheelUpgradeItem extends Item implements IGrinderWheelUpgrade {
    private ToolMaterial tier;
    private float efficiency;

    public GrinderWheelUpgradeItem(Properties properties, ToolMaterial tier, float efficiency) {
        super(properties);
        this.tier = tier;
        this.efficiency = efficiency;
    }

    public ToolMaterial getTier() {
        return tier;
    }

    public float getEfficiency() {
        return efficiency;
    }
}
