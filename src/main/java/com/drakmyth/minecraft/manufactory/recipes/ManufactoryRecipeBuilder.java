package com.drakmyth.minecraft.manufactory.recipes;

import com.drakmyth.minecraft.manufactory.init.ModRecipeSerializers;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

public final class ManufactoryRecipeBuilder implements RecipeBuilder {
    private final Ingredient ingredient;
    private final ItemStackTemplate result;
    private final boolean grinder;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
    private float extraChance;
    private int[] extraAmounts = new int[0];
    private ToolMaterial tierRequired = ToolMaterial.WOOD;
    private int powerRequired = 25;
    private int processTime = 200;
    private @Nullable String group;

    private ManufactoryRecipeBuilder(Ingredient ingredient, ItemStackTemplate result, boolean grinder) {
        this.ingredient = ingredient;
        this.result = result;
        this.grinder = grinder;
    }

    public static ManufactoryRecipeBuilder grinderRecipe(Ingredient ingredient, ItemLike result) {
        return grinderRecipe(ingredient, result, 1);
    }

    public static ManufactoryRecipeBuilder grinderRecipe(Ingredient ingredient, ItemLike result, int count) {
        return new ManufactoryRecipeBuilder(ingredient, new ItemStackTemplate(result.asItem(), count), true);
    }

    public static ManufactoryRecipeBuilder ballMillRecipe(Ingredient ingredient, ItemLike result) {
        return ballMillRecipe(ingredient, result, 1);
    }

    public static ManufactoryRecipeBuilder ballMillRecipe(Ingredient ingredient, ItemLike result, int count) {
        return new ManufactoryRecipeBuilder(ingredient, new ItemStackTemplate(result.asItem(), count), false);
    }

    public ManufactoryRecipeBuilder withExtraChance(float chance, int amount) {
        return withExtraChance(chance, new int[] { amount });
    }

    public ManufactoryRecipeBuilder withExtraChance(float chance, int[] amounts) {
        this.extraChance = chance;
        this.extraAmounts = amounts.clone();
        return this;
    }

    public ManufactoryRecipeBuilder withTierRequired(ToolMaterial tier) {
        this.tierRequired = tier;
        return this;
    }

    public ManufactoryRecipeBuilder withPowerRequired(int power) {
        this.powerRequired = power;
        return this;
    }

    public ManufactoryRecipeBuilder withProcessTime(int ticks) {
        this.processTime = ticks;
        return this;
    }

    public ManufactoryRecipeBuilder addCriterion(String name, Criterion<?> criterion) {
        return unlockedBy(name, criterion);
    }

    @Override
    public ManufactoryRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    @Override
    public ManufactoryRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(result);
    }

    public void build(RecipeOutput output) {
        save(output);
    }

    public void build(RecipeOutput output, String id) {
        save(output, id);
    }

    public void build(RecipeOutput output, Identifier id) {
        save(output, ResourceKey.create(Registries.RECIPE, id));
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        ManufactoryRecipe recipe = grinder
                ? new GrinderRecipe(ingredient, result, extraChance, extraAmounts, tierRequired, powerRequired, processTime)
                : new BallMillRecipe(ingredient, result, extraChance, extraAmounts, tierRequired, powerRequired, processTime);
        output.accept(id, recipe, advancementBuilder.build(output, id, "manufactory"));
    }

    public RecipeSerializer<?> serializer() {
        return grinder ? ModRecipeSerializers.GRINDER.get() : ModRecipeSerializers.BALL_MILL.get();
    }
}
