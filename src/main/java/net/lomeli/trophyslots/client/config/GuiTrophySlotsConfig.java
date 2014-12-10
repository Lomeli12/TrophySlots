package net.lomeli.trophyslots.client.config;

import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

import net.lomeli.trophyslots.TrophySlots;

public class GuiTrophySlotsConfig extends GuiConfig {
    public GuiTrophySlotsConfig(GuiScreen parent) {
        super(parent, new ConfigElement(TrophySlots.modConfig.getConfig().getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), TrophySlots.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(TrophySlots.modConfig.getConfig().toString()));
    }
}
