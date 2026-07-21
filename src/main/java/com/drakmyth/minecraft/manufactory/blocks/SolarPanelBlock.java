package com.drakmyth.minecraft.manufactory.blocks;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.config.ConfigData;
import com.drakmyth.minecraft.manufactory.power.IPowerBlock;
import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;
import com.drakmyth.minecraft.manufactory.util.LogHelper;
import com.mojang.logging.LogUtils;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;

public class SolarPanelBlock extends Block implements SimpleWaterloggedBlock, IPowerBlock {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final EnumProperty<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
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
    public boolean canConnectToFace(BlockState state, BlockPos pos, LevelReader level, Direction dir) {
        return dir == state.getValue(HORIZONTAL_FACING).getOpposite();
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
    protected boolean propagatesSkylightDown(BlockState state) {
        return !state.getValue(WATERLOGGED);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos currentPos,
            Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return state;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        LOGGER.debug(LogMarkers.INTERACTION, "Solar Panel placed at {}", LogHelper.blockPos(pos));
        if (level.isClientSide()) return;
        PowerNetworkManager pnm = PowerNetworkManager.get((ServerLevel)level);
        pnm.trackBlock(pos, new Direction[] { state.getValue(HORIZONTAL_FACING).getOpposite() }, getPowerBlockType());
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean isMoving) {
        LOGGER.debug(LogMarkers.MACHINE, "Solar Panel at {} replaced.", LogHelper.blockPos(pos));
        PowerNetworkManager pnm = PowerNetworkManager.get(level);
        pnm.untrackBlock(pos);
        super.affectNeighborsAfterRemoval(state, level, pos, isMoving);
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
    public float getAvailablePower(BlockState state, Level level, BlockPos pos) {
        if (!level.dimensionType().hasSkyLight()) return 0;

        float celestialAngle = getSunAngle(level.getDefaultClockTime());
        if (celestialAngle >= Math.PI / 2 && celestialAngle <= 3 * Math.PI / 2) return 0;
        float timeFactor = (float)Math.cos(celestialAngle);

        // TODO: change pos.above() to pos once solar panel is no longer a full block size
        float lightAndWeatherFactor = level.getMaxLocalRawBrightness(pos.above()) / 15f;
        float peakPowerGen = ConfigData.SERVER.SolarPanelPeakPowerGeneration.get().floatValue();
        float availablePower = peakPowerGen * timeFactor * lightAndWeatherFactor;
        LOGGER.trace(LogMarkers.POWERNETWORK, "Solar Panel at {} made {} power available", LogHelper.blockPos(pos), availablePower);
        return availablePower;
    }

    public static float getSunAngle(long clockTime) {
        double dayFraction = Math.floorMod(clockTime, 24000L) / 24000.0D - 0.25D;
        dayFraction -= Math.floor(dayFraction);
        double smoothed = 0.5D - Math.cos(dayFraction * Math.PI) / 2.0D;
        return (float)((dayFraction * 2.0D + (smoothed - dayFraction) / 3.0D) * Math.PI * 2.0D);
    }
}
