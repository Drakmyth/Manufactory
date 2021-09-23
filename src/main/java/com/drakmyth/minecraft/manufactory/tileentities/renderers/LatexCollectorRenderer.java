/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.tileentities.renderers;

import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock.FillStatus;
import com.drakmyth.minecraft.manufactory.config.ConfigData;
import com.drakmyth.minecraft.manufactory.tileentities.LatexCollectorTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

public class LatexCollectorRenderer implements BlockEntityRenderer<LatexCollectorTileEntity> {
    public static final ResourceLocation LATEX_TEXTURE = new ResourceLocation("minecraft", "block/quartz_block_top");
    private static final Logger LOGGER = LogManager.getLogger();

    public LatexCollectorRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(LatexCollectorTileEntity tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        LOGGER.trace("Beginning render of latex collector at (%d, %d, %d)...", tileEntity.getBlockPos().getX(), tileEntity.getBlockPos().getY(), tileEntity.getBlockPos().getZ());
        BlockState state = tileEntity.getBlockState();
        if (state.getValue(LatexCollectorBlock.FILL_STATUS) == FillStatus.EMPTY) {
            LOGGER.trace("Latex collector is empty. Nothing to render.");
            return;
        }

        int totalTime = ConfigData.SERVER.LatexFillSeconds.get() * 20;
        int remainingTime = tileEntity.getTicksRemaining();
        float progress = (totalTime - remainingTime) / (float)totalTime;

        @SuppressWarnings("deprecation")
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(LATEX_TEXTURE);
        VertexConsumer vertexBuffer = buffer.getBuffer(RenderType.solid());

        LOGGER.trace("Beginning matrix manipulation and vertex construction...");
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.5, 0.5);
        float x1 = -2f/16f; float z1 = -7f/16f; float u1 = sprite.getU0(); float v1 = sprite.getV1();
        float x2 = -2f/16f; float z2 = -3f/16f; float u2 = sprite.getU0(); float v2 = sprite.getV0();
        float x3 =  2f/16f; float z3 = -3f/16f; float u3 = sprite.getU1(); float v3 = sprite.getV0();
        float x4 =  2f/16f; float z4 = -7f/16f; float u4 = sprite.getU1(); float v4 = sprite.getV1();

        Direction facing = state.getValue(LatexCollectorBlock.HORIZONTAL_FACING);
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            facing = facing.getOpposite();
        }
        float angle = facing.toYRot();
        matrixStack.mulPose(new Quaternion(0, angle, 0, true));

        float yEmpty = -5f/16f;
        float yFull = -3f/16f;
        float y = Mth.lerp(progress, yEmpty, yFull);

        addVertex(vertexBuffer, matrixStack, x1, y, z1, u1, v1, combinedLight);
        addVertex(vertexBuffer, matrixStack, x2, y, z2, u2, v2, combinedLight);
        addVertex(vertexBuffer, matrixStack, x3, y, z3, u3, v3, combinedLight);
        addVertex(vertexBuffer, matrixStack, x4, y, z4, u4, v4, combinedLight);

        matrixStack.popPose();
        LOGGER.trace("Matrix manipulation and vertex construction complete");
    }

    private void addVertex(VertexConsumer buffer, PoseStack matrix, float x, float y, float z, float u, float v, int combinedLight) {
        Vector3f normal = Direction.UP.step();
        buffer.vertex(matrix.last().pose(), x, y, z)
            .color(1f, 1f, 1f, 1f)
            .uv(u, v)
            .uv2(combinedLight)
            .normal(normal.x(), normal.y(), normal.z())
            .endVertex();
    }
}
