package com.drakmyth.minecraft.manufactory.init;

import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.drakmyth.minecraft.manufactory.Reference;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid.Properties;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ModFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Reference.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUIDS, Reference.MOD_ID);

    // TODO: Consider utilizing tinting for fluid textures instead of color shifting the textures themselves?
    public static DeferredHolder<FluidType, FluidType> SLURRIED_COAL_ORE_TYPE = registerFluidType("slurried_coal_ore", 0xFFFFFFFF);
    public static DeferredHolder<FluidType, FluidType> SLURRIED_DIAMOND_ORE_TYPE = registerFluidType("slurried_diamond_ore", 0xFFFFFFFF);
    public static DeferredHolder<FluidType, FluidType> SLURRIED_EMERALD_ORE_TYPE = registerFluidType("slurried_emerald_ore", 0xFFFFFFFF);
    public static DeferredHolder<FluidType, FluidType> SLURRIED_GOLD_ORE_TYPE = registerFluidType("slurried_gold_ore", 0xFFFFFFFF);
    public static DeferredHolder<FluidType, FluidType> SLURRIED_IRON_ORE_TYPE = registerFluidType("slurried_iron_ore", 0xFFFFFFFF);
    public static DeferredHolder<FluidType, FluidType> SLURRIED_COPPER_ORE_TYPE = registerFluidType("slurried_copper_ore", 0xFFFFFFFF);
    public static DeferredHolder<FluidType, FluidType> SLURRIED_LAPIS_ORE_TYPE = registerFluidType("slurried_lapis_ore", 0xFFFFFFFF);
    public static DeferredHolder<FluidType, FluidType> SLURRIED_NETHER_QUARTZ_ORE_TYPE = registerFluidType("slurried_nether_quartz_ore", 0xFFFFFFFF);
    public static DeferredHolder<FluidType, FluidType> SLURRIED_REDSTONE_ORE_TYPE = registerFluidType("slurried_redstone_ore", 0xFFFFFFFF);
    public static DeferredHolder<FluidType, FluidType> SLURRIED_ANCIENT_DEBRIS_TYPE = registerFluidType("slurried_ancient_debris", 0xFFFFFFFF);

    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_COAL_ORE =
            FLUIDS.register("slurried_coal_ore", () -> new BaseFlowingFluid.Source(slurriedCoalOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_COAL_ORE_FLOWING =
            FLUIDS.register("slurried_coal_ore_flowing", () -> new BaseFlowingFluid.Flowing(slurriedCoalOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_DIAMOND_ORE =
            FLUIDS.register("slurried_diamond_ore", () -> new BaseFlowingFluid.Source(slurriedDiamondOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_DIAMOND_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_diamond_ore", () -> new BaseFlowingFluid.Flowing(slurriedDiamondOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_EMERALD_ORE =
            FLUIDS.register("slurried_emerald_ore", () -> new BaseFlowingFluid.Source(slurriedEmeraldOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_EMERALD_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_emerald_ore", () -> new BaseFlowingFluid.Flowing(slurriedEmeraldOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_GOLD_ORE =
            FLUIDS.register("slurried_gold_ore", () -> new BaseFlowingFluid.Source(slurriedGoldOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_GOLD_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_gold_ore", () -> new BaseFlowingFluid.Flowing(slurriedGoldOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_IRON_ORE =
            FLUIDS.register("slurried_iron_ore", () -> new BaseFlowingFluid.Source(slurriedIronOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_IRON_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_iron_ore", () -> new BaseFlowingFluid.Flowing(slurriedIronOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_COPPER_ORE =
            FLUIDS.register("slurried_copper_ore", () -> new BaseFlowingFluid.Source(slurriedCopperOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_COPPER_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_copper_ore", () -> new BaseFlowingFluid.Flowing(slurriedCopperOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_LAPIS_ORE =
            FLUIDS.register("slurried_lapis_ore", () -> new BaseFlowingFluid.Source(slurriedLapisOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_LAPIS_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_lapis_ore", () -> new BaseFlowingFluid.Flowing(slurriedLapisOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_NETHER_QUARTZ_ORE =
            FLUIDS.register("slurried_nether_quartz_ore", () -> new BaseFlowingFluid.Source(slurriedNetherQuartzOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_NETHER_QUARTZ_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_nether_quartz_ore", () -> new BaseFlowingFluid.Flowing(slurriedNetherQuartzOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_REDSTONE_ORE =
            FLUIDS.register("slurried_redstone_ore", () -> new BaseFlowingFluid.Source(slurriedRedstoneOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_REDSTONE_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_redstone_ore", () -> new BaseFlowingFluid.Flowing(slurriedRedstoneOreProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_ANCIENT_DEBRIS =
            FLUIDS.register("slurried_ancient_debris", () -> new BaseFlowingFluid.Source(slurriedAncientDebrisProperties()));
    public static final DeferredHolder<Fluid, FlowingFluid> SLURRIED_ANCIENT_DEBRIS_FLOWING =
            FLUIDS.register("flowing_slurried_ancient_debris", () -> new BaseFlowingFluid.Flowing(slurriedAncientDebrisProperties()));


    private static DeferredHolder<FluidType, FluidType> registerFluidType(String name, int tint) {
        return FLUID_TYPES.register(name, () -> new FluidType(FluidType.Properties.create()) {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {
                    @Override
                    public Identifier getStillTexture() {
                        return Identifier.fromNamespaceAndPath(Reference.MOD_ID, String.format("block/%s_still", name));
                    }

                    @Override
                    public Identifier getFlowingTexture() {
                        return Identifier.fromNamespaceAndPath(Reference.MOD_ID, String.format("block/%s_flow", name));
                    }

                    @Nullable
                    @Override
                    public Identifier getOverlayTexture() {
                        // TODO: provide %s_overlay textures
                        return Identifier.fromNamespaceAndPath(Reference.MOD_ID, String.format("block/%s_still", name));
                    }

                    @Override
                    public int getTintColor() {
                        // return 0x3F1080FF;
                        return tint;
                    }
                });
            }
        });
    }

    private static Properties slurriedCoalOreProperties() {
        Supplier<LiquidBlock> blockSupplier = () -> ModBlocks.SLURRIED_COAL_ORE.get();
        Supplier<Item> bucketSupplier = () -> ModItems.SLURRIED_COAL_ORE_BUCKET.get();

        return new Properties(SLURRIED_COAL_ORE_TYPE, SLURRIED_COAL_ORE, SLURRIED_COAL_ORE_FLOWING)
                .block(blockSupplier)
                .bucket(bucketSupplier);
    }

    private static Properties slurriedDiamondOreProperties() {
        return new Properties(SLURRIED_DIAMOND_ORE_TYPE, SLURRIED_DIAMOND_ORE, SLURRIED_DIAMOND_ORE_FLOWING)
                .block(ModBlocks.SLURRIED_DIAMOND_ORE)
                .bucket(ModItems.SLURRIED_DIAMOND_ORE_BUCKET);
    }

    private static Properties slurriedEmeraldOreProperties() {
        return new Properties(SLURRIED_EMERALD_ORE_TYPE, SLURRIED_EMERALD_ORE, SLURRIED_EMERALD_ORE_FLOWING)
                .block(ModBlocks.SLURRIED_EMERALD_ORE)
                .bucket(ModItems.SLURRIED_EMERALD_ORE_BUCKET);
    }

    private static Properties slurriedGoldOreProperties() {
        return new Properties(SLURRIED_GOLD_ORE_TYPE, SLURRIED_GOLD_ORE, SLURRIED_GOLD_ORE_FLOWING)
                .block(ModBlocks.SLURRIED_GOLD_ORE)
                .bucket(ModItems.SLURRIED_GOLD_ORE_BUCKET);
    }

    private static Properties slurriedIronOreProperties() {
        return new Properties(SLURRIED_IRON_ORE_TYPE, SLURRIED_IRON_ORE, SLURRIED_IRON_ORE_FLOWING)
                .block(ModBlocks.SLURRIED_IRON_ORE)
                .bucket(ModItems.SLURRIED_IRON_ORE_BUCKET);
    }

    private static Properties slurriedCopperOreProperties() {
        return new Properties(SLURRIED_COPPER_ORE_TYPE, SLURRIED_COPPER_ORE, SLURRIED_COPPER_ORE_FLOWING)
                .block(ModBlocks.SLURRIED_COPPER_ORE)
                .bucket(ModItems.SLURRIED_COPPER_ORE_BUCKET);
    }

    private static Properties slurriedLapisOreProperties() {
        return new Properties(SLURRIED_LAPIS_ORE_TYPE, SLURRIED_LAPIS_ORE, SLURRIED_LAPIS_ORE_FLOWING)
                .block(ModBlocks.SLURRIED_LAPIS_ORE)
                .bucket(ModItems.SLURRIED_LAPIS_ORE_BUCKET);
    }

    private static Properties slurriedNetherQuartzOreProperties() {
        return new Properties(SLURRIED_NETHER_QUARTZ_ORE_TYPE, SLURRIED_NETHER_QUARTZ_ORE, SLURRIED_NETHER_QUARTZ_ORE_FLOWING)
                .block(ModBlocks.SLURRIED_NETHER_QUARTZ_ORE)
                .bucket(ModItems.SLURRIED_NETHER_QUARTZ_ORE_BUCKET);
    }

    private static Properties slurriedRedstoneOreProperties() {
        return new Properties(SLURRIED_REDSTONE_ORE_TYPE, SLURRIED_REDSTONE_ORE, SLURRIED_REDSTONE_ORE_FLOWING)
                .block(ModBlocks.SLURRIED_REDSTONE_ORE)
                .bucket(ModItems.SLURRIED_REDSTONE_ORE_BUCKET);
    }

    private static Properties slurriedAncientDebrisProperties() {
        return new Properties(SLURRIED_ANCIENT_DEBRIS_TYPE, SLURRIED_ANCIENT_DEBRIS, SLURRIED_ANCIENT_DEBRIS_FLOWING)
                .block(ModBlocks.SLURRIED_ANCIENT_DEBRIS)
                .bucket(ModItems.SLURRIED_ANCIENT_DEBRIS_BUCKET);
    }
}
