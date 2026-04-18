package com.remag.uniquecrops.items.curios;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.remag.uniquecrops.items.base.ItemCurioUC;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class EmblemMelee extends ItemCurioUC {

    @Override
    public void onEquip(SlotContext ctx, ItemStack prevStack, ItemStack stack) {

        ctx.entity().getAttributes().addTransientAttributeModifiers(getEquippedAttributeModifiers(stack));
    }

    @Override
    public void onUnequip(SlotContext ctx, ItemStack newStack, ItemStack stack) {

        ctx.entity().getAttributes().removeAttributeModifiers(getEquippedAttributeModifiers(stack));
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {

        Multimap<Holder<Attribute>, AttributeModifier> attributes = HashMultimap.create();
        attributes.put(Attributes.ATTACK_SPEED, new AttributeModifier(BuiltInRegistries.ITEM.getKey(stack.getItem()), 1, AttributeModifier.Operation.ADD_VALUE));
        return attributes;
    }
}
