/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.commands;

import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class PowerNetworkArgument implements ArgumentType<String> {
    private static final Logger LOGGER = LogManager.getLogger();

    public static SuggestionProvider<CommandSourceStack> SUGGESTIONS = (ctx, sb) -> SharedSuggestionProvider.suggest(PowerNetworkArgument.getPowerNetworkIdsForDimension(ctx), sb);

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();

        while(reader.canRead() && isValidIdCharacter(reader.peek())) {
            reader.skip();
        }

        String arg = reader.getString().substring(i, reader.getCursor());
        LOGGER.trace("Parsed PowerNetworkArgument: %s", arg);
        return arg;
    }

    private boolean isValidIdCharacter(char charIn) {
        return charIn >= '0' && charIn <= '9' || charIn >= 'a' && charIn <= 'f' || charIn == '-';
    }

    public static PowerNetworkArgument getPowerNetwork() {
        return new PowerNetworkArgument();
    }

    public static String getPowerNetworkArgument(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, String.class);
    }

    private static String[] getPowerNetworkIdsForDimension(CommandContext<CommandSourceStack> context) {
        ResourceLocation resourcelocation = context.getArgument("dim", ResourceLocation.class);
        LOGGER.debug("Retrieving power network ids for dimension: %s...", resourcelocation);
        ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, resourcelocation);
        ServerLevel serverworld = context.getSource().getServer().getLevel(key);
        PowerNetworkManager pnm = PowerNetworkManager.get(serverworld);
        String[] powerNetworkIds = pnm.getNetworkIds();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Power Network ids found:");
            for (String pni : powerNetworkIds) {
                LOGGER.debug(pni);
            }
        }
        return powerNetworkIds;
    }
}
