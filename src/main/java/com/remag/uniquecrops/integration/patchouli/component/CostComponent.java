package com.remag.uniquecrops.integration.patchouli.component;

import com.remag.uniquecrops.api.IMultiblockRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.function.UnaryOperator;

public class CostComponent implements ICustomComponent {

    ResourceLocation RES = ResourceLocation.fromNamespaceAndPath("patchouli", "textures/gui/crafting.png");

    private transient int x, y;
    private transient ItemStack stack = ItemStack.EMPTY;
    private transient int cost;

    IVariable multiblock;

    @Override
    public void build(int cx, int cy, int pageNum) {

        this.x = cx;
        this.y = cy;
    }

    @Override
    public void render(GuiGraphics guiGraphics, IComponentRenderContext ctx, float pticks, int mouseX, int mouseY) {
        PoseStack ms = guiGraphics.pose();

        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, RES);
        int w = 66, h = 26;
        guiGraphics.blit(RES, (x + 120) / 2 - w / 2, 10, 0, 128 - h, w, h, 128, 256);
        ctx.renderItemStack(guiGraphics, (x + 120) / 2 - 8, 14, mouseX, mouseY, stack);
        String title = "Recipe Catalyst", text = (cost > 0) ? "Cost: " + cost : "Consumable";
        ms.pushPose();
        guiGraphics.drawString(mc.font, title, (x + 60) - mc.font.width(title) / 2.0F, 0, 0, false);
        guiGraphics.drawString(mc.font, text, (x + 60) - mc.font.width(text) / 2.0F, y, 0, false);
        ms.popPose();
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup, HolderLookup.Provider registries) {

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null)
            return;

        IVariable recipeVar = lookup.apply(multiblock);
        ResourceLocation id = ResourceLocation.tryParse(recipeVar.asString());
        if (id == null)
            return;

        RecipeHolder<?> holder = mc.level.getRecipeManager().byKey(id).orElseThrow(IllegalArgumentException::new);
        if (holder.value() instanceof IMultiblockRecipe mb) {
            stack = mb.getCatalyst();
            cost = mb.getPower();
        }
    }
}
