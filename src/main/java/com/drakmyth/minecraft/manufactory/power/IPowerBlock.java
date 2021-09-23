/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.power;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

public interface IPowerBlock {
    boolean canConnectToFace(BlockState state, BlockPos pos, LevelAccessor world, Direction dir);
    Type getPowerBlockType();
    float getAvailablePower(BlockState state, Level world, BlockPos pos);

    public static enum Type {
        NONE,
        SOURCE,
        SINK
    }
}
