package com.drakmyth.minecraft.manufactory.commands;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.power.PowerNetworkManager;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class PowerNetworkArgument implements ArgumentType<String> {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static SuggestionProvider<CommandSourceStack> SUGGESTIONS = (ctx, sb) -> SharedSuggestionProvider.suggest(PowerNetworkArgument.getPowerNetworkIdsForDimension(ctx), sb);

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();

        while (reader.canRead() && isValidIdCharacter(reader.peek())) {
            reader.skip();
        }

        String arg = reader.getString().substring(i, reader.getCursor());
        LOGGER.trace(LogMarkers.POWERNETWORK, "Parsed PowerNetworkArgument: {}", arg);
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
        LOGGER.debug(LogMarkers.POWERNETWORK, "Retrieving power network ids for dimension: {}...", resourcelocation);
        ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, resourcelocation);
        ServerLevel level = context.getSource().getServer().getLevel(key);
        PowerNetworkManager pnm = PowerNetworkManager.get(level);
        String[] powerNetworkIds = pnm.getNetworkIds();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(LogMarkers.POWERNETWORK, "Power Network ids found:");
            for (String pni : powerNetworkIds) {
                LOGGER.debug(LogMarkers.POWERNETWORK, pni);
            }
        }
        return powerNetworkIds;
    }
}
