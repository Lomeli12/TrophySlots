package net.lomeli.trophyslots.core

import net.lomeli.trophyslots.TrophySlots
import net.minecraft.util.text.translation.I18n
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.io.File

class Config {
    val config: Configuration

    constructor(file: File) {
        config = Configuration(file)
    }

    fun loadConfig() {
        TrophySlots.proxy?.startingSlots = config.getInt("startingSlots", Configuration.CATEGORY_GENERAL, 9, 0, 36, translate("config.trophyslots.starting_slots"))
        //TrophySlots.unlockViaAdvancements = config.getBoolean("unlockViaAdvancements", Configuration.CATEGORY_GENERAL, true, translate("config.trophyslots.unlock_via_advancements"))
        TrophySlots.canUseTrophy = config.getBoolean("canUseTrophy", Configuration.CATEGORY_GENERAL, true, translate("config.trophyslots.can_use_trophy"))
        TrophySlots.canBuyTrophy = config.getBoolean("canBuyTrophies", Configuration.CATEGORY_GENERAL, false, translate("config.trophyslots.can_buy_trophy"))
        TrophySlots.checkForUpdates = config.getBoolean("checkForUpdates", Configuration.CATEGORY_GENERAL, true, translate("config.trophyslots.update"))
        TrophySlots.slotRenderType = config.getInt("slotRenderType", Configuration.CATEGORY_GENERAL, 0, 0, 4, translate("config.trophyslots.render_locked_slots"))
        TrophySlots.proxy?.setReverse(config.getBoolean("reverseUnlock", Configuration.CATEGORY_GENERAL, false, translate("config.trophyslots.reverse")))
        TrophySlots.loseSlots = config.getBoolean("loseSlotsOnDeath", Configuration.CATEGORY_GENERAL, false, translate("config.trophyslots.lose_slots"))
        TrophySlots.loseSlotNum = config.getInt("slotsLost", Configuration.CATEGORY_GENERAL, 1, -1, 36, translate("config.trophyslots.lose_slots.num"))
        TrophySlots.useProgressionUnlocks = config.getBoolean("useProgressionUnlocks", Configuration.CATEGORY_GENERAL, true, translate("config.trophyslots.progression"))

        if (config.hasChanged())
            config.save()
    }

    fun translate(st: String): String = I18n.translateToLocal(st)

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun onConfigChange(event: ConfigChangedEvent.OnConfigChangedEvent) {
        if (event.modID.equals(TrophySlots.MOD_ID))
            TrophySlots.modConfig?.loadConfig()
    }
}