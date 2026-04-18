package com.remag.uniquecrops.integration.jei;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.crafting.RecipeArtisia;
import com.remag.uniquecrops.crafting.RecipeEnchanter;
import com.remag.uniquecrops.crafting.RecipeHeater;
import com.remag.uniquecrops.crafting.RecipeHourglass;
import com.remag.uniquecrops.init.UCBlocks;
import com.remag.uniquecrops.init.UCItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class JEIPluginUC implements IModPlugin {

    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "main");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {

        registry.addRecipeCategories(
                new UCArtisiaCategory(registry.getJeiHelpers().getGuiHelper()),
                new UCHourglassCategory(registry.getJeiHelpers().getGuiHelper()),
                new UCHeaterCategory(registry.getJeiHelpers().getGuiHelper()),
                new UCEnchanterCategory(registry.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null)
            return;

        RecipeManager recipeManager = mc.level.getRecipeManager();

        List<RecipeArtisia> artisiaRecipes = recipeManager.getRecipes().stream()
                .map(holder -> holder.value())
                .filter(RecipeArtisia.class::isInstance)
                .map(RecipeArtisia.class::cast)
                .toList();
        List<RecipeHourglass> hourglassRecipes = recipeManager.getRecipes().stream()
                .map(holder -> holder.value())
                .filter(RecipeHourglass.class::isInstance)
                .map(RecipeHourglass.class::cast)
                .toList();
        List<RecipeHeater> heaterRecipes = recipeManager.getRecipes().stream()
                .map(holder -> holder.value())
                .filter(RecipeHeater.class::isInstance)
                .map(RecipeHeater.class::cast)
                .toList();
        List<RecipeEnchanter> enchanterRecipes = recipeManager.getRecipes().stream()
                .map(holder -> holder.value())
                .filter(RecipeEnchanter.class::isInstance)
                .map(RecipeEnchanter.class::cast)
                .toList();

        registry.addRecipes(JEIRecipeTypesUC.ARTISIA, artisiaRecipes);
        registry.addRecipes(JEIRecipeTypesUC.HOURGLASS, hourglassRecipes);
        registry.addRecipes(JEIRecipeTypesUC.HEATER, heaterRecipes);
        registry.addRecipes(JEIRecipeTypesUC.ENCHANTER, enchanterRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {

        registry.addRecipeCatalyst(new ItemStack(UCItems.DUMMY_ARTISIA.get()), JEIRecipeTypesUC.ARTISIA);
        registry.addRecipeCatalyst(new ItemStack(UCBlocks.HOURGLASS.get()), JEIRecipeTypesUC.HOURGLASS);
        registry.addRecipeCatalyst(new ItemStack(UCItems.DUMMY_HEATER.get()), JEIRecipeTypesUC.HEATER);
        registry.addRecipeCatalyst(new ItemStack(UCItems.DUMMY_FASCINO.get()), JEIRecipeTypesUC.ENCHANTER);
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {

        return ID;
    }
}
