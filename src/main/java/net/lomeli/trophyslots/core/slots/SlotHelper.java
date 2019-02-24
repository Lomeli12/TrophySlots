package net.lomeli.trophyslots.core.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class SlotHelper {

    @SuppressWarnings("unchecked")
    public static void disableSlot(CallbackInfoReturnable callback, Inventory inventory, int slotNum, Object returnValue) {
        if (inventory instanceof PlayerInventory) {
            PlayerEntity player = ((PlayerInventory) inventory).player;
            if (!player.abilities.creativeMode && player instanceof ISlotHolder) {
                PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
                if (!slotManager.slotUnlocked(slotNum)) {
                    callback.setReturnValue(returnValue);
                    callback.cancel();
                }
            }
        }
    }
}
