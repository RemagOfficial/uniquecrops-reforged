package com.remag.uniquecrops.data;

import com.remag.uniquecrops.data.advancements.UCAdvancementGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UCAdvancementProvider extends AdvancementProvider {

    public UCAdvancementProvider(DataGenerator gen, CompletableFuture<HolderLookup.Provider> providerCompletableFuture, ExistingFileHelper existingFileHelper) {

        super(gen.getPackOutput(), providerCompletableFuture, existingFileHelper, List.of(new UCAdvancementGenerator()));
    }
}
