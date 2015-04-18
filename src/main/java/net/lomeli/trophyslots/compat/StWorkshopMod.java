package net.lomeli.trophyslots.compat;

import vswe.production.gui.GuiTable;
import vswe.production.gui.container.slot.SlotPlayer;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.slots.SlotLocked;
import net.lomeli.trophyslots.client.slots.workshop.SlotLockedPlayer;

public class StWorkshopMod implements ICompatModule {
    @Override
    public boolean isCompatibleGui(GuiContainer gui) {
        return gui instanceof GuiTable;
    }

    @Override
    public void replaceSlots(GuiContainer gui, EntityPlayer player) {
        List slotList = gui.inventorySlots.inventorySlots;
        if (slotList != null) {
            for (int i = 0; i < slotList.size(); i++) {
                Slot slot = gui.inventorySlots.getSlot(i);
                if (slot != null && slot.isSlotInInventory(player.inventory, slot.getSlotIndex())) {
                    if (!TrophySlots.proxy.slotUnlocked(slot.getSlotIndex()))
                        gui.inventorySlots.inventorySlots.set(i, getSlot(gui, player, slot));
                }
            }
        }
    }

    public Slot getSlot(GuiContainer gui, EntityPlayer player, Slot oldSlot) {
        return oldSlot instanceof SlotPlayer ? SlotLockedPlayer.getSlot(player, gui, oldSlot) : SlotLocked.getSlot(player, oldSlot);
    }
}
