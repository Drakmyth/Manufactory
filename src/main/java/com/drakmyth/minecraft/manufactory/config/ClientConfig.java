/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.config;

import com.drakmyth.minecraft.manufactory.LogMarkers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    private static final Logger LOGGER = LogManager.getLogger();

    // public final ForgeConfigSpec.IntValue Field;

    ClientConfig(ForgeConfigSpec.Builder builder) {
        LOGGER.debug(LogMarkers.CONFIG, "Initializing CLIENT config...");

        // builder.push("general");
        // Field = builder.comment("This is a comment for the field")
        // .defineInRange("field", 13, 0, 20);
        // builder.pop();
    }
}
