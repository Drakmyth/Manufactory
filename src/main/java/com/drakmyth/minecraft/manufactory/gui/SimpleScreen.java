/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.gui;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;

public class SimpleScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    private static final Logger LOGGER = LogManager.getLogger();

    public SimpleScreen(T menu, Inventory inv, Component name) {
        super(menu, inv, name);
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        LOGGER.trace(LogMarkers.RENDERING, "Rendering {} screen...", getTitle());
        this.renderBackground(pose);
        super.render(pose, mouseX, mouseY, partialTicks);
        this.renderTooltip(pose, mouseX, mouseY);
        LOGGER.trace(LogMarkers.RENDERING, "{} screen render complete", getTitle());
     }

    @Override
    protected void renderBg(PoseStack pose, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ScreenTextures.get(menu.getType()));
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(pose, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
