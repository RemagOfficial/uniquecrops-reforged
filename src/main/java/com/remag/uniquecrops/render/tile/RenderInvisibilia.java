package com.remag.uniquecrops.render.tile;

import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.blocks.tiles.TileInvisibilia;
import com.remag.uniquecrops.init.UCBlocks;
import com.remag.uniquecrops.init.UCItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.entity.player.Player;

import java.util.stream.IntStream;

import static com.remag.uniquecrops.blocks.crops.Invisibilia.VISIBLE;

public class RenderInvisibilia implements BlockEntityRenderer<TileInvisibilia> {

    private final BlockRenderDispatcher renderDispatcher;
    private final int[] textureSkipper = IntStream.of(1, 2, 2, 3, 3, 4, 4, 5).toArray();

    public RenderInvisibilia(BlockEntityRendererProvider.Context ctx) {

        this.renderDispatcher = ctx.getBlockRenderDispatcher();
    }

    @Override
    public void render(TileInvisibilia te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;
        if (!player.isCreative()) {
            if (player.getInventory().armor.get(3).getItem() != UCItems.GLASSES_3D.get())
                return;
        }

        int age = te.getBlockState().getValue(BaseCropsBlock.AGE);
        ms.pushPose();
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                UCBlocks.INVISIBILIA_CROP.get().getStateForAge(age).setValue(VISIBLE, true),
                ms,
                buffer,
                light,
                overlay);
        ms.popPose();
    }

}
