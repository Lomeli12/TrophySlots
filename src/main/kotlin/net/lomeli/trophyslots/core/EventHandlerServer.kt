package net.lomeli.trophyslots.core

import net.lomeli.trophyslots.TrophySlots
import net.lomeli.trophyslots.capabilities.slots.SlotManager
import net.lomeli.trophyslots.core.triggers.AllTriggers
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.translation.I18n
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.AdvancementEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

@Mod.EventBusSubscriber(modid = TrophySlots.MOD_ID) object EventHandlerServer {
    @JvmStatic fun searchForPossibleSlot(stack: ItemStack, player: EntityPlayer): Int {
        val slotInfo = SlotManager.getPlayerSlotInfo(player)!!
        for (i in player.inventory.mainInventory.indices) {
            val item = player.inventory.getStackInSlot(i)
            if (slotInfo.slotUnlocked(i)) {
                if (item.isEmpty)
                    return i
                else if (doStackMatch(stack, item) && (item.count + stack.count) < item.maxStackSize)
                    return i
            }
        }
        return -1
    }

    @JvmStatic fun findNextEmptySlot(player: EntityPlayer): Int {
        val slotInfo = SlotManager.getPlayerSlotInfo(player)!!
        for (i in player.inventory.mainInventory.indices) {
            val item = player.inventory.getStackInSlot(i)
            if (item.isEmpty && slotInfo.slotUnlocked(i))
                return i
        }
        return -1
    }

    @JvmStatic fun doStackMatch(stack1: ItemStack, stack2: ItemStack): Boolean {
        var flag = false
        if (!stack1.isEmpty && !stack2.isEmpty)
            flag = stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2)
        return flag
    }

    @SubscribeEvent @JvmStatic fun advancementEvent(event: AdvancementEvent) {
        if (!TrophySlots.unlockViaAdvancements) return
        val player = event.entityPlayer
        if (player.capabilities.isCreativeMode) return
        val slotInfo = SlotManager.getPlayerSlotInfo(player) ?: return
        if (slotInfo.isAtMaxSlots()) return
        if (slotInfo.unlockSlot(1)) {
            player.sendStatusMessage(TextComponentTranslation("msg.trophyslots.unlock"), true)
            SlotManager.updateClient(player, slotInfo)
            if (player is EntityPlayerMP) AllTriggers.UNLOCK_SLOT.trigger(player)
        }

    }


    @SubscribeEvent @JvmStatic fun playerTickEvent(event: TickEvent.PlayerTickEvent) {
        if (event.player == null || event.phase != TickEvent.Phase.END) return
        val player = event.player
        if (player.capabilities.isCreativeMode) return
        val slotInfo = SlotManager.getPlayerSlotInfo(player)!!
        if (slotInfo.isAtMaxSlots()) return
        for (i in player.inventory.mainInventory.indices) {
            val stack = player.inventory.getStackInSlot(i)
            if (stack.isEmpty || slotInfo.slotUnlocked(i)) continue
            var slot = findNextEmptySlot(player)
            if (slot <= -1) {
                if (!player.world.isRemote)
                    player.entityDropItem(stack, 0f)
                player.inventory.setInventorySlotContents(i, ItemStack.EMPTY)
            } else
                player.inventory.setInventorySlotContents(slot, player.inventory.removeStackFromSlot(i))
        }
    }

    @SubscribeEvent @JvmStatic fun itemPickupEvent(event: EntityItemPickupEvent) {
        val slotInfo = SlotManager.getPlayerSlotInfo(event.entityPlayer)!!
        if (event.entityPlayer.capabilities.isCreativeMode || slotInfo.isAtMaxSlots()) return
        val stack = event.item.item
        if (stack.isEmpty) return
        val slot = searchForPossibleSlot(stack, event.entityPlayer)
        event.isCanceled = (slot == -1 || !slotInfo.slotUnlocked(slot))
    }

    @SubscribeEvent @JvmStatic fun playerDeath(event: LivingDeathEvent) {
        if (event.entityLiving == null && !TrophySlots.loseSlots) return
        if (event.entityLiving is EntityPlayer) {
            val player = FMLCommonHandler.instance().minecraftServerInstance.playerList.getPlayerByUUID(event.entityLiving.uniqueID) as EntityPlayerMP
            val slotInfo = SlotManager.getPlayerSlotInfo(player)!!
            var slots = slotInfo.getSlotsUnlocked()
            if (slots > 0) {
                if (TrophySlots.loseSlotNum == -1)
                    slots -= slots
                else
                    slots = TrophySlots.loseSlotNum
                slotInfo.setSlots(slots)
                SlotManager.updateClient(player, slotInfo)
                if (TrophySlots.loseSlotNum == -1)
                    player.sendStatusMessage(TextComponentTranslation("msg.trophyslots.lost_all"), true)
                else
                    player.sendStatusMessage(TextComponentString(I18n.translateToLocal("msg.trophyslots.lost_slot").format(TrophySlots.loseSlotNum)), true)
            }
        }
    }

    @SubscribeEvent @JvmStatic fun registerItems(event: RegistryEvent.Register<Item>) {
        event.registry.register(TrophySlots.TROPHY)
    }
}