package com.remag.uniquecrops.items.base;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.Rarity;

public class ItemRecordUC extends Item {

    public ItemRecordUC(ResourceKey<JukeboxSong> songKey) {

        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(songKey));
    }
}
