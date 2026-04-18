package com.remag.uniquecrops.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.remag.uniquecrops.api.IHeaterRecipe;
import com.remag.uniquecrops.init.UCRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RecipeHeater implements IHeaterRecipe {

    private final ResourceLocation id;
    private final ItemStack output, input;

    public RecipeHeater(ResourceLocation id, ItemStack output, ItemStack input) {

        this.id = id;
        this.output = output;
        this.input = input;
    }

    @Override
    public boolean matches(RecipeInput inv, Level world) {

        return ItemStack.isSameItem(inv.getItem(0), this.input);
    }

    public boolean matches(ItemStack stack) {

        return ItemStack.isSameItem(stack, this.input);
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
        return this.output.copy();
    }

    @Override
    public ItemStack getInput() {

        return this.input;
    }

    public @NotNull ResourceLocation getId() {

        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {

        return UCRecipes.HEATER_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<RecipeHeater> {

        private static final MapCodec<RecipeHeater> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceLocation.CODEC.optionalFieldOf("id", IHeaterRecipe.RES).forGetter(recipe -> recipe.id),
                ItemStack.CODEC.fieldOf("output").forGetter(recipe -> recipe.output),
                ItemStack.CODEC.fieldOf("input").forGetter(recipe -> recipe.input)
        ).apply(instance, RecipeHeater::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, RecipeHeater> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork,
                Serializer::fromNetwork
        );

        @Override
        public MapCodec<RecipeHeater> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RecipeHeater> streamCodec() {
            return STREAM_CODEC;
        }

        private static RecipeHeater fromNetwork(RegistryFriendlyByteBuf buf) {

            ResourceLocation id = buf.readResourceLocation();
            ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
            ItemStack input = ItemStack.STREAM_CODEC.decode(buf);
            return new RecipeHeater(id, output, input);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, RecipeHeater recipe) {

            buf.writeResourceLocation(recipe.id);
            ItemStack.STREAM_CODEC.encode(buf, recipe.output);
            ItemStack.STREAM_CODEC.encode(buf, recipe.input);
        }
    }
}
