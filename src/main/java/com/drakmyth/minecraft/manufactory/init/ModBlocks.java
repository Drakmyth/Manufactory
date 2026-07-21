package com.drakmyth.minecraft.manufactory.init;

import java.util.function.Function;
import java.util.function.Supplier;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MOD_ID);

    public static final DeferredHolder<Block, Block> AMBER_BLOCK =
            registerBlock("amber_block", Block::new, () -> defaultDecorProperties(MapColor.COLOR_YELLOW));
    public static final DeferredHolder<Block, Block> METALOSOL_BLOCK =
            registerBlock("metalosol_block", Block::new, () -> defaultDecorProperties(MapColor.COLOR_PURPLE));
    public static final DeferredHolder<Block, Block> MECHANITE_BLOCK =
            registerBlock("mechanite_block", Block::new, () -> defaultDecorProperties(MapColor.COLOR_PURPLE));
    public static final DeferredHolder<Block, Block> MECHANITE_PANEL =
            registerBlock("mechanite_panel", Block::new, () -> defaultDecorProperties(MapColor.COLOR_PURPLE));
    public static final DeferredHolder<Block, Block> MECHANITE_LAMP =
            registerBlock("mechanite_lamp", MechaniteLampBlock::UnlitBlock, ModBlocks::lampProperties);
    public static final DeferredHolder<Block, Block> MECHANITE_LAMP_INVERTED =
            registerBlock("mechanite_lamp_inverted", MechaniteLampBlock::LitBlock, ModBlocks::lampProperties);
    public static final DeferredHolder<Block, Block> POWER_CELL_RECEPTACLE =
            registerBlock("power_cell_receptacle", PowerCellReceptacleBlock::new, () -> defaultDecorProperties(MapColor.COLOR_PURPLE));
    public static final DeferredHolder<Block, Block> GRINDER =
            registerBlock("grinder", GrinderBlock::new, ModBlocks::defaultMachineProperties);
    public static final DeferredHolder<Block, Block> BALL_MILL =
            registerBlock("ball_mill", BallMillBlock::new, ModBlocks::defaultMachineProperties);
    public static final DeferredHolder<Block, Block> LATEX_COLLECTOR =
            registerBlock("latex_collector", LatexCollectorBlock::new,
                    () -> Block.Properties.of().mapColor(MapColor.WOOD).strength(1.0f).sound(SoundType.WOOD), 16);
    public static final DeferredHolder<Block, Block> POWER_CABLE =
            registerBlock("power_cable", PowerCableBlock::new,
                    () -> Block.Properties.of().mapColor(MapColor.METAL).strength(0.7f).sound(SoundType.METAL));
    public static final DeferredHolder<Block, Block> SOLAR_PANEL =
            registerBlock("solar_panel", SolarPanelBlock::new, ModBlocks::defaultMachineProperties);

    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_COAL_ORE =
            BLOCKS.registerBlock("slurried_coal_ore", properties -> new LiquidBlock(ModFluids.SLURRIED_COAL_ORE.get(), properties), ModBlocks::defaultFluidProperties);
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_DIAMOND_ORE =
            BLOCKS.registerBlock("slurried_diamond_ore", properties -> new LiquidBlock(ModFluids.SLURRIED_DIAMOND_ORE.get(), properties), ModBlocks::defaultFluidProperties);
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_EMERALD_ORE =
            BLOCKS.registerBlock("slurried_emerald_ore", properties -> new LiquidBlock(ModFluids.SLURRIED_EMERALD_ORE.get(), properties), ModBlocks::defaultFluidProperties);
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_GOLD_ORE =
            BLOCKS.registerBlock("slurried_gold_ore", properties -> new LiquidBlock(ModFluids.SLURRIED_GOLD_ORE.get(), properties), ModBlocks::defaultFluidProperties);
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_IRON_ORE =
            BLOCKS.registerBlock("slurried_iron_ore", properties -> new LiquidBlock(ModFluids.SLURRIED_IRON_ORE.get(), properties), ModBlocks::defaultFluidProperties);
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_COPPER_ORE =
            BLOCKS.registerBlock("slurried_copper_ore", properties -> new LiquidBlock(ModFluids.SLURRIED_COPPER_ORE.get(), properties), ModBlocks::defaultFluidProperties);
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_LAPIS_ORE =
            BLOCKS.registerBlock("slurried_lapis_ore", properties -> new LiquidBlock(ModFluids.SLURRIED_LAPIS_ORE.get(), properties), ModBlocks::defaultFluidProperties);
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_NETHER_QUARTZ_ORE =
            BLOCKS.registerBlock("slurried_nether_quartz_ore", properties -> new LiquidBlock(ModFluids.SLURRIED_NETHER_QUARTZ_ORE.get(), properties), ModBlocks::defaultFluidProperties);
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_REDSTONE_ORE =
            BLOCKS.registerBlock("slurried_redstone_ore", properties -> new LiquidBlock(ModFluids.SLURRIED_REDSTONE_ORE.get(), properties), ModBlocks::defaultFluidProperties);
    public static final DeferredHolder<Block, LiquidBlock> SLURRIED_ANCIENT_DEBRIS =
            BLOCKS.registerBlock("slurried_ancient_debris", properties -> new LiquidBlock(ModFluids.SLURRIED_ANCIENT_DEBRIS.get(), properties), ModBlocks::defaultFluidProperties);

    private static DeferredHolder<Block, Block> registerBlock(String name,
            Function<BlockBehaviour.Properties, ? extends Block> factory, Supplier<BlockBehaviour.Properties> properties) {
        return registerBlock(name, factory, properties, 64);
    }

    private static DeferredHolder<Block, Block> registerBlock(String name,
            Function<BlockBehaviour.Properties, ? extends Block> factory, Supplier<BlockBehaviour.Properties> properties,
            int stackSize) {
        DeferredHolder<Block, Block> block = BLOCKS.registerBlock(name, factory, properties);
        ITEMS.registerSimpleBlockItem(block, itemProperties -> itemProperties.stacksTo(stackSize));
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

}
