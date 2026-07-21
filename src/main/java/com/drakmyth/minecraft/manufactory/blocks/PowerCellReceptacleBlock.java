package com.drakmyth.minecraft.manufactory.blocks;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.ScheduledTick;

public class PowerCellReceptacleBlock extends Block implements SimpleWaterloggedBlock {
    public static final EnumProperty<ReceptacleCell> CELL = EnumProperty.create("cell", ReceptacleCell.class);
    public static final EnumProperty<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape AABB_RECEPTACLE = Block.box(6.0D, 0.0D, 8.0D, 10.0D, 16.0D, 16.0D);
    protected static final VoxelShape AABB_CELL = Block.box(5.0D, 3.0D, 7.0D, 11.0D, 13.0D, 13.0D);

    public PowerCellReceptacleBlock(Properties properties) {
        super(properties);

        BlockState defaultState = this.stateDefinition.any()
                .setValue(CELL, ReceptacleCell.NONE)
                .setValue(HORIZONTAL_FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false);
        this.registerDefaultState(defaultState);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        List<VoxelShape> shapes = new ArrayList<>();
        if (state.getValue(CELL) != ReceptacleCell.EMPTY) {
            shapes.add(AABB_CELL);
        }
        return Shapes.or(AABB_RECEPTACLE, shapes.toArray(new VoxelShape[0]));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, facing)
                .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
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
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(CELL, HORIZONTAL_FACING, WATERLOGGED);
    }
}
