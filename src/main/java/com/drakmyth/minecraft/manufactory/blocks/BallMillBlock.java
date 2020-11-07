/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import com.drakmyth.minecraft.manufactory.containers.BallMillContainerProvider;
import com.drakmyth.minecraft.manufactory.containers.BallMillUpgradeContainerProvider;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import com.drakmyth.minecraft.manufactory.init.ModTileEntityTypes;
import com.drakmyth.minecraft.manufactory.items.upgrades.IPowerUpgrade;
import com.drakmyth.minecraft.manufactory.power.IPowerBlock;
import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;
import com.drakmyth.minecraft.manufactory.tileentities.BallMillTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

public class BallMillBlock extends Block implements IPowerBlock {
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BallMillBlock(Properties properties) {
        super(properties);

        BlockState defaultState = this.stateContainer.getBaseState()
            .with(HORIZONTAL_FACING, Direction.NORTH);
        this.setDefaultState(defaultState);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntityTypes.BALL_MILL.get().create();
    }

    @Override
    public boolean canConnectToFace(BlockState state, BlockPos pos, IWorld world, Direction dir) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof BallMillTileEntity)) return false;
        if (dir != state.get(HORIZONTAL_FACING).getOpposite()) return false;

        BallMillTileEntity gte = (BallMillTileEntity)te;
        ItemStackHandler upgradeInventory = gte.getUpgradeInventory();
        Item powerUpgrade = upgradeInventory.getStackInSlot(2).getItem();
        if (!(powerUpgrade instanceof IPowerUpgrade)) return false;

        return ((IPowerUpgrade)powerUpgrade).rendersConnection();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction facing = context.getPlacementHorizontalFacing().getOpposite();
        return this.getDefaultState().with(HORIZONTAL_FACING, facing);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isRemote) return ActionResultType.SUCCESS;
        interactWith(state, world, pos, player, player.getHeldItem(hand), hit.getFace());
        return ActionResultType.CONSUME;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (world.isRemote()) return;
        PowerNetworkManager pnm = PowerNetworkManager.get((ServerWorld)world);
        pnm.trackBlock(pos, new Direction[] {state.get(HORIZONTAL_FACING).getOpposite()}, getPowerBlockType());
    }

    private void interactWith(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack heldItem, Direction face) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof BallMillTileEntity)) return;
        INamedContainerProvider containerProvider;
        if (heldItem.getItem() == ModItems.WRENCH.get() && face == state.get(HORIZONTAL_FACING).getOpposite()) {
            containerProvider = new BallMillUpgradeContainerProvider(pos);
        } else {
            containerProvider = new BallMillContainerProvider(pos);
        }
        NetworkHooks.openGui((ServerPlayerEntity)player, containerProvider, pos);
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (world.isRemote()) return;
        if (!state.isIn(newState.getBlock())) {
            PowerNetworkManager pnm = PowerNetworkManager.get((ServerWorld)world);
            pnm.untrackBlock(pos);

            TileEntity tileentity = world.getTileEntity(pos);
            if (!(tileentity instanceof BallMillTileEntity)) return;
            BallMillTileEntity ballMillTE = (BallMillTileEntity)tileentity;
            ItemStackHandler inventory = ballMillTE.getInventory();
            for (int i = 0; i < inventory.getSlots(); i++) {
                spawnAsEntity(world, pos, inventory.getStackInSlot(i));
            }
            ItemStackHandler upgradeInventory = ballMillTE.getUpgradeInventory();
            for (int i = 0; i < upgradeInventory.getSlots(); i++) {
                spawnAsEntity(world, pos, upgradeInventory.getStackInSlot(i));
            }
            world.removeTileEntity(pos);
        }
    }

    @Override
    public Type getPowerBlockType() {
        return Type.SINK;
    }

    @Override
    public float getAvailablePower(BlockState state, World world, BlockPos pos) {
        return 0;
    }
}
