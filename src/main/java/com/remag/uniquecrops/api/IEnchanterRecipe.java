package com.remag.uniquecrops.api;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.init.UCRecipes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface IEnchanterRecipe extends Recipe<Container> {

    ResourceLocation RES = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "enchanter");

    @Override
    default @NotNull RecipeType<?> getType() {

        return UCRecipes.ENCHANTER_TYPE.get();
    }

    @Override
    default boolean canCraftInDimensions(int width, int height) {

        return true;
    }

    @Override
    default boolean isSpecial() {

        return true;
    }

    boolean matchesEnchantment(String location);

    void applyEnchantment(ItemStack toApply);

    Enchantment getEnchantment();

    int getCost();
}
