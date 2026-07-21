package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.blocks.MechaniteLampBlock;
import com.drakmyth.minecraft.manufactory.blocks.PowerCableBlock;
import com.drakmyth.minecraft.manufactory.blocks.PowerCellReceptacleBlock;
import com.drakmyth.minecraft.manufactory.blocks.ReceptacleCell;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.stream.Stream;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.Direction;
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
        createLampModels(blockModels);
        createSolarPanelModel(blockModels);
        createLatexCollectorModels(blockModels);
        createPowerCableModels(blockModels);
        createPowerCellReceptacleModels(blockModels);
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
                ModBlocks.BALL_MILL,
                ModBlocks.MECHANITE_LAMP,
                ModBlocks.MECHANITE_LAMP_INVERTED,
                ModBlocks.SOLAR_PANEL,
                ModBlocks.LATEX_COLLECTOR,
                ModBlocks.POWER_CABLE,
                ModBlocks.POWER_CELL_RECEPTACLE);
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return Stream.of(
                ModBlocks.AMBER_BLOCK.get().asItem().builtInRegistryHolder(),
                ModBlocks.METALOSOL_BLOCK.get().asItem().builtInRegistryHolder(),
                ModBlocks.MECHANITE_BLOCK.get().asItem().builtInRegistryHolder(),
                ModBlocks.MECHANITE_PANEL.get().asItem().builtInRegistryHolder(),
                ModBlocks.GRINDER.get().asItem().builtInRegistryHolder(),
                ModBlocks.BALL_MILL.get().asItem().builtInRegistryHolder(),
                ModBlocks.MECHANITE_LAMP.get().asItem().builtInRegistryHolder(),
                ModBlocks.MECHANITE_LAMP_INVERTED.get().asItem().builtInRegistryHolder(),
                ModBlocks.SOLAR_PANEL.get().asItem().builtInRegistryHolder(),
                ModBlocks.LATEX_COLLECTOR.get().asItem().builtInRegistryHolder(),
                ModBlocks.POWER_CABLE.get().asItem().builtInRegistryHolder(),
                ModBlocks.POWER_CELL_RECEPTACLE.get().asItem().builtInRegistryHolder());
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

    private static void createLampModels(BlockModelGenerators blockModels) {
        Identifier unlitModel = ModelTemplates.CUBE_ALL.create(
                ModBlocks.MECHANITE_LAMP.get(),
                TextureMapping.cube(texture("mechanite_lamp")),
                blockModels.modelOutput);
        Identifier litModel = ModelTemplates.CUBE_ALL.createWithSuffix(
                ModBlocks.MECHANITE_LAMP.get(),
                "_on",
                TextureMapping.cube(texture("mechanite_lamp_on")),
                blockModels.modelOutput);
        registerLamp(blockModels, ModBlocks.MECHANITE_LAMP.get(), litModel, unlitModel, false);
        registerLamp(blockModels, ModBlocks.MECHANITE_LAMP_INVERTED.get(), litModel, unlitModel, true);
    }

    private static void registerLamp(BlockModelGenerators blockModels, Block block,
            Identifier litModel, Identifier unlitModel, boolean defaultLit) {
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(BlockModelGenerators.createBooleanModelDispatch(
                        MechaniteLampBlock.LIT,
                        BlockModelGenerators.plainVariant(litModel),
                        BlockModelGenerators.plainVariant(unlitModel))));
        blockModels.registerSimpleItemModel(block, defaultLit ? litModel : unlitModel);
    }

    private static void createSolarPanelModel(BlockModelGenerators blockModels) {
        Block block = ModBlocks.SOLAR_PANEL.get();
        Identifier daylightDetectorModel = Identifier.withDefaultNamespace("block/daylight_detector");
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(
                block, BlockModelGenerators.plainVariant(daylightDetectorModel)));
        blockModels.registerSimpleItemModel(block, daylightDetectorModel);
    }

    private static void createLatexCollectorModels(BlockModelGenerators blockModels) {
        Identifier empty = customModel(blockModels, "latex_collector", latexCollectorModel(false));
        Identifier filling = customModel(blockModels, "latex_collector_filling", latexCollectorModel(true));
        MultiPartGenerator parts = MultiPartGenerator.multiPart(ModBlocks.LATEX_COLLECTOR.get());
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            var rotation = switch (direction) {
                case EAST -> BlockModelGenerators.Y_ROT_90;
                case SOUTH -> BlockModelGenerators.Y_ROT_180;
                case WEST -> BlockModelGenerators.Y_ROT_270;
                default -> BlockModelGenerators.NOP;
            };
            parts.with(BlockModelGenerators.condition(LatexCollectorBlock.HORIZONTAL_FACING, direction),
                    BlockModelGenerators.plainVariant(empty).with(rotation));
            parts.with(BlockModelGenerators.condition()
                            .term(LatexCollectorBlock.HORIZONTAL_FACING, direction)
                            .term(LatexCollectorBlock.FILL_STATUS, LatexCollectorBlock.FillStatus.FILLING),
                    BlockModelGenerators.plainVariant(filling).with(rotation));
        }
        blockModels.blockStateOutput.accept(parts);
        blockModels.registerSimpleItemModel(ModBlocks.LATEX_COLLECTOR.get(), empty);
    }

    private static Identifier customModel(BlockModelGenerators blockModels, String name, JsonObject model) {
        Identifier id = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "block/" + name);
        blockModels.modelOutput.accept(id, () -> model);
        return id;
    }

    private static JsonObject latexCollectorModel(boolean filling) {
        JsonObject model = new JsonObject();
        JsonObject textures = new JsonObject();
        textures.addProperty(filling ? "latex" : "collector",
                filling ? "minecraft:block/quartz_block_top" : "minecraft:block/dirt");
        if (!filling) textures.addProperty("particle", "minecraft:block/dirt");
        model.add("textures", textures);
        JsonArray elements = new JsonArray();
        if (filling) {
            elements.add(element(7, 3, 0, 9, 12, 2, "#latex", Direction.EAST, Direction.SOUTH,
                    Direction.WEST, Direction.UP));
        } else {
            elements.add(element(6, 2, 1, 10, 3, 5, "#collector", Direction.values()));
            elements.add(element(5, 3, 0, 11, 5, 1, "#collector", Direction.values()));
            elements.add(element(5, 3, 5, 11, 5, 6, "#collector", Direction.values()));
            elements.add(element(5, 3, 1, 6, 5, 5, "#collector", Direction.values()));
            elements.add(element(10, 3, 1, 11, 5, 5, "#collector", Direction.values()));
        }
        model.add("elements", elements);
        return model;
    }

    private static JsonObject element(int fromX, int fromY, int fromZ, int toX, int toY, int toZ,
            String texture, Direction... directions) {
        JsonObject element = new JsonObject();
        JsonArray from = new JsonArray();
        from.add(fromX);
        from.add(fromY);
        from.add(fromZ);
        element.add("from", from);
        JsonArray to = new JsonArray();
        to.add(toX);
        to.add(toY);
        to.add(toZ);
        element.add("to", to);
        JsonObject faces = new JsonObject();
        for (Direction direction : directions) {
            JsonObject face = new JsonObject();
            face.addProperty("texture", texture);
            faces.add(direction.getSerializedName(), face);
        }
        element.add("faces", faces);
        return element;
    }

    private static void createPowerCableModels(BlockModelGenerators blockModels) {
        Identifier center = customModel(blockModels, "power_cable", powerCableCenterModel());
        Identifier side = customModel(blockModels, "power_cable_side", powerCableSideModel());
        Identifier up = customModel(blockModels, "power_cable_up", powerCableUpModel());
        Identifier down = customModel(blockModels, "power_cable_down", powerCableDownModel());
        MultiPartGenerator parts = MultiPartGenerator.multiPart(ModBlocks.POWER_CABLE.get())
                .with(BlockModelGenerators.plainVariant(center))
                .with(BlockModelGenerators.condition(PowerCableBlock.NORTH, true), BlockModelGenerators.plainVariant(side))
                .with(BlockModelGenerators.condition(PowerCableBlock.EAST, true),
                        BlockModelGenerators.plainVariant(side).with(BlockModelGenerators.Y_ROT_90))
                .with(BlockModelGenerators.condition(PowerCableBlock.SOUTH, true),
                        BlockModelGenerators.plainVariant(side).with(BlockModelGenerators.Y_ROT_180))
                .with(BlockModelGenerators.condition(PowerCableBlock.WEST, true),
                        BlockModelGenerators.plainVariant(side).with(BlockModelGenerators.Y_ROT_270))
                .with(BlockModelGenerators.condition(PowerCableBlock.UP, true), BlockModelGenerators.plainVariant(up))
                .with(BlockModelGenerators.condition(PowerCableBlock.DOWN, true), BlockModelGenerators.plainVariant(down));
        blockModels.blockStateOutput.accept(parts);
        blockModels.registerSimpleItemModel(ModBlocks.POWER_CABLE.get(), center);
    }

    private static JsonObject powerCableCenterModel() {
        return model(new String[][]{{"cable", "minecraft:block/coal_block"}, {"particle", "minecraft:block/coal_block"}},
                element(5, 2, 7, 11, 4, 9, "#cable", Direction.values()),
                element(7, 0, 7, 9, 6, 9, "#cable", Direction.values()),
                element(7, 2, 5, 9, 4, 11, "#cable", Direction.values()),
                element(7, 1, 6, 9, 5, 10, "#cable", Direction.values()),
                element(6, 2, 6, 10, 4, 10, "#cable", Direction.values()),
                element(6, 1, 7, 10, 5, 9, "#cable", Direction.values()));
    }

    private static JsonObject powerCableSideModel() {
        return model(new String[][]{{"cable", "minecraft:block/coal_block"}},
                element(5, 2, 0, 11, 4, 7, "#cable", Direction.values()),
                element(7, 0, 0, 9, 6, 7, "#cable", Direction.values()),
                element(6, 1, 0, 10, 5, 7, "#cable", Direction.values()));
    }

    private static JsonObject powerCableUpModel() {
        return model(new String[][]{{"cable", "minecraft:block/coal_block"}},
                element(5, 4, 7, 11, 16, 9, "#cable", Direction.values()),
                element(7, 4, 5, 9, 16, 11, "#cable", Direction.values()),
                element(6, 4, 6, 10, 16, 10, "#cable", Direction.values()));
    }

    private static JsonObject powerCableDownModel() {
        return model(new String[][]{{"cable", "minecraft:block/coal_block"}},
                element(5, 0, 7, 11, 2, 9, "#cable", Direction.values()),
                element(7, 0, 5, 9, 2, 11, "#cable", Direction.values()),
                element(6, 0, 6, 10, 2, 10, "#cable", Direction.values()));
    }

    private static void createPowerCellReceptacleModels(BlockModelGenerators blockModels) {
        Identifier receptacle = customModel(blockModels, "power_cell_receptacle", powerCellReceptacleModel());
        Identifier emptyCell = customModel(blockModels, "power_cell_receptacle_empty", powerCellReceptacleEmptyModel());
        MultiPartGenerator parts = MultiPartGenerator.multiPart(ModBlocks.POWER_CELL_RECEPTACLE.get())
                .with(BlockModelGenerators.plainVariant(receptacle))
                .with(BlockModelGenerators.condition(PowerCellReceptacleBlock.CELL, ReceptacleCell.EMPTY),
                        BlockModelGenerators.plainVariant(emptyCell))
                .with(BlockModelGenerators.condition(PowerCellReceptacleBlock.CELL, ReceptacleCell.FULL),
                        BlockModelGenerators.plainVariant(emptyCell));
        blockModels.blockStateOutput.accept(parts);
        blockModels.registerSimpleItemModel(ModBlocks.POWER_CELL_RECEPTACLE.get(), receptacle);
    }

    private static JsonObject powerCellReceptacleModel() {
        return model(new String[][]{{"mechanite", "manufactory:block/mechanite_block"},
                        {"copper", "minecraft:block/copper_block"}, {"particle", "manufactory:block/mechanite_block"}},
                element(6, 14, 8, 10, 16, 16, "#mechanite", Direction.values()),
                element(6, 0, 8, 10, 2, 16, "#mechanite", Direction.values()),
                element(6, 2, 14, 10, 14, 16, "#mechanite", Direction.values()),
                element(7, 13, 9, 9, 14, 11, "#copper", Direction.values()),
                element(7, 2, 9, 9, 3, 11, "#copper", Direction.values()));
    }

    private static JsonObject powerCellReceptacleEmptyModel() {
        return model(new String[][]{{"mechanite", "manufactory:block/mechanite_block"}, {"glass", "minecraft:block/glass"}},
                element(6, 12, 8, 10, 13, 12, "#mechanite", Direction.values()),
                element(5, 11, 7, 11, 12, 13, "#mechanite", Direction.values()),
                element(6, 5, 8, 10, 11, 12, "#glass", Direction.values()),
                element(5, 4, 7, 11, 5, 13, "#mechanite", Direction.values()),
                element(6, 3, 8, 10, 4, 12, "#mechanite", Direction.values()));
    }

    private static JsonObject model(String[][] textureEntries, JsonObject... modelElements) {
        JsonObject model = new JsonObject();
        JsonObject textures = new JsonObject();
        for (String[] entry : textureEntries) textures.addProperty(entry[0], entry[1]);
        model.add("textures", textures);
        JsonArray elements = new JsonArray();
        for (JsonObject modelElement : modelElements) elements.add(modelElement);
        model.add("elements", elements);
        return model;
    }
}
