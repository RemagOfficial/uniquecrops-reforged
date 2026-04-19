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
import com.remag.uniquecrops.init.UCTiles;
import com.remag.uniquecrops.integration.patchouli.PatchouliUtils;
import com.remag.uniquecrops.items.DyedBonemealItem;
import com.remag.uniquecrops.items.GoodieBagItem;
import com.remag.uniquecrops.items.LeagueBootsItem;
import com.remag.uniquecrops.network.PacketSyncCap;
import com.remag.uniquecrops.network.UCPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static com.remag.uniquecrops.items.LeagueBootsItem.DEFAULT_SPEED;

@EventBusSubscriber(modid = UniqueCrops.MOD_ID)
public class UCEventHandlerCommon {

    public static void updateAnvilCost(AnvilUpdateEvent event) {

        ItemStack left = event.getLeft();
        Item leftItem = left.getItem();
        ItemStack right = event.getRight();
        Item rightItem = right.getItem();

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

        if (!(event.getLevel().getBlockState(event.getPos()).getBlock() instanceof GrassBlock) || event.getLevel().isClientSide()) return;
        ItemStack stack = event.getStack();
        if ((stack.getItem() instanceof DyedBonemealItem) &&
                event.getLevel().isEmptyBlock(event.getPos().above())) {
            DyeUtils.BONEMEAL_DYE.forEach((key, value) -> {
                if (value.asItem() == stack.getItem()) {
                    EnumBonemealDye.values()[key.ordinal()].grow(event.getLevel(), event.getPos());
                    event.setCanceled(true);
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

        if (event.getState().is(Blocks.SHORT_GRASS) || event.getState().is(Blocks.TALL_GRASS) || event.getState().is(Blocks.FERN) || event.getState().is(Blocks.LARGE_FERN)) {
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

        if (event.getName().equals(BuiltInLootTables.WOODLAND_MANSION.location()))
            event.getTable().addPool(getInjectPool("chests/woodland_mansion"));
        if (event.getName().equals(BuiltInLootTables.IGLOO_CHEST.location()))
            event.getTable().addPool(getInjectPool("chests/igloo_chest"));
        if (event.getName().equals(BuiltInLootTables.SIMPLE_DUNGEON.location()))
            event.getTable().addPool(getInjectPool("chests/simple_dungeon"));
    }

    private static LootPool getInjectPool(String pool) {

        return LootPool.lootPool()
                .add(getInjectEntry(pool))
                .name("uniquecrops_inject")
                .build();
    }

    private static LootPoolEntryContainer.Builder<?> getInjectEntry(String name) {

        ResourceLocation injectFolder = ResourceLocation.fromNamespaceAndPath(UniqueCrops.MOD_ID, "inject/" + name);
        ResourceKey<net.minecraft.world.level.storage.loot.LootTable> injectKey = ResourceKey.create(Registries.LOOT_TABLE, injectFolder);
        return NestedLootTable.lootTableReference(injectKey).setWeight(1);
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
            Object capability = held.getCapability(CPProvider.CROP_POWER, null);
            if (powerNeeded <= 0) {
                event.setCanceled(true);
                if (!player.isCreative())
                    held.shrink(1);
            } else {
                if (!(capability instanceof ICropPower crop)) {
                    event.setCanceled(true);
                    player.displayClientMessage(Component.literal("Crop power is not present in this item: " + held.getDisplayName()), true);
                    return;
                }
                if (!player.isCreative() && (crop.getPower() < powerNeeded)) {
                    player.displayClientMessage(Component.literal("Need " + powerNeeded + " Crop Power."), true);
                } else {
                    event.setCanceled(true);
                    if (!player.isCreative())
                        crop.remove(powerNeeded);
                    if (player instanceof ServerPlayer serverPlayer) {
                        CompoundTag syncTag = new CompoundTag();
                        syncTag.putInt("UC:cropPowerCapacity", crop.getCapacity());
                        syncTag.putInt("UC:cropPowerCurrent", crop.getPower());
                        syncTag.putInt("UC:cropPowerCooldown", crop.getCooldown());
                        syncTag.putBoolean("UC:hasCooldown", !crop.hasCooldown());
                        UCPacketHandler.sendTo(serverPlayer, new PacketSyncCap(syncTag));
                    }
                }
            }
            if (event.isCanceled())
                recipe.setResult(event.getLevel(), event.getPos());
            else
                event.setCanceled(true);
            player.swing(event.getHand());
        }
    }

    private static IMultiblockRecipe findRecipe(Level level, BlockPos pos) {

        for (RecipeHolder<?> holder : level.getRecipeManager().getRecipes()) {
            Recipe<?> recipe = holder.value();
            if (recipe instanceof IMultiblockRecipe multi && multi.match(level, pos))
                return multi;
        }
        return null;
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {

        Player player = event.getEntity();
        Level world = player.level();

        ItemStack stack = player.getItemBySlot(EquipmentSlot.FEET);
        if (!(stack.getItem() instanceof LeagueBootsItem leagueBootsItem)) return;

        if (world.isClientSide) {
            float SPEED = NBTUtils.getFloat(stack, UCStrings.SPEED_MODIFIER, DEFAULT_SPEED);
            if ((player.onGround() || player.getAbilities().flying) && player.zza > 0F && !player.isInWaterOrBubble()) {
                player.moveRelative(SPEED, new Vec3(0F, 0F, 1F));
            }

            leagueBootsItem.snapForward(player, stack);
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

    @SubscribeEvent
    public static void registerBlockEntityCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, UCTiles.BARREL.get(), (be, direction) -> be.getInventory());
    }

    @SubscribeEvent
    public static void registerItemCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(com.remag.uniquecrops.capabilities.CPProvider.CROP_POWER, new com.remag.uniquecrops.capabilities.CPProvider(), com.remag.uniquecrops.init.UCItems.IMPREGNATED_LEATHER.get());
    }
}
