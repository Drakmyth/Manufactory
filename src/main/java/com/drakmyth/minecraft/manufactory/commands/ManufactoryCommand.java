/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.commands;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.commands.CommandSourceStack;

public class ManufactoryCommand {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LOGGER.debug(LogMarkers.REGISTRATION, "Registering ManufactoryCommand...");
        dispatcher.register(
            LiteralArgumentBuilder.<CommandSourceStack>literal("manufactory")
            .then(PNetCommand.register())
        );
        LOGGER.debug(LogMarkers.REGISTRATION, "ManufactoryCommand registered");
    }
}
