package net.lomeli.trophyslots.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.SlotUtil;
import net.lomeli.trophyslots.core.network.MessageSlotsClient;

public class EventHandlerServer {
    public int searchForPossibleSlot(ItemStack stack, InventoryPlayer inventoryPlayer) {
        if (TrophySlots.reverse) {
            for (int i = inventoryPlayer.getSizeInventory() - 5; i > 0; i--) {
                ItemStack item = inventoryPlayer.getStackInSlot(i);
                if (item == null || item.getItem() == null)
                    return i;
                else if (doStackMatch(stack, item) && (item.stackSize + stack.stackSize) < item.getMaxStackSize())
                    return i;
            }
        }
        for (int i = 0; i < inventoryPlayer.getSizeInventory() - 4; i++) {
            ItemStack item = inventoryPlayer.getStackInSlot(TrophySlots.reverse ? 35 - i : i);
            if (item == null || item.getItem() == null)
                return i;
            else if (doStackMatch(stack, item) && (item.stackSize + stack.stackSize) < item.getMaxStackSize())
                return i;
        }
        return -1;
    }

    public int findNextEmptySlot(ItemStack stack, EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory() - 4; i++) {
            ItemStack item = player.inventory.getStackInSlot(i);
            if (item == null && SlotUtil.slotUnlocked(player, i))
                return i;
        }
        return -1;
    }

    public boolean doStackMatch(ItemStack stack1, ItemStack stack2) {
        boolean flag = false;
        if (stack1 != null && stack1.getItem() != null && stack2 != null && stack2.getItem() != null)
            flag = stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
        return flag;
    }

    //Handles awarding slots on achievement unlocks, if that method is enabled
    @SubscribeEvent
    public void achievementGetEvent(AchievementEvent event) {
        if (!event.isCanceled() && !event.entityPlayer.worldObj.isRemote) {
            EntityPlayerMP playerMP = (EntityPlayerMP) FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(event.entityPlayer.dimension).func_152378_a(event.entityPlayer.getUniqueID());
            if (playerMP != null && !playerMP.func_147099_x().hasAchievementUnlocked(event.achievement) && TrophySlots.unlockViaAchievements && !SlotUtil.hasUnlockedAllSlots(playerMP)) {
                if (playerMP.func_147099_x().canUnlockAchievement(event.achievement) && event.achievement != TrophySlots.firstSlot && event.achievement != TrophySlots.maxCapcity) {
                    if (TrophySlots.disable3 ? (event.achievement == AchievementList.openInventory || event.achievement == AchievementList.mineWood || event.achievement == AchievementList.buildWorkBench) : false)
                        return;
                    if (TrophySlots.useWhiteList ? TrophySlots.achievementWhiteList.contains(event.achievement.statId) : true)
                        TrophySlots.proxy.unlockSlot(playerMP);
                }
            }
        }
    }

    // Drops items if they somehow got into a locked slot.
    @SubscribeEvent
    public void playerTickEvent(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (event.phase == TickEvent.Phase.END && !player.worldObj.isRemote && !player.capabilities.isCreativeMode && !SlotUtil.hasUnlockedAllSlots(player)) {
            for (int i = 0; i < player.inventory.getSizeInventory() - 4; i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack != null && stack.getItem() != null) {
                    if (!SlotUtil.slotUnlocked(player, i)) {
                        int slot = TrophySlots.reverse ? findNextEmptySlot(stack, player) : -1;
                        if (slot <= -1) {
                            player.entityDropItem(stack, 0);
                            player.inventory.setInventorySlotContents(i, null);
                        } else
                            player.inventory.setInventorySlotContents(slot, player.inventory.getStackInSlotOnClosing(i));
                    }
                }
            }
        }
    }

    // Prevents items from being picked up if there are not empty unlocked slots
    @SubscribeEvent
    public void itemPickupEvent(EntityItemPickupEvent event) {
        if (!event.entityPlayer.worldObj.isRemote && !event.entityPlayer.capabilities.isCreativeMode && !SlotUtil.hasUnlockedAllSlots(event.entityPlayer)) {
            ItemStack stack = event.item.getEntityItem();
            if (stack != null && stack.getItem() != null && stack.stackSize > 0) {
                int slot = searchForPossibleSlot(stack, event.entityPlayer.inventory);
                event.setCanceled(slot == -1 || !SlotUtil.slotUnlocked(event.entityPlayer, slot));
            }
        }
    }

    @SubscribeEvent
    public void playerJoinedServer(EntityJoinWorldEvent event) {
        if (!event.world.isRemote && event.entity != null && event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            if (player != null && SlotUtil.getSlotsUnlocked(player) > 0) {
                EntityPlayerMP mp = (EntityPlayerMP) FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(player.dimension).func_152378_a(player.getUniqueID());
                if (mp != null)
                    TrophySlots.packetHandler.sendTo(new MessageSlotsClient(SlotUtil.getSlotsUnlocked(player)), mp);
            }
        }
    }
}
