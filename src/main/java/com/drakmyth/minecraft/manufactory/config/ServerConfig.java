/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    private static final Logger LOGGER = LogManager.getLogger();

    public final ForgeConfigSpec.DoubleValue AmberChance;
    public final ForgeConfigSpec.IntValue AmberTapSpawnCount;
    public final ForgeConfigSpec.IntValue LatexFillSeconds;
    public final ForgeConfigSpec.IntValue FullLatexSpawnCount;
    public final ForgeConfigSpec.DoubleValue SolarPanelPeakPowerGeneration;

    ServerConfig(ForgeConfigSpec.Builder builder) {
        LOGGER.debug("Initializing SERVER config...");

        builder.push("general");
        AmberChance = builder.comment("Chance to get Amber after a successful latex tap")
        .defineInRange("amberChance", 0.1, 0.0, 1.0);
        AmberTapSpawnCount = builder.comment("Number of Amber player receives if chance succeeds")
        .defineInRange("amberTapSpawnCount", 1, 0, Integer.MAX_VALUE);
        LatexFillSeconds = builder.comment("Time in seconds for Latex Collector to fill")
        .defineInRange("latexFillSeconds", 600, 1, Integer.MAX_VALUE);
        FullLatexSpawnCount = builder.comment("Number of Coagulated Latex player receives from a full Latex Collector")
        .defineInRange("fullLatexSpawnCount", 1, 0, Integer.MAX_VALUE);
        SolarPanelPeakPowerGeneration = builder.comment("Amount of power a Solar Panel will generate per tick at noon when it's not raining")
        .defineInRange("solarPanelPeakPowerGeneration", 0.03125, 0, Double.MAX_VALUE);
        builder.pop();
    }
}
