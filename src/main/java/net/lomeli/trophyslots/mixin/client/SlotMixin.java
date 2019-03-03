package net.lomeli.trophyslots.mixin.client;

import net.lomeli.trophyslots.client.accessors.ISlotAccessor;
import net.lomeli.trophyslots.core.slots.SlotHelper;
import net.minecraft.container.Slot;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin implements ISlotAccessor {

    @Shadow
    @Final
    private int invSlot;

    @Shadow
    @Final
    public Inventory inventory;

    @Inject(method = "doDrawHoveringEffect", at = @At("TAIL"), cancellable = true)
    private void slotEnabled(CallbackInfoReturnable<Boolean> callback) {
        SlotHelper.disableSlot(callback, inventory, invSlot, false);
    }

    @Override
    public int getSlotIndex() {
        return invSlot;
    }
}
