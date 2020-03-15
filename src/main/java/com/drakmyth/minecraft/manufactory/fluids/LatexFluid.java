/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.fluids;

import com.drakmyth.minecraft.manufactory.blocks.ModBlocks;
import com.drakmyth.minecraft.manufactory.items.ModItems;

public class LatexFluid extends ManufactoryFluid {

    public static final String REGISTRY_NAME = "latex";

    public LatexFluid() {
        super(REGISTRY_NAME, () -> ModBlocks.LATEX, () -> ModItems.LATEX_BUCKET);
    }

}
