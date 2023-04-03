/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.blocks.entities.LatexCollectorBlockEntity;
import com.drakmyth.minecraft.manufactory.config.ConfigData;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import com.drakmyth.minecraft.manufactory.init.ModTags;
import com.drakmyth.minecraft.manufactory.util.LogHelper;
import com.drakmyth.minecraft.manufactory.init.ModBlockEntityTypes;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.StringRepresentable;
import net.minecraft.core.Direction.Plane;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;

public class LatexCollectorBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<FillStatus> FILL_STATUS = EnumProperty.create("fill_status", FillStatus.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape AABB_NORTH = Block.box(5.0D, 2.0D, 0.0D, 11.0D, 5.0D, 6.0D);
    protected static final VoxelShape AABB_SOUTH = Block.box(5.0D, 2.0D, 10.0D, 11.0D, 5.0D, 16.0D);
    protected static final VoxelShape AABB_WEST = Block.box(0.0D, 2.0D, 5.0D, 6.0D, 5.0D, 11.0D);
    protected static final VoxelShape AABB_EAST = Block.box(10.0D, 2.0D, 5.0D, 16.0D, 5.0D, 11.0D);

    public LatexCollectorBlock(Properties properties) {
        super(properties);

        BlockState defaultState = this.stateDefinition.any()
            .setValue(HORIZONTAL_FACING, Direction.NORTH)
            .setValue(FILL_STATUS, FillStatus.EMPTY)
            .setValue(WATERLOGGED, false);
        this.registerDefaultState(defaultState);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        LOGGER.debug(LogMarkers.INTERACTION, "Interacted with Latex Collector at {}", LogHelper.blockPos(pos));
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (state.getValue(FILL_STATUS) == FillStatus.FULL) {
            int configLatexSpawnCount = ConfigData.SERVER.FullLatexSpawnCount.get();
            ItemStack latexItemStack = new ItemStack(ModItems.COAGULATED_LATEX.get(), configLatexSpawnCount);
            LOGGER.debug(LogMarkers.MACHINE, "Spawning coagulated latex and setting collector to EMPTY...");
            popResource(level, pos, latexItemStack);
            level.setBlockAndUpdate(pos, state.setValue(FILL_STATUS, FillStatus.EMPTY));
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        LOGGER.trace(LogMarkers.MACHINE, "Creating Latex Collector tile entity...");
        return ModBlockEntityTypes.LATEX_COLLECTOR.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (tickerLevel, pos, tickerState, blockEntity) -> {
            if (blockEntity instanceof LatexCollectorBlockEntity be) {
                be.tick(tickerLevel, pos, tickerState);
            }
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(HORIZONTAL_FACING);
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        final Direction face = context.getClickedFace();
        if (!isFaceHorizontal(face) || !blockHasLatex(context.getLevel(), context.getClickedPos().relative(face.getOpposite()))) {
            LOGGER.debug(LogMarkers.INTERACTION, "Latex Collector must be placed on the side of a block with latex");
            return null;
        }

        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, face.getOpposite())
                                       .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return !state.getValue(WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.getFluidTicks().schedule(new ScheduledTick<Fluid>(Fluids.WATER, currentPos, Fluids.WATER.getTickDelay(level), 0));
        }
        return state;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbor, BlockPos neighborPos, boolean isMoving) {
        Direction facing = state.getValue(HORIZONTAL_FACING);
        // Only pay attention to the neighbor we're attached to
        if (!pos.relative(facing).equals(neighborPos)) return;

        if (!blockHasLatex(level, neighborPos)) {
            LOGGER.debug(LogMarkers.MACHINE, "Block destroyed. Destroying attached latex collector...");
            level.destroyBlock(pos, true);
        }
    }

    private boolean isFaceHorizontal(Direction direction) {
        return Plane.HORIZONTAL.test(direction);
    }

    private boolean blockHasLatex(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos).is(ModTags.Blocks.BLOCKS_WITH_LATEX);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, FILL_STATUS, WATERLOGGED);
    }

    public enum FillStatus implements StringRepresentable {
        EMPTY,
        FILLING,
        FULL;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}
