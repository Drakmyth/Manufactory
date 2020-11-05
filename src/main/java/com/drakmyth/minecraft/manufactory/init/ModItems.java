/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.items.upgrades.BatteryItem;
import com.drakmyth.minecraft.manufactory.items.upgrades.GrinderWheelUpgradeItem;
import com.drakmyth.minecraft.manufactory.items.upgrades.MotorUpgradeItem;
import com.drakmyth.minecraft.manufactory.items.upgrades.PowerSocketItem;
import com.drakmyth.minecraft.manufactory.items.TappingKnifeItem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static final RegistryObject<Item> AMBER = ITEMS.register("amber", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> MOTOR_TIER0 = ITEMS.register("motor_tier0", () -> new MotorUpgradeItem(singleItemProperties(), 1.0f));
    public static final RegistryObject<Item> MOTOR_TIER1 = ITEMS.register("motor_tier1", () -> new MotorUpgradeItem(singleItemProperties(), 2.0f));
    public static final RegistryObject<Item> MOTOR_TIER2 = ITEMS.register("motor_tier2", () -> new MotorUpgradeItem(singleItemProperties(), 4.0f));
    public static final RegistryObject<Item> MOTOR_TIER3 = ITEMS.register("motor_tier3", () -> new MotorUpgradeItem(singleItemProperties(), 8.0f));
    public static final RegistryObject<Item> GRINDER_WHEEL_TIER0 = ITEMS.register("grinder_wheel_tier0", () -> new GrinderWheelUpgradeItem(singleItemProperties(), ItemTier.WOOD, 0f));
    public static final RegistryObject<Item> GRINDER_WHEEL_TIER1 = ITEMS.register("grinder_wheel_tier1", () -> new GrinderWheelUpgradeItem(singleItemProperties(), ItemTier.STONE, 0.25f));
    public static final RegistryObject<Item> GRINDER_WHEEL_TIER2 = ITEMS.register("grinder_wheel_tier2", () -> new GrinderWheelUpgradeItem(singleItemProperties(), ItemTier.IRON, 0.5f));
    public static final RegistryObject<Item> GRINDER_WHEEL_TIER3 = ITEMS.register("grinder_wheel_tier3", () -> new GrinderWheelUpgradeItem(singleItemProperties(), ItemTier.DIAMOND, 0.75f));
    public static final RegistryObject<Item> GRINDER_WHEEL_TIER4 = ITEMS.register("grinder_wheel_tier4", () -> new GrinderWheelUpgradeItem(singleItemProperties(), ItemTier.NETHERITE, 1f));
    public static final RegistryObject<Item> POWER_SOCKET = ITEMS.register("power_socket", () -> new PowerSocketItem(singleItemProperties()));
    public static final RegistryObject<Item> BATTERY = ITEMS.register("battery", () -> new BatteryItem(singleItemProperties()));
    public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", () -> new Item(singleItemProperties()));
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
    public static final RegistryObject<Item> TAPPING_KNIFE = ITEMS.register("tapping_knife", () -> new TappingKnifeItem(singleItemProperties()));

    private static Item.Properties singleItemProperties() {
        return defaultProperties().maxStackSize(1);
    }

    private static Item.Properties defaultProperties() {
        return new Item.Properties().maxStackSize(64).group(ModItemGroups.MANUFACTORY_ITEM_GROUP);
    }
}
