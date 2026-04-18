package com.remag.uniquecrops.entities;

import com.remag.uniquecrops.core.enums.EnumParticle;
import com.remag.uniquecrops.init.UCEntities;
import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.network.PacketUCEffect;
import com.remag.uniquecrops.network.UCPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = ItemSupplier.class
)
public class WeepingEyeEntity extends ThrowableProjectile implements ItemSupplier {

    public WeepingEyeEntity(EntityType<WeepingEyeEntity> type, Level world) {

        super(type, world);
    }

    public WeepingEyeEntity(LivingEntity thrower) {

        super(UCEntities.WEEPING_EYE.get(), thrower, thrower.level());
    }

    @Override
    protected void onHit(@NotNull HitResult rtr) {

        if (!level().isClientSide) {
            BlockPos pos = new BlockPos((int) rtr.getLocation().x, (int) rtr.getLocation().y, (int) rtr.getLocation().z);
            AABB area = new AABB(
                    pos.getX() - 10, pos.getY() - 5, pos.getZ() - 10,
                    pos.getX() + 10, pos.getY() + 5, pos.getZ() + 10
            );
            List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, area);
            for (LivingEntity ent : entities) {
                if (ent.isAlive() && (ent instanceof Monster || ent instanceof Slime))
                    ent.addEffect(new MobEffectInstance(MobEffects.GLOWING, 300));
            }
            UCPacketHandler.sendToNearbyPlayers(level(), pos, new PacketUCEffect(EnumParticle.CLOUD, pos.getX() - 0.5D, pos.getY() + 0.1D, pos.getZ() - 0.5D, 5));
        }
    }

    @Override
    public @NotNull ItemStack getItem() {

        return new ItemStack(UCItems.WEEPINGEYE.get());
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {

    }
}
