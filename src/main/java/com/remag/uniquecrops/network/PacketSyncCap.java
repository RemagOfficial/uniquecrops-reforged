package com.remag.uniquecrops.network;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.api.ICropPower;
import com.remag.uniquecrops.capabilities.CPProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class PacketSyncCap implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("uniquecrops", "sync_cap");
    public static final CustomPacketPayload.Type<PacketSyncCap> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketSyncCap> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public PacketSyncCap decode(RegistryFriendlyByteBuf buf) {
            return new PacketSyncCap(buf.readNbt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PacketSyncCap value) {
            buf.writeNbt(value.tag);
        }
    };

    private final CompoundTag tag;

    public PacketSyncCap(CompoundTag tag) {
        this.tag = tag;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketSyncCap msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = UniqueCrops.proxy.getPlayer();
            if (player == null) {
                return;
            }

            ICropPower crop = player.getMainHandItem().getCapability(CPProvider.CROP_POWER, null);
            if (crop == null) {
                return;
            }

            crop.deserializeNBT(msg.tag);
        });
    }
}
