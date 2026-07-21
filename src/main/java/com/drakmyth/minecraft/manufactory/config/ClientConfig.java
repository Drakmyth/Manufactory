package com.drakmyth.minecraft.manufactory.config;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    private static final Logger LOGGER = LogUtils.getLogger();

    // public final ModConfigSpec.IntValue Field;

    ClientConfig(ModConfigSpec.Builder builder) {
        LOGGER.debug(LogMarkers.CONFIG, "Initializing CLIENT config...");

        // builder.push("general");
        // Field = builder.comment("This is a comment for the field")
        // .defineInRange("field", 13, 0, 20);
        // builder.pop();
    }
}
