/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items;

import com.drakmyth.minecraft.manufactory.config.ConfigData;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import com.drakmyth.minecraft.manufactory.tileentities.LatexCollectorTileEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class TappingKnifeItem extends Item {
    private static final Logger LOGGER = LogManager.getLogger();

    public TappingKnifeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        LOGGER.debug("Tapping knife used");
        Level world = context.getLevel();
        if (world.isClientSide) return InteractionResult.SUCCESS;
        BlockPos tePos = context.getClickedPos().relative(context.getClickedFace());
        BlockEntity te = world.getBlockEntity(tePos);
        if (!LatexCollectorTileEntity.class.isInstance(te)) {
            LOGGER.debug("Latex Collector tile entity not found");
            return InteractionResult.PASS;
        }
        LOGGER.debug("Tapping...");
        LatexCollectorTileEntity lcte = (LatexCollectorTileEntity)te;
        boolean tapped = lcte.onTap(world, tePos, world.getBlockState(tePos));
        if (tapped) {
            tryGiveAmber(context.getPlayer(), context.getHand());
        }
        return InteractionResult.CONSUME;
    }

    private void tryGiveAmber(Player player, InteractionHand hand) {
        double configAmberSpawnChance = ConfigData.SERVER.AmberChance.get();
        LOGGER.debug("Rolling for amber against chance %f...", configAmberSpawnChance);
        if (configAmberSpawnChance <= 0) return;
        if (player.getRandom().nextDouble() >= configAmberSpawnChance ) return;
        LOGGER.debug("Roll success!");

        int configAmberSpawnCount = ConfigData.SERVER.AmberTapSpawnCount.get();
        ItemStack holdingItem = player.getItemInHand(hand);
        ItemStack amberItemStack = new ItemStack(ModItems.AMBER.get(), configAmberSpawnCount);
        if (holdingItem.isEmpty()) {
            player.setItemInHand(hand, amberItemStack);
            LOGGER.debug("%d amber put in player's hand", configAmberSpawnCount);
        } else if (!player.addItem(amberItemStack)) {
            player.drop(amberItemStack, false);
            LOGGER.debug("Player inventory full. %d amber spawned into world", configAmberSpawnCount);
        }
    }
}
