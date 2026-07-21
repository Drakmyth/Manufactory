package com.drakmyth.minecraft.manufactory.datagen.recipes;

import com.drakmyth.minecraft.manufactory.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Items;
import net.minecraft.tags.ItemTags;

public final class MachineUpgradeRecipes extends RecipeProvider {
    public MachineUpgradeRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    public void buildRecipes() {
        buildGrinderWheelRecipes(output);
        buildMotorRecipes(output);
        buildMillingBallRecipes(output);
        buildPowerUpgradeRecipes(output);
        buildDrillHeadRecipes(output);
    }

    private void buildGrinderWheelRecipes(RecipeOutput output) {
        // Grinder Wheel Tier 0 = Coupling + 4 Planks
        shaped(RecipeCategory.MISC, ModItems.GRINDER_WHEEL_TIER0.get())
                .pattern(" p ")
                .pattern("pcp")
                .pattern(" p ")
                .define('c', ModItems.COUPLING.get())
                .define('p', ItemTags.PLANKS)
                .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
                .save(output);

        // Grinder Wheel Tier 1 = Coupling + 4 Cobblestone
        shaped(RecipeCategory.MISC, ModItems.GRINDER_WHEEL_TIER1.get())
                .pattern(" s ")
                .pattern("scs")
                .pattern(" s ")
                .define('c', ModItems.COUPLING.get())
                .define('s', ItemTags.STONE_CRAFTING_MATERIALS)
                .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
                .save(output);

        // Grinder Wheel Tier 2 = Coupling + 4 Iron Ingot
        shaped(RecipeCategory.MISC, ModItems.GRINDER_WHEEL_TIER2.get())
                .pattern(" i ")
                .pattern("ici")
                .pattern(" i ")
                .define('c', ModItems.COUPLING.get())
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
                .save(output);

        // Grinder Wheel Tier 3 = Coupling + 4 Diamond
        shaped(RecipeCategory.MISC, ModItems.GRINDER_WHEEL_TIER3.get())
                .pattern(" d ")
                .pattern("dcd")
                .pattern(" d ")
                .define('c', ModItems.COUPLING.get())
                .define('d', Items.DIAMOND)
                .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
                .save(output);

        // Grinder Wheel Tier 4 = Coupling + 4 Netherite Ingot
        shaped(RecipeCategory.MISC, ModItems.GRINDER_WHEEL_TIER4.get())
                .pattern(" n ")
                .pattern("ncn")
                .pattern(" n ")
                .define('c', ModItems.COUPLING.get())
                .define('n', Items.NETHERITE_INGOT)
                .unlockedBy("has_coupling", has(ModItems.COUPLING.get()))
                .save(output);
    }

    private void buildMotorRecipes(RecipeOutput output) {
        // Motor Tier 0 = Coupling + Redstone Dust + 3 Iron Ingot
        shaped(RecipeCategory.MISC, ModItems.MOTOR_TIER0.get())
                .pattern(" i ")
                .pattern("cwi")
                .pattern(" i ")
                .define('i', Items.IRON_INGOT)
                .define('c', ModItems.COUPLING.get())
                .define('w', ModItems.REDSTONE_WIRE.get())
                .unlockedBy("has_redstone", has(ModItems.REDSTONE_WIRE.get()))
                .save(output);

        // Motor Tier 1 = Motor Tier 0 + Gold Ingot
        shapeless(RecipeCategory.MISC, ModItems.MOTOR_TIER1.get())
                .requires(ModItems.MOTOR_TIER0.get())
                .requires(Items.GOLD_INGOT)
                .unlockedBy("has_motor_tier_0", has(ModItems.MOTOR_TIER0.get()))
                .save(output);

        // Motor Tier 2 = Motor Tier 1 + Diamond
        shapeless(RecipeCategory.MISC, ModItems.MOTOR_TIER2.get())
                .requires(ModItems.MOTOR_TIER1.get())
                .requires(Items.DIAMOND)
                .unlockedBy("has_motor_tier_1", has(ModItems.MOTOR_TIER1.get()))
                .save(output);

        // Motor Tier 3 = Motor Tier 2 + Netherite Ingot
        shapeless(RecipeCategory.MISC, ModItems.MOTOR_TIER3.get())
                .requires(ModItems.MOTOR_TIER2.get())
                .requires(Items.NETHERITE_INGOT)
                .unlockedBy("has_motor_tier_2", has(ModItems.MOTOR_TIER2.get()))
                .save(output);
    }

