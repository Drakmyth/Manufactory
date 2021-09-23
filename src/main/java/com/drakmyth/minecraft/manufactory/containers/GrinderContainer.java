/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.containers;

import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModContainerTypes;
import com.drakmyth.minecraft.manufactory.tileentities.GrinderTileEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class GrinderContainer extends AbstractContainerMenu {
    private static final Logger LOGGER = LogManager.getLogger();

    public final ItemStackHandler grinderInventory;
    private final ContainerLevelAccess posCallable;
    private final GrinderTileEntity tileEntity;

    public GrinderContainer(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        this(windowId, new InvWrapper(playerInventory), playerInventory.player, data.readBlockPos());
    }

    public GrinderContainer(int windowId, IItemHandler playerInventory, Player player, BlockPos pos) {
        super(ModContainerTypes.GRINDER.get(), windowId);
        LOGGER.debug("Initializing GrinderContainer...");
        Level world = player.getCommandSenderWorld();
        posCallable = ContainerLevelAccess.create(world, pos);
        tileEntity = (GrinderTileEntity)world.getBlockEntity(pos);
        grinderInventory = tileEntity.getInventory();

        // Grinder Slots
        // Input Slot
        this.addSlot(new SlotItemHandler(grinderInventory, 0, 56, 35));
        LOGGER.debug("Input slot added with index 0");
        // Output Slot
        this.addSlot(new SlotItemHandler(grinderInventory, 1, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        LOGGER.debug("Output slot added with index 1");

        // Player Inventory
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 9; i++) {
                this.addSlot(new SlotItemHandler(playerInventory, i + (j * 9) + 9, (i + 1) * 8 + (i * 10), j * 18 + 84));
            }
        }
        LOGGER.debug("Player inventory slots added with indices 9-35");

        // Player Hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new SlotItemHandler(playerInventory, i, (i + 1) * 8 + (i * 10), 142));
        }
        LOGGER.debug("Player hotbar slots added with indices 0-8");
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < grinderInventory.getSlots()) { // transfer from grinder to inventory
                LOGGER.debug("Transferring stack from grinder slot %d to player inventory...", index);
                if (!this.moveItemStackTo(itemstack1, grinderInventory.getSlots(), this.slots.size(), false)) {
                    LOGGER.debug("Transfer failed because player inventory is full");
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) { // transfer from inventory to grinder
                LOGGER.debug("Transfer of stack from player inventory slot %d to grinder failed because grinder input is full", index);
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }

    public float getProgress() {
        return tileEntity.getProgress();
    }

    public float getPowerRate() {
        return tileEntity.getPowerRate();
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(posCallable, playerIn, ModBlocks.GRINDER.get());
    }
}
