package com.drakmyth.minecraft.manufactory.init;

import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.blocks.BallMillBlock;
import com.drakmyth.minecraft.manufactory.blocks.GrinderBlock;
import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.blocks.PowerCableBlock;
import com.drakmyth.minecraft.manufactory.blocks.SolarPanelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static final RegistryObject<Block> AMBER_BLOCK =
            registerBlock("amber_block", () -> new Block(defaultDecorProperties(MaterialColor.COLOR_YELLOW)));
    public static final RegistryObject<Block> METALOSOL_BLOCK =
            registerBlock("metalosol_block", () -> new Block(defaultDecorProperties(MaterialColor.COLOR_PURPLE)));
    public static final RegistryObject<Block> MECHANITE_BLOCK =
            registerBlock("mechanite_block", () -> new Block(defaultDecorProperties(MaterialColor.COLOR_PURPLE)));
    public static final RegistryObject<Block> MECHANITE_PANEL =
            registerBlock("mechanite_panel", () -> new Block(defaultDecorProperties(MaterialColor.COLOR_PURPLE)));
    public static final RegistryObject<Block> GRINDER =
            registerBlock("grinder", () -> new GrinderBlock(defaultMachineProperties()));
    public static final RegistryObject<Block> BALL_MILL =
            registerBlock("ball_mill", () -> new BallMillBlock(defaultMachineProperties()));
    public static final RegistryObject<Block> LATEX_COLLECTOR =
            registerBlock("latex_collector", () -> new LatexCollectorBlock(Block.Properties.of(Material.WOOD).strength(1.0f).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> POWER_CABLE =
            registerBlock("power_cable", () -> new PowerCableBlock(Block.Properties.of(Material.DECORATION).strength(0.7f).sound(SoundType.METAL)));
    public static final RegistryObject<Block> SOLAR_PANEL =
            registerBlock("solar_panel", () -> new SolarPanelBlock(defaultMachineProperties()));

    public static final RegistryObject<LiquidBlock> SLURRIED_COAL_ORE =
            BLOCKS.register("slurried_coal_ore", () -> new LiquidBlock(() -> ModFluids.SLURRIED_COAL_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<LiquidBlock> SLURRIED_DIAMOND_ORE =
            BLOCKS.register("slurried_diamond_ore", () -> new LiquidBlock(() -> ModFluids.SLURRIED_DIAMOND_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<LiquidBlock> SLURRIED_EMERALD_ORE =
            BLOCKS.register("slurried_emerald_ore", () -> new LiquidBlock(() -> ModFluids.SLURRIED_EMERALD_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<LiquidBlock> SLURRIED_GOLD_ORE =
            BLOCKS.register("slurried_gold_ore", () -> new LiquidBlock(() -> ModFluids.SLURRIED_GOLD_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<LiquidBlock> SLURRIED_IRON_ORE =
            BLOCKS.register("slurried_iron_ore", () -> new LiquidBlock(() -> ModFluids.SLURRIED_IRON_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<LiquidBlock> SLURRIED_COPPER_ORE =
            BLOCKS.register("slurried_copper_ore", () -> new LiquidBlock(() -> ModFluids.SLURRIED_COPPER_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<LiquidBlock> SLURRIED_LAPIS_ORE =
            BLOCKS.register("slurried_lapis_ore", () -> new LiquidBlock(() -> ModFluids.SLURRIED_LAPIS_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<LiquidBlock> SLURRIED_NETHER_QUARTZ_ORE =
            BLOCKS.register("slurried_nether_quartz_ore", () -> new LiquidBlock(() -> ModFluids.SLURRIED_NETHER_QUARTZ_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<LiquidBlock> SLURRIED_REDSTONE_ORE =
            BLOCKS.register("slurried_redstone_ore", () -> new LiquidBlock(() -> ModFluids.SLURRIED_REDSTONE_ORE.get(), defaultFluidProperties()));
    public static final RegistryObject<LiquidBlock> SLURRIED_ANCIENT_DEBRIS =
            BLOCKS.register("slurried_ancient_debris", () -> new LiquidBlock(() -> ModFluids.SLURRIED_ANCIENT_DEBRIS.get(), defaultFluidProperties()));

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> sup) {
        RegistryObject<Block> block = BLOCKS.register(name, sup);
        ITEMS.register(name, () -> new BlockItem(block.get(), defaultBlockItemProps()));
        return block;
    }

    private static Block.Properties defaultDecorProperties(MaterialColor color) {
        return Block.Properties.of(Material.METAL, color)
                .requiresCorrectToolForDrops()
                .strength(5f, 6f)
                .sound(SoundType.METAL);
    }

    private static Block.Properties defaultMachineProperties() {
        return Block.Properties.of(Material.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.5f);
    }

    private static Block.Properties defaultFluidProperties() {
        return Block.Properties.of(Material.WATER)
                .noCollission()
                .strength(100f)
                .noLootTable();
    }

    public static final Map<RegistryObject<Block>, Item.Properties> BLOCKITEM_PROPS =
            Stream.of(new SimpleEntry<>(LATEX_COLLECTOR, defaultBlockItemProps().stacksTo(16))).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

    public static Item.Properties defaultBlockItemProps() {
        return new Item.Properties().stacksTo(64).tab(ModCreativeTabs.MANUFACTORY);
    }
}
