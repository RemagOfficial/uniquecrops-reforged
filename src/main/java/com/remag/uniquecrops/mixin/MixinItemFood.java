package com.remag.uniquecrops.mixin;

import com.remag.uniquecrops.items.curios.EmblemBookworm;
import com.remag.uniquecrops.items.curios.EmblemIronStomach;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItemFood {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true, remap = false)
    private void use(Level world, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack stack = player.getItemInHand(hand);
        FoodProperties food = getCustomFood(stack);
        if (food == null) return;

        if (!canUseCustomFood((Item)(Object)this, player)) {
            cir.setReturnValue(InteractionResultHolder.pass(stack));
            return;
        }

        if (player.canEat(food.canAlwaysEat())) {
            player.startUsingItem(hand);
            cir.setReturnValue(InteractionResultHolder.consume(stack));
        } else {
            cir.setReturnValue(InteractionResultHolder.fail(stack));
        }
    }

    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true, remap = false)
    private void finishUsingItem(ItemStack stack, Level world, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        FoodProperties food = getCustomFood(stack);
        if (food == null) return;

        if (canUseCustomFood((Item)(Object)this, livingEntity)) {
            cir.setReturnValue(livingEntity.eat(world, stack, food));
        }
    }

    @Inject(method = "getUseAnimation", at = @At("HEAD"), cancellable = true, remap = false)
    private void getUseAnimation(ItemStack stack, CallbackInfoReturnable<UseAnim> cir) {
        if (getCustomFood(stack) != null) {
            cir.setReturnValue(UseAnim.EAT);
        }
    }

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true, remap = false)
    private void getUseDuration(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        FoodProperties food = getCustomFood(stack);
        if (food != null && canUseCustomFood((Item)(Object)this, entity)) {
            cir.setReturnValue(food.eatDurationTicks());
        }
    }

    private static boolean canUseCustomFood(Item item, LivingEntity livingEntity) {
        if (EmblemBookworm.isEdible(item)) {
            return EmblemBookworm.isEquipped(livingEntity);
        }
        if (EmblemIronStomach.containsTag(item)) {
            return EmblemIronStomach.isEquipped(livingEntity);
        }
        return false;
    }

    private static FoodProperties getCustomFood(ItemStack stack) {
        Item item = stack.getItem();
        if (EmblemBookworm.isEdible(item)) {
            return EmblemBookworm.getFood(stack);
        }
        if (EmblemIronStomach.containsTag(item)) {
            return EmblemIronStomach.getFood(item);
        }
        return null;
    }
}
