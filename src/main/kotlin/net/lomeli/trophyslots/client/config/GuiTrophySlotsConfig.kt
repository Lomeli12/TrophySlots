package net.lomeli.trophyslots.client.config


import net.minecraft.client.gui.GuiScreen

import net.minecraftforge.common.config.ConfigElement
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.config.GuiConfig

import net.lomeli.trophyslots.TrophySlots

public class GuiTrophySlotsConfig(parent: GuiScreen) : GuiConfig(parent, ConfigElement(TrophySlots.modConfig!!.config.getCategory(Configuration.CATEGORY_GENERAL)).childElements, TrophySlots.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(TrophySlots.modConfig!!.config.toString()))
