package com.remag.uniquecrops.items;

import com.remag.uniquecrops.core.UCStrings;
import com.remag.uniquecrops.core.enums.TierItem;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PrecisionHammerItem extends Item implements IItemExtension {

    public PrecisionHammerItem() {
        super(UCItems.unstackable().durability(1751));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable(UCStrings.TOOLTIP + "precisionhammer"));
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level world, @NotNull Player player) {
        stack.enchant(world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH), 1);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        return ItemAttributeModifiers.builder()
            .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                BuiltInRegistries.ITEM.getKey(this),
                3.0F,
                AttributeModifier.Operation.ADD_VALUE
            ), EquipmentSlotGroup.MAINHAND)
            .add(Attributes.ATTACK_SPEED, new AttributeModifier(
                BuiltInRegistries.ITEM.getKey(this),
                -2.8F,
                AttributeModifier.Operation.ADD_VALUE
            ), EquipmentSlotGroup.MAINHAND)
            .build();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        // Custom destroy speed - mines all blocks at pickaxe speed
        return TierItem.PRECISION.getSpeed();
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        // Can mine any block that requires a tool
        return true;
    }
}
