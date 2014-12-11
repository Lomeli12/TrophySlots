package net.lomeli.trophyslots.core;

import java.io.File;
import java.util.ArrayList;

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
        TrophySlots.startingSlots = config.getInt("startingSlots", Configuration.CATEGORY_GENERAL, 9, 0, 36, SimpleUtil.translate("config.trophyslots.startingSlots"));
        TrophySlots.unlockViaAchievements = config.getBoolean("unlockViaAchievements", Configuration.CATEGORY_GENERAL, true, SimpleUtil.translate("config.trophyslots.unlockAchieve"));
        TrophySlots.canUseTrophy = config.getBoolean("canUseTrophy", Configuration.CATEGORY_GENERAL, true, SimpleUtil.translate("config.trophyslots.canUseTrophy"));
        TrophySlots.canBuyTrophy = config.getBoolean("canBuyTrophies", Configuration.CATEGORY_GENERAL, false, SimpleUtil.translate("config.trophyslots.canBuyTrophy"));
        TrophySlots.disable3 = config.getBoolean("disableFirst3", Configuration.CATEGORY_GENERAL, false, SimpleUtil.translate("config.trophyslots.disable3"));
        TrophySlots.checkForUpdates = config.getBoolean("checkForUpdates", Configuration.CATEGORY_GENERAL, true, SimpleUtil.translate("config.trophyslots.update"));
        TrophySlots.useWhiteList = config.getBoolean("useWhitelist", Configuration.CATEGORY_GENERAL, false, SimpleUtil.translate("config.trophyslots.useWhitelist"));
        TrophySlots.slotRenderType = config.getInt("slotRenderType", Configuration.CATEGORY_GENERAL, 0, 0, 4, SimpleUtil.translate("config.trophyslots.renderLockedSlots"));
        String whiteList = config.getString("WhiteList", Configuration.CATEGORY_GENERAL, "", SimpleUtil.translate("config.trophyslots.whitelist"));

        TrophySlots.xmas = config.getBoolean("xmas", Configuration.CATEGORY_GENERAL, true, "");

        if (TrophySlots.useWhiteList)
            fillWhitelist(whiteList);
        if (config.hasChanged())
            config.save();
    }

    public void fillWhitelist(String whiteList) {
        if (TrophySlots.achievementWhiteList == null)
            TrophySlots.achievementWhiteList = new ArrayList<String>();
        if (!TrophySlots.achievementWhiteList.isEmpty())
            TrophySlots.achievementWhiteList.clear();

        String[] achievementIDs = whiteList.split(";");
        if (achievementIDs != null && achievementIDs.length > 0) {
            for (String id : achievementIDs)
                TrophySlots.achievementWhiteList.add(id);
        }
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.modID.equalsIgnoreCase(TrophySlots.MOD_ID))
            TrophySlots.modConfig.loadConfig();
    }
}
