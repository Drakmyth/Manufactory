/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.network;

import java.util.function.Supplier;

import com.drakmyth.minecraft.manufactory.LogMarkers;

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

public class MachineProgressPacket {
    private static final Logger LOGGER = LogManager.getLogger();

    private float progress;
    private float total;
    private BlockPos pos;

    public MachineProgressPacket(float progress, float total, BlockPos pos) {
        this.progress = progress;
        this.total = total;
        this.pos = pos;
    }

    public float getProgress() {
        return progress;
    }

    public float getTotal() {
        return total;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void encode(FriendlyByteBuf data) {
        data.writeFloat(progress);
        data.writeFloat(total);
        data.writeBlockPos(pos);
        LOGGER.trace(LogMarkers.NETWORK, "MachineProgress packet encoded { progress: %f, total: %f, pos: (%d, %d, %d) }", progress, total, pos.getX(), pos.getY(), pos.getZ());
    }

    public static MachineProgressPacket decode(FriendlyByteBuf data) {
        float progress = data.readFloat();
        float total = data.readFloat();
        BlockPos pos = data.readBlockPos();
        LOGGER.trace(LogMarkers.NETWORK, "MachineProgress packet decoded { progress: %f, total: %f, pos: (%d, %d, %d) }", progress, total, pos.getX(), pos.getY(), pos.getZ());
        return new MachineProgressPacket(progress, total, pos);
    }

    public void handle(Supplier<Context> contextSupplier) {
        Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    LOGGER.trace(LogMarkers.NETWORK, "Processing MachineProgress packet...");
                    Minecraft minecraft = Minecraft.getInstance();
                    Level world = minecraft.level;
                    if (!world.isAreaLoaded(pos, 1)) {
                        LOGGER.warn(LogMarkers.NETWORK, "Position (%d, %d, %d) is not currently loaded. Dropping packet...",  pos.getX(), pos.getY(), pos.getZ());
                        return;
                    }
                    BlockEntity te = world.getBlockEntity(pos);
                    if (!(te instanceof IMachineProgressListener)) {
                        LOGGER.warn(LogMarkers.NETWORK, "Position (%d, %d, %d) does not contain an IMachineProgressListener tile entity. Dropping packet...", pos.getX(), pos.getY(), pos.getZ());
                        return;
                    }
                    IMachineProgressListener mpl = (IMachineProgressListener) te;
                    mpl.onProgressUpdate(progress, total);
                    LOGGER.trace(LogMarkers.NETWORK, "Machine progress synced - progress %f, total %f", progress, total);
                }
            });
        });
        ctx.setPacketHandled(true);
        LOGGER.trace(LogMarkers.NETWORK, "MachineProgress packet received and queued");
    }
}
