package com.remag.uniquecrops.blocks.crops;

import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class Precision extends BaseCropsBlock {

    public Precision() {

        super(UCItems.PRENUGGET, UCItems.PRECISION_SEED);
        this.setBonemealable(false);
        this.setIncludeSeed(false);
    }

    @Override
    public int getHarvestAge() {

        return 6;
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if (this.getAge(state) != 6) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (!world.isClientSide) {
            int fortune = EnchantmentHelper.getItemEnchantmentLevel(
                    world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE),
                    stack
            );
            harvestItems(world, pos, state, fortune);
            world.setBlock(pos, this.setValueAge(0), 3);
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {

        if (!worldIn.isAreaLoaded(pos, 1)) return; // Prevent loading unloaded chunks
        if (worldIn.getRawBrightness(pos, 0) >= 9 && worldIn.getRawBrightness(pos.above(), 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getGrowthChance(this, worldIn, pos);
                if (random.nextInt((int)(25.0F / f) + 1) == 0) {
                    worldIn.setBlock(pos, this.setValueAge(i + 1), 3); // flag set to 3 instead of 2 to cause redstone updates
                }
            }
        }
    }

    @Override
    public int getSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {

        if (this.getAge(state) != 6)
            return 0;

        return 15;
    }

    @Override
    public boolean isSignalSource(BlockState state) {

        return this.getAge(state) == 6;
    }
}
