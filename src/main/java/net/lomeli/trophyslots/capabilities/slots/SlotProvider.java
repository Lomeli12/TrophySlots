package net.lomeli.trophyslots.capabilities.slots;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class SlotProvider implements ICapabilitySerializable<NBTBase> {
    @CapabilityInject(ISlotInfo.class)
    public static Capability<ISlotInfo> SLOT_INFO = null;
    private ISlotInfo instance = SLOT_INFO.getDefaultInstance();

    public SlotProvider() {
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == SLOT_INFO;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == capability ? SLOT_INFO.cast(instance) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return SLOT_INFO.getStorage().writeNBT(SLOT_INFO, instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        SLOT_INFO.getStorage().readNBT(SLOT_INFO, instance, null, nbt);
    }
}
