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
import com.drakmyth.minecraft.manufactory.blocks.entities.renderers.LatexCollectorRenderer;
import com.drakmyth.minecraft.manufactory.datagen.ModLanguageProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModLootTableProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModRecipeProvider;
import com.drakmyth.minecraft.manufactory.datagen.ModTagsProvider;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class ModEventSubscriber {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void fmlEntityRenderers(final RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntityTypes.LATEX_COLLECTOR.get(), LatexCollectorRenderer::new);
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

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        event.getGenerator().addProvider(true,
                new ModLanguageProvider(event.getGenerator().getPackOutput(), "en_us"));
        event.getGenerator().addProvider(true,
                new ModRecipeProvider.Runner(event.getGenerator().getPackOutput(), event.getLookupProvider()));
        event.createProvider(ModTagsProvider.Blocks::new);
        event.createProvider(ModTagsProvider.Items::new);
        event.createProvider(ModTagsProvider.Fluids::new);
        event.createProvider(ModLootTableProvider::new);
    }
}
