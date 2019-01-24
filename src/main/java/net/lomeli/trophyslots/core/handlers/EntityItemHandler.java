package net.lomeli.trophyslots.core.handlers;

import net.lomeli.trophyslots.core.slots.ISlotHolder;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class EntityItemHandler {
    public static boolean onItemEntityPickup(PlayerEntity player, ItemStack stack) {
        if (!stack.isEmpty() && !player.abilities.creativeMode && player instanceof ISlotHolder) {
            PlayerSlotManager slotManager = ((ISlotHolder) player).getSlotManager();
            if (!slotManager.maxSlotsUnlocked()) {
                int slot = getPossibleUnlockedSlot(stack, player);
                return !slotManager.slotUnlocked(slot);
            }
        }
        return false;
    }

    private static int getPossibleUnlockedSlot(ItemStack stack, PlayerEntity player) {
        int slot = player.inventory.getOccupiedSlotWithRoomForStack(stack);
        if (slot == -1)
            slot = player.inventory.getEmptySlot();
        return slot;
    }
}
