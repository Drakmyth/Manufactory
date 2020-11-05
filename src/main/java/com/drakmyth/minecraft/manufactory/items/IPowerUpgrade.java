/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.items;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public interface IPowerUpgrade {
    float consumePower(float requestedPower, ServerWorld world, BlockPos pos);
}
