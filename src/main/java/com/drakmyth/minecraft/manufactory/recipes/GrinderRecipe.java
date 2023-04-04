package com.drakmyth.minecraft.manufactory.recipes;

import com.drakmyth.minecraft.manufactory.init.ModRecipeSerializers;
import com.drakmyth.minecraft.manufactory.init.ModRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;

public class GrinderRecipe extends ManufactoryRecipe {

    public GrinderRecipe(ResourceLocation recipeId, Ingredient ingredient, ItemStack result, float extraChance, int[] extraAmounts, Tier tierRequired, int powerRequired,
            int processTime) {
        super(recipeId, ingredient, result, extraChance, extraAmounts, tierRequired, powerRequired, processTime);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.GRINDER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.GRINDER.get();
    }
}
