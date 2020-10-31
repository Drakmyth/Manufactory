/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.recipes;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ManufactoryRecipeSerializer<T extends IRecipe<IInventory>> extends ForgeRegistryEntry<IRecipeSerializer<?>>
        implements IRecipeSerializer<GrinderRecipe> {

    @Override
    public GrinderRecipe read(ResourceLocation recipeId, JsonObject json) {
        Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
        JsonObject resultObj = json.get("result").getAsJsonObject();
        ResourceLocation itemResourceLocation = ResourceLocation.create(JSONUtils.getString(resultObj, "item", "minecraft:empty"), ':');
        int amount = JSONUtils.getInt(resultObj, "amount", 0);
        ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemResourceLocation), amount);
        float extraChance = JSONUtils.getFloat(json, "extraChance");
        JsonArray resultArray = json.getAsJsonArray("extraAmounts");
        float[] extraAmounts = new float[resultArray.size()];
        for (int i = 0; i < resultArray.size(); i++) {
            float element = resultArray.get(i).getAsFloat();
            extraAmounts[i] = element;
        }
        int powerRequired = JSONUtils.getInt(json, "powerRequired", 25);
        int processTime = JSONUtils.getInt(json, "processTime", 200);
        return new GrinderRecipe(recipeId, ingredient, result, extraChance, extraAmounts, powerRequired, processTime);
    }

    @Nullable
    @Override
    public GrinderRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient ingredient = Ingredient.read(buffer);
        ItemStack result = buffer.readItemStack();
        float extraChance = buffer.readFloat();
        int extraAmountsCount = buffer.readInt();
        float[] extraAmounts = new float[extraAmountsCount];
        for (int i = 0; i < extraAmountsCount; i++) {
            extraAmounts[i] = buffer.readFloat();
        }
        int powerRequired = buffer.readInt();
        int processTime = buffer.readInt();
        return new GrinderRecipe(recipeId, ingredient, result, extraChance, extraAmounts, powerRequired, processTime);
    }

    @Override
    public void write(PacketBuffer buffer, GrinderRecipe recipe) {
        recipe.getIngredient().write(buffer);
        buffer.writeItemStack(recipe.getRecipeOutput());
        buffer.writeFloat(recipe.getExtraChance());
        float[] extraAmounts = recipe.getExtraAmounts();
        buffer.writeInt(extraAmounts.length);
        for (float amount : extraAmounts) {
            buffer.writeFloat(amount);
        }
        buffer.writeInt(recipe.getPowerRequired());
        buffer.writeInt(recipe.getProcessTime());
    }
}
