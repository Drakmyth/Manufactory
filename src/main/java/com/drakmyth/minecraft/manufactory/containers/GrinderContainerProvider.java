/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.containers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.wrapper.InvWrapper;

public class GrinderContainerProvider implements INamedContainerProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private BlockPos pos;

    public GrinderContainerProvider(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        LOGGER.debug("Creating Grinder gui...");
        return new GrinderContainer(windowId, new InvWrapper(playerInventory), player, pos);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Grinder");
    }
}
