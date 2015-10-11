package net.lomeli.trophyslots.compat

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.client.slots.SlotLocked
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.ContainerPlayer
import net.minecraft.inventory.SlotCrafting

public object PlayerInvMod : ICompatModule {
    override fun replaceSlots(gui: GuiContainer, player: EntityPlayer) {
        val containerPlayer = ContainerPlayer(player.inventory, !player.worldObj.isRemote, player)
        val slotList = containerPlayer.inventorySlots
        if (slotList != null) {
            for (i in 5..slotList.size() - 1) {
                val slot = containerPlayer.getSlot(i)
                if (slot != null && slot !is SlotCrafting) {
                    if (slot.inventory !== containerPlayer.craftMatrix && slot.isHere(player.inventory, slot.slotIndex) && !TrophySlots.proxy!!.slotUnlocked(slot.slotIndex))
                        containerPlayer.inventorySlots.set(i, SlotLocked(player.inventory, slot.slotIndex, slot.xDisplayPosition, slot.yDisplayPosition))
                }
            }
        }
        gui.inventorySlots = containerPlayer
    }

    override fun isCompatibleGui(gui: GuiContainer): Boolean {
        return gui is GuiInventory
    }
}
