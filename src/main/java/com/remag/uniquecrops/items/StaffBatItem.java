package com.remag.uniquecrops.items;

import com.remag.uniquecrops.core.UCStrings;
import com.remag.uniquecrops.core.enums.EnumParticle;
import com.remag.uniquecrops.items.base.ItemBaseUC;
import com.remag.uniquecrops.network.PacketUCEffect;
import com.remag.uniquecrops.network.UCPacketHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StaffBatItem extends ItemBaseUC {

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        tooltipComponents.add(Component.translatable(UCStrings.TOOLTIP + "batstaff").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean isFoil(ItemStack stack) {

        return true;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        boolean damage = false;
        if (stack.getItem() == this) {
            BlockPos pos = player.blockPosition();
            List<? extends LivingEntity> entities = getEntityToErase(player.level(), pos);
            for (LivingEntity ent : entities) {
                if (ent != entities && !world.isClientSide) {
                    UCPacketHandler.sendToNearbyPlayers(world, pos, new PacketUCEffect(EnumParticle.WITCH, ent.getX(), ent.getY(), ent.getZ(), 4));
                    ent.discard();
                    damage = true;
                }
            }
            if (damage && player instanceof ServerPlayer serverPlayer)
                stack.hurtAndBreak(1, serverPlayer, player.getEquipmentSlotForItem(stack));
        }
        return damage ? InteractionResultHolder.success(player.getItemInHand(hand)) : InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    protected List<? extends LivingEntity> getEntityToErase(Level world, BlockPos pos) {

        return world.getEntitiesOfClass(Bat.class, new AABB(pos).inflate(15));
    }
}
