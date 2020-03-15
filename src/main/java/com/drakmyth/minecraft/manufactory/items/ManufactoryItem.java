/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ManufactoryItem extends Item {

    public ManufactoryItem(String registryName, int stackSize, Properties properties) {
        super(properties.maxStackSize(stackSize).group(ItemGroup.MISC));
        setRegistryName(Reference.MOD_ID, registryName);
    }

}
