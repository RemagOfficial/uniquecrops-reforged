package com.remag.uniquecrops.blocks.tiles;

import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.core.UCConfig;
import com.remag.uniquecrops.init.UCTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class TileIndustria extends BaseTileUC implements IEnergyStorage {

    final UCEnergyStorage energy = new UCEnergyStorage(40000, 200);

    public TileIndustria(BlockPos pos, BlockState state) {

        super(UCTiles.INDUSTRIA.get(), pos, state);
    }

    public void tickServer() {

        if (!level.canSeeSkyFromBelowWater(worldPosition)) return;

        if (level.isDay()) {
            if (!energy.canReceive()) return;

            energy.receiveEnergy(UCConfig.COMMON.energyPerTick.get(), false);
            int age = energy.getEnergyStored() / 5000;
            if (Math.min(age, 7) != getBlockState().getValue(BaseCropsBlock.AGE))
                level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BaseCropsBlock.AGE, Math.min(age, 7)));
        }
    }

    @Override
    public void writeCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {

        tag.putInt("UC:energy", this.energy.getEnergyStored());
    }

    @Override
    public void readCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {

        energy.setEnergy(tag.getInt("UC:energy"));
    }

    // IEnergyStorage implementation delegates to internal energy storage
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return energy.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return energy.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return energy.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return energy.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return energy.canExtract();
    }

    @Override
    public boolean canReceive() {
        return energy.canReceive();
    }

    public static class UCEnergyStorage extends EnergyStorage {

        public UCEnergyStorage(int capacity, int maxTransfer) {

            super(capacity, maxTransfer);
        }

        public void setEnergy(int energy) {

            this.energy = energy;
        }
    }
}
