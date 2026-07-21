package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.items.upgrades.BatteryItem;
import com.drakmyth.minecraft.manufactory.items.upgrades.DrillHeadUpgradeItem;
import com.drakmyth.minecraft.manufactory.items.upgrades.GrinderWheelUpgradeItem;
import com.drakmyth.minecraft.manufactory.items.upgrades.MillingBallTier0UpgradeItem;
import com.drakmyth.minecraft.manufactory.items.upgrades.MillingBallUpgradeItem;
import com.drakmyth.minecraft.manufactory.items.upgrades.MotorUpgradeItem;
import com.drakmyth.minecraft.manufactory.items.upgrades.PowerSocketItem;
import com.drakmyth.minecraft.manufactory.items.RockDrillItem;
import com.drakmyth.minecraft.manufactory.items.TappingKnifeItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MOD_ID);

    // Motor Tiers
    public static final DeferredHolder<Item, Item> MOTOR_TIER0 =
            ITEMS.registerItem("motor_tier0", properties -> new MotorUpgradeItem(properties.stacksTo(1), 1.0f, ToolMaterial.WOOD.speed()));
    public static final DeferredHolder<Item, Item> MOTOR_TIER1 =
            ITEMS.registerItem("motor_tier1", properties -> new MotorUpgradeItem(properties.stacksTo(1), 2.0f, ToolMaterial.STONE.speed()));
    public static final DeferredHolder<Item, Item> MOTOR_TIER2 =
            ITEMS.registerItem("motor_tier2", properties -> new MotorUpgradeItem(properties.stacksTo(1), 4.0f, ToolMaterial.IRON.speed()));
    public static final DeferredHolder<Item, Item> MOTOR_TIER3 =
            ITEMS.registerItem("motor_tier3", properties -> new MotorUpgradeItem(properties.stacksTo(1), 8.0f, ToolMaterial.DIAMOND.speed()));

    // Grinder Wheels
    public static final DeferredHolder<Item, Item> GRINDER_WHEEL_TIER0 =
            ITEMS.registerItem("grinder_wheel_tier0", properties -> new GrinderWheelUpgradeItem(properties.stacksTo(1), ToolMaterial.WOOD, 0f));
    public static final DeferredHolder<Item, Item> GRINDER_WHEEL_TIER1 =
            ITEMS.registerItem("grinder_wheel_tier1", properties -> new GrinderWheelUpgradeItem(properties.stacksTo(1), ToolMaterial.STONE, 0.25f));
    public static final DeferredHolder<Item, Item> GRINDER_WHEEL_TIER2 =
            ITEMS.registerItem("grinder_wheel_tier2", properties -> new GrinderWheelUpgradeItem(properties.stacksTo(1), ToolMaterial.IRON, 0.5f));
    public static final DeferredHolder<Item, Item> GRINDER_WHEEL_TIER3 =
            ITEMS.registerItem("grinder_wheel_tier3", properties -> new GrinderWheelUpgradeItem(properties.stacksTo(1), ToolMaterial.DIAMOND, 0.75f));
    public static final DeferredHolder<Item, Item> GRINDER_WHEEL_TIER4 =
            ITEMS.registerItem("grinder_wheel_tier4", properties -> new GrinderWheelUpgradeItem(properties.stacksTo(1), ToolMaterial.NETHERITE, 1f));

    // Milling Balls
    public static final DeferredHolder<Item, Item> MILLING_BALL_TIER0 =
            ITEMS.registerItem("milling_ball_tier0", properties -> new MillingBallTier0UpgradeItem(properties.stacksTo(16)));
    public static final DeferredHolder<Item, Item> MILLING_BALL_TIER1 =
            ITEMS.registerItem("milling_ball_tier1", properties -> new MillingBallUpgradeItem(properties.stacksTo(16), ToolMaterial.STONE, 0.25f));
    public static final DeferredHolder<Item, Item> MILLING_BALL_TIER2 =
            ITEMS.registerItem("milling_ball_tier2", properties -> new MillingBallUpgradeItem(properties.stacksTo(16), ToolMaterial.IRON, 0.5f));
    public static final DeferredHolder<Item, Item> MILLING_BALL_TIER3 =
            ITEMS.registerItem("milling_ball_tier3", properties -> new MillingBallUpgradeItem(properties.stacksTo(16), ToolMaterial.DIAMOND, 0.75f));
    public static final DeferredHolder<Item, Item> MILLING_BALL_TIER4 =
            ITEMS.registerItem("milling_ball_tier4", properties -> new MillingBallUpgradeItem(properties.stacksTo(16), ToolMaterial.NETHERITE, 1f));

    // Drill Heads
    public static final DeferredHolder<Item, Item> DRILL_HEAD_TIER0 = ITEMS.registerItem("drill_head_tier0", properties -> new DrillHeadUpgradeItem(properties.stacksTo(1), ToolMaterial.WOOD));
    public static final DeferredHolder<Item, Item> DRILL_HEAD_TIER1 = ITEMS.registerItem("drill_head_tier1", properties -> new DrillHeadUpgradeItem(properties.stacksTo(1), ToolMaterial.STONE));
    public static final DeferredHolder<Item, Item> DRILL_HEAD_TIER2 = ITEMS.registerItem("drill_head_tier2", properties -> new DrillHeadUpgradeItem(properties.stacksTo(1), ToolMaterial.IRON));
    public static final DeferredHolder<Item, Item> DRILL_HEAD_TIER3 = ITEMS.registerItem("drill_head_tier3", properties -> new DrillHeadUpgradeItem(properties.stacksTo(1), ToolMaterial.DIAMOND));
    public static final DeferredHolder<Item, Item> DRILL_HEAD_TIER4 = ITEMS.registerItem("drill_head_tier4", properties -> new DrillHeadUpgradeItem(properties.stacksTo(1), ToolMaterial.NETHERITE));

    // Ground Ores
    public static final DeferredHolder<Item, Item> GROUND_COAL_ORE_ROUGH = ITEMS.registerItem("ground_coal_ore_rough", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_DIAMOND_ORE_ROUGH = ITEMS.registerItem("ground_diamond_ore_rough", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_EMERALD_ORE_ROUGH = ITEMS.registerItem("ground_emerald_ore_rough", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_GOLD_ORE_ROUGH = ITEMS.registerItem("ground_gold_ore_rough", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_IRON_ORE_ROUGH = ITEMS.registerItem("ground_iron_ore_rough", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_COPPER_ORE_ROUGH = ITEMS.registerItem("ground_copper_ore_rough", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_LAPIS_ORE_ROUGH = ITEMS.registerItem("ground_lapis_ore_rough", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_NETHER_QUARTZ_ORE_ROUGH = ITEMS.registerItem("ground_nether_quartz_ore_rough", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_REDSTONE_ORE_ROUGH = ITEMS.registerItem("ground_redstone_ore_rough", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_ANCIENT_DEBRIS_ROUGH = ITEMS.registerItem("ground_ancient_debris_rough", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_COAL_ORE_FINE = ITEMS.registerItem("ground_coal_ore_fine", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_DIAMOND_ORE_FINE = ITEMS.registerItem("ground_diamond_ore_fine", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_EMERALD_ORE_FINE = ITEMS.registerItem("ground_emerald_ore_fine", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_GOLD_ORE_FINE = ITEMS.registerItem("ground_gold_ore_fine", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_IRON_ORE_FINE = ITEMS.registerItem("ground_iron_ore_fine", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_COPPER_ORE_FINE = ITEMS.registerItem("ground_copper_ore_fine", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_LAPIS_ORE_FINE = ITEMS.registerItem("ground_lapis_ore_fine", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_NETHER_QUARTZ_ORE_FINE = ITEMS.registerItem("ground_nether_quartz_ore_fine", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_REDSTONE_ORE_FINE = ITEMS.registerItem("ground_redstone_ore_fine", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> GROUND_ANCIENT_DEBRIS_FINE = ITEMS.registerItem("ground_ancient_debris_fine", properties -> new Item(properties.stacksTo(64)));

    // Slurried Ore Buckets
    public static final DeferredHolder<Item, Item> SLURRIED_COAL_ORE_BUCKET =
            ITEMS.registerItem("slurried_coal_ore_bucket", properties -> new BucketItem(ModFluids.SLURRIED_COAL_ORE.get(), properties.stacksTo(1)));
    public static final DeferredHolder<Item, Item> SLURRIED_DIAMOND_ORE_BUCKET =
            ITEMS.registerItem("slurried_diamond_ore_bucket", properties -> new BucketItem(ModFluids.SLURRIED_DIAMOND_ORE.get(), properties.stacksTo(1)));
    public static final DeferredHolder<Item, Item> SLURRIED_EMERALD_ORE_BUCKET =
            ITEMS.registerItem("slurried_emerald_ore_bucket", properties -> new BucketItem(ModFluids.SLURRIED_EMERALD_ORE.get(), properties.stacksTo(1)));
    public static final DeferredHolder<Item, Item> SLURRIED_GOLD_ORE_BUCKET =
            ITEMS.registerItem("slurried_gold_ore_bucket", properties -> new BucketItem(ModFluids.SLURRIED_GOLD_ORE.get(), properties.stacksTo(1)));
    public static final DeferredHolder<Item, Item> SLURRIED_IRON_ORE_BUCKET =
            ITEMS.registerItem("slurried_iron_ore_bucket", properties -> new BucketItem(ModFluids.SLURRIED_IRON_ORE.get(), properties.stacksTo(1)));
    public static final DeferredHolder<Item, Item> SLURRIED_COPPER_ORE_BUCKET =
            ITEMS.registerItem("slurried_copper_ore_bucket", properties -> new BucketItem(ModFluids.SLURRIED_COPPER_ORE.get(), properties.stacksTo(1)));
    public static final DeferredHolder<Item, Item> SLURRIED_LAPIS_ORE_BUCKET =
            ITEMS.registerItem("slurried_lapis_ore_bucket", properties -> new BucketItem(ModFluids.SLURRIED_LAPIS_ORE.get(), properties.stacksTo(1)));
    public static final DeferredHolder<Item, Item> SLURRIED_NETHER_QUARTZ_ORE_BUCKET =
            ITEMS.registerItem("slurried_nether_quartz_ore_bucket", properties -> new BucketItem(ModFluids.SLURRIED_NETHER_QUARTZ_ORE.get(), properties.stacksTo(1)));
    public static final DeferredHolder<Item, Item> SLURRIED_REDSTONE_ORE_BUCKET =
            ITEMS.registerItem("slurried_redstone_ore_bucket", properties -> new BucketItem(ModFluids.SLURRIED_REDSTONE_ORE.get(), properties.stacksTo(1)));
    public static final DeferredHolder<Item, Item> SLURRIED_ANCIENT_DEBRIS_BUCKET =
            ITEMS.registerItem("slurried_ancient_debris_bucket", properties -> new BucketItem(ModFluids.SLURRIED_ANCIENT_DEBRIS.get(), properties.stacksTo(1)));

    // Other Items
    public static final DeferredHolder<Item, Item> AMBER = ITEMS.registerItem("amber", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> POWER_SOCKET = ITEMS.registerItem("power_socket", properties -> new PowerSocketItem(properties.stacksTo(1)));
    public static final DeferredHolder<Item, Item> BATTERY = ITEMS.registerItem("battery", properties -> new BatteryItem(properties.stacksTo(1)));
    public static final DeferredHolder<Item, Item> REDSTONE_WIRE = ITEMS.registerItem("redstone_wire", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> COUPLING = ITEMS.registerItem("coupling", properties -> new Item(properties.stacksTo(16)));
    public static final DeferredHolder<Item, Item> WRENCH = ITEMS.registerItem("wrench", properties -> new Item(properties.stacksTo(1)));
    public static final DeferredHolder<Item, Item> COAGULATED_LATEX = ITEMS.registerItem("coagulated_latex", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> RUBBER = ITEMS.registerItem("rubber", properties -> new Item(properties.stacksTo(64)));
    public static final DeferredHolder<Item, Item> TAPPING_KNIFE = ITEMS.registerItem("tapping_knife", properties -> new TappingKnifeItem(properties.stacksTo(1)));
    public static final DeferredHolder<Item, Item> ROCK_DRILL = ITEMS.registerItem("rock_drill", properties -> new RockDrillItem(properties.stacksTo(1)));

}
