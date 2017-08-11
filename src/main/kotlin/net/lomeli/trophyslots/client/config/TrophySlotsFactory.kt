package net.lomeli.trophyslots.client.config

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.client.IModGuiFactory

class TrophySlotsFactory : IModGuiFactory {
    override fun runtimeGuiCategories(): MutableSet<IModGuiFactory.RuntimeOptionCategoryElement>? = null

    override fun initialize(minecraftInstance: Minecraft?) {
    }

    override fun hasConfigGui(): Boolean = true

    override fun createConfigGui(parentScreen: GuiScreen?): GuiScreen = GuiTrophySlotsConfig(parentScreen!!)
}