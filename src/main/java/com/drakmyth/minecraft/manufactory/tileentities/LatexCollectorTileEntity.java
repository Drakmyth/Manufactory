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

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.PacketDistributor;

public class LatexCollectorTileEntity extends TileEntity implements ITickableTileEntity, IMachineProgressListener {
    private static final Logger LOGGER = LogManager.getLogger();

    private int totalTicks = 0;
    private int ticksRemaining = 0;

    public LatexCollectorTileEntity() {
        super(ModTileEntityTypes.LATEX_COLLECTOR.get());
    }

    public boolean onTap() {
        BlockState state = getBlockState();
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

        World world = getWorld();
        if (world.isRemote()) return true;
        world.setBlockState(getPos(), state.with(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING));
        updateClient();
        markDirty();
        return true;
    }

    private void updateClient() {
        LOGGER.trace("Sending MachineProgress packet to update animation at (%d, %d, %d)...", getPos().getX(), getPos().getY(), getPos().getZ());
        MachineProgressPacket msg = new MachineProgressPacket(ticksRemaining, totalTicks, getPos());
        Chunk chunk = world.getChunkAt(getPos());
        ModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), msg);
        LOGGER.trace("Packet sent");
    }

    private boolean isWaterlogged(BlockState state) {
        return state.get(LatexCollectorBlock.WATERLOGGED);
    }

    private boolean isEmpty(BlockState state) {
        return state.get(LatexCollectorBlock.FILL_STATUS) == LatexCollectorBlock.FillStatus.EMPTY;
    }

    public int getTicksRemaining() {
        return ticksRemaining;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        LOGGER.trace("Writing Latex Collector at (%d, %d, %d) to NBT...", getPos().getX(), getPos().getY(), getPos().getZ());
        compound.putInt("totalTicks", totalTicks);
        compound.putInt("ticksRemaining", ticksRemaining);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        LOGGER.debug("Reading Latex Collector at (%d, %d, %d) from NBT...", getPos().getX(), getPos().getY(), getPos().getZ());
        totalTicks = nbt.getInt("totalTicks");
        ticksRemaining = nbt.getInt("ticksRemaining");
        LOGGER.debug("Latex Collector Loaded!");
    }

    private void reset() {
        totalTicks = 0;
        ticksRemaining = 0;
        markDirty();
    }

    @Override
    public void tick() {
        BlockState state = getBlockState();
        if (isWaterlogged(state) && ticksRemaining > 0) {
            LOGGER.debug("Latex Collector flooded! Stop filling");
            reset();
        }

        if (ticksRemaining <= 0) return;
        if (!this.hasWorld()) {
            LOGGER.warn("Latex Collector at (%d, %d, %d) has no world!", getPos().getX(), getPos().getY(), getPos().getZ());
            return;
        }

        World world = this.getWorld();
        if (world.isRemote) return;
        ticksRemaining--;
        updateClient();
        if (ticksRemaining > 0) return;
        LOGGER.debug("Latex Collector fill complete! Transitioning to FULL state...");
        world.setBlockState(getPos(), state.with(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FULL));
        reset();
    }

    @Override
    public void onProgressUpdate(float progress, float total) {
        totalTicks = (int)total;
        ticksRemaining = (int)progress;
        LOGGER.debug("Latex Collector at (%d, %d, %d) synced progress with ticksRemaining %d and totalTicks %d", getPos().getX(), getPos().getY(), getPos().getZ(), ticksRemaining, totalTicks);
    }
}
