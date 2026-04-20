package com.remag.uniquecrops.core.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.UUID;

public record UUIDData(UUID uuid) {
    public static final Codec<UUIDData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.xmap(UUID::fromString, UUID::toString).fieldOf("uuid").forGetter(UUIDData::uuid)
    ).apply(instance, UUIDData::new));
}
