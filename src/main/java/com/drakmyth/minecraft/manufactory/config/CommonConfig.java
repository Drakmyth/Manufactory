/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    private static final Logger LOGGER = LogManager.getLogger();

    // public final ForgeConfigSpec.IntValue Field;

    CommonConfig(ForgeConfigSpec.Builder builder) {
        LOGGER.debug("Initializing COMMON config...");

        // builder.push("general");
        // Field = builder.comment("This is a comment for the field")
        // .defineInRange("field", 13, 0, 20);
        // builder.pop();
    }
}
