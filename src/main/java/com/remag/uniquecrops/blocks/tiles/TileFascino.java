package com.remag.uniquecrops.blocks.tiles;

import com.remag.uniquecrops.api.IEnchanterRecipe;
import com.remag.uniquecrops.blocks.BaseCropsBlock;
import com.remag.uniquecrops.core.UCStrings;
import com.remag.uniquecrops.core.enums.EnumParticle;
import com.remag.uniquecrops.init.UCBlocks;
import com.remag.uniquecrops.init.UCItems;
import com.remag.uniquecrops.init.UCTiles;
import com.remag.uniquecrops.items.StaffWildwoodItem;
import com.remag.uniquecrops.network.PacketUCEffect;
import com.remag.uniquecrops.network.UCPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;
import java.util.UUID;

public class TileFascino extends BaseTileUC {

    private final BlockPos[] ENCHPOS = new BlockPos[] {
            new BlockPos(0, 0, 3), new BlockPos(0, 0, -3), new BlockPos(3, 0, 0), new BlockPos(-3, 0, 0),
            new BlockPos(2, 0, 2), new BlockPos(-2, 0, -2), new BlockPos(2, 0, -2), new BlockPos(-2, 0, 2)
    };

    public TileFascino(BlockPos pos, BlockState state) {

        super(UCTiles.FASCINO.get(), pos, state);
    }

