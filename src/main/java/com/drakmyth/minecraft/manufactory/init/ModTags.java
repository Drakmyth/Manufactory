package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;

public final class ModTags {
    public static final class Blocks {
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(Reference.MOD_ID, name));
        }

        public static final TagKey<Block> BLOCKS_WITH_LATEX = tag("blocks_with_latex");
        public static final TagKey<Block> MINEABLE_WITH_ROCK_DRILL = tag("mineable/rock_drill");
        public static final TagKey<Block> ROCK_DRILL_SILK_TOUCH = tag("rock_drill_silk_touch");
    }

    public static final class Items {
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(Reference.MOD_ID, name));
        }

        public static final TagKey<Item> UPGRADE_ACCESS_TOOL = tag("upgrade_access_tool");
    }
}
