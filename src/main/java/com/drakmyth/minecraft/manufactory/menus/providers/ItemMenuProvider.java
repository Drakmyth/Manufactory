/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.menus.providers;

import com.drakmyth.minecraft.manufactory.LogMarkers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.MenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ItemMenuProvider implements MenuProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private String displayName;
    private ItemStack stack;
    private IItemMenuFactory factory;

    public ItemMenuProvider(String displayName, ItemStack stack, IItemMenuFactory factory) {
        this.displayName = displayName;
        this.stack = stack;
        this.factory = factory;
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        LOGGER.debug(LogMarkers.CONTAINER, "Creating {} gui...", displayName);
        return factory.create(windowId, new InvWrapper(playerInventory), player, stack);
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent(displayName);
    }
}