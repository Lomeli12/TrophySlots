package net.lomeli.trophyslots.utils;

import net.lomeli.trophyslots.core.slots.PlayerSlotManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class InventoryUtils {
    public static boolean canMergeStacks(ItemStack stack1, ItemStack stack2) {
        if (stack1.getItem() != stack2.getItem())
            return false;
        else if (stack1.getDamage() != stack2.getDamage())
            return false;
        else if (stack1.getAmount() > stack1.getMaxAmount())
            return false;
        else
            return ItemStack.areTagsEqual(stack1, stack2);
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
}
