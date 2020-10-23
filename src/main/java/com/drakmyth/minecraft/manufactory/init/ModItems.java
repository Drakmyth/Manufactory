/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.items.TappingKnifeItem;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static final RegistryObject<Item> AMBER = ITEMS.register("amber", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> COAGULATED_LATEX = ITEMS.register("coagulated_latex", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> RUBBER = ITEMS.register("rubber", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> TAPPING_KNIFE = ITEMS.register("tapping_knife", () -> new TappingKnifeItem(defaultProperties().maxStackSize(1)));

    private static Item.Properties defaultProperties() {
        return new Item.Properties().maxStackSize(64).group(ModItemGroups.MANUFACTORY_ITEM_GROUP);
    }
}
