package com.remag.uniquecrops.crafting;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.remag.uniquecrops.api.IMultiblockRecipe;
import com.remag.uniquecrops.core.UCUtils;
import com.remag.uniquecrops.init.UCRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.function.Predicate;

public class RecipeMultiblock implements IMultiblockRecipe {

    private final ResourceLocation id;
    private final ItemStack catalyst;
    private final int power;
    private final String[] shape;
    private final String[] shapeResult;
    private final Point origin;
    private final Map<Character, Slot> definition;
    private final Map<Character, Slot> definitionResult;

    public RecipeMultiblock(ResourceLocation id, ItemStack catalyst, int power, String[] shape, String[] shapeResult, Point origin, Map<Character, Slot> definition, Map<Character, Slot> definitionResult) {

        this.id = id;
        this.catalyst = catalyst;
        this.power = power;
        this.shape = shape;
        this.shapeResult = shapeResult;
        this.origin = origin;
        this.definition = new HashMap<>(definition);
        this.definition.put(' ', new Slot(Blocks.AIR.defaultBlockState()));
        this.definitionResult = new HashMap<>(definitionResult);
        this.definitionResult.put(' ', new Slot(Blocks.AIR.defaultBlockState()));

        char originChar = shape[origin.y].charAt(origin.x);
        if (originChar == ' ' || definition.get(originChar).test(Blocks.AIR.defaultBlockState()))
            throw new IllegalStateException(id + ": Origin point cannot be blank space");

        int lineLength = shape[0].length();
        for (String line : shape) {
            if (line.length() != lineLength)
                throw new IllegalStateException(id + ": All lines in the shape must be the same size");
            for (char letter : line.toCharArray())
                if (definition.get(letter) == null)
                    throw new IllegalStateException(id + ": " + letter + " is not defined");
        }
        for (String line2 : shapeResult) {
            if (line2.length() != lineLength)
                throw new IllegalStateException(id + ": All lines in the shape must be the same size");
            for (char letter : line2.toCharArray())
                if (definitionResult.get(letter) == null)
                    throw new IllegalStateException(id + ": " + letter + " is not defined");
        }
    }

