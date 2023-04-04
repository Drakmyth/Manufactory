package com.drakmyth.minecraft.manufactory.config;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    private static final Logger LOGGER = LogUtils.getLogger();

    // public final ForgeConfigSpec.IntValue Field;

    CommonConfig(ForgeConfigSpec.Builder builder) {
        LOGGER.debug(LogMarkers.CONFIG, "Initializing COMMON config...");

        // builder.push("general");
        // Field = builder.comment("This is a comment for the field")
        // .defineInRange("field", 13, 0, 20);
        // builder.pop();
    }
}
