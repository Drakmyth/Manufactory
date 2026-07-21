package com.drakmyth.minecraft.manufactory.menus.screens;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.menus.IPowerProgressMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;

public class PowerProgressScreen<T extends AbstractContainerMenu & IPowerProgressMenu> extends SimpleScreen<T> {
    private static final Logger LOGGER = LogUtils.getLogger();

    public PowerProgressScreen(T menu, Inventory inv, Component name) {
        super(menu, inv, name);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        int i = this.leftPos;
        int j = this.topPos;

        float powerRate = this.menu.getPowerRate();
        if (powerRate > 0) {
            LOGGER.trace(LogMarkers.RENDERING, "Power rate {} greater than 0. Rendering power indicator...", powerRate);
            int yOffset = (int)((1 - powerRate) * 15);
            graphics.blit(RenderPipelines.GUI_TEXTURED, ScreenTextures.get(menu.getType()), i + 57, j + 54 + yOffset,
                    176, yOffset, 15, 15 - yOffset, 256, 256);
        }

        float progress = this.menu.getProgress();
        if (progress > 0) {
            LOGGER.trace(LogMarkers.RENDERING, "Progress {} greater than 0. Rendering progress indicator...", progress);
            graphics.blit(RenderPipelines.GUI_TEXTURED, ScreenTextures.get(menu.getType()), i + 79, j + 34,
                    176, 15, (int)(progress * 24), 16, 256, 256);
        }
    }
}
