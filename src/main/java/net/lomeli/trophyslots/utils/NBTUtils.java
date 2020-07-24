package net.lomeli.trophyslots.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class NBTUtils {
    private static CompoundNBT getItemTag(ItemStack stack) {
        if (!stack.hasTag()) stack.setTag(new CompoundNBT());
        return stack.getTag();
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

    public static boolean getBoolean(ItemStack stack, String tagName) {
        CompoundNBT tag = getItemTag(stack);
        return tag.getBoolean(tagName);
    }

    public static void setBoolean(ItemStack stack, String tagName, boolean value) {
        CompoundNBT tag = getItemTag(stack);
        tag.putBoolean(tagName, value);
        stack.setTag(tag);
    }
}
