package com.drakmyth.minecraft.manufactory.recipes;

import com.drakmyth.minecraft.manufactory.init.ModRecipeSerializers;
import com.drakmyth.minecraft.manufactory.init.ModRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.Identifier;

public class BallMillRecipe extends ManufactoryRecipe {

    public BallMillRecipe(Identifier recipeId, Ingredient ingredient, ItemStack result, float extraChance, int[] extraAmounts, ToolMaterial tierRequired, int powerRequired,
            int processTime) {
        super(recipeId, ingredient, result, extraChance, extraAmounts, tierRequired, powerRequired, processTime);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.GRINDER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.BALL_MILL.get();
    }
}
