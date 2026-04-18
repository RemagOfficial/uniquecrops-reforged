package com.remag.uniquecrops.core;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.component.CustomData;

import java.util.function.Consumer;

public class NBTUtils {

    /** Checks if an ItemStack has a Tag Compound **/
    public static boolean detectNBT(ItemStack stack) {
        return stack.has(DataComponents.CUSTOM_DATA);
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
        mutateNBT(stack, nbt -> nbt.putBoolean(tag, b));
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
        mutateNBT(stack, nbt -> nbt.putInt(tag, i));
    }

    public static void setLong(ItemStack stack, String tag, long l) {
        mutateNBT(stack, nbt -> nbt.putLong(tag, l));
    }

    public static void setFloat(ItemStack stack, String tag, float f) {
        mutateNBT(stack, nbt -> nbt.putFloat(tag, f));
    }

    public static void setDouble(ItemStack stack, String tag, double d) {
        mutateNBT(stack, nbt -> nbt.putDouble(tag, d));
    }

    public static void setCompound(ItemStack stack, String tag, CompoundTag cmp) {
        if(!tag.equalsIgnoreCase("ench")) // not override the enchantments
            mutateNBT(stack, nbt -> nbt.put(tag, cmp));
    }

    public static void setString(ItemStack stack, String tag, String s) {
        mutateNBT(stack, nbt -> nbt.putString(tag, s));
    }

    public static void setList(ItemStack stack, String tag, ListTag list) {
        mutateNBT(stack, nbt -> nbt.put(tag, list));
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
        return stack != null && getNBT(stack).contains(tag);
    }

    public static boolean getBoolean(ItemStack stack, String tag, boolean defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getBoolean(tag) : defaultExpected;
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
        return verifyExistance(stack, tag) ? getNBT(stack).getInt(tag) : defaultExpected;
    }

    public static long getLong(ItemStack stack, String tag, long defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getLong(tag) : defaultExpected;
    }

    public static float getFloat(ItemStack stack, String tag, float defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getFloat(tag) : defaultExpected;
    }

    public static double getDouble(ItemStack stack, String tag, double defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getDouble(tag) : defaultExpected;
    }

    /** If nullifyOnFail is true it'll return null if it doesn't find any
     * compounds, otherwise it'll return a new one. **/
    public static CompoundTag getCompound(ItemStack stack, String tag, boolean nullifyOnFail) {
        return verifyExistance(stack, tag) ? getNBT(stack).getCompound(tag) : nullifyOnFail ? null : new CompoundTag();
    }

    public static String getString(ItemStack stack, String tag, String defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getString(tag) : defaultExpected;
    }

    public static ListTag getList(ItemStack stack, String tag, int objtype, boolean nullifyOnFail) {
        return verifyExistance(stack, tag) ? getNBT(stack).getList(tag, objtype) : nullifyOnFail ? null : new ListTag();
    }
}

