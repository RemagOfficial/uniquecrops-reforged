package com.remag.uniquecrops.blocks.crops;

import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class MaryJane extends BaseCropsBlock {

    public MaryJane() {

        super(UCItems.CINDERLEAF, UCItems.MARYJANE_SEED);
        setClickHarvest(false);
        setBonemealable(false);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {

        if (world.dimensionType().ultraWarm())
            super.randomTick(state, world, pos, rand);
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, BlockEntity tile, ItemStack stack) {

        if (isMaxAge(state)) {
            if (player == null) {
                super.playerDestroy(world, player, pos, state, tile, stack);
                return;
            }
            if (!player.isCreative() && !player.isOnFire()) {
                world.setBlock(pos.below(), Blocks.DIRT.defaultBlockState(), 2);
                world.setBlock(pos, Blocks.FIRE.defaultBlockState(), 2);
            } else {
                Containers.dropItemStack(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, new ItemStack(UCItems.CINDERLEAF.get()));
                for (int i=0; i<2; ++i)
                    if (world.random.nextFloat()<0.5)
                        Containers.dropItemStack(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, new ItemStack(UCItems.MARYJANE_SEED.get()));
            }
        }
        super.playerDestroy(world, player, pos, state, tile, stack);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (isMaxAge(state) || !world.dimensionType().ultraWarm())
            return ItemInteractionResult.FAIL;

        if (stack.getItem() == Items.BLAZE_POWDER) {
            if (!world.isClientSide) {
                this.performBonemeal((ServerLevel)world, world.random, pos, state);
                player.getItemInHand(hand).shrink(1);
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    /*
    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos blockPos, BlockState blockState) {

        return world.dimensionType().ultraWarm();
    }
    */

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {

        if (isMaxAge(state))
            world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.85, pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
    }
}
