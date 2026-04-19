package com.remag.uniquecrops.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class UCPacketHandler {

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        
        registrar.playBidirectional(
            PacketSyncCap.TYPE,
            PacketSyncCap.STREAM_CODEC,
            (msg, ctx) -> PacketSyncCap.handle(msg, ctx)
        );
        
        registrar.playBidirectional(
            PacketChangeBiome.TYPE,
            PacketChangeBiome.STREAM_CODEC,
            (msg, ctx) -> PacketChangeBiome.handle(msg, ctx)
        );
        
        registrar.playToServer(
            PacketSendKey.TYPE,
            PacketSendKey.STREAM_CODEC,
            (msg, ctx) -> PacketSendKey.handle(msg, ctx)
        );
        
        registrar.playToServer(
            PacketColorfulCube.TYPE,
            PacketColorfulCube.STREAM_CODEC,
            (msg, ctx) -> PacketColorfulCube.handle(msg, ctx)
        );
        
        registrar.playBidirectional(
            PacketUCEffect.TYPE,
            PacketUCEffect.STREAM_CODEC,
            (msg, ctx) -> PacketUCEffect.handle(msg, ctx)
        );
        
        registrar.playBidirectional(
            PacketOpenBook.TYPE,
            PacketOpenBook.STREAM_CODEC,
            (msg, ctx) -> PacketOpenBook.handle(msg, ctx)
        );
        
        registrar.playBidirectional(
            PacketOpenCube.TYPE,
            PacketOpenCube.STREAM_CODEC,
            (msg, ctx) -> PacketOpenCube.handle(msg, ctx)
        );
    }

    public static void sendToNearbyPlayers(Level world, BlockPos pos, CustomPacketPayload toSend) {
        if (world instanceof ServerLevel ws) {
            ws.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false)
                    .stream()
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
                    .forEach(p -> PacketDistributor.sendToPlayer(p, toSend));
        }
    }

    public static void sendTo(ServerPlayer playerMP, CustomPacketPayload toSend) {
        PacketDistributor.sendToPlayer(playerMP, toSend);
    }
    
    public static void sendToServer(CustomPacketPayload toSend) {
        PacketDistributor.sendToServer(toSend);
    }
}
