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

public class PacketOpenBook implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("uniquecrops", "open_book");
    public static final CustomPacketPayload.Type<PacketOpenBook> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketOpenBook> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public PacketOpenBook decode(RegistryFriendlyByteBuf buf) {
            int id = buf.readInt();
            return new PacketOpenBook(id);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PacketOpenBook value) {
            buf.writeInt(value.id);
        }
    };

    private final int id;

    public PacketOpenBook(int id) {
        this.id = id;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketOpenBook packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
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
}
