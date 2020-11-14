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
        withExistingParent("item/latex_collector", "item/handheld").texture("layer0", modLoc("item/latex_collector"));
        withExistingParent("item/amber", "item/generated").texture("layer0", modLoc("item/amber"));
        withExistingParent("item/motor_tier0", "item/generated").texture("layer0", modLoc("item/motor_tier0"));
        withExistingParent("item/motor_tier1", "item/generated").texture("layer0", modLoc("item/motor_tier1"));
        withExistingParent("item/motor_tier2", "item/generated").texture("layer0", modLoc("item/motor_tier2"));
        withExistingParent("item/motor_tier3", "item/generated").texture("layer0", modLoc("item/motor_tier3"));
        withExistingParent("item/grinder_wheel_tier0", "item/generated").texture("layer0", modLoc("item/grinder_wheel_tier0"));
        withExistingParent("item/grinder_wheel_tier1", "item/generated").texture("layer0", modLoc("item/grinder_wheel_tier1"));
        withExistingParent("item/grinder_wheel_tier2", "item/generated").texture("layer0", modLoc("item/grinder_wheel_tier2"));
        withExistingParent("item/grinder_wheel_tier3", "item/generated").texture("layer0", modLoc("item/grinder_wheel_tier3"));
        withExistingParent("item/grinder_wheel_tier4", "item/generated").texture("layer0", modLoc("item/grinder_wheel_tier4"));
        withExistingParent("item/milling_ball_tier0", "item/generated").texture("layer0", modLoc("item/milling_ball_tier0"));
        withExistingParent("item/milling_ball_tier1", "item/generated").texture("layer0", modLoc("item/milling_ball_tier1"));
        withExistingParent("item/milling_ball_tier2", "item/generated").texture("layer0", modLoc("item/milling_ball_tier2"));
        withExistingParent("item/milling_ball_tier3", "item/generated").texture("layer0", modLoc("item/milling_ball_tier3"));
        withExistingParent("item/milling_ball_tier4", "item/generated").texture("layer0", modLoc("item/milling_ball_tier4"));
        withExistingParent("item/power_socket", "item/generated").texture("layer0", modLoc("item/power_socket"));
        withExistingParent("item/battery", "item/generated").texture("layer0", modLoc("item/battery"));
        withExistingParent("item/redstone_wire", "item/generated").texture("layer0", modLoc("item/redstone_wire"));
        withExistingParent("item/coupling", "item/generated").texture("layer0", modLoc("item/coupling"));
        withExistingParent("item/wrench", "item/handheld").texture("layer0", modLoc("item/wrench"));
        withExistingParent("item/coagulated_latex", "item/generated").texture("layer0", modLoc("item/coagulated_latex"));
        withExistingParent("item/ground_coal_ore_rough", "item/generated").texture("layer0", modLoc("item/ground_coal_ore_rough"));
        withExistingParent("item/ground_diamond_ore_rough", "item/generated").texture("layer0", modLoc("item/ground_diamond_ore_rough"));
        withExistingParent("item/ground_emerald_ore_rough", "item/generated").texture("layer0", modLoc("item/ground_emerald_ore_rough"));
        withExistingParent("item/ground_gold_ore_rough", "item/generated").texture("layer0", modLoc("item/ground_gold_ore_rough"));
        withExistingParent("item/ground_iron_ore_rough", "item/generated").texture("layer0", modLoc("item/ground_iron_ore_rough"));
        withExistingParent("item/ground_lapis_ore_rough", "item/generated").texture("layer0", modLoc("item/ground_lapis_ore_rough"));
        withExistingParent("item/ground_nether_quartz_ore_rough", "item/generated").texture("layer0", modLoc("item/ground_nether_quartz_ore_rough"));
        withExistingParent("item/ground_redstone_ore_rough", "item/generated").texture("layer0", modLoc("item/ground_redstone_ore_rough"));
        withExistingParent("item/ground_ancient_debris_rough", "item/generated").texture("layer0", modLoc("item/ground_ancient_debris_rough"));
        withExistingParent("item/ground_coal_ore_fine", "item/generated").texture("layer0", modLoc("item/ground_coal_ore_fine"));
        withExistingParent("item/ground_diamond_ore_fine", "item/generated").texture("layer0", modLoc("item/ground_diamond_ore_fine"));
        withExistingParent("item/ground_emerald_ore_fine", "item/generated").texture("layer0", modLoc("item/ground_emerald_ore_fine"));
        withExistingParent("item/ground_gold_ore_fine", "item/generated").texture("layer0", modLoc("item/ground_gold_ore_fine"));
        withExistingParent("item/ground_iron_ore_fine", "item/generated").texture("layer0", modLoc("item/ground_iron_ore_fine"));
        withExistingParent("item/ground_lapis_ore_fine", "item/generated").texture("layer0", modLoc("item/ground_lapis_ore_fine"));
        withExistingParent("item/ground_nether_quartz_ore_fine", "item/generated").texture("layer0", modLoc("item/ground_nether_quartz_ore_fine"));
        withExistingParent("item/ground_redstone_ore_fine", "item/generated").texture("layer0", modLoc("item/ground_redstone_ore_fine"));
        withExistingParent("item/ground_ancient_debris_fine", "item/generated").texture("layer0", modLoc("item/ground_ancient_debris_fine"));
        withExistingParent("item/rubber", "item/generated").texture("layer0", modLoc("item/rubber"));
        withExistingParent("item/tapping_knife", "item/handheld").texture("layer0", modLoc("item/tapping_knife"));
        withExistingParent("item/slurried_coal_ore_bucket", "item/handheld").texture("layer0", modLoc("item/slurried_coal_ore_bucket"));
        withExistingParent("item/slurried_diamond_ore_bucket", "item/handheld").texture("layer0", modLoc("item/slurried_diamond_ore_bucket"));
        withExistingParent("item/slurried_emerald_ore_bucket", "item/handheld").texture("layer0", modLoc("item/slurried_emerald_ore_bucket"));
        withExistingParent("item/slurried_gold_ore_bucket", "item/handheld").texture("layer0", modLoc("item/slurried_gold_ore_bucket"));
        withExistingParent("item/slurried_iron_ore_bucket", "item/handheld").texture("layer0", modLoc("item/slurried_iron_ore_bucket"));
        withExistingParent("item/slurried_lapis_ore_bucket", "item/handheld").texture("layer0", modLoc("item/slurried_lapis_ore_bucket"));
        withExistingParent("item/slurried_nether_quartz_ore_bucket", "item/handheld").texture("layer0", modLoc("item/slurried_nether_quartz_ore_bucket"));
        withExistingParent("item/slurried_redstone_ore_bucket", "item/handheld").texture("layer0", modLoc("item/slurried_redstone_ore_bucket"));
        withExistingParent("item/slurried_ancient_debris_bucket", "item/handheld").texture("layer0", modLoc("item/slurried_ancient_debris_bucket"));
    }
}
