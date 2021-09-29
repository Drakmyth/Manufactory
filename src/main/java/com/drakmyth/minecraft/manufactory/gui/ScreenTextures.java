/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */
package com.drakmyth.minecraft.manufactory.gui;

import java.util.Map;
import static java.util.Map.entry;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.init.ModMenuTypes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;

public final class ScreenTextures {
    private static Map<MenuType<?>, ResourceLocation> textures = Map.of();

    public static void init() {
        textures = Map.ofEntries(
            entry(ModMenuTypes.GRINDER.get(), new ResourceLocation(Reference.MOD_ID, "textures/gui/grinder.png")),
            entry(ModMenuTypes.GRINDER_UPGRADE.get(), new ResourceLocation(Reference.MOD_ID, "textures/gui/grinder_upgrade.png")),
            entry(ModMenuTypes.BALL_MILL.get(), new ResourceLocation(Reference.MOD_ID, "textures/gui/ball_mill.png")),
            entry(ModMenuTypes.BALL_MILL_UPGRADE.get(), new ResourceLocation(Reference.MOD_ID, "textures/gui/ball_mill_upgrade.png")),
            entry(ModMenuTypes.ROCK_DRILL_UPGRADE.get(), new ResourceLocation(Reference.MOD_ID, "textures/gui/rock_drill_upgrade.png"))
        );
    }

    public static ResourceLocation get(MenuType<?> menuType) {
        if (textures.isEmpty()) init();
        return textures.get(menuType);
    }
}
