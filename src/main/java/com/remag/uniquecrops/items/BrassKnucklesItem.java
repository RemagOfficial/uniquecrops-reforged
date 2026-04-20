package com.remag.uniquecrops.items;

import com.remag.uniquecrops.core.UCDataUtils;
import com.remag.uniquecrops.core.enums.EnumParticle;
import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.network.PacketUCEffect;
import com.remag.uniquecrops.network.UCPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import org.jetbrains.annotations.NotNull;

public class BrassKnucklesItem extends SwordItem {

    private static final String HIT_LIST = "UC:hitList";
    private static final String HIT_ENTITY = "UC:hitEntityId";
    private static final String HIT_TIME = "UC:hitTime";
    private static final String HIT_AMOUNT = "UC:hitAmount";

    public BrassKnucklesItem() {

        super(Tiers.IRON, UCItems.unstackable().rarity(Rarity.RARE));
        NeoForge.EVENT_BUS.addListener(this::knuckleDuster);
    }

    private void knuckleDuster(AttackEntityEvent event) {

        if (event.getEntity().level().isClientSide) return;
        if (event.getTarget() instanceof LivingEntity target) {
            Player player = event.getEntity();
            ItemStack brassKnuckles = player.getMainHandItem();
            if (brassKnuckles.getItem() == this) {
                float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                addHitEntity(target, brassKnuckles, damage);
                event.setCanceled(true);
                BlockPos pos = target.blockPosition();
                UCPacketHandler.sendToNearbyPlayers(player.level(), player.blockPosition(), new PacketUCEffect(EnumParticle.CRIT, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, 6));
            }
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean selected) {

        if (!world.isClientSide && entity instanceof Player)
            removeHitEntity(stack, world, (Player)entity, selected);
    }

    @Override
    public boolean shouldCauseReequipAnimation(@NotNull ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {

        return !ItemStack.isSameItem(oldStack, newStack);
    }

    private void addHitEntity(LivingEntity target, ItemStack stack, float damage) {

        ListTag tagList = UCDataUtils.getList(stack, HIT_LIST, 10, false);
        if (tagList == null) return;
        if (tagList.size() > 4) return;

        CompoundTag nbt = new CompoundTag();
        nbt.putInt(HIT_ENTITY, target.getId());
        nbt.putInt(HIT_TIME, 25);
        nbt.putFloat(HIT_AMOUNT, damage);
        tagList.add(nbt);
        UCDataUtils.setList(stack, HIT_LIST, tagList);
    }

    private void removeHitEntity(ItemStack stack, Level world, Player player, boolean selected) {

        ListTag tagList = UCDataUtils.getList(stack, HIT_LIST, 10, true);
        if (tagList == null || tagList.isEmpty()) return;

        boolean remove = false;
        if (!selected) {
            tagList.clear();
            return;
        }
        for (int i = tagList.size() - 1; i >= 0; i--) {
            CompoundTag nbt = tagList.getCompound(i);
            int timer = nbt.getInt(HIT_TIME);
            if (timer > 0)
                nbt.putInt(HIT_TIME, --timer);
            else {
                Entity hitEntity = world.getEntity(nbt.getInt(HIT_ENTITY));
                LivingEntity elb = hitEntity instanceof LivingEntity living ? living : null;
                if (elb != null) {
                    float damage = nbt.getFloat(HIT_AMOUNT);
                    Holder<DamageType> playerDamage = player.level().registryAccess()
                            .registryOrThrow(Registries.DAMAGE_TYPE)
                            .getHolderOrThrow(DamageTypes.PLAYER_ATTACK);

                    DamageSource source = new DamageSource(playerDamage);
                    elb.hurt(source, damage);
                    elb.knockback(damage * 0.131F, Mth.sin(player.yRotO * ((float)Math.PI / 180F)), -Mth.cos(player.yRotO * ((float)Math.PI / 180F)));
                    elb.invulnerableTime = 0;
                }
                tagList.remove(i);
                remove = true;
            }
        }
        if (remove)
            UCDataUtils.setList(stack, HIT_LIST, tagList);
    }
}
