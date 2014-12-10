package net.lomeli.trophyslots.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;

import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import net.lomeli.trophyslots.TrophySlots;
import net.lomeli.trophyslots.core.SimpleUtil;

public class EventHandler {

    //Handles awarding slots on achievement unlocks, if that method is enabled
    @SubscribeEvent
    public void achievementGetEvent(AchievementEvent event) {
        if (event.isCanceled() || event.entityPlayer.worldObj.isRemote)
            return;
        EntityPlayerMP playerMP = (EntityPlayerMP) event.entityPlayer;
        if (!playerMP.getStatFile().hasAchievementUnlocked(event.achievement) && TrophySlots.unlockViaAchievements) {
            if (playerMP.getStatFile().canUnlockAchievement(event.achievement) && event.achievement != TrophySlots.firstSlot && event.achievement != TrophySlots.maxCapcity) {
                if (TrophySlots.disable3 ? !(event.achievement == AchievementList.openInventory || event.achievement == AchievementList.mineWood || event.achievement == AchievementList.buildWorkBench) : true)
                    SimpleUtil.unlockSlot(event.entityPlayer);
            }
        }
    }

    // Drops items if they somehow got into a locked slot.
    @SubscribeEvent
    public void playerTickEvent(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (event.phase == TickEvent.Phase.END && !player.worldObj.isRemote && !player.capabilities.isCreativeMode&& !SimpleUtil.hasAllSlotsUnlocked(player)) {
            for (int i = 0; i < player.inventory.getSizeInventory() - 4; i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack != null && stack.getItem() != null) {
                    if (!SimpleUtil.slotUnlocked(i, player)) {
                        player.entityDropItem(stack, 0);
                        player.inventory.setInventorySlotContents(i, null);
                    }
                }
            }
        }
    }

    // Prevents items from being picked up if there are not empty unlocked slots
    @SubscribeEvent
    public void itemPickupEvent(EntityItemPickupEvent event) {
        if (!event.entityPlayer.worldObj.isRemote && !event.entityPlayer.capabilities.isCreativeMode && !SimpleUtil.hasAllSlotsUnlocked(event.entityPlayer)) {
            ItemStack stack = event.item.getEntityItem();
            if (stack != null && stack.getItem() != null && stack.stackSize > 0) {
                int slot = SimpleUtil.searchForPossibleSlot(stack, event.entityPlayer.inventory);
                event.setCanceled(slot == -1 || !SimpleUtil.slotUnlocked(slot, event.entityPlayer));
            }
        }
    }
}
