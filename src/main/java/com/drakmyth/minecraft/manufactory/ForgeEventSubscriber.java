package com.drakmyth.minecraft.manufactory;

import com.drakmyth.minecraft.manufactory.commands.ManufactoryCommand;
import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.TickEvent.Phase;
import net.neoforged.neoforge.eventbus.api.SubscribeEvent;
import net.neoforged.neoforge.fml.LogicalSide;
import net.neoforged.neoforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public final class ForgeEventSubscriber {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void levelTick(TickEvent.LevelTickEvent event) {
        if (event.side == LogicalSide.CLIENT) return;
        if (event.phase == Phase.END) return;
        PowerNetworkManager networkManager = PowerNetworkManager.get((ServerLevel)event.level);
        networkManager.tick(event.level);
    }

    @SubscribeEvent
    public static void registerCommands(final RegisterCommandsEvent event) {
        LOGGER.info(LogMarkers.REGISTRATION, "Registering commands...");
        ManufactoryCommand.register(event.getDispatcher());
        LOGGER.info(LogMarkers.REGISTRATION, "Command registration complete");
    }
}
