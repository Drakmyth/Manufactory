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

public class MachineProgressPacket {
    private static final Logger LOGGER = LogManager.getLogger();

    private float progress;
    private float total;
    private float powerAmount;
    private float powerExpected;
    private BlockPos pos;

    public MachineProgressPacket(float progress, float total, float powerAmount, float powerExpected, BlockPos pos) {
        this.progress = progress;
        this.total = total;
        this.powerAmount = powerAmount;
        this.powerExpected = powerExpected;
        this.pos = pos;
    }

    public float getProgress() {
        return progress;
    }

    public float getTotal() {
        return total;
    }

    public float getPowerAmount() {
        return powerAmount;
    }

    public float getPowerExpected() {
        return powerExpected;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void encode(PacketBuffer data) {
        data.writeFloat(progress);
        data.writeFloat(total);
        data.writeFloat(powerAmount);
        data.writeFloat(powerExpected);
        data.writeBlockPos(pos);
        LOGGER.trace("MachineProgress packet encoded { progress: %f, total: %f, powerAmount: %f, powerExpected: %f, pos: (%d, %d, %d) }", progress, total, powerAmount, powerExpected, pos.getX(), pos.getY(), pos.getZ());
    }

    public static MachineProgressPacket decode(PacketBuffer data) {
        float progress = data.readFloat();
        float total = data.readFloat();
        float powerAmount = data.readFloat();
        float powerExpected = data.readFloat();
        BlockPos pos = data.readBlockPos();
        LOGGER.trace("MachineProgress packet decoded { progress: %f, total: %f, powerAmount: %f, powerExpected: %f, pos: (%d, %d, %d) }", progress, total, powerAmount, powerExpected, pos.getX(), pos.getY(), pos.getZ());
        return new MachineProgressPacket(progress, total, powerAmount, powerExpected, pos);
    }

    public void handle(Supplier<Context> contextSupplier) {
        Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    LOGGER.trace("Processing MachineProgress packet...");
                    Minecraft minecraft = Minecraft.getInstance();
                    World world = minecraft.world;
                    if (!world.isAreaLoaded(pos, 1)) {
                        LOGGER.warn("Position (%d, %d, %d) is not currently loaded. Dropping packet...",  pos.getX(), pos.getY(), pos.getZ());
                        return;
                    }
                    TileEntity te = world.getTileEntity(pos);
                    if (!(te instanceof IMachineProgressListener)) {
                        LOGGER.warn("Position (%d, %d, %d) does not contain an IMachineProgressListener tile entity. Dropping packet...", pos.getX(), pos.getY(), pos.getZ());
                        return;
                    }
                    IMachineProgressListener mpl = (IMachineProgressListener) te;
                    mpl.onProgressUpdate(progress, total);
                    LOGGER.trace("Machine progress synced - progress %f, total %f", progress, total);
                    mpl.onPowerRateUpdate(powerAmount, powerExpected);
                    LOGGER.trace("Machine power rate synced - powerAmount %f, powerExpected %f", powerAmount, powerExpected);
                }
            });
        });
        ctx.setPacketHandled(true);
        LOGGER.trace("MachineProgress packet received and queued");
    }
}
