package net.lomeli.trophyslots.client.slots;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

import cpw.mods.fml.common.Loader;

public class SlotHandler {

    public static Slot getSlotBasedOnGui(GuiContainer gui, Slot oldSlot) {
        Minecraft mc = Minecraft.getMinecraft();
        if (Loader.isModLoaded("StevesWorkshop")) {
            if (SlotLockedPlayer.isValidGuiAndSlot(gui, oldSlot))
                return SlotLockedPlayer.getSlot(mc, gui, oldSlot);
        }
        return new SlotLocked(mc.thePlayer.inventory, oldSlot.getSlotIndex(), oldSlot.xDisplayPosition, oldSlot.yDisplayPosition);
    }
}
