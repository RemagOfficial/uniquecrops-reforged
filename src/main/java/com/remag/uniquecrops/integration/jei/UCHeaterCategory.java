package com.remag.uniquecrops.integration.jei;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.crafting.RecipeHeater;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class UCHeaterCategory implements IRecipeCategory<RecipeHeater> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "heater");
    private final IDrawable background;

    public UCHeaterCategory(IGuiHelper helper) {

        this.background = helper.createDrawable(ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "textures/gui/heater.png"), 0, 0, 126, 64);
    }

    @Override
    public RecipeType<RecipeHeater> getRecipeType() {
        return JEIRecipeTypesUC.HEATER;
    }

    @Override
    public Component getTitle() {

        return Component.translatable("container.jei.uniquecrops.heater");
    }

    @Override
    public IDrawable getBackground() {

        return background;
    }

    @Override
    public IDrawable getIcon() {

        return null;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHeater recipe, IFocusGroup ingredients) {

        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 24)
                .addItemStack(recipe.getResultItem());

        builder.addSlot(RecipeIngredientRole.INPUT, 11, 24)
                .addItemStack(recipe.getInput());
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(RecipeHeater recipe) {
        return ID;
    }
}
