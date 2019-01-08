package net.lomeli.trophyslots.core.slots;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.config.ModConfig;
import net.minecraft.nbt.CompoundTag;

public class PlayerSlotManager {
    private int slotsUnlocked;

    public PlayerSlotManager() {
        slotsUnlocked = 0;
    }

    public boolean unlockSlot(int amount) {
        if (!maxSlotsUnlocked()){
            slotsUnlocked += amount;
            if (maxSlotsUnlocked()) slotsUnlocked = getMaxSlots();
            return true;
        }
        return false;
    }

    public boolean slotUnlocked(int index) {
        if (index < TrophySlots.MAX_SLOTS) {
            if (ModConfig.reverseOrder && index >= 9)
                return index > TrophySlots.MAX_INV_SLOTS - (ModConfig.startingSlots + slotsUnlocked);
            else return index < ModConfig.startingSlots + slotsUnlocked;
        }
        return true;
    }

    public int getSlotsUnlocked() {
        return slotsUnlocked;
    }

    public void setSlotsUnlocked(int slotsUnlocked) {
        this.slotsUnlocked = slotsUnlocked;
    }

    @Environment(EnvType.CLIENT)
    public void setSlotsUnlockedClient(int slotsUnlocked) {
        this.slotsUnlocked = slotsUnlocked;
    }

    public boolean maxSlotsUnlocked() {
        return slotsUnlocked >= getMaxSlots();
    }

    public int getMaxSlots() {
        return TrophySlots.MAX_SLOTS;
    }

    public void serialize(CompoundTag nbt) {
        nbt.putInt("player_slots", slotsUnlocked);
    }

    public void deserialize(CompoundTag tag) {
        slotsUnlocked = tag.getInt("player_slots");
    }
}
