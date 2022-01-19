package net.lomeli.trophyslots.mixin;

import net.lomeli.trophyslots.core.capabilities.PlayerSlotHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotCommon {

    @Shadow
    @Final
    public int index;

    @Shadow
    @Final
    public Container container;

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static void disableSlot(CallbackInfoReturnable callback, Container container, int index,
                                      Object defaultReturn) {
        if (!PlayerSlotHelper.isSlotUnlocked(container, index)) {
            callback.setReturnValue(defaultReturn);
            callback.cancel();
        }
    }

    @Inject(method = "mayPlace", at = @At("TAIL"), cancellable = true)
    private void mayPlaceItem(ItemStack stack, CallbackInfoReturnable<Boolean> callback) {
        disableSlot(callback, container, index, false);
    }

    @Inject(method = "mayPickup", at = @At("TAIL"), cancellable = true)
    private void mayPickupItem(Player player, CallbackInfoReturnable<Boolean> callback) {
        disableSlot(callback, container, index, false);
    }

    @Inject(method = "remove", at = @At("TAIL"), cancellable = true)
    private void removeItem(int amount, CallbackInfoReturnable<ItemStack> callback) {
        disableSlot(callback, container, index, ItemStack.EMPTY);
    }
}
