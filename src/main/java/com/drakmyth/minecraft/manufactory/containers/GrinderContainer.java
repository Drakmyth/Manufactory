/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.containers;

import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModContainerTypes;
import com.drakmyth.minecraft.manufactory.tileentities.GrinderTileEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GrinderContainer extends Container {

    public final Inventory grinderInventory;
    private final IWorldPosCallable posCallable;

    public GrinderContainer(int windowId, PlayerInventory playerInventory, PacketBuffer data) {
        this(windowId, playerInventory, playerInventory.player, data.readBlockPos());
    }

    public GrinderContainer(int windowId, PlayerInventory playerInventory, PlayerEntity player, BlockPos pos) {
        super(ModContainerTypes.GRINDER.get(), windowId);
        World world = player.getEntityWorld();
        posCallable = IWorldPosCallable.of(world, pos);
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof GrinderTileEntity)) {
            grinderInventory = new Inventory(2);
        } else {
            grinderInventory = ((GrinderTileEntity)te).getInventory();
        }

        // Grinder Slots
        // Input Slot
        this.addSlot(new Slot(grinderInventory, 0, 56, 35));
        // Output Slot
        this.addSlot(new Slot(grinderInventory, 1, 116, 35));

        // Player Inventory
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 9; i++) {
                this.addSlot(new Slot(playerInventory, i + (j * 9) + 9, (i + 1) * 8 + (i * 10), j * 18 + 84));
            }
        }

        // Player Hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, (i + 1) * 8 + (i * 10), 142));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(posCallable, playerIn, ModBlocks.GRINDER.get());
    }
}
