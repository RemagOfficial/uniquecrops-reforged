package com.remag.uniquecrops.items;

import com.remag.uniquecrops.core.NBTUtils;
import com.remag.uniquecrops.core.UCStrings;
import com.remag.uniquecrops.core.UCUtils;
import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.items.base.ItemBaseUC;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class PixelBrushItem extends ItemBaseUC {

    public PixelBrushItem() {

        super(UCItems.defaultBuilder().durability(131));
    }

    /*@Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {

        if (allowdedIn(tab)) {
            ItemStack brush = new ItemStack(this);
            items.add(brush.copy());
            brush.setDamageValue(brush.getMaxDamage());
            items.add(brush);
        }
    }

    private boolean allowdedIn(CreativeModeTab tab) {
        return true;
    }*/

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (NBTUtils.verifyExistance(stack, UCStrings.TAG_BIOME)) {
            ResourceLocation biomeId = ResourceLocation.tryParse(NBTUtils.getString(stack, UCStrings.TAG_BIOME, ""));
            if (biomeId == null) {
                tooltipComponents.add(Component.literal(ChatFormatting.GREEN + "Biome: " + ChatFormatting.RESET + "<NONE>"));
                return;
            }

            Biome biome = Objects.requireNonNull(context.level()).registryAccess().registryOrThrow(Registries.BIOME).get(biomeId);
            ResourceLocation rl = biome == null ? null : Objects.requireNonNull(context.level()).registryAccess().registryOrThrow(Registries.BIOME).getKey(biome);
            if (rl == null || rl.getPath().isEmpty()) {
                tooltipComponents.add(Component.literal(ChatFormatting.GREEN + "Biome: " + ChatFormatting.RESET + biomeId));
            } else {
                tooltipComponents.add(Component.literal(ChatFormatting.GREEN + "Biome: " + ChatFormatting.RESET + rl.getPath()));
            }
        } else {
            tooltipComponents.add(Component.literal(ChatFormatting.GREEN + "Biome: " + ChatFormatting.RESET + "<NONE>"));
        }
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext ctx) {

        if (ctx.getItemInHand().getDamageValue() == ctx.getItemInHand().getMaxDamage()) return InteractionResult.PASS;
        if (!NBTUtils.verifyExistance(ctx.getItemInHand(), UCStrings.TAG_BIOME)) return InteractionResult.PASS;

        ResourceLocation biomeId = ResourceLocation.tryParse(NBTUtils.getString(ctx.getItemInHand(), UCStrings.TAG_BIOME, ""));
        if (biomeId == null) return InteractionResult.PASS;
        boolean flag = UCUtils.setBiome(biomeId, ctx.getLevel(), ctx.getClickedPos());
        if (!flag) return InteractionResult.PASS;
        if (!ctx.getLevel().isClientSide() && ctx.getPlayer() instanceof ServerPlayer serverPlayer) {
            ItemStack usedStack = ctx.getItemInHand();
            usedStack.hurtAndBreak(1, serverPlayer, serverPlayer.getEquipmentSlotForItem(usedStack));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level world, @NotNull Player player) {

        stack.setDamageValue(stack.getMaxDamage());
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {

        return false;
    }
}
