/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */
package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.init.ModFluids;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModTagsProvider {

    public static class Fluids extends FluidTagsProvider {
        public Fluids(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
            super(generatorIn, Reference.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerTags() {
            getOrCreateBuilder(FluidTags.WATER).add(ModFluids.SLURRIED_COAL_ORE.get())
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
