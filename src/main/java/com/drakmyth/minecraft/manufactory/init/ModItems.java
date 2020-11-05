/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.items.BatteryItem;
import com.drakmyth.minecraft.manufactory.items.MotorUpgradeItem;
import com.drakmyth.minecraft.manufactory.items.PowerSocketItem;
import com.drakmyth.minecraft.manufactory.items.TappingKnifeItem;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static final RegistryObject<Item> AMBER = ITEMS.register("amber", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> MOTOR_TIER0 = ITEMS.register("motor_tier0", () -> new MotorUpgradeItem(defaultProperties(), 1.0f));
    public static final RegistryObject<Item> MOTOR_TIER1 = ITEMS.register("motor_tier1", () -> new MotorUpgradeItem(defaultProperties(), 2.0f));
    public static final RegistryObject<Item> MOTOR_TIER2 = ITEMS.register("motor_tier2", () -> new MotorUpgradeItem(defaultProperties(), 4.0f));
    public static final RegistryObject<Item> MOTOR_TIER3 = ITEMS.register("motor_tier3", () -> new MotorUpgradeItem(defaultProperties(), 8.0f));
    public static final RegistryObject<Item> POWER_SOCKET = ITEMS.register("power_socket", () -> new PowerSocketItem(defaultProperties()));
    public static final RegistryObject<Item> BATTERY = ITEMS.register("battery", () -> new BatteryItem(defaultProperties()));
    public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> COAGULATED_LATEX = ITEMS.register("coagulated_latex", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> GROUND_COAL_ORE_ROUGH = ITEMS.register("ground_coal_ore_rough", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> GROUND_DIAMOND_ORE_ROUGH = ITEMS.register("ground_diamond_ore_rough", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> GROUND_EMERALD_ORE_ROUGH = ITEMS.register("ground_emerald_ore_rough", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> GROUND_GOLD_ORE_ROUGH = ITEMS.register("ground_gold_ore_rough", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> GROUND_IRON_ORE_ROUGH = ITEMS.register("ground_iron_ore_rough", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> GROUND_LAPIS_ORE_ROUGH = ITEMS.register("ground_lapis_ore_rough", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> GROUND_NETHER_QUARTZ_ORE_ROUGH = ITEMS.register("ground_nether_quartz_ore_rough", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> GROUND_REDSTONE_ORE_ROUGH = ITEMS.register("ground_redstone_ore_rough", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> GROUND_ANCIENT_DEBRIS_ROUGH = ITEMS.register("ground_ancient_debris_rough", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> RUBBER = ITEMS.register("rubber", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> TAPPING_KNIFE = ITEMS.register("tapping_knife", () -> new TappingKnifeItem(defaultProperties().maxStackSize(1)));

    private static Item.Properties defaultProperties() {
        return new Item.Properties().maxStackSize(64).group(ModItemGroups.MANUFACTORY_ITEM_GROUP);
    }
}
