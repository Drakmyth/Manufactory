package com.drakmyth.minecraft.manufactory;

import com.drakmyth.minecraft.manufactory.commands.ManufactoryCommand;
import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.bus.api.SubscribeEvent;

public final class ForgeEventSubscriber {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void levelTick(LevelTickEvent.Pre event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        PowerNetworkManager networkManager = PowerNetworkManager.get(level);
        networkManager.tick(level);
    }

    @SubscribeEvent
    public static void registerCommands(final RegisterCommandsEvent event) {
        LOGGER.info(LogMarkers.REGISTRATION, "Registering commands...");
        ManufactoryCommand.register(event.getDispatcher());
        LOGGER.info(LogMarkers.REGISTRATION, "Command registration complete");
    }
}
