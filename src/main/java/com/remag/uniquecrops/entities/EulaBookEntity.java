package com.remag.uniquecrops.entities;

import com.remag.uniquecrops.init.UCEntities;
import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.network.PacketOpenBook;
import com.remag.uniquecrops.network.UCPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = ItemSupplier.class
)
public class EulaBookEntity extends ThrowableProjectile implements ItemSupplier {

    public EulaBookEntity(EntityType<EulaBookEntity> type, Level world) {

        super(type, world);
    }

    public EulaBookEntity(LivingEntity thrower) {

        super(UCEntities.THROWABLE_BOOK.get(), thrower, thrower.level());
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {}

    @Override
    protected void onHit(@NotNull HitResult rtr) {

        if (!level().isClientSide) {
            AABB aabb = this.getBoundingBox().inflate(2.0D, 2.0D, 2.0D);
            List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, aabb);
            for (LivingEntity elb : entities) {
                if (elb instanceof Player) {
                    double d0 = this.distanceToSqr(elb);
                    if (d0 < 4.0D) {
                        if (elb instanceof ServerPlayer)
                            UCPacketHandler.sendTo((ServerPlayer)elb, new PacketOpenBook(elb.getId()));
                    }
                }
            }
            this.discard();
            if (rtr.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult) rtr).getBlockPos().relative(((BlockHitResult) rtr).getDirection());
                ItemStack book = new ItemStack(UCItems.BOOK_EULA.get());
                Containers.dropItemStack(level(), pos.getX(), pos.getY(), pos.getZ(), book);
            }
        }
    }

    @Override
    public @NotNull ItemStack getItem() {

        return new ItemStack(UCItems.BOOK_EULA.get());
    }
}
