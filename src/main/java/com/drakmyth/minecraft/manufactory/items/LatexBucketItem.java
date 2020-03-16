/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items;

import com.drakmyth.minecraft.manufactory.fluids.ModFluids;

public class LatexBucketItem extends ManufactoryBucketItem {

    public static final String REGISTRY_NAME = "latex_bucket";

    public LatexBucketItem() {
        super(REGISTRY_NAME, () -> ModFluids.LATEX, defaultProperties());
    }
}
