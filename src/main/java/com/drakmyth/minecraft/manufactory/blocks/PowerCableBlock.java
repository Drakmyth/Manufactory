/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class PowerCableBlock extends Block implements IWaterLoggable {
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape AABB_CENTER = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);
    protected static final VoxelShape AABB_NORTH = Block.makeCuboidShape(5.0D, 0.0D, 0.0D, 11.0D, 6.0D, 7.0D);
    protected static final VoxelShape AABB_EAST = Block.makeCuboidShape(9.0D, 0.0D, 5.0D, 16.0D, 6.0D, 11.0D);
    protected static final VoxelShape AABB_SOUTH = Block.makeCuboidShape(5.0D, 0.0D, 9.0D, 11.0D, 6.0D, 16.0D);
    protected static final VoxelShape AABB_WEST = Block.makeCuboidShape(0.0D, 0.0D, 5.0D, 7.0D, 6.0D, 11.0D);
    protected static final VoxelShape AABB_UP = Block.makeCuboidShape(5.0D, 4.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    protected static final VoxelShape AABB_DOWN = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 2.0D, 11.0D);

    public PowerCableBlock(Properties properties) {
        super(properties);

        BlockState defaultState = this.stateContainer.getBaseState()
            .with(NORTH, false)
            .with(EAST, false)
            .with(SOUTH, false)
            .with(WEST, false)
            .with(UP, false)
            .with(DOWN, false)
            .with(WATERLOGGED, false);
        this.setDefaultState(defaultState);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        List<VoxelShape> shapes = new ArrayList<>();
        if (state.get(NORTH)) {
            shapes.add(AABB_NORTH);
        }
        if (state.get(EAST)) {
            shapes.add(AABB_EAST);
        }
        if (state.get(SOUTH)) {
            shapes.add(AABB_SOUTH);
        }
        if (state.get(WEST)) {
            shapes.add(AABB_WEST);
        }
        if (state.get(UP)) {
            shapes.add(AABB_UP);
        }
        if (state.get(DOWN)) {
            shapes.add(AABB_DOWN);
        }
        return VoxelShapes.or(AABB_CENTER, shapes.toArray(new VoxelShape[0]));
    }

    private boolean canConnect(BlockState state) {
        return isCable(state.getBlock());
    }

    private boolean isCable(Block block) {
        return block instanceof PowerCableBlock;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getWorld();
        BlockPos northPos = context.getPos().north();
        BlockPos eastPos = context.getPos().east();
        BlockPos southPos = context.getPos().south();
        BlockPos westPos = context.getPos().west();
        BlockPos upPos = context.getPos().up();
        BlockPos downPos = context.getPos().down();
        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
        return this.getDefaultState()
            .with(NORTH, canConnect(world.getBlockState(northPos)))
            .with(EAST, canConnect(world.getBlockState(eastPos)))
            .with(SOUTH, canConnect(world.getBlockState(southPos)))
            .with(WEST, canConnect(world.getBlockState(westPos)))
            .with(UP, canConnect(world.getBlockState(upPos)))
            .with(DOWN, canConnect(world.getBlockState(downPos)))
            .with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return !state.get(WATERLOGGED);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        switch(facing) {
            case NORTH:
                return stateIn.with(NORTH, canConnect(facingState));
            case EAST:
                return stateIn.with(EAST, canConnect(facingState));
            case SOUTH:
                return stateIn.with(SOUTH, canConnect(facingState));
            case WEST:
                return stateIn.with(WEST, canConnect(facingState));
            case UP:
                return stateIn.with(UP, canConnect(facingState));
            case DOWN:
                return stateIn.with(DOWN, canConnect(facingState));
            default:
                return stateIn;
        }
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED);
    }
}
