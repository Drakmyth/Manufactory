package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.recipes.GrinderRecipe;
import com.drakmyth.minecraft.manufactory.recipes.BallMillRecipe;
import com.drakmyth.minecraft.manufactory.recipes.ManufactoryRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Reference.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<GrinderRecipe>> GRINDER =
            RECIPE_SERIALIZERS.register("grinder", () -> ManufactoryRecipeSerializer.create(GrinderRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BallMillRecipe>> BALL_MILL =
            RECIPE_SERIALIZERS.register("ball_mill", () -> ManufactoryRecipeSerializer.create(BallMillRecipe::new));
}
