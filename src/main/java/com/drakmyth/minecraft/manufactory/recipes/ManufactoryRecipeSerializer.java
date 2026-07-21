package com.drakmyth.minecraft.manufactory.recipes;

import javax.annotation.Nullable;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;

public class ManufactoryRecipeSerializer<T extends ManufactoryRecipe> implements RecipeSerializer<T> {
    private final ManufactoryRecipeSerializer.IFactory<T> factory;


    public ManufactoryRecipeSerializer(ManufactoryRecipeSerializer.IFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public T fromJson(Identifier recipeId, JsonObject json) {
        Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
        JsonObject resultObj = json.get("result").getAsJsonObject();
        Identifier itemIdentifier = Identifier.of(GsonHelper.getAsString(resultObj, "item", "minecraft:empty"), ':');
        int amount = GsonHelper.getAsInt(resultObj, "count", 0);
        ItemStack result = new ItemStack(BuiltInRegistries.ITEMS.getValue(itemIdentifier), amount);
        float extraChance = GsonHelper.getAsFloat(json, "extraChance");
        JsonArray resultArray = json.getAsJsonArray("extraAmounts");
        int[] extraAmounts = new int[resultArray.size()];
        for (int i = 0; i < resultArray.size(); i++) {
            int element = resultArray.get(i).getAsInt();
            extraAmounts[i] = element;
        }
        ToolMaterial tierRequired = ToolMaterial.valueOf(GsonHelper.getAsString(json, "tierRequired", "WOOD"));
        int powerRequired = GsonHelper.getAsInt(json, "powerRequired", 25);
        int processTime = GsonHelper.getAsInt(json, "processTime", 200);
        return factory.create(recipeId, ingredient, result, extraChance, extraAmounts, tierRequired, powerRequired, processTime);
    }

    @Nullable
    @Override
    public T fromNetwork(Identifier recipeId, FriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack result = buffer.readItem();
        float extraChance = buffer.readFloat();
        int extraAmountsCount = buffer.readInt();
        int[] extraAmounts = new int[extraAmountsCount];
        for (int i = 0; i < extraAmountsCount; i++) {
            extraAmounts[i] = buffer.readInt();
        }
        ToolMaterial tierRequired = ToolMaterial.valueOf(buffer.readUtf());
        int powerRequired = buffer.readInt();
        int processTime = buffer.readInt();
        return factory.create(recipeId, ingredient, result, extraChance, extraAmounts, tierRequired, powerRequired, processTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        recipe.getIngredient().toNetwork(buffer);
        buffer.writeItem(recipe.getResultItem());
        buffer.writeFloat(recipe.getExtraChance());
        int[] extraAmounts = recipe.getExtraAmounts();
        buffer.writeInt(extraAmounts.length);
        for (int amount : extraAmounts) {
            buffer.writeFloat(amount);
        }
        buffer.writeUtf(recipe.getTierRequired().toString());
        buffer.writeInt(recipe.getPowerRequired());
        buffer.writeInt(recipe.getProcessTime());
    }

    public interface IFactory<T extends ManufactoryRecipe> {
        T create(Identifier recipeId, Ingredient ingredient, ItemStack result, float extraChance, int[] extraAmounts, ToolMaterial tierRequired, int powerRequired, int processTime);
    }
}
