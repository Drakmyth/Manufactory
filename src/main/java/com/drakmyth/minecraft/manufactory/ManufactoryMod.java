package com.drakmyth.minecraft.manufactory;

import com.drakmyth.minecraft.manufactory.config.ConfigData;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModCommandArgumentTypes;
import com.drakmyth.minecraft.manufactory.init.ModMenuTypes;
import com.drakmyth.minecraft.manufactory.init.ModFluids;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import com.drakmyth.minecraft.manufactory.init.ModRecipeSerializers;
import com.drakmyth.minecraft.manufactory.init.ModRecipeTypes;
import com.drakmyth.minecraft.manufactory.init.ModBlockEntityTypes;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Reference.MOD_ID)
public class ManufactoryMod {
    private static final Logger LOGGER = LogUtils.getLogger();

    public ManufactoryMod(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("HELLO from Manufactory!");

        modEventBus.register(ModEventSubscriber.class);
        NeoForge.EVENT_BUS.register(ForgeEventSubscriber.class);

        LOGGER.info(LogMarkers.REGISTRATION, "Registering blocks...");
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.ITEMS.register(modEventBus);
        LOGGER.info(LogMarkers.REGISTRATION, "Block registration complete");

        LOGGER.info(LogMarkers.REGISTRATION, "Registering items...");
        ModItems.ITEMS.register(modEventBus);
        LOGGER.info(LogMarkers.REGISTRATION, "Item registration complete");

        ModFluids.FLUID_TYPES.register(modEventBus);
        ModFluids.FLUIDS.register(modEventBus);

        LOGGER.info(LogMarkers.REGISTRATION, "Registering tile entity types...");
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        LOGGER.info(LogMarkers.REGISTRATION, "Tile entity type registration complete");

        LOGGER.info(LogMarkers.REGISTRATION, "Registering container types...");
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        LOGGER.info(LogMarkers.REGISTRATION, "Container type registration complete");

        ModRecipeTypes.RECIPE_TYPES.register(modEventBus);

        LOGGER.info(LogMarkers.REGISTRATION, "Registering recipe serializers...");
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        LOGGER.info(LogMarkers.REGISTRATION, "Recipe serializer registration complete");

        LOGGER.info(LogMarkers.REGISTRATION, "Registering command argument types...");
        ModCommandArgumentTypes.COMMAND_ARGUMENT_TYPES.register(modEventBus);
        LOGGER.info(LogMarkers.REGISTRATION, "Command argument type registration complete");

        LOGGER.info(LogMarkers.REGISTRATION, "Register config files...");
        modContainer.registerConfig(ModConfig.Type.SERVER, ConfigData.SERVER_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ConfigData.CLIENT_SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, ConfigData.COMMON_SPEC);
        LOGGER.info(LogMarkers.REGISTRATION, "Config file registration complete");
    }
}
