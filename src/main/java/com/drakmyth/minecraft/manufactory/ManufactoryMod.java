/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory;

import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.blocks.LatexFluidBlock;
import com.drakmyth.minecraft.manufactory.blocks.ModBlocks;
import com.drakmyth.minecraft.manufactory.fluids.LatexFluid;
import com.drakmyth.minecraft.manufactory.fluids.ManufactoryFluid;
import com.drakmyth.minecraft.manufactory.items.LatexBucketItem;
import com.drakmyth.minecraft.manufactory.items.TappingKnifeItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class ManufactoryMod {
    private static final Logger LOGGER = LogManager.getLogger();

    public ManufactoryMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO from Preinit");
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(new LatexFluidBlock(), new LatexCollectorBlock());
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(ModBlocks.LATEX_COLLECTOR.BLOCK_ITEM, new TappingKnifeItem(), new LatexBucketItem());
        }

        @SubscribeEvent
        public static void onFlowingFluidsRegistry(final RegistryEvent.Register<Fluid> event) {
            ManufactoryFluid latex = new LatexFluid();
            event.getRegistry().registerAll(latex.SOURCE, latex.FLOWING);
        }
    }
}
