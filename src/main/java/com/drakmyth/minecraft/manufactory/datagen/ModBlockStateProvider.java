/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.blocks.PowerCableBlock;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    private ExistingFileHelper exFileHelper;

    public ModBlockStateProvider(DataGenerator generator, ExistingFileHelper exFileHelper) {
        super(generator, Reference.MOD_ID, exFileHelper);
        this.exFileHelper = exFileHelper;
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

        ModelFile powerCableCenterModel = generatePowerCableCenterModel();
        ModelFile powerCableSideModel = generatePartialPowerCableSideModel();
        ModelFile powerCableUpModel = generatePartialPowerCableUpModel();
        ModelFile powerCableDownModel = generatePartialPowerCableDownModel();
        generatePowerCableBlockState(powerCableCenterModel, powerCableSideModel, powerCableUpModel, powerCableDownModel);
        itemModels().getBuilder("power_cable").parent(powerCableCenterModel);

        ResourceLocation daylightDetectorModelLoc = new ResourceLocation("minecraft", "block/daylight_detector");
        ModelFile daylightDetectorModel = new ExistingModelFile(daylightDetectorModelLoc, exFileHelper);
        ModelFile solarPanelModel = models().getBuilder("solar_panel").parent(daylightDetectorModel);
        simpleBlock(ModBlocks.SOLAR_PANEL.get(), solarPanelModel);
        itemModels().getBuilder("solar_panel").parent(solarPanelModel);
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
        // latex_surface
        builder.element().from(6, 3, 1).to(10, 4, 5).face(Direction.UP).texture("#latex").end().end();
        // latex_stream
        builder.element().from(7, 5, 0).to(9, 12, 2).allFaces((dir, face) -> face.texture("#latex")).end();
        builder.texture("latex", "minecraft:block/quartz_block_top");
        return builder;
    }

    private ModelFile generatePartialLatexCollectorFullModel() {
        BlockModelBuilder builder = models().getBuilder("latex_collector_full");
        // latex_surface
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
        builder.part().modelFile(fillingModel).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.NORTH).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING);
        builder.part().modelFile(fillingModel).rotationY(90).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.EAST).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING);
        builder.part().modelFile(fillingModel).rotationY(180).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.SOUTH).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING);
        builder.part().modelFile(fillingModel).rotationY(270).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.WEST).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING);
        builder.part().modelFile(fullModel).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.NORTH).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FULL);
        builder.part().modelFile(fullModel).rotationY(90).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.EAST).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FULL);
        builder.part().modelFile(fullModel).rotationY(180).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.SOUTH).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FULL);
        builder.part().modelFile(fullModel).rotationY(270).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.WEST).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FULL);
    }

    private ModelFile generatePowerCableCenterModel() {
        BlockModelBuilder builder = models().getBuilder("power_cable");
        // x_core
        builder.element().from(5, 2, 7).to(11, 4, 9).allFaces((dir, face) -> face.texture("#cable")).end();
        // y_core
        builder.element().from(7, 0, 7).to(9, 6, 9).allFaces((dir, face) -> face.texture("#cable")).end();
        // z_core
        builder.element().from(7, 2, 5).to(9, 4, 11).allFaces((dir, face) -> face.texture("#cable")).end();
        // x_corners
        builder.element().from(7, 1, 6).to(9, 5, 10).allFaces((dir, face) -> face.texture("#cable")).end();
        // y_corners
        builder.element().from(6, 2, 6).to(10, 4, 10).allFaces((dir, face) -> face.texture("#cable")).end();
        // z_corners
        builder.element().from(6, 1, 7).to(10, 5, 9).allFaces((dir, face) -> face.texture("#cable")).end();
        builder.texture("cable", "minecraft:block/coal_block");
        builder.texture("particle", "minecraft:block/coal_block");
        return builder;
    }

    private ModelFile generatePartialPowerCableSideModel() {
        BlockModelBuilder builder = models().getBuilder("power_cable_side");
        // x_core
        builder.element().from(5, 2, 0).to(11, 4, 7).allFaces((dir, face) -> face.texture("#cable")).end();
        // y_core
        builder.element().from(7, 0, 0).to(9, 6, 7).allFaces((dir, face) -> face.texture("#cable")).end();
        // z_corners
        builder.element().from(6, 1, 0).to(10, 5, 7).allFaces((dir, face) -> face.texture("#cable")).end();
        builder.texture("cable", "minecraft:block/coal_block");
        return builder;
    }

    private ModelFile generatePartialPowerCableUpModel() {
        BlockModelBuilder builder = models().getBuilder("power_cable_up");
        // x_core
        builder.element().from(5, 4, 7).to(11, 16, 9).allFaces((dir, face) -> face.texture("#cable")).end();
        // z_core
        builder.element().from(7, 4, 5).to(9, 16, 11).allFaces((dir, face) -> face.texture("#cable")).end();
        // y_corners
        builder.element().from(6, 4, 6).to(10, 16, 10).allFaces((dir, face) -> face.texture("#cable")).end();
        builder.texture("cable", "minecraft:block/coal_block");
        return builder;
    }

    private ModelFile generatePartialPowerCableDownModel() {
        BlockModelBuilder builder = models().getBuilder("power_cable_down");
        // x_core
        builder.element().from(5, 0, 7).to(11, 2, 9).allFaces((dir, face) -> face.texture("#cable")).end();
        // z_core
        builder.element().from(7, 0, 5).to(9, 2, 11).allFaces((dir, face) -> face.texture("#cable")).end();
        // y_corners
        builder.element().from(6, 0, 6).to(10, 2, 10).allFaces((dir, face) -> face.texture("#cable")).end();
        builder.texture("cable", "minecraft:block/coal_block");
        return builder;
    }

    private void generatePowerCableBlockState(ModelFile centerModel, ModelFile sideModel, ModelFile upModel, ModelFile downModel) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(ModBlocks.POWER_CABLE.get());
        builder.part().modelFile(centerModel).addModel();
        builder.part().modelFile(sideModel).addModel().condition(PowerCableBlock.NORTH, true);
        builder.part().modelFile(upModel).addModel().condition(PowerCableBlock.UP, true);
        builder.part().modelFile(downModel).addModel().condition(PowerCableBlock.DOWN, true);
        builder.part().modelFile(sideModel).rotationY(90).addModel().condition(PowerCableBlock.EAST, true);
        builder.part().modelFile(sideModel).rotationY(180).addModel().condition(PowerCableBlock.SOUTH, true);
        builder.part().modelFile(sideModel).rotationY(270).addModel().condition(PowerCableBlock.WEST, true);
    }
}
