package com.remag.uniquecrops.network;

import com.remag.uniquecrops.core.UCDataUtils;
import com.remag.uniquecrops.items.GlassesPixelItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class PacketSendKey implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("uniquecrops", "send_key");
    public static final CustomPacketPayload.Type<PacketSendKey> TYPE = new CustomPacketPayload.Type<>(ID);
    public static final PacketSendKey INSTANCE = new PacketSendKey();
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketSendKey> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private PacketSendKey() {}

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketSendKey msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player == null) {
                return;
            }
            ItemStack glasses = player.getInventory().armor.get(3);
            if (glasses.getItem() instanceof GlassesPixelItem)
                UCDataUtils.setBoolean(glasses, "isActive", !UCDataUtils.getBoolean(glasses, "isActive", false));
        });
    }
}
