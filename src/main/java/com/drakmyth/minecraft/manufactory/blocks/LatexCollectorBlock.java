/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Plane;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class LatexCollectorBlock extends Block {
    public static final DirectionProperty HORIZONTAL_FACING = HorizontalBlock.HORIZONTAL_FACING;

    public LatexCollectorBlock(Properties properties) {
        super(properties);
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
        builder.add(HORIZONTAL_FACING);
    }
}
