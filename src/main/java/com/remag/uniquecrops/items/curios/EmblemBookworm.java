package com.remag.uniquecrops.items.curios;

import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.items.base.ItemCurioUC;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class EmblemBookworm extends ItemCurioUC {

    public static boolean isEdible(Item item) {

        return item == Items.ENCHANTED_BOOK;
    }

    public static FoodProperties getFood(ItemStack stack) {

        var enchants = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        int hunger = 0;
        float saturation = 0.0F;
        float f = 0.25F;

        for (var enchant : enchants.entrySet()) {
            int lvl = enchant.getIntValue();
            if (lvl <= 0)
                continue;

            float sat = Math.max(f * lvl, f);
            if (sat > saturation)
                saturation = sat;
            hunger += lvl * 2;
        }

        return new FoodProperties.Builder().nutrition(hunger).saturationModifier(saturation).build();
    }

    public static boolean isEquipped(LivingEntity living) {

        return ((EmblemBookworm) UCItems.EMBLEM_BOOKWORM.get()).hasCurio(living);
    }
}
