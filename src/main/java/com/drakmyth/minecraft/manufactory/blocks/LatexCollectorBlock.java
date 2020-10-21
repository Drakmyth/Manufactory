/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import com.drakmyth.minecraft.manufactory.init.ModTileEntityTypes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Plane;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class LatexCollectorBlock extends Block {
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty FULL = BooleanProperty.create("full");
    protected static final VoxelShape AABB_NORTH = Block.makeCuboidShape(5.0D, 2.0D, 0.0D, 11.0D, 5.0D, 6.0D);
    protected static final VoxelShape AABB_SOUTH = Block.makeCuboidShape(5.0D, 2.0D, 10.0D, 11.0D, 5.0D, 16.0D);
    protected static final VoxelShape AABB_WEST = Block.makeCuboidShape(0.0D, 2.0D, 5.0D, 6.0D, 5.0D, 11.0D);
    protected static final VoxelShape AABB_EAST = Block.makeCuboidShape(10.0D, 2.0D, 5.0D, 16.0D, 5.0D, 11.0D);

    public LatexCollectorBlock(Properties properties) {
        super(properties);

        BlockState defaultState = this.stateContainer.getBaseState()
            .with(HORIZONTAL_FACING, Direction.NORTH)
            .with(FULL, false);
        this.setDefaultState(defaultState);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
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
            return null;
        }

        return this.getDefaultState().with(HORIZONTAL_FACING, face.getOpposite());
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighbor, BlockPos neighborPos, boolean isMoving) {
        Direction facing = state.get(HORIZONTAL_FACING);
        if (!pos.offset(facing).equals(neighborPos)) {
            return;
        }

        if (!isBlockLog(world, neighborPos)) {
            world.destroyBlock(pos, true);
        }
    }

    private boolean isFaceHorizontal(Direction direction) {
        return Plane.HORIZONTAL.test(direction);
    }

    private boolean isBlockLog(IWorldReader world, BlockPos pos) {
        return world.getBlockState(pos).func_235714_a_(BlockTags.LOGS);
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING)
               .add(FULL);
    }
}
