/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.network;

public interface IPowerRateListener {
    void onPowerRateUpdate(float received, float expected);
}
