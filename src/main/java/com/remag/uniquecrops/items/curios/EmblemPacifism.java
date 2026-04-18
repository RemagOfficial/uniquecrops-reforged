package com.remag.uniquecrops.items.curios;

import com.remag.uniquecrops.items.base.ItemCurioUC;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class EmblemPacifism extends ItemCurioUC {

    public EmblemPacifism() {

        NeoForge.EVENT_BUS.addListener(this::noDamage);
    }

    private void noDamage(LivingIncomingDamageEvent event) {

        Player victim = event.getEntity() instanceof Player p ? p : null;
        Player attacker = event.getSource().getDirectEntity() instanceof Player p ? p : null;

        boolean blockIncomingToCurioWearer = victim != null && attacker != null && hasCurio(victim);
        boolean blockOutgoingFromCurioWearer = attacker != null && hasCurio(attacker);

        if (blockIncomingToCurioWearer || blockOutgoingFromCurioWearer) {
            event.setAmount(0F);
            event.setCanceled(true);
        }
    }
}
