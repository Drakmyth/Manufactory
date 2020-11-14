/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.FluidAttributes.Builder;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModFluids {
    private static final ResourceLocation TEST_FLUID_STILL = new ResourceLocation(Reference.MOD_ID, "block/slurried_coal_ore_still");
    private static final ResourceLocation TEST_FLUID_FLOWING = new ResourceLocation(Reference.MOD_ID, "block/slurried_coal_ore_flow");

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Reference.MOD_ID);

    public static final RegistryObject<FlowingFluid> TEST_FLUID = FLUIDS.register("test_fluid", () -> new ForgeFlowingFluid.Source(defaultProperties()));
    public static final RegistryObject<Fluid> TEST_FLOWING = FLUIDS.register("test_flowing", () -> new ForgeFlowingFluid.Flowing(defaultProperties()));

    private static Properties defaultProperties() {
        Builder attributes = FluidAttributes.builder(TEST_FLUID_STILL, TEST_FLUID_FLOWING);
        return new Properties(() -> TEST_FLUID.get(), () -> TEST_FLOWING.get(), attributes)
            .block(() -> ModBlocks.TEST_FLUID_BLOCK.get())
            .bucket(() -> ModItems.TEST_FLUID_BUCKET.get());
    }
}
