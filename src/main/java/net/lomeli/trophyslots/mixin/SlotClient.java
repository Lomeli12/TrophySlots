package net.lomeli.trophyslots.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotClient {

    @Shadow
    @Final
    public int index;

    @Shadow
    @Final
    public Container container;

    @Inject(method = "isActive", at = @At("TAIL"), cancellable = true)
    private void isSlotActive(CallbackInfoReturnable<Boolean> callback) {
        SlotCommon.disableSlot(callback, container, index, false);
    }
}
