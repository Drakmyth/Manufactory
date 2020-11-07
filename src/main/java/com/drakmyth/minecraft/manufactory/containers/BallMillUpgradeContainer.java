/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.containers;

import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModContainerTypes;
import com.drakmyth.minecraft.manufactory.items.upgrades.IMillingBallUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IMotorUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IPowerUpgrade;
import com.drakmyth.minecraft.manufactory.tileentities.BallMillTileEntity;

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

public class BallMillUpgradeContainer extends Container {

    public final ItemStackHandler upgradeInventory;
    private final IWorldPosCallable posCallable;
    private final BallMillTileEntity tileEntity;

    public BallMillUpgradeContainer(int windowId, PlayerInventory playerInventory, PacketBuffer data) {
        this(windowId, new InvWrapper(playerInventory), playerInventory.player, data.readBlockPos());
    }

    public BallMillUpgradeContainer(int windowId, IItemHandler playerInventory, PlayerEntity player, BlockPos pos) {
        super(ModContainerTypes.BALL_MILL_UPGRADE.get(), windowId);
        World world = player.getEntityWorld();
        posCallable = IWorldPosCallable.of(world, pos);
        tileEntity = (BallMillTileEntity)world.getTileEntity(pos);
        upgradeInventory = tileEntity.getUpgradeInventory();

        // Ball Mill Slots
        // Milling Ball Slot
        this.addSlot(new SlotItemHandler(upgradeInventory, 0, 62, 14) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() instanceof IMillingBallUpgrade;
            }
        });
        // Motor Slot
        this.addSlot(new SlotItemHandler(upgradeInventory, 1, 80, 36) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() instanceof IMotorUpgrade;
            }
        });
        // Power Slot
        this.addSlot(new SlotItemHandler(upgradeInventory, 2, 80, 58) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() instanceof IPowerUpgrade;
            }

            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
                tileEntity.getBlockState().updateNeighbours(world, pos, 3);
            }
        });

        // Player Inventory
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 9; i++) {
                this.addSlot(new SlotItemHandler(playerInventory, i + (j * 9) + 9, (i + 1) * 8 + (i * 10), j * 18 + 84));
            }
        }

        // Player Hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new SlotItemHandler(playerInventory, i, (i + 1) * 8 + (i * 10), 142));
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < upgradeInventory.getSlots()) { // transfer from ball mill to inventory
                if (!this.mergeItemStack(itemstack1, upgradeInventory.getSlots(), this.inventorySlots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 1, false)) { // transfer from inventory to ball mill
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