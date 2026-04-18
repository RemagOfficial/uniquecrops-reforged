package com.remag.uniquecrops.items;

import com.remag.uniquecrops.core.UCStrings;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class StaffPhantomItem extends StaffBatItem {

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        tooltipComponents.add(Component.translatable(UCStrings.TOOLTIP + "phantomstaff").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public List<? extends LivingEntity> getEntityToErase(Level world, BlockPos pos) {

        return world.getEntitiesOfClass(Phantom.class, new AABB(pos).inflate(15));
    }
}
