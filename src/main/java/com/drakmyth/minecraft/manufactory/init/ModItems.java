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
import net.minecraft.core.registries.BuiltInRegistries;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEMS, Reference.MOD_ID);

    // Motor Tiers
    public static final DeferredHolder<Item, Item> MOTOR_TIER0 =
            ITEMS.register("motor_tier0", () -> new MotorUpgradeItem(singleItemProperties(), 1.0f, ToolMaterial.WOOD.speed()));
    public static final DeferredHolder<Item, Item> MOTOR_TIER1 =
            ITEMS.register("motor_tier1", () -> new MotorUpgradeItem(singleItemProperties(), 2.0f, ToolMaterial.STONE.speed()));
    public static final DeferredHolder<Item, Item> MOTOR_TIER2 =
            ITEMS.register("motor_tier2", () -> new MotorUpgradeItem(singleItemProperties(), 4.0f, ToolMaterial.IRON.speed()));
    public static final DeferredHolder<Item, Item> MOTOR_TIER3 =
            ITEMS.register("motor_tier3", () -> new MotorUpgradeItem(singleItemProperties(), 8.0f, ToolMaterial.DIAMOND.speed()));

    // Grinder Wheels
    public static final DeferredHolder<Item, Item> GRINDER_WHEEL_TIER0 =
            ITEMS.register("grinder_wheel_tier0", () -> new GrinderWheelUpgradeItem(singleItemProperties(), ToolMaterial.WOOD, 0f));
    public static final DeferredHolder<Item, Item> GRINDER_WHEEL_TIER1 =
            ITEMS.register("grinder_wheel_tier1", () -> new GrinderWheelUpgradeItem(singleItemProperties(), ToolMaterial.STONE, 0.25f));
    public static final DeferredHolder<Item, Item> GRINDER_WHEEL_TIER2 =
            ITEMS.register("grinder_wheel_tier2", () -> new GrinderWheelUpgradeItem(singleItemProperties(), ToolMaterial.IRON, 0.5f));
    public static final DeferredHolder<Item, Item> GRINDER_WHEEL_TIER3 =
            ITEMS.register("grinder_wheel_tier3", () -> new GrinderWheelUpgradeItem(singleItemProperties(), ToolMaterial.DIAMOND, 0.75f));
    public static final DeferredHolder<Item, Item> GRINDER_WHEEL_TIER4 =
            ITEMS.register("grinder_wheel_tier4", () -> new GrinderWheelUpgradeItem(singleItemProperties(), ToolMaterial.NETHERITE, 1f));

    // Milling Balls
    public static final DeferredHolder<Item, Item> MILLING_BALL_TIER0 =
            ITEMS.register("milling_ball_tier0", () -> new MillingBallTier0UpgradeItem(smallStackItemProperties()));
    public static final DeferredHolder<Item, Item> MILLING_BALL_TIER1 =
            ITEMS.register("milling_ball_tier1", () -> new MillingBallUpgradeItem(smallStackItemProperties(), ToolMaterial.STONE, 0.25f));
    public static final DeferredHolder<Item, Item> MILLING_BALL_TIER2 =
            ITEMS.register("milling_ball_tier2", () -> new MillingBallUpgradeItem(smallStackItemProperties(), ToolMaterial.IRON, 0.5f));
    public static final DeferredHolder<Item, Item> MILLING_BALL_TIER3 =
            ITEMS.register("milling_ball_tier3", () -> new MillingBallUpgradeItem(smallStackItemProperties(), ToolMaterial.DIAMOND, 0.75f));
    public static final DeferredHolder<Item, Item> MILLING_BALL_TIER4 =
            ITEMS.register("milling_ball_tier4", () -> new MillingBallUpgradeItem(smallStackItemProperties(), ToolMaterial.NETHERITE, 1f));

    // Drill Heads
    public static final DeferredHolder<Item, Item> DRILL_HEAD_TIER0 = ITEMS.register("drill_head_tier0", () -> new DrillHeadUpgradeItem(singleItemProperties(), ToolMaterial.WOOD));
    public static final DeferredHolder<Item, Item> DRILL_HEAD_TIER1 = ITEMS.register("drill_head_tier1", () -> new DrillHeadUpgradeItem(singleItemProperties(), ToolMaterial.STONE));
    public static final DeferredHolder<Item, Item> DRILL_HEAD_TIER2 = ITEMS.register("drill_head_tier2", () -> new DrillHeadUpgradeItem(singleItemProperties(), ToolMaterial.IRON));
    public static final DeferredHolder<Item, Item> DRILL_HEAD_TIER3 = ITEMS.register("drill_head_tier3", () -> new DrillHeadUpgradeItem(singleItemProperties(), ToolMaterial.DIAMOND));
    public static final DeferredHolder<Item, Item> DRILL_HEAD_TIER4 = ITEMS.register("drill_head_tier4", () -> new DrillHeadUpgradeItem(singleItemProperties(), ToolMaterial.NETHERITE));

    // Ground Ores
    public static final DeferredHolder<Item, Item> GROUND_COAL_ORE_ROUGH = ITEMS.register("ground_coal_ore_rough", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_DIAMOND_ORE_ROUGH = ITEMS.register("ground_diamond_ore_rough", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_EMERALD_ORE_ROUGH = ITEMS.register("ground_emerald_ore_rough", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_GOLD_ORE_ROUGH = ITEMS.register("ground_gold_ore_rough", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_IRON_ORE_ROUGH = ITEMS.register("ground_iron_ore_rough", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_COPPER_ORE_ROUGH = ITEMS.register("ground_copper_ore_rough", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_LAPIS_ORE_ROUGH = ITEMS.register("ground_lapis_ore_rough", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_NETHER_QUARTZ_ORE_ROUGH = ITEMS.register("ground_nether_quartz_ore_rough", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_REDSTONE_ORE_ROUGH = ITEMS.register("ground_redstone_ore_rough", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_ANCIENT_DEBRIS_ROUGH = ITEMS.register("ground_ancient_debris_rough", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_COAL_ORE_FINE = ITEMS.register("ground_coal_ore_fine", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_DIAMOND_ORE_FINE = ITEMS.register("ground_diamond_ore_fine", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_EMERALD_ORE_FINE = ITEMS.register("ground_emerald_ore_fine", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_GOLD_ORE_FINE = ITEMS.register("ground_gold_ore_fine", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_IRON_ORE_FINE = ITEMS.register("ground_iron_ore_fine", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_COPPER_ORE_FINE = ITEMS.register("ground_copper_ore_fine", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_LAPIS_ORE_FINE = ITEMS.register("ground_lapis_ore_fine", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_NETHER_QUARTZ_ORE_FINE = ITEMS.register("ground_nether_quartz_ore_fine", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_REDSTONE_ORE_FINE = ITEMS.register("ground_redstone_ore_fine", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> GROUND_ANCIENT_DEBRIS_FINE = ITEMS.register("ground_ancient_debris_fine", () -> new Item(defaultItemProperties()));

    // Slurried Ore Buckets
    public static final DeferredHolder<Item, Item> SLURRIED_COAL_ORE_BUCKET =
            ITEMS.register("slurried_coal_ore_bucket", () -> new BucketItem(() -> ModFluids.SLURRIED_COAL_ORE.get(), singleItemProperties()));
    public static final DeferredHolder<Item, Item> SLURRIED_DIAMOND_ORE_BUCKET =
            ITEMS.register("slurried_diamond_ore_bucket", () -> new BucketItem(() -> ModFluids.SLURRIED_DIAMOND_ORE.get(), singleItemProperties()));
    public static final DeferredHolder<Item, Item> SLURRIED_EMERALD_ORE_BUCKET =
            ITEMS.register("slurried_emerald_ore_bucket", () -> new BucketItem(() -> ModFluids.SLURRIED_EMERALD_ORE.get(), singleItemProperties()));
    public static final DeferredHolder<Item, Item> SLURRIED_GOLD_ORE_BUCKET =
            ITEMS.register("slurried_gold_ore_bucket", () -> new BucketItem(() -> ModFluids.SLURRIED_GOLD_ORE.get(), singleItemProperties()));
    public static final DeferredHolder<Item, Item> SLURRIED_IRON_ORE_BUCKET =
            ITEMS.register("slurried_iron_ore_bucket", () -> new BucketItem(() -> ModFluids.SLURRIED_IRON_ORE.get(), singleItemProperties()));
    public static final DeferredHolder<Item, Item> SLURRIED_COPPER_ORE_BUCKET =
            ITEMS.register("slurried_copper_ore_bucket", () -> new BucketItem(() -> ModFluids.SLURRIED_COPPER_ORE.get(), singleItemProperties()));
    public static final DeferredHolder<Item, Item> SLURRIED_LAPIS_ORE_BUCKET =
            ITEMS.register("slurried_lapis_ore_bucket", () -> new BucketItem(() -> ModFluids.SLURRIED_LAPIS_ORE.get(), singleItemProperties()));
    public static final DeferredHolder<Item, Item> SLURRIED_NETHER_QUARTZ_ORE_BUCKET =
            ITEMS.register("slurried_nether_quartz_ore_bucket", () -> new BucketItem(() -> ModFluids.SLURRIED_NETHER_QUARTZ_ORE.get(), singleItemProperties()));
    public static final DeferredHolder<Item, Item> SLURRIED_REDSTONE_ORE_BUCKET =
            ITEMS.register("slurried_redstone_ore_bucket", () -> new BucketItem(() -> ModFluids.SLURRIED_REDSTONE_ORE.get(), singleItemProperties()));
    public static final DeferredHolder<Item, Item> SLURRIED_ANCIENT_DEBRIS_BUCKET =
            ITEMS.register("slurried_ancient_debris_bucket", () -> new BucketItem(() -> ModFluids.SLURRIED_ANCIENT_DEBRIS.get(), singleItemProperties()));

    // Other Items
    public static final DeferredHolder<Item, Item> AMBER = ITEMS.register("amber", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> POWER_SOCKET = ITEMS.register("power_socket", () -> new PowerSocketItem(singleItemProperties()));
    public static final DeferredHolder<Item, Item> BATTERY = ITEMS.register("battery", () -> new BatteryItem(singleItemProperties()));
    public static final DeferredHolder<Item, Item> REDSTONE_WIRE = ITEMS.register("redstone_wire", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> COUPLING = ITEMS.register("coupling", () -> new Item(smallStackItemProperties()));
    public static final DeferredHolder<Item, Item> WRENCH = ITEMS.register("wrench", () -> new Item(singleItemProperties()));
    public static final DeferredHolder<Item, Item> COAGULATED_LATEX = ITEMS.register("coagulated_latex", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> RUBBER = ITEMS.register("rubber", () -> new Item(defaultItemProperties()));
    public static final DeferredHolder<Item, Item> TAPPING_KNIFE = ITEMS.register("tapping_knife", () -> new TappingKnifeItem(singleItemProperties()));
    public static final DeferredHolder<Item, Item> ROCK_DRILL = ITEMS.register("rock_drill", () -> new RockDrillItem(singleItemProperties()));

    private static Item.Properties singleItemProperties() {
        return defaultItemProperties().stacksTo(1);
    }

    private static Item.Properties smallStackItemProperties() {
        return defaultItemProperties().stacksTo(16);
    }

    private static Item.Properties defaultItemProperties() {
        return new Item.Properties().stacksTo(64);
    }
}
