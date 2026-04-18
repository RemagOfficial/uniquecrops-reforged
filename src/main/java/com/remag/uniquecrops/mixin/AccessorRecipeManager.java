package com.remag.uniquecrops.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;
import java.util.List;

// TODO: Fix obfuscation mappings for 1.21.1
@Mixin(RecipeManager.class)
public interface AccessorRecipeManager {
    @Invoker("byType")
    <I extends RecipeInput, T extends Recipe<I>> Collection<RecipeHolder<T>> uc_byType(RecipeType<T> type);

    @Invoker("getAllRecipesFor")
    <C extends Container, T extends Recipe<RecipeInput>> List<T> uc_getRecipes(RecipeType<T> type);
}
