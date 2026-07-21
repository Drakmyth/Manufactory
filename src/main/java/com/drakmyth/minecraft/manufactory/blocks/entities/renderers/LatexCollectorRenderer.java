package com.drakmyth.minecraft.manufactory.blocks.entities.renderers;

import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock;
import com.drakmyth.minecraft.manufactory.blocks.LatexCollectorBlock.FillStatus;
import com.drakmyth.minecraft.manufactory.blocks.entities.LatexCollectorBlockEntity;
import com.drakmyth.minecraft.manufactory.config.ConfigData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class LatexCollectorRenderer implements BlockEntityRenderer<LatexCollectorBlockEntity, LatexCollectorRenderer.State> {
    private static final Identifier LATEX_TEXTURE = Identifier.withDefaultNamespace("textures/block/quartz_block_top.png");

    public LatexCollectorRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LatexCollectorBlockEntity blockEntity, State state, float partialTicks,
            Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = blockEntity.getBlockState();
        state.visible = blockState.getValue(LatexCollectorBlock.FILL_STATUS) != FillStatus.EMPTY;
        state.facing = blockState.getValue(LatexCollectorBlock.HORIZONTAL_FACING);
        int totalTime = Math.max(1, ConfigData.SERVER.LatexFillSeconds.get() * 20);
        state.progress = Mth.clamp((totalTime - blockEntity.getTicksRemaining() + partialTicks) / totalTime, 0.0F, 1.0F);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.visible) return;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        Direction facing = state.facing;
        if (facing == Direction.NORTH || facing == Direction.SOUTH) facing = facing.getOpposite();
        poseStack.mulPose(Axis.YP.rotationDegrees(facing.toYRot()));
        float y = Mth.lerp(state.progress, -5.0F / 16.0F, -3.0F / 16.0F);
        int light = state.lightCoords;
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.entitySolid(LATEX_TEXTURE),
                (pose, buffer) -> renderSurface(pose, buffer, y, light));
        poseStack.popPose();
    }

    private static void renderSurface(PoseStack.Pose pose, VertexConsumer buffer, float y, int light) {
        addVertex(pose, buffer, -2.0F / 16.0F, y, -7.0F / 16.0F, 0.0F, 1.0F, light);
        addVertex(pose, buffer, -2.0F / 16.0F, y, -3.0F / 16.0F, 0.0F, 0.0F, light);
        addVertex(pose, buffer,  2.0F / 16.0F, y, -3.0F / 16.0F, 1.0F, 0.0F, light);
        addVertex(pose, buffer,  2.0F / 16.0F, y, -7.0F / 16.0F, 1.0F, 1.0F, light);
    }

    private static void addVertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float z,
            float u, float v, int light) {
        buffer.addVertex(pose, x, y, z)
                .setColor(-1)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    public static final class State extends BlockEntityRenderState {
        private boolean visible;
        private float progress;
        private Direction facing = Direction.NORTH;
    }
}
