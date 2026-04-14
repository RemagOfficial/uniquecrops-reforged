package com.remag.uniquecrops.events;

import com.remag.uniquecrops.UniqueCrops;
import com.remag.uniquecrops.api.IBookUpgradeable;
import com.remag.uniquecrops.api.ICropPower;
import com.remag.uniquecrops.api.IMultiblockRecipe;
import com.remag.uniquecrops.capabilities.CPProvider;
import com.remag.uniquecrops.core.DyeUtils;
import com.remag.uniquecrops.core.NBTUtils;
import com.remag.uniquecrops.core.UCStrings;
import com.remag.uniquecrops.core.enums.EnumBonemealDye;
import com.remag.uniquecrops.core.enums.EnumLily;
import com.remag.uniquecrops.init.UCBlocks;
import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.integration.patchouli.PatchouliUtils;
import com.remag.uniquecrops.items.DyedBonemealItem;
import com.remag.uniquecrops.items.GoodieBagItem;
import com.remag.uniquecrops.items.LeagueBootsItem;
import com.remag.uniquecrops.network.PacketSyncCap;
import com.remag.uniquecrops.network.UCPacketHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.remag.uniquecrops.items.LeagueBootsItem.CMONSTEPITUP;
import static com.remag.uniquecrops.items.LeagueBootsItem.DEFAULT_SPEED;

@Mod.EventBusSubscriber(modid = UniqueCrops.MOD_ID)
public class UCEventHandlerCommon {

    public static void updateAnvilCost(AnvilUpdateEvent event) {

        ItemStack left = event.getLeft();
        Item leftItem = left.getItem();
        ItemStack right = event.getRight();
        Item rightItem = right.getItem();
        ItemStack output = event.getOutput();

        if (left.isEmpty() || right.isEmpty()) return;

        if ((leftItem == UCItems.BOOK_UPGRADE.get() && rightItem instanceof IBookUpgradeable) ||
                (leftItem instanceof IBookUpgradeable && rightItem == UCItems.BOOK_UPGRADE.get())) {
            ItemStack newOutput = (leftItem instanceof IBookUpgradeable) ? left.copy() : right.copy();
            IBookUpgradeable upgrade = ((IBookUpgradeable)newOutput.getItem());
            if (upgrade.isMaxLevel(newOutput)) return;

            if (upgrade.getLevel(newOutput) <= 0)
                upgrade.setLevel(newOutput, 1);
            else
                upgrade.setLevel(newOutput, upgrade.getLevel(newOutput) + 1);

            event.setOutput(newOutput);
            event.setCost(5);
            return;
        }

        if ((leftItem == UCItems.BOOK_DISCOUNT.get() || rightItem == UCItems.BOOK_DISCOUNT.get())) {
            ItemStack newOutput = (leftItem == UCItems.BOOK_DISCOUNT.get()) ? right.copy() : left.copy();
            if (newOutput.getItem() != Items.ENCHANTED_BOOK &&
                    (newOutput.isEnchantable() || newOutput.isEnchanted()) &&
                    !NBTUtils.getBoolean(newOutput, UCStrings.TAG_DISCOUNT, false)) {
                NBTUtils.setBoolean(newOutput, UCStrings.TAG_DISCOUNT, true);
                event.setOutput(newOutput);
                event.setCost(1);
            }
            return;
        }

    }

    public static void onBonemealEvent(BonemealEvent event) {

        if (!(event.getBlock().getBlock() instanceof GrassBlock) || event.getLevel().isClientSide()) return;
        ItemStack stack = event.getStack();
        if ((stack.getItem() instanceof DyedBonemealItem) &&
                event.getLevel().isEmptyBlock(event.getPos().above())) {
            DyeUtils.BONEMEAL_DYE.forEach((key, value) -> {
                if (value.asItem() == stack.getItem()) {
                    event.setResult(EnumBonemealDye.values()[key.ordinal()].grow(event.getLevel(), event.getPos()));
                }
            });
        }
    }

    public static void jumpTele(LivingEvent.LivingJumpEvent event) {

        LivingEntity elb = event.getEntity();
        if (elb.level().isClientSide) return;

        if (elb instanceof Player) {
            if (elb.level().getBlockState(elb.blockPosition()).getBlock() == UCBlocks.LILY_ENDER.get()) {
                EnumLily.searchNearbyPads(elb.level(), elb.blockPosition(), elb, Direction.UP);
            }
        }
    }

    public static void addSeed(BlockEvent.BreakEvent event) {

        if (event.getState().is(Blocks.GRASS) || event.getState().is(Blocks.TALL_GRASS) || event.getState().is(Blocks.FERN) || event.getState().is(Blocks.LARGE_FERN)) {
            if (event.getLevel() instanceof ServerLevel serverlevel) {
                BlockPos pos = event.getPos();
                float value = serverlevel.random.nextFloat();
                if (value > 0.90F) {
                    Containers.dropItemStack(serverlevel, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(UCItems.NORMAL_SEED.get()));
                }
                if (value > 0.92F && GoodieBagItem.isHoliday()) {
                    Containers.dropItemStack(serverlevel, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(UCItems.ADVENTUS_SEED.get()));
                }
            }
        }
    }

