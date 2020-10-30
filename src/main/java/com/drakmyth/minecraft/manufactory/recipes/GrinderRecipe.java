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
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;

public class GrinderRecipe implements IRecipe<IInventory> {

    public static final IRecipeType<GrinderRecipe> recipeType = IRecipeType.register("grinder");

    private final ResourceLocation recipeId;
    private Ingredient ingredient;
    private List<Tuple<ItemStack, Float>> results;
    private int powerRequired;
    private int processTime;

    public GrinderRecipe(ResourceLocation recipeId) {
        this.recipeId = recipeId;
        results = new ArrayList<>();
    }

    public GrinderRecipe(ResourceLocation recipeId, Ingredient ingredient, List<Tuple<ItemStack, Float>> results, int powerRequired, int processTime) {
        this.recipeId = recipeId;
        this.ingredient = ingredient;
        this.results = results;
        this.powerRequired = powerRequired;
        this.processTime = processTime;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public List<Tuple<ItemStack, Float>> getResults() {
        return results;
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
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
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
