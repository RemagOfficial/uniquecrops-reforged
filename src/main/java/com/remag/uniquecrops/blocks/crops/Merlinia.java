package com.remag.uniquecrops.blocks.crops;

import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class Merlinia extends BaseCropsBlock {

    public Merlinia() {

        super(UCItems.TIMEDUST, UCItems.MERLINIA_SEED);
        setClickHarvest(false);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if (this.getAge(state) == 0) {
            if (!world.isClientSide) {
                world.setBlock(pos, this.setValueAge(getMaxAge()), 3);
                int fortune = EnchantmentHelper.getItemEnchantmentLevel(
                    world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE),
                    stack
                );
                this.harvestItems(world, pos, state, fortune);
            }
            return ItemInteractionResult.SUCCESS;
        }
        return merliniaGrowth(stack, state, world, pos, player);
    }

    private ItemInteractionResult merliniaGrowth(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player) {

        ItemStack timemeal = stack;
        if (timemeal.getItem() == UCItems.TIMEMEAL.get()) {
            if (!world.isClientSide) {
                int i = Math.max(this.getAge(state) - this.getBonemealAgeIncrease(world), 0);
                world.setBlock(pos, this.setValueAge(i), 2);
                world.levelEvent(2005, pos, 0);
                if (!player.isCreative())
                    timemeal.shrink(1);
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {

        return this.setValueAge(getMaxAge());
    }
}
