package com.remag.uniquecrops.items;

import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.items.base.ItemBaseUC;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class EnchantedLeatherItem extends ItemBaseUC {

    public EnchantedLeatherItem() {

        super(UCItems.unstackable().rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean isFoil(ItemStack stack) {

        return true;
    }
}
