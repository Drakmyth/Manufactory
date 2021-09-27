/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.network;

import net.minecraft.world.item.ItemStack;

public interface IOpenContainerWithUpgradesListener {
    void onContainerOpened(ItemStack[] upgrades);
}
