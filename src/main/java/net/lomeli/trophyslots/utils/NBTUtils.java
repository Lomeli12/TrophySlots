package net.lomeli.trophyslots.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class NBTUtils {
    private static CompoundTag getItemTag(ItemStack stack) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        return stack.getTag();
    }

    public static int getInt(ItemStack stack, String tagName) {
        CompoundTag tag = getItemTag(stack);
        return tag.getInt(tagName);
    }

    public static void setInt(ItemStack stack, String tagName, int value) {
        CompoundTag tag = getItemTag(stack);
        tag.putInt(tagName, value);
        stack.setTag(tag);
    }

    public static boolean getBoolean(ItemStack stack, String tagName) {
        CompoundTag tag = getItemTag(stack);
        return tag.getBoolean(tagName);
    }

    public static void setBoolean(ItemStack stack, String tagName, boolean value) {
        CompoundTag tag = getItemTag(stack);
        tag.putBoolean(tagName, value);
        stack.setTag(tag);
    }
}
