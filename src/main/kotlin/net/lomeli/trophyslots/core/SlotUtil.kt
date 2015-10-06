package net.lomeli.trophyslots.core

import net.lomeli.trophyslots.TrophySlots
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound

public object SlotUtil {
    public fun getSlotsUnlocked(player: EntityPlayer): Int {
        if (player != null && player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).hasKey(TrophySlots.slotsUnlocked))
            return player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger(TrophySlots.slotsUnlocked);
        return 0;
    }

    public fun hasUnlockedAllSlots(player: EntityPlayer): Boolean {
        return getSlotsUnlocked(player) >= getMaxSlots();
    }

    public fun slotUnlocked(player: EntityPlayer, slotNum: Int) : Boolean{
        if (TrophySlots.proxy.unlockReverse() && slotNum > 8) {
            if (slotNum < 36)
                return slotNum > 44 - (TrophySlots.proxy.startingSlots + getSlotsUnlocked(player))
            else
                return true
        }
        if (slotNum < 36)
            return slotNum < TrophySlots.proxy.startingSlots + getSlotsUnlocked(player)
        return true
    }

    public fun setSlotsUnlocked(player: EntityPlayer, slots: Int) {
        if (player != null) {
            var tag = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            if (tag == null)
                tag = NBTTagCompound();
            tag.setInteger(TrophySlots.slotsUnlocked, slots);
            player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, tag);
        }
    }

    public fun getMaxSlots(): Int {
        return 36 - TrophySlots.proxy.startingSlots;
    }
}