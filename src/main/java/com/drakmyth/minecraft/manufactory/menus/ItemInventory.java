/*
 * This class is adapted from ItemBackedInventory which is distributed as part of the Botania Mod.
 * Get the Source Code in github: https://github.com/Vazkii/Botania
 * Botania is Open Source and distributed under the Botania License: http://botaniamod.net/license.php
 */
package com.drakmyth.minecraft.manufactory.menus;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public class ItemInventory extends SimpleContainer {
    private final ItemStack host;

    public ItemInventory(ItemStack stack, int size) {
        super(size);
        this.host = stack;

        NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
        host.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(items);
        for (int i = 0; i < size; i++) setItem(i, items.get(i));
    }

    @Override
    public boolean stillValid(Player player) {
        return !host.isEmpty();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < getContainerSize(); i++) items.set(i, getItem(i));
        host.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
    }
}
