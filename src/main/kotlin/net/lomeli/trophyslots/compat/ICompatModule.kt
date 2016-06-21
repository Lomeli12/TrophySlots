package net.lomeli.trophyslots.compat

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer

interface ICompatModule {
    fun replaceSlots(container: GuiContainer, player: EntityPlayer)

    fun isCompatibleGui(gui: GuiContainer): Boolean
}