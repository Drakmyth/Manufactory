/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import com.drakmyth.minecraft.manufactory.recipes.ManufactoryRecipeBuilder;

import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {

        // Amber Block = 9 Amber
        ShapedRecipeBuilder.shaped(ModBlocks.AMBER_BLOCK.get())
            .pattern("aaa")
            .pattern("aaa")
            .pattern("aaa")
            .define('a', ModItems.AMBER.get())
            .unlockedBy("has_amber", has(ModItems.AMBER.get()))
            .save(consumer);

        // 9 Amber = Amber Block
        ShapelessRecipeBuilder.shapeless(ModItems.AMBER.get(), 9)
            .requires(ModBlocks.AMBER_BLOCK.get())
            .unlockedBy("has_amber_block", has(ModBlocks.AMBER_BLOCK.get()))
            .save(consumer);

        // Latex Collector = String + Bowl
        ShapelessRecipeBuilder.shapeless(ModBlocks.LATEX_COLLECTOR.get())
            .requires(Items.STRING)
            .requires(Items.BOWL)
            .unlockedBy("has_bowl", has(Items.BOWL))
            .save(consumer);

        // Tapping Knife = 2 Iron Ingot + Stick
        ShapedRecipeBuilder.shaped(ModItems.TAPPING_KNIFE.get())
            .pattern("ii ")
            .pattern("  s")
            .define('i', Items.IRON_INGOT)
            .define('s', Items.STICK)
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .save(consumer);

        // Rubber = Coagulated Latex
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.COAGULATED_LATEX.get()), ModItems.RUBBER.get(), 0.1f, 200)
            .unlockedBy("has_coagulated_latex", has(ModItems.COAGULATED_LATEX.get()))
            .save(consumer);

        // 3 Redstone Wire = 3 String + Redstone Dust
        ShapelessRecipeBuilder.shapeless(ModItems.REDSTONE_WIRE.get(), 3)
            .requires(Items.REDSTONE)
            .requires(Items.STRING, 3)
            .unlockedBy("has_string", has(Items.STRING))
            .save(consumer);

        // Coupling = 4 Iron Ingot
        ShapedRecipeBuilder.shaped(ModItems.COUPLING.get())
            .pattern(" i ")
            .pattern("i i")
            .pattern(" i ")
            .define('i', Items.IRON_INGOT)
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .save(consumer);

        // Wrench = 4 Iron Ingot
        ShapedRecipeBuilder.shaped(ModItems.WRENCH.get())
            .pattern("i i")
            .pattern(" i ")
            .pattern(" i ")
            .define('i', Items.IRON_INGOT)
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .save(consumer);

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

        // 3 Power Cable = 3 Rubber + 3 Redstone Wire
        ShapedRecipeBuilder.shaped(ModBlocks.POWER_CABLE.get(), 3)
            .pattern("rrr")
            .pattern("www")
            .define('r', ModItems.RUBBER.get())
            .define('w', ModItems.REDSTONE_WIRE.get())
            .unlockedBy("has_rubber", has(ModItems.RUBBER.get()))
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

        List<OreProcessingRecipeData> grinderOres = Arrays.asList(
            new OreProcessingRecipeData(Items.COAL_ORE, Tiers.WOOD, ModItems.GROUND_COAL_ORE_ROUGH.get(), Items.COAL),
            new OreProcessingRecipeData(Items.DIAMOND_ORE, Tiers.IRON, ModItems.GROUND_DIAMOND_ORE_ROUGH.get(), Items.DIAMOND),
            new OreProcessingRecipeData(Items.EMERALD_ORE, Tiers.IRON, ModItems.GROUND_EMERALD_ORE_ROUGH.get(), Items.EMERALD),
            new OreProcessingRecipeData(Items.GOLD_ORE, Tiers.IRON, ModItems.GROUND_GOLD_ORE_ROUGH.get(), Items.GOLD_INGOT),
            new OreProcessingRecipeData(Items.IRON_ORE, Tiers.STONE, ModItems.GROUND_IRON_ORE_ROUGH.get(), Items.IRON_INGOT),
            new OreProcessingRecipeData(Items.LAPIS_ORE, Tiers.STONE, ModItems.GROUND_LAPIS_ORE_ROUGH.get(), Items.LAPIS_LAZULI),
            new OreProcessingRecipeData(Items.NETHER_QUARTZ_ORE, Tiers.WOOD, ModItems.GROUND_NETHER_QUARTZ_ORE_ROUGH.get(), Items.QUARTZ),
            new OreProcessingRecipeData(Items.REDSTONE_ORE, Tiers.IRON, ModItems.GROUND_REDSTONE_ORE_ROUGH.get(), Items.REDSTONE),
            new OreProcessingRecipeData(Items.ANCIENT_DEBRIS, Tiers.DIAMOND, ModItems.GROUND_ANCIENT_DEBRIS_ROUGH.get(), Items.NETHERITE_SCRAP)
            );

        grinderOres.stream().forEach(data -> {
            String inputName = ForgeRegistries.ITEMS.getKey(data.getInput()).getPath();
            String outputName = ForgeRegistries.ITEMS.getKey(data.getOutput()).getPath();
            String processedName = ForgeRegistries.ITEMS.getKey(data.getProcessed()).getPath();

            // Ore -> Ground Ore (Rough)
            ManufactoryRecipeBuilder.grinderRecipe(Ingredient.of(data.getInput()), data.getOutput())
                .withExtraChance(0.3f, data.getExtraAmounts())
                .withTierRequired(data.getTier())
                .addCriterion(String.format("has_%s", inputName), has(data.getInput()))
                .build(consumer);

            // Ground Ore (Rough) -> Ingot
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(data.getOutput()), data.getProcessed(), 0.2f, 200)
                .unlockedBy(String.format("has_%s", outputName), has(data.getOutput()))
                .save(consumer, String.format("%s:%s_from_ground_ore_rough", Reference.MOD_ID, processedName));
        });

        // Nether Gold Ore -> Ground Gold Ore (Rough)
        ManufactoryRecipeBuilder.grinderRecipe(Ingredient.of(Items.NETHER_GOLD_ORE), ModItems.GROUND_GOLD_ORE_ROUGH.get())
            .withExtraChance(0.3f, 1)
            .withTierRequired(Tiers.WOOD)
            .addCriterion("has_nether_gold_ore", has(Items.NETHER_GOLD_ORE))
            .build(consumer, String.format("%s:ground_gold_ore_rough_from_nether_gold_ore", Reference.MOD_ID));

        List<OreProcessingRecipeData> ballMillOres = Arrays.asList(
            new OreProcessingRecipeData(ModItems.GROUND_COAL_ORE_ROUGH.get(), Tiers.WOOD, ModItems.GROUND_COAL_ORE_FINE.get(), Items.COAL),
            new OreProcessingRecipeData(ModItems.GROUND_DIAMOND_ORE_ROUGH.get(), Tiers.IRON, ModItems.GROUND_DIAMOND_ORE_FINE.get(), Items.DIAMOND),
            new OreProcessingRecipeData(ModItems.GROUND_EMERALD_ORE_ROUGH.get(), Tiers.IRON, ModItems.GROUND_EMERALD_ORE_FINE.get(), Items.EMERALD),
            new OreProcessingRecipeData(ModItems.GROUND_GOLD_ORE_ROUGH.get(), Tiers.IRON, ModItems.GROUND_GOLD_ORE_FINE.get(), Items.GOLD_INGOT),
            new OreProcessingRecipeData(ModItems.GROUND_IRON_ORE_ROUGH.get(), Tiers.STONE, ModItems.GROUND_IRON_ORE_FINE.get(), Items.IRON_INGOT),
            new OreProcessingRecipeData(ModItems.GROUND_LAPIS_ORE_ROUGH.get(), Tiers.STONE, ModItems.GROUND_LAPIS_ORE_FINE.get(), Items.LAPIS_LAZULI),
            new OreProcessingRecipeData(ModItems.GROUND_NETHER_QUARTZ_ORE_ROUGH.get(), Tiers.WOOD, ModItems.GROUND_NETHER_QUARTZ_ORE_FINE.get(), Items.QUARTZ),
            new OreProcessingRecipeData(ModItems.GROUND_REDSTONE_ORE_ROUGH.get(), Tiers.IRON, ModItems.GROUND_REDSTONE_ORE_FINE.get(), Items.REDSTONE),
            new OreProcessingRecipeData(ModItems.GROUND_ANCIENT_DEBRIS_ROUGH.get(), Tiers.DIAMOND, ModItems.GROUND_ANCIENT_DEBRIS_FINE.get(), Items.NETHERITE_SCRAP)
            );

        ballMillOres.stream().forEach(data -> {
            String inputName = ForgeRegistries.ITEMS.getKey(data.getInput()).getPath();
            String outputName = ForgeRegistries.ITEMS.getKey(data.getOutput()).getPath();
            String processedName = ForgeRegistries.ITEMS.getKey(data.getProcessed()).getPath();

            // Ground Ore (Rough) -> Ground Ore (Fine)
            ManufactoryRecipeBuilder.ballMillRecipe(Ingredient.of(data.getInput()), data.getOutput())
                .withExtraChance(0.54f, data.getExtraAmounts())
                .withTierRequired(data.getTier())
                .addCriterion(String.format("has_%s", inputName), has(data.getInput()))
                .build(consumer);

            // Ground Ore (Fine) -> Ingot
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(data.getOutput()), data.getProcessed(), 0.2f, 200)
                .unlockedBy(String.format("has_%s", outputName), has(data.getOutput()))
                .save(consumer, String.format("%s:%s_from_ground_ore_fine", Reference.MOD_ID, processedName));
        });
    }

    private static class OreProcessingRecipeData {
        private Item input;
        private Tier tier;
        private Item output;
        private Item processed;
        private int[] extraAmounts;

        public OreProcessingRecipeData(Item input, Tier tier, Item output, Item processed) {
            this(input, tier, output, processed, new int[]{1});
        }

        public OreProcessingRecipeData(Item input, Tier tier, Item output, Item processed, int[] extraAmounts) {
            this.input = input;
            this.tier = tier;
            this.output = output;
            this.processed = processed;
            this.extraAmounts = extraAmounts;
        }

        public Item getInput() {
            return input;
        }

        public Tier getTier() {
            return tier;
        }

        public Item getOutput() {
            return output;
        }

        public Item getProcessed() {
            return processed;
        }

        public int[] getExtraAmounts() {
            return extraAmounts;
        }
    }
}
