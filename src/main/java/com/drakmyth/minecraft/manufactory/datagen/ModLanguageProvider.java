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
        addItem(ModItems.RUBBER, "Rubber");
        addItem(ModItems.TAPPING_KNIFE, "Tapping Knife");

        add("itemGroup.manufactory", "Manufactory");
    }
}
