/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.menus;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModContainerTypes;
import com.drakmyth.minecraft.manufactory.items.upgrades.IMillingBallUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IMotorUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IPowerUpgrade;
import com.drakmyth.minecraft.manufactory.tileentities.BallMillTileEntity;

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

public class BallMillUpgradeMenu extends AbstractContainerMenu {
    private static final Logger LOGGER = LogManager.getLogger();
    public final ItemStackHandler upgradeInventory;
    private final ContainerLevelAccess posCallable;
    private final BallMillTileEntity tileEntity;

    public BallMillUpgradeMenu(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        this(windowId, new InvWrapper(playerInventory), playerInventory.player, data.readBlockPos());
    }

    public BallMillUpgradeMenu(int windowId, IItemHandler playerInventory, Player player, BlockPos pos) {
        super(ModContainerTypes.BALL_MILL_UPGRADE.get(), windowId);
        LOGGER.debug(LogMarkers.CONTAINER, "Initializing BallMillUpgradeContainer...");
        Level world = player.getCommandSenderWorld();
        posCallable = ContainerLevelAccess.create(world, pos);
        tileEntity = (BallMillTileEntity)world.getBlockEntity(pos);
        upgradeInventory = tileEntity.getUpgradeInventory();

        // Ball Mill Slots
        // Milling Ball Slot
        this.addSlot(new SlotItemHandler(upgradeInventory, 0, 44, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof IMillingBallUpgrade;
            }
        });
        LOGGER.debug(LogMarkers.CONTAINER, "Milling ball slot added with index 0");
        // Motor Slot
        this.addSlot(new SlotItemHandler(upgradeInventory, 1, 80, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof IMotorUpgrade;
            }
        });
        LOGGER.debug(LogMarkers.CONTAINER, "Motor slot added with index 1");
        // Power Slot
        this.addSlot(new SlotItemHandler(upgradeInventory, 2, 80, 58) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof IPowerUpgrade;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                LOGGER.debug(LogMarkers.CONTAINER, "Power slot contents changed. Notifying neighbors...");
                tileEntity.getBlockState().updateNeighbourShapes(world, pos, 3);
            }
        });
        LOGGER.debug(LogMarkers.CONTAINER, "Power slot added with index 2");

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
            if (index < upgradeInventory.getSlots()) { // transfer from ball mill to inventory
                LOGGER.debug(LogMarkers.CONTAINER, "Transferring stack from ball mill upgrade slot {} to player inventory...", index);
                if (!this.moveItemStackTo(itemstack1, upgradeInventory.getSlots(), this.slots.size(), false)) {
                    LOGGER.debug(LogMarkers.CONTAINER, "Transfer failed because player inventory is full");
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, upgradeInventory.getSlots(), false)) { // transfer from inventory to ball mill
                LOGGER.debug(LogMarkers.CONTAINER, "Transfer of stack from player inventory slot {} to ball mill upgrade inventory failed because upgrade inputs are full", index);
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
