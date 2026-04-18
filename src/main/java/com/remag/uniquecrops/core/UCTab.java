package com.remag.uniquecrops.core;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class UCTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UniqueCrops.MOD_ID);

    public static final List<Supplier<? extends ItemLike>> UNIQUECROPS_TABS = new ArrayList<>();

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> UNIQUECROPS_TAB = CREATIVE_MODE_TABS.register("uniquecrops_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(UCItems.BOOK_GUIDE.get()))
                    .title(Component.translatable("itemGroup.uniquecrops"))
                    .displayItems((pParameters, pOutput) -> {
                        UNIQUECROPS_TABS.forEach(itemLike -> pOutput.accept(new ItemStack(itemLike.get())));
                    })
                    .build());


    public static <T extends ItemLike, H extends Supplier<T>> H addToTab(H itemLike) {
        UNIQUECROPS_TABS.add(itemLike);
        return itemLike;
    }

}
