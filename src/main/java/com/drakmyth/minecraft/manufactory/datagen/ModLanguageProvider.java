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
        addBlock(ModBlocks.LATEX_COLLECTOR, "Latex Collector");
        addBlock(ModBlocks.POWER_CABLE, "Power Cable");
        addBlock(ModBlocks.SOLAR_PANEL, "Solar Panel");

        addItem(ModItems.AMBER, "Amber");
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
        addItem(ModItems.RUBBER, "Rubber");
        addItem(ModItems.TAPPING_KNIFE, "Tapping Knife");

        add("itemGroup.manufactory", "Manufactory");
    }
}
