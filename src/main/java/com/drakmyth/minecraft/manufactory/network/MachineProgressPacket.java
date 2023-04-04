/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.network;

import java.util.function.Supplier;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.util.LogHelper;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class MachineProgressPacket {
    private static final Logger LOGGER = LogUtils.getLogger();

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
        LOGGER.trace(LogMarkers.NETWORK, "MachineProgress packet encoded { progress: {}, total: {}, pos: {} }", progress, total, LogHelper.blockPos(pos));
    }

    public static MachineProgressPacket decode(FriendlyByteBuf data) {
        float progress = data.readFloat();
        float total = data.readFloat();
        BlockPos pos = data.readBlockPos();
        LOGGER.trace(LogMarkers.NETWORK, "MachineProgress packet decoded { progress: {}, total: {}, pos: {} }", progress, total, LogHelper.blockPos(pos));
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
                    Level level = minecraft.level;
                    if (level == null) return;
                    if (!level.isLoaded(pos)) {
                        LOGGER.warn(LogMarkers.NETWORK, "Position {} is not currently loaded. Dropping packet...", LogHelper.blockPos(pos));
                        return;
                    }
                    BlockEntity be = level.getBlockEntity(pos);
                    if (!(be instanceof IMachineProgressListener)) {
                        LOGGER.warn(LogMarkers.NETWORK, "Position {} does not contain an IMachineProgressListener tile entity. Dropping packet...", LogHelper.blockPos(pos));
                        return;
                    }
                    IMachineProgressListener mpl = (IMachineProgressListener) be;
                    mpl.onProgressUpdate(progress, total);
                    LOGGER.trace(LogMarkers.NETWORK, "Machine progress synced - progress {}, total {}", progress, total);
                }
            });
        });
        ctx.setPacketHandled(true);
        LOGGER.trace(LogMarkers.NETWORK, "MachineProgress packet received and queued");
    }
}
