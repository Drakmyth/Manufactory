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
            )
            // .executes(ctx -> {
            //     for (ServerWorld dim : ctx.getSource().getServer().getWorlds()) {
            //         sendTime(ctx.getSource(), dim);
            //     }

            //     MinecraftServer server = ctx.getSource().getServer();
            //     double meanTickTime = mean(server.tickTimeArray) * 1.0E-6D;
            //     double meanTPS = Math.min(1000.0/meanTickTime, 20);
            //     ctx.getSource().sendFeedback(new TranslationTextComponent("commands.forge.tps.summary.all", TIME_FORMATTER.format(meanTickTime), TIME_FORMATTER.format(meanTPS)), false);

            //     return 0;
            // }
        ;
    }

    private static int listNetworksInDimension(CommandSource cs, ServerWorld dim) throws CommandSyntaxException {
        PowerNetworkManager pnm = PowerNetworkManager.get(dim);
        String[] networkIds = pnm.getNetworkIds();
        for (String networkId : networkIds) {
            int blockCount = pnm.getBlockCount(networkId);
            int sourceCount = pnm.getSourceCount(networkId);
            cs.sendFeedback(new StringTextComponent(String.format("%s B:%d, S:%d", networkId, blockCount, sourceCount)), false);
        }

        return 1;
    }

    private static int deleteNetworkInDimension(CommandSource cs, ServerWorld dim, String networkId) throws CommandSyntaxException {
        PowerNetworkManager pnm = PowerNetworkManager.get(dim);
        pnm.deleteNetwork(networkId);
        cs.sendFeedback(new StringTextComponent(String.format("Network %s deleted", networkId)), false);

        return 1;
    }
}
