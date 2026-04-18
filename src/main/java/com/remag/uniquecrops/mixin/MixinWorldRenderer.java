package com.remag.uniquecrops.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.remag.uniquecrops.events.UCEventHandlerClient;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class MixinWorldRenderer {
    @Inject(at = @At("RETURN"), method = "renderLevel", remap = false)
    public void onRender(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo info) {
        PoseStack ms = new PoseStack();
        ms.mulPose(frustumMatrix);
        UCEventHandlerClient.renderWorldLast(ms);
    }
}
