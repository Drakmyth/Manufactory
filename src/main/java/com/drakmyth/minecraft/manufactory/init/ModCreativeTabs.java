package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Reference.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MANUFACTORY = CREATIVE_TABS.register(
            "manufactory",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.manufactory"))
                    .icon(() -> new ItemStack(ModItems.TAPPING_KNIFE.get()))
                    .displayItems((parameters, output) -> {
                        ModBlocks.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                        ModItems.ITEMS.getEntries().stream()
                                .filter(item -> item != ModItems.BATTERY)
                                .forEach(item -> output.accept(item.get()));
                    })
                    .build());
}
