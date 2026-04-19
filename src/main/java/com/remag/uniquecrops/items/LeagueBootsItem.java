package com.remag.uniquecrops.items;

import com.remag.uniquecrops.api.IBookUpgradeable;
import com.remag.uniquecrops.core.NBTUtils;
import com.remag.uniquecrops.core.UCStrings;
import com.remag.uniquecrops.core.enums.EnumArmorMaterial;
import com.remag.uniquecrops.items.base.ItemArmorUC;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;

public class LeagueBootsItem extends ItemArmorUC implements IBookUpgradeable {

    public static final float DEFAULT_SPEED = 0.055F;
    private static final float JUMP_FACTOR = 0.2F;
    private static final float FALL_BUFFER = 2F;
    private static final float STEP_HEIGHT_BONUS = 1.0F;

    private static final ResourceLocation STEP_HEIGHT_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("uniquecrops", "league_boots_step_height");

    public LeagueBootsItem() {

        super(EnumArmorMaterial.BOOTS_LEAGUE, Type.BOOTS);
        NeoForge.EVENT_BUS.addListener(this::onPlayerJump);
        NeoForge.EVENT_BUS.addListener(this::onPlayerFall);
        NeoForge.EVENT_BUS.addListener(this::playerTick);
    }

    private void onPlayerJump(LivingEvent.LivingJumpEvent event) {

        if (event.getEntity() instanceof Player) {
            ItemStack boots = event.getEntity().getItemBySlot(EquipmentSlot.FEET);
            if (boots.getItem() == this) {
                event.getEntity().setDeltaMovement(event.getEntity().getDeltaMovement().add(0, JUMP_FACTOR, 0));
                event.getEntity().fallDistance -= FALL_BUFFER;
            }
        }
    }

    private void onPlayerFall(LivingFallEvent event) {

        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) {
            ItemStack boots = entity.getItemBySlot(EquipmentSlot.FEET);
            if (boots.getItem() == this) {
                event.setDistance(Math.max(0, event.getDistance() - FALL_BUFFER));
            }
        }
    }

    private void playerTick(PlayerTickEvent.Post event) {

        Player player = event.getEntity();
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        
        if (boots.getItem() == this) {
            // Add step height modifier if not already present
            if (player.getAttributes().getInstance(Attributes.STEP_HEIGHT).getModifier(STEP_HEIGHT_MODIFIER_ID) == null) {
                AttributeModifier modifier = new AttributeModifier(
                    STEP_HEIGHT_MODIFIER_ID,
                    STEP_HEIGHT_BONUS,
                    AttributeModifier.Operation.ADD_VALUE
                );
                player.getAttributes().getInstance(Attributes.STEP_HEIGHT).addTransientModifier(modifier);
            }
        } else {
            // Remove step height modifier if boots are not equipped
            if (player.getAttributes().getInstance(Attributes.STEP_HEIGHT).getModifier(STEP_HEIGHT_MODIFIER_ID) != null) {
                player.getAttributes().getInstance(Attributes.STEP_HEIGHT).removeModifier(STEP_HEIGHT_MODIFIER_ID);
            }
        }
    }

    public void snapForward(Player player, ItemStack stack) {

//        if (player.world.provider.getDimension() == UCDimension.dimID) return;

        float speedMod = 0.95F;
        int sprintTicks = NBTUtils.getInt(stack, UCStrings.SPRINTING_TICKS, 0);
        if (sprintTicks > 0) {
            NBTUtils.setInt(stack, UCStrings.SPRINTING_TICKS, sprintTicks - 1);
            return;
        }
        if (player.isSprinting() && !player.getAbilities().flying &&
                !player.isSwimming() && !player.isInLava()) {
            if (NBTUtils.getFloat(stack, UCStrings.SPEED_MODIFIER, DEFAULT_SPEED) == DEFAULT_SPEED) {
                NBTUtils.setFloat(stack, UCStrings.SPEED_MODIFIER, speedMod * Math.max(getLevel(stack), 1));
                return;
            }
            else {
                player.setSprinting(false);
                NBTUtils.setInt(stack, UCStrings.SPRINTING_TICKS, 20);
            }
        }
        if (!player.isSprinting()) {
            NBTUtils.setFloat(stack, UCStrings.SPEED_MODIFIER, DEFAULT_SPEED);
        }
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {

        return false;
    }

    /*@Override
    public Rarity getRarity(ItemStack stack) {

        return Rarity.EPIC;
    }*/
}
