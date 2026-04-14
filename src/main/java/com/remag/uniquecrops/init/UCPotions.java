package com.remag.uniquecrops.init;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.potions.PotionEnnui;
import com.remag.uniquecrops.potions.PotionIgnorance;
import com.remag.uniquecrops.potions.PotionReverse;
import com.remag.uniquecrops.potions.PotionZombification;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class UCPotions {

    public static final DeferredRegister<MobEffect> POTIONS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, UniqueCrops.MOD_ID);

    public static final RegistryObject<MobEffect> ENNUI = register("ennui", PotionEnnui::new);
    public static final RegistryObject<MobEffect> IGNORANCE = register("ignorance", PotionIgnorance::new);
    public static final RegistryObject<MobEffect> REVERSE = register("reverse", PotionReverse::new);
    public static final RegistryObject<MobEffect> ZOMBIFICATION = register("zombification", PotionZombification::new);

    public static <E extends MobEffect> RegistryObject<E> register(String name, Supplier<? extends E> supplier) {

        return POTIONS.register(name, supplier);
    }
}
