/*
 * This class is adapted from ItemBackedInventory which is distributed
 * as part of the Botania Mod.
 * 
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package com.drakmyth.minecraft.manufactory.containers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Constants.NBT;

public class ItemInventory extends SimpleContainer {
	private static final String ITEMS_KEY = "items";
	private final ItemStack host;

	public ItemInventory(ItemStack stack, int size) {
		super(size);
		this.host = stack;

        ListTag items = host.getOrCreateTag().getList(ITEMS_KEY, NBT.TAG_COMPOUND);
		for (int i = 0; i < size && i < items.size(); i++) {
            setItem(i, ItemStack.of(items.getCompound(i)));
		}
	}
    
	@Override
	public boolean stillValid(Player player) {
        return !host.isEmpty();
	}
    
	@Override
	public void setChanged() {
        super.setChanged();
		ListTag list = new ListTag();
		for (int i = 0; i < getContainerSize(); i++) {
            list.add(getItem(i).save(new CompoundTag()));
		}
        host.getOrCreateTag().put(ITEMS_KEY, list);
	}
}