package net.lomeli.trophyslots.core;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.util.StatCollector;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
        TrophySlots.proxy.setStartingSlots(config.getInt("startingSlots", Configuration.CATEGORY_GENERAL, 9, 0, 36, translate("config.trophyslots.startingSlots")));
        TrophySlots.unlockViaAchievements = config.getBoolean("unlockViaAchievements", Configuration.CATEGORY_GENERAL, true, translate("config.trophyslots.unlockAchieve"));
        TrophySlots.canUseTrophy = config.getBoolean("canUseTrophy", Configuration.CATEGORY_GENERAL, true, translate("config.trophyslots.canUseTrophy"));
        TrophySlots.canBuyTrophy = config.getBoolean("canBuyTrophies", Configuration.CATEGORY_GENERAL, false, translate("config.trophyslots.canBuyTrophy"));
        TrophySlots.disable3 = config.getBoolean("disableFirst3", Configuration.CATEGORY_GENERAL, false, translate("config.trophyslots.disable3"));
        TrophySlots.checkForUpdates = config.getBoolean("checkForUpdates", Configuration.CATEGORY_GENERAL, true, translate("config.trophyslots.update"));
        TrophySlots.useWhiteList = config.getBoolean("useWhitelist", Configuration.CATEGORY_GENERAL, false, translate("config.trophyslots.useWhitelist"));
        TrophySlots.slotRenderType = config.getInt("slotRenderType", Configuration.CATEGORY_GENERAL, 0, 0, 4, translate("config.trophyslots.renderLockedSlots"));
        TrophySlots.proxy.setReverse(config.getBoolean("reverseUnlock", Configuration.CATEGORY_GENERAL, false, translate("config.trophyslots.reverse")));
        String whiteList = config.getString("WhiteList", Configuration.CATEGORY_GENERAL, "", translate("config.trophyslots.whitelist"));

        TrophySlots.xmas = config.getBoolean("xmas", Configuration.CATEGORY_GENERAL, true, "");

        if (TrophySlots.useWhiteList)
            fillWhitelist(whiteList);
        if (TrophySlots.useBlackList)
            fillBlackList("");
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

    public void fillBlackList(String blackList) {
        if (TrophySlots.achievementBlackList == null)
            TrophySlots.achievementBlackList = new ArrayList<String>();
        if (!TrophySlots.achievementBlackList.isEmpty())
            TrophySlots.achievementBlackList.clear();

        String[] achievementIDs = blackList.split(";");
        if (achievementIDs != null && achievementIDs.length > 0) {
            for (String id : achievementIDs)
                TrophySlots.achievementBlackList.add(id);
        }
    }

    public String translate(String st) {
        return StatCollector.translateToLocal(st);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.modID.equalsIgnoreCase(TrophySlots.MOD_ID))
            TrophySlots.modConfig.loadConfig();
    }
}
