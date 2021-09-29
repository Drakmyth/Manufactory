/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items;

import java.util.Map;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.init.ModTags;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
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
        // return super.getDestroySpeed(stack, state);
        return 8; // Diamond speed
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        // TODO: Open upgrade gui
        return super.use(level, player, usedHand);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return state.is(ModTags.Blocks.MINEABLE_WITH_ROCK_DRILL);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        BlockState state = player.level.getBlockState(pos);
        if (state.is(ModTags.Blocks.ROCK_DRILL_SILK_TOUCH)) {
            itemstack.enchant(Enchantments.SILK_TOUCH, 1);
            LOGGER.debug(LogMarkers.INTERACTION, "Silk touch added to rock drill");
        }
        return super.onBlockStartBreak(itemstack, pos, player);
    }

    /*
    / This is a bit of a hack. Since there doesn't seem to be a way to set the loot context to use silk touch
    / without actually adding it to the rock drill, we *do* actually add it to the drill in onBlockStartBreak
    / if it's a block we want to silk touch. Then, since we don't want to silk touch all blocks, we immediately
    / remove silk touch here in the next inventory tick after the block has been mined.
    */
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        if (enchantments.containsKey(Enchantments.SILK_TOUCH)) {
            LOGGER.debug(LogMarkers.INTERACTION, "Silk touch detected on rock drill. Removing...");
            enchantments.remove(Enchantments.SILK_TOUCH);
            EnchantmentHelper.setEnchantments(enchantments, stack);
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
