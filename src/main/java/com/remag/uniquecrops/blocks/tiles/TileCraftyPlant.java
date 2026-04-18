package com.remag.uniquecrops.blocks.tiles;

import com.remag.uniquecrops.gui.ContainerCraftyPlant;
import com.remag.uniquecrops.init.UCTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileCraftyPlant extends BaseTileUC implements MenuProvider {

    final ItemStackHandler inv = new ItemStackHandler(11) {
        @Override
        protected void onContentsChanged(int slot) {

            setChanged();
        }
    };

    public TileCraftyPlant(BlockPos pos, BlockState state) {

        super(UCTiles.CRAFTYPLANT.get(), pos, state);
    }

    public IItemHandlerModifiable getCraftingInventory() {

        return this.inv;
    }

    public int getCraftingSize() {

        return 9;
    }

    public ItemStack getStaff() {

        return this.inv.getStackInSlot(10);
    }

    public ItemStack getResult() {

        return this.inv.getStackInSlot(9);
    }

    public void setResult(ItemStack toSet) {

        this.inv.setStackInSlot(9, toSet);
    }

    public List<ItemStack> getStoredItems() {

        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < this.inv.getSlots(); i++) {
            ItemStack stack = this.inv.getStackInSlot(i);
            if (!stack.isEmpty())
                stacks.add(stack.copy());
        }
        return stacks;
    }

    // NeoForge 1.21+: NBT methods require HolderLookup.Provider
    @Override
    public void writeCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {
        tag.put("inventory", inv.serializeNBT(provider));
    }

    @Override
    public void readCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {
        if (tag.contains("inventory", 10)) {
            inv.deserializeNBT(provider, tag.getCompound("inventory"));
        } else {
            inv.deserializeNBT(provider, new CompoundTag());
        }
    }

    @Override
    public Component getDisplayName() {

        return Component.translatable("container.uniquecrops.craftyplant");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerinv, Player player) {

        return new ContainerCraftyPlant(windowId, playerinv, this);
    }
}
