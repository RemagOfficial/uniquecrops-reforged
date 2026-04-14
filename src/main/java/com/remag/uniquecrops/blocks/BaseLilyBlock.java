package com.remag.uniquecrops.blocks;

import com.remag.uniquecrops.core.enums.EnumLily;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.PlantType;

public class BaseLilyBlock extends WaterlilyBlock {

    final EnumLily lily;

    public BaseLilyBlock(EnumLily lilyEnum) {

        super(Properties.copy(Blocks.LILY_PAD));
        this.lily = lilyEnum;
    }

    @Override
    public PlantType getPlantType(BlockGetter world, BlockPos pos) {
        return PlantType.WATER;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        BlockPos posBelow = pos.below();
        return this.mayPlaceOn(reader.getBlockState(posBelow), reader, posBelow);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {

        if (world.isClientSide) return;
        lily.collide(state, world, pos, entity);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter reader, BlockPos pos) {

        return lily.isValidGround(state, reader, pos);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {

        if (rand.nextInt(2) == 0)
            world.addParticle(lily.getParticle(), pos.getX() + rand.nextFloat(), pos.getY(), pos.getZ() + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
    }
}
