/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class ManufactoryRecipe implements IRecipe<IInventory> {
    private final ResourceLocation recipeId;
    private Ingredient ingredient;
    private ItemStack result;
    private float extraChance;
    private int[] extraAmounts;
    private int powerRequired;
    private int processTime;

    public ManufactoryRecipe(ResourceLocation recipeId, Ingredient ingredient, ItemStack result, float extraChance, int[] extraAmounts, int powerRequired, int processTime) {
        this.recipeId = recipeId;
        this.ingredient = ingredient;
        this.result = result;
        this.extraChance = extraChance;
        this.extraAmounts = extraAmounts;
        this.powerRequired = powerRequired;
        this.processTime = processTime;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public float getExtraChance() {
        return extraChance;
    }

    public int[] getExtraAmounts() {
        return extraAmounts;
    }

    public int getPowerRequired() {
        return powerRequired;
    }

    public int getProcessTime() {
        return processTime;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return ingredient.test(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return result;
    }

    public ItemStack getMaxOutput() {
        ItemStack maxResult = result.copy();
        int max = 0;
        for(int amount : extraAmounts) {
            max = Math.max(max, amount);
        }
        maxResult.grow(max);
        return maxResult;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }
}
