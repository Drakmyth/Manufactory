package com.drakmyth.minecraft.manufactory.config;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    private static final Logger LOGGER = LogUtils.getLogger();

    public final ModConfigSpec.DoubleValue AmberChance;
    public final ModConfigSpec.IntValue AmberTapSpawnCount;
    public final ModConfigSpec.IntValue LatexFillSeconds;
    public final ModConfigSpec.IntValue FullLatexSpawnCount;
    public final ModConfigSpec.DoubleValue SolarPanelPeakPowerGeneration;

    ServerConfig(ModConfigSpec.Builder builder) {
        LOGGER.debug(LogMarkers.CONFIG, "Initializing SERVER config...");

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
