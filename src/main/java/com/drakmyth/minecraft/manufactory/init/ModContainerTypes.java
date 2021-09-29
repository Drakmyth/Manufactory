/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.containers.BallMillContainer;
import com.drakmyth.minecraft.manufactory.containers.BallMillUpgradeContainer;
import com.drakmyth.minecraft.manufactory.containers.GrinderContainer;
import com.drakmyth.minecraft.manufactory.containers.GrinderUpgradeContainer;
import com.drakmyth.minecraft.manufactory.containers.RockDrillUpgradeContainer;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModContainerTypes {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Reference.MOD_ID);

    public static final RegistryObject<MenuType<GrinderContainer>> GRINDER = CONTAINER_TYPES.register("grinder", () -> IForgeContainerType.create(GrinderContainer::new));
    public static final RegistryObject<MenuType<GrinderUpgradeContainer>> GRINDER_UPGRADE = CONTAINER_TYPES.register("grinder_upgrade", () -> IForgeContainerType.create(GrinderUpgradeContainer::new));
    public static final RegistryObject<MenuType<BallMillContainer>> BALL_MILL = CONTAINER_TYPES.register("ball_mill", () -> IForgeContainerType.create(BallMillContainer::new));
    public static final RegistryObject<MenuType<BallMillUpgradeContainer>> BALL_MILL_UPGRADE = CONTAINER_TYPES.register("ball_mill_upgrade", () -> IForgeContainerType.create(BallMillUpgradeContainer::new));
    public static final RegistryObject<MenuType<RockDrillUpgradeContainer>> ROCK_DRILL_UPGRADE = CONTAINER_TYPES.register("rock_drill_upgrade", () -> IForgeContainerType.create(RockDrillUpgradeContainer::new));
}
