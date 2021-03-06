/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import com.drakmyth.minecraft.manufactory.config.ConfigData;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import com.drakmyth.minecraft.manufactory.init.ModTileEntityTypes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Direction.Plane;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class LatexCollectorBlock extends Block implements IWaterLoggable {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<FillStatus> FILL_STATUS = EnumProperty.create("fill_status", FillStatus.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape AABB_NORTH = Block.makeCuboidShape(5.0D, 2.0D, 0.0D, 11.0D, 5.0D, 6.0D);
    protected static final VoxelShape AABB_SOUTH = Block.makeCuboidShape(5.0D, 2.0D, 10.0D, 11.0D, 5.0D, 16.0D);
    protected static final VoxelShape AABB_WEST = Block.makeCuboidShape(0.0D, 2.0D, 5.0D, 6.0D, 5.0D, 11.0D);
    protected static final VoxelShape AABB_EAST = Block.makeCuboidShape(10.0D, 2.0D, 5.0D, 16.0D, 5.0D, 11.0D);

    public LatexCollectorBlock(Properties properties) {
        super(properties);

        BlockState defaultState = this.stateContainer.getBaseState()
            .with(HORIZONTAL_FACING, Direction.NORTH)
            .with(FILL_STATUS, FillStatus.EMPTY)
            .with(WATERLOGGED, false);
        this.setDefaultState(defaultState);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        LOGGER.debug("Interacted with Latex Collector at (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ());
        if (world.isRemote) return ActionResultType.SUCCESS;
        if (state.get(FILL_STATUS) == FillStatus.FULL) {
            int configLatexSpawnCount = ConfigData.SERVER.FullLatexSpawnCount.get();
            ItemStack latexItemStack = new ItemStack(ModItems.COAGULATED_LATEX.get(), configLatexSpawnCount);
            LOGGER.debug("Spawning coagulated latex and setting collector to EMPTY...");
            spawnAsEntity(world, pos, latexItemStack);
            world.setBlockState(pos, state.with(FILL_STATUS, FillStatus.EMPTY));
        }
        return ActionResultType.CONSUME;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        LOGGER.trace("Creating Latex Collector tile entity...");
        return ModTileEntityTypes.LATEX_COLLECTOR.get().create();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.get(HORIZONTAL_FACING);
        switch (direction) {
            case EAST:
                return AABB_EAST;
            case WEST:
                return AABB_WEST;
            case SOUTH:
                return AABB_SOUTH;
            case NORTH:
            default:
                return AABB_NORTH;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        final Direction face = context.getFace();
        if (!isFaceHorizontal(face) || !isBlockLog(context.getWorld(), context.getPos().offset(face.getOpposite()))) {
            LOGGER.debug("Latex Collector must be placed on the side of a log");
            return null;
        }

        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
        return this.getDefaultState().with(HORIZONTAL_FACING, face.getOpposite())
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
        return stateIn;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighbor, BlockPos neighborPos, boolean isMoving) {
        Direction facing = state.get(HORIZONTAL_FACING);
        // Only pay attention to the neighbor we're attached to
        if (!pos.offset(facing).equals(neighborPos)) return;

        if (!isBlockLog(world, neighborPos)) {
            LOGGER.debug("Log destroyed. Destroying attached latex collector...");
            world.destroyBlock(pos, true);
        }
    }

    private boolean isFaceHorizontal(Direction direction) {
        return Plane.HORIZONTAL.test(direction);
    }

    private boolean isBlockLog(IWorldReader world, BlockPos pos) {
        return world.getBlockState(pos).isIn(BlockTags.LOGS);
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, FILL_STATUS, WATERLOGGED);
    }

    public enum FillStatus implements IStringSerializable {
        EMPTY,
        FILLING,
        FULL;

        @Override
        public String getString() {
            return this.name().toLowerCase();
        }
    }
}
