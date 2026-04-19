package com.remag.uniquecrops.items;

import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.items.base.ItemBaseUC;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import org.jetbrains.annotations.NotNull;

public class OldBow extends ItemBaseUC {

    public OldBow() {

        super(UCItems.unstackable());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(this) && (player.getInventory().contains(new ItemStack(Items.ARROW)) || player.isCreative())) {
            if (!world.isClientSide()) {
                int charge = 15;
                ItemStack arrowItem = player.getInventory().items.stream().filter(arr -> arr.is(Items.ARROW)).findFirst().orElse(ItemStack.EMPTY);
                if (arrowItem.isEmpty() && !player.isCreative()) return InteractionResultHolder.fail(stack);
                if (arrowItem.isEmpty()) arrowItem = new ItemStack(Items.ARROW);
                float f;
                
                // Fire ArrowLooseEvent using NeoForge event bus
                boolean hasAmmo = player.isCreative() || player.getInventory().contains(new ItemStack(Items.ARROW));
                ArrowLooseEvent event = new ArrowLooseEvent(player, stack, world, charge, hasAmmo);
                NeoForge.EVENT_BUS.post(event);
                if (event.isCanceled()) {
                    return InteractionResultHolder.fail(stack);
                }
                
                charge = event.getCharge();
                f = BowItem.getPowerForTime(charge);
                AbstractArrow arrow = ((ArrowItem)Items.ARROW).createArrow(world, arrowItem, player, stack);
                if (player.isCreative())
                    arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);

                world.addFreshEntity(arrow);
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                if (!player.isCreative()) {
                    arrowItem.shrink(1);
                    if (arrowItem.isEmpty())
                        player.getInventory().removeItem(arrowItem);
                }
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {

        return false;
    }
}
