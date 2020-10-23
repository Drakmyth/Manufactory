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
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class LatexCollectorTileEntity extends TileEntity implements ITickableTileEntity {
    private static final Logger LOGGER = LogManager.getLogger();

    private int ticksRemaining = 0;
    private boolean filling = false;

    public LatexCollectorTileEntity() {
        super(ModTileEntityTypes.LATEX_COLLECTOR.get());
    }

    public void onTap() {
        BlockState state = getBlockState();
        if (isWaterlogged(state)) {
            LOGGER.debug("Tapped, but waterlogged");
            return;
        }
        if (isFull(state)) {
            LOGGER.debug("Tapped, but full");
            return;
        }
        if (filling) {
            LOGGER.debug("Tapped, but already filling");
            return;
        }
        LOGGER.debug("Tapped, starting countdown...");
        filling = true;
        ticksRemaining = 20 * 60;  // TODO: Read fill time from config
    }

    private void reset() {
        filling = false;
        ticksRemaining = 0;
    }

    private boolean isWaterlogged(BlockState state) {
        return state.get(LatexCollectorBlock.WATERLOGGED);
    }

    private boolean isFull(BlockState state) {
        return state.get(LatexCollectorBlock.FULL);
    }

    @Override
    public void tick() {
        BlockState state = getBlockState();
        if (isWaterlogged(state)) reset();
        if (!filling) return;
        if (!this.hasWorld()) return;
        World world = this.getWorld();
        if (world.isRemote) return;
        ticksRemaining--;
        LOGGER.debug(String.format("filling: %s, ticks: %d", filling, ticksRemaining));
        if (ticksRemaining > 0) return;
        world.setBlockState(getPos(), state.with(LatexCollectorBlock.FULL, true));
        reset();
        markDirty();
    }
}
