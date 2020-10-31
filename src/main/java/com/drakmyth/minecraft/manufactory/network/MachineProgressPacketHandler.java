/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.network;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.DistExecutor;

public class MachineProgressPacketHandler {
    public static DistExecutor.SafeRunnable handle(MachineProgressPacket msg) {
        return new DistExecutor.SafeRunnable() {
			private static final long serialVersionUID = 1L;

			@Override
            public void run() {
                Minecraft minecraft = Minecraft.getInstance();
                World world = minecraft.world;
                if (!world.isAreaLoaded(msg.getPos(), 1)) return;
                TileEntity te = world.getTileEntity(msg.getPos());
                if (!(te instanceof IMachineProgressListener)) return;
                IMachineProgressListener mpl = (IMachineProgressListener) te;
                mpl.onProgressUpdate(msg.getProgress(), msg.getTotal());
                mpl.onPowerRateUpdate(msg.getPowerAmount(), msg.getPowerExpected());
            }
        };
    }
}
