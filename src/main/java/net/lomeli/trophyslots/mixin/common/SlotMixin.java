package net.lomeli.trophyslots.mixin.common;

import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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
        if (inventory instanceof PlayerInventory) {
            PlayerEntity player = ((PlayerInventory) inventory).player;
            if (!player.abilities.creativeMode && player instanceof ISlotHolder) {
                PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
                if (!slotManager.slotUnlocked(invSlot)) {
                    callback.setReturnValue(false);
                    callback.cancel();
                }
            }
        }
    }

    @Inject(method = "canTakeItems", at = @At("TAIL"), cancellable = true)
    private void canTakeItemStack(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> callback) {
        if (inventory instanceof PlayerInventory) {
            PlayerEntity player = ((PlayerInventory) inventory).player;
            if (!player.abilities.creativeMode && player instanceof ISlotHolder) {
                PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
                if (!slotManager.slotUnlocked(invSlot)) {
                    callback.setReturnValue(false);
                    callback.cancel();
                }
            }
        }
    }

    @Inject(method = "takeStack", at = @At("HEAD"), cancellable = true)
    private void takeItemStack(int amount, CallbackInfoReturnable<ItemStack> callback) {
        if (inventory instanceof PlayerInventory) {
            PlayerEntity player = ((PlayerInventory) inventory).player;
            if (!player.abilities.creativeMode && player instanceof ISlotHolder) {
                PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
                if (!slotManager.slotUnlocked(invSlot)) {
                    callback.setReturnValue(ItemStack.EMPTY);
                    callback.cancel();
                }
            }
        }
    }
}
