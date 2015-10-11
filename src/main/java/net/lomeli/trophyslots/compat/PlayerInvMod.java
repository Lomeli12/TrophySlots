package net.lomeli.trophyslots.compat;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.client.slots.SlotLocked;

public class PlayerInvMod implements ICompatModule {
    @Override
    public void replaceSlots(GuiContainer gui, EntityPlayer player) {
        ContainerPlayer containerPlayer = new ContainerPlayer(player.inventory, !player.worldObj.isRemote, player);
        List slotList = containerPlayer.inventorySlots;
        if (slotList != null) {
            for (int i = 5; i < slotList.size(); i++) {
                Slot slot = containerPlayer.getSlot(i);
                if (slot != null && !(slot instanceof SlotCrafting)) {
                    if (slot.inventory != containerPlayer.craftMatrix && slot.isHere(player.inventory, slot.getSlotIndex()) && !TrophySlots.proxy.slotUnlocked(slot.getSlotIndex()))
                        containerPlayer.inventorySlots.set(i, new SlotLocked(player.inventory, slot.getSlotIndex(), slot.xDisplayPosition, slot.yDisplayPosition));
                }
            }
        }
        gui.inventorySlots = containerPlayer;
    }

    @Override
    public boolean isCompatibleGui(GuiContainer gui) {
        return gui instanceof GuiInventory;
    }
}
