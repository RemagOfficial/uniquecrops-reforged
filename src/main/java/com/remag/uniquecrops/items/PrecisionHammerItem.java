package com.remag.uniquecrops.items;

import com.remag.uniquecrops.core.UCStrings;
import com.remag.uniquecrops.core.enums.TierItem;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PrecisionHammerItem extends PickaxeItem {

    // Migration note: temporarily removed legacy behavior until NeoForge equivalents are selected.
    // 1) Forge ToolAction-based "mine anything" support.
    // 2) Custom mainhand attack-speed attribute map built on old AttributeModifier APIs.

    public PrecisionHammerItem() {

        super(TierItem.PRECISION, UCItems.unstackable());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> list, @NotNull TooltipFlag flag) {

        list.add(Component.translatable(UCStrings.TOOLTIP + "precisionhammer"));
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level world, @NotNull Player player) {

        stack.enchant(world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH), 1);
    }
}
