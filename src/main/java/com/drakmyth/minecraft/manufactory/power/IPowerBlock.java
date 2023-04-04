package com.drakmyth.minecraft.manufactory.power;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

public interface IPowerBlock {
    boolean canConnectToFace(BlockState state, BlockPos pos, LevelAccessor level, Direction dir);

    Type getPowerBlockType();

    float getAvailablePower(BlockState state, Level level, BlockPos pos);

    public static enum Type {
        // @formatter:off
        NONE,
        SOURCE,
        SINK
        // @formatter:on
    }
}
