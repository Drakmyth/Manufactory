/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.blocks.entities.BallMillBlockEntity;
import com.drakmyth.minecraft.manufactory.init.ModTags;
import com.drakmyth.minecraft.manufactory.init.ModBlockEntityTypes;
import com.drakmyth.minecraft.manufactory.items.upgrades.IPowerUpgrade;
import com.drakmyth.minecraft.manufactory.menus.BallMillMenu;
import com.drakmyth.minecraft.manufactory.menus.BallMillUpgradeMenu;
import com.drakmyth.minecraft.manufactory.menus.providers.BlockMenuProvider;
import com.drakmyth.minecraft.manufactory.network.ModPacketHandler;
import com.drakmyth.minecraft.manufactory.network.OpenMenuWithUpgradesPacket;
import com.drakmyth.minecraft.manufactory.power.IPowerBlock;
import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;
import com.drakmyth.minecraft.manufactory.util.LogHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;

public class BallMillBlock extends Block implements IPowerBlock, EntityBlock {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BallMillBlock(Properties properties) {
        super(properties);

        BlockState defaultState = this.stateDefinition.any()
            .setValue(HORIZONTAL_FACING, Direction.NORTH);
        this.registerDefaultState(defaultState);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        LOGGER.trace(LogMarkers.MACHINE, "Creating Ball Mill block entity...");
        return ModBlockEntityTypes.BALL_MILL.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (tickerLevel, pos, tickerState, blockEntity) -> {
            if (blockEntity instanceof BallMillBlockEntity be) {
                be.tick();
            }
        };
    }

    @Override
    public boolean canConnectToFace(BlockState state, BlockPos pos, LevelAccessor level, Direction dir) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!BallMillBlockEntity.class.isInstance(be)) return false;
        if (dir != state.getValue(HORIZONTAL_FACING).getOpposite()) return false;

        BallMillBlockEntity gte = (BallMillBlockEntity)be;
        ItemStackHandler upgradeInventory = gte.getUpgradeInventory();
        Item powerUpgrade = upgradeInventory.getStackInSlot(2).getItem();
        if (!IPowerUpgrade.class.isInstance(powerUpgrade)) return false;

        return ((IPowerUpgrade)powerUpgrade).rendersConnection();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, facing);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        LOGGER.debug(LogMarkers.INTERACTION, "Interacted with Ball Mill at {}", () -> LogHelper.blockPos(pos));
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        interactWith(state, level, pos, player, player.getItemInHand(hand), hit.getDirection());
        return InteractionResult.CONSUME;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        LOGGER.debug(LogMarkers.INTERACTION, "Ball Mill placed at {}", () -> LogHelper.blockPos(pos));
        if (level.isClientSide()) return;
        PowerNetworkManager pnm = PowerNetworkManager.get((ServerLevel)level);
        pnm.trackBlock(pos, new Direction[] {state.getValue(HORIZONTAL_FACING).getOpposite()}, getPowerBlockType());
    }

    private void interactWith(BlockState state, Level level, BlockPos pos, Player player, ItemStack heldItem, Direction face) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!BallMillBlockEntity.class.isInstance(be)) {
            LOGGER.warn(LogMarkers.MACHINE, "Block entity not instance of BallMillBlockEntity!");
            return;
        }

        MenuProvider containerProvider;
        if (ModTags.Items.UPGRADE_ACCESS_TOOL.contains(heldItem.getItem()) && face == state.getValue(HORIZONTAL_FACING).getOpposite()) {
            LOGGER.debug(LogMarkers.INTERACTION, "Used access tool on back face. Opening upgrade gui...");
            containerProvider = new BlockMenuProvider("Ball Mill", pos, BallMillUpgradeMenu::new);
        } else {
            LOGGER.debug(LogMarkers.INTERACTION, "Opening main gui...");
            containerProvider = new BlockMenuProvider("Ball Mill", pos, BallMillMenu::new);
        }
        OpenMenuWithUpgradesPacket packet = new OpenMenuWithUpgradesPacket(((BallMillBlockEntity)be).getInstalledUpgrades(), pos);
        ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), packet);
        NetworkHooks.openGui((ServerPlayer)player, containerProvider, pos);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        LOGGER.debug(LogMarkers.MACHINE, "Ball Mill at {} replaced.", () -> LogHelper.blockPos(pos));
        if (level.isClientSide()) return;
        
        if (state.is(newState.getBlock())) return;

        PowerNetworkManager pnm = PowerNetworkManager.get((ServerLevel)level);
        pnm.untrackBlock(pos);

        BlockEntity be = level.getBlockEntity(pos);
        if (!BallMillBlockEntity.class.isInstance(be)) {
            LOGGER.warn(LogMarkers.MACHINE, "Block entity not instance of BallMillBlockEntity!");
            return;
        }

        BallMillBlockEntity ballMillBE = (BallMillBlockEntity)be;
        ItemStackHandler inventory = ballMillBE.getInventory();
        for (int i = 0; i < inventory.getSlots(); i++) {
            LOGGER.debug(LogMarkers.MACHINE, "Spawning inventory contents in level...");
            popResource(level, pos, inventory.getStackInSlot(i));
        }

        ItemStackHandler upgradeInventory = ballMillBE.getUpgradeInventory();
        for (int i = 0; i < upgradeInventory.getSlots(); i++) {
            LOGGER.debug(LogMarkers.MACHINE, "Spawning upgrade inventory contents in level...");
            popResource(level, pos, upgradeInventory.getStackInSlot(i));
        }
        level.removeBlockEntity(pos);
    }

    @Override
    public Type getPowerBlockType() {
        return Type.SINK;
    }

    @Override
    public float getAvailablePower(BlockState state, Level level, BlockPos pos) {
        return 0;
    }
}
