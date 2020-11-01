/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import com.drakmyth.minecraft.manufactory.recipes.ManufactoryRecipeBuilder;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {

        // 9 Amber -> Amber Block
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.AMBER_BLOCK.get())
            .patternLine("aaa")
            .patternLine("aaa")
            .patternLine("aaa")
            .key('a', ModItems.AMBER.get())
            .addCriterion("has_amber", InventoryChangeTrigger.Instance.forItems(ModItems.AMBER.get()))
            .build(consumer);

        // Amber Block -> 9 Amber
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.AMBER.get(), 9)
            .addIngredient(ModBlocks.AMBER_BLOCK.get())
            .addCriterion("has_amber_block", InventoryChangeTrigger.Instance.forItems(ModBlocks.AMBER_BLOCK.get()))
            .build(consumer);

        // String + Bowl -> Latex Collector
        ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.LATEX_COLLECTOR.get())
            .addIngredient(Items.STRING)
            .addIngredient(Items.BOWL)
            .addCriterion("has_bowl", InventoryChangeTrigger.Instance.forItems(Items.BOWL))
            .build(consumer);

        // 2 Iron Ingot + Stick -> Tapping Knife
        ShapedRecipeBuilder.shapedRecipe(ModItems.TAPPING_KNIFE.get())
            .patternLine("ii ")
            .patternLine("  s")
            .key('i', Items.IRON_INGOT)
            .key('s', Items.STICK)
            .addCriterion("has_iron_ingot", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT))
            .build(consumer);

        // Coagulated Latex -> Rubber
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModItems.COAGULATED_LATEX.get()), ModItems.RUBBER.get(), 0.1f, 200)
            .addCriterion("has_coagulated_latex", InventoryChangeTrigger.Instance.forItems(ModItems.COAGULATED_LATEX.get()))
            .build(consumer);

        List<OreProcessingRecipeData> grinderOres = Arrays.asList(
            new OreProcessingRecipeData(Items.COAL_ORE, ModItems.GROUND_COAL_ORE_ROUGH.get(), Items.COAL),
            new OreProcessingRecipeData(Items.DIAMOND_ORE, ModItems.GROUND_DIAMOND_ORE_ROUGH.get(), Items.DIAMOND),
            new OreProcessingRecipeData(Items.EMERALD_ORE, ModItems.GROUND_EMERALD_ORE_ROUGH.get(), Items.EMERALD),
            new OreProcessingRecipeData(Items.GOLD_ORE, ModItems.GROUND_GOLD_ORE_ROUGH.get(), Items.GOLD_INGOT),
            new OreProcessingRecipeData(Items.IRON_ORE, ModItems.GROUND_IRON_ORE_ROUGH.get(), Items.IRON_INGOT),
            new OreProcessingRecipeData(Items.LAPIS_ORE, ModItems.GROUND_LAPIS_ORE_ROUGH.get(), Items.LAPIS_LAZULI),
            new OreProcessingRecipeData(Items.NETHER_QUARTZ_ORE, ModItems.GROUND_NETHER_QUARTZ_ORE_ROUGH.get(), Items.QUARTZ),
            new OreProcessingRecipeData(Items.REDSTONE_ORE, ModItems.GROUND_REDSTONE_ORE_ROUGH.get(), Items.REDSTONE),
            new OreProcessingRecipeData(Items.ANCIENT_DEBRIS, ModItems.GROUND_ANCIENT_DEBRIS_ROUGH.get(), Items.NETHERITE_SCRAP)
            );

        grinderOres.stream().forEach(data -> {
            String inputName = ForgeRegistries.ITEMS.getKey(data.getInput()).getPath();
            String outputName = ForgeRegistries.ITEMS.getKey(data.getOutput()).getPath();
            String processedName = ForgeRegistries.ITEMS.getKey(data.getProcessed()).getPath();
            // Ore -> Ground Ore (Rough)
            ManufactoryRecipeBuilder.grinderRecipe(Ingredient.fromItems(data.getInput()), data.getOutput())
            .withExtraChance(0.3f, 1)
            .addCriterion(String.format("has_%s", inputName), InventoryChangeTrigger.Instance.forItems(data.getInput()))
            .build(consumer);

            // Ground Ore (Rough) -> Ingot
            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(data.getOutput()), data.getProcessed(), 0.2f, 200)
            .addCriterion(String.format("has_%s", outputName), InventoryChangeTrigger.Instance.forItems(data.getOutput()))
            .build(consumer, String.format("manufactory:%s_from_ground_ore_rough", processedName));
        });
    }

    private static class OreProcessingRecipeData {
        private Item input;
        private Item output;
        private Item processed;

        public OreProcessingRecipeData(Item input, Item output, Item processed) {
            this.input = input;
            this.output = output;
            this.processed = processed;
        }

        public Item getInput() {
            return input;
        }

        public Item getOutput() {
            return output;
        }

        public Item getProcessed() {
            return processed;
        }
    }
}
