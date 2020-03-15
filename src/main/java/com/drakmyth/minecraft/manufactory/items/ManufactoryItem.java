/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items;

import com.drakmyth.minecraft.manufactory.ModItemGroups;
import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.item.Item;

public abstract class ManufactoryItem extends Item {

    public ManufactoryItem(final String registryName, final int stackSize, final Properties properties) {
        super(properties.maxStackSize(stackSize).group(ModItemGroups.MANUFACTORY));
        setRegistryName(Reference.MOD_ID, registryName);
    }

}
