/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction.Plane;

public class LatexCollectorBlock extends ManufactoryBlock {

    public static final String REGISTRY_NAME = "latex_collector";

    public LatexCollectorBlock() {
        super(REGISTRY_NAME, defaultBlockProperties(Material.IRON), defaultItemProperties().maxStackSize(16));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return Plane.HORIZONTAL.test(context.getFace()) ? getDefaultState() : null;
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
    }
}
