package com.remag.uniquecrops.items;

import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.items.base.ItemBaseUC;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;

public class MultiblockBookItem extends ItemBaseUC {

    public MultiblockBookItem() {

        super(UCItems.unstackable().rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        tooltipComponents.add(getEdition().copy().withStyle(ChatFormatting.GOLD));
    }

    public static Component getEdition() {

        try {
            return PatchouliAPI.get().getSubtitle(BuiltInRegistries.ITEM.getKey(UCItems.BOOK_MULTIBLOCK.get()));
        } catch (IllegalArgumentException e) {
            return Component.literal("");
        }
//        return PatchouliAPI.get().getSubtitle(UCItems.BOOK_MULTIBLOCK.getId());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {

        ItemStack stack = player.getMainHandItem();

        if (player instanceof ServerPlayer sPlayer) {
            PatchouliAPI.get().openBookGUI(sPlayer, UCItems.BOOK_MULTIBLOCK.getId());
        }
        return InteractionResultHolder.success(stack);
    }
}
