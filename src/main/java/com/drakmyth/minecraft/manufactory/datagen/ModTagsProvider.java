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
            getOrCreateBuilder(FluidTags.WATER).add(ModFluids.SLURRIED_COAL_ORE.get());
            getOrCreateBuilder(FluidTags.WATER).add(ModFluids.SLURRIED_COAL_ORE_FLOWING.get());
        }
    }

}
