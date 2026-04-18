package com.remag.uniquecrops.network;

import com.remag.uniquecrops.UniqueCrops;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class PacketOpenBook {

    final int id;

    public PacketOpenBook(int id) {

        this.id = id;
    }

    public static void encode(PacketOpenBook packet, FriendlyByteBuf buf) {

        buf.writeInt(packet.id);
    }

    public static PacketOpenBook decode(FriendlyByteBuf buf) {

        int id = buf.readInt();
        return new PacketOpenBook(id);
    }

    public static void handle(PacketOpenBook packet, Supplier<UCPacketHandler.PacketContext> ctx) {

        if (ctx.get().isClientSide()) {
            ctx.get().enqueueWork(() -> {
                Player player = UniqueCrops.proxy.getPlayer();
                if (player == null) {
                    return;
                }
                Entity entity = player.level().getEntity(packet.id);
                if (entity instanceof Player && packet.id == player.getId()) {
                    UniqueCrops.proxy.openBook();
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}
