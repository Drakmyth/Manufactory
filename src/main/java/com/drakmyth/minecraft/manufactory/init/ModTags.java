/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag.Named;

public final class ModTags {
    public static final class Blocks {
        private static Named<Block> tag(String name) {
            return BlockTags.bind(Reference.MOD_ID + ":" + name);
        }

        public static final Named<Block> BLOCKS_WITH_LATEX = tag("blocks_with_latex");
    }
}
