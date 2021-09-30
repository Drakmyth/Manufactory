/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.world.level.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.RegistryObject;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(
            Pair.of(ModBlockLoot::new, LootContextParamSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationContext) {
        // TODO: This seems to be here to validate that loot tables are not modified programmatically. Do we need this?
    }

    public static class ModBlockLoot extends BlockLoot {
        @Override
        protected void addTables() {
            dropSelf(ModBlocks.AMBER_BLOCK.get());
            dropSelf(ModBlocks.GRINDER.get());
            dropSelf(ModBlocks.BALL_MILL.get());
            dropSelf(ModBlocks.LATEX_COLLECTOR.get());
            dropSelf(ModBlocks.POWER_CABLE.get());
            dropSelf(ModBlocks.SOLAR_PANEL.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
        }
    }
}
