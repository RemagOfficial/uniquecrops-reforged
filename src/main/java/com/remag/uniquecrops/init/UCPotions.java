package com.remag.uniquecrops.init;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.potions.PotionEnnui;
import com.remag.uniquecrops.potions.PotionIgnorance;
import com.remag.uniquecrops.potions.PotionReverse;
import com.remag.uniquecrops.potions.PotionZombification;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class UCPotions {

    public static final DeferredRegister<MobEffect> POTIONS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, UniqueCrops.MOD_ID);

    public static final DeferredHolder<MobEffect, PotionEnnui> ENNUI = register("ennui", PotionEnnui::new);
    public static final DeferredHolder<MobEffect, PotionIgnorance> IGNORANCE = register("ignorance", PotionIgnorance::new);
    public static final DeferredHolder<MobEffect, PotionReverse> REVERSE = register("reverse", PotionReverse::new);
    public static final DeferredHolder<MobEffect, PotionZombification> ZOMBIFICATION = register("zombification", PotionZombification::new);

    public static <E extends MobEffect> DeferredHolder<MobEffect, E> register(String name, Supplier<? extends E> supplier) {
        return POTIONS.register(name, supplier);
    }
}
