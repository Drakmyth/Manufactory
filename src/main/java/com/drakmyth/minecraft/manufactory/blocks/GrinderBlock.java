/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.init.ModTags;
import com.drakmyth.minecraft.manufactory.init.ModTileEntityTypes;
import com.drakmyth.minecraft.manufactory.items.upgrades.IPowerUpgrade;
import com.drakmyth.minecraft.manufactory.menus.providers.GrinderContainerProvider;
import com.drakmyth.minecraft.manufactory.menus.providers.GrinderUpgradeContainerProvider;
import com.drakmyth.minecraft.manufactory.network.ModPacketHandler;
import com.drakmyth.minecraft.manufactory.network.OpenContainerWithUpgradesPacket;
import com.drakmyth.minecraft.manufactory.power.IPowerBlock;
import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;
import com.drakmyth.minecraft.manufactory.tileentities.GrinderTileEntity;

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

public class GrinderBlock extends Block implements IPowerBlock, EntityBlock {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public GrinderBlock(Properties properties) {
        super(properties);

        BlockState defaultState = this.stateDefinition.any()
            .setValue(HORIZONTAL_FACING, Direction.NORTH);
        this.registerDefaultState(defaultState);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        LOGGER.trace(LogMarkers.MACHINE, "Creating Grinder tile entity...");
        return ModTileEntityTypes.GRINDER.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> blockEntityType) {
        return (l, p, s, t) -> {
            if (t instanceof GrinderTileEntity tile) {
                tile.tick();
            }
        };
    }

    @Override
    public boolean canConnectToFace(BlockState state, BlockPos pos, LevelAccessor world, Direction dir) {
        BlockEntity te = world.getBlockEntity(pos);
        if (!(te instanceof GrinderTileEntity)) return false;
        if (dir != state.getValue(HORIZONTAL_FACING).getOpposite()) return false;

        GrinderTileEntity gte = (GrinderTileEntity)te;
        ItemStackHandler upgradeInventory = gte.getUpgradeInventory();
        Item powerUpgrade = upgradeInventory.getStackInSlot(3).getItem();
        if (!(powerUpgrade instanceof IPowerUpgrade)) return false;

        return ((IPowerUpgrade)powerUpgrade).rendersConnection();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, facing);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        LOGGER.debug(LogMarkers.INTERACTION, "Interacted with Grinder at ({}, {}, {})", pos.getX(), pos.getY(), pos.getZ());
        if (world.isClientSide) return InteractionResult.SUCCESS;
        interactWith(state, world, pos, player, player.getItemInHand(hand), hit.getDirection());
        return InteractionResult.CONSUME;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        LOGGER.debug(LogMarkers.INTERACTION, "Grinder placed at ({}, {}, {})", pos.getX(), pos.getY(), pos.getZ());
        if (world.isClientSide()) return;
        PowerNetworkManager pnm = PowerNetworkManager.get((ServerLevel)world);
        pnm.trackBlock(pos, new Direction[] {state.getValue(HORIZONTAL_FACING).getOpposite()}, getPowerBlockType());
    }

    private void interactWith(BlockState state, Level world, BlockPos pos, Player player, ItemStack heldItem, Direction face) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (!(tileEntity instanceof GrinderTileEntity)) {
            LOGGER.warn(LogMarkers.MACHINE, "Tile entity not instance of GrinderTileEntity!");
            return;
        }

        MenuProvider containerProvider;
        if (ModTags.Items.UPGRADE_ACCESS_TOOL.contains(heldItem.getItem()) && face == state.getValue(HORIZONTAL_FACING).getOpposite()) {
            LOGGER.debug(LogMarkers.INTERACTION, "Used wrench on back face. Opening upgrade gui...");
            containerProvider = new GrinderUpgradeContainerProvider(pos);
        } else {
            LOGGER.debug(LogMarkers.INTERACTION, "Opening main gui...");
            containerProvider = new GrinderContainerProvider(pos);
        }
        OpenContainerWithUpgradesPacket packet = new OpenContainerWithUpgradesPacket(((GrinderTileEntity)tileEntity).getInstalledUpgrades(), pos);
        ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), packet);
        NetworkHooks.openGui((ServerPlayer)player, containerProvider, pos);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        LOGGER.debug(LogMarkers.MACHINE, "Grinder at ({}, {}, {}) replaced.", pos.getX(), pos.getY(), pos.getZ());
        if (world.isClientSide()) return;
        if (state.is(newState.getBlock())) return;

        PowerNetworkManager pnm = PowerNetworkManager.get((ServerLevel)world);
        pnm.untrackBlock(pos);

        BlockEntity tileentity = world.getBlockEntity(pos);
        if (!(tileentity instanceof GrinderTileEntity)) {
            LOGGER.warn(LogMarkers.MACHINE, "Tile entity not instance of GrinderTileEntity!");
            return;
        }

        GrinderTileEntity grinderTE = (GrinderTileEntity)tileentity;
        ItemStackHandler inventory = grinderTE.getInventory();
        for (int i = 0; i < inventory.getSlots(); i++) {
            LOGGER.debug(LogMarkers.MACHINE, "Spawning inventory contents in world...");
            popResource(world, pos, inventory.getStackInSlot(i));
        }

        ItemStackHandler upgradeInventory = grinderTE.getUpgradeInventory();
        for (int i = 0; i < upgradeInventory.getSlots(); i++) {
            LOGGER.debug(LogMarkers.MACHINE, "Spawning upgrade inventory contents in world...");
            popResource(world, pos, upgradeInventory.getStackInSlot(i));
        }
        world.removeBlockEntity(pos);
    }

    @Override
    public Type getPowerBlockType() {
        return Type.SINK;
    }

    @Override
    public float getAvailablePower(BlockState state, Level world, BlockPos pos) {
        return 0;
    }
}
