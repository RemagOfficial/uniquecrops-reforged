package com.remag.ucse.blocks.crops;

import com.remag.ucse.blocks.BaseCropsBlock;
import com.remag.ucse.core.UCConfig;
import com.remag.ucse.init.UCItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.Random;

public class Cobblonia extends BaseCropsBlock {

    public Cobblonia() {
        super(() -> Item.byBlock(Blocks.COBBLESTONE), UCItems.COBBLONIA_SEED);
        setIgnoreGrowthRestrictions(true);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, world, pos, oldState, isMoving);
        if (!world.isClientSide) {
            world.scheduleTick(pos, this, UCConfig.COMMON.cobbloniaSpeed.get());
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
        if (this.isMaxAge(state)) {
            float skipChance = UCConfig.COMMON.cobbloniaNoGenChance.get() / 100f;

            if (rand.nextFloat() < skipChance) {
                world.scheduleTick(pos, this, UCConfig.COMMON.cobbloniaSpeed.get());
                return;
            }

            boolean flag = this.canIgnoreGrowthRestrictions(world, pos);
            cobbleGen(world, pos, flag);
        }

        world.scheduleTick(pos, this, UCConfig.COMMON.cobbloniaSpeed.get());
    }

    private void cobbleGen(ServerLevel world, BlockPos pos, boolean boost) {
        BlockPos north = pos.below().north();
        BlockPos south = pos.below().south();
        BlockPos east = pos.below().east();
        BlockPos west = pos.below().west();

        int cobblegen = 0;

        if (isFluidSource(world, north, FluidTags.WATER) && isFluidSource(world, south, FluidTags.LAVA)) cobblegen += 1;
        if (isFluidSource(world, north, FluidTags.LAVA) && isFluidSource(world, south, FluidTags.WATER)) cobblegen += 1;
        if (isFluidSource(world, west, FluidTags.WATER) && isFluidSource(world, east, FluidTags.LAVA)) cobblegen += 1;
        if (isFluidSource(world, west, FluidTags.LAVA) && isFluidSource(world, east, FluidTags.WATER)) cobblegen += 1;

        if (cobblegen > 0) {
            cobblegen = boost ? 64 : cobblegen;
            ItemStack toDrop = new ItemStack(Blocks.COBBLESTONE, cobblegen);
            Containers.dropItemStack(world,
                    pos.getX() + 0.5,
                    pos.getY() + 0.1,
                    pos.getZ() + 0.5,
                    toDrop);
        }
    }

    private boolean isFluidSource(Level world, BlockPos pos, TagKey<Fluid> tag) {
        return world.getFluidState(pos).is(tag) && world.getFluidState(pos).isSource();
    }
}
