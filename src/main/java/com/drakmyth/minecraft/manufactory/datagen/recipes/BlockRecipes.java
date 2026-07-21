package com.drakmyth.minecraft.manufactory.datagen.recipes;

import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Items;
import net.minecraft.tags.ItemTags;

public final class BlockRecipes extends RecipeProvider {

    public BlockRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    public void buildRecipes() {

        // Amber Block = 9 Amber
        shaped(RecipeCategory.MISC, ModBlocks.AMBER_BLOCK.get())
                .pattern("aaa")
                .pattern("aaa")
                .pattern("aaa")
                .define('a', ModItems.AMBER.get())
                .unlockedBy("has_amber", has(ModItems.AMBER.get()))
                .save(output);

        // Latex Collector = String + Bowl
        shapeless(RecipeCategory.MISC, ModBlocks.LATEX_COLLECTOR.get())
                .requires(Items.STRING)
                .requires(Items.BOWL)
                .unlockedBy("has_bowl", has(Items.BOWL))
                .save(output);

        // 3 Power Cable = 3 Rubber + 3 Redstone Wire
        shaped(RecipeCategory.MISC, ModBlocks.POWER_CABLE.get(), 3)
                .pattern("rrr")
                .pattern("www")
                .define('r', ModItems.RUBBER.get())
                .define('w', ModItems.REDSTONE_WIRE.get())
                .unlockedBy("has_rubber", has(ModItems.RUBBER.get()))
                .save(output);

        // Solar Panel = 3 Daylight Detector + 2 Nether Quartz + Redstone Dust + 2 Wooden Slab
        shaped(RecipeCategory.MISC, ModBlocks.SOLAR_PANEL.get())
                .pattern("ddd")
                .pattern("qrq")
                .pattern("wpw")
                .define('d', Items.DAYLIGHT_DETECTOR)
                .define('q', Items.QUARTZ)

                .define('r', Items.REDSTONE)
                .define('w', ItemTags.WOODEN_SLABS)
                .define('p', ModBlocks.POWER_CABLE.get())
                .unlockedBy("has_daylight_detector", has(Items.DAYLIGHT_DETECTOR))
                .save(output);

        // Grinder = 2 Coupling + 2 Redstone Wire + 2 Stone + Power Cable
        shaped(RecipeCategory.MISC, ModBlocks.GRINDER.get())
                .pattern("c c")
                .pattern("w w")
                .pattern("sps")
                .define('c', ModItems.COUPLING.get())

                .define('w', ModItems.REDSTONE_WIRE.get())
                .define('p', ModBlocks.POWER_CABLE.get())
                .define('s', Items.STONE)
                .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
                .save(output);

        // Ball Mill = 1 Coupling + 3 Redstone Wire + 4 Stone + Power Cable
        shaped(RecipeCategory.MISC, ModBlocks.BALL_MILL.get())
                .pattern("sws")
                .pattern("wcw")
                .pattern("sps")
                .define('c', ModItems.COUPLING.get())

                .define('w', ModItems.REDSTONE_WIRE.get())
                .define('p', ModBlocks.POWER_CABLE.get())
                .define('s', Items.STONE)
                .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
                .save(output);
    }
}
