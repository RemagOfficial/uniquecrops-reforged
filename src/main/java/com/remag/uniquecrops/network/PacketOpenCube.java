package com.remag.uniquecrops.network;

import com.remag.uniquecrops.UniqueCrops;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class PacketOpenCube implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("uniquecrops", "open_cube");
    public static final CustomPacketPayload.Type<PacketOpenCube> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketOpenCube> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public PacketOpenCube decode(RegistryFriendlyByteBuf buf) {
            int id = buf.readInt();
            return new PacketOpenCube(id);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PacketOpenCube value) {
            buf.writeInt(value.id);
        }
    };

    private final int id;

    public PacketOpenCube(int id) {
        this.id = id;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketOpenCube packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = UniqueCrops.proxy.getPlayer();
            if (player == null) {
                return;
            }
            Entity entity = player.level().getEntity(packet.id);
            if (entity instanceof Player && packet.id == player.getId()) {
                UniqueCrops.proxy.openCube();
            }
        });
    }
}
