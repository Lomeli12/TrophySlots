package net.lomeli.trophyslots.compat

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer

interface ICompatModule {
    public fun replaceSlots(container: GuiContainer, player: EntityPlayer)

    public fun isCompatibleGui(gui: GuiContainer): Boolean
}