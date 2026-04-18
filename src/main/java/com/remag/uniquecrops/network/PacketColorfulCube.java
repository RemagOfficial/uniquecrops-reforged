package com.remag.uniquecrops.network;

import com.remag.uniquecrops.items.RubiksCubeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class PacketColorfulCube {

    private final int rotation;
    private final boolean teleport;

    public PacketColorfulCube(int rotation, boolean teleport) {

        this.rotation = rotation;
        this.teleport = teleport;
    }

    public static void encode(PacketColorfulCube msg, FriendlyByteBuf buf) {

        buf.writeInt(msg.rotation);
        buf.writeBoolean(msg.teleport);
    }

    public static PacketColorfulCube decode(FriendlyByteBuf buf) {

        int rotation = buf.readInt();
        boolean teleport = buf.readBoolean();
        return new PacketColorfulCube(rotation, teleport);
    }

    public static void handle(PacketColorfulCube msg, Supplier<UCPacketHandler.PacketContext> ctx) {

        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }
            ItemStack mainHand = player.getMainHandItem();
            if (mainHand.getItem() instanceof RubiksCubeItem cube) {
                cube.teleportToPosition(player, msg.rotation, msg.teleport);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
