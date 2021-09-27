/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.recipes;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.init.ModRecipeSerializers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;

public class BallMillRecipe extends ManufactoryRecipe {

    public static final RecipeType<BallMillRecipe> recipeType = RecipeType.register(String.format("%s:ball_mill", Reference.MOD_ID));

    public BallMillRecipe(ResourceLocation recipeId, Ingredient ingredient, ItemStack result, float extraChance, int[] extraAmounts, int tierRequired, int powerRequired, int processTime) {
        super(recipeId, ingredient, result, extraChance, extraAmounts, tierRequired, powerRequired, processTime);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.GRINDER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return recipeType;
    }
}
