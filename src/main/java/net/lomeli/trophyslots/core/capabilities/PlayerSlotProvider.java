package net.lomeli.trophyslots.core.capabilities;

import net.lomeli.trophyslots.TrophySlots;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerSlotProvider implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation ID = new ResourceLocation(TrophySlots.MOD_ID, "player_slots");

    public static final Capability<IPlayerSlots> SLOTS = null;
    private final LazyOptional<IPlayerSlots> INSTANCE = LazyOptional.of(PlayerSlotManager::new);

    @Nonnull
    @Override
    @SuppressWarnings("ConstantConditions")
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == SLOTS ? INSTANCE.cast() : LazyOptional.empty();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        IPlayerSlots slots = getCapability(SLOTS).orElseThrow(() ->
                new IllegalArgumentException("LazyOptional must not be empty!"));
        if (slots != null) slots.serialize(tag);
        return tag;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void deserializeNBT(CompoundTag tag) {
        IPlayerSlots slots = getCapability(SLOTS).orElseThrow(() ->
                new IllegalArgumentException("LazyOptional must not be empty!"));
        if (slots != null) slots.deserialize(tag);
    }
}
