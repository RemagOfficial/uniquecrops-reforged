package com.remag.uniquecrops.gui;

import com.remag.uniquecrops.api.IArtisiaRecipe;
import com.remag.uniquecrops.api.ICropPower;
import com.remag.uniquecrops.blocks.tiles.TileCraftyPlant;
import com.remag.uniquecrops.capabilities.CPProvider;
import com.remag.uniquecrops.crafting.RecipeArtisia;
import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.init.UCScreens;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ContainerCraftyPlant extends AbstractContainerMenu {

    TileCraftyPlant tile;
    final int OUTPUT_SLOT = 9;
    final int STAFF_SLOT = 10;

    public ContainerCraftyPlant(int windowId, Inventory playerinv, TileCraftyPlant tile) {

        super(UCScreens.CRAFTYPLANT.get(), windowId);
        this.tile = tile;

        addSlot(new SlotSeedCrafting(tile.getCraftingInventory(), OUTPUT_SLOT, 124, 35));
        addSlot(new SlotSeedCrafting(tile.getCraftingInventory(), STAFF_SLOT, 94, 17));

        for (int i = 0; i < 3; ++i) {
            for (int m = 0; m < 3; ++m)
                this.addSlot(new SlotSeedCrafting(tile.getCraftingInventory(), m + i * 3, 30 + m * 18, 17 + i * 18));
        }
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 9; k++)
                addSlot(new Slot(playerinv, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
        }
        for (int l = 0; l < 9; l++)
            addSlot(new Slot(playerinv, l, 8 + l * 18, 142));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {

        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack stack1 = slot.getItem();
            stack = stack1.copy();
            int size = tile.getCraftingInventory().getSlots();

            if (i < size) {
                slot.onTake(player, stack1);
                if (!this.moveItemStackTo(stack1, size, this.slots.size(), true))
                    return ItemStack.EMPTY;
            } else {
                boolean b = false;
                for (int j = 0; j < size; j++) {
                    if (this.getSlot(j).mayPlace(stack1)) {
                        if (this.moveItemStackTo(stack1, j, j + 1, false)) {
                            b = true;
                            break;
                        }
                    }
                }
                if (!b)
                    return ItemStack.EMPTY;
            }
            if (stack1.getCount() == 0)
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();

            if (stack1.getCount() == stack.getCount())
                return ItemStack.EMPTY;

            slot.onTake(player, stack1);
        }
        return stack;
    }

    @Override
    public boolean stillValid(Player player) {

        return true;
    }

    private class SlotSeedCrafting extends SlotItemHandler {

        final int COST = 50;
        final int indexSlot;

        public SlotSeedCrafting(IItemHandler itemHandler, int index, int xPosition, int yPosition) {

            super(itemHandler, index, xPosition, yPosition);
            this.indexSlot = index;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public int getMaxStackSize(@NotNull ItemStack stack) {
            return 1;
        }

        @Override
        public void setChanged() {

            if (!tile.getLevel().isClientSide && this.indexSlot < 9) {
                IItemHandler handler = getItemHandler();
                List<ItemStack> stacks = IntStream.range(0, tile.getCraftingSize()).mapToObj(i -> handler.getStackInSlot(i)).collect(Collectors.toList());
                AtomicReference<ItemStack> result = new AtomicReference<>(ItemStack.EMPTY);
                IArtisiaRecipe artisiaRecipe = RecipeArtisia.findRecipe(stacks, tile.getLevel());
                if (artisiaRecipe != null)
                    result.set(artisiaRecipe.getResultItem());
                tile.setResult(result.get());
                tile.setChanged();
            }
            super.setChanged();
        }

        @Override
        public void onTake(Player player, ItemStack stack) {


            if (!tile.getLevel().isClientSide && indexSlot == tile.getCraftingSize()) {
                if (!stack.isEmpty()) {
                    if (indexSlot == tile.getCraftingSize()) {
                        if (!stack.isEmpty()) {
                            LazyOptional<ICropPower> cap = tile.getStaff().getCapability(CPProvider.CROP_POWER, null);
                            if (!cap.isPresent()) {
                                IntStream.range(0, tile.getCraftingSize()).forEach(i -> {
                                    if (!getItemHandler().getStackInSlot(i).isEmpty())
                                        getItemHandler().getStackInSlot(i).shrink(1);
                                });
                            }
                            cap.ifPresent(crop -> {
                                if (crop.getPower() >= COST) {
                                    crop.remove(COST);
                                    getItemHandler().insertItem(OUTPUT_SLOT, stack.copy(), false);
                                } else
                                    IntStream.range(0, tile.getCraftingSize()).forEach(i -> {
                                        if (!getItemHandler().getStackInSlot(i).isEmpty())
                                            getItemHandler().getStackInSlot(i).shrink(1);
                                    });
                            });
                        }
                    }
                }
            }
            super.onTake(player, stack);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {

            if (indexSlot == OUTPUT_SLOT)
                return false;
            if (indexSlot == STAFF_SLOT)
                return stack.getItem() == UCItems.WILDWOOD_STAFF.get();

            return true;
        }
    }
}
