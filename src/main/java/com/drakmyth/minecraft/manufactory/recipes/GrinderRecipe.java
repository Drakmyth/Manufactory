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
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class GrinderRecipe implements IRecipe<IInventory> {

    public static final IRecipeType<GrinderRecipe> recipeType = IRecipeType.register("grinder");
    public static final Serializer serializer = new Serializer();

    private final ResourceLocation recipeId;
    private Ingredient ingredient;
    private List<Tuple<ItemStack, Float>> results;
    private int powerRequired;
    private int processTime;

    public GrinderRecipe(ResourceLocation recipeId) {
        this.recipeId = recipeId;
        results = new ArrayList<>();
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
        return serializer;
    }

    @Override
    public IRecipeType<?> getType() {
        return recipeType;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<GrinderRecipe> {

        @Override
        public GrinderRecipe read(ResourceLocation recipeId, JsonObject json) {
            GrinderRecipe recipe = new GrinderRecipe(recipeId);

            recipe.ingredient = Ingredient.deserialize(json.get("ingredient"));
            JsonArray resultArray = json.getAsJsonArray("results");
            recipe.results = Stream.of(resultArray).map(element -> {
                JsonObject result = element.get(0).getAsJsonObject();
                ResourceLocation itemResourceLocation = ResourceLocation.create(JSONUtils.getString(result, "item", "minecraft:empty"), ':');
                int amount = JSONUtils.getInt(result, "amount", 0);
                ItemStack item = new ItemStack(ForgeRegistries.ITEMS.getValue(itemResourceLocation), amount);
                float chance = JSONUtils.getFloat(result, "chance", 1.0f);
                return new Tuple<ItemStack, Float>(item, chance);
            }).collect(Collectors.toList());
            recipe.powerRequired = JSONUtils.getInt(json, "powerRequired", 25);
            recipe.processTime = JSONUtils.getInt(json, "processTime", 200);

            return recipe;
        }

        @Nullable
        @Override
        public GrinderRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            GrinderRecipe recipe = new GrinderRecipe(recipeId);
            recipe.ingredient = Ingredient.read(buffer);
            int resultCount = buffer.readInt();
            for (int i = 0; i < resultCount; i++) {
                ItemStack result = buffer.readItemStack();
                float chance = buffer.readFloat();
                recipe.results.add(new Tuple<ItemStack, Float>(result, chance));
            }
            recipe.powerRequired = buffer.readInt();
            recipe.processTime = buffer.readInt();
            return recipe;
        }

        @Override
        public void write(PacketBuffer buffer, GrinderRecipe recipe) {
            recipe.ingredient.write(buffer);
            buffer.writeInt(recipe.results.size());
            for (Tuple<ItemStack, Float> result : recipe.results) {
                buffer.writeItemStack(result.getA());
                buffer.writeFloat(result.getB());
            }
            buffer.writeInt(recipe.getPowerRequired());
            buffer.writeInt(recipe.getProcessTime());
        }
    }
}
