package net.lomeli.trophyslots.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.List;

public class NBTUtils {
    private static CompoundNBT getItemTag(ItemStack stack) {
        if (!stack.hasTag()) stack.setTag(new CompoundNBT());
        return stack.getTag();
    }

    public static boolean hasTag(ItemStack stack, String tagName, int type) {
        return getItemTag(stack).contains(tagName, type);
    }

    public static boolean hasTag(ItemStack stack, String tagName) {
        return getItemTag(stack).contains(tagName);
    }

    public static void removeTag(ItemStack stack, String tagName) {
        getItemTag(stack).remove(tagName);
    }

    public static byte getByte(ItemStack stack, String tagName) {
        CompoundNBT tag = getItemTag(stack);
        return tag.getByte(tagName);
    }

    public static void setByte(ItemStack stack, String tagName, byte value) {
        CompoundNBT tag = getItemTag(stack);
        tag.putByte(tagName, value);
        stack.setTag(tag);
    }

    public static short getShort(ItemStack stack, String tagName) {
        CompoundNBT tag = getItemTag(stack);
        return tag.getShort(tagName);
    }

    public static void setShort(ItemStack stack, String tagName, short value) {
        CompoundNBT tag = getItemTag(stack);
        tag.putShort(tagName, value);
        stack.setTag(tag);
    }

    public static int getInt(ItemStack stack, String tagName) {
        CompoundNBT tag = getItemTag(stack);
        return tag.getInt(tagName);
    }

    public static void setInt(ItemStack stack, String tagName, int value) {
        CompoundNBT tag = getItemTag(stack);
        tag.putInt(tagName, value);
        stack.setTag(tag);
    }

    public static long getLong(ItemStack stack, String tagName) {
        CompoundNBT tag = getItemTag(stack);
        return tag.getLong(tagName);
    }

    public static void setLong(ItemStack stack, String tagName, long value) {
        CompoundNBT tag = getItemTag(stack);
        tag.putLong(tagName, value);
        stack.setTag(tag);
    }

    public static float getFloat(ItemStack stack, String tagName) {
        CompoundNBT tag = getItemTag(stack);
        return tag.getFloat(tagName);
    }

    public static void setFloat(ItemStack stack, String tagName, float value) {
        CompoundNBT tag = getItemTag(stack);
        tag.putFloat(tagName, value);
        stack.setTag(tag);
    }

    public static double getDouble(ItemStack stack, String tagName) {
        CompoundNBT tag = getItemTag(stack);
        return tag.getDouble(tagName);
    }

    public static void setDouble(ItemStack stack, String tagName, double value) {
        CompoundNBT tag = getItemTag(stack);
        tag.putDouble(tagName, value);
        stack.setTag(tag);
    }

    public static boolean getBoolean(ItemStack stack, String tagName) {
        CompoundNBT tag = getItemTag(stack);
        return tag.getBoolean(tagName);
    }

    public static void setBoolean(ItemStack stack, String tagName, boolean value) {
        CompoundNBT tag = getItemTag(stack);
        tag.putBoolean(tagName, value);
        stack.setTag(tag);
    }

    public static String getString(ItemStack stack, String tagName) {
        CompoundNBT tag = getItemTag(stack);
        return tag.getString(tagName);
    }

    public static void setString(ItemStack stack, String tagName, String value) {
        CompoundNBT tag = getItemTag(stack);
        tag.putString(tagName, value);
        stack.setTag(tag);
    }

    public static byte[] getByteArray(ItemStack stack, String tagName) {
        CompoundNBT tag = getItemTag(stack);
        return tag.getByteArray(tagName);
    }

    public static void setByteArray(ItemStack stack, String tagName, byte[] value) {
        CompoundNBT tag = getItemTag(stack);
        tag.putByteArray(tagName, value);
        stack.setTag(tag);
    }

    public static int[] getIntArray(ItemStack stack, String tagName) {
        CompoundNBT tag = getItemTag(stack);
        return tag.getIntArray(tagName);
    }

    public static void setIntArray(ItemStack stack, String tagName, int[] value) {
        CompoundNBT tag = getItemTag(stack);
        tag.putIntArray(tagName, value);
        stack.setTag(tag);
    }

    public static void setIntArray(ItemStack stack, String tagName, List<Integer> value) {
        CompoundNBT tag = getItemTag(stack);
        tag.putIntArray(tagName, value);
        stack.setTag(tag);
    }

    public static long[] getLongArray(ItemStack stack, String tagName) {
        CompoundNBT tag = getItemTag(stack);
        return tag.getLongArray(tagName);
    }

    public static void setLongArray(ItemStack stack, String tagName, long[] value) {
        CompoundNBT tag = getItemTag(stack);
        tag.putLongArray(tagName, value);
        stack.setTag(tag);
    }

    public static void setLongArray(ItemStack stack, String tagName, List<Long> value) {
        CompoundNBT tag = getItemTag(stack);
        tag.putLongArray(tagName, value);
        stack.setTag(tag);
    }

    public static CompoundNBT getCompoundTag(ItemStack stack, String tagName) {
        CompoundNBT tag = getItemTag(stack);
        return tag.getCompound(tagName);
    }

    public static void setCompoundTag(ItemStack stack, String tagName, CompoundNBT value) {
        CompoundNBT tag = getItemTag(stack);
        tag.put(tagName, value);
        stack.setTag(tag);
    }
}
