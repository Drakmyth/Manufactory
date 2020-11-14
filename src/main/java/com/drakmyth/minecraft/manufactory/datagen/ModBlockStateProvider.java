/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.blocks.BallMillBlock;
import com.drakmyth.minecraft.manufactory.blocks.GrinderBlock;
import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.blocks.PowerCableBlock;
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
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
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
        registerCubeBlock(ModBlocks.AMBER_BLOCK.get());

        ModelFile latexCollectorEmptyModel = generateLatexCollectorEmptyModel();
        ModelFile latexCollectorFillingModel = generatePartialLatexCollectorFillingModel();
        generateLatexCollectorBlockState(latexCollectorEmptyModel, latexCollectorFillingModel);

        registerFluidBlock(ModBlocks.SLURRIED_COAL_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_DIAMOND_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_EMERALD_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_GOLD_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_IRON_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_LAPIS_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_NETHER_QUARTZ_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_REDSTONE_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_ANCIENT_DEBRIS.get());

        ModelFile powerCableCenterModel = generatePowerCableCenterModel();
        ModelFile powerCableSideModel = generatePartialPowerCableSideModel();
        ModelFile powerCableUpModel = generatePartialPowerCableUpModel();
        ModelFile powerCableDownModel = generatePartialPowerCableDownModel();
        generatePowerCableBlockState(powerCableCenterModel, powerCableSideModel, powerCableUpModel, powerCableDownModel);
        itemModels().getBuilder("power_cable").parent(powerCableCenterModel);

        ModelFile daylightDetectorModel = new ExistingModelFile(mcLoc("block/daylight_detector"), exFileHelper);
        ModelFile solarPanelModel = models().getBuilder("solar_panel").parent(daylightDetectorModel);
        simpleBlock(ModBlocks.SOLAR_PANEL.get(), solarPanelModel);
        itemModels().getBuilder("solar_panel").parent(solarPanelModel);

        ModelFile grinderModel = generateGrinderModel();
        generateGrinderBlockState(grinderModel);
        itemModels().getBuilder("grinder").parent(grinderModel);

        ModelFile ballMillModel = generateBallMillModel();
        generateBallMillBlockState(ballMillModel);
        itemModels().getBuilder("ball_mill").parent(ballMillModel);
    }

    private void registerCubeBlock(Block block) {
        String name = block.getRegistryName().getPath();
        ModelFile model = cubeAll(block);
        simpleBlock(block, model);
        itemModels().getBuilder(name).parent(model);
    };

    private void registerFluidBlock(Block block) {
        String name = block.getRegistryName().getPath();
        ModelFile model = models().getBuilder(name).texture("particle", modLoc("block/" + name + "_still"));
        simpleBlock(block, model);
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
        builder.texture("collector", mcLoc("block/dirt"));
        builder.texture("particle", mcLoc("block/dirt"));
        return builder;
    }

    private ModelFile generatePartialLatexCollectorFillingModel() {
        BlockModelBuilder builder = models().getBuilder("latex_collector_filling");
        // latex_stream
        builder.element().from(7, 3, 0).to(9, 12, 2)
            .face(Direction.EAST).texture("#latex").end()
            .face(Direction.SOUTH).texture("#latex").end()
            .face(Direction.WEST).texture("#latex").end()
            .face(Direction.UP).texture("#latex").end();
        builder.texture("latex", mcLoc("block/quartz_block_top"));
        return builder;
    }

    private void generateLatexCollectorBlockState(ModelFile emptyModel, ModelFile fillingModel) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(ModBlocks.LATEX_COLLECTOR.get());
        builder.part().modelFile(emptyModel).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.NORTH);
        builder.part().modelFile(emptyModel).rotationY(90).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.EAST);
        builder.part().modelFile(emptyModel).rotationY(180).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.SOUTH);
        builder.part().modelFile(emptyModel).rotationY(270).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.WEST);
        builder.part().modelFile(fillingModel).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.NORTH).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING);
        builder.part().modelFile(fillingModel).rotationY(90).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.EAST).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING);
        builder.part().modelFile(fillingModel).rotationY(180).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.SOUTH).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING);
        builder.part().modelFile(fillingModel).rotationY(270).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.WEST).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING);
    }

    private ModelFile generatePowerCableCenterModel() {
        BlockModelBuilder builder = models().getBuilder("power_cable").parent(new ExistingModelFile(new ResourceLocation("minecraft", "block/block"), exFileHelper));
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
        builder.texture("cable", mcLoc("block/coal_block"));
        builder.texture("particle", mcLoc("block/coal_block"));
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
        builder.texture("cable", mcLoc("block/coal_block"));
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
        builder.texture("cable", mcLoc("block/coal_block"));
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
        builder.texture("cable", mcLoc("block/coal_block"));
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

    private ModelFile generateGrinderModel() {
        BlockModelBuilder builder = models().getBuilder("grinder").parent(new ExistingModelFile(new ResourceLocation("minecraft", "block/cube"), exFileHelper));
        builder.texture("up", modLoc("block/grinder_top"));
        builder.texture("down", modLoc("block/grinder_top"));
        builder.texture("north", modLoc("block/grinder_front"));
        builder.texture("east", modLoc("block/grinder_side"));
        builder.texture("south", modLoc("block/grinder_back_socket"));
        builder.texture("west", modLoc("block/grinder_side"));
        builder.texture("particle", modLoc("block/grinder_top"));
        return builder;
    }

    private void generateGrinderBlockState(ModelFile grinderModel) {
        VariantBlockStateBuilder builder = getVariantBuilder(ModBlocks.GRINDER.get());
        builder.forAllStates(state -> {
            int yRotation = (int)state.get(GrinderBlock.HORIZONTAL_FACING).getOpposite().getHorizontalAngle();
            return ConfiguredModel.builder().modelFile(grinderModel).rotationY(yRotation).build();
        });
    }

    private ModelFile generateBallMillModel() {
        BlockModelBuilder builder = models().getBuilder("ball_mill").parent(new ExistingModelFile(mcLoc("block/block"), exFileHelper));;
        builder.element().from(0, 0, 0).to(16, 16, 16)
            .face(Direction.NORTH).texture("#side").end()
            .face(Direction.EAST).texture("#side").end()
            .face(Direction.SOUTH).texture("#back").end()
            .face(Direction.WEST).texture("#side").end()
            .face(Direction.UP).texture("#top").end()
            .face(Direction.DOWN).texture("#bottom").end().end();

        builder.texture("front", modLoc("block/ball_mill_side"));
        builder.texture("side", modLoc("block/ball_mill_side"));
        builder.texture("back", modLoc("block/ball_mill_back_socket"));
        builder.texture("top", modLoc("block/ball_mill_top"));
        builder.texture("bottom", mcLoc("block/furnace_top"));
        builder.texture("particle", modLoc("block/ball_mill_side"));
        return builder;
    }

    private void generateBallMillBlockState(ModelFile ballMillModel) {
        VariantBlockStateBuilder builder = getVariantBuilder(ModBlocks.BALL_MILL.get());
        builder.forAllStates(state -> {
            int yRotation = (int)state.get(BallMillBlock.HORIZONTAL_FACING).getOpposite().getHorizontalAngle();
            return ConfiguredModel.builder().modelFile(ballMillModel).rotationY(yRotation).build();
        });
    }
}
