package com.remag.uniquecrops.api;

import com.remag.uniquecrops.core.UCDataUtils;
import com.remag.uniquecrops.core.UCStrings;
import net.minecraft.world.item.ItemStack;

public interface IBookUpgradeable {

    default int getLevel(ItemStack stack) {

        if (UCDataUtils.detectNBT(stack) && UCDataUtils.getNBT(stack).contains(UCStrings.TAG_UPGRADE))
            return UCDataUtils.getInt(stack, UCStrings.TAG_UPGRADE, -1);

        return -1;
    }

    default void setLevel(ItemStack stack, int level) {

        UCDataUtils.setInt(stack, UCStrings.TAG_UPGRADE, level);
    }

    default boolean isMaxLevel(ItemStack stack) {

        return getLevel(stack) >= 10;
    }
}
