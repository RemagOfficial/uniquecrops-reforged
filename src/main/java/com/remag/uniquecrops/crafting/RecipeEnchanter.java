package com.remag.uniquecrops.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.remag.uniquecrops.api.IEnchanterRecipe;
import com.remag.uniquecrops.init.UCRecipes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecipeEnchanter implements IEnchanterRecipe {

    private final ResourceLocation id;
    private final Holder<Enchantment> ench;
    private final ResourceLocation enchId;
    private final int cost;
    private final net.minecraft.core.NonNullList<Ingredient> inputs;

    public RecipeEnchanter(ResourceLocation id, Holder<Enchantment> ench, int cost, Ingredient... inputs) {

        this.id = id;
        this.ench = ench;
        this.enchId = ench.unwrapKey()
                .orElseThrow(() -> new IllegalStateException("Enchantment holder does not have a key"))
                .location();
        this.cost = cost;
        this.inputs = net.minecraft.core.NonNullList.of(Ingredient.EMPTY, inputs);
        if (inputs.length == 0)
            throw new IllegalStateException("Inputs cannot be empty or null");
    }

    @Override
    public boolean matches(RecipeInput inv, Level world) {

        if (!inputs.isEmpty()) {
            Ingredient ingredient = inputs.get(0);
            int inputsCount = 0;
            for (int i = 0; i < inv.size(); i++) {
                ItemStack stack = inv.getItem(i);
                if (stack.isEmpty()) break;
                if (ingredient.test(stack))
                    inputsCount++;
            }
            return inputsCount == ingredient.getItems().length;
        }
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(RecipeInput container, HolderLookup.Provider provider) {
        return getResultItem(provider).copy();
    }

    @Override
    public boolean matchesEnchantment(String location) {
        return enchId.toString().equals(location);
    }

    @Override
    public void applyEnchantment(ItemStack toApply) {

        toApply.enchant(this.ench, this.ench.value().getMaxLevel());
    }

    @Override
    public Enchantment getEnchantment() {

        return this.ench.value();
    }

    @Override
    public int getCost() {

        return this.cost;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
        applyEnchantment(enchantedBook);

        return enchantedBook;
    }

    public ItemStack getResultItem() {
        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
        applyEnchantment(enchantedBook);

        return enchantedBook;
    }

    @Override
    public @NotNull net.minecraft.core.NonNullList<Ingredient> getIngredients() {

        return this.inputs;
    }

    public @NotNull ResourceLocation getId() {

        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {

        return UCRecipes.ENCHANTER_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<RecipeEnchanter> {

        private static final MapCodec<RecipeEnchanter> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceLocation.CODEC.optionalFieldOf("id", IEnchanterRecipe.RES).forGetter(recipe -> recipe.id),
                Enchantment.CODEC.fieldOf("enchantment").forGetter(recipe -> recipe.ench),
                Codec.INT.fieldOf("cost").forGetter(recipe -> recipe.cost),
                Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> List.copyOf(recipe.inputs))
        ).apply(instance, (id, enchantment, cost, inputs) -> new RecipeEnchanter(id, enchantment, cost, inputs.toArray(new Ingredient[0]))));

        private static final StreamCodec<RegistryFriendlyByteBuf, RecipeEnchanter> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork,
                Serializer::fromNetwork
        );

        @Override
        public MapCodec<RecipeEnchanter> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RecipeEnchanter> streamCodec() {
            return STREAM_CODEC;
        }

        private static RecipeEnchanter fromNetwork(RegistryFriendlyByteBuf buf) {

            ResourceLocation id = buf.readResourceLocation();
            Holder<Enchantment> enchantment = Enchantment.STREAM_CODEC.decode(buf);
            int cost = buf.readVarInt();
            int size = buf.readVarInt();
            Ingredient[] inputs = new Ingredient[size];
            for (int i = 0; i < size; i++)
                inputs[i] = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);

            return new RecipeEnchanter(id, enchantment, cost, inputs);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, RecipeEnchanter recipe) {

            buf.writeResourceLocation(recipe.id);
            Enchantment.STREAM_CODEC.encode(buf, recipe.ench);
            buf.writeVarInt(recipe.cost);
            buf.writeVarInt(recipe.inputs.size());
            for (Ingredient input : recipe.inputs)
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, input);
        }
    }
}
