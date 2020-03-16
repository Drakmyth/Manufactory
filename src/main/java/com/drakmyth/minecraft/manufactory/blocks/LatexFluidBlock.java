/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import com.drakmyth.minecraft.manufactory.fluids.ModFluids;

public class LatexFluidBlock extends ManufactoryFlowingFluidBlock {

    public static final String REGISTRY_NAME = "latex";

    public LatexFluidBlock() {
        super(REGISTRY_NAME, () -> ModFluids.LATEX, defaultProperties());
    }
}
