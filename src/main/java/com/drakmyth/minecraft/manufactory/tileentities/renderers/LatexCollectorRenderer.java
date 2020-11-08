/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.tileentities.renderers;

import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock.FillStatus;
import com.drakmyth.minecraft.manufactory.config.ConfigData;
import com.drakmyth.minecraft.manufactory.tileentities.LatexCollectorTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;

public class LatexCollectorRenderer extends TileEntityRenderer<LatexCollectorTileEntity> {

    public LatexCollectorRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(LatexCollectorTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        BlockState state = tileEntity.getBlockState();
        if (state.get(LatexCollectorBlock.FILL_STATUS) == FillStatus.EMPTY) return;

        int totalTime = ConfigData.SERVER.LatexFillSeconds.get() * 20;
        int remainingTime = tileEntity.getTicksRemaining();
        float progress = (totalTime - remainingTime) / (float)totalTime;

        @SuppressWarnings("deprecation")
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/quartz_block_top"));
        IVertexBuilder vertexBuffer = buffer.getBuffer(RenderType.getSolid());

        matrixStack.push();
        matrixStack.translate(0.5, 0.5, 0.5);
        float x1 = -2f/16f; float z1 = -7f/16f;
        float x2 = -2f/16f; float z2 = -3f/16f;
        float x3 =  2f/16f; float z3 = -3f/16f;
        float x4 =  2f/16f; float z4 = -7f/16f;

        Direction facing = state.get(LatexCollectorBlock.HORIZONTAL_FACING);
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            facing = facing.getOpposite();
        }
        float angle = facing.getHorizontalAngle();
        matrixStack.rotate(new Quaternion(0, angle, 0, true));

        float yEmpty = -5f/16f;
        float yFull = -3f/16f;
        float y = MathHelper.lerp(progress, yEmpty, yFull);

        addVertex(vertexBuffer, matrixStack, x1, y, z1, sprite.getMinU(), sprite.getMaxV(), combinedLight, combinedOverlay);
        addVertex(vertexBuffer, matrixStack, x2, y, z2, sprite.getMaxU(), sprite.getMaxV(), combinedLight, combinedOverlay);
        addVertex(vertexBuffer, matrixStack, x3, y, z3, sprite.getMaxU(), sprite.getMinV(), combinedLight, combinedOverlay);
        addVertex(vertexBuffer, matrixStack, x4, y, z4, sprite.getMinU(), sprite.getMinV(), combinedLight, combinedOverlay);

        matrixStack.pop();
    }

    private void addVertex(IVertexBuilder buffer, MatrixStack matrix, float x, float y, float z, float u, float v, int combinedLight, int combinedOverlay) {
        buffer.pos(matrix.getLast().getMatrix(), x, y, z)
            .color(1, 1, 1, 1)
            .tex(u, v)
            .lightmap(combinedLight)
            .normal(0, 1, 0)
            .endVertex();
    }
}
