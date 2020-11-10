/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import com.drakmyth.minecraft.manufactory.power.IPowerBlock;
import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SolarPanelBlock extends Block implements IWaterLoggable, IPowerBlock {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public SolarPanelBlock(Properties properties) {
        super(properties);

        BlockState defaultState = this.stateContainer.getBaseState()
            .with(HORIZONTAL_FACING, Direction.NORTH)
            .with(WATERLOGGED, false);
        this.setDefaultState(defaultState);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
    }

    @Override
    public boolean canConnectToFace(BlockState state, BlockPos pos, IWorld world, Direction dir) {
        return dir == state.get(HORIZONTAL_FACING).getOpposite();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction facing = context.getPlacementHorizontalFacing().getOpposite();
        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
        return this.getDefaultState().with(HORIZONTAL_FACING, facing)
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
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        LOGGER.debug("Solar Panel placed at (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ());
        if (world.isRemote()) return;
        PowerNetworkManager pnm = PowerNetworkManager.get((ServerWorld)world);
        pnm.trackBlock(pos, new Direction[] {state.get(HORIZONTAL_FACING).getOpposite()}, getPowerBlockType());
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        LOGGER.debug("Solar Panel at (%d, %d, %d) replaced.", pos.getX(), pos.getY(), pos.getZ());
        if (world.isRemote()) return;
        if (state.isIn(newState.getBlock())) return;

        PowerNetworkManager pnm = PowerNetworkManager.get((ServerWorld)world);
        pnm.untrackBlock(pos);
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, WATERLOGGED);
    }

    @Override
    public Type getPowerBlockType() {
        return Type.SOURCE;
    }

    @Override
    public float getAvailablePower(BlockState state, World world, BlockPos pos) {
        if (!world.getDimensionType().hasSkyLight()) return 0;

        float celestialAngle = world.getCelestialAngleRadians(1.0F);
        if (celestialAngle >= Math.PI / 2 && celestialAngle <= 3 * Math.PI / 2) return 0;
        float timeFactor = (float)Math.cos(celestialAngle);

        // TODO: change pos.up() to pos once solar panel is no longer a full block size
        float lightAndWeatherFactor = world.getLight(pos.up()) / 15f;
        // TODO: make 0.03125f read from config file
        float availablePower = 0.03125f * timeFactor * lightAndWeatherFactor;
        LOGGER.trace("Solar Panel at (%d, %d, %d) made %f power available", pos.getX(), pos.getY(), pos.getZ(), availablePower);
        return availablePower;
    }
}
