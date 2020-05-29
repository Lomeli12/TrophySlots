package net.lomeli.trophyslots.core.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;

import net.minecraftforge.common.capabilities.Capability;

public class PlayerSlotStorage implements Capability.IStorage<IPlayerSlots> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IPlayerSlots> capability, IPlayerSlots instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        instance.serialize(nbt);
        return nbt;
    }

    @Override
    public void readNBT(Capability<IPlayerSlots> capability, IPlayerSlots instance, Direction side, INBT nbt) {
        if (nbt instanceof CompoundNBT)
            instance.deserialize((CompoundNBT) nbt);
    }
}
