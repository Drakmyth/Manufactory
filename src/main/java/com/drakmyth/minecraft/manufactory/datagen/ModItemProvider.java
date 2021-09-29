/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */
package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemProvider extends ItemModelProvider {

    public ModItemProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerTieredItem("motor", 4);
        registerTieredItem("grinder_wheel", 5);
        registerTieredItem("milling_ball", 5);
        registerTieredItem("drill_head", 5);

        registerTool("wrench");
        registerTool("tapping_knife");
        registerTool("rock_drill");

        registerItem("latex_collector");
        registerItem("amber");
        registerItem("power_socket");
        registerItem("battery");
        registerItem("redstone_wire");
        registerItem("coupling");
        registerItem("coagulated_latex");
        registerItem("rubber");

        String[] ores = {
            "coal_ore",
            "diamond_ore",
            "emerald_ore",
            "gold_ore",
            "iron_ore",
            "lapis_ore",
            "nether_quartz_ore",
            "redstone_ore",
            "ancient_debris"
        };

        for (String ore : ores) {
            registerOreItem(ore, "ground", "rough");
            registerOreItem(ore, "ground", "fine");
            registerOreTool(ore, "slurried", "bucket");
        }
    }


    private void registerTieredItem(String name, int tiers) {
        for (int i = 0; i < tiers; i++) {
            registerItem(name + "_tier" + i);
        }
    }

    private void registerItem(String name) {
        registerGenerated("item/" + name);
    }

    private void registerTool(String name) {
        registerHandheld("item/" + name);
    }

    private void registerOreItem(String name, String prefix, String suffix) {
        registerGenerated("item/" + prefix + "_" + name + "_" + suffix);
    }

    private void registerOreTool(String name, String prefix, String suffix) {
        registerHandheld("item/" + prefix + "_" + name + "_" + suffix);
    }

    private void registerGenerated(String name) {
        registerItem(name, "item/generated");
    }

    private void registerHandheld(String name) {
        registerItem(name, "item/handheld");
    }

    private void registerItem(String name, String parent) {
        withExistingParent(name, parent).texture("layer0", modLoc(name));
    }
}
