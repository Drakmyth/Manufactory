/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.network;

import com.drakmyth.minecraft.manufactory.Reference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static final Logger LOGGER = LogManager.getLogger();

    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Reference.MOD_ID, "main"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    public static void registerMessages() {
        int messageId = 0;
        LOGGER.debug("Registering MachineProgressPacket with id %d...", messageId);
        INSTANCE.messageBuilder(MachineProgressPacket.class, messageId++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(MachineProgressPacket::encode)
                .decoder(MachineProgressPacket::decode)
                .consumer(MachineProgressPacket::handle)
                .add();
    }
}
