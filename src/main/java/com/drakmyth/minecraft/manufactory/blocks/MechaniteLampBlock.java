package com.drakmyth.minecraft.manufactory.blocks;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class MechaniteLampBlock extends Block {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;

    private MechaniteLampBlock(BlockBehaviour.Properties props, boolean defaultLit) {
        super(props);
        BlockState defaultState = this.stateDefinition.any()
                .setValue(LIT, defaultLit)
                .setValue(INVERTED, defaultLit);
        this.registerDefaultState(defaultState);
    }

    public static MechaniteLampBlock LitBlock(BlockBehaviour.Properties props) {
        return new MechaniteLampBlock(props, true);
    }

    public static MechaniteLampBlock UnlitBlock(BlockBehaviour.Properties props) {
        return new MechaniteLampBlock(props, false);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean hasNeighborSignal = context.getLevel().hasNeighborSignal(context.getClickedPos());
        BlockState state = this.defaultBlockState();
        return state.setValue(LIT, hasNeighborSignal ^ state.getValue(INVERTED));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.isClientSide) return;

        boolean isLit = state.getValue(LIT);
        if (isLit != (level.hasNeighborSignal(pos) ^ state.getValue(INVERTED))) {
            level.setBlock(pos, state.cycle(LIT), 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, INVERTED);
    }
}
