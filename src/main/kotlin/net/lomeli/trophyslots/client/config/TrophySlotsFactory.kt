package net.lomeli.trophyslots.client.config

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.client.IModGuiFactory

public class TrophySlotsFactory : IModGuiFactory {
    override fun runtimeGuiCategories(): MutableSet<IModGuiFactory.RuntimeOptionCategoryElement>? {
        return null
    }

    override fun mainConfigGuiClass(): Class<out GuiScreen>? {
        return GuiTrophySlotsConfig::class.java
    }

    override fun getHandlerFor(element: IModGuiFactory.RuntimeOptionCategoryElement?): IModGuiFactory.RuntimeOptionGuiHandler? {
        return null
    }

    override fun initialize(minecraftInstance: Minecraft?) {
    }
}