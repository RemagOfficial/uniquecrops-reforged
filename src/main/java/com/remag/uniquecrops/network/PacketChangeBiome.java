package com.remag.uniquecrops.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketChangeBiome {

    private final BlockPos pos;
    private final ResourceLocation biomeId;

    public PacketChangeBiome(BlockPos pos, ResourceLocation id) {

        this.pos = pos;
        this.biomeId = id;
    }

    public void encode(FriendlyByteBuf buf) {

        buf.writeInt(pos.getX());
        buf.writeInt(pos.getZ());
        buf.writeResourceLocation(biomeId);
    }

    public static PacketChangeBiome decode(FriendlyByteBuf buf) {

        BlockPos pos = new BlockPos(buf.readInt(), 0, buf.readInt());
        ResourceLocation biomeId = buf.readResourceLocation();

        return new PacketChangeBiome(pos, biomeId);
    }

    public static void handle(PacketChangeBiome msg, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(new Runnable() {
           // This whole function copied from Ars Nouveau. Open Source rulez.
            @Override
            public void run() {
                ClientLevel world = Minecraft.getInstance().level;
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
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
