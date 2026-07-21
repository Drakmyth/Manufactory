package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import java.util.stream.Stream;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModSimpleBlockModelProvider extends ModelProvider {
    public ModSimpleBlockModelProvider(PackOutput output) {
        super(output, Reference.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createTrivialCube(ModBlocks.AMBER_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.METALOSOL_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.MECHANITE_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.MECHANITE_PANEL.get());
        createFluidModel(blockModels, ModBlocks.SLURRIED_COAL_ORE.get());
        createFluidModel(blockModels, ModBlocks.SLURRIED_DIAMOND_ORE.get());
        createFluidModel(blockModels, ModBlocks.SLURRIED_EMERALD_ORE.get());
        createFluidModel(blockModels, ModBlocks.SLURRIED_GOLD_ORE.get());
        createFluidModel(blockModels, ModBlocks.SLURRIED_IRON_ORE.get());
        createFluidModel(blockModels, ModBlocks.SLURRIED_COPPER_ORE.get());
        createFluidModel(blockModels, ModBlocks.SLURRIED_LAPIS_ORE.get());
        createFluidModel(blockModels, ModBlocks.SLURRIED_NETHER_QUARTZ_ORE.get());
        createFluidModel(blockModels, ModBlocks.SLURRIED_REDSTONE_ORE.get());
        createFluidModel(blockModels, ModBlocks.SLURRIED_ANCIENT_DEBRIS.get());
        createMachineModel(blockModels, ModBlocks.GRINDER.get(), "grinder");
        createMachineModel(blockModels, ModBlocks.BALL_MILL.get(), "ball_mill");
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.of(
                ModBlocks.AMBER_BLOCK,
                ModBlocks.METALOSOL_BLOCK,
                ModBlocks.MECHANITE_BLOCK,
                ModBlocks.MECHANITE_PANEL,
                ModBlocks.SLURRIED_COAL_ORE,
                ModBlocks.SLURRIED_DIAMOND_ORE,
                ModBlocks.SLURRIED_EMERALD_ORE,
                ModBlocks.SLURRIED_GOLD_ORE,
                ModBlocks.SLURRIED_IRON_ORE,
                ModBlocks.SLURRIED_COPPER_ORE,
                ModBlocks.SLURRIED_LAPIS_ORE,
                ModBlocks.SLURRIED_NETHER_QUARTZ_ORE,
                ModBlocks.SLURRIED_REDSTONE_ORE,
                ModBlocks.SLURRIED_ANCIENT_DEBRIS,
                ModBlocks.GRINDER,
                ModBlocks.BALL_MILL);
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return Stream.of(
                ModBlocks.AMBER_BLOCK.get().asItem().builtInRegistryHolder(),
                ModBlocks.METALOSOL_BLOCK.get().asItem().builtInRegistryHolder(),
                ModBlocks.MECHANITE_BLOCK.get().asItem().builtInRegistryHolder(),
                ModBlocks.MECHANITE_PANEL.get().asItem().builtInRegistryHolder(),
                ModBlocks.GRINDER.get().asItem().builtInRegistryHolder(),
                ModBlocks.BALL_MILL.get().asItem().builtInRegistryHolder());
    }

    @Override
    public String getName() {
        return "Manufactory simple block models";
    }

    private static void createFluidModel(BlockModelGenerators blockModels, Block block) {
        String path = block.builtInRegistryHolder().key().identifier().getPath();
        Identifier model = ModelTemplates.PARTICLE_ONLY.create(
                block,
                TextureMapping.particle(new Material(Identifier.fromNamespaceAndPath(
                        Reference.MOD_ID, "block/" + path + "_still"))),
                blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(
                block, BlockModelGenerators.plainVariant(model)));
    }

    private static void createMachineModel(BlockModelGenerators blockModels, Block block, String name) {
        TextureMapping textures = new TextureMapping()
                .put(TextureSlot.UP, texture(name + "_top"))
                .put(TextureSlot.DOWN, texture(name + "_top"))
                .put(TextureSlot.NORTH, texture(name + "_front"))
                .put(TextureSlot.EAST, texture(name + "_side"))
                .put(TextureSlot.SOUTH, texture(name + "_back_socket"))
                .put(TextureSlot.WEST, texture(name + "_side"))
                .put(TextureSlot.PARTICLE, texture(name + "_top"));
        Identifier model = ModelTemplates.CUBE.create(block, textures, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(
                block, BlockModelGenerators.plainVariant(model))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
        blockModels.registerSimpleItemModel(block, model);
    }

    private static Material texture(String name) {
        return new Material(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "block/" + name));
    }
}
