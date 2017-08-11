package net.lomeli.trophyslots.compat

import net.lomeli.trophyslots.capabilities.slots.SlotManager
import net.lomeli.trophyslots.client.slots.SlotLocked
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.ContainerPlayer
import net.minecraft.inventory.SlotCrafting

object PlayerInvMod : ICompatModule {
    override fun replaceSlots(container: GuiContainer, player: EntityPlayer) {
        val containerPlayer = ContainerPlayer(player.inventory, !player.world.isRemote, player)
        val slotList = containerPlayer.inventorySlots
        val slotInfo = SlotManager.getPlayerSlotInfo(player)!!
        if (slotList != null) {
            for (i in slotList.indices) {
                val slot = containerPlayer.getSlot(i)
                if (slot != null && slot !is SlotCrafting) {
                    if (slot.inventory !== containerPlayer.craftMatrix && slot.isHere(player.inventory, slot.slotIndex) && !slotInfo.slotUnlocked(slot.slotIndex))
                        containerPlayer.inventorySlots.set(i, SlotLocked(player.inventory, slot.slotIndex, slot.xPos, slot.yPos))
                }
            }
        }
        container.inventorySlots = containerPlayer
    }

    override fun isCompatibleGui(gui: GuiContainer): Boolean = gui is GuiInventory
}
