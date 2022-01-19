package net.lomeli.trophyslots.core.capabilities;

import net.lomeli.trophyslots.core.CommonConfig;
import net.lomeli.trophyslots.utils.InventoryUtils;
import net.minecraft.nbt.CompoundTag;

public class PlayerSlotManager implements IPlayerSlots {
    private int slotsUnlocked;

    @Override
    public boolean unlockSlot(int amount) {
        int oldSlotAmount = slotsUnlocked;
        slotsUnlocked += amount;
        if (slotsUnlocked > InventoryUtils.getMaxUnlockableSlots())
            slotsUnlocked = InventoryUtils.getMaxUnlockableSlots();
        if (slotsUnlocked < 0)
            slotsUnlocked = 0;
        return oldSlotAmount != slotsUnlocked;
    }

    @Override
    public boolean slotUnlocked(int index) {
        if (index < InventoryUtils.MAX_SLOTS) {
            if (CommonConfig.reverseOrder && index >= 9)
                return index > InventoryUtils.MAX_INV_SLOTS - (CommonConfig.startingSlots + slotsUnlocked);
            else return index < CommonConfig.startingSlots + slotsUnlocked;
        }
        return true;
    }

    @Override
    public int getSlotsUnlocked() {
        return slotsUnlocked;
    }

    @Override
    public void setSlotsUnlocked(int slotsUnlocked) {
        this.slotsUnlocked = slotsUnlocked;
        if (this.slotsUnlocked < 0)
            this.slotsUnlocked = 0;
        if (this.slotsUnlocked > InventoryUtils.getMaxUnlockableSlots())
            this.slotsUnlocked = InventoryUtils.getMaxUnlockableSlots();
    }

    @Override
    public boolean maxSlotsUnlocked() {
        return slotsUnlocked >= InventoryUtils.getMaxUnlockableSlots();
    }

    @Override
    public void serialize(CompoundTag nbt) {
        nbt.putInt("player_slots", slotsUnlocked);
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        this.setSlotsUnlocked(nbt.getInt("player_slots"));
    }
}
