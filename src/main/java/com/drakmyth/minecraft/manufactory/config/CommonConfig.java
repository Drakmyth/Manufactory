/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.config;

import com.drakmyth.minecraft.manufactory.util.LogLevel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    private static final Logger LOGGER = LogManager.getLogger();

    public final ForgeConfigSpec.EnumValue<LogLevel> LoggingLevel;

    CommonConfig(ForgeConfigSpec.Builder builder) {
        LOGGER.debug("Initializing COMMON config...");

        builder.push("logging");
        LoggingLevel = builder.defineEnum("logLevel", LogLevel.INFO);
        builder.pop();
    }
}
