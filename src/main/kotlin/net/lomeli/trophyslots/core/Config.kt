package net.lomeli.trophyslots.core

import net.lomeli.trophyslots.TrophySlots
import net.minecraft.util.StatCollector
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.io.File

public class Config {
    val config: Configuration

    constructor(file: File) {
        config = Configuration(file)
    }

    public fun loadConfig() {
        TrophySlots.proxy?.startingSlots = config.getInt("startingSlots", Configuration.CATEGORY_GENERAL, 9, 0, 36, translate("config.trophyslots.startingSlots"));
        TrophySlots.unlockViaAchievements = config.getBoolean("unlockViaAchievements", Configuration.CATEGORY_GENERAL, true, translate("config.trophyslots.unlockAchieve"));
        TrophySlots.canUseTrophy = config.getBoolean("canUseTrophy", Configuration.CATEGORY_GENERAL, true, translate("config.trophyslots.canUseTrophy"));
        TrophySlots.canBuyTrophy = config.getBoolean("canBuyTrophies", Configuration.CATEGORY_GENERAL, false, translate("config.trophyslots.canBuyTrophy"));
        TrophySlots.disable3 = config.getBoolean("disableFirst3", Configuration.CATEGORY_GENERAL, false, translate("config.trophyslots.disable3"));
        TrophySlots.checkForUpdates = config.getBoolean("checkForUpdates", Configuration.CATEGORY_GENERAL, true, translate("config.trophyslots.update"));
        TrophySlots.useWhiteList = config.getBoolean("useWhitelist", Configuration.CATEGORY_GENERAL, false, translate("config.trophyslots.useWhitelist"));
        TrophySlots.slotRenderType = config.getInt("slotRenderType", Configuration.CATEGORY_GENERAL, 0, 0, 4, translate("config.trophyslots.renderLockedSlots"));
        TrophySlots.proxy?.setReverse(config.getBoolean("reverseUnlock", Configuration.CATEGORY_GENERAL, false, translate("config.trophyslots.reverse")));
        TrophySlots.loseSlots = config.getBoolean("loseSlotsOnDeath", Configuration.CATEGORY_GENERAL, false, translate("config.trophyslots.loseSlots"));
        TrophySlots.loseSlotNum = config.getInt("slotsLost", Configuration.CATEGORY_GENERAL, 1, -1, 36, translate("config.trophyslots.loseSlots.num"));
        var whiteList = config.getString("WhiteList", Configuration.CATEGORY_GENERAL, "", translate("config.trophyslots.whitelist"));

        if (TrophySlots.useWhiteList)
            fillWhiteList(whiteList)
        if (config.hasChanged())
            config.save()
    }

    public fun fillWhiteList(whiteList: String) {
        var achievementIDs = whiteList.split(";")
        if (achievementIDs.size() > 0)
            TrophySlots.proxy?.setWhiteList(achievementIDs)
    }

    public fun translate(st: String): String = StatCollector.translateToLocal(st)

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public fun onConfigChange(event: ConfigChangedEvent.OnConfigChangedEvent) {
        if (event.modID.equals(TrophySlots.MOD_ID))
            TrophySlots.modConfig?.loadConfig()
    }
}