package com.remag.uniquecrops.integration.jei;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.crafting.RecipeArtisia;
import com.remag.uniquecrops.crafting.RecipeEnchanter;
import com.remag.uniquecrops.crafting.RecipeHeater;
import com.remag.uniquecrops.crafting.RecipeHourglass;
import mezz.jei.api.recipe.RecipeType;

public class JEIRecipeTypesUC {
    public static final RecipeType<RecipeArtisia> ARTISIA = RecipeType.create(UniqueCrops.MOD_ID, "artisia", RecipeArtisia.class);
    public static final RecipeType<RecipeHourglass> HOURGLASS = RecipeType.create(UniqueCrops.MOD_ID, "hourglass", RecipeHourglass.class);
    public static final RecipeType<RecipeHeater> HEATER = RecipeType.create(UniqueCrops.MOD_ID, "heater", RecipeHeater.class);
    public static final RecipeType<RecipeEnchanter> ENCHANTER = RecipeType.create(UniqueCrops.MOD_ID, "enchanter", RecipeEnchanter.class);
}
