package com.remag.uniquecrops.core;

import com.remag.uniquecrops.core.data.BlockPosData;
import com.remag.uniquecrops.core.data.CompoundTagData;
import com.remag.uniquecrops.core.data.ListTagData;
import com.remag.uniquecrops.core.data.UUIDData;
import com.remag.uniquecrops.init.UCDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.component.CustomData;

import java.util.UUID;
import java.util.function.Consumer;

public class UCDataUtils {

    /** Checks if an ItemStack has any custom data component **/
    public static boolean detectNBT(ItemStack stack) {
        return stack.has(DataComponents.CUSTOM_DATA) || hasAnyDataComponent(stack);
    }

    private static boolean hasAnyDataComponent(ItemStack stack) {
        return stack.has(UCDataComponents.TAG_UPGRADE.get()) ||
               stack.has(UCDataComponents.BOOKMARK.get()) ||
               stack.has(UCDataComponents.IMPERIA_POS_TAG.get()) ||
               stack.has(UCDataComponents.TAG_BIOME.get()) ||
               stack.has(UCDataComponents.TAG_LOCK.get()) ||
               stack.has(UCDataComponents.TAG_DISCOUNT.get()) ||
               stack.has(UCDataComponents.CROP_POWER_CURRENT.get()) ||
               stack.has(UCDataComponents.IS_ACTIVE.get()) ||
               stack.has(UCDataComponents.ORE_POS.get()) ||
               stack.has(UCDataComponents.TAG_GROWTHSTAGES.get()) ||
               stack.has(UCDataComponents.TAG_DIAMONDS.get()) ||
               stack.has(UCDataComponents.UC_ENERGY.get()) ||
               stack.has(UCDataComponents.TAG_CHARGE.get()) ||
               stack.has(UCDataComponents.TAG_CUBE_ROTATION.get()) ||
               stack.has(UCDataComponents.CUBE_ROTATION_DATA.get()) ||
               stack.has(UCDataComponents.SPAWNER_DATA.get()) ||
               stack.has(UCDataComponents.ARMOR_COUNT.get()) ||
               stack.has(UCDataComponents.HIT_LIST.get()) ||
               stack.has(UCDataComponents.ITEM_ACTIVATED.get()) ||
               stack.has(UCDataComponents.DAMAGE_POOL.get()) ||
               stack.has(UCDataComponents.SPRINTING_TICKS.get()) ||
               stack.has(UCDataComponents.SPEED_MODIFIER.get()) ||
               stack.has(UCDataComponents.TAG_CURIO_UUID_MOST.get()) ||
               stack.has(UCDataComponents.TAG_CURIO_UUID_LEAST.get());
    }

    /** Tries to initialize an NBT Tag Compound in an ItemStack,
     * this will not do anything if the stack already has a tag
     * compound **/
    public static void initNBT(ItemStack stack) {
        if(!detectNBT(stack))
            injectNBT(stack, new CompoundTag());
    }

