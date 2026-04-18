package com.remag.uniquecrops.init;

import com.remag.uniquecrops.UniqueCrops;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class UCParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, UniqueCrops.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPARK = PARTICLE_TYPES.register("spark", () -> new SimpleParticleType(false));
}
