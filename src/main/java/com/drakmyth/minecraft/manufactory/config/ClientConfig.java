/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public final ForgeConfigSpec.ConfigValue<String> Test3;

    ClientConfig(ForgeConfigSpec.Builder builder) {
        builder.push("misc");
        Test3 = builder.comment("This is a comment for the Test3 field")
        .define("test3", "a value");
        builder.pop();
    }
}
