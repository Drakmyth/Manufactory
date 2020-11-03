/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory;

import com.drakmyth.minecraft.manufactory.commands.ManufactoryCommand;
import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
public final class ForgeEventSubscriber {

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.CLIENT) return;
        if (event.phase == Phase.END) return;
        PowerNetworkManager networkManager = PowerNetworkManager.get((ServerWorld)event.world);
        networkManager.tick(event.world);
    }

    @SubscribeEvent
    public static void registerCommands(final RegisterCommandsEvent event) {
        ManufactoryCommand.register(event.getDispatcher());
    }
}
