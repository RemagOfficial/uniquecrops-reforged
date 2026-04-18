package com.remag.uniquecrops.init;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.blocks.tiles.TileBarrel;
import com.remag.uniquecrops.blocks.tiles.TileCraftyPlant;
import com.remag.uniquecrops.gui.ContainerBarrel;
import com.remag.uniquecrops.gui.ContainerCraftyPlant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class UCScreens {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(BuiltInRegistries.MENU, UniqueCrops.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerBarrel>> BARREL = register("abstract_barrel", (windowId, inventory, data) -> {
        TileBarrel te = (TileBarrel) inventory.player.level().getBlockEntity(data.readBlockPos());
        return new ContainerBarrel(windowId, inventory, te);
    });
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerCraftyPlant>> CRAFTYPLANT = register("crafty_plant", (windowId, inventory, data) -> {
        TileCraftyPlant te = (TileCraftyPlant) inventory.player.level().getBlockEntity(data.readBlockPos());
        return new ContainerCraftyPlant(windowId, inventory, te);
    });

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String id, IContainerFactory<T> factory) {
        return CONTAINERS.register(id, () -> IMenuTypeExtension.create(factory));
    }
}
