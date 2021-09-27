/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory;

import com.drakmyth.minecraft.manufactory.commands.ManufactoryCommand;
import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public final class ForgeEventSubscriber {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.CLIENT) return;
        if (event.phase == Phase.END) return;
        PowerNetworkManager networkManager = PowerNetworkManager.get((ServerLevel)event.world);
        networkManager.tick(event.world);
    }

    @SubscribeEvent
    public static void registerCommands(final RegisterCommandsEvent event) {
        LOGGER.info(LogMarkers.REGISTRATION, "Registering commands...");
        ManufactoryCommand.register(event.getDispatcher());
        LOGGER.info(LogMarkers.REGISTRATION, "Command registration complete");
    }
}
