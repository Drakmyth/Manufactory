/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */
package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.item.ItemTier;

public interface IGrinderWheelUpgrade {
    ItemTier getTier();
    float getEfficiency();
}
