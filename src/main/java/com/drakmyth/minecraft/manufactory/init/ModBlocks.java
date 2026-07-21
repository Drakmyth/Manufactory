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
import com.drakmyth.minecraft.manufactory.blocks.MechaniteLampBlock;
import com.drakmyth.minecraft.manufactory.blocks.PowerCableBlock;
import com.drakmyth.minecraft.manufactory.blocks.PowerCellReceptacleBlock;
import com.drakmyth.minecraft.manufactory.blocks.SolarPanelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Reference.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Reference.MOD_ID);

    public static final DeferredHolder<Block, Block> AMBER_BLOCK =
            registerBlock("amber_block", () -> new Block(defaultDecorProperties(MapColor.COLOR_YELLOW)));
    public static final DeferredHolder<Block, Block> METALOSOL_BLOCK =
            registerBlock("metalosol_block", () -> new Block(defaultDecorProperties(MapColor.COLOR_PURPLE)));
    public static final DeferredHolder<Block, Block> MECHANITE_BLOCK =
            registerBlock("mechanite_block", () -> new Block(defaultDecorProperties(MapColor.COLOR_PURPLE)));
    public static final DeferredHolder<Block, Block> MECHANITE_PANEL =
            registerBlock("mechanite_panel", () -> new Block(defaultDecorProperties(MapColor.COLOR_PURPLE)));
    public static final DeferredHolder<Block, Block> MECHANITE_LAMP =
            registerBlock("mechanite_lamp", () -> MechaniteLampBlock.UnlitBlock(lampProperties()));
    public static final DeferredHolder<Block, Block> MECHANITE_LAMP_INVERTED =
            registerBlock("mechanite_lamp_inverted", () -> MechaniteLampBlock.LitBlock(lampProperties()));
    public static final DeferredHolder<Block, Block> POWER_CELL_RECEPTACLE =
            registerBlock("power_cell_receptacle", () -> new PowerCellReceptacleBlock(defaultDecorProperties(MapColor.COLOR_PURPLE)));
    public static final DeferredHolder<Block, Block> GRINDER =
            registerBlock("grinder", () -> new GrinderBlock(defaultMachineProperties()));
    public static final DeferredHolder<Block, Block> BALL_MILL =
            registerBlock("ball_mill", () -> new BallMillBlock(defaultMachineProperties()));
    public static final DeferredHolder<Block, Block> LATEX_COLLECTOR =
            registerBlock("latex_collector", () -> new LatexCollectorBlock(Block.Properties.of().mapColor(MapColor.WOOD).strength(1.0f).sound(SoundType.WOOD)));
    public static final DeferredHolder<Block, Block> POWER_CABLE =
            registerBlock("power_cable", () -> new PowerCableBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(0.7f).sound(SoundType.METAL)));
    public static final DeferredHolder<Block, Block> SOLAR_PANEL =
            registerBlock("solar_panel", () -> new SolarPanelBlock(defaultMachineProperties()));

    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_COAL_ORE =
            BLOCKS.register("slurried_coal_ore", () -> new LiquidBlock(ModFluids.SLURRIED_COAL_ORE.get(), defaultFluidProperties()));
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_DIAMOND_ORE =
            BLOCKS.register("slurried_diamond_ore", () -> new LiquidBlock(ModFluids.SLURRIED_DIAMOND_ORE.get(), defaultFluidProperties()));
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_EMERALD_ORE =
            BLOCKS.register("slurried_emerald_ore", () -> new LiquidBlock(ModFluids.SLURRIED_EMERALD_ORE.get(), defaultFluidProperties()));
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_GOLD_ORE =
            BLOCKS.register("slurried_gold_ore", () -> new LiquidBlock(ModFluids.SLURRIED_GOLD_ORE.get(), defaultFluidProperties()));
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_IRON_ORE =
            BLOCKS.register("slurried_iron_ore", () -> new LiquidBlock(ModFluids.SLURRIED_IRON_ORE.get(), defaultFluidProperties()));
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_COPPER_ORE =
            BLOCKS.register("slurried_copper_ore", () -> new LiquidBlock(ModFluids.SLURRIED_COPPER_ORE.get(), defaultFluidProperties()));
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_LAPIS_ORE =
            BLOCKS.register("slurried_lapis_ore", () -> new LiquidBlock(ModFluids.SLURRIED_LAPIS_ORE.get(), defaultFluidProperties()));
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_NETHER_QUARTZ_ORE =
            BLOCKS.register("slurried_nether_quartz_ore", () -> new LiquidBlock(ModFluids.SLURRIED_NETHER_QUARTZ_ORE.get(), defaultFluidProperties()));
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_REDSTONE_ORE =
            BLOCKS.register("slurried_redstone_ore", () -> new LiquidBlock(ModFluids.SLURRIED_REDSTONE_ORE.get(), defaultFluidProperties()));
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_ANCIENT_DEBRIS =
            BLOCKS.register("slurried_ancient_debris", () -> new LiquidBlock(ModFluids.SLURRIED_ANCIENT_DEBRIS.get(), defaultFluidProperties()));

    private static DeferredHolder<Block, Block> registerBlock(String name, Supplier<Block> sup) {
        DeferredHolder<Block, Block> block = BLOCKS.register(name, sup);
        ITEMS.register(name, () -> new BlockItem(block.get(), defaultBlockItemProps()));
        return block;
    }

    private static Block.Properties defaultDecorProperties(MapColor color) {
        return Block.Properties.of().mapColor(color)
                .requiresCorrectToolForDrops()
                .strength(5f, 6f)
                .sound(SoundType.METAL);
    }

    private static Block.Properties defaultMachineProperties() {
        return Block.Properties.of().mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.5f);
    }

    private static Block.Properties defaultFluidProperties() {
        return Block.Properties.of().mapColor(MapColor.WATER)
                .noCollision()
                .strength(100f)
                .noLootTable();
    }

    private static Block.Properties lampProperties() {
        return Block.Properties.of().mapColor(MapColor.NONE)
                .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0)
                .strength(0.3F)
                .sound(SoundType.GLASS);
    }

    public static final Map<DeferredHolder<Block, Block>, Item.Properties> BLOCKITEM_PROPS =
            Stream.of(new SimpleEntry<>(LATEX_COLLECTOR, defaultBlockItemProps().stacksTo(16))).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

    public static Item.Properties defaultBlockItemProps() {
        return new Item.Properties().stacksTo(64);
    }
}
