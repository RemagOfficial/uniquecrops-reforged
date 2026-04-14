package com.remag.uniquecrops.render.tile;

import com.remag.uniquecrops.blocks.tiles.TileInvisibiliaGlass;
import com.remag.uniquecrops.init.UCBlocks;
import com.remag.uniquecrops.init.UCItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.entity.player.Player;

import static com.remag.uniquecrops.blocks.InvisibiliaGlass.VISIBLE;

public class RenderInvisibiliaGlass implements BlockEntityRenderer<TileInvisibiliaGlass> {

    private final BlockRenderDispatcher renderDispatcher;

    public RenderInvisibiliaGlass(BlockEntityRendererProvider.Context ctx) {

        this.renderDispatcher = ctx.getBlockRenderDispatcher();
    }

    @Override
    public void render(TileInvisibiliaGlass te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;
        if (!player.isCreative()) {
            if (player.getInventory().armor.get(3).getItem() != UCItems.GLASSES_3D.get())
                return;
        }

        ms.pushPose();
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
				UCBlocks.INVISIBILIA_GLASS.get().defaultBlockState().setValue(VISIBLE, true),
                ms,
                buffer,
                light,
                overlay);
        ms.popPose();
    }

}
