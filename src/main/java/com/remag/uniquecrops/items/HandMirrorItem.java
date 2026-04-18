package com.remag.uniquecrops.items;

import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.items.base.ItemBaseUC;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class HandMirrorItem extends ItemBaseUC {

    public HandMirrorItem() {

        super(UCItems.unstackable());
        NeoForge.EVENT_BUS.addListener(this::reflectLazers);
    }


    private void reflectLazers(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getSource().getEntity() instanceof Guardian guardian)) return;

        ItemStack mirror = player.getOffhandItem();
        if (mirror.getItem() != this) return;

        float damage = event.getAmount();

        if (!player.level().isClientSide && player.level() instanceof ServerLevel serverLevel) {
            Holder<DamageType> magicDamageType = serverLevel.registryAccess()
                    .registryOrThrow(Registries.DAMAGE_TYPE)
                    .getHolderOrThrow(DamageTypes.MAGIC);

            // Reflect guardian laser damage back to the attacker.
            guardian.hurt(new DamageSource(magicDamageType, player), damage);

            // Cancel incoming damage to the player.
            event.setAmount(0F);
            event.setCanceled(true);

            if (player instanceof ServerPlayer serverPlayer) {
                mirror.hurtAndBreak(1, serverPlayer, serverPlayer.getEquipmentSlotForItem(mirror));
            }
        }
    }
}
