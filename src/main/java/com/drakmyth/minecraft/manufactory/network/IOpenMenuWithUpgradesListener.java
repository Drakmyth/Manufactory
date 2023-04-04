package com.drakmyth.minecraft.manufactory.network;

import net.minecraft.world.item.ItemStack;

public interface IOpenMenuWithUpgradesListener {
    void onContainerOpened(ItemStack[] upgrades);
}
