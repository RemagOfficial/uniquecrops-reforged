package com.remag.uniquecrops.items;

import com.remag.uniquecrops.api.ICropPower;
import com.remag.uniquecrops.capabilities.CPProvider;
import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.items.base.ItemBaseUC;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ImpregnatedLeatherItem extends ItemBaseUC {

    public ImpregnatedLeatherItem() {

        super(UCItems.unstackable().rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        boolean flag = Screen.hasShiftDown();
        ICropPower crop = stack.getCapability(CPProvider.CROP_POWER, null);
        if (crop != null) {
            if (flag)
                tooltipComponents.add(Component.literal(ChatFormatting.GREEN + "Crop Power: " + crop.getPower() + "/" + crop.getCapacity()));
        }
        if (!flag)
            tooltipComponents.add(Component.literal(ChatFormatting.LIGHT_PURPLE + "<Press shift>"));
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean isSelected) {

        if (!(entity instanceof Player)) return;

        ICropPower crop = stack.getCapability(CPProvider.CROP_POWER, null);
        if (crop != null) {
            if (!world.isClientSide && crop.getPower() >= crop.getCapacity()) {
                stack.shrink(1);
                ItemHandlerHelper.giveItemToPlayer((Player)entity, new ItemStack(UCItems.ENCHANTED_LEATHER.get()));
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(@NotNull ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {

        return !ItemStack.isSameItem(oldStack, newStack);
    }
}
