package net.lomeli.trophyslots.compat;

import vswe.production.gui.GuiTable;
import vswe.production.gui.container.slot.SlotPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

import cpw.mods.fml.client.FMLClientHandler;

import net.lomeli.trophyslots.client.slots.SlotLocked;
import net.lomeli.trophyslots.client.slots.workshop.SlotLockedPlayer;

public class StWorkshopMod {
    private Minecraft mc = FMLClientHandler.instance().getClient();
    public Slot getSlotForGui(GuiContainer gui, Slot oldSlot) {
        if (gui instanceof GuiTable && oldSlot instanceof SlotPlayer)
            return SlotLockedPlayer.getSlot(mc, gui, oldSlot);
        return SlotLocked.getSlot(mc.thePlayer, oldSlot);
    }
}
