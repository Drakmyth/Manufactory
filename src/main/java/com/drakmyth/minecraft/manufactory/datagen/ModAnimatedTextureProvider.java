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
        texture(modLoc("block/test_fluid_still.png"), 2);
        texture(modLoc("block/test_fluid_flow.png"));
    }
}
