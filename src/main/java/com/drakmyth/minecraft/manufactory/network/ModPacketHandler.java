/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.network;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.Reference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class ModPacketHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Reference.MOD_ID, "main"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    public static void registerMessages() {
        int messageId = 0;
        LOGGER.info(LogMarkers.REGISTRATION, "Registering MachineProgressPacket with id {}...", messageId);
        INSTANCE.messageBuilder(MachineProgressPacket.class, messageId++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(MachineProgressPacket::encode)
                .decoder(MachineProgressPacket::decode)
                .consumer(MachineProgressPacket::handle)
                .add();

        LOGGER.info(LogMarkers.REGISTRATION, "Registering PowerRatePacket with id {}...", messageId);
        INSTANCE.messageBuilder(PowerRatePacket.class, messageId++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PowerRatePacket::encode)
                .decoder(PowerRatePacket::decode)
                .consumer(PowerRatePacket::handle)
                .add();

        LOGGER.info(LogMarkers.REGISTRATION, "Registering OpenContainerWithUpgradesPacket with id {}...", messageId);
        INSTANCE.messageBuilder(OpenContainerWithUpgradesPacket.class, messageId++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(OpenContainerWithUpgradesPacket::encode)
                .decoder(OpenContainerWithUpgradesPacket::decode)
                .consumer(OpenContainerWithUpgradesPacket::handle)
                .add();
    }
}
