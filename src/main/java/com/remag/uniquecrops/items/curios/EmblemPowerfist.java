package com.remag.uniquecrops.items.curios;

import com.remag.uniquecrops.items.base.ItemCurioUC;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class EmblemPowerfist extends ItemCurioUC {

    public EmblemPowerfist() {

        NeoForge.EVENT_BUS.addListener(this::fistingSpeed);
    }

    private void fistingSpeed(PlayerEvent.BreakSpeed event) {

        if (hasCurio(event.getEntity())) {
            ItemStack miningHand = event.getEntity().getMainHandItem();
            if (!miningHand.isEmpty()) return;

            if (event.getNewSpeed() < 8.0F)
                event.setNewSpeed(8.0F);
        }
    }
}
