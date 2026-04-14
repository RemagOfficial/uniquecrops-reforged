package com.remag.uniquecrops.gui;

import com.remag.uniquecrops.events.UCTickHandler;
import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.items.RubiksCubeItem;
import com.remag.uniquecrops.network.PacketColorfulCube;
import com.remag.uniquecrops.network.UCPacketHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import org.joml.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class GuiColorfulCube extends Screen {

    double lastRotationTime;
    boolean hasRotated = false;

    public GuiColorfulCube() {

        super(Component.empty());
    }

    private static class InvisibleButton extends Button {
        public InvisibleButton(int x, int y, int width, int height, Component title, OnPress onPress) {
            super(x, y, width, height, title, onPress, Button.DEFAULT_NARRATION);
            this.setAlpha(0.0f);
        }
    }

    @Override
    public void init() {
        int k = this.width / 2;
        int l = this.height / 2;
        renderables.add(new InvisibleButton(k - 40, l - 40, 80, 80, CommonComponents.GUI_PROCEED, button -> this.clickTeleport()));
    }

    @Override
    public boolean isPauseScreen() {

        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack ms = guiGraphics.pose();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Level level = Minecraft.getInstance().level;

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        double time = (UCTickHandler.ticksInGame + UCTickHandler.partialTicks) * 12F;

        ms.pushPose();

        ms.translate(guiGraphics.guiWidth()/2, guiGraphics.guiHeight()/2, 0.001F); // small offset to avoid z-fighting

        ms.scale(225f, 225f, 225f);

        Vec3i vec3 = this.getRotationVec();
        Vector3f vecf = new Vector3f(vec3.getX(), vec3.getY(), vec3.getZ());
        boolean north = vec3.equals(Direction.NORTH.getNormal());

        if (hasRotated) {
            if (lastRotationTime == -1)
                this.lastRotationTime = time;

            double rotationElapsed = time - lastRotationTime;

            if (north) {
                Vector3f yAxis = new Vector3f(0f, 1f, 0f);
                Quaternionf quat = new Quaternionf();
                quat.fromAxisAngleDeg(yAxis, (float)rotationElapsed + 90f);
                ms.mulPose(quat);

                if (rotationElapsed + 90F >= 180F) {
                    this.lastRotationTime = -1;
                    this.hasRotated = false;
                }
            } else {
                Quaternionf quat = new Quaternionf();
                quat.fromAxisAngleDeg(vecf, (float)rotationElapsed);
                ms.mulPose(quat);

                if (rotationElapsed >= 90F) {
                    this.lastRotationTime = -1;
                    this.hasRotated = false;
                }
            }
        } else {
            if (north) {
                Vector3f yAxis = new Vector3f(0f, 1f, 0f);
                Quaternionf quat = new Quaternionf();
                quat.fromAxisAngleDeg(yAxis, 180f);
                ms.mulPose(quat);
            } else {
                Quaternionf quat = new Quaternionf();
                quat.fromAxisAngleDeg(vecf, 90f);
                ms.mulPose(quat);
            }
        }

        ItemStack cubeStack = new ItemStack(UCItems.RUBIKS_CUBE.get());

        MultiBufferSource.BufferSource renderBuffer = guiGraphics.bufferSource();

        // The packed light is 0xF000F0 for full brightness in MC — keep as is
        int packedLight = 0xF000F0;

        itemRenderer.renderStatic(cubeStack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, ms, renderBuffer, level, 0);

        renderBuffer.endBatch();

        RenderSystem.disableBlend();

        ms.popPose();

        // Call super.render with GuiGraphics, since the signature changed
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if (this.hasRotated)
            return super.keyPressed(keyCode, scanCode, modifiers);

        int i = ((RubiksCubeItem)UCItems.RUBIKS_CUBE.get()).getRotation(getCube());
        switch (keyCode) {
            case GLFW.GLFW_KEY_W: rotateUp(i); break;
            case GLFW.GLFW_KEY_A: rotateLeft(i); break;
            case GLFW.GLFW_KEY_S: rotateDown(i); break;
            case GLFW.GLFW_KEY_D: rotateRight(i); break;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if (button == 0 && !renderables.isEmpty()) {
            if (renderables.get(0) instanceof Button && ((Button)renderables.get(0)).isHoveredOrFocused())
                this.clickTeleport();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private Vec3i getRotationVec() {

        int rot = ((RubiksCubeItem)UCItems.RUBIKS_CUBE.get()).getRotation(getCube());
        Direction facing = Direction.from3DDataValue(rot);
        return facing.getNormal();
    }

    private void rotateLeft(int i) {

        Direction facing = Direction.from3DDataValue(i);
        if (facing == Direction.UP || facing == Direction.DOWN) return;
        updateCube(facing.getClockWise().ordinal());
    }

    private void rotateRight(int i) {

        Direction facing = Direction.from3DDataValue(i);
        if (facing == Direction.UP || facing == Direction.DOWN) return;
        updateCube(facing.getCounterClockWise().ordinal());
    }

    private void rotateUp(int i) {

        Direction facing = Direction.from3DDataValue(i);
        if (facing != Direction.UP && facing != Direction.DOWN)
            updateCube(Direction.UP.ordinal());
        if (facing == Direction.DOWN) {
            updateCube(Direction.NORTH.ordinal());
        }
    }

    private void rotateDown(int i) {

        Direction facing = Direction.from3DDataValue(i);
        if (facing != Direction.DOWN && facing != Direction.UP)
            updateCube(Direction.DOWN.ordinal());
        if (facing == Direction.UP) {
            updateCube(Direction.NORTH.ordinal());
        }
    }

    private void updateCube(int rotation) {

        UCPacketHandler.INSTANCE.sendToServer(new PacketColorfulCube(rotation, false));
        this.hasRotated = true;
    }

    private ItemStack getCube() {

        ItemStack stack = Minecraft.getInstance().player.getMainHandItem();
        if (stack.getItem() == UCItems.RUBIKS_CUBE.get())
            return stack;

        return ItemStack.EMPTY;
    }

    private void clickTeleport() {

        ItemStack cube = getCube();
        if (!cube.isEmpty()) {
            int rot = ((RubiksCubeItem)cube.getItem()).getRotation(cube);
            UCPacketHandler.INSTANCE.sendToServer(new PacketColorfulCube(rot, true));
            this.onClose();
        }
    }
}
