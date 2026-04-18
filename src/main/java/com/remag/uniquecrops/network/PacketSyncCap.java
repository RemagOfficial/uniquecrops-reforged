package com.remag.uniquecrops.network;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.api.ICropPower;
import com.remag.uniquecrops.capabilities.CPProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class PacketSyncCap {

    final CompoundTag tag;

    public PacketSyncCap(CompoundTag tag) {

        this.tag = tag;
    }

    public static void encode(PacketSyncCap msg, FriendlyByteBuf buf) {

        buf.writeNbt(msg.tag);
    }

    public static PacketSyncCap decode(FriendlyByteBuf buf) {

        return new PacketSyncCap(buf.readNbt());
    }

    public static void handle(PacketSyncCap msg, Supplier<UCPacketHandler.PacketContext> ctx) {

        if (ctx.get().isClientSide()) {
            ctx.get().enqueueWork(() -> {
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
        ctx.get().setPacketHandled(true);
    }
}
