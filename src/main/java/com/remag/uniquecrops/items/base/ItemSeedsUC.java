package com.remag.uniquecrops.items.base;

import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemSeedsUC extends ItemNameBlockItem {

    public ItemSeedsUC(BaseCropsBlock block) {

        super(block, UCItems.defaultBuilder());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {

        boolean flag = Screen.hasShiftDown();

        if (flag) {
            list.add(Component.literal("Bonemealable: ").withStyle(ChatFormatting.GRAY).append(tf(getCrop().isBonemealable())));
            list.add(Component.literal("Right-click Harvest: ").withStyle(ChatFormatting.GRAY).append(tf(getCrop().isClickHarvest())));
            list.add(Component.literal("Can Ignore Restrictions: ").withStyle(ChatFormatting.GRAY).append(tf(getCrop().isIgnoreGrowthRestrictions())));
        } else
            list.add(Component.literal("<Press Shift>").withStyle(ChatFormatting.GRAY));
    }

    private BaseCropsBlock getCrop() {

        return (BaseCropsBlock)this.getBlock();
    }

    private Component tf(boolean flag) {

        return Component.literal(String.valueOf(flag)).withStyle(flag ? ChatFormatting.GREEN : ChatFormatting.RED);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext ctx) {

        return super.useOn(ctx);
    }
}
