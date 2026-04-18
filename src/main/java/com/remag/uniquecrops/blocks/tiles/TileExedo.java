package com.remag.uniquecrops.blocks.tiles;

import com.remag.uniquecrops.core.UCUtils;
import com.remag.uniquecrops.init.UCTiles;
import com.remag.uniquecrops.network.UCPacketDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;

// removed registry/holder-based DamageType lookup (changed in 1.21 mappings)

public class TileExedo extends BaseTileUC {

    int searchTime = 100;
    int wiggleTime;
    public int timeAfterWiggle;
    public final int maxTime = 15;
    public boolean isWiggling = false;

    boolean foundEntity = false;
    private UUID entityId;
    public LivingEntity ent;

    public TileExedo(BlockPos pos, BlockState state) {

        super(UCTiles.EXEDO.get(), pos, state);
    }

    public void tickServer() {

        ++timeAfterWiggle;
//        if (!level.isClientSide && timeAfterWiggle >= maxTime)
//            nomAndDrop();

        if (!level.isClientSide && isWiggling && --wiggleTime <= 0) {
            isWiggling = false;
            UCPacketDispatcher.dispatchTEToNearbyPlayers(this);
        }
        if (level.getGameTime() % (searchTime - level.random.nextInt(20)) == 0) {
            if (!isWiggling) {
                LivingEntity elb = getTargetedEntity();
                if (elb != null) {
                    if (foundEntity) {
                        chomp();
                        return;
                    }
                    entityId = elb.getUUID();
                    wiggle();
                }
            }
        }
    }

    private void wiggle() {

        wiggleTime = 20;
        foundEntity = true;
        isWiggling = true;
        UCPacketDispatcher.dispatchTEToNearbyPlayers(this);
    }

    private void chomp() {

        timeAfterWiggle = 0;
        foundEntity = false;
        this.setChanged();
        nomAndDrop();
        UCPacketDispatcher.dispatchTEToNearbyPlayers(this);
    }

    private void nomAndDrop() {

        LivingEntity elb = UCUtils.getTaggedEntity(entityId);
        if (elb != null && elb.isAlive()) {
            float f = (float) Mth.atan2(elb.getZ() - worldPosition.getZ(), elb.getX() - worldPosition.getX());
            EvokerFangs evoke = new EvokerFangs(level, elb.getX(), elb.getY(), elb.getZ(), f, 0, null);
            level.addFreshEntity(evoke);
            Holder<DamageType> magicDamage = level.registryAccess()
                    .registryOrThrow(Registries.DAMAGE_TYPE)
                    .getHolderOrThrow(DamageTypes.MAGIC);

            DamageSource source = new DamageSource(magicDamage);
            elb.hurt(source, elb.getMaxHealth());
        }
        entityId = null;
    }

    private LivingEntity getTargetedEntity() {

        if (!level.isClientSide) {
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class,
                new AABB(
                    worldPosition.getX() - 5, worldPosition.getY() - 1, worldPosition.getZ() - 5,
                    worldPosition.getX() + 5, worldPosition.getY() + 2, worldPosition.getZ() + 5
                )
            );
            for (LivingEntity elb : entities) {
                if (!(elb instanceof Player) && !elb.isInvulnerable()) {
                    entityId = elb.getUUID();
                    return elb;
                }
            }
        }
        return null;
    }

    @Override
    public void writeCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {

        tag.putBoolean("UC:wiggle", this.isWiggling);
        if (entityId != null)
            tag.putString("UC:targetEntity", entityId.toString());
        else
            tag.remove("UC:targetEntity");
    }

    @Override
    public void readCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {

        this.isWiggling = tag.getBoolean("UC:wiggle");
        if (tag.contains("UC:targetEntity"))
            entityId = UUID.fromString(tag.getString("UC:targetEntity"));
    }
}
