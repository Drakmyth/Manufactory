/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BallMillContainerProvider implements INamedContainerProvider {
    private BlockPos pos;

    public BallMillContainerProvider(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        return new BallMillContainer(windowId, new InvWrapper(playerInventory), player, pos);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Ball Mill");
    }
}
