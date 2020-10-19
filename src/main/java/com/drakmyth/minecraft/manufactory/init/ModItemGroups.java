/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public final class ModItemGroups {

    public static final ItemGroup MANUFACTORY_ITEM_GROUP = new ItemGroup("manufactory") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.TAPPING_KNIFE.get());
        }
    };
}
