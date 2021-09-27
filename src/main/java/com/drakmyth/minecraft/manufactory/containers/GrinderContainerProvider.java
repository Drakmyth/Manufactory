/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.containers;

import com.drakmyth.minecraft.manufactory.LogMarkers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.items.wrapper.InvWrapper;

public class GrinderContainerProvider implements MenuProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private BlockPos pos;

    public GrinderContainerProvider(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory Inventory, Player player) {
        LOGGER.debug(LogMarkers.CONTAINER, "Creating Grinder gui...");
        return new GrinderContainer(windowId, new InvWrapper(Inventory), player, pos);
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Grinder");
    }
}
