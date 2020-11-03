/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;

public class ManufactoryCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
            LiteralArgumentBuilder.<CommandSource>literal("manufactory")
            .then(PNetCommand.register())
        );
    }
}
