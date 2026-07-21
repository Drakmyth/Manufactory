package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class DrillHeadUpgradeItem extends Item implements IDrillHeadUpgrade {
    private ToolMaterial tier;

    public DrillHeadUpgradeItem(Properties properties, ToolMaterial tier) {
        super(properties);
        this.tier = tier;
    }

    public ToolMaterial getTier() {
        return tier;
    }
}
