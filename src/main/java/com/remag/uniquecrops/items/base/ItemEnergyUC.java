package com.remag.uniquecrops.items.base;

import com.remag.uniquecrops.api.IItemEnergy;
import com.remag.uniquecrops.core.NBTUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public class ItemEnergyUC extends ItemBaseUC implements IItemEnergy {

    public ItemEnergyUC(Properties prop) {

        super(prop);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {

        return getEnergy(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {

        IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (storage != null)
            return Math.round((float) storage.getEnergyStored() * 13.0F / (float) storage.getMaxEnergyStored());
        return 0;
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {

        return 0x03fcb6;
    }

    @Override
    public boolean shouldCauseReequipAnimation(@NotNull ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {

        return !ItemStack.isSameItem(oldStack, newStack);
    }

    public void setEnergyStored(ItemStack stack, int value) {

        NBTUtils.setInt(stack, "UC_energy", Mth.clamp(value, 0, getCapacity(stack)));
    }

    @Override
    public int receiveEnergy(ItemStack stack, int maxReceive, boolean sim) {

        int energystored = getEnergy(stack);
        int energyreceived = Math.min(getCapacity(stack) - energystored, maxReceive);
        if (!sim)
            setEnergyStored(stack, energystored + energyreceived);
        return energyreceived;
    }

    @Override
    public int extractEnergy(ItemStack stack, int maxExtract, boolean sim) {

        int energystored = getEnergy(stack);
        int energyextracted = Math.min(energystored, maxExtract);
        if (!sim)
            setEnergyStored(stack, energystored - energyextracted);
        return energyextracted;
    }

    @Override
    public int getEnergy(ItemStack stack) {

        return NBTUtils.getInt(stack, "UC_energy", 0);
    }

    @Override
    public int getCapacity(ItemStack stack) {

        return 500;
    }
}
