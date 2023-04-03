/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory;

import com.drakmyth.minecraft.manufactory.blocks.entities.renderers.LatexCollectorRenderer;
import com.drakmyth.minecraft.manufactory.datagen.ModAnimatedTextureProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModBlockStateProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModItemProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModLanguageProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModLootTableProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModRecipeProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModTagsProvider;
import com.drakmyth.minecraft.manufactory.init.ModMenuTypes;
import com.drakmyth.minecraft.manufactory.menus.BallMillMenu;
import com.drakmyth.minecraft.manufactory.menus.BallMillUpgradeMenu;
import com.drakmyth.minecraft.manufactory.menus.GrinderMenu;
import com.drakmyth.minecraft.manufactory.menus.GrinderUpgradeMenu;
import com.drakmyth.minecraft.manufactory.menus.RockDrillUpgradeMenu;
import com.drakmyth.minecraft.manufactory.menus.screens.PowerProgressScreen;
import com.drakmyth.minecraft.manufactory.menus.screens.ScreenTextures;
import com.drakmyth.minecraft.manufactory.menus.screens.SimpleScreen;
import com.drakmyth.minecraft.manufactory.init.ModBlockEntityTypes;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEventSubscriber {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        LOGGER.info(LogMarkers.REGISTRATION, "Registering data generators...");
        
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        
        generator.addProvider(event.includeClient(), new ModRecipeProvider(generator));
        generator.addProvider(event.includeClient(), new ModLootTableProvider(generator));
        generator.addProvider(event.includeClient(), new ModItemProvider(generator, helper));
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(generator, helper));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(generator, "en_us"));
        generator.addProvider(event.includeClient(), new ModAnimatedTextureProvider(generator, helper));

        BlockTagsProvider blockTagsProvider = new ModTagsProvider.Blocks(generator, helper);
        generator.addProvider(event.includeClient(), blockTagsProvider);
        generator.addProvider(event.includeClient(), new ModTagsProvider.Items(generator, blockTagsProvider, helper));
        generator.addProvider(event.includeClient(), new ModTagsProvider.Fluids(generator, helper));

        LOGGER.info(LogMarkers.REGISTRATION, "Data generator registration complete");
    }

    @SubscribeEvent
    public static void fmlEntityRenderers(final RegisterRenderers event) {
        LOGGER.info(LogMarkers.REGISTRATION, "Binding block entity renderers...");
        event.registerBlockEntityRenderer(ModBlockEntityTypes.LATEX_COLLECTOR.get(), LatexCollectorRenderer::new);
        LOGGER.info(LogMarkers.REGISTRATION, "Block entity renderer binding complete");
    }

    @SubscribeEvent
    public static void fmlClientSetup(FMLClientSetupEvent event) {
        ScreenTextures.init();
        LOGGER.info(LogMarkers.REGISTRATION, "Registering screens...");
        MenuScreens.register(ModMenuTypes.GRINDER.get(), PowerProgressScreen<GrinderMenu>::new);
        MenuScreens.register(ModMenuTypes.GRINDER_UPGRADE.get(), SimpleScreen<GrinderUpgradeMenu>::new);
        MenuScreens.register(ModMenuTypes.BALL_MILL.get(), PowerProgressScreen<BallMillMenu>::new);
        MenuScreens.register(ModMenuTypes.BALL_MILL_UPGRADE.get(), SimpleScreen<BallMillUpgradeMenu>::new);
        MenuScreens.register(ModMenuTypes.ROCK_DRILL_UPGRADE.get(), SimpleScreen<RockDrillUpgradeMenu>::new);
        LOGGER.info(LogMarkers.REGISTRATION, "Screen registration complete");
    }
}
