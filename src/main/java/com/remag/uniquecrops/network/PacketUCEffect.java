package com.remag.uniquecrops.network;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.core.enums.EnumParticle;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class PacketUCEffect implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("uniquecrops", "uc_effect");
    public static final CustomPacketPayload.Type<PacketUCEffect> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketUCEffect> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public PacketUCEffect decode(RegistryFriendlyByteBuf buf) {
            EnumParticle type = EnumParticle.values()[buf.readShort()];
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            int loopSize = buf.readInt();
            return new PacketUCEffect(type, x, y, z, loopSize);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PacketUCEffect value) {
            buf.writeShort(value.type.ordinal());
            buf.writeDouble(value.x);
            buf.writeDouble(value.y);
            buf.writeDouble(value.z);
            buf.writeInt(value.loopSize);
        }
    };

    private final EnumParticle type;
    private final double x, y, z;
    private final int loopSize;

    public PacketUCEffect(EnumParticle type, double x, double y, double z, int loopSize) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.loopSize = loopSize;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketUCEffect msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = UniqueCrops.proxy.getPlayer();
            if (player == null) {
                return;
            }
            if (msg.loopSize > 0)
                for (int i = 0; i < msg.loopSize; i++)
                    player.level().addParticle(msg.type.getType(), (msg.x + 0.5D) + player.level().random.nextFloat(), msg.y, (msg.z + 0.5D) + player.level().random.nextFloat(), 0, 0, 0);
            else
                player.level().addParticle(msg.type.getType(), msg.x + 0.5D, msg.y, msg.z + 0.5D, 0, 0, 0);
        });
    }
}
