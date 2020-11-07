/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.tileentities.GrinderTileEntity;
import com.drakmyth.minecraft.manufactory.tileentities.BallMillTileEntity;
import com.drakmyth.minecraft.manufactory.tileentities.LatexCollectorTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModTileEntityTypes {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Reference.MOD_ID);

    public static final RegistryObject<TileEntityType<GrinderTileEntity>> GRINDER = TILE_ENTITY_TYPES.register("grinder", () -> TileEntityType.Builder.create(GrinderTileEntity::new, ModBlocks.GRINDER.get()).build(null));
    public static final RegistryObject<TileEntityType<BallMillTileEntity>> BALL_MILL = TILE_ENTITY_TYPES.register("ball_mill", () -> TileEntityType.Builder.create(BallMillTileEntity::new, ModBlocks.BALL_MILL.get()).build(null));
    public static final RegistryObject<TileEntityType<LatexCollectorTileEntity>> LATEX_COLLECTOR = TILE_ENTITY_TYPES.register("latex_collector", () -> TileEntityType.Builder.create(LatexCollectorTileEntity::new, ModBlocks.LATEX_COLLECTOR.get()).build(null));
}
