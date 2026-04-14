package com.remag.uniquecrops.render.tile;

import com.mojang.math.Axis;
import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.blocks.tiles.TileDyeius;
import com.remag.uniquecrops.render.CustomRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.joml.Matrix4f;
import java.util.stream.IntStream;

public class RenderDyeius implements BlockEntityRenderer<TileDyeius> {

    private final BlockRenderDispatcher renderDispatcher;
    private final int[] textureSkipper = IntStream.of(1, 2, 2, 3, 3, 4, 4, 5).toArray();

    public RenderDyeius(BlockEntityRendererProvider.Context ctx) {

        this.renderDispatcher = ctx.getBlockRenderDispatcher();
    }

    @Override
    public void render(TileDyeius te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        int age = te.getBlockState().getValue(BaseCropsBlock.AGE);
        ResourceLocation res = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "textures/block/dyeplant" + textureSkipper[age] + ".png");

        int calculatedFrame = 1 + (int)((Minecraft.getInstance().level.getDayTime() % 24000L) / 1500) % 16;
        // I should NOT need a custom renderer to manually draw a block model, especially not one
        // originally designed for a beacon-like beam, but I couldn't get it working the right way
        // so I just copied the Succo code.
        Level level = te.getLevel();
        BlockPos pos = te.getBlockPos();
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        int packlight = LightTexture.pack(bLight, sLight);
        final VertexConsumer buff = buffer.getBuffer(CustomRenderType.CUSTOM_BEAM.apply(res, false));

        ms.pushPose();

        float r = 1.0F, g = r, b = g;
        Matrix4f mat = ms.last().pose();

        ms.mulPose(Axis.YP.rotationDegrees(45.0F));
        ms.translate(0f, 0.45f, 0.75f);
        ms.mulPose(Axis.ZP.rotationDegrees(90.0F));
        this.quad(buff, mat, 0.5F, 0.5F, 0f, 1f, packlight, age, calculatedFrame);
        ms.mulPose(Axis.XP.rotationDegrees(180.0F));
        this.quad(buff, mat, 0.5F, 0.5F, 0f, 1f, packlight, age, calculatedFrame);

        ms.popPose();
    }

    private void quad(VertexConsumer buff, Matrix4f mat, float x, float y, float z, float a, int p, int age, int frame) {

        float vTop = 0f, vBottom = 1f;
        if (age == 7) {
            vTop = (float)(frame) / 17f + 0.002f;    // There are 17 frames in the image. The first is blank.
            vBottom = vTop + 1f / 17f - 0.004f;
        }
        buff.vertex(mat, -x, -y, z).color(1.0F, 1.0F, 1.0F, a).uv(0f, vBottom).uv2(p).normal(1, 0, 0).endVertex();
        buff.vertex(mat, -x,  y, z).color(1.0F, 1.0F, 1.0F, a).uv(1f, vBottom).uv2(p).normal(1, 0, 0).endVertex();
        buff.vertex(mat,  x,  y, z).color(1.0F, 1.0F, 1.0F, a).uv(1f, vTop).uv2(p).normal(1, 0, 0).endVertex();
        buff.vertex(mat,  x, -y, z).color(1.0F, 1.0F, 1.0F, a).uv(0f, vTop).uv2(p).normal(1, 0, 0).endVertex();

        buff.vertex(mat, -x, -y + 0.5F, z + 0.5F).color(1.0F, 1.0F, 1.0F, a).uv(0f, vBottom).uv2(p).normal(1, 0, 0).endVertex(); // bottom z
        buff.vertex(mat, -x,  y - 0.5F, z - 0.5F).color(1.0F, 1.0F, 1.0F, a).uv(1f, vBottom).uv2(p).normal(1, 0, 0).endVertex();
        buff.vertex(mat,  x,  y - 0.5F, z - 0.5F).color(1.0F, 1.0F, 1.0F, a).uv(1f, vTop).uv2(p).normal(1, 0, 0).endVertex();
        buff.vertex(mat,  x, -y + 0.5F, z + 0.5F).color(1.0F, 1.0F, 1.0F, a).uv(0f, vTop).uv2(p).normal(1, 0, 0).endVertex(); // top z
    }
}
