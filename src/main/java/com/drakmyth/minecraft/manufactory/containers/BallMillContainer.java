/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.containers;

import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModContainerTypes;
import com.drakmyth.minecraft.manufactory.tileentities.BallMillTileEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BallMillContainer extends Container {
    private static final Logger LOGGER = LogManager.getLogger();

    public final ItemStackHandler ballMillInventory;
    private final IWorldPosCallable posCallable;
    private final BallMillTileEntity tileEntity;

    public BallMillContainer(int windowId, PlayerInventory playerInventory, PacketBuffer data) {
        this(windowId, new InvWrapper(playerInventory), playerInventory.player, data.readBlockPos());
    }

    public BallMillContainer(int windowId, IItemHandler playerInventory, PlayerEntity player, BlockPos pos) {
        super(ModContainerTypes.BALL_MILL.get(), windowId);
        LOGGER.debug("Initializing BallMillContainer...");
        World world = player.getEntityWorld();
        posCallable = IWorldPosCallable.of(world, pos);
        tileEntity = (BallMillTileEntity)world.getTileEntity(pos);
        ballMillInventory = tileEntity.getInventory();

        // Grinder Slots
        // Input Slot
        this.addSlot(new SlotItemHandler(ballMillInventory, 0, 56, 35));
        LOGGER.debug("Input slot added with index 0");
        // Output Slot
        this.addSlot(new SlotItemHandler(ballMillInventory, 1, 116, 35) {
            @Override
            public boolean isItemValid(ItemStack stack) {
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
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < ballMillInventory.getSlots()) { // transfer from ball mill to inventory
                LOGGER.debug("Transferring stack from ball mill slot %d to player inventory...", index);
                if (!this.mergeItemStack(itemstack1, ballMillInventory.getSlots(), this.inventorySlots.size(), false)) {
                    LOGGER.debug("Transfer failed because player inventory is full");
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 1, false)) { // transfer from inventory to ball mill
                LOGGER.debug("Transfer of stack from player inventory slot %d to ball mill failed because ball mill input is full", index);
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
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
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(posCallable, playerIn, ModBlocks.BALL_MILL.get());
    }
}
