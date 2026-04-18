package com.remag.uniquecrops.capabilities;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.api.ICropPower;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.Nullable;

// NOTE: NeoForge 1.21+ ICapabilityProvider likely requires 3 type parameters: <T, C, R>
// Adjust the generics and method signature below to match your NeoForge version if needed.
public class CPProvider implements ICapabilityProvider<ItemStack, Void, ICropPower> {

    public static final ItemCapability<ICropPower, Void> CROP_POWER = ItemCapability.createVoid(
        ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "crop_power"),
        ICropPower.class
    );

    private final ICropPower crop;

    public CPProvider() {

        this.crop = new CPCapability();
    }

    public CPProvider(int capacity, boolean ignoreCooldown) {

        this.crop = new CPCapability();
        crop.setCapacity(capacity);
        crop.setIgnoreCooldown(ignoreCooldown);
    }

    @Nullable
    @Override
    public ICropPower getCapability(ItemStack stack, Void context) {

        return crop;
    }

    public CompoundTag serializeNBT() {

        return crop.serializeNBT();
    }

    public void deserializeNBT(CompoundTag nbt) {

        crop.deserializeNBT(nbt);
    }
}
