/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import java.util.function.Supplier;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;

public class ManufactoryFlowingFluidBlock extends FlowingFluidBlock {

    public ManufactoryFlowingFluidBlock(final String registryName, final Supplier<? extends FlowingFluid> fluid, final Properties properties) {
        super(fluid, properties);
        setRegistryName(Reference.MOD_ID, registryName);
    }

}
