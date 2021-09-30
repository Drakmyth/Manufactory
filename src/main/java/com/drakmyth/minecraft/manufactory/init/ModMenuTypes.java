/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.menus.BallMillMenu;
import com.drakmyth.minecraft.manufactory.menus.BallMillUpgradeMenu;
import com.drakmyth.minecraft.manufactory.menus.GrinderMenu;
import com.drakmyth.minecraft.manufactory.menus.GrinderUpgradeMenu;
import com.drakmyth.minecraft.manufactory.menus.RockDrillUpgradeMenu;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Reference.MOD_ID);

    public static final RegistryObject<MenuType<GrinderMenu>> GRINDER = MENU_TYPES.register("grinder", () -> IForgeContainerType.create(GrinderMenu::new));
    public static final RegistryObject<MenuType<GrinderUpgradeMenu>> GRINDER_UPGRADE = MENU_TYPES.register("grinder_upgrade", () -> IForgeContainerType.create(GrinderUpgradeMenu::new));
    public static final RegistryObject<MenuType<BallMillMenu>> BALL_MILL = MENU_TYPES.register("ball_mill", () -> IForgeContainerType.create(BallMillMenu::new));
    public static final RegistryObject<MenuType<BallMillUpgradeMenu>> BALL_MILL_UPGRADE = MENU_TYPES.register("ball_mill_upgrade", () -> IForgeContainerType.create(BallMillUpgradeMenu::new));
    public static final RegistryObject<MenuType<RockDrillUpgradeMenu>> ROCK_DRILL_UPGRADE = MENU_TYPES.register("rock_drill_upgrade", () -> IForgeContainerType.create(RockDrillUpgradeMenu::new));
}
