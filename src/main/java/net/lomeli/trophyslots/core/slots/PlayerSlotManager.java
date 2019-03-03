package net.lomeli.trophyslots.core.slots;

import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.utils.InventoryUtils;
import net.minecraft.nbt.CompoundTag;

public class PlayerSlotManager {
    private int slotsUnlocked;

    public boolean unlockSlot(int amount) {
        if (!maxSlotsUnlocked()) {
            slotsUnlocked += amount;
            if (maxSlotsUnlocked()) slotsUnlocked = InventoryUtils.getMaxUnlockableSlots();
            return true;
        }
        return false;
    }

    public boolean slotUnlocked(int index) {
        if (index < InventoryUtils.getMaxUnlockableSlots()) {
            if (ModConfig.reverseOrder && index >= 9)
                return index > InventoryUtils.MAX_INV_SLOTS - (ModConfig.startingSlots + slotsUnlocked);
            else return index < ModConfig.startingSlots + slotsUnlocked;
        }
        return true;
    }

    public int getSlotsUnlocked() {
        return slotsUnlocked;
    }

    public void setSlotsUnlocked(int slotsUnlocked) {
        this.slotsUnlocked = slotsUnlocked;
        if (this.slotsUnlocked < 0)
            this.slotsUnlocked = 0;
        if (this.slotsUnlocked > InventoryUtils.getMaxUnlockableSlots())
            this.slotsUnlocked = InventoryUtils.getMaxUnlockableSlots();
    }

    public boolean maxSlotsUnlocked() {
        return slotsUnlocked >= InventoryUtils.getMaxUnlockableSlots();
    }
    
    public void serialize(CompoundTag nbt) {
        nbt.putInt("player_slots", slotsUnlocked);
    }

    public void deserialize(CompoundTag tag) {
        setSlotsUnlocked(tag.getInt("player_slots"));
    }
}
