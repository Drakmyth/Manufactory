package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.world.item.ToolMaterial;

public interface IGrinderWheelUpgrade {
    ToolMaterial getTier();

    float getEfficiency();
}
