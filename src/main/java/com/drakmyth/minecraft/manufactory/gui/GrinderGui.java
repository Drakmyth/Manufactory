/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.gui;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.containers.GrinderContainer;
import com.mojang.blaze3d.matrix.MatrixStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GrinderGui extends ContainerScreen<GrinderContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/grinder.png");
    private static final Logger LOGGER = LogManager.getLogger();

    public GrinderGui(GrinderContainer screenContainer, PlayerInventory inv, ITextComponent name) {
        super(screenContainer, inv, name);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        LOGGER.trace("Rendering Grinder gui...");
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        LOGGER.trace("Grinder gui render complete");
     }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);

        float powerRate = this.container.getPowerRate();
        if (powerRate > 0) {
            LOGGER.trace("Power rate %f greater than 0. Rendering power indicator...", powerRate);
            int yOffset = (int)((1 - powerRate) * 15);
            this.blit(matrixStack, i + 57, j + 54 + yOffset, 176, yOffset, 15, 15 - yOffset);
        }

        float progress = this.container.getProgress();
        if (progress > 0) {
            LOGGER.trace("Progress %f greater than 0. Rendering progress indicator...", progress);
            this.blit(matrixStack, i + 79, j + 34, 176, 15, (int)(progress * 24), 16);
        }
    }
}
