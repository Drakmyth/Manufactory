package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.world.item.Item;

public class MotorUpgradeItem extends Item implements IMotorUpgrade {
    private float powerCapMultiplier;
    private float blockBreakingSpeed;

    public MotorUpgradeItem(Properties properties, float powerCapMultiplier, float blockBreakingSpeed) {
        super(properties);
        this.powerCapMultiplier = powerCapMultiplier;
        this.blockBreakingSpeed = blockBreakingSpeed;
    }

    public float getPowerCapMultiplier() {
        return powerCapMultiplier;
    }

    @Override
    public float getMachineSpeedMultiplier() {
        return 1;
    }

    @Override
    public float getBlockBreakingSpeed() {
        return blockBreakingSpeed;
    }
}
