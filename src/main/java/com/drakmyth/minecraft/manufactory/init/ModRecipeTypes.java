package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.recipes.BallMillRecipe;
import com.drakmyth.minecraft.manufactory.recipes.GrinderRecipe;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Reference.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<GrinderRecipe>> GRINDER =
            RECIPE_TYPES.register("grinder", () -> RecipeType.simple(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "grinder")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<BallMillRecipe>> BALL_MILL =
            RECIPE_TYPES.register("ball_mill", () -> RecipeType.simple(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "ball_mill")));
}
