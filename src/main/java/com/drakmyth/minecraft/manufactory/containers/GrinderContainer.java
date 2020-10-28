/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.containers;

import com.drakmyth.minecraft.manufactory.init.ModContainerTypes;
import com.drakmyth.minecraft.manufactory.tileentities.GrinderTileEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.FurnaceFuelSlot;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class GrinderContainer extends Container {

    private TileEntity tileEntity;
    private PlayerEntity player;
    private IItemHandler playerInventory;

    public GrinderContainer(int windowId, PlayerInventory playerInventory, PacketBuffer data) {
        super(ModContainerTypes.GRINDER.get(), windowId);
        player = playerInventory.player;
        World world = player.getEntityWorld();
        BlockPos pos = data.readBlockPos();
        tileEntity = world.getTileEntity(pos);
        this.playerInventory = new InvWrapper(playerInventory);

        // if (tileEntity != null) {
        //     tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
        //         addSlot(new SlotItemHandler(handler, 0, 64, 24));
        //     });
        // }
        this.addSlot(new Slot(p_i241922_6_, 0, 56, 17));
        this.addSlot(new FurnaceFuelSlot(this, p_i241922_6_, 1, 56, 53));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, p_i241922_6_, 2, 116, 35));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
               this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
         }

         for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
         }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }
}
