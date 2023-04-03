/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.recipes.GrinderRecipe;
import com.drakmyth.minecraft.manufactory.recipes.BallMillRecipe;
import com.drakmyth.minecraft.manufactory.recipes.ManufactoryRecipeSerializer;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> GRINDER = RECIPE_SERIALIZERS.register("grinder", () -> new ManufactoryRecipeSerializer<GrinderRecipe>(GrinderRecipe::new));
    public static final RegistryObject<RecipeSerializer<?>> BALL_MILL = RECIPE_SERIALIZERS.register("ball_mill", () -> new ManufactoryRecipeSerializer<BallMillRecipe>(BallMillRecipe::new));
}
