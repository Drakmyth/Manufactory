/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.gui;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.containers.GrinderUpgradeContainer;
import com.mojang.blaze3d.matrix.MatrixStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GrinderUpgradeGui extends ContainerScreen<GrinderUpgradeContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/grinder_upgrade.png");
    private static final Logger LOGGER = LogManager.getLogger();

    public GrinderUpgradeGui(GrinderUpgradeContainer screenContainer, PlayerInventory inv, ITextComponent name) {
        super(screenContainer, inv, name);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        LOGGER.trace("Rendering Grinder Upgrade gui...");
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        LOGGER.trace("Grinder Upgrade gui render complete");
     }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
    }
}
