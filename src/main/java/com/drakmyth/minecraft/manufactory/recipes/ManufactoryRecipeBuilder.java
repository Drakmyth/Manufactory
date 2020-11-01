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
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ManufactoryRecipeBuilder {
   private Ingredient ingredient;
   private ItemStack result;
   private float extraChance;
   private float[] extraAmounts;
   private int powerRequired;
   private int processTime;
   private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
   private String group;
   private final ManufactoryRecipeSerializer<?> recipeSerializer;

   private ManufactoryRecipeBuilder(Ingredient ingredient, ItemStack result, ManufactoryRecipeSerializer<?> serializer) {
      this.ingredient = ingredient;
      this.result = result.copy();
      this.extraChance = 0;
      this.extraAmounts = new float[0];
      this.powerRequired = 25;
      this.processTime = 200;
      this.recipeSerializer = serializer;
   }

   private static ManufactoryRecipeBuilder manufactoryRecipe(Ingredient ingredient, ItemStack result, ManufactoryRecipeSerializer<?> serializer) {
      return new ManufactoryRecipeBuilder(ingredient, result, serializer);
   }

   public static ManufactoryRecipeBuilder grinderRecipe(Ingredient ingredient, IItemProvider result) {
      return grinderRecipe(ingredient, result, 1);
   }

   public static ManufactoryRecipeBuilder grinderRecipe(Ingredient ingredient, IItemProvider result, int count) {
      return manufactoryRecipe(ingredient, new ItemStack(result, count), (ManufactoryRecipeSerializer<?>)ModRecipeSerializers.GRINDER.get());
   }

   public ManufactoryRecipeBuilder withExtraChance(float extraChance, float extraAmount) {
      return withExtraChance(extraChance, new float[]{extraAmount});
   }

   public ManufactoryRecipeBuilder withExtraChance(float extraChance, float[] extraAmounts) {
      this.extraChance = extraChance;
      this.extraAmounts = extraAmounts;
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

   public ManufactoryRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn) {
      this.advancementBuilder.withCriterion(name, criterionIn);
      return this;
   }

   public void build(Consumer<IFinishedRecipe> consumerIn) {
      this.build(consumerIn, ForgeRegistries.ITEMS.getKey(this.result.getItem()));
   }

   public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
      ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result.getItem());
      ResourceLocation resourcelocation1 = new ResourceLocation(save);
      if (resourcelocation1.equals(resourcelocation)) {
         throw new IllegalStateException("Recipe " + resourcelocation1 + " should remove its 'save' argument");
      } else {
         this.build(consumerIn, resourcelocation1);
      }
   }

   public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
      this.validate(id);
      this.advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id)).withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
      consumerIn.accept(new ManufactoryRecipeBuilder.Result(id, this.group == null ? "" : this.group, this.ingredient, this.result, this.extraChance, this.extraAmounts, this.powerRequired, this.processTime, this.advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getItem().getGroup().getPath() + "/" + id.getPath()), this.recipeSerializer));
   }

   /**
    * Makes sure that this obtainable.
    */
   private void validate(ResourceLocation id) {
      if (this.advancementBuilder.getCriteria().isEmpty()) {
         throw new IllegalStateException("No way of obtaining recipe " + id);
      }
   }

   public static class Result implements IFinishedRecipe {
      private final ResourceLocation id;
      private final String group;
      private Ingredient ingredient;
      private ItemStack result;
      private float extraChance;
      private float[] extraAmounts;
      private int powerRequired;
      private int processTime;
      private final Advancement.Builder advancementBuilder;
      private final ResourceLocation advancementId;
      private final IRecipeSerializer<? extends IRecipe<IInventory>> serializer;

      public Result(ResourceLocation idIn, String groupIn, Ingredient ingredient, ItemStack result, float extraChance, float[] extraAmounts, int powerRequired, int processTime, Advancement.Builder advancementBuilderIn, ResourceLocation advancementIdIn, IRecipeSerializer<? extends IRecipe<IInventory>> serializerIn) {
         this.id = idIn;
         this.group = groupIn;
         this.ingredient = ingredient;
         this.result = result;
         this.extraChance = extraChance;
         this.extraAmounts = extraAmounts;
         this.powerRequired = powerRequired;
         this.processTime = processTime;
         this.advancementBuilder = advancementBuilderIn;
         this.advancementId = advancementIdIn;
         this.serializer = serializerIn;
      }

      public void serialize(JsonObject json) {
         if (!this.group.isEmpty()) {
            json.addProperty("group", this.group);
         }

         json.add("ingredient", this.ingredient.serialize());
         ItemStack resultStack = this.result.copy();
         json.add("result", serializeItemStack(resultStack));
         json.addProperty("extraChance", this.extraChance);
         JsonArray extraAmountsArray = new JsonArray();
         for (float amount : extraAmounts) {
            extraAmountsArray.add(amount);
         }
         json.add("extraAmounts", extraAmountsArray);
         json.addProperty("powerRequired", this.powerRequired);
         json.addProperty("processTime", this.processTime);
      }

      private JsonObject serializeItemStack(ItemStack itemStack) {
         JsonObject json = new JsonObject();
         json.addProperty("item", ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString());
         json.addProperty("count", itemStack.getCount());
         return json;
      }

      public IRecipeSerializer<?> getSerializer() {
         return this.serializer;
      }

      /**
       * Gets the ID for the recipe.
       */
      public ResourceLocation getID() {
         return this.id;
      }

      /**
       * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
       */
      @Nullable
      public JsonObject getAdvancementJson() {
         return this.advancementBuilder.serialize();
      }

      /**
       * Gets the ID for the advancement associated with this recipe. Should not be null if {@link #getAdvancementJson}
       * is non-null.
       */
      @Nullable
      public ResourceLocation getAdvancementID() {
         return this.advancementId;
      }
   }
}
