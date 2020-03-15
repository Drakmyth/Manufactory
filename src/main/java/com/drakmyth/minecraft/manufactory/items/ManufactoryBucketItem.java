/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items;

import java.util.function.Supplier;

import com.drakmyth.minecraft.manufactory.ModItemGroups;
import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Items;

public abstract class ManufactoryBucketItem extends BucketItem {

    public ManufactoryBucketItem(final String registryName, final Supplier<? extends Fluid> fluid, final Properties properties) {
        super(fluid, properties.maxStackSize(1).containerItem(Items.BUCKET).group(ModItemGroups.MANUFACTORY));
        setRegistryName(Reference.MOD_ID, registryName);
    }
}
