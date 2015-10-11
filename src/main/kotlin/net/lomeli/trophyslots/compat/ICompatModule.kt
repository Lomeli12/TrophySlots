package net.lomeli.trophyslots.compat

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer

interface ICompatModule {
    public abstract fun replaceSlots(container: GuiContainer, player: EntityPlayer)

    public abstract fun isCompatibleGui(gui: GuiContainer): Boolean
}