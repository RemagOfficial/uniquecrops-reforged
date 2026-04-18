package com.remag.uniquecrops.items;

import com.remag.uniquecrops.api.IBookUpgradeable;
import com.remag.uniquecrops.core.enums.EnumArmorMaterial;
import com.remag.uniquecrops.items.base.ItemArmorUC;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class Glasses3DItem extends ItemArmorUC implements IBookUpgradeable {

    public Glasses3DItem() {

        super(EnumArmorMaterial.GLASSES_3D, Type.HELMET);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean isSelected) {

        if (!(entity instanceof Player player)) return;
        if (player.getItemBySlot(EquipmentSlot.HEAD) != stack) return;

        if (world.isClientSide) return;
        if (!isMaxLevel(stack)) return;
        if ((world.getGameTime() % 40) != 0) return;

        int light = world.getRawBrightness(player.blockPosition().offset(0, (int) player.getEyeHeight(), 0),
                                           world.getSkyDarken());
        if (light <= 3)
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 250));
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {

        return false;
    }
}
