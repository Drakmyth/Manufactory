/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.menus;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.blocks.entities.BallMillBlockEntity;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModMenuTypes;

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

public class BallMillMenu extends AbstractContainerMenu {
    private static final Logger LOGGER = LogManager.getLogger();

    public final ItemStackHandler ballMillInventory;
    private final ContainerLevelAccess posCallable;
    private final BallMillBlockEntity tileEntity;

    public BallMillMenu(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        this(windowId, new InvWrapper(playerInventory), playerInventory.player, data.readBlockPos());
    }

    public BallMillMenu(int windowId, IItemHandler playerInventory, Player player, BlockPos pos) {
        super(ModMenuTypes.BALL_MILL.get(), windowId);
        LOGGER.debug(LogMarkers.CONTAINER, "Initializing BallMillContainer...");
        Level world = player.getCommandSenderWorld();
        posCallable = ContainerLevelAccess.create(world, pos);
        tileEntity = (BallMillBlockEntity)world.getBlockEntity(pos);
        ballMillInventory = tileEntity.getInventory();

        // Grinder Slots
        // Input Slot
        this.addSlot(new SlotItemHandler(ballMillInventory, 0, 56, 35));
        LOGGER.debug(LogMarkers.CONTAINER, "Input slot added with index 0");
        // Output Slot
        this.addSlot(new SlotItemHandler(ballMillInventory, 1, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        LOGGER.debug(LogMarkers.CONTAINER, "Output slot added with index 1");

        // Player Inventory
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 9; i++) {
                this.addSlot(new SlotItemHandler(playerInventory, i + (j * 9) + 9, (i + 1) * 8 + (i * 10), j * 18 + 84));
            }
        }
        LOGGER.debug(LogMarkers.CONTAINER, "Player inventory slots added with indices 9-35");

        // Player Hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new SlotItemHandler(playerInventory, i, (i + 1) * 8 + (i * 10), 142));
        }
        LOGGER.debug(LogMarkers.CONTAINER, "Player hotbar slots added with indices 0-8");
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < ballMillInventory.getSlots()) { // transfer from ball mill to inventory
                LOGGER.debug(LogMarkers.CONTAINER, "Transferring stack from ball mill slot {} to player inventory...", index);
                if (!this.moveItemStackTo(itemstack1, ballMillInventory.getSlots(), this.slots.size(), false)) {
                    LOGGER.debug(LogMarkers.CONTAINER, "Transfer failed because player inventory is full");
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) { // transfer from inventory to ball mill
                LOGGER.debug(LogMarkers.CONTAINER, "Transfer of stack from player inventory slot {} to ball mill failed because ball mill input is full", index);
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
        return stillValid(posCallable, playerIn, ModBlocks.BALL_MILL.get());
    }
}
