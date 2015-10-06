package net.lomeli.trophyslots.core.handler

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.core.SlotUtil
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.stats.AchievementList
import net.minecraftforge.event.entity.player.AchievementEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

public class EventHandlerServer {
    public fun searchForPossibleSlot(stack: ItemStack, player: EntityPlayer) : Int{
        if (player != null) {
            val inventoryPlayer = player.inventory
            var i = 0;
            while (i < inventoryPlayer.sizeInventory - 4) {
                var item = inventoryPlayer.getStackInSlot(i)
                if (SlotUtil.slotUnlocked(player, i)) {
                    if (item == null || item.item == null)
                        return i
                    else if (doStackMatch(stack, item) && (item.stackSize + stack.stackSize) < item.maxStackSize)
                        return i
                }
                i++
            }
        }
        return -1
    }

    public fun findNextEmptySlot(player: EntityPlayer) : Int {
        if (player != null) {
            var i = 0
            while (i < player.inventory.sizeInventory - 4) {
                var item = player.inventory.getStackInSlot(i)
                if (item == null && SlotUtil.slotUnlocked(player, i))
                    return i
                i++
            }
        }
        return -1
    }

    public fun doStackMatch(stack1: ItemStack, stack2: ItemStack) : Boolean {
        var flag = false;
        if (stack1 != null && stack1.item != null && stack2 != null && stack2.item != null)
            flag = stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2)
        return flag
    }

    @SubscribeEvent public fun achievementGetEvent(event:AchievementEvent) {
        if (!event.isCanceled && !event.entityPlayer.worldObj.isRemote) {
            var player = FMLCommonHandler.instance().minecraftServerInstance.worldServerForDimension(event.entityPlayer.dimension).getPlayerEntityByUUID(event.entityPlayer.uniqueID);
            if (player != null && player is EntityPlayerMP) {
                if (!player.statFile.hasAchievementUnlocked(event.achievement) && TrophySlots.unlockViaAchievements && !SlotUtil.hasUnlockedAllSlots(player)) {
                    if (player.statFile.canUnlockAchievement(event.achievement) && event.achievement != TrophySlots.firstSlot && event.achievement != TrophySlots.maxCapcity) {
                        if (TrophySlots.disable3 && (event.achievement == AchievementList.openInventory || event.achievement == AchievementList.mineWood || event.achievement == AchievementList.buildWorkBench))
                            return
                        if (TrophySlots.useWhiteList) {
                            if (TrophySlots.proxy.getWhiteList().contains(event.achievement))
                                TrophySlots.proxy.unlockSlot(player)
                        } else
                            TrophySlots.proxy.unlockSlot(player)
                    }
                }
            }
        }
    }

    @SubscribeEvent public fun playerTickEvent(event: TickEvent.PlayerTickEvent) {
        var player = event.player
        if (player != null && !player.worldObj.isRemote && !player.capabilities.isCreativeMode && !SlotUtil.hasUnlockedAllSlots(player) && event.phase ==TickEvent.Phase.END) {
            var i = 0
            while (i < player.inventory.sizeInventory - 4) {
                var stack = player.inventory.getStackInSlot(i)
                if (stack != null && stack.item != null) {
                    if (!SlotUtil.slotUnlocked(player, i)) {
                        var slot = findNextEmptySlot(player)
                        if (slot <= -1) {
                            player.entityDropItem(stack, 0f)
                            player.inventory.setInventorySlotContents(i, null)
                        } else
                            player.inventory.setInventorySlotContents(slot, player.inventory.getStackInSlotOnClosing(i))
                    }
                }
            }
        }
    }

    @SubscribeEvent public fun itemPickupEvent(event: EntityItemPickupEvent) {
        if (!event.entityPlayer.worldObj.isRemote && !event.entityPlayer.capabilities.isCreativeMode && !SlotUtil.hasUnlockedAllSlots(event.entityPlayer)) {

        }
    }
}