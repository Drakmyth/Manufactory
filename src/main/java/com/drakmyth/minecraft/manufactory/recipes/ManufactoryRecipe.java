/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.recipes;

import java.util.Random;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public abstract class ManufactoryRecipe implements Recipe<Container> {
    private final ResourceLocation recipeId;
    private Ingredient ingredient;
    private ItemStack result;
    private float extraChance;
    private int[] extraAmounts;
    private int tierRequired;
    private int powerRequired;
    private int processTime;

    public ManufactoryRecipe(ResourceLocation recipeId, Ingredient ingredient, ItemStack result, float extraChance, int[] extraAmounts, int tierRequired, int powerRequired, int processTime) {
        this.recipeId = recipeId;
        this.ingredient = ingredient;
        this.result = result;
        this.extraChance = extraChance;
        this.extraAmounts = extraAmounts;
        this.tierRequired = tierRequired;
        this.powerRequired = powerRequired;
        this.processTime = processTime;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public float getExtraChance() {
        return extraChance;
    }

    public boolean hasExtraChance() {
        return extraAmounts.length > 0;
    }

    public int getRandomExtraAmount(Random rand) {
        return extraAmounts[rand.nextInt(extraAmounts.length)];
    }

    public int[] getExtraAmounts() {
        return extraAmounts;
    }

    public int getTierRequired() {
        return tierRequired;
    }

    public int getPowerRequired() {
        return powerRequired;
    }

    public int getProcessTime() {
        return processTime;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        return ingredient.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(Container inv) {
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
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }
}
