/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.power;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class PowerNetworkNode {
    private BlockPos position;
    private Direction[] directions;

    public PowerNetworkNode(BlockPos position, Direction[] directions) {
        this.position = position;
        this.directions = directions;
    }

    public BlockPos getPos() {
        return position;
    }

    public Direction[] getDirections() {
        return directions;
    }
}
