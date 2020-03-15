/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraftforge.registries.ObjectHolder;

public abstract class ModBlocks {

    @ObjectHolder(Reference.MOD_ID + ":" + LatexFluidBlock.REGISTRY_NAME)
    public static LatexFluidBlock LATEX;
}
