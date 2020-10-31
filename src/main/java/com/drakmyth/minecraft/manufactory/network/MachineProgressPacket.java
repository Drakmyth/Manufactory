/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class MachineProgressPacket {
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
    }

    public static MachineProgressPacket decode(PacketBuffer data) {
        float progress = data.readFloat();
        float total = data.readFloat();
        float powerAmount = data.readFloat();
        float powerExpected = data.readFloat();
        BlockPos pos = data.readBlockPos();
        return new MachineProgressPacket(progress, total, powerAmount, powerExpected, pos);
    }

    public void handle(Supplier<Context> contextSupplier) {
        Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    Minecraft minecraft = Minecraft.getInstance();
                    World world = minecraft.world;
                    if (!world.isAreaLoaded(pos, 1)) return;
                    TileEntity te = world.getTileEntity(pos);
                    if (!(te instanceof IMachineProgressListener)) return;
                    IMachineProgressListener mpl = (IMachineProgressListener) te;
                    mpl.onProgressUpdate(progress, total);
                    mpl.onPowerRateUpdate(powerAmount, powerExpected);
                }
            });
        });
        ctx.setPacketHandled(true);
    }
}
