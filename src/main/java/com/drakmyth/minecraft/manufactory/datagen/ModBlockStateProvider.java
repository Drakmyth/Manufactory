/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.blocks.PowerCableBlock;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.data.DataGenerator;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
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

        registerFluidBlock(ModBlocks.SLURRIED_COAL_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_DIAMOND_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_EMERALD_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_GOLD_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_IRON_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_LAPIS_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_NETHER_QUARTZ_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_REDSTONE_ORE.get());
        registerFluidBlock(ModBlocks.SLURRIED_ANCIENT_DEBRIS.get());

        registerCubeMachineBlock(ModBlocks.GRINDER.get());
        registerCubeMachineBlock(ModBlocks.BALL_MILL.get());

        ModelFile latexCollectorEmptyModel = generateLatexCollectorEmptyModel();
        ModelFile latexCollectorFillingModel = generatePartialLatexCollectorFillingModel();
        generateLatexCollectorBlockState(latexCollectorEmptyModel, latexCollectorFillingModel);

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

    private void registerCubeMachineBlock(Block block) {
        String name = block.getRegistryName().getPath();
        ModelFile model = generateCubeMachineModel(name);
        generateCubeMachineBlockState(block, model);
        itemModels().getBuilder(name).parent(model);
    }

    private ModelFile generateLatexCollectorEmptyModel() {
        return models().getBuilder("latex_collector")
            // bottom
            .element().from(6, 2, 1).to(10, 3, 5).allFaces((dir, face) -> face.texture("#collector")).end()
            // north_side
            .element().from(5, 3, 0).to(11, 5, 1).allFaces((dir, face) -> face.texture("#collector")).end()
            // south_side
            .element().from(5, 3, 5).to(11, 5, 6).allFaces((dir, face) -> face.texture("#collector")).end()
            // west_side
            .element().from(5, 3, 1).to(6, 5, 5).allFaces((dir, face) -> face.texture("#collector")).end()
            // east_side
            .element().from(10, 3, 1).to(11, 5, 5).allFaces((dir, face) -> face.texture("#collector")).end()
            .texture("collector", mcLoc("block/dirt"))
            .texture("particle", mcLoc("block/dirt"));
    }

    private ModelFile generatePartialLatexCollectorFillingModel() {
        return models().getBuilder("latex_collector_filling")
            // latex_stream
            .element().from(7, 3, 0).to(9, 12, 2)
                .face(Direction.EAST).texture("#latex").end()
                .face(Direction.SOUTH).texture("#latex").end()
                .face(Direction.WEST).texture("#latex").end()
                .face(Direction.UP).texture("#latex").end()
            .end()
            .texture("latex", mcLoc("block/quartz_block_top"));
    }

    private void generateLatexCollectorBlockState(ModelFile emptyModel, ModelFile fillingModel) {
        getMultipartBuilder(ModBlocks.LATEX_COLLECTOR.get())
            .part().modelFile(emptyModel).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.NORTH).end()
            .part().modelFile(emptyModel).rotationY(90).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.EAST).end()
            .part().modelFile(emptyModel).rotationY(180).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.SOUTH).end()
            .part().modelFile(emptyModel).rotationY(270).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.WEST).end()
            .part().modelFile(fillingModel).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.NORTH).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING).end()
            .part().modelFile(fillingModel).rotationY(90).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.EAST).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING).end()
            .part().modelFile(fillingModel).rotationY(180).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.SOUTH).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING).end()
            .part().modelFile(fillingModel).rotationY(270).addModel().condition(LatexCollectorBlock.HORIZONTAL_FACING, Direction.WEST).condition(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING);
    }

    private ModelFile generatePowerCableCenterModel() {
        return models().getBuilder("power_cable").parent(new ExistingModelFile(new ResourceLocation("minecraft", "block/block"), exFileHelper))
            // x_core
            .element().from(5, 2, 7).to(11, 4, 9).allFaces((dir, face) -> face.texture("#cable")).end()
            // y_core
            .element().from(7, 0, 7).to(9, 6, 9).allFaces((dir, face) -> face.texture("#cable")).end()
            // z_core
            .element().from(7, 2, 5).to(9, 4, 11).allFaces((dir, face) -> face.texture("#cable")).end()
            // x_corners
            .element().from(7, 1, 6).to(9, 5, 10).allFaces((dir, face) -> face.texture("#cable")).end()
            // y_corners
            .element().from(6, 2, 6).to(10, 4, 10).allFaces((dir, face) -> face.texture("#cable")).end()
            // z_corners
            .element().from(6, 1, 7).to(10, 5, 9).allFaces((dir, face) -> face.texture("#cable")).end()
            .texture("cable", mcLoc("block/coal_block"))
            .texture("particle", mcLoc("block/coal_block"));
    }

    private ModelFile generatePartialPowerCableSideModel() {
        return models().getBuilder("power_cable_side")
            // x_core
            .element().from(5, 2, 0).to(11, 4, 7).allFaces((dir, face) -> face.texture("#cable")).end()
            // y_core
            .element().from(7, 0, 0).to(9, 6, 7).allFaces((dir, face) -> face.texture("#cable")).end()
            // z_corners
            .element().from(6, 1, 0).to(10, 5, 7).allFaces((dir, face) -> face.texture("#cable")).end()
            .texture("cable", mcLoc("block/coal_block"));
    }

    private ModelFile generatePartialPowerCableUpModel() {
        return models().getBuilder("power_cable_up")
            // x_core
            .element().from(5, 4, 7).to(11, 16, 9).allFaces((dir, face) -> face.texture("#cable")).end()
            // z_core
            .element().from(7, 4, 5).to(9, 16, 11).allFaces((dir, face) -> face.texture("#cable")).end()
            // y_corners
            .element().from(6, 4, 6).to(10, 16, 10).allFaces((dir, face) -> face.texture("#cable")).end()
            .texture("cable", mcLoc("block/coal_block"));
    }

    private ModelFile generatePartialPowerCableDownModel() {
        return models().getBuilder("power_cable_down")
            // x_core
            .element().from(5, 0, 7).to(11, 2, 9).allFaces((dir, face) -> face.texture("#cable")).end()
            // z_core
            .element().from(7, 0, 5).to(9, 2, 11).allFaces((dir, face) -> face.texture("#cable")).end()
            // y_corners
            .element().from(6, 0, 6).to(10, 2, 10).allFaces((dir, face) -> face.texture("#cable")).end()
            .texture("cable", mcLoc("block/coal_block"));
    }

    private void generatePowerCableBlockState(ModelFile centerModel, ModelFile sideModel, ModelFile upModel, ModelFile downModel) {
        getMultipartBuilder(ModBlocks.POWER_CABLE.get())
            .part().modelFile(centerModel).addModel().end()
            .part().modelFile(sideModel).addModel().condition(PowerCableBlock.NORTH, true).end()
            .part().modelFile(upModel).addModel().condition(PowerCableBlock.UP, true).end()
            .part().modelFile(downModel).addModel().condition(PowerCableBlock.DOWN, true).end()
            .part().modelFile(sideModel).rotationY(90).addModel().condition(PowerCableBlock.EAST, true).end()
            .part().modelFile(sideModel).rotationY(180).addModel().condition(PowerCableBlock.SOUTH, true).end()
            .part().modelFile(sideModel).rotationY(270).addModel().condition(PowerCableBlock.WEST, true).end();
    }

    private ModelFile generateCubeMachineModel(String name) {
        return models().getBuilder(name)
        .parent(new ExistingModelFile(new ResourceLocation("minecraft", "block/cube"), exFileHelper))
        .texture("up", modLoc("block/" + name + "_top"))
        .texture("down", modLoc("block/" + name + "_top"))
        .texture("north", modLoc("block/" + name + "_front"))
        .texture("east", modLoc("block/" + name + "_side"))
        .texture("south", modLoc("block/" + name + "_back_socket"))
        .texture("west", modLoc("block/" + name + "_side"))
        .texture("particle", modLoc("block/" + name + "_top"));
    }
    
    private void generateCubeMachineBlockState(Block block, ModelFile model) {
        getVariantBuilder(block).forAllStates(state -> {
            int yRotation = (int)state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite().toYRot();
            return ConfiguredModel.builder().modelFile(model).rotationY(yRotation).build();
        });
    }
}
