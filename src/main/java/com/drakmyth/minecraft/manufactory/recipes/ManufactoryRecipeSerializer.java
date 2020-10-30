/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import net.minecraft.util.Tuple;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ManufactoryRecipeSerializer<T extends IRecipe<IInventory>> extends ForgeRegistryEntry<IRecipeSerializer<?>>
        implements IRecipeSerializer<GrinderRecipe> {

    @Override
    public GrinderRecipe read(ResourceLocation recipeId, JsonObject json) {
        Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
        JsonArray resultArray = json.getAsJsonArray("results");
        List<Tuple<ItemStack, Float>> results = Stream.of(resultArray).map(element -> {
            JsonObject result = element.get(0).getAsJsonObject();
            ResourceLocation itemResourceLocation = ResourceLocation.create(JSONUtils.getString(result, "item", "minecraft:empty"), ':');
            int amount = JSONUtils.getInt(result, "amount", 0);
            ItemStack item = new ItemStack(ForgeRegistries.ITEMS.getValue(itemResourceLocation), amount);
            float chance = JSONUtils.getFloat(result, "chance", 1.0f);
            return new Tuple<ItemStack, Float>(item, chance);
        }).collect(Collectors.toList());
        int powerRequired = JSONUtils.getInt(json, "powerRequired", 25);
        int processTime = JSONUtils.getInt(json, "processTime", 200);

        return new GrinderRecipe(recipeId, ingredient, results, powerRequired, processTime);
    }

    @Nullable
    @Override
    public GrinderRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient ingredient = Ingredient.read(buffer);
        int resultCount = buffer.readInt();
        List<Tuple<ItemStack, Float>> results = new ArrayList<>();
        for (int i = 0; i < resultCount; i++) {
            ItemStack result = buffer.readItemStack();
            float chance = buffer.readFloat();
            results.add(new Tuple<ItemStack, Float>(result, chance));
        }
        int powerRequired = buffer.readInt();
        int processTime = buffer.readInt();
        return new GrinderRecipe(recipeId, ingredient, results, powerRequired, processTime);
    }

    @Override
    public void write(PacketBuffer buffer, GrinderRecipe recipe) {
        recipe.getIngredient().write(buffer);
        List<Tuple<ItemStack, Float>> results = recipe.getResults();
        buffer.writeInt(results.size());
        for (Tuple<ItemStack, Float> result : results) {
            buffer.writeItemStack(result.getA());
            buffer.writeFloat(result.getB());
        }
        buffer.writeInt(recipe.getPowerRequired());
        buffer.writeInt(recipe.getProcessTime());
    }
}
