/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.tileentities;

import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.config.ConfigData;
import com.drakmyth.minecraft.manufactory.init.ModTileEntityTypes;
import com.drakmyth.minecraft.manufactory.network.IMachineProgressListener;
import com.drakmyth.minecraft.manufactory.network.MachineProgressPacket;
import com.drakmyth.minecraft.manufactory.network.ModPacketHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

public class LatexCollectorTileEntity extends BlockEntity implements IMachineProgressListener {
    private static final Logger LOGGER = LogManager.getLogger();

    private int totalTicks = 0;
    private int ticksRemaining = 0;

    public LatexCollectorTileEntity(BlockPos pos, BlockState state) {
        super(ModTileEntityTypes.LATEX_COLLECTOR.get(), pos, state);
    }

    public boolean onTap(Level level, BlockPos pos, BlockState state) {
        if (isWaterlogged(state)) {
            LOGGER.debug("Tapped, but waterlogged");
            return false;
        }
        if (!isEmpty(state)) {
            LOGGER.debug("Tapped, but not empty");
            return false;
        }
        if (ticksRemaining > 0) {
            LOGGER.debug("Tapped, but already filling");
            return false;
        }
        LOGGER.debug("Tapped, starting countdown...");

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
        LOGGER.trace("Sending MachineProgress packet to update animation at (%d, %d, %d)...", pos.getX(), pos.getY(), pos.getZ());
        MachineProgressPacket msg = new MachineProgressPacket(ticksRemaining, totalTicks, pos);
        LevelChunk chunk = level.getChunkAt(pos);
        ModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), msg);
        LOGGER.trace("Packet sent");
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
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        LOGGER.trace("Writing Latex Collector at (%d, %d, %d) to NBT...", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
        compound.putInt("totalTicks", totalTicks);
        compound.putInt("ticksRemaining", ticksRemaining);
        return compound;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        LOGGER.debug("Reading Latex Collector at (%d, %d, %d) from NBT...", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
        totalTicks = nbt.getInt("totalTicks");
        ticksRemaining = nbt.getInt("ticksRemaining");
        LOGGER.debug("Latex Collector Loaded!");
    }

    private void reset() {
        totalTicks = 0;
        ticksRemaining = 0;
        setChanged();
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (isWaterlogged(state) && ticksRemaining > 0) {
            LOGGER.debug("Latex Collector flooded! Stop filling");
            level.setBlockAndUpdate(pos, state.setValue(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.EMPTY));
            reset();
        }

        if (ticksRemaining <= 0) return;
        if (level.isClientSide) return;
        ticksRemaining--;
        updateClient(level, pos);
        if (ticksRemaining > 0) return;
        LOGGER.debug("Latex Collector fill complete! Transitioning to FULL state...");
        level.setBlockAndUpdate(pos, state.setValue(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FULL));
        reset();
    }

    @Override
    public void onProgressUpdate(float progress, float total) {
        totalTicks = (int)total;
        ticksRemaining = (int)progress;
        LOGGER.debug("Latex Collector at (%d, %d, %d) synced progress with ticksRemaining %d and totalTicks %d", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), ticksRemaining, totalTicks);
    }
}
