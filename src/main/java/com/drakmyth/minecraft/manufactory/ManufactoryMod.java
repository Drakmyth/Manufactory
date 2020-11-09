/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory;

import com.drakmyth.minecraft.manufactory.config.ConfigData;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModContainerTypes;
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
        LOGGER.info("Registering Packet Handlers...");
        ModPacketHandler.registerMessages();
        LOGGER.info("Packet Handler registration complete");

        LOGGER.info("Registering blocks...");
        ModBlocks.BLOCKS.register(modEventBus);
        LOGGER.info("Block registration complete");

        LOGGER.info("Registering items...");
        ModItems.ITEMS.register(modEventBus);
        LOGGER.info("Item registration complete");

        LOGGER.info("Registering tile entity types...");
        ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        LOGGER.info("Tile entity type registration complete");

        LOGGER.info("Registering container types...");
        ModContainerTypes.CONTAINER_TYPES.register(modEventBus);
        LOGGER.info("Container type registration complete");

        LOGGER.info("Registering recipe serializers...");
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        LOGGER.info("Recipe serializer registration complete");

        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        LOGGER.info("Register config files...");
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, ConfigData.SERVER_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigData.CLIENT_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigData.COMMON_SPEC);
        LOGGER.info("Config file registration complete");
    }
}
