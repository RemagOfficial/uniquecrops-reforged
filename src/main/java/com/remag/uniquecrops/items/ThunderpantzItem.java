package com.remag.uniquecrops.items;

import com.remag.uniquecrops.api.IBookUpgradeable;
import com.remag.uniquecrops.core.UCDataUtils;
import com.remag.uniquecrops.core.enums.EnumArmorMaterial;
import com.remag.uniquecrops.items.base.ItemArmorUC;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import org.jetbrains.annotations.NotNull;

public class ThunderpantzItem extends ItemArmorUC implements IBookUpgradeable {

    private static final String TAG_CHARGE = "UC:pantsCharge";
    private static final float MAX_CHARGE = 32.0F;

    public ThunderpantzItem() {

        super(EnumArmorMaterial.THUNDERPANTZ, Type.LEGGINGS);
        NeoForge.EVENT_BUS.addListener(this::onLivingAttack);
    }

    private void onLivingAttack(AttackEntityEvent event) {
        Player player = event.getEntity();

        if (event.getTarget() instanceof LivingEntity el) {
            ItemStack pants = player.getItemBySlot(EquipmentSlot.LEGS);
            if (pants.getItem() == this) {
                if (getCharge(pants) < 1F) return;

                event.setCanceled(true);
                float toDamage = getCharge(pants);
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(el.level());
                if (bolt != null) {
                    bolt.setVisualOnly(true);
                    bolt.moveTo(el.getX(), el.getY(), el.getZ());
                    player.level().addFreshEntity(bolt);
                }

                Holder<DamageType> lightningDamage = player.level().registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(DamageTypes.LIGHTNING_BOLT);

                DamageSource source = new DamageSource(lightningDamage);
                el.hurt(source, toDamage);
                setCharge(pants, 0F);
            }
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean selected) {

        super.inventoryTick(stack, world, entity, slot, selected);
        if (!(entity instanceof Player player)) return;
        if (player.getItemBySlot(EquipmentSlot.LEGS) != stack) return;

        if (world.isClientSide) return;
        if (getCharge(stack) >= MAX_CHARGE) return;

        if (player.onGround() && player.isCrouching()) {
            BlockPos pos = new BlockPos(Mth.floor(player.getX()), Mth.floor(player.getY()), Mth.floor(player.getZ()));
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof WoolCarpetBlock) {
                if (world.random.nextInt(11 - Math.max(this.getLevel(stack), 0)) == 0)
                    setCharge(stack, getCharge(stack) + world.random.nextFloat());
            }
        }
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {

        return false;
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {

        return stack.is(this);
    }

    public void setCharge(ItemStack stack, float f) {

        UCDataUtils.setFloat(stack, TAG_CHARGE, f);
    }

    public float getCharge(ItemStack stack) {

        return UCDataUtils.getFloat(stack, TAG_CHARGE, 0);
    }
}
