package com.drakmyth.minecraft.manufactory.init;

import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.drakmyth.minecraft.manufactory.Reference;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(Keys.FLUID_TYPES, Reference.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Reference.MOD_ID);

    // TODO: Consider utilizing tinting for fluid textures instead of color shifting the textures themselves?
    public static RegistryObject<FluidType> SLURRIED_COAL_ORE_TYPE = registerFluidType("slurried_coal_ore", 0xFFFFFFFF);
    public static RegistryObject<FluidType> SLURRIED_DIAMOND_ORE_TYPE = registerFluidType("slurried_diamond_ore", 0xFFFFFFFF);
    public static RegistryObject<FluidType> SLURRIED_EMERALD_ORE_TYPE = registerFluidType("slurried_emerald_ore", 0xFFFFFFFF);
    public static RegistryObject<FluidType> SLURRIED_GOLD_ORE_TYPE = registerFluidType("slurried_gold_ore", 0xFFFFFFFF);
    public static RegistryObject<FluidType> SLURRIED_IRON_ORE_TYPE = registerFluidType("slurried_iron_ore", 0xFFFFFFFF);
    public static RegistryObject<FluidType> SLURRIED_COPPER_ORE_TYPE = registerFluidType("slurried_copper_ore", 0xFFFFFFFF);
    public static RegistryObject<FluidType> SLURRIED_LAPIS_ORE_TYPE = registerFluidType("slurried_lapis_ore", 0xFFFFFFFF);
    public static RegistryObject<FluidType> SLURRIED_NETHER_QUARTZ_ORE_TYPE = registerFluidType("slurried_nether_quartz_ore", 0xFFFFFFFF);
    public static RegistryObject<FluidType> SLURRIED_REDSTONE_ORE_TYPE = registerFluidType("slurried_redstone_ore", 0xFFFFFFFF);
    public static RegistryObject<FluidType> SLURRIED_ANCIENT_DEBRIS_TYPE = registerFluidType("slurried_ancient_debris", 0xFFFFFFFF);

    public static final RegistryObject<FlowingFluid> SLURRIED_COAL_ORE =
            FLUIDS.register("slurried_coal_ore", () -> new ForgeFlowingFluid.Source(slurriedCoalOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_COAL_ORE_FLOWING =
            FLUIDS.register("slurried_coal_ore_flowing", () -> new ForgeFlowingFluid.Flowing(slurriedCoalOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_DIAMOND_ORE =
            FLUIDS.register("slurried_diamond_ore", () -> new ForgeFlowingFluid.Source(slurriedDiamondOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_DIAMOND_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_diamond_ore", () -> new ForgeFlowingFluid.Flowing(slurriedDiamondOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_EMERALD_ORE =
            FLUIDS.register("slurried_emerald_ore", () -> new ForgeFlowingFluid.Source(slurriedEmeraldOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_EMERALD_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_emerald_ore", () -> new ForgeFlowingFluid.Flowing(slurriedEmeraldOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_GOLD_ORE =
            FLUIDS.register("slurried_gold_ore", () -> new ForgeFlowingFluid.Source(slurriedGoldOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_GOLD_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_gold_ore", () -> new ForgeFlowingFluid.Flowing(slurriedGoldOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_IRON_ORE =
            FLUIDS.register("slurried_iron_ore", () -> new ForgeFlowingFluid.Source(slurriedIronOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_IRON_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_iron_ore", () -> new ForgeFlowingFluid.Flowing(slurriedIronOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_COPPER_ORE =
            FLUIDS.register("slurried_copper_ore", () -> new ForgeFlowingFluid.Source(slurriedCopperOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_COPPER_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_copper_ore", () -> new ForgeFlowingFluid.Flowing(slurriedCopperOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_LAPIS_ORE =
            FLUIDS.register("slurried_lapis_ore", () -> new ForgeFlowingFluid.Source(slurriedLapisOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_LAPIS_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_lapis_ore", () -> new ForgeFlowingFluid.Flowing(slurriedLapisOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_NETHER_QUARTZ_ORE =
            FLUIDS.register("slurried_nether_quartz_ore", () -> new ForgeFlowingFluid.Source(slurriedNetherQuartzOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_NETHER_QUARTZ_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_nether_quartz_ore", () -> new ForgeFlowingFluid.Flowing(slurriedNetherQuartzOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_REDSTONE_ORE =
            FLUIDS.register("slurried_redstone_ore", () -> new ForgeFlowingFluid.Source(slurriedRedstoneOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_REDSTONE_ORE_FLOWING =
            FLUIDS.register("flowing_slurried_redstone_ore", () -> new ForgeFlowingFluid.Flowing(slurriedRedstoneOreProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_ANCIENT_DEBRIS =
            FLUIDS.register("slurried_ancient_debris", () -> new ForgeFlowingFluid.Source(slurriedAncientDebrisProperties()));
    public static final RegistryObject<FlowingFluid> SLURRIED_ANCIENT_DEBRIS_FLOWING =
            FLUIDS.register("flowing_slurried_ancient_debris", () -> new ForgeFlowingFluid.Flowing(slurriedAncientDebrisProperties()));


    private static RegistryObject<FluidType> registerFluidType(String name, int tint) {
        return FLUID_TYPES.register(name, () -> new FluidType(FluidType.Properties.create()) {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {
                    @Override
                    public ResourceLocation getStillTexture() {
                        return new ResourceLocation(Reference.MOD_ID, String.format("block/%s_still", name));
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return new ResourceLocation(Reference.MOD_ID, String.format("block/%s_flow", name));
                    }

                    @Nullable
                    @Override
                    public ResourceLocation getOverlayTexture() {
                        // TODO: provide %s_overlay textures
                        return new ResourceLocation(Reference.MOD_ID, String.format("block/%s_still", name));
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
