package com.remag.uniquecrops.items;

import com.remag.uniquecrops.api.IBookUpgradeable;
import com.remag.uniquecrops.core.NBTUtils;
import com.remag.uniquecrops.core.enums.TierItem;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class PrecisionPickaxeItem extends PickaxeItem implements IBookUpgradeable {

    public PrecisionPickaxeItem() {

        super(TierItem.PRECISION, UCItems.unstackable());
        NeoForge.EVENT_BUS.addListener(this::breakSpawner);
        NeoForge.EVENT_BUS.addListener(this::placeSpawner);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> list, @NotNull TooltipFlag flag) {

        if (stack.getItem() instanceof IBookUpgradeable) {
            if (((IBookUpgradeable)stack.getItem()).getLevel(stack) > -1)
                list.add(Component.literal(ChatFormatting.GOLD + "+" + ((IBookUpgradeable)stack.getItem()).getLevel(stack)));
            else
                list.add(Component.literal(ChatFormatting.GOLD + "Upgradeable"));
        }
    }

    private void breakSpawner(BlockEvent.BreakEvent event) {

        boolean flag = event.getPlayer().getMainHandItem().getItem() == this;
        if (!flag) return;

        ItemStack pick = event.getPlayer().getMainHandItem();
        if (!this.isMaxLevel(pick)) return;

        if (event.getState().getBlock() == Blocks.SPAWNER) {
            event.setCanceled(true);
            BlockEntity tile = event.getLevel().getBlockEntity(event.getPos());
            if (tile instanceof SpawnerBlockEntity) {
                ItemStack stack = new ItemStack(event.getState().getBlock());
                if (event.getLevel() instanceof Level level && !level.isClientSide()) {
                    CompoundTag tileTag = saveBlockEntity(tile, level);
                    if (tileTag != null) {
                        NBTUtils.setCompound(stack, "Spawner", tileTag);
                    }
                    Containers.dropItemStack(level, event.getPos().getX() + 0.5, event.getPos().getY() + 0.5, event.getPos().getZ() + 0.5, stack);
                }
            }
            event.getLevel().removeBlock(event.getPos(), false);
            if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
                ItemStack held = event.getPlayer().getMainHandItem();
                held.hurtAndBreak(1, serverPlayer, serverPlayer.getEquipmentSlotForItem(held));
            }
        }
    }

    private void placeSpawner(PlayerInteractEvent.RightClickBlock event) {

        if (event.getItemStack().getItem()!= Blocks.SPAWNER.asItem()) return;
        if (event.getFace() == null) return;

        ItemStack stack = event.getEntity().getItemInHand(event.getHand());
        if (stack.getItem() == Blocks.SPAWNER.asItem() && NBTUtils.verifyExistance(stack, "Spawner")) {
            BlockPos pos = event.getPos().relative(event.getFace());
            if (!event.getLevel().isEmptyBlock(pos))
                return;
            BlockState spawner = Blocks.SPAWNER.defaultBlockState();
            event.getLevel().setBlockAndUpdate(pos, spawner);
            BlockEntity tile = event.getLevel().getBlockEntity(pos);
            CompoundTag tag = NBTUtils.getCompound(stack, "Spawner", true);
            if (tag == null || tile == null) return;
            tag.putInt("x", pos.getX());
            tag.putInt("y", pos.getY());
            tag.putInt("z", pos.getZ());
            loadBlockEntity(tile, tag, event.getLevel());
            event.getEntity().swing(event.getHand());
            if (!event.getEntity().isCreative())
                event.getItemStack().shrink(1);
            event.setCanceled(true);
        }
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level world, @NotNull Player player) {

        stack.enchant(world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH), 1);
    }

    private static @Nullable CompoundTag saveBlockEntity(BlockEntity tile, Level level) {
        return tile.saveWithoutMetadata(level.registryAccess());
    }

    private static void loadBlockEntity(BlockEntity tile, CompoundTag tag, Level level) {
        tile.loadCustomOnly(tag, level.registryAccess());
    }
}
