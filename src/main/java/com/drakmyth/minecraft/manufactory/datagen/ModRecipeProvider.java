/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import com.drakmyth.minecraft.manufactory.recipes.ManufactoryRecipeBuilder;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {

        // Amber Block = 9 Amber
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.AMBER_BLOCK.get())
            .patternLine("aaa")
            .patternLine("aaa")
            .patternLine("aaa")
            .key('a', ModItems.AMBER.get())
            .addCriterion("has_amber", InventoryChangeTrigger.Instance.forItems(ModItems.AMBER.get()))
            .build(consumer);

        // 9 Amber = Amber Block
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.AMBER.get(), 9)
            .addIngredient(ModBlocks.AMBER_BLOCK.get())
            .addCriterion("has_amber_block", InventoryChangeTrigger.Instance.forItems(ModBlocks.AMBER_BLOCK.get()))
            .build(consumer);

        // Latex Collector = String + Bowl
        ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.LATEX_COLLECTOR.get())
            .addIngredient(Items.STRING)
            .addIngredient(Items.BOWL)
            .addCriterion("has_bowl", InventoryChangeTrigger.Instance.forItems(Items.BOWL))
            .build(consumer);

        // Tapping Knife = 2 Iron Ingot + Stick
        ShapedRecipeBuilder.shapedRecipe(ModItems.TAPPING_KNIFE.get())
            .patternLine("ii ")
            .patternLine("  s")
            .key('i', Items.IRON_INGOT)
            .key('s', Items.STICK)
            .addCriterion("has_iron_ingot", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT))
            .build(consumer);

        // Rubber = Coagulated Latex
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModItems.COAGULATED_LATEX.get()), ModItems.RUBBER.get(), 0.1f, 200)
            .addCriterion("has_coagulated_latex", InventoryChangeTrigger.Instance.forItems(ModItems.COAGULATED_LATEX.get()))
            .build(consumer);

        // 3 Redstone Wire = 3 String + Redstone Dust
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.REDSTONE_WIRE.get(), 3)
            .addIngredient(Items.REDSTONE)
            .addIngredient(Items.STRING, 3)
            .addCriterion("has_string", InventoryChangeTrigger.Instance.forItems(Items.STRING))
            .build(consumer);

        // Coupling = 4 Iron Ingot
        ShapedRecipeBuilder.shapedRecipe(ModItems.COUPLING.get())
            .patternLine(" i ")
            .patternLine("i i")
            .patternLine(" i ")
            .key('i', Items.IRON_INGOT)
            .addCriterion("has_iron_ingot", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT))
            .build(consumer);

        // Wrench = 4 Iron Ingot
        ShapedRecipeBuilder.shapedRecipe(ModItems.WRENCH.get())
            .patternLine("i i")
            .patternLine(" i ")
            .patternLine(" i ")
            .key('i', Items.IRON_INGOT)
            .addCriterion("has_iron_ingot", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT))
            .build(consumer);

        // Motor Tier 0 = Coupling + Redstone Dust + 3 Iron Ingot
        ShapedRecipeBuilder.shapedRecipe(ModItems.MOTOR_TIER0.get())
            .patternLine(" i ")
            .patternLine("cri")
            .patternLine(" i ")
            .key('i', Items.IRON_INGOT)
            .key('c', ModItems.COUPLING.get())
            .key('r', Items.REDSTONE)
            .addCriterion("has_redstone", InventoryChangeTrigger.Instance.forItems(Items.REDSTONE))
            .build(consumer);

        // Motor Tier 1 = Motor Tier 0 + Gold Ingot
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.MOTOR_TIER1.get())
            .addIngredient(ModItems.MOTOR_TIER0.get())
            .addIngredient(Items.GOLD_INGOT)
            .addCriterion("has_motor_tier_0", InventoryChangeTrigger.Instance.forItems(ModItems.MOTOR_TIER0.get()))
            .build(consumer);

        // Motor Tier 2 = Motor Tier 1 + Diamond
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.MOTOR_TIER2.get())
            .addIngredient(ModItems.MOTOR_TIER1.get())
            .addIngredient(Items.DIAMOND)
            .addCriterion("has_motor_tier_1", InventoryChangeTrigger.Instance.forItems(ModItems.MOTOR_TIER1.get()))
            .build(consumer);

        // Motor Tier 3 = Motor Tier 2 + Netherite Ingot
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.MOTOR_TIER3.get())
            .addIngredient(ModItems.MOTOR_TIER2.get())
            .addIngredient(Items.NETHERITE_INGOT)
            .addCriterion("has_motor_tier_2", InventoryChangeTrigger.Instance.forItems(ModItems.MOTOR_TIER2.get()))
            .build(consumer);

        // Battery = Redstone Wire + Nether Quartz + Redstone Dust
        ShapedRecipeBuilder.shapedRecipe(ModItems.BATTERY.get())
            .patternLine("w")
            .patternLine("n")
            .patternLine("r")
            .key('w', ModItems.REDSTONE_WIRE.get())
            .key('n', Items.QUARTZ)
            .key('r', Items.REDSTONE)
            .addCriterion("has_nether_quartz", InventoryChangeTrigger.Instance.forItems(Items.QUARTZ))
            .build(consumer);

        // Power Socket = 3 Redstone Wire + 6 Coagulated Latex
        ShapedRecipeBuilder.shapedRecipe(ModItems.POWER_SOCKET.get())
            .patternLine("wlw")
            .patternLine("lll")
            .patternLine("lwl")
            .key('w', ModItems.REDSTONE_WIRE.get())
            .key('l', ModItems.COAGULATED_LATEX.get())
            .addCriterion("has_redstone_wire", InventoryChangeTrigger.Instance.forItems(ModItems.REDSTONE_WIRE.get()))
            .build(consumer);

        // Grinder Wheel Tier 0 = Coupling + 4 Planks
        ShapedRecipeBuilder.shapedRecipe(ModItems.GRINDER_WHEEL_TIER0.get())
            .patternLine(" w ")
            .patternLine("wcw")
            .patternLine(" w ")
            .key('c', ModItems.COUPLING.get())
            .key('w', ItemTags.PLANKS)
            .addCriterion("has_coupling", InventoryChangeTrigger.Instance.forItems(ModItems.COUPLING.get()))
            .build(consumer);

        // Grinder Wheel Tier 1 = Coupling + 4 Cobblestone
        ShapedRecipeBuilder.shapedRecipe(ModItems.GRINDER_WHEEL_TIER1.get())
            .patternLine(" s ")
            .patternLine("scs")
            .patternLine(" s ")
            .key('c', ModItems.COUPLING.get())
            .key('s', ItemTags.STONE_CRAFTING_MATERIALS)
            .addCriterion("has_coupling", InventoryChangeTrigger.Instance.forItems(ModItems.COUPLING.get()))
            .build(consumer);

        // Grinder Wheel Tier 2 = Coupling + 4 Iron Ingot
        ShapedRecipeBuilder.shapedRecipe(ModItems.GRINDER_WHEEL_TIER2.get())
            .patternLine(" i ")
            .patternLine("ici")
            .patternLine(" i ")
            .key('c', ModItems.COUPLING.get())
            .key('i', Items.IRON_INGOT)
            .addCriterion("has_coupling", InventoryChangeTrigger.Instance.forItems(ModItems.COUPLING.get()))
            .build(consumer);

        // Grinder Wheel Tier 3 = Coupling + 4 Diamond
        ShapedRecipeBuilder.shapedRecipe(ModItems.GRINDER_WHEEL_TIER3.get())
            .patternLine(" d ")
            .patternLine("dcd")
            .patternLine(" d ")
            .key('c', ModItems.COUPLING.get())
            .key('d', Items.DIAMOND)
            .addCriterion("has_coupling", InventoryChangeTrigger.Instance.forItems(ModItems.COUPLING.get()))
            .build(consumer);

        // Grinder Wheel Tier 4 = Coupling + 4 Netherite Ingot
        ShapedRecipeBuilder.shapedRecipe(ModItems.GRINDER_WHEEL_TIER4.get())
            .patternLine(" n ")
            .patternLine("ncn")
            .patternLine(" n ")
            .key('c', ModItems.COUPLING.get())
            .key('n', Items.NETHERITE_INGOT)
            .addCriterion("has_coupling", InventoryChangeTrigger.Instance.forItems(ModItems.COUPLING.get()))
            .build(consumer);

        // 3 Power Cable = 3 Rubber + 3 Redstone Wire
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.POWER_CABLE.get(), 3)
            .patternLine("rrr")
            .patternLine("www")
            .key('r', ModItems.RUBBER.get())
            .key('w', ModItems.REDSTONE_WIRE.get())
            .addCriterion("has_rubber", InventoryChangeTrigger.Instance.forItems(ModItems.RUBBER.get()))
            .build(consumer);

        // Solar Panel = 3 Daylight Detector + 2 Nether Quartz + Redstone Dust + 2 Wooden Slab
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.SOLAR_PANEL.get())
            .patternLine("ddd")
            .patternLine("qrq")
            .patternLine("wpw")
            .key('d', Items.DAYLIGHT_DETECTOR)
            .key('q', Items.QUARTZ)
            .key('r', Items.REDSTONE)
            .key('w', ItemTags.WOODEN_SLABS)
            .key('p', ModBlocks.POWER_CABLE.get())
            .addCriterion("has_daylight_detector", InventoryChangeTrigger.Instance.forItems(Items.DAYLIGHT_DETECTOR))
            .build(consumer);

        // Grinder = 2 Coupling + 2 Redstone Wire + 2 Stone + Power Cable
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.GRINDER.get())
            .patternLine("c c")
            .patternLine("w w")
            .patternLine("sps")
            .key('c', ModItems.COUPLING.get())
            .key('w', ModItems.REDSTONE_WIRE.get())
            .key('p', ModBlocks.POWER_CABLE.get())
            .key('s', Items.STONE)
            .addCriterion("has_coupling", InventoryChangeTrigger.Instance.forItems(ModItems.COUPLING.get()))
            .build(consumer);

        // Ball Mill = 1 Coupling + 3 Redstone Wire + 4 Stone + Power Cable
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.BALL_MILL.get())
            .patternLine("sws")
            .patternLine("wcw")
            .patternLine("sps")
            .key('c', ModItems.COUPLING.get())
            .key('w', ModItems.REDSTONE_WIRE.get())
            .key('p', ModBlocks.POWER_CABLE.get())
            .key('s', Items.STONE)
            .addCriterion("has_coupling", InventoryChangeTrigger.Instance.forItems(ModItems.COUPLING.get()))
            .build(consumer);

        // Milling Ball Tier 0


        // Milling Ball Tier 1 = Milling Ball Tier 0 + Cobblestone
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.MILLING_BALL_TIER1.get())
            .addIngredient(ModItems.MILLING_BALL_TIER0.get())
            .addIngredient(Items.COBBLESTONE)
            .addCriterion("has_milling_ball_tier0", InventoryChangeTrigger.Instance.forItems(ModItems.MILLING_BALL_TIER0.get()))
            .build(consumer);

        // Milling Ball Tier 2 = Milling Ball Tier 1 + Iron Ingot
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.MILLING_BALL_TIER2.get())
            .addIngredient(ModItems.MILLING_BALL_TIER1.get())
            .addIngredient(Items.IRON_INGOT)
            .addCriterion("has_milling_ball_tier1", InventoryChangeTrigger.Instance.forItems(ModItems.MILLING_BALL_TIER1.get()))
            .build(consumer);

        // Milling Ball Tier 3 = Milling Ball Tier 2 + Diamond
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.MILLING_BALL_TIER3.get())
            .addIngredient(ModItems.MILLING_BALL_TIER2.get())
            .addIngredient(Items.DIAMOND)
            .addCriterion("has_milling_ball_tier2", InventoryChangeTrigger.Instance.forItems(ModItems.MILLING_BALL_TIER2.get()))
            .build(consumer);

        // Milling Ball Tier 4 = Milling Ball Tier 3 + Netherite Ingot
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.MILLING_BALL_TIER4.get())
            .addIngredient(ModItems.MILLING_BALL_TIER3.get())
            .addIngredient(Items.NETHERITE_INGOT)
            .addCriterion("has_milling_ball_tier3", InventoryChangeTrigger.Instance.forItems(ModItems.MILLING_BALL_TIER3.get()))
            .build(consumer);

        List<OreProcessingRecipeData> grinderOres = Arrays.asList(
            new OreProcessingRecipeData(Items.COAL_ORE, ItemTier.WOOD, ModItems.GROUND_COAL_ORE_ROUGH.get(), Items.COAL),
            new OreProcessingRecipeData(Items.DIAMOND_ORE, ItemTier.IRON, ModItems.GROUND_DIAMOND_ORE_ROUGH.get(), Items.DIAMOND),
            new OreProcessingRecipeData(Items.EMERALD_ORE, ItemTier.IRON, ModItems.GROUND_EMERALD_ORE_ROUGH.get(), Items.EMERALD),
            new OreProcessingRecipeData(Items.GOLD_ORE, ItemTier.IRON, ModItems.GROUND_GOLD_ORE_ROUGH.get(), Items.GOLD_INGOT),
            new OreProcessingRecipeData(Items.IRON_ORE, ItemTier.STONE, ModItems.GROUND_IRON_ORE_ROUGH.get(), Items.IRON_INGOT),
            new OreProcessingRecipeData(Items.LAPIS_ORE, ItemTier.STONE, ModItems.GROUND_LAPIS_ORE_ROUGH.get(), Items.LAPIS_LAZULI),
            new OreProcessingRecipeData(Items.NETHER_QUARTZ_ORE, ItemTier.WOOD, ModItems.GROUND_NETHER_QUARTZ_ORE_ROUGH.get(), Items.QUARTZ),
            new OreProcessingRecipeData(Items.REDSTONE_ORE, ItemTier.IRON, ModItems.GROUND_REDSTONE_ORE_ROUGH.get(), Items.REDSTONE),
            new OreProcessingRecipeData(Items.ANCIENT_DEBRIS, ItemTier.DIAMOND, ModItems.GROUND_ANCIENT_DEBRIS_ROUGH.get(), Items.NETHERITE_SCRAP)
            );

        grinderOres.stream().forEach(data -> {
            String inputName = ForgeRegistries.ITEMS.getKey(data.getInput()).getPath();
            String outputName = ForgeRegistries.ITEMS.getKey(data.getOutput()).getPath();
            String processedName = ForgeRegistries.ITEMS.getKey(data.getProcessed()).getPath();

            // Ore -> Ground Ore (Rough)
            ManufactoryRecipeBuilder.grinderRecipe(Ingredient.fromItems(data.getInput()), data.getOutput())
                .withExtraChance(0.3f, data.getExtraAmounts())
                .withTierRequired(data.getTier().getHarvestLevel())
                .addCriterion(String.format("has_%s", inputName), InventoryChangeTrigger.Instance.forItems(data.getInput()))
                .build(consumer);

            // Ground Ore (Rough) -> Ingot
            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(data.getOutput()), data.getProcessed(), 0.2f, 200)
                .addCriterion(String.format("has_%s", outputName), InventoryChangeTrigger.Instance.forItems(data.getOutput()))
                .build(consumer, String.format("%s:%s_from_ground_ore_rough", Reference.MOD_ID, processedName));
        });

        // Nether Gold Ore -> Ground Gold Ore (Rough)
        ManufactoryRecipeBuilder.grinderRecipe(Ingredient.fromItems(Items.NETHER_GOLD_ORE), ModItems.GROUND_GOLD_ORE_ROUGH.get())
            .withExtraChance(0.3f, 1)
            .withTierRequired(ItemTier.WOOD.getHarvestLevel())
            .addCriterion("has_nether_gold_ore", InventoryChangeTrigger.Instance.forItems(Items.NETHER_GOLD_ORE))
            .build(consumer, String.format("%s:ground_gold_ore_rough_from_nether_gold_ore", Reference.MOD_ID));

        List<OreProcessingRecipeData> ballMillOres = Arrays.asList(
            new OreProcessingRecipeData(ModItems.GROUND_COAL_ORE_ROUGH.get(), ItemTier.WOOD, ModItems.GROUND_COAL_ORE_FINE.get(), Items.COAL),
            new OreProcessingRecipeData(ModItems.GROUND_DIAMOND_ORE_ROUGH.get(), ItemTier.IRON, ModItems.GROUND_DIAMOND_ORE_FINE.get(), Items.DIAMOND),
            new OreProcessingRecipeData(ModItems.GROUND_EMERALD_ORE_ROUGH.get(), ItemTier.IRON, ModItems.GROUND_EMERALD_ORE_FINE.get(), Items.EMERALD),
            new OreProcessingRecipeData(ModItems.GROUND_GOLD_ORE_ROUGH.get(), ItemTier.IRON, ModItems.GROUND_GOLD_ORE_FINE.get(), Items.GOLD_INGOT),
            new OreProcessingRecipeData(ModItems.GROUND_IRON_ORE_ROUGH.get(), ItemTier.STONE, ModItems.GROUND_IRON_ORE_FINE.get(), Items.IRON_INGOT),
            new OreProcessingRecipeData(ModItems.GROUND_LAPIS_ORE_ROUGH.get(), ItemTier.STONE, ModItems.GROUND_LAPIS_ORE_FINE.get(), Items.LAPIS_LAZULI),
            new OreProcessingRecipeData(ModItems.GROUND_NETHER_QUARTZ_ORE_ROUGH.get(), ItemTier.WOOD, ModItems.GROUND_NETHER_QUARTZ_ORE_FINE.get(), Items.QUARTZ),
            new OreProcessingRecipeData(ModItems.GROUND_REDSTONE_ORE_ROUGH.get(), ItemTier.IRON, ModItems.GROUND_REDSTONE_ORE_FINE.get(), Items.REDSTONE),
            new OreProcessingRecipeData(ModItems.GROUND_ANCIENT_DEBRIS_ROUGH.get(), ItemTier.DIAMOND, ModItems.GROUND_ANCIENT_DEBRIS_FINE.get(), Items.NETHERITE_SCRAP)
            );

        ballMillOres.stream().forEach(data -> {
            String inputName = ForgeRegistries.ITEMS.getKey(data.getInput()).getPath();
            String outputName = ForgeRegistries.ITEMS.getKey(data.getOutput()).getPath();
            String processedName = ForgeRegistries.ITEMS.getKey(data.getProcessed()).getPath();

            // Ground Ore (Rough) -> Ground Ore (Fine)
            ManufactoryRecipeBuilder.ballMillRecipe(Ingredient.fromItems(data.getInput()), data.getOutput())
                .withExtraChance(0.54f, data.getExtraAmounts())
                .withTierRequired(data.getTier().getHarvestLevel())
                .addCriterion(String.format("has_%s", inputName), InventoryChangeTrigger.Instance.forItems(data.getInput()))
                .build(consumer);

            // Ground Ore (Fine) -> Ingot
            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(data.getOutput()), data.getProcessed(), 0.2f, 200)
                .addCriterion(String.format("has_%s", outputName), InventoryChangeTrigger.Instance.forItems(data.getOutput()))
                .build(consumer, String.format("%s:%s_from_ground_ore_fine", Reference.MOD_ID, processedName));
        });
    }

    private static class OreProcessingRecipeData {
        private Item input;
        private ItemTier tier;
        private Item output;
        private Item processed;
        private int[] extraAmounts;

        public OreProcessingRecipeData(Item input, ItemTier tier, Item output, Item processed) {
            this(input, tier, output, processed, new int[]{1});
        }

        public OreProcessingRecipeData(Item input, ItemTier tier, Item output, Item processed, int[] extraAmounts) {
            this.input = input;
            this.tier = tier;
            this.output = output;
            this.processed = processed;
            this.extraAmounts = extraAmounts;
        }

        public Item getInput() {
            return input;
        }

        public ItemTier getTier() {
            return tier;
        }

        public Item getOutput() {
            return output;
        }

        public Item getProcessed() {
            return processed;
        }

        public int[] getExtraAmounts() {
            return extraAmounts;
        }
    }
}
