package com.remag.uniquecrops.items.curios;

import com.remag.uniquecrops.items.base.ItemCurioUC;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.List;

public class EmblemTransformation extends ItemCurioUC {

    public EmblemTransformation() {

        NeoForge.EVENT_BUS.addListener(this::onHitEntity);
    }

    private void onHitEntity(LivingIncomingDamageEvent event) {
        if (event.getAmount() <= 0) return;
        if (!(event.getSource().getDirectEntity() instanceof Player player)) return;
        if (!hasCurio(player)) return;
        Entity entityHurt = event.getEntity();
        if (entityHurt instanceof Player || entityHurt instanceof WitherBoss ||
                entityHurt instanceof EnderDragon || entityHurt instanceof Warden) return;

        if (player.level().random.nextInt(100) == 0) {
            LivingEntity elb = event.getEntity();

            List<EntityType<?>> entityTypes = BuiltInRegistries.ENTITY_TYPE.stream()
                    .filter(type -> canTransformInto(type, elb))
                    .toList();

            if (entityTypes.isEmpty()) return;

            EntityType<?> type = entityTypes.get(player.level().random.nextInt(entityTypes.size()));
            Entity entity = createFromType(type, elb.level());

            if (entity == null) return;

            entity.moveTo(elb.getX(), elb.getY(), elb.getZ(), elb.getYRot(), elb.getXRot());
            elb.level().addFreshEntity(entity);
            elb.discard();
        }
    }

    private static boolean canTransformInto(EntityType<?> type, LivingEntity source) {

        Entity probe = createFromType(type, source.level());
        if (probe == null) return false;
        if (!(probe instanceof LivingEntity)) return false;
        if (probe instanceof Player || probe instanceof WitherBoss || probe instanceof EnderDragon || probe instanceof Warden || probe instanceof ArmorStand) {
            probe.discard();
            return false;
        }
        probe.discard();
        return true;
    }

    private static Entity createFromType(EntityType<?> type, net.minecraft.world.level.Level level) {

        var typeKey = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        CompoundTag tag = new CompoundTag();
        tag.putString("id", typeKey.toString());
        return EntityType.create(tag, level).orElse(null);
    }
}
