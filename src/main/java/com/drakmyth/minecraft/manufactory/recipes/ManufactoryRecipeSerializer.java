/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.recipes;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ManufactoryRecipeSerializer<T extends ManufactoryRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>>
        implements IRecipeSerializer<T> {
    private final ManufactoryRecipeSerializer.IFactory<T> factory;


    public ManufactoryRecipeSerializer(ManufactoryRecipeSerializer.IFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public T read(ResourceLocation recipeId, JsonObject json) {
        Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
        JsonObject resultObj = json.get("result").getAsJsonObject();
        ResourceLocation itemResourceLocation = ResourceLocation.create(JSONUtils.getString(resultObj, "item", "minecraft:empty"), ':');
        int amount = JSONUtils.getInt(resultObj, "count", 0);
        ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemResourceLocation), amount);
        float extraChance = JSONUtils.getFloat(json, "extraChance");
        JsonArray resultArray = json.getAsJsonArray("extraAmounts");
        int[] extraAmounts = new int[resultArray.size()];
        for (int i = 0; i < resultArray.size(); i++) {
            int element = resultArray.get(i).getAsInt();
            extraAmounts[i] = element;
        }
        int tierRequired = JSONUtils.getInt(json, "tierRequired", 0);
        int powerRequired = JSONUtils.getInt(json, "powerRequired", 25);
        int processTime = JSONUtils.getInt(json, "processTime", 200);
        return factory.create(recipeId, ingredient, result, extraChance, extraAmounts, tierRequired, powerRequired, processTime);
    }

    @Nullable
    @Override
    public T read(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient ingredient = Ingredient.read(buffer);
        ItemStack result = buffer.readItemStack();
        float extraChance = buffer.readFloat();
        int extraAmountsCount = buffer.readInt();
        int[] extraAmounts = new int[extraAmountsCount];
        for (int i = 0; i < extraAmountsCount; i++) {
            extraAmounts[i] = buffer.readInt();
        }
        int tierRequired = buffer.readInt();
        int powerRequired = buffer.readInt();
        int processTime = buffer.readInt();
        return factory.create(recipeId, ingredient, result, extraChance, extraAmounts, tierRequired, powerRequired, processTime);
    }

    @Override
    public void write(PacketBuffer buffer, T recipe) {
        recipe.getIngredient().write(buffer);
        buffer.writeItemStack(recipe.getRecipeOutput());
        buffer.writeFloat(recipe.getExtraChance());
        int[] extraAmounts = recipe.getExtraAmounts();
        buffer.writeInt(extraAmounts.length);
        for (int amount : extraAmounts) {
            buffer.writeFloat(amount);
        }
        buffer.writeInt(recipe.getTierRequired());
        buffer.writeInt(recipe.getPowerRequired());
        buffer.writeInt(recipe.getProcessTime());
    }

    public interface IFactory<T extends ManufactoryRecipe> {
        T create(ResourceLocation recipeId, Ingredient ingredient, ItemStack result, float extraChance, int[] extraAmounts, int tierRequired, int powerRequired, int processTime);
    }
}
