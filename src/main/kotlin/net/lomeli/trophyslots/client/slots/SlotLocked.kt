package net.lomeli.trophyslots.client.slots

import net.lomeli.trophyslots.TrophySlots

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class SlotLocked(inv: IInventory, slot: Int, x: Int, y: Int) : Slot(inv, slot, x, y) {

    override fun decrStackSize(amount: Int): ItemStack? = if (TrophySlots.proxy!!.slotUnlocked(slotIndex)) super.decrStackSize(amount) else null

    override fun isItemValid(stack: ItemStack?): Boolean = if (TrophySlots.proxy!!.slotUnlocked(slotIndex)) super.isItemValid(stack) else false

    override fun canTakeStack(playerIn: EntityPlayer?): Boolean = if (TrophySlots.proxy!!.slotUnlocked(slotIndex)) super.canTakeStack(playerIn) else false

    @SideOnly(Side.CLIENT) override fun canBeHovered(): Boolean = if (TrophySlots.proxy!!.slotUnlocked(slotIndex)) super.canBeHovered() else false
}