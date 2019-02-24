package net.lomeli.trophyslots.mixin.common;

import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements ISlotHolder {
    private PlayerSlotManager slotManager = new PlayerSlotManager();

    @Inject(method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
    private void readNBT(CompoundTag tag, CallbackInfo callback) {
        getSlotManager().deserialize(tag);
    }

    @Inject(method = "writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
    private void writeNBT(CompoundTag tag, CallbackInfo callback) {
        getSlotManager().serialize(tag);
    }

    @Override
    public PlayerSlotManager getSlotManager() {
        return slotManager;
    }

}
