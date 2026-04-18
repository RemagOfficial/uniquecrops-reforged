package com.remag.uniquecrops.items.curios;

import com.remag.uniquecrops.core.DyeUtils;
import com.remag.uniquecrops.items.base.ItemCurioUC;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class EmblemRainbow extends ItemCurioUC {

    public EmblemRainbow() {

        NeoForge.EVENT_BUS.addListener(this::onSheared);
    }

    private void onSheared(PlayerInteractEvent.EntityInteractSpecific event) {

        if (!hasCurio(event.getEntity())) return;

        if (!(event.getTarget() instanceof Sheep sheep)) return;
        if (!(event.getItemStack().getItem() instanceof ShearsItem) || sheep.isSheared()) return;

        int fortune = EnchantmentHelper.getItemEnchantmentLevel(
                event.getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE),
                event.getItemStack());
        if (!event.getLevel().isClientSide) {
            sheep.shear(SoundSource.PLAYERS);
            int drops = 1 + event.getLevel().random.nextInt(2 + Math.max(fortune, 0));
            for (int i = 0; i < drops; i++) {
                ItemStack stack = new ItemStack(DyeUtils.WOOL_BY_COLOR.get(DyeColor.byId(event.getLevel().random.nextInt(15))));
                event.getLevel().addFreshEntity(new ItemEntity(event.getLevel(), event.getTarget().getX(), event.getTarget().getY(), event.getTarget().getZ(), stack));
                if (event.getEntity() instanceof ServerPlayer serverPlayer)
                    event.getItemStack().hurtAndBreak(1, serverPlayer, serverPlayer.getEquipmentSlotForItem(event.getItemStack()));
            }
        }
    }
}
