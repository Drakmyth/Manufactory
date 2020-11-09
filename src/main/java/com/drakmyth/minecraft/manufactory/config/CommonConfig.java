/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.config;

import com.drakmyth.minecraft.manufactory.util.LogLevel;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public final ForgeConfigSpec.EnumValue<LogLevel> LoggingLevel;

    CommonConfig(ForgeConfigSpec.Builder builder) {
        builder.push("logging");
        LoggingLevel = builder.defineEnum("logLevel", LogLevel.INFO);
        builder.pop();
    }
}
