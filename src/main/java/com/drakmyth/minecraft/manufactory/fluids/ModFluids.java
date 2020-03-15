/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.fluids;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ObjectHolder;

public abstract class ModFluids {

    @ObjectHolder(Reference.MOD_ID + ":" + LatexFluid.REGISTRY_NAME)
    public static ForgeFlowingFluid.Source LATEX;

    @ObjectHolder(Reference.MOD_ID + ":" + LatexFluid.REGISTRY_NAME + "_flowing")
    public static ForgeFlowingFluid.Flowing LATEX_FLOWING;
}
