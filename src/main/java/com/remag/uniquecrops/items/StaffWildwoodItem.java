package com.remag.uniquecrops.items;

import com.remag.uniquecrops.api.ICropPower;
import com.remag.uniquecrops.api.IItemBooster;
import com.remag.uniquecrops.blocks.tiles.TileDigger;
import com.remag.uniquecrops.capabilities.CPProvider;
import com.remag.uniquecrops.core.UCUtils;
import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.items.base.ItemBaseUC;
import com.remag.uniquecrops.network.PacketSyncCap;
import com.remag.uniquecrops.network.UCPacketHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class StaffWildwoodItem extends ItemBaseUC {

    public StaffWildwoodItem() {

        super(UCItems.unstackable().rarity(Rarity.EPIC));
        NeoForge.EVENT_BUS.addListener(this::onCropGrowth);
    }

    private void onCropGrowth(CropGrowEvent.Pre event) {
        if (!(event.getLevel() instanceof Level level) || level.isClientSide) return;

        BlockPos pos = event.getPos();

        List<Player> players = level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(7, 3, 7));
        for (Player player : players) {
            ItemStack itemCap = player.getMainHandItem();
            ItemStack offhand = player.getOffhandItem();
            int distance = (int) player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
            int range = (offhand.getItem() instanceof IItemBooster booster) ? 4 + booster.getRange(offhand) : 3;

            if (distance <= range) {
                withCropPower(itemCap, crop -> {
                    if (crop.canAdd()) {
                        int extra = (offhand.getItem() instanceof IItemBooster booster) ? booster.getPower(offhand) : 0;
                        crop.add(1 + extra);
                        if (player instanceof ServerPlayer serverPlayer) {
                            CompoundTag syncTag = crop.serializeNBT();
                            UCPacketHandler.sendTo(serverPlayer, new PacketSyncCap(syncTag));
                        }
                        event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
                    }
                });
            }
        }

        BlockState state = event.getState();
        if (!(state.getBlock() instanceof CropBlock)) return;

        BlockEntity tile = UCUtils.getClosest(pos, 8.0D, TileDigger.class);
        if (tile instanceof TileDigger digger) {
            if (digger.isJobDone()) {
                event.setResult(CropGrowEvent.Pre.Result.DEFAULT);
                return;
            }

            if (digger.digBlock(level)) {
                event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
            } else {
                event.setResult(CropGrowEvent.Pre.Result.DEFAULT);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        boolean flag = Screen.hasShiftDown();
        withCropPower(stack, crop -> {
            if (flag) {
                tooltipComponents.add(Component.literal(ChatFormatting.GREEN + "Crop Power: " + crop.getPower() + "/" + crop.getCapacity()));
            }
        });

        if (!flag) {
            tooltipComponents.add(Component.literal(ChatFormatting.LIGHT_PURPLE + "<Press shift>"));
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean isSelected) {

        withCropPower(stack, crop -> {
            if (crop.hasCooldown() && !world.isClientSide) {
                crop.setCooldown(crop.getCooldown() - 1);
            }
        });
    }

    @Override
    public boolean shouldCauseReequipAnimation(@NotNull ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {

        return !ItemStack.isSameItem(oldStack, newStack);
    }

    public static boolean adjustPower(ItemStack stack, int amount) {

        final boolean[] success = {false};
        withCropPower(stack, crop -> {
            int power = crop.getPower();
            if (power >= amount) {
                crop.remove(amount);
                success[0] = true;
            }
        });
        return success[0];
    }

    private static void withCropPower(ItemStack stack, Consumer<ICropPower> action) {
        ICropPower crop = stack.getCapability(CPProvider.CROP_POWER, null);
        if (crop != null) {
            action.accept(crop);
        }
    }
}

