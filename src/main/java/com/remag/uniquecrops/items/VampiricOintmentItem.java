package com.remag.uniquecrops.items;

import com.remag.uniquecrops.core.UCDataUtils;
import com.remag.uniquecrops.core.UCStrings;
import com.remag.uniquecrops.items.base.ItemBaseUC;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class VampiricOintmentItem extends ItemBaseUC {

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity target, @NotNull InteractionHand hand) {

        if (!player.level().isClientSide && !hasTaglock(stack) && !(target instanceof Player) &&
            !(target instanceof WitherBoss) && !(target instanceof EnderDragon) && !(target instanceof Warden)) {
            ItemStack newStack = new ItemStack(this);
            setTaglock(newStack, target);
            ItemHandlerHelper.giveItemToPlayer(player, newStack);
            stack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public boolean hasTaglock(ItemStack stack) {

        return UCDataUtils.verifyExistance(stack, UCStrings.TAG_LOCK);
    }

    public void setTaglock(ItemStack stack, LivingEntity target) {

        UUID id = target.getUUID();
        UCDataUtils.setString(stack, UCStrings.TAG_LOCK, id.toString());
    }
}
