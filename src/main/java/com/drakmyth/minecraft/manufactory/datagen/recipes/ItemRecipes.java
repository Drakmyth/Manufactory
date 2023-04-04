package com.drakmyth.minecraft.manufactory.datagen.recipes;

import java.util.function.Consumer;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public final class ItemRecipes extends RecipeProvider {
    private ItemRecipes(DataGenerator generator) {
        super(generator);
    }

    public static void build(Consumer<FinishedRecipe> consumer) {

        // 9 Amber = Amber Block
        ShapelessRecipeBuilder.shapeless(ModItems.AMBER.get(), 9)
                .requires(ModBlocks.AMBER_BLOCK.get())
                .unlockedBy("has_amber_block", has(ModBlocks.AMBER_BLOCK.get()))
                .save(consumer);

        // Tapping Knife = 2 Iron Ingot + Stick
        ShapedRecipeBuilder.shaped(ModItems.TAPPING_KNIFE.get())
                .pattern("ii ")
                .pattern("  s")
                .define('i', Items.IRON_INGOT)
                .define('s', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);

        // Rubber = Coagulated Latex
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.COAGULATED_LATEX.get()), ModItems.RUBBER.get(), 0.1f, 200)
                .unlockedBy("has_coagulated_latex", has(ModItems.COAGULATED_LATEX.get()))
                .save(consumer);

        // 3 Redstone Wire = 3 String + Redstone Dust
        ShapelessRecipeBuilder.shapeless(ModItems.REDSTONE_WIRE.get(), 3)
                .requires(Items.REDSTONE)
                .requires(Items.STRING, 3)
                .unlockedBy("has_string", has(Items.STRING))
                .save(consumer);

        // Coupling = 4 Iron Ingot
        ShapedRecipeBuilder.shaped(ModItems.COUPLING.get())
                .pattern(" i ")
                .pattern("i i")
                .pattern(" i ")
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);

        // Wrench = 4 Iron Ingot
        ShapedRecipeBuilder.shaped(ModItems.WRENCH.get())
                .pattern("i i")
                .pattern(" i ")
                .pattern(" i ")
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);

        // Rock Drill = 2 Coupling + Stone Pickaxe
        ShapedRecipeBuilder.shaped(ModItems.ROCK_DRILL.get())
                .pattern("cpc")
                .define('c', ModItems.COUPLING.get())
                .define('p', Items.STONE_PICKAXE)
                .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
                .save(consumer);
    }
}