    private final ItemStackHandler inv = new ItemStackHandler(5) {
        @Override
        public int getSlotLimit(int slot) {

            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {

            setChanged();
        }
    };

    private ItemStack enchantItem = ItemStack.EMPTY;

    private final int RANGE = 7;
    private Stage stage = Stage.IDLE;
    private UUID enchanterId;
    private int enchantingTicks = 0;
    private int enchantmentCost = 7;
    private boolean showMissingCrops;

    public void tickServer() {

        if (showMissingCrops && (enchantingTicks % 3 == 0))
            loopMissingCrops();

        if (stage == Stage.IDLE) return;

        enchantingTicks++;
        stage.advance(this);
    }

    public void loopMissingCrops() {

        for (int i = 0; i < ENCHPOS.length; i++) {
            BlockPos loopPos = worldPosition.offset(ENCHPOS[i]);
            BlockState loopState = level.getBlockState(loopPos);
            if (loopState.getBlock() != UCBlocks.HEXIS_CROP.get()) {
                UCPacketHandler.sendToNearbyPlayers(level, loopPos, new PacketUCEffect(EnumParticle.ENCHANT, loopPos.getX() - 0.5D, loopPos.getY() + 0.25D, loopPos.getZ() - 0.5D, 2));
            }
        }
    }

    public void checkEnchants(Player player, ItemStack staff) {
        IEnchanterRecipe fascinoRecipe = findRecipe(level, wrap());
        if (fascinoRecipe == null) {
            player.displayClientMessage(Component.translatable("uniquecrops.enchanting.unknownrecipe"), true);
            return;
        }

        ItemStack heldItem = ItemStack.EMPTY;
        this.showMissingCrops = false;
        for (ItemStack stack : player.getHandSlots()) {
            if (!stack.isEmpty() && stack.isEnchantable() && stack.getItem() != UCItems.WILDWOOD_STAFF.get()) {
                heldItem = stack;
                break;
            }
        }
        if (heldItem.isEmpty()) {
            player.displayClientMessage(Component.translatable("uniquecrops.enchanting.nothing"), true);
            return;
        }
        if (!fascinoRecipe.getEnchantment().canEnchant(heldItem)) {
            player.displayClientMessage(Component.translatable("uniquecrops.enchanting.unenchantable", heldItem.getDisplayName()), true);
            return;
        }
        HolderLookup.RegistryLookup<Enchantment> enchLookup = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var enchantments = heldItem.getAllEnchantments(enchLookup);
        var registry = player.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        var targetKey = registry.getResourceKey(fascinoRecipe.getEnchantment()).orElse(null);
        Holder<Enchantment> targetHolder = targetKey != null ? registry.getHolder(targetKey).orElse(null) : null;
        if (targetHolder != null && enchantments.getLevel(targetHolder) > 0) {
            player.displayClientMessage(Component.translatable("uniquecrops.enchanting.enchantmentexists"), true);
            return;
        }
        for (Holder<Enchantment> enchHolder : enchantments.keySet()) {
            if (targetHolder != null && enchHolder != null && !Enchantment.areCompatible(targetHolder, enchHolder)) {
                // Use getDescription() if available, else fallback to literal
                Component desc;
                try {
                    desc = enchHolder.value().description();
                } catch (Exception e) {
                    desc = Component.literal(enchHolder.toString());
                }
                player.displayClientMessage(Component.translatable("uniquecrops.enchanting.incompatible", desc), true);
                return;
            }
        }
        // Use enchantments.keySet().size() + 1 for enchantmentSize
        prepareEnchanting(player, enchantments.keySet().size() + 1, staff, fascinoRecipe.getCost());
        enchantItem = heldItem;
    }

    // Replace wrap() to return a RecipeInput
    private RecipeInput wrap() {
        IItemHandler handler = getInventory();
        // If only one slot is used for the recipe, use the first non-empty stack
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                return new SingleRecipeInput(stack);
            }
        }
        // Fallback: empty input
        return new SingleRecipeInput(ItemStack.EMPTY);
    }

    // Update findRecipe to use RecipeInput
    private static IEnchanterRecipe findRecipe(Level world, RecipeInput input) {
        for (RecipeHolder<?> holder : world.getRecipeManager().getRecipes()) {
            Recipe<?> recipe = holder.value();
            if (recipe instanceof IEnchanterRecipe enchanterRecipe && enchanterRecipe.matches(input, world))
                return enchanterRecipe;
        }
        return null;
    }

    private void prepareEnchanting(Player player, int enchantmentSize, ItemStack staff, int powerCost) {

        int youngestAge = 7;
        for (int i = 0; i < ENCHPOS.length; i++) {
            BlockPos loopPos = worldPosition.offset(ENCHPOS[i]);
            BlockState loopState = level.getBlockState(loopPos);
            if (loopState.getBlock() == UCBlocks.HEXIS_CROP.get()) {
                int age = loopState.getValue(BaseCropsBlock.AGE);
                if (age < youngestAge)
                    youngestAge = age;
            }
            else {
                player.displayClientMessage(Component.translatable("uniquecrops.enchanting.missingcrops"), true);
                this.showMissingCrops = true;
                enchantItem = ItemStack.EMPTY;
                return;
            }
        }
        if (youngestAge < enchantmentSize) {
            player.displayClientMessage(Component.translatable("uniquecrops.enchanting.cropgrowth", enchantmentSize), true);
            enchantItem = ItemStack.EMPTY;
            return;
        }
        if (!StaffWildwoodItem.adjustPower(staff, (player.isCreative() ? 0 : powerCost))) {
            player.displayClientMessage(Component.translatable("uniquecrops.enchanting.notenoughpower", powerCost), true);
            enchantItem = ItemStack.EMPTY;
            return;
        }
        stage = Stage.PREPARE;
        enchantmentCost = enchantmentSize;
        enchanterId = player.getUUID();
        this.markBlockForUpdate();
        this.setChanged();
    }

    private Player getEnchanter() {

        Vec3 min = new Vec3(worldPosition.getX() - RANGE, worldPosition.getY() - 1, worldPosition.getZ() - RANGE);
        Vec3 max = new Vec3(worldPosition.getX() + RANGE + 1, worldPosition.getY() + 2, worldPosition.getZ() + RANGE + 1);
        List<Player> playerList = level.getEntitiesOfClass(Player.class, new AABB(min, max));
        for (Player player : playerList) {
            if (player.getUUID().equals(enchanterId)) {
                return player;
            }
        }
        return null;
    }

    public void advanceEnchanting() {

        Player player = getEnchanter();
        if (player == null) {
            advanceStage();
        }
        for (int i = 0; i < ENCHPOS.length; i++) {
            BlockPos loopPos = worldPosition.offset(ENCHPOS[i]);
            BlockState loopState = level.getBlockState(loopPos);
            if (loopState.getBlock() == UCBlocks.HEXIS_CROP.get()) {
                int age = loopState.getValue(BaseCropsBlock.AGE);
                if (age < (7 - enchantmentCost)) {
                    finishEnchanting();
                    break;
                }
                level.levelEvent(2001, loopPos, Block.getId(loopState));
                level.setBlockAndUpdate(loopPos, loopState.setValue(BaseCropsBlock.AGE, Math.max(age - 1, 0)));
            }
            else {
                advanceStage();
                break;
            }
        }
    }

    private void finishEnchanting() {

        if (enchantingTicks < 80) return;

        Player player = getEnchanter();
        if (player == null) {
            advanceStage();
            enchantItem = ItemStack.EMPTY;
            return;
        }
        ItemStack heldItem = ItemStack.EMPTY;
        for (ItemStack stack : player.getHandSlots()) {
            if (!stack.isEmpty() && stack.getItem().isEnchantable(stack) && stack.getItem() != UCItems.WILDWOOD_STAFF.get()) {
                heldItem = stack;
                break;
            }
        }
        if (heldItem.isEmpty()) {
            advanceStage();
            player.displayClientMessage(Component.translatable("uniquecrops.enchanting.nothing"), true);
            enchantItem = ItemStack.EMPTY;
            return;
        }
        if (!ItemStack.isSameItem(heldItem, enchantItem)) {
            advanceStage();
            player.displayClientMessage(Component.translatable("uniquecrops.enchanting.nomatch"), true);
            enchantItem = ItemStack.EMPTY;
            return;
        }
        IEnchanterRecipe enchanterRecipe = findRecipe(level, wrap());
        if (enchanterRecipe == null) {
            advanceStage();
            player.displayClientMessage(Component.translatable("uniquecrops.enchanting.unknownrecipe"), true);
        } else {
            enchanterRecipe.applyEnchantment(heldItem);
            this.clearInv();
            advanceStage();
            level.levelEvent(2004, getBlockPos().offset(0, 1, 0), 0);
            for (int i = 0; i < ENCHPOS.length; i++) {
                BlockPos loopPos = worldPosition.offset(ENCHPOS[i]);
                BlockState loopState = level.getBlockState(loopPos);
                if (loopState.getBlock() == UCBlocks.HEXIS_CROP.get()) {
                    int age = loopState.getValue(BaseCropsBlock.AGE);
                    level.setBlockAndUpdate(loopPos, loopState.setValue(BaseCropsBlock.AGE, Math.max(age - 1, 0)));
                }
            }
        }
        enchantItem = ItemStack.EMPTY;
    }

    public void loopEffects() {

        for (int i = 0; i < ENCHPOS.length; i++) {
            BlockPos loopPos = worldPosition.offset(ENCHPOS[i]);
            BlockState loopState = level.getBlockState(loopPos);
            if (loopState.getBlock() == UCBlocks.HEXIS_CROP.get()) {
                int size = 4;
                ((ServerLevel)level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, loopState), loopPos.getX(), (double)loopPos.getY() + 0.25D + loopState.getShape(level, loopPos).max(Direction.Axis.Y), loopPos.getZ(), size, ((double)this.getBlockPos().getX() - loopPos.getX()) / 8, 0, ((double)this.getBlockPos().getZ() - loopPos.getZ()) / 8, 0.25F);
            }
        }
    }

    public IItemHandler getInventory() {

        return this.inv;
    }

    private void clearInv() {

        for (int i = 0; i < inv.getSlots(); i++) {
            inv.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public Stage getStage() {

        return this.stage;
    }

    public void advanceStage() {

        int mod = Math.floorMod(stage.ordinal() + 1, Stage.values().length);
        stage = Stage.values()[mod];
        this.enchantingTicks = 0;
        this.markBlockForUpdate();
        this.setChanged();
    }

    @Override
    public void writeCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {
        // NeoForge 1.21.1: serializeNBT now requires HolderLookup.Provider; pass null for now
        tag.put("inventory", inv.serializeNBT(provider));
        tag.putInt(UCStrings.TAG_ENCHANTSTAGE, stage.ordinal());
        if (enchanterId != null)
            tag.putString("UC:targetEnchanter", enchanterId.toString());
        else
            tag.remove("UC:targetEnchanter");
        tag.putInt(UCStrings.TAG_ENCHANT_TIMER, this.enchantingTicks);
        tag.putInt(UCStrings.TAG_ENCHANT_COST, this.enchantmentCost);
        if (!enchantItem.isEmpty()) {
            // NeoForge 1.21.1: ItemStack NBT serialization via CODEC
            var result = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, enchantItem);
            result.result().ifPresent(nbt -> {
                if (nbt instanceof CompoundTag compound)
                    tag.put("enchItem", compound);
            });
        }
    }

    @Override
    public void readCustomNBT(CompoundTag tag, HolderLookup.Provider provider) {
        // NeoForge 1.21.1: deserializeNBT now requires HolderLookup.Provider; pass null for now
        inv.deserializeNBT(provider, tag.getCompound("inventory"));
        stage = Stage.values()[tag.getInt(UCStrings.TAG_ENCHANTSTAGE)];
        if (tag.contains("UC:targetEnchanter"))
            enchanterId = UUID.fromString(tag.getString("UC:targetEnchanter"));
        enchantingTicks = tag.getInt(UCStrings.TAG_ENCHANT_TIMER);
        enchantmentCost = tag.getInt(UCStrings.TAG_ENCHANT_COST);
        if (tag.contains("enchItem")) {
            enchantItem = ItemStack.CODEC.parse(NbtOps.INSTANCE, tag.get("enchItem")).result().orElse(ItemStack.EMPTY);
        }
    }

    public enum Stage {

        IDLE,
        PREPARE {
            @Override
            public void advance(TileFascino tile) {

                if (tile.enchantingTicks >= 20)
                    tile.advanceStage();
            }
        },
        ENCHANT {
            @Override
            public void advance(TileFascino tile) {

                if (tile.enchantingTicks % 4 == 0)
                    tile.loopEffects();
                if (tile.enchantingTicks % 20 == 0)
                    tile.advanceEnchanting();
            }
        },
        STOP {
            @Override
            public void advance(TileFascino tile) {

                tile.enchanterId = null;
                tile.advanceStage();
            }
        };

        public void advance(TileFascino tile) {}
    }
}
