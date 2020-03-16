/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items;

public class TappingKnifeItem extends ManufactoryItem {

    public static final String REGISTRY_NAME = "tapping_knife";

    public TappingKnifeItem() {
        super(REGISTRY_NAME, defaultProperties().maxStackSize(1));
    }
}
