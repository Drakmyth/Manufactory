/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */
package com.drakmyth.minecraft.manufactory.items.upgrades;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public interface IPowerProvider {
    float consumePower(float requestedPower, ServerLevel level, BlockPos pos);
}
