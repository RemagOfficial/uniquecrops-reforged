package com.remag.uniquecrops.render;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.remag.uniquecrops.mixin.AccessorBS;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import java.util.LinkedHashMap;
import java.util.SequencedMap;

public class CustomBufferSource extends MultiBufferSource.BufferSource {

    protected CustomBufferSource(ByteBufferBuilder fallback, SequencedMap<RenderType, ByteBufferBuilder> layerbuffers) {

        super(fallback, layerbuffers);
    }

    public VertexConsumer getBuffer(RenderType type) {

        return super.getBuffer(CustomRenderType.remap(type));
    }

    public static MultiBufferSource.BufferSource initBuffers(MultiBufferSource.BufferSource original) {

        ByteBufferBuilder fallback = ((AccessorBS)original).getFallbackBuffer();
        SequencedMap<RenderType, ByteBufferBuilder> layerBuffers = ((AccessorBS)original).getFixedBuffers();
        SequencedMap<RenderType, ByteBufferBuilder> remapped = new LinkedHashMap<>();
        for (var e : layerBuffers.entrySet())
            remapped.put(CustomRenderType.remap(e.getKey()), e.getValue());

        return new CustomBufferSource(fallback, remapped);
    }
}
