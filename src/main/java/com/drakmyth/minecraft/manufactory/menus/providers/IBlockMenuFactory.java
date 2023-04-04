package com.drakmyth.minecraft.manufactory.menus.providers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.BlockPos;
import net.minecraftforge.items.IItemHandler;

public interface IBlockMenuFactory {
    AbstractContainerMenu create(int windowId, IItemHandler playerInventory, Player player, BlockPos pos);
}
