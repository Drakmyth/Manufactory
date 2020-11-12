/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.drakmyth.minecraft.TestUtils;
import com.drakmyth.minecraft.manufactory.tileentities.BallMillTileEntity;

import org.junit.jupiter.api.Test;

import net.minecraft.block.BlockState;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BallMillBlockTests {
    @Test
    public void testHorizontalFacingDefaultsToNorth() {
        // Arrange
        DirectionProperty property = BallMillBlock.HORIZONTAL_FACING;

        // Act
        BallMillBlock block = new BallMillBlock(defaultProperties());

        // Assert
        assertEquals(Direction.NORTH, block.getDefaultState().get(property));
    }

    @Test
    public void testCreatesAssociatedTileEntity() {
        // Arrange
        BallMillBlock block = new BallMillBlock(defaultProperties());
        BlockState state = block.getDefaultState();
        World world = TestUtils.getWorld();

        // Act
        boolean hasTileEntity = block.hasTileEntity(state);
        TileEntity tileEntity = block.createTileEntity(state, world);

        // Assert
        assertTrue(hasTileEntity);
        assertTrue(tileEntity instanceof BallMillTileEntity);
    }

    @Test
    public void testCanConnectToFaceReturnsFalseForAllNonBackFaces() {
        // Arrange
        BallMillBlock block = new BallMillBlock(defaultProperties());
        // North is default, but let's be explicit for the test
        Direction facing = Direction.NORTH;
        BlockState state = block.getDefaultState().with(BallMillBlock.HORIZONTAL_FACING, facing);
        BlockPos pos = new BlockPos(0, 0, 0);
        List<Direction> nonBackFaces = new ArrayList<>(Arrays.asList(Direction.values()));
        nonBackFaces.remove(facing.getOpposite());
        World world = TestUtils.getWorld();
        Map<Direction, Boolean> results = new HashMap<>();

        // Act
        for (Direction dir : nonBackFaces) {
            boolean result = block.canConnectToFace(state, pos, world, dir);
            results.put(dir, result);
        }

        // Assert
        for (Entry<Direction, Boolean> entry : results.entrySet()) {
            assertNotEquals(facing.getOpposite(), entry.getKey());
            assertFalse(entry.getValue());
        }
    }

    private Properties defaultProperties() {
        return Properties.create(Material.ROCK);
    }
}
