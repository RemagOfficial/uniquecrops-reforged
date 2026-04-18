package com.remag.uniquecrops.items.base;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.api.IBookUpgradeable;
import com.remag.uniquecrops.core.enums.EnumArmorMaterial;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemArmorUC extends ArmorItem {
    private final String textureName;

    public ItemArmorUC(EnumArmorMaterial armorMaterial, Type type) {
        super(armorMaterial.getMaterial(), type, UCItems.defaultBuilder().stacksTo(1));
        this.textureName = armorMaterial.getName();
    }

    /* public ItemArmorUC(ArmorMaterial material, EquipmentSlot slot) {
        super(material, slot, UCItems.defaultBuilder().stacksTo(1));
    } */

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag flag) {

        if (stack.getItem() instanceof IBookUpgradeable) {
            if (((IBookUpgradeable)stack.getItem()).getLevel(stack) > -1)
                list.add(Component.literal(ChatFormatting.GOLD + "+" + ((IBookUpgradeable)stack.getItem()).getLevel(stack)));
            else
                list.add(Component.literal(ChatFormatting.GOLD + "Upgradeable"));
        }
    }

    public String getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        return String.format("%s:textures/models/armor/%s_layer_%s.png", UniqueCrops.MOD_ID, textureName, isUpper(slot) ? "1" : "2");
    }

    private boolean isUpper(EquipmentSlot slot) {

        return slot == EquipmentSlot.CHEST || slot == EquipmentSlot.HEAD;
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level world, @NotNull Player player) {

        if (stack.getItem() == UCItems.CACTUS_BOOTS.get() || stack.getItem() == UCItems.CACTUS_CHESTPLATE.get() || stack.getItem() == UCItems.CACTUS_HELM.get() || stack.getItem() == UCItems.CACTUS_LEGGINGS.get())
            stack.enchant(player.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.THORNS), 1);
    }

    @Override
    public boolean makesPiglinsNeutral(@NotNull ItemStack stack, @NotNull LivingEntity wearer) {

        if (stack.getItem() == UCItems.GLASSES_3D.get() || stack.getItem() == UCItems.GLASSES_PIXELS.get() || stack.getItem() == UCItems.THUNDERPANTZ.get())
            return true;

        return super.makesPiglinsNeutral(stack, wearer);
    }
}
