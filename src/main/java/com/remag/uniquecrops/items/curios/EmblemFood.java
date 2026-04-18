package com.remag.uniquecrops.items.curios;

import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.items.base.ItemCurioUC;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class EmblemFood extends ItemCurioUC {

    public EmblemFood() {

        super(UCItems.unstackable().durability(50));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {

        if (!slotContext.entity().level().isClientSide && slotContext.entity() instanceof ServerPlayer playerMP) {
            int diff = 20 - playerMP.getFoodData().getFoodLevel();
            if (playerMP.getFoodData().needsFood() && diff >= 3) {
                playerMP.getFoodData().eat(6, 0.6F);
                stack.hurtAndBreak(1, playerMP, playerMP.getEquipmentSlotForItem(stack));
            }
        }
    }
}
