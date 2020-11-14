/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import java.util.function.Supplier;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.FluidAttributes.Builder;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Reference.MOD_ID);

    public static final RegistryObject<FlowingFluid> SLURRIED_COAL_ORE = FLUIDS.register("slurried_coal_ore", () -> new ForgeFlowingFluid.Source(slurriedCoalOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_COAL_ORE_FLOWING = FLUIDS.register("flowing_slurried_coal_ore", () -> new ForgeFlowingFluid.Flowing(slurriedCoalOreProperties()));

    private static Properties slurriedCoalOreProperties() {
        ResourceLocation stillResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_coal_ore_still");
        ResourceLocation flowResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_coal_ore_flow");

        Supplier<Fluid> stillSupplier = () -> SLURRIED_COAL_ORE.get();
        Supplier<Fluid> flowSupplier = () -> SLURRIED_COAL_ORE_FLOWING.get();
        Supplier<FlowingFluidBlock> blockSupplier = () -> ModBlocks.SLURRIED_COAL_ORE.get();
        Supplier<Item> bucketSupplier = () -> ModItems.SLURRIED_COAL_ORE_BUCKET.get();

        Builder attributes = FluidAttributes.builder(stillResource, flowResource);
        return new Properties(stillSupplier, flowSupplier, attributes).block(blockSupplier).bucket(bucketSupplier);
    }
}
