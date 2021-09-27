/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.commands;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;

public class PNetCommand {
    private static final Logger LOGGER = LogManager.getLogger();

    static ArgumentBuilder<CommandSourceStack, ?> register()
    {
        LOGGER.debug(LogMarkers.REGISTRATION, "Registering PNetCommand...");
        ArgumentBuilder<CommandSourceStack, ?> builder = Commands.literal("pnet")
            .requires(cs->cs.hasPermission(0)) //permission
            .then(Commands.argument("dim", DimensionArgument.dimension())
                .then(Commands.literal("list")
                    .executes(ctx -> listNetworksInDimension(ctx.getSource(), DimensionArgument.getDimension(ctx, "dim")))
                )
                .then(Commands.literal("delete")
                    .then(Commands.argument("networkId", PowerNetworkArgument.getPowerNetwork())
                        .suggests(PowerNetworkArgument.SUGGESTIONS)
                        .executes(ctx -> deleteNetworkInDimension(ctx.getSource(), DimensionArgument.getDimension(ctx, "dim"), PowerNetworkArgument.getPowerNetworkArgument(ctx, "networkId")))
                    )
                )
                .then(Commands.literal("time")
                    .executes(ctx -> printTime(ctx.getSource(), DimensionArgument.getDimension(ctx, "dim")))
                )
            )
        ;
        LOGGER.debug(LogMarkers.REGISTRATION, "PNetCommand registered");
        return builder;
    }

    private static int listNetworksInDimension(CommandSourceStack cs, ServerLevel dim) throws CommandSyntaxException {
        PowerNetworkManager pnm = PowerNetworkManager.get(dim);
        String[] networkIds = pnm.getNetworkIds();
        for (String networkId : networkIds) {
            int blockCount = pnm.getBlockCount(networkId);
            int sourceCount = pnm.getSourceCount(networkId);
            int sinkCount = pnm.getSinkCount(networkId);
            cs.sendSuccess(new TextComponent(String.format("%s Size:%d, In:%d, Out:%d", networkId, blockCount, sourceCount, sinkCount)), false);
        }

        return 1;
    }

    private static int deleteNetworkInDimension(CommandSourceStack cs, ServerLevel dim, String networkId) throws CommandSyntaxException {
        PowerNetworkManager pnm = PowerNetworkManager.get(dim);
        pnm.deleteNetwork(networkId);
        cs.sendSuccess(new TextComponent(String.format("Network %s deleted", networkId)), false);

        return 1;
    }

    private static int printTime(CommandSourceStack cs, ServerLevel dim) {
        long daytime = dim.getDayTime();
        float celestialAngle = dim.getSunAngle(1.0F);

        cs.sendSuccess(new TextComponent(String.format("daytime: %d, angle: %f", daytime, celestialAngle)), false);
        return 1;
    }
}
