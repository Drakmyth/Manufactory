/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory;

import com.drakmyth.minecraft.manufactory.blocks.entities.renderers.LatexCollectorRenderer;
import com.drakmyth.minecraft.manufactory.commands.PowerNetworkArgument;
import com.drakmyth.minecraft.manufactory.datagen.ModAnimatedTextureProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModBlockStateProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModItemProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModLanguageProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModLootTableProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModRecipeProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModTagsProvider;
import com.drakmyth.minecraft.manufactory.gui.PowerProgressScreen;
import com.drakmyth.minecraft.manufactory.gui.ScreenTextures;
import com.drakmyth.minecraft.manufactory.gui.SimpleScreen;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModMenuTypes;
import com.drakmyth.minecraft.manufactory.menus.BallMillMenu;
import com.drakmyth.minecraft.manufactory.menus.BallMillUpgradeMenu;
import com.drakmyth.minecraft.manufactory.menus.GrinderMenu;
import com.drakmyth.minecraft.manufactory.menus.GrinderUpgradeMenu;
import com.drakmyth.minecraft.manufactory.menus.RockDrillUpgradeMenu;
import com.drakmyth.minecraft.manufactory.init.ModBlockEntityTypes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEventSubscriber {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        LOGGER.info(LogMarkers.REGISTRATION, "Registering blockitems...");
        final IForgeRegistry<Item> registry = event.getRegistry();

        ModBlocks.BLOCKS.getEntries().stream()
            .filter(blockRegistryObject -> !(blockRegistryObject.get() instanceof LiquidBlock))
            .forEach(blockRegistryObject -> {
                final Item.Properties properties = ModBlocks.BLOCKITEM_PROPS.getOrDefault(blockRegistryObject, ModBlocks.defaultBlockItemProps());
                final Block block = blockRegistryObject.get();
                final BlockItem blockItem = new BlockItem(block, properties);
                blockItem.setRegistryName(block.getRegistryName());
                registry.register(blockItem);
            });
        LOGGER.info(LogMarkers.REGISTRATION, "BlockItem registration complete");
    }

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        LOGGER.info(LogMarkers.REGISTRATION, "Registering data generators...");
        
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        
        generator.addProvider(new ModRecipeProvider(generator));
        generator.addProvider(new ModLootTableProvider(generator));
        generator.addProvider(new ModItemProvider(generator, helper));
        generator.addProvider(new ModBlockStateProvider(generator, helper));
        generator.addProvider(new ModLanguageProvider(generator, "en_us"));
        generator.addProvider(new ModAnimatedTextureProvider(generator, helper));

        BlockTagsProvider blockTagsProvider = new ModTagsProvider.Blocks(generator, helper);
        generator.addProvider(blockTagsProvider);
        generator.addProvider(new ModTagsProvider.Items(generator, blockTagsProvider, helper));
        generator.addProvider(new ModTagsProvider.Fluids(generator, helper));

        LOGGER.info(LogMarkers.REGISTRATION, "Data generator registration complete");
    }

    @SubscribeEvent
    public static void fmlCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info(LogMarkers.REGISTRATION, "Registering argument types...");
            ArgumentTypes.register("power_network", PowerNetworkArgument.class, new EmptyArgumentSerializer<>(PowerNetworkArgument::getPowerNetwork));
            LOGGER.info(LogMarkers.REGISTRATION, "Argument type registration complete");
        });
        LOGGER.info(LogMarkers.REGISTRATION, "ArgumentType registration queued");
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
