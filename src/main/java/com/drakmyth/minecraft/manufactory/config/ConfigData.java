package com.drakmyth.minecraft.manufactory.config;

import org.apache.commons.lang3.tuple.Pair;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigData {
    public static final ServerConfig SERVER;
    public static final ModConfigSpec SERVER_SPEC;

    static {
        final Pair<ServerConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ServerConfig::new);
        SERVER = specPair.getLeft();
        SERVER_SPEC = specPair.getRight();
    }

    public static final ClientConfig CLIENT;
    public static final ModConfigSpec CLIENT_SPEC;

    static {
        final Pair<ClientConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT = specPair.getLeft();
        CLIENT_SPEC = specPair.getRight();
    }

    public static final CommonConfig COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    static {
        final Pair<CommonConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(CommonConfig::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }
}
