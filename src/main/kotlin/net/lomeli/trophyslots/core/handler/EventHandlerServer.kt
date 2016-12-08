package net.lomeli.trophyslots.core.handler

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.core.SlotUtil
import net.lomeli.trophyslots.core.network.MessageSlotsClient
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.stats.AchievementList
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.translation.I18n
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.AchievementEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

class EventHandlerServer {
    fun searchForPossibleSlot(stack: ItemStack, player: EntityPlayer): Int {
        for (i in player.inventory.mainInventory.indices) {
            val item = player.inventory.getStackInSlot(i)
            if (SlotUtil.slotUnlocked(player, i)) {
                if (item == null || item.item == null)
                    return i
                else if (doStackMatch(stack, item) && (item.stackSize + stack.stackSize) < item.maxStackSize)
                    return i
            }
        }
        return -1
    }

    fun findNextEmptySlot(player: EntityPlayer): Int {
        for (i in player.inventory.mainInventory.indices) {
            val item = player.inventory.getStackInSlot(i)
            if (item == null && SlotUtil.slotUnlocked(player, i))
                return i
        }
        return -1
    }

    fun doStackMatch(stack1: ItemStack, stack2: ItemStack): Boolean {
        var flag = false;
        if (stack1.item != null && stack2.item != null)
            flag = stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2)
        return flag
    }

    @SubscribeEvent fun achievementGetEvent(event: AchievementEvent) {
        if (!event.isCanceled && !event.entityPlayer.worldObj.isRemote && FMLCommonHandler.instance().minecraftServerInstance != null) {
            val player = FMLCommonHandler.instance().minecraftServerInstance.worldServerForDimension(event.entityPlayer.dimension).getPlayerEntityByUUID(event.entityPlayer.uniqueID) as EntityPlayerMP
            if (!player.statFile.hasAchievementUnlocked(event.achievement) && TrophySlots.unlockViaAchievements && !SlotUtil.hasUnlockedAllSlots(player)) {
                if (player.statFile.canUnlockAchievement(event.achievement) && event.achievement != TrophySlots.firstSlot && event.achievement != TrophySlots.maxCapcity) {
                    if (TrophySlots.disable3 && (event.achievement == AchievementList.OPEN_INVENTORY || event.achievement == AchievementList.MINE_WOOD || event.achievement == AchievementList.BUILD_WORK_BENCH))
                        return
                    if (TrophySlots.useWhiteList) {
                        if (TrophySlots.proxy!!.getWhiteList().contains(event.achievement.toString()))
                            TrophySlots.proxy?.unlockSlot(player)
                    } else
                        TrophySlots.proxy?.unlockSlot(player)
                }
            }
        }
    }

    @SubscribeEvent fun playerTickEvent(event: TickEvent.PlayerTickEvent) {
        val player = event.player
        if (player != null && !player.worldObj.isRemote && !player.capabilities.isCreativeMode && !SlotUtil.hasUnlockedAllSlots(player) && event.phase == TickEvent.Phase.END) {
            for (i in player.inventory.mainInventory.indices) {
                val stack = player.inventory.getStackInSlot(i)
                if (stack != null && stack.item != null) {
                    if (!SlotUtil.slotUnlocked(player, i)) {
                        var slot = findNextEmptySlot(player)
                        if (slot <= -1) {
                            player.entityDropItem(stack, 0f)
                            player.inventory.setInventorySlotContents(i, null)
                        } else
                            player.inventory.setInventorySlotContents(slot, player.inventory.removeStackFromSlot(i))
                    }
                }
            }
        }
    }

    @SubscribeEvent fun itemPickupEvent(event: EntityItemPickupEvent) {
        if (!event.entityPlayer.worldObj.isRemote && !event.entityPlayer.capabilities.isCreativeMode && !SlotUtil.hasUnlockedAllSlots(event.entityPlayer)) {
            val stack = event.item.entityItem
            if (stack != null && stack.item != null && stack.stackSize > 0) {
                val slot = searchForPossibleSlot(stack, event.entityPlayer)
                event.isCanceled = (slot == -1 || !SlotUtil.slotUnlocked(event.entityPlayer, slot));
            }
        }
    }

    @SubscribeEvent fun playerJoinedServer(event: EntityJoinWorldEvent) {
        if (!event.world.isRemote && event.entity != null && event.entity is EntityPlayer) {
            val player = FMLCommonHandler.instance().minecraftServerInstance.worldServerForDimension(event.entity.dimension).getPlayerEntityByUUID(event.entity.uniqueID) as EntityPlayerMP
            if (SlotUtil.getSlotsUnlocked(player) > 0)
                TrophySlots.packetHandler?.sendTo(MessageSlotsClient(SlotUtil.getSlotsUnlocked(player), TrophySlots.proxy!!.unlockReverse(), TrophySlots.proxy!!.startingSlots), player)
        }
    }

    @SubscribeEvent fun playerDeath(event: LivingDeathEvent) {
        if (event.entityLiving != null && !event.entityLiving.worldObj.isRemote && TrophySlots.loseSlots) {
            if (event.entityLiving is EntityPlayer) {
                val player = FMLCommonHandler.instance().minecraftServerInstance.worldServerForDimension(event.entityLiving.dimension).getPlayerEntityByUUID(event.entityLiving.uniqueID) as EntityPlayerMP
                var slots = SlotUtil.getSlotsUnlocked(player)
                if (slots > 0) {
                    if (TrophySlots.loseSlotNum == -1)
                        slots -= slots
                    else
                        slots = TrophySlots.loseSlotNum
                    SlotUtil.setSlotsUnlocked(player, slots)
                    TrophySlots.packetHandler?.sendTo(MessageSlotsClient(slots), player)
                    if (TrophySlots.loseSlotNum == -1)
                        player.addChatMessage(TextComponentTranslation("msg.trophyslots.lostAll"))
                    else
                        player.addChatMessage(TextComponentString(I18n.translateToLocal("msg.trophyslots.lostSlot").format(TrophySlots.loseSlotNum)))
                }
            }
        }
    }
}