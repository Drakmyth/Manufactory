package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import java.util.stream.Stream;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
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
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.of(
                ModBlocks.AMBER_BLOCK,
                ModBlocks.METALOSOL_BLOCK,
                ModBlocks.MECHANITE_BLOCK,
                ModBlocks.MECHANITE_PANEL);
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return Stream.of(
                ModBlocks.AMBER_BLOCK.get().asItem().builtInRegistryHolder(),
                ModBlocks.METALOSOL_BLOCK.get().asItem().builtInRegistryHolder(),
                ModBlocks.MECHANITE_BLOCK.get().asItem().builtInRegistryHolder(),
                ModBlocks.MECHANITE_PANEL.get().asItem().builtInRegistryHolder());
    }

    @Override
    public String getName() {
        return "Manufactory simple block models";
    }
}
