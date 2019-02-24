package net.lomeli.trophyslots.mixin.common;

import net.lomeli.trophyslots.core.slots.SlotHelper;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow
    @Final
    private int invSlot;

    @Shadow
    @Final
    public Inventory inventory;

    @Inject(method = "canInsert", at = @At("TAIL"), cancellable = true)
    private void canInsertItem(ItemStack stack, CallbackInfoReturnable<Boolean> callback) {
        SlotHelper.disableSlot(callback, inventory, invSlot, false);
    }

    @Inject(method = "canTakeItems", at = @At("TAIL"), cancellable = true)
    private void canTakeItemStack(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> callback) {
        SlotHelper.disableSlot(callback, inventory, invSlot, false);
    }

    @Inject(method = "takeStack", at = @At("HEAD"), cancellable = true)
    private void takeItemStack(int amount, CallbackInfoReturnable<ItemStack> callback) {
        SlotHelper.disableSlot(callback, inventory, invSlot, ItemStack.EMPTY);
    }
}
