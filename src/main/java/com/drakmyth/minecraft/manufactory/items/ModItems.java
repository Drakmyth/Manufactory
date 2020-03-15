/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraftforge.registries.ObjectHolder;

public class ModItems {
    @ObjectHolder(Reference.MOD_ID + ":" + LatexCollectorItem.REGISTRY_NAME)
    public static LatexCollectorItem LATEX_COLLECTOR;

    @ObjectHolder(Reference.MOD_ID + ":" + TappingKnifeItem.REGISTRY_NAME)
    public static TappingKnifeItem TAPPING_KNIFE;
}
