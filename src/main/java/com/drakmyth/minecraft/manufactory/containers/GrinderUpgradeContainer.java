/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.containers;

import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModContainerTypes;
import com.drakmyth.minecraft.manufactory.items.upgrades.IGrinderWheelUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IMotorUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IPowerUpgrade;
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

public class GrinderUpgradeContainer extends AbstractContainerMenu {
    private static final Logger LOGGER = LogManager.getLogger();

    public final ItemStackHandler upgradeInventory;
    private final ContainerLevelAccess posCallable;
    private final GrinderTileEntity tileEntity;

    public GrinderUpgradeContainer(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        this(windowId, new InvWrapper(playerInventory), playerInventory.player, data.readBlockPos());
    }

    public GrinderUpgradeContainer(int windowId, IItemHandler playerInventory, Player player, BlockPos pos) {
        super(ModContainerTypes.GRINDER_UPGRADE.get(), windowId);
        LOGGER.debug("Initializing GrinderContainer...");
        Level world = player.getCommandSenderWorld();
        posCallable = ContainerLevelAccess.create(world, pos);
        tileEntity = (GrinderTileEntity)world.getBlockEntity(pos);
        upgradeInventory = tileEntity.getUpgradeInventory();

        // Grinder Slots
        // Wheel Slot 1
        this.addSlot(new SlotItemHandler(upgradeInventory, 0, 62, 14) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof IGrinderWheelUpgrade;
            }
        });
        LOGGER.debug("Grinder wheel slot 1 added with index 0");
        // Wheel Slot 2
        this.addSlot(new SlotItemHandler(upgradeInventory, 1, 98, 14) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof IGrinderWheelUpgrade;
            }
        });
        LOGGER.debug("Grinder wheel slot 2 added with index 1");
        // Motor Slot
        this.addSlot(new SlotItemHandler(upgradeInventory, 2, 80, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof IMotorUpgrade;
            }
        });
        LOGGER.debug("Motor slot added with index 2");
        // Power Slot
        this.addSlot(new SlotItemHandler(upgradeInventory, 3, 80, 58) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof IPowerUpgrade;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                LOGGER.debug("Power slot contents changed. Notifying neighbors...");
                tileEntity.getBlockState().updateNeighbourShapes(world, pos, 3);
            }
        });
        LOGGER.debug("Power slot added with index 3");

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
            if (index < upgradeInventory.getSlots()) { // transfer from grinder to inventory
                LOGGER.debug("Transferring stack from grinder upgrade slot %d to player inventory...", index);
                if (!this.moveItemStackTo(itemstack1, upgradeInventory.getSlots(), this.slots.size(), false)) {
                    LOGGER.debug("Transfer failed because player inventory is full");
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) { // transfer from inventory to grinder
                LOGGER.debug("Transfer of stack from player inventory slot %d to grinder upgrade inventory failed because upgrade inputs are full", index);
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
