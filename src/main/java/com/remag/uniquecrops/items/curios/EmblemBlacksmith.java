package com.remag.uniquecrops.items.curios;

import com.remag.uniquecrops.items.base.ItemCurioUC;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.AnvilRepairEvent;

public class EmblemBlacksmith extends ItemCurioUC {

    public EmblemBlacksmith() {

        NeoForge.EVENT_BUS.addListener(this::blacksmithAnvil);
    }

    private void blacksmithAnvil(AnvilRepairEvent event) {

        if (hasCurio(event.getEntity())) event.setBreakChance(0.0F);
    }
}
