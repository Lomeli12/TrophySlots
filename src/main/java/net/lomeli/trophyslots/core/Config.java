package net.lomeli.trophyslots.core;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.trophyslots.TrophySlots;

public class Config {
    private Configuration config;

    public Config(File file) {
        config = new Configuration(file);
    }

    public Configuration getConfig() {
        return config;
    }

    public void loadConfig() {
        TrophySlots.startingSlots = config.getInt("startingSlots", Configuration.CATEGORY_GENERAL, 9, 9, 36, SimpleUtil.translate("config.trophyslots.startingSlots"));
        TrophySlots.unlockViaAchievements = config.getBoolean("unlockViaAchievements", Configuration.CATEGORY_GENERAL, true, SimpleUtil.translate("config.trophyslots.unlockAchieve"));
        TrophySlots.canUseTrophy = config.getBoolean("canUseTrophy", Configuration.CATEGORY_GENERAL, true, SimpleUtil.translate("config.trophyslots.canUseTrophy"));
        TrophySlots.canBuyTrophy = config.getBoolean("canBuyTrophies", Configuration.CATEGORY_GENERAL, false, SimpleUtil.translate("config.trophyslots.canBuyTrophy"));
        if (config.hasChanged())
            config.save();
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.modID.equalsIgnoreCase(TrophySlots.MOD_ID))
            TrophySlots.modConfig.loadConfig();
    }
}
