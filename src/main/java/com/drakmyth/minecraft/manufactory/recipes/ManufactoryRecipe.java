package com.drakmyth.minecraft.manufactory.recipes;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public abstract class ManufactoryRecipe implements Recipe<SingleRecipeInput> {
    private final Ingredient ingredient;
    private final ItemStackTemplate result;
    private final float extraChance;
    private final int[] extraAmounts;
    private final ToolMaterial tierRequired;
    private final int powerRequired;
    private final int processTime;

    protected ManufactoryRecipe(Ingredient ingredient, ItemStackTemplate result, float extraChance, int[] extraAmounts,
            ToolMaterial tierRequired, int powerRequired, int processTime) {
        if (extraChance < 0.0F || extraChance > 1.0F) throw new IllegalArgumentException("extraChance must be in [0, 1]");
        if (powerRequired <= 0) throw new IllegalArgumentException("powerRequired must be positive");
        if (processTime <= 0) throw new IllegalArgumentException("processTime must be positive");
        for (int amount : extraAmounts) {
            if (result.count() + amount < 0) {
                throw new IllegalArgumentException("extraAmounts cannot reduce the result below zero");
            }
        }
        this.ingredient = ingredient;
        this.result = result;
        this.extraChance = extraChance;
        this.extraAmounts = extraAmounts.clone();
        this.tierRequired = tierRequired;
        this.powerRequired = powerRequired;
        this.processTime = processTime;
    }

    public Ingredient getIngredient() { return ingredient; }
    public float getExtraChance() { return extraChance; }
    public boolean hasExtraChance() { return extraAmounts.length > 0; }
    public int[] getExtraAmounts() { return extraAmounts.clone(); }
    public ToolMaterial getTierRequired() { return tierRequired; }
    public int getPowerRequired() { return powerRequired; }
    public int getProcessTime() { return processTime; }
    public ItemStackTemplate getResultTemplate() { return result; }
    public ItemStack getResultItem() { return result.create(); }

    public int getRandomExtraAmount(RandomSource random) {
        return extraAmounts[random.nextInt(extraAmounts.length)];
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return ingredient.test(input.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input) {
        return result.create();
    }

    public ItemStack getMaxOutput() {
        ItemStack maximum = result.create();
        int extra = 0;
        for (int amount : extraAmounts) extra = Math.max(extra, amount);
        maximum.grow(extra);
        return maximum;
    }

    @Override public boolean showNotification() { return false; }
    @Override public String group() { return ""; }
    @Override public PlacementInfo placementInfo() { return PlacementInfo.create(ingredient); }
    @Override public RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.CRAFTING_MISC; }
}
