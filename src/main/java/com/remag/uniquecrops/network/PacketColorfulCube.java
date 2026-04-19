package com.remag.uniquecrops.network;

import com.remag.uniquecrops.items.RubiksCubeItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class PacketColorfulCube implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("uniquecrops", "colorful_cube");
    public static final CustomPacketPayload.Type<PacketColorfulCube> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketColorfulCube> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public PacketColorfulCube decode(RegistryFriendlyByteBuf buf) {
            int rotation = buf.readInt();
            boolean teleport = buf.readBoolean();
            return new PacketColorfulCube(rotation, teleport);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PacketColorfulCube value) {
            buf.writeInt(value.rotation);
            buf.writeBoolean(value.teleport);
        }
    };

    private final int rotation;
    private final boolean teleport;

    public PacketColorfulCube(int rotation, boolean teleport) {
        this.rotation = rotation;
        this.teleport = teleport;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketColorfulCube msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player == null) {
                return;
            }
            ItemStack mainHand = player.getMainHandItem();
            if (mainHand.getItem() instanceof RubiksCubeItem cube) {
                cube.teleportToPosition(player, msg.rotation, msg.teleport);
            }
        });
    }
}
