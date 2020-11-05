/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.containers;

import java.util.Arrays;
import java.util.List;

import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModContainerTypes;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import com.drakmyth.minecraft.manufactory.items.IMotorUpgrade;
import com.drakmyth.minecraft.manufactory.tileentities.GrinderTileEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class GrinderUpgradeContainer extends Container {

    public final ItemStackHandler upgradeInventory;
    private final IWorldPosCallable posCallable;
    private final GrinderTileEntity tileEntity;

    public GrinderUpgradeContainer(int windowId, PlayerInventory playerInventory, PacketBuffer data) {
        this(windowId, new InvWrapper(playerInventory), playerInventory.player, data.readBlockPos());
    }

    public GrinderUpgradeContainer(int windowId, IItemHandler playerInventory, PlayerEntity player, BlockPos pos) {
        super(ModContainerTypes.GRINDER_UPGRADE.get(), windowId);
        World world = player.getEntityWorld();
        posCallable = IWorldPosCallable.of(world, pos);
        tileEntity = (GrinderTileEntity)world.getTileEntity(pos);
        upgradeInventory = tileEntity.getUpgradeInventory();

        // Grinder Slots
        // Wheel Slot 1
        this.addSlot(new SlotItemHandler(upgradeInventory, 0, 62, 14) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return isItemGrinderWheel(stack);
            }
        });
        // Wheel Slot 2
        this.addSlot(new SlotItemHandler(upgradeInventory, 1, 98, 14) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return isItemGrinderWheel(stack);
            }
        });
        // Motor Slot
        this.addSlot(new SlotItemHandler(upgradeInventory, 2, 80, 36) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() instanceof IMotorUpgrade;
            }
        });
        // Power Slot
        this.addSlot(new SlotItemHandler(upgradeInventory, 3, 80, 58) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return isItemPowerUpgrade(stack);
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

    private boolean isItemGrinderWheel(ItemStack stack) {
        return false;
    }

    private boolean isItemPowerUpgrade(ItemStack stack) {
        List<Item> powerUpgrades = Arrays.asList(ModItems.BATTERY.get(), ModItems.POWER_SOCKET.get());
        return powerUpgrades.contains(stack.getItem());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < upgradeInventory.getSlots()) { // transfer from grinder to inventory
                if (!this.mergeItemStack(itemstack1, upgradeInventory.getSlots(), this.inventorySlots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 1, false)) { // transfer from inventory to grinder
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
        return isWithinUsableDistance(posCallable, playerIn, ModBlocks.GRINDER.get());
    }
}
