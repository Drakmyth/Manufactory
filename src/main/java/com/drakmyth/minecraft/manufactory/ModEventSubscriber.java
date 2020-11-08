/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory;

import com.drakmyth.minecraft.manufactory.datagen.ModBlockStateProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModLanguageProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModLootTableProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModRecipeProvider;
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
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEventSubscriber {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        LOGGER.info("Registering BlockItems...");
        final IForgeRegistry<Item> registry = event.getRegistry();

        ModBlocks.BLOCKS.getEntries().stream()
            .forEach(blockRegistryObject -> {
                LOGGER.info("Registering block");
                final Item.Properties properties = ModBlocks.BLOCKITEM_PROPS.getOrDefault(blockRegistryObject, ModBlocks.defaultBlockItemProps());
                final Block block = blockRegistryObject.get();
                final BlockItem blockItem = new BlockItem(block, properties);
                blockItem.setRegistryName(block.getRegistryName());
                registry.register(blockItem);
            });
    }

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(new ModRecipeProvider(generator));
        generator.addProvider(new ModLootTableProvider(generator));
        generator.addProvider(new ModBlockStateProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(new ModLanguageProvider(generator, "en_us"));
    }

    @SubscribeEvent
    public static void fmlClientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainerTypes.GRINDER.get(), GrinderGui::new);
        ScreenManager.registerFactory(ModContainerTypes.GRINDER_UPGRADE.get(), GrinderUpgradeGui::new);
        ScreenManager.registerFactory(ModContainerTypes.BALL_MILL.get(), BallMillGui::new);
        ScreenManager.registerFactory(ModContainerTypes.BALL_MILL_UPGRADE.get(), BallMillUpgradeGui::new);

        ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.LATEX_COLLECTOR.get(), LatexCollectorRenderer::new);
    }

    @SubscribeEvent
    public static void preTextureStitch(TextureStitchEvent.Pre event) {
        if (!event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) return;

        event.addSprite(LatexCollectorRenderer.LATEX_TEXTURE);
    }
}
