package com.remag.uniquecrops.blocks.crops;

import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.init.UCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class HolyCrop extends BaseCropsBlock {

    public HolyCrop() {

        super(() -> Items.APPLE, UCItems.BLESSED_SEED, Properties.ofFullCopy(Blocks.WHEAT).lightLevel(l -> l.getValue(AGE) == 7 ? 15 : 0));
        setBonemealable(false);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {

        if (entity instanceof LivingEntity) {
            boolean blessed = isMaxAge(state);
            if (!blessed) {
                if (!(entity instanceof Villager)) return;

                VillagerProfession prof = ((Villager)entity).getVillagerData().getProfession();
                if (prof == VillagerProfession.CLERIC) {
                    world.setBlock(pos, setValueAge(getMaxAge()), 3);
                }
            }
            // Use the vanilla 'minecraft:undead' tag for robust undead detection
            TagKey<EntityType<?>> UNDEAD_TAG = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.tryParse("minecraft:undead"));
            if (entity.getType().is(UNDEAD_TAG)) {
                entity.igniteForSeconds(2);
                Holder<DamageType> magicDamage = world.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(DamageTypes.MAGIC);

                DamageSource source = new DamageSource(magicDamage);
                entity.hurt(source, blessed ? 3.0F : 1.5F);
            }
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {

        // NO-OP
    }
}
