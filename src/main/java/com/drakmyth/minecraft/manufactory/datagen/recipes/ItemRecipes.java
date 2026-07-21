package com.drakmyth.minecraft.manufactory.datagen.recipes;

import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public final class ItemRecipes extends RecipeProvider {
    public ItemRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    public void buildRecipes() {

        // 9 Amber = Amber Block
        shapeless(RecipeCategory.MISC, ModItems.AMBER.get(), 9)
                .requires(ModBlocks.AMBER_BLOCK.get())
                .unlockedBy("has_amber_block", has(ModBlocks.AMBER_BLOCK.get()))
                .save(output);

        // Tapping Knife = 2 Iron Ingot + Stick
        shaped(RecipeCategory.MISC, ModItems.TAPPING_KNIFE.get())
                .pattern("ii ")
                .pattern("  s")
                .define('i', Items.IRON_INGOT)
                .define('s', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output);

        // Rubber = Coagulated Latex
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.COAGULATED_LATEX.get()), RecipeCategory.MISC,
                CookingBookCategory.MISC, ModItems.RUBBER.get(), 0.1f, 200)
                .unlockedBy("has_coagulated_latex", has(ModItems.COAGULATED_LATEX.get()))
                .save(output);

        // 3 Redstone Wire = 3 String + Redstone Dust
        shapeless(RecipeCategory.MISC, ModItems.REDSTONE_WIRE.get(), 3)
                .requires(Items.REDSTONE)
                .requires(Items.STRING, 3)
                .unlockedBy("has_string", has(Items.STRING))
                .save(output);

        // Coupling = 4 Iron Ingot
        shaped(RecipeCategory.MISC, ModItems.COUPLING.get())
                .pattern(" i ")
                .pattern("i i")
                .pattern(" i ")
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output);

        // Wrench = 4 Iron Ingot
        shaped(RecipeCategory.MISC, ModItems.WRENCH.get())
                .pattern("i i")
                .pattern(" i ")
                .pattern(" i ")
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output);

        // Rock Drill = 2 Coupling + Stone Pickaxe
        shaped(RecipeCategory.MISC, ModItems.ROCK_DRILL.get())
                .pattern("cpc")
                .define('c', ModItems.COUPLING.get())
                .define('p', Items.STONE_PICKAXE)
                .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
                .save(output);
    }
}
