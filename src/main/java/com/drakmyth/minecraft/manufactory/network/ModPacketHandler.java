package com.drakmyth.minecraft.manufactory.network;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.Reference;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPacketHandler {
    private static final Logger LOGGER = LogUtils.getLogger();
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
                .consumerMainThread(MachineProgressPacket::handle)
                .add();

        LOGGER.info(LogMarkers.REGISTRATION, "Registering PowerRatePacket with id {}...", messageId);
        INSTANCE.messageBuilder(PowerRatePacket.class, messageId++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PowerRatePacket::encode)
                .decoder(PowerRatePacket::decode)
                .consumerMainThread(PowerRatePacket::handle)
                .add();

        LOGGER.info(LogMarkers.REGISTRATION, "Registering OpenContainerWithUpgradesPacket with id {}...", messageId);
        INSTANCE.messageBuilder(OpenMenuWithUpgradesPacket.class, messageId++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(OpenMenuWithUpgradesPacket::encode)
                .decoder(OpenMenuWithUpgradesPacket::decode)
                .consumerMainThread(OpenMenuWithUpgradesPacket::handle)
                .add();
    }
}
