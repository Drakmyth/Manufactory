/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.data.DataGenerator;

public class ModAnimatedTextureProvider extends AnimatedTextureProvider {

    public ModAnimatedTextureProvider(DataGenerator gen) {
        super(gen, Reference.MOD_ID);
    }

    @Override
    protected void registerAnimatedTextures() {
        getBuilder(modLoc("block/slurried_coal_ore_still.png")).frametime(2)
            .frames(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        getBuilder(modLoc("block/slurried_coal_ore_flow.png")).frametime(3);
    }
}
