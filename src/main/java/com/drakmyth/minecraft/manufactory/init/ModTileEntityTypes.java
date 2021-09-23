/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.tileentities.GrinderTileEntity;
import com.drakmyth.minecraft.manufactory.tileentities.BallMillTileEntity;
import com.drakmyth.minecraft.manufactory.tileentities.LatexCollectorTileEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModTileEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Reference.MOD_ID);

    public static final RegistryObject<BlockEntityType<GrinderTileEntity>> GRINDER = TILE_ENTITY_TYPES.register("grinder", () -> BlockEntityType.Builder.of(GrinderTileEntity::new, ModBlocks.GRINDER.get()).build(null));
    public static final RegistryObject<BlockEntityType<BallMillTileEntity>> BALL_MILL = TILE_ENTITY_TYPES.register("ball_mill", () -> BlockEntityType.Builder.of(BallMillTileEntity::new, ModBlocks.BALL_MILL.get()).build(null));
    public static final RegistryObject<BlockEntityType<LatexCollectorTileEntity>> LATEX_COLLECTOR = TILE_ENTITY_TYPES.register("latex_collector", () -> BlockEntityType.Builder.of(LatexCollectorTileEntity::new, ModBlocks.LATEX_COLLECTOR.get()).build(null));
}
