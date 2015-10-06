package net.lomeli.trophyslots.core

import com.google.common.collect.Lists
import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.core.network.MessageSlotsClient
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.ChatComponentTranslation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.FMLCommonHandler

public open class Proxy {
    protected var reverseOrder = false
    public var startingSlots = 9
    protected var achievementWhiteList: List<String> = Lists.newArrayList()

    public open fun preInit() {
        ModItems.registerItems()
        //TODO: Villager stuff once VillagerRegistry is back
    }

    public open fun init() {

    }

    public open fun postInit() {

    }

    public fun unlockReverse(): Boolean {
        return reverseOrder
    }

    public fun setReverse(bool: Boolean) {
        reverseOrder = bool
    }

    protected fun registerFMLEvent(obj: Object) {
        FMLCommonHandler.instance().bus().register(obj)
    }

    protected fun registerForgeEvent(obj: Object) {
        MinecraftForge.EVENT_BUS.register(obj)
    }

    public fun getWhiteList(): List<String> {
        if (achievementWhiteList == null)
            achievementWhiteList = Lists.newArrayList()
        return achievementWhiteList
    }

    public fun setWhiteList(list: List<String>) {
        achievementWhiteList = list;
    }

    public fun unlockSlot(player: EntityPlayer): Boolean {
        if (player != null && !SlotUtil.hasUnlockedAllSlots(player)) {
            var i = SlotUtil.getSlotsUnlocked(player) + 1
            SlotUtil.setSlotsUnlocked(player, i)
            if (i >= SlotUtil.getMaxSlots())
                player.addChatComponentMessage(ChatComponentTranslation("msg.trophyslots.unlockAll"))
            else
                player.addChatComponentMessage(ChatComponentTranslation("msg.trophyslots.unlock"))
            if (player is EntityPlayerMP)
                sendMessageSlotClient(player, i)
            else {
                var tempPlayer = FMLCommonHandler.instance().minecraftServerInstance.worldServerForDimension(player.dimension).getPlayerEntityByUUID(player.uniqueID)
                if (tempPlayer is EntityPlayerMP)
                    sendMessageSlotClient(tempPlayer, i)
            }
            return true
        }
        return false
    }

    private fun sendMessageSlotClient(mp: EntityPlayerMP, slots: Int) {
        if (mp != null) {
            if (slots > 0 && !mp.statFile.hasAchievementUnlocked(TrophySlots.firstSlot) && mp.statFile.canUnlockAchievement(TrophySlots.firstSlot))
                mp.addStat(TrophySlots.firstSlot, 1);
            if (slots >= SlotUtil.getMaxSlots() && !mp.statFile.hasAchievementUnlocked(TrophySlots.maxCapcity) && mp.statFile.canUnlockAchievement(TrophySlots.maxCapcity))
                mp.addStat(TrophySlots.maxCapcity, 1);
            TrophySlots.packetHandler.sendTo(MessageSlotsClient(slots), mp)
        }
    }

    public fun unlockAllSlots(player: EntityPlayer): Boolean {
        if (player != null && !SlotUtil.hasUnlockedAllSlots(player)) {
            SlotUtil.setSlotsUnlocked(player, SlotUtil.getMaxSlots())
            player.addChatComponentMessage(ChatComponentTranslation("msg.trophyslots.unlockAll"))
            if (player is EntityPlayerMP)
                sendMessageSlotClient(player, SlotUtil.getMaxSlots())
            else {
                var tempPlayer = FMLCommonHandler.instance().minecraftServerInstance.worldServerForDimension(player.dimension).getPlayerEntityByUUID(player.uniqueID)
                if (tempPlayer is EntityPlayerMP)
                    sendMessageSlotClient(tempPlayer, SlotUtil.getMaxSlots())
            }
            return true
        }
        return false;
    }

    public open fun getSlotsUnlocked(): Int {
        return 0
    }

    public open fun setSlotsUnlocked(i0: Int) {
    }

    public open fun slotUnlocked(slot: Int): Boolean {
        return false
    }

    public open fun hasUnlockedAllSlots(): Boolean {
        return false
    }

    public open fun reset() {

    }

    public open fun resetConfig() {

    }

    public open fun openWhitelistGui() {
    }
}