/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.gui;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.containers.BallMillContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class BallMillGui extends AbstractContainerScreen<BallMillContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/ball_mill.png");
    private static final Logger LOGGER = LogManager.getLogger();

    public BallMillGui(BallMillContainer screenContainer, Inventory inv, Component name) {
        super(screenContainer, inv, name);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        LOGGER.trace(LogMarkers.RENDERING, "Rendering Ball Mill gui...");
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        LOGGER.trace(LogMarkers.RENDERING, "Ball Mill gui render complete");
     }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

        float powerRate = this.menu.getPowerRate();
        if (powerRate > 0) {
            LOGGER.trace(LogMarkers.RENDERING, "Power rate %f greater than 0. Rendering power indicator...", powerRate);
            int yOffset = (int)((1 - powerRate) * 15);
            this.blit(matrixStack, i + 57, j + 54 + yOffset, 176, yOffset, 15, 15 - yOffset);
        }

        float progress = this.menu.getProgress();
        if (progress > 0) {
            LOGGER.trace(LogMarkers.RENDERING, "Progress %f greater than 0. Rendering progress indicator...", progress);
            this.blit(matrixStack, i + 79, j + 34, 176, 15, (int)(progress * 24), 16);
        }
    }
}
