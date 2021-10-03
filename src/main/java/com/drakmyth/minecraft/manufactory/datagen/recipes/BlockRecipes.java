/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen.recipes;

import java.util.function.Consumer;

import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModItems;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.tags.ItemTags;

public final class BlockRecipes extends RecipeProvider {

    private BlockRecipes(DataGenerator generator) {
        super(generator);
    }

    public static void build(Consumer<FinishedRecipe> consumer) {

        // Amber Block = 9 Amber
        ShapedRecipeBuilder.shaped(ModBlocks.AMBER_BLOCK.get())
            .pattern("aaa")
            .pattern("aaa")
            .pattern("aaa")
            .define('a', ModItems.AMBER.get())
            .unlockedBy("has_amber", has(ModItems.AMBER.get()))
            .save(consumer);

        // Solar Panel = 3 Daylight Detector + 2 Nether Quartz + Redstone Dust + 2 Wooden Slab
        ShapedRecipeBuilder.shaped(ModBlocks.SOLAR_PANEL.get())
            .pattern("ddd")
            .pattern("qrq")
            .pattern("wpw")
            .define('d', Items.DAYLIGHT_DETECTOR)
            .define('q', Items.QUARTZ)
            .define('r', Items.REDSTONE)
            .define('w', ItemTags.WOODEN_SLABS)
            .define('p', ModBlocks.POWER_CABLE.get())
            .unlockedBy("has_daylight_detector", has(Items.DAYLIGHT_DETECTOR))
            .save(consumer);

        // Grinder = 2 Coupling + 2 Redstone Wire + 2 Stone + Power Cable
        ShapedRecipeBuilder.shaped(ModBlocks.GRINDER.get())
            .pattern("c c")
            .pattern("w w")
            .pattern("sps")
            .define('c', ModItems.COUPLING.get())
            .define('w', ModItems.REDSTONE_WIRE.get())
            .define('p', ModBlocks.POWER_CABLE.get())
            .define('s', Items.STONE)
            .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
            .save(consumer);

        // Ball Mill = 1 Coupling + 3 Redstone Wire + 4 Stone + Power Cable
        ShapedRecipeBuilder.shaped(ModBlocks.BALL_MILL.get())
            .pattern("sws")
            .pattern("wcw")
            .pattern("sps")
            .define('c', ModItems.COUPLING.get())
            .define('w', ModItems.REDSTONE_WIRE.get())
            .define('p', ModBlocks.POWER_CABLE.get())
            .define('s', Items.STONE)
            .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
            .save(consumer);
    }
}
