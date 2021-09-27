/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory;

import com.drakmyth.minecraft.manufactory.config.ConfigData;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModContainerTypes;
import com.drakmyth.minecraft.manufactory.init.ModFluids;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import com.drakmyth.minecraft.manufactory.init.ModRecipeSerializers;
import com.drakmyth.minecraft.manufactory.init.ModTileEntityTypes;
import com.drakmyth.minecraft.manufactory.network.ModPacketHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class ManufactoryMod {
    private static final Logger LOGGER = LogManager.getLogger();

    public ManufactoryMod() {
        LOGGER.info("HELLO from Manufactory!");

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        LOGGER.info(LogMarkers.REGISTRATION, "Registering Packet Handlers...");
        ModPacketHandler.registerMessages();
        LOGGER.info(LogMarkers.REGISTRATION, "Packet Handler registration complete");

        LOGGER.info(LogMarkers.REGISTRATION, "Registering blocks...");
        ModBlocks.BLOCKS.register(modEventBus);
        LOGGER.info(LogMarkers.REGISTRATION, "Block registration complete");

        LOGGER.info(LogMarkers.REGISTRATION, "Registering items...");
        ModItems.ITEMS.register(modEventBus);
        LOGGER.info(LogMarkers.REGISTRATION, "Item registration complete");

        ModFluids.FLUIDS.register(modEventBus);

        LOGGER.info(LogMarkers.REGISTRATION, "Registering tile entity types...");
        ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        LOGGER.info(LogMarkers.REGISTRATION, "Tile entity type registration complete");

        LOGGER.info(LogMarkers.REGISTRATION, "Registering container types...");
        ModContainerTypes.CONTAINER_TYPES.register(modEventBus);
        LOGGER.info(LogMarkers.REGISTRATION, "Container type registration complete");

        LOGGER.info(LogMarkers.REGISTRATION, "Registering recipe serializers...");
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        LOGGER.info(LogMarkers.REGISTRATION, "Recipe serializer registration complete");

        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        LOGGER.info(LogMarkers.REGISTRATION, "Register config files...");
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, ConfigData.SERVER_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigData.CLIENT_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigData.COMMON_SPEC);
        LOGGER.info(LogMarkers.REGISTRATION, "Config file registration complete");
    }
}
