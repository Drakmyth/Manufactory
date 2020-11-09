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

import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class PowerNetworkArgument implements ArgumentType<String> {

    public static SuggestionProvider<CommandSource> SUGGESTIONS = (ctx, sb) -> ISuggestionProvider.suggest(PowerNetworkArgument.getPowerNetworkIdsForDimension(ctx), sb);

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();

        while(reader.canRead() && isValidIdCharacter(reader.peek())) {
            reader.skip();
        }

        return reader.getString().substring(i, reader.getCursor());
    }

    private boolean isValidIdCharacter(char charIn) {
        return charIn >= '0' && charIn <= '9' || charIn >= 'a' && charIn <= 'f' || charIn == '-';
    }

    public static PowerNetworkArgument getPowerNetwork() {
        return new PowerNetworkArgument();
    }

    public static String getPowerNetworkArgument(CommandContext<CommandSource> context, String name) {
        return context.getArgument(name, String.class);
    }

    private static String[] getPowerNetworkIdsForDimension(CommandContext<CommandSource> context) {
        ResourceLocation resourcelocation = context.getArgument("dim", ResourceLocation.class);
        RegistryKey<World> registrykey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, resourcelocation);
        ServerWorld serverworld = context.getSource().getServer().getWorld(registrykey);
        PowerNetworkManager pnm = PowerNetworkManager.get(serverworld);
        return pnm.getNetworkIds();
    }
}
