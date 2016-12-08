package net.lomeli.trophyslots.core

import com.google.common.collect.Lists
import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.core.handler.EventHandlerServer
import net.lomeli.trophyslots.core.network.MessageSlotsClient
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.FMLCommonHandler

open class Proxy {
    protected var reverseOrder = false
    var startingSlots: Int = 9
    protected var achievementWhiteList: List<String> = Lists.newArrayList()

    open fun preInit() {
        TrophySlots.log?.logInfo("Pre-Init")
        ModItems.registerItems()
        //TODO: Villager stuff once VillagerRegistry is back
    }

    open fun init() {
        TrophySlots.log?.logInfo("Init")
        val eventHandlerServer = EventHandlerServer()
        registerForgeEvent(eventHandlerServer)
        registerForgeEvent(eventHandlerServer)
    }

    open fun postInit() {
        TrophySlots.log?.logInfo("Post-Init")
    }

    fun unlockReverse(): Boolean = reverseOrder

    fun setReverse(bool: Boolean) {
        reverseOrder = bool
    }

    protected fun registerForgeEvent(obj: Any) = MinecraftForge.EVENT_BUS.register(obj)

    fun getWhiteList(): List<String> {
        achievementWhiteList = Lists.newArrayList()
        return achievementWhiteList
    }

    fun setWhiteList(list: List<String>) {
        achievementWhiteList = list;
    }

    fun unlockSlot(player: EntityPlayer): Boolean {
        if (!SlotUtil.hasUnlockedAllSlots(player)) {
            var i = SlotUtil.getSlotsUnlocked(player) + 1
            SlotUtil.setSlotsUnlocked(player, i)
            if (i >= SlotUtil.getMaxSlots())
                player.addChatComponentMessage(TextComponentTranslation("msg.trophyslots.unlockAll"))
            else
                player.addChatComponentMessage(TextComponentTranslation("msg.trophyslots.unlock"))
            if (player is EntityPlayerMP)
                sendMessageSlotClient(player, i)
            else {
                var tempPlayer = FMLCommonHandler.instance().minecraftServerInstance.worldServerForDimension(player.dimension).getPlayerEntityByUUID(player.uniqueID) as EntityPlayerMP
                sendMessageSlotClient(tempPlayer, i)
            }
            return true
        }
        return false
    }

    private fun sendMessageSlotClient(mp: EntityPlayerMP, slots: Int) {
        if (slots > 0 && !mp.statFile.hasAchievementUnlocked(TrophySlots.firstSlot) && mp.statFile.canUnlockAchievement(TrophySlots.firstSlot))
            mp.addStat(TrophySlots.firstSlot, 1);
        if (slots >= SlotUtil.getMaxSlots() && !mp.statFile.hasAchievementUnlocked(TrophySlots.maxCapcity) && mp.statFile.canUnlockAchievement(TrophySlots.maxCapcity))
            mp.addStat(TrophySlots.maxCapcity, 1);
        TrophySlots.packetHandler?.sendTo(MessageSlotsClient(slots), mp)

    }

    fun unlockAllSlots(player: EntityPlayer): Boolean {
        if (!SlotUtil.hasUnlockedAllSlots(player)) {
            SlotUtil.setSlotsUnlocked(player, SlotUtil.getMaxSlots())
            player.addChatComponentMessage(TextComponentTranslation("msg.trophyslots.unlockAll"))
            if (player is EntityPlayerMP)
                sendMessageSlotClient(player, SlotUtil.getMaxSlots())
            else {
                var tempPlayer = FMLCommonHandler.instance().minecraftServerInstance.worldServerForDimension(player.dimension).getPlayerEntityByUUID(player.uniqueID) as EntityPlayerMP
                sendMessageSlotClient(tempPlayer, SlotUtil.getMaxSlots())
            }
            return true
        }
        return false;
    }

    open fun getSlotsUnlocked(): Int = 0

    open fun setSlotsUnlocked(i0: Int) {
    }

    open fun slotUnlocked(slot: Int): Boolean = false

    open fun hasUnlockedAllSlots(): Boolean = false

    open fun reset() {

    }

    open fun resetConfig() {

    }

    open fun openWhitelistGui() {
    }
}