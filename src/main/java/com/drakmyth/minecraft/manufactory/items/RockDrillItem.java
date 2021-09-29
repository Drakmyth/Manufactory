/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RockDrillItem extends Item {
    private static final Logger LOGGER = LogManager.getLogger();

    public RockDrillItem(Properties properties) {
        super(properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        // TODO: Implement motor effects
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        // TODO: Open upgrade gui
        return super.use(level, player, usedHand);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        // TODO: Implement valid block detection
        return super.isCorrectToolForDrops(stack, state);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        // TODO: Add Silk Touch enchant if block is valid
        return super.onBlockStartBreak(itemstack, pos, player);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        // TODO: Remove silk touch enchant because we don't actually want it for everything
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