    public static void injectLoot(LootTableLoadEvent event) {

        if (event.getName().equals(BuiltInLootTables.WOODLAND_MANSION))
            event.getTable().addPool(getInjectPool("chests/woodland_mansion"));
        if (event.getName().equals(BuiltInLootTables.IGLOO_CHEST))
            event.getTable().addPool(getInjectPool("chests/igloo_chest"));
        if (event.getName().equals(BuiltInLootTables.SIMPLE_DUNGEON))
            event.getTable().addPool(getInjectPool("chests/simple_dungeon"));
    }

    private static LootPool getInjectPool(String pool) {

        return LootPool.lootPool()
                .add(getInjectEntry(pool, 1))
                .name("uniquecrops_inject")
                .build();
    }

    private static LootPoolEntryContainer.Builder<?> getInjectEntry(String name, int weight) {

        ResourceLocation injectFolder = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "inject/" + name);
        return LootTableReference.lootTableReference(injectFolder).setWeight(weight);
    }

    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {

        IMultiblockRecipe recipe = findRecipe(event.getLevel(), event.getPos());
        if (event.getLevel().isClientSide)
            return;
        if (recipe != null) {
            Player player = event.getEntity();
            ItemStack held = player.getItemInHand(event.getHand());
            if (!ItemStack.isSameItem(held, recipe.getCatalyst()))
                return;

            int powerNeeded = recipe.getPower();
            LazyOptional<ICropPower> cap = held.getCapability(CPProvider.CROP_POWER, null);
            if (powerNeeded <= 0) { // recipe needs to consume non-Staff catalyst item
                event.setCanceled(true);
                if (!player.isCreative())
                    held.shrink(1);
            } else {    // recipe needs to deduct Staff power
                if (!cap.isPresent()) {
                    // Odd corner case of a Wildwood Staff that has no Crop Power capacity -- should never happen
                    event.setCanceled(true);
                    player.displayClientMessage(Component.literal("Crop power is not present in this item: " + held.getDisplayName()), true);
                    return;
                }
                cap.ifPresent(crop -> {
                    if (!player.isCreative() && (crop.getPower() < powerNeeded)) {
                        player.displayClientMessage(Component.literal("Need " + powerNeeded + " Crop Power."), true);
                    } else {
                        event.setCanceled(true);
                        if (!player.isCreative())
                            crop.remove(powerNeeded);
                        if (player instanceof ServerPlayer)
                            UCPacketHandler.sendTo((ServerPlayer) player, new PacketSyncCap(crop.serializeNBT()));
                    }
                });
            }
            // I canceled the normal interaction event above ONLY IF the multiblock formation cost was paid.
            // Couldn't use a simple local boolean, it would be out of scope to the lambda. :/
            if (event.isCanceled())
                recipe.setResult(event.getLevel(), event.getPos());
            else
                event.setCanceled(true);
            player.swing(event.getHand());
        }
    }

    public static void attachItemCaps(AttachCapabilitiesEvent<ItemStack> event) {

        var stack = event.getObject();
        if (stack.getItem() == UCItems.WILDWOOD_STAFF.get()) {
            event.addCapability(ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "crop_power"), new CPProvider());
        }
    }

    private static IMultiblockRecipe findRecipe(Level level, BlockPos pos) {

        for (Recipe<?> recipe : level.getRecipeManager().getRecipes()) {
            if (recipe instanceof IMultiblockRecipe && ((IMultiblockRecipe)recipe).match(level, pos))
                return ((IMultiblockRecipe) recipe);
        }
        return null;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        Level world = player.level();

        ItemStack stack = player.getItemBySlot(EquipmentSlot.FEET); // Or HEAD, CHEST, LEGS
        if (!(stack.getItem() instanceof LeagueBootsItem leagueBootsItem)) return;

        String name = getPlayerStr(player);
        if (CMONSTEPITUP.contains(name)) {
            if (world.isClientSide) {
                float SPEED = NBTUtils.getFloat(stack, UCStrings.SPEED_MODIFIER, DEFAULT_SPEED);
                if ((player.onGround() || player.getAbilities().flying) && player.zza > 0F && !player.isInWaterOrBubble()) {
                    player.moveRelative(SPEED, new Vec3(0F, 0F, 1F));
                }

                if (player.isCrouching()) {
                    player.setMaxUpStep(0.60001F);
                } else {
                    player.setMaxUpStep(1.0625F);
                }

                leagueBootsItem.snapForward(player, stack);
            }
        } else {
            CMONSTEPITUP.add(name);
            player.setMaxUpStep(1.0625F);
        }
    }

    private static String getPlayerStr(Player player) {
        return player.getStringUUID();
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        System.out.println("Running Registering Multiblocks...");
        PatchouliUtils.registerMultiblocks();
    }
}
