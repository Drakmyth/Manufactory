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

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModContainerTypes {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Reference.MOD_ID);

    public static final RegistryObject<ContainerType<GrinderContainer>> GRINDER = CONTAINER_TYPES.register("grinder", () -> IForgeContainerType.create(GrinderContainer::new));
    public static final RegistryObject<ContainerType<GrinderUpgradeContainer>> GRINDER_UPGRADE = CONTAINER_TYPES.register("grinder_upgrade", () -> IForgeContainerType.create(GrinderUpgradeContainer::new));
    public static final RegistryObject<ContainerType<BallMillContainer>> BALL_MILL = CONTAINER_TYPES.register("ball_mill", () -> IForgeContainerType.create(BallMillContainer::new));
    public static final RegistryObject<ContainerType<BallMillUpgradeContainer>> BALL_MILL_UPGRADE = CONTAINER_TYPES.register("ball_mill_upgrade", () -> IForgeContainerType.create(BallMillUpgradeContainer::new));
}
