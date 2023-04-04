package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.world.item.Tier;

public interface IGrinderWheelUpgrade {
    Tier getTier();

    float getEfficiency();
}
