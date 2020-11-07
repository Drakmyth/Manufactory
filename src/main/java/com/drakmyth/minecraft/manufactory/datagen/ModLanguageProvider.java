/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModItems;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(DataGenerator gen, String locale) {
        super(gen, Reference.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        addBlock(ModBlocks.AMBER_BLOCK, "Amber Block");
        addBlock(ModBlocks.GRINDER, "Grinder");
        addBlock(ModBlocks.BALL_MILL, "Ball Mill");
        addBlock(ModBlocks.LATEX_COLLECTOR, "Latex Collector");
        addBlock(ModBlocks.POWER_CABLE, "Power Cable");
        addBlock(ModBlocks.SOLAR_PANEL, "Solar Panel");

        addItem(ModItems.AMBER, "Amber");
        addItem(ModItems.MOTOR_TIER0, "Basic Motor");
        addItem(ModItems.MOTOR_TIER1, "Magnetic Motor");
        addItem(ModItems.MOTOR_TIER2, "Compound Motor");
        addItem(ModItems.MOTOR_TIER3, "Induction Motor");
        addItem(ModItems.GRINDER_WHEEL_TIER0, "Wooden Grinder Wheel");
        addItem(ModItems.GRINDER_WHEEL_TIER1, "Stone Grinder Wheel");
        addItem(ModItems.GRINDER_WHEEL_TIER2, "Iron Grinder Wheel");
        addItem(ModItems.GRINDER_WHEEL_TIER3, "Diamond Grinder Wheel");
        addItem(ModItems.GRINDER_WHEEL_TIER4, "Netherite Grinder Wheel");
        addItem(ModItems.MILLING_BALL_TIER0, "Wooden Milling Ball");
        addItem(ModItems.MILLING_BALL_TIER1, "Stone Milling Ball");
        addItem(ModItems.MILLING_BALL_TIER2, "Iron Milling Ball");
        addItem(ModItems.MILLING_BALL_TIER3, "Diamond Milling Ball");
        addItem(ModItems.MILLING_BALL_TIER4, "Netherite Milling Ball");
        addItem(ModItems.POWER_SOCKET, "Power Socket");
        addItem(ModItems.BATTERY, "Battery");
        addItem(ModItems.WRENCH, "Wrench");
        addItem(ModItems.COAGULATED_LATEX, "Coagulated Latex");
        addItem(ModItems.GROUND_COAL_ORE_ROUGH, "Ground Coal Ore (Rough)");
        addItem(ModItems.GROUND_DIAMOND_ORE_ROUGH, "Ground Diamond Ore (Rough)");
        addItem(ModItems.GROUND_EMERALD_ORE_ROUGH, "Ground Emerald Ore (Rough)");
        addItem(ModItems.GROUND_GOLD_ORE_ROUGH, "Ground Gold Ore (Rough)");
        addItem(ModItems.GROUND_IRON_ORE_ROUGH, "Ground Iron Ore (Rough)");
        addItem(ModItems.GROUND_LAPIS_ORE_ROUGH, "Ground Lapis Lazuli Ore (Rough)");
        addItem(ModItems.GROUND_NETHER_QUARTZ_ORE_ROUGH, "Ground Nether Quartz Ore (Rough)");
        addItem(ModItems.GROUND_REDSTONE_ORE_ROUGH, "Ground Redstone Ore (Rough)");
        addItem(ModItems.GROUND_ANCIENT_DEBRIS_ROUGH, "Ground Ancient Debris (Rough)");
        addItem(ModItems.GROUND_COAL_ORE_FINE, "Ground Coal Ore (Fine)");
        addItem(ModItems.GROUND_DIAMOND_ORE_FINE, "Ground Diamond Ore (Fine)");
        addItem(ModItems.GROUND_EMERALD_ORE_FINE, "Ground Emerald Ore (Fine)");
        addItem(ModItems.GROUND_GOLD_ORE_FINE, "Ground Gold Ore (Fine)");
        addItem(ModItems.GROUND_IRON_ORE_FINE, "Ground Iron Ore (Fine)");
        addItem(ModItems.GROUND_LAPIS_ORE_FINE, "Ground Lapis Lazuli Ore (Fine)");
        addItem(ModItems.GROUND_NETHER_QUARTZ_ORE_FINE, "Ground Nether Quartz Ore (Fine)");
        addItem(ModItems.GROUND_REDSTONE_ORE_FINE, "Ground Redstone Ore (Fine)");
        addItem(ModItems.GROUND_ANCIENT_DEBRIS_FINE, "Ground Ancient Debris (Fine)");
        addItem(ModItems.RUBBER, "Rubber");
        addItem(ModItems.TAPPING_KNIFE, "Tapping Knife");

        add("itemGroup.manufactory", "Manufactory");
    }
}
