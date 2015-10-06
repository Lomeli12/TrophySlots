package net.lomeli.trophyslots.client

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.core.ModItems
import net.lomeli.trophyslots.core.Proxy
import net.lomeli.trophyslots.core.SlotUtil
import net.minecraft.client.Minecraft

public class ClientProxy : Proxy() {
    private var slotsUnlocked = 0

    override fun preInit() {
        super.preInit()
    }

    override fun init() {
        super.init()
        Minecraft.getMinecraft().renderItem.itemModelMesher.register(ModItems.trophy, BasicItemMesh("${TrophySlots.MOD_ID}:trophy"));
    }

    override fun postInit() {
        super.postInit()
    }

    override fun getSlotsUnlocked(): Int = slotsUnlocked

    override fun setSlotsUnlocked(i0: Int) {
        slotsUnlocked = i0
    }

    override fun slotUnlocked(slot: Int): Boolean {
        if (unlockReverse() && slot >= 9) {
            if (slot < 36)
                return slot > 44 - (TrophySlots.proxy!!.startingSlots + slotsUnlocked)
            return true
        }
        if (slot < 36)
            return slot < TrophySlots.proxy!!.startingSlots + slotsUnlocked
        return true
    }

    override fun hasUnlockedAllSlots(): Boolean = slotsUnlocked >= SlotUtil.getMaxSlots()

    override fun reset() {
        slotsUnlocked = 0
    }

    override fun resetConfig() {
        TrophySlots.modConfig?.loadConfig()
    }

    override fun openWhitelistGui() {
        //super.openWhitelistGui()
    }
}