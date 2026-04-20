package com.remag.uniquecrops.core.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

public record BlockPosData(BlockPos pos) {
    public static final Codec<BlockPosData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(BlockPosData::pos)
    ).apply(instance, BlockPosData::new));
}
