package com.remag.uniquecrops.items.curios;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.core.UCStrings;
import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.items.base.ItemCurioUC;
import com.google.common.collect.Lists;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import java.util.List;

public class EmblemScarab extends ItemCurioUC {

    private static final List<String> BLACKLIST = Lists.newArrayList();

    public EmblemScarab() {

        // Keep local blacklist initialization direct; this class no longer self-sends IMC.
        blacklistPotionEffect("minecraft.effect.awkward");
        blacklistPotionEffect("effect.uniquecrops.zombification");
        NeoForge.EVENT_BUS.addListener(this::onApplyPotion);
    }

    private void onApplyPotion(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();

        if (entity instanceof Player player) {
            MobEffectInstance effectInstance = event.getEffectInstance();
            var effect = effectInstance.getEffect();

            // Block all effects if Curio is equipped and not in blacklist
            if (hasCurio(player)) {
                if (!BLACKLIST.contains(effect.value().getDescriptionId())) {
                    event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
                    return;
                }
            }

            // Special case for Hunger
            if (effect == MobEffects.HUNGER) {
                if (hasCurio(player, UCItems.EMBLEM_IRONSTOMACH.get())) {
                    event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
                }
            }
        }
    }

    public static void blacklistPotionEffect(String effect) {

        BLACKLIST.add(effect);
    }
}
