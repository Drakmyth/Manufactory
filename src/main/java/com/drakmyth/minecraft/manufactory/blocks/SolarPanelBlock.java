/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.config.ConfigData;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class SolarPanelBlock extends Block implements SimpleWaterloggedBlock, IPowerBlock {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public SolarPanelBlock(Properties properties) {
        super(properties);

        BlockState defaultState = this.stateDefinition.any()
            .setValue(HORIZONTAL_FACING, Direction.NORTH)
            .setValue(WATERLOGGED, false);
        this.registerDefaultState(defaultState);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public boolean canConnectToFace(BlockState state, BlockPos pos, LevelAccessor world, Direction dir) {
        return dir == state.getValue(HORIZONTAL_FACING).getOpposite();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, facing)
                                     .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return !state.getValue(WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        return stateIn;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        LOGGER.debug(LogMarkers.INTERACTION, "Solar Panel placed at (%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ());
        if (world.isClientSide()) return;
        PowerNetworkManager pnm = PowerNetworkManager.get((ServerLevel)world);
        pnm.trackBlock(pos, new Direction[] {state.getValue(HORIZONTAL_FACING).getOpposite()}, getPowerBlockType());
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        LOGGER.debug(LogMarkers.MACHINE, "Solar Panel at (%d, %d, %d) replaced.", pos.getX(), pos.getY(), pos.getZ());
        if (world.isClientSide()) return;
        if (state.is(newState.getBlock())) return;

        PowerNetworkManager pnm = PowerNetworkManager.get((ServerLevel)world);
        pnm.untrackBlock(pos);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, WATERLOGGED);
    }

    @Override
    public Type getPowerBlockType() {
        return Type.SOURCE;
    }

    @Override
    public float getAvailablePower(BlockState state, Level world, BlockPos pos) {
        if (!world.dimensionType().hasSkyLight()) return 0;

        float celestialAngle = world.getSunAngle(1.0F);
        if (celestialAngle >= Math.PI / 2 && celestialAngle <= 3 * Math.PI / 2) return 0;
        float timeFactor = (float)Math.cos(celestialAngle);

        // TODO: change pos.above() to pos once solar panel is no longer a full block size
        float lightAndWeatherFactor = world.getMaxLocalRawBrightness(pos.above()) / 15f;
        float peakPowerGen = ConfigData.SERVER.SolarPanelPeakPowerGeneration.get().floatValue();
        float availablePower = peakPowerGen * timeFactor * lightAndWeatherFactor;
        LOGGER.trace(LogMarkers.OPERATION, "Solar Panel at (%d, %d, %d) made %f power available", pos.getX(), pos.getY(), pos.getZ(), availablePower);
        return availablePower;
    }
}
