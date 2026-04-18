package com.remag.uniquecrops.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.remag.uniquecrops.api.IHourglassRecipe;
import com.remag.uniquecrops.init.UCRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class RecipeHourglass implements IHourglassRecipe {

    private final ResourceLocation id;
    private final BlockState input;
    private final BlockState output;

    public RecipeHourglass(ResourceLocation id, BlockState input, BlockState output) {

        this.id = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(BlockState state) {

        return input.equals(state);
    }

    @Override
    public BlockState getInput() {

        return input;
    }

    @Override
    public BlockState getOutput() {

        return output;
    }

    @Override
    public @NotNull ItemStack assemble(RecipeInput container, HolderLookup.Provider provider) {
        return getResultItem(provider).copy();
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return new ItemStack(output.getBlock().asItem());
    }

    public ItemStack getResultItem() {
        return new ItemStack(output.getBlock().asItem());
    }

    public @NotNull ResourceLocation getId() {

        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {

        return UCRecipes.HOURGLASS_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<RecipeHourglass> {

        private static final MapCodec<RecipeHourglass> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceLocation.CODEC.optionalFieldOf("id", IHourglassRecipe.RES).forGetter(recipe -> recipe.id),
                BlockState.CODEC.fieldOf("input").forGetter(recipe -> recipe.input),
                BlockState.CODEC.fieldOf("output").forGetter(recipe -> recipe.output)
        ).apply(instance, RecipeHourglass::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, RecipeHourglass> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork,
                Serializer::fromNetwork
        );

        @Override
        public MapCodec<RecipeHourglass> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RecipeHourglass> streamCodec() {
            return STREAM_CODEC;
        }

        private static RecipeHourglass fromNetwork(RegistryFriendlyByteBuf buf) {

            ResourceLocation id = buf.readResourceLocation();
            BlockState input = Block.stateById(buf.readVarInt());
            BlockState output = Block.stateById(buf.readVarInt());
            return new RecipeHourglass(id, input, output);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, RecipeHourglass recipe) {

            buf.writeResourceLocation(recipe.id);
            buf.writeVarInt(Block.getId(recipe.input));
            buf.writeVarInt(Block.getId(recipe.output));
        }
    }
}
