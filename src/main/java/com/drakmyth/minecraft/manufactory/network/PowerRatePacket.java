/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.network;

import java.util.function.Supplier;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.util.LogHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent.Context;

public class PowerRatePacket {
    private static final Logger LOGGER = LogManager.getLogger();

    private float received;
    private float expected;
    private BlockPos pos;

    public PowerRatePacket(float received, float expected, BlockPos pos) {
        this.received = received;
        this.expected = expected;
        this.pos = pos;
    }

    public float getReceived() {
        return received;
    }

    public float getExpected() {
        return expected;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void encode(FriendlyByteBuf data) {
        data.writeFloat(received);
        data.writeFloat(expected);
        data.writeBlockPos(pos);
        LOGGER.trace(LogMarkers.NETWORK, "PowerRate packet encoded { received: {}, expected: {}, pos: {} }", () -> received, () -> expected, () -> LogHelper.blockPos(pos));
    }

    public static PowerRatePacket decode(FriendlyByteBuf data) {
        float received = data.readFloat();
        float expected = data.readFloat();
        BlockPos pos = data.readBlockPos();
        LOGGER.trace(LogMarkers.NETWORK, "PowerRate packet decoded { received: {}, expected: {}, pos: {} }", () -> received, () -> expected, () -> LogHelper.blockPos(pos));
        return new PowerRatePacket(received, expected, pos);
    }

    public void handle(Supplier<Context> contextSupplier) {
        Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    LOGGER.trace(LogMarkers.NETWORK, "Processing PowerRate packet...");
                    Minecraft minecraft = Minecraft.getInstance();
                    Level level = minecraft.level;
                    if (!level.isAreaLoaded(pos, 1)) {
                        LOGGER.warn(LogMarkers.NETWORK, "Position {} is not currently loaded. Dropping packet...", () -> LogHelper.blockPos(pos));
                        return;
                    }
                    BlockEntity be = level.getBlockEntity(pos);
                    if (!(be instanceof IPowerRateListener)) {
                        LOGGER.warn(LogMarkers.NETWORK, "Position {} does not contain an IPowerRateListener tile entity. Dropping packet...", () -> LogHelper.blockPos(pos));
                        return;
                    }
                    IPowerRateListener prl = (IPowerRateListener) be;
                    prl.onPowerRateUpdate(received, expected);
                    LOGGER.trace(LogMarkers.NETWORK, "Power rate synced - received {}, expected {}", received, expected);
                }
            });
        });
        ctx.setPacketHandled(true);
        LOGGER.trace(LogMarkers.NETWORK, "PowerRate packet received and queued");
    }
}
