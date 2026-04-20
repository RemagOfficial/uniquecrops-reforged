package com.remag.uniquecrops.capabilities;

import com.remag.uniquecrops.core.UCDataUtils;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.EnergyStorage;

public class UCEnergy extends EnergyStorage {

    private final ItemStack stack;

    public UCEnergy(ItemStack stack, int capacity, int energy) {

        super(capacity, 100, 100, energy);
        this.stack = stack;
    }

    @Override
    public int receiveEnergy(int receive, boolean sim) {

        if (!this.canReceive()) return 0;

        int energystored = this.getEnergyStored();
        int energyreceived = Math.min(capacity - energystored, Math.min(this.maxReceive, receive));
        if (!sim)
            this.setEnergyStored(energystored + energyreceived);

        return energystored;
    }

    @Override
    public int extractEnergy(int extract, boolean sim) {

        if (!this.canExtract()) return 0;

        int energystored = this.getEnergyStored();
        int energyextracted = Math.min(energystored, Math.min(this.maxExtract, extract));
        if (!sim)
            this.setEnergyStored(energystored - energyextracted);
        return energyextracted;
    }

    @Override
    public int getEnergyStored() {

        int legacy = UCDataUtils.getInt(this.stack, "UC_Energy", Integer.MIN_VALUE);
        if (legacy != Integer.MIN_VALUE)
            return legacy;

        return UCDataUtils.getInt(this.stack, "UC_energy", 0);
    }

    private void setEnergyStored(int amount) {

        UCDataUtils.setInt(this.stack, "UC_Energy", amount);
    }
}
