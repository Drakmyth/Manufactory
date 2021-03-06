/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModAnimatedTextureProvider extends AnimatedTextureProvider {

    public ModAnimatedTextureProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Reference.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerAnimatedTextures() {
        getBuilder(modLoc("block/slurried_coal_ore_still")).frametime(2)
            .frames(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        getBuilder(modLoc("block/slurried_coal_ore_flow")).frametime(3);
        getBuilder(modLoc("block/slurried_diamond_ore_still")).frametime(2)
            .frames(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        getBuilder(modLoc("block/slurried_diamond_ore_flow")).frametime(3);
        getBuilder(modLoc("block/slurried_emerald_ore_still")).frametime(2)
            .frames(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        getBuilder(modLoc("block/slurried_emerald_ore_flow")).frametime(3);
        getBuilder(modLoc("block/slurried_gold_ore_still")).frametime(2)
            .frames(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        getBuilder(modLoc("block/slurried_gold_ore_flow")).frametime(3);
        getBuilder(modLoc("block/slurried_iron_ore_still")).frametime(2)
            .frames(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        getBuilder(modLoc("block/slurried_iron_ore_flow")).frametime(3);
        getBuilder(modLoc("block/slurried_lapis_ore_still")).frametime(2)
            .frames(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        getBuilder(modLoc("block/slurried_lapis_ore_flow")).frametime(3);
        getBuilder(modLoc("block/slurried_nether_quartz_ore_still")).frametime(2)
            .frames(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        getBuilder(modLoc("block/slurried_nether_quartz_ore_flow")).frametime(3);
        getBuilder(modLoc("block/slurried_redstone_ore_still")).frametime(2)
            .frames(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        getBuilder(modLoc("block/slurried_redstone_ore_flow")).frametime(3);
        getBuilder(modLoc("block/slurried_ancient_debris_still")).frametime(2)
            .frames(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        getBuilder(modLoc("block/slurried_ancient_debris_flow")).frametime(3);
    }
}
