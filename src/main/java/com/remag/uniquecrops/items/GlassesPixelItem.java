package com.remag.uniquecrops.items;

import com.remag.uniquecrops.api.IBookUpgradeable;
import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.core.UCOreHandler;
import com.remag.uniquecrops.core.UCDataUtils;
import com.remag.uniquecrops.core.UCStrings;
import com.remag.uniquecrops.core.UCUtils;
import com.remag.uniquecrops.core.enums.EnumArmorMaterial;
import com.remag.uniquecrops.items.base.ItemArmorUC;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.Random;

public class GlassesPixelItem extends ItemArmorUC implements IBookUpgradeable {


    public GlassesPixelItem() {

        super(EnumArmorMaterial.GLASSES_PIXELS, Type.HELMET);
        NeoForge.EVENT_BUS.addListener(this::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(this::onBlockBreak);
    }

    private void onPlayerTick(PlayerTickEvent.Pre event) {

        Player player = event.getEntity();

        ItemStack pixelGlasses = player.getInventory().armor.get(3);
        if (pixelGlasses.is(this)) {
            boolean flag = UCDataUtils.getBoolean(pixelGlasses, "isActive", false);
            boolean flag2 = isMaxLevel(pixelGlasses);
            if (flag && flag2) {
                if (player.level().getGameTime() % 20 == 0) {
                    ChunkPos cPos = new ChunkPos(player.blockPosition());
                    if (!player.level().isClientSide) {
                        if (UCOreHandler.getInstance().getSaveInfo().containsKey(cPos)) {
                            BlockPos pos = UCOreHandler.getInstance().getSaveInfo().get(cPos);
                            UCDataUtils.setLong(pixelGlasses, "orePos", pos.asLong());
                            UCOreHandler.getInstance().removeChunk(player.level(), BlockPos.ZERO, true);
                        }
                        else {
                            UCDataUtils.setLong(pixelGlasses, "orePos", BlockPos.ZERO.asLong());
                            UCOreHandler.getInstance().addChunk(player.level(), BlockPos.ZERO, true);
                        }
                    }
                }
            }
        }
        if (player.getPersistentData().contains(UCStrings.TAG_ABSTRACT)) {
            if (player.level().random.nextInt(1000) == 0) {
                Random rand = new Random();
                if (rand.nextInt(10) != 0) {
                    ResourceLocation abstractId = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "abstract");
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(BuiltInRegistries.ITEM.getOptional(abstractId).orElse(Items.AIR)));
                    if (!player.level().isClientSide)
                        UCUtils.setAbstractCropGrowth(player, -1);
                }
            }
        }
    }

    private void onBlockBreak(BlockEvent.BreakEvent event) {

        Player player = event.getPlayer();

        if (player.getMainHandItem().isCorrectToolForDrops(event.getState()) && player.getInventory().armor.get(3).getItem() == this) {
            boolean flag = UCDataUtils.getBoolean(player.getInventory().armor.get(3), "isActive", false);
            boolean flag2 = isMaxLevel(player.getInventory().armor.get(3));
            if (flag && flag2 && event.getState().is(BlockTags.BASE_STONE_OVERWORLD)) {
                if (UCOreHandler.getInstance().getSaveInfo().containsValue(event.getPos())) {
                    if (!event.getLevel().isClientSide()) {
                        ResourceLocation diamondsId = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "diamonds");
                        Containers.dropItemStack(event.getPlayer().level(), event.getPos().getX() + 0.5, event.getPos().getY() + 0.5, event.getPos().getZ() + 0.5,
                                new ItemStack(BuiltInRegistries.ITEM.getOptional(diamondsId).orElse(Items.DIAMOND)));
                        UCOreHandler.getInstance().removeChunk(event.getPlayer().level(), event.getPos(), true);
                    }
                    if (!player.isCreative() && player instanceof ServerPlayer serverPlayer)
                        player.getInventory().armor.get(3).hurtAndBreak(2, serverPlayer, serverPlayer.getEquipmentSlotForItem(player.getInventory().armor.get(3)));
                }
            }
        }
    }
}
