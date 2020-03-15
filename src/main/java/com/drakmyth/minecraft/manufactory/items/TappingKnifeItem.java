/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items;

import net.minecraft.item.Item;

public class TappingKnifeItem extends ManufactoryItem {

    public static final String REGISTRY_NAME = "tapping_knife";

    public TappingKnifeItem() {
        super(REGISTRY_NAME, 1, new Item.Properties());
    }

}
