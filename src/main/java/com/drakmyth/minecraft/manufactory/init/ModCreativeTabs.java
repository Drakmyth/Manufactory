package com.drakmyth.minecraft.manufactory.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ModCreativeTabs {

    public static final CreativeModeTab MANUFACTORY = new CreativeModeTab("manufactory") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.TAPPING_KNIFE.get());
        }
    };
}
