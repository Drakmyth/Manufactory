package com.drakmyth.minecraft.manufactory.datagen;

import java.util.function.Consumer;
import com.drakmyth.minecraft.manufactory.datagen.recipes.BlockRecipes;
import com.drakmyth.minecraft.manufactory.datagen.recipes.ItemRecipes;
import com.drakmyth.minecraft.manufactory.datagen.recipes.MachineUpgradeRecipes;
import com.drakmyth.minecraft.manufactory.datagen.recipes.OreRecipes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        BlockRecipes.build(consumer);
        OreRecipes.build(consumer);
        ItemRecipes.build(consumer);
        MachineUpgradeRecipes.build(consumer);
    }
}
