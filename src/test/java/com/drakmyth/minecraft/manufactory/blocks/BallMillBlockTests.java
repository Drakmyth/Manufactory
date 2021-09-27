// /*
//  *  SPDX-License-Identifier: LGPL-3.0-only
//  *  Copyright (c) 2020 Drakmyth. All rights reserved.
//  */

// package com.drakmyth.minecraft.manufactory.blocks;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertNotEquals;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Map.Entry;

// import com.drakmyth.minecraft.TestUtils;
// import com.drakmyth.minecraft.manufactory.tileentities.BallMillTileEntity;

// import org.junit.jupiter.api.Test;

// import net.minecraft.world.level.block.state.BlockState;
// import net.minecraft.block.AbstractBlock.Properties;
// import net.minecraft.block.material.Material;
// import net.minecraft.world.entity.player.Player;
// import net.minecraft.world.item.context.BlockPlaceContext;
// import net.minecraft.world.item.ItemStack;
// import net.minecraft.world.level.block.state.properties.DirectionProperty;
// import net.minecraft.world.level.block.entity.BlockEntity;
// import net.minecraft.core.Direction;
// import net.minecraft.world.InteractionHand;
// import net.minecraft.core.BlockPos;
// import net.minecraft.world.phys.BlockHitResult;
// import net.minecraft.util.math.vector.Vector3d;
// import net.minecraft.world.level.Level;

// public class BallMillBlockTests {

//     public static class Constructor extends BallMillBlockTests {
//         @Test
//         public void horizontalFacingDefaultsToNorth() {
//             // Arrange
//             DirectionProperty property = BallMillBlock.HORIZONTAL_FACING;

//             // Act
//             BallMillBlock block = new BallMillBlock(defaultProperties());
//             Direction facing = block.getDefaultState().get(property);

//             // Assert
//             assertEquals(Direction.NORTH, facing);
//         }
//     }

//     public static class HasTileEntity extends BallMillBlockTests {
//         @Test
//         public void returnsTrue() {
//             // Arrange
//             BallMillBlock block = new BallMillBlock(defaultProperties());
//             BlockState state = block.getDefaultState();

//             // Act
//             boolean hasTileEntity = block.hasTileEntity(state);

//             // Assert
//             assertTrue(hasTileEntity);
//         }
//     }

//     public static class CreateTileEntity extends BallMillBlockTests {
//         @Test
//         public void createsBallMillTileEntity() {
//             // Arrange
//             BallMillBlock block = new BallMillBlock(defaultProperties());
//             BlockState state = block.getDefaultState();
//             World world = TestUtils.getWorld();

//             // Act
//             TileEntity tileEntity = block.createTileEntity(state, world);

//             // Assert
//             assertTrue(tileEntity instanceof BallMillTileEntity);
//         }
//     }

//     public static class CanConnectToFace extends BallMillBlockTests {

//         @Test
//         public void returnsFalseIfTileEntityDoesNotExist() {
//             // Arrange
//             BallMillBlock block = new BallMillBlock(defaultProperties());
//             BlockState state = block.getDefaultState();
//             BlockPos pos = new BlockPos(0, 0, 0);
//             World world = TestUtils.getWorld();
//             Direction dir = Direction.NORTH;

//             // Act
//             boolean canConnectToFace = block.canConnectToFace(state, pos, world, dir);

//             // Assert
//             assertFalse(canConnectToFace);
//         }

//         @Test
//         public void returnsFalseIfTileEntityWrongType() {
//             // Arrange
//             BallMillBlock block = new BallMillBlock(defaultProperties());
//             GrinderBlock grinder = new GrinderBlock(defaultProperties());
//             BlockState state = block.getDefaultState();
//             BlockState grinderState = grinder.getDefaultState();
//             BlockPos pos = new BlockPos(0, 0, 0);
//             World world = TestUtils.getWorld();
//             world.setBlockState(pos, grinderState);
//             Direction dir = Direction.NORTH;

//             // Act
//             boolean canConnectToFace = block.canConnectToFace(state, pos, world, dir);

//             // Assert
//             assertFalse(canConnectToFace);
//         }

//         @Test
//         public void returnsFalseForAllNonBackFaces() {
//             // Arrange
//             BallMillBlock block = new BallMillBlock(defaultProperties());
//             // North is default, but let's be explicit for the test
//             Direction facing = Direction.NORTH;
//             BlockState state = block.getDefaultState().with(BallMillBlock.HORIZONTAL_FACING, facing);
//             BlockPos pos = new BlockPos(0, 0, 0);
//             World world = TestUtils.getWorld();
//             world.setBlockState(pos, state);
//             List<Direction> nonBackFaces = new ArrayList<>(Arrays.asList(Direction.values()));
//             nonBackFaces.remove(facing.getOpposite());
//             Map<Direction, Boolean> results = new HashMap<>();

//             // Act
//             for (Direction dir : nonBackFaces) {
//                 boolean canConnectToFace = block.canConnectToFace(state, pos, world, dir);
//                 results.put(dir, canConnectToFace);
//             }

//             // Assert
//             for (Entry<Direction, Boolean> entry : results.entrySet()) {
//                 assertNotEquals(facing.getOpposite(), entry.getKey());
//                 assertFalse(entry.getValue());
//             }
//         }

//         @Test
//         public void returnsFalseIfNoPowerUpgradeInstalled() {
//             // Arrange
//             BallMillBlock block = new BallMillBlock(defaultProperties());
//             // North is default, but let's be explicit for the test
//             Direction facing = Direction.NORTH;
//             BlockState state = block.getDefaultState().with(BallMillBlock.HORIZONTAL_FACING, facing);
//             BlockPos pos = new BlockPos(0, 0, 0);
//             World world = TestUtils.getWorld();
//             world.setBlockState(pos, state);

//             // Act
//             boolean canConnectToFace = block.canConnectToFace(state, pos, world, facing.getOpposite());

//             // Assert
//             assertFalse(canConnectToFace);
//         }
//     }

//     public static class GetStateForPlacement extends BallMillBlockTests {
//         @Test
//         public void returnsStateWithOppositeFacing() {
//             // Arrange
//             BallMillBlock block = new BallMillBlock(defaultProperties());
//             Direction playerFacing = Direction.EAST;
//             PlayerEntity player = TestUtils.getPlayer();
//             player.setPositionAndRotation(0, 0, 0, playerFacing.getHorizontalAngle(), 0);
//             Hand hand = player.getActiveHand();
//             ItemStack stack = player.getHeldItem(hand);

//             Vector3d hitVec = new Vector3d(0, 0, 0);
//             BlockPos pos = new BlockPos(0, 0, 0);
//             BlockRayTraceResult raytrace = new BlockRayTraceResult(hitVec, playerFacing.getOpposite(), pos, false);
//             BlockItemUseContext context = new BlockItemUseContext(player, hand, stack, raytrace);

//             // Act
//             BlockState state = block.getStateForPlacement(context);
//             Direction facing = state.get(BallMillBlock.HORIZONTAL_FACING);

//             // Assert
//             assertEquals(playerFacing.getOpposite(), facing);
//         }
//     }

//     protected Properties defaultProperties() {
//         return Properties.create(Material.ROCK);
//     }
// }
