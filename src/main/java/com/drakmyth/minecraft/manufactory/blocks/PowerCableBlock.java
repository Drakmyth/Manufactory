/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import java.util.ArrayList;
import java.util.List;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.power.IPowerBlock;
import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class PowerCableBlock extends Block implements SimpleWaterloggedBlock, IPowerBlock {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape AABB_CENTER = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);
    protected static final VoxelShape AABB_NORTH = Block.box(5.0D, 0.0D, 0.0D, 11.0D, 6.0D, 7.0D);
    protected static final VoxelShape AABB_EAST = Block.box(9.0D, 0.0D, 5.0D, 16.0D, 6.0D, 11.0D);
    protected static final VoxelShape AABB_SOUTH = Block.box(5.0D, 0.0D, 9.0D, 11.0D, 6.0D, 16.0D);
    protected static final VoxelShape AABB_WEST = Block.box(0.0D, 0.0D, 5.0D, 7.0D, 6.0D, 11.0D);
    protected static final VoxelShape AABB_UP = Block.box(5.0D, 4.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    protected static final VoxelShape AABB_DOWN = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 2.0D, 11.0D);

    public PowerCableBlock(Properties properties) {
        super(properties);

        BlockState defaultState = this.stateDefinition.any()
            .setValue(NORTH, false)
            .setValue(EAST, false)
            .setValue(SOUTH, false)
            .setValue(WEST, false)
            .setValue(UP, false)
            .setValue(DOWN, false)
            .setValue(WATERLOGGED, false);
        this.registerDefaultState(defaultState);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        List<VoxelShape> shapes = new ArrayList<>();
        if (state.getValue(NORTH)) {
            shapes.add(AABB_NORTH);
        }
        if (state.getValue(EAST)) {
            shapes.add(AABB_EAST);
        }
        if (state.getValue(SOUTH)) {
            shapes.add(AABB_SOUTH);
        }
        if (state.getValue(WEST)) {
            shapes.add(AABB_WEST);
        }
        if (state.getValue(UP)) {
            shapes.add(AABB_UP);
        }
        if (state.getValue(DOWN)) {
            shapes.add(AABB_DOWN);
        }
        return Shapes.or(AABB_CENTER, shapes.toArray(new VoxelShape[0]));
    }

    private boolean canConnect(BlockState state, BlockPos pos, LevelAccessor world, Direction dir) {
        Block block = state.getBlock();
        if (!(block instanceof IPowerBlock)) return false;
        return ((IPowerBlock)block).canConnectToFace(state, pos, world, dir);
    }

    @Override
    public boolean canConnectToFace(BlockState state, BlockPos pos, LevelAccessor world, Direction dir) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos northPos = context.getClickedPos().north();
        BlockPos eastPos = context.getClickedPos().east();
        BlockPos southPos = context.getClickedPos().south();
        BlockPos westPos = context.getClickedPos().west();
        BlockPos upPos = context.getClickedPos().above();
        BlockPos downPos = context.getClickedPos().below();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
            .setValue(NORTH, canConnect(world.getBlockState(northPos), northPos, world, Direction.SOUTH))
            .setValue(EAST, canConnect(world.getBlockState(eastPos), eastPos, world, Direction.WEST))
            .setValue(SOUTH, canConnect(world.getBlockState(southPos), southPos, world, Direction.NORTH))
            .setValue(WEST, canConnect(world.getBlockState(westPos), westPos, world, Direction.EAST))
            .setValue(UP, canConnect(world.getBlockState(upPos), upPos, world, Direction.DOWN))
            .setValue(DOWN, canConnect(world.getBlockState(downPos), downPos, world, Direction.UP))
            .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return !state.getValue(WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            world.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        Direction oppositeFacing = facing.getOpposite();
        boolean canConnect = canConnect(facingState, facingPos, world, oppositeFacing);
        switch(facing) {
            case NORTH:
                return stateIn.setValue(NORTH, canConnect);
            case EAST:
                return stateIn.setValue(EAST, canConnect);
            case SOUTH:
                return stateIn.setValue(SOUTH, canConnect);
            case WEST:
                return stateIn.setValue(WEST, canConnect);
            case UP:
                return stateIn.setValue(UP, canConnect);
            case DOWN:
                return stateIn.setValue(DOWN, canConnect);
            default:
                return stateIn;
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        LOGGER.debug(LogMarkers.INTERACTION, "Power Cable placed at ({}, {}, {})", pos.getX(), pos.getY(), pos.getZ());
        if (world.isClientSide()) return;
        PowerNetworkManager pnm = PowerNetworkManager.get((ServerLevel)world);
        pnm.trackBlock(pos, Direction.values(), getPowerBlockType());
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        LOGGER.debug(LogMarkers.MACHINE, "Power Cable at ({}, {}, {}) replaced.", pos.getX(), pos.getY(), pos.getZ());
        if (world.isClientSide()) return;
        if (state.is(newState.getBlock())) return;

        PowerNetworkManager pnm = PowerNetworkManager.get((ServerLevel)world);
        pnm.untrackBlock(pos);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED);
    }

    @Override
    public Type getPowerBlockType() {
        return Type.NONE;
    }

    @Override
    public float getAvailablePower(BlockState state, Level world, BlockPos pos) {
        return 0;
    }
}
