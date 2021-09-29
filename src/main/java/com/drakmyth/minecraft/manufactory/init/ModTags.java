/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag.Named;

public final class ModTags {
    public static final class Blocks {
        private static Named<Block> tag(String name) {
            return BlockTags.bind(Reference.MOD_ID + ":" + name);
        }

        public static final Named<Block> BLOCKS_WITH_LATEX = tag("blocks_with_latex");
        public static final Named<Block> MINEABLE_WITH_ROCK_DRILL = tag("mineable/rock_drill");
        public static final Named<Block> ROCK_DRILL_SILK_TOUCH = tag("rock_drill_silk_touch");
    }

    public static final class Items {
        private static Named<Item> tag(String name) {
            return ItemTags.bind(Reference.MOD_ID + ":" + name);
        }

        public static final Named<Item> UPGRADE_ACCESS_TOOL = tag("upgrade_access_tool");
    }
}
