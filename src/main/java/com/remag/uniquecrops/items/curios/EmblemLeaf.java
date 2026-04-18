package com.remag.uniquecrops.items.curios;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.remag.uniquecrops.core.NBTUtils;
import com.remag.uniquecrops.items.base.ItemCurioUC;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class EmblemLeaf extends ItemCurioUC {

    private static final String ARMORCOUNT = "UC:armorCount";

    @Override
    public void onEquip(SlotContext ctx, ItemStack prevStack, ItemStack stack) {

        int armorCount = 0;
        for (ItemStack armor : ((Player)ctx.entity()).getInventory().armor) {
            if (armor.getItem() instanceof ArmorItem) {
                if (((ArmorItem)armor.getItem()).getMaterial() != ArmorMaterials.LEATHER)
                    armorCount++;
            }
        }
        NBTUtils.setInt(stack, ARMORCOUNT, armorCount);
        ctx.entity().getAttributes().addTransientAttributeModifiers(getEquippedAttributeModifiers(stack));
    }

    @Override
    public void onUnequip(SlotContext ctx, ItemStack newStack, ItemStack stack) {

        ctx.entity().getAttributes().removeAttributeModifiers(getEquippedAttributeModifiers(stack));
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {

        int armorCount = NBTUtils.getInt(stack, ARMORCOUNT, 0);
        Multimap<Holder<Attribute>, AttributeModifier> attributes = HashMultimap.create();
        attributes.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(BuiltInRegistries.ITEM.getKey(stack.getItem()), armorCount, AttributeModifier.Operation.ADD_VALUE));
        return attributes;
    }
}