    private void buildMillingBallRecipes(RecipeOutput output) {
        // 4 Milling Ball Tier 0 = 4 Planks
        shaped(RecipeCategory.MISC, ModItems.MILLING_BALL_TIER0.get(), 4)
                .pattern(" p ")
                .pattern("p p")
                .pattern(" p ")
                .define('p', ItemTags.PLANKS)
                .unlockedBy("has_wood_planks", has(ItemTags.PLANKS))
                .save(output);

        // Milling Ball Tier 1 = Milling Ball Tier 0 + Cobblestone
        shapeless(RecipeCategory.MISC, ModItems.MILLING_BALL_TIER1.get())
                .requires(ModItems.MILLING_BALL_TIER0.get())
                .requires(Items.COBBLESTONE)
                .unlockedBy("has_milling_ball_tier0", has(ModItems.MILLING_BALL_TIER0.get()))
                .save(output);

        // Milling Ball Tier 2 = Milling Ball Tier 1 + Iron Ingot
        shapeless(RecipeCategory.MISC, ModItems.MILLING_BALL_TIER2.get())
                .requires(ModItems.MILLING_BALL_TIER1.get())
                .requires(Items.IRON_INGOT)
                .unlockedBy("has_milling_ball_tier1", has(ModItems.MILLING_BALL_TIER1.get()))
                .save(output);

        // Milling Ball Tier 3 = Milling Ball Tier 2 + Diamond
        shapeless(RecipeCategory.MISC, ModItems.MILLING_BALL_TIER3.get())
                .requires(ModItems.MILLING_BALL_TIER2.get())
                .requires(Items.DIAMOND)
                .unlockedBy("has_milling_ball_tier2", has(ModItems.MILLING_BALL_TIER2.get()))
                .save(output);

        // Milling Ball Tier 4 = Milling Ball Tier 3 + Netherite Ingot
        shapeless(RecipeCategory.MISC, ModItems.MILLING_BALL_TIER4.get())
                .requires(ModItems.MILLING_BALL_TIER3.get())
                .requires(Items.NETHERITE_INGOT)
                .unlockedBy("has_milling_ball_tier3", has(ModItems.MILLING_BALL_TIER3.get()))
                .save(output);
    }

    private void buildPowerUpgradeRecipes(RecipeOutput output) {
        // Power Socket = 3 Redstone Wire + 6 Coagulated Latex
        shaped(RecipeCategory.MISC, ModItems.POWER_SOCKET.get())
                .pattern("wlw")
                .pattern("lll")
                .pattern("lwl")
                .define('w', ModItems.REDSTONE_WIRE.get())
                .define('l', ModItems.COAGULATED_LATEX.get())
                .unlockedBy("has_redstone_wire", has(ModItems.REDSTONE_WIRE.get()))
                .save(output);
    }

    private void buildDrillHeadRecipes(RecipeOutput output) {
        // Drill Head Tier 0 = 5 Iron Ingot
        shaped(RecipeCategory.MISC, ModItems.DRILL_HEAD_TIER0.get())
                .pattern("p  ")
                .pattern("ppp")
                .pattern("p  ")
                .define('p', ItemTags.PLANKS)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(output);

        // Drill Head Tier 1 = Drill Head Tier 0 + Cobblestone
        shapeless(RecipeCategory.MISC, ModItems.DRILL_HEAD_TIER1.get())
                .requires(ModItems.DRILL_HEAD_TIER0.get())
                .requires(Items.COBBLESTONE)
                .unlockedBy("has_drill_head_tier0", has(ModItems.DRILL_HEAD_TIER0.get()))
                .save(output);

        // Drill Head Tier 2 = Drill Head Tier 1 + Iron Ingot
        shapeless(RecipeCategory.MISC, ModItems.DRILL_HEAD_TIER2.get())
                .requires(ModItems.DRILL_HEAD_TIER1.get())
                .requires(Items.IRON_INGOT)
                .unlockedBy("has_drill_head_tier0", has(ModItems.DRILL_HEAD_TIER1.get()))
                .save(output);

        // Drill Head Tier 3 = Drill Head Tier 2 + Diamond
        shapeless(RecipeCategory.MISC, ModItems.DRILL_HEAD_TIER3.get())
                .requires(ModItems.DRILL_HEAD_TIER2.get())
                .requires(Items.DIAMOND)
                .unlockedBy("has_drill_head_tier0", has(ModItems.DRILL_HEAD_TIER2.get()))
                .save(output);

        // Drill Head Tier 4 = Drill Head Tier 3 + Netherite Ingot
        shapeless(RecipeCategory.MISC, ModItems.DRILL_HEAD_TIER4.get())
                .requires(ModItems.DRILL_HEAD_TIER3.get())
                .requires(Items.NETHERITE_INGOT)
                .unlockedBy("has_drill_head_tier0", has(ModItems.DRILL_HEAD_TIER3.get()))
                .save(output);
    }
}
