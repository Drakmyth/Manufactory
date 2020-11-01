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
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.RegistryObject;

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

        List<Tuple<Tuple<String, Item>, RegistryObject<Item>>> ores = Arrays.asList(
            new Tuple<>(new Tuple<>("coal_ore", Items.COAL_ORE), ModItems.GROUND_COAL_ORE_ROUGH),
            new Tuple<>(new Tuple<>("diamond_ore", Items.DIAMOND_ORE), ModItems.GROUND_DIAMOND_ORE_ROUGH),
            new Tuple<>(new Tuple<>("emerald_ore", Items.EMERALD_ORE), ModItems.GROUND_EMERALD_ORE_ROUGH),
            new Tuple<>(new Tuple<>("gold_ore", Items.GOLD_ORE), ModItems.GROUND_GOLD_ORE_ROUGH),
            new Tuple<>(new Tuple<>("iron_ore", Items.IRON_ORE), ModItems.GROUND_IRON_ORE_ROUGH),
            new Tuple<>(new Tuple<>("lapis_ore", Items.LAPIS_ORE), ModItems.GROUND_LAPIS_ORE_ROUGH),
            new Tuple<>(new Tuple<>("nether_quartz_ore", Items.NETHER_QUARTZ_ORE), ModItems.GROUND_NETHER_QUARTZ_ORE_ROUGH),
            new Tuple<>(new Tuple<>("redstone_ore", Items.REDSTONE_ORE), ModItems.GROUND_REDSTONE_ORE_ROUGH),
            new Tuple<>(new Tuple<>("ancient_debris", Items.ANCIENT_DEBRIS), ModItems.GROUND_ANCIENT_DEBRIS_ROUGH)
            );

        // Ore -> Ground Ore (Rough)
        ores.stream().forEach(tuple -> {
            ManufactoryRecipeBuilder.grinderRecipe(Ingredient.fromItems(tuple.getA().getB()), tuple.getB().get())
            .withExtraChance(0.3f, 1)
            .addCriterion(String.format("has_%s", tuple.getA().getA()), InventoryChangeTrigger.Instance.forItems(tuple.getA().getB()))
            .build(consumer);
        });
    }
}
