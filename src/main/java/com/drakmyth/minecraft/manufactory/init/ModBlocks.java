/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.blocks.BallMillBlock;
import com.drakmyth.minecraft.manufactory.blocks.GrinderBlock;
import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.blocks.PowerCableBlock;
import com.drakmyth.minecraft.manufactory.blocks.SolarPanelBlock;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);

    public static final RegistryObject<Block> AMBER_BLOCK = BLOCKS.register("amber_block", () -> new Block(defaultDecorProperties(MaterialColor.YELLOW)));
    public static final RegistryObject<Block> GRINDER = BLOCKS.register("grinder", () -> new GrinderBlock(defaultMachineProperties()));
    public static final RegistryObject<Block> BALL_MILL = BLOCKS.register("ball_mill", () -> new BallMillBlock(defaultMachineProperties()));
    public static final RegistryObject<Block> LATEX_COLLECTOR = BLOCKS.register("latex_collector", () -> new LatexCollectorBlock(Block.Properties.create(Material.WOOD).harvestTool(ToolType.AXE).hardnessAndResistance(1.0f).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> POWER_CABLE = BLOCKS.register("power_cable", () -> new PowerCableBlock(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0.7f).sound(SoundType.METAL)));
    public static final RegistryObject<Block> SOLAR_PANEL = BLOCKS.register("solar_panel", () -> new SolarPanelBlock(defaultMachineProperties()));

    public static final RegistryObject<FlowingFluidBlock> SLURRIED_COAL_ORE = BLOCKS.register("slurried_coal_ore", () -> new FlowingFluidBlock(() -> ModFluids.SLURRIED_COAL_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<FlowingFluidBlock> SLURRIED_DIAMOND_ORE = BLOCKS.register("slurried_diamond_ore", () -> new FlowingFluidBlock(() -> ModFluids.SLURRIED_DIAMOND_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<FlowingFluidBlock> SLURRIED_EMERALD_ORE = BLOCKS.register("slurried_emerald_ore", () -> new FlowingFluidBlock(() -> ModFluids.SLURRIED_EMERALD_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<FlowingFluidBlock> SLURRIED_GOLD_ORE = BLOCKS.register("slurried_gold_ore", () -> new FlowingFluidBlock(() -> ModFluids.SLURRIED_GOLD_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<FlowingFluidBlock> SLURRIED_IRON_ORE = BLOCKS.register("slurried_iron_ore", () -> new FlowingFluidBlock(() -> ModFluids.SLURRIED_IRON_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<FlowingFluidBlock> SLURRIED_LAPIS_ORE = BLOCKS.register("slurried_lapis_ore", () -> new FlowingFluidBlock(() -> ModFluids.SLURRIED_LAPIS_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<FlowingFluidBlock> SLURRIED_NETHER_QUARTZ_ORE = BLOCKS.register("slurried_nether_quartz_ore", () -> new FlowingFluidBlock(() -> ModFluids.SLURRIED_NETHER_QUARTZ_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<FlowingFluidBlock> SLURRIED_REDSTONE_ORE = BLOCKS.register("slurried_redstone_ore", () -> new FlowingFluidBlock(() -> ModFluids.SLURRIED_REDSTONE_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<FlowingFluidBlock> SLURRIED_ANCIENT_DEBRIS = BLOCKS.register("slurried_ancient_debris", () -> new FlowingFluidBlock(() -> ModFluids.SLURRIED_ANCIENT_DEBRIS.get(), defaultFluidProperties()));

    private static Block.Properties defaultDecorProperties(MaterialColor color) {
        return Block.Properties.create(Material.IRON, color)
            .setRequiresTool()
            .harvestTool(ToolType.PICKAXE)
            .harvestLevel(ItemTier.WOOD.getHarvestLevel())
            .hardnessAndResistance(5f, 6f)
            .sound(SoundType.METAL);
    }

    private static Block.Properties defaultMachineProperties() {
        return Block.Properties.create(Material.ROCK)
            .setRequiresTool()
            .harvestTool(ToolType.PICKAXE)
            .harvestLevel(ItemTier.WOOD.getHarvestLevel())
            .hardnessAndResistance(3.5f);
    }

    private static Block.Properties defaultFluidProperties() {
        return Block.Properties.create(Material.WATER)
            .doesNotBlockMovement()
            .hardnessAndResistance(100f)
            .noDrops();
    }

    public static final Map<RegistryObject<Block>, Item.Properties> BLOCKITEM_PROPS = Stream.of(
        new SimpleEntry<>(LATEX_COLLECTOR, defaultBlockItemProps().maxStackSize(16))
    ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

    public static Item.Properties defaultBlockItemProps() {
        return new Item.Properties().maxStackSize(64).group(ModItemGroups.MANUFACTORY_ITEM_GROUP);
    }
}
