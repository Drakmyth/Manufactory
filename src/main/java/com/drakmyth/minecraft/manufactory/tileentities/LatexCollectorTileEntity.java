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

    private boolean full = false;
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
        if (full || filling) {
            LOGGER.debug(String.format("Tapped, but filling: %s, full: %s", filling, full));
            return;
        }
        LOGGER.debug("Tapped, starting countdown...");
        filling = true;
        ticksRemaining = 20 * 60;
    }

    private void reset() {
        full = false;
        filling = false;
        ticksRemaining = 0;
    }

    private void setFull() {
        reset();
        full = true;
    }

    private boolean isWaterlogged(BlockState state) {
        return state.get(LatexCollectorBlock.WATERLOGGED);
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
        LOGGER.debug(String.format("filling: %s, full: %s, ticks: %d", filling, full, ticksRemaining));
        if (ticksRemaining > 0) return;
        setFull();
        world.setBlockState(getPos(), state.with(LatexCollectorBlock.FULL, true));
        markDirty();
    }
}
