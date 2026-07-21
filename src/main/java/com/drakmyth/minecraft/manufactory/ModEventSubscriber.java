package com.drakmyth.minecraft.manufactory;

import com.drakmyth.minecraft.manufactory.init.ModMenuTypes;
import com.drakmyth.minecraft.manufactory.menus.BallMillMenu;
import com.drakmyth.minecraft.manufactory.menus.BallMillUpgradeMenu;
import com.drakmyth.minecraft.manufactory.menus.GrinderMenu;
import com.drakmyth.minecraft.manufactory.menus.GrinderUpgradeMenu;
import com.drakmyth.minecraft.manufactory.menus.RockDrillUpgradeMenu;
import com.drakmyth.minecraft.manufactory.menus.screens.PowerProgressScreen;
import com.drakmyth.minecraft.manufactory.menus.screens.ScreenTextures;
import com.drakmyth.minecraft.manufactory.menus.screens.SimpleScreen;
import com.drakmyth.minecraft.manufactory.init.ModBlockEntityTypes;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public final class ModEventSubscriber {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void fmlEntityRenderers(final RegisterRenderers event) {
        // Renderer registration returns when the 26.1 render-state implementation is restored.
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        ScreenTextures.init();
        LOGGER.info(LogMarkers.REGISTRATION, "Registering screens...");
        event.register(ModMenuTypes.GRINDER.get(), PowerProgressScreen<GrinderMenu>::new);
        event.register(ModMenuTypes.GRINDER_UPGRADE.get(), SimpleScreen<GrinderUpgradeMenu>::new);
        event.register(ModMenuTypes.BALL_MILL.get(), PowerProgressScreen<BallMillMenu>::new);
        event.register(ModMenuTypes.BALL_MILL_UPGRADE.get(), SimpleScreen<BallMillUpgradeMenu>::new);
        event.register(ModMenuTypes.ROCK_DRILL_UPGRADE.get(), SimpleScreen<RockDrillUpgradeMenu>::new);
        LOGGER.info(LogMarkers.REGISTRATION, "Screen registration complete");
    }
}
