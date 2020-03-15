/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.fluids;

import java.util.function.Supplier;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class ManufactoryFluid {

    public final ForgeFlowingFluid.Source SOURCE;
    public final ForgeFlowingFluid.Flowing FLOWING;

    public ManufactoryFluid(final String registryName, final Supplier<? extends FlowingFluidBlock> block, final Supplier<? extends Item> bucket) {
        ForgeFlowingFluid.Properties fluidProps = createFluidProperties(registryName, block, bucket);

        SOURCE = new ManufactoryStillFluid(registryName, fluidProps);
        FLOWING = new ManufactoryFlowingFluid(registryName, fluidProps);
    }

    private ForgeFlowingFluid.Properties createFluidProperties(final String registryName, final Supplier<? extends FlowingFluidBlock> block, final Supplier<? extends Item> bucket) {
        ResourceLocation stillRL = new ResourceLocation(Reference.MOD_ID, "fluid/" + registryName + "_still");
        ResourceLocation flowingRL = new ResourceLocation(Reference.MOD_ID, "fluid/" + registryName + "_flow");
        FluidAttributes.Builder fluidBuilder = FluidAttributes.builder(stillRL, flowingRL);
        return new ForgeFlowingFluid.Properties(() -> SOURCE, () -> FLOWING, fluidBuilder).block(block).bucket(bucket);
    }

    private class ManufactoryStillFluid extends ForgeFlowingFluid.Source {

        public ManufactoryStillFluid(final String registryName, final Properties properties) {
            super(properties);
            setRegistryName(Reference.MOD_ID, registryName);
        }
    }

    private class ManufactoryFlowingFluid extends ForgeFlowingFluid.Flowing {

        public ManufactoryFlowingFluid(final String registryName, final Properties properties) {
            super(properties);
            setRegistryName(Reference.MOD_ID, registryName + "_flowing");
        }
    }
}
