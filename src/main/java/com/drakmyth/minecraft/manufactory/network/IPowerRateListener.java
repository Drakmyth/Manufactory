package com.drakmyth.minecraft.manufactory.network;

public interface IPowerRateListener {
    void onPowerRateUpdate(float received, float expected);
}
