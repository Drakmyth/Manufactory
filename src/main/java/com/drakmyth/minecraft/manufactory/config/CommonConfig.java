/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public final ForgeConfigSpec.IntValue Test2;

    CommonConfig(ForgeConfigSpec.Builder builder) {
        builder.push("extra");
        Test2 = builder.comment("This is a comment for the Test2 field")
        .defineInRange("test2", 13, 0, 20);
        builder.pop();
    }
}
