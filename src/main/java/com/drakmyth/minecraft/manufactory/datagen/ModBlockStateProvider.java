/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator generator, ExistingFileHelper exFileHelper) {
        super(generator, Reference.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModelFile amberBlockModel = cubeAllWithTexture(ModBlocks.AMBER_BLOCK.get(), new ResourceLocation("minecraft", "block/yellow_terracotta"));
        simpleBlock(ModBlocks.AMBER_BLOCK.get(), amberBlockModel);
        itemModels().getBuilder("amber_block").parent(amberBlockModel);

        ModelFile latexCollectorEmptyModel = generateLatexCollectorEmptyModel();
        ModelFile latexCollectorFillingModel = generatePartialLatexCollectorFillingModel();
        ModelFile latexCollectorFullModel = generatePartialLatexCollectorFullModel();
        generateLatexCollectorBlockState(latexCollectorEmptyModel, latexCollectorFillingModel, latexCollectorFullModel);

        itemModels().withExistingParent("item/latex_collector", "item/handheld").texture("layer0", "manufactory:item/latex_collector");
        itemModels().withExistingParent("item/amber", "item/handheld").texture("layer0", "minecraft:item/baked_potato");
        itemModels().withExistingParent("item/coagulated_latex", "item/handheld").texture("layer0", "minecraft:item/bone_meal");
        itemModels().withExistingParent("item/rubber", "item/handheld").texture("layer0", "minecraft:item/ink_sac");
        itemModels().withExistingParent("item/tapping_knife", "item/handheld").texture("layer0", "manufactory:item/tapping_knife");
    }

    private ModelFile cubeAllWithTexture(Block block, ResourceLocation texture) {
        return models().cubeAll(block.getRegistryName().getPath(), texture);
    }

    private ModelFile generateLatexCollectorEmptyModel() {
        BlockModelBuilder builder = models().getBuilder("latex_collector");

        // bottom
        builder.element().from(6, 2, 1).to(10, 3, 5).allFaces((dir, face) -> face.texture("#collector")).end();
        // north_side
        builder.element().from(5, 3, 0).to(11, 5, 1).allFaces((dir, face) -> face.texture("#collector")).end();
        // south_side
        builder.element().from(5, 3, 5).to(11, 5, 6).allFaces((dir, face) -> face.texture("#collector")).end();
        // west_side
        builder.element().from(5, 3, 1).to(6, 5, 5).allFaces((dir, face) -> face.texture("#collector")).end();
        // east_side
        builder.element().from(10, 3, 1).to(11, 5, 5).allFaces((dir, face) -> face.texture("#collector")).end();
        builder.texture("collector", "minecraft:block/dirt");
        builder.texture("particle", "minecraft:block/dirt");
        return builder;
    }

    private ModelFile generatePartialLatexCollectorFillingModel() {
        BlockModelBuilder builder = models().getBuilder("latex_collector_filling");
        return builder;
    }

    private ModelFile generatePartialLatexCollectorFullModel() {
        BlockModelBuilder builder = models().getBuilder("latex_collector_full");
        builder.element().from(6, 4, 1).to(10, 5, 5).face(Direction.UP).texture("#latex").end().end();
        builder.texture("latex", "minecraft:block/quartz_block_top");
        return builder;
    }

    private void generateLatexCollectorBlockState(ModelFile emptyModel, ModelFile fillingModel, ModelFile fullModel) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(ModBlocks.LATEX_COLLECTOR.get());
        builder.part().modelFile(emptyModel).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.NORTH);
        builder.part().modelFile(emptyModel).rotationY(90).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.EAST);
        builder.part().modelFile(emptyModel).rotationY(180).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.SOUTH);
        builder.part().modelFile(emptyModel).rotationY(270).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.WEST);
        builder.part().modelFile(fullModel).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.NORTH).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FULL);
        builder.part().modelFile(fullModel).rotationY(90).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.EAST).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FULL);
        builder.part().modelFile(fullModel).rotationY(180).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.SOUTH).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FULL);
        builder.part().modelFile(fullModel).rotationY(270).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.WEST).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FULL);
    }
}
