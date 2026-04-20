package com.remag.uniquecrops.items;

import com.remag.uniquecrops.core.UCDataUtils;
import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.items.base.ItemBaseUC;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ImpactShieldItem extends ItemBaseUC {

    private static final String DAMAGE_POOL = "UC:ImpactShieldDamage";
    private static long lastBlockTime = 0;

    public ImpactShieldItem() {

        super(UCItems.defaultBuilder().durability(25));
        NeoForge.EVENT_BUS.addListener(this::onShieldBlock);
    }

    /*
    private void onRealBlock(ShieldBlockEvent event) {
    }
    */

    // This SHOULD be based on actual ShieldBlockEvent.
    // That would involve registering the Impact Shield as a proper shield instead of handling its use ourselves.
    private void onShieldBlock(LivingShieldBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Level level = player.level();
        if (level.isClientSide) return;

        DamageSource incoming = event.getDamageSource();
        if (incoming.getEntity() instanceof LivingEntity) {
            ItemStack activeStack = player.getUseItem();
            if (activeStack.getItem() == this) {
                long blockTime = level.getGameTime();
                if (blockTime - lastBlockTime >= 10) {
                    level.playSound(null, player.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS);
                    lastBlockTime = blockTime;
                }

                // Force this hit to count as fully blocked by the active mirror.
                event.setBlocked(true);
                event.setBlockedDamage(event.getOriginalBlockedDamage());
                // We handle durability ourselves below.
                event.setShieldDamage(0F);

                damageImpactShield(player, activeStack, event.getBlockedDamage());
            }
        }
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @Nullable LivingEntity entity) {

        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {

        return UseAnim.BLOCK;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(this))
            return InteractionResultHolder.pass(stack);

        player.startUsingItem(hand);
        return InteractionResultHolder.success(stack);
    }

    private void damageImpactShield(Player player, ItemStack stack, float damage) {

        stack.setDamageValue(stack.getDamageValue() + 1);
        float strength = UCDataUtils.getFloat(stack, DAMAGE_POOL, 0);
        if (stack.getDamageValue() > stack.getMaxDamage()) {
            player.level().explode(player, player.getX(), player.getY(), player.getZ(), Math.min(strength, 20F), Level.ExplosionInteraction.NONE);

            stack.setDamageValue(0);
            player.getCooldowns().addCooldown(this, 300);
            UCDataUtils.setFloat(stack, DAMAGE_POOL, 0);
            player.stopUsingItem();
            return;
        }
        UCDataUtils.setFloat(stack, DAMAGE_POOL, strength + damage);
    }
}
