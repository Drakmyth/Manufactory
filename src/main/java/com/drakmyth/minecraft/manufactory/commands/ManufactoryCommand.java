package com.drakmyth.minecraft.manufactory.commands;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.commands.CommandSourceStack;

public class ManufactoryCommand {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LOGGER.debug(LogMarkers.REGISTRATION, "Registering ManufactoryCommand...");
        dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal("manufactory").then(PNetCommand.register()));
        LOGGER.debug(LogMarkers.REGISTRATION, "ManufactoryCommand registered");
    }
}
