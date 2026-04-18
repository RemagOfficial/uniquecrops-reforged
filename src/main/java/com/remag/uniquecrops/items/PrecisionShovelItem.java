package com.remag.uniquecrops.items;

import com.remag.uniquecrops.api.IBookUpgradeable;
import com.remag.uniquecrops.core.enums.TierItem;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PrecisionShovelItem extends ShovelItem implements IBookUpgradeable {

    private static final int RANGE = 5;

    public PrecisionShovelItem() {

        super(TierItem.PRECISION, UCItems.unstackable());
        NeoForge.EVENT_BUS.addListener(this::onBlockFall);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> list, @NotNull TooltipFlag flag) {

        if (stack.getItem() instanceof IBookUpgradeable) {
            if (((IBookUpgradeable)stack.getItem()).getLevel(stack) > -1)
                list.add(Component.literal(ChatFormatting.GOLD + "+" + ((IBookUpgradeable)stack.getItem()).getLevel(stack)));
            else
                list.add(Component.literal(ChatFormatting.GOLD + "Upgradeable"));
        }
    }

    public void onBlockFall(EntityJoinLevelEvent event) {

        if (event.getEntity() instanceof FallingBlockEntity) {
            FallingBlockEntity fbEntity = (FallingBlockEntity) event.getEntity();
            Player player = fbEntity.level().getNearestPlayer(fbEntity, RANGE);
            if (player != null && player.getMainHandItem().getItem() == this) {
                if (isMaxLevel(player.getMainHandItem())) {
                    Block fallingBlock = fbEntity.getBlockState().getBlock();
                    if (fallingBlock instanceof ConcretePowderBlock ||
                            fbEntity.getBlockState().is(Blocks.SAND) ||
                            fbEntity.getBlockState().is(Blocks.GRAVEL)) {
                        event.setCanceled(true);
                        fbEntity.level().setBlock(fbEntity.getStartPos(), fbEntity.getBlockState(), Block.UPDATE_NONE);
                        // This new block will immediately try to fall again. Need to find a setBlock() that
                        // doesn't call the block's onPlace() method.
                    }
                }
            }
        }
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level world, @NotNull Player player) {

        stack.enchant(world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH), 1);
    }
}
