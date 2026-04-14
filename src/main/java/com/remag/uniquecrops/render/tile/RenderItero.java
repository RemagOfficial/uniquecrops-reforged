package com.remag.uniquecrops.render.tile;

import com.mojang.math.Axis;
import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.blocks.tiles.TileItero;
import com.mojang.blaze3d.vertex.*;
import com.mojang.blaze3d.systems.RenderSystem;
import com.remag.uniquecrops.render.CustomRenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class RenderItero implements BlockEntityRenderer<TileItero> {

    private final BlockRenderDispatcher renderDispatcher;
    static final ResourceLocation RES = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "textures/models/sunglow.png");

    public RenderItero(BlockEntityRendererProvider.Context ctx) {

        this.renderDispatcher = ctx.getBlockRenderDispatcher();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void render(TileItero te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        if (!te.showingDemo()) return;

        ms.pushPose();
        ms.translate(0.5f, 0.1f, 0.5f);

        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableBlend();
        VertexConsumer builder = buffer.getBuffer(CustomRenderType.CUSTOM_BEAM.apply(RES, true));

        for  (int i = 0; i < TileItero.PLATES.length; i++) {
            BlockPos platePos = te.getBlockPos().offset(TileItero.PLATES[i]);
            BlockState state = te.getLevel().getBlockState(platePos);
            if (state.getBlock() == Blocks.STONE_PRESSURE_PLATE && state.getValue(PressurePlateBlock.POWERED)) {
                ms.pushPose();
                ms.translate(TileItero.PLATES[i].getX(), 0, TileItero.PLATES[i].getZ());
                this.renderLight(ms, i, buffer, builder);
                ms.popPose();
                break;
            }
        }
        RenderSystem.disableBlend();
        ms.popPose();
    }

    private void renderLight(PoseStack ms, int plateIdx, MultiBufferSource mbs , VertexConsumer vc) {

        final int[] tintcolors = { 0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00 };

        for (int j = 0; j < 4; j++) {
            ms.pushPose();
            switch(j) {
                case 0: ms.translate(0, 0, 0.375F); break;
                case 1: ms.translate(0.375F, 0, 0); break;
                case 2: ms.translate(0, 0, -0.375F); break;
                case 3: ms.translate(-0.375F, 0, 0); break;
            }
            ms.mulPose(Axis.YP.rotationDegrees(j * 90.0F));
            float power = 2.5F;
            float phase = 0.1F;

            float w = 1.0F;
            float h = 40.0F * phase * power;

            Matrix4f mat = ms.last().pose();
            int tint = tintcolors[plateIdx];
            vc.vertex(mat, -0.5F * w, -0.25F, 0.0F).color(tint).uv(0f, 1f).uv2(0x00F000F0).normal(1, 0, 0).endVertex();
            vc.vertex(mat,0.5F * w, -0.25F, 0.0F).color(tint).uv(1f, 1f).uv2(0x00F000F0).normal(1, 0, 0).endVertex();
            vc.vertex(mat,0.5F, 0.75F * h, 0.0F).color(tint).uv(1f, 0f).uv2(0x00F000F0).normal(1, 0, 0).endVertex();
            vc.vertex(mat, -0.5F, 0.75F * h, 0.0F).color(tint).uv(0f, 0f).uv2(0x00F000F0).normal(1, 0, 0).endVertex();
            ms.popPose();
        }
    }
}
