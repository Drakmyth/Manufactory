/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;

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

    private Properties defaultProperties() {
        return Properties.create(Material.ROCK);
    }
}
