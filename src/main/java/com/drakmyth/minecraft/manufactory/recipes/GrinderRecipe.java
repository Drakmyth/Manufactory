package com.drakmyth.minecraft.manufactory.recipes;

import com.drakmyth.minecraft.manufactory.init.ModRecipeSerializers;
import com.drakmyth.minecraft.manufactory.init.ModRecipeTypes;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;

public final class GrinderRecipe extends ManufactoryRecipe {
    public GrinderRecipe(Ingredient ingredient, ItemStackTemplate result, float extraChance, int[] extraAmounts,
            ToolMaterial tierRequired, int powerRequired, int processTime) {
        super(ingredient, result, extraChance, extraAmounts, tierRequired, powerRequired, processTime);
    }

    @Override public RecipeSerializer<? extends GrinderRecipe> getSerializer() { return ModRecipeSerializers.GRINDER.get(); }
    @Override public RecipeType<? extends GrinderRecipe> getType() { return ModRecipeTypes.GRINDER.get(); }
}
