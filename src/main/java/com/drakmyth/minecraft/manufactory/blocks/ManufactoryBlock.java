/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import com.drakmyth.minecraft.manufactory.ModItemGroups;
import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public abstract class ManufactoryBlock extends Block {

    public final Item BLOCK_ITEM;

    public ManufactoryBlock(final String registryName, final Properties blockProperties, final Item.Properties itemProperties) {
        super(blockProperties);
        setRegistryName(Reference.MOD_ID, registryName);

        BLOCK_ITEM = new BlockItem(this, itemProperties).setRegistryName(Reference.MOD_ID, registryName);
    }

    protected static Properties defaultBlockProperties(final Material material) {
        return Properties.create(material);
    }

    protected static Item.Properties defaultItemProperties() {
        return new Item.Properties().maxStackSize(64).group(ModItemGroups.MANUFACTORY);
    }
}
