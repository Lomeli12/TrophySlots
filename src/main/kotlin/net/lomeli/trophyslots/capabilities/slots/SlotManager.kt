package net.lomeli.trophyslots.capabilities.slots

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.core.network.MessageSlotsClient
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object SlotManager {
    @JvmStatic val PLAYER_SLOTS = ResourceLocation(TrophySlots.MOD_ID, "slots_trophy_slots")

    fun getPlayerSlotInfo(player: EntityPlayer?): ISlotInfo? {
        if (player == null) return null
        return player.getCapability(SlotProvider.SLOT_INFO!!, null) as ISlotInfo
    }

    fun updateClient(player: EntityPlayer, slotInfo: ISlotInfo) {
        val playerMP = FMLCommonHandler.instance().minecraftServerInstance.playerList.getPlayerByUUID(player.uniqueID)
        TrophySlots.packetHandler?.sendTo(MessageSlotsClient(slotInfo.getSlotsUnlocked(), TrophySlots.proxy!!.unlockReverse(), TrophySlots.proxy!!.startingSlots), playerMP)
    }

    @SubscribeEvent fun attachProgressionCapability(event: AttachCapabilitiesEvent<Entity>) {
        if (event.`object` !is EntityPlayer) return
        event.addCapability(PLAYER_SLOTS, SlotProvider())
    }

    @SubscribeEvent fun restoreProgressionAfterDeath(event: PlayerEvent.Clone) {
        val slotInfo = getPlayerSlotInfo(event.entityPlayer)!!
        val oldSlotInfo = getPlayerSlotInfo(event.original)!!
        slotInfo.setSlots(oldSlotInfo.getSlotsUnlocked())
    }

    @SubscribeEvent fun playerJoinedServer(event: net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent) {
        if (event.player == null) return
        val slotInfo = getPlayerSlotInfo(event.player)
        if (slotInfo != null && slotInfo.getSlotsUnlocked() > 0) updateClient(event.player, slotInfo)
    }
}