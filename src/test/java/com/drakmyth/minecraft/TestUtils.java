/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */
package com.drakmyth.minecraft;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public class TestUtils {
    private static MinecraftServer server;
    private static ServerWorld world;

    @SubscribeEvent
    public static void fmlServerStarting(FMLServerStartingEvent event) {
        server = event.getServer();
    }

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load event) {
        if (world != null) return;
        if (!(event.getWorld() instanceof ServerWorld)) return;
        world = (ServerWorld)event.getWorld();
        if (world.getDimensionKey() != World.OVERWORLD) {
            world = null;
        }
    }

    public static MinecraftServer getServer() {
        return server;
    }

    public static ServerWorld getWorld() {
        return world;
    }
}
