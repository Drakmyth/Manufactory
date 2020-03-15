/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory;

import com.drakmyth.minecraft.manufactory.items.ModItems;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public abstract class ModItemGroups {

    public static ItemGroup MANUFACTORY = new ItemGroup("manufactory") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.TAPPING_KNIFE);
        }
    };
}
