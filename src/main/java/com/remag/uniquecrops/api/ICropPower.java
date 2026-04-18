package com.remag.uniquecrops.api;

import net.minecraft.nbt.CompoundTag;

public interface ICropPower {

    int getCapacity();

    int getPower();

    int getCooldown();

    boolean hasCooldown();

    boolean canAdd();

    void add(int add);

    void remove(int subtract);

    void setPower(int power);

    void setCapacity(int capacity);

    void setCooldown(int amount);

    void setIgnoreCooldown(boolean flag);

    CompoundTag serializeNBT();

    void deserializeNBT(CompoundTag nbt);
}
