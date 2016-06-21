package net.lomeli.trophyslots.client.config

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.client.IModGuiFactory

class TrophySlotsFactory : IModGuiFactory {
    override fun runtimeGuiCategories(): MutableSet<IModGuiFactory.RuntimeOptionCategoryElement>? = null

    override fun mainConfigGuiClass(): Class<out GuiScreen>? = GuiTrophySlotsConfig::class.java

    override fun getHandlerFor(element: IModGuiFactory.RuntimeOptionCategoryElement?): IModGuiFactory.RuntimeOptionGuiHandler? = null

    override fun initialize(minecraftInstance: Minecraft?) {
    }
}