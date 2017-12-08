package net.lomeli.trophyslots.core

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.capabilities.slots.SlotManager
import net.lomeli.trophyslots.core.triggers.AllTriggers
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.common.registry.VillagerRegistry

open class Proxy {
    protected var reverseOrder = false
    var startingSlots = 9

    open fun preInit() {
        TrophySlots.log?.logInfo("Pre-Init")
        AllTriggers.registerTriggers()
        addTrades()
        MinecraftForge.EVENT_BUS.register(SlotManager)
    }

    fun addTrades() {
        val registry = ForgeRegistries.VILLAGER_PROFESSIONS
        val librarianPro: VillagerRegistry.VillagerProfession? = registry.getValue(ResourceLocation("minecraft", "librarian"))
        val librarian: VillagerRegistry.VillagerCareer? = librarianPro!!.getCareer(0)
        librarian!!.addTrade(4, TrophyTrade())
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