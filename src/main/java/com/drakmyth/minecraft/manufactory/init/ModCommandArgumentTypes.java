package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.commands.PowerNetworkArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ModCommandArgumentTypes {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPES, Reference.MOD_ID);

    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<PowerNetworkArgument>> POWER_NETWORK_ARGUMENT = COMMAND_ARGUMENT_TYPES.register("power_network",
            () -> ArgumentTypeInfos.registerByClass(PowerNetworkArgument.class, SingletonArgumentInfo.contextFree(PowerNetworkArgument::getPowerNetwork)));
}
