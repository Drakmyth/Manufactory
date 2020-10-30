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
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class MachineProgressPacket {
    private float progress;
    private float total;
    private BlockPos pos;

    public MachineProgressPacket(float progress, float total, BlockPos pos) {
        this.progress = progress;
        this.total = total;
        this.pos = pos;
    }

    public void encode(PacketBuffer data) {
        data.writeFloat(progress);
        data.writeFloat(total);
        data.writeBlockPos(pos);
    }

    public static MachineProgressPacket decode(PacketBuffer data) {
        float progress = data.readFloat();
        float total = data.readFloat();
        BlockPos pos = data.readBlockPos();
        return new MachineProgressPacket(progress, total, pos);
    }

    public void handle(Supplier<Context> contextSupplier) {
        Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            World world = minecraft.world;
            if (!world.isAreaLoaded(pos, 1)) return;
            TileEntity te = world.getTileEntity(pos);
            if (!(te instanceof IMachineProgressListener)) return;
            ((IMachineProgressListener)te).onProgressUpdate(progress, total);
        });
        ctx.setPacketHandled(true);
    }
}
