/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import java.util.function.Supplier;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.FluidAttributes.Builder;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Reference.MOD_ID);

    public static final RegistryObject<FlowingFluid> SLURRIED_COAL_ORE = FLUIDS.register("slurried_coal_ore", () -> new ForgeFlowingFluid.Source(slurriedCoalOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_COAL_ORE_FLOWING = FLUIDS.register("flowing_slurried_coal_ore", () -> new ForgeFlowingFluid.Flowing(slurriedCoalOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_DIAMOND_ORE = FLUIDS.register("slurried_diamond_ore", () -> new ForgeFlowingFluid.Source(slurriedDiamondOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_DIAMOND_ORE_FLOWING = FLUIDS.register("flowing_slurried_diamond_ore", () -> new ForgeFlowingFluid.Flowing(slurriedDiamondOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_EMERALD_ORE = FLUIDS.register("slurried_emerald_ore", () -> new ForgeFlowingFluid.Source(slurriedEmeraldOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_EMERALD_ORE_FLOWING = FLUIDS.register("flowing_slurried_emerald_ore", () -> new ForgeFlowingFluid.Flowing(slurriedEmeraldOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_GOLD_ORE = FLUIDS.register("slurried_gold_ore", () -> new ForgeFlowingFluid.Source(slurriedGoldOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_GOLD_ORE_FLOWING = FLUIDS.register("flowing_slurried_gold_ore", () -> new ForgeFlowingFluid.Flowing(slurriedGoldOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_IRON_ORE = FLUIDS.register("slurried_iron_ore", () -> new ForgeFlowingFluid.Source(slurriedIronOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_IRON_ORE_FLOWING = FLUIDS.register("flowing_slurried_iron_ore", () -> new ForgeFlowingFluid.Flowing(slurriedIronOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_LAPIS_ORE = FLUIDS.register("slurried_lapis_ore", () -> new ForgeFlowingFluid.Source(slurriedLapisOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_LAPIS_ORE_FLOWING = FLUIDS.register("flowing_slurried_lapis_ore", () -> new ForgeFlowingFluid.Flowing(slurriedLapisOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_NETHER_QUARTZ_ORE = FLUIDS.register("slurried_nether_quartz_ore", () -> new ForgeFlowingFluid.Source(slurriedNetherQuartzOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_NETHER_QUARTZ_ORE_FLOWING = FLUIDS.register("flowing_slurried_nether_quartz_ore", () -> new ForgeFlowingFluid.Flowing(slurriedNetherQuartzOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_REDSTONE_ORE = FLUIDS.register("slurried_redstone_ore", () -> new ForgeFlowingFluid.Source(slurriedRedstoneOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_REDSTONE_ORE_FLOWING = FLUIDS.register("flowing_slurried_redstone_ore", () -> new ForgeFlowingFluid.Flowing(slurriedRedstoneOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_ANCIENT_DEBRIS = FLUIDS.register("slurried_ancient_debris", () -> new ForgeFlowingFluid.Source(slurriedAncientDebrisProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_ANCIENT_DEBRIS_FLOWING = FLUIDS.register("flowing_slurried_ancient_debris", () -> new ForgeFlowingFluid.Flowing(slurriedAncientDebrisProperties()));

    private static Properties slurriedCoalOreProperties() {
        ResourceLocation stillResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_coal_ore_still");
        ResourceLocation flowResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_coal_ore_flow");

        Supplier<Fluid> stillSupplier = () -> SLURRIED_COAL_ORE.get();
        Supplier<Fluid> flowSupplier = () -> SLURRIED_COAL_ORE_FLOWING.get();
        Supplier<LiquidBlock> blockSupplier = () -> ModBlocks.SLURRIED_COAL_ORE.get();
        Supplier<Item> bucketSupplier = () -> ModItems.SLURRIED_COAL_ORE_BUCKET.get();

        Builder attributes = FluidAttributes.builder(stillResource, flowResource);
        return new Properties(stillSupplier, flowSupplier, attributes).block(blockSupplier).bucket(bucketSupplier);
    }

    private static Properties slurriedDiamondOreProperties() {
        ResourceLocation stillResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_diamond_ore_still");
        ResourceLocation flowResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_diamond_ore_flow");

        Supplier<Fluid> stillSupplier = () -> SLURRIED_DIAMOND_ORE.get();
        Supplier<Fluid> flowSupplier = () -> SLURRIED_DIAMOND_ORE_FLOWING.get();
        Supplier<LiquidBlock> blockSupplier = () -> ModBlocks.SLURRIED_DIAMOND_ORE.get();
        Supplier<Item> bucketSupplier = () -> ModItems.SLURRIED_DIAMOND_ORE_BUCKET.get();

        Builder attributes = FluidAttributes.builder(stillResource, flowResource);
        return new Properties(stillSupplier, flowSupplier, attributes).block(blockSupplier).bucket(bucketSupplier);
    }

    private static Properties slurriedEmeraldOreProperties() {
        ResourceLocation stillResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_emerald_ore_still");
        ResourceLocation flowResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_emerald_ore_flow");

        Supplier<Fluid> stillSupplier = () -> SLURRIED_EMERALD_ORE.get();
        Supplier<Fluid> flowSupplier = () -> SLURRIED_EMERALD_ORE_FLOWING.get();
        Supplier<LiquidBlock> blockSupplier = () -> ModBlocks.SLURRIED_EMERALD_ORE.get();
        Supplier<Item> bucketSupplier = () -> ModItems.SLURRIED_EMERALD_ORE_BUCKET.get();

        Builder attributes = FluidAttributes.builder(stillResource, flowResource);
        return new Properties(stillSupplier, flowSupplier, attributes).block(blockSupplier).bucket(bucketSupplier);
    }

    private static Properties slurriedGoldOreProperties() {
        ResourceLocation stillResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_gold_ore_still");
        ResourceLocation flowResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_gold_ore_flow");

        Supplier<Fluid> stillSupplier = () -> SLURRIED_GOLD_ORE.get();
        Supplier<Fluid> flowSupplier = () -> SLURRIED_GOLD_ORE_FLOWING.get();
        Supplier<LiquidBlock> blockSupplier = () -> ModBlocks.SLURRIED_GOLD_ORE.get();
        Supplier<Item> bucketSupplier = () -> ModItems.SLURRIED_GOLD_ORE_BUCKET.get();

        Builder attributes = FluidAttributes.builder(stillResource, flowResource);
        return new Properties(stillSupplier, flowSupplier, attributes).block(blockSupplier).bucket(bucketSupplier);
    }

    private static Properties slurriedIronOreProperties() {
        ResourceLocation stillResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_iron_ore_still");
        ResourceLocation flowResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_iron_ore_flow");

        Supplier<Fluid> stillSupplier = () -> SLURRIED_IRON_ORE.get();
        Supplier<Fluid> flowSupplier = () -> SLURRIED_IRON_ORE_FLOWING.get();
        Supplier<LiquidBlock> blockSupplier = () -> ModBlocks.SLURRIED_IRON_ORE.get();
        Supplier<Item> bucketSupplier = () -> ModItems.SLURRIED_IRON_ORE_BUCKET.get();

        Builder attributes = FluidAttributes.builder(stillResource, flowResource);
        return new Properties(stillSupplier, flowSupplier, attributes).block(blockSupplier).bucket(bucketSupplier);
    }

    private static Properties slurriedLapisOreProperties() {
        ResourceLocation stillResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_lapis_ore_still");
        ResourceLocation flowResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_lapis_ore_flow");

        Supplier<Fluid> stillSupplier = () -> SLURRIED_LAPIS_ORE.get();
        Supplier<Fluid> flowSupplier = () -> SLURRIED_LAPIS_ORE_FLOWING.get();
        Supplier<LiquidBlock> blockSupplier = () -> ModBlocks.SLURRIED_LAPIS_ORE.get();
        Supplier<Item> bucketSupplier = () -> ModItems.SLURRIED_LAPIS_ORE_BUCKET.get();

        Builder attributes = FluidAttributes.builder(stillResource, flowResource);
        return new Properties(stillSupplier, flowSupplier, attributes).block(blockSupplier).bucket(bucketSupplier);
    }

    private static Properties slurriedNetherQuartzOreProperties() {
        ResourceLocation stillResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_nether_quartz_ore_still");
        ResourceLocation flowResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_nether_quartz_ore_flow");

        Supplier<Fluid> stillSupplier = () -> SLURRIED_NETHER_QUARTZ_ORE.get();
        Supplier<Fluid> flowSupplier = () -> SLURRIED_NETHER_QUARTZ_ORE_FLOWING.get();
        Supplier<LiquidBlock> blockSupplier = () -> ModBlocks.SLURRIED_NETHER_QUARTZ_ORE.get();
        Supplier<Item> bucketSupplier = () -> ModItems.SLURRIED_NETHER_QUARTZ_ORE_BUCKET.get();

        Builder attributes = FluidAttributes.builder(stillResource, flowResource);
        return new Properties(stillSupplier, flowSupplier, attributes).block(blockSupplier).bucket(bucketSupplier);
    }

    private static Properties slurriedRedstoneOreProperties() {
        ResourceLocation stillResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_redstone_ore_still");
        ResourceLocation flowResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_redstone_ore_flow");

        Supplier<Fluid> stillSupplier = () -> SLURRIED_REDSTONE_ORE.get();
        Supplier<Fluid> flowSupplier = () -> SLURRIED_REDSTONE_ORE_FLOWING.get();
        Supplier<LiquidBlock> blockSupplier = () -> ModBlocks.SLURRIED_REDSTONE_ORE.get();
        Supplier<Item> bucketSupplier = () -> ModItems.SLURRIED_REDSTONE_ORE_BUCKET.get();

        Builder attributes = FluidAttributes.builder(stillResource, flowResource);
        return new Properties(stillSupplier, flowSupplier, attributes).block(blockSupplier).bucket(bucketSupplier);
    }

    private static Properties slurriedAncientDebrisProperties() {
        ResourceLocation stillResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_ancient_debris_still");
        ResourceLocation flowResource = new ResourceLocation(Reference.MOD_ID, "block/slurried_ancient_debris_flow");

        Supplier<Fluid> stillSupplier = () -> SLURRIED_ANCIENT_DEBRIS.get();
        Supplier<Fluid> flowSupplier = () -> SLURRIED_ANCIENT_DEBRIS_FLOWING.get();
        Supplier<LiquidBlock> blockSupplier = () -> ModBlocks.SLURRIED_ANCIENT_DEBRIS.get();
        Supplier<Item> bucketSupplier = () -> ModItems.SLURRIED_ANCIENT_DEBRIS_BUCKET.get();

        Builder attributes = FluidAttributes.builder(stillResource, flowResource);
        return new Properties(stillSupplier, flowSupplier, attributes).block(blockSupplier).bucket(bucketSupplier);
    }
}
