/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */
package com.drakmyth.minecraft.manufactory.blocks;

import java.util.function.Supplier;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * This whole class is a hack around the provider constructor on LiquidBlock being broken. The below code is copy-pasted
 * from LiquidBlock and has references to this.fluid replaced with getFluid(). When Forge releases an update that fixes
 * that constructor, this whole class should be removed.
 */
public class FixedLiquidBlock extends LiquidBlock {
    public FixedLiquidBlock(Supplier<? extends FlowingFluid> pFluid, Properties pProperties) {
        super(pFluid, pProperties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos,
            CollisionContext pContext) {
        return pContext.isAbove(STABLE_SHAPE, pPos, true) && pState.getValue(LEVEL) == 0
                && pContext.canStandOnFluid(pLevel.getFluidState(pPos.above()), getFluid()) ? STABLE_SHAPE
                        : Shapes.empty();
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return !getFluid().is(FluidTags.LAVA);
    }

    @Override
    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide) {
        return pAdjacentBlockState.getFluidState().getType().isSame(getFluid());
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (this.shouldSpreadLiquid(pLevel, pPos, pState)) {
            pLevel.getLiquidTicks().scheduleTick(pPos, pState.getFluidState().getType(),
                    getFluid().getTickDelay(pLevel));
        }

    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel,
            BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getFluidState().isSource() || pFacingState.getFluidState().isSource()) {
            pLevel.getLiquidTicks().scheduleTick(pCurrentPos, pState.getFluidState().getType(),
                    getFluid().getTickDelay(pLevel));
        }

        return pState;
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos,
            boolean pIsMoving) {
        if (this.shouldSpreadLiquid(pLevel, pPos, pState)) {
            pLevel.getLiquidTicks().scheduleTick(pPos, pState.getFluidState().getType(),
                    getFluid().getTickDelay(pLevel));
        }

    }

    private boolean shouldSpreadLiquid(Level pLevel, BlockPos pPos, BlockState pState) {
        if (getFluid().is(FluidTags.LAVA)) {
            boolean flag = pLevel.getBlockState(pPos.below()).is(Blocks.SOUL_SOIL);

            for (Direction direction : POSSIBLE_FLOW_DIRECTIONS) {
                BlockPos blockpos = pPos.relative(direction.getOpposite());
                if (pLevel.getFluidState(blockpos).is(FluidTags.WATER)) {
                    Block block = pLevel.getFluidState(pPos).isSource() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE;
                    pLevel.setBlockAndUpdate(pPos, net.minecraftforge.event.ForgeEventFactory
                            .fireFluidPlaceBlockEvent(pLevel, pPos, pPos, block.defaultBlockState()));
                    this.fizz(pLevel, pPos);
                    return false;
                }

                if (flag && pLevel.getBlockState(blockpos).is(Blocks.BLUE_ICE)) {
                    pLevel.setBlockAndUpdate(pPos, net.minecraftforge.event.ForgeEventFactory
                            .fireFluidPlaceBlockEvent(pLevel, pPos, pPos, Blocks.BASALT.defaultBlockState()));
                    this.fizz(pLevel, pPos);
                    return false;
                }
            }
        }

        return true;
    }

    private void fizz(LevelAccessor pLevel, BlockPos pPos) {
        pLevel.levelEvent(1501, pPos, 0);
     }

    @Override
    public ItemStack pickupBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if (pState.getValue(LEVEL) == 0) {
            pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 11);
            return new ItemStack(getFluid().getBucket());
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return getFluid().getPickupSound();
    }

}
