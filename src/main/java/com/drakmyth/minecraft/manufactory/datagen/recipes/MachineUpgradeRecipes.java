/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen.recipes;

import java.util.function.Consumer;

import com.drakmyth.minecraft.manufactory.init.ModItems;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.tags.ItemTags;

public final class MachineUpgradeRecipes extends RecipeProvider {
    private MachineUpgradeRecipes(DataGenerator generator) {
        super(generator);
    }

    public static void build(Consumer<FinishedRecipe> consumer) {
        buildGrinderWheelRecipes(consumer);
        buildMotorRecipes(consumer);
        buildMillingBallRecipes(consumer);
        buildPowerUpgradeRecipes(consumer);
        buildDrillHeadRecipes(consumer);
    }

    private static void buildGrinderWheelRecipes(Consumer<FinishedRecipe> consumer) {
        // Grinder Wheel Tier 0 = Coupling + 4 Planks
        ShapedRecipeBuilder.shaped(ModItems.GRINDER_WHEEL_TIER0.get())
            .pattern(" p ")
            .pattern("pcp")
            .pattern(" p ")
            .define('c', ModItems.COUPLING.get())
            .define('p', ItemTags.PLANKS)
            .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
            .save(consumer);

        // Grinder Wheel Tier 1 = Coupling + 4 Cobblestone
        ShapedRecipeBuilder.shaped(ModItems.GRINDER_WHEEL_TIER1.get())
            .pattern(" s ")
            .pattern("scs")
            .pattern(" s ")
            .define('c', ModItems.COUPLING.get())
            .define('s', ItemTags.STONE_CRAFTING_MATERIALS)
            .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
            .save(consumer);

        // Grinder Wheel Tier 2 = Coupling + 4 Iron Ingot
        ShapedRecipeBuilder.shaped(ModItems.GRINDER_WHEEL_TIER2.get())
            .pattern(" i ")
            .pattern("ici")
            .pattern(" i ")
            .define('c', ModItems.COUPLING.get())
            .define('i', Items.IRON_INGOT)
            .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
            .save(consumer);

        // Grinder Wheel Tier 3 = Coupling + 4 Diamond
        ShapedRecipeBuilder.shaped(ModItems.GRINDER_WHEEL_TIER3.get())
            .pattern(" d ")
            .pattern("dcd")
            .pattern(" d ")
            .define('c', ModItems.COUPLING.get())
            .define('d', Items.DIAMOND)
            .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
            .save(consumer);

        // Grinder Wheel Tier 4 = Coupling + 4 Netherite Ingot
        ShapedRecipeBuilder.shaped(ModItems.GRINDER_WHEEL_TIER4.get())
            .pattern(" n ")
            .pattern("ncn")
            .pattern(" n ")
            .define('c', ModItems.COUPLING.get())
            .define('n', Items.NETHERITE_INGOT)
            .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
            .save(consumer);
    }

    private static void buildMotorRecipes(Consumer<FinishedRecipe> consumer) {
        // Motor Tier 0 = Coupling + Redstone Dust + 3 Iron Ingot
        ShapedRecipeBuilder.shaped(ModItems.MOTOR_TIER0.get())
            .pattern(" i ")
            .pattern("cri")
            .pattern(" i ")
            .define('i', Items.IRON_INGOT)
            .define('c', ModItems.COUPLING.get())
            .define('r', Items.REDSTONE)
            .unlockedBy("has_redstone", has(Items.REDSTONE))
            .save(consumer);

        // Motor Tier 1 = Motor Tier 0 + Gold Ingot
        ShapelessRecipeBuilder.shapeless(ModItems.MOTOR_TIER1.get())
            .requires(ModItems.MOTOR_TIER0.get())
            .requires(Items.GOLD_INGOT)
            .unlockedBy("has_motor_tier_0", has(ModItems.MOTOR_TIER0.get()))
            .save(consumer);

        // Motor Tier 2 = Motor Tier 1 + Diamond
        ShapelessRecipeBuilder.shapeless(ModItems.MOTOR_TIER2.get())
            .requires(ModItems.MOTOR_TIER1.get())
            .requires(Items.DIAMOND)
            .unlockedBy("has_motor_tier_1", has(ModItems.MOTOR_TIER1.get()))
            .save(consumer);

        // Motor Tier 3 = Motor Tier 2 + Netherite Ingot
        ShapelessRecipeBuilder.shapeless(ModItems.MOTOR_TIER3.get())
            .requires(ModItems.MOTOR_TIER2.get())
            .requires(Items.NETHERITE_INGOT)
            .unlockedBy("has_motor_tier_2", has(ModItems.MOTOR_TIER2.get()))
            .save(consumer);
    }

