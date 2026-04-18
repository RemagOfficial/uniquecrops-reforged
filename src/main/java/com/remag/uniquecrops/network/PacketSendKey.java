package com.remag.uniquecrops.network;

import com.remag.uniquecrops.core.NBTUtils;
import com.remag.uniquecrops.items.GlassesPixelItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class PacketSendKey {

    public PacketSendKey() {}

    public static void encode(PacketSendKey msg, FriendlyByteBuf buf) {

    }

    public static PacketSendKey decode(FriendlyByteBuf buf) {

        return new PacketSendKey();
    }

    public static void handle(PacketSendKey msg, Supplier<UCPacketHandler.PacketContext> ctx) {

        ctx.get().enqueueWork(() -> {

            Player player = ctx.get().getSender();
            if (player == null) {
                return;
            }
            ItemStack glasses = player.getInventory().armor.get(3);
            if (glasses.getItem() instanceof GlassesPixelItem)
                NBTUtils.setBoolean(glasses, "isActive", !NBTUtils.getBoolean(glasses, "isActive", false));
        });
        ctx.get().setPacketHandled(true);
    }
}
