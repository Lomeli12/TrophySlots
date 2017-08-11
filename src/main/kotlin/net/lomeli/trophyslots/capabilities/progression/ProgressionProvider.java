package net.lomeli.trophyslots.capabilities.progression;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProgressionProvider implements ICapabilitySerializable<NBTBase> {
    @CapabilityInject(IPlayerProgression.class)
    public static Capability<IPlayerProgression> PLAYER_PROGRESSION = null;
    private IPlayerProgression instance = PLAYER_PROGRESSION.getDefaultInstance();

    public ProgressionProvider() {
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == PLAYER_PROGRESSION;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == PLAYER_PROGRESSION ? PLAYER_PROGRESSION.cast(instance) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return PLAYER_PROGRESSION.getStorage().writeNBT(PLAYER_PROGRESSION, instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        PLAYER_PROGRESSION.getStorage().readNBT(PLAYER_PROGRESSION, instance, null, nbt);
    }
}
