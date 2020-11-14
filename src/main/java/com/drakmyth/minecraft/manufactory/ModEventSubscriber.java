/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory;

import com.drakmyth.minecraft.manufactory.commands.PowerNetworkArgument;
import com.drakmyth.minecraft.manufactory.datagen.ModAnimatedTextureProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModBlockStateProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModItemProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModLanguageProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModLootTableProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModRecipeProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModTagsProvider;
import com.drakmyth.minecraft.manufactory.gui.BallMillGui;
import com.drakmyth.minecraft.manufactory.gui.BallMillUpgradeGui;
import com.drakmyth.minecraft.manufactory.gui.GrinderGui;
import com.drakmyth.minecraft.manufactory.gui.GrinderUpgradeGui;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModContainerTypes;
import com.drakmyth.minecraft.manufactory.init.ModTileEntityTypes;
import com.drakmyth.minecraft.manufactory.tileentities.renderers.LatexCollectorRenderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEventSubscriber {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        LOGGER.info("Registering blockitems...");
        final IForgeRegistry<Item> registry = event.getRegistry();

        ModBlocks.BLOCKS.getEntries().stream()
            .filter(blockRegistryObject -> !(blockRegistryObject.get() instanceof FlowingFluidBlock))
            .forEach(blockRegistryObject -> {
                final Item.Properties properties = ModBlocks.BLOCKITEM_PROPS.getOrDefault(blockRegistryObject, ModBlocks.defaultBlockItemProps());
                final Block block = blockRegistryObject.get();
                final BlockItem blockItem = new BlockItem(block, properties);
                blockItem.setRegistryName(block.getRegistryName());
                registry.register(blockItem);
            });
        LOGGER.info("BlockItem registration complete");
    }

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        LOGGER.info("Registering data generators...");
        DataGenerator generator = event.getGenerator();
        generator.addProvider(new ModRecipeProvider(generator));
        generator.addProvider(new ModLootTableProvider(generator));
        generator.addProvider(new ModItemProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(new ModBlockStateProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(new ModLanguageProvider(generator, "en_us"));
        generator.addProvider(new ModAnimatedTextureProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(new ModTagsProvider.Fluids(generator, event.getExistingFileHelper()));
        LOGGER.info("Data generator registration complete");
    }

    @SubscribeEvent
    public static void fmlCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("Registering argument types...");
            ArgumentTypes.register("power_network", PowerNetworkArgument.class, new ArgumentSerializer<>(PowerNetworkArgument::getPowerNetwork));
            LOGGER.info("Argument type registration complete");
        });
        LOGGER.info("ArgumentType registration queued");
    }

    @SubscribeEvent
    public static void fmlClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("Registering screens...");
        ScreenManager.registerFactory(ModContainerTypes.GRINDER.get(), GrinderGui::new);
        ScreenManager.registerFactory(ModContainerTypes.GRINDER_UPGRADE.get(), GrinderUpgradeGui::new);
        ScreenManager.registerFactory(ModContainerTypes.BALL_MILL.get(), BallMillGui::new);
        ScreenManager.registerFactory(ModContainerTypes.BALL_MILL_UPGRADE.get(), BallMillUpgradeGui::new);
        LOGGER.info("Screen registration complete");

        LOGGER.info("Binding tile entity renderers...");
        ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.LATEX_COLLECTOR.get(), LatexCollectorRenderer::new);
        LOGGER.info("Tile entity renderer binding complete");
    }
}
