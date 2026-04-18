package com.remag.uniquecrops.blocks.tiles;

import com.remag.uniquecrops.init.UCTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;

public class TileHarvestTrap extends BaseTileUC {

    boolean collectedSpirit;
    int spiritTime = 0;
    int baitPower = 0;

    static final int RANGE = 4;

    public TileHarvestTrap(BlockPos pos, BlockState state) {

        super(UCTiles.HARVESTTRAP.get(), pos, state);
    }

    public void tickServer() {

        if (!this.hasSpirit())
            return;

        if (!this.isCollected()) {
            spiritTime--;
            if (spiritTime <=0) {
                this.markBlockForUpdate();
            }
            return;
        }

        if (level.getGameTime() % 20 == 0) {
            tickCropGrowth();
            spiritTime--;
            if (spiritTime <= 0) {
                this.collectedSpirit = false;
                this.markBlockForUpdate();
            }
        }

    }

    public void tickCropGrowth() {

        if (level.isClientSide) return;

        Iterable<BlockPos> posList = BlockPos.betweenClosed(worldPosition.offset(-RANGE, 0, -RANGE), worldPosition.offset(RANGE, 1, RANGE));
        for (BlockPos loopPos : posList) {
            BlockState loopState = level.getBlockState(loopPos);
            if (loopState.getBlock() instanceof BonemealableBlock && ((BonemealableBlock) loopState.getBlock()).isValidBonemealTarget(level, loopPos, loopState)) {
                level.levelEvent(2005, loopPos, 0);
                ((BonemealableBlock) loopState.getBlock()).performBonemeal((ServerLevel) level, level.random, loopPos, loopState);
            }
        }
    }

    @Override
    public void writeCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {

        tag.putBoolean("UC:collectedSpirit", this.collectedSpirit);
        tag.putInt("UC:spiritTime", this.spiritTime);
    }

    @Override
    public void readCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {

        this.collectedSpirit = tag.getBoolean("UC:collectedSpirit");
        this.spiritTime = tag.getInt("UC:spiritTime");
    }

    public void setSpiritTime(int time) {

        this.spiritTime = time;
        this.markBlockForUpdate();
    }

    public void setCollected() {

        this.collectedSpirit = true;
        this.markBlockForUpdate();
    }

    public boolean hasSpirit() {

        return this.spiritTime > 0;
    }

    public boolean isCollected() {

        return this.collectedSpirit;
    }

    public int getBaitPower() {

        return this.baitPower;
    }

    public void setBaitPower(int power) {

        this.baitPower = power;
        this.markBlockForUpdate();
    }

    public float[] getSpiritColor() {

        return collectedSpirit ? new float[] { (float) Color.GREEN.getRed(), (float)Color.GREEN.getGreen(), (float)Color.GREEN.getBlue() } : new float[] { (float)Color.ORANGE.getRed(), (float)Color.ORANGE.getGreen(), (float)Color.ORANGE.getBlue() };
    }
}
