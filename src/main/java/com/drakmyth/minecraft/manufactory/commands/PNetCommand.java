/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.commands;

import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

public class PNetCommand {
    static ArgumentBuilder<CommandSource, ?> register()
    {
        return Commands.literal("pnet")
            .requires(cs->cs.hasPermissionLevel(0)) //permission
            .then(Commands.argument("dim", DimensionArgument.getDimension())
                .then(Commands.literal("list")
                    .executes(ctx -> listNetworksInDimension(ctx.getSource(), DimensionArgument.getDimensionArgument(ctx, "dim")))
                )
                .then(Commands.literal("delete")
                    .then(Commands.argument("networkId", PowerNetworkArgument.getPowerNetwork())
                        .suggests(PowerNetworkArgument.SUGGESTIONS)
                        .executes(ctx -> deleteNetworkInDimension(ctx.getSource(), DimensionArgument.getDimensionArgument(ctx, "dim"), PowerNetworkArgument.getPowerNetworkArgument(ctx, "networkId")))
                    )
                )
                .then(Commands.literal("time")
                    .executes(ctx -> printTime(ctx.getSource(), DimensionArgument.getDimensionArgument(ctx, "dim")))
                )
            )
        ;
    }

    private static int listNetworksInDimension(CommandSource cs, ServerWorld dim) throws CommandSyntaxException {
        PowerNetworkManager pnm = PowerNetworkManager.get(dim);
        String[] networkIds = pnm.getNetworkIds();
        for (String networkId : networkIds) {
            int blockCount = pnm.getBlockCount(networkId);
            int sourceCount = pnm.getSourceCount(networkId);
            int sinkCount = pnm.getSinkCount(networkId);
            cs.sendFeedback(new StringTextComponent(String.format("%s Size:%d, In:%d, Out:%d", networkId, blockCount, sourceCount, sinkCount)), false);
        }

        return 1;
    }

    private static int deleteNetworkInDimension(CommandSource cs, ServerWorld dim, String networkId) throws CommandSyntaxException {
        PowerNetworkManager pnm = PowerNetworkManager.get(dim);
        pnm.deleteNetwork(networkId);
        cs.sendFeedback(new StringTextComponent(String.format("Network %s deleted", networkId)), false);

        return 1;
    }

    private static int printTime(CommandSource cs, ServerWorld dim) {
        long daytime = dim.getDayTime();
        float celestialAngle = dim.getCelestialAngleRadians(1.0F);

        cs.sendFeedback(new StringTextComponent(String.format("daytime: %d, angle: %f", daytime, celestialAngle)), false);
        return 1;
    }
}
