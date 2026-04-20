package com.remag.uniquecrops.core.data;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public record ListTagData(ListTag tag) {
    public static final Codec<ListTagData> CODEC = CompoundTag.CODEC.xmap(
            compound -> new ListTagData(compound.getList("data", 10)),
            data -> {
                CompoundTag compound = new CompoundTag();
                compound.put("data", data.tag);
                return compound;
            }
    );
}
