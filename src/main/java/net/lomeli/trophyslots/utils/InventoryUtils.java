package net.lomeli.trophyslots.utils;

import net.lomeli.trophyslots.core.CommonConfig;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

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
        else if (slotStack.getDamageValue() != inputStack.getDamageValue())
            return false;
        else if (slotStack.getCount() >= slotStack.getMaxStackSize())
            return false;
        else
            return ItemStack.matches(slotStack, inputStack);
    }

    public static int getNextEmptySlot(IPlayerSlots slotManager, Inventory inventory) {
        for (int i = 0; i < inventory.items.size(); i++) {
            if (slotManager.slotUnlocked(i) && inventory.getItem(i).isEmpty())
                return i;
        }
        return -1;
    }

    //God I hate this method but I need it for now.
    public static int searchForPossibleSlots(IPlayerSlots slotManager, Inventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.items.size(); i++) {
            if (slotManager.slotUnlocked(i)) {
                ItemStack item = inventory.getItem(i);
                if (item.isEmpty())
                    return i;
                else if (canMergeStacks(item, stack))
                    return i;
            }
        }
        return -1;
    }

    public static int getMaxUnlockableSlots() {
        return MAX_SLOTS - CommonConfig.startingSlots;
    }
}
