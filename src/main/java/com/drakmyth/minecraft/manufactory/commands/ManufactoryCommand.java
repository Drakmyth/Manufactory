/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.command.CommandSource;

public class ManufactoryCommand {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LOGGER.debug("Registering ManufactoryCommand...");
        dispatcher.register(
            LiteralArgumentBuilder.<CommandSource>literal("manufactory")
            .then(PNetCommand.register())
        );
        LOGGER.debug("ManufactoryCommand registered");
    }
}
