/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    public final ForgeConfigSpec.BooleanValue Test1;

    ServerConfig(ForgeConfigSpec.Builder builder) {
        builder.push("general");
        Test1 = builder.comment("This is a comment for the Test1 field")
        .define("test1", true);
        builder.pop();
    }
}
