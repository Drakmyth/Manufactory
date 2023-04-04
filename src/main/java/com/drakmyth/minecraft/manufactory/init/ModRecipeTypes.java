package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.recipes.BallMillRecipe;
import com.drakmyth.minecraft.manufactory.recipes.GrinderRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Reference.MOD_ID);

    public static final RegistryObject<RecipeType<GrinderRecipe>> GRINDER =
            RECIPE_TYPES.register("grinder", () -> RecipeType.simple(new ResourceLocation(Reference.MOD_ID, "grinder")));
    public static final RegistryObject<RecipeType<BallMillRecipe>> BALL_MILL =
            RECIPE_TYPES.register("ball_mill", () -> RecipeType.simple(new ResourceLocation(Reference.MOD_ID, "ball_mill")));
}
