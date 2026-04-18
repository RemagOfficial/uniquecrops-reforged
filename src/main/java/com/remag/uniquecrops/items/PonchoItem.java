package com.remag.uniquecrops.items;

import com.remag.uniquecrops.api.IBookUpgradeable;
import com.remag.uniquecrops.core.enums.EnumArmorMaterial;
import com.remag.uniquecrops.init.UCPotions;
import com.remag.uniquecrops.items.base.ItemArmorUC;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

public class PonchoItem extends ItemArmorUC implements IBookUpgradeable {

    public PonchoItem() {

        super(EnumArmorMaterial.PONCHO, Type.CHESTPLATE);
        NeoForge.EVENT_BUS.addListener(this::checkSetTarget);
    }

    private void checkSetTarget(LivingChangeTargetEvent event) {

        if (event.getNewAboutToBeSetTarget() == null) return;
        if (!(event.getNewAboutToBeSetTarget() instanceof Player player) || event.getNewAboutToBeSetTarget() instanceof FakePlayer) return;
        if (!(event.getEntity() instanceof Mob ent)) return;

        if (player.getEffect(UCPotions.IGNORANCE) != null) {
            ent.setTarget(null);
            ent.setLastHurtByMob(null);
            event.setCanceled(true);
            return;
        }
        boolean flag = player.getInventory().armor.get(2).getItem() == this && this.isMaxLevel(player.getInventory().armor.get(2));
        if (flag && ent.isPickable() && !(ent instanceof Guardian || ent instanceof Shulker)) {
            ent.setTarget(null);
            ent.setLastHurtByMob(null);
            event.setCanceled(true);
        }
    }
}
