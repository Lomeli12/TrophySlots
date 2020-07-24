package net.lomeli.trophyslots.core.capabilities;

import net.minecraft.nbt.CompoundNBT;

public interface IPlayerSlots {
    boolean unlockSlot(int amount);

    boolean slotUnlocked(int index);

    int getSlotsUnlocked();

    void setSlotsUnlocked(int slots);

    boolean maxSlotsUnlocked();

    void serialize(CompoundNBT nbt);

    void deserialize(CompoundNBT nbt);
}