    @Override
    public @NotNull ItemStack assemble(RecipeInput container, HolderLookup.Provider provider) {
        return getResultItem(provider).copy();
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {

        return UCRecipes.MULTIBLOCK_SERIALIZER.get();
    }

    @Override
    public boolean match(Level world, BlockPos originBlock) {

        for (int y = 0; y < shape.length; y++) {
            String line = shape[y];
            for (int x = 0; x < line.length(); x++) {
                BlockPos offset = originBlock.offset(x - origin.x, 0, y - origin.y);
                BlockState state = world.getBlockState(offset);
                if (!definition.get(line.charAt(x)).test(state))
                    return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getCatalyst() {

        return this.catalyst;
    }

    @Override
    public int getPower() {

        return this.power;
    }

    @Override
    public String[] getShape() {

        return this.shape;
    }

    public Point getOrigin() {
        return this.origin;
    }

    @Override
    public Map<Character, Slot> getDefinition() {

        return this.definition;
    }

    @Override
    public void setResult(Level world, BlockPos originBlock) {

        for (int y = 0; y < shapeResult.length; y++) {
            String line = shapeResult[y];
            for (int x = 0; x < line.length(); x++) {
                BlockPos offset = originBlock.offset(x - origin.x, 0, y - origin.y);
                BlockState state = definitionResult.get(line.charAt(x)).getFirstState();
                world.levelEvent(2001, offset, Block.getId(state));
                world.setBlock(offset, state, 2);
            }
        }
    }

    @Override
    public boolean isOriginBlock(BlockState state) {

        Slot slot = definition.get(shape[origin.y].charAt(origin.x));
        return slot.test(state);
    }

    public static class Slot implements Predicate<BlockState> {

        public final Set<BlockState> states;

        public Slot(BlockState... states) {

            this.states = Sets.newHashSet(states);
        }

        public Slot(Block block) {

            this(block.defaultBlockState());
        }

        @Override
        public boolean test(BlockState state) {

            if (getFirstState().equals(state.getBlock().defaultBlockState()))
                return true;

            return states.contains(state);
        }

        public BlockState getFirstState() {

            Iterator<BlockState> iter = states.iterator();
            return iter.next();
        }
    }

    public static class Serializer implements RecipeSerializer<RecipeMultiblock> {

        private static final Codec<Character> CHARACTER_CODEC = Codec.STRING.xmap(s -> s.charAt(0), String::valueOf);
        private static final Codec<Slot> SLOT_CODEC = Codec.list(BlockState.CODEC)
                .xmap(states -> new Slot(states.toArray(new BlockState[0])), slot -> new ArrayList<>(slot.states));
        private static final Codec<Point> POINT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("x").forGetter(point -> point.x),
                Codec.INT.fieldOf("y").forGetter(point -> point.y)
        ).apply(instance, Point::new));
        private static final Codec<String[]> SHAPE_CODEC = Codec.list(Codec.STRING)
                .xmap(list -> list.toArray(new String[0]), Arrays::asList);

        private static final MapCodec<RecipeMultiblock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceLocation.CODEC.optionalFieldOf("id", IMultiblockRecipe.RES).forGetter(recipe -> recipe.id),
                ItemStack.CODEC.fieldOf("catalyst").forGetter(recipe -> recipe.catalyst),
                Codec.INT.fieldOf("power").forGetter(recipe -> recipe.power),
                SHAPE_CODEC.fieldOf("shape").forGetter(recipe -> recipe.shape),
                SHAPE_CODEC.fieldOf("shaperesult").forGetter(recipe -> recipe.shapeResult),
                POINT_CODEC.fieldOf("origin").forGetter(recipe -> recipe.origin),
                Codec.unboundedMap(CHARACTER_CODEC, SLOT_CODEC).fieldOf("definition").forGetter(recipe -> recipe.definition),
                Codec.unboundedMap(CHARACTER_CODEC, SLOT_CODEC).fieldOf("definitionresult").forGetter(recipe -> recipe.definitionResult)
        ).apply(instance, RecipeMultiblock::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, RecipeMultiblock> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork,
                Serializer::fromNetwork
        );

        @Override
        public MapCodec<RecipeMultiblock> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RecipeMultiblock> streamCodec() {
            return STREAM_CODEC;
        }

        private static RecipeMultiblock fromNetwork(RegistryFriendlyByteBuf buf) {

            ResourceLocation id = buf.readResourceLocation();
            ItemStack catalyst = ItemStack.STREAM_CODEC.decode(buf);
            int power = buf.readVarInt();
            String[] shape = UCUtils.deserializeString(buf);
            String[] shapeResult = UCUtils.deserializeString(buf);
            int[] origin = buf.readVarIntArray();
            Point point = new Point(origin[0], origin[1]);
            CompoundTag defTag = Objects.requireNonNullElseGet(buf.readNbt(), CompoundTag::new);
            CompoundTag defResultTag = Objects.requireNonNullElseGet(buf.readNbt(), CompoundTag::new);
            Map<Character, Slot> definition = UCUtils.deserializeMap("definition", defTag);
            Map<Character, Slot> definitionResult = UCUtils.deserializeMap("definitionresult", defResultTag);

            return new RecipeMultiblock(id, catalyst, power, shape, shapeResult, point, definition, definitionResult);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, RecipeMultiblock recipe) {

            buf.writeResourceLocation(recipe.id);
            ItemStack.STREAM_CODEC.encode(buf, recipe.catalyst);
            buf.writeVarInt(recipe.power);
            UCUtils.serializeArray(buf, recipe.shape);
            UCUtils.serializeArray(buf, recipe.shapeResult);
            buf.writeVarIntArray(new int[] { recipe.origin.x, recipe.origin.y });
            buf.writeNbt(UCUtils.serializeMap("definition", recipe.definition));
            buf.writeNbt(UCUtils.serializeMap("definitionresult", recipe.definitionResult));
        }
    }
}
