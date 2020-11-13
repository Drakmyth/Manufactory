/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.network;

import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent.Context;

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

    public void encode(PacketBuffer data) {
        data.writeFloat(received);
        data.writeFloat(expected);
        data.writeBlockPos(pos);
        LOGGER.trace("PowerRate packet encoded { received: %f, expected: %f, pos: (%d, %d, %d) }", received, expected, pos.getX(), pos.getY(), pos.getZ());
    }

    public static PowerRatePacket decode(PacketBuffer data) {
        float received = data.readFloat();
        float expected = data.readFloat();
        BlockPos pos = data.readBlockPos();
        LOGGER.trace("PowerRate packet decoded { received: %f, expected: %f, pos: (%d, %d, %d) }", received, expected, pos.getX(), pos.getY(), pos.getZ());
        return new PowerRatePacket(received, expected, pos);
    }

    public void handle(Supplier<Context> contextSupplier) {
        Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    LOGGER.trace("Processing PowerRate packet...");
                    Minecraft minecraft = Minecraft.getInstance();
                    World world = minecraft.world;
                    if (!world.isAreaLoaded(pos, 1)) {
                        LOGGER.warn("Position (%d, %d, %d) is not currently loaded. Dropping packet...",  pos.getX(), pos.getY(), pos.getZ());
                        return;
                    }
                    TileEntity te = world.getTileEntity(pos);
                    if (!(te instanceof IPowerRateListener)) {
                        LOGGER.warn("Position (%d, %d, %d) does not contain an IPowerRateListener tile entity. Dropping packet...", pos.getX(), pos.getY(), pos.getZ());
                        return;
                    }
                    IPowerRateListener prl = (IPowerRateListener) te;
                    prl.onPowerRateUpdate(received, expected);
                    LOGGER.trace("Power rate synced - received %f, expected %f", received, expected);
                }
            });
        });
        ctx.setPacketHandled(true);
        LOGGER.trace("PowerRate packet received and queued");
    }
}
