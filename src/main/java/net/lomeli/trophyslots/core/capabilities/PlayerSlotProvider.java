package net.lomeli.trophyslots.core.capabilities;

import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

//I kinda don't know what I'm doing here.
public class PlayerSlotProvider implements ICapabilitySerializable<CompoundNBT> {
    public static final ResourceLocation ID = new ResourceLocation(TrophySlots.MOD_ID, "player_slots");

    @CapabilityInject(IPlayerSlots.class)
    public static final Capability<IPlayerSlots> SLOTS = null;
    @SuppressWarnings({"ConstantConditions", "NullableProblems"})
    private final LazyOptional<IPlayerSlots> INSTANCE = LazyOptional.of(SLOTS::getDefaultInstance);

    @Nonnull
    @Override
    @SuppressWarnings("ConstantConditions")
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == SLOTS ? INSTANCE.cast() : LazyOptional.empty();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) SLOTS.getStorage().writeNBT(SLOTS, INSTANCE
                .orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void deserializeNBT(CompoundNBT nbt) {
        SLOTS.getStorage().readNBT(SLOTS, this.INSTANCE
                .orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}
