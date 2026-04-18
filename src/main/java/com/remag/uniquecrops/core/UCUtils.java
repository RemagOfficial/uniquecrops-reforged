package com.remag.uniquecrops.core;

import com.google.gson.JsonArray;
import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.core.enums.EnumGrowthSteps;
import com.remag.uniquecrops.crafting.RecipeMultiblock;
import com.remag.uniquecrops.network.PacketChangeBiome;
import com.remag.uniquecrops.network.UCPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UCUtils {

    public static Player getPlayerFromUUID(String uuid) {

        return UniqueCrops.proxy.getPlayerFromUUID(uuid);
    }

    public static LivingEntity getTaggedEntity(UUID uuid) {

        if (uuid != null) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null)
                return null;
            for (ServerLevel ws : server.getAllLevels()) {
                Entity entity = ws.getEntity(uuid);
                if (entity instanceof LivingEntity && entity.isAlive())
                    return (LivingEntity)entity;
            }
        }
        return null;
    }

    public static void serializeArray(FriendlyByteBuf buf, String[] str) {

        String serialize = Arrays.stream(str).map(s -> s.replace(",", "\\,")).collect(Collectors.joining(","));
        buf.writeUtf(serialize);
    }

    public static String[] deserializeString(FriendlyByteBuf buf) {

        return Pattern.compile("(?<!\\\\),").splitAsStream(buf.readUtf()).map(s -> s.replace("\\,", ",")).toArray(String[]::new);
    }

    public static String[] convertJson(JsonArray array) {

        if (array == null) return null;

        String[] arr = new String[array.size()];
        for (int i = 0; i < arr.length; i++)
            arr[i] = array.get(i).getAsString();

        return arr;
    }

    public static CompoundTag serializeMap(String name, Map<Character, RecipeMultiblock.Slot> recipe) {

        CompoundTag nbt = new CompoundTag();
        ListTag list = new ListTag();
        recipe.forEach((key, value) -> {
            CompoundTag charTag = new CompoundTag();
            int[] states = value.states.stream().mapToInt(Block::getId).toArray();
            charTag.putIntArray(key.toString(), states);
            list.add(charTag);
        });
        nbt.put(name, list);
        return nbt;
    }

    public static Map<Character, RecipeMultiblock.Slot> deserializeMap(String name, CompoundTag nbt) {

        Map<Character, RecipeMultiblock.Slot> map = new HashMap<>();
        if (nbt.contains(name)) {
            ListTag list = nbt.getList(name, 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag tag = list.getCompound(i);
                for (String str : tag.getAllKeys()) {
                    BlockState[] states = Arrays.stream(tag.getIntArray(str)).mapToObj(Block::stateById).toArray(BlockState[]::new);
                    RecipeMultiblock.Slot slot = new RecipeMultiblock.Slot(states);
                    map.put(str.charAt(0), slot);
                }
            }
        }
        return map;
    }

    private static final Set<BlockEntity> tracked = ConcurrentHashMap.newKeySet();

    public static void register(BlockEntity tile) {
        tracked.add(tile);
    }

    public static void unregister(BlockEntity tile) {
        tracked.remove(tile);
    }

    public static BlockEntity getClosest(BlockPos pos, double maxDistSqr, Class<?> clazz) {
        BlockEntity closest = null;
        for (BlockEntity tile : tracked) {
            if (clazz.isInstance(tile) && !tile.getBlockPos().equals(pos)) {
                double dist = tile.getBlockPos().distSqr(pos);
                if (dist <= maxDistSqr) {
                    closest = tile;
                    maxDistSqr = dist;
                }
            }
        }
        return closest;
    }


    public static ListTag getServerTaglist(UUID uuid) {

        LivingEntity entity = getTaggedEntity(uuid);
        if (!(entity instanceof Player)) return null;

        CompoundTag tag = entity.getPersistentData();
        if (tag.contains(UCStrings.TAG_GROWTHSTAGES))
            return tag.getList(UCStrings.TAG_GROWTHSTAGES, 10);

        return null;
    }

    // Updated: wrap now returns RecipeInput for NeoForge 1.21+
    public static RecipeInput wrap(List<ItemStack> stacks) {
        // If only one stack, use SingleRecipeInput
        if (stacks.size() == 1) {
            return new SingleRecipeInput(stacks.get(0));
        }
        // If multiple, use the first non-empty stack (or adapt if you have a MultiRecipeInput)
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                return new SingleRecipeInput(stack);
            }
        }
        // Fallback: empty input
        return new SingleRecipeInput(ItemStack.EMPTY);
    }

    public static void drawSplitString(GuiGraphics guiGraphics, Font font, Component text, float x, float y, int wordWrap, int color) {
        List<FormattedCharSequence> lines = font.split(text, wordWrap);
        for (FormattedCharSequence line : lines) {
            guiGraphics.drawString(font, line, (int) x, (int) y, color, true);
            y += font.lineHeight;
        }
    }

    public static void generateSteps(Player player) {

        CompoundTag tag = player.getPersistentData();
        if (tag.contains(UCStrings.TAG_GROWTHSTAGES))
            tag.remove(UCStrings.TAG_GROWTHSTAGES);

        if (UCConfig.COMMON.selfSacrifice.get() && player.getCommandSenderWorld().random.nextInt(200) == 0) {
            ListTag taglist = new ListTag();
            CompoundTag tag2 = new CompoundTag();
            tag2.putInt("stage0", 20);
            taglist.add(tag2);
            tag.put(UCStrings.TAG_GROWTHSTAGES, taglist);
            return;
        }

        List<EnumGrowthSteps> copysteps = Arrays.stream(EnumGrowthSteps.values())
                .filter(g -> g.isEnabled() && g != EnumGrowthSteps.SELFSACRIFICE).collect(Collectors.toList());
        Collections.shuffle(copysteps);
        ListTag taglist = new ListTag();
        for (int i = 0; i < 7; ++i) {
            CompoundTag tag2 = new CompoundTag();
            int index = copysteps.get(0).ordinal();
            copysteps.remove(0);
            tag2.putInt("stage" + i, index);
            taglist.add(tag2);
        }
        tag.put(UCStrings.TAG_GROWTHSTAGES, taglist);
    }

    public static void setAbstractCropGrowth(LivingEntity player, int num) {

        CompoundTag tag = player.getPersistentData();
        if (!tag.contains(UCStrings.TAG_ABSTRACT)) {
            tag.putInt(UCStrings.TAG_ABSTRACT, num);
            return;
        }
        tag.putInt(UCStrings.TAG_ABSTRACT, tag.getInt(UCStrings.TAG_ABSTRACT) + num);
        if (tag.getInt(UCStrings.TAG_ABSTRACT) <= 0)
            tag.remove(UCStrings.TAG_ABSTRACT);
    }

    public static boolean setBiome(ResourceLocation biomeId, Level world, BlockPos pos) {
        if (world.isClientSide()) return false;

        ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, biomeId);
        Holder<Biome> biome = world.registryAccess()
                .registryOrThrow(Registries.BIOME)
                .getHolderOrThrow(key);

        if (world.getBiome(pos).is(key)) return false;

        int x = QuartPos.fromBlock(pos.getX());
        int y = QuartPos.fromBlock(pos.getY());
        int z = QuartPos.fromBlock(pos.getZ());

        LevelChunk chunk = (LevelChunk) world.getChunk(pos);
        LevelChunkSection[] sections = chunk.getSections();

        int quartX = x & 3;
        int quartY = y & 3;
        int quartZ = z & 3;

        int sectionIndex = (QuartPos.fromBlock(pos.getY()) - QuartPos.fromBlock(world.getMinBuildHeight())) / 4;
        if (sectionIndex < 0 || sectionIndex >= sections.length) return false;

        LevelChunkSection section = sections[sectionIndex];
        PalettedContainer<Holder<Biome>> biomes = (PalettedContainer<Holder<Biome>>) section.getBiomes();

        Holder<Biome> current = biomes.get(quartX, quartY, quartZ);
        if (current.is(key)) return false;

        biomes.set(quartX, quartY, quartZ, biome);
        chunk.setUnsaved(true);

        if (world instanceof ServerLevel) {
            PacketChangeBiome msg = new PacketChangeBiome(pos, biomeId);
            UCPacketHandler.sendToNearbyPlayers(world, pos, msg);
        }

        return true;
    }


    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> Collection<T> loadType(RecipeType<T> type) {

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null)
                return Collections.emptyList();

            return server.getRecipeManager().getRecipes().stream()
                    .map(RecipeHolder::value)
                    .filter(recipe -> recipe.getType() == type)
                    .map(recipe -> (T) recipe)
                    .toList();
        }

        return mc.level.getRecipeManager().getRecipes().stream()
                .map(RecipeHolder::value)
                .filter(recipe -> recipe.getType() == type)
                .map(recipe -> (T) recipe)
                .toList();
    }

    public static <E> List<E> makeCollection(Iterable<E> iter, boolean shuffle) {

        List<E> list = new ArrayList<>();
        iter.forEach(list::add);
        if (shuffle)
            Collections.shuffle(list);

        return list;
    }

    public static <T> T selectRandom(RandomSource rand, T[] type) {

        return type[rand.nextInt(type.length)];
    }

    public static <T> T selectRandom(RandomSource rand, List<T> list) {

        return list.get(rand.nextInt(list.size()));
    }

    @SuppressWarnings("unchecked")
    public static <T> boolean hasTag(TagKey<?> tagKey, T tag) {

        if (tag instanceof Block block) {
            return block.builtInRegistryHolder().is((TagKey<Block>) tagKey);
        }
        if (tag instanceof Item item) {
            return item.builtInRegistryHolder().is((TagKey<Item>) tagKey);
        }
        return false;
    }

}
