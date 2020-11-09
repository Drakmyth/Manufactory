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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TappingKnifeItem extends Item {
    private static final Logger LOGGER = LogManager.getLogger();

    public TappingKnifeItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        LOGGER.debug("Tapping knife used");
        World world = context.getWorld();
        if (world.isRemote) return ActionResultType.SUCCESS;
        BlockPos tePos = context.getPos().offset(context.getFace());
        TileEntity te = world.getTileEntity(tePos);
        if (!LatexCollectorTileEntity.class.isInstance(te)) {
            LOGGER.debug("Latex Collector tile entity not found");
            return ActionResultType.PASS;
        }
        LOGGER.debug("Tapping...");
        LatexCollectorTileEntity lcte = (LatexCollectorTileEntity)te;
        boolean tapped = lcte.onTap();
        if (tapped) {
            tryGiveAmber(context.getPlayer(), context.getHand());
        }
        return ActionResultType.CONSUME;
    }

    private void tryGiveAmber(PlayerEntity player, Hand hand) {
        double configAmberSpawnChance = ConfigData.SERVER.AmberChance.get();
        LOGGER.debug("Rolling for amber against chance %f...", configAmberSpawnChance);
        if (configAmberSpawnChance <= 0) return;
        if (random.nextDouble() >= configAmberSpawnChance ) return;
        LOGGER.debug("Roll success!");

        int configAmberSpawnCount = ConfigData.SERVER.AmberTapSpawnCount.get();
        ItemStack holdingItem = player.getHeldItem(hand);
        ItemStack amberItemStack = new ItemStack(ModItems.AMBER.get(), configAmberSpawnCount);
        if (holdingItem.isEmpty()) {
            player.setHeldItem(hand, amberItemStack);
            LOGGER.debug("%d amber put in player's hand", configAmberSpawnCount);
        } else if (!player.addItemStackToInventory(amberItemStack)) {
            player.dropItem(amberItemStack, false);
            LOGGER.debug("Player inventory full. %d amber spawned into world", configAmberSpawnCount);
        }
    }
}
