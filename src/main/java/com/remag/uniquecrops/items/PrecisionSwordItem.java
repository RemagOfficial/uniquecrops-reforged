package com.remag.uniquecrops.items;

import com.remag.uniquecrops.api.IBookUpgradeable;
import com.remag.uniquecrops.core.enums.TierItem;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PrecisionSwordItem extends SwordItem implements IBookUpgradeable {

    public PrecisionSwordItem() {

        super(TierItem.PRECISION, UCItems.unstackable());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> list, @NotNull TooltipFlag flag) {

        if (stack.getItem() instanceof IBookUpgradeable) {
            if (((IBookUpgradeable)stack.getItem()).getLevel(stack) > -1)
                list.add(Component.literal(ChatFormatting.GOLD + "+" + ((IBookUpgradeable)stack.getItem()).getLevel(stack)));
            else
                list.add(Component.literal(ChatFormatting.GOLD + "Upgradeable"));
        }
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {

        if (!target.level().isClientSide && target.invulnerableTime > 0) {
            if (isMaxLevel(stack))
                target.invulnerableTime = 0;
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level world, @NotNull Player player) {

        stack.enchant(world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.LOOTING), 1);
    }
}
