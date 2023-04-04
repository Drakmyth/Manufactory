package com.drakmyth.minecraft.manufactory.blocks;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.power.IPowerBlock;
import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;
import com.drakmyth.minecraft.manufactory.util.LogHelper;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
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
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class PowerCableBlock extends Block implements SimpleWaterloggedBlock, IPowerBlock {
    private static final Logger LOGGER = LogUtils.getLogger();
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
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
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

    private boolean canConnect(BlockState state, BlockPos pos, LevelAccessor level, Direction dir) {
        Block block = state.getBlock();
        if (!(block instanceof IPowerBlock)) return false;
        return ((IPowerBlock)block).canConnectToFace(state, pos, level, dir);
    }

    @Override
    public boolean canConnectToFace(BlockState state, BlockPos pos, LevelAccessor level, Direction dir) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos northPos = context.getClickedPos().north();
        BlockPos eastPos = context.getClickedPos().east();
        BlockPos southPos = context.getClickedPos().south();
        BlockPos westPos = context.getClickedPos().west();
        BlockPos upPos = context.getClickedPos().above();
        BlockPos downPos = context.getClickedPos().below();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(NORTH, canConnect(level.getBlockState(northPos), northPos, level, Direction.SOUTH))
                .setValue(EAST, canConnect(level.getBlockState(eastPos), eastPos, level, Direction.WEST))
                .setValue(SOUTH, canConnect(level.getBlockState(southPos), southPos, level, Direction.NORTH))
                .setValue(WEST, canConnect(level.getBlockState(westPos), westPos, level, Direction.EAST))
                .setValue(UP, canConnect(level.getBlockState(upPos), upPos, level, Direction.DOWN))
                .setValue(DOWN, canConnect(level.getBlockState(downPos), downPos, level, Direction.UP))
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

        Direction oppositeFacing = facing.getOpposite();
        boolean canConnect = canConnect(facingState, facingPos, level, oppositeFacing);
        switch (facing) {
            case NORTH:
                return state.setValue(NORTH, canConnect);
            case EAST:
                return state.setValue(EAST, canConnect);
            case SOUTH:
                return state.setValue(SOUTH, canConnect);
            case WEST:
                return state.setValue(WEST, canConnect);
            case UP:
                return state.setValue(UP, canConnect);
            case DOWN:
                return state.setValue(DOWN, canConnect);
            default:
                return state;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        LOGGER.debug(LogMarkers.INTERACTION, "Power Cable placed at {}", LogHelper.blockPos(pos));
        if (level.isClientSide()) return;
        PowerNetworkManager pnm = PowerNetworkManager.get((ServerLevel)level);
        pnm.trackBlock(pos, Direction.values(), getPowerBlockType());
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        LOGGER.debug(LogMarkers.MACHINE, "Power Cable at {} replaced.", LogHelper.blockPos(pos));
        if (level.isClientSide()) return;
        if (state.is(newState.getBlock())) return;

        PowerNetworkManager pnm = PowerNetworkManager.get((ServerLevel)level);
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
    public float getAvailablePower(BlockState state, Level level, BlockPos pos) {
        return 0;
    }
}
