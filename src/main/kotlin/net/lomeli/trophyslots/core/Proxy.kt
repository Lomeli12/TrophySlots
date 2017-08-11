package net.lomeli.trophyslots.core

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.capabilities.slots.SlotManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge

open class Proxy {
    protected var reverseOrder = false
    var startingSlots = 9

    open fun preInit() {
        TrophySlots.log?.logInfo("Pre-Init")
        //TODO: Villager stuff once VillagerRegistry is back
        MinecraftForge.EVENT_BUS.register(SlotManager)
    }

    open fun init() {
        TrophySlots.log?.logInfo("Init")
    }

    open fun postInit() {
        TrophySlots.log?.logInfo("Post-Init")
    }

    fun unlockReverse(): Boolean = reverseOrder

    fun setReverse(bool: Boolean) {
        reverseOrder = bool
    }

    open fun resetConfig() {
    }
}