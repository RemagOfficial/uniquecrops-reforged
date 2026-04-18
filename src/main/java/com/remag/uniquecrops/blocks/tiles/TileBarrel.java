package com.remag.uniquecrops.blocks.tiles;

import com.remag.uniquecrops.gui.ContainerBarrel;
import com.remag.uniquecrops.init.UCTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileBarrel extends BaseTileUC implements MenuProvider {

    private final ItemStackHandler inv = new ItemStackHandler(100);

    public TileBarrel(BlockPos pos, BlockState state) {

        super(UCTiles.BARREL.get(), pos, state);
    }

    // NeoForge 1.21+: NBT methods require HolderLookup.Provider
    public void writeCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {
        tag.put("inventory", inv.serializeNBT(provider));
    }

    public void readCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {
        if (tag.contains("inventory", 10)) {
            inv.deserializeNBT(provider, tag.getCompound("inventory"));
        } else {
            inv.deserializeNBT(provider, new CompoundTag());
        }
    }

    public IItemHandler getInventory() {

        return this.inv;
    }

    @Override
    public Component getDisplayName() {

        return Component.translatable("container.uniquecrops.abstractbarrel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerinv, Player player) {

        return new ContainerBarrel(windowId, playerinv, this);
    }
}
