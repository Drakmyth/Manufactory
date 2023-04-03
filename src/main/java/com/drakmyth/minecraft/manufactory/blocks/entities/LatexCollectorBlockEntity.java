/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks.entities;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.config.ConfigData;
import com.drakmyth.minecraft.manufactory.init.ModBlockEntityTypes;
import com.drakmyth.minecraft.manufactory.network.IMachineProgressListener;
import com.drakmyth.minecraft.manufactory.network.MachineProgressPacket;
import com.drakmyth.minecraft.manufactory.network.ModPacketHandler;
import com.drakmyth.minecraft.manufactory.util.LogHelper;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.PacketDistributor;

public class LatexCollectorBlockEntity extends BlockEntity implements IMachineProgressListener {
    private static final Logger LOGGER = LogUtils.getLogger();

    private int totalTicks = 0;
    private int ticksRemaining = 0;

    public LatexCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.LATEX_COLLECTOR.get(), pos, state);
    }

    public boolean onTap(Level level, BlockPos pos, BlockState state) {
        if (isWaterlogged(state)) {
            LOGGER.debug(LogMarkers.INTERACTION, "Tapped, but waterlogged");
            return false;
        }
        if (!isEmpty(state)) {
            LOGGER.debug(LogMarkers.INTERACTION, "Tapped, but not empty");
            return false;
        }
        if (ticksRemaining > 0) {
            LOGGER.debug(LogMarkers.INTERACTION, "Tapped, but already filling");
            return false;
        }
        LOGGER.debug(LogMarkers.INTERACTION, "Tapped, starting countdown...");

        int configFillTimeSeconds = ConfigData.SERVER.LatexFillSeconds.get();
        totalTicks = 20 * configFillTimeSeconds;
        ticksRemaining = totalTicks;

        if (level.isClientSide()) return true;
        level.setBlockAndUpdate(getBlockPos(), state.setValue(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING));
        updateClient(level, pos);
        setChanged();
        return true;
    }

    private void updateClient(Level level, BlockPos pos) {
        LOGGER.trace(LogMarkers.NETWORK, "Sending MachineProgress packet to update animation at {}...", LogHelper.blockPos(pos));
        MachineProgressPacket msg = new MachineProgressPacket(ticksRemaining, totalTicks, pos);
        LevelChunk chunk = level.getChunkAt(pos);
        ModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), msg);
        LOGGER.trace(LogMarkers.NETWORK, "Packet sent");
    }

    private boolean isWaterlogged(BlockState state) {
        return state.getValue(LatexCollectorBlock.WATERLOGGED);
    }

    private boolean isEmpty(BlockState state) {
        return state.getValue(LatexCollectorBlock.FILL_STATUS) == LatexCollectorBlock.FillStatus.EMPTY;
    }

    public int getTicksRemaining() {
        return ticksRemaining;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        LOGGER.trace(LogMarkers.MACHINE, "Writing Latex Collector at {} to NBT...", LogHelper.blockPos(getBlockPos()));
        compound.putInt("totalTicks", totalTicks);
        compound.putInt("ticksRemaining", ticksRemaining);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        LOGGER.debug(LogMarkers.MACHINE, "Reading Latex Collector at {} from NBT...", LogHelper.blockPos(getBlockPos()));
        totalTicks = tag.getInt("totalTicks");
        ticksRemaining = tag.getInt("ticksRemaining");
        LOGGER.debug(LogMarkers.MACHINE, "Latex Collector Loaded!");
    }

    private void reset() {
        totalTicks = 0;
        ticksRemaining = 0;
        setChanged();
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (isWaterlogged(state) && ticksRemaining > 0) {
            LOGGER.debug(LogMarkers.MACHINE, "Latex Collector flooded! Stop filling");
            level.setBlockAndUpdate(pos, state.setValue(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.EMPTY));
            reset();
        }

        if (ticksRemaining <= 0) return;
        if (level.isClientSide()) return;
        ticksRemaining--;
        updateClient(level, pos);
        if (ticksRemaining > 0) return;
        LOGGER.debug(LogMarkers.MACHINE, "Latex Collector fill complete! Transitioning to FULL state...");
        level.setBlockAndUpdate(pos, state.setValue(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FULL));
        reset();
    }

    @Override
    public void onProgressUpdate(float progress, float total) {
        totalTicks = (int)total;
        ticksRemaining = (int)progress;
        LOGGER.trace(LogMarkers.MACHINE, "Latex Collector at {} synced progress with ticksRemaining {} and totalTicks {}", LogHelper.blockPos(getBlockPos()), ticksRemaining, totalTicks);
    }
}
