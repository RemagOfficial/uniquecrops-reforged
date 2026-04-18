package com.remag.uniquecrops.api;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.crafting.RecipeMultiblock;
import com.remag.uniquecrops.init.UCRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface IMultiblockRecipe extends Recipe<RecipeInput> {

    ResourceLocation RES = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "multiblock");

    boolean match(Level world, BlockPos originBlock);
    boolean isOriginBlock(BlockState state);

    ItemStack getCatalyst();
    int getPower();
    String[] getShape();
    Map<Character, RecipeMultiblock.Slot> getDefinition();

    void setResult(Level world, BlockPos originBlock);

    @Override
    default @NotNull RecipeType<?> getType() {

        return UCRecipes.MULTIBLOCK_TYPE.get();
    }

    @Override
    default boolean matches(RecipeInput inv, Level worldIn) {

        return false;
    }

    @Override
    default boolean canCraftInDimensions(int width, int height) {

        return false;
    }

    @Override
    default boolean isSpecial() {

        return true;
    }
}
