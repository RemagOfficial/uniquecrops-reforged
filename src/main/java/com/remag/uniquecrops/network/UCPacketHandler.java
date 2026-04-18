package com.remag.uniquecrops.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class UCPacketHandler {

    public static final LegacyChannel INSTANCE = new LegacyChannel();

    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(id++, PacketSyncCap.class, PacketSyncCap::encode, PacketSyncCap::decode, PacketSyncCap::handle);
        INSTANCE.registerMessage(id++, PacketChangeBiome.class, PacketChangeBiome::encode, PacketChangeBiome::decode, PacketChangeBiome::handle);
        INSTANCE.registerMessage(id++, PacketSendKey.class, PacketSendKey::encode, PacketSendKey::decode, PacketSendKey::handle);
        INSTANCE.registerMessage(id++, PacketColorfulCube.class, PacketColorfulCube::encode, PacketColorfulCube::decode, PacketColorfulCube::handle);
        INSTANCE.registerMessage(id++, PacketUCEffect.class, PacketUCEffect::encode, PacketUCEffect::decode, PacketUCEffect::handle);
        INSTANCE.registerMessage(id++, PacketOpenBook.class, PacketOpenBook::encode, PacketOpenBook::decode, PacketOpenBook::handle);
        INSTANCE.registerMessage(id++, PacketOpenCube.class, PacketOpenCube::encode, PacketOpenCube::decode, PacketOpenCube::handle);
    }

    public static void sendToNearbyPlayers(Level world, BlockPos pos, Object toSend) {

        if (world instanceof ServerLevel ws) {

            ws.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false)
                    .stream()
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
                    .forEach(p -> INSTANCE.send(p, toSend));
        }
    }

    public static void sendTo(ServerPlayer playerMP, Object toSend) {

        INSTANCE.send(playerMP, toSend);
    }

    /**
     * Compatibility shim while packet classes are migrated to NeoForge CustomPacketPayload.
     */
    public static final class LegacyChannel {

        public void sendToServer(Object msg) {
            // TODO: replace with PacketDistributor.sendToServer(payload) once packets are migrated.
        }

        public void send(Object target, Object msg) {
            // TODO: replace with PacketDistributor.sendToPlayer/sendToPlayersTrackingChunk.
        }

        public <MSG, CTX> void registerMessage(
                int id,
                Class<MSG> messageType,
                BiConsumer<MSG, FriendlyByteBuf> encoder,
                Function<FriendlyByteBuf, MSG> decoder,
                BiConsumer<MSG, Supplier<PacketContext>> handler) {
            // TODO: replace with RegisterPayloadHandlersEvent + PayloadRegistrar registration.
        }
    }

    /**
     * Minimal compatibility context for old packet handlers during staged migration.
     */
    public static final class PacketContext {

        private final @Nullable ServerPlayer sender;
        private final boolean clientSide;

        public PacketContext(@Nullable ServerPlayer sender, boolean clientSide) {
            this.sender = sender;
            this.clientSide = clientSide;
        }

        public void enqueueWork(Runnable task) {
            task.run();
        }

        public @Nullable ServerPlayer getSender() {
            return sender;
        }

        public boolean isClientSide() {
            return clientSide;
        }

        public void setPacketHandled(boolean handled) {
            // no-op in shim
        }
    }
}
