/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */
package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModFluids;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import com.drakmyth.minecraft.manufactory.init.ModTags;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModTagsProvider {
    public static class Blocks extends BlockTagsProvider {
        public Blocks(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
            super(generatorIn, Reference.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.AMBER_BLOCK.get())
                                                .add(ModBlocks.GRINDER.get())
                                                .add(ModBlocks.BALL_MILL.get())
                                                .add(ModBlocks.SOLAR_PANEL.get());
            
            tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.LATEX_COLLECTOR.get());

            tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(ModBlocks.AMBER_BLOCK.get())
                                            .add(ModBlocks.GRINDER.get())
                                            .add(ModBlocks.BALL_MILL.get())
                                            .add(ModBlocks.SOLAR_PANEL.get())
                                            .add(ModBlocks.LATEX_COLLECTOR.get());
            
            tag(ModTags.Blocks.BLOCKS_WITH_LATEX).addTag(BlockTags.LOGS);
        }
    }

    public static class Items extends ItemTagsProvider {
        public Items(DataGenerator generatorIn, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
            super(generatorIn, blockTagsProvider, Reference.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            tag(ModTags.Items.UPGRADE_ACCESS_TOOL).add(ModItems.WRENCH.get());
        }
    }

    public static class Fluids extends FluidTagsProvider {
        public Fluids(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
            super(generatorIn, Reference.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            tag(FluidTags.WATER).add(ModFluids.SLURRIED_COAL_ORE.get())
                                .add(ModFluids.SLURRIED_COAL_ORE_FLOWING.get())
                                .add(ModFluids.SLURRIED_DIAMOND_ORE.get())
                                .add(ModFluids.SLURRIED_DIAMOND_ORE_FLOWING.get())
                                .add(ModFluids.SLURRIED_EMERALD_ORE.get())
                                .add(ModFluids.SLURRIED_EMERALD_ORE_FLOWING.get())
                                .add(ModFluids.SLURRIED_GOLD_ORE.get())
                                .add(ModFluids.SLURRIED_GOLD_ORE_FLOWING.get())
                                .add(ModFluids.SLURRIED_IRON_ORE.get())
                                .add(ModFluids.SLURRIED_IRON_ORE_FLOWING.get())
                                .add(ModFluids.SLURRIED_LAPIS_ORE.get())
                                .add(ModFluids.SLURRIED_LAPIS_ORE_FLOWING.get())
                                .add(ModFluids.SLURRIED_NETHER_QUARTZ_ORE.get())
                                .add(ModFluids.SLURRIED_NETHER_QUARTZ_ORE_FLOWING.get())
                                .add(ModFluids.SLURRIED_REDSTONE_ORE.get())
                                .add(ModFluids.SLURRIED_REDSTONE_ORE_FLOWING.get())
                                .add(ModFluids.SLURRIED_ANCIENT_DEBRIS.get())
                                .add(ModFluids.SLURRIED_ANCIENT_DEBRIS_FLOWING.get());

        }
    }
}
