package com.remag.uniquecrops.init;

import com.remag.uniquecrops.UniqueCrops;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class UCSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, UniqueCrops.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> OOF = createSound("oof");
    public static final DeferredHolder<SoundEvent, SoundEvent> NEON_SIGNS_SOUND = createSound("neonsigns");
    public static final DeferredHolder<SoundEvent, SoundEvent> FAR_AWAY_SOUND = createSound("faraway");
    public static final DeferredHolder<SoundEvent, SoundEvent> TAXI_SOUND = createSound("taxi");
    public static final DeferredHolder<SoundEvent, SoundEvent> SIMPLY_SOUND = createSound("simply");

    public static final ResourceKey<JukeboxSong> NEON_SIGNS = createSong("neonsigns");
    public static final ResourceKey<JukeboxSong> FAR_AWAY = createSong("faraway");
    public static final ResourceKey<JukeboxSong> TAXI = createSong("taxi");
    public static final ResourceKey<JukeboxSong> SIMPLY = createSong("simply");

    private static DeferredHolder<SoundEvent, SoundEvent> createSound(String name) {

        return SOUNDS.register(name, () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, name), 1.0F));
    }

    private static ResourceKey<JukeboxSong> createSong(String name) {

        return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, name));
    }
}
