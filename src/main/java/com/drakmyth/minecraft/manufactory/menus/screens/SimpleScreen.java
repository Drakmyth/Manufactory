package com.drakmyth.minecraft.manufactory.menus.screens;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;

public class SimpleScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    private static final Logger LOGGER = LogUtils.getLogger();

    public SimpleScreen(T menu, Inventory inv, Component name) {
        super(menu, inv, name);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, ScreenTextures.get(menu.getType()), leftPos, topPos,
                0, 0, imageWidth, imageHeight, 256, 256);
    }
}
