/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.util.LogHelper;

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

public class OpenMenuWithUpgradesPacket {
    private static final Logger LOGGER = LogManager.getLogger();

    private ItemStack[] upgrades;
    private BlockPos pos;

    public OpenMenuWithUpgradesPacket(ItemStack[] upgrades, BlockPos pos) {
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
        LOGGER.trace(LogMarkers.NETWORK, "OpenMenuWithUpgrades packet encoded { upgrades: {}, pos: {} }", () -> LogHelper.items(upgrades), () -> LogHelper.blockPos(pos));
    }

    public static OpenMenuWithUpgradesPacket decode(FriendlyByteBuf data) {
        float count = data.readInt();
        List<ItemStack> upgrades = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            upgrades.add(data.readItem());
        }
        BlockPos pos = data.readBlockPos();
        // TODO: Update log to print item information
        LOGGER.trace(LogMarkers.NETWORK, "OpenMenuWithUpgrades packet decoded { upgrades: {}, pos: {} }", () -> LogHelper.items(upgrades), () -> LogHelper.blockPos(pos));
        return new OpenMenuWithUpgradesPacket(upgrades.toArray(new ItemStack[]{}), pos);
    }

    public void handle(Supplier<Context> contextSupplier) {
        Context ctx = contextSupplier.get();
        ctx.enqueueWork(() -> {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
                private static final long serialVersionUID = 1L;

                @Override
                public void run() {
                    LOGGER.trace(LogMarkers.NETWORK, "Processing OpenMenuWithUpgrades packet...");
                    Minecraft minecraft = Minecraft.getInstance();
                    Level level = minecraft.level;
                    if (!level.isAreaLoaded(pos, 1)) {
                        LOGGER.warn(LogMarkers.NETWORK, "Position {} is not currently loaded. Dropping packet...", () -> LogHelper.blockPos(pos));
                        return;
                    }
                    BlockEntity be = level.getBlockEntity(pos);
                    if (!(be instanceof IOpenMenuWithUpgradesListener)) {
                        LOGGER.warn(LogMarkers.NETWORK, "Position {} does not contain an IOpenMenuWithUpgradesListener tile entity. Dropping packet...", () -> LogHelper.blockPos(pos));
                        return;
                    }
                    IOpenMenuWithUpgradesListener prl = (IOpenMenuWithUpgradesListener) be;
                    prl.onContainerOpened(upgrades);
                    LOGGER.trace(LogMarkers.NETWORK, "Upgrades synced - upgrades: {}", () -> LogHelper.items(upgrades));
                }
            });
        });
        ctx.setPacketHandled(true);
        LOGGER.trace(LogMarkers.NETWORK, "PowerRate packet received and queued");
    }
}
