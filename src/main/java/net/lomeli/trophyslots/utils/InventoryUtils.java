package net.lomeli.trophyslots.utils;

import net.lomeli.trophyslots.core.ModConfig;
import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class InventoryUtils {
    /**
     * Player inventory slots
     */
    public static final int MAX_SLOTS = 36;

    /**
     * Player Inventory slots + armor slots + crafting slots
     */
    public static final int MAX_INV_SLOTS = 44;

    public static boolean canMergeStacks(ItemStack slotStack, ItemStack inputStack) {
        if (slotStack.getItem() != inputStack.getItem())
            return false;
        else if (slotStack.getDamage() != inputStack.getDamage())
            return false;
        else if (slotStack.getAmount() >= slotStack.getMaxAmount())
            return false;
        else
            return ItemStack.areTagsEqual(slotStack, inputStack);
    }

    public static int getNextEmptySlot(PlayerSlotManager slotManager, PlayerInventory inventory) {
        for (int i = 0; i < inventory.main.size(); i++) {
            if (slotManager.slotUnlocked(i) && inventory.getInvStack(i).isEmpty())
                return i;
        }
        return -1;
    }

    //God I hate this method but I need it for now.
    public static int searchForPossibleSlots(PlayerSlotManager slotManager, PlayerInventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.main.size(); i++) {
            if (slotManager.slotUnlocked(i)) {
                ItemStack item = inventory.getInvStack(i);
                if (item.isEmpty())
                    return i;
                else if (canMergeStacks(item, stack))
                    return i;
            }
        }
        return -1;
    }

    public static int getMaxUnlockableSlots() {
        return MAX_SLOTS - ModConfig.startingSlots;
    }
}
