package com.drakmyth.minecraft.manufactory.datagen;

import java.util.concurrent.CompletableFuture;
import com.drakmyth.minecraft.manufactory.datagen.recipes.BlockRecipes;
import com.drakmyth.minecraft.manufactory.datagen.recipes.ItemRecipes;
import com.drakmyth.minecraft.manufactory.datagen.recipes.MachineUpgradeRecipes;
import com.drakmyth.minecraft.manufactory.datagen.recipes.OreRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

public final class ModRecipeProvider extends RecipeProvider {
    private ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        new BlockRecipes(registries, output).buildRecipes();
        new ItemRecipes(registries, output).buildRecipes();
        new MachineUpgradeRecipes(registries, output).buildRecipes();
        new OreRecipes(registries, output).buildRecipes();
    }

    public static final class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Manufactory recipes";
        }
    }
}
