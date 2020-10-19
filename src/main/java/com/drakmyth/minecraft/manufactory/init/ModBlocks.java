/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);

    public static final RegistryObject<Block> LATEX_COLLECTOR = BLOCKS.register("latex_collector", () -> new LatexCollectorBlock(Block.Properties.create(Material.IRON)));

    public static final Map<RegistryObject<Block>, Item.Properties> BLOCKITEM_PROPS = Stream.of(
        new SimpleEntry<>(LATEX_COLLECTOR, defaultBlockItemProps().maxStackSize(16))
    ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

    public static Item.Properties defaultBlockItemProps() {
        return new Item.Properties().maxStackSize(64).group(ModItemGroups.MANUFACTORY_ITEM_GROUP);
    }
}
