package com.remag.uniquecrops.items;

import com.remag.uniquecrops.api.IBookUpgradeable;
import com.remag.uniquecrops.core.enums.TierItem;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PrecisionAxeItem extends AxeItem implements IBookUpgradeable {

    public PrecisionAxeItem() {

        super(TierItem.PRECISION, UCItems.unstackable());
        NeoForge.EVENT_BUS.addListener(this::checkDrops);
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

    private void checkDrops(LivingDropsEvent event) {
        LivingEntity el = event.getEntity();
        if (el instanceof Player) return;

        DamageSource source = event.getSource();
        if (!(source.getEntity() instanceof Player player)) return;

        ItemStack boots = el.getItemBySlot(EquipmentSlot.FEET);
        Item slipperGlass = UCItems.SLIPPERGLASS.get();
        Item glassSlippers = UCItems.GLASS_SLIPPERS.get();
        if (!boots.isEmpty() && player.getInventory().contains(new ItemStack(slipperGlass))) {
            if (player.level().random.nextInt(2) == 0) {
                addDrop(event, el, new ItemStack(glassSlippers));
                for (int i = 0; i < player.getInventory().items.size(); i++) {
                    ItemStack singleslipper = player.getInventory().getItem(i);
                    if (singleslipper.getItem() == slipperGlass) {
                        singleslipper.shrink(1);
                        break;
                    }
                }
            }
        }

        if (player.getMainHandItem().getItem() == this) {
            ItemStack axe = player.getMainHandItem();
            if (((IBookUpgradeable) axe.getItem()).isMaxLevel(axe)) {
                int looting = EnchantmentHelper.getItemEnchantmentLevel(
                        player.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE),
                        player.getMainHandItem());
                if (player.level().random.nextInt(15) <= 2 + looting) {
                    if (el instanceof Skeleton)
                        addDrop(event, el, new ItemStack(Items.SKELETON_SKULL));
                    if (el instanceof WitherSkeleton)
                        addDrop(event, el, new ItemStack(Items.WITHER_SKELETON_SKULL));
                    if (el instanceof Zombie)
                        addDrop(event, el, new ItemStack(Items.ZOMBIE_HEAD));
                    if (el instanceof Creeper)
                        addDrop(event, el, new ItemStack(Items.CREEPER_HEAD));
                }
            }
        }
    }

    private void addDrop(LivingDropsEvent event, LivingEntity entity, ItemStack drop) {

        ItemEntity ei = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), drop);
        ei.setPickUpDelay(10);
        event.getDrops().add(ei);
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level world, @NotNull Player player) {

        stack.enchant(world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH), 1);
    }
}
