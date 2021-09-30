/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.recipes;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.drakmyth.minecraft.manufactory.init.ModRecipeSerializers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ManufactoryRecipeBuilder {
   private Ingredient ingredient;
   private ItemStack result;
   private float extraChance;
   private int[] extraAmounts;
   private Tier tierRequired;
   private int powerRequired;
   private int processTime;
   private final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();
   private String group;
   private final ManufactoryRecipeSerializer<?> recipeSerializer;

   private ManufactoryRecipeBuilder(Ingredient ingredient, ItemStack result, ManufactoryRecipeSerializer<?> serializer) {
      this.ingredient = ingredient;
      this.result = result.copy();
      this.extraChance = 0;
      this.extraAmounts = new int[0];
      this.tierRequired = Tiers.WOOD;
      this.powerRequired = 25;
      this.processTime = 200;
      this.recipeSerializer = serializer;
   }

   private static ManufactoryRecipeBuilder manufactoryRecipe(Ingredient ingredient, ItemStack result, ManufactoryRecipeSerializer<?> serializer) {
      return new ManufactoryRecipeBuilder(ingredient, result, serializer);
   }

   public static ManufactoryRecipeBuilder grinderRecipe(Ingredient ingredient, ItemLike result) {
      return grinderRecipe(ingredient, result, 1);
   }

   public static ManufactoryRecipeBuilder grinderRecipe(Ingredient ingredient, ItemLike result, int count) {
      return manufactoryRecipe(ingredient, new ItemStack(result, count), (ManufactoryRecipeSerializer<?>)ModRecipeSerializers.GRINDER.get());
   }

   public static ManufactoryRecipeBuilder ballMillRecipe(Ingredient ingredient, ItemLike result) {
      return ballMillRecipe(ingredient, result, 1);
   }

   public static ManufactoryRecipeBuilder ballMillRecipe(Ingredient ingredient, ItemLike result, int count) {
      return manufactoryRecipe(ingredient, new ItemStack(result, count), (ManufactoryRecipeSerializer<?>)ModRecipeSerializers.BALL_MILL.get());
   }

   public ManufactoryRecipeBuilder withExtraChance(float extraChance, int extraAmount) {
      return withExtraChance(extraChance, new int[]{extraAmount});
   }

   public ManufactoryRecipeBuilder withExtraChance(float extraChance, int[] extraAmounts) {
      this.extraChance = extraChance;
      this.extraAmounts = extraAmounts;
      return this;
   }

   public ManufactoryRecipeBuilder withTierRequired(Tier tierRequired) {
      this.tierRequired = tierRequired;
      return this;
   }

   public ManufactoryRecipeBuilder withPowerRequired(int powerRequired) {
      this.powerRequired = powerRequired;
      return this;
   }

   public ManufactoryRecipeBuilder withProcessTime(int processTime) {
      this.processTime = processTime;
      return this;
   }

   public ManufactoryRecipeBuilder addCriterion(String name, CriterionTriggerInstance criterion) {
      this.advancementBuilder.addCriterion(name, criterion);
      return this;
   }

   public void build(Consumer<FinishedRecipe> consumer) {
      this.build(consumer, ForgeRegistries.ITEMS.getKey(this.result.getItem()));
   }

   public void build(Consumer<FinishedRecipe> consumer, String save) {
      ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result.getItem());
      ResourceLocation resourcelocation1 = new ResourceLocation(save);
      if (resourcelocation1.equals(resourcelocation)) {
         throw new IllegalStateException("Recipe " + resourcelocation1 + " should remove its 'save' argument");
      } else {
         this.build(consumer, resourcelocation1);
      }
   }

   public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
      this.validate(id);
      this.advancementBuilder.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
      consumer.accept(new ManufactoryRecipeBuilder.Result(id, this.group == null ? "" : this.group, this.ingredient, this.result, this.extraChance, this.extraAmounts, this.tierRequired, this.powerRequired, this.processTime, this.advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getItem().getItemCategory().getRecipeFolderName() + "/" + id.getPath()), this.recipeSerializer));
   }

   /**
    * Makes sure that this obtainable.
    */
   private void validate(ResourceLocation id) {
      if (this.advancementBuilder.getCriteria().isEmpty()) {
         throw new IllegalStateException("No way of obtaining recipe " + id);
      }
   }

   public static class Result implements FinishedRecipe {
      private final ResourceLocation id;
      private final String group;
      private Ingredient ingredient;
      private ItemStack result;
      private float extraChance;
      private int[] extraAmounts;
      private Tier tierRequired;
      private int powerRequired;
      private int processTime;
      private final Advancement.Builder advancementBuilder;
      private final ResourceLocation advancementId;
      private final RecipeSerializer<? extends Recipe<Container>> serializer;

      public Result(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float extraChance, int[] extraAmounts, Tier tierRequired, int powerRequired, int processTime, Advancement.Builder advancementBuilder, ResourceLocation advancementId, RecipeSerializer<? extends Recipe<Container>> serializer) {
         this.id = id;
         this.group = group;
         this.ingredient = ingredient;
         this.result = result;
         this.extraChance = extraChance;
         this.extraAmounts = extraAmounts;
         this.tierRequired = tierRequired;
         this.powerRequired = powerRequired;
         this.processTime = processTime;
         this.advancementBuilder = advancementBuilder;
         this.advancementId = advancementId;
         this.serializer = serializer;
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
         if (!this.group.isEmpty()) {
            json.addProperty("group", this.group);
         }

         json.add("ingredient", this.ingredient.toJson());
         ItemStack resultStack = this.result.copy();
         json.add("result", serializeItemStack(resultStack));
         json.addProperty("extraChance", this.extraChance);
         JsonArray extraAmountsArray = new JsonArray();
         for (int amount : extraAmounts) {
            extraAmountsArray.add(amount);
         }
         json.add("extraAmounts", extraAmountsArray);
         json.addProperty("tierRequired", this.tierRequired.toString());
         json.addProperty("powerRequired", this.powerRequired);
         json.addProperty("processTime", this.processTime);
      }

      private JsonObject serializeItemStack(ItemStack itemStack) {
         JsonObject json = new JsonObject();
         json.addProperty("item", ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString());
         json.addProperty("count", itemStack.getCount());
         return json;
      }

      @Override
      public RecipeSerializer<?> getType() {
         return this.serializer;
      }

      /**
       * Gets the ID for the recipe.
       */
      @Override
      public ResourceLocation getId() {
         return this.id;
      }

      /**
       * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
       */
      @Override
      @Nullable
      public JsonObject serializeAdvancement() {
         return this.advancementBuilder.serializeToJson();
      }

      /**
       * Gets the ID for the advancement associated with this recipe. Should not be null if {@link #getAdvancementJson}
       * is non-null.
       */
      @Override
      @Nullable
      public ResourceLocation getAdvancementId() {
         return this.advancementId;
      }
   }
}
