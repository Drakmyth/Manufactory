package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.world.item.Item;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class BatteryItem extends Item implements IPowerUpgrade {

    public BatteryItem(Properties properties) {
        super(properties);
    }

    @Override
    public float consumePower(float requestedPower, ServerLevel level, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean rendersConnection() {
        return false;
    }
}
