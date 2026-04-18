package com.remag.uniquecrops.items;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.items.base.ItemBaseUC;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EnderSnookerItem extends ItemBaseUC {

    public EnderSnookerItem() {

        super(UCItems.defaultBuilder().durability(16));
    }

    @Override
    public boolean isFoil(ItemStack stack) {

        return true;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext ctx) {

        //TODO: fix weirdness
        Player player = ctx.getPlayer();
        if (player != null && player.isCrouching()) {
            Level lvl = ctx.getLevel();
            BlockState state = lvl.getBlockState(ctx.getClickedPos());
            ResourceLocation darkBlockId = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "dark_block");
            if (BuiltInRegistries.BLOCK.getKey(state.getBlock()).equals(darkBlockId)) {
                if (!lvl.isClientSide) {
                    if (ctx.getClickedPos().getY() <= lvl.getMinBuildHeight()+1)
                        lvl.setBlock(ctx.getClickedPos(), Blocks.BEDROCK.defaultBlockState(), 2);
                    else
                        lvl.removeBlock(ctx.getClickedPos(), false);
                }
                player.addItem(new ItemStack(BuiltInRegistries.ITEM.getOptional(darkBlockId).orElse(Items.AIR)));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand) {

        if (hand != InteractionHand.MAIN_HAND) return InteractionResultHolder.pass(player.getItemInHand(hand));

        List<LivingEntity> elb = acquireAllLookTargets(player, 32, 2);
        for (LivingEntity target : elb) {
            if (target.hasLineOfSight(player) && target.isPickable()) {
                BlockPos targetPos = target.blockPosition();
                BlockPos playerPos = player.blockPosition();
                if (!world.isClientSide) {
                    target.teleportTo(playerPos.getX(), playerPos.getY(), playerPos.getZ());
                    player.teleportTo(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                    if (target instanceof Wolf && world.random.nextInt(100) == 0)
                        target.spawnAtLocation(new ItemStack(BuiltInRegistries.ITEM.getOptional(ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "dogresidue")).orElse(Items.AIR)));
                    if (!player.isCreative() && player instanceof ServerPlayer serverPlayer)
                        player.getItemInHand(hand).hurtAndBreak(1, serverPlayer, serverPlayer.getEquipmentSlotForItem(player.getItemInHand(hand)));
                }
                return InteractionResultHolder.consume(player.getItemInHand(hand));
            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    /* Thanks to Coolalias for all code below this line */
    private static final int MAX_DISTANCE = 256;

    private List<LivingEntity> acquireAllLookTargets(LivingEntity seeker, int distance, double radius) {

        if (distance < 0 || distance > MAX_DISTANCE) {
            distance = MAX_DISTANCE;
        }
        List<LivingEntity> targets = new ArrayList<>();
        Vec3 vec3 = seeker.getViewVector(1.0F).normalize();
        double targetX = seeker.getX();
        double targetY = seeker.getY() + seeker.getEyeHeight() - 0.10000000149011612D;
        double targetZ = seeker.getZ();
        double distanceTraveled = 0;

        while ((int) distanceTraveled < distance) {
            targetX += vec3.x;
            targetY += vec3.y;
            targetZ += vec3.z;
            distanceTraveled += vec3.length();
            AABB bb = new AABB(targetX-radius, targetY-radius, targetZ-radius, targetX+radius, targetY+radius, targetZ+radius);
            List<LivingEntity> list = seeker.level().getEntitiesOfClass(LivingEntity.class, bb);
            for (LivingEntity target : list) {
                if (target == seeker || target instanceof Player) continue;
                if (target.isPushable() && isTargetInSight(seeker, target)) {
                    if (!targets.contains(target)) {
                        targets.add(target);
                    }
                }
            }
        }
        return targets;
    }

    private boolean isTargetInSight(LivingEntity seeker, Entity target) {

        return seeker.hasLineOfSight(target) && isTargetInFrontOf(seeker, target, 60);
    }

    private boolean isTargetInFrontOf(Entity seeker, Entity target, float fov) {
        // thanks again to Battlegear2 for the following code snippet
        double dx = target.getY() - seeker.getY();
        double dz;
        for (dz = target.getZ() - seeker.getZ(); dx * dx + dz * dz < 1.0E-4D; dz = (Math.random() - Math.random()) * 0.01D) {
            dx = (Math.random() - Math.random()) * 0.01D;
        }
        while (seeker.yRotO > 360) { seeker.yRotO -= 360; }
        while (seeker.yRotO < -360) { seeker.yRotO += 360; }
        float yaw = (float)(Math.atan2(dz, dx) * 180.0D / Math.PI) - seeker.yRotO;
        yaw = yaw - 90;
        while (yaw < -180) { yaw += 360; }
        while (yaw >= 180) { yaw -= 360; }

        return yaw < fov && yaw > -fov;
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {

        @Nullable var lilyTwine = BuiltInRegistries.ITEM.getOptional(ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "lilytwine")).orElse(null);
        return lilyTwine != null && repair.is(lilyTwine);
    }
}
