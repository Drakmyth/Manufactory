/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.recipes;

import java.util.ArrayList;
import java.util.List;

import com.drakmyth.minecraft.manufactory.init.ModRecipeSerializers;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;

public class GrinderRecipe extends ManufactoryRecipe {

    public static final IRecipeType<GrinderRecipe> recipeType = IRecipeType.register("grinder");

    private final ResourceLocation recipeId;
    private Ingredient ingredient;
    private ItemStack result;
    private float extraChance;
    private float[] extraAmounts;
    private int powerRequired;
    private int processTime;

    public GrinderRecipe(ResourceLocation recipeId) {
        this.recipeId = recipeId;
        result = ItemStack.EMPTY;
        extraAmounts = new float[0];
    }

    public GrinderRecipe(ResourceLocation recipeId, Ingredient ingredient, ItemStack result, float extraChance, float[] extraAmounts, int powerRequired, int processTime) {
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

    public float[] getExtraAmounts() {
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

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.GRINDER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return recipeType;
    }
}
