package com.remag.uniquecrops.potions;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class PotionEnnui extends MobEffect {

    public PotionEnnui() {

        super(MobEffectCategory.NEUTRAL, 0xeef442);
        NeoForge.EVENT_BUS.addListener(this::onPlayerHitBlock);
        NeoForge.EVENT_BUS.addListener(this::onPlayerClickBlock);
        NeoForge.EVENT_BUS.addListener(this::onPlayerClickItem);
        NeoForge.EVENT_BUS.addListener(this::onPlayerJump);
    }

    private void onPlayerJump(LivingEvent.LivingJumpEvent event) {

        if (hasEnnui(event.getEntity()))
            event.getEntity().setDeltaMovement(event.getEntity().getDeltaMovement().x, 0, event.getEntity().getDeltaMovement().z);
    }

    private void onPlayerClickBlock(PlayerInteractEvent.RightClickBlock event) {

        if (hasEnnui(event.getEntity()))
            event.setCanceled(true);
    }

    private void onPlayerClickItem(PlayerInteractEvent.RightClickItem event) {

        if (hasEnnui(event.getEntity()))
            event.setCanceled(true);
    }

    private void onPlayerHitBlock(PlayerInteractEvent.LeftClickBlock event) {

        if (hasEnnui(event.getEntity()))
            event.setCanceled(true);
    }

    private boolean hasEnnui(LivingEntity entity) {

        return entity.getActiveEffects().stream().anyMatch(effect -> effect.getEffect().value() == this);
    }
}
