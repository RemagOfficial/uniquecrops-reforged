package com.remag.uniquecrops.core.data;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;

public record CompoundTagData(CompoundTag tag) {
    public static final Codec<CompoundTagData> CODEC = CompoundTag.CODEC.xmap(CompoundTagData::new, CompoundTagData::tag);
}
