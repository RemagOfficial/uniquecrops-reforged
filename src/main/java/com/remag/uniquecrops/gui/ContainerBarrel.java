package com.remag.uniquecrops.gui;

import com.remag.uniquecrops.blocks.tiles.TileBarrel;
import com.remag.uniquecrops.init.UCScreens;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ContainerBarrel extends AbstractContainerMenu {

    TileBarrel tile;
    private final IItemHandler inv;

    @SuppressWarnings("unchecked")
    public ContainerBarrel(int windowId, Inventory playerinv, TileBarrel tile) {

        super(UCScreens.BARREL.get(), windowId);
        this.tile = tile;
        // Temporary hard bridge while TileBarrel still exposes legacy handler type.
        this.inv = (IItemHandler) (Object) tile.getInventory();
        int i;
        int j;
        Random rand = new Random();
        for (i = 0; i < 2; ++i) {
            for (j = 0; j < 2; ++j) {
                int z = rand.nextInt(inv.getSlots());
                this.addSlot(new SlotItemHandler(inv, z, 72 + j * 18, 27 + i * 18));
            }
        }
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerinv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        }
        for (i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerinv, i, 8 + i * 18, 142));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {

        return !(player instanceof FakePlayer);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slot) {

        ItemStack stack = ItemStack.EMPTY;
        Slot slotto = slots.get(slot);

        if (slotto.hasItem()) {
            ItemStack stackInSlot = slotto.getItem();
            stack = stackInSlot.copy();

            if (slot < 4) {
                if (!this.moveItemStackTo(stackInSlot, 4, slots.size(), true))
                    return ItemStack.EMPTY;
            } else {
                boolean b = false;
                for (int i = 0; i < inv.getSlots(); i++) {
                    if (this.getSlot(i).mayPlace(stackInSlot)) {
                        if (this.moveItemStackTo(stackInSlot, i, i + 1, false)) {
                            b = true;
                            break;
                        }
                    }
                }
                if (!b)
                    return ItemStack.EMPTY;
            }
            if (stackInSlot.getCount() == 0)
                slotto.set(ItemStack.EMPTY);
            else
                slotto.setChanged();

            if (stackInSlot.getCount() == stack.getCount())
                return ItemStack.EMPTY;

            slotto.onTake(player, stackInSlot);
        }
        return stack;
    }
}
