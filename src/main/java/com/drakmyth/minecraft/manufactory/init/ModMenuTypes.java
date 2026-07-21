package com.drakmyth.minecraft.manufactory.init;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.menus.BallMillMenu;
import com.drakmyth.minecraft.manufactory.menus.BallMillUpgradeMenu;
import com.drakmyth.minecraft.manufactory.menus.GrinderMenu;
import com.drakmyth.minecraft.manufactory.menus.GrinderUpgradeMenu;
import com.drakmyth.minecraft.manufactory.menus.RockDrillUpgradeMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU_TYPES, Reference.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<GrinderMenu>> GRINDER =
            MENU_TYPES.register("grinder", () -> IMenuTypeExtension.create(GrinderMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<GrinderUpgradeMenu>> GRINDER_UPGRADE =
            MENU_TYPES.register("grinder_upgrade", () -> IMenuTypeExtension.create(GrinderUpgradeMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<BallMillMenu>> BALL_MILL =
            MENU_TYPES.register("ball_mill", () -> IMenuTypeExtension.create(BallMillMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<BallMillUpgradeMenu>> BALL_MILL_UPGRADE =
            MENU_TYPES.register("ball_mill_upgrade", () -> IMenuTypeExtension.create(BallMillUpgradeMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<RockDrillUpgradeMenu>> ROCK_DRILL_UPGRADE =
            MENU_TYPES.register("rock_drill_upgrade", () -> IMenuTypeExtension.create(RockDrillUpgradeMenu::new));
}
