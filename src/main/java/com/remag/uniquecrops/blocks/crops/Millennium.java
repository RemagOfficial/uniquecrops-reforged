package com.remag.uniquecrops.blocks.crops;

import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.blocks.tiles.TileMillennium;
import com.remag.uniquecrops.core.UCConfig;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class Millennium extends BaseCropsBlock implements EntityBlock {

    public Millennium() {

        super(UCItems.MILLENNIUMEYE, UCItems.MILLENNIUM_SEED);
        setBonemealable(false);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {

        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof TileMillennium mill) {
            if (!world.isClientSide && !isMaxAge(state)) {
                if (mill.isTimeEmpty()) {
                    mill.setTime();
                    return;
                }
                if (mill.calcTime() >= UCConfig.COMMON.millenniumTime.get()) {
                    float f = getGrowthChance(this, world, pos);
                    if (rand.nextInt((int)(250.0F / f) + 1) == 0) {
                        world.setBlock(pos, this.setValueAge(getAge(state) + 1), UPDATE_CLIENTS);
                        mill.setTime();
                    }
                }
            }
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

        return new TileMillennium(pos, state);
    }
}
