package net.lomeli.trophyslots.client.slots

import net.lomeli.trophyslots.capabilities.slots.SlotManager

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.client.FMLClientHandler

class SlotLocked(inv: IInventory, slot: Int, x: Int, y: Int) : Slot(inv, slot, x, y) {

    override fun decrStackSize(amount: Int): ItemStack? {
        val player = FMLClientHandler.instance().clientPlayerEntity
        val slotInfo = SlotManager.getPlayerSlotInfo(player)!!
        return if (slotInfo.slotUnlocked(slotIndex)) super.decrStackSize(amount) else null
    }

    override fun isItemValid(stack: ItemStack?): Boolean {
        val player = FMLClientHandler.instance().clientPlayerEntity
        val slotInfo = SlotManager.getPlayerSlotInfo(player)!!
        return if (slotInfo.slotUnlocked(slotIndex)) super.isItemValid(stack) else false
    }

    override fun canTakeStack(playerIn: EntityPlayer?): Boolean {
        if (playerIn == null) return false
        val slotInfo = SlotManager.getPlayerSlotInfo(playerIn)!!
        return if (slotInfo.slotUnlocked(slotIndex)) super.canTakeStack(playerIn) else false
    }
}