package net.lomeli.trophyslots.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import net.lomeli.trophyslots.TrophySlots;

public class SlotUtil {
    public static int getSlotsUnlocked(EntityPlayer player) {
        if (player != null && player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).hasKey(TrophySlots.slotsUnlocked))
            return player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger(TrophySlots.slotsUnlocked);
        return 0;
    }

    public static boolean hasUnlockedAllSlots(EntityPlayer player) {
        return getSlotsUnlocked(player) >= getMaxSlots();
    }

    public static boolean slotUnlocked(EntityPlayer player, int slotNum) {
        if (TrophySlots.proxy.unlockReverse() && slotNum > 8)
            return slotNum < 36 ? slotNum > 44 - (TrophySlots.proxy.getStartingSlots() + getSlotsUnlocked(player)) : true;
        return slotNum < 36 ? slotNum < TrophySlots.proxy.getStartingSlots() + getSlotsUnlocked(player) : true;
    }

    public static void setSlotsUnlocked(EntityPlayer player, int slots) {
        if (player != null) {
            NBTTagCompound tag = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            if (tag == null)
                tag = new NBTTagCompound();
            tag.setInteger(TrophySlots.slotsUnlocked, slots);
            player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, tag);
        }
    }

    public static int getMaxSlots() {
        return 36 - TrophySlots.proxy.getStartingSlots();
    }
}
