/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent.Context;

public class OpenContainerWithUpgradesPacket {
    private static final Logger LOGGER = LogManager.getLogger();

    private ItemStack[] upgrades;
    private BlockPos pos;

    public OpenContainerWithUpgradesPacket(ItemStack[] upgrades, BlockPos pos) {
        this.upgrades = upgrades;
        this.pos = pos;
    }

    public ItemStack[] getUpgrades() {
        return upgrades;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void encode(FriendlyByteBuf data) {
        data.writeInt(upgrades.length);
        for(ItemStack upgrade : upgrades) {
            data.writeItem(upgrade);
        }
        data.writeBlockPos(pos);
        String upgradesStr = String.join(" , ", Arrays.stream(upgrades).map(u -> u.toString()).toList());
        LOGGER.trace("OpenContainerWithUpgrades packet encoded { upgrades: [ {} ], pos: ({}, {}, {}) }", upgradesStr, pos.getX(), pos.getY(), pos.getZ());
    }

    public static OpenContainerWithUpgradesPacket decode(FriendlyByteBuf data) {
        float count = data.readInt();
        List<ItemStack> upgrades = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            upgrades.add(data.readItem());
        }
        BlockPos pos = data.readBlockPos();
        // TODO: Update log to print item information
        LOGGER.trace("OpenContainerWithUpgrades packet decoded { upgrades: <todo>, pos: (%d, %d, %d) }", pos.getX(), pos.getY(), pos.getZ());
        return new OpenContainerWithUpgradesPacket(upgrades.toArray(new ItemStack[]{}), pos);
    }

    public void handle(Supplier<Context> contextSupplier) {
        Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    LOGGER.trace("Processing OpenContainerWithUpgrades packet...");
                    Minecraft minecraft = Minecraft.getInstance();
                    Level world = minecraft.level;
                    if (!world.isAreaLoaded(pos, 1)) {
                        LOGGER.warn("Position (%d, %d, %d) is not currently loaded. Dropping packet...",  pos.getX(), pos.getY(), pos.getZ());
                        return;
                    }
                    BlockEntity te = world.getBlockEntity(pos);
                    if (!(te instanceof IOpenContainerWithUpgradesListener)) {
                        LOGGER.warn("Position (%d, %d, %d) does not contain an IOpenContainerWithUpgradesListener tile entity. Dropping packet...", pos.getX(), pos.getY(), pos.getZ());
                        return;
                    }
                    IOpenContainerWithUpgradesListener prl = (IOpenContainerWithUpgradesListener) te;
                    prl.onContainerOpened(upgrades);
                    // TODO: Update log to print item information
                    LOGGER.trace("Upgrades synced - upgrades <todo>");
                }
            });
        });
        ctx.setPacketHandled(true);
        LOGGER.trace("PowerRate packet received and queued");
    }
}
