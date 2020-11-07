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
        ModelFile amberBlockModel = cubeAllWithTexture(ModBlocks.AMBER_BLOCK.get(), getBlockTexture("amber_block"));
        simpleBlock(ModBlocks.AMBER_BLOCK.get(), amberBlockModel);
        itemModels().getBuilder("amber_block").parent(amberBlockModel);

        ModelFile latexCollectorEmptyModel = generateLatexCollectorEmptyModel();
        ModelFile latexCollectorFillingModel = generatePartialLatexCollectorFillingModel();
        ModelFile latexCollectorFullModel = generatePartialLatexCollectorFullModel();
        generateLatexCollectorBlockState(latexCollectorEmptyModel, latexCollectorFillingModel, latexCollectorFullModel);

        itemModels().withExistingParent("item/latex_collector", "item/handheld").texture("layer0", getItemTexture("latex_collector"));
        itemModels().withExistingParent("item/amber", "item/handheld").texture("layer0", getItemTexture("amber"));
        itemModels().withExistingParent("item/motor_tier0", "item/handheld").texture("layer0", getItemTexture("motor_tier0"));
        itemModels().withExistingParent("item/motor_tier1", "item/handheld").texture("layer0", getItemTexture("motor_tier1"));
        itemModels().withExistingParent("item/motor_tier2", "item/handheld").texture("layer0", getItemTexture("motor_tier2"));
        itemModels().withExistingParent("item/motor_tier3", "item/handheld").texture("layer0", getItemTexture("motor_tier3"));
        itemModels().withExistingParent("item/grinder_wheel_tier0", "item/handheld").texture("layer0", getItemTexture("grinder_wheel_tier0"));
        itemModels().withExistingParent("item/grinder_wheel_tier1", "item/handheld").texture("layer0", getItemTexture("grinder_wheel_tier1"));
        itemModels().withExistingParent("item/grinder_wheel_tier2", "item/handheld").texture("layer0", getItemTexture("grinder_wheel_tier2"));
        itemModels().withExistingParent("item/grinder_wheel_tier3", "item/handheld").texture("layer0", getItemTexture("grinder_wheel_tier3"));
        itemModels().withExistingParent("item/grinder_wheel_tier4", "item/handheld").texture("layer0", getItemTexture("grinder_wheel_tier4"));
        itemModels().withExistingParent("item/milling_ball_tier0", "item/handheld").texture("layer0", getItemTexture("milling_ball_tier0"));
        itemModels().withExistingParent("item/milling_ball_tier1", "item/handheld").texture("layer0", getItemTexture("milling_ball_tier1"));
        itemModels().withExistingParent("item/milling_ball_tier2", "item/handheld").texture("layer0", getItemTexture("milling_ball_tier2"));
        itemModels().withExistingParent("item/milling_ball_tier3", "item/handheld").texture("layer0", getItemTexture("milling_ball_tier3"));
        itemModels().withExistingParent("item/milling_ball_tier4", "item/handheld").texture("layer0", getItemTexture("milling_ball_tier4"));
        itemModels().withExistingParent("item/power_socket", "item/handheld").texture("layer0", getItemTexture("power_socket"));
        itemModels().withExistingParent("item/battery", "item/handheld").texture("layer0", getItemTexture("battery"));
        itemModels().withExistingParent("item/wrench", "item/handheld").texture("layer0", getItemTexture("wrench"));
        itemModels().withExistingParent("item/coagulated_latex", "item/handheld").texture("layer0", getItemTexture("coagulated_latex"));
        itemModels().withExistingParent("item/ground_coal_ore_rough", "item/handheld").texture("layer0", getItemTexture("ground_coal_ore_rough"));
        itemModels().withExistingParent("item/ground_diamond_ore_rough", "item/handheld").texture("layer0", getItemTexture("ground_diamond_ore_rough"));
        itemModels().withExistingParent("item/ground_emerald_ore_rough", "item/handheld").texture("layer0", getItemTexture("ground_emerald_ore_rough"));
        itemModels().withExistingParent("item/ground_gold_ore_rough", "item/handheld").texture("layer0", getItemTexture("ground_gold_ore_rough"));
        itemModels().withExistingParent("item/ground_iron_ore_rough", "item/handheld").texture("layer0", getItemTexture("ground_iron_ore_rough"));
        itemModels().withExistingParent("item/ground_lapis_ore_rough", "item/handheld").texture("layer0", getItemTexture("ground_lapis_ore_rough"));
        itemModels().withExistingParent("item/ground_nether_quartz_ore_rough", "item/handheld").texture("layer0", getItemTexture("ground_nether_quartz_ore_rough"));
        itemModels().withExistingParent("item/ground_redstone_ore_rough", "item/handheld").texture("layer0", getItemTexture("ground_redstone_ore_rough"));
        itemModels().withExistingParent("item/ground_ancient_debris_rough", "item/handheld").texture("layer0", getItemTexture("ground_ancient_debris_rough"));
        itemModels().withExistingParent("item/ground_coal_ore_fine", "item/handheld").texture("layer0", getItemTexture("ground_coal_ore_fine"));
        itemModels().withExistingParent("item/ground_diamond_ore_fine", "item/handheld").texture("layer0", getItemTexture("ground_diamond_ore_fine"));
        itemModels().withExistingParent("item/ground_emerald_ore_fine", "item/handheld").texture("layer0", getItemTexture("ground_emerald_ore_fine"));
        itemModels().withExistingParent("item/ground_gold_ore_fine", "item/handheld").texture("layer0", getItemTexture("ground_gold_ore_fine"));
        itemModels().withExistingParent("item/ground_iron_ore_fine", "item/handheld").texture("layer0", getItemTexture("ground_iron_ore_fine"));
        itemModels().withExistingParent("item/ground_lapis_ore_fine", "item/handheld").texture("layer0", getItemTexture("ground_lapis_ore_fine"));
        itemModels().withExistingParent("item/ground_nether_quartz_ore_fine", "item/handheld").texture("layer0", getItemTexture("ground_nether_quartz_ore_fine"));
        itemModels().withExistingParent("item/ground_redstone_ore_fine", "item/handheld").texture("layer0", getItemTexture("ground_redstone_ore_fine"));
        itemModels().withExistingParent("item/ground_ancient_debris_fine", "item/handheld").texture("layer0", getItemTexture("ground_ancient_debris_fine"));
        itemModels().withExistingParent("item/rubber", "item/handheld").texture("layer0", getItemTexture("rubber"));
        itemModels().withExistingParent("item/tapping_knife", "item/handheld").texture("layer0", getItemTexture("tapping_knife"));

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

        ModelFile grinderModel = generateGrinderModel();
        generateGrinderBlockState(grinderModel);
        itemModels().getBuilder("grinder").parent(grinderModel);

        ModelFile ballMillModel = generateBallMillModel();
        generateBallMillBlockState(ballMillModel);
        itemModels().getBuilder("ball_mill").parent(ballMillModel);
    }

    private String getItemTexture(String texture) {
        return String.format("%s:item/%s", Reference.MOD_ID, texture);
    }

    private String getBlockTexture(String texture) {
        return String.format("%s:block/%s", Reference.MOD_ID, texture);
    }

    private String getMinecraftBlockTexture(String block) {
        return String.format("minecraft:block/%s", block);
    }

    private ModelFile cubeAllWithTexture(Block block, String texture) {
        return models().cubeAll(block.getRegistryName().getPath(), new ResourceLocation(texture));
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
        builder.texture("collector", getMinecraftBlockTexture("dirt"));
        builder.texture("particle", getMinecraftBlockTexture("dirt"));
        return builder;
    }

    private ModelFile generatePartialLatexCollectorFillingModel() {
        BlockModelBuilder builder = models().getBuilder("latex_collector_filling");
        // latex_surface
        builder.element().from(6, 3, 1).to(10, 4, 5).face(Direction.UP).texture("#latex").end().end();
        // latex_stream
        builder.element().from(7, 5, 0).to(9, 12, 2).allFaces((dir, face) -> face.texture("#latex")).end();
        builder.texture("latex", getMinecraftBlockTexture("quartz_block_top"));
        return builder;
    }

    private ModelFile generatePartialLatexCollectorFullModel() {
        BlockModelBuilder builder = models().getBuilder("latex_collector_full");
        // latex_surface
        builder.element().from(6, 4, 1).to(10, 5, 5).face(Direction.UP).texture("#latex").end().end();
        builder.texture("latex", getMinecraftBlockTexture("quartz_block_top"));
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
        builder.texture("cable", getMinecraftBlockTexture("coal_block"));
        builder.texture("particle", getMinecraftBlockTexture("coal_block"));
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
        builder.texture("cable", getMinecraftBlockTexture("coal_block"));
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
        builder.texture("cable", getMinecraftBlockTexture("coal_block"));
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
        builder.texture("cable", getMinecraftBlockTexture("coal_block"));
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
        BlockModelBuilder builder = models().getBuilder("grinder");
        // north_side
        builder.element().from(2, 0, 0).to(14, 16, 2)
            .face(Direction.NORTH).texture("#side").end()
            .face(Direction.SOUTH).texture("#inside").end()
            .face(Direction.UP).texture("#top").end()
            .face(Direction.DOWN).texture("#bottom").end().end();
        // east_side
        builder.element().from(14, 0, 0).to(16, 16, 16)
            .face(Direction.NORTH).texture("#side").end()
            .face(Direction.EAST).texture("#side").end()
            .face(Direction.SOUTH).texture("#back").end()
            .face(Direction.WEST).texture("#inside").end()
            .face(Direction.UP).texture("#top").end()
            .face(Direction.DOWN).texture("#bottom").end().end();
        // south_side
        builder.element().from(2, 0, 14).to(14, 16, 16)
            .face(Direction.NORTH).texture("#inside").end()
            .face(Direction.SOUTH).texture("#back").end()
            .face(Direction.UP).texture("#top").end()
            .face(Direction.DOWN).texture("#bottom").end().end();
        // west_side
        builder.element().from(0, 0, 0).to(2, 16, 16)
            .face(Direction.NORTH).texture("#side").end()
            .face(Direction.EAST).texture("#inside").end()
            .face(Direction.SOUTH).texture("#back").end()
            .face(Direction.WEST).texture("#side").end()
            .face(Direction.UP).texture("#top").end()
            .face(Direction.DOWN).texture("#bottom").end().end();
        // bottom_side
        builder.element().from(2, 0, 2).to(14, 2, 14)
            .face(Direction.UP).texture("#bottom").end()
            .face(Direction.DOWN).texture("#bottom").end().end();

        builder.texture("front", getBlockTexture("grinder_side"));
        builder.texture("side", getBlockTexture("grinder_side"));
        builder.texture("inside", getBlockTexture("grinder_inside"));
        builder.texture("back", getBlockTexture("grinder_back_socket"));
        builder.texture("top", getBlockTexture("grinder_top"));
        builder.texture("bottom", getMinecraftBlockTexture("furnace_top"));
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
        BlockModelBuilder builder = models().getBuilder("ball_mill");
        builder.element().from(0, 0, 0).to(16, 16, 16)
            .face(Direction.NORTH).texture("#side").end()
            .face(Direction.EAST).texture("#side").end()
            .face(Direction.SOUTH).texture("#back").end()
            .face(Direction.WEST).texture("#side").end()
            .face(Direction.UP).texture("#top").end()
            .face(Direction.DOWN).texture("#bottom").end().end();

        builder.texture("front", getBlockTexture("ball_mill_side"));
        builder.texture("side", getBlockTexture("ball_mill_side"));
        builder.texture("back", getBlockTexture("ball_mill_back_socket"));
        builder.texture("top", getBlockTexture("ball_mill_top"));
        builder.texture("bottom", getMinecraftBlockTexture("furnace_top"));
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
