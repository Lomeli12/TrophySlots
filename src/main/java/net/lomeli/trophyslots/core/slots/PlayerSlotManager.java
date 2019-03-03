package net.lomeli.trophyslots.core.slots;

import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.utils.InventoryUtils;
import net.minecraft.nbt.CompoundTag;

public class PlayerSlotManager {
    private int slotsUnlocked;

    public boolean unlockSlot(int amount) {
        if (!maxSlotsUnlocked()) {
            slotsUnlocked += amount;
            if (maxSlotsUnlocked()) slotsUnlocked = getMaxSlots();
            return true;
        }
        return false;
    }

    public boolean slotUnlocked(int index) {
        if (index < InventoryUtils.MAX_SLOTS) {
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
        if (this.slotsUnlocked > InventoryUtils.MAX_SLOTS)
            this.slotsUnlocked = InventoryUtils.MAX_SLOTS;
    }

    public boolean maxSlotsUnlocked() {
        return slotsUnlocked >= getMaxSlots();
    }

    public int getMaxSlots() {
        return InventoryUtils.MAX_SLOTS;
    }

    public void serialize(CompoundTag nbt) {
        nbt.putInt("player_slots", slotsUnlocked);
    }

    public void deserialize(CompoundTag tag) {
        setSlotsUnlocked(tag.getInt("player_slots"));
    }
}