    private static void buildMillingBallRecipes(Consumer<FinishedRecipe> consumer) {
        // 4 Milling Ball Tier 0 = 4 Planks
        ShapedRecipeBuilder.shaped(ModItems.MILLING_BALL_TIER0.get(), 4)
            .pattern(" p ")
            .pattern("p p")
            .pattern(" p ")
            .define('p', ItemTags.PLANKS)
            .unlockedBy("has_wood_planks", has(ItemTags.PLANKS))
            .save(consumer);

        // Milling Ball Tier 1 = Milling Ball Tier 0 + Cobblestone
        ShapelessRecipeBuilder.shapeless(ModItems.MILLING_BALL_TIER1.get())
            .requires(ModItems.MILLING_BALL_TIER0.get())
            .requires(Items.COBBLESTONE)
            .unlockedBy("has_milling_ball_tier0", has(ModItems.MILLING_BALL_TIER0.get()))
            .save(consumer);

        // Milling Ball Tier 2 = Milling Ball Tier 1 + Iron Ingot
        ShapelessRecipeBuilder.shapeless(ModItems.MILLING_BALL_TIER2.get())
            .requires(ModItems.MILLING_BALL_TIER1.get())
            .requires(Items.IRON_INGOT)
            .unlockedBy("has_milling_ball_tier1", has(ModItems.MILLING_BALL_TIER1.get()))
            .save(consumer);

        // Milling Ball Tier 3 = Milling Ball Tier 2 + Diamond
        ShapelessRecipeBuilder.shapeless(ModItems.MILLING_BALL_TIER3.get())
            .requires(ModItems.MILLING_BALL_TIER2.get())
            .requires(Items.DIAMOND)
            .unlockedBy("has_milling_ball_tier2", has(ModItems.MILLING_BALL_TIER2.get()))
            .save(consumer);

        // Milling Ball Tier 4 = Milling Ball Tier 3 + Netherite Ingot
        ShapelessRecipeBuilder.shapeless(ModItems.MILLING_BALL_TIER4.get())
            .requires(ModItems.MILLING_BALL_TIER3.get())
            .requires(Items.NETHERITE_INGOT)
            .unlockedBy("has_milling_ball_tier3", has(ModItems.MILLING_BALL_TIER3.get()))
            .save(consumer);
    }

    private static void buildPowerUpgradeRecipes(Consumer<FinishedRecipe> consumer) {
        // Battery = Redstone Wire + Nether Quartz + Redstone Dust
        ShapedRecipeBuilder.shaped(ModItems.BATTERY.get())
            .pattern("w")
            .pattern("n")
            .pattern("r")
            .define('w', ModItems.REDSTONE_WIRE.get())
            .define('n', Items.QUARTZ)
            .define('r', Items.REDSTONE)
            .unlockedBy("has_nether_quartz", has(Items.QUARTZ))
            .save(consumer);

        // Power Socket = 3 Redstone Wire + 6 Coagulated Latex
        ShapedRecipeBuilder.shaped(ModItems.POWER_SOCKET.get())
            .pattern("wlw")
            .pattern("lll")
            .pattern("lwl")
            .define('w', ModItems.REDSTONE_WIRE.get())
            .define('l', ModItems.COAGULATED_LATEX.get())
            .unlockedBy("has_redstone_wire", has(ModItems.REDSTONE_WIRE.get()))
            .save(consumer);
    }

    private static void buildDrillHeadRecipes(Consumer<FinishedRecipe> consumer) {
        // Drill Head Tier 0 = 5 Iron Ingot
        ShapedRecipeBuilder.shaped(ModItems.DRILL_HEAD_TIER0.get())
            .pattern("i  ")
            .pattern("iii")
            .pattern("i  ")
            .define('i', Items.IRON_INGOT)
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .save(consumer);

        // Drill Head Tier 1 = Drill Head Tier 0 + Cobblestone
        ShapelessRecipeBuilder.shapeless(ModItems.DRILL_HEAD_TIER1.get())
            .requires(ModItems.DRILL_HEAD_TIER0.get())
            .requires(Items.COBBLESTONE)
            .unlockedBy("has_drill_head_tier0", has(ModItems.DRILL_HEAD_TIER0.get()))
            .save(consumer);
        
        // Drill Head Tier 2 = Drill Head Tier 1 + Iron Ingot
        ShapelessRecipeBuilder.shapeless(ModItems.DRILL_HEAD_TIER2.get())
            .requires(ModItems.DRILL_HEAD_TIER1.get())
            .requires(Items.IRON_INGOT)
            .unlockedBy("has_drill_head_tier0", has(ModItems.DRILL_HEAD_TIER1.get()))
            .save(consumer);

        // Drill Head Tier 3 = Drill Head Tier 2 + Diamond
        ShapelessRecipeBuilder.shapeless(ModItems.DRILL_HEAD_TIER3.get())
            .requires(ModItems.DRILL_HEAD_TIER2.get())
            .requires(Items.DIAMOND)
            .unlockedBy("has_drill_head_tier0", has(ModItems.DRILL_HEAD_TIER2.get()))
            .save(consumer);

        // Drill Head Tier 4 = Drill Head Tier 3 + Netherite Ingot
        ShapelessRecipeBuilder.shapeless(ModItems.DRILL_HEAD_TIER4.get())
            .requires(ModItems.DRILL_HEAD_TIER3.get())
            .requires(Items.NETHERITE_INGOT)
            .unlockedBy("has_drill_head_tier0", has(ModItems.DRILL_HEAD_TIER3.get()))
            .save(consumer);
    }
}
