package com.remag.uniquecrops.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class PacketChangeBiome implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("uniquecrops", "change_biome");
    public static final CustomPacketPayload.Type<PacketChangeBiome> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketChangeBiome> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public PacketChangeBiome decode(RegistryFriendlyByteBuf buf) {
            BlockPos pos = new BlockPos(buf.readInt(), 0, buf.readInt());
            ResourceLocation biomeId = buf.readResourceLocation();
            return new PacketChangeBiome(pos, biomeId);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PacketChangeBiome value) {
            buf.writeInt(value.pos.getX());
            buf.writeInt(value.pos.getZ());
            buf.writeResourceLocation(value.biomeId);
        }
    };

    private final BlockPos pos;
    private final ResourceLocation biomeId;

    public PacketChangeBiome(BlockPos pos, ResourceLocation biomeId) {
        this.pos = pos;
        this.biomeId = biomeId;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketChangeBiome msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if (world == null) {
                return;
            }
            LevelChunk chunkAt = (LevelChunk) world.getChunk(msg.pos);

            Holder<Biome> biome = world.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(ResourceKey.create(Registries.BIOME, msg.biomeId));

            int minY = QuartPos.fromBlock(world.getMinBuildHeight());
            int maxY = minY + QuartPos.fromBlock(world.getHeight()) - 1;

            int x = QuartPos.fromBlock(msg.pos.getX());
            int z = QuartPos.fromBlock(msg.pos.getZ());

            for (LevelChunkSection section : chunkAt.getSections()) {
                for (int sy = 0; sy < 16; sy += 4) {
                    int y = Mth.clamp(QuartPos.fromBlock(chunkAt.getMinSection() + sy), minY, maxY);
                    if (section.getBiomes() instanceof PalettedContainer<Holder<Biome>> container)
                        container.set(x & 3, y & 3, z & 3, biome);
                    SectionPos pos = SectionPos.of(msg.pos.getX() >> 4, (chunkAt.getMinSection() >> 4) + sy, msg.pos.getZ() >> 4);
                    world.setSectionDirtyWithNeighbors(pos.x(), pos.y(), pos.z());
                }
            }
            world.onChunkLoaded(new ChunkPos(msg.pos));
        });
    }
}
