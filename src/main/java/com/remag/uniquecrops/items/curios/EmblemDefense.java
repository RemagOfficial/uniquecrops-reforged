package com.remag.uniquecrops.items.curios;

import com.remag.uniquecrops.items.base.ItemCurioUC;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import java.util.Objects;

public class EmblemDefense extends ItemCurioUC {

    public EmblemDefense() {

        NeoForge.EVENT_BUS.addListener(this::autoShield);
    }

    private void autoShield(AttackEntityEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer)) return;
        if (!(event.getTarget() instanceof Arrow)) return;
        if (!this.hasCurio(event.getEntity())) return;

        ItemStack shield = event.getEntity().getOffhandItem();
        if (!(shield.getItem() instanceof ShieldItem)) return;

        shield.hurtAndBreak(1, event.getEntity(), Objects.requireNonNull(event.getEntity().getOffhandItem().getEquipmentSlot()));
        event.setCanceled(true);
    }
}
