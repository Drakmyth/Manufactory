/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
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
}
