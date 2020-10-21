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
        if (full || filling) {
            LOGGER.debug(String.format("Tapped, but filling: %s, full: %s", filling, full));
            return;
        }
        LOGGER.debug(String.format("Tapped, starting countdown..."));
        filling = true;
        ticksRemaining = 20 * 60;
    }

    @Override
    public void tick() {
        if (!filling) return;
        if (!this.hasWorld()) return;
        World world = this.getWorld();
        if (world.isRemote) return;
        ticksRemaining--;
        LOGGER.debug(String.format("filling: %s, full: %s, ticks: %d", filling, full, ticksRemaining));
        if (ticksRemaining > 0) return;
        filling = false;
        full = true;
        BlockState bs = getBlockState().with(LatexCollectorBlock.FULL, true);
        world.setBlockState(getPos(), bs);
        markDirty();
    }
}
