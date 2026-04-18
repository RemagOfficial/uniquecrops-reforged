package com.remag.uniquecrops.init;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.crafting.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@EventBusSubscriber(modid = UniqueCrops.MOD_ID)
public class UCRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, UniqueCrops.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, UniqueCrops.MOD_ID);

    // Recipe Serializers
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> ARTISIA_SERIALIZER =
            registerSerializer("artisia", RecipeArtisia.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> HOURGLASS_SERIALIZER =
            registerSerializer("hourglass", RecipeHourglass.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> ENCHANTER_SERIALIZER =
            registerSerializer("enchanter", RecipeEnchanter.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> HEATER_SERIALIZER =
            registerSerializer("heater", RecipeHeater.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> MULTIBLOCK_SERIALIZER =
            registerSerializer("multiblock", RecipeMultiblock.Serializer::new);

    // Recipe Types
    public static final DeferredHolder<RecipeType<?>, RecipeType<RecipeArtisia>> ARTISIA_TYPE =
            registerType("artisia");
    public static final DeferredHolder<RecipeType<?>, RecipeType<RecipeHourglass>> HOURGLASS_TYPE =
            registerType("hourglass");
    public static final DeferredHolder<RecipeType<?>, RecipeType<RecipeEnchanter>> ENCHANTER_TYPE =
            registerType("enchanter");
    public static final DeferredHolder<RecipeType<?>, RecipeType<RecipeHeater>> HEATER_TYPE =
            registerType("heater");
    public static final DeferredHolder<RecipeType<?>, RecipeType<RecipeMultiblock>> MULTIBLOCK_TYPE =
            registerType("multiblock");

    @SubscribeEvent
    public static void registerBrews(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addRecipe(potionBrewRecipe(Potions.AWKWARD, UCItems.TIMEDUST.get(), UCItems.POTION_REVERSE.get()));
        event.getBuilder().addRecipe(potionBrewRecipe(Potions.INVISIBILITY, UCBlocks.INVISIBILIA_GLASS.get(), UCItems.POTION_IGNORANCE.get()));
        event.getBuilder().addRecipe(potionBrewRecipe(Potions.AWKWARD, UCItems.ZOMBIE_SLURRY.get(), UCItems.POTION_ZOMBIFICATION.get()));
        event.getBuilder().addRecipe(potionBrewRecipe(Potions.AWKWARD, UCBlocks.DARK_BLOCK.get(), UCItems.POTION_ENNUI.get()));
    }

    private static IBrewingRecipe potionBrewRecipe(net.minecraft.core.Holder<net.minecraft.world.item.alchemy.Potion> inputPotion,
                                                    net.minecraft.world.level.ItemLike reagent,
                                                    net.minecraft.world.level.ItemLike output) {
        return new IBrewingRecipe() {
            @Override
            public boolean isInput(@NotNull ItemStack input) {
                PotionContents contents = input.get(DataComponents.POTION_CONTENTS);
                return contents != null && contents.is(inputPotion);
            }
            @Override
            public boolean isIngredient(@NotNull ItemStack ingredient) {
                return ingredient.is(reagent.asItem());
            }
            @Override
            public @NotNull ItemStack getOutput(@NotNull ItemStack input, @NotNull ItemStack ingredient) {
                return new ItemStack(output.asItem());
            }
        };
    }

    private static <R extends RecipeSerializer<?>> DeferredHolder<RecipeSerializer<?>, R> registerSerializer(String name, Supplier<? extends R> supplier) {
        return RECIPE_SERIALIZERS.register(name, supplier);
    }

    public static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> registerType(String name) {
        return RECIPE_TYPES.register(name, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return UniqueCrops.MOD_ID + ":" + name;
            }
        });
    }
}
