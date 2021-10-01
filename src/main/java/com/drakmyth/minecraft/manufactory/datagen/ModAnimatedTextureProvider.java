/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModAnimatedTextureProvider extends AnimatedTextureProvider {

    public ModAnimatedTextureProvider(DataGenerator generator, ExistingFileHelper exFileHelper) {
        super(generator, Reference.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerAnimatedTextures() {
        registerOreFluid("slurried_coal_ore");
        registerOreFluid("slurried_diamond_ore");
        registerOreFluid("slurried_emerald_ore");
        registerOreFluid("slurried_gold_ore");
        registerOreFluid("slurried_iron_ore");
        registerOreFluid("slurried_copper_ore");
        registerOreFluid("slurried_lapis_ore");
        registerOreFluid("slurried_nether_quartz_ore");
        registerOreFluid("slurried_redstone_ore");
        registerOreFluid("slurried_ancient_debris");
    }

    private void registerOreFluid(String name) {
        getBuilder(modLoc("block/" + name + "_still")).frametime(2)
            .frames(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        getBuilder(modLoc("block/" + name + "_flow")).frametime(3);
    }
}
