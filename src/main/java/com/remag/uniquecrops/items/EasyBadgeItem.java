package com.remag.uniquecrops.items;

import com.remag.uniquecrops.core.UCStrings;
import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.items.base.ItemBaseUC;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.List;

public class EasyBadgeItem extends ItemBaseUC {

    private static final int RANGE = 5;

    public EasyBadgeItem() {

        super(UCItems.unstackable());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        tooltipComponents.add(Component.translatable(UCStrings.TOOLTIP + "easybadge").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean isSelected) {

        if (!(entity instanceof Player) || (entity instanceof FakePlayer) ||
                slot >= Inventory.getSelectionSize() || world.isClientSide ||
                (world.getGameTime() % 10) != 0)
            return;

        BlockPos pos = entity.blockPosition();
        List<Monster> monsters = world.getEntitiesOfClass(Monster.class, new AABB(pos).expandTowards(RANGE, RANGE, RANGE));
        for (Monster ent: monsters) {

            if (ent instanceof Zombie zombo)
                zombo.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0D);

            if (ent instanceof Skeleton skele)
                skele.goalSelector.getAvailableGoals().stream().filter(goal -> goal.getGoal() instanceof RangedBowAttackGoal)
                        .findFirst().ifPresent(g -> {
                    ((RangedBowAttackGoal)g.getGoal()).setMinAttackInterval(80);
                });

            if (ent instanceof Creeper creep) {
                CompoundTag creepTags = creep.getPersistentData();
                creepTags.putFloat("Fuse", 60);
                creep.readAdditionalSaveData(creepTags);
                //ObfuscationReflectionHelper.setPrivateValue(Creeper.class, creep, 60, "field_82225_f");
            }
        }
    }
}
