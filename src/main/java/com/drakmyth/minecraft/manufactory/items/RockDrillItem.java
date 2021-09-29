/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items;

import java.util.Map;

import javax.annotation.Nullable;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.menus.ItemInventory;
import com.drakmyth.minecraft.manufactory.menus.providers.RockDrillUpgradeContainerProvider;
import com.drakmyth.minecraft.manufactory.init.ModTags;
import com.drakmyth.minecraft.manufactory.items.upgrades.IDrillHeadUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IMotorUpgrade;
import com.drakmyth.minecraft.manufactory.items.upgrades.IPowerUpgrade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class RockDrillItem extends Item {
    private static final Logger LOGGER = LogManager.getLogger();

    public RockDrillItem(Properties properties) {
        super(properties);
    }

    public Container getInventory(ItemStack stack) {
        return new ItemInventory(stack, 3);
    }

    private IDrillHeadUpgrade getDrillHead(ItemStack stack) {
        Container inv = getInventory(stack);
        return getDrillHead(inv);
    }

    @Nullable
    private IDrillHeadUpgrade getDrillHead(Container inv) {
        ItemStack head = inv.getItem(0);
        return head.getItem() instanceof IDrillHeadUpgrade ? (IDrillHeadUpgrade)head.getItem() : null;
    }

    @Nullable
    private IMotorUpgrade getMotor(Container inv) {
        ItemStack motor = inv.getItem(1);
        return motor.getItem() instanceof IMotorUpgrade ? (IMotorUpgrade)motor.getItem() : null;
    }

    @Nullable
    private IPowerUpgrade getPowerAdapter(Container inv) {
        ItemStack power = inv.getItem(2);
        return power.getItem() instanceof IPowerUpgrade ? (IPowerUpgrade)power.getItem() : null;
    }
    
    private boolean isReadyToDig(ItemStack stack) {
        Container upgradeInventory = getInventory(stack);
        IDrillHeadUpgrade head = getDrillHead(upgradeInventory);
        IMotorUpgrade motor = getMotor(upgradeInventory);
        IPowerUpgrade power = getPowerAdapter(upgradeInventory);
        // TODO: check if enough power is available
        return head != null && motor != null && power != null;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Container upgradeInventory = getInventory(stack);
        IMotorUpgrade motor = getMotor(upgradeInventory);
        IDrillHeadUpgrade head = getDrillHead(upgradeInventory);
        return isReadyToDig(stack) && TierSortingRegistry.isCorrectTierForDrops(head.getTier(), state) ? motor.getBlockBreakingSpeed() : 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!(stack.getItem() instanceof RockDrillItem)) {
            LOGGER.warn(LogMarkers.MACHINE, "Stack not instance of RockDrillItem!");
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide()) {
            LOGGER.debug(LogMarkers.INTERACTION, "Opening upgrade gui...");
            MenuProvider containerProvider = new RockDrillUpgradeContainerProvider(stack);
            NetworkHooks.openGui((ServerPlayer)player, containerProvider, buf -> {
                buf.writeItem(stack);
            });
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        IDrillHeadUpgrade head = getDrillHead(stack);
        return isReadyToDig(stack) && state.is(ModTags.Blocks.MINEABLE_WITH_ROCK_DRILL) && TierSortingRegistry.isCorrectTierForDrops(head.getTier(), state);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        BlockState state = player.level.getBlockState(pos);
        if (isReadyToDig(stack) && state.is(ModTags.Blocks.ROCK_DRILL_SILK_TOUCH)) {
            stack.enchant(Enchantments.SILK_TOUCH, 1);
            LOGGER.debug(LogMarkers.INTERACTION, "Silk touch added to rock drill");
        }
        return super.onBlockStartBreak(stack, pos, player);
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