    /** Injects an NBT Tag Compound to an ItemStack, no checks
     * are made previously **/
    public static void injectNBT(ItemStack stack, CompoundTag nbt) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
    }

    /** Gets the NBTTagCompound in an ItemStack. Tries to init it
     * previously in case there isn't one present **/
    public static CompoundTag getNBT(ItemStack stack) {
        initNBT(stack);
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    private static void mutateNBT(ItemStack stack, Consumer<CompoundTag> mutator) {
        CompoundTag tag = getNBT(stack);
        mutator.accept(tag);
        injectNBT(stack, tag);
    }

    // SETTERS ///////////////////////////////////////////////////////////////////

    public static void setBoolean(ItemStack stack, String tag, boolean b) {
        // Map tag names to data components
        switch (tag) {
            case UCStrings.BOOKMARK:
                stack.set(UCDataComponents.BOOKMARK.get(), b);
                break;
            case UCStrings.TAG_DISCOUNT:
                stack.set(UCDataComponents.TAG_DISCOUNT.get(), b);
                break;
            case "isActive":
                stack.set(UCDataComponents.IS_ACTIVE.get(), b);
                break;
            case UCStrings.ITEM_ACTIVATED:
                stack.set(UCDataComponents.ITEM_ACTIVATED.get(), b);
                break;
            default:
                mutateNBT(stack, nbt -> nbt.putBoolean(tag, b));
        }
    }

    public static void setByte(ItemStack stack, String tag, byte b) {
        mutateNBT(stack, nbt -> nbt.putByte(tag, b));
    }

    public static void setByteArray(ItemStack stack, String tag, byte[] b) {
        mutateNBT(stack, nbt -> nbt.putByteArray(tag, b));
    }

    public static void setShort(ItemStack stack, String tag, short s) {
        mutateNBT(stack, nbt -> nbt.putShort(tag, s));
    }

    public static void setInt(ItemStack stack, String tag, int i) {
        // Map tag names to data components
        switch (tag) {
            case UCStrings.TAG_UPGRADE:
                stack.set(UCDataComponents.TAG_UPGRADE.get(), i);
                break;
            case "UC:cropPowerCurrent":
                stack.set(UCDataComponents.CROP_POWER_CURRENT.get(), i);
                break;
            case UCStrings.TAG_DIAMONDS:
                stack.set(UCDataComponents.TAG_DIAMONDS.get(), i);
                break;
            case "UC_Energy":
            case "UC_energy":
                stack.set(UCDataComponents.UC_ENERGY.get(), i);
                break;
            case UCStrings.TAG_CUBE_ROTATION:
                stack.set(UCDataComponents.TAG_CUBE_ROTATION.get(), i);
                break;
            case "ARMORCOUNT":
                stack.set(UCDataComponents.ARMOR_COUNT.get(), i);
                break;
            case UCStrings.SPRINTING_TICKS:
                stack.set(UCDataComponents.SPRINTING_TICKS.get(), i);
                break;
            default:
                mutateNBT(stack, nbt -> nbt.putInt(tag, i));
        }
    }

    public static void setLong(ItemStack stack, String tag, long l) {
        // Map tag names to data components
        switch (tag) {
            case "TAG_CURIO_UUID_MOST":
                stack.set(UCDataComponents.TAG_CURIO_UUID_MOST.get(), l);
                break;
            case "TAG_CURIO_UUID_LEAST":
                stack.set(UCDataComponents.TAG_CURIO_UUID_LEAST.get(), l);
                break;
            case "orePos":
                stack.set(UCDataComponents.ORE_POS.get(), new BlockPosData(BlockPos.of(l)));
                break;
            default:
                mutateNBT(stack, nbt -> nbt.putLong(tag, l));
        }
    }

    public static void setFloat(ItemStack stack, String tag, float f) {
        // Map tag names to data components
        switch (tag) {
            case "TAG_CHARGE":
                stack.set(UCDataComponents.TAG_CHARGE.get(), f);
                break;
            case "DAMAGE_POOL":
                stack.set(UCDataComponents.DAMAGE_POOL.get(), f);
                break;
            case UCStrings.SPEED_MODIFIER:
                stack.set(UCDataComponents.SPEED_MODIFIER.get(), f);
                break;
            default:
                mutateNBT(stack, nbt -> nbt.putFloat(tag, f));
        }
    }

    public static void setDouble(ItemStack stack, String tag, double d) {
        mutateNBT(stack, nbt -> nbt.putDouble(tag, d));
    }

    public static void setCompound(ItemStack stack, String tag, CompoundTag cmp) {
        if(!tag.equalsIgnoreCase("ench")) { // not override the enchantments
            // Map tag names to data components
            if (tag.startsWith(UCStrings.TAG_CUBE_ROTATION)) {
                stack.set(UCDataComponents.CUBE_ROTATION_DATA.get(), new CompoundTagData(cmp));
            } else if (tag.equals("Spawner")) {
                stack.set(UCDataComponents.SPAWNER_DATA.get(), new CompoundTagData(cmp));
            } else {
                mutateNBT(stack, nbt -> nbt.put(tag, cmp));
            }
        }
    }

    public static void setString(ItemStack stack, String tag, String s) {
        // Map tag names to data components
        switch (tag) {
            case UCStrings.TAG_BIOME:
                stack.set(UCDataComponents.TAG_BIOME.get(), s);
                break;
            case UCStrings.TAG_LOCK:
                try {
                    UUID uuid = UUID.fromString(s);
                    stack.set(UCDataComponents.TAG_LOCK.get(), new UUIDData(uuid));
                } catch (IllegalArgumentException e) {
                    mutateNBT(stack, nbt -> nbt.putString(tag, s));
                }
                break;
            default:
                mutateNBT(stack, nbt -> nbt.putString(tag, s));
        }
    }

    public static void setList(ItemStack stack, String tag, ListTag list) {
        // Map tag names to data components
        switch (tag) {
            case UCStrings.TAG_GROWTHSTAGES:
                stack.set(UCDataComponents.TAG_GROWTHSTAGES.get(), new ListTagData(list));
                break;
            case "HIT_LIST":
                stack.set(UCDataComponents.HIT_LIST.get(), new ListTagData(list));
                break;
            default:
                mutateNBT(stack, nbt -> nbt.put(tag, list));
        }
    }

    public static void setIntArray(ItemStack stack, String key, int[] val) {
        mutateNBT(stack, nbt -> {
            CompoundTag compound = nbt.contains(key) ? nbt.getCompound(key) : new CompoundTag();
            compound.putIntArray(key, val);
            nbt.put(key, compound);
        });
    }

    // GETTERS ///////////////////////////////////////////////////////////////////

    public static int[] getIntArray(ItemStack stack, String key) {
        CompoundTag compound = detectNBT(stack) ? getCompound(stack, key, true) : null;
        return compound != null ? compound.getIntArray(key) : new int[0];
    }

    public static boolean verifyExistance(ItemStack stack, String tag) {
        if (stack == null) return false;
        // Check data components first
        switch (tag) {
            case UCStrings.BOOKMARK:
                return stack.has(UCDataComponents.BOOKMARK.get());
            case UCStrings.TAG_DISCOUNT:
                return stack.has(UCDataComponents.TAG_DISCOUNT.get());
            case "isActive":
                return stack.has(UCDataComponents.IS_ACTIVE.get());
            case UCStrings.ITEM_ACTIVATED:
                return stack.has(UCDataComponents.ITEM_ACTIVATED.get());
            case UCStrings.TAG_UPGRADE:
                return stack.has(UCDataComponents.TAG_UPGRADE.get());
            case "UC:cropPowerCurrent":
                return stack.has(UCDataComponents.CROP_POWER_CURRENT.get());
            case UCStrings.TAG_DIAMONDS:
                return stack.has(UCDataComponents.TAG_DIAMONDS.get());
            case "UC_Energy":
            case "UC_energy":
                return stack.has(UCDataComponents.UC_ENERGY.get());
            case UCStrings.TAG_CUBE_ROTATION:
                return stack.has(UCDataComponents.TAG_CUBE_ROTATION.get());
            case "ARMORCOUNT":
                return stack.has(UCDataComponents.ARMOR_COUNT.get());
            case UCStrings.SPRINTING_TICKS:
                return stack.has(UCDataComponents.SPRINTING_TICKS.get());
            case UCStrings.TAG_BIOME:
                return stack.has(UCDataComponents.TAG_BIOME.get());
            case UCStrings.TAG_LOCK:
                return stack.has(UCDataComponents.TAG_LOCK.get());
            case "TAG_CHARGE":
                return stack.has(UCDataComponents.TAG_CHARGE.get());
            case "DAMAGE_POOL":
                return stack.has(UCDataComponents.DAMAGE_POOL.get());
            case UCStrings.SPEED_MODIFIER:
                return stack.has(UCDataComponents.SPEED_MODIFIER.get());
            case UCStrings.TAG_GROWTHSTAGES:
                return stack.has(UCDataComponents.TAG_GROWTHSTAGES.get());
            case "HIT_LIST":
                return stack.has(UCDataComponents.HIT_LIST.get());
            case "orePos":
                return stack.has(UCDataComponents.ORE_POS.get());
            case "TAG_CURIO_UUID_MOST":
                return stack.has(UCDataComponents.TAG_CURIO_UUID_MOST.get());
            case "TAG_CURIO_UUID_LEAST":
                return stack.has(UCDataComponents.TAG_CURIO_UUID_LEAST.get());
            default:
                return getNBT(stack).contains(tag);
        }
    }

    public static boolean getBoolean(ItemStack stack, String tag, boolean defaultExpected) {
        switch (tag) {
            case UCStrings.BOOKMARK:
                return stack.getOrDefault(UCDataComponents.BOOKMARK.get(), defaultExpected);
            case UCStrings.TAG_DISCOUNT:
                return stack.getOrDefault(UCDataComponents.TAG_DISCOUNT.get(), defaultExpected);
            case "isActive":
                return stack.getOrDefault(UCDataComponents.IS_ACTIVE.get(), defaultExpected);
            case UCStrings.ITEM_ACTIVATED:
                return stack.getOrDefault(UCDataComponents.ITEM_ACTIVATED.get(), defaultExpected);
            default:
                return verifyExistance(stack, tag) ? getNBT(stack).getBoolean(tag) : defaultExpected;
        }
    }

    public static byte getByte(ItemStack stack, String tag, byte defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getByte(tag) : defaultExpected;
    }

    public static byte[] getByteArray(ItemStack stack, String tag) {
        return verifyExistance(stack, tag) ? getNBT(stack).getByteArray(tag) : null;
    }

    public static short getShort(ItemStack stack, String tag, short defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getShort(tag) : defaultExpected;
    }

    public static int getInt(ItemStack stack, String tag, int defaultExpected) {
        switch (tag) {
            case UCStrings.TAG_UPGRADE:
                return stack.getOrDefault(UCDataComponents.TAG_UPGRADE.get(), defaultExpected);
            case "UC:cropPowerCurrent":
                return stack.getOrDefault(UCDataComponents.CROP_POWER_CURRENT.get(), defaultExpected);
            case UCStrings.TAG_DIAMONDS:
                return stack.getOrDefault(UCDataComponents.TAG_DIAMONDS.get(), defaultExpected);
            case "UC_Energy":
            case "UC_energy":
                return stack.getOrDefault(UCDataComponents.UC_ENERGY.get(), defaultExpected);
            case UCStrings.TAG_CUBE_ROTATION:
                return stack.getOrDefault(UCDataComponents.TAG_CUBE_ROTATION.get(), defaultExpected);
            case "ARMORCOUNT":
                return stack.getOrDefault(UCDataComponents.ARMOR_COUNT.get(), defaultExpected);
            case UCStrings.SPRINTING_TICKS:
                return stack.getOrDefault(UCDataComponents.SPRINTING_TICKS.get(), defaultExpected);
            default:
                return verifyExistance(stack, tag) ? getNBT(stack).getInt(tag) : defaultExpected;
        }
    }

    public static long getLong(ItemStack stack, String tag, long defaultExpected) {
        switch (tag) {
            case "TAG_CURIO_UUID_MOST":
                return stack.getOrDefault(UCDataComponents.TAG_CURIO_UUID_MOST.get(), defaultExpected);
            case "TAG_CURIO_UUID_LEAST":
                return stack.getOrDefault(UCDataComponents.TAG_CURIO_UUID_LEAST.get(), defaultExpected);
            case "orePos":
                BlockPosData posData = stack.get(UCDataComponents.ORE_POS.get());
                return posData != null ? posData.pos().asLong() : defaultExpected;
            default:
                return verifyExistance(stack, tag) ? getNBT(stack).getLong(tag) : defaultExpected;
        }
    }

    public static float getFloat(ItemStack stack, String tag, float defaultExpected) {
        switch (tag) {
            case "TAG_CHARGE":
                return stack.getOrDefault(UCDataComponents.TAG_CHARGE.get(), defaultExpected);
            case "DAMAGE_POOL":
                return stack.getOrDefault(UCDataComponents.DAMAGE_POOL.get(), defaultExpected);
            case UCStrings.SPEED_MODIFIER:
                return stack.getOrDefault(UCDataComponents.SPEED_MODIFIER.get(), defaultExpected);
            default:
                return verifyExistance(stack, tag) ? getNBT(stack).getFloat(tag) : defaultExpected;
        }
    }

    public static double getDouble(ItemStack stack, String tag, double defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getDouble(tag) : defaultExpected;
    }

    /** If nullifyOnFail is true it'll return null if it doesn't find any
     * compounds, otherwise it'll return a new one. **/
    public static CompoundTag getCompound(ItemStack stack, String tag, boolean nullifyOnFail) {
        // Map tag names to data components
        if (tag.startsWith(UCStrings.TAG_CUBE_ROTATION)) {
            CompoundTagData data = stack.get(UCDataComponents.CUBE_ROTATION_DATA.get());
            return data != null ? data.tag() : (nullifyOnFail ? null : new CompoundTag());
        }
        if (tag.equals("Spawner")) {
            CompoundTagData data = stack.get(UCDataComponents.SPAWNER_DATA.get());
            return data != null ? data.tag() : (nullifyOnFail ? null : new CompoundTag());
        }
        return verifyExistance(stack, tag) ? getNBT(stack).getCompound(tag) : nullifyOnFail ? null : new CompoundTag();
    }

    public static String getString(ItemStack stack, String tag, String defaultExpected) {
        switch (tag) {
            case UCStrings.TAG_BIOME:
                return stack.getOrDefault(UCDataComponents.TAG_BIOME.get(), defaultExpected);
            case UCStrings.TAG_LOCK:
                UUIDData uuidData = stack.get(UCDataComponents.TAG_LOCK.get());
                return uuidData != null ? uuidData.uuid().toString() : defaultExpected;
            default:
                return verifyExistance(stack, tag) ? getNBT(stack).getString(tag) : defaultExpected;
        }
    }

    public static ListTag getList(ItemStack stack, String tag, int objtype, boolean nullifyOnFail) {
        switch (tag) {
            case UCStrings.TAG_GROWTHSTAGES:
                ListTagData growthData = stack.get(UCDataComponents.TAG_GROWTHSTAGES.get());
                return growthData != null ? growthData.tag() : (nullifyOnFail ? null : new ListTag());
            case "HIT_LIST":
                ListTagData hitData = stack.get(UCDataComponents.HIT_LIST.get());
                return hitData != null ? hitData.tag() : (nullifyOnFail ? null : new ListTag());
            default:
                return verifyExistance(stack, tag) ? getNBT(stack).getList(tag, objtype) : nullifyOnFail ? null : new ListTag();
        }
    }
}
