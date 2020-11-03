/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.power;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

public interface IPowerBlock {
    boolean canConnectToFace(BlockState state, Direction dir);
    Type getPowerBlockType();
    float getAvailablePower();

    public static enum Type {
        NONE,
        SOURCE,
        SINK
    }
}
