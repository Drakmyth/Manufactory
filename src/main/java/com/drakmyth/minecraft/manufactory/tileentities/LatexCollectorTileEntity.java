/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.tileentities;

import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.init.ModTileEntityTypes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class LatexCollectorTileEntity extends TileEntity implements ITickableTileEntity {
    private static final Logger LOGGER = LogManager.getLogger();

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
        if (isFull(state)) {
            LOGGER.debug("Tapped, but full");
            return false;
        }
        if (ticksRemaining > 0) {
            LOGGER.debug("Tapped, but already filling");
            return false;
        }
        LOGGER.debug("Tapped, starting countdown...");
        ticksRemaining = 20 * 60;  // TODO: Read fill time from config
        return true;
    }

    private boolean isWaterlogged(BlockState state) {
        return state.get(LatexCollectorBlock.WATERLOGGED);
    }

    private boolean isFull(BlockState state) {
        return state.get(LatexCollectorBlock.FULL);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("ticksRemaining", ticksRemaining);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        ticksRemaining = nbt.getInt("ticksRemaining");
    }

    private void reset() {
        ticksRemaining = 0;
        markDirty();
    }

    @Override
    public void tick() {
        BlockState state = getBlockState();
        if (isWaterlogged(state) && ticksRemaining > 0) reset();
        if (ticksRemaining <= 0) return;
        if (!this.hasWorld()) return;
        World world = this.getWorld();
        if (world.isRemote) return;
        ticksRemaining--;
        if (ticksRemaining > 0) return;
        world.setBlockState(getPos(), state.with(LatexCollectorBlock.FULL, true));
        reset();
    }
}
