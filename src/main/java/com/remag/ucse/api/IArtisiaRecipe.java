package com.remag.ucse.api;

import com.remag.ucse.UniqueCrops;
import com.remag.ucse.core.UCUtils;
import com.remag.ucse.init.UCRecipes;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IArtisiaRecipe extends Recipe<Container> {

    ResourceLocation RES = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "artisia");

    ItemStack getResultItem();

    @Override
    default @NotNull RecipeType<?> getType() {

        return UCRecipes.ARTISIA_TYPE.get();
    }

    @Override
    default boolean canCraftInDimensions(int width, int height) {

        return true;
    }

    @Override
    default boolean isSpecial() {

        return true;
    }

}
