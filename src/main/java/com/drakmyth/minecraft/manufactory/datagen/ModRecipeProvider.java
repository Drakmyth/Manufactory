/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen;

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
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

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

        // Coagulated Latex -> 5 Rubber
        ManufactoryRecipeBuilder.grinderRecipe(Ingredient.fromItems(ModItems.COAGULATED_LATEX.get()), ModItems.RUBBER.get(), 5, 0.25f, new float[]{4, 5, 6, 7, 8}, 25, 200)
            .addCriterion("has_coagulated_latex", InventoryChangeTrigger.Instance.forItems(ModItems.COAGULATED_LATEX.get()))
            .build(consumer, "manufactory:rubber_from_grinder");
    }
}
