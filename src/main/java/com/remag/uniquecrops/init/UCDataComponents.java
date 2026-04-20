package com.remag.uniquecrops.init;

import com.mojang.serialization.Codec;
import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.core.data.BlockPosData;
import com.remag.uniquecrops.core.data.CompoundTagData;
import com.remag.uniquecrops.core.data.ListTagData;
import com.remag.uniquecrops.core.data.UUIDData;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class UCDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(UniqueCrops.MOD_ID);

    // Book Upgradeable Items
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TAG_UPGRADE = register("tag_upgrade",
            builder -> builder.persistent(Codec.INT));

    // Knowledge Crop
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> BOOKMARK = register("bookmark",
            builder -> builder.persistent(Codec.BOOL));

    // Imperia Crop
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPosData>> IMPERIA_POS_TAG = register("imperia_pos_tag",
            builder -> builder.persistent(BlockPosData.CODEC));

    // Weatherflesia Crop & Pixel Brush
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> TAG_BIOME = register("tag_biome",
            builder -> builder.persistent(Codec.STRING));

    // Goblet Block
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUIDData>> TAG_LOCK = register("tag_lock",
            builder -> builder.persistent(UUIDData.CODEC));

    // Anvil Menu
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> TAG_DISCOUNT = register("tag_discount",
            builder -> builder.persistent(Codec.BOOL));

    // Crafty Plant Container
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CROP_POWER_CURRENT = register("crop_power_current",
            builder -> builder.persistent(Codec.INT));

    // Glasses Items
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> IS_ACTIVE = register("is_active",
            builder -> builder.persistent(Codec.BOOL));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPosData>> ORE_POS = register("ore_pos",
            builder -> builder.persistent(BlockPosData.CODEC));

    // Guide Book
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ListTagData>> TAG_GROWTHSTAGES = register("tag_growthstages",
            builder -> builder.persistent(ListTagData.CODEC));

    // Diamond Bunch
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TAG_DIAMONDS = register("tag_diamonds",
            builder -> builder.persistent(Codec.INT));

    // Energy Items
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> UC_ENERGY = register("uc_energy",
            builder -> builder.persistent(Codec.INT));

    // Thunderpantz
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> TAG_CHARGE = register("tag_charge",
            builder -> builder.persistent(Codec.FLOAT));

    // Rubiks Cube
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TAG_CUBE_ROTATION = register("tag_cube_rotation",
            builder -> builder.persistent(Codec.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTagData>> CUBE_ROTATION_DATA = register("cube_rotation_data",
            builder -> builder.persistent(CompoundTagData.CODEC));

    // Precision Pickaxe
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTagData>> SPAWNER_DATA = register("spawner_data",
            builder -> builder.persistent(CompoundTagData.CODEC));

    // Emblem Leaf
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ARMOR_COUNT = register("armor_count",
            builder -> builder.persistent(Codec.INT));

    // Brass Knuckles
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ListTagData>> HIT_LIST = register("hit_list",
            builder -> builder.persistent(ListTagData.CODEC));

    // Magnet
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ITEM_ACTIVATED = register("item_activated",
            builder -> builder.persistent(Codec.BOOL));

    // Impact Shield
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> DAMAGE_POOL = register("damage_pool",
            builder -> builder.persistent(Codec.FLOAT));

    // League Boots
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SPRINTING_TICKS = register("sprinting_ticks",
            builder -> builder.persistent(Codec.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> SPEED_MODIFIER = register("speed_modifier",
            builder -> builder.persistent(Codec.FLOAT));

    // Curio Items
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> TAG_CURIO_UUID_MOST = register("tag_curio_uuid_most",
            builder -> builder.persistent(Codec.LONG));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> TAG_CURIO_UUID_LEAST = register("tag_curio_uuid_least",
            builder -> builder.persistent(Codec.LONG));

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(net.neoforged.bus.api.IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
