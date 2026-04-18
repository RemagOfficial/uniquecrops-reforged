package com.remag.uniquecrops.integration.patchouli.processor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.Objects;

public class CostProcessor implements IComponentProcessor {

    private ResourceLocation recipeId;

    @Override
    public void setup(Level level, IVariableProvider var) {

        if (!var.has("multiblock")) return;

        RecipeManager manager = level.getRecipeManager();
        ResourceLocation id = Objects.requireNonNull(ResourceLocation.tryParse(var.get("multiblock", level.registryAccess()).asString()));
        recipeId = manager.byKey(id).orElseThrow(IllegalArgumentException::new).id();
    }

    @Override
    public @NotNull IVariable process(Level level, String key) {

        if (key.equals("multiblock") && recipeId != null)
            return IVariable.wrap(recipeId.toString());

        return IVariable.wrap("");
    }
}
