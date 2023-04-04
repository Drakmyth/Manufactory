package com.drakmyth.minecraft.manufactory.power;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

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
