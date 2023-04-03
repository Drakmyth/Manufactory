/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen.recipes;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.init.ModFluids;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import com.drakmyth.minecraft.manufactory.recipes.ManufactoryRecipeBuilder;

import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public final class OreRecipes extends RecipeProvider {

    private OreRecipes(DataGenerator generator) {
        super(generator);
    }

    public static void build(Consumer<FinishedRecipe> consumer) {

        List<OreProcessingRecipeData> ores = Arrays.asList(
            new OreProcessingRecipeData(ItemTags.COAL_ORES, ModItems.GROUND_COAL_ORE_ROUGH, ModItems.GROUND_COAL_ORE_FINE, ModFluids.SLURRIED_COAL_ORE, Items.COAL, Tiers.WOOD),
            new OreProcessingRecipeData(ItemTags.DIAMOND_ORES, ModItems.GROUND_DIAMOND_ORE_ROUGH, ModItems.GROUND_DIAMOND_ORE_FINE, ModFluids.SLURRIED_DIAMOND_ORE, Items.DIAMOND, Tiers.IRON),
            new OreProcessingRecipeData(ItemTags.EMERALD_ORES, ModItems.GROUND_EMERALD_ORE_ROUGH, ModItems.GROUND_EMERALD_ORE_FINE, ModFluids.SLURRIED_EMERALD_ORE, Items.EMERALD, Tiers.IRON),
            new OreProcessingRecipeData(ItemTags.GOLD_ORES, ModItems.GROUND_GOLD_ORE_ROUGH, ModItems.GROUND_GOLD_ORE_FINE, ModFluids.SLURRIED_GOLD_ORE, Items.RAW_GOLD, Tiers.IRON),
            new OreProcessingRecipeData(ItemTags.IRON_ORES, ModItems.GROUND_IRON_ORE_ROUGH, ModItems.GROUND_IRON_ORE_FINE, ModFluids.SLURRIED_IRON_ORE, Items.RAW_IRON, Tiers.STONE),
            new OreProcessingRecipeData(ItemTags.COPPER_ORES, ModItems.GROUND_COPPER_ORE_ROUGH, ModItems.GROUND_COPPER_ORE_FINE, ModFluids.SLURRIED_COPPER_ORE, Items.RAW_COPPER, Tiers.STONE),
            new OreProcessingRecipeData(ItemTags.LAPIS_ORES, ModItems.GROUND_LAPIS_ORE_ROUGH, ModItems.GROUND_LAPIS_ORE_FINE, ModFluids.SLURRIED_LAPIS_ORE, Items.LAPIS_LAZULI, Tiers.STONE),
            new OreProcessingRecipeData(Items.NETHER_QUARTZ_ORE, ModItems.GROUND_NETHER_QUARTZ_ORE_ROUGH, ModItems.GROUND_NETHER_QUARTZ_ORE_FINE, ModFluids.SLURRIED_NETHER_QUARTZ_ORE, Items.QUARTZ, Tiers.WOOD),
            new OreProcessingRecipeData(ItemTags.REDSTONE_ORES, ModItems.GROUND_REDSTONE_ORE_ROUGH, ModItems.GROUND_REDSTONE_ORE_FINE, ModFluids.SLURRIED_REDSTONE_ORE, Items.REDSTONE, Tiers.IRON),
            new OreProcessingRecipeData(Items.ANCIENT_DEBRIS, ModItems.GROUND_ANCIENT_DEBRIS_ROUGH, ModItems.GROUND_ANCIENT_DEBRIS_FINE, ModFluids.SLURRIED_ANCIENT_DEBRIS, Items.NETHERITE_SCRAP, Tiers.DIAMOND)
        );

        ores.stream().forEach(data -> {
            Item grinderOutput = data.getGrinderOutput();
            Item ballMillOutput = data.getBallMillOutput();
            // Fluid spiralClassifierOutput = data.getSpiralClassifierOutput();
            // Item floatationSeparatorOutput = data.getFloatationSeparatorOutput();
            Item finalOutput = data.getConcentratingDryerOutput();
            String grinderOutputName = ForgeRegistries.ITEMS.getKey(grinderOutput).getPath();
            String ballMillOutputName = ForgeRegistries.ITEMS.getKey(ballMillOutput).getPath();
            // String spiralClassifierOutputName = ForgeRegistries.FLUIDS.getKey(spiralClassifierOutput).getPath();
            // String floatationSeparatorOutputName = ForgeRegistries.ITEMS.getKey(floatationSeparatorOutput).getPath();
            String finalOutputName = ForgeRegistries.ITEMS.getKey(finalOutput).getPath();

            // Ore -> Ground Ore (Rough)
            ManufactoryRecipeBuilder.grinderRecipe(data.getOreIngredient(), data.getGrinderOutput())
                .withExtraChance(0.3f, data.getExtraAmounts())
                .withTierRequired(data.getTier())
                .addCriterion(String.format("has_%s", data.getOreName()), data.getOreInventoryChangeTriggerInstance())
                .build(consumer);

            // Ground Ore (Rough) -> Ground Ore (Fine)
            ManufactoryRecipeBuilder.ballMillRecipe(Ingredient.of(grinderOutput), ballMillOutput)
                .withExtraChance(0.54f, data.getExtraAmounts())
                .withTierRequired(data.getTier())
                .addCriterion(String.format("has_%s", grinderOutputName), has(grinderOutput))
                .build(consumer);

            // Ground Ore (Rough) -> Ingot
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(grinderOutput), finalOutput, 0.2f, 200)
                .unlockedBy(String.format("has_%s", grinderOutputName), has(grinderOutput)).group(finalOutputName)
                .save(consumer, String.format("%s:%s_from_ground_ore_rough", Reference.MOD_ID, finalOutputName));

            // Ground Ore (Fine) -> Ingot
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(ballMillOutput), finalOutput, 0.2f, 200)
                .unlockedBy(String.format("has_%s", ballMillOutputName), has(ballMillOutput)).group(finalOutputName)
                .save(consumer, String.format("%s:%s_from_ground_ore_fine", Reference.MOD_ID, finalOutputName));
        });

        // Nether Gold Ore -> Ground Gold Ore (Rough)
        ManufactoryRecipeBuilder.grinderRecipe(Ingredient.of(Items.NETHER_GOLD_ORE), ModItems.GROUND_GOLD_ORE_ROUGH.get())
            .withExtraChance(0.7f, -1) // 70% nothing, 30% rough ore
            .withTierRequired(Tiers.WOOD)
            .addCriterion("has_nether_gold_ore", has(Items.NETHER_GOLD_ORE))
            .build(consumer, String.format("%s:ground_gold_ore_rough_from_nether_gold_ore", Reference.MOD_ID));

        // Gilded Blackstone -> Ground Gold Ore (Rough)
        ManufactoryRecipeBuilder.grinderRecipe(Ingredient.of(Items.GILDED_BLACKSTONE), ModItems.GROUND_GOLD_ORE_ROUGH.get())
            .withExtraChance(0.7f, -1) // 70% nothing, 30% rough ore
            .withTierRequired(Tiers.WOOD)
            .addCriterion("has_gilded_blackstone", has(Items.GILDED_BLACKSTONE))
            .build(consumer, String.format("%s:ground_gold_ore_rough_from_gilded_blackstone", Reference.MOD_ID));
    }

    private static class OreProcessingRecipeData {
        private final Item ore;
        private final TagKey<Item> oreTag;
        private final Item stage1;
        private final Item stage2;
        // private final Fluid stage3;
        // private final Item stage4;
        private final Item stage5;
        private final Tier tier;
        private final int[] extraAmounts;
        
        private OreProcessingRecipeData(Item ore, TagKey<Item> oreTag, Item stage1, Item stage2, Fluid stage3, /*Item stage4,*/ Item stage5, Tier tier, int[] extraAmounts) {
            this.ore = ore;
            this.oreTag = oreTag;
            this.stage1 = stage1;
            this.stage2 = stage2;
            // this.stage3 = stage3;
            //this.stage4 = stage4;
            this.stage5 = stage5;
            this.tier = tier;
            this.extraAmounts = extraAmounts;
        }
        
        public OreProcessingRecipeData(TagKey<Item> oreTag, RegistryObject<Item> stage1, RegistryObject<Item> stage2, RegistryObject<FlowingFluid> stage3, /*Item stage4,*/ Item stage5, Tier tier, int[] extraAmounts) {
            this(null, oreTag, stage1.get(), stage2.get(), stage3.get(), /*stage4,*/ stage5, tier, extraAmounts);
        }

        public OreProcessingRecipeData(TagKey<Item> oreTag, RegistryObject<Item> stage1, RegistryObject<Item> stage2, RegistryObject<FlowingFluid> stage3, /*Item stage4,*/ Item stage5, Tier tier) {
            this(oreTag, stage1, stage2, stage3, /*stage4,*/ stage5, tier, new int[]{1});
        }
        
        public OreProcessingRecipeData(Item ore, RegistryObject<Item> stage1, RegistryObject<Item> stage2, RegistryObject<FlowingFluid> stage3, /*Item stage4,*/ Item stage5, Tier tier, int[] extraAmounts) {
            this(ore, null, stage1.get(), stage2.get(), stage3.get(), /*stage4,*/ stage5, tier, extraAmounts);
        }

        public OreProcessingRecipeData(Item ore, RegistryObject<Item> stage1, RegistryObject<Item> stage2, RegistryObject<FlowingFluid> stage3, /*Item stage4,*/ Item stage5, Tier tier) {
            this(ore, stage1, stage2, stage3, /*stage4,*/ stage5, tier, new int[]{1});
        }

        public Ingredient getOreIngredient() {
            return ore == null ? Ingredient.of(oreTag) : Ingredient.of(ore);
        }

        public String getOreName() {
            return ore == null ? oreTag.location().getPath() : ForgeRegistries.ITEMS.getKey(ore).getPath();
        }

        public InventoryChangeTrigger.TriggerInstance getOreInventoryChangeTriggerInstance() {
            return ore == null ? has(oreTag) : has(ore);
        }

        public Item getGrinderOutput() {
            return stage1;
        }

        public Item getBallMillOutput() {
            return stage2;
        }

        // public Fluid getSpiralClassifierOutput() {
        //     return stage3;
        // }

        // public Item getFloatationSeparatorOutput() {
        //     return stage4;
        // }

        public Item getConcentratingDryerOutput() {
            return stage5;
        }

        public Tier getTier() {
            return tier;
        }

        public int[] getExtraAmounts() {
            return extraAmounts;
        }
    }
}
