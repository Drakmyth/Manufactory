package com.drakmyth.minecraft.manufactory.menus.providers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public interface IItemMenuFactory {
    AbstractContainerMenu create(int windowId, IItemHandler playerInventory, Player player, ItemStack stack);
}
