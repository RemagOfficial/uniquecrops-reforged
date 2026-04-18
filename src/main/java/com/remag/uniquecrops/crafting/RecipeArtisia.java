package com.remag.uniquecrops.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.remag.uniquecrops.api.IArtisiaRecipe;
import com.remag.uniquecrops.core.UCUtils;
import com.remag.uniquecrops.init.UCRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecipeArtisia implements IArtisiaRecipe {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> inputs;

    public RecipeArtisia(ResourceLocation id, ItemStack output, Ingredient... inputs) {

        this.id = id;
        this.output = output;
        this.inputs = NonNullList.of(Ingredient.EMPTY, inputs);
        if (inputs.length > 9)
            throw new IllegalStateException("Inputs cannot be more than 9");
    }

    @Override
    public boolean matches(RecipeInput inv, Level world) {

        List<Ingredient> ingredientsMissing = new ArrayList<>(inputs);

        for (int i = 0; i < inv.size(); i++) {
            ItemStack input = inv.getItem(i);
            if (input.isEmpty()) {
                break;
            }
            int stackIndex = -1;
            for (int j = 0; j < ingredientsMissing.size(); j++) {
                Ingredient ingr = ingredientsMissing.get(j);
                if (ingr.test(input)) {
                    stackIndex = j;
                    break;
                }
            }
            if (stackIndex != -1) {
                ingredientsMissing.remove(stackIndex);
            } else {
                return false;
            }
        }
        return ingredientsMissing.isEmpty();
    }

    @Override
    public @NotNull ItemStack assemble(RecipeInput container, HolderLookup.Provider provider) {
        return getResultItem(provider).copy();
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return output.copy();
    }

    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {

        return this.inputs;
    }

    public @NotNull ResourceLocation getId() {

        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {

        return UCRecipes.ARTISIA_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<RecipeArtisia> {

        private static final MapCodec<RecipeArtisia> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceLocation.CODEC.optionalFieldOf("id", IArtisiaRecipe.RES).forGetter(recipe -> recipe.id),
                ItemStack.CODEC.fieldOf("output").forGetter(recipe -> recipe.output),
                Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> List.copyOf(recipe.inputs))
        ).apply(instance, (id, output, inputs) -> new RecipeArtisia(id, output, inputs.toArray(new Ingredient[0]))));

        private static final StreamCodec<RegistryFriendlyByteBuf, RecipeArtisia> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork,
                Serializer::fromNetwork
        );

        @Override
        public MapCodec<RecipeArtisia> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RecipeArtisia> streamCodec() {
            return STREAM_CODEC;
        }

        private static RecipeArtisia fromNetwork(RegistryFriendlyByteBuf buf) {

            ResourceLocation id = buf.readResourceLocation();
            ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
            int size = buf.readVarInt();
            Ingredient[] ingredients = new Ingredient[size];
            for (int i = 0; i < size; i++)
                ingredients[i] = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);

            return new RecipeArtisia(id, output, ingredients);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, RecipeArtisia recipe) {

            buf.writeResourceLocation(recipe.id);
            ItemStack.STREAM_CODEC.encode(buf, recipe.output);
            buf.writeVarInt(recipe.inputs.size());
            for (Ingredient input : recipe.inputs)
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, input);
        }
    }

    public static IArtisiaRecipe findRecipe(List<ItemStack> inputs, Level level) {

        for (RecipeHolder<?> holder : level.getRecipeManager().getRecipes()) {
            Recipe<?> recipe = holder.value();
            if (recipe instanceof IArtisiaRecipe artisiaRecipe && artisiaRecipe.matches(UCUtils.wrap(inputs), level))
                return artisiaRecipe;
        }
        return null;
    }

}
